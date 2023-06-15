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
import sa.lib.grid.SGridRow;

/**
 *
 * @author Edwin Carmona
 */
public class SDbAuthStep extends SDbRegistryUser implements SGridRow {

    protected int mnPkStepId;
    protected String msResourceTableName_n;
    protected String msResourcePkNum1_n;
    protected String msResourcePkNum2_n;
    protected String msResourcePkNum3_n;
    protected String msResourcePkNum4_n;
    protected String msResourcePkNum5_n;
    protected String msResourcePkNum6_n;
    protected String msResourcePkNum7_n;
    protected String msResourcePkNum8_n;
    protected String msResourcePkNum9_n;
    protected String msResourcePkNum10_n;
    protected int mnResourcePkLength;
    protected int mnUserLevel;
    protected Date mtDateTimeAuthorized_n;
    protected Date mtDateTimeRejected_n;
    protected String msComments;
    protected boolean mbAuthorized;
    protected boolean mbRejected;
    protected boolean mbRequired;
//    protected boolean mbDeleted;
//    protected boolean mbSystem;
    protected int mnFkAuthTypeId;
    protected int mnFkCfgAuthId_n;
    protected int mnFkUserStepId;
    protected int mnFkUserAuthId_n;
    protected int mnFkUserRejId_n;
//    protected int mnFkUserInsertId;
//    protected int mnFkUserUpdateId;
//    protected Date mtTsUserInsert;
//    protected Date mtTsUserUpdate;
    
    protected String msAuxStepUsername;

    public SDbAuthStep() {
        super(SModConsts.CFGU_AUTH_STEP);
    }
    
    public void setPkStepId(int n) { mnPkStepId = n; }
    public void setResourceTableName_n(String s) { msResourceTableName_n = s; }
    public void setResourcePkNum1_n(String s) { msResourcePkNum1_n = s; }
    public void setResourcePkNum2_n(String s) { msResourcePkNum2_n = s; }
    public void setResourcePkNum3_n(String s) { msResourcePkNum3_n = s; }
    public void setResourcePkNum4_n(String s) { msResourcePkNum4_n = s; }
    public void setResourcePkNum5_n(String s) { msResourcePkNum5_n = s; }
    public void setResourcePkNum6_n(String s) { msResourcePkNum6_n = s; }
    public void setResourcePkNum7_n(String s) { msResourcePkNum7_n = s; }
    public void setResourcePkNum8_n(String s) { msResourcePkNum8_n = s; }
    public void setResourcePkNum9_n(String s) { msResourcePkNum9_n = s; }
    public void setResourcePkNum10_n(String s) { msResourcePkNum10_n = s; }
    public void setResourcePkLength(int n) { mnResourcePkLength = n; }
    public void setUserLevel(int n) { mnUserLevel = n; }
    public void setDateTimeAuthorized_n(Date t) { mtDateTimeAuthorized_n = t; }
    public void setDateTimeRejected_n(Date t) { mtDateTimeRejected_n = t; }
    public void setComments(String s) { msComments = s; }
    public void setAuthorized(boolean b) { mbAuthorized = b; }
    public void setRejected(boolean b) { mbRejected = b; }
    public void setRequired(boolean b) { mbRequired = b; }
//    public void setDeleted(boolean b) { mbDeleted = b; }
//    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkAuthTypeId(int n) { mnFkAuthTypeId = n; }
    public void setFkCfgAuthId_n(int n) { mnFkCfgAuthId_n = n; }
    public void setFkUserStepId(int n) { mnFkUserStepId = n; }
    public void setFkUserAuthId_n(int n) { mnFkUserAuthId_n = n; }
    public void setFkUserRejId_n(int n) { mnFkUserRejId_n = n; }
//    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
//    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
//    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
//    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkStepId() { return mnPkStepId; }
    public String getResourceTableName_n() { return msResourceTableName_n; }
    public String getResourcePkNum1_n() { return msResourcePkNum1_n; }
    public String getResourcePkNum2_n() { return msResourcePkNum2_n; }
    public String getResourcePkNum3_n() { return msResourcePkNum3_n; }
    public String getResourcePkNum4_n() { return msResourcePkNum4_n; }
    public String getResourcePkNum5_n() { return msResourcePkNum5_n; }
    public String getResourcePkNum6_n() { return msResourcePkNum6_n; }
    public String getResourcePkNum7_n() { return msResourcePkNum7_n; }
    public String getResourcePkNum8_n() { return msResourcePkNum8_n; }
    public String getResourcePkNum9_n() { return msResourcePkNum9_n; }
    public String getResourcePkNum10_n() { return msResourcePkNum10_n; }
    public int getResourcePkLength() { return mnResourcePkLength; }
    public int getUserLevel() { return mnUserLevel; }
    public Date getDateTimeAuthorized_n() { return mtDateTimeAuthorized_n; }
    public Date getDateTimeRejected_n() { return mtDateTimeRejected_n; }
    public String getComments() { return msComments; }
    public boolean isAuthorized() { return mbAuthorized; }
    public boolean isRejected() { return mbRejected; }
    public boolean isRequired() { return mbRequired; }
//    public boolean isDeleted() { return mbDeleted; }
//    public boolean isSystem() { return mbSystem; }
    public int getFkAuthTypeId() { return mnFkAuthTypeId; }
    public int getFkCfgAuthId_n() { return mnFkCfgAuthId_n; }
    public int getFkUserStepId() { return mnFkUserStepId; }
    public int getFkUserAuthId_n() { return mnFkUserAuthId_n; }
    public int getFkUserRejId_n() { return mnFkUserRejId_n; }
//    public int getFkUserInsertId() { return mnFkUserInsertId; }
//    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
//    public Date getTsUserInsert() { return mtTsUserInsert; }
//    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public String getAuxStepUsername() { return msAuxStepUsername; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkStepId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStepId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStepId = 0;
        msResourceTableName_n = "";
        msResourcePkNum1_n = "";
        msResourcePkNum2_n = "";
        msResourcePkNum3_n = "";
        msResourcePkNum4_n = "";
        msResourcePkNum5_n = "";
        msResourcePkNum6_n = "";
        msResourcePkNum7_n = "";
        msResourcePkNum8_n = "";
        msResourcePkNum9_n = "";
        msResourcePkNum10_n = "";
        mnResourcePkLength = 0;
        mnUserLevel = 0;
        mtDateTimeAuthorized_n = null;
        mtDateTimeRejected_n = null;
        msComments = "";
        mbAuthorized = false;
        mbRejected = false;
        mbRequired = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkAuthTypeId = 0;
        mnFkCfgAuthId_n = 0;
        mnFkUserStepId = 0;
        mnFkUserAuthId_n = 0;
        mnFkUserRejId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        msAuxStepUsername = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_auth_step = " + mnPkStepId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_auth_step = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkStepId = 0;

        msSql = "SELECT COALESCE(MAX(id_auth_step), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkStepId = resultSet.getInt(1);
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
            mnPkStepId = resultSet.getInt("id_auth_step");
            msResourceTableName_n = resultSet.getString("res_tab_name_n");
            msResourcePkNum1_n = resultSet.getString("res_pk_n1_n");
            msResourcePkNum2_n = resultSet.getString("res_pk_n2_n");
            msResourcePkNum3_n = resultSet.getString("res_pk_n3_n");
            msResourcePkNum4_n = resultSet.getString("res_pk_n4_n");
            msResourcePkNum5_n = resultSet.getString("res_pk_n5_n");
            msResourcePkNum6_n = resultSet.getString("res_pk_n6_n");
            msResourcePkNum7_n = resultSet.getString("res_pk_n7_n");
            msResourcePkNum8_n = resultSet.getString("res_pk_n8_n");
            msResourcePkNum9_n = resultSet.getString("res_pk_n9_n");
            msResourcePkNum10_n = resultSet.getString("res_pk_n10_n");
            mnResourcePkLength = resultSet.getInt("res_pk_len");
            mnUserLevel = resultSet.getInt("lev");
            mtDateTimeAuthorized_n = resultSet.getTimestamp("dt_time_auth_n");
            mtDateTimeRejected_n = resultSet.getTimestamp("dt_time_rej_n");
            msComments = resultSet.getString("comments");
            mbAuthorized = resultSet.getBoolean("b_auth");
            mbRejected = resultSet.getBoolean("b_rej");
            mbRequired = resultSet.getBoolean("b_req");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkAuthTypeId = resultSet.getInt("fk_auth_type");
            mnFkCfgAuthId_n = resultSet.getInt("fk_auth_cfg_n");
            mnFkUserStepId = resultSet.getInt("fk_usr_step");
            mnFkUserAuthId_n = resultSet.getInt("fk_usr_auth_n");
            mnFkUserRejId_n = resultSet.getInt("fk_usr_rej_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
        }
        
        String complement = "SELECT usr "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.USRU_USR) 
                    + " WHERE id_usr = " + mnFkUserStepId;
        
        ResultSet resultSetCom = session.getStatement().getConnection().createStatement().executeQuery(complement);
        if (resultSetCom.next()) {
            msAuxStepUsername = resultSetCom.getString("usr");
        }
        else {
            msAuxStepUsername = "";
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
                    mnPkStepId + ", " + 
                    (msResourceTableName_n != null ? "'" + msResourceTableName_n + "'" : "null") + ", " +
                    (msResourcePkNum1_n != null ? "'" + msResourcePkNum1_n + "'" : "null") + ", " +
                    (msResourcePkNum2_n != null ? "'" + msResourcePkNum2_n + "'" : "null") + ", " +
                    (msResourcePkNum3_n != null ? "'" + msResourcePkNum3_n + "'" : "null") + ", " +
                    (msResourcePkNum4_n != null ? "'" + msResourcePkNum4_n + "'" : "null") + ", " +
                    (msResourcePkNum5_n != null ? "'" + msResourcePkNum5_n + "'" : "null") + ", " +
                    (msResourcePkNum6_n != null ? "'" + msResourcePkNum6_n + "'" : "null") + ", " +
                    (msResourcePkNum7_n != null ? "'" + msResourcePkNum7_n + "'" : "null") + ", " +
                    (msResourcePkNum8_n != null ? "'" + msResourcePkNum8_n + "'" : "null") + ", " +
                    (msResourcePkNum9_n != null ? "'" + msResourcePkNum9_n + "'" : "null") + ", " +
                    (msResourcePkNum10_n != null ? "'" + msResourcePkNum10_n + "'" : "null") + ", " +
                    mnResourcePkLength + ", " + 
                    mnUserLevel + ", " + 
                    (mtDateTimeAuthorized_n == null ? "null" : ("'" + SLibUtils.DbmsDateFormatDatetime.format(mtDateTimeAuthorized_n) + "'")) + ", " + 
                    (mtDateTimeRejected_n == null ? "null" : ("'" + SLibUtils.DbmsDateFormatDatetime.format(mtDateTimeRejected_n) + "'")) + ", " + 
                    "'" + msComments + "', " + 
                    (mbAuthorized ? 1 : 0) + ", " + 
                    (mbRejected ? 1 : 0) + ", " + 
                    (mbRequired ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkAuthTypeId + ", " + 
                    (mnFkCfgAuthId_n == 0 ? "null" : mnFkCfgAuthId_n) + ", " +
                    mnFkUserStepId + ", " + 
                    (mnFkUserAuthId_n == 0 ? "null" : mnFkUserAuthId_n) + ", " +
                    (mnFkUserRejId_n == 0 ? "null" : mnFkUserRejId_n) + ", " +
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
//                        "id_auth_step = " + mnPkStepId + ", " +
                        "res_tab_name_n = " + (msResourceTableName_n != null ? ("'" + msResourceTableName_n + "'") : "null") + ", " +
                        "res_pk_n1_n = " + (msResourcePkNum1_n != null ? ("'" + msResourcePkNum1_n + "'") : "null") + ", " +
                        "res_pk_n2_n = " + (msResourcePkNum2_n != null ? ("'" + msResourcePkNum2_n + "'") : "null") + ", " +
                        "res_pk_n3_n = " + (msResourcePkNum3_n != null ? ("'" + msResourcePkNum3_n + "'") : "null") + ", " +
                        "res_pk_n4_n = " + (msResourcePkNum4_n != null ? ("'" + msResourcePkNum4_n + "'") : "null") + ", " +
                        "res_pk_n5_n = " + (msResourcePkNum5_n != null ? ("'" + msResourcePkNum5_n + "'") : "null") + ", " +
                        "res_pk_n6_n = " + (msResourcePkNum6_n != null ? ("'" + msResourcePkNum6_n + "'") : "null") + ", " +
                        "res_pk_n7_n = " + (msResourcePkNum7_n != null ? ("'" + msResourcePkNum7_n + "'") : "null") + ", " +
                        "res_pk_n8_n = " + (msResourcePkNum8_n != null ? ("'" + msResourcePkNum8_n + "'") : "null") + ", " +
                        "res_pk_n9_n = " + (msResourcePkNum9_n != null ? ("'" + msResourcePkNum9_n + "'") : "null") + ", " +
                        "res_pk_n10_n = " + (msResourcePkNum10_n != null ? ("'" + msResourcePkNum10_n + "'") : "null") + ", " +
                        "res_pk_len = " + mnResourcePkLength + ", " +
                        "lev = " + mnUserLevel + ", " +
                        "dt_time_auth_n = " + (mtDateTimeAuthorized_n != null ? ("'" + SLibUtils.DbmsDateFormatDatetime.format(mtDateTimeAuthorized_n) + "'") : "NULL") + ", " +
                        "dt_time_rej_n = " + (mtDateTimeRejected_n != null ? ("'" + SLibUtils.DbmsDateFormatDatetime.format(mtDateTimeRejected_n) + "'") : "NULL") + ", " +
                        "comments = '" + msComments + "', " +
                        "b_auth = " + (mbAuthorized ? 1 : 0) + ", " +
                        "b_rej = " + (mbRejected ? 1 : 0) + ", " +
                        "b_req = " + (mbRequired ? 1 : 0) + ", " +
                        "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                        "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                        "fk_auth_type = " + mnFkAuthTypeId + ", " +
                        "fk_auth_cfg_n = " + (mnFkCfgAuthId_n == 0 ? "null" : mnFkCfgAuthId_n) + ", " +
                        "fk_usr_step = " + mnFkUserStepId + ", " +
                        "fk_usr_auth_n = " + (mnFkUserAuthId_n == 0 ? "null" : mnFkUserAuthId_n) + ", " +
                        "fk_usr_rej_n = " + (mnFkUserRejId_n == 0 ? "null" : mnFkUserRejId_n) + ", " +
//                        "fk_usr_ins = " + mnFkUserInsertId + ", " +
                        "fk_usr_upd = " + mnFkUserUpdateId + ", " +
//                        "ts_usr_ins = " + "NOW()" + ", " +
                        "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbAuthStep registry = new SDbAuthStep();

        registry.setPkStepId(this.getPkStepId());
        registry.setResourceTableName_n(this.getResourceTableName_n());
        registry.setResourcePkNum1_n(this.getResourcePkNum1_n());
        registry.setResourcePkNum2_n(this.getResourcePkNum2_n());
        registry.setResourcePkNum3_n(this.getResourcePkNum3_n());
        registry.setResourcePkNum4_n(this.getResourcePkNum4_n());
        registry.setResourcePkNum5_n(this.getResourcePkNum5_n());
        registry.setResourcePkNum6_n(this.getResourcePkNum6_n());
        registry.setResourcePkNum7_n(this.getResourcePkNum7_n());
        registry.setResourcePkNum8_n(this.getResourcePkNum8_n());
        registry.setResourcePkNum9_n(this.getResourcePkNum9_n());
        registry.setResourcePkNum10_n(this.getResourcePkNum10_n());
        registry.setResourcePkLength(this.getResourcePkLength());
        registry.setUserLevel(this.getUserLevel());
        registry.setDateTimeAuthorized_n(this.getDateTimeAuthorized_n());
        registry.setDateTimeRejected_n(this.getDateTimeRejected_n());
        registry.setComments(this.getComments());
        registry.setAuthorized(this.isAuthorized());
        registry.setRejected(this.isRejected());
        registry.setRequired(this.isRequired());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkAuthTypeId(this.getFkAuthTypeId());
        registry.setFkCfgAuthId_n(this.getFkCfgAuthId_n());
        registry.setFkUserStepId(this.getFkUserStepId());
        registry.setFkUserAuthId_n(this.getFkUserAuthId_n());
        registry.setFkUserRejId_n(this.getFkUserRejId_n());
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

    @Override
    public int[] getRowPrimaryKey() {
        return this.getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return this.isSystem();
    }

    @Override
    public boolean isRowDeletable() {
        return false;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {
        
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch (row) {
            case 0:
                value = mnUserLevel;
                break;
            case 1:
                value = mbRequired;
                break;
            case 2:
                value = mbAuthorized;
                break;
            case 3:
                value = mtDateTimeAuthorized_n;
                break;
            case 4:
                value = mbRejected;
                break;
            case 5:
                value = mtDateTimeRejected_n;
                break;
            case 6:
                value = msAuxStepUsername;
                break;
            default:
                break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("setrowAt"); //To change body of generated methods, choose Tools | Templates.
    }
}
