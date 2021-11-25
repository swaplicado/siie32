/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data.cfd;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mfin.data.SDataRecord;
import erp.mtrn.data.SDataCfdPayment;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;

/**
 * @author Sergio Flores
 */
public class SDataReceiptPaymentPay extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected SDataReceiptPayment moParentReceiptPayment;
    
    protected int mnPkReceiptId;
    protected int mnPkPaymentId;
    protected int mnEntryType;
    protected java.util.Date mtDatetime;
    protected java.lang.String msPaymentWayCode;
    protected java.lang.String msPaymentCurrencyCode;
    protected double mdPaymentCcy;
    protected double mdExchangeRate;
    protected double mdPaymentLoc;
    protected java.lang.String msOperationNum;
    protected java.lang.String msPayerBankName;
    protected java.lang.String msPayerBankFiscalId;
    protected java.lang.String msPayerBankAccount;
    protected java.lang.String msPayeeBankFiscalId;
    protected java.lang.String msPayeeBankAccount;
    protected int mnFkPaymentCurrencyId;
    protected int mnFkBankPayeeCompanyBranchId_n;
    protected int mnFkBankPayeeAccountCashId_n;
    protected int mnFkFinRecordYearId;
    protected int mnFkFinRecordPeriodId;
    protected int mnFkFinRecordBookkeepingCenterId;
    protected java.lang.String msFkFinRecordRecordTypeId;
    protected int mnFkFinRecordNumberId;
    
    protected SDataRecord moDbmsRecord;
    
    protected ArrayList<SDataReceiptPaymentPayDoc> maDbmsReceiptPaymentPayDocs;
    
    /**
     * Creates a new payment of a payment receipt.
     * @param parentReceiptPayment Parent payment receipt.
     */
    public SDataReceiptPaymentPay(SDataReceiptPayment parentReceiptPayment) {
        super(SDataConstants.TRN_PAY_PAY);

        moParentReceiptPayment = parentReceiptPayment;

        maDbmsReceiptPaymentPayDocs = new ArrayList<>();
        
        reset();
    }

    public void setPkReceiptId(int n) { mnPkReceiptId = n; }
    public void setPkPaymentId(int n) { mnPkPaymentId = n; }
    public void setEntryType(int n) { mnEntryType = n; }
    public void setDatetime(java.util.Date t) { mtDatetime = t; }
    public void setPaymentWayCode(java.lang.String s) { msPaymentWayCode = s; }
    public void setPaymentCurrencyCode(java.lang.String s) { msPaymentCurrencyCode = s; }
    public void setPaymentCcy(double d) { mdPaymentCcy = d; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setPaymentLoc(double d) { mdPaymentLoc = d; }
    public void setOperationNum(java.lang.String s) { msOperationNum = s; }
    public void setPayerBankName(java.lang.String s) { msPayerBankName = s; }
    public void setPayerBankFiscalId(java.lang.String s) { msPayerBankFiscalId = s; }
    public void setPayerBankAccount(java.lang.String s) { msPayerBankAccount = s; }
    public void setPayeeBankFiscalId(java.lang.String s) { msPayeeBankFiscalId = s; }
    public void setPayeeBankAccount(java.lang.String s) { msPayeeBankAccount = s; }
    public void setFkPaymentCurrencyId(int n) { mnFkPaymentCurrencyId = n; }
    public void setFkBankPayeeCompanyBranchId_n(int n) { mnFkBankPayeeCompanyBranchId_n = n; }
    public void setFkBankPayeeAccountCashId_n(int n) { mnFkBankPayeeAccountCashId_n = n; }
    public void setFkFinRecordYearId(int n) { mnFkFinRecordYearId = n; }
    public void setFkFinRecordPeriodId(int n) { mnFkFinRecordPeriodId = n; }
    public void setFkFinRecordBookkeepingCenterId(int n) { mnFkFinRecordBookkeepingCenterId = n; }
    public void setFkFinRecordRecordTypeId(java.lang.String s) { msFkFinRecordRecordTypeId = s; }
    public void setFkFinRecordNumberId(int n) { mnFkFinRecordNumberId = n; }

    public int getPkReceiptId() { return mnPkReceiptId; }
    public int getPkPaymentId() { return mnPkPaymentId; }
    public int getEntryType() { return mnEntryType; }
    public java.util.Date getDatetime() { return mtDatetime; }
    public java.lang.String getPaymentWayCode() { return msPaymentWayCode; }
    public java.lang.String getPaymentCurrencyCode() { return msPaymentCurrencyCode; }
    public double getPaymentCcy() { return mdPaymentCcy; }
    public double getExchangeRate() { return mdExchangeRate; }
    public double getPaymentLoc() { return mdPaymentLoc; }
    public java.lang.String getOperationNum() { return msOperationNum; }
    public java.lang.String getPayerBankName() { return msPayerBankName; }
    public java.lang.String getPayerBankFiscalId() { return msPayerBankFiscalId; }
    public java.lang.String getPayerBankAccount() { return msPayerBankAccount; }
    public java.lang.String getPayeeBankFiscalId() { return msPayeeBankFiscalId; }
    public java.lang.String getPayeeBankAccount() { return msPayeeBankAccount; }
    public int getFkPaymentCurrencyId() { return mnFkPaymentCurrencyId; }
    public int getFkBankPayeeCompanyBranchId_n() { return mnFkBankPayeeCompanyBranchId_n; }
    public int getFkBankPayeeAccountCashId_n() { return mnFkBankPayeeAccountCashId_n; }
    public int getFkFinRecordYearId() { return mnFkFinRecordYearId; }
    public int getFkFinRecordPeriodId() { return mnFkFinRecordPeriodId; }
    public int getFkFinRecordBookkeepingCenterId() { return mnFkFinRecordBookkeepingCenterId; }
    public java.lang.String getFkFinRecordRecordTypeId() { return msFkFinRecordRecordTypeId; }
    public int getFkFinRecordNumberId() { return mnFkFinRecordNumberId; }
    
    public void setDbmsRecord(SDataRecord o) {
        moDbmsRecord = o;
        
        if (moDbmsRecord == null) {
            mnFkFinRecordYearId = 0;
            mnFkFinRecordPeriodId = 0;
            mnFkFinRecordBookkeepingCenterId = 0;
            msFkFinRecordRecordTypeId = "";
            mnFkFinRecordNumberId = 0;
        }
        else {
            mnFkFinRecordYearId = moDbmsRecord.getPkYearId();
            mnFkFinRecordPeriodId = moDbmsRecord.getPkPeriodId();
            mnFkFinRecordBookkeepingCenterId = moDbmsRecord.getPkBookkeepingCenterId();
            msFkFinRecordRecordTypeId = moDbmsRecord.getPkRecordTypeId();
            mnFkFinRecordNumberId = moDbmsRecord.getPkNumberId();
        }
    }
    
    public SDataRecord getDbmsRecord() { return moDbmsRecord; }

    protected ArrayList<SDataReceiptPaymentPayDoc> getDbmsReceiptPaymentPayDocs() { return maDbmsReceiptPaymentPayDocs; }
    
    public int[] getBankPayeeKey() { return new int[] { mnFkBankPayeeCompanyBranchId_n, mnFkBankPayeeAccountCashId_n }; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkReceiptId = ((int[]) pk)[0];
        mnPkPaymentId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkReceiptId, mnPkPaymentId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkReceiptId = 0;
        mnPkPaymentId = 0;
        mnEntryType = 0;
        mtDatetime = null;
        msPaymentWayCode = "";
        msPaymentCurrencyCode = "";
        mdPaymentCcy = 0;
        mdExchangeRate = 0;
        mdPaymentLoc = 0;
        msOperationNum = "";
        msPayerBankName = "";
        msPayerBankFiscalId = "";
        msPayerBankAccount = "";
        msPayeeBankFiscalId = "";
        msPayeeBankAccount = "";
        mnFkPaymentCurrencyId = 0;
        mnFkBankPayeeCompanyBranchId_n = 0;
        mnFkBankPayeeAccountCashId_n = 0;
        mnFkFinRecordYearId = 0;
        mnFkFinRecordPeriodId = 0;
        mnFkFinRecordBookkeepingCenterId = 0;
        msFkFinRecordRecordTypeId = "";
        mnFkFinRecordNumberId = 0;
        
        moDbmsRecord = null;
        
        maDbmsReceiptPaymentPayDocs.clear();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            int[] key = (int[]) pk;
            String sql = "SELECT * "
                    + "FROM trn_pay_pay "
                    + "WHERE id_rcp = " + key[0] + " AND id_pay = " + key[1] + ";";
            
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
                }
                else {
                    mnPkReceiptId = resultSet.getInt("id_rcp");
                    mnPkPaymentId = resultSet.getInt("id_pay");
                    mnEntryType = resultSet.getInt("entry_tp");
                    mtDatetime = resultSet.getTimestamp("dt");
                    msPaymentWayCode = resultSet.getString("pay_way_code");
                    msPaymentCurrencyCode = resultSet.getString("pay_cur_code");
                    mdPaymentCcy = resultSet.getDouble("pay_cur");
                    mdExchangeRate = resultSet.getDouble("exc_rate");
                    mdPaymentLoc = resultSet.getDouble("pay_loc");
                    msOperationNum = resultSet.getString("oper_num");
                    msPayerBankName = resultSet.getString("payer_bank_name");
                    msPayerBankFiscalId = resultSet.getString("payer_bank_fiscal_id");
                    msPayerBankAccount = resultSet.getString("payer_bank_acc");
                    msPayeeBankFiscalId = resultSet.getString("payee_bank_fiscal_id");
                    msPayeeBankAccount = resultSet.getString("payee_bank_acc");
                    mnFkPaymentCurrencyId = resultSet.getInt("fid_pay_cur");
                    mnFkBankPayeeCompanyBranchId_n = resultSet.getInt("fid_bank_payee_cob_n");
                    mnFkBankPayeeAccountCashId_n = resultSet.getInt("fid_bank_payee_acc_cash_n");
                    mnFkFinRecordYearId = resultSet.getInt("fid_fin_rec_year");
                    mnFkFinRecordPeriodId = resultSet.getInt("fid_fin_rec_per");
                    mnFkFinRecordBookkeepingCenterId = resultSet.getInt("fid_fin_rec_bkc");
                    msFkFinRecordRecordTypeId = resultSet.getString("fid_fin_rec_tp_rec");
                    mnFkFinRecordNumberId = resultSet.getInt("fid_fin_rec_num");
                    
                    // read as well financial record:
                    
                    String recordKey = SDataRecord.getRecordPrimaryKey(mnFkFinRecordYearId, mnFkFinRecordPeriodId, mnFkFinRecordBookkeepingCenterId, msFkFinRecordRecordTypeId, mnFkFinRecordNumberId);
                    moDbmsRecord = moParentReceiptPayment.getXtaRecordsMap().get(recordKey);

                    if (moDbmsRecord == null) {
                        // financial record has not been read yet:
                        moDbmsRecord = new SDataRecord();
                        moDbmsRecord.read(new Object[] { mnFkFinRecordYearId, mnFkFinRecordPeriodId, mnFkFinRecordBookkeepingCenterId, msFkFinRecordRecordTypeId, mnFkFinRecordNumberId }, statement);
                        moParentReceiptPayment.getXtaRecordsMap().put(recordKey, moDbmsRecord);
                    }
                    
                    // read as well all documents:
                    
                    sql = "SELECT id_doc "
                            + "FROM trn_pay_pay_doc "
                            + "WHERE id_rcp = " + key[0] + " AND id_pay = " + key[1] + " "
                            + "ORDER BY id_doc;";
                    
                    try (Statement statementDocs = statement.getConnection().createStatement()) {
                        ResultSet resultSetDocs = statementDocs.executeQuery(sql);
                        while (resultSetDocs.next()) {
                            SDataReceiptPaymentPayDoc payDoc = new SDataReceiptPaymentPayDoc();
                            payDoc.read(new int[] { mnPkReceiptId, mnPkPaymentId, resultSetDocs.getInt("id_doc") }, statement);
                            maDbmsReceiptPaymentPayDocs.add(payDoc);
                        }
                    }
                    
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
                
                if (mnPkPaymentId == 0) {
                    sql = "SELECT COALESCE(MAX(id_pay), 0) + 1 "
                            + "FROM trn_pay_pay "
                            + "WHERE id_rcp = " + mnPkReceiptId + ";";
                    
                    ResultSet resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        mnPkPaymentId = resultSet.getInt(1);
                    }
                    
                    sql = "INSERT INTO trn_pay_pay VALUES (" +
                            mnPkReceiptId + ", " +
                            mnPkPaymentId + ", " +
                            mnEntryType + ", " +
                            "'" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetime) + "', " +
                            "'" + msPaymentWayCode + "', " +
                            "'" + msPaymentCurrencyCode + "', " +
                            mdPaymentCcy + ", " +
                            mdExchangeRate + ", " +
                            mdPaymentLoc + ", " +
                            "'" + msOperationNum + "', " +
                            "'" + msPayerBankName + "', " +
                            "'" + msPayerBankFiscalId + "', " +
                            "'" + msPayerBankAccount + "', " +
                            "'" + msPayeeBankFiscalId + "', " +
                            "'" + msPayeeBankAccount + "', " +
                            mnFkPaymentCurrencyId + ", " +
                            (mnFkBankPayeeCompanyBranchId_n == 0 ? "NULL" : mnFkBankPayeeCompanyBranchId_n) + ", " +
                            (mnFkBankPayeeAccountCashId_n == 0 ? "NULL" : mnFkBankPayeeAccountCashId_n) + ", " +
                            mnFkFinRecordYearId + ", " +
                            mnFkFinRecordPeriodId + ", " +
                            mnFkFinRecordBookkeepingCenterId + ", " +
                            "'" + msFkFinRecordRecordTypeId + "', " +
                            mnFkFinRecordNumberId +
                            ");";
                }
                else {
                    sql = "UPDATE trn_pay_pay SET " +
                            //"id_rcp = " + mnPkReceiptId + ", " +
                            //"id_pay = " + mnPkPaymentId + ", " +
                            "entry_tp = " + mnEntryType + ", " +
                            "dt = '" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetime) + "', " +
                            "pay_way_code = '" + msPaymentWayCode + "', " +
                            "pay_cur_code = '" + msPaymentCurrencyCode + "', " +
                            "pay_cur = " + mdPaymentCcy + ", " +
                            "exc_rate = " + mdExchangeRate + ", " +
                            "pay_loc = " + mdPaymentLoc + ", " +
                            "oper_num = '" + msOperationNum + "', " +
                            "payer_bank_name = '" + msPayerBankName + "', " +
                            "payer_bank_fiscal_id = '" + msPayerBankFiscalId + "', " +
                            "payer_bank_acc = '" + msPayerBankAccount + "', " +
                            "payee_bank_fiscal_id = '" + msPayeeBankFiscalId + "', " +
                            "payee_bank_acc = '" + msPayeeBankAccount + "', " +
                            "fid_pay_cur = " + mnFkPaymentCurrencyId + ", " +
                            "fid_bank_payee_cob_n = " + (mnFkBankPayeeCompanyBranchId_n == 0 ? "NULL" : mnFkBankPayeeCompanyBranchId_n) + ", " +
                            "fid_bank_payee_acc_cash_n = " + (mnFkBankPayeeAccountCashId_n == 0 ? "NULL" : mnFkBankPayeeAccountCashId_n) + ", " +
                            "fid_fin_rec_year = " + mnFkFinRecordYearId + ", " +
                            "fid_fin_rec_per = " + mnFkFinRecordPeriodId + ", " +
                            "fid_fin_rec_bkc = " + mnFkFinRecordBookkeepingCenterId + ", " +
                            "fid_fin_rec_tp_rec = '" + msFkFinRecordRecordTypeId + "', " +
                            "fid_fin_rec_num = " + mnFkFinRecordNumberId + ", " +
                            "WHERE id_rcp = " + mnPkReceiptId + " AND id_pay = " + mnPkPaymentId + ";";
                }
                
                statement.execute(sql);
                
                // save as well all documents:
                
                if (!mbIsRegistryNew) {
                    sql = "DELETE FROM trn_pay_pay_doc "
                            + "WHERE id_rcp = " + mnPkReceiptId + " AND id_pay = " + mnPkPaymentId + ";";
                    
                    statement.execute(sql);
                }
                
                for (SDataReceiptPaymentPayDoc payDoc : maDbmsReceiptPaymentPayDocs) {
                    payDoc.setPkReceiptId(mnPkReceiptId);
                    payDoc.setPkPaymentId(mnPkPaymentId);
                    payDoc.setPkDocumentId(0);
                    if (payDoc.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP + "\nTipo de registro: Documento relacionado.");
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
    public erp.mtrn.data.cfd.SDataReceiptPaymentPay clone() throws CloneNotSupportedException {
        SDataReceiptPaymentPay clone = new SDataReceiptPaymentPay(moParentReceiptPayment);

        clone.setIsRegistryNew(mbIsRegistryNew);

        clone.setPkReceiptId(mnPkReceiptId);
        clone.setPkPaymentId(mnPkPaymentId);
        clone.setEntryType(mnEntryType);
        clone.setDatetime((Date) mtDatetime.clone());
        clone.setPaymentWayCode(msPaymentWayCode);
        clone.setPaymentCurrencyCode(msPaymentCurrencyCode);
        clone.setPaymentCcy(mdPaymentCcy);
        clone.setExchangeRate(mdExchangeRate);
        clone.setPaymentLoc(mdPaymentLoc);
        clone.setOperationNum(msOperationNum);
        clone.setPayerBankName(msPayerBankName);
        clone.setPayerBankFiscalId(msPayerBankFiscalId);
        clone.setPayerBankAccount(msPayerBankAccount);
        clone.setPayeeBankFiscalId(msPayeeBankFiscalId);
        clone.setPayeeBankAccount(msPayeeBankAccount);
        clone.setFkPaymentCurrencyId(mnFkPaymentCurrencyId);
        clone.setFkBankPayeeCompanyBranchId_n(mnFkBankPayeeCompanyBranchId_n);
        clone.setFkBankPayeeAccountCashId_n(mnFkBankPayeeAccountCashId_n);
        clone.setFkFinRecordYearId(mnFkFinRecordYearId);
        clone.setFkFinRecordPeriodId(mnFkFinRecordPeriodId);
        clone.setFkFinRecordBookkeepingCenterId(mnFkFinRecordBookkeepingCenterId);
        clone.setFkFinRecordRecordTypeId(msFkFinRecordRecordTypeId);
        clone.setFkFinRecordNumberId(mnFkFinRecordNumberId);
        
        clone.setDbmsRecord(moDbmsRecord);
        
        for (SDataReceiptPaymentPayDoc payDoc : maDbmsReceiptPaymentPayDocs) {
            clone.getDbmsReceiptPaymentPayDocs().add(payDoc.clone());
        }

        return clone;
    }
    
    public SCfdPaymentEntry createCfdPaymentEntry(final SDataCfdPayment payment, final Statement statement) {
        SCfdPaymentEntry paymentEntry = new SCfdPaymentEntry(payment, mnPkPaymentId, mnEntryType, mtDatetime, msPaymentWayCode, mnFkPaymentCurrencyId, msPaymentCurrencyCode, mdPaymentCcy, mdExchangeRate, moDbmsRecord);
        
        paymentEntry.Operation = msOperationNum;
        paymentEntry.AccountSrcFiscalId = msPayerBankFiscalId;
        paymentEntry.AccountSrcNumber = msPayerBankAccount;
        paymentEntry.AccountSrcEntity = msPayerBankName;
        paymentEntry.AccountDestFiscalId = msPayeeBankFiscalId;
        paymentEntry.AccountDestNumber = msPayeeBankAccount;
        paymentEntry.AccountDestKey = getBankPayeeKey();

        if (moParentReceiptPayment.getDbmsFactoringBank_n() != null) {
            paymentEntry.AuxFactoringBankId = moParentReceiptPayment.getDbmsFactoringBank_n().getPkBizPartnerId();
            paymentEntry.AuxFactoringBankFiscalId = moParentReceiptPayment.getDbmsFactoringBank_n().getFiscalId();
        }
                        
        for (SDataReceiptPaymentPayDoc payDoc : maDbmsReceiptPaymentPayDocs) {
            SCfdPaymentEntryDoc paymentEntryDoc = payDoc.createCfdPaymentEntryDoc(paymentEntry, statement);
            paymentEntryDoc.prepareTableRow();
            paymentEntry.PaymentEntryDocs.add(paymentEntryDoc);
        }
        
        paymentEntry.computeTotalPayments();
        paymentEntry.prepareTableRow();
        
        return paymentEntry;
    }
    
    public void harvestCfdPaymentEntry(final SCfdPaymentEntry paymentEntry) {
        //mnPkReceiptId = ...;
        //mnPkPaymentId = ...;
        mnEntryType = paymentEntry.EntryType;
        mtDatetime = paymentEntry.PaymentDate;
        msPaymentWayCode = paymentEntry.PaymentWay;
        msPaymentCurrencyCode = paymentEntry.CurrencyKey;
        mdPaymentCcy = paymentEntry.Amount;
        mdExchangeRate = paymentEntry.ExchangeRate;
        mdPaymentLoc = paymentEntry.AmountLocal;
        msOperationNum = paymentEntry.Operation;
        msPayerBankName = paymentEntry.AccountSrcEntity;
        msPayerBankFiscalId = paymentEntry.AccountSrcFiscalId;
        msPayerBankAccount = paymentEntry.AccountSrcNumber;
        msPayeeBankFiscalId = paymentEntry.AccountDestFiscalId;
        msPayeeBankAccount = paymentEntry.AccountDestNumber;
        mnFkPaymentCurrencyId = paymentEntry.CurrencyId;
        mnFkBankPayeeCompanyBranchId_n = paymentEntry.AccountDestKey == null ? 0 : paymentEntry.AccountDestKey[0];
        mnFkBankPayeeAccountCashId_n = paymentEntry.AccountDestKey == null ? 0 : paymentEntry.AccountDestKey[1];
        //mnFkFinRecordYearId = ...; // set in setDbmsRecord()
        //mnFkFinRecordPeriodId = ...; // set in setDbmsRecord()
        //mnFkFinRecordBookkeepingCenterId = ...; // set in setDbmsRecord()
        //msFkFinRecordRecordTypeId = ...; // set in setDbmsRecord()
        //mnFkFinRecordNumberId = ...; // set in setDbmsRecord()
        
        setDbmsRecord(paymentEntry.DataRecord);
        
        maDbmsReceiptPaymentPayDocs.clear();
        
        for (SCfdPaymentEntryDoc paymentEntryDoc : paymentEntry.PaymentEntryDocs) {
            SDataReceiptPaymentPayDoc payDoc = new SDataReceiptPaymentPayDoc();
            payDoc.harvestCfdPaymentEntryDoc(paymentEntryDoc);
            maDbmsReceiptPaymentPayDocs.add(payDoc);
        }
    }
}
