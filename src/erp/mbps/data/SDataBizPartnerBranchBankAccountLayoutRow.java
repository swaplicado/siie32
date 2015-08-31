/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

/**
 *
 * @author Juan Barajas
 */
public final class SDataBizPartnerBranchBankAccountLayoutRow extends erp.lib.table.STableRow {
    
    protected int mFkTypePaymentBankId;
    protected int mnFkBankId;
    protected int mnPkTypeLayoutBankId;
    protected java.lang.String msBank;
    protected java.lang.String msLayoutType;
    protected boolean mbIsPayment;
    
    public SDataBizPartnerBranchBankAccountLayoutRow() {        
        reset();
    }
    
    private void reset() { 
        mFkTypePaymentBankId = 0;
        mnFkBankId = 0;
        mnPkTypeLayoutBankId = 0;      
        msBank = "";
        msLayoutType = "";
        mbIsPayment = false;
    }

    public void setFkTypePaymentBankId(int n) { mFkTypePaymentBankId = n; }
    public void setFkBankId(int n) { mnFkBankId = n; }
    public void setPkTypeLayoutBankId(int n) { mnPkTypeLayoutBankId = n; }
    public void setBank(String s) { msBank = s; }
    public void setLayoutType(String s) { msLayoutType = s; }
    public void setIsPayment(boolean b) { mbIsPayment = b; }
    
    public int getFkTypePaymentBankId() { return mFkTypePaymentBankId; }
    public int getFkBankId() { return mnFkBankId; }
    public int getPkTypeLayoutBankId() { return mnPkTypeLayoutBankId; }
    public java.lang.String getBank() { return msBank; }
    public java.lang.String getLayoutType() { return msLayoutType; }
    public boolean getIsPayment() { return mbIsPayment; }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(msBank);
        mvValues.add(msLayoutType);
        mvValues.add(mbIsPayment);
    }
}
