/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.HashSet;
import sa.lib.SLibUtils;

/**
 * Abstraction of a payment of a bank layout.
 * 
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores
 */
public class SLayoutBankPayment {
    
    public static final int ACTION_PAY_APPLY = 1;
    public static final int ACTION_PAY_REMOVE = 2;

    protected int mnTransactionType;
    protected int mnBizPartnerId;
    protected int mnBizPartnerBranchId;
    protected int mnBizPartnerBranchBankAccountId;
    protected SMoney moMoneyAmount;
    protected int mnBookkeepingYearId_n;
    protected int mnBookkeepingNumberId_n;
    protected int mnAction;
    protected String msReferenceRecord;
    protected String msPrepaymentObservations;
    protected String msPrepaymentEmail;
    
    protected ArrayList<SLayoutBankDps> maLayoutBankDpss;

    public SLayoutBankPayment(int transactionType, int bizPartnerId, int bizPartnerBranchId, int bizPartnerBranchBankAccountId) {
        mnTransactionType = transactionType;
        mnBizPartnerId = bizPartnerId;
        mnBizPartnerBranchId = bizPartnerBranchId;
        mnBizPartnerBranchBankAccountId = bizPartnerBranchBankAccountId;
        moMoneyAmount = null;
        mnBookkeepingYearId_n = 0;
        mnBookkeepingNumberId_n = 0;
        mnAction = 0;
        msReferenceRecord = "";
        msPrepaymentObservations = "";
        msPrepaymentEmail = "";
        
        maLayoutBankDpss = new ArrayList<>();
    }

    public void setTransactionType(int n) { mnTransactionType = n; }
    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartnerBranchId(int n) { mnBizPartnerBranchId = n; }
    public void setBizPartnerBranchBankAccountId(int n) { mnBizPartnerBranchBankAccountId = n; }
    public void setMoneyAmount(SMoney o) { moMoneyAmount = o; }
    public void setBookkeepingYearId_n(int n) { mnBookkeepingYearId_n = n; }
    public void setBookkeepingNumberId_n(int n) { mnBookkeepingNumberId_n = n; }
    public void setAction(int n) { mnAction = n; }
    public void setReferenceRecord(String s) { msReferenceRecord = s; }
    public void setPrepaymentObservations(String s) { msPrepaymentObservations = s; }
    public void setPrepaymentEmail(String s) { msPrepaymentEmail = s; }
    
    public int getTransactionType() { return mnTransactionType; }
    public int getBizPartnerId() { return mnBizPartnerId; }
    public int getBizPartnerBranchId() { return mnBizPartnerBranchId; }
    public int getBizPartnerBranchBankAccountId() { return mnBizPartnerBranchBankAccountId; }
    public SMoney getMoneyAmount() { return moMoneyAmount; }
    public int getBookkeepingYearId_n() { return mnBookkeepingYearId_n; }
    public int getBookkeepingNumberId_n() { return mnBookkeepingNumberId_n; }
    public int getAction() { return mnAction; }
    public String getReferenceRecord() { return msReferenceRecord; }
    public String getPrepaymentObservations() { return msPrepaymentObservations; }
    public String getPrepaymentEmail() { return msPrepaymentEmail; }
    
    public ArrayList<SLayoutBankDps> getLayoutBankDpss() { return maLayoutBankDpss; }

    public int[] getBookkeepingNumberKey_n() { return mnBookkeepingYearId_n == 0 && mnBookkeepingNumberId_n == 0 ? null : new int[] { mnBookkeepingYearId_n, mnBookkeepingNumberId_n }; }
    public int[] getBizPartnerBranchBankAccountKey() { return new int[] { mnBizPartnerBranchId, mnBizPartnerBranchBankAccountId }; }
    
    /**
     * Get a list of unrepeatable email recipients.
     * @return 
     */
    public ArrayList<String> getEmailRecipients() {
        HashSet<String> recipients = new HashSet<>();
        
        switch (mnTransactionType) {
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
                for (SLayoutBankDps layoutBankDps : maLayoutBankDpss) {
                    String[] emails = SLibUtils.textExplode(layoutBankDps.getEmail(), ";");
                    for (String email : emails) {
                        recipients.add(email);
                    }
                }
                break;
                
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                String[] emails = SLibUtils.textExplode(msPrepaymentEmail, ";");
                for (String email : emails) {
                    recipients.add(email);
                }
                break;
                
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_OWN_TRANSFER:
                // not supported yet!
                break;
                
            default:
        }
        
            
        return new ArrayList<>(recipients);
    }
    
    @Override
    public SLayoutBankPayment clone() {
        SLayoutBankPayment clone = new SLayoutBankPayment(this.getTransactionType(), this.getBizPartnerId(), this.getBizPartnerBranchId(), this.getBizPartnerBranchBankAccountId());
        
        if (this.getMoneyAmount() != null) {
            clone.setMoneyAmount(this.getMoneyAmount().clone());
        }
        clone.setBookkeepingYearId_n(this.getBookkeepingYearId_n());
        clone.setBookkeepingNumberId_n(this.getBookkeepingNumberId_n());
        clone.setAction(this.getAction());
        clone.setReferenceRecord(this.getReferenceRecord());
        clone.setPrepaymentObservations(this.getPrepaymentObservations());
        clone.setPrepaymentEmail(this.getPrepaymentEmail());
        
        for (SLayoutBankDps child : maLayoutBankDpss) {
            if (child != null) {
                clone.getLayoutBankDpss().add(child.clone());
            }
        }
        
        return clone;
    }
}
