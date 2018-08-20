/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import java.util.ArrayList;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores
 */
public class SLayoutBankRecord {

    protected SLayoutBankRecordKey moLayoutBankRecordKey;
    protected ArrayList<SLayoutBankPayment> maLayoutBankPayments;

    public SLayoutBankRecord(SLayoutBankRecordKey recordLayout) {
        moLayoutBankRecordKey = recordLayout;
        maLayoutBankPayments = new ArrayList<>();
    }

    public void setLayoutBankRecordKey(SLayoutBankRecordKey n) { moLayoutBankRecordKey = n; }

    public SLayoutBankRecordKey getLayoutBankRecordKey() { return moLayoutBankRecordKey; }
    
    public ArrayList<SLayoutBankPayment> getLayoutBankPayments() { return maLayoutBankPayments; }
    
    public SLayoutBankPayment obtainLayoutBankPayment(int bizPartner, int bizPartnerBranch, int bizPartnerAccBank) { 
        SLayoutBankPayment layoutBankPayment = null;
        
        for (SLayoutBankPayment bankPayment : maLayoutBankPayments) {
            if (SLibUtils.compareKeys(new int[] { bankPayment.getBizPartnerId(), bankPayment.getBizPartnerBranchId(), bankPayment.getBizPartnerBranchAccountId() }, new int[] {bizPartner, bizPartnerBranch, bizPartnerAccBank })) {
                layoutBankPayment = bankPayment;
                break;
            }
        }
        
        return layoutBankPayment; 
    }
    
    public void removeLayoutBankPayment(int bizPartner, int bizPartnerBranch, int bizPartnerAccBank) { 
        SLayoutBankPayment layoutBankPaymentRemove = null;
        
        for (SLayoutBankPayment bankPayment : maLayoutBankPayments) {
            if (SLibUtils.compareKeys(new int[] { bankPayment.getBizPartnerId(), bankPayment.getBizPartnerBranchId(), bankPayment.getBizPartnerBranchAccountId() }, new int[] {bizPartner, bizPartnerBranch, bizPartnerAccBank })) {
                layoutBankPaymentRemove = bankPayment;
                break;
            }
        }
        
        maLayoutBankPayments.remove(layoutBankPaymentRemove);
    }
    
    @Override
    public SLayoutBankRecord clone() {
        SLayoutBankRecord layoutBankRecord = new SLayoutBankRecord(moLayoutBankRecordKey);
        
        for (SLayoutBankPayment payment : maLayoutBankPayments) {
            layoutBankRecord.getLayoutBankPayments().add(payment.clone());
        }
        
        return layoutBankRecord;
    }
}
