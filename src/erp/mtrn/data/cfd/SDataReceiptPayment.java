/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data.cfd;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mfin.data.SDataRecord;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataCfdPayment;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;

/**
 * @author Sergio Flores
 */
public class SDataReceiptPayment extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    public static final int FIELD_NUM = 1;
    public static final int FIELD_ST_RCP = 2;

    protected int mnPkReceiptId;
    protected java.lang.String msSeries;
    protected int mnNumber;
    protected java.util.Date mtDatetime;
    protected java.lang.String msTaxRegimeCode;
    protected java.lang.String msConfirmationNum;
    protected java.lang.String msCfdiRelationCode;
    protected java.lang.String msCfdiRelatedUuid;
    protected double mdPaymentLoc_r;
    protected boolean mbIsDeleted;
    protected int mnFkReceiptStatusId;
    protected int mnFkCompanyBranchId;
    protected int mnFkBizPartnerId;
    protected int mnFkFactoringBankId_n;
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
    public void setTaxRegimeCode(java.lang.String s) { msTaxRegimeCode = s; }
    public void setConfirmationNum(java.lang.String s) { msConfirmationNum = s; }
    public void setCfdiRelationCode(java.lang.String s) { msCfdiRelationCode = s; }
    public void setCfdiRelatedUuid(java.lang.String s) { msCfdiRelatedUuid = s; }
    public void setPaymentLoc_r(double d) { mdPaymentLoc_r = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkReceiptStatusId(int n) { mnFkReceiptStatusId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    public void setFkFactoringBankId_n(int n) { mnFkFactoringBankId_n = n; }
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
    public java.lang.String getTaxRegimeCode() { return msTaxRegimeCode; }
    public java.lang.String getConfirmationNum() { return msConfirmationNum; }
    public java.lang.String getCfdiRelationCode() { return msCfdiRelationCode; }
    public java.lang.String getCfdiRelatedUuid() { return msCfdiRelatedUuid; }
    public double getPaymentLoc_r() { return mdPaymentLoc_r; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkReceiptStatusId() { return mnFkReceiptStatusId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    public int getFkFactoringBankId_n() { return mnFkFactoringBankId_n; }
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
    
    public void computePaymentLoc() {
        mdPaymentLoc_r = 0;
        
        for (SDataReceiptPaymentPay pay : maDbmsReceiptPaymentPays) {
            mdPaymentLoc_r = SLibUtils.roundAmount(mdPaymentLoc_r + pay.getPaymentLoc());
        }
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
        msTaxRegimeCode = "";
        msConfirmationNum = "";
        msCfdiRelationCode = "";
        msCfdiRelatedUuid = "";
        mdPaymentLoc_r = 0;
        mbIsDeleted = false;
        mnFkReceiptStatusId = 0;
        mnFkCompanyBranchId = 0;
        mnFkBizPartnerId = 0;
        mnFkFactoringBankId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
        
        moDbmsFactoringBank_n = null;
        
        maDbmsReceiptPaymentPays.clear();
        
        moXtaRecordsMap.clear();
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
                    throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
                }
                else {
                    mnPkReceiptId = resultSet.getInt("id_rcp");
                    msSeries = resultSet.getString("ser");
                    mnNumber = resultSet.getInt("num");
                    mtDatetime = resultSet.getTimestamp("dt");
                    msTaxRegimeCode = resultSet.getString("tax_regime_code");
                    msConfirmationNum = resultSet.getString("conf_num");
                    msCfdiRelationCode = resultSet.getString("cfdi_relation_code");
                    msCfdiRelatedUuid = resultSet.getString("cfdi_related_uuid");
                    mdPaymentLoc_r = resultSet.getDouble("pay_loc_r");
                    mbIsDeleted = resultSet.getBoolean("b_del");
                    mnFkReceiptStatusId = resultSet.getInt("fid_st_rcp");
                    mnFkCompanyBranchId = resultSet.getInt("fid_cob");
                    mnFkBizPartnerId = resultSet.getInt("fid_bp");
                    mnFkFactoringBankId_n = resultSet.getInt("fid_fact_bank_n");
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
                            pay.read(new int[] { mnPkReceiptId, resultSetPays.getInt("id_pay") }, statement);
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
                            "'" + msTaxRegimeCode + "', " +
                            "'" + msConfirmationNum + "', " +
                            "'" + msCfdiRelationCode + "', " +
                            "'" + msCfdiRelatedUuid + "', " +
                            mdPaymentLoc_r + ", " +
                            mbIsDeleted + ", " +
                            mnFkReceiptStatusId + ", " +
                            mnFkCompanyBranchId + ", " +
                            mnFkBizPartnerId + ", " +
                            (mnFkFactoringBankId_n != 0 ? mnFkFactoringBankId_n : "NULL") + ", " +
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
                            "tax_regime_code = '" + msTaxRegimeCode + "', " +
                            "conf_num = '" + msConfirmationNum + "', " +
                            "cfdi_relation_code = '" + msCfdiRelationCode + "', " +
                            "cfdi_related_uuid = '" + msCfdiRelatedUuid + "', " +
                            "pay_loc_r=" + mdPaymentLoc_r + ", " +
                            "b_del = " + (mbIsDeleted ? 1 : 0) + ", " +
                            "fid_st_rcp = " + mnFkReceiptStatusId + ", " +
                            "fid_cob = " + mnFkCompanyBranchId + ", " +
                            "fid_bp = " + mnFkBizPartnerId + ", " +
                            "fid_fact_bank_n = " + mnFkFactoringBankId_n + ", " +
                            "fid_usr_new = " + mnFkUserNewId + ", " +
                            "fid_usr_edit = " + mnFkUserEditId + ", " +
                            "fid_usr_del = " + mnFkUserDeleteId + ", " +
                            //"ts_new = NOW(), " +
                            "ts_edit = NOW(), " +
                            (mbIsDeleted ? "ts_del = NOW(), " : "") +
                            "WHERE id_rcp = " + mnPkReceiptId + ";";
                    
                            mtUserEditTs = mtUserNewTs;
                            if (mbIsDeleted) {
                                mtUserDeleteTs = mtUserNewTs;
                            }
                }
                
                statement.execute(sql);
                
                // save as well all documents:
                
                if (!mbIsRegistryNew) {
                    sql = "DELETE FROM trn_pay_pay_doc "
                            + "WHERE id_rcp = " + mnPkReceiptId + ";";
                    
                    statement.execute(sql);
                    
                    sql = "DELETE FROM trn_pay_pay "
                            + "WHERE id_rcp = " + mnPkReceiptId + ";";
                    
                    statement.execute(sql);
                }
                
                for (SDataReceiptPaymentPay pay : maDbmsReceiptPaymentPays) {
                    pay.setPkReceiptId(mnPkReceiptId);
                    pay.setPkPaymentId(0);
                    pay.save(connection);
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
    public erp.mtrn.data.cfd.SDataReceiptPayment clone() throws CloneNotSupportedException {
        SDataReceiptPayment clone = new SDataReceiptPayment();

        clone.setIsRegistryNew(mbIsRegistryNew);

        clone.setPkReceiptId(mnPkReceiptId);
        clone.setSeries(msSeries);
        clone.setNumber(mnNumber);
        clone.setDatetime((Date) mtDatetime.clone());
        clone.setTaxRegimeCode(msTaxRegimeCode);
        clone.setConfirmationNum(msConfirmationNum);
        clone.setCfdiRelationCode(msCfdiRelationCode);
        clone.setCfdiRelatedUuid(msCfdiRelatedUuid);
        clone.setPaymentLoc_r(mdPaymentLoc_r);
        clone.setIsDeleted(mbIsDeleted);
        clone.setFkReceiptStatusId(mnFkReceiptStatusId);
        clone.setFkCompanyBranchId(mnFkCompanyBranchId);
        clone.setFkBizPartnerId(mnFkBizPartnerId);
        clone.setFkFactoringBankId_n(mnFkFactoringBankId_n);
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
        msTaxRegimeCode = cfdPayment.getAuxCfdEmisorRegimenFiscal();
        msConfirmationNum = cfdPayment.getAuxCfdConfirmacion();
        msCfdiRelationCode = cfdPayment.getAuxCfdCfdiRelacionadosTipoRelacion();
        msCfdiRelatedUuid = cfdPayment.getAuxCfdCfdiRelacionadoUuid();
        mbIsDeleted = false;
        mnFkReceiptStatusId = SLibUtilities.belongsTo(cfd.getFkXmlStatusId(), new int[] { SDataConstantsSys.TRNS_ST_DPS_NEW, SDataConstantsSys.TRNS_ST_DPS_EMITED }) ? SDataConstantsSys.TRNS_ST_DPS_EMITED : cfd.getFkXmlStatusId();
        mnFkCompanyBranchId = cfd.getFkCompanyBranchId_n();
        mnFkBizPartnerId = cfdPayment.getAuxCfdDbmsDataReceptor().getPkBizPartnerId();
        //mnFkFactoringBankId_n = ...; // set in setDbmsFactoringBank_n()
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
    }
}
