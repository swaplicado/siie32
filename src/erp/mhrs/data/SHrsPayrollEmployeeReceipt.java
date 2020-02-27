/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mhrs.data;

import java.util.Date;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SHrsPayrollEmployeeReceipt extends erp.lib.table.STableRow {
    
    protected int mnPkPayrollId;
    protected int mnPkEmployeeId;
    protected int mnPkIssueId;
    protected int mnPeriodYear;
    protected int mnPeriod;
    protected int mnPayrollNumber;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    protected String msNotes;
    protected int mnFkPaymentTypeId;
    
    protected String msEmployeeNumber;
    protected String msEmployeeName;
    protected int mnDepartmentId;
    protected String msDepartment;
    protected double mdTotalEarnings;
    protected double mdTotalDeductions;
    protected double mdTotalNet;

    protected String msNumberSeries;
    protected Date mtDateOfIssue;
    protected Date mtDateOfPayment;
    /** Besides serving as payment type ID, it is a flag to determine if receipt has been already selected to be emmited, when this type is different from <code>SDataConstantsSys.TRNU_TP_PAY_SYS_NA</code>. */
    protected int mnPaymentTypeSysId;
    protected String msPaymentTypeSys;
    protected String msUuidToSubstitute;

    public SHrsPayrollEmployeeReceipt() {
        mnPkPayrollId = 0;
        mnPkEmployeeId = 0;
        mnPkIssueId = 0;
        mnPeriodYear = 0;
        mnPeriod = 0;
        mnPayrollNumber = 0;
        mtDateStart = null;
        mtDateEnd = null;
        msNotes = "";
        mnFkPaymentTypeId = 0;
        msEmployeeNumber = "";
        msEmployeeName = "";
        mnDepartmentId = 0;
        msDepartment = "";
        mdTotalEarnings = 0;
        mdTotalDeductions = 0;
        mdTotalNet = 0;
        msNumberSeries = "";
        mtDateOfIssue = null;
        mtDateOfPayment = null;
        mnPaymentTypeSysId = 0;
        msPaymentTypeSys = "";
        msUuidToSubstitute = "";
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(msEmployeeName);
        mvValues.add(msEmployeeNumber);
        mvValues.add(mdTotalEarnings);
        mvValues.add(mdTotalDeductions);
        mvValues.add(mdTotalNet);
        mvValues.add(msNumberSeries);
        mvValues.add(mtDateOfIssue);
        mvValues.add(mtDateOfPayment);
        mvValues.add(msPaymentTypeSys);
        mvValues.add(msUuidToSubstitute);
        mvValues.add(msDepartment);
    }

    public void setPkPayrollId(int n) { mnPkPayrollId = n; }
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setPkIssueId(int n) { mnPkIssueId = n; }
    public void setPeriodYear(int n) { mnPeriodYear = n; }
    public void setPeriod(int n) { mnPeriod = n; }
    public void setPayrollNumber(int n) { mnPayrollNumber = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setNotes(String s) { msNotes = s; }
    /** Besides setting payment type ID, it is a flag to determine if receipt has been already selected to be emmited, when this type is different from <code>SDataConstantsSys.TRNU_TP_PAY_SYS_NA</code>. */
    public void setFkPaymentTypeId(int n) { mnFkPaymentTypeId = n; }
    public void setEmployeeNumber(String s) { msEmployeeNumber = s; }
    public void setEmployeeName(String s) { msEmployeeName = s; }
    public void setDepartmentId(int n) { mnDepartmentId = n; }
    public void setDepartment(String s) { msDepartment = s; }
    public void setTotalEarnings(double d) { mdTotalEarnings = d; }
    public void setTotalDeductions(double d) { mdTotalDeductions = d; }
    public void setTotalNet(double d) { mdTotalNet = d; }
    public void setNumberSeries(String s) { msNumberSeries = s; }
    public void setDateOfIssue(Date t) { mtDateOfIssue = t; }
    public void setDateOfPayment(Date t) { mtDateOfPayment = t; }
    public void setPaymentTypeSysId(int n) { mnPaymentTypeSysId = n; }
    public void setPaymentTypeSys(String s) { msPaymentTypeSys = s; }
    public void setUuidToSubstitute(String s) { msUuidToSubstitute = s; }

    public int getPkPayrollId() { return mnPkPayrollId; }
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getPkIssueId() { return mnPkIssueId; }
    public int getPeriodYear() { return mnPeriodYear; }
    public int getPeriod() { return mnPeriod; }
    public int getPayrollNumber() { return mnPayrollNumber; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public String getNotes() { return msNotes; }
    /** Besides getting payment type ID, it is a flag to determine if receipt has been already selected to be emmited, when this type is different from <code>SDataConstantsSys.TRNU_TP_PAY_SYS_NA</code>. */
    public int getFkPaymentTypeId() { return mnFkPaymentTypeId; }
    public String getEmployeeNumber() { return msEmployeeNumber; }
    public String getEmployeeName() { return msEmployeeName; }
    public int getDepartmentId() { return mnDepartmentId; }
    public String getDepartment() { return msDepartment; }
    public double getTotalEarnings() { return mdTotalEarnings ; }
    public double getTotalDeductions() { return mdTotalDeductions ; }
    public double getTotalNet() { return mdTotalNet ; }
    public String getNumberSeries() { return msNumberSeries ; }
    public Date getDateOfIssue() { return mtDateOfIssue; }
    public Date getDateOfPayment() { return mtDateOfPayment; }
    public int getPaymentTypeSysId() { return mnPaymentTypeSysId; }
    public String getPaymentTypeSys() { return msPaymentTypeSys ; }
    public String getUuidToSubstitute() { return msUuidToSubstitute; }

    /**
     * Get primary key of issue of payroll receipt.
     * @return 
     */
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkPayrollId, mnPkEmployeeId, mnPkIssueId };
    }
}
