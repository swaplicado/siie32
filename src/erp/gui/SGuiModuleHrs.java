/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.cfd.SCfdConsts;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.gui.mod.cfg.SCfgMenu;
import erp.gui.mod.cfg.SCfgModule;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STableTabComponent;
import erp.lib.table.STableTabInterface;
import erp.mhrs.form.SDialogFormerPayrollImport;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SHrsConsts;
import erp.mod.hrs.form.SDialogCalculateIncomeTax;
import erp.mod.hrs.form.SDialogPayrollReceiptSsc;
import erp.mod.hrs.form.SDialogRepHrsActiveEmployees;
import erp.mod.hrs.form.SDialogRepHrsAuxPayroll;
import erp.mod.hrs.form.SDialogRepHrsEarDed;
import erp.mod.hrs.form.SDialogRepHrsEarningDeduction;
import erp.mod.hrs.form.SDialogRepHrsEarningsDeductionsFileCsv;
import erp.mod.hrs.form.SDialogRepHrsPayrollTax;
import erp.mod.hrs.form.SDialogRepHrsPayrollWageSalaryFileCsv;
import erp.mod.hrs.form.SDialogRepVacationsFileCsv;
import erp.mod.hrs.form.SFormCalculateNetGrossAmount;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Sergio Flores, Juan Barajas
 */
public class SGuiModuleHrs extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmCfg;
    private javax.swing.JMenuItem jmiCfgTaxTable;
    private javax.swing.JMenuItem jmiCfgTaxTableRow;
    private javax.swing.JMenuItem jmiCfgTaxSubsidyTable;
    private javax.swing.JMenuItem jmiCfgTaxSubsidyTableRow;
    private javax.swing.JMenuItem jmiCfgSsContributionTable;
    private javax.swing.JMenuItem jmiCfgSsContributionTableRow;
    private javax.swing.JMenuItem jmiCfgBenefitTable;
    private javax.swing.JMenuItem jmiCfgBenefitTableRow;
    private javax.swing.JMenuItem jmiCfgFirstDayYear;
    private javax.swing.JMenuItem jmiCfgHoliday;
    private javax.swing.JMenuItem jmiCfgWorkingDaySettings;
    private javax.swing.JMenu jmCfgBkkEarning;
    private javax.swing.JMenuItem jmiCfgBkkEarningGlobal;
    private javax.swing.JMenuItem jmiCfgBkkEarningDepartament;
    private javax.swing.JMenuItem jmiCfgBkkEarningEmployee;
    private javax.swing.JMenu jmCfgBkkDeduction;
    private javax.swing.JMenuItem jmiCfgBkkDeductionGlobal;
    private javax.swing.JMenuItem jmiCfgBkkDeductionDepartament;
    private javax.swing.JMenuItem jmiCfgBkkDeductionEmployee;
    private javax.swing.JMenuItem jmiCfgConfig;
    
    private javax.swing.JMenu jmCat;
    private javax.swing.JMenuItem jmiCatEmployee;
    private javax.swing.JMenuItem jmiCatEmployeeRelatives;
    private javax.swing.JMenuItem jmiCatEmployeeIntegral;
    private javax.swing.JMenuItem jmiCatEmployeeHireLog;
    private javax.swing.JMenuItem jmiCatEmployeeWageLog;
    private javax.swing.JMenuItem jmiCatEmployeeSscBaseLog;
    private javax.swing.JMenuItem jmiCatEmployeeSscBaseUpdate;
    private javax.swing.JMenuItem jmiCatEarnings;
    private javax.swing.JMenuItem jmiCatDeductions;
    private javax.swing.JMenuItem jmiCatDeparment;
    private javax.swing.JMenuItem jmiCatPosition;
    private javax.swing.JMenuItem jmiCatShift;
    private javax.swing.JMenuItem jmiCatEmployeeType;
    private javax.swing.JMenuItem jmiCatWorkerType;
    private javax.swing.JMenuItem jmiCatEmployeeDismissType;
    private javax.swing.JMenuItem jmiCatMwzType;
    private javax.swing.JMenuItem jmiCatMwzTypeWage;
    private javax.swing.JMenuItem jmiCatUma;
    private javax.swing.JMenuItem jmiCatUmi;
    private javax.swing.JMenuItem jmiCatWorkerTypeSalary;
    private javax.swing.JMenuItem jmiCatLoanTypeAdjustment;
    private javax.swing.JMenuItem jmiCatAbsenceType;
    private javax.swing.JMenuItem jmiCatAbsenceClass;
    
    private javax.swing.JMenu jmPay;
    private javax.swing.JMenuItem jmiPayPayrollWeekly;
    private javax.swing.JMenuItem jmiPayPayrollWeeklyRec;
    private javax.swing.JMenuItem jmiPayPayrollFortnightly;
    private javax.swing.JMenuItem jmiPayPayrollFortnightlyRowRec;
    private javax.swing.JMenuItem jmiPayAbsence;
    private javax.swing.JMenuItem jmiPayPayrollBkkRecord;
    private javax.swing.JMenuItem jmiPayCfdiPayroll;
    private javax.swing.JMenuItem jmiPayCfdiPayrollRec;
    private javax.swing.JMenu jmPayCfdi;
    private javax.swing.JMenuItem jmiPayCfdiStampSign;
    private javax.swing.JMenuItem jmiPayCfdiStampSignPending;
    private javax.swing.JMenuItem jmiPayCfdiSendingLog;
    private javax.swing.JMenu jmPayAutoEarnings;
    private javax.swing.JMenuItem jmiPayAutoEarningsGlobal;
    private javax.swing.JMenuItem jmiPayAutoEarningsByEmployee;
    private javax.swing.JMenuItem jmiPayAutoEarningsByEmployeeDet;
    private javax.swing.JMenu jmPayAutoDeductions;
    private javax.swing.JMenuItem jmiPayAutoDeductionsGlobal;
    private javax.swing.JMenuItem jmiPayAutoDeductionsByEmployee;
    private javax.swing.JMenuItem jmiPayAutoDeductionsByEmployeeDet;
    private javax.swing.JMenuItem jmiPayCalculatedAmountMonth;
    private javax.swing.JMenuItem jmiPayCalculatedEstimateIncomeTax;
    
    private javax.swing.JMenu jmBenefit;
    private javax.swing.JMenuItem jmiBenefitBenefitVac;
    private javax.swing.JMenuItem jmiBenefitBenefitBonVac;
    private javax.swing.JMenuItem jmiBenefitBenefitBonAnn;
    private javax.swing.JMenuItem jmiBenefitBenefitAdjustmentEarning;
    private javax.swing.JMenuItem jmiBenefitLoan;
    private javax.swing.JMenuItem jmiBenefitLoanAdjustmentEarning;
    private javax.swing.JMenuItem jmiBenefitLoanAdjustmentDeduction;
    private javax.swing.JMenuItem jmiBenefitAdvanceSettlement;
    
    private javax.swing.JMenu jmImp;
    private javax.swing.JMenuItem jmiImpFormerPayroll;
    private javax.swing.JMenuItem jmiImpFormerPayrollEmp;
    private javax.swing.JMenuItem jmiImpImport;
    private javax.swing.JMenuItem jmiImpCfdiPayroll;
    private javax.swing.JMenuItem jmiImpCfdiPayrollDetail;
    private javax.swing.JMenu jmImpCfdi;
    private javax.swing.JMenuItem jmiImpCfdiStampSign;
    private javax.swing.JMenuItem jmiImpCfdiStampSignPending;
    private javax.swing.JMenuItem jmiImpCfdiSendingLog;
    
    private javax.swing.JMenu jmRep;
    private javax.swing.JMenuItem jmiRepPayrollEarningsDeductions;
    private javax.swing.JMenuItem jmiRepPayrollEarnings;
    private javax.swing.JMenuItem jmiRepPayrollEarningsByEmployee;
    private javax.swing.JMenuItem jmiRepPayrollDeductions;
    private javax.swing.JMenuItem jmiRepPayrollDeductionsByEmployee;
    private javax.swing.JMenuItem jmiRepPayrollTax;
    private javax.swing.JMenuItem jmiRepPayrollAux;
    private javax.swing.JMenuItem jmiRepPayrollWageSalaryFileCsv;
    private javax.swing.JMenuItem jmiRepPayrollEarDedFileCsv;
    private javax.swing.JMenuItem jmiRepVacationsFileCsv;
    private javax.swing.JMenuItem jmiRepEmployeeActiveByPeriod;

    private erp.mhrs.form.SDialogFormerPayrollImport moDialogFormerPayrollImport;

    public SGuiModuleHrs(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_HRS);
        initComponents();
    }

    private void initComponents() {
        jmCfg = new JMenu("Configuración");
        jmiCfgTaxTable = new JMenuItem("Tablas de impuesto");
        jmiCfgTaxTableRow = new JMenuItem("Tablas de impuesto a detalle");
        jmiCfgTaxSubsidyTable = new JMenuItem("Tablas de Subsidio para el empleo");
        jmiCfgTaxSubsidyTableRow = new JMenuItem("Tablas de Subsidio para el empleo a detalle");
        jmiCfgSsContributionTable = new JMenuItem("Tablas de retención de SS");
        jmiCfgSsContributionTableRow = new JMenuItem("Tablas de retención de SS a detalle");
        jmiCfgBenefitTable = new JMenuItem("Tablas de prestaciones");
        jmiCfgBenefitTableRow = new JMenuItem("Tablas de prestaciones a detalle");
        jmiCfgFirstDayYear = new JMenuItem("Primer día del año");
        jmiCfgHoliday = new JMenuItem("Días feriados");
        jmiCfgWorkingDaySettings = new JMenuItem("Días laborables");
        jmCfgBkkEarning = new JMenu("Configuración contable de percepciones");
        jmiCfgBkkEarningGlobal = new JMenuItem("Globales");
        jmiCfgBkkEarningDepartament = new JMenuItem("Por departamento");
        jmiCfgBkkEarningEmployee = new JMenuItem("Por empleado");
        jmCfgBkkDeduction = new JMenu("Configuración contable de deducciones");
        jmiCfgBkkDeductionGlobal = new JMenuItem("Globales");
        jmiCfgBkkDeductionDepartament = new JMenuItem("Por departamento");
        jmiCfgBkkDeductionEmployee = new JMenuItem("Por empleado");
        jmiCfgConfig = new JMenuItem("Configuración del módulo");

        jmCfg.add(jmiCfgTaxTable);
        jmCfg.add(jmiCfgTaxTableRow);
        jmCfg.add(jmiCfgTaxSubsidyTable);
        jmCfg.add(jmiCfgTaxSubsidyTableRow);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgSsContributionTable);
        jmCfg.add(jmiCfgSsContributionTableRow);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgBenefitTable);
        jmCfg.add(jmiCfgBenefitTableRow);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgFirstDayYear);
        jmCfg.add(jmiCfgHoliday);
        jmCfg.add(jmiCfgWorkingDaySettings);
        jmCfg.addSeparator();
        jmCfgBkkEarning.add(jmiCfgBkkEarningGlobal);
        jmCfgBkkEarning.add(jmiCfgBkkEarningDepartament);
        jmCfgBkkEarning.add(jmiCfgBkkEarningEmployee);
        jmCfg.add(jmCfgBkkEarning);
        jmCfgBkkDeduction.add(jmiCfgBkkDeductionGlobal);
        jmCfgBkkDeduction.add(jmiCfgBkkDeductionDepartament);
        jmCfgBkkDeduction.add(jmiCfgBkkDeductionEmployee);
        jmCfg.add(jmCfgBkkDeduction);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgConfig);

        jmCat = new JMenu("Catálogos");
        jmiCatEmployee = new JMenuItem("Empleados");
        jmiCatEmployeeRelatives = new JMenuItem("Datos personales de empleados");
        jmiCatEmployeeIntegral = new JMenuItem("Consulta integral de empleados");
        jmiCatEmployeeHireLog = new JMenuItem("Bitácora de altas y bajas");
        jmiCatEmployeeWageLog = new JMenuItem("Bitácora de sueldos y salarios");
        jmiCatEmployeeSscBaseLog = new JMenuItem("Bitácora de salarios base de cotización");
        jmiCatEmployeeSscBaseUpdate = new JMenuItem("Actualización de salarios base de cotización");
        jmiCatEarnings = new JMenuItem("Percepciones");
        jmiCatDeductions = new JMenuItem("Deducciones");
        jmiCatDeparment = new JMenuItem("Departamentos");
        jmiCatPosition = new JMenuItem("Puestos");
        jmiCatShift = new JMenuItem("Turnos");
        jmiCatEmployeeType = new JMenuItem("Tipos de empleado");
        jmiCatWorkerType = new JMenuItem("Tipos de obrero");
        jmiCatEmployeeDismissType = new JMenuItem("Tipos de baja");
        jmiCatMwzType = new JMenuItem("Áreas geográficas");
        jmiCatWorkerTypeSalary = new JMenuItem("Salarios por tipo de obrero");
        jmiCatLoanTypeAdjustment = new JMenuItem("Ajuste por tipo de crédito/préstamo");
        jmiCatMwzTypeWage = new JMenuItem("Salarios mínimos de áreas geográficas");
        jmiCatUma = new JMenuItem("Unidades de Medida y Actualización (UMA)");
        jmiCatUmi = new JMenuItem("Unidades Mixtas INFONAVIT (UMI)");
        jmiCatAbsenceType = new JMenuItem("Tipos de incidencia");
        jmiCatAbsenceClass = new JMenuItem("Clases de incidencia");

        jmCat.add(jmiCatEmployee);
        jmCat.add(jmiCatEmployeeRelatives);
        jmCat.add(jmiCatEmployeeIntegral);
        jmCat.addSeparator();
        jmCat.add(jmiCatEmployeeHireLog);
        jmCat.add(jmiCatEmployeeWageLog);
        jmCat.add(jmiCatEmployeeSscBaseLog);
        //jmCat.add(jmiCatEmployeeSscBaseUpdate); XXX (2016-10-25) jbarajas is necesary revision
        jmCat.addSeparator();
        jmCat.add(jmiCatEarnings);
        jmCat.add(jmiCatDeductions);
        jmCat.addSeparator();
        jmCat.add(jmiCatDeparment);
        jmCat.add(jmiCatPosition);
        jmCat.add(jmiCatShift);
        jmCat.addSeparator();
        jmCat.add(jmiCatEmployeeType);
        jmCat.add(jmiCatWorkerType);
        jmCat.add(jmiCatEmployeeDismissType);
        jmCat.addSeparator();
        jmCat.add(jmiCatMwzType);
        jmCat.add(jmiCatMwzTypeWage);
        jmCat.add(jmiCatUma);
        jmCat.add(jmiCatUmi);
        jmCat.add(jmiCatWorkerTypeSalary);
        jmCat.add(jmiCatLoanTypeAdjustment);
        jmCat.addSeparator();
        jmCat.add(jmiCatAbsenceType);
        jmCat.add(jmiCatAbsenceClass);

        jmPay = new JMenu("Nóminas");
        jmiPayPayrollWeekly = new JMenuItem("Nóminas semanales");
        jmiPayPayrollWeeklyRec = new JMenuItem("Recibos de nóminas semanales");
        jmiPayPayrollFortnightly = new JMenuItem("Nóminas quincenales");
        jmiPayPayrollFortnightlyRowRec = new JMenuItem("Recibos de nóminas quincenales");
        jmiPayAbsence = new JMenuItem("Incidencias");
        jmiPayPayrollBkkRecord = new JMenuItem("Recibos de nóminas vs. pólizas contables");
        jmiPayCfdiPayroll = new JMenuItem("CFDI de nóminas");
        jmiPayCfdiPayrollRec = new JMenuItem("CFDI de recibos de nóminas");
        jmPayCfdi = new JMenu("Comprobantes fiscales digitales");
        jmiPayCfdiStampSign = new JMenuItem("CFDI de nóminas timbrados");
        jmiPayCfdiStampSignPending = new JMenuItem("CFDI de nóminas por timbrar");
        jmiPayCfdiSendingLog = new JMenuItem("Bitácora de envíos de CFDI de nóminas");
        jmPayAutoEarnings = new JMenu("Percepciones automáticas");
        jmiPayAutoEarningsGlobal = new JMenuItem("Globales");
        jmiPayAutoEarningsByEmployee = new JMenuItem("Por empleado");
        jmiPayAutoEarningsByEmployeeDet = new JMenuItem("Por empleado a detalle");
        jmPayAutoDeductions = new JMenu("Deducciones automáticas");
        jmiPayAutoDeductionsGlobal = new JMenuItem("Globales");
        jmiPayAutoDeductionsByEmployee = new JMenuItem("Por empleado");
        jmiPayAutoDeductionsByEmployeeDet = new JMenuItem("Por empleado a detalle");
        jmiPayCalculatedAmountMonth = new JMenuItem("Calcular ingreso mensual");
        jmiPayCalculatedEstimateIncomeTax = new JMenuItem("Calcular impuesto acumulado");

        jmPay.add(jmiPayPayrollWeekly);
        jmPay.add(jmiPayPayrollWeeklyRec);
        jmPay.addSeparator();
        jmPay.add(jmiPayPayrollFortnightly);
        jmPay.add(jmiPayPayrollFortnightlyRowRec);
        jmPay.addSeparator();
        jmPay.add(jmiPayAbsence);
        jmPay.addSeparator();
        jmPay.add(jmiPayPayrollBkkRecord);
        jmPay.add(jmiPayCfdiPayroll);
        jmPay.add(jmiPayCfdiPayrollRec);
        jmPayCfdi.add(jmiPayCfdiStampSign);
        jmPayCfdi.add(jmiPayCfdiStampSignPending);
        jmPayCfdi.addSeparator();
        jmPayCfdi.add(jmiPayCfdiSendingLog);
        jmPay.add(jmPayCfdi);
        jmPay.addSeparator();
        jmPayAutoEarnings.add(jmiPayAutoEarningsGlobal);
        jmPayAutoEarnings.add(jmiPayAutoEarningsByEmployee);
        jmPayAutoEarnings.add(jmiPayAutoEarningsByEmployeeDet);
        jmPay.add(jmPayAutoEarnings);
        jmPayAutoDeductions.add(jmiPayAutoDeductionsGlobal);
        jmPayAutoDeductions.add(jmiPayAutoDeductionsByEmployee);
        jmPayAutoDeductions.add(jmiPayAutoDeductionsByEmployeeDet);
        jmPay.add(jmPayAutoDeductions);
        jmPay.addSeparator();
        jmPay.add(jmiPayCalculatedAmountMonth);
        jmPay.add(jmiPayCalculatedEstimateIncomeTax);
                
        jmBenefit = new JMenu("Prestaciones");
        jmiBenefitBenefitVac = new JMenuItem("Control de vacaciones");
        jmiBenefitBenefitBonVac = new JMenuItem("Control de prima vacacional");
        jmiBenefitBenefitBonAnn = new JMenuItem("Control de gratificación anual");
        jmiBenefitBenefitAdjustmentEarning = new JMenuItem("Ajustes a prestaciones");
        jmiBenefitLoan = new JMenuItem("Control de créditos y préstamos");
        jmiBenefitLoanAdjustmentEarning = new JMenuItem("Incremento de créditos y préstamos");
        jmiBenefitLoanAdjustmentDeduction = new JMenuItem("Decremento de créditos y préstamos");
        jmiBenefitAdvanceSettlement = new JMenuItem("Control de adelantos de liquidación");
        
        jmBenefit.add(jmiBenefitBenefitVac);
        jmBenefit.add(jmiBenefitBenefitBonVac);
        jmBenefit.add(jmiBenefitBenefitBonAnn);
        jmBenefit.addSeparator();
        jmBenefit.add(jmiBenefitBenefitAdjustmentEarning);
        jmBenefit.addSeparator();
        jmBenefit.add(jmiBenefitLoan);
        jmBenefit.addSeparator();
        jmBenefit.add(jmiBenefitLoanAdjustmentEarning);
        jmBenefit.add(jmiBenefitLoanAdjustmentDeduction);
        jmBenefit.addSeparator();
        jmBenefit.add(jmiBenefitAdvanceSettlement);

        jmImp = new JMenu("Importación");
        jmiImpFormerPayroll = new JMenuItem("Nóminas importadas");
        jmiImpFormerPayrollEmp = new JMenuItem("Nóminas importadas vs. pólizas contables");
        jmiImpImport = new JMenuItem("Importación de nóminas...");
        jmiImpCfdiPayroll = new JMenuItem("CFDI de nóminas importadas");
        jmiImpCfdiPayrollDetail = new JMenuItem("CFDI de recibos de nóminas importadas");

        jmImp.add(jmiImpFormerPayroll);
        jmImp.add(jmiImpFormerPayrollEmp);
        jmImp.addSeparator();
        jmImp.add(jmiImpImport);
        jmImp.addSeparator();
        jmImp.add(jmiImpCfdiPayroll);
        jmImp.add(jmiImpCfdiPayrollDetail);
        jmImpCfdi = new JMenu("Comprobantes fiscales digitales");
        jmiImpCfdiStampSign = new JMenuItem("CFDI de nóminas importadas timbrados");
        jmiImpCfdiStampSignPending = new JMenuItem("CFDI de nóminas importadas por timbrar");
        jmiImpCfdiSendingLog = new JMenuItem("Bitácora de envíos de CFDI de nóminas importadas");
        jmImpCfdi.add(jmiImpCfdiStampSign);
        jmImpCfdi.add(jmiImpCfdiStampSignPending);
        jmImpCfdi.addSeparator();
        jmImpCfdi.add(jmiImpCfdiSendingLog);
        jmImp.add(jmImpCfdi);

        jmRep = new JMenu("Reportes");
        jmiRepPayrollEarningsDeductions = new JMenuItem("Percepciones y deducciones por periodo...");
        jmiRepPayrollEarnings = new JMenuItem("Percepciones por periodo...");
        jmiRepPayrollEarningsByEmployee = new JMenuItem("Percepciones por empleado por periodo...");
        jmiRepPayrollDeductions = new JMenuItem("Deducciones por periodo...");
        jmiRepPayrollDeductionsByEmployee = new JMenuItem("Deducciones por empleado por periodo...");
        jmiRepPayrollTax = new JMenuItem("Impuesto sobre nóminas...");
        jmiRepPayrollAux = new JMenuItem("Auxiliares de nóminas...");
        jmiRepPayrollWageSalaryFileCsv = new JMenuItem("Archivo CSV para declaración informativa de sueldos y salarios...");
        jmiRepPayrollEarDedFileCsv = new JMenuItem("Archivo CSV de percepciones y deducciones en el ejercicio...");
        jmiRepVacationsFileCsv = new JMenuItem("Archivo CSV de vacaciones pendientes...");
        jmiRepEmployeeActiveByPeriod = new JMenuItem("Reporte de empleados activos por periodo...");
        
        jmRep.add(jmiRepPayrollTax);
        jmRep.addSeparator();
        jmRep.add(jmiRepPayrollEarningsDeductions);
        /*
        jmRep.add(jmiRepPayrollEarnings);
        jmRep.add(jmiRepPayrollEarningsByEmployee);
        jmRep.add(jmiRepPayrollDeductions);
        jmRep.add(jmiRepPayrollDeductionsByEmployee);
        */
        jmRep.add(jmiRepPayrollAux);
        jmRep.addSeparator();
        jmRep.add(jmiRepPayrollWageSalaryFileCsv);
        jmRep.add(jmiRepPayrollEarDedFileCsv);
        jmRep.add(jmiRepVacationsFileCsv);
        jmRep.addSeparator();
        jmRep.add(jmiRepEmployeeActiveByPeriod);

        jmiCfgTaxTable.addActionListener(this);
        jmiCfgTaxTableRow.addActionListener(this);
        jmiCfgTaxSubsidyTable.addActionListener(this);
        jmiCfgTaxSubsidyTableRow.addActionListener(this);
        jmiCfgSsContributionTable.addActionListener(this);
        jmiCfgSsContributionTableRow.addActionListener(this);
        jmiCfgBenefitTable.addActionListener(this);
        jmiCfgBenefitTableRow.addActionListener(this);
        jmiCfgFirstDayYear.addActionListener(this);
        jmiCfgHoliday.addActionListener(this);
        jmiCfgWorkingDaySettings.addActionListener(this);
        jmiCfgBkkEarningGlobal.addActionListener(this);
        jmiCfgBkkEarningDepartament.addActionListener(this);
        jmiCfgBkkEarningEmployee.addActionListener(this);
        jmiCfgBkkDeductionGlobal.addActionListener(this);
        jmiCfgBkkDeductionDepartament.addActionListener(this);
        jmiCfgBkkDeductionEmployee.addActionListener(this);
        jmiCfgConfig.addActionListener(this);
        
        jmiCatEmployee.addActionListener(this);
        jmiCatEmployeeRelatives.addActionListener(this);
        jmiCatEmployeeIntegral.addActionListener(this);
        jmiCatEmployeeHireLog.addActionListener(this);
        jmiCatEmployeeWageLog.addActionListener(this);
        jmiCatEmployeeSscBaseLog.addActionListener(this);
        jmiCatEmployeeSscBaseUpdate.addActionListener(this);
        jmiCatEarnings.addActionListener(this);
        jmiCatDeductions.addActionListener(this);
        jmiCatDeparment.addActionListener(this);
        jmiCatPosition.addActionListener(this);
        jmiCatShift.addActionListener(this);
        jmiCatEmployeeType.addActionListener(this);
        jmiCatWorkerType.addActionListener(this);
        jmiCatEmployeeDismissType.addActionListener(this);
        jmiCatMwzType.addActionListener(this);
        jmiCatMwzTypeWage.addActionListener(this);
        jmiCatUma.addActionListener(this);
        jmiCatUmi.addActionListener(this);
        jmiCatWorkerTypeSalary.addActionListener(this);
        jmiCatLoanTypeAdjustment.addActionListener(this);
        jmiCatAbsenceType.addActionListener(this);
        jmiCatAbsenceClass.addActionListener(this);
        
        jmiPayPayrollWeekly.addActionListener(this);
        jmiPayPayrollWeeklyRec.addActionListener(this);
        jmiPayPayrollFortnightly.addActionListener(this);
        jmiPayPayrollFortnightlyRowRec.addActionListener(this);
        jmiPayAbsence.addActionListener(this);
        jmiPayPayrollBkkRecord.addActionListener(this);
        jmiPayCfdiPayroll.addActionListener(this);
        jmiPayCfdiPayrollRec.addActionListener(this);
        jmiPayCfdiStampSign.addActionListener(this);
        jmiPayCfdiStampSignPending.addActionListener(this);
        jmiPayCfdiSendingLog.addActionListener(this);
        jmiPayAutoEarningsGlobal.addActionListener(this);
        jmiPayAutoEarningsByEmployee.addActionListener(this);
        jmiPayAutoEarningsByEmployeeDet.addActionListener(this);
        jmiPayAutoDeductionsGlobal.addActionListener(this);
        jmiPayAutoDeductionsByEmployee.addActionListener(this);
        jmiPayAutoDeductionsByEmployeeDet.addActionListener(this);
        jmiPayCalculatedAmountMonth.addActionListener(this);
        jmiPayCalculatedEstimateIncomeTax.addActionListener(this);
        
        jmiBenefitBenefitVac.addActionListener(this);
        jmiBenefitBenefitBonVac.addActionListener(this);
        jmiBenefitBenefitBonAnn.addActionListener(this);
        jmiBenefitBenefitAdjustmentEarning.addActionListener(this);
        jmiBenefitLoan.addActionListener(this);
        jmiBenefitLoanAdjustmentEarning.addActionListener(this);
        jmiBenefitLoanAdjustmentDeduction.addActionListener(this);
        jmiBenefitAdvanceSettlement.addActionListener(this);
        
        jmiImpFormerPayroll.addActionListener(this);
        jmiImpFormerPayrollEmp.addActionListener(this);
        jmiImpImport.addActionListener(this);
        jmiImpCfdiPayroll.addActionListener(this);
        jmiImpCfdiPayrollDetail.addActionListener(this);
        jmiImpCfdiStampSign.addActionListener(this);
        jmiImpCfdiStampSignPending.addActionListener(this);
        jmiImpCfdiSendingLog.addActionListener(this);
        
        jmiRepPayrollEarningsDeductions.addActionListener(this);
        jmiRepPayrollEarnings.addActionListener(this);
        jmiRepPayrollDeductions.addActionListener(this);
        jmiRepPayrollEarningsByEmployee.addActionListener(this);
        jmiRepPayrollDeductionsByEmployee.addActionListener(this);
        jmiRepPayrollTax.addActionListener(this);
        jmiRepPayrollAux.addActionListener(this);
        jmiRepPayrollWageSalaryFileCsv.addActionListener(this);
        jmiRepPayrollEarDedFileCsv.addActionListener(this);
        jmiRepVacationsFileCsv.addActionListener(this);
        jmiRepEmployeeActiveByPeriod.addActionListener(this);

        jmiCfgTaxTable.setEnabled(true);
        jmiCfgTaxTableRow.setEnabled(true);
        jmiCfgTaxSubsidyTable.setEnabled(true);
        jmiCfgTaxSubsidyTableRow.setEnabled(true);
        jmiCfgSsContributionTable.setEnabled(true);
        jmiCfgSsContributionTableRow.setEnabled(true);
        jmiCfgBenefitTable.setEnabled(true);
        jmiCfgBenefitTableRow.setEnabled(true);
        jmiCfgFirstDayYear.setEnabled(true);
        jmiCfgHoliday.setEnabled(true);
        jmiCfgWorkingDaySettings.setEnabled(true);
        jmiCfgConfig.setEnabled(true);
        jmCfgBkkEarning.setEnabled(true);
        jmiCfgBkkEarningGlobal.setEnabled(true);
        jmiCfgBkkEarningDepartament.setEnabled(true);
        jmiCfgBkkEarningEmployee.setEnabled(true);
        jmCfgBkkDeduction.setEnabled(true);
        jmiCfgBkkDeductionGlobal.setEnabled(true);
        jmiCfgBkkDeductionDepartament.setEnabled(true);
        jmiCfgBkkDeductionEmployee.setEnabled(true);
        jmCfg.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CFG).HasRight);
        
        jmCat.setEnabled(
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT).HasRight ||
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP).HasRight ||
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP_WAGE).HasRight);
        jmiCatEmployee.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP).HasRight);
        jmiCatEmployeeRelatives.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP).HasRight);
        jmiCatEmployeeIntegral.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP).HasRight);
        jmiCatEmployeeHireLog.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP_WAGE).HasRight);
        jmiCatEmployeeWageLog.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP_WAGE).HasRight);
        jmiCatEmployeeSscBaseLog.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP_WAGE).HasRight);
        jmiCatEmployeeSscBaseUpdate.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP_WAGE).HasRight);
        jmiCatEarnings.setEnabled(true);
        jmiCatDeductions.setEnabled(true);
        jmiCatDeparment.setEnabled(true);
        jmiCatPosition.setEnabled(true);
        jmiCatShift.setEnabled(true);
        jmiCatEmployeeType.setEnabled(true);
        jmiCatWorkerType.setEnabled(true);
        jmiCatEmployeeDismissType.setEnabled(true);
        jmiCatMwzType.setEnabled(true);
        jmiCatMwzTypeWage.setEnabled(true);
        jmiCatUma.setEnabled(true);
        jmiCatUmi.setEnabled(true);
        jmiCatWorkerTypeSalary.setEnabled(true);
        jmiCatLoanTypeAdjustment.setEnabled(true);
        jmiCatAbsenceType.setEnabled(true);
        jmiCatAbsenceClass.setEnabled(true);
        jmiPayAbsence.setEnabled(true);
        
        boolean isPermissionPay = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_PAY).HasRight;
        boolean isPermissionPayWee = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_PAY_WEE).HasRight;
        boolean isPermissionPayFor = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_PAY_FOR).HasRight;
        
        jmPay.setEnabled(isPermissionPay || isPermissionPayWee || isPermissionPayFor);
        jmPayCfdi.setEnabled(isPermissionPay);
        jmiPayPayrollWeekly.setEnabled(isPermissionPay || isPermissionPayWee);
        jmiPayPayrollWeeklyRec.setEnabled(isPermissionPay || isPermissionPayWee);
        jmiPayPayrollFortnightly.setEnabled(isPermissionPay || isPermissionPayFor);
        jmiPayPayrollFortnightlyRowRec.setEnabled(isPermissionPay || isPermissionPayFor);
        jmiPayAbsence.setEnabled(isPermissionPay);
        jmiPayPayrollBkkRecord.setEnabled(isPermissionPay);
        jmiPayCfdiPayroll.setEnabled(isPermissionPay);
        jmiPayCfdiPayrollRec.setEnabled(isPermissionPay);
        jmiPayCfdiStampSign.setEnabled(isPermissionPay);
        jmiPayCfdiStampSignPending.setEnabled(isPermissionPay);
        jmiPayCfdiSendingLog.setEnabled(isPermissionPay);
        jmiPayAutoEarningsGlobal.setEnabled(isPermissionPay);
        jmiPayAutoEarningsByEmployee.setEnabled(isPermissionPay);
        jmiPayAutoEarningsByEmployeeDet.setEnabled(isPermissionPay);
        jmiPayAutoDeductionsGlobal.setEnabled(isPermissionPay);
        jmiPayAutoDeductionsByEmployee.setEnabled(isPermissionPay);
        jmiPayAutoDeductionsByEmployeeDet.setEnabled(isPermissionPay);
        jmiPayCalculatedAmountMonth.setEnabled(isPermissionPay);
        jmiPayCalculatedEstimateIncomeTax.setEnabled(isPermissionPay);
        
        jmBenefit.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_PAY).HasRight);
        jmiBenefitBenefitVac.setEnabled(true);
        jmiBenefitBenefitBonVac.setEnabled(true);
        jmiBenefitBenefitBonAnn.setEnabled(true);
        jmiBenefitBenefitAdjustmentEarning.setEnabled(true);
        jmiBenefitLoan.setEnabled(true);
        jmiBenefitLoanAdjustmentEarning.setEnabled(true);
        jmiBenefitLoanAdjustmentDeduction.setEnabled(true);
        jmiBenefitAdvanceSettlement.setEnabled(true);
        
        jmImp.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_IMP).HasRight);
        jmiImpFormerPayroll.setEnabled(true);
        jmiImpFormerPayrollEmp.setEnabled(true);
        jmiImpImport.setEnabled(true);
        jmiImpCfdiPayroll.setEnabled(true);
        jmiImpCfdiPayrollDetail.setEnabled(true);
        jmImpCfdi.setEnabled(true);
        
        jmRep.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_REP).HasRight);
        jmiRepPayrollEarningsDeductions.setEnabled(true);
        jmiRepPayrollEarnings.setEnabled(true);
        jmiRepPayrollDeductions.setEnabled(true);
        jmiRepPayrollEarningsByEmployee.setEnabled(true);
        jmiRepPayrollDeductionsByEmployee.setEnabled(true);
        jmiRepPayrollTax.setEnabled(true);
        jmiRepPayrollAux.setEnabled(true);
        jmiRepPayrollWageSalaryFileCsv.setEnabled(true);
        jmiRepPayrollEarDedFileCsv.setEnabled(true);
        jmiRepVacationsFileCsv.setEnabled(true);
        
        // GUI configuration:
        
        if (((erp.SClient) miClient).getCfgProcesor() != null) {
            SCfgModule module = new SCfgModule("" + mnModuleType);
            SCfgMenu menu = null;
            
            menu = new SCfgMenu(jmImp, "" + SDataConstants.MOD_HRS_IMP);
            module.getChildMenus().add(menu);
            
            ((erp.SClient) miClient).getCfgProcesor().processModule(module);
        }
    }

    private void showPanelQueryIntegralEmployee(int panelType) {
        int index = 0;
        int count = 0;
        boolean exists = false;
        String title = "";
        STableTabInterface tableTab = null;

        count = miClient.getTabbedPane().getTabCount();
        for (index = 0; index < count; index++) {
            if (miClient.getTabbedPane().getComponentAt(index) instanceof STableTabInterface) {
                tableTab = (STableTabInterface) miClient.getTabbedPane().getComponentAt(index);
                if (tableTab.getTabType() == panelType && tableTab.getTabTypeAux01() == SLibConstants.UNDEFINED && tableTab.getTabTypeAux02() == SLibConstants.UNDEFINED) {
                    exists = true;
                    break;
                }
            }
        }

        if (exists) {
            miClient.getTabbedPane().setSelectedIndex(index);
        }
        else {
            switch (panelType) {
                case SModConsts.HRSX_EMP_INT:
                    title = "Consulta integral empleados";
                    tableTab = new SPanelQueryIntegralEmployee(miClient);
                    break;
                default:
                    tableTab = null;
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
            }

            if (tableTab != null) {
                miClient.getTabbedPane().addTab(title, (JComponent) tableTab);
                miClient.getTabbedPane().setTabComponentAt(count, new STableTabComponent(miClient.getTabbedPane(), miClient.getImageIcon(mnModuleType)));
                miClient.getTabbedPane().setSelectedIndex(count);
            }
        }
    }

    private int showForm(int formType, int auxType, java.lang.Object pk, boolean isCopy) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void showView(int viewType) {
        showView(viewType, 0, 0);
    }

    @Override
    public void showView(int viewType, int auxType) {
        showView(viewType, auxType, 0);
    }

    @Override
    public void showView(int viewType, int auxType01, int auxType02) {
        java.lang.String sViewTitle = "";
        java.lang.Class oViewClass = null;

        try {
            setFrameWaitCursor();

            switch (viewType) {
                case SDataConstants.HRS_SIE_PAY:
                    oViewClass = erp.mhrs.view.SViewFormerPayroll.class;
                    sViewTitle = "Nóminas imp.";
                    break;
                case SDataConstants.HRS_SIE_PAY_EMP:
                    oViewClass = erp.mhrs.view.SViewFormerPayrollEmp.class;
                    sViewTitle = "Nóminas imp. vs. pólizas contables";
                    break;
                case SDataConstants.TRN_CFD:
                    oViewClass = erp.mtrn.view.SViewCfdXml.class;
                    switch (auxType02) {
                        case SCfdConsts.CFDI_PAYROLL_VER_OLD:
                            sViewTitle = "CFDI nóminas imp.";
                            break;
                        case SCfdConsts.CFDI_PAYROLL_VER_CUR:
                            sViewTitle = "CFDI nóminas";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    switch (auxType01) {
                        case SDataConstants.TRNX_STAMP_SIGN:
                            sViewTitle += " timbrados";
                            break;
                        case SDataConstants.TRNX_STAMP_SIGN_PEND:
                            sViewTitle += " x timbrar";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                case SDataConstants.TRN_CFD_SND_LOG:
                    oViewClass = erp.mtrn.view.SViewCfdSendingLog.class;
                    sViewTitle = "Bitácora envíos CFDI";
                    switch (auxType01) {
                        case SCfdConsts.CFDI_PAYROLL_VER_OLD:
                            sViewTitle += " nóminas imp.";
                            break;
                        case SCfdConsts.CFDI_PAYROLL_VER_CUR:
                            sViewTitle += " nóminas";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
            }

            processView(oViewClass, sViewTitle, viewType, auxType01, auxType02);
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            restoreFrameCursor();
        }
    }

   @Override
    public int showForm(int formType, java.lang.Object pk) {
        return showForm(formType, SDataConstants.UNDEFINED, pk, false);
    }

    @Override
    public int showForm(int formType, int auxType, java.lang.Object pk) {
        return showForm(formType, auxType, pk, false);
    }

   @Override
    public int showFormForCopy(int formType, java.lang.Object pk) {
        return showForm(formType, SDataConstants.UNDEFINED, pk, true);
    }

    @Override
    public erp.lib.form.SFormOptionPickerInterface getOptionPicker(int optionType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int annulRegistry(int registryType, java.lang.Object pk, sa.lib.gui.SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int deleteRegistry(int registryType, java.lang.Object pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JMenu[] getMenues() {
        return new JMenu[] { jmCfg, jmCat, jmPay, jmBenefit, jmImp, jmRep };
    }

    @Override
    public javax.swing.JMenu[] getMenuesForModule(int moduleType) {
        return null;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JMenuItem) {
            javax.swing.JMenuItem item = (javax.swing.JMenuItem) e.getSource();

            if (item == jmiCfgTaxTable) {
                miClient.getSession().showView(SModConsts.HRS_TAX, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgTaxTableRow) {
                miClient.getSession().showView(SModConsts.HRS_TAX_ROW, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgTaxSubsidyTable) {
                miClient.getSession().showView(SModConsts.HRS_TAX_SUB, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgTaxSubsidyTableRow) {
                miClient.getSession().showView(SModConsts.HRS_TAX_SUB_ROW, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgSsContributionTable) {
                miClient.getSession().showView(SModConsts.HRS_SSC, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgSsContributionTableRow) {
                miClient.getSession().showView(SModConsts.HRS_SSC_ROW, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgBenefitTable) {
                miClient.getSession().showView(SModConsts.HRS_BEN, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgBenefitTableRow) {
                miClient.getSession().showView(SModConsts.HRS_BEN_ROW, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgFirstDayYear) {
                miClient.getSession().showView(SModConsts.HRS_FDY, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgHoliday) {
                miClient.getSession().showView(SModConsts.HRS_HOL, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgWorkingDaySettings) {
                miClient.getSession().showView(SModConsts.HRS_WDS, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgBkkEarningGlobal) {
                miClient.getSession().showView(SModConsts.HRS_ACC_EAR, SModSysConsts.HRSS_TP_ACC_GBL, null);
            }
            else if (item == jmiCfgBkkEarningDepartament) {
                miClient.getSession().showView(SModConsts.HRS_ACC_EAR, SModSysConsts.HRSS_TP_ACC_DEP, null);
            }
            else if (item == jmiCfgBkkEarningEmployee) {
                miClient.getSession().showView(SModConsts.HRS_ACC_EAR, SModSysConsts.HRSS_TP_ACC_EMP, null);
            }
            else if (item == jmiCfgBkkDeductionGlobal) {
                miClient.getSession().showView(SModConsts.HRS_ACC_DED, SModSysConsts.HRSS_TP_ACC_GBL, null);
            }
            else if (item == jmiCfgBkkDeductionDepartament) {
                miClient.getSession().showView(SModConsts.HRS_ACC_DED, SModSysConsts.HRSS_TP_ACC_DEP, null);
            }
            else if (item == jmiCfgBkkDeductionEmployee) {
                miClient.getSession().showView(SModConsts.HRS_ACC_DED, SModSysConsts.HRSS_TP_ACC_EMP, null);
            }
            else if (item == jmiCfgConfig) {
                miClient.getSession().showView(SModConsts.HRS_CFG, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatEmployee) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showView(SDataConstants.BPSX_BP_EMP);
            }
            else if (item == jmiCatEmployeeRelatives) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showView(SDataConstants.BPSX_BP_EMP_REL);
            }
            else if (item == jmiCatEmployeeIntegral) {
                showPanelQueryIntegralEmployee(SModConsts.HRSX_EMP_INT);
            }
            else if (item == jmiCatEmployeeHireLog) {
                miClient.getSession().showView(SModConsts.HRS_EMP_LOG_HIRE, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatEmployeeWageLog) {
                miClient.getSession().showView(SModConsts.HRS_EMP_LOG_WAGE, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatEmployeeSscBaseLog) {
                miClient.getSession().showView(SModConsts.HRS_EMP_LOG_SAL_SSC, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatEmployeeSscBaseUpdate) {
               new SDialogPayrollReceiptSsc((SGuiClient) miClient, "Actualización de salario base cotización").setVisible(true);
            }
            else if (item == jmiCatEarnings) {
                miClient.getSession().showView(SModConsts.HRS_EAR, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatDeductions) {
                miClient.getSession().showView(SModConsts.HRS_DED, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatDeparment) {
                miClient.getSession().showView(SModConsts.HRSU_DEP, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatPosition) {
                miClient.getSession().showView(SModConsts.HRSU_POS, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatShift) {
                miClient.getSession().showView(SModConsts.HRSU_SHT, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatEmployeeType) {
                miClient.getSession().showView(SModConsts.HRSU_TP_EMP, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatWorkerType) {
                miClient.getSession().showView(SModConsts.HRSU_TP_WRK, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatEmployeeDismissType) {
                miClient.getSession().showView(SModConsts.HRSU_TP_EMP_DIS, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatMwzType) {
                miClient.getSession().showView(SModConsts.HRSU_TP_MWZ, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatMwzTypeWage) {
                miClient.getSession().showView(SModConsts.HRS_MWZ_WAGE, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatUma) {
                miClient.getSession().showView(SModConsts.HRS_UMA, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatUmi) {
                miClient.getSession().showView(SModConsts.HRS_UMI, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatWorkerTypeSalary) {
                miClient.getSession().showView(SModConsts.HRS_WRK_SAL, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatLoanTypeAdjustment) {
                miClient.getSession().showView(SModConsts.HRS_TP_LOAN_ADJ, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatAbsenceType) {
                miClient.getSession().showView(SModConsts.HRSU_TP_ABS, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatAbsenceClass) {
                miClient.getSession().showView(SModConsts.HRSU_CL_ABS, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiPayAbsence) {
                miClient.getSession().showView(SModConsts.HRS_ABS, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiPayPayrollWeekly) {
                miClient.getSession().showView(SModConsts.HRS_PAY, SModSysConsts.HRSS_TP_PAY_WEE, null);
            }
            else if (item == jmiPayPayrollWeeklyRec) {
                miClient.getSession().showView(SModConsts.HRS_PAY_RCP, SModSysConsts.HRSS_TP_PAY_WEE, null);
            }
            else if (item == jmiPayPayrollFortnightly) {
                miClient.getSession().showView(SModConsts.HRS_PAY, SModSysConsts.HRSS_TP_PAY_FOR, null);
            }
            else if (item == jmiPayPayrollFortnightlyRowRec) {
                miClient.getSession().showView(SModConsts.HRS_PAY_RCP, SModSysConsts.HRSS_TP_PAY_FOR, null);
            }
            else if (item == jmiPayPayrollBkkRecord) {
                miClient.getSession().showView(SModConsts.HRSX_PAY_REC, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiPayCfdiPayroll) {
                miClient.getSession().showView(SModConsts.HRS_SIE_PAY, SModConsts.VIEW_SC_SUM, new SGuiParams(SCfdConsts.CFDI_PAYROLL_VER_CUR));
            }
            else if (item == jmiPayCfdiPayrollRec) {
                miClient.getSession().showView(SModConsts.HRS_SIE_PAY, SModConsts.VIEW_SC_DET, new SGuiParams(SCfdConsts.CFDI_PAYROLL_VER_CUR));
            }
            else if (item == jmiPayCfdiStampSign) {
                showView(SDataConstants.TRN_CFD, SDataConstants.TRNX_STAMP_SIGN, SCfdConsts.CFDI_PAYROLL_VER_CUR);
            }
            else if (item == jmiPayCfdiStampSignPending) {
                showView(SDataConstants.TRN_CFD, SDataConstants.TRNX_STAMP_SIGN_PEND, SCfdConsts.CFDI_PAYROLL_VER_CUR);
            }
            else if (item == jmiPayCfdiSendingLog) {
                showView(SDataConstants.TRN_CFD_SND_LOG, SCfdConsts.CFDI_PAYROLL_VER_CUR);
            }
            else if (item == jmiPayAutoEarningsGlobal) {
                miClient.getSession().showView(SModConsts.HRSX_AUT_EAR, SModSysConsts.HRS_AUT_GBL, null);
            }
            else if (item == jmiPayAutoEarningsByEmployee) {
                miClient.getSession().showView(SModConsts.HRSX_AUT_EAR, SModSysConsts.HRS_AUT_EMP, null);
            }
            else if (item == jmiPayAutoEarningsByEmployeeDet) {
                miClient.getSession().showView(SModConsts.HRSX_AUT_EAR, SModSysConsts.HRS_AUT_EMP, new SGuiParams(SUtilConsts.QRY_DET));
            }
            else if (item == jmiPayAutoDeductionsGlobal) {
                miClient.getSession().showView(SModConsts.HRSX_AUT_DED, SModSysConsts.HRS_AUT_GBL, null);
            }
            else if (item == jmiPayAutoDeductionsByEmployee) {
                miClient.getSession().showView(SModConsts.HRSX_AUT_DED, SModSysConsts.HRS_AUT_EMP, null);
            }
            else if (item == jmiPayAutoDeductionsByEmployeeDet) {
                miClient.getSession().showView(SModConsts.HRSX_AUT_DED, SModSysConsts.HRS_AUT_EMP, new SGuiParams(SUtilConsts.QRY_DET));
            }
            else if (item == jmiPayCalculatedAmountMonth) {
                new SFormCalculateNetGrossAmount((SGuiClient) miClient, SHrsConsts.CAL_NET_AMT_TYPE, "Calcular ingreso mensual").setFormVisible(true);
            }
            else if (item == jmiPayCalculatedEstimateIncomeTax) {
                new SDialogCalculateIncomeTax((SGuiClient) miClient, "Calcular impuesto acumulado").setFormVisible(true);
            }
            else if (item == jmiBenefitBenefitVac) {
                miClient.getSession().showView(SModConsts.HRSX_BEN_MOV, SModSysConsts.HRSS_TP_BEN_VAC, null);
            }
            else if (item == jmiBenefitBenefitBonVac) {
                miClient.getSession().showView(SModConsts.HRSX_BEN_MOV, SModSysConsts.HRSS_TP_BEN_VAC_BON, null);
            }
            else if (item == jmiBenefitBenefitBonAnn) {
                miClient.getSession().showView(SModConsts.HRSX_BEN_MOV, SModSysConsts.HRSS_TP_BEN_ANN_BON, null);
            }
            else if (item == jmiBenefitBenefitAdjustmentEarning) {
                miClient.getSession().showView(SModConsts.HRS_PAY_RCP_EAR, SModConsts.HRS_BEN, null);
            }
            else if (item == jmiBenefitLoan) {
                miClient.getSession().showView(SModConsts.HRS_LOAN, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiBenefitLoanAdjustmentEarning) {
                miClient.getSession().showView(SModConsts.HRS_PAY_RCP_EAR, SModConsts.HRS_LOAN, null);
            }
            else if (item == jmiBenefitLoanAdjustmentDeduction) {
                miClient.getSession().showView(SModConsts.HRS_PAY_RCP_DED, SModConsts.HRS_LOAN, null);
            }
            else if (item == jmiBenefitAdvanceSettlement) {
                miClient.getSession().showView(SModConsts.HRS_ADV_SET, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiImpFormerPayroll) {
                showView(SDataConstants.HRS_SIE_PAY);
            }
            else if (item == jmiImpFormerPayrollEmp) {
                showView(SDataConstants.HRS_SIE_PAY_EMP);
            }
            else if (item == jmiImpImport) {
                if (moDialogFormerPayrollImport == null) {
                    moDialogFormerPayrollImport = new SDialogFormerPayrollImport(miClient);
                }
                moDialogFormerPayrollImport.resetForm();
                moDialogFormerPayrollImport.setVisible(true);
                //showView(SDataConstants.TRN_DNC_DPS_DNS, SDataConstantsSys.TRNS_CT_DPS_SAL);
            }
            else if (item == jmiImpCfdiPayroll) {
                miClient.getSession().showView(SModConsts.HRS_SIE_PAY, SModConsts.VIEW_SC_SUM, new SGuiParams(SCfdConsts.CFDI_PAYROLL_VER_OLD));
            }
            else if (item == jmiImpCfdiPayrollDetail) {
                miClient.getSession().showView(SModConsts.HRS_SIE_PAY, SModConsts.VIEW_SC_DET, new SGuiParams(SCfdConsts.CFDI_PAYROLL_VER_OLD));
            }
            else if (item == jmiImpCfdiStampSign) {
                showView(SDataConstants.TRN_CFD, SDataConstants.TRNX_STAMP_SIGN, SCfdConsts.CFDI_PAYROLL_VER_OLD);
            }
            else if (item == jmiImpCfdiStampSignPending) {
                showView(SDataConstants.TRN_CFD, SDataConstants.TRNX_STAMP_SIGN_PEND, SCfdConsts.CFDI_PAYROLL_VER_OLD);
            }
            else if (item == jmiImpCfdiSendingLog) {
                showView(SDataConstants.TRN_CFD_SND_LOG, SCfdConsts.CFDI_PAYROLL_VER_OLD);
            }
            else if (item == jmiRepPayrollEarningsDeductions) {
                new SDialogRepHrsEarningDeduction((SGuiClient) miClient, "Percepciones y deducciones por periodo").setFormVisible(true);
            }
            else if (item == jmiRepPayrollEarnings) {
                new SDialogRepHrsEarDed((SGuiClient) miClient, SModConsts.HRSR_EAR, "Percepciones por periodo").setFormVisible(true);
            }
            else if (item == jmiRepPayrollDeductions) {
                new SDialogRepHrsEarDed((SGuiClient) miClient, SModConsts.HRSR_DED, "Deducciones por periodo").setFormVisible(true);
            }
            else if (item == jmiRepPayrollEarningsByEmployee) {
                new SDialogRepHrsEarDed((SGuiClient) miClient, SModConsts.HRSR_EAR_EMP, "Percepciones por empleado por periodo").setFormVisible(true);
            }
            else if (item == jmiRepPayrollDeductionsByEmployee) {
                new SDialogRepHrsEarDed((SGuiClient) miClient, SModConsts.HRSR_DED_EMP, "Deducciones por empleado por periodo").setFormVisible(true);
            }
            else if (item == jmiRepPayrollTax) {
                new SDialogRepHrsPayrollTax((SGuiClient) miClient, "Impuesto sobre nóminas").setFormVisible(true);
            }
            else if (item == jmiRepPayrollAux) {
               new SDialogRepHrsAuxPayroll((SGuiClient) miClient, "Auxiliares de nóminas").setFormVisible(true);
            }
            else if (item == jmiRepPayrollWageSalaryFileCsv) {
                new SDialogRepHrsPayrollWageSalaryFileCsv((SGuiClient) miClient, "Archivo CSV para declaración informativa de sueldos y salarios").setFormVisible(true);
            }
            else if (item == jmiRepPayrollEarDedFileCsv) {
                new SDialogRepHrsEarningsDeductionsFileCsv((SGuiClient) miClient, "Archivo CSV de percepciones y deducciones en el ejercicio").setFormVisible(true);
            }
            else if (item == jmiRepVacationsFileCsv) {
                new SDialogRepVacationsFileCsv((SGuiClient) miClient, "Archivo CSV de vacaciones pendientes").setFormVisible(true);
            }
            else if (item == jmiRepEmployeeActiveByPeriod) {
                new SDialogRepHrsActiveEmployees((SGuiClient) miClient, "Reporte de empleados activos por periodo").setFormVisible(true);
            }
        }
    }
}
