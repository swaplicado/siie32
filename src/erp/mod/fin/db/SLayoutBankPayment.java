/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import java.util.ArrayList;

/**
 *
 * @author Juan Barajas, Alfredo Pérez
 */
public class SLayoutBankPayment {
    
    public static final int ACTION_PAY_APPLY = 1;
    public static final int ACTION_PAY_REMOVE = 2;

    protected int mnLayoutPaymentType;
    protected int mnBizPartnerId;
    protected int mnBizPartnerBranchId;
    protected int mnBizPartnerBranchBankAccountId;
    protected SMoney moAmount;
    protected int mnFkBookkeepingYearId_n;
    protected int mnFkBookkeepingNumberId_n;
    protected int mnAction;
    protected String msReferenceRecord;
    
    protected ArrayList<SLayoutBankDps> maLayoutBankDps;

    public SLayoutBankPayment(int layoutRowType, int bizPartnerId, int bizPartnerBranchId, int bizPartnerBranchBankAccountId) {
        mnLayoutPaymentType = layoutRowType;
        mnBizPartnerId = bizPartnerId;
        mnBizPartnerBranchId = bizPartnerBranchId;
        mnBizPartnerBranchBankAccountId = bizPartnerBranchBankAccountId;
        moAmount = null;
        mnFkBookkeepingYearId_n = 0;
        mnFkBookkeepingNumberId_n = 0;
        mnAction = 0;
        msReferenceRecord = "";
        
        maLayoutBankDps = new ArrayList<SLayoutBankDps>();
    }

    public void setLayoutRowType(int n) { mnLayoutPaymentType = n; }
    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartnerBranchId(int n) { mnBizPartnerBranchId = n; }
    public void setBizPartnerBranchAccountId(int n) { mnBizPartnerBranchBankAccountId = n; }
    public void setAmount(SMoney a) { moAmount = a; }
    public void setFkBookkeepingYearId_n(int n) { mnFkBookkeepingYearId_n = n; }
    public void setFkBookkeepingNumberId_n(int n) { mnFkBookkeepingNumberId_n = n; }
    public void setAction(int n) { mnAction = n; }
    public void setReferenceRecord(String s) { msReferenceRecord = s; }
    
    public int getLayoutPaymentType() { return mnLayoutPaymentType; }
    public int getBizPartnerId() { return mnBizPartnerId; }
    public int getBizPartnerBranchId() { return mnBizPartnerBranchId; }
    public int getBizPartnerBranchAccountId() { return mnBizPartnerBranchBankAccountId; }
    public SMoney getAmount() { return moAmount; }
    public int getFkBookkeepingYearId_n() { return mnFkBookkeepingYearId_n; }
    public int getFkBookkeepingNumberId_n() { return mnFkBookkeepingNumberId_n; }
    public int getAction() { return mnAction; }
    public String getReferenceRecord() { return msReferenceRecord; }
    
    public ArrayList<SLayoutBankDps> getLayoutBankDps() { return maLayoutBankDps; }
    
    @Override
    public SLayoutBankPayment clone() {
        SLayoutBankPayment bankPayment = new SLayoutBankPayment(this.getLayoutPaymentType(), this.getBizPartnerId(), this.getBizPartnerBranchId(), this.getBizPartnerBranchAccountId());
        
        bankPayment.setAmount(this.getAmount());
        bankPayment.setFkBookkeepingYearId_n(this.getFkBookkeepingYearId_n());
        bankPayment.setFkBookkeepingNumberId_n(this.getFkBookkeepingNumberId_n());
        bankPayment.setAction(this.getAction());
        bankPayment.setReferenceRecord(this.getReferenceRecord());
        
        for (SLayoutBankDps bankDps : maLayoutBankDps) {
            if (bankDps != null) {
                bankPayment.getLayoutBankDps().add(bankDps.clone());
            }
        }
        
        return bankPayment;
    }
}
