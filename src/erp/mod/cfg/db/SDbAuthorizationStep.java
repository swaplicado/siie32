/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.cfg.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona, Isabel Servín
 */
public class SDbAuthorizationStep extends SDbRegistryUser implements SGridRow {

    protected int mnPkAuthorizationStepId;
    protected String msResourceTableName_n;
    protected int mnResourcePkNum1_n;
    protected int mnResourcePkNum2_n;
    protected int mnResourcePkNum3_n;
    protected int mnResourcePkNum4_n;
    protected int mnResourcePkNum5_n;
    protected int mnResourcePkLength;
    protected int mnUserLevel;
    protected Date mtDateTimeAuthorized_n;
    protected Date mtDateTimeRejected_n;
    protected String msComments;
    protected int mnAuthorizationGrouper_n;
    protected int mnUserAuthorizationsNode_n;
    protected int mnPriority;
    protected boolean mbAllUsers;
    protected boolean mbAuthorized;
    protected boolean mbRejected;
    protected boolean mbRequired;
    //protected boolean mbDeleted;
    //protected boolean mbSystem;
    protected int mnFkAuthorizationTypeId;
    protected int mnFkAuthorizationPathId_n;
    protected int mnFkUserStepId;
    protected int mnFkNodeStepId_n;
    protected int mnFkUserAuthorizationId_n;
    protected int mnFkUserRejectId_n;
    //protected int mnFkUserInsertId;
    //protected int mnFkUserUpdateId;
    //protected Date mtTsUserInsert;
    //protected Date mtTsUserUpdate;

    protected String msAuxStepUsername;
    protected String msAuxAuthUsername;
    protected String msAuxRejectUsername;

    public SDbAuthorizationStep() {
        super(SModConsts.CFGU_AUTHORN_STEP);
    }
    
    public void setPkAuthorizationStepId(int n) { mnPkAuthorizationStepId = n; }
    public void setResourceTableName_n(String s) { msResourceTableName_n = s; }
    public void setResourcePkNum1_n(int n) { mnResourcePkNum1_n = n; }
    public void setResourcePkNum2_n(int n) { mnResourcePkNum2_n = n; }
    public void setResourcePkNum3_n(int n) { mnResourcePkNum3_n = n; }
    public void setResourcePkNum4_n(int n) { mnResourcePkNum4_n = n; }
    public void setResourcePkNum5_n(int n) { mnResourcePkNum5_n = n; }
    public void setResourcePkLength(int n) { mnResourcePkLength = n; }
    public void setUserLevel(int n) { mnUserLevel = n; }
    public void setDateTimeAuthorized_n(Date t) { mtDateTimeAuthorized_n = t; }
    public void setDateTimeRejected_n(Date t) { mtDateTimeRejected_n = t; }
    public void setComments(String s) { msComments = s; }
    public void setAuthorizationGrouper_n(int n) { mnAuthorizationGrouper_n = n; }
    public void setUserAuthorizationsNode_n(int n) { mnUserAuthorizationsNode_n = n; }
    public void setPriority(int n) { mnPriority = n; }
    public void setAllUsers(boolean b) { mbAllUsers = b; }
    public void setAuthorized(boolean b) { mbAuthorized = b; }
    public void setRejected(boolean b) { mbRejected = b; }
    public void setRequired(boolean b) { mbRequired = b; }
//    public void setDeleted(boolean b) { mbDeleted = b; }
//    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkAuthorizationTypeId(int n) { mnFkAuthorizationTypeId = n; }
    public void setFkAuthorizationPathId_n(int n) { mnFkAuthorizationPathId_n = n; }
    public void setFkUserStepId(int n) { mnFkUserStepId = n; }
    public void setFkNodeStepId_n(int n) { mnFkNodeStepId_n = n; }
    public void setFkUserAuthorizationId_n(int n) { mnFkUserAuthorizationId_n = n; }
    public void setFkUserRejectId_n(int n) { mnFkUserRejectId_n = n; }
//    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
//    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
//    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
//    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkAuthorizationStepId() { return mnPkAuthorizationStepId; }
    public String getResourceTableName_n() { return msResourceTableName_n; }
    public int getResourcePkNum1_n() { return mnResourcePkNum1_n; }
    public int getResourcePkNum2_n() { return mnResourcePkNum2_n; }
    public int getResourcePkNum3_n() { return mnResourcePkNum3_n; }
    public int getResourcePkNum4_n() { return mnResourcePkNum4_n; }
    public int getResourcePkNum5_n() { return mnResourcePkNum5_n; }
    public int getResourcePkLength() { return mnResourcePkLength; }
    public int getUserLevel() { return mnUserLevel; }
    public Date getDateTimeAuthorized_n() { return mtDateTimeAuthorized_n; }
    public Date getDateTimeRejected_n() { return mtDateTimeRejected_n; }
    public String getComments() { return msComments; }
    public int getAuthorizationGrouper_n() { return mnAuthorizationGrouper_n; }
    public int getUserAuthorizationsNode_n() { return mnUserAuthorizationsNode_n; }
    public int getPriority() { return mnPriority; }
    public boolean isAllUsers() { return mbAllUsers; }
    public boolean isAuthorized() { return mbAuthorized; }
    public boolean isRejected() { return mbRejected; }
    public boolean isRequired() { return mbRequired; }
//    public boolean isDeleted() { return mbDeleted; }
//    public boolean isSystem() { return mbSystem; }
    public int getFkAuthorizationTypeId() { return mnFkAuthorizationTypeId; }
    public int getFkAuthorizationPathId_n() { return mnFkAuthorizationPathId_n; }
    public int getFkUserStepId() { return mnFkUserStepId; }
    public int getFkNodeStepId_n() { return mnFkNodeStepId_n; }
    public int getFkUserAuthorizationId_n() { return mnFkUserAuthorizationId_n; }
    public int getFkUserRejectId_n() { return mnFkUserRejectId_n; }
//    public int getFkUserInsertId() { return mnFkUserInsertId; }
//    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
//    public Date getTsUserInsert() { return mtTsUserInsert; }
//    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    
    public String getAuxStepUsername() { return msAuxStepUsername; }
    public String getAuxAuthUsername() { return msAuxAuthUsername; }
    public String getAuxRejectUsername() { return msAuxRejectUsername; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAuthorizationStepId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAuthorizationStepId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkAuthorizationStepId = 0;
        msResourceTableName_n = "";
        mnResourcePkNum1_n = 0;
        mnResourcePkNum2_n = 0;
        mnResourcePkNum3_n = 0;
        mnResourcePkNum4_n = 0;
        mnResourcePkNum5_n = 0;
        mnResourcePkLength = 0;
        mnUserLevel = 0;
        mtDateTimeAuthorized_n = null;
        mtDateTimeRejected_n = null;
        msComments = "";
        mnAuthorizationGrouper_n = 0;
        mnUserAuthorizationsNode_n = 0;
        mnPriority = 0;
        mbAllUsers = false;
        mbAuthorized = false;
        mbRejected = false;
        mbRequired = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkAuthorizationTypeId = 0;
        mnFkAuthorizationPathId_n = 0;
        mnFkUserStepId = 0;
        mnFkNodeStepId_n = 0;
        mnFkUserAuthorizationId_n = 0;
        mnFkUserRejectId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        msAuxStepUsername = "";
        msAuxAuthUsername = "";
        msAuxRejectUsername = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_authorn_step = " + mnPkAuthorizationStepId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_authorn_step = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAuthorizationStepId = 0;

        msSql = "SELECT COALESCE(MAX(id_authorn_step), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAuthorizationStepId = resultSet.getInt(1);
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
            mnPkAuthorizationStepId = resultSet.getInt("id_authorn_step");
            msResourceTableName_n = resultSet.getString("res_tab_name_n");
            mnResourcePkNum1_n = resultSet.getInt("res_pk_n1_n");
            mnResourcePkNum2_n = resultSet.getInt("res_pk_n2_n");
            mnResourcePkNum3_n = resultSet.getInt("res_pk_n3_n");
            mnResourcePkNum4_n = resultSet.getInt("res_pk_n4_n");
            mnResourcePkNum5_n = resultSet.getInt("res_pk_n5_n");
            mnResourcePkLength = resultSet.getInt("res_pk_len");
            mnUserLevel = resultSet.getInt("lev");
            mtDateTimeAuthorized_n = resultSet.getTimestamp("dt_time_authorn_n");
            mtDateTimeRejected_n = resultSet.getTimestamp("dt_time_reject_n");
            msComments = resultSet.getString("comments");
            mnAuthorizationGrouper_n = resultSet.getInt("authorn_grouper_n");
            mnUserAuthorizationsNode_n = resultSet.getInt("usrs_authorn_node_n");
            mnPriority = resultSet.getInt("priority");
            mbAllUsers = resultSet.getBoolean("b_all");
            mbAuthorized = resultSet.getBoolean("b_authorn");
            mbRejected = resultSet.getBoolean("b_reject");
            mbRequired = resultSet.getBoolean("b_req");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkAuthorizationTypeId = resultSet.getInt("fk_tp_authorn");
            mnFkAuthorizationPathId_n = resultSet.getInt("fk_authorn_path_n");
            mnFkUserStepId = resultSet.getInt("fk_usr_step");
            mnFkNodeStepId_n = resultSet.getInt("fk_node_step_n");
            mnFkUserAuthorizationId_n = resultSet.getInt("fk_usr_authorn_n");
            mnFkUserRejectId_n = resultSet.getInt("fk_usr_reject_n");
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
        
        String userAuth = "SELECT usr "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.USRU_USR) 
                    + " WHERE id_usr = " + mnFkUserAuthorizationId_n;
        
        ResultSet resultSetAuth = session.getStatement().getConnection().createStatement().executeQuery(userAuth);
        if (resultSetAuth.next()) {
            msAuxAuthUsername = resultSetAuth.getString("usr");
        }
        else {
            msAuxAuthUsername = "";
        }
        
        String userReject = "SELECT usr "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.USRU_USR) 
                    + " WHERE id_usr = " + mnFkUserRejectId_n;
        
        ResultSet resultSetReject = session.getStatement().getConnection().createStatement().executeQuery(userReject);
        if (resultSetReject.next()) {
            msAuxRejectUsername = resultSetReject.getString("usr");
        }
        else {
            msAuxRejectUsername = "";
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
                    mnPkAuthorizationStepId + ", " + 
                    (msResourceTableName_n != null ? "'" + msResourceTableName_n + "'" : "null") + ", " +
                    (mnResourcePkNum1_n > 0 ? "" + mnResourcePkNum1_n + "" : "null") + ", " +
                    (mnResourcePkNum2_n > 0 ? "" + mnResourcePkNum2_n + "" : "null") + ", " +
                    (mnResourcePkNum3_n > 0 ? "" + mnResourcePkNum3_n + "" : "null") + ", " +
                    (mnResourcePkNum4_n > 0 ? "" + mnResourcePkNum4_n + "" : "null") + ", " +
                    (mnResourcePkNum5_n > 0 ? "" + mnResourcePkNum5_n + "" : "null") + ", " +
                    mnResourcePkLength + ", " + 
                    mnUserLevel + ", " + 
                    (mtDateTimeAuthorized_n == null ? "null" : ("'" + SLibUtils.DbmsDateFormatDatetime.format(mtDateTimeAuthorized_n) + "'")) + ", " + 
                    (mtDateTimeRejected_n == null ? "null" : ("'" + SLibUtils.DbmsDateFormatDatetime.format(mtDateTimeRejected_n) + "'")) + ", " + 
                    "'" + msComments + "', " + 
                    (mnAuthorizationGrouper_n == 0 ? "null" : ("'" + mnAuthorizationGrouper_n + "'")) + ", " + 
                    (mnUserAuthorizationsNode_n == 0 ? "null" : mnUserAuthorizationsNode_n) + ", " + 
                    mnPriority + ", " + 
                    (mbAllUsers ? 1 : 0) + ", " + 
                    (mbAuthorized ? 1 : 0) + ", " + 
                    (mbRejected ? 1 : 0) + ", " + 
                    (mbRequired ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkAuthorizationTypeId + ", " + 
                    (mnFkAuthorizationPathId_n == 0 ? "null" : mnFkAuthorizationPathId_n) + ", " +
                    mnFkUserStepId + ", " + 
                    (mnFkNodeStepId_n == 0 ? "null" : mnFkNodeStepId_n) + ", " +
                    (mnFkUserAuthorizationId_n == 0 ? "null" : mnFkUserAuthorizationId_n) + ", " +
                    (mnFkUserRejectId_n == 0 ? "null" : mnFkUserRejectId_n) + ", " +
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
//                        "id_authorn_step = " + mnPkAuthorizationStepId + ", " +
                        "res_tab_name_n = " + (msResourceTableName_n != null ? ("'" + msResourceTableName_n + "'") : "null") + ", " +
                        "res_pk_n1_n = " + (mnResourcePkNum1_n > 0 ? ("" + mnResourcePkNum1_n + "") : "null") + ", " +
                        "res_pk_n2_n = " + (mnResourcePkNum2_n > 0 ? ("" + mnResourcePkNum2_n + "") : "null") + ", " +
                        "res_pk_n3_n = " + (mnResourcePkNum3_n > 0 ? ("" + mnResourcePkNum3_n + "") : "null") + ", " +
                        "res_pk_n4_n = " + (mnResourcePkNum4_n > 0 ? ("" + mnResourcePkNum4_n + "") : "null") + ", " +
                        "res_pk_n5_n = " + (mnResourcePkNum5_n > 0 ? ("" + mnResourcePkNum5_n + "") : "null") + ", " +
                        "res_pk_len = " + mnResourcePkLength + ", " +
                        "lev = " + mnUserLevel + ", " +
                        "dt_time_authorn_n = " + (mtDateTimeAuthorized_n != null ? ("'" + SLibUtils.DbmsDateFormatDatetime.format(mtDateTimeAuthorized_n) + "'") : "NULL") + ", " +
                        "dt_time_reject_n = " + (mtDateTimeRejected_n != null ? ("'" + SLibUtils.DbmsDateFormatDatetime.format(mtDateTimeRejected_n) + "'") : "NULL") + ", " +
                        "comments = '" + msComments + "', " +
                        "authorn_grouper_n = " + (mnAuthorizationGrouper_n > 0 ? mnAuthorizationGrouper_n : "NULL") + ", " +
                        "usrs_authorn_node_n = " + (mnUserAuthorizationsNode_n > 0 ? mnUserAuthorizationsNode_n : "NULL") + ", " +
                        "priority = " + mnPriority + ", " +
                        "b_all = " + (mbAllUsers ? 1 : 0) + ", " +
                        "b_authorn = " + (mbAuthorized ? 1 : 0) + ", " +
                        "b_reject = " + (mbRejected ? 1 : 0) + ", " +
                        "b_req = " + (mbRequired ? 1 : 0) + ", " +
                        "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                        "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                        "fk_tp_authorn = " + mnFkAuthorizationTypeId + ", " +
                        "fk_authorn_path_n = " + (mnFkAuthorizationPathId_n == 0 ? "null" : mnFkAuthorizationPathId_n) + ", " +
                        "fk_usr_step = " + mnFkUserStepId + ", " +
                        "fk_node_step_n = " + (mnFkNodeStepId_n == 0 ? "null" : mnFkNodeStepId_n) + ", " +
                        "fk_usr_authorn_n = " + (mnFkUserAuthorizationId_n == 0 ? "null" : mnFkUserAuthorizationId_n) + ", " +
                        "fk_usr_reject_n = " + (mnFkUserRejectId_n == 0 ? "null" : mnFkUserRejectId_n) + ", " +
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
        SDbAuthorizationStep registry = new SDbAuthorizationStep();

        registry.setPkAuthorizationStepId(this.getPkAuthorizationStepId());
        registry.setResourceTableName_n(this.getResourceTableName_n());
        registry.setResourcePkNum1_n(this.getResourcePkNum1_n());
        registry.setResourcePkNum2_n(this.getResourcePkNum2_n());
        registry.setResourcePkNum3_n(this.getResourcePkNum3_n());
        registry.setResourcePkNum4_n(this.getResourcePkNum4_n());
        registry.setResourcePkNum5_n(this.getResourcePkNum5_n());
        registry.setResourcePkLength(this.getResourcePkLength());
        registry.setUserLevel(this.getUserLevel());
        registry.setDateTimeAuthorized_n(this.getDateTimeAuthorized_n());
        registry.setDateTimeRejected_n(this.getDateTimeRejected_n());
        registry.setComments(this.getComments());
        registry.setAllUsers(this.isAllUsers());
        registry.setAuthorizationGrouper_n(this.getAuthorizationGrouper_n());
        registry.setUserAuthorizationsNode_n(this.getUserAuthorizationsNode_n());
        registry.setPriority(this.getPriority());
        registry.setAuthorized(this.isAuthorized());
        registry.setRejected(this.isRejected());
        registry.setRequired(this.isRequired());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkAuthorizationTypeId(this.getFkAuthorizationTypeId());
        registry.setFkAuthorizationPathId_n(this.getFkAuthorizationPathId_n());
        registry.setFkUserStepId(this.getFkUserStepId());
        registry.setFkNodeStepId_n(this.getFkNodeStepId_n());
        registry.setFkUserAuthorizationId_n(this.getFkUserAuthorizationId_n());
        registry.setFkUserRejectId_n(this.getFkUserRejectId_n());
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
                msSql += "res_tab_name_n ";
                break;
            case SDbRegistry.FIELD_NAME:
                msSql += "comments ";
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
                value = msAuxStepUsername;
                break;
            case 3:
                value = mbAuthorized;
                break;
            case 4:
                value = mtDateTimeAuthorized_n;
                break;
            case 5:
                value = msAuxAuthUsername;
                break;
            case 6:
                value = mbRejected;
                break;
            case 7:
                value = mtDateTimeRejected_n;
                break;
            case 8:
                value = msComments;
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
