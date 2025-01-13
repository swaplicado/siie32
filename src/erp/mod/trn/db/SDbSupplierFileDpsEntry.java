/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbSupplierFileDpsEntry extends SDbRegistryUser {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected int mnPkSupplierFileId;
    
    public SDbSupplierFileDpsEntry() {
        super(SModConsts.TRN_SUP_FILE_DPS_ETY);
    }
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setPkSupplierFileId(int n) { mnPkSupplierFileId = n; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getPkSupplierFileId() { return mnPkSupplierFileId; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkDocId = pk[1];
        mnPkEntryId = pk[2];
        mnPkSupplierFileId = pk[3];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId, mnPkSupplierFileId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        mnPkSupplierFileId = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_year = " + mnPkYearId + " " +
                "AND id_doc = " + mnPkDocId + " " +
                "AND id_ety = " + mnPkEntryId + " " +
                "AND id_sup_file = " + mnPkSupplierFileId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_year = " + pk[0] + " " +
                "AND id_doc = " + pk[1] + " " +
                "AND id_ety = " + pk[2] + " " +
                "AND id_sup_file = " + pk[3] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
    
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkYearId = resultSet.getInt("id_year");
            mnPkDocId = resultSet.getInt("id_doc");
            mnPkEntryId = resultSet.getInt("id_ety");
            mnPkSupplierFileId = resultSet.getInt("id_sup_file");
            
            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkYearId + ", " + 
                    mnPkDocId + ", " + 
                    mnPkEntryId + ", " + 
                    mnPkSupplierFileId + " " +
                    ")";
        }
        
        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbSupplierFileDpsEntry clone() throws CloneNotSupportedException {
        SDbSupplierFileDpsEntry registry = new SDbSupplierFileDpsEntry();
        
        registry.setPkYearId(this.getPkYearId());
        registry.setPkDocId(this.getPkDocId());
        registry.setPkEntryId(this.getPkEntryId());
        registry.setPkSupplierFileId(this.getPkSupplierFileId());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        msSql = "DELETE " + getSqlFromWhere();
        session.getStatement().execute(msSql);
    }
}
