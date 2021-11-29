/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data.cfd;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mtrn.data.SDataDps;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Sergio Flores
 */
public class SDataReceiptPaymentPayDoc extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkReceiptId;
    protected int mnPkPaymentId;
    protected int mnPkDocumentId;
    protected int mnEntryType;
    protected int mnInstallment;
    protected double mdDocBalancePrevCcy;
    protected double mdPaymentCcy;
    protected double mdDocBalanceUnpaidCcy_r;
    protected double mdExchangeRate;
    protected double mdDocBalancePrevPay;
    protected double mdPaymentPay;
    protected double mdDocBalanceUnpaidPay_r;
    protected double mdPaymentLoc;
    protected int mnFkDocYearId;
    protected int mnFkDocDocId;
    protected int mnFkDocCurrencyId;
    
    protected SDataDps moDbmsDps;

    /**
     * Creates a new document of a payment of a payment receipt.
     */
    public SDataReceiptPaymentPayDoc() {
        super(SDataConstants.TRN_PAY_PAY_DOC);
        reset();
    }

    public void setPkReceiptId(int n) { mnPkReceiptId = n; }
    public void setPkPaymentId(int n) { mnPkPaymentId = n; }
    public void setPkDocumentId(int n) { mnPkDocumentId = n; }
    public void setEntryType(int n) { mnEntryType = n; }
    public void setInstallment(int n) { mnInstallment = n; }
    public void setDocBalancePrevCcy(double d) { mdDocBalancePrevCcy = d; }
    public void setPaymentCcy(double d) { mdPaymentCcy = d; }
    public void setDocBalanceUnpaidCcy_r(double d) { mdDocBalanceUnpaidCcy_r = d; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setDocBalancePrevPay(double d) { mdDocBalancePrevPay = d; }
    public void setPaymentPay(double d) { mdPaymentPay = d; }
    public void setDocBalanceUnpaidPay_r(double d) { mdDocBalanceUnpaidPay_r = d; }
    public void setPaymentLoc(double d) { mdPaymentLoc = d; }
    public void setFkDocYearId(int n) { mnFkDocYearId = n; }
    public void setFkDocDocId(int n) { mnFkDocDocId = n; }
    public void setFkDocCurrencyId(int n) { mnFkDocCurrencyId = n; }

    public int getPkReceiptId() { return mnPkReceiptId; }
    public int getPkPaymentId() { return mnPkPaymentId; }
    public int getPkDocumentId() { return mnPkDocumentId; }
    public int getEntryType() { return mnEntryType; }
    public int getInstallment() { return mnInstallment; }
    public double getDocBalancePrevCcy() { return mdDocBalancePrevCcy; }
    public double getPaymentCcy() { return mdPaymentCcy; }
    public double getDocBalanceUnpaidCcy_r() { return mdDocBalanceUnpaidCcy_r; }
    public double getExchangeRate() { return mdExchangeRate; }
    public double getDocBalancePrevPay() { return mdDocBalancePrevPay; }
    public double getPaymentPay() { return mdPaymentPay; }
    public double getDocBalanceUnpaidPay_r() { return mdDocBalanceUnpaidPay_r; }
    public double getPaymentLoc() { return mdPaymentLoc; }
    public int getFkDocYearId() { return mnFkDocYearId; }
    public int getFkDocDocId() { return mnFkDocDocId; }
    public int getFkDocCurrencyId() { return mnFkDocCurrencyId; }

    public void setDbmsDps(SDataDps o) {
        moDbmsDps = o;
        
        if (moDbmsDps == null) {
            mnFkDocYearId = 0;
            mnFkDocDocId = 0;
            mnFkDocCurrencyId = 0;
        }
        else {
            mnFkDocYearId = moDbmsDps.getPkYearId();
            mnFkDocDocId = moDbmsDps.getPkDocId();
            mnFkDocCurrencyId = moDbmsDps.getFkCurrencyId();
        }
    }

    public SDataDps getDbmsDps() { return moDbmsDps; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkReceiptId = ((int[]) pk)[0];
        mnPkPaymentId = ((int[]) pk)[1];
        mnPkDocumentId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkReceiptId, mnPkPaymentId, mnPkDocumentId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkReceiptId = 0;
        mnPkPaymentId = 0;
        mnPkDocumentId = 0;
        mnEntryType = 0;
        mnInstallment = 0;
        mdDocBalancePrevCcy = 0;
        mdPaymentCcy = 0;
        mdDocBalanceUnpaidCcy_r = 0;
        mdExchangeRate = 0;
        mdDocBalancePrevPay = 0;
        mdPaymentPay = 0;
        mdDocBalanceUnpaidPay_r = 0;
        mdPaymentLoc = 0;
        mnFkDocYearId = 0;
        mnFkDocDocId = 0;
        mnFkDocCurrencyId = 0;
        
        moDbmsDps = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            int[] key = (int[]) pk;
            String sql = "SELECT * "
                    + "FROM trn_pay_pay_doc "
                    + "WHERE id_rcp = " + key[0] + " AND id_pay = " + key[1] + " AND id_doc = " + key[2] + ";";
            
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
                }
                else {
                    mnPkReceiptId = resultSet.getInt("id_rcp");
                    mnPkPaymentId = resultSet.getInt("id_pay");
                    mnPkDocumentId = resultSet.getInt("id_doc");
                    mnEntryType = resultSet.getInt("entry_tp");
                    mnInstallment = resultSet.getInt("install");
                    mdDocBalancePrevCcy = resultSet.getDouble("doc_bal_prev_cur");
                    mdPaymentCcy = resultSet.getDouble("pay_cur");
                    mdDocBalanceUnpaidCcy_r = resultSet.getDouble("doc_bal_unpd_cur_r");
                    mdExchangeRate = resultSet.getDouble("exc_rate");
                    mdDocBalancePrevPay = resultSet.getDouble("doc_bal_prev_pay");
                    mdPaymentPay = resultSet.getDouble("pay_pay");
                    mdDocBalanceUnpaidPay_r = resultSet.getDouble("doc_bal_unpd_pay_r");
                    mdPaymentLoc = resultSet.getDouble("pay_loc");
                    mnFkDocYearId = resultSet.getInt("fid_doc_year");
                    mnFkDocDocId = resultSet.getInt("fid_doc_doc");
                    mnFkDocCurrencyId = resultSet.getInt("fid_doc_cur");
                    
                    // read as well document:
                    
                    moDbmsDps = new SDataDps();
                    moDbmsDps.read(new int[] { mnFkDocYearId, mnFkDocDocId }, statement);
                    
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
                
                if (mnPkDocumentId == 0) {
                    sql = "SELECT COALESCE(MAX(id_doc), 0) + 1 "
                            + "FROM trn_pay_pay_doc "
                            + "WHERE id_rcp = " + mnPkReceiptId + " AND id_pay = " + mnPkPaymentId + ";";
                    
                    ResultSet resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        mnPkDocumentId = resultSet.getInt(1);
                    }
                    
                    sql = "INSERT INTO trn_pay_pay_doc VALUES (" +
                            mnPkReceiptId + ", " +
                            mnPkPaymentId + ", " +
                            mnPkDocumentId + ", " +
                            mnEntryType + ", " +
                            mnInstallment + ", " +
                            mdDocBalancePrevCcy + ", " +
                            mdPaymentCcy + ", " +
                            mdDocBalanceUnpaidCcy_r + ", " +
                            mdExchangeRate + ", " +
                            mdDocBalancePrevPay + ", " +
                            mdPaymentPay + ", " +
                            mdDocBalanceUnpaidPay_r + ", " +
                            mdPaymentLoc + ", " +
                            mnFkDocYearId + ", " +
                            mnFkDocDocId + ", " +
                            mnFkDocCurrencyId +
                            ");";
                }
                else {
                    sql = "UPDATE trn_pay_pay_doc SET " +
                            //"id_rcp = " + mnPkReceiptId + ", " +
                            //"id_pay = " + mnPkPaymentId + ", " +
                            //"id_doc = " + mnPkDocumentId + ", " +
                            "entry_tp = " + mnEntryType + ", " +
                            "install = " + mnInstallment + ", " +
                            "doc_bal_prev_cur = " + mdDocBalancePrevCcy + ", " +
                            "pay_cur = " + mdPaymentCcy + ", " +
                            "doc_bal_unpd_cur_r = " + mdDocBalanceUnpaidCcy_r + ", " +
                            "exc_rate = " + mdExchangeRate + ", " +
                            "doc_bal_prev_pay = " + mdDocBalancePrevPay + ", " +
                            "pay_pay = " + mdPaymentPay + ", " +
                            "doc_bal_unpd_pay_r = " + mdDocBalanceUnpaidPay_r + ", " +
                            "pay_loc = " + mdPaymentLoc + ", " +
                            "fid_doc_year = " + mnFkDocYearId + ", " +
                            "fid_doc_doc = " + mnFkDocDocId + ", " +
                            "fid_doc_cur = " + mnFkDocCurrencyId + " " +
                            "WHERE id_rcp = " + mnPkReceiptId + " AND " +
                            "id_pay = " + mnPkPaymentId + " AND " +
                            "id_doc = " + mnPkDocumentId + ";";
                }
                
                statement.execute(sql);
                
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
    public erp.mtrn.data.cfd.SDataReceiptPaymentPayDoc clone() throws CloneNotSupportedException {
        SDataReceiptPaymentPayDoc clone = new SDataReceiptPaymentPayDoc();

        clone.setIsRegistryNew(mbIsRegistryNew);

        clone.setPkReceiptId(mnPkReceiptId);
        clone.setPkPaymentId(mnPkPaymentId);
        clone.setPkDocumentId(mnPkDocumentId);
        clone.setEntryType(mnEntryType);
        clone.setInstallment(mnInstallment);
        clone.setDocBalancePrevCcy(mdDocBalancePrevCcy);
        clone.setPaymentCcy(mdPaymentCcy);
        clone.setDocBalanceUnpaidCcy_r(mdDocBalanceUnpaidCcy_r);
        clone.setExchangeRate(mdExchangeRate);
        clone.setDocBalancePrevPay(mdDocBalancePrevPay);
        clone.setPaymentPay(mdPaymentPay);
        clone.setDocBalanceUnpaidPay_r(mdDocBalanceUnpaidPay_r);
        clone.setPaymentLoc(mdPaymentLoc);
        clone.setFkDocYearId(mnFkDocYearId);
        clone.setFkDocDocId(mnFkDocDocId);
        clone.setFkDocCurrencyId(mnFkDocCurrencyId);
        
        clone.setDbmsDps(moDbmsDps);

        return clone;
    }
    
    public SCfdPaymentEntryDoc createCfdPaymentEntryDoc(final SCfdPaymentEntry paymentEntry, final Statement statement) {
        SCfdPaymentEntryDoc paymentEntryDoc = new SCfdPaymentEntryDoc(paymentEntry, moDbmsDps, mnPkDocumentId, mnEntryType, mnInstallment, mdDocBalancePrevCcy, mdPaymentCcy, mdExchangeRate); // convenience variable
        return paymentEntryDoc;
    }
    
    public void harvestCfdPaymentEntryDoc(final SCfdPaymentEntryDoc paymentEntryDoc) {
        //mnPkReceiptId = ...;
        //mnPkPaymentId = ...;
        //mnPkDocumentId = ...;
        mnEntryType = paymentEntryDoc.EntryDocType;
        mnInstallment = paymentEntryDoc.Installment;
        mdDocBalancePrevCcy = paymentEntryDoc.DocBalancePrev;
        mdPaymentCcy = paymentEntryDoc.DocPayment;
        mdDocBalanceUnpaidCcy_r = paymentEntryDoc.DocBalancePend;
        mdExchangeRate = paymentEntryDoc.ExchangeRate;
        mdDocBalancePrevPay = paymentEntryDoc.PayBalancePrev;
        mdPaymentPay = paymentEntryDoc.PayPayment;
        mdDocBalanceUnpaidPay_r = paymentEntryDoc.PayBalancePend;
        mdPaymentLoc = paymentEntryDoc.PayPaymentLocal;
        //mnFkDocYearId = ...; // set in setDbmsDps()
        //mnFkDocDocId = ...; // set in setDbmsDps()
        //mnFkDocCurrencyId = ...; // set in setDbmsDps()
        
        setDbmsDps(paymentEntryDoc.DataDps);
    }
}
