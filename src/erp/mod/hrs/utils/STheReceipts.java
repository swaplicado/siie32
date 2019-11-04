/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbConfig;
import erp.mod.hrs.db.SDbPayroll;
import erp.mod.hrs.db.SDbPayrollReceipt;
import erp.mod.hrs.db.SDbWorkingDaySettings;
import erp.mod.hrs.db.SHrsEmployee;
import erp.mod.hrs.db.SHrsPayroll;
import erp.mod.hrs.db.SHrsPayrollDataProvider;
import erp.mod.hrs.db.SHrsReceipt;
import erp.mod.hrs.db.SHrsUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.gui.util.SUtilConsts;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Edwin Carmona
 */
public class STheReceipts {
    private SGuiClient miClient;
    
    public STheReceipts(SGuiClient client) {
        this.miClient = client;
    }
    
    public boolean start(String path, int nYear, int nPer) {
         ResultSet resulPayroll;
         List<String[]> dataList = new ArrayList();
         dataList.add(new String[]
                            { 
                                "UUID XML",
                                "ISR_CALC",
                                "ISR_ACUM",
                                "ISR_PEND",
                                "SUB_CALC",
                                "SUB_ACUM",
                                "SUB_PEND",
                                "ISR X RET",
                                "SUB X PAG",
                                "ISR_XML",
                                "SUB_XML",
                                "RESULTADO",
                                "COMENTARIO",
                                "ID_PAY",
                                "ID_EMP",
                                "TIPO",
                                "NUM NOM",
                                "EMP"
                                });
        
        try {
            
            String query = "SELECT  " +
                            "    hp.fis_year, " +
                            "    hp.num, " +
                            "    hpr.id_pay, " +
                            "    hpr.id_emp, " +
                            "    bb.bp, " +
                            "    tc.uuid, " +
                            "    (SELECT  " +
                            "            MAX(fid_pay_rcp_iss_n) " +
                            "        FROM " +
                            "            trn_cfd " +
                            "        WHERE " +
                            "            fid_pay_rcp_pay_n = tc.fid_pay_rcp_pay_n " +
                            "                AND fid_pay_rcp_emp_n = tc.fid_pay_rcp_emp_n) AS max_issue " +
                            "FROM " +
                            "    hrs_pay hp " +
                            "        INNER JOIN " +
                            "    hrs_pay_rcp hpr ON hp.id_pay = hpr.id_pay " +
                            "        INNER JOIN " +
                            "    erp.bpsu_bp bb ON hpr.id_emp = bb.id_bp" +
                            "        INNER JOIN " +
                            "    trn_cfd tc ON hpr.id_pay = tc.fid_pay_rcp_pay_n " +
                            "        AND hpr.id_emp = tc.fid_pay_rcp_emp_n " +
                            "WHERE " +
                            "    hp.per_year = 2019 " +
                            "        AND hp.fk_tp_pay_sht = 1 " +
                            "        AND NOT hp.b_del " +
                            "        AND (tc.fid_st_xml = 2) AND hp.per_year = " + nYear + " AND hp.per = " + nPer +
                            " ORDER BY hp.num ASC, hp.fk_tp_pay, bb.bp ASC;";
            
            resulPayroll = miClient.getSession().getStatement().
                    getConnection().createStatement().
                    executeQuery(query);
            
            int id_pay = 0;
            int id_pay_read = 1;
            SDbPayroll payroll = null;
            String uuid = "";
            String nom = "";
            String emp = "";
            
            while (resulPayroll.next()) {
                id_pay_read = resulPayroll.getInt("id_pay");
                uuid = resulPayroll.getString("uuid");
                nom = resulPayroll.getString("num");
                emp = resulPayroll.getString("bp");
                
                if (id_pay_read != id_pay) {
                    id_pay = id_pay_read;
                    payroll = new SDbPayroll();
                    payroll.read(miClient.getSession(), new int[] { id_pay });
                }
                
                int receiptKey [] = new int[] { id_pay, resulPayroll.getInt("id_emp"), resulPayroll.getInt("max_issue") };
                SDbPayrollReceipt payrollReceipt = new SDbPayrollReceipt();
                payrollReceipt.read(miClient.getSession(), receiptKey);
                
                if (payrollReceipt.getPaymentMonthly() > 8000d) {
                    continue;
                }
                
                SOutputData row = this.calculate(payroll, payrollReceipt, uuid);
//                SOutputData row = this.calculate(payroll, payrollReceipt, uuid);
                
                dataList.add(new String[]
                            { 
                                row.getUuid(),
                                row.getTaxCalculated() + "",
                                row.getTaxPayed() + "",
                                (row.getTaxCalculated() - row.getTaxPayed()) + "",
                                row.getSubsidyCalculated() + "",
                                row.getSubPayed() + "",
                                (row.getSubsidyCalculated() - row.getSubPayed()) + "",
                                row.getTaxToHold() + "",
                                row.getSubsidyToPay() + "",
                                row.getTaxXml() + "",
                                row.getSubsidyXml() + "",
                                row.getResult(),
                                row.getComments(),
                                row.getIdPay() + "",
                                row.getIdEmp() + "",
                                row.getNomType(),
                                nom,
                                emp
                            });
            }
            
             DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
            //get current date time with Date()
            Date date = new Date();
            System.out.println(dateFormat.format(date));
            
            String sFile = path + "/recibos_" + dateFormat.format(new Date()) + ".csv";
            SCsvFileManager ob = new SCsvFileManager();
            ob.writeCsvFile(dataList, sFile);
            
            return true;
        }
        catch (SQLException ex) {
            Logger.getLogger(SReceiptsR.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(STheReceipts.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * 
     * 
     * @param payroll
     * @param payrollReceipt
     * @param uuid
     * @return 
     */
    private SOutputData calculate(SDbPayroll payroll, SDbPayrollReceipt payrollReceipt, String uuid) {
        SOutputData oRow = null;
        SDbPayrollReceipt payrollReceiptOriginal;
        
        try {
            payrollReceiptOriginal = payrollReceipt.clone();
            
            SDbWorkingDaySettings moWorkingDaySettings = SHrsUtils.getPayrollWorkingDaySettings(miClient.getSession(), payroll.getFkPaysheetTypeId());
            
            SDbConfig moConfig = (SDbConfig) miClient.getSession().readRegistry(SModConsts.HRS_CFG, new int[] { SUtilConsts.BPR_CO_ID });
            SHrsPayrollDataProvider prov = new SHrsPayrollDataProvider(miClient.getSession());
            
            SHrsPayroll moHrsPayroll = prov.createHrsPayroll(moConfig, moWorkingDaySettings, payroll, false);
            
            SHrsReceipt hrsReceipt = new SHrsReceipt();
            hrsReceipt.setHrsPayroll(moHrsPayroll);

            SHrsEmployee hrsEmployee = prov.createHrsEmployee(moHrsPayroll, payroll.getPkPayrollId(), payrollReceipt.getPkEmployeeId(), 
                                            payroll.getFiscalYear(), payroll.getPeriod(), payroll.getFiscalYear(), payroll.getDateStart(), payroll.getDateEnd(), payroll.getFkTaxComputationTypeId());
            hrsEmployee.setHrsReceipt(hrsReceipt);
            hrsReceipt.setHrsEmployee(hrsEmployee);

            hrsReceipt.setPayrollReceipt(payrollReceipt);
            
            if (payroll.isPayrollNormal()) {
                hrsReceipt.getAbsenceConsumptions().addAll(moHrsPayroll.crateAbsenceConsumptions(hrsReceipt));
            }

            hrsReceipt.getHrsReceiptEarnings().addAll(moHrsPayroll.createHrsReceiptEarnings(hrsReceipt, payroll.getDateStart(), payroll.getDateEnd()));
            hrsReceipt.getHrsReceiptDeductions().addAll(moHrsPayroll.createHrsReceiptDeductions(hrsReceipt, payroll.getDateStart(), payroll.getDateEnd()));

            hrsReceipt.renumberHrsReceiptEarnings();
            hrsReceipt.renumberHrsReceiptDeductions();
            hrsReceipt.computeReceipt();
            
            SDbPayrollReceipt receiptAux = hrsReceipt.getPayrollReceipt();
            
            oRow = new SOutputData();
            oRow.setIdPay(receiptAux.getPkPayrollId());
            oRow.setNomType(payroll.getAuxPaymentType());
            oRow.setIdEmp(receiptAux.getPkEmployeeId());
            oRow.setUuid(uuid);
            oRow.setSubsidyCalculated(receiptAux.getAnnualTaxSubsidyAssessed());
            oRow.setTaxCalculated(receiptAux.getAnnualTaxAssessed());
            oRow.setTaxPayed(receiptAux.getAnnualTaxPayed());
            oRow.setSubPayed(receiptAux.getAnnualTaxSubsidyPayed());
            
            DecimalFormat df = new DecimalFormat("##.00");
            if (receiptAux.getAnnualTaxSubsidyAssessed() > receiptAux.getAnnualTaxAssessed()) {
                double subsidy = receiptAux.getAnnualTaxSubsidyAssessed() - receiptAux.getAnnualTaxSubsidyPayed() - (receiptAux.getAnnualTaxAssessed() - receiptAux.getAnnualTaxPayed());

                if (Math.abs(subsidy - payrollReceiptOriginal.getAnnualTaxSubsidyAssessed()) < 0.001) { // si el monto es igual
                    System.out.println("CORRECTO. SUB > ISR");
                    oRow.setResult("CORRECTO");
                    oRow.setComments("SUB > ISR");
                }
                else {
                    // los montos no coinciden
                    System.out.println("INCORRECTO. SUB Calculado:" + df.format(subsidy) + " xml:" + payrollReceiptOriginal.getAnnualTaxSubsidyAssessed());
                    oRow.setResult("INCORRECTO");
                    oRow.setComments("SUB Calculado:" + df.format(subsidy) + " xml:" + df.format(payrollReceiptOriginal.getAnnualTaxSubsidyAssessed()));
                }
                
                oRow.setSubsidyToPay(df.format(subsidy));
            }
            else if (receiptAux.getAnnualTaxAssessed() > receiptAux.getAnnualTaxSubsidyAssessed()) {
                    double tax = receiptAux.getAnnualTaxAssessed() - receiptAux.getAnnualTaxPayed() - (receiptAux.getAnnualTaxSubsidyAssessed() - receiptAux.getAnnualTaxSubsidyPayed());

                    if (Math.abs(tax - payrollReceiptOriginal.getAnnualTaxAssessed()) < 0.001) { // si el monto es igual
                        System.out.println("CORRECTO. ISR > SUB");
                        oRow.setResult("CORRECTO");
                        oRow.setComments("ISR > SUB");
                    }
                    else {
                        // los montos no coinciden
                        System.out.println("INCORRECTO. ISR Calculado:" + df.format(tax) + " xml:" + payrollReceiptOriginal.getAnnualTaxAssessed());
                        oRow.setResult("INCORRECTO");
                        oRow.setComments("ISR Calculado:" + df.format(tax) + " xml:" + df.format(payrollReceiptOriginal.getAnnualTaxAssessed()));
                    }
                    
                    oRow.setTaxToHold(df.format(tax));
                }
            
            oRow.setSubsidyXml(payrollReceiptOriginal.getAnnualTaxSubsidyAssessed());
            oRow.setTaxXml(payrollReceiptOriginal.getAnnualTaxAssessed());
            
            System.out.println("");
        }
        catch (Exception ex) {
            Logger.getLogger(STheReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return oRow;
    }
}
