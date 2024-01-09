/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.cfd.SCfdConsts;
import erp.cfd.utils.SDialogReissueCfdis;
import erp.cfd.utils.SDialogVerifyCfdis;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.gui.mod.cfg.SCfgMenu;
import erp.gui.mod.cfg.SCfgModule;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STableTabComponent;
import erp.lib.table.STableTabInterface;
import erp.mcfg.data.SCfgUtils;
import erp.mhrs.form.SDialogFormerPayrollImport;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SHrsConsts;
import erp.mod.hrs.db.SHrsFinUtils;
import erp.mod.hrs.form.SDialogCalculateIncomeTax;
import erp.mod.hrs.form.SDialogLayoutAF02;
import erp.mod.hrs.form.SDialogRepHrsActiveEmployees;
import erp.mod.hrs.form.SDialogRepHrsAuxPayroll;
import erp.mod.hrs.form.SDialogRepHrsEarningDeduction;
import erp.mod.hrs.form.SDialogRepHrsEarningsDeductionsFileCsv;
import erp.mod.hrs.form.SDialogRepHrsPayrollTax;
import erp.mod.hrs.form.SDialogRepHrsPayrollWageSalaryFileCsv;
import erp.mod.hrs.form.SDialogRepHrsPos;
import erp.mod.hrs.form.SFormCalculateNetGrossAmount;
import erp.mod.hrs.view.SViewEmployeeHireLogByPeriod;
import erp.mod.trn.form.SDialogSearchCfdiByUuid;
import erp.mtrn.form.SFormCfdiMassiveValidation;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;

/**
 *
 * @author Sergio Flores, Juan Barajas, Edwin Carmona, Claudio Peña, Isabel Servín, Sergio Flores
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
    private javax.swing.JMenuItem jmiCfgCutoffCalendar;
    private javax.swing.JMenuItem jmiCfgWorkingDaySettings;
    private javax.swing.JMenuItem jmiCfgPaysheetCustomType;
    private javax.swing.JMenuItem jmiCfgEarnings;
    private javax.swing.JMenuItem jmiCfgDeductions;
    private javax.swing.JMenu jmCfgAcc;
    private javax.swing.JMenuItem jmiCfgAccDepartmentPackCostCenters;
    private javax.swing.JMenuItem jmiCfgAccEmployeePackCostCenters;
    private javax.swing.JMenuItem jmiCfgAccEmployeeEarnings;
    private javax.swing.JMenuItem jmiCfgAccEmployeeDeductions;
    private javax.swing.JMenuItem jmiCfgAccEarnings;
    private javax.swing.JMenuItem jmiCfgAccDeductions;
    private javax.swing.JMenuItem jmiCfgAccPackCostCenters;
    private javax.swing.JMenuItem jmiCfgAccPackCostCentersCostCenters;
    private javax.swing.JMenuItem jmiCfgAccPackExpenses;
    private javax.swing.JMenuItem jmiCfgAccPackExpensesItems;
    private javax.swing.JMenuItem jmiCfgAccExpenseTypeAccounts;
    private javax.swing.JMenuItem jmiCfgAccExpenseTypes;
    private javax.swing.JMenu jmCfgBkkEarning;
    private javax.swing.JMenuItem jmiCfgBkkEarningGlobal;
    private javax.swing.JMenuItem jmiCfgBkkEarningDepartament;
    private javax.swing.JMenuItem jmiCfgBkkEarningEmployee;
    private javax.swing.JMenu jmCfgBkkDeduction;
    private javax.swing.JMenuItem jmiCfgBkkDeductionGlobal;
    private javax.swing.JMenuItem jmiCfgBkkDeductionDepartament;
    private javax.swing.JMenuItem jmiCfgBkkDeductionEmployee;
    private javax.swing.JMenuItem jmiCfgBkkRestoreAccountingSettings;
    private javax.swing.JMenuItem jmiCfgConfig;
    
    private javax.swing.JMenu jmCat;
    private javax.swing.JMenuItem jmiCatEmployee;
    private javax.swing.JMenuItem jmiCatEmployeePersonalInfo;
    private javax.swing.JMenuItem jmiCatEmployeeContractsExp;
    private javax.swing.JMenuItem jmiCatEmployeeIntegral;
    private javax.swing.JMenuItem jmiCatEmployeeHireLog;
    private javax.swing.JMenuItem jmiCatEmployeeWageLog;
    private javax.swing.JMenuItem jmiCatEmployeeSscBaseLog;
    private javax.swing.JMenuItem jmiCatEmployeeSua;
    private javax.swing.JMenuItem jmiCatEmployeeIdse;
    private javax.swing.JMenuItem jmiCatPosition;
    private javax.swing.JMenuItem jmiCatDeparment;
    private javax.swing.JMenuItem jmiCatDepartmentCc;
    private javax.swing.JMenuItem jmiCatEmployeeCc;
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
    
    private javax.swing.JMenu jmBen;
    private javax.swing.JMenuItem jmiBenAbsence;
    private javax.swing.JMenuItem jmiBenBenefitTables;
    private javax.swing.JMenuItem jmiBenBenefitVacStat;
    private javax.swing.JMenuItem jmiBenBenefitVac;
    private javax.swing.JMenuItem jmiBenBenefitBonVac;
    private javax.swing.JMenuItem jmiBenBenefitBonAnn;
    private javax.swing.JMenuItem jmiBenBenefitAdjustmentEar;
    private javax.swing.JMenuItem jmiBenLoan;
    private javax.swing.JMenuItem jmiBenLoanAdjustmentEar;
    private javax.swing.JMenuItem jmiBenLoanAdjustmentDed;
    private javax.swing.JMenuItem jmiBenAdvanceSettlement;
    
    private javax.swing.JMenu jmPay;
    private javax.swing.JMenuItem jmiPayPayrollWeek;
    private javax.swing.JMenuItem jmiPayPayrollWeekRec;
    private javax.swing.JMenuItem jmiPayPayrollFortnight;
    private javax.swing.JMenuItem jmiPayPayrollFortnightRec;
    private javax.swing.JMenuItem jmiPayPayrollRecImportedEarWeek;
    private javax.swing.JMenuItem jmiPayPayrollRecImportedEarFortnight;
    private javax.swing.JMenuItem jmiPayCfdiPayroll;
    private javax.swing.JMenuItem jmiPayCfdiPayrollRec;
    private javax.swing.JMenuItem jmiCfdiMassiveValidation;
    private javax.swing.JMenuItem jmiUuidSearch;
    private javax.swing.JMenu jmPayCfdi;
    private javax.swing.JMenuItem jmiPayCfdiStampSign;
    private javax.swing.JMenuItem jmiPayCfdiStampSignPending;
    private javax.swing.JMenuItem jmiPayCfdiSendingLog;
    private javax.swing.JMenuItem jmiPayPayrollBkkRecord;
    private javax.swing.JMenuItem jmiPayConditionalEarnings;
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
    private javax.swing.JMenuItem jmiPayReissueCfdis;
    private javax.swing.JMenuItem jmiPayVerifyCfdis;
    
    private javax.swing.JMenu jmSan;
    private javax.swing.JMenuItem jmiSanDocBreach;
    private javax.swing.JMenuItem jmiSanDocBreachSum;
    private javax.swing.JMenuItem jmiSanDocAdminRecord;
    private javax.swing.JMenuItem jmiSanDocAdminRecordSum;
    
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
    private javax.swing.JMenuItem jmiRepPayrollAux;
    private javax.swing.JMenuItem jmiRepPayrollEarningsDeductions;
    private javax.swing.JMenuItem jmiRepPayrollVariableEarnings;
    private javax.swing.JMenuItem jmiRepPayrollTax;
    private javax.swing.JMenuItem jmiRepPayrollWageSalaryFileCsv;
    private javax.swing.JMenuItem jmiRepPayrollEarDedFileCsv;
    private javax.swing.JMenuItem jmiRepEmployeeActive;
    private javax.swing.JMenuItem jmiRepHireLogByPeriodActive;
    private javax.swing.JMenuItem jmiRepHireLogByPeriodInactive;
    private javax.swing.JMenuItem jmiRepPtu;
    private javax.swing.JMenuItem jmiRepBenAnnBon;
    private javax.swing.JMenuItem jmiRepBankPayrollDisp;
    private javax.swing.JMenuItem jmiRepAnnexAF02;
    private javax.swing.JMenuItem jmiRepPositions;

    private int mnParamPayrollAccProcess;
    private erp.mhrs.form.SDialogFormerPayrollImport moDialogFormerPayrollImport;

    public SGuiModuleHrs(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_HRS);
        initComponents();
    }

    private void initComponents() {
        try {
            mnParamPayrollAccProcess = SLibUtils.parseInt(SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_HRS_PAYROLL_ACC_PROCESS));
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
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
        jmiCfgCutoffCalendar = new JMenuItem("Calendario fechas de corte");
        jmiCfgWorkingDaySettings = new JMenuItem("Días laborables");
        jmiCfgPaysheetCustomType = new JMenuItem("Tipos de nómina de la empresa");
        jmiCfgEarnings = new JMenuItem("Percepciones");
        jmiCfgDeductions = new JMenuItem("Deducciones");
        jmCfgAcc = new JMenu("Contabilización de nóminas");
        jmiCfgAccDepartmentPackCostCenters = new JMenuItem("Departamentos y paquetes de centros de costos");
        jmiCfgAccEmployeePackCostCenters = new JMenuItem("Empleados y paquetes de centros de costos");
        jmiCfgAccEmployeeEarnings = new JMenuItem("Empleados y percepciones");
        jmiCfgAccEmployeeDeductions = new JMenuItem("Empleados y deducciones");
        jmiCfgAccEarnings = new JMenuItem("Contabilización de percepciones");
        jmiCfgAccDeductions = new JMenuItem("Contabilización de deducciones");
        jmiCfgAccPackCostCenters = new JMenuItem("Paquetes de centros de costos");
        jmiCfgAccPackCostCentersCostCenters = new JMenuItem("Paquetes de centros de costos y centros de costos");
        jmiCfgAccPackExpenses = new JMenuItem("Paquetes de gastos");
        jmiCfgAccPackExpensesItems = new JMenuItem("Paquetes de gastos e ítems");
        jmiCfgAccExpenseTypeAccounts = new JMenuItem("Contabilización de tipos de gasto");
        jmiCfgAccExpenseTypes = new JMenuItem("Tipos de gasto");
        jmCfgBkkEarning = new JMenu("Contabilización de percepciones");
        jmiCfgBkkEarningGlobal = new JMenuItem("Percepciones globales");
        jmiCfgBkkEarningDepartament = new JMenuItem("Percepciones por departamento");
        jmiCfgBkkEarningEmployee = new JMenuItem("Percepciones por empleado");
        jmCfgBkkDeduction = new JMenu("Contabilización de deducciones");
        jmiCfgBkkDeductionGlobal = new JMenuItem("Deducciones globales");
        jmiCfgBkkDeductionDepartament = new JMenuItem("Deducciones por departamento");
        jmiCfgBkkDeductionEmployee = new JMenuItem("Deducciones por empleado");
        jmiCfgBkkRestoreAccountingSettings = new JMenuItem("Restore configuraciones de contabilización...");
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
        jmCfg.add(jmiCfgCutoffCalendar);
        jmCfg.add(jmiCfgWorkingDaySettings);
        jmCfg.add(jmiCfgPaysheetCustomType);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgEarnings);
        jmCfg.add(jmiCfgDeductions);
        jmCfg.addSeparator();
        jmCfgAcc.add(jmiCfgAccDepartmentPackCostCenters);
        jmCfgAcc.addSeparator();
        jmCfgAcc.add(jmiCfgAccEmployeePackCostCenters);
        jmCfgAcc.add(jmiCfgAccEmployeeEarnings);
        jmCfgAcc.add(jmiCfgAccEmployeeDeductions);
        jmCfgAcc.addSeparator();
        jmCfgAcc.add(jmiCfgAccEarnings);
        jmCfgAcc.add(jmiCfgAccDeductions);
        jmCfgAcc.addSeparator();
        jmCfgAcc.add(jmiCfgAccPackCostCenters);
        jmCfgAcc.add(jmiCfgAccPackCostCentersCostCenters);
        jmCfgAcc.addSeparator();
        jmCfgAcc.add(jmiCfgAccPackExpenses);
        jmCfgAcc.add(jmiCfgAccPackExpensesItems);
        jmCfgAcc.addSeparator();
        jmCfgAcc.add(jmiCfgAccExpenseTypeAccounts);
        jmCfgAcc.add(jmiCfgAccExpenseTypes);
        jmCfg.add(jmCfgAcc);
        jmCfgBkkEarning.add(jmiCfgBkkEarningGlobal);
        jmCfgBkkEarning.add(jmiCfgBkkEarningDepartament);
        jmCfgBkkEarning.add(jmiCfgBkkEarningEmployee);
        jmCfg.add(jmCfgBkkEarning);
        jmCfgBkkDeduction.add(jmiCfgBkkDeductionGlobal);
        jmCfgBkkDeduction.add(jmiCfgBkkDeductionDepartament);
        jmCfgBkkDeduction.add(jmiCfgBkkDeductionEmployee);
        jmCfg.add(jmCfgBkkDeduction);
        jmCfg.add(jmiCfgBkkRestoreAccountingSettings);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgConfig);

        jmCat = new JMenu("Catálogos");
        jmiCatEmployee = new JMenuItem("Empleados");
        jmiCatEmployeePersonalInfo = new JMenuItem("Datos personales de empleados");
        jmiCatEmployeeContractsExp = new JMenuItem("Terminación de contratos de empleados");
        jmiCatEmployeeIntegral = new JMenuItem("Consulta integral de empleados");
        jmiCatEmployeeHireLog = new JMenuItem("Bitácora de altas y bajas");
        jmiCatEmployeeWageLog = new JMenuItem("Bitácora de sueldos y salarios");
        jmiCatEmployeeSscBaseLog = new JMenuItem("Bitácora de salarios base de cotización");
        jmiCatEmployeeSua = new JMenuItem("Empleados para SUA");
        jmiCatEmployeeIdse = new JMenuItem("Empleados para IDSE");
        jmiCatPosition = new JMenuItem("Puestos");
        jmiCatDeparment = new JMenuItem("Departamentos");
        jmiCatDepartmentCc = new JMenuItem("Departamentos y centros de costo");
        jmiCatEmployeeCc = new JMenuItem("Empleados y centros de costo");
        jmiCatShift = new JMenuItem("Turnos");
        jmiCatEmployeeType = new JMenuItem("Tipos de empleado");
        jmiCatWorkerType = new JMenuItem("Tipos de obrero");
        jmiCatEmployeeDismissType = new JMenuItem("Motivos de baja");
        jmiCatMwzType = new JMenuItem("Áreas geográficas");
        jmiCatMwzTypeWage = new JMenuItem("Salarios mínimos de áreas geográficas");
        jmiCatUma = new JMenuItem("Unidades de Medida y Actualización (UMA)");
        jmiCatUmi = new JMenuItem("Unidades Mixtas INFONAVIT (UMI)");
        jmiCatWorkerTypeSalary = new JMenuItem("Salarios diarios por tipo de obrero");
        jmiCatLoanTypeAdjustment = new JMenuItem("Ajustes por tipo de crédito/préstamo");
        jmiCatAbsenceType = new JMenuItem("Tipos de incidencia");
        jmiCatAbsenceClass = new JMenuItem("Clases de incidencia");

        jmCat.add(jmiCatEmployee);
        jmCat.add(jmiCatEmployeePersonalInfo);
        jmCat.add(jmiCatEmployeeContractsExp);
        jmCat.add(jmiCatEmployeeIntegral);
        jmCat.addSeparator();
        jmCat.add(jmiCatEmployeeHireLog);
        jmCat.add(jmiCatEmployeeWageLog);
        jmCat.add(jmiCatEmployeeSscBaseLog);
        jmCat.add(jmiCatEmployeeSua);
        jmCat.add(jmiCatEmployeeIdse);
        jmCat.addSeparator();
        jmCat.add(jmiCatPosition);
        jmCat.add(jmiCatDeparment);
        jmCat.add(jmiCatDepartmentCc);
        jmCat.add(jmiCatEmployeeCc);
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

        jmBen = new JMenu("Prestaciones");
        jmiBenAbsence = new JMenuItem("Incidencias");
        jmiBenBenefitTables = new JMenuItem("Prestaciones de empleados");
        jmiBenBenefitVacStat = new JMenuItem("Estatus de vacaciones");
        jmiBenBenefitVac = new JMenuItem("Control de vacaciones");
        jmiBenBenefitBonVac = new JMenuItem("Control de prima vacacional");
        jmiBenBenefitBonAnn = new JMenuItem("Control de gratificación anual");
        jmiBenBenefitAdjustmentEar = new JMenuItem("Ajustes a prestaciones");
        jmiBenLoan = new JMenuItem("Créditos/préstamos");
        jmiBenLoanAdjustmentEar = new JMenuItem("Incrementos a créditos/préstamos");
        jmiBenLoanAdjustmentDed = new JMenuItem("Decrementos a créditos/préstamos");
        jmiBenAdvanceSettlement = new JMenuItem("Control de adelantos de liquidación");
        
        jmBen.add(jmiBenAbsence);
        jmBen.addSeparator();
        jmBen.add(jmiBenBenefitTables);
        jmBen.add(jmiBenBenefitVacStat);
        jmBen.addSeparator();
        jmBen.add(jmiBenBenefitVac);
        jmBen.add(jmiBenBenefitBonVac);
        jmBen.add(jmiBenBenefitBonAnn);
        jmBen.add(jmiBenBenefitAdjustmentEar);
        jmBen.addSeparator();
        jmBen.add(jmiBenLoan);
        jmBen.add(jmiBenLoanAdjustmentEar);
        jmBen.add(jmiBenLoanAdjustmentDed);
        jmBen.addSeparator();
        jmBen.add(jmiBenAdvanceSettlement);

        jmPay = new JMenu("Nóminas");
        jmiPayPayrollWeek = new JMenuItem("Nóminas semanales");
        jmiPayPayrollWeekRec = new JMenuItem("Recibos de nóminas semanales");
        jmiPayPayrollFortnight = new JMenuItem("Nóminas quincenales");
        jmiPayPayrollFortnightRec = new JMenuItem("Recibos de nóminas quincenales");
        jmiPayPayrollRecImportedEarWeek = new JMenuItem("Percepciones semanales importadas");
        jmiPayPayrollRecImportedEarFortnight = new JMenuItem("Percepciones quincenales importadas");
        jmiPayCfdiPayroll = new JMenuItem("CFDI de nóminas");
        jmiPayCfdiPayrollRec = new JMenuItem("CFDI de recibos de nóminas");
        jmiCfdiMassiveValidation = new JMenuItem("Validación masiva de estatus de CFDI de recibos de nóminas... ");
        jmiUuidSearch = new JMenuItem("Busqueda de CFDI por UUID...");
        jmPayCfdi = new JMenu("Comprobantes fiscales digitales");
        jmiPayCfdiStampSign = new JMenuItem("CFDI de nóminas timbrados");
        jmiPayCfdiStampSignPending = new JMenuItem("CFDI de nóminas por timbrar");
        jmiPayCfdiSendingLog = new JMenuItem("Bitácora de envíos de CFDI de nóminas");
        jmiPayPayrollBkkRecord = new JMenuItem("Recibos de nóminas vs. pólizas contables");
        jmiPayConditionalEarnings = new JMenuItem("Percepciones condicionales");
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
        jmiPayReissueCfdis = new JMenuItem("Reexpedición de recibos");
        jmiPayVerifyCfdis = new JMenuItem("Verificar CFDIs");

        jmPay.add(jmiPayPayrollWeek);
        jmPay.add(jmiPayPayrollWeekRec);
        jmPay.addSeparator();
        jmPay.add(jmiPayPayrollFortnight);
        jmPay.add(jmiPayPayrollFortnightRec);
        jmPay.addSeparator();
        jmPay.add(jmiPayPayrollRecImportedEarWeek);
        jmPay.add(jmiPayPayrollRecImportedEarFortnight);
        jmPay.addSeparator();
        jmPay.add(jmiPayCfdiPayroll);
        jmPay.add(jmiPayCfdiPayrollRec);
        jmPay.add(jmiCfdiMassiveValidation);
        jmPay.add(jmiUuidSearch);
        jmPayCfdi.add(jmiPayCfdiStampSign);
        jmPayCfdi.add(jmiPayCfdiStampSignPending);
        jmPayCfdi.addSeparator();
        jmPayCfdi.add(jmiPayCfdiSendingLog);
        jmPay.add(jmPayCfdi);
        jmPay.addSeparator();
        jmPay.add(jmiPayPayrollBkkRecord);
        jmPay.addSeparator();
        jmPay.add(jmiPayConditionalEarnings);
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
        /* Funcionalidades temporales que por lo pronto no son requeridas:
        jmPay.addSeparator();
        jmPay.add(jmiPayReissueCfdis);
        jmPay.add(jmiPayVerifyCfdis);
        */
        
        jmSan = new JMenu("Sanciones");
        jmiSanDocBreach = new JMenuItem("Infracciones");
        jmiSanDocBreachSum = new JMenuItem("Infracciones por empleado (resumen)");
        jmiSanDocAdminRecord = new JMenuItem("Actas administrativas");
        jmiSanDocAdminRecordSum = new JMenuItem("Actas administrativas por empleado (resumen)");
        
        jmSan.add(jmiSanDocBreach);
        jmSan.add(jmiSanDocBreachSum);
        jmSan.addSeparator();
        jmSan.add(jmiSanDocAdminRecord);
        jmSan.add(jmiSanDocAdminRecordSum);
        
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
        jmiRepPayrollAux = new JMenuItem("Auxiliares de nóminas...");
        jmiRepPayrollEarningsDeductions = new JMenuItem("Percepciones y deducciones por período...");
        jmiRepPayrollVariableEarnings = new JMenuItem("Percepciones variables por período...");
        jmiRepPayrollTax = new JMenuItem("Impuesto sobre nóminas...");
        jmiRepPayrollWageSalaryFileCsv = new JMenuItem("Archivo CSV para declaración informativa de sueldos y salarios...");
        jmiRepPayrollEarDedFileCsv = new JMenuItem("Archivo CSV de percepciones y deducciones en el ejercicio...");
        jmiRepEmployeeActive = new JMenuItem("Reporte de empleados activos por período...");
        jmiRepHireLogByPeriodActive = new JMenuItem("Consulta de altas por período");
        jmiRepHireLogByPeriodInactive = new JMenuItem("Consulta de bajas por período");
        jmiRepPtu = new JMenuItem("Consulta para PTU");
        jmiRepBenAnnBon = new JMenuItem("Consulta provisión aguinaldo");
        jmiRepBankPayrollDisp = new JMenuItem("Consulta de dispersión de nóminas");
        jmiRepAnnexAF02 = new JMenuItem("Layout anexo AF02");
        jmiRepPositions = new JMenuItem("Reporte de posiciones y vacantes");
        
        jmRep.add(jmiRepPayrollAux);
        jmRep.add(jmiRepPayrollEarningsDeductions);
        jmRep.add(jmiRepPayrollVariableEarnings);
        jmRep.addSeparator();
        jmRep.add(jmiRepPayrollTax);
        jmRep.addSeparator();
        jmRep.add(jmiRepPayrollWageSalaryFileCsv);
        jmRep.add(jmiRepPayrollEarDedFileCsv);
        jmRep.addSeparator();
        jmRep.add(jmiRepEmployeeActive);
        jmRep.add(jmiRepHireLogByPeriodActive);
        jmRep.add(jmiRepHireLogByPeriodInactive);
        jmRep.add(jmiRepPtu);
        jmRep.add(jmiRepBenAnnBon);
        jmRep.add(jmiRepBankPayrollDisp);
        jmRep.add(jmiRepAnnexAF02);
        jmRep.addSeparator();
        jmRep.add(jmiRepPositions);
        
        // listeners:

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
        jmiCfgCutoffCalendar.addActionListener(this);
        jmiCfgWorkingDaySettings.addActionListener(this);
        jmiCfgPaysheetCustomType.addActionListener(this);
        jmiCfgEarnings.addActionListener(this);
        jmiCfgDeductions.addActionListener(this);
        jmiCfgAccDepartmentPackCostCenters.addActionListener(this);
        jmiCfgAccEmployeePackCostCenters.addActionListener(this);
        jmiCfgAccEmployeeEarnings.addActionListener(this);
        jmiCfgAccEmployeeDeductions.addActionListener(this);
        jmiCfgAccEarnings.addActionListener(this);
        jmiCfgAccDeductions.addActionListener(this);
        jmiCfgAccPackCostCenters.addActionListener(this);
        jmiCfgAccPackCostCentersCostCenters.addActionListener(this);
        jmiCfgAccPackExpenses.addActionListener(this);
        jmiCfgAccPackExpensesItems.addActionListener(this);
        jmiCfgAccExpenseTypeAccounts.addActionListener(this);
        jmiCfgAccExpenseTypes.addActionListener(this);
        jmiCfgBkkEarningGlobal.addActionListener(this);
        jmiCfgBkkEarningDepartament.addActionListener(this);
        jmiCfgBkkEarningEmployee.addActionListener(this);
        jmiCfgBkkDeductionGlobal.addActionListener(this);
        jmiCfgBkkDeductionDepartament.addActionListener(this);
        jmiCfgBkkDeductionEmployee.addActionListener(this);
        jmiCfgBkkRestoreAccountingSettings.addActionListener(this);
        jmiCfgConfig.addActionListener(this);
        
        jmiCatEmployee.addActionListener(this);
        jmiCatEmployeePersonalInfo.addActionListener(this);
        jmiCatEmployeeContractsExp.addActionListener(this);
        jmiCatEmployeeIntegral.addActionListener(this);
        jmiCatEmployeeHireLog.addActionListener(this);
        jmiCatEmployeeWageLog.addActionListener(this);
        jmiCatEmployeeSscBaseLog.addActionListener(this);
        jmiCatEmployeeSua.addActionListener(this);
        jmiCatEmployeeIdse.addActionListener(this);
        jmiCatPosition.addActionListener(this);
        jmiCatDeparment.addActionListener(this);
        jmiCatDepartmentCc.addActionListener(this);
        jmiCatEmployeeCc.addActionListener(this);
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
        
        jmiBenAbsence.addActionListener(this);
        jmiBenBenefitTables.addActionListener(this);
        jmiBenBenefitVacStat.addActionListener(this);
        jmiBenBenefitVac.addActionListener(this);
        jmiBenBenefitBonVac.addActionListener(this);
        jmiBenBenefitBonAnn.addActionListener(this);
        jmiBenBenefitAdjustmentEar.addActionListener(this);
        jmiBenLoan.addActionListener(this);
        jmiBenLoanAdjustmentEar.addActionListener(this);
        jmiBenLoanAdjustmentDed.addActionListener(this);
        jmiBenAdvanceSettlement.addActionListener(this);
        
        jmiPayPayrollWeek.addActionListener(this);
        jmiPayPayrollWeekRec.addActionListener(this);
        jmiPayPayrollFortnight.addActionListener(this);
        jmiPayPayrollFortnightRec.addActionListener(this);
        jmiPayPayrollRecImportedEarWeek.addActionListener(this);
        jmiPayPayrollRecImportedEarFortnight.addActionListener(this);
        jmiPayCfdiPayroll.addActionListener(this);
        jmiPayCfdiPayrollRec.addActionListener(this);
        jmiCfdiMassiveValidation.addActionListener(this);
        jmiUuidSearch.addActionListener(this);
        jmiPayCfdiStampSign.addActionListener(this);
        jmiPayCfdiStampSignPending.addActionListener(this);
        jmiPayCfdiSendingLog.addActionListener(this);
        jmiPayPayrollBkkRecord.addActionListener(this);
        jmiPayConditionalEarnings.addActionListener(this);
        jmiPayAutoEarningsGlobal.addActionListener(this);
        jmiPayAutoEarningsByEmployee.addActionListener(this);
        jmiPayAutoEarningsByEmployeeDet.addActionListener(this);
        jmiPayAutoDeductionsGlobal.addActionListener(this);
        jmiPayAutoDeductionsByEmployee.addActionListener(this);
        jmiPayAutoDeductionsByEmployeeDet.addActionListener(this);
        jmiPayCalculatedAmountMonth.addActionListener(this);
        jmiPayCalculatedEstimateIncomeTax.addActionListener(this);
        jmiPayReissueCfdis.addActionListener(this);
        jmiPayVerifyCfdis.addActionListener(this);
        
        jmiSanDocBreach.addActionListener(this);
        jmiSanDocBreachSum.addActionListener(this);
        jmiSanDocAdminRecord.addActionListener(this);
        jmiSanDocAdminRecordSum.addActionListener(this);
        
        jmiImpFormerPayroll.addActionListener(this);
        jmiImpFormerPayrollEmp.addActionListener(this);
        jmiImpImport.addActionListener(this);
        jmiImpCfdiPayroll.addActionListener(this);
        jmiImpCfdiPayrollDetail.addActionListener(this);
        jmiImpCfdiStampSign.addActionListener(this);
        jmiImpCfdiStampSignPending.addActionListener(this);
        jmiImpCfdiSendingLog.addActionListener(this);
        
        jmiRepPayrollAux.addActionListener(this);
        jmiRepPayrollEarningsDeductions.addActionListener(this);
        jmiRepPayrollVariableEarnings.addActionListener(this);
        jmiRepPayrollTax.addActionListener(this);
        jmiRepPayrollWageSalaryFileCsv.addActionListener(this);
        jmiRepPayrollEarDedFileCsv.addActionListener(this);
        jmiRepEmployeeActive.addActionListener(this);
        jmiRepHireLogByPeriodActive.addActionListener(this);
        jmiRepHireLogByPeriodInactive.addActionListener(this);
        jmiRepPtu.addActionListener(this);
        jmiRepBenAnnBon.addActionListener(this);
        jmiRepBankPayrollDisp.addActionListener(this);
        jmiRepAnnexAF02.addActionListener(this);
        jmiRepPositions.addActionListener(this);
        
        // display user rights:
        
        boolean hasRightConfig = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CFG).HasRight;
        
        jmCfg.setEnabled(hasRightConfig);
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
        jmiCfgCutoffCalendar.setEnabled(true);
        jmiCfgWorkingDaySettings.setEnabled(true);
        jmiCfgPaysheetCustomType.setEnabled(true);
        jmiCfgEarnings.setEnabled(true);
        jmiCfgDeductions.setEnabled(true);
        jmCfgAcc.setEnabled(mnParamPayrollAccProcess == SHrsConsts.CFG_ACC_PROCESS_DYNAMIC);
        jmiCfgAccDepartmentPackCostCenters.setEnabled(true);
        jmiCfgAccEmployeePackCostCenters.setEnabled(true);
        jmiCfgAccEmployeeEarnings.setEnabled(true);
        jmiCfgAccEmployeeDeductions.setEnabled(true);
        jmiCfgAccEarnings.setEnabled(true);
        jmiCfgAccDeductions.setEnabled(true);
        jmiCfgAccPackCostCenters.setEnabled(true);
        jmiCfgAccPackCostCentersCostCenters.setEnabled(true);
        jmiCfgAccPackExpenses.setEnabled(true);
        jmiCfgAccPackExpensesItems.setEnabled(true);
        jmiCfgAccExpenseTypeAccounts.setEnabled(true);
        jmiCfgAccExpenseTypes.setEnabled(true);
        jmCfgBkkEarning.setEnabled(mnParamPayrollAccProcess == SHrsConsts.CFG_ACC_PROCESS_ORIGINAL);
        jmiCfgBkkEarningGlobal.setEnabled(true);
        jmiCfgBkkEarningDepartament.setEnabled(true);
        jmiCfgBkkEarningEmployee.setEnabled(true);
        jmCfgBkkDeduction.setEnabled(mnParamPayrollAccProcess == SHrsConsts.CFG_ACC_PROCESS_ORIGINAL);
        jmiCfgBkkDeductionGlobal.setEnabled(true);
        jmiCfgBkkDeductionDepartament.setEnabled(true);
        jmiCfgBkkDeductionEmployee.setEnabled(true);
        jmiCfgBkkRestoreAccountingSettings.setEnabled(mnParamPayrollAccProcess == SHrsConsts.CFG_ACC_PROCESS_ORIGINAL);
        jmiCfgConfig.setEnabled(true);
        
        boolean hasRightCat = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT).HasRight;
        boolean hasRightEmp = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP).HasRight;
        boolean hasRightEmpWage = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP_WAGE).HasRight;
        boolean hasRightEmpData = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_EMP_PERS_DATA).HasRight;
        boolean hasRightEmpVariableEarnings = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_EMP_VARIABLE_EARNINGS).HasRight;
        boolean hasRightAuxHrs = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_AUX_HRS).HasRight;
        
        jmCat.setEnabled(hasRightConfig || hasRightCat || hasRightEmp || hasRightEmpWage || hasRightEmpData || hasRightAuxHrs);
        jmiCatEmployee.setEnabled(hasRightEmp || hasRightAuxHrs || hasRightEmpWage);
        jmiCatEmployeePersonalInfo.setEnabled(hasRightEmp || hasRightAuxHrs || hasRightEmpData);
        jmiCatEmployeeContractsExp.setEnabled(hasRightEmp || hasRightAuxHrs);
        jmiCatEmployeeIntegral.setEnabled(hasRightEmp);
        jmiCatEmployeeHireLog.setEnabled(hasRightEmp || hasRightAuxHrs);
        jmiCatEmployeeWageLog.setEnabled(hasRightEmp || hasRightEmpWage);
        jmiCatEmployeeSscBaseLog.setEnabled(hasRightEmp || hasRightEmpWage);
        jmiCatEmployeeSua.setEnabled(hasRightEmp || hasRightEmpWage);
        jmiCatEmployeeIdse.setEnabled(hasRightEmp || hasRightEmpWage);
        jmiCatPosition.setEnabled(hasRightCat || hasRightEmp);
        jmiCatDeparment.setEnabled(hasRightCat || hasRightEmp);
        jmiCatDepartmentCc.setEnabled(hasRightCat || hasRightEmp);
        jmiCatEmployeeCc.setEnabled(hasRightCat || hasRightEmp);
        jmiCatShift.setEnabled(hasRightCat || hasRightEmp);
        jmiCatEmployeeType.setEnabled(hasRightConfig || hasRightCat || hasRightEmp || hasRightEmpWage);
        jmiCatWorkerType.setEnabled(hasRightConfig || hasRightCat || hasRightEmp || hasRightEmpWage);
        jmiCatEmployeeDismissType.setEnabled(hasRightConfig || hasRightCat || hasRightEmp);
        jmiCatMwzType.setEnabled(hasRightConfig || hasRightCat || hasRightEmpWage);
        jmiCatMwzTypeWage.setEnabled(hasRightConfig || hasRightCat || hasRightEmpWage);
        jmiCatUma.setEnabled(hasRightConfig || hasRightCat || hasRightEmpWage);
        jmiCatUmi.setEnabled(hasRightConfig || hasRightCat || hasRightEmpWage);
        jmiCatWorkerTypeSalary.setEnabled(hasRightConfig || hasRightCat || hasRightEmp || hasRightEmpWage);
        jmiCatLoanTypeAdjustment.setEnabled(hasRightConfig || hasRightCat || hasRightEmp || hasRightEmpWage);
        jmiCatAbsenceType.setEnabled(hasRightConfig || hasRightCat || hasRightEmp);
        jmiCatAbsenceClass.setEnabled(hasRightConfig || hasRightCat || hasRightEmp);
        
        boolean hasRightPay = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_PAY).HasRight;
        boolean hasRightPayWeek = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_PAY_WEE).HasRight;
        boolean hasRightPayFortnight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_PAY_FOR).HasRight;
        boolean hasRightAbsence = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_ABS).HasRight;
        
        jmBen.setEnabled(hasRightPay || hasRightPayWeek || hasRightPayFortnight || hasRightAuxHrs || hasRightAbsence);
        jmiBenAbsence.setEnabled(hasRightPay || hasRightPayWeek || hasRightPayFortnight || hasRightAuxHrs || hasRightAbsence);
        jmiBenBenefitTables.setEnabled(hasRightPay);
        jmiBenBenefitVacStat.setEnabled(hasRightPay);
        jmiBenBenefitVac.setEnabled(/*hasRightPay*/false);
        jmiBenBenefitBonVac.setEnabled(/*hasRightPay*/false);
        jmiBenBenefitBonAnn.setEnabled(hasRightPay);
        jmiBenBenefitAdjustmentEar.setEnabled(hasRightPay || hasRightPayWeek || hasRightPayFortnight);
        jmiBenLoan.setEnabled(hasRightPay || hasRightPayWeek || hasRightPayFortnight || hasRightAuxHrs);
        jmiBenLoanAdjustmentEar.setEnabled(hasRightPay || hasRightPayWeek || hasRightPayFortnight || hasRightAuxHrs);
        jmiBenLoanAdjustmentDed.setEnabled(hasRightPay || hasRightPayWeek || hasRightPayFortnight || hasRightAuxHrs);
        jmiBenAdvanceSettlement.setEnabled(hasRightPay || hasRightPayWeek || hasRightPayFortnight);
        
        jmPay.setEnabled(hasRightPay || hasRightPayWeek || hasRightPayFortnight);
        jmiPayPayrollWeek.setEnabled(hasRightPay || hasRightPayWeek);
        jmiPayPayrollWeekRec.setEnabled(hasRightPay || hasRightPayWeek);
        jmiPayPayrollFortnight.setEnabled(hasRightPay || hasRightPayFortnight);
        jmiPayPayrollFortnightRec.setEnabled(hasRightPay || hasRightPayFortnight);
        jmiPayPayrollRecImportedEarWeek.setEnabled(hasRightPay || hasRightPayWeek);
        jmiPayPayrollRecImportedEarFortnight.setEnabled(hasRightPay || hasRightPayFortnight);
        jmiPayCfdiPayroll.setEnabled(hasRightPay);
        jmiPayCfdiPayrollRec.setEnabled(hasRightPay);
        jmiCfdiMassiveValidation.setEnabled(hasRightPay);
        jmiUuidSearch.setEnabled(hasRightPay);
        jmPayCfdi.setEnabled(hasRightPay);
        jmiPayCfdiStampSign.setEnabled(hasRightPay);
        jmiPayCfdiStampSignPending.setEnabled(hasRightPay);
        jmiPayCfdiSendingLog.setEnabled(hasRightPay);
        jmiPayPayrollBkkRecord.setEnabled(hasRightPay);
        jmiPayConditionalEarnings.setEnabled(hasRightPay);
        jmPayAutoEarnings.setEnabled(hasRightPay);
        jmiPayAutoEarningsGlobal.setEnabled(hasRightPay);
        jmiPayAutoEarningsByEmployee.setEnabled(hasRightPay);
        jmiPayAutoEarningsByEmployeeDet.setEnabled(hasRightPay);
        jmPayAutoDeductions.setEnabled(hasRightPay);
        jmiPayAutoDeductionsGlobal.setEnabled(hasRightPay);
        jmiPayAutoDeductionsByEmployee.setEnabled(hasRightPay);
        jmiPayAutoDeductionsByEmployeeDet.setEnabled(hasRightPay);
        jmiPayCalculatedAmountMonth.setEnabled(hasRightPay);
        jmiPayCalculatedEstimateIncomeTax.setEnabled(hasRightPay);
        jmiPayReissueCfdis.setEnabled(hasRightPay);
        jmiPayVerifyCfdis.setEnabled(hasRightPay);
        
        boolean hasRightDocBreach = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_DOC_BREACH).HasRight;
        boolean hasRightDocAdminRecord = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_DOC_ADM_REC).HasRight;
        int levelRightDocBreach = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_DOC_BREACH).Level;
        int levelRightDocAdminRecord = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_DOC_ADM_REC).Level;

        jmSan.setEnabled(hasRightDocBreach || hasRightDocAdminRecord);
        jmiSanDocBreach.setEnabled(hasRightDocBreach);
        jmiSanDocBreachSum.setEnabled(hasRightDocBreach && SLibUtilities.belongsTo(levelRightDocBreach, new int[] { SUtilConsts.LEV_READ, SUtilConsts.LEV_EDITOR, SUtilConsts.LEV_MANAGER }));
        jmiSanDocAdminRecord.setEnabled(hasRightDocAdminRecord);
        jmiSanDocAdminRecordSum.setEnabled(hasRightDocAdminRecord && SLibUtilities.belongsTo(levelRightDocAdminRecord, new int[] { SUtilConsts.LEV_READ, SUtilConsts.LEV_EDITOR, SUtilConsts.LEV_MANAGER }));

        boolean hasRightImport = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_IMP).HasRight;
        
        jmImp.setEnabled(hasRightImport);
        jmiImpFormerPayroll.setEnabled(true);
        jmiImpFormerPayrollEmp.setEnabled(true);
        jmiImpImport.setEnabled(true);
        jmiImpCfdiPayroll.setEnabled(true);
        jmiImpCfdiPayrollDetail.setEnabled(true);
        jmImpCfdi.setEnabled(true);
        jmiImpCfdiStampSign.setEnabled(true);
        jmiImpCfdiStampSignPending.setEnabled(true);
        jmiImpCfdiSendingLog.setEnabled(true);
        
        // averiguar si debe activarse el menú para el layout del anexo AF02:
        
        boolean hasRightReports = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_REP).HasRight;
        boolean isConfigAF02 = false;
        
        try {
            isConfigAF02 = SLibUtilities.parseInt(SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_HRS_AF02)) == 1;
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
        }
        
        jmRep.setEnabled(hasRightReports || isConfigAF02 || hasRightEmpWage || hasRightEmpVariableEarnings);
        jmiRepPayrollAux.setEnabled(hasRightEmpWage);
        jmiRepPayrollEarningsDeductions.setEnabled(hasRightEmpWage);
        jmiRepPayrollVariableEarnings.setEnabled(hasRightEmpVariableEarnings);
        jmiRepPayrollTax.setEnabled(hasRightEmpWage);
        jmiRepPayrollWageSalaryFileCsv.setEnabled(hasRightEmpWage);
        jmiRepPayrollEarDedFileCsv.setEnabled(hasRightEmpWage);
        jmiRepEmployeeActive.setEnabled(hasRightReports);
        jmiRepHireLogByPeriodActive.setEnabled(hasRightReports);
        jmiRepHireLogByPeriodInactive.setEnabled(hasRightReports);
        jmiRepPtu.setEnabled(hasRightEmpWage);
        jmiRepBenAnnBon.setEnabled(hasRightEmpWage);
        jmiRepBankPayrollDisp.setEnabled(hasRightEmpWage);
        jmiRepAnnexAF02.setEnabled(isConfigAF02);
        jmiRepPositions.setEnabled(hasRightReports);
        
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
    
    private void restoreAccountingSettings() {
        try {
            if (miClient.showMsgBoxConfirm("Este proceso actualizará las configuraciones de contabilización faltantes de percepciones y deducciones,\n"
                    + "así como borrará las configuraciones obsoletas.\n"
                    + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                SGuiUtils.setCursorWait((SGuiClient) miClient);
                SHrsFinUtils.restoreAccountingSettings(miClient.getSession());
                miClient.showMsgBoxInformation(SLibConsts.MSG_PROCESS_FINISHED);
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            SGuiUtils.setCursorDefault((SGuiClient) miClient);
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
        return new JMenu[] { jmCfg, jmCat, jmBen, jmPay, jmSan, jmImp, jmRep };
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
            else if (item == jmiCfgCutoffCalendar) {
                miClient.getSession().showView(SModConsts.HRS_PRE_PAY_CUT_CAL, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgWorkingDaySettings) {
                miClient.getSession().showView(SModConsts.HRS_WDS, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgPaysheetCustomType) {
                miClient.getSession().showView(SModConsts.HRSU_TP_PAY_SHT_CUS, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgEarnings) {
                miClient.getSession().showView(SModConsts.HRS_EAR, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgDeductions) {
                miClient.getSession().showView(SModConsts.HRS_DED, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgAccDepartmentPackCostCenters) {
                miClient.getSession().showView(SModConsts.HRS_CFG_ACC_DEP_PACK_CC, 0, null);
            }
            else if (item == jmiCfgAccEmployeePackCostCenters) {
                miClient.getSession().showView(SModConsts.HRS_CFG_ACC_EMP_PACK_CC, 0, null);
            }
            else if (item == jmiCfgAccEmployeeEarnings) {
                miClient.getSession().showView(SModConsts.HRS_CFG_ACC_EMP_EAR, 0, null);
            }
            else if (item == jmiCfgAccEmployeeDeductions) {
                miClient.getSession().showView(SModConsts.HRS_CFG_ACC_EMP_DED, 0, null);
            }
            else if (item == jmiCfgAccEarnings) {
                miClient.getSession().showView(SModConsts.HRS_CFG_ACC_EAR, 0, null);
            }
            else if (item == jmiCfgAccDeductions) {
                miClient.getSession().showView(SModConsts.HRS_CFG_ACC_DED, 0, null);
            }
            else if (item == jmiCfgAccPackCostCenters) {
                miClient.getSession().showView(SModConsts.HRS_PACK_CC, 0, null);
            }
            else if (item == jmiCfgAccPackCostCentersCostCenters) {
                miClient.getSession().showView(SModConsts.HRS_PACK_CC_CC, 0, null);
            }
            else if (item == jmiCfgAccPackExpenses) {
                miClient.getSession().showView(SModConsts.HRSU_PACK_EXP, 0, null);
            }
            else if (item == jmiCfgAccPackExpensesItems) {
                miClient.getSession().showView(SModConsts.HRSU_PACK_EXP_ITEM, 0, null);
            }
            else if (item == jmiCfgAccExpenseTypeAccounts) {
                miClient.getSession().showView(SModConsts.HRS_TP_EXP_ACC, 0, null);
            }
            else if (item == jmiCfgAccExpenseTypes) {
                miClient.getSession().showView(SModConsts.HRSU_TP_EXP, 0, null);
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
            else if (item == jmiCfgBkkRestoreAccountingSettings) {
                restoreAccountingSettings();
            }
            else if (item == jmiCfgConfig) {
                miClient.getSession().showView(SModConsts.HRS_CFG, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatEmployee) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showView(SDataConstants.BPSX_BP_EMP);
            }
            else if (item == jmiCatEmployeePersonalInfo) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showView(SDataConstants.BPSX_BP_EMP_REL);
            }
            else if (item == jmiCatEmployeeContractsExp) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showView(SDataConstants.BPSX_BP_EMP_CON_EXP);
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
            else if (item == jmiCatEmployeeSua) {
                miClient.getSession().showView(SModConsts.HRSX_EMP_LOG_SUA, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatEmployeeIdse) {
                miClient.getSession().showView(SModConsts.HRSX_EMP_LOG_IDSE, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatPosition) {
                miClient.getSession().showView(SModConsts.HRSU_POS, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatDeparment) {
                miClient.getSession().showView(SModConsts.HRSU_DEP, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatDepartmentCc) {
                miClient.getSession().showView(SModConsts.HRS_DEP_CC, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCatEmployeeCc) {
                miClient.getSession().showView(SModConsts.HRSX_EMP_CC, SLibConsts.UNDEFINED, null);
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
            else if (item == jmiBenAbsence) {
                miClient.getSession().showView(SModConsts.HRS_ABS, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiBenBenefitTables) {
                miClient.getSession().showView(SModConsts.HRS_EMP_BEN, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiBenBenefitVacStat) {
                miClient.getSession().showView(SModConsts.HRSX_BEN_VAC_STAT, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiBenBenefitVac) {
                miClient.getSession().showView(SModConsts.HRSX_BEN_MOV, SModSysConsts.HRSS_TP_BEN_VAC, null);
            }
            else if (item == jmiBenBenefitBonVac) {
                miClient.getSession().showView(SModConsts.HRSX_BEN_MOV, SModSysConsts.HRSS_TP_BEN_VAC_BON, null);
            }
            else if (item == jmiBenBenefitBonAnn) {
                miClient.getSession().showView(SModConsts.HRSX_BEN_MOV, SModSysConsts.HRSS_TP_BEN_ANN_BON, null);
            }
            else if (item == jmiBenBenefitAdjustmentEar) {
                miClient.getSession().showView(SModConsts.HRS_PAY_RCP_EAR, SModConsts.HRS_BEN, null);
            }
            else if (item == jmiBenLoan) {
                miClient.getSession().showView(SModConsts.HRS_LOAN, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiBenLoanAdjustmentEar) {
                miClient.getSession().showView(SModConsts.HRS_PAY_RCP_EAR, SModConsts.HRS_LOAN, null);
            }
            else if (item == jmiBenLoanAdjustmentDed) {
                miClient.getSession().showView(SModConsts.HRS_PAY_RCP_DED, SModConsts.HRS_LOAN, null);
            }
            else if (item == jmiBenAdvanceSettlement) {
                miClient.getSession().showView(SModConsts.HRS_ADV_SET, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiPayPayrollWeek) {
                miClient.getSession().showView(SModConsts.HRS_PAY, SModSysConsts.HRSS_TP_PAY_WEE, null);
            }
            else if (item == jmiPayPayrollWeekRec) {
                miClient.getSession().showView(SModConsts.HRS_PAY_RCP, SModSysConsts.HRSS_TP_PAY_WEE, null);
            }
            else if (item == jmiPayPayrollFortnight) {
                miClient.getSession().showView(SModConsts.HRS_PAY, SModSysConsts.HRSS_TP_PAY_FOR, null);
            }
            else if (item == jmiPayPayrollFortnightRec) {
                miClient.getSession().showView(SModConsts.HRS_PAY_RCP, SModSysConsts.HRSS_TP_PAY_FOR, null);
            }
            else if (item == jmiPayPayrollRecImportedEarWeek) {
                miClient.getSession().showView(SModConsts.HRS_PAY_RCP_IMPORT, SModSysConsts.HRSS_TP_PAY_WEE, null);
            }
            else if (item == jmiPayPayrollRecImportedEarFortnight) {
                miClient.getSession().showView(SModConsts.HRS_PAY_RCP_IMPORT, SModSysConsts.HRSS_TP_PAY_FOR, null);
            }
            else if (item == jmiPayCfdiPayroll) {
                miClient.getSession().showView(SModConsts.HRS_SIE_PAY, SModConsts.VIEW_SC_SUM, new SGuiParams(SCfdConsts.CFDI_PAYROLL_VER_CUR));
            }
            else if (item == jmiPayCfdiPayrollRec) {
                miClient.getSession().showView(SModConsts.HRS_SIE_PAY, SModConsts.VIEW_SC_DET, new SGuiParams(SCfdConsts.CFDI_PAYROLL_VER_CUR));
            }
            else if (item == jmiCfdiMassiveValidation) {
                new SFormCfdiMassiveValidation(miClient, SDataConstants.MOD_HRS, SDataConstants.UNDEFINED).setVisible(true);
            }
            else if (item == jmiUuidSearch) {
                new SDialogSearchCfdiByUuid((SGuiClient) miClient, SDataConstantsSys.TRNS_TP_CFD_PAYROLL, SLibConstants.UNDEFINED).setVisible(true);
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
            else if (item == jmiPayPayrollBkkRecord) {
                miClient.getSession().showView(SModConsts.HRSX_PAY_REC, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiPayConditionalEarnings) {
                miClient.getSession().showView(SModConsts.HRS_COND_EAR, SLibConsts.UNDEFINED, null);
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
            else if (item == jmiPayReissueCfdis) {
                new SDialogReissueCfdis((SGuiClient) miClient, "Reexpedición de recibos").setFormVisible(true);
            }
            else if (item == jmiPayVerifyCfdis) {
                new SDialogVerifyCfdis((SGuiClient) miClient, "Verificación de CFDIs").setFormVisible(true);
            }
            else if (item == jmiSanDocBreach) {
                miClient.getSession().showView(SModConsts.HRS_DOC_BREACH, 0, null);
            }
            else if (item == jmiSanDocBreachSum) {
                miClient.getSession().showView(SModConsts.HRSX_DOC_BREACH_SUM, 0, null);
            }
            else if (item == jmiSanDocAdminRecord) {
                miClient.getSession().showView(SModConsts.HRS_DOC_ADM_REC, 0, null);
            }
            else if (item == jmiSanDocAdminRecordSum) {
                miClient.getSession().showView(SModConsts.HRSX_DOC_ADM_REC_SUM, 0, null);
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
            else if (item == jmiRepPayrollAux) {
               new SDialogRepHrsAuxPayroll((SGuiClient) miClient, "Auxiliares de nóminas").setFormVisible(true);
            }
            else if (item == jmiRepPayrollEarningsDeductions) {
                new SDialogRepHrsEarningDeduction((SGuiClient) miClient, "Percepciones y deducciones por período").setFormVisible(true);
            }
            else if (item == jmiRepPayrollVariableEarnings) {
                new SDialogRepHrsEarningDeduction((SGuiClient) miClient, "Percepciones variables por período", true).setFormVisible(true);
            }
            else if (item == jmiRepPayrollTax) {
                new SDialogRepHrsPayrollTax((SGuiClient) miClient, "Impuesto sobre nóminas").setFormVisible(true);
            }
            else if (item == jmiRepPayrollWageSalaryFileCsv) {
                new SDialogRepHrsPayrollWageSalaryFileCsv((SGuiClient) miClient, "Archivo CSV para declaración informativa de sueldos y salarios").setFormVisible(true);
            }
            else if (item == jmiRepPayrollEarDedFileCsv) {
                new SDialogRepHrsEarningsDeductionsFileCsv((SGuiClient) miClient, "Archivo CSV de percepciones y deducciones en el ejercicio").setFormVisible(true);
            }
            else if (item == jmiRepEmployeeActive) {
                new SDialogRepHrsActiveEmployees((SGuiClient) miClient, "Reporte de empleados activos por período").setFormVisible(true);
            }
            else if (item == jmiRepHireLogByPeriodActive) {
                miClient.getSession().showView(SModConsts.HRSX_EMP_LOG_HIRE_BY_PER, SViewEmployeeHireLogByPeriod.GRID_SUBTYPE_HIRE, null);
            }
            else if (item == jmiRepHireLogByPeriodInactive) {
                miClient.getSession().showView(SModConsts.HRSX_EMP_LOG_HIRE_BY_PER, SViewEmployeeHireLogByPeriod.GRID_SUBTYPE_DISMISS, null);
            }
            else if (item == jmiRepPtu) {
                miClient.getSession().showView(SModConsts.HRSX_PTU, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiRepBenAnnBon) {
                miClient.getSession().showView(SModConsts.HRSX_BEN_ANN_BON, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiRepBankPayrollDisp) {
                miClient.getSession().showView(SModConsts.HRSX_BANK_PAY_DISP, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiRepAnnexAF02) {
                new SDialogLayoutAF02((SGuiClient) miClient, "Layout AF02").setFormVisible(true);
            }
            else if (item == jmiRepPositions) {
                new SDialogRepHrsPos((SGuiClient) miClient, "Reporte de posiciones y vacantes").setFormVisible(true);
            }
        }
    }
}
