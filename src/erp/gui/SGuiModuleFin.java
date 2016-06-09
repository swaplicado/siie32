/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataRepConstants;
import erp.form.SFormOptionPicker;
import erp.gui.mod.cfg.SCfgMenu;
import erp.gui.mod.cfg.SCfgMenuSection;
import erp.gui.mod.cfg.SCfgMenuSectionItem;
import erp.gui.mod.cfg.SCfgMenuSectionSeparator;
import erp.gui.mod.cfg.SCfgModule;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormOptionPickerInterface;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataAccountBizPartner;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataAccountItem;
import erp.mfin.data.SDataAccountTax;
import erp.mfin.data.SDataAdministrativeConceptType;
import erp.mfin.data.SDataCardIssuer;
import erp.mfin.data.SDataCheck;
import erp.mfin.data.SDataCheckPrintingFormat;
import erp.mfin.data.SDataCheckWallet;
import erp.mfin.data.SDataCostCenter;
import erp.mfin.data.SDataCostCenterItem;
import erp.mfin.data.SDataExchangeRate;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataTaxBasic;
import erp.mfin.data.SDataTaxGroup;
import erp.mfin.data.SDataTaxGroupAllItem;
import erp.mfin.data.SDataTaxGroupAllItemGeneric;
import erp.mfin.data.SDataTaxIdentity;
import erp.mfin.data.SDataTaxRegion;
import erp.mfin.data.SDataTaxableConceptType;
import erp.mfin.data.SDataYear;
import erp.mfin.form.SDialogRepAccount;
import erp.mfin.form.SDialogRepAccountCashBalance;
import erp.mfin.form.SDialogRepAccountConcept;
import erp.mfin.form.SDialogRepAuxAccounting;
import erp.mfin.form.SDialogRepBalanceSheet;
import erp.mfin.form.SDialogRepBizPartnerAccountingMoves;
import erp.mfin.form.SDialogRepBizPartnerBalance;
import erp.mfin.form.SDialogRepBizPartnerBalanceDps;
import erp.mfin.form.SDialogRepBizPartnerJournal;
import erp.mfin.form.SDialogRepBizPartnerMove;
import erp.mfin.form.SDialogRepBizPartnerStatement;
import erp.mfin.form.SDialogRepDpsMonthReport;
import erp.mfin.form.SDialogRepDpsPayment;
import erp.mfin.form.SDialogRepFinMov;
import erp.mfin.form.SDialogRepFinMovBankDayDet;
import erp.mfin.form.SDialogRepProfitLossStatement;
import erp.mfin.form.SDialogRepRecords;
import erp.mfin.form.SDialogRepTaxesByConcept;
import erp.mfin.form.SDialogRepTaxesMoves;
import erp.mfin.form.SDialogRepTrialBalance;
import erp.mfin.form.SDialogRepTrialBalanceDual;
import erp.mfin.form.SDialogUtilFiscalYearClosing;
import erp.mfin.form.SDialogUtilFiscalYearClosingDelete;
import erp.mfin.form.SDialogUtilFiscalYearOpening;
import erp.mfin.form.SDialogUtilFiscalYearOpeningDelete;
import erp.mfin.form.SFormAccount;
import erp.mfin.form.SFormAccountBizPartner;
import erp.mfin.form.SFormAccountCashBank;
import erp.mfin.form.SFormAccountCashCash;
import erp.mfin.form.SFormAccountItem;
import erp.mfin.form.SFormAccountMajor;
import erp.mfin.form.SFormAccountTax;
import erp.mfin.form.SFormAdministrativeConceptType;
import erp.mfin.form.SFormCardIssuer;
import erp.mfin.form.SFormCheckAnnuled;
import erp.mfin.form.SFormCheckFormat;
import erp.mfin.form.SFormCheckWallet;
import erp.mfin.form.SFormCostCenter;
import erp.mfin.form.SFormCostCenterItem;
import erp.mfin.form.SFormCostCenterMajor;
import erp.mfin.form.SFormExchangeRate;
import erp.mfin.form.SFormRecord;
import erp.mfin.form.SFormRecordHeader;
import erp.mfin.form.SFormTaxBasic;
import erp.mfin.form.SFormTaxGroup;
import erp.mfin.form.SFormTaxGroupAllItem;
import erp.mfin.form.SFormTaxGroupAllItemGeneric;
import erp.mfin.form.SFormTaxIdentity;
import erp.mfin.form.SFormTaxRegion;
import erp.mfin.form.SFormTaxableConceptType;
import erp.mfin.form.SFormYear;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SBpsUtils;
import erp.mod.fin.db.SFiscalAccounts;
import erp.mod.fin.form.SDialogFiscalAccountsConfig;
import erp.mod.fin.form.SDialogFiscalXmlFile;
import erp.mod.fin.form.SDialogRepCashFlowExpected;
import erp.mod.fin.form.SDialogReportTaxPending;
import erp.mtrn.data.SDataCtr;
import erp.mtrn.form.SDialogRepBizPartnerBalanceAging;
import erp.mtrn.form.SFormCtr;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Sergio Flores
 */
public class SGuiModuleFin extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmCfg;
    private javax.swing.JMenu jmiCfgAbp;
    private javax.swing.JMenuItem jmiCfgAbpEntityCash;
    private javax.swing.JMenuItem jmiCfgAbpEntityWarehouse;
    private javax.swing.JMenuItem jmiCfgAbpEntityPlant;
    private javax.swing.JMenuItem jmiCfgAbpBizPartner;
    private javax.swing.JMenuItem jmiCfgAbpItem;
    private javax.swing.JMenuItem jmiCfgAbpTax;
    private javax.swing.JMenu jmCfgAbpLink;
    private javax.swing.JMenuItem jmiCfgAbpLinkCashAccount;
    private javax.swing.JMenuItem jmiCfgAbpLinkWarehouse;
    private javax.swing.JMenuItem jmiCfgAbpLinkPlant;
    private javax.swing.JMenuItem jmiCfgAbpLinkBizPartner;
    private javax.swing.JMenuItem jmiCfgAbpLinkItem;
    private javax.swing.JMenuItem jmiCfgAccItemLink;
    private javax.swing.JMenuItem jmiCfgTaxItemLink;

    private javax.swing.JMenu jmCat;
    private javax.swing.JMenuItem jmiCatAccount;
    private javax.swing.JMenuItem jmiCatFiscalAccount;
    private javax.swing.JMenuItem jmiCatCostCenter;
    private javax.swing.JMenuItem jmiCatCashAccountCash;
    private javax.swing.JMenuItem jmiCatCashAccountBank;
    private javax.swing.JMenuItem jmiCatCardIssuer;
    private javax.swing.JMenuItem jmiCatCheckWallet;
    private javax.swing.JMenuItem jmiCatCheckFormat;
    private javax.swing.JMenuItem jmiCatTaxRegion;
    private javax.swing.JMenuItem jmiCatTaxIdentity;
    private javax.swing.JMenuItem jmiCatTaxBasic;
    private javax.swing.JMenuItem jmiCatTaxGroup;
    private javax.swing.JMenuItem jmiCatConceptAdministrativeType;
    private javax.swing.JMenuItem jmiCatConceptTaxableType;
    private javax.swing.JSeparator jsCatConcept;

    private javax.swing.JMenu jmCatSysAcc;
    private javax.swing.JMenuItem jmiCatSysAccTaxGroupForItem;
    private javax.swing.JMenuItem jmiCatSysAccTaxGroupForItemGeneric;
    private javax.swing.JMenuItem jmiCatSysAccAccountForBizPartner;
    private javax.swing.JMenuItem jmiCatSysAccAccountForItem;
    private javax.swing.JMenuItem jmiCatSysAccAccountForTax;
    private javax.swing.JMenuItem jmiCatSysAccQryAccountForBizPartner;
    private javax.swing.JMenuItem jmiCatSysAccQryAccountForItem;
    private javax.swing.JMenuItem jmiCatSysAccCostCenterForItem;

    private javax.swing.JMenu jmBkk;
    private javax.swing.JMenuItem jmiBkkYearOpenClose;
    private javax.swing.JMenuItem jmiBkkFiscalYearClosing;
    private javax.swing.JMenuItem jmiBkkFiscalYearClosingDel;
    private javax.swing.JMenuItem jmiBkkFiscalYearOpening;
    private javax.swing.JMenuItem jmiBkkFiscalYearOpeningDel;

    private javax.swing.JMenu jmRec;
    private javax.swing.JMenuItem jmiRecRec;
    private javax.swing.JMenuItem jmiRecRecEtyXmlIncome;
    private javax.swing.JMenuItem jmiRecRecEtyXmlExpenses;
    private javax.swing.JMenuItem jmiRecRecCash;
    private javax.swing.JMenuItem jmiRecBal;
    private javax.swing.JMenuItem jmiRecBalAll;// XXX (jbarajas 2016-10-26) new view for trial balance in maximo nivel
    private javax.swing.JMenuItem jmiRecCashAccBalountCash;
    private javax.swing.JMenuItem jmiRecCashAccBalountBank;
    private javax.swing.JMenuItem jmiRecBizPartnerBalCus;
    private javax.swing.JMenuItem jmiRecBizPartnerBalSup;
    private javax.swing.JMenuItem jmiRecBizPartnerBalDbr;
    private javax.swing.JMenuItem jmiRecBizPartnerBalCdr;
    private javax.swing.JMenuItem jmiRecBalInventory;
    private javax.swing.JMenuItem jmiRecBalTaxDebit;
    private javax.swing.JMenuItem jmiRecBalTaxCredit;
    private javax.swing.JMenuItem jmiRecBalProfitLoss;
    private javax.swing.JMenuItem jmiRecBookkeepingMoves;
    private javax.swing.JMenuItem jmiRecDpsBizPartnerCus;
    private javax.swing.JMenuItem jmiRecDpsBizPartnerSup;

    private javax.swing.JMenu jmFin;
    private javax.swing.JMenuItem jmiFinExchangeRate;
    private javax.swing.JMenuItem jmiFinCashCheck;
    private javax.swing.JMenuItem jmiFinCashCounterReceipt;
    private javax.swing.JSeparator jsFinCash;
    private javax.swing.JMenuItem jmiFinLayoutBank;
    private javax.swing.JMenuItem jmiFinLayoutBankPending;
    private javax.swing.JMenuItem jmiFinLayoutBankDone;
    private javax.swing.JMenuItem jmiFinLayoutBankAdvances;
    private javax.swing.JMenuItem jmiFinLayoutBankPendingAdvances;
    private javax.swing.JMenuItem jmiFinLayoutBankDoneAdvances;

    private javax.swing.JMenu jmRep;
    private javax.swing.JMenu jmRepTrialBal;
    private javax.swing.JMenuItem jmiRepTrialBalStandard;
    private javax.swing.JMenuItem jmiRepTrialBalCostCenter;
    private javax.swing.JMenuItem jmiRepTrialBalCostCenterItem;
    private javax.swing.JMenuItem jmiRepTrialBalItemType;
    private javax.swing.JMenu jmRepFinStat;
    private javax.swing.JMenuItem jmiRepFinStatBalanceSheet;
    private javax.swing.JMenuItem jmiRepFinStatProfitLossStat;
    private javax.swing.JMenu jmRepCashAccBal;
    private javax.swing.JMenuItem jmiRepCashAccBalCash;
    private javax.swing.JMenuItem jmiRepCashAccBalBank;
    private javax.swing.JMenuItem jmiRepCashAccBal;
    private javax.swing.JMenu jmRepCashAccMovs;
    private javax.swing.JMenuItem jmiRepCashAccMovsCash;
    private javax.swing.JMenuItem jmiRepCashAccMovsBank;
    private javax.swing.JMenuItem jmiRepCashAccMovsCashDay;
    private javax.swing.JMenuItem jmiRepCashAccMovsBankDay;
    private javax.swing.JMenu jmRepBizPartnerBal;
    private javax.swing.JMenuItem jmiRepBizPartnerBalCus;
    private javax.swing.JMenuItem jmiRepBizPartnerBalSup;
    private javax.swing.JMenuItem jmiRepBizPartnerBalDbr;
    private javax.swing.JMenuItem jmiRepBizPartnerBalCdr;
    private javax.swing.JMenuItem jmiRepBizPartnerBalDpsCus;
    private javax.swing.JMenuItem jmiRepBizPartnerBalDpsSup;
    private javax.swing.JMenu jmRepBizPartnerBalAging;
    private javax.swing.JMenuItem jmiRepBizPartnerBalAgingCus;
    private javax.swing.JMenuItem jmiRepBizPartnerBalAgingSup;
    private javax.swing.JMenu jmRepBizPartnerStat;
    private javax.swing.JMenuItem jmiRepBizPartnerStatCus;
    private javax.swing.JMenuItem jmiRepBizPartnerStatSup;
    private javax.swing.JMenuItem jmiRepBizPartnerStatDbr;
    private javax.swing.JMenuItem jmiRepBizPartnerStatCdr;
    private javax.swing.JMenu jmRepBizPartnerAccMovs;
    private javax.swing.JMenuItem jmiRepBizPartnerAccMovsCus;
    private javax.swing.JMenuItem jmiRepBizPartnerAccMovsSup;
    private javax.swing.JMenu jmiRepBizPartnerLedger;
    private javax.swing.JMenuItem jmiRepBizPartnerLedgerCus;
    private javax.swing.JMenuItem jmiRepBizPartnerLedgerSup;
    private javax.swing.JMenuItem jmiRepBizPartnerLedgerDbr;
    private javax.swing.JMenuItem jmiRepBizPartnerLedgerCdr;
    private javax.swing.JMenu jmRepAccMovs;
    private javax.swing.JMenuItem jmiRepAccMovsTax;
    private javax.swing.JMenuItem jmiRepAccMovsProfitLoss;
    private javax.swing.JMenu jmRepCashFlow;
    private javax.swing.JMenuItem jmiRepCashFlowPaysCus;
    private javax.swing.JMenuItem jmiRepCashFlowPaysSup;
    private javax.swing.JMenuItem jmiQryCashFlowPaysCusSum;
    private javax.swing.JMenuItem jmiQryCashFlowPaysCusDet;
    private javax.swing.JMenuItem jmiQryCashFlowPaysSupSum;
    private javax.swing.JMenuItem jmiQryCashFlowPaysSupDet;
    private javax.swing.JMenuItem jmiRepCashFlowExpected;
    private javax.swing.JMenuItem jmiRepLedgerAccount;
    private javax.swing.JMenuItem jmiRepLedgerCostCenter;
    private javax.swing.JMenuItem jmiRepConceptAdmin;
    private javax.swing.JMenuItem jmiRepConceptTax;
    private javax.swing.JSeparator jsRepAux1;
    private javax.swing.JMenuItem jmiRepPrtJournalVouchers;
    private javax.swing.JMenuItem jmiRepPrtChartOfAccounts;
    private javax.swing.JMenuItem jmiRepTaxesByConcept;
    private javax.swing.JMenu jmRepFiscal;
    private javax.swing.JMenuItem jmiRepFiscalTaxPendSal;
    private javax.swing.JMenuItem jmiRepFiscalTaxPendPur;
    private javax.swing.JMenuItem jmiRepFiscalXmlFiles;
    private javax.swing.JMenuItem jmiRepFiscalMonthRepCfd;
    private javax.swing.JMenuItem jmiRepFiscalMonthRepCf;

    private erp.mfin.form.SFormRecord moFormRecord;
    private erp.mfin.form.SFormRecord moFormRecordRo;
    private erp.mfin.form.SFormRecordHeader moFormRecordHeader;
    private erp.mfin.form.SFormAccount moFormAccount;
    private erp.mfin.form.SFormAccount moFormAccountChangeDeep;
    private erp.mfin.form.SFormAccountMajor moFormAccountMajor;
    private erp.mfin.form.SFormCostCenter moFormCostCenter;
    private erp.mfin.form.SFormCostCenterMajor moFormCostCenterMajor;
    private erp.mfin.form.SFormExchangeRate moFormExchangeRate;
    private erp.mfin.form.SFormYear moFormYear;
    private erp.mfin.form.SFormCardIssuer moFormCardIssuer;
    private erp.mfin.form.SFormTaxRegion moFormTaxRegion;
    private erp.mfin.form.SFormTaxIdentity moFormTaxIdentity;
    private erp.mfin.form.SFormAdministrativeConceptType moFormAdministrativeConceptType;
    private erp.mfin.form.SFormTaxableConceptType moFormTaxableConceptType;
    private erp.mfin.form.SFormTaxBasic moFormTaxBas;
    private erp.mfin.form.SFormTaxGroup moFormTaxGroup;
    private erp.mfin.form.SFormTaxGroupAllItem moFormTaxGroupItem;
    private erp.mfin.form.SFormTaxGroupAllItemGeneric moFormTaxGroupItemGeneric;
    private erp.mfin.form.SFormAccountBizPartner moFormAccountBizPartner;
    private erp.mfin.form.SFormAccountItem moFormAccountItem;
    private erp.mfin.form.SFormAccountTax moFormAccountTax;
    private erp.mfin.form.SFormAccountCashCash moFormAccountCash;
    private erp.mfin.form.SFormAccountCashBank moFormAccountCashBank;
    private erp.mfin.form.SFormCostCenterItem moFormItemConfig;
    private erp.mfin.form.SFormCheckWallet moFormCheckWallet;
    private erp.mfin.form.SFormCheckFormat moFormCheckFormat;
    private erp.mfin.form.SFormCheckAnnuled moFormCheckAnnuled;
    private erp.mtrn.form.SFormCtr moFormCtr;

    private erp.form.SFormOptionPicker moPickerAccountSpecializedType;
    private erp.form.SFormOptionPicker moPickerFiscalAccount;
    private erp.form.SFormOptionPicker moPickerFiscalBank;
    private erp.form.SFormOptionPicker moPickerAccountUserType;
    private erp.form.SFormOptionPicker moPickerAccountUserClass;
    private erp.form.SFormOptionPicker moPickerAccountUserSubclass;
    private erp.form.SFormOptionPicker moPickerAccountLedgerType;
    private erp.form.SFormOptionPicker moPickerAccountEbitdaType;
    private erp.form.SFormOptionPicker moPickerExchangeRate;
    private erp.form.SFormOptionPicker moPickerAccount;
    private erp.form.SFormOptionPicker moPickerCostCenter;
    private erp.form.SFormOptionPicker moPickerBookkeepingCenter;
    private erp.form.SFormOptionPicker moPickerCardIssuer;
    private erp.form.SFormOptionPicker moPickerTaxRegion;
    private erp.form.SFormOptionPicker moPickerTaxIdentity;
    private erp.form.SFormOptionPicker moPickerTaxBas;
    private erp.form.SFormOptionPicker moPickerTax;
    private erp.form.SFormOptionPicker moPickerTaxGroup;
    private erp.form.SFormOptionPicker moPickerCheckFormat;
    private erp.form.SFormOptionPicker moPickerCheckFormatGp;
    private erp.form.SFormOptionPicker moPickerAccountCash;
    private erp.form.SFormOptionPicker moPickerAccountCashCash;
    private erp.form.SFormOptionPicker moPickerAccountCashBank;
    private erp.form.SFormOptionPicker moPickerAccountCashBankCheck;
    private erp.form.SFormOptionPicker moPickerRecordType;
    private erp.form.SFormOptionPicker moPickerRecordTypeAll;
    private erp.form.SFormOptionPicker moPickerRecordTypeUser;
    private erp.form.SFormOptionPicker moPickerAdministrativeConceptType;
    private erp.form.SFormOptionPicker moPickerTaxableConceptType;
    private erp.form.SFormOptionPicker moPickerYear;

    public SGuiModuleFin(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_FIN);
        initComponents();
    }

    private void initComponents() {
        boolean hasYearRight = false;
        boolean hasYearPeriodRight = false;
        boolean hasExcRateRight = false;
        boolean hasCatAccRight = false;
        boolean hasCatCcRight = false;
        boolean hasCatAccCashRight = false;
        boolean hasCatBkcRight = false;
        boolean hasCatCheckRight = false;
        boolean hasCatTaxRight = false;
        boolean hasBkrRight = false;
        boolean hasAutAccBpRight = false;
        boolean hasAutAccItemRight = false;
        boolean hasMoveAccCash = false;
        boolean hasMoveBpCdr = false;
        boolean hasMoveBpDbr = false;
        boolean hasCounterReceiptRight = false;
        boolean hasRepRight = false;
        boolean hasRepStatementRight = false;
        boolean hasRepFinRateRight = false;
        boolean hasGblCatAccCfg = false;
        boolean hasGblCatAccTax = false;
        boolean hasGblCatAccMisc = false;

        jmCfg = new JMenu("Configuración");

        jmiCfgAbp = new JMenu("Paquetes de contabilización automática");
        jmiCfgAbpEntityCash = new JMenuItem("Cuentas de dinero");
        jmiCfgAbpEntityWarehouse = new JMenuItem("Almacenes de bienes");
        jmiCfgAbpEntityPlant = new JMenuItem("Plantas de producción");
        jmiCfgAbpBizPartner = new JMenuItem("Asociados de negocios");
        jmiCfgAbpItem = new JMenuItem("Ítems");
        jmiCfgAbpTax = new JMenuItem("Impuestos");

        jmiCfgAbp.add(jmiCfgAbpEntityCash);
        jmiCfgAbp.add(jmiCfgAbpEntityWarehouse);
        jmiCfgAbp.add(jmiCfgAbpEntityPlant);
        jmiCfgAbp.addSeparator();
        jmiCfgAbp.add(jmiCfgAbpBizPartner);
        jmiCfgAbp.addSeparator();
        jmiCfgAbp.add(jmiCfgAbpItem);
        jmiCfgAbp.addSeparator();
        jmiCfgAbp.add(jmiCfgAbpTax);

        jmCfgAbpLink = new JMenu("Configuración contable");
        jmiCfgAbpLinkCashAccount = new JMenuItem("Cuentas de dinero");
        jmiCfgAbpLinkWarehouse = new JMenuItem("Almacenes de bienes");
        jmiCfgAbpLinkPlant = new JMenuItem("Plantas de producción");
        jmiCfgAbpLinkBizPartner = new JMenuItem("Asociados de negocios");
        jmiCfgAbpLinkItem = new JMenuItem("Ítems");
        jmiCfgTaxItemLink = new JMenuItem("Ítems e impuestos");
        jmiCfgAccItemLink = new JMenuItem("Filtros de ítems para cuentas contables");

        jmCfgAbpLink.add(jmiCfgAbpLinkCashAccount);
        jmCfgAbpLink.add(jmiCfgAbpLinkWarehouse);
        jmCfgAbpLink.add(jmiCfgAbpLinkPlant);
        jmCfgAbpLink.addSeparator();
        jmCfgAbpLink.add(jmiCfgAbpLinkBizPartner);
        jmCfgAbpLink.addSeparator();
        jmCfgAbpLink.add(jmiCfgAbpLinkItem);
        jmCfgAbpLink.add(jmiCfgTaxItemLink);
        jmCfgAbpLink.add(jmiCfgAccItemLink);

        jmCfg.add(jmiCfgAbp);
        jmCfg.add(jmCfgAbpLink);

        jmCat = new JMenu("Catálogos");

        jmiCatAccount = new JMenuItem("Cuentas contables");
        jmiCatFiscalAccount = new JMenuItem("Código agrupador de cuentas del SAT...");
        jmiCatCostCenter = new JMenuItem("Centros de costo");
        jmiCatCashAccountCash = new JMenuItem("Cajas");
        jmiCatCashAccountBank = new JMenuItem("Cuentas bancarias");
        jmiCatCardIssuer = new JMenuItem("Emisores de tarjetas");
        jmiCatCheckWallet = new JMenuItem("Chequeras");
        jmiCatCheckFormat = new JMenuItem("Formatos de cheques");
        jmiCatTaxRegion = new JMenuItem("Regiones de impuestos");
        jmiCatTaxIdentity = new JMenuItem("Identidades de impuestos");
        jmiCatTaxBasic = new JMenuItem("Impuestos");
        jmiCatTaxGroup = new JMenuItem("Grupos de impuestos");
        jmiCatConceptAdministrativeType = new JMenuItem("Conceptos administrativos");
        jmiCatConceptTaxableType = new JMenuItem("Conceptos de impuestos");
        jsCatConcept = new JPopupMenu.Separator();

        jmCat.add(jmiCatAccount);
        jmCat.add(jmiCatFiscalAccount);
        jmCat.add(jmiCatCostCenter);
        jmCat.addSeparator();
        jmCat.add(jmiCatCashAccountCash);
        jmCat.add(jmiCatCashAccountBank);
        jmCat.add(jmiCatCardIssuer);
        jmCat.add(jmiCatCheckWallet);
        jmCat.add(jmiCatCheckFormat);
        jmCat.addSeparator();
        jmCat.add(jmiCatTaxRegion);
        jmCat.add(jmiCatTaxIdentity);
        jmCat.add(jmiCatTaxBasic);
        jmCat.add(jmiCatTaxGroup);
        jmCat.addSeparator();
        jmCat.add(jmiCatConceptAdministrativeType);
        jmCat.add(jmiCatConceptTaxableType);
        jmCat.add(jsCatConcept);    // separator

        jmCatSysAcc = new JMenu("Contabilización automática");
        jmiCatSysAccTaxGroupForItem = new JMenuItem("Grupos de impuestos vs. ítems");
        jmiCatSysAccTaxGroupForItemGeneric = new JMenuItem("Grupos de impuestos vs. ítems genéricos");
        jmiCatSysAccAccountForBizPartner = new JMenuItem("Cuentas contables vs. asociados de negocios");
        jmiCatSysAccAccountForItem = new JMenuItem("Cuentas contables vs. ítems");
        jmiCatSysAccAccountForTax = new JMenuItem("Cuentas contables vs. impuestos");
        jmiCatSysAccQryAccountForBizPartner = new JMenuItem("Consulta de cuentas contables vs. asociados de negocios");
        jmiCatSysAccQryAccountForItem = new JMenuItem("Consulta de cuentas contables vs. ítems");
        jmiCatSysAccCostCenterForItem = new JMenuItem("Configuración de centros de costo vs. ítems");

        jmCatSysAcc.add(jmiCatSysAccTaxGroupForItem);
        jmCatSysAcc.add(jmiCatSysAccTaxGroupForItemGeneric);
        jmCatSysAcc.addSeparator();
        jmCatSysAcc.add(jmiCatSysAccAccountForBizPartner);
        jmCatSysAcc.add(jmiCatSysAccAccountForItem);
        jmCatSysAcc.add(jmiCatSysAccAccountForTax);
        jmCatSysAcc.addSeparator();
        jmCatSysAcc.add(jmiCatSysAccQryAccountForBizPartner);
        jmCatSysAcc.add(jmiCatSysAccQryAccountForItem);
        jmCatSysAcc.addSeparator();
        jmCatSysAcc.add(jmiCatSysAccCostCenterForItem);

        jmCat.add(jmCatSysAcc);

        jmBkk = new JMenu("Contabilidad");

        jmiBkkYearOpenClose = new JMenuItem("Apertura/cierre de ejercicios contables");
        jmiBkkFiscalYearClosing = new JMenuItem("Generación de cierre del ejercicio...");
        jmiBkkFiscalYearClosingDel = new JMenuItem("Eliminación de cierre del ejercicio...");
        jmiBkkFiscalYearOpening = new JMenuItem("Generación de saldos iniciales...");
        jmiBkkFiscalYearOpeningDel = new JMenuItem("Eliminación de saldos iniciales...");

        jmBkk.add(jmiBkkYearOpenClose);
        jmBkk.addSeparator();
        jmBkk.add(jmiBkkFiscalYearClosing);
        jmBkk.add(jmiBkkFiscalYearClosingDel);
        jmBkk.addSeparator();
        jmBkk.add(jmiBkkFiscalYearOpening);
        jmBkk.add(jmiBkkFiscalYearOpeningDel);

        jmRec = new JMenu("Pólizas contables");

        jmiRecRec = new JMenuItem("Pólizas contables");
        jmiRecRecEtyXmlIncome = new JMenuItem("Pólizas contables con XML de ingresos");
        jmiRecRecEtyXmlExpenses = new JMenuItem("Pólizas contables con XML de egresos");
        jmiRecRecCash = new JMenuItem("Pólizas contables de cuentas de dinero");
        jmiRecBal = new JMenuItem("Balanza de comprobación");
        jmiRecBalAll = new JMenuItem("Balanza de comprobación completa");
        jmiRecCashAccBalountCash = new JMenuItem("Saldos cajas");
        jmiRecCashAccBalountBank = new JMenuItem("Saldos cuentas bancarias");
        jmiRecBizPartnerBalCus = new JMenuItem("Saldos clientes");
        jmiRecBizPartnerBalSup = new JMenuItem("Saldos proveedores");
        jmiRecBizPartnerBalDbr = new JMenuItem("Saldos deudores diversos");
        jmiRecBizPartnerBalCdr = new JMenuItem("Saldos acreedores diversos");
        jmiRecBalInventory = new JMenuItem("Saldos inventarios");
        jmiRecBalTaxDebit = new JMenuItem("Saldos impuestos a favor");
        jmiRecBalTaxCredit = new JMenuItem("Saldos impuestos a cargo");
        jmiRecBalProfitLoss = new JMenuItem("Saldos pérdidas y ganancias");
        jmiRecBookkeepingMoves = new JMenuItem("Movimientos de cuentas contables");
        jmiRecDpsBizPartnerCus = new JMenuItem("Consulta pólizas contables de docs. de clientes");
        jmiRecDpsBizPartnerSup = new JMenuItem("Consulta pólizas contables de docs. de proveedores");

        jmRec.add(jmiRecRec);
        jmRec.add(jmiRecRecEtyXmlIncome);
        jmRec.add(jmiRecRecEtyXmlExpenses);
        jmRec.add(jmiRecRecCash);
        jmRec.addSeparator();
        jmRec.add(jmiRecBal);
        jmRec.add(jmiRecBalAll);
        jmRec.addSeparator();
        jmRec.add(jmiRecCashAccBalountCash);
        jmRec.add(jmiRecCashAccBalountBank);
        jmRec.addSeparator();
        jmRec.add(jmiRecBizPartnerBalCus);
        jmRec.add(jmiRecBizPartnerBalSup);
        jmRec.add(jmiRecBizPartnerBalDbr);
        jmRec.add(jmiRecBizPartnerBalCdr);
        jmRec.addSeparator();
        jmRec.add(jmiRecBalInventory);
        jmRec.addSeparator();
        jmRec.add(jmiRecBalTaxDebit);
        jmRec.add(jmiRecBalTaxCredit);
        jmRec.addSeparator();
        jmRec.add(jmiRecBalProfitLoss);
        jmRec.addSeparator();
        jmRec.add(jmiRecBookkeepingMoves);
        jmRec.add(jmiRecDpsBizPartnerCus);
        jmRec.add(jmiRecDpsBizPartnerSup);

        jmFin = new JMenu("Finanzas");

        jmiFinExchangeRate = new JMenuItem("Tipos de cambio");
        jmiFinCashCheck = new JMenuItem("Cheques");
        jmiFinCashCounterReceipt = new JMenuItem("Contrarrecibos");
        jsFinCash = new JPopupMenu.Separator();
        jmiFinLayoutBank = new JMenuItem("Layouts de transferencias");
        jmiFinLayoutBankPending = new JMenuItem("Layouts de transferencias por pagar");
        jmiFinLayoutBankDone = new JMenuItem("Layouts de transferencias pagados");
        jmiFinLayoutBankAdvances = new JMenuItem("Layouts de anticipos");
        jmiFinLayoutBankPendingAdvances = new JMenuItem("Layouts de anticipos por pagar");
        jmiFinLayoutBankDoneAdvances = new JMenuItem("Layouts de anticipos pagados");

        jmFin.add(jmiFinExchangeRate);
        jmFin.addSeparator();
        jmFin.add(jmiFinCashCheck);
        jmFin.add(jmiFinCashCounterReceipt);
        jmFin.add(jsFinCash);   // separator
        jmFin.add(jmiFinLayoutBank);
        jmFin.add(jmiFinLayoutBankPending);
        jmFin.add(jmiFinLayoutBankDone);
        jmFin.addSeparator();
        jmFin.add(jmiFinLayoutBankAdvances);
        jmFin.add(jmiFinLayoutBankPendingAdvances);
        jmFin.add(jmiFinLayoutBankDoneAdvances);

        jmRep = new JMenu("Reportes");

        jmRepTrialBal = new JMenu("Balanzas de comprobación");
        jmiRepTrialBalStandard = new JMenuItem("Balanza de comprobación...");
        jmiRepTrialBalCostCenter = new JMenuItem("Balanza de comprobación de centros de costo...");
        jmiRepTrialBalCostCenterItem = new JMenuItem("Balanza de comprobación de centros de costo e ítems...");
        jmiRepTrialBalItemType = new JMenuItem("Balanza de comprobación por tipo de ítems...");
        jmRepFinStat = new JMenu("Estados financieros");
        jmiRepFinStatBalanceSheet = new JMenuItem("Balance general...");
        jmiRepFinStatProfitLossStat = new JMenuItem("Estado de resultados...");
        
        jmRepCashAccBal = new JMenu("Saldos de cuentas de dinero");
        jmiRepCashAccBalCash = new JMenuItem("Saldos de cajas...");
        jmiRepCashAccBalBank = new JMenuItem("Saldos de cuentas bancarias...");
        jmiRepCashAccBal = new JMenuItem("Saldos de cuentas de dinero...");
        jmRepCashAccMovs = new JMenu("Movimientos de cuentas de dinero");
        jmiRepCashAccMovsCash = new JMenuItem("Movimientos de cajas...");
        jmiRepCashAccMovsBank = new JMenuItem("Movimientos de cuentas bancarias...");
        jmiRepCashAccMovsCashDay = new JMenuItem("Movimientos de cajas por día...");
        jmiRepCashAccMovsBankDay = new JMenuItem("Movimientos de cuentas bancarias por día...");
        
        jmRepBizPartnerBal = new JMenu("Saldos de asociados de negocios");
        jmiRepBizPartnerBalCus = new JMenuItem("Saldos de clientes...");
        jmiRepBizPartnerBalSup = new JMenuItem("Saldos de proveedores...");
        jmiRepBizPartnerBalDbr = new JMenuItem("Saldos de deudores diversos...");
        jmiRepBizPartnerBalCdr = new JMenuItem("Saldos de acreedores diversos...");
        jmiRepBizPartnerBalDpsCus = new JMenuItem("Saldos de clientes por documento...");
        jmiRepBizPartnerBalDpsSup = new JMenuItem("Saldos de proveedores por documento...");
        jmRepBizPartnerBalAging = new JMenu("Antigüedad de saldos de asociados de negocios");
        jmiRepBizPartnerBalAgingCus = new JMenuItem("Antigüedad de saldos de clientes...");
        jmiRepBizPartnerBalAgingSup = new JMenuItem("Antigüedad de saldos de proveedores...");
        jmRepBizPartnerStat = new JMenu("Estados de cuenta de asociados de negocios");
        jmiRepBizPartnerStatCus = new JMenuItem("Estados de cuenta de clientes...");
        jmiRepBizPartnerStatSup = new JMenuItem("Estados de cuenta de proveedores...");
        jmiRepBizPartnerStatDbr = new JMenuItem("Estados de cuenta de deudores diversos...");
        jmiRepBizPartnerStatCdr = new JMenuItem("Estados de cuenta de acreedores diversos...");
        jmRepBizPartnerAccMovs = new JMenu("Movimientos contables de asociados de negocios");
        jmiRepBizPartnerAccMovsCus = new JMenuItem("Movimientos contables de clientes por documento...");
        jmiRepBizPartnerAccMovsSup = new JMenuItem("Movimientos contables de proveedores por documento...");
        jmiRepBizPartnerLedger = new JMenu("Auxiliares contables de asociados de negocios");
        jmiRepBizPartnerLedgerCus = new JMenuItem("Reporte de auxiliares contables de clientes...");
        jmiRepBizPartnerLedgerSup = new JMenuItem("Reporte de auxiliares contables de proveedores...");
        jmiRepBizPartnerLedgerDbr = new JMenuItem("Reporte de auxiliares contables de deudores diversos...");
        jmiRepBizPartnerLedgerCdr = new JMenuItem("Reporte de auxiliares contables de acreedores diversos...");
        
        jmRepAccMovs = new JMenu("Movimientos contables de cuentas de sistema");
        jmiRepAccMovsTax = new JMenuItem("Movimientos contables de impuestos...");
        jmiRepAccMovsProfitLoss = new JMenuItem("Movimientos contables de pérdidas y ganancias...");

        jmRepCashFlow = new JMenu("Flujo de efectivo");
        jmiRepCashFlowPaysCus = new JMenuItem("Reporte de cobros por periodo...");
        jmiRepCashFlowPaysSup = new JMenuItem("Reporte de pagos por periodo...");
        jmiQryCashFlowPaysCusSum = new JMenuItem("Consulta de cobros por periodo");
        jmiQryCashFlowPaysCusDet = new JMenuItem("Consulta de cobros por periodo a detalle");
        jmiQryCashFlowPaysSupSum = new JMenuItem("Consulta de pagos por periodo");
        jmiQryCashFlowPaysSupDet = new JMenuItem("Consulta de pagos por periodo a detalle");
        jmiRepCashFlowExpected = new JMenuItem("Reporte de ingresos y egresos esperados por periodo...");
        
        jmiRepLedgerAccount = new JMenuItem("Reporte de auxiliares contables...");
        jmiRepLedgerCostCenter = new JMenuItem("Reporte de auxiliares contables de centros de costo...");
        jmiRepConceptAdmin = new JMenuItem("Reporte de conceptos administrativos...");
        jmiRepConceptTax = new JMenuItem("Reporte de conceptos de impuestos...");
        jsRepAux1 = new JPopupMenu.Separator();
        jmiRepPrtJournalVouchers = new JMenuItem("Impresión de pólizas contables...");
        jmiRepPrtChartOfAccounts = new JMenuItem("Impresión de cuentas contables...");
        jmiRepTaxesByConcept = new JMenuItem("Reporte de impuestos por concepto...");
        
        jmRepFiscal = new JMenu("Reportes fiscales");
        jmiRepFiscalTaxPendSal = new JMenuItem("Impuestos pendientes de ingresos...");
        jmiRepFiscalTaxPendPur = new JMenuItem("Impuestos pendientes de egresos...");
        jmiRepFiscalXmlFiles = new JMenuItem("Archivos de contabilidad electrónica...");
        jmiRepFiscalMonthRepCfd = new JMenuItem("Informe mensual de comprobantes digitales...");
        jmiRepFiscalMonthRepCf = new JMenuItem("Informe mensual de comprobantes impresos...");
        
        jmRepTrialBal.add(jmiRepTrialBalStandard);
        jmRepTrialBal.addSeparator();
        jmRepTrialBal.add(jmiRepTrialBalCostCenter);
        jmRepTrialBal.add(jmiRepTrialBalCostCenterItem);
        jmRepTrialBal.addSeparator();
        jmRepTrialBal.add(jmiRepTrialBalItemType);
        jmRep.add(jmRepTrialBal);
        jmRepFinStat.add(jmiRepFinStatBalanceSheet);
        jmRepFinStat.add(jmiRepFinStatProfitLossStat);
        jmRep.add(jmRepFinStat);
        
        jmRep.addSeparator();

        jmRepCashAccBal.add(jmiRepCashAccBalCash);
        jmRepCashAccBal.add(jmiRepCashAccBalBank);
        jmRepCashAccBal.addSeparator();
        jmRepCashAccBal.add(jmiRepCashAccBal);
        jmRep.add(jmRepCashAccBal);
        jmRepCashAccMovs.add(jmiRepCashAccMovsCash);
        jmRepCashAccMovs.add(jmiRepCashAccMovsBank);
        jmRepCashAccMovs.addSeparator();
        jmRepCashAccMovs.add(jmiRepCashAccMovsCashDay);
        jmRepCashAccMovs.add(jmiRepCashAccMovsBankDay);
        jmRep.add(jmRepCashAccMovs);
        
        jmRep.addSeparator();
        
        jmRepBizPartnerBal.add(jmiRepBizPartnerBalCus);
        jmRepBizPartnerBal.add(jmiRepBizPartnerBalSup);
        jmRepBizPartnerBal.add(jmiRepBizPartnerBalDbr);
        jmRepBizPartnerBal.add(jmiRepBizPartnerBalCdr);
        jmRepBizPartnerBal.addSeparator();
        jmRepBizPartnerBal.add(jmiRepBizPartnerBalDpsCus);
        jmRepBizPartnerBal.add(jmiRepBizPartnerBalDpsSup);
        jmRep.add(jmRepBizPartnerBal);
        jmRepBizPartnerBalAging.add(jmiRepBizPartnerBalAgingCus);
        jmRepBizPartnerBalAging.add(jmiRepBizPartnerBalAgingSup);
        jmRep.add(jmRepBizPartnerBalAging);
        jmRepBizPartnerStat.add(jmiRepBizPartnerStatCus);
        jmRepBizPartnerStat.add(jmiRepBizPartnerStatSup);
        jmRepBizPartnerStat.add(jmiRepBizPartnerStatDbr);
        jmRepBizPartnerStat.add(jmiRepBizPartnerStatCdr);
        jmRep.add(jmRepBizPartnerStat);
        jmRepBizPartnerAccMovs.add(jmiRepBizPartnerAccMovsCus);
        jmRepBizPartnerAccMovs.add(jmiRepBizPartnerAccMovsSup);
        jmRep.add(jmRepBizPartnerAccMovs);
        jmiRepBizPartnerLedger.add(jmiRepBizPartnerLedgerCus);
        jmiRepBizPartnerLedger.add(jmiRepBizPartnerLedgerSup);
        jmiRepBizPartnerLedger.add(jmiRepBizPartnerLedgerDbr);
        jmiRepBizPartnerLedger.add(jmiRepBizPartnerLedgerCdr);
        jmRep.add(jmiRepBizPartnerLedger);

        jmRep.addSeparator();
        
        jmRepAccMovs.add(jmiRepAccMovsTax);
        jmRepAccMovs.add(jmiRepAccMovsProfitLoss);
        jmRep.add(jmRepAccMovs);

        jmRep.addSeparator();
        
        jmRepCashFlow.add(jmiRepCashFlowPaysCus);
        jmRepCashFlow.add(jmiRepCashFlowPaysSup);
        jmRepCashFlow.addSeparator();
        jmRepCashFlow.add(jmiQryCashFlowPaysCusSum);
        jmRepCashFlow.add(jmiQryCashFlowPaysCusDet);
        jmRepCashFlow.add(jmiQryCashFlowPaysSupSum);
        jmRepCashFlow.add(jmiQryCashFlowPaysSupDet);
        jmRep.add(jmRepCashFlow);
        jmRep.add(jmiRepCashFlowExpected);

        jmRep.addSeparator();
        
        jmRep.add(jmiRepLedgerAccount);
        jmRep.add(jmiRepLedgerCostCenter);
        jmRep.add(jmiRepConceptAdmin);
        jmRep.add(jmiRepConceptTax);
        jmRep.add(jsRepAux1);   // separator
        jmRep.add(jmiRepPrtJournalVouchers);
        jmRep.add(jmiRepPrtChartOfAccounts);
        jmRep.addSeparator();
        jmRep.add(jmiRepTaxesByConcept);
        
        jmRep.addSeparator();
        
        jmRepFiscal.add(jmiRepFiscalTaxPendPur);
        jmRepFiscal.add(jmiRepFiscalTaxPendSal);
        jmRepFiscal.addSeparator();
        jmRepFiscal.add(jmiRepFiscalXmlFiles);
        jmRepFiscal.addSeparator();
        jmRepFiscal.add(jmiRepFiscalMonthRepCfd);
        jmRepFiscal.add(jmiRepFiscalMonthRepCf);
        jmRep.add(jmRepFiscal);

        jmiCatAccount.addActionListener(this);
        jmiCatFiscalAccount.addActionListener(this);
        jmiCatCostCenter.addActionListener(this);
        jmiCatCashAccountCash.addActionListener(this);
        jmiCatCashAccountBank.addActionListener(this);
        jmiCatCardIssuer.addActionListener(this);
        jmiCatCheckWallet.addActionListener(this);
        jmiCatCheckFormat.addActionListener(this);
        jmiCatTaxRegion.addActionListener(this);
        jmiCatTaxIdentity.addActionListener(this);
        jmiCatTaxBasic.addActionListener(this);
        jmiCatTaxGroup.addActionListener(this);
        jmiCatConceptAdministrativeType.addActionListener(this);
        jmiCatConceptTaxableType.addActionListener(this);
        jmiCatSysAccTaxGroupForItem.addActionListener(this);
        jmiCatSysAccTaxGroupForItemGeneric.addActionListener(this);
        jmiCatSysAccAccountForBizPartner.addActionListener(this);
        jmiCatSysAccAccountForItem.addActionListener(this);
        jmiCatSysAccAccountForTax.addActionListener(this);
        jmiCatSysAccQryAccountForBizPartner.addActionListener(this);
        jmiCatSysAccQryAccountForItem.addActionListener(this);
        jmiCatSysAccCostCenterForItem.addActionListener(this);

        jmiBkkYearOpenClose.addActionListener(this);
        jmiBkkFiscalYearClosing.addActionListener(this);
        jmiBkkFiscalYearClosingDel.addActionListener(this);
        jmiBkkFiscalYearOpening.addActionListener(this);
        jmiBkkFiscalYearOpeningDel.addActionListener(this);

        jmiRecRec.addActionListener(this);
        jmiRecRecEtyXmlIncome.addActionListener(this);
        jmiRecRecEtyXmlExpenses.addActionListener(this);
        jmiRecRecCash.addActionListener(this);
        jmiRecBal.addActionListener(this);
        jmiRecBalAll.addActionListener(this);
        jmiRecCashAccBalountCash.addActionListener(this);
        jmiRecCashAccBalountBank.addActionListener(this);
        jmiRecBizPartnerBalCus.addActionListener(this);
        jmiRecBizPartnerBalSup.addActionListener(this);
        jmiRecBizPartnerBalDbr.addActionListener(this);
        jmiRecBizPartnerBalCdr.addActionListener(this);
        jmiRecBalInventory.addActionListener(this);
        jmiRecBalTaxDebit.addActionListener(this);
        jmiRecBalTaxCredit.addActionListener(this);
        jmiRecBalProfitLoss.addActionListener(this);
        jmiRecBookkeepingMoves.addActionListener(this);
        jmiRecDpsBizPartnerCus.addActionListener(this);
        jmiRecDpsBizPartnerSup.addActionListener(this);

        jmiFinExchangeRate.addActionListener(this);
        jmiFinCashCheck.addActionListener(this);
        jmiFinCashCounterReceipt.addActionListener(this);
        jmiFinLayoutBank.addActionListener(this);
        jmiFinLayoutBankPending.addActionListener(this);
        jmiFinLayoutBankDone.addActionListener(this);
        jmiFinLayoutBankAdvances.addActionListener(this);
        jmiFinLayoutBankPendingAdvances.addActionListener(this);
        jmiFinLayoutBankDoneAdvances.addActionListener(this);

        jmiCfgAbpEntityCash.addActionListener(this);
        jmiCfgAbpEntityWarehouse.addActionListener(this);
        jmiCfgAbpEntityPlant.addActionListener(this);
        jmiCfgAbpBizPartner.addActionListener(this);
        jmiCfgAbpItem.addActionListener(this);
        jmiCfgAbpTax.addActionListener(this);
        jmiCfgAbpLinkCashAccount.addActionListener(this);
        jmiCfgAbpLinkWarehouse.addActionListener(this);
        jmiCfgAbpLinkPlant.addActionListener(this);
        jmiCfgAbpLinkBizPartner.addActionListener(this);
        jmiCfgAbpLinkItem.addActionListener(this);
        jmiCfgAccItemLink.addActionListener(this);
        jmiCfgTaxItemLink.addActionListener(this);

        jmiRepTrialBalStandard.addActionListener(this);
        jmiRepTrialBalCostCenter.addActionListener(this);
        jmiRepTrialBalCostCenterItem.addActionListener(this);
        jmiRepTrialBalItemType.addActionListener(this);
        jmiRepFinStatBalanceSheet.addActionListener(this);
        jmiRepFinStatProfitLossStat.addActionListener(this);
        jmiRepCashAccBalCash.addActionListener(this);
        jmiRepCashAccBalBank.addActionListener(this);
        jmiRepCashAccBal.addActionListener(this);
        jmiRepCashAccMovsCash.addActionListener(this);
        jmiRepCashAccMovsBank.addActionListener(this);
        jmiRepCashAccMovsCashDay.addActionListener(this);
        jmiRepCashAccMovsBankDay.addActionListener(this);
        jmiRepBizPartnerBalCus.addActionListener(this);
        jmiRepBizPartnerBalSup.addActionListener(this);
        jmiRepBizPartnerBalDbr.addActionListener(this);
        jmiRepBizPartnerBalCdr.addActionListener(this);
        jmiRepBizPartnerBalDpsCus.addActionListener(this);
        jmiRepBizPartnerBalDpsSup.addActionListener(this);
        jmiRepBizPartnerBalAgingCus.addActionListener(this);
        jmiRepBizPartnerBalAgingSup.addActionListener(this);
        jmiRepBizPartnerStatCus.addActionListener(this);
        jmiRepBizPartnerStatSup.addActionListener(this);
        jmiRepBizPartnerStatDbr.addActionListener(this);
        jmiRepBizPartnerStatCdr.addActionListener(this);
        jmiRepBizPartnerAccMovsCus.addActionListener(this);
        jmiRepBizPartnerAccMovsSup.addActionListener(this);
        jmiRepAccMovsTax.addActionListener(this);
        jmiRepAccMovsProfitLoss.addActionListener(this);
        jmiRepCashFlowPaysCus.addActionListener(this);
        jmiRepCashFlowPaysSup.addActionListener(this);
        jmiQryCashFlowPaysCusSum.addActionListener(this);
        jmiQryCashFlowPaysCusDet.addActionListener(this);
        jmiQryCashFlowPaysSupSum.addActionListener(this);
        jmiQryCashFlowPaysSupDet.addActionListener(this);
        jmiRepCashFlowExpected.addActionListener(this);
        
        
        jmiRepBizPartnerLedgerCus.addActionListener(this);
        jmiRepBizPartnerLedgerSup.addActionListener(this);
        jmiRepBizPartnerLedgerDbr.addActionListener(this);
        jmiRepBizPartnerLedgerCdr.addActionListener(this);
        jmiRepLedgerAccount.addActionListener(this);
        jmiRepLedgerCostCenter.addActionListener(this);
        jmiRepConceptAdmin.addActionListener(this);
        jmiRepConceptTax.addActionListener(this);
        jmiRepPrtJournalVouchers.addActionListener(this);
        jmiRepTaxesByConcept.addActionListener(this);
        jmiRepPrtChartOfAccounts.addActionListener(this);
        jmiRepFiscalTaxPendPur.addActionListener(this);
        jmiRepFiscalTaxPendSal.addActionListener(this);
        jmiRepFiscalXmlFiles.addActionListener(this);
        jmiRepFiscalMonthRepCfd.addActionListener(this);
        jmiRepFiscalMonthRepCf.addActionListener(this);

        // User rights:

        hasYearRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_YEAR).HasRight;
        hasYearPeriodRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_PER).HasRight;
        hasExcRateRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_EXC_RATE).HasRight;
        hasCatAccRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_ACC).HasRight;
        hasCatAccCashRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_ACC_CASH).HasRight;
        hasCatCcRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_CC).HasRight;
        hasCatBkcRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_BKC).HasRight;
        hasCatCheckRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_CHECK).HasRight;
        hasCatTaxRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_TAX_GRP).HasRight;
        hasBkrRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_REG).HasRight;
        hasAutAccBpRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_ACC_BP).HasRight;
        hasAutAccItemRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_ACC_ITEM).HasRight;
        hasMoveAccCash = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_MOV_ACC_CASH).HasRight;
        hasMoveBpCdr = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_MOV_CDR).HasRight;
        hasMoveBpDbr = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_MOV_DBR).HasRight;

        hasCounterReceiptRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_COUNTER_RCPT).HasRight;
        hasRepStatementRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_REP_STATS).HasRight;
        hasRepFinRateRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_REP_INDEX).HasRight;
        hasRepRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_REP).HasRight;
        hasGblCatAccCfg = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_FIN_ACC_CFG).HasRight;
        hasGblCatAccTax = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_FIN_ACC_TAX).HasRight;
        hasGblCatAccMisc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_FIN_ACC_MISC).HasRight;

        jmCfg.setEnabled(hasGblCatAccCfg || hasAutAccBpRight || hasAutAccItemRight || hasGblCatAccTax);

        jmCfgAbpLink.setEnabled(hasGblCatAccCfg || hasAutAccBpRight || hasAutAccItemRight);
        jmiCfgAbpLinkCashAccount.setEnabled(hasGblCatAccCfg);
        jmiCfgAbpLinkWarehouse.setEnabled(hasGblCatAccCfg);
        jmiCfgAbpLinkPlant.setEnabled(hasGblCatAccCfg);
        jmiCfgAbpLinkBizPartner.setEnabled(hasAutAccBpRight);
        jmiCfgAbpLinkItem.setEnabled(hasAutAccItemRight);
        jmiCfgAccItemLink.setEnabled(hasAutAccItemRight);
        jmiCfgTaxItemLink.setEnabled(hasAutAccItemRight);
        jmiCfgAbp.setEnabled(hasGblCatAccCfg || hasAutAccBpRight || hasAutAccItemRight || hasGblCatAccTax);
        jmiCfgAbpEntityCash.setEnabled(hasGblCatAccCfg);
        jmiCfgAbpEntityWarehouse.setEnabled(hasGblCatAccCfg);
        jmiCfgAbpEntityPlant.setEnabled(hasGblCatAccCfg);
        jmiCfgAbpBizPartner.setEnabled(hasAutAccBpRight);
        jmiCfgAbpItem.setEnabled(hasAutAccItemRight);
        jmiCfgAbpTax.setEnabled(hasGblCatAccTax);

        jmCat.setEnabled(
                hasGblCatAccCfg || hasExcRateRight || hasGblCatAccTax || hasGblCatAccMisc ||
                hasCatAccRight || hasCatCcRight || hasCatAccCashRight || hasCatBkcRight || hasCatCheckRight || hasCatTaxRight ||
                hasBkrRight || hasAutAccBpRight || hasAutAccItemRight);
        jmiCatAccount.setEnabled(hasGblCatAccCfg || hasCatAccRight);
        jmiCatFiscalAccount.setEnabled(hasGblCatAccCfg || hasCatAccRight);
        jmiCatCostCenter.setEnabled(hasGblCatAccCfg || hasCatCcRight);
        jmiCatCashAccountCash.setEnabled(hasGblCatAccCfg || hasCatAccCashRight);
        jmiCatCashAccountBank.setEnabled(hasGblCatAccCfg || hasCatAccCashRight);
        jmiCatCardIssuer.setEnabled(hasGblCatAccCfg || hasCatAccCashRight);
        jmiCatCheckWallet.setEnabled(hasGblCatAccCfg || hasCatCheckRight);
        jmiCatCheckFormat.setEnabled(hasGblCatAccCfg || hasCatCheckRight);
        jmiCatTaxRegion.setEnabled(hasGblCatAccCfg || hasCatTaxRight);
        jmiCatTaxIdentity.setEnabled(hasGblCatAccCfg || hasCatTaxRight);
        jmiCatTaxBasic.setEnabled(hasGblCatAccCfg || hasGblCatAccTax || hasCatTaxRight);
        jmiCatTaxGroup.setEnabled(hasGblCatAccCfg || hasGblCatAccTax || hasCatTaxRight);
        jmiCatConceptAdministrativeType.setEnabled(hasGblCatAccCfg || hasGblCatAccMisc);
        jmiCatConceptTaxableType.setEnabled(hasGblCatAccCfg || hasGblCatAccMisc);

        jmCatSysAcc.setEnabled(hasGblCatAccCfg || hasGblCatAccTax || hasAutAccBpRight || hasAutAccItemRight);
        //jmiCatSysAccTaxGroupForItem.setEnabled(hasGblCatAccTax);
        jmiCatSysAccTaxGroupForItem.setEnabled(false);          // no longer needed (sflores, 2013-11-12)
        //jmiCatSysAccTaxGroupForItemGeneric.setEnabled(hasGblCatAccTax);
        jmiCatSysAccTaxGroupForItemGeneric.setEnabled(false);   // no longer needed (sflores, 2013-11-12)
        jmiCatSysAccAccountForBizPartner.setEnabled(hasAutAccBpRight);
        jmiCatSysAccAccountForItem.setEnabled(hasAutAccItemRight);
        jmiCatSysAccAccountForTax.setEnabled(hasGblCatAccTax);
        jmiCatSysAccCostCenterForItem.setEnabled(hasGblCatAccCfg);

        jmBkk.setEnabled(hasYearRight || hasYearPeriodRight);
        jmiBkkYearOpenClose.setEnabled(hasYearPeriodRight);
        jmiBkkFiscalYearClosing.setEnabled(hasYearRight);
        jmiBkkFiscalYearClosingDel.setEnabled(hasYearRight);
        jmiBkkFiscalYearOpening.setEnabled(hasYearRight);
        jmiBkkFiscalYearOpeningDel.setEnabled(hasYearRight);

        jmRec.setEnabled(hasBkrRight || hasRepRight || hasMoveAccCash || hasMoveBpCdr || hasMoveBpDbr);
        jmiRecRec.setEnabled(hasBkrRight);
        jmiRecRecEtyXmlIncome.setEnabled(hasBkrRight);
        jmiRecRecEtyXmlExpenses.setEnabled(hasBkrRight);
        jmiRecRecCash.setEnabled(hasBkrRight);
        jmiRecRecCash.setEnabled(false);    // XXX temporal code!!! (sflores, 2013-07-27)
        jmiRecBal.setEnabled(hasBkrRight || hasRepRight);
        jmiRecBalAll.setEnabled(hasBkrRight || hasRepRight);
        jmiRecCashAccBalountCash.setEnabled(hasBkrRight || hasMoveAccCash || hasRepRight);
        jmiRecCashAccBalountBank.setEnabled(hasBkrRight || hasMoveAccCash || hasRepRight);
        jmiRecBizPartnerBalCus.setEnabled(hasBkrRight || hasRepRight);
        jmiRecBizPartnerBalSup.setEnabled(hasBkrRight || hasRepRight);
        jmiRecBizPartnerBalDbr.setEnabled(hasBkrRight || hasMoveBpDbr || hasRepRight);
        jmiRecBizPartnerBalCdr.setEnabled(hasBkrRight || hasMoveBpCdr || hasRepRight);
        jmiRecBalInventory.setEnabled(hasBkrRight || hasRepRight);
        jmiRecBalTaxDebit.setEnabled(hasBkrRight || hasRepRight);
        jmiRecBalTaxCredit.setEnabled(hasBkrRight || hasRepRight);
        jmiRecBalProfitLoss.setEnabled(hasBkrRight || hasRepRight);
        jmiRecBookkeepingMoves.setEnabled(hasBkrRight || hasRepRight);
        jmiRecDpsBizPartnerCus.setEnabled(hasBkrRight || hasRepRight);
        jmiRecDpsBizPartnerSup.setEnabled(hasBkrRight || hasRepRight);

        jmFin.setEnabled(hasExcRateRight || hasMoveAccCash || hasCounterReceiptRight);
        jmiFinExchangeRate.setEnabled(hasExcRateRight);
        jmiFinCashCheck.setEnabled(hasMoveAccCash);
        jmiFinCashCounterReceipt.setEnabled(hasCounterReceiptRight);
        jmiFinLayoutBank.setEnabled(hasMoveAccCash);
        jmiFinLayoutBankPending.setEnabled(hasMoveAccCash);
        jmiFinLayoutBankDone.setEnabled(hasMoveAccCash);
        jmiFinLayoutBankAdvances.setEnabled(hasMoveAccCash);
        jmiFinLayoutBankPendingAdvances.setEnabled(hasMoveAccCash);
        jmiFinLayoutBankDoneAdvances.setEnabled(hasMoveAccCash);

        jmRep.setEnabled(hasRepRight || hasRepFinRateRight || hasRepStatementRight);
        jmRepFinStat.setEnabled(hasRepStatementRight);
        
        // GUI configuration:
        
        if (((erp.SClient) miClient).getCfgProcesor() != null) {
            SCfgModule module = new SCfgModule("" + mnModuleType);
            SCfgMenu menu = null;
            SCfgMenuSection section = null;
            
            menu = new SCfgMenu(jmCfg, "" + SDataConstants.MOD_FIN_CFG);
            module.getChildMenus().add(menu);
            
            menu = new SCfgMenu(jmCat, "" + SDataConstants.MOD_FIN_CAT);
            section = new SCfgMenuSection("" + SDataConstants.MOD_FIN_CAT_CPT);
            section.getChildItems().add(new SCfgMenuSectionItem(jmiCatConceptAdministrativeType, "" + SDataConstants.FINU_TP_ADM_CPT));
            section.getChildItems().add(new SCfgMenuSectionItem(jmiCatConceptTaxableType, "" + SDataConstants.FINU_TP_TAX_CPT));
            section.setChildSeparator(new SCfgMenuSectionSeparator(jsCatConcept));
            menu.getChildSections().add(section);
            module.getChildMenus().add(menu);
            
            menu = new SCfgMenu(jmCat, "" + SDataConstants.MOD_FIN_FIN);
            section = new SCfgMenuSection("" + SDataConstants.MOD_FIN_FIN_CSH);
            section.getChildItems().add(new SCfgMenuSectionItem(jmiFinCashCheck, "" + SDataConstants.FIN_CHECK));
            section.getChildItems().add(new SCfgMenuSectionItem(jmiFinCashCounterReceipt, "" + SDataConstants.TRN_CTR));
            section.setChildSeparator(new SCfgMenuSectionSeparator(jsFinCash));
            menu.getChildSections().add(section);
            module.getChildMenus().add(menu);
            
            menu = new SCfgMenu(jmCat, "" + SDataConstants.MOD_FIN_REP);
            section = new SCfgMenuSection("" + SDataConstants.MOD_FIN_REP_AUX);
            section.getChildItems().add(new SCfgMenuSectionItem(jmiRepLedgerAccount, "" + SDataConstants.FIN_ACC));
            section.getChildItems().add(new SCfgMenuSectionItem(jmiRepLedgerCostCenter, "" + SDataConstants.FIN_CC));
            section.getChildItems().add(new SCfgMenuSectionItem(jmiRepConceptAdmin, "" + SDataConstants.FINU_TP_ADM_CPT));
            section.getChildItems().add(new SCfgMenuSectionItem(jmiRepConceptTax, "" + SDataConstants.FINU_TP_TAX_CPT));
            section.setChildSeparator(new SCfgMenuSectionSeparator(jsRepAux1));
            menu.getChildSections().add(section);
            module.getChildMenus().add(menu);
            
            ((erp.SClient) miClient).getCfgProcesor().processModule(module);
        }
    }

    private void actionFinFiscalYearClosing() {
        SDialogUtilFiscalYearClosing dialog = new SDialogUtilFiscalYearClosing(miClient);
        dialog.resetForm();
        dialog.setVisible(true);
    }

    private void actionFinFiscalYearClosingDel() {
        SDialogUtilFiscalYearClosingDelete dialog = new SDialogUtilFiscalYearClosingDelete(miClient);
        dialog.resetForm();
        dialog.setVisible(true);
    }

    private void actionFinFiscalYearOpening() {
        SDialogUtilFiscalYearOpening dialog = new SDialogUtilFiscalYearOpening(miClient);
        dialog.resetForm();
        dialog.setVisible(true);
    }

    private void actionFinFiscalYearOpeningDel() {
        SDialogUtilFiscalYearOpeningDelete dialog = new SDialogUtilFiscalYearOpeningDelete(miClient);
        dialog.resetForm();
        dialog.setVisible(true);
    }

    private void actionCatFiscalAccounts() {
        try {
            SFiscalAccounts fiscalAccounts = null;
            SDialogFiscalAccountsConfig dialogFiscalAccountsConfig = new SDialogFiscalAccountsConfig(miClient.getSession().getClient(), "Configuración código agrupador de cuentas del SAT");
            dialogFiscalAccountsConfig.setRegistry(null);
            dialogFiscalAccountsConfig.setVisible(true);
            if (dialogFiscalAccountsConfig.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                fiscalAccounts = dialogFiscalAccountsConfig.getRegistry();
                fiscalAccounts.save(miClient.getSession());
            }
            miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(SDataConstants.FIN_ACC);
        }
        catch (Exception exception) {
            SLibUtils.showException(this, exception);
        }
    }

    private int showForm(int formType, int auxType, java.lang.Object pk, boolean isCopy) {
        int result = SLibConstants.UNDEFINED;

        try {
            clearFormMembers();
            setFrameWaitCursor();

            switch (formType) {
                case SDataConstants.FIN_REC:
                    if (moFormRecord == null) {
                        moFormRecord = new SFormRecord(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataRecord();
                    }
                    miForm = moFormRecord;
                    break;
                case SDataConstants.FINX_REC_RO:
                    if (moFormRecordRo == null) {
                        moFormRecordRo = new SFormRecord(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataRecord();
                    }
                    miForm = moFormRecordRo;
                    break;
                case SDataConstants.FINX_REC_HEADER:
                    if (moFormRecordHeader == null) {
                        moFormRecordHeader = new SFormRecordHeader(miClient);

                        if (moFormComplement != null) {
                            moFormRecordHeader.setValue(SDataConstants.FINU_TP_REC, moFormComplement);
                        }
                    }
                    if (pk != null) {
                        moRegistry = new SDataRecord();
                    }
                    miForm = moFormRecordHeader;
                    miForm.setValue(SLibConstants.VALUE_IS_COPY, isCopy);
                    break;
                case SDataConstants.FIN_ACC:
                    switch (auxType) {
                        case SDataConstantsSys.FINX_ACC:
                            if (moFormAccount == null) {
                               moFormAccount = new SFormAccount(miClient, auxType);                       
                            }
                            miForm = moFormAccount;
                            break;
                        case SDataConstantsSys.FINX_ACC_DEEP:
                            if (moFormAccountChangeDeep == null) {
                                moFormAccountChangeDeep = new SFormAccount(miClient, auxType);                       
                            }
                            if (moFormComplement != null) {
                                moFormAccountChangeDeep.setValue(SDataConstants.FIN_ACC, moFormComplement);
                            }
                            miForm = moFormAccountChangeDeep;
                            break;
                        default:
                    }
                    if (pk != null) {
                        moRegistry = new SDataAccount();
                    }
                    break;
                case SDataConstants.FINX_ACC_MAJOR:
                    if (moFormAccountMajor == null) {
                        moFormAccountMajor = new SFormAccountMajor(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataAccount();
                    }
                    miForm = moFormAccountMajor;
                    break;
                case SDataConstants.FIN_CC:
                    if (moFormCostCenter == null) {
                        moFormCostCenter = new SFormCostCenter(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataCostCenter();
                    }
                    miForm = moFormCostCenter;
                    break;
                case SDataConstants.FINX_CC_MAJOR:
                    if (moFormCostCenterMajor == null) {
                        moFormCostCenterMajor = new SFormCostCenterMajor(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataCostCenter();
                    }
                    miForm = moFormCostCenterMajor;
                    break;
                case SDataConstants.FINX_ACC_CASH_CASH:
                    if (moFormAccountCash == null) {
                        moFormAccountCash = new SFormAccountCashCash(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataAccountCash();
                    }
                    miForm = moFormAccountCash;
                    break;
                case SDataConstants.FINX_ACC_CASH_BANK:
                    if (moFormAccountCashBank == null) {
                        moFormAccountCashBank = new SFormAccountCashBank(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataAccountCash();
                    }
                    miForm = moFormAccountCashBank;
                    break;
                case SDataConstants.FINU_CARD_ISS:
                    if (moFormCardIssuer == null) {
                        moFormCardIssuer = new SFormCardIssuer(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataCardIssuer();
                    }
                    miForm = moFormCardIssuer;
                    break;
                case SDataConstants.FINU_TAX_REG:
                    if (moFormTaxRegion == null) {
                        moFormTaxRegion = new SFormTaxRegion(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataTaxRegion();
                    }
                    miForm = moFormTaxRegion;
                    break;
                case SDataConstants.FINU_TAX_IDY:
                    if (moFormTaxIdentity == null) {
                        moFormTaxIdentity = new SFormTaxIdentity(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataTaxIdentity();
                    }
                    miForm = moFormTaxIdentity;
                    break;
                case SDataConstants.FINU_TP_ADM_CPT:
                    if (moFormAdministrativeConceptType == null) {
                        moFormAdministrativeConceptType = new SFormAdministrativeConceptType(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataAdministrativeConceptType();
                    }
                    miForm = moFormAdministrativeConceptType;
                    break;
                case SDataConstants.FINU_TP_TAX_CPT:
                    if (moFormTaxableConceptType == null) {
                        moFormTaxableConceptType = new SFormTaxableConceptType(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataTaxableConceptType();
                    }
                    miForm = moFormTaxableConceptType;
                    break;
                case SDataConstants.FINU_TAX_BAS:
                    if (moFormTaxBas == null) {
                        moFormTaxBas = new SFormTaxBasic(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataTaxBasic();
                    }
                    miForm = moFormTaxBas;
                    break;
                case SDataConstants.FINU_CHECK_FMT:
                    if (moFormCheckFormat == null) {
                        moFormCheckFormat = new SFormCheckFormat(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataCheckPrintingFormat();
                    }
                    miForm = moFormCheckFormat;
                    break;
                case SDataConstants.FIN_TAX_GRP:
                    if (moFormTaxGroup == null) {
                        moFormTaxGroup = new SFormTaxGroup(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataTaxGroup();
                    }
                    miForm = moFormTaxGroup;
                    break;
                case SDataConstants.FIN_CHECK_WAL:
                    if (moFormCheckWallet == null) {
                        moFormCheckWallet = new SFormCheckWallet(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataCheckWallet();
                    }
                    miForm = moFormCheckWallet;
                    break;
                case SDataConstants.FIN_CHECK:
                    if (moFormCheckAnnuled == null) {
                        moFormCheckAnnuled = new SFormCheckAnnuled(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataCheck();
                    }
                    miForm = moFormCheckAnnuled;
                    break;
                case SDataConstants.FIN_TAX_GRP_ITEM:
                    if (moFormTaxGroupItem == null) {
                        moFormTaxGroupItem = new SFormTaxGroupAllItem(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataTaxGroupAllItem();
                    }
                    miForm = moFormTaxGroupItem;
                    break;
                case SDataConstants.FIN_TAX_GRP_IGEN:
                    if (moFormTaxGroupItemGeneric == null) {
                        moFormTaxGroupItemGeneric = new SFormTaxGroupAllItemGeneric(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataTaxGroupAllItemGeneric();
                    }
                    miForm = moFormTaxGroupItemGeneric;
                    break;
                case SDataConstants.FIN_ACC_BP:
                    if (moFormAccountBizPartner == null) {
                        moFormAccountBizPartner = new SFormAccountBizPartner(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataAccountBizPartner();
                    }
                    miForm = moFormAccountBizPartner;
                    break;
                case SDataConstants.FIN_ACC_ITEM:
                    if (moFormAccountItem == null) {
                        moFormAccountItem = new SFormAccountItem(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataAccountItem();
                    }
                    miForm = moFormAccountItem;
                    break;
                case SDataConstants.FIN_ACC_TAX:
                    if (moFormAccountTax == null) {
                        moFormAccountTax = new SFormAccountTax(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataAccountTax();
                    }
                    miForm = moFormAccountTax;
                    break;
                case SDataConstants.FIN_YEAR:
                    if (moFormYear == null) {
                        moFormYear = new SFormYear(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataYear();
                    }
                    miForm = moFormYear;
                    break;
                case SDataConstants.FIN_EXC_RATE:
                    if (moFormExchangeRate == null) {
                        moFormExchangeRate = new SFormExchangeRate(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataExchangeRate();
                    }
                    miForm = moFormExchangeRate;
                    break;
                case SDataConstants.FIN_CC_ITEM:
                    if (moFormItemConfig == null) {
                        moFormItemConfig = new SFormCostCenterItem(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataCostCenterItem();
                    }
                    miForm = moFormItemConfig;
                    break;
                case SDataConstants.TRN_CTR:
                    if (moFormCtr == null) {
                        moFormCtr = new SFormCtr(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataCtr();
                    }
                    miForm = moFormCtr;
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM);
            }

            // Additional configuration, if applies:

            switch (formType) {
                case SDataConstants.FIN_REC:
                    ((SFormRecord) miForm).setValue(SLibConstants.VALUE_STATUS, mbIsFormReadOnly);
                    break;
                case SDataConstants.FINX_REC_RO:
                    ((SFormRecord) miForm).setValue(SLibConstants.VALUE_STATUS, true);
                    break;
                default:
            }

            result = processForm(pk, isCopy);
            clearFormComplement();
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            restoreFrameCursor();
        }

        return result;
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
                case SDataConstants.FIN_ACC:
                    oViewClass = erp.mfin.view.SViewAccount.class;
                    sViewTitle = "Ctas. contables";
                    break;
                case SDataConstants.FIN_CC:
                    oViewClass = erp.mfin.view.SViewCostCenter.class;
                    sViewTitle = "Centros costo";
                    break;
                case SDataConstants.FINX_ACC_CASH_CASH:
                    oViewClass = erp.mfin.view.SViewAccountCashCash.class;
                    sViewTitle = "Cajas";
                    break;
                case SDataConstants.FINX_ACC_CASH_BANK:
                    oViewClass = erp.mfin.view.SViewAccountCashBank.class;
                    sViewTitle = "Ctas. bancarias";
                    break;
                case SDataConstants.FINU_CARD_ISS:
                    oViewClass = erp.mfin.view.SViewCardIssuer.class;
                    sViewTitle = "Emisores tarjetas";
                    break;
                case SDataConstants.FIN_CHECK_WAL:
                    oViewClass = erp.mfin.view.SViewCheckWallet.class;
                    sViewTitle = "Chequeras";
                    break;
                case SDataConstants.FINU_CHECK_FMT:
                    oViewClass = erp.mfin.view.SViewCheckFormat.class;
                    sViewTitle = "Formatos cheques";
                    break;
                case SDataConstants.FINU_TAX_REG:
                    oViewClass = erp.mfin.view.SViewTaxRegion.class;
                    sViewTitle = "Regiones impuestos";
                    break;
                case SDataConstants.FINU_TAX_IDY:
                    oViewClass = erp.mfin.view.SViewTaxIdentity.class;
                    sViewTitle = "Identidades impuestos";
                    break;
                case SDataConstants.FINU_TAX_BAS:
                    oViewClass = erp.mfin.view.SViewTaxBasic.class;
                    sViewTitle = "Impuestos";
                    break;
                case SDataConstants.FIN_TAX_GRP:
                    oViewClass = erp.mfin.view.SViewTaxGroup.class;
                    sViewTitle = "Gpos. impuestos";
                    break;
                case SDataConstants.FINU_TP_ADM_CPT:
                    oViewClass = erp.mfin.view.SViewAdministrativeConceptType.class;
                    sViewTitle = "Conceptos administrativos";
                    break;
                case SDataConstants.FINU_TP_TAX_CPT:
                    oViewClass = erp.mfin.view.SViewTaxableConceptType.class;
                    sViewTitle = "Conceptos impuestos";
                    break;
                case SDataConstants.FIN_TAX_GRP_ITEM:
                    oViewClass = erp.mfin.view.SViewTaxGroupItem.class;
                    sViewTitle = "Gpos. impuestos vs. ítems";
                    break;
                case SDataConstants.FIN_TAX_GRP_IGEN:
                    oViewClass = erp.mfin.view.SViewTaxGroupItemGeneric.class;
                    sViewTitle = "Gpos. impuestos vs. ítems gen.";
                    break;
                case SDataConstants.FIN_ACC_BP:
                    oViewClass = erp.mfin.view.SViewAccountBizPartner.class;
                    sViewTitle = "Ctas. contables vs. asocs. negs.";
                    break;
                case SDataConstants.FIN_ACC_ITEM:
                    oViewClass = erp.mfin.view.SViewAccountItem.class;
                    sViewTitle = "Ctas. contables vs. ítems";
                    break;
                case SDataConstants.FIN_ACC_TAX:
                    oViewClass = erp.mfin.view.SViewAccountTax.class;
                    sViewTitle = "Ctas. contables vs. impuestos";
                    break;
                case SDataConstants.FINX_ACC_BP_QRY:
                    oViewClass = erp.mfin.view.SViewAccountBizPartnerQuery.class;
                    sViewTitle = "Consulta ctas. contables vs. asocs. negs.";
                    break;
                case SDataConstants.FINX_ACC_ITEM_QRY:
                    oViewClass = erp.mfin.view.SViewAccountItemQuery.class;
                    sViewTitle = "Consulta ctas. contables vs. ítems";
                    break;
                case SDataConstants.FIN_CC_ITEM:
                    oViewClass = erp.mfin.view.SViewCostCenterItem.class;
                    sViewTitle = "Config. centros costo vs. ítems";
                    break;
                case SDataConstants.FIN_YEAR:
                    oViewClass = erp.mfin.view.SViewYear.class;
                    sViewTitle = "Apertura/cierre ejercicios contab.";
                    break;

                case SDataConstants.FIN_REC:
                    oViewClass = erp.mfin.view.SViewRecord.class;
                    sViewTitle = "Pólizas contab.";
                    break;
                case SDataConstants.FIN_REC_ETY:
                    switch(auxType01){
                        case SDataConstantsSys.TRNS_CT_DPS_PUR:
                            oViewClass = erp.mfin.view.SViewRecordEntriesXml.class;
                            sViewTitle = "Pólizas contab. con XML egresos";
                            break;
                        case SDataConstantsSys.TRNS_CT_DPS_SAL:
                            oViewClass = erp.mfin.view.SViewRecordEntriesXml.class;
                            sViewTitle = "Pólizas contab. con XML ingresos";
                            break;
                    }
                    break;
                case SDataConstants.FINX_REC_CASH:
                    oViewClass = erp.mfin.view.SViewRecordCash.class;
                    sViewTitle = "Pólizas contab. ctas. dinero";
                    break;
                case SDataConstants.FINX_ACCOUNTING:
                    oViewClass = erp.mfin.view.SPanelAccounting.class;
                    switch (auxType01) {
                        case SDataConstants.FINX_ACCOUNTING:
                            sViewTitle = "Balanza comprobación";
                            break;
                        case SDataConstants.FINX_ACCOUNTING_ALL:
                            sViewTitle = "Balanza comprobación completa";
                            break;
                        case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH:
                            sViewTitle = "Saldos cajas";
                            break;
                        case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK:
                            sViewTitle = "Saldos cuentas bancarias";
                            break;
                        case SDataConstantsSys.FINS_TP_ACC_SYS_CUS:
                            sViewTitle = "Saldos clientes";
                            break;
                        case SDataConstantsSys.FINS_TP_ACC_SYS_SUP:
                            sViewTitle = "Saldos proveedores";
                            break;
                        case SDataConstantsSys.FINS_TP_ACC_SYS_DBR:
                            sViewTitle = "Saldos deudores diversos";
                            break;
                        case SDataConstantsSys.FINS_TP_ACC_SYS_CDR:
                            sViewTitle = "Saldos acreedores diversos";
                            break;
                        case SDataConstantsSys.FINS_TP_ACC_SYS_INV:
                            sViewTitle = "Saldos inventarios";
                            break;
                        case SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT:
                            sViewTitle = "Saldos impuestos a favor";
                            break;
                        case SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT:
                            sViewTitle = "Saldos impuestos a cargo";
                            break;
                        case SDataConstantsSys.FINS_TP_ACC_SYS_PROF_LOSS:
                            sViewTitle = "Saldos pérdidas ganancias";
                            break;
                        case SDataConstants.FINX_MOVES_ACC:
                            oViewClass = erp.mfin.view.SViewAccountMoves.class;
                            sViewTitle = "Movimientos de cuentas contables";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                case SDataConstants.FINX_REC_DPS:
                    oViewClass = erp.mfin.view.SViewRecordDps.class;
                    switch (auxType01) {
                        case SDataConstantsSys.BPSS_CT_BP_CUS:
                            sViewTitle = "Consulta pólizas contab. docs. clientes";
                            break;
                        case SDataConstantsSys.BPSS_CT_BP_SUP:
                            sViewTitle = "Consulta pólizas contab. docs. proveedores";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                case SDataConstants.FIN_EXC_RATE:
                    oViewClass = erp.mfin.view.SViewExchangeRate.class;
                    sViewTitle = "Tipos cambio";
                    break;
                case SDataConstants.FIN_CHECK:
                    oViewClass = erp.mfin.view.SViewCheck.class;
                    sViewTitle = "Cheques";
                    break;
                case SDataConstants.TRN_CTR:
                    oViewClass = erp.mtrn.view.SViewCtr.class;
                    sViewTitle = "Contrarecibos";
                    break;
                case SDataConstants.TRNX_DPS_PAYS:
                    if (auxType01 == SDataConstantsSys.TRNS_CT_DPS_PUR) {
                        sViewTitle = "Pagos periodo";
                    }
                    else if (auxType01 == SDataConstantsSys.TRNS_CT_DPS_SAL) {
                        sViewTitle = "Cobros periodo";
                    }
                    if (auxType02 == SUtilConsts.QRY_DET) {
                        sViewTitle += " (detalle)";
                    }
                    oViewClass = erp.mfin.view.SViewDpsPayment.class;
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
        SFormOptionPickerInterface picker = null;

        try {
            switch (optionType) {
                case SDataConstants.FIN_ACC:
                    picker = moPickerAccount = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerAccount);
                    break;
                case SDataConstants.FIN_CC:
                    picker = moPickerCostCenter = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerCostCenter);
                    break;
                case SDataConstants.FIN_BKC:
                    picker = moPickerCostCenter = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBookkeepingCenter);
                    break;
                case SDataConstants.FIN_EXC_RATE:
                    picker = moPickerExchangeRate = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerExchangeRate);
                    break;
                case SDataConstants.FIN_TAX_GRP:
                    picker = moPickerTaxGroup = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerTaxGroup);
                    break;
                case SDataConstants.FIN_ACC_CASH:
                    picker = moPickerAccountCash = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerAccountCash);
                    break;
                case SDataConstants.FINX_ACC_CASH_CASH:
                    picker = moPickerAccountCashCash = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerAccountCashCash);
                    break;
                case SDataConstants.FINX_ACC_CASH_BANK:
                    picker = moPickerAccountCashBank = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerAccountCashBank);
                    break;
                case SDataConstants.FINX_ACC_CASH_BANK_CHECK:
                    picker = moPickerAccountCashBankCheck = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerAccountCashBankCheck);
                    break;
                case SDataConstants.FINS_TP_ACC_SPE:
                    picker = moPickerAccountSpecializedType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerAccountSpecializedType);
                    break;
                case SDataConstants.FINS_FISCAL_ACC:
                    picker = moPickerFiscalAccount = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerFiscalAccount);
                    break;
                case SDataConstants.FINS_FISCAL_BANK:
                    picker = moPickerFiscalBank = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerFiscalBank);
                    break;
                case SDataConstants.FINU_TP_ACC_USR:
                    picker = moPickerAccountUserType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerAccountUserType);
                    break;
                case SDataConstants.FINU_CL_ACC_USR:
                    picker = moPickerAccountUserClass = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerAccountUserClass);
                    break;
                case SDataConstants.FINU_CLS_ACC_USR:
                    picker = moPickerAccountUserSubclass = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerAccountUserSubclass);
                    break;
                case SDataConstants.FINU_TP_ACC_LEDGER:
                    picker = moPickerAccountLedgerType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerAccountLedgerType);
                    break;
                case SDataConstants.FINU_TP_ACC_EBITDA:
                    picker = moPickerAccountEbitdaType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerAccountEbitdaType);
                    break;
                case SDataConstants.FINU_TP_REC:
                    picker = moPickerRecordType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerRecordType);
                    break;
                case SDataConstants.FINX_TP_REC_ALL:
                    picker = moPickerRecordTypeAll = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerRecordTypeAll);
                    break;
                case SDataConstants.FINX_TP_REC_USER:
                    picker = moPickerRecordTypeUser = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerRecordTypeUser);
                    break;
                case SDataConstants.FINU_TP_ADM_CPT:
                    picker = moPickerAdministrativeConceptType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerAdministrativeConceptType);
                    break;
                case SDataConstants.FINU_TP_TAX_CPT:
                    picker = moPickerTaxableConceptType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerTaxableConceptType);
                    break;
                case SDataConstants.FINU_CARD_ISS:
                    picker = moPickerCardIssuer = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerCardIssuer);
                    break;
                case SDataConstants.FINU_CHECK_FMT:
                    picker = moPickerCheckFormat = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerCheckFormat);
                    break;
                case SDataConstants.FINU_CHECK_FMT_GP:
                    picker = moPickerCheckFormatGp = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerCheckFormatGp);
                    break;
                case SDataConstants.FINU_TAX_REG:
                    picker = moPickerTaxRegion = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerTaxRegion);
                    break;
                case SDataConstants.FINU_TAX_IDY:
                    picker = moPickerTaxIdentity = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerTaxIdentity);
                    break;
                case SDataConstants.FINU_TAX_BAS:
                    picker = moPickerTaxBas = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerTaxBas);
                    break;
                case SDataConstants.FINU_TAX:
                    picker = moPickerTax = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerTax);
                    break;
                case SDataConstants.FIN_YEAR:
                    picker = moPickerYear = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerTax);
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM_PICK);
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }

        return picker;
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
        return new JMenu[] { jmCfg, jmCat, jmBkk, jmRec, jmFin, jmRep };
    }

    @Override
    public javax.swing.JMenu[] getMenuesForModule(int moduleType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JMenuItem) {
            javax.swing.JMenuItem item = (javax.swing.JMenuItem) e.getSource();

            if (item == jmiCatAccount) {
                showView(SDataConstants.FIN_ACC);
            }
            else if (item == jmiCatFiscalAccount) {
                actionCatFiscalAccounts();
            }
            else if (item == jmiCatCostCenter) {
                showView(SDataConstants.FIN_CC);
            }
            else if (item == jmiCatCashAccountCash) {
                showView(SDataConstants.FINX_ACC_CASH_CASH);
            }
            else if (item == jmiCatCashAccountBank) {
                showView(SDataConstants.FINX_ACC_CASH_BANK);
            }
            else if (item == jmiCatCardIssuer) {
                showView(SDataConstants.FINU_CARD_ISS);
            }
            else if (item == jmiCatCheckWallet) {
                showView(SDataConstants.FIN_CHECK_WAL);
            }
            else if (item == jmiCatCheckFormat) {
                showView(SDataConstants.FINU_CHECK_FMT);
            }
            else if (item == jmiCatTaxRegion) {
                showView(SDataConstants.FINU_TAX_REG);
            }
            else if (item == jmiCatTaxIdentity) {
                showView(SDataConstants.FINU_TAX_IDY);
            }
            else if (item == jmiCatTaxBasic) {
                showView(SDataConstants.FINU_TAX_BAS);
            }
            else if (item == jmiCatTaxGroup) {
                showView(SDataConstants.FIN_TAX_GRP);
            }
            else if (item == jmiCatConceptAdministrativeType) {
                showView(SDataConstants.FINU_TP_ADM_CPT);
            }
            else if (item == jmiCatConceptTaxableType) {
                showView(SDataConstants.FINU_TP_TAX_CPT);
            }
            else if (item == jmiCatSysAccTaxGroupForItem) {
                showView(SDataConstants.FIN_TAX_GRP_ITEM);
            }
            else if (item == jmiCatSysAccTaxGroupForItemGeneric) {
                showView(SDataConstants.FIN_TAX_GRP_IGEN);
            }
            else if (item == jmiCatSysAccAccountForBizPartner) {
                showView(SDataConstants.FIN_ACC_BP);
            }
            else if (item == jmiCatSysAccAccountForItem) {
                showView(SDataConstants.FIN_ACC_ITEM);
            }
            else if (item == jmiCatSysAccAccountForTax) {
                showView(SDataConstants.FIN_ACC_TAX);
            }
            else if (item == jmiCatSysAccQryAccountForBizPartner) {
                showView(SDataConstants.FINX_ACC_BP_QRY);
            }
            else if (item == jmiCatSysAccQryAccountForItem) {
                showView(SDataConstants.FINX_ACC_ITEM_QRY);
            }
            else if (item == jmiCatSysAccCostCenterForItem) {
                showView(SDataConstants.FIN_CC_ITEM);
            }
            else if (item == jmiBkkYearOpenClose) {
                showView(SDataConstants.FIN_YEAR);
            }
            else if (item == jmiBkkFiscalYearClosing) {
                actionFinFiscalYearClosing();
            }
            else if (item == jmiBkkFiscalYearClosingDel) {
                actionFinFiscalYearClosingDel();
            }
            else if (item == jmiBkkFiscalYearOpening) {
                actionFinFiscalYearOpening();
            }
            else if (item == jmiBkkFiscalYearOpeningDel) {
                actionFinFiscalYearOpeningDel();
            }
            else if (item == jmiRecRec) {
                showView(SDataConstants.FIN_REC);
            }
            else if (item == jmiRecRecEtyXmlIncome) {
                showView(SDataConstants.FIN_REC_ETY, SDataConstantsSys.TRNS_CT_DPS_SAL);
            }
            else if (item == jmiRecRecEtyXmlExpenses) {
                showView(SDataConstants.FIN_REC_ETY, SDataConstantsSys.TRNS_CT_DPS_PUR);
            }
            else if (item == jmiRecRecCash) {
                showView(SDataConstants.FINX_REC_CASH);
            }
            else if (item == jmiRecBal) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstants.FINX_ACCOUNTING);
            }
            else if (item == jmiRecBalAll) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstants.FINX_ACCOUNTING_ALL);
            }
            else if (item == jmiRecCashAccBalountCash) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH);
            }
            else if (item == jmiRecCashAccBalountBank) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK);
            }
            else if (item == jmiRecBizPartnerBalCus) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_CUS);
            }
            else if (item == jmiRecBizPartnerBalSup) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_SUP);
            }
            else if (item == jmiRecBizPartnerBalDbr) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_DBR);
            }
            else if (item == jmiRecBizPartnerBalCdr) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_CDR);
            }
            else if (item == jmiRecBalInventory) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_INV);
            }
            else if (item == jmiRecBalTaxDebit) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT);
            }
            else if (item == jmiRecBalTaxCredit) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT);
            }
            else if (item == jmiRecBalProfitLoss) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_PROF_LOSS);
            }
            else if (item == jmiRecBookkeepingMoves) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstants.FINX_MOVES_ACC);
            }
            else if (item == jmiRecDpsBizPartnerCus) {
                showView(SDataConstants.FINX_REC_DPS, SDataConstantsSys.BPSS_CT_BP_CUS);
            }
            else if (item == jmiRecDpsBizPartnerSup) {
                showView(SDataConstants.FINX_REC_DPS, SDataConstantsSys.BPSS_CT_BP_SUP);
            }
            else if (item == jmiFinExchangeRate) {
                showView(SDataConstants.FIN_EXC_RATE);
            }
            else if (item == jmiFinCashCheck) {
                showView(SDataConstants.FIN_CHECK);
            }
            else if (item == jmiFinCashCounterReceipt) {
                showView(SDataConstants.TRN_CTR);
            }

            else if (item == jmiFinLayoutBank) {
                miClient.getSession().showView(SModConsts.FIN_LAY_BANK, SModSysConsts.FIN_LAY_BANK_DPS, null);
            }
            else if (item == jmiFinLayoutBankPending) {
                miClient.getSession().showView(SModConsts.FIN_LAY_BANK, SModSysConsts.FIN_LAY_BANK_DPS, new SGuiParams(SModConsts.VIEW_ST_PEND));
            }
            else if (item == jmiFinLayoutBankDone) {
                miClient.getSession().showView(SModConsts.FIN_LAY_BANK, SModSysConsts.FIN_LAY_BANK_DPS, new SGuiParams(SModConsts.VIEW_ST_DONE));
            }
            else if (item == jmiFinLayoutBankAdvances) {
                miClient.getSession().showView(SModConsts.FIN_LAY_BANK, SModSysConsts.FIN_LAY_BANK_ADV, null);
            }
            else if (item == jmiFinLayoutBankPendingAdvances) {
                miClient.getSession().showView(SModConsts.FIN_LAY_BANK, SModSysConsts.FIN_LAY_BANK_ADV, new SGuiParams(SModConsts.VIEW_ST_PEND));
            }
            else if (item == jmiFinLayoutBankDoneAdvances) {
                miClient.getSession().showView(SModConsts.FIN_LAY_BANK, SModSysConsts.FIN_LAY_BANK_ADV, new SGuiParams(SModConsts.VIEW_ST_DONE));
            }
            else if (item == jmiRepTrialBalStandard) {
                new SDialogRepTrialBalanceDual(miClient, SDataConstants.FIN_ACC, false).setVisible(true);
            }
            else if (item == jmiRepTrialBalCostCenter) {
                new SDialogRepTrialBalanceDual(miClient, SDataConstants.FIN_CC, false).setVisible(true);
            }
            else if (item == jmiRepTrialBalCostCenterItem) {
                new SDialogRepTrialBalance(miClient, false).setVisible(true);
            }
            else if (item == jmiRepTrialBalItemType) {
                new SDialogRepTrialBalance(miClient, true).setVisible(true);
            }
            else if (item == jmiRepFinStatBalanceSheet) {
                new SDialogRepBalanceSheet(miClient).setVisible(true);
            }
            else if (item == jmiRepFinStatProfitLossStat) {
                new SDialogRepProfitLossStatement(miClient).setVisible(true);
            }
            else if (item == jmiRepCashAccBalCash) {
                new SDialogRepBizPartnerMove(miClient, SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH).setVisible(true);
            }
            else if (item == jmiRepCashAccBalBank) {
                new SDialogRepBizPartnerMove(miClient, SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK).setVisible(true);
            }
            else if (item == jmiRepCashAccBal) {
                new SDialogRepAccountCashBalance(miClient).setVisible(true);
            }
            else if (item == jmiRepCashAccMovsCash) {
                new SDialogRepFinMov(miClient, new Object[] { SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH, SDataConstants.UNDEFINED }).setVisible(true);
            }
            else if (item == jmiRepCashAccMovsBank) {
                new SDialogRepFinMov(miClient, new Object[] { SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK, SDataConstants.UNDEFINED }).setVisible(true);
            }
            else if (item == jmiRepCashAccMovsCashDay) {
                new SDialogRepFinMovBankDayDet(miClient, new Object[] { SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH, SDataConstants.TRNR_ACCOUNT_CASH_PDAY }).setVisible(true);
            }
            else if (item == jmiRepCashAccMovsBankDay) {
                new SDialogRepFinMovBankDayDet(miClient, new Object[] { SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK, SDataConstants.TRNR_ACCOUNT_BANK_PDAY }).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalCus) {
                new SDialogRepBizPartnerBalance(miClient, SDataConstantsSys.BPSS_CT_BP_CUS).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalSup) {
                new SDialogRepBizPartnerBalance(miClient, SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalDbr) {
                new SDialogRepBizPartnerBalance(miClient, SDataConstantsSys.BPSS_CT_BP_DBR).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalCdr) {
                new SDialogRepBizPartnerBalance(miClient, SDataConstantsSys.BPSS_CT_BP_CDR).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalDpsCus) {
                new SDialogRepBizPartnerBalanceDps(miClient, SDataConstantsSys.BPSS_CT_BP_CUS).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalDpsSup) {
                new SDialogRepBizPartnerBalanceDps(miClient, SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalAgingCus) {
                new SDialogRepBizPartnerBalanceAging(miClient, SDataRepConstants.REP_ACC_AGI + " " + SBpsUtils.getBizPartnerCategoryName(SModSysConsts.BPSS_CT_BP_CUS, SUtilConsts.NUM_PLR).toLowerCase(), SDataConstantsSys.BPSS_CT_BP_CUS).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalAgingSup) {
                new SDialogRepBizPartnerBalanceAging(miClient, SDataRepConstants.REP_ACC_AGI + " " + SBpsUtils.getBizPartnerCategoryName(SModSysConsts.BPSS_CT_BP_SUP, SUtilConsts.NUM_PLR).toLowerCase(), SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepBizPartnerStatCus) {
                new SDialogRepBizPartnerStatement(miClient, SDataRepConstants.REP_STA + " " + SBpsUtils.getBizPartnerCategoryName(SModSysConsts.BPSS_CT_BP_CUS, SUtilConsts.NUM_PLR).toLowerCase(), SDataConstantsSys.BPSS_CT_BP_CUS).setVisible(true);
            }
            else if (item == jmiRepBizPartnerStatSup) {
                new SDialogRepBizPartnerStatement(miClient, SDataRepConstants.REP_STA + " " + SBpsUtils.getBizPartnerCategoryName(SModSysConsts.BPSS_CT_BP_SUP, SUtilConsts.NUM_PLR).toLowerCase(), SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepBizPartnerStatDbr) {
                new SDialogRepBizPartnerStatement(miClient, SDataRepConstants.REP_STA + " " + SBpsUtils.getBizPartnerCategoryName(SModSysConsts.BPSS_CT_BP_DBR, SUtilConsts.NUM_PLR).toLowerCase(), SDataConstantsSys.BPSS_CT_BP_DBR).setVisible(true);
            }
            else if (item == jmiRepBizPartnerStatCdr) {
                new SDialogRepBizPartnerStatement(miClient, SDataRepConstants.REP_STA + " " + SBpsUtils.getBizPartnerCategoryName(SModSysConsts.BPSS_CT_BP_CDR, SUtilConsts.NUM_PLR).toLowerCase(), SDataConstantsSys.BPSS_CT_BP_CDR).setVisible(true);
            }
            else if (item == jmiRepBizPartnerAccMovsCus) {
                new SDialogRepBizPartnerAccountingMoves(miClient, SDataConstantsSys.BPSS_CT_BP_CUS).setVisible(true);
            }
            else if (item == jmiRepBizPartnerAccMovsSup) {
                new SDialogRepBizPartnerAccountingMoves(miClient, SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepBizPartnerLedgerCus) {
                new SDialogRepBizPartnerJournal(miClient, SDataConstantsSys.BPSS_CT_BP_CUS).setVisible(true);
            }
            else if (item == jmiRepBizPartnerLedgerSup) {
                new SDialogRepBizPartnerJournal(miClient, SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepBizPartnerLedgerDbr) {
                new SDialogRepBizPartnerJournal(miClient, SDataConstantsSys.BPSS_CT_BP_DBR).setVisible(true);
            }
            else if (item == jmiRepBizPartnerLedgerCdr) {
                new SDialogRepBizPartnerJournal(miClient, SDataConstantsSys.BPSS_CT_BP_CDR).setVisible(true);
            }
            else if (item == jmiRepAccMovsTax) {
                new SDialogRepTaxesMoves(miClient).setFormVisible(true);
            }
            else if (item == jmiRepAccMovsProfitLoss) {
                new SDialogRepFinMov(miClient, new Object[] { new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_PROF_LOSS, SDataConstantsSys.FINS_TP_ACC_SYS_NA } , SDataConstants.UNDEFINED }).setVisible(true);
            }
            else if (item == jmiRepCashFlowPaysCus) {
                new SDialogRepDpsPayment(miClient, SDataConstantsSys.TRNS_CT_DPS_SAL).setVisible(true);
            }
            else if (item == jmiRepCashFlowPaysSup) {
                new SDialogRepDpsPayment(miClient, SDataConstantsSys.TRNS_CT_DPS_PUR).setVisible(true);
            }
            else if (item == jmiQryCashFlowPaysCusSum) {
                showView(SDataConstants.TRNX_DPS_PAYS, SDataConstantsSys.TRNS_CT_DPS_SAL, SUtilConsts.QRY_SUM);
            }
            else if (item == jmiQryCashFlowPaysCusDet) {
                showView(SDataConstants.TRNX_DPS_PAYS, SDataConstantsSys.TRNS_CT_DPS_SAL, SUtilConsts.QRY_DET);
            }
            else if (item == jmiQryCashFlowPaysSupSum) {
                showView(SDataConstants.TRNX_DPS_PAYS, SDataConstantsSys.TRNS_CT_DPS_PUR, SUtilConsts.QRY_SUM);
            }
            else if (item == jmiQryCashFlowPaysSupDet) {
                showView(SDataConstants.TRNX_DPS_PAYS, SDataConstantsSys.TRNS_CT_DPS_PUR, SUtilConsts.QRY_DET);
            }
            else if (item == jmiRepCashFlowExpected) {
                new SDialogRepCashFlowExpected(miClient.getSession().getClient(), SModConsts.FINR_CSH_FLW_EXP, SDataRepConstants.REP_CSH_FLW_EXP).setVisible(true);
            }
            else if (item == jmiRepLedgerAccount) {
                new SDialogRepAuxAccounting(miClient).setVisible(true);
            }
            else if (item == jmiRepLedgerCostCenter) {
                new SDialogRepTrialBalanceDual(miClient, SDataConstants.FIN_CC, true).setVisible(true);
            }
            else if (item == jmiRepConceptAdmin) {
                new SDialogRepAccountConcept(miClient, SDataConstants.FINU_TP_ADM_CPT).setVisible(true);
            }
            else if (item == jmiRepConceptTax) {
                new SDialogRepAccountConcept(miClient, SDataConstants.FINU_TP_TAX_CPT).setVisible(true);
            }
            else if (item == jmiRepPrtJournalVouchers) {
                new SDialogRepRecords(miClient).setVisible(true);
            }
            else if (item == jmiRepPrtChartOfAccounts) {
                new SDialogRepAccount(miClient).setVisible(true);
            }
            else if (item == jmiRepTaxesByConcept) {
                new SDialogRepTaxesByConcept(miClient).setVisible(true);
            }
            else if (item == jmiRepFiscalTaxPendSal) {
                new SDialogReportTaxPending(miClient.getSession().getClient(), SModSysConsts.TRNS_CT_DPS_SAL, SDataRepConstants.REP_TAX_PND_SAL).setVisible(true);
            }
            else if (item == jmiRepFiscalTaxPendPur) {
                new SDialogReportTaxPending(miClient.getSession().getClient(), SModSysConsts.TRNS_CT_DPS_PUR, SDataRepConstants.REP_TAX_PND_PUR).setVisible(true);
            }
            else if (item == jmiRepFiscalXmlFiles) {
                new SDialogFiscalXmlFile(miClient.getSession().getClient(), SDataRepConstants.REP_XML_FIL).setVisible(true);
            }
            else if (item == jmiRepFiscalMonthRepCfd) {
                new SDialogRepDpsMonthReport(miClient, SDialogRepDpsMonthReport.CFD).setVisible(true);
            }
            else if (item == jmiRepFiscalMonthRepCf) {
                new SDialogRepDpsMonthReport(miClient, SDialogRepDpsMonthReport.CF).setVisible(true);
            }
            else if (item == jmiCfgAbpEntityCash) {
                miClient.getSession().showView(SModConsts.FIN_ABP_ENT, SModSysConsts.CFGS_CT_ENT_CASH, null);
            }
            else if (item == jmiCfgAbpEntityWarehouse) {
                miClient.getSession().showView(SModConsts.FIN_ABP_ENT, SModSysConsts.CFGS_CT_ENT_WH, null);
            }
            else if (item == jmiCfgAbpEntityPlant) {
                miClient.getSession().showView(SModConsts.FIN_ABP_ENT, SModSysConsts.CFGS_CT_ENT_PLANT, null);
            }
            else if (item == jmiCfgAbpBizPartner) {
                miClient.getSession().showView(SModConsts.FIN_ABP_BP, SModSysConsts.BPSS_CT_BP_SUP, null);
            }
            else if (item == jmiCfgAbpItem) {
                miClient.getSession().showView(SModConsts.FIN_ABP_ITEM, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiCfgAbpTax) {
                miClient.getSession().showView(SModConsts.FIN_ABP_TAX, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiCfgAbpLinkCashAccount) {
                miClient.getSession().showView(SModConsts.FIN_ABP_ENT_LINK, SModSysConsts.CFGS_CT_ENT_CASH, null);
            }
            else if (item == jmiCfgAbpLinkWarehouse) {
                miClient.getSession().showView(SModConsts.FIN_ABP_ENT_LINK, SModSysConsts.CFGS_CT_ENT_WH, null);
            }
            else if (item == jmiCfgAbpLinkPlant) {
                miClient.getSession().showView(SModConsts.FIN_ABP_ENT_LINK, SModSysConsts.CFGS_CT_ENT_PLANT, null);
            }
            else if (item == jmiCfgAbpLinkBizPartner) {
                miClient.getSession().showView(SModConsts.FIN_ABP_BP_LINK, SModSysConsts.BPSS_CT_BP_SUP, null);
            }
            else if (item == jmiCfgAbpLinkItem) {
                miClient.getSession().showView(SModConsts.FIN_ABP_ITEM_LINK, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiCfgTaxItemLink) {
                miClient.getSession().showView(SModConsts.FIN_TAX_ITEM_LINK, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiCfgAccItemLink) {
                miClient.getSession().showView(SModConsts.FIN_ACC_ITEM_LINK, SLibConstants.UNDEFINED, null);
            }
        }
    }
}
