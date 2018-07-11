/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import java.util.Date;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public abstract interface SHrsDataProvider {

    public SHrsPayroll createHrsPayroll(final SDbConfig config, final SDbWorkingDaySettings workingDaySettings, final SDbPayroll payroll, final boolean isCopy) throws Exception;
    public SHrsEmployee createHrsEmployee(final SHrsPayroll hrsPayroll, final int payrollId, final int employeeId, final int payrollYear, final int payrollYearPeriod, final int fiscalYear, final Date dateStart, final Date dateEnd, final int taxComputationType) throws Exception;
}
