/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.cfd.SCfdConsts;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STableTabComponent;
import erp.lib.table.STableTabInterface;
import erp.mhrs.form.SDialogFormerPayrollImport;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.form.SDialogRepHrsAux;
import erp.mod.hrs.form.SDialogRepHrsPayrollTax;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import sa.lib.SLibConsts;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Sergio Flores
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
    private javax.swing.JMenuItem jmiCfgMwzType;
    private javax.swing.JMenuItem jmiCfgWorkerTypeSalary;
    private javax.swing.JMenuItem jmiCfgMwzTypeWage;
    private javax.swing.JMenuItem jmiCfgFirstDayYear;
    private javax.swing.JMenuItem jmiCfgHoliday;
    private javax.swing.JMenuItem jmiCfgWorkingDaySettings;
    private javax.swing.JMenuItem jmiCfgConfig;
    private javax.swing.JMenu jmCfgContabEarning;
    private javax.swing.JMenuItem jmiCfgContabEarningGlobal;
    private javax.swing.JMenuItem jmiCfgContabEarningDepartament;
    private javax.swing.JMenuItem jmiCfgContabEarningEmployee;
    private javax.swing.JMenu jmCfgContabDeduction;
    private javax.swing.JMenuItem jmiCfgContabDeductionGlobal;
    private javax.swing.JMenuItem jmiCfgContabDeductionDepartament;
    private javax.swing.JMenuItem jmiCfgContabDeductionEmployee;
    private javax.swing.JMenu jmCat;
    private javax.swing.JMenuItem jmiCatEmployee;
    private javax.swing.JMenuItem jmiCatEmployeeIntegral;
    private javax.swing.JMenuItem jmiCatEmployeeHireLog;
    private javax.swing.JMenuItem jmiCatEmployeeWageLog;
    private javax.swing.JMenuItem jmiCatEmployeeWageSscBaseLog;
    private javax.swing.JMenuItem jmiCatDeparment;
    private javax.swing.JMenuItem jmiCatPosition;
    private javax.swing.JMenuItem jmiCatShift;
    private javax.swing.JMenuItem jmiCatEarnings;
    private javax.swing.JMenuItem jmiCatDeductions;
    private javax.swing.JMenuItem jmiCatEmployeeType;
    private javax.swing.JMenuItem jmiCatWorkerType;
    private javax.swing.JMenuItem jmiCatEmployeeDismissType;
    private javax.swing.JMenuItem jmiCatAbsenceType;
    private javax.swing.JMenuItem jmiCatAbsenceClass;
    private javax.swing.JMenuItem jmiCatAbsence;
    private javax.swing.JMenu jmPay;
    private javax.swing.JMenuItem jmiPayPayrollWeekly;
    private javax.swing.JMenuItem jmiPayPayrollWeeklyRow;
    private javax.swing.JMenuItem jmiPayPayrollFortnightly;
    private javax.swing.JMenuItem jmiPayPayrollFortnightlyRow;
    private javax.swing.JMenuItem jmiPayPayrollRecord;
    private javax.swing.JMenuItem jmiPayCfdiPayroll;
    private javax.swing.JMenuItem jmiPayCfdiPayrollDetail;
    private javax.swing.JMenu jmPayCfdi;
    //private javax.swing.JMenuItem jmiPayCfdiStampAvailable;
    private javax.swing.JMenuItem jmiPayCfdiStampSign;
    private javax.swing.JMenuItem jmiPayCfdiStampSignPending;
    private javax.swing.JMenuItem jmiPayCfdiSendingLog;
    private javax.swing.JMenuItem jmiPayAutomaticEarningsGlobal;
    private javax.swing.JMenuItem jmiPayAutomaticEarningsByEmployee;
    private javax.swing.JMenuItem jmiPayAutomaticDeductionsGlobal;
    private javax.swing.JMenuItem jmiPayAutomaticDeductionsByEmployee;
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
    private javax.swing.JMenuItem jmiImpCfdiStampAvailable;
    private javax.swing.JMenuItem jmiImpCfdiStampSign;
    private javax.swing.JMenuItem jmiImpCfdiStampSignPending;
    private javax.swing.JMenuItem jmiImpCfdiSendingLog;
    private javax.swing.JMenu jmRep;
    private javax.swing.JMenuItem jmiRepPayrollEarningsAux;
    private javax.swing.JMenuItem jmiRepPayrollEarningsByEmployeeAux;
    private javax.swing.JMenuItem jmiRepPayrollDeductionsAux;
    private javax.swing.JMenuItem jmiRepPayrollDeductionsByEmployeeAux;
    private javax.swing.JMenuItem jmiRepPayrollTax;

    private erp.mhrs.form.SDialogFormerPayrollImport moDialogFormerPayrollImport;

    public SGuiModuleHrs(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_HRS);
        initComponents();
    }

    private void initComponents() {
        jmCfg = new JMenu("Configuración");
        jmiCfgTaxTable = new JMenuItem("Tablas de impuesto");
        jmiCfgTaxTableRow = new JMenuItem("Tablas de impuesto a detalle");
        jmiCfgTaxSubsidyTable = new JMenuItem("Tablas de subsidio de impuesto");
        jmiCfgTaxSubsidyTableRow = new JMenuItem("Tablas de subsidio de impuesto a detalle");
        jmiCfgSsContributionTable = new JMenuItem("Tablas de SS");
        jmiCfgSsContributionTableRow = new JMenuItem("Tablas de SS a detalle");
        jmiCfgBenefitTable = new JMenuItem("Tablas de prestaciones");
        jmiCfgBenefitTableRow = new JMenuItem("Tablas de prestaciones a detalle");
        jmiCfgMwzType = new JMenuItem("Áreas geográficas");
        jmiCfgMwzTypeWage = new JMenuItem("Salarios mínimos por área geográfica");
        jmiCfgWorkerTypeSalary = new JMenuItem("Salarios por tipo de obrero");
        jmiCfgFirstDayYear = new JMenuItem("Primer día del año");
        jmiCfgHoliday = new JMenuItem("Días feriados");
        jmiCfgWorkingDaySettings = new JMenuItem("Días laborables");
        jmiCfgConfig = new JMenuItem("Configuración");
        jmCfgContabEarning = new JMenu("Configuración contable de percepciones");
        jmiCfgContabEarningGlobal = new JMenuItem("Globales");
        jmiCfgContabEarningDepartament = new JMenuItem("Por departamento");
        jmiCfgContabEarningEmployee = new JMenuItem("Por empleado");
        jmCfgContabDeduction = new JMenu("Configuración contable de deducciones");
        jmiCfgContabDeductionGlobal = new JMenuItem("Globales");
        jmiCfgContabDeductionDepartament = new JMenuItem("Por departamento");
        jmiCfgContabDeductionEmployee = new JMenuItem("Por empleado");

        jmCfg.add(jmiCfgTaxTable);
        jmCfg.add(jmiCfgTaxTableRow);
        jmCfg.add(jmiCfgTaxSubsidyTable);
        jmCfg.add(jmiCfgTaxSubsidyTableRow);
        jmCfg.add(jmiCfgSsContributionTable);
        jmCfg.add(jmiCfgSsContributionTableRow);
        jmCfg.add(jmiCfgBenefitTable);
        jmCfg.add(jmiCfgBenefitTableRow);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgMwzType);
        jmCfg.add(jmiCfgMwzTypeWage);
        jmCfg.add(jmiCfgWorkerTypeSalary);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgFirstDayYear);
        jmCfg.add(jmiCfgHoliday);
        jmCfg.add(jmiCfgWorkingDaySettings);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgConfig);
        jmCfg.addSeparator();
        jmCfgContabEarning.add(jmiCfgContabEarningGlobal);
        jmCfgContabEarning.add(jmiCfgContabEarningDepartament);
        jmCfgContabEarning.add(jmiCfgContabEarningEmployee);
        jmCfg.add(jmCfgContabEarning);
        jmCfgContabDeduction.add(jmiCfgContabDeductionGlobal);
        jmCfgContabDeduction.add(jmiCfgContabDeductionDepartament);
        jmCfgContabDeduction.add(jmiCfgContabDeductionEmployee);
        jmCfg.add(jmCfgContabDeduction);

        jmCat = new JMenu("Catálogos");
        jmiCatEmployee = new JMenuItem("Empleados");
        jmiCatEmployeeIntegral = new JMenuItem("Consulta integral de empleados");
        jmiCatEmployeeHireLog = new JMenuItem("Historial de altas y bajas de empleados");
        jmiCatEmployeeWageLog = new JMenuItem("Historial de sueldos y salarios de empleados");
        jmiCatEmployeeWageSscBaseLog = new JMenuItem("Historial de salarios base de cotización de empleados");
        jmiCatDeparment = new JMenuItem("Departamentos");
        jmiCatPosition = new JMenuItem("Puestos");
        jmiCatShift = new JMenuItem("Turnos");
        jmiCatEarnings = new JMenuItem("Percepciones");
        jmiCatDeductions = new JMenuItem("Deducciones");
        jmiCatEmployeeType = new JMenuItem("Tipos de empleado");
        jmiCatWorkerType = new JMenuItem("Tipos de obrero");
        jmiCatEmployeeDismissType = new JMenuItem("Tipos de baja de empleado");
        jmiCatAbsenceType = new JMenuItem("Tipos de incidencia");
        jmiCatAbsenceClass = new JMenuItem("Clases de incidencia");
        jmiCatAbsence = new JMenuItem("Incidencias");

        jmCat.add(jmiCatEmployee);
        jmCat.add(jmiCatEmployeeIntegral);
        jmCat.add(jmiCatEmployeeHireLog);
        jmCat.add(jmiCatEmployeeWageLog);
        jmCat.add(jmiCatEmployeeWageSscBaseLog);
        jmCat.addSeparator();
        jmCat.add(jmiCatDeparment);
        jmCat.add(jmiCatPosition);
        jmCat.add(jmiCatShift);
        jmCat.addSeparator();
        jmCat.add(jmiCatEarnings);
        jmCat.add(jmiCatDeductions);
        jmCat.addSeparator();
        jmCat.add(jmiCatEmployeeType);
        jmCat.add(jmiCatWorkerType);
        jmCat.add(jmiCatEmployeeDismissType);
        jmCat.addSeparator();
        jmCat.add(jmiCatAbsenceType);
        jmCat.add(jmiCatAbsenceClass);
        jmCat.addSeparator();
        jmCat.add(jmiCatAbsence);

        jmPay = new JMenu("Nóminas");
        jmiPayPayrollWeekly = new JMenuItem("Nóminas semanales");
        jmiPayPayrollWeeklyRow = new JMenuItem("Recibos de nóminas semanales");
        jmiPayPayrollFortnightly = new JMenuItem("Nóminas quincenales");
        jmiPayPayrollFortnightlyRow = new JMenuItem("Recibos de nóminas quincenales");
        jmiPayCfdiPayroll = new JMenuItem("CFDI de nóminas");
        jmiPayCfdiPayrollDetail = new JMenuItem("CFDI de recibos de nóminas");
        jmiPayPayrollRecord = new JMenuItem("Recibos de nóminas vs. pólizas contables");
        jmPayCfdi = new JMenu("Comprobantes fiscales digitales");
        //jmiPayCfdiStampAvailable = new JMenuItem("Timbres disponibles");
        jmiPayCfdiStampSign = new JMenuItem("Comprobantes timbrados");
        jmiPayCfdiStampSignPending = new JMenuItem("Comprobantes por timbrar");
        jmiPayCfdiSendingLog = new JMenuItem("Bitácora de envíos");
        jmiPayAutomaticEarningsGlobal = new JMenuItem("Percepciones automáticas globales");
        jmiPayAutomaticEarningsByEmployee = new JMenuItem("Percepciones automáticas por empleado");
        jmiPayAutomaticDeductionsGlobal = new JMenuItem("Deducciones automáticas globales");
        jmiPayAutomaticDeductionsByEmployee = new JMenuItem("Deducciones automáticas por empleado");

        jmPay.add(jmiPayPayrollWeekly);
        jmPay.add(jmiPayPayrollWeeklyRow);
        jmPay.addSeparator();
        jmPay.add(jmiPayPayrollFortnightly);
        jmPay.add(jmiPayPayrollFortnightlyRow);
        jmPay.addSeparator();
        jmPay.add(jmiPayPayrollRecord);
        jmPay.add(jmiPayCfdiPayroll);
        jmPay.add(jmiPayCfdiPayrollDetail);
        //jmPayCfdi.add(jmiPayCfdiStampAvailable);
        jmPayCfdi.add(jmiPayCfdiStampSign);
        jmPayCfdi.add(jmiPayCfdiStampSignPending);
        jmPayCfdi.addSeparator();
        jmPayCfdi.add(jmiPayCfdiSendingLog);
        jmPay.add(jmPayCfdi);
        jmPay.addSeparator();
        jmPay.add(jmiPayAutomaticEarningsGlobal);
        jmPay.add(jmiPayAutomaticEarningsByEmployee);
        jmPay.add(jmiPayAutomaticDeductionsGlobal);
        jmPay.add(jmiPayAutomaticDeductionsByEmployee);
        
        jmBenefit = new JMenu("Prestaciones");
        jmiBenefitBenefitVac = new JMenuItem("Control de vacaciones");
        jmiBenefitBenefitBonVac = new JMenuItem("Control de prima vacacional");
        jmiBenefitBenefitBonAnn = new JMenuItem("Control de gratificación anual");
        jmiBenefitBenefitAdjustmentEarning = new JMenuItem("Incremento de prestaciones");
        jmiBenefitLoan = new JMenuItem("Control de créditos y préstamos");
        jmiBenefitLoanAdjustmentEarning = new JMenuItem("Incremento de créditos y préstamos");
        jmiBenefitLoanAdjustmentDeduction = new JMenuItem("Decremento de créditos y préstamos");
        jmiBenefitAdvanceSettlement = new JMenuItem("Control de adelantos de liquidación");
        
        jmBenefit.add(jmiBenefitBenefitVac);
        jmBenefit.add(jmiBenefitBenefitBonVac);
        jmBenefit.add(jmiBenefitBenefitBonAnn);
        jmBenefit.add(jmiBenefitBenefitAdjustmentEarning);
        jmBenefit.addSeparator();
        jmBenefit.add(jmiBenefitLoan);
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
        jmiImpCfdiStampAvailable = new JMenuItem("Timbres disponibles");
        jmiImpCfdiStampSign = new JMenuItem("Comprobantes timbrados");
        jmiImpCfdiStampSignPending = new JMenuItem("Comprobantes por timbrar");
        jmiImpCfdiSendingLog = new JMenuItem("Bitácora de envíos");
        jmImpCfdi.add(jmiImpCfdiStampAvailable);
        jmImpCfdi.add(jmiImpCfdiStampSign);
        jmImpCfdi.add(jmiImpCfdiStampSignPending);
        jmImpCfdi.addSeparator();
        jmImpCfdi.add(jmiImpCfdiSendingLog);
        jmImp.add(jmImpCfdi);

        jmRep = new JMenu("Reportes");
        jmiRepPayrollEarningsAux = new JMenuItem("Auxiliares de nóminas de percepciones");
        jmiRepPayrollEarningsByEmployeeAux = new JMenuItem("Auxiliares de nóminas de percepciones por empleado");
        jmiRepPayrollDeductionsAux = new JMenuItem("Auxiliares de nóminas de deducciones");
        jmiRepPayrollDeductionsByEmployeeAux = new JMenuItem("Auxiliares de nóminas de deducciones por empleado");
        jmiRepPayrollTax = new JMenuItem("Impuesto sobre nóminas");
        jmRep.add(jmiRepPayrollEarningsAux);
        jmRep.add(jmiRepPayrollEarningsByEmployeeAux);
        jmRep.add(jmiRepPayrollDeductionsAux);
        jmRep.add(jmiRepPayrollDeductionsByEmployeeAux);
        jmRep.add(jmiRepPayrollTax);

        jmiCfgTaxTable.addActionListener(this);
        jmiCfgTaxTableRow.addActionListener(this);
        jmiCfgTaxSubsidyTable.addActionListener(this);
        jmiCfgTaxSubsidyTableRow.addActionListener(this);
        jmiCfgSsContributionTable.addActionListener(this);
        jmiCfgSsContributionTableRow.addActionListener(this);
        jmiCfgBenefitTable.addActionListener(this);
        jmiCfgBenefitTableRow.addActionListener(this);
        jmiCfgMwzType.addActionListener(this);
        jmiCfgMwzTypeWage.addActionListener(this);
        jmiCfgWorkerTypeSalary.addActionListener(this);
        jmiCfgFirstDayYear.addActionListener(this);
        jmiCfgHoliday.addActionListener(this);
        jmiCfgWorkingDaySettings.addActionListener(this);
        jmiCfgConfig.addActionListener(this);
        jmiCfgContabEarningGlobal.addActionListener(this);
        jmiCfgContabEarningDepartament.addActionListener(this);
        jmiCfgContabEarningEmployee.addActionListener(this);
        jmiCfgContabDeductionGlobal.addActionListener(this);
        jmiCfgContabDeductionDepartament.addActionListener(this);
        jmiCfgContabDeductionEmployee.addActionListener(this);
        jmiCatEmployee.addActionListener(this);
        jmiCatEmployeeIntegral.addActionListener(this);
        jmiCatEmployeeHireLog.addActionListener(this);
        jmiCatEmployeeWageLog.addActionListener(this);
        jmiCatEmployeeWageSscBaseLog.addActionListener(this);
        jmiCatEarnings.addActionListener(this);
        jmiCatDeductions.addActionListener(this);
        jmiCatDeparment.addActionListener(this);
        jmiCatPosition.addActionListener(this);
        jmiCatShift.addActionListener(this);
        jmiCatEmployeeType.addActionListener(this);
        jmiCatWorkerType.addActionListener(this);
        jmiCatEmployeeDismissType.addActionListener(this);
        jmiCatAbsenceType.addActionListener(this);
        jmiCatAbsenceClass.addActionListener(this);
        jmiCatAbsence.addActionListener(this);
        jmiPayPayrollWeekly.addActionListener(this);
        jmiPayPayrollWeeklyRow.addActionListener(this);
        jmiPayPayrollFortnightly.addActionListener(this);
        jmiPayPayrollFortnightlyRow.addActionListener(this);
        jmiPayPayrollRecord.addActionListener(this);
        jmiPayCfdiPayroll.addActionListener(this);
        jmiPayCfdiPayrollDetail.addActionListener(this);
        //jmiPayCfdiStampAvailable.addActionListener(this);
        jmiPayCfdiStampSign.addActionListener(this);
        jmiPayCfdiStampSignPending.addActionListener(this);
        jmiPayCfdiSendingLog.addActionListener(this);
        jmiPayAutomaticEarningsGlobal.addActionListener(this);
        jmiPayAutomaticEarningsByEmployee.addActionListener(this);
        jmiPayAutomaticDeductionsGlobal.addActionListener(this);
        jmiPayAutomaticDeductionsByEmployee.addActionListener(this);
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
        jmiImpCfdiStampAvailable.addActionListener(this);
        jmiImpCfdiStampSign.addActionListener(this);
        jmiImpCfdiStampSignPending.addActionListener(this);
        jmiImpCfdiSendingLog.addActionListener(this);
        jmiRepPayrollEarningsAux.addActionListener(this);
        jmiRepPayrollDeductionsAux.addActionListener(this);
        jmiRepPayrollEarningsByEmployeeAux.addActionListener(this);
        jmiRepPayrollDeductionsByEmployeeAux.addActionListener(this);
        jmiRepPayrollTax.addActionListener(this);

        jmCfg.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CFG).HasRight);
        jmiCfgTaxTable.setEnabled(true);
        jmiCfgTaxTableRow.setEnabled(true);
        jmiCfgTaxSubsidyTable.setEnabled(true);
        jmiCfgTaxSubsidyTableRow.setEnabled(true);
        jmiCfgSsContributionTable.setEnabled(true);
        jmiCfgSsContributionTableRow.setEnabled(true);
        jmiCfgBenefitTable.setEnabled(true);
        jmiCfgBenefitTableRow.setEnabled(true);
        jmiCfgMwzTypeWage.setEnabled(true);
        jmiCfgWorkerTypeSalary.setEnabled(true);
        jmiCfgFirstDayYear.setEnabled(true);
        jmiCfgHoliday.setEnabled(true);
        jmiCfgWorkingDaySettings.setEnabled(true);
        jmiCfgConfig.setEnabled(true);
        jmCfgContabEarning.setEnabled(true);
        jmiCfgContabEarningGlobal.setEnabled(true);
        jmiCfgContabEarningDepartament.setEnabled(true);
        jmiCfgContabEarningEmployee.setEnabled(true);
        jmCfgContabDeduction.setEnabled(true);
        jmiCfgContabDeductionGlobal.setEnabled(true);
        jmiCfgContabDeductionDepartament.setEnabled(true);
        jmiCfgContabDeductionEmployee.setEnabled(true);
        jmCat.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT).HasRight ||
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP).HasRight ||
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP_WAGE).HasRight);
        jmiCatEmployee.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP).HasRight);
        jmiCatEmployeeIntegral.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP).HasRight);
        jmiCatEmployeeHireLog.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP_WAGE).HasRight);
        jmiCatEmployeeWageLog.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP_WAGE).HasRight);
        jmiCatEmployeeWageSscBaseLog.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP_WAGE).HasRight);
        jmiCatEarnings.setEnabled(true);
        jmiCatDeductions.setEnabled(true);
        jmiCatDeparment.setEnabled(true);
        jmiCatPosition.setEnabled(true);
        jmiCatShift.setEnabled(true);
        jmiCatEmployeeType.setEnabled(true);
        jmiCatWorkerType.setEnabled(true);
        jmiCatEmployeeDismissType.setEnabled(true);
        jmiCfgMwzType.setEnabled(true);
        jmiCatAbsenceType.setEnabled(true);
        jmiCatAbsenceClass.setEnabled(true);
        jmiCatAbsence.setEnabled(true);
        jmPay.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_PAY).HasRight);
        jmiPayPayrollWeekly.setEnabled(true);
        jmiPayPayrollWeeklyRow.setEnabled(true);
        jmiPayPayrollFortnightly.setEnabled(true);
        jmiPayPayrollFortnightlyRow.setEnabled(true);
        jmiPayPayrollRecord.setEnabled(true);
        jmiPayCfdiPayroll.setEnabled(true);
        jmiPayCfdiPayrollDetail.setEnabled(true);
        jmiPayAutomaticEarningsGlobal.setEnabled(true);
        jmiPayAutomaticEarningsByEmployee.setEnabled(true);
        jmiPayAutomaticDeductionsGlobal.setEnabled(true);
        jmiPayAutomaticDeductionsByEmployee.setEnabled(true);
        jmiPayCfdiStampSign.setEnabled(true);
        jmiPayCfdiStampSignPending.setEnabled(true);
        jmiPayCfdiSendingLog.setEnabled(true);
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
        jmiRepPayrollEarningsAux.setEnabled(true);
        jmiRepPayrollDeductionsAux.setEnabled(true);
        jmiRepPayrollEarningsByEmployeeAux.setEnabled(true);
        jmiRepPayrollDeductionsByEmployeeAux.setEnabled(true);
        jmiRepPayrollTax.setEnabled(true);
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
                case SDataConstants.HRS_FORMER_PAYR_EMP:
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
                case SDataConstants.HRS_FORMER_PAYR:
                    oViewClass = erp.mhrs.view.SViewFormerPayroll.class;
                    sViewTitle = "Nóminas importadas";
                    break;
                case SDataConstants.HRS_FORMER_PAYR_EMP:
                    oViewClass = erp.mhrs.view.SViewFormerPayrollEmp.class;
                    sViewTitle = "Nóminas importadas vs. pólizas contables";
                    break;
                case SDataConstants.TRN_CFD:
                    oViewClass = erp.mtrn.view.SViewCfdXml.class;
                    switch (auxType01) {
                        case SDataConstants.TRNX_STAMP_SIGN:
                            sViewTitle = "Nóminas - comprobantes timbrados";
                            break;
                        case SDataConstants.TRNX_STAMP_SIGN_PEND:
                            sViewTitle = "Nóminas - comprobantes x timbrar";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                case SDataConstants.TRN_CFD_SND_LOG:
                    oViewClass = erp.mtrn.view.SViewDpsSendingLog.class;
                    sViewTitle = "Nóminas - bitácora envíos";
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
            else if (item == jmiCfgMwzType) {
                miClient.getSession().showView(SModConsts.HRSU_TP_MWZ, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgMwzTypeWage) {
                miClient.getSession().showView(SModConsts.HRS_MWZ_WAGE, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgWorkerTypeSalary) {
                miClient.getSession().showView(SModConsts.HRS_WRK_SAL, SLibConsts.UNDEFINED, null);
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
            else if (item == jmiCfgConfig) {
                miClient.getSession().showView(SModConsts.HRS_CFG, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgContabEarningGlobal) {
                miClient.getSession().showView(SModConsts.HRS_ACC_EAR, SModSysConsts.HRSS_TP_ACC_GBL, null);
            }
            else if (item == jmiCfgContabEarningDepartament) {
                miClient.getSession().showView(SModConsts.HRS_ACC_EAR, SModSysConsts.HRSS_TP_ACC_DEP, null);
            }
            else if (item == jmiCfgContabEarningEmployee) {
                miClient.getSession().showView(SModConsts.HRS_ACC_EAR, SModSysConsts.HRSS_TP_ACC_EMP, null);
            }
            else if (item == jmiCfgContabDeductionGlobal) {
                miClient.getSession().showView(SModConsts.HRS_ACC_DED, SModSysConsts.HRSS_TP_ACC_GBL, null);
            }
            else if (item == jmiCfgContabDeductionDepartament) {
                miClient.getSession().showView(SModConsts.HRS_ACC_DED, SModSysConsts.HRSS_TP_ACC_DEP, null);
            }
            else if (item == jmiCfgContabDeductionEmployee) {
                miClient.getSession().showView(SModConsts.HRS_ACC_DED, SModSysConsts.HRSS_TP_ACC_EMP, null);
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
            else if (item == jmiCatAbsenceType) {
                miClient.getSession().showView(SModConsts.HRSU_TP_ABS, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatAbsenceClass) {
                miClient.getSession().showView(SModConsts.HRSU_CL_ABS, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatAbsence) {
                miClient.getSession().showView(SModConsts.HRS_ABS, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiPayPayrollWeekly) {
                miClient.getSession().showView(SModConsts.HRS_PAY, SModSysConsts.HRSS_TP_PAY_WEE, null);
            }
            else if (item == jmiPayPayrollWeeklyRow) {
                miClient.getSession().showView(SModConsts.HRS_PAY_RCP, SModSysConsts.HRSS_TP_PAY_WEE, null);
            }
            else if (item == jmiPayPayrollFortnightly) {
                miClient.getSession().showView(SModConsts.HRS_PAY, SModSysConsts.HRSS_TP_PAY_FOR, null);
            }
            else if (item == jmiPayPayrollFortnightlyRow) {
                miClient.getSession().showView(SModConsts.HRS_PAY_RCP, SModSysConsts.HRSS_TP_PAY_FOR, null);
            }
            else if (item == jmiPayPayrollRecord) {
                miClient.getSession().showView(SModConsts.HRSX_PAY_REC, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiPayCfdiPayroll) {
                miClient.getSession().showView(SModConsts.HRS_SIE_PAY, SModConsts.VIEW_SC_SUM, new SGuiParams(SCfdConsts.CFDI_PAYROLL_VER_CUR));
            }
            else if (item == jmiPayCfdiPayrollDetail) {
                miClient.getSession().showView(SModConsts.HRS_SIE_PAY, SModConsts.VIEW_SC_DET, new SGuiParams(SCfdConsts.CFDI_PAYROLL_VER_CUR));
            }
            /*
            else if (item == jmiPayCfdiStampAvailable) {
                miClient.getGuiModule(SDataConstants.MOD_CFG).showView(SDataConstants.TRNX_STAMP_AVL);
            }
            */
            else if (item == jmiPayCfdiStampSign) {
                showView(SDataConstants.TRN_CFD, SDataConstants.TRNX_STAMP_SIGN, SCfdConsts.CFDI_PAYROLL_VER_CUR);
            }
            else if (item == jmiPayCfdiStampSignPending) {
                showView(SDataConstants.TRN_CFD, SDataConstants.TRNX_STAMP_SIGN_PEND, SCfdConsts.CFDI_PAYROLL_VER_CUR);
            }
            else if (item == jmiPayCfdiSendingLog) {
                showView(SDataConstants.TRN_CFD_SND_LOG, SCfdConsts.CFDI_PAYROLL_VER_CUR);
            }
            else if (item == jmiPayAutomaticEarningsGlobal) {
                miClient.getSession().showView(SModConsts.HRSX_AUT_EAR, SModSysConsts.HRS_AUT_GBL, null);
            }
            else if (item == jmiPayAutomaticEarningsByEmployee) {
                miClient.getSession().showView(SModConsts.HRSX_AUT_EAR, SModSysConsts.HRS_AUT_EMP, null);
            }
            else if (item == jmiPayAutomaticDeductionsGlobal) {
                miClient.getSession().showView(SModConsts.HRSX_AUT_DED, SModSysConsts.HRS_AUT_GBL, null);
            }
            else if (item == jmiPayAutomaticDeductionsByEmployee) {
                miClient.getSession().showView(SModConsts.HRSX_AUT_DED, SModSysConsts.HRS_AUT_EMP, null);
            }
            else if (item == jmiBenefitBenefitVac) {
                miClient.getSession().showView(SModConsts.HRSX_BEN, SModSysConsts.HRSS_TP_BEN_VAC, null);
            }
            else if (item == jmiBenefitBenefitBonVac) {
                miClient.getSession().showView(SModConsts.HRSX_BEN, SModSysConsts.HRSS_TP_BEN_VAC_BON, null);
            }
            else if (item == jmiBenefitBenefitBonAnn) {
                miClient.getSession().showView(SModConsts.HRSX_BEN, SModSysConsts.HRSS_TP_BEN_ANN_BON, null);
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
                showView(SDataConstants.HRS_FORMER_PAYR);
            }
            else if (item == jmiImpFormerPayrollEmp) {
                showView(SDataConstants.HRS_FORMER_PAYR_EMP);
            }
            else if (item == jmiImpImport) {
                if (moDialogFormerPayrollImport == null) {
                    moDialogFormerPayrollImport = new SDialogFormerPayrollImport(miClient);
                }
                moDialogFormerPayrollImport.resetForm();
                moDialogFormerPayrollImport.setVisible(true);
                //showView(SDataConstants.TRN_DNC_DPS_DNS, SDataConstantsSys.TRNS_CT_DPS_SAL);
            }
            else if (item == jmiCatEmployee) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showView(SDataConstants.BPSX_BP_EMP);
            }
            else if (item == jmiCatEmployeeIntegral) {
                showPanelQueryIntegralEmployee(SDataConstants.HRS_FORMER_PAYR_EMP);
            }
            else if (item == jmiCatEmployeeHireLog) {
                miClient.getSession().showView(SModConsts.HRS_EMP_LOG_HIRE, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatEmployeeWageLog) {
                miClient.getSession().showView(SModConsts.HRS_EMP_LOG_WAGE, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatEmployeeWageSscBaseLog) {
                miClient.getSession().showView(SModConsts.HRS_EMP_LOG_SAL_SSC, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatEarnings) {
                miClient.getSession().showView(SModConsts.HRS_EAR, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatDeductions) {
                miClient.getSession().showView(SModConsts.HRS_DED, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiImpCfdiPayroll) {
                miClient.getSession().showView(SModConsts.HRS_SIE_PAY, SModConsts.VIEW_SC_SUM, new SGuiParams(SCfdConsts.CFDI_PAYROLL_VER_OLD));
            }
            else if (item == jmiImpCfdiPayrollDetail) {
                miClient.getSession().showView(SModConsts.HRS_SIE_PAY, SModConsts.VIEW_SC_DET, new SGuiParams(SCfdConsts.CFDI_PAYROLL_VER_OLD));
            }
            else if (item == jmiImpCfdiStampAvailable) {
                miClient.getGuiModule(SDataConstants.MOD_CFG).showView(SDataConstants.TRNX_STAMP_AVL);
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
            else if (item == jmiRepPayrollEarningsAux) {
                new SDialogRepHrsAux((SGuiClient) miClient, SModConsts.HRSR_AUX_EAR, "Auxiliares de nóminas de percepciones").setFormVisible(true);
            }
            else if (item == jmiRepPayrollDeductionsAux) {
                new SDialogRepHrsAux((SGuiClient) miClient, SModConsts.HRSR_AUX_DED, "Auxiliares de nóminas de deducciones").setFormVisible(true);
            }
            else if (item == jmiRepPayrollEarningsByEmployeeAux) {
                new SDialogRepHrsAux((SGuiClient) miClient, SModConsts.HRSR_AUX_EAR_EMP, "Auxiliares de nóminas de percepciones por empleado").setFormVisible(true);
            }
            else if (item == jmiRepPayrollDeductionsByEmployeeAux) {
                new SDialogRepHrsAux((SGuiClient) miClient, SModConsts.HRSR_AUX_DED_EMP, "Auxiliares de nóminas de deducciones por empleado").setFormVisible(true);
            }
            else if (item == jmiRepPayrollTax) {
                new SDialogRepHrsPayrollTax((SGuiClient) miClient, "Impuesto sobre nóminas").setFormVisible(true);
            }
        }
    }
}
