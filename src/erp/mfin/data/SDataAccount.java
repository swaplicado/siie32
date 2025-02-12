/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.data.SDataConstants;
import erp.gui.account.SAccountUtils;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following classes:
 * - erp.mod.fin.db.SFiscalAccounts
 * All of them also make raw SQL insertions.
 */

/**
 *
 * @author Sergio Flores
 */
public class SDataAccount extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected java.lang.String msPkAccountIdXXX;
    protected int mnPkAccountId;
    protected java.lang.String msCode;
    protected java.lang.String msAccount;
    protected int mnDeep;
    protected int mnLevel;
    protected java.util.Date mtDateStart;
    protected java.util.Date mtDateEnd_n;
    protected boolean mbIsRequiredCostCenter;
    protected boolean mbIsRequiredEntity;
    protected boolean mbIsRequiredBizPartner;
    protected boolean mbIsRequiredItem;
    protected boolean mbIsContraAccount;
    protected boolean mbIsActive;
    protected boolean mbIsDeleted;
    protected int mnFkAccountTypeId_r;
    protected int mnFkAccountClassId_r;
    protected int mnFkAccountSubclassId_r;
    protected int mnFkAccountUserTypeId;
    protected int mnFkAccountUserClassId;
    protected int mnFkAccountUserSubclassId;
    protected int mnFkAccountSpecializedTypeId;
    protected int mnFkAccountSystemTypeId;
    protected int mnFkAccountLedgerTypeId;
    protected int mnFkAccountEbitdaTypeId;
    protected int mnFkAssetFixedTypeId;
    protected int mnFkAssetDifferredTypedId;
    protected int mnFkLiabilityDifferredTypeId;
    protected int mnFkExpenseOperativeTypeId;
    protected int mnFkAdministrativeConceptTypeId;
    protected int mnFkTaxableConceptTypeId;
    protected int mnFkFiscalAccountId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected boolean mbDbmsIsRequiredCostCenter;
    protected boolean mbDbmsIsRequiredEntity;
    protected boolean mbDbmsIsRequiredBizPartner;
    protected boolean mbDbmsIsRequiredItem;
    protected int mnDbmsLedgerAccountDeep;
    protected java.lang.String msDbmsPkLedgerAccountIdXXX;

    public SDataAccount() {
        super(SDataConstants.FIN_ACC);
        reset();
    }

    public void setPkAccountIdXXX(java.lang.String s) { msPkAccountIdXXX = s; }
    public void setPkAccountId(int n) { mnPkAccountId = n; }
    public void setCode(java.lang.String s) { msCode = s; }
    public void setAccount(java.lang.String s) { msAccount = s; }
    public void setDeep(int n) { mnDeep = n; }
    public void setLevel(int n) { mnLevel = n; }
    public void setDateStart(java.util.Date t) { mtDateStart = t; }
    public void setDateEnd_n(java.util.Date t) { mtDateEnd_n = t; }
    public void setIsRequiredCostCenter(boolean b) { mbIsRequiredCostCenter = b; }
    public void setIsRequiredEntity(boolean b) { mbIsRequiredEntity = b; }
    public void setIsRequiredBizPartner(boolean b) { mbIsRequiredBizPartner = b; }
    public void setIsRequiredItem(boolean b) { mbIsRequiredItem = b; }
    public void setIsContraAccount(boolean b) { mbIsContraAccount = b; }
    public void setIsActive(boolean b) { mbIsActive = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkAccountTypeId_r(int n) { mnFkAccountTypeId_r = n; }
    public void setFkAccountClassId_r(int n) { mnFkAccountClassId_r = n; }
    public void setFkAccountSubclassId_r(int n) { mnFkAccountSubclassId_r = n; }
    public void setFkAccountUserTypeId(int n) { mnFkAccountUserTypeId = n; }
    public void setFkAccountUserClassId(int n) { mnFkAccountUserClassId = n; }
    public void setFkAccountSpecializedTypeId(int n) { mnFkAccountSpecializedTypeId = n; }
    public void setFkAccountUserSubclassId(int n) { mnFkAccountUserSubclassId = n; }
    public void setFkAccountSystemTypeId(int n) { mnFkAccountSystemTypeId = n; }
    public void setFkAccountLedgerTypeId(int n) { mnFkAccountLedgerTypeId = n; }
    public void setFkAccountEbitdaTypeId(int n) { mnFkAccountEbitdaTypeId = n; }
    public void setFkAssetFixedTypeId(int n) { mnFkAssetFixedTypeId = n; }
    public void setFkAssetDifferredTypedId(int n) { mnFkAssetDifferredTypedId = n; }
    public void setFkLiabilityDifferredTypeId(int n) { mnFkLiabilityDifferredTypeId = n; }
    public void setFkExpenseOperativeTypeId(int n) { mnFkExpenseOperativeTypeId = n; }
    public void setFkAdministrativeConceptTypeId(int n) { mnFkAdministrativeConceptTypeId = n; }
    public void setFkTaxableConceptTypeId(int n) { mnFkTaxableConceptTypeId = n; }
    public void setFkFiscalAccountId(int n) { mnFkFiscalAccountId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public java.lang.String getPkAccountIdXXX() { return msPkAccountIdXXX; }
    public int getPkAccountId() { return mnPkAccountId; }
    public java.lang.String getCode() { return msCode; }
    public java.lang.String getAccount() { return msAccount; }
    public int getDeep() { return mnDeep; }
    public int getLevel() { return mnLevel; }
    public java.util.Date getDateStart() { return mtDateStart; }
    public java.util.Date getDateEnd_n() { return mtDateEnd_n; }
    public boolean getIsRequiredCostCenter() { return mbIsRequiredCostCenter; }
    public boolean getIsRequiredEntity() { return mbIsRequiredEntity; }
    public boolean getIsRequiredBizPartner() { return mbIsRequiredBizPartner; }
    public boolean getIsRequiredItem() { return mbIsRequiredItem; }
    public boolean getIsContraAccount() { return mbIsContraAccount; }
    public boolean getIsActive() { return mbIsActive; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkAccountTypeId_r() { return mnFkAccountTypeId_r; }
    public int getFkAccountClassId_r() { return mnFkAccountClassId_r; }
    public int getFkAccountSubclassId_r() { return mnFkAccountSubclassId_r; }
    public int getFkAccountUserTypeId() { return mnFkAccountUserTypeId; }
    public int getFkAccountUserClassId() { return mnFkAccountUserClassId; }
    public int getFkAccountSpecializedTypeId() { return mnFkAccountSpecializedTypeId; }
    public int getFkAccountUserSubclassId() { return mnFkAccountUserSubclassId; }
    public int getFkAccountSystemTypeId() { return mnFkAccountSystemTypeId; }
    public int getFkAccountLedgerTypeId() { return mnFkAccountLedgerTypeId; }
    public int getFkAccountEbitdaTypeId() { return mnFkAccountEbitdaTypeId; }
    public int getFkAssetFixedTypeId() { return mnFkAssetFixedTypeId; }
    public int getFkAssetDifferredTypedId() { return mnFkAssetDifferredTypedId; }
    public int getFkLiabilityDifferredTypeId() { return mnFkLiabilityDifferredTypeId; }
    public int getFkExpenseOperativeTypeId() { return mnFkExpenseOperativeTypeId; }
    public int getFkAdministrativeConceptTypeId() { return mnFkAdministrativeConceptTypeId; }
    public int getFkTaxableConceptTypeId() { return mnFkTaxableConceptTypeId; }
    public int getFkFiscalAccountId() { return mnFkFiscalAccountId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public boolean getDbmsIsRequiredCostCenter() { return mbDbmsIsRequiredCostCenter; }
    public int getDbmsLedgerAccountDeep() { return mnDbmsLedgerAccountDeep; }
    public java.lang.String getDbmsPkLedgerAccountIdXXX() { return msDbmsPkLedgerAccountIdXXX; }
    
    public int[] getAccountClassKey() { return new int[] { mnFkAccountTypeId_r, mnFkAccountClassId_r }; }
    public int[] getAccountSubclassKey() { return new int[] { mnFkAccountTypeId_r, mnFkAccountClassId_r, mnFkAccountSubclassId_r }; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        msPkAccountIdXXX = (String) ((Object[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new Object[] { msPkAccountIdXXX };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        msPkAccountIdXXX = "";
        mnPkAccountId = 0;
        msCode = "";
        msAccount = "";
        mnDeep = 0;
        mnLevel = 0;
        mtDateStart = null;
        mtDateEnd_n = null;
        mbIsRequiredCostCenter = false;
        mbIsRequiredEntity = false;
        mbIsRequiredBizPartner = false;
        mbIsRequiredItem = false;
        mbIsContraAccount = false;
        mbIsActive = false;
        mbIsDeleted = false;
        mnFkAccountTypeId_r = 0;
        mnFkAccountClassId_r = 0;
        mnFkAccountSubclassId_r = 0;
        mnFkAccountUserTypeId = 0;
        mnFkAccountUserClassId = 0;
        mnFkAccountUserSubclassId = 0;
        mnFkAccountSpecializedTypeId= 0;
        mnFkAccountSystemTypeId = 0;
        mnFkAccountLedgerTypeId = 0;
        mnFkAccountEbitdaTypeId = 0;
        mnFkAssetFixedTypeId = 0;
        mnFkAssetDifferredTypedId = 0;
        mnFkLiabilityDifferredTypeId = 0;
        mnFkExpenseOperativeTypeId = 0;
        mnFkAdministrativeConceptTypeId = 0;
        mnFkTaxableConceptTypeId = 0;
        mnFkFiscalAccountId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mbDbmsIsRequiredCostCenter = false;
        mnDbmsLedgerAccountDeep = 0;
        msDbmsPkLedgerAccountIdXXX = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        Object[] key = (Object[]) pk;
        java.lang.String sql = "";
        java.lang.String accountFormat = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM fin_acc WHERE id_acc = '" + key[0] + "' ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                msPkAccountIdXXX = resultSet.getString("id_acc");
                mnPkAccountId = resultSet.getInt("pk_acc");
                msCode = resultSet.getString("code");
                msAccount = resultSet.getString("acc");
                mnDeep = resultSet.getInt("deep");
                mnLevel = resultSet.getInt("lev");
                mtDateStart = resultSet.getDate("dt_start");
                mtDateEnd_n = resultSet.getDate("dt_end_n");
                if (resultSet.wasNull()) mtDateEnd_n = null;
                mbIsRequiredCostCenter = resultSet.getBoolean("b_req_cc");
                mbIsRequiredEntity = resultSet.getBoolean("b_req_ent");
                mbIsRequiredBizPartner = resultSet.getBoolean("b_req_bp");
                mbIsRequiredItem = resultSet.getBoolean("b_req_item");
                mbIsContraAccount = resultSet.getBoolean("b_contra_acc");
                mbIsActive = resultSet.getBoolean("b_act");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkAccountTypeId_r = resultSet.getInt("fid_tp_acc_r");
                mnFkAccountClassId_r = resultSet.getInt("fid_cl_acc_r");
                mnFkAccountSubclassId_r = resultSet.getInt("fid_cls_acc_r");
                mnFkAccountUserTypeId = resultSet.getInt("fid_tp_acc_usr");
                mnFkAccountUserClassId = resultSet.getInt("fid_cl_acc_usr");
                mnFkAccountUserSubclassId = resultSet.getInt("fid_cls_acc_usr");
                mnFkAccountSpecializedTypeId= resultSet.getInt("fid_tp_acc_spe");
                mnFkAccountSystemTypeId = resultSet.getInt("fid_tp_acc_sys");
                mnFkAccountLedgerTypeId = resultSet.getInt("fid_tp_acc_ledger");
                mnFkAccountEbitdaTypeId = resultSet.getInt("fid_tp_acc_ebitda");
                mnFkAssetFixedTypeId = resultSet.getInt("fid_tp_asset_fix");
                mnFkAssetDifferredTypedId = resultSet.getInt("fid_tp_asset_dif");
                mnFkLiabilityDifferredTypeId = resultSet.getInt("fid_tp_liabty_dif");
                mnFkExpenseOperativeTypeId = resultSet.getInt("fid_tp_expens_op");
                mnFkAdministrativeConceptTypeId = resultSet.getInt("fid_tp_adm_cpt");
                mnFkTaxableConceptTypeId = resultSet.getInt("fid_tp_tax_cpt");
                mnFkFiscalAccountId = resultSet.getInt("fid_fiscal_acc");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                // Read as well ledger account's settings:

                if (mnDeep != 0) {
                    // This account is allready a ledger account:

                    msDbmsPkLedgerAccountIdXXX = msPkAccountIdXXX;
                    mnDbmsLedgerAccountDeep = mnDeep;
                    mbDbmsIsRequiredCostCenter = mbIsRequiredCostCenter;
                }
                else {
                    sql = "SELECT fmt_id_acc FROM erp.cfg_param_erp ";  // first read account format
                    resultSet = statement.executeQuery(sql);
                    if (!resultSet.next()) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        // Ledger account's deep:

                        accountFormat = resultSet.getString(1).replace('9', '0');
                        msDbmsPkLedgerAccountIdXXX = msPkAccountIdXXX.substring(0, msPkAccountIdXXX.indexOf('-')) + accountFormat.substring(msPkAccountIdXXX.indexOf('-'));

                        sql = "SELECT deep FROM fin_acc WHERE id_acc = '" + msDbmsPkLedgerAccountIdXXX + "' ";
                        resultSet = statement.executeQuery(sql);
                        if (!resultSet.next()) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                        else {
                            mnDbmsLedgerAccountDeep = resultSet.getInt("deep");
                        }

                        // Check if center cost and system subaccounts are required in parent account:

                        if (mnLevel == 1) {
                            mbDbmsIsRequiredCostCenter = mbIsRequiredCostCenter;
                            mbDbmsIsRequiredEntity = mbIsRequiredEntity;
                            mbDbmsIsRequiredBizPartner = mbIsRequiredBizPartner;
                            mbDbmsIsRequiredItem = mbIsRequiredItem;
                        }
                        else {
                            sql = "SELECT b_req_cc, b_req_ent, b_req_bp, b_req_item " +
                                    "FROM fin_acc WHERE code = '" + SAccountUtils.transformAncestorCodeStd(msCode, 1) + "' ";
                            resultSet = statement.executeQuery(sql);
                            if (!resultSet.next()) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                            }
                            else {
                                mbDbmsIsRequiredCostCenter = resultSet.getBoolean("b_req_cc");
                                mbDbmsIsRequiredEntity = resultSet.getBoolean("b_req_ent");
                                mbDbmsIsRequiredBizPartner = resultSet.getBoolean("b_req_bp");
                                mbDbmsIsRequiredItem = resultSet.getBoolean("b_req_item");
                            }
                        }
                    }
                }

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
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL fin_acc_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?) }");
            callableStatement.setString(nParam++, msPkAccountIdXXX);
            callableStatement.setInt(nParam++, mnPkAccountId);
            callableStatement.setString(nParam++, msCode);
            callableStatement.setString(nParam++, msAccount);
            callableStatement.setInt(nParam++, mnDeep);
            callableStatement.setInt(nParam++, mnLevel);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDateStart.getTime()));
            if (mtDateEnd_n != null) callableStatement.setDate(nParam++, new java.sql.Date(mtDateEnd_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.DATE);
            callableStatement.setBoolean(nParam++, mbIsRequiredCostCenter);
            callableStatement.setBoolean(nParam++, mbIsRequiredEntity);
            callableStatement.setBoolean(nParam++, mbIsRequiredBizPartner);
            callableStatement.setBoolean(nParam++, mbIsRequiredItem);
            callableStatement.setBoolean(nParam++, mbIsContraAccount);
            callableStatement.setBoolean(nParam++, mbIsActive);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkAccountTypeId_r);
            callableStatement.setInt(nParam++, mnFkAccountClassId_r);
            callableStatement.setInt(nParam++, mnFkAccountSubclassId_r);
            callableStatement.setInt(nParam++, mnFkAccountUserTypeId);
            callableStatement.setInt(nParam++, mnFkAccountUserClassId);
            callableStatement.setInt(nParam++, mnFkAccountUserSubclassId);
            callableStatement.setInt(nParam++, mnFkAccountSpecializedTypeId);
            callableStatement.setInt(nParam++, mnFkAccountSystemTypeId);
            callableStatement.setInt(nParam++, mnFkAccountLedgerTypeId);
            callableStatement.setInt(nParam++, mnFkAccountEbitdaTypeId);
            callableStatement.setInt(nParam++, mnFkAssetFixedTypeId);
            callableStatement.setInt(nParam++, mnFkAssetDifferredTypedId);
            callableStatement.setInt(nParam++, mnFkLiabilityDifferredTypeId);
            callableStatement.setInt(nParam++, mnFkExpenseOperativeTypeId);
            callableStatement.setInt(nParam++, mnFkAdministrativeConceptTypeId);
            callableStatement.setInt(nParam++, mnFkTaxableConceptTypeId);
            callableStatement.setInt(nParam++, mnFkFiscalAccountId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
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
}
