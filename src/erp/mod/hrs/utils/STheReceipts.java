/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

import erp.SClientUtils;
import erp.client.SClientInterface;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbConfig;
import erp.mod.hrs.db.SDbPayroll;
import erp.mod.hrs.db.SDbPayrollReceipt;
import erp.mod.hrs.db.SDbWorkingDaySettings;
import erp.mod.hrs.db.SHrsConsts;
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
 * Regeneración de CFDI de nóminas con dato incorrecto de Subsidio para el empleo.
 * @author Edwin Carmona, Isabel Servín
 */
public class STheReceipts {
    private SGuiClient miClient;
    
    private final static int DATA_TYPE_TEXT = 1;
    
    public STheReceipts(SGuiClient client) {
        this.miClient = client;
    }
    
    public boolean start(String path, int nYear, int nPer) {
         ResultSet resulPayroll;
         List<String[]> dataList = new ArrayList<>();
         dataList.add(new String[]
                            { 
                                "UUID XML",
                                "FOLIO XML",
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
                            "    erp.f_get_xml_atr('cfdi:Comprobante', 'Folio=', xc.doc_xml, " + DATA_TYPE_TEXT + ") AS _xml_folio, " +
                            "    IF(hpr.fk_tp_pay = " + SModSysConsts.HRSS_TP_PAY_WEE + ", " +
                            "        hpr.sal * " + SHrsConsts.MONTH_DAYS + ", " +
                            "        hpr.wage) AS montly_sal," +
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
                            "        LEFT OUTER JOIN " + 
                                 SClientUtils.getComplementaryDdName((SClientInterface)miClient) + ".trn_cfd AS xc ON tc.id_cfd = xc.id_cfd " +
                            "WHERE " +
                            "        hp.fk_tp_pay_sht = 1 " +
                            "        AND NOT hp.b_del " +
                            "        AND (tc.fid_st_xml = 2) AND hp.per_year = " + nYear + " AND hp.per = " + nPer + " " +
                            "HAVING montly_sal < 8000 " +
                            "ORDER BY hp.num ASC, hp.fk_tp_pay, bb.bp ASC;";
            
            resulPayroll = miClient.getSession().getStatement().
                    getConnection().createStatement().
                    executeQuery(query);
            
            int id_pay = 0;
            int id_pay_read = 1;
            SDbPayroll payroll = null;
            String uuid = "";
            String nom = "";
            String emp = "";
            String xmlFolio = "";
            SHrsPayroll hrsPayroll = null;
            SHrsPayrollDataProvider hrsPayrollDataProvider = new SHrsPayrollDataProvider(miClient.getSession());
            SDbConfig moduleConfig = (SDbConfig) miClient.getSession().readRegistry(SModConsts.HRS_CFG, new int[] { SUtilConsts.BPR_CO_ID });
            
            while (resulPayroll.next()) {
                id_pay_read = resulPayroll.getInt("id_pay");
                uuid = resulPayroll.getString("uuid");
                xmlFolio = resulPayroll.getString("_xml_folio");
                nom = resulPayroll.getString("num");
                emp = resulPayroll.getString("bp");
                
                if (id_pay_read != id_pay) {
                    id_pay = id_pay_read;
                    payroll = new SDbPayroll();
                    payroll.read(miClient.getSession(), new int[] { id_pay });
                    
                    SDbWorkingDaySettings workingDaySettings = SHrsUtils.getPayrollWorkingDaySettings(miClient.getSession(), payroll.getFkPaysheetTypeId());
                    hrsPayroll = hrsPayrollDataProvider.createHrsPayroll(moduleConfig, workingDaySettings, payroll);
                }
                
                int[] receiptKey = new int[] { id_pay, resulPayroll.getInt("id_emp"), resulPayroll.getInt("max_issue") };
                SDbPayrollReceipt payrollReceipt = new SDbPayrollReceipt();
                payrollReceipt.read(miClient.getSession(), receiptKey);
                
                SOutputData row = this.calculate(payroll, hrsPayroll, payrollReceipt, uuid, xmlFolio, hrsPayrollDataProvider);
                
                dataList.add(new String[] {
                    row.getUuid(),
                    row.getXmlFolio(),
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
        }
        catch (Exception ex) {
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
    private SOutputData calculate(SDbPayroll payroll, SHrsPayroll hrsPayroll, SDbPayrollReceipt payrollReceipt, String uuid, String xmlFolio, SHrsPayrollDataProvider dataProvider) {
        SOutputData oRow = null;
        SDbPayrollReceipt payrollReceiptOriginal;
        
        try {
            payrollReceiptOriginal = payrollReceipt.clone();
            
            // create HRS receipt:
            SHrsReceipt hrsReceipt = new SHrsReceipt(hrsPayroll);

            // create HRS employee:
            SHrsEmployee hrsEmployee = dataProvider.createHrsEmployee(hrsPayroll, hrsReceipt, payrollReceipt.getPkEmployeeId());
            
            // asign HRS employee to HRS receipt:
            hrsReceipt.setHrsEmployee(hrsEmployee);

            // asign payroll receipt to HRS receipt:
            hrsReceipt.setPayrollReceipt(payrollReceipt);
            
            if (payroll.isPayrollNormal()) {
                hrsReceipt.getAbsenceConsumptions().addAll(hrsPayroll.crateAbsenceConsumptions(hrsReceipt));
            }

            hrsReceipt.getHrsReceiptEarnings().addAll(hrsPayroll.createHrsReceiptEarnings(hrsReceipt, payroll.getDateStart(), payroll.getDateEnd()));
            hrsReceipt.getHrsReceiptDeductions().addAll(hrsPayroll.createHrsReceiptDeductions(hrsReceipt, payroll.getDateStart(), payroll.getDateEnd()));

            hrsReceipt.renumberHrsReceiptEarnings();
            hrsReceipt.renumberHrsReceiptDeductions();
            hrsReceipt.computeReceipt();
            
            SDbPayrollReceipt receiptAux = hrsReceipt.getPayrollReceipt();
            
            if (payroll.getFkTaxComputationTypeId() == SModSysConsts.HRSS_TP_TAX_COMP_ANN) {
              oRow = this.getRowAnnualCalc(payroll, payrollReceiptOriginal, receiptAux, uuid, xmlFolio);
            }
            else {
              oRow = this.getRowPayrollCalc(payroll, payrollReceiptOriginal, receiptAux, uuid, xmlFolio);
            }
            
            System.out.println("");
        }
        catch (Exception ex) {
            Logger.getLogger(STheReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return oRow;
    }
    
    private SOutputData getRowAnnualCalc(SDbPayroll payroll, SDbPayrollReceipt payrollReceiptOriginal, SDbPayrollReceipt receiptAux, String uuid, String xmlFolio) {
        SOutputData oRow = new SOutputData();
        oRow.setIdPay(receiptAux.getPkPayrollId());
        oRow.setNomType(payroll.getAuxPaymentType());
        oRow.setIdEmp(receiptAux.getPkEmployeeId());
        oRow.setUuid(uuid);
        oRow.setXmlFolio(xmlFolio);
        oRow.setSubsidyCalculated(receiptAux.getAnnualTaxSubsidyAssessed());
        oRow.setTaxCalculated(receiptAux.getAnnualTaxAssessed());
        oRow.setTaxPayed(receiptAux.getAnnualTaxPayed());
        oRow.setSubPayed(receiptAux.getAnnualTaxSubsidyPayed());

        DecimalFormat df = new DecimalFormat("#0.00");
        if ((receiptAux.getAnnualTaxSubsidyAssessed() - receiptAux.getAnnualTaxSubsidyPayed()) > (receiptAux.getAnnualTaxAssessed() - receiptAux.getAnnualTaxPayed())) {
            double subsidy = receiptAux.getAnnualTaxSubsidyAssessed() - receiptAux.getAnnualTaxSubsidyPayed() - (receiptAux.getAnnualTaxAssessed() - receiptAux.getAnnualTaxPayed());

            if (Math.abs(subsidy - payrollReceiptOriginal.getAnnualTaxSubsidyAssessed()) < 0.001) { // si el monto es igual
                System.out.println("CORRECTO. SUB > ISR");
                oRow.setResult("CORRECTO");
                oRow.setComments("SUB > ISR");
            }
            else {
                // los montos no coinciden
                System.out.println("SUB Calculado: " + df.format(subsidy) + 
                                    "; XML SUB: " + df.format(payrollReceiptOriginal.getAnnualTaxSubsidyAssessed()) + 
                                    (payrollReceiptOriginal.getAnnualTaxAssessed() > 0 ? "; XML ISR: " + df.format(payrollReceiptOriginal.getAnnualTaxAssessed()) : ""));
                oRow.setResult("INCORRECTO");
                oRow.setComments("SUB Calculado: " + df.format(subsidy) + 
                                    "; XML SUB: " + df.format(payrollReceiptOriginal.getAnnualTaxSubsidyAssessed()) + 
                                    (payrollReceiptOriginal.getAnnualTaxAssessed() > 0 ? "; XML ISR: " + df.format(payrollReceiptOriginal.getAnnualTaxAssessed()) : ""));
            }

            oRow.setSubsidyToPay(df.format(subsidy));
        }
        else if ((receiptAux.getAnnualTaxAssessed() - receiptAux.getAnnualTaxPayed()) > (receiptAux.getAnnualTaxSubsidyAssessed() - receiptAux.getAnnualTaxSubsidyPayed())) {
                double tax = receiptAux.getAnnualTaxAssessed() - receiptAux.getAnnualTaxPayed() - (receiptAux.getAnnualTaxSubsidyAssessed() - receiptAux.getAnnualTaxSubsidyPayed());

                if (Math.abs(tax - payrollReceiptOriginal.getAnnualTaxAssessed()) < 0.001) { // si el monto es igual
                    System.out.println("CORRECTO. ISR > SUB");
                    oRow.setResult("CORRECTO");
                    oRow.setComments("ISR > SUB");
                }
                else {
                    // los montos no coinciden
                    System.out.println("ISR Calculado: " + df.format(tax) + 
                                        "; XML ISR: " + df.format(payrollReceiptOriginal.getAnnualTaxAssessed()) + 
                                            (payrollReceiptOriginal.getAnnualTaxSubsidyAssessed() > 0 ? "; XML SUB: " + df.format(payrollReceiptOriginal.getAnnualTaxSubsidyAssessed()) : ""));
                    oRow.setResult("INCORRECTO");
                    oRow.setComments("ISR Calculado: " + df.format(tax) + 
                                        "; XML ISR: " + df.format(payrollReceiptOriginal.getAnnualTaxAssessed()) + 
                                            (payrollReceiptOriginal.getAnnualTaxSubsidyAssessed() > 0 ? "; XML SUB: " + df.format(payrollReceiptOriginal.getAnnualTaxSubsidyAssessed()) : ""));
                }

                oRow.setTaxToHold(df.format(tax));
            }
            else {
                double subsidy = receiptAux.getAnnualTaxSubsidyAssessed() - receiptAux.getAnnualTaxSubsidyPayed() - (receiptAux.getAnnualTaxAssessed() - receiptAux.getAnnualTaxPayed());
                double tax = receiptAux.getAnnualTaxAssessed() - receiptAux.getAnnualTaxPayed() - (receiptAux.getAnnualTaxSubsidyAssessed() - receiptAux.getAnnualTaxSubsidyPayed());

                oRow.setSubsidyToPay(df.format(subsidy));
                oRow.setTaxToHold(df.format(tax));

                System.out.println("ISR Calculado: " + df.format(tax) + " SUB calculado: " + df.format(subsidy) +
                                        "; XML ISR: " + df.format(payrollReceiptOriginal.getAnnualTaxAssessed()) + "; XML SUB: " + df.format(payrollReceiptOriginal.getAnnualTaxSubsidyAssessed()));
                oRow.setResult("NO DEFINIDO");
                oRow.setComments("ISR Calculado: " + df.format(tax) + " SUB calculado: " + df.format(subsidy) +
                                        "; XML ISR: " + df.format(payrollReceiptOriginal.getAnnualTaxAssessed()) + "; XML SUB: " + df.format(payrollReceiptOriginal.getAnnualTaxSubsidyAssessed()));
            }

        oRow.setSubsidyXml(payrollReceiptOriginal.getAnnualTaxSubsidyAssessed());
        oRow.setTaxXml(payrollReceiptOriginal.getAnnualTaxAssessed());
        
        return oRow;
    }
    
    private SOutputData getRowPayrollCalc(SDbPayroll payroll, SDbPayrollReceipt payrollReceiptOriginal, SDbPayrollReceipt receiptAux, String uuid, String xmlFolio) {
        SOutputData oRow = new SOutputData();
        oRow.setIdPay(receiptAux.getPkPayrollId());
        oRow.setNomType(payroll.getAuxPaymentType());
        oRow.setIdEmp(receiptAux.getPkEmployeeId());
        oRow.setUuid(uuid);
        oRow.setXmlFolio(xmlFolio);
        oRow.setSubsidyCalculated(receiptAux.getPayrollTaxSubsidyAssessed());
        oRow.setTaxCalculated(receiptAux.getPayrollTaxAssessed());
        oRow.setTaxPayed(receiptAux.getPayrollTaxPayed());
        oRow.setSubPayed(receiptAux.getPayrollTaxSubsidyPayed());

        DecimalFormat df = new DecimalFormat("#0.00");
        if ((receiptAux.getPayrollTaxSubsidyAssessed() - receiptAux.getPayrollTaxSubsidyPayed()) > (receiptAux.getPayrollTaxAssessed() - receiptAux.getPayrollTaxPayed())) {
            double subsidy = receiptAux.getPayrollTaxSubsidyAssessed() - receiptAux.getPayrollTaxSubsidyPayed() - (receiptAux.getPayrollTaxAssessed() - receiptAux.getPayrollTaxPayed());

            if (Math.abs(subsidy - payrollReceiptOriginal.getPayrollTaxSubsidyAssessed()) < 0.001) { // si el monto es igual
                System.out.println("CORRECTO. SUB > ISR");
                oRow.setResult("CORRECTO");
                oRow.setComments("SUB > ISR");
            }
            else {
                // los montos no coinciden
                System.out.println("SUB Calculado: " + df.format(subsidy) + 
                                    "; XML SUB: " + df.format(payrollReceiptOriginal.getPayrollTaxSubsidyAssessed()) + 
                                    (payrollReceiptOriginal.getPayrollTaxAssessed() > 0 ? "; XML ISR: " + df.format(payrollReceiptOriginal.getPayrollTaxAssessed()) : ""));
                oRow.setResult("INCORRECTO");
                oRow.setComments("SUB Calculado: " + df.format(subsidy) + 
                                    "; XML SUB: " + df.format(payrollReceiptOriginal.getPayrollTaxSubsidyAssessed()) + 
                                    (payrollReceiptOriginal.getPayrollTaxAssessed() > 0 ? "; XML ISR: " + df.format(payrollReceiptOriginal.getPayrollTaxAssessed()) : ""));
            }

            oRow.setSubsidyToPay(df.format(subsidy));
        }
        else if ((receiptAux.getPayrollTaxAssessed() - receiptAux.getPayrollTaxPayed()) > (receiptAux.getPayrollTaxSubsidyAssessed() - receiptAux.getPayrollTaxSubsidyPayed())) {
                double tax = receiptAux.getPayrollTaxAssessed() - receiptAux.getPayrollTaxPayed() - (receiptAux.getPayrollTaxSubsidyAssessed() - receiptAux.getPayrollTaxSubsidyPayed());

                if (Math.abs(tax - payrollReceiptOriginal.getPayrollTaxAssessed()) < 0.001) { // si el monto es igual
                    System.out.println("CORRECTO. ISR > SUB");
                    oRow.setResult("CORRECTO");
                    oRow.setComments("ISR > SUB");
                }
                else {
                    // los montos no coinciden
                    System.out.println("ISR Calculado: " + df.format(tax) + 
                                        "; XML ISR: " + df.format(payrollReceiptOriginal.getPayrollTaxAssessed()) + 
                                            (payrollReceiptOriginal.getPayrollTaxSubsidyAssessed() > 0 ? "; XML SUB: " + df.format(payrollReceiptOriginal.getPayrollTaxSubsidyAssessed()) : ""));
                    oRow.setResult("INCORRECTO");
                    oRow.setComments("ISR Calculado: " + df.format(tax) + 
                                        "; XML ISR: " + df.format(payrollReceiptOriginal.getPayrollTaxAssessed()) + 
                                            (payrollReceiptOriginal.getPayrollTaxSubsidyAssessed() > 0 ? "; XML SUB: " + df.format(payrollReceiptOriginal.getPayrollTaxSubsidyAssessed()) : ""));
                }

                oRow.setTaxToHold(df.format(tax));
            }
            else {
                double subsidy = receiptAux.getPayrollTaxSubsidyAssessed() - receiptAux.getPayrollTaxSubsidyPayed() - (receiptAux.getPayrollTaxAssessed() - receiptAux.getPayrollTaxPayed());
                double tax = receiptAux.getPayrollTaxAssessed() - receiptAux.getPayrollTaxPayed() - (receiptAux.getPayrollTaxSubsidyAssessed() - receiptAux.getPayrollTaxSubsidyPayed());

                oRow.setSubsidyToPay(df.format(subsidy));
                oRow.setTaxToHold(df.format(tax));

                System.out.println("ISR Calculado: " + df.format(tax) + " SUB calculado: " + df.format(subsidy) +
                                        "; XML ISR: " + df.format(payrollReceiptOriginal.getPayrollTaxAssessed()) + "; XML SUB: " + df.format(payrollReceiptOriginal.getPayrollTaxSubsidyAssessed()));
                oRow.setResult("NO DEFINIDO");
                oRow.setComments("ISR Calculado: " + df.format(tax) + " SUB calculado: " + df.format(subsidy) +
                                        "; XML ISR: " + df.format(payrollReceiptOriginal.getPayrollTaxAssessed()) + "; XML SUB: " + df.format(payrollReceiptOriginal.getPayrollTaxSubsidyAssessed()));
            }

        oRow.setSubsidyXml(payrollReceiptOriginal.getPayrollTaxSubsidyAssessed());
        oRow.setTaxXml(payrollReceiptOriginal.getPayrollTaxAssessed());
        
        return oRow;
    }
}
