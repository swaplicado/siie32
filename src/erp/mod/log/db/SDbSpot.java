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
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos
 */
public class SDbSpot extends SDbRegistryUser {

    public static final int FIELD_FK_TP_SPOT = FIELD_BASE + 1;

    protected int mnPkSpotId;
    protected String msCode;
    protected String msName;
    //protected boolean mbDeleted;
    protected int mnFkDeliveryTypeId;
    protected int mnFkSpotTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbSpot() {
        super(SModConsts.LOGU_SPOT);
    }

    public void setPkSpotId(int n) { mnPkSpotId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkDeliveryTypeId(int n) { mnFkDeliveryTypeId = n; }
    public void setFkSpotTypeId(int n) { mnFkSpotTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkSpotId() { return mnPkSpotId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkDeliveryTypeId() { return mnFkDeliveryTypeId; }
    public int getFkSpotTypeId() { return mnFkSpotTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSpotId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSpotId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkSpotId = 0;
        msCode = "";
        msName = "";
        mbDeleted = false;
        mnFkDeliveryTypeId = 0;
        mnFkSpotTypeId = 0;
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
        return "WHERE id_spot = " + mnPkSpotId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_spot = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkSpotId = 0;

        msSql = "SELECT COALESCE(MAX(id_spot), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkSpotId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkSpotId = resultSet.getInt("id_spot");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkDeliveryTypeId = resultSet.getInt("fk_tp_dly");
            mnFkSpotTypeId = resultSet.getInt("fk_tp_spot");
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
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkSpotId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkSpotTypeId + ", " +
                    mnFkDeliveryTypeId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_spot = " + mnPkSpotId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_spot = " + mnFkSpotTypeId + ", " +
                    "fk_tp_dly = " + mnFkDeliveryTypeId + ", " +
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
    public Object readField(Statement statement, int[] pk, int field) throws SQLException, Exception {
        Object value = null;
        ResultSet resultSet = null;

        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT ";

        switch (field) {
            case FIELD_FK_TP_SPOT:
                msSql += "fk_tp_spot FROM " + getSqlTable() + " ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        resultSet = statement.executeQuery(msSql);

        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            switch (field) {
                case FIELD_FK_TP_SPOT:
                    value = resultSet.getInt(1);
                    break;
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }

        mnQueryResultId = SDbConsts.READ_OK;

        return value;
    }

    @Override
    public SDbSpot clone() throws CloneNotSupportedException {
        SDbSpot registry = new SDbSpot();

        registry.setPkSpotId(this.getPkSpotId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDeleted(this.isDeleted());
        registry.setFkDeliveryTypeId(this.getFkDeliveryTypeId());
        registry.setFkSpotTypeId(this.getFkSpotTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        return registry;
    }
}
