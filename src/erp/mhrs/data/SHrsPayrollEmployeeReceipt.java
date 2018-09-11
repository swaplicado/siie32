/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mhrs.data;

import java.util.Date;

/**
 *
 * @author Juan Barajas
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
    protected double mdTotalEarnings;
    protected double mdTotalDeductions;
    protected double mdTotalNet;

    protected String msNumberSeries;
    protected Date mtDateIssue;
    protected Date mtDatePayment;
    protected String msPaymentTypeSys;
    protected int mnPaymentTypeSysId;

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
        mdTotalEarnings = 0;
        mdTotalDeductions = 0;
        mdTotalNet = 0;
        msNumberSeries = "";
        mtDateIssue = null;
        mtDatePayment = null;
        msPaymentTypeSys = "";
        mnPaymentTypeSysId = 0;
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
        mvValues.add(mtDateIssue);
        mvValues.add(mtDatePayment);
        mvValues.add(msPaymentTypeSys);
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
    public void setFkPaymentTypeId(int n) { mnFkPaymentTypeId = n; }
    public void setEmployeeNumber(String s) { msEmployeeNumber = s; }
    public void setEmployeeName(String s) { msEmployeeName = s; }
    public void setTotalEarnings(double d) { mdTotalEarnings = d; }
    public void setTotalDeductions(double d) { mdTotalDeductions = d; }
    public void setTotalNet(double d) { mdTotalNet = d; }
    public void setNumberSeries(String s) { msNumberSeries = s; }
    public void setDateIssue(Date t) { mtDateIssue = t; }
    public void setDatePayment(Date t) { mtDatePayment = t; }
    public void setPaymentTypeSys(String s) { msPaymentTypeSys = s; }
    public void setPaymentTypeSysId(int n) { mnPaymentTypeSysId = n; }

    public int getPkPayrollId() { return mnPkPayrollId; }
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getPkIssueId() { return mnPkIssueId; }
    public int getPeriodYear() { return mnPeriodYear; }
    public int getPeriod() { return mnPeriod; }
    public int getPayrollNumber() { return mnPayrollNumber; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public String getNotes() { return msNotes; }
    public int getFkPaymentTypeId() { return mnFkPaymentTypeId; }
    public String getEmployeeNumber() { return msEmployeeNumber ; }
    public String getEmployeeName() { return msEmployeeName ; }
    public double getTotalEarnings() { return mdTotalEarnings ; }
    public double getTotalDeductions() { return mdTotalDeductions ; }
    public double getTotalNet() { return mdTotalNet ; }
    public String getNumberSeries() { return msNumberSeries ; }
    public Date getDateIssue() { return mtDateIssue; }
    public Date getDatePayment() { return mtDatePayment; }
    public String getPaymentTypeSys() { return msPaymentTypeSys ; }
    public int getPaymentTypeSysId() { return mnPaymentTypeSysId; }
    
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkPayrollId, mnPkEmployeeId, mnPkIssueId };
    }
}
