/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.gui.account.SAccountUtils;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbAccount extends SDbRegistryUser {
    
    public static final int FIELD_DEEP = FIELD_BASE + 1;
    
    protected String msPkAccountIdXXX;
    protected int mnPkAccountId;
    protected String msCode;
    protected String msAccount;
    protected int mnDeep;
    protected int mnLevel;
    protected Date mtDateStart;
    protected Date mtDateEnd_n;
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
    protected Date mtUserNewTs;
    protected Date mtUserEditTs;
    protected Date mtUserDeleteTs;

    protected boolean mbDbmsIsRequiredCostCenter;
    protected boolean mbDbmsIsRequiredEntity;
    protected boolean mbDbmsIsRequiredBizPartner;
    protected boolean mbDbmsIsRequiredItem;
    protected int mnDbmsMajorDeep;
    protected String msDbmsPkAccountMajorId;
    
    public void setPkAccountIdXXX(String s) { msPkAccountIdXXX = s; }
    public void setPkAccountId(int n) { mnPkAccountId = n; }
    public void setCode(String s) { msCode = s; }
    public void setAccount(String s) { msAccount = s; }
    public void setDeep(int n) { mnDeep = n; }
    public void setLevel(int n) { mnLevel = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd_n(Date t) { mtDateEnd_n = t; }
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
    public void setUserNewTs(Date t) { mtUserNewTs = t; }
    public void setUserEditTs(Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(Date t) { mtUserDeleteTs = t; }

    public String getPkAccountIdXXX() { return msPkAccountIdXXX; }
    public int getPkAccountId() { return mnPkAccountId; }
    public String getCode() { return msCode; }
    public String getAccount() { return msAccount; }
    public int getDeep() { return mnDeep; }
    public int getLevel() { return mnLevel; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd_n() { return mtDateEnd_n; }
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
    public Date getUserNewTs() { return mtUserNewTs; }
    public Date getUserEditTs() { return mtUserEditTs; }
    public Date getUserDeleteTs() { return mtUserDeleteTs; }

    public boolean getDbmsIsRequiredCostCenter() { return mbDbmsIsRequiredCostCenter; }
    public int getDbmsMajorDeep() { return mnDbmsMajorDeep; }
    public String getDbmsPkAccountMajorId() { return msDbmsPkAccountMajorId; }

    public SDbAccount() {
        super(SModConsts.FIN_ACC);
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAccountId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAccountId };
    }

    @Override
    public void initRegistry() {        
        initBaseRegistry();
        
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

        mnDbmsMajorDeep = 0;
        mbDbmsIsRequiredCostCenter = false;
        msDbmsPkAccountMajorId = "";

    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE pk_acc = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;
        java.lang.String accountFormat = "";

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
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

                // Read aswell major account's settings:

                if (mnDeep != 0) {
                    // This account is allready a major account:

                    msDbmsPkAccountMajorId = msPkAccountIdXXX;
                    mnDbmsMajorDeep = mnDeep;
                    mbDbmsIsRequiredCostCenter = mbIsRequiredCostCenter;
                }
                else {
                    msSql = "SELECT fmt_id_acc FROM erp.cfg_param_erp ";  // first read account format
                    resultSet = session.getStatement().executeQuery(msSql);
                    if (!resultSet.next()) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        // Major account's deep:

                        accountFormat = resultSet.getString(1).replace('9', '0');
                        msDbmsPkAccountMajorId = msPkAccountIdXXX.substring(0, msPkAccountIdXXX.indexOf('-')) + accountFormat.substring(msPkAccountIdXXX.indexOf('-'));

                        msSql = "SELECT deep FROM fin_acc WHERE id_acc = '" + msDbmsPkAccountMajorId + "' ";
                        resultSet = session.getStatement().executeQuery(msSql);
                        if (!resultSet.next()) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                        else {
                            mnDbmsMajorDeep = resultSet.getInt("deep");
                        }

                        // Check if center cost and system subaccounts are required in parent account:

                        if (mnLevel == 1) {
                            mbDbmsIsRequiredCostCenter = mbIsRequiredCostCenter;
                            mbDbmsIsRequiredEntity = mbIsRequiredEntity;
                            mbDbmsIsRequiredBizPartner = mbIsRequiredBizPartner;
                            mbDbmsIsRequiredItem = mbIsRequiredItem;
                        }
                        else {
                            msSql = "SELECT b_req_cc, b_req_ent, b_req_bp, b_req_item " +
                                    "FROM fin_acc WHERE code = '" + SAccountUtils.transformAncestorCodeStd(msCode, 1) + "' ";
                            resultSet = session.getStatement().executeQuery(msSql);
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

                mbRegistryNew = false;                
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
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
                msSql += "code ";
                break;
            case SDbRegistry.FIELD_NAME:
                msSql += "acc ";
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
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_DEEP:
                msSql += "deep = " + (int) value + " ";
                break;

            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);

        mnQueryResultId = SDbConsts.SAVE_OK;
    }
}