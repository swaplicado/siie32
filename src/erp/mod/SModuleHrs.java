/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod;

import erp.cfd.SCfdConsts;
import erp.mod.hrs.db.SDbAbsence;
import erp.mod.hrs.db.SDbAbsenceClass;
import erp.mod.hrs.db.SDbAbsenceConsumption;
import erp.mod.hrs.db.SDbAbsenceType;
import erp.mod.hrs.db.SDbAccountingDeduction;
import erp.mod.hrs.db.SDbAccountingEarning;
import erp.mod.hrs.db.SDbAccountingPayroll;
import erp.mod.hrs.db.SDbAccountingPayrollReceipt;
import erp.mod.hrs.db.SDbAdvanceSettlement;
import erp.mod.hrs.db.SDbAutomaticDeduction;
import erp.mod.hrs.db.SDbAutomaticDeductionsAux;
import erp.mod.hrs.db.SDbAutomaticEarning;
import erp.mod.hrs.db.SDbAutomaticEarningsAux;
import erp.mod.hrs.db.SDbBenefitTable;
import erp.mod.hrs.db.SDbBenefitTableRow;
import erp.mod.hrs.db.SDbCfgAccountingDeduction;
import erp.mod.hrs.db.SDbCfgAccountingDepartment;
import erp.mod.hrs.db.SDbCfgAccountingDepartmentPackCostCenters;
import erp.mod.hrs.db.SDbCfgAccountingEarning;
import erp.mod.hrs.db.SDbCfgAccountingEmployeeDeduction;
import erp.mod.hrs.db.SDbCfgAccountingEmployeeEarning;
import erp.mod.hrs.db.SDbCfgAccountingEmployeePackCostCenters;
import erp.mod.hrs.db.SDbConditionalEarning;
import erp.mod.hrs.db.SDbConfig;
import erp.mod.hrs.db.SDbDeduction;
import erp.mod.hrs.db.SDbDepartment;
import erp.mod.hrs.db.SDbDepartmentCostCenter;
import erp.mod.hrs.db.SDbDocAdminRecord;
import erp.mod.hrs.db.SDbDocAdminRecordPreceptSubsection;
import erp.mod.hrs.db.SDbDocBreach;
import erp.mod.hrs.db.SDbDocBreachPreceptSubsection;
import erp.mod.hrs.db.SDbEarning;
import erp.mod.hrs.db.SDbEmployee;
import erp.mod.hrs.db.SDbEmployeeBenefitTables;
import erp.mod.hrs.db.SDbEmployeeBenefitTablesAnnum;
import erp.mod.hrs.db.SDbEmployeeDismissalType;
import erp.mod.hrs.db.SDbEmployeeHireLog;
import erp.mod.hrs.db.SDbEmployeeType;
import erp.mod.hrs.db.SDbEmployeeWageFactorAnnum;
import erp.mod.hrs.db.SDbEmployeeWageLog;
import erp.mod.hrs.db.SDbEmployeeWageSscBaseLog;
import erp.mod.hrs.db.SDbExpenseType;
import erp.mod.hrs.db.SDbExpenseTypeAccount;
import erp.mod.hrs.db.SDbFirstDayYear;
import erp.mod.hrs.db.SDbHoliday;
import erp.mod.hrs.db.SDbLoan;
import erp.mod.hrs.db.SDbLoanTypeAdjustment;
import erp.mod.hrs.db.SDbMwzType;
import erp.mod.hrs.db.SDbMwzTypeWage;
import erp.mod.hrs.db.SDbPackCostCenters;
import erp.mod.hrs.db.SDbPackCostCentersCostCenter;
import erp.mod.hrs.db.SDbPackExpenses;
import erp.mod.hrs.db.SDbPackExpensesItem;
import erp.mod.hrs.db.SDbPayroll;
import erp.mod.hrs.db.SDbPayrollReceipt;
import erp.mod.hrs.db.SDbPayrollReceiptDeduction;
import erp.mod.hrs.db.SDbPayrollReceiptEarning;
import erp.mod.hrs.db.SDbPayrollReceiptIssue;
import erp.mod.hrs.db.SDbPaysheetCustomType;
import erp.mod.hrs.db.SDbPosition;
import erp.mod.hrs.db.SDbPrePayrollCutoffCalendar;
import erp.mod.hrs.db.SDbPrecept;
import erp.mod.hrs.db.SDbPreceptSection;
import erp.mod.hrs.db.SDbPreceptSubsection;
import erp.mod.hrs.db.SDbShift;
import erp.mod.hrs.db.SDbSsContributionTable;
import erp.mod.hrs.db.SDbSsContributionTableRow;
import erp.mod.hrs.db.SDbTaxSubsidyTable;
import erp.mod.hrs.db.SDbTaxSubsidyTableRow;
import erp.mod.hrs.db.SDbTaxTable;
import erp.mod.hrs.db.SDbTaxTableRow;
import erp.mod.hrs.db.SDbUma;
import erp.mod.hrs.db.SDbUmi;
import erp.mod.hrs.db.SDbWorkerType;
import erp.mod.hrs.db.SDbWorkerTypeSalary;
import erp.mod.hrs.db.SDbWorkingDaySettings;
import erp.mod.hrs.form.SFormAbsence;
import erp.mod.hrs.form.SFormAbsenceClass;
import erp.mod.hrs.form.SFormAbsenceType;
import erp.mod.hrs.form.SFormAccountingDeduction;
import erp.mod.hrs.form.SFormAccountingEarning;
import erp.mod.hrs.form.SFormAdvanceSettlement;
import erp.mod.hrs.form.SFormAutomaticDeductions;
import erp.mod.hrs.form.SFormAutomaticEarnings;
import erp.mod.hrs.form.SFormBenefitAdjustmentEarning;
import erp.mod.hrs.form.SFormBenefitTable;
import erp.mod.hrs.form.SFormCfgAccountingDeduction;
import erp.mod.hrs.form.SFormCfgAccountingDepartmentPackCostCenters;
import erp.mod.hrs.form.SFormCfgAccountingEarning;
import erp.mod.hrs.form.SFormCfgAccountingEmployeeDeduction;
import erp.mod.hrs.form.SFormCfgAccountingEmployeeEarning;
import erp.mod.hrs.form.SFormCfgAccountingEmployeePackCostCenters;
import erp.mod.hrs.form.SFormConditionalEarning;
import erp.mod.hrs.form.SFormConfig;
import erp.mod.hrs.form.SFormCutoffCalendar;
import erp.mod.hrs.form.SFormDeduction;
import erp.mod.hrs.form.SFormDepartment;
import erp.mod.hrs.form.SFormDepartmentCostCenter;
import erp.mod.hrs.form.SFormDocAdminRecord;
import erp.mod.hrs.form.SFormDocBreach;
import erp.mod.hrs.form.SFormEarning;
import erp.mod.hrs.form.SFormEmployeeDismissalType;
import erp.mod.hrs.form.SFormEmployeeType;
import erp.mod.hrs.form.SFormExpenseType;
import erp.mod.hrs.form.SFormExpenseTypeAccount;
import erp.mod.hrs.form.SFormFirstDayYear;
import erp.mod.hrs.form.SFormHoliday;
import erp.mod.hrs.form.SFormLoan;
import erp.mod.hrs.form.SFormLoanAdjustmentDeduction;
import erp.mod.hrs.form.SFormLoanAdjustmentEarning;
import erp.mod.hrs.form.SFormLoanTypeAdjustment;
import erp.mod.hrs.form.SFormMwzType;
import erp.mod.hrs.form.SFormMwzTypeWage;
import erp.mod.hrs.form.SFormPackCostCenters;
import erp.mod.hrs.form.SFormPackExpenses;
import erp.mod.hrs.form.SFormPayroll;
import erp.mod.hrs.form.SFormPaysheetCustomType;
import erp.mod.hrs.form.SFormPosition;
import erp.mod.hrs.form.SFormShift;
import erp.mod.hrs.form.SFormSsContributionTable;
import erp.mod.hrs.form.SFormTaxSubsidyTable;
import erp.mod.hrs.form.SFormTaxTable;
import erp.mod.hrs.form.SFormUma;
import erp.mod.hrs.form.SFormUmi;
import erp.mod.hrs.form.SFormWorkerType;
import erp.mod.hrs.form.SFormWorkerTypeSalary;
import erp.mod.hrs.form.SFormWorkingDaySettings;
import erp.mod.hrs.view.SViewAbsence;
import erp.mod.hrs.view.SViewAbsenceClass;
import erp.mod.hrs.view.SViewAbsenceType;
import erp.mod.hrs.view.SViewAccountingDeduction;
import erp.mod.hrs.view.SViewAccountingEarning;
import erp.mod.hrs.view.SViewAdvanceSettlement;
import erp.mod.hrs.view.SViewAutomaticDeductions;
import erp.mod.hrs.view.SViewAutomaticEarnings;
import erp.mod.hrs.view.SViewBankPayDispersion;
import erp.mod.hrs.view.SViewBenAnnualBon;
import erp.mod.hrs.view.SViewBenefit;
import erp.mod.hrs.view.SViewBenefitTable;
import erp.mod.hrs.view.SViewBenefitTableRow;
import erp.mod.hrs.view.SViewBenefitVacationStatus;
import erp.mod.hrs.view.SViewCfgAccountingDeduction;
import erp.mod.hrs.view.SViewCfgAccountingDepartmentPackCostCenters;
import erp.mod.hrs.view.SViewCfgAccountingEarning;
import erp.mod.hrs.view.SViewCfgAccountingEmployeeDeduction;
import erp.mod.hrs.view.SViewCfgAccountingEmployeeEarning;
import erp.mod.hrs.view.SViewCfgAccountingEmployeePackCostCenters;
import erp.mod.hrs.view.SViewConditionalEarning;
import erp.mod.hrs.view.SViewConfig;
import erp.mod.hrs.view.SViewDeduction;
import erp.mod.hrs.view.SViewDepartment;
import erp.mod.hrs.view.SViewDepartmentCostCenter;
import erp.mod.hrs.view.SViewDocAdminRecord;
import erp.mod.hrs.view.SViewDocAdminRecordSummary;
import erp.mod.hrs.view.SViewDocBreach;
import erp.mod.hrs.view.SViewDocBreachSummary;
import erp.mod.hrs.view.SViewEarning;
import erp.mod.hrs.view.SViewEmployeeBenefitTables;
import erp.mod.hrs.view.SViewEmployeeDismissalType;
import erp.mod.hrs.view.SViewEmployeeHireLog;
import erp.mod.hrs.view.SViewEmployeeHireLogByPeriod;
import erp.mod.hrs.view.SViewEmployeeIdse;
import erp.mod.hrs.view.SViewEmployeeSua;
import erp.mod.hrs.view.SViewEmployeeType;
import erp.mod.hrs.view.SViewEmployeeWageLog;
import erp.mod.hrs.view.SViewEmployeeWageSscBaseLog;
import erp.mod.hrs.view.SViewEmployeesCc;
import erp.mod.hrs.view.SViewExpenseType;
import erp.mod.hrs.view.SViewExpenseTypeAccount;
import erp.mod.hrs.view.SViewFirstDayYear;
import erp.mod.hrs.view.SViewHoliday;
import erp.mod.hrs.view.SViewLoan;
import erp.mod.hrs.view.SViewLoanTypeAdjustment;
import erp.mod.hrs.view.SViewMwzType;
import erp.mod.hrs.view.SViewMwzTypeWage;
import erp.mod.hrs.view.SViewPackCostCenters;
import erp.mod.hrs.view.SViewPackCostCentersCostCenter;
import erp.mod.hrs.view.SViewPackExpenses;
import erp.mod.hrs.view.SViewPackExpensesItem;
import erp.mod.hrs.view.SViewPayroll;
import erp.mod.hrs.view.SViewPayrollBenefitEarningComplement;
import erp.mod.hrs.view.SViewPayrollCfdi;
import erp.mod.hrs.view.SViewPayrollLoanDeductionComplement;
import erp.mod.hrs.view.SViewPayrollLoanEarningComplement;
import erp.mod.hrs.view.SViewPayrollReceipt;
import erp.mod.hrs.view.SViewPayrollReceiptImportedEarnings;
import erp.mod.hrs.view.SViewPayrollReceiptRecord;
import erp.mod.hrs.view.SViewPaysheetCustomType;
import erp.mod.hrs.view.SViewPosition;
import erp.mod.hrs.view.SViewPrePayrollCutoffCalendar;
import erp.mod.hrs.view.SViewPtu;
import erp.mod.hrs.view.SViewShift;
import erp.mod.hrs.view.SViewSsContributionTable;
import erp.mod.hrs.view.SViewSsContributionTableRow;
import erp.mod.hrs.view.SViewTaxSubsidyTable;
import erp.mod.hrs.view.SViewTaxSubsidyTableRow;
import erp.mod.hrs.view.SViewTaxTable;
import erp.mod.hrs.view.SViewTaxTableRow;
import erp.mod.hrs.view.SViewUma;
import erp.mod.hrs.view.SViewUmi;
import erp.mod.hrs.view.SViewWorkerType;
import erp.mod.hrs.view.SViewWorkerTypeSalary;
import erp.mod.hrs.view.SViewWorkingDaySettings;
import java.util.ArrayList;
import javax.swing.JMenu;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistrySysFly;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiCatalogueSettings;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiForm;
import sa.lib.gui.SGuiModule;
import sa.lib.gui.SGuiOptionPicker;
import sa.lib.gui.SGuiOptionPickerSettings;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiReport;
import sa.lib.gui.bean.SBeanOptionPicker;

/**
 *
 * @author Juan Barajas, Edwin Carmona, Sergio Flores, Claudio Peña, Isabel Servín, Sergio Flores
 */
public class SModuleHrs extends SGuiModule {

    private SFormAbsenceClass moFormAbsenceClass;
    private SFormAbsenceType moFormAbsenceType;
    private SFormEmployeeDismissalType moFormEmployeeDismissalType;
    private SFormEmployeeType moFormEmployeeType;
    private SFormWorkerType moFormWorkerType;
    private SFormMwzType moFormMwzType;
    private SFormDepartment moFormDepartment;
    private SFormDepartmentCostCenter moFormDepartmentCenterCost;;
    private SFormPosition moFormPosition;
    private SFormShift moFormShift;
    private SFormExpenseType moFormExpenseType;
    private SFormPackExpenses moFormPackExpenses;
    private SFormPackCostCenters moFormPackCostCenters;
    private SFormConfig moFormConfig;
    private SFormWorkingDaySettings moFormWorkingDaySettings;
    private SFormConditionalEarning moFormConditionalEarning;
    private SFormCutoffCalendar moFormCutoffCalendar;
    private SFormFirstDayYear moFormFirstDayYear;
    private SFormHoliday moFormHoliday;
    private SFormTaxTable moFormTaxTable;
    private SFormTaxSubsidyTable moFormTaxSubsidyTable;
    private SFormSsContributionTable moFormSsContributionTable;
    private SFormBenefitTable moFormBenefitTable;
    private SFormWorkerTypeSalary moFormWorkerTypeSalary;
    private SFormMwzTypeWage moFormMwzTypeWage;
    private SFormPaysheetCustomType moForPaysheetCustomType;
    private SFormUma moFormUma;
    private SFormUmi moFormUmi;
    private SFormLoanTypeAdjustment moFormLoanTypeAdjustment;
    private SFormLoan moFormLoan;
    private SFormAbsence moFormAbsence;
    private SFormEarning moFormEarning;
    private SFormDeduction moFormDeduction;
    private SFormAutomaticEarnings moFormAutomaticEarningsGbl;
    private SFormAutomaticEarnings moFormAutomaticEarningsEmp;
    private SFormAutomaticDeductions moFormAutomaticDeductionsGbl;
    private SFormAutomaticDeductions moFormAutomaticDeductionsEmp;
    private SFormAccountingEarning moFormAccountingEarningGlobal;
    private SFormAccountingEarning moFormAccountingEarningDepartament;
    private SFormAccountingEarning moFormAccountingEarningEmployee;
    private SFormAccountingDeduction moFormAccountingDeductionGlobal;
    private SFormAccountingDeduction moFormAccountingDeductionDepartament;
    private SFormAccountingDeduction moFormAccountingDeductionEmployee;
    private SFormExpenseTypeAccount moFormExpenseTypeAccount;
    private SFormCfgAccountingDepartmentPackCostCenters moFormCfgAccountingDepartmentPackCostCenters;
    private SFormCfgAccountingEmployeePackCostCenters moFormCfgAccountingEmployeePackCostCenters;
    private SFormCfgAccountingEmployeeEarning moFormCfgAccountingEmployeeEarning;
    private SFormCfgAccountingEmployeeDeduction moFormCfgAccountingEmployeeDeduction;
    private SFormCfgAccountingEarning moFormCfgAccountingEarning;
    private SFormCfgAccountingDeduction moFormCfgAccountingDeduction;
    private SFormPayroll moFormPayrollWeekly;
    private SFormPayroll moFormPayrollFortnightly;
    private SFormAdvanceSettlement moAdvanceSettlement;
    private SFormBenefitAdjustmentEarning moFormBenefitAdjustmentEarning;
    private SFormLoanAdjustmentDeduction moFormLoanAdjustmentDeduction;
    private SFormLoanAdjustmentEarning moFormLoanAdjustmentEarning;
    private SFormDocBreach moFormDocBreach;
    private SFormDocAdminRecord moFormDocAdminRecord;

    private SBeanOptionPicker moPickerEarnings;
    private SBeanOptionPicker moPickerDeductions;
    private SBeanOptionPicker moPickerEmployees;

    public SModuleHrs(SGuiClient client) {
        super(client, SModConsts.MOD_HRS_N, SLibConsts.UNDEFINED);
        moModuleIcon = miClient.getImageIcon(mnModuleType);
    }

    @Override
    public JMenu[] getMenus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry getRegistry(final int type, final SGuiParams params) {
        SDbRegistry registry = null;

        switch (type) {
            case SModConsts.HRSS_CL_HRS_CAT:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_cl_hrs_cat = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_HRS_CAT:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_cl_hrs_cat = " + pk[0] + " AND id_tp_hrs_cat = " + pk[1] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_PAY:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_pay = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_PAY_SHT:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_pay_sht = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_SAL:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_sal = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_ACC:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_acc = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_EAR_COMP:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_ear_comp = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_EAR_EXEM:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_ear_exem = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_EAR:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_ear = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_OTH_PAY:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_oth_pay = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_DED_COMP:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_ded_comp = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_DED:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_ded = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_BEN:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_ben = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_LOAN:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_loan = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_LOAN_PAY:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_loan_pay = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_CON:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_con = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_REC_SCHE:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_rec_sche = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_POS_RISK:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_pos_risk = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_TP_WORK_DAY:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_work_day = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_BANK:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_bank = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_BONUS:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_bonus = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSS_GROCERY_SRV:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_grocery_srv = " + pk[0] + " "; }
                };
                break;
            case SModConsts.HRSU_CL_ABS:
                registry = new SDbAbsenceClass();
                break;
            case SModConsts.HRSU_TP_ABS:
                registry = new SDbAbsenceType();
                break;
            case SModConsts.HRSU_TP_EMP_DIS:
                registry = new SDbEmployeeDismissalType();
                break;
            case SModConsts.HRSU_TP_EMP:
                registry = new SDbEmployeeType();
                break;
            case SModConsts.HRSU_TP_WRK:
                registry = new SDbWorkerType();
                break;
            case SModConsts.HRSU_TP_MWZ:
                registry = new SDbMwzType();
                break;
            case SModConsts.HRSU_TP_PAY_SHT_CUS:
                registry = new SDbPaysheetCustomType();
                break;
            case SModConsts.HRSU_DEP:
                registry = new SDbDepartment();
                break;
            case SModConsts.HRSU_POS:
                registry = new SDbPosition();
                break;
            case SModConsts.HRSU_SHT:
                registry = new SDbShift();
                break;
            case SModConsts.HRSU_TP_EXP:
                registry = new SDbExpenseType();
                break;
            case SModConsts.HRSU_PACK_EXP:
                registry = new SDbPackExpenses();
                break;
            case SModConsts.HRSU_PACK_EXP_ITEM:
                registry = new SDbPackExpensesItem();
                break;
            case SModConsts.HRS_DEP_CC:
                registry = new SDbDepartmentCostCenter();
                break;
            case SModConsts.HRSU_EMP:
                registry = new SDbEmployee();
                break;
            case SModConsts.HRS_CFG:
                registry = new SDbConfig();
                break;
            case SModConsts.HRS_FDY:
                registry = new SDbFirstDayYear();
                break;
            case SModConsts.HRS_HOL:
                registry = new SDbHoliday();
                break;
            case SModConsts.HRS_WDS:
                registry = new SDbWorkingDaySettings();
                break;
            case SModConsts.HRS_COND_EAR:
                registry = new SDbConditionalEarning();
                break;  
            case SModConsts.HRS_PRE_PAY_CUT_CAL:
                registry = new SDbPrePayrollCutoffCalendar();
                break;
            case SModConsts.HRS_TAX:
                registry = new SDbTaxTable();
                break;
            case SModConsts.HRS_TAX_ROW:
                registry = new SDbTaxTableRow();
                break;
            case SModConsts.HRS_TAX_SUB:
                registry = new SDbTaxSubsidyTable();
                break;
            case SModConsts.HRS_TAX_SUB_ROW:
                registry = new SDbTaxSubsidyTableRow();
                break;
            case SModConsts.HRS_SSC:
                registry = new SDbSsContributionTable();
                break;
            case SModConsts.HRS_SSC_ROW:
                registry = new SDbSsContributionTableRow();
                break;
            case SModConsts.HRS_BEN:
                registry = new SDbBenefitTable();
                break;
            case SModConsts.HRS_BEN_ROW:
                registry = new SDbBenefitTableRow();
                break;
            case SModConsts.HRS_BEN_ROW_AUX:
                registry = null;
                break;
            case SModConsts.HRS_WRK_SAL:
                registry = new SDbWorkerTypeSalary();
                break;
            case SModConsts.HRS_MWZ_WAGE:
                registry = new SDbMwzTypeWage();
                break;
            case SModConsts.HRS_UMA:
                registry = new SDbUma();
                break;
            case SModConsts.HRS_UMI:
                registry = new SDbUmi();
                break;
            case SModConsts.HRS_TP_LOAN_ADJ:
                registry = new SDbLoanTypeAdjustment();
                break;
            case SModConsts.HRS_EMP_LOG_HIRE:
                registry = new SDbEmployeeHireLog();
                break;
            case SModConsts.HRS_EMP_LOG_WAGE:
                registry = new SDbEmployeeWageLog();
                break;
            case SModConsts.HRS_EMP_LOG_SAL_SSC:
                registry = new SDbEmployeeWageSscBaseLog();
                break;
            case SModConsts.HRS_EMP_BEN:
                registry = new SDbEmployeeBenefitTables();
                break;
            case SModConsts.HRS_EMP_BEN_ANN:
                registry = new SDbEmployeeBenefitTablesAnnum();
                break;
            case SModConsts.HRS_EMP_WAGE_FAC_ANN:
                registry = new SDbEmployeeWageFactorAnnum();
                break;
            case SModConsts.HRS_LOAN:
                registry = new SDbLoan();
                break;
            case SModConsts.HRS_ABS:
                registry = new SDbAbsence();
                break;
            case SModConsts.HRS_ABS_CNS:
                registry = new SDbAbsenceConsumption();
                break;
            case SModConsts.HRS_EAR:
                registry = new SDbEarning();
                break;
            case SModConsts.HRS_DED:
                registry = new SDbDeduction();
                break;
            case SModConsts.HRS_AUT_EAR:
                registry = new SDbAutomaticEarning();
                break;
            case SModConsts.HRS_AUT_DED:
                registry = new SDbAutomaticDeduction();
                break;
            case SModConsts.HRSX_AUT_EAR:
                registry = new SDbAutomaticEarningsAux();
                break;
            case SModConsts.HRSX_AUT_DED:
                registry = new SDbAutomaticDeductionsAux();
                break;
            case SModConsts.HRS_ACC_EAR:
                registry = new SDbAccountingEarning();
                break;
            case SModConsts.HRS_ACC_DED:
                registry = new SDbAccountingDeduction();
                break;
            case SModConsts.HRS_TP_EXP_ACC:
                registry = new SDbExpenseTypeAccount();
                break;
            case SModConsts.HRS_PACK_CC:
                registry = new SDbPackCostCenters();
                break;
            case SModConsts.HRS_PACK_CC_CC:
                registry = new SDbPackCostCentersCostCenter();
                break;
            case SModConsts.HRS_CFG_ACC_DEP:
                registry = new SDbCfgAccountingDepartment();
                break;
            case SModConsts.HRS_CFG_ACC_DEP_PACK_CC:
                registry = new SDbCfgAccountingDepartmentPackCostCenters();
                break;
            case SModConsts.HRS_CFG_ACC_EMP_PACK_CC:
                registry = new SDbCfgAccountingEmployeePackCostCenters();
                break;
            case SModConsts.HRS_CFG_ACC_EMP_EAR:
                registry = new SDbCfgAccountingEmployeeEarning();
                break;
            case SModConsts.HRS_CFG_ACC_EMP_DED:
                registry = new SDbCfgAccountingEmployeeDeduction();
                break;
            case SModConsts.HRS_CFG_ACC_EAR:
                registry = new SDbCfgAccountingEarning();
                break;
            case SModConsts.HRS_CFG_ACC_DED:
                registry = new SDbCfgAccountingDeduction();
                break;
            case SModConsts.HRS_PAY:
                registry = new SDbPayroll();
                break;
            case SModConsts.HRS_PAY_RCP:
                registry = new SDbPayrollReceipt();
                break;
            case SModConsts.HRS_PAY_RCP_ISS:
                registry = new SDbPayrollReceiptIssue();
                break;
            case SModConsts.HRS_PAY_RCP_EAR:
                registry = new SDbPayrollReceiptEarning();
                break;
            case SModConsts.HRS_PAY_RCP_DED:
                registry = new SDbPayrollReceiptDeduction();
                break;
            case SModConsts.HRS_ACC_PAY:
                registry = new SDbAccountingPayroll();
                break;
            case SModConsts.HRS_ACC_PAY_RCP:
                registry = new SDbAccountingPayrollReceipt();
                break;
            case SModConsts.HRS_ADV_SET:
                registry = new SDbAdvanceSettlement();
                break;
            case SModConsts.HRS_PREC:
                registry = new SDbPrecept();
                break;
            case SModConsts.HRS_PREC_SEC:
                registry = new SDbPreceptSection();
                break;
            case SModConsts.HRS_PREC_SUBSEC:
                registry = new SDbPreceptSubsection();
                break;
            case SModConsts.HRS_DOC_BREACH:
                registry = new SDbDocBreach();
                break;
            case SModConsts.HRS_DOC_BREACH_PREC_SUBSEC:
                registry = new SDbDocBreachPreceptSubsection();
                break;
            case SModConsts.HRS_DOC_ADM_REC:
                registry = new SDbDocAdminRecord();
                break;
            case SModConsts.HRS_DOC_ADM_REC_PREC_SUBSEC:
                registry = new SDbDocAdminRecordPreceptSubsection();
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public SGuiCatalogueSettings getCatalogueSettings(final int type, final int subtype, final SGuiParams params) {
        String sql = "";
        String aux = "";
        SGuiCatalogueSettings settings = null;

        switch (type) {
            case SModConsts.HRSS_CL_HRS_CAT:
                settings = new SGuiCatalogueSettings("Clase catálogo recursos humanos", 1);
                sql = "SELECT id_cl_hrs_cat AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_HRS_CAT:
                settings = new SGuiCatalogueSettings("Tipo catálogo recursos humanos", 2, 1);
                sql = "SELECT id_cl_hrs_cat AS " + SDbConsts.FIELD_ID + "1, id_tp_hrs_cat AS " + SDbConsts.FIELD_ID + "2, name AS " + SDbConsts.FIELD_ITEM + ", "
                        + "id_cl_hrs_cat AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + (params != null && params.getKey() != null ? " AND id_cl_hrs_cat = " + params.getKey()[0] : "") + " " 
                        + "ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_PAY:
                settings = new SGuiCatalogueSettings("Tipo período pago", 1);
                sql = "SELECT id_tp_pay AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_SAL:
                settings = new SGuiCatalogueSettings("Tipo salario", 1);
                sql = "SELECT id_tp_sal AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_ACC:
                settings = new SGuiCatalogueSettings("Tipo contabilización", 1);
                sql = "SELECT id_tp_acc AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_TAX_COMP:
                settings = new SGuiCatalogueSettings("Tipo cálculo impuestos", 1);
                sql = "SELECT id_tp_tax_comp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_EAR_COMP:
                settings = new SGuiCatalogueSettings("Tipo cálculo percepción", 1);
                sql = "SELECT id_tp_ear_comp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_EAR_EXEM:
                settings = new SGuiCatalogueSettings("Tipo exención percepción", 1);
                sql = "SELECT id_tp_ear_exem AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + (params != null && params.getKey() != null ? " AND id_tp_ear_exem IN ( " + SModSysConsts.HRSS_TP_EAR_EXEM_NA + ", " + params.getKey()[0] + ")" : "") + " " 
                        + "ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_EAR:
                settings = new SGuiCatalogueSettings("Tipo percepción", 1);
                sql = "SELECT id_tp_ear AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_OTH_PAY:
                settings = new SGuiCatalogueSettings("Tipo otro pago", 1);
                sql = "SELECT id_tp_oth_pay AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_DED_COMP:
                settings = new SGuiCatalogueSettings("Tipo cálculo deducción", 1);
                sql = "SELECT id_tp_ded_comp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_DED:
                settings = new SGuiCatalogueSettings("Tipo deducción", 1);
                sql = "SELECT id_tp_ded AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_BEN:
                settings = new SGuiCatalogueSettings("Tipo prestación", 1);
                sql = "SELECT id_tp_ben AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + (subtype == SModConsts.HRS_BEN ? " AND id_tp_ben > " + SModSysConsts.HRSS_TP_BEN_NA : "") + " "
                        + "ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_LOAN:
                settings = new SGuiCatalogueSettings("Tipo crédito/préstamo", 1);
                sql = "SELECT id_tp_loan AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + (subtype == SModConsts.HRS_LOAN ? " AND id_tp_loan > " + SModSysConsts.HRSS_TP_LOAN_NA : "") + " "
                        + "ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_LOAN_PAY:
                settings = new SGuiCatalogueSettings("Tipo pago", 1);
                sql = "SELECT id_tp_loan_pay AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_CON:
                settings = new SGuiCatalogueSettings("Tipo contrato", 1);
                sql = "SELECT id_tp_con AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_REC_SCHE:
                settings = new SGuiCatalogueSettings("Régimen contratación", 1);
                sql = "SELECT id_tp_rec_sche AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_POS_RISK:
                settings = new SGuiCatalogueSettings("Riesgo trabajo", 1);
                sql = "SELECT id_tp_pos_risk AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_WORK_DAY:
                settings = new SGuiCatalogueSettings("Tipo jornada", 1);
                sql = "SELECT id_tp_work_day AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_DIS:
                settings = new SGuiCatalogueSettings("Tipo incapacidad", 1);
                sql = "SELECT id_tp_dis AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_TP_DAY:
                settings = new SGuiCatalogueSettings("Tipo día", 1);
                sql = "SELECT id_tp_day AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_BANK:
                settings = new SGuiCatalogueSettings("Banco", 1);
                sql = "SELECT id_bank AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_BONUS:
                settings = new SGuiCatalogueSettings("Bono", 1);
                sql = "SELECT id_bonus AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSS_GROCERY_SRV:
                settings = new SGuiCatalogueSettings("Proveedor despensa", 1);
                sql = "SELECT id_grocery_srv AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.HRSU_CL_ABS:
                settings = new SGuiCatalogueSettings("Clase incidencia", 1);
                sql = "SELECT id_cl_abs AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_cl_abs ";
                break;
            case SModConsts.HRSU_TP_ABS:
                settings = new SGuiCatalogueSettings("Tipo incidencia", 2, 1);
                sql = "SELECT id_cl_abs AS " + SDbConsts.FIELD_ID + "1, id_tp_abs AS " + SDbConsts.FIELD_ID + "2, name AS " + SDbConsts.FIELD_ITEM + ", "
                        + "id_cl_abs AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY id_cl_abs, name, id_tp_abs ";
                break;
            case SModConsts.HRSU_TP_EMP_DIS:
                settings = new SGuiCatalogueSettings("Motivo baja", 1);
                sql = "SELECT id_tp_emp_dis AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_tp_emp_dis ";
                break;
            case SModConsts.HRSU_TP_EMP:
                settings = new SGuiCatalogueSettings("Tipo empleado", 1);
                sql = "SELECT id_tp_emp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_tp_emp ";
                break;
            case SModConsts.HRSU_TP_WRK:
                settings = new SGuiCatalogueSettings("Tipo obrero", 1);
                sql = "SELECT id_tp_wrk AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_tp_wrk ";
                break;
            case SModConsts.HRSU_TP_MWZ:
                settings = new SGuiCatalogueSettings("Área geográfica", 1);
                sql = "SELECT id_tp_mwz AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_tp_mwz ";
                break;
            case SModConsts.HRSU_TP_PAY_SHT_CUS:
                settings = new SGuiCatalogueSettings("Tipo nómina empresa", 1);
                sql = "SELECT id_tp_pay_sht_cus AS " + SDbConsts.FIELD_ID + "1, CONCAT(name, ' (', code, ')') AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_tp_pay_sht_cus ";
                break;
            case SModConsts.HRSU_DEP:
                settings = new SGuiCatalogueSettings("Departamento", 1);
                sql = "SELECT id_dep AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_dep ";
                break;
            case SModConsts.HRSU_POS:
                settings = new SGuiCatalogueSettings("Puesto", 1);
                sql = "SELECT id_pos AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_pos ";
                break;
            case SModConsts.HRSU_SHT:
                settings = new SGuiCatalogueSettings("Turno", 1);
                sql = "SELECT id_sht AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_sht ";
                break;
            case SModConsts.HRSU_EMP:
                settings = new SGuiCatalogueSettings("Empleado", 1);
                String join = "";
                String where = "";
                switch (subtype) {
                    case SModConsts.TRN_MAINT_USER:
                        sql = "SELECT e.id_emp AS " + SDbConsts.FIELD_ID + "1, bp.bp AS " + SDbConsts.FIELD_ITEM + " "
                                + "FROM " + SModConsts.TablesMap.get(type) + " AS e "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                                + "e.id_emp = bp.id_bp "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAINT_USER) + " AS mu ON " 
                                + "e.id_emp = mu.id_maint_user "
                                + "WHERE mu.b_employee AND e.b_act AND NOT e.b_del AND NOT bp.b_del";
                        break;
                    default:
                        if (params == null || params.getType() == SGuiConsts.PARAM_REGS_ACT) {
                            join = "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " AS em ON e.id_emp = em.id_emp ";
                            where = "e.b_act ";
                        }
                        else if (params.getType() == SGuiConsts.PARAM_REGS_ALL) {
                            join = "";
                            where = "e.id_emp IN ("
                                + "SELECT DISTINCT pr.id_emp "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                                + "WHERE NOT p.b_del AND NOT pr.b_del "
                                + "ORDER BY pr.id_emp) ";
                        }

                        sql = "SELECT e.id_emp AS " + SDbConsts.FIELD_ID + "1, bp.bp AS " + SDbConsts.FIELD_ITEM + " "
                                + "FROM " + SModConsts.TablesMap.get(type) + " AS e "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON e.id_emp = bp.id_bp "
                                + join
                                + "WHERE NOT bp.b_del AND NOT e.b_del "
                                + (where.isEmpty() ? "" : "AND " + where)
                                + "ORDER BY bp.bp, e.id_emp ";
                        break;
                }
                break;
            case SModConsts.HRSU_TP_EXP:
                settings = new SGuiCatalogueSettings("Tipo gasto", 1);
                settings.setCodeSettings(true, true);
                sql = "SELECT id_tp_exp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_tp_exp ";
                break;
            case SModConsts.HRSU_PACK_EXP:
                settings = new SGuiCatalogueSettings("Paquete gastos", 1);
                settings.setCodeSettings(true, true);
                aux = subtype == SModDataConsts.OPC_ALL ? "" : "AND id_pack_exp <> " + SModSysConsts.HRSU_PACK_EXP_NA + " ";
                sql = "SELECT id_pack_exp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del " + aux + "ORDER BY name, id_pack_exp ";
                break;
            case SModConsts.HRSU_EMP_IDSE:
                settings = new SGuiCatalogueSettings("Alta IDSE", 1);
                sql = "SELECT id_tp_acc AS " + SDbConsts.FIELD_ID + "1, IF(id_tp_acc = 1, 'ALTA EMPLEADOS', IF(id_tp_acc = 2, 'BAJA EMPLEADOS', IF(id_tp_acc = 3, 'MODIFICACIÓN EMPLEADOS', '') )) " + SDbConsts.FIELD_ITEM + " " 
                      + "FROM erp.HRSS_TP_ACC;";
                break;
            case SModConsts.HRSU_EMP_SUA:
                settings = new SGuiCatalogueSettings("Alta", 1);
                sql = "SELECT id_tp_con AS " + SDbConsts.FIELD_ID + "1, "
                       + "IF(id_tp_con = 1, 'ALTA', " 
                       + "IF(id_tp_con = 2, 'REINGRESO', "
                       + "IF(id_tp_con = 3, 'BAJA', "
                       + "IF(id_tp_con = 4, 'MOVIMIENTOS AFILIATORIOS', "
                       + "IF(id_tp_con = 5, 'INCAPACIDAD', "
                       + "IF(id_tp_con = 6, 'AUSENTISMO', '')))))) " + SDbConsts.FIELD_ITEM + " " 
                       + "FROM erp.hrss_tp_con " 
                       + "WHERE id_tp_con <= 6;";
                break;
            case SModConsts.HRS_TAX:
                settings = new SGuiCatalogueSettings("Tabla impuesto", 1);
                sql = "SELECT id_tax AS " + SDbConsts.FIELD_ID + "1, CONCAT('VIGENCIA: ', dt_sta) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY dt_sta, id_tax ";
                break;
            case SModConsts.HRS_TAX_SUB:
                settings = new SGuiCatalogueSettings("Tabla Subsidio empleo", 1);
                sql = "SELECT id_tax_sub AS " + SDbConsts.FIELD_ID + "1, CONCAT('VIGENCIA: ', dt_sta) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY dt_sta, id_tax_sub ";
                break;
            case SModConsts.HRS_SSC:
                settings = new SGuiCatalogueSettings("Tabla retención SS", 1);
                sql = "SELECT id_ssc AS " + SDbConsts.FIELD_ID + "1, CONCAT('VIGENCIA: ', dt_sta) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY dt_sta, id_ssc ";
                break;
            case SModConsts.HRS_LOAN:
                settings = new SGuiCatalogueSettings("Crédito/Préstamo", 2);
                sql = "SELECT l.id_emp AS " + SDbConsts.FIELD_ID + "1, l.id_loan AS " + SDbConsts.FIELD_ID + "2, CONCAT(lt.name, ',', l.num) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " AS l "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_LOAN) + " AS lt ON l.fk_tp_loan = lt.id_tp_loan "
                        + "WHERE l.b_del = 0 AND l.b_clo = 0 "
                        + (params != null && params.getKey() != null ? " AND l.id_emp = " + params.getKey()[0] + " AND l.fk_tp_loan = " + params.getKey()[1] : "") + " "
                        + "ORDER BY l.num, l.id_emp, l.id_loan ";
                break;
            case SModConsts.HRS_EAR:
                settings = new SGuiCatalogueSettings("Percepción", 1);
                sql = "SELECT id_ear AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + (params != null && params.getKey() != null ? "AND fk_tp_ben = " + params.getKey()[0] : "") + " "
                        + "ORDER BY " + SDbConsts.FIELD_ITEM + ", id_ear ";
                break;
            case SModConsts.HRS_DED:
                settings = new SGuiCatalogueSettings("Deducción", 1);
                sql = "SELECT id_ded AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + (params != null && params.getKey() != null ? "AND fk_tp_ben = " + params.getKey()[0] : "") + " " 
                        + "ORDER BY " + SDbConsts.FIELD_ITEM + ", id_ded ";
                break;
            case SModConsts.HRS_BEN:
                settings = new SGuiCatalogueSettings("Prestación", 1);
                sql = "SELECT id_ben AS " + SDbConsts.FIELD_ID + "1, CONCAT(name, ' - ', code) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + (subtype == 0 ? "" : "AND fk_tp_ben = " + subtype) + " " 
                        + "ORDER BY " + SDbConsts.FIELD_ITEM + ", id_ben ";
                break;
            case SModConsts.HRS_PACK_CC:
                settings = new SGuiCatalogueSettings("Paquete centros costo", 1);
                settings.setCodeSettings(true, false);
                sql = "SELECT id_pack_cc AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del ORDER BY " + SDbConsts.FIELD_ITEM + ", id_pack_cc ";
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        if (settings != null) {
            settings.setSql(sql);

            if (params != null && params.getParamsMap().get(SGuiConsts.PARAM_ITEM) != null) {
                settings.setSelectionItemApplying((Boolean) params.getParamsMap().get(SGuiConsts.PARAM_ITEM));
            }
        }

        return settings;
    }

    @Override
    public SGridPaneView getView(final int type, final int subtype, final SGuiParams params) {
        SGridPaneView view = null;
        String title;

        switch (type) {
            case SModConsts.HRSU_CL_ABS:
                view = new SViewAbsenceClass(miClient, "Clases incidencia");
                break;
            case SModConsts.HRSU_TP_ABS:
                view = new SViewAbsenceType(miClient, "Tipos incidencia");
                break;
            case SModConsts.HRSU_TP_EMP_DIS:
                view = new SViewEmployeeDismissalType(miClient, "Motivos baja");
                break;
            case SModConsts.HRSU_TP_EMP:
                view = new SViewEmployeeType(miClient, "Tipos empleado");
                break;
            case SModConsts.HRSU_TP_WRK:
                view = new SViewWorkerType(miClient, "Tipos obrero");
                break;
            case SModConsts.HRSU_TP_MWZ:
                view = new SViewMwzType(miClient, "Áreas geográficas");
                break;
            case SModConsts.HRSU_TP_PAY_SHT_CUS:
                view = new SViewPaysheetCustomType(miClient, "Tipos nómina empresa");
                break;
            case SModConsts.HRSU_POS:
                view = new SViewPosition(miClient, "Puestos");
                break;
            case SModConsts.HRSU_DEP:
                view = new SViewDepartment(miClient, "Deptos.");
                break;
            case SModConsts.HRSU_TP_EXP:
                view = new SViewExpenseType(miClient, "Tipos gasto");
                break;
            case SModConsts.HRSU_PACK_EXP:
                view = new SViewPackExpenses(miClient, "Paqs. gastos");
                break;
            case SModConsts.HRSU_PACK_EXP_ITEM:
                view = new SViewPackExpensesItem(miClient, "Paqs. gastos ítems");
                break;
            case SModConsts.HRS_DEP_CC:
                view = new SViewDepartmentCostCenter(miClient, "Deptos. y centros costo");
                break;
            case SModConsts.HRSX_EMP_CC:
                view = new SViewEmployeesCc(miClient, "Empleados y centros costo");
                break;
            case SModConsts.HRSU_SHT:
                view = new SViewShift(miClient, "Turnos");
                break;
            case SModConsts.HRS_CFG:
                view = new SViewConfig(miClient, "Configuración módulo");
                break;
            case SModConsts.HRS_FDY:
                view = new SViewFirstDayYear(miClient, "Primer día año");
                break;
            case SModConsts.HRS_HOL:
                view = new SViewHoliday(miClient, "Días feriados");
                break;
            case SModConsts.HRS_WDS:
                view = new SViewWorkingDaySettings(miClient, "Días laborables");
                break;
            case SModConsts.HRS_COND_EAR:
                view = new SViewConditionalEarning(miClient, "Percepciones condicionales");
                break;
            case SModConsts.HRS_PRE_PAY_CUT_CAL:
                view = new SViewPrePayrollCutoffCalendar(miClient, "Calendario de fechas de corte");
                break;
            case SModConsts.HRS_TAX:
                view = new SViewTaxTable(miClient, "Tablas impuesto");
                break;
            case SModConsts.HRS_TAX_ROW:
                view = new SViewTaxTableRow(miClient, "Tablas impuesto (detalle)");
                break;
            case SModConsts.HRS_TAX_SUB:
                view = new SViewTaxSubsidyTable(miClient, "Tablas Subsidio empleo");
                break;
            case SModConsts.HRS_TAX_SUB_ROW:
                view = new SViewTaxSubsidyTableRow(miClient, "Tablas Subsidio empleo (detalle)");
                break;
            case SModConsts.HRS_SSC:
                view = new SViewSsContributionTable(miClient, "Tablas retención SS");
                break;
            case SModConsts.HRS_SSC_ROW:
                view = new SViewSsContributionTableRow(miClient, "Tablas retención SS (detalle)");
                break;
            case SModConsts.HRS_BEN:
                view = new SViewBenefitTable(miClient, "Tablas prestaciones");
                break;
            case SModConsts.HRS_BEN_ROW:
                view = new SViewBenefitTableRow(miClient, "Tablas prestaciones (detalle)");
                break;
            case SModConsts.HRS_WRK_SAL:
                view = new SViewWorkerTypeSalary(miClient, "Salarios diarios tipo obrero");
                break;
            case SModConsts.HRS_MWZ_WAGE:
                view = new SViewMwzTypeWage(miClient, "Salarios mínimos área geográfica");
                break;
            case SModConsts.HRS_UMA:
                view = new SViewUma(miClient, "UMA");
                break;
            case SModConsts.HRS_UMI:
                view = new SViewUmi(miClient, "UMI");
                break;
            case SModConsts.HRS_TP_LOAN_ADJ:
                view = new SViewLoanTypeAdjustment(miClient, "Ajustes tipo crédito/préstamo");
                break;
            case SModConsts.HRS_EMP_LOG_HIRE:
                view = new SViewEmployeeHireLog(miClient, "Bitácora altas y bajas");
                break;
            case SModConsts.HRSX_EMP_LOG_HIRE_BY_PER:
                switch (subtype) {
                    case SViewEmployeeHireLogByPeriod.GRID_SUBTYPE_HIRE:
                        view = new SViewEmployeeHireLogByPeriod(miClient, "Altas x período", subtype);
                        break;
                    case SViewEmployeeHireLogByPeriod.GRID_SUBTYPE_DISMISS:
                        view = new SViewEmployeeHireLogByPeriod(miClient, "Bajas x período", subtype);
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.HRSX_EMP_LOG_SUA:
                view = new SViewEmployeeSua(miClient, "Bitácora empleados SUA");
                break;
            case SModConsts.HRSX_EMP_LOG_IDSE:
                view = new SViewEmployeeIdse(miClient, "Bitácora empleados IDSE");
                break;
            case SModConsts.HRS_EMP_LOG_WAGE:
                view = new SViewEmployeeWageLog(miClient, "Bitácora sueldos y salarios");
                break;
            case SModConsts.HRS_EMP_LOG_SAL_SSC:
                view = new SViewEmployeeWageSscBaseLog(miClient, "Bitácora salarios base cotización");
                break;
            case SModConsts.HRS_EMP_BEN:
                view = new SViewEmployeeBenefitTables(miClient, "Prestaciones empleados");
                break;
            case SModConsts.HRS_ABS:
                view = new SViewAbsence(miClient, "Incidencias");
                break;
            case SModConsts.HRS_LOAN:
                view = new SViewLoan(miClient, "Créditos/préstamos");
                break;
            case SModConsts.HRS_EAR:
                view = new SViewEarning(miClient, "Percepciones");
                break;
            case SModConsts.HRS_DED:
                view = new SViewDeduction(miClient, "Deducciones");
                break;
            case SModConsts.HRSX_AUT_EAR:
                switch (subtype) {
                    case SModSysConsts.HRS_AUT_GBL:
                        view = new SViewAutomaticEarnings(miClient, subtype, "Percepciones aut. globales");
                        break;
                    case SModSysConsts.HRS_AUT_EMP:
                        if (params == null) {
                            view = new SViewAutomaticEarnings(miClient, subtype, "Percepciones aut. empleado");
                        }
                        else {
                            view = new SViewAutomaticEarnings(miClient, subtype, "Percepciones aut. empleado (detalle)", params);
                        }
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.HRSX_AUT_DED:
                switch (subtype) {
                    case SModSysConsts.HRS_AUT_GBL:
                        view = new SViewAutomaticDeductions(miClient, subtype, "Deducciones aut. globales");
                        break;
                    case SModSysConsts.HRS_AUT_EMP:
                        if (params == null) {
                            view = new SViewAutomaticDeductions(miClient, subtype, "Deducciones aut. empleado");
                        }
                        else {
                            view = new SViewAutomaticDeductions(miClient, subtype, "Deducciones aut. empleado (detalle)", params);
                        }
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.HRSX_BEN_MOV:
                switch (subtype) {
                    case SModSysConsts.HRSS_TP_BEN_VAC:
                        view = new SViewBenefit(miClient, subtype, "Control vacaciones");
                        break;
                    case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                        view = new SViewBenefit(miClient, subtype, "Control prima vacacional");
                        break;
                    case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                        view = new SViewBenefit(miClient, subtype, "Control gratificación anual");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.HRSX_BEN_VAC_STAT:
                view = new SViewBenefitVacationStatus(miClient, "Estatus vacaciones");
                break;
            case SModConsts.HRS_ACC_EAR:
                    switch (subtype) {
                        case SModSysConsts.HRSS_TP_ACC_GBL:
                            view = new SViewAccountingEarning(miClient, subtype, "Contab. percepciones globales");
                            break;
                        case SModSysConsts.HRSS_TP_ACC_DEP:
                            view = new SViewAccountingEarning(miClient, subtype, "Contab. percepciones depto.");
                            break;
                        case SModSysConsts.HRSS_TP_ACC_EMP:
                            view = new SViewAccountingEarning(miClient, subtype, "Contab. percepciones empleado");
                            break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.HRS_ACC_DED:
                    switch (subtype) {
                        case SModSysConsts.HRSS_TP_ACC_GBL:
                            view = new SViewAccountingDeduction(miClient, subtype, "Contab. deducciones globales");
                            break;
                        case SModSysConsts.HRSS_TP_ACC_DEP:
                            view = new SViewAccountingDeduction(miClient, subtype, "Contab. deducciones depto.");
                            break;
                        case SModSysConsts.HRSS_TP_ACC_EMP:
                            view = new SViewAccountingDeduction(miClient, subtype, "Contab. deducciones empleado");
                            break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.HRS_TP_EXP_ACC:
                view = new SViewExpenseTypeAccount(miClient, "Contab. tipos gasto");
                break;
            case SModConsts.HRS_PACK_CC:
                view = new SViewPackCostCenters(miClient, "Paqs. centros costos");
                break;
            case SModConsts.HRS_PACK_CC_CC:
                view = new SViewPackCostCentersCostCenter(miClient, "Paqs. centros costos y centros costos");
                break;
            case SModConsts.HRS_CFG_ACC_DEP_PACK_CC:
                view = new SViewCfgAccountingDepartmentPackCostCenters(miClient, "Deptos. y paqs. centros costos");
                break;
            case SModConsts.HRS_CFG_ACC_EMP_PACK_CC:
                view = new SViewCfgAccountingEmployeePackCostCenters(miClient, "Empleados y paqs. centros costos");
                break;
            case SModConsts.HRS_CFG_ACC_EMP_EAR:
                view = new SViewCfgAccountingEmployeeEarning(miClient, "Empleados y percepciones");
                break;
            case SModConsts.HRS_CFG_ACC_EMP_DED:
                view = new SViewCfgAccountingEmployeeDeduction(miClient, "Empleados y deducciones");
                break;
            case SModConsts.HRS_CFG_ACC_EAR:
                view = new SViewCfgAccountingEarning(miClient, "Contab. percepciones");
                break;
            case SModConsts.HRS_CFG_ACC_DED:
                view = new SViewCfgAccountingDeduction(miClient, "Contab. deducciones");
                break;
            case SModConsts.HRS_PAY:
                view = new SViewPayroll(miClient, "Nóminas " + (subtype == SModSysConsts.HRSS_TP_PAY_WEE ? "semanales" : "quincenales"), subtype);
                break;
            case SModConsts.HRS_PAY_RCP:
                view = new SViewPayrollReceipt(miClient, "Recibos nóminas " + (subtype == SModSysConsts.HRSS_TP_PAY_WEE ? "semanales" : "quincenales"), subtype);
                break;
            case SModConsts.HRS_PAY_RCP_EAR:
                    switch (subtype) {
                        case SModConsts.HRS_BEN:
                            view = new SViewPayrollBenefitEarningComplement(miClient, "Ajustes prestaciones");
                            break;
                        case SModConsts.HRS_LOAN:
                            view = new SViewPayrollLoanEarningComplement(miClient, "Incrementos créditos/préstamos");
                            break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.HRS_PAY_RCP_DED:
                    switch (subtype) {
                        case SModConsts.HRS_LOAN:
                            view = new SViewPayrollLoanDeductionComplement(miClient, "Decrementos créditos/préstamos");
                            break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.HRS_PAY_RCP_IMPORT:
                view = new SViewPayrollReceiptImportedEarnings(miClient, "Percepciones importadas " + (subtype == SModSysConsts.HRSS_TP_PAY_WEE ? "semanales" : "quincenales"), subtype);
                break; 
            case SModConsts.HRS_ADV_SET:
                view = new SViewAdvanceSettlement(miClient, "Control adelantos liquidación");
                break;
            case SModConsts.HRS_DOC_BREACH:
                view = new SViewDocBreach(miClient, "Infracciones");
                break;
            case SModConsts.HRSX_DOC_BREACH_SUM:
                view = new SViewDocBreachSummary(miClient, "Infracciones x empleado");
                break;
            case SModConsts.HRS_DOC_ADM_REC:
                view = new SViewDocAdminRecord(miClient, "Actas administrativas");
                break;
            case SModConsts.HRSX_DOC_ADM_REC_SUM:
                view = new SViewDocAdminRecordSummary(miClient, "Actas administrativas x empleado");
                break;
            case SModConsts.HRSX_PAY_REC:
                view = new SViewPayrollReceiptRecord(miClient, "Recibos nóminas vs. pólizas contables");
                break;
            case SModConsts.HRS_SIE_PAY:
                if (subtype == SModConsts.VIEW_SC_SUM) {
                    title = "CFDI nóminas" + (params != null && params.getType() == SCfdConsts.CFDI_PAYROLL_VER_OLD ? " imp." : "");
                }
                else {
                    title = "CFDI recibos nóminas" + (params != null && params.getType() == SCfdConsts.CFDI_PAYROLL_VER_OLD ? " imp." : "");
                }
                view = new SViewPayrollCfdi(miClient, subtype, title, params);
                break;
            case SModConsts.HRSX_PTU:
                view = new SViewPtu(miClient, "Consulta para PTU");
                break;
            case SModConsts.HRSX_BEN_ANN_BON:
                view = new SViewBenAnnualBon(miClient, "Consulta provisión de aguinaldo");
                break;
            case SModConsts.HRSX_BANK_PAY_DISP:
                view = new SViewBankPayDispersion(miClient, "Consulta dispersión mensual");
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return view;
    }

    @Override
    public SGuiOptionPicker getOptionPicker(final int type, final int subtype, final SGuiParams params) {
        String sql = "";
        ArrayList<SGridColumnForm> gridColumns = new ArrayList<>();
        SGuiOptionPickerSettings settings = null;
        SGuiOptionPicker picker = null;

        switch (type) {
            case SModConsts.HRS_EAR:
                sql = "SELECT id_ear AS " + SDbConsts.FIELD_ID + "1, "
                        + "code AS " + SDbConsts.FIELD_PICK + "1, name AS " + SDbConsts.FIELD_PICK + "2, "
                        + "code AS " + SDbConsts.FIELD_VALUE + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY code, name, id_ear ";
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Código"));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "Nombre"));
                settings = new SGuiOptionPickerSettings("Percepciones", sql, gridColumns, 1, SLibConsts.DATA_TYPE_TEXT);

                if (moPickerEarnings == null) {
                    moPickerEarnings = new SBeanOptionPicker();
                    moPickerEarnings.setPickerSettings(miClient, type, subtype, settings);
                }
                picker = moPickerEarnings;
                break;
                
            case SModConsts.HRS_DED:
                sql = "SELECT id_ded AS " + SDbConsts.FIELD_ID + "1, "
                        + "code AS " + SDbConsts.FIELD_PICK + "1, name AS " + SDbConsts.FIELD_PICK + "2, "
                        + "code AS " + SDbConsts.FIELD_VALUE + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY code, name, id_ded ";
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Código"));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "Nombre"));
                settings = new SGuiOptionPickerSettings("Deducciones", sql, gridColumns, 1, SLibConsts.DATA_TYPE_TEXT);

                if (moPickerDeductions == null) {
                    moPickerDeductions = new SBeanOptionPicker();
                    moPickerDeductions.setPickerSettings(miClient, type, subtype, settings);
                }
                picker = moPickerDeductions;
                break;
                
            case SModConsts.HRSU_EMP:
                sql = "SELECT e.id_emp AS " + SDbConsts.FIELD_ID + "1, "
                        + "b.bp AS " + SDbConsts.FIELD_PICK + "1, e.num AS " + SDbConsts.FIELD_PICK + "2, "
                        + "e.id_emp AS " + SDbConsts.FIELD_VALUE + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = e.id_emp "
                        + "WHERE NOT e.b_del AND e.b_act AND NOT b.b_del "
                        + "ORDER BY b.bp, e.num, e.id_emp ";
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "Nombre"));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Código"));
                settings = new SGuiOptionPickerSettings("Empleados", sql, gridColumns, 1, SLibConsts.DATA_TYPE_INT);

                if (moPickerEmployees == null) {
                    moPickerEmployees = new SBeanOptionPicker();
                    moPickerEmployees.setPickerSettings(miClient, type, subtype, settings);
                }
                picker = moPickerEmployees;
                break;
                
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return picker;
    }

    @Override
    public SGuiForm getForm(final int type, final int subtype, final SGuiParams params) {
        SGuiForm form = null;

        switch (type) {
            case SModConsts.HRSU_CL_ABS:
                if (moFormAbsenceClass == null) moFormAbsenceClass = new SFormAbsenceClass(miClient, "Clase de incidencia");
                form = moFormAbsenceClass;
                break;
            case SModConsts.HRSU_TP_ABS:
                if (moFormAbsenceType == null) moFormAbsenceType = new SFormAbsenceType(miClient, "Tipo de incidencia");
                form = moFormAbsenceType;
                break;
            case SModConsts.HRSU_TP_EMP_DIS:
                if (moFormEmployeeDismissalType == null) moFormEmployeeDismissalType = new SFormEmployeeDismissalType(miClient, "Motivo de baja");
                form = moFormEmployeeDismissalType;
                break;
            case SModConsts.HRSU_TP_EMP:
                if (moFormEmployeeType == null) moFormEmployeeType = new SFormEmployeeType(miClient, "Tipo de empleado");
                form = moFormEmployeeType;
                break;
            case SModConsts.HRSU_TP_WRK:
                if (moFormWorkerType == null) moFormWorkerType = new SFormWorkerType(miClient, "Tipo de obrero");
                form = moFormWorkerType;
                break;
            case SModConsts.HRSU_TP_MWZ:
                if (moFormMwzType == null) moFormMwzType = new SFormMwzType(miClient, "Área geográfica");
                form = moFormMwzType;
                break;
            case SModConsts.HRSU_TP_PAY_SHT_CUS:
                if (moForPaysheetCustomType == null) moForPaysheetCustomType = new SFormPaysheetCustomType(miClient, "Tipo de nómina de la empresa");
                form = moForPaysheetCustomType;
                break;
            case SModConsts.HRSU_DEP:
                if (moFormDepartment == null) moFormDepartment = new SFormDepartment(miClient, "Departamento");
                form = moFormDepartment;
                break;
            case SModConsts.HRSU_POS:
                if (moFormPosition == null) moFormPosition = new SFormPosition(miClient, "Puesto");
                form = moFormPosition;
                break;
            case SModConsts.HRSU_SHT:
                if (moFormShift == null) moFormShift = new SFormShift(miClient, "Turno");
                form = moFormShift;
                break;
            case SModConsts.HRSU_TP_EXP:
                if (moFormExpenseType == null) moFormExpenseType = new SFormExpenseType(miClient, "Tipo gasto");
                form = moFormExpenseType;
                break;
            case SModConsts.HRSU_PACK_EXP:
                if (moFormPackExpenses == null) moFormPackExpenses = new SFormPackExpenses(miClient, "Paquete gastos");
                form = moFormPackExpenses;
                break;
            case SModConsts.HRS_DEP_CC:
                if (moFormDepartmentCenterCost == null) moFormDepartmentCenterCost = new SFormDepartmentCostCenter(miClient, "Departamento y centro costo");
                form = moFormDepartmentCenterCost;
                break;
            case SModConsts.HRS_CFG:
                if (moFormConfig == null) moFormConfig = new SFormConfig(miClient, "Configuración");
                form = moFormConfig;
                break;
            case SModConsts.HRS_FDY:
                if (moFormFirstDayYear == null) moFormFirstDayYear = new SFormFirstDayYear(miClient, "Primer día del año");
                form = moFormFirstDayYear;
                break;
            case SModConsts.HRS_HOL:
                if (moFormHoliday == null) moFormHoliday = new SFormHoliday(miClient, "Día feriado");
                form = moFormHoliday;
                break;
            case SModConsts.HRS_WDS:
                if (moFormWorkingDaySettings == null) moFormWorkingDaySettings = new SFormWorkingDaySettings(miClient, "Días laborables");
                form = moFormWorkingDaySettings;
                break;
            case SModConsts.HRS_COND_EAR:
                if (moFormConditionalEarning == null) moFormConditionalEarning = new SFormConditionalEarning(miClient, "Percepción condicional");
                form = moFormConditionalEarning;
                break;
            case SModConsts.HRS_PRE_PAY_CUT_CAL:
                if (moFormCutoffCalendar == null) moFormCutoffCalendar = new SFormCutoffCalendar(miClient, "Calendario de cortes prenómina");
                form = moFormCutoffCalendar;
                break;
            case SModConsts.HRS_TAX:
                if (moFormTaxTable == null) moFormTaxTable = new SFormTaxTable(miClient, "Tabla de impuesto");
                form = moFormTaxTable;
                break;
            case SModConsts.HRS_TAX_SUB:
                if (moFormTaxSubsidyTable == null) moFormTaxSubsidyTable = new SFormTaxSubsidyTable(miClient, "Tabla de Subsidio para el empleo");
                form = moFormTaxSubsidyTable;
                break;
            case SModConsts.HRS_SSC:
                if (moFormSsContributionTable == null) moFormSsContributionTable = new SFormSsContributionTable(miClient, "Tabla de retención de SS");
                form = moFormSsContributionTable;
                break;
            case SModConsts.HRS_BEN:
                if (moFormBenefitTable == null) moFormBenefitTable = new SFormBenefitTable(miClient, "Tabla de prestaciones");
                form = moFormBenefitTable;
                break;
            case SModConsts.HRS_WRK_SAL:
                if (moFormWorkerTypeSalary == null) moFormWorkerTypeSalary = new SFormWorkerTypeSalary(miClient, "Salario diario por tipo de obrero");
                form = moFormWorkerTypeSalary;
                break;
            case SModConsts.HRS_MWZ_WAGE:
                if (moFormMwzTypeWage == null) moFormMwzTypeWage = new SFormMwzTypeWage(miClient, "Salario mínimo por área geográfica");
                form = moFormMwzTypeWage;
                break;
            case SModConsts.HRS_UMA:
                if (moFormUma == null) moFormUma = new SFormUma(miClient, "UMA");
                form = moFormUma;
                break;
            case SModConsts.HRS_UMI:
                if (moFormUmi == null) moFormUmi = new SFormUmi(miClient, "UMI");
                form = moFormUmi;
                break;
            case SModConsts.HRS_TP_LOAN_ADJ:
                if (moFormLoanTypeAdjustment == null) moFormLoanTypeAdjustment = new SFormLoanTypeAdjustment(miClient, "Ajuste por tipo de crédito/préstamo");
                form = moFormLoanTypeAdjustment;
                break;
            case SModConsts.HRS_LOAN:
                if (moFormLoan == null) moFormLoan = new SFormLoan(miClient, "Crédito/Préstamo");
                form = moFormLoan;
                break;
            case SModConsts.HRS_ABS:
                if (moFormAbsence == null) moFormAbsence = new SFormAbsence(miClient, "Incidencia");
                form = moFormAbsence;
                break;
            case SModConsts.HRS_ABS_CNS:
                if (moFormAbsence == null) moFormAbsence = new SFormAbsence(miClient, "Incidencia");
                form = moFormAbsence;
                break;
            case SModConsts.HRS_EAR:
                if (moFormEarning == null) moFormEarning = new SFormEarning(miClient, "Percepción");
                form = moFormEarning;
                break;
            case SModConsts.HRS_DED:
                if (moFormDeduction == null) moFormDeduction = new SFormDeduction(miClient, "Deducción");
                form = moFormDeduction;
                break;
            case SModConsts.HRSX_AUT_EAR:
                switch (subtype) {
                    case SModSysConsts.HRS_AUT_GBL:
                        if (moFormAutomaticEarningsGbl == null) moFormAutomaticEarningsGbl = new SFormAutomaticEarnings(miClient, "Percepciones automáticas globales", subtype);
                        form = moFormAutomaticEarningsGbl;
                        break;
                    case SModSysConsts.HRS_AUT_EMP:
                        if (moFormAutomaticEarningsEmp == null) moFormAutomaticEarningsEmp = new SFormAutomaticEarnings(miClient, "Percepciones automáticas por empleado", subtype);
                        form = moFormAutomaticEarningsEmp;
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.HRSX_AUT_DED:
                switch (subtype) {
                    case SModSysConsts.HRS_AUT_GBL:
                        if (moFormAutomaticDeductionsGbl == null) moFormAutomaticDeductionsGbl = new SFormAutomaticDeductions(miClient, "Deducciones automáticas globales", subtype);
                        form = moFormAutomaticDeductionsGbl;
                        break;
                    case SModSysConsts.HRS_AUT_EMP:
                        if (moFormAutomaticDeductionsEmp == null) moFormAutomaticDeductionsEmp = new SFormAutomaticDeductions(miClient, "Deducciones automáticas por empleado", subtype);
                        form = moFormAutomaticDeductionsEmp;
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.HRS_ACC_EAR:
                switch (subtype) {
                    case SModSysConsts.HRSS_TP_ACC_GBL:
                        if (moFormAccountingEarningGlobal == null) moFormAccountingEarningGlobal = new SFormAccountingEarning(miClient, "Contabilización de percepción global");
                        form = moFormAccountingEarningGlobal;
                        break;
                    case SModSysConsts.HRSS_TP_ACC_DEP:
                        if (moFormAccountingEarningDepartament == null) moFormAccountingEarningDepartament = new SFormAccountingEarning(miClient, "Contabilización de percepción por departamento");
                        form = moFormAccountingEarningDepartament;
                        break;
                    case SModSysConsts.HRSS_TP_ACC_EMP:
                        if (moFormAccountingEarningEmployee == null) moFormAccountingEarningEmployee = new SFormAccountingEarning(miClient, "Contabilización de percepción por empleado");
                        form = moFormAccountingEarningEmployee;
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.HRS_ACC_DED:
                switch (subtype) {
                    case SModSysConsts.HRSS_TP_ACC_GBL:
                        if (moFormAccountingDeductionGlobal == null) moFormAccountingDeductionGlobal = new SFormAccountingDeduction(miClient, "Contabilización de deducción global");
                        form = moFormAccountingDeductionGlobal;
                        break;
                    case SModSysConsts.HRSS_TP_ACC_DEP:
                        if (moFormAccountingDeductionDepartament == null) moFormAccountingDeductionDepartament = new SFormAccountingDeduction(miClient, "Contabilización de deducción por departamento");
                        form = moFormAccountingDeductionDepartament;
                        break;
                    case SModSysConsts.HRSS_TP_ACC_EMP:
                        if (moFormAccountingDeductionEmployee == null) moFormAccountingDeductionEmployee = new SFormAccountingDeduction(miClient, "Contabilización de deducción por empleado");
                        form = moFormAccountingDeductionEmployee;
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.HRS_TP_EXP_ACC:
                if (moFormExpenseTypeAccount == null) moFormExpenseTypeAccount = new SFormExpenseTypeAccount(miClient, "Contabilización de tipo de gasto");
                form = moFormExpenseTypeAccount;
                break;
            case SModConsts.HRS_PACK_CC:
                if (moFormPackCostCenters == null) moFormPackCostCenters = new SFormPackCostCenters(miClient, "Paquete de centros de costo");
                form = moFormPackCostCenters;
                break;
            case SModConsts.HRS_CFG_ACC_DEP_PACK_CC:
                if (moFormCfgAccountingDepartmentPackCostCenters == null) moFormCfgAccountingDepartmentPackCostCenters = new SFormCfgAccountingDepartmentPackCostCenters(miClient, "Departamento y paquete de centros de costo");
                form = moFormCfgAccountingDepartmentPackCostCenters;
                break;
            case SModConsts.HRS_CFG_ACC_EMP_PACK_CC:
                if (moFormCfgAccountingEmployeePackCostCenters == null) moFormCfgAccountingEmployeePackCostCenters = new SFormCfgAccountingEmployeePackCostCenters(miClient, "Empleado y paquete de centros de costo");
                form = moFormCfgAccountingEmployeePackCostCenters;
                break;
            case SModConsts.HRS_CFG_ACC_EMP_EAR:
                if (moFormCfgAccountingEmployeeEarning == null) moFormCfgAccountingEmployeeEarning = new SFormCfgAccountingEmployeeEarning(miClient, "Empleado y percepción");
                form = moFormCfgAccountingEmployeeEarning;
                break;
            case SModConsts.HRS_CFG_ACC_EMP_DED:
                if (moFormCfgAccountingEmployeeDeduction == null) moFormCfgAccountingEmployeeDeduction = new SFormCfgAccountingEmployeeDeduction(miClient, "Empleado y deducción");
                form = moFormCfgAccountingEmployeeDeduction;
                break;
            case SModConsts.HRS_CFG_ACC_EAR:
                if (moFormCfgAccountingEarning == null) moFormCfgAccountingEarning = new SFormCfgAccountingEarning(miClient, "Contabilización de percepción");
                form = moFormCfgAccountingEarning;
                break;
            case SModConsts.HRS_CFG_ACC_DED:
                if (moFormCfgAccountingDeduction == null) moFormCfgAccountingDeduction = new SFormCfgAccountingDeduction(miClient, "Contabilización de deducción");
                form = moFormCfgAccountingDeduction;
                break;
            case SModConsts.HRS_PAY:
                switch (subtype) {
                    case SModSysConsts.HRSS_TP_PAY_WEE:
                        if (moFormPayrollWeekly == null) moFormPayrollWeekly = new SFormPayroll(miClient, "Nómina semanal", subtype);
                        form = moFormPayrollWeekly;
                        break;
                    case SModSysConsts.HRSS_TP_PAY_FOR:
                        if (moFormPayrollFortnightly == null) moFormPayrollFortnightly = new SFormPayroll(miClient, "Nómina quincenal", subtype);
                        form = moFormPayrollFortnightly;
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.HRS_PAY_RCP_EAR:
                switch (subtype) {
                    case SModConsts.HRS_BEN:
                        if (moFormBenefitAdjustmentEarning == null) moFormBenefitAdjustmentEarning = new SFormBenefitAdjustmentEarning(miClient, "Ajuste a prestaciones");
                        form = moFormBenefitAdjustmentEarning;
                        break;
                    case SModConsts.HRS_LOAN:
                        if (moFormLoanAdjustmentEarning == null) moFormLoanAdjustmentEarning = new SFormLoanAdjustmentEarning(miClient, "Incremento de crédito/préstamo");
                        form = moFormLoanAdjustmentEarning;
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.HRS_PAY_RCP_DED:
                switch (subtype) {
                    case SModConsts.HRS_LOAN:
                        if (moFormLoanAdjustmentDeduction == null) moFormLoanAdjustmentDeduction = new SFormLoanAdjustmentDeduction(miClient, "Decremento de crédito/préstamo");
                        form = moFormLoanAdjustmentDeduction;
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.HRS_ADV_SET:
                if (moAdvanceSettlement == null) moAdvanceSettlement = new SFormAdvanceSettlement(miClient, "Control de adelanto de liquidación");
                form = moAdvanceSettlement;
                break;
            case SModConsts.HRS_DOC_BREACH:
                if (moFormDocBreach == null) moFormDocBreach = new SFormDocBreach(miClient, "Infracción");
                form = moFormDocBreach;
                break;
            case SModConsts.HRS_DOC_ADM_REC:
                if (moFormDocAdminRecord == null) moFormDocAdminRecord = new SFormDocAdminRecord(miClient, "Acta administrativa");
                form = moFormDocAdminRecord;
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return form;
    }

    @Override
    public SGuiReport getReport(final int type, final int subtype, final SGuiParams params) {
        SGuiReport guiReport = null;

        switch (type) {
            case SModConsts.HRSR_PAY:
                guiReport = new SGuiReport("reps/hrs_pay.jasper", "Nómina");
                break;
            case SModConsts.HRSR_PAY_SUM:
                guiReport = new SGuiReport("reps/hrs_pay_summary.jasper", "Nómina resumen");
                break;
            case SModConsts.HRSR_PAY_RCP:
                guiReport = new SGuiReport("reps/hrs_pay_rcp.jasper", "Recibo de nómina");
                break;
            case SModConsts.HRSR_PAY_RCP_PAY:
                guiReport = new SGuiReport("reps/hrs_pay_rcp_pay.jasper", "Recibos de nómina");
                break;
            case SModConsts.HRSR_PRE_PAY:
                guiReport = new SGuiReport("reps/hrs_pre_pay.jasper", "Prenómina");
                break;
            case SModConsts.HRSR_PAY_TAX:
                guiReport = new SGuiReport("reps/hrs_pay_tax.jasper", "Impuesto sobre nóminas");
                break;
            case SModConsts.HRSR_PAY_AUX:
                guiReport = new SGuiReport("reps/hrs_pay_aux_pay.jasper", "Auxiliares de nóminas");
                break;
            case SModConsts.HRSR_PAY_AUX_EAR_DED:
                guiReport = new SGuiReport("reps/hrs_pay_aux_ear_ded.jasper", "Reporte de percepciones y deducciones");
                break;
            case SModConsts.HRSR_PAY_EAR_DED:
                guiReport = new SGuiReport("reps/hrs_pay_ear_ded.jasper", "Reporte de percepciones y deducciones");
                break;
            case SModConsts.HRSR_LIST_EAR:
                guiReport = new SGuiReport("reps/hrs_pay_list_ear.jasper", "Listado de percepciones");
                break;
            case SModConsts.HRSR_LIST_DED:
                guiReport = new SGuiReport("reps/hrs_pay_list_ded.jasper", "Listado de deducciones");
                break;
            case SModConsts.HRSR_ACT_EMP:
                guiReport = new SGuiReport("reps/hrs_emp_act_by_period.jasper", "Reporte de empleados activos por período");
                break;
            case SModConsts.HRSR_POS:
                guiReport = new SGuiReport("reps/hrsr_pos.jasper", "Reporte de posiciones");
                break;
            case SModConsts.HRSR_DOC_BREACH:
                guiReport = new SGuiReport("reps/hrs_doc_breach.jasper", "Infracción");
                break;
            case SModConsts.HRSR_DOC_ADM_REC:
                guiReport = new SGuiReport("reps/hrs_doc_adm_rec.jasper", "Acta administrativa");
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return guiReport;
    }
}
