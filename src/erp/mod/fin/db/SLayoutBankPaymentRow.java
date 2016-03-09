/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import java.util.Date;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Juan Barajas
 */
public class SLayoutBankPaymentRow implements SGridRow {

    protected SGuiClient miClient;
    
    protected int mnBizPartnerId;
    protected int mnBizPartnerBranchId;
    protected int mnBizPartnerBranchBankAccountId;
    protected String msBizPartner;
    protected String msBizPartnerKey;
    protected boolean mbIsForPayment;
    protected boolean mbIsToPayed;
    protected double mdBalance;
    protected double mdBalanceTot;
    protected String msCurrencyKey;
    protected String msAccountCredit;
    protected String msRecordPeriod;
    protected String msRecordBkc;
    protected String msRecordCob;
    protected String msRecordNumber;
    protected Date mtRecordDate;
    
    protected SLayoutBankPayment moLayoutBankPayment;
    protected SFinRecordLayout moFinRecordLayout;
    protected SFinRecordLayout moFinRecordLayoutOld;

    public SLayoutBankPaymentRow(SGuiClient client) {
        miClient = client;
        reset();
    }

    public void reset() {
        mnBizPartnerId = 0;
        mnBizPartnerBranchId = 0;
        mnBizPartnerBranchBankAccountId = 0;
        msBizPartner = "";
        msBizPartnerKey = "";
        mbIsForPayment = false;
        mbIsToPayed = false;
        mdBalance = 0;
        mdBalanceTot = 0;
        msCurrencyKey = "";
        msAccountCredit = "";
        msRecordPeriod = "";
        msRecordBkc = "";
        msRecordCob = "";
        msRecordNumber = "";
        mtRecordDate = null;
        
        moLayoutBankPayment = null;
        moFinRecordLayout = null;
        moFinRecordLayoutOld = null;
    }
    
    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartnerBranchId(int n) { mnBizPartnerBranchId = n; }
    public void setBizPartnerBranchAccountId(int n) { mnBizPartnerBranchBankAccountId = n; }
    public void setBizPartner(String s) { msBizPartner = s; }
    public void setBizPartnerKey(String s) { msBizPartnerKey = s; }
    public void setIsForPayment(boolean b) { mbIsForPayment = b; }
    public void setIsToPayed(boolean b) { mbIsToPayed = b; }
    public void setBalance(double d) { mdBalance = d; }
    public void setBalanceTot(double d) { mdBalanceTot = d; }
    public void setCurrencyKey(String s) { msCurrencyKey = s; }
    public void setAccountCredit(String s) { msAccountCredit = s; }
    public void setRecordPeriod(String s) { msRecordPeriod = s; }
    public void setRecordBkc(String s) { msRecordBkc = s; }
    public void setRecordCob(String s) { msRecordCob = s; }
    public void setRecordNumber(String s) { msRecordNumber = s; }
    public void setRecordDate(Date t) { mtRecordDate = t; }
    public void setLayoutBankPayment(SLayoutBankPayment o) { moLayoutBankPayment = o; }
    public void setFinRecordLayout(SFinRecordLayout o) { moFinRecordLayout = o; }
    public void setFinRecordLayoutOld(SFinRecordLayout o) { moFinRecordLayoutOld = o; }
    
    public void setPrimaryKey(int[] pk) {
        mnBizPartnerId = pk[0];
        mnBizPartnerBranchId = pk[1];
        mnBizPartnerBranchBankAccountId = pk[1];
    }
    
    public int getBizPartnerId() { return mnBizPartnerId; }
    public int getBizPartnerBranchId() { return mnBizPartnerBranchId; }
    public int getBizPartnerBranchAccountId() { return mnBizPartnerBranchBankAccountId; }
    public String getBizPartner() { return msBizPartner; }
    public String getBizPartnerKey() { return msBizPartnerKey; }
    public boolean getIsForPayment() { return mbIsForPayment; }
    public boolean getIsToPayed() { return mbIsToPayed; }
    public double getBalance() { return mdBalance; }
    public double getBalanceTot() { return mdBalanceTot; }
    public String getCurrencyKey() { return msCurrencyKey; }
    public String getAccountCredit() { return msAccountCredit; }
    public String getRecordPeriod() { return msRecordPeriod; }
    public String getRecordBkc() { return msRecordBkc; }
    public String getRecordCob() { return msRecordCob; }
    public String getRecordNumber() { return msRecordNumber; }
    public Date getRecordDate() { return mtRecordDate; }
    
    public SLayoutBankPayment getLayoutBankPayment() { return moLayoutBankPayment; }
    public SFinRecordLayout getFinRecordLayout() { return moFinRecordLayout; }
    public SFinRecordLayout getFinRecordLayoutOld() { return moFinRecordLayoutOld; }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnBizPartnerId, mnBizPartnerBranchId, mnBizPartnerBranchBankAccountId };
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = msBizPartner;
                break;
            case 1:
                value = msBizPartnerKey;
                break;
            case 2:
                value = mbIsForPayment;
                break;
            case 3:
                value = mdBalance;
                break;
            case 4:
                value = msCurrencyKey;
                break;
            case 5:
                value = msAccountCredit;
                break;
            case 6:
                value = msRecordPeriod;
                break;
            case 7:
                value = msRecordBkc;
                break;
            case 8:
                value = msRecordCob;
                break;
            case 9:
                value = msRecordNumber;
                break;
            case 10:
                value = mtRecordDate;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch (row) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                mbIsForPayment = (boolean) value;
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            default:
                break;
        }
    }
    
    public SLayoutBankPaymentRow clone() throws CloneNotSupportedException {
        SLayoutBankPaymentRow registry = new SLayoutBankPaymentRow(miClient);
        
        registry.setBizPartnerId(this.getBizPartnerId());
        registry.setBizPartnerBranchId(this.getBizPartnerBranchId());
        registry.setBizPartnerBranchAccountId(this.getBizPartnerBranchAccountId());
        registry.setBizPartner(this.getBizPartner());
        registry.setBizPartnerKey(this.getBizPartnerKey());
        registry.setIsForPayment(this.getIsForPayment());
        registry.setIsToPayed(this.getIsToPayed());
        registry.setBalance(this.getBalance());
        registry.setBalanceTot(this.getBalanceTot());
        registry.setCurrencyKey(this.getCurrencyKey());
        registry.setAccountCredit(this.getAccountCredit());
        registry.setRecordPeriod(this.getRecordPeriod());
        registry.setRecordBkc(this.getRecordBkc());
        registry.setRecordCob(this.getRecordCob());
        registry.setRecordNumber(this.getRecordNumber());
        registry.setRecordDate(this.getRecordDate());
        registry.setLayoutBankPayment(this.getLayoutBankPayment());
        registry.setFinRecordLayout(this.getFinRecordLayout());
        registry.setFinRecordLayoutOld(this.getFinRecordLayoutOld());
        
        return registry;
    }
}
