/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
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
import erp.mfin.form.SDialogRepAccountCostCenter;
import erp.mfin.form.SDialogRepAccountingAux;
import erp.mfin.form.SDialogRepAuxAccounting;
import erp.mfin.form.SDialogRepBalanceSheet;
import erp.mfin.form.SDialogRepBizPartnerAccountingMoves;
import erp.mfin.form.SDialogRepBizPartnerBalance;
import erp.mfin.form.SDialogRepBizPartnerBalanceDps;
import erp.mfin.form.SDialogRepBizPartnerJournal;
import erp.mfin.form.SDialogRepBizPartnerMove;
import erp.mfin.form.SDialogRepDpsMonthlyReport;
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
import erp.mod.fin.db.SFiscalAccounts;
import erp.mod.fin.form.SDialogFiscalAccountsConfig;
import erp.mod.fin.form.SDialogFiscalXmlFile;
import erp.mod.fin.form.SDialogRepIncomeExpenseDue;
import erp.mod.fin.form.SDialogReportTaxPending;
import erp.mtrn.data.SDataCtr;
import erp.mtrn.data.SDataDsm;
import erp.mtrn.form.SDialogRepBizPartnerBalanceAging;
import erp.mtrn.form.SFormCtr;
import erp.mtrn.form.SFormDsm;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiConsts;

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
    private javax.swing.JMenuItem jmiRecRecEtyXml;
    private javax.swing.JMenuItem jmiRecRecCash;
    private javax.swing.JMenuItem jmiRecBal;
    private javax.swing.JMenuItem jmiRecBalCashAccountCash;
    private javax.swing.JMenuItem jmiRecBalCashAccountBank;
    private javax.swing.JMenuItem jmiRecBalBizPartnerCus;
    private javax.swing.JMenuItem jmiRecBalBizPartnerSup;
    private javax.swing.JMenuItem jmiRecBalBizPartnerDbr;
    private javax.swing.JMenuItem jmiRecBalBizPartnerCdr;
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

    private javax.swing.JMenu jmRep;
    private javax.swing.JMenuItem jmiRepAccount;
    private javax.swing.JMenuItem jmiRepTrialBalance;
    private javax.swing.JMenuItem jmiRepTrialBalanceItem;
    private javax.swing.JMenuItem jmiRepTrialBalanceCostCenterItem;
    private javax.swing.JMenuItem jmiRepTrialBalanceCostCenter;
    private javax.swing.JMenu jmRepSheet;
    private javax.swing.JMenuItem jmiRepBalanceSheet;
    private javax.swing.JMenuItem jmiRepProfitLossStatement;
    private javax.swing.JMenu jmRepFinBpsBalCash;
    private javax.swing.JMenuItem jmiRepFinBpsBalCashCash;
    private javax.swing.JMenuItem jmiRepFinBpsBalCashBank;
    private javax.swing.JMenuItem jmiRepFinAccountCashBal;
    private javax.swing.JMenu jmRepBizPartnerBalance;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceCus;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceSup;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceDbr;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceCdr;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceCusDps;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceSupDps;
    private javax.swing.JMenu jmRepBizPartnerBalanceAging;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceCusAging;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceSupAging;
    private javax.swing.JMenu jmRepBizPartnerAccountingMoves;
    private javax.swing.JMenuItem jmiRepBizPartnerAccountingMovesCus;
    private javax.swing.JMenuItem jmiRepBizPartnerAccountingMovesSup;
    private javax.swing.JMenu jmiRepsBizPartnerAuxMoves;
    private javax.swing.JMenuItem jmiRepsBizPartnerAuxMovesCus;
    private javax.swing.JMenuItem jmiRepsBizPartnerAuxMovesSup;
    private javax.swing.JMenuItem jmiRepsBizPartnerAuxMovesDbr;
    private javax.swing.JMenuItem jmiRepsBizPartnerAuxMovesCdr;
    private javax.swing.JMenu jmRepFinMov;
    private javax.swing.JMenuItem jmiRepFinMovCash;
    private javax.swing.JMenuItem jmiRepFinMovBank;
    private javax.swing.JMenuItem jmiRepFinMovTax;
    private javax.swing.JMenuItem jmiRepFinMovProfLoss;
    private javax.swing.JMenuItem jmiRepFinMovCashDay;
    private javax.swing.JMenuItem jmiRepFinMovBankDay;
    private javax.swing.JMenu jmRepDpsPayment;
    private javax.swing.JMenuItem jmiRepDpsPaymentSup;
    private javax.swing.JMenuItem jmiRepDpsPaymentCus;
    private javax.swing.JMenuItem jmiDpsPaymentSup;
    private javax.swing.JMenuItem jmiDpsPaymentCus;
    private javax.swing.JMenuItem jmiRepIncomeExpenseDue;
    private javax.swing.JMenuItem jmiRepLedgerAccount;
    private javax.swing.JMenuItem jmiRepLedgerCostCenter;
    private javax.swing.JMenuItem jmiRepConceptAdminstrative;
    private javax.swing.JMenuItem jmiRepConceptTaxable;
    private javax.swing.JSeparator jsRepAuxiliar;
    private javax.swing.JMenuItem jmiRepFinanceRecords;
    private javax.swing.JMenuItem jmiRepTaxesByConcept;
    private javax.swing.JMenu jmRepFis;
    private javax.swing.JMenuItem jmiRepFisTaxPendingPur;
    private javax.swing.JMenuItem jmiRepFisTaxPendingSal;
    private javax.swing.JMenuItem jmiRepFisXmlFile;
    private javax.swing.JMenuItem jmiRepFisMonthlyReportCfd;
    private javax.swing.JMenuItem jmiRepFisMonthlyReportCf;

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
    private erp.mtrn.form.SFormDsm moFormDsm;
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

    private erp.mfin.form.SDialogRepRecords moDialogRepRecords;
    private erp.mfin.form.SDialogRepAccountingAux moDialogRepAccountingAux;
    private erp.mfin.form.SDialogRepAccountConcept moDialogRepAccountConcept;
    private erp.mfin.form.SDialogRepAccountCostCenter moDialogRepAccountCostCenter;
    private erp.mfin.form.SDialogRepTaxesByConcept moDialogRepTaxesByConcept;
    private erp.mfin.form.SDialogRepTaxesMoves moDialogRepTaxesMoves;
    private erp.mfin.form.SDialogRepDpsMonthlyReport moDialogRepDpsMonthlyReportCfd;
    private erp.mfin.form.SDialogRepDpsMonthlyReport moDialogRepDpsMonthlyReportCf;

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
        jmiRecRecEtyXml = new JMenuItem("Pólizas contables con XML");
        jmiRecRecCash = new JMenuItem("Pólizas contables de cuentas de dinero");
        jmiRecBal = new JMenuItem("Balanza de comprobación");
        jmiRecBalCashAccountCash = new JMenuItem("Saldos cajas");
        jmiRecBalCashAccountBank = new JMenuItem("Saldos cuentas bancarias");
        jmiRecBalBizPartnerCus = new JMenuItem("Saldos clientes");
        jmiRecBalBizPartnerSup = new JMenuItem("Saldos proveedores");
        jmiRecBalBizPartnerDbr = new JMenuItem("Saldos deudores diversos");
        jmiRecBalBizPartnerCdr = new JMenuItem("Saldos acreedores diversos");
        jmiRecBalInventory = new JMenuItem("Saldos inventarios");
        jmiRecBalTaxDebit = new JMenuItem("Saldos impuestos a favor");
        jmiRecBalTaxCredit = new JMenuItem("Saldos impuestos a cargo");
        jmiRecBalProfitLoss = new JMenuItem("Saldos pérdidas y ganancias");
        jmiRecBookkeepingMoves = new JMenuItem("Movimientos de cuentas contables");
        jmiRecDpsBizPartnerCus = new JMenuItem("Consulta pólizas contables de docs. de clientes");
        jmiRecDpsBizPartnerSup = new JMenuItem("Consulta pólizas contables de docs. de proveedores");

        jmRec.add(jmiRecRec);
        jmRec.add(jmiRecRecEtyXml);
        jmRec.add(jmiRecRecCash);
        jmRec.addSeparator();
        jmRec.add(jmiRecBal);
        jmRec.addSeparator();
        jmRec.add(jmiRecBalCashAccountCash);
        jmRec.add(jmiRecBalCashAccountBank);
        jmRec.addSeparator();
        jmRec.add(jmiRecBalBizPartnerCus);
        jmRec.add(jmiRecBalBizPartnerSup);
        jmRec.add(jmiRecBalBizPartnerDbr);
        jmRec.add(jmiRecBalBizPartnerCdr);
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

        jmFin.add(jmiFinExchangeRate);
        jmFin.addSeparator();
        jmFin.add(jmiFinCashCheck);
        jmFin.add(jmiFinCashCounterReceipt);
        jmFin.add(jsFinCash);   // separator
        jmFin.add(jmiFinLayoutBank);
        jmFin.add(jmiFinLayoutBankPending);
        jmFin.add(jmiFinLayoutBankDone);

        jmRep = new JMenu("Reportes");

        jmiRepAccount = new JMenuItem("Listado de cuentas contables...");
        jmiRepTrialBalance = new JMenuItem("Balanza de comprobación...");
        jmiRepTrialBalanceItem = new JMenuItem("Balanza de comprobación por tipo de ítem...");
        jmiRepTrialBalanceCostCenterItem = new JMenuItem("Balanza de comprobación con centros de costo e ítems...");
        jmiRepTrialBalanceCostCenter = new JMenuItem("Balanza de comprobación de centros de costo...");
        jmRepSheet = new JMenu("Estados financieros");
        jmiRepBalanceSheet = new JMenuItem("Balance general...");
        jmiRepProfitLossStatement = new JMenuItem("Estado de resultados...");
        jmRepFinBpsBalCash = new JMenu("Saldos de cuentas de efectivo");
        jmiRepFinBpsBalCashCash = new JMenuItem("Reporte de saldos de cajas...");
        jmiRepFinBpsBalCashBank = new JMenuItem("Reporte de saldos de cuentas bancarias...");
        jmiRepFinAccountCashBal = new JMenuItem("Reporte de saldos de cuentas de efectivo...");
        jmRepBizPartnerBalance = new JMenu("Saldos de asociados de negocios");
        jmiRepBizPartnerBalanceCus = new JMenuItem("Reporte de saldos de clientes...");
        jmiRepBizPartnerBalanceSup = new JMenuItem("Reporte de saldos de proveedores...");
        jmiRepBizPartnerBalanceDbr = new JMenuItem("Reporte de saldos de deudores diversos...");
        jmiRepBizPartnerBalanceCdr = new JMenuItem("Reporte de saldos de acreedores diversos...");
        jmiRepBizPartnerBalanceCusDps = new JMenuItem("Reporte de saldos de clientes por documento...");
        jmiRepBizPartnerBalanceSupDps = new JMenuItem("Reporte de saldos de proveedores por documento...");
        jmRepBizPartnerBalanceAging = new JMenu("Antigüedad de saldos de asociados de negocios");
        jmiRepBizPartnerBalanceCusAging = new JMenuItem("Reporte de antigüedad de saldos de clientes...");
        jmiRepBizPartnerBalanceSupAging = new JMenuItem("Reporte de antigüedad de saldos de proveedores...");
        jmRepBizPartnerAccountingMoves = new JMenu("Movimientos contables de asociados de negocios");
        jmiRepBizPartnerAccountingMovesCus = new JMenuItem("Movimientos contables de clientes...");
        jmiRepBizPartnerAccountingMovesSup = new JMenuItem("Movimientos contables de proveedores...");
        jmiRepsBizPartnerAuxMoves = new JMenu("Auxiliar de movimientos contables de asociados de negocios");
        jmiRepsBizPartnerAuxMovesCus = new JMenuItem("Reporte auxiliar de movimientos contables de clientes...");
        jmiRepsBizPartnerAuxMovesSup = new JMenuItem("Reporte auxiliar de movimientos contables de proveedores...");
        jmiRepsBizPartnerAuxMovesDbr = new JMenuItem("Reporte auxiliar de movimientos contables de deudores diversos...");
        jmiRepsBizPartnerAuxMovesCdr = new JMenuItem("Reporte auxiliar de movimientos contables de acreedores diversos...");

        jmRepFinMov = new JMenu("Movimientos de cuentas de efectivo e impuestos");
        jmiRepFinMovCash = new JMenuItem("Movimientos de cajas...");
        jmiRepFinMovBank = new JMenuItem("Movimientos de cuentas bancarias...");
        jmiRepFinMovTax = new JMenuItem("Movimientos de impuestos...");
        jmiRepFinMovProfLoss = new JMenuItem("Movimientos de pérdidas y ganancias...");
        jmiRepFinMovProfLoss.setEnabled(true);
        jmiRepFinMovCashDay = new JMenuItem("Movimientos de cajas por día...");
        jmiRepFinMovBankDay = new JMenuItem("Movimientos de cuentas bancarias por día...");

        jmRepDpsPayment = new JMenu("Reportes de flujo de efectivo");
        jmiRepDpsPaymentSup = new JMenuItem("Reporte de pagos por período...");
        jmiRepDpsPaymentCus = new JMenuItem("Reporte de cobros por período...");
        jmiDpsPaymentSup = new JMenuItem("Pagos por período");
        jmiDpsPaymentCus = new JMenuItem("Cobros por período");
        
        jmiRepIncomeExpenseDue = new JMenuItem("Reporte de ingresos y egresos esperados por periodo...");

        jmiRepLedgerAccount = new JMenuItem("Reporte de auxiliares de contabilidad...");
        jmiRepLedgerCostCenter = new JMenuItem("Reporte de auxiliares de centros de costo...");
        jmiRepConceptAdminstrative = new JMenuItem("Reporte de conceptos administrativos...");
        jmiRepConceptTaxable = new JMenuItem("Reporte de conceptos de impuestos...");
        jsRepAuxiliar = new JPopupMenu.Separator();
        jmiRepFinanceRecords = new JMenuItem("Impresión de pólizas contables...");
        jmiRepTaxesByConcept = new JMenuItem("Reporte de impuestos por concepto...");

        jmRepFis = new JMenu("Reportes fiscales");
        jmiRepFisTaxPendingPur = new JMenuItem("Impuestos pendientes de egresos...");
        jmiRepFisTaxPendingSal = new JMenuItem("Impuestos pendientes de ingresos...");
        jmiRepFisXmlFile = new JMenuItem("Archivos contabilidad electrónica...");
        jmiRepFisMonthlyReportCfd = new JMenuItem("Informe mensual de comprobantes digitales...");
        jmiRepFisMonthlyReportCf = new JMenuItem("Informe mensual de comprobantes impresos...");

        jmRep.add(jmiRepAccount);
        jmRep.add(jmiRepTrialBalance);
        jmRep.add(jmiRepTrialBalanceItem);
        jmRep.add(jmiRepTrialBalanceCostCenterItem);
        jmRep.add(jmiRepTrialBalanceCostCenter);

        jmRep.addSeparator();

        jmRepSheet.add(jmiRepBalanceSheet);
        jmRepSheet.add(jmiRepProfitLossStatement);
        jmRep.add(jmRepSheet);

        jmRep.addSeparator();

        jmRepFinBpsBalCash.add(jmiRepFinBpsBalCashCash);
        jmRepFinBpsBalCash.add(jmiRepFinBpsBalCashBank);
        jmRepFinBpsBalCash.addSeparator();
        jmRepFinBpsBalCash.add(jmiRepFinAccountCashBal);
        jmRep.add(jmRepFinBpsBalCash);

        jmRepBizPartnerBalance.add(jmiRepBizPartnerBalanceCus);
        jmRepBizPartnerBalance.add(jmiRepBizPartnerBalanceSup);
        jmRepBizPartnerBalance.add(jmiRepBizPartnerBalanceDbr);
        jmRepBizPartnerBalance.add(jmiRepBizPartnerBalanceCdr);
        jmRepBizPartnerBalance.addSeparator();
        jmRepBizPartnerBalance.add(jmiRepBizPartnerBalanceCusDps);
        jmRepBizPartnerBalance.add(jmiRepBizPartnerBalanceSupDps);
        jmRep.add(jmRepBizPartnerBalance);

        jmRepBizPartnerBalanceAging.add(jmiRepBizPartnerBalanceCusAging);
        jmRepBizPartnerBalanceAging.add(jmiRepBizPartnerBalanceSupAging);
        jmRep.add(jmRepBizPartnerBalanceAging);

        jmRepBizPartnerAccountingMoves.add(jmiRepBizPartnerAccountingMovesCus);
        jmRepBizPartnerAccountingMoves.add(jmiRepBizPartnerAccountingMovesSup);
        jmRep.add(jmRepBizPartnerAccountingMoves);

        jmiRepsBizPartnerAuxMoves.add(jmiRepsBizPartnerAuxMovesCus);
        jmiRepsBizPartnerAuxMoves.add(jmiRepsBizPartnerAuxMovesSup);
        jmiRepsBizPartnerAuxMoves.add(jmiRepsBizPartnerAuxMovesDbr);
        jmiRepsBizPartnerAuxMoves.add(jmiRepsBizPartnerAuxMovesCdr);
        jmRep.add(jmiRepsBizPartnerAuxMoves);

        jmRepFinMov.add(jmiRepFinMovCash);
        jmRepFinMov.add(jmiRepFinMovBank);
        jmRepFinMov.add(jmiRepFinMovTax);
        jmRepFinMov.add(jmiRepFinMovProfLoss);
        jmRepFinMov.addSeparator();
        jmRepFinMov.add(jmiRepFinMovCashDay);
        jmRepFinMov.add(jmiRepFinMovBankDay);
        jmRep.add(jmRepFinMov);


        jmRepDpsPayment.add(jmiRepDpsPaymentSup);
        jmRepDpsPayment.add(jmiRepDpsPaymentCus);
        jmRepDpsPayment.addSeparator();
        jmRepDpsPayment.add(jmiDpsPaymentSup);
        jmRepDpsPayment.add(jmiDpsPaymentCus);
        jmRep.add(jmRepDpsPayment);
        
        jmRep.add(jmiRepIncomeExpenseDue);

        jmRep.addSeparator();
        jmRep.add(jmiRepLedgerAccount);
        jmRep.add(jmiRepLedgerCostCenter);
        jmRep.add(jmiRepConceptAdminstrative);
        jmRep.add(jmiRepConceptTaxable);
        jmRep.add(jsRepAuxiliar);   // separator
        jmRep.add(jmiRepFinanceRecords);
        jmRep.addSeparator();
        jmRep.add(jmiRepTaxesByConcept);
        jmRep.addSeparator();

        jmRepFis.add(jmiRepFisTaxPendingPur);
        jmRepFis.add(jmiRepFisTaxPendingSal);
        jmRepFis.addSeparator();
        jmRepFis.add(jmiRepFisXmlFile);
        jmRepFis.addSeparator();
        jmRepFis.add(jmiRepFisMonthlyReportCfd);
        jmRepFis.add(jmiRepFisMonthlyReportCf);

        jmRep.add(jmRepFis);

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
        jmiRecRecEtyXml.addActionListener(this);
        jmiRecRecCash.addActionListener(this);
        jmiRecBal.addActionListener(this);
        jmiRecBalCashAccountCash.addActionListener(this);
        jmiRecBalCashAccountBank.addActionListener(this);
        jmiRecBalBizPartnerCus.addActionListener(this);
        jmiRecBalBizPartnerSup.addActionListener(this);
        jmiRecBalBizPartnerDbr.addActionListener(this);
        jmiRecBalBizPartnerCdr.addActionListener(this);
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

        jmiRepAccount.addActionListener(this);
        jmiRepTrialBalance.addActionListener(this);
        jmiRepTrialBalanceItem.addActionListener(this);
        jmiRepTrialBalanceCostCenterItem.addActionListener(this);
        jmiRepTrialBalanceCostCenter.addActionListener(this);
        jmiRepBalanceSheet.addActionListener(this);
        jmiRepProfitLossStatement.addActionListener(this);
        jmiRepFinBpsBalCashCash.addActionListener(this);
        jmiRepFinBpsBalCashBank.addActionListener(this);
        jmiRepFinAccountCashBal.addActionListener(this);
        jmiRepBizPartnerBalanceCus.addActionListener(this);
        jmiRepBizPartnerBalanceSup.addActionListener(this);
        jmiRepBizPartnerBalanceDbr.addActionListener(this);
        jmiRepBizPartnerBalanceCdr.addActionListener(this);
        jmiRepBizPartnerBalanceCusDps.addActionListener(this);
        jmiRepBizPartnerBalanceSupDps.addActionListener(this);
        jmiRepBizPartnerBalanceCusAging.addActionListener(this);
        jmiRepBizPartnerBalanceSupAging.addActionListener(this);
        jmiRepBizPartnerAccountingMovesCus.addActionListener(this);
        jmiRepBizPartnerAccountingMovesSup.addActionListener(this);
        jmiRepsBizPartnerAuxMovesCus.addActionListener(this);
        jmiRepsBizPartnerAuxMovesSup.addActionListener(this);
        jmiRepsBizPartnerAuxMovesDbr.addActionListener(this);
        jmiRepsBizPartnerAuxMovesCdr.addActionListener(this);
        jmiRepFinMovCash.addActionListener(this);
        jmiRepFinMovBank.addActionListener(this);
        jmiRepFinMovTax.addActionListener(this);
        jmiRepFinMovProfLoss.addActionListener(this);
        jmiRepFinMovCashDay.addActionListener(this);
        jmiRepFinMovBankDay.addActionListener(this);
        jmiRepDpsPaymentSup.addActionListener(this);
        jmiRepDpsPaymentCus.addActionListener(this);
        jmiDpsPaymentSup.addActionListener(this);
        jmiDpsPaymentCus.addActionListener(this);
        jmiRepIncomeExpenseDue.addActionListener(this);
        jmiRepLedgerAccount.addActionListener(this);
        jmiRepLedgerCostCenter.addActionListener(this);
        jmiRepConceptAdminstrative.addActionListener(this);
        jmiRepConceptTaxable.addActionListener(this);
        jmiRepFinanceRecords.addActionListener(this);
        jmiRepTaxesByConcept.addActionListener(this);
        jmiRepFisTaxPendingPur.addActionListener(this);
        jmiRepFisTaxPendingSal.addActionListener(this);
        jmiRepFisXmlFile.addActionListener(this);
        jmiRepFisMonthlyReportCfd.addActionListener(this);
        jmiRepFisMonthlyReportCf.addActionListener(this);

        moDialogRepRecords = new SDialogRepRecords(miClient);
        moDialogRepAccountingAux = new SDialogRepAccountingAux(miClient);
        moDialogRepAccountConcept = new SDialogRepAccountConcept(miClient);
        moDialogRepAccountCostCenter = new SDialogRepAccountCostCenter(miClient);
        moDialogRepTaxesByConcept = new SDialogRepTaxesByConcept(miClient);
        moDialogRepTaxesMoves = new SDialogRepTaxesMoves(miClient);
        moDialogRepDpsMonthlyReportCfd = new SDialogRepDpsMonthlyReport(miClient, SDialogRepDpsMonthlyReport.CFD);
        moDialogRepDpsMonthlyReportCf = new SDialogRepDpsMonthlyReport(miClient, SDialogRepDpsMonthlyReport.CF);

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

        jmRec.setEnabled(hasBkrRight || hasRepRight || hasCatAccCashRight || hasMoveAccCash || hasMoveBpCdr || hasMoveBpDbr);
        jmiRecRec.setEnabled(hasBkrRight);
        jmiRecRecEtyXml.setEnabled(hasBkrRight);
        jmiRecRecCash.setEnabled(hasBkrRight);
        jmiRecRecCash.setEnabled(false);    // XXX temporal code!!! (sflores, 2013-07-27)
        jmiRecBal.setEnabled(hasBkrRight || hasRepRight);
        jmiRecBalCashAccountCash.setEnabled(hasBkrRight || hasCatAccCashRight || hasMoveBpCdr || hasMoveBpDbr || hasRepRight);
        jmiRecBalCashAccountBank.setEnabled(hasBkrRight || hasCatAccCashRight || hasMoveBpCdr || hasMoveBpDbr || hasRepRight);
        jmiRecBalBizPartnerCus.setEnabled(hasBkrRight || hasRepRight);
        jmiRecBalBizPartnerSup.setEnabled(hasBkrRight || hasRepRight);
        jmiRecBalBizPartnerDbr.setEnabled(hasBkrRight || hasMoveBpDbr || hasRepRight);
        jmiRecBalBizPartnerCdr.setEnabled(hasBkrRight || hasMoveBpCdr || hasRepRight);
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

        jmRep.setEnabled(hasRepRight || hasRepFinRateRight || hasRepStatementRight);
        jmRepSheet.setEnabled(hasRepStatementRight);
        
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
            section.getChildItems().add(new SCfgMenuSectionItem(jmiRepConceptAdminstrative, "" + SDataConstants.FINU_TP_ADM_CPT));
            section.getChildItems().add(new SCfgMenuSectionItem(jmiRepConceptTaxable, "" + SDataConstants.FINU_TP_TAX_CPT));
            section.setChildSeparator(new SCfgMenuSectionSeparator(jsRepAuxiliar));
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
                case SDataConstants.TRN_DSM:
                    if (moFormDsm == null) {
                        moFormDsm = new SFormDsm(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataDsm();
                    }
                    miForm = moFormDsm;
                    miForm.setValue(1,  auxType);
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
                    oViewClass = erp.mfin.view.SViewRecordEntriesXml.class;
                    sViewTitle = "Pólizas contab. con XML";
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
                case SDataConstants.TRN_DSM:
                    oViewClass = erp.mtrn.view.SViewDsm.class;
                    sViewTitle = "Movimientos de asociados de negocios (ctes)";
                    break;
                case SDataConstants.TRNX_DPS_PAYS:
                    if (auxType01 == SDataConstantsSys.TRNS_CT_DPS_PUR) {
                        sViewTitle = "Pagos período";
                    }
                    else if (auxType01 == SDataConstantsSys.TRNS_CT_DPS_SAL) {
                        sViewTitle = "Cobros período";
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
            else if (item == jmiRecRecEtyXml) {
                showView(SDataConstants.FIN_REC_ETY);
            }
            else if (item == jmiRecRecCash) {
                showView(SDataConstants.FINX_REC_CASH);
            }
            else if (item == jmiRecBal) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstants.FINX_ACCOUNTING);
            }
            else if (item == jmiRecBalCashAccountCash) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH);
            }
            else if (item == jmiRecBalCashAccountBank) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK);
            }
            else if (item == jmiRecBalBizPartnerCus) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_CUS);
            }
            else if (item == jmiRecBalBizPartnerSup) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_SUP);
            }
            else if (item == jmiRecBalBizPartnerDbr) {
                showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_DBR);
            }
            else if (item == jmiRecBalBizPartnerCdr) {
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
                miClient.getSession().showView(SModConsts.FIN_LAY_BANK, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiFinLayoutBankPending) {
                miClient.getSession().showView(SModConsts.FIN_LAY_BANK, SModConsts.VIEW_ST_PEND, null);
            }
            else if (item == jmiFinLayoutBankDone) {
                miClient.getSession().showView(SModConsts.FIN_LAY_BANK, SModConsts.VIEW_ST_DONE, null);
            }
            else if (item == jmiRepAccount) {
                new SDialogRepAccount(miClient).setVisible(true);
            }
            else if (item == jmiRepTrialBalance) {
                new SDialogRepTrialBalanceDual(miClient, SDataConstants.FIN_ACC, false).setVisible(true);
            }
            else if (item == jmiRepTrialBalanceItem) {
                new SDialogRepTrialBalance(miClient, true).setVisible(true);
            }
            else if (item == jmiRepTrialBalanceCostCenterItem) {
                new SDialogRepTrialBalance(miClient, false).setVisible(true);
            }
            else if (item == jmiRepTrialBalanceCostCenter) {
                new SDialogRepTrialBalanceDual(miClient, SDataConstants.FIN_CC, false).setVisible(true);
            }
            else if (item == jmiRepBalanceSheet) {
                new SDialogRepBalanceSheet(miClient).setVisible(true);
            }
            else if (item == jmiRepProfitLossStatement) {
                new SDialogRepProfitLossStatement(miClient).setVisible(true);
            }
            else if (item == jmiRepFinBpsBalCashCash) {
                new SDialogRepBizPartnerMove(miClient, SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH).setVisible(true);
            }
            else if (item == jmiRepFinBpsBalCashBank) {
                new SDialogRepBizPartnerMove(miClient, SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK).setVisible(true);
            }
            else if (item == jmiRepFinAccountCashBal) {
                new SDialogRepAccountCashBalance(miClient).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalanceCus) {
                new SDialogRepBizPartnerBalance(miClient, SDataConstantsSys.BPSS_CT_BP_CUS).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalanceSup) {
                new SDialogRepBizPartnerBalance(miClient, SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalanceDbr) {
                new SDialogRepBizPartnerBalance(miClient, SDataConstantsSys.BPSS_CT_BP_DBR).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalanceCdr) {
                new SDialogRepBizPartnerBalance(miClient, SDataConstantsSys.BPSS_CT_BP_CDR).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalanceCusDps) {
                new SDialogRepBizPartnerBalanceDps(miClient, SDataConstantsSys.BPSS_CT_BP_CUS).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalanceSupDps) {
                new SDialogRepBizPartnerBalanceDps(miClient, SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalanceCusAging) {
                new SDialogRepBizPartnerBalanceAging(miClient, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalanceSupAging) {
                new SDialogRepBizPartnerBalanceAging(miClient, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP).setVisible(true);
            }
            else if (item == jmiRepBizPartnerAccountingMovesCus) {
                new SDialogRepBizPartnerAccountingMoves(miClient, SDataConstantsSys.BPSS_CT_BP_CUS).setVisible(true);
            }
            else if (item == jmiRepBizPartnerAccountingMovesSup) {
                new SDialogRepBizPartnerAccountingMoves(miClient, SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepsBizPartnerAuxMovesCus) {
                new SDialogRepBizPartnerJournal(miClient, SDataConstantsSys.BPSS_CT_BP_CUS).setVisible(true);
            }
            else if (item == jmiRepsBizPartnerAuxMovesSup) {
                new SDialogRepBizPartnerJournal(miClient, SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepsBizPartnerAuxMovesDbr) {
                new SDialogRepBizPartnerJournal(miClient, SDataConstantsSys.BPSS_CT_BP_DBR).setVisible(true);
            }
            else if (item == jmiRepsBizPartnerAuxMovesCdr) {
                new SDialogRepBizPartnerJournal(miClient, SDataConstantsSys.BPSS_CT_BP_CDR).setVisible(true);
            }
            else if (item == jmiRepFinMovCash) {
                new SDialogRepFinMov(miClient, new Object[] { SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH, SDataConstants.UNDEFINED }).setVisible(true);
            }
            else if (item == jmiRepFinMovBank) {
                new SDialogRepFinMov(miClient, new Object[] { SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK, SDataConstants.UNDEFINED }).setVisible(true);
            }
            else if (item == jmiRepFinMovTax) {
                moDialogRepTaxesMoves.formRefreshCatalogues();
                moDialogRepTaxesMoves.formReset();
                moDialogRepTaxesMoves.setFormVisible(true);
            }
            else if (item == jmiRepFinMovProfLoss) {
                new SDialogRepFinMov(miClient, new Object[] { new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_PROF_LOSS, SDataConstantsSys.FINS_TP_ACC_SYS_NA } , SDataConstants.UNDEFINED }).setVisible(true);
            }
            else if (item == jmiRepFinMovCashDay) {
                new SDialogRepFinMovBankDayDet(miClient, new Object[] { SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH, SDataConstants.TRNR_ACCOUNT_CASH_PDAY }).setVisible(true);
            }
            else if (item == jmiRepFinMovBankDay) {
                new SDialogRepFinMovBankDayDet(miClient, new Object[] { SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK, SDataConstants.TRNR_ACCOUNT_BANK_PDAY }).setVisible(true);
            }
            else if (item == jmiRepDpsPaymentSup) {
                new SDialogRepDpsPayment(miClient, SDataConstantsSys.TRNS_CT_DPS_PUR).setVisible(true);
            }
            else if (item == jmiRepDpsPaymentCus) {
                new SDialogRepDpsPayment(miClient, SDataConstantsSys.TRNS_CT_DPS_SAL).setVisible(true);
            }
            else if (item == jmiDpsPaymentSup) {
                showView(SDataConstants.TRNX_DPS_PAYS, SDataConstantsSys.TRNS_CT_DPS_PUR);
            }
            else if (item == jmiDpsPaymentCus) {
                showView(SDataConstants.TRNX_DPS_PAYS, SDataConstantsSys.TRNS_CT_DPS_SAL);
            }
            else if (item == jmiRepIncomeExpenseDue){
                new SDialogRepIncomeExpenseDue(miClient.getSession().getClient(), SModConsts.FINR_INC_EXP_DUE, "Reporte de ingresos y egresos esperados por periodo").setVisible(true);
            } 
            else if (item == jmiRepFinanceRecords) {
                moDialogRepRecords.formRefreshCatalogues();
                moDialogRepRecords.formReset();
                moDialogRepRecords.setFormVisible(true);
            }
            else if (item == jmiRepLedgerAccount) {
                /*
                moDialogRepAccountingAux.formRefreshCatalogues();
                moDialogRepAccountingAux.formReset();
                moDialogRepAccountingAux.setFormVisible(true);
                */
                new SDialogRepAuxAccounting(miClient).setVisible(true);
            }
            else if (item == jmiRepLedgerCostCenter) {
                /*
                moDialogRepAccountCostCenter.formRefreshCatalogues();
                moDialogRepAccountCostCenter.formReset();
                moDialogRepAccountCostCenter.setFormVisible(true);
                */
                new SDialogRepTrialBalanceDual(miClient, SDataConstants.FIN_CC, true).setVisible(true);
            }
            else if (item == jmiRepConceptAdminstrative) {
                moDialogRepAccountConcept.setParamIsConceptAdministrative(true);
                moDialogRepAccountConcept.formRefreshCatalogues();
                moDialogRepAccountConcept.formReset();
                moDialogRepAccountConcept.setFormVisible(true);
            }
            else if (item == jmiRepConceptTaxable) {
                moDialogRepAccountConcept.setParamIsConceptAdministrative(false);
                moDialogRepAccountConcept.formRefreshCatalogues();
                moDialogRepAccountConcept.formReset();
                moDialogRepAccountConcept.setFormVisible(true);
            }
            else if (item == jmiRepTaxesByConcept) {
                moDialogRepTaxesByConcept.formRefreshCatalogues();
                moDialogRepTaxesByConcept.formReset();
                moDialogRepTaxesByConcept.setFormVisible(true);
            }
            else if (item == jmiRepFisTaxPendingPur) {
                SDialogReportTaxPending dialog = new SDialogReportTaxPending(miClient.getSession().getClient(), SModSysConsts.TRNS_CT_DPS_PUR, "Impuestos pendientes de egresos");
                dialog.setVisible(true);
            }
            else if (item == jmiRepFisTaxPendingSal) {
                SDialogReportTaxPending dialog = new SDialogReportTaxPending(miClient.getSession().getClient(), SModSysConsts.TRNS_CT_DPS_SAL, "Impuestos pendientes de ingresos");
                dialog.setVisible(true);
            }
            else if (item == jmiRepFisXmlFile) {
                SDialogFiscalXmlFile dialog = new SDialogFiscalXmlFile(miClient.getSession().getClient(), "Archivos contabilidad electrónica");
                dialog.initForm();
                dialog.setVisible(true);
            }
            else if (item == jmiRepFisMonthlyReportCfd) {
                moDialogRepDpsMonthlyReportCfd.resetForm();
                moDialogRepDpsMonthlyReportCfd.setVisible(true);
            }
            else if (item == jmiRepFisMonthlyReportCf) {
                moDialogRepDpsMonthlyReportCf.resetForm();
                moDialogRepDpsMonthlyReportCf.setVisible(true);
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
