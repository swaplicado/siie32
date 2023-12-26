/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbCustomerReport extends SDbRegistryUser {

    protected int mnPkCustomSalesReportId;
    protected String msReportKey;
    protected String msName;
    protected String msSettings;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbCustomerReport() {
        super(SModConsts.CFG_CUSTOM_REP);
    }
    
    public void setPkCustomSalesReportId(int n) { mnPkCustomSalesReportId = n; }
    public void setReportKey(String s) { msReportKey = s; }
    public void setName(String s) { msName = s; }
    public void setSettings(String s) { msSettings = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkCustomSalesReportId() { return mnPkCustomSalesReportId; }
    public String getReportKey() { return msReportKey; }
    public String getName() { return msName; }
    public String getSettings() { return msSettings; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void readByReportKey(SGuiSession session, String key) throws SQLException, Exception {
        ResultSet resultSet;
        int[] pk;
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT id_custom_rep FROM " + getSqlTable() + " WHERE rep_key = '" + key + "' ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            pk = new int[] { resultSet.getInt(1) };
        }
        
        read(session, pk);
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCustomSalesReportId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCustomSalesReportId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkCustomSalesReportId = 0;
        msReportKey = "";
        msName = "";
        msSettings = null;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_custom_rep = " + mnPkCustomSalesReportId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_custom_rep = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            mnPkCustomSalesReportId = resultSet.getInt("id_custom_rep");
            msReportKey = resultSet.getString("rep_key");
            msName = resultSet.getString("name");
            msSettings = resultSet.getString("settings");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkCustomSalesReportId + ", " + 
                    "'" + msReportKey + "', " + 
                    "'" + msName + "', " + 
                    "'" + msSettings + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_custom_rep = " + mnPkCustomSalesReportId + ", " +
                    //"rep_key = '" + msReportKey + "', " +
                    "name = '" + msName + "', " +
                    "settings = '" + msSettings + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbCustomerReport clone() throws CloneNotSupportedException {
        SDbCustomerReport registry = new SDbCustomerReport();
        
        registry.setPkCustomSalesReportId(this.getPkCustomSalesReportId());
        registry.setReportKey(this.getReportKey());
        registry.setName(this.getName());
        registry.setSettings(this.getSettings());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        return registry;
    }
}
