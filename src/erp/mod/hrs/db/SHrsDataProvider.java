/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import java.util.Date;

/**
 *
 * @author Néstor Ávalos
 */
public abstract interface SHrsDataProvider {

    public SHrsPayroll createPayroll(final SDbConfig config, final SDbWorkingDaySettings workingDaySettings, final SDbPayroll payroll, final boolean isCopy) throws Exception;
    public SHrsEmployee createEmployee(final SHrsPayroll hrsPayroll, final int payrollId, final int employeeId, final int payrollYear, final int payrollYearPariod, final int fiscalYear, final Date dateStart, final Date dateEnd, final int taxComputationType) throws Exception;
}
