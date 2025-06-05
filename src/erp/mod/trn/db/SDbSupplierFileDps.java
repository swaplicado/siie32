/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbSupplierFileDps extends SDbRegistryUser {
    
    protected int mnPkSupplierFileId;
    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnSortingPos;
    protected String msSupplierFileDpsType;
    protected double mdTotalLocal_r;
    protected double mdExchangeRateLocal;
    protected double mdTotalDps_r;
    protected double mdExchangeRateDps;
    protected double mdTotalCyQuotation;
    protected String msNotes;
    protected boolean mbExtemporaneous;
    protected int mnFkCurrencyDpsId;
    protected int mnFkCurrencyQuotationId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    ArrayList<SDbSupplierFileDpsEntry> maSuppFileDpsEty;
    
    protected String msXtaDpsCurKey; 

    public SDbSupplierFileDps() {
        super(SModConsts.TRN_SUP_FILE_DPS);
    }
    
    public void setPkSupplierFileId(int n) { mnPkSupplierFileId = n; }
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setSortingPos(int n) { mnSortingPos = n; }
    public void setSupplierFileDpsType(String s) { msSupplierFileDpsType = s; }
    public void setTotalLocal_r(double d) { mdTotalLocal_r = d; }
    public void setExchangeRateLocal(double d) { mdExchangeRateLocal = d; }
    public void setTotalDps_r(double d) { mdTotalDps_r = d; }
    public void setExchangeRateDps(double d) { mdExchangeRateDps = d; }
    public void setTotalCyQuotation(double d) { mdTotalCyQuotation = d; }
    public void setNotes(String s) { msNotes = s; }
    public void setExtemporaneous(boolean b) { mbExtemporaneous = b; }
    public void setFkCurrencyDpsId(int n) { mnFkCurrencyDpsId = n; }
    public void setFkCurrencyQuotationId(int n) { mnFkCurrencyQuotationId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setXtaDpsCurKey(String s) { msXtaDpsCurKey = s; }
    
    public int getPkSupplierFileId() { return mnPkSupplierFileId; }
    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getSortingPos() { return mnSortingPos; }
    public String getSupplierFileDpsType() { return msSupplierFileDpsType; }
    public double getTotalLocal_r() { return mdTotalLocal_r; }
    public double getExchangeRateLocal() { return mdExchangeRateLocal; }
    public double getTotalDps_r() { return mdTotalDps_r; }
    public double getExchangeRateDps() { return mdExchangeRateDps; }
    public double getTotalCyQuotation() { return mdTotalCyQuotation; }
    public String getNotes() { return msNotes; }
    public boolean isExtemporaneous() { return mbExtemporaneous; }
    public int getFkCurrencyDpsId() { return mnFkCurrencyDpsId; }
    public int getFkCurrencyQuotationId() { return mnFkCurrencyQuotationId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public ArrayList<SDbSupplierFileDpsEntry> getSuppFileDpsEty() { return maSuppFileDpsEty; }
    
    public String getXtaDpsCurKey() { return msXtaDpsCurKey; }
    
    public String getSupplierFileDpsTypeDecode() {
        switch (msSupplierFileDpsType) {
            case SDbSupplierFile.QUA: return "Proveedor";
            case SDbSupplierFile.QUA_EXP: return "Otro proveedor +$";
            case SDbSupplierFile.QUA_CHE: return "Otro proveedor -$";
            case SDbSupplierFile.TEC: return "Ficha técnica";
        }
        
        return "";
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSupplierFileId = pk[0];
        mnPkYearId = pk[1];
        mnPkDocId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSupplierFileId, mnPkYearId, mnPkDocId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkSupplierFileId = 0;
        mnPkYearId = 0;
        mnPkDocId = 0;
        mnSortingPos = 0;
        msSupplierFileDpsType = "";
        mdTotalLocal_r = 0;
        mdExchangeRateLocal = 0;
        mdTotalDps_r = 0;
        mdExchangeRateDps = 0;
        mdTotalCyQuotation = 0;
        msNotes = "";
        mbExtemporaneous = false;
        mnFkCurrencyDpsId = 0;
        mnFkCurrencyQuotationId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maSuppFileDpsEty = new ArrayList<>();
        
        msXtaDpsCurKey = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_sup_file = " + mnPkSupplierFileId + " " + 
                "AND id_year = " + mnPkYearId + " " +
                "AND id_doc = " + mnPkDocId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_sup_file = " + pk[0] + " " +
                "AND id_year = " + pk[1] + " " +
                "AND id_doc = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement;
        SDbSupplierFileDpsEntry entry;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk) + " ORDER BY sort";
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkSupplierFileId = resultSet.getInt("id_sup_file");
            mnPkYearId = resultSet.getInt("id_year");
            mnPkDocId = resultSet.getInt("id_doc");
            mnSortingPos = resultSet.getInt("sort");
            msSupplierFileDpsType = resultSet.getString("sup_file_dps_type");
            mdTotalLocal_r = resultSet.getDouble("tot_loc_r");
            mdExchangeRateLocal = resultSet.getDouble("exc_rate_loc");
            mdTotalDps_r = resultSet.getDouble("tot_dps_r");
            mdExchangeRateDps = resultSet.getDouble("exc_rate_dps");
            mdTotalCyQuotation = resultSet.getDouble("tot_cur_quot");
            msNotes = resultSet.getString("nts");
            mbExtemporaneous = resultSet.getBoolean("b_extemp");
            mnFkCurrencyDpsId = resultSet.getInt("fk_cur_dps");
            mnFkCurrencyQuotationId = resultSet.getInt("fk_cur_quot");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            statement = session.getStatement().getConnection().createStatement();
            
            msSql = "SELECT id_ety " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_SUP_FILE_DPS_ETY) + " " + 
                    "WHERE id_year = " + mnPkYearId + " " +
                    "AND id_doc = " + mnPkDocId + " " +
                    "AND id_sup_file = " + mnPkSupplierFileId;
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                entry = new SDbSupplierFileDpsEntry();
                entry.read(session, new int[] { mnPkYearId, mnPkDocId, resultSet.getInt(1), mnPkSupplierFileId });
                maSuppFileDpsEty.add(entry);
            }
            
            msSql = "SELECT cur_key " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " " + 
                    "WHERE id_cur = " + mnFkCurrencyDpsId;
                    
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                msXtaDpsCurKey = resultSet.getString(1);
            }
            
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
                    mnPkSupplierFileId + ", " + 
                    mnPkYearId + ", " + 
                    mnPkDocId + ", " + 
                    mnSortingPos + ", " + 
                    "'" + msSupplierFileDpsType + "', " + 
                    mdTotalLocal_r + ", " + 
                    mdExchangeRateLocal + ", " + 
                    mdTotalDps_r + ", " + 
                    mdExchangeRateDps + ", " + 
                    mdTotalCyQuotation + ", " + 
                    "'" + msNotes + "', " + 
                    (mbExtemporaneous ? 1 : 0) + ", " + 
                    mnFkCurrencyDpsId + ", " + 
                    mnFkCurrencyQuotationId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_sup_file = " + mnPkSupplierFileId + ", " +
                    //"id_year = " + mnPkYearId + ", " +
                    //"id_doc = " + mnPkDocId + ", " +
                    "sort = " + mnSortingPos + ", " +
                    "sup_file_dps_type = '" + msSupplierFileDpsType + "', " +
                    "tot_loc_r = " + mdTotalLocal_r + ", " +
                    "exc_rate_loc = " + mdExchangeRateLocal + ", " +
                    "tot_dps_r = " + mdTotalDps_r + ", " +
                    "exc_rate_dps = " + mdExchangeRateDps + ", " +
                    "tot_cur_quot = " + mdTotalCyQuotation + ", " +
                    "nts = '" + msNotes + "', " +
                    "b_extemp = " + (mbExtemporaneous ? 1 : 0) + ", " +
                    "fk_cur_dps = " + mnFkCurrencyDpsId + ", " +
                    "fk_cur_quot = " + mnFkCurrencyQuotationId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_SUP_FILE_DPS_ETY) + " " + 
                "WHERE id_year = " + mnPkYearId + " " +
                "AND id_doc = " + mnPkDocId + " " +
                "AND id_sup_file = " + mnPkSupplierFileId;
        session.getStatement().execute(msSql);
        for (SDbSupplierFileDpsEntry ety : maSuppFileDpsEty) {
            ety.setPkSupplierFileId(mnPkSupplierFileId);
            ety.setRegistryNew(true);
            ety.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbSupplierFileDps clone() throws CloneNotSupportedException {
        SDbSupplierFileDps registry = new SDbSupplierFileDps();
        
        registry.setPkSupplierFileId(this.getPkSupplierFileId());
        registry.setPkYearId(this.getPkYearId());
        registry.setPkDocId(this.getPkDocId());
        registry.setSortingPos(this.getSortingPos());
        registry.setSupplierFileDpsType(this.getSupplierFileDpsType());
        registry.setTotalLocal_r(this.getTotalLocal_r());
        registry.setExchangeRateLocal(this.getExchangeRateLocal());
        registry.setTotalDps_r(this.getTotalDps_r());
        registry.setExchangeRateDps(this.getExchangeRateDps());
        registry.setTotalCyQuotation(this.getTotalCyQuotation());
        registry.setNotes(this.getNotes());
        registry.setExtemporaneous(this.isExtemporaneous());
        registry.setFkCurrencyDpsId(this.getFkCurrencyDpsId());
        registry.setFkCurrencyQuotationId(this.getFkCurrencyQuotationId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (SDbSupplierFileDpsEntry ety : this.getSuppFileDpsEty()) {
            registry.getSuppFileDpsEty().add(ety);
        }
        
        registry.setXtaDpsCurKey(this.getXtaDpsCurKey());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        for (SDbSupplierFileDpsEntry ety : maSuppFileDpsEty) {
            ety.delete(session);
        }
        
        msSql = "DELETE " + getSqlFromWhere();
        session.getStatement().execute(msSql);
    }
}