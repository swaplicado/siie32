/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.form;

import cfd.DCfdConsts;
import cfd.DCfdUtils;
import cfd.ver33.DCfdi33Catalogs;
import cfd.ver33.DCfdi33Consts;
import cfd.ver40.DCfdi40Catalogs;
import cfd.ver40.DCfdi40Consts;
import erp.cfd.SCfdConsts;
import erp.cfd.SCfdXmlCatalogs;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.form.SFormOptionPickerBizPartner;
import erp.gui.SGuiUtilities;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import erp.lib.form.SFormComboBoxGroup;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormOptionPickerInterface;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.gui.SGuiDate;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePaneGrid;
import erp.lib.table.STableRow;
import erp.lib.table.STableUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerAddressee;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mbps.data.SDataBizPartnerBranchContact;
import erp.mcfg.data.SDataParamsCompany;
import erp.mcfg.data.SDataParamsErp;
import erp.mfin.data.SDataRecord;
import erp.mfin.form.SDialogRecordPicker;
import erp.mfin.form.SPanelAccount;
import erp.mfin.form.SPanelRecord;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataItemConfigBizPartner;
import erp.mitm.data.SDataUnit;
import erp.mmkt.data.SDataCustomerBranchConfig;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.log.db.SDbBillOfLading;
import erp.mod.trn.db.STrnUtils;
import erp.mtrn.data.SCfdParams;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SCfdUtilsHandler;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsCfd;
import erp.mtrn.data.SDataDpsCustomAccEntry;
import erp.mtrn.data.SDataDpsCustomAccEntryRow;
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
import erp.mtrn.data.SDataMinorChangesDps;
import erp.mtrn.data.SDataPdf;
import erp.mtrn.data.SDataScaleTicketDps;
import erp.mtrn.data.SDataScaleTicketDpsEntry;
import erp.mtrn.data.SDataSystemNotes;
import erp.mtrn.data.SGuiDpsEntryPrice;
import erp.mtrn.data.SGuiDpsLink;
import erp.mtrn.data.SRowCfdRelatedDocs;
import erp.mtrn.data.STrnCfdRelatedDocs;
import erp.mtrn.data.STrnDpsUtilities;
import erp.mtrn.data.STrnUtilities;
import erp.mtrn.data.cfd.SAddendaAmc71Company;
import erp.mtrn.data.cfd.SAddendaAmc71CompanyBranch;
import erp.mtrn.data.cfd.SAddendaAmc71Manager;
import erp.mtrn.data.cfd.SAddendaAmc71Supplier;
import erp.mtrn.data.cfd.SAddendaAmc71XmlHeader;
import erp.redis.SLockUtils;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibMethod;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.srv.SLock;
import sa.lib.srv.SSrvConsts;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author Sergio Flores, Edwin Carmona, Uriel Castañeda, Juan Barajas, Isabel Servín, Adrián Avilés, Claudio Peña, Sergio Flores
 */
public class SFormDps extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.FocusListener, java.awt.event.ItemListener, javax.swing.event.ChangeListener, javax.swing.event.ListSelectionListener, erp.lib.form.SFormExtendedInterface {
    
    private static final int TAB_ETY = 0; // entries
    private static final int TAB_MKT = 1; // marketing
    private static final int TAB_NTS = 2; // notes
    private static final int TAB_CFD_INT_COM = 3; // CFD International Commerce
    private static final int TAB_CFD_ADD = 4; // CFD Addenda
    private static final int TAB_CFD_XML = 5; // CFD XML file
    private static final int TAB_ACC = 6; // costomized accounting
    private static final int ACTION_NEW = 1; // acción de captura: nuevo
    private static final int ACTION_EDIT = 2; // acción de captura: modificación
    private static final int DECS_PCT = 4; // 0.01% is 0.0001
    
    private static final int UUID_FIRST_SECC_LENGHT = 8;

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private boolean mbUpdatingForm;
    private boolean mbDocBeingImported;
    private boolean mbMatRequestImport;
    private java.util.Vector<SFormField> mvFields;
    private erp.client.SClientInterface miClient;
    
    private Vector<SDataScaleTicketDps> mvScaleTicDps;

    private erp.mtrn.data.SDataDps moDps;
    private erp.mtrn.data.SDataDps moLastDpsSource;
    private erp.mtrn.data.SDataDpsType moDpsType;
    private erp.mtrn.data.SGuiDpsLink moGuiDpsLink;
    private erp.mbps.data.SDataBizPartner moBizPartner;
    private erp.mbps.data.SDataBizPartnerBranch moBizPartnerBranch;
    private erp.mbps.data.SDataBizPartnerBranchAddress moBizPartnerBranchAddressMain;
    private erp.mbps.data.SDataBizPartnerBranchAddress moBizPartnerBranchAddress;
    private erp.mbps.data.SDataBizPartnerCategory moBizPartnerCategory;
    private erp.mod.log.db.SDbBillOfLading moBillOfLading;
    private java.util.ArrayList<SDataSystemNotes> maSystemNotes;
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
    private erp.lib.form.SFormField moFieldIsLinked;
    private erp.lib.form.SFormField moFieldIsClosed;
    private erp.lib.form.SFormField moFieldIsAudited;
    private erp.lib.form.SFormField moFieldIsAuthorized;
    private erp.lib.form.SFormField moFieldIsCopy;
    private erp.lib.form.SFormField moFieldIsSystem;
    private erp.lib.form.SFormField moFieldIsDeleted;
    private erp.lib.form.SFormField moFieldFkPaymentTypeId;
    private erp.lib.form.SFormField moFieldFkLanguajeId;
    private erp.lib.form.SFormField moFieldFkDpsNatureId;
    private erp.lib.form.SFormField moFieldFkFunctionalAreaId;
    private erp.lib.form.SFormField moFieldFkCurrencyId;
    private erp.lib.form.SFormField moFieldIsDiscountDocApplying;
    private erp.lib.form.SFormField moFieldIsDiscountDocPercentage;
    private erp.lib.form.SFormField moFieldDiscountDocPercentage;
    private erp.lib.form.SFormField moFieldExchangeRate;
    private erp.lib.form.SFormField moFieldExchangeRateSystem;
    private erp.lib.form.SFormField moFieldShipments;
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
    private erp.lib.form.SFormField moFieldReqNum;
    private erp.lib.form.SFormField moFieldDriver;
    private erp.lib.form.SFormField moFieldPlate;
    private erp.lib.form.SFormField moFieldTicket;
    private erp.lib.form.SFormField moFieldAddCfdAddendaSubtype;
    private erp.lib.form.SFormField moFieldAddLorealFolioNotaRecepción;
    private erp.lib.form.SFormField moFieldAddBachocoSociedad;
    private erp.lib.form.SFormField moFieldAddBachocoOrganizaciónCompra;
    private erp.lib.form.SFormField moFieldAddBachocoDivisión;
    private erp.lib.form.SFormField moFieldAddModeloDpsDescripción;
    private erp.lib.form.SFormField moFieldAddSorianaTienda;
    private erp.lib.form.SFormField moFieldAddSorianaEntregaMercancía;
    private erp.lib.form.SFormField moFieldAddSorianaRemisiónFecha;
    private erp.lib.form.SFormField moFieldAddSorianaRemisiónFolio;
    private erp.lib.form.SFormField moFieldAddSorianaPedidoFolio;
    private erp.lib.form.SFormField moFieldAddSorianaBultoTipo;
    private erp.lib.form.SFormField moFieldAddSorianaBultoCantidad;
    private erp.lib.form.SFormField moFieldAddSorianaNotaEntradaFolio;
    private erp.lib.form.SFormField moFieldAddSorianaCita;
    private erp.lib.form.SFormField moFieldAddAmc71SupplierGln;
    private erp.lib.form.SFormField moFieldAddAmc71SupplierNumber;
    private erp.lib.form.SFormField moFieldAddAmc71CompanyGln;
    private erp.lib.form.SFormField moFieldAddAmc71CompanyContact;
    private erp.lib.form.SFormField moFieldAddAmc71CompanyBranchGln;
    private erp.lib.form.SFormField moFieldAddAmc71ShipToName;
    private erp.lib.form.SFormField moFieldAddAmc71ShipToAddress;
    private erp.lib.form.SFormField moFieldAddAmc71ShipToCity;
    private erp.lib.form.SFormField moFieldAddAmc71ShipToPostalCode;
    private erp.lib.form.SFormField moFieldCfdiXmlFile;
    private erp.lib.form.SFormField moFieldCfdiPdfFile;
    private erp.lib.form.SFormField moFieldCfdiPaymentWay;
    private erp.lib.form.SFormField moFieldCfdiPaymentMethod;
    private erp.lib.form.SFormField moFieldConditionsPayment;
    private erp.lib.form.SFormField moFieldCfdiTaxRegimeIss;
    private erp.lib.form.SFormField moFieldCfdiTaxRegimeRec;
    private erp.lib.form.SFormField moFieldCfdiCfdiUsage;
    private erp.lib.form.SFormField moFieldCfdiConfirmation;
    private erp.lib.form.SFormField moFieldCfdiBillOfLading;
    private erp.lib.form.SFormField moFieldCfdiGblPeriodicity;
    private erp.lib.form.SFormField moFieldCfdiGblMonth;
    private erp.lib.form.SFormField moFieldCfdiGblYear;
    private erp.lib.form.SFormField moFieldCfdCceApplies;
    private erp.lib.form.SFormField moFieldCfdCceMoveReason;
    private erp.lib.form.SFormField moFieldCfdCceOperationType;
    private erp.lib.form.SFormField moFieldCfdCceRequestKey;
    private erp.lib.form.SFormField moFieldCfdCceExportation;
    private erp.lib.form.SFormField moFieldCfdCceCertificateOrigin;
    private erp.lib.form.SFormField moFieldCfdCceNumberCertificateOrigin;
    private erp.lib.form.SFormField moFieldCfdCceSubdivision;
    private erp.lib.form.SFormField moFieldCfdCceExchangeRateUsd;
    private erp.lib.form.SFormField moFieldCfdCceTotalUsd;
    private erp.lib.form.SFormField moFieldCfdCceBizPartnerAddressee;
    private erp.lib.form.SFormField moFieldCfdCceAddresseeBizPartner;
    private erp.lib.form.SFormField moFieldCfdCceAddresseeBizPartnerBranch;
    private erp.lib.form.SFormField moFieldCfdCceAddresseeBizPartnerBranchAddress;
    private erp.lib.form.SFormField moFieldAccItem;
    private erp.lib.form.SFormField moFieldAccItemRef_n;
    private erp.lib.form.SFormField moFieldAccQuantity;
    private erp.lib.form.SFormField moFieldAccUnit;
    private erp.lib.form.SFormField moFieldAccIsSubtotalPctApplying;
    private erp.lib.form.SFormField moFieldAccSubtotalPct;
    private erp.lib.form.SFormField moFieldAccSubtotalCy;
    private erp.lib.form.SFormField moFieldAccConcept;
    private erp.lib.form.SFormComboBoxGroup moComboBoxGroupCfdCceGroupAddressee;
    
    private erp.mtrn.data.SDataDps moParamDpsSource;
    private int mnParamCurrentUserPrivilegeLevel;
    private boolean mbParamIsReadOnly;

    private boolean mbIsDpsContract;
    private boolean mbIsDpsEstimate;
    private boolean mbIsDpsOrder;
    private boolean mbIsDpsInvoice;
    private boolean mbIsDpsAdjustment;
    private boolean mbIsSales;
    private boolean mbIsNumberSeriesRequired;
    private boolean mbIsNumberSeriesAvailable;
    private boolean mbHasRightOrderDelay;
    private boolean mbHasRightOmitSourceDoc;
    private int mnNumbersApprovalYear;      // CFD approval year
    private int mnNumbersApprovalNumber;    // CFD approval number
    private int[] manDpsClassKey;
    private int[] manDpsClassPreviousKey;
    private int[] manParamAdjustmentSubtypeKey;
    private int[] manDpsTime;
    private boolean mbIsPeriodOpen;
    private boolean mbIsLocalCurrency;
    private boolean mbIsImported;
    private boolean mbIsNumberEditable;
    private boolean mbPostEmissionEdition;
    private boolean mbFormSettingsOk;
    private boolean mbValidateLinkPeriod;
    private boolean mbHasDpsLinksAsSrc;
    private boolean mbHasDpsLinksAsDes;
    private boolean mbHasDpsAdjustmentsAsDoc;
    private boolean mbHasDpsAdjustmentsAsAdj;
    private boolean mbHasIogSupplies;
    private boolean mbHasCommissions;
    private boolean mbHasShipments;
    private boolean mbIsDpsTimeReq;
    private int mnSalesAgentId_n;
    private int mnSalesAgentBizPartnerId_n;
    private int mnSalesSupervisorId_n;
    private int mnSalesSupervisorBizPartnerId_n;
    private int mnDeliveryType;
    private int mnCfdXmlType; // current XML type for CFD type invoice
    private double mdPrepayments;
    private double mdPrepaymentsCy;
    private int mnOldFunctionalAreaId;
    private double mdOldExchangeRate;
    private double mdOldDiscountDocPercentage;
    private boolean mbOldIsDiscountDocApplying;
    private java.lang.String msFileXmlJustLoaded;
    private File moFilePdfJustLoaded;
    private erp.mtrn.data.cfd.SAddendaAmc71Manager moAddendaAmc71Manager;
    private java.lang.Object moRecordUserKey;
    /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
    private sa.lib.srv.SSrvLock moRecordUserLock;
    */
    /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
    private sa.lib.srv.redis.SRedisLock moRecordUserRedisLock;
    */
    private sa.lib.srv.SLock moRecordUserSLock;
    private erp.mfin.data.SDataRecord moRecordUser;
    private erp.form.SFormOptionPickerBizPartner moPickerBizPartner;
    private erp.lib.table.STablePaneGrid moPaneGridEntries;
    private erp.lib.table.STablePaneGrid moPaneGridNotes;
    private erp.lib.table.STablePaneGrid moPaneGridCustomAcc;
    private erp.mtrn.form.SFormDpsEntry moFormEntry;
    private erp.mtrn.form.SFormDpsEntryWizard moFormEntryWizard;
    private erp.mtrn.form.SFormDpsNotes moFormNotes;
    private erp.mtrn.form.SFormDpsCom moFormCom;
    private erp.mtrn.form.SDialogPickerDps moDialogPickerDps;
    private erp.mtrn.form.SDialogPickerDps moDialogPickerDpsForLink;
    private erp.mtrn.form.SDialogPickerDps moDialogPickerDpsForAdjustment;
    private erp.mtrn.form.SDialogPickerDps moDialogPickerBillOfLading;
    private erp.mtrn.form.SDialogDpsLink moDialogDpsLink;
    private erp.mtrn.form.SDialogDpsAdjustment moDialogDpsAdjustment;
    private erp.mtrn.form.SDialogCfdRelatedDocs moDialogCfdRelatedDocs;
    private erp.mtrn.data.STrnCfdRelatedDocs moCfdRelatedDocs;
    private erp.mfin.form.SDialogRecordPicker moDialogRecordPicker;
    private erp.mtrn.form.SDialogShowDocumentLinks moDialogShowDocumentLinks;
    private erp.mtrn.form.SDialogDpsTime moDialogDpsTime;
    private erp.mfin.form.SPanelRecord moPanelRecord;
    private cfd.ver33.DElementComprobante moComprobante33;
    private cfd.ver40.DElementComprobante moComprobante40;
    private java.lang.String msXmlUuid;
    private erp.mitm.data.SDataItem moAccItem;
    private erp.mfin.form.SPanelAccount moPanelFkCostCenterId_n;
    private int mnAccCurrentAction;
    private double mdAccSubtotal;
    private double mdAccSubtotalCy;
    private double mdAccSubtotalPct;
    private boolean mbCustomAccPrepared;

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
        jlFiscalId = new javax.swing.JLabel();
        jtfFiscalId = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jlBizPartnerBranch = new javax.swing.JLabel();
        jtfBizPartnerBranchRo = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jlBizPartnerBranchAddressMain01 = new javax.swing.JLabel();
        jtfBizPartnerBranchAddressMain01Ro = new javax.swing.JTextField();
        jPanel65 = new javax.swing.JPanel();
        jlBizPartnerBranchAddressMain02 = new javax.swing.JLabel();
        jtfBizPartnerBranchAddressMain02Ro = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jlBizPartnerBranchAddress01 = new javax.swing.JLabel();
        jtfBizPartnerBranchAddress01Ro = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        jftDate = new javax.swing.JFormattedTextField();
        jbDate = new javax.swing.JButton();
        jbSetTime = new javax.swing.JButton();
        jckIsRebill = new javax.swing.JCheckBox();
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
        jbDateMaturity = new javax.swing.JButton();
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
        jPanel67 = new javax.swing.JPanel();
        jckIsCopy = new javax.swing.JCheckBox();
        jbBizPartnerBalance = new javax.swing.JButton();
        jlRequisicion = new javax.swing.JLabel();
        jtfReqNum = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jlFkPaymentTypeId = new javax.swing.JLabel();
        jcbFkPaymentTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel35 = new javax.swing.JPanel();
        jlCfdiPaymentWay = new javax.swing.JLabel();
        jcbCfdiPaymentWay = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel32 = new javax.swing.JPanel();
        jlCfdiPaymentMethod = new javax.swing.JLabel();
        jcbCfdiPaymentMethod = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel25 = new javax.swing.JPanel();
        jlConditionsPayment = new javax.swing.JLabel();
        jtfConditionsPayment = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        jlFkDpsStatus = new javax.swing.JLabel();
        jtfFkDpsStatusRo = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jlFkDpsStatusValidity = new javax.swing.JLabel();
        jtfFkDpsStatusValidityRo = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jlFkDpsStatusAuthorization = new javax.swing.JLabel();
        jtfFkDpsStatusAuthorizationRo = new javax.swing.JTextField();
        jPanel26 = new javax.swing.JPanel();
        jlFkLanguageId = new javax.swing.JLabel();
        jcbFkLanguageId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel33 = new javax.swing.JPanel();
        jlFkDpsNatureId = new javax.swing.JLabel();
        jcbFkDpsNatureId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel92 = new javax.swing.JPanel();
        jlFkFunctionalAreaId = new javax.swing.JLabel();
        jcbFkFunctionalAreaId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel30 = new javax.swing.JPanel();
        jckIsAudited = new javax.swing.JCheckBox();
        jckIsSystem = new javax.swing.JCheckBox();
        jPanel31 = new javax.swing.JPanel();
        jckIsAuthorized = new javax.swing.JCheckBox();
        jckIsDeleted = new javax.swing.JCheckBox();
        jPanel13 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jckRecordUser = new javax.swing.JCheckBox();
        jtbSwitchCustomAcc = new javax.swing.JToggleButton();
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
        jPanel37 = new javax.swing.JPanel();
        jckIsDiscountDocPercentage = new javax.swing.JCheckBox();
        jtfDiscountDocPercentage = new javax.swing.JTextField();
        jPanel58 = new javax.swing.JPanel();
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
        jbEntryCopy = new javax.swing.JButton();
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
        jsEntry4 = new javax.swing.JSeparator();
        jbEntryImportFromMatRequest = new javax.swing.JButton();
        jbEntryViewMatReqLinks = new javax.swing.JButton();
        jsEntry5 = new javax.swing.JSeparator();
        jbExportCsv = new javax.swing.JButton();
        jpEntriesControlsEast = new javax.swing.JPanel();
        jlAdjustmentSubtypeId = new javax.swing.JLabel();
        jcbAdjustmentSubtypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jlTaxRegionId = new javax.swing.JLabel();
        jcbTaxRegionId = new javax.swing.JComboBox<SFormComponentItem>();
        jbTaxRegionId = new javax.swing.JButton();
        jbEditTaxRegion = new javax.swing.JButton();
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
        jPanel80 = new javax.swing.JPanel();
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
        jtbLinkTicket = new javax.swing.JToggleButton();
        jbEditLogistics = new javax.swing.JButton();
        jpNotes = new javax.swing.JPanel();
        jpNotesControls = new javax.swing.JPanel();
        jbNotesNew = new javax.swing.JButton();
        jbNotesEdit = new javax.swing.JButton();
        jbNotesDelete = new javax.swing.JButton();
        jsNotes01 = new javax.swing.JSeparator();
        jtbNotesFilter = new javax.swing.JToggleButton();
        jbPickSystemNotes = new javax.swing.JButton();
        jsNotes02 = new javax.swing.JSeparator();
        jbEditNotes = new javax.swing.JButton();
        jpCfdInternationalTrade = new javax.swing.JPanel();
        jPanel109 = new javax.swing.JPanel();
        jPanel97 = new javax.swing.JPanel();
        jPanel111 = new javax.swing.JPanel();
        jckCfdCceApplies = new javax.swing.JCheckBox();
        jPanel98 = new javax.swing.JPanel();
        jlCfdCceMoveReason = new javax.swing.JLabel();
        jcbCfdCceMoveReason = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel99 = new javax.swing.JPanel();
        jlCfdCceOperationType = new javax.swing.JLabel();
        jcbCfdCceOperationType = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel100 = new javax.swing.JPanel();
        jlCfdCceRequestKey = new javax.swing.JLabel();
        jcbCfdCceRequestKey = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel101 = new javax.swing.JPanel();
        jlCfdCceCertificateOrigin = new javax.swing.JLabel();
        jtfCfdCceCertificateOrigin = new javax.swing.JTextField();
        jPanel102 = new javax.swing.JPanel();
        jlCfdCceNumberCertificateOrigin = new javax.swing.JLabel();
        jtfCfdCceNumberCertificateOrigin = new javax.swing.JTextField();
        jPanel108 = new javax.swing.JPanel();
        jlCfdCceSubdivision = new javax.swing.JLabel();
        jtfCfdCceSubdivision = new javax.swing.JTextField();
        jPanel116 = new javax.swing.JPanel();
        jPanel117 = new javax.swing.JPanel();
        jlExportation = new javax.swing.JLabel();
        jcbExportation = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel110 = new javax.swing.JPanel();
        jPanel96 = new javax.swing.JPanel();
        jPanel103 = new javax.swing.JPanel();
        jlCfdCceExchangeRateUsd = new javax.swing.JLabel();
        jtfCfdCceExchangeRateUsd = new javax.swing.JTextField();
        jPanel104 = new javax.swing.JPanel();
        jlCfdCceTotalUsd = new javax.swing.JLabel();
        jtfCfdCceTotalUsd = new javax.swing.JTextField();
        jbCfdCceCalcTotalUsd = new javax.swing.JButton();
        jPanel106 = new javax.swing.JPanel();
        jPanel121 = new javax.swing.JPanel();
        jlCfdCceFkBizPartnerAddressee = new javax.swing.JLabel();
        jcbCfdCceFkBizPartnerAddressee = new javax.swing.JComboBox();
        jPanel122 = new javax.swing.JPanel();
        jlCfdCceAddresseeBizPartner = new javax.swing.JLabel();
        jPanel107 = new javax.swing.JPanel();
        jlCfdCceFkAddresseeBizPartner = new javax.swing.JLabel();
        jcbCfdCceFkAddresseeBizPartner = new javax.swing.JComboBox();
        jPanel112 = new javax.swing.JPanel();
        jlCfdCceFkAddresseeBizPartnerBranch = new javax.swing.JLabel();
        jcbCfdCceFkAddresseeBizPartnerBranch = new javax.swing.JComboBox();
        jPanel113 = new javax.swing.JPanel();
        jlCfdCceFkAddresseeBizPartnerBranchAddress = new javax.swing.JLabel();
        jcbCfdCceFkAddresseeBizPartnerBranchAddress = new javax.swing.JComboBox();
        jpCfdAddenda = new javax.swing.JPanel();
        jPanel91 = new javax.swing.JPanel();
        jPanel118 = new javax.swing.JPanel();
        jPanel68 = new javax.swing.JPanel();
        jlAddCfdAddendaType = new javax.swing.JLabel();
        jtfAddCfdAddendaType = new javax.swing.JTextField();
        jPanel41 = new javax.swing.JPanel();
        jlAddCfdAddendaSubtype = new javax.swing.JLabel();
        jcbAddCfdAddendaSubtype = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel69 = new javax.swing.JPanel();
        jlAddLorealFolioNotaRecepción = new javax.swing.JLabel();
        jtfAddLorealFolioNotaRecepción = new javax.swing.JTextField();
        jPanel70 = new javax.swing.JPanel();
        jlAddBachocoSociedad = new javax.swing.JLabel();
        jtfAddBachocoSociedad = new javax.swing.JTextField();
        jPanel71 = new javax.swing.JPanel();
        jlAddBachocoOrganizaciónCompra = new javax.swing.JLabel();
        jtfAddBachocoOrganizaciónCompra = new javax.swing.JTextField();
        jPanel72 = new javax.swing.JPanel();
        jlAddBachocoDivisión = new javax.swing.JLabel();
        jtfAddBachocoDivisión = new javax.swing.JTextField();
        jPanel81 = new javax.swing.JPanel();
        jlAddModeloDpsDescripción = new javax.swing.JLabel();
        jtfAddModeloDpsDescripción = new javax.swing.JTextField();
        jPanel119 = new javax.swing.JPanel();
        jPanel82 = new javax.swing.JPanel();
        jlAddSorianaTienda = new javax.swing.JLabel();
        jtfAddSorianaTienda = new javax.swing.JTextField();
        jPanel87 = new javax.swing.JPanel();
        jlAddSorianaEntregaMercancía = new javax.swing.JLabel();
        jtfAddSorianaEntregaMercancía = new javax.swing.JTextField();
        jPanel77 = new javax.swing.JPanel();
        jlAddSorianaRemisiónFecha = new javax.swing.JLabel();
        jftAddSorianaRemisiónFecha = new javax.swing.JFormattedTextField();
        jlAddSorianaRemisiónFechaHint = new javax.swing.JLabel();
        jPanel79 = new javax.swing.JPanel();
        jlAddSorianaRemisiónFolio = new javax.swing.JLabel();
        jtfAddSorianaRemisiónFolio = new javax.swing.JTextField();
        jPanel88 = new javax.swing.JPanel();
        jlAddSorianaPedidoFolio = new javax.swing.JLabel();
        jtfAddSorianaPedidoFolio = new javax.swing.JTextField();
        jPanel83 = new javax.swing.JPanel();
        jlAddSorianaBultoTipo = new javax.swing.JLabel();
        jtfAddSorianaBultoTipo = new javax.swing.JTextField();
        jPanel78 = new javax.swing.JPanel();
        jlAddSorianaBultoCantidad = new javax.swing.JLabel();
        jtfAddSorianaBultoCantidad = new javax.swing.JTextField();
        jPanel90 = new javax.swing.JPanel();
        jlAddSorianaNotaEntradaFolio = new javax.swing.JLabel();
        jtfAddSorianaNotaEntradaFolio = new javax.swing.JTextField();
        jPanel120 = new javax.swing.JPanel();
        jlAddSorianaCita = new javax.swing.JLabel();
        jtfAddSorianaCita = new javax.swing.JTextField();
        jPanel76 = new javax.swing.JPanel();
        jPanel123 = new javax.swing.JPanel();
        jlAddAmc71SupplierGln = new javax.swing.JLabel();
        jcbAddAmc71SupplierGln = new javax.swing.JComboBox<String>();
        jlAddAmc71SupplierGlnHint = new javax.swing.JLabel();
        jPanel124 = new javax.swing.JPanel();
        jlAddAmc71SupplierNumber = new javax.swing.JLabel();
        jtfAddAmc71SupplierNumber = new javax.swing.JTextField();
        jlAddAmc71SupplierNumberHint = new javax.swing.JLabel();
        jPanel125 = new javax.swing.JPanel();
        jlAddAmc71CompanyGln = new javax.swing.JLabel();
        jcbAddAmc71CompanyGln = new javax.swing.JComboBox<String>();
        jlAddAmc71CompanyGlnHint = new javax.swing.JLabel();
        jPanel131 = new javax.swing.JPanel();
        jlAddAmc71CompanyContact = new javax.swing.JLabel();
        jtfAddAmc71CompanyContact = new javax.swing.JTextField();
        jlAddAmc71CompanyContactHint = new javax.swing.JLabel();
        jPanel126 = new javax.swing.JPanel();
        jlAddAmc71CompanyBranchGln = new javax.swing.JLabel();
        jcbAddAmc71CompanyBranchGln = new javax.swing.JComboBox<String>();
        jlAddAmc71CompanyBranchGlnHint = new javax.swing.JLabel();
        jPanel130 = new javax.swing.JPanel();
        jlAddAmc71ShipToName = new javax.swing.JLabel();
        jtfAddAmc71ShipToName = new javax.swing.JTextField();
        jPanel127 = new javax.swing.JPanel();
        jlAddAmc71ShipToAddress = new javax.swing.JLabel();
        jtfAddAmc71ShipToAddress = new javax.swing.JTextField();
        jPanel128 = new javax.swing.JPanel();
        jlAddAmc71ShipToCity = new javax.swing.JLabel();
        jtfAddAmc71ShipToCity = new javax.swing.JTextField();
        jPanel129 = new javax.swing.JPanel();
        jlAddAmc71ShipToPostalCode = new javax.swing.JLabel();
        jtfAddAmc71ShipToPostalCode = new javax.swing.JTextField();
        jpCfdXml = new javax.swing.JPanel();
        jPanel73 = new javax.swing.JPanel();
        jPanel74 = new javax.swing.JPanel();
        jPanel75 = new javax.swing.JPanel();
        jlFileXml = new javax.swing.JLabel();
        jtfFileXml = new javax.swing.JTextField();
        jbLoadFileXml = new javax.swing.JButton();
        jbDeleteFileXml = new javax.swing.JButton();
        jlSpace = new javax.swing.JLabel();
        jckValidateOnSaveFileXml = new javax.swing.JCheckBox();
        jPanel105 = new javax.swing.JPanel();
        jlFilePdf = new javax.swing.JLabel();
        jtfFilePdf = new javax.swing.JTextField();
        jbLoadFilePdf = new javax.swing.JButton();
        jbDeleteFilePdf = new javax.swing.JButton();
        jlSpace1 = new javax.swing.JLabel();
        jlBillOfLading = new javax.swing.JLabel();
        jtfBillOfLading = new javax.swing.JTextField();
        jbLoadBillOfLading = new javax.swing.JButton();
        jbDeleteBillOfLading = new javax.swing.JButton();
        jPanel95 = new javax.swing.JPanel();
        jlCfdiTaxRegimeIssuing = new javax.swing.JLabel();
        jcbCfdiTaxRegimeIssuing = new javax.swing.JComboBox<SFormComponentItem>();
        jLabel1 = new javax.swing.JLabel();
        jlGlobalInf = new javax.swing.JLabel();
        jPanel93 = new javax.swing.JPanel();
        jlCfdiTaxRegimeReceptor = new javax.swing.JLabel();
        jcbCfdiTaxRegimeReceptor = new javax.swing.JComboBox<SFormComponentItem>();
        jLabel2 = new javax.swing.JLabel();
        jlGblPeriodicity = new javax.swing.JLabel();
        jcbGblPeriodicity = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel94 = new javax.swing.JPanel();
        jlCfdiCfdiUsage = new javax.swing.JLabel();
        jcbCfdiCfdiUsage = new javax.swing.JComboBox<SFormComponentItem>();
        jLabel3 = new javax.swing.JLabel();
        jlGblMonth = new javax.swing.JLabel();
        jcbGblMonth = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel114 = new javax.swing.JPanel();
        jlCfdiConfirmation = new javax.swing.JLabel();
        jtfCfdiConfirmation = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jlGblYear = new javax.swing.JLabel();
        jtfGblYear = new javax.swing.JTextField();
        jPanel115 = new javax.swing.JPanel();
        jbCfdiRelatedDocs = new javax.swing.JButton();
        jtfCfdiFirstRelatedDps = new javax.swing.JTextField();
        jspCfdiRelatedDocs = new javax.swing.JScrollPane();
        jtaCfdiRelatedDocs = new javax.swing.JTextArea();
        jpCustomAcc = new javax.swing.JPanel();
        jpCustomAcc1 = new javax.swing.JPanel();
        jpCustomAcc11 = new javax.swing.JPanel();
        jpCustomAcc111 = new javax.swing.JPanel();
        jpCustomAcc1111 = new javax.swing.JPanel();
        jlAccItem = new javax.swing.JLabel();
        jcbAccItem = new javax.swing.JComboBox();
        jbPickAccItem = new javax.swing.JButton();
        jpCustomAcc1112 = new javax.swing.JPanel();
        jlAccItemRef_n = new javax.swing.JLabel();
        jcbAccItemRef_n = new javax.swing.JComboBox();
        jbPickAccItemRef_n = new javax.swing.JButton();
        jpCustomAcc112 = new javax.swing.JPanel();
        jlAccDummyCostCenter = new javax.swing.JLabel();
        jpCustomAcc12 = new javax.swing.JPanel();
        jpCustomAcc121 = new javax.swing.JPanel();
        jlAccQuantity = new javax.swing.JLabel();
        jtfAccQuantity = new javax.swing.JTextField();
        jlAccUnit = new javax.swing.JLabel();
        jcbAccUnit = new javax.swing.JComboBox();
        jbPickAccUnit = new javax.swing.JButton();
        jckAccIsSubtotalPctApplying = new javax.swing.JCheckBox();
        jtfAccSubtotalPct = new javax.swing.JTextField();
        jtfAccSubtotal = new javax.swing.JTextField();
        jtfAccSubtotalCur = new javax.swing.JTextField();
        jtfAccSubtotalCy = new javax.swing.JTextField();
        jtfAccSubtotalCyCur = new javax.swing.JTextField();
        jbExeWizardAccSubtotal = new javax.swing.JButton();
        jpCustomAcc122 = new javax.swing.JPanel();
        jPanel139 = new javax.swing.JPanel();
        jlAccConcept = new javax.swing.JLabel();
        jtfAccConcept = new javax.swing.JTextField();
        jPanel142 = new javax.swing.JPanel();
        jbCustomAccEntryNew = new javax.swing.JButton();
        jbCustomAccEntryCopy = new javax.swing.JButton();
        jbCustomAccEntryEdit = new javax.swing.JButton();
        jbCustomAccEntryDelete = new javax.swing.JButton();
        jbCustomAccEntryOk = new javax.swing.JButton();
        jbCustomAccEntryCancel = new javax.swing.JButton();
        jpCustomAcc2 = new javax.swing.JPanel();
        jlAccSubtotal = new javax.swing.JLabel();
        jtfAccSubtotalSubtotalPct = new javax.swing.JTextField();
        jtfAccSubtotalSubtotal = new javax.swing.JTextField();
        jtfAccSubtotalSubtotalCur = new javax.swing.JTextField();
        jtfAccSubtotalSubtotalCy = new javax.swing.JTextField();
        jtfAccSubtotalSubtotalCyCur = new javax.swing.JTextField();
        jpControls = new javax.swing.JPanel();
        jpControlsPk = new javax.swing.JPanel();
        jtfPkRo = new javax.swing.JTextField();
        jpControlsRecord = new javax.swing.JPanel();
        jpControlsButtons = new javax.swing.JPanel();
        jpControlsButtonsWest = new javax.swing.JPanel();
        jlQuantityTotal = new javax.swing.JLabel();
        jtfQuantityTotal = new javax.swing.JTextField();
        jpControlsButtonsCenter = new javax.swing.JPanel();
        jbEdit = new javax.swing.JButton();
        jbEditHelp = new javax.swing.JButton();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

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

        jPanel57.setLayout(new java.awt.GridLayout(14, 1, 0, 1));

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

        jlFiscalId.setText("RFC:");
        jlFiscalId.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel8.add(jlFiscalId);

        jtfFiscalId.setEditable(false);
        jtfFiscalId.setText("FISCAL ID");
        jtfFiscalId.setFocusable(false);
        jtfFiscalId.setPreferredSize(new java.awt.Dimension(270, 23));
        jPanel8.add(jtfFiscalId);

        jPanel57.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerBranch.setText("Sucursal asociado:");
        jlBizPartnerBranch.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel9.add(jlBizPartnerBranch);

        jtfBizPartnerBranchRo.setEditable(false);
        jtfBizPartnerBranchRo.setText("BRANCH");
        jtfBizPartnerBranchRo.setFocusable(false);
        jtfBizPartnerBranchRo.setPreferredSize(new java.awt.Dimension(270, 23));
        jPanel9.add(jtfBizPartnerBranchRo);

        jPanel57.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerBranchAddressMain01.setText("Domicilio sucursal:");
        jlBizPartnerBranchAddressMain01.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel10.add(jlBizPartnerBranchAddressMain01);

        jtfBizPartnerBranchAddressMain01Ro.setEditable(false);
        jtfBizPartnerBranchAddressMain01Ro.setText("ADDRESS 01");
        jtfBizPartnerBranchAddressMain01Ro.setFocusable(false);
        jtfBizPartnerBranchAddressMain01Ro.setPreferredSize(new java.awt.Dimension(270, 23));
        jPanel10.add(jtfBizPartnerBranchAddressMain01Ro);

        jPanel57.add(jPanel10);

        jPanel65.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerBranchAddressMain02.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel65.add(jlBizPartnerBranchAddressMain02);

        jtfBizPartnerBranchAddressMain02Ro.setEditable(false);
        jtfBizPartnerBranchAddressMain02Ro.setText("ADDRESS 02");
        jtfBizPartnerBranchAddressMain02Ro.setFocusable(false);
        jtfBizPartnerBranchAddressMain02Ro.setPreferredSize(new java.awt.Dimension(270, 23));
        jPanel65.add(jtfBizPartnerBranchAddressMain02Ro);

        jPanel57.add(jPanel65);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerBranchAddress01.setText("Domicilio operación:");
        jlBizPartnerBranchAddress01.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel17.add(jlBizPartnerBranchAddress01);

        jtfBizPartnerBranchAddress01Ro.setEditable(false);
        jtfBizPartnerBranchAddress01Ro.setText("ADDRESS 01");
        jtfBizPartnerBranchAddress01Ro.setFocusable(false);
        jtfBizPartnerBranchAddress01Ro.setPreferredSize(new java.awt.Dimension(270, 23));
        jPanel17.add(jtfBizPartnerBranchAddress01Ro);

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

        jbSetTime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_clock.gif"))); // NOI18N
        jbSetTime.setToolTipText("Seleccionar hora");
        jbSetTime.setFocusable(false);
        jbSetTime.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbSetTime);

        jckIsRebill.setText("Re-emisión");
        jckIsRebill.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel11.add(jckIsRebill);

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
        jtfDaysOfCredit.setPreferredSize(new java.awt.Dimension(47, 23));
        jPanel18.add(jtfDaysOfCredit);

        jbDateMaturity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateMaturity.setToolTipText("Seleccionar fecha de vencimiento");
        jbDateMaturity.setFocusable(false);
        jbDateMaturity.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel18.add(jbDateMaturity);

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

        jPanel67.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsCopy.setText("Es copia");
        jckIsCopy.setMargin(new java.awt.Insets(2, 0, 2, 2));
        jckIsCopy.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel67.add(jckIsCopy);

        jbBizPartnerBalance.setText("Saldo AN");
        jbBizPartnerBalance.setToolTipText("Ver saldo del asociado de negocios");
        jbBizPartnerBalance.setFocusable(false);
        jPanel67.add(jbBizPartnerBalance);

        jlRequisicion.setText("Requisición:");
        jlRequisicion.setPreferredSize(new java.awt.Dimension(98, 23));
        jPanel67.add(jlRequisicion);

        jtfReqNum.setText("00000000");
        jtfReqNum.setToolTipText("Folio");
        jtfReqNum.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel67.add(jtfReqNum);

        jPanel57.add(jPanel67);

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

        jPanel35.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdiPaymentWay.setText("Forma de pago: *");
        jlCfdiPaymentWay.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel35.add(jlCfdiPaymentWay);

        jcbCfdiPaymentWay.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel35.add(jcbCfdiPaymentWay);

        jPanel5.add(jPanel35);

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdiPaymentMethod.setText("Método de pago: *");
        jlCfdiPaymentMethod.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel32.add(jlCfdiPaymentMethod);

        jcbCfdiPaymentMethod.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel32.add(jcbCfdiPaymentMethod);

        jPanel5.add(jPanel32);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlConditionsPayment.setText("Condiciones pago: ");
        jlConditionsPayment.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel25.add(jlConditionsPayment);

        jtfConditionsPayment.setText("TEXTO");
        jtfConditionsPayment.setToolTipText("Condiciones pago");
        jtfConditionsPayment.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel25.add(jtfConditionsPayment);

        jPanel5.add(jPanel25);

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

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkLanguageId.setText("Idioma doc.: *");
        jlFkLanguageId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel26.add(jlFkLanguageId);

        jcbFkLanguageId.setEnabled(false);
        jcbFkLanguageId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel26.add(jcbFkLanguageId);

        jPanel5.add(jPanel26);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkDpsNatureId.setText("Naturaleza doc.: *");
        jlFkDpsNatureId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel33.add(jlFkDpsNatureId);

        jcbFkDpsNatureId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel33.add(jcbFkDpsNatureId);

        jPanel5.add(jPanel33);

        jPanel92.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkFunctionalAreaId.setText("Área funcional: *");
        jlFkFunctionalAreaId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel92.add(jlFkFunctionalAreaId);

        jcbFkFunctionalAreaId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel92.add(jcbFkFunctionalAreaId);

        jPanel5.add(jPanel92);

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsAudited.setText("Auditado");
        jckIsAudited.setEnabled(false);
        jckIsAudited.setMargin(new java.awt.Insets(2, 0, 2, 2));
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
        jckIsAuthorized.setMargin(new java.awt.Insets(2, 0, 2, 2));
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
        jckRecordUser.setMargin(new java.awt.Insets(2, 0, 2, 2));
        jckRecordUser.setPreferredSize(new java.awt.Dimension(213, 23));
        jPanel19.add(jckRecordUser);

        jtbSwitchCustomAcc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_custom_acc.gif"))); // NOI18N
        jtbSwitchCustomAcc.setToolTipText("Personalizar contabilización");
        jtbSwitchCustomAcc.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_custom_acc_on_disabled.gif"))); // NOI18N
        jtbSwitchCustomAcc.setPreferredSize(new java.awt.Dimension(23, 23));
        jtbSwitchCustomAcc.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_custom_acc_on.gif"))); // NOI18N
        jPanel19.add(jtbSwitchCustomAcc);

        jPanel13.add(jPanel19);

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfRecordManualDateRo.setEditable(false);
        jtfRecordManualDateRo.setText("01/01/2000");
        jtfRecordManualDateRo.setToolTipText("Fecha de la póliza contable");
        jtfRecordManualDateRo.setFocusable(false);
        jtfRecordManualDateRo.setPreferredSize(new java.awt.Dimension(75, 23));
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

        jpCurrency.setLayout(new java.awt.GridLayout(7, 1, 0, 1));

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

        jlExchangeRate.setText("Tipo de cambio (TC): *");
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

        jckIsDiscountDocApplying.setText("Aplica descto. docto.");
        jckIsDiscountDocApplying.setMargin(new java.awt.Insets(2, 0, 2, 2));
        jckIsDiscountDocApplying.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel23.add(jckIsDiscountDocApplying);

        jpCurrency.add(jPanel23);

        jPanel37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsDiscountDocPercentage.setText("Descto. docto. en pct.:");
        jckIsDiscountDocPercentage.setMargin(new java.awt.Insets(2, 0, 2, 2));
        jckIsDiscountDocPercentage.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel37.add(jckIsDiscountDocPercentage);

        jtfDiscountDocPercentage.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDiscountDocPercentage.setText("0.00%");
        jtfDiscountDocPercentage.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel37.add(jtfDiscountDocPercentage);

        jpCurrency.add(jPanel37);

        jPanel58.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpCurrency.add(jPanel58);

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

        jlDiscountDoc.setText("Descto. docto. (-):");
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

        jpEntries.setLayout(new java.awt.BorderLayout(0, 2));

        jpEntriesControls.setLayout(new java.awt.BorderLayout());

        jpEntriesControlsWest.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 0));

        jbEntryNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbEntryNew.setToolTipText("Crear partida [Ctrl + N]");
        jbEntryNew.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEntriesControlsWest.add(jbEntryNew);

        jbEntryCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_copy.gif"))); // NOI18N
        jbEntryCopy.setToolTipText("Copiar partida");
        jbEntryCopy.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEntriesControlsWest.add(jbEntryCopy);

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
        jbEntryDiscountRetailChain.setToolTipText("Crear partida descuento independiente [Ctrl + R]");
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

        jsEntry4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jsEntry4.setPreferredSize(new java.awt.Dimension(3, 23));
        jpEntriesControlsWest.add(jsEntry4);

        jbEntryImportFromMatRequest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_doc_add_b.gif"))); // NOI18N
        jbEntryImportFromMatRequest.setToolTipText("Importar partidas de requisición de materiales");
        jbEntryImportFromMatRequest.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEntriesControlsWest.add(jbEntryImportFromMatRequest);

        jbEntryViewMatReqLinks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_link_b.gif"))); // NOI18N
        jbEntryViewMatReqLinks.setToolTipText("Ver vínculos de requisición de materiales de la partida");
        jbEntryViewMatReqLinks.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEntriesControlsWest.add(jbEntryViewMatReqLinks);

        jsEntry5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jsEntry5.setPreferredSize(new java.awt.Dimension(3, 23));
        jpEntriesControlsWest.add(jsEntry5);

        jbExportCsv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_file_csv.gif"))); // NOI18N
        jbExportCsv.setToolTipText("Exportar CSV [Ctrl + E]");
        jbExportCsv.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEntriesControlsWest.add(jbExportCsv);

        jpEntriesControls.add(jpEntriesControlsWest, java.awt.BorderLayout.WEST);

        jpEntriesControlsEast.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jlAdjustmentSubtypeId.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlAdjustmentSubtypeId.setText("Tipo ajuste: ");
        jlAdjustmentSubtypeId.setPreferredSize(new java.awt.Dimension(75, 23));
        jpEntriesControlsEast.add(jlAdjustmentSubtypeId);

        jcbAdjustmentSubtypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jpEntriesControlsEast.add(jcbAdjustmentSubtypeId);

        jlTaxRegionId.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlTaxRegionId.setText("Región de impuestos: ");
        jlTaxRegionId.setPreferredSize(new java.awt.Dimension(110, 23));
        jpEntriesControlsEast.add(jlTaxRegionId);

        jcbTaxRegionId.setPreferredSize(new java.awt.Dimension(200, 23));
        jpEntriesControlsEast.add(jcbTaxRegionId);

        jbTaxRegionId.setText("...");
        jbTaxRegionId.setToolTipText("Seleccionar región de impuestos");
        jbTaxRegionId.setFocusable(false);
        jbTaxRegionId.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEntriesControlsEast.add(jbTaxRegionId);

        jbEditTaxRegion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditTaxRegion.setToolTipText("Habilitar región impuestos");
        jbEditTaxRegion.setFocusable(false);
        jbEditTaxRegion.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEntriesControlsEast.add(jbEditTaxRegion);

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

        jPanel46.setLayout(new java.awt.GridLayout(10, 1, 0, 1));

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
        jbSalesSupervisor.setToolTipText("Seleccionar supervisor de ventas");
        jbSalesSupervisor.setFocusable(false);
        jbSalesSupervisor.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel56.add(jbSalesSupervisor);

        jPanel46.add(jPanel56);

        jPanel64.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkContactId_n.setText("Comprador:");
        jlFkContactId_n.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel64.add(jlFkContactId_n);

        jPanel46.add(jPanel64);

        jPanel80.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jcbFkContactId_n.setPreferredSize(new java.awt.Dimension(285, 23));
        jPanel80.add(jcbFkContactId_n);

        jPanel46.add(jPanel80);

        jpOtherMarketing.add(jPanel46, java.awt.BorderLayout.NORTH);

        jpMarketing.add(jpOtherMarketing, java.awt.BorderLayout.CENTER);

        jpOtherLogistics.setBorder(javax.swing.BorderFactory.createTitledBorder("Logística:"));
        jpOtherLogistics.setPreferredSize(new java.awt.Dimension(375, 10));
        jpOtherLogistics.setLayout(new java.awt.BorderLayout());

        jPanel49.setLayout(new java.awt.GridLayout(10, 1, 0, 1));

        jPanel50.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel50.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkIncotermId.setText("Entrega (Incoterm): *");
        jlFkIncotermId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel50.add(jlFkIncotermId);

        jcbFkIncotermId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel50.add(jcbFkIncotermId);

        jbFkIncotermId.setText("...");
        jbFkIncotermId.setToolTipText("Seleccionar entrega (Incoterm)");
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

        jlFkModeOfTransportationTypeId.setText("Tipo modo transp.: *");
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

        jlFkCarrierTypeId.setText("Tipo transportista: *");
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

        jtfTicket.setText("TEXT");
        jtfTicket.setPreferredSize(new java.awt.Dimension(177, 23));
        jPanel36.add(jtfTicket);

        jtbLinkTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_dps_link_off.gif"))); // NOI18N
        jtbLinkTicket.setToolTipText("Vinculación de boletos de báscula o captura manual");
        jtbLinkTicket.setPreferredSize(new java.awt.Dimension(23, 23));
        jtbLinkTicket.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_dps_link.gif"))); // NOI18N
        jPanel36.add(jtbLinkTicket);

        jbEditLogistics.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit_ro.gif"))); // NOI18N
        jbEditLogistics.setToolTipText("Modificar datos");
        jbEditLogistics.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel36.add(jbEditLogistics);

        jPanel49.add(jPanel36);

        jpOtherLogistics.add(jPanel49, java.awt.BorderLayout.NORTH);

        jpMarketing.add(jpOtherLogistics, java.awt.BorderLayout.EAST);

        jTabbedPane.addTab("Comercialización", jpMarketing);

        jpNotes.setLayout(new java.awt.BorderLayout(0, 2));

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

        jbPickSystemNotes.setText("...");
        jbPickSystemNotes.setToolTipText("Seleccionar notas de sistema");
        jbPickSystemNotes.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesControls.add(jbPickSystemNotes);

        jsNotes02.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jsNotes02.setPreferredSize(new java.awt.Dimension(3, 23));
        jpNotesControls.add(jsNotes02);

        jbEditNotes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit_ro.gif"))); // NOI18N
        jbEditNotes.setToolTipText("Modificar notas");
        jbEditNotes.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesControls.add(jbEditNotes);

        jpNotes.add(jpNotesControls, java.awt.BorderLayout.PAGE_START);

        jTabbedPane.addTab("Notas", jpNotes);

        jpCfdInternationalTrade.setBorder(javax.swing.BorderFactory.createTitledBorder("Comercio exterior:"));
        jpCfdInternationalTrade.setLayout(new java.awt.GridLayout(1, 2));

        jPanel109.setLayout(new java.awt.BorderLayout());

        jPanel97.setLayout(new java.awt.GridLayout(9, 1, 0, 2));

        jPanel111.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel111.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jckCfdCceApplies.setText("Incorporar complemento comercio exterior");
        jckCfdCceApplies.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel111.add(jckCfdCceApplies);

        jPanel97.add(jPanel111);

        jPanel98.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel98.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCfdCceMoveReason.setText("Motivo de traslado: *");
        jlCfdCceMoveReason.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel98.add(jlCfdCceMoveReason);

        jcbCfdCceMoveReason.setEnabled(false);
        jcbCfdCceMoveReason.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel98.add(jcbCfdCceMoveReason);

        jPanel97.add(jPanel98);

        jPanel99.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel99.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCfdCceOperationType.setText("Tipo operación: *");
        jlCfdCceOperationType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel99.add(jlCfdCceOperationType);

        jcbCfdCceOperationType.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel99.add(jcbCfdCceOperationType);

        jPanel97.add(jPanel99);

        jPanel100.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel100.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCfdCceRequestKey.setText("Clave de pedimento: *");
        jlCfdCceRequestKey.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel100.add(jlCfdCceRequestKey);

        jcbCfdCceRequestKey.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel100.add(jcbCfdCceRequestKey);

        jPanel97.add(jPanel100);

        jPanel101.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel101.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCfdCceCertificateOrigin.setText("Certificado origen: *");
        jlCfdCceCertificateOrigin.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel101.add(jlCfdCceCertificateOrigin);

        jtfCfdCceCertificateOrigin.setText("ABC000ABC000");
        jtfCfdCceCertificateOrigin.setToolTipText("Referencia");
        jtfCfdCceCertificateOrigin.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel101.add(jtfCfdCceCertificateOrigin);

        jPanel97.add(jPanel101);

        jPanel102.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel102.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCfdCceNumberCertificateOrigin.setText("Núm. certificado origen:");
        jlCfdCceNumberCertificateOrigin.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel102.add(jlCfdCceNumberCertificateOrigin);

        jtfCfdCceNumberCertificateOrigin.setText("ABC000ABC000");
        jtfCfdCceNumberCertificateOrigin.setToolTipText("Referencia");
        jtfCfdCceNumberCertificateOrigin.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel102.add(jtfCfdCceNumberCertificateOrigin);

        jPanel97.add(jPanel102);

        jPanel108.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel108.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCfdCceSubdivision.setText("Subdivisión: *");
        jlCfdCceSubdivision.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel108.add(jlCfdCceSubdivision);

        jtfCfdCceSubdivision.setText("ABC000ABC000");
        jtfCfdCceSubdivision.setToolTipText("Referencia");
        jtfCfdCceSubdivision.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel108.add(jtfCfdCceSubdivision);

        jPanel97.add(jPanel108);

        jPanel116.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel116.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));
        jPanel97.add(jPanel116);

        jPanel117.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel117.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlExportation.setText("Tipo exportación: *");
        jlExportation.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel117.add(jlExportation);

        jcbExportation.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel117.add(jcbExportation);

        jPanel97.add(jPanel117);

        jPanel109.add(jPanel97, java.awt.BorderLayout.NORTH);

        jpCfdInternationalTrade.add(jPanel109);

        jPanel110.setLayout(new java.awt.BorderLayout());

        jPanel96.setLayout(new java.awt.GridLayout(8, 1, 0, 2));

        jPanel103.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel103.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCfdCceExchangeRateUsd.setText("Tipo de cambio USD: *");
        jlCfdCceExchangeRateUsd.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel103.add(jlCfdCceExchangeRateUsd);

        jtfCfdCceExchangeRateUsd.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfCfdCceExchangeRateUsd.setText("0.0000");
        jtfCfdCceExchangeRateUsd.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel103.add(jtfCfdCceExchangeRateUsd);

        jPanel96.add(jPanel103);

        jPanel104.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel104.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCfdCceTotalUsd.setText("Total USD: *");
        jlCfdCceTotalUsd.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel104.add(jlCfdCceTotalUsd);

        jtfCfdCceTotalUsd.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfCfdCceTotalUsd.setText("0,000,000,000.00");
        jtfCfdCceTotalUsd.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel104.add(jtfCfdCceTotalUsd);

        jbCfdCceCalcTotalUsd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/gui/img/icon_cal.gif"))); // NOI18N
        jbCfdCceCalcTotalUsd.setToolTipText("Calcular total USD");
        jbCfdCceCalcTotalUsd.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel104.add(jbCfdCceCalcTotalUsd);

        jPanel96.add(jPanel104);

        jPanel106.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel96.add(jPanel106);

        jPanel121.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdCceFkBizPartnerAddressee.setText("Destinatario propio:");
        jlCfdCceFkBizPartnerAddressee.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel121.add(jlCfdCceFkBizPartnerAddressee);

        jcbCfdCceFkBizPartnerAddressee.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel121.add(jcbCfdCceFkBizPartnerAddressee);

        jPanel96.add(jPanel121);

        jPanel122.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdCceAddresseeBizPartner.setText("Otro asociado de negocios como destinatario:");
        jlCfdCceAddresseeBizPartner.setPreferredSize(new java.awt.Dimension(375, 23));
        jPanel122.add(jlCfdCceAddresseeBizPartner);

        jPanel96.add(jPanel122);

        jPanel107.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdCceFkAddresseeBizPartner.setText("Destinatario:");
        jlCfdCceFkAddresseeBizPartner.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel107.add(jlCfdCceFkAddresseeBizPartner);

        jcbCfdCceFkAddresseeBizPartner.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel107.add(jcbCfdCceFkAddresseeBizPartner);

        jPanel96.add(jPanel107);

        jPanel112.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdCceFkAddresseeBizPartnerBranch.setText("Sucursal destinatario:");
        jlCfdCceFkAddresseeBizPartnerBranch.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel112.add(jlCfdCceFkAddresseeBizPartnerBranch);

        jcbCfdCceFkAddresseeBizPartnerBranch.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel112.add(jcbCfdCceFkAddresseeBizPartnerBranch);

        jPanel96.add(jPanel112);

        jPanel113.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdCceFkAddresseeBizPartnerBranchAddress.setText("Domicilio destinatario:");
        jlCfdCceFkAddresseeBizPartnerBranchAddress.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel113.add(jlCfdCceFkAddresseeBizPartnerBranchAddress);

        jcbCfdCceFkAddresseeBizPartnerBranchAddress.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel113.add(jcbCfdCceFkAddresseeBizPartnerBranchAddress);

        jPanel96.add(jPanel113);

        jPanel110.add(jPanel96, java.awt.BorderLayout.NORTH);

        jpCfdInternationalTrade.add(jPanel110);

        jTabbedPane.addTab("CFD: comercio exterior", jpCfdInternationalTrade);

        jpCfdAddenda.setBorder(javax.swing.BorderFactory.createTitledBorder("Addenda:"));
        jpCfdAddenda.setLayout(new java.awt.BorderLayout());

        jPanel91.setLayout(new java.awt.BorderLayout());

        jPanel118.setLayout(new java.awt.GridLayout(9, 1, 0, 2));

        jPanel68.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel68.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddCfdAddendaType.setText("Tipo addenda:");
        jlAddCfdAddendaType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel68.add(jlAddCfdAddendaType);

        jtfAddCfdAddendaType.setEditable(false);
        jtfAddCfdAddendaType.setText("TEXT");
        jtfAddCfdAddendaType.setFocusable(false);
        jtfAddCfdAddendaType.setOpaque(false);
        jtfAddCfdAddendaType.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel68.add(jtfAddCfdAddendaType);

        jPanel118.add(jPanel68);

        jPanel41.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddCfdAddendaSubtype.setText("Subtipo addenda: *");
        jlAddCfdAddendaSubtype.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel41.add(jlAddCfdAddendaSubtype);

        jcbAddCfdAddendaSubtype.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel41.add(jcbAddCfdAddendaSubtype);

        jPanel118.add(jPanel41);

        jPanel69.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddLorealFolioNotaRecepción.setText("Folio nota recep.: *");
        jlAddLorealFolioNotaRecepción.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel69.add(jlAddLorealFolioNotaRecepción);

        jtfAddLorealFolioNotaRecepción.setText("TEXT");
        jtfAddLorealFolioNotaRecepción.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel69.add(jtfAddLorealFolioNotaRecepción);

        jPanel118.add(jPanel69);

        jPanel70.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddBachocoSociedad.setText("Sociedad: *");
        jlAddBachocoSociedad.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel70.add(jlAddBachocoSociedad);

        jtfAddBachocoSociedad.setText("TEXT");
        jtfAddBachocoSociedad.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel70.add(jtfAddBachocoSociedad);

        jPanel118.add(jPanel70);

        jPanel71.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddBachocoOrganizaciónCompra.setText("Org. compra: *");
        jlAddBachocoOrganizaciónCompra.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel71.add(jlAddBachocoOrganizaciónCompra);

        jtfAddBachocoOrganizaciónCompra.setText("TEXT");
        jtfAddBachocoOrganizaciónCompra.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel71.add(jtfAddBachocoOrganizaciónCompra);

        jPanel118.add(jPanel71);

        jPanel72.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddBachocoDivisión.setText("División: *");
        jlAddBachocoDivisión.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel72.add(jlAddBachocoDivisión);

        jtfAddBachocoDivisión.setText("TEXT");
        jtfAddBachocoDivisión.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel72.add(jtfAddBachocoDivisión);

        jPanel118.add(jPanel72);

        jPanel81.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddModeloDpsDescripción.setText("Descripción fact.: *");
        jlAddModeloDpsDescripción.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel81.add(jlAddModeloDpsDescripción);

        jtfAddModeloDpsDescripción.setText("TEXT");
        jtfAddModeloDpsDescripción.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel81.add(jtfAddModeloDpsDescripción);

        jPanel118.add(jPanel81);

        jPanel91.add(jPanel118, java.awt.BorderLayout.WEST);

        jPanel119.setLayout(new java.awt.GridLayout(9, 1, 0, 2));

        jPanel82.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddSorianaTienda.setText("Tienda: *");
        jlAddSorianaTienda.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel82.add(jlAddSorianaTienda);

        jtfAddSorianaTienda.setText("00000000");
        jtfAddSorianaTienda.setToolTipText("Folio");
        jtfAddSorianaTienda.setPreferredSize(new java.awt.Dimension(170, 23));
        jPanel82.add(jtfAddSorianaTienda);

        jPanel119.add(jPanel82);

        jPanel87.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddSorianaEntregaMercancía.setText("Entrega merc.: *");
        jlAddSorianaEntregaMercancía.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel87.add(jlAddSorianaEntregaMercancía);

        jtfAddSorianaEntregaMercancía.setText("00000000");
        jtfAddSorianaEntregaMercancía.setToolTipText("Folio");
        jtfAddSorianaEntregaMercancía.setPreferredSize(new java.awt.Dimension(170, 23));
        jPanel87.add(jtfAddSorianaEntregaMercancía);

        jPanel119.add(jPanel87);

        jPanel77.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddSorianaRemisiónFecha.setText("Fecha remisión: *");
        jlAddSorianaRemisiónFecha.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel77.add(jlAddSorianaRemisiónFecha);

        jftAddSorianaRemisiónFecha.setText("dd/mm/yyyy");
        jftAddSorianaRemisiónFecha.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel77.add(jftAddSorianaRemisiónFecha);

        jlAddSorianaRemisiónFechaHint.setForeground(java.awt.SystemColor.textInactiveText);
        jlAddSorianaRemisiónFechaHint.setText("(dd/mm/aaaa)");
        jlAddSorianaRemisiónFechaHint.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel77.add(jlAddSorianaRemisiónFechaHint);

        jPanel119.add(jPanel77);

        jPanel79.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddSorianaRemisiónFolio.setText("Folio remisión: *");
        jlAddSorianaRemisiónFolio.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel79.add(jlAddSorianaRemisiónFolio);

        jtfAddSorianaRemisiónFolio.setText("TEXT");
        jtfAddSorianaRemisiónFolio.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel79.add(jtfAddSorianaRemisiónFolio);

        jPanel119.add(jPanel79);

        jPanel88.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddSorianaPedidoFolio.setText("Folio pedido: *");
        jlAddSorianaPedidoFolio.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel88.add(jlAddSorianaPedidoFolio);

        jtfAddSorianaPedidoFolio.setText("TEXT");
        jtfAddSorianaPedidoFolio.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel88.add(jtfAddSorianaPedidoFolio);

        jPanel119.add(jPanel88);

        jPanel83.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddSorianaBultoTipo.setText("Tipo bulto: *");
        jlAddSorianaBultoTipo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel83.add(jlAddSorianaBultoTipo);

        jtfAddSorianaBultoTipo.setText("00000000");
        jtfAddSorianaBultoTipo.setToolTipText("Folio");
        jtfAddSorianaBultoTipo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel83.add(jtfAddSorianaBultoTipo);

        jPanel119.add(jPanel83);

        jPanel78.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddSorianaBultoCantidad.setText("Cantidad bultos: *");
        jlAddSorianaBultoCantidad.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel78.add(jlAddSorianaBultoCantidad);

        jtfAddSorianaBultoCantidad.setText("00000000");
        jtfAddSorianaBultoCantidad.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel78.add(jtfAddSorianaBultoCantidad);

        jPanel119.add(jPanel78);

        jPanel90.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddSorianaNotaEntradaFolio.setText("Folio entrada: *");
        jlAddSorianaNotaEntradaFolio.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel90.add(jlAddSorianaNotaEntradaFolio);

        jtfAddSorianaNotaEntradaFolio.setText("00000000");
        jtfAddSorianaNotaEntradaFolio.setToolTipText("Folio");
        jtfAddSorianaNotaEntradaFolio.setPreferredSize(new java.awt.Dimension(170, 23));
        jPanel90.add(jtfAddSorianaNotaEntradaFolio);

        jPanel119.add(jPanel90);

        jPanel120.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddSorianaCita.setText("Cita: *");
        jlAddSorianaCita.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel120.add(jlAddSorianaCita);

        jtfAddSorianaCita.setText("00000000");
        jtfAddSorianaCita.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel120.add(jtfAddSorianaCita);

        jPanel119.add(jPanel120);

        jPanel91.add(jPanel119, java.awt.BorderLayout.CENTER);

        jPanel76.setLayout(new java.awt.GridLayout(9, 1, 0, 2));

        jPanel123.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddAmc71SupplierGln.setText("GLN proveedor: *");
        jlAddAmc71SupplierGln.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel123.add(jlAddAmc71SupplierGln);

        jcbAddAmc71SupplierGln.setEditable(true);
        jcbAddAmc71SupplierGln.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel123.add(jcbAddAmc71SupplierGln);

        jlAddAmc71SupplierGlnHint.setForeground(java.awt.SystemColor.textInactiveText);
        jlAddAmc71SupplierGlnHint.setText("(de la empresa)");
        jlAddAmc71SupplierGlnHint.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel123.add(jlAddAmc71SupplierGlnHint);

        jPanel76.add(jPanel123);

        jPanel124.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddAmc71SupplierNumber.setText("No. proveedor: *");
        jlAddAmc71SupplierNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel124.add(jlAddAmc71SupplierNumber);

        jtfAddAmc71SupplierNumber.setText("TEXT");
        jtfAddAmc71SupplierNumber.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel124.add(jtfAddAmc71SupplierNumber);

        jlAddAmc71SupplierNumberHint.setForeground(java.awt.SystemColor.textInactiveText);
        jlAddAmc71SupplierNumberHint.setText("(de la empresa)");
        jlAddAmc71SupplierNumberHint.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel124.add(jlAddAmc71SupplierNumberHint);

        jPanel76.add(jPanel124);

        jPanel125.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddAmc71CompanyGln.setText("GLN compañía: *");
        jlAddAmc71CompanyGln.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel125.add(jlAddAmc71CompanyGln);

        jcbAddAmc71CompanyGln.setEditable(true);
        jcbAddAmc71CompanyGln.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel125.add(jcbAddAmc71CompanyGln);

        jlAddAmc71CompanyGlnHint.setForeground(java.awt.SystemColor.textInactiveText);
        jlAddAmc71CompanyGlnHint.setText("(del cliente)");
        jlAddAmc71CompanyGlnHint.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel125.add(jlAddAmc71CompanyGlnHint);

        jPanel76.add(jPanel125);

        jPanel131.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddAmc71CompanyContact.setText("Contacto: *");
        jlAddAmc71CompanyContact.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel131.add(jlAddAmc71CompanyContact);

        jtfAddAmc71CompanyContact.setText("TEXT");
        jtfAddAmc71CompanyContact.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel131.add(jtfAddAmc71CompanyContact);

        jlAddAmc71CompanyContactHint.setForeground(java.awt.SystemColor.textInactiveText);
        jlAddAmc71CompanyContactHint.setText("(sección o depto.)");
        jlAddAmc71CompanyContactHint.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel131.add(jlAddAmc71CompanyContactHint);

        jPanel76.add(jPanel131);

        jPanel126.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddAmc71CompanyBranchGln.setText("GLN sucursal: *");
        jlAddAmc71CompanyBranchGln.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel126.add(jlAddAmc71CompanyBranchGln);

        jcbAddAmc71CompanyBranchGln.setEditable(true);
        jcbAddAmc71CompanyBranchGln.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel126.add(jcbAddAmc71CompanyBranchGln);

        jlAddAmc71CompanyBranchGlnHint.setForeground(java.awt.SystemColor.textInactiveText);
        jlAddAmc71CompanyBranchGlnHint.setText("(de la entrega)");
        jlAddAmc71CompanyBranchGlnHint.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel126.add(jlAddAmc71CompanyBranchGlnHint);

        jPanel76.add(jPanel126);

        jPanel130.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddAmc71ShipToName.setText("Nombre sucursal: *");
        jlAddAmc71ShipToName.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel130.add(jlAddAmc71ShipToName);

        jtfAddAmc71ShipToName.setText("TEXT");
        jtfAddAmc71ShipToName.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel130.add(jtfAddAmc71ShipToName);

        jPanel76.add(jPanel130);

        jPanel127.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddAmc71ShipToAddress.setText("Domicilio entrega: *");
        jlAddAmc71ShipToAddress.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel127.add(jlAddAmc71ShipToAddress);

        jtfAddAmc71ShipToAddress.setText("TEXT");
        jtfAddAmc71ShipToAddress.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel127.add(jtfAddAmc71ShipToAddress);

        jPanel76.add(jPanel127);

        jPanel128.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddAmc71ShipToCity.setText("Ciudad entrega: *");
        jlAddAmc71ShipToCity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel128.add(jlAddAmc71ShipToCity);

        jtfAddAmc71ShipToCity.setText("TEXT");
        jtfAddAmc71ShipToCity.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel128.add(jtfAddAmc71ShipToCity);

        jPanel76.add(jPanel128);

        jPanel129.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddAmc71ShipToPostalCode.setText("Código postal: *");
        jlAddAmc71ShipToPostalCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel129.add(jlAddAmc71ShipToPostalCode);

        jtfAddAmc71ShipToPostalCode.setText("TEXT");
        jtfAddAmc71ShipToPostalCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel129.add(jtfAddAmc71ShipToPostalCode);

        jPanel76.add(jPanel129);

        jPanel91.add(jPanel76, java.awt.BorderLayout.EAST);

        jpCfdAddenda.add(jPanel91, java.awt.BorderLayout.NORTH);

        jTabbedPane.addTab("CFD: addenda", jpCfdAddenda);

        jpCfdXml.setLayout(new java.awt.BorderLayout());

        jPanel73.setBorder(javax.swing.BorderFactory.createTitledBorder("XML:"));
        jPanel73.setLayout(new java.awt.BorderLayout(0, 2));

        jPanel74.setLayout(new java.awt.GridLayout(7, 2, 0, 2));

        jPanel75.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel75.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFileXml.setText("Archivo XML:");
        jlFileXml.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel75.add(jlFileXml);

        jtfFileXml.setEditable(false);
        jtfFileXml.setText("XML");
        jtfFileXml.setOpaque(false);
        jtfFileXml.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel75.add(jtfFileXml);

        jbLoadFileXml.setText("...");
        jbLoadFileXml.setToolTipText("Seleccionar archivo XML...");
        jbLoadFileXml.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel75.add(jbLoadFileXml);

        jbDeleteFileXml.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbDeleteFileXml.setToolTipText("Eliminar archivo XML");
        jbDeleteFileXml.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel75.add(jbDeleteFileXml);

        jlSpace.setPreferredSize(new java.awt.Dimension(10, 23));
        jPanel75.add(jlSpace);

        jckValidateOnSaveFileXml.setText("Validar estatus SAT, serie y folio del CFDI al guardar");
        jckValidateOnSaveFileXml.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel75.add(jckValidateOnSaveFileXml);

        jPanel74.add(jPanel75);

        jPanel105.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFilePdf.setText("Archivo PDF:");
        jlFilePdf.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel105.add(jlFilePdf);

        jtfFilePdf.setEditable(false);
        jtfFilePdf.setText("PDF");
        jtfFilePdf.setOpaque(false);
        jtfFilePdf.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel105.add(jtfFilePdf);

        jbLoadFilePdf.setText("...");
        jbLoadFilePdf.setToolTipText("Seleccionar archivo XML...");
        jbLoadFilePdf.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel105.add(jbLoadFilePdf);

        jbDeleteFilePdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbDeleteFilePdf.setToolTipText("Eliminar archivo XML");
        jbDeleteFilePdf.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel105.add(jbDeleteFilePdf);

        jlSpace1.setPreferredSize(new java.awt.Dimension(10, 23));
        jPanel105.add(jlSpace1);

        jlBillOfLading.setText("Carta porte:");
        jlBillOfLading.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel105.add(jlBillOfLading);

        jtfBillOfLading.setEditable(false);
        jtfBillOfLading.setText("XML");
        jtfBillOfLading.setFocusable(false);
        jtfBillOfLading.setOpaque(false);
        jtfBillOfLading.setPreferredSize(new java.awt.Dimension(260, 23));
        jPanel105.add(jtfBillOfLading);

        jbLoadBillOfLading.setText("...");
        jbLoadBillOfLading.setToolTipText("Seleccionar carta porte...");
        jbLoadBillOfLading.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel105.add(jbLoadBillOfLading);

        jbDeleteBillOfLading.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbDeleteBillOfLading.setToolTipText("Eliminar archivo XML");
        jbDeleteBillOfLading.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel105.add(jbDeleteBillOfLading);

        jPanel74.add(jPanel105);

        jPanel95.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdiTaxRegimeIssuing.setText("Régimen emisor: *");
        jlCfdiTaxRegimeIssuing.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel95.add(jlCfdiTaxRegimeIssuing);

        jcbCfdiTaxRegimeIssuing.setToolTipText("Régimen fiscal del emisor");
        jcbCfdiTaxRegimeIssuing.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel95.add(jcbCfdiTaxRegimeIssuing);

        jLabel1.setPreferredSize(new java.awt.Dimension(67, 23));
        jPanel95.add(jLabel1);

        jlGlobalInf.setText("Información factura global:");
        jlGlobalInf.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel95.add(jlGlobalInf);

        jPanel74.add(jPanel95);

        jPanel93.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdiTaxRegimeReceptor.setText("Régimen receptor: *");
        jlCfdiTaxRegimeReceptor.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel93.add(jlCfdiTaxRegimeReceptor);

        jcbCfdiTaxRegimeReceptor.setToolTipText("Régimen fiscal del emisor");
        jcbCfdiTaxRegimeReceptor.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel93.add(jcbCfdiTaxRegimeReceptor);

        jLabel2.setPreferredSize(new java.awt.Dimension(67, 23));
        jPanel93.add(jLabel2);

        jlGblPeriodicity.setText("Periodo:");
        jlGblPeriodicity.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel93.add(jlGblPeriodicity);

        jcbGblPeriodicity.setToolTipText("Régimen fiscal del emisor");
        jcbGblPeriodicity.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel93.add(jcbGblPeriodicity);

        jPanel74.add(jPanel93);

        jPanel94.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdiCfdiUsage.setText("Uso CFDI: *");
        jlCfdiCfdiUsage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel94.add(jlCfdiCfdiUsage);

        jcbCfdiCfdiUsage.setToolTipText("Uso CFDI del receptor");
        jcbCfdiCfdiUsage.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel94.add(jcbCfdiCfdiUsage);

        jLabel3.setPreferredSize(new java.awt.Dimension(67, 23));
        jPanel94.add(jLabel3);

        jlGblMonth.setText("Mes:");
        jlGblMonth.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel94.add(jlGblMonth);

        jcbGblMonth.setToolTipText("Régimen fiscal del emisor");
        jcbGblMonth.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel94.add(jcbGblMonth);

        jPanel74.add(jPanel94);

        jPanel114.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdiConfirmation.setText("Clave confirmación:");
        jlCfdiConfirmation.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel114.add(jlCfdiConfirmation);

        jtfCfdiConfirmation.setText("TEXT");
        jtfCfdiConfirmation.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel114.add(jtfCfdiConfirmation);

        jLabel5.setForeground(java.awt.SystemColor.textInactiveText);
        jLabel5.setText("(Clave para expedir CFDI con importes o TC fuera de rango.)");
        jLabel5.setPreferredSize(new java.awt.Dimension(320, 23));
        jPanel114.add(jLabel5);

        jLabel4.setPreferredSize(new java.awt.Dimension(392, 23));
        jPanel114.add(jLabel4);

        jlGblYear.setText("Año:");
        jlGblYear.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel114.add(jlGblYear);

        jtfGblYear.setPreferredSize(new java.awt.Dimension(73, 23));
        jPanel114.add(jtfGblYear);

        jPanel74.add(jPanel114);

        jPanel115.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbCfdiRelatedDocs.setText("CFDI relacionados");
        jbCfdiRelatedDocs.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel115.add(jbCfdiRelatedDocs);

        jtfCfdiFirstRelatedDps.setEditable(false);
        jtfCfdiFirstRelatedDps.setText("FIRST RELATED DOCUMENT");
        jtfCfdiFirstRelatedDps.setToolTipText("CFDI relacionado insignia");
        jtfCfdiFirstRelatedDps.setFocusable(false);
        jtfCfdiFirstRelatedDps.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel115.add(jtfCfdiFirstRelatedDps);

        jPanel74.add(jPanel115);

        jPanel73.add(jPanel74, java.awt.BorderLayout.NORTH);

        jspCfdiRelatedDocs.setPreferredSize(new java.awt.Dimension(510, 83));

        jtaCfdiRelatedDocs.setEditable(false);
        jtaCfdiRelatedDocs.setColumns(20);
        jtaCfdiRelatedDocs.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jtaCfdiRelatedDocs.setRows(5);
        jtaCfdiRelatedDocs.setFocusable(false);
        jspCfdiRelatedDocs.setViewportView(jtaCfdiRelatedDocs);

        jPanel73.add(jspCfdiRelatedDocs, java.awt.BorderLayout.WEST);

        jpCfdXml.add(jPanel73, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("CFD: XML", jpCfdXml);

        jpCustomAcc.setBorder(javax.swing.BorderFactory.createTitledBorder("Personalización de contabilización:"));
        jpCustomAcc.setLayout(new java.awt.BorderLayout(0, 2));

        jpCustomAcc1.setLayout(new java.awt.BorderLayout(0, 1));

        jpCustomAcc11.setLayout(new java.awt.BorderLayout());

        jpCustomAcc111.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        jpCustomAcc1111.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAccItem.setText("Ítem partida:*");
        jlAccItem.setPreferredSize(new java.awt.Dimension(90, 23));
        jpCustomAcc1111.add(jlAccItem);

        jcbAccItem.setPreferredSize(new java.awt.Dimension(450, 23));
        jpCustomAcc1111.add(jcbAccItem);

        jbPickAccItem.setText("...");
        jbPickAccItem.setFocusable(false);
        jbPickAccItem.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCustomAcc1111.add(jbPickAccItem);

        jpCustomAcc111.add(jpCustomAcc1111);

        jpCustomAcc1112.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAccItemRef_n.setText("Ítem referencia:");
        jlAccItemRef_n.setPreferredSize(new java.awt.Dimension(90, 23));
        jpCustomAcc1112.add(jlAccItemRef_n);

        jcbAccItemRef_n.setPreferredSize(new java.awt.Dimension(450, 23));
        jpCustomAcc1112.add(jcbAccItemRef_n);

        jbPickAccItemRef_n.setText("...");
        jbPickAccItemRef_n.setFocusable(false);
        jbPickAccItemRef_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCustomAcc1112.add(jbPickAccItemRef_n);

        jpCustomAcc111.add(jpCustomAcc1112);

        jpCustomAcc11.add(jpCustomAcc111, java.awt.BorderLayout.WEST);

        jpCustomAcc112.setLayout(new java.awt.BorderLayout());

        jlAccDummyCostCenter.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlAccDummyCostCenter.setText("[Panel centro de costo-beneficio]");
        jpCustomAcc112.add(jlAccDummyCostCenter, java.awt.BorderLayout.CENTER);

        jpCustomAcc11.add(jpCustomAcc112, java.awt.BorderLayout.CENTER);

        jpCustomAcc1.add(jpCustomAcc11, java.awt.BorderLayout.PAGE_START);

        jpCustomAcc12.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        jpCustomAcc121.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAccQuantity.setText("Cantidad: *");
        jlAccQuantity.setPreferredSize(new java.awt.Dimension(90, 23));
        jpCustomAcc121.add(jlAccQuantity);

        jtfAccQuantity.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfAccQuantity.setText("0.0000");
        jtfAccQuantity.setPreferredSize(new java.awt.Dimension(90, 23));
        jpCustomAcc121.add(jtfAccQuantity);

        jlAccUnit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAccUnit.setText("Unidad partida:*");
        jlAccUnit.setPreferredSize(new java.awt.Dimension(100, 23));
        jpCustomAcc121.add(jlAccUnit);

        jcbAccUnit.setPreferredSize(new java.awt.Dimension(250, 23));
        jpCustomAcc121.add(jcbAccUnit);

        jbPickAccUnit.setText("...");
        jbPickAccUnit.setFocusable(false);
        jbPickAccUnit.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCustomAcc121.add(jbPickAccUnit);

        jckAccIsSubtotalPctApplying.setText("%");
        jckAccIsSubtotalPctApplying.setPreferredSize(new java.awt.Dimension(40, 23));
        jpCustomAcc121.add(jckAccIsSubtotalPctApplying);

        jtfAccSubtotalPct.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfAccSubtotalPct.setText("000.00%");
        jtfAccSubtotalPct.setPreferredSize(new java.awt.Dimension(60, 23));
        jpCustomAcc121.add(jtfAccSubtotalPct);

        jtfAccSubtotal.setEditable(false);
        jtfAccSubtotal.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfAccSubtotal.setText("0,000,000,000.00");
        jtfAccSubtotal.setFocusable(false);
        jtfAccSubtotal.setPreferredSize(new java.awt.Dimension(100, 23));
        jpCustomAcc121.add(jtfAccSubtotal);

        jtfAccSubtotalCur.setEditable(false);
        jtfAccSubtotalCur.setText("ERP");
        jtfAccSubtotalCur.setFocusable(false);
        jtfAccSubtotalCur.setPreferredSize(new java.awt.Dimension(30, 23));
        jpCustomAcc121.add(jtfAccSubtotalCur);

        jtfAccSubtotalCy.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfAccSubtotalCy.setText("0,000,000,000.00");
        jtfAccSubtotalCy.setPreferredSize(new java.awt.Dimension(100, 23));
        jpCustomAcc121.add(jtfAccSubtotalCy);

        jtfAccSubtotalCyCur.setEditable(false);
        jtfAccSubtotalCyCur.setText("CUR");
        jtfAccSubtotalCyCur.setFocusable(false);
        jtfAccSubtotalCyCur.setPreferredSize(new java.awt.Dimension(30, 23));
        jpCustomAcc121.add(jtfAccSubtotalCyCur);

        jbExeWizardAccSubtotal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_wizard.gif"))); // NOI18N
        jbExeWizardAccSubtotal.setToolTipText("Asignar remanente");
        jbExeWizardAccSubtotal.setFocusable(false);
        jbExeWizardAccSubtotal.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCustomAcc121.add(jbExeWizardAccSubtotal);

        jpCustomAcc12.add(jpCustomAcc121);

        jpCustomAcc122.setLayout(new java.awt.BorderLayout());

        jPanel139.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAccConcept.setText("Concepto:*");
        jlAccConcept.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel139.add(jlAccConcept);

        jtfAccConcept.setText("TEXT");
        jtfAccConcept.setPreferredSize(new java.awt.Dimension(450, 23));
        jPanel139.add(jtfAccConcept);

        jpCustomAcc122.add(jPanel139, java.awt.BorderLayout.WEST);

        jPanel142.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbCustomAccEntryNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbCustomAccEntryNew.setToolTipText("Crear partida [Ctrl + N]");
        jbCustomAccEntryNew.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel142.add(jbCustomAccEntryNew);

        jbCustomAccEntryCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_copy.gif"))); // NOI18N
        jbCustomAccEntryCopy.setToolTipText("Copiar partida");
        jbCustomAccEntryCopy.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel142.add(jbCustomAccEntryCopy);

        jbCustomAccEntryEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbCustomAccEntryEdit.setToolTipText("Modificar partida [Ctrl + M]");
        jbCustomAccEntryEdit.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel142.add(jbCustomAccEntryEdit);

        jbCustomAccEntryDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbCustomAccEntryDelete.setToolTipText("Eliminar partida [Ctrl + D]");
        jbCustomAccEntryDelete.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel142.add(jbCustomAccEntryDelete);

        jbCustomAccEntryOk.setText("Aceptar"); // NOI18N
        jbCustomAccEntryOk.setToolTipText("[Ctrl + Enter]");
        jbCustomAccEntryOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel142.add(jbCustomAccEntryOk);

        jbCustomAccEntryCancel.setText("Cancelar"); // NOI18N
        jbCustomAccEntryCancel.setToolTipText("[Escape]");
        jPanel142.add(jbCustomAccEntryCancel);

        jpCustomAcc122.add(jPanel142, java.awt.BorderLayout.CENTER);

        jpCustomAcc12.add(jpCustomAcc122);

        jpCustomAcc1.add(jpCustomAcc12, java.awt.BorderLayout.PAGE_END);

        jpCustomAcc.add(jpCustomAcc1, java.awt.BorderLayout.NORTH);

        jpCustomAcc2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jlAccSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAccSubtotal.setText("Subtotal:");
        jlAccSubtotal.setPreferredSize(new java.awt.Dimension(75, 23));
        jpCustomAcc2.add(jlAccSubtotal);

        jtfAccSubtotalSubtotalPct.setEditable(false);
        jtfAccSubtotalSubtotalPct.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfAccSubtotalSubtotalPct.setText("000.00%");
        jtfAccSubtotalSubtotalPct.setFocusable(false);
        jtfAccSubtotalSubtotalPct.setPreferredSize(new java.awt.Dimension(60, 23));
        jpCustomAcc2.add(jtfAccSubtotalSubtotalPct);

        jtfAccSubtotalSubtotal.setEditable(false);
        jtfAccSubtotalSubtotal.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfAccSubtotalSubtotal.setText("0,000,000,000.00");
        jtfAccSubtotalSubtotal.setFocusable(false);
        jtfAccSubtotalSubtotal.setPreferredSize(new java.awt.Dimension(100, 23));
        jpCustomAcc2.add(jtfAccSubtotalSubtotal);

        jtfAccSubtotalSubtotalCur.setEditable(false);
        jtfAccSubtotalSubtotalCur.setText("ERP");
        jtfAccSubtotalSubtotalCur.setFocusable(false);
        jtfAccSubtotalSubtotalCur.setPreferredSize(new java.awt.Dimension(30, 23));
        jpCustomAcc2.add(jtfAccSubtotalSubtotalCur);

        jtfAccSubtotalSubtotalCy.setEditable(false);
        jtfAccSubtotalSubtotalCy.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfAccSubtotalSubtotalCy.setText("0,000,000,000.00");
        jtfAccSubtotalSubtotalCy.setFocusable(false);
        jtfAccSubtotalSubtotalCy.setPreferredSize(new java.awt.Dimension(100, 23));
        jpCustomAcc2.add(jtfAccSubtotalSubtotalCy);

        jtfAccSubtotalSubtotalCyCur.setEditable(false);
        jtfAccSubtotalSubtotalCyCur.setText("CUR");
        jtfAccSubtotalSubtotalCyCur.setFocusable(false);
        jtfAccSubtotalSubtotalCyCur.setPreferredSize(new java.awt.Dimension(30, 23));
        jpCustomAcc2.add(jtfAccSubtotalSubtotalCyCur);

        jpCustomAcc.add(jpCustomAcc2, java.awt.BorderLayout.SOUTH);

        jTabbedPane.addTab("Contabilización", jpCustomAcc);

        jpDocument.add(jTabbedPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpDocument, java.awt.BorderLayout.CENTER);

        jpControls.setLayout(new java.awt.BorderLayout(5, 0));

        jpControlsPk.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jtfPkRo.setEditable(false);
        jtfPkRo.setText("2001-999999");
        jtfPkRo.setEnabled(false);
        jtfPkRo.setFocusable(false);
        jtfPkRo.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControlsPk.add(jtfPkRo);

        jpControls.add(jpControlsPk, java.awt.BorderLayout.WEST);

        jpControlsRecord.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jpControls.add(jpControlsRecord, java.awt.BorderLayout.CENTER);

        jpControlsButtons.setLayout(new java.awt.BorderLayout(5, 0));

        jlQuantityTotal.setText("Cantidad total:");
        jlQuantityTotal.setPreferredSize(new java.awt.Dimension(80, 23));
        jpControlsButtonsWest.add(jlQuantityTotal);

        jtfQuantityTotal.setEditable(false);
        jtfQuantityTotal.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfQuantityTotal.setText("0.000");
        jtfQuantityTotal.setFocusable(false);
        jtfQuantityTotal.setPreferredSize(new java.awt.Dimension(100, 23));
        jpControlsButtonsWest.add(jtfQuantityTotal);

        jpControlsButtons.add(jpControlsButtonsWest, java.awt.BorderLayout.WEST);

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

        jpControls.add(jpControlsButtons, java.awt.BorderLayout.EAST);

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

    private void initComponentsExtra() {
        int i = 0;
        STableColumnForm[] aoTableColumns = null;

        // document header fields:

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
        moFieldNumber.setLengthMax(SDataDps.LEN_NUMBER);
        moFieldNumberReference = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfNumberReference, jlNumber);
        moFieldNumberReference.setLengthMax(25);
        moFieldDaysOfCredit = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfDaysOfCredit, jlDaysOfCredit);
        moFieldIsLinked = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsLinked);
        moFieldIsClosed = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsClosed);
        moFieldIsAudited = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsAudited);
        moFieldIsAuthorized = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsAuthorized);
        moFieldIsCopy = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsCopy);
        moFieldIsSystem = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsSystem);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDeleted);
        moFieldFkPaymentTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkPaymentTypeId, jlFkPaymentTypeId);
        moFieldCfdiPaymentWay = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbCfdiPaymentWay, jlCfdiPaymentWay);
        moFieldCfdiPaymentMethod = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbCfdiPaymentMethod, jlCfdiPaymentMethod);
        moFieldConditionsPayment = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfConditionsPayment, jlConditionsPayment);
        moFieldConditionsPayment.setLengthMax(50);
        moFieldFkLanguajeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkLanguageId, jlFkLanguageId);
        moFieldFkDpsNatureId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkDpsNatureId, jlFkDpsNatureId);
        moFieldFkFunctionalAreaId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkFunctionalAreaId, jlFkFunctionalAreaId);
        moFieldFkCurrencyId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCurrencyId, jlFkCurrencyId);
        moFieldFkCurrencyId.setPickerButton(jbFkCurrencyId);
        moFieldIsDiscountDocApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDiscountDocApplying);
        moFieldIsDiscountDocPercentage = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDiscountDocPercentage);
        moFieldDiscountDocPercentage = new SFormField(miClient, SLibConstants.DATA_TYPE_FLOAT, false, jtfDiscountDocPercentage, jckIsDiscountDocPercentage);
        moFieldDiscountDocPercentage.setIsPercent(true);
        moFieldDiscountDocPercentage.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsPercentageFormat());
        moFieldExchangeRate = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfExchangeRate, jlExchangeRate);
        moFieldExchangeRate.setDecimalFormat(SLibUtils.getDecimalFormatExchangeRate());
        moFieldExchangeRate.setPickerButton(jbExchangeRate);
        moFieldExchangeRateSystem = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfExchangeRateSystemRo, jlExchangeRateSystem);
        moFieldExchangeRateSystem.setDecimalFormat(SLibUtils.getDecimalFormatExchangeRate());
        
        // marketing fields:
        
        moFieldShipments = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfShipments, jckShipments);
        moFieldShipments.setTabbedPaneIndex(TAB_MKT, jTabbedPane);
        moFieldFkProductionOrderId_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkProductionOrderId_n, jlFkProductionOrderId_n);
        moFieldFkProductionOrderId_n.setTabbedPaneIndex(TAB_MKT, jTabbedPane);
        moFieldFkProductionOrderId_n.setPickerButton(jbFkProductionOrderId_n);
        moFieldFkContactId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkContactId_n, jlFkContactId_n);
        moFieldFkContactId_n.setTabbedPaneIndex(TAB_MKT, jTabbedPane);
        moFieldFkIncotermId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkIncotermId, jlFkIncotermId);
        moFieldFkIncotermId.setTabbedPaneIndex(TAB_MKT, jTabbedPane);
        moFieldFkIncotermId.setPickerButton(jbFkIncotermId);
        moFieldFkSpotSrcId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkSpotSrcId_n, jlFkSpotSrcId_n);
        moFieldFkSpotSrcId_n.setTabbedPaneIndex(TAB_MKT, jTabbedPane);
        moFieldFkSpotDesId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkSpotDesId_n, jlFkSpotDesId_n);
        moFieldFkSpotDesId_n.setTabbedPaneIndex(TAB_MKT, jTabbedPane);
        moFieldFkModeOfTransportationTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkModeOfTransportationTypeId, jlFkModeOfTransportationTypeId);
        moFieldFkModeOfTransportationTypeId.setTabbedPaneIndex(TAB_MKT, jTabbedPane);
        moFieldFkModeOfTransportationTypeId.setPickerButton(jbFkModeOfTransportationTypeId);
        moFieldFkCarrierTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCarrierTypeId, jlFkCarrierTypeId);
        moFieldFkCarrierTypeId.setTabbedPaneIndex(TAB_MKT, jTabbedPane);
        moFieldFkCarrierId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkCarrierId_n, jlFkCarrierId_n);
        moFieldFkCarrierId_n.setTabbedPaneIndex(TAB_MKT, jTabbedPane);
        moFieldFkCarrierId_n.setPickerButton(jbFkCarrierId_n);
        moFieldFkVehicleTypeId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkVehicleTypeId_n, jlFkVehicleTypeId_n);
        moFieldFkVehicleTypeId_n.setTabbedPaneIndex(TAB_MKT, jTabbedPane);
        moFieldFkVehicleId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkVehicleId_n, jlFkVehicleId_n);
        moFieldFkVehicleId_n.setTabbedPaneIndex(TAB_MKT, jTabbedPane);
        moFieldFkVehicleId_n.setPickerButton(jbFkVehicleId_n);
        moFieldReqNum = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfReqNum, jlRequisicion);
        moFieldReqNum.setTabbedPaneIndex(TAB_MKT, jTabbedPane);
        moFieldReqNum.setLengthMax(10);
        moFieldDriver = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, (JTextField) jcbDriver.getEditor().getEditorComponent(), jlDriver);
        moFieldDriver.setTabbedPaneIndex(TAB_MKT, jTabbedPane);
        moFieldDriver.setLengthMax(50);
        moFieldPlate = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, (JTextField) jcbPlate.getEditor().getEditorComponent(), jlDriver);
        moFieldPlate.setTabbedPaneIndex(TAB_MKT, jTabbedPane);
        moFieldPlate.setLengthMax(25);
        moFieldTicket = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfTicket, jlTicket);
        moFieldTicket.setTabbedPaneIndex(TAB_MKT, jTabbedPane);
        moFieldTicket.setLengthMax(50);
        
        // CFD International Commerce fields:
        
        moFieldCfdCceApplies = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckCfdCceApplies);
        moFieldCfdCceApplies.setTabbedPaneIndex(TAB_CFD_INT_COM, jTabbedPane);
        moFieldCfdCceMoveReason = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbCfdCceMoveReason, jlCfdCceMoveReason);
        moFieldCfdCceMoveReason.setTabbedPaneIndex(TAB_CFD_INT_COM, jTabbedPane);
        moFieldCfdCceOperationType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbCfdCceOperationType, jlCfdCceOperationType);
        moFieldCfdCceOperationType.setTabbedPaneIndex(TAB_CFD_INT_COM, jTabbedPane);
        moFieldCfdCceRequestKey = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbCfdCceRequestKey, jlCfdCceRequestKey);
        moFieldCfdCceRequestKey.setTabbedPaneIndex(TAB_CFD_INT_COM, jTabbedPane);
        moFieldCfdCceExportation = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbExportation, jlExportation);
        moFieldCfdCceExportation.setTabbedPaneIndex(TAB_CFD_INT_COM, jTabbedPane);
        moFieldCfdCceCertificateOrigin = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfCfdCceCertificateOrigin, jlCfdCceCertificateOrigin);
        moFieldCfdCceCertificateOrigin.setTabbedPaneIndex(TAB_CFD_INT_COM, jTabbedPane);
        moFieldCfdCceNumberCertificateOrigin = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfCfdCceNumberCertificateOrigin, jlCfdCceNumberCertificateOrigin);
        moFieldCfdCceNumberCertificateOrigin.setTabbedPaneIndex(TAB_CFD_INT_COM, jTabbedPane);
        moFieldCfdCceSubdivision = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfCfdCceSubdivision, jlCfdCceSubdivision);
        moFieldCfdCceSubdivision.setTabbedPaneIndex(TAB_CFD_INT_COM, jTabbedPane);
        moFieldCfdCceExchangeRateUsd = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfCfdCceExchangeRateUsd, jlCfdCceExchangeRateUsd);
        moFieldCfdCceExchangeRateUsd.setDecimalFormat(SLibUtils.getDecimalFormatExchangeRate());
        moFieldCfdCceExchangeRateUsd.setTabbedPaneIndex(TAB_CFD_INT_COM, jTabbedPane);
        moFieldCfdCceTotalUsd = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfCfdCceTotalUsd, jlCfdCceTotalUsd);
        moFieldCfdCceTotalUsd.setTabbedPaneIndex(TAB_CFD_INT_COM, jTabbedPane);
        moFieldCfdCceBizPartnerAddressee = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCfdCceFkBizPartnerAddressee, jlCfdCceFkBizPartnerAddressee);
        moFieldCfdCceBizPartnerAddressee.setTabbedPaneIndex(TAB_CFD_INT_COM, jTabbedPane);
        moFieldCfdCceAddresseeBizPartner = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCfdCceFkAddresseeBizPartner, jlCfdCceFkAddresseeBizPartner);
        moFieldCfdCceAddresseeBizPartner.setTabbedPaneIndex(TAB_CFD_INT_COM, jTabbedPane);
        moFieldCfdCceAddresseeBizPartnerBranch = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCfdCceFkAddresseeBizPartnerBranch, jlCfdCceFkAddresseeBizPartnerBranch);
        moFieldCfdCceAddresseeBizPartnerBranch.setTabbedPaneIndex(TAB_CFD_INT_COM, jTabbedPane);
        moFieldCfdCceAddresseeBizPartnerBranchAddress = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCfdCceFkAddresseeBizPartnerBranchAddress, jlCfdCceFkAddresseeBizPartnerBranchAddress);
        moFieldCfdCceAddresseeBizPartnerBranchAddress.setTabbedPaneIndex(TAB_CFD_INT_COM, jTabbedPane);

        // CFD Addenda fields:

        moFieldAddCfdAddendaSubtype = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbAddCfdAddendaSubtype, jlAddCfdAddendaSubtype);
        moFieldAddCfdAddendaSubtype.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddLorealFolioNotaRecepción = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddLorealFolioNotaRecepción, jlAddLorealFolioNotaRecepción);
        moFieldAddLorealFolioNotaRecepción.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddLorealFolioNotaRecepción.setLengthMax(50);
        moFieldAddBachocoSociedad = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddBachocoSociedad, jlAddBachocoSociedad);
        moFieldAddBachocoSociedad.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddBachocoSociedad.setLengthMax(13);
        moFieldAddBachocoOrganizaciónCompra = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddBachocoOrganizaciónCompra, jlAddBachocoOrganizaciónCompra);
        moFieldAddBachocoOrganizaciónCompra.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddBachocoOrganizaciónCompra.setLengthMax(35);
        moFieldAddBachocoDivisión = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddBachocoDivisión, jlAddBachocoDivisión);
        moFieldAddBachocoDivisión.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddBachocoDivisión.setLengthMax(35);
        moFieldAddModeloDpsDescripción = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddModeloDpsDescripción, jlAddModeloDpsDescripción);
        moFieldAddModeloDpsDescripción.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddModeloDpsDescripción.setLengthMax(35);
        moFieldAddSorianaTienda = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfAddSorianaTienda, jlAddSorianaTienda);
        moFieldAddSorianaTienda.setDecimalFormat(SLibUtils.DecimalFormatIntegerRaw);
        moFieldAddSorianaTienda.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddSorianaEntregaMercancía = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfAddSorianaEntregaMercancía, jlAddSorianaEntregaMercancía);
        moFieldAddSorianaEntregaMercancía.setDecimalFormat(SLibUtils.DecimalFormatIntegerRaw);
        moFieldAddSorianaEntregaMercancía.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddSorianaRemisiónFecha = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftAddSorianaRemisiónFecha, jlAddSorianaRemisiónFecha);
        moFieldAddSorianaRemisiónFecha.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddSorianaRemisiónFolio = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddSorianaRemisiónFolio, jlAddSorianaRemisiónFolio);
        moFieldAddSorianaRemisiónFolio.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddSorianaRemisiónFolio.setLengthMax(25);
        moFieldAddSorianaPedidoFolio = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddSorianaPedidoFolio, jlAddSorianaPedidoFolio);
        moFieldAddSorianaPedidoFolio.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddSorianaPedidoFolio.setLengthMax(25);
        moFieldAddSorianaBultoTipo = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfAddSorianaBultoTipo, jlAddSorianaBultoTipo);
        moFieldAddSorianaBultoTipo.setDecimalFormat(SLibUtils.DecimalFormatIntegerRaw);
        moFieldAddSorianaBultoTipo.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddSorianaBultoCantidad = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfAddSorianaBultoCantidad, jlAddSorianaBultoCantidad);
        moFieldAddSorianaBultoCantidad.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddSorianaNotaEntradaFolio = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddSorianaNotaEntradaFolio, jlAddSorianaNotaEntradaFolio);
        moFieldAddSorianaNotaEntradaFolio.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddSorianaNotaEntradaFolio.setLengthMax(25);
        moFieldAddSorianaCita = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddSorianaCita, jlAddSorianaCita);
        moFieldAddSorianaCita.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddSorianaCita.setLengthMax(10);
        moFieldAddAmc71SupplierGln = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, (JTextField) jcbAddAmc71SupplierGln.getEditor().getEditorComponent(), jlAddAmc71SupplierGln);
        moFieldAddAmc71SupplierGln.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        //moFieldAddAmc71SupplierGln.setLengthMax(?); // unlimited length
        moFieldAddAmc71SupplierNumber = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddAmc71SupplierNumber, jlAddAmc71SupplierNumber);
        moFieldAddAmc71SupplierNumber.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        //moFieldAddAmc71SupplierNumber.setLengthMax(?); // unlimited length
        moFieldAddAmc71CompanyGln = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, (JTextField) jcbAddAmc71CompanyGln.getEditor().getEditorComponent(), jlAddAmc71CompanyGln);
        moFieldAddAmc71CompanyGln.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        //moFieldAddAmc71CompanyGln.setLengthMax(?); // unlimited length
        moFieldAddAmc71CompanyContact = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddAmc71CompanyContact, jlAddAmc71CompanyContact);
        moFieldAddAmc71CompanyContact.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        //moFieldAddAmc71CompanyContact.setLengthMax(?); // unlimited length
        moFieldAddAmc71CompanyBranchGln = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, (JTextField) jcbAddAmc71CompanyBranchGln.getEditor().getEditorComponent(), jlAddAmc71CompanyBranchGln);
        moFieldAddAmc71CompanyBranchGln.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        //moFieldAddAmc71CompanyBranchGln.setLengthMax(?); // unlimited length
        moFieldAddAmc71ShipToName = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddAmc71ShipToName, jlAddAmc71ShipToName);
        moFieldAddAmc71ShipToName.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        //moFieldAddAmc71ShipToName.setLengthMax(?); // unlimited length
        moFieldAddAmc71ShipToAddress = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddAmc71ShipToAddress, jlAddAmc71ShipToAddress);
        moFieldAddAmc71ShipToAddress.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        //moFieldAddAmc71ShipToAddress.setLengthMax(?); // unlimited length
        moFieldAddAmc71ShipToCity = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddAmc71ShipToCity, jlAddAmc71ShipToCity);
        moFieldAddAmc71ShipToCity.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        //moFieldAddAmc71ShipToCity.setLengthMax(?); // unlimited length
        moFieldAddAmc71ShipToPostalCode = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddAmc71ShipToPostalCode, jlAddAmc71ShipToPostalCode);
        moFieldAddAmc71ShipToPostalCode.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        //moFieldAddAmc71ShipToPostalCode.setLengthMax(?); // unlimited length
        
        moFieldCfdiXmlFile = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfFileXml, jlFileXml);
        moFieldCfdiXmlFile.setPickerButton(jbLoadFileXml); // maybe this is unnecessary, text field is read-only
        moFieldCfdiXmlFile.setTabbedPaneIndex(TAB_CFD_XML, jTabbedPane);
        moFieldCfdiPdfFile = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfFilePdf, jlFilePdf);
        moFieldCfdiPdfFile.setPickerButton(jbLoadFilePdf); // maybe this is unnecessary, text field is read-only
        moFieldCfdiPdfFile.setTabbedPaneIndex(TAB_CFD_XML, jTabbedPane);
        moFieldCfdiTaxRegimeIss = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbCfdiTaxRegimeIssuing, jlCfdiTaxRegimeIssuing);
        moFieldCfdiTaxRegimeIss.setTabbedPaneIndex(TAB_CFD_XML, jTabbedPane);
        moFieldCfdiTaxRegimeRec = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbCfdiTaxRegimeReceptor, jlCfdiTaxRegimeReceptor);
        moFieldCfdiTaxRegimeRec.setTabbedPaneIndex(TAB_CFD_XML, jTabbedPane);
        moFieldCfdiCfdiUsage = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbCfdiCfdiUsage, jlCfdiCfdiUsage);
        moFieldCfdiCfdiUsage.setTabbedPaneIndex(TAB_CFD_XML, jTabbedPane);
        moFieldCfdiConfirmation = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfCfdiConfirmation, jlCfdiConfirmation);
        moFieldCfdiConfirmation.setTabbedPaneIndex(TAB_CFD_XML, jTabbedPane);
        moFieldCfdiConfirmation.setLengthMin(5);
        moFieldCfdiConfirmation.setLengthMax(5);
        moFieldCfdiBillOfLading = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfBillOfLading, jlBillOfLading);
        moFieldCfdiBillOfLading.setPickerButton(jbLoadBillOfLading); // maybe this is unnecessary, text field is read-only
        moFieldCfdiBillOfLading.setTabbedPaneIndex(TAB_CFD_XML, jTabbedPane);
        moFieldCfdiGblPeriodicity = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbGblPeriodicity, jlGblPeriodicity);
        moFieldCfdiGblPeriodicity.setTabbedPaneIndex(TAB_CFD_XML, jTabbedPane);
        moFieldCfdiGblMonth = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbGblMonth, jlGblMonth);
        moFieldCfdiGblMonth.setTabbedPaneIndex(TAB_CFD_XML, jTabbedPane);
        moFieldCfdiGblYear = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfGblYear, jlGblYear);
        moFieldCfdiGblYear.setTabbedPaneIndex(TAB_CFD_XML, jTabbedPane);
        moFieldCfdiGblYear.setLengthMin(4);
        moFieldCfdiGblYear.setLengthMax(4);
        
        // costomized-accounting fields:
        
        moFieldAccItem = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbAccItem, jlAccItem);
        moFieldAccItem.setTabbedPaneIndex(TAB_ACC, jTabbedPane);
        moFieldAccItem.setPickerButton(jbPickAccItem);
        moFieldAccItemRef_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbAccItemRef_n, jlAccItemRef_n);
        moFieldAccItemRef_n.setTabbedPaneIndex(TAB_ACC, jTabbedPane);
        moFieldAccItemRef_n.setPickerButton(jbPickAccItemRef_n);
        moFieldAccQuantity = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfAccQuantity, jlAccQuantity);
        moFieldAccQuantity.setTabbedPaneIndex(TAB_ACC, jTabbedPane);
        moFieldAccQuantity.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat());
        moFieldAccUnit = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbAccUnit, jlAccUnit);
        moFieldAccUnit.setTabbedPaneIndex(TAB_ACC, jTabbedPane);
        moFieldAccUnit.setPickerButton(jbPickAccUnit);
        moFieldAccIsSubtotalPctApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckAccIsSubtotalPctApplying);
        moFieldAccIsSubtotalPctApplying.setTabbedPaneIndex(TAB_ACC, jTabbedPane);
        moFieldAccSubtotalPct = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfAccSubtotalPct, jckAccIsSubtotalPctApplying);
        moFieldAccSubtotalPct.setTabbedPaneIndex(TAB_ACC, jTabbedPane);
        moFieldAccSubtotalPct.setIsPercent(true);
        moFieldAccSubtotalPct.setDecimalFormat(SLibUtils.DecimalFormatPercentage2D);
        moFieldAccSubtotalCy = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfAccSubtotalCy, jlAccSubtotal);
        moFieldAccSubtotalCy.setTabbedPaneIndex(TAB_ACC, jTabbedPane);
        moFieldAccSubtotalCy.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat());
        moFieldAccConcept = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAccConcept, jlAccConcept);
        moFieldAccConcept.setTabbedPaneIndex(TAB_ACC, jTabbedPane);
        moFieldAccConcept.setLengthMax(100);
        
        mvFields = new Vector<>();
        mvFields.add(moFieldDate);
        mvFields.add(moFieldNumberSeries);
        mvFields.add(moFieldNumber);
        mvFields.add(moFieldNumberReference);
        mvFields.add(moFieldDateDoc);
        mvFields.add(moFieldDaysOfCredit);
        mvFields.add(moFieldDateStartCredit);
        mvFields.add(moFieldDateDocDelivery_n);
        mvFields.add(moFieldDateDocLapsing_n);
        mvFields.add(moFieldFkPaymentTypeId);
        mvFields.add(moFieldCfdiPaymentWay);
        mvFields.add(moFieldCfdiPaymentMethod);
        mvFields.add(moFieldConditionsPayment);
        mvFields.add(moFieldFkLanguajeId);
        mvFields.add(moFieldFkDpsNatureId);
        mvFields.add(moFieldFkFunctionalAreaId);
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
        
        mvFields.add(moFieldDateShipment_n);
        mvFields.add(moFieldDateDelivery_n);
        mvFields.add(moFieldShipments);
        mvFields.add(moFieldIsLinked);
        mvFields.add(moFieldIsClosed);
        mvFields.add(moFieldFkProductionOrderId_n);
        mvFields.add(moFieldFkContactId_n);
        mvFields.add(moFieldFkIncotermId);
        mvFields.add(moFieldFkSpotSrcId_n);
        mvFields.add(moFieldFkSpotDesId_n);
        mvFields.add(moFieldFkModeOfTransportationTypeId);
        mvFields.add(moFieldFkCarrierTypeId);
        mvFields.add(moFieldFkCarrierId_n);
        mvFields.add(moFieldFkVehicleTypeId_n);
        mvFields.add(moFieldFkVehicleId_n);
        mvFields.add(moFieldReqNum);
        mvFields.add(moFieldDriver);
        mvFields.add(moFieldPlate);
        mvFields.add(moFieldTicket);

        mvFields.add(moFieldCfdCceMoveReason);
        mvFields.add(moFieldCfdCceOperationType);
        mvFields.add(moFieldCfdCceRequestKey);
        mvFields.add(moFieldCfdCceExportation);
        mvFields.add(moFieldCfdCceCertificateOrigin);
        mvFields.add(moFieldCfdCceNumberCertificateOrigin);
        mvFields.add(moFieldCfdCceSubdivision);
        mvFields.add(moFieldCfdCceExchangeRateUsd);
        mvFields.add(moFieldCfdCceTotalUsd);
        mvFields.add(moFieldCfdCceBizPartnerAddressee);
        mvFields.add(moFieldCfdCceAddresseeBizPartner);
        mvFields.add(moFieldCfdCceAddresseeBizPartnerBranch);
        mvFields.add(moFieldCfdCceAddresseeBizPartnerBranchAddress);

        mvFields.add(moFieldAddCfdAddendaSubtype);
        mvFields.add(moFieldAddLorealFolioNotaRecepción);
        mvFields.add(moFieldAddBachocoSociedad);
        mvFields.add(moFieldAddBachocoOrganizaciónCompra);
        mvFields.add(moFieldAddBachocoDivisión);
        mvFields.add(moFieldAddModeloDpsDescripción);
        mvFields.add(moFieldAddSorianaTienda);
        mvFields.add(moFieldAddSorianaEntregaMercancía);
        mvFields.add(moFieldAddSorianaRemisiónFecha);
        mvFields.add(moFieldAddSorianaRemisiónFolio);
        mvFields.add(moFieldAddSorianaPedidoFolio);
        mvFields.add(moFieldAddSorianaBultoTipo);
        mvFields.add(moFieldAddSorianaBultoCantidad);
        mvFields.add(moFieldAddSorianaNotaEntradaFolio);
        mvFields.add(moFieldAddSorianaCita);
        mvFields.add(moFieldAddAmc71SupplierGln);
        mvFields.add(moFieldAddAmc71SupplierNumber);
        mvFields.add(moFieldAddAmc71CompanyGln);
        mvFields.add(moFieldAddAmc71CompanyContact);
        mvFields.add(moFieldAddAmc71CompanyBranchGln);
        mvFields.add(moFieldAddAmc71ShipToName);
        mvFields.add(moFieldAddAmc71ShipToAddress);
        mvFields.add(moFieldAddAmc71ShipToCity);
        mvFields.add(moFieldAddAmc71ShipToPostalCode);
        
        mvFields.add(moFieldCfdiXmlFile);
        mvFields.add(moFieldCfdiPdfFile);
        mvFields.add(moFieldCfdiTaxRegimeIss);
        mvFields.add(moFieldCfdiTaxRegimeRec);
        mvFields.add(moFieldCfdiCfdiUsage);
        mvFields.add(moFieldCfdiConfirmation);
        mvFields.add(moFieldCfdiBillOfLading);
        mvFields.add(moFieldCfdiGblPeriodicity);
        mvFields.add(moFieldCfdiGblMonth);
        mvFields.add(moFieldCfdiGblYear);
        
        mvFields.add(moFieldAccItem);
        mvFields.add(moFieldAccItemRef_n);
        mvFields.add(moFieldAccQuantity);
        mvFields.add(moFieldAccUnit);
        mvFields.add(moFieldAccIsSubtotalPctApplying);
        mvFields.add(moFieldAccSubtotalPct);
        mvFields.add(moFieldAccSubtotalCy);
        mvFields.add(moFieldAccConcept);
        
        moComboBoxGroupCfdCceGroupAddressee = new SFormComboBoxGroup(miClient);
        
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
        aoTableColumns = new STableColumnForm[11];
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Notas", 500);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Heredable a todos los documentos", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Visible al imprimir", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Complemento CFDI Leyendas Fiscales", STableConstants.WIDTH_BOOLEAN_2X);
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

        // Pane of costomized accounting entries:

        moPaneGridCustomAcc = new STablePaneGrid(miClient);
        moPaneGridCustomAcc.setDoubleClickAction(this, "publicActionDependentEdit");
        jpCustomAcc.add(moPaneGridCustomAcc, BorderLayout.CENTER);

        i = 0;
        aoTableColumns = new STableColumnForm[11];
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ítem", STableConstants.WIDTH_ITEM_2X);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Concepto", STableConstants.WIDTH_ITEM_2X);
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cantidad", 50);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Subtotal %", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(SGridUtils.CellRendererPercentage2D);
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Subtotal $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "No. centro costo ", STableConstants.WIDTH_ACCOUNT_ID);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Centro costo", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Clave ítem referencia", STableConstants.WIDTH_ITEM_KEY);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ítem referencia", STableConstants.WIDTH_ITEM_2X);

        for (i = 0; i < aoTableColumns.length; i++) {
            moPaneGridCustomAcc.addTableColumn(aoTableColumns[i]);
        }
        
        // Own forms:

        moFormEntry = new SFormDpsEntry(miClient);
        moFormEntryWizard = new SFormDpsEntryWizard(miClient);
        moFormNotes = new SFormDpsNotes(miClient);
        moFormCom = new SFormDpsCom(miClient);
        moDialogPickerDps = null;
        moDialogPickerDpsForLink = new SDialogPickerDps(miClient, SDataConstants.TRNX_DPS_PEND_LINK);
        moDialogPickerDpsForAdjustment = new SDialogPickerDps(miClient, SDataConstants.TRNX_DPS_PEND_ADJ);
        moDialogDpsLink = new SDialogDpsLink(miClient);
        moDialogDpsAdjustment = new SDialogDpsAdjustment(miClient);
        moDialogRecordPicker = new SDialogRecordPicker(miClient, SDataConstants.FINX_REC_USER);
        moDialogShowDocumentLinks = new SDialogShowDocumentLinks(miClient);
        moPanelRecord = new SPanelRecord(miClient);
        jpControlsRecord.add(moPanelRecord);

        // Form parameters:

        manDpsClassKey = null;
        manDpsClassPreviousKey = null;
        moDpsType = null;

        mnParamCurrentUserPrivilegeLevel = SLibConsts.UNDEFINED;
        mbParamIsReadOnly = false;
        moParamDpsSource = null;
        mbIsLocalCurrency = false;
        mbIsImported = false;
        mnDeliveryType = SLibConsts.UNDEFINED;
        mnCfdXmlType = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdTypeXmlTypes().get(SDataConstantsSys.TRNS_TP_CFD_INV);

        try {
            moPanelFkCostCenterId_n = new SPanelAccount(miClient, SDataConstants.FIN_CC, false, false, false);
            moPanelFkCostCenterId_n.setLabelsWidth(100);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        jpCustomAcc112.remove(jlAccDummyCostCenter);
        jpCustomAcc112.add(moPanelFkCostCenterId_n, BorderLayout.NORTH);
        
        // Action listeners:

        jbEdit.addActionListener(this);
        jbEditHelp.addActionListener(this);
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbDate.addActionListener(this);
        jbDateDoc.addActionListener(this);
        jbDateStartCredit.addActionListener(this);
        jbDateMaturity.addActionListener(this);
        jbBizPartnerBalance.addActionListener(this);
        jbRecordManualSelect.addActionListener(this);
        jbRecordManualView.addActionListener(this);
        jbFkCurrencyId.addActionListener(this);
        jbExchangeRate.addActionListener(this);
        jbComputeTotal.addActionListener(this);
        jbPrepayments.addActionListener(this);
        jbEntryNew.addActionListener(this);
        jbEntryCopy.addActionListener(this);
        jbEntryEdit.addActionListener(this);
        jbEntryDelete.addActionListener(this);
        jbEntryDiscountRetailChain.addActionListener(this);
        jbEntryImportFromDps.addActionListener(this);
        jbEntryWizard.addActionListener(this);
        jbEntryViewLinks.addActionListener(this);
        jbEntryImportFromMatRequest.addActionListener(this);
        jbEntryViewMatReqLinks.addActionListener(this);
        jbExportCsv.addActionListener(this);
        jbTaxRegionId.addActionListener(this);
        jbEditTaxRegion.addActionListener(this);
        jbNotesNew.addActionListener(this);
        jbNotesEdit.addActionListener(this);
        jbNotesDelete.addActionListener(this);
        jbPickSystemNotes.addActionListener(this);
        jbEditNotes.addActionListener(this);
        jbDateDocDelivery_n.addActionListener(this);
        jbDateDocLapsing_n.addActionListener(this);
        jbSalesAgent.addActionListener(this);
        jbSalesSupervisor.addActionListener(this);
        jbFkIncotermId.addActionListener(this);
        jbFkModeOfTransportationTypeId.addActionListener(this);
        jbFkCarrierId_n.addActionListener(this);
        jbFkVehicleId_n.addActionListener(this);
        jbEditLogistics.addActionListener(this);
        jbFkProductionOrderId_n.addActionListener(this);
        jbCfdCceCalcTotalUsd.addActionListener(this);
        jbLoadFileXml.addActionListener(this);
        jbDeleteFileXml.addActionListener(this);
        jbLoadFilePdf.addActionListener(this);
        jbDeleteFilePdf.addActionListener(this);
        jbCfdiRelatedDocs.addActionListener(this); 
        jbLoadBillOfLading.addActionListener(this);
        jbDeleteBillOfLading.addActionListener(this);
        jtbEntryFilter.addActionListener(this);
        jtbNotesFilter.addActionListener(this);
        jbSetTime.addActionListener(this);
        jtbSwitchCustomAcc.addActionListener(this);
        jbPickAccItem.addActionListener(this);
        jbPickAccItemRef_n.addActionListener(this);
        jbPickAccUnit.addActionListener(this);
        jbExeWizardAccSubtotal.addActionListener(this);
        jbCustomAccEntryNew.addActionListener(this);
        jbCustomAccEntryCopy.addActionListener(this);
        jbCustomAccEntryEdit.addActionListener(this);
        jbCustomAccEntryDelete.addActionListener(this);
        jbCustomAccEntryOk.addActionListener(this);
        jbCustomAccEntryCancel.addActionListener(this);
        jtbLinkTicket.addActionListener(this);

        // Focus listeners:

        jftDate.addFocusListener(this);
        jftDateDoc.addFocusListener(this);
        jftDateStartCredit.addFocusListener(this);
        jtfDaysOfCredit.addFocusListener(this);
        jtfExchangeRate.addFocusListener(this);
        jtfDiscountDocPercentage.addFocusListener(this);
        jcbAddAmc71SupplierGln.getEditor().getEditorComponent().addFocusListener(this);
        jcbAddAmc71CompanyGln.getEditor().getEditorComponent().addFocusListener(this);
        jcbAddAmc71CompanyBranchGln.getEditor().getEditorComponent().addFocusListener(this);
        jtfAccSubtotalCy.addFocusListener(this);
        jtfAccSubtotalPct.addFocusListener(this);

        // Item listeners:

        jckDateDoc.addItemListener(this);
        jckDateStartCredit.addItemListener(this);
        jckRecordUser.addItemListener(this);
        jckShipments.addItemListener(this);
        jckIsDiscountDocApplying.addItemListener(this);
        jckIsDiscountDocPercentage.addItemListener(this);
        jcbNumberSeries.addItemListener(this);
        jcbFkPaymentTypeId.addItemListener(this);
        jcbFkCurrencyId.addItemListener(this);
        jcbFkIncotermId.addItemListener(this);
        jcbFkCarrierTypeId.addItemListener(this);
        jcbFkVehicleTypeId_n.addItemListener(this);
        jcbAddCfdAddendaSubtype.addItemListener(this);
        jcbAddAmc71SupplierGln.addItemListener(this);
        jcbAddAmc71CompanyGln.addItemListener(this);
        jcbAddAmc71CompanyBranchGln.addItemListener(this);
        jcbAccItem.addItemListener(this);
        jckAccIsSubtotalPctApplying.addItemListener(this);
        
        // Change listeners:
        
        jTabbedPane.addChangeListener(this);
                
        // Action map:

        SFormUtilities.createActionMap(rootPane, this, "publicActionDependentNew", "dependentNew", KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionDependentEdit", "dependentEdit", KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionDependentDelete", "dependentDelete", KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionDependentFilter", "dependentFilter", KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryDiscountRetailChain", "entryDiscountRetailChain", KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryImportFromDps", "entryImport", KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryWizard", "entryWizard", KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryViewLinks", "entryViewLinks", KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionExportCsv", "exportCsv", KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK);

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
        
        mbIsDpsTimeReq = mnFormType == SDataConstantsSys.TRNS_CT_DPS_SAL;
    }
    
    public void actionExportCsv() {
        if (jbExportCsv.isEnabled()) {
            STableUtilities.actionExportCsv(miClient, moPaneGridEntries, getTitle());
        }
    }
    
    private void windowActivated() {
        boolean goAhead = true;
        
        mbDocBeingImported = false;
        
        if (mbFirstTime) {
            mbFirstTime = false;

            if (!moDps.getIsRegistryNew()) {
                if (!moDps.getIsRecordAutomatic() && moRecordUserSLock == null) {
                    miClient.showMsgBoxWarning("La póliza contable manual no debe estar siendo utilizada por otro usuario.");
                    actionCancel();     // if lock for manual record could not be gained, exit form
                }
                else {
                    jbCancel.requestFocus();
                }
            }
            else {
                if (miClient.getSessionXXX().getCurrentCompanyBranchId() == 0) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_SESSION_BRANCH);   // no company branch selected
                    mbFormSettingsOk = false;
                    actionCancel();
                }
                else if (mbIsNumberSeriesRequired && !mbIsNumberSeriesAvailable) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_CFG_DNS);  // number series required, but no number series available
                    mbFormSettingsOk = false;
                    actionCancel();
                }
                else {
                    if (moParamDpsSource == null) {
                        // new document from scratch:

                        if (moDpsType == null) {
                            pickDpsType();    // choose DPS type
                            if (moDpsType == null) {
                                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDpsType.getText() + "'.");
                                mbFormSettingsOk = false;
                                actionCancel();
                            }
                        } 

                        if (moBizPartner == null) {
                            pickBizPartner(); // choose business partner
                            if (moBizPartner == null) {
                                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlBizPartner.getText() + "'.");
                                mbFormSettingsOk = false;
                                actionCancel();
                            }
                            else {
                                // check business partner:
                                
                                if (isBizPartnerBlocked(moBizPartner.getPkBizPartnerId())) {
                                    miClient.showMsgBoxWarning(SLibConstants.MSG_INF_BP_BLOCKED);
                                    mbFormSettingsOk = goAhead = false;
                                    actionCancel();
                                }
                            }
                        }
                    }
                    else {
                        // new document from some previous document:

                        // check business partner:
                                
                        if (isBizPartnerBlocked(moParamDpsSource.getFkBizPartnerId_r())) {
                            miClient.showMsgBoxWarning(SLibConstants.MSG_INF_BP_BLOCKED);
                            mbFormSettingsOk = goAhead = false;
                            moParamDpsSource = null;
                            actionCancel();
                        }

                        // import data from previous document:
                              
                        try {
                            SDataDps dpsModel;
                            
                            if (! mbMatRequestImport) {
                                dpsModel = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moParamDpsSource.getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
                            }
                            else {
                                dpsModel = moParamDpsSource;
                                if (dpsModel.getFkBizPartnerId_r() == 0) {
                                    pickBizPartner();
                                    if (moPickerBizPartner.getFormResult() != SLibConstants.FORM_RESULT_OK) {
                                        releaseRecordUserSLock();
                                        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
                                        setVisible(false);
                                        return;
                                    }
                                }
                            }

                            if (!STrnDpsUtilities.isDpsAuthorized(miClient, dpsModel)) {
                                mbFormSettingsOk = goAhead = false;
                                actionCancel();
                            }
                            else {
                                mbDocBeingImported = true;
                                
                                SDataDps dps = createNewDps(dpsModel);
                                if (! mbMatRequestImport) {
                                    dps.getDbmsDpsEntries().clear();
                                }

                                for (SDataDpsNotes notes : dps.getDbmsDpsNotes()) {
                                    notes.setIsRegistryEdited(true);    // force original document notes to be attached to new document even if they are not edited
                                    notes.setPkNotesId(0);
                                }

                                setRegistry(dps);
                                actionEdit();

                                if (manParamAdjustmentSubtypeKey != null) {
                                    SFormUtilities.locateComboBoxItem(jcbAdjustmentSubtypeId, manParamAdjustmentSubtypeKey);
                                }
                                
                                if (! mbMatRequestImport) {
                                    actionEntryImportFromDps(moParamDpsSource);
                                }
                            }
                        }
                        catch (Exception e) {
                            Logger.getLogger(SFormDps.class.getName()).log(Level.SEVERE, null, e);
                            SLibUtilities.renderException(this, e);
                        }
                        finally {
                            moParamDpsSource = null;                // no longer needed
                            manParamAdjustmentSubtypeKey = null;    // no longer needed
                        }
                    }
                    
                    // check business partner credit:
                    
                    if (moBizPartnerCategory != null) {
                        if (!isBizPartnerCreditOk(moBizPartnerCategory.getEffectiveRiskTypeId(), true)) {
                            mbFormSettingsOk = goAhead = false;
                            actionCancel();
                        }
                    }

                    if (goAhead) {
                        try {
                            obtainNextNumber();
                            updateDpsWithCurrentFormData();
                            adequateDatesForOrderPrevious();
                            addSystemNotes();
                        }
                        catch (Exception e) {
                            SLibUtilities.renderException(this, e);
                        }
                        
                        if (jftDate.isEditable()) {
                            jftDate.requestFocus();
                        }
                        else {
                            jtfNumber.requestFocus();
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Checks if current business partner, if any, is an international one.
     * @return <code>true</code> if current business partner is an international one.
     */
    private boolean isBizPartnerInt() {
        return moBizPartner != null && !moBizPartner.isDomestic(miClient);
    }
    
    /**
     * Checks if current currency is USD.
     * @return 
     */
    private boolean isCurrencyUsd() {
        return jcbFkCurrencyId.getSelectedIndex() > 0 && moFieldFkCurrencyId.getKeyAsIntArray()[0] == SModSysConsts.CFGU_CUR_USD;
    }
    
    /**
     * Enables/disables tabs and fields for CFD.
     * Called within methods updateDpsFieldsStatus() and obtainNextNumber().
     * @param enable Desired status for tabs and fields.
     */
    private void enableCfdFields(boolean enable) {
        boolean isCfdEmissionRequired = isCfdEmissionRequired();
        
        jTabbedPane.setEnabledAt(TAB_CFD_INT_COM, isCfdIntCommerceRequired());
        jTabbedPane.setEnabledAt(TAB_CFD_ADD, isCfdAddendaRequired());
        jTabbedPane.setEnabledAt(TAB_CFD_XML, isCfdEmissionRequired || isCfdXmlFilePermitted());   // enable aswell XML file for expenses documents
        jcbCfdiPaymentWay.setEnabled(enable && isCfdEmissionRequired);
        jcbCfdiPaymentMethod.setEnabled(enable && isCfdEmissionRequired);
        
        enableCfdXmlFields(enable);
        enableCfdCceFields(enable);
        enableCfdAddendaFields(enable);
    }
                    
    /**
     * Enables/disables fields for CFD data.
     * @param enableFields Desired status for fields.
     */
    private void enableCfdXmlFields(boolean enable) {
        boolean enableFields = enable && isCfdEmissionRequired();
        boolean enableXmlFields = enable && isCfdXmlFilePermitted();
        boolean isCfdi33or40 = mnCfdXmlType == SDataConstantsSys.TRNS_TP_XML_CFDI_33 || mnCfdXmlType == SDataConstantsSys.TRNS_TP_XML_CFDI_40;
        
        jlFileXml.setEnabled(enableXmlFields);
        jbLoadFileXml.setEnabled(enableXmlFields);
        jbDeleteFileXml.setEnabled(enableXmlFields);
        jckValidateOnSaveFileXml.setEnabled(false);
        jlFilePdf.setEnabled(enableXmlFields);
        jbLoadFilePdf.setEnabled(enableXmlFields);
        jbDeleteFilePdf.setEnabled(enableXmlFields);
        
        jcbCfdiTaxRegimeIssuing.setEnabled(enableFields && isCfdi33or40);
        jcbCfdiTaxRegimeReceptor.setEnabled(enableFields && isCfdi33or40);
        jcbCfdiCfdiUsage.setEnabled(enableFields && isCfdi33or40);
        jtfCfdiConfirmation.setEnabled(enableFields && isCfdi33or40);
        jtfCfdiConfirmation.setFocusable(enableFields && isCfdi33or40);
        jbCfdiRelatedDocs.setEnabled(enableFields && isCfdi33or40);
        jcbGblPeriodicity.setEnabled(enableFields && isCfdi33or40);
        jcbGblMonth.setEnabled(enableFields && isCfdi33or40);
        jtfGblYear.setEnabled(enableFields && isCfdi33or40);
        jtfGblYear.setFocusable(enableFields && isCfdi33or40);
        
        jbLoadBillOfLading.setEnabled(enableFields && isCfdi33or40);
        jbDeleteBillOfLading.setEnabled(enableFields && isCfdi33or40);
    }

    /**
     * Enables/disables fields for CFD International Commerce data.
     * @param enable Desired status for fields.
     */
    private void enableCfdCceFields(boolean enable) {
        boolean enableFields = enable && isCfdIntCommerceRequired();
        boolean isCurrencyUsd = isCurrencyUsd();
        
        jckCfdCceApplies.setEnabled(enableFields);
        jckCfdCceApplies.setSelected(enableFields);     // selection does not belongs to here, please move this to other better place ASAP!!!
        //jcbCfdCceMoveReason.setEnabled(enableFields); // field stays disabled, required only for CFDI "T", but not supported yet!
        jcbCfdCceOperationType.setEnabled(enableFields);
        jcbCfdCceRequestKey.setEnabled(enableFields);
        jcbExportation.setEnabled(enableFields);
        jtfCfdCceCertificateOrigin.setEnabled(enableFields);
        jtfCfdCceCertificateOrigin.setFocusable(enableFields);
        jtfCfdCceNumberCertificateOrigin.setEnabled(enableFields);
        jtfCfdCceNumberCertificateOrigin.setFocusable(enableFields);
        jtfCfdCceSubdivision.setEnabled(enableFields);
        jtfCfdCceSubdivision.setFocusable(enableFields);
        jtfCfdCceExchangeRateUsd.setEnabled(enableFields && !isCurrencyUsd);
        jtfCfdCceExchangeRateUsd.setFocusable(enableFields && !isCurrencyUsd);
        jtfCfdCceTotalUsd.setEnabled(enableFields && !isCurrencyUsd);
        jtfCfdCceTotalUsd.setFocusable(enableFields && !isCurrencyUsd);
        jbCfdCceCalcTotalUsd.setEnabled(enableFields && !isCurrencyUsd);
        jcbCfdCceFkBizPartnerAddressee.setEnabled(enableFields);
        jcbCfdCceFkAddresseeBizPartner.setEnabled(enableFields);
        jcbCfdCceFkAddresseeBizPartnerBranch.setEnabled(enableFields && jcbCfdCceFkAddresseeBizPartnerBranch.getSelectedIndex() > 0);
        jcbCfdCceFkAddresseeBizPartnerBranchAddress.setEnabled(enableFields && jcbCfdCceFkAddresseeBizPartnerBranchAddress.getSelectedIndex() > 0);
    }
    
    private void enableCfdAddendaFields(boolean enable) {
        boolean enableFields = enable && isCfdEmissionRequired();
        
        if (moBizPartner != null) {
            jtfAddCfdAddendaType.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRN_DPS_ADD, new int[] { moBizPartnerCategory.getFkCfdAddendaTypeId() }));
            
            switch (moBizPartnerCategory.getFkCfdAddendaTypeId()) {
                case SDataConstantsSys.BPSS_TP_CFD_ADD_NA:
                    // addenda is not required
                    break;
                    
                case SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA:
                    jlAddCfdAddendaSubtype.setEnabled(enableFields);
                    jcbAddCfdAddendaSubtype.setEnabled(enableFields);
                    jlAddSorianaTienda.setEnabled(enableFields);
                    jtfAddSorianaTienda.setEnabled(enableFields);
                    jlAddSorianaEntregaMercancía.setEnabled(enableFields);
                    jtfAddSorianaEntregaMercancía.setEnabled(enableFields);
                    jlAddSorianaRemisiónFecha.setEnabled(enableFields);
                    jftAddSorianaRemisiónFecha.setEnabled(enableFields);
                    jlAddSorianaRemisiónFolio.setEnabled(enableFields);
                    jtfAddSorianaRemisiónFolio.setEnabled(enableFields);
                    jlAddSorianaPedidoFolio.setEnabled(enableFields);
                    jtfAddSorianaPedidoFolio.setEnabled(enableFields);
                    jlAddSorianaBultoTipo.setEnabled(enableFields);
                    jtfAddSorianaBultoTipo.setEnabled(enableFields);
                    jlAddSorianaBultoCantidad.setEnabled(enableFields);
                    jtfAddSorianaBultoCantidad.setEnabled(enableFields);
                    jlAddSorianaNotaEntradaFolio.setEnabled(enableFields);
                    jtfAddSorianaNotaEntradaFolio.setEnabled(enableFields);
                    jlAddSorianaCita.setEnabled(enableFields);
                    jtfAddSorianaCita.setEnabled(enableFields);
                    
                    jcbAddCfdAddendaSubtype.removeAllItems();
                    jcbAddCfdAddendaSubtype.addItem(new SFormComponentItem(new int[] { }, "(Seleccionar subtipo addenda)"));
                    jcbAddCfdAddendaSubtype.addItem(new SFormComponentItem(new int[] { SCfdConsts.ADD_SORIANA_NOR }, "Normal"));
                    jcbAddCfdAddendaSubtype.addItem(new SFormComponentItem(new int[] { SCfdConsts.ADD_SORIANA_EXT }, "Extemporánea"));
                    
                    SFormUtilities.locateComboBoxItem(jcbAddCfdAddendaSubtype, new int[] { (moDps.getDbmsDataAddenda() != null ? moDps.getDbmsDataAddenda().getCfdAddendaSubtype() : SCfdConsts.ADD_SORIANA_NOR) });
                    itemStateChangedAddCfdAddendaSubtype();
                    break;
                    
                case SDataConstantsSys.BPSS_TP_CFD_ADD_LOREAL:
                    jlAddLorealFolioNotaRecepción.setEnabled(enableFields);
                    jtfAddLorealFolioNotaRecepción.setEnabled(enableFields);
                    break;
                    
                case SDataConstantsSys.BPSS_TP_CFD_ADD_BACHOCO:
                    jlAddBachocoSociedad.setEnabled(enableFields);
                    jtfAddBachocoSociedad.setEnabled(enableFields);
                    jlAddBachocoOrganizaciónCompra.setEnabled(enableFields);
                    jtfAddBachocoOrganizaciónCompra.setEnabled(enableFields);
                    jlAddBachocoDivisión.setEnabled(enableFields);
                    jtfAddBachocoDivisión.setEnabled(enableFields);
                    break;
                    
                case SDataConstantsSys.BPSS_TP_CFD_ADD_MODELO:
                    jlAddBachocoSociedad.setEnabled(enableFields);
                    jtfAddBachocoSociedad.setEnabled(enableFields);
                    jlAddModeloDpsDescripción.setEnabled(enableFields);
                    jtfAddModeloDpsDescripción.setEnabled(enableFields);
                    break;
                    
                case SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA:
                    // this addenda does not have any field as header
                    break;
                    
                case SDataConstantsSys.BPSS_TP_CFD_ADD_WALDOS:
                    jlAddSorianaPedidoFolio.setEnabled(enableFields);
                    jtfAddSorianaPedidoFolio.setEnabled(enableFields);
                    break;
                    
                case SDataConstantsSys.BPSS_TP_CFD_ADD_AMECE71:
                    jlAddAmc71SupplierGln.setEnabled(enableFields);
                    jcbAddAmc71SupplierGln.setEnabled(enableFields);
                    jlAddAmc71SupplierNumber.setEnabled(enableFields);
                    jtfAddAmc71SupplierNumber.setEnabled(enableFields);
                    jlAddAmc71CompanyGln.setEnabled(enableFields);
                    jcbAddAmc71CompanyGln.setEnabled(enableFields);
                    jlAddAmc71CompanyContact.setEnabled(enableFields);
                    jtfAddAmc71CompanyContact.setEnabled(enableFields);
                    jlAddAmc71CompanyBranchGln.setEnabled(enableFields);
                    jcbAddAmc71CompanyBranchGln.setEnabled(enableFields);
                    jlAddAmc71ShipToName.setEnabled(enableFields);
                    jtfAddAmc71ShipToName.setEnabled(enableFields);
                    jlAddAmc71ShipToAddress.setEnabled(enableFields);
                    jtfAddAmc71ShipToAddress.setEnabled(enableFields);
                    jlAddAmc71ShipToCity.setEnabled(enableFields);
                    jtfAddAmc71ShipToCity.setEnabled(enableFields);
                    jlAddAmc71ShipToPostalCode.setEnabled(enableFields);
                    jtfAddAmc71ShipToPostalCode.setEnabled(enableFields);
                    break;
                    
                default:
            }
        }
    }

    private void disableCfdAddendaFields() {
        jlAddCfdAddendaSubtype.setEnabled(false);
        jcbAddCfdAddendaSubtype.setEnabled(false);
        jlAddSorianaTienda.setEnabled(false);
        jtfAddSorianaTienda.setEnabled(false);
        jlAddSorianaEntregaMercancía.setEnabled(false);
        jtfAddSorianaEntregaMercancía.setEnabled(false);
        jlAddSorianaRemisiónFecha.setEnabled(false);
        jftAddSorianaRemisiónFecha.setEnabled(false);
        jlAddSorianaRemisiónFolio.setEnabled(false);
        jtfAddSorianaRemisiónFolio.setEnabled(false);
        jlAddSorianaPedidoFolio.setEnabled(false);
        jtfAddSorianaPedidoFolio.setEnabled(false);
        jlAddSorianaBultoTipo.setEnabled(false);
        jtfAddSorianaBultoTipo.setEnabled(false);
        jlAddSorianaBultoCantidad.setEnabled(false);
        jtfAddSorianaBultoCantidad.setEnabled(false);
        jlAddSorianaNotaEntradaFolio.setEnabled(false);
        jtfAddSorianaNotaEntradaFolio.setEnabled(false);
        jlAddSorianaCita.setEnabled(false);
        jtfAddSorianaCita.setEnabled(false);
        
        jlAddLorealFolioNotaRecepción.setEnabled(false);
        jtfAddLorealFolioNotaRecepción.setEnabled(false);
        
        jlAddBachocoSociedad.setEnabled(false);
        jtfAddBachocoSociedad.setEnabled(false);
        jlAddBachocoOrganizaciónCompra.setEnabled(false);
        jtfAddBachocoOrganizaciónCompra.setEnabled(false);
        jlAddBachocoDivisión.setEnabled(false);
        jtfAddBachocoDivisión.setEnabled(false);
        
        jlAddModeloDpsDescripción.setEnabled(false);
        jtfAddModeloDpsDescripción.setEnabled(false);
        
        jlAddAmc71SupplierGln.setEnabled(false);
        jcbAddAmc71SupplierGln.setEnabled(false);
        jlAddAmc71SupplierNumber.setEnabled(false);
        jtfAddAmc71SupplierNumber.setEnabled(false);
        jlAddAmc71CompanyGln.setEnabled(false);
        jcbAddAmc71CompanyGln.setEnabled(false);
        jlAddAmc71CompanyContact.setEnabled(false);
        jtfAddAmc71CompanyContact.setEnabled(false);
        jlAddAmc71CompanyBranchGln.setEnabled(false);
        jcbAddAmc71CompanyBranchGln.setEnabled(false);
        jlAddAmc71ShipToName.setEnabled(false);
        jtfAddAmc71ShipToName.setEnabled(false);
        jlAddAmc71ShipToAddress.setEnabled(false);
        jtfAddAmc71ShipToAddress.setEnabled(false);
        jlAddAmc71ShipToCity.setEnabled(false);
        jtfAddAmc71ShipToCity.setEnabled(false);
        jlAddAmc71ShipToPostalCode.setEnabled(false);
        jtfAddAmc71ShipToPostalCode.setEnabled(false);
    }
    
    private void setAddendaData() {
        jtfAddCfdAddendaType.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRN_DPS_ADD, new int[] { moBizPartnerCategory.getFkCfdAddendaTypeId() }));

        switch(moBizPartnerCategory.getFkCfdAddendaTypeId()) {
            case SDataConstantsSys.BPSS_TP_CFD_ADD_NA:
                // addenda is not required
                break;

            case SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA:
                int addendaSubtype = 0;
                int sorianaTienda = 0;
                int sorianaEntregaMercancía = 0;
                Date sorianaRemisionFecha = null;
                String sorianaRemisionFolio = "";
                String sorianaPedidoFolio = "";
                int sorianaBultoTipo = 0;
                double sorianaBultoCantidad = 0;
                String sorianaFolioNotaEntradaFolio = "";
                String sorianaCita = "";
                    
                if (moDps.getDbmsDataAddenda() != null) {
                    addendaSubtype = moDps.getDbmsDataAddenda().getCfdAddendaSubtype();
                    sorianaTienda = moDps.getDbmsDataAddenda().getSorianaTienda();
                    sorianaEntregaMercancía = moDps.getDbmsDataAddenda().getSorianaEntregaMercancia();
                    sorianaRemisionFecha = moDps.getDbmsDataAddenda().getSorianaRemisionFecha();
                    sorianaRemisionFolio = moDps.getDbmsDataAddenda().getSorianaRemisionFolio();
                    sorianaPedidoFolio = moDps.getDbmsDataAddenda().getSorianaPedidoFolio();
                    sorianaBultoTipo = moDps.getDbmsDataAddenda().getSorianaBultoTipo();
                    sorianaBultoCantidad = moDps.getDbmsDataAddenda().getSorianaBultoCantidad();
                    sorianaFolioNotaEntradaFolio = moDps.getDbmsDataAddenda().getSorianaNotaEntradaFolio();
                    sorianaCita = moDps.getDbmsDataAddenda().getSorianaCita();
                }
                
                moFieldAddCfdAddendaSubtype.setFieldValue(new int[] { addendaSubtype });
                moFieldAddSorianaTienda.setFieldValue(sorianaTienda);
                moFieldAddSorianaEntregaMercancía.setFieldValue(sorianaEntregaMercancía);
                moFieldAddSorianaRemisiónFecha.setFieldValue(sorianaRemisionFecha);
                moFieldAddSorianaRemisiónFolio.setFieldValue(sorianaRemisionFolio);
                moFieldAddSorianaPedidoFolio.setFieldValue(sorianaPedidoFolio);
                moFieldAddSorianaBultoTipo.setFieldValue(sorianaBultoTipo);
                moFieldAddSorianaBultoCantidad.setFieldValue(sorianaBultoCantidad);
                moFieldAddSorianaNotaEntradaFolio.setFieldValue(sorianaFolioNotaEntradaFolio);
                moFieldAddSorianaCita.setFieldValue(sorianaCita);
                
                jlAddCfdAddendaSubtype.setEnabled(true);
                jcbAddCfdAddendaSubtype.setEnabled(false);
                jlAddSorianaTienda.setEnabled(true);
                jtfAddSorianaTienda.setEnabled(false);
                jlAddSorianaEntregaMercancía.setEnabled(true);
                jtfAddSorianaEntregaMercancía.setEnabled(false);
                jlAddSorianaRemisiónFecha.setEnabled(true);
                jftAddSorianaRemisiónFecha.setEnabled(false);
                jlAddSorianaRemisiónFolio.setEnabled(true);
                jtfAddSorianaRemisiónFolio.setEnabled(false);
                jlAddSorianaPedidoFolio.setEnabled(true);
                jtfAddSorianaPedidoFolio.setEnabled(false);
                jlAddSorianaBultoTipo.setEnabled(true);
                jtfAddSorianaBultoTipo.setEnabled(false);
                jlAddSorianaBultoCantidad.setEnabled(true);
                jtfAddSorianaBultoCantidad.setEnabled(false);
                jlAddSorianaNotaEntradaFolio.setEnabled(true);
                jtfAddSorianaNotaEntradaFolio.setEnabled(false);
                jlAddSorianaCita.setEnabled(true);
                jtfAddSorianaCita.setEnabled(false);
                break;

            case SDataConstantsSys.BPSS_TP_CFD_ADD_LOREAL:
                String folioNotaRecepción = "";
                
                if (moDps.getDbmsDataAddenda() != null) {
                    folioNotaRecepción = moDps.getDbmsDataAddenda().getLorealFolioNotaRecepcion();
                }
                
                moFieldAddLorealFolioNotaRecepción.setFieldValue(folioNotaRecepción);
                
                jlAddLorealFolioNotaRecepción.setEnabled(true);
                jtfAddLorealFolioNotaRecepción.setEnabled(false);
                break;

            case SDataConstantsSys.BPSS_TP_CFD_ADD_BACHOCO:
                String bachocoSociedad = "";
                String bachocoOrganizaciónCompra = "";
                String bachocoDivisión = "";
                
                if (moDps.getDbmsDataAddenda() != null) {
                    bachocoSociedad = moDps.getDbmsDataAddenda().getBachocoSociedad();
                    bachocoOrganizaciónCompra = moDps.getDbmsDataAddenda().getBachocoOrganizacionCompra();
                    bachocoDivisión = moDps.getDbmsDataAddenda().getBachocoDivision();
                }
                
                moFieldAddBachocoSociedad.setFieldValue(bachocoSociedad);
                moFieldAddBachocoOrganizaciónCompra.setFieldValue(bachocoOrganizaciónCompra);
                moFieldAddBachocoDivisión.setFieldValue(bachocoDivisión);
                
                jlAddBachocoSociedad.setEnabled(true);
                jtfAddBachocoSociedad.setEnabled(false);
                jlAddBachocoOrganizaciónCompra.setEnabled(true);
                jtfAddBachocoOrganizaciónCompra.setEnabled(false);
                jlAddBachocoDivisión.setEnabled(true);
                jtfAddBachocoDivisión.setEnabled(false);
                break;

            case SDataConstantsSys.BPSS_TP_CFD_ADD_MODELO:
                String modeloSociedad = "";
                String modeloDpsDescripción = "";
                
                if (moDps.getDbmsDataAddenda() != null) {
                    modeloSociedad = moDps.getDbmsDataAddenda().getBachocoSociedad();
                    modeloDpsDescripción = moDps.getDbmsDataAddenda().getModeloDpsDescripcion();
                }
                
                moFieldAddBachocoSociedad.setFieldValue(modeloSociedad);
                moFieldAddModeloDpsDescripción.setFieldValue(modeloDpsDescripción);
                
                jlAddBachocoSociedad.setEnabled(true);
                jtfAddBachocoSociedad.setEnabled(false);
                jlAddModeloDpsDescripción.setEnabled(true);
                jtfAddModeloDpsDescripción.setEnabled(false);
                break;

            case SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA:
                // this addenda does not have any field as header
                break;
            
            case SDataConstantsSys.BPSS_TP_CFD_ADD_WALDOS:
                // this addenda does not have any field as header
                break;
                    
            case SDataConstantsSys.BPSS_TP_CFD_ADD_AMECE71:
                String amc71SupplierGln = "";
                String amc71SupplierNumber = "";
                String amc71CompanyGln = "";
                String amc71CompanyContact = "";
                String amc71CompanyBranchGln = "";
                String amc71ShipToName = "";
                String amc71ShipToAddress = "";
                String amc71ShipToCity = "";
                String amc71ShipToPostalCode = "";
                
                if (moDps.getDbmsDataAddenda() != null) {
                    try {
                        SAddendaAmc71XmlHeader amc71XmlHeader = new SAddendaAmc71XmlHeader();
                        amc71XmlHeader.decodeJson(moDps.getDbmsDataAddenda().getJsonData());
                        
                        amc71SupplierGln = amc71XmlHeader.Supplier.SupplierGln;
                        amc71SupplierNumber = amc71XmlHeader.Supplier.SupplierNumber;
                        amc71CompanyGln = amc71XmlHeader.Company.CompanyGln;
                        amc71CompanyContact = amc71XmlHeader.Company.CompanyContact;
                        amc71CompanyBranchGln = amc71XmlHeader.CompanyBranch.CompanyBranchGln;
                        amc71ShipToName = amc71XmlHeader.CompanyBranch.ShipToName;
                        amc71ShipToAddress = amc71XmlHeader.CompanyBranch.ShipToAddress;
                        amc71ShipToCity = amc71XmlHeader.CompanyBranch.ShipToCity;
                        amc71ShipToPostalCode = amc71XmlHeader.CompanyBranch.ShipToPostalCode;
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                }
                
                moFieldAddAmc71SupplierGln.setFieldValue(amc71SupplierGln);
                moFieldAddAmc71SupplierNumber.setFieldValue(amc71SupplierNumber);
                moFieldAddAmc71CompanyGln.setFieldValue(amc71CompanyGln);
                moFieldAddAmc71CompanyContact.setFieldValue(amc71CompanyContact);
                moFieldAddAmc71CompanyBranchGln.setFieldValue(amc71CompanyBranchGln);
                moFieldAddAmc71ShipToName.setFieldValue(amc71ShipToName);
                moFieldAddAmc71ShipToAddress.setFieldValue(amc71ShipToAddress);
                moFieldAddAmc71ShipToCity.setFieldValue(amc71ShipToCity);
                moFieldAddAmc71ShipToPostalCode.setFieldValue(amc71ShipToPostalCode);
                
                jlAddAmc71SupplierGln.setEnabled(true);
                jcbAddAmc71SupplierGln.setEnabled(false);
                jlAddAmc71SupplierNumber.setEnabled(true);
                jtfAddAmc71SupplierNumber.setEnabled(false);
                jlAddAmc71CompanyGln.setEnabled(true);
                jcbAddAmc71CompanyGln.setEnabled(false);
                jlAddAmc71CompanyContact.setEnabled(true);
                jtfAddAmc71CompanyContact.setEnabled(false);
                jlAddAmc71CompanyBranchGln.setEnabled(true);
                jcbAddAmc71CompanyBranchGln.setEnabled(false);
                jlAddAmc71ShipToName.setEnabled(true);
                jtfAddAmc71ShipToName.setEnabled(false);
                jlAddAmc71ShipToAddress.setEnabled(true);
                jtfAddAmc71ShipToAddress.setEnabled(false);
                jlAddAmc71ShipToCity.setEnabled(true);
                jtfAddAmc71ShipToCity.setEnabled(false);
                jlAddAmc71ShipToPostalCode.setEnabled(true);
                jtfAddAmc71ShipToPostalCode.setEnabled(false);
                break;
                
            default:
        }
    }
    
    private java.lang.String composeAddendaJsonData() {
        String json = "";
        
        switch(moBizPartnerCategory.getFkCfdAddendaTypeId()) {
            case SDataConstantsSys.BPSS_TP_CFD_ADD_NA:
            case SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA:
            case SDataConstantsSys.BPSS_TP_CFD_ADD_LOREAL:
            case SDataConstantsSys.BPSS_TP_CFD_ADD_BACHOCO:
            case SDataConstantsSys.BPSS_TP_CFD_ADD_MODELO:
            case SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA:
                break;
                
            case SDataConstantsSys.BPSS_TP_CFD_ADD_AMECE71:
                SAddendaAmc71XmlHeader amc71XmlHeader = new SAddendaAmc71XmlHeader();
                amc71XmlHeader.Supplier.SupplierGln = moFieldAddAmc71SupplierGln.getString();
                amc71XmlHeader.Supplier.SupplierNumber = moFieldAddAmc71SupplierNumber.getString();
                amc71XmlHeader.Company.CompanyGln = moFieldAddAmc71CompanyGln.getString();
                amc71XmlHeader.Company.CompanyContact = moFieldAddAmc71CompanyContact.getString();
                amc71XmlHeader.CompanyBranch.CompanyBranchGln = moFieldAddAmc71CompanyBranchGln.getString();
                amc71XmlHeader.CompanyBranch.ShipToName = moFieldAddAmc71ShipToName.getString();
                amc71XmlHeader.CompanyBranch.ShipToAddress = moFieldAddAmc71ShipToAddress.getString();
                amc71XmlHeader.CompanyBranch.ShipToCity = moFieldAddAmc71ShipToCity.getString();
                amc71XmlHeader.CompanyBranch.ShipToPostalCode = moFieldAddAmc71ShipToPostalCode.getString();
                json = amc71XmlHeader.encodeJson();
                break;
                
            default:
        }
        
        return json;
    }

    /**
     * Checks if number series are defined by system.
     * @return <code>true</code> if number series are defined by system.
     */
    private boolean areNumberSeriesBySystem() {
        return mbIsSales ||
            SLibUtilities.compareKeys(manDpsClassKey, SDataConstantsSys.TRNS_CL_DPS_PUR_EST) ||
            SLibUtilities.compareKeys(manDpsClassKey, SDataConstantsSys.TRNS_CL_DPS_PUR_ORD);
    }
    
    private boolean isLocalCurrency() {
        return miClient.getSession().getSessionCustom().isLocalCurrency(moFieldFkCurrencyId.getKeyAsIntArray());
    }

    /**
     * Checks if CFD emission is required for current document.
     * @return <code>true</code> if CFD is required for current document.
     */
    private boolean isCfdEmissionRequired() {
        boolean isDocSuitable = mbIsSales && (mbIsDpsInvoice || mbIsDpsAdjustment);
        boolean isXmlSuitable = mnCfdXmlType != SDataConstantsSys.TRNS_TP_XML_NA;
        
        return isDocSuitable && isXmlSuitable;
    }
    
    private boolean isCfdCfdiRelatedRequired() {
        return isCfdEmissionRequired() && mbIsDpsAdjustment;
    }
    
    private boolean isCfdIntCommerceRequired() {
        return isCfdEmissionRequired() && isBizPartnerInt();
    }
    
    private boolean isCfdAddendaRequired() {
        return isCfdEmissionRequired() && moBizPartner != null && moBizPartner.getIsCustomer() && moBizPartner.getDbmsCategorySettingsCus().getFkCfdAddendaTypeId() != SDataConstantsSys.BPSS_TP_CFD_ADD_NA;
    }
    
    private boolean isCfdXmlFilePermitted() {
        return !mbIsSales && (mbIsDpsInvoice || mbIsDpsAdjustment);
    }
    
    private boolean isApplingFunctionalAreas() {
        return miClient.getSessionXXX().getParamsCompany().getIsFunctionalAreas();
    }

    private boolean isApplingCreditValidation() {
        boolean applyOnSal = mbIsSales && (
                ((mbIsDpsEstimate || mbIsDpsContract) && miClient.getSessionXXX().getParamsErp().getIsSalesCreditContract()) ||
                (mbIsDpsOrder && miClient.getSessionXXX().getParamsErp().getIsSalesCreditOrder()) ||
                (mbIsDpsInvoice && miClient.getSessionXXX().getParamsErp().getIsSalesCreditInvoice()));
        boolean applyOnPur = !mbIsSales && (
                ((mbIsDpsEstimate || mbIsDpsContract) && miClient.getSessionXXX().getParamsErp().getIsPurchasesCreditContract()) ||
                (mbIsDpsOrder && miClient.getSessionXXX().getParamsErp().getIsPurchasesCreditOrder()) ||
                (mbIsDpsInvoice && miClient.getSessionXXX().getParamsErp().getIsPurchasesCreditInvoice()));
                
        return applyOnSal || applyOnPur;
    }

    /**
     * Checks if business partner is manually blocked.
     * Business partner blocking applies only to orders and invoices.
     */
    private boolean isBizPartnerBlocked(int idBizPartner) {
        boolean blocked = false;

        if (mbIsDpsOrder || mbIsDpsInvoice) {
            try {
                blocked = SDataUtilities.obtainIsBizPartnerBlocked(miClient, idBizPartner, STrnUtils.getBizPartnerCategoryId(mnFormType));
            }
            catch (Exception e) {
                SLibUtilities.printOutException(this, e);
            }
        }

        return blocked;
    }

    private boolean isBizPartnerCreditOk(final int risk, final boolean isDocBeingOpened) {
        boolean isCreditOk = true;

        if (isApplingCreditValidation()) {
            if (risk == SModSysConsts.BPSS_RISK_D_BLOCKED) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_INF_BP_BLOCKED);
                isCreditOk = false;
            }
            else if (risk == SModSysConsts.BPSS_RISK_E_TRIAL_WO_OPS) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_INF_BP_TRIAL_WO_OPS);
                isCreditOk = false;
            }
            else {
                // risks low, med., high & trial w/ops.:
                
                if (risk == SModSysConsts.BPSS_RISK_F_TRIAL_W_OPS) {
                    if (miClient.showMsgBoxConfirm(SLibConstants.MSG_INF_BP_TRIAL_W_OPS + "\n" + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                        isCreditOk = false;
                    }
                }
                
                if (isCreditOk) {
                    // validate credit limit:
                    
                    boolean validateCreditLimit = risk > SModSysConsts.BPSS_RISK_A_RISK_LOW;
                    boolean canOmitCreditLimit = risk <= SModSysConsts.BPSS_RISK_B_RISK_MED;
                    
                    if (validateCreditLimit && !isBizPartnerCreditLimitOk(canOmitCreditLimit, isDocBeingOpened)) {
                        isCreditOk = false;
                    }

                    if (isCreditOk) {
                        // validate overdue documents:
                        
                        boolean validateOverdueDocs = true; // all risks apply
                        boolean canOmitOverdueDocs = risk != SModSysConsts.BPSS_RISK_C_RISK_HIGH;

                        if (validateOverdueDocs && !areBizPartnerOverdueDocsOk(canOmitOverdueDocs, isDocBeingOpened)) {
                            isCreditOk = false;
                        }
                    }
                }
            }
        }

        return isCreditOk;
    }

    private boolean isEntriesRecalculationNeeded() {
        boolean isUpdateNeeded = false;

        if (mdOldExchangeRate != moFieldExchangeRate.getDouble() || mdOldDiscountDocPercentage != moFieldDiscountDocPercentage.getDouble() || mbOldIsDiscountDocApplying != moFieldIsDiscountDocApplying.getBoolean()) {
            isUpdateNeeded = true;
        }

        return isUpdateNeeded;
    }
    
    private void isBolAlreadySelected(int[] pk) {
        try {
            ResultSet resultSet;
            String sql = "SELECT num_ser, num, id_year FROM trn_dps WHERE fid_bol_n = " + pk[0];
            resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                miClient.showMsgBoxInformation("Este registro ya fue seleccionado anteriormente en la facura " + 
                        (resultSet.getString(1).isEmpty() ? resultSet.getString(2) : resultSet.getString(1) + " - " + resultSet.getString(2)) +
                        " con año " + resultSet.getInt(3));
            }
        }
        catch(Exception e) {
            miClient.showMsgBoxWarning(e.getMessage());
        }
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

        // update DPS data required to calculate document's value:
        moDps.setFkLanguajeId(moFieldFkLanguajeId.getKeyAsIntArray()[0]);
        moDps.setFkCurrencyId(moFieldFkCurrencyId.getKeyAsIntArray()[0]);
        moDps.setExchangeRateSystem(moFieldExchangeRateSystem.getDouble());
        moDps.setExchangeRate(moFieldExchangeRate.getDouble());
        moDps.setIsDiscountDocApplying(moFieldIsDiscountDocApplying.getBoolean());
        moDps.setIsDiscountDocPercentage(moFieldIsDiscountDocPercentage.getBoolean());
        moDps.setDiscountDocPercentage(moFieldDiscountDocPercentage.getDouble());

        // to calculate document's value, all entries must be added to DPS object:
        moDps.getDbmsDpsEntries().clear();
        for (i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
            entry = (SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData();
            quantity += entry.getOriginalQuantity();
            moDps.getDbmsDpsEntries().add(entry);
        }

        // display total quantity:
        jtfQuantityTotal.setText(SLibUtils.getDecimalFormatQuantity().format(quantity));

        // update International Commerce data required (if applies) to calculate document's value:
        if (isCfdIntCommerceRequired()) {
            if (isCurrencyUsd()) {
                moFieldCfdCceExchangeRateUsd.setFieldValue(moFieldExchangeRate.getDouble());
            }
            moDps.getDbmsDataDpsCfd().setCfdCceTipoCambioUsd(SLibUtils.getDecimalFormatExchangeRate().format(moFieldCfdCceExchangeRateUsd.getDouble()));
        }
        
        // calculate and display document's value:
        try {
            moDps.calculateTotal(miClient);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            renderDpsValue();
        }
    }
    
    /**
     * Populates combo boxes that depends on current business partner.
     */
    @SuppressWarnings("unchecked")
    private void populateComboBoxesBizPartner() {
        // populate combo box for INCOTERM, set delivery type aswell:
        
        mnDeliveryType = (moBizPartnerBranchAddressMain.getFkCountryId_n() == SLibConstants.UNDEFINED || miClient.getSession().getSessionCustom().isLocalCountry(new int[] { (moBizPartnerBranchAddressMain.getFkCountryId_n()) })) ? SModSysConsts.LOGS_TP_DLY_DOM : SModSysConsts.LOGS_TP_DLY_INT;
        SFormUtilities.populateComboBox(miClient, jcbFkIncotermId, SModConsts.LOGS_INC, new int[] { mnDeliveryType });
        moFieldFkIncotermId.setFieldValue(new int[] { SModSysConsts.LOGS_INC_NA }); // set default value for field
        itemStateChangedFkIncotermId();
        
        // populate combo box for contacts:
        
        if (mbIsSales) {
            jcbFkContactId_n.removeAllItems();
            jcbFkContactId_n.addItem(new SFormComponentItem(new int[2], "(Seleccionar comprador)"));

            for (SDataBizPartnerBranchContact contact : moBizPartnerBranch.getDbmsBizPartnerBranchContacts()) {
                if (contact.getPkContactId() > 1 && !contact.getIsDeleted()) {  // contact #1 has company branch telephones
                    jcbFkContactId_n.addItem(new SFormComponentItem(contact.getPrimaryKey(), contact.getContact() + " (" + contact.getDbmsContactType() + ")"));
                }
            }
        }
        
        jcbCfdCceFkBizPartnerAddressee.removeAllItems();
        
        if (isCfdIntCommerceRequired()) {
            SFormUtilities.populateComboBox(miClient, jcbCfdCceFkBizPartnerAddressee, SDataConstants.BPSU_BP_ADDEE, new int[] { moBizPartner.getPkBizPartnerId() });
        }
    }
    
    /**
     * This method can be invoked only once and only for new documents!
     */
    private void updateDpsCfdiSettings() {
        if (moDps.getIsRegistryNew()) {
            // set document's CFDI values:
            if (isCfdEmissionRequired() && moDps.getDbmsDataDpsCfd() != null) {
                if (moDps.getDbmsDataDpsCfd().getPaymentWay().isEmpty()) {
                    moFieldCfdiPaymentWay.setFieldValue(moBizPartnerCategory.getCfdiPaymentWay());
                }

                if (moDps.getDbmsDataDpsCfd().getPaymentMethod().isEmpty()) {
                    moFieldCfdiPaymentMethod.setFieldValue(moFieldFkPaymentTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.TRNS_TP_PAY_CASH ? DCfdi40Catalogs.MDP_PUE : DCfdi40Catalogs.MDP_PPD);
                }
                
                if (moDps.getDbmsDataDpsCfd().getTaxRegimeIssuing().isEmpty()) {
                    moFieldCfdiTaxRegimeIss.setFieldValue(miClient.getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdRegimenFiscal());
                }
                
                if (moDps.getDbmsDataDpsCfd().getTaxRegimeReceptor().isEmpty()) {
                    moFieldCfdiTaxRegimeRec.setFieldValue(moBizPartnerCategory.getTaxRegime()); 
                }
                
                if (moDps.getDbmsDataDpsCfd().getGlobalPeriodocity().isEmpty()) {
                    moFieldCfdiGblPeriodicity.setFieldValue(""); 
                }
                
                if (moDps.getDbmsDataDpsCfd().getGlobalMonths().isEmpty()) {
                    moFieldCfdiGblMonth.setFieldValue(""); 
                }
                
                if (moDps.getDbmsDataDpsCfd().getGlobalYear() == 0) {
                    moFieldCfdiGblYear.setFieldValue(0); 
                }
                
                if (moDps.getDbmsDataDpsCfd().getCfdiUsage().isEmpty()) {
                    if (mbIsDpsAdjustment) {
                        moFieldCfdiCfdiUsage.setFieldValue(DCfdi40Catalogs.ClaveUsoCfdiDevolucionesDescuentos);
                    }
                    else {
                        if (isCfdIntCommerceRequired()) {
                            moFieldCfdiCfdiUsage.setFieldValue(DCfdi33Catalogs.CFDI_USO_POR_DEF);
                        }
                        else {
                            moFieldCfdiCfdiUsage.setFieldValue(moBizPartnerCategory.getCfdiCfdiUsage());
                            if (jcbCfdiCfdiUsage.getSelectedIndex() <= 0) {
                                moFieldCfdiCfdiUsage.setFieldValue(miClient.getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdUsoCFDI());
                            }
                        }
                    }
                }
                
                moFieldCfdiConfirmation.setFieldValue(""); // confirmation is unique, cannot be copied when document is being created
                moFieldCfdiGblYear.setFieldValue(0); // clear global year, cannot be copied when document is being created
            }
        }
    }
    
    /**
     * This method must be invoked every time an entry is added, edited or deleted!
     */
    private void updateDpsEntryCfdiSettings() throws SQLException {
        if (!moDps.getDbmsDpsEntries().isEmpty()) {
            for (int i = 0 ; i < moDps.getDbmsDpsEntries().size(); i++) {
                String useCfdi = SDataDps.getUseCfdi(miClient, moDps.getDbmsDpsEntries().get(i).getFkItemId(), moBizPartnerCategory.getPkBizPartnerId());
                if (!useCfdi.isEmpty()) {
                    moFieldCfdiCfdiUsage.setFieldValue(useCfdi);
                    i =  moDps.getDbmsDpsEntries().size();
                }
            }
        }
        else {
            if (mbIsDpsAdjustment) {
                moFieldCfdiCfdiUsage.setFieldValue(DCfdi40Catalogs.ClaveUsoCfdiDevolucionesDescuentos);
            }
            else {
                if (isCfdIntCommerceRequired()) {
                    moFieldCfdiCfdiUsage.setFieldValue(DCfdi33Catalogs.CFDI_USO_POR_DEF);
                }
                else {
                    moFieldCfdiCfdiUsage.setFieldValue(moBizPartnerCategory.getCfdiCfdiUsage());
                    if (jcbCfdiCfdiUsage.getSelectedIndex() <= 0) {
                        moFieldCfdiCfdiUsage.setFieldValue(miClient.getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdUsoCFDI());
                    }
                }
            }        
        }
    }

    private void setLogisticsData() {
        if (jtbLinkTicket.isSelected()) {
            SDataDpsEntry entry;
            Vector<SDataScaleTicketDps>aux = new Vector<>();
            ArrayList<String> numTics = new ArrayList<>();
            ArrayList<String> dri = new ArrayList<>();
            ArrayList<String> pla = new ArrayList<>();
            for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
                entry = (SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData();
                boolean foundDriver = false;
                for (String driver : dri) {
                    if (driver.equals(entry.getDriver())) {
                        foundDriver = true;
                        break;
                    }
                }
                if (!foundDriver) {
                    dri.add(entry.getDriver());
                }
                boolean foundPlate = false;
                for (String plate : pla) {
                    if (plate.equals(entry.getPlate())) {
                        foundPlate = true;
                        break;
                    }
                }
                if (!foundPlate) {
                    pla.add(entry.getPlate());
                }
                for (SDataScaleTicketDpsEntry ticDpsEty : entry.getDbmsScaleTicketsEty()) {
                    SDataScaleTicketDps ticDps = new SDataScaleTicketDps();
                    ticDps.setPkScaleTicketId(ticDpsEty.getPkScaleTicketId());
                    aux.add(ticDps);
                }
                String[] num = entry.getTicket().split(", ");
                boolean found = false;
                for (int j = 0; j < num.length; j++) {
                    for (String x : numTics) {
                        if (x.equals(num[j])) {
                            found = true;
                        }
                    }
                    if (!found) {
                        numTics.add(num[j]);
                    }
                }
            }
            mvScaleTicDps.clear();
            for (SDataScaleTicketDps ticDpsAux : aux) {
                if (mvScaleTicDps.isEmpty()) {
                    mvScaleTicDps.add(ticDpsAux);
                }
                else {
                    boolean found = false;
                    for (SDataScaleTicketDps ticDps : mvScaleTicDps) {
                        if (ticDps.getPkScaleTicketId() == ticDpsAux.getPkScaleTicketId()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        mvScaleTicDps.add(ticDpsAux);
                    }
                }
            }
            String tickets = "";
            for (String num : numTics) {
                tickets += (tickets.isEmpty() ? "" : ", ") + num;
            }
            String plates = "";
            for (String plate : pla) {
                plates += (plates.isEmpty() ? "" : ", ") + plate;
            }
            String drivers = "";
            for (String driver : dri) {
                drivers += (drivers.isEmpty() ? "" : ", ") + driver;
            }
            moFieldDriver.setString(drivers);
            moFieldPlate.setString(plates);
            moFieldTicket.setString(tickets);
        }
    }
    /**
     * Updates current document, when new, with business partner default settings.
     */
    private void updateDpsWithBizPartnerSettings() {
        if (moDps.getIsRegistryNew()) {
            // update document properties that cannot be edited by user into GUI:

            moDps.setFkBizPartnerId_r(moBizPartner.getPkBizPartnerId());
            moDps.setFkBizPartnerBranchId(moBizPartnerBranch.getPkBizPartnerBranchId());

            if (mbIsSales) {
                moDps.setFkTaxIdentityEmisorTypeId(miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFkTaxIdentityId());
                moDps.setFkTaxIdentityReceptorTypeId(moBizPartner.getFkTaxIdentityId());

                int[] keyAgent = (int[]) moBizPartner.getSalesAgentKey((int[]) moBizPartnerBranch.getPrimaryKey());
                int[] keySuper = (int[]) moBizPartner.getSalesSupervisorKey((int[]) moBizPartnerBranch.getPrimaryKey());

                mnSalesAgentBizPartnerId_n = keyAgent == null ? SLibConsts.UNDEFINED : keyAgent[0];
                mnSalesSupervisorBizPartnerId_n = keySuper == null ? SLibConsts.UNDEFINED : keySuper[0];
            }
            else {
                moDps.setFkTaxIdentityEmisorTypeId(moBizPartner.getFkTaxIdentityId());
                moDps.setFkTaxIdentityReceptorTypeId(miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFkTaxIdentityId());

                mnSalesAgentBizPartnerId_n = SLibConsts.UNDEFINED;
                mnSalesSupervisorBizPartnerId_n = SLibConsts.UNDEFINED;
            }

            if (moDps.getFkSalesAgentId_n() == SLibConsts.UNDEFINED) {
                mnSalesAgentId_n = mnSalesAgentBizPartnerId_n;
                moDps.setFkSalesAgentId_n(mnSalesAgentId_n);
            }
            moDps.setFkSalesAgentBizPartnerId_n(mnSalesAgentBizPartnerId_n);

            if (moDps.getFkSalesSupervisorId_n() == SLibConsts.UNDEFINED) {
                mnSalesSupervisorId_n = mnSalesSupervisorBizPartnerId_n;
                moDps.setFkSalesSupervisorId_n(mnSalesSupervisorId_n);
            }
            moDps.setFkSalesSupervisorBizPartnerId_n(mnSalesSupervisorBizPartnerId_n);

            renderSalesAgentOfBizPartner(moDps.getFkSalesAgentBizPartnerId_n() == 0 ? null : new int[] { moDps.getFkSalesAgentBizPartnerId_n() });
            renderSalesAgentOfDocument(moDps.getFkSalesAgentId_n() == 0 ? null : new int[] { moDps.getFkSalesAgentId_n() });
            renderSalesSupervisorOfBizPartner(moDps.getFkSalesSupervisorBizPartnerId_n() == 0 ? null : new int[] { moDps.getFkSalesSupervisorBizPartnerId_n() });
            renderSalesSupervisorOfDocument(moDps.getFkSalesSupervisorId_n() == 0 ? null : new int[] { moDps.getFkSalesSupervisorId_n() });

            // set document's payment type:
            if (moDps.getFkPaymentTypeId() == SLibConsts.UNDEFINED) {
                if (moBizPartnerCategory.getEffectiveCreditTypeId() == SDataConstantsSys.BPSS_TP_CRED_CRED_NO) {
                    moFieldFkPaymentTypeId.setFieldValue(new int[] { SDataConstantsSys.TRNS_TP_PAY_CASH });
                }
                else {
                    moFieldFkPaymentTypeId.setFieldValue(new int[] { SDataConstantsSys.TRNS_TP_PAY_CREDIT });
                }
            }

            // set document's CFDI values:
            updateDpsCfdiSettings();

            // set document's language:
            if (moDps.getFkLanguajeId() == SLibConsts.UNDEFINED) {
                moFieldFkLanguajeId.setFieldValue(new int[] { moBizPartnerCategory.getFkLanguageId_n() != SLibConsts.UNDEFINED ? moBizPartnerCategory.getFkLanguageId_n() : miClient.getSessionXXX().getParamsErp().getFkLanguageId() });
            }

            // set document's currency:
            if (moDps.getFkCurrencyId() == SLibConsts.UNDEFINED) {
                double exr = 0;

                moFieldFkCurrencyId.setFieldValue(new int[] { moBizPartnerCategory.getFkCurrencyId_n() != SLibConsts.UNDEFINED ? moBizPartnerCategory.getFkCurrencyId_n() : miClient.getSessionXXX().getParamsErp().getFkCurrencyId() });

                if (isLocalCurrency()) {
                    exr = 1;
                }
                else {
                    try {
                        exr = SDataUtilities.obtainExchangeRate(miClient, moFieldFkCurrencyId.getKeyAsIntArray()[0], moDps.getDate());
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                }

                moFieldExchangeRateSystem.setFieldValue(exr);
                moFieldExchangeRate.setFieldValue(exr);
            }

            // emulate item state changed events:
            
            itemStateChangedFkPaymentTypeId(moDps.getFkPaymentTypeId() == SLibConsts.UNDEFINED || mbIsDpsAdjustment);   // reset days of credit if needed
            itemStateChangedFkCurrencyId(false); // do not calculate document's total
        }
    }

    /**
     * Updates current document with form data, that one needed to recalculate document's total.
     */
    private void updateDpsWithCurrentFormData() {
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

    private void adequateDatesForOrderPrevious() {
        if (mbIsDpsOrder) {
            if (moDps.getIsRegistryNew() && !mbHasRightOrderDelay) {
                moFieldDate.setFieldValue(miClient.getSessionXXX().getSystemDate());
                moFieldDateDoc.setFieldValue(miClient.getSessionXXX().getSystemDate());
                moFieldDateStartCredit.setFieldValue(miClient.getSessionXXX().getSystemDate());
            }
            jftDate.setEditable(mbHasRightOrderDelay);
            jftDate.setFocusable(mbHasRightOrderDelay);
            jbDate.setEnabled(mbHasRightOrderDelay);
            jbSetTime.setEnabled(mbHasRightOrderDelay && mbIsDpsTimeReq);
        }
    }
    
    /**
     * Add system notes for document.
     */
    private void addSystemNotes() throws Exception {
        for (SDataDpsNotesRow dpsNotesRow : STrnUtilities.createDpsNotesRowsFromDpsNotes(STrnUtilities.createDpsNotesFromSystemNotes(miClient, (int[]) moDpsType.getPrimaryKey(), moDps.getFkCurrencyId(), true))) {
            // check that current system notes has not been inserted into notes grid:
            
            boolean exists = false;
            for (STableRow tableRow: moPaneGridNotes.getGridRows()) {
                if (((SDataDpsNotes) dpsNotesRow.getData()).getNotes().equals(((SDataDpsNotes) tableRow.getData()).getNotes())) {
                    exists = true;
                    break;
                }
            }
            
            if (!exists){
                moPaneGridNotes.addTableRow(dpsNotesRow);
            }
        }

        moPaneGridNotes.renderTableRows();
        moPaneGridNotes.setTableRowSelection(0);
    }

    /**
     * Obtains and set next document number.
     * Method setDpsType() must be already called before calling this method, document number obtaining depends on it.
     * Method evaluateAvailabilityCfdTabs() is called within this method.
     */
    private void obtainNextNumber() {
        int numNew = 0;
        int numMin = 0;
        int numMax = 0;
        int numNext = 0;

        if (mbIsNumberSeriesRequired && mbIsNumberSeriesAvailable) {
            try {
                /* Number series complement int array values:
                 * · index 0: minumum document number
                 * · index 1: maximum document number
                 * · index 2: document numbers approval year
                 * · index 3: document numbers approval number
                 */
                
                // set next document number:
                
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
                
                jcbNumberSeries.setEnabled(moDps.getIsRegistryNew() && jcbNumberSeries.getItemCount() > 1);
                jtfNumber.setEnabled(false);
                jtfNumber.setFocusable(false);
                
                // set document numbers aproval year & number:
                
                mnNumbersApprovalYear = ((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[2];
                mnNumbersApprovalNumber = ((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[3];
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
        else {
            mnNumbersApprovalYear = 0;
            mnNumbersApprovalNumber = 0;
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

        in = new Vector<>();
        in.add(moBizPartner.getPkBizPartnerId());
        in.add(cat);
        in.add(moDps.getPkYearId());
        in.add(moDps.getPkDocId());
        out = SDataUtilities.callProcedure(miClient, SProcConstants.FIN_GET_BP_BAL, in, SLibConstants.EXEC_MODE_VERBOSE);
        
        return (Double) out.get(0);
    }
    
    /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
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
    */
    
    /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro:
    private sa.lib.srv.redis.SRedisLock gainRecordUserRedisLock(java.lang.Object pk, long timeout) throws Exception {
        SRedisLock rlock = null;

        try {
            rlock = SRedisLockUtils.gainLock(miClient, SDataConstants.FIN_REC, pk, timeout / 1000);
        }
        catch (Exception e) {
            rlock =  null;
            miClient.showMsgBoxWarning("No fue posible obtener el acceso exclusivo al registro '" + jckRecordUser.getText() + "'.\n" + e);
            throw new Exception(e);
        }

        return rlock;
    }
    
    private void releaseRecordUserRedisLock() {
        if (moRecordUserRedisLock != null) {
            try {
                SRedisLockUtils.releaseLock(miClient, moRecordUserRedisLock);
                moRecordUserRedisLock = null;
            }
            catch (Exception e) {
                miClient.showMsgBoxWarning("No fue posible liberar el acceso exclusivo del registro '" + jckRecordUser.getText() + "'.\n" + e);
            }
        }
    }
    */

    private sa.lib.srv.SLock gainRecordUserSLock(java.lang.Object pk, long timeout) {
        SLock slock = null;

        try {
            slock = SLockUtils.gainLock(miClient, SDataConstants.FIN_REC, pk, timeout);
        }
        catch (Exception e) {
            slock = null;
            miClient.showMsgBoxWarning("No fue posible obtener el acceso exclusivo al registro '" + jckRecordUser.getText() + "'.\n" + e);
        }

        return slock;
    }
    
    private void releaseRecordUserSLock() {
        if (moRecordUserSLock != null) {
            try {
                SLockUtils.releaseLock(miClient, moRecordUserSLock);
                moRecordUserSLock = null;
            }
            catch (Exception e) {
                miClient.showMsgBoxWarning("No fue posible liberar el acceso exclusivo del registro '" + jckRecordUser.getText() + "'.\n" + e);
            }
        }
    }
    
    private boolean readRecordUser(Object pk) throws Exception {
        boolean error = true;
        SDataRecord record = null;
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
        SSrvLock lock = null;
        releaseRecordUserLock();
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        SRedisLock rlock = null;
        releaseRecordUserRedisLock();
        */
        SLock slock = null;
        releaseRecordUserSLock();
        moRecordUser = null;

        if (pk != null) {
            record = (SDataRecord) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_REC, pk, SLibConstants.EXEC_MODE_VERBOSE);
            /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
            lock = gainRecordUserLock(pk, record.getRegistryTimeout());
            if (lock != null) {
                moRecordUser = record;
                moRecordUserLock = lock;
                error = false;
            }
            */
            /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
            rlock = gainRecordUserRedisLock(pk, record.getRegistryTimeout());
            if (rlock != null) {
                moRecordUser = record;
                moRecordUserRedisLock = rlock;
                error = false;
            }
            */
            slock = gainRecordUserSLock(pk, record.getRegistryTimeout());
            if (slock != null) {
                moRecordUser = record;
                moRecordUserSLock = slock;
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
            jcbFkPaymentTypeId.setEnabled(true);
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
        dps.setConditionsPayment("");
        dps.setApprovalYear(0);
        dps.setApprovalNumber(0);
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
        dps.setFkUserDpsDeliveryAckId(SDataConstantsSys.USRX_USER_NA);
        dps.setFkUserAuditedId(SDataConstantsSys.USRX_USER_NA);
        dps.setFkUserAuthorizedId(SDataConstantsSys.USRX_USER_NA);
        dps.setFkCompanyBranchId(miClient.getSessionXXX().getCurrentCompanyBranchId());

        dps.resetRecord();

        if (mbIsDpsAdjustment) {
            dps.setFkPaymentTypeId(SLibConstants.UNDEFINED);
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
                dps.setDateDocDelivery_n(dps.getDate()); // document delivery date
            }

            for (SDataDpsEntry entry : dps.getDbmsDpsEntries()) {
                entry.setPkEntryId(0);
                entry.setIsRegistryNew(true); // force entries to be treated as new
                if (mbMatRequestImport) {
                    if (moBizPartnerBranch != null) {
                        entry.setFkTaxRegionId(moBizPartnerBranch.getFkTaxRegionId_n() != 0 ? moBizPartnerBranch.getFkTaxRegionId_n() : miClient.getSessionXXX().getParamsCompany().getFkDefaultTaxRegionId_n());
                    }
                    else {
                        entry.setFkTaxRegionId(miClient.getSessionXXX().getParamsCompany().getFkDefaultTaxRegionId_n());
                    }
                }
            }
        }
        
        if (isCfdEmissionRequired()) {
            SDataDpsCfd dpsCfd = dps.getDbmsDataDpsCfd();
            
            if (dpsCfd == null) {
                dpsCfd = new SDataDpsCfd();
                dps.setDbmsDataDpsCfd(dpsCfd);
            }
            
            dpsCfd.setIsRegistryNew(true); // force entries to be treated as new
        }

        return dps;
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

        if (isLocalCurrency()) {
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
        
        if (isCfdIntCommerceRequired()) {
            moFieldCfdCceTotalUsd.setFieldValue(SLibUtils.parseDouble(moDps.getDbmsDataDpsCfd().getCfdCceTotalUsd()));
        }
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

    private void renderPk() {
        jtfPkRo.setText(moDps.getIsRegistryNew() ? "" : moDps.getPkYearId() + "-" + moDps.getPkDocId());
        jtfPkRo.setCaretPosition(0);
    }

    private void renderBizPartner() {
        String[] address = null;
        int nFkRecAddressFormatTypeId_n = 0;
        
        jtfBizPartnerRo.setText("");
        jtfFiscalId.setText("");
        jtfBizPartnerBranchRo.setText("");
        jtfBizPartnerBranchAddressMain01Ro.setText("");
        jtfBizPartnerBranchAddressMain02Ro.setText("");
        jtfBizPartnerBranchAddress01Ro.setText("");
        
        jtfBizPartnerRo.setToolTipText(null);
        jtfFiscalId.setToolTipText(null);
        jtfBizPartnerBranchRo.setToolTipText(null);
        jtfBizPartnerBranchAddressMain01Ro.setToolTipText(null);
        jtfBizPartnerBranchAddressMain02Ro.setToolTipText(null);
        jtfBizPartnerBranchAddress01Ro.setToolTipText(null);
        
        SFormUtilities.locateComboBoxItem(jcbTaxRegionId, new int[] { 0 });

        if (moBizPartner != null) {
            jtfBizPartnerRo.setText(moBizPartner.getBizPartner());
            jtfBizPartnerRo.setToolTipText(jtfBizPartnerRo.getText());
            jtfBizPartnerRo.setCaretPosition(0);
            
            jtfFiscalId.setText(moBizPartner.getFiscalId());
            jtfFiscalId.setToolTipText(jtfFiscalId.getText());
            jtfFiscalId.setCaretPosition(0);

            if (moDps.getIsRegistryNew() || moDps.getDbmsDpsEntries().isEmpty()) {
                SFormUtilities.locateComboBoxItem(jcbTaxRegionId, new int[] { moBizPartnerBranch.getFkTaxRegionId_n() != 0 ? moBizPartnerBranch.getFkTaxRegionId_n() : miClient.getSessionXXX().getParamsCompany().getFkDefaultTaxRegionId_n() });

                if (jcbTaxRegionId.getSelectedIndex() == 0) {
                    jcbTaxRegionId.setEnabled(true);
                    jbTaxRegionId.setEnabled(true);
                    jbEditTaxRegion.setEnabled(false);
                }
            }
            else {
                SFormUtilities.locateComboBoxItem(jcbTaxRegionId, new int[] { moDps.getDbmsDpsEntries().get(0).getFkTaxRegionId() });
            }
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

                jtfBizPartnerBranchAddress01Ro.setText(address[0] + " " + address[1]);
                jtfBizPartnerBranchAddress01Ro.setToolTipText(jtfBizPartnerBranchAddressMain01Ro.getText());
                jtfBizPartnerBranchAddress01Ro.setCaretPosition(0);
            }
        }
        
        renderBizPartnerPrepaymentsBalance();
        adecuatePaymentTypeSettings();
        enableCfdFields(true);
    }

    private void renderBizPartnerPrepaymentsBalance() {
        mdPrepayments = 0;
        mdPrepaymentsCy = 0;
        
        if (moBizPartner != null) {
            try {
                mdPrepayments = STrnUtils.getPrepaymentsBalance(miClient.getSession(), 
                        manDpsClassKey[0], moBizPartner.getPkBizPartnerId(), (int[]) moDps.getPrimaryKey(), 
                        SLibConsts.UNDEFINED);
                
                if (jcbFkCurrencyId.getSelectedIndex() <= 0) {
                    mdPrepaymentsCy = mdPrepayments;
                }
                else {
                    mdPrepaymentsCy = STrnUtils.getPrepaymentsBalance(miClient.getSession(), 
                            manDpsClassKey[0], moBizPartner.getPkBizPartnerId(), (int[]) moDps.getPrimaryKey(), 
                            moFieldFkCurrencyId.getKeyAsIntArray()[0]);
                }
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
        
        jtfPrepaymentsCy.setText(SLibUtils.getDecimalFormatAmount().format(mdPrepaymentsCy));
        jtfPrepaymentsCyCurRo.setText(jtfCurrencyKeyRo.getText());
        jlPrepaymentsWarning.setVisible(mdPrepayments != 0);
    }
    
    private void renderSalesAgentOfBizPartner(int[] pk) {
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

    private void renderSalesSupervisorOfBizPartner(int[] pk) {
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

    private void renderSalesAgentOfDocument(int[] pk) {
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

    private void renderSalesSupervisorOfDocument(int[] pk) {
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
    
    private void renderAddendaJsonData() {
        if (moBizPartner != null && moDpsType.getXtaDpsType().isDocumentOrAdjustmentSal()) {
            switch (moBizPartner.getDbmsCategorySettingsCus().getFkCfdAddendaTypeId()) {
                case SDataConstantsSys.BPSS_TP_CFD_ADD_NA:
                case SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA:
                case SDataConstantsSys.BPSS_TP_CFD_ADD_LOREAL:
                case SDataConstantsSys.BPSS_TP_CFD_ADD_BACHOCO:
                case SDataConstantsSys.BPSS_TP_CFD_ADD_MODELO:
                case SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA:
                    break;
                    
                case SDataConstantsSys.BPSS_TP_CFD_ADD_AMECE71:
                    try {
                        String originalSupplierGln = moFieldAddAmc71SupplierGln.getString(); // preserve original value
                        
                        jcbAddAmc71SupplierGln.removeAllItems();
                        
                        moAddendaAmc71Manager = new SAddendaAmc71Manager(miClient.getSession().getStatement(), moBizPartner.getPkBizPartnerId());
                        
                        ArrayList<String> glns = moAddendaAmc71Manager.getSuppliersGlns();
                        
                        for (String gln : glns) {
                            jcbAddAmc71SupplierGln.addItem(gln);
                        }
                        
                        moFieldAddAmc71SupplierGln.setFieldValue(originalSupplierGln); // restore original value
                        
                        itemStateChangedAddAmc71SupplierGln();
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                    break;
                    
                default:
            }
        }
    }
    
    private void setExchangeRate(final int idCurrency, final SFormField field) {
        double rate = 0;

        try {
            rate = SDataUtilities.obtainExchangeRate(miClient, idCurrency, moFieldDate.getDate());
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        field.setFieldValue(rate);
     }

    private void updateCurrencyFieldsStatus(boolean enable) {
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
        jckRecordUser.setEnabled(mbIsDpsInvoice || mbIsDpsAdjustment);
        jtbSwitchCustomAcc.setEnabled(mbIsDpsInvoice/* || mbIsDpsAdjustment*/); // 2023-10-18 Sergio Flores: por ahora la contabilización personalizada sólo está disponible para facturas

        jbEntryNew.setEnabled(!mbIsDpsAdjustment);
        jbEntryCopy.setEnabled(!mbIsDpsAdjustment);
        jbEntryDiscountRetailChain.setEnabled(mbIsDpsAdjustment);
        jbEntryImportFromDps.setEnabled(mbIsDpsOrder || mbIsDpsInvoice || mbIsDpsAdjustment);
        jbEntryWizard.setEnabled(!mbIsDpsAdjustment);
        jbEntryImportFromMatRequest.setEnabled((mbIsDpsEstimate || mbIsDpsOrder || mbIsDpsInvoice) && ! mbIsSales);

        jlAdjustmentSubtypeId.setEnabled(mbIsDpsAdjustment);
        jcbAdjustmentSubtypeId.setEnabled(mbIsDpsAdjustment);

        jlDateDocLapsing_n.setEnabled(mbIsDpsOrder || mbIsDpsEstimate);
        jftDateDocLapsing_n.setEditable(mbIsDpsOrder || mbIsDpsEstimate);
        jftDateDocLapsing_n.setFocusable(mbIsDpsOrder || mbIsDpsEstimate);
        jbDateDocLapsing_n.setEnabled(mbIsDpsOrder || mbIsDpsEstimate);
        
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
        
        jckIsRebill.setEnabled(mbIsDpsOrder);
        
        if (!mbIsSales) {
            updateAgentBizPartnerLabels();
        }
    }
    
    private void updateAgentBizPartnerLabels(){
        
        jlSalesAgentBizPartner.setText("Agente de compras del asociado de negocios:");
        jlSalesAgent.setText("Agente de compras del documento:");
        jlSalesSupervisorBizPartner.setText("Supervisor de compras del asociado de negocios:");
        jlSalesSupervisor.setText("Supervisor de compras del documento:");
    }

    private void updateDpsFieldsStatus(boolean enable) {
        if (!enable) {
            mnFormStatus = SLibConstants.FORM_STATUS_READ_ONLY;

            jftDate.setEditable(false);
            jftDate.setFocusable(false);
            jbDate.setEnabled(false);
            jbSetTime.setEnabled(false);
            jcbNumberSeries.setEnabled(false);
            jtfNumber.setEditable(false);
            jtfNumber.setFocusable(false);
            jtfNumberReference.setEditable(false);
            jtfNumberReference.setFocusable(false);
            jckIsRebill.setEnabled(false);

            jckDateDoc.setEnabled(false);
            jftDateDoc.setEditable(false);
            jftDateDoc.setFocusable(false);
            jbDateDoc.setEnabled(false);

            jckRecordUser.setEnabled(false);
            jbRecordManualSelect.setEnabled(false);
            jbRecordManualView.setEnabled(false);
            jtbSwitchCustomAcc.setEnabled(false);

            jckShipments.setEnabled(false);
            jtfShipments.setEditable(false);
            jtfShipments.setFocusable(false);

            jcbFkPaymentTypeId.setEnabled(false);
            jtfDaysOfCredit.setEditable(false);
            jtfDaysOfCredit.setFocusable(false);
            jckDateStartCredit.setEnabled(false);
            jftDateStartCredit.setEditable(false);
            jftDateStartCredit.setFocusable(false);
            jbDateStartCredit.setEnabled(false);
            jbDateMaturity.setEnabled(false);
            jtfConditionsPayment.setEditable(false);
            jtfConditionsPayment.setFocusable(false);
            //jcbFkLanguageId.setEnabled(...); // language is not editable
            jcbFkDpsNatureId.setEnabled(false);
            jcbFkFunctionalAreaId.setEnabled(false);

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
            jbEntryCopy.setEnabled(false);
            jbEntryDelete.setEnabled(false);
            jbEntryDiscountRetailChain.setEnabled(false);
            jbEntryImportFromDps.setEnabled(false);
            jbEntryWizard.setEnabled(false);
            jbEntryImportFromMatRequest.setEnabled(false);

            jbNotesNew.setEnabled(false);
            jbNotesEdit.setEnabled(false);
            jbNotesDelete.setEnabled(false);
            jbPickSystemNotes.setEnabled(false);
            jbEditNotes.setEnabled(false);

            jcbAdjustmentSubtypeId.setEnabled(false);
            jcbTaxRegionId.setEnabled(false);
            jbTaxRegionId.setEnabled(false);
            jbEditTaxRegion.setEnabled(false);

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
            jtfReqNum.setEnabled(false);
            jcbDriver.setEnabled(false);
            jcbPlate.setEnabled(false);
            jtfTicket.setEditable(false);
            jtfTicket.setFocusable(false);
            jtbLinkTicket.setEnabled(false);
            
            jbEditLogistics.setEnabled(false);
            
            jlFkProductionOrderId_n.setEnabled(false);
            jcbFkProductionOrderId_n.setEnabled(false);
            jbFkProductionOrderId_n.setEnabled(false);
            
            enableCfdFields(false);   // disable CFD form tabs & fields
            
            enableCustomAccControls(false, false);
            enableCustomAccFields(false, true);
        }
        else {
            mnFormStatus = SLibConstants.FORM_STATUS_EDIT;

            jftDate.setEditable(true);
            jftDate.setFocusable(true);
            jbDate.setEnabled(true);
            jbSetTime.setEnabled(mbIsDpsTimeReq);
            jcbNumberSeries.setEnabled(moDps.getIsRegistryNew() || !mbIsNumberSeriesRequired);
            jtfNumber.setEditable(mbIsNumberEditable);
            jtfNumber.setFocusable(mbIsNumberEditable);
            jtfNumberReference.setEditable(true);
            jtfNumberReference.setFocusable(true);
            jckIsRebill.setEnabled(mbIsDpsOrder);

            jckDateDoc.setEnabled(true);
            itemStateChangedDateDoc();

            updateDpsControlsStatus();
            
            itemStateChangedRecordUser();

            jckShipments.setEnabled(true);
            itemStateChangedShipments();
            
            adecuatePaymentTypeSettings();
            itemStateChangedFkPaymentTypeId(false); // invokes methods itemStatePayments() and itemStateDateStartCredit()
            
            jcbFkPaymentTypeId.setEnabled(jcbFkPaymentTypeId.isEnabled() && !moDps.getIsCopied());
            jtfDaysOfCredit.setEditable(!moDps.getIsCopied() && moFieldFkPaymentTypeId.getKeyAsIntArray()[0] != SDataConstantsSys.TRNS_TP_PAY_CASH);
            jtfDaysOfCredit.setFocusable(!moDps.getIsCopied() && moFieldFkPaymentTypeId.getKeyAsIntArray()[0] != SDataConstantsSys.TRNS_TP_PAY_CASH);
            //jckDateStartCredit.setEnabled(...);   // status already set by previous call to method itemChangeFkPaymentTypeId()
            //jftDateStartCredit.setEditable(...);  // status already set by previous call to method itemChangeFkPaymentTypeId()
            //jftDateStartCredit.setFocusable(...); // status already set by previous call to method itemChangeFkPaymentTypeId()
            //jbDateStartCredit.setEnabled(...);    // status already set by previous call to method itemChangeFkPaymentTypeId()
            jtfConditionsPayment.setEditable(true);
            jtfConditionsPayment.setFocusable(true);
            //jcbFkLanguageId.setEnabled(...);      // language is not editable
            jcbFkDpsNatureId.setEnabled(jcbFkDpsNatureId.getItemCount() > 2);
            jcbFkFunctionalAreaId.setEnabled(isApplingFunctionalAreas() && (jcbFkFunctionalAreaId.getItemCount() - 1) != 1); // enable when functional areas applying and only one asignated to current user

            jcbFkCurrencyId.setEnabled(moDps.getIsRegistryNew());
            jbFkCurrencyId.setEnabled(moDps.getIsRegistryNew());
            itemStateChangedFkCurrencyId(false);      // do not calculate document's total
            updateCurrencyFieldsStatus(true);   // last time status check to prevent invalid currency changes

            //jbExchangeRate.setEnabled(...);       // status already set by previous call to method itemChangeFkCurrencyId()
            //jtfExchangeRate.setEditable(...);     // status already set by previous call to method itemChangeFkCurrencyId()
            //jtfExchangeRate.setFocusable(...);    // status already set by previous call to method itemChangeFkCurrencyId()
            //jbComputeTotal.setEnabled(...);       // status already set by previous call to method itemChangeFkCurrencyId()
            
            jckIsDiscountDocApplying.setEnabled(true);
            itemStateChangedIsDiscountDocApplying(false);
            itemStateChangedIsDiscountDocPercentage(false);
            jckIsCopy.setEnabled(!mbIsSales && (mbIsDpsInvoice || mbIsDpsAdjustment));

            //jbEntryNew.setEnabled(true); // status already set by previous call to method updateDpsControlsStatus()
            jbEntryDelete.setEnabled(true);
            //jbEntryDiscountRetailChain.setEnabled(true);  // status already set by previous call to method updateDpsControlsStatus()
            //jbEntryImportFromDps.setEnabled(true);        // status already set by previous call to method updateDpsControlsStatus()
            //jbEntryWizard.setEnabled(true);               // status already set by previous call to method updateDpsControlsStatus()

            jbNotesNew.setEnabled(true);
            jbNotesEdit.setEnabled(true);
            jbNotesDelete.setEnabled(true);
            jbPickSystemNotes.setEnabled(true);
            jbEditNotes.setEnabled(false);

            //jcbAdjustTypeId.setEnabled(true); // status already set by previous call to method updateDpsControlsStatus()
            jcbTaxRegionId.setEnabled(false);
            jbTaxRegionId.setEnabled(false);
            jbEditTaxRegion.setEnabled(true);

            jftDateDocDelivery_n.setEditable(!mbIsDpsAdjustment);
            jftDateDocDelivery_n.setFocusable(!mbIsDpsAdjustment);
            jbDateDocDelivery_n.setEnabled(!mbIsDpsAdjustment);
            //jftDateOrderLapsing_n.setEditable(...);   // status already set by previous call to method updateDpsControlsStatus()
            //jftDateOrderLapsing_n.setFocusable(...);  // status already set by previous call to method updateDpsControlsStatus()
            //jbDateOrderLapsing_n.setEnabled(...);     // status already set by previous call to method updateDpsControlsStatus()
            //jbSalesAgent.setEnabled(false);           // status already set by previous call to method updateDpsControlsStatus()
            //jbSalesSupervisor.setEnabled(false);      // status already set by previous call to method updateDpsControlsStatus()
            //jlFkContactId_n.setEnabled(false);        // status already set by previous call to method updateDpsControlsStatus()
            //jcbFkContactId_n.setEnabled(false);       // status already set by previous call to method updateDpsControlsStatus()

            jcbFkIncotermId.setEnabled(true);
            jbFkIncotermId.setEnabled(true);
            
            int[] spotSrcKey = moFieldFkSpotSrcId_n.getKeyAsIntArray(); // preserve original value
            int[] spotDesKey = moFieldFkSpotDesId_n.getKeyAsIntArray(); // preserve original value
            itemStateChangedFkIncotermId();
            moFieldFkSpotSrcId_n.setFieldValue(spotSrcKey); // restore original value
            moFieldFkSpotDesId_n.setFieldValue(spotDesKey); // restore original value
            
            //jcbFkSpotSrcId_n.setEnabled(...); // status already set by previous call to method itemStateChangedFkIncotermId()
            //jcbFkSpotDesId_n.setEnabled(...); // status already set by previous call to method itemStateChangedFkIncotermId()
            
            jcbFkModeOfTransportationTypeId.setEnabled(true);
            jbFkModeOfTransportationTypeId.setEnabled(true);
            
            jcbFkCarrierTypeId.setEnabled(true);
            jtfReqNum.setEnabled(true);
            itemStateChangedFkCarrierTypeId();      // invokes method itemChangeFkVehicleTypeId_n()
            //jcbFkCarrierId_n.setEnabled(...);     // status already set by previous call to method itemStateChangedFkCarrierTypeId()
            //jbFkCarrierId_n.setEnabled(...);      // status already set by previous call to method itemStateChangedFkCarrierTypeId()
            //jcbFkVehicleTypeId_n.setEnabled(...); // status already set by previous call to method itemStateChangedFkCarrierTypeId()
            //jcbFkVehicleId_n.setEnabled(...);     // status already set by previous call to method itemChangeFkVehicleTypeId_n()
            //jbFkVehicleId_n.setEnabled(...);      // status already set by previous call to method itemChangeFkVehicleTypeId_n()
            //jcbDriver.setEnabled(...);            // status already set by previous call to method itemStateChangedFkCarrierTypeId()
            //jcbPlate.setEnabled(...);             // status already set by previous call to method itemStateChangedFkCarrierTypeId()
            //jtfTicket.setEditable(...);           // status already set by previous call to method itemStateChangedFkCarrierTypeId()
            //jtfTicket.setFocusable(...);          // status already set by previous call to method itemStateChangedFkCarrierTypeId()
            
            jbEditLogistics.setEnabled(false);
            
            jlFkProductionOrderId_n.setEnabled(mbIsSales);
            jcbFkProductionOrderId_n.setEnabled(mbIsSales);
            jbFkProductionOrderId_n.setEnabled(mbIsSales);
            
            enableCfdFields(true);    // enable CFD form tabs & fields
            
            enableCustomAccControls(true, false);
            enableCustomAccFields(true, true);
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
        return ((mbHasDpsLinksAsSrc || mbHasDpsLinksAsDes) && !(mbHasDpsAdjustmentsAsDoc || mbHasDpsAdjustmentsAsAdj)) || mbHasIogSupplies || mbHasShipments && !mbHasCommissions;
    }

    private boolean hasDpsAdjustmentsAsAdjButIsEditable() {
        return (!(mbHasDpsLinksAsSrc || mbHasDpsLinksAsDes) && (mbHasDpsAdjustmentsAsDoc || mbHasDpsAdjustmentsAsAdj)) && !mbHasIogSupplies && !mbHasShipments && !mbHasCommissions;
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
                            if (miClient.showMsgBoxConfirm(msg + "\n¿Desea continuar?") == JOptionPane.YES_OPTION) {
                                msg = "";
                            }
                            break DPS;
                        }
                        else if (mbValidateLinkPeriod && dpsLinked.getDateDocLapsing_n() != null && moFieldDate.getDate().after(dpsLinked.getDateDocLapsing_n())) {
                            msg = "El valor del campo '" + jlDate.getText() + "' (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ")\nes posterior a la fecha 'última entrega' del documento de origen '" +  dpsLinked.getDpsNumber() + "' (" + SLibUtils.DateFormatDate.format(dpsLinked.getDateDocLapsing_n()) + ").";
                            if (miClient.showMsgBoxConfirm(msg + "\n¿Desea continuar?") == JOptionPane.YES_OPTION) {
                                msg = "";
                            }
                            break DPS;
                        }
                    }
                }

                // Validate links as document:

                if (mbIsDpsInvoice && entry.getDbmsDpsAdjustmentsAsDps().size() > 0) {
                    for (SDataDpsDpsAdjustment adjustment : entry.getDbmsDpsAdjustmentsAsDps()) {
                        dpsLinked = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, adjustment.getDbmsDpsAdjustmentKey(), SLibConstants.EXEC_MODE_VERBOSE);
                        if (moFieldDate.getDate().after(dpsLinked.getDate())) {
                            msg = "no puede ser posterior a la fecha del documento '" +  dpsLinked.getDpsNumber() + "' (" + SLibUtils.DateFormatDate.format(dpsLinked.getDate()) + ").";
                            break DPS;
                        }
                    }
                }

                // validate links as adjustment:

                if (mbIsDpsAdjustment && entry.getDbmsDpsAdjustmentsAsAdjustment().size() > 0) {
                    for (SDataDpsDpsAdjustment adjustment : entry.getDbmsDpsAdjustmentsAsAdjustment()) {
                        dpsLinked = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, adjustment.getDbmsDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
                        if (moFieldDate.getDate().before(dpsLinked.getDate())) {
                            msg = "no puede ser anterior a la fecha del documento '" +  dpsLinked.getDpsNumber() + "' (" + SLibUtils.DateFormatDate.format(dpsLinked.getDate()) + ").";
                            break DPS;
                        }
                    }
                }

                // Validate other links:

                if (mbIsDpsInvoice && !moDps.getIsRegistryNew()) {
                    vParams.clear();
                    vParams.add(entry.getPrimaryKey());
                    vParams.add(moFieldDate.getDate());
                    vParams = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_DPS_ETY_DATE_VAL, vParams, SLibConstants.EXEC_MODE_SILENT);

                    if (vParams.size() > 0) {
                        switch((Integer) vParams.get(0)) {
                            case 1: // linked to commissions
                                msg = "no puede ser posterior a la fecha de los documentos de comisiones.";
                                break;
                            // Deshabilitado, al momento de surtir inventarios por facturas/pedidos y editar el documento, lanzaba la validación
                            // case 2: // linked to DIOG's
                            //     msg = "no puede ser posterior a la fecha del documento de inventarios: '" + (String) vParams.get(1) + "'.";
                            //     break;
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

    private void setDpsType(int[] keyDocType) {
        int index = 0;
        Vector<SFormComponentItem> componentItems = null;

        mbResetingForm = true;
        
        // define DPS type:

        mbIsDpsContract = false;
        mbIsDpsEstimate = false;
        mbIsDpsOrder = false;
        mbIsDpsInvoice = false;
        mbIsDpsAdjustment = false;
        
        mbIsSales = false;
        
        manDpsClassKey = new int[] { keyDocType[0], keyDocType[1] };
        moDpsType = (SDataDpsType) SDataUtilities.readRegistry(miClient, SDataConstants.TRNU_TP_DPS, keyDocType, SLibConstants.EXEC_MODE_VERBOSE);

        mbIsDpsContract = SLibUtilities.compareKeys(SDataConstantsSys.TRNU_TP_DPS_SAL_CON, keyDocType) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNU_TP_DPS_PUR_CON, keyDocType);
        mbIsDpsEstimate = SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_PUR_EST, manDpsClassKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_EST, manDpsClassKey);
        mbIsDpsOrder = SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_PUR_ORD, manDpsClassKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_ORD, manDpsClassKey);
        mbIsDpsInvoice = SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_PUR_DOC, manDpsClassKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_DOC, manDpsClassKey);
        mbIsDpsAdjustment = SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ, manDpsClassKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ, manDpsClassKey);
        
        mbIsSales = SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_EST, manDpsClassKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_ORD, manDpsClassKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_DOC, manDpsClassKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ, manDpsClassKey);

        // define previous DPS type, if any:

        if (mbIsDpsEstimate) {
            moDialogPickerDps = null;
            manDpsClassPreviousKey = null;
        }
        else if (mbIsDpsOrder) {
            moDialogPickerDps = moDialogPickerDpsForLink;
            manDpsClassPreviousKey = mbIsSales ? SDataConstantsSys.TRNS_CL_DPS_SAL_EST : SDataConstantsSys.TRNS_CL_DPS_PUR_EST;
        }
        else if (mbIsDpsInvoice) {
            moDialogPickerDps = moDialogPickerDpsForLink;
            manDpsClassPreviousKey = mbIsSales ? SDataConstantsSys.TRNS_CL_DPS_SAL_ORD : SDataConstantsSys.TRNS_CL_DPS_PUR_ORD;
        }
        else if (mbIsDpsAdjustment) {
            moDialogPickerDps = moDialogPickerDpsForAdjustment;
            manDpsClassPreviousKey = mbIsSales ? SDataConstantsSys.TRNS_CL_DPS_SAL_DOC : SDataConstantsSys.TRNS_CL_DPS_PUR_DOC;
        }

        updateDpsControlsStatus();

        // define document number series:

        index = mvFields.indexOf(moFieldNumberSeries);
        mvFields.removeElementAt(index);
        jcbNumberSeries.removeAllItems();

        if (!areNumberSeriesBySystem()) {
            // document number series defined by user:

            jcbNumberSeries.setEditable(true);
            moFieldNumberSeries = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jcbNumberSeries, jlNumber);
            moFieldNumberSeries.setLengthMax(SDataDps.LEN_SERIES);
            
            mbIsNumberSeriesRequired = false;
            mbIsNumberSeriesAvailable = false;
        }
        else {
            // document number series defined by system:
            
            jcbNumberSeries.setEditable(false);
            moFieldNumberSeries = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbNumberSeries, jlNumber);
            moFieldNumberSeries.setIsSelectionItemApplying(false);  // combo box does not have label item
            
            // get available document number series from current company branch configuration:

            if (miClient.getSessionXXX().getCurrentCompanyBranch() != null) {
                componentItems = miClient.getSessionXXX().getCurrentCompanyBranch().getDnsForDps();

                for (SFormComponentItem componentItem : componentItems) {
                    if (SLibUtilities.compareKeys(moDpsType.getPrimaryKey(), componentItem.getPrimaryKey())) {
                        jcbNumberSeries.addItem(componentItem);
                    }
                }
            }

            mbIsNumberSeriesRequired = true;
            mbIsNumberSeriesAvailable = jcbNumberSeries.getItemCount() > 0;
        }

        mvFields.insertElementAt(moFieldNumberSeries, index);

        // activate/inactivate validation of link period:

        if (mbIsSales) {
            mbValidateLinkPeriod = ((SDataParamsErp) miClient.getSession().getConfigSystem()).getIsSalesLinkPeriod();
        }
        else {
            mbValidateLinkPeriod = ((SDataParamsErp) miClient.getSession().getConfigSystem()).getIsPurchasesLinkPeriod();
        }

        // add drivers and plates related to current business partner:

        jcbDriver.removeAllItems(); // XXX improve this! (sflores, 2017-02-23)
        jcbPlate.removeAllItems();  // XXX improve this! (sflores, 2017-02-23)

        mbResetingForm = false;
    }

    private void setBizPartner(int[] keyBizPartner, int[] keyBizPartnerBranch, int[] keyBizPartnerBranchAddress) {
        mbUpdatingForm = true;  // prevent item state change events to be processed
        
        // initialize current business partner registry objects:
        
        moBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, keyBizPartner, SLibConstants.EXEC_MODE_VERBOSE);
        moBizPartnerBranch = moBizPartner.getDbmsBizPartnerBranch(keyBizPartnerBranch);
        moBizPartnerBranchAddressMain = moBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial();
        moBizPartnerBranchAddress = moBizPartnerBranch.getDbmsBizPartnerBranchAddress(keyBizPartnerBranchAddress);
        
        if (mbIsSales) {
            moBizPartnerCategory = moBizPartner.getDbmsCategorySettingsCus();
        }
        else {
            moBizPartnerCategory = moBizPartner.getDbmsCategorySettingsSup();
        }
        
        if (moBizPartner.getIsDeleted() || moBizPartnerCategory.getIsDeleted()) {
            miClient.showMsgBoxWarning("¡El asociado de negocios está eliminado!");
        }
        
        // populate form combo boxes properly for current business partner:

        populateComboBoxesBizPartner();
        
        // if needed, set form and document fields with business partner preferences:
        
        if (moDps.getIsRegistryNew()) {
            updateDpsWithBizPartnerSettings();
            mbUpdatingForm = true;  // restore flag to prevent item state change events to be processed
        }
        
        // show current business partner:

        renderBizPartner();
        
        // show addenda JSON data, when needed:
        
        renderAddendaJsonData();
        
        mbUpdatingForm = false; // allow item state change events to be processed
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
        oDpsEntryComplementary.setOperationsType(SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS);
        oDpsEntryComplementary.setUserId(SLibConstants.UNDEFINED);
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
                        updateCurrencyFieldsStatus(false);
                    }
                    jcbFkCurrencyId.setEnabled(false);
                    jcbFkCurrencyId.setEnabled(false);
                    
                    if (mbIsDpsOrder) {
                        if (entry.getContractPriceYear() > 0 && entry.getContractPriceMonth()> 0) {
                            moGuiDpsLink.addDataDpsDestinyEntry(entry);
                        }
                    }
                    
                    break;
                }
            }
        }
    }

    /**
     * Creates CFD parameters.
     * NOTE 2018-05-16, Sergio Flores: This method is replicated in private static method erp.mtrn.data.SCfdUtils.createCfdParams(SClientInterface, SDataDps)!
     *      It is not clear yet if method version in current class is really needed.
     *      It seems that is used to process Addenda registrys when saving DPS, so, by now, it cannot be removed.
     * @return CFD parameters.
     */
    private erp.mtrn.data.SCfdParams createCfdParams() {
        String factura = "";
        String pedido = "";
        String contrato = "";
        String ruta = "";
        SDataDps dpsFactura = null;
        SDataDps dpsPedido = null;
        SDataDps dpsContrato = null;
        SDataCustomerBranchConfig customerBranchConfig = null;
        SCfdParams params = new SCfdParams();
        SDataBizPartner bizPartner = moBizPartner;
        SDataBizPartnerBranch bizPartnerBranch = moBizPartnerBranch;
        
        params.setReceptor(bizPartner);
        params.setReceptorBranch(bizPartnerBranch);
        params.setEmisor(miClient.getSessionXXX().getCompany().getDbmsDataCompany());

        if (moDps.getFkCompanyBranchId() == miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsBizPartnerBranchHq().getPkBizPartnerBranchId()) {
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
                        //dpsPedido = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, linkPedido.getDbmsSourceDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
                        dpsPedido = STrnUtilities.getFirtsLinkOrderType(miClient, moDps);
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

        params.setFkCfdAddendaTypeId(moBizPartnerCategory.getFkCfdAddendaTypeId());
        params.setLorealFolioNotaRecepción(moFieldAddLorealFolioNotaRecepción.getString());
        params.setBachocoSociedad(moFieldAddBachocoSociedad.getString());
        params.setBachocoOrganizaciónCompra(moFieldAddBachocoOrganizaciónCompra.getString());
        params.setBachocoDivisión(moFieldAddBachocoDivisión.getString());
        params.setSorianaTienda(moFieldAddSorianaTienda.getInteger());
        params.setSorianaEntregaMercancía(moFieldAddSorianaEntregaMercancía.getInteger());
        params.setSorianaRemisiónFecha(moFieldAddSorianaRemisiónFecha.getDate());
        params.setSorianaRemisiónFolio(moFieldAddSorianaRemisiónFolio.getString());
        params.setSorianaPedidoFolio(moFieldAddSorianaPedidoFolio.getString());
        params.setSorianaBultoTipo(moFieldAddSorianaBultoTipo.getInteger());
        params.setSorianaBultoCantidad(moFieldAddSorianaBultoCantidad.getDouble());
        params.setSorianaNotaEntradaFolio(!jtfAddSorianaNotaEntradaFolio.isEnabled() ? "" : moFieldAddSorianaNotaEntradaFolio.getString());
        params.setSorianaCita(moFieldAddSorianaCita.getString());
        params.setModeloDpsDescripción(moFieldAddModeloDpsDescripción.getString());
        params.setCfdAddendaSubtype(jcbAddCfdAddendaSubtype.getSelectedIndex() <= 0 ? 0 : moFieldAddCfdAddendaSubtype.getKeyAsIntArray()[0]);
        params.setJsonData(composeAddendaJsonData());

        switch (mnCfdXmlType) {
            case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                if (isCfdIntCommerceRequired()) {
                    params.setRegimenFiscal(new String[] { miClient.getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdRegimenFiscal() });
                }
                else {
                    params.setRegimenFiscal(SLibUtilities.textExplode(miClient.getSessionXXX().getParamsCompany().getFiscalSettings(), ";"));
                }
                break;
            case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
            case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                params.setRegimenFiscal(new String[] { miClient.getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdRegimenFiscal() });
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        // Ruta:

        customerBranchConfig = (SDataCustomerBranchConfig) SDataUtilities.readRegistry(miClient, SDataConstants.MKT_CFG_CUSB, new int[] { bizPartnerBranch.getPkBizPartnerBranchId() }, SLibConstants.EXEC_MODE_SILENT);

        if (customerBranchConfig != null) {
            if (customerBranchConfig.getFkSalesRouteId() != 0) {
                ruta = "" + customerBranchConfig.getFkSalesRouteId();
            }
        }

        params.setRuta(ruta);

        // Miscellaneous:

        params.setInterestDelayRate(miClient.getSessionXXX().getParamsCompany().getInterestDelayRate());

        return params;
    }

    private boolean isBizPartnerCreditLimitOk(final boolean canOmitCreditLimit, final boolean isDocBeingOpened) {
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

    private boolean areBizPartnerOverdueDocsOk(final boolean canOmitOverdueDocs, final boolean isDocBeingOpened) {
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
            if (canOmitOverdueDocs) {
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

    private boolean canClearAddAmc71Company() {
        return moFieldAddAmc71CompanyGln.getString().isEmpty() && moFieldAddAmc71CompanyContact.getString().isEmpty() && canClearAddAmc71CompanyBranch();
    }
    
    private boolean canClearAddAmc71CompanyBranch() {
        return moFieldAddAmc71CompanyBranchGln.getString().isEmpty() && canClearAddAmc71CompanyBranchShipTo();
    }
    
    private boolean canClearAddAmc71CompanyBranchShipTo() {
        return moFieldAddAmc71ShipToName.getString().isEmpty() && moFieldAddAmc71ShipToAddress.getString().isEmpty() && moFieldAddAmc71ShipToCity.getString().isEmpty() && moFieldAddAmc71ShipToPostalCode.getString().isEmpty();
    }
    
    /**
     * Allows user to pick document type when current document is being created, is new and no source document is provided.
     */
    private void pickDpsType() {
        SFormOptionPickerInterface picker = miClient.getOptionPicker(SDataConstants.TRNU_TP_DPS);

        picker.formReset();
        picker.setFilterKey(manDpsClassKey);
        picker.formRefreshOptionPane();
        picker.setSelectedPrimaryKey(moDpsType == null ? null : moDpsType.getPrimaryKey());
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            setDpsType((int[]) picker.getSelectedPrimaryKey());
            updateDpsWithCurrentFormData();
        }
    }

    /**
     * Allows user to pick business partner when current document is being created, is new and no source document is provided.
     */
    private void pickBizPartner() {
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
    
    private boolean isCustomAccEnableable() {
        return mnFormStatus == SLibConstants.FORM_STATUS_EDIT && mnAccCurrentAction != 0 && jcbAccItem.getSelectedIndex() > 0;
    }
    
    private double getAccSubtotal() {
        return SLibUtils.roundAmount(moFieldAccSubtotalCy.getDouble() * moDps.getExchangeRate());
    }

    /**
     * Calculate subtotal of customized accounting.
     * @param entryToExclude Entry to exclude. Can be <code>null</code>.
     */
    private void calculateCustomAccSubtotal(final SDataDpsCustomAccEntry entryToExclude) {
        mdAccSubtotal = 0d;
        mdAccSubtotalCy = 0d;
        mdAccSubtotalPct = 0d;
        
        for (STableRow row : moPaneGridCustomAcc.getGridRows()) {
            SDataDpsCustomAccEntry entry = (SDataDpsCustomAccEntry) row.getData();
            if (entryToExclude == null || entryToExclude != entry) {
                mdAccSubtotal = SLibUtils.roundAmount(mdAccSubtotal + entry.getSubtotal());
                mdAccSubtotalCy = SLibUtils.roundAmount(mdAccSubtotalCy + entry.getSubtotalCy());
                mdAccSubtotalPct = SLibUtils.round(mdAccSubtotalPct + entry.getSubtotalPct(), DECS_PCT);
            }
        }
    }
    
    private void enableCustomAccControls(final boolean enableCrud, final boolean enableOkCancel) {
        jbCustomAccEntryNew.setEnabled(enableCrud);
        jbCustomAccEntryCopy.setEnabled(enableCrud);
        jbCustomAccEntryEdit.setEnabled(enableCrud);
        jbCustomAccEntryDelete.setEnabled(enableCrud);
        jbCustomAccEntryOk.setEnabled(enableOkCancel);
        jbCustomAccEntryCancel.setEnabled(enableOkCancel);
        
        moPaneGridCustomAcc.getTable().setEnabled(mnAccCurrentAction == 0);
        moPaneGridCustomAcc.getTable().getTableHeader().setEnabled(mnAccCurrentAction == 0);
    }
    
    private void enableCustomAccFields(final boolean enable, final boolean appliesToAccItem) {
        if (!enable) {
            if (appliesToAccItem) {
                jcbAccItem.setEnabled(false);
                
                jbPickAccItem.setEnabled(false);
            }
            
            jcbAccItemRef_n.setEnabled(false);
            jtfAccQuantity.setEnabled(false);
            jcbAccUnit.setEnabled(false);
            jckAccIsSubtotalPctApplying.setEnabled(false);
            jtfAccSubtotalPct.setEnabled(false);
            jtfAccSubtotalCy.setEnabled(false);
            jtfAccConcept.setEnabled(false);
            
            jbPickAccItemRef_n.setEnabled(false);
            jbPickAccUnit.setEnabled(false);
            jbExeWizardAccSubtotal.setEnabled(false);
            
            moPanelFkCostCenterId_n.enableFields(false);
        }
        else {
            boolean enableable = isCustomAccEnableable();
            boolean areThereEntries = !moPaneGridCustomAcc.getGridRows().isEmpty();
            
            if (appliesToAccItem) {
                jcbAccItem.setEnabled(enableable);
                
                jbPickAccItem.setEnabled(enableable);
            }
            
            jcbAccItemRef_n.setEnabled(enableable);
            jtfAccQuantity.setEnabled(enableable);
            jcbAccUnit.setEnabled(enableable);
            jckAccIsSubtotalPctApplying.setEnabled(enableable && !areThereEntries);
            jtfAccSubtotalPct.setEnabled(enableable && jckAccIsSubtotalPctApplying.isSelected());
            jtfAccSubtotalCy.setEnabled(enableable && !jckAccIsSubtotalPctApplying.isSelected());
            jtfAccConcept.setEnabled(enableable);
            
            jbPickAccItemRef_n.setEnabled(enableable);
            jbPickAccUnit.setEnabled(enableable);
            jbExeWizardAccSubtotal.setEnabled(enableable);
            
            moPanelFkCostCenterId_n.enableFields(enableable);
        }
    }
    
    private void clearCustomAccFields(final boolean appliesToAccItem) {
        if (appliesToAccItem) {
            moFieldAccItem.resetField();
            itemStateChangedAccItem(false);
        }
        
        moFieldAccItemRef_n.resetField();
        moFieldAccQuantity.resetField();
        moFieldAccUnit.resetField();
        moFieldAccIsSubtotalPctApplying.resetField();
        itemStateChangedAccIsSubtotalPctApplying(false);
        moFieldAccSubtotalPct.resetField();
        moFieldAccSubtotalCy.resetField();
        moFieldAccConcept.resetField();
        
        moPanelFkCostCenterId_n.resetPanel();
        
        if (isLocalCurrency()) {
            jtfAccSubtotal.setText("");
        }
        else {
            jtfAccSubtotal.setText(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(0));
        }
    }
    
    @SuppressWarnings("unchecked")
    private void renderAccItem(final boolean clearFields) {
        if (clearFields) {
            clearCustomAccFields(false);
        }
        
        if (moAccItem == null) {
            enableCustomAccFields(false, false);
            
            // update units:
            
            jcbAccUnit.removeAllItems();
        }
        else {
            enableCustomAccFields(true, false);
            
            // update units:
            
            if (moAccItem.getFkUnitAlternativeTypeId() != SDataConstantsSys.ITMU_TP_UNIT_NA) {
                SFormUtilities.populateComboBox(miClient, jcbAccUnit, SDataConstants.ITMU_UNIT, new Object[] { moAccItem.getDbmsDataUnit().getFkUnitTypeId(), moAccItem.getFkUnitAlternativeTypeId() });
            }
            else {
                SFormUtilities.populateComboBox(miClient, jcbAccUnit, SDataConstants.ITMU_UNIT, new int[] { moAccItem.getDbmsDataUnit().getFkUnitTypeId() });
            }
            
            moFieldAccUnit.setKey(new int[] { moAccItem.getFkUnitId() });
            
            // update percentage applying:
            
            moFieldAccIsSubtotalPctApplying.setBoolean(moPaneGridCustomAcc.getGridRows().isEmpty() ? true : ((SDataDpsCustomAccEntry) moPaneGridCustomAcc.getGridRows().get(0).getData()).getIsSubtotalPctApplying());
            itemStateChangedAccIsSubtotalPctApplying(false);
            
            // update cost center:
            
            moPanelFkCostCenterId_n.enableFields(isCustomAccEnableable());

            try {
                moPanelFkCostCenterId_n.getFieldAccount().setString(SDataUtilities.obtainCostCenterItem(miClient.getSession(), moAccItem.getPkItemId()));
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
            finally {
                moPanelFkCostCenterId_n.refreshPanel();
            }
        }
    }
    
    private void renderCustomAccEntry(final SDataDpsCustomAccEntry entry) {
        mbUpdatingForm = true;
        
        if (entry == null) {
            clearCustomAccFields(true);
        }
        else {
            moFieldAccItem.setKey(new int[] { entry.getFkItemId() });
            itemStateChangedAccItem(true);
            moFieldAccItemRef_n.setKey(new int[] { entry.getFkItemRefId_n() });
            moFieldAccQuantity.setDouble(entry.getQuantity());
            moFieldAccUnit.setKey(new int[] { entry.getFkUnitId() });
            moFieldAccIsSubtotalPctApplying.setBoolean(entry.getIsSubtotalPctApplying());
            itemStateChangedAccIsSubtotalPctApplying(false);
            moFieldAccSubtotalPct.setDouble(entry.getSubtotalPct());
            moFieldAccSubtotalCy.setDouble(entry.getSubtotalCy());
            moFieldAccConcept.setString(entry.getConcept());
            
            moPanelFkCostCenterId_n.getFieldAccount().setFieldValue(entry.getFkCostCenterId_n().isEmpty() ? moPanelFkCostCenterId_n.getEmptyAccountId() : entry.getFkCostCenterId_n());
            moPanelFkCostCenterId_n.refreshPanel();
            
            if (isLocalCurrency()) {
                jtfAccSubtotal.setText("");
            }
            else {
                jtfAccSubtotal.setText(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(entry.getSubtotal()));
            }
        }
        
        mbUpdatingForm = false;
    }
    
    private void renderCustomAccSubtotal() {
        calculateCustomAccSubtotal(null);
        
        if (isLocalCurrency() || mbResetingForm) {
            jtfAccSubtotalSubtotal.setText("");
        }
        else {
            jtfAccSubtotalSubtotal.setText(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(mdAccSubtotal));
        }
        
        jtfAccSubtotalSubtotalCy.setText(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(mdAccSubtotalCy));
        jtfAccSubtotalSubtotalPct.setText(SLibUtils.DecimalFormatPercentage2D.format(mdAccSubtotalPct));
    }
    
    private void renderCfdiFirstRelatedDps() {
        if (moCfdRelatedDocs == null || moCfdRelatedDocs.getRowCfdRelatedDocs().isEmpty()) {
            jtfCfdiFirstRelatedDps.setText("");
        }
        else {
            SRowCfdRelatedDocs firstRow = moCfdRelatedDocs.getRowCfdRelatedDocs().get(0);
            jtfCfdiFirstRelatedDps.setText(firstRow.composeFirstDps(miClient));
            jtfCfdiFirstRelatedDps.setCaretPosition(0);
        }
    }

    private void actionDate() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDate.getDate(), moFieldDate);
    }

    private void actionDateDoc() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateDoc.getDate(), moFieldDateDoc);
    }

    private void actionRecordManualSelect() throws Exception {
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
    
    private void actionDateMaturity() {
        SGuiDate date = miClient.getGuiDatePickerXXX().pickDate(moFieldDateStartCredit.getDate());
        jtfDaysOfCredit.setText(SLibTimeUtilities.getDaysDiff(date, moFieldDateStartCredit.getDate())+ "");
        renderDateMaturity();
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
            moFieldExchangeRate.setFieldValue(rate);
            jtfExchangeRate.requestFocus();
        }
    }

    private void actionComputeTotal() {
        calculateTotal();
    }

    private void actionPrepayments() {
        try {
            miClient.showMsgBoxInformation("Saldo de anticipos del asociado de negocios en la moneda del documento : $ " + SLibUtils.getDecimalFormatAmount().format(mdPrepaymentsCy) + " " + jtfPrepaymentsCyCurRo.getText() + ".\n"
                    + "Saldo global de anticipos totales del asociado de negocios en moneda local: $ " + SLibUtils.getDecimalFormatAmount().format(mdPrepayments) + " " + jtfCurrencySystemKeyRo.getText() + "."); 
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private void actionEntryNew() throws SQLException {
        if (jbEntryNew.isEnabled() && mnFormStatus == SLibConstants.FORM_STATUS_EDIT) {
            SDataDpsEntry entry = null;

            if (jcbTaxRegionId.getSelectedIndex() > 0) {
                updateDpsWithCurrentFormData();

                moFormEntry.formReset();
                moFormEntry.setValue(SDataConstants.TRN_DPS, moDps);
                moFormEntry.setValue(SDataConstants.BPSU_BP, moBizPartner);
                moFormEntry.setValue(SDataConstants.BPSU_BPB, moBizPartnerBranch);
                moFormEntry.setEnableDataAddenda(isCfdAddendaRequired());
                moFormEntry.setValue(SDataConstants.FINU_TAX_REG, ((SFormComponentItem) jcbTaxRegionId.getSelectedItem()).getPrimaryKey());
                moFormEntry.setValue(SFormDpsEntry.LINK_TICKET_SELECT, jtbLinkTicket.isSelected());
                moFormEntry.setValue(SFormDpsEntry.LINKED_TICKET, mvScaleTicDps);
                
                moFormEntry.setFormVisible(true);

                if (moFormEntry.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    entry = (SDataDpsEntry) moFormEntry.getRegistry();

                    moPaneGridEntries.addTableRow(new SDataDpsEntryRow(entry, ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter()));
                    renderEntries();
                    calculateTotal();
                    updateDpsEntryCfdiSettings();
                    setLogisticsData();
                    moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
                }
            }
            else {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + " '" + SGuiUtils.getLabelName(jlTaxRegionId.getText()) + "'.");
                jcbTaxRegionId.requestFocus();
            }
        }
    }

    private void actionEntryEdit() throws SQLException {
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
                    boolean postEmissionEditionAllowed = jbEditLogistics.isEnabled() || mbPostEmissionEdition;
                    
                    if (mnFormStatus == SLibConstants.FORM_STATUS_EDIT) {
                        canEdit = canEditEntry(entry);
                        updateDpsWithCurrentFormData();
                    }

                    moFormEntry.formReset();
                    moFormEntry.setValue(SDataConstants.TRN_DPS, moDps);
                    moFormEntry.setValue(SDataConstants.BPSU_BP, moBizPartner);
                    moFormEntry.setValue(SDataConstants.BPSU_BPB, moBizPartnerBranch);
                    moFormEntry.setValue(SLibConstants.VALUE_POST_EMIT_EDIT, postEmissionEditionAllowed);
                    moFormEntry.setValue(SLibConstants.VALUE_IS_MAT_REQ, mbMatRequestImport);
                    moFormEntry.setValue(SFormDpsEntry.LINK_TICKET_SELECT, jtbLinkTicket.isSelected());
                    if (jbEditLogistics.isEnabled()) {
                        moFormEntry.setValue(SFormDpsEntry.LINK_TICKET_ENABLED, jtbLinkTicket.isEnabled());
                    }
                    moFormEntry.setValue(SFormDpsEntry.LINKED_TICKET, mvScaleTicDps);
                    moFormEntry.setEnableDataAddenda(isCfdAddendaRequired());
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

                        if (mbIsDpsOrder) {
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
                                moFormEntry.enableDpsLinkedEditableFields(mbHasDpsLinksAsSrc);
                                moFormEntry.setQuantityLimit(dQuantityDes, dQuantityAdj, dQuantityPrc, isLastPrc);
                            }
                            else if (hasDpsAdjustmentsAsAdjButIsEditable()) {
                                moFormEntry.enableDpsAdjustedEditableFields();
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
                        
                        if (postEmissionEditionAllowed) {
                            mbPostEmissionEdition = true; // to be sure that a post-emission edition is in progress
                            jbOk.setEnabled(true);
                        }

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
                        updateDpsEntryCfdiSettings();
                        setLogisticsData();
                        moPaneGridEntries.setTableRowSelection(index < moPaneGridEntries.getTableGuiRowCount() ? index : moPaneGridEntries.getTableGuiRowCount() - 1);
                        
                        if (mbIsDpsOrder) {
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

    private void actionEntryDelete() throws SQLException {
        if (jbEntryDelete.isEnabled() && mnFormStatus == SLibConstants.FORM_STATUS_EDIT) {
            boolean delete = true;
            SDataDpsEntry entry = null;
            SDataDpsEntry entryComplementary = null;
            int index = moPaneGridEntries.getTable().getSelectedRow();

            if (index != -1) {
                entry = (SDataDpsEntry) moPaneGridEntries.getTableRow(index).getData();

                if (entry.getFkDpsEntryTypeId() != SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY) {
                    miClient.showMsgBoxWarning("Solamente pueden modificarse partidas de tipo ordinario.");
                }
                else if (entry.getIsRegistryNew() || canDeleteEntry(entry)) {
                    if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                        if (entry.getIsDeleted()) {
                            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_REG_ALREADY_DELETE);
                        }
                        else {
                            if (mbIsDpsOrder) {
                                if (entry.getContractPriceYear() > 0 && entry.getContractPriceMonth() > 0) {
                                    if (!entry.getDbmsDpsLinksAsDestiny().isEmpty()) {
                                        for (SGuiDpsEntryPrice entryPrice : moGuiDpsLink.pickGuiDpsSourceEntry(entry.getDbmsDpsLinksAsDestiny().get(0).getDbmsSourceDpsKey(), entry.getDbmsDpsLinksAsDestiny().get(0).getDbmsSourceDpsEntryKey()).getGuiDpsSourceEntryPrices()) {
                                            if (entry.getContractPriceYear() == entryPrice.getDataDpsEntryPrice().getContractPriceYear() && entry.getContractPriceMonth() == entryPrice.getDataDpsEntryPrice().getContractPriceMonth()) {
                                                entryPrice.removeDataDpsDestinyEntry(entry);
                                                break;
                                            }
                                        }
                                    }
                                    else {
                                        delete = false;
                                        miClient.showMsgBoxWarning("Esta partida está asignada a una entrega mensual " + SLibUtils.DecimalFormatCalendarYear.format(entry.getContractPriceYear()) + "-" + SLibUtils.DecimalFormatCalendarMonth.format(entry.getContractPriceMonth()) +  ", ¡pero no tiene vínculos con ningún contrato!");
                                    }
                                }
                            }
                            
                            if (delete) {
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

                                renderEntries();
                                calculateTotal();
                                updateDpsEntryCfdiSettings();
                                setLogisticsData();
                                moPaneGridEntries.setTableRowSelection(index < moPaneGridEntries.getTableGuiRowCount() ? index : moPaneGridEntries.getTableGuiRowCount() - 1);
                                if (moPaneGridEntries.getTableGuiRowCount() == 0) {
                                    updateCurrencyFieldsStatus(true);
                                }
                            }
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

            updateDpsWithCurrentFormData();
            
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
                    updateDpsWithCurrentFormData();

                    moFormEntry.formReset();
                    moFormEntry.setValue(SDataConstants.TRN_DPS, moDps);
                    moFormEntry.setValue(SDataConstants.BPSU_BP, moBizPartner);
                    moFormEntry.setValue(SDataConstants.BPSU_BPB, moBizPartnerBranch);
                    moFormEntry.setEnableDataAddenda(isCfdAddendaRequired());
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
                        
                        if (moCfdRelatedDocs == null) {
                            moCfdRelatedDocs = new STrnCfdRelatedDocs();
                        }
                        
                        String relTp = DCfdi40Catalogs.ClaveTipoRelaciónNotaCrédito;
                        if (oDpsEntryAdjustment.getOperationsType() == SDataConstantsSys.TRNX_OPS_TYPE_ADJ_APP_PREPAY) {
                            relTp = DCfdi40Catalogs.ClaveTipoRelaciónAplicaciónAnticipo;
                        }
                        
                        moCfdRelatedDocs.addCfdRelatedDoc(relTp, oDpsSource.getDbmsDataCfd().getUuid());
                        jtaCfdiRelatedDocs.setText(moCfdRelatedDocs.toString());
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void actionEntryImportFromDps(final erp.mtrn.data.SDataDps poDpsSource) {
        boolean goAhead = true;
        boolean confirmPriceMsgReq = false;
        boolean addendaReq = false;
        int addendaEntries = 0;
        
        if (jbEntryImportFromDps.isEnabled() && mnFormStatus == SLibConstants.FORM_STATUS_EDIT) {
            int i = 0;
            int[] adjustmentSubtypeKey = null;
            SDataDps oDpsSource = poDpsSource;
            SDataItem oItem = null;

            // If this document is an "adjustment", check that some valid type of adjustment is selected:

            if (mbIsDpsAdjustment) {
                if (jcbAdjustmentSubtypeId.getSelectedIndex() <= 0) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + " '" + jlAdjustmentSubtypeId.getText() + "'.");
                    jcbAdjustmentSubtypeId.requestFocus();
                    return;
                }
                else {
                    adjustmentSubtypeKey = (int[]) ((SFormComponentItem) jcbAdjustmentSubtypeId.getSelectedItem()).getPrimaryKey();
                }
            }

            // Check if addenda is required for document entries:
            
            addendaReq = !SLibUtils.belongsTo(moBizPartnerCategory.getFkCfdAddendaTypeId(), new int[] { SDataConstantsSys.BPSS_TP_CFD_ADD_NA, SDataConstantsSys.BPSS_TP_CFD_ADD_MODELO });

            /*
             * IMPORTING DOCUMENT ENTRIES FROM OTHER DOCUMENTS:
             *
             * If this document is an "order" or a "document", import means "to link".
             * If this document is an "adjustment", import means "to adjust".
             */

            // 1. Pick source DPS:

            if (oDpsSource == null) {
                updateDpsWithCurrentFormData();
                adequateDatesForOrderPrevious();
                
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
                    if (mbIsDpsOrder || mbIsDpsInvoice) {
                        // A.1. Validate that source DPS can be used:

                        if (moDps.getFkBizPartnerBranchId() != oDpsSource.getFkBizPartnerBranchId()) {
                            miClient.showMsgBoxWarning("Este documento y el documento de origen deben coincidir en: sucursal asociado.");
                        }
                        else if (moDps.getFkCurrencyId() != oDpsSource.getFkCurrencyId() && moDps.getFkCurrencyId() != miClient.getSessionXXX().getParamsErp().getFkCurrencyId()) {
                            miClient.showMsgBoxWarning("Este documento y el documento de origen deben coincidir en: moneda del documento.");
                        }
                        else if (moDps.getIsDiscountDocApplying() != oDpsSource.getIsDiscountDocApplying()) {
                            miClient.showMsgBoxWarning("Este documento y el documento de origen deben coincidir en: si aplica o no el descuento en el documento.");
                        }
                        else if (moDps.getIsDiscountDocPercentage() != oDpsSource.getIsDiscountDocPercentage()) {
                            miClient.showMsgBoxWarning("Este documento y el documento de origen deben coincidir en: si es porcentaje o no el descuento en el documento.");
                        }
                        else if (moDps.getDiscountDocPercentage() != oDpsSource.getDiscountDocPercentage()) {
                            miClient.showMsgBoxWarning("Este documento y el documento de origen deben coincidir en: el porcentaje de descuento en el documento.");
                        }
                        else if (oDpsSource.getDate().after(moFieldDate.getDate())) {
                            miClient.showMsgBoxWarning("La fecha del documento de origen (" + SLibUtils.DateFormatDate.format(oDpsSource.getDate()) + ") " +
                                    "no puede ser posterior a la fecha de este documento (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ").");
                        }
                        else {
                            if (moDps.getFkCurrencyId() != oDpsSource.getFkCurrencyId() && moDps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getFkCurrencyId()) {
                                mbIsLocalCurrency = miClient.showMsgBoxConfirm("Se cargará el valor del documento en moneda local,\n ¿Desea continuar?") == JOptionPane.YES_OPTION;
                                
                                if (!mbIsLocalCurrency) {
                                    mbIsLocalCurrency = false;
                                    goAhead = false;
                                }
                            }
                            
                            if (goAhead && mbValidateLinkPeriod && oDpsSource.getDateDocDelivery_n() != null && moFieldDate.getDate().before(oDpsSource.getDateDocDelivery_n())) {
                                if (miClient.showMsgBoxConfirm("La fecha de este documento (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ") " +
                                        "es anterior a la fecha entrega programada del documento de origen (" + SLibUtils.DateFormatDate.format(oDpsSource.getDateDocDelivery_n()) +"),\n ¿Desea continuar?") != JOptionPane.YES_OPTION) {
                                    goAhead = false;
                                }
                            }
                            
                            if (goAhead && mbValidateLinkPeriod && oDpsSource.getDateDocLapsing_n() != null && moFieldDate.getDate().after(oDpsSource.getDateDocLapsing_n())) {
                                if (miClient.showMsgBoxConfirm("La fecha de este documento (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ") " +
                                        "es posterior a la fecha última entrega del documento de origen (" + SLibUtils.DateFormatDate.format(oDpsSource.getDateDocLapsing_n()) +"),\n ¿Desea continuar?") != JOptionPane.YES_OPTION) {
                                    goAhead = false;
                                }
                            }
                            
                            if (goAhead) {
                                // A.2. Remove from just picked source DPS all registries linked to current DPS:

                                for (SDataDpsEntry dspSourceEntry : oDpsSource.getDbmsDpsEntries()) {
                                    if (dspSourceEntry.getIsPriceConfirm()) {
                                        confirmPriceMsgReq = true;
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
                                
                                if (confirmPriceMsgReq) {
                                    miClient.showMsgBoxInformation("Importante: será necesario confirmar precios mensuales del documento.");
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

                                moDialogDpsLink.formReset();
                                if (mbIsDpsOrder) {
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
                                moDialogDpsLink.setValue(SDataConstants.TRNS_CL_DPS, mbIsDpsOrder);
                                moDialogDpsLink.setFormVisible(true);

                                // Complement addenda entries:

                                if (moDialogDpsLink.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                    Vector<SDataDpsEntry> dpsSourceEntries = new Vector<>();

                                    updateDpsWithCurrentFormData();
                                    adequateDatesForOrderPrevious();
                                    
                                    jcbFkPaymentTypeId.setEnabled(!(oDpsSource.getIsCopied() || moDps.getIsCopied()));
                                    jtfDaysOfCredit.setEditable(!(oDpsSource.getIsCopied() || moDps.getIsCopied()));
                                    moDps.setIsCopied(oDpsSource.getIsCopied() || moDps.getIsCopied()); // preserve "is copied" attribute if document already is so!
                                    moDps.setFkSourceYearId_n(moDps.getFkSourceYearId_n() != SLibConstants.UNDEFINED ? moDps.getFkSourceYearId_n() : oDpsSource.getPkYearId());
                                    moDps.setFkSourceDocId_n(moDps.getFkSourceDocId_n() != SLibConstants.UNDEFINED ? moDps.getFkSourceDocId_n() : oDpsSource.getPkDocId());
                                    moDps.getDbmsScaleTickets().clear();
                                    
                                    if (addendaReq) {
                                        DLG_LINK:
                                        for (SDataEntryDpsDpsLink entryLink : (Vector<SDataEntryDpsDpsLink>) moDialogDpsLink.getValue(SDataConstants.TRNX_DPS_DES)) {
                                            for (SDataDpsEntry dpsSourceEntry : oDpsSource.getDbmsDpsEntries()) {
                                                if (SLibUtilities.compareKeys(entryLink.getDpsEntryKey(), dpsSourceEntry.getPrimaryKey())) {
                                                    moFormEntry.formReset();
                                                    moFormEntry.setValue(SDataConstants.TRN_DPS, moDps);
                                                    moFormEntry.setValue(SDataConstants.BPSU_BP, moBizPartner);
                                                    moFormEntry.setValue(SDataConstants.BPSU_BPB, moBizPartnerBranch);
                                                    moFormEntry.setEnableDataAddenda(isCfdAddendaRequired());
                                                    moFormEntry.setRegistry(dpsSourceEntry.clone());
                                                    moFormEntry.setFormVisible(true);

                                                    if (moFormEntry.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                                        dpsSourceEntries.add((SDataDpsEntry) moFormEntry.getRegistry());
                                                        addendaEntries++;
                                                    }
                                                    else {
                                                        break DLG_LINK;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        ETY_SELECTED:
                                        for (SDataEntryDpsDpsLink entryLink : (Vector<SDataEntryDpsDpsLink>) moDialogDpsLink.getValue(SDataConstants.TRNX_DPS_DES)) {
                                            for (SDataDpsEntry dpsSourceEntry : oDpsSource.getDbmsDpsEntries()) {
                                                if (SLibUtilities.compareKeys(entryLink.getDpsEntryKey(), dpsSourceEntry.getPrimaryKey())) {
                                                    if ((jtbLinkTicket.isSelected() && !dpsSourceEntry.getDbmsScaleTicketsEty().isEmpty()) || (!jtbLinkTicket.isSelected() && dpsSourceEntry.getDbmsScaleTicketsEty().isEmpty())) {
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
                                                            /* XXX Check this bug in edition of the orders sales (jbarajas, 2015-08-18)
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
                                                    }
                                                    else {
                                                        miClient.showMsgBoxInformation("No se puede vincular la(s) partida(s) debido a que el modo de vinculación de boletos no coincide con el seleccionado en la captura.");
                                                        dpsSourceEntries.clear();
                                                        break ETY_SELECTED;
                                                    }
                                                    dpsSourceEntries.add(dpsSourceEntry);
                                                }
                                            }
                                        }
                                    }

                                    if (!dpsSourceEntries.isEmpty()) {
                                        if (addendaEntries > 0) {
                                            if (miClient.showMsgBoxConfirm("¿Desea agregar las " + addendaEntries + " partidas importadas?") == JOptionPane.YES_OPTION) {
                                                prepareDpsEntryImport(dpsSourceEntries, oDpsSource, oItem);
                                            }
                                        }
                                        else {
                                            prepareDpsEntryImport(dpsSourceEntries, oDpsSource, oItem);
                                        }

                                        renderEntries();
                                        calculateTotal();
                                        setLogisticsData();
                                        moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
                                    }
                                }
                            }
                        }
                    }
                    else if (mbIsDpsAdjustment) {
                        // B.1. Validate that source DPS can be used:

                        if (moDps.getFkCurrencyId() != oDpsSource.getFkCurrencyId()) {
                            miClient.showMsgBoxWarning("El documento origen y este documento deben coincidir en: moneda del documento.");
                        }
                        else if (oDpsSource.getDate().after(moFieldDate.getDate())) {
                            miClient.showMsgBoxWarning("La fecha del documento origen (" + SLibUtils.DateFormatDate.format(oDpsSource.getDate()) + ") " +
                                    "no puede ser posterior a la fecha de este documento (" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + ").");
                        }
                        else if (oDpsSource.getIsDeleted() || oDpsSource.getFkDpsStatusId() != SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                            miClient.showMsgBoxWarning("El documento origen debe estar emitido.");
                        }
                        else {
                            //BizParther is internacional
                            if (moBizPartner.getFiscalId().equals(DCfdConsts.RFC_GEN_INT)){
                                // document sales
                                if (mnFormType == SDataConstantsSys.TRNS_CT_DPS_SAL) {
                            // B.2. Remove from just picked source DPS all adjustment registries linked to current DPS:
                                    if (oDpsSource.getDbmsDataCfd().getUuid().isEmpty()) {
                                        miClient.showMsgBoxWarning("El documento origen no tiene UUID, por lo cual al emitir el XML de ajuste no tendrá la relación correspondiente.");
                                    }
                                }
                            }
                            
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
                                                
                                                //entry.setContractBase(...);         // contracts have not credit notes!
                                                //entry.setContractFuture(...);       // contracts have not credit notes!
                                                //entry.setContractFactor(...);       // contracts have not credit notes!
                                                //entry.setContractPriceYear(...);    // contracts have not credit notes!
                                                //entry.setContractPriceMonth(...);   // contracts have not credit notes!
                                                entry.setSealQuality(dpsSourceEntry.getSealQuality());
                                                entry.setSealSecurity(dpsSourceEntry.getSealSecurity());
                                                entry.setDriver(dpsSourceEntry.getDriver());
                                                entry.setPlate(dpsSourceEntry.getPlate());
                                                entry.setTicket(dpsSourceEntry.getTicket());
                                                entry.setContainerTank(dpsSourceEntry.getContainerTank());
                                                entry.setVgm("");   // user must update manually verified gross mass if needed!

                                                entry.setOperationsType(STrnUtils.mirrowOperationsType(dpsSourceEntry.getOperationsType()));
                                                entry.setUserId(SLibConstants.UNDEFINED);
                                                entry.setSortingPosition(0);
                                                
                                                entry.setIsPrepayment(dpsSourceEntry.getIsPrepayment());
                                                //entry.setIsDiscountRetailChain(...);  // for discounts only, special case set by GUI
                                                entry.setIsTaxesAutomaticApplying(true);
                                                //entry.setIsPriceVariable(...);    // contracts have not credit notes!
                                                //entry.setIsPriceConfirm(...);     // contracts have not credit notes!
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

                                                entry.setDbmsAddBachocoNumeroPosicion(dpsSourceEntry.getDbmsAddBachocoNumeroPosicion());
                                                entry.setDbmsAddBachocoCentro(dpsSourceEntry.getDbmsAddBachocoCentro());
                                                entry.setDbmsAddLorealEntryNumber(dpsSourceEntry.getDbmsAddLorealEntryNumber());
                                                entry.setDbmsAddSorianaCodigo(dpsSourceEntry.getDbmsAddSorianaCodigo());
                                                entry.setDbmsAddElektraOrder(dpsSourceEntry.getDbmsAddElektraOrder());
                                                entry.setDbmsAddElektraBarcode(dpsSourceEntry.getDbmsAddElektraBarcode());
                                                entry.setDbmsAddElektraCages(dpsSourceEntry.getDbmsAddElektraCages());
                                                entry.setDbmsAddElektraCagePriceUnitary(dpsSourceEntry.getDbmsAddElektraCagePriceUnitary());
                                                entry.setDbmsAddElektraParts(dpsSourceEntry.getDbmsAddElektraParts());
                                                entry.setDbmsAddElektraPartPriceUnitary(dpsSourceEntry.getDbmsAddElektraPartPriceUnitary());
                                                entry.setDbmsAddJsonData(dpsSourceEntry.getDbmsAddJsonData());
                                                entry.setDbmsDpsEntryMatRequest(dpsSourceEntry.getDbmsDpsEntryMatRequestLink());

                                                entry.calculateTotal(miClient, moDps.getDate(),
                                                        moDps.getFkTaxIdentityEmisorTypeId(), moDps.getFkTaxIdentityReceptorTypeId(),
                                                        moDps.getIsDiscountDocPercentage(), moDps.getDiscountDocPercentage(), moDps.getExchangeRate());

                                                if (addendaReq && moBizPartnerCategory.getFkCfdAddendaTypeId() == SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA) {
                                                    moFormEntry.formReset();
                                                    moFormEntry.setValue(SDataConstants.TRN_DPS, moDps);
                                                    moFormEntry.setValue(SDataConstants.BPSU_BP, moBizPartner);
                                                    moFormEntry.setValue(SDataConstants.BPSU_BPB, moBizPartnerBranch);
                                                    moFormEntry.setEnableDataAddenda(isCfdAddendaRequired());
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
                                                
                                                //entry.setContractBase(...);         // contracts have not credit notes!
                                                //entry.setContractFuture(...);       // contracts have not credit notes!
                                                //entry.setContractFactor(...);       // contracts have not credit notes!
                                                //entry.setContractPriceYear(...);    // contracts have not credit notes!
                                                //entry.setContractPriceMonth(...);   // contracts have not credit notes!
                                                entry.setSealQuality(dpsSourceEntry.getSealQuality());
                                                entry.setSealSecurity(dpsSourceEntry.getSealSecurity());
                                                entry.setDriver(dpsSourceEntry.getDriver());
                                                entry.setPlate(dpsSourceEntry.getPlate());
                                                entry.setTicket(dpsSourceEntry.getTicket());
                                                entry.setContainerTank(dpsSourceEntry.getContainerTank());
                                                entry.setVgm("");   // user must update manually verified gross mass if needed!

                                                entry.setOperationsType(STrnUtils.mirrowOperationsType(dpsSourceEntry.getOperationsType()));
                                                entry.setUserId(SLibConstants.UNDEFINED);
                                                entry.setSortingPosition(0);
                                                
                                                entry.setIsPrepayment(dpsSourceEntry.getIsPrepayment());
                                                //entry.setIsDiscountRetailChain(...);  // for discounts only, special case set by GUI
                                                entry.setIsTaxesAutomaticApplying(true);
                                                //entry.setIsPriceVariable(...);    // contracts have not credit notes!
                                                //entry.setIsPriceConfirm(...);     // contracts have not credit notes!
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

                                                entry.setDbmsAddBachocoNumeroPosicion(dpsSourceEntry.getDbmsAddBachocoNumeroPosicion());
                                                entry.setDbmsAddBachocoCentro(dpsSourceEntry.getDbmsAddBachocoCentro());
                                                entry.setDbmsAddLorealEntryNumber(dpsSourceEntry.getDbmsAddLorealEntryNumber());
                                                entry.setDbmsAddSorianaCodigo(dpsSourceEntry.getDbmsAddSorianaCodigo());
                                                entry.setDbmsAddElektraOrder(dpsSourceEntry.getDbmsAddElektraOrder());
                                                entry.setDbmsAddElektraBarcode(dpsSourceEntry.getDbmsAddElektraBarcode());
                                                entry.setDbmsAddElektraCages(dpsSourceEntry.getDbmsAddElektraCages());
                                                entry.setDbmsAddElektraCagePriceUnitary(dpsSourceEntry.getDbmsAddElektraCagePriceUnitary());
                                                entry.setDbmsAddElektraParts(dpsSourceEntry.getDbmsAddElektraParts());
                                                entry.setDbmsAddElektraPartPriceUnitary(dpsSourceEntry.getDbmsAddElektraPartPriceUnitary());
                                                entry.setDbmsAddJsonData(dpsSourceEntry.getDbmsAddJsonData());

                                                entry.calculateTotal(miClient, moDps.getDate(),
                                                        moDps.getFkTaxIdentityEmisorTypeId(), moDps.getFkTaxIdentityReceptorTypeId(),
                                                        moDps.getIsDiscountDocPercentage(), moDps.getDiscountDocPercentage(), moDps.getExchangeRate());

                                                if (addendaReq && moBizPartnerCategory.getFkCfdAddendaTypeId() == SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA) {
                                                    moFormEntry.formReset();
                                                    moFormEntry.setValue(SDataConstants.TRN_DPS, moDps);
                                                    moFormEntry.setValue(SDataConstants.BPSU_BP, moBizPartner);
                                                    moFormEntry.setValue(SDataConstants.BPSU_BPB, moBizPartnerBranch);
                                                    moFormEntry.setEnableDataAddenda(isCfdAddendaRequired());
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
                                if (mbIsDpsAdjustment) {
                                    if (oDpsSource.getDbmsDataCfd() != null) {
                                        if (moCfdRelatedDocs == null) {
                                            moCfdRelatedDocs = new STrnCfdRelatedDocs();
                                        }
                                        
                                        moCfdRelatedDocs.addCfdRelatedDoc(adjustmentSubtypeKey[0] == SDataConstantsSys.TRNS_TP_DPS_ADJ_RET ? DCfdi40Catalogs.ClaveTipoRelaciónDevolución : DCfdi40Catalogs.ClaveTipoRelaciónNotaCrédito, oDpsSource.getDbmsDataCfd().getUuid());
                                        jtaCfdiRelatedDocs.setText(moCfdRelatedDocs.toString());
                                    }
                                }              
                                
                                moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
                                if (moPaneGridEntries.getTableGuiRowCount() > 0) {
                                    updateCurrencyFieldsStatus(false);
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
            updateDpsWithCurrentFormData();
            
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
                    updateDpsWithCurrentFormData();
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
    
    @SuppressWarnings("unchecked")
    private void actionImportEntryFromMatRequest() {
        SDialogDpsMaterialRequestLink oDialog = new SDialogDpsMaterialRequestLink(miClient, moDps.getDpsTypeKey());
        oDialog.setValue(SDataConstants.TRN_DPS, moDps);
        oDialog.setFormVisible(true);
        if (oDialog.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            ArrayList<SDataDpsEntry> lEntries = (ArrayList<SDataDpsEntry>) oDialog.getValue(SDataConstants.TRN_DPS_ETY);
            for (SDataDpsEntry oEntryImported : lEntries) {
                oEntryImported.setFkTaxRegionId(moBizPartnerBranch.getFkTaxRegionId_n() != 0 ? moBizPartnerBranch.getFkTaxRegionId_n() : miClient.getSessionXXX().getParamsCompany().getFkDefaultTaxRegionId_n());
                moPaneGridEntries.addTableRow(new SDataDpsEntryRow(oEntryImported, ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter()));
            }
            
            mbMatRequestImport = true;
            renderEntries();
            calculateTotal();
            try {
                updateDpsEntryCfdiSettings();
            }
            catch (SQLException ex) {
                miClient.showMsgBoxWarning(ex.getMessage());
                Logger.getLogger(SFormDps.class.getName()).log(Level.SEVERE, null, ex);
            }
            moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
        }
    }
    
    private void actionViewMatReqEntryLinks() {
        int index = moPaneGridEntries.getTable().getSelectedRow();
        SDataDpsEntry entry;

        if (index != -1) {
            entry = ((SDataDpsEntry) moPaneGridEntries.getTableRow(index).getData()).clone();

            if (entry.getDbmsDpsEntryMatRequestLink() != null) {
                SDialogDpsEtyMatReq oDialog = new SDialogDpsEtyMatReq(miClient);
                oDialog.setFormParams(entry.getDbmsDpsEntryMatRequestLink().getDbmsMaterialRequestEntryKey(), 
                                        entry.getDbmsDpsEntryMatRequestLink().getDbmsDpsKey(), 
                                        (int[]) moDpsType.getPrimaryKey(), 
                                        entry.getDbmsDpsEntryMatRequestLink().getQuantity());
                oDialog.setVisible(true);
            }
        }
    }
    
    private void actionEntryCopy() {
        if (jbEntryCopy.isEnabled()) {
            try {
                int links;
                int index = moPaneGridEntries.getTable().getSelectedRow();
                SDataDpsEntry entry;

                if (index != -1) {
                    entry = ((SDataDpsEntry) moPaneGridEntries.getTableRow(index).getData()).clone();

                    if (mnFormStatus == SLibConstants.FORM_STATUS_EDIT) {
                        updateDpsWithCurrentFormData();
                    }

                    links = entry.getDbmsDpsAdjustmentsAsDps().size();
                    links += entry.getDbmsDpsAdjustmentsAsAdjustment().size();
                    links += entry.getDbmsDpsLinksAsSource().size();
                    links += entry.getDbmsDpsLinksAsDestiny().size();
                    
                    if (links == 0) {
                        entry.setPkEntryId(0);
                        moPaneGridEntries.addTableRow(new SDataDpsEntryRow(entry, ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter()));
                        renderEntries();
                        calculateTotal();
                        updateDpsEntryCfdiSettings();
                        setLogisticsData();
                        moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
                    }
                    else {
                        miClient.showMsgBoxInformation("No se puede copiar esta partida ya que tiene documentos vinculados");
                    }
                }
            } 
            catch (Exception e) {
                miClient.showMsgBoxWarning(e.getMessage()); 
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
    
    private void actionEditTaxRegion() {
        if (miClient.showMsgBoxConfirm("¿Está seguro(a) que desea habilitar la región de impuestos?\nLa configuración se aplicará para las partidas que se agreguen en adelante.") == JOptionPane.OK_OPTION) {
            jcbTaxRegionId.setEnabled(true);
            jbTaxRegionId.setEnabled(true);
            jbEditTaxRegion.setEnabled(false);
            jcbTaxRegionId.requestFocus();
        }
    }

    private void actionNotesNew() {
        if (jbNotesNew.isEnabled()) {
            moFormNotes.formReset();
            moFormNotes.setValue(SLibConstants.VALUE_POST_EMIT_EDIT, mbPostEmissionEdition);
            moFormNotes.setFormVisible(true);

            if (moFormNotes.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                boolean add = true;
                SDataDpsNotes notes = (SDataDpsNotes) moFormNotes.getRegistry();
                
                if (mbPostEmissionEdition && notes.getIsCfdComplement()) {
                    if (miClient.showMsgBoxConfirm("No se pueden agregar notas que se integren como complemento CFDI Leyendas Fiscales.\n"
                            + "¿Desea agregarlas como notas ordinarias?, si no, las notas serán desechadas.") != JOptionPane.YES_OPTION) {
                        add = false;
                    }
                    else {
                        notes.setIsCfdComplement(false);
                        notes.setCfdComplementDisposition("");
                        notes.setCfdComplementRule("");
                    }
                }
                
                if (add) {
                    moPaneGridNotes.addTableRow(new SDataDpsNotesRow(notes));
                    moPaneGridNotes.renderTableRows();
                    moPaneGridNotes.setTableRowSelection(moPaneGridNotes.getTableGuiRowCount() - 1);
                }
            }
        }
    }

    private void actionNotesEdit() {
        if (jbNotesEdit.isEnabled()) {
            int index = moPaneGridNotes.getTable().getSelectedRow();

            if (index != -1) {
                SDataDpsNotes notes = (SDataDpsNotes) moPaneGridNotes.getSelectedTableRow().getData();
                
                if (mbPostEmissionEdition && notes.getIsCfdComplement()) {
                    miClient.showMsgBoxWarning("Esta nota no se puede modificar porque forma parte del CFD.");
                }
                else {
                    moFormNotes.formReset();
                    moFormNotes.setValue(SLibConstants.VALUE_POST_EMIT_EDIT, mbPostEmissionEdition);
                    moFormNotes.setRegistry(notes);
                    moFormNotes.setFormVisible(true);

                    if (moFormNotes.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                        moPaneGridNotes.setTableRow(new SDataDpsNotesRow(moFormNotes.getRegistry()), index);
                        moPaneGridNotes.renderTableRows();
                        moPaneGridNotes.setTableRowSelection(index < moPaneGridNotes.getTableGuiRowCount() ? index : moPaneGridNotes.getTableGuiRowCount() - 1);
                    }
                }
            }
        }
    }

    private void actionNotesDelete() {
        if (jbNotesDelete.isEnabled()) {
            int index = moPaneGridNotes.getTable().getSelectedRow();

            if (index != -1) {
                SDataDpsNotes notes = (SDataDpsNotes) moPaneGridNotes.getSelectedTableRow().getData();
                
                if (mbPostEmissionEdition && notes.getIsCfdComplement()) {
                    miClient.showMsgBoxWarning("Esta nota no se puede eliminar porque forma parte del CFD.");
                }
                else {
                    if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
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
    }

    private void actionNotesFilter() {
        if (jtbNotesFilter.isEnabled()) {
            int index = moPaneGridNotes.getTable().getSelectedRow();

            moPaneGridNotes.setGridViewStatus(!jtbNotesFilter.isSelected() ? STableConstants.VIEW_STATUS_ALL : STableConstants.VIEW_STATUS_ALIVE);
            moPaneGridNotes.renderTableRows();
            moPaneGridNotes.setTableRowSelection(index < moPaneGridNotes.getTableGuiRowCount() ? index : moPaneGridNotes.getTableGuiRowCount() - 1);
        }
    }

    private void actionPickSystemNotes() {
        String option = miClient.pickOption(SDataConstants.TRN_SYS_NTS, new int[] { moDps.getFkDpsCategoryId(), moDps.getFkDpsClassId(), moDps.getFkDpsTypeId(), moDps.getFkCurrencyId() });

        if (!option.isEmpty()) {
            SDataDpsNotes notes = new SDataDpsNotes();

            notes.setNotes(option);
            notes.setIsAllDocs(true);
            notes.setIsPrintable(true);
            notes.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
            
            // identify system notes, when possible:
            
            SDataSystemNotes systemNotes = null;
            
            for (SDataSystemNotes sn : maSystemNotes) {
                if (option.equals(sn.getNotes())) {
                    systemNotes = sn;
                    break;
                }
            }
            
            if (systemNotes != null) {
                notes.setIsCfdComplement(systemNotes.getIsCfdComplement());
                notes.setCfdComplementDisposition(systemNotes.getCfdComplementDisposition());
                notes.setCfdComplementRule(systemNotes.getCfdComplementRule());
            }
            
            // add notes:
            
            boolean add = true;

            if (mbPostEmissionEdition && notes.getIsCfdComplement()) {
                if (miClient.showMsgBoxConfirm("No se pueden agregar notas que se integren como complemento CFDI Leyendas Fiscales.\n"
                        + "¿Desea agregarlas como notas ordinarias?, si no, las notas serán desechadas.") != JOptionPane.YES_OPTION) {
                    add = false;
                }
                else {
                    notes.setIsCfdComplement(false);
                    notes.setCfdComplementDisposition("");
                    notes.setCfdComplementRule("");
                }
            }
            
            if (add) {
                moPaneGridNotes.addTableRow(new SDataDpsNotesRow(notes));
                moPaneGridNotes.renderTableRows();
                moPaneGridNotes.setTableRowSelection(0);
            }
        }
    }
    
    private void actionEditNotes() {
        mbPostEmissionEdition = true;
        
        jbEditNotes.setEnabled(false);           
        jbOk.setEnabled(true); // allow post-emission-edition changes to be saved
        
        jbNotesNew.setEnabled(true);
        jbNotesEdit.setEnabled(true);
        jbNotesDelete.setEnabled(true);
        jbPickSystemNotes.setEnabled(true);
        
        jbNotesNew.requestFocusInWindow();
    }
    
    private void actionCfdCceCalcTotalUsd() {
        double xrtUsd = moFieldCfdCceExchangeRateUsd.getDouble();
        moFieldCfdCceTotalUsd.setFieldValue(SLibUtils.roundAmount(xrtUsd == 0 ? 0.0 : moDps.getTotal_r() / xrtUsd));
        jtfCfdCceTotalUsd.requestFocusInWindow();
    }
    
    @SuppressWarnings("deprecation")
    private void actionLoadFileXml() {
        FileFilter filter = new FileNameExtensionFilter("XML file", "xml");
        miClient.getFileChooser().repaint();
        miClient.getFileChooser().setAcceptAllFileFilterUsed(false);
        miClient.getFileChooser().setFileFilter(filter);
       
        try {
            if (miClient.getFileChooser().showOpenDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION ) {
                if (miClient.getFileChooser().getSelectedFile().getName().toLowerCase().contains(".xml")) {
                    String absolutePath = miClient.getFileChooser().getSelectedFile().getAbsolutePath();
                    
                    if (SCfdUtils.checkCompanyAsCfdiReceptor(miClient, absolutePath)) {
                        String cfdi = SXmlUtils.readXml(absolutePath);
                        float version = DCfdUtils.getCfdiVersion(cfdi);
                        if (version == DCfdConsts.CFDI_VER_40) {
                            msXmlUuid = "";
                            moComprobante40 = DCfdUtils.getCfdi40(cfdi);
                            cfd.ver40.DElementTimbreFiscalDigital tfd = moComprobante40.getEltOpcComplementoTimbreFiscalDigital();

                            if (tfd != null) {
                                msXmlUuid = tfd.getAttUUID().getString();
                            }

                            if (jcbNumberSeries.getSelectedItem().toString().isEmpty()) {
                                if (!moComprobante40.getAttSerie().getString().isEmpty()) {
                                    jcbNumberSeries.setSelectedItem(moComprobante40.getAttSerie().getString());
                                }
                            }

                            if (jtfNumber.getText().isEmpty()) {
                                if (!moComprobante40.getAttFolio().getString().isEmpty()) {
                                    jtfNumber.setText(moComprobante40.getAttFolio().getString());
                                }
                                else {
                                    jtfNumber.setText(SLibUtils.textLeft(msXmlUuid, UUID_FIRST_SECC_LENGHT));
                                }
                            }
                        }
                        else if (version == DCfdConsts.CFDI_VER_33) {
                            msXmlUuid = "";
                            moComprobante33 = DCfdUtils.getCfdi33(cfdi);
                            cfd.ver33.DElementTimbreFiscalDigital tfd = moComprobante33.getEltOpcComplementoTimbreFiscalDigital();

                            if (tfd != null) {
                                msXmlUuid = tfd.getAttUUID().getString();
                            }

                            if (jcbNumberSeries.getSelectedItem().toString().isEmpty()) {
                                if (!moComprobante33.getAttSerie().getString().isEmpty()) {
                                    jcbNumberSeries.setSelectedItem(moComprobante33.getAttSerie().getString());
                                }
                            }

                            if (jtfNumber.getText().isEmpty()) {
                                if (!moComprobante33.getAttFolio().getString().isEmpty()) {
                                    jtfNumber.setText(moComprobante33.getAttFolio().getString());
                                }
                                else {
                                    jtfNumber.setText(SLibUtils.textLeft(msXmlUuid, UUID_FIRST_SECC_LENGHT));
                                }
                            }
                        }
                        else {
                            miClient.showMsgBoxInformation("Solo se pueden leer los CFDI versión 3.3 y 4.0."); 
                        }
                        if (moComprobante33 != null || moComprobante40 != null) {
                            moFieldCfdiXmlFile.setFieldValue(miClient.getFileChooser().getSelectedFile().getName());
                            msFileXmlJustLoaded = absolutePath;
                            jckValidateOnSaveFileXml.setEnabled(true);
                            jckValidateOnSaveFileXml.setSelected(true);
                        }
                    }
                }
                else {
                    miClient.showMsgBoxInformation("El archivo sólo puede ser XML.");
                }
            }
            miClient.getFileChooser().resetChoosableFileFilters();
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }
    
    private void actionLoadFilePdf() {
        FileFilter filter = new FileNameExtensionFilter("PDF file", "pdf");
        miClient.getFileChooser().repaint();
        miClient.getFileChooser().setAcceptAllFileFilterUsed(false);
        miClient.getFileChooser().setFileFilter(filter);
       
        try {
            if (miClient.getFileChooser().showOpenDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION ) {
                if (miClient.getFileChooser().getSelectedFile().getName().toLowerCase().contains(".pdf")) {
                    moFieldCfdiPdfFile.setFieldValue(miClient.getFileChooser().getSelectedFile().getName());
                    moFilePdfJustLoaded = new File(miClient.getFileChooser().getSelectedFile().getAbsolutePath());
                }
                else {
                    miClient.showMsgBoxInformation("El archivo sólo puede ser PDF.");
                }
            }
            miClient.getFileChooser().resetChoosableFileFilters();
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }
    
    private void actionDeleteFileXml() {
        msXmlUuid = "";
        moComprobante33 = null;
        moComprobante40 = null;
        moFieldCfdiXmlFile.setFieldValue("");
        msFileXmlJustLoaded = "";
        jckValidateOnSaveFileXml.setEnabled(false);
        jckValidateOnSaveFileXml.setSelected(false);
    }
    
    private void actionDeleteBillOfLading() {
        moFieldCfdiBillOfLading.setFieldValue("");
        moBillOfLading = null;
    }
    
    private void actionDeleteFilePdf() {
        moFieldCfdiPdfFile.setFieldValue("");
        moFilePdfJustLoaded = null;
    }
    
    private void actionCfdiRelatedDocs() {
        if (moDialogCfdRelatedDocs == null) {
            moDialogCfdRelatedDocs = new SDialogCfdRelatedDocs(miClient, moDps);
        }
        else {
            moDialogCfdRelatedDocs.formReset();
        }
        
        if (moCfdRelatedDocs != null) {
            moDialogCfdRelatedDocs.setValue(SDialogCfdRelatedDocs.CFD_RELATED, moCfdRelatedDocs);
        }
        
        moDialogCfdRelatedDocs.setFormVisible(true);
        if (moDialogCfdRelatedDocs.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moCfdRelatedDocs = (STrnCfdRelatedDocs) moDialogCfdRelatedDocs.getValue(SDialogCfdRelatedDocs.CFD_RELATED);
            jtaCfdiRelatedDocs.setText(moCfdRelatedDocs.toString());
            
            renderCfdiFirstRelatedDps();
        }
    }
    
    private void actionBillOfLading() {
        try {
            if (moDialogPickerBillOfLading == null) {
                moDialogPickerBillOfLading = new SDialogPickerDps(miClient, SModConsts.LOG_BOL);
            }

            moDialogPickerBillOfLading.formReset();
            moDialogPickerBillOfLading.setFilterKey(SLibTimeUtils.digestYear(moFieldDate.getDate()));
            moDialogPickerBillOfLading.formRefreshOptionPane();
            moDialogPickerBillOfLading.setFormVisible(true);

            if (moDialogPickerBillOfLading.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                isBolAlreadySelected((int[]) moDialogPickerBillOfLading.getSelectedPrimaryKey());
                moBillOfLading = new SDbBillOfLading();
                moBillOfLading.read(miClient.getSession(), (int[]) moDialogPickerBillOfLading.getSelectedPrimaryKey() );

                if (moBillOfLading.getDataCfd() == null) {
                    miClient.showMsgBoxWarning("El documento " + moBillOfLading.getNumber() + " no cuenta con CFD.");
                }
                else {
                    moFieldCfdiBillOfLading.setString(
                            (moBillOfLading.getSeries().isEmpty() ? moBillOfLading.getNumber() : moBillOfLading.getSeries() + moBillOfLading.getNumber()) + " - " + 
                            SLibUtils.DateFormatDate.format(moBillOfLading.getDate()));
                }
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxWarning(e.getMessage());
        }
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
            renderSalesAgentOfDocument(new int[] { mnSalesAgentId_n });
        }
    }

    private void actionSalesSupervisor() {
        SFormOptionPickerInterface picker = miClient.getOptionPicker(SDataConstants.BPSX_BP_ATT_SAL_AGT);

        picker.formReset();
        picker.formRefreshOptionPane();
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            mnSalesSupervisorId_n = ((int[]) picker.getSelectedPrimaryKey())[0];
            renderSalesSupervisorOfDocument(new int[] { mnSalesSupervisorId_n });
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

    private void actionEditLogistics() {       
        if (mvScaleTicDps.isEmpty()) {
            moFormCom.formReset();
            moFormCom.setRegistry(moDps);
            moFormCom.setVisible(true);

            if (moFormCom.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                mbPostEmissionEdition = true;

                SDataDps dps = (SDataDps) moFormCom.getRegistry(); // convenience variable, dps is the same object as moDps!

                moFieldFkCarrierTypeId.setFieldValue(new int[] { dps.getFkCarrierTypeId() });
                itemStateChangedFkCarrierTypeId();
                moFieldFkCarrierId_n.setFieldValue(new int[] { dps.getFkCarrierId_n() });
                moFieldFkVehicleTypeId_n.setFieldValue(new int[] { dps.getFkVehicleTypeId_n() });
                itemStateChangedFkVehicleTypeId_n();
                moFieldFkVehicleId_n.setFieldValue(new int[] { dps.getFkVehicleId_n() });

                moFieldDriver.setFieldValue(dps.getDriver());
                moFieldPlate.setFieldValue(dps.getPlate());
                moFieldTicket.setFieldValue(dps.getTicket());

                mnSalesAgentId_n = dps.getFkSalesAgentId_n();
                renderSalesAgentOfDocument(new int[] { mnSalesAgentId_n });

                mnSalesSupervisorId_n = dps.getFkSalesSupervisorId_n();
                renderSalesSupervisorOfDocument(new int[] { mnSalesSupervisorId_n });

                // fields shoud not be editable in this context (post-emission edition):
                jcbFkCarrierTypeId.setEnabled(false);
                jcbFkCarrierId_n.setEnabled(false);
                jcbFkVehicleTypeId_n.setEnabled(false);
                jcbFkVehicleId_n.setEnabled(false);
                jcbDriver.setEnabled(false);
                jcbPlate.setEnabled(false);
                jtfTicket.setEditable(false);
                jtfTicket.setFocusable(false);
                jtbLinkTicket.setEnabled(false);

                jbOk.setEnabled(true); // allow post-emission-edition changes to be saved
            }
        }
        else {
            jtbLinkTicket.setEnabled(true);
            jtbLinkTicket.setSelected(true);
            jbOk.setEnabled(true);
        }
    }
    
    private void actionSetTime() {
        try {
            if (moDialogDpsTime == null) {
                moDialogDpsTime = new SDialogDpsTime(miClient);
            }
            moDialogDpsTime.setFormVisible(true);
            if (moDialogDpsTime.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                manDpsTime = moDialogDpsTime.getValue();
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxWarning(e.getMessage());
        }
    }
    
    private boolean canSwitchOnCustomAcc() {
        boolean canSwitchOn = true;
        
        if (jcbFkCurrencyId.getSelectedIndex() <= 0) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkCurrencyId.getText() + "',\n"
                    + "a fin de personalizar la contabilización del documento.");
            jcbFkCurrencyId.requestFocusInWindow();
            canSwitchOn = false;
        }
        else if (moPaneGridEntries.getGridRows().isEmpty()) {
            miClient.showMsgBoxWarning("El documento debe tener al menos una partida,\n"
                    + "a fin de personalizar su contabilización.");
            jTabbedPane.setSelectedIndex(TAB_ETY);
            canSwitchOn = false;
        }
        else {
            ENTRIES:
            for (STableRow row : moPaneGridEntries.getGridRows()) {
                switch (((SDataDpsEntry) row.getData()).getOperationsType()) {
                    case SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS:               // operations
                    case SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS_APP_PREPAY:    // operations - application of advance invoiced as discount
                    case SDataConstantsSys.TRNX_OPS_TYPE_ADJ_OPS:               // adjustment of operations
                    case SDataConstantsSys.TRNX_OPS_TYPE_ADJ_OPS_APP_PREPAY:    // adjustment of operations - application of advance invoiced as discount
                        continue;
                    case SDataConstantsSys.TRNX_OPS_TYPE_OPS_PREPAY:        // prepayments invoiced
                    case SDataConstantsSys.TRNX_OPS_TYPE_ADJ_PREPAY:        // adjustment of prepayments invoiced
                    case SDataConstantsSys.TRNX_OPS_TYPE_ADJ_APP_PREPAY:    // application of prepayments invoiced
                        miClient.showMsgBoxWarning("Todas las partidas del documento deben ser de operaciones o de operaciones con aplicación de anticipo facturado,\n"
                                + "a fin de personalizar su contabilización.");
                        jTabbedPane.setSelectedIndex(TAB_ETY);
                        canSwitchOn = false;
                        break ENTRIES;
                    default:
                        // nothing
                }
            }
        }
        
        return canSwitchOn;
    }
    
    @SuppressWarnings("unchecked")
    private void prepareCustomAcc(final boolean invokeEntryCancel) {
        if (!mbCustomAccPrepared) {
            try {
                mbUpdatingForm = true;
                
                getRootPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                
                // prepate tab settings:

                jtfAccSubtotalCur.setText(miClient.getSession().getSessionCustom().getLocalCurrencyCode());

                if (isLocalCurrency()) {
                    jtfAccSubtotalCyCur.setText(miClient.getSession().getSessionCustom().getLocalCurrencyCode());
                }
                else {
                    jtfAccSubtotalCyCur.setText(miClient.getSession().getSessionCustom().getCurrencyCode(new int[] { moDps.getFkCurrencyId() }));
                }

                jtfAccSubtotalSubtotalCur.setText(jtfAccSubtotalCur.getText());
                jtfAccSubtotalSubtotalCyCur.setText(jtfAccSubtotalCyCur.getText());

                SFormUtilities.populateComboBox(miClient, jcbAccItem, SDataConstants.ITMU_ITEM);
                SFormUtilities.populateComboBox(miClient, jcbAccItemRef_n, SDataConstants.ITMU_ITEM);
                
                if (invokeEntryCancel) {
                    actionCustomAccEntryCancel(true, true);
                }

                // preparation done:
                mbCustomAccPrepared = true;
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
            finally {
                getRootPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                
                mbUpdatingForm = false;
            }
        }
    }
    
    private void dismissCustomAcc() {
        try {
            mbUpdatingForm = true;

            getRootPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            
            // dismiss tab settings:

            moDps.getDbmsDpsCustomAccEntries().clear();
            moPaneGridCustomAcc.clearTableRows();
            
            // preparation ready to be started again:
            mbCustomAccPrepared = false;
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            getRootPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            mbUpdatingForm = false;
        }
    }
    
    /**
     * Switch on/of customized accounting.
     * 
     * ================================================================================
     * Personalización de la contabilización del documento (facturas y NC).
     * ================================================================================
     * Fecha: 2023-10-12
     * Autor: Sergio Abraham FLORES GUTIÉRREZ
     * 
     * OBJETIVO:
     * Permitir contabilizar de forma discrecional el egreso o ingreso de una factura o NC, tanto de compras como de ventas.
     * 
     * Funcionalidades:
     * + Hacer disponible la opción de personalización de la contabilización sólo en facturas y NC.
     * + Validar que el documento, si es factura, no tenga partidas de facturación de anticipos.
     * + Validar que el documento, si es NC, no tenga ajustes de facturación de anticipos o aplicaciones de anticipos facturados.
     * + Repartir el subtotal del documento (importe antes de impuestos) en una o más partidas de contabilización independientes de sus partidas originales.
     * + Las partidas de contabilización sólo se usan en la contabilización del documento.
     * + Las partidas originales se usan para todas las funcionalidades del sistema, p. ej., movimientos de almacén, y para los reportes relacionados con documentos.
     * 
     * + Lógica de captura de la personalización de la contabilización del documento:
     * - Indicar que se opta por personalizar la contabilización del documento dando clic en el botón correspondiente.
     * - Iniciar la captura de partidas de contabilización dando clic en el botón Crear o Copiar, para habilitar los campos de captura.
     * - Capturar el ítem, ítem de referencia (opcional), centro de costo (opcional), unidad de medida, cantidad y concepto contable.
     * - Indicar la forma de repartir la proporción del subtotal del documento que corresponde a la partida de contabilización mediante la modalidad de porcentaje o monto directo.
     * - Sólo es posible elegir la modalidad de porcentaje o monto directo en captura de la primer partida de contabilización.
     * - Guardar los cambios, inhabilitando los campos de captura de partidas de contabilización.
     * - Se puede modificar la partida de contabilización seleccionada dando clic en el botón Modificar.
     * - Se puede eliminar la partida de contabilización seleccionada dando clic en el botón Eliminar.
     * - Se debe actualizar la suma del subtotal personalizado en porcentaje o monto o ambos, según aplique.
     * - Se puede agilizar la captura del porcentaje o proporción del subtotal de la última partida agregando el remanente en automático dando clic en el botón Ejecutar ayudante.
     * - Se puede deseleccionar la opción de personalización de la contabilización, y se eliminarán todas las partidas de contabilización que hayan sido capturadas.
     * - Al guardarse el documento, y si se optó por personalizar su contabilización, se debe validar que:
     * - no haya una captura de partida de contabilización en marcha;
     * - la suma del subtotal personalizado sea 100% o igual al subtotal del documento, según aplique.
     * 
     * + Al guardarse el documento, y si tiene partidas de contabilización:
     * - la contabilización de los impuestos y la afectación del saldo del asociado de negocios se hace en base a las partidas originales del documento;
     * - la contabilización del egreso o ingreso se hace en base a las partidas de contabilización.
     * 
     * NOTA: Por el momento sólo se implementa para facturas, dado que para NC es necesario agregar la suguiente información a cada partida de contabilización personalizada:
     * 1. tipo de ajuste (desctuento, devolución, etc.)
     * 2. partida de factura afectada (esto en particular requiere de mayor análisis especialmente en los casos en que el documento tenga un número distinto de partidas originales que partidas de contabilización personalizada.)
     * ================================================================================
     */
    private void actionSwitchCustomAcc() {
        if (jtbSwitchCustomAcc.isEnabled()) {
            if (jtbSwitchCustomAcc.isSelected()) {
                // switch just set on:
                
                String confirmation = "¿Está seguro que desea habilitar la contabilización personalizada del documento?";
                
                if (miClient.showMsgBoxConfirm(confirmation) == JOptionPane.YES_OPTION && canSwitchOnCustomAcc()) { // if YES selected & swtiching on allowed
                    // first prepare tab settings, then enable tab:

                    prepareCustomAcc(true);

                    jTabbedPane.setEnabledAt(TAB_ACC, true);
                    jTabbedPane.setSelectedIndex(TAB_ACC);
                }
                else {
                    jtbSwitchCustomAcc.setSelected(false); // switch off again
                }
            }
            else {
                // switch just set off:
                
                String confirmation = "¿Está seguro que desea deshabilitar la contabilización personalizada del documento?"
                        + (!moPaneGridCustomAcc.getGridRows().isEmpty() ? "\n¡Todas las partidas de personalización de contabilización existentes serán eliminadas!" : "");
                
                if (miClient.showMsgBoxConfirm(confirmation) == JOptionPane.YES_OPTION) { // if YES selected
                    // first disable tab, then dismiss tab settings:
                    
                    jTabbedPane.setEnabledAt(TAB_ACC, false);
                    jTabbedPane.setSelectedIndex(TAB_ETY);
                    
                    dismissCustomAcc();
                }
                else {
                    jtbSwitchCustomAcc.setSelected(true); // switch on again
                }
            }
        }
    }
    
    private void actionLinkTicket() {
        if (jtbLinkTicket.isEnabled()) {
            if (!jtfTicket.getText().isEmpty()) {
                if (miClient.showMsgBoxConfirm("Ya hay boletos capturados, si cambia el modo de captura los datos se perderán\n¿Desea continuar?") == JOptionPane.OK_OPTION) {
                    jcbDriver.setEnabled(!jtbLinkTicket.isSelected());
                    jcbPlate.setEnabled(!jtbLinkTicket.isSelected());
                    jtfTicket.setEnabled(!jtbLinkTicket.isSelected());   
                    jcbDriver.removeAllItems();
                    jcbPlate.removeAllItems();
                    jtfTicket.setText("");
                    mvScaleTicDps.clear();
                    for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
                        SDataDpsEntry entry = (SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData();
                        entry.setDriver("");
                        entry.setPlate("");
                        entry.setTicket("");
                        entry.getDbmsScaleTicketsEty().clear();                        
                    }
                }
                else {
                    jtbLinkTicket.removeActionListener(this);
                    jtbLinkTicket.setEnabled(!jtbLinkTicket.isSelected());
                    jtbLinkTicket.addActionListener(this);
                }
            }
            else {
                jcbDriver.setEnabled(!jtbLinkTicket.isSelected());
                jcbPlate.setEnabled(!jtbLinkTicket.isSelected());
                jtfTicket.setEnabled(!jtbLinkTicket.isSelected());           
                jcbDriver.removeAllItems();
                jcbPlate.removeAllItems();
                jtfTicket.setText("");
                mvScaleTicDps.clear();
            }
        }
    }
    
    private void actionPickAccItem() {
        mbUpdatingForm = true;
        
        if (miClient.pickOption(SDataConstants.ITMX_ITEM_IOG, moFieldAccItem, null) == SLibConstants.FORM_RESULT_OK) {
            itemStateChangedAccItem(true);
        }
        
        mbUpdatingForm = false;
    }
    
    private void actionPickAccItemRef_n() {
        miClient.pickOption(SDataConstants.ITMX_ITEM_IOG, moFieldAccItemRef_n, null);
    }
    
    private void actionPickAccUnit() {
        miClient.pickOption(SDataConstants.ITMU_UNIT, moFieldAccUnit, new int[] { moAccItem.getDbmsDataItemGeneric().getFkUnitTypeId() });
    }
    
    private void actionExeWizardAccSubtotal() {
        JComponent component;
        
        calculateCustomAccSubtotal(mnAccCurrentAction == ACTION_EDIT ? (SDataDpsCustomAccEntry) moPaneGridCustomAcc.getSelectedTableRow().getData() : null);
        
        if (jckAccIsSubtotalPctApplying.isSelected()) {
            if (mdAccSubtotalPct >= 1d) {
                moFieldAccSubtotalPct.setDouble(0d);
            }
            else {
                moFieldAccSubtotalPct.setDouble(SLibUtils.round(1d - mdAccSubtotalPct, DECS_PCT));
            }

            component = moFieldAccSubtotalPct.getComponent();
            
            focusLostAccSubtotalPct(); // triggers entry's subtotals calculation
        }
        else {
            if (mdAccSubtotalCy >= moDps.getSubtotalCy_r()) {
                moFieldAccSubtotalCy.setDouble(0d);
            }
            else {
                moFieldAccSubtotalCy.setDouble(SLibUtils.roundAmount(moDps.getSubtotalCy_r() - mdAccSubtotalCy));
            }
            
            component = moFieldAccSubtotalCy.getComponent();
        }
        
        component.requestFocusInWindow();
    }
    
    private void startCustomAccEntryInput(final int currentAction) {
        mnAccCurrentAction = currentAction;
        
        enableCustomAccControls(false, true);
        
        jbPickAccItem.setEnabled(true);
        jcbAccItem.setEnabled(true);
        jcbAccItem.requestFocusInWindow();
    }
    
    private void actionCustomAccEntryNew() {
        if (jbCustomAccEntryNew.isEnabled()) {
            startCustomAccEntryInput(ACTION_NEW);
            renderCustomAccEntry(null);
        }
    }
    
    private void actionCustomAccEntryCopy() {
        if (jbCustomAccEntryCopy.isEnabled()) {
            if (moPaneGridCustomAcc.getSelectedTableRow() == null) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                startCustomAccEntryInput(ACTION_NEW);
                enableCustomAccFields(true, false);
            }
        }
    }
    
    private void actionCustomAccEntryEdit() {
        if (jbCustomAccEntryEdit.isEnabled()) {
            if (moPaneGridCustomAcc.getSelectedTableRow() == null) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                startCustomAccEntryInput(ACTION_EDIT);
                enableCustomAccFields(true, false);
            }
        }
    }
    
    private void actionCustomAccEntryDelete() {
        if (jbCustomAccEntryDelete.isEnabled()) {
            if (moPaneGridCustomAcc.getSelectedTableRow() == null) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int index = moPaneGridCustomAcc.getTable().getSelectedRow();
                moPaneGridCustomAcc.getGridRows().remove(moPaneGridCustomAcc.getTable().convertRowIndexToModel(index));
                moPaneGridCustomAcc.renderTableRows();
                moPaneGridCustomAcc.setTableRowSelection(index < moPaneGridNotes.getTableGuiRowCount() ? index : index - 1);
                
                renderCustomAccSubtotal();
            }
        }
    }
    
    private void actionCustomAccEntryOk() {
        if (jbCustomAccEntryOk.isEnabled()) {
            boolean error = false;
            String message = "";
            JComponent component = null;
            
            // validate user input:
            
            ArrayList<SFormField> fields = new ArrayList<>();
            fields.add(moFieldAccItem);
            fields.add(moFieldAccItemRef_n);
            fields.add(moFieldAccQuantity);
            fields.add(moFieldAccUnit);
            fields.add(moFieldAccIsSubtotalPctApplying);
            fields.add(moFieldAccSubtotalPct);
            fields.add(moFieldAccSubtotalCy);
            fields.add(moFieldAccConcept);
            
            for (SFormField field : fields) {
                if (!field.validateField()) {
                    error = true;
                    // message = ... message already displayed to user on field validation
                    component = field.getComponent();
                    break;
                }
            }
            
            boolean isCostCenterEmpty = moPanelFkCostCenterId_n.isEmptyAccountId() || moPanelFkCostCenterId_n.getCurrentInputCostCenter() == null;
            double accSubtotal = getAccSubtotal();
            
            if (!error) {
                if (moAccItem == null) {
                    error = true;
                    message = "El ítem relacionado con el campo '" + jlAccItem.getText() + "' no existe.";
                    component = moFieldAccItem.getComponent();
                }
                else if (moAccItem.getIsPrepayment()) {
                    error = true;
                    message = SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlAccItem.getText() + "', no puede ser 'anticipo'.";
                    component = moFieldAccItem.getComponent();
                }
                else {

                    if (!isCostCenterEmpty) {
                        String validation = SDataUtilities.validateCostCenter(miClient, moPanelFkCostCenterId_n.getCurrentInputCostCenter(), moDps.getDate());

                        if (!validation.isEmpty()) {
                            error = true;
                            message = validation;
                            component = moPanelFkCostCenterId_n.getFieldAccount().getComponent();
                        }
                    }

                    if (!error) {
                        // validate subtotal of customized accounting:

                        calculateCustomAccSubtotal(mnAccCurrentAction == ACTION_EDIT ? (SDataDpsCustomAccEntry) moPaneGridCustomAcc.getSelectedTableRow().getData() : null);

                        if (jckAccIsSubtotalPctApplying.isSelected() && SLibUtils.round(mdAccSubtotalPct + moFieldAccSubtotalPct.getDouble(), DECS_PCT) > 1d) {
                            error = true;
                            message = SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jckAccIsSubtotalPctApplying.getText() + "':\n"
                                    + "no puede ser mayor a " + SLibUtils.DecimalFormatPercentage2D.format(SLibUtils.round(1d - mdAccSubtotalPct, DECS_PCT)) + ", "
                                    + "porque se excede el valor máximo " + SLibUtils.DecimalFormatPercentage2D.format(1d) + ".";
                            component = moFieldAccSubtotalPct.getComponent().isEnabled() ? moFieldAccSubtotalPct.getComponent() : null;
                        }
                        else if (SLibUtils.roundAmount(mdAccSubtotalCy + moFieldAccSubtotalCy.getDouble()) > moDps.getSubtotalCy_r()) {
                            error = true;
                            message = SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlAccSubtotal.getText() + "':\n"
                                    + "no puede ser mayor a $" + SLibUtils.getDecimalFormatAmount().format(SLibUtils.roundAmount(moDps.getSubtotalCy_r() - mdAccSubtotalCy)) + ", "
                                    + "porque se excede el subtotal del documento en la moneda original $" + SLibUtils.getDecimalFormatAmount().format(moDps.getSubtotalCy_r()) + " " + jtfAccSubtotalCyCur.getText() + ".";
                            component = moFieldAccSubtotalCy.getComponent();
                        }
                        else if (!isLocalCurrency() && SLibUtils.roundAmount(mdAccSubtotal + accSubtotal) > moDps.getSubtotal_r()) {
                            error = true;
                            message = SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlAccSubtotal.getText() + "':\n"
                                    + "no puede ser mayor a $" + SLibUtils.getDecimalFormatAmount().format(SLibUtils.roundAmount(moDps.getSubtotal_r() - mdAccSubtotal)) + ", "
                                    + "porque se excede el subtotal del documento en la moneda local $" + SLibUtils.getDecimalFormatAmount().format(moDps.getSubtotal_r()) + " " + jtfAccSubtotalCur.getText() + ".";
                            //component = ... there is not any field for local-currency subtotal
                        }
                    }
                }
            }
            
            if (error) {
                jTabbedPane.setSelectedIndex(TAB_ACC);
                
                if (!message.isEmpty()) {
                    miClient.showMsgBoxWarning(message);
                }
                
                if (component != null) {
                    component.requestFocusInWindow();
                }
            }
            else {
                // save entry:

                SDataDpsCustomAccEntry entry;
                SDataItem itemRef_n = jcbAccItemRef_n.getSelectedIndex() <= 0 ? null : (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, moFieldAccItemRef_n.getKeyAsIntArray(), SLibConstants.EXEC_MODE_VERBOSE);

                switch (mnAccCurrentAction) {
                    case ACTION_NEW:
                        entry = new SDataDpsCustomAccEntry();
                        break;
                    case ACTION_EDIT:
                        entry = (SDataDpsCustomAccEntry) moPaneGridCustomAcc.getSelectedTableRow().getData();
                        break;
                    default:
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
                        return;
                }

                //entry.setPkDpsYearId(...);
                //entry.setPkDpsDocId(...);
                //entry.setPkAccEntryId(...);
                entry.setConcept(moFieldAccConcept.getString());
                entry.setQuantity(moFieldAccQuantity.getDouble());
                entry.setIsSubtotalPctApplying(moFieldAccIsSubtotalPctApplying.getBoolean());
                entry.setSubtotalPct(moFieldAccSubtotalPct.getDouble());
                entry.setSubtotal(accSubtotal);
                entry.setSubtotalCy(moFieldAccSubtotalCy.getDouble());
                entry.setIsDeleted(false);
                entry.setFkItemId(moFieldAccItem.getKeyAsIntArray()[0]);
                entry.setFkUnitId(moFieldAccUnit.getKeyAsIntArray()[0]);
                entry.setFkCostCenterId_n(isCostCenterEmpty ? "" : moPanelFkCostCenterId_n.getFieldAccount().getString());
                entry.setFkItemRefId_n(moFieldAccItemRef_n.getKeyAsIntArray()[0]);
                entry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                //entry.setFkUserEditId(...);
                //entry.setFkUserDeleteId(...);
                //entry.setUserNewTs(...);
                //entry.setUserEditTs(...);
                //entry.setUserDeleteTs(...);

                entry.setDbmsItemKey(moAccItem.getKey());
                entry.setDbmsItem(moAccItem.getItem());
                entry.setDbmsUnitSymbol(moAccItem.getDbmsDataUnit().getSymbol());
                entry.setDbmsCostCenter_n(isCostCenterEmpty ? "" : moPanelFkCostCenterId_n.getCurrentInputCostCenter().getCostCenter());
                entry.setDbmsItemRefKey_n(itemRef_n == null ? "" : itemRef_n.getKey());
                entry.setDbmsItemRef_n(itemRef_n == null ? "" : itemRef_n.getItem());
                //entry.setDbmsUserNew(...);
                //entry.setDbmsUserEdit(...);
                //entry.setDbmsUserDelete(..);

                // add or replace entry:

                switch (mnAccCurrentAction) {
                    case ACTION_NEW:
                        moPaneGridCustomAcc.addTableRow(new SDataDpsCustomAccEntryRow(entry));
                        moPaneGridCustomAcc.renderTableRows();
                        moPaneGridCustomAcc.setTableRowSelection(moPaneGridCustomAcc.getTableGuiRowCount() - 1);
                        break;
                    case ACTION_EDIT:
                        int selectedRow = moPaneGridCustomAcc.getTable().getSelectedRow();
                        moPaneGridCustomAcc.setTableRow(new SDataDpsCustomAccEntryRow(entry), selectedRow);
                        moPaneGridCustomAcc.renderTableRows();
                        moPaneGridCustomAcc.setTableRowSelection(selectedRow);
                        break;
                    default:
                        // nothing
                }

                renderCustomAccSubtotal();
                actionCustomAccEntryCancel(false, false);
            }
        }
    }
    
    private void actionCustomAccEntryCancel(final boolean forceCancel, final boolean renderSelectedEntry) {
        if (jbCustomAccEntryCancel.isEnabled() || forceCancel) {
            mnAccCurrentAction = 0;
            
            enableCustomAccControls(true, false);
            enableCustomAccFields(false, true);
            
            if (renderSelectedEntry) {
                valueChangedCustomAcc();
            }
        }
    }

    private void focusLostDate() {
        renderDate();
        renderDateMaturity();
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

    private void focusLostAddAmc71SupplierGln() {
        if (canClearAddAmc71Company()) {
            itemStateChangedAddAmc71SupplierGln();
        }
    }
    
    private void focusLostAddAmc71CompanyGln() {
        if (canClearAddAmc71CompanyBranch()) {
            itemStateChangedAddAmc71CompanyGln();
        }
    }
    
    private void focusLostAddAmc71CompanyBranchGln() {
        if (canClearAddAmc71CompanyBranchShipTo()) {
            itemStateChangedAddAmc71CompanyBranchGln();
        }
    }
    
    private void focusLostAccSubtotalPct() {
        moFieldAccSubtotalCy.setDouble(SLibUtils.roundAmount(moDps.getSubtotalCy_r() * moFieldAccSubtotalPct.getDouble()));
        focusLostAccSubtotalCy();
    }
    
    private void focusLostAccSubtotalCy() {
        if (isLocalCurrency()) {
            jtfAccSubtotal.setText("");
        }
        else {
            jtfAccSubtotal.setText(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(getAccSubtotal()));
        }
    }

    private void itemStateChangedDateDoc() {
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

    private void itemStateChangedDateStartCredit() {
        if (jckDateStartCredit.isSelected()) {
            jftDateStartCredit.setEditable(true);
            jftDateStartCredit.setFocusable(true);
            jbDateStartCredit.setEnabled(true);
            jbDateMaturity.setEnabled(true);
            jftDateStartCredit.requestFocus();
        }
        else {
            jftDateStartCredit.setEditable(false);
            jftDateStartCredit.setFocusable(false);
            jbDateStartCredit.setEnabled(false);
            jbDateMaturity.setEnabled(false);
            moFieldDateStartCredit.setFieldValue(moFieldDate.getDate());
            renderDateMaturity();
        }
    }

    private void itemStateChangedRecordUser() {
        boolean enabled = jckRecordUser.isSelected();

        jtfRecordManualDateRo.setEnabled(enabled);
        jtfRecordManualBranchRo.setEnabled(enabled);
        jtfRecordManualNumberRo.setEnabled(enabled);
        jbRecordManualSelect.setEnabled(enabled);
        jbRecordManualView.setEnabled(enabled && moRecordUserKey != null);
    }

    private void itemStateChangedShipments() {
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

    private void itemStateChangedIsDiscountDocApplying(boolean calculate) {
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

    private void itemStateChangedIsDiscountDocPercentage(boolean calculate) {
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

    private void itemStateChangedNumberSeries() {
        obtainNextNumber();
    }

    private void itemStateChangedFkPaymentTypeId(boolean resetDependents) {
        mbUpdatingForm = true;
        
        boolean isCfdEmissionRequired = isCfdEmissionRequired();
        
        if (jcbFkPaymentTypeId.getSelectedIndex() <= 0) {
            jckDateStartCredit.setEnabled(false);
            jtfDaysOfCredit.setEditable(false);
            jtfDaysOfCredit.setFocusable(false);
            if (resetDependents) {
                moFieldDaysOfCredit.setFieldValue(0);
            }
            
            if (isCfdEmissionRequired) {
                moFieldCfdiPaymentWay.setFieldValue("");
                moFieldCfdiPaymentMethod.setFieldValue("");
            }
        }
        else {
            switch (moFieldFkPaymentTypeId.getKeyAsIntArray()[0]) {
                case SDataConstantsSys.TRNS_TP_PAY_CASH:
                    jckDateStartCredit.setEnabled(false);
                    jtfDaysOfCredit.setEditable(false);
                    jtfDaysOfCredit.setFocusable(false);
                    if (resetDependents) {
                        moFieldDaysOfCredit.setFieldValue(0);
                    }

                    if (isCfdEmissionRequired) {
                        if (resetDependents) {
                            moFieldCfdiPaymentWay.setFieldValue(moBizPartnerCategory == null ? "" : moBizPartnerCategory.getCfdiPaymentWay());
                        }
                        moFieldCfdiPaymentMethod.setFieldValue(DCfdi40Catalogs.MDP_PUE); // fixed required value
                    }
                    break;

                case SDataConstantsSys.TRNS_TP_PAY_CREDIT:
                    jckDateStartCredit.setEnabled(true);
                    jtfDaysOfCredit.setEditable(true);      // XXX must be enabled according to user rights
                    jtfDaysOfCredit.setFocusable(true);     // XXX must be enabled according to user rights
                    if (resetDependents) {
                        moFieldDaysOfCredit.setFieldValue(moBizPartnerCategory == null ? 0 : moBizPartnerCategory.getEffectiveDaysOfCredit());
                    }

                    if (isCfdEmissionRequired) {
                        moFieldCfdiPaymentWay.setFieldValue(DCfdi40Catalogs.FDP_POR_DEF);     // fixed required value
                        moFieldCfdiPaymentMethod.setFieldValue(DCfdi40Catalogs.MDP_PPD); // fixed required value
                    }
                    break;

                default:
            }
        }

        if (mnFormStatus == SLibConstants.FORM_STATUS_EDIT && !mbParamIsReadOnly) {
            if (!jckDateStartCredit.isEnabled()) {
                jckDateStartCredit.setSelected(false);
            }
            itemStateChangedDateStartCredit();
            renderDateMaturity();
        }

        mbUpdatingForm = false;
    }

    private void itemStateChangedFkCurrencyId(boolean calculateTotal) {
        if (jcbFkCurrencyId.getSelectedIndex() <= 0 || isLocalCurrency()) {
            // Document in local currency:

            jtfExchangeRate.setEditable(false);
            jtfExchangeRate.setFocusable(false);
            jbExchangeRate.setEnabled(false);
            jbComputeTotal.setEnabled(false);
            jbEntryWizard.setEnabled(!mbIsDpsAdjustment);

            moFieldExchangeRateSystem.setFieldValue(1d);
            moFieldExchangeRate.setFieldValue(1d);

            jtfCurrencyKeyRo.setText(miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
        }
        else {
            // Document in foreign currency:

            if (moDps != null && moDps.getIsRegistryNew() && !moDps.getAuxKeepExchangeRate() && !mbDocBeingImported) {
                setExchangeRate(moFieldFkCurrencyId.getKeyAsIntArray()[0], moFieldExchangeRate);
                setExchangeRate(moFieldFkCurrencyId.getKeyAsIntArray()[0], moFieldExchangeRateSystem);
            }
            else if (mbDocBeingImported && moParamDpsSource != null) {
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
        
        if (jcbFkCurrencyId.getSelectedIndex() <= 0) {
            maSystemNotes = null;
        }
        else {
            try {
                maSystemNotes = STrnUtilities.getSystemNotes(miClient, (int[]) moDpsType.getPrimaryKey(), moFieldFkCurrencyId.getKeyAsIntArray()[0]);
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }

        enableCfdCceFields(true);
        
        if (calculateTotal) {
            calculateTotal();
        }
        
        renderBizPartnerPrepaymentsBalance();
    }

    private void itemStateChangedFkIncotermId() {
        int[] filterTypeSpot = null;

        if (jcbFkIncotermId.getSelectedIndex() <= 0 || moFieldFkIncotermId.getKeyAsIntArray()[0] == SModSysConsts.LOGS_INC_NA) {
            jcbFkSpotSrcId_n.setEnabled(false);
            jcbFkSpotDesId_n.setEnabled(false);

            jcbFkSpotSrcId_n.removeAllItems();
            jcbFkSpotDesId_n.removeAllItems();
        }
        else {
            jcbFkSpotSrcId_n.setEnabled(true);
            jcbFkSpotDesId_n.setEnabled(true);

            switch (moFieldFkIncotermId.getKeyAsIntArray()[0]) {
                case SModSysConsts.LOGS_INC_EXW:
                case SModSysConsts.LOGS_INC_FCA:
                case SModSysConsts.LOGS_INC_CPT:
                case SModSysConsts.LOGS_INC_CIP:
                case SModSysConsts.LOGS_INC_DAP:
                case SModSysConsts.LOGS_INC_DAF:
                case SModSysConsts.LOGS_INC_DDU:
                case SModSysConsts.LOGS_INC_DDP:
                case SModSysConsts.LOGS_INC_DPU:
                    filterTypeSpot = new int[] { SModSysConsts.LOGS_TP_SPOT_PLA };
                    break;
                case SModSysConsts.LOGS_INC_DAT:
                    filterTypeSpot = new int[] { SModSysConsts.LOGS_TP_SPOT_PLA, SModSysConsts.LOGS_TP_SPOT_TER };
                    break;
                case SModSysConsts.LOGS_INC_FAS:
                case SModSysConsts.LOGS_INC_FOB:
                case SModSysConsts.LOGS_INC_DES:
                case SModSysConsts.LOGS_INC_DEQ:
                case SModSysConsts.LOGS_INC_CFR:
                case SModSysConsts.LOGS_INC_CIF:
                    filterTypeSpot = new int[] { SModSysConsts.LOGS_TP_SPOT_POR };
                    break;
                default:
            }

            SFormUtilities.populateComboBox(miClient, jcbFkSpotSrcId_n, SModConsts.LOGU_SPOT_COB, new int[] { moDps != null ? moDps.getFkCompanyBranchId() : ((SSessionCustom) miClient.getSession().getSessionCustom()).getCurrentBranchKey()[0] });
            SFormUtilities.populateComboBox(miClient, jcbFkSpotDesId_n, SModConsts.LOGU_SPOT, new Object[] { filterTypeSpot, mnDeliveryType });

            if (jcbFkSpotSrcId_n.getItemCount() >= 2) {
                // select the FIRST option:
                jcbFkSpotSrcId_n.setSelectedIndex(1);
            }

            if (jcbFkSpotDesId_n.getItemCount() == 2) {
                // select the UNIQUE option:
                jcbFkSpotDesId_n.setSelectedIndex(1);
            }
        }
    }

    private void itemStateChangedFkCarrierTypeId() {
        boolean enableCarrier = moFieldFkCarrierTypeId.getKeyAsIntArray()[0] == SModSysConsts.LOGS_TP_CAR_CAR;
        boolean enableTypeVehicle = moFieldFkCarrierTypeId.getKeyAsIntArray()[0] > SModSysConsts.LOGS_TP_CAR_NA;

        jcbFkCarrierId_n.setEnabled(enableCarrier);
        jbFkCarrierId_n.setEnabled(enableCarrier);
        
        
        jtbLinkTicket.setEnabled(mbIsDpsOrder || mbIsDpsInvoice);
        jtbLinkTicket.setSelected(mvScaleTicDps != null && !mvScaleTicDps.isEmpty());
        
        jcbFkVehicleTypeId_n.setEnabled(enableTypeVehicle);
        jcbDriver.setEnabled(enableTypeVehicle && !jtbLinkTicket.isSelected());
        jcbPlate.setEnabled(enableTypeVehicle && !jtbLinkTicket.isSelected());
        jtfTicket.setEnabled(enableTypeVehicle && !jtbLinkTicket.isSelected());
        jtfTicket.setEditable(enableTypeVehicle && !jtbLinkTicket.isSelected());
        jtfTicket.setFocusable(enableTypeVehicle && !jtbLinkTicket.isSelected());
        
        if (!enableCarrier) {
            moFieldFkCarrierId_n.setFieldValue(null);
        }

        if (!enableTypeVehicle && !jtbLinkTicket.isSelected()) {
            moFieldFkVehicleTypeId_n.setFieldValue(null);
            moFieldDriver.setFieldValue("");
            moFieldPlate.setFieldValue("");
            moFieldTicket.setFieldValue("");
        }
        
        itemStateChangedFkVehicleTypeId_n();
    }

    private void itemStateChangedFkVehicleTypeId_n() {
	boolean enableVehicle = jcbFkVehicleTypeId_n.getSelectedIndex() > 0 && moFieldFkCarrierTypeId.getKeyAsIntArray()[0] == SModSysConsts.LOGS_TP_CAR_OWN;
        
        SFormUtilities.populateComboBox(miClient, jcbFkVehicleId_n, SModConsts.LOG_VEH, new int[] { moFieldFkVehicleTypeId_n.getKeyAsIntArray()[0] });
        
        jcbFkVehicleId_n.setEnabled(enableVehicle);
        jbFkVehicleId_n.setEnabled(enableVehicle);
    }

    private void itemStateChangedAddCfdAddendaSubtype() {
        if (jcbAddCfdAddendaSubtype.isEnabled() && jcbAddCfdAddendaSubtype.getSelectedIndex() > 0 && moFieldAddCfdAddendaSubtype.getKeyAsIntArray()[0] == SCfdConsts.ADD_SORIANA_EXT) {
            jtfAddSorianaNotaEntradaFolio.setEnabled(true);
        }
        else {
            jtfAddSorianaNotaEntradaFolio.setEnabled(false);
        }
    }

    private void itemStateChangedAddAmc71SupplierGln() {
        String oldCompanyGln = moFieldAddAmc71CompanyGln.getString(); // preserve original value
        
        jcbAddAmc71CompanyGln.removeAllItems();
        
        if (!moFieldAddAmc71SupplierGln.getString().isEmpty()) {
            SAddendaAmc71Supplier supplier = moAddendaAmc71Manager.getSupplierByGln(moFieldAddAmc71SupplierGln.getString());
            
            if (supplier != null) {
                moFieldAddAmc71SupplierNumber.setFieldValue(supplier.SupplierNumber);
                
                ArrayList<String> glns = supplier.getCompaniesGlns();
                
                for (String gln : glns) {
                    jcbAddAmc71CompanyGln.addItem(gln);
                }
            }
        }
        
        if (jcbAddAmc71CompanyGln.getItemCount() == 0) {
            moFieldAddAmc71CompanyGln.setFieldValue(oldCompanyGln); // restore original value
        }
        
        itemStateChangedAddAmc71CompanyGln();
    }
    
    private void itemStateChangedAddAmc71CompanyGln() {
        String oldCompanyBranchGln = moFieldAddAmc71CompanyBranchGln.getString(); // preserve original value
        
        jcbAddAmc71CompanyBranchGln.removeAllItems();
        
        if (!moFieldAddAmc71SupplierGln.getString().isEmpty() && !moFieldAddAmc71CompanyGln.getString().isEmpty()) {
            SAddendaAmc71Supplier supplier = moAddendaAmc71Manager.getSupplierByGln(moFieldAddAmc71SupplierGln.getString());
            
            if (supplier != null) {
                SAddendaAmc71Company company = supplier.getCompanyByGln(moFieldAddAmc71CompanyGln.getString());
                
                if (company != null) {
                    ArrayList<String> glns = company.getCompanyBranchesGlns();

                    for (String gln : glns) {
                        jcbAddAmc71CompanyBranchGln.addItem(gln);
                    }
                    
                    if (moFieldAddAmc71CompanyContact.getString().isEmpty()) {
                        moFieldAddAmc71CompanyContact.setFieldValue(company.CompanyContact);
                    }
                }
            }
        }
        
        if (jcbAddAmc71CompanyBranchGln.getItemCount() == 0) {
            moFieldAddAmc71CompanyBranchGln.setFieldValue(oldCompanyBranchGln); // restore original value
        }
        
        itemStateChangedAddAmc71CompanyBranchGln();
    }
    
    private void itemStateChangedAddAmc71CompanyBranchGln() {
        if (!moFieldAddAmc71SupplierGln.getString().isEmpty() && !moFieldAddAmc71CompanyGln.getString().isEmpty() && !moFieldAddAmc71CompanyBranchGln.getString().isEmpty()) {
            SAddendaAmc71Supplier supplier = moAddendaAmc71Manager.getSupplierByGln(moFieldAddAmc71SupplierGln.getString());
            
            if (supplier != null) {
                SAddendaAmc71Company company = supplier.getCompanyByGln(moFieldAddAmc71CompanyGln.getString());
                
                if (company != null) {
                    SAddendaAmc71CompanyBranch companyBranch = company.getCompanyBranchByGln(moFieldAddAmc71CompanyBranchGln.getString());
                    
                    if (companyBranch != null) {
                        moFieldAddAmc71ShipToName.setFieldValue(companyBranch.ShipToName);
                        moFieldAddAmc71ShipToAddress.setFieldValue(companyBranch.ShipToAddress);
                        moFieldAddAmc71ShipToCity.setFieldValue(companyBranch.ShipToCity);
                        moFieldAddAmc71ShipToPostalCode.setFieldValue(companyBranch.ShipToPostalCode);
                    }
                }
            }
        }
    }
    
    private void itemStateChangedAccItem(final boolean clearFields) {
        if (jcbAccItem.getSelectedIndex() <= 0) {
            moAccItem = null;
        }
        else {
            moAccItem = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, moFieldAccItem.getKeyAsIntArray(), SLibConstants.EXEC_MODE_VERBOSE);
        }

        renderAccItem(clearFields);
    }
    
    private void itemStateChangedAccIsSubtotalPctApplying(final boolean requestFocus) {
        boolean enableable = isCustomAccEnableable();
        
        if (jckAccIsSubtotalPctApplying.isSelected()) {
            jtfAccSubtotalPct.setEnabled(enableable);
            jtfAccSubtotalCy.setEnabled(false);
            
            moFieldAccSubtotalCy.resetField();
            
            if (requestFocus) {
                jtfAccSubtotalPct.requestFocusInWindow();
            }
        }
        else {
            jtfAccSubtotalPct.setEnabled(false);
            jtfAccSubtotalCy.setEnabled(enableable);
            
            moFieldAccSubtotalPct.resetField();
            
            if (requestFocus) {
                jtfAccSubtotalCy.requestFocusInWindow();
            }
        }
    }
    
    private void stateChangedTabbedPane() {
        if (jTabbedPane.getSelectedIndex() == TAB_ACC) { // customized-accounting tab
            if (!moDps.getDbmsDpsCustomAccEntries().isEmpty() && !mbCustomAccPrepared) {
                // Prepare customized-accounting controls and render entries until required:
                
                mbResetingForm = true; // same case as in method setRegistry()

                prepareCustomAcc(false); // as well resets member mbCustomAccPrepared

                for (SDataDpsCustomAccEntry entry : moDps.getDbmsDpsCustomAccEntries()) {
                    entry.setIsRegistryEdited(true); // assure that entry will be saved
                    moPaneGridCustomAcc.addTableRow(new SDataDpsCustomAccEntryRow(entry));
                }

                moPaneGridCustomAcc.renderTableRows();
                moPaneGridCustomAcc.setTableRowSelection(0);
                valueChangedCustomAcc();

                mbResetingForm = false; // same case as in method setRegistry()
                
                renderCustomAccSubtotal(); // is sensible to member flag mbResetingForm
            }
        }
    }
    
    private void valueChangedCustomAcc() {
        renderCustomAccEntry(moPaneGridCustomAcc.getSelectedTableRow() == null ? null : (SDataDpsCustomAccEntry) moPaneGridCustomAcc.getSelectedTableRow().getData());
    }
    
    private void actionEdit() {
        boolean edit = false;

        if (isBizPartnerBlocked(moBizPartner.getPkBizPartnerId())) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_INF_BP_BLOCKED);
            jbCancel.requestFocus();
        }
        else {
            switch (moDps.getFkDpsStatusId()) {
                case SDataConstantsSys.TRNS_ST_DPS_NEW:
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
                updateDpsFieldsStatus(true);
                adequateDatesForOrderPrevious();
                
                jbEdit.setEnabled(false);
                jbEditHelp.setEnabled(false);
                jbOk.setEnabled(true);

                if (jftDate.isEditable()) {
                    jftDate.requestFocus();
                }
                else if (jtfNumber.isEditable()) {
                    jtfNumber.requestFocus();
                }
                else {
                    jbCancel.requestFocus();
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
        if (!mbIsPeriodOpen && (mbIsDpsInvoice || mbIsDpsAdjustment)) {
            msg += "\n- El período contable de la fecha del documento ya está cerrado.";
        }
        if (mnParamCurrentUserPrivilegeLevel != SDataConstantsSys.UNDEFINED && mnParamCurrentUserPrivilegeLevel < SUtilConsts.LEV_AUTHOR) {
            msg += "\n- El usuario '" + miClient.getSessionXXX().getUser().getUser() + "' no tiene el nivel de derecho suficiente para modificar el registro.";
        }
        if (moDps.isDocumentSal() && moDps.getDbmsDataCfd() != null && moDps.getDbmsDataCfd().isCfdi() && moDps.getDbmsDataCfd().isStamped()) {
            msg += "\n- Este documento es un CFDI y está emitido.";
        }
        if (moDps.isDocumentSal() && moDps.getDbmsDataCfd() != null && moDps.getDbmsDataCfd().isCfdi() && moDps.getDbmsDataCfd().isStamped() && moDps.getDbmsDataCfd().getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
            msg += "\n- Este documento es un CFDI y está anulado.";
        }
        if (moDps.isAdjustmentSal() && moDps.getDbmsDataCfd() != null && moDps.getDbmsDataCfd().isCfdi() && moDps.getDbmsDataCfd().isStamped()) {
            msg += "\n- Este documento de ajuste es un CFDI y está emitido.";
        }
        if (moDps.isAdjustmentSal() && moDps.getDbmsDataCfd() != null && moDps.getDbmsDataCfd().isCfdi() && moDps.getDbmsDataCfd().isStamped() && moDps.getDbmsDataCfd().getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
            msg += "\n- Este documento de ajuste es un CFDI y está anulado.";
        }
        if (moDps.isDocumentSal() && moDps.getDbmsDataCfd() != null && moDps.getDbmsDataCfd().isCfdi() && moDps.getDbmsDataCfd().getIsProcessingWebService()) {
            msg += "\n- Este documento es un CFDI y está en proceso de comunicación con el WS.";
        }
        if (moDps.isDocumentSal() && moDps.getDbmsDataCfd() != null && moDps.getDbmsDataCfd().isCfdi() && moDps.getDbmsDataCfd().getIsProcessingStorageXml()) {
            msg += "\n- Este documento es un CFDI y está en proceso de almacenamiento en el disco.";
        }
        if (moDps.isAdjustmentSal() && moDps.getDbmsDataCfd() != null && moDps.getDbmsDataCfd().isCfdi() && moDps.getDbmsDataCfd().getIsProcessingWebService()) {
            msg += "\n- Este documento de ajuste es un CFDI y está en proceso de comunicación con el WS.";
        }
        if (moDps.isAdjustmentSal() && moDps.getDbmsDataCfd() != null && moDps.getDbmsDataCfd().isCfdi() && moDps.getDbmsDataCfd().getIsProcessingStorageXml()) {
            msg += "\n- Este documento de ajuste es un CFDI y está en proceso de almacenamiento en el disco.";
        }
        if (moDps.getXtaHasSuppFiles()) {
            msg += "\n- Este documento tiene archivos de soporte anexados.";
        }
        if (moDps.getXtaHasAuthWeb()) {
            msg += "\n- Este documento ya inició el proceso de autorización via web.";
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

            jbOk.requestFocus(); // this forces all pending focus lost function to be called

            validation = formValidate();

            if (validation.getIsError()) {
                if (validation.getTabbedPaneIndex() != -1) {
                    jTabbedPane.setSelectedIndex(validation.getTabbedPaneIndex());
                }
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
                /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                releaseRecordUserLock();
                */
                /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                releaseRecordUserRedisLock();
                */
                releaseRecordUserSLock();
                mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
                setVisible(false);
            }
        }
    }
    
    private SFormValidation validateCfdi33() {
        SFormValidation validation = new SFormValidation();
        
        try {
            String message = "No se puede guardar el archivo XML del CFDI porque ";
            String cfdiStatus = new SCfdUtilsHandler(miClient).getCfdiSatStatus(SDataConstantsSys.TRNS_TP_CFD_INV, moComprobante33).getCfdiStatus();
            
            if (!cfdiStatus.equals(DCfdi33Consts.CFDI_ESTATUS_VIG)) {
                validation.setMessage(message + "su estatus es: '" + cfdiStatus + "'.");
            }
            else if (!moBizPartner.getFiscalId().equals(moComprobante33.getEltEmisor().getAttRfc().getString())) {
                validation.setMessage(message + "el RFC del emisor, '" + moComprobante33.getEltEmisor().getAttRfc().getString() + "', no corresponde al RFC del asociado de negocios seleccionado, '" + moBizPartner.getFiscalId() + "'.");
            }
            else {
                int cfdId = SCfdUtils.getCfdIdByUuid(miClient, msXmlUuid);
                
                if (cfdId != 0 && moDps.getDbmsDataCfd() != null && cfdId != moDps.getDbmsDataCfd().getPkCfdId()) {
                    validation.setMessage(message + "su UUID, '" + msXmlUuid + "', ya existe en la base de datos.");
                }
                else {
                    if (!moComprobante33.getAttSerie().getString().isEmpty()) {
                        String xmlSeriesTrimmed = SLibUtils.textLeft(moComprobante33.getAttSerie().getString(), SDataDps.LEN_SERIES).trim();

                        if (!xmlSeriesTrimmed.equals(jcbNumberSeries.getSelectedItem().toString())) {
                            String errorMessage = message + "su serie '" + moComprobante33.getAttSerie().getString() + "'"
                                    + (xmlSeriesTrimmed.length() == moComprobante33.getAttSerie().getString().length() ? "" :
                                            ",\najustada a " + SDataDps.LEN_SERIES + " caracteres para su comparación, sin espacios iniciales ni finales, como '" + xmlSeriesTrimmed + "',")
                                    + "\nno coincide con el valor de la serie capturada '" + jcbNumberSeries.getSelectedItem().toString() + "'.";

                            if (miClient.showMsgBoxConfirm(errorMessage + "\n¿Está seguro que desea dejar así la serie sin corregir esta diferencia?") != JOptionPane.YES_OPTION) {
                                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'serie', p. ej., '" + xmlSeriesTrimmed + "'.");
                            }
                        }
                    }

                    if (!validation.getIsError()) {
                        if (!moComprobante33.getAttFolio().getString().isEmpty()) {
                            String[] dpsFolio = jtfNumber.getText().split("-"); // a hyphen can be used to differentiate the number of another existing document when needed
                            String xmlFolioTrimmed = SLibUtils.textLeft(moComprobante33.getAttFolio().getString(), SDataDps.LEN_NUMBER).trim();

                            if (!xmlFolioTrimmed.equals(dpsFolio[0])) {
                                String errorMessage = message + "su folio '" + moComprobante33.getAttFolio().getString() + "'"
                                        + (xmlFolioTrimmed.length() == moComprobante33.getAttFolio().getString().length() ? "" :
                                                ",\najustado a " + SDataDps.LEN_NUMBER + " caracteres para su comparación, sin espacios iniciales ni finales, como '" + xmlFolioTrimmed + "'" + (dpsFolio.length == 1 ? "," : ""))
                                        + (dpsFolio.length == 1 ? "" : ",\ndescartando el texto diferenciador '" + dpsFolio[1] + "',")
                                        + "\nno coincide con el valor del folio capturado '" + dpsFolio[0] + "'.";

                                if (miClient.showMsgBoxConfirm(errorMessage + "\n¿Está seguro que desea dejar así el folio sin corregir esta diferencia?") != JOptionPane.YES_OPTION) {
                                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'folio', p. ej., '" + xmlFolioTrimmed + "'.");
                                }
                            }
                        }
                    }
                    if (!validation.getIsError() && isCfdIntCommerceRequired()) {
                        if (moFieldCfdiCfdiUsage.getFieldValue().toString().compareTo(DCfdi33Catalogs.CFDI_USO_POR_DEF) != 0) {
                            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlCfdiCfdiUsage.getText() + "': <" + DCfdi33Catalogs.CFDI_USO_POR_DEF + ">.");
                            validation.setComponent(jcbCfdiCfdiUsage);
                            jTabbedPane.setSelectedIndex(TAB_CFD_XML);
                        }
                    }
                }
            }
            
            if (validation.getIsError()) {
                validation.setComponent(jbLoadFileXml);
            }
        } 
        catch(Exception e) {
            SLibUtilities.renderException(this, e);
        }
        
        return validation;
    }
    
    private SFormValidation validateCfdi40(){
        SFormValidation validation = new SFormValidation();
        
        try {
            String message = "No se puede guardar el archivo XML del CFDI porque ";
            String cfdiStatus = new SCfdUtilsHandler(miClient).getCfdiSatStatus(SDataConstantsSys.TRNS_TP_CFD_INV, moComprobante40).getCfdiStatus();
            
            if (!cfdiStatus.equals(DCfdi40Consts.CFDI_ESTATUS_VIG)) {
                validation.setMessage(message + "su estatus es: '" + cfdiStatus + "'.");
            }
            else if (!moBizPartner.getFiscalId().equals(moComprobante40.getEltEmisor().getAttRfc().getString())) {
                validation.setMessage(message + "el RFC del emisor, '" + moComprobante40.getEltEmisor().getAttRfc().getString() + "', no corresponde al RFC del asociado de negocios seleccionado, '" + moBizPartner.getFiscalId() + "'.");
            }
            else {
                int cfdId = SCfdUtils.getCfdIdByUuid(miClient, msXmlUuid);
                if (cfdId != 0 && moDps.getDbmsDataCfd() != null && cfdId != moDps.getDbmsDataCfd().getPkCfdId()) {
                    ArrayList<String> numDps;
                    numDps = SCfdUtils.getSerNumByCfd(miClient, cfdId);
                    validation.setMessage(message + "su UUID, '" + msXmlUuid + "', ya existe en la base de datos con fecha.\nCon fecha: " + numDps.get(0) + " y con el folio: " + numDps.get(1) + "");
                }
                else {
                    if (!moComprobante40.getAttSerie().getString().isEmpty()) {
                        String xmlSeriesTrimmed = SLibUtils.textLeft(moComprobante40.getAttSerie().getString(), SDataDps.LEN_SERIES).trim();

                        if (!xmlSeriesTrimmed.equals(jcbNumberSeries.getSelectedItem().toString())) {
                            String errorMessage = message + "su serie '" + moComprobante40.getAttSerie().getString() + "'"
                                    + (xmlSeriesTrimmed.length() == moComprobante40.getAttSerie().getString().length() ? "" :
                                            ",\najustada a " + SDataDps.LEN_SERIES + " caracteres para su comparación, sin espacios iniciales ni finales, como '" + xmlSeriesTrimmed + "',")
                                    + "\nno coincide con el valor de la serie capturada '" + jcbNumberSeries.getSelectedItem().toString() + "'.";

                            if (miClient.showMsgBoxConfirm(errorMessage + "\n¿Está seguro que desea dejar así la serie sin corregir esta diferencia?") != JOptionPane.YES_OPTION) {
                                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'serie', p. ej., '" + xmlSeriesTrimmed + "'.");
                            }
                        }
                    }

                    if (!validation.getIsError()) {
                        if (!moComprobante40.getAttFolio().getString().isEmpty()) {
                            String[] dpsFolio = jtfNumber.getText().split("-"); // a hyphen can be used to differentiate the number of another existing document when needed
                            String xmlFolioTrimmed = SLibUtils.textLeft(moComprobante40.getAttFolio().getString(), SDataDps.LEN_NUMBER).trim();

                            if (!xmlFolioTrimmed.equals(dpsFolio[0])) {
                                String errorMessage = message + "su folio '" + moComprobante40.getAttFolio().getString() + "'"
                                        + (xmlFolioTrimmed.length() == moComprobante40.getAttFolio().getString().length() ? "" :
                                                ",\najustado a " + SDataDps.LEN_NUMBER + " caracteres para su comparación, sin espacios iniciales ni finales, como '" + xmlFolioTrimmed + "'" + (dpsFolio.length == 1 ? "," : ""))
                                        + (dpsFolio.length == 1 ? "" : ",\ndescartando el texto diferenciador '" + dpsFolio[1] + "',")
                                        + "\nno coincide con el valor del folio capturado '" + dpsFolio[0] + "'.";

                                if (miClient.showMsgBoxConfirm(errorMessage + "\n¿Está seguro que desea dejar así el folio sin corregir esta diferencia?") != JOptionPane.YES_OPTION) {
                                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'folio', p. ej., '" + xmlFolioTrimmed + "'.");
                                }
                            }
                        }
                    }
                    if (!validation.getIsError() && isCfdIntCommerceRequired()) {
                        if (moFieldCfdiCfdiUsage.getFieldValue().toString().compareTo(DCfdi40Catalogs.ClaveUsoCfdiSinEfectosFiscales) != 0) {
                            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlCfdiCfdiUsage.getText() + "': <" + DCfdi40Catalogs.ClaveUsoCfdiSinEfectosFiscales + ">.");
                            validation.setComponent(jcbCfdiCfdiUsage);
                            jTabbedPane.setSelectedIndex(TAB_CFD_XML);
                        }
                    }
                }
            }
            
            if (validation.getIsError()) {
                validation.setComponent(jbLoadFileXml);
            }
        } 
        catch(Exception e) {
            SLibUtilities.renderException(this, e);
        }
        
        return validation;
    }
    
    /**
     * Verify if shipment data is needed.
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
     *  Verify valid but provisional data in shipmente fields.
     */
    private String validateProvisionalShipmentData() {
        String shipmentMessageMissingData = "";
         
        for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
            SDataDpsEntry entry = (SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData();

            if (entry.isValueProvisionalShipmentData()) {
                shipmentMessageMissingData = "Tiene partidas con valores de embarque provisionales.\n¿Desea conservarlos?";
            }
        }
        
        return shipmentMessageMissingData;
    }
 
    public void publicActionDependentNew() throws SQLException {
        if (jTabbedPane.getSelectedIndex() == 0) {
            actionEntryNew();
        }
        else if (jTabbedPane.getSelectedIndex() == 2) {
            actionNotesNew();
        }
    }

    public void publicActionDependentEdit() throws SQLException {
        if (jTabbedPane.getSelectedIndex() == TAB_ETY) {
            actionEntryEdit();
        }
        else if (jTabbedPane.getSelectedIndex() == TAB_NTS) {
            actionNotesEdit();
        }
        else if (jTabbedPane.getSelectedIndex() == TAB_ACC) {
            actionCustomAccEntryEdit();
        }
    }

    public void publicActionDependentDelete() throws SQLException, Exception {
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel100;
    private javax.swing.JPanel jPanel101;
    private javax.swing.JPanel jPanel102;
    private javax.swing.JPanel jPanel103;
    private javax.swing.JPanel jPanel104;
    private javax.swing.JPanel jPanel105;
    private javax.swing.JPanel jPanel106;
    private javax.swing.JPanel jPanel107;
    private javax.swing.JPanel jPanel108;
    private javax.swing.JPanel jPanel109;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel110;
    private javax.swing.JPanel jPanel111;
    private javax.swing.JPanel jPanel112;
    private javax.swing.JPanel jPanel113;
    private javax.swing.JPanel jPanel114;
    private javax.swing.JPanel jPanel115;
    private javax.swing.JPanel jPanel116;
    private javax.swing.JPanel jPanel117;
    private javax.swing.JPanel jPanel118;
    private javax.swing.JPanel jPanel119;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel120;
    private javax.swing.JPanel jPanel121;
    private javax.swing.JPanel jPanel122;
    private javax.swing.JPanel jPanel123;
    private javax.swing.JPanel jPanel124;
    private javax.swing.JPanel jPanel125;
    private javax.swing.JPanel jPanel126;
    private javax.swing.JPanel jPanel127;
    private javax.swing.JPanel jPanel128;
    private javax.swing.JPanel jPanel129;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel130;
    private javax.swing.JPanel jPanel131;
    private javax.swing.JPanel jPanel139;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel142;
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
    private javax.swing.JPanel jPanel95;
    private javax.swing.JPanel jPanel96;
    private javax.swing.JPanel jPanel97;
    private javax.swing.JPanel jPanel98;
    private javax.swing.JPanel jPanel99;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JButton jbBizPartnerBalance;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbCfdCceCalcTotalUsd;
    private javax.swing.JButton jbCfdiRelatedDocs;
    private javax.swing.JButton jbComputeTotal;
    private javax.swing.JButton jbCustomAccEntryCancel;
    private javax.swing.JButton jbCustomAccEntryCopy;
    private javax.swing.JButton jbCustomAccEntryDelete;
    private javax.swing.JButton jbCustomAccEntryEdit;
    private javax.swing.JButton jbCustomAccEntryNew;
    private javax.swing.JButton jbCustomAccEntryOk;
    private javax.swing.JButton jbDate;
    private javax.swing.JButton jbDateDoc;
    private javax.swing.JButton jbDateDocDelivery_n;
    private javax.swing.JButton jbDateDocLapsing_n;
    private javax.swing.JButton jbDateMaturity;
    private javax.swing.JButton jbDateStartCredit;
    private javax.swing.JButton jbDeleteBillOfLading;
    private javax.swing.JButton jbDeleteFilePdf;
    private javax.swing.JButton jbDeleteFileXml;
    private javax.swing.JButton jbEdit;
    private javax.swing.JButton jbEditHelp;
    private javax.swing.JButton jbEditLogistics;
    private javax.swing.JButton jbEditNotes;
    private javax.swing.JButton jbEditTaxRegion;
    private javax.swing.JButton jbEntryCopy;
    private javax.swing.JButton jbEntryDelete;
    private javax.swing.JButton jbEntryDiscountRetailChain;
    private javax.swing.JButton jbEntryEdit;
    private javax.swing.JButton jbEntryImportFromDps;
    private javax.swing.JButton jbEntryImportFromMatRequest;
    private javax.swing.JButton jbEntryNew;
    private javax.swing.JButton jbEntryViewLinks;
    private javax.swing.JButton jbEntryViewMatReqLinks;
    private javax.swing.JButton jbEntryWizard;
    private javax.swing.JButton jbExchangeRate;
    private javax.swing.JButton jbExeWizardAccSubtotal;
    private javax.swing.JButton jbExportCsv;
    private javax.swing.JButton jbFkCarrierId_n;
    private javax.swing.JButton jbFkCurrencyId;
    private javax.swing.JButton jbFkIncotermId;
    private javax.swing.JButton jbFkModeOfTransportationTypeId;
    private javax.swing.JButton jbFkProductionOrderId_n;
    private javax.swing.JButton jbFkVehicleId_n;
    private javax.swing.JButton jbLoadBillOfLading;
    private javax.swing.JButton jbLoadFilePdf;
    private javax.swing.JButton jbLoadFileXml;
    private javax.swing.JButton jbNotesDelete;
    private javax.swing.JButton jbNotesEdit;
    private javax.swing.JButton jbNotesNew;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPickAccItem;
    private javax.swing.JButton jbPickAccItemRef_n;
    private javax.swing.JButton jbPickAccUnit;
    private javax.swing.JButton jbPickSystemNotes;
    private javax.swing.JButton jbPrepayments;
    private javax.swing.JButton jbRecordManualSelect;
    private javax.swing.JButton jbRecordManualView;
    private javax.swing.JButton jbSalesAgent;
    private javax.swing.JButton jbSalesSupervisor;
    private javax.swing.JButton jbSetTime;
    private javax.swing.JButton jbTaxRegionId;
    private javax.swing.JComboBox jcbAccItem;
    private javax.swing.JComboBox jcbAccItemRef_n;
    private javax.swing.JComboBox jcbAccUnit;
    private javax.swing.JComboBox<String> jcbAddAmc71CompanyBranchGln;
    private javax.swing.JComboBox<String> jcbAddAmc71CompanyGln;
    private javax.swing.JComboBox<String> jcbAddAmc71SupplierGln;
    private javax.swing.JComboBox<SFormComponentItem> jcbAddCfdAddendaSubtype;
    private javax.swing.JComboBox<SFormComponentItem> jcbAdjustmentSubtypeId;
    private javax.swing.JComboBox jcbCfdCceFkAddresseeBizPartner;
    private javax.swing.JComboBox jcbCfdCceFkAddresseeBizPartnerBranch;
    private javax.swing.JComboBox jcbCfdCceFkAddresseeBizPartnerBranchAddress;
    private javax.swing.JComboBox jcbCfdCceFkBizPartnerAddressee;
    private javax.swing.JComboBox<SFormComponentItem> jcbCfdCceMoveReason;
    private javax.swing.JComboBox<SFormComponentItem> jcbCfdCceOperationType;
    private javax.swing.JComboBox<SFormComponentItem> jcbCfdCceRequestKey;
    private javax.swing.JComboBox<SFormComponentItem> jcbCfdiCfdiUsage;
    private javax.swing.JComboBox<SFormComponentItem> jcbCfdiPaymentMethod;
    private javax.swing.JComboBox<SFormComponentItem> jcbCfdiPaymentWay;
    private javax.swing.JComboBox<SFormComponentItem> jcbCfdiTaxRegimeIssuing;
    private javax.swing.JComboBox<SFormComponentItem> jcbCfdiTaxRegimeReceptor;
    private javax.swing.JComboBox jcbDriver;
    private javax.swing.JComboBox<SFormComponentItem> jcbExportation;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCarrierId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCarrierTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkContactId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCurrencyId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkDpsNatureId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkFunctionalAreaId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkIncotermId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkLanguageId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkModeOfTransportationTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkPaymentTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkProductionOrderId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSpotDesId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSpotSrcId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkVehicleId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkVehicleTypeId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbGblMonth;
    private javax.swing.JComboBox<SFormComponentItem> jcbGblPeriodicity;
    private javax.swing.JComboBox<SFormComponentItem> jcbNumberSeries;
    private javax.swing.JComboBox jcbPlate;
    private javax.swing.JComboBox<SFormComponentItem> jcbTaxRegionId;
    private javax.swing.JCheckBox jckAccIsSubtotalPctApplying;
    private javax.swing.JCheckBox jckCfdCceApplies;
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
    private javax.swing.JCheckBox jckRecordUser;
    private javax.swing.JCheckBox jckShipments;
    private javax.swing.JCheckBox jckValidateOnSaveFileXml;
    private javax.swing.JFormattedTextField jftAddSorianaRemisiónFecha;
    private javax.swing.JFormattedTextField jftDate;
    private javax.swing.JFormattedTextField jftDateDelivery_nRo;
    private javax.swing.JFormattedTextField jftDateDoc;
    private javax.swing.JFormattedTextField jftDateDocDelivery_n;
    private javax.swing.JFormattedTextField jftDateDocLapsing_n;
    private javax.swing.JFormattedTextField jftDateMaturityRo;
    private javax.swing.JFormattedTextField jftDateShipment_nRo;
    private javax.swing.JFormattedTextField jftDateStartCredit;
    private javax.swing.JLabel jlAccConcept;
    private javax.swing.JLabel jlAccDummyCostCenter;
    private javax.swing.JLabel jlAccItem;
    private javax.swing.JLabel jlAccItemRef_n;
    private javax.swing.JLabel jlAccQuantity;
    private javax.swing.JLabel jlAccSubtotal;
    private javax.swing.JLabel jlAccUnit;
    private javax.swing.JLabel jlAddAmc71CompanyBranchGln;
    private javax.swing.JLabel jlAddAmc71CompanyBranchGlnHint;
    private javax.swing.JLabel jlAddAmc71CompanyContact;
    private javax.swing.JLabel jlAddAmc71CompanyContactHint;
    private javax.swing.JLabel jlAddAmc71CompanyGln;
    private javax.swing.JLabel jlAddAmc71CompanyGlnHint;
    private javax.swing.JLabel jlAddAmc71ShipToAddress;
    private javax.swing.JLabel jlAddAmc71ShipToCity;
    private javax.swing.JLabel jlAddAmc71ShipToName;
    private javax.swing.JLabel jlAddAmc71ShipToPostalCode;
    private javax.swing.JLabel jlAddAmc71SupplierGln;
    private javax.swing.JLabel jlAddAmc71SupplierGlnHint;
    private javax.swing.JLabel jlAddAmc71SupplierNumber;
    private javax.swing.JLabel jlAddAmc71SupplierNumberHint;
    private javax.swing.JLabel jlAddBachocoDivisión;
    private javax.swing.JLabel jlAddBachocoOrganizaciónCompra;
    private javax.swing.JLabel jlAddBachocoSociedad;
    private javax.swing.JLabel jlAddCfdAddendaSubtype;
    private javax.swing.JLabel jlAddCfdAddendaType;
    private javax.swing.JLabel jlAddLorealFolioNotaRecepción;
    private javax.swing.JLabel jlAddModeloDpsDescripción;
    private javax.swing.JLabel jlAddSorianaBultoCantidad;
    private javax.swing.JLabel jlAddSorianaBultoTipo;
    private javax.swing.JLabel jlAddSorianaCita;
    private javax.swing.JLabel jlAddSorianaEntregaMercancía;
    private javax.swing.JLabel jlAddSorianaNotaEntradaFolio;
    private javax.swing.JLabel jlAddSorianaPedidoFolio;
    private javax.swing.JLabel jlAddSorianaRemisiónFecha;
    private javax.swing.JLabel jlAddSorianaRemisiónFechaHint;
    private javax.swing.JLabel jlAddSorianaRemisiónFolio;
    private javax.swing.JLabel jlAddSorianaTienda;
    private javax.swing.JLabel jlAdjustmentSubtypeId;
    private javax.swing.JLabel jlBillOfLading;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlBizPartnerBranch;
    private javax.swing.JLabel jlBizPartnerBranchAddress01;
    private javax.swing.JLabel jlBizPartnerBranchAddressMain01;
    private javax.swing.JLabel jlBizPartnerBranchAddressMain02;
    private javax.swing.JLabel jlCfdCceAddresseeBizPartner;
    private javax.swing.JLabel jlCfdCceCertificateOrigin;
    private javax.swing.JLabel jlCfdCceExchangeRateUsd;
    private javax.swing.JLabel jlCfdCceFkAddresseeBizPartner;
    private javax.swing.JLabel jlCfdCceFkAddresseeBizPartnerBranch;
    private javax.swing.JLabel jlCfdCceFkAddresseeBizPartnerBranchAddress;
    private javax.swing.JLabel jlCfdCceFkBizPartnerAddressee;
    private javax.swing.JLabel jlCfdCceMoveReason;
    private javax.swing.JLabel jlCfdCceNumberCertificateOrigin;
    private javax.swing.JLabel jlCfdCceOperationType;
    private javax.swing.JLabel jlCfdCceRequestKey;
    private javax.swing.JLabel jlCfdCceSubdivision;
    private javax.swing.JLabel jlCfdCceTotalUsd;
    private javax.swing.JLabel jlCfdiCfdiUsage;
    private javax.swing.JLabel jlCfdiConfirmation;
    private javax.swing.JLabel jlCfdiPaymentMethod;
    private javax.swing.JLabel jlCfdiPaymentWay;
    private javax.swing.JLabel jlCfdiTaxRegimeIssuing;
    private javax.swing.JLabel jlCfdiTaxRegimeReceptor;
    private javax.swing.JLabel jlCompanyBranch;
    private javax.swing.JLabel jlConditionsPayment;
    private javax.swing.JLabel jlCurrency;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlDateDelivery_n;
    private javax.swing.JLabel jlDateDocDelivery_n;
    private javax.swing.JLabel jlDateDocLapsing_n;
    private javax.swing.JLabel jlDateMaturity;
    private javax.swing.JLabel jlDateShipment_n;
    private javax.swing.JLabel jlDaysOfCredit;
    private javax.swing.JLabel jlDiscountDoc;
    private javax.swing.JLabel jlDpsType;
    private javax.swing.JLabel jlDriver;
    private javax.swing.JLabel jlExchangeRate;
    private javax.swing.JLabel jlExchangeRateSystem;
    private javax.swing.JLabel jlExportation;
    private javax.swing.JLabel jlFilePdf;
    private javax.swing.JLabel jlFileXml;
    private javax.swing.JLabel jlFiscalId;
    private javax.swing.JLabel jlFkCarrierId_n;
    private javax.swing.JLabel jlFkCarrierTypeId;
    private javax.swing.JLabel jlFkContactId_n;
    private javax.swing.JLabel jlFkCurrencyId;
    private javax.swing.JLabel jlFkDpsNatureId;
    private javax.swing.JLabel jlFkDpsStatus;
    private javax.swing.JLabel jlFkDpsStatusAuthorization;
    private javax.swing.JLabel jlFkDpsStatusValidity;
    private javax.swing.JLabel jlFkFunctionalAreaId;
    private javax.swing.JLabel jlFkIncotermId;
    private javax.swing.JLabel jlFkLanguageId;
    private javax.swing.JLabel jlFkModeOfTransportationTypeId;
    private javax.swing.JLabel jlFkPaymentTypeId;
    private javax.swing.JLabel jlFkProductionOrderId_n;
    private javax.swing.JLabel jlFkSpotDesId_n;
    private javax.swing.JLabel jlFkSpotSrcId_n;
    private javax.swing.JLabel jlFkVehicleId_n;
    private javax.swing.JLabel jlFkVehicleTypeId_n;
    private javax.swing.JLabel jlGblMonth;
    private javax.swing.JLabel jlGblPeriodicity;
    private javax.swing.JLabel jlGblYear;
    private javax.swing.JLabel jlGlobalInf;
    private javax.swing.JLabel jlNumber;
    private javax.swing.JLabel jlPrepaymentsCy;
    private javax.swing.JLabel jlPrepaymentsWarning;
    private javax.swing.JLabel jlQuantityTotal;
    private javax.swing.JLabel jlRequisicion;
    private javax.swing.JLabel jlSalesAgent;
    private javax.swing.JLabel jlSalesAgentBizPartner;
    private javax.swing.JLabel jlSalesSupervisor;
    private javax.swing.JLabel jlSalesSupervisorBizPartner;
    private javax.swing.JLabel jlSpace;
    private javax.swing.JLabel jlSpace1;
    private javax.swing.JLabel jlSubtotal;
    private javax.swing.JLabel jlSubtotalProvisional;
    private javax.swing.JLabel jlTaxCharged;
    private javax.swing.JLabel jlTaxRegionId;
    private javax.swing.JLabel jlTaxRetained;
    private javax.swing.JLabel jlTicket;
    private javax.swing.JLabel jlTotal;
    private javax.swing.JPanel jpCfdAddenda;
    private javax.swing.JPanel jpCfdInternationalTrade;
    private javax.swing.JPanel jpCfdXml;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpControlsButtons;
    private javax.swing.JPanel jpControlsButtonsCenter;
    private javax.swing.JPanel jpControlsButtonsWest;
    private javax.swing.JPanel jpControlsPk;
    private javax.swing.JPanel jpControlsRecord;
    private javax.swing.JPanel jpCurrency;
    private javax.swing.JPanel jpCustomAcc;
    private javax.swing.JPanel jpCustomAcc1;
    private javax.swing.JPanel jpCustomAcc11;
    private javax.swing.JPanel jpCustomAcc111;
    private javax.swing.JPanel jpCustomAcc1111;
    private javax.swing.JPanel jpCustomAcc1112;
    private javax.swing.JPanel jpCustomAcc112;
    private javax.swing.JPanel jpCustomAcc12;
    private javax.swing.JPanel jpCustomAcc121;
    private javax.swing.JPanel jpCustomAcc122;
    private javax.swing.JPanel jpCustomAcc2;
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
    private javax.swing.JSeparator jsEntry4;
    private javax.swing.JSeparator jsEntry5;
    private javax.swing.JSeparator jsNotes01;
    private javax.swing.JSeparator jsNotes02;
    private javax.swing.JScrollPane jspCfdiRelatedDocs;
    private javax.swing.JTextArea jtaCfdiRelatedDocs;
    private javax.swing.JToggleButton jtbEntryFilter;
    private javax.swing.JToggleButton jtbLinkTicket;
    private javax.swing.JToggleButton jtbNotesFilter;
    private javax.swing.JToggleButton jtbSwitchCustomAcc;
    private javax.swing.JTextField jtfAccConcept;
    private javax.swing.JTextField jtfAccQuantity;
    private javax.swing.JTextField jtfAccSubtotal;
    private javax.swing.JTextField jtfAccSubtotalCur;
    private javax.swing.JTextField jtfAccSubtotalCy;
    private javax.swing.JTextField jtfAccSubtotalCyCur;
    private javax.swing.JTextField jtfAccSubtotalPct;
    private javax.swing.JTextField jtfAccSubtotalSubtotal;
    private javax.swing.JTextField jtfAccSubtotalSubtotalCur;
    private javax.swing.JTextField jtfAccSubtotalSubtotalCy;
    private javax.swing.JTextField jtfAccSubtotalSubtotalCyCur;
    private javax.swing.JTextField jtfAccSubtotalSubtotalPct;
    private javax.swing.JTextField jtfAddAmc71CompanyContact;
    private javax.swing.JTextField jtfAddAmc71ShipToAddress;
    private javax.swing.JTextField jtfAddAmc71ShipToCity;
    private javax.swing.JTextField jtfAddAmc71ShipToName;
    private javax.swing.JTextField jtfAddAmc71ShipToPostalCode;
    private javax.swing.JTextField jtfAddAmc71SupplierNumber;
    private javax.swing.JTextField jtfAddBachocoDivisión;
    private javax.swing.JTextField jtfAddBachocoOrganizaciónCompra;
    private javax.swing.JTextField jtfAddBachocoSociedad;
    private javax.swing.JTextField jtfAddCfdAddendaType;
    private javax.swing.JTextField jtfAddLorealFolioNotaRecepción;
    private javax.swing.JTextField jtfAddModeloDpsDescripción;
    private javax.swing.JTextField jtfAddSorianaBultoCantidad;
    private javax.swing.JTextField jtfAddSorianaBultoTipo;
    private javax.swing.JTextField jtfAddSorianaCita;
    private javax.swing.JTextField jtfAddSorianaEntregaMercancía;
    private javax.swing.JTextField jtfAddSorianaNotaEntradaFolio;
    private javax.swing.JTextField jtfAddSorianaPedidoFolio;
    private javax.swing.JTextField jtfAddSorianaRemisiónFolio;
    private javax.swing.JTextField jtfAddSorianaTienda;
    private javax.swing.JTextField jtfBillOfLading;
    private javax.swing.JTextField jtfBizPartnerBranchAddress01Ro;
    private javax.swing.JTextField jtfBizPartnerBranchAddressMain01Ro;
    private javax.swing.JTextField jtfBizPartnerBranchAddressMain02Ro;
    private javax.swing.JTextField jtfBizPartnerBranchRo;
    private javax.swing.JTextField jtfBizPartnerRo;
    private javax.swing.JTextField jtfCfdCceCertificateOrigin;
    private javax.swing.JTextField jtfCfdCceExchangeRateUsd;
    private javax.swing.JTextField jtfCfdCceNumberCertificateOrigin;
    private javax.swing.JTextField jtfCfdCceSubdivision;
    private javax.swing.JTextField jtfCfdCceTotalUsd;
    private javax.swing.JTextField jtfCfdiConfirmation;
    private javax.swing.JTextField jtfCfdiFirstRelatedDps;
    private javax.swing.JTextField jtfCompanyBranchCodeRo;
    private javax.swing.JTextField jtfCompanyBranchRo;
    private javax.swing.JTextField jtfConditionsPayment;
    private javax.swing.JTextField jtfCurrencyKeyRo;
    private javax.swing.JTextField jtfCurrencySystemKeyRo;
    private javax.swing.JTextField jtfDaysOfCredit;
    private javax.swing.JTextField jtfDiscountDocCy_rRo;
    private javax.swing.JTextField jtfDiscountDocPercentage;
    private javax.swing.JTextField jtfDiscountDoc_rRo;
    private javax.swing.JTextField jtfDpsTypeRo;
    private javax.swing.JTextField jtfExchangeRate;
    private javax.swing.JTextField jtfExchangeRateSystemRo;
    private javax.swing.JTextField jtfFilePdf;
    private javax.swing.JTextField jtfFileXml;
    private javax.swing.JTextField jtfFiscalId;
    private javax.swing.JTextField jtfFkDpsStatusAuthorizationRo;
    private javax.swing.JTextField jtfFkDpsStatusRo;
    private javax.swing.JTextField jtfFkDpsStatusValidityRo;
    private javax.swing.JTextField jtfGblYear;
    private javax.swing.JTextField jtfNumber;
    private javax.swing.JTextField jtfNumberReference;
    private javax.swing.JTextField jtfPkRo;
    private javax.swing.JTextField jtfPrepaymentsCy;
    private javax.swing.JTextField jtfPrepaymentsCyCurRo;
    private javax.swing.JTextField jtfQuantityTotal;
    private javax.swing.JTextField jtfRecordManualBranchRo;
    private javax.swing.JTextField jtfRecordManualDateRo;
    private javax.swing.JTextField jtfRecordManualNumberRo;
    private javax.swing.JTextField jtfReqNum;
    private javax.swing.JTextField jtfSalesAgentBizPartnerRo;
    private javax.swing.JTextField jtfSalesAgentRo;
    private javax.swing.JTextField jtfSalesSupervisorBizPartnerRo;
    private javax.swing.JTextField jtfSalesSupervisorRo;
    private javax.swing.JTextField jtfShipments;
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
        
        if (!moDps.getAuxKeepDpsData()) {
            moDps = createNewDps(null);
            setBizPartner(keyBp, keyBpb, keyBpbAdd);
            renderRecordAutomatic();
            renderPk();

            for (STableRow row : moPaneGridEntries.getGridRows()) {
                SDataDpsEntry entry = (SDataDpsEntry) row.getData();
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
                SDataDpsNotes notes = (SDataDpsNotes) row.getData();
                notes.setPkNotesId(0);
                notes.setIsRegistryNew(true);
            }

            for (STableRow row : moPaneGridCustomAcc.getGridRows()) {
                SDataDpsCustomAccEntry entry = (SDataDpsCustomAccEntry) row.getData();
                entry.setPkAccEntryId(0);
                entry.setIsRegistryNew(true);
            }
        }
        
        actionEdit();
    }

    @Override
    public void formReset() {
        mbResetingForm = true;

        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;
        mbDocBeingImported = false;

        moDps = createNewDps(null);
        moLastDpsSource = null;
        moGuiDpsLink = null;
        moBizPartner = null;
        moBizPartnerBranch = null;
        moBizPartnerBranchAddressMain = null;
        moBizPartnerBranchAddress = null;
        moBizPartnerCategory = null;
        moRecordUserKey = null;
        moRecordUser = null;
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
        moRecordUserLock = null;
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        moRecordUserRedisLock = null;
        */
        moRecordUserSLock = null;
        mnSalesAgentId_n = 0;
        mnSalesAgentBizPartnerId_n = 0;
        mnSalesSupervisorId_n = 0;
        mnSalesSupervisorBizPartnerId_n = 0;
        mbFormSettingsOk = true;
        mbIsNumberEditable = true;
        mnNumbersApprovalYear = 0;
        mnNumbersApprovalNumber = 0;
        mbPostEmissionEdition = false;
        mbHasRightOrderDelay = mbIsSales ? 
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD_DELAY).HasRight :
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD_DELAY).HasRight;
        mbHasRightOmitSourceDoc = mbIsSales ? 
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_OMT_DOC_SRC).HasRight :
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_OMT_DOC_SRC).HasRight;
        msFileXmlJustLoaded = "";
        moFilePdfJustLoaded = null;
        moAddendaAmc71Manager = null;
        moComprobante33 = null;
        moComprobante40 = null;
        msXmlUuid = "";
        
        moPaneGridEntries.createTable();
        moPaneGridEntries.clearTableRows();
        moPaneGridEntries.setGridViewStatus(STableConstants.VIEW_STATUS_ALIVE);
        jtbEntryFilter.setSelected(true);

        moPaneGridNotes.createTable();
        moPaneGridNotes.clearTableRows();
        moPaneGridNotes.setGridViewStatus(STableConstants.VIEW_STATUS_ALIVE);
        jtbNotesFilter.setSelected(true);

        moPaneGridCustomAcc.createTable();
        moPaneGridCustomAcc.clearTableRows();
        moPaneGridCustomAcc.setGridViewStatus(STableConstants.VIEW_STATUS_ALIVE);
        moPaneGridCustomAcc.getTable().getSelectionModel().addListSelectionListener(this);
        
        // no filter for costomized-accounting entries

        moPickerBizPartner.formReset();
        moComboBoxGroupCfdCceGroupAddressee.reset();

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
        jckIsRebill.setSelected(false);
        jtbSwitchCustomAcc.setSelected(false);
        jtbLinkTicket.setSelected(false);

        mvScaleTicDps = new Vector<>();
        
        moFieldDate.setFieldValue(moDps.getDate());
        moFieldDateDoc.setFieldValue(moDps.getDate());
        moFieldDateStartCredit.setFieldValue(moDps.getDate());
        moFieldFkPaymentTypeId.setFieldValue(new int[] { SDataConstantsSys.TRNS_TP_PAY_CASH });
        moFieldFkDpsNatureId.setFieldValue(new int[] { SDataConstantsSys.TRNU_DPS_NAT_DEF });
        if (!isApplingFunctionalAreas()) {
            moFieldFkFunctionalAreaId.setFieldValue(new int[] { SModSysConsts.CFGU_FUNC_NA });
        }
        else if ((jcbFkFunctionalAreaId.getItemCount() - 1) == 1) {
            jcbFkFunctionalAreaId.setSelectedIndex(1);
        }
        moFieldFkIncotermId.setFieldValue(new int[] { SModSysConsts.LOGS_INC_NA });
        moFieldFkModeOfTransportationTypeId.setFieldValue(new int[] { SModSysConsts.LOGS_TP_MOT_NA });
        moFieldFkCarrierTypeId.setFieldValue(new int[] { SModSysConsts.LOGS_TP_CAR_NA });
        
        renderBasicSettings();
        renderDpsType();
        renderDpsValue();
        renderDpsStatus();
        renderRecordManual();
        renderRecordAutomatic();
        renderPk();
        
        renderBizPartner();
        
        renderDateMaturity();
        renderSalesAgentOfBizPartner(null);
        renderSalesAgentOfDocument(null);
        renderSalesSupervisorOfBizPartner(null);
        renderSalesSupervisorOfDocument(null);

        itemStateChangedDateDoc();
        itemStateChangedDateStartCredit();
        itemStateChangedRecordUser();
        itemStateChangedShipments();
        itemStateChangedIsDiscountDocApplying(false);
        itemStateChangedIsDiscountDocPercentage(false);

        itemStateChangedFkPaymentTypeId(true);
        itemStateChangedFkCurrencyId(false); // do not calculate document's total
        itemStateChangedFkIncotermId();
        itemStateChangedFkCarrierTypeId();
        itemStateChangedFkVehicleTypeId_n();
        itemStateChangedAddCfdAddendaSubtype();
        itemStateChangedAddAmc71SupplierGln();

        jTabbedPane.setSelectedIndex(TAB_ETY);
        
        updateDpsFieldsStatus(true);
        disableCfdAddendaFields();
        
        jbEdit.setEnabled(false);
        jbEditHelp.setEnabled(false);
        jbOk.setEnabled(true);

        mnOldFunctionalAreaId = 0;
        mdOldExchangeRate = 0;
        mdOldDiscountDocPercentage = 0;
        mbOldIsDiscountDocApplying = false;

        mnAccCurrentAction = 0;
        mbCustomAccPrepared = false;
        renderCustomAccSubtotal();
        
        moDialogDpsTime = null;
        manDpsTime = null;
        
        moDialogCfdRelatedDocs = null;
        moCfdRelatedDocs = null;
        
        jtaCfdiRelatedDocs.setText("");
        
        mbResetingForm = false;
    }

    @Override
    public void formRefreshCatalogues() {
        mbResetingForm = true;

        moPickerBizPartner.formRefreshOptionPane();
        moFormEntry.formRefreshCatalogues();
        moFormEntryWizard.formRefreshCatalogues();
        moFormNotes.formRefreshCatalogues();
        moFormCom.formRefreshCatalogues();

        SFormUtilities.populateComboBox(miClient, jcbFkPaymentTypeId, SDataConstants.TRNS_TP_PAY);
        SFormUtilities.populateComboBox(miClient, jcbFkLanguageId, SDataConstants.CFGU_LAN);
        SFormUtilities.populateComboBox(miClient, jcbFkDpsNatureId, SDataConstants.TRNU_DPS_NAT);
        
        if (!isApplingFunctionalAreas()) {
            SFormUtilities.populateComboBox(miClient, jcbFkFunctionalAreaId, SModConsts.CFGU_FUNC); // load all functional areas, "non-applying" inclusive
        }
        else {
            SFormUtilities.populateComboBox(miClient, jcbFkFunctionalAreaId, SModConsts.CFGU_FUNC, new int[] { miClient.getSessionXXX().getUser().getPkUserId() }); // load only user-asigned functional areas, "non-applying" may not be included
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
        SFormUtilities.populateComboBox(miClient, jcbFkProductionOrderId_n, SDataConstants.MFG_ORD, new Object[] { "" + SDataConstantsSys.MFGS_ST_ORD_END + ", " + SDataConstantsSys.MFGS_ST_ORD_CLS, SDataConstants.MFGX_ORD_MAIN_NA, false });
        
        // XML catalogs for CFD:
        SCfdXmlCatalogs catalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        catalogs.populateComboBox(jcbCfdiPaymentWay, SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY, miClient.getSession().getSystemDate());
        catalogs.populateComboBox(jcbCfdiPaymentMethod, SDataConstantsSys.TRNS_CFD_CAT_PAY_MET, miClient.getSession().getSystemDate());
        catalogs.populateComboBox(jcbCfdiTaxRegimeIssuing, SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, miClient.getSession().getSystemDate());
        catalogs.populateComboBox(jcbCfdiTaxRegimeReceptor, SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, miClient.getSession().getSystemDate());
        catalogs.populateComboBox(jcbGblPeriodicity, SDataConstantsSys.TRNS_CFD_CAT_PER, miClient.getSession().getSystemDate());
        catalogs.populateComboBox(jcbGblMonth, SDataConstantsSys.TRNS_CFD_CAT_MON, miClient.getSession().getSystemDate());
        catalogs.populateComboBox(jcbCfdiCfdiUsage, SDataConstantsSys.TRNS_CFD_CAT_CFD_USE, miClient.getSession().getSystemDate());
        catalogs.populateComboBox(jcbCfdCceMoveReason, SDataConstantsSys.TRNS_CFD_CAT_INT_MOV_REA, miClient.getSession().getSystemDate());
        catalogs.populateComboBox(jcbCfdCceOperationType, SDataConstantsSys.TRNS_CFD_CAT_INT_OPN_TP, miClient.getSession().getSystemDate());
        catalogs.populateComboBox(jcbCfdCceRequestKey, SDataConstantsSys.TRNS_CFD_CAT_INT_REQ_KEY, miClient.getSession().getSystemDate());
        catalogs.populateComboBox(jcbExportation, SDataConstantsSys.TRNS_CFD_CAT_EXP, miClient.getSession().getSystemDate());
        
        moComboBoxGroupCfdCceGroupAddressee.clear();
        moComboBoxGroupCfdCceGroupAddressee.addComboBox(mbIsSales ? SDataConstants.BPSX_BP_INT_CUS : SDataConstants.BPSX_BP_INT_SUP, jcbCfdCceFkAddresseeBizPartner);
        moComboBoxGroupCfdCceGroupAddressee.addComboBox(SDataConstants.BPSU_BPB, jcbCfdCceFkAddresseeBizPartnerBranch);
        moComboBoxGroupCfdCceGroupAddressee.addComboBox(SDataConstants.BPSU_BPB_ADD, jcbCfdCceFkAddresseeBizPartnerBranchAddress);
        
        mbResetingForm = false;
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
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
        
        if (jtbLinkTicket.isSelected() && mvScaleTicDps.isEmpty()) {
            miClient.showMsgBoxInformation("Se seleccionó la opcion de vincular boletos desde báscula pero no se vinculó ninguno\n"
                    + "Se tendra que volver a elegir la opción si se quiere vincular boletos de esta forma.");
        }

        if (!mbPostEmissionEdition) {
            if (!validation.getIsError()) {
                String msg = validateDateLinks();
                boolean isCfdEmissionRequired = isCfdEmissionRequired();

                if (!msg.isEmpty()) {
                    validation.setMessage(msg);
                    validation.setComponent(jftDate);
                }
                else if (moDpsType == null) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDpsType.getText() + "'.");
                }
                else if (moBizPartner == null) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlBizPartner.getText() + "'.");
                }
                else if (moBizPartner.getIsDeleted() || moBizPartnerCategory.getIsDeleted()) {
                    validation.setMessage("¡El asociado de negocios está eliminado!");
                }
                else if (isCfdAddendaRequired() && moBizPartner.getDbmsCategorySettingsCus().getCompanyKey().isEmpty()) {
                    validation.setMessage("¡No se han especificado la clave de esta empresa como proveedor del cliente " + moBizPartner.getBizPartner() + ".\n" +
                            "Actualizar este dato en el Módulo Configuración, vista 'Clientes', forma de captura 'Cliente', pestaña 'Información adicional', campo 'Clave de la empresa'.");
                }
                else if (!moDps.isEstimate() && !moDps.isOrder() && !SDataUtilities.isPeriodOpen(miClient, moFieldDate.getDate())) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
                    validation.setComponent(jftDate);
                }
                else if (moDps.getPkYearId() != SLibTimeUtilities.digestYear(moFieldDate.getDate())[0]) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_YEAR);
                    validation.setComponent(jftDate);
                }
                else if (jcbNumberSeries.isEditable() && moFieldNumberSeries.getString().length() > SDataDps.LEN_SERIES) { // only when combo box of number series is editable
                    validation.setMessage("La longitud máxima para el campo 'Serie' es " + SDataDps.LEN_SERIES + ".");
                    validation.setComponent(jcbNumberSeries);
                }
                else if (isCfdEmissionRequired && jcbCfdiPaymentWay.getSelectedIndex() <= 0) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlCfdiPaymentWay.getText() + "'.");
                    validation.setComponent(jcbCfdiPaymentWay);
                }
                else if (isCfdEmissionRequired && jcbCfdiPaymentMethod.getSelectedIndex() <= 0) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlCfdiPaymentMethod.getText() + "'.");
                    validation.setComponent(jcbCfdiPaymentMethod);
                }
                else if (moDps.getTotal_r() < 0) {
                    validation.setMessage("El total del documento no puede ser negativo.");
                    validation.setComponent(moPaneGridEntries);
                    validation.setTabbedPaneIndex(TAB_ETY);
                }
                else {
                    // validate document number generated by system:

                    try {
                        int[] dpsKey;

                        if (mbIsSales || mbIsDpsEstimate || mbIsDpsContract || mbIsDpsOrder) {
                            dpsKey = SDataUtilities.obtainDpsKey(miClient, moFieldNumberSeries.getString(), moFieldNumber.getString(), moDpsType.getPrimaryKey());
                        }
                        else {
                            dpsKey = SDataUtilities.obtainDpsKeyForBizPartner(miClient, moFieldNumberSeries.getString(), moFieldNumber.getString(), moDpsType.getPrimaryKey(), moBizPartner.getPrimaryKey());
                        }

                        if (dpsKey != null && (moDps.getIsRegistryNew() || (!SLibUtilities.compareKeys(dpsKey, moDps.getPrimaryKey())))) {
                            // validate document number:

                            if (moDps.getIsRegistryNew() && (mbIsSales || mbIsDpsEstimate || mbIsDpsContract || mbIsDpsOrder)) {
                                obtainNextNumber(); // attempt to get a new number

                                dpsKey = SDataUtilities.obtainDpsKey(miClient, moFieldNumberSeries.getString(), moFieldNumber.getString(), moDpsType.getPrimaryKey());
                                if (dpsKey != null) {
                                    jcbNumberSeries.setEnabled(true);
                                    jtfNumber.setEnabled(true);
                                    jtfNumber.setFocusable(true);
                                }
                            }

                            if (dpsKey != null) {
                                // the number for this very document already exists!
                                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, dpsKey, SLibConstants.EXEC_MODE_SILENT);
                                validation.setMessage("Otro documento '" + moDpsType.getDpsType() + "' del '" + SLibUtils.DateFormatDate.format(dps.getDate()) + "' ya tiene el folio '" + STrnUtils.formatDocNumber(moFieldNumberSeries.getString(), moFieldNumber.getString()) + "'.");
                                validation.setComponent(jcbNumberSeries.isEnabled() ? jcbNumberSeries : jtfNumber);
                            }
                        }
                        else if (moFieldNumberSeries.getDataType() == SLibConstants.DATA_TYPE_KEY) {
                            // validate document number according to curren number series:

                            int num = SLibUtilities.parseInt(moFieldNumber.getString());
                            int numMin = ((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[0];
                            int numMax = ((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[1]; // -1 means unlimited

                            if (num < numMin) {
                                validation.setMessage("El valor para el campo '" + jlNumber.getText() + "' no puede ser menor a " + numMin + "'.");
                                validation.setComponent(jtfNumber);
                            }
                            else if (numMax != -1 && num > numMax) { // -1 means unlimited
                                validation.setMessage("El valor para el campo '" + jlNumber.getText() + "' no puede ser mayor a " + numMax + "'.");
                                validation.setComponent(jtfNumber);
                            }
                        }
                    }
                    catch (Exception e) {
                        SLibUtilities.printOutException(this, e);
                        validation.setMessage(e.toString());
                    }
                }
                
                // check if month can be changed, when neccesary:
                
                if (!validation.getIsError() && !moDps.getIsRegistryNew() && mbIsDpsInvoice) {
                    int[] dateOld = SLibTimeUtils.digestMonth(moDps.getOldDate());
                    int[] dateNew = SLibTimeUtils.digestMonth(moFieldDate.getDate());

                    if (dateOld[0] != dateNew[0] || dateOld[1] != dateNew[1]) { // does month or year change?
                        String[] months = SLibTimeUtils.createMonthsOfYearStd(Calendar.LONG);
                        String change = "de " + months[dateOld[1] - 1] + " " + dateOld[0] + " a " + months[dateNew[1] - 1] + " " + dateNew[0];
                        
                        try {
                            // exception will be thrown if documet has payments:
                            moDps.validateDocBalance(miClient.getSession().getDatabase().getConnection(), "No se puede cambiar el período del documento " + change + ":\n");
                            
                            // no exception thrown, so confirm change of month or year:
                            if (miClient.showMsgBoxConfirm("La fecha original del documento '" + SLibUtils.DateFormatDate.format(moDps.getOldDate()) + "' ha sido cambiada a '" + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + "'.\n"
                                    + "¿Está seguro que desea cambiar el período del documento " + change + "?") != JOptionPane.YES_OPTION) {
                                throw new Exception(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlDate.getText() + "':\n"
                                        + "puede ser '" + SLibUtils.DateFormatDate.format(moDps.getOldDate()) + "' o al menos una fecha de " + months[dateOld[1] - 1] + " " + dateOld[0] + ".");
                            }
                        }
                        catch (Exception e) {
                            validation.setMessage(e.getMessage());
                            validation.setComponent(jftDate);
                        }
                    }
                }

                // validate exchange rate, if neccesary:

                if (!validation.getIsError() && !isLocalCurrency()) {
                    if (SLibUtils.roundAmount(moFieldExchangeRate.getDouble()) == 1d && 
                            miClient.showMsgBoxConfirm("¡La moneda del documento es '" + moFieldFkCurrencyId.getString() + "'!\n"
                                    + "¿Es correcto que el valor del campo '" + jlExchangeRate.getText() + "' sea " + SLibUtils.getDecimalFormatExchangeRate().format(1d) + "?") != JOptionPane.YES_OPTION) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlExchangeRate.getText() + "'.");
                        validation.setComponent(jtfExchangeRate);
                    }
                    else {
                        double xrt = 0;

                        try {
                            xrt = SDataUtilities.obtainExchangeRate(miClient, moFieldFkCurrencyId.getKeyAsIntArray()[0], moFieldDate.getDate());
                        }
                        catch (Exception e) {
                            SLibUtilities.renderException(this, e);
                        }

                        if (xrt != 0d) {
                            int decs = SLibUtils.getDecimalFormatExchangeRate().getMaximumFractionDigits();

                            if (SLibUtils.round(Math.abs(SLibUtils.round(xrt, decs) - SLibUtils.round(moFieldExchangeRate.getDouble(), decs)), decs) >= 0.0001 &&
                                    miClient.showMsgBoxConfirm("¡El tipo de cambio del día " + SLibUtils.DateFormatDate.format(moFieldDate.getDate()) + " para '" + moFieldFkCurrencyId.getString() + "' es " + SLibUtils.getDecimalFormatExchangeRate().format(xrt) + "!\n"
                                            + "¿Es correcto que el valor del campo '" + jlExchangeRate.getText() + "' sea " + SLibUtils.getDecimalFormatExchangeRate().format(moFieldExchangeRate.getDouble()) + "?") != JOptionPane.YES_OPTION) {
                                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlExchangeRate.getText() + "'.");
                                validation.setComponent(jtfExchangeRate);
                            }
                        }
                    }
                }

                // validate other all purpose fields:

                if (!validation.getIsError()) {
                    if (jcbFkFunctionalAreaId.getSelectedIndex() <= 0) {
                        String message = "";
                        if (mnOldFunctionalAreaId != 0 && SFormUtilities.isComboBoxItemContained(jcbFkFunctionalAreaId, new int[] { mnOldFunctionalAreaId })) {
                            message = "La opción original del campo '" + jlFkFunctionalAreaId.getText() + "', '" + (String) miClient.getSession().readField(SModConsts.CFGU_FUNC, new int[] { mnOldFunctionalAreaId }, SDbRegistry.FIELD_NAME) + "', "
                                    + "no está asignada al usuario '" + miClient.getSession().getUser().getName() + "'.";
                        }
                        validation.setMessage((!message.isEmpty() ? message + "\n" : "") + SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkFunctionalAreaId.getText() + "'.");
                        validation.setComponent(jcbFkFunctionalAreaId);
                    }
                    else if (jckRecordUser.isSelected() && moRecordUserKey == null) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jckRecordUser.getText() + "'.");
                        validation.setComponent(jbRecordManualSelect);
                    }
                    else if (jckShipments.isSelected() && moFieldShipments.getInteger() < 0) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jckShipments.getText() + "'.");
                        validation.setComponent(jtfShipments);
                    }
                    else if (isCfdEmissionRequired && moFieldFkPaymentTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.TRNS_TP_PAY_CASH && moFieldCfdiPaymentWay.getKey().toString().compareTo(DCfdi40Catalogs.FDP_POR_DEF) == 0) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlCfdiPaymentWay.getText() + "'.");
                        validation.setComponent(jcbCfdiPaymentWay);
                    }
                    else if (isCfdEmissionRequired && moFieldFkPaymentTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.TRNS_TP_PAY_CREDIT && moFieldCfdiPaymentWay.getKey().toString().compareTo(DCfdi40Catalogs.FDP_POR_DEF) != 0) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlCfdiPaymentWay.getText() + "' (" + DCfdi40Catalogs.FDP_POR_DEF + ").");
                        validation.setComponent(jcbCfdiPaymentWay);
                    }
                    else if (isCfdEmissionRequired && moFieldFkPaymentTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.TRNS_TP_PAY_CASH && moFieldCfdiPaymentMethod.getKey().toString().compareTo(DCfdi40Catalogs.MDP_PUE) != 0) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlCfdiPaymentMethod.getText() + "' (" + DCfdi40Catalogs.MDP_PUE + ").");
                        validation.setComponent(jcbCfdiPaymentMethod);
                    }
                    else if (isCfdEmissionRequired && moFieldFkPaymentTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.TRNS_TP_PAY_CREDIT && moFieldCfdiPaymentMethod.getKey().toString().compareTo(DCfdi40Catalogs.MDP_PUE) == 0 &&
                            miClient.showMsgBoxConfirm(SGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + jlCfdiPaymentMethod.getText() + "' \"" + moFieldCfdiPaymentMethod.getFieldValue() + "\", en ventas a crédito, no está permitido según las disposiciones fiscales.\n" + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlCfdiPaymentMethod.getText() + "'.");
                        validation.setComponent(jcbCfdiPaymentMethod);
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
                    else if (mbIsDpsContract && moFieldDateDocDelivery_n.getDate() == null) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDateDocDelivery_n.getText() + "'.");
                        validation.setComponent(jftDateDocDelivery_n);
                    }
                    else if (mbIsDpsContract && moFieldDateDocLapsing_n.getDate() == null) {
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
                    else if (isCfdEmissionRequired && moPaneGridEntries.getTableGuiRowCount() == 0) {
                        validation.setMessage("El documento debe tener al menos una partida.");
                        validation.setTabbedPaneIndex(TAB_ETY);
                    }
                    else if (mnSalesSupervisorId_n != 0 && mnSalesAgentId_n == 0) {
                        validation.setMessage("Se debe ingresar un valor para el campo '" + jlSalesAgent.getText() + "'.");
                        validation.setComponent(jbSalesAgent);
                        validation.setTabbedPaneIndex(TAB_MKT);
                    }
                    else if (mnSalesSupervisorId_n != 0 && mnSalesSupervisorId_n == mnSalesAgentId_n) {
                        validation.setMessage("Se debe ingresar un valor diferente para el campo '" + jlSalesAgent.getText() + "'.");
                        validation.setComponent(jbSalesAgent);
                        validation.setTabbedPaneIndex(TAB_MKT);
                    }
                    else {
                        // validate shipping information if Incoterm has been set:

                        if (mbIsSales && (mbIsDpsInvoice || mbIsDpsAdjustment) && !moBizPartner.isDomestic(miClient) && moFieldFkIncotermId.getKeyAsIntArray()[0] == SModSysConsts.LOGS_INC_NA) {
                            validation.setMessage("Se debe ingresar un valor diferente para el campo '" + jlFkIncotermId.getText() + "'.");
                            validation.setComponent(jcbFkIncotermId);
                            validation.setTabbedPaneIndex(TAB_MKT);
                        }
                        else if (moFieldFkIncotermId.getKeyAsIntArray()[0] != SModSysConsts.LOGS_INC_NA) {
                            if (jcbFkSpotSrcId_n.isEnabled() && jcbFkSpotSrcId_n.getSelectedIndex() <= 0) {
                                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkSpotSrcId_n.getText() + "'.");
                                validation.setComponent(jcbFkSpotSrcId_n);
                                validation.setTabbedPaneIndex(TAB_MKT);
                            }
                            else if (jcbFkSpotDesId_n.isEnabled() && jcbFkSpotDesId_n.getSelectedIndex() <= 0) {
                                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkSpotDesId_n.getText() + "'.");
                                validation.setComponent(jcbFkSpotDesId_n);
                                validation.setTabbedPaneIndex(TAB_MKT);
                            }
                            else {
                                switch (moFieldFkIncotermId.getKeyAsIntArray()[0]) {
                                    case SModSysConsts.LOGS_INC_EXW:
                                    case SModSysConsts.LOGS_INC_FCA:
                                    case SModSysConsts.LOGS_INC_CPT:
                                    case SModSysConsts.LOGS_INC_CIP:
                                    case SModSysConsts.LOGS_INC_DAT:
                                    case SModSysConsts.LOGS_INC_DAP:
                                    case SModSysConsts.LOGS_INC_DAF:
                                    case SModSysConsts.LOGS_INC_DDU:       
                                    case SModSysConsts.LOGS_INC_DDP:
                                    case SModSysConsts.LOGS_INC_DPU:    
                                        if (moFieldFkModeOfTransportationTypeId.getKeyAsIntArray()[0] == SModSysConsts.LOGS_TP_MOT_NA) {
                                            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFkModeOfTransportationTypeId.getText() + "'.");
                                            validation.setComponent(jcbFkModeOfTransportationTypeId);
                                            validation.setTabbedPaneIndex(TAB_MKT);
                                        }
                                        break;
                                    case SModSysConsts.LOGS_INC_FAS:
                                    case SModSysConsts.LOGS_INC_FOB:
                                    case SModSysConsts.LOGS_INC_DES:
                                    case SModSysConsts.LOGS_INC_DEQ:
                                    case SModSysConsts.LOGS_INC_CFR:
                                    case SModSysConsts.LOGS_INC_CIF:
                                        if (moFieldFkModeOfTransportationTypeId.getKeyAsIntArray()[0] != SModSysConsts.LOGS_TP_MOT_SEA) {
                                            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFkModeOfTransportationTypeId.getText() + "'.");
                                            validation.setComponent(jcbFkModeOfTransportationTypeId);
                                            validation.setTabbedPaneIndex(TAB_MKT);
                                        }
                                        break;
                                    default:
                                }
                            }
                        }
                    }
                    
                    // check if items of entries of this document need to be added from another source document:

                    if (!validation.getIsError()) {
                        for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
                            SDataDpsEntry entry = (SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData();

                            try {
                                if (!mbHasRightOmitSourceDoc) { // condition for check items
                                    STrnUtils.checkItemStandaloneDoc(miClient.getSession(), moDps.getDpsTypeKey(), entry.getFkItemId(), entry.hasDpsLinksAsDestiny());
                                }
                            }
                            catch (Exception e) {
                                SLibUtilities.printOutException(this, e);
                                validation.setMessage(e.getMessage());
                                validation.setComponent(moPaneGridEntries);
                                validation.setTabbedPaneIndex(TAB_ETY);
                                break;
                            }
                        }
                    }
                    
                    if (!validation.getIsError()) {
                        for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
                            SDataDpsEntry entry = (SDataDpsEntry) (SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData();
                            try {
                                if (STrnUtils.getIsItemDeleted(miClient.getSession(), entry.getFkItemId())) {
                                    validation.setMessage("La partida # " + (i + 1) + " no se puede guardar debido a que el ítem '" + entry.getConcept() + " (" + entry.getConceptKey() + ")' está eliminado.");
                                    validation.setComponent(moPaneGridEntries);
                                    validation.setTabbedPaneIndex(TAB_ETY);
                                    break;
                                }
                            }
                            catch (Exception e) { }
                        }
                    }
                    
                    if (!validation.getIsError()) {
                        ArrayList<Integer> taxReg = new ArrayList<>();
                        for (STableRow row : moPaneGridEntries.getGridRows()) {
                            SDataDpsEntry entry = (SDataDpsEntry) row.getData();
                            if (!taxReg.contains(entry.getFkTaxRegionId())){
                                taxReg.add(entry.getFkTaxRegionId());
                            }
                        }
                        if (taxReg.size() > 1) {
                            if (miClient.showMsgBoxConfirm("El documento tiene partidas con " + taxReg.size() + " distintas regiones de impuestos.\n¿Desea continuar?") != JOptionPane.OK_OPTION) {
                                validation.setMessage("Cambiar la región de impuestos de las partidas para que coincidan.");
                            }
                        }
                    }
                    
                    if (!validation.getIsError() && mbHasDpsLinksAsDes) {
                        for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
                            SDataDpsEntry entry = (SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData();
                            try {
                                if(entry.hasDpsLinksAsDestiny()){
                                    if (STrnUtilities.getTaxRegionDpsEty(miClient, entry.getDbmsDpsLinksAsDestiny().get(0).getDbmsSourceDpsEntryKey()) != entry.getFkTaxRegionId()) {
                                        if (miClient.showMsgBoxConfirm("La región de impuestos de la partida #" + (i + 1) + " es diferente a la región de impuestos de la partida del documento de origen.\n¿Desea continuar?") != JOptionPane.OK_OPTION) {
                                            validation.setMessage("Seleccionar la región de impuestos de la partida #" + (i + 1) + " del documento de origen.");
                                        }
                                    }
                                }
                            }
                            catch (Exception e) {}
                        }
                    }
                    
                    if (!validation.getIsError() && mbIsDpsAdjustment) {
                        for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
                            SDataDpsEntry entry = (SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData();
                            if (!entry.getDbmsDpsAdjustmentsAsAdjustment().isEmpty()) {
                                if (entry.getIsRegistryNew() && !entry.getFlagDpsEtyOpened() && entry.getIsPrepayment()) {
                                    validation.setMessage("La partida #" + (i + 1) + " fue importada de una partida de anticipos y no le fue especificado si se debe de contabilizar en una cuenta de dinero o en anticipos facturados por aplicar.\n"
                                            + "Favor de modificar la partida y especificar.");
                                }
                            }
                        }   
                    }
                    
                    double prepaymentsCy = mdPrepaymentsCy;
                    double applicationsCy = 0;

                    // check prepayments:

                    if (!validation.getIsError() && mdPrepaymentsCy != 0) {
                        boolean operationsAvailable = false;

                        for (STableRow row : moPaneGridEntries.getGridRows()) {
                            SDataDpsEntry dpsEntry = (SDataDpsEntry) row.getData();
                            if (dpsEntry.isAccountable()) {
                                switch (dpsEntry.getOperationsType()) {
                                    case SDataConstantsSys.TRNX_OPS_TYPE_OPS_PREPAY:
                                        prepaymentsCy = SLibUtils.round(prepaymentsCy + dpsEntry.getSubtotalCy_r(), 2);
                                        break;
                                    case SDataConstantsSys.TRNX_OPS_TYPE_ADJ_PREPAY:
                                        prepaymentsCy = SLibUtils.round(prepaymentsCy - dpsEntry.getSubtotalCy_r(), 2);
                                        break;
                                    case SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS_APP_PREPAY:
                                        applicationsCy = SLibUtils.round(applicationsCy + dpsEntry.getDiscountDocCy(), 2);
                                        break;
                                    case SDataConstantsSys.TRNX_OPS_TYPE_ADJ_OPS_APP_PREPAY:
                                        applicationsCy = SLibUtils.round(applicationsCy + dpsEntry.getDiscountDocCy(), 2);
                                        break;
                                    case SDataConstantsSys.TRNX_OPS_TYPE_ADJ_APP_PREPAY:
                                        applicationsCy = SLibUtils.round(applicationsCy + dpsEntry.getSubtotalCy_r(), 2);
                                        break;
                                    default:
                                        operationsAvailable = true;
                                }
                            }
                        }

                        if (applicationsCy > prepaymentsCy) {
                            if (miClient.showMsgBoxConfirm("La aplicación de anticipos facturados $" + SLibUtils.getDecimalFormatAmount().format(applicationsCy) + " " + jtfCurrencyKeyRo.getText() + " "
                                    + "no debería ser mayor a los anticipos facturados $" + SLibUtils.getDecimalFormatAmount().format(prepaymentsCy) + " " + jtfCurrencyKeyRo.getText() + "." +
                                    SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                                validation.setMessage("La aplicación de anticipos facturados $" + SLibUtils.getDecimalFormatAmount().format(applicationsCy) + " " + jtfCurrencyKeyRo.getText() + " "
                                        + "NO debería ser mayor a los anticipos facturados $" + SLibUtils.getDecimalFormatAmount().format(prepaymentsCy) + " " + jtfCurrencyKeyRo.getText() + ".");
                                validation.setComponent(moPaneGridEntries);
                                validation.setTabbedPaneIndex(TAB_ETY);
                            }
                        }
                        else if (mbIsDpsInvoice && operationsAvailable && prepaymentsCy > 0 && applicationsCy == 0) {
                            if (miClient.showMsgBoxConfirm("'" + moBizPartner.getBizPartner() + "' tiene anticipos facturados a su favor por $" + SLibUtils.getDecimalFormatAmount().format(prepaymentsCy) + " " + jtfCurrencyKeyRo.getText() + ","
                                + "\n¿está seguro que NO desea aplicarlos en este documento?") != JOptionPane.YES_OPTION) {
                                validation.setMessage("Se deberían aplicar anticipos facturados en este documento.");
                                validation.setComponent(moPaneGridEntries);
                                validation.setTabbedPaneIndex(TAB_ETY);
                            }
                        }
                    }

                    // validate costomized complementary shipping data:

                    if (!validation.getIsError() && mbIsSales && (mbIsDpsOrder || mbIsDpsInvoice)) {
                        String shipmentMessageMissingData = validateRequiredShipmentData();

                        if (!shipmentMessageMissingData.isEmpty()) {
                                validation.setMessage(shipmentMessageMissingData);
                                validation.setComponent(moPaneGridEntries);
                                validation.setTabbedPaneIndex(TAB_ETY);
                        }
                        else {
                            String shipmentMessage = validateProvisionalShipmentData();

                            if (!shipmentMessage.isEmpty() && miClient.showMsgBoxConfirm(shipmentMessage) != JOptionPane.YES_OPTION) {
                                validation.setMessage("Es necesario revisar la información de las partidas del documento.");
                                validation.setComponent(moPaneGridEntries);
                                validation.setTabbedPaneIndex(TAB_ETY);
                            }
                        }
                    }

                    // validate contract's calendar of monthly deliveries:

                    if (!validation.getIsError() && mbIsDpsContract) {
                        try {
                            int deliveryMonths;
                            int periodMonths = SGuiUtilities.getPeriodMonths(moFieldDateDocDelivery_n.getDate(), moFieldDateDocLapsing_n.getDate());

                            for (int row = 0; row < moPaneGridEntries.getTableGuiRowCount(); row++) {
                                deliveryMonths = 0;

                                // validate number of monthly deliveries on each document row, it must match the number of months of the delivery period of this document:

                                for (SDataDpsEntryPrice price : ((SDataDpsEntry) moPaneGridEntries.getTableRow(row).getData()).getDbmsEntryPrices()) {
                                     if (!price.getIsDeleted()) {
                                         deliveryMonths++;
                                     }
                                }

                                if (deliveryMonths != periodMonths) {
                                    validation.setMessage("El número de entregas mensuales capturadas en la partida #" + (row + 1) + " "
                                            + "no coincide con el número de los meses (" + periodMonths + ") del periodo de entrega del documento "
                                            + "(del " + SLibUtils.DateFormatDate.format(moFieldDateDocDelivery_n.getDate()) + " al " + SLibUtils.DateFormatDate.format(moFieldDateDocLapsing_n.getDate()) + ").");
                                    validation.setComponent(moPaneGridEntries);
                                    validation.setTabbedPaneIndex(TAB_ETY);
                                    break;
                                }

                                // validate that all monthly delivery really belongs to delivery period of this document:

                                for (SDataDpsEntryPrice price : ((SDataDpsEntry) moPaneGridEntries.getTableRow(row).getData()).getDbmsEntryPrices()) {
                                    if (!price.getIsDeleted()) {
                                        if (!SLibTimeUtilities.isBelongingToPeriod(SLibTimeUtilities.createDate(price.getContractPriceYear(), price.getContractPriceMonth()), SLibTimeUtilities.getBeginOfMonth(moFieldDateDocDelivery_n.getDate()), SLibTimeUtilities.getEndOfMonth(moFieldDateDocLapsing_n.getDate()))) {
                                            validation.setMessage("La entrega mensual '" + miClient.getSessionXXX().getFormatters().getDateYearMonthFormat().format(SLibTimeUtilities.createDate(price.getContractPriceYear(), price.getContractPriceMonth())) + "' de la partida #" + (row + 1) + " "
                                                    + "no se encuentra dentro del periodo de entrega del documento.");
                                            validation.setComponent(moPaneGridEntries);
                                            validation.setTabbedPaneIndex(TAB_ETY);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        catch (Exception e) {
                            SLibUtilities.printOutException(this, e);
                            validation.setMessage(e.toString());
                            validation.setComponent(moPaneGridEntries);
                            validation.setTabbedPaneIndex(TAB_ETY);
                        }
                    }

                    if (!validation.getIsError() && (mbIsDpsInvoice || mbIsDpsAdjustment)) {
                        // check manual accounting record:

                        if (jckRecordUser.isSelected()) {
                            if (moRecordUser == null) {
                                validation.setMessage("No fue posible leer el registro '" + jckRecordUser.getText() + "'.");
                                validation.setComponent(jbRecordManualSelect);
                            }
                            else if (moRecordUserSLock == null) {
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
                                    /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                                    SSrvUtils.verifyLockStatus(miClient.getSession(), moRecordUserLock);
                                    */
                                    /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                                    SRedisLockUtils.verifyLockStatus(miClient, moRecordUserRedisLock);
                                    */
                                    SLockUtils.verifyLockStatus(miClient, moRecordUserSLock);
                                }
                                catch (Exception e) {
                                    validation.setMessage("No fue posible validar el acceso exclusivo al registro '" + jckRecordUser.getText() + "'.\n" + e);
                                    validation.setComponent(jbRecordManualSelect);
                                }
                            }
                        }

                        // validate CFD emission:

                        if (!validation.getIsError() && isCfdEmissionRequired) {
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
                            else if ((jcbGblPeriodicity.getSelectedIndex() > 0 && (jcbGblMonth.getSelectedIndex() <= 0 || moFieldCfdiGblYear.getInteger() == 0)) ||
                                    (jcbGblMonth.getSelectedIndex() > 0 && (jcbGblPeriodicity.getSelectedIndex() <= 0 || moFieldCfdiGblYear.getInteger() == 0)) ||
                                    (moFieldCfdiGblYear.getInteger() != 0 && (jcbGblPeriodicity.getSelectedIndex() <= 0 || jcbGblMonth.getSelectedIndex() <= 0))) {
                                validation.setMessage("Todos los campos con la información de la factura global deben contener un valor.");
                                validation.setComponent(jcbGblPeriodicity);
                            }
                            else if (isCfdAddendaRequired()) {
                                switch (moBizPartnerCategory.getFkCfdAddendaTypeId()) {
                                    case SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA:
                                        if (mbIsDpsInvoice && moFieldDateDocDelivery_n.getDate() == null) {
                                            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDateDocDelivery_n.getText() + "'.");
                                            validation.setComponent(jftDateDocDelivery_n);
                                        }
                                        else if (moFieldAddSorianaRemisiónFecha.getDate() == null) {
                                            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlAddSorianaRemisiónFecha.getText() + "'.\n" + SLibConstants.MSG_ERR_GUI_DATE);
                                            validation.setComponent(jftAddSorianaRemisiónFecha);
                                            validation.setTabbedPaneIndex(TAB_CFD_ADD);
                                        }
                                        break;

                                    case SDataConstantsSys.BPSS_TP_CFD_ADD_LOREAL:
                                    case SDataConstantsSys.BPSS_TP_CFD_ADD_BACHOCO:
                                    case SDataConstantsSys.BPSS_TP_CFD_ADD_MODELO:
                                    case SDataConstantsSys.BPSS_TP_CFD_ADD_AMECE71:
                                        if (moFieldNumberReference.getString().isEmpty()) {
                                            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jtfNumberReference.getToolTipText() + "'.");
                                            validation.setComponent(jtfNumberReference);
                                        }
                                        break;

                                    default:
                                }
                            }

                            if (!validation.getIsError()) {
                                 if ((isCfdCfdiRelatedRequired() || applicationsCy != 0) && (moCfdRelatedDocs == null || moCfdRelatedDocs.getRowCfdRelatedDocs().isEmpty())) {
                                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jbCfdiRelatedDocs.getText() + "'.");
                                    validation.setComponent(jbCfdiRelatedDocs);
                                    validation.setTabbedPaneIndex(TAB_CFD_XML);
                                 }
                            }

                            // Validate tax regime receptor:
                            if (!validation.getIsError()) {
                                if (mbIsSales && (mbIsDpsInvoice || mbIsDpsAdjustment) && jcbCfdiTaxRegimeReceptor.getSelectedIndex() <= 0) {
                                    validation.setMessage("El regimén fiscal del receptor debe de tener un valor.");
                                    validation.setComponent(jcbCfdiTaxRegimeReceptor);
                                }
                            }

                            // validate International Commerce settings:

                            if (!validation.getIsError() && isCfdIntCommerceRequired()) {
                                if (moFieldFkIncotermId.getKeyAsIntArray()[0] == SModSysConsts.LOGS_INC_NA) {
                                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFkIncotermId.getText() + "'.");
                                    validation.setComponent(jcbFkIncotermId);
                                    validation.setTabbedPaneIndex(TAB_MKT);
                                }
                                else if (moFieldCfdCceCertificateOrigin.getInteger() == 1 && moFieldCfdCceNumberCertificateOrigin.getString().isEmpty()) {
                                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlCfdCceNumberCertificateOrigin.getText() + "'.");
                                    validation.setComponent(jtfCfdCceNumberCertificateOrigin);
                                    validation.setTabbedPaneIndex(TAB_CFD_INT_COM);

                                }
                                else if (moFieldCfdCceCertificateOrigin.getInteger() == 0 && !moFieldCfdCceNumberCertificateOrigin.getString().isEmpty()) {
                                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_NOT_REQ + "'" + jlCfdCceNumberCertificateOrigin.getText() + "'.");
                                    validation.setComponent(jtfCfdCceNumberCertificateOrigin);
                                    validation.setTabbedPaneIndex(TAB_CFD_INT_COM);
                                }
                                else if (jcbCfdCceFkBizPartnerAddressee.getSelectedIndex() > 0 && jcbCfdCceFkAddresseeBizPartner.getSelectedIndex() > 0) {
                                    validation.setMessage("Se puede especificar un valor para el campo '" + jlCfdCceFkBizPartnerAddressee.getText() + "' o para el campo '" + "'" + jlCfdCceFkAddresseeBizPartner.getText() + "', pero no para ambos a la vez.");
                                    validation.setComponent(jcbCfdCceFkBizPartnerAddressee);
                                    validation.setTabbedPaneIndex(TAB_CFD_INT_COM);
                                }
                                else if ((jcbCfdCceFkAddresseeBizPartner.getSelectedIndex() > 0 || jcbCfdCceFkAddresseeBizPartnerBranch.getSelectedIndex() > 0 || jcbCfdCceFkAddresseeBizPartnerBranchAddress.getSelectedIndex() > 0) && 
                                        (jcbCfdCceFkAddresseeBizPartner.getSelectedIndex() <= 0 || jcbCfdCceFkAddresseeBizPartnerBranch.getSelectedIndex() <= 0 || jcbCfdCceFkAddresseeBizPartnerBranchAddress.getSelectedIndex() <= 0)) {
                                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlCfdCceFkAddresseeBizPartner.getText() + "', '" + jlCfdCceFkAddresseeBizPartnerBranch.getText() + "' y '" + jlCfdCceFkAddresseeBizPartnerBranchAddress.getText() + "'.");
                                    validation.setComponent(jcbCfdCceFkAddresseeBizPartner);
                                    validation.setTabbedPaneIndex(TAB_CFD_INT_COM);
                                }
                                else {
                                    // validate addressee:

                                    if (jcbCfdCceFkBizPartnerAddressee.getSelectedIndex() > 0) {
                                        SDataBizPartnerAddressee addressee = (SDataBizPartnerAddressee) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP_ADDEE, moFieldCfdCceBizPartnerAddressee.getKeyAsIntArray(), SLibConstants.EXEC_MODE_VERBOSE);
                                        if (addressee.getFkCountryId_n() == 0 || addressee.getFkCountryId_n() == miClient.getSessionXXX().getParamsErp().getFkCountryId()) {
                                            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlCfdCceFkBizPartnerAddressee.getText() + "':\n"
                                                    + "el destinatario debe ser del extranjero.");
                                            validation.setComponent(jcbCfdCceFkBizPartnerAddressee);
                                            validation.setTabbedPaneIndex(TAB_CFD_INT_COM);
                                        }
                                    }
                                    else if (jcbCfdCceFkAddresseeBizPartner.getSelectedIndex() > 0) {
                                        if (moFieldCfdCceAddresseeBizPartner.getKeyAsIntArray()[0] == moBizPartner.getPkBizPartnerId()) {
                                            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlCfdCceFkAddresseeBizPartner.getText() + "':\n"
                                                    + "el destinatario debe ser distinto al receptor del comprobante.");
                                            validation.setComponent(jcbCfdCceFkAddresseeBizPartner);
                                            validation.setTabbedPaneIndex(TAB_CFD_INT_COM);
                                        }
                                        else {
                                            SDataBizPartnerBranchAddress address = (SDataBizPartnerBranchAddress) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB_ADD, moFieldCfdCceAddresseeBizPartnerBranchAddress.getKeyAsIntArray(), SLibConstants.EXEC_MODE_VERBOSE);
                                            if (address.getFkCountryId_n() == 0 || address.getFkCountryId_n() == miClient.getSessionXXX().getParamsErp().getFkCountryId()) {
                                                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlCfdCceFkAddresseeBizPartner.getText() + "':\n"
                                                        + "el destinatario debe ser del extranjero.");
                                                validation.setComponent(jcbCfdCceFkAddresseeBizPartner);
                                                validation.setTabbedPaneIndex(TAB_CFD_INT_COM);
                                            }
                                        }
                                    }
                                }
                            }

                            // validate CFDI usage when some item requires a specific one:

                            if (!validation.getIsError()) {
                                for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
                                    SDataDpsEntry entry = ((SDataDpsEntry) moPaneGridEntries.getTableRow(i).getData());
                                        for (SDataItemConfigBizPartner itemConfig : moBizPartner.getDbmsItemConfigs()) {
                                            if (entry.getFkItemId() == itemConfig.getPkItemId() && !itemConfig.getCfdiUsage().isEmpty() && !itemConfig.getIsDeleted() &&
                                                    !(moDps.getDbmsDataDpsCfd() != null)  ) {
                                                if (!moFieldCfdiCfdiUsage.getKey().toString().equals(itemConfig.getCfdiUsage())) {
                                                    validation.setMessage(SLibConstants.MSG_ERR_GUI_CFDI_USE + "'" + itemConfig.getCfdiUsage() + "'.");
                                                    validation.setComponent(jcbCfdiCfdiUsage);
                                                    validation.setTabbedPaneIndex(TAB_CFD_XML);
                                                    break;
                                                }
                                            }
                                        }
//                                    }    
                                }
                            }
                        }

                        if (!validation.getIsError() && jtbSwitchCustomAcc.isSelected() ) {
                            // validate costomized accounting:

                            if (!canSwitchOnCustomAcc()) {
                                validation.setMessage("Realizar las correcciones indicadas para personalizar la contabilización del documento.");
                            }
                            else if (mnAccCurrentAction != 0) {
                                validation.setMessage("Se debe completar la captura actual de partidas de personalización de contabilización del documento.");
                                validation.setTabbedPaneIndex(TAB_ACC);
                            }
                            else if (moPaneGridCustomAcc.getGridRows().isEmpty()) {
                                validation.setMessage("El documento debe tener al menos una partida de personalización de contabilización.");
                                validation.setTabbedPaneIndex(TAB_ACC);
                            }
                            else {
                                calculateCustomAccSubtotal(null);

                                if (!SLibUtils.compareAmount(moDps.getSubtotalCy_r(), mdAccSubtotalCy)) {
                                    validation.setMessage("El subtotal del documento en la moneda original "
                                            + "(" + jtfAccSubtotalCyCur.getText() + "), "
                                            + "$" + SLibUtils.getDecimalFormatAmount().format(moDps.getSubtotalCy_r()) + ",\n"
                                            + "no coincide con el subtotal de la personalización de contabilización, "
                                            + "$" + SLibUtils.getDecimalFormatAmount().format(mdAccSubtotalCy) + ".\n"
                                            + "SUGERENCIA:\n"
                                            + "Usar la utilería '" + jbExeWizardAccSubtotal.getToolTipText() + "' disponible al modificar la partida que requiera el ajuste.");
                                    validation.setTabbedPaneIndex(TAB_ACC);
                                }
                                else if (!isLocalCurrency() && !SLibUtils.compareAmount(moDps.getSubtotal_r(), mdAccSubtotal)) {
                                    validation.setMessage("El subtotal del documento en la moneda local "
                                            + "(" + jtfAccSubtotalCur.getText() + "), "
                                            + "$" + SLibUtils.getDecimalFormatAmount().format(moDps.getSubtotal_r()) + ",\n"
                                            + "no coincide con el subtotal de la personalización de contabilización, "
                                            + "$" + SLibUtils.getDecimalFormatAmount().format(mdAccSubtotal) + ".\n"
                                            + "SUGERENCIA:\n"
                                            + "Usar la utilería '" + jbExeWizardAccSubtotal.getToolTipText() + "' disponible al modificar la partida que requiera el ajuste.");
                                    validation.setTabbedPaneIndex(TAB_ACC);
                                }
                            }
                        }
                    }
                    
                    /*
                     * IMPORTANT!: THIS IS THE VERY LAST BLOCK OF VALIDATIONS IN THIS SECTION!!!
                     */
                    if (!validation.getIsError()) {
                        if (moComprobante33 != null && jckValidateOnSaveFileXml.isSelected()) {
                            validation = validateCfdi33();
                        }
                        else if (moComprobante40 != null && jckValidateOnSaveFileXml.isSelected()) {
                            validation = validateCfdi40();
                        }
                    }
                    // WARNING!: PLEASE DO NOT ADD ANY CODE AROUND THIS LINE!!!
                    if (!validation.getIsError()) {
                        // credit status of business partner:

                        if (!isBizPartnerCreditOk(moBizPartnerCategory.getEffectiveRiskTypeId(), false)) {
                            validation.setIsError(true);
                            validation.setComponent(jftDate);
                        }
                        else {
                            // check if DPS can be saved:

                            SDataDps dps = (SDataDps) getRegistry();
                            SServerRequest request = new SServerRequest(SServerConstants.REQ_DB_CAN_SAVE);
                            SServerResponse response = null;

                            request.setPacket(dps);
                            response = miClient.getSessionXXX().request(request);

                            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK || response.getResultType() != SLibConstants.DB_CAN_SAVE_YES) {
                                validation.setMessage(response.getMessage().isEmpty() ? SLibConstants.MSG_ERR_UTIL_UNKNOWN_ERR : response.getMessage());
                            } // WARNING!: PLEASE DO NOT ADD ANY CODE AFTER THIS LINE!!!
                        } // WARNING!: PLEASE DO NOT ADD ANY CODE AFTER THIS LINE!!!
                    } // WARNING!: PLEASE DO NOT ADD ANY CODE AFTER THIS LINE!!!
                } // WARNING!: PLEASE DO NOT ADD ANY CODE AFTER THIS LINE!!!
                // WARNING!: PLEASE DO NOT ADD ANY CODE AFTER THIS LINE!!!
                /*
                 * WARNING!: This last validation is the only one allowed to be at the end of this method!!!
                 */
                if (!validation.getIsError()) {
                    if (jckIsDeleted.isSelected()) {
                        if (miClient.showMsgBoxConfirm("El documento está eliminado. Puede guardarlo de nuevo como eliminado o reactivarlo.\n¿Desea reactivar el documento?") == JOptionPane.YES_OPTION) {
                            jckIsDeleted.setSelected(false);
                        } // WARNING!: PLEASE DO NOT ADD ANY CODE AFTER THIS LINE!!!
                    } // WARNING!: PLEASE DO NOT ADD ANY CODE AFTER THIS LINE!!!
                } // WARNING!: PLEASE DO NOT ADD ANY CODE AFTER THIS LINE!!!
            } // WARNING!: PLEASE DO NOT ADD ANY CODE AFTER THIS LINE!!!
        } // WARNING!: PLEASE DO NOT ADD ANY CODE AFTER THIS LINE!!!
        // WARNING!: PLEASE DO NOT ADD ANY CODE AFTER THIS LINE!!!
        return validation; // WARNING!: PLEASE DO NOT ADD ANY CODE AFTER THIS LINE!!!
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
        
        // set form field values:

        moFieldDate.setFieldValue(moDps.getDate());
        moFieldDateDoc.setFieldValue(moDps.getDateDoc());
        moFieldDateStartCredit.setFieldValue(moDps.getDateStartCredit());
        moFieldDateShipment_n.setFieldValue(moDps.getDateShipment_n());
        moFieldDateDelivery_n.setFieldValue(moDps.getDateDelivery_n());
        moFieldDateDocLapsing_n.setFieldValue(moDps.getDateDocLapsing_n());
        moFieldDateDocDelivery_n.setFieldValue(moDps.getDateDocDelivery_n());
        moFieldNumberSeries.setFieldValue(moDps.getNumberSeries());
        moFieldNumber.setFieldValue(moDps.getNumber());
        moFieldNumberReference.setFieldValue(moDps.getNumberReference());
        moFieldConditionsPayment.setFieldValue(moDps.getConditionsPayment());
        
        if (areNumberSeriesBySystem()) {
            if (moDps.getIsRegistryNew()) {
                obtainNextNumber();
            }
            else {
                mnNumbersApprovalYear = moDps.getApprovalYear();
                mnNumbersApprovalNumber = moDps.getApprovalNumber();
            }
            
            if (jcbNumberSeries.getSelectedIndex() == -1) {
                int number = SLibUtilities.parseInt(moDps.getNumber());
                SFormComponentItem item = new SFormComponentItem(new Object[] { moDps.getNumberSeries() }, moDps.getNumberSeries());
                item.setComplement(new int[] { number, number });
                jcbNumberSeries.removeAllItems();
                jcbNumberSeries.addItem(item);
                jcbNumberSeries.setSelectedIndex(0);
                mbIsNumberEditable = false;     // there is no way to validate any number change for there is not a current company branch selected in user session
            }
        }

        moFieldDaysOfCredit.setFieldValue(moDps.getDaysOfCredit());
        moFieldIsDiscountDocApplying.setFieldValue(moDps.getIsDiscountDocApplying());
        moFieldIsDiscountDocPercentage.setFieldValue(moDps.getIsDiscountDocPercentage());
        moFieldDiscountDocPercentage.setFieldValue(moDps.getDiscountDocPercentage());
        moFieldExchangeRate.setFieldValue(moDps.getExchangeRate());
        moFieldExchangeRateSystem.setFieldValue(moDps.getExchangeRateSystem());
        
        moFieldIsCopy.setFieldValue(moDps.getIsCopy());
        moFieldIsAudited.setFieldValue(moDps.getIsAudited());
        moFieldIsAuthorized.setFieldValue(moDps.getIsAuthorized());
        moFieldIsSystem.setFieldValue(moDps.getIsSystem());
        moFieldIsDeleted.setFieldValue(moDps.getIsDeleted());
        
        moFieldShipments.setFieldValue(moDps.getShipments());
        moFieldIsLinked.setFieldValue(moDps.getIsLinked());
        moFieldIsClosed.setFieldValue(moDps.getIsClosed());

        moFieldFkPaymentTypeId.setFieldValue(new int[] { moDps.getFkPaymentTypeId() });
        moFieldFkLanguajeId.setFieldValue(new int[] { moDps.getFkLanguajeId() });
        moFieldFkDpsNatureId.setFieldValue(new int[] { moDps.getFkDpsNatureId() });
        moFieldFkFunctionalAreaId.setFieldValue(new int[] { moDps.getFkFunctionalAreaId() });
        moFieldFkCurrencyId.setFieldValue(new int[] { !mbIsLocalCurrency ? moDps.getFkCurrencyId() : miClient.getSessionXXX().getParamsErp().getFkCurrencyId()});
        
        // set business partner, set aswell business partner default preferences when document is new:

        if (! mbMatRequestImport || moDps.getFkBizPartnerId_r() > 0) {
            setBizPartner(new int[] { moDps.getFkBizPartnerId_r() }, new int[] { moDps.getFkBizPartnerBranchId() }, new int[] { moDps.getFkBizPartnerBranchId(), moDps.getFkBizPartnerBranchAddressId() });
        }
        else if (moBizPartner != null) {
            setBizPartner((int[]) moBizPartner.getPrimaryKey(), (int[]) moBizPartnerBranch.getPrimaryKey(), (int[]) moBizPartnerBranchAddress.getPrimaryKey());
        }
        
        // check if payment way should be taken from document:
        if (moDps.getDbmsDataDpsCfd() != null && !moDps.getDbmsDataDpsCfd().getPaymentWay().isEmpty()) {
            moFieldCfdiPaymentWay.setFieldValue(moDps.getDbmsDataDpsCfd().getPaymentWay());
        }

        // check if payment method should be taken from document:
        if (moDps.getDbmsDataDpsCfd() != null && !moDps.getDbmsDataDpsCfd().getPaymentMethod().isEmpty()) {
            moFieldCfdiPaymentMethod.setFieldValue(moDps.getDbmsDataDpsCfd().getPaymentMethod());
        }
        
        moFieldFkProductionOrderId_n.setFieldValue(new int[] { moDps.getFkMfgYearId_n(), moDps.getFkMfgOrderId_n() });
        
        moFieldFkContactId_n.setFieldValue(new int[] { moDps.getFkContactBizPartnerBranchId_n(), moDps.getFkContactContactId_n() }); // setBizPartner() populates contact combobox for sales documents
        
        // continue setting form field values:
        
        moFieldFkIncotermId.setFieldValue(new int[] { moDps.getFkIncotermId() });
        itemStateChangedFkIncotermId();
        moFieldFkSpotSrcId_n.setFieldValue(new int[] { moDps.getFkSpotSourceId_n() });
        moFieldFkSpotDesId_n.setFieldValue(new int[] { moDps.getFkSpotDestinyId_n() });
        
        moFieldFkModeOfTransportationTypeId.setFieldValue(new int[] { moDps.getFkModeOfTransportationTypeId() });
        moFieldFkCarrierTypeId.setFieldValue(new int[] { moDps.getFkCarrierTypeId() });
        itemStateChangedFkCarrierTypeId();
        moFieldFkCarrierId_n.setFieldValue(new int[] { moDps.getFkCarrierId_n() });
        moFieldFkVehicleTypeId_n.setFieldValue(new int[] { moDps.getFkVehicleTypeId_n() });
        itemStateChangedFkVehicleTypeId_n();
        moFieldFkVehicleId_n.setFieldValue(new int[] { moDps.getFkVehicleId_n() });
          
        moFieldDriver.setFieldValue(moDps.getDriver());
        moFieldPlate.setFieldValue(moDps.getPlate());
        moFieldTicket.setFieldValue(moDps.getTicket());
        
        // entries:
        
        for (SDataDpsEntry entry : moDps.getDbmsDpsEntries()) {
            moPaneGridEntries.addTableRow(new SDataDpsEntryRow(entry, ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter()));
        }
        
        moPaneGridEntries.renderTableRows();
        moPaneGridEntries.setTableRowSelection(0);
        
        // notes:

        for (SDataDpsNotes notes : moDps.getDbmsDpsNotes()) {
            if (notes.getPkNotesId() != SLibConstants.UNDEFINED || notes.getIsAllDocs()) {
                moPaneGridNotes.addTableRow(new SDataDpsNotesRow(notes));
            }
        }
        
        moPaneGridNotes.renderTableRows();
        moPaneGridNotes.setTableRowSelection(0);
        
        for (SDataScaleTicketDps t : moDps.getDbmsScaleTickets()) {
            mvScaleTicDps.add(t);
        }
        
        // customized-accounting entries:
        
        /*
         * NOTE FOR CUSTOM ACCOUNTING ENTRIES:
         * Custom accounting entries should be rendered here, if exist,
         * but they will be rendered until corresponding tab is selected.
         */
        
        boolean thereAreCustomAccEntries = !moDps.getDbmsDpsCustomAccEntries().isEmpty();
        
        // user accounting record:

        if (!moDps.getIsRecordAutomatic()) {
            try {
                if (readRecordUser(moDps.getDbmsRecordKey())) {
                    moRecordUserKey = moDps.getDbmsRecordKey();
                }
            }
            catch (Exception ex) {
                SLibUtilities.renderException(this, ex);
                Logger.getLogger(SFormDps.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // tailor form extra data and controls:
        
        if (mbMatRequestImport) {
            calculateTotal();
            renderEntries();
            moPaneGridEntries.renderTableRows();
            moPaneGridEntries.setTableRowSelection(0);
        }

        renderBasicSettings();
        renderDpsType();
        renderDpsValue();
        renderDpsStatus();
        renderRecordManual();
        renderRecordAutomatic();
        renderPk();
        
        renderDateMaturity();
        renderSalesAgentOfBizPartner(moDps.getFkSalesAgentBizPartnerId_n() == 0 ? null : new int[] { moDps.getFkSalesAgentBizPartnerId_n() });
        renderSalesAgentOfDocument(moDps.getFkSalesAgentId_n() == 0 ? null : new int[] { moDps.getFkSalesAgentId_n() });
        renderSalesSupervisorOfBizPartner(moDps.getFkSalesSupervisorBizPartnerId_n() == 0 ? null : new int[] { moDps.getFkSalesSupervisorBizPartnerId_n() });
        renderSalesSupervisorOfDocument(moDps.getFkSalesSupervisorId_n() == 0 ? null : new int[] { moDps.getFkSalesSupervisorId_n() });
        
        jckDateDoc.setSelected(!moDps.getDate().equals(moDps.getDateDoc()));
        jckDateStartCredit.setSelected(!moDps.getDate().equals(moDps.getDateStartCredit()));
        jckRecordUser.setSelected(!moDps.getIsRecordAutomatic());
        jckShipments.setSelected(moFieldShipments.getInteger() > 0);
        jckIsRebill.setSelected(moDps.getIsRebill());
        jtbSwitchCustomAcc.setSelected(thereAreCustomAccEntries);
        jTabbedPane.setEnabledAt(TAB_ACC, thereAreCustomAccEntries);
        
        itemStateChangedDateDoc();
        itemStateChangedDateStartCredit();
        itemStateChangedRecordUser();
        itemStateChangedShipments();
        itemStateChangedIsDiscountDocApplying(false);
        itemStateChangedIsDiscountDocPercentage(false);

        itemStateChangedFkPaymentTypeId(false);
        itemStateChangedFkCurrencyId(false); // do not calculate document's total

        if (isCfdEmissionRequired()) {
            setAddendaData();
            
            if (moDps.getDbmsDataDpsCfd() != null) {
                if (!moDps.getDbmsDataDpsCfd().getTaxRegimeIssuing().isEmpty()) {
                    moFieldCfdiTaxRegimeIss.setFieldValue(moDps.getDbmsDataDpsCfd().getTaxRegimeIssuing());
                }
                
                if (!moDps.getDbmsDataDpsCfd().getTaxRegimeReceptor().isEmpty()) {
                    moFieldCfdiTaxRegimeRec.setFieldValue(moDps.getDbmsDataDpsCfd().getTaxRegimeReceptor());
                }
                else {
                    moFieldCfdiTaxRegimeRec.setFieldValue(moBizPartnerCategory.getTaxRegime());
                }
                
                if (!moDps.getDbmsDataDpsCfd().getGlobalPeriodocity().isEmpty()) {
                    moFieldCfdiGblPeriodicity.setFieldValue(moDps.getDbmsDataDpsCfd().getGlobalPeriodocity());
                }
                
                if (!moDps.getDbmsDataDpsCfd().getGlobalMonths().isEmpty()) {
                    moFieldCfdiGblMonth.setFieldValue(moDps.getDbmsDataDpsCfd().getGlobalMonths());
                }
                
                if (moDps.getDbmsDataDpsCfd().getGlobalYear() != 0) {
                    moFieldCfdiGblYear.setFieldValue(moDps.getDbmsDataDpsCfd().getGlobalYear());
                }
                
                if (moDps.getIsRegistryNew() && isCfdCfdiRelatedRequired()) {
                    moFieldCfdiCfdiUsage.setFieldValue(DCfdi40Catalogs.ClaveUsoCfdiDevolucionesDescuentos);
                }
                else if (!moDps.getDbmsDataDpsCfd().getCfdiUsage().isEmpty()) {
                    moFieldCfdiCfdiUsage.setFieldValue(moDps.getDbmsDataDpsCfd().getCfdiUsage());
                }
                
                if (!moDps.getDbmsDataDpsCfd().getConfirmation().isEmpty()) {
                    moFieldCfdiConfirmation.setFieldValue(moDps.getDbmsDataDpsCfd().getConfirmation());
                }
                
                if (moDps.getDbmsDataDpsCfd().getCfdRelatedDocs() != null) {
                    moCfdRelatedDocs = moDps.getDbmsDataDpsCfd().getCfdRelatedDocs();
                    jtaCfdiRelatedDocs.setText(moCfdRelatedDocs.toString());
                }
                else {
                    jtaCfdiRelatedDocs.setText("");
                }
                
                renderCfdiFirstRelatedDps();

                if (isCfdIntCommerceRequired()) {
                    moFieldCfdCceMoveReason.setFieldValue(moDps.getDbmsDataDpsCfd().getCfdCceMotivoTraslado());
                    moFieldCfdCceOperationType.setFieldValue(moDps.getDbmsDataDpsCfd().getCfdCceTipoOperacion());
                    moFieldCfdCceRequestKey.setFieldValue(moDps.getDbmsDataDpsCfd().getCfdCceClaveDePedimento());
                    moFieldCfdCceExportation.setFieldValue(moDps.getDbmsDataDpsCfd().getExportation());
                    moFieldCfdCceCertificateOrigin.setFieldValue(SLibUtils.parseInt(moDps.getDbmsDataDpsCfd().getCfdCceCertificadoOrigen()));
                    moFieldCfdCceNumberCertificateOrigin.setFieldValue(moDps.getDbmsDataDpsCfd().getCfdCceNumCertificadoOrigen());
                    moFieldCfdCceSubdivision.setFieldValue(SLibUtils.parseInt(moDps.getDbmsDataDpsCfd().getCfdCceSubdivision()));
                    moFieldCfdCceExchangeRateUsd.setFieldValue(SLibUtils.parseDouble(moDps.getDbmsDataDpsCfd().getCfdCceTipoCambioUsd()));
                    moFieldCfdCceTotalUsd.setFieldValue(SLibUtils.parseDouble(moDps.getDbmsDataDpsCfd().getCfdCceTotalUsd()));
                    
                    moFieldCfdCceBizPartnerAddressee.setFieldValue(new int[] { moDps.getFkBizPartnerAddresseeId_n() });
                    moFieldCfdCceAddresseeBizPartner.setFieldValue(new int[] { moDps.getFkAddresseeBizPartnerId_nr() });
                    moFieldCfdCceAddresseeBizPartnerBranch.setFieldValue(new int[] { moDps.getFkAddresseeBizPartnerBranchId_n() });
                    moFieldCfdCceAddresseeBizPartnerBranchAddress.setFieldValue(new int[] { moDps.getFkAddresseeBizPartnerBranchId_n(), moDps.getFkAddresseeBizPartnerBranchAddressId_n() });
                }
            }
        }
        
        if (isCfdXmlFilePermitted()) {
            if (moDps.getDbmsDataCfd() != null) { // Cuando se modifica un DPS existente desde la vista de facturas.
                moFieldCfdiXmlFile.setFieldValue(moDps.getDbmsDataCfd().getDocXmlName());
                msFileXmlJustLoaded = "";
            }
            else if (!moDps.getAuxFileXmlAbsolutePath().isEmpty()) { // Cuando se crea un DPS a partir de un archivo XML.
                moFieldCfdiXmlFile.setFieldValue(moDps.getAuxFileXmlName());
                msFileXmlJustLoaded = moDps.getAuxFileXmlAbsolutePath();
            }
            
            if (moDps.getDbmsDataPdf() != null) { // Cuando el DPS posee un archivo PDF asociado.
                moFieldCfdiPdfFile.setFieldValue(moDps.getDbmsDataPdf().getDocPdfName());
                moFilePdfJustLoaded = null;
            }
        }

        updateDpsFieldsStatus(false);

        // The sequence of asumptions are set this way in order to preserve correspondence with method actionEditHelp():

        mbIsPeriodOpen = SDataUtilities.isPeriodOpen(miClient, moDps.getDate());
        
        boolean denyEdition = mbParamIsReadOnly || moDps.getIsAudited() || moDps.getIsAuthorized() || moDps.getIsSystem() ||
                (!mbIsPeriodOpen && (mbIsDpsInvoice || mbIsDpsAdjustment)) || moDps.getFkDpsValidityStatusId() != SDataConstantsSys.TRNS_ST_DPS_VAL_EFF ||
                (mnParamCurrentUserPrivilegeLevel != SDataConstantsSys.UNDEFINED && mnParamCurrentUserPrivilegeLevel < SUtilConsts.LEV_AUTHOR);

        jbEdit.setEnabled(!(denyEdition || moDps.getXtaHasSuppFiles() || moDps.getXtaHasAuthWeb() ||
                (moDps.isDocumentSal() && moDps.getDbmsDataCfd() != null && moDps.getDbmsDataCfd().isCfdi() && moDps.getDbmsDataCfd().isStamped()) ||
                (moDps.isAdjustmentSal() && moDps.getDbmsDataCfd() != null && moDps.getDbmsDataCfd().isCfdi() && moDps.getDbmsDataCfd().isStamped()) ||
                (moDps.isDocumentSal() && moDps.getDbmsDataCfd() != null && moDps.getDbmsDataCfd().isCfdi() && moDps.getDbmsDataCfd().getIsProcessingWebService()) ||
                (moDps.isDocumentSal() && moDps.getDbmsDataCfd() != null && moDps.getDbmsDataCfd().isCfdi() && moDps.getDbmsDataCfd().getIsProcessingStorageXml()) ||
                (moDps.isAdjustmentSal() && moDps.getDbmsDataCfd() != null && moDps.getDbmsDataCfd().isCfdi() && moDps.getDbmsDataCfd().getIsProcessingWebService()) ||
                (moDps.isAdjustmentSal() && moDps.getDbmsDataCfd() != null && moDps.getDbmsDataCfd().isCfdi() && moDps.getDbmsDataCfd().getIsProcessingStorageXml())));
        
        jbEditHelp.setEnabled(!jbEdit.isEnabled());
        jbOk.setEnabled(false);
        
        boolean enableMinor = !jbEdit.isEnabled() && !denyEdition;
        jbEditLogistics.setEnabled(enableMinor);
        jbEditNotes.setEnabled(enableMinor);

        mnOldFunctionalAreaId = moDps.getFkFunctionalAreaId();
        mdOldExchangeRate = moFieldExchangeRate.getDouble();
        mdOldDiscountDocPercentage = moFieldDiscountDocPercentage.getDouble();
        mbOldIsDiscountDocApplying = moFieldIsDiscountDocApplying.getBoolean();
        
        if (moGuiDpsLink == null) {
            moGuiDpsLink = new SGuiDpsLink(miClient);
            moGuiDpsLink.addDataDpsDestiny(moDps);
        }
        
        if (moDps.getDbmsDataCfdBol() != null) {
            moBillOfLading = new SDbBillOfLading();
            try {
                moBillOfLading.read(miClient.getSession(), new int[] { moDps.getDbmsDataCfdBol().getFkBillOfLadingId_n() } );
            }
            catch (Exception e) {}
            moFieldCfdiBillOfLading.setFieldValue((moBillOfLading.getSeries().isEmpty() ? moBillOfLading.getNumber() : moBillOfLading.getSeries() + moBillOfLading.getNumber()) + " - " + 
                            SLibUtils.DateFormatDate.format(moBillOfLading.getDate()));
        }

        mbResetingForm = false;
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (mbPostEmissionEdition) {
            moDps.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
            
            moDps.getDbmsDpsNotes().clear();
            for (STableRow row : moPaneGridNotes.getGridRows()) {
                moDps.getDbmsDpsNotes().add((SDataDpsNotes) row.getData());
            }
            
            SDataMinorChangesDps minorChangesDps = new SDataMinorChangesDps();
            minorChangesDps.setData(moDps);
            return minorChangesDps;
        }
        else {
            if (moDps.getIsRegistryNew()) {
                moDps.setPkYearId(SLibTimeUtilities.digestYear(moFieldDate.getDate())[0]);
                moDps.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
            }
            else {
                moDps.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
            }

            calculateTotal();

            moDps.setDate(moFieldDate.getDate());
            if (manDpsTime != null) {
                moDps.setAuxDpsTime(manDpsTime);
            }
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
            moDps.setConditionsPayment(moFieldConditionsPayment.getString());

            if (mbIsSales && (mbIsDpsInvoice || mbIsDpsAdjustment)) {
                if (moDps.getIsRegistryNew()) {
                    moDps.setApprovalYear(((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[2]);
                    moDps.setApprovalNumber(((int[]) ((SFormComponentItem) jcbNumberSeries.getSelectedItem()).getComplement())[3]);
                }
            }
            else {
                moDps.setApprovalYear(0);
                moDps.setApprovalNumber(0);
            }

            moDps.setIsRebill(jckIsRebill.isSelected());

            moDps.setDriver(jtbLinkTicket.isSelected() || moFieldDriver.getComponent().isEnabled() ? moFieldDriver.getString() : "");
            moDps.setPlate(jtbLinkTicket.isSelected() || moFieldPlate.getComponent().isEnabled() ? moFieldPlate.getString() : "");
            moDps.setTicket(jtbLinkTicket.isSelected() || moFieldTicket.getComponent().isEnabled() ? moFieldTicket.getString() : "");
            moDps.setIsRecordAutomatic(!jckRecordUser.isSelected());
            moDps.setShipments(!moFieldShipments.getComponent().isEnabled() ? 0 : moFieldShipments.getInteger());
            //moDps.setPayments(...     // about to be deprecated!
            //moDps.setIsLinked(...
            //moDps.setIsClosed(...
            //moDps.setIsAudited(...
            //moDps.setIsAuthorized(...
            //moDps.setIsRecordAutomatic(...
            moDps.setIsCopy(moFieldIsCopy.getBoolean());
            //moDps.setIsSystem(...
            moDps.setIsDeleted(moFieldIsDeleted.getBoolean());  // when document was deleted, user can reactivate it on save

            moDps.setFkDpsCategoryId(moDpsType.getPkDpsCategoryId());
            moDps.setFkDpsClassId(moDpsType.getPkDpsClassId());
            moDps.setFkDpsTypeId(moDpsType.getPkDpsTypeId());
            moDps.setFkPaymentTypeId(moFieldFkPaymentTypeId.getKeyAsIntArray()[0]);
            moDps.setFkPaymentSystemTypeId(SDataConstantsSys.TRNU_TP_PAY_SYS_NA);   // XXX remove ASAP (Sergio Flores, 2017-08-09)!
            //moDps.setPaymentMethod(...
            //moDps.setPaymentAccount(...
            moDps.setFkDpsStatusId(SDataConstantsSys.TRNS_ST_DPS_EMITED);   // all saved documents have "emited" status

            // Fields non editable in form (allready set for new documents in formReset() function):
            //moDps.setFkDpsAuthorizationStatusId(...
            //moDps.setFkCompanyBranchId(...

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
            moDps.setFkFunctionalAreaId(jcbFkFunctionalAreaId.getSelectedIndex() > 0 ? moFieldFkFunctionalAreaId.getKeyAsIntArray()[0] : SModSysConsts.CFGU_FUNC_NA);
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

            // Fields non editable in form (allready set for new documents in formReset() function):
            //moDps.setFkUserClosedId(...
            //moDps.setFkUserAuditedId(...
            //moDps.setFkUserAuthorizedId(...

            boolean recalculateEntries = isEntriesRecalculationNeeded();

            moDps.getDbmsDpsEntries().clear();
            for (STableRow row : moPaneGridEntries.getGridRows()) {
                SDataDpsEntry entry = (SDataDpsEntry) row.getData();
                if (!entry.getIsRegistryEdited()) {
                    entry.setIsRegistryEdited(recalculateEntries);  // force entry recalculation when saved
                }
                entry.setConcept(entry.getConcept().length() > 130 ? entry.getConcept().substring(0, 130) : entry.getConcept());
                moDps.getDbmsDpsEntries().add(entry);
            }

            moDps.getDbmsDpsNotes().clear();
            for (STableRow row : moPaneGridNotes.getGridRows()) {
                moDps.getDbmsDpsNotes().add((SDataDpsNotes) row.getData());
            }

            moDps.getDbmsDpsCustomAccEntries().clear();
            for (STableRow row : moPaneGridCustomAcc.getGridRows()) {
                moDps.getDbmsDpsCustomAccEntries().add((SDataDpsCustomAccEntry) row.getData());
            }

            moDps.setDbmsCurrency(((SFormComponentItem) jcbFkCurrencyId.getSelectedItem()).getItem());
            moDps.setDbmsCurrencyKey((String) ((SFormComponentItem) jcbFkCurrencyId.getSelectedItem()).getComplement());

            // Set record user object:

            moDps.getRegistryComplements().clear();
            if (!jckRecordUser.isSelected()) {
                /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro  
                releaseRecordUserLock();    // if lock was gained, release it
                */
                /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                releaseRecordUserRedisLock();
                */
                releaseRecordUserSLock();
                moDps.setDbmsRecordKey(null);
            }
            else {
                moDps.setDbmsRecordKey(moRecordUserKey);
                /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                moDps.getRegistryComplements().add(moRecordUserLock);
                */
                /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                moDps.getRegistryComplements().add(moRecordUserRedisLock);
                */
                moDps.getRegistryComplements().add(moRecordUserSLock);
            }

            // Set information for CFDI:
            
            moDps.setAuxAppPrepayCurCrossPayCurrencyId(0);
            moDps.setAuxAppPrepayCurCrossPayExchangeRate(0.0);

            boolean isCfdEmissionRequired = isCfdEmissionRequired(); // convenience variable

            if (isCfdEmissionRequired) {
                SDataDpsCfd dpsCfd = new SDataDpsCfd();

                //dpsCfd.setPkDpsYearId(...    // set when DPS saved!
                //dpsCfd.setPkDpsDocId(...     // set when DPS saved!
                //dpsCfd.setVersion(...     // set when DPS saved!
                dpsCfd.setCfdiType(mbIsDpsInvoice ? DCfdi33Catalogs.CFD_TP_I : DCfdi33Catalogs.CFD_TP_E);
                dpsCfd.setPaymentWay(moFieldCfdiPaymentWay.getFieldValue().toString());
                dpsCfd.setPaymentMethod(moFieldCfdiPaymentMethod.getFieldValue().toString());
                dpsCfd.setPaymentConditions(!moFieldConditionsPayment.getString().isEmpty() ? moFieldConditionsPayment.getString() : (moFieldFkPaymentTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.TRNS_TP_PAY_CASH ? "CONTADO" : "CRÉDITO " + moFieldDaysOfCredit.getInteger() + " DÍAS"));  // XXX: implement method!
                if (jcbGblPeriodicity.getSelectedIndex() > 0) {
                    dpsCfd.setGlobalPeriodocity(moFieldCfdiGblPeriodicity.getFieldValue().toString());
                    dpsCfd.setGlobalMonths(moFieldCfdiGblMonth.getFieldValue().toString());
                    dpsCfd.setGlobalYear((int) moFieldCfdiGblYear.getFieldValue());
                }
                dpsCfd.setZipIssue(miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsBizPartnerBranch(new int[] { moDps.getFkCompanyBranchId() }).getDbmsBizPartnerBranchAddressOfficial().getZipCode());
                dpsCfd.setConfirmation(moFieldCfdiConfirmation.getFieldValue().toString());
                dpsCfd.setTaxRegimeIssuing(moFieldCfdiTaxRegimeIss.getFieldValue().toString());
                dpsCfd.setTaxRegimeReceptor(moFieldCfdiTaxRegimeRec.getFieldValue().toString());
                dpsCfd.setCfdiUsage(moFieldCfdiCfdiUsage.getFieldValue().toString());

                if (isCfdCfdiRelatedRequired() || (moCfdRelatedDocs != null && !moCfdRelatedDocs.getRowCfdRelatedDocs().isEmpty())) {
                    // Include related CFDI data node:
                    
                    SRowCfdRelatedDocs firstRow = moCfdRelatedDocs.getRowCfdRelatedDocs().get(0);
                    
                    dpsCfd.setRelationType(firstRow.getRelationType());
                    
                    if (firstRow.isFirstDocSet()) {
                        // set lead-related CFDI:
                        
                        dpsCfd.setRelatedUuid(firstRow.getFirstDocUuid());
                        dpsCfd.setFkRelatedDpsYearId_n(firstRow.getFirstDocKey()[0]);
                        dpsCfd.setFkRelatedDpsDocId_n(firstRow.getFirstDocKey()[1]);
                    }
                    
                    dpsCfd.setCfdRelatedDocs(moCfdRelatedDocs);
                    
                    if (dpsCfd.getRelatedUuid().isEmpty() || (dpsCfd.getFkRelatedDpsYearId_n() == 0 && dpsCfd.getFkRelatedDpsDocId_n() == 0)) {
                        // try to set lead-related CFDI from adjustment DPS:
                        
                        for (SDataDpsEntry dpsEntry : moDps.getDbmsDpsEntries()) {
                            for (SDataDpsDpsAdjustment dpsAdjustment : dpsEntry.getDbmsDpsAdjustmentsAsAdjustment()) {
                                SDataDps dpsInvoice = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, dpsAdjustment.getDbmsDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
                                if (dpsInvoice != null) {
                                    String invoiceUuid = dpsInvoice.getDbmsDataCfd() == null || dpsInvoice.getDbmsDataCfd().getUuid().isEmpty() ? "" : dpsInvoice.getDbmsDataCfd().getUuid();

                                    if (dpsCfd.getRelatedUuid().isEmpty() || (dpsCfd.getFkRelatedDpsYearId_n() == 0 && dpsCfd.getFkRelatedDpsDocId_n() == 0)) {
                                        dpsCfd.setRelatedUuid(invoiceUuid);
                                        dpsCfd.setFkRelatedDpsYearId_n(dpsInvoice.getPkYearId());
                                        dpsCfd.setFkRelatedDpsDocId_n(dpsInvoice.getPkDocId());
                                    }

                                    if (!invoiceUuid.isEmpty() && !dpsCfd.getUuidRelacionados().contains(invoiceUuid)) {
                                        try {
                                            dpsCfd.addCfdiRelacionado(invoiceUuid);
                                        }
                                        catch (Exception e) {
                                            SLibUtils.printException(this, e);
                                        }
                                    }

                                    if (!dpsCfd.getRelatedUuid().isEmpty() && dpsCfd.getFkRelatedDpsYearId_n() != 0 && dpsCfd.getFkRelatedDpsDocId_n() != 0) {
                                        break; // lead-related CFDI already set!
                                    }
                                }
                            }
                        }
                    }
                    
                    // Check if there is currency crossing (applyes only for application of prepayments!):
                    
                    if (firstRow.isFirstDocSet()) {
                        SDataDps firstDps = firstRow.getFirstDps(miClient);
                        
                        if (moDps.getFkCurrencyId() != firstDps.getFkCurrencyId()) {
                            moDps.setAuxAppPrepayCurCrossPayCurrencyId(firstDps.getFkCurrencyId()); // preserve currency of source prepayment
                            moDps.setAuxAppPrepayCurCrossPayExchangeRate(firstDps.getExchangeRate()); // preserve exchange rate of source prepayment
                        }
                    }
                }

                if (isCfdIntCommerceRequired() && jckCfdCceApplies.isSelected()) {
                    // include International Commerce data:
                    dpsCfd.setCfdCceMotivoTraslado(jcbCfdCceMoveReason.getSelectedIndex() <= 0 ? "" : moFieldCfdCceMoveReason.getFieldValue().toString());
                    dpsCfd.setCfdCceTipoOperacion(jcbCfdCceOperationType.getSelectedIndex() <= 0 ? "" : moFieldCfdCceOperationType.getFieldValue().toString());
                    dpsCfd.setCfdCceClaveDePedimento(jcbCfdCceRequestKey.getSelectedIndex() <= 0 ? "" : moFieldCfdCceRequestKey.getFieldValue().toString());
                    dpsCfd.setCfdCceCertificadoOrigen("" + moFieldCfdCceCertificateOrigin.getInteger());
                    dpsCfd.setCfdCceNumCertificadoOrigen(moFieldCfdCceNumberCertificateOrigin.getString());
                    dpsCfd.setCfdCceSubdivision("" + moFieldCfdCceSubdivision.getInteger());
                    dpsCfd.setCfdCceTipoCambioUsd(SLibUtils.getDecimalFormatExchangeRate().format(moFieldCfdCceExchangeRateUsd.getDouble()));
                    dpsCfd.setCfdCceTotalUsd(DCfdUtils.AmountFormat.format(moFieldCfdCceTotalUsd.getDouble()));
                    dpsCfd.setExportation(DCfdi40Catalogs.ClaveExportacionDefinitivaA1);
                    if (moBizPartnerBranchAddress.getDbmsDataCountry().getCountryGroup().compareTo(DCfdi40Catalogs.AgrupaciónPaísesUE) == 0) {
                        dpsCfd.setCfdCceNumeroExportadorConfiable(miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsCategorySettingsCo().getNumberExporter());
                    }
                    dpsCfd.setCfdCceIncoterm(jcbFkIncotermId.getSelectedIndex() <= 0 ? "" : ((SFormComponentItem) jcbFkIncotermId.getSelectedItem()).getComplement().toString());

                    moDps.setFkBizPartnerAddresseeId_n(jcbCfdCceFkBizPartnerAddressee.getSelectedIndex() <= 0 ? 0 : moFieldCfdCceBizPartnerAddressee.getKeyAsIntArray()[0]);
                    moDps.setFkAddresseeBizPartnerId_nr(jcbCfdCceFkAddresseeBizPartner.getSelectedIndex() <= 0 ? 0 : moFieldCfdCceAddresseeBizPartner.getKeyAsIntArray()[0]);
                    moDps.setFkAddresseeBizPartnerBranchId_n(jcbCfdCceFkAddresseeBizPartnerBranchAddress.getSelectedIndex() <= 0 ? 0 : moFieldCfdCceAddresseeBizPartnerBranchAddress.getKeyAsIntArray()[0]);
                    moDps.setFkAddresseeBizPartnerBranchAddressId_n(jcbCfdCceFkAddresseeBizPartnerBranchAddress.getSelectedIndex() <= 0 ? 0 : moFieldCfdCceAddresseeBizPartnerBranchAddress.getKeyAsIntArray()[1]);
                    
                }
                else {
                    dpsCfd.setExportation(DCfdi40Catalogs.ClaveExportacionNoAplica);
                }

                moDps.setDbmsDataDpsCfd(dpsCfd);
            }

            // Set params for CFD:

            if (!isCfdEmissionRequired) {
                moDps.setAuxIsNeedCfd(false);
                moDps.setAuxIsNeedCfdCce(false);
                moDps.setApprovalYear(0);
                moDps.setApprovalNumber(0);
                moDps.setAuxCfdParams(null);
            }
            else {
                moDps.setAuxIsNeedCfd(true);
                moDps.setAuxIsNeedCfdCce(jckCfdCceApplies.isSelected());
                moDps.setApprovalYear(mnNumbersApprovalYear);
                moDps.setApprovalNumber(mnNumbersApprovalNumber);
                moDps.setAuxCfdParams(createCfdParams());
            }

            // process added XML file of CFDI:

            if (!msFileXmlJustLoaded.isEmpty()) {
                // an XML has just been provided to be attached to current registry:
                try {
                    String xml = SXmlUtils.readXml(msFileXmlJustLoaded);
                    SDataCfd cfd = moDps.getDbmsDataCfd() != null ? moDps.getDbmsDataCfd() : new SDataCfd();

                    cfd.setCertNumber("");
                    cfd.setStringSigned("");
                    cfd.setSignature("");
                    cfd.setDocXml(xml);
                    cfd.setDocXmlName(moFieldCfdiXmlFile.getString());
                    cfd.setIsConsistent(true);
                    cfd.setFkCfdTypeId(SDataConstantsSys.TRNS_TP_CFD_INV);
                    cfd.setFkXmlTypeId(SDataConstantsSys.TRNS_TP_XML_NA);
                    cfd.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_EMITED);
                    cfd.setFkXmlDeliveryTypeId(SModSysConsts.TRNS_TP_XML_DVY_NA);
                    cfd.setFkXmlDeliveryStatusId(SModSysConsts.TRNS_ST_XML_DVY_PENDING);
                    cfd.setFkUserProcessingId(miClient.getSession().getUser().getPkUserId());
                    cfd.setFkUserDeliveryId(miClient.getSession().getUser().getPkUserId());

                    moDps.setDbmsDataCfd(cfd);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
            else if (moFieldCfdiXmlFile.getString().isEmpty() && moDps.getDbmsDataCfd() != null) {
                // XXX NOTE: 2018-05-24, Sergio Flores: Check if this code is correct. It seems it does not, because when XML file is deleted, it is preserved... why?!
                SDataCfd cfd = moDps.getDbmsDataCfd();

                cfd.setDocXml("");
                cfd.setDocXmlName("");
                cfd.setIsConsistent(true);
                cfd.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW);

                moDps.setDbmsDataCfd(cfd);
            }

            if ((moFilePdfJustLoaded != null) || (moFieldCfdiPdfFile.getString().isEmpty() && moDps.getDbmsDataPdf() != null)) {
                SDataPdf pdf;

                if (moDps.getDbmsDataPdf() != null) {
                    pdf =  moDps.getDbmsDataPdf();
                }
                else {
                    pdf = new SDataPdf();
                    moDps.setDbmsDataPdf(pdf);
                }
                
                if (moFilePdfJustLoaded != null) {
                    pdf.setAuxDocPdfFile(moFilePdfJustLoaded);
                }
                else {
                    pdf.setAuxToBeDeleted(true);
                }

                pdf.setAuxXmlBaseDirectory(((SDataParamsCompany) miClient.getSession().getConfigCompany()).getXmlBaseDirectory());
            }
            
            if (!jtfBillOfLading.getText().isEmpty()) {
                moDps.setFkBillOfLading_n(moBillOfLading.getPkBillOfLadingId());
                moDps.setDbmsDataCfdBol(moBillOfLading == null ? null : moBillOfLading.getDataCfd());
            }
            
            moDps.getDbmsScaleTickets().clear(); 
            for (SDataScaleTicketDps t : mvScaleTicDps) {
                moDps.getDbmsScaleTickets().add(t);
            }
            
            return moDps;
        }
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case SLibConstants.VALUE_TYPE:
                setDpsType((int[]) value);
                break;
            case SLibConstants.VALUE_IS_IMPORTED:
                mbIsImported = (Boolean) value;
                break;
            case SDataConstants.TRN_DPS:
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
            case SLibConstants.VALUE_IS_MAT_REQ:
                mbMatRequestImport = (Boolean) value;
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

            try {
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
                else if (button == jbDateMaturity) {
                    actionDateMaturity();
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
                else if (button == jbEntryCopy) {
                    actionEntryCopy();
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
                else if (button == jbEntryImportFromMatRequest) {
                    actionImportEntryFromMatRequest();
                }
                else if (button == jbEntryViewMatReqLinks) {
                    actionViewMatReqEntryLinks();
                }
                else if (button == jbExportCsv) {
                    actionExportCsv();
                }
                else if (button == jbTaxRegionId) {
                    actionTaxRegionId();
                }
                else if (button == jbEditTaxRegion) {
                    actionEditTaxRegion();
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
                else if (button == jbPickSystemNotes) {
                    actionPickSystemNotes();
                }
                else if (button == jbEditNotes) {
                    actionEditNotes();
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
                else if (button == jbEditLogistics) {
                    actionEditLogistics();
                }
                else if (button == jbCfdCceCalcTotalUsd) {
                    actionCfdCceCalcTotalUsd();
                }
                else if (button == jbLoadFileXml) {
                    actionLoadFileXml();
                }
                else if (button == jbDeleteFileXml) {
                    actionDeleteFileXml();
                }
                else if (button == jbLoadFilePdf) {
                    actionLoadFilePdf();
                }
                else if (button == jbDeleteFilePdf) {
                    actionDeleteFilePdf();
                }
                else if (button == jbCfdiRelatedDocs) {
                    actionCfdiRelatedDocs();
                }
                else if (button == jbLoadBillOfLading) {
                    actionBillOfLading();
                }
                else if (button == jbDeleteBillOfLading) {
                    actionDeleteBillOfLading();
                }
                else if (button == jbSetTime) {
                    actionSetTime();
                }
                else if (button == jbPickAccItem) {
                    actionPickAccItem();
                }
                else if (button == jbPickAccItemRef_n) {
                    actionPickAccItemRef_n();
                }
                else if (button == jbPickAccUnit) {
                    actionPickAccUnit();
                }
                else if (button == jbExeWizardAccSubtotal) {
                    actionExeWizardAccSubtotal();
                }
                else if (button == jbCustomAccEntryNew) {
                    actionCustomAccEntryNew();
                }
                else if (button == jbCustomAccEntryCopy) {
                    actionCustomAccEntryCopy();
                }
                else if (button == jbCustomAccEntryEdit) {
                    actionCustomAccEntryEdit();
                }
                else if (button == jbCustomAccEntryDelete) {
                    actionCustomAccEntryDelete();
                }
                else if (button == jbCustomAccEntryOk) {
                    actionCustomAccEntryOk();
                }
                else if (button == jbCustomAccEntryCancel) {
                    actionCustomAccEntryCancel(false, true);
                }
            }
            catch (SQLException se) {
                SLibUtilities.renderException(this, se);
            }
            catch (Exception ex) {
                Logger.getLogger(SFormDps.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (e.getSource() instanceof javax.swing.JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) e.getSource();

            if (toggleButton == jtbEntryFilter) {
                actionEntryFilter();
            }
            else if (toggleButton == jtbNotesFilter) {
                actionNotesFilter();
            }
            else if (toggleButton == jtbSwitchCustomAcc) {
                actionSwitchCustomAcc();
            }
            else if (toggleButton == jtbLinkTicket) {
                actionLinkTicket();
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
            else if (textField == jcbAddAmc71SupplierGln.getEditor().getEditorComponent()) {
                focusLostAddAmc71SupplierGln();
            }
            else if (textField == jcbAddAmc71CompanyGln.getEditor().getEditorComponent()) {
                focusLostAddAmc71CompanyGln();
            }
            else if (textField == jcbAddAmc71CompanyBranchGln.getEditor().getEditorComponent()) {
                focusLostAddAmc71CompanyBranchGln();
            }
            else if (textField == jtfAccSubtotalPct) {
                focusLostAccSubtotalPct();
            }
            else if (textField == jtfAccSubtotalCy) {
                focusLostAccSubtotalCy();
            }
        }
    }

    @Override
    public void itemStateChanged(java.awt.event.ItemEvent e) {
        if (!mbResetingForm && !mbUpdatingForm) {
            if (e.getSource() instanceof javax.swing.JCheckBox) {
                JCheckBox checkBox = (JCheckBox) e.getSource();

                if (checkBox == jckDateDoc) {
                    itemStateChangedDateDoc();
                }
                else if (checkBox == jckDateStartCredit) {
                    itemStateChangedDateStartCredit();
                }
                else if (checkBox == jckRecordUser) {
                    itemStateChangedRecordUser();
                }
                else if (checkBox == jckShipments) {
                    itemStateChangedShipments();
                }
                else if (checkBox == jckIsDiscountDocApplying) {
                    itemStateChangedIsDiscountDocApplying(true);
                }
                else if (checkBox == jckIsDiscountDocPercentage) {
                    itemStateChangedIsDiscountDocPercentage(true);
                }
                else if (checkBox == jckAccIsSubtotalPctApplying) {
                    itemStateChangedAccIsSubtotalPctApplying(true);
                }
            }
            else if (e.getSource() instanceof javax.swing.JComboBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JComboBox comboBox = (JComboBox) e.getSource();

                    if (comboBox == jcbNumberSeries) {
                        itemStateChangedNumberSeries();
                    }
                    else if (comboBox == jcbFkPaymentTypeId) {
                        itemStateChangedFkPaymentTypeId(true);
                    }
                    else if (comboBox == jcbFkCurrencyId) {
                        itemStateChangedFkCurrencyId(true);
                    }
                    else if (comboBox == jcbFkIncotermId) {
                        itemStateChangedFkIncotermId();
                    }
                    else if (comboBox == jcbFkCarrierTypeId) {
                        itemStateChangedFkCarrierTypeId();
                    }
                    else if (comboBox == jcbFkVehicleTypeId_n) {
                        itemStateChangedFkVehicleTypeId_n();
                    }
                    else if (comboBox == jcbAddCfdAddendaSubtype) {
                        itemStateChangedAddCfdAddendaSubtype();
                    }
                    else if (comboBox == jcbAddAmc71SupplierGln) {
                        itemStateChangedAddAmc71SupplierGln();
                    }
                    else if (comboBox == jcbAddAmc71CompanyGln) {
                        itemStateChangedAddAmc71CompanyGln();
                    }
                    else if (comboBox == jcbAddAmc71CompanyBranchGln) {
                        itemStateChangedAddAmc71CompanyBranchGln();
                    }
                    else if (comboBox == jcbAccItem) {
                        itemStateChangedAccItem(true);
                    }
                }
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JTabbedPane) {
            if (e.getSource() == jTabbedPane) {
                stateChangedTabbedPane();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!mbResetingForm && !mbUpdatingForm) {
            if (!e.getValueIsAdjusting()) {
                if (e.getSource() == moPaneGridCustomAcc.getTable().getSelectionModel()) {
                    valueChangedCustomAcc();
                }
            }
        }
    }

    @Override
    public SLibMethod getPostSaveMethod(SDataRegistry registry) {
        SLibMethod method = null;
        SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, registry.getPrimaryKey(), SLibConstants.EXEC_MODE_STEALTH);

        boolean isMailReq = false;
        int typeMms = SLibConstants.UNDEFINED;

        if (SLibUtilities.compareKeys(SDataConstantsSys.TRNU_TP_DPS_SAL_CON, dps.getDpsTypeKey())) {
            isMailReq = true;
            typeMms = SModSysConsts.CFGS_TP_MMS_CON_SAL;
        }
        else if (SLibUtilities.compareKeys(SDataConstantsSys.TRNU_TP_DPS_SAL_ORD, dps.getDpsTypeKey())) {
            isMailReq = true;
            typeMms = SModSysConsts.CFGS_TP_MMS_ORD_SAL;
        }

        if (isMailReq) {
            try {
                method = new SLibMethod(dps, dps.getClass().getMethod("sendMail", new Class[] { SClientInterface.class, Object.class, int.class }), new Object[] { miClient, dps.getPrimaryKey(), typeMms });
            }
            catch (NoSuchMethodException | SecurityException e) {
                SLibUtilities.printOutException(this, e);
            }
        }
        
        try {
            boolean reset = false;
            SAuthorizationUtils.processAuthorizations(miClient.getSession(), SAuthorizationUtils.AUTH_TYPE_DPS, registry.getPrimaryKey(), reset);
        }
        catch (Exception ex) {
            Logger.getLogger(SFormDps.class.getName()).log(Level.SEVERE, null, ex);
        }

        return method;
    }
}
