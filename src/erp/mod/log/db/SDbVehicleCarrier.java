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
 * @author Sergio Flores
 */
public class SDbVehicleCarrier extends SDbRegistryUser {

    protected int mnPkVehicleId;
    protected int mnPkCarrierId;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbVehicleCarrier() {
        super(SModConsts.LOG_VEH_BP_CAR);
    }

    public void setPkVehicleId(int n) { mnPkVehicleId = n; }
    public void setPkCarrierId(int n) { mnPkCarrierId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkVehicleId() { return mnPkVehicleId; }
    public int getPkCarrierId() { return mnPkCarrierId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkVehicleId = pk[0];
        mnPkCarrierId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkVehicleId, mnPkCarrierId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkVehicleId = 0;
        mnPkCarrierId = 0;
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
        return "WHERE id_veh = " + mnPkVehicleId + " AND id_car = " + mnPkCarrierId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_veh = " + pk[0] + " AND id_car = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            mnPkCarrierId = resultSet.getInt("id_car");
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
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkVehicleId + ", " + 
                    mnPkCarrierId + ", " + 
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
                    /*
                    "id_veh = " + mnPkVehicleId + ", " +
                    "id_car = " + mnPkCarrierId + ", " +
                    */
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
    public SDbVehicleCarrier clone() throws CloneNotSupportedException {
        SDbVehicleCarrier registry = new SDbVehicleCarrier();

        registry.setPkVehicleId(this.getPkVehicleId());
        registry.setPkCarrierId(this.getPkCarrierId());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
