/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataUserConfigurationTransaction;
import erp.mtrn.data.STrnUtilities;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import sa.gui.util.SUtilConsts;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;
import sa.lib.gui.SGuiSessionCustom;
import sa.lib.gui.SGuiUser;

/**
 *
 * @author Sergio Flores, Alfonso Flores, Claudio Peña, Edwin Carmona
 */
public class SDataUser extends SDataRegistry implements Serializable, SGuiUser {
    
    protected int mnPkUserId;
    protected java.lang.String msUser;
    protected java.lang.String msUserPassword;
    protected java.lang.String msEmail;
    protected boolean mbIsUniversal;
    protected boolean mbIsCanEdit;
    protected boolean mbIsCanDelete;
    protected boolean mbIsActive;
    protected boolean mbIsDeleted;
    protected int mnFkUserTypeId;
    protected int mnFkBizPartnerId_n;
    protected int mnFkUserLastSyncId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserLastSyncTs_n;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;
    
    // members for management of data registry:

    protected java.util.Vector<SDataTypeModule> mvDbmsTypeModules;
    protected java.util.Vector<SDataUserPrivilegeUser> mvDbmsUserPrivilegesUser;
    protected java.util.Vector<SDataUserPrivilegeCompany> mvDbmsUserPrivilegesCompany;
    protected java.util.Vector<SDataUserRoleUser> mvDbmsUserRolesUser;
    protected java.util.Vector<SDataUserRoleCompany> mvDbmsUserRolesCompany;
    protected java.util.Vector<SDataAccessCompany> mvDbmsAccessCompanies;
    protected java.util.Vector<SDataAccessCompanyBranch> mvDbmsAccessCompanyBranches;
    protected java.util.Vector<SDataAccessCompanyBranchEntity> mvDbmsAccessCompanyBranchEntities;
    protected java.util.Vector<SDataAccessCompanyBranchEntityUniversal> mvDbmsAccessCompanyBranchEntitiesUniversal;

    private erp.mtrn.data.SDataUserConfigurationTransaction moDbmsUserConfigurationTransaction;
    
    // members for management of user privileges in user session:
    
    private java.util.HashSet<Integer> moModuleAccess; // element: ID of module
    private java.util.HashMap<Integer, Integer> moPrivilegeUser; // key: 
    private java.util.HashMap<Integer, Integer> moRightsUser;
    private java.util.HashMap<int[], Integer> moRightsCompany;

    protected boolean mbExtraIsPasswordUpdateRequired;
    protected boolean mbExtraIsSpecialRoleSuper;
    protected boolean mbExtraIsSpecialRoleConfig;
    protected boolean mbExtraIsSpecialRoleAdmor;

    protected int mnAuxCompanyId;
    
    protected ArrayList<Integer> maUserGroupsIds;

    public SDataUser() {
        super(SDataConstants.USRU_USR);

        mvDbmsTypeModules = new Vector<>();
        mvDbmsUserPrivilegesUser = new Vector<>();
        mvDbmsUserPrivilegesCompany = new Vector<>();
        mvDbmsUserRolesUser = new Vector<>();
        mvDbmsUserRolesCompany = new Vector<>();
        mvDbmsAccessCompanies = new Vector<>();
        mvDbmsAccessCompanyBranches = new Vector<>();
        mvDbmsAccessCompanyBranchEntities = new Vector<>();
        mvDbmsAccessCompanyBranchEntitiesUniversal = new Vector<>();
        
        moModuleAccess = new HashSet<>();
        moPrivilegeUser = new HashMap<>();
        moRightsUser = new HashMap<>();
        moRightsCompany = new HashMap<>();
        
        maUserGroupsIds = new ArrayList<>();

        reset();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setUser(java.lang.String s) { msUser = s; }
    public void setUserPassword(java.lang.String s) { msUserPassword = s; }
    public void setEmail(java.lang.String s) { msEmail = s; }
    public void setIsUniversal(boolean b) { mbIsUniversal = b; }
    public void setIsCanEdit(boolean b) { mbIsCanEdit = b; }
    public void setIsCanDelete(boolean b) { mbIsCanDelete = b; }
    public void setIsActive(boolean b) { mbIsActive = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserTypeId(int n) { mnFkUserTypeId = n; }
    public void setFkBizPartnerId_n(int n) { mnFkBizPartnerId_n = n; }
    public void setFkUserLastSyncId_n(int n) { mnFkUserLastSyncId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserLastSyncTs_n(java.util.Date t) { mtUserLastSyncTs_n = t; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkUserId() { return mnPkUserId; }
    public java.lang.String getUser() { return msUser; }
    public java.lang.String getUserPassword() { return msUserPassword; }
    public java.lang.String getEmail() { return msEmail; }
    public boolean getIsUniversal() { return mbIsUniversal; }
    public boolean getIsCanEdit() { return mbIsCanEdit; }
    public boolean getIsCanDelete() { return mbIsCanDelete; }
    public boolean getIsActive() { return mbIsActive; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserTypeId() { return mnFkUserTypeId; }
    public int getFkBizPartnerId_n() { return mnFkBizPartnerId_n; }
    public int getFkUserLastSyncId_n() { return mnFkUserLastSyncId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserLastSyncTs_n() { return mtUserLastSyncTs_n; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public java.util.Vector<SDataTypeModule> getDbmsTypeModules() { return mvDbmsTypeModules; }
    public java.util.Vector<SDataUserPrivilegeUser> getDbmsUserPrivilegesUser() { return mvDbmsUserPrivilegesUser; }
    public java.util.Vector<SDataUserPrivilegeCompany> getDbmsUserPrivilegesCompany() { return mvDbmsUserPrivilegesCompany; }
    public java.util.Vector<SDataUserRoleUser> getDbmsUserRolesUser() { return mvDbmsUserRolesUser; }
    public java.util.Vector<SDataUserRoleCompany> getDbmsUserRolesCompany() { return mvDbmsUserRolesCompany; }
    public java.util.Vector<SDataAccessCompany> getDbmsAccessCompanies() { return mvDbmsAccessCompanies; }
    public java.util.Vector<SDataAccessCompanyBranch> getDbmsAccessCompanyBranches() { return mvDbmsAccessCompanyBranches; }
    public java.util.Vector<SDataAccessCompanyBranchEntity> getDbmsAccessCompanyBranchEntities() { return mvDbmsAccessCompanyBranchEntities; }
    public java.util.Vector<SDataAccessCompanyBranchEntityUniversal> getDbmsAccessCompanyBranchEntitiesUniversal() { return mvDbmsAccessCompanyBranchEntitiesUniversal; }
    
    public SDataUserConfigurationTransaction getDbmsConfigurationTransaction() { return moDbmsUserConfigurationTransaction; }

    public void setExtraIsPasswordUpdateRequired(boolean b) { mbExtraIsPasswordUpdateRequired = b; }

    public boolean getExtraIsPasswordUpdateRequired() { return mbExtraIsPasswordUpdateRequired; }

    public void setAuxCompanyId(int n) { mnAuxCompanyId = n; }
    
    public int getAuxCompanyId() { return mnAuxCompanyId; }
    
    public ArrayList<Integer> getUserGroupsIds() { return maUserGroupsIds; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkUserId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkUserId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkUserId = 0;
        msUser = "";
        msUserPassword = "";
        msEmail = "";
        mbIsUniversal = false;
        mbIsCanEdit = false;
        mbIsCanDelete = false;
        mbIsActive = false;
        mbIsDeleted = false;
        mnFkUserTypeId = 0;
        mnFkBizPartnerId_n = 0;
        mnFkUserLastSyncId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserLastSyncTs_n = null;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mvDbmsTypeModules.clear();
        mvDbmsUserPrivilegesUser.clear();
        mvDbmsUserPrivilegesCompany.clear();
        mvDbmsUserRolesUser.clear();
        mvDbmsUserRolesCompany.clear();
        mvDbmsAccessCompanies.clear();
        mvDbmsAccessCompanyBranches.clear();
        mvDbmsAccessCompanyBranchEntities.clear();
        mvDbmsAccessCompanyBranchEntitiesUniversal.clear();
        
        moDbmsUserConfigurationTransaction = null;

        moModuleAccess.clear();
        moPrivilegeUser.clear();
        moRightsUser.clear();
        moRightsCompany.clear();

        mbExtraIsPasswordUpdateRequired = false;
        mbExtraIsSpecialRoleSuper = false;
        mbExtraIsSpecialRoleConfig = false;
        mbExtraIsSpecialRoleAdmor = false;

        mnAuxCompanyId = 0;
        
        maUserGroupsIds.clear();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;
        java.sql.Statement statementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.usru_usr WHERE id_usr = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUserId = resultSet.getInt("id_usr");
                msUser = resultSet.getString("usr");
                msUserPassword = "";    // password is encrypted in DBMS, so is not usefull here
                msEmail = resultSet.getString("email");
                mbIsUniversal = resultSet.getBoolean("b_univ");
                mbIsCanEdit = resultSet.getBoolean("b_can_edit");
                mbIsCanDelete = resultSet.getBoolean("b_can_del");
                mbIsActive = resultSet.getBoolean("b_act");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserTypeId = resultSet.getInt("fid_tp_usr");
                mnFkBizPartnerId_n = resultSet.getInt("fid_bp_n");
                mnFkUserLastSyncId_n = resultSet.getInt("fid_usr_last_sync_n");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserLastSyncTs_n = resultSet.getTimestamp("ts_last_sync_n");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                statementAux = statement.getConnection().createStatement();

                // Read aswell the access permission to companies:

                sql = "SELECT id_usr, id_co FROM erp.usru_access_co WHERE id_usr = " + mnPkUserId + " ORDER BY id_co ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataAccessCompany company = new SDataAccessCompany();
                    if (company.read(new int[] { mnPkUserId, resultSet.getInt("id_co") }, statementAux) == SLibConstants.DB_ACTION_READ_OK) {
                        mvDbmsAccessCompanies.add(company);
                    }
                    else {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                }

                // Read aswell the access permission to company branches:

                sql = "SELECT id_usr, id_cob FROM erp.usru_access_cob WHERE id_usr = " + mnPkUserId + " ORDER BY id_cob ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataAccessCompanyBranch branch = new SDataAccessCompanyBranch();
                    if (branch.read(new int[] { mnPkUserId, resultSet.getInt("id_cob") }, statementAux) == SLibConstants.DB_ACTION_READ_OK) {
                        mvDbmsAccessCompanyBranches.add(branch);
                    }
                    else {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                }

                // Read aswell the access permission to branch entities:

                sql = "SELECT id_usr, id_cob, id_ent FROM erp.usru_access_cob_ent WHERE id_usr = " + mnPkUserId + " ORDER BY id_cob, id_ent ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataAccessCompanyBranchEntity entity = new SDataAccessCompanyBranchEntity();
                    if (entity.read(new int[] { mnPkUserId, resultSet.getInt("id_cob"), resultSet.getInt("id_ent") }, statementAux) == SLibConstants.DB_ACTION_READ_OK) {
                        mvDbmsAccessCompanyBranchEntities.add(entity);
                    }
                    else {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                }

                // Read aswell the access permission to branch entities universal:

                sql = "SELECT id_usr, id_cob, id_ct_ent FROM erp.usru_access_cob_ent_univ WHERE id_usr = " + mnPkUserId + " ORDER BY id_cob, id_ct_ent ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataAccessCompanyBranchEntityUniversal entityUniversal = new SDataAccessCompanyBranchEntityUniversal();
                    if (entityUniversal.read(new int[] { mnPkUserId, resultSet.getInt("id_cob"), resultSet.getInt("id_ct_ent") }, statementAux) == SLibConstants.DB_ACTION_READ_OK) {
                        mvDbmsAccessCompanyBranchEntitiesUniversal.add(entityUniversal);
                    }
                    else {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                }

                // Read aswell the privileges concerning user:

                sql = "SELECT id_usr, id_prv FROM erp.usru_prv_usr WHERE id_usr = " + mnPkUserId + " ORDER BY id_prv ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataUserPrivilegeUser priv = new SDataUserPrivilegeUser();
                    if (priv.read(new int[] { mnPkUserId, resultSet.getInt("id_prv") }, statementAux) == SLibConstants.DB_ACTION_READ_OK) {
                        mvDbmsUserPrivilegesUser.add(priv);
                    }
                    else {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                }

                // Read aswell the privileges concerning company:

                sql = "SELECT id_usr, id_co, id_prv FROM erp.usru_prv_co WHERE id_usr = " + mnPkUserId + " ORDER BY id_co, id_prv ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataUserPrivilegeCompany priv = new SDataUserPrivilegeCompany();
                    if (priv.read(new int[] { mnPkUserId, resultSet.getInt("id_co"), resultSet.getInt("id_prv") }, statementAux) == SLibConstants.DB_ACTION_READ_OK) {
                        mvDbmsUserPrivilegesCompany.add(priv);
                    }
                    else {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                }

                // Read aswell the roles concerning user:

                sql = "SELECT id_usr, id_rol FROM erp.usru_rol_usr WHERE id_usr = " + mnPkUserId + " ORDER BY id_rol ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataUserRoleUser role = new SDataUserRoleUser();
                    if (role.read(new int[] { mnPkUserId, resultSet.getInt("id_rol") }, statementAux) == SLibConstants.DB_ACTION_READ_OK) {
                        mvDbmsUserRolesUser.add(role);
                    }
                    else {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                }

                // Read aswell the roles concerning company:

                sql = "SELECT id_usr, id_co, id_rol FROM erp.usru_rol_co WHERE id_usr = " + mnPkUserId + " ORDER BY id_co, id_rol ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataUserRoleCompany role = new SDataUserRoleCompany();
                    if (role.read(new int[] { mnPkUserId, resultSet.getInt("id_co"), resultSet.getInt("id_rol") }, statementAux) == SLibConstants.DB_ACTION_READ_OK) {
                        mvDbmsUserRolesCompany.add(role);
                    }
                    else {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                }

                // Read aswell the configuration for transactions:

                sql = "SELECT id_usr FROM trn_usr_cfg WHERE id_usr = " + mnPkUserId + " ORDER BY id_usr ";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    SDataUserConfigurationTransaction configuration = new SDataUserConfigurationTransaction();
                    if (configuration.read(new int[] { mnPkUserId }, statementAux) == SLibConstants.DB_ACTION_READ_OK) {
                        moDbmsUserConfigurationTransaction = configuration;
                    }
                    else {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                }

                // Define special roles:

                for (SDataUserRoleUser role : mvDbmsUserRolesUser) {
                    switch (role.getPkRoleId()) {
                        case SDataConstantsSys.ROL_SPE_SUPER:
                            mbExtraIsSpecialRoleSuper = true;
                            mbExtraIsSpecialRoleConfig = true;
                            mbExtraIsSpecialRoleAdmor = true;
                            break;
                        case SDataConstantsSys.ROL_SPE_CONFIG:
                            mbExtraIsSpecialRoleConfig = true;
                            break;
                        case SDataConstantsSys.ROL_SPE_ADMOR:
                            mbExtraIsSpecialRoleAdmor = true;
                            break;
                        default:
                    }
                }

                // Prepare all user privileges in hash maps:

                prepareUserRights(statement);
                
                // Read user groups
                
                maUserGroupsIds = SUserUtils.getUserGroupsIds(statement, mnPkUserId);

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int param = 1;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;
        java.sql.Statement statement = null;
        java.sql.Statement statementAux = null;
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL erp.usru_usr_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(param++, mnPkUserId);
            callableStatement.setString(param++, msUser);
            callableStatement.setString(param++, msUserPassword);
            callableStatement.setString(param++, msEmail);
            callableStatement.setBoolean(param++, mbIsUniversal);
            callableStatement.setBoolean(param++, mbIsCanEdit);
            callableStatement.setBoolean(param++, mbIsCanDelete);
            callableStatement.setBoolean(param++, mbIsActive);
            callableStatement.setBoolean(param++, mbIsDeleted);
            callableStatement.setBoolean(param++, mbIsRegistryNew ? true : mbExtraIsPasswordUpdateRequired);
            callableStatement.setInt(param++, mnFkUserTypeId);
            if (mnFkBizPartnerId_n > 0) callableStatement.setInt(param++, mnFkBizPartnerId_n); else callableStatement.setNull(param++, java.sql.Types.INTEGER);
            if (mnFkUserLastSyncId_n > 0) callableStatement.setInt(param++, mnFkUserLastSyncId_n); else callableStatement.setNull(param++, java.sql.Types.SMALLINT);
            callableStatement.setInt(param++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(param++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(param++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(param++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnPkUserId = callableStatement.getInt(param - 3);
            mnDbmsErrorId = callableStatement.getInt(param - 2);
            msDbmsError = callableStatement.getString(param - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                // Save aswell the access permission to companies:

                param = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_access_co_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(param++, mnPkUserId);
                callableStatement.registerOutParameter(param++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(param++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsAccessCompanies.size(); i++) {
                    mvDbmsAccessCompanies.get(i).setPkUserId(mnPkUserId);
                    if (mvDbmsAccessCompanies.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell the access permission to company branches:

                param = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_access_cob_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(param++, mnPkUserId);
                callableStatement.registerOutParameter(param++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(param++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsAccessCompanyBranches.size(); i++) {
                    mvDbmsAccessCompanyBranches.get(i).setPkUserId(mnPkUserId);
                    if (mvDbmsAccessCompanyBranches.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell the access permission to branch entities:

                param = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_access_cob_ent_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(param++, mnPkUserId);
                callableStatement.registerOutParameter(param++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(param++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsAccessCompanyBranchEntities.size(); i++) {
                    mvDbmsAccessCompanyBranchEntities.get(i).setPkUserId(mnPkUserId);
                    if (mvDbmsAccessCompanyBranchEntities.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell the access permission to branch entities universal:

                param = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_access_cob_ent_univ_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(param++, mnPkUserId);
                callableStatement.registerOutParameter(param++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(param++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsAccessCompanyBranchEntitiesUniversal.size(); i++) {
                    mvDbmsAccessCompanyBranchEntitiesUniversal.get(i).setPkUserId(mnPkUserId);
                    if (mvDbmsAccessCompanyBranchEntitiesUniversal.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell the privileges at user level:

                param = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_prv_usr_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(param++, mnPkUserId);
                callableStatement.registerOutParameter(param++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(param++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsUserPrivilegesUser.size(); i++) {
                    mvDbmsUserPrivilegesUser.get(i).setPkUserId(mnPkUserId);
                    if (mvDbmsUserPrivilegesUser.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell the privileges at company level:

                param = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_prv_co_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(param++, mnPkUserId);
                callableStatement.registerOutParameter(param++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(param++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsUserPrivilegesCompany.size(); i++) {
                    mvDbmsUserPrivilegesCompany.get(i).setPkUserId(mnPkUserId);
                    if (mvDbmsUserPrivilegesCompany.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell the roles at user level:

                param = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_rol_usr_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(param++, mnPkUserId);
                callableStatement.registerOutParameter(param++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(param++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsUserRolesUser.size(); i++) {
                    mvDbmsUserRolesUser.get(i).setPkUserId(mnPkUserId);
                    if (mvDbmsUserRolesUser.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell the roles at company level:

                param = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_rol_co_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(param++, mnPkUserId);
                callableStatement.registerOutParameter(param++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(param++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsUserRolesCompany.size(); i++) {
                    mvDbmsUserRolesCompany.get(i).setPkUserId(mnPkUserId);
                    if (mvDbmsUserRolesCompany.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell user transaction configuration:

                if (moDbmsUserConfigurationTransaction == null) {
                    moDbmsUserConfigurationTransaction = new SDataUserConfigurationTransaction();
                    moDbmsUserConfigurationTransaction.setPkUserId(mnPkUserId);
                    moDbmsUserConfigurationTransaction.setIsPurchasesItemAllApplying(true);
                    moDbmsUserConfigurationTransaction.setPurchasesContractLimit_n(-1);
                    moDbmsUserConfigurationTransaction.setPurchasesOrderLimit_n(-1);
                    moDbmsUserConfigurationTransaction.setPurchasesOrderLimitMonthly_n(-1);
                    moDbmsUserConfigurationTransaction.setPurchasesDocLimit_n(-1);
                    moDbmsUserConfigurationTransaction.setIsSalesItemAllApplying(true);
                    moDbmsUserConfigurationTransaction.setSalesContractLimit_n(-1);
                    moDbmsUserConfigurationTransaction.setSalesOrderLimit_n(-1);
                    moDbmsUserConfigurationTransaction.setSalesOrderLimitMonthly_n(-1);
                    moDbmsUserConfigurationTransaction.setSalesDocLimit_n(-1);
                    moDbmsUserConfigurationTransaction.setCapacityVolumeMinPercentage(0);
                    moDbmsUserConfigurationTransaction.setCapacityMassMinPercentage(0);
                    moDbmsUserConfigurationTransaction.setFkUserNewId(mnFkUserNewId);
                    moDbmsUserConfigurationTransaction.setFkUserEditId(mnFkUserNewId);
                }
                else {
                    moDbmsUserConfigurationTransaction.setPkUserId(mnPkUserId);
                    moDbmsUserConfigurationTransaction.setFkUserNewId(mnFkUserEditId);
                    moDbmsUserConfigurationTransaction.setFkUserEditId(mnFkUserNewId);
                }

                moDbmsUserConfigurationTransaction.setIsDeleted(mbIsDeleted);

                if (moDbmsUserConfigurationTransaction.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
                
                // Create transactions user configuration for all existing companies:

                if (mbIsRegistryNew) {
                    statement = connection.createStatement();
                    statementAux = statement.getConnection().createStatement();

                    sql = "SELECT bd FROM erp.cfgu_co WHERE b_del = 0 AND id_co <> " + mnAuxCompanyId;
                    resultSet = statement.executeQuery(sql);

                    while (resultSet.next()) {
                        sql = "INSERT INTO " + resultSet.getString("bd") + ".trn_usr_cfg VALUES (" + 
                                mnPkUserId + ", " +
                                "TRUE, 0, 0, 0, 0, " +
                                "TRUE, 0, 0, 0, 0, " +
                                "0, 0, FALSE, " + 
                                mnFkUserNewId + ", 1, 1, NOW(), NOW(), NOW());";
                        statementAux.execute(sql);
                    }
                }
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }

    /*
     * Other class specific methods:
     */
    
    /**
     * Check if privilege is restricted.
     * @param privilege Constants defined in SDataConstantsSys.PRV_...
     * @return <code>true</code> if privilege is restricted, otherwise <code>false</code>.
     */
    private boolean isPrivilegeRestricted(final int privilege) {
        return SLibUtilities.belongsTo(privilege, new int[] { SDataConstantsSys.PRV_CFG_ERP, SDataConstantsSys.PRV_CFG_CO, SDataConstantsSys.PRV_CAT_USR });
    }
    
    /**
     * Check if privilege is granted by special role.
     * @param privilege Constants defined in SDataConstantsSys.PRV_...
     * @return <code>true</code> if privilege is granted by special role, otherwise <code>false</code>.
     */
    private boolean isPrivilegeGrantedBySpecialRole(final int privilege) {
        boolean granted = false;
        boolean isPrivilegeRestricted = isPrivilegeRestricted(privilege); // convenience variable
        
        if (isSupervisor()) {
            granted = true;
        }
        else if (isConfigurator() && isPrivilegeRestricted) {
            granted = true;
        }
        else if (isAdministrator() && !isPrivilegeRestricted) {
            granted = true;
        }
        
        return granted;
    }

    private void putPrivilegeHashMap(final int privilege, final int level, final HashMap<Integer, Integer> map) {
        map.put(privilege, level);
    }

    private void putPrivilegeHashMap(final int company, final int privilege, final int level, final HashMap<int[], Integer> map) {
        if (map.get(new int[] { company, privilege }) == null) {
            map.put(new int[] { company, privilege }, level);
        }
        else if (map.get(new int[] { company, privilege }) < level) {
            map.put(new int[] { company, privilege }, level);
        }
    }

    private void prepareUserRights(final Statement statement) throws Exception {
        // Prepare user privilegies from user roles:

        for (SDataUserRoleUser roleUser : mvDbmsUserRolesUser) {
            SDataRole role = new SDataRole();
            if (role.read(new int[] { roleUser.getPkRoleId() }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
            else {
                for (SDataPrivilege privilege : role.mvDbmsPrivileges) {
                    putPrivilegeHashMap(privilege.getPkPrivilegeId(), roleUser.getFkLevelTypeId(), moRightsUser);
                }
            }
        }

        for (SDataUserRoleCompany roleCompany : mvDbmsUserRolesCompany) {
            SDataRole role = new SDataRole();
            if (role.read(new int[] { roleCompany.getPkRoleId() }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
            else {
                for (SDataPrivilege privilege : role.mvDbmsPrivileges) {
                    putPrivilegeHashMap(roleCompany.getPkCompanyId(), privilege.getPkPrivilegeId(), roleCompany.getFkLevelTypeId(), moRightsCompany);
                }
            }
        }

        // Prepare user privilegies:

        for (SDataUserPrivilegeUser privilege : mvDbmsUserPrivilegesUser) {
            putPrivilegeHashMap(privilege.getPkPrivilegeId(), privilege.getFkLevelTypeId(), moRightsUser);
        }

        for (SDataUserPrivilegeCompany privilege : mvDbmsUserPrivilegesCompany) {
            putPrivilegeHashMap(privilege.getPkCompanyId(), privilege.getPkPrivilegeId(), privilege.getFkLevelTypeId(), moRightsCompany);
        }
    }

    /*
     * Class public methods:
     */

    /**
     * Check if user has access to module.
     * @param moduleId
     * @param companyId
     * @return 
     */
    public boolean hasAccessToModule(final int moduleId, final int companyId) {
        boolean access = false;

        if (isSupervisor()) {
            access = true;
        }

        if (!access) {
            if (moduleId == SDataConstants.MOD_CFG) {
                access = isConfigurator();
            }
            else {
                access = isAdministrator();
            }
        }

        if (!access) {
            for (SDataUserRoleUser role: mvDbmsUserRolesUser) {
                if (role.getDbmsFkTypeRoleId() == moduleId) {
                    access = true;
                    break;
                }
            }
        }

        if (!access) {
            for (SDataUserRoleCompany role: mvDbmsUserRolesCompany) {
                if (role.getDbmsFkTypeRoleId() == moduleId && role.getPkCompanyId() == companyId) {
                    access = true;
                    break;
                }
            }
        }

        if (!access) {
            for (SDataUserPrivilegeUser priv : mvDbmsUserPrivilegesUser) {
                if (priv.getDbmsFkTypePrivilegeId() == moduleId) {
                    access = true;
                    break;
                }
            }
        }

        if (!access) {
            for (SDataUserPrivilegeCompany priv : mvDbmsUserPrivilegesCompany) {
                if (priv.getDbmsFkTypePrivilegeId() == moduleId && priv.getPkCompanyId() == companyId) {
                    access = true;
                    break;
                }
            }
        }

        if (!access) {
            if (moduleId == SDataConstants.MOD_CFG) {
                for (Integer right : moRightsUser.keySet()) {
                    if (SDataUtilities.isCatalogueCfg(right) || SDataUtilities.isCatalogueUsr(right) || SDataUtilities.isCatalogueLoc(right) ||
                        SDataUtilities.isCatalogueBps(right) || SDataUtilities.isCatalogueItm(right) || SDataUtilities.isCatalogueFin(right) ||
                        SDataUtilities.isCatalogueTrn(right)) {
                        access = true;
                        break;
                    }
                }

                if (!access) {
                    for (int[] right : moRightsCompany.keySet()) {
                        if ((right[0] == companyId) && (
                            SDataUtilities.isCatalogueCfg(right[1]) || SDataUtilities.isCatalogueUsr(right[1]) || SDataUtilities.isCatalogueLoc(right[1]) ||
                            SDataUtilities.isCatalogueBps(right[1]) || SDataUtilities.isCatalogueItm(right[1]) || SDataUtilities.isCatalogueFin(right[1]) ||
                            SDataUtilities.isCatalogueTrn(right[1]))) {
                            access = true;
                            break;
                        }
                    }
                }
            }
            else {
                for (Integer right : moRightsUser.keySet()) {
                    if ((right / 1000 * 1000) == moduleId) {
                        access = true;
                        break;
                    }
                }

                if (!access) {
                    for (int[] right : moRightsCompany.keySet()) {
                        if (right[0] == companyId && (right[1] / 1000 * 1000) == moduleId) {
                            access = true;
                            break;
                        }
                    }
                }
            }
        }

        return access;
    }

    /**
     * Look for a specific privilege for the user of current session.
     *
     * @param client ERP Client interface.
     * @param privilege The privilege to look.
     * @return A instance of <code>erp.musr.data.SDataUser.Right</code>.
     */
    public erp.musr.data.SDataUser.Right hasRight(final SClientInterface client, final int privilege) {
        SDataUser.Right right = new SDataUser.Right(privilege, false, SLibConstants.UNDEFINED);
        
        if (isPrivilegeGrantedBySpecialRole(privilege)) {
            right.HasRight = true;
            right.Level = SUtilConsts.LEV_MANAGER;
        }

        if (!right.HasRight) {
            Integer level = null;
            
            // check if privilege is assigned at company scope (higher precedence):
            
            Object[] companyPrivileges = moRightsCompany.keySet().toArray();
            
            for (SDataAccessCompany access : mvDbmsAccessCompanies) {
                if (access.getPkCompanyId() == client.getSessionXXX().getCurrentCompany().getPkCompanyId()) {
                    int[] companyPrivilege = new int[] { access.getPkCompanyId(), privilege };
                    
                    for (int i = 0; i < moRightsCompany.size(); i++) {
                        if (SLibUtilities.compareKeys(companyPrivilege, (int[]) companyPrivileges[i])) {
                            Integer levelAux = (Integer) moRightsCompany.values().toArray()[i];
                            if (level == null || levelAux > level) {
                                level = levelAux;   // do not break when found for searching the highest level of privilege assigned to user at company scope
                            }
                        }
                    }
                    
                    if (level != null) {
                        right.HasRight = level > SLibConstants.UNDEFINED;
                        right.Level = level;
                        break;
                    }
                }
            }

            // check if privilege is assigned at user scope (lesser precedence):
            
            if (level == null) {
                level = moRightsUser.get(privilege);
                
                if (level != null) {
                    right.HasRight = level > SLibConstants.UNDEFINED;
                    right.Level = level;
                }
            }
        }

        return right;
    }

    public void readUserPrivileges(SGuiSession session) {
        String sql = "";
        ResultSet resultSet = null;
        ResultSet resultSetAux = null;
        Statement statementAux;

        try {
            // Read roles by user:

            sql = "SELECT id_rol, fid_tp_lev "
                    + "FROM erp.usru_rol_usr "
                    + "WHERE id_usr = " + session.getUser().getPkUserId() + " "
                    + "ORDER BY id_rol;";

            statementAux = session.getStatement().getConnection().createStatement();

            resultSet = session.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                // Read privileges by role by user:

                sql = "SELECT id_prv "
                        + "FROM erp.usrs_rol_prv "
                        + "WHERE id_rol = " + resultSet.getInt("id_rol") + ";";

                resultSetAux = statementAux.executeQuery(sql);

                while (resultSetAux.next()) {
                    moPrivilegeUser.put(resultSetAux.getInt("id_prv"), resultSet.getInt("fid_tp_lev"));
                }
            }
            
            // Read privileges by user:

            sql = "SELECT id_prv, fid_tp_lev "
                    + "FROM erp.usru_prv_usr "
                    + "WHERE id_usr = " + session.getUser().getPkUserId() + " "
                    + "ORDER BY id_prv;";

            resultSet = session.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                moPrivilegeUser.put(resultSet.getInt("id_prv"), resultSet.getInt("fid_tp_lev"));
            }
            
            // Read roles by company:

            sql = "SELECT id_rol, fid_tp_lev "
                    + "FROM erp.usru_rol_co "
                    + "WHERE id_usr = " + session.getUser().getPkUserId() + " AND id_co = " + session.getConfigCompany().getCompanyId() + " "
                    + "ORDER BY id_rol;";

            resultSet = session.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                // Read privileges by role by company:

                sql = "SELECT id_prv "
                        + "FROM erp.usrs_rol_prv "
                        + "WHERE id_rol = " + resultSet.getInt("id_rol") + ""
                        + "ORDER BY id_prv;";

                resultSetAux = statementAux.executeQuery(sql);

                while (resultSetAux.next()) {
                    moPrivilegeUser.put(resultSetAux.getInt("id_prv"), resultSet.getInt("fid_tp_lev"));
                }
            }
            
            // Read privileges by company:

            sql = "SELECT id_prv, fid_tp_lev "
                    + "FROM erp.usru_prv_co "
                    + "WHERE id_usr = " + session.getUser().getPkUserId() + " AND id_co = " + session.getConfigCompany().getCompanyId() + " "
                    + "ORDER BY id_prv;";

            resultSet = session.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                moPrivilegeUser.put(resultSet.getInt("id_prv"), resultSet.getInt("fid_tp_lev"));
            }
            
            // Establish access to modules from type of user's roles and privileges:

            sql = "SELECT DISTINCT(tp_prv) AS tp_prv FROM (";

            sql += "SELECT DISTINCT(r_u.fid_tp_rol) AS tp_prv "
                    + "FROM erp.usru_rol_usr AS rol_u "
                    + "INNER JOIN erp.usrs_rol AS r_u ON rol_u.id_rol = r_u.id_rol "
                    + "WHERE rol_u.id_usr = " + session.getUser().getPkUserId() + " ";
            sql += "UNION ";
            sql += "SELECT DISTINCT(p_u.fid_tp_prv) AS tp_prv "
                    + "FROM erp.usru_prv_usr AS prv_u "
                    + "INNER JOIN erp.usrs_prv AS p_u ON prv_u.id_prv = p_u.id_prv "
                    + "WHERE prv_u.id_usr = " + session.getUser().getPkUserId() + " ";
            sql += "UNION ";
            sql += "SELECT DISTINCT(r.fid_tp_rol) AS tp_prv "
                    + "FROM erp.usru_rol_co AS rol "
                    + "INNER JOIN erp.usrs_rol AS r ON rol.id_rol = r.id_rol "
                    + "WHERE rol.id_usr = " + session.getUser().getPkUserId() + " AND rol.id_co = " + session.getConfigCompany().getCompanyId() + " ";
            sql += "UNION ";
            sql += "SELECT DISTINCT(p.fid_tp_prv) AS tp_prv "
                    + "FROM erp.usru_prv_co AS prv "
                    + "INNER JOIN erp.usrs_prv AS p ON prv.id_prv = p.id_prv "
                    + "WHERE prv.id_usr = " + session.getUser().getPkUserId() + " AND prv.id_co = " + session.getConfigCompany().getCompanyId() + ") AS t ";

            resultSet = session.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                switch (resultSet.getInt("tp_prv")) {
                    case SModConsts.MOD_CFG:
                    case SModConsts.GLOBAL_CAT_CFG:
                        moModuleAccess.add(SModConsts.MOD_CFG_N);
                        break;
                    case SModConsts.MOD_FIN:
                    case SModConsts.GLOBAL_CAT_FIN:
                        moModuleAccess.add(SModConsts.MOD_FIN_N);
                        break;
                    case SModConsts.MOD_PUR:
                    case SModConsts.MOD_SAL:
                    case SModConsts.MOD_INV:
                    case SModConsts.GLOBAL_CAT_TRN:
                        moModuleAccess.add(SModConsts.MOD_TRN_N);
                        break;
                    case SModConsts.MOD_MKT:
                    case SModConsts.GLOBAL_CAT_MKT:
                        moModuleAccess.add(SModConsts.MOD_MKT_N);
                        break;
                    case SModConsts.MOD_LOG:
                    case SModConsts.GLOBAL_CAT_LOG:
                        moModuleAccess.add(SModConsts.MOD_LOG_N);
                        break;
                    case SModConsts.MOD_MFG:
                    case SModConsts.GLOBAL_CAT_MFG:
                        moModuleAccess.add(SModConsts.MOD_MFG_N);
                        break;
                    case SModConsts.MOD_HRS:
                    case SModConsts.GLOBAL_CAT_HRS:
                        moModuleAccess.add(SModConsts.MOD_HRS_N);
                        break;
                    default:
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }
    }

    /*
     * sa.lib.gui.SGuiUser overriden methods
     */

    @Override
    public String getName() {
        return msUser;
    }

    @Override
    public boolean isAdministrator() {
        return isSupervisor() || mnFkUserTypeId == SModSysConsts.USRS_TP_USR_ADM || mbExtraIsSpecialRoleAdmor;
    }

    public boolean isConfigurator() {
        return isSupervisor() || mbExtraIsSpecialRoleConfig;
    }

    @Override
    public boolean isSupervisor() {
        return mnFkUserTypeId == SModSysConsts.USRS_TP_USR_SUP || mnPkUserId == SDataConstantsSys.USRX_USER_SUPER || mbExtraIsSpecialRoleSuper;
    }

    @Override
    public boolean hasModuleAccess(final int module) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasPrivilege(final int privilege) {
        return hasPrivilege(new int[] { privilege });
    }

    @Override
    public boolean hasPrivilege(final int[] privileges) {
        boolean has = false;

        for (Integer privilege : privileges) {
            has = getPrivilegeLevel(privilege) != SUtilConsts.LEV_NONE;
            break;
        }

        return has;
    }

    @Override
    public int getPrivilegeLevel(final int privilege) {
        if (isPrivilegeGrantedBySpecialRole(privilege)) {
            return SUtilConsts.LEV_MANAGER;
        }
        else {
            Integer level = moPrivilegeUser.get(privilege);

            return level == null ? SUtilConsts.LEV_NONE : level;
        }
    }

    @Override
    public HashMap<Integer, Integer> getPrivilegesMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HashSet<Integer> getModulesSet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void computeAccess(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiSessionCustom createSessionCustom(SGuiClient client) {
        return createSessionCustom(client, 0);
    }

    @Override
    public SGuiSessionCustom createSessionCustom(SGuiClient client, int terminal) {
        SSessionCustom sessionCustom = new SSessionCustom(client.getSession());

        sessionCustom.updateSessionSettings();

        return sessionCustom;
    }

    @Override
    public boolean showUserSessionConfigOnLogin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public class Right {
        public int Privilege;
        public boolean HasRight;
        public int Level;

        public Right() {
            Privilege = SDataConstantsSys.UNDEFINED;
            HasRight = false;
            Level = SUtilConsts.LEV_READ;
        }

        public Right(int privilege, boolean hasRight, int level) {
            Privilege = privilege;
            HasRight = hasRight;
            Level = level;
        }
    }
}
