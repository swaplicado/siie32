/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores
 */
public class SLayoutBankRecord {

    protected SLayoutBankRecordKey moLayoutBankRecordKey;
    protected String msBookkeepingCenterCode;
    protected String msCompanyBranchCode;
    protected Date mtDate;
    
    protected ArrayList<SLayoutBankPayment> maLayoutBankPayments;

    public SLayoutBankRecord(SLayoutBankRecordKey recordLayout) {
        moLayoutBankRecordKey = recordLayout;
        msBookkeepingCenterCode = "";
        msCompanyBranchCode = "";
        mtDate = null;
        
        maLayoutBankPayments = new ArrayList<>();
    }

    public void setLayoutBankRecordKey(SLayoutBankRecordKey n) { moLayoutBankRecordKey = n; }
    public void setBookkeepingCenterCode(String s) { msBookkeepingCenterCode = s; }
    public void setCompanyBranchCode(String s) { msCompanyBranchCode = s; }
    public void setDate(Date t) { mtDate = t; }
    
    public SLayoutBankRecordKey getLayoutBankRecordKey() { return moLayoutBankRecordKey; }
    public String getBookkeepingCenterCode() { return msBookkeepingCenterCode; }
    public String getCompanyBranchCode() { return msCompanyBranchCode; }
    public Date getDate() { return mtDate; }
    
    public ArrayList<SLayoutBankPayment> getLayoutBankPayments() { return maLayoutBankPayments; }
    
    public SLayoutBankPayment getLayoutBankPayment(int bizPartnerId, int bizPartnerBranchId, int bizPartnerBranchAccountBankId) { 
        SLayoutBankPayment layoutBankPayment = null;
        
        for (SLayoutBankPayment payment : maLayoutBankPayments) {
            if (SLibUtils.compareKeys(new int[] { payment.getBizPartnerId(), payment.getBizPartnerBranchId(), payment.getBizPartnerBranchBankAccountId() }, new int[] {bizPartnerId, bizPartnerBranchId, bizPartnerBranchAccountBankId })) {
                layoutBankPayment = payment;
                break;
            }
        }
        
        return layoutBankPayment; 
    }
    
    public void removeLayoutBankPayment(int bizPartnerId, int bizPartnerBranchId, int bizPartnerBranchAccountBankId) { 
        SLayoutBankPayment layoutBankPayment = getLayoutBankPayment(bizPartnerId, bizPartnerBranchId, bizPartnerBranchAccountBankId);
        
        if (layoutBankPayment != null) {
            maLayoutBankPayments.remove(layoutBankPayment);
        }
    }
    
    @Override
    public SLayoutBankRecord clone() {
        SLayoutBankRecord clone = new SLayoutBankRecord(moLayoutBankRecordKey.clone());
        
        clone.setBookkeepingCenterCode(this.getBookkeepingCenterCode());
        clone.setCompanyBranchCode(this.getCompanyBranchCode());
        clone.setDate(this.getDate());
    
        for (SLayoutBankPayment payment : maLayoutBankPayments) {
            clone.getLayoutBankPayments().add(payment.clone());
        }
        
        return clone;
    }
}
