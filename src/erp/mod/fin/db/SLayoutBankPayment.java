/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import java.util.ArrayList;

/**
 *
 * @author Juan Barajas
 */
public class SLayoutBankPayment {

    protected int mnLayoutPaymentType;
    protected int mnBizPartnerId;
    protected int mnBizPartnerBranchId;
    protected int mnBizPartnerBranchBankAccountId;
    protected int mnCurrencyId;
    protected double mdAmount;
    protected double mdExcRate;
    protected int mnFkBookkeepingYearId_n;
    protected int mnFkBookkeepingNumberId_n;
    protected int mnAction;
    
    protected ArrayList<SLayoutBankDps> maLayoutBankDps;

    public SLayoutBankPayment(int layoutRowType, int bizPartnerId, int bizPartnerBranchId, int bizPartnerBranchBankAccountId) {
        mnLayoutPaymentType = layoutRowType;
        mnBizPartnerId = bizPartnerId;
        mnBizPartnerBranchId = bizPartnerBranchId;
        mnBizPartnerBranchBankAccountId = bizPartnerBranchBankAccountId;
        mnFkBookkeepingYearId_n = 0;
        mnFkBookkeepingNumberId_n = 0;
        
        maLayoutBankDps = new ArrayList<SLayoutBankDps>();
    }

    public void setLayoutRowType(int n) { mnLayoutPaymentType = n; }
    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartnerBranchId(int n) { mnBizPartnerBranchId = n; }
    public void setBizPartnerBranchAccountId(int n) { mnBizPartnerBranchBankAccountId = n; }
    public void setCurrencyId(int n) { mnCurrencyId = n; }
    public void setAmount(double d) { mdAmount = d; }
    public void setExcRate(double d) { mdExcRate = d; }
    public void setFkBookkeepingYearId_n(int n) { mnFkBookkeepingYearId_n = n; }
    public void setFkBookkeepingNumberId_n(int n) { mnFkBookkeepingNumberId_n = n; }
    public void setAction(int n) { mnAction = n; }
    
    public void setLayoutBankDps(ArrayList<SLayoutBankDps> o) { maLayoutBankDps = o; }

    public int getLayoutPaymentType() { return mnLayoutPaymentType; }
    public int getBizPartnerId() { return mnBizPartnerId; }
    public int getBizPartnerBranchId() { return mnBizPartnerBranchId; }
    public int getBizPartnerBranchAccountId() { return mnBizPartnerBranchBankAccountId; }
    public int getCurrencyId() { return mnCurrencyId; }
    public double getAmount() { return mdAmount; }
    public double getExcRate() { return mdExcRate; }
    public int getFkBookkeepingYearId_n() { return mnFkBookkeepingYearId_n; }
    public int getFkBookkeepingNumberId_n() { return mnFkBookkeepingNumberId_n; }
    public int getAction() { return mnAction; }
    
    public ArrayList<SLayoutBankDps> getLayoutBankDps() { return maLayoutBankDps; }
    
    @Override
     public SLayoutBankPayment clone() throws CloneNotSupportedException {
        SLayoutBankPayment bankPayment = new SLayoutBankPayment(this.getLayoutPaymentType(), this.getBizPartnerId(), this.getBizPartnerBranchId(), this.getBizPartnerBranchAccountId());
        
        bankPayment.setCurrencyId(this.getCurrencyId());
        bankPayment.setAmount(this.getAmount());
        bankPayment.setExcRate(this.getExcRate());
        bankPayment.setFkBookkeepingYearId_n(this.getFkBookkeepingYearId_n());
        bankPayment.setFkBookkeepingNumberId_n(this.getFkBookkeepingNumberId_n());
        bankPayment.setAction(this.getAction());
        
        for (SLayoutBankDps bankDps : maLayoutBankDps) {
            bankPayment.getLayoutBankDps().add(bankDps);
        }
        
        return bankPayment;
    }
}
