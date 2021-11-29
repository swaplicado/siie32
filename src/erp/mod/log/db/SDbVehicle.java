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
 * @author Néstor Ávalos, Isabel Servín
 */
public class SDbVehicle extends SDbRegistryUser {

    protected int mnPkVehicleId;
    protected String msCode;
    protected String msName;
    protected String msPlate;
    protected int mnVehicleYear;
    protected String msVehicleConfiguration;
    protected String msPermissonSctType;
    protected String msPermissonSctNumber;
    protected String msInsurancePolicy;
    //protected boolean mbDeleted;
    protected int mnFkVehicleTypeId;
    protected int mnFkInsurerId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected String msXtaInsurerName;

    public SDbVehicle() {
        super(SModConsts.LOG_VEH);
    }

    public void setPkVehicleId(int n) { mnPkVehicleId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setPlate(String s) { msPlate = s; }
    public void setVehicleYear(int n) { mnVehicleYear = n; }
    public void setVehicleConfiguration(String s) { msVehicleConfiguration = s; }
    public void setPermissonSctType(String s) { msPermissonSctType = s; }
    public void setPermissonSctNumber(String s) { msPermissonSctNumber = s; }
    public void setInsurancePolicy(String s) { msInsurancePolicy = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkVehicleTypeId(int n) { mnFkVehicleTypeId = n; }
    public void setFkInsurerId_n(int n) { mnFkInsurerId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkVehicleId() { return mnPkVehicleId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getPlate() { return msPlate; }
    public int getVehicleYear() { return mnVehicleYear; }
    public String getVehicleConfiguration() { return msVehicleConfiguration; }
    public String getPermissonSctType() { return msPermissonSctType; }
    public String getPermissonSctNumber() { return msPermissonSctNumber; }
    public String getInsurancePolicy() { return msInsurancePolicy; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkVehicleTypeId() { return mnFkVehicleTypeId; }
    public int getFkInsurerId_n() { return mnFkInsurerId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public void setXtaInsurerName(String s) { msXtaInsurerName = s; }
    
    public String getXtaInsurerName() { return msXtaInsurerName; }

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
        mnVehicleYear = 0;
        msVehicleConfiguration = "";
        msPermissonSctType = "";
        msPermissonSctNumber = "";
        msInsurancePolicy = "";
        mbDeleted = false;
        mnFkVehicleTypeId = 0;
        mnFkInsurerId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        msXtaInsurerName = "";
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

        msSql = "SELECT v.*, i.name AS insurer FROM log_veh AS v "
                + "LEFT OUTER JOIN log_insurer AS i ON i.id_insurer = v.fk_insurer_n "
                + getSqlWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkVehicleId = resultSet.getInt("id_veh");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msPlate = resultSet.getString("plate");
            mnVehicleYear = resultSet.getInt("veh_year");
            msVehicleConfiguration = resultSet.getString("veh_conf");
            msPermissonSctType = resultSet.getString("perm_sct_tp");
            msPermissonSctNumber = resultSet.getString("perm_sct_num");
            msInsurancePolicy = resultSet.getString("insurance_policy");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkVehicleTypeId = resultSet.getInt("fk_tp_veh");
            mnFkInsurerId_n = resultSet.getInt("fk_insurer_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            msXtaInsurerName = resultSet.getString("insurer");

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
                    mnVehicleYear + ", " + 
                    "'" + msVehicleConfiguration + "', " + 
                    "'" + msPermissonSctType + "', " + 
                    "'" + msPermissonSctNumber + "', " + 
                    "'" + msInsurancePolicy + "', " + 
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkVehicleTypeId + ", " +
                    (mnFkInsurerId_n == 0 ? "NULL, " : mnFkInsurerId_n + ", ") + 
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
                    "veh_year = " + mnVehicleYear + ", " +
                    "veh_conf = '" + msVehicleConfiguration + "', " +
                    "perm_sct_tp = '" + msPermissonSctType + "', " +
                    "perm_sct_num = '" + msPermissonSctNumber + "', " +
                    "insurance_policy = '" + msInsurancePolicy + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_veh = " + mnFkVehicleTypeId + ", " +
                    "fk_insurer_n = " + (mnFkInsurerId_n == 0 ? "NULL, " : mnFkInsurerId_n + ", ") +
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
        registry.setVehicleYear(this.getVehicleYear());
        registry.setVehicleConfiguration(this.getVehicleConfiguration());
        registry.setPermissonSctType(this.getPermissonSctType());
        registry.setPermissonSctNumber(this.getPermissonSctNumber());
        registry.setInsurancePolicy(this.getInsurancePolicy());
        registry.setDeleted(this.isDeleted());
        registry.setFkVehicleTypeId(this.getFkVehicleTypeId());
        registry.setFkInsurerId_n(this.getFkInsurerId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        return registry;
    }
}
