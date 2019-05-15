/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import sa.lib.SLibConsts;

/**
 * WARNING: Every change that affects the structure of this registry must be reflected in SIIE/ETL Avista classes and methods!
 * @author Sergio Flores, Edwin Carmona
 */
public class SDataBizPartnerCategory extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkBizPartnerId;
    protected int mnPkBizPartnerCategoryId;
    protected java.lang.String msKey;
    protected java.lang.String msCompanyKey;
    protected double mdCreditLimit;
    protected int mnDaysOfCredit;
    protected int mnDaysOfGrace;
    protected double mdGuarantee;
    protected double mdInsurance;
    protected java.util.Date mtDateStart;
    protected java.util.Date mtDateEnd_n;
    protected java.lang.String msPaymentAccount;
    protected java.lang.String msNumberExporter;
    protected java.lang.String msCfdiPaymentWay;
    protected java.lang.String msCfdiCfdiUsage;
    protected boolean mbIsCreditByUser;
    protected boolean mbIsGuaranteeInProcess;
    protected boolean mbIsInsuranceInProcess;
    protected boolean mbIsDeleted;
    protected int mnFkBizPartnerCategoryId;
    protected int mnFkBizPartnerTypeId;
    protected int mnFkCreditTypeId_n;
    protected int mnFkRiskId_n;
    protected int mnFkCfdAddendaTypeId;
    protected int mnFkLanguageId_n;
    protected int mnFkCurrencyId_n;
    protected int mnFkPaymentSystemTypeId_n;
    protected int mnFkUserAnalystId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsBizPartnerCategory;
    protected java.lang.String msDbmsBizPartnerType;
    protected java.lang.String msDbmsCreditType;
    protected java.lang.String msDbmsLanguage;
    protected java.lang.String msDbmsCurrency;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;

    protected int mnAuxLanguageSysId;
    protected int mnAuxCurrencySysId;

    protected double mdEffectiveCreditLimit;
    protected int mnEffectiveDaysOfCredit;
    protected int mnEffectiveDaysOfGrace;
    protected int mnEffectiveCreditTypeId;
    protected int mnEffectiveRiskTypeId;

    public SDataBizPartnerCategory() {
        super(SDataConstants.BPSU_BP_CT);
        reset();
    }

    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkBizPartnerCategoryId(int n) { mnPkBizPartnerCategoryId = n; }
    public void setKey(java.lang.String s) { msKey = s; }
    public void setCompanyKey(java.lang.String s) { msCompanyKey = s; }
    public void setCreditLimit(double d) { mdCreditLimit = d; }
    public void setDaysOfCredit(int n) { mnDaysOfCredit = n; }
    public void setDaysOfGrace(int n) { mnDaysOfGrace = n; }
    public void setGuarantee(double d) { mdGuarantee = d; }
    public void setInsurance(double d) { mdInsurance = d; }
    public void setDateStart(java.util.Date t) { mtDateStart = t; }
    public void setDateEnd_n(java.util.Date t) { mtDateEnd_n = t; }
    public void setPaymentAccount(java.lang.String s) { msPaymentAccount = s; }
    public void setNumberExporter(java.lang.String s) { msNumberExporter = s; }
    public void setCfdiPaymentWay(java.lang.String s) { msCfdiPaymentWay = s; }
    public void setCfdiCfdiUsage(java.lang.String s) { msCfdiCfdiUsage = s; }
    public void setIsCreditByUser(boolean b) { mbIsCreditByUser = b; }
    public void setIsGuaranteeInProcess(boolean b) { mbIsGuaranteeInProcess = b; }
    public void setIsInsuranceInProcess(boolean b) { mbIsInsuranceInProcess = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkBizPartnerCategoryId(int n) { mnFkBizPartnerCategoryId = n; }
    public void setFkBizPartnerTypeId(int n) { mnFkBizPartnerTypeId = n; }
    public void setFkCreditTypeId_n(int n) { mnFkCreditTypeId_n = n; }
    public void setFkRiskId_n(int n) { mnFkRiskId_n = n; }
    public void setFkCfdAddendaTypeId(int n) { mnFkCfdAddendaTypeId = n; }
    public void setFkLanguageId_n(int n) { mnFkLanguageId_n = n; }
    public void setFkCurrencyId_n(int n) { mnFkCurrencyId_n = n; }
    public void setFkPaymentSystemTypeId_n(int n) { mnFkPaymentSystemTypeId_n = n; }
    public void setFkUserAnalystId_n(int n) { mnFkUserAnalystId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkBizPartnerCategoryId() { return mnPkBizPartnerCategoryId; }
    public java.lang.String getKey() { return msKey; }
    public java.lang.String getCompanyKey() { return msCompanyKey; }
    public double getCreditLimit() { return mdCreditLimit; }
    public int getDaysOfCredit() { return mnDaysOfCredit; }
    public int getDaysOfGrace() { return mnDaysOfGrace; }
    public double getGuarantee() { return mdGuarantee; }
    public double getInsurance() { return mdInsurance; }
    public java.util.Date getDateStart() { return mtDateStart; }
    public java.util.Date getDateEnd_n() { return mtDateEnd_n; }
    public java.lang.String getPaymentAccount() { return msPaymentAccount; }
    public java.lang.String getNumberExporter() { return msNumberExporter; }
    public java.lang.String getCfdiPaymentWay() { return msCfdiPaymentWay; }
    public java.lang.String getCfdiCfdiUsage() { return msCfdiCfdiUsage; }
    public boolean getIsCreditByUser() { return mbIsCreditByUser; }
    public boolean getIsGuaranteeInProcess() { return mbIsGuaranteeInProcess; }
    public boolean getIsInsuranceInProcess() { return mbIsInsuranceInProcess; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkBizPartnerCategoryId() { return mnFkBizPartnerCategoryId; }
    public int getFkBizPartnerTypeId() { return mnFkBizPartnerTypeId; }
    public int getFkCreditTypeId_n() { return mnFkCreditTypeId_n; }
    public int getFkRiskId_n() { return mnFkRiskId_n; }
    public int getFkCfdAddendaTypeId() { return mnFkCfdAddendaTypeId; }
    public int getFkLanguageId_n() { return mnFkLanguageId_n; }
    public int getFkCurrencyId_n() { return mnFkCurrencyId_n; }
    public int getFkPaymentSystemTypeId_n() { return mnFkPaymentSystemTypeId_n; }
    public int getFkUserAnalystId_n() { return mnFkUserAnalystId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsBizPartnerCategory(java.lang.String s) { msDbmsBizPartnerCategory = s; }
    public void setDbmBizPartnerType(java.lang.String s) { msDbmsBizPartnerType= s; }
    public void setDbmsCreditType(java.lang.String s) { msDbmsCreditType = s; }
    public void setDbmsLanguage(java.lang.String s) { msDbmsLanguage = s; }
    public void setDbmsCurrency(java.lang.String s) { msDbmsCurrency = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public void setAuxLanguageSysId(int n) { mnAuxLanguageSysId = n; }
    public void setAuxCurrencySysId(int n) { mnAuxCurrencySysId = n; }

    public java.lang.String getDbmsBizPartnerCategory() { return msDbmsBizPartnerCategory; }
    public java.lang.String getDbmsBizPartnerType() { return msDbmsBizPartnerType; }
    public java.lang.String getDbmsCreditType() { return msDbmsCreditType; }
    public java.lang.String getDbmsLanguage() { return msDbmsLanguage; }
    public java.lang.String getDbmsCurrency() { return msDbmsCurrency; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }

    public int getAuxLanguageSysId() { return mnAuxLanguageSysId; }
    public int getAuxCurrencySysId() { return mnAuxCurrencySysId; }

    public double getEffectiveCreditLimit() { return mdEffectiveCreditLimit; }
    public int getEffectiveDaysOfCredit() { return mnEffectiveDaysOfCredit; }
    public int getEffectiveDaysOfGrace() { return mnEffectiveDaysOfGrace; }
    public int getEffectiveCreditTypeId() { return mnEffectiveCreditTypeId; }
    public int getEffectiveRiskTypeId() { return mnEffectiveRiskTypeId; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBizPartnerId = ((int[]) pk)[0];
        mnPkBizPartnerCategoryId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkBizPartnerId, mnPkBizPartnerCategoryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkBizPartnerId = 0;
        mnPkBizPartnerCategoryId = 0;
        msKey = "";
        msCompanyKey = "";
        mdCreditLimit = 0;
        mnDaysOfCredit = 0;
        mnDaysOfGrace = 0;
        mdGuarantee = 0;
        mdInsurance = 0;
        mtDateStart = null;
        mtDateEnd_n = null;
        msPaymentAccount = "";
        msNumberExporter = "";
        msCfdiPaymentWay = "";
        msCfdiCfdiUsage = "";
        mbIsCreditByUser = false;
        mbIsGuaranteeInProcess = false;
        mbIsInsuranceInProcess = false;
        mbIsDeleted = false;
        mnFkBizPartnerCategoryId = 0;
        mnFkBizPartnerTypeId = 0;
        mnFkCreditTypeId_n = 0;
        mnFkRiskId_n = 0;
        mnFkCfdAddendaTypeId = 0;
        mnFkLanguageId_n = 0;
        mnFkCurrencyId_n = 0;
        mnFkPaymentSystemTypeId_n = 0;
        mnFkUserAnalystId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsBizPartnerCategory = "";
        msDbmsBizPartnerType = "";
        msDbmsCreditType = "";
        msDbmsLanguage = "";
        msDbmsCurrency = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";

        mnAuxLanguageSysId = 0;
        mnAuxCurrencySysId = 0;

        mdEffectiveCreditLimit = 0;
        mnEffectiveDaysOfCredit = 0;
        mnEffectiveDaysOfGrace = 0;
        mnEffectiveCreditTypeId = 0;
        mnEffectiveRiskTypeId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConsts.UNDEFINED;
        reset();

        try {
            sql = "SELECT bp_ct.*, ct_bp.ct_bp, tp_bp.tp_bp, tp_cred.tp_cred, lan.lan_key, cur.cur_key, un.usr, ue.usr, ud.usr, " +
                    "tp_bp.cred_lim, tp_bp.days_cred, tp_bp.days_grace, tp_bp.fid_tp_cred, tp_bp.fid_risk " +
                    "FROM erp.bpsu_bp_ct AS bp_ct " +
                    "INNER JOIN erp.bpss_ct_bp AS ct_bp ON bp_ct.fid_ct_bp = ct_bp.id_ct_bp " +
                    "INNER JOIN erp.bpsu_tp_bp AS tp_bp ON bp_ct.fid_tp_bp = tp_bp.id_tp_bp AND tp_bp.id_ct_bp = ct_bp.id_ct_bp " +
                    "INNER JOIN erp.usru_usr AS un ON bp_ct.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON bp_ct.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON bp_ct.fid_usr_del = ud.id_usr " +
                    "LEFT OUTER JOIN erp.bpss_tp_cred AS tp_cred ON bp_ct.fid_tp_cred_n = tp_cred.id_tp_cred " +
                    "LEFT OUTER JOIN erp.cfgu_lan AS lan ON bp_ct.fid_lan_n = lan.id_lan " +
                    "LEFT OUTER JOIN erp.cfgu_cur AS cur ON bp_ct.fid_cur_n = cur.id_cur " +
                    "WHERE bp_ct.id_bp = " + key[0] + " AND bp_ct.id_ct_bp = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBizPartnerId = resultSet.getInt("bp_ct.id_bp");
                mnPkBizPartnerCategoryId = resultSet.getInt("bp_ct.id_ct_bp");
                msKey = resultSet.getString("bp_ct.bp_key");
                msCompanyKey = resultSet.getString("bp_ct.co_key");
                mdCreditLimit = resultSet.getDouble("bp_ct.cred_lim");
                mnDaysOfCredit = resultSet.getInt("bp_ct.days_cred");
                mnDaysOfGrace = resultSet.getInt("bp_ct.days_grace");
                mdGuarantee = resultSet.getDouble("bp_ct.garnt");
                mdInsurance = resultSet.getDouble("bp_ct.insur");
                mtDateStart = resultSet.getDate("bp_ct.dt_start");
                mtDateEnd_n = resultSet.getDate("bp_ct.dt_end_n");
                msPaymentAccount = resultSet.getString("bp_ct.pay_account");
                msNumberExporter = resultSet.getString("bp_ct.num_exporter");
                msCfdiPaymentWay = resultSet.getString("bp_ct.cfdi_pay_way");
                msCfdiCfdiUsage = resultSet.getString("bp_ct.cfdi_cfd_use");
                mbIsCreditByUser = resultSet.getBoolean("bp_ct.b_cred_usr");
                mbIsGuaranteeInProcess = resultSet.getBoolean("bp_ct.b_garnt_prc");
                mbIsInsuranceInProcess = resultSet.getBoolean("bp_ct.b_insur_prc");
                mbIsDeleted = resultSet.getBoolean("bp_ct.b_del");
                mnFkBizPartnerCategoryId = resultSet.getInt("bp_ct.fid_ct_bp");
                mnFkBizPartnerTypeId = resultSet.getInt("bp_ct.fid_tp_bp");
                mnFkCreditTypeId_n = resultSet.getInt("bp_ct.fid_tp_cred_n");
                if (resultSet.wasNull()) mnFkCreditTypeId_n = 0;
                mnFkRiskId_n = resultSet.getInt("bp_ct.fid_risk_n");
                if (resultSet.wasNull()) mnFkRiskId_n = 0;
                mnFkCfdAddendaTypeId = resultSet.getInt("bp_ct.fid_tp_cfd_add");
                mnFkLanguageId_n = resultSet.getInt("bp_ct.fid_lan_n");
                if (resultSet.wasNull()) mnFkLanguageId_n = 0;
                mnFkCurrencyId_n = resultSet.getInt("bp_ct.fid_cur_n");
                if (resultSet.wasNull()) mnFkCurrencyId_n = 0;
                mnFkPaymentSystemTypeId_n = resultSet.getInt("fid_tp_pay_sys_n");
                if (resultSet.wasNull()) mnFkPaymentSystemTypeId_n = 0;
                mnFkUserAnalystId_n = resultSet.getInt("bp_ct.fid_usr_ana_n");
                if (resultSet.wasNull()) mnFkUserAnalystId_n = 0;
                mnFkUserNewId = resultSet.getInt("bp_ct.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("bp_ct.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("bp_ct.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("bp_ct.ts_new");
                mtUserEditTs = resultSet.getTimestamp("bp_ct.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("bp_ct.ts_del");

                msDbmsBizPartnerCategory = resultSet.getString("ct_bp.ct_bp");
                msDbmsBizPartnerType = resultSet.getString("tp_bp.tp_bp");
                msDbmsCreditType = resultSet.getString("tp_cred.tp_cred");
                msDbmsLanguage = resultSet.getString("lan.lan_key");
                msDbmsCurrency = resultSet.getString("cur.cur_key");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

                if (mbIsCreditByUser) {
                    mdEffectiveCreditLimit = mdCreditLimit;
                    mnEffectiveDaysOfCredit = mnDaysOfCredit;
                    mnEffectiveDaysOfGrace = mnDaysOfGrace;
                    mnEffectiveCreditTypeId = mnFkCreditTypeId_n;
                    mnEffectiveRiskTypeId = mnFkRiskId_n;
                }
                else {
                    mdEffectiveCreditLimit = resultSet.getDouble("tp_bp.cred_lim");
                    mnEffectiveDaysOfCredit = resultSet.getInt("tp_bp.days_cred");
                    mnEffectiveDaysOfGrace = resultSet.getInt("tp_bp.days_grace");
                    mnEffectiveCreditTypeId = resultSet.getInt("tp_bp.fid_tp_cred");
                    mnEffectiveRiskTypeId = resultSet.getInt("tp_bp.fid_risk");
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
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConsts.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL erp.bpsu_bp_ct_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?) }");
            callableStatement.setInt(nParam++, mnPkBizPartnerId);
            callableStatement.setInt(nParam++, mnPkBizPartnerCategoryId);
            callableStatement.setString(nParam++, msKey);
            callableStatement.setString(nParam++, msCompanyKey);
            callableStatement.setDouble(nParam++, mdCreditLimit);
            callableStatement.setInt(nParam++, mnDaysOfCredit);
            callableStatement.setInt(nParam++, mnDaysOfGrace);
            callableStatement.setDouble(nParam++, mdGuarantee);
            callableStatement.setDouble(nParam++, mdInsurance);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDateStart.getTime()));
            if (mtDateEnd_n != null) callableStatement.setDate(nParam++, new java.sql.Date(mtDateEnd_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.DATE);
            callableStatement.setString(nParam++, msPaymentAccount);
            callableStatement.setString(nParam++, msNumberExporter);
            callableStatement.setString(nParam++, msCfdiPaymentWay);
            callableStatement.setString(nParam++, msCfdiCfdiUsage);
            callableStatement.setBoolean(nParam++, mbIsCreditByUser);
            callableStatement.setBoolean(nParam++, mbIsGuaranteeInProcess);
            callableStatement.setBoolean(nParam++, mbIsInsuranceInProcess);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkBizPartnerCategoryId);
            callableStatement.setInt(nParam++, mnFkBizPartnerTypeId);
            if (mnFkCreditTypeId_n > SLibConsts.UNDEFINED) callableStatement.setInt(nParam++, mnFkCreditTypeId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkRiskId_n > SLibConsts.UNDEFINED) callableStatement.setInt(nParam++, mnFkRiskId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            callableStatement.setInt(nParam++, mnFkCfdAddendaTypeId);
            if (mnFkLanguageId_n > SLibConsts.UNDEFINED && mnFkLanguageId_n != mnAuxLanguageSysId) callableStatement.setInt(nParam++, mnFkLanguageId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkCurrencyId_n > SLibConsts.UNDEFINED && mnFkCurrencyId_n != mnAuxCurrencySysId) callableStatement.setInt(nParam++, mnFkCurrencyId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkPaymentSystemTypeId_n > SLibConsts.UNDEFINED) callableStatement.setInt(nParam++, mnFkPaymentSystemTypeId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkUserAnalystId_n > SLibConsts.UNDEFINED) callableStatement.setInt(nParam++, mnFkUserAnalystId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
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
