/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.form;

import cfd.util.DUtilUtils;
import erp.cfd.SCatalogXmlUtils;
import erp.cfd.SCfdConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.form.SFormOptionPickerBizPartner;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormOptionPickerInterface;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePaneGrid;
import erp.lib.table.STableRow;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mbps.data.SDataBizPartnerBranchContact;
import erp.mcfg.data.SDataParamsCompany;
import erp.mcfg.data.SDataParamsErp;
import erp.mfin.data.SDataRecord;
import erp.mfin.form.SDialogRecordPicker;
import erp.mfin.form.SPanelRecord;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mmkt.data.SDataCustomerBranchConfig;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.STrnUtils;
import erp.mtrn.data.SCfdParams;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsDpsAdjustment;
import erp.mtrn.data.SDataDpsDpsLink;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SDataDpsEntryCommissions;
import erp.mtrn.data.SDataDpsEntryNotes;
import erp.mtrn.data.SDataDpsEntryPrice;
import erp.mtrn.data.SDataDpsEntryRow;
import erp.mtrn.data.SDataDpsEntryTax;
import erp.mtrn.data.SDataDpsNotes;
import erp.mtrn.data.SDataDpsNotesRow;
import erp.mtrn.data.SDataDpsType;
import erp.mtrn.data.SDataEntryDpsDpsAdjustment;
import erp.mtrn.data.SDataEntryDpsDpsLink;
import erp.mtrn.data.SGuiDpsEntryPrice;
import erp.mtrn.data.SGuiDpsLink;
import erp.mtrn.data.STrnDpsUtilities;
import erp.mtrn.data.STrnUtilities;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibMethod;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.srv.SSrvConsts;
import sa.lib.srv.SSrvLock;
import sa.lib.srv.SSrvUtils;

/**
 *
 * @author  Sergio Flores, Edwin Carmona, Uriel Castañeda, juan Barajas
 */
public class SFormDps extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.FocusListener, java.awt.event.ItemListener, erp.lib.form.SFormExtendedInterface {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private boolean mbUpdatingForm;
    private boolean mbImportation;
    private java.util.Vector<SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mtrn.data.SDataDps moDps;
    private erp.mtrn.data.SDataDps moLastDpsSource;
    private erp.mbps.data.SDataBizPartner moBizPartner;
    private erp.mbps.data.SDataBizPartnerBranch moBizPartnerBranch;
    private erp.mbps.data.SDataBizPartnerBranchAddress moBizPartnerBranchAddressMain;
    private erp.mbps.data.SDataBizPartnerBranchAddress moBizPartnerBranchAddress;
    private erp.mbps.data.SDataBizPartnerCategory moBizPartnerCategory;
    private erp.lib.form.SFormField moFieldDate;
    private erp.lib.form.SFormField moFieldDateDoc;
    private erp.lib.form.SFormField moFieldDateStartCredit;
    private erp.lib.form.SFormField moFieldDateShipment_n;
    private erp.lib.form.SFormField moFieldDateDelivery_n;
    private erp.lib.form.SFormField moFieldDateDocLapsing_n;
    private erp.lib.form.SFormField moFieldDateDocDelivery_n;
    private erp.lib.form.SFormField moFieldNumberSeries;
    private erp.lib.form.SFormField moFieldNumber;
    private erp.lib.form.SFormField moFieldNumberReference;
    private erp.lib.form.SFormField moFieldDaysOfCredit;
    private erp.lib.form.SFormField moFieldIsDiscountDocApplying;
    private erp.lib.form.SFormField moFieldIsDiscountDocPercentage;
    private erp.lib.form.SFormField moFieldDiscountDocPercentage;
    private erp.lib.form.SFormField moFieldExchangeRate;
    private erp.lib.form.SFormField moFieldExchangeRateSystem;
    private erp.lib.form.SFormField moFieldIsCopy;
    private erp.lib.form.SFormField moFieldDriver;
    private erp.lib.form.SFormField moFieldPlate;
    private erp.lib.form.SFormField moFieldTicket;
    private erp.lib.form.SFormField moFieldShipments;
    private erp.lib.form.SFormField moFieldPayments;
    private erp.lib.form.SFormField moFieldIsLinked;
    private erp.lib.form.SFormField moFieldIsClosed;
    private erp.lib.form.SFormField moFieldIsAudited;
    private erp.lib.form.SFormField moFieldIsAuthorized;
    private erp.lib.form.SFormField moFieldIsSystem;
    private erp.lib.form.SFormField moFieldIsDeleted;
    private erp.lib.form.SFormField moFieldFkPaymentTypeId;
    private erp.lib.form.SFormField moFieldFkPaymentSystemTypeId;
    private erp.lib.form.SFormField moFieldPaymentAccount;
    private erp.lib.form.SFormField moFieldFkLanguajeId;
    private erp.lib.form.SFormField moFieldFkFunctionalAreaId;
    private erp.lib.form.SFormField moFieldFkDpsNatureId;
    private erp.lib.form.SFormField moFieldFkCurrencyId;
    private erp.lib.form.SFormField moFieldFkProductionOrderId_n;
    private erp.lib.form.SFormField moFieldFkContactId_n;
    private erp.lib.form.SFormField moFieldFkIncotermId;
    private erp.lib.form.SFormField moFieldFkSpotSrcId_n;
    private erp.lib.form.SFormField moFieldFkSpotDesId_n;
    private erp.lib.form.SFormField moFieldFkModeOfTransportationTypeId;
    private erp.lib.form.SFormField moFieldFkCarrierTypeId;
    private erp.lib.form.SFormField moFieldFkCarrierId_n;
    private erp.lib.form.SFormField moFieldFkVehicleTypeId_n;
    private erp.lib.form.SFormField moFieldFkVehicleId_n;

    private erp.lib.form.SFormField moFieldFolioNotaRecepcion;
    private erp.lib.form.SFormField moFieldBachocoSociedad;
    private erp.lib.form.SFormField moFieldBachocoOrganizacionCompra;
    private erp.lib.form.SFormField moFieldBachocoDivision;
    private erp.lib.form.SFormField moFieldDpsDescripcion;
    private erp.lib.form.SFormField moFieldStore;
    private erp.lib.form.SFormField moFieldGoodsDelivery;
    private erp.lib.form.SFormField moFieldDateRemission;
    private erp.lib.form.SFormField moFieldRemission;
    private erp.lib.form.SFormField moFieldSalesOrder;
    private erp.lib.form.SFormField moFieldCfdAddendaSubtypeId;
    private erp.lib.form.SFormField moFieldNumberNoteIn;
    private erp.lib.form.SFormField moFieldBulkType;
    private erp.lib.form.SFormField moFieldBulkQty;
    private erp.lib.form.SFormField moFieldFileXml;
    private erp.lib.form.SFormField moFieldFkCfdUseId;
    private erp.lib.form.SFormField moFieldCfdConfirmationNumber;

    private int[] manDpsClassKey;
    private int[] manDpsClassPreviousKey;
    private int[] manParamAdjustmentSubtypeKey;
    private boolean mbIsPeriodOpen;

    private int mnParamCurrentUserPrivilegeLevel;
    private boolean mbParamIsReadOnly;
    private erp.mtrn.data.SDataDps moParamDpsSource;

    private erp.mtrn.data.SDataDpsType moDpsType;

    private boolean mbIsCon;
    private boolean mbIsEst;
    private boolean mbIsOrd;
    private boolean mbIsDoc;
    private boolean mbIsAdj;
    private boolean mbIsSales;
    private boolean mbFormSettingsOk;
    private boolean mbIsNumberSeriesRequired;
    private boolean mbIsNumberSeriesAvailable;
    private boolean mbIsNumberEditable;
    private boolean mbValidateLinkPeriod;
    private boolean mbHasDpsLinksAsSrc;
    private boolean mbHasDpsLinksAsDes;
    private boolean mbHasDpsAdjustmentsAsDoc;
    private boolean mbHasDpsAdjustmentsAsAdj;
    private boolean mbHasIogSupplies;
    private boolean mbHasCommissions;
    private boolean mbHasShipments;
    private int mnSalesAgentId_n;
    private int mnSalesAgentBizPartnerId_n;
    private int mnSalesSupervisorId_n;
    private int mnSalesSupervisorBizPartnerId_n;
    private int mnPrepaymentsItemId;
    private double mdPrepayments;
    private double mdPrepaymentsCy;
    private java.lang.Object moRecordUserKey;
    private sa.lib.srv.SSrvLock moRecordUserLock;
    private erp.mfin.data.SDataRecord moRecordUser;
    private erp.form.SFormOptionPickerBizPartner moPickerBizPartner;
    private erp.lib.table.STablePaneGrid moPaneGridEntries;
    private erp.lib.table.STablePaneGrid moPaneGridNotes;
    private erp.mtrn.form.SFormDpsEntry moFormEntry;
    private erp.mtrn.form.SFormDpsEntryWizard moFormEntryWizard;
    private erp.mtrn.form.SFormDpsNotes moFormNotes;
    private erp.mtrn.form.SDialogPickerDps moDialogPickerDps;
    private erp.mtrn.form.SDialogPickerDps moDialogPickerDpsForLink;
    private erp.mtrn.form.SDialogPickerDps moDialogPickerDpsForAdjustment;
    private erp.mtrn.form.SDialogDpsLink moDialogDpsLink;
    private erp.mtrn.form.SDialogDpsAdjustment moDialogDpsAdjustment;
    private erp.mfin.form.SDialogRecordPicker moDialogRecordPicker;
    private erp.mtrn.form.SDialogShowDocumentLinks moDialogShowDocumentLinks;
    private erp.mfin.form.SPanelRecord moPanelRecord;
    private boolean mbIsAddendaNeeded;
    private int mnCountEntries;
    private String msFileXmlPath;

    private double mdOrigExchangeRate;
    private double mdOrigDiscountDocPercentage;
    private boolean mbOrigIsDiscountDocApplying;
    private boolean mbIsLocalCurrency;
    private SGuiDpsLink moGuiDpsLink;

    private int mnTypeDelivery;
    private boolean mbHasRightOrderDelay;
    private boolean mbHasRightOmitSourceDoc;

    /**
     * Creates new form DFormDps
     * @param client Client interface.
     * @param formType Form type can be either erp.data.SDataConstantsSys.TRNS_CT_DPS_PUR or erp.data.SDataConstantsSys.TRNS_CT_DPS_SAL.
     */
    public SFormDps(erp.client.SClientInterface client, java.lang.Integer formType) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = formType;

        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpDocument = new javax.swing.JPanel();
        jpHeader = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel57 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlDpsType = new javax.swing.JLabel();
        jtfDpsTypeRo = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jlCompanyBranch = new javax.swing.JLabel();
        jtfCompanyBranchRo = new javax.swing.JTextField();
        jtfCompanyBranchCodeRo = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        jtfBizPartnerRo = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jlBizPartnerBranch = new javax.swing.JLabel();
        jtfBizPartnerBranchRo = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jlBizPartnerBranchAddressMain01 = new javax.swing.JLabel();
        jtfBizPartnerBranchAddressMain01Ro = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jlBizPartnerBranchAddressMain02 = new javax.swing.JLabel();
        jtfBizPartnerBranchAddressMain02Ro = new javax.swing.JTextField();
        jPanel65 = new javax.swing.JPanel();
        jlBizPartnerBranchAddress01 = new javax.swing.JLabel();
        jtfBizPartnerBranchAddress01Ro = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jlBizPartnerBranchAddress02 = new javax.swing.JLabel();
        jtfBizPartnerBranchAddress02Ro = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        jftDate = new javax.swing.JFormattedTextField();
        jbDate = new javax.swing.JButton();
        jckIsRebill = new javax.swing.JCheckBox();
        jbBizPartnerBalance = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jlNumber = new javax.swing.JLabel();
        jcbNumberSeries = new javax.swing.JComboBox<SFormComponentItem>();
        jtfNumber = new javax.swing.JTextField();
        jtfNumberReference = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jckDateDoc = new javax.swing.JCheckBox();
        jftDateDoc = new javax.swing.JFormattedTextField();
        jbDateDoc = new javax.swing.JButton();
        jlDaysOfCredit = new javax.swing.JLabel();
        jtfDaysOfCredit = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jckDateStartCredit = new javax.swing.JCheckBox();
        jftDateStartCredit = new javax.swing.JFormattedTextField();
        jbDateStartCredit = new javax.swing.JButton();
        jlDateMaturity = new javax.swing.JLabel();
        jftDateMaturityRo = new javax.swing.JFormattedTextField();
        jPanel62 = new javax.swing.JPanel();
        jlDateDocDelivery_n = new javax.swing.JLabel();
        jftDateDocDelivery_n = new javax.swing.JFormattedTextField();
        jbDateDocDelivery_n = new javax.swing.JButton();
        jlDateDocLapsing_n = new javax.swing.JLabel();
        jftDateDocLapsing_n = new javax.swing.JFormattedTextField();
        jbDateDocLapsing_n = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jlFkPaymentTypeId = new javax.swing.JLabel();
        jcbFkPaymentTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel25 = new javax.swing.JPanel();
        jckPayments = new javax.swing.JCheckBox();
        jtfPayments = new javax.swing.JTextField();
        jPanel35 = new javax.swing.JPanel();
        jlFkPaymentSystemTypeId = new javax.swing.JLabel();
        jcbFkPaymentSystemTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel32 = new javax.swing.JPanel();
        jlPaymentAccount = new javax.swing.JLabel();
        jcbPaymentAccount = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel26 = new javax.swing.JPanel();
        jlFkLanguageId = new javax.swing.JLabel();
        jcbFkLanguageId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel27 = new javax.swing.JPanel();
        jlFkDpsStatus = new javax.swing.JLabel();
        jtfFkDpsStatusRo = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jlFkDpsStatusValidity = new javax.swing.JLabel();
        jtfFkDpsStatusValidityRo = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jlFkDpsStatusAuthorization = new javax.swing.JLabel();
        jtfFkDpsStatusAuthorizationRo = new javax.swing.JTextField();
        jPanel92 = new javax.swing.JPanel();
        jlFkFunctionalArea = new javax.swing.JLabel();
        jcbFkFunctionalArea = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel33 = new javax.swing.JPanel();
        jlFkDpsNatureId = new javax.swing.JLabel();
        jcbFkDpsNatureId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel30 = new javax.swing.JPanel();
        jckIsAudited = new javax.swing.JCheckBox();
        jckIsSystem = new javax.swing.JCheckBox();
        jPanel31 = new javax.swing.JPanel();
        jckIsAuthorized = new javax.swing.JCheckBox();
        jckIsDeleted = new javax.swing.JCheckBox();
        jPanel13 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jckRecordUser = new javax.swing.JCheckBox();
        jPanel34 = new javax.swing.JPanel();
        jtfRecordManualDateRo = new javax.swing.JTextField();
        jtfRecordManualBranchRo = new javax.swing.JTextField();
        jtfRecordManualNumberRo = new javax.swing.JTextField();
        jbRecordManualSelect = new javax.swing.JButton();
        jbRecordManualView = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jpCurrency = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jlFkCurrencyId = new javax.swing.JLabel();
        jcbFkCurrencyId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkCurrencyId = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jlExchangeRateSystem = new javax.swing.JLabel();
        jtfExchangeRateSystemRo = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jlExchangeRate = new javax.swing.JLabel();
        jtfExchangeRate = new javax.swing.JTextField();
        jbExchangeRate = new javax.swing.JButton();
        jbComputeTotal = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jlPrepaymentsCy = new javax.swing.JLabel();
        jtfPrepaymentsCy = new javax.swing.JTextField();
        jtfPrepaymentsCyCurRo = new javax.swing.JTextField();
        jbPrepayments = new javax.swing.JButton();
        jlPrepaymentsWarning = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jckIsDiscountDocApplying = new javax.swing.JCheckBox();
        jckIsDiscountDocPercentage = new javax.swing.JCheckBox();
        jtfDiscountDocPercentage = new javax.swing.JTextField();
        jPanel37 = new javax.swing.JPanel();
        jckIsCopy = new javax.swing.JCheckBox();
        jpValue = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jlCurrency = new javax.swing.JLabel();
        jtfCurrencySystemKeyRo = new javax.swing.JTextField();
        jtfCurrencyKeyRo = new javax.swing.JTextField();
        jlSubtotalProvisional = new javax.swing.JLabel();
        jtfSubtotalProvisional_rRo = new javax.swing.JTextField();
        jtfSubtotalProvisionalCy_rRo = new javax.swing.JTextField();
        jlDiscountDoc = new javax.swing.JLabel();
        jtfDiscountDoc_rRo = new javax.swing.JTextField();
        jtfDiscountDocCy_rRo = new javax.swing.JTextField();
        jlSubtotal = new javax.swing.JLabel();
        jtfSubtotal_rRo = new javax.swing.JTextField();
        jtfSubtotalCy_rRo = new javax.swing.JTextField();
        jlTaxCharged = new javax.swing.JLabel();
        jtfTaxCharged_rRo = new javax.swing.JTextField();
        jtfTaxChargedCy_rRo = new javax.swing.JTextField();
        jlTaxRetained = new javax.swing.JLabel();
        jtfTaxRetained_rRo = new javax.swing.JTextField();
        jtfTaxRetainedCy_rRo = new javax.swing.JTextField();
        jlTotal = new javax.swing.JLabel();
        jtfTotal_rRo = new javax.swing.JTextField();
        jtfTotalCy_rRo = new javax.swing.JTextField();
        jTabbedPane = new javax.swing.JTabbedPane();
        jpEntries = new javax.swing.JPanel();
        jpEntriesControls = new javax.swing.JPanel();
        jpEntriesControlsWest = new javax.swing.JPanel();
        jbEntryNew = new javax.swing.JButton();
        jbEntryEdit = new javax.swing.JButton();
        jbEntryDelete = new javax.swing.JButton();
        jsEntry01 = new javax.swing.JSeparator();
        jtbEntryFilter = new javax.swing.JToggleButton();
        jsEntry02 = new javax.swing.JSeparator();
        jbEntryDiscountRetailChain = new javax.swing.JButton();
        jbEntryImportFromDps = new javax.swing.JButton();
        jbEntryWizard = new javax.swing.JButton();
        jsEntry03 = new javax.swing.JSeparator();
        jbEntryViewLinks = new javax.swing.JButton();
        jpEntriesControlsEast = new javax.swing.JPanel();
        jlAdjustmentSubtypeId = new javax.swing.JLabel();
        jcbAdjustmentSubtypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jlTaxRegionId = new javax.swing.JLabel();
        jcbTaxRegionId = new javax.swing.JComboBox<SFormComponentItem>();
        jbTaxRegionId = new javax.swing.JButton();
        jpMarketing = new javax.swing.JPanel();
        jpOtherSupply = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jlDateShipment_n = new javax.swing.JLabel();
        jftDateShipment_nRo = new javax.swing.JFormattedTextField();
        jPanel43 = new javax.swing.JPanel();
        jlDateDelivery_n = new javax.swing.JLabel();
        jftDateDelivery_nRo = new javax.swing.JFormattedTextField();
        jPanel44 = new javax.swing.JPanel();
        jckShipments = new javax.swing.JCheckBox();
        jtfShipments = new javax.swing.JTextField();
        jPanel59 = new javax.swing.JPanel();
        jckIsLinked = new javax.swing.JCheckBox();
        jPanel45 = new javax.swing.JPanel();
        jckIsClosed = new javax.swing.JCheckBox();
        jPanel38 = new javax.swing.JPanel();
        jlFkProductionOrderId_n = new javax.swing.JLabel();
        jPanel40 = new javax.swing.JPanel();
        jcbFkProductionOrderId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkProductionOrderId_n = new javax.swing.JButton();
        jpOtherMarketing = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jPanel60 = new javax.swing.JPanel();
        jlSalesAgentBizPartner = new javax.swing.JLabel();
        jPanel47 = new javax.swing.JPanel();
        jtfSalesAgentBizPartnerRo = new javax.swing.JTextField();
        jPanel66 = new javax.swing.JPanel();
        jlSalesSupervisorBizPartner = new javax.swing.JLabel();
        jPanel63 = new javax.swing.JPanel();
        jtfSalesSupervisorBizPartnerRo = new javax.swing.JTextField();
        jPanel61 = new javax.swing.JPanel();
        jlSalesAgent = new javax.swing.JLabel();
        jPanel48 = new javax.swing.JPanel();
        jtfSalesAgentRo = new javax.swing.JTextField();
        jbSalesAgent = new javax.swing.JButton();
        jPanel89 = new javax.swing.JPanel();
        jlSalesSupervisor = new javax.swing.JLabel();
        jPanel56 = new javax.swing.JPanel();
        jtfSalesSupervisorRo = new javax.swing.JTextField();
        jbSalesSupervisor = new javax.swing.JButton();
        jPanel64 = new javax.swing.JPanel();
        jlFkContactId_n = new javax.swing.JLabel();
        jcbFkContactId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jpOtherLogistics = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();
        jlFkIncotermId = new javax.swing.JLabel();
        jcbFkIncotermId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkIncotermId = new javax.swing.JButton();
        jPanel84 = new javax.swing.JPanel();
        jlFkSpotSrcId_n = new javax.swing.JLabel();
        jcbFkSpotSrcId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel85 = new javax.swing.JPanel();
        jlFkSpotDesId_n = new javax.swing.JLabel();
        jcbFkSpotDesId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel51 = new javax.swing.JPanel();
        jlFkModeOfTransportationTypeId = new javax.swing.JLabel();
        jcbFkModeOfTransportationTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkModeOfTransportationTypeId = new javax.swing.JButton();
        jPanel52 = new javax.swing.JPanel();
        jlFkCarrierTypeId = new javax.swing.JLabel();
        jcbFkCarrierTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel53 = new javax.swing.JPanel();
        jlFkCarrierId_n = new javax.swing.JLabel();
        jcbFkCarrierId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkCarrierId_n = new javax.swing.JButton();
        jPanel86 = new javax.swing.JPanel();
        jlFkVehicleTypeId_n = new javax.swing.JLabel();
        jcbFkVehicleTypeId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel54 = new javax.swing.JPanel();
        jlFkVehicleId_n = new javax.swing.JLabel();
        jcbFkVehicleId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkVehicleId_n = new javax.swing.JButton();
        jPanel55 = new javax.swing.JPanel();
        jlDriver = new javax.swing.JLabel();
        jcbDriver = new javax.swing.JComboBox();
        jcbPlate = new javax.swing.JComboBox();
        jPanel36 = new javax.swing.JPanel();
        jlTicket = new javax.swing.JLabel();
        jtfTicket = new javax.swing.JTextField();
        jpNotes = new javax.swing.JPanel();
        jpNotesControls = new javax.swing.JPanel();
        jbNotesNew = new javax.swing.JButton();
        jbNotesEdit = new javax.swing.JButton();
        jbNotesDelete = new javax.swing.JButton();
        jsNotes01 = new javax.swing.JSeparator();
        jtbNotesFilter = new javax.swing.JToggleButton();
        jbSystemNotes = new javax.swing.JButton();
        jpAddendaData = new javax.swing.JPanel();
        jPanel91 = new javax.swing.JPanel();
        jPanel67 = new javax.swing.JPanel();
        jPanel58 = new javax.swing.JPanel();
        jPanel68 = new javax.swing.JPanel();
        jlFkCfdAddendaTypeId = new javax.swing.JLabel();
        jtfFkCfdAddendaTypeId = new javax.swing.JTextField();
        jPanel41 = new javax.swing.JPanel();
        jlCfdAddendaSubtypeId = new javax.swing.JLabel();
        jcbCfdAddendaSubtypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel69 = new javax.swing.JPanel();
        jlFolioNotaRecepcion = new javax.swing.JLabel();
        jtfFolioNotaRecepcion = new javax.swing.JTextField();
        jPanel70 = new javax.swing.JPanel();
        jlBachocoSociedad = new javax.swing.JLabel();
        jtfBachocoSociedad = new javax.swing.JTextField();
        jPanel71 = new javax.swing.JPanel();
        jlBachocoOrganizacion = new javax.swing.JLabel();
        jtfBachocoOrganizacion = new javax.swing.JTextField();
        jPanel72 = new javax.swing.JPanel();
        jlBachocoDivision = new javax.swing.JLabel();
        jtfBachocoDivision = new javax.swing.JTextField();
        jPanel81 = new javax.swing.JPanel();
        jlDpsDescripcion = new javax.swing.JLabel();
        jtfDpsDescripcion = new javax.swing.JTextField();
        jPanel76 = new javax.swing.JPanel();
        jPanel82 = new javax.swing.JPanel();
        jlStore = new javax.swing.JLabel();
        jtfStore = new javax.swing.JTextField();
        jPanel87 = new javax.swing.JPanel();
        jlGoodsDelivery = new javax.swing.JLabel();
        jtfGoodsDelivery = new javax.swing.JTextField();
        jPanel77 = new javax.swing.JPanel();
        jlRemissionDate = new javax.swing.JLabel();
        jftRemissionDate = new javax.swing.JFormattedTextField();
        jPanel79 = new javax.swing.JPanel();
        jlRemission = new javax.swing.JLabel();
        jtfRemission = new javax.swing.JTextField();
        jPanel88 = new javax.swing.JPanel();
        jlSalesOrder = new javax.swing.JLabel();
        jtfSalesOrder = new javax.swing.JTextField();
        jPanel83 = new javax.swing.JPanel();
        jlBulkType = new javax.swing.JLabel();
        jtfBulkType = new javax.swing.JTextField();
        jPanel78 = new javax.swing.JPanel();
        jlBulkQuantity = new javax.swing.JLabel();
        jtfBulkQuantity = new javax.swing.JTextField();
        jPanel90 = new javax.swing.JPanel();
        jlNumberNoteIn = new javax.swing.JLabel();
        jtfNumberNoteIn = new javax.swing.JTextField();
        jPanel73 = new javax.swing.JPanel();
        jPanel74 = new javax.swing.JPanel();
        jPanel75 = new javax.swing.JPanel();
        jlFileXml = new javax.swing.JLabel();
        jtfFileXml = new javax.swing.JTextField();
        jbFileXml = new javax.swing.JButton();
        jbDeleteFileXml = new javax.swing.JButton();
        jPanel93 = new javax.swing.JPanel();
        jlCfdUseId = new javax.swing.JLabel();
        jcbCfdUseId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel94 = new javax.swing.JPanel();
        jlFkCfdConfirmationNum = new javax.swing.JLabel();
        jtfCfdConfirmationNum = new javax.swing.JTextField();
        jpControls = new javax.swing.JPanel();
        jpControlsRecord = new javax.swing.JPanel();
        jpControlsButtons = new javax.swing.JPanel();
        jpControlsButtonsCenter = new javax.swing.JPanel();
        jbEdit = new javax.swing.JButton();
        jbEditHelp = new javax.swing.JButton();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jPanel80 = new javax.swing.JPanel();
        jlQuantityTotal = new javax.swing.JLabel();
        jtfQuantityTotal = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Documento de compras-ventas"); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jpDocument.setLayout(new java.awt.BorderLayout());

        jpHeader.setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del documento:"));
        jPanel4.setPreferredSize(new java.awt.Dimension(400, 341));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel57.setLayout(new java.awt.GridLayout(13, 1, 0, 1));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDpsType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlDpsType.setText("Tipo documento:");
        jlDpsType.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel6.add(jlDpsType);

        jtfDpsTypeRo.setEditable(false);
        jtfDpsTypeRo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfDpsTypeRo.setText("DOCUMENT TYPE");
        jtfDpsTypeRo.setFocusable(false);
        jtfDpsTypeRo.setPreferredSize(new java.awt.Dimension(270, 23));
        jPanel6.add(jtfDpsTypeRo);

        jPanel57.add(jPanel6);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCompanyBranch.setText("Sucursal empresa:");
        jlCompanyBranch.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel12.add(jlCompanyBranch);

        jtfCompanyBranchRo.setEditable(false);
        jtfCompanyBranchRo.setBackground(java.awt.Color.lightGray);
        jtfCompanyBranchRo.setText("COMPANY BRANCH");
        jtfCompanyBranchRo.setFocusable(false);
        jtfCompanyBranchRo.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel12.add(jtfCompanyBranchRo);

        jtfCompanyBranchCodeRo.setEditable(false);
        jtfCompanyBranchCodeRo.setBackground(java.awt.Color.lightGray);
        jtfCompanyBranchCodeRo.setText("CODE");
        jtfCompanyBranchCodeRo.setFocusable(false);
        jtfCompanyBranchCodeRo.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel12.add(jtfCompanyBranchCodeRo);

        jPanel57.add(jPanel12);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner.setText("Asociado negocios:");
        jlBizPartner.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel7.add(jlBizPartner);

        jtfBizPartnerRo.setEditable(false);
        jtfBizPartnerRo.setText("BUSINESS PARTNER");
        jtfBizPartnerRo.setFocusable(false);
        jtfBizPartnerRo.setPreferredSize(new java.awt.Dimension(270, 23));
        jPanel7.add(jtfBizPartnerRo);

        jPanel57.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerBranch.setText("Sucursal asociado:");
        jlBizPartnerBranch.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel8.add(jlBizPartnerBranch);

        jtfBizPartnerBranchRo.setEditable(false);
        jtfBizPartnerBranchRo.setText("BRANCH");
        jtfBizPartnerBranchRo.setFocusable(false);
        jtfBizPartnerBranchRo.setPreferredSize(new java.awt.Dimension(270, 23));
        jPanel8.add(jtfBizPartnerBranchRo);

        jPanel57.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerBranchAddressMain01.setText("Domicilio sucursal:");
        jlBizPartnerBranchAddressMain01.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel9.add(jlBizPartnerBranchAddressMain01);

        jtfBizPartnerBranchAddressMain01Ro.setEditable(false);
        jtfBizPartnerBranchAddressMain01Ro.setText("ADDRESS 01");
        jtfBizPartnerBranchAddressMain01Ro.setFocusable(false);
        jtfBizPartnerBranchAddressMain01Ro.setPreferredSize(new java.awt.Dimension(270, 23));
        jPanel9.add(jtfBizPartnerBranchAddressMain01Ro);

        jPanel57.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerBranchAddressMain02.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel10.add(jlBizPartnerBranchAddressMain02);

        jtfBizPartnerBranchAddressMain02Ro.setEditable(false);
        jtfBizPartnerBranchAddressMain02Ro.setText("ADDRESS 02");
        jtfBizPartnerBranchAddressMain02Ro.setFocusable(false);
        jtfBizPartnerBranchAddressMain02Ro.setPreferredSize(new java.awt.Dimension(270, 23));
        jPanel10.add(jtfBizPartnerBranchAddressMain02Ro);

        jPanel57.add(jPanel10);

        jPanel65.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerBranchAddress01.setText("Domicilio operación:");
        jlBizPartnerBranchAddress01.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel65.add(jlBizPartnerBranchAddress01);

        jtfBizPartnerBranchAddress01Ro.setEditable(false);
        jtfBizPartnerBranchAddress01Ro.setText("ADDRESS 01");
        jtfBizPartnerBranchAddress01Ro.setFocusable(false);
        jtfBizPartnerBranchAddress01Ro.setPreferredSize(new java.awt.Dimension(270, 23));
        jPanel65.add(jtfBizPartnerBranchAddress01Ro);

        jPanel57.add(jPanel65);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerBranchAddress02.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel17.add(jlBizPartnerBranchAddress02);

        jtfBizPartnerBranchAddress02Ro.setEditable(false);
        jtfBizPartnerBranchAddress02Ro.setText("ADDRESS 02");
        jtfBizPartnerBranchAddress02Ro.setFocusable(false);
        jtfBizPartnerBranchAddress02Ro.setPreferredSize(new java.awt.Dimension(270, 23));
        jPanel17.add(jtfBizPartnerBranchAddress02Ro);

        jPanel57.add(jPanel17);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate.setText("Fecha: *");
        jlDate.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel11.add(jlDate);

        jftDate.setText("yyyy/mm/dd");
        jftDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel11.add(jftDate);

        jbDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDate.setToolTipText("Seleccionar fecha");
        jbDate.setFocusable(false);
        jbDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbDate);

        jckIsRebill.setText("Re-emisión");
        jckIsRebill.setPreferredSize(new java.awt.Dimension(83, 23));
        jPanel11.add(jckIsRebill);

        jbBizPartnerBalance.setText("Saldo AN");
        jbBizPartnerBalance.setToolTipText("Ver saldo del asociado de negocios");
        jbBizPartnerBalance.setFocusable(false);
        jPanel11.add(jbBizPartnerBalance);

        jPanel57.add(jPanel11);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNumber.setText("Serie y folio: *");
        jlNumber.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel16.add(jlNumber);

        jcbNumberSeries.setToolTipText("Serie");
        jcbNumberSeries.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel16.add(jcbNumberSeries);

        jtfNumber.setText("00000000");
        jtfNumber.setToolTipText("Folio");
        jtfNumber.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel16.add(jtfNumber);

        jtfNumberReference.setText("ABC000ABC000");
        jtfNumberReference.setToolTipText("Referencia");
        jtfNumberReference.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel16.add(jtfNumberReference);

        jPanel57.add(jPanel16);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckDateDoc.setText("Fecha docto.:");
        jckDateDoc.setMargin(new java.awt.Insets(2, 0, 2, 2));
        jckDateDoc.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel18.add(jckDateDoc);

        jftDateDoc.setText("yyyy/mm/dd");
        jftDateDoc.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel18.add(jftDateDoc);

        jbDateDoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateDoc.setToolTipText("Seleccionar fecha");
        jbDateDoc.setFocusable(false);
        jbDateDoc.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel18.add(jbDateDoc);

        jlDaysOfCredit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlDaysOfCredit.setText(" Días crédito:");
        jlDaysOfCredit.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel18.add(jlDaysOfCredit);

        jtfDaysOfCredit.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDaysOfCredit.setText("0");
        jtfDaysOfCredit.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel18.add(jtfDaysOfCredit);

        jPanel57.add(jPanel18);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckDateStartCredit.setText("Base crédito:");
        jckDateStartCredit.setMargin(new java.awt.Insets(2, 0, 2, 2));
        jckDateStartCredit.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel15.add(jckDateStartCredit);

        jftDateStartCredit.setText("yyyy/mm/dd");
        jftDateStartCredit.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel15.add(jftDateStartCredit);

        jbDateStartCredit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateStartCredit.setToolTipText("Seleccionar fecha");
        jbDateStartCredit.setFocusable(false);
        jbDateStartCredit.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel15.add(jbDateStartCredit);

        jlDateMaturity.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlDateMaturity.setText(" Vencimiento:");
        jlDateMaturity.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel15.add(jlDateMaturity);

        jftDateMaturityRo.setEditable(false);
        jftDateMaturityRo.setText("yyyy/mm/dd");
        jftDateMaturityRo.setFocusable(false);
        jftDateMaturityRo.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel15.add(jftDateMaturityRo);

        jPanel57.add(jPanel15);

        jPanel62.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateDocDelivery_n.setText("Entrega programada:");
        jlDateDocDelivery_n.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel62.add(jlDateDocDelivery_n);

        jftDateDocDelivery_n.setText("yyyy/mm/dd");
        jftDateDocDelivery_n.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel62.add(jftDateDocDelivery_n);

        jbDateDocDelivery_n.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateDocDelivery_n.setToolTipText("Seleccionar fecha");
        jbDateDocDelivery_n.setFocusable(false);
        jbDateDocDelivery_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel62.add(jbDateDocDelivery_n);

        jlDateDocLapsing_n.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlDateDocLapsing_n.setText(" Últ. entrega:");
        jlDateDocLapsing_n.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel62.add(jlDateDocLapsing_n);

        jftDateDocLapsing_n.setText("yyyy/mm/dd");
        jftDateDocLapsing_n.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel62.add(jftDateDocLapsing_n);

        jbDateDocLapsing_n.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateDocLapsing_n.setToolTipText("Seleccionar fecha");
        jbDateDocLapsing_n.setFocusable(false);
        jbDateDocLapsing_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel62.add(jbDateDocLapsing_n);

        jPanel57.add(jPanel62);

        jPanel4.add(jPanel57, java.awt.BorderLayout.NORTH);

        jpHeader.add(jPanel4, java.awt.BorderLayout.WEST);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Estado del documento:"));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.GridLayout(12, 1, 0, 1));

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkPaymentTypeId.setText("Tipo de pago: *");
        jlFkPaymentTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel24.add(jlFkPaymentTypeId);

        jcbFkPaymentTypeId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel24.add(jcbFkPaymentTypeId);

        jPanel5.add(jPanel24);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckPayments.setText("Pagos prog.:");
        jckPayments.setFocusable(false);
        jckPayments.setMargin(new java.awt.Insets(2, 0, 2, 2));
        jckPayments.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel25.add(jckPayments);

        jtfPayments.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayments.setText("0");
        jtfPayments.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel25.add(jtfPayments);

        jPanel5.add(jPanel25);

        jPanel35.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkPaymentSystemTypeId.setText("Forma de pago: *");
        jlFkPaymentSystemTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel35.add(jlFkPaymentSystemTypeId);

        jcbFkPaymentSystemTypeId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel35.add(jcbFkPaymentSystemTypeId);

        jPanel5.add(jPanel35);

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPaymentAccount.setText("No. cuenta pago:");
        jlPaymentAccount.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel32.add(jlPaymentAccount);

        jcbPaymentAccount.setEditable(true);
        jcbPaymentAccount.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel32.add(jcbPaymentAccount);

        jPanel5.add(jPanel32);

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkLanguageId.setText("Idioma: *");
        jlFkLanguageId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel26.add(jlFkLanguageId);

        jcbFkLanguageId.setEnabled(false);
        jcbFkLanguageId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel26.add(jcbFkLanguageId);

        jPanel5.add(jPanel26);

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkDpsStatus.setText("Estado documento:");
        jlFkDpsStatus.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel27.add(jlFkDpsStatus);

        jtfFkDpsStatusRo.setEditable(false);
        jtfFkDpsStatusRo.setText("STATUS");
        jtfFkDpsStatusRo.setFocusable(false);
        jtfFkDpsStatusRo.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel27.add(jtfFkDpsStatusRo);

        jPanel5.add(jPanel27);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkDpsStatusValidity.setText("Estado validez:");
        jlFkDpsStatusValidity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel28.add(jlFkDpsStatusValidity);

        jtfFkDpsStatusValidityRo.setEditable(false);
        jtfFkDpsStatusValidityRo.setText("STATUS");
        jtfFkDpsStatusValidityRo.setFocusable(false);
        jtfFkDpsStatusValidityRo.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel28.add(jtfFkDpsStatusValidityRo);

        jPanel5.add(jPanel28);

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkDpsStatusAuthorization.setText("Estado autorización:");
        jlFkDpsStatusAuthorization.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel29.add(jlFkDpsStatusAuthorization);

        jtfFkDpsStatusAuthorizationRo.setEditable(false);
        jtfFkDpsStatusAuthorizationRo.setText("STATUS");
        jtfFkDpsStatusAuthorizationRo.setFocusable(false);
        jtfFkDpsStatusAuthorizationRo.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel29.add(jtfFkDpsStatusAuthorizationRo);

        jPanel5.add(jPanel29);

        jPanel92.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkFunctionalArea.setText("Área funcional: *");
        jlFkFunctionalArea.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel92.add(jlFkFunctionalArea);

        jcbFkFunctionalArea.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel92.add(jcbFkFunctionalArea);

        jPanel5.add(jPanel92);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkDpsNatureId.setText("Naturaleza docto.: *");
        jlFkDpsNatureId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel33.add(jlFkDpsNatureId);

        jcbFkDpsNatureId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel33.add(jcbFkDpsNatureId);

        jPanel5.add(jPanel33);

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsAudited.setText("Auditado");
        jckIsAudited.setEnabled(false);
        jckIsAudited.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel30.add(jckIsAudited);

        jckIsSystem.setText("Sistema");
        jckIsSystem.setEnabled(false);
        jckIsSystem.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel30.add(jckIsSystem);

        jPanel5.add(jPanel30);

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsAuthorized.setText("Autorizado");
        jckIsAuthorized.setEnabled(false);
        jckIsAuthorized.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel31.add(jckIsAuthorized);

        jckIsDeleted.setText("Eliminado");
        jckIsDeleted.setEnabled(false);
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel31.add(jckIsDeleted);

        jPanel5.add(jPanel31);

        jPanel3.add(jPanel5, java.awt.BorderLayout.NORTH);

        jPanel13.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckRecordUser.setText("Póliza contable:");
        jckRecordUser.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel19.add(jckRecordUser);

        jPanel13.add(jPanel19);

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfRecordManualDateRo.setEditable(false);
        jtfRecordManualDateRo.setText("01/01/2000");
        jtfRecordManualDateRo.setToolTipText("Fecha de la póliza contable");
        jtfRecordManualDateRo.setFocusable(false);
        jtfRecordManualDateRo.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel34.add(jtfRecordManualDateRo);

        jtfRecordManualBranchRo.setEditable(false);
        jtfRecordManualBranchRo.setText("SUC");
        jtfRecordManualBranchRo.setToolTipText("Sucursal de la empresa");
        jtfRecordManualBranchRo.setFocusable(false);
        jtfRecordManualBranchRo.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel34.add(jtfRecordManualBranchRo);

        jtfRecordManualNumberRo.setEditable(false);
        jtfRecordManualNumberRo.setText("TP-000001");
        jtfRecordManualNumberRo.setToolTipText("Número de póliza contable");
        jtfRecordManualNumberRo.setFocusable(false);
        jtfRecordManualNumberRo.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel34.add(jtfRecordManualNumberRo);

        jbRecordManualSelect.setText("...");
        jbRecordManualSelect.setToolTipText("Seleccionar póliza contable");
        jbRecordManualSelect.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel34.add(jbRecordManualSelect);

        jbRecordManualView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif"))); // NOI18N
        jbRecordManualView.setToolTipText("Ver póliza contable");
        jbRecordManualView.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel34.add(jbRecordManualView);

        jPanel13.add(jPanel34);

        jPanel3.add(jPanel13, java.awt.BorderLayout.SOUTH);

        jpHeader.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Valor del documento:"));
        jPanel1.setPreferredSize(new java.awt.Dimension(350, 33));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jpCurrency.setLayout(new java.awt.GridLayout(6, 1, 0, 1));

        jPanel20.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCurrencyId.setForeground(java.awt.Color.blue);
        jlFkCurrencyId.setText("Moneda documento: *");
        jlFkCurrencyId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel20.add(jlFkCurrencyId);

        jcbFkCurrencyId.setPreferredSize(new java.awt.Dimension(165, 23));
        jPanel20.add(jcbFkCurrencyId);

        jbFkCurrencyId.setText("...");
        jbFkCurrencyId.setToolTipText("Seleccionar moneda");
        jbFkCurrencyId.setFocusable(false);
        jbFkCurrencyId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel20.add(jbFkCurrencyId);

        jpCurrency.add(jPanel20);

        jPanel21.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlExchangeRateSystem.setText("Tipo de cambio sistema:");
        jlExchangeRateSystem.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel21.add(jlExchangeRateSystem);

        jtfExchangeRateSystemRo.setEditable(false);
        jtfExchangeRateSystemRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfExchangeRateSystemRo.setText("0.0000");
        jtfExchangeRateSystemRo.setFocusable(false);
        jtfExchangeRateSystemRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel21.add(jtfExchangeRateSystemRo);

        jpCurrency.add(jPanel21);

        jPanel22.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlExchangeRate.setText("Tipo de cambio: *");
        jlExchangeRate.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel22.add(jlExchangeRate);

        jtfExchangeRate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfExchangeRate.setText("0.0000");
        jtfExchangeRate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel22.add(jtfExchangeRate);

        jbExchangeRate.setText("...");
        jbExchangeRate.setToolTipText("Seleccionar tipo de cambio");
        jbExchangeRate.setFocusable(false);
        jbExchangeRate.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel22.add(jbExchangeRate);

        jbComputeTotal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbComputeTotal.setToolTipText("Calcular total");
        jbComputeTotal.setFocusable(false);
        jbComputeTotal.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel22.add(jbComputeTotal);

        jpCurrency.add(jPanel22);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPrepaymentsCy.setText("Anticipos disponibles:");
        jlPrepaymentsCy.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel2.add(jlPrepaymentsCy);

        jtfPrepaymentsCy.setEditable(false);
        jtfPrepaymentsCy.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPrepaymentsCy.setText("0.00");
        jtfPrepaymentsCy.setFocusable(false);
        jtfPrepaymentsCy.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel2.add(jtfPrepaymentsCy);

        jtfPrepaymentsCyCurRo.setEditable(false);
        jtfPrepaymentsCyCurRo.setText("CUR");
        jtfPrepaymentsCyCurRo.setFocusable(false);
        jtfPrepaymentsCyCurRo.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel2.add(jtfPrepaymentsCyCurRo);

        jbPrepayments.setText("...");
        jbPrepayments.setToolTipText("Ver anticipos totales");
        jbPrepayments.setFocusable(false);
        jbPrepayments.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel2.add(jbPrepayments);

        jlPrepaymentsWarning.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlPrepaymentsWarning.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_warn.png"))); // NOI18N
        jlPrepaymentsWarning.setOpaque(true);
        jlPrepaymentsWarning.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel2.add(jlPrepaymentsWarning);

        jpCurrency.add(jPanel2);

        jPanel23.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsDiscountDocApplying.setText("Descuento:");
        jckIsDiscountDocApplying.setFocusable(false);
        jckIsDiscountDocApplying.setMargin(new java.awt.Insets(2, 0, 2, 2));
        jckIsDiscountDocApplying.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel23.add(jckIsDiscountDocApplying);

        jckIsDiscountDocPercentage.setText("%");
        jckIsDiscountDocPercentage.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel23.add(jckIsDiscountDocPercentage);

        jtfDiscountDocPercentage.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDiscountDocPercentage.setText("0.00%");
        jtfDiscountDocPercentage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel23.add(jtfDiscountDocPercentage);

        jpCurrency.add(jPanel23);

        jPanel37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsCopy.setText("Es copia");
        jckIsCopy.setMargin(new java.awt.Insets(2, 0, 2, 2));
        jckIsCopy.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel37.add(jckIsCopy);

        jpCurrency.add(jPanel37);

        jPanel1.add(jpCurrency, java.awt.BorderLayout.NORTH);

        jpValue.setLayout(new java.awt.BorderLayout());

        jPanel14.setLayout(new java.awt.GridLayout(7, 3, 5, 1));

        jlCurrency.setText("Moneda:");
        jlCurrency.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlCurrency);

        jtfCurrencySystemKeyRo.setEditable(false);
        jtfCurrencySystemKeyRo.setText("ERP");
        jtfCurrencySystemKeyRo.setFocusable(false);
        jtfCurrencySystemKeyRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jtfCurrencySystemKeyRo);

        jtfCurrencyKeyRo.setEditable(false);
        jtfCurrencyKeyRo.setText("CUR");
        jtfCurrencyKeyRo.setFocusable(false);
        jtfCurrencyKeyRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jtfCurrencyKeyRo);

        jlSubtotalProvisional.setText("Subtotal provisional:");
        jlSubtotalProvisional.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlSubtotalProvisional);

        jtfSubtotalProvisional_rRo.setEditable(false);
        jtfSubtotalProvisional_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSubtotalProvisional_rRo.setText("0,000,000,000.00");
        jtfSubtotalProvisional_rRo.setFocusable(false);
        jtfSubtotalProvisional_rRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jtfSubtotalProvisional_rRo);

        jtfSubtotalProvisionalCy_rRo.setEditable(false);
        jtfSubtotalProvisionalCy_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSubtotalProvisionalCy_rRo.setText("0,000,000,000.00");
        jtfSubtotalProvisionalCy_rRo.setFocusable(false);
        jtfSubtotalProvisionalCy_rRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jtfSubtotalProvisionalCy_rRo);

        jlDiscountDoc.setText("Descuento docto. (-):");
        jlDiscountDoc.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlDiscountDoc);

        jtfDiscountDoc_rRo.setEditable(false);
        jtfDiscountDoc_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDiscountDoc_rRo.setText("0,000,000,000.00");
        jtfDiscountDoc_rRo.setFocusable(false);
        jtfDiscountDoc_rRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jtfDiscountDoc_rRo);

        jtfDiscountDocCy_rRo.setEditable(false);
        jtfDiscountDocCy_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDiscountDocCy_rRo.setText("0,000,000,000.00");
        jtfDiscountDocCy_rRo.setFocusable(false);
        jtfDiscountDocCy_rRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jtfDiscountDocCy_rRo);

        jlSubtotal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlSubtotal.setText("Subtotal:");
        jlSubtotal.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlSubtotal);

        jtfSubtotal_rRo.setEditable(false);
        jtfSubtotal_rRo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfSubtotal_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSubtotal_rRo.setText("0,000,000,000.00");
        jtfSubtotal_rRo.setFocusable(false);
        jtfSubtotal_rRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jtfSubtotal_rRo);

        jtfSubtotalCy_rRo.setEditable(false);
        jtfSubtotalCy_rRo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfSubtotalCy_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSubtotalCy_rRo.setText("0,000,000,000.00");
        jtfSubtotalCy_rRo.setFocusable(false);
        jtfSubtotalCy_rRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jtfSubtotalCy_rRo);

        jlTaxCharged.setText("Imptos. trasladados:");
        jlTaxCharged.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlTaxCharged);

        jtfTaxCharged_rRo.setEditable(false);
        jtfTaxCharged_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTaxCharged_rRo.setText("0,000,000,000.00");
        jtfTaxCharged_rRo.setFocusable(false);
        jtfTaxCharged_rRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jtfTaxCharged_rRo);

        jtfTaxChargedCy_rRo.setEditable(false);
        jtfTaxChargedCy_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTaxChargedCy_rRo.setText("0,000,000,000.00");
        jtfTaxChargedCy_rRo.setFocusable(false);
        jtfTaxChargedCy_rRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jtfTaxChargedCy_rRo);

        jlTaxRetained.setText("Imptos. retenidos (-):");
        jlTaxRetained.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlTaxRetained);

        jtfTaxRetained_rRo.setEditable(false);
        jtfTaxRetained_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTaxRetained_rRo.setText("0,000,000,000.00");
        jtfTaxRetained_rRo.setFocusable(false);
        jtfTaxRetained_rRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jtfTaxRetained_rRo);

        jtfTaxRetainedCy_rRo.setEditable(false);
        jtfTaxRetainedCy_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTaxRetainedCy_rRo.setText("0,000,000,000.00");
        jtfTaxRetainedCy_rRo.setFocusable(false);
        jtfTaxRetainedCy_rRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jtfTaxRetainedCy_rRo);

        jlTotal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlTotal.setText("Total:");
        jlTotal.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlTotal);

        jtfTotal_rRo.setEditable(false);
        jtfTotal_rRo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfTotal_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTotal_rRo.setText("0,000,000,000.00");
        jtfTotal_rRo.setFocusable(false);
        jtfTotal_rRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jtfTotal_rRo);

        jtfTotalCy_rRo.setEditable(false);
        jtfTotalCy_rRo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfTotalCy_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTotalCy_rRo.setText("0,000,000,000.00");
        jtfTotalCy_rRo.setFocusable(false);
        jtfTotalCy_rRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jtfTotalCy_rRo);

        jpValue.add(jPanel14, java.awt.BorderLayout.NORTH);

        jPanel1.add(jpValue, java.awt.BorderLayout.SOUTH);

        jpHeader.add(jPanel1, java.awt.BorderLayout.EAST);

        jpDocument.add(jpHeader, java.awt.BorderLayout.NORTH);

        jpEntries.setLayout(new java.awt.BorderLayout());

        jpEntriesControls.setLayout(new java.awt.BorderLayout());

        jpEntriesControlsWest.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 0));

        jbEntryNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbEntryNew.setToolTipText("Crear partida [Ctrl + N]");
        jbEntryNew.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEntriesControlsWest.add(jbEntryNew);

        jbEntryEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEntryEdit.setToolTipText("Modificar partida [Ctrl + M]");
        jbEntryEdit.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEntriesControlsWest.add(jbEntryEdit);

        jbEntryDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbEntryDelete.setToolTipText("Eliminar partida [Ctrl + D]");
        jbEntryDelete.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEntriesControlsWest.add(jbEntryDelete);

        jsEntry01.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jsEntry01.setPreferredSize(new java.awt.Dimension(3, 23));
        jpEntriesControlsWest.add(jsEntry01);

        jtbEntryFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_filter_off.gif"))); // NOI18N
        jtbEntryFilter.setToolTipText("Filtrar partidas eliminadas [Ctrl + F]");
        jtbEntryFilter.setPreferredSize(new java.awt.Dimension(23, 23));
        jtbEntryFilter.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_filter_on.gif"))); // NOI18N
        jpEntriesControlsWest.add(jtbEntryFilter);

        jsEntry02.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jsEntry02.setPreferredSize(new java.awt.Dimension(3, 23));
        jpEntriesControlsWest.add(jsEntry02);

        jbEntryDiscountRetailChain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_disc_add.gif"))); // NOI18N
        jbEntryDiscountRetailChain.setToolTipText("Crear partida descuento cadena comercial [Ctrl + R]");
        jbEntryDiscountRetailChain.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEntriesControlsWest.add(jbEntryDiscountRetailChain);

        jbEntryImportFromDps.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_doc_add.gif"))); // NOI18N
        jbEntryImportFromDps.setToolTipText("Importar partidas [Ctrl + I]");
        jbEntryImportFromDps.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEntriesControlsWest.add(jbEntryImportFromDps);

        jbEntryWizard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_wizard.gif"))); // NOI18N
        jbEntryWizard.setToolTipText("Crear varias partidas [Ctrl + W]");
        jbEntryWizard.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEntriesControlsWest.add(jbEntryWizard);

        jsEntry03.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jsEntry03.setPreferredSize(new java.awt.Dimension(3, 23));
        jpEntriesControlsWest.add(jsEntry03);

        jbEntryViewLinks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_link.gif"))); // NOI18N
        jbEntryViewLinks.setToolTipText("Ver vínculos de la partida [Ctrl + L]");
        jbEntryViewLinks.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEntriesControlsWest.add(jbEntryViewLinks);

        jpEntriesControls.add(jpEntriesControlsWest, java.awt.BorderLayout.WEST);

        jpEntriesControlsEast.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jlAdjustmentSubtypeId.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlAdjustmentSubtypeId.setText("Tipo de ajuste: ");
        jlAdjustmentSubtypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jpEntriesControlsEast.add(jlAdjustmentSubtypeId);

        jcbAdjustmentSubtypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jpEntriesControlsEast.add(jcbAdjustmentSubtypeId);

        jlTaxRegionId.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlTaxRegionId.setText("Región de impuestos: ");
        jlTaxRegionId.setPreferredSize(new java.awt.Dimension(125, 23));
        jpEntriesControlsEast.add(jlTaxRegionId);

        jcbTaxRegionId.setPreferredSize(new java.awt.Dimension(200, 23));
        jpEntriesControlsEast.add(jcbTaxRegionId);

        jbTaxRegionId.setText("...");
        jbTaxRegionId.setToolTipText("Seleccionar región de impuestos");
        jbTaxRegionId.setFocusable(false);
        jbTaxRegionId.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEntriesControlsEast.add(jbTaxRegionId);

        jpEntriesControls.add(jpEntriesControlsEast, java.awt.BorderLayout.CENTER);

        jpEntries.add(jpEntriesControls, java.awt.BorderLayout.NORTH);

        jTabbedPane.addTab("Partidas", jpEntries);

        jpMarketing.setLayout(new java.awt.BorderLayout());

        jpOtherSupply.setBorder(javax.swing.BorderFactory.createTitledBorder("Surtido del documento:"));
        jpOtherSupply.setLayout(new java.awt.BorderLayout());

        jPanel39.setLayout(new java.awt.GridLayout(8, 1, 0, 1));

        jPanel42.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDateShipment_n.setText("Fecha real de embarque:");
        jlDateShipment_n.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel42.add(jlDateShipment_n);

        jftDateShipment_nRo.setEditable(false);
        jftDateShipment_nRo.setText("yyyy/mm/dd");
        jftDateShipment_nRo.setFocusable(false);
        jftDateShipment_nRo.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel42.add(jftDateShipment_nRo);

        jPanel39.add(jPanel42);

        jPanel43.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDateDelivery_n.setText("Fecha real de entrega:");
        jlDateDelivery_n.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel43.add(jlDateDelivery_n);

        jftDateDelivery_nRo.setEditable(false);
        jftDateDelivery_nRo.setText("yyyy/mm/dd");
        jftDateDelivery_nRo.setFocusable(false);
        jftDateDelivery_nRo.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel43.add(jftDateDelivery_nRo);

        jPanel39.add(jPanel43);

        jPanel44.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jckShipments.setText("Surtidos programados:");
        jckShipments.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel44.add(jckShipments);

        jtfShipments.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfShipments.setText("0");
        jtfShipments.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel44.add(jtfShipments);

        jPanel39.add(jPanel44);

        jPanel59.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jckIsLinked.setText("Documento vinculado de forma manual");
        jckIsLinked.setEnabled(false);
        jckIsLinked.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel59.add(jckIsLinked);

        jPanel39.add(jPanel59);

        jPanel45.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jckIsClosed.setText("Documento cerrado de forma manual");
        jckIsClosed.setEnabled(false);
        jckIsClosed.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel45.add(jckIsClosed);

        jPanel39.add(jPanel45);

        jPanel38.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkProductionOrderId_n.setText("Orden de producción:");
        jlFkProductionOrderId_n.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel38.add(jlFkProductionOrderId_n);

        jPanel39.add(jPanel38);

        jPanel40.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jcbFkProductionOrderId_n.setPreferredSize(new java.awt.Dimension(235, 23));
        jPanel40.add(jcbFkProductionOrderId_n);

        jbFkProductionOrderId_n.setText("jButton1");
        jbFkProductionOrderId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel40.add(jbFkProductionOrderId_n);

        jPanel39.add(jPanel40);

        jpOtherSupply.add(jPanel39, java.awt.BorderLayout.NORTH);

        jpMarketing.add(jpOtherSupply, java.awt.BorderLayout.WEST);

        jpOtherMarketing.setBorder(javax.swing.BorderFactory.createTitledBorder("Comercialización:"));
        jpOtherMarketing.setLayout(new java.awt.BorderLayout());

        jPanel46.setLayout(new java.awt.GridLayout(9, 1, 0, 1));

        jPanel60.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlSalesAgentBizPartner.setText("Agente de ventas del asociado de negocios:");
        jlSalesAgentBizPartner.setPreferredSize(new java.awt.Dimension(295, 23));
        jPanel60.add(jlSalesAgentBizPartner);

        jPanel46.add(jPanel60);

        jPanel47.setPreferredSize(new java.awt.Dimension(108, 23));
        jPanel47.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jtfSalesAgentBizPartnerRo.setEditable(false);
        jtfSalesAgentBizPartnerRo.setText("BUSINESS PARTNER");
        jtfSalesAgentBizPartnerRo.setFocusable(false);
        jtfSalesAgentBizPartnerRo.setPreferredSize(new java.awt.Dimension(285, 23));
        jPanel47.add(jtfSalesAgentBizPartnerRo);

        jPanel46.add(jPanel47);

        jPanel66.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlSalesSupervisorBizPartner.setText("Supervisor de ventas del asociado de negocios:");
        jlSalesSupervisorBizPartner.setPreferredSize(new java.awt.Dimension(295, 23));
        jPanel66.add(jlSalesSupervisorBizPartner);

        jPanel46.add(jPanel66);

        jPanel63.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jtfSalesSupervisorBizPartnerRo.setEditable(false);
        jtfSalesSupervisorBizPartnerRo.setText("BUSINESS PARTNER");
        jtfSalesSupervisorBizPartnerRo.setFocusable(false);
        jtfSalesSupervisorBizPartnerRo.setPreferredSize(new java.awt.Dimension(285, 23));
        jPanel63.add(jtfSalesSupervisorBizPartnerRo);

        jPanel46.add(jPanel63);

        jPanel61.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlSalesAgent.setText("Agente de ventas del documento:");
        jlSalesAgent.setPreferredSize(new java.awt.Dimension(295, 23));
        jPanel61.add(jlSalesAgent);

        jPanel46.add(jPanel61);

        jPanel48.setPreferredSize(new java.awt.Dimension(108, 23));
        jPanel48.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jtfSalesAgentRo.setEditable(false);
        jtfSalesAgentRo.setText("BUSINESS PARTNER");
        jtfSalesAgentRo.setFocusable(false);
        jtfSalesAgentRo.setPreferredSize(new java.awt.Dimension(285, 23));
        jPanel48.add(jtfSalesAgentRo);

        jbSalesAgent.setText("...");
        jbSalesAgent.setToolTipText("Seleccionar agente de ventas");
        jbSalesAgent.setFocusable(false);
        jbSalesAgent.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel48.add(jbSalesAgent);

        jPanel46.add(jPanel48);

        jPanel89.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlSalesSupervisor.setText("Supervisor de ventas del documento:");
        jlSalesSupervisor.setPreferredSize(new java.awt.Dimension(295, 23));
        jPanel89.add(jlSalesSupervisor);

        jPanel46.add(jPanel89);

        jPanel56.setPreferredSize(new java.awt.Dimension(108, 23));
        jPanel56.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jtfSalesSupervisorRo.setEditable(false);
        jtfSalesSupervisorRo.setText("BUSINESS PARTNER");
        jtfSalesSupervisorRo.setFocusable(false);
        jtfSalesSupervisorRo.setPreferredSize(new java.awt.Dimension(285, 23));
        jPanel56.add(jtfSalesSupervisorRo);

        jbSalesSupervisor.setText("...");
        jbSalesSupervisor.setToolTipText("Seleccionar agente de ventas");
        jbSalesSupervisor.setFocusable(false);
        jbSalesSupervisor.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel56.add(jbSalesSupervisor);

        jPanel46.add(jPanel56);

        jPanel64.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkContactId_n.setText("Comprador:");
        jlFkContactId_n.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel64.add(jlFkContactId_n);

        jcbFkContactId_n.setPreferredSize(new java.awt.Dimension(208, 23));
        jPanel64.add(jcbFkContactId_n);

        jPanel46.add(jPanel64);

        jpOtherMarketing.add(jPanel46, java.awt.BorderLayout.NORTH);

        jpMarketing.add(jpOtherMarketing, java.awt.BorderLayout.CENTER);

        jpOtherLogistics.setBorder(javax.swing.BorderFactory.createTitledBorder("Logística:"));
        jpOtherLogistics.setPreferredSize(new java.awt.Dimension(375, 10));
        jpOtherLogistics.setLayout(new java.awt.BorderLayout());

        jPanel49.setLayout(new java.awt.GridLayout(10, 1, 0, 1));

        jPanel50.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel50.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkIncotermId.setText("Incoterm:*");
        jlFkIncotermId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel50.add(jlFkIncotermId);

        jcbFkIncotermId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel50.add(jcbFkIncotermId);

        jbFkIncotermId.setText("...");
        jbFkIncotermId.setToolTipText("Seleccionar Incoterm");
        jbFkIncotermId.setFocusable(false);
        jbFkIncotermId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel50.add(jbFkIncotermId);

        jPanel49.add(jPanel50);

        jPanel84.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel84.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkSpotSrcId_n.setText("Origen:");
        jlFkSpotSrcId_n.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel84.add(jlFkSpotSrcId_n);

        jcbFkSpotSrcId_n.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel84.add(jcbFkSpotSrcId_n);

        jPanel49.add(jPanel84);

        jPanel85.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel85.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkSpotDesId_n.setText("Destino:");
        jlFkSpotDesId_n.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel85.add(jlFkSpotDesId_n);

        jcbFkSpotDesId_n.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel85.add(jcbFkSpotDesId_n);

        jPanel49.add(jPanel85);

        jPanel51.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel51.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkModeOfTransportationTypeId.setText("Tipo modo transp.:*");
        jlFkModeOfTransportationTypeId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel51.add(jlFkModeOfTransportationTypeId);

        jcbFkModeOfTransportationTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel51.add(jcbFkModeOfTransportationTypeId);

        jbFkModeOfTransportationTypeId.setText("...");
        jbFkModeOfTransportationTypeId.setToolTipText("Seleccionar tipo de modo de transportación");
        jbFkModeOfTransportationTypeId.setFocusable(false);
        jbFkModeOfTransportationTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel51.add(jbFkModeOfTransportationTypeId);

        jPanel49.add(jPanel51);

        jPanel52.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel52.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkCarrierTypeId.setText("Tipo transportista:*");
        jlFkCarrierTypeId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel52.add(jlFkCarrierTypeId);

        jcbFkCarrierTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel52.add(jcbFkCarrierTypeId);

        jPanel49.add(jPanel52);

        jPanel53.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel53.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkCarrierId_n.setText("Transportista:");
        jlFkCarrierId_n.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel53.add(jlFkCarrierId_n);

        jcbFkCarrierId_n.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel53.add(jcbFkCarrierId_n);

        jbFkCarrierId_n.setText("...");
        jbFkCarrierId_n.setToolTipText("Seleccionar transportista");
        jbFkCarrierId_n.setFocusable(false);
        jbFkCarrierId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel53.add(jbFkCarrierId_n);

        jPanel49.add(jPanel53);

        jPanel86.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel86.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkVehicleTypeId_n.setText("Tipo vehículo:");
        jlFkVehicleTypeId_n.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel86.add(jlFkVehicleTypeId_n);

        jcbFkVehicleTypeId_n.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel86.add(jcbFkVehicleTypeId_n);

        jPanel49.add(jPanel86);

        jPanel54.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel54.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkVehicleId_n.setText("Vehículo empresa:");
        jlFkVehicleId_n.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel54.add(jlFkVehicleId_n);

        jcbFkVehicleId_n.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel54.add(jcbFkVehicleId_n);

        jbFkVehicleId_n.setText("...");
        jbFkVehicleId_n.setToolTipText("Seleccionar vehículo de la empresa");
        jbFkVehicleId_n.setFocusable(false);
        jbFkVehicleId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel54.add(jbFkVehicleId_n);

        jPanel49.add(jPanel54);

        jPanel55.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel55.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDriver.setText("Chofer y placas vehículo:");
        jlDriver.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel55.add(jlDriver);

        jcbDriver.setEditable(true);
        jcbDriver.setToolTipText("Chofer vehículo");
        jcbDriver.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel55.add(jcbDriver);

        jcbPlate.setEditable(true);
        jcbPlate.setToolTipText("Placas vehículo");
        jcbPlate.setPreferredSize(new java.awt.Dimension(97, 23));
        jPanel55.add(jcbPlate);

        jPanel49.add(jPanel55);

        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlTicket.setText("Boleto(s) báscula:");
        jlTicket.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel36.add(jlTicket);

        jtfTicket.setText("TICKET");
        jtfTicket.setPreferredSize(new java.awt.Dimension(107, 23));
        jPanel36.add(jtfTicket);

        jPanel49.add(jPanel36);

        jpOtherLogistics.add(jPanel49, java.awt.BorderLayout.NORTH);

        jpMarketing.add(jpOtherLogistics, java.awt.BorderLayout.EAST);

        jTabbedPane.addTab("Comercialización", jpMarketing);

        jpNotes.setLayout(new java.awt.BorderLayout());

        jpNotesControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 0));

        jbNotesNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbNotesNew.setToolTipText("Crear notas [Ctrl + N]");
        jbNotesNew.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesControls.add(jbNotesNew);

        jbNotesEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbNotesEdit.setToolTipText("Modificar notas [Ctrl + M]");
        jbNotesEdit.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesControls.add(jbNotesEdit);

        jbNotesDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbNotesDelete.setToolTipText("Eliminar notas [Ctrl + D]");
        jbNotesDelete.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesControls.add(jbNotesDelete);

        jsNotes01.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jsNotes01.setPreferredSize(new java.awt.Dimension(3, 23));
        jpNotesControls.add(jsNotes01);

        jtbNotesFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_filter_off.gif"))); // NOI18N
        jtbNotesFilter.setToolTipText("Filtrar notas eliminadas [Ctrl + F]");
        jtbNotesFilter.setPreferredSize(new java.awt.Dimension(23, 23));
        jtbNotesFilter.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_filter_on.gif"))); // NOI18N
        jpNotesControls.add(jtbNotesFilter);

        jbSystemNotes.setText("...");
        jbSystemNotes.setToolTipText("Seleccionar notas de sistema");
        jbSystemNotes.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesControls.add(jbSystemNotes);

        jpNotes.add(jpNotesControls, java.awt.BorderLayout.PAGE_START);

        jTabbedPane.addTab("Notas", jpNotes);

        jpAddendaData.setLayout(new java.awt.BorderLayout());

        jPanel91.setBorder(javax.swing.BorderFactory.createTitledBorder("Información para addenda:"));
        jPanel91.setLayout(new java.awt.BorderLayout());

        jPanel67.setMinimumSize(new java.awt.Dimension(300, 195));
        jPanel67.setPreferredSize(new java.awt.Dimension(625, 195));
        jPanel67.setLayout(new java.awt.BorderLayout());

        jPanel58.setPreferredSize(new java.awt.Dimension(325, 23));
        jPanel58.setLayout(new java.awt.GridLayout(8, 1));

        jPanel68.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel68.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCfdAddendaTypeId.setText("Tipo addenda:");
        jlFkCfdAddendaTypeId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel68.add(jlFkCfdAddendaTypeId);

        jtfFkCfdAddendaTypeId.setEditable(false);
        jtfFkCfdAddendaTypeId.setText("ADDENDA");
        jtfFkCfdAddendaTypeId.setFocusable(false);
        jtfFkCfdAddendaTypeId.setOpaque(false);
        jtfFkCfdAddendaTypeId.setPreferredSize(new java.awt.Dimension(170, 23));
        jPanel68.add(jtfFkCfdAddendaTypeId);

        jPanel58.add(jPanel68);

        jPanel41.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdAddendaSubtypeId.setText("Subtipo addenda:*");
        jlCfdAddendaSubtypeId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel41.add(jlCfdAddendaSubtypeId);

        jcbCfdAddendaSubtypeId.setPreferredSize(new java.awt.Dimension(170, 23));
        jPanel41.add(jcbCfdAddendaSubtypeId);

        jPanel58.add(jPanel41);

        jPanel69.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFolioNotaRecepcion.setText("Folio nota recepción: *");
        jlFolioNotaRecepcion.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel69.add(jlFolioNotaRecepcion);

        jtfFolioNotaRecepcion.setText("TEXT");
        jtfFolioNotaRecepcion.setPreferredSize(new java.awt.Dimension(170, 23));
        jPanel69.add(jtfFolioNotaRecepcion);

        jPanel58.add(jPanel69);

        jPanel70.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBachocoSociedad.setText("Sociedad: *");
        jlBachocoSociedad.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel70.add(jlBachocoSociedad);

        jtfBachocoSociedad.setText("TEXT");
        jtfBachocoSociedad.setPreferredSize(new java.awt.Dimension(170, 23));
        jPanel70.add(jtfBachocoSociedad);

        jPanel58.add(jPanel70);

        jPanel71.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBachocoOrganizacion.setText("Organización compra: *");
        jlBachocoOrganizacion.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel71.add(jlBachocoOrganizacion);

        jtfBachocoOrganizacion.setText("TEXT");
        jtfBachocoOrganizacion.setPreferredSize(new java.awt.Dimension(170, 23));
        jPanel71.add(jtfBachocoOrganizacion);

        jPanel58.add(jPanel71);

        jPanel72.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBachocoDivision.setText("División: *");
        jlBachocoDivision.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel72.add(jlBachocoDivision);

        jtfBachocoDivision.setText("TEXT");
        jtfBachocoDivision.setPreferredSize(new java.awt.Dimension(170, 23));
        jPanel72.add(jtfBachocoDivision);

        jPanel58.add(jPanel72);

        jPanel81.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDpsDescripcion.setText("Descripción factura: *");
        jlDpsDescripcion.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel81.add(jlDpsDescripcion);

        jtfDpsDescripcion.setText("TEXT");
        jtfDpsDescripcion.setPreferredSize(new java.awt.Dimension(170, 23));
        jPanel81.add(jtfDpsDescripcion);

        jPanel58.add(jPanel81);

        jPanel67.add(jPanel58, java.awt.BorderLayout.WEST);

        jPanel76.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel76.setLayout(new java.awt.GridLayout(8, 1));

        jPanel82.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlStore.setText("Tienda: *");
        jlStore.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel82.add(jlStore);

        jtfStore.setText("00000000");
        jtfStore.setToolTipText("Folio");
        jtfStore.setPreferredSize(new java.awt.Dimension(170, 23));
        jPanel82.add(jtfStore);

        jPanel76.add(jPanel82);

        jPanel87.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlGoodsDelivery.setText("Entrega merc.: *");
        jlGoodsDelivery.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel87.add(jlGoodsDelivery);

        jtfGoodsDelivery.setText("00000000");
        jtfGoodsDelivery.setToolTipText("Folio");
        jtfGoodsDelivery.setPreferredSize(new java.awt.Dimension(170, 23));
        jPanel87.add(jtfGoodsDelivery);

        jPanel76.add(jPanel87);

        jPanel77.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRemissionDate.setText("Fecha remisión: *");
        jlRemissionDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel77.add(jlRemissionDate);

        jftRemissionDate.setText("yyyy/mm/dd");
        jftRemissionDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel77.add(jftRemissionDate);

        jPanel76.add(jPanel77);

        jPanel79.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRemission.setText("Folio remisión: *");
        jlRemission.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel79.add(jlRemission);

        jtfRemission.setText("TEXT");
        jtfRemission.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel79.add(jtfRemission);

        jPanel76.add(jPanel79);

        jPanel88.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSalesOrder.setText("Pedido: *");
        jlSalesOrder.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel88.add(jlSalesOrder);

        jtfSalesOrder.setText("TEXT");
        jtfSalesOrder.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel88.add(jtfSalesOrder);

        jPanel76.add(jPanel88);

        jPanel83.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBulkType.setText("Tipo bulto: *");
        jlBulkType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel83.add(jlBulkType);

        jtfBulkType.setText("00000000");
        jtfBulkType.setToolTipText("Folio");
        jtfBulkType.setPreferredSize(new java.awt.Dimension(100, 23));
        jtfBulkType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfBulkTypeActionPerformed(evt);
            }
        });
        jPanel83.add(jtfBulkType);

        jPanel76.add(jPanel83);

        jPanel78.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBulkQuantity.setText("Cantidad bultos: *");
        jlBulkQuantity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel78.add(jlBulkQuantity);

        jtfBulkQuantity.setText("00000000");
        jtfBulkQuantity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel78.add(jtfBulkQuantity);

        jPanel76.add(jPanel78);

        jPanel90.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNumberNoteIn.setText("Fol. nota entada:*");
        jlNumberNoteIn.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel90.add(jlNumberNoteIn);

        jtfNumberNoteIn.setText("00000000");
        jtfNumberNoteIn.setToolTipText("Folio");
        jtfNumberNoteIn.setPreferredSize(new java.awt.Dimension(170, 23));
        jPanel90.add(jtfNumberNoteIn);

        jPanel76.add(jPanel90);

        jPanel67.add(jPanel76, java.awt.BorderLayout.CENTER);

        jPanel91.add(jPanel67, java.awt.BorderLayout.NORTH);

        jpAddendaData.add(jPanel91, java.awt.BorderLayout.WEST);

        jPanel73.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos XML:"));
        jPanel73.setLayout(new java.awt.BorderLayout());

        jPanel74.setLayout(new java.awt.GridLayout(3, 2, 0, 2));

        jPanel75.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel75.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFileXml.setText("Archivo XML:");
        jlFileXml.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel75.add(jlFileXml);

        jtfFileXml.setEditable(false);
        jtfFileXml.setText("XML");
        jtfFileXml.setOpaque(false);
        jtfFileXml.setPreferredSize(new java.awt.Dimension(190, 23));
        jPanel75.add(jtfFileXml);

        jbFileXml.setText("...");
        jbFileXml.setToolTipText("Seleccionar archivo XML...");
        jbFileXml.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel75.add(jbFileXml);

        jbDeleteFileXml.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbDeleteFileXml.setToolTipText("Eliminar archivo XML");
        jbDeleteFileXml.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel75.add(jbDeleteFileXml);

        jPanel74.add(jPanel75);

        jPanel93.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdUseId.setText("Uso CFDI: *");
        jlCfdUseId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel93.add(jlCfdUseId);

        jcbCfdUseId.setPreferredSize(new java.awt.Dimension(245, 23));
        jPanel93.add(jcbCfdUseId);

        jPanel74.add(jPanel93);

        jPanel94.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCfdConfirmationNum.setText("No. confirmación:");
        jlFkCfdConfirmationNum.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel94.add(jlFkCfdConfirmationNum);

        jtfCfdConfirmationNum.setText("00000000");
        jtfCfdConfirmationNum.setToolTipText("Folio");
        jtfCfdConfirmationNum.setPreferredSize(new java.awt.Dimension(190, 23));
        jPanel94.add(jtfCfdConfirmationNum);

        jPanel74.add(jPanel94);

        jPanel73.add(jPanel74, java.awt.BorderLayout.NORTH);

        jpAddendaData.add(jPanel73, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Datos CFD", jpAddendaData);

        jpDocument.add(jTabbedPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpDocument, java.awt.BorderLayout.CENTER);

        jpControls.setLayout(new java.awt.GridLayout(1, 2));

        jpControlsRecord.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jpControls.add(jpControlsRecord);

        jpControlsButtons.setLayout(new java.awt.BorderLayout());

        jpControlsButtonsCenter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbEdit.setText("Modificar"); // NOI18N
        jpControlsButtonsCenter.add(jbEdit);

        jbEditHelp.setText("?");
        jbEditHelp.setToolTipText("¿Por qué no se puede modificar el documento?");
        jbEditHelp.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbEditHelp.setPreferredSize(new java.awt.Dimension(23, 23));
        jpControlsButtonsCenter.add(jbEditHelp);

        jbOk.setText("Aceptar"); // NOI18N
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControlsButtonsCenter.add(jbOk);

        jbCancel.setText("Cancelar"); // NOI18N
        jbCancel.setToolTipText("[Escape]");
        jpControlsButtonsCenter.add(jbCancel);

        jpControlsButtons.add(jpControlsButtonsCenter, java.awt.BorderLayout.CENTER);

        jlQuantityTotal.setText("Cantidad total:");
        jlQuantityTotal.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel80.add(jlQuantityTotal);

        jtfQuantityTotal.setEditable(false);
        jtfQuantityTotal.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfQuantityTotal.setText("0.000");
        jtfQuantityTotal.setFocusable(false);
        jtfQuantityTotal.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel80.add(jtfQuantityTotal);

        jpControlsButtons.add(jPanel80, java.awt.BorderLayout.WEST);

        jpControls.add(jpControlsButtons);

        getContentPane().add(jpControls, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(1040, 709));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        actionCancel();
    }//GEN-LAST:event_formWindowClosing

    private void jtfBulkTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfBulkTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfBulkTypeActionPerformed

    private void initComponentsExtra() {
        int i = 0;
        STableColumnForm[] aoTableColumns = null;

        mvFields = new Vector<SFormField>();

        moFieldDate = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDate, jlDate);
        moFieldDate.setPickerButton(jbDate);
        moFieldDateDoc = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateDoc, jckDateDoc);
        moFieldDateDoc.setPickerButton(jbDateDoc);
        moFieldDateStartCredit = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateStartCredit, jckDateStartCredit);
        moFieldDateStartCredit.setPickerButton(jbDateStartCredit);
        moFieldDateShipment_n = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftDateShipment_nRo, jlDateShipment_n);
        moFieldDateDelivery_n = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftDateDelivery_nRo, jlDateDelivery_n);
        moFieldDateDocLapsing_n = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftDateDocLapsing_n, jlDateDocLapsing_n);
        moFieldDateDocLapsing_n.setPickerButton(jbDateDocLapsing_n);
        moFieldDateDocDelivery_n = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftDateDocDelivery_n, jlDateDocDelivery_n);
        moFieldDateDocDelivery_n.setPickerButton(jbDateDocDelivery_n);
        moFieldNumberSeries = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbNumberSeries, jlNumber);
        moFieldNumber = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfNumber, jlNumber);
        moFieldNumber.setLengthMax(15);
        moFieldNumberReference = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfNumberReference, jlNumber);
        moFieldNumberReference.setLengthMax(25);
        moFieldDaysOfCredit = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfDaysOfCredit, jlDaysOfCredit);
        moFieldIsDiscountDocApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDiscountDocApplying);
        moFieldIsDiscountDocPercentage = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDiscountDocPercentage);
        moFieldDiscountDocPercentage = new SFormField(miClient, SLibConstants.DATA_TYPE_FLOAT, false, jtfDiscountDocPercentage, jckIsDiscountDocPercentage);
        moFieldDiscountDocPercentage.setIsPercent(true);
        moFieldDiscountDocPercentage.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsPercentageFormat());
        moFieldExchangeRate = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfExchangeRate, jlExchangeRate);
        moFieldExchangeRate.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsExchangeRateFormat());
        moFieldExchangeRate.setPickerButton(jbExchangeRate);
        moFieldExchangeRateSystem = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfExchangeRateSystemRo, jlExchangeRateSystem);
        moFieldExchangeRateSystem.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsExchangeRateFormat());
        moFieldDriver = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, (JTextField) jcbDriver.getEditor().getEditorComponent(), jlDriver);
        moFieldDriver.setLengthMax(50);
        moFieldPlate = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, (JTextField) jcbPlate.getEditor().getEditorComponent(), jlDriver);
        moFieldPlate.setLengthMax(25);
        moFieldTicket = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfTicket, jlTicket);
        moFieldTicket.setLengthMax(50);
        moFieldShipments = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfShipments, jckShipments);
        moFieldPayments = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfPayments, jckPayments);
        moFieldIsLinked = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsLinked);
        moFieldIsClosed = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsClosed);
        moFieldIsAudited = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsAudited);
        moFieldIsAuthorized = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsAuthorized);
        moFieldIsCopy = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsCopy);
        moFieldIsSystem = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsSystem);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDeleted);
        moFieldFkPaymentTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkPaymentTypeId, jlFkPaymentTypeId);
        moFieldFkPaymentSystemTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkPaymentSystemTypeId, jlFkPaymentSystemTypeId);
        moFieldPaymentAccount = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, (JTextField) jcbPaymentAccount.getEditor().getEditorComponent(), jlPaymentAccount);
        moFieldPaymentAccount.setLengthMax(20);
        moFieldFkLanguajeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkLanguageId, jlFkLanguageId);
        moFieldFkFunctionalAreaId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkFunctionalArea, jlFkFunctionalArea);
        moFieldFkDpsNatureId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkDpsNatureId, jlFkDpsNatureId);
        moFieldFkCurrencyId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCurrencyId, jlFkCurrencyId);
        moFieldFkCurrencyId.setPickerButton(jbFkCurrencyId);
        moFieldFkProductionOrderId_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkProductionOrderId_n, jlFkProductionOrderId_n);
        moFieldFkProductionOrderId_n.setPickerButton(jbFkProductionOrderId_n);
        moFieldFkContactId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkContactId_n, jlFkContactId_n);
        moFieldFkIncotermId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkIncotermId, jlFkIncotermId);
        moFieldFkIncotermId.setTabbedPaneIndex(1, jTabbedPane);
        moFieldFkIncotermId.setPickerButton(jbFkIncotermId);
        moFieldFkSpotSrcId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkSpotSrcId_n, jlFkSpotSrcId_n);
        moFieldFkSpotDesId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkSpotDesId_n, jlFkSpotDesId_n);
        moFieldFkModeOfTransportationTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkModeOfTransportationTypeId, jlFkModeOfTransportationTypeId);
        moFieldFkModeOfTransportationTypeId.setTabbedPaneIndex(1, jTabbedPane);
        moFieldFkModeOfTransportationTypeId.setPickerButton(jbFkModeOfTransportationTypeId);
        moFieldFkCarrierTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCarrierTypeId, jlFkCarrierTypeId);
        moFieldFkCarrierTypeId.setTabbedPaneIndex(1, jTabbedPane);
        moFieldFkCarrierId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkCarrierId_n, jlFkCarrierId_n);
        moFieldFkCarrierId_n.setPickerButton(jbFkCarrierId_n);
        moFieldFkVehicleTypeId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkVehicleTypeId_n, jlFkVehicleTypeId_n);
        moFieldFkVehicleId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkVehicleId_n, jlFkVehicleId_n);
        moFieldFkVehicleId_n.setPickerButton(jbFkVehicleId_n);

        moFieldFolioNotaRecepcion = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfFolioNotaRecepcion, jlFolioNotaRecepcion);
        moFieldFolioNotaRecepcion.setTabbedPaneIndex(3, jTabbedPane);
        moFieldFolioNotaRecepcion.setLengthMax(50);
        moFieldBachocoSociedad = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfBachocoSociedad, jlBachocoSociedad);
        moFieldBachocoSociedad.setTabbedPaneIndex(3, jTabbedPane);
        moFieldBachocoSociedad.setLengthMax(13);
        moFieldBachocoOrganizacionCompra = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfBachocoOrganizacion, jlBachocoOrganizacion);
        moFieldBachocoOrganizacionCompra.setTabbedPaneIndex(3, jTabbedPane);
        moFieldBachocoOrganizacionCompra.setLengthMax(35);
        moFieldBachocoDivision = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfBachocoDivision, jlBachocoDivision);
        moFieldBachocoDivision.setTabbedPaneIndex(3, jTabbedPane);
        moFieldBachocoDivision.setLengthMax(35);
        moFieldDpsDescripcion = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfDpsDescripcion, jlDpsDescripcion);
        moFieldDpsDescripcion.setTabbedPaneIndex(3, jTabbedPane);
        moFieldDpsDescripcion.setLengthMax(35);
        moFieldStore = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfStore, jlStore);
        moFieldStore.setTabbedPaneIndex(3, jTabbedPane);
        moFieldGoodsDelivery = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfGoodsDelivery, jlGoodsDelivery);
        moFieldGoodsDelivery.setTabbedPaneIndex(3, jTabbedPane);
        moFieldDateRemission = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftRemissionDate, jlRemissionDate);
        moFieldDateRemission.setTabbedPaneIndex(3, jTabbedPane);
        moFieldRemission = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfRemission, jlRemission);
        moFieldRemission.setTabbedPaneIndex(3, jTabbedPane);
        moFieldSalesOrder = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfSalesOrder, jlSalesOrder);
        moFieldSalesOrder.setTabbedPaneIndex(3, jTabbedPane);
        moFieldCfdAddendaSubtypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCfdAddendaSubtypeId, jlCfdAddendaSubtypeId);
        moFieldCfdAddendaSubtypeId.setTabbedPaneIndex(3, jTabbedPane);
        moFieldBulkType = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfBulkType, jlBulkType);
        moFieldBulkType.setTabbedPaneIndex(3, jTabbedPane);
        moFieldBulkQty = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfBulkQuantity, jlBulkQuantity);
        moFieldBulkQty.setTabbedPaneIndex(3, jTabbedPane);
        moFieldNumberNoteIn = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfNumberNoteIn, jlNumberNoteIn);
        moFieldNumberNoteIn.setTabbedPaneIndex(3, jTabbedPane);
        moFieldFileXml = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfFileXml, jlFileXml);
        moFieldFileXml.setTabbedPaneIndex(3, jTabbedPane);
        moFieldFkCfdUseId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCfdUseId, jlCfdUseId);
        moFieldFkCfdUseId.setTabbedPaneIndex(3, jTabbedPane);
        moFieldCfdConfirmationNumber = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfCfdConfirmationNum, jlCfdAddendaSubtypeId);
        moFieldCfdConfirmationNumber.setTabbedPaneIndex(3, jTabbedPane);
        moFieldCfdConfirmationNumber.setLengthMax(25);

        mvFields.add(moFieldDate);
        mvFields.add(moFieldNumberSeries);
        mvFields.add(moFieldNumber);
        mvFields.add(moFieldNumberReference);
        mvFields.add(moFieldDateDoc);
        mvFields.add(moFieldDaysOfCredit);
        mvFields.add(moFieldDateStartCredit);
        mvFields.add(moFieldDateDocDelivery_n);

        mvFields.add(moFieldFkPaymentTypeId);
        mvFields.add(moFieldPayments);
        mvFields.add(moFieldFkPaymentSystemTypeId);
        mvFields.add(moFieldPaymentAccount);
        mvFields.add(moFieldFkLanguajeId);
        mvFields.add(moFieldFkFunctionalAreaId);
        mvFields.add(moFieldFkDpsNatureId);
        mvFields.add(moFieldIsAudited);
        mvFields.add(moFieldIsAuthorized);
        mvFields.add(moFieldIsSystem);
        mvFields.add(moFieldIsDeleted);

        mvFields.add(moFieldFkCurrencyId);
        mvFields.add(moFieldExchangeRate);
        mvFields.add(moFieldExchangeRateSystem);
        mvFields.add(moFieldIsDiscountDocApplying);
        mvFields.add(moFieldIsDiscountDocPercentage);
        mvFields.add(moFieldDiscountDocPercentage);
        mvFields.add(moFieldIsCopy);

        mvFields.add(moFieldDateDocLapsing_n);
        mvFields.add(moFieldDateShipment_n);
        mvFields.add(moFieldDateDelivery_n);
        mvFields.add(moFieldShipments);
        mvFields.add(moFieldIsLinked);
        mvFields.add(moFieldIsClosed);

        mvFields.add(moFieldFkProductionOrderId_n);
        mvFields.add(moFieldTicket);
        mvFields.add(moFieldFkContactId_n);
        mvFields.add(moFieldFkIncotermId);
        mvFields.add(moFieldFkSpotSrcId_n);
        mvFields.add(moFieldFkSpotDesId_n);
        mvFields.add(moFieldFkModeOfTransportationTypeId);
        mvFields.add(moFieldFkCarrierTypeId);
        mvFields.add(moFieldFkCarrierId_n);
        mvFields.add(moFieldFkVehicleTypeId_n);
        mvFields.add(moFieldFkVehicleId_n);
        mvFields.add(moFieldDriver);
        mvFields.add(moFieldPlate);

        mvFields.add(moFieldCfdAddendaSubtypeId);
        mvFields.add(moFieldFolioNotaRecepcion);
        mvFields.add(moFieldBachocoSociedad);
        mvFields.add(moFieldBachocoOrganizacionCompra);
        mvFields.add(moFieldBachocoDivision);
        mvFields.add(moFieldDpsDescripcion);
        mvFields.add(moFieldStore);
        mvFields.add(moFieldGoodsDelivery);
        mvFields.add(moFieldDateRemission);
        mvFields.add(moFieldRemission);
        mvFields.add(moFieldSalesOrder);
        mvFields.add(moFieldBulkType);
        mvFields.add(moFieldBulkQty);
        mvFields.add(moFieldNumberNoteIn);
        mvFields.add(moFieldFileXml);
        mvFields.add(moFieldFkCfdUseId);
        mvFields.add(moFieldCfdConfirmationNumber);

        if (mnFormType == SDataConstantsSys.TRNS_CT_DPS_PUR) {
            moPickerBizPartner = new SFormOptionPickerBizPartner(miClient, SDataConstants.BPSX_BP_SUP);
        }
        else {
            moPickerBizPartner = new SFormOptionPickerBizPartner(miClient, SDataConstants.BPSX_BP_CUS);
        }

        // Pane of document entries:

        moPaneGridEntries = new STablePaneGrid(miClient);
        moPaneGridEntries.setDoubleClickAction(this, "publicActionDependentEdit");
        jpEntries.add(moPaneGridEntries, BorderLayout.CENTER);

        i = 0;
        aoTableColumns = new STableColumnForm[26];
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "#", STableConstants.WIDTH_NUM_TINYINT);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Clave", STableConstants.WIDTH_ITEM_KEY);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Concepto", STableConstants.WIDTH_ITEM);
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cantidad", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Precio u. $", STableConstants.WIDTH_VALUE); // due to space restrictions
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitaryFixed4());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Descto. u. $", STableConstants.WIDTH_VALUE); // due to space restrictions
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitaryFixed4());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Descto. $", STableConstants.WIDTH_DISCOUNT - 15); // due to space restrictions
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Subtotal prov. $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Descto. doc. $", STableConstants.WIDTH_DISCOUNT - 15); // due to space restrictions
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Subtotal $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Impto. tras. $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Impto. ret. $", STableConstants.WIDTH_VALUE - 15); // due to space restrictions
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Total $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Vínculos como origen", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Vínculos como destino", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Ajustes como doc.", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Ajustes como doc. de ajuste", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo detalle", 100);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo ajuste", 150);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Región de impuestos", 100);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ítem referencia", STableConstants.WIDTH_ITEM);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "No. centro costo", STableConstants.WIDTH_ACCOUNT_ID);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Centro costo", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "ID interno", STableConstants.WIDTH_NUM_TINYINT);

        for (i = 0; i < aoTableColumns.length; i++) {
            moPaneGridEntries.addTableColumn(aoTableColumns[i]);
        }

        // Pane of document notes:

        moPaneGridNotes = new STablePaneGrid(miClient);
        moPaneGridNotes.setDoubleClickAction(this, "publicActionDependentEdit");
        jpNotes.add(moPaneGridNotes, BorderLayout.CENTER);

        i = 0;
        aoTableColumns = new STableColumnForm[10];
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Notas", 500);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Todos los docs.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Impresión", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Eliminación", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < aoTableColumns.length; i++) {
            moPaneGridNotes.addTableColumn(aoTableColumns[i]);
        }

        // Own forms:

        moFormEntry = new SFormDpsEntry(miClient);
        moFormEntryWizard = new SFormDpsEntryWizard(miClient);
        moFormNotes = new SFormDpsNotes(miClient);
        moDialogPickerDps = null;
        moDialogPickerDpsForLink = new SDialogPickerDps(miClient, SDataConstants.TRNX_DPS_PEND_LINK);
        moDialogPickerDpsForAdjustment = new SDialogPickerDps(miClient, SDataConstants.TRNX_DPS_PEND_ADJ);
        moDialogDpsLink = new SDialogDpsLink(miClient);
        moDialogDpsAdjustment = new SDialogDpsAdjustment(miClient);
        moDialogRecordPicker = new SDialogRecordPicker(miClient, SDataConstants.FINX_REC_USER);
        moDialogShowDocumentLinks = new SDialogShowDocumentLinks(miClient);
        moPanelRecord = new SPanelRecord(miClient);
        jpControlsRecord.add(moPanelRecord);

        // Form paramters:

        manDpsClassKey = null;
        manDpsClassPreviousKey = null;
        moDpsType = null;

        mnParamCurrentUserPrivilegeLevel = SDataConstantsSys.UNDEFINED;
        mbParamIsReadOnly = false;
        moParamDpsSource = null;
        mbIsLocalCurrency = false;
        mnTypeDelivery = 0;

        // Action listeners:

        jbEdit.addActionListener(this);
        jbEditHelp.addActionListener(this);
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbDate.addActionListener(this);
        jbDateDoc.addActionListener(this);
        jbDateStartCredit.addActionListener(this);
        jbBizPartnerBalance.addActionListener(this);
        jbRecordManualSelect.addActionListener(this);
        jbRecordManualView.addActionListener(this);
        jbFkCurrencyId.addActionListener(this);
        jbExchangeRate.addActionListener(this);
        jbComputeTotal.addActionListener(this);
        jbPrepayments.addActionListener(this);
        jbEntryNew.addActionListener(this);
        jbEntryEdit.addActionListener(this);
        jbEntryDelete.addActionListener(this);
        jbEntryDiscountRetailChain.addActionListener(this);
        jbEntryImportFromDps.addActionListener(this);
        jbEntryWizard.addActionListener(this);
        jbEntryViewLinks.addActionListener(this);
        jbTaxRegionId.addActionListener(this);
        jbNotesNew.addActionListener(this);
        jbNotesEdit.addActionListener(this);
        jbNotesDelete.addActionListener(this);
        jbSystemNotes.addActionListener(this);
        jbDateDocDelivery_n.addActionListener(this);
        jbDateDocLapsing_n.addActionListener(this);
        jbSalesAgent.addActionListener(this);
        jbSalesSupervisor.addActionListener(this);
        jbFkIncotermId.addActionListener(this);
        jbFkModeOfTransportationTypeId.addActionListener(this);
        jbFkCarrierId_n.addActionListener(this);
        jbFkVehicleId_n.addActionListener(this);
        jbFkProductionOrderId_n.addActionListener(this);
        jbFileXml.addActionListener(this);
        jbDeleteFileXml.addActionListener(this);
        jtbEntryFilter.addActionListener(this);
        jtbNotesFilter.addActionListener(this);

        // Focus listeners:

        jftDate.addFocusListener(this);
        jftDateDoc.addFocusListener(this);
        jftDateStartCredit.addFocusListener(this);
        jtfDaysOfCredit.addFocusListener(this);
        jtfExchangeRate.addFocusListener(this);
        jtfDiscountDocPercentage.addFocusListener(this);

        // Item listeners

        jckDateDoc.addItemListener(this);
        jckDateStartCredit.addItemListener(this);
        jckRecordUser.addItemListener(this);
        jckPayments.addItemListener(this);
        jckShipments.addItemListener(this);
        jckIsDiscountDocApplying.addItemListener(this);
        jckIsDiscountDocPercentage.addItemListener(this);
        jcbNumberSeries.addItemListener(this);
        jcbFkPaymentTypeId.addItemListener(this);
        jcbFkPaymentSystemTypeId.addItemListener(this);
        jcbFkLanguageId.addItemListener(this);
        jcbFkDpsNatureId.addItemListener(this);
        jcbFkCurrencyId.addItemListener(this);
        jcbFkIncotermId.addItemListener(this);
        jcbFkCarrierTypeId.addItemListener(this);
        jcbCfdAddendaSubtypeId.addItemListener(this);

        SFormUtilities.createActionMap(rootPane, this, "publicActionDependentNew", "dependentNew", KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionDependentEdit", "dependentEdit", KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionDependentDelete", "dependentDelete", KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionDependentFilter", "dependentFilter", KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryDiscountRetailChain", "entryDiscountRetailChain", KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryImportFromDps", "entryImport", KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryWizard", "entryWizard", KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryViewLinks", "entryViewLinks", KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionCancel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);
        SFormUtilities.putActionMap(moPanelRecord, actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        boolean bContinue = true;
        boolean bExists = false;
        SDataDpsNotes dpsNotes = null;
        SDataDpsNotes dpsNotesAux = null;
        mbImportation = false;

        if (mbFirstTime) {
            mbFirstTime = false;

            if (!moDps.getIsRegistryNew()) {
                if (!moDps.getIsRecordAutomatic() && moRecordUserLock == null) {
                    miClient.showMsgBoxWarning("La póliza contable manual no debe estar siendo utilizada por otro usuario.");
                    actionCancel();     // if lock for manual record could not be gained, exit form
                }
                else {
                    if (moBizPartner != null && (mbIsDoc || mbIsAdj) && (!mbIsSales || isCfdNeeded() || isCfdiNeeded())) {
                        jTabbedPane.setEnabledAt(3, true);
                    }
                    else {
                        jTabbedPane.setEnabledAt(3, false);
                        renderAddendaData(false);
                    }

                    jbCancel.requestFocus();
                }
            }
            else {
                if (miClient.getSessionXXX().getCurrentCompanyBranchId() == 0) {
                    // A company branch must be selected:

                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_SESSION_BRANCH);
                    mbFormSettingsOk = false;
                    actionCancel();
                }
                else if (mbIsNumberSeriesRequired && !mbIsNumberSeriesAvailable) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_CFG_DNS);
                    mbFormSettingsOk = false;
                    actionCancel();
                }
                else {
                    if (moParamDpsSource == null) {
                        // New document from scratch:

                        if (moDpsType == null) {
                            actionDpsType();
                            if (moDpsType == null) {
                                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDpsType.getText() + "'.");
                                mbFormSettingsOk = false;
                                actionCancel();
                            }
                        }

                        if (moBizPartner == null) {
                            actionBizPartner();
                            if (moBizPartner == null) {
                                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlBizPartner.getText() + "'.");
                                mbFormSettingsOk = false;
                                actionCancel();
                            }
                            else {
                                renderLastPaymentSettings(moBizPartner.getPkBizPartnerId());
                                if (isBizPartnerBlocked(moBizPartner.getPkBizPartnerId(), manDpsClassKey[0])) {
                                    miClient.showMsgBoxWarning(SLibConstants.MSG_INF_BP_BLOCKED);
                                    mbFormSettingsOk = bContinue = false;
                                    actionCancel();
                                }
                                else {
                                    
                                }
                            }
                        }
                    }
                    else {
                        // New document from a previous document:

                        if (isBizPartnerBlocked(moParamDpsSource.getFkBizPartnerId_r(), moParamDpsSource.getFkDpsCategoryId())) {
                            miClient.showMsgBoxWarning(SLibConstants.MSG_INF_BP_BLOCKED);
                            mbFormSettingsOk = bContinue = false;
                            moParamDpsSource = null;
                            actionCancel();
                        }

                        try {
                            SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moParamDpsSource.getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);

                            if (!STrnDpsUtilities.isDpsAuthorized(miClient, dps)) {
                                mbFormSettingsOk = bContinue = false;
                                actionCancel();
                            }
                            else {
                                mbImportation = true;
                                
                                dps = createNewDps(dps);
                                dps.getDbmsDpsEntries().clear();

                                for (SDataDpsNotes notes : dps.getDbmsDpsNotes()) {
                                    notes.setIsRegistryEdited(true);    // force original document notes to be attached to new document even if they are not edited
                                    notes.setPkNotesId(0);
                                }

                                setRegistry(dps);
                                actionEdit();

                                if (manParamAdjustmentSubtypeKey != null) {
                                    SFormUtilities.locateComboBoxItem(jcbAdjustmentSubtypeId, manParamAdjustmentSubtypeKey);
                                }

                                actionEntryImportFromDps(moParamDpsSource);
                            }
                        }
                        catch (Exception e) {
                            SLibUtilities.renderException(this, e);
                        }
                        finally {
                            moParamDpsSource = null;                // no longer needed
                            manParamAdjustmentSubtypeKey = null;    // no longer needed
                        }
                    }
                    
                    if (moBizPartnerCategory != null) {
                        if (!isCreditRiskOk(moBizPartnerCategory.getEffectiveRiskTypeId(), true)) {
                            mbFormSettingsOk = bContinue = false;
                            actionCancel();
                        }
                    }

                    if (moBizPartner != null && (mbIsDoc || mbIsAdj) && (!mbIsSales || isCfdNeeded() || isCfdiNeeded())) {
                        jTabbedPane.setEnabledAt(3, true);
                        renderAddendaData(true);
                    }
                    else {
                        jTabbedPane.setEnabledAt(3, false);
                        renderAddendaData(false);
                    }

                    if (bContinue) {
                        obtainNextNumber();
                        updateDpsWithDocSettings();
                        updateDateForOrderPrevious();
                        if (jftDate.isEditable()) {
                            jftDate.requestFocus();
                        }
                        else {
                            jtfNumber.requestFocus();
                        }
                    }

                    // Render system notes:
                    for (SDataDpsNotesRow row : STrnUtilities.getSystemNotes(miClient, moDpsType.getPkDpsCategoryId(), moDpsType.getPkDpsClassId(), moDpsType.getPkDpsTypeId(), moDps.getFkCurrencyId())) {
                        bExists = false;
                        dpsNotes = (SDataDpsNotes) row.getData();
                        for (STableRow note: moPaneGridNotes.getGridRows()){
                            dpsNotesAux = (SDataDpsNotes) note.getData();
                            if (dpsNotes.getNotes().equals(dpsNotesAux.getNotes())) {
                                bExists = true;
                                break;
                            }
                        }
                        if (!bExists){
                            moPaneGridNotes.addTableRow(row);
                        }
                    }
                    moPaneGridNotes.renderTableRows();
                    moPaneGridNotes.setTableRowSelection(0);

                }
            }
        }
    }
    
    private void setEnebleCfdFields(boolean enable) {
        jcbCfdUseId.setEnabled(enable);
        jtfCfdConfirmationNum.setEditable(enable);
    }

    private boolean isCfdNeeded() {
        boolean isNeeded = false;

        if (mbIsSales && (mbIsDoc || mbIsAdj)) {
            if (moDps.getIsRegistryNew()) {
                moDps.setApproveYear(((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[2]);
                moDps.setApproveNumber(((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[3]);
            }

            isNeeded = miClient.getSessionXXX().getParamsCompany().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFD &&
                moDps.getApproveYear() != 0 && moDps.getApproveNumber() != 0;
        }

        return isNeeded;
    }

    private boolean isCfdiNeeded() {
        boolean isNeeded = false;

        if (mbIsSales && (mbIsDoc || mbIsAdj)) {
            if (moDps.getIsRegistryNew()) {
                moDps.setApproveYear(((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[2]);
                moDps.setApproveNumber(((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[3]);
            }

            isNeeded = (miClient.getSessionXXX().getParamsCompany().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 ||
                    miClient.getSessionXXX().getParamsCompany().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) &&
                moDps.getApproveYear() != 0 && moDps.getApproveNumber() != 0;
        }

        return isNeeded;
    }

    private boolean isNumberSeriesBySystem() {
        return mnFormType == SDataConstantsSys.TRNS_CT_DPS_SAL ||
            SLibUtilities.compareKeys(manDpsClassKey, SDataConstantsSys.TRNS_CL_DPS_PUR_EST) ||
            SLibUtilities.compareKeys(manDpsClassKey, SDataConstantsSys.TRNS_CL_DPS_PUR_ORD);
    }

    private boolean isDocumentApplyCreditRiskPurchases() {
        return
            (SLibUtilities.compareKeys(moDpsType.getPrimaryKey(), SDataConstantsSys.TRNU_TP_DPS_PUR_EST) &&
            miClient.getSessionXXX().getParamsErp().getIsPurchasesCreditContract())  ||
            (SLibUtilities.compareKeys(moDpsType.getPrimaryKey(), SDataConstantsSys.TRNU_TP_DPS_PUR_CON)&&
            miClient.getSessionXXX().getParamsErp().getIsPurchasesCreditContract()) ||
            (SLibUtilities.compareKeys(moDpsType.getPrimaryKey(), SDataConstantsSys.TRNU_TP_DPS_PUR_ORD) &&
            miClient.getSessionXXX().getParamsErp().getIsPurchasesCreditOrder()) ||
            (SLibUtilities.compareKeys(moDpsType.getPrimaryKey(), SDataConstantsSys.TRNU_TP_DPS_PUR_INV) &&
            miClient.getSessionXXX().getParamsErp().getIsPurchasesCreditInvoice());
    }

    private boolean isDocumentApplyCreditRiskSales() {
        return
            (SLibUtilities.compareKeys(moDpsType.getPrimaryKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_EST) &&
            miClient.getSessionXXX().getParamsErp().getIsSalesCreditContract()) ||
            (SLibUtilities.compareKeys(moDpsType.getPrimaryKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_CON) &&
            miClient.getSessionXXX().getParamsErp().getIsSalesCreditContract()) ||
            (SLibUtilities.compareKeys(moDpsType.getPrimaryKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_ORD) &&
            miClient.getSessionXXX().getParamsErp().getIsSalesCreditOrder()) ||
            (SLibUtilities.compareKeys(moDpsType.getPrimaryKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_INV) &&
            miClient.getSessionXXX().getParamsErp().getIsSalesCreditInvoice());
}
    
    private boolean isFunctionalAreasApply() {
        return !mbIsSales && mbIsOrd && miClient.getSessionXXX().getParamsCompany().getIsFunctionalAreas();
    }

    private boolean isCreditRiskOk(final int riskType, final boolean isDocBeingOpened) {
        boolean validateCreditLimit = false;
        boolean validateExpiredDocs = false;
        boolean canOmitCreditLimit = false;
        boolean canOmitExpiredDocs = false;
        boolean isCreditRiskOk = true;

        if (isDocumentApplyCreditRiskPurchases() || isDocumentApplyCreditRiskSales()) {
            if (riskType == SModSysConsts.BPSS_RISK_D_BLK) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_INF_BP_BLOCKED);
                isCreditRiskOk = false;
            }
            else if (riskType == SModSysConsts.BPSS_RISK_E_TRL) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_INF_BP_TRIAL);
                isCreditRiskOk = false;
            }
            else {
                validateCreditLimit = riskType == SModSysConsts.BPSS_RISK_B_RSK_M || riskType == SModSysConsts.BPSS_RISK_C_RSK_H;
                validateExpiredDocs = riskType == SModSysConsts.BPSS_RISK_A_RSK_L || riskType == SModSysConsts.BPSS_RISK_B_RSK_M || riskType == SModSysConsts.BPSS_RISK_C_RSK_H;
                canOmitCreditLimit = riskType == SModSysConsts.BPSS_RISK_B_RSK_M;
                canOmitExpiredDocs = riskType == SModSysConsts.BPSS_RISK_A_RSK_L || riskType == SModSysConsts.BPSS_RISK_B_RSK_M;

                if (validateCreditLimit && !checkBizPartnerCreditLimitOk(canOmitCreditLimit, isDocBeingOpened)) {
                    isCreditRiskOk = false;
                }
                else if (validateExpiredDocs && !checkBizPartnerExpiredDocsOk(canOmitExpiredDocs, isDocBeingOpened)) {
                    isCreditRiskOk = false;
                }
            }
        }

        return isCreditRiskOk;
    }

    /**
     * Business Partner Blocking applies only to Orders and Purchases and Sales Documents.
     */
    private boolean isBizPartnerBlocked(int idBizPartner, int idDpsCategory) {
        boolean blocked = false;

        if (mbIsOrd || mbIsDoc) {
            try {
                blocked = SDataUtilities.obtainIsBizPartnerBlocked(miClient, idBizPartner, idDpsCategory == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);
            }
            catch (Exception e) {
                SLibUtilities.printOutException(this, e);
            }
        }

        return blocked;
    }


    private boolean isUpdateEntriesAllNeeded() {
        boolean isUpdateNeeded = false;

        if (mdOrigExchangeRate != moFieldExchangeRate.getDouble() || mdOrigDiscountDocPercentage != moFieldDiscountDocPercentage.getDouble() || mbOrigIsDiscountDocApplying != moFieldIsDiscountDocApplying.getBoolean()) {
            isUpdateNeeded = true;
        }

        return isUpdateNeeded;
    }

    private void renderEntries() {
        int sortingPosition = 0;
        SDataDpsEntry entry = null;

        moPaneGridEntries.renderTableRows();
        for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
            entry = (SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData();

            if (entry.getIsDeleted() || entry.getFkDpsEntryTypeId() == SDataConstantsSys.TRNS_TP_DPS_ETY_VIRT) {
                entry.setSortingPosition(0);
            }
            else {
                entry.setSortingPosition(++sortingPosition);
            }

            entry.setIsRegistryEdited(true);
            moPaneGridEntries.getTableRow(i).prepareTableRow();
        }
    }
    
    private void calculateTotal() {
        int i = 0;
        double quantity = 0;
        SDataDpsEntry entry = null;

        moDps.setFkLanguajeId(moFieldFkLanguajeId.getKeyAsIntArray()[0]);
        moDps.setFkCurrencyId(moFieldFkCurrencyId.getKeyAsIntArray()[0]);
        moDps.setExchangeRateSystem(moFieldExchangeRateSystem.getDouble());
        moDps.setExchangeRate(moFieldExchangeRate.getDouble());

        moDps.setIsDiscountDocApplying(moFieldIsDiscountDocApplying.getBoolean());
        moDps.setIsDiscountDocPercentage(moFieldIsDiscountDocPercentage.getBoolean());
        moDps.setDiscountDocPercentage(moFieldDiscountDocPercentage.getDouble());

        // In order to calculate document's value, all entries must be in moDps object:

        moDps.getDbmsDpsEntries().clear();
        for (i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
            entry = (SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData();
            quantity += entry.getOriginalQuantity();
            moDps.getDbmsDpsEntries().add(entry);
        }

        // Calculate and render document's value:

        jtfQuantityTotal.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(quantity));

        try {
            moDps.calculateTotal(miClient);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        
        renderDpsValue();
    }

    private void updateDpsWithDocSettings() {
        moDps.setDate(moFieldDate.getDate());
        moDps.setFkCurrencyId(moFieldFkCurrencyId.getKeyAsIntArray()[0]);
        moDps.setFkLanguajeId(moFieldFkLanguajeId.getKeyAsIntArray()[0]);
        moDps.setExchangeRate(moFieldExchangeRate.getDouble());
        moDps.setIsDiscountDocApplying(moFieldIsDiscountDocApplying.getBoolean());
        moDps.setIsDiscountDocPercentage(moFieldIsDiscountDocPercentage.getBoolean());
        moDps.setDiscountDocPercentage(moFieldDiscountDocPercentage.getDouble());

        if (moDpsType == null) {
            moDps.setFkDpsCategoryId(0);
            moDps.setFkDpsClassId(0);
            moDps.setFkDpsTypeId(0);
        }
        else {
            moDps.setFkDpsCategoryId(moDpsType.getPkDpsCategoryId());
            moDps.setFkDpsClassId(moDpsType.getPkDpsClassId());
            moDps.setFkDpsTypeId(moDpsType.getPkDpsTypeId());
        }
    }

    private void updateDpsWithBizPartnerSettings() {
        int id = -1;
        int[] key = null;
        double rate = 0;

        mbUpdatingForm = true;

        // Update in moDps object only the fields that cannot be edited in GUI:

        moDps.setFkBizPartnerId_r(moBizPartner.getPkBizPartnerId());
        moDps.setFkBizPartnerBranchId(moBizPartnerBranch.getPkBizPartnerBranchId());

        if (mnFormType == SDataConstantsSys.TRNS_CT_DPS_PUR) {
            moDps.setFkTaxIdentityEmisorTypeId(moBizPartner.getFkTaxIdentityId());
            moDps.setFkTaxIdentityReceptorTypeId(miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFkTaxIdentityId());

            mnSalesAgentBizPartnerId_n = 0;
            mnSalesSupervisorBizPartnerId_n = 0;
        }
        else {
            moDps.setFkTaxIdentityEmisorTypeId(miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFkTaxIdentityId());
            moDps.setFkTaxIdentityReceptorTypeId(moBizPartner.getFkTaxIdentityId());

            key = (int[]) moBizPartner.getSalesAgentKey((int[]) moBizPartnerBranch.getPrimaryKey());
            mnSalesAgentBizPartnerId_n = key == null ? 0 : key[0];

            key = (int[]) moBizPartner.getSalesSupervisorKey((int[]) moBizPartnerBranch.getPrimaryKey());
            mnSalesSupervisorBizPartnerId_n = key == null ? 0 : key[0];
        }

        if (moDps.getFkSalesAgentId_n() == 0) {
            mnSalesAgentId_n = mnSalesAgentBizPartnerId_n;
            moDps.setFkSalesAgentId_n(mnSalesAgentId_n);
        }
        moDps.setFkSalesAgentBizPartnerId_n(mnSalesAgentBizPartnerId_n);

        if (moDps.getFkSalesSupervisorId_n() == 0) {
            mnSalesSupervisorId_n = mnSalesSupervisorBizPartnerId_n;
            moDps.setFkSalesSupervisorId_n(mnSalesSupervisorId_n);
        }
        moDps.setFkSalesSupervisorBizPartnerId_n(mnSalesSupervisorBizPartnerId_n);

        renderSalesAgentBizPartner(moDps.getFkSalesAgentBizPartnerId_n() == 0 ? null : new int[] { moDps.getFkSalesAgentBizPartnerId_n() });
        renderSalesAgent(moDps.getFkSalesAgentId_n() == 0 ? null : new int[] { moDps.getFkSalesAgentId_n() });
        renderSalesSupervisorBizPartner(moDps.getFkSalesSupervisorBizPartnerId_n() == 0 ? null : new int[] { moDps.getFkSalesSupervisorBizPartnerId_n() });
        renderSalesSupervisor(moDps.getFkSalesSupervisorId_n() == 0 ? null : new int[] { moDps.getFkSalesSupervisorId_n() });

        // Document's payment type:

        if (moDps.getFkPaymentTypeId() == 0) {
            if (mbIsAdj || moBizPartnerCategory.getEffectiveCreditTypeId() == SDataConstantsSys.BPSS_TP_CRED_CRED_NO) {
                moFieldFkPaymentTypeId.setFieldValue(new int[] { SDataConstantsSys.TRNS_TP_PAY_CASH });
            }
            else {
                moFieldFkPaymentTypeId.setFieldValue(new int[] { SDataConstantsSys.TRNS_TP_PAY_CREDIT });
            }
        }

        // Document's language:

        if (jcbFkLanguageId.getSelectedIndex() <= 0) {
            id = moBizPartnerCategory.getFkLanguageId_n();
            if (id == SLibConstants.UNDEFINED) {
                moFieldFkLanguajeId.setFieldValue(new int[] { miClient.getSessionXXX().getParamsErp().getFkLanguageId() });
            }
            else {
                moFieldFkLanguajeId.setFieldValue(new int[] { id });
            }
        }

        // Document's currency:

        if (jcbFkCurrencyId.getSelectedIndex() <= 0) {
            id = moBizPartnerCategory.getFkCurrencyId_n();

            if (id == SLibConstants.UNDEFINED) {
                moFieldFkCurrencyId.setFieldValue(new int[] { miClient.getSessionXXX().getParamsErp().getFkCurrencyId() });
            }
            else {
                moFieldFkCurrencyId.setFieldValue(new int[] { id });

                try {
                    rate = SDataUtilities.obtainExchangeRate(miClient, id, moDps.getDate());
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }

                if (rate != 0) {
                    moFieldExchangeRateSystem.setFieldValue(rate);
                    moFieldExchangeRate.setFieldValue(rate);
                }
            }
        }

        itemChangeFkPaymentTypeId(moDps.getFkPaymentTypeId() == SLibConsts.UNDEFINED || mbIsAdj);
        itemChangeFkPaymentSystemTypeId();
        itemChangeFkLanguageId();
        itemChangeFkCurrencyId(false);

        mbUpdatingForm = false;
    }

    private void updateDateForOrderPrevious() {
        if (mbIsOrd) {
            if (moDps.getIsRegistryNew() && !mbHasRightOrderDelay) {
                moFieldDate.setFieldValue(miClient.getSessionXXX().getSystemDate());
                moFieldDateDoc.setFieldValue(miClient.getSessionXXX().getSystemDate());
                moFieldDateStartCredit.setFieldValue(miClient.getSessionXXX().getSystemDate());
            }
            jftDate.setEditable(mbHasRightOrderDelay);
            jftDate.setFocusable(mbHasRightOrderDelay);
            jbDate.setEnabled(mbHasRightOrderDelay);
            jbDate.setFocusable(mbHasRightOrderDelay);
        }
    }
    
    private void obtainNextNumber() {
        int numNew = 0;
        int numMin = 0;
        int numMax = 0;
        int numNext = 0;

        if (moFieldNumberSeries.getDataType() == SLibConstants.DATA_TYPE_KEY) {
            try {
                numMin = ((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[0];
                numMax = ((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[1];
                numNext = SDataUtilities.obtainNextNumberForDps(miClient, moFieldNumberSeries.getString(), moDpsType.getPrimaryKey());
                if (numNext <= numMin) {
                    numNew = numMin;
                }
                else if (numMax == -1 || numNext <= numMax) {
                    numNew = numNext;
                }
                else {
                    throw new Exception("El rango de folios válido para la serie '" + moFieldNumberSeries.getString() + "' es del " + numMin + " al " + numMax + ".");
                }

                moFieldNumber.setFieldValue("" + numNew);
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
    }

    private double obtainBizPartnerBalance() {
        int cat = SLibConstants.UNDEFINED;
        Vector<Object> in = null;
        Vector<Object> out = null;

        if (manDpsClassKey[0] == SDataConstantsSys.TRNS_CT_DPS_PUR) {
            cat = SDataConstantsSys.BPSS_CT_BP_SUP;
        }
        else {
            cat = SDataConstantsSys.BPSS_CT_BP_CUS;
        }

        in = new Vector<Object>();
        in.add(moBizPartner.getPkBizPartnerId());
        in.add(cat);
        in.add(moDps.getPkYearId());
        in.add(moDps.getPkDocId());
        out = SDataUtilities.callProcedure(miClient, SProcConstants.FIN_GET_BP_BAL, in, SLibConstants.EXEC_MODE_VERBOSE);
        
        return ((Double) out.get(0)).doubleValue();
    }

    private sa.lib.srv.SSrvLock gainRecordUserLock(java.lang.Object pk, long timeout) {
        SSrvLock lock = null;

        try {
            lock = SSrvUtils.gainLock(miClient.getSession(), miClient.getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.FIN_REC, pk, timeout);
        }
        catch (Exception e) {
            lock = null;
            miClient.showMsgBoxWarning("No fue posible obtener el acceso exclusivo al registro '" + jckRecordUser.getText() + "'.\n" + e);
        }

        return lock;
    }

    private void releaseRecordUserLock() {
        if (moRecordUserLock != null) {
            try {
                SSrvUtils.releaseLock(miClient.getSession(), moRecordUserLock);
                moRecordUserLock = null;
            }
            catch (Exception e) {
                miClient.showMsgBoxWarning("No fue posible liberar el acceso exclusivo del registro '" + jckRecordUser.getText() + "'.\n" + e);
            }
        }
    }

    private boolean readRecordUser(Object pk) {
        boolean error = true;
        SDataRecord record = null;
        SSrvLock lock = null;

        releaseRecordUserLock();
        moRecordUser = null;

        if (pk != null) {
            record = (SDataRecord) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_REC, pk, SLibConstants.EXEC_MODE_VERBOSE);
            lock = gainRecordUserLock(pk, record.getRegistryTimeout());

            if (lock != null) {
                moRecordUser = record;
                moRecordUserLock = lock;
                error = false;
            }
        }

        return !error;
    }

    private void adecuatePaymentTypeSettings() {
        if (moBizPartnerCategory == null || moBizPartnerCategory.getEffectiveCreditTypeId() == SDataConstantsSys.BPSS_TP_CRED_CRED_NO) {
            jcbFkPaymentTypeId.setEnabled(false);
        }
        else {
            jcbFkPaymentTypeId.setEnabled(!mbIsAdj);
        }
    }

    private erp.mtrn.data.SDataDps createNewDps(erp.mtrn.data.SDataDps dpsModel) {
        SDataDps dps = dpsModel != null ? dpsModel : new SDataDps();

        dps.setPkYearId(miClient.getSessionXXX().getWorkingYear());
        dps.setPkDocId(0);
        dps.setDate(miClient.getSessionXXX().getWorkingDate());
        dps.setDateDoc(miClient.getSessionXXX().getWorkingDate());
        dps.setDateStartCredit(miClient.getSessionXXX().getWorkingDate());
        dps.setNumberSeries("");
        dps.setNumber("");
        dps.setApproveYear(0);
        dps.setApproveNumber(0);
        dps.setIsRegistryNew(true);
        dps.setIsLinked(false);
        dps.setIsClosed(false);
        dps.setIsClosedCommissions(false);
        dps.setIsAudited(false);
        dps.setIsAuthorized(false);
        dps.setIsSystem(false);
        dps.setIsDeleted(false);
        dps.setFkDpsStatusId(SDataConstantsSys.TRNS_ST_DPS_EMITED);
        dps.setFkDpsValidityStatusId(SDataConstantsSys.TRNS_ST_DPS_VAL_EFF);
        dps.setFkDpsAuthorizationStatusId(SDataConstantsSys.TRNS_ST_DPS_AUTHORN_NA);
        dps.setFkDpsAnnulationTypeId(SModSysConsts.TRNU_TP_DPS_ANN_NA);
        dps.setFkUserLinkedId(SDataConstantsSys.USRX_USER_NA);
        dps.setFkUserClosedId(SDataConstantsSys.USRX_USER_NA);
        dps.setFkUserClosedCommissionsId(SDataConstantsSys.USRX_USER_NA);
        dps.setFkUserShippedId(SDataConstantsSys.USRX_USER_NA);
        dps.setFkUserAuditedId(SDataConstantsSys.USRX_USER_NA);
        dps.setFkUserAuthorizedId(SDataConstantsSys.USRX_USER_NA);
        dps.setFkCompanyBranchId(miClient.getSessionXXX().getCurrentCompanyBranchId());

        dps.resetRecord();

        if (mbIsAdj) {
            dps.setFkPaymentTypeId(SDataConstantsSys.TRNS_TP_PAY_CASH);
            dps.setFkPaymentSystemTypeId(SDataConstantsSys.TRNU_TP_PAY_SYS_NA);
            dps.setDateDocDelivery_n(null);
            dps.setDateDocLapsing_n(null);
            dps.setDateShipment_n(null);
            dps.setDateDelivery_n(null);
            dps.setShipments(0);
            dps.setFkIncotermId(SModSysConsts.LOGS_INC_NA);
            dps.setFkSpotSourceId_n(0);
            dps.setFkSpotDestinyId_n(0);
            dps.setFkModeOfTransportationTypeId(SModSysConsts.LOGS_TP_MOT_NA);
            dps.setFkCarrierTypeId(SModSysConsts.LOGS_TP_CAR_NA);
            dps.setFkCarrierId_n(0);
            dps.setFkVehicleTypeId_n(0);
            dps.setFkVehicleId_n(0);
            dps.setDriver("");
            dps.setPlate("");
            dps.setTicket("");
        }

        if (dpsModel != null) {
            if (dps.getDateDocDelivery_n() != null && dps.getDateDocDelivery_n().before(dps.getDate())) {
                dps.setDateDocDelivery_n(dps.getDate());   // document delivery date
            }

            for (SDataDpsEntry entry : dps.getDbmsDpsEntries()) {
                entry.setPkEntryId(0);
                entry.setIsRegistryNew(true);   // force entries to be treated as new
            }
        }

        return dps;
    }

    private void renderCatalogForCfd() {
        try {
            for (SFormComponentItem item : SCatalogXmlUtils.getComponentItems((SGuiClient) miClient, SDataConstantsSys.TRNS_CFD_CAT_CFD_USE, moFieldDate.getDate() == null ? miClient.getSessionXXX().getWorkingDate() : moFieldDate.getDate())) {
                jcbCfdUseId.addItem(item);
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }
    
    private void renderBasicSettings() {
        jtfCurrencySystemKeyRo.setText(miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
    }

    private void renderDpsType() {
        if (moDpsType == null) {
            jtfDpsTypeRo.setText("");
            jtfDpsTypeRo.setToolTipText(null);
        }
        else {
            jtfDpsTypeRo.setText(moDpsType.getDpsType());
            jtfDpsTypeRo.setToolTipText(jtfDpsTypeRo.getText());
            jtfDpsTypeRo.setCaretPosition(0);
        }
    }

    private void renderDpsValue() {
        double quantity = 0;
        DecimalFormat format = miClient.getSessionXXX().getFormatters().getDecimalsValueFormat();

        if (moFieldFkCurrencyId.getKeyAsIntArray()[0] == miClient.getSessionXXX().getParamsErp().getFkCurrencyId()) {
            jtfSubtotalProvisional_rRo.setText("");
            jtfDiscountDoc_rRo.setText("");
            jtfSubtotal_rRo.setText("");
            jtfTaxCharged_rRo.setText("");
            jtfTaxRetained_rRo.setText("");
            jtfTotal_rRo.setText("");
        }
        else {
            jtfSubtotalProvisional_rRo.setText(format.format(moDps.getSubtotalProvisional_r()));
            jtfDiscountDoc_rRo.setText(format.format(moDps.getDiscountDoc_r()));
            jtfSubtotal_rRo.setText(format.format(moDps.getSubtotal_r()));
            jtfTaxCharged_rRo.setText(format.format(moDps.getTaxCharged_r()));
            jtfTaxRetained_rRo.setText(format.format(moDps.getTaxRetained_r()));
            jtfTotal_rRo.setText(format.format(moDps.getTotal_r()));
        }

        jtfSubtotalProvisionalCy_rRo.setText(format.format(moDps.getSubtotalProvisionalCy_r()));
        jtfDiscountDocCy_rRo.setText(format.format(moDps.getDiscountDocCy_r()));
        jtfSubtotalCy_rRo.setText(format.format(moDps.getSubtotalCy_r()));
        jtfTaxChargedCy_rRo.setText(format.format(moDps.getTaxChargedCy_r()));
        jtfTaxRetainedCy_rRo.setText(format.format(moDps.getTaxRetainedCy_r()));
        jtfTotalCy_rRo.setText(format.format(moDps.getTotalCy_r()));

        // Calculate document's total quantity:

        for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
            quantity += ((SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData()).getOriginalQuantity();
        }

        jtfQuantityTotal.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(quantity));
    }

    private void renderDpsStatus() {
        jtfCompanyBranchRo.setText(moDps.getIsRegistryNew() ?
            miClient.getSessionXXX().getCurrentCompanyBranchName() :
            SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.BPSU_BPB, new int[] { moDps.getFkCompanyBranchId() }));
        jtfCompanyBranchRo.setToolTipText(jtfCompanyBranchRo.getText());
        jtfCompanyBranchRo.setCaretPosition(0);

        jtfCompanyBranchCodeRo.setText(moDps.getIsRegistryNew() ?
            (miClient.getSessionXXX().getCurrentCompanyBranch() == null ? "" : miClient.getSessionXXX().getCurrentCompanyBranch().getCode()) :
            SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.BPSU_BPB, new int[] { moDps.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
        jtfCompanyBranchCodeRo.setToolTipText(jtfCompanyBranchCodeRo.getText());
        jtfCompanyBranchCodeRo.setCaretPosition(0);

        jtfFkDpsStatusRo.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_ST_DPS, new int[] { moDps.getFkDpsStatusId() }));
        jtfFkDpsStatusRo.setCaretPosition(0);
        jtfFkDpsStatusValidityRo.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_ST_DPS_VAL, new int[] { moDps.getFkDpsValidityStatusId() }));
        jtfFkDpsStatusValidityRo.setCaretPosition(0);
        jtfFkDpsStatusAuthorizationRo.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_ST_DPS_AUTHORN, new int[] { moDps.getFkDpsAuthorizationStatusId() }));
        jtfFkDpsStatusAuthorizationRo.setCaretPosition(0);
    }

    private void renderRecordManual() {
        if (moRecordUser == null) {
            jtfRecordManualDateRo.setText("");
            jtfRecordManualBranchRo.setText("");
            jtfRecordManualNumberRo.setText("");
            jbRecordManualView.setEnabled(false);
        }
        else {
            jtfRecordManualDateRo.setText(SLibUtils.DateFormatDate.format(moRecordUser.getDate()));
            jtfRecordManualBranchRo.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.BPSU_BPB, new int[] { moRecordUser.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
            jtfRecordManualNumberRo.setText(moRecordUser.getPkRecordTypeId() + "-" + moRecordUser.getPkNumberId());
            jbRecordManualView.setEnabled(jckRecordUser.isSelected());
        }
    }

    private void renderRecordAutomatic() {
        moPanelRecord.setRecordKey(moDps.getDbmsRecordKey());
    }

    private void renderBizPartner() {
        String[] address = null;
        int nFkRecAddressFormatTypeId_n = 0;

        jtfBizPartnerRo.setText("");
        jtfBizPartnerBranchRo.setText("");
        jtfBizPartnerBranchAddressMain01Ro.setText("");
        jtfBizPartnerBranchAddressMain02Ro.setText("");
        jtfBizPartnerBranchAddress01Ro.setText("");
        jtfBizPartnerBranchAddress02Ro.setText("");

        jtfBizPartnerRo.setToolTipText(null);
        jtfBizPartnerBranchRo.setToolTipText(null);
        jtfBizPartnerBranchAddressMain01Ro.setToolTipText(null);
        jtfBizPartnerBranchAddressMain02Ro.setToolTipText(null);
        jtfBizPartnerBranchAddress01Ro.setToolTipText(null);
        jtfBizPartnerBranchAddress02Ro.setToolTipText(null);

        SFormUtilities.locateComboBoxItem(jcbTaxRegionId, new int[] { 0 });

        if (moBizPartner != null) {
            jtfBizPartnerRo.setText(moBizPartner.getBizPartner());
            jtfBizPartnerRo.setToolTipText(jtfBizPartnerRo.getText());
            jtfBizPartnerRo.setCaretPosition(0);

            SFormUtilities.locateComboBoxItem(jcbTaxRegionId, new int[] { moBizPartnerBranch.getFkTaxRegionId_n() != 0 ? moBizPartnerBranch.getFkTaxRegionId_n() : miClient.getSessionXXX().getParamsCompany().getFkDefaultTaxRegionId_n() });
        }

        if (moBizPartnerBranch != null) {
            jtfBizPartnerBranchRo.setText(moBizPartnerBranch.getBizPartnerBranch());
            jtfBizPartnerBranchRo.setToolTipText(jtfBizPartnerBranchRo.getText());
            jtfBizPartnerBranchRo.setCaretPosition(0);
        }

        nFkRecAddressFormatTypeId_n = miClient.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n();

        if (moBizPartnerBranchAddressMain != null) {
            address = moBizPartnerBranchAddressMain.obtainAddress(nFkRecAddressFormatTypeId_n, SDataBizPartnerBranchAddress.ADDRESS_2ROWS,
                    moBizPartnerBranchAddressMain.getFkCountryId_n() != miClient.getSessionXXX().getParamsErp().getFkCountryId());

            jtfBizPartnerBranchAddressMain01Ro.setText(address[0]);
            jtfBizPartnerBranchAddressMain01Ro.setToolTipText(jtfBizPartnerBranchAddressMain01Ro.getText());
            jtfBizPartnerBranchAddressMain01Ro.setCaretPosition(0);

            jtfBizPartnerBranchAddressMain02Ro.setText(address[1]);
            jtfBizPartnerBranchAddressMain02Ro.setToolTipText(jtfBizPartnerBranchAddressMain02Ro.getText());
            jtfBizPartnerBranchAddressMain02Ro.setCaretPosition(0);

            if (moBizPartnerBranchAddress != null &&
                    !SLibUtilities.compareKeys(moBizPartnerBranchAddress.getPrimaryKey(), moBizPartnerBranchAddressMain.getPrimaryKey())) {
                address = moBizPartnerBranchAddress.obtainAddress(nFkRecAddressFormatTypeId_n, SDataBizPartnerBranchAddress.ADDRESS_2ROWS,
                        moBizPartnerBranchAddress.getFkCountryId_n() != miClient.getSessionXXX().getParamsErp().getFkCountryId());

                jtfBizPartnerBranchAddress01Ro.setText(address[0]);
                jtfBizPartnerBranchAddress01Ro.setToolTipText(jtfBizPartnerBranchAddressMain01Ro.getText());
                jtfBizPartnerBranchAddress01Ro.setCaretPosition(0);

                jtfBizPartnerBranchAddress02Ro.setText(address[1]);
                jtfBizPartnerBranchAddress02Ro.setToolTipText(jtfBizPartnerBranchAddressMain02Ro.getText());
                jtfBizPartnerBranchAddress02Ro.setCaretPosition(0);
            }
        }
    }

    private void renderBizPartnerPrepaymentsBalance() {
        mnPrepaymentsItemId = SLibConsts.UNDEFINED;
        mdPrepayments = 0;
        mdPrepaymentsCy = 0;
        
        if (moBizPartner != null) {
            if (moDps.getFkDpsCategoryId() == SModSysConsts.TRNS_CT_DPS_PUR) {
                mnPrepaymentsItemId = miClient.getSessionXXX().getParamsCompany().getFkItemPrepaymentPurId_n();
            }
            else {
                mnPrepaymentsItemId = miClient.getSessionXXX().getParamsCompany().getFkItemPrepaymentSalId_n();
            }
            
            try {
                mdPrepayments = STrnUtils.getPrepaymentsBalance(
                        miClient.getSession(), moBizPartner.getPkBizPartnerId(), (int[]) moDps.getPrimaryKey(), 
                        SLibConsts.UNDEFINED);
                
                if (jcbFkCurrencyId.getSelectedIndex() <= 0) {
                    mdPrepaymentsCy = mdPrepayments;
                }
                else {
                    mdPrepaymentsCy = STrnUtils.getPrepaymentsBalance(
                            miClient.getSession(), moBizPartner.getPkBizPartnerId(), (int[]) moDps.getPrimaryKey(), 
                            moFieldFkCurrencyId.getKeyAsIntArray()[0]);
                }
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
        
        jtfPrepaymentsCy.setText(SLibUtils.getDecimalFormatAmount().format(mdPrepaymentsCy));
        jtfPrepaymentsCyCurRo.setText(jtfCurrencyKeyRo.getText());
        jlPrepaymentsWarning.setVisible(mdPrepayments > 0);
    }
    
    private void renderSalesAgentBizPartner(int[] pk) {
        if (pk == null) {
            jtfSalesAgentBizPartnerRo.setText("");
            jtfSalesAgentBizPartnerRo.setToolTipText(null);
        }
        else {
            jtfSalesAgentBizPartnerRo.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.BPSU_BP, pk));
            jtfSalesAgentBizPartnerRo.setToolTipText(jtfSalesAgentBizPartnerRo.getText());
            jtfSalesAgentBizPartnerRo.setCaretPosition(0);
        }
    }

    private void renderSalesSupervisorBizPartner(int[] pk) {
        if (pk == null) {
            jtfSalesSupervisorBizPartnerRo.setText("");
            jtfSalesSupervisorBizPartnerRo.setToolTipText(null);
        }
        else {
            jtfSalesSupervisorBizPartnerRo.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.BPSU_BP, pk));
            jtfSalesSupervisorBizPartnerRo.setToolTipText(jtfSalesSupervisorBizPartnerRo.getText());
            jtfSalesSupervisorBizPartnerRo.setCaretPosition(0);
        }
    }

    private void renderSalesAgent(int[] pk) {
        if (pk == null) {
            mnSalesAgentId_n = 0;
            jtfSalesAgentRo.setText("");
            jtfSalesAgentRo.setToolTipText(null);
        }
        else {
            mnSalesAgentId_n = pk[0];
            jtfSalesAgentRo.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.BPSU_BP, pk));
            jtfSalesAgentRo.setToolTipText(jtfSalesAgentRo.getText());
            jtfSalesAgentRo.setCaretPosition(0);
        }
    }

    private void renderSalesSupervisor(int[] pk) {
        if (pk == null) {
            mnSalesSupervisorId_n = 0;
            jtfSalesSupervisorRo.setText("");
            jtfSalesSupervisorRo.setToolTipText(null);
        }
        else {
            mnSalesSupervisorId_n = pk[0];
            jtfSalesSupervisorRo.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.BPSU_BP, pk));
            jtfSalesSupervisorRo.setToolTipText(jtfSalesSupervisorRo.getText());
            jtfSalesSupervisorRo.setCaretPosition(0);
        }
    }

    private void renderDate() {
        if (!jckDateDoc.isSelected()) {
            moFieldDateDoc.setFieldValue(moFieldDate.getDate());
        }
        if (!jckDateStartCredit.isSelected()) {
            moFieldDateStartCredit.setFieldValue(moFieldDate.getDate());
        }
    }

    private void renderDateMaturity() {
        if (moFieldDateStartCredit.getDate() != null) {
            jftDateMaturityRo.setText(SLibUtils.DateFormatDate.format(
                    SLibTimeUtilities.addDate(moFieldDateStartCredit.getDate(), 0, 0, moFieldDaysOfCredit.getInteger())));
        }
        else {
            jftDateMaturityRo.setText("");
        }
    }

    private void renderLastPaymentSettings(int idBizPartner) {
        int year = SLibTimeUtilities.digestYear(moFieldDate.getDate())[0];
        int categoryDps = mbIsSales ? SDataConstantsSys.TRNS_CT_DPS_SAL : SDataConstantsSys.TRNS_CT_DPS_PUR;
        int paymentSystem = SLibConsts.UNDEFINED;
        boolean available = false;
        String paymentAccount = "";
        String[] lastPaymentSettings = null;

        SFormUtilities.populateComboBox(miClient, jcbFkPaymentSystemTypeId, SDataConstants.TRNU_TP_PAY_SYS);
        SFormUtilities.populateComboBox(miClient, jcbPaymentAccount, SDataConstants.BPSX_BANK_ACC, new int[] { idBizPartner, year, categoryDps }); 
        jcbPaymentAccount.removeItemAt(0);

        for (int i = 0; i < jcbPaymentAccount.getItemCount(); i++) {
            if (jcbPaymentAccount.getItemAt(i).toString().equalsIgnoreCase(SCfdConsts.UNIDENTIFIED)) {
                available = true;
                break;
            }
        }

        if (!available) {
            jcbPaymentAccount.addItem(new SFormComponentItem(new int[] { SLibConsts.UNDEFINED }, SCfdConsts.UNIDENTIFIED)); // "unidentified" option must be available allways
        }

        if (moDps.getFkPaymentSystemTypeId() != SLibConstants.UNDEFINED) {
            paymentSystem = moDps.getFkPaymentSystemTypeId();
            paymentAccount = moDps.getPaymentAccount();
        }
        else {
            lastPaymentSettings = STrnUtilities.getLastPaymentSettings(miClient, idBizPartner, categoryDps, year);
            
            if (SCfdConsts.MetodoPagoIdsMap.containsKey(lastPaymentSettings[0])) {
                paymentSystem = SCfdConsts.MetodoPagoIdsMap.get(lastPaymentSettings[0]);
            }
            else {
                paymentSystem = SDataConstantsSys.TRNU_TP_PAY_SYS_NA;
            }
            
            paymentAccount = lastPaymentSettings[1];
        }
        
        moFieldFkPaymentSystemTypeId.setFieldValue(new int[] { paymentSystem });
        jcbPaymentAccount.setSelectedItem(paymentAccount);
    }

    private void renderAddendaData(boolean enable) {
        if (moBizPartner != null) {
            switch(moBizPartnerCategory.getFkCfdAddendaTypeId()) {
                case SDataConstantsSys.BPSS_TP_CFD_ADD_NA:
                    jtfFkCfdAddendaTypeId.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRN_DPS_ADD, new int[]{ SDataConstantsSys.BPSS_TP_CFD_ADD_NA }));
                    break;
                case SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA:
                    jlStore.setEnabled(enable);
                    jtfStore.setEnabled(enable);
                    jlGoodsDelivery.setEnabled(enable);
                    jtfGoodsDelivery.setEnabled(enable);
                    jlRemissionDate.setEnabled(enable);
                    jftRemissionDate.setEnabled(enable);
                    jlRemission.setEnabled(enable);
                    jtfRemission.setEnabled(enable);
                    jlBulkType.setEnabled(enable);
                    jtfBulkType.setEnabled(enable);
                    jlBulkQuantity.setEnabled(enable);
                    jtfBulkQuantity.setEnabled(enable);
                    jlSalesOrder.setEnabled(enable);
                    jtfSalesOrder.setEnabled(enable);
                    jlCfdAddendaSubtypeId.setEnabled(enable);
                    jcbCfdAddendaSubtypeId.setEnabled(enable);
                    jlNumberNoteIn.setEnabled(enable);
                    jtfNumberNoteIn.setEnabled(enable);
                    jtfFkCfdAddendaTypeId.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRN_DPS_ADD, new int[]{ SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA }));
                    
                    jcbCfdAddendaSubtypeId.removeAllItems();
                    jcbCfdAddendaSubtypeId.addItem(new SFormComponentItem(new int[] { SDataConstantsSys.BPSS_STP_CFD_ADD_SORIANA_NOR }, "Normal"));
                    jcbCfdAddendaSubtypeId.addItem(new SFormComponentItem(new int[] { SDataConstantsSys.BPSS_STP_CFD_ADD_SORIANA_EXT }, "Extemporánea"));
                    
                    SFormUtilities.locateComboBoxItem(jcbCfdAddendaSubtypeId, new int[] { (moDps.getDbmsDataAddenda() != null ? moDps.getDbmsDataAddenda().getCfdAddendaSubtype() : SDataConstantsSys.BPSS_STP_CFD_ADD_SORIANA_NOR) });
                    itemChangeCfdAddendaSubtypeId();
                    break;
                case SDataConstantsSys.BPSS_TP_CFD_ADD_LOREAL:
                    jlFolioNotaRecepcion.setEnabled(enable);
                    jtfFolioNotaRecepcion.setEnabled(enable);
                    jtfFkCfdAddendaTypeId.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRN_DPS_ADD, new int[]{ SDataConstantsSys.BPSS_TP_CFD_ADD_LOREAL }));
                    break;
                case SDataConstantsSys.BPSS_TP_CFD_ADD_BACHOCO:
                    jlBachocoSociedad.setEnabled(enable);
                    jtfBachocoSociedad.setEnabled(enable);
                    jlBachocoOrganizacion.setEnabled(enable);
                    jtfBachocoOrganizacion.setEnabled(enable);
                    jlBachocoDivision.setEnabled(enable);
                    jtfBachocoDivision.setEnabled(enable);
                    jtfFkCfdAddendaTypeId.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRN_DPS_ADD, new int[]{ SDataConstantsSys.BPSS_TP_CFD_ADD_BACHOCO }));
                    break;
                case SDataConstantsSys.BPSS_TP_CFD_ADD_MODELO:
                    jlBachocoSociedad.setEnabled(enable);
                    jtfBachocoSociedad.setEnabled(enable);
                    jlDpsDescripcion.setEnabled(enable);
                    jtfDpsDescripcion.setEnabled(enable);
                    jtfFkCfdAddendaTypeId.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRN_DPS_ADD, new int[]{ SDataConstantsSys.BPSS_TP_CFD_ADD_MODELO }));
                    break;
                case SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA:
                    jtfFkCfdAddendaTypeId.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRN_DPS_ADD, new int[]{ SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA }));
                    break;
                default:
            }

            if (!mbIsSales) {
                jlFileXml.setEnabled(true);
                jbFileXml.setEnabled(true);                
                jbDeleteFileXml.setEnabled(true);                
                
            }
            else {
                jlFileXml.setEnabled(false);
                jbFileXml.setEnabled(false);
                jbDeleteFileXml.setEnabled(false);                
            }
        }
    }

    private void setAddendaData() {
        String sFolioNotaRecepcion = "";
        String sBachocoSociedad = "";
        String sBachocoOrganizacion = "";
        String sBachocoDivision = "";
        String sDpsDescripcion = "";
        int nSorianaTienda = 0;
        int nGoodsDelivery = 0;
        int nSorianaTipoBulto = 0;
        int nCfdAddendaSubtype = 0;
        Date tSorianaFechaRemision = null;
        String sSorianaFolioRemision = "";
        String sSorianaFolioPedido = "";
        String sSorianaFolioNotaEntrada = "";
        double dSorianaCantidadBulto = 0;

        switch(moBizPartnerCategory.getFkCfdAddendaTypeId()) {
            case SDataConstantsSys.BPSS_TP_CFD_ADD_NA:
                jtfFkCfdAddendaTypeId.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRN_DPS_ADD, new int[] { SDataConstantsSys.BPSS_TP_CFD_ADD_NA }));
                break;

            case SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA:
                jlStore.setEnabled(true);
                jtfStore.setEnabled(false);
                jlGoodsDelivery.setEnabled(true);
                jtfGoodsDelivery.setEnabled(false);
                jlBulkType.setEnabled(true);
                jtfBulkType.setEnabled(false);
                jlBulkQuantity.setEnabled(true);
                jtfBulkQuantity.setEnabled(false);
                jlRemission.setEnabled(true);
                jtfRemission.setEnabled(false);
                jlSalesOrder.setEnabled(true);
                jtfSalesOrder.setEnabled(false);
                jlCfdAddendaSubtypeId.setEnabled(false);
                jcbCfdAddendaSubtypeId.setEnabled(false);
                jlNumberNoteIn.setEnabled(false);
                jtfNumberNoteIn.setEnabled(false);
                jlRemissionDate.setEnabled(true);
                jftRemissionDate.setEnabled(false);
                jtfFkCfdAddendaTypeId.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRN_DPS_ADD, new int[] { SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA }));
                if (moDps.getDbmsDataAddenda() != null && moDps.getDbmsDataAddenda().getSorianaTienda() > 0) {
                    nSorianaTienda = moDps.getDbmsDataAddenda().getSorianaTienda();
                    nGoodsDelivery = moDps.getDbmsDataAddenda().getSorianaEntregaMercancia();
                    tSorianaFechaRemision = moDps.getDbmsDataAddenda().getSorianaRemisionFecha();
                    sSorianaFolioRemision = moDps.getDbmsDataAddenda().getSorianaRemisionFolio();
                    sSorianaFolioPedido = moDps.getDbmsDataAddenda().getSorianaPedidoFolio();
                    nCfdAddendaSubtype = moDps.getDbmsDataAddenda().getCfdAddendaSubtype();
                    sSorianaFolioNotaEntrada = moDps.getDbmsDataAddenda().getSorianaNotaEntradaFolio();
                    nSorianaTipoBulto = moDps.getDbmsDataAddenda().getSorianaBultoTipo();
                    dSorianaCantidadBulto = moDps.getDbmsDataAddenda().getSorianaBultoCantidad();
                }
                moFieldStore.setFieldValue(nSorianaTienda);
                moFieldGoodsDelivery.setFieldValue(nGoodsDelivery);
                moFieldDateRemission.setFieldValue(tSorianaFechaRemision);
                moFieldRemission.setFieldValue(sSorianaFolioRemision);
                moFieldSalesOrder.setFieldValue(sSorianaFolioPedido);
                moFieldCfdAddendaSubtypeId.setFieldValue(new int[] { nCfdAddendaSubtype });
                moFieldNumberNoteIn.setFieldValue(sSorianaFolioNotaEntrada);
                moFieldBulkType.setFieldValue(nSorianaTipoBulto);
                moFieldBulkQty.setFieldValue(dSorianaCantidadBulto);
                break;

            case SDataConstantsSys.BPSS_TP_CFD_ADD_LOREAL:
                jlFolioNotaRecepcion.setEnabled(true);
                jtfFolioNotaRecepcion.setEnabled(false);
                jtfFkCfdAddendaTypeId.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRN_DPS_ADD, new int[] { SDataConstantsSys.BPSS_TP_CFD_ADD_LOREAL }));
                if (moDps.getDbmsDataAddenda() != null && moDps.getDbmsDataAddenda().getLorealFolioNotaRecepcion().length() > 0) {
                    sFolioNotaRecepcion = moDps.getDbmsDataAddenda().getLorealFolioNotaRecepcion();
                }
                moFieldFolioNotaRecepcion.setString(sFolioNotaRecepcion);
                break;

            case SDataConstantsSys.BPSS_TP_CFD_ADD_BACHOCO:
                jlBachocoSociedad.setEnabled(true);
                jtfBachocoSociedad.setEnabled(false);
                jlBachocoOrganizacion.setEnabled(true);
                jtfBachocoOrganizacion.setEnabled(false);
                jlBachocoDivision.setEnabled(true);
                jtfBachocoDivision.setEnabled(false);
                jtfFkCfdAddendaTypeId.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRN_DPS_ADD, new int[] { SDataConstantsSys.BPSS_TP_CFD_ADD_BACHOCO }));
                if (moDps.getDbmsDataAddenda() != null && moDps.getDbmsDataAddenda().getBachocoSociedad().length() > 0) {
                    sBachocoSociedad = moDps.getDbmsDataAddenda().getBachocoSociedad();
                    sBachocoOrganizacion = moDps.getDbmsDataAddenda().getBachocoSociedad();
                    sBachocoDivision = moDps.getDbmsDataAddenda().getBachocoDivision();
                }
                moFieldBachocoSociedad.setString(sBachocoSociedad);
                moFieldBachocoOrganizacionCompra.setString(sBachocoOrganizacion);
                moFieldBachocoDivision.setString(sBachocoDivision);
                break;

            case SDataConstantsSys.BPSS_TP_CFD_ADD_MODELO:
                jlBachocoSociedad.setEnabled(true);
                jtfBachocoSociedad.setEnabled(false);
                jlDpsDescripcion.setEnabled(true);
                jtfDpsDescripcion.setEnabled(false);
                jtfFkCfdAddendaTypeId.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRN_DPS_ADD, new int[] { SDataConstantsSys.BPSS_TP_CFD_ADD_MODELO }));
                if (moDps.getDbmsDataAddenda() != null && moDps.getDbmsDataAddenda().getBachocoSociedad().length() > 0) {
                    sBachocoSociedad = moDps.getDbmsDataAddenda().getBachocoSociedad();
                    sDpsDescripcion = moDps.getDbmsDataAddenda().getModeloDpsDescripcion();
                }
                moFieldBachocoSociedad.setString(sBachocoSociedad);
                moFieldDpsDescripcion.setString(sDpsDescripcion);
                break;

            case SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA:
                jtfFkCfdAddendaTypeId.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRN_DPS_ADD, new int[] { SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA }));
                break;
            default:
        }
    }

    private void setExchangeRate(final int idCurrency, final SFormField field) {
         double rate = 0;

        try {
            rate = SDataUtilities.obtainExchangeRate(miClient, idCurrency, moFieldDateDoc.getDate());
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        field.setFieldValue(rate);
     }

    private void updateEditForImportFromDps(boolean enable) {
        if (enable) {
            if (moPaneGridEntries.getTableGuiRowCount() > 0) {
                for (SDataDpsEntry entry : moDps.getDbmsDpsEntries()) {
                    if (entry.hasDpsLinksAsDestiny() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_LINK_DPS_DES, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
                        enable = false;
                        break;
                    }
                    else if (entry.hasDpsLinksAsSource() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_LINK_DPS_SRC, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
                        enable = false;
                        break;
                    }
                    else if (entry.hasDpsAdjustmentsAsDoc() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_ADJ_DOC, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
                        enable = false;
                        break;
                    }
                    else if (entry.hasDpsAdjustmentsAsAdjustment() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_ADJ_ADJ, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
                        enable = false;
                        break;
                    }
                    else if (SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_DIOG, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
                        enable = false;
                        break;
                    }
                    else if (SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_SHIP, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
                        enable = false;
                        break;
                    }
                }
            }
        }

        jcbFkCurrencyId.setEnabled(enable);
        jbFkCurrencyId.setEnabled(enable);
    }

    private void updateDpsControlsStatus() {
        jckRecordUser.setEnabled(mbIsDoc || mbIsAdj);

        jbEntryNew.setEnabled(!mbIsAdj);
        jbEntryDiscountRetailChain.setEnabled(mbIsSales && mbIsAdj);
        jbEntryImportFromDps.setEnabled(mbIsOrd || mbIsDoc || mbIsAdj);
        jbEntryWizard.setEnabled(!mbIsAdj);

        jlAdjustmentSubtypeId.setEnabled(mbIsAdj);
        jcbAdjustmentSubtypeId.setEnabled(mbIsAdj);

        jlDateDocLapsing_n.setEnabled(mbIsOrd || mbIsEst);
        jftDateDocLapsing_n.setEditable(mbIsOrd || mbIsEst);
        jftDateDocLapsing_n.setFocusable(mbIsOrd || mbIsEst);
        jbDateDocLapsing_n.setEnabled(mbIsOrd || mbIsEst);

        jlSalesAgentBizPartner.setEnabled(mbIsSales);
        jtfSalesAgentBizPartnerRo.setEnabled(mbIsSales);
        jlSalesAgent.setEnabled(mbIsSales);
        jtfSalesAgentRo.setEnabled(mbIsSales);
        jbSalesAgent.setEnabled(mbIsSales);
        jlSalesSupervisorBizPartner.setEnabled(mbIsSales);
        jtfSalesSupervisorBizPartnerRo.setEnabled(mbIsSales);
        jlSalesSupervisor.setEnabled(mbIsSales);
        jtfSalesSupervisorRo.setEnabled(mbIsSales);
        jbSalesSupervisor.setEnabled(mbIsSales);
        jlFkContactId_n.setEnabled(mbIsSales);
        jcbFkContactId_n.setEnabled(mbIsSales);
        jckIsRebill.setEnabled(mbIsOrd);
        
        jcbFkFunctionalArea.setEnabled(isFunctionalAreasApply());
    }

    private void updateFormEditStatus(boolean enable) {
        if (!enable) {
            mnFormStatus = SLibConstants.FORM_STATUS_READ_ONLY;

            jftDate.setEditable(false);
            jftDate.setFocusable(false);
            jbDate.setEnabled(false);
            jcbNumberSeries.setEnabled(false);
            jtfNumber.setEditable(false);
            jtfNumber.setFocusable(false);
            jtfNumberReference.setEditable(false);
            jtfNumberReference.setFocusable(false);

            jckDateDoc.setEnabled(false);
            jftDateDoc.setEditable(false);
            jftDateDoc.setFocusable(false);
            jbDateDoc.setEnabled(false);

            jckRecordUser.setEnabled(false);
            jbRecordManualSelect.setEnabled(false);
            jbRecordManualView.setEnabled(false);

            jckShipments.setEnabled(false);
            jtfShipments.setEditable(false);
            jtfShipments.setFocusable(false);

            jcbFkPaymentTypeId.setEnabled(false);
            jckPayments.setEnabled(false);
            jtfPayments.setEditable(false);
            jtfPayments.setFocusable(false);
            jtfDaysOfCredit.setEditable(false);
            jtfDaysOfCredit.setFocusable(false);
            jckDateStartCredit.setEnabled(false);
            jftDateStartCredit.setEditable(false);
            jftDateStartCredit.setFocusable(false);
            jbDateStartCredit.setEnabled(false);

            jcbFkPaymentSystemTypeId.setEnabled(false);
            jcbPaymentAccount.setEnabled(false);
            jcbFkLanguageId.setEnabled(false);
            jcbFkDpsNatureId.setEnabled(false);

            jcbFkCurrencyId.setEnabled(false);
            jbFkCurrencyId.setEnabled(false);
            jbExchangeRate.setEnabled(false);
            jtfExchangeRate.setEditable(false);
            jtfExchangeRate.setFocusable(false);
            jbComputeTotal.setEnabled(false);

            jckIsDiscountDocApplying.setEnabled(false);
            jckIsDiscountDocPercentage.setEnabled(false);
            jtfDiscountDocPercentage.setEditable(false);
            jtfDiscountDocPercentage.setFocusable(false);
            jckIsCopy.setEnabled(false);

            jbEntryNew.setEnabled(false);
            jbEntryDelete.setEnabled(false);
            jbEntryDiscountRetailChain.setEnabled(false);
            jbEntryImportFromDps.setEnabled(false);
            jbEntryWizard.setEnabled(false);

            jbNotesNew.setEnabled(false);
            jbNotesEdit.setEnabled(false);
            jbNotesDelete.setEnabled(false);
            jbSystemNotes.setEnabled(false);

            jcbAdjustmentSubtypeId.setEnabled(false);
            jcbTaxRegionId.setEnabled(false);
            jbTaxRegionId.setEnabled(false);

            jftDateDocDelivery_n.setEditable(false);
            jftDateDocDelivery_n.setFocusable(false);
            jbDateDocDelivery_n.setEnabled(false);
            jftDateDocLapsing_n.setEditable(false);
            jftDateDocLapsing_n.setFocusable(false);
            jbDateDocLapsing_n.setEnabled(false);
            jbSalesAgent.setEnabled(false);
            jbSalesSupervisor.setEnabled(false);
            jlFkContactId_n.setEnabled(false);
            jcbFkContactId_n.setEnabled(false);

            jcbFkIncotermId.setEnabled(false);
            jbFkIncotermId.setEnabled(false);
            jcbFkSpotSrcId_n.setEnabled(false);
            jcbFkSpotDesId_n.setEnabled(false);
            jcbFkModeOfTransportationTypeId.setEnabled(false);
            jbFkModeOfTransportationTypeId.setEnabled(false);
            jcbFkCarrierTypeId.setEnabled(false);
            jcbFkCarrierId_n.setEnabled(false);
            jbFkCarrierId_n.setEnabled(false);
            jcbFkVehicleTypeId_n.setEnabled(false);
            jcbFkVehicleId_n.setEnabled(false);
            jbFkVehicleId_n.setEnabled(false);
            jlFkProductionOrderId_n.setEnabled(false);
            jcbFkProductionOrderId_n.setEnabled(false);
            jbFkProductionOrderId_n.setEnabled(false);
            jbFileXml.setEnabled(false);
            jbDeleteFileXml.setEnabled(false);                
            jcbDriver.setEnabled(false);
            jcbPlate.setEnabled(false);

            jtfTicket.setEditable(false);
            jtfTicket.setFocusable(false);
            
            jckIsRebill.setEnabled(false);
            
            setEnebleCfdFields(false);
        }
        else {
            mnFormStatus = SLibConstants.FORM_STATUS_EDIT;

            jftDate.setEditable(true);
            jftDate.setFocusable(true);
            jbDate.setEnabled(true);
            jcbNumberSeries.setEnabled(moDps.getIsRegistryNew() || !mbIsNumberSeriesRequired);
            jtfNumber.setEditable(mbIsNumberEditable);
            jtfNumber.setFocusable(mbIsNumberEditable);
            jtfNumberReference.setEditable(true);
            jtfNumberReference.setFocusable(true);

            jckDateDoc.setEnabled(true);
            itemStateDateDoc();

            updateDpsControlsStatus();
            itemStateRecordManual();                    // status updated in function updateDpsControlsStatus()

            jckShipments.setEnabled(true);
            itemStateShipments();

            adecuatePaymentTypeSettings();
            itemChangeFkPaymentTypeId(false);           // it invokes itemStatePayments() and itemStateDateStartCredit()

            jcbFkPaymentTypeId.setEnabled(!moDps.getIsCopied());
            jtfDaysOfCredit.setEditable(!moDps.getIsCopied() && moFieldFkPaymentTypeId.getKeyAsIntArray()[0] != SDataConstantsSys.TRNS_TP_PAY_CASH);
            jtfDaysOfCredit.setFocusable(!moDps.getIsCopied() && moFieldFkPaymentTypeId.getKeyAsIntArray()[0] != SDataConstantsSys.TRNS_TP_PAY_CASH);
            jcbFkPaymentSystemTypeId.setEnabled(true);
            jcbPaymentAccount.setEnabled(true);
            //jcbFkLanguageId.setEnabled(true);
            jcbFkDpsNatureId.setEnabled(true);

            itemChangeFkPaymentSystemTypeId();
            itemChangeFkLanguageId();

            jcbFkCurrencyId.setEnabled(moDps.getIsRegistryNew());
            jbFkCurrencyId.setEnabled(moDps.getIsRegistryNew());
            itemChangeFkCurrencyId(false);

            jckIsDiscountDocApplying.setEnabled(true);
            itemStateIsDiscountDocApplying(false);
            itemStateIsDiscountDocPercentage(false);
            jckIsCopy.setEnabled(!mbIsSales && (mbIsDoc || mbIsAdj));

            //jbEntryNew.setEnabled(true);                  // status updated in function updateDpsControlsStatus()
            jbEntryDelete.setEnabled(true);
            //jbEntryDiscountRetailChain.setEnabled(true);  // status updated in function updateDpsControlsStatus()
            //jbEntryImportFromDps.setEnabled(true);        // status updated in function updateDpsControlsStatus()
            //jbEntryWizard.setEnabled(true);               // status updated in function updateDpsControlsStatus()

            jbNotesNew.setEnabled(true);
            jbNotesEdit.setEnabled(true);
            jbNotesDelete.setEnabled(true);
            jbSystemNotes.setEnabled(true);

            //jcbAdjustTypeId.setEnabled(true);         // status updated in function updateDpsControlsStatus()
            jcbTaxRegionId.setEnabled(true);
            jbTaxRegionId.setEnabled(true);

            jftDateDocDelivery_n.setEditable(!mbIsAdj);
            jftDateDocDelivery_n.setFocusable(!mbIsAdj);
            jbDateDocDelivery_n.setEnabled(!mbIsAdj);
            //jftDateOrderLapsing_n.setEnabled(true);   // status updated in function updateDpsControlsStatus()
            //jbDateOrderLapsing_n.setEnabled(true);    // status updated in function updateDpsControlsStatus()
            //jbSalesAgent.setEnabled(false);           // status updated in function updateDpsControlsStatus()
            //jlFkContactId_n.setEnabled(false);        // status updated in function updateDpsControlsStatus()
            //jcbFkContactId_n.setEnabled(false);       // status updated in function updateDpsControlsStatus()

            jcbFkIncotermId.setEnabled(true);
            jbFkIncotermId.setEnabled(true);
            jcbFkModeOfTransportationTypeId.setEnabled(true);
            jbFkModeOfTransportationTypeId.setEnabled(true);
            jcbFkCarrierTypeId.setEnabled(true);
            itemChangeFkCarrierTypeId();
            jcbFkSpotDesId_n.setEnabled(true);
            itemChangeFkVehicleTypeId_n();
            jlFkProductionOrderId_n.setEnabled(mbIsSales);
            jcbFkProductionOrderId_n.setEnabled(mbIsSales);
            jbFkProductionOrderId_n.setEnabled(mbIsSales);

            jtfTicket.setEditable(true);
            jtfTicket.setFocusable(true);

            updateEditForImportFromDps(true);
            jckIsRebill.setEnabled(mbIsOrd);
            
            setEnebleCfdFields(mbIsSales && (mbIsDoc || mbIsAdj));
        }

        if (jTabbedPane.isEnabledAt(3)) {
            renderAddendaData(true);
        }
    }

    private void resetEditionFlags() {
        mbHasDpsLinksAsSrc = false;
        mbHasDpsLinksAsDes = false;
        mbHasDpsAdjustmentsAsDoc = false;
        mbHasDpsAdjustmentsAsAdj = false;
        mbHasIogSupplies = false;
        mbHasCommissions = false;
        mbHasShipments = false;
    }

    private boolean hasDpsLinksButIsEditable() {
        return mbHasDpsLinksAsDes || mbHasDpsLinksAsSrc && !mbHasDpsAdjustmentsAsAdj &&  !mbHasDpsAdjustmentsAsDoc || mbHasIogSupplies && !mbHasCommissions || mbHasShipments;
    }

    private boolean hasDpsAdjustmentsAsAdjButIsEditable() {
        return mbHasDpsAdjustmentsAsAdj || mbHasDpsAdjustmentsAsDoc && !mbHasDpsLinksAsDes && !mbHasDpsLinksAsSrc && !mbHasIogSupplies && !mbHasCommissions && !mbHasShipments;
    }

    private boolean canEditEntry(erp.mtrn.data.SDataDpsEntry entry) {
        boolean can = true;

        resetEditionFlags();

        if (entry.hasDpsLinksAsSource() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_LINK_DPS_SRC, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
            can = miClient.showMsgBoxConfirm(SProcConstants.MSG_TRN_DPS_ETY_COUNT_LINK_DPS_SRC + "\nSin embargo, puede modificar la información de la partida con ciertas reservas. ¿Desea modificarla?") == JOptionPane.YES_OPTION;
            mbHasDpsLinksAsSrc = true;
        }
        else if (entry.hasDpsAdjustmentsAsDoc() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_ADJ_DOC, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
            can = miClient.showMsgBoxConfirm(SProcConstants.MSG_TRN_DPS_ETY_COUNT_ADJ_DOC + "\nSin embargo, puede modificar la información de la partida con ciertas reservas. ¿Desea modificarla?") == JOptionPane.YES_OPTION;
            mbHasDpsAdjustmentsAsDoc = true;
        }
        else if (SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_COMMS, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
            can = false;
            mbHasCommissions = true;
            miClient.showMsgBoxWarning(SProcConstants.MSG_TRN_DPS_ETY_COUNT_COMMS);
        }
        else if (SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_DIOG, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
            can = miClient.showMsgBoxConfirm(SProcConstants.MSG_TRN_DPS_ETY_COUNT_DIOG + "\nSin embargo, puede modificar la información de la partida con ciertas reservas. ¿Desea modificarla?") == JOptionPane.YES_OPTION;
            mbHasIogSupplies = true;
        }
        else if (SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_SHIP, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
            can = miClient.showMsgBoxConfirm(SProcConstants.MSG_TRN_DPS_ETY_COUNT_SHIP + "\nSin embargo, puede modificar la información de la partida con ciertas reservas. ¿Desea modificarla?") == JOptionPane.YES_OPTION;
            mbHasShipments = true;
        }
        else if (entry.hasDpsLinksAsDestiny() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_LINK_DPS_DES, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
            can = miClient.showMsgBoxConfirm(SProcConstants.MSG_TRN_DPS_ETY_COUNT_LINK_DPS_DES + "\nSin embargo, puede modificar la información de la partida con ciertas reservas. ¿Desea modificarla?") == JOptionPane.YES_OPTION;
            mbHasDpsLinksAsDes = true;
        }
        else if (!entry.getIsDiscountRetailChain() &&
            (entry.hasDpsAdjustmentsAsAdjustment() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_ADJ_ADJ, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0)) {
            can = miClient.showMsgBoxConfirm(SProcConstants.MSG_TRN_DPS_ETY_COUNT_ADJ_ADJ + "\nSin embargo, puede modificar la información de la partida con ciertas reservas. ¿Desea modificarla?") == JOptionPane.YES_OPTION;
            mbHasDpsAdjustmentsAsAdj = true;
        }

        return can;
    }

    private boolean canDeleteEntry(erp.mtrn.data.SDataDpsEntry entry) {
        boolean can = true;

        resetEditionFlags();

        if (entry.hasDpsLinksAsSource() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_LINK_DPS_SRC, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
            can = false;
            mbHasDpsLinksAsSrc = true;
            miClient.showMsgBoxWarning(SProcConstants.MSG_TRN_DPS_ETY_COUNT_LINK_DPS_SRC);
        }
        else if (entry.hasDpsAdjustmentsAsDoc() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_ADJ_DOC, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
            can = false;
            mbHasDpsAdjustmentsAsDoc = true;
            miClient.showMsgBoxWarning(SProcConstants.MSG_TRN_DPS_ETY_COUNT_ADJ_DOC);
        }
        else if (SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_COMMS, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
            can = false;
            mbHasCommissions = true;
            miClient.showMsgBoxWarning(SProcConstants.MSG_TRN_DPS_ETY_COUNT_COMMS);
        }
        else if (SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_DIOG, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
            can = false;
            mbHasIogSupplies = true;
            miClient.showMsgBoxWarning(SProcConstants.MSG_TRN_DPS_ETY_COUNT_DIOG);
        }
        else if (SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_SHIP, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
            can = false;
            mbHasShipments = true;
            miClient.showMsgBoxWarning(SProcConstants.MSG_TRN_DPS_ETY_COUNT_SHIP);
        }

        if (can) {
            if (entry.hasDpsLinksAsDestiny() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_LINK_DPS_DES, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
                can = miClient.showMsgBoxConfirm(SProcConstants.MSG_TRN_DPS_ETY_COUNT_LINK_DPS_DES + "\nSi elimina la partida se perderá dicha información. ¿Desea continuar?") == JOptionPane.YES_OPTION;
                mbHasDpsLinksAsDes = true;
            }
        }

        if (can) {
            if (!entry.getIsDiscountRetailChain() &&
                    (entry.hasDpsAdjustmentsAsAdjustment() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_ADJ_ADJ, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0)) {
                can = miClient.showMsgBoxConfirm(SProcConstants.MSG_TRN_DPS_ETY_COUNT_ADJ_ADJ + "\nSi elimina la partida se perderá dicha información. ¿Desea continuar?") == JOptionPane.YES_OPTION;
                mbHasDpsAdjustmentsAsAdj = true;
            }
        }

        return can;
    }

     private String validateDateLinks() {
        String msg = "";
        SDataDps dpsLinked = null;
        Vector<Object> vParams = new Vector<Object>();

        resetEditionFlags();

        DPS:
        for (SDataDpsEntry entry : moDps.getDbmsDpsEntries()) {
            if (!entry.getIsDeleted()) {
                // Validate links as source:

                if (entry.getDbmsDpsLinksAsSource().size() > 0) {
                    mbHasDpsLinksAsSrc = true;

                    for (SDataDpsDpsLink link : entry.getDbmsDpsLinksAsSource()) {
                        dpsLinked = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, link.getDbmsDestinyDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);

                        if (moFieldDate.getDate().after(dpsLinked.getDate())) {
                            msg = "no puede ser posterior a la fecha del documento '" +  dpsLinked.getDpsNumber() + "' (" + SLibUtils.DateFormatDate.format(dpsLinked.getDate()) + ").";
                            break DPS;
                        }
                    }
                }

                // Validate links as destiny:

                if (entry.getDbmsDpsLinksAsDestiny().size() > 0) {
                    mbHasDpsLinksAsDes = true;

                    for (SDataDpsDpsLink link : entry.getDbmsDpsLinksAsDestiny()) {
                        dpsLinked = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, link.getDbmsSourceDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);

                        if (moFieldDate.getDate().before(dpsLinked.getDate())) {
                            msg = "no puede ser anterior a la fecha del documento '" +  dpsLinked.getDpsNumber() + "' (" + SLibUtils.DateFormatDate.format(dpsLinked.getDate()) + ").";
                            break DPS;
                        }
                        else if (mbValidateLinkPeriod && dpsLinked.getDateDocDelivery_n() != null && moFieldDate.getDate().before(dpsLinked.getDateDocDelivery_n())) {
                            msg = "El valor del campo '" + jlDate.getText() + "' (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ")\nes anterior a la fecha 'entrega programada' del documento de origen '" +  dpsLinked.getDpsNumber() + "' (" + SLibUtils.DateFormatDate.format(dpsLinked.getDateDocDelivery_n()) + ").";
                            if (miClient.showMsgBoxConfirm(msg + "\n ¿Desea continuar?") == JOptionPane.YES_OPTION) {
                                msg = "";
                            }
                            break DPS;
                        }
                        else if (mbValidateLinkPeriod && dpsLinked.getDateDocLapsing_n() != null && moFieldDate.getDate().after(dpsLinked.getDateDocLapsing_n())) {
                            msg = "El valor del campo '" + jlDate.getText() + "' (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ")\nes posterior a la fecha 'última entrega' del documento de origen '" +  dpsLinked.getDpsNumber() + "' (" + SLibUtils.DateFormatDate.format(dpsLinked.getDateDocLapsing_n()) + ").";
                            if (miClient.showMsgBoxConfirm(msg + "\n ¿Desea continuar?") == JOptionPane.YES_OPTION) {
                                msg = "";
                            }
                            break DPS;
                        }
                    }
                }

                // Validate links as document:

                if (mbIsDoc && entry.getDbmsDpsAdjustmentsAsDps().size() > 0) {
                    for (SDataDpsDpsAdjustment adjustment : entry.getDbmsDpsAdjustmentsAsDps()) {
                        dpsLinked = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, adjustment.getDbmsDpsAdjustmentKey(), SLibConstants.EXEC_MODE_VERBOSE);
                        if (moFieldDate.getDate().after(dpsLinked.getDate())) {
                            msg = "no puede ser posterior a la fecha del documento '" +  dpsLinked.getDpsNumber() + "' (" + SLibUtils.DateFormatDate.format(dpsLinked.getDate()) + ").";
                            break DPS;
                        }
                    }
                }

                // validate links as adjustment:

                if (mbIsAdj && entry.getDbmsDpsAdjustmentsAsAdjustment().size() > 0) {
                    for (SDataDpsDpsAdjustment adjustment : entry.getDbmsDpsAdjustmentsAsAdjustment()) {
                        dpsLinked = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, adjustment.getDbmsDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
                        if (moFieldDate.getDate().before(dpsLinked.getDate())) {
                            msg = "no puede ser anterior a la fecha del documento '" +  dpsLinked.getDpsNumber() + "' (" + SLibUtils.DateFormatDate.format(dpsLinked.getDate()) + ").";
                            break DPS;
                        }
                    }
                }

                // Validate other links:

                if (mbIsDoc && !moDps.getIsRegistryNew()) {
                    vParams.clear();
                    vParams.add(entry.getPrimaryKey());
                    vParams.add(moFieldDate.getDate());
                    vParams = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_DPS_ETY_DATE_VAL, vParams, SLibConstants.EXEC_MODE_SILENT);

                    if (vParams.size() > 0) {
                        switch((Integer) vParams.get(0)) {
                            case 1: // linked to commissions
                                msg = "no puede ser posterior a la fecha de los documentos de comisiones.";
                                break;
                            case 2: // linked to DIOG's
                                msg = "no puede ser posterior a la fecha del documento de inventarios: '" + (String) vParams.get(1) + "'.";
                                break;
                            case 3: // linked to shipments
                                msg = "no puede ser posterior a la fecha del documento de embarque: '" + (String) vParams.get(1) + "'.";
                                break;
                            default:
                        }
                    }

                    if (!msg.isEmpty()) {
                        break DPS;
                    }
                }
            }
        }

        if (!msg.isEmpty()) {
            msg = "El valor del campo '" + jlDate.getText() + "' (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ")\n" + msg;
        }

        return msg;
    }

    private void setDpsType(int[] docTypeKey) {
        int index = 0;
        Vector<SFormComponentItem> componentItems = null;

        mbResetingForm = true;
        mbIsCon = false;
        mbIsEst = false;
        mbIsOrd = false;
        mbIsDoc = false;
        mbIsAdj = false;
        mbIsSales = false;
        mbIsNumberSeriesRequired = false;
        mbIsNumberSeriesAvailable = false;
        manDpsClassKey = new int[] { docTypeKey[0], docTypeKey[1] };
        moDpsType = (SDataDpsType) SDataUtilities.readRegistry(miClient, SDataConstants.TRNU_TP_DPS, docTypeKey, SLibConstants.EXEC_MODE_VERBOSE);

        mbIsCon = SLibUtilities.compareKeys(SDataConstantsSys.TRNU_TP_DPS_SAL_CON, docTypeKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNU_TP_DPS_PUR_CON, docTypeKey);
        mbIsEst = SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_PUR_EST, manDpsClassKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_EST, manDpsClassKey);
        mbIsOrd = SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_PUR_ORD, manDpsClassKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_ORD, manDpsClassKey);
        mbIsDoc = SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_PUR_DOC, manDpsClassKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_DOC, manDpsClassKey);
        mbIsAdj = SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ, manDpsClassKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ, manDpsClassKey);
        mbIsSales = SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_EST, manDpsClassKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_ORD, manDpsClassKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_DOC, manDpsClassKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ, manDpsClassKey);

        if (mbIsEst) {
            manDpsClassPreviousKey = null;
            moDialogPickerDps = null;
        }
        else if (mbIsOrd) {
            moDialogPickerDps = moDialogPickerDpsForLink;

            if (manDpsClassKey[0] == SDataConstantsSys.TRNS_CT_DPS_PUR) {
                manDpsClassPreviousKey = SDataConstantsSys.TRNS_CL_DPS_PUR_EST;
            }
            else {
                manDpsClassPreviousKey = SDataConstantsSys.TRNS_CL_DPS_SAL_EST;
            }
        }
        else if (mbIsDoc) {
            moDialogPickerDps = moDialogPickerDpsForLink;

            if (manDpsClassKey[0] == SDataConstantsSys.TRNS_CT_DPS_PUR) {
                manDpsClassPreviousKey = SDataConstantsSys.TRNS_CL_DPS_PUR_ORD;
            }
            else {
                manDpsClassPreviousKey = SDataConstantsSys.TRNS_CL_DPS_SAL_ORD;
            }
        }
        else if (mbIsAdj) {
            moDialogPickerDps = moDialogPickerDpsForAdjustment;

            if (manDpsClassKey[0] == SDataConstantsSys.TRNS_CT_DPS_PUR) {
                manDpsClassPreviousKey = SDataConstantsSys.TRNS_CL_DPS_PUR_DOC;
            }
            else {
                manDpsClassPreviousKey = SDataConstantsSys.TRNS_CL_DPS_SAL_DOC;
            }
        }

        updateDpsControlsStatus();

        // Adecuate DPS number:

        index = mvFields.indexOf(moFieldNumberSeries);
        mvFields.removeElementAt(index);
        jcbNumberSeries.removeAllItems();

        if (isNumberSeriesBySystem()) {
            jcbNumberSeries.setEditable(false);
            moFieldNumberSeries = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbNumberSeries, jlNumber);
            moFieldNumberSeries.setIsSelectionItemApplying(false);  // Combo Box does not have item 0

            if (miClient.getSessionXXX().getCurrentCompanyBranch() != null) {
                componentItems = miClient.getSessionXXX().getCurrentCompanyBranch().getDnsForDps();

                for (SFormComponentItem componentItem : componentItems) {
                    if (SLibUtilities.compareKeys(moDpsType.getPrimaryKey(), componentItem.getPrimaryKey())) {
                        jcbNumberSeries.addItem(componentItem);
                    }
                }
            }

            mbIsNumberSeriesRequired = true;
            if (jcbNumberSeries.getItemCount() > 0) {
                mbIsNumberSeriesAvailable = true;
            }
        }
        else {
            jcbNumberSeries.setEditable(true);
            moFieldNumberSeries = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jcbNumberSeries, jlNumber);
            moFieldNumberSeries.setLengthMax(15);
        }

        mvFields.insertElementAt(moFieldNumberSeries, index);

        // Link period validation:

        if (mbIsSales) {
            mbValidateLinkPeriod = ((SDataParamsErp) miClient.getSession().getConfigSystem()).getIsSalesLinkPeriod();
        }
        else {
            mbValidateLinkPeriod = ((SDataParamsErp) miClient.getSession().getConfigSystem()).getIsPurchasesLinkPeriod();
        }

        // XXX Add drivers and plates related to current business partner...

        jcbDriver.removeAllItems();
        jcbPlate.removeAllItems();

        mbResetingForm = false;
    }

    private void setBizPartner(int[] bizPartnerKey, int[] bizPartnerBranchKey, int[] branchAddressKey) {
        moBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, bizPartnerKey, SLibConstants.EXEC_MODE_VERBOSE);
        moBizPartnerBranch = null;
        moBizPartnerBranchAddress = null;
        moBizPartnerBranchAddressMain = null;

        if (bizPartnerBranchKey != null) {
            moBizPartnerBranch = moBizPartner.getDbmsBizPartnerBranch(bizPartnerBranchKey);
            moBizPartnerBranchAddressMain = moBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial();

            mnTypeDelivery = (moBizPartnerBranchAddressMain.getFkCountryId_n() == SLibConstants.UNDEFINED || miClient.getSession().getSessionCustom().isLocalCountry(new int[] { (moBizPartnerBranchAddressMain.getFkCountryId_n()) })) ? SModSysConsts.LOGS_TP_DLY_DOM : SModSysConsts.LOGS_TP_DLY_INT;

            SFormUtilities.populateComboBox(miClient, jcbFkIncotermId, SModConsts.LOGS_INC,
                    new int[] { mnTypeDelivery });

             moFieldFkIncotermId.setFieldValue(new int[] { SModSysConsts.LOGS_INC_NA });
        }

        if (moBizPartnerBranch != null && branchAddressKey != null) {
            moBizPartnerBranchAddress = moBizPartnerBranch.getDbmsBizPartnerBranchAddress(branchAddressKey);
        }

        jcbFkContactId_n.removeAllItems();

        if (mnFormType == SDataConstantsSys.TRNS_CT_DPS_PUR) {
            moBizPartnerCategory = moBizPartner.getDbmsCategorySettingsSup();
        }
        else {
            moBizPartnerCategory = moBizPartner.getDbmsCategorySettingsCus();

            jcbFkContactId_n.addItem(new SFormComponentItem(new int[2], "(Seleccionar comprador)"));

            for (SDataBizPartnerBranchContact contact : moBizPartnerBranch.getDbmsBizPartnerBranchContacts()) {
                if (contact.getPkContactId() > 1 && !contact.getIsDeleted()) {  // contact #1 has company branch telephones
                    jcbFkContactId_n.addItem(new SFormComponentItem(contact.getPrimaryKey(), contact.getContact() + " (" + contact.getDbmsContactType() + ")"));
                }
            }
        }

        if (moDps.getIsRegistryNew()) {
            updateDpsWithBizPartnerSettings();
        }

        adecuatePaymentTypeSettings();
        renderBizPartner();
        renderBizPartnerPrepaymentsBalance();
    }

    private void prepareDpsEntryComplementary(erp.mtrn.data.SDataDpsEntry oDpsEntryAdjustment, erp.mtrn.data.SDataDpsEntry oDpsEntryComplementary) {
        SDataDpsDpsAdjustment adjustment = null;

        oDpsEntryComplementary.setConceptKey(oDpsEntryAdjustment.getConceptKey());
        oDpsEntryComplementary.setConcept(oDpsEntryAdjustment.getConcept());
        oDpsEntryComplementary.setOriginalQuantity(0);
        oDpsEntryComplementary.setOriginalPriceUnitaryCy(0);
        oDpsEntryComplementary.setOriginalPriceUnitarySystemCy(0);
        oDpsEntryComplementary.setOriginalDiscountUnitaryCy(0);
        oDpsEntryComplementary.setOriginalDiscountUnitarySystemCy(0);

        oDpsEntryComplementary.setLength(0);
        oDpsEntryComplementary.setSurface(0);
        oDpsEntryComplementary.setVolume(0);
        oDpsEntryComplementary.setMass(0);
        oDpsEntryComplementary.setWeightGross(0);
        oDpsEntryComplementary.setWeightDelivery(0);

        oDpsEntryComplementary.setSurplusPercentage(0);
        oDpsEntryComplementary.setSortingPosition(0);
        oDpsEntryComplementary.setIsTaxesAutomaticApplying(true);
        oDpsEntryComplementary.setIsInventoriable(false);
        oDpsEntryComplementary.setIsDeleted(false);
        oDpsEntryComplementary.setFkItemId(oDpsEntryAdjustment.getFkItemId());
        oDpsEntryComplementary.setFkUnitId(oDpsEntryAdjustment.getFkUnitId());
        oDpsEntryComplementary.setFkOriginalUnitId(oDpsEntryAdjustment.getFkOriginalUnitId());
        oDpsEntryComplementary.setFkTaxRegionId(oDpsEntryAdjustment.getFkTaxRegionId());
        oDpsEntryComplementary.setFkDpsAdjustmentTypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[0]);
        oDpsEntryComplementary.setFkDpsAdjustmentSubtypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[1]);
        oDpsEntryComplementary.setFkDpsEntryTypeId(SDataConstantsSys.TRNS_TP_DPS_ETY_VIRT);
        oDpsEntryComplementary.setFkItemRefId_n(oDpsEntryAdjustment.getFkItemRefId_n());
        oDpsEntryComplementary.setFkCostCenterId_n(oDpsEntryAdjustment.getFkCostCenterId_n());
        oDpsEntryComplementary.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

        /*
        oDpsEntryComplementary.setDbmsFkItemGenericId(oDpsEntry.getDbmsFkItemGenericId());
        oDpsEntryComplementary.setDbmsUnitSymbol(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.ITMU_UNIT, new int[] { oDpsEntryComplementary.getFkUnitId() }, SLibConstants.DESCRIPTION_CODE));
        oDpsEntryComplementary.setDbmsOriginalUnitSymbol(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.ITMU_UNIT, new int[] { oDpsEntryComplementary.getFkOriginalUnitId() }, SLibConstants.DESCRIPTION_CODE));
        oDpsEntryComplementary.setDbmsTaxRegion(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.FINU_TAX_REG, new int[] { oDpsEntryComplementary.getFkTaxRegionId() }));
        oDpsEntryComplementary.setDbmsDpsAdjustmentType(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_TP_DPS_ADJ, new int[] { oDpsEntryComplementary.getFkDpsAdjustmentTypeId() }));
        oDpsEntryComplementary.setDbmsDpsEntryType(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_TP_DPS_ETY, new int[] { oDpsEntryComplementary.getFkDpsEntryTypeId() }));
        */

        oDpsEntryComplementary.calculateTotal(miClient, moDps.getDate(),
                moDps.getFkTaxIdentityEmisorTypeId(), moDps.getFkTaxIdentityReceptorTypeId(),
                moDps.getIsDiscountDocPercentage(), moDps.getDiscountDocPercentage(), moDps.getExchangeRate());

        adjustment = new SDataDpsDpsAdjustment();
        adjustment.setPkDpsYearId(oDpsEntryComplementary.getPkYearId());
        adjustment.setPkDpsDocId(oDpsEntryComplementary.getPkDocId());
        adjustment.setPkDpsEntryId(oDpsEntryComplementary.getPkEntryId());
        adjustment.setPkDpsAdjustmentYearId(oDpsEntryAdjustment.getPkYearId());
        adjustment.setPkDpsAdjustmentDocId(oDpsEntryAdjustment.getPkDocId());
        adjustment.setPkDpsAdjustmentEntryId(oDpsEntryAdjustment.getPkEntryId());
        adjustment.setQuantity(0);
        adjustment.setOriginalQuantity(0);
        adjustment.setValue(oDpsEntryAdjustment.getTotal_r());
        adjustment.setValueCy(oDpsEntryAdjustment.getTotalCy_r());
        adjustment.setAuxDpsEntryComplementary(oDpsEntryComplementary);

        oDpsEntryAdjustment.getDbmsDpsAdjustmentsAsAdjustment().clear();
        oDpsEntryAdjustment.getDbmsDpsAdjustmentsAsAdjustment().add(adjustment);
    }

    @SuppressWarnings("unchecked")
    private void prepareDpsEntryImport(Vector<SDataDpsEntry> vndpsSourceEntryTemp, SDataDps oDpsSource, SDataItem oItem) {
        int decs = miClient.getSessionXXX().getParamsErp().getDecimalsValue();

        if (mbIsLocalCurrency && moDps.getFkCurrencyId() != oDpsSource.getFkCurrencyId()) {
            for (int i = 0; i < vndpsSourceEntryTemp.size(); i++) {
                vndpsSourceEntryTemp.get(i).setOriginalPriceUnitaryCy(vndpsSourceEntryTemp.get(i).getPriceUnitary());
                vndpsSourceEntryTemp.get(i).setOriginalPriceUnitarySystemCy(vndpsSourceEntryTemp.get(i).getPriceUnitarySystem());
                vndpsSourceEntryTemp.get(i).setOriginalDiscountUnitaryCy(vndpsSourceEntryTemp.get(i).getDiscountUnitary());
                vndpsSourceEntryTemp.get(i).setOriginalDiscountUnitarySystemCy(vndpsSourceEntryTemp.get(i).getDiscountUnitarySystem());
                vndpsSourceEntryTemp.get(i).setDiscountEntryCy(vndpsSourceEntryTemp.get(i).getDiscountEntry());
                vndpsSourceEntryTemp.get(i).setDiscountDocCy(vndpsSourceEntryTemp.get(i).getDiscountDoc());
            }
        }

        for (SDataEntryDpsDpsLink entryLink : (Vector<SDataEntryDpsDpsLink>) moDialogDpsLink.getValue(SDataConstants.TRNX_DPS_DES)) {
            for (SDataDpsEntry dpsSourceEntry : vndpsSourceEntryTemp ) {
                if (SLibUtilities.compareKeys(entryLink.getDpsEntryKey(), dpsSourceEntry.getPrimaryKey())) {
                    SDataDpsEntry entry = null;
                    SDataDpsDpsLink link = null;

                    entry = dpsSourceEntry.clone();
                    entry.setPkYearId(moDps.getPkYearId());
                    entry.setPkDocId(moDps.getPkDocId());
                    entry.setPkEntryId(0);
                    entry.setIsRegistryNew(true);
                    entry.setOriginalQuantity(entryLink.getQuantityToLink());
                    entry.setQuantity(SLibUtilities.round(entry.getOriginalQuantity() * ((SSessionCustom) miClient.getSession().getSessionCustom()).getUnitsFactorForQuantity(entry.getFkItemId(), entry.getFkOriginalUnitId(), entry.getFkUnitId()), miClient.getSessionXXX().getParamsErp().getDecimalsQuantity()));  // required for physical units
                    entry.setSurplusPercentage(0);

                    if (!dpsSourceEntry.getIsDiscountEntryPercentage()) {
                        entry.setDiscountEntryCy(SLibUtilities.round(dpsSourceEntry.getDiscountEntryCy() * (dpsSourceEntry.getOriginalQuantity() == 0 ? 0 : entryLink.getQuantityToLink() / dpsSourceEntry.getOriginalQuantity()), decs));
                    }

                    if (oDpsSource.getIsDiscountDocApplying() && !oDpsSource.getIsDiscountDocPercentage()) {
                        entry.setDiscountDocCy(SLibUtilities.round(dpsSourceEntry.getDiscountDocCy() * (dpsSourceEntry.getOriginalQuantity() == 0 ? 0 : entryLink.getQuantityToLink() / dpsSourceEntry.getOriginalQuantity()), decs));
                    }

                    if (entry.getIsTaxesAutomaticApplying()) {
                        entry.getDbmsEntryTaxes().clear();  // taxes will be calculated again
                    }
                    else {
                        for (SDataDpsEntryTax tax : entry.getDbmsEntryTaxes()) {
                            tax.setTaxCy(SLibUtilities.round((mbIsLocalCurrency && moDps.getFkCurrencyId() != oDpsSource.getFkCurrencyId() ? tax.getTax() : tax.getTaxCy()) * (dpsSourceEntry.getOriginalQuantity() == 0 ? 0 : entryLink.getQuantityToLink() / dpsSourceEntry.getOriginalQuantity()), decs));
                        }
                    }

                    for (SDataDpsEntryCommissions comms : entry.getDbmsEntryCommissions()) {
                        comms.setCommissionsCy(SLibUtilities.round(comms.getCommissionsCy() * (dpsSourceEntry.getOriginalQuantity() == 0 ? 0 : entryLink.getQuantityToLink() / dpsSourceEntry.getOriginalQuantity()), decs));
                    }

                    //entry.getDbmsEntryNotes().clear();    // notes are imported aswell
                    entry.getDbmsDpsLinksAsSource().clear();
                    entry.getDbmsDpsLinksAsDestiny().clear();
                    entry.getDbmsDpsAdjustmentsAsDps().clear();
                    entry.getDbmsDpsAdjustmentsAsAdjustment().clear();

                    // Adjust physical units:

                    oItem = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { entry.getFkItemId() }, SLibConstants.EXEC_MODE_VERBOSE);
                    if (oItem != null) {
                        entry.setLength(!oItem.getDbmsDataItemGeneric().getIsLengthApplying() ? 0d : entry.getQuantity() * oItem.getLength());
                        entry.setSurface(!oItem.getDbmsDataItemGeneric().getIsSurfaceApplying() ? 0d : entry.getQuantity() * oItem.getSurface());
                        entry.setVolume(!oItem.getDbmsDataItemGeneric().getIsVolumeApplying() ? 0d : entry.getQuantity() * oItem.getVolume());
                        entry.setMass(!oItem.getDbmsDataItemGeneric().getIsMassApplying() ? 0d : entry.getQuantity() * oItem.getMass());
                        entry.setWeightGross(!oItem.getDbmsDataItemGeneric().getIsWeightGrossApplying() ? 0d : entry.getQuantity() * oItem.getWeightGross());
                        entry.setWeightDelivery(!oItem.getDbmsDataItemGeneric().getIsWeightDeliveryApplying() ? 0d : entry.getQuantity() * oItem.getWeightDelivery());
                    }

                    // Create link:

                    link = new SDataDpsDpsLink();
                    link.setPkSourceYearId(dpsSourceEntry.getPkYearId());
                    link.setPkSourceDocId(dpsSourceEntry.getPkDocId());
                    link.setPkSourceEntryId(dpsSourceEntry.getPkEntryId());
                    link.setPkDestinyYearId(entry.getPkYearId());
                    link.setPkDestinyDocId(entry.getPkDocId());
                    link.setPkDestinyEntryId(entry.getPkEntryId());
                    link.setQuantity(entry.getQuantity());
                    link.setOriginalQuantity(entry.getOriginalQuantity());
                    
                    try{
                        link.setDbmsIsSouceOrderSupplied(STrnDpsUtilities.IsSourceOrderSupplied(miClient, oDpsSource, dpsSourceEntry));
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                     

                    entry.getDbmsDpsLinksAsDestiny().add(link);

                    for (SDataDpsEntryNotes notes : entry.getDbmsEntryNotes()) {
                        notes.setIsRegistryEdited(true);    // force original document entry notes to be attached to new document entry even if they are not edited
                        notes.setPkNotesId(0);
                    }

                    entry.calculateTotal(miClient, moFieldDate.getDate(), moDps.getFkTaxIdentityEmisorTypeId(), moDps.getFkTaxIdentityReceptorTypeId(), moFieldIsDiscountDocPercentage.getBoolean(), moFieldDiscountDocPercentage.getDouble(), moFieldExchangeRate.getDouble());
                    moPaneGridEntries.addTableRow(new SDataDpsEntryRow(entry, ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter()));
                    if (moPaneGridEntries.getTableGuiRowCount() > 0) {
                        updateEditForImportFromDps(false);
                    }
                    jcbFkCurrencyId.setEnabled(false);
                    jcbFkCurrencyId.setEnabled(false);
                    
                    if (mbIsOrd) {
                        if (entry.getContractPriceYear() > 0 && entry.getContractPriceMonth()> 0) {
                            moGuiDpsLink.addDataDpsDestinyEntry(entry);
                        }
                    }
                    
                    break;
                }
            }
        }
    }

    private erp.mtrn.data.SCfdParams createCfdParams() {
        String factura = "";
        String pedido = "";
        String contrato = "";
        String ruta = "";
        SDataDps dpsFactura = null;
        SDataDps dpsPedido = null;
        SDataDps dpsContrato = null;
        SDataCustomerBranchConfig cusBranchConfig = null;
        SCfdParams params = new SCfdParams();
        
        params.setReceptor(moBizPartner);
        params.setReceptorBranch(moBizPartnerBranch);
        params.setEmisor(miClient.getSessionXXX().getCompany().getDbmsDataCompany());

        if (moDps.getFkCompanyBranchId() == miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getPkBizPartnerBranchId()) {
            params.setLugarExpedicion(null);
        }
        else {
            params.setLugarExpedicion(miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsBizPartnerBranch(new int[] { moDps.getFkCompanyBranchId() }).getDbmsBizPartnerBranchAddressOfficial());
        }

        params.setUnidadPesoBruto(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.ITMU_TP_UNIT, new int[] { SDataConstantsSys.ITMU_TP_UNIT_MASS }, SLibConstants.DESCRIPTION_CODE));
        params.setUnidadPesoNeto(params.getUnidadPesoBruto());

        // Lookup for "pedido" (the first one found):

        for (SDataDpsEntry entryDocumento : moDps.getDbmsDpsEntries()) {
            if (entryDocumento.isAccountable()) {
                for (SDataDpsDpsLink linkPedido : entryDocumento.getDbmsDpsLinksAsDestiny()) {
                    if (!linkPedido.getDbmsIsSourceDeleted() && !linkPedido.getDbmsIsSourceEntryDeleted()) {
                        dpsPedido = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, linkPedido.getDbmsSourceDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
                        pedido = (dpsPedido.getNumberSeries().length() == 0 ? "" : dpsPedido.getNumberSeries() + "-") + dpsPedido.getNumber();

                        // Lookup for "contrato" (the first one found):

                        for (SDataDpsEntry entryPedido : dpsPedido.getDbmsDpsEntries()) {
                            if (!entryPedido.getIsDeleted()) {
                                for (SDataDpsDpsLink linkContrato : entryPedido.getDbmsDpsLinksAsDestiny()) {
                                    if (!linkContrato.getDbmsIsSourceDeleted() && !linkContrato.getDbmsIsSourceEntryDeleted()) {
                                        dpsContrato = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, linkContrato.getDbmsSourceDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
                                        contrato = (dpsContrato.getNumberSeries().length() == 0 ? "" : dpsContrato.getNumberSeries() + "-") + dpsContrato.getNumber();

                                        break;  // a "contrato" was found
                                    }
                                }
                            }

                            if (contrato.length() > 0) {
                                break;
                            }
                        }

                        break;  // a "pedido" was found
                    }
                }

                // Lookup for "factura" (the first one found):

                for (SDataDpsDpsAdjustment linkFactura : entryDocumento.getDbmsDpsAdjustmentsAsAdjustment()) {
                    if (!linkFactura.getDbmsIsDpsDeleted() && !linkFactura.getDbmsIsDpsEntryDeleted()) {
                        dpsFactura = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, linkFactura.getDbmsDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
                        factura = (dpsFactura.getNumberSeries().length() == 0 ? "" : dpsFactura.getNumberSeries() + "-") + dpsFactura.getNumber();

                        // Lookup for "pedido" (the first one found):

                        for (SDataDpsEntry entryFactura : dpsFactura.getDbmsDpsEntries()) {
                            if (!entryFactura.getIsDeleted()) {
                                for (SDataDpsDpsLink linkPedido : entryFactura.getDbmsDpsLinksAsDestiny()) {
                                    if (!linkPedido.getDbmsIsSourceDeleted() && !linkPedido.getDbmsIsSourceEntryDeleted()) {
                                        dpsPedido = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, linkPedido.getDbmsSourceDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
                                        pedido = (dpsPedido.getNumberSeries().length() == 0 ? "" : dpsPedido.getNumberSeries() + "-") + dpsPedido.getNumber();

                                        break;  // a "pedido" was found
                                    }
                                }
                            }

                            if (pedido.length() > 0) {
                                break;
                            }
                        }

                        break;  // a "factura" was found
                    }
                }
            }

            if (factura.length() > 0) {
                break;
            }

            if (pedido.length() > 0) {
                break;
            }
        }

        params.setFactura(factura);
        params.setPedido(pedido);
        params.setContrato(contrato);

        params.setTipoAddenda(moBizPartnerCategory.getFkCfdAddendaTypeId());
        params.setLorealFolioNotaRecepcion(moFieldFolioNotaRecepcion.getString());
        params.setBachocoSociedad(moFieldBachocoSociedad.getString());
        params.setBachocoOrganizacionCompra(moFieldBachocoOrganizacionCompra.getString());
        params.setBachocoDivision(moFieldBachocoDivision.getString());
        params.setModeloDpsDescripcion(moFieldDpsDescripcion.getString());

        params.setSorianaTienda(moFieldStore.getInteger());
        params.setSorianaEntregaMercancia(moFieldGoodsDelivery.getInteger());
        params.setSorianaFechaRemision(moFieldDateRemission.getDate());
        params.setSorianaFolioRemision(moFieldRemission.getString());
        params.setSorianaFolioPedido(moFieldSalesOrder.getString());
        params.setSorianaTipoBulto(moFieldBulkType.getInteger());
        params.setSorianaCantidadBulto(moFieldBulkQty.getDouble());
        params.setSorianaNotaEntradaFolio(moFieldNumberNoteIn.getString());
        if (jcbCfdAddendaSubtypeId.isEnabled()) {
            params.setCfdAddendaSubtype(moFieldCfdAddendaSubtypeId.getKeyAsIntArray()[0]);
        }
        else {
            params.setCfdAddendaSubtype(SLibConstants.UNDEFINED);
        }

        if (miClient.getSessionXXX().getParamsCompany().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32) {
            params.setRegimenFiscal(SLibUtilities.textExplode(miClient.getSessionXXX().getParamsCompany().getFiscalSettings(), ";"));            
        }
        else if (miClient.getSessionXXX().getParamsCompany().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) {
            params.setRegimenFiscal(SLibUtilities.textExplode(miClient.getSessionXXX().getParamsCompany().getTaxRegime(), ";"));            
        }

        if (isCfdNeeded()) {
            params.setAgregarAddenda(true);
        }
        else if (isCfdiNeeded()) {
            params.setAgregarAddenda(false);
        }

        // Ruta:

        cusBranchConfig = (SDataCustomerBranchConfig) SDataUtilities.readRegistry(miClient, SDataConstants.MKT_CFG_CUSB, new int[] { moBizPartnerBranch.getPkBizPartnerBranchId() }, SLibConstants.EXEC_MODE_SILENT);

        if (cusBranchConfig != null) {
            if (cusBranchConfig.getFkSalesRouteId() != 0) {
                ruta = "" + cusBranchConfig.getFkSalesRouteId();
            }
        }

        params.setRuta(ruta);

        // Miscellaneous:

        params.setInterestDelayRate(miClient.getSessionXXX().getParamsCompany().getInterestDelayRate());

        return params;
    }

    private boolean checkBizPartnerCreditLimitOk(final boolean canOmitCreditLimit, final boolean isDocBeingOpened) {
        boolean confirmOmission = true;
        boolean isCreditLimitOk = true;
        String msgCreditLimit = "";

        if (moFieldFkPaymentTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.TRNS_TP_PAY_CREDIT) {
            if (moBizPartnerCategory == null) {
                confirmOmission = false;
                msgCreditLimit = "No se ha accedido aún a la información de la categoría del asociado de negocios.";
            }
            else if (moBizPartnerCategory.getEffectiveCreditTypeId() == SDataConstantsSys.BPSS_TP_CRED_CRED_NO) {
                confirmOmission = false;
                msgCreditLimit = "El asociado de negocios no tiene crédito.";
            }
        }

        if (msgCreditLimit.isEmpty() && moBizPartnerCategory.getEffectiveCreditTypeId() != SDataConstantsSys.BPSS_TP_CRED_CRED_UNLIM) {
            double balance = obtainBizPartnerBalance();
            double balanceTotal = balance;
            boolean includeCurrentDoc = !isDocBeingOpened && moFieldFkPaymentTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.TRNS_TP_PAY_CREDIT;
            
            if (includeCurrentDoc) {
                balanceTotal += moDps.getTotal_r();
            }
            
            if (balanceTotal > moBizPartnerCategory.getEffectiveCreditLimit()) {
                msgCreditLimit = "El saldo actual del asociado de negocios " + SLibUtils.getDecimalFormatAmount().format(balance) + "\n";
                
                if (includeCurrentDoc) {
                    msgCreditLimit += "más el presente documento " + SLibUtils.getDecimalFormatAmount().format(moDps.getTotal_r()) + ", " + 
                            "igual a " + SLibUtils.getDecimalFormatAmount().format(balanceTotal) + ",\n";
                }
                
                msgCreditLimit +=
                    "es mayor a su límite de crédito de " + SLibUtils.getDecimalFormatAmount().format(moBizPartnerCategory.getEffectiveCreditLimit()) + " " +
                    "por " + SLibUtils.getDecimalFormatAmount().format(balanceTotal - moBizPartnerCategory.getEffectiveCreditLimit()) + ".";
            }
        }
        
        if (!msgCreditLimit.isEmpty()) {
            if (canOmitCreditLimit && confirmOmission) {
                if (miClient.showMsgBoxConfirm(msgCreditLimit + "\n" + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                    isCreditLimitOk = false;
                }
            }
            else {
                miClient.showMsgBoxWarning(msgCreditLimit);
                isCreditLimitOk = false;
            }
        }

        return isCreditLimitOk;
    }

    private boolean checkBizPartnerExpiredDocsOk(final boolean canOmitExpiredDocs, final boolean isDocBeingOpened) {
        long expiredDays = 0;
        boolean isExpiredDocsOk = true;
        String msgExpiredDocs = "";
        Date tCurrentDueDate = null;

        if (STrnUtilities.hasBizPartnerExpiredDocs(miClient, moBizPartner.getPkBizPartnerId(), moBizPartnerCategory.getPkBizPartnerCategoryId(), 
            miClient.getSession().getSystemDate(), moDps.getPkYearId(), moDps.getPkDocId())) {
            msgExpiredDocs = "El asociado de negocios tiene documentos vencidos.";
        }
        
        if (!isDocBeingOpened) {
            tCurrentDueDate = SLibTimeUtilities.addDate(moFieldDateStartCredit.getDate(), 0, 0, moFieldDaysOfCredit.getInteger() + moBizPartnerCategory.getEffectiveDaysOfGrace());
            expiredDays = SLibTimeUtilities.getDaysDiff(miClient.getSession().getSystemDate(), tCurrentDueDate);
            
            if (expiredDays > 0) {
                msgExpiredDocs += (msgExpiredDocs.isEmpty() ? "" : "\n") + "El documento actual está vencido por " + SLibUtils.DecimalFormatInteger.format(expiredDays) + " días.";
            }
        }
        
        if (!msgExpiredDocs.isEmpty()) {
            if (canOmitExpiredDocs) {
                if (miClient.showMsgBoxConfirm(msgExpiredDocs + "\n" + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                    isExpiredDocsOk = false;
                }
            }
            else {
                miClient.showMsgBoxWarning(msgExpiredDocs);
                isExpiredDocsOk = false;
            }
        }

        return isExpiredDocsOk;
    }

    private void actionDpsType() {
        SFormOptionPickerInterface picker = miClient.getOptionPicker(SDataConstants.TRNU_TP_DPS);

        picker.formReset();
        picker.setFilterKey(manDpsClassKey);
        picker.formRefreshOptionPane();
        picker.setSelectedPrimaryKey(moDpsType == null ? null : moDpsType.getPrimaryKey());
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            setDpsType((int[]) picker.getSelectedPrimaryKey());
            updateDpsWithDocSettings();
        }
    }

    private void actionBizPartner() {
        int[] key = null;

        if (moBizPartner != null) {
            key = new int[3];
            key[0] = moBizPartner.getPkBizPartnerId();
            if (moBizPartnerBranch != null) {
                key[1] = moBizPartnerBranch.getPkBizPartnerBranchId();
                if (moBizPartnerBranchAddress == null) {
                    key[2] = moBizPartnerBranchAddress.getPkAddressId();
                }
            }
        }

        moPickerBizPartner.formReset();
        moPickerBizPartner.setSelectedPrimaryKey(key);
        moPickerBizPartner.setFormVisible(true);

        if (moPickerBizPartner.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            key = (int[]) moPickerBizPartner.getSelectedPrimaryKey();
            setBizPartner(new int[] { key[0] }, new int[] { key[1] }, new int[] { key[1], key[2] });
        }
    }

    private void actionDate() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDate.getDate(), moFieldDate);
    }

    private void actionDateDoc() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateDoc.getDate(), moFieldDateDoc);
    }

    private void actionRecordManualSelect() {
        Object key = null;
        String message = "";

        moDialogRecordPicker.formReset();
        moDialogRecordPicker.setFilterKey(moFieldDate.getDate());
        moDialogRecordPicker.formRefreshOptionPane();
        moDialogRecordPicker.setSelectedPrimaryKey(moRecordUserKey);
        moDialogRecordPicker.setFormVisible(true);

        if (moDialogRecordPicker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            key = moDialogRecordPicker.getSelectedPrimaryKey();

            if (readRecordUser(key)) {
                if (moRecordUser != null) {
                    if (moRecordUser.getIsSystem()) {
                        message = "No puede seleccionarse esta póliza contable porque es de sistema.";
                    }
                    else if (moRecordUser.getIsAudited()) {
                        message = "No puede seleccionarse esta póliza contable porque ya está auditada.";
                    }
                    else if (moRecordUser.getIsAuthorized()) {
                        message = "No puede seleccionarse esta póliza contable porque ya está autorizada.";
                    }

                    if (message.length() > 0) {
                        miClient.showMsgBoxWarning(message);
                    }
                    else {
                        moRecordUserKey = key;
                        renderRecordManual();
                    }
                }
            }
        }
    }

    private void actionRecordManualView() {
        miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(SDataConstants.FINX_REC_RO, moRecordUserKey);
    }

    private void actionDateStartCredit() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateStartCredit.getDate(), moFieldDateStartCredit);
    }

    private void actionBizPartnerBalance() {
        double limit = 0;
        double balance = 0;
        String credit = "Estatus crediticio del ";

        if (moBizPartnerCategory == null) {
            credit += "asociado de negocios:";
            credit += "\n" + moBizPartner.getBizPartner();
            credit += "\n\nTipo de crédito: ¡No se ha accedido aún a la información de la categoría del asociado de negocios!";
        }
        else {
            credit += SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.BPSS_CT_BP, new int[] { moBizPartnerCategory.getPkBizPartnerCategoryId() }).toLowerCase() + ":";
            credit += "\n" + moBizPartner.getBizPartner();
            credit += "\n\nTipo de crédito: " + SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.BPSS_TP_CRED, new int[] { moBizPartnerCategory.getEffectiveCreditTypeId() }) + "";

            if (moBizPartnerCategory.getEffectiveCreditTypeId() == SDataConstantsSys.BPSS_TP_CRED_CRED_LIM) {
                limit = moBizPartnerCategory.getEffectiveCreditLimit();
                credit += "\nLímite de crédito: " + SLibUtils.getDecimalFormatAmount().format(limit);
            }
        }

        balance = obtainBizPartnerBalance();
        credit += "\nSaldo actual (" + moDps.getPkYearId() + "): " + SLibUtils.getDecimalFormatAmount().format(balance);
        if (moBizPartnerCategory.getEffectiveCreditTypeId() == SDataConstantsSys.BPSS_TP_CRED_CRED_LIM) {
            if (balance <= limit) {
                credit += "\nCrédito libre: " + SLibUtils.getDecimalFormatAmount().format(limit - balance);
            }
            else {
                credit += "\nCrédito excedido: " + SLibUtils.getDecimalFormatAmount().format(balance - limit);
            }
        }

        miClient.showMsgBoxInformation(credit);
    }

    private void actionFkCurrencyId() {
        miClient.pickOption(SDataConstants.CFGU_CUR, moFieldFkCurrencyId, null);
    }

    private void actionExchangeRate() {
        double rate = miClient.pickExchangeRate(moFieldFkCurrencyId.getKeyAsIntArray()[0], moFieldDate.getDate());

        if (rate != 0d) {
//            moFieldExchangeRateSystem.setFieldValue(rate);
            moFieldExchangeRate.setFieldValue(rate);
            jtfExchangeRate.requestFocus();
        }
    }

    private void actionComputeTotal() {
        calculateTotal();
    }

    private void actionPrepayments() {
        try {
            miClient.showMsgBoxInformation("Saldo global de anticipos totales del asociado de negocios en moneda local: $ " + SLibUtils.getDecimalFormatAmount().format(mdPrepayments) + " " + jtfCurrencySystemKeyRo.getText() + ".");
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private void actionEntryNew() {
        if (jbEntryNew.isEnabled() && mnFormStatus == SLibConstants.FORM_STATUS_EDIT) {
            SDataDpsEntry entry = null;

            updateDpsWithDocSettings();

            moFormEntry.formReset();
            moFormEntry.setValue(SDataConstants.TRN_DPS, moDps);
            moFormEntry.setValue(SDataConstants.BPSU_BP, moBizPartner);
            moFormEntry.setValue(SDataConstants.BPSU_BPB, moBizPartnerBranch);
            moFormEntry.setEnableDataAddenda(jTabbedPane.isEnabledAt(3));
            if (jcbTaxRegionId.getSelectedIndex() > 0) {
                moFormEntry.setValue(SDataConstants.FINU_TAX_REG, ((SFormComponentItem) jcbTaxRegionId.getSelectedItem()).getPrimaryKey());
            }

            moFormEntry.setFormVisible(true);

            if (moFormEntry.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                entry = (SDataDpsEntry) moFormEntry.getRegistry();

                moPaneGridEntries.addTableRow(new SDataDpsEntryRow(entry, ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter()));
                renderEntries();
                calculateTotal();
                moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
            }
        }
    }

    private void actionEntryEdit() {
        double dQuantityDes = 0;
        double dQuantityAdj = 0;
        double dQuantityPrc = 0;
        boolean isLastPrc = false;
        
        if (jbEntryEdit.isEnabled()) {
            SDataDpsEntry entry = null;
            SDataDpsEntry entryOld = null;
            SDataDps dpsSource = null;
            SDataDpsEntry entryAux = null;
            SDataDpsEntry entryComplementary = null;
            SDataDpsEntry entrySource = null;
            int index = moPaneGridEntries.getTable().getSelectedRow();
            boolean canEdit = false;

            if (index != -1) {
                entry = ((SDataDpsEntry) moPaneGridEntries.getTableRow(index).getData()).clone();
                entryOld = (SDataDpsEntry) moPaneGridEntries.getTableRow(index).getData();

                if (entry.getFkDpsEntryTypeId() != SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY) {
                    miClient.showMsgBoxWarning("Solamente pueden modificarse los detalles de tipo ordinario.");
                }
                else {
                    if (mnFormStatus == SLibConstants.FORM_STATUS_EDIT) {
                        canEdit = canEditEntry(entry);
                        updateDpsWithDocSettings();
                    }

                    moFormEntry.formReset();
                    moFormEntry.setValue(SDataConstants.TRN_DPS, moDps);
                    moFormEntry.setValue(SDataConstants.BPSU_BP, moBizPartner);
                    moFormEntry.setValue(SDataConstants.BPSU_BPB, moBizPartnerBranch);
                    moFormEntry.setEnableDataAddenda(jTabbedPane.isEnabledAt(3));
                    moFormEntry.setRegistry(entry);

                    if (mnFormStatus == SLibConstants.FORM_STATUS_READ_ONLY || !canEdit) {
                        // Dps entry cannot be edited:

                        moFormEntry.setFormStatus(SLibConstants.FORM_STATUS_READ_ONLY);
                    }
                    else {
                        // Dps entry can be edited, but restrictions can apply:

                        for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
                            entryAux = ((SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData()).clone();
                            if (entryAux.hasDpsLinksAsDestiny() || entryAux.hasDpsAdjustmentsAsAdjustment()) {
                                if (i != index) {
                                    if (entry.hasDpsLinksAsDestiny() && SLibUtilities.compareKeys(entryAux.getDbmsDpsLinksAsDestiny().get(0).getDbmsSourceDpsEntryKey(), entry.getDbmsDpsLinksAsDestiny().get(0).getDbmsSourceDpsEntryKey())) {
                                        dQuantityDes += entryAux.getQuantity();
                                    }
                                    if (entry.hasDpsAdjustmentsAsAdjustment() && SLibUtilities.compareKeys(entryAux.getDbmsDpsAdjustmentsAsAdjustment().get(0).getDbmsDpsEntryKey(), entry.getDbmsDpsAdjustmentsAsAdjustment().get(0).getDbmsDpsEntryKey())) {
                                        dQuantityAdj += entryAux.getQuantity();
                                    }
                                }
                            }
                        }
                        if (mbIsOrd) {
                            if (entryOld.getContractPriceYear() > 0 && entryOld.getContractPriceMonth() > 0) {                                 
                                for (SGuiDpsEntryPrice entryPrice : moGuiDpsLink.pickGuiDpsSourceEntry(entryOld.getDbmsDpsLinksAsDestiny().get(0).getDbmsSourceDpsKey(), entryOld.getDbmsDpsLinksAsDestiny().get(0).getDbmsSourceDpsEntryKey()).getGuiDpsSourceEntryPrices()) {
                                    dpsSource = moGuiDpsLink.pickGuiDpsSource(entryOld.getDbmsDpsLinksAsDestiny().get(0).getDbmsSourceDpsKey()).getDataDpsSource();
                                    entrySource = moGuiDpsLink.pickGuiDpsSourceEntry(entryOld.getDbmsDpsLinksAsDestiny().get(0).getDbmsSourceDpsKey(), entryOld.getDbmsDpsLinksAsDestiny().get(0).getDbmsSourceDpsEntryKey()).getDataDpsSourceEntry();
                
                                    if (entryOld.getContractPriceYear() == entryPrice.getDataDpsEntryPrice().getContractPriceYear() && entryOld.getContractPriceMonth() == entryPrice.getDataDpsEntryPrice().getContractPriceMonth()) {
                                        //dQuantityPrc = entryPrice.obtainQtyAvailable(entryOld) - (entryOld.getOriginalQuantity() * entryOld.getSurplusPercentage());
                                        dQuantityPrc = entryPrice.obtainQtyAvailable(entryOld);
                                        
                                        Date datePrice = null;
                                        Date dateSourceDocLapsing = null;
                                        
                                        datePrice = SLibTimeUtilities.createDate(entryPrice.getDataDpsEntryPrice().getContractPriceYear(), entryPrice.getDataDpsEntryPrice().getContractPriceMonth());
                                        dateSourceDocLapsing = SLibTimeUtilities.createDate(SLibTimeUtilities.digestYearMonth(dpsSource.getDateDocLapsing_n())[0], SLibTimeUtilities.digestYearMonth(dpsSource.getDateDocLapsing_n())[1]);
                                        if (datePrice.compareTo(dateSourceDocLapsing) == 0) {
                                            isLastPrc = true;
                                            dQuantityPrc += (entrySource.getQuantity() * entrySource.getSurplusPercentage());
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        try {
                            if (hasDpsLinksButIsEditable()) {
                                moFormEntry.hasDpsLinksButIsEditable();
                                moFormEntry.setQuantityLimit(dQuantityDes, dQuantityAdj, dQuantityPrc, isLastPrc);
                            }
                            else if (hasDpsAdjustmentsAsAdjButIsEditable()) {
                                moFormEntry.hasDpsAdjustmentsAsAdjButIsEditable();
                                moFormEntry.setQuantityLimit(dQuantityDes, dQuantityAdj, dQuantityPrc, isLastPrc);
                            }
                        }
                        catch (java.lang.Exception e) {
                            SLibUtilities.renderException(this, e);
                        }
                    }

                    if (mnFormStatus == SLibConstants.FORM_STATUS_EDIT && canEdit) {
                        moFormEntry.activateSurplusPercentage();    // at least, surplus percentage can be updated
                    }

                    moFormEntry.setFormVisible(true);

                    if (moFormEntry.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                        entry = (SDataDpsEntry) moFormEntry.getRegistry();

                        if (entry.getIsDiscountRetailChain()) {
                            // Edit aswell complementary dps entry:

                            entryComplementary = entry.getDbmsDpsAdjustmentsAsAdjustment().get(0).getAuxDpsEntryComplementary();
                            entryComplementary.setIsRegistryEdited(true);
                            entryComplementary.setFkUserEditId(miClient.getSession().getUser().getPkUserId());

                            prepareDpsEntryComplementary(entry, entryComplementary);
                        }

                        moPaneGridEntries.setTableRow(new SDataDpsEntryRow(entry, ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter()), index);
                        renderEntries();
                        calculateTotal();
                        moPaneGridEntries.setTableRowSelection(index < moPaneGridEntries.getTableGuiRowCount() ? index : moPaneGridEntries.getTableGuiRowCount() - 1);
                        
                        if (mbIsOrd) {
                            if (entry.getContractPriceYear() > 0 && entry.getContractPriceMonth()> 0) {                                 
                                for (SGuiDpsEntryPrice entryPrice : moGuiDpsLink.pickGuiDpsSourceEntry(entry.getDbmsDpsLinksAsDestiny().get(0).getDbmsSourceDpsKey(), entry.getDbmsDpsLinksAsDestiny().get(0).getDbmsSourceDpsEntryKey()).getGuiDpsSourceEntryPrices()) {
                                    if (entry.getContractPriceYear() == entryPrice.getDataDpsEntryPrice().getContractPriceYear() && entry.getContractPriceMonth() == entryPrice.getDataDpsEntryPrice().getContractPriceMonth()) {
                                        entryPrice.updateDataDpsDestinyEntry(entryOld, entry);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void actionEntryDelete() {
        if (jbEntryDelete.isEnabled() && mnFormStatus == SLibConstants.FORM_STATUS_EDIT) {
            SDataDpsEntry entry = null;
            SDataDpsEntry entryComplementary = null;
            int index = moPaneGridEntries.getTable().getSelectedRow();

            if (index != -1) {
                entry = (SDataDpsEntry) moPaneGridEntries.getTableRow(index).getData();

                if (entry.getFkDpsEntryTypeId() != SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY) {
                    miClient.showMsgBoxWarning("Solamente pueden modificarse los detalles de tipo ordinario.");
                }
                else if (entry.getIsRegistryNew() || canDeleteEntry(entry)) {
                    if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                        if (entry.getIsDeleted()) {
                            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_REG_ALREADY_DELETE);
                        }
                        else {
                            if (mbIsOrd) {
                                if (entry.getContractPriceYear() > 0 && entry.getContractPriceMonth() > 0) {                                 
                                    for (SGuiDpsEntryPrice entryPrice : moGuiDpsLink.pickGuiDpsSourceEntry(entry.getDbmsDpsLinksAsDestiny().get(0).getDbmsSourceDpsKey(), entry.getDbmsDpsLinksAsDestiny().get(0).getDbmsSourceDpsEntryKey()).getGuiDpsSourceEntryPrices()) {
                                        if (entry.getContractPriceYear() == entryPrice.getDataDpsEntryPrice().getContractPriceYear() && entry.getContractPriceMonth() == entryPrice.getDataDpsEntryPrice().getContractPriceMonth()) {
                                            entryPrice.removeDataDpsDestinyEntry(entry);
                                            break;
                                        }
                                    }
                                }
                            }
                            
                            if (entry.getIsRegistryNew()) {
                                moPaneGridEntries.removeTableRow(index);
                            }
                            else {
                                if (entry.getIsDiscountRetailChain()) {
                                    // Delete aswell complementary dps entry:

                                    entryComplementary = entry.getDbmsDpsAdjustmentsAsAdjustment().get(0).getAuxDpsEntryComplementary();
                                    entryComplementary.setIsDeleted(true);
                                    entryComplementary.setFkUserDeleteId(miClient.getSession().getUser().getPkUserId());
                                    entryComplementary.setIsRegistryEdited(true);
                                    entryComplementary.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
                                }

                                entry.setIsDeleted(true);
                                entry.setFkUserDeleteId(miClient.getSession().getUser().getPkUserId());
                                entry.setIsRegistryEdited(true);
                                entry.setFkUserEditId(miClient.getSession().getUser().getPkUserId());

                                moPaneGridEntries.setTableRow(new SDataDpsEntryRow(entry, ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter()), index);
                            }
                        }

                        renderEntries();
                        calculateTotal();
                        moPaneGridEntries.setTableRowSelection(index < moPaneGridEntries.getTableGuiRowCount() ? index : moPaneGridEntries.getTableGuiRowCount() - 1);
                        if (moPaneGridEntries.getTableGuiRowCount() == 0) {
                            updateEditForImportFromDps(true);
                        }
                    }
                }
            }
        }
    }

    private void actionEntryFilter() {
        if (jtbEntryFilter.isEnabled()) {
            int index = moPaneGridEntries.getTable().getSelectedRow();

            moPaneGridEntries.setGridViewStatus(!jtbEntryFilter.isSelected() ? STableConstants.VIEW_STATUS_ALL : STableConstants.VIEW_STATUS_ALIVE);
            moPaneGridEntries.renderTableRows();
            moPaneGridEntries.setTableRowSelection(index < moPaneGridEntries.getTableGuiRowCount() ? index : moPaneGridEntries.getTableGuiRowCount() - 1);
        }
    }

    private void actionEntryDiscountRetailChain(final erp.mtrn.data.SDataDps poDpsSource) {
        if (jbEntryDiscountRetailChain.isEnabled() && mnFormStatus == SLibConstants.FORM_STATUS_EDIT) {
            SDataDps oDpsSource = poDpsSource;
            SDataDpsEntry oDpsEntryAdjustment = null;
            SDataDpsEntry oDpsEntryComplementary = null;

            // 1. Pick source DPS:

            updateDpsWithDocSettings();
            moDialogPickerDps.formReset();
            moDialogPickerDps.setFilterKey(new Object[] { manDpsClassPreviousKey, moBizPartner.getPrimaryKey() });
            moDialogPickerDps.formRefreshOptionPane();
            moDialogPickerDps.setSelectedPrimaryKey(oDpsSource == null ? null : oDpsSource.getPrimaryKey());
            moDialogPickerDps.setFormVisible(true);

            if (moDialogPickerDps.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                oDpsSource = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moDialogPickerDps.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);

                // A.1. Validate that source DPS can be used:

                if (moDps.getFkCurrencyId() != oDpsSource.getFkCurrencyId()) {
                    miClient.showMsgBoxWarning("El documento a ajustar y este documento deben coincidir en: moneda del documento.");
                }
                else if (oDpsSource.getDate().after(moFieldDate.getDate())) {
                    miClient.showMsgBoxWarning("La fecha del documento a ajustar (" + SLibUtils.DateFormatDate.format(oDpsSource.getDate()) + ") " +
                            "no puede ser posterior a la fecha de este documento (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ").");
                }
                else {
                    updateDpsWithDocSettings();

                    moFormEntry.formReset();
                    moFormEntry.setValue(SDataConstants.TRN_DPS, moDps);
                    moFormEntry.setValue(SDataConstants.BPSU_BP, moBizPartner);
                    moFormEntry.setValue(SDataConstants.BPSU_BPB, moBizPartnerBranch);
                    moFormEntry.setEnableDataAddenda(jTabbedPane.isEnabledAt(3));
                    moFormEntry.setValue(SDataConstants.TRNS_TP_DPS_ADJ, SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC);
                    if (jcbTaxRegionId.getSelectedIndex() > 0) {
                        moFormEntry.setValue(SDataConstants.FINU_TAX_REG, ((SFormComponentItem) jcbTaxRegionId.getSelectedItem()).getPrimaryKey());
                    }

                    moFormEntry.activateDiscountRetailChain();
                    moFormEntry.setFormVisible(true);

                    if (moFormEntry.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                        oDpsEntryAdjustment = (SDataDpsEntry) moFormEntry.getRegistry();
                        oDpsEntryAdjustment.setIsDiscountRetailChain(true);
                        oDpsEntryAdjustment.setFkDpsAdjustmentTypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_DISC_DISC[0]);
                        oDpsEntryAdjustment.setFkDpsAdjustmentSubtypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_DISC_DISC[1]);
                        oDpsEntryAdjustment.setAuxPkDpsYearId(oDpsSource.getPkYearId());
                        oDpsEntryAdjustment.setAuxPkDpsDocId(oDpsSource.getPkDocId());

                        // Create affected DPS's virtual entry:

                        oDpsEntryComplementary = new SDataDpsEntry();
                        oDpsEntryComplementary.setPkYearId(oDpsSource.getPkYearId());
                        oDpsEntryComplementary.setPkDocId(oDpsSource.getPkDocId());
                        oDpsEntryComplementary.setPkEntryId(0);
                        oDpsEntryComplementary.setIsRegistryNew(true);

                        prepareDpsEntryComplementary(oDpsEntryAdjustment, oDpsEntryComplementary);

                        moPaneGridEntries.addTableRow(new SDataDpsEntryRow(oDpsEntryAdjustment, ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter()));
                        renderEntries();
                        calculateTotal();
                        moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);

                        moLastDpsSource = oDpsSource;
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void actionEntryImportFromDps(final erp.mtrn.data.SDataDps poDpsSource) {
        boolean bContinue = true;
        boolean reqMsg = false;
        if (jbEntryImportFromDps.isEnabled() && mnFormStatus == SLibConstants.FORM_STATUS_EDIT) {
            int i = 0;
            int[] adjustmentSubtypeKey = null;
            SDataDps oDpsSource = poDpsSource;
            SDataItem oItem = null;

            // If this document is an "adjustment", validate that some type of adjustment has been selected:

            if (mbIsAdj) {
                if (jcbAdjustmentSubtypeId.getSelectedIndex() <= 0) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + " '" + jlAdjustmentSubtypeId.getText() + "'.");
                    jcbAdjustmentSubtypeId.requestFocus();
                    return;
                }
                else {
                    adjustmentSubtypeKey = (int[]) ((SFormComponentItem) jcbAdjustmentSubtypeId.getSelectedItem()).getPrimaryKey();
                }
            }

            // Validate if addenda is needed in entries:

            switch(moBizPartnerCategory.getFkCfdAddendaTypeId()) {
                case SDataConstantsSys.BPSS_TP_CFD_ADD_NA:
                    break;
                case SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA:
                    mbIsAddendaNeeded = true;
                    break;
                case SDataConstantsSys.BPSS_TP_CFD_ADD_LOREAL:
                    mbIsAddendaNeeded = true;
                    break;
                case SDataConstantsSys.BPSS_TP_CFD_ADD_BACHOCO:
                    mbIsAddendaNeeded = true;
                    break;
                case SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA:
                    mbIsAddendaNeeded = true;
                    break;
                default:
            }

            /*
             * IMPORTING DOCUMENT ENTRIES FROM OTHER DOCUMENTS:
             *
             * If this document is an "order" or a "document", import means to link.
             * If this document is an "adjustment", import means to adjust.
             */

            // 1. Pick source DPS:

            if (oDpsSource == null) {
                updateDpsWithDocSettings();
                updateDateForOrderPrevious();
                moDialogPickerDps.formReset();
                moDialogPickerDps.setFilterKey(new Object[] { manDpsClassPreviousKey, moBizPartner.getPrimaryKey() });
                moDialogPickerDps.formRefreshOptionPane();
                moDialogPickerDps.setFormVisible(true);

                if (moDialogPickerDps.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    oDpsSource = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moDialogPickerDps.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
                }
            }
            else {
                oDpsSource.setIsCopied(true);
            }

            if (oDpsSource != null) {
                if (STrnDpsUtilities.isDpsAuthorized(miClient, oDpsSource)) {
                    if (mbIsOrd || mbIsDoc) {
                        // A.1. Validate that source DPS can be used:

                        if (moDps.getFkBizPartnerBranchId() != oDpsSource.getFkBizPartnerBranchId()) {
                            miClient.showMsgBoxWarning("El documento de origen y destino deben coincidir en: sucursal asociado.");
                        }
                        else if (moDps.getFkCurrencyId() != oDpsSource.getFkCurrencyId() && moDps.getFkCurrencyId() != miClient.getSessionXXX().getParamsErp().getFkCurrencyId()) {
                            miClient.showMsgBoxWarning("El documento de origen y destino deben coincidir en: moneda del documento.");
                        }
                        else if (moDps.getIsDiscountDocApplying() != oDpsSource.getIsDiscountDocApplying()) {
                            miClient.showMsgBoxWarning("El documento de origen y destino deben coincidir en: si aplica o no el descuento en el documento.");
                        }
                        else if (moDps.getIsDiscountDocPercentage() != oDpsSource.getIsDiscountDocPercentage()) {
                            miClient.showMsgBoxWarning("El documento de origen y destino deben coincidir en: si es porcentaje o no el descuento en el documento.");
                        }
                        else if (moDps.getDiscountDocPercentage() != oDpsSource.getDiscountDocPercentage()) {
                            miClient.showMsgBoxWarning("El documento de origen y destino deben coincidir en: el porcentaje de descuento en el documento.");
                        }
                        else if (moDps.getDiscountDocPercentage() != oDpsSource.getDiscountDocPercentage()) {
                            miClient.showMsgBoxWarning("El documento de origen y destino deben coincidir en: el porcentaje de descuento en el documento.");
                        }
                        /*
                        else if (mbValidateLinkPeriod && oDpsSource.getDateDocDelivery_n() != null && moFieldDate.getDate().before(oDpsSource.getDateDocDelivery_n())) {
                            miClient.showMsgBoxWarning("La fecha del documento (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ") " +
                                    "no puede ser anterior a la fecha entrega programada del documento de origen (" + SLibUtils.DateFormatDate.format(oDpsSource.getDateDocDelivery_n()) + ").");
                        }
                        else if (mbValidateLinkPeriod && oDpsSource.getDateDocLapsing_n() != null && moFieldDate.getDate().after(oDpsSource.getDateDocLapsing_n())) {
                            miClient.showMsgBoxWarning("La fecha del documento (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ") " +
                                    "no puede ser posterior a la fecha última entrega del documento de origen (" + SLibUtils.DateFormatDate.format(oDpsSource.getDateDocLapsing_n()) + ").");
                        }
                        */
                        else if (oDpsSource.getDate().after(moFieldDate.getDate())) {
                            miClient.showMsgBoxWarning("La fecha del documento de origen (" + SLibUtils.DateFormatDate.format(oDpsSource.getDate()) + ") " +
                                    "no puede ser posterior a la fecha de este documento (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ").");
                        }
                        else {
                            if (moDps.getFkCurrencyId() != oDpsSource.getFkCurrencyId() && moDps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getFkCurrencyId()) {
                                if (miClient.showMsgBoxConfirm("Se cargará el valor del documento en moneda local,\n ¿Desea continuar?") == JOptionPane.YES_OPTION) {
                                    mbIsLocalCurrency = true;
                                }
                                else {
                                    miClient.showMsgBoxWarning("El documento de origen y destino deben coincidir en: moneda del documento.");
                                    mbIsLocalCurrency = false;
                                    bContinue = false;
                                }
                            }
                            
                            if (bContinue && mbValidateLinkPeriod && oDpsSource.getDateDocDelivery_n() != null && moFieldDate.getDate().before(oDpsSource.getDateDocDelivery_n())) {
                                if (miClient.showMsgBoxConfirm("La fecha del documento (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ") "
                                        + "es anterior a la fecha entrega programada del documento de origen (" + SLibUtils.DateFormatDate.format(oDpsSource.getDateDocDelivery_n()) +"),\n ¿Desea continuar?") == JOptionPane.NO_OPTION) {
                                    miClient.showMsgBoxWarning("La fecha del documento (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ") "
                                        + "es anterior a la fecha entrega programada del documento de origen (" + SLibUtils.DateFormatDate.format(oDpsSource.getDateDocDelivery_n()) +").");
                                    bContinue = false;
                                }
                            }
                            
                            if (bContinue && mbValidateLinkPeriod && oDpsSource.getDateDocLapsing_n() != null && moFieldDate.getDate().after(oDpsSource.getDateDocLapsing_n())) {
                                if (miClient.showMsgBoxConfirm("La fecha del documento (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ") "
                                        + "es posterior a la fecha última entrega del documento de origen (" + SLibUtils.DateFormatDate.format(oDpsSource.getDateDocLapsing_n()) +"),\n ¿Desea continuar?") == JOptionPane.NO_OPTION) {
                                    miClient.showMsgBoxWarning("La fecha del documento (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ") "
                                        + "es posterior a la fecha última entrega del documento de origen (" + SLibUtils.DateFormatDate.format(oDpsSource.getDateDocLapsing_n()) +").");
                                    bContinue = false;
                                }
                            }
                            

                            if (bContinue) {
                                // A.2. Remove from just picked source DPS all registries linked to current DPS:

                                for (SDataDpsEntry dspSourceEntry : oDpsSource.getDbmsDpsEntries()) {
                                    if (dspSourceEntry.getIsPriceConfirm()) {
                                        reqMsg = true;
                                    }
                                    for (i = 0; i < dspSourceEntry.getDbmsDpsLinksAsSource().size(); ) {                                        
                                        if (!SLibUtilities.compareKeys(moDps.getPrimaryKey(), dspSourceEntry.getDbmsDpsLinksAsSource().get(i).getDbmsDestinyDpsKey())) {
                                            i++;
                                        }
                                        else {
                                            dspSourceEntry.getDbmsDpsLinksAsSource().removeElementAt(i);
                                        }
                                    }
                                }

                                // A.3. Add to just picked source DPS all registries linked to current DPS:

                                for (i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
                                    SDataDpsEntry dpsEntry = (SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData();

                                    for (SDataDpsDpsLink link : dpsEntry.getDbmsDpsLinksAsDestiny()) {

                                        // Find all link registries that belogs to just picked source DPS:

                                        if (SLibUtilities.compareKeys(oDpsSource.getPrimaryKey(), link.getDbmsSourceDpsKey())) {

                                            for (SDataDpsEntry dpsSourceEntry : oDpsSource.getDbmsDpsEntries()) {

                                                // Lookup entry in just picked source DPS and add link registry:

                                                if (SLibUtilities.compareKeys(link.getDbmsSourceDpsEntryKey(), dpsSourceEntry.getPrimaryKey())) {
                                                    dpsSourceEntry.getDbmsDpsLinksAsSource().add(link);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }

                                // A.4 Import entries:
                                if (reqMsg) {
                                    miClient.showMsgBoxInformation("Importante: confirmar precios mensuales del documento.");
                                }

                                moDialogDpsLink.formReset();
                                if (mbIsOrd) {
                                    if (moGuiDpsLink == null) {
                                        moGuiDpsLink = new SGuiDpsLink(miClient);
                                        moGuiDpsLink.addDataDpsDestiny(moDps);
                                        moGuiDpsLink.addDataDpsSource(oDpsSource);
                                    }
                                    else {
                                        moGuiDpsLink.addDataDpsSource(oDpsSource);
                                    }
                                    moDialogDpsLink.setValue(SDataConstants.TRN_DPS_ETY_PRC, moGuiDpsLink);
                                }
                                moDialogDpsLink.setValue(SDataConstants.TRNX_DPS_SRC, oDpsSource);
                                moDialogDpsLink.setValue(SDataConstants.TRNX_DPS_DES, moDps.getPrimaryKey());
                                moDialogDpsLink.setValue(SDataConstants.TRNS_CL_DPS, mbIsOrd);
                                moDialogDpsLink.setFormVisible(true);

                                // Complement addenda entries:

                                if (moDialogDpsLink.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                    Vector<SDataDpsEntry> dpsSourceEntries = new Vector<SDataDpsEntry>();

                                    updateDpsWithDocSettings();
                                    updateDateForOrderPrevious();
                                    
                                    jcbFkPaymentTypeId.setEnabled(!(oDpsSource.getIsCopied() || moDps.getIsCopied()));
                                    jtfDaysOfCredit.setEditable(!(oDpsSource.getIsCopied() || moDps.getIsCopied()));
                                    moDps.setIsCopied(oDpsSource.getIsCopied() || moDps.getIsCopied());
                                    moDps.setFkSourceYearId_n(moDps.getFkSourceYearId_n() != SLibConstants.UNDEFINED ? moDps.getFkSourceYearId_n() : oDpsSource.getPkYearId());
                                    moDps.setFkSourceDocId_n(moDps.getFkSourceDocId_n() != SLibConstants.UNDEFINED ? moDps.getFkSourceDocId_n() : oDpsSource.getPkDocId());

                                    if (mbIsAddendaNeeded) {
                                        for (SDataEntryDpsDpsLink entryLink : (Vector<SDataEntryDpsDpsLink>) moDialogDpsLink.getValue(SDataConstants.TRNX_DPS_DES)) {
                                            for (SDataDpsEntry dpsSourceEntry : oDpsSource.getDbmsDpsEntries()) {
                                                if (SLibUtilities.compareKeys(entryLink.getDpsEntryKey(), dpsSourceEntry.getPrimaryKey())) {

                                                    if (mbIsAddendaNeeded) {
                                                        moFormEntry.formReset();
                                                        moFormEntry.setValue(SDataConstants.TRN_DPS, moDps);
                                                        moFormEntry.setValue(SDataConstants.BPSU_BP, moBizPartner);
                                                        moFormEntry.setValue(SDataConstants.BPSU_BPB, moBizPartnerBranch);
                                                        moFormEntry.setEnableDataAddenda(jTabbedPane.isEnabledAt(3));
                                                        moFormEntry.setRegistry(dpsSourceEntry.clone());

                                                        moFormEntry.setFormVisible(true);

                                                        if (moFormEntry.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                                            dpsSourceEntries.add((SDataDpsEntry) moFormEntry.getRegistry());
                                                            mnCountEntries ++;
                                                        }
                                                        else {
                                                            mbIsAddendaNeeded = false;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        for (SDataEntryDpsDpsLink entryLink : (Vector<SDataEntryDpsDpsLink>) moDialogDpsLink.getValue(SDataConstants.TRNX_DPS_DES)) {
                                            for (SDataDpsEntry dpsSourceEntry : oDpsSource.getDbmsDpsEntries()) {
                                                if (SLibUtilities.compareKeys(entryLink.getDpsEntryKey(), dpsSourceEntry.getPrimaryKey())) {
                                                    dpsSourceEntry.setAuxPkDpsEntryPrice(null);
                                                    dpsSourceEntry.getDbmsEntryPrices().clear();
                                                    dpsSourceEntry.setContractPriceYear(SLibConstants.UNDEFINED);
                                                    dpsSourceEntry.setContractPriceMonth(SLibConstants.UNDEFINED);
                                                    dpsSourceEntry.setIsPriceVariable(false);
                                                    dpsSourceEntry.setIsPriceConfirm(false);
                                                    dpsSourceEntry.setContractBase(0d);
                                                    dpsSourceEntry.setContractFuture(0d);
                                                    dpsSourceEntry.setContractFactor(0d);
                                                    
                                                    if (entryLink.getAuxSGuiDpsEntryPrice() != null) {
                                                        dpsSourceEntry.setAuxPkDpsEntryPrice((int[]) entryLink.getAuxSGuiDpsEntryPrice().getDataDpsEntryPrice().getPrimaryKey());
                                                        dpsSourceEntry.setContractPriceYear(entryLink.getAuxSGuiDpsEntryPrice().getDataDpsEntryPrice().getContractPriceYear());
                                                        dpsSourceEntry.setContractPriceMonth(entryLink.getAuxSGuiDpsEntryPrice().getDataDpsEntryPrice().getContractPriceMonth());
                                                        /* jbarajas 18/08/2015 Bug in edition of the orders sales.
                                                        dpsSourceEntry.setAuxPkDpsEntryPrice((int[]) entryLink.getAuxSGuiDpsEntryPrice().getDataDpsEntryPrice().getPrimaryKey());
                                                        dpsSourceEntry.getDbmsEntryPrices().clear();
                                                        dpsSourceEntry.setContractPriceYear(entryLink.getAuxSGuiDpsEntryPrice().getDataDpsEntryPrice().getContractPriceYear());
                                                        dpsSourceEntry.setContractPriceMonth(entryLink.getAuxSGuiDpsEntryPrice().getDataDpsEntryPrice().getContractPriceMonth());
                                                        dpsSourceEntry.setIsPriceVariable(false);
                                                        dpsSourceEntry.setIsPriceConfirm(false);
                                                        dpsSourceEntry.setContractBase(0d);
                                                        dpsSourceEntry.setContractFuture(0d);
                                                        dpsSourceEntry.setContractFactor(0d);
                                                        */
                                                        if(jtfNumberReference.getText().isEmpty()) {
                                                            jtfNumberReference.setText(entryLink.getAuxSGuiDpsEntryPrice().getDataDpsEntryPrice().getReferenceNumber());
                                                        }
                                                        double price = 0.0;
                                                        if(entryLink.getAuxSGuiDpsEntryPrice().getDataDpsEntryPrice().getIsPriceVariable() && entryLink.getAuxSGuiDpsEntryPrice().getDataDpsEntryPrice().getOriginalPriceUnitaryCy() == 0d) {
                                                            double conversionFactor = ((SSessionCustom) miClient.getSession().getSessionCustom()).getUnitsFactorForQuantity(0, SModSysConsts.ITMU_UNIT_KG, SModSysConsts.ITMU_UNIT_LB);
                                                            double conversionOriginalQuantity = ((SSessionCustom) miClient.getSession().getSessionCustom()).getUnitsFactorForQuantity(dpsSourceEntry.getFkItemId(), SModSysConsts.ITMU_UNIT_MT_TON, dpsSourceEntry.getFkOriginalUnitId());
                                                            price = STrnUtilities.calculateDpsEntryPriceUnitary(entryLink.getAuxSGuiDpsEntryPrice().getDataDpsEntryPrice().getContractBase() , entryLink.getAuxSGuiDpsEntryPrice().getDataDpsEntryPrice().getContractFuture(), conversionFactor, entryLink.getAuxSGuiDpsEntryPrice().getDataDpsEntryPrice().getContractFactor(), conversionOriginalQuantity);
                                                        }
                                                        else {
                                                            price = entryLink.getAuxSGuiDpsEntryPrice().getDataDpsEntryPrice().getOriginalPriceUnitaryCy();
                                                        }
                                                        dpsSourceEntry.setOriginalPriceUnitaryCy(price);
                                                    }                                                    
                                                    dpsSourceEntries.add(dpsSourceEntry);
                                                }
                                            }
                                        }
                                    }

                                    if (!dpsSourceEntries.isEmpty()) {
                                        if (mnCountEntries > 0) {
                                            if (miClient.showMsgBoxConfirm("¿Desea agregar las " + mnCountEntries + " partidas importadas?") == JOptionPane.YES_OPTION) {
                                                prepareDpsEntryImport(dpsSourceEntries, oDpsSource, oItem);
                                            }
                                        }
                                        else {
                                            prepareDpsEntryImport(dpsSourceEntries, oDpsSource, oItem);
                                        }

                                        renderEntries();
                                        calculateTotal();
                                        moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
                                    }

                                    mnCountEntries = 0;
                                }
                            }
                        }
                    }
                    else if (mbIsAdj) {
                        // B.1. Validate that source DPS can be used:

                        if (moDps.getFkCurrencyId() != oDpsSource.getFkCurrencyId()) {
                            miClient.showMsgBoxWarning("El documento a ajustar y este documento deben coincidir en: moneda del documento.");
                        }
                        else if (oDpsSource.getDate().after(moFieldDate.getDate())) {
                            miClient.showMsgBoxWarning("La fecha del documento a ajustar (" + SLibUtils.DateFormatDate.format(oDpsSource.getDate()) + ") " +
                                    "no puede ser posterior a la fecha de este documento (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ").");
                        }
                        else {
                            // B.2. Remove from just picked source DPS all adjustment registries linked to current DPS:

                            for (SDataDpsEntry dpsSourceEntry : oDpsSource.getDbmsDpsEntries()) {
                                for (i = 0; i < dpsSourceEntry.getDbmsDpsAdjustmentsAsDps().size(); ) {
                                    if (!SLibUtilities.compareKeys(moDps.getPrimaryKey(), dpsSourceEntry.getDbmsDpsAdjustmentsAsDps().get(i).getDbmsDpsAdjustmentKey())) {
                                        i++;
                                    }
                                    else {
                                        dpsSourceEntry.getDbmsDpsAdjustmentsAsDps().removeElementAt(i);
                                    }
                                }
                            }

                            // B.3. Add to just picked source DPS all adjustment registries linked to current DPS:

                            for (i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
                                SDataDpsEntry dpsEntry = (SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData();

                                for (SDataDpsDpsAdjustment adjustment : dpsEntry.getDbmsDpsAdjustmentsAsAdjustment()) {

                                    // Find all adjustment registries that belogs to just picked source DPS:

                                    if (SLibUtilities.compareKeys(oDpsSource.getPrimaryKey(), adjustment.getDbmsDpsKey())) {

                                        for (SDataDpsEntry dpsSourceEntry : oDpsSource.getDbmsDpsEntries()) {

                                            // Lookup entry in just picked source DPS and add adjustment registry:

                                            if (SLibUtilities.compareKeys(adjustment.getDbmsDpsEntryKey(), dpsSourceEntry.getPrimaryKey())) {
                                                dpsSourceEntry.getDbmsDpsAdjustmentsAsDps().add(adjustment);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            // B.4 Import entries:

                            moDialogDpsAdjustment.formRefreshCatalogues();
                            moDialogDpsAdjustment.formReset();
                            moDialogDpsAdjustment.setValue(SDataConstants.TRNX_DPS_SRC, oDpsSource);
                            moDialogDpsAdjustment.setValue(SDataConstants.TRNX_DPS_DES, moDps.getPrimaryKey());
                            moDialogDpsAdjustment.setValue(SDataConstants.TRNS_STP_DPS_ADJ, adjustmentSubtypeKey);
                            moDialogDpsAdjustment.setFormVisible(true);

                            if (moDialogDpsAdjustment.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                double subtotalFactor = 0;

                                for (SDataEntryDpsDpsAdjustment entryAdjustment : (Vector<SDataEntryDpsDpsAdjustment>) moDialogDpsAdjustment.getValue(SDataConstants.TRNX_DPS_DES)) {
                                    if (adjustmentSubtypeKey[0] == SDataConstantsSys.TRNS_TP_DPS_ADJ_RET) {
                                        // Entry devolution:

                                        for (SDataDpsEntry dpsSourceEntry : oDpsSource.getDbmsDpsEntries()) {
                                            if (SLibUtilities.compareKeys(entryAdjustment.getDpsEntryKey(), dpsSourceEntry.getPrimaryKey())) {
                                                SDataDpsEntry entry = null;
                                                SDataDpsDpsAdjustment adjustment = null;

                                                subtotalFactor = dpsSourceEntry.getTotalCy_r() == 0 ? 0 : dpsSourceEntry.getSubtotalCy_r() / dpsSourceEntry.getTotalCy_r();

                                                entry = new SDataDpsEntry();
                                                entry.setPkYearId(moDps.getPkYearId());
                                                entry.setPkDocId(moDps.getPkDocId());
                                                entry.setPkEntryId(0);
                                                entry.setIsRegistryNew(true);

                                                entry.setConceptKey(dpsSourceEntry.getConceptKey());
                                                entry.setConcept(dpsSourceEntry.getConcept());
                                                entry.setOriginalQuantity(entryAdjustment.getQuantityToReturn());
                                                entry.setOriginalPriceUnitaryCy(entryAdjustment.getQuantityToReturn() == 0 ? 0 : entryAdjustment.getAmountToReturn() / entryAdjustment.getQuantityToReturn() * subtotalFactor);
                                                entry.setOriginalPriceUnitarySystemCy(entryAdjustment.getQuantityToReturn() == 0 ? 0 : entryAdjustment.getAmountToReturn() / entryAdjustment.getQuantityToReturn() * subtotalFactor);
                                                entry.setOriginalDiscountUnitaryCy(0);
                                                entry.setOriginalDiscountUnitarySystemCy(0);
                                                
                                                if (dpsSourceEntry.getFkUnitId() != dpsSourceEntry.getFkOriginalUnitId()){
                                                    SDataUnit origUnit = null;
                                                    SDataUnit sysUnit = null;
                                                    
                                                    sysUnit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { dpsSourceEntry.getFkUnitId() }, SLibConstants.EXEC_MODE_VERBOSE);
                                                    origUnit  = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { dpsSourceEntry.getFkOriginalUnitId() }, SLibConstants.EXEC_MODE_VERBOSE);
                                                    
                                                    if (origUnit.getFkUnitTypeId() != sysUnit.getFkUnitTypeId()){
                                                        oItem = null;
                                                        
                                                        oItem = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { dpsSourceEntry.getFkItemId() }, SLibConstants.EXEC_MODE_VERBOSE);
                                                        if (oItem.getFkUnitAlternativeTypeId() != SDataConstantsSys.ITMU_TP_UNIT_NA && oItem.getUnitAlternativeBaseEquivalence() == 0) {
                                                            entry.setAuxPreserveQuantity(true);
                                                            entry.setQuantity(dpsSourceEntry.getQuantity() * entry.getOriginalQuantity() / dpsSourceEntry.getOriginalQuantity());
                                                        }
                                                    }
                                                }
                                                
                                                entry.setLength(dpsSourceEntry.getOriginalQuantity() == 0 ? 0 : dpsSourceEntry.getLength() * entryAdjustment.getQuantityToReturn() / dpsSourceEntry.getOriginalQuantity());
                                                entry.setSurface(dpsSourceEntry.getOriginalQuantity() == 0 ? 0 : dpsSourceEntry.getSurface() * entryAdjustment.getQuantityToReturn() / dpsSourceEntry.getOriginalQuantity());
                                                entry.setVolume(dpsSourceEntry.getOriginalQuantity() == 0 ? 0 : dpsSourceEntry.getVolume() * entryAdjustment.getQuantityToReturn() / dpsSourceEntry.getOriginalQuantity());
                                                entry.setMass(dpsSourceEntry.getOriginalQuantity() == 0 ? 0 : dpsSourceEntry.getMass() * entryAdjustment.getQuantityToReturn() / dpsSourceEntry.getOriginalQuantity());
                                                entry.setWeightGross(dpsSourceEntry.getOriginalQuantity() == 0 ? 0 : dpsSourceEntry.getWeightGross() * entryAdjustment.getQuantityToReturn() / dpsSourceEntry.getOriginalQuantity());
                                                entry.setWeightDelivery(dpsSourceEntry.getOriginalQuantity() == 0 ? 0 : dpsSourceEntry.getWeightDelivery() * entryAdjustment.getQuantityToReturn() / dpsSourceEntry.getOriginalQuantity());

                                                entry.setSurplusPercentage(0);
                                                entry.setSortingPosition(0);
                                                entry.setIsTaxesAutomaticApplying(true);
                                                entry.setIsInventoriable(dpsSourceEntry.getIsInventoriable());
                                                entry.setIsDeleted(false);
                                                entry.setFkItemId(dpsSourceEntry.getFkItemId());
                                                entry.setFkUnitId(dpsSourceEntry.getFkUnitId());
                                                entry.setFkOriginalUnitId(dpsSourceEntry.getFkOriginalUnitId());
                                                entry.setFkTaxRegionId(dpsSourceEntry.getFkTaxRegionId());
                                                entry.setFkDpsAdjustmentTypeId(adjustmentSubtypeKey[0]);
                                                entry.setFkDpsAdjustmentSubtypeId(adjustmentSubtypeKey[1]);
                                                entry.setFkDpsEntryTypeId(SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY);
                                                entry.setFkItemRefId_n(dpsSourceEntry.getFkItemRefId_n());
                                                entry.setFkCostCenterId_n(dpsSourceEntry.getFkCostCenterId_n());
                                                entry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

                                                entry.setDbmsFkItemGenericId(dpsSourceEntry.getDbmsFkItemGenericId());
                                                entry.setDbmsUnitSymbol(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.ITMU_UNIT, new int[] { entry.getFkUnitId() }, SLibConstants.DESCRIPTION_CODE));
                                                entry.setDbmsOriginalUnitSymbol(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.ITMU_UNIT, new int[] { entry.getFkOriginalUnitId() }, SLibConstants.DESCRIPTION_CODE));
                                                entry.setDbmsTaxRegion(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.FINU_TAX_REG, new int[] { entry.getFkTaxRegionId() }));
                                                entry.setDbmsDpsAdjustmentType(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_STP_DPS_ADJ, new int[] { entry.getFkDpsAdjustmentTypeId(), entry.getFkDpsAdjustmentSubtypeId() }));
                                                entry.setDbmsDpsEntryType(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_TP_DPS_ETY, new int[] { entry.getFkDpsEntryTypeId() }));
                                                entry.setAuxPkDpsYearId(oDpsSource.getPkYearId());
                                                entry.setAuxPkDpsDocId(oDpsSource.getPkDocId());

                                                entry.setDbmsDpsAddBachocoNumberPosition(dpsSourceEntry.getDbmsDpsAddBachocoNumberPosition());
                                                entry.setDbmsDpsAddBachocoCenter(dpsSourceEntry.getDbmsDpsAddBachocoCenter());
                                                entry.setDbmsDpsAddLorealEntryNumber(dpsSourceEntry.getDbmsDpsAddLorealEntryNumber());
                                                entry.setDbmsDpsAddSorianaBarCode(dpsSourceEntry.getDbmsDpsAddSorianaBarCode());

                                                entry.calculateTotal(miClient, moDps.getDate(),
                                                        moDps.getFkTaxIdentityEmisorTypeId(), moDps.getFkTaxIdentityReceptorTypeId(),
                                                        moDps.getIsDiscountDocPercentage(), moDps.getDiscountDocPercentage(), moDps.getExchangeRate());

                                                if (mbIsAddendaNeeded &&
                                                        moBizPartnerCategory.getFkCfdAddendaTypeId() == SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA) {

                                                    moFormEntry.formReset();
                                                    moFormEntry.setValue(SDataConstants.TRN_DPS, moDps);
                                                    moFormEntry.setValue(SDataConstants.BPSU_BP, moBizPartner);
                                                    moFormEntry.setValue(SDataConstants.BPSU_BPB, moBizPartnerBranch);
                                                    moFormEntry.setEnableDataAddenda(jTabbedPane.isEnabledAt(3));
                                                    moFormEntry.setRegistry(entry.clone());

                                                    moFormEntry.setFormVisible(true);

                                                    if (moFormEntry.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                                        entry = (SDataDpsEntry) moFormEntry.getRegistry();
                                                    }
                                                }

                                                adjustment = new SDataDpsDpsAdjustment();
                                                adjustment.setPkDpsYearId(dpsSourceEntry.getPkYearId());
                                                adjustment.setPkDpsDocId(dpsSourceEntry.getPkDocId());
                                                adjustment.setPkDpsEntryId(dpsSourceEntry.getPkEntryId());
                                                adjustment.setPkDpsAdjustmentYearId(entry.getPkYearId());
                                                adjustment.setPkDpsAdjustmentDocId(entry.getPkDocId());
                                                adjustment.setPkDpsAdjustmentEntryId(entry.getPkEntryId());
                                                adjustment.setQuantity(entry.getQuantity());
                                                adjustment.setOriginalQuantity(entry.getOriginalQuantity());
                                                adjustment.setValue(entry.getTotal_r());
                                                adjustment.setValueCy(entry.getTotalCy_r());

                                                entry.getDbmsDpsAdjustmentsAsAdjustment().add(adjustment);

                                                for (SDataDpsEntryNotes notes : entry.getDbmsEntryNotes()) {
                                                    notes.setIsRegistryEdited(true);    // force original document entry notes to be attached to new document entry even if they are not edited
                                                    notes.setPkNotesId(0);
                                                }

                                                moPaneGridEntries.addTableRow(new SDataDpsEntryRow(entry, ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter()));
                                                break;
                                            }
                                        }
                                    }
                                    else {
                                        // Entry discount:

                                        for (SDataDpsEntry dpsSourceEntry : oDpsSource.getDbmsDpsEntries()) {
                                            if (SLibUtilities.compareKeys(entryAdjustment.getDpsEntryKey(), dpsSourceEntry.getPrimaryKey())) {
                                                SDataDpsEntry entry = null;
                                                SDataDpsDpsAdjustment adjustment = null;

                                                subtotalFactor = dpsSourceEntry.getTotalCy_r() == 0 ? 0 : dpsSourceEntry.getSubtotalCy_r() / dpsSourceEntry.getTotalCy_r();

                                                entry = new SDataDpsEntry();
                                                entry.setPkYearId(moDps.getPkYearId());
                                                entry.setPkDocId(moDps.getPkDocId());
                                                entry.setPkEntryId(0);
                                                entry.setIsRegistryNew(true);

                                                entry.setConceptKey(dpsSourceEntry.getConceptKey());
                                                entry.setConcept(dpsSourceEntry.getConcept());
                                                entry.setOriginalQuantity(1);
                                                entry.setOriginalPriceUnitaryCy(entryAdjustment.getAmountToDiscount() * subtotalFactor);
                                                entry.setOriginalPriceUnitarySystemCy(entryAdjustment.getAmountToDiscount() * subtotalFactor);
                                                entry.setOriginalDiscountUnitaryCy(0);
                                                entry.setOriginalDiscountUnitarySystemCy(0);
                                                
                                                if (dpsSourceEntry.getFkUnitId() != dpsSourceEntry.getFkOriginalUnitId()) {
                                                    SDataUnit origUnit = null;
                                                    SDataUnit sysUnit = null;
                                                    
                                                    sysUnit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { dpsSourceEntry.getFkUnitId() }, SLibConstants.EXEC_MODE_VERBOSE);
                                                    origUnit  = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { dpsSourceEntry.getFkOriginalUnitId() }, SLibConstants.EXEC_MODE_VERBOSE);
                                                    
                                                    if (origUnit.getFkUnitTypeId() != sysUnit.getFkUnitTypeId()) {
                                                        oItem = null;
                                                        
                                                        oItem = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { dpsSourceEntry.getFkItemId() }, SLibConstants.EXEC_MODE_VERBOSE);
                                                        if (oItem.getFkUnitAlternativeTypeId() != SDataConstantsSys.ITMU_TP_UNIT_NA && oItem.getUnitAlternativeBaseEquivalence() == 0) {
                                                            entry.setAuxPreserveQuantity(true);
                                                            entry.setQuantity(dpsSourceEntry.getQuantity() * entry.getOriginalQuantity() / dpsSourceEntry.getOriginalQuantity());
                                                        }
                                                    }
                                                }
                                                
                                                entry.setLength(0);
                                                entry.setSurface(0);
                                                entry.setVolume(0);
                                                entry.setMass(0);
                                                entry.setWeightGross(0);
                                                entry.setWeightDelivery(0);

                                                entry.setSurplusPercentage(0);
                                                entry.setSortingPosition(0);
                                                entry.setIsTaxesAutomaticApplying(true);
                                                entry.setIsInventoriable(false);
                                                entry.setIsDeleted(false);
                                                entry.setFkItemId(dpsSourceEntry.getFkItemId());
                                                entry.setFkUnitId(dpsSourceEntry.getFkUnitId());
                                                entry.setFkOriginalUnitId(dpsSourceEntry.getFkOriginalUnitId());
                                                entry.setFkTaxRegionId(dpsSourceEntry.getFkTaxRegionId());
                                                entry.setFkDpsAdjustmentTypeId(adjustmentSubtypeKey[0]);
                                                entry.setFkDpsAdjustmentSubtypeId(adjustmentSubtypeKey[1]);
                                                entry.setFkDpsEntryTypeId(SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY);
                                                entry.setFkItemRefId_n(dpsSourceEntry.getFkItemRefId_n());
                                                entry.setFkCostCenterId_n(dpsSourceEntry.getFkCostCenterId_n());
                                                entry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

                                                entry.setDbmsFkItemGenericId(dpsSourceEntry.getDbmsFkItemGenericId());
                                                entry.setDbmsUnitSymbol(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.ITMU_UNIT, new int[] { entry.getFkUnitId() }, SLibConstants.DESCRIPTION_CODE));
                                                entry.setDbmsOriginalUnitSymbol(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.ITMU_UNIT, new int[] { entry.getFkOriginalUnitId() }, SLibConstants.DESCRIPTION_CODE));
                                                entry.setDbmsTaxRegion(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.FINU_TAX_REG, new int[] { entry.getFkTaxRegionId() }));
                                                entry.setDbmsDpsAdjustmentType(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_STP_DPS_ADJ, new int[] { entry.getFkDpsAdjustmentTypeId(), entry.getFkDpsAdjustmentSubtypeId() }));
                                                entry.setDbmsDpsEntryType(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_TP_DPS_ETY, new int[] { entry.getFkDpsEntryTypeId() }));
                                                entry.setAuxPkDpsYearId(oDpsSource.getPkYearId());
                                                entry.setAuxPkDpsDocId(oDpsSource.getPkDocId());

                                                entry.setDbmsDpsAddBachocoNumberPosition(dpsSourceEntry.getDbmsDpsAddBachocoNumberPosition());
                                                entry.setDbmsDpsAddBachocoCenter(dpsSourceEntry.getDbmsDpsAddBachocoCenter());
                                                entry.setDbmsDpsAddLorealEntryNumber(dpsSourceEntry.getDbmsDpsAddLorealEntryNumber());
                                                entry.setDbmsDpsAddSorianaBarCode(dpsSourceEntry.getDbmsDpsAddSorianaBarCode());

                                                entry.calculateTotal(miClient, moDps.getDate(),
                                                        moDps.getFkTaxIdentityEmisorTypeId(), moDps.getFkTaxIdentityReceptorTypeId(),
                                                        moDps.getIsDiscountDocPercentage(), moDps.getDiscountDocPercentage(), moDps.getExchangeRate());

                                                if (mbIsAddendaNeeded &&
                                                        moBizPartnerCategory.getFkCfdAddendaTypeId() == SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA) {

                                                    moFormEntry.formReset();
                                                    moFormEntry.setValue(SDataConstants.TRN_DPS, moDps);
                                                    moFormEntry.setValue(SDataConstants.BPSU_BP, moBizPartner);
                                                    moFormEntry.setValue(SDataConstants.BPSU_BPB, moBizPartnerBranch);
                                                    moFormEntry.setEnableDataAddenda(jTabbedPane.isEnabledAt(3));
                                                    moFormEntry.setRegistry(entry.clone());

                                                    moFormEntry.setFormVisible(true);

                                                    if (moFormEntry.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                                        entry = (SDataDpsEntry) moFormEntry.getRegistry();
                                                    }
                                                }

                                                adjustment = new SDataDpsDpsAdjustment();
                                                adjustment.setPkDpsYearId(dpsSourceEntry.getPkYearId());
                                                adjustment.setPkDpsDocId(dpsSourceEntry.getPkDocId());
                                                adjustment.setPkDpsEntryId(dpsSourceEntry.getPkEntryId());
                                                adjustment.setPkDpsAdjustmentYearId(entry.getPkYearId());
                                                adjustment.setPkDpsAdjustmentDocId(entry.getPkDocId());
                                                adjustment.setPkDpsAdjustmentEntryId(entry.getPkEntryId());
                                                adjustment.setQuantity(0);
                                                adjustment.setOriginalQuantity(0);
                                                adjustment.setValue(entry.getTotal_r());
                                                adjustment.setValueCy(entry.getTotalCy_r());

                                                entry.getDbmsDpsAdjustmentsAsAdjustment().add(adjustment);

                                                for (SDataDpsEntryNotes notes : entry.getDbmsEntryNotes()) {
                                                    notes.setIsRegistryEdited(true);    // force original document entry notes to be attached to new document entry even if they are not edited
                                                    notes.setPkNotesId(0);
                                                }

                                                moPaneGridEntries.addTableRow(new SDataDpsEntryRow(entry, ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter()));
                                                break;
                                            }
                                        }
                                    }
                                }

                                renderEntries();
                                calculateTotal();
                                moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
                                if (moPaneGridEntries.getTableGuiRowCount() > 0) {
                                    updateEditForImportFromDps(false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void actionEntryWizard() {
        if (jbEntryWizard.isEnabled() && mnFormStatus == SLibConstants.FORM_STATUS_EDIT) {
            updateDpsWithDocSettings();
            moFormEntryWizard.formReset();
            moFormEntryWizard.setValue(SDataConstants.TRN_DPS, moDps);
            moFormEntryWizard.setValue(SDataConstants.BPSU_BP, moBizPartner);
            moFormEntryWizard.setValue(SDataConstants.BPSU_BPB, moBizPartnerBranch);
            if (jcbTaxRegionId.getSelectedIndex() > 0) {
                moFormEntryWizard.setValue(SDataConstants.FINU_TAX_REG, ((SFormComponentItem) jcbTaxRegionId.getSelectedItem()).getPrimaryKey());
            }

            moFormEntryWizard.setFormVisible(true);

            if (moFormEntryWizard.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                int usrIdMax = 0;
                Vector<SDataDpsEntry> entries = null;

                for (STableRow row : moPaneGridEntries.getGridRows()) {
                    if (((SDataDpsEntry) row.getData()).getUserId() > usrIdMax) {
                        usrIdMax = ((SDataDpsEntry) row.getData()).getUserId();
                    }
                }

                usrIdMax++;
                entries = (Vector<SDataDpsEntry>) moFormEntryWizard.getValue(SDataConstants.TRN_DPS_ETY);
                for (SDataDpsEntry entry : entries) {
                    entry.setUserId(usrIdMax);
                    moPaneGridEntries.addTableRow(new SDataDpsEntryRow(entry, ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter()));
                }

                renderEntries();
                calculateTotal();

                for (STableRow row : moPaneGridEntries.getGridRows()) { // update in table each entry values
                    row.prepareTableRow();
                }

                moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
            }
        }
    }

    private void actionEntryViewLinks() {
        if (jbEntryViewLinks.isEnabled()) {
            int links = 0;
            int index = moPaneGridEntries.getTable().getSelectedRow();
            String sourseDps = "";
            SDataDpsEntry entry = null;

            if (index != -1) {
                entry = ((SDataDpsEntry) moPaneGridEntries.getTableRow(index).getData()).clone();

                if (mnFormStatus == SLibConstants.FORM_STATUS_EDIT) {
                    updateDpsWithDocSettings();
                }

                moDialogShowDocumentLinks.formReset();
                moDialogShowDocumentLinks.setValue(SDataConstants.TRN_DPS, moDps);
                moDialogShowDocumentLinks.setValue(SDataConstants.TRN_DPS_ETY, entry);
                links = moDialogShowDocumentLinks.readLinks();

                if (links == 0) {
                    if (mnFormStatus == SLibConstants.FORM_STATUS_EDIT && entry.hasDpsLinksAsSource()) {
                        sourseDps = SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.TRN_DPS, entry.getDbmsDpsLinksAsSource().elementAt(0).getDbmsSourceDpsKey(), SLibConstants.DESCRIPTION_CODE);
                        miClient.showMsgBoxInformation("Documento destino: " + sourseDps);
                    }
                    else if (mnFormStatus == SLibConstants.FORM_STATUS_EDIT && entry.hasDpsLinksAsDestiny()) {
                        sourseDps = SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.TRN_DPS, entry.getDbmsDpsLinksAsDestiny().elementAt(0).getDbmsSourceDpsKey(), SLibConstants.DESCRIPTION_CODE);
                        miClient.showMsgBoxInformation("Documento origen: " + sourseDps);
                    }
                    else {
                        miClient.showMsgBoxInformation(SLibConstants.MSG_INF_NO_LINK_DPS_ETY);
                    }
                }
                else {
                    moDialogShowDocumentLinks.setFormVisible(true);
                }
            }
        }
    }

    private void actionTaxRegionId() {
        SFormOptionPickerInterface picker = miClient.getOptionPicker(SDataConstants.FINU_TAX_REG);

        picker.formReset();
        picker.formRefreshOptionPane();
        picker.setSelectedPrimaryKey(((SFormComponentItem) jcbTaxRegionId.getSelectedItem()).getPrimaryKey());
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            SFormUtilities.locateComboBoxItem(jcbTaxRegionId, picker.getSelectedPrimaryKey());
            jcbTaxRegionId.requestFocus();
        }
    }

    private void actionNotesNew() {
        if (jbNotesNew.isEnabled()) {
            moFormNotes.formReset();
            moFormNotes.setFormVisible(true);

            if (moFormNotes.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                moPaneGridNotes.addTableRow(new SDataDpsNotesRow(moFormNotes.getRegistry()));
                moPaneGridNotes.renderTableRows();
                moPaneGridNotes.setTableRowSelection(moPaneGridNotes.getTableGuiRowCount() - 1);
            }
        }
    }

    private void actionNotesEdit() {
        if (jbNotesEdit.isEnabled()) {
            int index = moPaneGridNotes.getTable().getSelectedRow();

            if (index != -1) {
                moFormNotes.formReset();
                moFormNotes.setRegistry((SDataDpsNotes) moPaneGridNotes.getSelectedTableRow().getData());
                moFormNotes.setFormVisible(true);

                if (moFormNotes.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    moPaneGridNotes.setTableRow(new SDataDpsNotesRow(moFormNotes.getRegistry()), index);
                    moPaneGridNotes.renderTableRows();
                    moPaneGridNotes.setTableRowSelection(index < moPaneGridNotes.getTableGuiRowCount() ? index : moPaneGridNotes.getTableGuiRowCount() - 1);
                }
            }
        }
    }

    private void actionNotesDelete() {
        if (jbNotesDelete.isEnabled()) {
            int index = moPaneGridNotes.getTable().getSelectedRow();

            if (index != -1) {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                    SDataDpsNotes notes = (SDataDpsNotes) moPaneGridNotes.getTableRow(index).getData();

                    if (notes.getIsDeleted()) {
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_REG_ALREADY_DELETE);
                    }
                    else {
                        if (notes.getIsRegistryNew()) {
                            moPaneGridNotes.removeTableRow(index);
                        }
                        else {
                            notes.setIsDeleted(true);
                            notes.setFkUserDeleteId(miClient.getSession().getUser().getPkUserId());
                            notes.setIsRegistryEdited(true);
                            notes.setFkUserEditId(miClient.getSession().getUser().getPkUserId());

                            moPaneGridNotes.setTableRow(new SDataDpsNotesRow(notes), index);
                        }

                        moPaneGridNotes.renderTableRows();
                        moPaneGridNotes.setTableRowSelection(index < moPaneGridNotes.getTableGuiRowCount() ? index : moPaneGridNotes.getTableGuiRowCount() - 1);
                    }
                }
            }
        }
    }

    private void actionNotesFilter() {
        if (jtbNotesFilter.isEnabled()) {
            int index = moPaneGridNotes.getTable().getSelectedRow();

            moPaneGridNotes.setGridViewStatus(!jtbNotesFilter.isSelected() ? STableConstants.VIEW_STATUS_ALL : STableConstants.VIEW_STATUS_ALIVE);
            moPaneGridNotes.renderTableRows();
            moPaneGridNotes.setTableRowSelection(index < moPaneGridNotes.getTableGuiRowCount() ? index : moPaneGridNotes.getTableGuiRowCount() - 1);
        }
    }

    private void actionSystemNotes() {
        String option = "";

        option = miClient.pickOption(SDataConstants.TRN_SYS_NTS, new int[] { moDps.getFkDpsCategoryId(), moDps.getFkDpsClassId(), moDps.getFkDpsTypeId(), moDps.getFkCurrencyId() });

        if (!option.isEmpty()) {
            SDataDpsNotes notes = new SDataDpsNotes();

            notes.setNotes(option);
            notes.setIsAllDocs(true);
            notes.setIsPrintable(true);
            notes.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

            moPaneGridNotes.addTableRow(new SDataDpsNotesRow(notes));

            moPaneGridNotes.renderTableRows();
            moPaneGridNotes.setTableRowSelection(0);
        }
    }

    private void actionLoadFileXml() {
        FileFilter filter = new FileNameExtensionFilter("XML file", "xml");
        miClient.getFileChooser().repaint();
        miClient.getFileChooser().addChoosableFileFilter(filter);

        try {
            if (miClient.getFileChooser().showOpenDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                if (SCfdUtils.validateEmisorXmlExpenses(miClient, miClient.getFileChooser().getSelectedFile().getAbsolutePath())) {
                    moFieldFileXml.setFieldValue(miClient.getFileChooser().getSelectedFile().getName());
                    msFileXmlPath = miClient.getFileChooser().getSelectedFile().getAbsolutePath();
                }
            }
            miClient.getFileChooser().resetChoosableFileFilters();
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }
    
    private void actionDeleteFileXml() {
        moFieldFileXml.setFieldValue("");            
    }

    private void actionDateDocDelivery_n() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateDocDelivery_n.getDate(), moFieldDateDocDelivery_n);
    }

    private void actionDateDocLapsing_n() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateDocLapsing_n.getDate(), moFieldDateDocLapsing_n);
    }

    private void actionSalesAgent() {
        SFormOptionPickerInterface picker = miClient.getOptionPicker(SDataConstants.BPSX_BP_ATT_SAL_AGT);

        picker.formReset();
        picker.formRefreshOptionPane();
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            mnSalesAgentId_n = ((int[]) picker.getSelectedPrimaryKey())[0];
            renderSalesAgent(new int[] { mnSalesAgentId_n });
        }
    }

    private void actionSalesSupervisor() {
        SFormOptionPickerInterface picker = miClient.getOptionPicker(SDataConstants.BPSX_BP_ATT_SAL_AGT);

        picker.formReset();
        picker.formRefreshOptionPane();
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            mnSalesSupervisorId_n = ((int[]) picker.getSelectedPrimaryKey())[0];
            renderSalesSupervisor(new int[] { mnSalesSupervisorId_n });
        }
    }

    private void actionFkProductionOrderId_n() {
        miClient.pickOption(SDataConstants.MFG_ORD, moFieldFkProductionOrderId_n, new Object[] { "" + SDataConstantsSys.MFGS_ST_ORD_END + ", "  + SDataConstantsSys.MFGS_ST_ORD_CLS, SDataConstants.MFGX_ORD_MAIN_NA, false });
    }

    private void actionFkIncotermId() {
        miClient.pickOption(SModConsts.LOGS_INC, moFieldFkIncotermId, null);
    }

    private void actionFkModeOfTransportationTypeId() {
        miClient.pickOption(SModConsts.LOGS_TP_MOT, moFieldFkModeOfTransportationTypeId, null);
    }

    private void actionFkCarrierId_n() {
        miClient.pickOption(SDataConstants.BPSX_BP_ATT_CARR, moFieldFkCarrierId_n, null);
    }

    private void actionFkVehicleId_n() {
        miClient.pickOption(SModConsts.LOG_VEH, moFieldFkVehicleId_n, new int[] { moFieldFkVehicleTypeId_n.getKeyAsIntArray()[0] });
    }

    private void focusLostDate() {
        renderDate();
        renderDateMaturity();
        renderCatalogForCfd();
    }

    private void focusLostDateDoc() {

    }

    private void focusLostDateStartCredit() {
        renderDateMaturity();
    }

    private void focusLostDaysOfCredit() {
        renderDateMaturity();
    }

    private void focusLostExchangeRate() {
        calculateTotal();
    }

    private void focusLostDiscountDocPercentage() {
        calculateTotal();
    }

    private void itemStateDateDoc() {
        if (jckDateDoc.isSelected()) {
            jftDateDoc.setEditable(true);
            jftDateDoc.setFocusable(true);
            jbDateDoc.setEnabled(true);
            jftDateDoc.requestFocus();
        }
        else {
            jftDateDoc.setEditable(false);
            jftDateDoc.setFocusable(false);
            jbDateDoc.setEnabled(false);
            moFieldDateDoc.setFieldValue(moFieldDate.getDate());
        }
    }

    private void itemStateDateStartCredit() {
        if (jckDateStartCredit.isSelected()) {
            jftDateStartCredit.setEditable(true);
            jftDateStartCredit.setFocusable(true);
            jbDateStartCredit.setEnabled(true);
            jftDateStartCredit.requestFocus();
        }
        else {
            jftDateStartCredit.setEditable(false);
            jftDateStartCredit.setFocusable(false);
            jbDateStartCredit.setEnabled(false);
            moFieldDateStartCredit.setFieldValue(moFieldDate.getDate());
            renderDateMaturity();
        }
    }

    private void itemStateRecordManual() {
        boolean enabled = jckRecordUser.isSelected();

        jtfRecordManualDateRo.setEnabled(enabled);
        jtfRecordManualBranchRo.setEnabled(enabled);
        jtfRecordManualNumberRo.setEnabled(enabled);
        jbRecordManualSelect.setEnabled(enabled);
        jbRecordManualView.setEnabled(enabled && moRecordUserKey != null);
    }

    private void itemStatePayments() {
        if (jckPayments.isSelected()) {
            jtfPayments.setEditable(true);
            jtfPayments.setFocusable(true);
            jtfPayments.requestFocus();
        }
        else {
            jtfPayments.setEditable(false);
            jtfPayments.setFocusable(false);
            moFieldPayments.setFieldValue(0);
        }
    }

    private void itemStateShipments() {
        if (jckShipments.isSelected()) {
            jtfShipments.setEditable(true);
            jtfShipments.setFocusable(true);
            jtfShipments.requestFocus();
        }
        else {
            jtfShipments.setEditable(false);
            jtfShipments.setFocusable(false);
            moFieldShipments.setFieldValue(0);
        }
    }

    private void itemStateIsDiscountDocApplying(boolean calculate) {
        if (jckIsDiscountDocApplying.isSelected()) {
            jckIsDiscountDocPercentage.setEnabled(true);
            jckIsDiscountDocPercentage.requestFocus();
        }
        else {
            jckIsDiscountDocPercentage.setEnabled(false);
            jckIsDiscountDocPercentage.setSelected(false);
        }

        if (calculate) {
            calculateTotal();
        }
    }

    private void itemStateIsDiscountDocPercentage(boolean calculate) {
        if (jckIsDiscountDocPercentage.isSelected()) {
            jtfDiscountDocPercentage.setEditable(true);
            jtfDiscountDocPercentage.setFocusable(true);
            jtfDiscountDocPercentage.requestFocus();
        }
        else {
            jtfDiscountDocPercentage.setEditable(false);
            jtfDiscountDocPercentage.setFocusable(false);
            moFieldDiscountDocPercentage.setFieldValue(0);
        }

        if (calculate) {
            calculateTotal();
        }
    }

    private void itemChangeNumberSeries() {
        obtainNextNumber();
        if (moBizPartner != null && (mbIsDoc || mbIsAdj) && (!mbIsSales || isCfdNeeded() || isCfdiNeeded())) {
            jTabbedPane.setEnabledAt(3, true);
            renderAddendaData(true);
        }
        else {
            jTabbedPane.setEnabledAt(3, false);
            renderAddendaData(false);
        }
    }

    private void itemChangeFkPaymentTypeId(boolean reset) {
        mbUpdatingForm = true;

        if (moFieldFkPaymentTypeId.getKeyAsIntArray()[0] != SDataConstantsSys.TRNS_TP_PAY_CREDIT) {
            jckPayments.setEnabled(false);
            jckDateStartCredit.setEnabled(false);
            jtfDaysOfCredit.setEditable(false);
            jtfDaysOfCredit.setFocusable(false);

            if (reset) {
                moFieldDaysOfCredit.setFieldValue(0);
            }
        }
        else {
            jckPayments.setEnabled(true);
            jckDateStartCredit.setEnabled(true);
            jtfDaysOfCredit.setEditable(true);      // XXX must be enabled according to user rights
            jtfDaysOfCredit.setFocusable(true);     // XXX must be enabled according to user rights

            if (reset) {
                moFieldDaysOfCredit.setFieldValue(moBizPartnerCategory.getEffectiveDaysOfCredit());
            }
        }

        if (mnFormStatus == SLibConstants.FORM_STATUS_EDIT && !mbParamIsReadOnly) {
            if (!jckPayments.isEnabled()) {
                jckPayments.setSelected(false);
            }
            itemStatePayments();

            if (!jckDateStartCredit.isEnabled()) {
                jckDateStartCredit.setSelected(false);
            }
            itemStateDateStartCredit();

            renderDateMaturity();
        }

        mbUpdatingForm = false;
    }

    private void itemChangeFkPaymentSystemTypeId() {
        // XXX add code here!!!
    }

    private void itemChangeFkLanguageId() {

    }

    private void itemChangeFkCurrencyId(boolean calculate) {
        if (jcbFkCurrencyId.getSelectedIndex() <= 0 || moFieldFkCurrencyId.getKeyAsIntArray()[0] == miClient.getSessionXXX().getParamsErp().getFkCurrencyId()) {
            // Document in local currency:

            jtfExchangeRate.setEditable(false);
            jtfExchangeRate.setFocusable(false);
            jbExchangeRate.setEnabled(false);
            jbComputeTotal.setEnabled(false);
            jbEntryWizard.setEnabled(!mbIsAdj);

            moFieldExchangeRateSystem.setFieldValue(1d);
            moFieldExchangeRate.setFieldValue(1d);

            jtfCurrencyKeyRo.setText(miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
        }
        else {
            // Document in foreign currency:

            if (moDps != null && moDps.getIsRegistryNew() && !mbImportation) {
                setExchangeRate(moFieldFkCurrencyId.getKeyAsIntArray()[0], moFieldExchangeRate);
                setExchangeRate(moFieldFkCurrencyId.getKeyAsIntArray()[0], moFieldExchangeRateSystem);
            }
            else if (mbImportation && moParamDpsSource != null) {
                int [] typeDps = { moDpsType.getPkDpsCategoryId(), moDpsType.getPkDpsClassId(), moDpsType.getPkDpsTypeId() };

                if (!moParamDpsSource.getDate().equals(moDps.getDate()) && moParamDpsSource.getFkCurrencyId() == moDps.getFkCurrencyId() && 
                        !(SLibUtils.compareKeys(typeDps, SDataConstantsSys.TRNU_TP_DPS_SAL_CN) || SLibUtils.compareKeys(typeDps, SDataConstantsSys.TRNU_TP_DPS_PUR_CN))
                        ) {
                    setExchangeRate(moParamDpsSource.getFkCurrencyId(), moFieldExchangeRate);
                }
                
                setExchangeRate(moParamDpsSource.getFkCurrencyId(), moFieldExchangeRateSystem);
            }

            jtfExchangeRate.setEditable(true);
            jtfExchangeRate.setFocusable(true);
            jbExchangeRate.setEnabled(true);
            jbComputeTotal.setEnabled(true);
            jbEntryWizard.setEnabled(false);

            jtfCurrencyKeyRo.setText((String) ((SFormComponentItem) jcbFkCurrencyId.getSelectedItem()).getComplement());
        }

        if (calculate) {
            calculateTotal();
        }
        
        renderBizPartnerPrepaymentsBalance();
    }

    private void itemChangeFkIncotermId() {
        int[] filterTypeSpot = null;

        if (jcbFkIncotermId.getSelectedIndex() <= 0 || moFieldFkIncotermId.getKeyAsIntArray()[0] == SModSysConsts.LOGS_INC_NA) {
            jcbFkSpotDesId_n.setEnabled(false);

            jcbFkSpotSrcId_n.removeAllItems();
            jcbFkSpotDesId_n.removeAllItems();
        }
        else {
            jcbFkSpotDesId_n.setEnabled(true);

            switch (moFieldFkIncotermId.getKeyAsIntArray()[0]) {
                case SModSysConsts.LOGS_INC_EXW:
                case SModSysConsts.LOGS_INC_FCA:
                case SModSysConsts.LOGS_INC_CPT:
                case SModSysConsts.LOGS_INC_CIP:
                case SModSysConsts.LOGS_INC_DAP:
                case SModSysConsts.LOGS_INC_DDP:
                    filterTypeSpot = new int[] { SModSysConsts.LOGS_TP_SPOT_PLA };
                    break;
                case SModSysConsts.LOGS_INC_DAT:
                    filterTypeSpot = new int[] { SModSysConsts.LOGS_TP_SPOT_PLA, SModSysConsts.LOGS_TP_SPOT_TER };
                    break;
                case SModSysConsts.LOGS_INC_FAS:
                case SModSysConsts.LOGS_INC_FOB:
                case SModSysConsts.LOGS_INC_CFR:
                case SModSysConsts.LOGS_INC_CIF:
                    filterTypeSpot = new int[] { SModSysConsts.LOGS_TP_SPOT_POR };
                    break;
                default:
            }

            SFormUtilities.populateComboBox(miClient, jcbFkSpotSrcId_n, SModConsts.LOGU_SPOT_COB, new int[] { moDps != null ? moDps.getFkCompanyBranchId() : ((SSessionCustom) miClient.getSession().getSessionCustom()).getCurrentBranchKey()[0] });
            SFormUtilities.populateComboBox(miClient, jcbFkSpotDesId_n, SModConsts.LOGU_SPOT, new Object[] { filterTypeSpot, mnTypeDelivery });

            if (jcbFkSpotSrcId_n.getItemCount() == 2) {
                jcbFkSpotSrcId_n.setSelectedIndex(1);
            }

            if (jcbFkSpotDesId_n.getItemCount() == 2) {
                jcbFkSpotDesId_n.setSelectedIndex(1);
            }
        }
    }

    private void itemChangeFkCarrierTypeId() {
        boolean enableCarrier = moFieldFkCarrierTypeId.getKeyAsIntArray()[0] == SModSysConsts.LOGS_TP_CAR_CAR;
        boolean enableTypeVehicle = moFieldFkCarrierTypeId.getKeyAsIntArray()[0] > SModSysConsts.LOGS_TP_CAR_NA;

        jcbFkCarrierId_n.setEnabled(enableCarrier);
        jbFkCarrierId_n.setEnabled(enableCarrier);
        
        jcbFkVehicleTypeId_n.setEnabled(enableTypeVehicle);
        jcbDriver.setEnabled(enableTypeVehicle);
        jcbPlate.setEnabled(enableTypeVehicle);
        
        if (!enableCarrier) {
            moFieldFkCarrierId_n.setFieldValue(null);
        }

        if (!enableTypeVehicle) {
            moFieldFkVehicleTypeId_n.setFieldValue(null);
            moFieldDriver.setFieldValue("");
            moFieldPlate.setFieldValue("");
        }
        
        itemChangeFkVehicleTypeId_n();
    }

    private void itemChangeFkVehicleTypeId_n() {
	boolean enableVehicle = jcbFkVehicleTypeId_n.getSelectedIndex() > 0 && moFieldFkCarrierTypeId.getKeyAsIntArray()[0] == SModSysConsts.LOGS_TP_CAR_OWN;
        
        SFormUtilities.populateComboBox(miClient, jcbFkVehicleId_n, SModConsts.LOG_VEH, new int[] { moFieldFkVehicleTypeId_n.getKeyAsIntArray()[0] });
        
        jcbFkVehicleId_n.setEnabled(enableVehicle);
        jbFkVehicleId_n.setEnabled(enableVehicle);
    }

    private void itemChangeCfdAddendaSubtypeId() {
        if (moFieldCfdAddendaSubtypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_STP_CFD_ADD_SORIANA_NOR) {
            jtfNumberNoteIn.setEnabled(false);
        }
        else {
            jtfNumberNoteIn.setEnabled(true);
        }

    }

    private void actionEdit() {
        boolean edit = false;

        if (isBizPartnerBlocked(moBizPartner.getPkBizPartnerId(), manDpsClassKey[0])) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_INF_BP_BLOCKED);
            jbCancel.requestFocus();
        }
        else {
            switch (moDps.getFkDpsStatusId()) {
                case SDataConstantsSys.TRNS_ST_DPS_NEW:
                    edit = true;    // document will be saved as 'emited'
                    break;
                case SDataConstantsSys.TRNS_ST_DPS_EMITED:
                    edit = true;    // document will be saved as 'emited'
                    break;
                case SDataConstantsSys.TRNS_ST_DPS_ANNULED:
                    edit = miClient.showMsgBoxConfirm("El documento está 'anulado', si procede a modificarlo quedará nuevamente como 'emitido'.\n" + SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION;
                    break;
                default:
                    miClient.showMsgBoxWarning("No es posible determinar el estatus del documento.");
            }

            if (edit) {
                updateFormEditStatus(true);
                updateDateForOrderPrevious();
                jbEdit.setEnabled(false);
                jbEditHelp.setEnabled(false);
                jbOk.setEnabled(true);

                // XXX !!!!
                if (jftDate.isEditable()) {
                    jftDate.requestFocus();
                }
                else {
                    jtfNumber.requestFocus();
                }
            }
        }
    }

    private void actionEditHelp() {
        String msg = "";

        if (mbParamIsReadOnly) {
            msg += "\n- El documento se muestra sólo para lectura.";
        }
        if (moDps.getIsAudited()) {
            msg += "\n- El documento está auditado.";
        }
        if (moDps.getIsAuthorized()) {
            msg += "\n- El documento está autorizado.";
        }
        if (moDps.getIsSystem()) {
            msg += "\n- El documento es de sistema.";
        }
        if (moDps.getFkDpsValidityStatusId() == SDataConstantsSys.TRNS_ST_DPS_VAL_REPL) {
            msg += "\n- El documento ha sido reemplazado.";
        }
        if (moDps.getFkDpsValidityStatusId() == SDataConstantsSys.TRNS_ST_DPS_VAL_RISS) {
            msg += "\n- El documento ha sido reimpreso.";
        }
        if (!mbIsPeriodOpen && (mbIsDoc || mbIsAdj)) {
            msg += "\n- El período contable de la fecha del documento ya está cerrado.";
        }
        if (mnParamCurrentUserPrivilegeLevel != SDataConstantsSys.UNDEFINED && mnParamCurrentUserPrivilegeLevel < SUtilConsts.LEV_AUTHOR) {
            msg += "\n- El usuario '" + miClient.getSessionXXX().getUser().getUser() + "' no tiene el nivel de derecho suficiente para modificar el registro.";
        }
        if (moDps.isDocumentSal() && moDps.getDbmsDataCfd() != null && (moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && moDps.getDbmsDataCfd().isStamped()) {
            msg += "\n- Este documento es un CFDI y está emitido.";
        }
        if (moDps.isDocumentSal() && moDps.getDbmsDataCfd() != null && (moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && moDps.getDbmsDataCfd().isStamped() && moDps.getDbmsDataCfd().getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
            msg += "\n- Este documento es un CFDI y está anulado.";
        }
        if (moDps.isAdjustmentSal() && moDps.getDbmsDataCfd() != null && (moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && moDps.getDbmsDataCfd().isStamped()) {
            msg += "\n- Este documento de ajuste es un CFDI y está emitido.";
        }
        if (moDps.isAdjustmentSal() && moDps.getDbmsDataCfd() != null && (moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && moDps.getDbmsDataCfd().isStamped() && moDps.getDbmsDataCfd().getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
            msg += "\n- Este documento de ajuste es un CFDI y está anulado.";
        }
        if (moDps.isDocumentSal() && moDps.getDbmsDataCfd() != null && (moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && moDps.getDbmsDataCfd().getIsProcessingWebService()) {
            msg += "\n- Este documento es un CFDI y está en proceso de comunicación con el WS.";
        }
        if (moDps.isDocumentSal() && moDps.getDbmsDataCfd() != null && (moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && moDps.getDbmsDataCfd().getIsProcessingStorageXml()) {
            msg += "\n- Este documento es un CFDI y está en proceso de almacenamiento en el disco.";
        }
        if (moDps.isAdjustmentSal() && moDps.getDbmsDataCfd() != null && (moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && moDps.getDbmsDataCfd().getIsProcessingWebService()) {
            msg += "\n- Este documento de ajuste es un CFDI y está en proceso de comunicación con el WS.";
        }
        if (moDps.isAdjustmentSal() && moDps.getDbmsDataCfd() != null && (moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && moDps.getDbmsDataCfd().getIsProcessingStorageXml()) {
            msg += "\n- Este documento de ajuste es un CFDI y está en proceso de almacenamiento en el disco.";
        }

        if (msg.length() == 0) {
            msg += "\n- No se pudo determinar por qué razón el documento no se puede modificar.";
        }

        miClient.showMsgBoxInformation("No se puede modificar el documento porque:" + msg);
    }

    private void actionOk() {
        SFormValidation validation = null;

        if (jbOk.isEnabled()) {
            if (this.getFocusOwner() != null) {
                focusLost(new FocusEvent(this.getFocusOwner(), FocusEvent.FOCUS_LOST));
            }

            jbOk.requestFocus();    // this forces all pending focus lost function to be called

            validation = formValidate();

            if (validation.getIsError()) {
                if (validation.getComponent() != null) {
                    validation.getComponent().requestFocus();
                }
                if (validation.getMessage().length() > 0) {
                    miClient.showMsgBoxWarning(validation.getMessage());
                }
            }
            else {
                 mnFormResult = SLibConstants.FORM_RESULT_OK;
                 setVisible(false);
            }
        }
    }

    private void actionCancel() {
        if (jbCancel.isEnabled()) {
            if (!mbFormSettingsOk || mbParamIsReadOnly || mnFormStatus == SLibConstants.FORM_STATUS_READ_ONLY || miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_FORM_CLOSE) == JOptionPane.YES_OPTION) {
                releaseRecordUserLock();
                mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
                setVisible(false);
            }
        }
    }
    
    /**
     * Verify if shipment data is needed
     * @return 
     */
    private String validateRequiredShipmentData() {
        String shipmentMessageMissingData = "";
        
        for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
            SDataDpsEntry entry = (SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData();

            if (entry.isMissingRequiredShipmentData()) {
                shipmentMessageMissingData = "Partida número : " + String.valueOf(i + 1) + " Concepto : " + entry.getConcept() + "\n";
            }
        }

        if (!shipmentMessageMissingData.isEmpty()) {
            shipmentMessageMissingData = ("Tiene valores de embarque faltantes en : \n" + shipmentMessageMissingData);
        }
        
        return shipmentMessageMissingData;
    }
    
    /**
     *  Verify valid but provisional data in shipmente fields
     */
    private String validateProvisionalShipmentData() {
        String shipmentMessageMissingData = "";
         
        for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
            SDataDpsEntry entry = (SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData();

            if (entry.isValueProvisionalShipmentData()) {
                shipmentMessageMissingData = "Tiene partidas con valores de embarque provisionales. \n ¿Desea conservarlos?";
            }
        }
        
        return shipmentMessageMissingData;
    }
 
    public void publicActionDependentNew() {
        if (jTabbedPane.getSelectedIndex() == 0) {
            actionEntryNew();
        }
        else if (jTabbedPane.getSelectedIndex() == 2) {
            actionNotesNew();
        }
    }

    public void publicActionDependentEdit() {
        if (jTabbedPane.getSelectedIndex() == 0) {
            actionEntryEdit();
        }
        else if (jTabbedPane.getSelectedIndex() == 2) {
            actionNotesEdit();
        }
    }

    public void publicActionDependentDelete() {
        if (jTabbedPane.getSelectedIndex() == 0) {
            actionEntryDelete();
        }
        else if (jTabbedPane.getSelectedIndex() == 2) {
            actionNotesDelete();
        }
    }

    public void publicActionDependentFilter() {
        if (jTabbedPane.getSelectedIndex() == 0) {
            jtbEntryFilter.setSelected(!jtbEntryFilter.isSelected());
        }
        else if (jTabbedPane.getSelectedIndex() == 2) {
            jtbNotesFilter.setSelected(!jtbNotesFilter.isSelected());
        }
    }

    public void publicActionEntryDiscountRetailChain() {
        actionEntryDiscountRetailChain(moLastDpsSource);
    }

    public void publicActionEntryImportFromDps() {
        actionEntryImportFromDps(null);
    }

    public void publicActionEntryWizard() {
        actionEntryWizard();
    }

    public void publicActionEntryViewLinks() {
        actionEntryViewLinks();
    }

    public void publicActionNotesEdit() {
        actionNotesEdit();  // public access to notes edition, used by notes grid
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel64;
    private javax.swing.JPanel jPanel65;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel73;
    private javax.swing.JPanel jPanel74;
    private javax.swing.JPanel jPanel75;
    private javax.swing.JPanel jPanel76;
    private javax.swing.JPanel jPanel77;
    private javax.swing.JPanel jPanel78;
    private javax.swing.JPanel jPanel79;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel80;
    private javax.swing.JPanel jPanel81;
    private javax.swing.JPanel jPanel82;
    private javax.swing.JPanel jPanel83;
    private javax.swing.JPanel jPanel84;
    private javax.swing.JPanel jPanel85;
    private javax.swing.JPanel jPanel86;
    private javax.swing.JPanel jPanel87;
    private javax.swing.JPanel jPanel88;
    private javax.swing.JPanel jPanel89;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel90;
    private javax.swing.JPanel jPanel91;
    private javax.swing.JPanel jPanel92;
    private javax.swing.JPanel jPanel93;
    private javax.swing.JPanel jPanel94;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JButton jbBizPartnerBalance;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbComputeTotal;
    private javax.swing.JButton jbDate;
    private javax.swing.JButton jbDateDoc;
    private javax.swing.JButton jbDateDocDelivery_n;
    private javax.swing.JButton jbDateDocLapsing_n;
    private javax.swing.JButton jbDateStartCredit;
    private javax.swing.JButton jbDeleteFileXml;
    private javax.swing.JButton jbEdit;
    private javax.swing.JButton jbEditHelp;
    private javax.swing.JButton jbEntryDelete;
    private javax.swing.JButton jbEntryDiscountRetailChain;
    private javax.swing.JButton jbEntryEdit;
    private javax.swing.JButton jbEntryImportFromDps;
    private javax.swing.JButton jbEntryNew;
    private javax.swing.JButton jbEntryViewLinks;
    private javax.swing.JButton jbEntryWizard;
    private javax.swing.JButton jbExchangeRate;
    private javax.swing.JButton jbFileXml;
    private javax.swing.JButton jbFkCarrierId_n;
    private javax.swing.JButton jbFkCurrencyId;
    private javax.swing.JButton jbFkIncotermId;
    private javax.swing.JButton jbFkModeOfTransportationTypeId;
    private javax.swing.JButton jbFkProductionOrderId_n;
    private javax.swing.JButton jbFkVehicleId_n;
    private javax.swing.JButton jbNotesDelete;
    private javax.swing.JButton jbNotesEdit;
    private javax.swing.JButton jbNotesNew;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPrepayments;
    private javax.swing.JButton jbRecordManualSelect;
    private javax.swing.JButton jbRecordManualView;
    private javax.swing.JButton jbSalesAgent;
    private javax.swing.JButton jbSalesSupervisor;
    private javax.swing.JButton jbSystemNotes;
    private javax.swing.JButton jbTaxRegionId;
    private javax.swing.JComboBox<SFormComponentItem> jcbAdjustmentSubtypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbCfdAddendaSubtypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbCfdUseId;
    private javax.swing.JComboBox jcbDriver;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCarrierId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCarrierTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkContactId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCurrencyId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkDpsNatureId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkFunctionalArea;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkIncotermId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkLanguageId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkModeOfTransportationTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkPaymentSystemTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkPaymentTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkProductionOrderId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSpotDesId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSpotSrcId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkVehicleId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkVehicleTypeId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbNumberSeries;
    private javax.swing.JComboBox<SFormComponentItem> jcbPaymentAccount;
    private javax.swing.JComboBox jcbPlate;
    private javax.swing.JComboBox<SFormComponentItem> jcbTaxRegionId;
    private javax.swing.JCheckBox jckDateDoc;
    private javax.swing.JCheckBox jckDateStartCredit;
    private javax.swing.JCheckBox jckIsAudited;
    private javax.swing.JCheckBox jckIsAuthorized;
    private javax.swing.JCheckBox jckIsClosed;
    private javax.swing.JCheckBox jckIsCopy;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JCheckBox jckIsDiscountDocApplying;
    private javax.swing.JCheckBox jckIsDiscountDocPercentage;
    private javax.swing.JCheckBox jckIsLinked;
    private javax.swing.JCheckBox jckIsRebill;
    private javax.swing.JCheckBox jckIsSystem;
    private javax.swing.JCheckBox jckPayments;
    private javax.swing.JCheckBox jckRecordUser;
    private javax.swing.JCheckBox jckShipments;
    private javax.swing.JFormattedTextField jftDate;
    private javax.swing.JFormattedTextField jftDateDelivery_nRo;
    private javax.swing.JFormattedTextField jftDateDoc;
    private javax.swing.JFormattedTextField jftDateDocDelivery_n;
    private javax.swing.JFormattedTextField jftDateDocLapsing_n;
    private javax.swing.JFormattedTextField jftDateMaturityRo;
    private javax.swing.JFormattedTextField jftDateShipment_nRo;
    private javax.swing.JFormattedTextField jftDateStartCredit;
    private javax.swing.JFormattedTextField jftRemissionDate;
    private javax.swing.JLabel jlAdjustmentSubtypeId;
    private javax.swing.JLabel jlBachocoDivision;
    private javax.swing.JLabel jlBachocoOrganizacion;
    private javax.swing.JLabel jlBachocoSociedad;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlBizPartnerBranch;
    private javax.swing.JLabel jlBizPartnerBranchAddress01;
    private javax.swing.JLabel jlBizPartnerBranchAddress02;
    private javax.swing.JLabel jlBizPartnerBranchAddressMain01;
    private javax.swing.JLabel jlBizPartnerBranchAddressMain02;
    private javax.swing.JLabel jlBulkQuantity;
    private javax.swing.JLabel jlBulkType;
    private javax.swing.JLabel jlCfdAddendaSubtypeId;
    private javax.swing.JLabel jlCfdUseId;
    private javax.swing.JLabel jlCompanyBranch;
    private javax.swing.JLabel jlCurrency;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlDateDelivery_n;
    private javax.swing.JLabel jlDateDocDelivery_n;
    private javax.swing.JLabel jlDateDocLapsing_n;
    private javax.swing.JLabel jlDateMaturity;
    private javax.swing.JLabel jlDateShipment_n;
    private javax.swing.JLabel jlDaysOfCredit;
    private javax.swing.JLabel jlDiscountDoc;
    private javax.swing.JLabel jlDpsDescripcion;
    private javax.swing.JLabel jlDpsType;
    private javax.swing.JLabel jlDriver;
    private javax.swing.JLabel jlExchangeRate;
    private javax.swing.JLabel jlExchangeRateSystem;
    private javax.swing.JLabel jlFileXml;
    private javax.swing.JLabel jlFkCarrierId_n;
    private javax.swing.JLabel jlFkCarrierTypeId;
    private javax.swing.JLabel jlFkCfdAddendaTypeId;
    private javax.swing.JLabel jlFkCfdConfirmationNum;
    private javax.swing.JLabel jlFkContactId_n;
    private javax.swing.JLabel jlFkCurrencyId;
    private javax.swing.JLabel jlFkDpsNatureId;
    private javax.swing.JLabel jlFkDpsStatus;
    private javax.swing.JLabel jlFkDpsStatusAuthorization;
    private javax.swing.JLabel jlFkDpsStatusValidity;
    private javax.swing.JLabel jlFkFunctionalArea;
    private javax.swing.JLabel jlFkIncotermId;
    private javax.swing.JLabel jlFkLanguageId;
    private javax.swing.JLabel jlFkModeOfTransportationTypeId;
    private javax.swing.JLabel jlFkPaymentSystemTypeId;
    private javax.swing.JLabel jlFkPaymentTypeId;
    private javax.swing.JLabel jlFkProductionOrderId_n;
    private javax.swing.JLabel jlFkSpotDesId_n;
    private javax.swing.JLabel jlFkSpotSrcId_n;
    private javax.swing.JLabel jlFkVehicleId_n;
    private javax.swing.JLabel jlFkVehicleTypeId_n;
    private javax.swing.JLabel jlFolioNotaRecepcion;
    private javax.swing.JLabel jlGoodsDelivery;
    private javax.swing.JLabel jlNumber;
    private javax.swing.JLabel jlNumberNoteIn;
    private javax.swing.JLabel jlPaymentAccount;
    private javax.swing.JLabel jlPrepaymentsCy;
    private javax.swing.JLabel jlPrepaymentsWarning;
    private javax.swing.JLabel jlQuantityTotal;
    private javax.swing.JLabel jlRemission;
    private javax.swing.JLabel jlRemissionDate;
    private javax.swing.JLabel jlSalesAgent;
    private javax.swing.JLabel jlSalesAgentBizPartner;
    private javax.swing.JLabel jlSalesOrder;
    private javax.swing.JLabel jlSalesSupervisor;
    private javax.swing.JLabel jlSalesSupervisorBizPartner;
    private javax.swing.JLabel jlStore;
    private javax.swing.JLabel jlSubtotal;
    private javax.swing.JLabel jlSubtotalProvisional;
    private javax.swing.JLabel jlTaxCharged;
    private javax.swing.JLabel jlTaxRegionId;
    private javax.swing.JLabel jlTaxRetained;
    private javax.swing.JLabel jlTicket;
    private javax.swing.JLabel jlTotal;
    private javax.swing.JPanel jpAddendaData;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpControlsButtons;
    private javax.swing.JPanel jpControlsButtonsCenter;
    private javax.swing.JPanel jpControlsRecord;
    private javax.swing.JPanel jpCurrency;
    private javax.swing.JPanel jpDocument;
    private javax.swing.JPanel jpEntries;
    private javax.swing.JPanel jpEntriesControls;
    private javax.swing.JPanel jpEntriesControlsEast;
    private javax.swing.JPanel jpEntriesControlsWest;
    private javax.swing.JPanel jpHeader;
    private javax.swing.JPanel jpMarketing;
    private javax.swing.JPanel jpNotes;
    private javax.swing.JPanel jpNotesControls;
    private javax.swing.JPanel jpOtherLogistics;
    private javax.swing.JPanel jpOtherMarketing;
    private javax.swing.JPanel jpOtherSupply;
    private javax.swing.JPanel jpValue;
    private javax.swing.JSeparator jsEntry01;
    private javax.swing.JSeparator jsEntry02;
    private javax.swing.JSeparator jsEntry03;
    private javax.swing.JSeparator jsNotes01;
    private javax.swing.JToggleButton jtbEntryFilter;
    private javax.swing.JToggleButton jtbNotesFilter;
    private javax.swing.JTextField jtfBachocoDivision;
    private javax.swing.JTextField jtfBachocoOrganizacion;
    private javax.swing.JTextField jtfBachocoSociedad;
    private javax.swing.JTextField jtfBizPartnerBranchAddress01Ro;
    private javax.swing.JTextField jtfBizPartnerBranchAddress02Ro;
    private javax.swing.JTextField jtfBizPartnerBranchAddressMain01Ro;
    private javax.swing.JTextField jtfBizPartnerBranchAddressMain02Ro;
    private javax.swing.JTextField jtfBizPartnerBranchRo;
    private javax.swing.JTextField jtfBizPartnerRo;
    private javax.swing.JTextField jtfBulkQuantity;
    private javax.swing.JTextField jtfBulkType;
    private javax.swing.JTextField jtfCfdConfirmationNum;
    private javax.swing.JTextField jtfCompanyBranchCodeRo;
    private javax.swing.JTextField jtfCompanyBranchRo;
    private javax.swing.JTextField jtfCurrencyKeyRo;
    private javax.swing.JTextField jtfCurrencySystemKeyRo;
    private javax.swing.JTextField jtfDaysOfCredit;
    private javax.swing.JTextField jtfDiscountDocCy_rRo;
    private javax.swing.JTextField jtfDiscountDocPercentage;
    private javax.swing.JTextField jtfDiscountDoc_rRo;
    private javax.swing.JTextField jtfDpsDescripcion;
    private javax.swing.JTextField jtfDpsTypeRo;
    private javax.swing.JTextField jtfExchangeRate;
    private javax.swing.JTextField jtfExchangeRateSystemRo;
    private javax.swing.JTextField jtfFileXml;
    private javax.swing.JTextField jtfFkCfdAddendaTypeId;
    private javax.swing.JTextField jtfFkDpsStatusAuthorizationRo;
    private javax.swing.JTextField jtfFkDpsStatusRo;
    private javax.swing.JTextField jtfFkDpsStatusValidityRo;
    private javax.swing.JTextField jtfFolioNotaRecepcion;
    private javax.swing.JTextField jtfGoodsDelivery;
    private javax.swing.JTextField jtfNumber;
    private javax.swing.JTextField jtfNumberNoteIn;
    private javax.swing.JTextField jtfNumberReference;
    private javax.swing.JTextField jtfPayments;
    private javax.swing.JTextField jtfPrepaymentsCy;
    private javax.swing.JTextField jtfPrepaymentsCyCurRo;
    private javax.swing.JTextField jtfQuantityTotal;
    private javax.swing.JTextField jtfRecordManualBranchRo;
    private javax.swing.JTextField jtfRecordManualDateRo;
    private javax.swing.JTextField jtfRecordManualNumberRo;
    private javax.swing.JTextField jtfRemission;
    private javax.swing.JTextField jtfSalesAgentBizPartnerRo;
    private javax.swing.JTextField jtfSalesAgentRo;
    private javax.swing.JTextField jtfSalesOrder;
    private javax.swing.JTextField jtfSalesSupervisorBizPartnerRo;
    private javax.swing.JTextField jtfSalesSupervisorRo;
    private javax.swing.JTextField jtfShipments;
    private javax.swing.JTextField jtfStore;
    private javax.swing.JTextField jtfSubtotalCy_rRo;
    private javax.swing.JTextField jtfSubtotalProvisionalCy_rRo;
    private javax.swing.JTextField jtfSubtotalProvisional_rRo;
    private javax.swing.JTextField jtfSubtotal_rRo;
    private javax.swing.JTextField jtfTaxChargedCy_rRo;
    private javax.swing.JTextField jtfTaxCharged_rRo;
    private javax.swing.JTextField jtfTaxRetainedCy_rRo;
    private javax.swing.JTextField jtfTaxRetained_rRo;
    private javax.swing.JTextField jtfTicket;
    private javax.swing.JTextField jtfTotalCy_rRo;
    private javax.swing.JTextField jtfTotal_rRo;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        int[] keyBp = new int[] { moDps.getFkBizPartnerId_r() };
        int[] keyBpb = new int[] { moDps.getFkBizPartnerBranchId() };
        int[] keyBpbAdd = new int[] { moDps.getFkBizPartnerBranchId(), moDps.getFkBizPartnerBranchAddressId() };
        SDataDpsEntry entry = null;
        SDataDpsNotes notes = null;

        moDps = createNewDps(null);
        renderRecordAutomatic();
        setBizPartner(keyBp, keyBpb, keyBpbAdd);

        for (STableRow row : moPaneGridEntries.getGridRows()) {
            entry = (SDataDpsEntry) row.getData();
            entry.setPkEntryId(0);
            entry.setIsRegistryNew(true);

            entry.getDbmsDpsLinksAsDestiny().clear();
            entry.getDbmsDpsLinksAsSource().clear();
            entry.getDbmsDpsAdjustmentsAsDps().clear();
            entry.getDbmsDpsAdjustmentsAsAdjustment().clear();
            entry.setIsPriceVariable(false);
            entry.setIsPriceConfirm(false);
            entry.setContractPriceYear(SLibConstants.UNDEFINED);
            entry.setContractPriceMonth(SLibConstants.UNDEFINED);
            entry.setContractBase(0d);
            entry.setContractFactor(0d);
            entry.setContractFuture(0d);

            for (SDataDpsEntryTax entryTax : entry.getDbmsEntryTaxes()) {
                entryTax.setPkEntryId(0);
                entryTax.setIsRegistryNew(true);
            }
            for (SDataDpsEntryCommissions entryCommissions : entry.getDbmsEntryCommissions()) {
                entryCommissions.setPkCommissionsId(0);
                entryCommissions.setIsRegistryNew(true);
            }
            for (SDataDpsEntryNotes entryNotes : entry.getDbmsEntryNotes()) {
                entryNotes.setPkNotesId(0);
                entryNotes.setIsRegistryNew(true);
            }
        }

        for (STableRow row : moPaneGridNotes.getGridRows()) {
            notes = (SDataDpsNotes) row.getData();
            notes.setPkNotesId(0);
            notes.setIsRegistryNew(true);
        }

        actionEdit();
    }

    @Override
    public void formReset() {
        mbResetingForm = true;

        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;
        mbImportation = false;

        moDps = createNewDps(null);
        moGuiDpsLink = null;
        moLastDpsSource = null;
        moBizPartner = null;
        moBizPartnerBranch = null;
        moBizPartnerBranchAddressMain = null;
        moBizPartnerBranchAddress = null;
        moBizPartnerCategory = null;
        moRecordUserKey = null;
        moRecordUser = null;
        moRecordUserLock = null;
        mnSalesAgentId_n = 0;
        mnSalesAgentBizPartnerId_n = 0;
        mnSalesSupervisorId_n = 0;
        mnSalesSupervisorBizPartnerId_n = 0;
        mbFormSettingsOk = true;
        mbIsNumberEditable = true;
        mbHasRightOrderDelay = mbIsSales ? 
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD_DELAY).HasRight :
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD_DELAY).HasRight;
        mbHasRightOmitSourceDoc = mbIsSales ? 
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_OMT_DOC_SRC).HasRight :
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_OMT_DOC_SRC).HasRight;
        moPaneGridEntries.createTable();
        moPaneGridEntries.clearTableRows();
        moPaneGridEntries.setGridViewStatus(STableConstants.VIEW_STATUS_ALIVE);
        jtbEntryFilter.setSelected(true);

        moPaneGridNotes.createTable();
        moPaneGridNotes.clearTableRows();
        moPaneGridNotes.setGridViewStatus(STableConstants.VIEW_STATUS_ALIVE);
        jtbNotesFilter.setSelected(true);

        moPickerBizPartner.formReset();

        for (int i = 0; i < mvFields.size(); i++) {
            mvFields.get(i).resetField();
        }

        jcbAdjustmentSubtypeId.setSelectedIndex(0);

        if (jcbNumberSeries.getItemCount() > 0) {
            jcbNumberSeries.setSelectedIndex(0);
        }

        jckDateDoc.setSelected(false);
        jckDateStartCredit.setSelected(false);
        jckRecordUser.setSelected(false);
        jckShipments.setSelected(false);
        jckPayments.setSelected(false);

        moFieldDate.setFieldValue(moDps.getDate());
        moFieldDateDoc.setFieldValue(moDps.getDate());
        moFieldDateStartCredit.setFieldValue(moDps.getDate());
        moFieldFkPaymentTypeId.setFieldValue(new int[] { SDataConstantsSys.TRNS_TP_PAY_CASH });
        moFieldFkPaymentSystemTypeId.setFieldValue(new int[] { SDataConstantsSys.TRNU_TP_PAY_SYS_NA });
        moFieldPaymentAccount.setFieldValue("");
        moFieldFkFunctionalAreaId.setFieldValue(new int[] { SDataConstantsSys.TRNU_DPS_NAT_DEF });
        if (!isFunctionalAreasApply()) { 
            moFieldFkDpsNatureId.setFieldValue(new int[] { SModSysConsts.CFGU_FUNC_NON });
        }
        else {
            if (jcbFkFunctionalArea.getItemCount() == 2) {
                jcbFkFunctionalArea.setSelectedIndex(1);
            }
        }
        moFieldFkIncotermId.setFieldValue(new int[] { SModSysConsts.LOGS_INC_NA });
        moFieldFkModeOfTransportationTypeId.setFieldValue(new int[] { SModSysConsts.LOGS_TP_MOT_NA });
        moFieldFkCarrierTypeId.setFieldValue(new int[] { SModSysConsts.LOGS_TP_CAR_NA });
        
        if (mbIsSales && (mbIsDoc || mbIsAdj)) {
            try {
                moFieldFkCfdUseId.setFieldValue(SCatalogXmlUtils.getNameEntry((SGuiClient) miClient, SDataConstantsSys.TRNS_CFD_CAT_CFD_USE, moDps.getDate(), miClient.getSessionXXX().getParamsCompany().getXtaCfdUseCfdi()));
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }


        renderBasicSettings();
        adecuatePaymentTypeSettings();

        renderDpsType();
        renderDpsValue();
        renderDpsStatus();
        renderRecordManual();
        renderRecordAutomatic();
        renderBizPartner();
        renderBizPartnerPrepaymentsBalance();
        renderDateMaturity();
        renderSalesAgentBizPartner(null);
        renderSalesAgent(null);
        renderSalesSupervisorBizPartner(null);
        renderSalesSupervisor(null);

        itemStateDateDoc();
        itemStateDateStartCredit();
        itemStateRecordManual();
        itemStatePayments();
        itemStateShipments();
        itemStateIsDiscountDocApplying(false);
        itemStateIsDiscountDocPercentage(false);

        itemChangeFkPaymentTypeId(true);
        itemChangeFkPaymentSystemTypeId();
        itemChangeFkLanguageId();
        itemChangeFkCurrencyId(false);
        itemChangeFkIncotermId();
        itemChangeFkCarrierTypeId();
        itemChangeFkVehicleTypeId_n();

        jTabbedPane.setSelectedIndex(0);
        jTabbedPane.setEnabledAt(3, false);

        updateFormEditStatus(true);
        jbEdit.setEnabled(false);
        jbEditHelp.setEnabled(false);
        jbOk.setEnabled(true);

        jcbFkSpotSrcId_n.setEnabled(false);
        jcbFkSpotDesId_n.setEnabled(false);
        jlFolioNotaRecepcion.setEnabled(false);
        jtfFolioNotaRecepcion.setEnabled(false);
        jlBachocoSociedad.setEnabled(false);
        jtfBachocoSociedad.setEnabled(false);
        jlBachocoOrganizacion.setEnabled(false);
        jtfBachocoOrganizacion.setEnabled(false);
        jlBachocoDivision.setEnabled(false);
        jtfBachocoDivision.setEnabled(false);
        jlDpsDescripcion.setEnabled(false);
        jtfDpsDescripcion.setEnabled(false);
        jlStore.setEnabled(false);
        jtfStore.setEnabled(false);
        jlGoodsDelivery.setEnabled(false);
        jtfGoodsDelivery.setEnabled(false);
        jlBulkType.setEnabled(false);
        jtfBulkType.setEnabled(false);
        jlBulkQuantity.setEnabled(false);
        jtfBulkQuantity.setEnabled(false);
        jlRemission.setEnabled(false);
        jtfRemission.setEnabled(false);
        jlSalesOrder.setEnabled(false);
        jtfSalesOrder.setEnabled(false);
        jlCfdAddendaSubtypeId.setEnabled(false);
        jcbCfdAddendaSubtypeId.setEnabled(false);
        jlNumberNoteIn.setEnabled(false);
        jtfNumberNoteIn.setEnabled(false);
        jlRemissionDate.setEnabled(false);
        jftRemissionDate.setEnabled(false);
        mbIsAddendaNeeded = false;
        mnCountEntries = 0;
        msFileXmlPath  = "";

        mdOrigExchangeRate = 0;
        mdOrigDiscountDocPercentage = 0;
        mbOrigIsDiscountDocApplying = false;

        mbResetingForm = false;
    }

    @Override
    public void formRefreshCatalogues() {
        mbResetingForm = true;

        moPickerBizPartner.formRefreshOptionPane();
        moFormEntry.formRefreshCatalogues();
        moFormEntryWizard.formRefreshCatalogues();
        moFormNotes.formRefreshCatalogues();

        SFormUtilities.populateComboBox(miClient, jcbFkPaymentTypeId, SDataConstants.TRNS_TP_PAY);
        SFormUtilities.populateComboBox(miClient, jcbFkPaymentSystemTypeId, SDataConstants.TRNU_TP_PAY_SYS);
        SFormUtilities.populateComboBox(miClient, jcbFkLanguageId, SDataConstants.CFGU_LAN);
        SFormUtilities.populateComboBox(miClient, jcbFkDpsNatureId, SDataConstants.TRNU_DPS_NAT);
        if (!isFunctionalAreasApply()) {
            SFormUtilities.populateComboBox(miClient, jcbFkFunctionalArea, SModConsts.CFGU_FUNC);
        }
        else {
            SFormUtilities.populateComboBox(miClient, jcbFkFunctionalArea, SModConsts.CFGU_FUNC, new int[] { miClient.getSessionXXX().getUser().getPkUserId() });
        }
        SFormUtilities.populateComboBox(miClient, jcbFkCurrencyId, SDataConstants.CFGU_CUR);
        SFormUtilities.populateComboBox(miClient, jcbAdjustmentSubtypeId, SDataConstants.TRNS_STP_DPS_ADJ);
        SFormUtilities.populateComboBox(miClient, jcbTaxRegionId, SDataConstants.FINU_TAX_REG);
        SFormUtilities.populateComboBox(miClient, jcbFkIncotermId, SModConsts.LOGS_INC);
        SFormUtilities.populateComboBox(miClient, jcbFkSpotSrcId_n, SModConsts.LOGU_SPOT_COB, null);
        SFormUtilities.populateComboBox(miClient, jcbFkSpotDesId_n, SModConsts.LOGU_SPOT, null);
        SFormUtilities.populateComboBox(miClient, jcbFkModeOfTransportationTypeId, SModConsts.LOGS_TP_MOT);
        SFormUtilities.populateComboBox(miClient, jcbFkCarrierTypeId, SModConsts.LOGS_TP_CAR);
        SFormUtilities.populateComboBox(miClient, jcbFkCarrierId_n, SDataConstants.BPSX_BP_ATT_CARR);
        SFormUtilities.populateComboBox(miClient, jcbFkVehicleTypeId_n, SModConsts.LOGU_TP_VEH);
        SFormUtilities.populateComboBox(miClient, jcbFkProductionOrderId_n, SDataConstants.MFG_ORD,
                new Object[] { "" + SDataConstantsSys.MFGS_ST_ORD_END + ", " + SDataConstantsSys.MFGS_ST_ORD_CLS, SDataConstants.MFGX_ORD_MAIN_NA, false });
        
        renderCatalogForCfd();

        mbResetingForm = false;
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        int[] key = null;
        double prepaymentsCy = 0;
        String msg = "";
        SFormValidation validation = new SFormValidation();

        if (moDps.getIsRegistryNew()) {
            moDps.setPkYearId(SLibTimeUtilities.digestYear(moFieldDate.getDate())[0]);
        }

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(mvFields.get(i).getComponent());
                break;
            }
        }

        if (!validation.getIsError()) {
            msg = validateDateLinks();

            if (msg.length() > 0) {
                validation.setMessage(msg);
                validation.setComponent(jftDate);
            }
            else if (moDpsType == null) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDpsType.getText() + "'.");
            }
            else if (moBizPartner == null) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlBizPartner.getText() + "'.");
            }
            else if (!SDataUtilities.isPeriodOpen(miClient, moFieldDate.getDate()) && (moDps.getIsRegistryNew() || mbIsDoc || mbIsAdj)) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
                validation.setComponent(jftDate);
            }
            else if (moDps.getPkYearId() != SLibTimeUtilities.digestYear(moFieldDate.getDate())[0]) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_YEAR);
                validation.setComponent(jftDate);
            }
            else if (jcbNumberSeries.isEditable() && moFieldNumberSeries.getString().length() > 15) {
                validation.setMessage("La longitud máxima para el campo 'Serie' es 15.");
                validation.setComponent(jcbNumberSeries);
            }
            else if (jcbFkPaymentSystemTypeId.getSelectedIndex() <= 0) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + jlFkPaymentSystemTypeId.getText() + "'.");
                validation.setComponent(jcbFkPaymentSystemTypeId);
            }
            else if (moDps.getTotal_r() < 0) {
                validation.setMessage("El valor del documento no puede ser negativo.");
                validation.setComponent(moPaneGridEntries);
                jTabbedPane.setSelectedIndex(0);
            }
            else {
                try {
                    if (mbIsSales || mbIsEst || mbIsOrd) {
                        key = SDataUtilities.obtainDpsKey(miClient, moFieldNumberSeries.getString(), moFieldNumber.getString(), moDpsType.getPrimaryKey());
                    }
                    else {
                        key = SDataUtilities.obtainDpsKeyForBizPartner(miClient, moFieldNumberSeries.getString(), moFieldNumber.getString(), moDpsType.getPrimaryKey(), moBizPartner.getPrimaryKey());
                    }

                    if ((moDps.getIsRegistryNew() && key != null) || (!moDps.getIsRegistryNew()) && key != null && !SLibUtilities.compareKeys(key, moDps.getPrimaryKey())) {
                        validation.setMessage("Ya existe otro documento '" + moDpsType.getDpsType() + "' " +
                                "con el folio '" + moFieldNumberSeries.getString() + (moFieldNumberSeries.getString().length() == 0 ? "" : "-") + moFieldNumber.getString() + "'.");
                        validation.setComponent(jcbNumberSeries.isEnabled() ? jcbNumberSeries : jtfNumber);
                    }
                }
                catch (Exception e) {
                    validation.setMessage("" + e);
                }

                if (!validation.getIsError()) {
                    if (jckRecordUser.isSelected() && moRecordUserKey == null) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jckRecordUser.getText() + "'.");
                        validation.setComponent(jbRecordManualSelect);
                    }
                    else if (jckPayments.isSelected() && moFieldPayments.getInteger() < 0) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jckPayments.getText() + "'.");
                        validation.setComponent(jtfPayments);
                    }
                    else if (jckShipments.isSelected() && moFieldShipments.getInteger() < 0) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jckShipments.getText() + "'.");
                        validation.setComponent(jtfShipments);
                    }
                    else if (mbIsSales && mbIsDoc && moFieldFkPaymentTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.TRNS_TP_PAY_CASH && moFieldFkPaymentSystemTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.TRNU_TP_PAY_SYS_NA) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFkPaymentSystemTypeId.getText() + "'.");
                        validation.setComponent(jcbFkPaymentSystemTypeId);
                    }
                    else if (moBizPartnerCategory.getDateStart().after(moFieldDate.getDate())) {
                        validation.setMessage("La fecha inicial de operaciones del asociado de negocios (" + SLibUtils.DateFormatDate.format(moBizPartnerCategory.getDateStart()) + ") " +
                                "no puede ser posterior a la fecha del campo '" + jlDate.getText() + "'.");
                        validation.setComponent(jftDate);
                    }
                    else if (moBizPartnerCategory.getDateEnd_n() != null && moBizPartnerCategory.getDateEnd_n().before(moFieldDate.getDate())) {
                        validation.setMessage("La fecha final de operaciones del asociado de negocios (" + SLibUtils.DateFormatDate.format(moBizPartnerCategory.getDateStart()) + ") " +
                                "no puede ser anterior a la fecha del campo '" + jlDate.getText() + "'.");
                        validation.setComponent(jftDate);
                    }
                    else if (moFieldDateDocDelivery_n.getDate() != null && moFieldDateDocDelivery_n.getDate().before(moFieldDate.getDate())) {
                        validation.setMessage("La fecha del campo '" + jlDateDocDelivery_n.getText() + "' no puede ser anterior a la del campo '" + jlDate.getText() + "'.");
                        validation.setComponent(jftDateDocDelivery_n);
                    }
                    else if (moFieldDateDocLapsing_n.getDate() != null && moFieldDateDocLapsing_n.getDate().before(moFieldDate.getDate())) {
                        validation.setMessage("La fecha del campo '" + jlDateDocLapsing_n.getText() + "' no puede ser anterior a la del campo '" + jlDate.getText() + "'.");
                        validation.setComponent(jftDateDocLapsing_n);
                    }
                    else if (moFieldDateDocDelivery_n.getDate() != null && moFieldDateDocLapsing_n.getDate() != null && moFieldDateDocLapsing_n.getDate().before(moFieldDateDocDelivery_n.getDate())) {
                        validation.setMessage("La fecha del campo '" + jlDateDocLapsing_n.getText() + "' no puede ser anterior a la del campo '" + jlDateDocDelivery_n.getText() + "'.");
                        validation.setComponent(jftDateDocLapsing_n);
                    }
                    else if (mbIsCon && moFieldDateDocDelivery_n.getDate() == null) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDateDocDelivery_n.getText() + "'.");
                        validation.setComponent(jftDateDocDelivery_n);
                    }
                    else if (mbIsCon && moFieldDateDocLapsing_n.getDate() == null) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDateDocLapsing_n.getText() + "'.");
                        validation.setComponent(jftDateDocLapsing_n);
                    }
                    else if (moFieldDateShipment_n.getDate() != null && moFieldDateShipment_n.getDate().before(moFieldDate.getDate())) {
                        validation.setMessage("La fecha del campo '" + jlDateShipment_n.getText() + "' no puede ser anterior a la del campo '" + jlDate.getText() + "'.");
                        validation.setComponent(jftDateShipment_nRo);
                    }
                    else if (moFieldDateDelivery_n.getDate() != null && moFieldDateDelivery_n.getDate().before(moFieldDate.getDate())) {
                        validation.setMessage("La fecha del campo '" + jlDateDelivery_n.getText() + "' no puede ser anterior a la del campo '" + jlDate.getText() + "'.");
                        validation.setComponent(jftDateDelivery_nRo);
                    }
                    else if (isCfdNeeded() && moPaneGridEntries.getTableGuiRowCount() == 0) {
                        validation.setMessage("El CFD debe tener al menos una partida.");
                    }
                    else if (isCfdiNeeded() && moPaneGridEntries.getTableGuiRowCount() == 0) {
                        validation.setMessage("El CFDI debe tener al menos una partida.");
                    }
                    else {
                        if (moFieldNumberSeries.getDataType() == SLibConstants.DATA_TYPE_KEY) {
                            int num = SLibUtilities.parseInt(moFieldNumber.getString());
                            int numMin = ((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[0];
                            int numMax = ((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[1];

                            if (num < numMin) {
                                validation.setMessage("El valor para el campo '" + jlNumber.getText() + "' no puede ser menor a " + numMin + "'.");
                                validation.setComponent(jtfNumber);
                            }
                            else if (numMax != -1 && num > numMax) {
                                validation.setMessage("El valor para el campo '" + jlNumber.getText() + "' no puede ser mayor a " + numMax + "'.");
                                validation.setComponent(jtfNumber);
                            }
                        }

                        if (!validation.getIsError()) {
                            if (!isCreditRiskOk(moBizPartnerCategory.getEffectiveRiskTypeId(), false)) {
                                validation.setIsError(true);
                                validation.setComponent(jftDate);
                            }

                            if (!validation.getIsError()) {
                                if (mbIsDoc || mbIsAdj) {
                                    // Check manual accounting record:

                                    if (jckRecordUser.isSelected()) {
                                        if (moRecordUser == null) {
                                            validation.setMessage("No fue posible leer el registro '" + jckRecordUser.getText() + "'.");
                                            validation.setComponent(jbRecordManualSelect);
                                        }
                                        else if (moRecordUserLock == null) {
                                            validation.setMessage("No fue posible obtener el acceso exclusivo al registro '" + jckRecordUser.getText() + "'.");
                                            validation.setComponent(jbRecordManualSelect);
                                        }
                                        else if (!SLibTimeUtilities.isBelongingToPeriod(moFieldDate.getDate(), moRecordUser.getPkYearId(), moRecordUser.getPkPeriodId())) {
                                            if (miClient.showMsgBoxConfirm(SLibConstants.MSG_ERR_GUI_PER_DATE_REC + "\n" + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                                                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + jlDate.getText() + "'.");
                                                validation.setComponent(jftDate);
                                            }
                                            else if (!SDataUtilities.isPeriodOpen(miClient, moRecordUser.getDate())) {
                                                validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
                                                validation.setComponent(jckRecordUser);
                                            }
                                        }
                                        
                                        if (!validation.getIsError()) {
                                            try {
                                                SSrvUtils.verifyLockStatus(miClient.getSession(), moRecordUserLock);
                                            }
                                            catch (Exception e) {
                                                validation.setMessage("No fue posible validar el acceso exclusivo al registro '" + jckRecordUser.getText() + "'.\n" + e);
                                                validation.setComponent(jbRecordManualSelect);
                                            }
                                        }
                                    }

                                    if (!validation.getIsError()) {
                                        if (isCfdNeeded() || isCfdiNeeded()) {
                                            if (miClient.getSessionXXX().getParamsCompany().getDbmsDataCertificate_n() == null) {
                                                validation.setMessage("No se ha configurado un certificado de sello digital (CSD).");
                                            }
                                            else if (miClient.getSessionXXX().getParamsCompany().getDbmsDataCertificate_n().getDate().compareTo(moFieldDate.getDate()) > 0) {
                                                validation.setMessage("La vigencia del certificado de sello digital (CSD) actual es inválida para la fecha del documento " +
                                                        "(" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + "):\n" +
                                                        "La vigencia del certificado comienza el " + SLibUtils.DateFormatDate.format(miClient.getSessionXXX().getParamsCompany().getDbmsDataCertificate_n().getDate()) + ".");
                                            }
                                            else if (miClient.getSessionXXX().getParamsCompany().getDbmsDataCertificate_n().getExpirationDate().compareTo(moFieldDate.getDate()) < 0) {
                                                validation.setMessage("La vigencia del certificado de sello digital (CSD) actual es inválida para la fecha del documento " +
                                                        "(" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + "):\n" +
                                                        "El certificado expiró el " + SLibUtils.DateFormatDate.format(miClient.getSessionXXX().getParamsCompany().getDbmsDataCertificate_n().getExpirationDate()) + ".");
                                            }
                                        }
                                    }

                                    if (!validation.getIsError()) {
                                        // Check if DPS can be saved:

                                        SDataDps dps = (SDataDps) getRegistry();
                                        SServerRequest request = new SServerRequest(SServerConstants.REQ_DB_CAN_SAVE);
                                        SServerResponse response = null;

                                        request.setPacket(dps);
                                        response = miClient.getSessionXXX().request(request);

                                        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK || response.getResultType() != SLibConstants.DB_CAN_SAVE_YES) {
                                            validation.setMessage(response.getMessage().isEmpty() ? SLibConstants.MSG_ERR_UTIL_UNKNOWN_ERR : response.getMessage());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (!validation.getIsError()) {
                if ((isCfdNeeded() || isCfdiNeeded()) && moBizPartnerCategory.getFkCfdAddendaTypeId() != SDataConstantsSys.BPSS_TP_CFD_ADD_NA) {
                    switch (moBizPartnerCategory.getFkCfdAddendaTypeId()) {
                        case SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA:
                            /* XXX Code for future versions
                             if (moFieldBulkQty.getString().length() == 0) {
                                validation.setMessage("No se ha especificado la cantidad de " + " '" + jcbBulkType.getSelectedItem() + "'.");
                                jtfBulkQty.requestFocus();
                            }
                            */
                            if (moFieldDateRemission.getDate() == null) {
                                validation.setMessage(SLibConstants.MSG_ERR_GUI_DATE);
                                jTabbedPane.setSelectedIndex(3);
                                validation.setComponent(jftRemissionDate);
                            }
                            if (!mbIsAdj && moFieldDateDocDelivery_n.getDate() == null) {
                                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDateDocDelivery_n.getText() + "'.");
                                jTabbedPane.setSelectedIndex(3);
                                validation.setComponent(jftDateDocDelivery_n);
                            }
                            break;
                        case SDataConstantsSys.BPSS_TP_CFD_ADD_LOREAL:
                            if (moFieldNumberReference.getString().length() == 0) {
                                validation.setMessage("No se ha especificado una " + " '" + jtfNumberReference.getToolTipText() + "'.");
                                jtfNumberReference.requestFocus();
                            }
                             else {
                                for (SDataDpsEntry entry : moDps.getDbmsDpsEntries()) {
                                    if (entry.getDbmsDpsAddLorealEntryNumber() == 0 && !entry.getIsDeleted()) {
                                        validation.setMessage("No se han especificado datos requeridos para la addenda en una o varias partidas.");
                                        jTabbedPane.setSelectedIndex(0);
                                    }
                                }
                            }
                            break;
                        case SDataConstantsSys.BPSS_TP_CFD_ADD_BACHOCO:
                            if (moFieldNumberReference.getString().length() == 0) {
                                validation.setMessage("No se ha especificado una " + " '" + jtfNumberReference.getToolTipText() + "'.");
                                jtfNumberReference.requestFocus();
                            }
                            else {
                                for (SDataDpsEntry entry : moDps.getDbmsDpsEntries()) {
                                    if (entry.getDbmsDpsAddBachocoCenter().length() == 0 && !entry.getIsDeleted()) {
                                        validation.setMessage("No se han especificado datos requeridos para la addenda en una o varias partidas.");
                                        jTabbedPane.setSelectedIndex(0);
                                    }
                                }
                            }
                            break;
                        case SDataConstantsSys.BPSS_TP_CFD_ADD_MODELO:
                            if (moFieldNumberReference.getString().length() == 0) {
                                validation.setMessage("No se ha especificado una " + " '" + jtfNumberReference.getToolTipText() + "'.");
                                jtfNumberReference.requestFocus();
                            }
                            break;
                        default:
                    }

                    if (moBizPartner.getDbmsCategorySettingsCus().getCompanyKey().length() == 0) {
                        validation.setMessage("No se han especificado la clave de la empresa\n" +
                                "como proveedor del cliente " + moBizPartner.getBizPartner() + "\n" +
                                "en el catálogo de clientes, categoría: 'Cliente', campo: 'Clave empresa (asignada por el AN):'.");
                    }
                }
            }

            if (!validation.getIsError()) {
                if (moFieldFkIncotermId.getKeyAsIntArray()[0] != SModSysConsts.LOGS_INC_NA) {
                    if (jcbFkSpotSrcId_n.getSelectedIndex() <= 0) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkSpotSrcId_n.getText() + "'.");
                        validation.setComponent(jcbFkSpotSrcId_n);
                        jTabbedPane.setSelectedIndex(1);
                    }
                    else if (jcbFkSpotDesId_n.getSelectedIndex() <= 0) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkSpotDesId_n.getText() + "'.");
                        validation.setComponent(jcbFkSpotDesId_n);
                        jTabbedPane.setSelectedIndex(1);
                    }
                    else {
                        switch (moFieldFkIncotermId.getKeyAsIntArray()[0]) {
                            case SModSysConsts.LOGS_INC_EXW:
                            case SModSysConsts.LOGS_INC_DAP:
                            case SModSysConsts.LOGS_INC_FCA:
                            case SModSysConsts.LOGS_INC_CPT:
                            case SModSysConsts.LOGS_INC_CIP:
                            case SModSysConsts.LOGS_INC_DDP:
                            case SModSysConsts.LOGS_INC_DAT:
                                if (moFieldFkModeOfTransportationTypeId.getKeyAsIntArray()[0] == SModSysConsts.LOGS_TP_MOT_NA) {
                                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFkModeOfTransportationTypeId.getText() + "'.");
                                    validation.setComponent(jcbFkModeOfTransportationTypeId);
                                    jTabbedPane.setSelectedIndex(1);
                                }
                                break;
                            case SModSysConsts.LOGS_INC_FAS:
                            case SModSysConsts.LOGS_INC_FOB:
                            case SModSysConsts.LOGS_INC_CFR:
                            case SModSysConsts.LOGS_INC_CIF:
                                if (moFieldFkModeOfTransportationTypeId.getKeyAsIntArray()[0] != SModSysConsts.LOGS_TP_MOT_SEA) {
                                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFkModeOfTransportationTypeId.getText() + "'.");
                                    validation.setComponent(jcbFkModeOfTransportationTypeId);
                                    jTabbedPane.setSelectedIndex(1);
                                }
                                break;
                            default:
                        }
                    }
                }
            }   
            
            if (!validation.getIsError()) {
                if (mnSalesSupervisorId_n != 0 && mnSalesAgentId_n == 0) {
                    validation.setMessage("Se debe ingresar un valor para el campo '" + jlSalesAgent.getText() + "'.");
                    validation.setComponent(jbSalesAgent);
                    jTabbedPane.setSelectedIndex(1);
                }
                else if (mnSalesSupervisorId_n != 0 && mnSalesSupervisorId_n == mnSalesAgentId_n) {
                    validation.setMessage("Se debe ingresar un valor diferente para el campo '" + jlSalesAgent.getText() + "'.");
                    validation.setComponent(jbSalesAgent);
                    jTabbedPane.setSelectedIndex(1);
                }
            }
            
            if (!validation.getIsError()) {
                // Check if document items need to be added to document from source document:
                
                for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
                    SDataDpsEntry entry = (SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData();
                    
                    try {
                        if (!mbHasRightOmitSourceDoc) { //condition for check items
                            STrnUtils.checkItemStandaloneDoc(miClient.getSession(), moDps.getDpsTypeKey(), entry.getFkItemId(), entry.hasDpsLinksAsDestiny());
                        }
                    }
                    catch (Exception e) {
                        validation.setMessage(e.getMessage());
                        validation.setComponent(moPaneGridEntries);
                        SLibUtilities.printOutException(this, e);
                    }
                }
            }
            
            if (!validation.getIsError() && (SLibUtilities.compareKeys(manDpsClassKey, SDataConstantsSys.TRNS_CL_DPS_SAL_ORD) || SLibUtilities.compareKeys(manDpsClassKey, SDataConstantsSys.TRNS_CL_DPS_SAL_DOC))) {
                String shipmentMessageMissingData = validateRequiredShipmentData();
                
                if (!shipmentMessageMissingData.isEmpty()) {
                        validation.setMessage(shipmentMessageMissingData);
                        validation.setComponent(moPaneGridEntries);
                }
                else{
                    String shipmentMessage = validateProvisionalShipmentData();
                
                    if (!shipmentMessage.isEmpty() && miClient.showMsgBoxConfirm(shipmentMessage) != JOptionPane.YES_OPTION) {
                        validation.setMessage("Revise la información de las partidas.");
                        validation.setComponent(moPaneGridEntries);
                    }
                }
            }
            
            if (!validation.getIsError()) {
                // Validate prepayments:
                
                if (mdPrepaymentsCy > 0) {
                    for (STableRow row : moPaneGridEntries.getGridRows()) {
                        if (((SDataDpsEntry) row.getData()).getFkItemId() == mnPrepaymentsItemId) {
                            prepaymentsCy += ((SDataDpsEntry) row.getData()).getSubtotalCy_r();
                        }
                    }
                    
                    if (prepaymentsCy == 0) {
                        if (miClient.showMsgBoxConfirm("'" + moBizPartner.getBizPartner() + "' tiene anticipos,\n"
                                + "¿está seguro que no desea aplicarlos en este documento?") != JOptionPane.YES_OPTION) {
                            validation.setMessage("Se debe aplicar anticipos en este documento.");
                            validation.setComponent(moPaneGridEntries);
                        }
                    }
                    else if (mdPrepaymentsCy + prepaymentsCy < 0) {
                        validation.setMessage("La aplicación total de anticipos $ " + SLibUtils.getDecimalFormatAmount().format(-prepaymentsCy) + " " + jtfCurrencyKeyRo.getText() + " "
                                + "no puede ser mayor al saldo actual de anticipos $ " + SLibUtils.getDecimalFormatAmount().format(mdPrepaymentsCy) + " " + jtfCurrencyKeyRo.getText() + ".");
                        validation.setComponent(moPaneGridEntries);
                    }
                }
            }
            
            if (!validation.getIsError() && mbIsCon) {
                // Validate contract:
                
                int months = 0;
                int totalDeliveryMonths = 0;
                int[] dateStart = null;
                int[] dateEnd = null;
                            
                dateStart = SLibTimeUtilities.digestYearMonth(moFieldDateDocDelivery_n.getDate());
                dateEnd = SLibTimeUtilities.digestYearMonth(moFieldDateDocLapsing_n.getDate());
                
                months = (dateEnd[0] - dateStart[0]) * SLibTimeConsts.MONTH_MAX + dateEnd[1] - dateStart[1] + 1;

                for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
                   for (SDataDpsEntryPrice price : ((SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData()).getDbmsEntryPrices()) {
                        if (!price.getIsDeleted()) {
                            totalDeliveryMonths ++;
                        }
                   }
                   if (totalDeliveryMonths != months) {
                        validation.setMessage("El número de entregas mensuales capturadas en la partida número '" + (i + 1) + "' no coinciden con periodo de entrega del documento, en total deben ser " + months + ".");
                        validation.setComponent(moPaneGridEntries);
                        jTabbedPane.setSelectedIndex(0);
                        break;
                    }
                    
                    for (SDataDpsEntryPrice price : ((SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData()).getDbmsEntryPrices()) {
                        if (!price.getIsDeleted()) {
                            totalDeliveryMonths ++;
                            if (!SLibTimeUtilities.isBelongingToPeriod(SLibTimeUtilities.createDate(price.getContractPriceYear(), price.getContractPriceMonth()), SLibTimeUtilities.getBeginOfMonth(moFieldDateDocDelivery_n.getDate()), SLibTimeUtilities.getEndOfMonth(moFieldDateDocLapsing_n.getDate()))) {
                                validation.setMessage("La entrega mensual '" + miClient.getSessionXXX().getFormatters().getDateYearMonthFormat().format(SLibTimeUtilities.createDate(price.getContractPriceYear(), price.getContractPriceMonth())) + "' de la partida '" + (i + 1) + "' no se encuentra dentro del periodo de entrega del documento.");
                                validation.setComponent(moPaneGridEntries);
                                jTabbedPane.setSelectedIndex(0);
                                break;
                            }
                        }
                    }
                    
                    totalDeliveryMonths = 0;
                }
            }
            
            if (!validation.getIsError()) {
                 if (jckIsDeleted.isSelected()) {
                     if (miClient.showMsgBoxConfirm("El documento está eliminado. Puede guardarlo de nuevo como eliminado o reactivarlo.\n¿Desea reactivar el documento?") == JOptionPane.YES_OPTION) {
                         jckIsDeleted.setSelected(false);
                     }
                 }
            }
        }

        return validation;
    }

    @Override
    public void setFormStatus(int status) {
        mnFormStatus = status;
    }

    @Override
    public void setFormVisible(boolean visible) {
        setVisible(visible);
    }

    @Override
    public int getFormStatus() {
        return mnFormStatus;
    }

    @Override
    public int getFormResult() {
        return mnFormResult;
    }

    @Override
    public void setRegistry(erp.lib.data.SDataRegistry registry) {
        mbResetingForm = true;

        moDps = (SDataDps) registry;

        moFieldDate.setFieldValue(moDps.getDate());
        moFieldDateDoc.setFieldValue(moDps.getDateDoc());
        moFieldDateStartCredit.setFieldValue(moDps.getDateStartCredit());
        moFieldDateShipment_n.setFieldValue(moDps.getDateShipment_n());
        moFieldDateDelivery_n.setFieldValue(moDps.getDateDelivery_n());
        moFieldDateDocLapsing_n.setFieldValue(moDps.getDateDocLapsing_n());
        moFieldDateDocDelivery_n.setFieldValue(moDps.getDateDocDelivery_n());

        moFieldNumberSeries.setFieldValue(moDps.getNumberSeries());

        if (isNumberSeriesBySystem() && jcbNumberSeries.getSelectedIndex() == -1) {
            int number = SLibUtilities.parseInt(moDps.getNumber());
            SFormComponentItem item = new SFormComponentItem(new Object[] { moDps.getNumberSeries() }, moDps.getNumberSeries());
            item.setComplement(new int[] { number, number });
            jcbNumberSeries.removeAllItems();
            jcbNumberSeries.addItem(item);
            jcbNumberSeries.setSelectedIndex(0);
            mbIsNumberEditable = false;     // there is no way to validate any number change for there is not a current company branch selected in user session
        }

        moFieldNumber.setFieldValue(moDps.getNumber());
        moFieldNumberReference.setFieldValue(moDps.getNumberReference());
        moFieldDaysOfCredit.setFieldValue(moDps.getDaysOfCredit());
        moFieldIsDiscountDocApplying.setFieldValue(moDps.getIsDiscountDocApplying());
        moFieldIsDiscountDocPercentage.setFieldValue(moDps.getIsDiscountDocPercentage());
        moFieldDiscountDocPercentage.setFieldValue(moDps.getDiscountDocPercentage());
        moFieldExchangeRate.setFieldValue(moDps.getExchangeRate());
        moFieldExchangeRateSystem.setFieldValue(moDps.getExchangeRateSystem());
        moFieldDriver.setFieldValue(moDps.getDriver());
        moFieldPlate.setFieldValue(moDps.getPlate());
        moFieldTicket.setFieldValue(moDps.getTicket());
        moFieldShipments.setFieldValue(moDps.getShipments());
        moFieldPayments.setFieldValue(moDps.getPayments());
        moFieldIsLinked.setFieldValue(moDps.getIsLinked());
        moFieldIsClosed.setFieldValue(moDps.getIsClosed());
        moFieldIsAudited.setFieldValue(moDps.getIsAudited());
        moFieldIsAuthorized.setFieldValue(moDps.getIsAuthorized());
        moFieldIsCopy.setFieldValue(moDps.getIsCopy());
        moFieldIsSystem.setFieldValue(moDps.getIsSystem());
        moFieldIsDeleted.setFieldValue(moDps.getIsDeleted());
        moFieldFkPaymentTypeId.setFieldValue(new int[] { moDps.getFkPaymentTypeId() });
        moFieldFkPaymentSystemTypeId.setFieldValue(new int[] { moDps.getFkPaymentSystemTypeId() });
        renderLastPaymentSettings(moDps.getFkBizPartnerId_r());
        moFieldCfdConfirmationNumber.setFieldValue(moDps.getXtaCfdConfirmacion());
        moFieldFkLanguajeId.setFieldValue(new int[] { moDps.getFkLanguajeId() });
        moFieldFkFunctionalAreaId.setFieldValue(new int[] { moDps.getFkFunctionalAreaId() });
        moFieldFkDpsNatureId.setFieldValue(new int[] { moDps.getFkDpsNatureId() });
        moFieldFkCurrencyId.setFieldValue(new int[] { !mbIsLocalCurrency ? moDps.getFkCurrencyId() : miClient.getSessionXXX().getParamsErp().getFkCurrencyId()});
        setBizPartner(new int[] { moDps.getFkBizPartnerId_r() }, new int[] { moDps.getFkBizPartnerBranchId() }, new int[] { moDps.getFkBizPartnerBranchId(), moDps.getFkBizPartnerBranchAddressId() });
        moFieldFkIncotermId.setFieldValue(new int[] { moDps.getFkIncotermId() });
        itemChangeFkIncotermId();
        moFieldFkSpotSrcId_n.setFieldValue(new int[] { moDps.getFkSpotSourceId_n() });
        moFieldFkSpotDesId_n.setFieldValue(new int[] { moDps.getFkSpotDestinyId_n() });
        moFieldFkModeOfTransportationTypeId.setFieldValue(new int[] { moDps.getFkModeOfTransportationTypeId() });
        moFieldFkCarrierTypeId.setFieldValue(new int[] { moDps.getFkCarrierTypeId() });
        itemChangeFkCarrierTypeId();
        moFieldFkCarrierId_n.setFieldValue(new int[] { moDps.getFkCarrierId_n() });
        moFieldFkVehicleTypeId_n.setFieldValue(new int[] { moDps.getFkVehicleTypeId_n() });
        itemChangeFkVehicleTypeId_n();
        moFieldFkVehicleId_n.setFieldValue(new int[] { moDps.getFkVehicleId_n() });
        moFieldFkProductionOrderId_n.setFieldValue(new int[] { moDps.getFkMfgYearId_n(), moDps.getFkMfgOrderId_n() });
        
        try {
            moFieldFkCfdUseId.setFieldValue(SCatalogXmlUtils.getNameEntry((SGuiClient) miClient, SDataConstantsSys.TRNS_CFD_CAT_CFD_USE, moDps.getDate(), moDps.getXtaCfdUsoCfdi()));
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        for (SDataDpsEntry entry : moDps.getDbmsDpsEntries()) {
            moPaneGridEntries.addTableRow(new SDataDpsEntryRow(entry, ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter()));
        }
        moPaneGridEntries.renderTableRows();
        moPaneGridEntries.setTableRowSelection(0);

        for (SDataDpsNotes notes : moDps.getDbmsDpsNotes()) {
            if (notes.getPkNotesId() != SLibConstants.UNDEFINED) {
                moPaneGridNotes.addTableRow(new SDataDpsNotesRow(notes));
            }
            else if (notes.getIsAllDocs()) {
                moPaneGridNotes.addTableRow(new SDataDpsNotesRow(notes));
            }
        }
        moPaneGridNotes.renderTableRows();
        moPaneGridNotes.setTableRowSelection(0);

        moFieldFkContactId_n.setFieldValue(new int[] { moDps.getFkContactBizPartnerBranchId_n(), moDps.getFkContactContactId_n() });    // setBizPartner() populates contact combobox for sales documents

        if (!moDps.getIsRecordAutomatic()) {
            if (readRecordUser(moDps.getDbmsRecordKey())) {
                moRecordUserKey = moDps.getDbmsRecordKey();
            }
        }

        renderDpsType();
        renderDpsValue();
        renderDpsStatus();
        renderRecordManual();
        renderRecordAutomatic();
        renderBizPartner();
        renderBizPartnerPrepaymentsBalance();
        renderDateMaturity();
        renderSalesAgentBizPartner(moDps.getFkSalesAgentBizPartnerId_n() == 0 ? null : new int[] { moDps.getFkSalesAgentBizPartnerId_n() });
        renderSalesAgent(moDps.getFkSalesAgentId_n() == 0 ? null : new int[] { moDps.getFkSalesAgentId_n() });
        renderSalesSupervisorBizPartner(moDps.getFkSalesSupervisorBizPartnerId_n() == 0 ? null : new int[] { moDps.getFkSalesSupervisorBizPartnerId_n() });
        renderSalesSupervisor(moDps.getFkSalesSupervisorId_n() == 0 ? null : new int[] { moDps.getFkSalesSupervisorId_n() });

        jckDateDoc.setSelected(!moDps.getDate().equals(moDps.getDateDoc()));
        jckDateStartCredit.setSelected(!moDps.getDate().equals(moDps.getDateStartCredit()));
        jckRecordUser.setSelected(!moDps.getIsRecordAutomatic());
        jckPayments.setSelected(moFieldPayments.getInteger() > 0);
        jckShipments.setSelected(moFieldShipments.getInteger() > 0);
        jckIsRebill.setSelected(moDps.getIsRebill());
        itemStateDateDoc();
        itemStateDateStartCredit();
        itemStateRecordManual();
        itemStatePayments();
        itemStateShipments();
        itemStateIsDiscountDocApplying(false);
        itemStateIsDiscountDocPercentage(false);

        itemChangeFkPaymentTypeId(false);
        itemChangeFkPaymentSystemTypeId();
        itemChangeFkLanguageId();
        itemChangeFkCurrencyId(false);
        itemChangeFkCarrierTypeId();

        mbIsPeriodOpen = SDataUtilities.isPeriodOpen(miClient, moDps.getDate());

        updateFormEditStatus(false);

        // The sequence of asumptions are set this way in order to preserve correspondence with method actionEditHelp():

        jbEdit.setEnabled(!(mbParamIsReadOnly || moDps.getIsAudited() || moDps.getIsAuthorized() || moDps.getIsSystem() ||
                (!mbIsPeriodOpen && (mbIsDoc || mbIsAdj)) || moDps.getFkDpsValidityStatusId() != SDataConstantsSys.TRNS_ST_DPS_VAL_EFF ||
                (mnParamCurrentUserPrivilegeLevel != SDataConstantsSys.UNDEFINED && mnParamCurrentUserPrivilegeLevel < SUtilConsts.LEV_AUTHOR) ||
                (moDps.isDocumentSal() && moDps.getDbmsDataCfd() != null && (moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && moDps.getDbmsDataCfd().isStamped()) ||
                (moDps.isAdjustmentSal() && moDps.getDbmsDataCfd() != null && (moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && moDps.getDbmsDataCfd().isStamped()) ||
                (moDps.isDocumentSal() && moDps.getDbmsDataCfd() != null && (moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && moDps.getDbmsDataCfd().getIsProcessingWebService()) ||
                (moDps.isDocumentSal() && moDps.getDbmsDataCfd() != null && (moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && moDps.getDbmsDataCfd().getIsProcessingStorageXml()) ||
                (moDps.isAdjustmentSal() && moDps.getDbmsDataCfd() != null && (moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && moDps.getDbmsDataCfd().getIsProcessingWebService()) ||
                (moDps.isAdjustmentSal() && moDps.getDbmsDataCfd() != null && (moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || moDps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && moDps.getDbmsDataCfd().getIsProcessingStorageXml())));

        jbEditHelp.setEnabled(!jbEdit.isEnabled());
        jbOk.setEnabled(false);

        if ((mbIsDoc || mbIsAdj) && (!mbIsSales || isCfdNeeded() || isCfdiNeeded())) {
            jTabbedPane.setEnabledAt(3, true);
            setAddendaData();
            if (!mbIsSales && moDps.getDbmsDataCfd() != null) {
                moFieldFileXml.setFieldValue(moDps.getDbmsDataCfd().getDocXmlName());
            }
        }
        
        if (!mbIsSales && (mbIsDoc || mbIsAdj)) {
            
        }

        mdOrigExchangeRate = moFieldExchangeRate.getDouble();
        mdOrigDiscountDocPercentage = moFieldDiscountDocPercentage.getDouble();
        mbOrigIsDiscountDocApplying = moFieldIsDiscountDocApplying.getBoolean();
        
        if (moGuiDpsLink == null) {
            moGuiDpsLink = new SGuiDpsLink(miClient);
            moGuiDpsLink.addDataDpsDestiny(moDps);
        }

        mbResetingForm = false;
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        SDataCfd cfd = null;
        String sFileXml = "";
        SDataDpsEntry entry = null;

        if (moDps.getIsRegistryNew()) {
            moDps.setPkYearId(SLibTimeUtilities.digestYear(moFieldDate.getDate())[0]);
            moDps.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moDps.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        calculateTotal();

        moDps.setDate(moFieldDate.getDate());
        moDps.setDateDoc(moFieldDateDoc.getDate());
        moDps.setDateStartCredit(moFieldDateStartCredit.getDate());
        moDps.setDateShipment_n(moFieldDateShipment_n.getDate());
        moDps.setDateDelivery_n(moFieldDateDelivery_n.getDate());
        moDps.setDateDocLapsing_n(moFieldDateDocLapsing_n.getDate());
        moDps.setDateDocDelivery_n(moFieldDateDocDelivery_n.getDate());
        moDps.setNumberSeries(moFieldNumberSeries.getString());
        moDps.setNumber(moFieldNumber.getString());
        moDps.setNumberReference(moFieldNumberReference.getString());
        moDps.setDaysOfCredit(moFieldDaysOfCredit.getInteger());

        if (mbIsSales && (mbIsDoc || mbIsAdj)) {
            if (moDps.getIsRegistryNew()) {
                moDps.setApproveYear(((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[2]);
                moDps.setApproveNumber(((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[3]);
            }
        }
        else {
            moDps.setApproveYear(0);
            moDps.setApproveNumber(0);
        }

        moDps.setIsRebill(jckIsRebill.isSelected());

        moDps.setDriver(!moFieldDriver.getComponent().isEnabled() ? "" : moFieldDriver.getString());
        moDps.setPlate(!moFieldPlate.getComponent().isEnabled() ? "" : moFieldPlate.getString());
        moDps.setTicket(!moFieldTicket.getComponent().isEnabled() ? "" : moFieldTicket.getString());
        moDps.setIsRecordAutomatic(!jckRecordUser.isSelected());
        moDps.setShipments(!moFieldShipments.getComponent().isEnabled() ? 0 : moFieldShipments.getInteger());
        moDps.setPayments(!moFieldPayments.getComponent().isEnabled() ? 0 : moFieldPayments.getInteger());

        // Fields non editable in form (allready set for new documents in formReset() function):

        /*
        moDps.setIsLinked(...
        moDps.setIsClosed(...
        moDps.setIsAudited(...
        moDps.setIsAuthorized(...
        moDps.setIsRecordAutomatic(...
        */
        moDps.setIsCopy(moFieldIsCopy.getBoolean());
        /*
        moDps.setIsSystem(...
        */
        moDps.setIsDeleted(moFieldIsDeleted.getBoolean());  // when document was deleted, user can reactivate it on save

        moDps.setFkDpsCategoryId(moDpsType.getPkDpsCategoryId());
        moDps.setFkDpsClassId(moDpsType.getPkDpsClassId());
        moDps.setFkDpsTypeId(moDpsType.getPkDpsTypeId());
        
        moDps.setFkPaymentTypeId(moFieldFkPaymentTypeId.getKeyAsIntArray()[0]);
        
        moDps.setFkPaymentSystemTypeId(moFieldFkPaymentSystemTypeId.getKeyAsIntArray()[0] == SLibConstants.UNDEFINED ? SDataConstantsSys.TRNU_TP_PAY_SYS_NA : moFieldFkPaymentSystemTypeId.getKeyAsIntArray()[0]);
        moDps.setPaymentMethod(moFieldFkPaymentSystemTypeId.getString());
        moDps.setPaymentAccount(moFieldPaymentAccount.getString());
        moDps.setXtaCfdConfirmacion(moFieldCfdConfirmationNumber.getString());
        
        moDps.setFkDpsStatusId(SDataConstantsSys.TRNS_ST_DPS_EMITED);   // all saved documents have "emited" status
        moDps.setFkDpsNatureId(SDataConstantsSys.TRNU_DPS_NAT_DEF);   // all saved documents have "default" nature

        // Fields non editable in form (allready set for new documents in formReset() function):

        /*
        moDps.setFkDpsAuthorizationStatusId(...
        moDps.setFkCompanyBranchId(...
        */

        moDps.setFkBizPartnerId_r(moBizPartner.getPkBizPartnerId());
        moDps.setFkBizPartnerBranchId(moBizPartnerBranchAddress.getPkBizPartnerBranchId());
        moDps.setFkBizPartnerBranchAddressId(moBizPartnerBranchAddress.getPkAddressId());
        moDps.setFkBizPartnerAltId_r(moBizPartner.getPkBizPartnerId());
        moDps.setFkBizPartnerBranchAltId(moBizPartnerBranchAddress.getPkBizPartnerBranchId());
        moDps.setFkBizPartnerBranchAddressAltId(moBizPartnerBranchAddress.getPkAddressId());

        // Fields non editable in form (allready set for new documents in updateInDpsBizPartnerSettings() function):

        //moDps.setFkTaxIdentityEmisorTypeId(...
        //moDps.setFkTaxIdentityReceptorTypeId(...
        //moDps.setFkSalesAgentBizPartnerId_n(...

        moDps.setFkLanguajeId(moFieldFkLanguajeId.getKeyAsIntArray()[0]);
        moDps.setFkFunctionalAreaId(moFieldFkFunctionalAreaId.getKeyAsIntArray()[0]);
        moDps.setFkDpsNatureId(moFieldFkDpsNatureId.getKeyAsIntArray()[0]);
        moDps.setFkCurrencyId(moFieldFkCurrencyId.getKeyAsIntArray()[0]);
        moDps.setFkSalesAgentId_n(mnSalesAgentId_n);
        moDps.setFkSalesSupervisorId_n(mnSalesSupervisorId_n);

        if (jcbFkContactId_n.getSelectedIndex() <= 0) {
            moDps.setFkContactBizPartnerBranchId_n(0);
            moDps.setFkContactContactId_n(0);
        }
        else {
            moDps.setFkContactBizPartnerBranchId_n(moFieldFkContactId_n.getKeyAsIntArray()[0]);
            moDps.setFkContactContactId_n(moFieldFkContactId_n.getKeyAsIntArray()[1]);
        }

        moDps.setFkIncotermId(moFieldFkIncotermId.getKeyAsIntArray()[0]);
        moDps.setFkSpotSourceId_n(moFieldFkIncotermId.getKeyAsIntArray()[0] == SModSysConsts.LOGS_INC_NA || jcbFkSpotSrcId_n.getSelectedIndex() <= 0 ? SLibConsts.UNDEFINED : moFieldFkSpotSrcId_n.getKeyAsIntArray()[0]);
        moDps.setFkSpotDestinyId_n(moFieldFkIncotermId.getKeyAsIntArray()[0] == SModSysConsts.LOGS_INC_NA || jcbFkSpotDesId_n.getSelectedIndex() <= 0  ? SLibConsts.UNDEFINED : moFieldFkSpotDesId_n.getKeyAsIntArray()[0]);
        moDps.setFkModeOfTransportationTypeId(moFieldFkModeOfTransportationTypeId.getKeyAsIntArray()[0]);
        moDps.setFkCarrierTypeId(moFieldFkCarrierTypeId.getKeyAsIntArray()[0]);
        moDps.setFkCarrierId_n(moFieldFkCarrierId_n.getKeyAsIntArray()[0]);
        moDps.setFkVehicleTypeId_n(moFieldFkVehicleTypeId_n.getKeyAsIntArray()[0]);
        moDps.setFkVehicleId_n(moFieldFkVehicleId_n.getKeyAsIntArray()[0]);
        //moDps.setFkSourceYearId_n(...
        //moDps.setFkSourceDocId_n(...
        moDps.setFkMfgYearId_n(moFieldFkProductionOrderId_n.getKeyAsIntArray()[0]);
        moDps.setFkMfgOrderId_n(moFieldFkProductionOrderId_n.getKeyAsIntArray()[1]);
        //moDps.setFkCfdRelationType(...
        moDps.setXtaCfdUsoCfdi((String) moFieldFkCfdUseId.getKey());

        // Fields non editable in form (allready set for new documents in formReset() function):

        //moDps.setFkUserClosedId(...
        //moDps.setFkUserAuditedId(...
        //moDps.setFkUserAuthorizedId(...

        moDps.getDbmsDpsEntries().clear();
        for (STableRow row : moPaneGridEntries.getGridRows()) {
            entry = (SDataDpsEntry) row.getData();
            if (!entry.getIsRegistryEdited()) {
                entry.setIsRegistryEdited(isUpdateEntriesAllNeeded());
            }
            moDps.getDbmsDpsEntries().add(entry);
        }

        moDps.getDbmsDpsNotes().clear();
        for (STableRow row : moPaneGridNotes.getGridRows()) {
            moDps.getDbmsDpsNotes().add((SDataDpsNotes) row.getData());
        }

        moDps.setDbmsCurrency(((SFormComponentItem) jcbFkCurrencyId.getSelectedItem()).getItem());
        moDps.setDbmsCurrencyKey((String) ((SFormComponentItem) jcbFkCurrencyId.getSelectedItem()).getComplement());

        // Set record user object:

        moDps.getRegistryComplements().clear();

        if (!jckRecordUser.isSelected()) {
            releaseRecordUserLock();    // if lock was gained, release it
            moDps.setDbmsRecordKey(null);
        }
        else {
            moDps.setDbmsRecordKey(moRecordUserKey);
            moDps.getRegistryComplements().add(moRecordUserLock);
        }

        // Set params for CFD:

        if (isCfdNeeded() || isCfdiNeeded()) {
            moDps.setAuxCfdParams(createCfdParams());
            moDps.setAuxIsNeedCfd(true);
        }

        if (!msFileXmlPath.isEmpty()) {
            try {
                cfd = moDps.getDbmsDataCfd() != null ? moDps.getDbmsDataCfd() : new SDataCfd();

                sFileXml = DUtilUtils.readXml(msFileXmlPath);
                if (cfd != null) {
                    cfd.setCertNumber("");
                    cfd.setStringSigned("");
                    cfd.setSignature("");
                    cfd.setDocXml(sFileXml);
                    cfd.setDocXmlName(moFieldFileXml.getString());
                    cfd.setIsConsistent(true);
                    cfd.setFkCfdTypeId(SDataConstantsSys.TRNS_TP_CFD_CFD);
                    cfd.setFkXmlTypeId(SDataConstantsSys.TRNS_TP_XML_NA);
                    cfd.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_EMITED);
                    cfd.setFkXmlDeliveryTypeId(SModSysConsts.TRNS_TP_XML_DVY_NA);
                    cfd.setFkXmlDeliveryStatusId(SModSysConsts.TRNS_ST_XML_DVY_PENDING);
                    cfd.setFkUserProcessingId(miClient.getSession().getUser().getPkUserId());
                    cfd.setFkUserDeliveryId(miClient.getSession().getUser().getPkUserId());

                    moDps.setDbmsDataCfd(cfd);
                }
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
        else {
            try {
                if (moFieldFileXml.getString().isEmpty() && moDps.getDbmsDataCfd() != null) {
                    cfd = moDps.getDbmsDataCfd();
                    
                    cfd.setDocXml("");
                    cfd.setDocXmlName("");
                    cfd.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW);
                    cfd.setIsConsistent(true);
                    
                    moDps.setDbmsDataCfd(cfd);

                }
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }            
            //moDps.setDbmsDataCfd(null);
        }

        return moDps;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case SLibConstants.VALUE_TYPE:
                setDpsType((int[]) value);
                break;
            case SDataConstantsSys.TRNX_TP_DPS_EST:
            case SDataConstantsSys.TRNX_TP_DPS_ORD:
            case SDataConstantsSys.TRNX_TP_DPS_DOC:
                moParamDpsSource = (SDataDps) value;
                break;
            case SDataConstants.TRNS_STP_DPS_ADJ:
                manParamAdjustmentSubtypeKey = (int[]) value;
                break;
            case SLibConstants.VALUE_STATUS:
                mbParamIsReadOnly = (Boolean) value;
                break;
            case SDataConstants.USRS_TP_LEV:
                mnParamCurrentUserPrivilegeLevel = (Integer) value;
                break;
            case SLibConstants.VALUE_CURRENCY_LOCAL:
                mbIsLocalCurrency = (Boolean) value;
                break;
            default:
        }
    }

    @Override
    public java.lang.Object getValue(int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JLabel getTimeoutLabel() {
        return null;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbEdit) {
                actionEdit();
            }
            else if (button == jbEditHelp) {
                actionEditHelp();
            }
            else if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbDate) {
                actionDate();
            }
            else if (button == jbDateDoc) {
                actionDateDoc();
            }
            else if (button == jbDateStartCredit) {
                actionDateStartCredit();
            }
            else if (button == jbBizPartnerBalance) {
                actionBizPartnerBalance();
            }
            else if (button == jbRecordManualSelect) {
                actionRecordManualSelect();
            }
            else if (button == jbRecordManualView) {
                actionRecordManualView();
            }
            else if (button == jbFkCurrencyId) {
                actionFkCurrencyId();
            }
            else if (button == jbExchangeRate) {
                actionExchangeRate();
            }
            else if (button == jbComputeTotal) {
                actionComputeTotal();
            }
            else if (button == jbPrepayments) {
                actionPrepayments();
            }
            else if (button == jbEntryNew) {
                actionEntryNew();
            }
            else if (button == jbEntryEdit) {
                actionEntryEdit();
            }
            else if (button == jbEntryDelete) {
                actionEntryDelete();
            }
            else if (button == jbEntryDiscountRetailChain) {
                actionEntryDiscountRetailChain(moLastDpsSource);
            }
            else if (button == jbEntryImportFromDps) {
                actionEntryImportFromDps(null);
            }
            else if (button == jbEntryWizard) {
                actionEntryWizard();
            }
            else if (button == jbEntryViewLinks) {
                actionEntryViewLinks();
            }
            else if (button == jbTaxRegionId) {
                actionTaxRegionId();
            }
            else if (button == jbNotesNew) {
                actionNotesNew();
            }
            else if (button == jbNotesEdit) {
                actionNotesEdit();
            }
            else if (button == jbNotesDelete) {
                actionNotesDelete();
            }
            else if (button == jbSystemNotes) {
                actionSystemNotes();
            }
            else if (button == jbDateDocDelivery_n) {
                actionDateDocDelivery_n();
            }
            else if (button == jbDateDocLapsing_n) {
                actionDateDocLapsing_n();
            }
            else if (button == jbSalesAgent) {
                actionSalesAgent();
            }
            else if (button == jbSalesSupervisor) {
                actionSalesSupervisor();
            }
            else if (button == jbFkProductionOrderId_n) {
                actionFkProductionOrderId_n();
            }
            else if (button == jbFkIncotermId) {
                actionFkIncotermId();
            }
            else if (button == jbFkModeOfTransportationTypeId) {
                actionFkModeOfTransportationTypeId();
            }
            else if (button == jbFkCarrierId_n) {
                actionFkCarrierId_n();
            }
            else if (button == jbFkVehicleId_n) {
                actionFkVehicleId_n();
            }
            else if (button == jbFileXml) {
                actionLoadFileXml();
            }
            else if (button == jbDeleteFileXml) {
                actionDeleteFileXml();
            }
        }
        if (e.getSource() instanceof javax.swing.JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) e.getSource();

            if (toggleButton == jtbEntryFilter) {
                actionEntryFilter();
            }
            else if (toggleButton == jtbNotesFilter) {
                actionNotesFilter();
            }
        }
    }

    @Override
    public void focusGained(java.awt.event.FocusEvent e) {

    }

    @Override
    public void focusLost(java.awt.event.FocusEvent e) {
        if (e.getSource() instanceof javax.swing.JTextField) {
            JTextField textField = (JTextField) e.getSource();

            if (textField == jftDate) {
                focusLostDate();
            }
            else if (textField == jftDateDoc) {
                focusLostDateDoc();
            }
            else if (textField == jftDateStartCredit) {
                focusLostDateStartCredit();
            }
            else if (textField == jtfDaysOfCredit) {
                focusLostDaysOfCredit();
            }
            else if (textField == jtfExchangeRate) {
                focusLostExchangeRate();
            }
            else if (textField == jtfDiscountDocPercentage) {
                focusLostDiscountDocPercentage();
            }
        }
    }

    @Override
    public void itemStateChanged(java.awt.event.ItemEvent e) {
        if (!mbResetingForm && !mbUpdatingForm) {
            if (e.getSource() instanceof javax.swing.JCheckBox) {
                JCheckBox checkBox = (JCheckBox) e.getSource();

                if (checkBox == jckDateDoc) {
                    itemStateDateDoc();
                }
                else if (checkBox == jckDateStartCredit) {
                    itemStateDateStartCredit();
                }
                else if (checkBox == jckRecordUser) {
                    itemStateRecordManual();
                }
                else if (checkBox == jckPayments) {
                    itemStatePayments();
                }
                else if (checkBox == jckShipments) {
                    itemStateShipments();
                }
                else if (checkBox == jckIsDiscountDocApplying) {
                    itemStateIsDiscountDocApplying(true);
                }
                else if (checkBox == jckIsDiscountDocPercentage) {
                    itemStateIsDiscountDocPercentage(true);
                }
            }
            else if (e.getSource() instanceof javax.swing.JComboBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JComboBox comboBox = (JComboBox) e.getSource();

                    if (comboBox == jcbNumberSeries) {
                        itemChangeNumberSeries();
                    }
                    else if (comboBox == jcbFkPaymentTypeId) {
                        itemChangeFkPaymentTypeId(true);
                    }
                    else if (comboBox == jcbFkPaymentSystemTypeId) {
                        itemChangeFkPaymentSystemTypeId();
                    }
                    else if (comboBox == jcbFkLanguageId) {
                        itemChangeFkLanguageId();
                    }
                    else if (comboBox == jcbFkCurrencyId) {
                        itemChangeFkCurrencyId(true);
                    }
                    else if (comboBox == jcbFkIncotermId) {
                        itemChangeFkIncotermId();
                    }
                    else if (comboBox == jcbFkCarrierTypeId) {
                        itemChangeFkCarrierTypeId();
                    }
                    else if (comboBox == jcbFkVehicleTypeId_n) {
                        itemChangeFkVehicleTypeId_n();
                    }
                    else if (comboBox == jcbCfdAddendaSubtypeId) {
                        itemChangeCfdAddendaSubtypeId();
                    }
                }
            }
        }
    }

    @Override
    public SLibMethod getPostSaveMethod(SDataRegistry registry) {
        int mmsType = SLibConstants.UNDEFINED;
        boolean sendMail = false;
        SLibMethod method = null;
        SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, registry.getPrimaryKey(), SLibConstants.EXEC_MODE_STEALTH);
        
        sendMail = (SLibUtilities.compareKeys(SDataConstantsSys.TRNU_TP_DPS_SAL_CON, dps.getDpsTypeKey()) ||
                        SLibUtilities.compareKeys(SDataConstantsSys.TRNU_TP_DPS_SAL_ORD, dps.getDpsTypeKey()));
        
        if (SLibUtilities.compareKeys(SDataConstantsSys.TRNU_TP_DPS_SAL_CON, dps.getDpsTypeKey())) {
            mmsType = SModSysConsts.CFGS_TP_MMS_CON;
        }
        else if (SLibUtilities.compareKeys(SDataConstantsSys.TRNU_TP_DPS_SAL_ORD, dps.getDpsTypeKey())) {
            mmsType = SModSysConsts.CFGS_TP_MMS_ORD_SAL;
        }
        
        if (sendMail) {
            
            if (mmsType != SLibConstants.UNDEFINED) {
                try {                    
                    method = new SLibMethod(dps, dps.getClass().getMethod("sendMail", new Class[] { SClientInterface.class, Object.class, int.class }), new Object[] { miClient, dps.getPrimaryKey(), mmsType });
                } 
                catch (NoSuchMethodException | SecurityException e) {
                    SLibUtilities.printOutException(this, e);
                }
            }
            else {
                SLibUtilities.printOutException(this, new UnsupportedOperationException("Not supported yet."));
            }
        }
        
        return method;
    }
}
