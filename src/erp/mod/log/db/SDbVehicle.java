/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.log.db;

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
 * @author Néstor Ávalos
 */
public class SDbVehicle extends SDbRegistryUser {

    protected int mnPkVehicleId;
    protected String msCode;
    protected String msName;
    protected String msPlate;
    //protected boolean mbDeleted;
    protected int mnFkVehicleTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbVehicle() {
        super(SModConsts.LOG_VEH);
    }

    public void setPkVehicleId(int n) { mnPkVehicleId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setPlate(String s) { msPlate = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkVehicleTypeId(int n) { mnFkVehicleTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkVehicleId() { return mnPkVehicleId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getPlate() { return msPlate; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkVehicleTypeId() { return mnFkVehicleTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkVehicleId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkVehicleId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkVehicleId = 0;
        msCode = "";
        msName = "";
        msPlate = "";
        mbDeleted = false;
        mnFkVehicleTypeId = 0;
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
        return "WHERE id_veh = " + mnPkVehicleId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_veh = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkVehicleId = 0;

        msSql = "SELECT COALESCE(MAX(id_veh), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkVehicleId = resultSet.getInt(1);
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
            mnPkVehicleId = resultSet.getInt("id_veh");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msPlate = resultSet.getString("plate");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkVehicleTypeId = resultSet.getInt("fk_tp_veh");
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
                    mnPkVehicleId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    "'" + msPlate + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkVehicleTypeId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_veh = " + mnPkVehicleId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "plate = '" + msPlate + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_veh = " + mnFkVehicleTypeId + ", " +
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
    public SDbVehicle clone() throws CloneNotSupportedException {
        SDbVehicle registry = new SDbVehicle();

        registry.setPkVehicleId(this.getPkVehicleId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setPlate(this.getPlate());
        registry.setDeleted(this.isDeleted());
        registry.setFkVehicleTypeId(this.getFkVehicleTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        return registry;
    }
}
