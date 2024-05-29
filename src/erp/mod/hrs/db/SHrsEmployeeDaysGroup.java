/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

/**
 * Las instancias de esta clase son una estructura de datos
 * que contiene los valores de días de contratación, de incapacidad, no trabajados, etc.,
 * para el procesamiento de impuestos y demás cálculos y emisión de nóminas.
 * 
 * @author Sergio Flores
 */
public class SHrsEmployeeDaysGroup {
    
    protected double mdDaysHired;
    protected double mdDaysIncapacityNotPaid;
    protected double mdDaysNotWorkedButPaid;
    protected double mdDaysNotWorkedNotPaid;
    
    public SHrsEmployeeDaysGroup() {
        mdDaysHired = 0;
        mdDaysIncapacityNotPaid = 0;
        mdDaysNotWorkedButPaid = 0;
        mdDaysNotWorkedNotPaid = 0;
    }
    
    public void setDaysHired(double d) { mdDaysHired = d; }
    public void setDaysIncapacityNotPaid(double d) { mdDaysIncapacityNotPaid = d; }
    public void setDaysNotWorkedButPaid(double d) { mdDaysNotWorkedButPaid = d; }
    public void setDaysNotWorkedNotPaid(double d) { mdDaysNotWorkedNotPaid = d; }
    
    public double getDaysHired() { return mdDaysHired; }
    public double getDaysIncapacityNotPaid() { return mdDaysIncapacityNotPaid; }
    public double getDaysNotWorkedButPaid() { return mdDaysNotWorkedButPaid; }
    public double getDaysNotWorkedNotPaid() { return mdDaysNotWorkedNotPaid; }
    
    /**
     * Get not-worked days.
     * @return Days not worked but paid + days not worked and not paid.
     */
    public double getNotWorkedDays() { return mdDaysNotWorkedButPaid - mdDaysNotWorkedNotPaid; }
    
    /**
     * Get taxable days.
     * @return Days hired - days of incapacity not paid.
     */
    public double getTaxableDays() { return mdDaysHired - mdDaysIncapacityNotPaid; }
}
