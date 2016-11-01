/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

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
 * @author Sergio Flores, Alfonso Flores
 */
public class SDataUser extends SDataRegistry implements Serializable, SGuiUser {

    protected int mnPkUserId;
    protected java.lang.String msUser;
    protected java.lang.String msUserPassword;
    protected boolean mbIsUniversal;
    protected boolean mbIsCanEdit;
    protected boolean mbIsCanDelete;
    protected boolean mbIsActive;
    protected boolean mbIsDeleted;
    protected int mnFkUserTypeId;
    protected int mnFkBizPartnerId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.util.Vector<SDataTypeModule> mvDbmsTypeModules;
    protected java.util.Vector<SDataUserPrivilegeUser> mvDbmsUserPrivilegesUser;
    protected java.util.Vector<SDataUserPrivilegeCompany> mvDbmsUserPrivilegesCompany;
    protected java.util.Vector<SDataUserRoleUser> mvDbmsUserRolesUser;
    protected java.util.Vector<SDataUserRoleCompany> mvDbmsUserRolesCompany;
    protected java.util.Vector<SDataAccessCompany> mvDbmsAccessCompanies;
    protected java.util.Vector<SDataAccessCompanyBranch> mvDbmsAccessCompanyBranches;
    protected java.util.Vector<SDataAccessCompanyBranchEntity> mvDbmsAccessCompanyBranchEntities;
    protected java.util.Vector<SDataAccessCompanyBranchEntityUniversal> mvDbmsAccessCompanyBranchEntitiesUniversal;

    protected erp.mtrn.data.SDataUserConfigurationTransaction moDbmsUserConfigurationTransaction;

    protected boolean mbExtraIsPasswordUpdateRequired;
    protected boolean mbExtraIsSpecialSuperUser;
    protected boolean mbExtraIsSpecialConfigurator;
    protected boolean mbExtraIsSpecialAdministrator;

    private java.util.HashSet<Integer> moModuleAccess;
    private java.util.HashMap<Integer, Integer> moPrivilegeUser;
    private java.util.HashMap<Integer, Integer> moRightsUser;
    private java.util.HashMap<int[], Integer> moRightsCompany;

    protected int mnAuxCompanyId;

    public SDataUser() {
        super(SDataConstants.USRU_USR);

        mvDbmsTypeModules = new Vector<SDataTypeModule>();
        mvDbmsUserPrivilegesUser = new Vector<SDataUserPrivilegeUser>();
        mvDbmsUserPrivilegesCompany = new Vector<SDataUserPrivilegeCompany>();
        mvDbmsUserRolesUser = new Vector<SDataUserRoleUser>();
        mvDbmsUserRolesCompany = new Vector<SDataUserRoleCompany>();
        mvDbmsAccessCompanies = new Vector<SDataAccessCompany>();
        mvDbmsAccessCompanyBranches = new Vector<SDataAccessCompanyBranch>();
        mvDbmsAccessCompanyBranchEntities = new Vector<SDataAccessCompanyBranchEntity>();
        mvDbmsAccessCompanyBranchEntitiesUniversal = new Vector<SDataAccessCompanyBranchEntityUniversal>();
        moModuleAccess = new HashSet<Integer>();
        moPrivilegeUser = new HashMap<Integer, Integer>();
        moRightsUser = new HashMap<Integer, Integer>();
        moRightsCompany = new HashMap<int[], Integer>();

        reset();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setUser(java.lang.String s) { msUser = s; }
    public void setUserPassword(java.lang.String s) { msUserPassword = s; }
    public void setIsUniversal(boolean b) { mbIsUniversal = b; }
    public void setIsCanEdit(boolean b) { mbIsCanEdit = b; }
    public void setIsCanDelete(boolean b) { mbIsCanDelete = b; }
    public void setIsActive(boolean b) { mbIsActive = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserTypeId(int n) { mnFkUserTypeId = n; }
    public void setFkBizPartnerId_n(int n) { mnFkBizPartnerId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }
    public void setAuxCompanyId(int n) { mnAuxCompanyId = n; }

    public int getPkUserId() { return mnPkUserId; }
    public java.lang.String getUser() { return msUser; }
    public java.lang.String getUserPassword() { return msUserPassword; }
    public boolean getIsUniversal() { return mbIsUniversal; }
    public boolean getIsCanEdit() { return mbIsCanEdit; }
    public boolean getIsCanDelete() { return mbIsCanDelete; }
    public boolean getIsActive() { return mbIsActive; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserTypeId() { return mnFkUserTypeId; }
    public int getFkBizPartnerId_n() { return mnFkBizPartnerId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
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

    public void setExtraIsPasswordUpdateRequired(boolean b) { mbExtraIsPasswordUpdateRequired = b; }

    public boolean getExtraIsPasswordUpdateRequired() { return mbExtraIsPasswordUpdateRequired; }
    public boolean getExtraIsSpecialSuperUser() { return mbExtraIsSpecialSuperUser; }
    public boolean getExtraIsSpecialConfigurator() { return mbExtraIsSpecialConfigurator; }
    public boolean getExtraIsSpecialAdministrator() { return mbExtraIsSpecialAdministrator; }

    public int getAuxCompanyId() { return mnAuxCompanyId; }

    public erp.mtrn.data.SDataUserConfigurationTransaction getDbmsUserConfigurationTransaction() { return moDbmsUserConfigurationTransaction; }

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
        mbIsUniversal = false;
        mbIsCanEdit = false;
        mbIsCanDelete = false;
        mbIsActive = false;
        mbIsDeleted = false;
        mnFkUserTypeId = 0;
        mnFkBizPartnerId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
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

        moModuleAccess.clear();
        moPrivilegeUser.clear();
        moRightsUser.clear();
        moRightsCompany.clear();

        mbExtraIsPasswordUpdateRequired = false;
        mbExtraIsSpecialSuperUser = false;
        mbExtraIsSpecialConfigurator = false;
        mbExtraIsSpecialAdministrator = false;

        mnAuxCompanyId = 0;
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
                mbIsUniversal = resultSet.getBoolean("b_univ");
                mbIsCanEdit = resultSet.getBoolean("b_can_edit");
                mbIsCanDelete = resultSet.getBoolean("b_can_del");
                mbIsActive = resultSet.getBoolean("b_act");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserTypeId = resultSet.getInt("fid_tp_usr");
                mnFkBizPartnerId_n = resultSet.getInt("fid_bp_n");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                statementAux = statement.getConnection().createStatement();

                // Read aswell the access permission to companies:

                sql = "SELECT id_usr, id_co FROM erp.usru_access_co WHERE id_usr = " + mnPkUserId + " ORDER BY id_co ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataAccessCompany company = new SDataAccessCompany();
                    if (company.read(new int[] { mnPkUserId, resultSet.getInt("id_co") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsAccessCompanies.add(company);
                    }
                }

                // Read aswell the access permission to company branches:

                sql = "SELECT id_usr, id_cob FROM erp.usru_access_cob WHERE id_usr = " + mnPkUserId + " ORDER BY id_cob ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataAccessCompanyBranch branch = new SDataAccessCompanyBranch();
                    if (branch.read(new int[] { mnPkUserId, resultSet.getInt("id_cob") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsAccessCompanyBranches.add(branch);
                    }
                }

                // Read aswell the access permission to branch entities:

                sql = "SELECT id_usr, id_cob, id_ent FROM erp.usru_access_cob_ent WHERE id_usr = " + mnPkUserId + " ORDER BY id_cob, id_ent ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataAccessCompanyBranchEntity entity = new SDataAccessCompanyBranchEntity();
                    if (entity.read(new int[] { mnPkUserId, resultSet.getInt("id_cob"), resultSet.getInt("id_ent") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsAccessCompanyBranchEntities.add(entity);
                    }
                }

                // Read aswell the access permission to branch entities universal:

                sql = "SELECT id_usr, id_cob, id_ct_ent FROM erp.usru_access_cob_ent_univ WHERE id_usr = " + mnPkUserId + " ORDER BY id_cob, id_ct_ent ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataAccessCompanyBranchEntityUniversal entityUniversal = new SDataAccessCompanyBranchEntityUniversal();
                    if (entityUniversal.read(new int[] { mnPkUserId, resultSet.getInt("id_cob"), resultSet.getInt("id_ct_ent") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsAccessCompanyBranchEntitiesUniversal.add(entityUniversal);
                    }
                }

                // Read aswell the privileges concerning user:

                sql = "SELECT id_usr, id_prv FROM erp.usru_prv_usr WHERE id_usr = " + mnPkUserId + " ORDER BY id_prv ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataUserPrivilegeUser priv = new SDataUserPrivilegeUser();
                    if (priv.read(new int[] { mnPkUserId, resultSet.getInt("id_prv") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsUserPrivilegesUser.add(priv);
                    }
                }

                // Read aswell the privileges concerning company:

                sql = "SELECT id_usr, id_co, id_prv FROM erp.usru_prv_co WHERE id_usr = " + mnPkUserId + " ORDER BY id_co, id_prv ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataUserPrivilegeCompany priv = new SDataUserPrivilegeCompany();
                    if (priv.read(new int[] { mnPkUserId, resultSet.getInt("id_co"), resultSet.getInt("id_prv") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsUserPrivilegesCompany.add(priv);
                    }
                }

                // Read aswell the roles concerning user:

                sql = "SELECT id_usr, id_rol FROM erp.usru_rol_usr WHERE id_usr = " + mnPkUserId + " ORDER BY id_rol ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataUserRoleUser role = new SDataUserRoleUser();
                    if (role.read(new int[] { mnPkUserId, resultSet.getInt("id_rol") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsUserRolesUser.add(role);
                    }
                }

                // Read aswell the roles concerning company:

                sql = "SELECT id_usr, id_co, id_rol FROM erp.usru_rol_co WHERE id_usr = " + mnPkUserId + " ORDER BY id_co, id_rol ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataUserRoleCompany role = new SDataUserRoleCompany();
                    if (role.read(new int[] { mnPkUserId, resultSet.getInt("id_co"), resultSet.getInt("id_rol") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsUserRolesCompany.add(role);
                    }
                }

                // Read aswell the configuration for transactions:

                sql = "SELECT id_usr FROM trn_usr_cfg WHERE id_usr = " + mnPkUserId + " ORDER BY id_usr ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    moDbmsUserConfigurationTransaction = new SDataUserConfigurationTransaction();
                    if (moDbmsUserConfigurationTransaction.read(new int[] { mnPkUserId }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                }

                // Define special roles:

                if (mnPkUserId == SDataConstantsSys.USRX_USER_SUPER) {
                    mbExtraIsSpecialSuperUser = true;
                }
                else {
                    for (SDataUserRoleUser role : mvDbmsUserRolesUser) {
                        switch (role.getPkRoleId()) {
                            case SDataConstantsSys.ROL_SPE_SUPER:
                                mbExtraIsSpecialSuperUser = true;
                                break;
                            case SDataConstantsSys.ROL_SPE_CONFIG:
                                mbExtraIsSpecialConfigurator = true;
                                break;
                            case SDataConstantsSys.ROL_SPE_ADMOR:
                                mbExtraIsSpecialAdministrator = true;
                                break;
                            default:
                        }
                    }
                }

                // Prepare all user privileges in hash maps:

                prepareUserRights(statement);

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
        int nParam = 1;
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
                    "?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkUserId);
            callableStatement.setString(nParam++, msUser);
            callableStatement.setString(nParam++, msUserPassword);
            callableStatement.setBoolean(nParam++, mbIsUniversal);
            callableStatement.setBoolean(nParam++, mbIsCanEdit);
            callableStatement.setBoolean(nParam++, mbIsCanDelete);
            callableStatement.setBoolean(nParam++, mbIsActive);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setBoolean(nParam++, mbIsRegistryNew ? true : mbExtraIsPasswordUpdateRequired);
            callableStatement.setInt(nParam++, mnFkUserTypeId);
            if (mnFkBizPartnerId_n > 0) callableStatement.setInt(nParam++, mnFkBizPartnerId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnPkUserId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                // Save aswell the access permission to companies:

                nParam = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_access_co_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(nParam++, mnPkUserId);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsAccessCompanies.size(); i++) {
                    mvDbmsAccessCompanies.get(i).setPkUserId(mnPkUserId);
                    if (mvDbmsAccessCompanies.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell the access permission to company branches:

                nParam = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_access_cob_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(nParam++, mnPkUserId);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsAccessCompanyBranches.size(); i++) {
                    mvDbmsAccessCompanyBranches.get(i).setPkUserId(mnPkUserId);
                    if (mvDbmsAccessCompanyBranches.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell the access permission to branch entities:

                nParam = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_access_cob_ent_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(nParam++, mnPkUserId);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsAccessCompanyBranchEntities.size(); i++) {
                    mvDbmsAccessCompanyBranchEntities.get(i).setPkUserId(mnPkUserId);
                    if (mvDbmsAccessCompanyBranchEntities.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell the access permission to branch entities universal:

                nParam = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_access_cob_ent_univ_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(nParam++, mnPkUserId);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsAccessCompanyBranchEntitiesUniversal.size(); i++) {
                    mvDbmsAccessCompanyBranchEntitiesUniversal.get(i).setPkUserId(mnPkUserId);
                    if (mvDbmsAccessCompanyBranchEntitiesUniversal.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell the privileges at user level:

                nParam = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_prv_usr_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(nParam++, mnPkUserId);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsUserPrivilegesUser.size(); i++) {
                    mvDbmsUserPrivilegesUser.get(i).setPkUserId(mnPkUserId);
                    if (mvDbmsUserPrivilegesUser.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell the privileges at company level:

                nParam = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_prv_co_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(nParam++, mnPkUserId);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsUserPrivilegesCompany.size(); i++) {
                    mvDbmsUserPrivilegesCompany.get(i).setPkUserId(mnPkUserId);
                    if (mvDbmsUserPrivilegesCompany.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell the roles at user level:

                nParam = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_rol_usr_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(nParam++, mnPkUserId);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsUserRolesUser.size(); i++) {
                    mvDbmsUserRolesUser.get(i).setPkUserId(mnPkUserId);
                    if (mvDbmsUserRolesUser.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell the roles at company level:

                nParam = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.usru_rol_co_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(nParam++, mnPkUserId);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
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
                    moDbmsUserConfigurationTransaction.setPurchasesOrderLimit_n(-1);
                    moDbmsUserConfigurationTransaction.setPurchasesOrderLimitMonthly_n(-1);
                    moDbmsUserConfigurationTransaction.setPurchasesDocLimit_n(-1);
                    moDbmsUserConfigurationTransaction.setIsSalesItemAllApplying(true);
                    moDbmsUserConfigurationTransaction.setSalesOrderLimit_n(-1);
                    moDbmsUserConfigurationTransaction.setSalesOrderLimitMonthly_n(-1);
                    moDbmsUserConfigurationTransaction.setSalesDocLimit_n(-1);
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
                        sql = "INSERT INTO " + resultSet.getString("bd") + ".trn_usr_cfg VALUES (" + mnPkUserId +
                              ", true, 0, 0, true, 0, 0, 0, 0, false, " + mnFkUserNewId + ", 1, 1, NOW(), NOW(), NOW())";
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

    private void putPrivilegeHashMap(int privilege, int level, HashMap<Integer, Integer> map) {
        map.put(privilege, level);
    }

    private void putPrivilegeHashMap(int company, int privilege, int level, HashMap<int[], Integer> map) {
        if (map.get(new int[] { company, privilege }) == null) {
            map.put(new int[] { company, privilege }, level);
        }
        else if (map.get(new int[] { company, privilege }) < level) {
            map.put(new int[] { company, privilege }, level);
        }
    }

    private void prepareUserRights(java.sql.Statement statement) throws java.lang.Exception {
        SDataRole oRole = new SDataRole();

        // Prepare user privilegies in user roles:

        for (SDataUserRoleUser role : mvDbmsUserRolesUser) {
            oRole.reset();
            if (oRole.read(new int[] { role.getPkRoleId() }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
            else {
                for (SDataPrivilege privilege : oRole.mvDbmsPrivileges) {
                    //System.out.println("Preparing user privilege: " + privilege.getPkPrivilegeId() + ", of rol: " + role.getPkRoleId() + ".");
                    putPrivilegeHashMap(privilege.getPkPrivilegeId(), role.getFkLevelTypeId(), moRightsUser);
                }
            }
        }

        for (SDataUserRoleCompany role : mvDbmsUserRolesCompany) {
            oRole.reset();
            if (oRole.read(new int[] { role.getPkRoleId() }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
            else {
                for (SDataPrivilege privilege : oRole.mvDbmsPrivileges) {
                    //System.out.println("Preparing company privilege: " + privilege.getPkPrivilegeId() + ", of rol: " + role.getPkRoleId() + ".");
                    putPrivilegeHashMap(role.getPkCompanyId(), privilege.getPkPrivilegeId(), role.getFkLevelTypeId(), moRightsCompany);
                }
            }
        }

        // Prepare user privilegies:

        for (SDataUserPrivilegeUser privilege : mvDbmsUserPrivilegesUser) {
            //System.out.println("Preparing user privilege: " + privilege.getPkPrivilegeId() + ".");
            putPrivilegeHashMap(privilege.getPkPrivilegeId(), privilege.getFkLevelTypeId(), moRightsUser);
        }

        for (SDataUserPrivilegeCompany privilege : mvDbmsUserPrivilegesCompany) {
            //System.out.println("Preparing company privilege: " + privilege.getPkPrivilegeId() + ".");
            putPrivilegeHashMap(privilege.getPkCompanyId(), privilege.getPkPrivilegeId(), privilege.getFkLevelTypeId(), moRightsCompany);
        }
    }

    /*
     * Class public methods:
     */

    public boolean hasAccessToModule(int moduleId, int companyId) {
        boolean access = false;

        if (mbExtraIsSpecialSuperUser) {
            access = true;
        }

        if (!access) {
            if (moduleId == SDataConstants.MOD_CFG) {
                access = mbExtraIsSpecialConfigurator;
            }
            else {
                access = mbExtraIsSpecialAdministrator;
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

    public boolean hasSpecialRole(int role) {
        boolean hasRole = false;

        switch (role) {
            case SDataConstantsSys.ROL_SPE_SUPER:
                hasRole = mbExtraIsSpecialSuperUser;
                break;
            case SDataConstantsSys.ROL_SPE_CONFIG:
                hasRole = mbExtraIsSpecialSuperUser || mbExtraIsSpecialConfigurator;
                break;
            case SDataConstantsSys.ROL_SPE_ADMOR:
                hasRole = mbExtraIsSpecialSuperUser || mbExtraIsSpecialAdministrator;
                break;
            default:
        }

        return hasRole;
    }

    /*
    public boolean hasUserPrivilege(int privilegeId, int companyId, int companyBranchId, java.util.Vector<erp.musr.data.SDataRole> roles) {
        boolean access = false;

        if (mbExtraIsSpecialSuperUser) {
            access = true;
        }
        else {
            if (privilegeId == SDataConstantsSys.PRV_CFG_CO ||
                    privilegeId == SDataConstantsSys.PRV_CFG_ERP ||
                    privilegeId == SDataConstantsSys.PRV_CAT_CFG_LAN ||
                    privilegeId == SDataConstantsSys.PRV_CAT_CFG_CUR ||
                    privilegeId == SDataConstantsSys.PRV_CAT_CFG_CO ||
                    privilegeId == SDataConstantsSys.PRV_CAT_USR) {
                access = mbExtraIsSpecialConfigurator;
            }
            else {
                access = mbExtraIsSpecialAdministrator;

                if (!access) {
                    for (SDataUserPrivilegeUser priv : mvDbmsUserPrivilegesUser) {
                        if (privilegeId == priv.getPkPrivilegeId()) {
                            access = true;
                        }
                    }
                }

                if (!access) {
                    if (companyId != 0) {
                        for (SDataUserPrivilegeCompany priv : mvDbmsUserPrivilegesCompany) {
                            if (privilegeId == priv.getPkPrivilegeId() && companyId == priv.getPkCompanyId()) {
                                access = true;
                            }
                        }
                    }
                }

                if (!access) {
                    if (companyBranchId != 0) {
                        for (SDataUserPrivilegeCompanyBranch priv : mvDbmsUserPrivilegesCompanyBranch) {
                            if (privilegeId == priv.getPkPrivilegeId() && companyBranchId == priv.getPkCompanyBranchId()) {
                                access = true;
                            }
                        }
                    }
                }

                if (!access) {
                    int roleId = SLibConstants.UNDEFINED;

                    for (SDataRole role : roles) {
                        if (roleId != SLibConstants.UNDEFINED) {
                            break;
                        }
                        else {
                            for (SDataPrivilege priv : role.getDbmsPrivileges()) {
                                if (privilegeId == priv.getPkPrivilegeId()) {
                                    roleId = role.getPkRoleId();
                                    break;
                                }
                            }
                        }
                    }

                    if (roleId != SLibConstants.UNDEFINED) {
                        access = hasUserRole(roleId, companyId, companyBranchId);
                    }
                }
            }
        }

        return access;
    }
    */

    /**
     * This funtion looks for a specific privilege for the user logged and
     * determines if it has right.
     *
     * @param client ERP Client interface.
     * @param privilege The privilege to look.
     */
    public erp.musr.data.SDataUser.Right hasRight(erp.client.SClientInterface client, int privilege) {
        Integer level = null;
        SDataUser.Right right = new SDataUser.Right(privilege, false, SUtilConsts.LEV_READ);

        if (hasSpecialRole(SDataConstantsSys.ROL_SPE_SUPER)) {
            right.HasRight = true;
            right.Level = SUtilConsts.LEV_MANAGER;
        }
        else if (hasSpecialRole(SDataConstantsSys.ROL_SPE_CONFIG) &&
                SLibUtilities.belongsTo(privilege, new int[] { SDataConstantsSys.PRV_CFG_ERP, SDataConstantsSys.PRV_CFG_CO, SDataConstantsSys.PRV_CAT_USR })) {
            right.HasRight = true;
            right.Level = SUtilConsts.LEV_MANAGER;
        }
        else if (hasSpecialRole(SDataConstantsSys.ROL_SPE_ADMOR) &&
                !SLibUtilities.belongsTo(privilege, new int[] { SDataConstantsSys.PRV_CFG_ERP, SDataConstantsSys.PRV_CFG_CO, SDataConstantsSys.PRV_CAT_USR })) {
            right.HasRight = true;
            right.Level = SUtilConsts.LEV_MANAGER;
        }


        if (!right.HasRight) {
            if (!right.HasRight) {
                for (SDataAccessCompany access : mvDbmsAccessCompanies) {
                    if (access.getPkCompanyId() == client.getSessionXXX().getCurrentCompany().getPkCompanyId()) {
                        for (int i = 0; i < moRightsCompany.size(); i++) {
                            if (SLibUtilities.compareKeys((int[]) moRightsCompany.keySet().toArray()[i], new int[] { access.getPkCompanyId(), privilege })) {
                                level = (Integer) moRightsCompany.values().toArray()[i];
                                break;
                            }
                        }
                        if (level != null) {
                            right.HasRight = true;
                            right.Level = level;
                            break;
                        }
                    }
                }

                if (!right.HasRight) {
                    level = moRightsUser.get(privilege);
                    if (level != null) {
                        right.HasRight = true;
                        right.Level = level;
                    }
                }
            }
        }

        return right;
    }

    public void readPrivilegeUser(SGuiSession session) {
        String sql = "";
        ResultSet resultSet = null;
        ResultSet resultSetAux = null;
        Statement statementAux;

        try {
            // Read role of user:

            sql = "SELECT id_rol, fid_tp_lev "
                    + "FROM erp.usru_rol_usr WHERE id_usr = " + session.getUser().getPkUserId() + " ORDER BY id_rol ";

            statementAux = session.getStatement().getConnection().createStatement();

            resultSet = session.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                // Read privilege by role:

                sql = "SELECT id_prv FROM erp.usrs_rol_prv WHERE id_rol = " + resultSet.getInt("id_rol") + " ";

                resultSetAux = statementAux.executeQuery(sql);

                while (resultSetAux.next()) {
                    moPrivilegeUser.put(resultSetAux.getInt("id_prv"), resultSet.getInt("fid_tp_lev"));
                }
            }
            // Read privilege of user:

            sql = "SELECT id_prv, fid_tp_lev "
                    + "FROM erp.usru_prv_usr WHERE id_usr = " + session.getUser().getPkUserId() + " ORDER BY id_prv ";

            resultSet = session.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                moPrivilegeUser.put(resultSet.getInt("id_prv"), resultSet.getInt("fid_tp_lev"));
            }
            // Read role of user by company:

            sql = "SELECT id_rol, fid_tp_lev "
                    + "FROM erp.usru_rol_co "
                    + "WHERE id_usr = " + session.getUser().getPkUserId() + " AND id_co = " + session.getConfigCompany().getCompanyId() + " "
                    + "ORDER BY id_rol ";

            resultSet = session.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                // Read privilege by role:

                sql = "SELECT id_prv FROM erp.usrs_rol_prv WHERE id_rol = " + resultSet.getInt("id_rol") + " ";

                resultSetAux = statementAux.executeQuery(sql);

                while (resultSetAux.next()) {
                    moPrivilegeUser.put(resultSetAux.getInt("id_prv"), resultSet.getInt("fid_tp_lev"));
                }
            }
            // Read privilege of user by company:

            sql = "SELECT id_prv, fid_tp_lev "
                    + "FROM erp.usru_prv_co WHERE id_usr = " + session.getUser().getPkUserId() + " AND id_co = " + session.getConfigCompany().getCompanyId() + " "
                    + "ORDER BY id_prv ";

            resultSet = session.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                moPrivilegeUser.put(resultSet.getInt("id_prv"), resultSet.getInt("fid_tp_lev"));
            }
            // Read role type and privilege type of user:

            sql = "SELECT DISTINCT(tp_prv) AS tp_prv FROM(";

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
        return isSupervisor() || mnFkUserTypeId == SModSysConsts.USRS_TP_USR_ADM;
    }

    @Override
    public boolean isSupervisor() {
        return mnFkUserTypeId == SModSysConsts.USRS_TP_USR_SUP;
    }

    @Override
    public boolean hasModuleAccess(int module) {
        boolean access = false;

        if (mbExtraIsSpecialSuperUser) {
            access = true;
        }

        if (!access) {
            if (module == SModConsts.MOD_CFG_N) {
                access = mbExtraIsSpecialConfigurator;
            }
            else {
                access = mbExtraIsSpecialAdministrator;
            }
        }

        if (!access) {
            access = moModuleAccess.contains(module);
        }

        return access;
    }

    @Override
    public boolean hasPrivilege(int privilege) {
        return hasPrivilege(new int[] { privilege });
    }

    @Override
    public boolean hasPrivilege(int[] privileges) {
        boolean has = false;

        for (Integer privilege : privileges) {
            for (Integer privilegeUser : moPrivilegeUser.keySet()) {
                if (privilege.intValue() == privilegeUser.intValue()) {
                    has = true;
                    break;
                }
            }
        }

        return has;
    }

    @Override
    public int getPrivilegeLevel(int privilege) {
        int level = 0;

        if (hasPrivilege(privilege)) {
            level = moPrivilegeUser.get(privilege);
        }

        return level;
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
        SSessionCustom sessionCustom = new SSessionCustom(client.getSession());

        sessionCustom.updateSessionSettings();

        return sessionCustom;
    }

    @Override
    public SGuiSessionCustom createSessionCustom(SGuiClient client, int terminal) {
        throw new UnsupportedOperationException("Not supported yet.");
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
