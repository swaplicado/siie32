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
public class SDbVehicleType extends SDbRegistryUser {

    protected int mnPkVehicleTypeId;
    protected String msCode;
    protected String msName;
    protected double mdCapacityVolume;
    protected double mdCapacityMass;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    * */

    public SDbVehicleType() {
        super(SModConsts.LOGU_TP_VEH);
    }

    public void setPkVehicleTypeId(int n) { mnPkVehicleTypeId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setCapacityVolume(double d) { mdCapacityVolume = d; }
    public void setCapacityMass(double d) { mdCapacityMass = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkVehicleTypeId() { return mnPkVehicleTypeId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public double getCapacityVolume() { return mdCapacityVolume; }
    public double getCapacityMass() { return mdCapacityMass; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkVehicleTypeId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkVehicleTypeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkVehicleTypeId = 0;
        msCode = "";
        msName = "";
        mdCapacityVolume = 0;
        mdCapacityMass = 0;
        mbDeleted = false;
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
        return "WHERE id_tp_veh = " + mnPkVehicleTypeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tp_veh = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkVehicleTypeId = 0;

        msSql = "SELECT COALESCE(MAX(id_tp_veh), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkVehicleTypeId = resultSet.getInt(1);
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
            mnPkVehicleTypeId = resultSet.getInt("id_tp_veh");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mdCapacityVolume = resultSet.getDouble("cap_vol");
            mdCapacityMass = resultSet.getDouble("cap_mass");
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
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkVehicleTypeId + ", " +
                "'" + msCode + "', " +
                "'" + msName + "', " +
                mdCapacityVolume + ", " +
                mdCapacityMass + ", " +
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
                //"id_tp_veh = " + mnPkVehicleTypeId + ", " +
                "code = '" + msCode + "', " +
                "name = '" + msName + "', " +
                "cap_vol = " + mdCapacityVolume + ", " +
                "cap_mass = " + mdCapacityMass + ", " +
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
    public SDbVehicleType clone() throws CloneNotSupportedException {
        SDbVehicleType registry = new SDbVehicleType();

        registry.setPkVehicleTypeId(this.getPkVehicleTypeId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setCapacityVolume(this.getCapacityVolume());
        registry.setCapacityMass(this.getCapacityMass());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        return registry;
    }
}
