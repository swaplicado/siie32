/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.itm.db;

import erp.mod.SModConsts;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servin
 */
public class SDbPriceCommercialLog extends SDbRegistryUser {

    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkLogId;
    protected Date mtDate;
    protected double mdPrice;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkDpsYear_n;
    protected int mnFkDpsDoc_n;
    protected int mnFkDpsEntry_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbPriceCommercialLog() {
        super(SModConsts.ITMU_PRICE_COMM_LOG);
    }
    
    private boolean isItemInv(Connection connection) throws SQLException, Exception { 
        boolean inv = false;
        
        msSql = "SELECT b_inv FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " WHERE id_item = " + mnPkItemId;
        try (ResultSet resultSet = connection.createStatement().executeQuery(msSql)) {
            if (resultSet.next()) {
                inv = resultSet.getBoolean(1);
            }
        }
        
        return inv;
    }
    
    private boolean isSamePrice(Connection connection) throws SQLException, Exception { 
        boolean samePrice = false;
        
        msSql = "SELECT * FROM " + getSqlTable() + " WHERE id_item = " + mnPkItemId + " AND id_unit = " + mnPkUnitId + " AND price = " + mdPrice + " AND NOT b_del";
        try (ResultSet resultSet = connection.createStatement().executeQuery(msSql)) {
            if (resultSet.next()) {
                samePrice = true;
            }
        }
        
        return samePrice;
    }
    
    private void sameItem(Connection connection) throws SQLException, Exception { 
        boolean sameItem = false;
        
        msSql = "SELECT id_log FROM " + getSqlTable() + " WHERE id_item = " + mnPkItemId + " AND id_unit = " + mnPkUnitId + " AND NOT b_del";
        try (ResultSet resultSet = connection.createStatement().executeQuery(msSql)) {
            if (resultSet.next()) {
                sameItem = true;
            }
        }
        if (sameItem) {
            msSql = "UPDATE " + getSqlTable() + " SET b_del = 1 WHERE id_item = " + mnPkItemId + " AND id_unit = " + mnPkUnitId;
            connection.createStatement().execute(msSql);
        }
    }
    
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkLogId(int n) { mnPkLogId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setPrice(double d) { mdPrice = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkDpsYear_n(int n) { mnFkDpsYear_n = n; }
    public void setFkDpsDoc_n(int n) { mnFkDpsDoc_n = n; }
    public void setFkDpsEntry_n(int n) { mnFkDpsEntry_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkLogId() { return mnPkLogId; }
    public Date getDate() { return mtDate; }
    public double getPrice() { return mdPrice; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkDpsYear_n() { return mnFkDpsYear_n; }
    public int getFkDpsDoc_n() { return mnFkDpsDoc_n; }
    public int getFkDpsEntry_n() { return mnFkDpsEntry_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public void saveFromDps(Connection connection) throws SQLException, Exception {
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        ResultSet resultSet;
        if (isItemInv(connection)) {
            if (!isSamePrice(connection)) {
                sameItem(connection);
                mnPkLogId = 0;
        
                msSql = "SELECT COALESCE(MAX(id_log), 0) + 1 FROM " + getSqlTable() + " " +
                        "WHERE id_item = " + mnPkItemId + " AND id_unit = " + mnPkUnitId + " ";
                resultSet = connection.createStatement().executeQuery(msSql);
                if (resultSet.next()) {
                    mnPkLogId = resultSet.getInt(1);
                }
                resultSet.close();
                
                mbDeleted = false;
                mnFkUserInsertId = SUtilConsts.USR_NA_ID;
                mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

                msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                        mnPkItemId + ", " + 
                        mnPkUnitId + ", " + 
                        mnPkLogId + ", " + 
                        "NOW(), " + 
                        mdPrice + ", " + 
                        (mbDeleted ? 1 : 0) + ", " + 
                        (mbSystem ? 1 : 0) + ", " + 
                        mnFkDpsYear_n + ", " + 
                        mnFkDpsDoc_n + ", " + 
                        mnFkDpsEntry_n + ", " + 
                        mnFkUserInsertId + ", " + 
                        mnFkUserUpdateId + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + " " + 
                        ")";
                
                connection.createStatement().execute(msSql);
                mbRegistryNew = false;
                mnQueryResultId = SDbConsts.SAVE_OK;
            }
        }
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkItemId = pk[0];
        mnPkUnitId = pk[1];
        mnPkLogId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkItemId, mnPkUnitId, mnPkLogId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkItemId = 0;
        mnPkUnitId = 0;
        mnPkLogId = 0;
        mtDate = null;
        mdPrice = 0;
        mbDeleted = false;
        mbSystem = false;
        mnFkDpsYear_n = 0;
        mnFkDpsDoc_n = 0;
        mnFkDpsEntry_n = 0;
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
        return "WHERE id_item = " + mnPkItemId + " " + 
                "AND id_unit = " + mnPkUnitId + " " + 
                "AND id_log = " + mnPkLogId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_item = " + pk[0] + " " + 
                "AND id_unit = " + pk[1] + " " + 
                "AND id_log = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkLogId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_log), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_item = " + mnPkItemId + " AND id_unit = " + mnPkUnitId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkLogId = resultSet.getInt(1);
        }
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
            mnPkItemId = resultSet.getInt("id_item");
            mnPkUnitId = resultSet.getInt("id_unit");
            mnPkLogId = resultSet.getInt("id_log");
            mtDate = resultSet.getDate("dt");
            mdPrice = resultSet.getDouble("price");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkDpsYear_n = resultSet.getInt("fk_dps_year_n");
            mnFkDpsDoc_n = resultSet.getInt("fk_dps_doc_n");
            mnFkDpsEntry_n = resultSet.getInt("fk_dps_ety_n");
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
            if (!isSamePrice(session.getDatabase().getConnection())) {
                computePrimaryKey(session);
                mbDeleted = false;
                mnFkUserInsertId = session.getUser().getPkUserId();
                mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
                sameItem(session.getDatabase().getConnection());

                msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                        mnPkItemId + ", " + 
                        mnPkUnitId + ", " + 
                        mnPkLogId + ", " + 
                        "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                        mdPrice + ", " + 
                        (mbDeleted ? 1 : 0) + ", " + 
                        (mbSystem ? 1 : 0) + ", " + 
                        (mnFkDpsYear_n == 0 ? "NULL, " : mnFkDpsYear_n + ", ") + 
                        (mnFkDpsDoc_n == 0 ? "NULL, " : mnFkDpsDoc_n + ", ") + 
                        (mnFkDpsEntry_n == 0 ? "NULL, " : mnFkDpsEntry_n + ", ") + 
                        mnFkUserInsertId + ", " + 
                        mnFkUserUpdateId + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + " " + 
                        ")";
            }
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_item = " + mnPkItemId + ", " +
                    //"id_unit = " + mnPkUnitId + ", " +
                    //"id_log = " + mnPkLogId + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "price = " + mdPrice + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    //"fk_dps_year_n = " + mnFkDpsYear_n + ", " +
                    //"fk_dps_doc_n = " + mnFkDpsDoc_n + ", " +
                    //"fk_dps_ety_n = " + mnFkDpsEntry_n + ", " +
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
    public SDbPriceCommercialLog clone() throws CloneNotSupportedException {
        SDbPriceCommercialLog registry = new SDbPriceCommercialLog();
        
        registry.setPkItemId(this.getPkItemId());
        registry.setPkUnitId(this.getPkUnitId());
        registry.setPkLogId(this.getPkLogId());
        registry.setDate(this.getDate());
        registry.setPrice(this.getPrice());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkDpsYear_n(this.getFkDpsYear_n());
        registry.setFkDpsDoc_n(this.getFkDpsDoc_n());
        registry.setFkDpsEntry_n(this.getFkDpsEntry_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setRegistryNew(this.isRegistryNew());

        return registry;
    }
}
