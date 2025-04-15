/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.client.SClientInterface;
import erp.mod.SModConsts;
import erp.mod.cfg.utils.SAuthorizationUtils;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbSupplierFileProcess extends SDbRegistryUser {
    
    protected int mnPkYearId;
    protected int mnPkDocId;
    
    protected SDbDps moDps;
    protected String msDpsStatus;
    protected ArrayList<SDbSupplierFile> maSuppFiles;
    protected ArrayList<SDbSupplierFile> maSuppFilesDeleted;
    protected ArrayList<File> maFilesDeleted;
    protected ArrayList<SDbMaterialRequest> maMaterialRequests;
    
    protected SGuiClient miClient;
    
    public SDbSupplierFileProcess() {
        super(SModConsts.TRNX_SUP_FILE_DPS_PROC);
    }
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    
    public void setDps(SDbDps o) { moDps = o; }
    public void setDpsStatus(String s) { msDpsStatus = s; }
    
    public void setClient(SGuiClient o) { miClient = o; };
    
    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }

    public SDbDps getDps() { return moDps; }
    public String getDpsStatus() { return msDpsStatus; }
    public ArrayList<SDbSupplierFile> getSuppFiles() { return maSuppFiles; }
    public ArrayList<SDbSupplierFile> getSuppFilesDeleted() { return maSuppFilesDeleted; }
    public ArrayList<File> getFilesDeleted() { return maFilesDeleted; }
    public ArrayList<SDbMaterialRequest> getMaterialRequests() { return maMaterialRequests; }
        
    public void updateDpsStatus(SGuiSession session, int stAuth) throws Exception {
        msSql = "UPDATE trn_dps SET fid_st_dps_authorn = " + stAuth + " WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId;
        session.getStatement().getConnection().createStatement().execute(msSql);
    }
    
    public void readMaterialRequests(SGuiSession session) throws Exception {
        maMaterialRequests = new ArrayList<>();
        Statement statement = session.getDatabase().getConnection().createStatement();
        msSql = "SELECT DISTINCT fid_mat_req FROM trn_dps_mat_req WHERE fid_dps_year = " + mnPkYearId + " AND fid_dps_doc = " + mnPkDocId;
        ResultSet resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            SDbMaterialRequest mat = new SDbMaterialRequest();
            mat.read(session, new int[] { resultSet.getInt(1) });
            maMaterialRequests.add(mat);
        }
    }
    
    public int getDpsEntriesWithoutMaterialRequest(SGuiSession session) throws Exception {
        int entries = 0;
        Statement statement = session.getDatabase().getConnection().createStatement();
        msSql = "SELECT COUNT(*) FROM trn_dps_ety de " +
                "LEFT JOIN trn_dps_mat_req dr ON de.id_year = dr.fid_dps_year AND de.id_doc = dr.fid_dps_doc AND de.id_ety = dr.fid_dps_ety " +
                "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND NOT de.b_del AND dr.id_dps_mat_req IS NULL;";
        ResultSet resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            entries = resultSet.getInt(1);
        }
        return entries;
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkDocId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkYearId = 0;
        mnPkDocId = 0;
        
        moDps = null;
        maSuppFiles = new ArrayList<>();
        maSuppFilesDeleted = new ArrayList<>();
        maFilesDeleted = new ArrayList<>();
        maMaterialRequests = new ArrayList<>();
        
        miClient = null;
        
        msDpsStatus = "";
    }

    @Override
    public String getSqlTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_year = " + mnPkYearId + " " +
                "AND id_doc = " + mnPkDocId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_year = " + pk[0] + " " +
                "AND id_doc = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement;
        SDbSupplierFile file;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        moDps = new SDbDps();
        moDps.read(session, pk);
        
        mnPkYearId = moDps.getPkYearId();
        mnPkDocId = moDps.getPkDocId();
        
        statement = session.getDatabase().getConnection().createStatement();
        
        msSql = "SELECT st_dps_authorn FROM erp.trns_st_dps_authorn WHERE id_st_dps_authorn = " + moDps.getFkDpsAuthorizationStatusId();
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            msDpsStatus = resultSet.getString(1);
        }
        
        msSql = "SELECT id_sup_file FROM trn_sup_file_dps " +
                "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId;
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            file = new SDbSupplierFile();
            file.read(session, new int[] { resultSet.getInt(1) });
            file.readSuppFileDps(session, mnPkYearId, mnPkDocId);
            maSuppFiles.add(file);
        }
        
        mbRegistryNew = false;
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        for (SDbSupplierFile file : maSuppFiles) {
            file.save(session);
        }
        
        for (SDbSupplierFile file : maSuppFilesDeleted) {
            file.delete(session);
        }
        
        for (File file : maFilesDeleted) {
            file.delete();
        }
        
        if (miClient != null) {
            SAuthorizationUtils.sendAuthornAppWeb((SClientInterface) miClient, getPrimaryKey());
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbSupplierFileProcess clone() throws CloneNotSupportedException {
        SDbSupplierFileProcess registry = new SDbSupplierFileProcess();
        
        registry.setPkYearId(this.getPkYearId());
        registry.setPkDocId(this.getPkDocId());
        registry.setDps(this.getDps());
        
        for (SDbSupplierFile file : this.getSuppFiles()) {
            registry.getSuppFiles().add(file);
        }
        
        return registry;
    }
    
    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        for (SDbSupplierFile file : maSuppFiles) {
            file.delete(session);
        }
    }
}
