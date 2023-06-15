/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.cfg.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Edwin Carmona
 */
public class SDbAuthConfiguration extends SDbRegistryUser {

    protected int mnPkConfigurationId;
    protected String msTableName_n;
    protected String msConditionField_n;
    protected String msConditionOperator_n;
    protected String msConditionValue_n;
    protected int mnUserLevel;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    protected boolean mbRequired;
//    protected boolean mbDeleted;
//    protected boolean mbSystem;
    protected int mnFkAuthTypeId;
    protected int mnFkUserAuthId;
//    protected int mnFkUserInsertId;
//    protected int mnFkUserUpdateId;
//    protected Date mtTsUserInsert;
//    protected Date mtTsUserUpdate;

    public SDbAuthConfiguration() {
        super(SModConsts.CFGU_AUTH);
    }
    
    public void setPkConfigurationId(int n) { mnPkConfigurationId = n; }
    public void setTableName_n(String s) { msTableName_n = s; }
    public void setConditionField_n(String s) { msConditionField_n = s; }
    public void setConditionOperator_n(String s) { msConditionOperator_n = s; }
    public void setConditionValue_n(String s) { msConditionValue_n = s; }
    public void setUserLevel(int n) { mnUserLevel = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setRequired(boolean b) { mbRequired = b; }
//    public void setDeleted(boolean b) { mbDeleted = b; }
//    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkAuthTypeId(int n) { mnFkAuthTypeId = n; }
    public void setFkUserAuthId(int n) { mnFkUserAuthId = n; }
//    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
//    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
//    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
//    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkConfigurationId() { return mnPkConfigurationId; }
    public String getTableName_n() { return msTableName_n; }
    public String getConditionField_n() { return msConditionField_n; }
    public String getConditionOperator_n() { return msConditionOperator_n; }
    public String getConditionValue_n() { return msConditionValue_n; }
    public int getUserLevel() { return mnUserLevel; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public boolean isRequired() { return mbRequired; }
//    public boolean isDeleted() { return mbDeleted; }
//    public boolean isSystem() { return mbSystem; }
    public int getFkAuthTypeId() { return mnFkAuthTypeId; }
    public int getFkUserAuthId() { return mnFkUserAuthId; }
//    public int getFkUserInsertId() { return mnFkUserInsertId; }
//    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
//    public Date getTsUserInsert() { return mtTsUserInsert; }
//    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkConfigurationId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkConfigurationId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkConfigurationId = 0;
        msTableName_n = "";
        msConditionField_n = "";
        msConditionOperator_n = "";
        msConditionValue_n = "";
        mnUserLevel = 0;
        mtDateStart = null;
        mtDateEnd = null;
        mbRequired = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkAuthTypeId = 0;
        mnFkUserAuthId = 0;
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
        return "WHERE id_auth = " + mnPkConfigurationId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_auth = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkConfigurationId = 0;

        msSql = "SELECT COALESCE(MAX(id_auth), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkConfigurationId = resultSet.getInt(1);
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
            mnPkConfigurationId = resultSet.getInt("id_auth");
            msTableName_n = resultSet.getString("cond_tab_name_n");
            msConditionField_n = resultSet.getString("cond_field");
            msConditionOperator_n = resultSet.getString("cond_operator");
            msConditionValue_n = resultSet.getString("cond_value");
            mnUserLevel = resultSet.getInt("lev");
            mtDateStart = resultSet.getDate("dt_sta");
            mtDateEnd = resultSet.getDate("dt_end_n");
            mbRequired = resultSet.getBoolean("b_req");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkAuthTypeId = resultSet.getInt("fk_auth_type");
            mnFkUserAuthId = resultSet.getInt("fk_usr_auth");
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
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkConfigurationId + ", " + 
                    "'" + msTableName_n + "', " + 
                    "'" + msConditionField_n + "', " + 
                    "'" + msConditionOperator_n + "', " + 
                    "'" + msConditionValue_n + "', " + 
                    mnUserLevel + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " + 
                    (mbRequired ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkAuthTypeId + ", " + 
                    mnFkUserAuthId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "id_auth = " + mnPkConfigurationId + ", " +
                    "cond_tab_name_n = '" + msTableName_n + "', " +
                    "cond_field = '" + msConditionField_n + "', " +
                    "cond_operator = '" + msConditionOperator_n + "', " +
                    "cond_value = '" + msConditionValue_n + "', " +
                    "lev = " + mnUserLevel + ", " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end_n = '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    "b_req = " + (mbRequired ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_auth_type = " + mnFkAuthTypeId + ", " +
                    "fk_usr_auth = " + mnFkUserAuthId + ", " +
                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbAuthConfiguration registry = new SDbAuthConfiguration();

        registry.setPkConfigurationId(this.getPkConfigurationId());
        registry.setTableName_n(this.getTableName_n());
        registry.setConditionField_n(this.getConditionField_n());
        registry.setConditionOperator_n(this.getConditionOperator_n());
        registry.setConditionValue_n(this.getConditionValue_n());
        registry.setUserLevel(this.getUserLevel());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd(this.getDateEnd());
        registry.setRequired(this.isRequired());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkAuthTypeId(this.getFkAuthTypeId());
        registry.setFkUserAuthId(this.getFkUserAuthId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public Object readField(final Statement statement, final int[] pk, final int field) throws SQLException, Exception {
        Object value = null;
        ResultSet resultSet = null;

        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT ";

        switch (field) {
            case SDbRegistry.FIELD_CODE:
                msSql += "cty_abbr ";
                break;
            case SDbRegistry.FIELD_NAME:
                msSql += "cty ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlFromWhere(pk);

        resultSet = statement.executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            switch (field) {
                case SDbRegistry.FIELD_CODE:
                case SDbRegistry.FIELD_NAME:
                    value = resultSet.getString(1);
                    break;
                default:
            }
        }

        mnQueryResultId = SDbConsts.READ_OK;
        return value;
    }
}
