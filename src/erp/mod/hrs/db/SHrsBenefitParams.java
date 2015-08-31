/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import java.util.Date;

/**
 *
 * @author Juan Barajas
 */
public class SHrsBenefitParams {

    protected SDbBenefitTable moBenefit;
    protected SDbBenefitTable moBenefitAux;
    protected SDbEmployee moEmployee;
    protected SHrsPayrollReceipt moHrsPayrollReceipt;
    protected Date mtDateCut;
    protected int mnPayrollId;
    protected int mnEarningId;
    protected int mnEarningComputationTypeId;

    public SHrsBenefitParams(SDbBenefitTable benefit, SDbBenefitTable benefitAux, SDbEmployee employee, SHrsPayrollReceipt hrsPayrollReceipt, Date dateCut, int earningId, int earningComputationType) {
        moBenefit = benefit;
        moBenefitAux = benefitAux;
        moEmployee = employee;
        moHrsPayrollReceipt = hrsPayrollReceipt;
        mtDateCut = dateCut;
        
        mnPayrollId = 0;
        mnEarningId = earningId;
        mnEarningComputationTypeId = earningComputationType;
    }

    public void setBenefit(SDbBenefitTable o) { moBenefit = o; }
    public void setBenefitAux(SDbBenefitTable o) { moBenefitAux = o; }
    public void setEmployee(SDbEmployee o) { moEmployee = o; }
    public void setHrsPayrollReceipt(SHrsPayrollReceipt o) { moHrsPayrollReceipt = o; }
    public void setDateCut(Date t) { mtDateCut = t; }
    public void setPayrollId(int n) { mnPayrollId = n; }
    public void setEarningId(int n) { mnEarningId = n; }
    public void setEarningComputationTypeId(int n) { mnEarningComputationTypeId = n; }

    public SDbBenefitTable getBenefit() { return moBenefit; }
    public SDbBenefitTable getBenefitAux() { return moBenefitAux; }
    public SDbEmployee getEmployee() { return moEmployee; }
    public SHrsPayrollReceipt getHrsPayrollReceipt() { return moHrsPayrollReceipt; }
    public Date getDateCut() { return mtDateCut; }
    public int getPayrollId() { return mnPayrollId; }
    public int getEarningId() { return mnEarningId; }
    public int getEarningComputationTypeId() { return mnEarningComputationTypeId; }
}
