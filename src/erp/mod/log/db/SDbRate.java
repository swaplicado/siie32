/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.log.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos, Isabel Servín
 */
public class SDbRate extends SDbRegistryUser {

    protected int mnPkRateId;
    protected double mdRate;
    protected boolean mbConsolidated;
    //protected boolean mbDeleted;
    protected int mnFkCurrencyId;
    protected int mnFkSourceSpotTypeId;
    protected int mnFkSourceSpotId;
    protected int mnFkDestinySpotTypeId;
    protected int mnFkDestinySpotId;
    protected int mnFkCarrierId;
    protected int mnFkVehicleTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected String msXtaCurrency;
    protected String msXtaCurrencySymbol;

    public SDbRate() {
        super(SModConsts.LOG_RATE);
    }

    public void setPkRateId(int n) { mnPkRateId = n; }
    public void setRate(double d) { mdRate = d; }
    public void setConsolidated(boolean b) { mbConsolidated = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkSourceSpotTypeId(int n) { mnFkSourceSpotTypeId = n; }
    public void setFkSourceSpotId(int n) { mnFkSourceSpotId = n; }
    public void setFkDestinySpotTypeId(int n) { mnFkDestinySpotTypeId = n; }
    public void setFkDestinySpotId(int n) { mnFkDestinySpotId = n; }
    public void setFkCarrierId(int n) { mnFkCarrierId = n; }
    public void setFkVehicleTypeId(int n) { mnFkVehicleTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkRateId() { return mnPkRateId; }
    public double getRate() { return mdRate; }
    public boolean isConsolidated() { return mbConsolidated; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkSourceSpotTypeId() { return mnFkSourceSpotTypeId; }
    public int getFkSourceSpotId() { return mnFkSourceSpotId; }
    public int getFkDestinySpotTypeId() { return mnFkDestinySpotTypeId; }
    public int getFkDestinySpotId() { return mnFkDestinySpotId; }
    public int getFkCarrierId() { return mnFkCarrierId; }
    public int getFkVehicleTypeId() { return mnFkVehicleTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setXtaCurrency(String s) { msXtaCurrency = s; }
    public void setXtaCurrencySymbol(String s) { msXtaCurrencySymbol = s; }

    public String getXtaCurrency() { return msXtaCurrency; }
    public String getXtaCurrencySymbol() { return msXtaCurrencySymbol; }

    public Vector<SDbRate> readRows(SGuiSession session, int[] params) throws SQLException, Exception {
        Vector<SDbRate> mvRates = null;
        SDbRate rate = null;

        ResultSet resultSet = null;
        Statement statement = null;

        msSql = "SELECT * " + getSqlTable() + " WHERE b_del = 0 AND fk_src_spot =  " + params[0] + " AND fk_des_spot = " + params[1] + " AND " +
                "fk_car_n = " + params[2] + " AND fk_tp_veh_n = " + params[3] + " ";
        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {

            rate = new SDbRate();
            rate.read(session, new int[] { resultSet.getInt("id_rate") } );
            mvRates.add(rate);
        }

        return mvRates;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkRateId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkRateId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkRateId = 0;
        mdRate = 0;
        mbConsolidated = false;
        mbDeleted = false;
        mnFkCurrencyId = 0;
        mnFkSourceSpotTypeId = 0;
        mnFkSourceSpotId = 0;
        mnFkDestinySpotTypeId = 0;
        mnFkDestinySpotId = 0;
        mnFkCarrierId = 0;
        mnFkVehicleTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        msXtaCurrency = "";
        msXtaCurrencySymbol = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_rate = " + mnPkRateId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_rate = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkRateId = 0;

        msSql = "SELECT COALESCE(MAX(id_rate), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkRateId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT r.*, cur.cur, cur.cur_key FROM " + getSqlTable() + " AS r  "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS cur ON r.fk_cur = cur.id_cur "
            + "WHERE r.id_rate = " + pk[0];
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkRateId = resultSet.getInt("id_rate");
            mdRate = resultSet.getDouble("rate");
            mbConsolidated = resultSet.getBoolean("b_con");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkCurrencyId = resultSet.getInt("fk_cur");
            mnFkSourceSpotTypeId = resultSet.getInt("fk_src_tp_spot");
            mnFkSourceSpotId = resultSet.getInt("fk_src_spot");
            mnFkDestinySpotTypeId = resultSet.getInt("fk_des_tp_spot");
            mnFkDestinySpotId = resultSet.getInt("fk_des_spot");
            mnFkCarrierId = resultSet.getInt("fk_car_n");
            mnFkVehicleTypeId = resultSet.getInt("fk_tp_veh_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            msXtaCurrency = resultSet.getString("cur.cur");
            msXtaCurrencySymbol = resultSet.getString("cur.cur_key");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkRateId + ", " +
                mdRate + ", " +
                (mbConsolidated ? 1 : 0) + ", " +
                (mbDeleted ? 1 : 0) + ", " +
                mnFkCurrencyId + ", " +
                mnFkSourceSpotTypeId + ", " +
                mnFkSourceSpotId + ", " +
                mnFkDestinySpotTypeId + ", " +
                mnFkDestinySpotId + ", " +
                (mnFkCarrierId > 0 ? mnFkCarrierId : "NULL") + ", " +
                (mnFkVehicleTypeId > 0 ? mnFkVehicleTypeId : "NULL") + ", " +
                mnFkUserInsertId + ", " +
                mnFkUserUpdateId + ", " +
                "NOW()" + ", " +
                "NOW()" + " " +
                ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                //"id_rate = " + mnPkRateId + ", " +
                "rate = " + mdRate + ", " +
                "b_con = " + (mbConsolidated ? 1 : 0) + ", " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fk_cur = " + mnFkCurrencyId + ", " +
                "fk_src_tp_spot = " + mnFkSourceSpotTypeId + ", " +
                "fk_src_spot = " + mnFkSourceSpotId + ", " +
                "fk_des_tp_spot = " + mnFkDestinySpotTypeId + ", " +
                "fk_des_spot = " + mnFkDestinySpotId + ", " +
                "fk_car_n = " + (mnFkCarrierId > 0 ? mnFkCarrierId : "NULL") + ", " +
                "fk_tp_veh_n = " + (mnFkVehicleTypeId > 0 ? mnFkVehicleTypeId : "NULL") + ", " +
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
    public SDbRate clone() throws CloneNotSupportedException {
        SDbRate registry = new SDbRate();

        registry.setPkRateId(this.getPkRateId());
        registry.setRate(this.getRate());
        registry.setConsolidated(this.isConsolidated());
        registry.setDeleted(this.isDeleted());
        registry.setFkCurrencyId(this.getFkCurrencyId());
        registry.setFkSourceSpotTypeId(this.getFkSourceSpotTypeId());
        registry.setFkSourceSpotId(this.getFkSourceSpotId());
        registry.setFkDestinySpotTypeId(this.getFkDestinySpotTypeId());
        registry.setFkDestinySpotId(this.getFkDestinySpotId());
        registry.setFkCarrierId(this.getFkCarrierId());
        registry.setFkVehicleTypeId(this.getFkVehicleTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        return registry;
    }
}
