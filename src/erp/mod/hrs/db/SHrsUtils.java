/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.SClient;
import erp.cfd.SCfdConsts;
import erp.cfd.SDialogCfdProcessing;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STableUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mfin.data.SFinUtilities;
import erp.mhrs.data.SDataPayrollReceiptIssue;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.fin.util.STreasuryBankLayoutFile;
import erp.mod.fin.util.STreasuryBankLayoutRequest;
import erp.print.SDataConstantsPrint;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.joda.time.DateTime;
import org.joda.time.Period;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Alfredo Perez, Claudio Peña, Sergio Flores
 */
public abstract class SHrsUtils {
    
    /**
     * Replaces special characters \áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ\.
     * @param input Original text.
     * @return Text without special characters.
     */
    private static String removeSpecialChars(String input) {
        String specialCharacters = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        String replaceCharacters = "aaaeeeiiiooouuu#AAAEEEIIIOOOUUU#cC";
        String output = input;
        
        for (int i = 0; i < specialCharacters.length(); i++) {
            output = output.replace(specialCharacters.charAt(i), replaceCharacters.charAt(i));
        }
        
        return output;
    }        

    /**
     * Shows total of payroll bank-layout.
     * @param client GUI client.
     * @param total Total to be shown.
     */
    private static void showPayrollLayoutTotal(final SGuiClient client, final double total) {
        client.showMsgBoxInformation("Total a dispersar: $ " + SLibUtils.getDecimalFormatAmount().format(total));
    }
    
    /**
     * Creates payroll bank-layout for BanBajio.
     * @param client
     * @param payrollId
     * @param title
     * @param dateApplication
     * @param accountDebit
     * @param consecutiveDay
     * @param employees Employees IDs.
     */
    public static void createPayrollLayoutBanBajio(SGuiClient client, int payrollId, java.lang.String title, Date dateApplication, String accountDebit, int consecutiveDay, String[] employees) {
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
        String employeesId = "";
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String leyend = "";
        java.lang.String buffer = "";
        DecimalFormat formatDesc = new DecimalFormat("0000000000000.00");
        DecimalFormat formatDescTotal = new DecimalFormat("0000000000000000.00");
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
        double balance = 0;
        double total = 0;
        SDbConfig config = null;
        
        config = (SDbConfig) client.getSession().readRegistry(SModConsts.HRS_CFG, new int[] { SUtilConsts.BPR_CO_ID });
        dayFileName = SLibTimeUtils.digestDate(dateApplication)[2];
        monthFileName = SLibTimeUtils.digestDate(dateApplication)[1];
        n = (int) (Math.floor(Math.log10(consecutiveDay)) + 1);
        m = (int) (Math.floor(Math.log10(dayFileName)) + 1);
        employeesId = SLibUtils.textImplode(employees, ",");
        
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
                
                sql = "SELECT rcp.id_emp, emp.bank_acc, " +
                        "(SELECT COALESCE(SUM(rcp_ear.amt_r), 0) " +
                        "FROM hrs_pay_rcp AS r " +
                        "INNER JOIN hrs_pay_rcp_ear AS rcp_ear ON rcp_ear.id_pay = r.id_pay AND rcp_ear.id_emp = r.id_emp " +
                        "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ear.b_del = 0 AND rcp_ear.id_emp = rcp.id_emp) - " +
                        "(SELECT COALESCE(SUM(rcp_ded.amt_r), 0) " +
                        "FROM hrs_pay_rcp AS r " +
                        "INNER JOIN hrs_pay_rcp_ded AS rcp_ded ON rcp_ded.id_pay = r.id_pay AND rcp_ded.id_emp = r.id_emp " +
                        "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ded.b_del = 0 AND rcp_ded.id_emp = rcp.id_emp) AS _pay_net " +
                        "FROM hrs_pay AS p " +
                        "INNER JOIN hrs_pay_rcp AS rcp ON rcp.id_pay = p.id_pay " +
                        "INNER JOIN erp.hrsu_emp AS emp ON emp.id_emp = rcp.id_emp " +
                        "WHERE p.b_del = 0 AND rcp.b_del = 0 AND LENGTH(emp.bank_acc) > 0 AND rcp.id_pay = " + payrollId + " AND rcp.pay_r > 0 " +
                        "AND rcp.id_emp IN (" + employeesId + ")";
                
                resultSet = client.getSession().getStatement().executeQuery(sql);
                while (resultSet.next()) {
                    buffer = "";

                    sAccountCredit = SLibUtilities.textTrim(resultSet.getString("emp.bank_acc"));
                    leyend = "DEPOSITO DE NOMINA";
                    employeeId = resultSet.getInt("rcp.id_emp");
                    balance = resultSet.getDouble("_pay_net");
                    total += balance;

                    n = (int) (Math.floor(Math.log10(nMoveNum)) + 1);
                    m = (int) (Math.floor(Math.log10(employeeId)) + 1);
                    
                    buffer += "02";
                    buffer += SLibUtilities.textRepeat("0", 7 - n).concat(nMoveNum++ + "");
                    buffer += "90";
                    buffer += formatDate.format(dateApplication);
                    buffer += SLibUtilities.textRepeat("0", 3); // FILLER
                    buffer += "030";
                    buffer += formatDesc.format(balance).replace(".", "");
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

                // Summary:
                
                buffer = "";
                n = (int) (Math.floor(Math.log10(nMoveNum)) + 1);
                m = (int) (Math.floor(Math.log10(nMoveNumTotal)) + 1);

                buffer += "09";
                buffer += SLibUtilities.textRepeat("0", 7 - n).concat(nMoveNum + "");
                buffer += "90";
                buffer += SLibUtilities.textRepeat("0", 7 - m).concat(nMoveNumTotal + "");
                buffer += formatDescTotal.format(total).replace(".", "");
                buffer += SLibUtilities.textRepeat(" ", 145); // FILLER USE FUTURE BANK

                bw.write(buffer);
                bw.newLine();
                bw.flush();
                bw.close();

                showPayrollLayoutTotal(client, total);
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
     * Creates payroll bank-layout for Banamex.
     * @param client
     * @param payrollId
     * @param title
     * @param dateApplication
     * @param accountDebit
     * @param consecutiveDay
     * @param employees Employees IDs.
     * @param bankId ID of the bank of payment.
     */
    public static void createPayrollLayoutBanamex(SGuiClient client, int payrollId, java.lang.String title, Date dateApplication, String accountDebit, int consecutiveDay, String[] employees, int bankId) {
        ResultSet resultSetHeader = null;
        ResultSet resultSetDetail = null;
        BufferedWriter bw = null;
        DecimalFormat formatDescTotal = new DecimalFormat("0000000000000000.00");
        SimpleDateFormat formatDateData = new SimpleDateFormat("yyMMdd");
        SimpleDateFormat formatDateTitle = new SimpleDateFormat("yyMMdd HHmm");
        SDbConfig moConfig;
        SDataBizPartner oBizPartner = null;
        SDbPayroll moPayroll = null;
        String sql = "";
        String fileName = "";
        String buffer = "";
        String sAccountDebit = "";
        String sAccountCredit = "";
        String sBizPartner = "";
        String sDescription = "";
        String sCompany = "";
        String employeesId = "";
        double balance = 0;
        double total = 0;
        int nBankKey = 0;
        int n = 0;
        int nMoveNumTotal = 0;
        int nEmployeeBankId = 0;
        int nEmployeeBankAuxId = 0;
        
        moConfig = (SDbConfig) client.getSession().readRegistry(SModConsts.HRS_CFG, new int[] { SUtilConsts.BPR_CO_ID });
        oBizPartner = (SDataBizPartner) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.BPSU_BP, new int[] { bankId }, SLibConstants.EXEC_MODE_SILENT);
        moPayroll = (SDbPayroll)  client.getSession().readRegistry(SModConsts.HRS_PAY, new int[] { payrollId }, SDbConsts.MODE_STEALTH);
        sDescription = (moPayroll.getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_WEE ? SHrsFormerConsts.PAY_WEE_ABB : SHrsFormerConsts.PAY_FOR_ABB ) + moPayroll.getNumber() + " " + formatDateData.format(dateApplication);
        sCompany = SLibUtilities.textToAlphanumeric(((SClientInterface) client).getSessionXXX().getCompany().getDbmsDataCompany().getBizPartner());
        employeesId = SLibUtils.textImplode(employees, ",");
        
        fileName = formatDateTitle.format(new Date()).concat(" bmx nom.txt");

        client.getFileChooser().setSelectedFile(new File(fileName));
        if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());

            try {
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ASCII"));
                
                sAccountDebit = SLibUtilities.textTrim(accountDebit).replace("-", "");
                
                // control registry (type 1)
                buffer += "1"; //type of registry
                buffer += SLibUtilities.textRepeat("0", 12 - oBizPartner.getDbmsCategorySettingsSup().getCompanyKey().length()).concat(oBizPartner.getDbmsCategorySettingsSup().getCompanyKey()); //client
                buffer += formatDateData.format(dateApplication); //payment date
                buffer += SLibUtilities.textRepeat("0", 4 - ("" + consecutiveDay).length()).concat("" + consecutiveDay); //sequential number
                buffer += sCompany.length() > 36 ? sCompany.substring(0,35) : sCompany.concat(SLibUtilities.textRepeat(" ", 36 - sCompany.length())); //company's name
                buffer += sDescription.concat(SLibUtilities.textRepeat(" ", (sDescription.length() >= 20 ? 0 : 20 - sDescription.length()))); //description
                buffer += "15"; //nature of file
                buffer += "D"; //layout version
                buffer += "01"; //type of debit
                
                bw.write(buffer);
                bw.newLine();
                
                // global registry (Type 2)
                sql = "SELECT rcp.id_emp, emp.bank_acc, " +
                        "(SELECT COALESCE(SUM(rcp_ear.amt_r), 0) " +
                        "FROM hrs_pay_rcp AS r " +
                        "INNER JOIN hrs_pay_rcp_ear AS rcp_ear ON rcp_ear.id_pay = r.id_pay AND rcp_ear.id_emp = r.id_emp " +
                        "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ear.b_del = 0 AND rcp_ear.id_emp = rcp.id_emp) - " +
                        "(SELECT COALESCE(SUM(rcp_ded.amt_r), 0) " +
                        "FROM hrs_pay_rcp AS r " +
                        "INNER JOIN hrs_pay_rcp_ded AS rcp_ded ON rcp_ded.id_pay = r.id_pay AND rcp_ded.id_emp = r.id_emp " +
                        "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ded.b_del = 0 AND rcp_ded.id_emp = rcp.id_emp) AS pay_net " +
                        "FROM hrs_pay AS p " +
                        "INNER JOIN hrs_pay_rcp AS rcp ON rcp.id_pay = p.id_pay " +
                        "INNER JOIN erp.hrsu_emp AS emp ON emp.id_emp = rcp.id_emp " +
                        "WHERE p.b_del = 0 AND rcp.b_del = 0 AND LENGTH(emp.bank_acc) > 0 AND rcp.id_pay = " + payrollId + " AND rcp.pay_r > 0 " +
                        "AND rcp.id_emp IN (" + employeesId + ")";
                
                resultSetHeader = client.getSession().getStatement().executeQuery(sql);
                while (resultSetHeader.next()) {
                    total += resultSetHeader.getDouble("pay_net");
                    nMoveNumTotal++;
                }
                
                buffer = "";
                buffer += "2"; //type of registry
                buffer += "1"; //type of operation
                buffer += "001"; //currency key
                buffer += formatDescTotal.format(total).replace(".", ""); //amount to be debit
                buffer += "01"; //account type
                buffer += SLibUtilities.textRepeat("0", 20 - sAccountDebit.length()).concat(sAccountDebit); //account number
                buffer += SLibUtilities.textRepeat("0", 6 - ("" + nMoveNumTotal).length()).concat("" + nMoveNumTotal); //total credits
                
                bw.write(buffer);
                bw.newLine();
                
                // detail registry (type 3)
                sql = "SELECT rcp.id_emp, emp.bank_acc, COALESCE(emp.fk_bank_n, 0) AS _bank_id, " +
                        "(SELECT COALESCE(SUM(rcp_ear.amt_r), 0) " +
                        "FROM hrs_pay_rcp AS r " +
                        "INNER JOIN hrs_pay_rcp_ear AS rcp_ear ON rcp_ear.id_pay = r.id_pay AND rcp_ear.id_emp = r.id_emp " +
                        "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ear.b_del = 0 AND rcp_ear.id_emp = rcp.id_emp) - " +
                        "(SELECT COALESCE(SUM(rcp_ded.amt_r), 0) " +
                        "FROM hrs_pay_rcp AS r " +
                        "INNER JOIN hrs_pay_rcp_ded AS rcp_ded ON rcp_ded.id_pay = r.id_pay AND rcp_ded.id_emp = r.id_emp " +
                        "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ded.b_del = 0 AND rcp_ded.id_emp = rcp.id_emp) AS pay_net " +
                        "FROM hrs_pay AS p " +
                        "INNER JOIN hrs_pay_rcp AS rcp ON rcp.id_pay = p.id_pay " +
                        "INNER JOIN erp.hrsu_emp AS emp ON emp.id_emp = rcp.id_emp " +
                        "INNER JOIN erp.bpsu_bp bp ON (emp.id_emp = bp.id_bp) " +
                        "WHERE p.b_del = 0 AND rcp.b_del = 0 AND LENGTH(emp.bank_acc) > 0 AND rcp.id_pay = " + payrollId + " AND rcp.pay_r > 0 " +
                        "AND rcp.id_emp IN (" + employeesId + ") " +
                        "ORDER BY bp.bp, emp.num;";
                
                resultSetDetail = client.getSession().getStatement().executeQuery(sql);
                while (resultSetDetail.next()) {
                    buffer = "";
                    
                    sAccountCredit = SLibUtilities.textTrim(resultSetDetail.getString("emp.bank_acc"));
                    balance = resultSetDetail.getDouble("pay_net");
                    sBizPartner = SFinUtilities.getBizPartnerForBanamex(client.getSession(), resultSetDetail.getInt("rcp.id_emp"));
                    nEmployeeBankAuxId = resultSetDetail.getInt("_bank_id");
                    nEmployeeBankId = nEmployeeBankAuxId == 0 ? moConfig.getFkBankId() : nEmployeeBankAuxId;
                    
                    if (nEmployeeBankId != oBizPartner.getFkFiscalBankId()) {
                        nBankKey = SLibUtilities.parseInt((sAccountCredit.length() > 10 ? sAccountCredit.substring(0, 3) : "000"));
                        
                        n = (int) (Math.floor(Math.log10(nBankKey)) + 1);
                    }
                    
                    buffer += "3"; //type of registry
                    buffer += "0"; //type of operation
                    buffer += (nEmployeeBankId != oBizPartner.getFkFiscalBankId() ? "002" : "001"); //payment method
                    buffer += "01"; //payment type
                    buffer += "001"; //currency key
                    buffer += formatDescTotal.format(balance).replace(".", ""); //amount
                    buffer += (nEmployeeBankId != oBizPartner.getFkFiscalBankId() ? "40" : "03"); //type of account credit
                    buffer += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 20 ? 0 : 20 - sAccountCredit.length())).concat(sAccountCredit); //Credit account
                    buffer += (nEmployeeBankId != oBizPartner.getFkFiscalBankId() ? formatDateData.format(dateApplication).concat((SLibUtilities.textRepeat(" ", (formatDateData.format(dateApplication).length() == 16 ? 0 : 16 - formatDateData.format(dateApplication).length())))) : sDescription.concat(sDescription.length() > 16 ? sDescription.substring(0,15) : (SLibUtilities.textRepeat(" ", (sDescription.length() == 16 ? 0 : 16 - sDescription.length()))))); //payment reference
                    buffer += sBizPartner.concat(sBizPartner.length() > 55 ? sBizPartner.substring(0,54) : (SLibUtilities.textRepeat(" ", (sBizPartner.length() == 55 ? 0 : 55 - sBizPartner.length())))); //beneficiary
                    buffer += SLibUtilities.textRepeat(" ", 35); //reference 1
                    buffer += SLibUtilities.textRepeat(" ", 35); //reference 2
                    buffer += SLibUtilities.textRepeat(" ", 35); //reference 3
                    buffer += SLibUtilities.textRepeat(" ", 35); //reference 4
                    buffer += (nEmployeeBankId != oBizPartner.getFkFiscalBankId() ? SLibUtilities.textRepeat("0", 4 - n).concat(nBankKey + "") : SLibUtilities.textRepeat("0", 4)); //key bank
                    buffer += "00"; //term
                    buffer += SLibUtilities.textRepeat(" ", 14); //RFC
                    buffer += SLibUtilities.textRepeat("0", 8); //IVA
                    buffer += SLibUtilities.textRepeat(" ", 80); //future use
                    buffer += SLibUtilities.textRepeat(" ", 50); //future use
                    
                    bw.write(buffer);
                    bw.newLine();
                }
                
                // Summary:
                
                buffer = "";
                buffer += "4";//type of registry
                buffer += "001";//currency key
                buffer += SLibUtilities.textRepeat("0", 6 - (nMoveNumTotal + "").length()).concat(nMoveNumTotal + ""); //number of credits
                buffer += formatDescTotal.format(total).replace(".", ""); //total amount of credits
                buffer += SLibUtilities.textRepeat("0", 5).concat("1"); //number of debit
                buffer += formatDescTotal.format(total).replace(".", ""); //total amount of debit

                bw.write(buffer);
                bw.newLine();
                bw.flush();
                bw.close();

                showPayrollLayoutTotal(client, total);
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
     * Creates payroll bank-layout for BBVA Bancomer.
     * @param client
     * @param payrollId
     * @param dateApplication
     * @param employees Employees IDs.
    */
    public static void createPayrollLayoutBbva(SGuiClient client, int payrollId, Date dateApplication, String[] employees) {
        ResultSet resulSet = null;
        Statement statement = null;
        String sql = "";
        String fileName = "";
        String sBizPartner = "";
        String employeesId = "";
        int nMoveNum = 1;
        int n = 0;
        String sAccountCredit = "";
        String buffer = "";
        DecimalFormat formatDesc = new DecimalFormat("0000000000000.00");
        SimpleDateFormat formatDateTitle = new SimpleDateFormat("yyMMdd HHmm");
        double balance = 0;
        double total = 0;
        
        fileName = formatDateTitle.format(new Date()).concat(" bbva nom.txt");
        
        employeesId = SLibUtils.textImplode(employees, ",");
        
        client.getFileChooser().setSelectedFile(new File(fileName));
        if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());

            try {
                statement = client.getSession().getStatement();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ASCII"));
                
                sql = "SELECT rcp.id_emp, emp.bank_acc, b.bp, " +
                        "(SELECT COALESCE(SUM(rcp_ear.amt_r), 0) " +
                        "FROM hrs_pay_rcp AS r " +
                        "INNER JOIN hrs_pay_rcp_ear AS rcp_ear ON rcp_ear.id_pay = r.id_pay AND rcp_ear.id_emp = r.id_emp " +
                        "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ear.b_del = 0 AND rcp_ear.id_emp = rcp.id_emp) - " +
                        "(SELECT COALESCE(SUM(rcp_ded.amt_r), 0) " +
                        "FROM hrs_pay_rcp AS r " +
                        "INNER JOIN hrs_pay_rcp_ded AS rcp_ded ON rcp_ded.id_pay = r.id_pay AND rcp_ded.id_emp = r.id_emp " +
                        "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ded.b_del = 0 AND rcp_ded.id_emp = rcp.id_emp) AS _pay_net " +
                        "FROM hrs_pay AS p " +
                        "INNER JOIN hrs_pay_rcp AS rcp ON rcp.id_pay = p.id_pay " +
                        "INNER JOIN erp.hrsu_emp AS emp ON emp.id_emp = rcp.id_emp " +
                        "INNER JOIN erp.bpsu_bp AS b ON emp.id_emp = b.id_bp " +
                        "WHERE p.b_del = 0 AND rcp.b_del = 0 AND LENGTH(emp.bank_acc) > 0 AND rcp.id_pay = " + payrollId + " AND rcp.pay_r > 0 " +
                        "AND rcp.id_emp IN (" + employeesId + ") " +
                        "ORDER BY rcp.id_emp, emp.bank_acc, b.bp";
                
                resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    buffer = "";
                    
                    sAccountCredit = SLibUtilities.textTrim(resulSet.getString("emp.bank_acc"));
                    balance = resulSet.getDouble("_pay_net");
                    sBizPartner = SLibUtilities.textToAlphanumeric(SLibUtilities.textTrim(resulSet.getString("b.bp")));
                  
                    n = (int) (Math.floor(Math.log10(nMoveNum)) + 1);
                     
                    buffer += SLibUtilities.textRepeat("0", 9 - n).concat(nMoveNum++ + "");
                    buffer += SLibUtilities.textRepeat(" ", 16); // Blank
                    buffer += "99";
                    buffer += sAccountCredit.concat(sAccountCredit.length() > 20 ? sAccountCredit.substring(0,20) : (SLibUtilities.textRepeat(" ", (sAccountCredit.length() == 20 ? 0 : 20 - sAccountCredit.length())))); 
                    buffer += formatDesc.format(balance).replace(".", "");
                    buffer += sBizPartner.concat(sBizPartner.length() > 40 ? sBizPartner.substring(0,40) : (SLibUtilities.textRepeat(" ", (sBizPartner.length() == 40 ? 0 : 40 - sBizPartner.length())))); //beneficiary
                    buffer += "001";
                    buffer += "001";
                    
                    bw.write(buffer);
                    total += balance;
                    bw.newLine();
                }

                bw.flush();
                bw.close();
                
                showPayrollLayoutTotal(client, total);
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
     * Creates payroll bank-layout for HSBC.
     * @param client
     * @param payrollId
     * @param dateApplication
     * @param employees Employees IDs.
     * @param accountDebit
     */
    public static void createPayrollLayoutHsbc(SGuiClient client, int payrollId, Date dateApplication, String[] employees, String accountDebit) {
        ResultSet resulSet = null;
        Statement statement = null;
        String sql = "";
        String fileName = "";
        String employeesId = "";
        SimpleDateFormat formatDateTitle = new SimpleDateFormat("yyMMdd HHmm");
        String sNameEmploy = "";
        String sAccountCredit = "";
        StringBuilder headerLayout = new StringBuilder();
        StringBuilder bodyLayout = new StringBuilder();
        int nCont = 0;
        int type = 0;
        double balance = 0;
        double total = 0;
        
        sAccountCredit = SLibUtilities.textTrim(accountDebit);

        SimpleDateFormat formatDate = new SimpleDateFormat("ddMMyyyy");

        fileName = formatDateTitle.format(new Date()).concat(" HSBC nom.csv");

        employeesId = SLibUtils.textImplode(employees, ",");

        client.getFileChooser().setSelectedFile(new File(fileName));

        if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());

            try {   
                statement = client.getSession().getStatement();

                sql = "SELECT rcp.id_emp, emp.bank_acc, b.bp, p.fk_tp_pay, " +
                        "(SELECT COALESCE(SUM(rcp_ear.amt_r), 0) " +
                        "FROM hrs_pay_rcp AS r " +
                        "INNER JOIN hrs_pay_rcp_ear AS rcp_ear ON rcp_ear.id_pay = r.id_pay AND rcp_ear.id_emp = r.id_emp " +
                        "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ear.b_del = 0 AND rcp_ear.id_emp = rcp.id_emp) - " +
                        "(SELECT COALESCE(SUM(rcp_ded.amt_r), 0) " +
                        "FROM hrs_pay_rcp AS r " +
                        "INNER JOIN hrs_pay_rcp_ded AS rcp_ded ON rcp_ded.id_pay = r.id_pay AND rcp_ded.id_emp = r.id_emp " +
                        "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ded.b_del = 0 AND rcp_ded.id_emp = rcp.id_emp) AS _pay_net " +
                        "FROM hrs_pay AS p " +
                        "INNER JOIN hrs_pay_rcp AS rcp ON rcp.id_pay = p.id_pay " +
                        "INNER JOIN erp.hrsu_emp AS emp ON emp.id_emp = rcp.id_emp " +
                        "INNER JOIN erp.bpsu_bp AS b ON emp.id_emp = b.id_bp " +
                        "WHERE p.b_del = 0 AND rcp.b_del = 0 AND LENGTH(emp.bank_acc) > 0 AND rcp.id_pay = " + payrollId + " AND rcp.pay_r > 0 " +
                        "AND rcp.id_emp IN (" + employeesId + ") " +
                        "ORDER BY rcp.id_emp, emp.bank_acc, b.bp;";

                resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    nCont++;
                    type = resulSet.getInt("fk_tp_pay");
                    sNameEmploy = resulSet.getString("bp");
                    String[] parts = sNameEmploy.split(",");
                    sNameEmploy = parts[1] + " " + parts[0];
                    balance = resulSet.getDouble("_pay_net");
                    total = SLibUtils.roundAmount(total + balance);
                    sNameEmploy = removeSpecialChars(sNameEmploy);
                    bodyLayout.append(SLibUtilities.textTrim(resulSet.getString("emp.bank_acc"))).append(',');
                    bodyLayout.append(balance).append(',');
                    bodyLayout.append("PAGO NOMINA").append(',');
                    bodyLayout.append(sNameEmploy.substring(1,sNameEmploy.length() <= 35 ? sNameEmploy.length() : 35)).append(",,,,");
                    bodyLayout.append("\r\n");
                }
                
                headerLayout.append("MXPRLF").append(',');
                headerLayout.append("F").append(",");
                headerLayout.append(sAccountCredit).append(',');
                headerLayout.append(total).append(',');
                headerLayout.append(nCont).append(',');
                headerLayout.append(formatDate.format(new Date())).append(',');
                headerLayout.append("").append(',');
                headerLayout.append("PAGO NOMINA ").append( type == SModSysConsts.HRSS_TP_PAY_WEE ? "SEMANAL" : "QUINCENAL");
                headerLayout.append("\r\n");
                headerLayout.append(bodyLayout.toString());

                String path = file.toString();
                try {
                    OutputStream outputStream = new FileOutputStream(path.endsWith(".csv") ? path : path + ".csv");
                    Writer outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
                    outputStreamWriter.write(headerLayout.toString());
                    outputStreamWriter.close();
                }
                catch (FileNotFoundException ex) {
                    Logger.getLogger(SHrsUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                showPayrollLayoutTotal(client, total);
                if (client.showMsgBoxConfirm(SLibConstants.MSG_INF_FILE_CREATE + file.getPath() + "\n" + SLibConstants.MSG_CNF_FILE_OPEN) == JOptionPane.YES_OPTION) {
                    SLibUtilities.launchFile(file.getPath());
                }
            }
            catch (SQLException | IOException e) {
                SLibUtilities.renderException(STableUtilities.class.getName(), e);
            }
        }
    }
	
    /**
     * 
     * @param client
     * @param dateApplicationIni
     * @param dateApplicationEnd
     * @return
     * @throws SQLException 
     */
    private static ArrayList prepareSqlQueryHigh(SGuiClient client, Date dateApplicationIni, Date dateApplicationEnd) throws SQLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateIni = formatter.format(dateApplicationIni);
        String dateEnd = formatter.format(dateApplicationEnd);
        String sqlId = "";
        ResultSet resultSet = null;
        int[] employeeId = null;

        sqlId = "SELECT id_emp FROM hrs_emp_log_hire " 
                + "WHERE dt_hire >= '" + dateIni + "' AND dt_hire <= '" + dateEnd + "' "
                + "AND NOT b_del AND b_hire = " + SModConsts.HRSX_HIRE_ACTIVE ;

        resultSet = client.getSession().getStatement().executeQuery(sqlId);
        ArrayList <Integer> resultsId = new ArrayList();

        while (resultSet.next()) {
          resultsId.add(resultSet.getInt("id_emp"));                  
        }
        return resultsId;
    }
    
    /**
     * 
     * @param client
     * @param dateApplicationIni
     * @param dateApplicationEnd
     * @return
     * @throws SQLException 
     */
    private static ArrayList prepareSqlQueryMod(SGuiClient client, Date dateApplicationIni, Date dateApplicationEnd) throws SQLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateIni = formatter.format(dateApplicationIni);
        String dateEnd = formatter.format(dateApplicationEnd);
        String sqlId = "";
        ResultSet resultSet = null;
        int[] employeeId = null;

        sqlId = "SELECT id_emp FROM hrs_emp_log_sal_ssc " 
                + "WHERE dt >= '" + dateIni + "' AND dt <= '" + dateEnd + "' " 
                + "AND sal_ssc != " + SModConsts.HRSX_HIRE_DISMISSED + " AND NOT b_del ";        

        resultSet = client.getSession().getStatement().executeQuery(sqlId);
        ArrayList <Integer> resultsId = new ArrayList();

        while (resultSet.next()) {
          resultsId.add(resultSet.getInt("id_emp"));                  
        }
        return resultsId;
    }
    
    /**
     * 
     * @param client
     * @param dateApplicationIni
     * @param dateApplicationEnd
     * @return
     * @throws SQLException 
     */
    private static ArrayList prepareSqlQueryLow(SGuiClient client, Date dateApplicationIni, Date dateApplicationEnd) throws SQLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateIni = formatter.format(dateApplicationIni);
        String dateEnd = formatter.format(dateApplicationEnd);
        String sqlId = "";
        ResultSet resultSet = null;
        int[] employeeId = null;

        sqlId = "SELECT id_emp FROM hrs_emp_log_hire "
                + "WHERE dt_dis_n >= '" + dateIni + "' AND dt_dis_n <= '" + dateEnd + "' "
                + "AND NOT b_del AND b_hire = " + SModConsts.HRSX_HIRE_DISMISSED + " "; 

        resultSet = client.getSession().getStatement().executeQuery(sqlId);
        ArrayList <Integer> resultsId = new ArrayList();

        while (resultSet.next()) {
          resultsId.add(resultSet.getInt("id_emp"));                  
        }
        return resultsId;
    }
     
    /**
     * 
     * @param client
     * @param layoutSuaType Type Layout
     * @param dateLayoutStart Date start layout
     * @param dateLayoutEnd  Date final layout
     */
    public static void createLayoutEmployeeRegister(SGuiClient client, int layoutSuaType, Date dateLayoutStart, Date dateLayoutEnd) {
        ResultSet resultSetHeader = null;
        BufferedWriter bw = null;
        Statement statement = null;
        SimpleDateFormat formatDateData = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat formatDateTitle = new SimpleDateFormat("yyyyMMdd HHmm");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateIni = formatter.format(dateLayoutStart);
        String dateEnd = formatter.format(dateLayoutEnd);        
        java.lang.String buffer = "";
        String sql = "";
        String fileName = "";
        String param = "";
        String ssn = "";
        String name = "";
        String fatherName = "";
        String motherName = "";
        String curp = "";
        String salaryType = "";
        String workerKey = "";
        String guia = "";
        double baseSalary = 0;
        Date dateApplication = null;

        fileName = formatDateTitle.format(new Date()).concat(" Altas.txt");
        
        client.getFileChooser().setSelectedFile(new File(fileName));
        if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());

            try {
                statement = client.getSession().getStatement();
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ASCII"));
                ArrayList <Integer> pkPrimaryK = prepareSqlQueryHigh(client, dateLayoutStart, dateLayoutEnd);
    
                for (int i = 0; i <= pkPrimaryK.size()-1; i++){
                int pkUser = pkPrimaryK.get(i);
                    
                    sql = "SELECT bp.firstname AS Nombre, bp.alt_id AS CURP, emp.lastname1 AS ApellidoP, emp.lastname2 AS ApellidoM, "
                            + "emp.num AS ClaveTrab, emp.ssn AS SSN, emp.sal_ssc AS Salario, e.id_tp_emp AS TpTrabajador, "
                            + "sal.id_tp_sal AS TpSalario, wrktp.id_tp_work_day AS Jornada, cfg.ss_subbra AS Guia, hire.dt_hire AS DateApplication, par.reg_ss AS Param "
                            + "FROM erp.HRSU_EMP AS emp "
                            + "INNER JOIN erp.BPSU_BP AS bp ON bp.id_bp = emp.id_emp "
                            + "INNER JOIN erp.HRSU_TP_EMP AS e ON e.id_tp_emp = emp.fk_tp_emp "
                            + "INNER JOIN erp.hrss_tp_sal AS sal ON sal.id_tp_sal = emp.fk_tp_sal "
                            + "INNER JOIN erp.hrss_tp_work_day AS wrktp ON emp.fk_tp_work_day = wrktp.id_tp_work_day "
                            + "INNER JOIN hrs_emp_log_hire AS hire ON hire.id_emp = emp.id_emp "
                            + "INNER JOIN cfg_param_co AS par "
                            + "INNER JOIN hrs_cfg AS cfg "
                            + "WHERE hire.b_hire = " + (layoutSuaType == SModConsts.HRSX_HIRE_ACTIVE ? SModConsts.HRSX_HIRE_ACTIVE : SModConsts.HRSX_HIRE_DISMISSED) + " AND not hire.b_del "
                            + "AND hire.dt_hire >= '" + dateIni + "' AND hire.dt_hire <= '" + dateEnd + "' "
                            + "AND emp.id_emp = " + pkUser;

                    resultSetHeader = client.getSession().getStatement().executeQuery(sql);

                    while (resultSetHeader.next()) {
                        param = resultSetHeader.getString("Param");
                        dateApplication = resultSetHeader.getDate("DateApplication");
                        ssn = resultSetHeader.getString("SSN");
                        baseSalary = resultSetHeader.getDouble("Salario");
                        salaryType = resultSetHeader.getString("TpSalario");
                        workerKey = resultSetHeader.getString("ClaveTrab");
                        name = resultSetHeader.getString("Nombre");
                        fatherName = resultSetHeader.getString("ApellidoP");
                        motherName = resultSetHeader.getString("ApellidoM");
                        curp = resultSetHeader.getString("CURP");
                        guia = resultSetHeader.getString("Guia");
                    }

                    buffer += param.substring(0, 10); // (Employer registration)
                    buffer += param.substring(10); // (Digit verifier of R.P)
                    buffer += (ssn.length() > 10 ? ssn.substring(0, 9) : ssn.concat((SLibUtilities.textRepeat(" ", (ssn.length() == 10 ? 0 : 10 - ssn.length()))))); // (Social Security number)
                    buffer += (ssn.length() > 10 ? ssn.substring(9) : " " ); // (Check digit of the NSS)
                    buffer += removeSpecialChars(fatherName).concat(fatherName.length() > 27 ? fatherName.substring(0, 27) : (SLibUtilities.textRepeat(" ", (fatherName.length() == 27 ? 0 : 27 - fatherName.length())))); // (Last name)
                    buffer += removeSpecialChars(motherName).concat(motherName.length() > 27 ? motherName.substring(0, 27) : (SLibUtilities.textRepeat(" ", (motherName.length() == 27 ? 0 : 27 - motherName.length())))); // (Mother's last name)
                    buffer += removeSpecialChars(name).concat(name.length() > 27 ? name.substring(0, 27) : (SLibUtilities.textRepeat(" ", (name.length() == 27 ? 0 : 27 - name.length())))); // (name)
                    String.format("%.2f", baseSalary);
                    String baseSalaryS = String.valueOf(baseSalary);
                    baseSalaryS = baseSalaryS.replaceAll("\\.","");
                    buffer += (baseSalaryS.length() > 6 ? baseSalaryS.substring(0, 6) : (SLibUtilities.textRepeat("0", (baseSalaryS.length() == 6 ? 0 : 6 - baseSalaryS.length())))).concat(baseSalaryS); // (Salary base quote)
                    buffer += String.valueOf(SLibUtilities.textRepeat(" ", 6));
                    buffer += "1"; // (Type of worker)
                    buffer += (salaryType.equals(1) ? "0" : salaryType.equals(2) ? "1" : "2"); //(Type of salary)
                    buffer += "0"; //(Week or reduced working day)
                    buffer += formatDateData.format(dateApplication); // (Movement date)
                    buffer += "000"; //(Family medicine unit)
                    buffer += String.valueOf(SLibUtilities.textRepeat(" ", 2));
                    buffer += "08"; // (Movement type)
                    buffer += (guia + "400"); // (Guide)
                    buffer += (workerKey.length() > 10 ? workerKey.substring(0, 10) : (SLibUtilities.textRepeat("0", (workerKey.length() == 10 ? 0 : 10 - workerKey.length())))).concat(workerKey); // (Worker's code)
                    buffer += SLibUtilities.textRepeat(" ", 1);
                    buffer += curp; // CURP
                    buffer += "9"; // (Format Identifier)
                    buffer += "\n";
                }
                    
                bw.write(buffer);
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
     * 
     * @param client
     * @param layoutSuaType Type Layout
     * @param dateLayoutStart Date start layout
     * @param dateLayoutEnd  Date final layout
     */
    public static void createLayoutEmployeeModification(SGuiClient client, int layoutSuaType, Date dateLayoutStart, Date dateLayoutEnd) {
        ResultSet resultSetHeader = null;
        BufferedWriter bw = null;
        Statement statement = null;
        SimpleDateFormat formatDateData = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat formatDateTitle = new SimpleDateFormat("yyyyMMdd HHmm");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateIni = formatter.format(dateLayoutStart);
        String dateEnd = formatter.format(dateLayoutEnd); 
        java.lang.String buffer = "";
        String sql = "";
        String param = "";
        String fileName = "";
        String ssn = "";
        String name = "";
        String fatherName = "";
        String motherName = "";
        String curp = "";
        String salaryType = "";
        String workerKey = "";
        String guia = "";
        double baseSalary = 0;
        Date dateApplication = null;
        
        fileName = formatDateTitle.format(new Date()).concat(" SBC.txt");
        
        client.getFileChooser().setSelectedFile(new File(fileName));
        if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());

            try {
                statement = client.getSession().getStatement();
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ASCII"));
                ArrayList <Integer> pkPrimaryK = prepareSqlQueryMod(client, dateLayoutStart, dateLayoutEnd);
    
                for (int i = 0; i <= pkPrimaryK.size()-1; i++){
                    int pkUser = pkPrimaryK.get(i);

                    sql = "SELECT bp.firstname AS Nombre, bp.alt_id AS CURP, emp.lastname1 AS ApellidoP, emp.lastname2 AS ApellidoM, "
                            + "emp.num AS ClaveTrab, emp.ssn AS SSN, emp.sal_ssc AS Salario, e.id_tp_emp AS TpTrabajador, " 
                            + "sal.id_tp_sal AS TpSalario, wrktp.id_tp_work_day AS Jornada, cfg.ss_subbra AS Guia, ssc.dt AS DateApplication, par.reg_ss AS Param "
                            + "FROM erp.HRSU_EMP AS emp "
                            + "INNER JOIN erp.BPSU_BP AS bp ON bp.id_bp = emp.id_emp "
                            + "INNER JOIN erp.HRSU_TP_EMP AS e ON e.id_tp_emp = emp.fk_tp_emp "
                            + "INNER JOIN erp.hrss_tp_sal AS sal ON sal.id_tp_sal = emp.fk_tp_sal "
                            + "INNER JOIN erp.hrss_tp_work_day AS wrktp ON emp.fk_tp_work_day = wrktp.id_tp_work_day "
                            + "INNER JOIN HRS_EMP_LOG_SAL_SSC AS ssc ON ssc.id_emp = emp.id_emp "
                            + "INNER JOIN cfg_param_co AS par "
                            + "INNER JOIN hrs_cfg AS cfg "
                            + "WHERE ssc.dt >= '" + dateIni + "' AND ssc.dt <= '" + dateEnd + "' AND NOT ssc.b_del "
                            + "AND emp.id_emp = " + pkUser ; 

                    resultSetHeader = client.getSession().getStatement().executeQuery(sql);

                    while (resultSetHeader.next()) {
                        param = resultSetHeader.getString("Param");
                        ssn = resultSetHeader.getString("SSN");
                        dateApplication = resultSetHeader.getDate("DateApplication");
                        baseSalary = resultSetHeader.getDouble("Salario");
                        salaryType = resultSetHeader.getString("TpSalario");
                        workerKey = resultSetHeader.getString("ClaveTrab");
                        name = resultSetHeader.getString("Nombre");
                        fatherName = resultSetHeader.getString("ApellidoP");
                        motherName = resultSetHeader.getString("ApellidoM");
                        curp = resultSetHeader.getString("CURP");
                        guia = resultSetHeader.getString("Guia");
                    }             

                    buffer += param.substring(0, 10); // (Employer registration)
                    buffer += param.substring(10); // (Digit verifier of R.P)
                    buffer += (ssn.length() > 10 ? ssn.substring(0, 9) : ssn.concat((SLibUtilities.textRepeat(" ", (ssn.length() == 10 ? 0 : 10 - ssn.length()))))); // (Social Security number)
                    buffer += (ssn.length() > 10 ? ssn.substring(9) : " " ); // (Check digit of the NSS)
                    buffer += removeSpecialChars(fatherName).concat(fatherName.length() > 27 ? fatherName.substring(0, 27) : (SLibUtilities.textRepeat(" ", (fatherName.length() == 27 ? 0 : 27 - fatherName.length())))); // (Last name)
                    buffer += removeSpecialChars(motherName).concat(motherName.length() > 27 ? motherName.substring(0, 27) : (SLibUtilities.textRepeat(" ", (motherName.length() == 27 ? 0 : 27 - motherName.length())))); // (Mother's last name)
                    buffer += removeSpecialChars(name).concat(name.length() > 27 ? name.substring(0, 27) : (SLibUtilities.textRepeat(" ", (name.length() == 27 ? 0 : 27 - name.length())))); // (name)
                    String.format("%.2f", baseSalary);
                    String baseSalaryS = String.valueOf(baseSalary);
                    baseSalaryS = baseSalaryS.replaceAll("\\.","");
                    buffer += (baseSalaryS.length() > 6 ? baseSalaryS.substring(0, 6) : (SLibUtilities.textRepeat("0", (baseSalaryS.length() == 6 ? 0 : 6 - baseSalaryS.length())))).concat(baseSalaryS); // (Salary base quote)
                    buffer += String.valueOf(SLibUtilities.textRepeat(" ", 6));
                    buffer += String.valueOf(SLibUtilities.textRepeat(" ", 1));
                    buffer += (salaryType.equals(1) ? "0" : salaryType.equals(2) ? "1" : "2"); //(Type of salary)
                    buffer += "0"; //(Week or reduced working day)
                    buffer += formatDateData.format(dateApplication); // (Movement date)
                    buffer += String.valueOf(SLibUtilities.textRepeat(" ", 5));
                    buffer += "07"; // (Movement type)
                    buffer += (guia + "400"); // (Guide)
                    buffer += (workerKey.length() > 10 ? workerKey.substring(0, 10) : (SLibUtilities.textRepeat("0", (workerKey.length() == 10 ? 0 : 10 - workerKey.length())))).concat(workerKey); // (Worker's code)
                    buffer += SLibUtilities.textRepeat(" ", 1);
                    buffer += curp; // CURP
                    buffer += "9"; // (Format Identifier)
                    buffer += "\n";                                  
                }
                
                bw.write(buffer);
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
     * 
     * @param client
     * @param layoutSuaType Type Layout
     * @param dateLayoutStart Date start layout
     * @param dateLayoutEnd  Date final layout
     */
    public static void createLayoutEmployeeDeletion(SGuiClient client, int layoutSuaType, Date dateLayoutStart, Date dateLayoutEnd) {
        ResultSet resultSetHeader = null;
        BufferedWriter bw = null;
        Statement statement = null;
        SimpleDateFormat formatDateData = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat formatDateTitle = new SimpleDateFormat("yyyyMMdd HHmm");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateIni = formatter.format(dateLayoutStart);
        String dateEnd = formatter.format(dateLayoutEnd);
        java.lang.String buffer = "";
        String sql = "";
        String param = "";
        String fileName = "";
        String ssn = "";
        String name = "";
        String fatherName = "";
        String motherName = "";
        String workerKey = "";
        String guia = "";
        String type = "";
        Date dateApplication = null;
        
        fileName = formatDateTitle.format(new Date()).concat(" Bajas.txt");
        
        client.getFileChooser().setSelectedFile(new File(fileName));
        if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());

            try {
                statement = client.getSession().getStatement();
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ASCII"));
                ArrayList <Integer> pkPrimaryK = prepareSqlQueryLow(client, dateLayoutStart, dateLayoutEnd);
    
                for (int i = 0; i <= pkPrimaryK.size()-1; i++){
                int pkUser = pkPrimaryK.get(i);
                
                    sql = "SELECT bp.firstname AS Nombre, bp.alt_id AS CURP, emp.lastname1 AS ApellidoP, emp.lastname2 AS ApellidoM,"
                          + "emp.num AS ClaveTrab, emp.ssn AS SSN, emp.sal_ssc AS Salario, e.id_tp_emp AS TpTrabajador, sal.id_tp_sal AS TpSalario, "
                          + "wrktp.id_tp_work_day AS Jornada, cfg.ss_subbra AS Guia, hire.b_hire AS Motivo, hire.fk_tp_emp_dis AS Tipe, hire.dt_dis_n AS DateApplication, par.reg_ss AS Param "
                          + "FROM erp.HRSU_EMP AS emp "
                          + "INNER JOIN erp.BPSU_BP AS bp ON bp.id_bp = emp.id_emp "
                          + "INNER JOIN erp.HRSU_TP_EMP AS e ON e.id_tp_emp = emp.fk_tp_emp "
                          + "INNER JOIN erp.hrss_tp_sal AS sal ON sal.id_tp_sal = emp.fk_tp_sal "
                          + "INNER JOIN erp.hrss_tp_work_day AS wrktp ON emp.fk_tp_work_day = wrktp.id_tp_work_day "
                          + "INNER JOIN HRS_EMP_LOG_HIRE AS hire ON hire.id_emp = emp.id_emp "
                          + "INNER JOIN cfg_param_co AS par "
                          + "INNER JOIN hrs_cfg AS cfg "
                          + "WHERE hire.b_hire = " + SModConsts.HRSX_HIRE_DISMISSED + " AND not hire.b_del "
                          + "AND hire.dt_dis_n >= '" + dateIni + "' AND hire.dt_dis_n <= '" + dateEnd + "' "
                          + "AND emp.id_emp = " + pkUser;

                    resultSetHeader = client.getSession().getStatement().executeQuery(sql);
                        while (resultSetHeader.next()) {
                            dateApplication = resultSetHeader.getDate("DateApplication");
                            param = resultSetHeader.getString("Param");
                            ssn = resultSetHeader.getString("SSN");
                            workerKey = resultSetHeader.getString("ClaveTrab");
                            name = resultSetHeader.getString("Nombre");
                            fatherName = resultSetHeader.getString("ApellidoP");
                            motherName = resultSetHeader.getString("ApellidoM");
                            guia = resultSetHeader.getString("Guia");
                            type = resultSetHeader.getString("Tipe");
                        }


                    buffer += param.substring(0, 10); // (Employer registration)
                    buffer += param.substring(10); // (Digit verifier of R.P)
                    buffer += (ssn.length() > 10 ? ssn.substring(0, 9) : ssn.concat((SLibUtilities.textRepeat(" ", (ssn.length() == 10 ? 0 : 10 - ssn.length()))))); // (Social Security number)
                    buffer += (ssn.length() > 10 ? ssn.substring(9) : " " ); // (Check digit of the NSS)
                    buffer += removeSpecialChars(fatherName).concat(fatherName.length() > 27 ? fatherName.substring(0, 27) : (SLibUtilities.textRepeat(" ", (fatherName.length() == 27 ? 0 : 27 - fatherName.length())))); // (Last name)
                    buffer += removeSpecialChars(motherName).concat(motherName.length() > 27 ? motherName.substring(0, 27) : (SLibUtilities.textRepeat(" ", (motherName.length() == 27 ? 0 : 27 - motherName.length())))); // (Mother's last name)
                    buffer += removeSpecialChars(name).concat(name.length() > 27 ? name.substring(0, 27) : (SLibUtilities.textRepeat(" ", (name.length() == 27 ? 0 : 27 - name.length())))); // (name)
                    buffer += String.valueOf(SLibUtilities.textRepeat(" ", 15));
                    buffer += formatDateData.format(dateApplication); // (Movement date)
                    buffer += String.valueOf(SLibUtilities.textRepeat(" ", 5));
                    buffer += "02"; // (Movement type)
                    buffer += (guia + "400"); // (Guide)
                    buffer += (workerKey.length() > 10 ? workerKey.substring(0, 10) : (SLibUtilities.textRepeat("0", (workerKey.length() == 10 ? 0 : 10 - workerKey.length())))).concat(workerKey); // (Worker's code)
                    buffer += (type.equals(1) ? "6" : type.equals(2) ? "2" : type.equals(3) ? "1" : type.equals(4) ? "3" : "6" );
                    buffer += SLibUtilities.textRepeat(" ", 18);
                    buffer += "9"; // (Format Identifier)
                    buffer += "\n";
                }
                
                bw.write(buffer);
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

    public static void updateEmployeeSBC(SGuiClient client, int layoutSuaType, int idEmployeSBC, Date dateSystem) {
        ResultSet resultSetHeader = null;
        Statement statement = null;
        int id_emp = 0;
        int nLogId = 0;
        int id_Emp = idEmployeSBC;
        int b_del = 0;
        double sal = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = formatter.format(dateSystem);
        String sql = "";
        String usr_upd = "";
        String usr_ins = "";
             
        try {
            statement = client.getSession().getStatement();
                         
                sql = "SELECT COALESCE(MAX(id_log), 0) FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_SAL_SSC) + " WHERE id_emp = " + id_Emp ;
                resultSetHeader = client.getSession().getStatement().executeQuery(sql);
                    if (resultSetHeader.next()) {
                        nLogId = resultSetHeader.getInt(1);
                    }
                    // Comente el update para que no cambie nada en la BD
/*
                sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_SAL_SSC) + " SET " +
                    "id_emp = '" + id_emp + "', " +
                    "id_log = '" + nLogId + "', " +
                    "dt = '" + currentDate + "', " +
//                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(dt) + "', " +
                    "sal_ssc = '" + sal + "', " +
                    "b_del = '" + b_del + "', " +
                    "fk_usr_ins = '" + usr_ins + "', " +
                    "fk_usr_upd = '" + usr_upd + "', " +
                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    "WHERE id_emp = " + id_emp + " AND id_log = " + nLogId;

                    client.getSession().getStatement().execute(sql);
                    */
                    client.showMsgBoxInformation("SBC actualizados");

        }
        catch (SQLException e) {
            SLibUtilities.renderException(STableUtilities.class.getName(), e);
        }
    }
    
    /**
     * Checks if period is anniversary of provided date.
     * @param anniversary
     * @param periodStart
     * @param periodEnd
     * @return 
     */
    public static boolean isAnniversaryBelongingToPeriod(final Date anniversary, final Date periodStart, final Date periodEnd) throws Exception {
        SLibTimeUtils.validatePeriod(periodStart, periodEnd);
        int[] elementsDate = SLibTimeUtils.digestDate(anniversary);
        int[] elementsPeriodEnd = SLibTimeUtils.digestDate(periodEnd);
        elementsDate[0] = elementsPeriodEnd[0];
        Date newDate = SLibTimeUtils.createDate(elementsDate[0], elementsDate[1], elementsDate[2]);
        return SLibTimeUtils.isBelongingToPeriod(newDate, periodStart, periodEnd);
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
     * @param tpPaySht Type of paysheet
     * @return Payroll next number of provided payment type.
     */
    public static int getPayrollNextNumber(final SGuiSession session, final int year, final int paymentType, final int tpPaySht) throws Exception {
        int nextNumber = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COALESCE(MAX(num), 0) + 1 "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " "
                + "WHERE per_year = " + year + " AND fk_tp_pay = " + paymentType + " AND b_del = 0 "
                + (tpPaySht != SModSysConsts.HRSS_TP_PAY_SHT_EXT ? "AND fk_tp_pay_sht = " + tpPaySht : "");

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

        receiptIssues = new ArrayList<>();

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
            validNumber = 1;        // payroll number equal or less than 1 is set to 1, regardless of payment type
        }
        else {
            validNumber = number;   // assume by instance that number is correct

            switch(paymentType) {
                case SModSysConsts.HRSS_TP_PAY_WEE:
                    if (number >= SHrsConsts.YEAR_WEEKS_EXTENDED) {
                        if (!SLibTimeUtils.isLeapYear(year)) {
                            // maximum number of weekly payrolls must be 53 on ordinary years:
                            validNumber = SHrsConsts.YEAR_WEEKS_EXTENDED - 1;
                        }
                        else {
                            // maximum number of weekly payrolls must be 54 on leap years:
                            validNumber = SHrsConsts.YEAR_WEEKS_EXTENDED;
                        }
                    }
                    break;

                case SModSysConsts.HRSS_TP_PAY_FOR:
                    if (number > SHrsConsts.YEAR_FORTNIGHTS) {
                        // maximum number of fortnightly payrolls must be 24:
                        validNumber = SHrsConsts.YEAR_FORTNIGHTS;
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
     * @param config Object with information of configuration the module resource human.
     * @param workingDaySettings Configuration of days working.
     * @return true if is valid.
     * @throws Exception 
     */
    public static boolean validatePayroll(final SGuiSession session, final SDbConfig config, final SDbWorkingDaySettings workingDaySettings) throws Exception {
        if (config == null) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Configuración del módulo)");
        }
        else if (workingDaySettings == null) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Configuración de días laborables)");
        }
        else if (workingDaySettings.countWorkingDays() == 0) {
            throw new Exception(SLibConsts.ERR_MSG_UTILS_QTY_INVALID + " (Configuración de días laborables inválida)");
        }
        else if (config.getFkEarningEarningId_n() == 0) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración de percepción normal)");
        }
        else if (config.getFkEarningVacationId_n() == 0) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración de percepción vacaciones)");
        }
        /* 2019-02-07, Sergio Flores: Not implemented yet! Even not in form of payroll configuration!
        else if (config.getFkEarningTaxId_n() == 0) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración de percepción impuesto)");
        }
        */
        else if (config.getFkEarningTaxSubsidyId_n() == 0) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración de percepción subsidio para el empleo)");
        }
        /* 2019-02-07, Sergio Flores: Not implemented yet! Even not in form of payroll configuration!
        else if (config.getFkEarningSsContributionId_n()== 0) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración de percepción retención de SS)");
        }
        */
        else if (config.getFkDeductionTaxId_n() == 0) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración de deducción impuesto)");
        }
        /* 2019-02-07, Sergio Flores: Not implemented yet! Even not in form of payroll configuration!
        else if (config.getFkDeductionTaxSubsidyId_n()== 0) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración de deducción subsidio para el empleo)");
        }
        */
        else if (config.getFkDeductionSsContributionId_n() == 0) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración de deducción retención de SS)");
        }
        
        return true;
    }
    
    public static boolean validateEmployeeHireLog(final SGuiSession session, final int employeeId, final boolean isHire) throws Exception {
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

        String sql = "SELECT id_wds "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_WDS) + " "
                + "WHERE NOT b_del AND fk_tp_pay = " + paymentType + " ";

        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            workingDaySettings = (SDbWorkingDaySettings) session.readRegistry(SModConsts.HRS_WDS, new int[] { resultSet.getInt(1) });
        }

        return workingDaySettings;
    }
    
    /**
     * Counts business days in supplied time span.
     * @param dateStart Start of time span.
     * @param dateEnd End of time span.
     * @param workingDaySettings Working day settings applying.
     * @param holidays Holidays available.
     * @return Number of business days in supplied time span.
     */
    public static int countBusinessDays(final Date dateStart, final Date dateEnd, final SDbWorkingDaySettings workingDaySettings, final ArrayList<SDbHoliday> holidays) {
        int businessDays = 0;
        int periodDays = SLibTimeUtils.countPeriodDays(dateStart, dateEnd);
        Calendar calendar = Calendar.getInstance();
        
        SPAN:
        for (int day = 0; day < periodDays; day++) {
            Date date = SLibTimeUtils.addDate(dateStart, 0, 0, day);
            calendar.setTime(date);
            
            if (!workingDaySettings.isWorkingDay(calendar.get(Calendar.DAY_OF_WEEK))) {
                continue;
            }
                    
            for (SDbHoliday holiday : holidays) {
                if (holiday.getDate().compareTo(date) == 0) {
                    continue SPAN;
                }
            }
            
            businessDays++;
        }
            
        return businessDays;
    }

    /**
     * Counts true holidays in supplied time span. Holidays that match a non-working day are omitted.
     * @param dateStart Start of time span.
     * @param dateEnd End of time span.
     * @param workingDaySettings Working day settings applying.
     * @param holidays Holidays available.
     * @return Number of business days in supplied time span.
     */
    public static int countTrueHolidays(final Date dateStart, final Date dateEnd, final SDbWorkingDaySettings workingDaySettings, final ArrayList<SDbHoliday> holidays) {
        int trueHolydays = 0;
        int periodDays = SLibTimeUtils.countPeriodDays(dateStart, dateEnd);
        Calendar calendar = Calendar.getInstance();
        
        SPAN:
        for (int day = 0; day < periodDays; day++) {
            Date date = SLibTimeUtils.addDate(dateStart, 0, 0, day);
            calendar.setTime(date);
            
            if (!workingDaySettings.isWorkingDay(calendar.get(Calendar.DAY_OF_WEEK))) {
                continue;
            }
                    
            for (SDbHoliday holiday : holidays) {
                if (holiday.getDate().compareTo(date) == 0) {
                    trueHolydays++;
                    continue SPAN;
                }
            }
        }
            
        return trueHolydays;
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

        sql = "SELECT id_ear "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " "
                + "WHERE code = '" + codeToFind + "' AND NOT b_del "
                + "ORDER BY code, id_ear;";
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

        sql = "SELECT id_ded "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " "
                + "WHERE code = '" + codeToFind + "' AND NOT b_del "
                + "ORDER BY code, id_ded;";
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

    /**
     * Gets benefits table by earning.
     * @param session
     * @param earningId
     * @param paymentType
     * @param dateCutOff
     * @return
     * @throws Exception if benefit table is not found or on SQL exception as well.
     */
    public static SDbBenefitTable getBenefitTableByEarning(final SGuiSession session, final int earningId, final int paymentType, final Date dateCutOff) throws Exception {
        SDbBenefitTable benefitTable = null;

        String sql = "SELECT id_ben "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " "
                + "WHERE b_del = 0 AND fk_ear = " + earningId + " AND "
                + "dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(dateCutOff) + "' AND "
                + "(fk_tp_pay_n IS NULL" + (paymentType == 0 ? "" : " OR fk_tp_pay_n = " + paymentType) + ") "
                + "ORDER BY fk_tp_pay_n DESC, dt_sta DESC, id_ben " // priority to requested payment type, if any, and then the most recent starting date
                + "LIMIT 1;";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            benefitTable = new SDbBenefitTable();
            benefitTable.read(session, new int[] { resultSet.getInt("id_ben") });
        }
        
        if (benefitTable == null) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + "\nTabla de prestaciones adecuada para la fecha de corte '" + SLibUtils.DateFormatDate.format(dateCutOff) + "'.");
        }

        return benefitTable;
    }
    
    /**
     * Gets benefits table by deduction.
     * @param session
     * @param deductionId 
     * @param dateCutOff
     * @return
     * @throws Exception if benefit table is not found or on SQL exception as well.
     */
    public static SDbBenefitTable getBenefitTableByDeduction(final SGuiSession session, final int deductionId, final Date dateCutOff) throws Exception {
        SDbBenefitTable benefitTable = null;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_ben "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " "
            + "WHERE b_del = 0 AND fk_ded_n = " + deductionId + " AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(dateCutOff) + "' "
            + "ORDER BY dt_sta DESC, id_ben "
            + "LIMIT 1;";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            benefitTable = new SDbBenefitTable();
            benefitTable.read(session, new int[] { resultSet.getInt("id_ben") });
        }

        return benefitTable;
    }
    
    public static String getDisabilityName(final SGuiClient client, final String codeToFind) throws Exception {
        String disabilityName = null;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT name " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_DIS) + " " +
                "WHERE code = '" + codeToFind + "' ORDER BY code, id_tp_dis LIMIT 1 ";
        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            disabilityName = resultSet.getString("name");
        }

        return disabilityName;
    }
    
    /**
     * Create anniversary entries from 1 to 99 for each given table of benefits.
     * @param benefitTables
     * @return Array of anniversaries (from 1 to 100) for each given table of benefits.
     */
    public static ArrayList<SHrsBenefitTableAnniversary> createBenefitTablesAnniversarys(ArrayList<SDbBenefitTable> benefitTables) {
        ArrayList<SHrsBenefitTableAnniversary> benefitTableAnniversarys = new ArrayList<>();
        
        benefitTables.stream().filter((table) -> (!table.getChildRows().isEmpty())).forEach((table) -> {
            int currIndex = -1; // current index of row for current benefit table
            double benefit = 0; // current benefit, expressed as days or bonus percentage
            SDbBenefitTableRow tableRow = null; // current benefit-table row
            
            for (int year = 1; year <= 100; year++) {
                if (tableRow == null || (year * SLibTimeConsts.MONTHS) > tableRow.getMonths()) {
                    if (currIndex + 1 < table.getChildRows().size()) {
                        ++currIndex;
                        tableRow = table.getChildRows().get(currIndex);
                        benefit = table.getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_VAC_BON ? tableRow.getBenefitBonusPercentage() : tableRow.getBenefitDays();
                    }
                }
                
                benefitTableAnniversarys.add(new SHrsBenefitTableAnniversary(table.getPkBenefitId(), year, benefit));
            }
        });

        return benefitTableAnniversarys;
    }

    /**
     * Get recent tax table
     * @param session SGuiSession
     * @param start Date start date
     * @return id_tax int
     * @throws Exception
     */
    public static int getRecentTaxTable(final SGuiSession session, final Date start) throws SQLException, Exception {
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
    public static int getRecentTaxSubsidyTable(final SGuiSession session, final Date start) throws SQLException, Exception {
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
    public static int getRecentSsContributionTable(final SGuiSession session, final Date start) throws SQLException, Exception {
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
    
    public static int getRecentBenefitTable(final SGuiSession session, final int benefitType, final int paymentType, final Date dateCutOff) throws Exception {
        int id_ben = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_ben "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " "
            + "WHERE b_del = 0 AND fk_tp_ben = " + benefitType + " AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(dateCutOff) + "' "
            + (paymentType == 0 ? "AND fk_tp_pay_n IS NULL" : "AND (fk_tp_pay_n IS NULL OR fk_tp_pay_n = " + paymentType + ")") + " "
            + "ORDER BY dt_sta DESC, id_ben "
            + "LIMIT 1;";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            id_ben = resultSet.getInt(1);
        }

        return id_ben;
    }

    /**
     * Gets recent UMA.
     * @param session SGuiSession
     * @param start Date start.
     * @return UMA more recent value.
     * @throws Exception
     */
    public static double getRecentUma(final SGuiSession session, final Date start) throws Exception {
        double uma = 0;

        String sql = "SELECT amt "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_UMA) + " "
            + "WHERE NOT b_del AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(start) + "' "
            + "ORDER BY dt_sta DESC, id_uma "
            + "LIMIT 1;";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            uma = resultSet.getDouble(1);
        }

        return uma;
    }

    /**
     * Gets recent UMI.
     * @param session SGuiSession
     * @param start Date start.
     * @return UMI more recent value.
     * @throws Exception
     */
    public static double getRecentUmi(final SGuiSession session, final Date start) throws Exception {
        double umi = 0;

        String sql = "SELECT amt "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_UMI) + " "
            + "WHERE NOT b_del AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(start) + "' "
            + "ORDER BY dt_sta DESC, id_umi "
            + "LIMIT 1;";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            umi = resultSet.getDouble(1);
        }

        return umi;
    }

    /**
     * Gets recent minimum wage from desired minimum wage zone.
     * @param session SGuiSession
     * @param idMwzType Minimum wage zone
     * @param start Date start.
     * @return wage double
     * @throws Exception
     */
    public static double getRecentMinimumWage(final SGuiSession session, final int idMwzType, final Date start) throws Exception {
        double wage = 0;

        String sql = "SELECT wage "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_MWZ_WAGE) + " "
            + "WHERE NOT b_del AND id_tp_mwz = " + idMwzType + " AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(start) + "' "
            + "ORDER BY dt_sta DESC, id_wage "
            + "LIMIT 1;";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            wage = resultSet.getDouble(1);
        }

        return wage;
    }

    public static HashMap<String, Object> createPayrollReceiptMap(final SGuiClient client, final int[] payrollKey, final int pnPrintMode) throws java.lang.Exception {
        double dTotalPercepciones = 0;
        double dTotalDeducciones = 0;
        SDbLoan loan = null;
        SDataBizPartner bizPartnerCompany = null;
        SDataBizPartner bizPartnerEmployee = null;
        SDbPayroll payroll = null;
        SDbPayrollReceipt payrollReceipt = null;
        HashMap<String, Object> map = null;
        ArrayList aPercepciones = null;
        ArrayList aDeducciones = null;
        DecimalFormat oFixedFormatAux = null;

        payroll = new SDbPayroll();
        payroll.read(client.getSession(), new int[] { payrollKey[0] });
        
        for (SDbPayrollReceipt receipt : payroll.getChildPayrollReceipts()) { // TODO Improve this receipt lookup, is bizarre!
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

        map.put("bIsReceipt", true); // print as a simple receipt!

        map.put("sEmiRfc", bizPartnerCompany.getFiscalId());
        map.put("sRecRfc", bizPartnerEmployee.getFiscalId());
        map.put("ReceptorNombre", bizPartnerEmployee.getBizPartner());
        map.put("nPayrrollId", payrollReceipt.getPkPayrollId());
        map.put("nEmployeeId", payrollReceipt.getPkEmployeeId());
        map.put("NominaNumTipo", payroll.getNumber() + " " + (String) client.getSession().readField(SModConsts.HRSS_TP_PAY, new int[] { payroll.getFkPaymentTypeId() }, SDbRegistry.FIELD_NAME));
        map.put("NominaFolio", "");

        map.put("RegistroPatronal", ((SClientInterface) client).getSessionXXX().getParamsCompany().getRegistrySs());
        map.put("CURP", bizPartnerEmployee.getAlternativeId());
        map.put("NumEmpleado", bizPartnerEmployee.getDbmsDataEmployee().getNumber());
        map.put("NumSeguridadSocial", bizPartnerEmployee.getDbmsDataEmployee().getSocialSecurityNumber());
        map.put("FechaInicialPago", SLibUtils.DateFormatDate.format(payroll.getDateStart()));
        map.put("FechaFinalPago", SLibUtils.DateFormatDate.format(payroll.getDateEnd()));
        map.put("NumDiasNoLaborados", payrollReceipt.getDaysNotWorked_r());
        map.put("NumDiasLaborados", payrollReceipt.getDaysWorked());
        map.put("NumDiasPagados", payrollReceipt.getDaysPaid());
        map.put("Departamento", (String) client.getSession().readField(SModConsts.HRSU_DEP, new int[] { bizPartnerEmployee.getDbmsDataEmployee().getFkDepartmentId() }, SDbRegistry.FIELD_NAME));
        map.put("Puesto", (String) client.getSession().readField(SModConsts.HRSU_POS, new int[] { bizPartnerEmployee.getDbmsDataEmployee().getFkPositionId() }, SDbRegistry.FIELD_NAME));
        map.put("PeriodicidadPago", (String) client.getSession().readField(SModConsts.HRSS_TP_PAY, new int[] { bizPartnerEmployee.getDbmsDataEmployee().getFkPaymentTypeId() }, SDbRegistry.FIELD_NAME));
        map.put("Sueldo", payrollReceipt.getEffectiveSalary(payroll.isFortnightStandard()));
        map.put("SalarioBaseCotApor", payrollReceipt.getSalarySscBase());
        map.put("SalarioDiarioIntegrado", payrollReceipt.getSalarySscBase());

        map.put("FechaInicioRelLaboral", SLibUtils.DateFormatDate.format(bizPartnerEmployee.getDbmsDataEmployee().getDateLastHire()));
        map.put("TipoJornada", bizPartnerEmployee.getDbmsDataEmployee().getWorkingHoursDay() + " hr");

        map.put("TipoEmpleado", (String) client.getSession().readField(SModConsts.HRSU_TP_EMP, new int[] { bizPartnerEmployee.getDbmsDataEmployee().getFkEmployeeTypeId() }, SDbRegistry.FIELD_NAME));
        map.put("TipoSalario", (String) client.getSession().readField(SModConsts.HRSS_TP_SAL, new int[] { bizPartnerEmployee.getDbmsDataEmployee().getFkSalaryTypeId() }, SDbRegistry.FIELD_NAME));

        map.put("Ejercicio", payroll.getPeriodYear() + "-" + oFixedFormatAux.format(payroll.getPeriod()));

        aPercepciones = new ArrayList();
        aDeducciones = new ArrayList();

        dTotalPercepciones = 0;
        dTotalDeducciones = 0;

        // Earnings:

        for (SDbPayrollReceiptEarning payrollReceiptEarning : payrollReceipt.getChildPayrollReceiptEarnings()) {
            SDbEarning earning = new SDbEarning();
            loan = null;

            earning.read(client.getSession(),  new int[] { payrollReceiptEarning.getFkEarningId() });

            if (payrollReceiptEarning.getFkLoanEmployeeId_n() != 0) {
                loan = new SDbLoan();

                loan.read(client.getSession(), new int[] { payrollReceiptEarning.getFkLoanEmployeeId_n(), payrollReceiptEarning.getFkLoanLoanId_n() });
            }

            aPercepciones.add(earning.getCode());
            aPercepciones.add(earning.getCode());
            aPercepciones.add(earning.getName() + (loan == null ? "" : " (" + loan.composeLoanDescription() + ") "));
            aPercepciones.add(!earning.isBasedOnUnits() ? null : payrollReceiptEarning.getUnits());
            aPercepciones.add(!earning.isBasedOnUnits() ? null : (String) client.getSession().readField(SModConsts.HRSS_TP_EAR_COMP, new int[] { earning.getFkEarningComputationTypeId() }, SDbRegistry.FIELD_CODE));
            aPercepciones.add(payrollReceiptEarning.getAmount_r());
            aPercepciones.add(null);

            dTotalPercepciones += payrollReceiptEarning.getAmount_r();
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

        for (SDbPayrollReceiptDeduction payrollReceiptDeduction : payrollReceipt.getChildPayrollReceiptDeductions()) {
            SDbDeduction deduction = new SDbDeduction();
            loan = null;

            deduction.read(client.getSession(),  new int[] { payrollReceiptDeduction.getFkDeductionId() });

            if (payrollReceiptDeduction.getFkLoanEmployeeId_n() != 0) {
                loan = new SDbLoan();

                loan.read(client.getSession(), new int[] { payrollReceiptDeduction.getFkLoanEmployeeId_n(), payrollReceiptDeduction.getFkLoanLoanId_n() });
            }

            aDeducciones.add(deduction.getCode());
            aDeducciones.add(deduction.getCode());
            aDeducciones.add(deduction.getName() + (loan == null ? "" : " (" + loan.composeLoanDescription() + ") "));
            aDeducciones.add(payrollReceiptDeduction.getAmount_r());
            aDeducciones.add(null);

            dTotalDeducciones += payrollReceiptDeduction.getAmount_r();
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
        
        return map;
    }
    
    /**
     * Print payrroll receipt
     * @param client Interface Client
     * @param pnPrintMode print mode (e.g. SDataConstantsPrint.PRINT_MODE_)
     * @param payrollKey payrroll key
     * @throws java.lang.Exception
     */
    public static void printPayrollReceipt(final SGuiClient client, final int pnPrintMode, final int[] payrollKey) throws java.lang.Exception {
        HashMap<String, Object> map = new HashMap<>();
        map = createPayrollReceiptMap(client, payrollKey, pnPrintMode);
        
        switch (pnPrintMode) {
            case SDataConstantsPrint.PRINT_MODE_VIEWER:
                client.getSession().printReport(SModConsts.HRSR_PAY_RCP, 0, null, map);
                break;
            case SDataConstantsPrint.PRINT_MODE_PDF_FILE:
                break;
            case SDataConstantsPrint.PRINT_MODE_PRINT:
                break;
            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }
    }
    
    public static void sendPayrollReceipt(final SGuiClient client, final int pnPrintMode, final int[] payrollKey) throws java.lang.Exception {
        boolean isSent = false;
        HashMap<String, Object> map = new HashMap<>();
        File pdf = null;
        STreasuryBankLayoutRequest layoutRequest = new STreasuryBankLayoutRequest(client, null);
        
        map = createPayrollReceiptMap(client, payrollKey, pnPrintMode);
        pdf = createPayrollReceipt(client, map);
        
        SDataBizPartner bizPartner  = (SDataBizPartner) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.BPSU_BP, new int[] { (Integer)map.get("nEmployeeId") }, SLibConstants.EXEC_MODE_SILENT);
        String mail = bizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(0).getEmail01();
            
        if (pdf != null) {
            isSent = layoutRequest.sendMail(null, "", pdf, STreasuryBankLayoutRequest.SND_TP_PAY_RCP, mail);
            if (isSent) {
                client.showMsgBoxInformation("El recibo de nómina ha sido enviado correctamente.\n");
            }
            else {
                client.showMsgBoxInformation("El recibo de nómina no ha sido enviado.\n");
            }
        }
    }
    
    public static void sendPayrollReceipts(final SGuiClient client, final int pnPrintMode, final int[] payrollKey) throws java.lang.Exception {
        ArrayList<SDbPayrollReceipt> payrollReceipts = new ArrayList<>();
        SDbPayroll payroll = new SDbPayroll();
        payroll.read(client.getSession(), new int[] { payrollKey[0] });
        
        for (SDbPayrollReceipt payrollReceipt : payroll.getChildPayrollReceipts()) {
            if (!payrollReceipt.isDeleted()) {
                payrollReceipts.add(payrollReceipt);
            }
        }
        
        SDialogCfdProcessing dialog = new SDialogCfdProcessing((SClient) client, "Procesamiento de envío", SCfdConsts.PROC_REQ_SEND_CFD_PAYROLL);
        dialog.setFormParams((SClientInterface) client, null, null, 0, null, true, 0, SModSysConsts.TRNU_TP_DPS_ANN_NA);
        dialog.setPayrollReceipts(payrollReceipts);
        dialog.setVisible(true);
    }
    
    /**
     * Create a object File in PDF format payroll receipt
     * @param map Obj HashMap with all information about payroll receipt
     * @param client interface Client
     * @return Object file payroll receipt
     */
    public static File createPayrollReceipt(SGuiClient client, HashMap<String, Object> map) {
        JasperPrint jasperPrint = null;
        byte[] reportBytes = null;
        File file = null;
        File fileTemporal = null;
        FileOutputStream fos = null;
        JasperReport reporte = null;
        
        try {
            fileTemporal = new File("reps/hrs_pay_rcp.jasper");
            reporte = (JasperReport) JRLoader.loadObject(fileTemporal);
            
            jasperPrint = JasperFillManager.fillReport(reporte, map,client.getSession().getDatabase().getConnection());
            reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            fileTemporal = File.createTempFile("document", ".pdf");
            fos = new FileOutputStream(fileTemporal);
            fos.write(reportBytes);
            fos.close();

            file = new File(fileTemporal.getParentFile() + "\\"+ "Recibo de nomina.pdf");
            fos = new FileOutputStream(file);
            fos.write(reportBytes);
            fos.close();
        }
        catch (Exception e) {
            SLibUtils.showException(STreasuryBankLayoutFile.class, e);
        }
        
        return file;  
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
        
        client.getSession().printReport(SModConsts.HRSR_PRE_PAY, 0, null, map);
    }
    
    public static String getEmployeeNextNumber(Connection connection) throws Exception {
        String nextNumber = "";

        String sql = "SELECT COALESCE(MAX(CAST(num AS UNSIGNED INTEGER)), 0) + 1 "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + ";";

        try (ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                nextNumber = resultSet.getInt(1) + "";
            }
        }

        return nextNumber;
    }
    
    public static int getEmployeeSeniority(final Date dateBenefits, final Date dateCutOff) {
        DateTime start = new DateTime(dateBenefits);
        DateTime end = new DateTime(dateCutOff);
        Period period = new Period(start, end);
        
        return period.getYears();
    }
    
    public static int getPaymentVacationsByEmployee(final SGuiSession session, final int employeeId, final int benefitAnn, final int benefitYear) throws Exception {
        int days = 0;
        
        String sql = "SELECT COALESCE(SUM(pre.unt_all), 0) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                + "WHERE pr.id_emp = " + employeeId + " AND NOT pr.b_del AND NOT pre.b_del AND pre.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND "
                + "pre.ben_ann = " + benefitAnn + " AND pre.ben_year = " + benefitYear + ";";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                days = resultSet.getInt(1);
            }
        }
        
        return days;
    }
    
    public static int getDaysVacationsAll(final SGuiSession session, final int benefitAnn, final Date dateCutoff) throws Exception {
        int daysTableVacation = 0;
        ArrayList<SDbBenefitTable> aTableVacation = new ArrayList<>();
        ArrayList<SHrsBenefitTableAnniversary> aTableVacationByAnniversary = new ArrayList<>();
        
        aTableVacation.add((SDbBenefitTable) session.readRegistry(SModConsts.HRS_BEN, new int[] { getRecentBenefitTable(session, SModSysConsts.HRSS_TP_BEN_VAC, 0, dateCutoff) }));
        
        aTableVacationByAnniversary = createBenefitTablesAnniversarys(aTableVacation);
        
        for (SHrsBenefitTableAnniversary anniversary : aTableVacationByAnniversary) {
            if (anniversary.getBenefitAnn() <= benefitAnn) {
                daysTableVacation += (int) anniversary.getValue();
            }
        }
        
        return daysTableVacation;
    }

    public static ArrayList<SDbEmployeeHireLog> readEmployeeHireLogs(final SGuiSession session, final Statement resultSetStatement, final int employeeId, final Date dateStart, final Date dateEnd) throws Exception {
        ArrayList<SDbEmployeeHireLog> employeeHireLogs = new ArrayList<>();

        String sql = "SELECT id_emp, id_log " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_HIRE) + " " +
                "WHERE NOT b_del AND id_emp = " + employeeId + " AND dt_hire <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' AND " +
                "(dt_dis_n IS NULL OR dt_dis_n >= '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "');";
        
        try (ResultSet resultSet = resultSetStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbEmployeeHireLog employeeHireLog = new SDbEmployeeHireLog();
                employeeHireLog.read(session, new int[] { resultSet.getInt("id_emp"), resultSet.getInt("id_log") });
                employeeHireLogs.add(employeeHireLog);
            }
        }

        return employeeHireLogs;
    }
    
    public static SDbEmployeeHireLog getEmployeeLastHire(final SGuiSession session, final int employeeId, final int logId, final String schema) throws Exception {
        SDbEmployeeHireLog employeeHireLog = null;
        
        String sql = "SELECT id_log, dt_hire " +
            "FROM " + (schema.isEmpty() ? "" : (schema + ".")) + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_HIRE) + " " +
            "WHERE NOT b_del AND id_emp = " + employeeId + " AND dt_dis_n IS NULL AND id_log <> " + logId + " " +
            "ORDER BY dt_hire DESC, id_log " +
            "LIMIT 1;";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                employeeHireLog = new SDbEmployeeHireLog();
                employeeHireLog.read(session, new int[] { employeeId, resultSet.getInt("id_log") });
            }
        }
        
        return employeeHireLog;
    }
    
    public static SDbEmployeeHireLog getEmployeeLastDismiss(final SGuiSession session, final int employeeId, final int logId, final String schema) throws Exception {
        SDbEmployeeHireLog employeeHireLog = null;
        
        String sql = "SELECT id_log, dt_dis_n " +
            "FROM " + (schema.isEmpty() ? "" : (schema + ".")) + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_HIRE) + " " +
            "WHERE NOT b_del AND id_emp = " + employeeId + " AND dt_dis_n IS NOT NULL AND id_log <> " + logId + " " +
            "ORDER BY dt_dis_n DESC, id_log " +
            "LIMIT 1;";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                employeeHireLog = new SDbEmployeeHireLog();
                employeeHireLog.read(session, new int[] { employeeId, resultSet.getInt("id_log") });
            }
        }
        
        return employeeHireLog;
    }
    
    public static int getEmployeeHireDays(final ArrayList<SDbEmployeeHireLog> aEmployeeHireLogs, final Date dateStart, final Date dateEnd) throws Exception {
        int daysHired = 0;
        
        for (SDbEmployeeHireLog hireLog : aEmployeeHireLogs) {
            if (hireLog.getDateHire().compareTo(dateStart) <= 0) {
                daysHired += SLibTimeUtils.countPeriodDays(
                        dateStart, 
                        hireLog.getDateDismissed_n() == null ? dateEnd : hireLog.getDateDismissed_n().compareTo(dateEnd) >= 0 ? dateEnd : hireLog.getDateDismissed_n());
            }
            else if (hireLog.getDateHire().compareTo(dateStart) >= 0) {
                daysHired += SLibTimeUtils.countPeriodDays(
                        hireLog.getDateHire(),
                        hireLog.getDateDismissed_n() == null ? dateEnd : hireLog.getDateDismissed_n().compareTo(dateEnd) >= 0 ? dateEnd : hireLog.getDateDismissed_n());
            }
        }
        
        return daysHired;
    }
    
    public static int getEmployeeIncapacityNotPayed(final ArrayList<SDbAbsenceConsumption> aAbsenceConsumptions, final Date dateStart, final Date dateEnd) {
        int daysIncapacityNotPaidAnnual = 0;
        
        for (SDbAbsenceConsumption absenceConsumption : aAbsenceConsumptions) {
            if (!absenceConsumption.getParentAbsence().isXtaAbsenceTypePayable() && absenceConsumption.getParentAbsence().isDisability() && 
                    SLibTimeUtils.isBelongingToPeriod(absenceConsumption.getDateStart(), dateStart, dateEnd)) {
                daysIncapacityNotPaidAnnual += absenceConsumption.getEffectiveDays();
            }
        }
        
        return daysIncapacityNotPaidAnnual;
    }
    
    public static ArrayList<SDbAbsence> getEmployeeAbsences(final SGuiSession session, final int employeeId) throws Exception {
        ArrayList<SDbAbsence> aAbsences = new ArrayList<>();

        String sql = "SELECT id_emp, id_abs " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ABS) + " " +
            "WHERE id_emp = " + employeeId + " AND NOT b_del AND NOT b_clo " +
            "ORDER BY dt_sta, id_emp, id_abs;";

        try (ResultSet resultSet = session.getDatabase().getConnection().createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                SDbAbsence absence = new SDbAbsence();
                absence.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
                aAbsences.add(absence);
            }
        }
        
        return aAbsences;
    }
    
    public static ArrayList<SDbAbsenceConsumption> getEmployeeAbsencesConsumptions(final SGuiSession session, final ArrayList<SDbAbsence> absences, final int payrollId) throws Exception {
        ArrayList<SDbAbsenceConsumption> absencesConsumptions = new ArrayList<>();

        String sql = "SELECT ac.id_cns "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ABS_CNS) + " AS ac "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON ac.fk_rcp_pay = pr.id_pay AND ac.fk_rcp_emp = pr.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p ON pr.id_pay = p.id_pay "
                + "WHERE ac.id_emp = ? AND ac.id_abs = ? AND ac.fk_rcp_pay <> " + payrollId + " AND NOT ac.b_del AND NOT pr.b_del AND NOT p.b_del "
                + "ORDER BY ac.id_emp, ac.id_abs, ac.id_cns;";

        try (PreparedStatement preparedStatement = session.getDatabase().getConnection().prepareStatement(sql)) {
            for (SDbAbsence absence : absences) {
                preparedStatement.setInt(1, absence.getPkEmployeeId());
                preparedStatement.setInt(2, absence.getPkAbsenceId());
                
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        SDbAbsenceConsumption absenceConsumption = new SDbAbsenceConsumption();
                        absenceConsumption.read(session, new int[] { absence.getPkEmployeeId(), absence.getPkAbsenceId(), resultSet.getInt(1) });
                        absencesConsumptions.add(absenceConsumption);
                    }
                }
            }
        }
        
        return absencesConsumptions;
    }
    
    private static boolean isFirstHire(final SGuiSession session, final int employeeId) throws Exception {
        boolean isFirtsHire = false;
        
        String sql = "SELECT COUNT(*) AS _count, MAX(id_log) AS _max_id_log " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_HIRE) + " " +
            "WHERE NOT b_del AND id_emp = " + employeeId + ";";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next() && resultSet.getInt("_count") == 1) {
                SDbEmployeeHireLog employeeHireLog = (SDbEmployeeHireLog) session.readRegistry(SModConsts.HRS_EMP_LOG_HIRE, new int[] { employeeId, resultSet.getInt("_max_id_log") }, SDbConsts.MODE_STEALTH);
                isFirtsHire = employeeHireLog.getDateDismissed_n() == null;
            }
        }
        
        return isFirtsHire;
    }
    
    public static boolean deleteHireLog(final SGuiSession session, final int employeeId) throws Exception {
        if (isFirstHire(session, employeeId)) {
            throw new Exception("El empleado no tiene registros adicionales a su única alta en la bitácora altas y bajas.");
        }
        else {
            SDbEmployeeHireLog employeeHireLog;
            SDbEmployee employee = (SDbEmployee) session.readRegistry(SModConsts.HRSU_EMP, new int[] { employeeId });
            
            if (employee.isActive()) {
                employeeHireLog = getEmployeeLastHire(session, employeeId, 0, "");
            }
            else {
                employeeHireLog = getEmployeeLastDismiss(session, employeeId, 0, "");
            }

            SHrsEmployeeHireLog hrsEmployeeHireLog = new SHrsEmployeeHireLog(null, session);
            hrsEmployeeHireLog.setPkEmployeeId(employeeHireLog.getPkEmployeeId());
            hrsEmployeeHireLog.setDateLastHire(employeeHireLog.getDateHire());
            hrsEmployeeHireLog.setIsHire(!employee.isActive());
            hrsEmployeeHireLog.setDeleted(employeeHireLog.isDeleted());
            hrsEmployeeHireLog.setDateLastHire(employeeHireLog.getDateHire());
            hrsEmployeeHireLog.setNotesHire(employeeHireLog.getNotesHire());
            hrsEmployeeHireLog.setDateLastDismiss_n(employeeHireLog.getDateDismissed_n());
            hrsEmployeeHireLog.setNotesDismissed(employeeHireLog.getNotesDismissed());
            hrsEmployeeHireLog.setFkDismissedType(employeeHireLog.getFkEmployeeDismissTypeId());
            hrsEmployeeHireLog.setFkUserInsertId(employeeHireLog.getFkUserInsertId());
            hrsEmployeeHireLog.setFkUserUpdateId(employeeHireLog.getFkUserUpdateId());

            hrsEmployeeHireLog.setIsFirtsHire(false);
            hrsEmployeeHireLog.setIsCorrection(true);

            if (employee.isActive()) {
                hrsEmployeeHireLog.setDeleted(true);
            }
            else {
                hrsEmployeeHireLog.setDateLastDismiss_n(null);
                hrsEmployeeHireLog.setNotesDismissed("");
                hrsEmployeeHireLog.setFkDismissedType(SModSysConsts.HRSU_TP_EMP_DIS_NON); 
            }
            
            hrsEmployeeHireLog.save();
        }
        
        return true;
    }
    
    public static boolean editHireLog(final SGuiSession session, final SDbEmployeeHireLog employeeHireLog) throws Exception {
        SHrsEmployeeHireLog hrsEmployeeHireLog;
        
        hrsEmployeeHireLog = new SHrsEmployeeHireLog(null,session);

        hrsEmployeeHireLog.setPkEmployeeId(employeeHireLog.getPkEmployeeId());
        hrsEmployeeHireLog.setDateLastHire(employeeHireLog.getDateHire());
        hrsEmployeeHireLog.setIsHire(employeeHireLog.isHired());
        hrsEmployeeHireLog.setDeleted(employeeHireLog.isDeleted());
        hrsEmployeeHireLog.setDateLastHire(employeeHireLog.getDateHire());
        hrsEmployeeHireLog.setNotesHire(employeeHireLog.getNotesHire());
        hrsEmployeeHireLog.setDateLastDismiss_n(employeeHireLog.getDateDismissed_n());
        hrsEmployeeHireLog.setNotesDismissed(employeeHireLog.getNotesDismissed());
        hrsEmployeeHireLog.setFkDismissedType(employeeHireLog.getFkEmployeeDismissTypeId());
        hrsEmployeeHireLog.setFkUserInsertId(employeeHireLog.getFkUserInsertId());
        hrsEmployeeHireLog.setFkUserUpdateId(employeeHireLog.getFkUserUpdateId());

        hrsEmployeeHireLog.setIsFirtsHire(false);
        hrsEmployeeHireLog.setIsCorrection(false);
        hrsEmployeeHireLog.setIsEdit(true);
        
        hrsEmployeeHireLog.save();
        
        return true;
    }
    
    public static ArrayList<SHrsBenefit> readHrsBenefits(final SGuiSession session, final SDbEmployee employee, final int benefitType, final int anniversaryLimit, final int benefitYear, final int payrrollId, final ArrayList<SHrsBenefitTableAnniversary> benefitTableAnniversarys, final ArrayList<SHrsBenefitTableAnniversary> benefitTableAnniversarysAux, final double paymentDaily) throws Exception {
        ArrayList<SHrsBenefit> hrsBenefits = new ArrayList<>();
        boolean foundAnniversary = false;
        SHrsBenefit hrsBenefit = null;
        SHrsBenefitTableAnniversary benefitTableAnniversary = null;
        SHrsBenefitTableAnniversary benefitTableAnniversaryAux = null;
        
        String sql = "SELECT ben_ann, ben_year, SUM(unt_all) AS _val_payed, SUM(amt_r) AS _amt_payed " +
                "FROM hrs_pay_rcp_ear " +
                "WHERE id_emp = " + employee.getPkEmployeeId() + " AND id_pay <> " + payrrollId + " AND fk_tp_ben = " + benefitType + " AND ben_ann <= " + anniversaryLimit + " AND NOT b_del " +
                "GROUP BY ben_ann, ben_year " +
                "ORDER BY ben_ann, ben_year;";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        
        while (resultSet.next()) {
            hrsBenefit = new SHrsBenefit(benefitType, resultSet.getInt("ben_ann"), resultSet.getInt("ben_year"));
            
            hrsBenefit.setValuePayed(resultSet.getDouble("_val_payed"));
            hrsBenefit.setAmountPayed(resultSet.getDouble("_amt_payed"));
            
            hrsBenefits.add(hrsBenefit);
        }
        
        for (SHrsBenefit benefit : hrsBenefits) {
            if (SLibUtils.compareKeys(benefit.getBenefitKey(), new int[] { benefitType, anniversaryLimit, benefitYear })) {
                foundAnniversary = true;
            }
        }
        
        if (!foundAnniversary) {
            hrsBenefit = new SHrsBenefit(benefitType, anniversaryLimit, benefitYear);
            hrsBenefits.add(hrsBenefit);
        }
        
        // To complete benefits registries accumulated by benefit type:
        
        for (SHrsBenefit benefit : hrsBenefits) {
            for (SHrsBenefitTableAnniversary anniversary : benefitTableAnniversarys) { // lookup requested benefit
                if (anniversary.getBenefitAnn() <= benefit.getBenefitAnn()) {
                    benefitTableAnniversary = anniversary;
                }
            }

            if (benefitType == SModSysConsts.HRSS_TP_BEN_VAC_BON) {
                for (SHrsBenefitTableAnniversary anniversaryAux : benefitTableAnniversarysAux) { // lookup vacations benefit
                    if (anniversaryAux.getBenefitAnn() <= benefit.getBenefitAnn()) {
                        benefitTableAnniversaryAux = anniversaryAux;
                    }
                }
            }

            if (benefitType == SModSysConsts.HRSS_TP_BEN_VAC_BON) {
                benefit.setValue(benefitTableAnniversary == null || benefitTableAnniversaryAux == null ? 0d : benefitTableAnniversaryAux.getValue());
                benefit.setAmount(benefitTableAnniversary == null || benefitTableAnniversaryAux == null ? 0d : SLibUtils.roundAmount(benefitTableAnniversaryAux.getValue() * paymentDaily * benefitTableAnniversary.getValue()));
            }
            else {
                benefit.setValue(benefitTableAnniversary == null ? 0d : benefitTableAnniversary.getValue());
                benefit.setAmount(benefitTableAnniversary == null ? 0d : SLibUtils.roundAmount(benefitTableAnniversary.getValue() * paymentDaily));
            }
        }

        return hrsBenefits;
    }
    
    public static int getScheduledDays(final SGuiSession session, final SDbEmployee employee, final int benefitAnn, final int benefitYear, final int absenceId) throws Exception {
        int scheduledDays = 0;
        String sql = "";
        ResultSet resultSet = null;

         sql = "SELECT SUM(a.eff_day) " +
                 "FROM hrs_abs AS a " +
                 "WHERE NOT a.b_del AND a.id_emp = " + employee.getPkEmployeeId() + " AND " +
                 "a.ben_ann = " + benefitAnn + " AND a.ben_year = " + benefitYear + " AND a.id_abs <> " + absenceId + " ";
         
         resultSet = session.getStatement().executeQuery(sql);
         if (resultSet.next()) {
             scheduledDays = resultSet.getInt(1);
         }
         
        return scheduledDays;
    }
    
    public static void createPayrollReceiptIssues(final SGuiSession session, final SDbPayroll payroll) throws Exception {
        for (SDbPayrollReceipt payrollReceipt : payroll.getChildPayrollReceipts()) {
            payrollReceipt.setAuxDateIssue(payroll.getDateEnd());
            payrollReceipt.createIssues(session);
        }
    }
    
    public static void updateToNewStatusPayrollReceiptIssues(final SGuiSession session, final SDbPayroll payroll) throws Exception {
        for (SDbPayrollReceipt payrollReceipt : payroll.getChildPayrollReceipts()) {
            payrollReceipt.updateToNewStatusIssues(session);
        }
    }
    
    public static double getSbcIntegrationFactor(final SGuiSession session, final Date dateBenefits, final Date dateCutoff) throws Exception {
        int seniority = 0;
        int daysTableAnnualBonus = 0;
        int daysTableVacation = 0;
        double percentageTableVacationBonus = 0;
        double salaryUnit = 1;
        double integrationFactorSbc = 0;
        SHrsBenefitTableAnniversary benefitTableAnniversary = null;
        ArrayList<SDbBenefitTable> benefitTableAnnualBonus = new ArrayList<>();
        ArrayList<SDbBenefitTable> benefitTableVacation = new ArrayList<>();
        ArrayList<SDbBenefitTable> benefitTableVacationBonus = new ArrayList<>();
        
        benefitTableAnnualBonus.add((SDbBenefitTable) session.readRegistry(SModConsts.HRS_BEN, new int[] { getRecentBenefitTable(session, SModSysConsts.HRSS_TP_BEN_ANN_BON, 0, dateCutoff) }));
        benefitTableVacation.add((SDbBenefitTable) session.readRegistry(SModConsts.HRS_BEN, new int[] { getRecentBenefitTable(session, SModSysConsts.HRSS_TP_BEN_VAC, 0, dateCutoff) }));
        benefitTableVacationBonus.add((SDbBenefitTable) session.readRegistry(SModConsts.HRS_BEN, new int[] { getRecentBenefitTable(session, SModSysConsts.HRSS_TP_BEN_VAC_BON, 0, dateCutoff) }));
        
        if (dateBenefits != null) {
            seniority = getEmployeeSeniority(dateBenefits, dateCutoff);
        }
        else {
            seniority = 1;
        }
        
        ArrayList<SHrsBenefitTableAnniversary> benefitTableAnnualBonusAnniversarys = createBenefitTablesAnniversarys(benefitTableAnnualBonus);
        ArrayList<SHrsBenefitTableAnniversary> benefitTableVacationAnniversarys = createBenefitTablesAnniversarys(benefitTableVacation);
        ArrayList<SHrsBenefitTableAnniversary> benefitTableVacationBonusAnniversarys = createBenefitTablesAnniversarys(benefitTableVacationBonus);
        
        for (SHrsBenefitTableAnniversary anniversary : benefitTableAnnualBonusAnniversarys) {
            if (anniversary.getBenefitAnn() <= seniority) {
                benefitTableAnniversary = anniversary;
            }
        }
        daysTableAnnualBonus = benefitTableAnniversary == null ? 0 : (int) benefitTableAnniversary.getValue();
        
        for (SHrsBenefitTableAnniversary anniversary : benefitTableVacationAnniversarys) {
            if (anniversary.getBenefitAnn() <= seniority) {
                benefitTableAnniversary = anniversary;
            }
        }
        daysTableVacation = benefitTableAnniversary == null ? 0 : (int) benefitTableAnniversary.getValue();
        
        for (SHrsBenefitTableAnniversary anniversary : benefitTableVacationBonusAnniversarys) {
            if (anniversary.getBenefitAnn() <= seniority) {
                benefitTableAnniversary = anniversary;
            }
        }
        percentageTableVacationBonus = benefitTableAnniversary == null ? 0 : (double) benefitTableAnniversary.getValue();
        
        integrationFactorSbc = salaryUnit + ((double) daysTableAnnualBonus / SHrsConsts.YEAR_DAYS) + (double) (daysTableVacation * percentageTableVacationBonus / SHrsConsts.YEAR_DAYS);
        
        return integrationFactorSbc;
    }
    
    /**
     * Gets loan balance.
     * @param loan Loan.
     * @param hrsReceipt Current payroll receipt.
     * @param hrsReceiptEarningBeingEdited Current earning being edited. Can be <code>null</code>.
     * @param hrsReceiptDeductionBeingEdited Current deduction being edited. Can be <code>null</code>.
     * @return Loan balance.
     * @throws Exception When loan is not a plain loan.
     */
    public static double getLoanBalance(final SDbLoan loan, final SHrsReceipt hrsReceipt, final SHrsReceiptEarning hrsReceiptEarningBeingEdited, final SHrsReceiptDeduction hrsReceiptDeductionBeingEdited) throws Exception {
        if (!loan.isPlainLoan()) {
            throw new Exception(SDbLoan.ONLY_PLAIN_LOANS_HAVE_BALANCE);
        }
        
        double loanBalance = 0;
        
        // get loan balance excluding current payroll receipt:
        
        SHrsLoan hrsLoan = hrsReceipt.getHrsEmployee().getHrsLoan(loan.getPkLoanId());
        if (hrsLoan == null) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + "\n"
                    + SHrsLoan.class.getName() + ": " + loan.composeLoanDescription() + ".");
        }
        else {
            loanBalance = hrsLoan.getLoanBalance();
        }
        
        // update loan balance including current payroll receipt:
        
        for (SHrsReceiptEarning hrsReceiptEarning : hrsReceipt.getHrsReceiptEarnings()) {
            if (hrsReceiptEarningBeingEdited == null || !SLibUtils.compareKeys(hrsReceiptEarningBeingEdited.getPayrollReceiptEarning().getPrimaryKey(), hrsReceiptEarning.getPayrollReceiptEarning().getPrimaryKey())) {
                if (SLibUtils.compareKeys(loan.getPrimaryKey(), hrsReceiptEarning.getPayrollReceiptEarning().getLoanKey())) {
                    loanBalance = SLibUtils.roundAmount(loanBalance + hrsReceiptEarning.getPayrollReceiptEarning().getAmount_r());
                }
            }
        }
        
        for (SHrsReceiptDeduction hrsReceiptDeduction : hrsReceipt.getHrsReceiptDeductions()) {
            if (hrsReceiptDeductionBeingEdited == null || !SLibUtils.compareKeys(hrsReceiptDeductionBeingEdited.getPayrollReceiptDeduction().getPrimaryKey(), hrsReceiptDeduction.getPayrollReceiptDeduction().getPrimaryKey())) {
                if (SLibUtils.compareKeys(loan.getPrimaryKey(), hrsReceiptDeduction.getPayrollReceiptDeduction().getLoanKey())) {
                    loanBalance = SLibUtils.roundAmount(loanBalance - hrsReceiptDeduction.getPayrollReceiptDeduction().getAmount_r());
                }
            }
        }
        
        return loanBalance;
    }
    
    /**
     * Computes loan amount.
     * For plain loans, loan amount is already limited to current loan balance.
     * @param loan
     * @param hrsReceipt Current payroll receipt.
     * @param hrsReceiptEarningBeingEdited Current earning being edited. Can be <code>null</code>.
     * @param hrsReceiptDeductionBeingEdited Current deduction being edited. Can be <code>null</code>.
     * @return
     * @throws Exception 
     */
    public static double computeLoanAmount(final SDbLoan loan, final SHrsReceipt hrsReceipt, final SHrsReceiptEarning hrsReceiptEarningBeingEdited, final SHrsReceiptDeduction hrsReceiptDeductionBeingEdited) throws Exception {
        double loanAmount = 0;
        double monthlyAdjustment = hrsReceipt.getHrsPayroll().getLoanTypeMonthlyAdjustment(hrsReceipt.getHrsPayroll().getPayroll().getDateEnd(), loan.getFkLoanTypeId());
        
        if (loan.isPlainLoan()) {
            // it a simple plain loan:

            loanAmount = loan.getPaymentAmount();

            if (monthlyAdjustment != 0) {
                switch (hrsReceipt.getHrsPayroll().getPayroll().getFkPaymentTypeId()) {
                    case SModSysConsts.HRSS_TP_PAY_WEE:
                        loanAmount = SLibUtils.roundAmount(loanAmount + (monthlyAdjustment * SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_WEEKS));
                        break;
                    case SModSysConsts.HRSS_TP_PAY_FOR:
                        loanAmount = SLibUtils.roundAmount(loanAmount + (monthlyAdjustment / SHrsConsts.MONTH_FORTNIGHTS));
                        break;
                    default:
                }
            }

            // limit payment to loan balance, if necessary:
            
            double loanBalance = getLoanBalance(loan, hrsReceipt, hrsReceiptEarningBeingEdited, hrsReceiptDeductionBeingEdited);
            if (loanAmount > loanBalance) {
                loanAmount = loanBalance;
            }
        }
        else {
            // it is a home or consumer credit:
            
            // compute loan amount considering also the days of previous or next months (only for weekly payrolls):
            
            SHrsDaysByPeriod hrsDaysPrev = hrsReceipt.getHrsEmployee().getHrsDaysPrev();
            SHrsDaysByPeriod hrsDaysCurr = hrsReceipt.getHrsEmployee().getHrsDaysCurr();
            SHrsDaysByPeriod hrsDaysNext = hrsReceipt.getHrsEmployee().getHrsDaysNext();
            double propPrev = hrsDaysPrev == null ? 0d : ((double) hrsDaysPrev.getPeriodPayrollDays() - hrsDaysPrev.getDaysNotWorkedNotPaid()) / hrsDaysPrev.getPeriodDays();
            double propCurr = hrsDaysCurr == null ? 0d : ((double) hrsDaysCurr.getPeriodPayrollDays() - hrsDaysCurr.getDaysNotWorkedNotPaid()) / hrsDaysCurr.getPeriodDays();
            double propNext = hrsDaysNext == null ? 0d : ((double) hrsDaysNext.getPeriodPayrollDays() - hrsDaysNext.getDaysNotWorkedNotPaid()) / hrsDaysNext.getPeriodDays();
            
            if (loan.getFkLoanPaymentTypeId() == SModSysConsts.HRSS_TP_LOAN_PAY_PCT) {
                double salary = 0;
                double loanAdj = 0;
                
                switch (loan.getPaymentPercentageReference()) {
                    case SHrsConsts.PAY_PER_REF_SD:
                        salary = hrsReceipt.getPayrollReceipt().getPaymentDaily();
                        break;
                    case SHrsConsts.PAY_PER_REF_SBC:
                        salary = hrsReceipt.getPayrollReceipt().getSalarySscBase();
                        break;
                    case SHrsConsts.PAY_PER_REF_OTRO:
                        salary = loan.getPaymentPercentageAmount();
                        break;
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                
                loanAdj +=  propPrev * monthlyAdjustment;
                loanAdj +=  propCurr * monthlyAdjustment;
                loanAdj +=  propNext * monthlyAdjustment;

                loanAmount = (hrsReceipt.getPayrollReceipt().getDaysHiredPayroll() - hrsReceipt.getPayrollReceipt().getDaysNotWorkedNotPaid()) * salary * loan.getPaymentPercentage() + loanAdj;
            }
            else {
                double monthlyPay;
                
                switch (loan.getFkLoanPaymentTypeId()) {
                    case SModSysConsts.HRSS_TP_LOAN_PAY_AMT: // Amount
                        monthlyPay = loan.getPaymentAmount();
                        break;
                    case SModSysConsts.HRSS_TP_LOAN_PAY_FACT_SAL: // Factor Salary
                        monthlyPay = loan.getPaymentFixedFees() * hrsReceipt.getHrsPayroll().getPayroll().getMwzReferenceWage();
                        break;
                    case SModSysConsts.HRSS_TP_LOAN_PAY_FACT_UMA: // Factor UMA
                        monthlyPay = loan.getPaymentUmas() * hrsReceipt.getHrsPayroll().getUma(hrsReceipt.getHrsPayroll().getPayroll().getDateEnd());
                        break;
                    case SModSysConsts.HRSS_TP_LOAN_PAY_FACT_UMI: // Factor UMI
                        monthlyPay = loan.getPaymentUmis() * hrsReceipt.getHrsPayroll().getUmi(hrsReceipt.getHrsPayroll().getPayroll().getDateEnd());
                        break;
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                
                monthlyPay += monthlyAdjustment;
                
                loanAmount += propPrev * monthlyPay;
                loanAmount += propCurr * monthlyPay;
                loanAmount += propNext * monthlyPay;
            }
        }
        
        return SLibUtils.roundAmount(loanAmount);
    }
    
    /**
     * Computes tax.
     * @param taxTable Table of tax for computation.
     * @param taxableEarnings Taxable earnings.
     * @param tableFactor Adjustment factor to apply to tax table.
     * @return Computed tax.
     * @throws Exception 
     */
    public static double computeTax(final SDbTaxTable taxTable, final double taxableEarnings, final double tableFactor) throws Exception {
        double taxAssessed = 0;
        
        for (int row = 0; row < taxTable.getChildRows().size(); row++) {
            SDbTaxTableRow taxTableRow = taxTable.getChildRows().get(row);
            if (taxableEarnings >= SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactor) &&
                    (row + 1 == taxTable.getChildRows().size() || taxableEarnings < SLibUtils.roundAmount(taxTable.getChildRows().get(row + 1).getLowerLimit() * tableFactor))) {
                taxAssessed = SLibUtils.roundAmount((taxableEarnings - SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactor)) * taxTableRow.getTaxRate() + taxTableRow.getFixedFee() * tableFactor);
                break;
            }
        }
        
        return taxAssessed;
    }
    
    /**
     * Computes tax subsidy.
     * @param taxSubsidyTable Table of tax subsidy for computation.
     * @param taxableEarnings Taxable earnings.
     * @param tableFactor Adjustment factor to apply to tax subsidy table.
     * @return Computed tax subsidy.
     * @throws Exception 
     */
    public static double computeTaxSubsidy(final SDbTaxSubsidyTable taxSubsidyTable, final double taxableEarnings, final double tableFactor) throws Exception {
        double taxSubsidyAssessed = 0;
        
        for (int row = 0; row < taxSubsidyTable.getChildRows().size(); row++) {
            SDbTaxSubsidyTableRow taxSubsidyTableRow = taxSubsidyTable.getChildRows().get(row);
            if (taxableEarnings >= taxSubsidyTableRow.getLowerLimit() * tableFactor &&
                    (row + 1 == taxSubsidyTable.getChildRows().size() || taxableEarnings < taxSubsidyTable.getChildRows().get(row + 1).getLowerLimit() * tableFactor)) {
                taxSubsidyAssessed = SLibUtils.roundAmount(taxSubsidyTableRow.getTaxSubsidy() * tableFactor);
                break;
            }
        }
        
        return taxSubsidyAssessed;
    }
    
    /**
     * Computes tax based in Articule 174 RLISR.
     * @param taxTable Table of tax for computation.
     * @param taxableEarnings Taxable earnings.
     * @param monthlyIncome Ordinary monthly income.
     * @param tableFactor Adjustment factor to apply to tax table.
     * @return Computed tax.
     * @throws Exception 
     */
    public static double computeTaxAlt(final SDbTaxTable taxTable, final double taxableEarnings, final double monthlyIncome, final double tableFactor) throws Exception {
        double amountFractionI = taxableEarnings / SHrsConsts.YEAR_DAYS * SHrsConsts.MONTH_DAYS_FIXED;
        
        double amountFractionII = computeTax(taxTable, (monthlyIncome + amountFractionI), tableFactor);
        
        double amountFractionIIIAux = computeTax(taxTable, monthlyIncome, tableFactor);
        double amountFractionIII = amountFractionIIIAux > 0 ? (amountFractionII - amountFractionIIIAux) : 0;
        
        double amountFractionV = amountFractionI == 0 ? 0 : (amountFractionIII / amountFractionI);
        
        double amountFractionIV = amountFractionIIIAux > 0 ? (taxableEarnings * amountFractionV) : 0;
        
        return amountFractionIV;
    }
    
    /**
     * Computes tax subsidy based in Articule 174 RLISR.
     * @param taxSubsidyTable Table of tax subsidy for computation.
     * @param taxableEarnings Taxable earnings.
     * @param monthlyIncome Ordinary monthly income.
     * @param tableFactor Adjustment factor to apply to tax subsidy table.
     * @return Computed tax subsidy.
     * @throws Exception 
     */
    public static double computeTaxSubsidyAlt(final SDbTaxSubsidyTable taxSubsidyTable, final double taxableEarnings, final double monthlyIncome, final double tableFactor) throws Exception {
        double amountFractionI = taxableEarnings / SHrsConsts.YEAR_DAYS * SHrsConsts.MONTH_DAYS_FIXED;
        
        double amountFractionII = computeTaxSubsidy(taxSubsidyTable, (monthlyIncome + amountFractionI), tableFactor);
        
        double amountFractionIIIAux = computeTaxSubsidy(taxSubsidyTable, monthlyIncome, tableFactor);
        double amountFractionIII = amountFractionIIIAux > 0 ? (amountFractionII - amountFractionIIIAux) : 0;
        
        double amountFractionV = amountFractionI == 0 ? 0 : (amountFractionIII / amountFractionI);
        
        double amountFractionIV = amountFractionIIIAux > 0 ? (taxableEarnings * amountFractionV) : 0;
        
        return amountFractionIV;
    }
    
    /**
     * Computes Social Security Contribution.
     * @param sscTable Table of Social Security Contribution for computation.
     * @param salarySsc Employee's base salary for Social Security Contribution.
     * @param mwzReferenceWage Minimum wage of reference zone.
     * @param hrsDaysPrev Number of employee's days hired in previous period the payroll.
     * @param hrsDaysCurr Number of employee's days hired in current period the payroll.
     * @param hrsDaysNext Number of employee's days hired in next period the payroll.
     * @return double amount calculated of security social contribution.
     * @throws Exception 
     */
    public static double computeSsContribution(final SDbSsContributionTable sscTable, final double salarySsc, final double mwzReferenceWage, 
            final SHrsDaysByPeriod hrsDaysPrev, final SHrsDaysByPeriod hrsDaysCurr, final SHrsDaysByPeriod hrsDaysNext) throws Exception {
        double sscAssessed = 0;
        
        for (int row = 0; row < sscTable.getChildRows().size(); row++) {
            SDbSsContributionTableRow sscTableRow = sscTable.getChildRows().get(row);
            double sscEarning = 0;
            
            switch(sscTableRow.getPkRowId()) {
                case SHrsConsts.SS_INC_MON:
                case SHrsConsts.SS_INC_PEN:
                    sscEarning = SLibUtils.roundAmount((hrsDaysPrev.getPeriodPayrollDays() + hrsDaysCurr.getPeriodPayrollDays() + hrsDaysNext.getPeriodPayrollDays() - hrsDaysPrev.getDaysIncapacityNotPaid() - hrsDaysCurr.getDaysIncapacityNotPaid() - hrsDaysNext.getDaysIncapacityNotPaid()) * salarySsc);
                    break;
                    
                case SHrsConsts.SS_INC_KND_SSC_LET:
                    sscEarning = SLibUtils.roundAmount((hrsDaysPrev.getPeriodPayrollDays() + hrsDaysCurr.getPeriodPayrollDays() + hrsDaysNext.getPeriodPayrollDays() - hrsDaysPrev.getDaysIncapacityNotPaid() - hrsDaysCurr.getDaysIncapacityNotPaid() - hrsDaysNext.getDaysIncapacityNotPaid()) * mwzReferenceWage);
                    break;
                    
                case SHrsConsts.SS_INC_KND_SSC_GT:
                    sscEarning = SLibUtils.roundAmount(salarySsc <= (sscTableRow.getLowerLimitMwzReference() * mwzReferenceWage) ? 0 :
                           ((hrsDaysPrev.getPeriodPayrollDays() + hrsDaysCurr.getPeriodPayrollDays() + hrsDaysNext.getPeriodPayrollDays() - hrsDaysPrev.getDaysIncapacityNotPaid() - hrsDaysCurr.getDaysIncapacityNotPaid() - hrsDaysNext.getDaysIncapacityNotPaid()) * (salarySsc - (sscTableRow.getLowerLimitMwzReference() * mwzReferenceWage))));
                    break;
                    
                case SHrsConsts.SS_DIS_LIF:
                case SHrsConsts.SS_CRE:
                case SHrsConsts.SS_RSK:
                case SHrsConsts.SS_RET:
                case SHrsConsts.SS_SEV:
                case SHrsConsts.SS_HOM:
                    sscEarning = SLibUtils.roundAmount((hrsDaysPrev.getPeriodPayrollDays() + hrsDaysCurr.getPeriodPayrollDays() + hrsDaysNext.getPeriodPayrollDays() - hrsDaysPrev.getDaysNotWorkedNotPaid() - hrsDaysCurr.getDaysNotWorkedNotPaid() - hrsDaysNext.getDaysNotWorkedNotPaid()) * salarySsc);
                    break;
                    
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
            
            sscAssessed += SLibUtils.roundAmount(sscEarning * sscTableRow.getWorkerPercentage());
        }
        
        return sscAssessed;
    }
    
    /**
     * Estimates net monthly payment from a suggested gross payment.
     * @param session GUI session.
     * @param grossAmount Suggested gross monthly payment.
     * @param dateCutoff Cutoff date.
     * @param dateBenefits Benefits date.
     * @return Estimation.
     * @throws Exception 
     */
    public static SHrsCalculatedNetGrossAmount estimateMonthlyPaymentNet(final SGuiSession session, final double grossAmount, final Date dateCutoff, final Date dateBenefits) throws Exception {
        SHrsCalculatedNetGrossAmount hrsCalculatedNetGrossAmount = null;
        SDbTaxTable taxTable = null;
        SDbTaxSubsidyTable taxSubsidyTable = null;
        SDbSsContributionTable sscTable = null;
        SDbConfig config = null;
        double salaryDaily = 0;
        double salarySsc = 0;
        double mwzReferenceWage = 0;
        double netAmount = 0;
        double taxAmount = 0;
        double taxSubsidyAmount = 0;
        double sscAmount = 0;
        double tableFactor = 0;
        int year = SLibTimeUtils.digestYear(dateCutoff)[0];
        int days = SLibTimeUtils.getMaxDayOfMonth(dateCutoff);
        SHrsDaysByPeriod hrsDaysPrev = new SHrsDaysByPeriod(0, 0, 0, 0);
        SHrsDaysByPeriod hrsDaysCurr = new SHrsDaysByPeriod(year, 0, days, days);
        SHrsDaysByPeriod hrsDaysNext = new SHrsDaysByPeriod(0, 0, 0, 0);
        
        salaryDaily = grossAmount * SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS;
        salarySsc = salaryDaily * getSbcIntegrationFactor(session, dateBenefits, dateCutoff);
        
        config = (SDbConfig) session.readRegistry(SModConsts.HRS_CFG, new int[] { SUtilConsts.BPR_CO_ID });
        taxTable = (SDbTaxTable) session.readRegistry(SModConsts.HRS_TAX, new int[] { getRecentTaxTable(session, dateCutoff) });
        taxSubsidyTable = (SDbTaxSubsidyTable) session.readRegistry(SModConsts.HRS_TAX_SUB, new int[] { getRecentTaxSubsidyTable(session, dateCutoff) });
        sscTable = (SDbSsContributionTable) session.readRegistry(SModConsts.HRS_SSC, new int[] { getRecentSsContributionTable(session, dateCutoff) });
        mwzReferenceWage = getRecentMinimumWage(session, config.getFkMwzReferenceTypeId(), dateCutoff);
        
        tableFactor = ((double) SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS) * days;
        
        taxAmount = computeTax(taxTable, grossAmount, tableFactor);
        taxSubsidyAmount = computeTaxSubsidy(taxSubsidyTable, grossAmount, tableFactor);
        sscAmount = computeSsContribution(sscTable, salarySsc, mwzReferenceWage, hrsDaysPrev, hrsDaysCurr, hrsDaysNext);
        
        netAmount = grossAmount - taxAmount - sscAmount;
        
        hrsCalculatedNetGrossAmount = new SHrsCalculatedNetGrossAmount(netAmount, grossAmount, taxAmount, taxSubsidyAmount, sscAmount);
        hrsCalculatedNetGrossAmount.setCalculatedAmountType(SHrsConsts.CAL_NET_AMT_TYPE);
        hrsCalculatedNetGrossAmount.setSalary(salaryDaily);
        hrsCalculatedNetGrossAmount.setSalarySs(salarySsc);
        
        return hrsCalculatedNetGrossAmount;
    }
    
    /**
     * Estimates gross monthly payment from a suggested net payment.
     * @param session GUI session.
     * @param netAmount Suggested net monthly payment.
     * @param dateCutoff Cutoff date.
     * @param dateBenefits Benefits date.
     * @param tolerance Estimation tolerance as an amount of money.
     * @return
     * @throws Exception 
     */
    public static SHrsCalculatedNetGrossAmount estimateMonthlyPaymentGross(final SGuiSession session, final double netAmount, final Date dateCutoff, final Date dateBenefits, final double tolerance) throws Exception {
        SHrsCalculatedNetGrossAmount hrsCalculatedNetGrossAmount = null;
        SDbTaxTable taxTable = null;
        SDbTaxTableRow taxTableRow = null;
        int days = SLibTimeUtils.getMaxDayOfMonth(dateCutoff);
        double salaryDaily = 0;
        double salarySsc = 0;
        double tableFactor = 0;
        double average = 0;
        double grossAmount = 0;
        double limitInf = 0;
        double limitSup = 0;
        double toleranceAux = 0;
        boolean calculate = true;
        
        taxTable = (SDbTaxTable) session.readRegistry(SModConsts.HRS_TAX, new int[] { getRecentTaxTable(session, dateCutoff) });
        
        tableFactor = ((double) SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS) * days;
        
        if (taxTable != null) {
            for (int i = 0; i < taxTable.getChildRows().size(); i++) {
                taxTableRow = taxTable.getChildRows().get(i);
                if (i == 0) {
                    limitInf = SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactor);
                }
                if (netAmount <= SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactor)) {
                    limitSup = SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactor);
                }
                
                average = (limitInf + limitSup) / 2;
                
                salaryDaily = limitSup * SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS;
                salarySsc = salaryDaily * getSbcIntegrationFactor(session, dateBenefits, dateCutoff);

                hrsCalculatedNetGrossAmount = estimateMonthlyPaymentNet(session, average, dateCutoff, dateBenefits);
                
                if (hrsCalculatedNetGrossAmount.getNetAmount() > netAmount) {
                    break;
                }
            }
        }
        
        average = 0;

        while (calculate) {
            average = (limitInf + limitSup) / 2;
            
            salaryDaily = limitSup * SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS;
            salarySsc = salaryDaily * getSbcIntegrationFactor(session, dateBenefits, dateCutoff);

            hrsCalculatedNetGrossAmount = estimateMonthlyPaymentNet(session, average, dateCutoff, dateBenefits);

            if (hrsCalculatedNetGrossAmount.getNetAmount() > netAmount) {
                limitSup = average;
            }
            else {
                limitInf = average;
            }
            toleranceAux = SLibUtils.roundAmount(netAmount - hrsCalculatedNetGrossAmount.getNetAmount());
            
            calculate = SLibUtils.roundAmount(limitInf) != SLibUtils.roundAmount(limitSup) && Math.abs(toleranceAux) > tolerance;
        }
        grossAmount = average;
        
        hrsCalculatedNetGrossAmount.setSalary(salaryDaily);
        hrsCalculatedNetGrossAmount.setSalarySs(salarySsc);
        hrsCalculatedNetGrossAmount.setGrossAmount(grossAmount);
        hrsCalculatedNetGrossAmount.setCalculatedAmountType(SHrsConsts.CAL_GROSS_AMT_TYPE);
        
        return hrsCalculatedNetGrossAmount;
    }
    
    /**
     * Gets amounts (total, exempt and taxable) of earnings of employee.
     * @param session
     * @param employeeId
     * @param periodYear
     * @param dateCutOff
     * @param earningTypeId Can be omitted with 0.
     * @return
     * @throws Exception 
     */
    public static SHrsAmountEarning getAmountEarningByEmployee(final SGuiSession session, final int employeeId, final int periodYear, final Date dateCutOff, final int earningTypeId) throws Exception {
        SHrsAmountEarning amountEarning = null;
        String sql = "";
        ResultSet resultSet = null;
        Statement statement = session.getDatabase().getConnection().createStatement();

        sql = "SELECT SUM(pre.amt_r) AS f_amount, SUM(pre.amt_exem) AS f_exem, SUM(pre.amt_taxa) AS f_taxa " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON " +
            "p.id_pay = pr.id_pay " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON " +
            "pr.id_pay = pre.id_pay AND pr.id_emp = pre.id_emp " +
            "WHERE p.b_del = 0 AND pr.b_del = 0 AND pre.b_del = 0 AND pr.id_emp = " + employeeId + 
            (earningTypeId == 0 ? "" : " AND pre.fk_tp_ear = " + earningTypeId) + " AND " +
            "p.per_year = " + periodYear + " AND p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(dateCutOff) + "'";
        
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            amountEarning = new SHrsAmountEarning(earningTypeId);
            
            amountEarning.setAmount(resultSet.getDouble("f_amount"));
            amountEarning.setAmountExempt(resultSet.getDouble("f_exem"));
            amountEarning.setAmountTaxable(resultSet.getDouble("f_taxa"));
        }
        
        return amountEarning;
    }

    /**
     * Gets amount of deductions of employee.
     * @param session
     * @param employeeId
     * @param periodYear
     * @param dateCutOff
     * @param deductionTypeId Can be omitted with 0.
     * @return
     * @throws Exception 
     */
    public static double getAmountDeductionByEmployee(final SGuiSession session, final int employeeId, final int periodYear, final Date dateCutOff, final int deductionTypeId) throws Exception {
        double amountDeduction = 0;
        String sql = "";
        ResultSet resultSet = null;
        Statement statement = session.getDatabase().getConnection().createStatement();

        sql = "SELECT SUM(prd.amt_r) AS f_amount " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON " +
            "p.id_pay = pr.id_pay " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON " +
            "pr.id_pay = prd.id_pay AND pr.id_emp = prd.id_emp " +
            "WHERE p.b_del = 0 AND pr.b_del = 0 AND prd.b_del = 0 AND pr.id_emp = " + employeeId + 
            (deductionTypeId == 0 ? "" : " AND prd.fk_tp_ded = " + deductionTypeId) + " AND " +
            "p.per_year = " + periodYear + " AND p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(dateCutOff) + "'";
        
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            amountDeduction = resultSet.getDouble("f_amount");
        }
        
        return amountDeduction;
    }
    
    /**
     * Gets the accounting setup for a earning and a type of accounting specific configuration.
     * @param session User GUI session.
     * @param earningId earning Id.
     * @param accountingType accounting settings type.
     * @return Object of type SDbAccountingEarning
     * @throws Exception 
     */
    private static ArrayList<SDbAccountingEarning> getAccountingEarnings(final SGuiSession session, final int earningId, final int accountingType) throws Exception {
        ArrayList<SDbAccountingEarning> accountingEarnings = new ArrayList<>();
        
        String sql = "SELECT id_ref "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_EAR) + " "
                + "WHERE NOT b_del AND id_ear = " + earningId + " AND id_tp_acc = " + accountingType + " "
                + "ORDER BY id_ref;";
        
        try (ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                SDbAccountingEarning accountingEarning = new SDbAccountingEarning();
                accountingEarning.read(session, new int[] { earningId, accountingType, resultSet.getInt(1) });
                accountingEarnings.add(accountingEarning);
            }
        }
        
        return accountingEarnings;
    }
    
    /**
     * Gets the accounting setup for a deducction and a type of accounting specific configuration.
     * @param session User GUI session.
     * @param deductionId deducction Id.
     * @param accountingType accounting settings type.
     * @return Object of type SDbAccountingDeduction
     * @throws Exception 
     */
    private static ArrayList<SDbAccountingDeduction> getAccountingDeductions(final SGuiSession session, final int deductionId, final int accountingType) throws Exception {
        ArrayList<SDbAccountingDeduction> accountingDeductions = new ArrayList<>();
        
        String sql = "SELECT id_ref "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_DED) + " "
                + "WHERE NOT b_del AND id_ded = " + deductionId + " AND id_tp_acc = " + accountingType + " "
                + "ORDER BY id_ref;";
        
        try (ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                SDbAccountingDeduction accountingDeduction = new SDbAccountingDeduction();
                accountingDeduction.read(session, new int[] { deductionId, accountingType, resultSet.getInt(1) });
                accountingDeductions.add(accountingDeduction);
            }
        }
        
        return accountingDeductions;
    }
    
    /**
     * Create accounting settings by default for earning.
     * @return 
     */
    private static SDbAccountingEarning createAccountingEarning(final int earningId, final int accountingType, final int referenceId) {
        SDbAccountingEarning accountingEarning = new SDbAccountingEarning();
        
        accountingEarning.setPkEarningId(earningId);
        accountingEarning.setPkAccountingTypeId(accountingType);
        accountingEarning.setPkReferenceId(referenceId);
        //...
        accountingEarning.setFkAccountId(SModSysConsts.FIN_ACC_NA);
        
        return accountingEarning;
    }
    
    /**
     * Create accounting settings by default for deduction.
     * @return 
     */
    private static SDbAccountingDeduction createAccountingDeduction(final int deductionId, final int accountingType, final int referenceId) {
        SDbAccountingDeduction accountingDeduction = new SDbAccountingDeduction();
            
        accountingDeduction.setPkDeductionId(deductionId);
        accountingDeduction.setPkAccountingTypeId(accountingType);
        accountingDeduction.setPkReferenceId(referenceId);
        //...
        accountingDeduction.setFkAccountId(SModSysConsts.FIN_ACC_NA);
        
        return accountingDeduction;
    }
    
    /**
     * Creates accounting earning settings for departament.
     * @param accountingEarnings Array objects of SDbAccountingEarning type.
     * @param departamentId departament Id.
     * @return
     * @throws Exception 
     */
    private static SDbAccountingEarning createAccountingEarningForDepartament(final int earningId, final ArrayList<SDbAccountingEarning> accountingEarnings, final int departamentId) throws Exception {
        SDbAccountingEarning accountingEarning = null;
        
        for (SDbAccountingEarning o : accountingEarnings) {
            if (o.getPkReferenceId() == departamentId) {
                accountingEarning = o.clone(); // create it!
                break;
            }
        }
        
        if (accountingEarning == null) {
            accountingEarning = createAccountingEarning(earningId, SModSysConsts.HRSS_TP_ACC_DEP, departamentId);
        }
        
        return accountingEarning;
    }
    
    /**
     * Creates accounting deduction settings for departament.
     * @param accountingDeductions Array objects of SDbAccountingDeduction type.
     * @param departamentId departament Id.
     * @return
     * @throws Exception 
     */
    private static SDbAccountingDeduction createAccountingDeductionForDepartament(final int deductionId, final ArrayList<SDbAccountingDeduction> accountingDeductions, final int departamentId) throws Exception {
        SDbAccountingDeduction accountingDeduction = null;
        
        for (SDbAccountingDeduction o : accountingDeductions) {
            if (o.getPkReferenceId() == departamentId) {
                accountingDeduction = o.clone(); // create it!
                break;
            }
        }
        
        if (accountingDeduction == null) {
            accountingDeduction = createAccountingDeduction(deductionId, SModSysConsts.HRSS_TP_ACC_DEP, departamentId);
        }
        
        return accountingDeduction;
    }
    
    /**
     * Creates accounting settings for earning.
     * @param session User GUI session.
     * @param earningId Earning ID.
     * @param newAccountingType New accounting settings type.
     * @param oldAccountingType Old accounting settings type.
     * @throws Exception 
     */
    public static void createAccountingEarningConfiguration(final SGuiSession session, final int earningId, final int newAccountingType, final int oldAccountingType) throws Exception {
        ArrayList<SDbAccountingEarning> oldAccountingEarnings = null;
        
        if (oldAccountingType != 0 && oldAccountingType != newAccountingType) {
            oldAccountingEarnings = getAccountingEarnings(session, earningId, oldAccountingType);
        }
        
        if (oldAccountingType != newAccountingType) {
            String sql;
            SDbAccountingEarning accountingEarning;
            ArrayList<SDbAccountingEarning> newAccountingEarnings = new ArrayList<>();

            switch (newAccountingType) {
                case SModSysConsts.HRSS_TP_ACC_GBL:
                    // attempt to recover existing registry (if any, it would have been deleted):
                    accountingEarning = (SDbAccountingEarning) session.readRegistry(SModConsts.HRS_ACC_EAR, new int[] { earningId, newAccountingType, 0 });

                    // create new registry if needed:
                    if (accountingEarning.getQueryResultId() != SDbConsts.READ_OK) {
                        accountingEarning = createAccountingEarning(earningId, newAccountingType, 0);
                    }

                    accountingEarning.setDeleted(false); // force restoration

                    newAccountingEarnings.add(accountingEarning);
                    break;
                    
                case SModSysConsts.HRSS_TP_ACC_DEP:
                    // process all departments:
                    
                    sql = "SELECT id_dep FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " ORDER BY id_dep;";
                    try (ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql)) {
                        while (resultSet.next()) {
                            accountingEarning = null;
                            
                            if (oldAccountingEarnings != null && !oldAccountingEarnings.isEmpty()) {
                                switch (oldAccountingType) {
                                    case SModSysConsts.HRSS_TP_ACC_GBL:
                                        accountingEarning = oldAccountingEarnings.get(0).clone(); // the very one setting
                                        break;
                                        
                                    default:
                                }
                            }
                            
                            if (accountingEarning != null) {
                                accountingEarning.setPkAccountingTypeId(newAccountingType);
                                accountingEarning.setPkReferenceId(resultSet.getInt("id_dep"));
                            }
                            else {
                                // attempt to recover existing registry (if any, it would have been deleted):
                                accountingEarning = (SDbAccountingEarning) session.readRegistry(SModConsts.HRS_ACC_EAR, new int[] { earningId, newAccountingType, resultSet.getInt("id_dep") });
                                
                                // create new registry if needed:
                                if (accountingEarning.getQueryResultId() != SDbConsts.READ_OK) {
                                    accountingEarning = createAccountingEarning(earningId, newAccountingType, resultSet.getInt("id_dep"));
                                }
                            }
                            
                            accountingEarning.setDeleted(false); // force restoration
                            
                            newAccountingEarnings.add(accountingEarning);
                        }
                    }
                    break;
                    
                case SModSysConsts.HRSS_TP_ACC_EMP:
                    // process all employees:
                    
                    sql = "SELECT id_emp, fk_dep FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " ORDER BY id_emp;";
                    try (ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql)) {
                        while (resultSet.next()) {
                            accountingEarning = null;
                            
                            if (oldAccountingEarnings != null && !oldAccountingEarnings.isEmpty()) {
                                switch (oldAccountingType) {
                                    case SModSysConsts.HRSS_TP_ACC_GBL:
                                        accountingEarning = oldAccountingEarnings.get(0).clone(); // the very one setting
                                        break;
                                        
                                    case SModSysConsts.HRSS_TP_ACC_DEP:
                                        accountingEarning = createAccountingEarningForDepartament(earningId, oldAccountingEarnings, resultSet.getInt("fk_dep"));
                                        break;
                                        
                                    default:
                                }
                            }
                            
                            if (accountingEarning != null) {
                                accountingEarning.setPkAccountingTypeId(newAccountingType);
                                accountingEarning.setPkReferenceId(resultSet.getInt("id_emp"));
                            }
                            else {
                                // attempt to recover existing registry (if any, it would have been deleted):
                                accountingEarning = (SDbAccountingEarning) session.readRegistry(SModConsts.HRS_ACC_EAR, new int[] { earningId, newAccountingType, resultSet.getInt("id_emp") });
                                
                                // create new registry if needed:
                                if (accountingEarning.getQueryResultId() != SDbConsts.READ_OK) {
                                    accountingEarning = createAccountingEarning(earningId, newAccountingType, resultSet.getInt("id_emp"));
                                }
                            }
                            
                            accountingEarning.setDeleted(false); // force restoration
                            
                            newAccountingEarnings.add(accountingEarning);
                        }
                    }
                    break;
                    
                default:
            }
            
            for (SDbAccountingEarning o : newAccountingEarnings) {
                o.save(session);
            }
            
            if (oldAccountingEarnings != null) {
                for (SDbAccountingEarning o : oldAccountingEarnings) {
                    o.setDeleted(true);
                    o.save(session);
                }
            }
        }
    }
    
    /**
     * Creates accounting settings for deduction.
     * @param session User GUI session.
     * @param deductionId Deduction ID.
     * @param newAccountingType New accounting settings type.
     * @param oldAccountingType Old accounting settings type.
     * @throws Exception 
     */
    public static void createAccountingDeductionConfiguration(final SGuiSession session, final int deductionId, final int newAccountingType, final int oldAccountingType) throws Exception {
        ArrayList<SDbAccountingDeduction> oldAccountingDeductions = null;
        
        if (oldAccountingType != 0 && oldAccountingType != newAccountingType) {
            oldAccountingDeductions = getAccountingDeductions(session, deductionId, oldAccountingType);
        }
        
        if (oldAccountingType != newAccountingType) {
            String sql;
            SDbAccountingDeduction accountingDeduction;
            ArrayList<SDbAccountingDeduction> newAccountingDeductions = new ArrayList<>();

            switch (newAccountingType) {
                case SModSysConsts.HRSS_TP_ACC_GBL:
                    // attempt to recover existing registry (if any, it would have been deleted):
                    accountingDeduction = (SDbAccountingDeduction) session.readRegistry(SModConsts.HRS_ACC_DED, new int[] { deductionId, newAccountingType, 0 });
                    
                    // create new registry if needed:
                    if (accountingDeduction.getQueryResultId() != SDbConsts.READ_OK) {
                        accountingDeduction = createAccountingDeduction(deductionId, newAccountingType, 0);
                    }
                    
                    accountingDeduction.setDeleted(false); // force restoration
                    
                    newAccountingDeductions.add(accountingDeduction);
                    break;
                    
                case SModSysConsts.HRSS_TP_ACC_DEP:
                    // process all departments:
                    
                    sql = "SELECT id_dep FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " ORDER BY id_dep;";
                    try (ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql)) {
                        while (resultSet.next()) {
                            accountingDeduction = null;
                            
                            if (oldAccountingDeductions != null && !oldAccountingDeductions.isEmpty()) {
                                switch (oldAccountingType) {
                                    case SModSysConsts.HRSS_TP_ACC_GBL:
                                        accountingDeduction = oldAccountingDeductions.get(0).clone(); // the very one setting
                                        break;
                                        
                                    default:
                                }
                            }
                            
                            if (accountingDeduction != null) {
                                accountingDeduction.setPkAccountingTypeId(newAccountingType);
                                accountingDeduction.setPkReferenceId(resultSet.getInt("id_dep"));
                            }
                            else {
                                // attempt to recover existing registry (if any, it would have been deleted):
                                accountingDeduction = (SDbAccountingDeduction) session.readRegistry(SModConsts.HRS_ACC_DED, new int[] { deductionId, newAccountingType, resultSet.getInt("id_dep") });
                                
                                // create new registry if needed:
                                if (accountingDeduction.getQueryResultId() != SDbConsts.READ_OK) {
                                    accountingDeduction = createAccountingDeduction(deductionId, newAccountingType, resultSet.getInt("id_dep"));
                                }
                            }
                            
                            accountingDeduction.setDeleted(false); // force restoration
                            
                            newAccountingDeductions.add(accountingDeduction);
                        }
                    }
                    break;
                    
                case SModSysConsts.HRSS_TP_ACC_EMP:
                    // process all employees:
                    
                    sql = "SELECT id_emp, fk_dep FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " ORDER BY id_emp;";
                    try (ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql)) {
                        while (resultSet.next()) {
                            accountingDeduction = null;
                            
                            if (oldAccountingDeductions != null && !oldAccountingDeductions.isEmpty()) {
                                switch (oldAccountingType) {
                                    case SModSysConsts.HRSS_TP_ACC_GBL:
                                        accountingDeduction = oldAccountingDeductions.get(0).clone(); // the very one setting
                                        break;
                                        
                                    case SModSysConsts.HRSS_TP_ACC_DEP:
                                        accountingDeduction = createAccountingDeductionForDepartament(deductionId, oldAccountingDeductions, resultSet.getInt("fk_dep"));
                                        break;
                                        
                                    default:
                                }
                            }
                            
                            if (accountingDeduction != null) {
                                accountingDeduction.setPkAccountingTypeId(newAccountingType);
                                accountingDeduction.setPkReferenceId(resultSet.getInt("id_emp"));
                            }
                            else {
                                // attempt to recover existing registry (if any, it would have been deleted):
                                accountingDeduction = (SDbAccountingDeduction) session.readRegistry(SModConsts.HRS_ACC_DED, new int[] { deductionId, newAccountingType, resultSet.getInt("id_emp") });
                                
                                // create new registry if needed:
                                if (accountingDeduction.getQueryResultId() != SDbConsts.READ_OK) {
                                    accountingDeduction = createAccountingDeduction(deductionId, newAccountingType, resultSet.getInt("id_emp"));
                                }
                            }
                            
                            accountingDeduction.setDeleted(false); // force restoration
                            
                            newAccountingDeductions.add(accountingDeduction);
                        }
                    }
                    break;
                    
                default:
            }
            
            for (SDbAccountingDeduction o : newAccountingDeductions) {
                o.save(session);
            }
            
            if (oldAccountingDeductions != null) {
                for (SDbAccountingDeduction o : oldAccountingDeductions) {
                    o.setDeleted(true);
                    o.save(session);
                }
            }
        }
    }
}
