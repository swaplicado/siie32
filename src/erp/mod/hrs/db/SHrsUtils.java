/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.cfd.SCfdConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STableUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mhrs.data.SDataPayrollReceiptIssue;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.print.SDataConstantsPrint;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public abstract class SHrsUtils {

    public static void createLayoutBanBajioPayroll(SGuiClient client, int payrollId, java.lang.String title, Date dateApplication, String accountDebit, int consecutiveDay) {
        ResultSet resultSet = null;
        String sql = "";
        String fileName = "";
        int nMoveNum = 2;
        int nMoveNumTotal = 0;
        int n = 0;
        int m = 0;
        int employeeId = 0;
        int dayFileName = 0;
        int monthFileName = 0;
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String leyend = "";
        java.lang.String buffer = "";
        DecimalFormat formatDesc = new DecimalFormat("0000000000000.00");
        DecimalFormat formatDescTotal = new DecimalFormat("0000000000000000.00");
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
        double mdBalance = 0;
        double mdBalanceTotal = 0;
        SDbConfig config = null;
        
        config = (SDbConfig) client.getSession().readRegistry(SModConsts.HRS_CFG, new int[] { SUtilConsts.BPR_CO_ID });
        dayFileName = SLibTimeUtils.digestDate(dateApplication)[2];
        monthFileName = SLibTimeUtils.digestDate(dateApplication)[1];
        n = (int) (Math.floor(Math.log10(consecutiveDay)) + 1);
        m = (int) (Math.floor(Math.log10(dayFileName)) + 1);
        
        fileName = "D" + config.getBajioAffinityGroup() + SLibUtilities.textRepeat("0", 2 - n).concat(consecutiveDay + "") + (monthFileName < 10 ? "0." + monthFileName : "1." + (monthFileName - 10)) + SLibUtilities.textRepeat("0", 2 - m).concat(dayFileName + "")+ "";

        client.getFileChooser().setSelectedFile(new File(fileName));
        if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());

            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ASCII"));
                
                sAccountDebit = SLibUtilities.textTrim(accountDebit);
                
                buffer += "01";
                buffer += "0000001";
                buffer += "030";
                buffer += "S";
                buffer += "90";
                buffer += "0";
                buffer += SLibUtilities.textRepeat("0", (config.getBajioAffinityGroup().length() >= 7 ? 0 : 7 - config.getBajioAffinityGroup().length())).concat(SLibUtilities.textLeft(config.getBajioAffinityGroup(), 7));
                buffer += formatDate.format(dateApplication);
                buffer += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 20 ? 0 : 20 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 20)); // Debit acccount
                buffer += SLibUtilities.textRepeat(" ", 130); // FILLER USE FUTURE BANK

                bw.write(buffer);
                bw.newLine();
                
                sql = "SELECT rcp.id_emp, rcp.pay_r, emp.bank_acc " +
                        "FROM hrs_pay_rcp AS rcp " +
                        "INNER JOIN erp.hrsu_emp AS emp ON emp.id_emp = rcp.id_emp " +
                        "WHERE rcp.b_del = 0 AND LENGTH(emp.bank_acc) > 0 AND rcp.id_pay = " + payrollId + " ";
                resultSet = client.getSession().getStatement().executeQuery(sql);
                while (resultSet.next()) {
                    buffer = "";

                    sAccountCredit = SLibUtilities.textTrim(resultSet.getString("emp.bank_acc"));
                    leyend = "DEPOSITO DE NOMINA";
                    employeeId = resultSet.getInt("rcp.id_emp");
                    mdBalance = resultSet.getDouble("rcp.pay_r");
                    mdBalanceTotal += mdBalance;

                    n = (int) (Math.floor(Math.log10(nMoveNum)) + 1);
                    m = (int) (Math.floor(Math.log10(employeeId)) + 1);
                    
                    buffer += "02";
                    buffer += SLibUtilities.textRepeat("0", 7 - n).concat(nMoveNum++ + "");
                    buffer += "90";
                    buffer += formatDate.format(dateApplication);
                    buffer += SLibUtilities.textRepeat("0", 3); // FILLER
                    buffer += "030";
                    buffer += formatDesc.format(mdBalance).replace(".", "");
                    buffer += formatDate.format(dateApplication);
                    buffer += SLibUtilities.textRepeat("0", 2); // FILLER
                    buffer += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 20 ? 0 : 20 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 20)); // Debit acccount
                    buffer += " ";
                    buffer += SLibUtilities.textRepeat("0", 2); // FILLER
                    buffer += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 20 ? 0 : 20 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 20)); // Credit account
                    buffer += " ";
                    buffer += SLibUtilities.textRepeat("0", 7 - m).concat(employeeId + "");
                    buffer += leyend.concat(SLibUtilities.textRepeat(" ", (40 - leyend.length()))); // Leyend
                    buffer += SLibUtilities.textRepeat("0", 30); // NUMBER TARJETA
                    buffer += SLibUtilities.textRepeat("0", 10); // FILLER
                    
                    bw.write(buffer);
                    bw.newLine();
                    
                    nMoveNumTotal++;
                }

                // Summary
                buffer = "";
                n = (int) (Math.floor(Math.log10(nMoveNum)) + 1);
                m = (int) (Math.floor(Math.log10(nMoveNumTotal)) + 1);

                buffer += "09";
                buffer += SLibUtilities.textRepeat("0", 7 - n).concat(nMoveNum + "");
                buffer += "90";
                buffer += SLibUtilities.textRepeat("0", 7 - m).concat(nMoveNumTotal + "");
                buffer += formatDescTotal.format(mdBalanceTotal).replace(".", "");
                buffer += SLibUtilities.textRepeat(" ", 145); // FILLER USE FUTURE BANK

                bw.write(buffer);
                bw.newLine();
                bw.flush();
                bw.close();

                if (client.showMsgBoxConfirm(SLibConstants.MSG_INF_FILE_CREATE + file.getPath() + "\n" + SLibConstants.MSG_CNF_FILE_OPEN) == JOptionPane.YES_OPTION) {
                    SLibUtilities.launchFile(file.getPath());
                }
            }
            catch (java.lang.Exception e) {
                SLibUtilities.renderException(STableUtilities.class.getName(), e);
            }
        }
    }
    
    /**
     * @param session User GUI session.
     * @param year Payroll year.
     * @return First day of year set by user on module configuration, otherwise January 1st of desired year.
     */
    public static Date getFirstDayYear(final SGuiSession session, final int year) {
        SDbFirstDayYear firstDayYear = (SDbFirstDayYear) session.readRegistry(SModConsts.HRS_FDY, new int[] { year }, SDbConsts.MODE_STEALTH);
        return firstDayYear == null || firstDayYear.isRegistryNew() ? SLibTimeUtils.createDate(year, 1, 1) : firstDayYear.getFirstDayYear();
    }

    /**
     * @param session User GUI session.
     * @param year Payroll year.
     * @param paymentType Payroll payment type (constants defined in class <code>SModSysConsts</code>, HRSS_TP_PAY).
     * @return Payroll next number of provided payment type.
     */
    public static int getPayrollNextNumber(final SGuiSession session, final int year, final int paymentType) throws Exception {
        int nextNumber = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COALESCE(MAX(num), 0) + 1 "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " "
                + "WHERE per_year = " + year + " AND fk_tp_pay = " + paymentType + " AND b_del = 0;";

        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            nextNumber = resultSet.getInt(1);
        }

        return validatePayrollNumber(session, year, nextNumber, paymentType);
    }
    
    public static int getPayrollReceiptNextNumber(final SGuiSession session, final String series) throws Exception {
        int number = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT MAX(CONVERT(num, UNSIGNED INTEGER)) + 1 AS f_num "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " "
                + "WHERE num_ser = '" + series + "' AND b_del = 0 ";

        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            if (resultSet.getObject("f_num") != null) {
                number = resultSet.getInt("f_num");
            }
            else {
                number = 1;
            }
        }

        return number;
    }

    public static ArrayList<SDataPayrollReceiptIssue> getPayrollReceiptIssues(final SGuiSession session, final int[] payrollKey) throws Exception {
        String sql = "";
        ResultSet resultSet = null;
        ArrayList<SDataPayrollReceiptIssue> receiptIssues = null;
        SDataPayrollReceiptIssue receiptIssue = null;

        receiptIssues = new ArrayList<SDataPayrollReceiptIssue>();

        sql = "SELECT id_pay, id_emp, id_iss " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " " + 
                "WHERE id_pay = " + payrollKey[0] + " AND b_del = 0 AND fk_st_rcp = "  + SModSysConsts.TRNS_ST_DPS_EMITED + " ORDER BY id_iss ";
        
        resultSet = session.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            receiptIssue = new SDataPayrollReceiptIssue();
            
            if (receiptIssue.read(new int[] { resultSet.getInt("id_pay"), resultSet.getInt("id_emp"), resultSet.getInt("id_iss") }, session.getStatement().getConnection().createStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
            receiptIssues.add(receiptIssue);
        }

        return receiptIssues;
    }

    /**
     * @param session User GUI session.
     * @param year Payroll year.
     * @param number Payroll number to validate.
     * @param paymentType Payroll payment type (constants defined in class <code>SModSysConsts</code>, HRSS_TP_PAY).
     * @return Valid payroll number, that is, same provided payroll number if valid or maximum valid payroll number of provided payment type.
     */
    public static int validatePayrollNumber(final SGuiSession session, final int year, final int number, final int paymentType) throws Exception {
        int validNumber = 0;
        Date lastPeriod[] = null;

        if (number <= 1) {
            validNumber = 1;        // 1, 0 or less are set to 1 (please note that payroll number 1, regardless the payment type, does not need to be checked, it is correct by default!)
        }
        else {
            validNumber = number;   // assume by instance that number can be correct

            switch(paymentType) {
                case SModSysConsts.HRSS_TP_PAY_WEE:
                    if (number > SHrsConsts.YEAR_WEEKS) {
                        if (SLibTimeUtils.isSameDate(getFirstDayYear(session, year), SLibTimeUtils.createDate(year - 1, 12, 26))) {
                            // If FDY is minimum FDY and year is leap-year, then there are up to 54 weeks
                            // If FDY is minimum FDY and year is ordinary-year, then there are up to 53 weeks

                            validNumber = SHrsConsts.YEAR_WEEKS + (SLibTimeUtils.isLeapYear(year) ? 2 : 1);
                        }
                        else {
                            // If FDY is greater than minimum FDY, check if year has up to 52 or 53 weeks

                            lastPeriod = getPayrollPeriod(session, year, SHrsConsts.YEAR_WEEKS + 1, paymentType);
                            validNumber = SHrsConsts.YEAR_WEEKS + (SLibTimeUtils.digestYear(lastPeriod[1])[0] == year ? 1 : 0);
                        }
                    }
                    break;

                case SModSysConsts.HRSS_TP_PAY_FOR:
                    if (number > SHrsConsts.YEAR_FORNIGHTS) {
                        validNumber = SHrsConsts.YEAR_FORNIGHTS;
                    }
                    break;

                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }

        return validNumber;
    }
    
    /**
     * Validate date start and date end of payroll versus start operations date of company. 
     * @param config Object with information of configuration the module resource human
     * @param dateStart Date start of payroll
     * @param dateEnd Date end of payroll
     * @return true if is valid
     * @throws Exception 
     */
    public static boolean validatePayrollDate(final SDbConfig config, final Date dateStart, final Date dateEnd) throws Exception {
        
        if (config == null) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Configuración del módulo)");
        }
        else if (dateStart.compareTo(config.getDateOperations()) < 0) {
            throw new Exception("La fecha inicial de la nómina '" + SLibUtils.DateFormatDate.format(dateStart) + "', debe ser posterior o igual a la fecha de inicio de operaciones de la empresa '" + SLibUtils.DateFormatDate.format(config.getDateOperations()) + "'.");
        }
        else if (dateEnd.compareTo(config.getDateOperations()) < 0) {
            throw new Exception("La fecha final de la nómina '" + SLibUtils.DateFormatDate.format(dateEnd) + "', debe ser posterior o igual a la fecha de inicio de operaciones de la empresa '" + SLibUtils.DateFormatDate.format(config.getDateOperations()) + "'.");
        }
        return true;
    }
    
    /**
     * Validate information necesary basic for capture payroll.
     * @param session User GUI session.
     * @param config Object with information of configuration the module resource human
     * @param workingDaySettings Configuration of days working
     * @return true if is valid
     * @throws Exception 
     */
    public static boolean validatePayroll(final SGuiSession session, final SDbConfig config, final SDbWorkingDaySettings workingDaySettings) throws Exception {
        SDbDeduction deduction = null;
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        
        if (config == null) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Configuración del módulo)");
        }
        else if (workingDaySettings == null) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Configuración de días laborables)");
        }
        else if (config.getFkEarningEarningId_n() == SLibConsts.UNDEFINED) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración percepción normal)");
        }
        else if (config.getFkEarningVacationsId_n() == SLibConsts.UNDEFINED) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración vacaciones)");
        }
        else if (config.getFkEarningTaxSubsidyId_n() == SLibConsts.UNDEFINED) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración subsidio impuesto)");
        }
        else if (config.getFkDeductionTaxId_n() == SLibConsts.UNDEFINED) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración impuesto)");
        }
        else if (config.getFkDeductionSsContributionId_n() == SLibConsts.UNDEFINED) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración SS)");
        }
        
        statement = session.getStatement().getConnection().createStatement();
        sql = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " "
                + "WHERE b_del = 0 AND fk_tp_loan <> " + SModSysConsts.HRSS_TP_LOAN_NON + " "
                + "ORDER BY CONCAT(code, ' - ', name), id_ded ";

        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            deduction = new SDbDeduction();
            deduction.read(session, new int[] { resultSet.getInt("id_ded") });;
        }
        
        sql = "SELECT id_emp, id_loan " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_LOAN) + " " +
            "WHERE b_del = 0 AND b_clo = 0;";
        
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            if (deduction == null) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Deducción tipo crédito/préstamo)");
            }
        }
        
        return true;
    }
    
    public static boolean validateHireDismissedEmployee(final SGuiSession session, final int employeeId, final boolean isHire) throws Exception {
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COUNT(id_log) AS f_count " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_HIRE) + " " +
            "WHERE b_del = 0 AND id_emp = " + employeeId + " AND dt_dis_n IS NULL ";
        
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            if (isHire && resultSet.getInt("f_count") > 0) {
                throw new Exception("Inconsistencia en la secuencia cronológica de movimientos en la bitácora de altas/bajas,\n existe al menos un movimiento de alta sin su baja correspondiente.");
            }
            else if (!isHire) {
                if (resultSet.getInt("f_count") > 1) {
                    throw new Exception("Inconsistencia en la secuencia cronológica de movimientos en la bitácora de altas/bajas,\n existe más de un movimiento de alta sin su baja correspondiente.");
                }
                else if (resultSet.getInt("f_count") == 0) {
                    throw new Exception("No existe ningún movimiento de alta previo.");
                }
            }
        }
        
        return true;
    }

    /**
     * @param session User GUI session.
     * @param year Payroll year.
     * @param number Payroll number.
     * @param paymentType Payroll payment type (constants defined in class <code>SModSysConsts</code>, HRSS_TP_PAY).
     * @return Default payroll period as an <code>java.util.Date</code> array.
     */
    public static Date[] getPayrollPeriod(final SGuiSession session, final int year, final int number, final int paymentType) throws SQLException, Exception {
        int month = 0;
        Date fdy = null;
        Date start = null;
        Date end = null;

        switch (paymentType) {
            case SModSysConsts.HRSS_TP_PAY_WEE:
                fdy = getFirstDayYear(session, year);
                start = SLibTimeUtils.addDate(fdy, 0, 0, (number - 1) * SHrsConsts.WEEK_DAYS);
                end = SLibTimeUtils.addDate(fdy, 0, 0, number * SHrsConsts.WEEK_DAYS - 1);
                break;

            case SModSysConsts.HRSS_TP_PAY_FOR:
                month = (int) SLibUtils.round(number / 2d, 0);
                if (number % 2 == 1) {
                    start = SLibTimeUtils.createDate(year, month, 1);
                    end = SLibTimeUtils.createDate(year, month, 15);
                }
                else {
                    start = SLibTimeUtils.createDate(year, month, 16);
                    end = SLibTimeUtils.getEndOfMonth(start);
                }
                break;

            default:
        }

        return new Date[] { start, end };
    }
    
    /**
     * 
     * @param session
     * @param paymentType
     * @return
     * @throws Exception 
     */
    public static SDbWorkingDaySettings getPayrollWorkingDaySettings(final SGuiSession session, final int paymentType) throws Exception {
        SDbWorkingDaySettings workingDaySettings = null;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_wds "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_WDS) + " "
                + "WHERE b_del = 0 AND fk_tp_pay = " + paymentType + " ";

        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            workingDaySettings = (SDbWorkingDaySettings) session.readRegistry(SModConsts.HRS_WDS, new int[] { resultSet.getInt(1) });
        }

        return workingDaySettings;
    }
    
    /**
     * 
     * @param dateStart
     * @param dateEnd
     * @param aHolidays
     * @param workingDaySettings
     * @return
     * @throws Exception 
     */
    public static int getEmployeeBusinessDays(final Date dateStart, final Date dateEnd, final ArrayList<SDbHoliday> aHolidays, final  SDbWorkingDaySettings workingDaySettings) throws Exception {
        int businessDays = 0;
        int difDays = 0;
        boolean dayWorked = false;
        Date dateReference = null;
        Calendar calendar = Calendar.getInstance();
        
        difDays = (int) SLibTimeUtils.getDaysDiff(dateEnd, dateStart);
            
        for (int i = 0; i <= difDays; i++) {
            dateReference = SLibTimeUtils.addDate(dateStart, 0, 0, i);
            dayWorked = true;

            // Is working day:

            calendar.setTime(dateReference);

            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    dayWorked = workingDaySettings.isWeekdaySettingSunday();
                    break;
                case 2:
                    dayWorked = workingDaySettings.isWeekdaySettingMonday();
                    break;
                case 3:
                    dayWorked = workingDaySettings.isWeekdaySettingTuesday();
                    break;
                case 4:
                    dayWorked = workingDaySettings.isWeekdaySettingWednesday();
                    break;
                case 5:
                    dayWorked = workingDaySettings.isWeekdaySettingThursday();
                    break;
                case 6:
                    dayWorked = workingDaySettings.isWeekdaySettingFriday();
                    break;
                case 7:
                    dayWorked = workingDaySettings.isWeekdaySettingSaturday();
                    break;
                default:
            }

            // Is holiday:

            if (dayWorked) {
                for (SDbHoliday holiday : aHolidays) {
                    if (holiday.getDate().compareTo(dateReference) == 0) {
                        dayWorked = false;
                        break;
                    }
                }
            }

            if (dayWorked) {
                businessDays ++;
            }
        }
            
        return businessDays;
    }

    /**
     * Get earning by provided code
     * @param client SGuiClient
     * @param codeToFind Code of desired earning
     * @return Object Earning
     * @throws Exception
     */
    public static SDbEarning getEarning(final SGuiClient client, final String codeToFind) throws Exception {
        SDbEarning earning = null;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_ear " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " " +
                "WHERE code = '" + codeToFind + "' ORDER BY code, id_ear LIMIT 1 ";
        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            earning = new SDbEarning();
            earning.read(client.getSession(), new int[] { resultSet.getInt("id_ear") });
        }

        return earning;
    }

    /**
     * Get deduction by provided code
     * @param client SGuiClient
     * @param codeToFind Code of desired deduction
     * @return Object Deduction
     * @throws Exception
     */
    public static SDbDeduction getDeduction(final SGuiClient client, final String codeToFind) throws Exception {
        SDbDeduction deduction = null;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_ded " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " " +
                "WHERE code = '" + codeToFind + "' ORDER BY code, id_ded LIMIT 1 ";
        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            deduction = new SDbDeduction();
            deduction.read(client.getSession(), new int[] { resultSet.getInt("id_ded") });
        }

        return deduction;
    }
    
    /**
     * Get earning by provided type ID.
     * @param client SGuiClient
     * @param type type filter options: SModConsts.HRSS_TP_BEN or SModConsts.HRSS_TP_LOAN
     * @param typeId ID the type indicated.
     * @return Object Earning
     * @throws Exception
     */
    public static SDbEarning getEarningByType(final SGuiClient client, final int type, final int typeId) throws Exception {
        SDbEarning earning = null;
        String sql = "";
        String sqlType = "";
        ResultSet resultSet = null;
        
        switch (type) {
            case SModConsts.HRSS_TP_BEN:
                sqlType = "fk_tp_ben = " + typeId;
                break;
            case SModConsts.HRSS_TP_LOAN:
                sqlType = "fk_tp_loan = " + typeId;
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        sql = "SELECT id_ear " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " " +
                "WHERE " + sqlType + " ORDER BY code, name, id_ear LIMIT 1 ";
        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            earning = new SDbEarning();
            earning.read(client.getSession(), new int[] { resultSet.getInt("id_ear") });
        }

        return earning;
    }

    /**
     * Get deduction by provided type ID.
     * @param client SGuiClient
     * @param type type filter options: SModConsts.HRSS_TP_BEN or SModConsts.HRSS_TP_LOAN
     * @param typeId ID the type indicated.
     * @return Object Deduction
     * @throws Exception
     */
    public static SDbDeduction getDeductionByType(final SGuiClient client, final int type, final int typeId) throws Exception {
        SDbDeduction deduction = null;
        String sql = "";
        String sqlType = "";
        ResultSet resultSet = null;
        
        switch (type) {
            case SModConsts.HRSS_TP_BEN:
                sqlType = "fk_tp_ben = " + typeId;
                break;
            case SModConsts.HRSS_TP_LOAN:
                sqlType = "fk_tp_loan = " + typeId;
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        sql = "SELECT id_ded " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " " +
                "WHERE " + sqlType + " ORDER BY code, name, id_ded LIMIT 1 ";
        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            deduction = new SDbDeduction();
            deduction.read(client.getSession(), new int[] { resultSet.getInt("id_ded") });
        }

        return deduction;
    }
    
    public static SDbBenefitTable getBenefitTableByEarning(final SGuiSession session, final int earningId, final int paymentType, final Date dateCut) throws Exception {
        SDbBenefitTable benefitTable = null;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_ben "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " "
            + "WHERE b_del = 0 AND fk_ear = " + earningId + " AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(dateCut) + "' "
            + (paymentType == SLibConsts.UNDEFINED ? "AND fk_tp_pay_n IS NULL" : "AND (fk_tp_pay_n IS NULL OR fk_tp_pay_n = " + paymentType + ")") + " "
            + "ORDER BY dt_sta DESC, id_ben "
            + "LIMIT 1;";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            benefitTable = new SDbBenefitTable();
            benefitTable.read(session, new int[] { resultSet.getInt("id_ben") });
        }

        return benefitTable;
    }
    
    public static SDbBenefitTable getBenefitTableByDeduction(final SGuiSession session, final int deductionId, final Date dateCut) throws Exception {
        SDbBenefitTable benefitTable = null;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_ben "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " "
            + "WHERE b_del = 0 AND fk_ded_n = " + deductionId + " AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(dateCut) + "' "
            + "ORDER BY dt_sta DESC, id_ben "
            + "LIMIT 1;";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            benefitTable = new SDbBenefitTable();
            benefitTable.read(session, new int[] { resultSet.getInt("id_ben") });
        }

        return benefitTable;
    }
    
    public static ArrayList<SHrsBenefitTableByAnniversary> getBenefitTablesAnniversarys(ArrayList<SDbBenefitTable> benefitTables) throws Exception {
        int i = 0;
        ArrayList<SHrsBenefitTableByAnniversary> aBenefitTableByAnniversary = new ArrayList<SHrsBenefitTableByAnniversary>();
        
        for (SDbBenefitTable table : benefitTables) {
            i = 1;
            for (SDbBenefitTableRow tableRow : table.getChildRows()) {
                while ( i * 12 <= tableRow.getMonths()) {
                    aBenefitTableByAnniversary.add(new SHrsBenefitTableByAnniversary(table.getPkBenefitId(), i, table.getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_VAC_BON ? tableRow.getBenefitBonusPercentage() : tableRow.getBenefitDays()));
                    i++;
                }
            }
        }

        return aBenefitTableByAnniversary;
    }

    /**
     * Get recent tax table
     * @param session SGuiSession
     * @param start Date start date
     * @return id_tax int
     * @throws Exception
     */
    public static int getRecentTaxTable(final  SGuiSession session, final Date start) throws SQLException, Exception {
        int id_tax = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_tax "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_TAX) + " "
            + "WHERE b_del = 0 AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(start) + "' "
            + "ORDER BY dt_sta DESC, id_tax "
            + "LIMIT 1;";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            id_tax = resultSet.getInt(1);
        }

        return id_tax;
    }

    /**
     * Get recent tax subsidy table
     * @param session SGuiSession
     * @param start Date start date
     * @return id_tax_sub int
     * @throws Exception
     */
    public static int getRecentTaxSubsidyTable(final  SGuiSession session, final Date start) throws SQLException, Exception {
        int id_tax_sub = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_tax_sub "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_TAX_SUB) + " "
            + "WHERE b_del = 0 AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(start) + "' "
            + "ORDER BY dt_sta DESC, id_tax_sub "
            + "LIMIT 1;";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            id_tax_sub = resultSet.getInt(1);
        }

        return id_tax_sub;
    }

    /**
     * Get recent SS contribution table
     * @param session SGuiSession
     * @param start Date start date
     * @return id_ss int
     * @throws Exception
     */
    public static int getRecentSsContributionTable(final  SGuiSession session, final Date start) throws SQLException, Exception {
        int id_ss = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_ssc "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_SSC) + " "
            + "WHERE b_del = 0 AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(start) + "' "
            + "ORDER BY dt_sta DESC, id_ssc "
            + "LIMIT 1;";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            id_ss = resultSet.getInt(1);
        }

        return id_ss;
    }
    
    public static int getRecentBenefitTable(final SGuiSession session, final int benefitType, final int paymentType, final Date dateCut) throws Exception {
        int id_ben = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_ben "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " "
            + "WHERE b_del = 0 AND fk_tp_ben = " + benefitType + " AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(dateCut) + "' "
            + (paymentType == SLibConsts.UNDEFINED ? "AND fk_tp_pay_n IS NULL" : "AND (fk_tp_pay_n IS NULL OR fk_tp_pay_n = " + paymentType + ")") + " "
            + "ORDER BY dt_sta DESC, id_ben "
            + "LIMIT 1;";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            id_ben = resultSet.getInt(1);
        }

        return id_ben;
    }

    /**
     * Get recent minimum wage zone
     * @param session SGuiSession
     * @param start Date start date
     * @return wage double
     * @throws Exception
     */
    public static double getRecentMwz(final  SGuiSession session, final int idMwzType, final Date start) throws SQLException, Exception {
        double wage = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT wage "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_MWZ_WAGE) + " "
            + "WHERE b_del = 0 AND id_tp_mwz = " + idMwzType + " AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(start) + "' "
            + "ORDER BY dt_sta DESC, id_tp_mwz, id_wage "
            + "LIMIT 1;";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            wage = resultSet.getDouble(1);
        }

        return wage;
    }

    /**
     * Print payrroll receipt
     * @param client Interface Client
     * @param pnPrintMode print mode (e.g. SDataConstantsPrint.PRINT_MODE_)
     * @param payrollKey payrroll key
     * @throws java.lang.Exception
     */
    public static void printPayrollReceipt(final SGuiClient client, final int pnPrintMode, final int[] payrollKey) throws java.lang.Exception {
        double dTotalPercepciones = 0;
        double dTotalDeducciones = 0;
        SDbLoan loan = null;
        SDataBizPartner bizPartnerCompany = null;
        SDataBizPartner bizPartnerEmployee = null;
        SDbPayrollReceipt payrollReceipt = null;
        SDbPayroll payroll = null;
        HashMap<String, Object> map = null;
        ArrayList aPercepciones = null;
        ArrayList aDeducciones = null;
        DecimalFormat oFixedFormatAux = null;

        payroll = new SDbPayroll();
        payroll.read(client.getSession(), new int[] { payrollKey[0] });

        for (SDbPayrollReceipt receipt : payroll.getChildPayrollReceipts()) {
            if (SLibUtils.compareKeys(receipt.getPrimaryKey(), new int[] { payrollKey[0], payrollKey[1] })) {
                payrollReceipt = receipt;
            }
        }

        bizPartnerCompany = new SDataBizPartner();
        bizPartnerCompany.read(new int[] { ((SClientInterface) client).getSessionXXX().getCompany().getPkCompanyId() }, client.getSession().getStatement());

        bizPartnerEmployee = new SDataBizPartner();
        bizPartnerEmployee.read(new int[] { payrollReceipt.getPkEmployeeId() }, client.getSession().getStatement());

        oFixedFormatAux = new DecimalFormat(SLibUtils.textRepeat("0", 2));

        map = client.createReportParams();

        map.put("bIsReceipt", true);

        map.put("sEmiRfc", bizPartnerCompany.getFiscalId());
        map.put("sRecRfc", bizPartnerEmployee.getFiscalId());
        map.put("ReceptorNombre", bizPartnerEmployee.getBizPartner());
        map.put("nPayrrollId", payrollReceipt.getPkPayrollId());
        map.put("nEmployeeId", payrollReceipt.getPkEmployeeId());
        map.put("NominaNumTipo", payroll.getNumber() + "  " + (String) client.getSession().readField(SModConsts.HRSS_TP_PAY, new int[] { payroll.getFkPaymentTypeId() }, SDbRegistry.FIELD_NAME));
        map.put("NominaFolio", "");

        SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        map.put("RegistroPatronal", ((SClientInterface) client).getSessionXXX().getParamsCompany().getRegistrySs());
        map.put("CURP", bizPartnerEmployee.getAlternativeId());
        map.put("NumEmpleado", bizPartnerEmployee.getDbmsDataEmployee().getNumber());
        map.put("NumSeguridadSocial", bizPartnerEmployee.getDbmsDataEmployee().getSocialSecurityNumber());
        map.put("FechaInicialPago", oSimpleDateFormat.format(payroll.getDateStart()));
        map.put("FechaFinalPago", oSimpleDateFormat.format(payroll.getDateEnd()));
        map.put("NumDiasNoLaborados", payrollReceipt.getDaysNotWorkedPaid() + payrollReceipt.getDaysNotWorkedNotPaid());
        map.put("NumDiasLaborados", payrollReceipt.getDaysWorked());
        map.put("NumDiasPagados", payrollReceipt.getDaysPaid());
        map.put("Departamento", (String) client.getSession().readField(SModConsts.HRSU_DEP, new int[] { bizPartnerEmployee.getDbmsDataEmployee().getFkDepartmentId() }, SDbRegistry.FIELD_NAME));
        map.put("Puesto", (String) client.getSession().readField(SModConsts.HRSU_POS, new int[] { bizPartnerEmployee.getDbmsDataEmployee().getFkPositionId() }, SDbRegistry.FIELD_NAME));
        map.put("PeriodicidadPago", (String) client.getSession().readField(SModConsts.HRSS_TP_PAY, new int[] { bizPartnerEmployee.getDbmsDataEmployee().getFkPaymentTypeId() }, SDbRegistry.FIELD_NAME));
        //map.put("Sueldo", bizPartnerEmployee.getDbmsDataEmployee().getSalary());
        map.put("Sueldo", payrollReceipt.getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_WEE ? payrollReceipt.getSalary() : ((payrollReceipt.getWage() * SHrsConsts.YEAR_MONTHS) / SHrsConsts.YEAR_DAYS));
        //map.put("SalarioBaseCotApor", bizPartnerEmployee.getDbmsDataEmployee().getSalarySscBase());
        //map.put("SalarioDiarioIntegrado", bizPartnerEmployee.getDbmsDataEmployee().getSalarySscBase());
        map.put("SalarioBaseCotApor", payrollReceipt.getSalarySscBase());
        map.put("SalarioDiarioIntegrado", payrollReceipt.getSalarySscBase());

        map.put("FechaInicioRelLaboral", oSimpleDateFormat.format(bizPartnerEmployee.getDbmsDataEmployee().getDateLastHire()));
        map.put("TipoJornada", bizPartnerEmployee.getDbmsDataEmployee().getWorkingHoursDay() + " hr");

        map.put("TipoEmpleado", (String) client.getSession().readField(SModConsts.HRSU_TP_EMP, new int[] { bizPartnerEmployee.getDbmsDataEmployee().getFkEmployeeTypeId() }, SDbRegistry.FIELD_NAME));
        map.put("TipoSalario", (String) client.getSession().readField(SModConsts.HRSS_TP_SAL, new int[] { bizPartnerEmployee.getDbmsDataEmployee().getFkSalaryTypeId() }, SDbRegistry.FIELD_NAME));

        map.put("Ejercicio", payroll.getPeriodYear() + "-" + oFixedFormatAux.format(payroll.getPeriod()));

        aPercepciones = new ArrayList();
        aDeducciones = new ArrayList();

        dTotalPercepciones = 0;
        dTotalDeducciones = 0;

        // Earnings:

        for (SDbPayrollReceiptEarning receiptEarning : payrollReceipt.getChildPayrollReceiptEarnings()) {
            SDbEarning earning = new SDbEarning();
            loan = null;

            earning.read(client.getSession(),  new int[] { receiptEarning.getFkEarningId() });

            if (receiptEarning.getFkLoanEmployeeId_n() != SLibConsts.UNDEFINED) {
                loan = new SDbLoan();

                loan.read(client.getSession(), new int[] { receiptEarning.getFkLoanEmployeeId_n(), receiptEarning.getFkLoanLoanId_n() });
            }

            aPercepciones.add(earning.getCode());
            aPercepciones.add(earning.getCode());
            aPercepciones.add(earning.getName() + (loan == null ? "" : " (" + loan.getLoanIdentificator() + ") "));
            aPercepciones.add(earning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT ? null : receiptEarning.getUnits());
            aPercepciones.add(earning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT ? null : (String) client.getSession().readField(SModConsts.HRSS_TP_EAR_COMP, new int[] { earning.getFkEarningComputationTypeId() }, SDbRegistry.FIELD_CODE));
            aPercepciones.add(receiptEarning.getAmount_r());
            aPercepciones.add(null);

            dTotalPercepciones += receiptEarning.getAmount_r();
        }

        for (int j = payrollReceipt.getChildPayrollReceiptEarnings().size(); j < 10; j++) {
            aPercepciones.add(null);
            aPercepciones.add(null);
            aPercepciones.add(null);
            aPercepciones.add(null);
            aPercepciones.add(null);
            aPercepciones.add(null);
            aPercepciones.add(null);
        }

        // Deductions:

        for (SDbPayrollReceiptDeduction receiptDeduction : payrollReceipt.getChildPayrollReceiptDeductions()) {
            SDbDeduction deduction = new SDbDeduction();
            loan = null;

            deduction.read(client.getSession(),  new int[] { receiptDeduction.getFkDeductionId() });

            if (receiptDeduction.getFkLoanEmployeeId_n() != SLibConsts.UNDEFINED) {
                loan = new SDbLoan();

                loan.read(client.getSession(), new int[] { receiptDeduction.getFkLoanEmployeeId_n(), receiptDeduction.getFkLoanLoanId_n() });
            }

            aDeducciones.add(deduction.getCode());
            aDeducciones.add(deduction.getCode());
            aDeducciones.add(deduction.getName() + (loan == null ? "" : " (" + loan.getLoanIdentificator() + ") "));
            aDeducciones.add(receiptDeduction.getAmount_r());
            aDeducciones.add(null);

            dTotalDeducciones += receiptDeduction.getAmount_r();
        }

        for (int j = payrollReceipt.getChildPayrollReceiptDeductions().size(); j < 10; j++) {
            aDeducciones.add(null);
            aDeducciones.add(null);
            aDeducciones.add(null);
            aDeducciones.add(null);
            aDeducciones.add(null);
        }

        map.put("oPerceptions", aPercepciones);
        map.put("oDeductions", aDeducciones);
        map.put("TotalPercepcionesGravado", dTotalPercepciones);
        map.put("TotalDeduccionesGravado", dTotalDeducciones);
        map.put("TotalNeto", dTotalPercepciones - dTotalDeducciones);

        switch (pnPrintMode) {
            case SDataConstantsPrint.PRINT_MODE_VIEWER:
                client.getSession().printReport(SModConsts.HRSR_PAY_RCP, SLibConsts.UNDEFINED, null, map);
                break;
            case SDataConstantsPrint.PRINT_MODE_PDF:
                break;
            case SDataConstantsPrint.PRINT_MODE_STREAM:
                break;
            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }
    }
    
    public static void printPrePayroll(final SGuiClient client, final int payrollKey) throws java.lang.Exception {
        HashMap<String, Object> map = null;
        SDataBizPartner bizPartnerCompany = null;

        bizPartnerCompany = new SDataBizPartner();
        bizPartnerCompany.read(new int[] { ((SClientInterface) client).getSessionXXX().getCompany().getPkCompanyId() }, client.getSession().getStatement());
        
        map = client.createReportParams();
        
        map.put("nPayrollId", payrollKey);
        map.put("RegistroPatronal", ((SClientInterface) client).getSessionXXX().getParamsCompany().getRegistrySs());
        map.put("sEmiRfc", bizPartnerCompany.getFiscalId());
        
        client.getSession().printReport(SModConsts.HRSR_PRE_PAY, SLibConsts.UNDEFINED, null, map);
    }
    
    /**
     * Obtain consumption days in payrolls previous
     * @param absence Absence registry consumption
     * @param hrsEmployee Object the employee
     * @return int; consumption days in payrolls previous
     */
    public static int getConsumptionPreviousDays(final SDbAbsence absence, final SHrsEmployee hrsEmployee) {
        int consumptionPreviousDays = 0;
        
        for (SDbAbsenceConsumption absenceConsumptionAux : hrsEmployee.getAbsencesConsumptions()) {
            if (absence.getPkAbsenceId() == absenceConsumptionAux.getPkAbsenceId() &&
                    absence.getPkEmployeeId() == absenceConsumptionAux.getPkEmployeeId()) {
                consumptionPreviousDays += absenceConsumptionAux.getEffectiveDays();
            }
        }
        
        for (SDbAbsenceConsumption absenceConsumptionAux : hrsEmployee.getHrsPayrollReceipt().getAbsenceConsumptions()) {
            if (absence.getPkAbsenceId() == absenceConsumptionAux.getPkAbsenceId() &&
                    absence.getPkEmployeeId() == absenceConsumptionAux.getPkEmployeeId()) {
                consumptionPreviousDays += absenceConsumptionAux.getEffectiveDays();
            }
        }
        
        return consumptionPreviousDays;
    }
    
    /**
     * Obtain last date consumption
     * @param absence Absence registry consumption
     * @param hrsEmployee Object the employee
     * @return Date; last date consumption
     */
    public static Date getConsumptionDateLast(final SDbAbsence absence, final SHrsEmployee hrsEmployee) {
        Date dateConsumptionLast = null;
        
        for (SDbAbsenceConsumption absenceConsumptionAux : hrsEmployee.getHrsPayrollReceipt().getAbsenceConsumptions()) {
            if (absence.getPkAbsenceId() == absenceConsumptionAux.getPkAbsenceId() &&
                    absence.getPkEmployeeId() == absenceConsumptionAux.getPkEmployeeId()) {
                dateConsumptionLast = absenceConsumptionAux.getDateEnd();
            }
        }
        
        if (dateConsumptionLast == null) {
            for (SDbAbsenceConsumption absenceConsumptionAux : hrsEmployee.getAbsencesConsumptions()) {
                if (absence.getPkAbsenceId() == absenceConsumptionAux.getPkAbsenceId() &&
                        absence.getPkEmployeeId() == absenceConsumptionAux.getPkEmployeeId()) {
                    dateConsumptionLast = absenceConsumptionAux.getDateEnd();
                }
            }
        }
        
        return dateConsumptionLast;
    }
    
    /**
     * Obtain ID next of consumption
     * @param absence Absence registry consumption
     * @param hrsEmployee Object the employee
     * @return int; ID next of consumption
     */
    public static int getConsumptionNextId(final SDbAbsence absence, final SHrsEmployee hrsEmployee) {
        int consumptionLast = 0;
        
        for (SDbAbsenceConsumption absenceConsumptionAux : hrsEmployee.getHrsPayrollReceipt().getAbsenceConsumptions()) {
            if (absence.getPkAbsenceId() == absenceConsumptionAux.getPkAbsenceId() &&
                    absence.getPkEmployeeId() == absenceConsumptionAux.getPkEmployeeId()) {
                consumptionLast = absenceConsumptionAux.getPkConsumptionId();
            }
        }
        
        return consumptionLast + 1;
    }
    
    /**
     * Obtain balance loan
     * @param loan Registry loan
     * @param hrsEmployee SHrsEmployee registry the employee
     * @return double; balance loan
     * @throws Exception 
     */
    public static double getBalanceLoan(final SDbLoan loan, final SHrsEmployee hrsEmployee) throws Exception {
        double balance = 0;
        
        balance = loan.getTotalAmount();
        
        if (hrsEmployee.getLoanPayment(loan.getPkLoanId()) != null) {
            balance += hrsEmployee.getLoanPayment(loan.getPkLoanId()).getTotalEarnings();
        }
        
        if (hrsEmployee.getLoanPayment(loan.getPkLoanId()) != null) {
            balance -= hrsEmployee.getLoanPayment(loan.getPkLoanId()).getTotalPayment();
        }
        
        for (SHrsPayrollReceiptEarning deduction : hrsEmployee.getHrsPayrollReceipt().getHrsEarnings()) {
            if (SLibUtils.compareKeys(new int[] { deduction.getReceiptEarning().getFkLoanEmployeeId_n(), deduction.getReceiptEarning().getFkLoanLoanId_n()}, 
                    loan.getPrimaryKey())) {
                balance += deduction.getReceiptEarning().getAmount_r();
            }
        }
        
        for (SHrsPayrollReceiptDeduction deduction : hrsEmployee.getHrsPayrollReceipt().getHrsDeductions()) {
            if (SLibUtils.compareKeys(new int[] { deduction.getReceiptDeduction().getFkLoanEmployeeId_n(), deduction.getReceiptDeduction().getFkLoanLoanId_n()}, 
                    loan.getPrimaryKey())) {
                balance -= deduction.getReceiptDeduction().getAmount_r();
            }
        }
        
        return balance;
    }
    
    public static String getEmployeeNextNumber(Connection connection) throws Exception {
        String nextNumber = "";
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COALESCE(MAX(num), 0) + 1 "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + "; ";

        resultSet = connection.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            nextNumber = resultSet.getInt(1) + "";
        }

        return nextNumber;
    }
    
    public static int getSeniorityEmployee(final SGuiSession session, final Date dateBenefits, final Date dateCut) throws Exception {
        int seniority = 0;
        String sql = "";
        ResultSet resultSet = null;
        
        sql = "SELECT TIMESTAMPDIFF(YEAR,'" + SLibUtils.DbmsDateFormatDate.format(dateBenefits) + "','" + 
                SLibUtils.DbmsDateFormatDate.format(dateCut) + "') ";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            seniority = resultSet.getInt(1);
        }
        
        return seniority;
    }
    
    public static int getPaymentVacationsByEmployee(final SGuiSession session, final int employeeId, final int benefitAnn, final int benefitYear) throws Exception {
        int seniority = 0;
        String sql = "";
        ResultSet resultSet = null;
        
        sql = "SELECT COALESCE(SUM(ear.unt_all), 0) AS f_payed_unt " +
                "FROM hrs_pay_rcp AS rcp " +
                "INNER JOIN hrs_pay_rcp_ear AS ear ON ear.id_pay = rcp.id_pay AND ear.id_emp = rcp.id_emp " +
                "WHERE rcp.id_emp = " + employeeId + " AND rcp.b_del = 0 AND ear.b_del = 0 AND ear.fk_tp_ben = 21 AND ear.ben_ann = " + benefitAnn + 
                " AND ear.ben_year = " + benefitYear + " ";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            seniority = resultSet.getInt(1);
        }
        
        return seniority;
    }

    public static ArrayList<SDbEmployeeHireLog> getEmployeeHireLogs(final SGuiSession session, final int employeeId, final Date dateStart, final Date dateEnd) throws Exception {
        ArrayList<SDbEmployeeHireLog> aEmployeeHireLogs = new ArrayList<SDbEmployeeHireLog>();
        SDbEmployeeHireLog employeeHireLog = null;
        String sql = "";

        ResultSet resultSet = null;
        Statement statement = session.getDatabase().getConnection().createStatement();

        sql = "SELECT id_emp, id_log " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_HIRE) + " " +
            "WHERE b_del = 0 AND id_emp = " + employeeId + " AND (dt_hire <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' AND " +
            "(dt_dis_n IS NULL OR dt_dis_n >= '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "')) ";
        
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            employeeHireLog = new SDbEmployeeHireLog();
            employeeHireLog.read(session, new int[] { resultSet.getInt("id_emp"), resultSet.getInt("id_log") });
            aEmployeeHireLogs.add(employeeHireLog);
        }

        return aEmployeeHireLogs;
    }
    
    public static SDbEmployeeHireLog getEmployeeLastHired(final SGuiSession session, final int employeeId, final String table) throws Exception {
        String sql = "";
        ResultSet resultSet = null;
        SDbEmployeeHireLog employeeHireLog = null;
        
        sql = "SELECT id_emp, id_log " +
            "FROM " + (table.isEmpty() ? "" : (table + ".")) + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_HIRE) + " " +
            "WHERE b_del = 0 AND id_emp = " + employeeId + " AND dt_dis_n IS NULL " +
            "ORDER BY dt_hire DESC, id_emp, id_log " +
            "LIMIT 1 ";
        
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            employeeHireLog = new SDbEmployeeHireLog();
            employeeHireLog.read(session, new int[] { resultSet.getInt("id_emp"), resultSet.getInt("id_log") });
        }
        
        return employeeHireLog;
    }
    
    public static ArrayList<SHrsBenefit> readHrsBenefits(final SGuiSession session, final SDbEmployee employee, final int benefitType, final int aniversaryLimit, final int benefitYearAux, final int payrrollId, final ArrayList<SHrsBenefitTableByAnniversary> benefitTableByAnniversary, final ArrayList<SHrsBenefitTableByAnniversary> benefitTableByAnniversaryAux, final double paymentDaily) throws Exception {
        boolean foundAnniversary = false;
        ArrayList<SHrsBenefit> aHrsBenefits = new ArrayList<SHrsBenefit>();
        SHrsBenefit hrsBenefit = null;
        SHrsBenefitTableByAnniversary benefitTableRow = null;
        SHrsBenefitTableByAnniversary benefitTableRowAux = null;
        String sql = "";
        ResultSet resultSet = null;
        
        sql = "SELECT fk_tp_ben, ben_ann, ben_year, SUM(unt_all) AS f_val_payed, " +
                "SUM(amt_r) AS f_amt_payed " +
                "FROM hrs_pay_rcp_ear " +
                "WHERE b_del = 0 AND id_emp = " + employee.getPkEmployeeId() + " AND id_pay <> " + payrrollId + " AND fk_tp_ben = " + benefitType + " AND ben_ann <= " + aniversaryLimit + " " +
                "GROUP BY fk_tp_ben, ben_ann, ben_year ";
        resultSet = session.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            hrsBenefit = new SHrsBenefit(benefitType, resultSet.getInt("ben_ann"), resultSet.getInt("ben_year"));
            
            hrsBenefit.setValuePayed(resultSet.getDouble("f_val_payed"));
            hrsBenefit.setAmountPayed(resultSet.getDouble("f_amt_payed"));
            
            aHrsBenefits.add(hrsBenefit);
        }
        
        for (SHrsBenefit benefit : aHrsBenefits) {
            if (SLibUtils.compareKeys(benefit.getPrimaryBenefitType(), new int[] { benefitType, aniversaryLimit, benefitYearAux })) {
                foundAnniversary = true;
            }
        }
        
        if (!foundAnniversary) {
            hrsBenefit = new SHrsBenefit(benefitType, aniversaryLimit, benefitYearAux);
            aHrsBenefits.add(hrsBenefit);
        }
        
        // To complete benefits registries accumulated by benefit type:
        
        for (SHrsBenefit benefit : aHrsBenefits) {
            for (SHrsBenefitTableByAnniversary row : benefitTableByAnniversary) {
                if (row.getBenefitAnn() <= benefit.getBenefitAnn()) {
                    benefitTableRow = row;
                }
            }

            if (benefitType == SModSysConsts.HRSS_TP_BEN_VAC_BON) {
                for (SHrsBenefitTableByAnniversary row : benefitTableByAnniversaryAux) {
                    if (row.getBenefitAnn() <= benefit.getBenefitAnn()) {
                        benefitTableRowAux = row;
                    }
                }
            }

            if (benefitType == SModSysConsts.HRSS_TP_BEN_VAC_BON) {
                benefit.setValue(benefitTableRow == null || benefitTableRowAux == null ? 0d : benefitTableRowAux.getValue());
                benefit.setAmount(benefitTableRow == null || benefitTableRowAux == null ? 0d : benefitTableRowAux.getValue() * paymentDaily * benefitTableRow.getValue());
            }
            else {
                benefit.setValue(benefitTableRow == null ? 0d : benefitTableRow.getValue());
                benefit.setAmount(benefitTableRow == null ? 0d : benefitTableRow.getValue() * paymentDaily);
            }
        }
        
        return aHrsBenefits;
    }
    
    public static int getScheduledDays(final SGuiSession session, final SDbEmployee employee, final int benefitAnn, final int benefitYear, final int absenceId) throws Exception {
        int scheduledDays = 0;
        String sql = "";
        ResultSet resultSet = null;

         sql = "SELECT a.id_emp, a.id_abs, SUM(a.eff_day) AS f_days " +
                 "FROM hrs_abs AS a " +
                 "WHERE a.b_del = 0 AND a.id_emp = " + employee.getPkEmployeeId() + " AND a.id_abs <> " + absenceId + " AND a.ben_year = " + benefitYear + " AND a.ben_ann = " + benefitAnn + " " +
                 "GROUP BY a.id_emp, a.id_abs ";
         
         resultSet = session.getStatement().executeQuery(sql);
         if (resultSet.next()) {
             scheduledDays = resultSet.getInt("f_days");
         }
         
        return scheduledDays;
    }
    
    public static SHrsFormerPayroll readPayroll(final SClientInterface client, final int payrollId, final int payrollEmployeeId, final int payrollIssueId) throws SQLException, Exception {
        int f_emp_map_bp = 0;
        int f_emp_id = 0;
        int claveOficial = 0;
        String sql = "";
        String deductionsTaxRetained = "";
        String cia_reg_imss = "";
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        SHrsFormerPayroll hrsPayroll = null;
        SHrsFormerPayrollReceipt hrsPayrollReceipt = null;
        SHrsFormerPayrollConcept hrsPayrollConcept = null;
        SHrsFormerPayrollExtraTime hrsPayrollExtraTime = null;

        Statement statement = client.getSession().getStatement().getConnection().createStatement();
        Statement statementAux = client.getSession().getStatement().getConnection().createStatement();
        Statement statementClient = client.getSession().getStatement().getConnection().createStatement();

        ResultSet resultSet = null;
        ResultSet resultSetAux = null;
        ResultSet resultSetClient = null;

        // Obtain deductions for tax retained:
        
        sql = "SELECT id_ded FROM hrs_ded WHERE fk_tp_ded = " + SModSysConsts.HRSS_TP_DED_TAX + "";
        
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            deductionsTaxRetained += (deductionsTaxRetained.length() == 0 ? "" : ", ") + resultSet.getInt("id_ded");
        }

        // Obtain payroll header:

        sql = "SELECT p.per_year, p.id_pay, tp.code, p.num, p.id_pay, NOW() AS f_date, p.dt_sta, p.dt_end, " +
                "SUM(rcp.ear_r) AS f_ear, " +
                "SUM(rcp.ded_r) AS f_ded, " +
                "pei.dt_iss, " +
                "(SELECT COALESCE(SUM(rcp_ded.amt_r), 0) " +
                "FROM hrs_pay_rcp AS r " +
                "INNER JOIN hrs_pay_rcp_ded AS rcp_ded ON rcp_ded.id_pay = r.id_pay AND rcp_ded.id_emp = r.id_emp " +
                "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ded.b_del = 0 AND rcp_ded.fk_ded IN (" + deductionsTaxRetained + ")) AS f_rent_ret, " +
                "'SUELDOS Y SALARIOS ' AS f_descrip, p.fk_tp_pay " +
                "FROM hrs_pay AS p " +
                "INNER JOIN hrs_pay_rcp AS rcp ON rcp.id_pay = p.id_pay " +
                "INNER JOIN hrs_pay_rcp_iss AS pei ON " +
                "rcp.id_pay = pei.id_pay AND rcp.id_emp = pei.id_emp AND pei.b_del = 0 AND pei.id_iss = " + payrollIssueId + " " +
                "INNER JOIN erp.hrss_tp_pay AS tp ON p.fk_tp_pay = tp.id_tp_pay " +
                "WHERE rcp.id_pay = " + payrollId + " AND rcp.b_del = 0 AND rcp.id_emp = " + payrollEmployeeId + " " + 
                "GROUP BY p.per_year, p.per, tp.code, p.num, p.id_pay, f_date, p.dt_sta, p.dt_end " +
                "ORDER BY p.per_year DESC, p.per DESC, tp.code, p.num, p.id_pay, f_date, p.dt_sta, p.dt_end; ";
        
        resultSet = statement.executeQuery(sql);
        if (!resultSet.next()) {
            throw new Exception("No se encontró la nómina.");
        }
        else {

            // Obtain company parameters (id_co, id_bpb, fiscal_settings):

            sql = "SELECT p.id_co, bpb.id_bpb, p.fiscal_settings " +
                "FROM cfg_param_co AS p " +
                "INNER JOIN erp.bpsu_bp AS b ON " +
                "p.id_co = b.id_bp " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON " +
                "b.id_bp = bpb.fid_bp " +
                "WHERE p.b_del = 0 AND p.id_co = " + client.getSession().getConfigCompany().getCompanyId() + " AND b.b_co = " + SUtilConsts.BPR_CO_ID + " ";
            resultSetAux = statementClient.executeQuery(sql);

            if (!resultSetAux.next()) {
                throw new Exception("No se encontró la configuración de la empresa.");
            }

            hrsPayroll = new SHrsFormerPayroll(client);

            hrsPayroll.setPkNominaId(resultSet.getInt("id_pay"));
            hrsPayroll.setFecha(resultSet.getDate("pei.dt_iss"));
            hrsPayroll.setFechaInicial(resultSet.getDate("dt_sta"));
            hrsPayroll.setFechaFinal(resultSet.getDate("dt_end"));
            hrsPayroll.setTotalPercepciones(resultSet.getDouble("f_ear"));
            hrsPayroll.setTotalDeducciones(resultSet.getDouble("f_ded"));
            hrsPayroll.setTotalRetenciones(resultSet.getDouble("f_rent_ret"));
            hrsPayroll.setDescripcion(SLibUtilities.textLeft(SLibUtilities.textTrim(resultSet.getString("f_descrip")), 100)); // 100 = pay_note column width
            hrsPayroll.setEmpresaId(resultSetAux.getInt("p.id_co"));
            hrsPayroll.setSucursalEmpresaId(resultSetAux.getInt("bpb.id_bpb"));
            hrsPayroll.setRegimenFiscal(new String[] { SLibUtilities.textTrim(resultSetAux.getString("p.fiscal_settings")) });
        }

        // Obtain employer registry:

        cia_reg_imss = client.getSessionXXX().getParamsCompany().getRegistrySs();

        // Obtain employee payroll:

        sql = "SELECT bp.bp, bp.id_bp AS f_emp_map_bp, emp.id_emp AS f_emp_id, emp.num AS f_emp_num, bp.alt_id AS f_emp_curp, emp.ssn AS f_emp_nss, " +
                "emp.fk_tp_rec_sche AS f_emp_reg_tp, rcp.day_pad AS f_emp_dias_pag, d.name AS f_emp_dep, d.code AS f_emp_dep_cve, " +
                "'' AS f_emp_bank_clabe, " +
                "pei.dt_pay, pei.num_ser, pei.num, pei.fk_tp_pay_sys, " +
                "CASE WHEN emp.fk_bank_n IS NOT NULL THEN emp.fk_bank_n ELSE (SELECT fk_bank FROM hrs_cfg WHERE id_cfg = " + SUtilConsts.BPR_CO_ID + ") END AS f_emp_bank, " +
                "emp.dt_hire AS f_emp_alta, p.dt_sta AS f_nom_date_start, p.dt_end AS f_nom_date_end, " +
                "TIMESTAMPDIFF(DAY, emp.dt_ben, p.dt_end) / " + SHrsConsts.WEEK_DAYS + " AS f_emp_sen, pos.name AS f_emp_pos, " +
                "'' AS f_emp_cont_tp, '' AS f_emp_jorn_tp, tp.name AS f_emp_pay, rcp.sal_ssc AS f_emp_sal_bc, ris.id_tp_pos_risk AS f_emp_risk, " +
                "SUM(rcp.ear_r) AS f_ear, " +
                "SUM(rcp.ded_r) AS f_ded, " +
                "(SELECT COALESCE(SUM(rcp_ded.amt_r), 0) " +
                "FROM hrs_pay_rcp AS r " +
                "INNER JOIN hrs_pay_rcp_ded AS rcp_ded ON rcp_ded.id_pay = r.id_pay AND rcp_ded.id_emp = r.id_emp " +
                "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ded.b_del = 0 AND rcp_ded.id_emp = rcp.id_emp AND rcp_ded.fk_ded IN (" + deductionsTaxRetained + ")) AS f_emp_tot_rent_ret, " +
                "SUM(rcp.pay_r) AS f_emp_tot_net, NOW() AS f_emp_date_edit " +
                "FROM hrs_pay AS p " +
                "INNER JOIN hrs_pay_rcp AS rcp ON rcp.id_pay = p.id_pay " +
                "INNER JOIN hrs_pay_rcp_iss AS pei ON " +
                "rcp.id_pay = pei.id_pay AND rcp.id_emp = pei.id_emp AND pei.b_del = 0 AND pei.id_iss = " + payrollIssueId + " " +
                "INNER JOIN erp.hrsu_emp AS emp ON emp.id_emp = rcp.id_emp " +
                "INNER JOIN erp.bpsu_bp AS bp ON bp.id_bp = emp.id_emp " +
                "INNER JOIN erp.hrsu_dep AS d ON d.id_dep = rcp.fk_dep " +
                "INNER JOIN erp.hrsu_pos AS pos ON pos.id_pos = rcp.fk_pos " +
                "INNER JOIN erp.hrss_tp_sal AS st ON st.id_tp_sal = rcp.fk_tp_sal " +
                "INNER JOIN erp.hrsu_tp_emp AS et ON et.id_tp_emp = rcp.fk_tp_emp " +
                "INNER JOIN erp.hrss_tp_pos_risk AS ris ON ris.id_tp_pos_risk = rcp.fk_tp_pos_risk " +
                "INNER JOIN erp.hrss_tp_pay AS tp ON p.fk_tp_pay = tp.id_tp_pay " +
                "WHERE rcp.id_pay = " + payrollId + " AND rcp.b_del = 0 AND rcp.id_emp = " + payrollEmployeeId + " " +
                "GROUP BY bp.bp, f_emp_map_bp, f_emp_num, f_emp_curp, f_emp_nss, f_emp_reg_tp, f_emp_dias_pag, f_emp_dep, f_emp_dep_cve, d.id_dep, f_emp_bank, f_emp_alta, f_nom_date_start, f_nom_date_end, " +
                "f_emp_sen, f_emp_pos, f_emp_pay, f_emp_sal_bc, f_emp_risk " +
                "ORDER BY bp.bp, f_emp_map_bp, f_emp_num, f_emp_curp, f_emp_nss, f_emp_reg_tp, f_emp_dias_pag, f_emp_dep, f_emp_dep_cve, d.id_dep, f_emp_bank, f_emp_alta, f_nom_date_start, f_nom_date_end, " +
                "f_emp_sen, f_emp_pos, f_emp_pay, f_emp_sal_bc, f_emp_risk; ";
        resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            hrsPayrollReceipt = new SHrsFormerPayrollReceipt(hrsPayroll, client);

            // Obtain employee company branch:

            f_emp_map_bp = resultSet.getInt("f_emp_map_bp");

            sql = "SELECT bpb.id_bpb  " +
                "FROM erp.bpsu_bpb AS bpb " +
                "WHERE bpb.b_del = 0 AND bpb.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " AND bpb.fid_bp = " + f_emp_map_bp + " ";
            resultSetClient = statementClient.executeQuery(sql);

            if (!resultSetClient.next()) {
                throw new Exception("No se encontró la sucursal del empleado '" + f_emp_map_bp + "'.");
            }

            hrsPayrollReceipt.setPkEmpleadoId(f_emp_map_bp);
            f_emp_id = resultSet.getInt("f_emp_id");
            hrsPayrollReceipt.setAuxEmpleadoId(f_emp_id);
            hrsPayrollReceipt.setPkSucursalEmpleadoId(resultSetClient.getInt("bpb.id_bpb"));
            hrsPayrollReceipt.setRegistroPatronal(cia_reg_imss);
            hrsPayrollReceipt.setNumEmpleado(SLibUtilities.textTrim(resultSet.getString("f_emp_num")));
            hrsPayrollReceipt.setCurp(SLibUtilities.textTrim(resultSet.getString("f_emp_curp")));
            hrsPayrollReceipt.setNumSeguridadSocial(SLibUtilities.textTrim(resultSet.getString("f_emp_nss")));
            hrsPayrollReceipt.setTipoRegimen(resultSet.getInt("f_emp_reg_tp"));
            hrsPayrollReceipt.setNumDiasPagados(resultSet.getDouble("f_emp_dias_pag"));
            hrsPayrollReceipt.setDepartamento(SLibUtilities.textTrim(resultSet.getString("f_emp_dep")));
            hrsPayrollReceipt.setClabe(SLibUtilities.textTrim(resultSet.getString("f_emp_bank_clabe")));
            hrsPayrollReceipt.setBanco(resultSet.getInt("f_emp_bank"));
            hrsPayrollReceipt.setFechaPago(resultSet.getDate("pei.dt_pay"));
            hrsPayrollReceipt.setFechaInicioRelLaboral(resultSet.getDate("f_emp_alta"));
            hrsPayrollReceipt.setFechaInicialPago(resultSet.getDate("f_nom_date_start"));
            hrsPayrollReceipt.setFechaFinalPago(resultSet.getDate("f_nom_date_end"));
            hrsPayrollReceipt.setAntiguedad(resultSet.getInt("f_emp_sen"));
            hrsPayrollReceipt.setPuesto(SLibUtilities.textTrim(resultSet.getString("f_emp_pos")));
            hrsPayrollReceipt.setTipoContrato(SLibUtilities.textTrim(resultSet.getString("f_emp_cont_tp")));
            hrsPayrollReceipt.setTipoJornada(SLibUtilities.textTrim(resultSet.getString("f_emp_jorn_tp")));
            hrsPayrollReceipt.setPeriodicidadPago(SLibUtilities.textTrim(resultSet.getString("f_emp_pay")));
            hrsPayrollReceipt.setSalarioBaseCotApor(resultSet.getDouble("f_emp_sal_bc"));
            hrsPayrollReceipt.setRiesgoPuesto(resultSet.getInt("f_emp_risk"));
            hrsPayrollReceipt.setSalarioDiarioIntegrado(hrsPayrollReceipt.getSalarioBaseCotApor());
            hrsPayrollReceipt.setTotalPercepciones(resultSet.getDouble("f_ear"));
            hrsPayrollReceipt.setTotalDeducciones(resultSet.getDouble("f_ded"));
            hrsPayrollReceipt.setTotalRetenciones(resultSet.getDouble("f_emp_tot_rent_ret"));
            hrsPayrollReceipt.setTotalNeto(resultSet.getDouble("f_emp_tot_net"));
            hrsPayrollReceipt.setFechaEdicion(resultSet.getDate("f_emp_date_edit"));
            hrsPayrollReceipt.setSerie(SLibUtilities.textTrim(resultSet.getString("pei.num_ser")));
            hrsPayrollReceipt.setFolio(resultSet.getInt("pei.num"));
            hrsPayrollReceipt.setMetodoPago(resultSet.getInt("pei.fk_tp_pay_sys"));

            /*
            // Obtain 'num_ser', 'num', 'fid_tp_pay_sys' from 'hrs_sie_pay_emp' table:

            sql = "SELECT pe.num_ser, pe.num, pe.fid_tp_pay_sys " +
                "FROM hrs_sie_pay AS p " +
                "INNER JOIN hrs_sie_pay_emp AS pe ON " +
                "p.id_pay = pe.id_pay AND pe.b_del = FALSE " +
                "WHERE pe.id_pay = " + payrollId + " AND pe.id_emp = " + f_emp_id + "";
            resultSetClient = statementClient.executeQuery(sql);

            if (!resultSetClient.next()) {
                throw new Exception("No se encontró el folio del empleado '" + SLibUtilities.textTrim(resultSet.getString("f_emp_num")) + "'.");
            }
            else {
                hrsPayrollReceipt.setSerie(SLibUtilities.textTrim(resultSetClient.getString("pe.num_ser")));
                hrsPayrollReceipt.setFolio(resultSetClient.getInt("pe.num"));
                hrsPayrollReceipt.setMetodoPago(resultSetClient.getInt("pe.fid_tp_pay_sys"));
            }
            */

            // Obtain currency key from ERP parameters:

            sql = "SELECT c.cur_key " +
                "FROM erp.cfg_param_erp AS p " +
                "INNER JOIN erp.cfgu_cur AS c ON " +
                "p.fid_cur = c.id_cur " +
                "WHERE p.b_del = 0 ";
            resultSetClient = statementClient.executeQuery(sql);

            if (!resultSetClient.next()) {
                throw new Exception("No se encontró la configuración del ERP.");
            }
            else {
                hrsPayrollReceipt.setMoneda(SLibUtilities.textTrim(resultSetClient.getString("c.cur_key")));
            }

            hrsPayroll.getChildPayrollReceipts().add(hrsPayrollReceipt);

            if (hrsPayrollReceipt != null) {

                // Obtain perceptions:

                sql = "SELECT ear.code AS f_conc_cve, ear.name_abbr AS f_conc, ear.fk_tp_ear AS f_conc_cfdi, ear.unt_fac, " +
                        "CASE WHEN ear.fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_OVR_TME + " THEN CASE WHEN rcp_ear.unt >= 0 AND rcp_ear.unt < " + SHrsConsts.OVER_TIME_2X_MAX_DAY + " THEN 1 ELSE rcp_ear.unt / " + SHrsConsts.OVER_TIME_2X_MAX_DAY + " END ELSE " +
                        "CASE WHEN rcp_ear.unt >= 0 AND rcp_ear.unt <= 1 THEN 1 ELSE rcp_ear.unt END END AS f_conc_qty, " +
                        "CASE WHEN ear.fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_OVR_TME + " THEN " +
                        "CASE WHEN rcp_ear.unt >= 0 AND rcp_ear.unt <= 1 THEN 1 ELSE rcp_ear.unt END ELSE 0 END AS f_conc_hrs, comp.code AS f_conc_unid, " +
                        "rcp_ear.amt_taxa AS f_conc_mont_grav, rcp_ear.amt_exem AS f_conc_mont_ext, " +
                        "1 AS f_conc_tp, " +
                        "CASE WHEN ear.fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_OVR_TME + " AND ear.unt_fac = " + SHrsConsts.OVER_TIME_2X + " THEN " + SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1] + " ELSE " +
                        "CASE WHEN ear.fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_OVR_TME + " AND ear.unt_fac = " + SHrsConsts.OVER_TIME_3X + " THEN " + SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_TRIPLE[1] + " ELSE " + 
                        "" + SCfdConsts.CFDI_PAYROLL_PERCEPTION_PERCEPTION[1] + " END END AS f_conc_stp " +
                        "FROM hrs_pay AS p " +
                        "INNER JOIN hrs_pay_rcp AS rcp ON rcp.id_pay = p.id_pay AND rcp.b_del = 0 " +
                        "INNER JOIN hrs_pay_rcp_ear AS rcp_ear ON rcp_ear.id_pay = rcp.id_pay AND rcp_ear.id_emp = rcp.id_emp AND rcp_ear.b_del = 0 " +
                        "INNER JOIN hrs_ear AS ear ON ear.id_ear = rcp_ear.fk_ear " +
                        "INNER JOIN erp.hrss_tp_ear_comp AS comp ON comp.id_tp_ear_comp = ear.fk_tp_ear_comp " +
                        "WHERE p.id_pay = " + payrollId + " AND rcp.id_emp = " + f_emp_id + "; ";

                resultSetAux = statementAux.executeQuery(sql);
                while (resultSetAux.next()) {

                    hrsPayrollConcept = new SHrsFormerPayrollConcept();

                    hrsPayrollConcept.setClaveEmpresa(SLibUtilities.textTrim(resultSetAux.getString("f_conc_cve")));
                    hrsPayrollConcept.setConcepto(SLibUtilities.textTrim(resultSetAux.getString("f_conc")));
                    hrsPayrollConcept.setClaveOficial(resultSetAux.getInt("f_conc_cfdi"));
                    claveOficial = hrsPayrollConcept.getClaveOficial();
                    hrsPayrollConcept.setCantidad(claveOficial == SModSysConsts.HRSS_TP_EAR_OVR_TME ? Math.ceil(resultSetAux.getDouble("f_conc_qty")) : resultSetAux.getDouble("f_conc_qty"));
                    hrsPayrollConcept.setHoras_r(resultSetAux.getInt("f_conc_hrs"));
                    hrsPayrollConcept.setTotalGravado(resultSetAux.getDouble("f_conc_mont_grav"));
                    hrsPayrollConcept.setTotalExento(resultSetAux.getDouble("f_conc_mont_ext"));
                    hrsPayrollConcept.setPkTipoConcepto(resultSetAux.getInt("f_conc_tp"));
                    hrsPayrollConcept.setPkSubtipoConcepto(resultSetAux.getInt("f_conc_stp"));

                    if (claveOficial == SModSysConsts.HRSS_TP_EAR_OVR_TME) {
                        hrsPayrollExtraTime = new SHrsFormerPayrollExtraTime();
                        hrsPayrollExtraTime.setTipoHoras(resultSetAux.getInt("unt_fac") == SHrsConsts.OVER_TIME_2X ?
                            SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE : SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE);
                        hrsPayrollExtraTime.setDias(hrsPayrollConcept.getCantidad());
                        hrsPayrollExtraTime.setHorasExtra(hrsPayrollConcept.getHoras_r());
                        hrsPayrollExtraTime.setImportePagado(hrsPayrollConcept.getTotalGravado() + hrsPayrollConcept.getTotalExento());

                        hrsPayrollConcept.setChildPayrollExtraTimes(hrsPayrollExtraTime);
                    }

                    hrsPayrollReceipt.getChildPayrollConcept().add(hrsPayrollConcept);
                }

                // Obtain deductions:
                
                sql = "SELECT ded.code AS f_conc_cve, ded.fk_tp_ded AS f_conc_cfdi, " +
                        "(CASE WHEN rcp_ded.fk_loan_emp_n IS NULL AND rcp_ded.fk_loan_loan_n IS NULL THEN ded.name ELSE " +
                        "CAST(CONCAT(ded.name,'; ', ltp.name, '; no. ', l.num, '; ini.: ', l.dt_sta) AS CHAR CHARACTER SET latin1) END) AS f_conc, " +
                        "rcp_ded.unt AS f_conc_qty, 0 AS f_conc_hrs, '' AS f_conc_unid, rcp_ded.amt_r AS f_conc_mont_grav, 0 AS f_conc_mont_ext, " +
                        "" + SCfdConsts.CFDI_PAYROLL_DEDUCTION_DEDUCTION[0] + " AS f_conc_tp, " +
                        "" + SCfdConsts.CFDI_PAYROLL_DEDUCTION_DEDUCTION[1] + " AS f_conc_stp " +
                        "FROM hrs_pay AS p " +
                        "INNER JOIN hrs_pay_rcp AS rcp ON rcp.id_pay = p.id_pay " +
                        "INNER JOIN hrs_pay_rcp_ded AS rcp_ded ON rcp_ded.id_pay = rcp.id_pay AND rcp_ded.id_emp = rcp.id_emp " +
                        "INNER JOIN hrs_ded AS ded ON ded.id_ded = rcp_ded.fk_ded " +
                        "LEFT OUTER JOIN hrs_loan AS l ON l.id_emp = rcp_ded.fk_loan_emp_n AND l.id_loan = rcp_ded.fk_loan_loan_n " +
                        "LEFT OUTER JOIN erp.hrss_tp_loan AS ltp ON ltp.id_tp_loan = rcp_ded.fk_tp_loan_n " +
                        "WHERE p.id_pay = " + payrollId + " AND rcp.b_del = 0 AND rcp_ded.b_del = 0 AND rcp.id_emp = " + f_emp_id + "; ";

                resultSetAux = statementAux.executeQuery(sql);
                while (resultSetAux.next()) {

                    hrsPayrollConcept = new SHrsFormerPayrollConcept();

                    hrsPayrollConcept.setClaveEmpresa(SLibUtilities.textTrim(resultSetAux.getString("f_conc_cve")));
                    hrsPayrollConcept.setClaveOficial(resultSetAux.getInt("f_conc_cfdi"));
                    hrsPayrollConcept.setConcepto(SLibUtilities.textTrim(resultSetAux.getString("f_conc")));
                    hrsPayrollConcept.setCantidad(SLibUtils.round(resultSetAux.getDouble("f_conc_qty"), SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));
                    hrsPayrollConcept.setHoras_r(resultSetAux.getInt("f_conc_hrs"));
                    hrsPayrollConcept.setTotalGravado(resultSetAux.getDouble("f_conc_mont_grav"));
                    hrsPayrollConcept.setTotalExento(resultSetAux.getDouble("f_conc_mont_ext"));
                    hrsPayrollConcept.setPkTipoConcepto(resultSetAux.getInt("f_conc_tp"));
                    hrsPayrollConcept.setPkSubtipoConcepto(resultSetAux.getInt("f_conc_stp"));

                    hrsPayrollReceipt.getChildPayrollConcept().add(hrsPayrollConcept);
                }
            }
        }

        return hrsPayroll;
    }
    
    public static void createPayrollReceiptIssues(final SGuiSession session, final SDbPayroll payroll) throws Exception {
        for (SDbPayrollReceipt receipt : payroll.getChildPayrollReceipts()) {
            receipt.setAuxDateIssue(payroll.getDateEnd());
            receipt.createIssues(session);
        }
    }
    
    public static void updateToNewStatusPayrollReceiptIssues(final SGuiSession session, final SDbPayroll payroll) throws Exception {
        for (SDbPayrollReceipt receipt : payroll.getChildPayrollReceipts()) {
            receipt.updateToNewStatusIssues(session);
        }
    }
    
    public static double computeAmoutLoan(final SHrsPayrollReceipt hrsReceipt, final SDbLoan loan) throws Exception {
        double amoutAdjustment = 0;
        double amoutAux = 0;
        double amoutMonth = 0;
        double amout = 0;
        double adjustmentAux = 0;
        double salaryReference = 0;
        SHrsDaysByPeriod hrsDaysPrev = hrsReceipt.getHrsEmployee().getHrsDaysPrev();
        SHrsDaysByPeriod hrsDaysCurr = hrsReceipt.getHrsEmployee().getHrsDaysCurr();
        SHrsDaysByPeriod hrsDaysNext = hrsReceipt.getHrsEmployee().getHrsDaysNext();
        
        amoutAdjustment = hrsReceipt.getHrsPayroll().getLoanTypeAdjustment(hrsReceipt.getHrsPayroll().getPayroll().getDateEnd(), loan.getFkLoanTypeId());
        switch (loan.getFkLoanPaymentTypeId()) {
            case SModSysConsts.HRSS_TP_LOAN_PAY_AMT:
                amoutMonth = loan.getPaymentAmount() + amoutAdjustment;
                
                if (loan.getFkLoanTypeId() == SModSysConsts.HRSS_TP_LOAN_LOA) {
                    amoutAux = amoutMonth;
                }
                else {
                    amoutAux += hrsDaysPrev == null ? 0 : (amoutMonth / hrsDaysPrev.getDaysPeriod() * (hrsDaysPrev.getDaysPeriodPayroll() - hrsDaysPrev.getDaysPeriodPayrollNotWorkedNotPaid()));
                    amoutAux += amoutMonth / hrsDaysCurr.getDaysPeriod() * (hrsDaysCurr.getDaysPeriodPayroll() - hrsDaysCurr.getDaysPeriodPayrollNotWorkedNotPaid());
                    amoutAux += hrsDaysNext == null ? 0 : (amoutMonth / hrsDaysNext.getDaysPeriod() * (hrsDaysNext.getDaysPeriodPayroll() - hrsDaysNext.getDaysPeriodPayrollNotWorkedNotPaid()));
                }
                break;
            case SModSysConsts.HRSS_TP_LOAN_PAY_FIX:
                amoutMonth = loan.getPaymentFixed() * hrsReceipt.getHrsPayroll().getPayroll().getMwzReferenceWage() + amoutAdjustment;
                amoutAux += hrsDaysPrev == null ? 0 : (amoutMonth / hrsDaysPrev.getDaysPeriod() * (hrsDaysPrev.getDaysPeriodPayroll() - hrsDaysPrev.getDaysPeriodPayrollNotWorkedNotPaid()));
                amoutAux += amoutMonth / hrsDaysCurr.getDaysPeriod() * (hrsDaysCurr.getDaysPeriodPayroll() - hrsDaysCurr.getDaysPeriodPayrollNotWorkedNotPaid());
                amoutAux += hrsDaysNext == null ? 0 : (amoutMonth / hrsDaysNext.getDaysPeriod() * (hrsDaysNext.getDaysPeriodPayroll() - hrsDaysNext.getDaysPeriodPayrollNotWorkedNotPaid()));
                break;
            case SModSysConsts.HRSS_TP_LOAN_PAY_PER:
                adjustmentAux += hrsDaysPrev == null ? 0 : (double) ((hrsDaysPrev.getDaysPeriodPayroll() - hrsDaysPrev.getDaysPeriodPayrollNotWorkedNotPaid()) / hrsDaysPrev.getDaysPeriod()) * amoutAdjustment;
                adjustmentAux += (double) (hrsDaysCurr.getDaysPeriodPayroll() - hrsDaysCurr.getDaysPeriodPayrollNotWorkedNotPaid()) / hrsDaysCurr.getDaysPeriod() * amoutAdjustment;
                adjustmentAux += hrsDaysNext == null ? 0 : (double) ((hrsDaysNext.getDaysPeriodPayroll() - hrsDaysNext.getDaysPeriodPayrollNotWorkedNotPaid()) / hrsDaysNext.getDaysPeriod() * amoutAdjustment);
                
                if (loan.getPaymentPercentageReference() == SHrsConsts.SAL_REF_SAL) {
                    salaryReference = hrsReceipt.getReceipt().getPaymentDaily();
                }
                else if (loan.getPaymentPercentageReference() == SHrsConsts.SAL_REF_SAL_SS) {
                    salaryReference = hrsReceipt.getReceipt().getSalarySscBase();
                }
                else if (loan.getPaymentPercentageReference() == SHrsConsts.SAL_REF_SAL_FIX) {
                    salaryReference = loan.getPaymentPercentageAmount();
                }
                //amoutAux = (hrsReceipt.getReceipt().getDaysHiredPayroll() - hrsReceipt.getReceipt().getDaysNotWorkedNotPaid()) * hrsReceipt.getReceipt().getPaymentDaily() * loan.getPaymentPercentage() + adjustmentAux; // XXX (2016-03-04) jbarajas
                amoutAux = (hrsReceipt.getReceipt().getDaysHiredPayroll() - hrsReceipt.getReceipt().getDaysNotWorkedNotPaid()) * salaryReference * loan.getPaymentPercentage() + adjustmentAux;
                break;
            default:
                break;
        }
        amout = (SLibUtils.round(amoutAux, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));
        
        return amout;
    }
    
    public static double getAdjustmentLoan(final SHrsPayroll hrsPayroll, final int typeLoan) throws Exception {
        double amout = 0;
        
        switch (typeLoan) {
            case SModSysConsts.HRSS_TP_LOAN_LOA:
                break;
            case SModSysConsts.HRSS_TP_LOAN_HOM:
                amout = 7.5;
                break;
            case SModSysConsts.HRSS_TP_LOAN_CON:
                break;
            default:
                break;
        }
        
        return amout;
    }
}
