package erp.mod.hrs.db.ssc;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbEarning;
import erp.mod.hrs.db.SDbEmployee;
import erp.mod.hrs.db.SHrsConsts;
import erp.mod.hrs.db.SHrsUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Claudio Peña
 */
public abstract class SSscUtils {
    
    private static final double FOOD_EXEMPT_PCT = 0.4;
    private static final double OVERTIME_EXEMPT_PCT = 0.1;
    private static final int OVERTIME_EXEMPT_DAYS_WEEK = 9;
    private static final int OVERTIME_EXEMPT_DAYS_FORT = 18;

    /**
     * Obtener arreglo de tipos de percepciones excluidas para cálculo del SBC.
     * @return Arreglo de tipos de percepciones excluidas.
     */
    private static ArrayList<Integer> getExcludedEarningTypes() {
        ArrayList<Integer> excludedEarningTypes = new ArrayList<>();
        
        excludedEarningTypes.add(SModSysConsts.HRSS_TP_EAR_EAR);
        excludedEarningTypes.add(SModSysConsts.HRSS_TP_EAR_ANN_BONUS);
        excludedEarningTypes.add(SModSysConsts.HRSS_TP_EAR_PTU);
        excludedEarningTypes.add(SModSysConsts.HRSS_TP_EAR_SAVINGS);
        excludedEarningTypes.add(SModSysConsts.HRSS_TP_EAR_TAX_SUB);
        excludedEarningTypes.add(SModSysConsts.HRSS_TP_EAR_VAC_BONUS);
        excludedEarningTypes.add(SModSysConsts.HRSS_TP_EAR_OTH);
        
        return excludedEarningTypes;
    }
    
    /**
     * Obtener texto de tipos de percepciones excluidas para cálculo del SBC para sentencias SQL.
     * Los valores están separados entre sí mediante comas.
     * @return Texto de tipos de percepciones excluidas.
     */
    private static String getExcludedEarningTypesSqlList() {
        String sql = "";
        
        for (Integer type : getExcludedEarningTypes()) {
            sql += (sql.isEmpty() ? "" : ", ") + type;
        }
        
        return sql;
    }
    
    /**
     * Obtener el conjunto de las percepciones pagadas a todos los empleados en un período dado a manera de arreglo.
     * El arreglo de percepciones está ordenado por código de la percepción.
     * @param session Sesión de usuario.
     * @param year Año del período.
     * @param monthStart Mes inicial del período.
     * @param monthEnd Mes final del período.
     * @return Arreglo con el conjunto de las percepciones pagadas a todos los empleados en un período dado.
     * @throws Exception 
     */
    public static ArrayList<SDbEarning> getEarningPaidInPeriod(final SGuiSession session, final int year, final int monthStart, final int monthEnd) throws Exception {
        /*
        1) ejecutar consulta para obtener dicho conjunto de percepciones
        2) recorrer el result set para leer la percepción llenar el arreglo
        */
        ArrayList<SDbEarning> earnings = new ArrayList<>();

        String sql = "SELECT DISTINCT e.id_ear, e.code, e.name "
                + "FROM hrs_pay AS p "
                + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                + "INNER JOIN hrs_pay_rcp_ear AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                + "INNER JOIN hrs_ear AS e ON e.id_ear = pre.fk_ear "
                + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del AND "
                + "p.per_year = " + year + " AND p.per BETWEEN " + monthStart + " AND " + monthEnd + " AND "
                + "e.fk_tp_ear NOT IN (" + getExcludedEarningTypesSqlList() + ") "
                + "ORDER BY e.code, e.name, e.id_ear;";

        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                SDbEarning earning = (SDbEarning) session.readRegistry(SModConsts.HRS_EAR, new int[] { resultSet.getInt("e.id_ear") });
                earnings.add(earning);
            }
        }
            
        return earnings;
    }
    
    /**
     * Obtener el conjunto de los empleados activos al momento de invocarse este método que tuvieron recibos de nómina en el período dado y que estuvieron cuando menos dados de alta durante todo el referido período a manera de arreglo.
     * El arreglo de empleados está ordenado por nombre del empleado.
     * @param session Sesión de usuario.
     * @param year Año del período.
     * @param monthStart Mes inicial del período.
     * @param monthEnd Mes final del período.
     * @return Arreglo con el conjunto de los empleados activos al momento de invocarse este método que tuvieron recibos de nómina en el período dado y que estuvieron cuando menos dados de alta durante todo el referido período.
     * @throws Exception 
     */
    public static ArrayList<SDbEmployee> getEmployeesPaidInPeriod(final SGuiSession session, final int year, final int monthStart, final int monthEnd) throws Exception {
        ArrayList<SDbEmployee> employees = new ArrayList<>();
        
        /*
        1) ejecutar consulta para obtener dicho conjunto de empleados
        2) recorrer el result set para leer el empleado en cuestión y llenar el arreglo
        */
        
        Date cutoffdate = SLibTimeUtils.createDate(year, monthStart);

        String sql = "SELECT DISTINCT pr.id_emp, bp.bp "
                + "FROM hrs_pay AS p "
                + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                + "INNER JOIN hrs_pay_rcp_ear AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                + "INNER JOIN hrs_ear AS e ON e.id_ear = pre.fk_ear "
                + "INNER JOIN erp.bpsu_bp AS bp ON bp.id_bp = pr.id_emp "
                + "INNER JOIN erp.hrsu_emp AS emp ON emp.id_emp = pr.id_emp "
                + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del AND "
                + "p.per_year = " + year + " AND p.per BETWEEN " + monthStart + " AND " + monthEnd + " AND "
//                + "e.fk_tp_ear NOT IN (" + getExcludedEarningTypesSqlList() + ") AND " // ¿?
                + "emp.dt_hire < '" + SLibUtils.DbmsDateFormatDate.format(cutoffdate) + "' AND emp.b_act AND "
                + "emp.dt_sal_ssc < '" + SLibUtils.DbmsDateFormatDate.format(cutoffdate) + "' "
//                + "and pr.id_emp = 3504 " // xxx pruebas
                + "GROUP BY pr.id_emp ORDER BY bp.bp, pr.id_emp;" ; 
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                SDbEmployee employee = (SDbEmployee) session.readRegistry(SModConsts.HRSU_EMP, new int[] { resultSet.getInt("pr.id_emp") });
                employees.add(employee);
            }
        }
        
        return employees;
    }
    
    /**
     * 
     * @param session
     * @param year
     * @param monthStart
     * @param monthEnd
     * @return
     * @throws Exception 
     */
    public static ArrayList<SRowEmployeeSsc> createEmployeeSbcRows(final SGuiSession session, final int year, final int monthStart, final int monthEnd) throws Exception {
        ArrayList<SRowEmployeeSsc> rows = new ArrayList<>();
        
        /*
        1) obtener el conjunto de las percepciones pagadas a todos los empleados en un período dado a manera de arreglo
        2) obtener el conjunto de los empleados activos al momento de invocarse este método que tuvieron recibos de nómina en el período dado y que estuvieron cuando menos dados de alta durante todo el referido período a manera de arreglo
        3) recorrer el arreglo de empleados para:
        3.1) crear objeto row (para el grid del diálogo de actualización masiva del SBC) del empleado prellenado con todas las percepciones pagadas a todos los empleados en un período dado
        3.2) ejecutar consulta para obtener individualmente todas las percepciones pagadas del empleado actual, percepción a percepción, nómina a nómina
        3.3) para cada percepción en cada nómina: determinar parte gravada y parte excenta (según el tipo de percepción del SAT), y sumarizar dichos valores
                una vez terminada de procesar una percepción, actualizar el objeto SbcEarning correspondente al renglón (row) del empleado actual con los montos totales de parte gravada y parte excenta
        */
        
        ArrayList<SDbEarning> earnings = SSscUtils.getEarningPaidInPeriod(session, year , monthStart, monthEnd);
        ArrayList<SDbEmployee> employees = SSscUtils.getEmployeesPaidInPeriod(session, year , monthStart, monthEnd);
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStart = formatter.format(SLibTimeUtils.getBeginOfMonth(SLibTimeUtils.createDate(year, monthStart))); 
        Date periodStart = SLibTimeUtils.getBeginOfMonth(SLibTimeUtils.createDate(year, monthStart)); 
        String dateEnd = formatter.format(SLibTimeUtils.getEndOfMonth(SLibTimeUtils.createDate(year, monthEnd)));
        Date periodEnd = SLibTimeUtils.getEndOfMonth(SLibTimeUtils.createDate(year, monthEnd));
        
        double uma = SHrsUtils.getRecentUma(session, periodEnd);
        double foodExemptWeekly = SLibUtils.roundAmount(uma * (SHrsConsts.WEEK_DAYS) * FOOD_EXEMPT_PCT);
        double foodExemptFortnightly = SLibUtils.roundAmount(uma * (SHrsConsts.FORTNIGHT_FIXED_DAYS ) * FOOD_EXEMPT_PCT);
        double overtimeExemptWeekly = SLibUtils.roundAmount(uma * (SHrsConsts.WEEK_DAYS * 4) * OVERTIME_EXEMPT_PCT);
        double overtimeExemptFortnightly = SLibUtils.roundAmount(uma * (SHrsConsts.FORTNIGHT_FIXED_DAYS * 2) * OVERTIME_EXEMPT_PCT);
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            for (SDbEmployee employee : employees) {
                // crear nuevo renglón para el empleado actual:
                SRowEmployeeSsc row = new SRowEmployeeSsc(session, employee, periodStart, periodEnd, year , monthStart, monthEnd);

                // prellenar el arreglo de percepciones del nuevo renglón con todas las percepciones pagadas a todos los empleados en un período dado:
                for (SDbEarning earning : earnings) {
                    SSscEarning sbcEarning = new SSscEarning(earning.getPkEarningId());
                    row.getSbcEarnings().add(sbcEarning);
                }

                // procesar las percepciones pagadas al empleado:
                
                int currentEarningId = 0;
                double totalAmountExempt = 0;
                double totalAmountTaxed = 0;

                int calendarDaysInPeriod = daysCalendarPeriod(year , monthStart);
                calendarDaysInPeriod = calendarDaysInPeriod + daysCalendarPeriod(year , monthEnd);
            
                String sql = "SELECT e.id_ear, e.fk_tp_ear, pre.id_pay, pre.id_mov, pre.amt_r, pr.pay_hr_r, emp.wrk_hrs_day "
                        + "FROM hrs_pay AS p "
                        + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                        + "INNER JOIN hrs_pay_rcp_ear AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                        + "INNER JOIN hrs_ear AS e ON e.id_ear = pre.fk_ear "
                        + "INNER JOIN erp.hrsu_emp AS emp on pre.id_emp = emp.id_emp "
                        + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del "
                        + "AND p.per_year = " + year + " AND p.per BETWEEN " + monthStart + " AND " + monthEnd + " AND "
                        + "e.fk_tp_ear NOT IN (" + getExcludedEarningTypesSqlList() + ") "
                        + "AND pre.id_emp = " + employee.getPkEmployeeId() + " "
                        + "ORDER BY e.id_ear, e.fk_tp_ear, pre.id_pay, pre.id_mov;";
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        if (currentEarningId != resultSet.getInt("e.id_ear")) {
                            if (currentEarningId != 0) {
                                SSscEarning sbcEarning = row.getSbcEarnginById(currentEarningId);
                                sbcEarning.AmountExempt = totalAmountExempt;
                                sbcEarning.AmountTaxed = totalAmountTaxed;
                            }
                            
                            currentEarningId = resultSet.getInt("e.id_ear");
                            totalAmountExempt = 0;
                            totalAmountTaxed = 0;
                        }
                        
                        double amountExempt = 0;
                        double amountTaxed = 0;
                        double ssd = 0;

                        switch (resultSet.getInt("e.fk_tp_ear")) {
                            case SModSysConsts.HRSS_TP_EAR_FOOD:
                                switch (employee.getFkPaymentTypeId()) {
                                    case SModSysConsts.HRSS_TP_PAY_WEE:
                                        if ((resultSet.getDouble("pre.amt_r")) >= foodExemptWeekly) {
                                            amountTaxed = (resultSet.getDouble("pre.amt_r") - foodExemptWeekly);
                                            amountExempt = 0;
                                        }
                                        else {
                                            amountTaxed = 0;
                                            amountExempt = resultSet.getDouble("pre.amt_r");
                                        }
                                        break;
                                        
                                    case SModSysConsts.HRSS_TP_PAY_FOR:
                                        if ((resultSet.getDouble("pre.amt_r")) >= foodExemptFortnightly) {
                                            amountTaxed = resultSet.getDouble("pre.amt_r") - foodExemptFortnightly;
                                            amountExempt = 0;
                                        }
                                        else {
                                            amountTaxed = 0;
                                            amountExempt = resultSet.getDouble("pre.amt_r");
                                        }
                                        break;
                                        
                                    default:
                                }
                                break;
                            case SModSysConsts.HRSS_TP_EAR_SUN_BONUS:
                                switch (employee.getFkPaymentTypeId()) {
                                    case SModSysConsts.HRSS_TP_PAY_WEE:
                                        if ((resultSet.getDouble("pre.amt_r")) >= foodExemptWeekly) {
                                            amountTaxed = (resultSet.getDouble("pre.amt_r") - foodExemptWeekly);
                                            amountExempt = 0;
                                        }
                                        else {
                                            amountTaxed = 0;
                                            amountExempt = resultSet.getDouble("pre.amt_r");
                                        }
                                        break;
                                        
                                    case SModSysConsts.HRSS_TP_PAY_FOR:
                                        if ((resultSet.getDouble("pre.amt_r")) >= foodExemptFortnightly) {
                                            amountTaxed = resultSet.getDouble("pre.amt_r") - foodExemptFortnightly;
                                            amountExempt = 0;
                                        }
                                        else {
                                            amountTaxed = 0;
                                            amountExempt = resultSet.getDouble("pre.amt_r");
                                        }
                                        break;
                                        
                                    default:
                                }
                                break;
                            case SModSysConsts.HRSS_TP_EAR_OVER_TIME: //Tiempo extra
                                switch (employee.getFkPaymentTypeId()) {
                                    case SModSysConsts.HRSS_TP_PAY_WEE:
                                        if ((((resultSet.getDouble("pr.pay_hr_r") * (resultSet.getDouble("emp.wrk_hrs_day")) * SHrsConsts.WEEK_DAYS ) + ( OVERTIME_EXEMPT_DAYS_WEEK * resultSet.getDouble("pre.amt_r"))) > 
                                                    ((resultSet.getDouble("pr.pay_hr_r") * (resultSet.getDouble("emp.wrk_hrs_day")) * SHrsConsts.WEEK_DAYS )) +  resultSet.getDouble("pre.amt_r"))) {
                                                amountExempt = resultSet.getDouble("pre.amt_r");
                                                amountTaxed = 0;
                                        }
                                        else {
                                            ssd = ((resultSet.getDouble("pr.pay_hr_r") * (resultSet.getDouble("emp.wrk_hrs_day")) * SHrsConsts.WEEK_DAYS ) + ( OVERTIME_EXEMPT_DAYS_WEEK * resultSet.getDouble("pre.amt_r"))) -
                                                    ((resultSet.getDouble("pr.pay_hr_r") * (resultSet.getDouble("emp.wrk_hrs_day")) * SHrsConsts.WEEK_DAYS )) +  resultSet.getDouble("pre.amt_r");
                                            amountExempt = (ssd - overtimeExemptWeekly);
                                            amountTaxed = resultSet.getDouble("pre.amt_r") - amountExempt;
                                        }
                                        break;

                                    case SModSysConsts.HRSS_TP_PAY_FOR:
                                        if ((((resultSet.getDouble("pr.pay_hr_r") * (resultSet.getDouble("emp.wrk_hrs_day")) * SHrsConsts.FORTNIGHT_FIXED_DAYS ) + ( OVERTIME_EXEMPT_DAYS_FORT * resultSet.getDouble("pre.amt_r"))) > 
                                                    ((resultSet.getDouble("pr.pay_hr_r") * (resultSet.getDouble("emp.wrk_hrs_day")) * SHrsConsts.FORTNIGHT_FIXED_DAYS )) +  resultSet.getDouble("pre.amt_r"))) {
                                                amountExempt = resultSet.getDouble("pre.amt_r");
                                                amountTaxed = 0;
                                        }
                                        else {
                                            ssd = ((resultSet.getDouble("pr.pay_hr_r") * (resultSet.getDouble("emp.wrk_hrs_day")) * SHrsConsts.FORTNIGHT_FIXED_DAYS ) + ( OVERTIME_EXEMPT_DAYS_FORT * resultSet.getDouble("pre.amt_r"))) -
                                                    ((resultSet.getDouble("pr.pay_hr_r") * (resultSet.getDouble("emp.wrk_hrs_day")) * SHrsConsts.FORTNIGHT_FIXED_DAYS )) +  resultSet.getDouble("pre.amt_r");
                                            amountExempt = (ssd - overtimeExemptFortnightly);
                                            amountTaxed = resultSet.getDouble("pre.amt_r") - amountExempt;
                                        }
                                    break;
                                default:
                                }
                                break;
                            default:
                                amountExempt = 0;
                                amountTaxed = resultSet.getDouble("pre.amt_r");
                        }            

                        totalAmountExempt = SLibUtils.roundAmount(totalAmountExempt + amountExempt);
                        totalAmountTaxed = SLibUtils.roundAmount(totalAmountTaxed + amountTaxed);
                    }
                    
                    if (currentEarningId != 0) {
                        SSscEarning sbcEarning = row.getSbcEarnginById(currentEarningId);
                        sbcEarning.AmountExempt = totalAmountExempt;
                        sbcEarning.AmountTaxed = totalAmountTaxed;
                    }
                    
                    // procesar los días de ausencia no pagados al empleado:
                    
                    int absenceEffectiveDays = 0;
                      
                    sql = "SELECT a.dt_sta, a.dt_end, a.eff_day "
                        + "FROM hrs_abs AS a "
                        + "INNER JOIN erp.hrsu_tp_abs AS ta ON ta.id_cl_abs = a.fk_cl_abs AND ta.id_tp_abs = a.fk_tp_abs " 
                        + "WHERE ((a.dt_sta BETWEEN '" + dateStart + "' AND '" + dateEnd + "') OR (a.dt_end BETWEEN '" + dateStart + "' AND '" + dateEnd + "') OR (a.dt_sta < '" + dateStart + "' AND a.dt_end > '" + dateEnd + "')) AND "
                        + "NOT ta.b_pay AND NOT a.b_del AND a.id_emp = " + employee.getPkEmployeeId() + " "
                        + "ORDER BY a.dt_sta, a.id_abs;";
                        try (ResultSet resultSet1 = statement.executeQuery(sql)) {
                            while (resultSet1.next()) {
                                Date absenceStart = resultSet1.getDate("a.dt_sta");
                                Date absenceEnd = resultSet1.getDate("a.dt_end");
                                int absenceCase = 0;
                                int absenceCalendarDaysInPeriod = 0;
                                
                                if (absenceStart.before(periodStart) && !absenceEnd.after(periodEnd)) {
                                    //caso 1. Explicar brevemente qué es este caso:
                                    //Las fechas de incidencias del empleado inician ANTES del bimestre indicado y terminan DENTRO.

                                    absenceCase = 1;
                                    absenceCalendarDaysInPeriod = SLibTimeUtils.countPeriodDays(periodStart, absenceEnd);
                                }
                                else if (absenceStart.before(periodStart) && absenceEnd.after(periodEnd)) {
                                    //caso 2. Explicar brevemente qué es este caso:
                                    //Las fechas de incidencias del empleado inician ANTES bimestre indicado y terminan DESPUES.
                                    absenceCase = 2;
                                    absenceCalendarDaysInPeriod = SLibTimeUtils.countPeriodDays(periodStart, periodEnd);
                                }
                                else if (!absenceStart.before(periodStart) && !absenceEnd.after(periodEnd)) {
                                    //caso 3. Explicar brevemente qué es este caso:
                                    //Las fechas de incidencias del empleado inician en el bimestre indicado y terminan DEMTRO.
                                    absenceCase = 3;
                                    absenceCalendarDaysInPeriod = SLibTimeUtils.countPeriodDays(absenceStart, absenceEnd);
                                }
                                else if (!absenceStart.before(periodStart) && absenceEnd.after(periodEnd)) {
                                    //caso 4. Explicar brevemente qué es este caso:
                                    //Las fechas de incidencias del empleado inician en el bimestre indicado y terminan DESPUES.
                                    absenceCase = 4;
                                    absenceCalendarDaysInPeriod = SLibTimeUtils.countPeriodDays(absenceStart, periodEnd);
                                }
                                else {
                                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                                }
                                
                                // Porcentaje de los dias que caen 
                                int absenceCalendarDays = SLibTimeUtils.countPeriodDays(absenceStart, absenceEnd);
                                double calendarDaysRate = resultSet1.getDouble("a.eff_day") * (absenceCalendarDays == 0 ? 0 : ((double) absenceCalendarDaysInPeriod / absenceCalendarDays));
                                int effectiveDaysInPeriod = 0;
                                
                                switch (absenceCase) {
                                    case 1:
                                    case 2:
                                        effectiveDaysInPeriod = (int) Math.ceil(calendarDaysRate);
                                        break;
                                    case 3:
                                        effectiveDaysInPeriod = (int) calendarDaysRate;
                                        break;
                                    case 4:
                                        effectiveDaysInPeriod = (int) Math.floor(calendarDaysRate);
                                        break;
                                    default:
                                }
                                
                                absenceEffectiveDays += effectiveDaysInPeriod;
                            }
                        }
                        
                        row.setAbsenceEffectiveDaysSuggested(absenceEffectiveDays);
                }

                rows.add(row);
            }
        }
        
        return rows;
    }
    
    public static double getEmployeeDailyIncome(final SGuiSession session, final int year, final int monthStart, final int monthEnd, final int typePay, final int idEmp) throws SQLException, Exception {
        ArrayList<Double> rowsS = new ArrayList<>();
        int mnYearPay = year;
        int mnMonthStartPay = monthStart;
        int mnMonthEndPay = monthEnd;
        int mnTypePaypal = typePay;
        int mnIdEmp = idEmp;
        double DailyIncome = 0.0;
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            ArrayList<Integer> payrollReceipts = getEmployeeRecentPayroll(session, mnYearPay, mnMonthStartPay,mnMonthEndPay, mnTypePaypal);
            for (int i = 0; i < payrollReceipts.size()-1; i++) {
                String sql = "SELECT pay_day_r FROM HRS_PAY_RCP WHERE id_emp = " + mnIdEmp + " AND id_pay = " + payrollReceipts.get(i) + " ; " ;
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                     rowsS.add(resultSet.getDouble("pay_day_r"));
                }        
                ArrayList<Double> rowsSSecond = rowsS;
                for (int x = 0; x < rowsS.size()- 1; x++) {
                    if (!rowsS.get(x).equals(rowsSSecond.get(1))) {
                       DailyIncome = 0.0;
                       x = rowsS.size()-1;
                    } 
                    else {
                       DailyIncome = rowsS.get(1);
                    }
                }
            }
        }
        
        return DailyIncome;
    }
    
    public static ArrayList<Integer> getEmployeeRecentPayroll(final SGuiSession session, final int year, final int monthStart, final int monthEnd, final int typePay) throws Exception {
        ArrayList<Integer> rows = new ArrayList<>();
        
        String sql = "SELECT id_pay from hrs_pay where fis_year= " + year + " and per_year = " + year + " " +
                        "AND per >= " + monthStart + " AND per <= " + monthEnd + " " +
                        "AND fk_tp_pay_sht = 1 " +
                        "AND fk_tp_pay = " + typePay + " " +
                        "AND NOT b_del; ";
                try (Statement statement = session.getStatement().getConnection().createStatement()) {
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    rows.add(resultSet.getInt("id_pay"));
                }
            }
                
            return rows;
    }
    
    
    public static int daysCalendarPeriod(final int year, final int month) {
        int daysCalendarPeriod = 0;
        int numDaysMonthNotLeap = 28;
        int numDaysMonthLeap = 29;
        int numDaysMonthS = 30;
        int numDaysMonthL = 31;
 
        switch(month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysCalendarPeriod = numDaysMonthL;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysCalendarPeriod = numDaysMonthS;
                break;
            case 2:
                if(leapYear(year)) {
                    daysCalendarPeriod = numDaysMonthLeap;
                } else {
                    daysCalendarPeriod = numDaysMonthNotLeap;
                }
                break;
        }
        return daysCalendarPeriod;
    }
    
    public static boolean leapYear (final int year) {
        GregorianCalendar calendar = new GregorianCalendar();
        boolean bLeapYear = false;
        
        if (calendar.isLeapYear(year)) {
            bLeapYear = true;
        }
        return bLeapYear;
    }
    
     public static int getEmployeeAntiquity(final Date dateBenefits, final Date dateCutoff) {
       String yearAnti = String.valueOf(SHrsUtils.getEmployeeSeniority(dateBenefits, dateCutoff));
       String monthAnti = String.valueOf(SHrsUtils.getEmployeeSeniorityAntMonth(dateBenefits, dateCutoff));
       String antiquityCompleteResult = yearAnti + "." + monthAnti;
       int antiquityComplete = (int)(Math.round(Math.ceil(Double.parseDouble(antiquityCompleteResult))));
       
       return antiquityComplete;
    }
}
