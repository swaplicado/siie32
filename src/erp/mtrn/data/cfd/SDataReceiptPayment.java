/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data.cfd;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mfin.data.SDataRecord;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataCfdPayment;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;

/**
 * @author Sergio Flores, Isabel Servín
 */
public class SDataReceiptPayment extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    public static final int FIELD_NUM = 1001; // number
    public static final int FIELD_ST_RCP = 1002; // receipt status
    
    public static final int RETAINED_INCOME_TAX = 1;
    public static final int RETAINED_VAT = 2;
    public static final int RETAINED_SPECIAL_TAX = 3;
    public static final int[] CHARGED_16_BASE = new int[] { 16, 1 };
    public static final int[] CHARGED_16_TAX = new int[] { 16, 2 };
    public static final int[] CHARGED_08_BASE = new int[] { 8, 1 };
    public static final int[] CHARGED_08_TAX = new int[] { 8, 2 };
    public static final int[] CHARGED_00_BASE = new int[] { 0, 1 };
    
    protected int mnPkReceiptId;
    protected java.lang.String msSeries;
    protected int mnNumber;
    protected java.util.Date mtDatetime;
    protected java.lang.String msTaxRegimeCodeIssuier;
    protected java.lang.String msTaxRegimeCodeReceptor;
    protected java.lang.String msConfirmationNum;
    protected java.lang.String msCfdiRelationCode;
    protected java.lang.String msCfdiRelatedUuid;
    protected double mdTotalRetainedVat;
    protected double mdTotalRetainedIncomeTax;
    protected double mdTotalRetainedSpecialTax;
    protected double mdTotalChargedVat16Base;
    protected double mdTotalChargedVat16Tax;
    protected double mdTotalChargedVat08Base;
    protected double mdTotalChargedVat08Tax;
    protected double mdTotalChargedVat00Base;
    protected double mdTotalChargedVat00Tax;
    protected double mdTotalChargedVatExemptBase;
    protected double mdTotalPaymentLocal_r;
    protected boolean mbIsDeleted;
    protected int mnFkReceiptStatusId;
    protected int mnFkCompanyBranchId;
    protected int mnFkBizPartnerId;
    protected int mnFkFactoringBankId_n;
    protected int mnFkReceiptAnnulationTypeId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;
    
    protected SDataBizPartner moDbmsFactoringBank_n;
    
    protected ArrayList<SDataReceiptPaymentPay> maDbmsReceiptPaymentPays;
    
    /** Map of financial records (journal vouchers) to optimize its retrieval from database reading each of them only once. */
    protected HashMap<String, SDataRecord> moXtaRecordsMap; // key = financial record PK as String; value = financial record
    
    protected int mnAuxAnnulType;
    protected boolean mbAuxIsProcessingCfdi; // to reduce reading time when extra stuff is useless
    
    /**
     * Creates a new payment receipt.
     */
    public SDataReceiptPayment() {
        super(SDataConstants.TRN_PAY);

        maDbmsReceiptPaymentPays = new ArrayList<>();
        
        moXtaRecordsMap = new HashMap<>();

        reset();
    }

    public void setPkReceiptId(int n) { mnPkReceiptId = n; }
    public void setSeries(java.lang.String s) { msSeries = s; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDatetime(java.util.Date t) { mtDatetime = t; }
    public void setTaxRegimeCodeIssuier(java.lang.String s) { msTaxRegimeCodeIssuier = s; }
    public void setTaxRegimeCodeReceptor(java.lang.String s) { msTaxRegimeCodeReceptor = s; }
    public void setConfirmationNum(java.lang.String s) { msConfirmationNum = s; }
    public void setCfdiRelationCode(java.lang.String s) { msCfdiRelationCode = s; }
    public void setCfdiRelatedUuid(java.lang.String s) { msCfdiRelatedUuid = s; }
    public void setTotalRetainedVat(double d) { mdTotalRetainedVat = d; }
    public void setTotalRetainedIncomeTax(double d) { mdTotalRetainedIncomeTax = d; }
    public void setTotalRetainedSpecialTax(double d) { mdTotalRetainedSpecialTax = d; }
    public void setTotalChargedVat16Base(double d) { mdTotalChargedVat16Base = d; }
    public void setTotalChargedVat16Tax(double d) { mdTotalChargedVat16Tax = d; }
    public void setTotalChargedVat08Base(double d) { mdTotalChargedVat08Base = d; }
    public void setTotalChargedVat08Tax(double d) { mdTotalChargedVat08Tax = d; }
    public void setTotalChargedVat00Base(double d) { mdTotalChargedVat00Base = d; }
    public void setTotalChargedVat00Tax(double d) { mdTotalChargedVat00Tax = d; }
    public void setTotalChargedVatExemptBase(double d) { mdTotalChargedVatExemptBase = d; }
    public void setTotalPaymentLocal_r(double d) { mdTotalPaymentLocal_r = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkReceiptStatusId(int n) { mnFkReceiptStatusId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    public void setFkFactoringBankId_n(int n) { mnFkFactoringBankId_n = n; }
    public void setFkReceiptAnnulationTypeId(int n) { mnFkReceiptAnnulationTypeId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkReceiptId() { return mnPkReceiptId; }
    public java.lang.String getSeries() { return msSeries; }
    public int getNumber() { return mnNumber; }
    public java.util.Date getDatetime() { return mtDatetime; }
    public java.lang.String getTaxRegimeCodeIssuier() { return msTaxRegimeCodeIssuier; }
    public java.lang.String getTaxRegimeCodeReceptor() { return msTaxRegimeCodeReceptor; }
    public java.lang.String getConfirmationNum() { return msConfirmationNum; }
    public java.lang.String getCfdiRelationCode() { return msCfdiRelationCode; }
    public java.lang.String getCfdiRelatedUuid() { return msCfdiRelatedUuid; }
    public double getTotalRetainedVat() { return mdTotalRetainedVat; }
    public double getTotalRetainedIncomeTax() { return mdTotalRetainedIncomeTax; }
    public double getTotalRetainedSpecialTax() { return mdTotalRetainedSpecialTax; }
    public double getTotalChargedVat16Base() { return mdTotalChargedVat16Base; }
    public double getTotalChargedVat16Tax() { return mdTotalChargedVat16Tax; }
    public double getTotalChargedVat08Base() { return mdTotalChargedVat08Base; }
    public double getTotalChargedVat08Tax() { return mdTotalChargedVat08Tax; }
    public double getTotalChargedVat00Base() { return mdTotalChargedVat00Base; }
    public double getTotalChargedVat00Tax() { return mdTotalChargedVat00Tax; }
    public double getTotalChargedVatExemptBase() { return mdTotalChargedVatExemptBase; }
    public double getTotalPaymentLocal_r() { return mdTotalPaymentLocal_r; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkReceiptStatusId() { return mnFkReceiptStatusId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    public int getFkFactoringBankId_n() { return mnFkFactoringBankId_n; }
    public int getFkReceiptAnnulationTypeId() { return mnFkReceiptAnnulationTypeId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsFactoringBank_n(SDataBizPartner o) {
        moDbmsFactoringBank_n = o;
        
        if (moDbmsFactoringBank_n == null) {
            mnFkFactoringBankId_n = 0;
        }
        else {
            mnFkFactoringBankId_n = moDbmsFactoringBank_n.getPkBizPartnerId();
        }
    }
    
    public SDataBizPartner getDbmsFactoringBank_n() { return moDbmsFactoringBank_n; }
    
    public ArrayList<SDataReceiptPaymentPay> getDbmsReceiptPaymentPays() { return maDbmsReceiptPaymentPays; }
    
    /** Map of financial records (journal vouchers) to optimize its retrieval from database reading each of them only once.
     * @return Map of financial records.
     */
    public HashMap<String, SDataRecord> getXtaRecordsMap() { return moXtaRecordsMap; }
    
    public void setAuxAnnulType(int n) { mnAuxAnnulType = n; }
    public void setAuxIsProcessingCfdi(boolean b) { mbAuxIsProcessingCfdi = b; }
    
    public int getAuxAnnulType() { return mnAuxAnnulType; }
    public boolean getAuxIsProcessingCfdi() { return mbAuxIsProcessingCfdi; }
    
    private void computePaymentLoc() {
        mdTotalPaymentLocal_r = 0;
        
        if (mnFkReceiptStatusId != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
            for (SDataReceiptPaymentPay pay : maDbmsReceiptPaymentPays) {
                mdTotalPaymentLocal_r = SLibUtils.roundAmount(mdTotalPaymentLocal_r + pay.getPaymentLoc());
            }
        }
    }
    
    private void computeTotalTaxes() {
        mdTotalRetainedVat = 0;
        mdTotalRetainedIncomeTax = 0;
        mdTotalRetainedSpecialTax = 0;
        mdTotalChargedVat16Base = 0;
        mdTotalChargedVat16Tax = 0;
        mdTotalChargedVat08Base = 0;
        mdTotalChargedVat08Tax = 0;
        mdTotalChargedVat00Base = 0;
        mdTotalChargedVat00Tax = 0;
        mdTotalChargedVatExemptBase = 0;

        if (mnFkReceiptStatusId != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
            for (SDataReceiptPaymentPay pay : maDbmsReceiptPaymentPays) {
                mdTotalRetainedVat += pay.getRetainedTax(RETAINED_VAT);
                mdTotalRetainedIncomeTax += pay.getRetainedTax(RETAINED_INCOME_TAX);
                mdTotalRetainedSpecialTax += pay.getRetainedTax(RETAINED_SPECIAL_TAX);
                mdTotalChargedVat16Base += pay.getChargedVatTax(CHARGED_16_BASE);
                mdTotalChargedVat16Tax += pay.getChargedVatTax(CHARGED_16_TAX);
                mdTotalChargedVat08Base += pay.getChargedVatTax(CHARGED_08_BASE);
                mdTotalChargedVat08Tax += pay.getChargedVatTax(CHARGED_08_TAX);
                mdTotalChargedVat00Base += pay.getChargedVatTax(CHARGED_00_BASE);
                mdTotalChargedVatExemptBase += pay.getChargedVatExemptBase();
            }
            mdTotalRetainedVat = SLibUtils.roundAmount(mdTotalRetainedVat);
            mdTotalRetainedIncomeTax = SLibUtils.roundAmount(mdTotalRetainedIncomeTax);
            mdTotalRetainedSpecialTax = SLibUtils.roundAmount(mdTotalRetainedSpecialTax);
            mdTotalChargedVat16Base = SLibUtils.roundAmount(mdTotalChargedVat16Base);
            mdTotalChargedVat16Tax = SLibUtils.roundAmount(mdTotalChargedVat16Tax);
            mdTotalChargedVat08Base = SLibUtils.roundAmount(mdTotalChargedVat08Base);
            mdTotalChargedVat08Tax = SLibUtils.roundAmount(mdTotalChargedVat08Tax);
            mdTotalChargedVat00Base = SLibUtils.roundAmount(mdTotalChargedVat00Base);
        }
    }
    
    private boolean testAnnulment(java.sql.Connection connection, java.lang.String msg) throws java.lang.Exception {
        int param = 1;
        int[] periodKey = SLibTimeUtilities.digestYearMonth(mtDatetime);
        CallableStatement callableStatement = connection.prepareCall("{ CALL fin_year_per_st(?, ?, ?) }");
        callableStatement.setInt(param++, periodKey[0]);
        callableStatement.setInt(param++, periodKey[1]);
        callableStatement.registerOutParameter(param++, java.sql.Types.INTEGER);
        callableStatement.execute();

        if (callableStatement.getBoolean(param - 1)) {
            mnDbmsErrorId = 101;
            msDbmsError = msg + "¡El período contable de la fecha del comprobante está cerrado!";
            throw new Exception(msDbmsError);
        }

        return true; // if this line is reached, no errors were found
    }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkReceiptId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkReceiptId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkReceiptId = 0;
        msSeries = "";
        mnNumber = 0;
        mtDatetime = null;
        msTaxRegimeCodeIssuier = "";
        msTaxRegimeCodeReceptor = "";
        msConfirmationNum = "";
        msCfdiRelationCode = "";
        msCfdiRelatedUuid = "";
        mdTotalRetainedVat = 0;
        mdTotalRetainedIncomeTax = 0;
        mdTotalRetainedSpecialTax = 0;
        mdTotalChargedVat16Base = 0;
        mdTotalChargedVat16Tax = 0;
        mdTotalChargedVat08Base = 0;
        mdTotalChargedVat08Tax = 0;
        mdTotalChargedVat00Base = 0;
        mdTotalChargedVat00Tax = 0;
        mdTotalChargedVatExemptBase = 0;
        mdTotalPaymentLocal_r = 0;
        mbIsDeleted = false;
        mnFkReceiptStatusId = 0;
        mnFkCompanyBranchId = 0;
        mnFkBizPartnerId = 0;
        mnFkFactoringBankId_n = 0;
        mnFkReceiptAnnulationTypeId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
        
        moDbmsFactoringBank_n = null;
        
        maDbmsReceiptPaymentPays.clear();
        
        moXtaRecordsMap.clear();
        
        mnAuxAnnulType = 0;
        //mbAuxIsProcessingCfdi = false; // prevent from reseting this flag!
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            int[] key = (int[]) pk;
            String sql = "SELECT * "
                    + "FROM trn_pay "
                    + "WHERE id_rcp = " + key[0] + ";";
            
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\nComprobante de recepción de pagos.");
                }
                else {
                    mnPkReceiptId = resultSet.getInt("id_rcp");
                    msSeries = resultSet.getString("ser");
                    mnNumber = resultSet.getInt("num");
                    mtDatetime = resultSet.getTimestamp("dt");
                    msTaxRegimeCodeIssuier = resultSet.getString("tax_regime_code_iss");
                    msTaxRegimeCodeReceptor = resultSet.getString("tax_regime_code_rec");
                    msConfirmationNum = resultSet.getString("conf_num");
                    msCfdiRelationCode = resultSet.getString("cfdi_relation_code");
                    msCfdiRelatedUuid = resultSet.getString("cfdi_related_uuid");
                    mdTotalRetainedVat = resultSet.getDouble("tot_ret_vat");
                    mdTotalRetainedIncomeTax = resultSet.getDouble("tot_ret_income_tax");
                    mdTotalRetainedSpecialTax = resultSet.getDouble("tot_ret_special_tax");
                    mdTotalChargedVat16Base = resultSet.getDouble("tot_cha_vat_16_base");
                    mdTotalChargedVat16Tax = resultSet.getDouble("tot_cha_vat_16_tax");
                    mdTotalChargedVat08Base = resultSet.getDouble("tot_cha_vat_08_base");
                    mdTotalChargedVat08Tax = resultSet.getDouble("tot_cha_vat_08_tax");
                    mdTotalChargedVat00Base = resultSet.getDouble("tot_cha_vat_00_base");
                    mdTotalChargedVat00Tax = resultSet.getDouble("tot_cha_vat_00_tax");
                    mdTotalChargedVatExemptBase = resultSet.getDouble("tot_cha_vat_exempt_base");
                    mdTotalPaymentLocal_r = resultSet.getDouble("tot_pay_loc_r");
                    mbIsDeleted = resultSet.getBoolean("b_del");
                    mnFkReceiptStatusId = resultSet.getInt("fid_st_rcp");
                    mnFkCompanyBranchId = resultSet.getInt("fid_cob");
                    mnFkBizPartnerId = resultSet.getInt("fid_bp");
                    mnFkFactoringBankId_n = resultSet.getInt("fid_fact_bank_n");
                    mnFkReceiptAnnulationTypeId = resultSet.getInt("fid_tp_rcp_ann");
                    mnFkUserNewId = resultSet.getInt("fid_usr_new");
                    mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                    mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                    mtUserNewTs = resultSet.getTimestamp("ts_new");
                    mtUserEditTs = resultSet.getTimestamp("ts_edit");
                    mtUserDeleteTs = resultSet.getTimestamp("ts_del");
                    
                    // read as well factoring bank:
                    
                    if (mnFkFactoringBankId_n != 0) {
                        moDbmsFactoringBank_n = new SDataBizPartner();
                        moDbmsFactoringBank_n.read(new int[] { mnFkFactoringBankId_n }, statement);
                    }
                    
                    // read as well all payments:
                    
                    sql = "SELECT id_pay "
                            + "FROM trn_pay_pay "
                            + "WHERE id_rcp = " + key[0] + " "
                            + "ORDER BY id_pay;";
                    
                    try (Statement statementPays = statement.getConnection().createStatement()) {
                        ResultSet resultSetPays = statementPays.executeQuery(sql);
                        while (resultSetPays.next()) {
                            SDataReceiptPaymentPay pay = new SDataReceiptPaymentPay(this);
                            pay.setAuxIsProcessingCfdi(mbAuxIsProcessingCfdi);
                            
                            int[] payKey = new int[] { mnPkReceiptId, resultSetPays.getInt("id_pay") };
                            if (pay.read(payKey, statement) != SLibConstants.DB_ACTION_READ_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\nPago #" + SLibUtils.textImplode(payKey, "-") + ".");
                            }
                            
                            maDbmsReceiptPaymentPays.add(pay);
                        }
                    }
                    
                    computePaymentLoc();
                    
                    // finish reading registry:
                    
                    mbIsRegistryNew = false;
                    mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
                }
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
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            try (Statement statement = connection.createStatement()) {
                String sql = "";
                
                computePaymentLoc();
                computeTotalTaxes();
                
                if (mnPkReceiptId == 0) {
                    sql = "SELECT COALESCE(MAX(id_rcp), 0) + 1 "
                            + "FROM trn_pay;";
                    
                    ResultSet resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        mnPkReceiptId = resultSet.getInt(1);
                    }
                    
                    if (mnFkUserNewId == 0) {
                        mnFkUserNewId = SUtilConsts.USR_NA_ID;
                    }
                    if (mnFkUserEditId == 0) {
                        mnFkUserEditId = SUtilConsts.USR_NA_ID;
                    }
                    if (mnFkUserDeleteId == 0) {
                        mnFkUserDeleteId = SUtilConsts.USR_NA_ID;
                    }
                    
                    sql = "INSERT INTO trn_pay VALUES (" +
                            mnPkReceiptId + ", " +
                            "'" + msSeries + "', " +
                            mnNumber + ", " +
                            "'" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetime) + "', " +
                            "'" + msTaxRegimeCodeIssuier + "', " +
                            "'" + msTaxRegimeCodeReceptor + "', " +
                            "'" + msConfirmationNum + "', " +
                            "'" + msCfdiRelationCode + "', " +
                            "'" + msCfdiRelatedUuid + "', " +
                            mdTotalRetainedVat + ", " + 
                            mdTotalRetainedIncomeTax + ", " +
                            mdTotalRetainedSpecialTax + ", " +
                            mdTotalChargedVat16Base + ", " +
                            mdTotalChargedVat16Tax + ", " +
                            mdTotalChargedVat08Base + ", " +
                            mdTotalChargedVat08Tax + ", " +
                            mdTotalChargedVat00Base + ", " +
                            mdTotalChargedVat00Tax + ", " +
                            mdTotalChargedVatExemptBase + ", " +
                            mdTotalPaymentLocal_r + ", " +
                            mbIsDeleted + ", " +
                            mnFkReceiptStatusId + ", " +
                            mnFkCompanyBranchId + ", " +
                            mnFkBizPartnerId + ", " +
                            (mnFkFactoringBankId_n == 0 ? "NULL" : mnFkFactoringBankId_n) + ", " +
                            mnFkReceiptAnnulationTypeId + ", " +
                            mnFkUserNewId + ", " +
                            mnFkUserEditId + ", " +
                            mnFkUserDeleteId + ", " +
                            "NOW(), " +
                            "NOW(), " +
                            "NOW() " +
                            ");";
                    
                    mtUserNewTs = new Date();
                    mtUserEditTs = mtUserNewTs;
                    mtUserDeleteTs = mtUserNewTs;
                }
                else {
                    sql = "UPDATE trn_pay SET " +
                            //"id_rcp = " + mnPkReceiptId + ", " +
                            "ser = '" + msSeries + "', " +
                            "num = " + mnNumber + ", " +
                            "dt = '" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetime) + "', " +
                            "tax_regime_code_iss='" + msTaxRegimeCodeIssuier + "', " +
                            "tax_regime_code_rec='" + msTaxRegimeCodeReceptor + "', " +
                            "conf_num = '" + msConfirmationNum + "', " +
                            "cfdi_relation_code = '" + msCfdiRelationCode + "', " +
                            "cfdi_related_uuid = '" + msCfdiRelatedUuid + "', " +
                            "tot_ret_vat = " + mdTotalRetainedVat + ", " +
                            "tot_ret_income_tax = " + mdTotalRetainedIncomeTax + ", " +
                            "tot_ret_special_tax = " + mdTotalRetainedSpecialTax + ", " +
                            "tot_cha_vat_16_base = " + mdTotalChargedVat16Base + ", " +
                            "tot_cha_vat_16_tax = " + mdTotalChargedVat16Tax + ", " +
                            "tot_cha_vat_08_base = " + mdTotalChargedVat08Base + ", " +
                            "tot_cha_vat_08_tax = " + mdTotalChargedVat08Tax + ", " +
                            "tot_cha_vat_00_base = " + mdTotalChargedVat00Base + ", " +
                            "tot_cha_vat_00_tax = " + mdTotalChargedVat00Tax + ", " +
                            "tot_cha_vat_exempt_base = " + mdTotalChargedVatExemptBase + ", " +
                            "tot_pay_loc_r = " + mdTotalPaymentLocal_r + ", " +
                            "b_del = " + (mbIsDeleted ? 1 : 0) + ", " +
                            "fid_st_rcp = " + mnFkReceiptStatusId + ", " +
                            "fid_cob = " + mnFkCompanyBranchId + ", " +
                            "fid_bp = " + mnFkBizPartnerId + ", " +
                            "fid_fact_bank_n = " + (mnFkFactoringBankId_n == 0 ? "NULL" : mnFkFactoringBankId_n) + ", " +
                            "fid_tp_rcp_ann = " + mnFkReceiptAnnulationTypeId + ", " +
                            "fid_usr_new = " + mnFkUserNewId + ", " +
                            "fid_usr_edit = " + mnFkUserEditId + ", " +
                            "fid_usr_del = " + mnFkUserDeleteId + ", " +
                            //"ts_new = NOW(), " +
                            "ts_edit = NOW() " +
                            (mbIsDeleted ? ", ts_del = NOW() " : "") +
                            "WHERE id_rcp = " + mnPkReceiptId + ";";
                    
                    mtUserEditTs = new Date();
                    if (mbIsDeleted) {
                        mtUserDeleteTs = mtUserEditTs;
                    }
                }
                
                statement.execute(sql);
                
                // save as well all documents:
                
                if (!mbIsRegistryNew) {
                    sql = "DELETE FROM trn_pay_pay_doc_tax "
                            + "WHERE id_rcp = " + mnPkReceiptId + ";";
                    
                    statement.execute(sql);
                    
                    sql = "DELETE FROM trn_pay_pay_doc "
                            + "WHERE id_rcp = " + mnPkReceiptId + ";";
                    
                    statement.execute(sql);
                    
                    sql = "DELETE FROM trn_pay_pay_tax "
                            + "WHERE id_rcp = " + mnPkReceiptId + ";";
                    
                    statement.execute(sql);
                    
                    sql = "DELETE FROM trn_pay_pay "
                            + "WHERE id_rcp = " + mnPkReceiptId + ";";
                    
                    statement.execute(sql);
                }
                
                for (SDataReceiptPaymentPay pay : maDbmsReceiptPaymentPays) {
                    pay.setPkReceiptId(mnPkReceiptId);
                    pay.setPkPaymentId(0);
                    
                    if (pay.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP + "\nPago.");
                    }
                }
                
                // finish saving registry:
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return null;
    }

    @Override
    public int canAnnul(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConsts.UNDEFINED;

        try {
            if (testAnnulment(connection, "No se puede anular el comprobante: ")) {
                mnLastDbActionResult = SLibConstants.DB_CAN_ANNUL_YES;
            }
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_CAN_ANNUL_NO;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_CAN_ANNUL;
            }
            msDbmsError += "\n" + e.toString();
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int annul(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConsts.UNDEFINED;

        try {
            if (testAnnulment(connection, "No se puede anular el comprobante: ")) {
                mdTotalPaymentLocal_r = 0;
                mnFkReceiptStatusId = SDataConstantsSys.TRNS_ST_DPS_ANNULED;
                mnFkReceiptAnnulationTypeId = mnAuxAnnulType;
                save(connection);
                mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_OK;
            }
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_ERROR;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_ANNUL;
            }
            msDbmsError += "\n" + e.toString();
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public erp.mtrn.data.cfd.SDataReceiptPayment clone() throws CloneNotSupportedException {
        SDataReceiptPayment clone = new SDataReceiptPayment();

        clone.setIsRegistryNew(mbIsRegistryNew);

        clone.setPkReceiptId(mnPkReceiptId);
        clone.setSeries(msSeries);
        clone.setNumber(mnNumber);
        clone.setDatetime((Date) mtDatetime.clone());
        clone.setTaxRegimeCodeIssuier(msTaxRegimeCodeIssuier);
        clone.setTaxRegimeCodeReceptor(msTaxRegimeCodeReceptor);
        clone.setConfirmationNum(msConfirmationNum);
        clone.setCfdiRelationCode(msCfdiRelationCode);
        clone.setCfdiRelatedUuid(msCfdiRelatedUuid);
        clone.setTotalRetainedVat(mdTotalRetainedVat);
        clone.setTotalRetainedIncomeTax(mdTotalRetainedIncomeTax);
        clone.setTotalRetainedSpecialTax(mdTotalRetainedSpecialTax);
        clone.setTotalChargedVat16Base(mdTotalChargedVat16Base);
        clone.setTotalChargedVat16Tax(mdTotalChargedVat16Tax);
        clone.setTotalChargedVat08Base(mdTotalChargedVat08Base);
        clone.setTotalChargedVat08Tax(mdTotalChargedVat08Tax);
        clone.setTotalChargedVat00Base(mdTotalChargedVat00Base);
        clone.setTotalChargedVat00Tax(mdTotalChargedVat00Tax);
        clone.setTotalChargedVatExemptBase(mdTotalChargedVatExemptBase);
        clone.setTotalPaymentLocal_r(mdTotalPaymentLocal_r);
        clone.setIsDeleted(mbIsDeleted);
        clone.setFkReceiptStatusId(mnFkReceiptStatusId);
        clone.setFkCompanyBranchId(mnFkCompanyBranchId);
        clone.setFkBizPartnerId(mnFkBizPartnerId);
        clone.setFkFactoringBankId_n(mnFkFactoringBankId_n);
        clone.setFkReceiptAnnulationTypeId(mnFkReceiptAnnulationTypeId);
        clone.setFkUserNewId(mnFkUserNewId);
        clone.setFkUserEditId(mnFkUserEditId);
        clone.setFkUserDeleteId(mnFkUserDeleteId);
        clone.setUserNewTs((Date) mtUserNewTs.clone());
        clone.setUserEditTs((Date) mtUserEditTs.clone());
        clone.setUserDeleteTs((Date) mtUserDeleteTs.clone());
        
        for (SDataReceiptPaymentPay pay : maDbmsReceiptPaymentPays) {
            clone.getDbmsReceiptPaymentPays().add(pay.clone());
        }
        
        clone.getXtaRecordsMap().putAll(moXtaRecordsMap);
        
        clone.setAuxAnnulType(mnAuxAnnulType);
        clone.setAuxIsProcessingCfdi(mbAuxIsProcessingCfdi);

        return clone;
    }
    
    /**
     * Update a field of this payment receipt.
     * @param connection Database connection.
     * @param field Field to be updated. Available options defined in this class by constants named FIELD_...
     * @param value Updated value.
     * @param userEditId User edition ID, if any, can be zero wich means that no user edition ID is needed.
     * @throws Exception 
     */
    public void saveField(java.sql.Connection connection, final int field, final Object value, final int userEditId) throws Exception {
        String sql = "";

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        sql = "UPDATE trn_pay SET ";
        
        if (userEditId != 0) {
            sql += "fid_usr_edit = " + userEditId + ", ts_edit = NOW(), ";
        }

        switch (field) {
            case FIELD_NUM:
                sql += "num = " + value + " ";
                break;
            case FIELD_ST_RCP:
                sql += "fid_st_rcp = " + value + " ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        sql += "WHERE id_rcp = " + mnPkReceiptId + ";";
        
        connection.createStatement().execute(sql);
        
        mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
    }
    
    /**
     * Harvest all data from instance of <code>SDataCfdPayment</code>.
     * @param cfdPayment CFD of payment receipt.
     */
    public void harvestCfdPayment(final SDataCfdPayment cfdPayment) {
        SDataCfd cfd = cfdPayment.getDbmsDataCfd();
        
        //mnPkReceiptId = ...;
        msSeries = cfd.getSeries();
        mnNumber = cfd.getNumber();
        mtDatetime = (Date) cfd.getTimestamp().clone();
        msTaxRegimeCodeIssuier = cfdPayment.getAuxCfdEmisorRegimenFiscal();
        msTaxRegimeCodeReceptor = cfdPayment.getAuxCfdReceptorRegimenFiscal();
        msConfirmationNum = cfdPayment.getAuxCfdConfirmacion();
        msCfdiRelationCode = cfdPayment.getAuxCfdCfdiRelacionadosTipoRelacion();
        msCfdiRelatedUuid = cfdPayment.getAuxCfdCfdiRelacionadoUuid();
        //mdTotalRetainedVat = ...; // computed in computeTotalTaxes()
        //mdTotalRetainedIncomeTax = ...; // computed in computeTotalTaxes()
        //mdTotalRetainedSpecialTax = ...; // computed in computeTotalTaxes()
        //mdTotalChargedVat16Base = ...; // computed in computeTotalTaxes()
        //mdTotalChargedVat16Tax = ...; // computed in computeTotalTaxes()
        //mdTotalChargedVat08Base = ...; // computed in computeTotalTaxes()
        //mdTotalChargedVat08Tax = ...; // computed in computeTotalTaxes()
        //mdTotalChargedVat00Base = ...; // computed in computeTotalTaxes()
        //mdTotalChargedVat00Tax = ...; // computed in computeTotalTaxes()
        //mdTotalChargedVatExemptBase = ...; // computed in computeTotalTaxes()
        //mdTotalPaymentLocal_r = ...; // computed in computePaymentLoc()
        mbIsDeleted = false;
        mnFkReceiptStatusId = SLibUtilities.belongsTo(cfd.getFkXmlStatusId(), new int[] { SDataConstantsSys.TRNS_ST_DPS_NEW, SDataConstantsSys.TRNS_ST_DPS_EMITED }) ? SDataConstantsSys.TRNS_ST_DPS_EMITED : cfd.getFkXmlStatusId();
        mnFkCompanyBranchId = cfd.getFkCompanyBranchId_n();
        mnFkBizPartnerId = cfdPayment.getAuxCfdDbmsDataReceptor().getPkBizPartnerId();
        //mnFkFactoringBankId_n = ...; // set in setDbmsFactoringBank_n()
        mnFkReceiptAnnulationTypeId = mnFkReceiptAnnulationTypeId == 0 ? SDataConstantsSys.TRNU_TP_DPS_ANN_NA : mnFkReceiptAnnulationTypeId;
        mnFkUserNewId = cfdPayment.getFkUserNewId() != 0 ? cfdPayment.getFkUserNewId() : SUtilConsts.USR_NA_ID;
        mnFkUserEditId = cfdPayment.getFkUserEditId() != 0 ? cfdPayment.getFkUserEditId() : SUtilConsts.USR_NA_ID;
        mnFkUserDeleteId = cfdPayment.getFkUserDeleteId() != 0 ? cfdPayment.getFkUserDeleteId() : SUtilConsts.USR_NA_ID;
        //mtUserNewTs = ...;
        //mtUserEditTs = ...;
        //mtUserDeleteTs = ...;
        
        setDbmsFactoringBank_n(cfdPayment.getAuxCfdDbmsDataReceptorFactoring());

        maDbmsReceiptPaymentPays.clear();

        for (SCfdPaymentEntry paymentEntry : cfdPayment.getAuxCfdPaymentEntries()) {
            SDataReceiptPaymentPay pay = new SDataReceiptPaymentPay(this);
            pay.harvestCfdPaymentEntry(paymentEntry);
            maDbmsReceiptPaymentPays.add(pay);
        }
        
        computePaymentLoc();
        computeTotalTaxes();
    }
}
