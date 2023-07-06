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
public class SDbAuthorizationPath extends SDbRegistryUser {

    protected int mnPkAuthorizationPathId;
    protected String msConditionTableName;
    protected String msConditionField;
    protected String msConditionOperator;
    protected String msConditionValue;
    protected int mnUserLevel;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    protected boolean mbRequired;
//    protected boolean mbDeleted;
//    protected boolean mbSystem;
    protected int mnFkAuthorizationTypeId;
    protected int mnFkUserAuthorizationId;
//    protected int mnFkUserInsertId;
//    protected int mnFkUserUpdateId;
//    protected Date mtTsUserInsert;
//    protected Date mtTsUserUpdate;

    public SDbAuthorizationPath() {
        super(SModConsts.CFGU_AUTHORN_PATH);
    }
    
    public void setPkAuthorizationPathId(int n) { mnPkAuthorizationPathId = n; }
    public void setConditionTableName(String s) { msConditionTableName = s; }
    public void setConditionField(String s) { msConditionField = s; }
    public void setConditionOperator(String s) { msConditionOperator = s; }
    public void setConditionValue(String s) { msConditionValue = s; }
    public void setUserLevel(int n) { mnUserLevel = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setRequired(boolean b) { mbRequired = b; }
//    public void setDeleted(boolean b) { mbDeleted = b; }
//    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkAuthorizationTypeId(int n) { mnFkAuthorizationTypeId = n; }
    public void setFkUserAuthorizationId(int n) { mnFkUserAuthorizationId = n; }
//    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
//    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
//    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
//    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkAuthorizationPathId() { return mnPkAuthorizationPathId; }
    public String getCondictionTableName() { return msConditionTableName; }
    public String getConditionField() { return msConditionField; }
    public String getConditionOperator() { return msConditionOperator; }
    public String getConditionValue() { return msConditionValue; }
    public int getUserLevel() { return mnUserLevel; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public boolean isRequired() { return mbRequired; }
//    public boolean isDeleted() { return mbDeleted; }
//    public boolean isSystem() { return mbSystem; }
    public int getFkAuthorizationTypeId() { return mnFkAuthorizationTypeId; }
    public int getFkUserAuthorizationId() { return mnFkUserAuthorizationId; }
//    public int getFkUserInsertId() { return mnFkUserInsertId; }
//    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
//    public Date getTsUserInsert() { return mtTsUserInsert; }
//    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAuthorizationPathId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAuthorizationPathId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkAuthorizationPathId = 0;
        msConditionTableName = "";
        msConditionField = "";
        msConditionOperator = "";
        msConditionValue = "";
        mnUserLevel = 0;
        mtDateStart = null;
        mtDateEnd = null;
        mbRequired = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkAuthorizationTypeId = 0;
        mnFkUserAuthorizationId = 0;
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
        return "WHERE id_authorn_path = " + mnPkAuthorizationPathId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_authorn_path = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAuthorizationPathId = 0;

        msSql = "SELECT COALESCE(MAX(id_authorn_path), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAuthorizationPathId = resultSet.getInt(1);
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
            mnPkAuthorizationPathId = resultSet.getInt("id_authorn_path");
            msConditionTableName = resultSet.getString("cond_tab_name");
            msConditionField = resultSet.getString("cond_field");
            msConditionOperator = resultSet.getString("cond_operator");
            msConditionValue = resultSet.getString("cond_value");
            mnUserLevel = resultSet.getInt("lev");
            mtDateStart = resultSet.getDate("dt_sta");
            mtDateEnd = resultSet.getDate("dt_end_n");
            mbRequired = resultSet.getBoolean("b_req");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkAuthorizationTypeId = resultSet.getInt("fk_auth_type");
            mnFkUserAuthorizationId = resultSet.getInt("fk_usr_auth");
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
                    mnPkAuthorizationPathId + ", " + 
                    "'" + msConditionTableName + "', " + 
                    "'" + msConditionField + "', " + 
                    "'" + msConditionOperator + "', " + 
                    "'" + msConditionValue + "', " + 
                    mnUserLevel + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " + 
                    (mbRequired ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkAuthorizationTypeId + ", " + 
                    mnFkUserAuthorizationId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
//                    "id_authorn_path = " + mnPkAuthorizationPathId + ", " +
                    "cond_tab_name = '" + msConditionTableName + "', " +
                    "cond_field = '" + msConditionField + "', " +
                    "cond_operator = '" + msConditionOperator + "', " +
                    "cond_value = '" + msConditionValue + "', " +
                    "lev = " + mnUserLevel + ", " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end_n = '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    "b_req = " + (mbRequired ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_auth_type = " + mnFkAuthorizationTypeId + ", " +
                    "fk_usr_auth = " + mnFkUserAuthorizationId + ", " +
//                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
//                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbAuthorizationPath registry = new SDbAuthorizationPath();

        registry.setPkAuthorizationPathId(this.getPkAuthorizationPathId());
        registry.setConditionTableName(this.getCondictionTableName());
        registry.setConditionField(this.getConditionField());
        registry.setConditionOperator(this.getConditionOperator());
        registry.setConditionValue(this.getConditionValue());
        registry.setUserLevel(this.getUserLevel());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd(this.getDateEnd());
        registry.setRequired(this.isRequired());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkAuthorizationTypeId(this.getFkAuthorizationTypeId());
        registry.setFkUserAuthorizationId(this.getFkUserAuthorizationId());
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
                msSql += "cond_tab_name ";
                break;
            case SDbRegistry.FIELD_NAME:
                msSql += "cond_value ";
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
