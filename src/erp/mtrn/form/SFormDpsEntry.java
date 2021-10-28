/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.form.SFormOptionPicker;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.lib.table.STablePaneGrid;
import erp.lib.table.STableRow;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mcfg.data.SDataParamsCompany;
import erp.mfin.form.SPanelAccount;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataItemBizPartnerDescription;
import erp.mitm.data.SDataItemForeignLanguage;
import erp.mitm.data.SDataUnitType;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.itm.db.SItmConsts;
import erp.mod.trn.db.STrnConsts;
import erp.mod.trn.db.STrnUtils;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsDpsAdjustment;
import erp.mtrn.data.SDataDpsDpsLink;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SDataDpsEntryCommissions;
import erp.mtrn.data.SDataDpsEntryCommissionsRow;
import erp.mtrn.data.SDataDpsEntryComplement;
import erp.mtrn.data.SDataDpsEntryNotes;
import erp.mtrn.data.SDataDpsEntryNotesRow;
import erp.mtrn.data.SDataDpsEntryPrice;
import erp.mtrn.data.SDataDpsEntryPriceRow;
import erp.mtrn.data.SDataDpsEntryTax;
import erp.mtrn.data.SDataDpsEntryTaxRow;
import erp.mtrn.data.STrnUtilities;
import erp.mtrn.data.cfd.SAddendaAmc71XmlLine;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author  Sergio Flores, Juan Barajas, Irving Sánchez, Gerardo Hernández, Uriel Castañeda, Sergio Flores, Claudio Peña
 */
public class SFormDpsEntry extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.FocusListener, java.awt.event.ItemListener, javax.swing.event.CellEditorListener {
    
    public static final int CONCEPT_LENGTH_MAX = 130;
    
    private static final int TAB_TAX = 0;
    private static final int TAB_SAL_CMS = 1; // sales comissions
    private static final int TAB_PRC = 2;
    private static final int TAB_MKT = 3;
    private static final int TAB_NOT = 4;
    private static final int TAB_CFD_ADD = 5; // CFD addenda
    private static final int TAB_CFD_COMPL = 6; // CFD complement
    
    private static final int CFD_COMPL_VALS = 4; //number of CFD complement values
    
    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private boolean mbUpdatingForm;
    private java.util.Vector<SFormField> mvFields;
    private erp.client.SClientInterface miClient;
    
    private erp.mtrn.data.SDataDpsEntry moDpsEntry;
    private erp.mitm.data.SDataItem moItem;
    private erp.lib.form.SFormField moFieldFkItemId;
    private erp.lib.form.SFormField moFieldConceptKey;
    private erp.lib.form.SFormField moFieldConcept;
    private erp.lib.form.SFormField moFieldFkOriginalUnitId;
    private erp.lib.form.SFormField moFieldPartNum;
    private erp.lib.form.SFormField moFieldIsDiscountUnitaryPercentage;
    private erp.lib.form.SFormField moFieldDiscountUnitaryPercentage;
    private erp.lib.form.SFormField moFieldIsDiscountEntryPercentage;
    private erp.lib.form.SFormField moFieldDiscountEntryPercentage;
    private erp.lib.form.SFormField moFieldIsDiscountDocApplying;
    private erp.lib.form.SFormField moFieldOriginalQuantity;
    private erp.lib.form.SFormField moFieldOriginalPriceUnitaryCy;
    private erp.lib.form.SFormField moFieldOriginalDiscountUnitaryCy;
    private erp.lib.form.SFormField moFieldSalesPriceUnitaryCy;
    private erp.lib.form.SFormField moFieldSalesFreightUnitaryCy;
    private erp.lib.form.SFormField moFieldIsSalesFreightRequired;
    private erp.lib.form.SFormField moFieldIsSalesFreightAddPrice;
    private erp.lib.form.SFormField moFieldIsSalesFreightConfirm;
    private erp.lib.form.SFormField moFieldDiscountEntryCy;
    private erp.lib.form.SFormField moFieldDiscountDocCy;
    private erp.lib.form.SFormField moFieldSurplusPercentage;
    private erp.lib.form.SFormField moFieldIsDpsPriceVariable;
    
    private erp.lib.form.SFormField moFieldDpsContractBase;
    private erp.lib.form.SFormField moFieldDpsContractFuture;
    private erp.lib.form.SFormField moFieldDpsContractFactor;
    
    private erp.lib.form.SFormField moFieldContractPriceReferenceNumbrer;
    private erp.lib.form.SFormField moFieldContractPriceYear;
    private erp.lib.form.SFormField moFieldContractPriceMonth;
    private erp.lib.form.SFormField moFieldPriceOriginalQuantity;
    private erp.lib.form.SFormField moFieldPriceOriginalPriceUnitaryCy;
    private erp.lib.form.SFormField moFieldContractBase;
    private erp.lib.form.SFormField moFieldContractFuture;
    private erp.lib.form.SFormField moFieldContractFactor;
    
    private erp.lib.form.SFormField moFieldFkVehicleTypeId_n;
    private erp.lib.form.SFormField moFieldDriver;
    private erp.lib.form.SFormField moFieldPlate;
    private erp.lib.form.SFormField moFieldContainerTank;
    private erp.lib.form.SFormField moFieldSealQuality;
    private erp.lib.form.SFormField moFieldSealSecurity;
    private erp.lib.form.SFormField moFieldTicket;
    private erp.lib.form.SFormField moFieldVgm;
    private erp.lib.form.SFormField moFieldFkBankAccountId_n;
    private erp.lib.form.SFormField moFieldLength;
    private erp.lib.form.SFormField moFieldSurface;
    private erp.lib.form.SFormField moFieldVolume;
    private erp.lib.form.SFormField moFieldMass;
    private erp.lib.form.SFormField moFieldWeightPackagingExtra;
    private erp.lib.form.SFormField moFieldWeightGross;
    private erp.lib.form.SFormField moFieldWeightDelivery;
    private erp.lib.form.SFormField moFieldIsPrepayment;
    private erp.lib.form.SFormField moFieldIsDiscountRetailChain;
    private erp.lib.form.SFormField moFieldIsTaxesAutomaticApplying;
    private erp.lib.form.SFormField moFieldIsInventoriable;
    private erp.lib.form.SFormField moFieldIsDeleted;
    private erp.lib.form.SFormField moFieldFkTaxRegionId;
    private erp.lib.form.SFormField moFieldFkThirdTaxCausingId_n;
    private erp.lib.form.SFormField moFieldFkItemReferenceId_n;
    private erp.lib.form.SFormField moFieldReference;
    private erp.lib.form.SFormField moFieldOperationsType;
    private erp.lib.form.SFormField moFieldAddBachocoNúmeroPosición;
    private erp.lib.form.SFormField moFieldAddBachocoCentro;
    private erp.lib.form.SFormField moFieldAddLorealEntryNumber;
    private erp.lib.form.SFormField moFieldAddElektraOrder;
    private erp.lib.form.SFormField moFieldAddGenBarcode;
    private erp.lib.form.SFormField moFieldAddElektraCages;
    private erp.lib.form.SFormField moFieldAddElektraCagePriceUnitary;
    private erp.lib.form.SFormField moFieldAddElektraParts;
    private erp.lib.form.SFormField moFieldAddElektraPartPriceUnitary;
    private erp.lib.form.SFormField moFieldAddAmc71PurchaseOrder;
    private erp.lib.form.SFormField moFieldAddAmc71PurchaseOrderDate;
    private erp.lib.form.SFormField moFieldComplConceptKey;
    private erp.lib.form.SFormField moFieldComplConcept;
    private erp.lib.form.SFormField moFieldComplCfdProdServ;
    private erp.lib.form.SFormField moFieldComplCfdUnit;
    
    private int mnAuxCurrentUnitTypeId;
    private int mnAuxCurrentUnitAlternativeTypeId;
    private double mdTempCurrentValue;
    private Object moTempCurrentUnitKey;
    private erp.lib.table.STablePane moPaneTaxes;
    private erp.lib.table.STablePane moPaneCommissions;
    private erp.lib.table.STablePaneGrid moPaneGridNotes;
    private erp.lib.table.STablePaneGrid moPaneGridPrices;
    private erp.mtrn.form.SDialogPriceUnitaryWizard moDialogPriceUnitaryWizard;
    private erp.mtrn.form.SDialogItemPriceHistory moDialogItemPriceHistory;
    private erp.mtrn.form.SFormDpsEntryNotes moFormNotes;
    private erp.mfin.form.SPanelAccount moPanelFkCostCenterId_n;

    private erp.mtrn.data.SDataDps moParamDps;
    private erp.mbps.data.SDataBizPartner moParamBizPartner;
    private erp.mbps.data.SDataBizPartnerBranch moParamBizPartnerBranch;
    private erp.mitm.data.SDataItemBizPartnerDescription moDataItemBizPartnerDescription;
    private int mnParamAdjustmentTypeId;

    private int[] manItemClassFilterKey;

    private final int COL_TAX_CUR = 5;   // column index for amount in taxes pane

    private boolean mbEnableDataAddenda;
    private boolean mbRightMktPriceListPurchases;
    private boolean mbRightMktPriceListSales;
    private boolean mbRightPurPriceChange;
    private boolean mbRightSalPriceChange;
    private boolean mbRightTrnOmitSourceDoc;
    private boolean mbAllowDiscount;
    private double mdQuantitySrcOrig;
    private double mdQuantityDesOrig;
    private double mdQuantityPrc;
    private boolean mbIsLastPrc;
    private erp.mmkt.data.SParamsItemPriceList moParamsItemPriceList;
    
    private int mnAuxEntryPriceAction;
    private int mnAuxEntryPriceEditedIndex;
    private SDataDpsEntryPrice moAuxEntryPriceEdited;
    
    private boolean mbPostEmissionEdition;
    private erp.mtrn.form.SFormDpsComEntry moFormComEntry;

    /** Creates new form DFormDpsEntry
     * @param client GUI client.
     */
    public SFormDpsEntry(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.TRN_DPS_ETY;

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

        bgAccOptions = new javax.swing.ButtonGroup();
        jpRegistry = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlFkItemId = new javax.swing.JLabel();
        jcbFkItemId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkItemId = new javax.swing.JButton();
        jbSetPrepayment = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jlConceptKey = new javax.swing.JLabel();
        jtConceptfKey = new javax.swing.JTextField();
        jbConceptKey = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jlConcept = new javax.swing.JLabel();
        jtfConcept = new javax.swing.JTextField();
        jbConcept = new javax.swing.JButton();
        jbItemBizPartnerDescription = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jlFkOriginalUnitId = new javax.swing.JLabel();
        jcbFkOriginalUnitId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkOriginalUnitId = new javax.swing.JButton();
        jlPartNum = new javax.swing.JLabel();
        jtPartNum = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jckIsDiscountUnitaryPercentage = new javax.swing.JCheckBox();
        jtfDiscountUnitaryPercentage = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jckIsBulk = new javax.swing.JCheckBox();
        jckIsInventoriable = new javax.swing.JCheckBox();
        jckIsPrepayment = new javax.swing.JCheckBox();
        jPanel16 = new javax.swing.JPanel();
        jckIsDiscountEntryPercentage = new javax.swing.JCheckBox();
        jtfDiscountEntryPercentage = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jckIsDiscountRetailChain = new javax.swing.JCheckBox();
        jPanel17 = new javax.swing.JPanel();
        jckIsDiscountDocApplying = new javax.swing.JCheckBox();
        jtfDiscountDocPercentageRo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jckIsDeleted = new javax.swing.JCheckBox();
        jPanel15 = new javax.swing.JPanel();
        jlOriginalQuantity = new javax.swing.JLabel();
        jtfOriginalQuantity = new javax.swing.JTextField();
        jtfOriginalUnitSymbolRo = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        jlQuantity = new javax.swing.JLabel();
        jtfQuantityRo = new javax.swing.JTextField();
        jtfUnitSymbolRo = new javax.swing.JTextField();
        jckAuxPreserveQuantity = new javax.swing.JCheckBox();
        jckIsSalesFreightRequired = new javax.swing.JCheckBox();
        jPanel20 = new javax.swing.JPanel();
        jlOriginalPriceUnitaryCy = new javax.swing.JLabel();
        jtfOriginalPriceUnitaryCy = new javax.swing.JTextField();
        jtfOriginalPriceUnitaryCyCurrencyKeyRo = new javax.swing.JTextField();
        jbOriginalPriceUnitaryCyWizard = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jlSalesPriceUnitaryCy = new javax.swing.JLabel();
        jtfSalesPriceUnitaryCy = new javax.swing.JTextField();
        jtfSalesPriceUnitaryCyCurrencyKeyRo = new javax.swing.JTextField();
        jckIsSalesFreightAddPrice = new javax.swing.JCheckBox();
        jPanel21 = new javax.swing.JPanel();
        jlOriginalDiscountUnitaryCy = new javax.swing.JLabel();
        jtfOriginalDiscountUnitaryCy = new javax.swing.JTextField();
        jtfOriginalDiscountUnitaryCyCurrencyKeyRo = new javax.swing.JTextField();
        jbPriceHistory = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jlSalesFreightUnitaryCy = new javax.swing.JLabel();
        jtfSalesFreightUnitaryCy = new javax.swing.JTextField();
        jtfSalesFreightUnitaryCyCurrencyKeyRo = new javax.swing.JTextField();
        jckIsSalesFreightConfirm = new javax.swing.JCheckBox();
        jPanel24 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jlCurrency = new javax.swing.JLabel();
        jtfCurrencySystemKeyRo = new javax.swing.JTextField();
        jtfCurrencyKeyRo = new javax.swing.JTextField();
        jlPriceUnitary = new javax.swing.JLabel();
        jtfPriceUnitaryRo = new javax.swing.JTextField();
        jtfPriceUnitaryCyRo = new javax.swing.JTextField();
        jlDiscountUnitary = new javax.swing.JLabel();
        jtfDiscountUnitaryRo = new javax.swing.JTextField();
        jtfDiscountUnitaryCyRo = new javax.swing.JTextField();
        jlDiscountEntry = new javax.swing.JLabel();
        jtfDiscountEntryRo = new javax.swing.JTextField();
        jtfDiscountEntryCy = new javax.swing.JTextField();
        jlSubtotalProvisional_r = new javax.swing.JLabel();
        jtfSubtotalProvisional_rRo = new javax.swing.JTextField();
        jtfSubtotalProvisionalCy_rRo = new javax.swing.JTextField();
        jlDiscountDoc = new javax.swing.JLabel();
        jtfDiscountDocRo = new javax.swing.JTextField();
        jtfDiscountDocCy = new javax.swing.JTextField();
        jlSubtotal_r = new javax.swing.JLabel();
        jtfSubtotal_rRo = new javax.swing.JTextField();
        jtfSubtotalCy_rRo = new javax.swing.JTextField();
        jlTaxCharged_r = new javax.swing.JLabel();
        jtfTaxCharged_rRo = new javax.swing.JTextField();
        jtfTaxChargedCy_rRo = new javax.swing.JTextField();
        jlTaxRetained_r = new javax.swing.JLabel();
        jtfTaxRetained_rRo = new javax.swing.JTextField();
        jtfTaxRetainedCy_rRo = new javax.swing.JTextField();
        jlTotal_r = new javax.swing.JLabel();
        jtfTotal_rRo = new javax.swing.JTextField();
        jtfTotalCy_rRo = new javax.swing.JTextField();
        jlPriceUnitaryReal_r = new javax.swing.JLabel();
        jtfPriceUnitaryReal_rRo = new javax.swing.JTextField();
        jtfPriceUnitaryRealCy_rRo = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jlFkItemReferenceId_n = new javax.swing.JLabel();
        jcbFkItemReferenceId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkItemReferenceId_n = new javax.swing.JButton();
        jPanel40 = new javax.swing.JPanel();
        jlReference = new javax.swing.JLabel();
        jtfReference = new javax.swing.JTextField();
        jlOperationsType = new javax.swing.JLabel();
        jcbOperationsType = new javax.swing.JComboBox();
        jpCostCenter = new javax.swing.JPanel();
        jlDummyCostCenter = new javax.swing.JLabel();
        jTabbedPane = new javax.swing.JTabbedPane();
        jpTaxes = new javax.swing.JPanel();
        jpTaxInfo = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jlTaxIdentityEmisor = new javax.swing.JLabel();
        jtfTaxIdentityEmisorRo = new javax.swing.JTextField();
        jlTaxIdentityReceptor = new javax.swing.JLabel();
        jtfTaxIdentityReceptorRo = new javax.swing.JTextField();
        jPanel33 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jlFkTaxRegionId = new javax.swing.JLabel();
        jcbFkTaxRegionId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkTaxRegionId = new javax.swing.JButton();
        jckIsTaxesAutomaticApplying = new javax.swing.JCheckBox();
        jPanel34 = new javax.swing.JPanel();
        jlFkThirdTaxCausingId_n = new javax.swing.JLabel();
        jcbFkThirdTaxCausingId_n = new javax.swing.JComboBox();
        jbFkThirdTaxCausingId_n = new javax.swing.JButton();
        jbEditFkThirdTaxCausingId_n = new javax.swing.JButton();
        jpCommissions = new javax.swing.JPanel();
        jpPricesData = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jckIsDpsReqMonthDelivery = new javax.swing.JCheckBox();
        jlDpsContractBase = new javax.swing.JLabel();
        jtfDpsContractBase = new javax.swing.JTextField();
        jlDpsContractFuture = new javax.swing.JLabel();
        jtfDpsContractFuture = new javax.swing.JTextField();
        jlDpsContractFactor = new javax.swing.JLabel();
        jtfDpsContractFactor = new javax.swing.JTextField();
        jckIsPriceConfirm = new javax.swing.JCheckBox();
        jPanel51 = new javax.swing.JPanel();
        jPanel59 = new javax.swing.JPanel();
        jPanel67 = new javax.swing.JPanel();
        jlIsPriceVariable = new javax.swing.JLabel();
        jckIsDirectPrice = new javax.swing.JCheckBox();
        jPanel39 = new javax.swing.JPanel();
        jlContractPriceNumbrerReference = new javax.swing.JLabel();
        jtfContractPriceNumbrerReference = new javax.swing.JTextField();
        jPanel63 = new javax.swing.JPanel();
        jlContractPriceYear = new javax.swing.JLabel();
        jtfContractPriceYear = new javax.swing.JTextField();
        jPanel65 = new javax.swing.JPanel();
        jlContractPriceMonth = new javax.swing.JLabel();
        jtfContractPriceMonth = new javax.swing.JTextField();
        jPanel72 = new javax.swing.JPanel();
        jlPriceOriginalQuantity = new javax.swing.JLabel();
        jtfPriceOriginalQuantity = new javax.swing.JTextField();
        jtfPriceOriginalUnitSymbol = new javax.swing.JTextField();
        jPanel77 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jckChangePrice = new javax.swing.JCheckBox();
        jPanel73 = new javax.swing.JPanel();
        jlPriceOriginalPriceUnitaryCy = new javax.swing.JLabel();
        jtfPriceOriginalPriceUnitaryCy = new javax.swing.JTextField();
        jtfPriceCurrencyKeyPriceUnitaryCy = new javax.swing.JTextField();
        jPanel74 = new javax.swing.JPanel();
        jlContractBase = new javax.swing.JLabel();
        jtfContractBase = new javax.swing.JTextField();
        jPanel75 = new javax.swing.JPanel();
        jlContractFuture = new javax.swing.JLabel();
        jtfContractFuture = new javax.swing.JTextField();
        jPanel76 = new javax.swing.JPanel();
        jlContractFactor = new javax.swing.JLabel();
        jtfContractFactor = new javax.swing.JTextField();
        jPanel78 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jbPriceNew = new javax.swing.JButton();
        jPanel80 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jbPriceSave = new javax.swing.JButton();
        jPanel79 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jbPriceClear = new javax.swing.JButton();
        jpPrices = new javax.swing.JPanel();
        jpNotesControls1 = new javax.swing.JPanel();
        jbGridPriceNew = new javax.swing.JButton();
        jbGridPriceEdit = new javax.swing.JButton();
        jbGridPriceDelete = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jtbGridPricesFilter = new javax.swing.JToggleButton();
        jpMarketing = new javax.swing.JPanel();
        jpDataShip = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jlFkVehicleTypeId_n = new javax.swing.JLabel();
        jcbFkVehicleTypeId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel43 = new javax.swing.JPanel();
        jlDriver = new javax.swing.JLabel();
        jtfDriver = new javax.swing.JTextField();
        jPanel45 = new javax.swing.JPanel();
        jlVehicleNumber = new javax.swing.JLabel();
        jtfVehicleNumber = new javax.swing.JTextField();
        jPanel47 = new javax.swing.JPanel();
        jlContainerTank = new javax.swing.JLabel();
        jtfContTank = new javax.swing.JTextField();
        jlAddingMultipleMailHelp = new javax.swing.JLabel();
        jPanel55 = new javax.swing.JPanel();
        jlQualitySeal = new javax.swing.JLabel();
        jtfSealQuality = new javax.swing.JTextField();
        jlAddingMultipleMailHelp1 = new javax.swing.JLabel();
        jPanel57 = new javax.swing.JPanel();
        jlSecuritySeal = new javax.swing.JLabel();
        jtfSecuritySeal = new javax.swing.JTextField();
        jlAddingMultipleMailHelp2 = new javax.swing.JLabel();
        jPanel56 = new javax.swing.JPanel();
        jlTicket = new javax.swing.JLabel();
        jtfTicket = new javax.swing.JTextField();
        jPanel58 = new javax.swing.JPanel();
        jlVgm = new javax.swing.JLabel();
        jtfVgm = new javax.swing.JTextField();
        jbEditLogistics = new javax.swing.JButton();
        jpExtraDataUnits = new javax.swing.JPanel();
        jpExtraDataUnitsNorth = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jlLength = new javax.swing.JLabel();
        jtfLength = new javax.swing.JTextField();
        jtfLengthUnitSymbolRo = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jlSurface = new javax.swing.JLabel();
        jtfSurface = new javax.swing.JTextField();
        jtfSurfaceUnitSymbolRo = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jlVolume = new javax.swing.JLabel();
        jtfVolume = new javax.swing.JTextField();
        jtfVolumeUnitSymbolRo = new javax.swing.JTextField();
        jPanel30 = new javax.swing.JPanel();
        jlMass = new javax.swing.JLabel();
        jtfMass = new javax.swing.JTextField();
        jtfMassUnitSymbolRo = new javax.swing.JTextField();
        jPanel66 = new javax.swing.JPanel();
        jlWeightPackagingExtra = new javax.swing.JLabel();
        jtfWeightPackagingExtra = new javax.swing.JTextField();
        jtfWeightPackagingExtraUnitSymbolRo = new javax.swing.JTextField();
        jPanel31 = new javax.swing.JPanel();
        jlWeightGross = new javax.swing.JLabel();
        jtfWeightGrossRo = new javax.swing.JTextField();
        jtfWeightGrossUnitSymbolRo = new javax.swing.JTextField();
        jPanel32 = new javax.swing.JPanel();
        jlWeightDelivery = new javax.swing.JLabel();
        jtfWeightDeliveryRo = new javax.swing.JTextField();
        jtfWeightDeliveryUnitSymbolRo = new javax.swing.JTextField();
        jpExtraDataOther = new javax.swing.JPanel();
        jpExtraDataOtherNorth = new javax.swing.JPanel();
        jpExtraDataOtherFillment = new javax.swing.JPanel();
        jckIsSurplusPercentageApplying = new javax.swing.JCheckBox();
        jtfSurplusPercentage = new javax.swing.JTextField();
        jPanel25 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel60 = new javax.swing.JPanel();
        jPanel61 = new javax.swing.JPanel();
        jlAccOptions = new javax.swing.JLabel();
        jradAccCashAccount = new javax.swing.JRadioButton();
        jradAccAdvanceBilled = new javax.swing.JRadioButton();
        jPanel62 = new javax.swing.JPanel();
        jlFkCashAccountId_n = new javax.swing.JLabel();
        jcbFkCashAccountId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel64 = new javax.swing.JPanel();
        jlFkCashAccountId_n1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jpNotes = new javax.swing.JPanel();
        jpNotesControls = new javax.swing.JPanel();
        jbNotesNew = new javax.swing.JButton();
        jbNotesEdit = new javax.swing.JButton();
        jbNotesDelete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jtbNotesFilter = new javax.swing.JToggleButton();
        jbPickSystemNotes = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jbEditNotes = new javax.swing.JButton();
        jpCfdAddenda = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jPanel94 = new javax.swing.JPanel();
        jlAddCfdAddendaType = new javax.swing.JLabel();
        jtfAddCfdAddendaType = new javax.swing.JTextField();
        jPanel68 = new javax.swing.JPanel();
        jlAddBachocoNúmeroPosición = new javax.swing.JLabel();
        jtfAddBachocoNúmeroPosición = new javax.swing.JTextField();
        jPanel69 = new javax.swing.JPanel();
        jlAddBachocoCentro = new javax.swing.JLabel();
        jtfAddBachocoCentro = new javax.swing.JTextField();
        jPanel36 = new javax.swing.JPanel();
        jlAddLorealEntryNumber = new javax.swing.JLabel();
        jtfAddLorealEntryNumber = new javax.swing.JTextField();
        jPanel44 = new javax.swing.JPanel();
        jlAddElektraOrder = new javax.swing.JLabel();
        jtfAddElektraOrder = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jlAddGenBarcode = new javax.swing.JLabel();
        jcbAddGenBarcode = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel48 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jPanel70 = new javax.swing.JPanel();
        jlAddElektraCages = new javax.swing.JLabel();
        jtfAddElektraCages = new javax.swing.JTextField();
        jPanel71 = new javax.swing.JPanel();
        jlAddElektraCagePriceUnitary = new javax.swing.JLabel();
        jtfAddElektraCagePriceUnitary = new javax.swing.JTextField();
        jPanel50 = new javax.swing.JPanel();
        jlAddElektraParts = new javax.swing.JLabel();
        jtfAddElektraParts = new javax.swing.JTextField();
        jPanel52 = new javax.swing.JPanel();
        jlAddElektraPartPriceUnitary = new javax.swing.JLabel();
        jtfAddElektraPartPriceUnitary = new javax.swing.JTextField();
        jPanel53 = new javax.swing.JPanel();
        jPanel54 = new javax.swing.JPanel();
        jPanel87 = new javax.swing.JPanel();
        jlAddAmc71PurchaseOrder = new javax.swing.JLabel();
        jtfAddAmc71PurchaseOrder = new javax.swing.JTextField();
        jPanel88 = new javax.swing.JPanel();
        jlAddAmc71PurchaseOrderDate = new javax.swing.JLabel();
        jftfAddAmc71PurchaseOrderDate = new javax.swing.JFormattedTextField();
        jlAddAmc71PurchaseOrderDateHint = new javax.swing.JLabel();
        jpCfdComplement = new javax.swing.JPanel();
        jPanel81 = new javax.swing.JPanel();
        jPanel86 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel82 = new javax.swing.JPanel();
        jlComplConceptKey = new javax.swing.JLabel();
        jtfComplConceptKey = new javax.swing.JTextField();
        jPanel83 = new javax.swing.JPanel();
        jlComplConcept = new javax.swing.JLabel();
        jtfComplConcept = new javax.swing.JTextField();
        jPanel84 = new javax.swing.JPanel();
        jlComplCfdProdServ = new javax.swing.JLabel();
        jtfComplCfdProdServ = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel85 = new javax.swing.JPanel();
        jlComplCfdUnit = new javax.swing.JLabel();
        jtfComplCfdUnit = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jpControls = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Partida de documento"); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpRegistry.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la partida:"));
        jPanel22.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridLayout(11, 1, 0, 1));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkItemId.setText("Ítem partida: *");
        jlFkItemId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlFkItemId);

        jcbFkItemId.setMaximumRowCount(16);
        jcbFkItemId.setPreferredSize(new java.awt.Dimension(450, 23));
        jPanel6.add(jcbFkItemId);

        jbFkItemId.setText("...");
        jbFkItemId.setToolTipText("Seleccionar ítem");
        jbFkItemId.setFocusable(false);
        jbFkItemId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbFkItemId);

        jbSetPrepayment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_money.gif"))); // NOI18N
        jbSetPrepayment.setToolTipText("Seleccionar anticipo");
        jbSetPrepayment.setFocusable(false);
        jbSetPrepayment.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbSetPrepayment);

        jPanel4.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlConceptKey.setText("Clave: *");
        jlConceptKey.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlConceptKey);

        jtConceptfKey.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel7.add(jtConceptfKey);

        jbConceptKey.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbConceptKey.setToolTipText("Copiar clave del ítem");
        jbConceptKey.setFocusable(false);
        jbConceptKey.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jbConceptKey);

        jPanel4.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlConcept.setText("Concepto: *");
        jlConcept.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlConcept);

        jtfConcept.setPreferredSize(new java.awt.Dimension(450, 23));
        jPanel8.add(jtfConcept);

        jbConcept.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbConcept.setToolTipText("Copiar concepto del ítem");
        jbConcept.setFocusable(false);
        jbConcept.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel8.add(jbConcept);

        jbItemBizPartnerDescription.setText("...");
        jbItemBizPartnerDescription.setToolTipText("Seleccionar descripción propia del asociado de negocios");
        jbItemBizPartnerDescription.setFocusable(false);
        jbItemBizPartnerDescription.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel8.add(jbItemBizPartnerDescription);

        jPanel4.add(jPanel8);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkOriginalUnitId.setText("Unidad partida: *");
        jlFkOriginalUnitId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlFkOriginalUnitId);

        jcbFkOriginalUnitId.setMaximumRowCount(12);
        jcbFkOriginalUnitId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel10.add(jcbFkOriginalUnitId);

        jbFkOriginalUnitId.setText("...");
        jbFkOriginalUnitId.setToolTipText("Seleccionar unidad");
        jbFkOriginalUnitId.setFocusable(false);
        jbFkOriginalUnitId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel10.add(jbFkOriginalUnitId);

        jlPartNum.setText("No. parte:");
        jlPartNum.setPreferredSize(new java.awt.Dimension(67, 23));
        jPanel10.add(jlPartNum);

        jtPartNum.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel10.add(jtPartNum);

        jPanel4.add(jPanel10);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsDiscountUnitaryPercentage.setText("Descto. unitario en pct.:");
        jckIsDiscountUnitaryPercentage.setMargin(new java.awt.Insets(2, 0, 2, 2));
        jckIsDiscountUnitaryPercentage.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel19.add(jckIsDiscountUnitaryPercentage);

        jtfDiscountUnitaryPercentage.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDiscountUnitaryPercentage.setText("0.00%");
        jtfDiscountUnitaryPercentage.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel19.add(jtfDiscountUnitaryPercentage);

        jLabel1.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel19.add(jLabel1);

        jckIsBulk.setText("Es a granel");
        jckIsBulk.setEnabled(false);
        jckIsBulk.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel19.add(jckIsBulk);

        jckIsInventoriable.setText("Es inventariable");
        jckIsInventoriable.setEnabled(false);
        jckIsInventoriable.setPreferredSize(new java.awt.Dimension(128, 23));
        jPanel19.add(jckIsInventoriable);

        jckIsPrepayment.setText("Es anticipo");
        jckIsPrepayment.setEnabled(false);
        jckIsPrepayment.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel19.add(jckIsPrepayment);

        jPanel4.add(jPanel19);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsDiscountEntryPercentage.setText("Descto. partida en pct.:");
        jckIsDiscountEntryPercentage.setMargin(new java.awt.Insets(2, 0, 2, 2));
        jckIsDiscountEntryPercentage.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel16.add(jckIsDiscountEntryPercentage);

        jtfDiscountEntryPercentage.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDiscountEntryPercentage.setText("0.00%");
        jtfDiscountEntryPercentage.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel16.add(jtfDiscountEntryPercentage);

        jLabel9.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel16.add(jLabel9);

        jckIsDiscountRetailChain.setText("Descuento cadena comercial");
        jckIsDiscountRetailChain.setEnabled(false);
        jckIsDiscountRetailChain.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel16.add(jckIsDiscountRetailChain);

        jPanel4.add(jPanel16);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsDiscountDocApplying.setText("Aplica descto. docto.");
        jckIsDiscountDocApplying.setMargin(new java.awt.Insets(2, 0, 2, 2));
        jckIsDiscountDocApplying.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel17.add(jckIsDiscountDocApplying);

        jtfDiscountDocPercentageRo.setEditable(false);
        jtfDiscountDocPercentageRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDiscountDocPercentageRo.setText("0.00%");
        jtfDiscountDocPercentageRo.setFocusable(false);
        jtfDiscountDocPercentageRo.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel17.add(jtfDiscountDocPercentageRo);

        jLabel7.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel17.add(jLabel7);

        jckIsDeleted.setText("Registro eliminado");
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel17.add(jckIsDeleted);

        jPanel4.add(jPanel17);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOriginalQuantity.setText("Cantidad: *");
        jlOriginalQuantity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel15.add(jlOriginalQuantity);

        jtfOriginalQuantity.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfOriginalQuantity.setText("0.0000");
        jtfOriginalQuantity.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel15.add(jtfOriginalQuantity);

        jtfOriginalUnitSymbolRo.setEditable(false);
        jtfOriginalUnitSymbolRo.setText("UN");
        jtfOriginalUnitSymbolRo.setFocusable(false);
        jtfOriginalUnitSymbolRo.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel15.add(jtfOriginalUnitSymbolRo);

        jPanel4.add(jPanel15);

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlQuantity.setText("Cant. equivalente:");
        jlQuantity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel23.add(jlQuantity);

        jtfQuantityRo.setEditable(false);
        jtfQuantityRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfQuantityRo.setText("0.0000");
        jtfQuantityRo.setFocusable(false);
        jtfQuantityRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel23.add(jtfQuantityRo);

        jtfUnitSymbolRo.setEditable(false);
        jtfUnitSymbolRo.setText("UN");
        jtfUnitSymbolRo.setFocusable(false);
        jtfUnitSymbolRo.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel23.add(jtfUnitSymbolRo);

        jckAuxPreserveQuantity.setText("Preservar cant. equivalente");
        jckAuxPreserveQuantity.setToolTipText("Evitar calcular la cantidad equivalente en función de la configuración del ítem");
        jckAuxPreserveQuantity.setPreferredSize(new java.awt.Dimension(218, 23));
        jPanel23.add(jckAuxPreserveQuantity);

        jckIsSalesFreightRequired.setText("Definir flete u.");
        jckIsSalesFreightRequired.setToolTipText("Definir el flete unitario");
        jckIsSalesFreightRequired.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel23.add(jckIsSalesFreightRequired);

        jPanel4.add(jPanel23);

        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOriginalPriceUnitaryCy.setText("Precio unitario:");
        jlOriginalPriceUnitaryCy.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel20.add(jlOriginalPriceUnitaryCy);

        jtfOriginalPriceUnitaryCy.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfOriginalPriceUnitaryCy.setText("0,000,000.0000");
        jtfOriginalPriceUnitaryCy.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel20.add(jtfOriginalPriceUnitaryCy);

        jtfOriginalPriceUnitaryCyCurrencyKeyRo.setEditable(false);
        jtfOriginalPriceUnitaryCyCurrencyKeyRo.setText("CUR");
        jtfOriginalPriceUnitaryCyCurrencyKeyRo.setFocusable(false);
        jtfOriginalPriceUnitaryCyCurrencyKeyRo.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel20.add(jtfOriginalPriceUnitaryCyCurrencyKeyRo);

        jbOriginalPriceUnitaryCyWizard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_wizard.gif"))); // NOI18N
        jbOriginalPriceUnitaryCyWizard.setToolTipText("Calcular precio unitario [Ctrl + W]");
        jbOriginalPriceUnitaryCyWizard.setFocusable(false);
        jbOriginalPriceUnitaryCyWizard.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel20.add(jbOriginalPriceUnitaryCyWizard);

        jLabel10.setPreferredSize(new java.awt.Dimension(10, 23));
        jPanel20.add(jLabel10);

        jlSalesPriceUnitaryCy.setText("Precio u.:");
        jlSalesPriceUnitaryCy.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel20.add(jlSalesPriceUnitaryCy);

        jtfSalesPriceUnitaryCy.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSalesPriceUnitaryCy.setText("0,000,000.0000");
        jtfSalesPriceUnitaryCy.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel20.add(jtfSalesPriceUnitaryCy);

        jtfSalesPriceUnitaryCyCurrencyKeyRo.setEditable(false);
        jtfSalesPriceUnitaryCyCurrencyKeyRo.setText("CUR");
        jtfSalesPriceUnitaryCyCurrencyKeyRo.setFocusable(false);
        jtfSalesPriceUnitaryCyCurrencyKeyRo.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel20.add(jtfSalesPriceUnitaryCyCurrencyKeyRo);

        jckIsSalesFreightAddPrice.setText("Agregar flete u.");
        jckIsSalesFreightAddPrice.setToolTipText("Agregar el flete unitario al precio unitario");
        jckIsSalesFreightAddPrice.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel20.add(jckIsSalesFreightAddPrice);

        jPanel4.add(jPanel20);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOriginalDiscountUnitaryCy.setText("Descto. unitario:");
        jlOriginalDiscountUnitaryCy.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel21.add(jlOriginalDiscountUnitaryCy);

        jtfOriginalDiscountUnitaryCy.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfOriginalDiscountUnitaryCy.setText("0,000,000.0000");
        jtfOriginalDiscountUnitaryCy.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel21.add(jtfOriginalDiscountUnitaryCy);

        jtfOriginalDiscountUnitaryCyCurrencyKeyRo.setEditable(false);
        jtfOriginalDiscountUnitaryCyCurrencyKeyRo.setText("CUR");
        jtfOriginalDiscountUnitaryCyCurrencyKeyRo.setFocusable(false);
        jtfOriginalDiscountUnitaryCyCurrencyKeyRo.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel21.add(jtfOriginalDiscountUnitaryCyCurrencyKeyRo);

        jbPriceHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif"))); // NOI18N
        jbPriceHistory.setToolTipText("Ver historial de precios");
        jbPriceHistory.setFocusable(false);
        jbPriceHistory.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel21.add(jbPriceHistory);

        jLabel12.setPreferredSize(new java.awt.Dimension(10, 23));
        jPanel21.add(jLabel12);

        jlSalesFreightUnitaryCy.setText("Flete u.:");
        jlSalesFreightUnitaryCy.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel21.add(jlSalesFreightUnitaryCy);

        jtfSalesFreightUnitaryCy.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSalesFreightUnitaryCy.setText("0,000,000.0000");
        jtfSalesFreightUnitaryCy.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel21.add(jtfSalesFreightUnitaryCy);

        jtfSalesFreightUnitaryCyCurrencyKeyRo.setEditable(false);
        jtfSalesFreightUnitaryCyCurrencyKeyRo.setText("CUR");
        jtfSalesFreightUnitaryCyCurrencyKeyRo.setFocusable(false);
        jtfSalesFreightUnitaryCyCurrencyKeyRo.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel21.add(jtfSalesFreightUnitaryCyCurrencyKeyRo);

        jckIsSalesFreightConfirm.setText("Confirmar flete u.");
        jckIsSalesFreightConfirm.setToolTipText("Confirmar si hay que agregar el flete unitario al precio unitario al guardar");
        jckIsSalesFreightConfirm.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel21.add(jckIsSalesFreightConfirm);

        jPanel4.add(jPanel21);

        jPanel22.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel22, java.awt.BorderLayout.CENTER);

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder("Valor de la partida:"));
        jPanel24.setPreferredSize(new java.awt.Dimension(350, 200));
        jPanel24.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel9.setLayout(new java.awt.GridLayout(11, 3, 5, 1));

        jlCurrency.setText("Moneda:");
        jlCurrency.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlCurrency);

        jtfCurrencySystemKeyRo.setEditable(false);
        jtfCurrencySystemKeyRo.setText("ERP");
        jtfCurrencySystemKeyRo.setFocusable(false);
        jtfCurrencySystemKeyRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfCurrencySystemKeyRo);

        jtfCurrencyKeyRo.setEditable(false);
        jtfCurrencyKeyRo.setText("CUR");
        jtfCurrencyKeyRo.setFocusable(false);
        jtfCurrencyKeyRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfCurrencyKeyRo);

        jlPriceUnitary.setText("Precio unitario:");
        jlPriceUnitary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlPriceUnitary);

        jtfPriceUnitaryRo.setEditable(false);
        jtfPriceUnitaryRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPriceUnitaryRo.setText("0,000,000.0000");
        jtfPriceUnitaryRo.setFocusable(false);
        jtfPriceUnitaryRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfPriceUnitaryRo);

        jtfPriceUnitaryCyRo.setEditable(false);
        jtfPriceUnitaryCyRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPriceUnitaryCyRo.setText("0,000,000.0000");
        jtfPriceUnitaryCyRo.setFocusable(false);
        jtfPriceUnitaryCyRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfPriceUnitaryCyRo);

        jlDiscountUnitary.setText("Descto. unitario (-):");
        jlDiscountUnitary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlDiscountUnitary);

        jtfDiscountUnitaryRo.setEditable(false);
        jtfDiscountUnitaryRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDiscountUnitaryRo.setText("0,000,000.0000");
        jtfDiscountUnitaryRo.setFocusable(false);
        jtfDiscountUnitaryRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfDiscountUnitaryRo);

        jtfDiscountUnitaryCyRo.setEditable(false);
        jtfDiscountUnitaryCyRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDiscountUnitaryCyRo.setText("0,000,000.0000");
        jtfDiscountUnitaryCyRo.setFocusable(false);
        jtfDiscountUnitaryCyRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfDiscountUnitaryCyRo);

        jlDiscountEntry.setText("Descto. partida (-):");
        jlDiscountEntry.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlDiscountEntry);

        jtfDiscountEntryRo.setEditable(false);
        jtfDiscountEntryRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDiscountEntryRo.setText("0,000,000,000.00");
        jtfDiscountEntryRo.setFocusable(false);
        jtfDiscountEntryRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfDiscountEntryRo);

        jtfDiscountEntryCy.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDiscountEntryCy.setText("0,000,000,000.00");
        jtfDiscountEntryCy.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfDiscountEntryCy);

        jlSubtotalProvisional_r.setText("Subtotal provisional:");
        jlSubtotalProvisional_r.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlSubtotalProvisional_r);

        jtfSubtotalProvisional_rRo.setEditable(false);
        jtfSubtotalProvisional_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSubtotalProvisional_rRo.setText("0,000,000,000.00");
        jtfSubtotalProvisional_rRo.setFocusable(false);
        jtfSubtotalProvisional_rRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfSubtotalProvisional_rRo);

        jtfSubtotalProvisionalCy_rRo.setEditable(false);
        jtfSubtotalProvisionalCy_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSubtotalProvisionalCy_rRo.setText("0,000,000,000.00");
        jtfSubtotalProvisionalCy_rRo.setFocusable(false);
        jtfSubtotalProvisionalCy_rRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfSubtotalProvisionalCy_rRo);

        jlDiscountDoc.setText("Descto. docto. (-):");
        jlDiscountDoc.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlDiscountDoc);

        jtfDiscountDocRo.setEditable(false);
        jtfDiscountDocRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDiscountDocRo.setText("0,000,000,000.00");
        jtfDiscountDocRo.setFocusable(false);
        jtfDiscountDocRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfDiscountDocRo);

        jtfDiscountDocCy.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDiscountDocCy.setText("0,000,000,000.00");
        jtfDiscountDocCy.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfDiscountDocCy);

        jlSubtotal_r.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlSubtotal_r.setText("Subtotal partida:");
        jlSubtotal_r.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlSubtotal_r);

        jtfSubtotal_rRo.setEditable(false);
        jtfSubtotal_rRo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfSubtotal_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSubtotal_rRo.setText("0,000,000,000.00");
        jtfSubtotal_rRo.setFocusable(false);
        jtfSubtotal_rRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfSubtotal_rRo);

        jtfSubtotalCy_rRo.setEditable(false);
        jtfSubtotalCy_rRo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfSubtotalCy_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSubtotalCy_rRo.setText("0,000,000,000.00");
        jtfSubtotalCy_rRo.setFocusable(false);
        jtfSubtotalCy_rRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfSubtotalCy_rRo);

        jlTaxCharged_r.setText("Imptos. trasladados:");
        jlTaxCharged_r.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlTaxCharged_r);

        jtfTaxCharged_rRo.setEditable(false);
        jtfTaxCharged_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTaxCharged_rRo.setText("0,000,000,000.00");
        jtfTaxCharged_rRo.setFocusable(false);
        jtfTaxCharged_rRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfTaxCharged_rRo);

        jtfTaxChargedCy_rRo.setEditable(false);
        jtfTaxChargedCy_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTaxChargedCy_rRo.setText("0,000,000,000.00");
        jtfTaxChargedCy_rRo.setFocusable(false);
        jtfTaxChargedCy_rRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfTaxChargedCy_rRo);

        jlTaxRetained_r.setText("Imptos. retenidos (-):");
        jlTaxRetained_r.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlTaxRetained_r);

        jtfTaxRetained_rRo.setEditable(false);
        jtfTaxRetained_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTaxRetained_rRo.setText("0,000,000,000.00");
        jtfTaxRetained_rRo.setFocusable(false);
        jtfTaxRetained_rRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfTaxRetained_rRo);

        jtfTaxRetainedCy_rRo.setEditable(false);
        jtfTaxRetainedCy_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTaxRetainedCy_rRo.setText("0,000,000,000.00");
        jtfTaxRetainedCy_rRo.setFocusable(false);
        jtfTaxRetainedCy_rRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfTaxRetainedCy_rRo);

        jlTotal_r.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlTotal_r.setText("Total partida:");
        jlTotal_r.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlTotal_r);

        jtfTotal_rRo.setEditable(false);
        jtfTotal_rRo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfTotal_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTotal_rRo.setText("0,000,000,000.00");
        jtfTotal_rRo.setFocusable(false);
        jtfTotal_rRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfTotal_rRo);

        jtfTotalCy_rRo.setEditable(false);
        jtfTotalCy_rRo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfTotalCy_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTotalCy_rRo.setText("0,000,000,000.00");
        jtfTotalCy_rRo.setFocusable(false);
        jtfTotalCy_rRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfTotalCy_rRo);

        jlPriceUnitaryReal_r.setForeground(java.awt.Color.gray);
        jlPriceUnitaryReal_r.setText("Precio unitario neto:");
        jlPriceUnitaryReal_r.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlPriceUnitaryReal_r);

        jtfPriceUnitaryReal_rRo.setEditable(false);
        jtfPriceUnitaryReal_rRo.setForeground(java.awt.Color.gray);
        jtfPriceUnitaryReal_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPriceUnitaryReal_rRo.setText("0,000,000.0000");
        jtfPriceUnitaryReal_rRo.setFocusable(false);
        jtfPriceUnitaryReal_rRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfPriceUnitaryReal_rRo);

        jtfPriceUnitaryRealCy_rRo.setEditable(false);
        jtfPriceUnitaryRealCy_rRo.setForeground(java.awt.Color.gray);
        jtfPriceUnitaryRealCy_rRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPriceUnitaryRealCy_rRo.setText("0,000,000.0000");
        jtfPriceUnitaryRealCy_rRo.setFocusable(false);
        jtfPriceUnitaryRealCy_rRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfPriceUnitaryRealCy_rRo);

        jPanel5.add(jPanel9, java.awt.BorderLayout.NORTH);

        jPanel24.add(jPanel5, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel24, java.awt.BorderLayout.EAST);

        jPanel2.add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Miscelánea:"));
        jPanel18.setLayout(new java.awt.BorderLayout());

        jPanel35.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkItemReferenceId_n.setText("Ítem referencia:");
        jlFkItemReferenceId_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlFkItemReferenceId_n);

        jcbFkItemReferenceId_n.setMaximumRowCount(12);
        jcbFkItemReferenceId_n.setPreferredSize(new java.awt.Dimension(450, 23));
        jPanel12.add(jcbFkItemReferenceId_n);

        jbFkItemReferenceId_n.setText("...");
        jbFkItemReferenceId_n.setToolTipText("Seleccionar ítem referencia");
        jbFkItemReferenceId_n.setFocusable(false);
        jbFkItemReferenceId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel12.add(jbFkItemReferenceId_n);

        jPanel35.add(jPanel12);

        jPanel40.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReference.setText("Referencia:");
        jlReference.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel40.add(jlReference);

        jtfReference.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel40.add(jtfReference);

        jlOperationsType.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlOperationsType.setText("Tipo operación:");
        jlOperationsType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel40.add(jlOperationsType);

        jcbOperationsType.setPreferredSize(new java.awt.Dimension(190, 23));
        jPanel40.add(jcbOperationsType);

        jPanel35.add(jPanel40);

        jPanel18.add(jPanel35, java.awt.BorderLayout.NORTH);

        jPanel3.add(jPanel18, java.awt.BorderLayout.WEST);

        jpCostCenter.setBorder(javax.swing.BorderFactory.createTitledBorder("Centro de costo-beneficio:"));
        jpCostCenter.setPreferredSize(new java.awt.Dimension(400, 50));
        jpCostCenter.setLayout(new java.awt.BorderLayout());

        jlDummyCostCenter.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlDummyCostCenter.setText("[Panel centro de costo-beneficio]");
        jlDummyCostCenter.setPreferredSize(new java.awt.Dimension(250, 50));
        jpCostCenter.add(jlDummyCostCenter, java.awt.BorderLayout.NORTH);

        jPanel3.add(jpCostCenter, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        jpRegistry.add(jPanel2, java.awt.BorderLayout.NORTH);

        jpTaxes.setLayout(new java.awt.BorderLayout());

        jpTaxInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Impuestos de la partida:"));
        jpTaxInfo.setLayout(new java.awt.BorderLayout(10, 0));

        jPanel14.setLayout(new java.awt.GridLayout(2, 2, 5, 1));

        jlTaxIdentityEmisor.setText("Identidad de impuestos del emisor:");
        jlTaxIdentityEmisor.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel14.add(jlTaxIdentityEmisor);

        jtfTaxIdentityEmisorRo.setEditable(false);
        jtfTaxIdentityEmisorRo.setText("TAX IDENTITY");
        jtfTaxIdentityEmisorRo.setFocusable(false);
        jPanel14.add(jtfTaxIdentityEmisorRo);

        jlTaxIdentityReceptor.setText("Identidad de impuestos del receptor:");
        jlTaxIdentityReceptor.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel14.add(jlTaxIdentityReceptor);

        jtfTaxIdentityReceptorRo.setEditable(false);
        jtfTaxIdentityReceptorRo.setText("TAX IDENTITY");
        jtfTaxIdentityReceptorRo.setFocusable(false);
        jPanel14.add(jtfTaxIdentityReceptorRo);

        jpTaxInfo.add(jPanel14, java.awt.BorderLayout.WEST);

        jPanel33.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkTaxRegionId.setText("Región de impuestos: *");
        jlFkTaxRegionId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel13.add(jlFkTaxRegionId);

        jcbFkTaxRegionId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel13.add(jcbFkTaxRegionId);

        jbFkTaxRegionId.setText("...");
        jbFkTaxRegionId.setToolTipText("Seleccionar región de impuestos");
        jbFkTaxRegionId.setFocusable(false);
        jbFkTaxRegionId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel13.add(jbFkTaxRegionId);

        jckIsTaxesAutomaticApplying.setText("Cálculo automático de impuestos");
        jckIsTaxesAutomaticApplying.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jckIsTaxesAutomaticApplying.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel13.add(jckIsTaxesAutomaticApplying);

        jPanel33.add(jPanel13);

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkThirdTaxCausingId_n.setText("Tercero causante:");
        jlFkThirdTaxCausingId_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel34.add(jlFkThirdTaxCausingId_n);

        jcbFkThirdTaxCausingId_n.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel34.add(jcbFkThirdTaxCausingId_n);

        jbFkThirdTaxCausingId_n.setText("...");
        jbFkThirdTaxCausingId_n.setToolTipText("Seleccionar tercero causante");
        jbFkThirdTaxCausingId_n.setFocusable(false);
        jbFkThirdTaxCausingId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel34.add(jbFkThirdTaxCausingId_n);

        jbEditFkThirdTaxCausingId_n.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditFkThirdTaxCausingId_n.setToolTipText("Habilitar tercero causante");
        jbEditFkThirdTaxCausingId_n.setFocusable(false);
        jbEditFkThirdTaxCausingId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel34.add(jbEditFkThirdTaxCausingId_n);

        jPanel33.add(jPanel34);

        jpTaxInfo.add(jPanel33, java.awt.BorderLayout.CENTER);

        jpTaxes.add(jpTaxInfo, java.awt.BorderLayout.NORTH);

        jTabbedPane.addTab("Impuestos", jpTaxes);

        jpCommissions.setLayout(new java.awt.BorderLayout());
        jTabbedPane.addTab("Comisiones", jpCommissions);

        jpPricesData.setLayout(new java.awt.BorderLayout());

        jPanel46.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos generales:"));
        jPanel46.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 1));

        jckIsDpsReqMonthDelivery.setSelected(true);
        jckIsDpsReqMonthDelivery.setText("Requiere entregas mensuales");
        jckIsDpsReqMonthDelivery.setEnabled(false);
        jckIsDpsReqMonthDelivery.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel46.add(jckIsDpsReqMonthDelivery);

        jlDpsContractBase.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlDpsContractBase.setText("Base:");
        jlDpsContractBase.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel46.add(jlDpsContractBase);

        jtfDpsContractBase.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDpsContractBase.setText("0,000.0000");
        jtfDpsContractBase.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel46.add(jtfDpsContractBase);

        jlDpsContractFuture.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlDpsContractFuture.setText("Futuro:");
        jlDpsContractFuture.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel46.add(jlDpsContractFuture);

        jtfDpsContractFuture.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDpsContractFuture.setText("0,000.0000");
        jtfDpsContractFuture.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel46.add(jtfDpsContractFuture);

        jlDpsContractFactor.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlDpsContractFactor.setText("Factor ajuste:");
        jlDpsContractFactor.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel46.add(jlDpsContractFactor);

        jtfDpsContractFactor.setEditable(false);
        jtfDpsContractFactor.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDpsContractFactor.setText("0,000.0000");
        jtfDpsContractFactor.setFocusable(false);
        jtfDpsContractFactor.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel46.add(jtfDpsContractFactor);

        jckIsPriceConfirm.setText("Confirmar precio");
        jckIsPriceConfirm.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jckIsPriceConfirm.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel46.add(jckIsPriceConfirm);

        jpPricesData.add(jPanel46, java.awt.BorderLayout.NORTH);

        jPanel51.setBorder(javax.swing.BorderFactory.createTitledBorder("Entregas mensuales:"));
        jPanel51.setLayout(new java.awt.BorderLayout());

        jPanel59.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jPanel67.setLayout(new java.awt.GridLayout(2, 0, 2, 0));

        jlIsPriceVariable.setText("Tipo precio:");
        jlIsPriceVariable.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel67.add(jlIsPriceVariable);

        jckIsDirectPrice.setSelected(true);
        jckIsDirectPrice.setText("Directo");
        jckIsDirectPrice.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jckIsDirectPrice.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel67.add(jckIsDirectPrice);

        jPanel59.add(jPanel67);

        jPanel39.setLayout(new java.awt.GridLayout(2, 0, 2, 0));

        jlContractPriceNumbrerReference.setText("No. orden:");
        jlContractPriceNumbrerReference.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel39.add(jlContractPriceNumbrerReference);

        jtfContractPriceNumbrerReference.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel39.add(jtfContractPriceNumbrerReference);

        jPanel59.add(jPanel39);

        jPanel63.setLayout(new java.awt.GridLayout(2, 0, 2, 0));

        jlContractPriceYear.setText("Año:");
        jlContractPriceYear.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel63.add(jlContractPriceYear);

        jtfContractPriceYear.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfContractPriceYear.setText("2001");
        jtfContractPriceYear.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel63.add(jtfContractPriceYear);

        jPanel59.add(jPanel63);

        jPanel65.setLayout(new java.awt.GridLayout(2, 0, 2, 0));

        jlContractPriceMonth.setText("Mes:");
        jlContractPriceMonth.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel65.add(jlContractPriceMonth);

        jtfContractPriceMonth.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfContractPriceMonth.setText("01");
        jtfContractPriceMonth.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel65.add(jtfContractPriceMonth);

        jPanel59.add(jPanel65);

        jPanel72.setPreferredSize(new java.awt.Dimension(135, 46));
        jPanel72.setLayout(new java.awt.BorderLayout(5, 0));

        jlPriceOriginalQuantity.setText("Cantidad:");
        jlPriceOriginalQuantity.setPreferredSize(new java.awt.Dimension(62, 23));
        jPanel72.add(jlPriceOriginalQuantity, java.awt.BorderLayout.NORTH);

        jtfPriceOriginalQuantity.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPriceOriginalQuantity.setText("0,000,000.0000");
        jtfPriceOriginalQuantity.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel72.add(jtfPriceOriginalQuantity, java.awt.BorderLayout.CENTER);

        jtfPriceOriginalUnitSymbol.setEditable(false);
        jtfPriceOriginalUnitSymbol.setText("UN");
        jtfPriceOriginalUnitSymbol.setFocusable(false);
        jtfPriceOriginalUnitSymbol.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel72.add(jtfPriceOriginalUnitSymbol, java.awt.BorderLayout.EAST);

        jPanel59.add(jPanel72);

        jPanel77.setLayout(new java.awt.GridLayout(2, 0, 2, 0));

        jLabel2.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel77.add(jLabel2);

        jckChangePrice.setText("Cambiar");
        jckChangePrice.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jckChangePrice.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jPanel77.add(jckChangePrice);

        jPanel59.add(jPanel77);

        jPanel73.setPreferredSize(new java.awt.Dimension(150, 46));
        jPanel73.setLayout(new java.awt.BorderLayout(5, 0));

        jlPriceOriginalPriceUnitaryCy.setText("Precio unitario $:");
        jlPriceOriginalPriceUnitaryCy.setPreferredSize(new java.awt.Dimension(62, 23));
        jPanel73.add(jlPriceOriginalPriceUnitaryCy, java.awt.BorderLayout.NORTH);

        jtfPriceOriginalPriceUnitaryCy.setEditable(false);
        jtfPriceOriginalPriceUnitaryCy.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPriceOriginalPriceUnitaryCy.setText("0,000,000.0000");
        jtfPriceOriginalPriceUnitaryCy.setFocusable(false);
        jtfPriceOriginalPriceUnitaryCy.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel73.add(jtfPriceOriginalPriceUnitaryCy, java.awt.BorderLayout.CENTER);

        jtfPriceCurrencyKeyPriceUnitaryCy.setEditable(false);
        jtfPriceCurrencyKeyPriceUnitaryCy.setText("CUR");
        jtfPriceCurrencyKeyPriceUnitaryCy.setFocusable(false);
        jtfPriceCurrencyKeyPriceUnitaryCy.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel73.add(jtfPriceCurrencyKeyPriceUnitaryCy, java.awt.BorderLayout.EAST);

        jPanel59.add(jPanel73);

        jPanel74.setLayout(new java.awt.GridLayout(2, 0, 2, 0));

        jlContractBase.setText("Base:");
        jlContractBase.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel74.add(jlContractBase);

        jtfContractBase.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfContractBase.setText("0,000.0000");
        jtfContractBase.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel74.add(jtfContractBase);

        jPanel59.add(jPanel74);

        jPanel75.setLayout(new java.awt.GridLayout(2, 0, 2, 0));

        jlContractFuture.setText("Futuro:");
        jlContractFuture.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel75.add(jlContractFuture);

        jtfContractFuture.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfContractFuture.setText("0,000.0000");
        jtfContractFuture.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel75.add(jtfContractFuture);

        jPanel59.add(jPanel75);

        jPanel76.setLayout(new java.awt.GridLayout(2, 0, 2, 0));

        jlContractFactor.setText("Factor ajuste:");
        jlContractFactor.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel76.add(jlContractFactor);

        jtfContractFactor.setEditable(false);
        jtfContractFactor.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfContractFactor.setText("0,000.0000");
        jtfContractFactor.setFocusable(false);
        jtfContractFactor.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel76.add(jtfContractFactor);

        jPanel59.add(jPanel76);

        jPanel78.setLayout(new java.awt.GridLayout(2, 0, 2, 0));
        jPanel78.add(jLabel19);

        jbPriceNew.setText("Nueva");
        jbPriceNew.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbPriceNew.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel78.add(jbPriceNew);

        jPanel59.add(jPanel78);

        jPanel80.setLayout(new java.awt.GridLayout(2, 0, 2, 0));
        jPanel80.add(jLabel21);

        jbPriceSave.setText("Guardar");
        jbPriceSave.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbPriceSave.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel80.add(jbPriceSave);

        jPanel59.add(jPanel80);

        jPanel79.setLayout(new java.awt.GridLayout(2, 0, 2, 0));
        jPanel79.add(jLabel20);

        jbPriceClear.setText("Limpiar");
        jbPriceClear.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbPriceClear.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel79.add(jbPriceClear);

        jPanel59.add(jPanel79);

        jPanel51.add(jPanel59, java.awt.BorderLayout.NORTH);

        jpPrices.setLayout(new java.awt.BorderLayout());

        jpNotesControls1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 0));

        jbGridPriceNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbGridPriceNew.setToolTipText("Crear entrega");
        jbGridPriceNew.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesControls1.add(jbGridPriceNew);

        jbGridPriceEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbGridPriceEdit.setToolTipText("Modificar entrega");
        jbGridPriceEdit.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesControls1.add(jbGridPriceEdit);

        jbGridPriceDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbGridPriceDelete.setToolTipText("Eliminar entrega");
        jbGridPriceDelete.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesControls1.add(jbGridPriceDelete);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setPreferredSize(new java.awt.Dimension(3, 23));
        jpNotesControls1.add(jSeparator2);

        jtbGridPricesFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_filter_off.gif"))); // NOI18N
        jtbGridPricesFilter.setToolTipText("Filtrar entregas eliminadas");
        jtbGridPricesFilter.setPreferredSize(new java.awt.Dimension(23, 23));
        jtbGridPricesFilter.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_filter_on.gif"))); // NOI18N
        jpNotesControls1.add(jtbGridPricesFilter);

        jpPrices.add(jpNotesControls1, java.awt.BorderLayout.NORTH);

        jPanel51.add(jpPrices, java.awt.BorderLayout.CENTER);

        jpPricesData.add(jPanel51, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Entregas mensuales", jpPricesData);

        jpMarketing.setLayout(new java.awt.BorderLayout());

        jpDataShip.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de embarque:"));
        jpDataShip.setLayout(new java.awt.BorderLayout());

        jPanel38.setLayout(new java.awt.GridLayout(8, 1, 0, 1));

        jPanel41.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkVehicleTypeId_n.setText("Tipo vehículo:");
        jlFkVehicleTypeId_n.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel41.add(jlFkVehicleTypeId_n);

        jcbFkVehicleTypeId_n.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel41.add(jcbFkVehicleTypeId_n);

        jPanel38.add(jPanel41);

        jPanel43.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDriver.setText("Chofer:");
        jlDriver.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel43.add(jlDriver);

        jtfDriver.setText("DRIVER");
        jtfDriver.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel43.add(jtfDriver);

        jPanel38.add(jPanel43);

        jPanel45.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlVehicleNumber.setText("Placas vehículo:");
        jlVehicleNumber.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel45.add(jlVehicleNumber);

        jtfVehicleNumber.setText("VEHICLE NUMBER");
        jtfVehicleNumber.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel45.add(jtfVehicleNumber);

        jPanel38.add(jPanel45);

        jPanel47.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlContainerTank.setText("Remolque, tanq./cont.:");
        jlContainerTank.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel47.add(jlContainerTank);

        jtfContTank.setText("TRAILER CONTAINER TANK");
        jtfContTank.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel47.add(jtfContTank);

        jlAddingMultipleMailHelp.setForeground(new java.awt.Color(109, 109, 109));
        jlAddingMultipleMailHelp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAddingMultipleMailHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlAddingMultipleMailHelp.setToolTipText("Número/ID remolque y tanque/contenedor");
        jlAddingMultipleMailHelp.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel47.add(jlAddingMultipleMailHelp);

        jPanel38.add(jPanel47);

        jPanel55.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlQualitySeal.setText("Sello(s) de calidad:");
        jlQualitySeal.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel55.add(jlQualitySeal);

        jtfSealQuality.setText("SEAL QUALITY");
        jtfSealQuality.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel55.add(jtfSealQuality);

        jlAddingMultipleMailHelp1.setForeground(new java.awt.Color(109, 109, 109));
        jlAddingMultipleMailHelp1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAddingMultipleMailHelp1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlAddingMultipleMailHelp1.setToolTipText("Sellos de la empresa");
        jlAddingMultipleMailHelp1.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel55.add(jlAddingMultipleMailHelp1);

        jPanel38.add(jPanel55);

        jPanel57.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlSecuritySeal.setText("Sello(s) de seguridad:");
        jlSecuritySeal.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel57.add(jlSecuritySeal);

        jtfSecuritySeal.setText("SEAL SECURITY");
        jtfSecuritySeal.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel57.add(jtfSecuritySeal);

        jlAddingMultipleMailHelp2.setForeground(new java.awt.Color(109, 109, 109));
        jlAddingMultipleMailHelp2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAddingMultipleMailHelp2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlAddingMultipleMailHelp2.setToolTipText("Sellos del asociado de negocios");
        jlAddingMultipleMailHelp2.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel57.add(jlAddingMultipleMailHelp2);

        jPanel38.add(jPanel57);

        jPanel56.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlTicket.setText("Boleto(s) báscula:");
        jlTicket.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel56.add(jlTicket);

        jtfTicket.setText("TICKET");
        jtfTicket.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel56.add(jtfTicket);

        jPanel38.add(jPanel56);

        jPanel58.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlVgm.setText("VGM:");
        jlVgm.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel58.add(jlVgm);

        jtfVgm.setText("VGM");
        jtfVgm.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel58.add(jtfVgm);

        jbEditLogistics.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit_ro.gif"))); // NOI18N
        jbEditLogistics.setToolTipText("Modificar datos");
        jbEditLogistics.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel58.add(jbEditLogistics);

        jPanel38.add(jPanel58);

        jpDataShip.add(jPanel38, java.awt.BorderLayout.NORTH);

        jpMarketing.add(jpDataShip, java.awt.BorderLayout.EAST);

        jpExtraDataUnits.setBorder(javax.swing.BorderFactory.createTitledBorder("Unidades físicas de la partida:"));
        jpExtraDataUnits.setLayout(new java.awt.BorderLayout());

        jpExtraDataUnitsNorth.setLayout(new java.awt.GridLayout(7, 1, 0, 1));

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLength.setText("Longitud: *");
        jlLength.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel27.add(jlLength);

        jtfLength.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfLength.setText("0.0000");
        jtfLength.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel27.add(jtfLength);

        jtfLengthUnitSymbolRo.setEditable(false);
        jtfLengthUnitSymbolRo.setText("UN");
        jtfLengthUnitSymbolRo.setFocusable(false);
        jtfLengthUnitSymbolRo.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel27.add(jtfLengthUnitSymbolRo);

        jpExtraDataUnitsNorth.add(jPanel27);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSurface.setText("Superficie: *");
        jlSurface.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel28.add(jlSurface);

        jtfSurface.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSurface.setText("0.0000");
        jtfSurface.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel28.add(jtfSurface);

        jtfSurfaceUnitSymbolRo.setEditable(false);
        jtfSurfaceUnitSymbolRo.setText("UN");
        jtfSurfaceUnitSymbolRo.setFocusable(false);
        jtfSurfaceUnitSymbolRo.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel28.add(jtfSurfaceUnitSymbolRo);

        jpExtraDataUnitsNorth.add(jPanel28);

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVolume.setText("Volumen: *");
        jlVolume.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel29.add(jlVolume);

        jtfVolume.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfVolume.setText("0.0000");
        jtfVolume.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel29.add(jtfVolume);

        jtfVolumeUnitSymbolRo.setEditable(false);
        jtfVolumeUnitSymbolRo.setText("UN");
        jtfVolumeUnitSymbolRo.setFocusable(false);
        jtfVolumeUnitSymbolRo.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel29.add(jtfVolumeUnitSymbolRo);

        jpExtraDataUnitsNorth.add(jPanel29);

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMass.setText("Masa: *");
        jlMass.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel30.add(jlMass);

        jtfMass.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfMass.setText("0.0000");
        jtfMass.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel30.add(jtfMass);

        jtfMassUnitSymbolRo.setEditable(false);
        jtfMassUnitSymbolRo.setText("UN");
        jtfMassUnitSymbolRo.setFocusable(false);
        jtfMassUnitSymbolRo.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel30.add(jtfMassUnitSymbolRo);

        jpExtraDataUnitsNorth.add(jPanel30);

        jPanel66.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlWeightPackagingExtra.setText("Peso empaque:");
        jlWeightPackagingExtra.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel66.add(jlWeightPackagingExtra);

        jtfWeightPackagingExtra.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfWeightPackagingExtra.setText("0.0000");
        jtfWeightPackagingExtra.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel66.add(jtfWeightPackagingExtra);

        jtfWeightPackagingExtraUnitSymbolRo.setEditable(false);
        jtfWeightPackagingExtraUnitSymbolRo.setText("UN");
        jtfWeightPackagingExtraUnitSymbolRo.setFocusable(false);
        jtfWeightPackagingExtraUnitSymbolRo.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel66.add(jtfWeightPackagingExtraUnitSymbolRo);

        jpExtraDataUnitsNorth.add(jPanel66);

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlWeightGross.setText("Peso bruto:");
        jlWeightGross.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel31.add(jlWeightGross);

        jtfWeightGrossRo.setEditable(false);
        jtfWeightGrossRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfWeightGrossRo.setText("0.0000");
        jtfWeightGrossRo.setFocusable(false);
        jtfWeightGrossRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel31.add(jtfWeightGrossRo);

        jtfWeightGrossUnitSymbolRo.setEditable(false);
        jtfWeightGrossUnitSymbolRo.setText("UN");
        jtfWeightGrossUnitSymbolRo.setFocusable(false);
        jtfWeightGrossUnitSymbolRo.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel31.add(jtfWeightGrossUnitSymbolRo);

        jpExtraDataUnitsNorth.add(jPanel31);

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlWeightDelivery.setText("Peso flete:");
        jlWeightDelivery.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel32.add(jlWeightDelivery);

        jtfWeightDeliveryRo.setEditable(false);
        jtfWeightDeliveryRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfWeightDeliveryRo.setText("0.0000");
        jtfWeightDeliveryRo.setFocusable(false);
        jtfWeightDeliveryRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel32.add(jtfWeightDeliveryRo);

        jtfWeightDeliveryUnitSymbolRo.setEditable(false);
        jtfWeightDeliveryUnitSymbolRo.setText("UN");
        jtfWeightDeliveryUnitSymbolRo.setFocusable(false);
        jtfWeightDeliveryUnitSymbolRo.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel32.add(jtfWeightDeliveryUnitSymbolRo);

        jpExtraDataUnitsNorth.add(jPanel32);

        jpExtraDataUnits.add(jpExtraDataUnitsNorth, java.awt.BorderLayout.NORTH);

        jpMarketing.add(jpExtraDataUnits, java.awt.BorderLayout.WEST);

        jpExtraDataOther.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpExtraDataOther.setLayout(new java.awt.GridLayout(2, 1));

        jpExtraDataOtherNorth.setBorder(javax.swing.BorderFactory.createTitledBorder("Surtido y vinculación de la partida:"));
        jpExtraDataOtherNorth.setLayout(new java.awt.GridLayout(3, 1, 1, 0));

        jpExtraDataOtherFillment.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jckIsSurplusPercentageApplying.setText("Aplica excedente al surtir o vincular la partida:");
        jckIsSurplusPercentageApplying.setPreferredSize(new java.awt.Dimension(275, 23));
        jpExtraDataOtherFillment.add(jckIsSurplusPercentageApplying);

        jtfSurplusPercentage.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSurplusPercentage.setText("0.00%");
        jtfSurplusPercentage.setPreferredSize(new java.awt.Dimension(75, 23));
        jpExtraDataOtherFillment.add(jtfSurplusPercentage);

        jpExtraDataOtherNorth.add(jpExtraDataOtherFillment);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel3.setForeground(java.awt.SystemColor.textInactiveText);
        jLabel3.setText("Surtir una partida: Asociar la partida con movimientos de almacén (i.e., entrada o salida).");
        jLabel3.setPreferredSize(new java.awt.Dimension(600, 23));
        jPanel25.add(jLabel3);

        jpExtraDataOtherNorth.add(jPanel25);

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel5.setForeground(java.awt.SystemColor.textInactiveText);
        jLabel5.setText("Vincular una partida: Asociar la partida con otro documento (e.g., de pedido a factura).");
        jLabel5.setPreferredSize(new java.awt.Dimension(600, 23));
        jPanel26.add(jLabel5);

        jpExtraDataOtherNorth.add(jPanel26);

        jpExtraDataOther.add(jpExtraDataOtherNorth);

        jPanel60.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel60.setLayout(new java.awt.GridLayout(3, 0));

        jPanel61.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAccOptions.setText("Contabilizar en:");
        jlAccOptions.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel61.add(jlAccOptions);

        bgAccOptions.add(jradAccCashAccount);
        jradAccCashAccount.setText("Cuenta dinero");
        jradAccCashAccount.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel61.add(jradAccCashAccount);

        bgAccOptions.add(jradAccAdvanceBilled);
        jradAccAdvanceBilled.setText("Anticipos fact.");
        jradAccAdvanceBilled.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel61.add(jradAccAdvanceBilled);

        jPanel60.add(jPanel61);

        jPanel62.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCashAccountId_n.setText("Cuenta dinero:");
        jlFkCashAccountId_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel62.add(jlFkCashAccountId_n);

        jcbFkCashAccountId_n.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel62.add(jcbFkCashAccountId_n);

        jPanel60.add(jPanel62);

        jPanel64.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCashAccountId_n1.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel64.add(jlFkCashAccountId_n1);

        jLabel11.setForeground(java.awt.SystemColor.textInactiveText);
        jLabel11.setText("Para la contrapartida del anticipo.");
        jLabel11.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel64.add(jLabel11);

        jPanel60.add(jPanel64);

        jpExtraDataOther.add(jPanel60);

        jpMarketing.add(jpExtraDataOther, java.awt.BorderLayout.CENTER);

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

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setPreferredSize(new java.awt.Dimension(3, 23));
        jpNotesControls.add(jSeparator1);

        jtbNotesFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_filter_off.gif"))); // NOI18N
        jtbNotesFilter.setToolTipText("Filtrar notas eliminadas [Ctrl + F]");
        jtbNotesFilter.setPreferredSize(new java.awt.Dimension(23, 23));
        jtbNotesFilter.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_filter_on.gif"))); // NOI18N
        jpNotesControls.add(jtbNotesFilter);

        jbPickSystemNotes.setText("...");
        jbPickSystemNotes.setToolTipText("Seleccionar notas de sistema");
        jbPickSystemNotes.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesControls.add(jbPickSystemNotes);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator3.setPreferredSize(new java.awt.Dimension(3, 23));
        jpNotesControls.add(jSeparator3);

        jbEditNotes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit_ro.gif"))); // NOI18N
        jbEditNotes.setToolTipText("Modificar notas");
        jbEditNotes.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesControls.add(jbEditNotes);

        jpNotes.add(jpNotesControls, java.awt.BorderLayout.NORTH);

        jTabbedPane.addTab("Notas", jpNotes);

        jpCfdAddenda.setBorder(javax.swing.BorderFactory.createTitledBorder("Información para addenda:"));
        jpCfdAddenda.setLayout(new java.awt.BorderLayout());

        jPanel37.setLayout(new java.awt.BorderLayout());

        jPanel42.setLayout(new java.awt.GridLayout(6, 1, 0, 2));

        jPanel94.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel94.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddCfdAddendaType.setText("Tipo addenda:");
        jlAddCfdAddendaType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel94.add(jlAddCfdAddendaType);

        jtfAddCfdAddendaType.setEditable(false);
        jtfAddCfdAddendaType.setText("TEXT");
        jtfAddCfdAddendaType.setFocusable(false);
        jtfAddCfdAddendaType.setOpaque(false);
        jtfAddCfdAddendaType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel94.add(jtfAddCfdAddendaType);

        jPanel42.add(jPanel94);

        jPanel68.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel68.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddBachocoNúmeroPosición.setText("No. posición: *");
        jlAddBachocoNúmeroPosición.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel68.add(jlAddBachocoNúmeroPosición);

        jtfAddBachocoNúmeroPosición.setText("TEXT");
        jtfAddBachocoNúmeroPosición.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel68.add(jtfAddBachocoNúmeroPosición);

        jPanel42.add(jPanel68);

        jPanel69.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddBachocoCentro.setText("Centro: *");
        jlAddBachocoCentro.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel69.add(jlAddBachocoCentro);

        jtfAddBachocoCentro.setText("TEXT");
        jtfAddBachocoCentro.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel69.add(jtfAddBachocoCentro);

        jPanel42.add(jPanel69);

        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddLorealEntryNumber.setText("No. partida:");
        jlAddLorealEntryNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel36.add(jlAddLorealEntryNumber);

        jtfAddLorealEntryNumber.setText("TEXT");
        jtfAddLorealEntryNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel36.add(jtfAddLorealEntryNumber);

        jPanel42.add(jPanel36);

        jPanel44.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddElektraOrder.setText("Folio pedido: *");
        jlAddElektraOrder.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel44.add(jlAddElektraOrder);

        jtfAddElektraOrder.setText("TEXT");
        jtfAddElektraOrder.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel44.add(jtfAddElektraOrder);

        jPanel42.add(jPanel44);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddGenBarcode.setText("Código barras: *");
        jlAddGenBarcode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlAddGenBarcode);

        jcbAddGenBarcode.setEditable(true);
        jcbAddGenBarcode.setMaximumRowCount(12);
        jcbAddGenBarcode.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel11.add(jcbAddGenBarcode);

        jPanel42.add(jPanel11);

        jPanel37.add(jPanel42, java.awt.BorderLayout.NORTH);

        jpCfdAddenda.add(jPanel37, java.awt.BorderLayout.WEST);

        jPanel48.setLayout(new java.awt.BorderLayout());

        jPanel49.setLayout(new java.awt.GridLayout(6, 1, 0, 2));

        jPanel70.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel70.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddElektraCages.setText("Cajas entregadas: *");
        jlAddElektraCages.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel70.add(jlAddElektraCages);

        jtfAddElektraCages.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfAddElektraCages.setText("0");
        jtfAddElektraCages.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel70.add(jtfAddElektraCages);

        jPanel49.add(jPanel70);

        jPanel71.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddElektraCagePriceUnitary.setText("Precio u. por caja: *");
        jlAddElektraCagePriceUnitary.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel71.add(jlAddElektraCagePriceUnitary);

        jtfAddElektraCagePriceUnitary.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfAddElektraCagePriceUnitary.setText("0");
        jtfAddElektraCagePriceUnitary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel71.add(jtfAddElektraCagePriceUnitary);

        jPanel49.add(jPanel71);

        jPanel50.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddElektraParts.setText("Piezas entregadas: *");
        jlAddElektraParts.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel50.add(jlAddElektraParts);

        jtfAddElektraParts.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfAddElektraParts.setText("0");
        jtfAddElektraParts.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel50.add(jtfAddElektraParts);

        jPanel49.add(jPanel50);

        jPanel52.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddElektraPartPriceUnitary.setText("Precio u. por pieza: *");
        jlAddElektraPartPriceUnitary.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel52.add(jlAddElektraPartPriceUnitary);

        jtfAddElektraPartPriceUnitary.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfAddElektraPartPriceUnitary.setText("0");
        jtfAddElektraPartPriceUnitary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel52.add(jtfAddElektraPartPriceUnitary);

        jPanel49.add(jPanel52);

        jPanel48.add(jPanel49, java.awt.BorderLayout.NORTH);

        jpCfdAddenda.add(jPanel48, java.awt.BorderLayout.CENTER);

        jPanel53.setLayout(new java.awt.BorderLayout());

        jPanel54.setLayout(new java.awt.GridLayout(6, 1, 0, 2));

        jPanel87.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddAmc71PurchaseOrder.setText("Folio ord. cpa.: *");
        jlAddAmc71PurchaseOrder.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel87.add(jlAddAmc71PurchaseOrder);

        jtfAddAmc71PurchaseOrder.setText("TEXT");
        jtfAddAmc71PurchaseOrder.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel87.add(jtfAddAmc71PurchaseOrder);

        jPanel54.add(jPanel87);

        jPanel88.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAddAmc71PurchaseOrderDate.setText("Fecha ord. cpa.: *");
        jlAddAmc71PurchaseOrderDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel88.add(jlAddAmc71PurchaseOrderDate);

        jftfAddAmc71PurchaseOrderDate.setText("dd/mm/yyyy");
        jftfAddAmc71PurchaseOrderDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel88.add(jftfAddAmc71PurchaseOrderDate);

        jlAddAmc71PurchaseOrderDateHint.setForeground(java.awt.SystemColor.textInactiveText);
        jlAddAmc71PurchaseOrderDateHint.setText("(dd/mm/aaaa)");
        jlAddAmc71PurchaseOrderDateHint.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel88.add(jlAddAmc71PurchaseOrderDateHint);

        jPanel54.add(jPanel88);

        jPanel53.add(jPanel54, java.awt.BorderLayout.NORTH);

        jpCfdAddenda.add(jPanel53, java.awt.BorderLayout.EAST);

        jTabbedPane.addTab("CFD: addenda", jpCfdAddenda);

        jpCfdComplement.setBorder(javax.swing.BorderFactory.createTitledBorder("Información alterna para Concepto CFD:"));
        jpCfdComplement.setLayout(new java.awt.BorderLayout());

        jPanel81.setLayout(new java.awt.GridLayout(5, 1, 0, 2));

        jPanel86.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel4.setForeground(java.awt.Color.blue);
        jLabel4.setText("IMPORTANTE: Los datos que proporciones a continuación serán incorporados en la emisión del CFDI en lugar de los datos propios de esta partida.");
        jLabel4.setPreferredSize(new java.awt.Dimension(950, 23));
        jPanel86.add(jLabel4);

        jPanel81.add(jPanel86);

        jPanel82.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlComplConceptKey.setText("Clave:");
        jlComplConceptKey.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel82.add(jlComplConceptKey);

        jtfComplConceptKey.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel82.add(jtfComplConceptKey);

        jPanel81.add(jPanel82);

        jPanel83.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlComplConcept.setText("Concepto:");
        jlComplConcept.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel83.add(jlComplConcept);

        jtfComplConcept.setPreferredSize(new java.awt.Dimension(750, 23));
        jPanel83.add(jtfComplConcept);

        jPanel81.add(jPanel83);

        jPanel84.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlComplCfdProdServ.setText("ClaveProdServ:");
        jlComplCfdProdServ.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel84.add(jlComplCfdProdServ);

        jtfComplCfdProdServ.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel84.add(jtfComplCfdProdServ);

        jLabel6.setForeground(java.awt.SystemColor.textInactiveText);
        jLabel6.setText("p. ej., 01010101");
        jLabel6.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel84.add(jLabel6);

        jPanel81.add(jPanel84);

        jPanel85.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlComplCfdUnit.setText("ClaveUnidad:");
        jlComplCfdUnit.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel85.add(jlComplCfdUnit);

        jtfComplCfdUnit.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel85.add(jtfComplCfdUnit);

        jLabel8.setForeground(java.awt.SystemColor.textInactiveText);
        jLabel8.setText("p. ej., XUN");
        jLabel8.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel85.add(jLabel8);

        jPanel81.add(jPanel85);

        jpCfdComplement.add(jPanel81, java.awt.BorderLayout.NORTH);

        jTabbedPane.addTab("CFD: XML", jpCfdComplement);

        jpRegistry.add(jTabbedPane, java.awt.BorderLayout.CENTER);
        jTabbedPane.getAccessibleContext().setAccessibleName("Precios");

        getContentPane().add(jpRegistry, java.awt.BorderLayout.CENTER);

        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar"); // NOI18N
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbOk);

        jbCancel.setText("Cancelar"); // NOI18N
        jbCancel.setToolTipText("[Escape]");
        jpControls.add(jbCancel);

        getContentPane().add(jpControls, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(1040, 679));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        int i = 0;
        STableColumnForm[] aoTableColumns = null;
        moParamsItemPriceList = null;

        mbRightMktPriceListPurchases = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MKT_PLIST_PUR).HasRight;
        mbRightMktPriceListSales = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MKT_PLIST_SAL).HasRight;
        mbRightPurPriceChange = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_PRICE_CHG).HasRight;
        mbRightSalPriceChange = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_PRICE_CHG).HasRight;

        mvFields = new Vector<>();
        moFieldFkItemId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkItemId, jlFkItemId);
        moFieldFkItemId.setPickerButton(jbFkItemId);
        moFieldConceptKey = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtConceptfKey, jlConceptKey);
        moFieldConceptKey.setLengthMax(35);
        moFieldConcept = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfConcept, jlConcept);
        moFieldConcept.setLengthMax(CONCEPT_LENGTH_MAX);
        moFieldFkOriginalUnitId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkOriginalUnitId, jlFkOriginalUnitId);
        moFieldFkOriginalUnitId.setPickerButton(jbFkOriginalUnitId);
        moFieldPartNum = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtPartNum, jlPartNum);
        moFieldIsDiscountUnitaryPercentage = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDiscountUnitaryPercentage);
        moFieldDiscountUnitaryPercentage = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfDiscountUnitaryPercentage, jckIsDiscountUnitaryPercentage);
        moFieldDiscountUnitaryPercentage.setIsPercent(true);
        moFieldDiscountUnitaryPercentage.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsPercentageFormat());
        moFieldIsDiscountEntryPercentage = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDiscountEntryPercentage);
        moFieldDiscountEntryPercentage = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfDiscountEntryPercentage, jckIsDiscountEntryPercentage);
        moFieldDiscountEntryPercentage.setIsPercent(true);
        moFieldDiscountEntryPercentage.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsPercentageFormat());
        moFieldIsDiscountDocApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDiscountDocApplying);
        moFieldOriginalQuantity = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfOriginalQuantity, jlOriginalQuantity);
        moFieldOriginalQuantity.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat());
        moFieldOriginalQuantity.setDoubleMin(-1);
        moFieldOriginalQuantity.setMinInclusive(true);
        moFieldOriginalPriceUnitaryCy = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfOriginalPriceUnitaryCy, jlOriginalPriceUnitaryCy);
        moFieldOriginalPriceUnitaryCy.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormat());
        moFieldOriginalDiscountUnitaryCy = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfOriginalDiscountUnitaryCy, jlOriginalDiscountUnitaryCy);
        moFieldOriginalDiscountUnitaryCy.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormat());
        moFieldSalesPriceUnitaryCy = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfSalesPriceUnitaryCy, jlSalesPriceUnitaryCy);
        moFieldSalesPriceUnitaryCy.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormat());
        moFieldSalesFreightUnitaryCy = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfSalesFreightUnitaryCy, jlSalesFreightUnitaryCy);
        moFieldSalesFreightUnitaryCy.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormat());
        moFieldIsSalesFreightRequired = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsSalesFreightRequired);
        moFieldIsSalesFreightAddPrice = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsSalesFreightAddPrice);
        moFieldIsSalesFreightConfirm = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsSalesFreightConfirm);
        moFieldDiscountEntryCy = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfDiscountEntryCy, jlDiscountEntry);
        moFieldDiscountEntryCy.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat());
        moFieldDiscountDocCy = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfDiscountDocCy, jlDiscountDoc);
        moFieldDiscountDocCy.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat());
        moFieldSurplusPercentage = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfSurplusPercentage, jckIsSurplusPercentageApplying);
        moFieldSurplusPercentage.setIsPercent(true);
        moFieldSurplusPercentage.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsPercentageFormat());
        
        moFieldIsDpsPriceVariable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDpsReqMonthDelivery);
        moFieldIsDpsPriceVariable.setBoolean(true);
        
        moFieldDpsContractBase = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfDpsContractBase, jlDpsContractBase);
        moFieldDpsContractBase.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat());
        moFieldDpsContractFuture = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfDpsContractFuture, jlDpsContractFuture);
        moFieldDpsContractFuture.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat());
        moFieldDpsContractFactor = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfDpsContractFactor, jlDpsContractFactor);
        moFieldDpsContractFactor.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat());
        moFieldDpsContractFactor.setDefaultValue(0.10d);
        moFieldContractPriceReferenceNumbrer = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfContractPriceNumbrerReference, jlContractPriceNumbrerReference);
        moFieldContractPriceYear = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfContractPriceYear, jlContractPriceYear);
        moFieldContractPriceYear.setDecimalFormat(miClient.getSessionXXX().getFormatters().getYearFormat());
        moFieldContractPriceYear.setDefaultValue(SLibTimeUtilities.digestYearMonth(miClient.getSession().getCurrentDate())[0]);
        moFieldContractPriceYear.setIntegerMin(2000);
        moFieldContractPriceYear.setIntegerMax(2100);
        moFieldContractPriceMonth = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfContractPriceMonth, jlContractPriceMonth);
        moFieldContractPriceMonth.setDecimalFormat(miClient.getSessionXXX().getFormatters().getMonthFormat());
        moFieldContractPriceMonth.setDefaultValue(SLibTimeUtilities.digestYearMonth(miClient.getSession().getCurrentDate())[1]);
        moFieldContractPriceMonth.setIntegerMin(1);
        moFieldContractPriceMonth.setIntegerMax(12);
        moFieldPriceOriginalQuantity = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfPriceOriginalQuantity, jlPriceOriginalQuantity);
        moFieldPriceOriginalQuantity.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat());
        moFieldPriceOriginalPriceUnitaryCy = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfPriceOriginalPriceUnitaryCy, jlPriceOriginalPriceUnitaryCy);
        moFieldPriceOriginalPriceUnitaryCy.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormat());
        moFieldContractBase = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfContractBase, jlContractBase);
        moFieldContractBase.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat());
        moFieldContractFuture = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfContractFuture, jlContractFuture);
        moFieldContractFuture.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat());
        moFieldContractFactor = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfContractFactor, jlContractFactor);
        moFieldContractFactor.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat());
                
        moFieldFkVehicleTypeId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkVehicleTypeId_n, jlFkVehicleTypeId_n);
        moFieldDriver = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfDriver, jlDriver);
        moFieldDriver.setLengthMax(50);
        moFieldPlate = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfVehicleNumber, jlVehicleNumber);
        moFieldPlate.setLengthMax(25);
        moFieldContainerTank = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfContTank, jlContainerTank);
        moFieldContainerTank.setLengthMax(50);
        moFieldSealQuality = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfSealQuality, jlQualitySeal);
        moFieldSealQuality.setLengthMax(100);
        moFieldSealSecurity = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfSecuritySeal, jlSecuritySeal);
        moFieldSealSecurity.setLengthMax(50);
        moFieldTicket = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfTicket, jlTicket);
        moFieldTicket.setLengthMax(50);
        moFieldVgm = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfVgm, jlVgm);
        moFieldVgm.setLengthMax(15);
        moFieldFkBankAccountId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkCashAccountId_n, jlFkCashAccountId_n);
        moFieldLength = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfLength, jlLength);
        moFieldLength.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsLengthFormat());
        moFieldSurface = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfSurface, jlSurface);
        moFieldSurface.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsSurfaceFormat());
        moFieldVolume = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfVolume, jlVolume);
        moFieldVolume.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsVolumeFormat());
        moFieldMass = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfMass, jlMass);
        moFieldMass.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsMassFormat());
        moFieldWeightPackagingExtra = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfWeightPackagingExtra, jlWeightPackagingExtra);
        moFieldWeightPackagingExtra.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsWeigthGrossFormat());
        moFieldWeightGross = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfWeightGrossRo, jlWeightGross);
        moFieldWeightGross.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsWeigthGrossFormat());
        moFieldWeightDelivery = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfWeightDeliveryRo, jlWeightDelivery);
        moFieldWeightDelivery.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsWeightDeliveryFormat());
        moFieldIsPrepayment = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsPrepayment);
        moFieldIsDiscountRetailChain = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDiscountRetailChain);
        moFieldIsTaxesAutomaticApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsTaxesAutomaticApplying);
        moFieldIsInventoriable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsInventoriable);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDeleted);
        moFieldFkTaxRegionId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTaxRegionId, jlFkTaxRegionId);
        moFieldFkTaxRegionId.setPickerButton(jbFkTaxRegionId);
        moFieldFkThirdTaxCausingId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkThirdTaxCausingId_n, jlFkThirdTaxCausingId_n);
        moFieldFkThirdTaxCausingId_n.setPickerButton(jbFkThirdTaxCausingId_n);
        moFieldFkItemReferenceId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkItemReferenceId_n, jlFkItemReferenceId_n);
        moFieldFkItemReferenceId_n.setPickerButton(jbFkItemReferenceId_n);
        moFieldReference = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfReference, jlReference);
        moFieldReference.setLengthMax(25);
        moFieldOperationsType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbOperationsType, jlOperationsType);
        
        moFieldAddBachocoNúmeroPosición = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfAddBachocoNúmeroPosición, jlAddBachocoNúmeroPosición);
        moFieldAddBachocoNúmeroPosición.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddBachocoNúmeroPosición.setLengthMax(5);
        moFieldAddBachocoCentro = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddBachocoCentro, jlAddBachocoCentro);
        moFieldAddBachocoCentro.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddBachocoCentro.setLengthMax(15);
        moFieldAddLorealEntryNumber = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfAddLorealEntryNumber, jlAddLorealEntryNumber);
        moFieldAddLorealEntryNumber.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddLorealEntryNumber.setLengthMax(5);
        moFieldAddElektraOrder = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddElektraOrder, jlAddElektraOrder);
        moFieldAddElektraOrder.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddElektraOrder.setLengthMax(20);
        moFieldAddGenBarcode = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, (JTextField) jcbAddGenBarcode.getEditor().getEditorComponent(), jlAddGenBarcode);
        moFieldAddGenBarcode.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddGenBarcode.setLengthMax(20);
        moFieldAddElektraCages = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfAddElektraCages, jlAddElektraCages);
        moFieldAddElektraCages.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddElektraCagePriceUnitary = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfAddElektraCagePriceUnitary, jlAddElektraCagePriceUnitary);
        moFieldAddElektraCagePriceUnitary.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddElektraParts = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfAddElektraParts, jlAddElektraParts);
        moFieldAddElektraParts.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddElektraPartPriceUnitary = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfAddElektraPartPriceUnitary, jlAddElektraPartPriceUnitary);
        moFieldAddElektraPartPriceUnitary.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        moFieldAddAmc71PurchaseOrder = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAddAmc71PurchaseOrder, jlAddAmc71PurchaseOrder);
        moFieldAddAmc71PurchaseOrder.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        //moFieldAddAmc71PurchaseOrder.setLengthMax(?); // unlimited length
        moFieldAddAmc71PurchaseOrderDate = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftfAddAmc71PurchaseOrderDate, jlAddAmc71PurchaseOrderDate);
        moFieldAddAmc71PurchaseOrderDate.setTabbedPaneIndex(TAB_CFD_ADD, jTabbedPane);
        
        moFieldComplConceptKey = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfComplConceptKey, jlComplConceptKey);
        moFieldComplConceptKey.setLengthMax(35);
        moFieldComplConceptKey.setTabbedPaneIndex(TAB_CFD_COMPL, jTabbedPane);
        moFieldComplConcept = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfComplConcept, jlComplConcept);
        moFieldComplConcept.setLengthMax(130);
        moFieldComplConcept.setTabbedPaneIndex(TAB_CFD_COMPL, jTabbedPane);
        moFieldComplCfdProdServ = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfComplCfdProdServ, jlComplCfdProdServ);
        moFieldComplCfdProdServ.setLengthMax(8);
        moFieldComplCfdProdServ.setTabbedPaneIndex(TAB_CFD_COMPL, jTabbedPane);
        moFieldComplCfdUnit = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfComplCfdUnit, jlComplCfdUnit);
        moFieldComplCfdUnit.setLengthMax(3);
        moFieldComplCfdUnit.setTabbedPaneIndex(TAB_CFD_COMPL, jTabbedPane);

        mvFields.add(moFieldFkItemId);
        mvFields.add(moFieldConceptKey);
        mvFields.add(moFieldConcept);
        mvFields.add(moFieldFkOriginalUnitId);
        mvFields.add(moFieldIsDiscountUnitaryPercentage);
        mvFields.add(moFieldDiscountUnitaryPercentage);
        mvFields.add(moFieldIsDiscountEntryPercentage);
        mvFields.add(moFieldDiscountEntryPercentage);
        mvFields.add(moFieldIsDiscountDocApplying);
        mvFields.add(moFieldOriginalQuantity);
        mvFields.add(moFieldOriginalPriceUnitaryCy);
        mvFields.add(moFieldOriginalDiscountUnitaryCy);
        mvFields.add(moFieldSalesPriceUnitaryCy);
        mvFields.add(moFieldSalesFreightUnitaryCy);
        mvFields.add(moFieldIsSalesFreightRequired);
        mvFields.add(moFieldIsSalesFreightAddPrice);
        mvFields.add(moFieldIsSalesFreightConfirm);
        mvFields.add(moFieldDiscountEntryCy);
        mvFields.add(moFieldDiscountDocCy);
        mvFields.add(moFieldSurplusPercentage);
        
        /*
        mvFields.add(moFieldIsDpsPriceVariable);
        mvFields.add(moFieldDpsContractBase);
        mvFields.add(moFieldDpsContractFuture);
        mvFields.add(moFieldDpsContractFactor);
        mvFields.add(moFieldDpsContractExchangeRate);
        */
        
        mvFields.add(moFieldContractPriceReferenceNumbrer);
        mvFields.add(moFieldContractPriceYear);
        mvFields.add(moFieldContractPriceMonth);
        mvFields.add(moFieldPriceOriginalQuantity);
        mvFields.add(moFieldPriceOriginalPriceUnitaryCy);
        mvFields.add(moFieldContractBase);
        mvFields.add(moFieldContractFuture);
        mvFields.add(moFieldContractFactor);
        
        mvFields.add(moFieldFkVehicleTypeId_n);
        mvFields.add(moFieldDriver);
        mvFields.add(moFieldPlate);
        mvFields.add(moFieldContainerTank);
        mvFields.add(moFieldSealQuality);
        mvFields.add(moFieldSealSecurity);
        mvFields.add(moFieldTicket);
        mvFields.add(moFieldVgm);
        mvFields.add(moFieldFkBankAccountId_n);
        mvFields.add(moFieldLength);
        mvFields.add(moFieldSurface);
        mvFields.add(moFieldVolume);
        mvFields.add(moFieldMass);
        mvFields.add(moFieldWeightPackagingExtra);
        mvFields.add(moFieldWeightGross);
        mvFields.add(moFieldWeightDelivery);
        mvFields.add(moFieldIsPrepayment);
        mvFields.add(moFieldIsDiscountRetailChain);
        mvFields.add(moFieldIsTaxesAutomaticApplying);
        mvFields.add(moFieldIsInventoriable);
        mvFields.add(moFieldIsDeleted);
        mvFields.add(moFieldFkTaxRegionId);
        mvFields.add(moFieldFkThirdTaxCausingId_n);
        mvFields.add(moFieldFkItemReferenceId_n);
        mvFields.add(moFieldReference);
        mvFields.add(moFieldOperationsType);
        
        mvFields.add(moFieldAddBachocoNúmeroPosición);
        mvFields.add(moFieldAddBachocoCentro);
        mvFields.add(moFieldAddLorealEntryNumber);
        mvFields.add(moFieldAddElektraOrder);
        mvFields.add(moFieldAddGenBarcode);
        mvFields.add(moFieldAddElektraCages);
        mvFields.add(moFieldAddElektraCagePriceUnitary);
        mvFields.add(moFieldAddElektraParts);
        mvFields.add(moFieldAddElektraPartPriceUnitary);
        mvFields.add(moFieldAddAmc71PurchaseOrder);
        mvFields.add(moFieldAddAmc71PurchaseOrderDate);
        
        mvFields.add(moFieldComplConceptKey);
        mvFields.add(moFieldComplConcept);
        mvFields.add(moFieldComplCfdProdServ);
        mvFields.add(moFieldComplCfdUnit);

        // Taxes pane:

        moPaneTaxes = new STablePane(miClient);
        jpTaxes.add(moPaneTaxes, BorderLayout.CENTER);

        i = 0;
        aoTableColumns = new STableColumnForm[9];
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Impuesto", 200);
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Tasa", STableConstants.WIDTH_PERCENTAGE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Valor u.", STableConstants.WIDTH_VALUE_UNITARY);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Valor", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Monto $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Monto mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo impuesto", 150);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Cálculo impuesto", 150);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Aplicación impuesto", 150);

        for (i = 0; i < aoTableColumns.length; i++) {
            moPaneTaxes.addTableColumn(aoTableColumns[i]);
        }

        // Commissions pane:

        moPaneCommissions = new STablePane(miClient);
        jpCommissions.add(moPaneCommissions, BorderLayout.CENTER);

        i = 0;
        aoTableColumns = new STableColumnForm[6];
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Porcentaje", STableConstants.WIDTH_PERCENTAGE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Valor u.", STableConstants.WIDTH_VALUE_UNITARY);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Valor", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Monto $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Monto mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo comisión", 150);

        for (i = 0; i < aoTableColumns.length; i++) {
            moPaneCommissions.addTableColumn(aoTableColumns[i]);
        }

        // Notes pane:

        moPaneGridNotes = new STablePaneGrid(miClient);
        moPaneGridNotes.setDoubleClickAction(this, "publicActionNotesEdit");
        jpNotes.add(moPaneGridNotes, BorderLayout.CENTER);
        
        i = 0;
        aoTableColumns = new STableColumnForm[11];
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Notas", 500);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Heredable a todos los documentos", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Visible al imprimir", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Descripción concepto CFDI", STableConstants.WIDTH_BOOLEAN_2X);
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
        
        // Prices pane:
        
        moPaneGridPrices = new STablePaneGrid(miClient);
        moPaneGridPrices.setDoubleClickAction(this, "publicActionPriceEdit");
        jpPrices.add(moPaneGridPrices, BorderLayout.CENTER);
        
        i = 0;
        aoTableColumns = new STableColumnForm[10];
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "No. orden", STableConstants.WIDTH_ACCOUNT_ID);
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Año", STableConstants.WIDTH_YEAR_PERIOD);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererYear());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Mes", STableConstants.WIDTH_YEAR);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererMonth());
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Precio directo", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cantidad", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Precio u. $", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Base", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererNumberDouble());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Futuro", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererNumberDouble());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Factor ajuste", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Eliminado", STableConstants.WIDTH_BOOLEAN);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moPaneGridPrices.addTableColumn(aoTableColumns[i]);
        }
        
        // Complimentary dialogs and forms:

        moDialogPriceUnitaryWizard = new SDialogPriceUnitaryWizard(miClient);
        moDialogItemPriceHistory = new SDialogItemPriceHistory(miClient);
        moFormNotes = new SFormDpsEntryNotes(miClient);
        moFormComEntry = new SFormDpsComEntry(miClient);

        // Miscellaneous:

        try {
            moPanelFkCostCenterId_n = new SPanelAccount(miClient, SDataConstants.FIN_CC, false, false, false);
            moPanelFkCostCenterId_n.setLabelsWidth(100);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        jpCostCenter.remove(jlDummyCostCenter);
        jpCostCenter.add(moPanelFkCostCenterId_n, BorderLayout.NORTH);

        // Action listeners:

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbConceptKey.addActionListener(this);
        jbConcept.addActionListener(this);
        jbItemBizPartnerDescription.addActionListener(this);
        jbFkItemId.addActionListener(this);
        jbSetPrepayment.addActionListener(this);
        jbFkOriginalUnitId.addActionListener(this);
        jbOriginalPriceUnitaryCyWizard.addActionListener(this);
        jbPriceHistory.addActionListener(this);
        jbFkItemReferenceId_n.addActionListener(this);
        jbFkTaxRegionId.addActionListener(this);
        jbFkThirdTaxCausingId_n.addActionListener(this);
        jbEditFkThirdTaxCausingId_n.addActionListener(this);
        jbEditLogistics.addActionListener(this);
        jbNotesNew.addActionListener(this);
        jbNotesEdit.addActionListener(this);
        jbNotesDelete.addActionListener(this);
        jbPickSystemNotes.addActionListener(this);
        jbEditNotes.addActionListener(this);
        jtbNotesFilter.addActionListener(this);
        jbPriceNew.addActionListener(this);
        jbPriceSave.addActionListener(this);
        jbPriceClear.addActionListener(this);
        jbGridPriceNew.addActionListener(this);
        jbGridPriceEdit.addActionListener(this);
        jbGridPriceDelete.addActionListener(this);
        jtbGridPricesFilter.addActionListener(this);

        // Focus listeners:

        jcbFkOriginalUnitId.addFocusListener(this);
        jtfDiscountUnitaryPercentage.addFocusListener(this);
        jtfDiscountEntryPercentage.addFocusListener(this);
        jtfOriginalQuantity.addFocusListener(this);
        jtfOriginalPriceUnitaryCy.addFocusListener(this);
        jtfOriginalDiscountUnitaryCy.addFocusListener(this);
        jtfSalesPriceUnitaryCy.addFocusListener(this);
        jtfSalesFreightUnitaryCy.addFocusListener(this);
        jtfDiscountEntryCy.addFocusListener(this);
        jtfDiscountDocCy.addFocusListener(this);
        jtfWeightPackagingExtra.addFocusListener(this);
        jtfContractBase.addFocusListener(this);
        jtfContractFuture.addFocusListener(this);
        jtfContractFactor.addFocusListener(this);

        // Item listeners:

        jckIsDiscountUnitaryPercentage.addItemListener(this);
        jckIsDiscountEntryPercentage.addItemListener(this);
        jckIsDiscountDocApplying.addItemListener(this);
        jckIsSalesFreightRequired.addItemListener(this);
        jckIsSalesFreightAddPrice.addItemListener(this);
        jckIsSurplusPercentageApplying.addItemListener(this);
        jckIsTaxesAutomaticApplying.addItemListener(this);
        jcbFkItemId.addItemListener(this);
        jcbFkOriginalUnitId.addItemListener(this);
        jcbFkTaxRegionId.addItemListener(this);
        
        jckIsDpsReqMonthDelivery.addItemListener(this);
        jckIsDirectPrice.addItemListener(this);
        jckChangePrice.addItemListener(this);
        
        jradAccCashAccount.addItemListener(this);
        jradAccAdvanceBilled.addItemListener(this);
        
        jckAuxPreserveQuantity.addItemListener(this);

        SFormUtilities.createActionMap(rootPane, this, "publicPriceUnitaryCyWizard", "priceUnitaryCyWizard", KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionNotesNew", "notesNew", KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionNotesEdit", "notesEdit", KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionNotesDelete", "notesDelete", KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionNotesFilter", "notesFilter", KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK);
        
        SFormUtilities.createActionMap(rootPane, this, "publicActionPriceNew", "priceNew", KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionPriceEdit", "priceEdit", KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionPriceDelete", "priceDelete", KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionPriceFilter", "priceFilter", KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK);

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
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            if (mnFormStatus == SLibConstants.FORM_STATUS_READ_ONLY) {
                jbCancel.requestFocus();
                enablePriceContractFields(false);
            }
            else {
                if (jcbFkItemId.isEnabled()) {
                    jcbFkItemId.requestFocus();
                }
                else if (jtfOriginalPriceUnitaryCy.isFocusable() && !jtfOriginalQuantity.isFocusable()) {
                    jtfOriginalPriceUnitaryCy.requestFocus();
                }
                else if (jtfOriginalQuantity.isFocusable() ) {
                    jtfOriginalQuantity.requestFocus();
                }
                else {
                    jbCancel.requestFocus();
                }
            }
            
            if (moParamDps.isDpsTypeContract()) { // only contracts
                moFieldIsDpsPriceVariable.setBoolean(true);
                enablePriceContractFields(true);
                
                if (moParamDps.getFkCurrencyId() == SModSysConsts.CFGU_CUR_USD) {
                    moFieldDpsContractFactor.setDouble(0.10d);
                }
                else{
                    moFieldDpsContractFactor.setDouble(0.0d);
                }
                
                jTabbedPane.setEnabledAt(TAB_PRC, true);
            }
            else {
                jTabbedPane.setEnabledAt(TAB_PRC, false);
                moFieldIsDpsPriceVariable.setBoolean(false);
                moFieldDpsContractFactor.setDouble(0.0d);
                enablePriceContractFields(false);
            }

            if (moParamDps.isDocumentOrAdjustmentSal() && mbEnableDataAddenda) {
                jTabbedPane.setEnabledAt(TAB_CFD_ADD, true);
                renderAddendaData();
            }
            else {
                jTabbedPane.setEnabledAt(TAB_CFD_ADD, false);
            }
            
            if (moParamDps.isDocumentOrAdjustmentSal()) {
                jTabbedPane.setEnabledAt(TAB_CFD_COMPL, true);
            }
            else {
                jTabbedPane.setEnabledAt(TAB_CFD_COMPL, false);
            }
            
            if (moParamDps != null) {
                mbRightTrnOmitSourceDoc = moParamDps.isForSales() ? 
                    miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_OMT_DOC_SRC).HasRight :
                    miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_OMT_DOC_SRC).HasRight;
            }
        }
    }

    private boolean isTextFieldFoTotal(javax.swing.JTextField textField) {
        return textField == jtfDiscountUnitaryPercentage || textField == jtfDiscountEntryPercentage ||
                textField == jtfOriginalQuantity ||
                textField == jtfSalesPriceUnitaryCy || textField == jtfSalesFreightUnitaryCy ||
                textField == jtfOriginalPriceUnitaryCy || textField == jtfOriginalDiscountUnitaryCy ||
                textField == jtfDiscountEntryCy || textField == jtfDiscountDocCy;
    }
    
    private boolean isTextFieldForContract(javax.swing.JTextField textField) {
        return textField == jtfContractBase || textField == jtfContractFuture || textField == jtfContractFactor;
    }
    
    private boolean isCfdAddendaPosible() {
        return moParamDps != null && moParamDps.isDocumentOrAdjustmentSal() && 
                moParamBizPartner != null && moParamBizPartner.getIsCustomer() && moParamBizPartner.getDbmsCategorySettingsCus().getFkCfdAddendaTypeId() != SDataConstantsSys.BPSS_TP_CFD_ADD_NA;
    }
    
    private String composeMsgMissingShipData() {
        String message = "";

        if (SDataDpsEntry.isProvisionalValue(moFieldDriver.getString())) {
            message += moFieldDriver.getFieldName() + "\n";
        }
        if (SDataDpsEntry.isProvisionalValue(moFieldPlate.getString())) {
            message = message +  moFieldPlate.getFieldName() + "\n";
        }
        if (SDataDpsEntry.isProvisionalValue(moFieldContainerTank.getString())) {
            message = message + moFieldContainerTank.getFieldName() + "\n";
        }
        if (SDataDpsEntry.isProvisionalValue(moFieldSealQuality.getString())) {
            message = message + moFieldSealQuality.getFieldName() + "\n";
        }
        if (SDataDpsEntry.isProvisionalValue(moFieldSealSecurity.getString())) {
            message = message + moFieldSealSecurity.getFieldName() + "\n";
        }
        if (SDataDpsEntry.isProvisionalValue(moFieldTicket.getString())) {
            message = message + moFieldTicket.getFieldName() + "\n";
        }
        if (SDataDpsEntry.isProvisionalValue(moFieldVgm.getString())) {
            message = message + moFieldVgm.getFieldName() + "\n";
        }

        return message;
    }

    private void calculateTotal() {
        int i = 0;

        moDpsEntry.setIsDiscountDocApplying(moFieldIsDiscountDocApplying.getBoolean());
        moDpsEntry.setIsDiscountUnitaryPercentage(moFieldIsDiscountUnitaryPercentage.getBoolean());
        moDpsEntry.setIsDiscountUnitaryPercentageSystem(moFieldIsDiscountUnitaryPercentage.getBoolean()); // XXX
        moDpsEntry.setIsDiscountEntryPercentage(moFieldIsDiscountEntryPercentage.getBoolean());
        moDpsEntry.setDiscountUnitaryPercentage(!moDpsEntry.getIsDiscountUnitaryPercentage() ? 0 : moFieldDiscountUnitaryPercentage.getFloat());
        moDpsEntry.setDiscountUnitaryPercentageSystem(!moDpsEntry.getIsDiscountUnitaryPercentageSystem() ? 0 : moFieldDiscountUnitaryPercentage.getFloat()); // XXX
        moDpsEntry.setDiscountEntryPercentage(!moDpsEntry.getIsDiscountEntryPercentage() ? 0 : moFieldDiscountEntryPercentage.getFloat());

        moDpsEntry.setOriginalQuantity(moFieldOriginalQuantity.getDouble());
        moDpsEntry.setOriginalPriceUnitaryCy(moFieldOriginalPriceUnitaryCy.getDouble());
        moDpsEntry.setOriginalPriceUnitarySystemCy(moFieldOriginalPriceUnitaryCy.getDouble()); // XXX
        moDpsEntry.setOriginalDiscountUnitaryCy(moFieldOriginalDiscountUnitaryCy.getDouble());
        moDpsEntry.setOriginalDiscountUnitarySystemCy(moFieldOriginalDiscountUnitaryCy.getDouble()); // XXX
        
        moDpsEntry.setSalesPriceUnitaryCy(moFieldSalesPriceUnitaryCy.getDouble());
        moDpsEntry.setSalesFreightUnitaryCy(moFieldSalesFreightUnitaryCy.getDouble());
        moDpsEntry.setIsSalesFreightRequired(moFieldIsSalesFreightRequired.getBoolean());
        moDpsEntry.setIsSalesFreightConfirm(moFieldIsSalesFreightConfirm.getBoolean());
        moDpsEntry.setIsSalesFreightAdd(moFieldIsSalesFreightAddPrice.getBoolean());

        moDpsEntry.setDiscountEntryCy(moFieldDiscountEntryCy.getDouble());
        moDpsEntry.setDiscountDocCy(moFieldDiscountDocCy.getDouble());

        moDpsEntry.setIsTaxesAutomaticApplying(moFieldIsTaxesAutomaticApplying.getBoolean());

        moDpsEntry.setAuxPreserveQuantity(jckAuxPreserveQuantity.isSelected());

        if (moParamDps == null || moItem == null || moFieldFkOriginalUnitId.getKey() == null || moFieldFkTaxRegionId.getKey() == null) {
            // There is not any way to calculate DPS entry's value:

            moDpsEntry.setFkItemId(0);
            moDpsEntry.setFkUnitId(0);
            moDpsEntry.setFkOriginalUnitId(0);
            moDpsEntry.setFkTaxRegionId(0);
            moDpsEntry.setFkThirdTaxCausingId_n(0);

            moDpsEntry.setDbmsFkItemGenericId(0);

            moDpsEntry.resetValue();
            moPaneTaxes.clearTableRows();
            moPaneCommissions.clearTableRows();
        }
        else {
            // Calculate DPS entry's value:

            moDpsEntry.setFkItemId(moItem.getPkItemId());
            moDpsEntry.setFkUnitId(moItem.getFkUnitId());
            moDpsEntry.setFkOriginalUnitId(moFieldFkOriginalUnitId.getKeyAsIntArray()[0]);
            moDpsEntry.setFkTaxRegionId(moFieldFkTaxRegionId.getKeyAsIntArray()[0]);
            moDpsEntry.setFkThirdTaxCausingId_n(moFieldFkThirdTaxCausingId_n.getKeyAsIntArray()[0]);
            moDpsEntry.setIsPrepayment(moItem.getIsPrepayment());

            moDpsEntry.setDbmsFkItemGenericId(moItem.getFkItemGenericId());

            moDpsEntry.calculateTotal(miClient, moParamDps.getDate(),
                    moParamDps.getFkTaxIdentityEmisorTypeId(), moParamDps.getFkTaxIdentityReceptorTypeId(),
                    moParamDps.getIsDiscountDocPercentage(), moParamDps.getDiscountDocPercentage(), moParamDps.getExchangeRate());
            
            if (moFieldIsSalesFreightRequired.getBoolean()) {
                // original price unitary has just been updated in previous call to method SDataDpsEntry.calculateTotal(), so reflect its new value:
                moFieldOriginalPriceUnitaryCy.setDouble(moDpsEntry.getOriginalPriceUnitaryCy());
            }

            moPaneTaxes.clearTableRows();
            for (i = 0; i < moDpsEntry.getDbmsEntryTaxes().size(); i++) {
                moPaneTaxes.addTableRow(new SDataDpsEntryTaxRow(moDpsEntry.getDbmsEntryTaxes().get(i)));
            }
            moPaneTaxes.renderTableRows();
            moPaneTaxes.setTableRowSelection(0);

            moPaneCommissions.clearTableRows();
            for (i = 0; i < moDpsEntry.getDbmsEntryCommissions().size(); i++) {
                moPaneCommissions.addTableRow(new SDataDpsEntryCommissionsRow(moDpsEntry.getDbmsEntryCommissions().get(i)));
            }
            moPaneCommissions.renderTableRows();
            moPaneCommissions.setTableRowSelection(0);
        }

        if (moItem == null || mnParamAdjustmentTypeId == SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC) {
            moFieldLength.setFieldValue(0d);
            moFieldSurface.setFieldValue(0d);
            moFieldVolume.setFieldValue(0d);
            moFieldMass.setFieldValue(0d);
        }
        else {
            if (moItem.getDbmsDataItemGeneric().getIsLengthApplying() && (moFieldLength.getDouble() == 0 || (!moItem.getIsLengthVariable() && !moItem.getDbmsDataItemGeneric().getIsLengthVariable()))) {
                moFieldLength.setFieldValue(moDpsEntry.getQuantity() * moItem.getLength());
            }
            if (moItem.getDbmsDataItemGeneric().getIsSurfaceApplying() && (moFieldSurface.getDouble() == 0 || (!moItem.getIsSurfaceVariable() && !moItem.getDbmsDataItemGeneric().getIsSurfaceVariable()))) {
                moFieldSurface.setFieldValue(moDpsEntry.getQuantity() * moItem.getSurface());
            }
            if (moItem.getDbmsDataItemGeneric().getIsVolumeApplying() && (moFieldVolume.getDouble() == 0 || (!moItem.getIsVolumeVariable() && !moItem.getDbmsDataItemGeneric().getIsVolumeVariable()))) {
                moFieldVolume.setFieldValue(moDpsEntry.getQuantity() * moItem.getVolume());
            }
            if (moItem.getDbmsDataItemGeneric().getIsMassApplying() && (moFieldMass.getDouble() == 0 || (!moItem.getIsMassVariable() && !moItem.getDbmsDataItemGeneric().getIsMassVariable()))) {
                moFieldMass.setFieldValue(moDpsEntry.getQuantity() * moItem.getMass());
            }
        }

        calculateWeights();
        renderDpsEntryValue();
    }
    
    private void calculateWeights() {
        if (moItem == null || mnParamAdjustmentTypeId == SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC) {
            moFieldWeightPackagingExtra.setDouble(0d);
            
            moFieldWeightGross.setDouble(0d);
            
            moFieldWeightDelivery.setDouble(0d);
        }
        else {
            if (moItem.getDbmsDataItemGeneric().getIsWeightGrossApplying()) {
                moFieldWeightGross.setDouble(moDpsEntry.getQuantity() * moItem.getWeightGross() + moFieldWeightPackagingExtra.getDouble());
            }
            else {
                moFieldWeightGross.setDouble(0d);
            }
            
            if (moItem.getDbmsDataItemGeneric().getIsWeightDeliveryApplying()) {
                moFieldWeightDelivery.setDouble(moDpsEntry.getQuantity() * moItem.getWeightDelivery());
            }
            else {
                moFieldWeightDelivery.setDouble(0d);
            }
        }
    }

    private void calculateEntryPrice() {
        double price = 0;
        
        if (jcbFkItemId.getSelectedIndex() > 0 && jbPriceSave.isEnabled()) {
            if (!jckChangePrice.isSelected()) {
                if (!jckIsDirectPrice.isSelected()) {
                    double conversionFactor = ((SSessionCustom) miClient.getSession().getSessionCustom()).getUnitsFactorForQuantity(0, SModSysConsts.ITMU_UNIT_KG, SModSysConsts.ITMU_UNIT_LB);
                    double conversionOriginalQuantity = ((SSessionCustom) miClient.getSession().getSessionCustom()).getUnitsFactorForQuantity(moItem.getPkItemId(), SModSysConsts.ITMU_UNIT_MT_TON, ((int[]) moFieldFkOriginalUnitId.getKey())[0]);
                    price = STrnUtilities.calculateDpsEntryPriceUnitary(moFieldContractBase.getDouble(), moFieldContractFuture.getDouble(), conversionFactor, moFieldContractFactor.getDouble(), conversionOriginalQuantity);
                }
                else {
                    price = moFieldOriginalPriceUnitaryCy.getDouble();
                }
                moFieldPriceOriginalPriceUnitaryCy.setDouble(price);
            }
        }
    }

    private void renderBasicSettings() {
        SDataUnitType type;

        // local currency:
        jtfCurrencySystemKeyRo.setText(miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());

        // type of unit of measure for length:
        type = (SDataUnitType) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_TP_UNIT, new int[] { SDataConstantsSys.ITMU_TP_UNIT_LEN }, SLibConstants.EXEC_MODE_VERBOSE);
        if (type == null) {
            jtfLengthUnitSymbolRo.setText("?");
        }
        else {
            jtfLengthUnitSymbolRo.setText(type.getUnitBase());
        }

        // type of unit of measure for surface:
        type = (SDataUnitType) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_TP_UNIT, new int[] { SDataConstantsSys.ITMU_TP_UNIT_SURF }, SLibConstants.EXEC_MODE_VERBOSE);
        if (type == null) {
            jtfSurfaceUnitSymbolRo.setText("?");
        }
        else {
            jtfSurfaceUnitSymbolRo.setText(type.getUnitBase());
        }

        // type of unit of measure for volume:
        type = (SDataUnitType) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_TP_UNIT, new int[] { SDataConstantsSys.ITMU_TP_UNIT_VOL }, SLibConstants.EXEC_MODE_VERBOSE);
        if (type == null) {
            jtfVolumeUnitSymbolRo.setText("?");
        }
        else {
            jtfVolumeUnitSymbolRo.setText(type.getUnitBase());
        }

        // type of unit of measure for mass:
        type = (SDataUnitType) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_TP_UNIT, new int[] { SDataConstantsSys.ITMU_TP_UNIT_MASS }, SLibConstants.EXEC_MODE_VERBOSE);
        if (type == null) {
            jtfMassUnitSymbolRo.setText("?");
            jtfWeightPackagingExtraUnitSymbolRo.setText("?");
            jtfWeightGrossUnitSymbolRo.setText("?");
            jtfWeightDeliveryUnitSymbolRo.setText("?");
        }
        else {
            jtfMassUnitSymbolRo.setText(type.getUnitBase());
            jtfWeightPackagingExtraUnitSymbolRo.setText(type.getUnitBase());
            jtfWeightGrossUnitSymbolRo.setText(type.getUnitBase());
            jtfWeightDeliveryUnitSymbolRo.setText(type.getUnitBase());
        }
    }

    private void renderDpsEntryValue() {
        DecimalFormat formatQuantity = miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat();
        DecimalFormat formatValue = miClient.getSessionXXX().getFormatters().getDecimalsValueFormat();
        DecimalFormat formatValueUnitary = miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormat();

        jtfQuantityRo.setText(formatQuantity.format(moDpsEntry.getQuantity()));
        jtfPriceUnitaryCyRo.setText(formatValueUnitary.format(moDpsEntry.getPriceUnitaryCy()));
        jtfDiscountUnitaryCyRo.setText(formatValueUnitary.format(moDpsEntry.getDiscountUnitaryCy()));
        jtfDiscountEntryCy.setText(formatValue.format(moDpsEntry.getDiscountEntryCy()));
        jtfSubtotalProvisionalCy_rRo.setText(formatValue.format(moDpsEntry.getSubtotalProvisionalCy_r()));
        jtfDiscountDocCy.setText(formatValue.format(moDpsEntry.getDiscountDocCy()));
        jtfSubtotalCy_rRo.setText(formatValue.format(moDpsEntry.getSubtotalCy_r()));
        jtfTaxChargedCy_rRo.setText(formatValue.format(moDpsEntry.getTaxChargedCy_r()));
        jtfTaxRetainedCy_rRo.setText(formatValue.format(moDpsEntry.getTaxRetainedCy_r()));
        jtfTotalCy_rRo.setText(formatValue.format(moDpsEntry.getTotalCy_r()));
        jtfPriceUnitaryRealCy_rRo.setText(formatValueUnitary.format(moDpsEntry.getPriceUnitaryRealCy_r()));

        if (moParamDps == null || moParamDps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getFkCurrencyId()) {
            jtfPriceUnitaryRo.setText("");
            jtfDiscountUnitaryRo.setText("");
            jtfDiscountEntryRo.setText("");
            jtfSubtotalProvisional_rRo.setText("");
            jtfDiscountDocRo.setText("");
            jtfSubtotal_rRo.setText("");
            jtfTaxCharged_rRo.setText("");
            jtfTaxRetained_rRo.setText("");
            jtfTotal_rRo.setText("");
            jtfPriceUnitaryReal_rRo.setText("");
        }
        else {
            jtfPriceUnitaryRo.setText(formatValueUnitary.format(moDpsEntry.getPriceUnitary()));
            jtfDiscountUnitaryRo.setText(formatValueUnitary.format(moDpsEntry.getDiscountUnitary()));
            jtfDiscountEntryRo.setText(formatValue.format(moDpsEntry.getDiscountEntry()));
            jtfSubtotalProvisional_rRo.setText(formatValue.format(moDpsEntry.getSubtotalProvisional_r()));
            jtfDiscountDocRo.setText(formatValue.format(moDpsEntry.getDiscountDoc()));
            jtfSubtotal_rRo.setText(formatValue.format(moDpsEntry.getSubtotal_r()));
            jtfTaxCharged_rRo.setText(formatValue.format(moDpsEntry.getTaxCharged_r()));
            jtfTaxRetained_rRo.setText(formatValue.format(moDpsEntry.getTaxRetained_r()));
            jtfTotal_rRo.setText(formatValue.format(moDpsEntry.getTotal_r()));
            jtfPriceUnitaryReal_rRo.setText(formatValueUnitary.format(moDpsEntry.getPriceUnitaryReal_r()));
        }
    }
    
    private void updateItemPrice(final boolean updateItemDiscount) {
        if (moParamsItemPriceList == null || !moParamsItemPriceList.isItemPriceFound()) {
            moFieldOriginalPriceUnitaryCy.setFieldValue(0d);
            if (updateItemDiscount) {
                moFieldOriginalDiscountUnitaryCy.setFieldValue(0d);
            }
        }
        else {
            if (moParamsItemPriceList.getCurrencyId() == moParamDps.getFkCurrencyId()) {
                moFieldOriginalPriceUnitaryCy.setFieldValue(moParamsItemPriceList.getItemPrice());
                if (updateItemDiscount) {
                    moFieldOriginalDiscountUnitaryCy.setFieldValue(moParamsItemPriceList.getItemDiscount());
                }
            }
            else if (moParamsItemPriceList.getCurrencyId() == miClient.getSessionXXX().getParamsErp().getFkCurrencyId()) {
                moFieldOriginalPriceUnitaryCy.setFieldValue(moParamDps.getExchangeRate() == 0 ? 0 : moParamsItemPriceList.getItemPrice() / moParamDps.getExchangeRate());
                if (updateItemDiscount) {
                    moFieldOriginalDiscountUnitaryCy.setFieldValue(moParamDps.getExchangeRate() == 0 ? 0 : moParamsItemPriceList.getItemDiscount() / moParamDps.getExchangeRate());
                }
            }
            else {
                moFieldOriginalPriceUnitaryCy.setFieldValue(0d);
                if (updateItemDiscount) {
                    moFieldOriginalDiscountUnitaryCy.setFieldValue(0d);
                }
            }
        }
    }

    private void renderItem(boolean preserveFields, boolean recalculate) {
        mbUpdatingForm = true; // prevent from triggering of multiple item-state-changed events

        boolean isDiscRetailChain = false;
        Object keyItem = null;
        Object keyTaxRegion = null;
        Object keyOperationsType = 0;

        if (preserveFields) {
            isDiscRetailChain = moFieldIsDiscountRetailChain.getBoolean();
            keyItem = moFieldFkItemId.getKey();
            keyTaxRegion = moFieldFkTaxRegionId.getKey();
            keyOperationsType = moFieldOperationsType.getKey();
        }

        // Every time a different item is selected, form fields are cleared:

        for (int i = 0; i < mvFields.size(); i++) {
            mvFields.get(i).resetField();
        }

        // Restore values currently selected:

        if (preserveFields) {
            moFieldIsDiscountRetailChain.setFieldValue(isDiscRetailChain);
            moFieldFkItemId.setFieldValue(keyItem);
            moFieldFkTaxRegionId.setFieldValue(keyTaxRegion);
            moFieldOperationsType.setFieldValue(keyOperationsType);
        }
        
        // Reset other controls:

        moPanelFkCostCenterId_n.resetPanel();
        
        // Default values:

        moFieldIsTaxesAutomaticApplying.setFieldValue(true);

        // Render item:

        if (moItem == null) {
            // Clear original unit combo box:

            mnAuxCurrentUnitTypeId = SDataConstantsSys.UNDEFINED;
            mnAuxCurrentUnitAlternativeTypeId = SDataConstantsSys.UNDEFINED;
            
            jcbFkOriginalUnitId.removeAllItems();

            // Clear fields:

            jtConceptfKey.setToolTipText(null);
            jtfConcept.setToolTipText(null);
            jtPartNum.setToolTipText(null);
            
            jckIsBulk.setSelected(false);
            jckAuxPreserveQuantity.setSelected(false);
            jckIsSurplusPercentageApplying.setSelected(false);

            jcbAddGenBarcode.removeAllItems();
            
            // Disable fields:

            //jckIsBulk.setEnabled(false); // allways remains disabled
            //jckIsInventoriable.setEnabled(false); // allways remains disabled
            //jckIsPrepayment.setEnabled(false); // allways remains disabled
            //jckIsDiscountRetailChain.setEnabled(false);
            
            jtConceptfKey.setEditable(false);
            jtConceptfKey.setFocusable(false);
            jbConceptKey.setEnabled(false);
            jtfConcept.setEditable(false);
            jtfConcept.setFocusable(false);
            jtPartNum.setEditable(false);
            jtPartNum.setFocusable(false);
            jbConcept.setEnabled(false);
            jbItemBizPartnerDescription.setEnabled(false);
            jcbFkOriginalUnitId.setEnabled(false);
            jbFkOriginalUnitId.setEnabled(false);

            jckAuxPreserveQuantity.setEnabled(false);
            
            jckIsDiscountUnitaryPercentage.setEnabled(false);
            jckIsDiscountEntryPercentage.setEnabled(false);
            jckIsDiscountDocApplying.setEnabled(false);

            jtfOriginalQuantity.setEditable(false);
            jtfOriginalQuantity.setFocusable(false);
            jtfOriginalPriceUnitaryCy.setEditable(false);
            jtfOriginalPriceUnitaryCy.setFocusable(false);
            jtfOriginalDiscountUnitaryCy.setEditable(false);
            jtfOriginalDiscountUnitaryCy.setFocusable(false);
            jbOriginalPriceUnitaryCyWizard.setEnabled(false);
            jbPriceHistory.setEnabled(false);

            jlSalesPriceUnitaryCy.setEnabled(false);
            jtfSalesPriceUnitaryCy.setEditable(false);
            jtfSalesPriceUnitaryCy.setFocusable(false);
            jtfSalesPriceUnitaryCy.setEnabled(false); // yes, enable/disable this text field
            jtfSalesPriceUnitaryCyCurrencyKeyRo.setEnabled(false); // yes, enable/disable this text field
            jlSalesFreightUnitaryCy.setEnabled(false);
            jtfSalesFreightUnitaryCy.setEditable(false);
            jtfSalesFreightUnitaryCy.setFocusable(false);
            jtfSalesFreightUnitaryCy.setEnabled(false); // yes, enable/disable this text field
            jtfSalesFreightUnitaryCyCurrencyKeyRo.setEnabled(false); // yes, enable/disable this text field
            jckIsSalesFreightRequired.setEnabled(false);
            jckIsSalesFreightAddPrice.setEnabled(false);
            jckIsSalesFreightConfirm.setEnabled(false);
            
            jtfDiscountEntryCy.setEditable(false);
            jtfDiscountEntryCy.setFocusable(false);
            jtfDiscountDocCy.setEditable(false);
            jtfDiscountDocCy.setFocusable(false);

            jtfLength.setEnabled(false);
            jtfSurface.setEnabled(false);
            jtfVolume.setEnabled(false);
            jtfMass.setEnabled(false);
            jtfWeightPackagingExtra.setEnabled(false);

            jckIsSurplusPercentageApplying.setEnabled(false);
            jtfSurplusPercentage.setEditable(false);
            jtfSurplusPercentage.setFocusable(false);

            jcbFkItemReferenceId_n.setEnabled(false);
            jbFkItemReferenceId_n.setEnabled(false);

            jtfAddBachocoNúmeroPosición.setEnabled(false);
            jtfAddBachocoCentro.setEnabled(false);
            jtfAddLorealEntryNumber.setEnabled(false);
            jtfAddElektraOrder.setEnabled(false);
            jcbAddGenBarcode.setEnabled(false);
            jtfAddElektraCages.setEnabled(false);
            jtfAddElektraCagePriceUnitary.setEnabled(false);
            jtfAddElektraParts.setEnabled(false);
            jtfAddElektraPartPriceUnitary.setEnabled(false);
            jtfAddAmc71PurchaseOrder.setEnabled(false);
            jftfAddAmc71PurchaseOrderDate.setEnabled(false);

            moPaneTaxes.clearTableRows();
            moPaneCommissions.clearTableRows();

            moParamsItemPriceList = null;
        }
        else {
            // Item concept code and description:

            String conceptKey = "";
            String concept = "";
            String partNum = "";
            int unitId = 0;
            
            // custom item description fo business-partner:
            
            jbItemBizPartnerDescription.setEnabled(false);
            
            if (moParamBizPartner.getDbmsItemBizPartnerDescription().size() > 0) {
                for (SDataItemBizPartnerDescription description : moParamBizPartner.getDbmsItemBizPartnerDescription()) {
                    if (moItem.getPkItemId() == description.getPkItemId() && description.getIsItemDescription() && !description.getIsDeleted()) {
                        conceptKey = description.getKey();
                        concept = description.getItem().length() <= moFieldConcept.getLengthMax() ? description.getItem() : description.getItemShort();
                        unitId = description.getFkUnitId();

                        jbItemBizPartnerDescription.setEnabled(true);
                        break;
                    }
                }
            }
            
            if (concept.isEmpty()) {
                // custom item description for language:
                
                if (moParamDps.getFkLanguajeId() != miClient.getSessionXXX().getParamsErp().getFkLanguageId()) {
                    for (SDataItemForeignLanguage description : moItem.getDbmsItemForeignLanguageDescriptions()) {
                        if (moParamDps.getFkLanguajeId() == description.getPkLanguageId() && !description.getIsDeleted()) {
                            concept = description.getItem().length() <= moFieldConcept.getLengthMax() ? description.getItem() : description.getItemShort();
                            break;
                        }
                    }
                }

                if (concept.isEmpty()) {
                    // default item description from catalog:
                    
                    concept = moItem.getItem().length() <= moFieldConcept.getLengthMax() ? moItem.getItem() : moItem.getItemShort();
                    partNum = moItem.getPartNumber().length() == 0 ? "S/No.Parte" : moItem.getPartNumber();
                }
            }

            // Initialize original unit combo box:

            if (mnAuxCurrentUnitTypeId != moItem.getDbmsDataUnit().getFkUnitTypeId() || 
                    (mnAuxCurrentUnitTypeId == moItem.getDbmsDataUnit().getFkUnitTypeId() && mnAuxCurrentUnitAlternativeTypeId != moItem.getFkUnitAlternativeTypeId())) {
                mnAuxCurrentUnitTypeId = moItem.getDbmsDataUnit().getFkUnitTypeId();
                mnAuxCurrentUnitAlternativeTypeId = moItem.getFkUnitAlternativeTypeId();

                if (moItem.getFkUnitAlternativeTypeId() != SDataConstantsSys.ITMU_TP_UNIT_NA) {
                    SFormUtilities.populateComboBox(miClient, jcbFkOriginalUnitId, SDataConstants.ITMU_UNIT, new Object[] { moItem.getDbmsDataUnit().getFkUnitTypeId(), moItem.getFkUnitAlternativeTypeId() });
                }
                else {
                    SFormUtilities.populateComboBox(miClient, jcbFkOriginalUnitId, SDataConstants.ITMU_UNIT, new int[] { moItem.getDbmsDataUnit().getFkUnitTypeId() });
                }
            }

            // Initialize surplus default:

            jckIsSurplusPercentageApplying.setSelected(false);
            
            if (moItem.getSurplusPercentage() > 0) {
                jckIsSurplusPercentageApplying.setSelected(true);
                activateSurplusPercentage();
                moFieldSurplusPercentage.setFieldValue(moItem.getSurplusPercentage());
            }
            else if (moItem.getDbmsDataItemGeneric().getSurplusPercentage() > 0) {
                jckIsSurplusPercentageApplying.setSelected(true);
                activateSurplusPercentage();
                moFieldSurplusPercentage.setFieldValue(moItem.getDbmsDataItemGeneric().getSurplusPercentage());
            }

            // Initializa addenda fields:

            if (moParamDps.isDocumentOrAdjustmentSal() && mbEnableDataAddenda) {
                SFormUtilities.populateComboBox(miClient, jcbAddGenBarcode, SDataConstants.ITMU_ITEM_BARC, new int[] { moItem.getPkItemId() });
                jcbAddGenBarcode.removeItemAt(0);
                renderAddendaData();
            }
            
            // Initialize fields:

            jckIsBulk.setSelected(moItem.getIsBulk());

            moFieldConceptKey.setFieldValue(!conceptKey.isEmpty() ? conceptKey : moItem.getKey());
            moFieldConcept.setFieldValue(concept);
            moFieldFkOriginalUnitId.setFieldValue(new int[] { unitId != 0 ? unitId : moItem.getFkUnitId() });
            moFieldIsInventoriable.setFieldValue(moItem.getIsInventoriable());
            moFieldIsPrepayment.setFieldValue(moItem.getIsPrepayment());
            moFieldPartNum.setFieldValue(!partNum.isEmpty() ? partNum : "S/No.Parte");

            jtConceptfKey.setCaretPosition(0);
            jtfConcept.setCaretPosition(0);
            jtPartNum.setCaretPosition(0);

            if (!moItem.getDbmsDataItemGeneric().getIsItemKeyEditable()) {
                jtConceptfKey.setToolTipText(jtConceptfKey.getText());
            }
            if (!moItem.getDbmsDataItemGeneric().getIsItemNameEditable()) {
                jtfConcept.setToolTipText(jtfConcept.getText());
            }

            // Unitary discount settings:

            if (moItem.getIsFreeDiscountUnitary()) {
                moFieldIsDiscountUnitaryPercentage.setFieldValue(false);
                moFieldOriginalDiscountUnitaryCy.setFieldValue(0d);
            }

            // Entry discount settings:

            if (moItem.getIsFreeDiscountEntry()) {
                moFieldIsDiscountEntryPercentage.setFieldValue(false);
                moFieldDiscountEntryCy.setFieldValue(0d);
            }

            // Document discount settings:

            if (!moParamDps.getIsDiscountDocApplying() || moItem.getIsFreeDiscountDoc()) {
                moFieldIsDiscountDocApplying.setFieldValue(false);
                moFieldDiscountDocCy.setFieldValue(0d);
                jtfDiscountDocPercentageRo.setText("");
            }

            // Obtain item price and discount from price lists:

            try {
                // lookup item price by document currency or by local currency when document currency is not local currency:
                
                ArrayList<Integer> lookupCurrencies = new ArrayList<>();
                
                lookupCurrencies.add(moParamDps.getFkCurrencyId());
                
                if (moParamDps.getFkCurrencyId() != miClient.getSessionXXX().getParamsErp().getFkCurrencyId()) {
                    lookupCurrencies.add(miClient.getSession().getSessionCustom().getLocalCurrencyKey()[0]);
                }
                
                moParamsItemPriceList = null;
                
                for (Integer currency : lookupCurrencies) {
                    moParamsItemPriceList = SDataUtilities.obtainItemPrice(miClient, 
                            moParamBizPartner.getPkBizPartnerId(), moParamDps.getFkBizPartnerBranchId(),
                            (moParamDps.isForSales() ? moParamBizPartner.getDbmsCategorySettingsCus().getFkBizPartnerCategoryId() : moParamBizPartner.getDbmsCategorySettingsSup().getFkBizPartnerCategoryId()),
                            (moParamDps.isForSales() ? moParamBizPartner.getDbmsCategorySettingsCus().getFkBizPartnerTypeId() : moParamBizPartner.getDbmsCategorySettingsSup().getFkBizPartnerTypeId()),
                            moParamDps.getFkDpsCategoryId(), moParamDps.getDateDoc(), moItem.getPkItemId(), currency);
                    
                    if (moParamsItemPriceList.isItemPriceFound()) {
                        break;
                    }
                }
                
                updateItemPrice(true);
            }
            catch (Exception e) {
                SLibUtilities.printOutException(this, e);
            }
            
            // prepare setting sales freight if needed:
            
            moFieldIsSalesFreightRequired.setFieldValue(isSalesFreightRequirable());

            enableItemFields();
        }
        
        renderFieldsStatus();

        if (recalculate) {
            calculateTotal(); // actually this clears all entry's value fields
        }

        mbUpdatingForm = false;
    }

    private void renderItemBizPartnerDescription(int[] itemBizPartnerDescriptionKey) {
        moDataItemBizPartnerDescription = (SDataItemBizPartnerDescription) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_CFG_ITEM_BP, itemBizPartnerDescriptionKey, SLibConstants.EXEC_MODE_SILENT);

        moFieldConceptKey.setFieldValue(moDataItemBizPartnerDescription.getKey());
        moFieldConcept.setFieldValue(moDataItemBizPartnerDescription.getItem());
        moFieldFkOriginalUnitId.setFieldValue(new int[] { moDataItemBizPartnerDescription.getFkUnitId() });
    }

    private void renderOriginalUnitSymbol() {
        if (jcbFkOriginalUnitId.getSelectedIndex() <= 0) {
            jtfOriginalUnitSymbolRo.setText("");            
            jtfPriceOriginalUnitSymbol.setText("");
        }
        else {
            jtfOriginalUnitSymbolRo.setText((String) ((SFormComponentItem) jcbFkOriginalUnitId.getSelectedItem()).getComplement());
            jtfPriceOriginalUnitSymbol.setText((String) ((SFormComponentItem) jcbFkOriginalUnitId.getSelectedItem()).getComplement());
        }
    }

    private void renderUnitSymbol() {
        if (moItem == null) {
            jtfUnitSymbolRo.setText("");
        }
        else {
            jtfUnitSymbolRo.setText(moItem.getDbmsDataUnit().getSymbol());
        }
    }

    private void renderFieldsStatus() {
        itemStateIsDiscountUnitaryPercentage(false);
        itemStateIsDiscountEntryPercentage(false);
        itemStateIsDiscountDocApplying(false);
        itemStateIsSalesFreightRequired(false);
        itemStateIsSalesFreightAddPrice(false);
        itemStateIsSurplusPercentageApplying(false);
        itemStateIsTaxesAutomaticApplying(false);
        renderOriginalUnitSymbol();
        renderUnitSymbol();
    }
    
    private boolean isPriceEditable() {
        if (moDpsEntry.getContractPriceMonth() != 0 && moDpsEntry.getContractPriceMonth() != 0) {
            return false; // price set by contract
        }
        else if (moItem == null) {
            return false; // no item available
        }
        else {
            if (moParamDps.isForSales() && moItem.isClassSalesProduct()) {
                // sales document and a product is being sold:

                if (mbRightMktPriceListSales || mbRightSalPriceChange) {
                    return true; // user can set or edit prices
                }
                else {
                    switch (miClient.getSessionXXX().getParamsCompany().getPricePolicyForSales()) {
                        case SDataParamsCompany.PRICE_POLICY_NOT_RESTRICTED:
                            if (moParamsItemPriceList == null || (!moParamsItemPriceList.isItemPriceFound() || (moParamsItemPriceList.isItemPriceFound() && moParamsItemPriceList.getItemPrice() == 0))) {
                                return true; // no price list available or price not found or found but zero
                            }
                            else {
                                return false; // preserve price found
                            }

                        case SDataParamsCompany.PRICE_POLICY_PRICE_REQUIRED:
                            return false; // price should be set

                        default:
                    }
                }
            }
            else if (moParamDps.isForPurchases() && moItem.isClassPurchasesConsumable()) {
                // purchases document and a consumable is being purchased:

                if (mbRightMktPriceListPurchases || mbRightPurPriceChange) {
                    return true; // user can set or edit prices
                }
                else {
                    switch (miClient.getSessionXXX().getParamsCompany().getPricePolicyForPurchases()) {
                        case SDataParamsCompany.PRICE_POLICY_NOT_RESTRICTED:
                            if (moParamsItemPriceList == null || (!moParamsItemPriceList.isItemPriceFound() || (moParamsItemPriceList.isItemPriceFound() && moParamsItemPriceList.getItemPrice() == 0))) {
                                return true; // no price list available or price not found or found but zero
                            }
                            else {
                                return false; // preserve price found
                            }

                        case SDataParamsCompany.PRICE_POLICY_PRICE_REQUIRED:
                            return false; // price should be set

                        default:
                    }
                }
            }
        }
        
        return true; // otherwise price should be allways editable
    }

    private void enableItemFields() {
        if (moItem != null) {
            // Validate if user can modify unitary price and unitary discount:

            if (!isPriceEditable()) {
                mbAllowDiscount = false;

                jtfOriginalPriceUnitaryCy.setEditable(false);
                jtfOriginalPriceUnitaryCy.setFocusable(false);
                jtfOriginalDiscountUnitaryCy.setEditable(false);
                jtfOriginalDiscountUnitaryCy.setFocusable(false);
                jtfDiscountEntryCy.setEditable(false);
                jtfDiscountEntryCy.setFocusable(false);
                jtfDiscountDocCy.setEditable(false);
                jtfDiscountDocCy.setFocusable(false);

                jbOriginalPriceUnitaryCyWizard.setEnabled(false);
                jbPriceHistory.setEnabled(false);
            }
            else {
                mbAllowDiscount = true;

                jtfOriginalPriceUnitaryCy.setEditable(true);
                jtfOriginalPriceUnitaryCy.setFocusable(true);
                jtfOriginalDiscountUnitaryCy.setEditable(true);
                jtfOriginalDiscountUnitaryCy.setFocusable(true);
                jtfDiscountEntryCy.setEditable(true);
                jtfDiscountEntryCy.setFocusable(true);
                jtfDiscountDocCy.setEditable(true);
                jtfDiscountDocCy.setFocusable(true);

                jbOriginalPriceUnitaryCyWizard.setEnabled(true);
                jbPriceHistory.setEnabled(true);
            }

            boolean isSalesFreightAllowed = isSalesFreightAllowed();
            
            jckIsSalesFreightRequired.setEnabled(isSalesFreightAllowed);
            jlSalesPriceUnitaryCy.setEnabled(isSalesFreightAllowed);
            jtfSalesPriceUnitaryCy.setEnabled(isSalesFreightAllowed); // yes, enable/disable this text field
            jtfSalesPriceUnitaryCyCurrencyKeyRo.setEnabled(isSalesFreightAllowed); // yes, enable/disable this text field
            jlSalesFreightUnitaryCy.setEnabled(isSalesFreightAllowed);
            jtfSalesFreightUnitaryCy.setEnabled(isSalesFreightAllowed); // yes, enable/disable this text field
            jtfSalesFreightUnitaryCyCurrencyKeyRo.setEnabled(isSalesFreightAllowed); // yes, enable/disable this text field
            
            itemStateIsSalesFreightRequired(false);
            
            if (moItem.getIsFreeDiscountUnitary()) {
                jckIsDiscountUnitaryPercentage.setEnabled(false);
            }
            else {
                jckIsDiscountUnitaryPercentage.setEnabled(mbAllowDiscount); // XXX requires user right to be enabled
            }

            if (moItem.getIsFreeDiscountEntry()) {
                jckIsDiscountEntryPercentage.setEnabled(false);
            }
            else {
                jckIsDiscountEntryPercentage.setEnabled(mbAllowDiscount); // XXX requires user right to be enabled
            }

            if (!moParamDps.getIsDiscountDocApplying() || moItem.getIsFreeDiscountDoc()) {
                jckIsDiscountDocApplying.setEnabled(false);
                jckIsDiscountDocApplying.setSelected(false);
                jtfDiscountDocPercentageRo.setText("");
            }
            else {
                jckIsDiscountDocApplying.setEnabled(mbAllowDiscount); // XXX requires user right to be enabled
                jckIsDiscountDocApplying.setSelected(mbAllowDiscount);
                jtfDiscountDocPercentageRo.setText(moParamDps.getIsDiscountDocPercentage() ? SLibUtils.getDecimalFormatPercentageDiscount().format(moParamDps.getDiscountDocPercentage()) : "");
            }

            // Enable remaining fields:

            jtConceptfKey.setEditable(moItem.getDbmsDataItemGeneric().getIsItemKeyEditable());
            jtConceptfKey.setFocusable(moItem.getDbmsDataItemGeneric().getIsItemKeyEditable());
            jbConceptKey.setEnabled(moItem.getDbmsDataItemGeneric().getIsItemKeyEditable());
            jtfConcept.setEditable(moItem.getDbmsDataItemGeneric().getIsItemNameEditable());
            jtfConcept.setFocusable(moItem.getDbmsDataItemGeneric().getIsItemNameEditable());
            jbConcept.setEnabled(moItem.getDbmsDataItemGeneric().getIsItemNameEditable());
            jcbFkOriginalUnitId.setEnabled(true);
            jbFkOriginalUnitId.setEnabled(true);

            jtfOriginalQuantity.setEditable(true);
            jtfOriginalQuantity.setFocusable(true);
            
            jckAuxPreserveQuantity.setEnabled(moItem.getFkUnitAlternativeTypeId() != SDataConstantsSys.ITMU_TP_UNIT_NA);
            jckAuxPreserveQuantity.setSelected(jckAuxPreserveQuantity.isEnabled() && moItem.getUnitAlternativeBaseEquivalence() == 0);

            if (moItem.getDbmsDataItemGeneric().getIsItemReferenceRequired()) {
                moFieldFkItemReferenceId_n.setFieldValue(new int[] { moItem.getDbmsFkDefaultItemRefId_n() });
            }

            jcbFkItemReferenceId_n.setEnabled(moItem.getDbmsDataItemGeneric().getIsItemReferenceRequired());
            jbFkItemReferenceId_n.setEnabled(moItem.getDbmsDataItemGeneric().getIsItemReferenceRequired());

            moPanelFkCostCenterId_n.enableFields(true);

            try {
                moPanelFkCostCenterId_n.getFieldAccount().setString(SDataUtilities.obtainCostCenterItem(miClient, moItem.getPkItemId()));
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
            finally {
                moPanelFkCostCenterId_n.refreshPanel();
            }

            jtfLength.setEnabled(moItem.getDbmsDataItemGeneric().getIsLengthApplying() && (moItem.getIsLengthVariable() || moItem.getDbmsDataItemGeneric().getIsLengthVariable()));
            jtfSurface.setEnabled(moItem.getDbmsDataItemGeneric().getIsSurfaceApplying() && (moItem.getIsSurfaceVariable() || moItem.getDbmsDataItemGeneric().getIsSurfaceVariable()));
            jtfVolume.setEnabled(moItem.getDbmsDataItemGeneric().getIsVolumeApplying() && (moItem.getIsVolumeVariable() || moItem.getDbmsDataItemGeneric().getIsVolumeVariable()));
            jtfMass.setEnabled(moItem.getDbmsDataItemGeneric().getIsMassApplying() && (moItem.getIsMassVariable() || moItem.getDbmsDataItemGeneric().getIsMassVariable()));
            jtfWeightPackagingExtra.setEnabled(moItem.getDbmsDataItemGeneric().getIsWeightGrossApplying());
            
            enableAccountFields(true);
            
            jckIsSurplusPercentageApplying.setEnabled(true);
        }
    }

    private void enableAccountFields(boolean edit) {
        if (moFieldIsPrepayment.getBoolean() && moParamDps.isDocument()) {
            if (edit) {
                jradAccCashAccount.setEnabled(true);
                jradAccAdvanceBilled.setEnabled(true);
            }
            else {
                jradAccCashAccount.setEnabled(false);
                jradAccAdvanceBilled.setEnabled(false);
            }
        }
        else {
            bgAccOptions.clearSelection();
            jradAccCashAccount.setEnabled(false);
            jradAccAdvanceBilled.setEnabled(false);
        }
        
        renderAccountOptions();
    }

    private void renderAddendaData() {
        if (jbOk.isEnabled()) {
            switch(moParamBizPartner.getDbmsCategorySettingsCus().getFkCfdAddendaTypeId()) {
                case SDataConstantsSys.BPSS_TP_CFD_ADD_NA:
                    // addenda is not required
                    break;
                    
                case SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA:
                    jlAddGenBarcode.setEnabled(mbEnableDataAddenda);
                    jcbAddGenBarcode.setEnabled(mbEnableDataAddenda);
                    break;
                    
                case SDataConstantsSys.BPSS_TP_CFD_ADD_LOREAL:
                    jlAddLorealEntryNumber.setEnabled(mbEnableDataAddenda);
                    jtfAddLorealEntryNumber.setEnabled(mbEnableDataAddenda);
                    break;
                    
                case SDataConstantsSys.BPSS_TP_CFD_ADD_BACHOCO:
                    jlAddBachocoNúmeroPosición.setEnabled(mbEnableDataAddenda);
                    jtfAddBachocoNúmeroPosición.setEnabled(mbEnableDataAddenda);
                    jlAddBachocoCentro.setEnabled(mbEnableDataAddenda);
                    jtfAddBachocoCentro.setEnabled(mbEnableDataAddenda);
                    break;
                    
                case SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA:
                    jlAddElektraOrder.setEnabled(mbEnableDataAddenda);
                    jtfAddElektraOrder.setEnabled(mbEnableDataAddenda);
                    jlAddGenBarcode.setEnabled(mbEnableDataAddenda);
                    jcbAddGenBarcode.setEnabled(mbEnableDataAddenda);
                    jlAddElektraCages.setEnabled(mbEnableDataAddenda);
                    jtfAddElektraCages.setEnabled(mbEnableDataAddenda);
                    jlAddElektraCagePriceUnitary.setEnabled(mbEnableDataAddenda);
                    jtfAddElektraCagePriceUnitary.setEnabled(mbEnableDataAddenda);
                    jlAddElektraParts.setEnabled(mbEnableDataAddenda);
                    jtfAddElektraParts.setEnabled(mbEnableDataAddenda);
                    jlAddElektraPartPriceUnitary.setEnabled(mbEnableDataAddenda);
                    jtfAddElektraPartPriceUnitary.setEnabled(mbEnableDataAddenda);
                    break;
                    
                case SDataConstantsSys.BPSS_TP_CFD_ADD_AMECE71:
                    jlAddGenBarcode.setEnabled(mbEnableDataAddenda);
                    jcbAddGenBarcode.setEnabled(mbEnableDataAddenda);
                    jlAddAmc71PurchaseOrder.setEnabled(mbEnableDataAddenda);
                    jtfAddAmc71PurchaseOrder.setEnabled(mbEnableDataAddenda);
                    jlAddAmc71PurchaseOrderDate.setEnabled(mbEnableDataAddenda);
                    jftfAddAmc71PurchaseOrderDate.setEnabled(mbEnableDataAddenda);
                    break;
                    
                default:
            }
        }
    }

    private void setAddendaData() {
        switch(moParamBizPartner.getDbmsCategorySettingsCus().getFkCfdAddendaTypeId()) {
            case SDataConstantsSys.BPSS_TP_CFD_ADD_NA:
                // addenda is not required
                break;
                
            case SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA:
                jcbAddGenBarcode.setSelectedItem(moDpsEntry.getDbmsAddSorianaCodigo());
                moFieldAddGenBarcode.setFieldValue(moDpsEntry.getDbmsAddSorianaCodigo());
                break;
                
            case SDataConstantsSys.BPSS_TP_CFD_ADD_LOREAL:
                moFieldAddLorealEntryNumber.setFieldValue(moDpsEntry.getDbmsAddLorealEntryNumber());
                break;
                
            case SDataConstantsSys.BPSS_TP_CFD_ADD_BACHOCO:
                moFieldAddBachocoNúmeroPosición.setFieldValue(moDpsEntry.getDbmsAddBachocoNumeroPosicion());
                moFieldAddBachocoCentro.setFieldValue(moDpsEntry.getDbmsAddBachocoCentro());
                break;
                
            case SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA:
                moFieldAddElektraOrder.setFieldValue(moDpsEntry.getDbmsAddElektraOrder());
                jcbAddGenBarcode.setSelectedItem(moDpsEntry.getDbmsAddElektraBarcode());
                moFieldAddGenBarcode.setFieldValue(moDpsEntry.getDbmsAddElektraBarcode());
                moFieldAddElektraCages.setFieldValue(moDpsEntry.getDbmsAddElektraCages());
                moFieldAddElektraCagePriceUnitary.setFieldValue(moDpsEntry.getDbmsAddElektraCagePriceUnitary());
                moFieldAddElektraParts.setFieldValue(moDpsEntry.getDbmsAddElektraParts());
                moFieldAddElektraPartPriceUnitary.setFieldValue(moDpsEntry.getDbmsAddElektraPartPriceUnitary());
                break;
                
            case SDataConstantsSys.BPSS_TP_CFD_ADD_AMECE71:
                String amc71PurchaseOrder = "";
                Date amc71PurchaseOrderDate = null;
                String amc71Barcode = "";
                
                if (!moDpsEntry.getDbmsAddJsonData().isEmpty()) {
                    try {
                        SAddendaAmc71XmlLine amc71XmlLine = new SAddendaAmc71XmlLine();
                        amc71XmlLine.decodeJson(moDpsEntry.getDbmsAddJsonData());

                        amc71PurchaseOrder = amc71XmlLine.PurchaseOrder;
                        amc71PurchaseOrderDate = amc71XmlLine.PurchaseOrderDate;
                        amc71Barcode = amc71XmlLine.Barcode;
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                }
                
                moFieldAddGenBarcode.setFieldValue(amc71Barcode);
                moFieldAddAmc71PurchaseOrder.setFieldValue(amc71PurchaseOrder);
                moFieldAddAmc71PurchaseOrderDate.setFieldValue(amc71PurchaseOrderDate);
                break;

            default:
        }
    }

    private java.lang.String composeAddendaJsonData() {
        String json = "";
        
        if (isCfdAddendaPosible()) {
            switch(moParamBizPartner.getDbmsCategorySettingsCus().getFkCfdAddendaTypeId()) {
                case SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA:
                case SDataConstantsSys.BPSS_TP_CFD_ADD_LOREAL:
                case SDataConstantsSys.BPSS_TP_CFD_ADD_BACHOCO:
                case SDataConstantsSys.BPSS_TP_CFD_ADD_MODELO:
                case SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA:
                    break;

                case SDataConstantsSys.BPSS_TP_CFD_ADD_AMECE71:
                    SAddendaAmc71XmlLine amc71XmlLine = new SAddendaAmc71XmlLine();
                    amc71XmlLine.PurchaseOrder = moFieldAddAmc71PurchaseOrder.getString();
                    amc71XmlLine.PurchaseOrderDate = moFieldAddAmc71PurchaseOrderDate.getDate();
                    amc71XmlLine.Barcode = moFieldAddGenBarcode.getString();
                    json = amc71XmlLine.encodeJson();
                    break;

                default:
            }
        }
        
        return json;
    }

    private void renderAccountOptions() {
        if (jradAccCashAccount.isSelected() && jradAccCashAccount.isEnabled() && jradAccAdvanceBilled.isEnabled()) {
            jcbFkCashAccountId_n.setEnabled(true);
        }
        else {
            jcbFkCashAccountId_n.setEnabled(false);
            if (jcbFkCashAccountId_n.getItemCount() > 0 && !jradAccCashAccount.isSelected()) {
                jcbFkCashAccountId_n.setSelectedIndex(0);
            }
        }
    }

    private void setTaxesColumnEditable(boolean editable) {
        int index = moPaneTaxes.getTable().getSelectedRow();

        moPaneTaxes.getTableColumn(COL_TAX_CUR).setEditable(editable);
        moPaneTaxes.renderTableRows();
        if (index != -1) {
            moPaneTaxes.setTableRowSelection(index);
        }
    }

    private void updateFormEditStatus(boolean edit) {
        if (!edit) {
            mnFormStatus = SLibConstants.FORM_STATUS_READ_ONLY;

            jcbFkItemId.setEnabled(false);
            jbFkItemId.setEnabled(false);
            jbSetPrepayment.setEnabled(false);
            jtConceptfKey.setEditable(false);
            jtConceptfKey.setFocusable(false);
            jbConceptKey.setEnabled(false);
            jtfConcept.setEditable(false);
            jtfConcept.setFocusable(false);
            jbConcept.setEnabled(false);
            jtPartNum.setEditable(false);
            jtPartNum.setFocusable(false);
            jbItemBizPartnerDescription.setEnabled(false);
            jcbFkOriginalUnitId.setEnabled(false);
            jbFkOriginalUnitId.setEnabled(false);
            jckIsDeleted.setEnabled(false);

            jckIsDiscountUnitaryPercentage.setEnabled(false);
            jtfDiscountUnitaryPercentage.setEditable(false);
            jtfDiscountUnitaryPercentage.setFocusable(false);
            jckIsDiscountEntryPercentage.setEnabled(false);
            jtfDiscountEntryPercentage.setEditable(false);
            jtfDiscountEntryPercentage.setFocusable(false);
            jckIsDiscountDocApplying.setEnabled(false);

            jtfOriginalQuantity.setEditable(false);
            jtfOriginalQuantity.setFocusable(false);
            jtfOriginalPriceUnitaryCy.setEditable(false);
            jtfOriginalPriceUnitaryCy.setFocusable(false);
            jtfOriginalDiscountUnitaryCy.setEditable(false);
            jtfOriginalDiscountUnitaryCy.setFocusable(false);
            jbOriginalPriceUnitaryCyWizard.setEnabled(false);
            jbPriceHistory.setEnabled(false);
            jckAuxPreserveQuantity.setEnabled(false);
            
            boolean isSalesFreightAllowed = isSalesFreightAllowed();
            jlSalesPriceUnitaryCy.setEnabled(isSalesFreightAllowed);
            jtfSalesPriceUnitaryCy.setEditable(false);
            jtfSalesPriceUnitaryCy.setFocusable(false);
            jtfSalesPriceUnitaryCy.setEnabled(isSalesFreightAllowed); // yes, enable/disable this text field, to improve user visual experience
            jtfSalesPriceUnitaryCyCurrencyKeyRo.setEnabled(isSalesFreightAllowed); // yes, enable/disable this text field, to improve user visual experience
            jlSalesFreightUnitaryCy.setEnabled(isSalesFreightAllowed);
            jtfSalesFreightUnitaryCy.setEditable(false);
            jtfSalesFreightUnitaryCy.setFocusable(false);
            jtfSalesFreightUnitaryCy.setEnabled(isSalesFreightAllowed); // yes, enable/disable this text field, to improve user visual experience
            jtfSalesFreightUnitaryCyCurrencyKeyRo.setEnabled(isSalesFreightAllowed); // yes, enable/disable this text field, to improve user visual experience
            jckIsSalesFreightRequired.setEnabled(false);
            jckIsSalesFreightAddPrice.setEnabled(false);
            jckIsSalesFreightConfirm.setEnabled(false);

            jtfDiscountEntryCy.setEditable(false);
            jtfDiscountEntryCy.setFocusable(false);
            jtfDiscountDocCy.setEditable(false);
            jtfDiscountDocCy.setFocusable(false);

            jcbFkItemReferenceId_n.setEnabled(false);
            jbFkItemReferenceId_n.setEnabled(false);
            jtfReference.setEditable(false);
            jtfReference.setFocusable(false);

            jcbOperationsType.setEnabled(false);

            moPanelFkCostCenterId_n.enableFields(false);

            jcbFkTaxRegionId.setEnabled(false);
            jbFkTaxRegionId.setEnabled(false);
            jckIsTaxesAutomaticApplying.setEnabled(false);
            jcbFkThirdTaxCausingId_n.setEnabled(false);
            jbFkThirdTaxCausingId_n.setEnabled(false);
            jbEditFkThirdTaxCausingId_n.setEnabled(false);

            jtfLength.setEnabled(false);
            jtfSurface.setEnabled(false);
            jtfVolume.setEnabled(false);
            jtfMass.setEnabled(false);
            jtfWeightPackagingExtra.setEnabled(false);

            jckIsSurplusPercentageApplying.setEnabled(false);
            jtfSurplusPercentage.setEditable(false);
            jtfSurplusPercentage.setFocusable(false);
            
            enableAccountFields(false);
            
            jcbFkVehicleTypeId_n.setEnabled(false);
            jtfDriver.setEnabled(false);
            jtfVehicleNumber.setEditable(false);
            jtfContTank.setEditable(false);
            jtfSealQuality.setEditable(false);
            jtfSecuritySeal.setEditable(false);
            jtfTicket.setEditable(false);
            jtfVgm.setEditable(false);

            jbNotesNew.setEnabled(false);
            jbNotesEdit.setEnabled(false);
            jbNotesDelete.setEnabled(false);
            jbPickSystemNotes.setEnabled(false);

            jlAddBachocoNúmeroPosición.setEnabled(false);
            jtfAddBachocoNúmeroPosición.setEnabled(false);
            jlAddBachocoCentro.setEnabled(false);
            jtfAddBachocoCentro.setEnabled(false);
            jlAddLorealEntryNumber.setEnabled(false);
            jtfAddLorealEntryNumber.setEnabled(false);
            jlAddElektraOrder.setEnabled(false);
            jtfAddElektraOrder.setEnabled(false);
            jlAddGenBarcode.setEnabled(false);
            jcbAddGenBarcode.setEnabled(false);
            jlAddElektraCages.setEnabled(false);
            jtfAddElektraCages.setEnabled(false);
            jlAddElektraCagePriceUnitary.setEnabled(false);
            jtfAddElektraCagePriceUnitary.setEnabled(false);
            jlAddElektraParts.setEnabled(false);
            jtfAddElektraParts.setEnabled(false);
            jlAddElektraPartPriceUnitary.setEnabled(false);
            jtfAddElektraPartPriceUnitary.setEnabled(false);
            jlAddAmc71PurchaseOrder.setEnabled(false);
            jtfAddAmc71PurchaseOrder.setEnabled(false);
            jlAddAmc71PurchaseOrderDate.setEnabled(false);
            jftfAddAmc71PurchaseOrderDate.setEnabled(false);
            
            jtfComplConceptKey.setEnabled(false);
            jtfComplConcept.setEnabled(false);
            jtfComplCfdProdServ.setEnabled(false);
            jtfComplCfdUnit.setEnabled(false);

            jbOk.setEnabled(false);
            
            setTaxesColumnEditable(false);
        }
        else {
            mnFormStatus = SLibConstants.FORM_STATUS_EDIT;

            jcbFkItemId.setEnabled(true);
            jbFkItemId.setEnabled(true);
            jbSetPrepayment.setEnabled(true);
            jckIsDeleted.setEnabled(!moDpsEntry.getIsRegistryNew());

            jtfReference.setEditable(true);
            jtfReference.setFocusable(true);
            
            jcbOperationsType.setEnabled(true);

            jcbFkTaxRegionId.setEnabled(true);
            jbFkTaxRegionId.setEnabled(true);
            jckIsTaxesAutomaticApplying.setEnabled(true);
            jcbFkThirdTaxCausingId_n.setEnabled(false);
            jbFkThirdTaxCausingId_n.setEnabled(false);
            jbEditFkThirdTaxCausingId_n.setEnabled(true);

            jcbFkVehicleTypeId_n.setEnabled(true);
            jtfDriver.setEnabled(true);
            jtfVehicleNumber.setEditable(true);
            jtfContTank.setEditable(true);
            jtfSealQuality.setEditable(true);
            jtfSecuritySeal.setEditable(true);
            jtfTicket.setEditable(true);
            jtfVgm.setEditable(true);
            jbEditLogistics.setEnabled(false);
            
            jbNotesNew.setEnabled(true);
            jbNotesEdit.setEnabled(true);
            jbNotesDelete.setEnabled(true);
            jbPickSystemNotes.setEnabled(true);
            jbEditNotes.setEnabled(false);
            
            jtfComplConceptKey.setEnabled(true);
            jtfComplConcept.setEnabled(true);
            jtfComplCfdProdServ.setEnabled(true);
            jtfComplCfdUnit.setEnabled(true);

            jbOk.setEnabled(true);

            enableItemFields();
            
            renderFieldsStatus();
        }
    }

    private void updateDpsEntryTaxRow() {
        int index = moPaneTaxes.getTable().getSelectedRow();
        SDataDpsEntryTaxRow entryTaxRow = (SDataDpsEntryTaxRow) moPaneTaxes.getSelectedTableRow();
        SDataDpsEntryTax entryTax = (SDataDpsEntryTax) entryTaxRow.getData();

        entryTax.setTaxCy(entryTaxRow.getValues() == null ? 0d : ((Number) entryTaxRow.getValues().get(COL_TAX_CUR)).doubleValue());
        calculateTotal();

        moPaneTaxes.setTableRowSelection(index);
    }

    private void itemStateIsDiscountUnitaryPercentage(boolean recalculate) {
        if (moFieldIsDiscountUnitaryPercentage.getBoolean()) {
            jtfDiscountUnitaryPercentage.setEditable(mbAllowDiscount);
            jtfDiscountUnitaryPercentage.setFocusable(mbAllowDiscount);
            jtfOriginalDiscountUnitaryCy.setEditable(false);
            jtfOriginalDiscountUnitaryCy.setFocusable(false);

            jtfDiscountUnitaryPercentage.requestFocus();
        }
        else {
            jtfDiscountUnitaryPercentage.setEditable(false);
            jtfDiscountUnitaryPercentage.setFocusable(false);
            jtfOriginalDiscountUnitaryCy.setEditable(moItem == null ? false : !moItem.getIsFreeDiscountUnitary() && mbAllowDiscount);
            jtfOriginalDiscountUnitaryCy.setFocusable(moItem == null ? false : !moItem.getIsFreeDiscountUnitary() && mbAllowDiscount);

            moFieldDiscountUnitaryPercentage.setFieldValue(0d);
        }

        if (recalculate) {
            calculateTotal();
        }
    }

    private void itemStateIsDiscountEntryPercentage(boolean recalculate) {
        if (moFieldIsDiscountEntryPercentage.getBoolean()) {
            jtfDiscountEntryPercentage.setEditable(mbAllowDiscount);
            jtfDiscountEntryPercentage.setFocusable(mbAllowDiscount);
            jtfDiscountEntryCy.setEditable(false);
            jtfDiscountEntryCy.setFocusable(false);

            jtfDiscountEntryPercentage.requestFocus();
        }
        else {
            jtfDiscountEntryPercentage.setEditable(false);
            jtfDiscountEntryPercentage.setFocusable(false);
            jtfDiscountEntryCy.setEditable(moItem == null ? false : !moItem.getIsFreeDiscountEntry() && mbAllowDiscount);
            jtfDiscountEntryCy.setFocusable(moItem == null ? false : !moItem.getIsFreeDiscountEntry() && mbAllowDiscount);

            moFieldDiscountEntryPercentage.setFieldValue(0d);
        }

        if (recalculate) {
            calculateTotal();
        }
    }

    private void itemStateIsDiscountDocApplying(boolean recalculate) {
        if (moFieldIsDiscountDocApplying.getBoolean()) {
            jtfDiscountDocCy.setEditable(moParamDps == null ? false : !moParamDps.getIsDiscountDocPercentage() && mbAllowDiscount);
            jtfDiscountDocCy.setFocusable(moParamDps == null ? false : !moParamDps.getIsDiscountDocPercentage() && mbAllowDiscount);
        }
        else {
            jtfDiscountDocCy.setEditable(false);
            jtfDiscountDocCy.setFocusable(false);

            moFieldDiscountDocCy.setFieldValue(0d);
            jtfDiscountDocPercentageRo.setText("");
        }

        if (recalculate) {
            calculateTotal();
        }
    }
    
    private void itemStateIsSalesFreightRequired(boolean recalculate) {
        boolean isPriceEditable = isPriceEditable();
        
        if (moFieldIsSalesFreightRequired.getBoolean()) {
            // enable/disable related fields:
            
            jtfOriginalPriceUnitaryCy.setEditable(false);
            jtfOriginalPriceUnitaryCy.setFocusable(false);
            
            jtfSalesPriceUnitaryCy.setEditable(isPriceEditable);
            jtfSalesPriceUnitaryCy.setFocusable(isPriceEditable);
            jtfSalesFreightUnitaryCy.setEditable(true);
            jtfSalesFreightUnitaryCy.setFocusable(true);
            
            jckIsSalesFreightAddPrice.setEnabled(true);
            jckIsSalesFreightConfirm.setEnabled(true);
            
            // set suitable values:
            
            if (recalculate) {
                moFieldSalesPriceUnitaryCy.setFieldValue(moFieldOriginalPriceUnitaryCy.getDouble());
                moFieldSalesFreightUnitaryCy.setFieldValue(0d);
            }
            
            moFieldIsSalesFreightAddPrice.setFieldValue(true);
            moFieldIsSalesFreightConfirm.setFieldValue(false);
        }
        else {
            // enable/disable related fields:
            
            jtfOriginalPriceUnitaryCy.setEditable(isPriceEditable);
            jtfOriginalPriceUnitaryCy.setFocusable(isPriceEditable);
            
            jtfSalesPriceUnitaryCy.setEditable(false);
            jtfSalesPriceUnitaryCy.setFocusable(false);
            jtfSalesFreightUnitaryCy.setEditable(false);
            jtfSalesFreightUnitaryCy.setFocusable(false);
            
            jckIsSalesFreightAddPrice.setEnabled(false);
            jckIsSalesFreightConfirm.setEnabled(false);
            
            // set suitable values:
            
            if (recalculate) {
                if (moParamsItemPriceList != null && moParamsItemPriceList.isItemPriceFound()) {
                    // reset price previously found:
                    updateItemPrice(false);
                }
                else {
                    // preserve price already set:
                    moFieldOriginalPriceUnitaryCy.setFieldValue(SLibUtils.roundAmount(moFieldSalesPriceUnitaryCy.getDouble() + (moFieldIsSalesFreightAddPrice.getBoolean() ? moFieldSalesFreightUnitaryCy.getDouble() : 0d)));
                }

                moFieldSalesPriceUnitaryCy.setFieldValue(0d);
                moFieldSalesFreightUnitaryCy.setFieldValue(0d);
            }
            
            moFieldIsSalesFreightAddPrice.setFieldValue(false);
            moFieldIsSalesFreightConfirm.setFieldValue(false);
        }
    }
    
    private void itemStateIsSalesFreightAddPrice(boolean recalculate) {
        if (recalculate) {
            calculateTotal();
        }
    }

    private void itemStateIsSurplusPercentageApplying(boolean recalculate) {
        if (jckIsSurplusPercentageApplying.isSelected()) {
            jtfSurplusPercentage.setEditable(true);
            jtfSurplusPercentage.setFocusable(true);

            jtfSurplusPercentage.requestFocus();
        }
        else {
            jtfSurplusPercentage.setEditable(false);
            jtfSurplusPercentage.setFocusable(false);
        }
    }

    private void itemStateIsTaxesAutomaticApplying(boolean recalculate) {
        setTaxesColumnEditable(!jckIsTaxesAutomaticApplying.isSelected());

        if (recalculate && jckIsTaxesAutomaticApplying.isSelected()) {
            calculateTotal();
        }
    }

    private void itemChangedFkItemId(boolean recalculate) {
        if (jcbFkItemId.getSelectedIndex() <= 0) {
            moItem = null;
        }
        else {
            moItem = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, moFieldFkItemId.getKeyAsIntArray(), SLibConstants.EXEC_MODE_VERBOSE);
        }

        renderItem(true, recalculate);
    }

    private void itemChangedFkOriginalUnitId() {
        renderOriginalUnitSymbol();
        calculateTotal();
    }

    private void itemChangedFkTaxRegionId() {
        mbUpdatingForm = true;

        moFieldIsTaxesAutomaticApplying.setFieldValue(true);
        itemStateIsTaxesAutomaticApplying(true);

        mbUpdatingForm = false;
    }

    private void actionKey() {
        if (moItem != null) {
            moFieldConceptKey.setFieldValue(moItem.getKey());
            jtConceptfKey.setCaretPosition(0);
        }
    }

    private void actionConcept() {
        if (moItem != null) {
            moFieldConcept.setFieldValue(moItem.getItem());
            jtfConcept.setCaretPosition(0);
        }
    }

    private void actionItemBizPartnerDescription() {
        SFormOptionPicker picker = (SFormOptionPicker) miClient.getOptionPicker(SDataConstants.ITMU_CFG_ITEM_BP);

        picker.formReset();
        picker.setFilterKey(new int[] { moItem.getPkItemId(), moParamBizPartner.getPkBizPartnerId() });
        picker.formRefreshOptionPane();
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            renderItemBizPartnerDescription((int []) picker.getSelectedPrimaryKey());
        }
    }

    private void actionFkItemId() {
        if (miClient.pickOption(SDataConstants.ITMX_ITEM_IOG, moFieldFkItemId, manItemClassFilterKey) == SLibConstants.FORM_RESULT_OK) {
            itemChangedFkItemId(true);
        }
    }
    
    private void actionSetPrepayment() {
        int item = SLibConstants.UNDEFINED;
        
        if (moParamDps.getFkDpsCategoryId() == SDataConstantsSys.TRNS_CT_DPS_PUR) {
            item = miClient.getSessionXXX().getParamsCompany().getFkItemPrepaymentPurId_n();
        }
        else {
            item = miClient.getSessionXXX().getParamsCompany().getFkItemPrepaymentSalId_n();
        }
        
        if (item == SLibConstants.UNDEFINED) {
            miClient.showMsgBoxWarning("No se ha configurado un ítem para anticipos.");
        }
        else {
            moFieldFkItemId.setKey(new int[] { item });
        }
        
        moFieldFkItemId.getComponent().requestFocus();
    }

    private void actionFkOriginalUnitId() {
        if (miClient.pickOption(SDataConstants.ITMU_UNIT, moFieldFkOriginalUnitId, new int[] { moItem.getDbmsDataItemGeneric().getFkUnitTypeId() }) == SLibConstants.FORM_RESULT_OK) {
            itemChangedFkOriginalUnitId();
        }
    }

    private void actionPriceUnitaryCyWizard() {
        moDialogPriceUnitaryWizard.setParams(moFieldOriginalQuantity.getDouble(), jtfOriginalQuantity.isEditable(), jtfOriginalUnitSymbolRo.getText(), jtfOriginalPriceUnitaryCyCurrencyKeyRo.getText());
        moDialogPriceUnitaryWizard.setVisible(true);

        if (moDialogPriceUnitaryWizard.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldOriginalQuantity.setFieldValue(moDialogPriceUnitaryWizard.getQuantity());
            moFieldOriginalPriceUnitaryCy.setFieldValue(moDialogPriceUnitaryWizard.getPriceUnitary());
            calculateTotal(); // calculate total only if value has changed
        }

        jtfOriginalPriceUnitaryCy.requestFocus();
    }

    private void actionPriceHistory() {
        moDialogItemPriceHistory.refreshForm();
        moDialogItemPriceHistory.showPriceHistory(moParamDps.getFkDpsCategoryId() == SDataConstantsSys.TRNS_CT_DPS_PUR ? true : false,
            moFieldFkItemId.getKeyAsIntArray()[0], moParamBizPartner.getPkBizPartnerId(), new int[] { moParamDps.getFkDpsCategoryId(), moParamDps.getFkDpsClassId()});
    }

    private void actionFkItemReferenceId_n() {
        miClient.pickOption(SDataConstants.ITMX_ITEM_IOG, moFieldFkItemReferenceId_n, null);
    }

    private void actionFkTaxRegionId() {
        if (miClient.pickOption(SDataConstants.FINU_TAX_REG, moFieldFkTaxRegionId, null) == SLibConstants.FORM_RESULT_OK) {
            itemChangedFkTaxRegionId();
        }
    }
    
    private void actionFkThirdTaxCausingId_n() {
        miClient.pickOption(moParamDps.isForSales() ? SDataConstants.BPSX_BP_CUS : SDataConstants.BPSX_BP_SUP, moFieldFkThirdTaxCausingId_n, null);
    }

    private void actionEditFkThirdTaxCausingId_n() {
        jbEditFkThirdTaxCausingId_n.setEnabled(false);
        jbFkThirdTaxCausingId_n.setEnabled(true);
        jcbFkThirdTaxCausingId_n.setEnabled(true);
        jcbFkThirdTaxCausingId_n.requestFocusInWindow();
    }

    private void actionEditLogistics() {
        moFormComEntry.formReset();
        moFormComEntry.formRefreshCatalogues();
        moFormComEntry.setRegistry(moDpsEntry);
        moFormComEntry.setVisible(true);
        
        if (moFormComEntry.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            SDataDpsEntry dpsEntry = (SDataDpsEntry) moFormComEntry.getRegistry();
            moFieldFkVehicleTypeId_n.setFieldValue(dpsEntry.getFkVehicleTypeId_n());
            moFieldDriver.setFieldValue(dpsEntry.getDriver());
            moFieldPlate.setFieldValue(dpsEntry.getPlate());
            moFieldTicket.setFieldValue(dpsEntry.getTicket());
            moFieldContainerTank.setFieldValue(dpsEntry.getContainerTank());
            moFieldSealQuality.setFieldValue(dpsEntry.getSealQuality());
            moFieldSealSecurity.setFieldValue(dpsEntry.getSealSecurity());
            moFieldVgm.setFieldValue(dpsEntry.getVgm());
            
            jbOk.setEnabled(true);
            
            jcbFkVehicleTypeId_n.requestFocusInWindow();
        }
    }
    
    private void actionNotesNew() {
        if (jbNotesNew.isEnabled()) {
            moFormNotes.formReset();
            moFormNotes.setValue(SLibConstants.VALUE_POST_EMIT_EDIT, mbPostEmissionEdition);
            moFormNotes.setFormVisible(true);

            if (moFormNotes.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                moPaneGridNotes.addTableRow(new SDataDpsEntryNotesRow(moFormNotes.getRegistry()));
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
                moFormNotes.setValue(SLibConstants.VALUE_POST_EMIT_EDIT, mbPostEmissionEdition);
                moFormNotes.setRegistry((SDataDpsEntryNotes) moPaneGridNotes.getSelectedTableRow().getData());
                moFormNotes.setFormVisible(true);

                if (moFormNotes.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    moPaneGridNotes.setTableRow(new SDataDpsEntryNotesRow(moFormNotes.getRegistry()), index);
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
                    SDataDpsEntryNotes notes = (SDataDpsEntryNotes) moPaneGridNotes.getTableRow(index).getData();

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

                            moPaneGridNotes.setTableRow(new SDataDpsEntryNotesRow(notes), index);
                        }

                        moPaneGridNotes.renderTableRows();
                        moPaneGridNotes.setTableRowSelection(index < moPaneGridNotes.getTableGuiRowCount() ? index : moPaneGridNotes.getTableGuiRowCount() - 1);
                    }
                }
            }
        }
    }

    private void actionPickSystemNotes() {
        String option = miClient.pickOption(SDataConstants.TRN_SYS_NTS, new int[] { moParamDps.getFkDpsCategoryId(), moParamDps.getFkDpsClassId(), moParamDps.getFkDpsTypeId(), moParamDps.getFkCurrencyId() });

        if (!option.isEmpty()) {
            SDataDpsEntryNotes dpsEntryNotes = new SDataDpsEntryNotes();

            dpsEntryNotes.setNotes(option);
            dpsEntryNotes.setIsAllDocs(true);
            dpsEntryNotes.setIsPrintable(true);
            dpsEntryNotes.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

            // add notes:

            moPaneGridNotes.addTableRow(new SDataDpsEntryNotesRow(dpsEntryNotes));
            moPaneGridNotes.renderTableRows();
            moPaneGridNotes.setTableRowSelection(0);
        }
    }
    
    private void actionEditNotes(){
        jbNotesNew.setEnabled(true);
        jbNotesEdit.setEnabled(true);
        //jbNotesDelete.setEnabled(true); // by now, notes elimination not supported, because additional validations is needed to preserve CFD data
        jbPickSystemNotes.setEnabled(true);
        
        jbEditNotes.setEnabled(false);
        jbOk.setEnabled(true);
        
        jbNotesNew.requestFocusInWindow();
    }
    
    private void actionNotesFilter() {
        if (jtbNotesFilter.isEnabled()) {
            int index = moPaneGridNotes.getTable().getSelectedRow();

            moPaneGridNotes.setGridViewStatus(!jtbNotesFilter.isSelected() ? STableConstants.VIEW_STATUS_ALL : STableConstants.VIEW_STATUS_ALIVE);
            moPaneGridNotes.renderTableRows();
            moPaneGridNotes.setTableRowSelection(index < moPaneGridNotes.getTableGuiRowCount() ? index : moPaneGridNotes.getTableGuiRowCount() - 1);
        }
    }

    private void actionPriceNew() {
        mnAuxEntryPriceAction = STrnConsts.ACTION_NEW;
        mnAuxEntryPriceEditedIndex = -1;
        
        moAuxEntryPriceEdited = new SDataDpsEntryPrice();
        
        if (jbGridPriceNew.isEnabled() || jbPriceNew.isEnabled()) {
            enableDeliveryPriceButtonFields(false);
            enablePriceEntryContractFields(true);
            enableDeliveryPriceFields(jckIsDirectPrice.isSelected());
            enablePriceGridFields(false);
            
            moFieldPriceOriginalQuantity.setDouble(0d);
            if (!jckIsDirectPrice.isSelected()) {
                moFieldContractBase.setDouble(moFieldDpsContractBase.getDouble());
                moFieldContractFuture.setDouble(moFieldDpsContractFuture.getDouble());
                moFieldContractFactor.setDouble(moFieldDpsContractFactor.getDouble());
            }
            else {
                moFieldContractBase.setDouble(0.0d);
                moFieldContractFuture.setDouble(0.0d);
                moFieldContractFactor.setDouble(0.0d);
            }

            calculateEntryPrice();
            moAuxEntryPriceEdited.setOriginalPriceUnitaryCySystem(moFieldPriceOriginalPriceUnitaryCy.getDouble());
            jtfContractPriceNumbrerReference.requestFocus();
        }
    }

    private void actionPriceSave() {
        SDataDpsEntryPrice entryPrice = null;
        SFormValidation validation = new SFormValidation();
        
        if (jbPriceSave.isEnabled()) {
            if (moAuxEntryPriceEdited == null) {
                miClient.showMsgBoxWarning("Se debe capturar previamente un periodo de entrega."); // XXX remove if not needed!!!
            }
            else {
                // Validate if entry price can be saved:
                
                if (moFieldContractPriceReferenceNumbrer.getString().isEmpty()) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + moFieldContractPriceReferenceNumbrer.getFieldName() + "'.");
                    validation.setComponent(jtfContractPriceNumbrerReference);
                }
                else if (!moFieldContractPriceYear.validateFieldForcing()) {
                    validation.setIsError(true);
                    validation.setComponent(jtfContractPriceYear);
                }
                else if (!moFieldContractPriceMonth.validateFieldForcing()) {
                    validation.setIsError(true);
                    validation.setComponent(jtfContractPriceMonth);
                }
                else if (moFieldPriceOriginalQuantity.getDouble() < 0) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + moFieldPriceOriginalQuantity.getFieldName() + "'.");
                    validation.setComponent(jtfPriceOriginalQuantity);
                }
                else if (moFieldPriceOriginalPriceUnitaryCy.getDouble() < 0) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + moFieldPriceOriginalPriceUnitaryCy.getFieldName() + "'.");
                    validation.setComponent(jtfPriceOriginalPriceUnitaryCy);
                }
                else if (!jckIsDirectPrice.isSelected() && moFieldContractBase.getDouble() <= 0) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + moFieldContractBase.getFieldName() + "'.");
                    validation.setComponent(jtfContractBase);
                }
                else if (!jckIsDirectPrice.isSelected() && moFieldContractFuture.getDouble() <= 0) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + moFieldContractFuture.getFieldName() + "'.");
                    validation.setComponent(jtfContractFuture);
                }
                else if (!jckIsDirectPrice.isSelected() && moFieldContractFactor.getDouble() <= 0) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + moFieldContractFactor.getFieldName() + "'.");
                    validation.setComponent(jtfContractFactor);
                }
                else {
                    for (int i = 0; i < moPaneGridPrices.getGridRows().size(); i++) {
                        entryPrice = (SDataDpsEntryPrice) moPaneGridPrices.getGridRows().get(i).getData();
                        
                        if (entryPrice.getContractPriceYear() == moFieldContractPriceYear.getInteger() && entryPrice.getContractPriceMonth() == moFieldContractPriceMonth.getInteger() && !entryPrice.getIsDeleted() && entryPrice != moAuxEntryPriceEdited) {
                            validation.setMessage("Ya existe un registro para este periodo de entrega.");
                            validation.setComponent(jtfContractPriceYear);
                            break;
                        }
                    }
                }
                
                if (validation.getIsError()) {
                    if (validation.getComponent() != null) {
                        validation.getComponent().requestFocus();
                    }
                    if (!validation.getMessage().isEmpty()) {
                        miClient.showMsgBoxWarning(validation.getMessage());
                    }
                }
                else {
                    // Save entry price:
                    
                    moAuxEntryPriceEdited.setReferenceNumber(moFieldContractPriceReferenceNumbrer.getString());
                    moAuxEntryPriceEdited.setIsPriceVariable(jckIsDirectPrice.isSelected());
                    moAuxEntryPriceEdited.setContractPriceYear(moFieldContractPriceYear.getInteger());
                    moAuxEntryPriceEdited.setContractPriceMonth(moFieldContractPriceMonth.getInteger());
                    moAuxEntryPriceEdited.setOriginalQuantity(moFieldPriceOriginalQuantity.getDouble());

                    moAuxEntryPriceEdited.setOriginalPriceUnitaryCy(moFieldPriceOriginalPriceUnitaryCy.getDouble());
                    moAuxEntryPriceEdited.setContractBase(moFieldContractBase.getDouble());
                    moAuxEntryPriceEdited.setContractFuture(moFieldContractFuture.getDouble());
                    moAuxEntryPriceEdited.setContractFactor(moFieldContractFactor.getDouble());
                    moAuxEntryPriceEdited.setIsRegistryEdited(!moAuxEntryPriceEdited.getIsRegistryNew());
                    moAuxEntryPriceEdited.setIsDeleted(false);
                    
                    if (moAuxEntryPriceEdited.getIsRegistryNew()) {
                        moAuxEntryPriceEdited.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                    }
                    else {
                        moAuxEntryPriceEdited.setFkUserEditId(miClient.getSession().getUser().getFkUserTypeId());
                    }
                    
                    if (mnAuxEntryPriceAction == STrnConsts.ACTION_NEW) {
                        moPaneGridPrices.addTableRow(new SDataDpsEntryPriceRow(moAuxEntryPriceEdited));
                    }
                    else {
                        moPaneGridPrices.getTableModel().getTableRows().set(moPaneGridPrices.getTable().convertRowIndexToModel(mnAuxEntryPriceEditedIndex), new SDataDpsEntryPriceRow(moAuxEntryPriceEdited));
                    }
                    
                    moPaneGridPrices.renderTableRows();
                    moPaneGridPrices.setTableRowSelection(mnAuxEntryPriceEditedIndex < 0 ? moPaneGridPrices.getTableGuiRowCount() - 1 : mnAuxEntryPriceEditedIndex);
                    moPaneGridPrices.getTableRow(mnAuxEntryPriceEditedIndex < 0 ? moPaneGridPrices.getTableGuiRowCount() - 1 : mnAuxEntryPriceEditedIndex).prepareTableRow();
                    actionPriceClear();
                }
            }
        }
    }

    private void actionPriceClear() {
        if (jbPriceClear.isEnabled()) {
            enableDeliveryPriceButtonFields(true);
            enablePriceEntryContractFields(false);
            enableDeliveryPriceFields(false);
            enablePriceGridFields(true);
        
            moFieldContractPriceReferenceNumbrer.setString("");
            moFieldContractPriceYear.setInteger(SLibTimeUtilities.digestYearMonth(miClient.getSession().getCurrentDate())[0]);
            moFieldContractPriceMonth.setInteger(SLibTimeUtilities.digestYearMonth(miClient.getSession().getCurrentDate())[1]);
            jckIsDirectPrice.setSelected(true);
            moFieldPriceOriginalQuantity.setDouble(0d);
            jckChangePrice.setSelected(false);
            moFieldPriceOriginalPriceUnitaryCy.setDouble(0d);
            moFieldContractBase.setDouble(moFieldDpsContractBase.getDouble());
            moFieldContractFuture.setDouble(moFieldDpsContractFuture.getDouble());
            moFieldContractFactor.setDouble(moFieldDpsContractFactor.getDouble());
            if (moAuxEntryPriceEdited != null) {
                moAuxEntryPriceEdited.setIsRegistryEdited(false);
                moAuxEntryPriceEdited = null;
                mnAuxEntryPriceEditedIndex = -1;
            }
            jbPriceNew.requestFocus();
        }    
    }
    
    private void actionPriceEdit() {
        if (jbGridPriceEdit.isEnabled()) {
            mnAuxEntryPriceAction = STrnConsts.ACTION_EDIT;
            mnAuxEntryPriceEditedIndex = moPaneGridPrices.getTable().getSelectedRow();
            
            if (mnAuxEntryPriceEditedIndex != -1) {
                moAuxEntryPriceEdited = (SDataDpsEntryPrice) moPaneGridPrices.getSelectedTableRow().getData();
                
                enableDeliveryPriceButtonFields(false);
                enablePriceEntryContractFields(true);
                enableDeliveryPriceFields(true);
                enablePriceGridFields(false);

                jtfContractPriceYear.setEditable(false);
                jtfContractPriceMonth.setEditable(false);
                
                jckIsDirectPrice.setSelected(moAuxEntryPriceEdited.getIsPriceVariable());
                moFieldContractPriceReferenceNumbrer.setString(moAuxEntryPriceEdited.getReferenceNumber());
                moFieldContractPriceYear.setInteger(moAuxEntryPriceEdited.getContractPriceYear());
                moFieldContractPriceMonth.setInteger(moAuxEntryPriceEdited.getContractPriceMonth());
                moFieldPriceOriginalQuantity.setDouble(moAuxEntryPriceEdited.getOriginalQuantity());
                jckChangePrice.setSelected(moAuxEntryPriceEdited.getOriginalPriceUnitaryCy() != moAuxEntryPriceEdited.getOriginalPriceUnitaryCySystem());
                moFieldPriceOriginalPriceUnitaryCy.setDouble(moAuxEntryPriceEdited.getOriginalPriceUnitaryCy());
                moFieldContractBase.setDouble(moAuxEntryPriceEdited.getContractBase());
                moFieldContractFuture.setDouble(moAuxEntryPriceEdited.getContractFuture());
                moFieldContractFactor.setDouble(moAuxEntryPriceEdited.getContractFactor());
            }
        }
    }

    private void actionPriceDelete() {
        if (jbGridPriceDelete.isEnabled()) {
            mnAuxEntryPriceEditedIndex = moPaneGridPrices.getTable().getSelectedRow();

            if (mnAuxEntryPriceEditedIndex != -1) {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                    moAuxEntryPriceEdited = (SDataDpsEntryPrice) moPaneGridPrices.getTableRow(mnAuxEntryPriceEditedIndex).getData();

                    if (moAuxEntryPriceEdited.getIsDeleted()) {
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_REG_ALREADY_DELETE);
                    }
                    else {
                        if (moAuxEntryPriceEdited.getUserNewTs() == null) {
                            moPaneGridPrices.removeTableRow(mnAuxEntryPriceEditedIndex);
                        }
                        else {
                            moAuxEntryPriceEdited.setIsDeleted(true);
                            moAuxEntryPriceEdited.setFkUserDeleteId(miClient.getSession().getUser().getPkUserId());
                            moAuxEntryPriceEdited.setIsRegistryEdited(true);
                            moAuxEntryPriceEdited.setFkUserEditId(miClient.getSession().getUser().getPkUserId());

                            moPaneGridPrices.setTableRow(new SDataDpsEntryPriceRow(moAuxEntryPriceEdited), mnAuxEntryPriceEditedIndex);
                        }

                        moPaneGridPrices.renderTableRows();
                        moPaneGridPrices.setTableRowSelection(mnAuxEntryPriceEditedIndex < moPaneGridPrices.getTableGuiRowCount() ? mnAuxEntryPriceEditedIndex : moPaneGridPrices.getTableGuiRowCount() - 1);
                    }
                    moAuxEntryPriceEdited = null;
                }                
            }
        }
    }

    private void actionPricesFilter() {
        if (jtbGridPricesFilter.isEnabled()) {
            mnAuxEntryPriceEditedIndex = moPaneGridPrices.getTable().getSelectedRow();
            moPaneGridPrices.setGridViewStatus(!jtbGridPricesFilter.isSelected() ? STableConstants.VIEW_STATUS_ALL : STableConstants.VIEW_STATUS_ALIVE);
            moPaneGridPrices.renderTableRows();
            moPaneGridPrices.setTableRowSelection(mnAuxEntryPriceEditedIndex < moPaneGridPrices.getTableGuiRowCount() ? mnAuxEntryPriceEditedIndex : moPaneGridPrices.getTableGuiRowCount() - 1);
        }
    }

    private void actionEdit() {

    }

    private void actionOk() {
        SFormValidation validation = null;

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

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    public void publicPriceUnitaryCyWizard() {
        actionPriceUnitaryCyWizard();
    }

    public void publicPriceHistory() {
        actionPriceHistory();
    }
    
    public void publicActionPriceNew() {
        if (jTabbedPane.getSelectedIndex() == TAB_PRC) {
            actionPriceNew();
        }
    }

    public void publicActionPriceEdit() {
        if (jTabbedPane.getSelectedIndex() == TAB_PRC) {
            actionPriceEdit();
        }
    }

    public void publicActionPriceDelete() {
        if (jTabbedPane.getSelectedIndex() == TAB_PRC) {
            actionPriceDelete();
        }
    }

    public void publicActionPrcieFilter() {
        if (jTabbedPane.getSelectedIndex() == TAB_PRC) {
            jtbNotesFilter.setSelected(!jtbNotesFilter.isSelected());
        }
    }

    public void publicActionNotesNew() {
        if (jTabbedPane.getSelectedIndex() == TAB_NOT) {
            actionNotesNew();
        }
    }

    public void publicActionNotesEdit() {
        if (jTabbedPane.getSelectedIndex() == TAB_NOT) {
            actionNotesEdit();
        }
    }

    public void publicActionNotesDelete() {
        if (jTabbedPane.getSelectedIndex() == TAB_NOT) {
            actionNotesDelete();
        }
    }

    public void publicActionNotesFilter() {
        if (jTabbedPane.getSelectedIndex() == TAB_NOT) {
            jtbNotesFilter.setSelected(!jtbNotesFilter.isSelected());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgAccOptions;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel94;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbConcept;
    private javax.swing.JButton jbConceptKey;
    private javax.swing.JButton jbEditFkThirdTaxCausingId_n;
    private javax.swing.JButton jbEditLogistics;
    private javax.swing.JButton jbEditNotes;
    private javax.swing.JButton jbFkItemId;
    private javax.swing.JButton jbFkItemReferenceId_n;
    private javax.swing.JButton jbFkOriginalUnitId;
    private javax.swing.JButton jbFkTaxRegionId;
    private javax.swing.JButton jbFkThirdTaxCausingId_n;
    private javax.swing.JButton jbGridPriceDelete;
    private javax.swing.JButton jbGridPriceEdit;
    private javax.swing.JButton jbGridPriceNew;
    private javax.swing.JButton jbItemBizPartnerDescription;
    private javax.swing.JButton jbNotesDelete;
    private javax.swing.JButton jbNotesEdit;
    private javax.swing.JButton jbNotesNew;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbOriginalPriceUnitaryCyWizard;
    private javax.swing.JButton jbPickSystemNotes;
    private javax.swing.JButton jbPriceClear;
    private javax.swing.JButton jbPriceHistory;
    private javax.swing.JButton jbPriceNew;
    private javax.swing.JButton jbPriceSave;
    private javax.swing.JButton jbSetPrepayment;
    private javax.swing.JComboBox<SFormComponentItem> jcbAddGenBarcode;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCashAccountId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkItemId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkItemReferenceId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkOriginalUnitId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkTaxRegionId;
    private javax.swing.JComboBox jcbFkThirdTaxCausingId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkVehicleTypeId_n;
    private javax.swing.JComboBox jcbOperationsType;
    private javax.swing.JCheckBox jckAuxPreserveQuantity;
    private javax.swing.JCheckBox jckChangePrice;
    private javax.swing.JCheckBox jckIsBulk;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JCheckBox jckIsDirectPrice;
    private javax.swing.JCheckBox jckIsDiscountDocApplying;
    private javax.swing.JCheckBox jckIsDiscountEntryPercentage;
    private javax.swing.JCheckBox jckIsDiscountRetailChain;
    private javax.swing.JCheckBox jckIsDiscountUnitaryPercentage;
    private javax.swing.JCheckBox jckIsDpsReqMonthDelivery;
    private javax.swing.JCheckBox jckIsInventoriable;
    private javax.swing.JCheckBox jckIsPrepayment;
    private javax.swing.JCheckBox jckIsPriceConfirm;
    private javax.swing.JCheckBox jckIsSalesFreightAddPrice;
    private javax.swing.JCheckBox jckIsSalesFreightConfirm;
    private javax.swing.JCheckBox jckIsSalesFreightRequired;
    private javax.swing.JCheckBox jckIsSurplusPercentageApplying;
    private javax.swing.JCheckBox jckIsTaxesAutomaticApplying;
    private javax.swing.JFormattedTextField jftfAddAmc71PurchaseOrderDate;
    private javax.swing.JLabel jlAccOptions;
    private javax.swing.JLabel jlAddAmc71PurchaseOrder;
    private javax.swing.JLabel jlAddAmc71PurchaseOrderDate;
    private javax.swing.JLabel jlAddAmc71PurchaseOrderDateHint;
    private javax.swing.JLabel jlAddBachocoCentro;
    private javax.swing.JLabel jlAddBachocoNúmeroPosición;
    private javax.swing.JLabel jlAddCfdAddendaType;
    private javax.swing.JLabel jlAddElektraCagePriceUnitary;
    private javax.swing.JLabel jlAddElektraCages;
    private javax.swing.JLabel jlAddElektraOrder;
    private javax.swing.JLabel jlAddElektraPartPriceUnitary;
    private javax.swing.JLabel jlAddElektraParts;
    private javax.swing.JLabel jlAddGenBarcode;
    private javax.swing.JLabel jlAddLorealEntryNumber;
    private javax.swing.JLabel jlAddingMultipleMailHelp;
    private javax.swing.JLabel jlAddingMultipleMailHelp1;
    private javax.swing.JLabel jlAddingMultipleMailHelp2;
    private javax.swing.JLabel jlComplCfdProdServ;
    private javax.swing.JLabel jlComplCfdUnit;
    private javax.swing.JLabel jlComplConcept;
    private javax.swing.JLabel jlComplConceptKey;
    private javax.swing.JLabel jlConcept;
    private javax.swing.JLabel jlConceptKey;
    private javax.swing.JLabel jlContainerTank;
    private javax.swing.JLabel jlContractBase;
    private javax.swing.JLabel jlContractFactor;
    private javax.swing.JLabel jlContractFuture;
    private javax.swing.JLabel jlContractPriceMonth;
    private javax.swing.JLabel jlContractPriceNumbrerReference;
    private javax.swing.JLabel jlContractPriceYear;
    private javax.swing.JLabel jlCurrency;
    private javax.swing.JLabel jlDiscountDoc;
    private javax.swing.JLabel jlDiscountEntry;
    private javax.swing.JLabel jlDiscountUnitary;
    private javax.swing.JLabel jlDpsContractBase;
    private javax.swing.JLabel jlDpsContractFactor;
    private javax.swing.JLabel jlDpsContractFuture;
    private javax.swing.JLabel jlDriver;
    private javax.swing.JLabel jlDummyCostCenter;
    private javax.swing.JLabel jlFkCashAccountId_n;
    private javax.swing.JLabel jlFkCashAccountId_n1;
    private javax.swing.JLabel jlFkItemId;
    private javax.swing.JLabel jlFkItemReferenceId_n;
    private javax.swing.JLabel jlFkOriginalUnitId;
    private javax.swing.JLabel jlFkTaxRegionId;
    private javax.swing.JLabel jlFkThirdTaxCausingId_n;
    private javax.swing.JLabel jlFkVehicleTypeId_n;
    private javax.swing.JLabel jlIsPriceVariable;
    private javax.swing.JLabel jlLength;
    private javax.swing.JLabel jlMass;
    private javax.swing.JLabel jlOperationsType;
    private javax.swing.JLabel jlOriginalDiscountUnitaryCy;
    private javax.swing.JLabel jlOriginalPriceUnitaryCy;
    private javax.swing.JLabel jlOriginalQuantity;
    private javax.swing.JLabel jlPartNum;
    private javax.swing.JLabel jlPriceOriginalPriceUnitaryCy;
    private javax.swing.JLabel jlPriceOriginalQuantity;
    private javax.swing.JLabel jlPriceUnitary;
    private javax.swing.JLabel jlPriceUnitaryReal_r;
    private javax.swing.JLabel jlQualitySeal;
    private javax.swing.JLabel jlQuantity;
    private javax.swing.JLabel jlReference;
    private javax.swing.JLabel jlSalesFreightUnitaryCy;
    private javax.swing.JLabel jlSalesPriceUnitaryCy;
    private javax.swing.JLabel jlSecuritySeal;
    private javax.swing.JLabel jlSubtotalProvisional_r;
    private javax.swing.JLabel jlSubtotal_r;
    private javax.swing.JLabel jlSurface;
    private javax.swing.JLabel jlTaxCharged_r;
    private javax.swing.JLabel jlTaxIdentityEmisor;
    private javax.swing.JLabel jlTaxIdentityReceptor;
    private javax.swing.JLabel jlTaxRetained_r;
    private javax.swing.JLabel jlTicket;
    private javax.swing.JLabel jlTotal_r;
    private javax.swing.JLabel jlVehicleNumber;
    private javax.swing.JLabel jlVgm;
    private javax.swing.JLabel jlVolume;
    private javax.swing.JLabel jlWeightDelivery;
    private javax.swing.JLabel jlWeightGross;
    private javax.swing.JLabel jlWeightPackagingExtra;
    private javax.swing.JPanel jpCfdAddenda;
    private javax.swing.JPanel jpCfdComplement;
    private javax.swing.JPanel jpCommissions;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpCostCenter;
    private javax.swing.JPanel jpDataShip;
    private javax.swing.JPanel jpExtraDataOther;
    private javax.swing.JPanel jpExtraDataOtherFillment;
    private javax.swing.JPanel jpExtraDataOtherNorth;
    private javax.swing.JPanel jpExtraDataUnits;
    private javax.swing.JPanel jpExtraDataUnitsNorth;
    private javax.swing.JPanel jpMarketing;
    private javax.swing.JPanel jpNotes;
    private javax.swing.JPanel jpNotesControls;
    private javax.swing.JPanel jpNotesControls1;
    private javax.swing.JPanel jpPrices;
    private javax.swing.JPanel jpPricesData;
    private javax.swing.JPanel jpRegistry;
    private javax.swing.JPanel jpTaxInfo;
    private javax.swing.JPanel jpTaxes;
    private javax.swing.JRadioButton jradAccAdvanceBilled;
    private javax.swing.JRadioButton jradAccCashAccount;
    private javax.swing.JTextField jtConceptfKey;
    private javax.swing.JTextField jtPartNum;
    private javax.swing.JToggleButton jtbGridPricesFilter;
    private javax.swing.JToggleButton jtbNotesFilter;
    private javax.swing.JTextField jtfAddAmc71PurchaseOrder;
    private javax.swing.JTextField jtfAddBachocoCentro;
    private javax.swing.JTextField jtfAddBachocoNúmeroPosición;
    private javax.swing.JTextField jtfAddCfdAddendaType;
    private javax.swing.JTextField jtfAddElektraCagePriceUnitary;
    private javax.swing.JTextField jtfAddElektraCages;
    private javax.swing.JTextField jtfAddElektraOrder;
    private javax.swing.JTextField jtfAddElektraPartPriceUnitary;
    private javax.swing.JTextField jtfAddElektraParts;
    private javax.swing.JTextField jtfAddLorealEntryNumber;
    private javax.swing.JTextField jtfComplCfdProdServ;
    private javax.swing.JTextField jtfComplCfdUnit;
    private javax.swing.JTextField jtfComplConcept;
    private javax.swing.JTextField jtfComplConceptKey;
    private javax.swing.JTextField jtfConcept;
    private javax.swing.JTextField jtfContTank;
    private javax.swing.JTextField jtfContractBase;
    private javax.swing.JTextField jtfContractFactor;
    private javax.swing.JTextField jtfContractFuture;
    private javax.swing.JTextField jtfContractPriceMonth;
    private javax.swing.JTextField jtfContractPriceNumbrerReference;
    private javax.swing.JTextField jtfContractPriceYear;
    private javax.swing.JTextField jtfCurrencyKeyRo;
    private javax.swing.JTextField jtfCurrencySystemKeyRo;
    private javax.swing.JTextField jtfDiscountDocCy;
    private javax.swing.JTextField jtfDiscountDocPercentageRo;
    private javax.swing.JTextField jtfDiscountDocRo;
    private javax.swing.JTextField jtfDiscountEntryCy;
    private javax.swing.JTextField jtfDiscountEntryPercentage;
    private javax.swing.JTextField jtfDiscountEntryRo;
    private javax.swing.JTextField jtfDiscountUnitaryCyRo;
    private javax.swing.JTextField jtfDiscountUnitaryPercentage;
    private javax.swing.JTextField jtfDiscountUnitaryRo;
    private javax.swing.JTextField jtfDpsContractBase;
    private javax.swing.JTextField jtfDpsContractFactor;
    private javax.swing.JTextField jtfDpsContractFuture;
    private javax.swing.JTextField jtfDriver;
    private javax.swing.JTextField jtfLength;
    private javax.swing.JTextField jtfLengthUnitSymbolRo;
    private javax.swing.JTextField jtfMass;
    private javax.swing.JTextField jtfMassUnitSymbolRo;
    private javax.swing.JTextField jtfOriginalDiscountUnitaryCy;
    private javax.swing.JTextField jtfOriginalDiscountUnitaryCyCurrencyKeyRo;
    private javax.swing.JTextField jtfOriginalPriceUnitaryCy;
    private javax.swing.JTextField jtfOriginalPriceUnitaryCyCurrencyKeyRo;
    private javax.swing.JTextField jtfOriginalQuantity;
    private javax.swing.JTextField jtfOriginalUnitSymbolRo;
    private javax.swing.JTextField jtfPriceCurrencyKeyPriceUnitaryCy;
    private javax.swing.JTextField jtfPriceOriginalPriceUnitaryCy;
    private javax.swing.JTextField jtfPriceOriginalQuantity;
    private javax.swing.JTextField jtfPriceOriginalUnitSymbol;
    private javax.swing.JTextField jtfPriceUnitaryCyRo;
    private javax.swing.JTextField jtfPriceUnitaryRealCy_rRo;
    private javax.swing.JTextField jtfPriceUnitaryReal_rRo;
    private javax.swing.JTextField jtfPriceUnitaryRo;
    private javax.swing.JTextField jtfQuantityRo;
    private javax.swing.JTextField jtfReference;
    private javax.swing.JTextField jtfSalesFreightUnitaryCy;
    private javax.swing.JTextField jtfSalesFreightUnitaryCyCurrencyKeyRo;
    private javax.swing.JTextField jtfSalesPriceUnitaryCy;
    private javax.swing.JTextField jtfSalesPriceUnitaryCyCurrencyKeyRo;
    private javax.swing.JTextField jtfSealQuality;
    private javax.swing.JTextField jtfSecuritySeal;
    private javax.swing.JTextField jtfSubtotalCy_rRo;
    private javax.swing.JTextField jtfSubtotalProvisionalCy_rRo;
    private javax.swing.JTextField jtfSubtotalProvisional_rRo;
    private javax.swing.JTextField jtfSubtotal_rRo;
    private javax.swing.JTextField jtfSurface;
    private javax.swing.JTextField jtfSurfaceUnitSymbolRo;
    private javax.swing.JTextField jtfSurplusPercentage;
    private javax.swing.JTextField jtfTaxChargedCy_rRo;
    private javax.swing.JTextField jtfTaxCharged_rRo;
    private javax.swing.JTextField jtfTaxIdentityEmisorRo;
    private javax.swing.JTextField jtfTaxIdentityReceptorRo;
    private javax.swing.JTextField jtfTaxRetainedCy_rRo;
    private javax.swing.JTextField jtfTaxRetained_rRo;
    private javax.swing.JTextField jtfTicket;
    private javax.swing.JTextField jtfTotalCy_rRo;
    private javax.swing.JTextField jtfTotal_rRo;
    private javax.swing.JTextField jtfUnitSymbolRo;
    private javax.swing.JTextField jtfVehicleNumber;
    private javax.swing.JTextField jtfVgm;
    private javax.swing.JTextField jtfVolume;
    private javax.swing.JTextField jtfVolumeUnitSymbolRo;
    private javax.swing.JTextField jtfWeightDeliveryRo;
    private javax.swing.JTextField jtfWeightDeliveryUnitSymbolRo;
    private javax.swing.JTextField jtfWeightGrossRo;
    private javax.swing.JTextField jtfWeightGrossUnitSymbolRo;
    private javax.swing.JTextField jtfWeightPackagingExtra;
    private javax.swing.JTextField jtfWeightPackagingExtraUnitSymbolRo;
    // End of variables declaration//GEN-END:variables

    /*
     * Public methods
     */
    
    private boolean isSalesFreightAllowed() {
        return moParamDps != null && moParamDps.isForSales() && moItem != null && (moItem.getIsSalesFreightRequired() || moItem.isClassSalesProduct());
    }
    
    private boolean isSalesFreightRequirable() {
        return moParamDps != null && moParamDps.isForSales() && moItem != null && moItem.getIsSalesFreightRequired();
    }
    
    private void setEditableDependentFields(final boolean asSource) {
        jtfOriginalQuantity.setEditable(true);
        jtfOriginalQuantity.setFocusable(true);
        
        /*
        jtfOriginalPriceUnitaryCy.setEditable(false);
        jtfOriginalPriceUnitaryCy.setFocusable(false);
        jtfOriginalDiscountUnitaryCy.setEditable(false);
        jtfOriginalDiscountUnitaryCy.setFocusable(false);
        jbPriceUnitaryCyWizard.setEnabled(false);
        */
        
        /*
        boolean isSalesFreightAllowed = isSalesFreightAllowed();
        jlSalesPriceUnitaryCy.setEnabled(isSalesFreightAllowed);
        jtfSalesPriceUnitaryCy.setEditable(false);
        jtfSalesPriceUnitaryCy.setFocusable(false);
        jtfSalesPriceUnitaryCy.setEnabled(isSalesFreightAllowed); // yes, enable/disable this text field, to improve user visual experience
        jtfSalesPriceUnitaryCyCurrencyKeyRo.setEnabled(isSalesFreightAllowed); // yes, enable/disable this text field, to improve user visual experience
        jlSalesFreightUnitaryCy.setEnabled(isSalesFreightAllowed);
        jtfSalesFreightUnitaryCy.setEditable(false);
        jtfSalesFreightUnitaryCy.setFocusable(false);
        jtfSalesFreightUnitaryCy.setEnabled(isSalesFreightAllowed); // yes, enable/disable this text field, to improve user visual experience
        jtfSalesFreightUnitaryCyCurrencyKeyRo.setEnabled(isSalesFreightAllowed); // yes, enable/disable this text field, to improve user visual experience
        jckIsSalesFreightRequired.setEnabled(false);
        jckIsSalesFreightAddPrice.setEnabled(false);
        jckIsSalesFreightConfirm.setEnabled(false);
        */
        
        /*
        jtfDiscountEntryCy.setEditable(false);
        jtfDiscountEntryCy.setFocusable(false);
        jtfDiscountDocCy.setEditable(false);
        jtfDiscountDocCy.setFocusable(false);
        */

        jcbFkItemReferenceId_n.setEnabled(false);
        jbFkItemReferenceId_n.setEnabled(false);
        moPanelFkCostCenterId_n.enableFields(true);

        /*
        jcbFkTaxRegionId.setEnabled(false);
        jbFkTaxRegionId.setEnabled(false);
        jckIsTaxesAutomaticApplying.setEnabled(false);
        */

        jtfLength.setEnabled(false);
        jtfSurface.setEnabled(false);
        jtfVolume.setEnabled(false);
        jtfMass.setEnabled(false);
        jtfWeightPackagingExtra.setEnabled(moItem != null && moItem.getDbmsDataItemGeneric().getIsWeightGrossApplying());
    }

    public void enableDpsLinkedEditableFields(final boolean asSource) {
        jcbFkItemId.setEnabled(false);
        jbFkItemId.setEnabled(false);
        jbSetPrepayment.setEnabled(false);
        
        jtConceptfKey.setEditable(false);
        jtConceptfKey.setFocusable(false);
        jbConceptKey.setEnabled(false);
        jtfConcept.setEditable(false);
        jtfConcept.setFocusable(false);
        jbConcept.setEnabled(false);
        
        jcbFkOriginalUnitId.setEnabled(false);
        jbFkOriginalUnitId.setEnabled(false);
        jckIsDeleted.setEnabled(false);

        jckIsDiscountUnitaryPercentage.setEnabled(false);
        jtfDiscountUnitaryPercentage.setEditable(false);
        jtfDiscountUnitaryPercentage.setFocusable(false);
        jckIsDiscountEntryPercentage.setEnabled(false);
        jtfDiscountEntryPercentage.setEditable(false);
        jtfDiscountEntryPercentage.setFocusable(false);
        jckIsDiscountDocApplying.setEnabled(false);
        
        setEditableDependentFields(asSource);
    }

    public void enableDpsAdjustedEditableFields() {
        jcbFkItemId.setEnabled(false);
        jbFkItemId.setEnabled(false);
        jbSetPrepayment.setEnabled(false);
        
        /*
        jtConceptfKey.setEditable(false);
        jtConceptfKey.setFocusable(false);
        jbConceptKey.setEnabled(false);
        jtfConcept.setEditable(false);
        jtfConcept.setFocusable(false);
        jbConcept.setEnabled(false);
        */
        
        jcbFkOriginalUnitId.setEnabled(false);
        jbFkOriginalUnitId.setEnabled(false);
        jckIsDeleted.setEnabled(false);

        /*
        jckIsDiscountUnitaryPercentage.setEnabled(false);
        jtfDiscountUnitaryPercentage.setEditable(false);
        jtfDiscountUnitaryPercentage.setFocusable(false);
        jckIsDiscountEntryPercentage.setEnabled(false);
        jtfDiscountEntryPercentage.setEditable(false);
        jtfDiscountEntryPercentage.setFocusable(false);
        jckIsDiscountDocApplying.setEnabled(false);
        */

        setEditableDependentFields(false);
    }
    
    public void enablePriceContractFields (boolean enabled) {
        boolean isFormEdit = mnFormStatus != SLibConstants.FORM_STATUS_READ_ONLY;
        boolean isCurUsd = ((moParamDps != null) && (moParamDps.getFkCurrencyId() == SModSysConsts.CFGU_CUR_USD));
        boolean isDpsReqMonthDelivery = jckIsDpsReqMonthDelivery.isSelected();
        
        jckIsPriceConfirm.setEnabled(isFormEdit && enabled && isDpsReqMonthDelivery);
        jckIsPriceConfirm.setFocusable(isFormEdit && enabled && isDpsReqMonthDelivery);
        jtfDpsContractBase.setEditable(isFormEdit && enabled && isCurUsd && isDpsReqMonthDelivery);
        jtfDpsContractBase.setFocusable(isFormEdit && enabled && isCurUsd && isDpsReqMonthDelivery);
        jtfDpsContractFuture.setEditable(isFormEdit && enabled && isCurUsd && isDpsReqMonthDelivery);
        jtfDpsContractFuture.setFocusable(isFormEdit && enabled && isCurUsd && isDpsReqMonthDelivery);
        
        jckIsDirectPrice.setEnabled(isFormEdit && enabled && isCurUsd);
        
        enablePriceEntryContractFields(false);
        enableDeliveryPriceFields(false);
        enablePriceGridFields(enabled);
        enableDeliveryPriceButtonFields(enabled);
    }
    
    public void enablePriceEntryContractFields (boolean enabled) {
        boolean isFormEdit = mnFormStatus != SLibConstants.FORM_STATUS_READ_ONLY;
        boolean isCurUsd = ((moParamDps != null) && (moParamDps.getFkCurrencyId() == SModSysConsts.CFGU_CUR_USD));
        boolean isDpsReqMonthDelivery = jckIsDpsReqMonthDelivery.isSelected();
        
        jckIsDirectPrice.setEnabled(isCurUsd && isFormEdit && !enabled && isDpsReqMonthDelivery);
        jtfContractPriceNumbrerReference.setEditable(isFormEdit && enabled && isDpsReqMonthDelivery);
        jtfContractPriceYear.setEditable(isFormEdit && enabled && isDpsReqMonthDelivery);
        jtfContractPriceMonth.setEditable(isFormEdit && enabled && isDpsReqMonthDelivery);
        jtfPriceOriginalQuantity.setEditable(isFormEdit && enabled && isDpsReqMonthDelivery);
        jtfContractPriceNumbrerReference.setFocusable(isFormEdit && enabled && isDpsReqMonthDelivery);
        jtfContractPriceYear.setFocusable(isFormEdit && enabled && isDpsReqMonthDelivery);
        jtfContractPriceMonth.setFocusable(isFormEdit && enabled && isDpsReqMonthDelivery);
        jtfPriceOriginalQuantity.setFocusable(isFormEdit && enabled && isDpsReqMonthDelivery);
    }
    
    public void enableDeliveryPriceFields(boolean enabled) {
        boolean bCurUSD = ((moParamDps != null) && (moParamDps.getFkCurrencyId() == SModSysConsts.CFGU_CUR_USD));
        boolean isDpsReqMonthDelivery = jckIsDpsReqMonthDelivery.isSelected();
        boolean isSaveButtonEnabled = jbPriceSave.isEnabled();
        boolean isFormEdit = mnFormStatus != SLibConstants.FORM_STATUS_READ_ONLY;
        
        jckChangePrice.setEnabled(isDpsReqMonthDelivery && isSaveButtonEnabled);
        
        jtfContractBase.setEditable(isFormEdit && bCurUSD && isDpsReqMonthDelivery && isSaveButtonEnabled && !enabled);
        jtfContractBase.setFocusable(isFormEdit && bCurUSD && isDpsReqMonthDelivery && isSaveButtonEnabled && !enabled);
        jtfContractFuture.setEditable(isFormEdit && bCurUSD && isDpsReqMonthDelivery && isSaveButtonEnabled && !enabled);
        jtfContractFuture.setFocusable(isFormEdit && bCurUSD && isDpsReqMonthDelivery && isSaveButtonEnabled && !enabled);
    }
    
    public void enableDeliveryPriceButtonFields(boolean enabled) {
        boolean isDpsReqMonthDelivery = jckIsDpsReqMonthDelivery.isSelected();
        boolean isFormEdit = mnFormStatus != SLibConstants.FORM_STATUS_READ_ONLY;
        
        jbPriceNew.setEnabled(isFormEdit && enabled && isDpsReqMonthDelivery);
        jbPriceSave.setEnabled(isFormEdit && !enabled && isDpsReqMonthDelivery);
        jbPriceClear.setEnabled(isFormEdit && !enabled && isDpsReqMonthDelivery);
    }
    
    public void enableChangeEntryPrice(boolean enabled) {
        boolean isDpsReqMonthDelivery = jckIsDpsReqMonthDelivery.isSelected();
        boolean isFormEdit = mnFormStatus != SLibConstants.FORM_STATUS_READ_ONLY;
        
        jtfPriceOriginalPriceUnitaryCy.setEditable(isFormEdit && enabled && isDpsReqMonthDelivery);
        jtfPriceOriginalPriceUnitaryCy.setFocusable(isFormEdit && enabled && isDpsReqMonthDelivery);
        
        calculateEntryPrice();
    }
    
    public void enablePriceGridFields(boolean enabled) {
        boolean isFormEdit = mnFormStatus != SLibConstants.FORM_STATUS_READ_ONLY;
        
        jbGridPriceNew.setEnabled(isFormEdit && false);
        jbGridPriceEdit.setEnabled(isFormEdit && enabled);
        jbGridPriceDelete.setEnabled(isFormEdit && enabled);
        jtbGridPricesFilter.setEnabled(isFormEdit && enabled);
        moPaneGridPrices.setEnabled(isFormEdit && enabled);
    }
    
    public void setQuantityLimit(double dQuantityDes, double dQuantityAdj, double dQuantityPrc, boolean isLastPrc) {
        Vector<SDataDpsDpsLink> vDbmsDpsLinksAsSource = null;
        Vector<SDataDpsDpsLink> vDbmsDpsLinksAsDestiny = null;
        Vector<SDataDpsDpsAdjustment> vDbmsDpsAdjustmentsAsDps = null;
        Vector<SDataDpsDpsAdjustment> vDbmsDpsAdjustmentsAsAdjustment = null;
        
        mdQuantityPrc = dQuantityPrc;
        mbIsLastPrc = isLastPrc;

        if (SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_DIOG, moDpsEntry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
            mdQuantitySrcOrig = moDpsEntry.getOriginalQuantity();
        }

        if (SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_SHIP, moDpsEntry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
            mdQuantitySrcOrig = moDpsEntry.getOriginalQuantity();
        }

        if (moDpsEntry.hasDpsLinksAsSource()) {
            vDbmsDpsLinksAsSource = moDpsEntry.getDbmsDpsLinksAsSource();

            for (SDataDpsDpsLink link : vDbmsDpsLinksAsSource) {
                try {
                    mdQuantitySrcOrig = STrnUtilities.obtainQuantityLimit(miClient, SDataConstants.TRN_DPS_DPS_SUPPLY, link.getDbmsSourceDpsEntryKey(), null);
                }
                catch (java.lang.Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }

        if (moDpsEntry.hasDpsLinksAsDestiny()) {
            vDbmsDpsLinksAsDestiny = moDpsEntry.getDbmsDpsLinksAsDestiny();

            for (SDataDpsDpsLink link : vDbmsDpsLinksAsDestiny) {
                try {
                    mdQuantityDesOrig = STrnUtilities.obtainQuantityLimit(miClient, SDataConstants.TRN_DPS_DPS_SUPPLY, link.getDbmsSourceDpsEntryKey(), link.getDbmsDestinyDpsEntryKey()) - dQuantityDes;
                }
                catch (java.lang.Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }

        if (mdQuantitySrcOrig == 0 && (moDpsEntry.hasDpsAdjustmentsAsDoc() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_ADJ_DOC, moDpsEntry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0)) {
            vDbmsDpsAdjustmentsAsDps = moDpsEntry.getDbmsDpsAdjustmentsAsDps();

            for (SDataDpsDpsAdjustment adjustment : vDbmsDpsAdjustmentsAsDps) {
                try {
                    mdQuantitySrcOrig = STrnUtilities.obtainQuantityLimit(miClient, SDataConstants.TRN_DPS_DPS_ADJ, adjustment.getDbmsDpsEntryKey(), null);
                }
                catch (java.lang.Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }

        if (moDpsEntry.hasDpsAdjustmentsAsAdjustment() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DPS_ETY_COUNT_ADJ_ADJ, moDpsEntry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
            vDbmsDpsAdjustmentsAsAdjustment = moDpsEntry.getDbmsDpsAdjustmentsAsAdjustment();

            for (SDataDpsDpsAdjustment adjustment : vDbmsDpsAdjustmentsAsAdjustment) {
                try {
                    mdQuantityDesOrig = STrnUtilities.obtainQuantityLimit(miClient, SDataConstants.TRN_DPS_DPS_ADJ, adjustment.getDbmsDpsEntryKey(), adjustment.getDbmsDpsAdjustmentEntryKey()) - dQuantityAdj;
                }
                catch (java.lang.Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    public void setEnableDataAddenda(boolean b) {
        mbEnableDataAddenda = b;
    }

    public void activateSurplusPercentage() {
        jckIsSurplusPercentageApplying.setEnabled(true);
        itemStateIsSurplusPercentageApplying(false);
    }

    public void activateDiscountRetailChain() {
        moFieldIsDiscountRetailChain.setFieldValue(true);
    }

    /*
     * Overriden methods
     */

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formReset() {
        mbResetingForm = true;
        mbRightTrnOmitSourceDoc = false;
        mbAllowDiscount = true;
        
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        moDpsEntry = new SDataDpsEntry();
        moDpsEntry.setFkDpsAdjustmentTypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[0]);
        moDpsEntry.setFkDpsAdjustmentSubtypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[1]);
        moDpsEntry.setDbmsDpsAdjustmentType(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_TP_DPS_ADJ, new int[] { moDpsEntry.getFkDpsAdjustmentTypeId() }));
        moDpsEntry.setFkDpsEntryTypeId(SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY);
        moDpsEntry.setDbmsDpsEntryType(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_TP_DPS_ETY, new int[] { moDpsEntry.getFkDpsEntryTypeId() }));

        moItem = null;
        moParamDps = null;
        moParamBizPartner = null;
        moParamBizPartnerBranch = null;
        moDataItemBizPartnerDescription = null;
        mnParamAdjustmentTypeId = SDataConstantsSys.UNDEFINED;

        manItemClassFilterKey = null;

        mnAuxCurrentUnitTypeId = SLibConstants.UNDEFINED;
        mnAuxCurrentUnitAlternativeTypeId = SLibConstants.UNDEFINED;
        mdTempCurrentValue = 0; // clear temporal current-value
        moTempCurrentUnitKey = null; // clear temporal current-unit-key

        moPaneTaxes.createTable();
        moPaneTaxes.clearTableRows();
        
        moPaneCommissions.createTable();
        moPaneCommissions.clearTableRows();

        moPaneGridNotes.createTable();
        moPaneGridNotes.clearTableRows();
        moPaneGridNotes.setGridViewStatus(STableConstants.VIEW_STATUS_ALIVE);
        jtbNotesFilter.setSelected(true);
        
        moPaneGridPrices.createTable();
        moPaneGridPrices.clearTableRows();
        moPaneGridPrices.setGridViewStatus(STableConstants.VIEW_STATUS_ALIVE);
        jtbGridPricesFilter.setSelected(true);

        // Add cell editor listener to just created taxes table:
        
        moPaneTaxes.getTable().getColumnModel().getColumn(COL_TAX_CUR).setCellEditor(moPaneTaxes.getTable().getDefaultEditor(Number.class));
        moPaneTaxes.getTable().getColumnModel().getColumn(COL_TAX_CUR).getCellEditor().addCellEditorListener(this);

        renderBasicSettings();
        renderItem(false, true); // this function actually clears all form fields, moItem already set to null

        jckIsDeleted.setEnabled(false);
        
        jckIsDpsReqMonthDelivery.setSelected(false);
        jckIsDpsReqMonthDelivery.setEnabled(false);
        jckIsDirectPrice.setSelected(true);
        
        jradAccAdvanceBilled.setSelected(true);
        renderAccountOptions();
        
        jTabbedPane.setSelectedIndex(TAB_TAX);
        jTabbedPane.setEnabledAt(TAB_PRC, false);
        jTabbedPane.setEnabledAt(TAB_CFD_ADD, false);
        updateFormEditStatus(true);

        jtfAddBachocoNúmeroPosición.setEnabled(false);
        jtfAddBachocoCentro.setEnabled(false);
        jtfAddLorealEntryNumber.setEnabled(false);
        jcbAddGenBarcode.setEnabled(false);
        jtfAddElektraOrder.setEnabled(false);
        jtfAddElektraCages.setEnabled(false);
        jtfAddElektraCagePriceUnitary.setEnabled(false);
        jtfAddElektraParts.setEnabled(false);
        jtfAddElektraPartPriceUnitary.setEnabled(false);
        jtfAddAmc71PurchaseOrder.setEnabled(false);
        jftfAddAmc71PurchaseOrderDate.setEnabled(false);

        mdQuantitySrcOrig = 0;
        mdQuantityDesOrig = 0;

        mbEnableDataAddenda = false;

        mbResetingForm = false;
        
        moFieldDpsContractBase.setDouble(0d);
        moFieldDpsContractFuture.setDouble(0d);
        moFieldDpsContractFactor.setDouble(0.10d);
        jckIsPriceConfirm.setSelected(false);
    
        actionPriceClear();

        mbPostEmissionEdition = false;
    }

    @Override
    public void formRefreshCatalogues() {
        mbResetingForm = true;

        SFormUtilities.populateComboBox(miClient, jcbFkItemId, SDataConstants.ITMU_ITEM);
        SFormUtilities.populateComboBox(miClient, jcbFkTaxRegionId, SDataConstants.FINU_TAX_REG);
        SFormUtilities.populateComboBox(miClient, jcbFkItemReferenceId_n, SDataConstants.ITMU_ITEM);
        SFormUtilities.populateComboBox(miClient, jcbFkVehicleTypeId_n, SModConsts.LOGU_TP_VEH);

        mbResetingForm = false;
    }
    
    @Override
    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(mvFields.get(i).getComponent());
                break;
            }
        }
        
        if (!mbPostEmissionEdition) {
            if (!validation.getIsError()) {
                boolean isDataShipDomReq = moItem.getDbmsDataItemGeneric().getIsDataShipDomesticReq();
                boolean isDataShipIntReq = moItem.getDbmsDataItemGeneric().getIsDataShipInternationalReq();
                boolean isDataShipQltReq = moItem.getDbmsDataItemGeneric().getIsDataQualityReq();

                String msgMissingShipData = "";
                if (isDataShipDomReq || isDataShipIntReq || isDataShipQltReq) {
                    msgMissingShipData = composeMsgMissingShipData();
                }

                if (moItem.getFkItemStatusId() == SModSysConsts.ITMS_ST_ITEM_INA) {
                    validation.setMessage(SItmConsts.MSG_ERR_ST_ITEM_INA + "\n" + SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFkItemId.getText() + "'."); // validate that item is not inactive
                    validation.setComponent(jcbFkItemId);
                }
                else if (moItem.getFkItemStatusId() == SModSysConsts.ITMS_ST_ITEM_RES && STrnUtilities.getIogCatForDpsClass(moParamDps.getDpsClassKey()) == SModSysConsts.TRNS_CT_IOG_IN) {
                    validation.setMessage(SItmConsts.MSG_ERR_ST_ITEM_RES + "\n" + SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFkItemId.getText() + "'."); // falidate that item is not restricted on in moves
                    validation.setComponent(jcbFkItemId);
                }
                else if (moFieldIsDiscountUnitaryPercentage.getBoolean() && moFieldDiscountUnitaryPercentage.getDouble() == 0) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jckIsDiscountUnitaryPercentage.getText() + "'.");
                    validation.setComponent(jtfDiscountUnitaryPercentage);
                }
                else if (moFieldIsDiscountEntryPercentage.getBoolean() && moFieldDiscountEntryPercentage.getDouble() == 0) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jckIsDiscountEntryPercentage.getText() + "'.");
                    validation.setComponent(jtfDiscountEntryPercentage);
                }
                else if (mdQuantitySrcOrig > 0 && moFieldOriginalQuantity.getDouble() < mdQuantitySrcOrig) {
                    validation.setMessage("El valor mínimo permitido para el campo '" + jlOriginalQuantity.getText() + "' es " +
                            miClient.getSessionXXX().getFormatters().getDecimalsSurfaceFormat().format(mdQuantitySrcOrig) + " " + jtfOriginalUnitSymbolRo.getText() + ".");
                    validation.setComponent(jtfOriginalQuantity);
                }
                else if (mdQuantityDesOrig > 0 && moFieldOriginalQuantity.getDouble() > mdQuantityDesOrig) {
                    validation.setMessage("El valor máximo permitido para el campo '" + jlOriginalQuantity.getText() + "' es " +
                            miClient.getSessionXXX().getFormatters().getDecimalsSurfaceFormat().format(mdQuantityDesOrig) + " " + jtfOriginalUnitSymbolRo.getText() + ".");
                    validation.setComponent(jtfOriginalQuantity);
                }
                else if (moFieldOriginalQuantity.getDouble() == 0d) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlOriginalQuantity.getText() + "'.");
                    validation.setComponent(jtfOriginalQuantity);
                }
                else if (!moItem.getIsBulk() && moFieldOriginalQuantity.getDouble() != Math.round(moFieldOriginalQuantity.getDouble())) {
                    validation.setMessage("El valor para el campo '" + jlOriginalQuantity.getText() + "' debe ser entero.");
                    validation.setComponent(jtfOriginalQuantity);
                }
                else if (SLibUtils.parseDouble(jtfQuantityRo.getText()) == 0d) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlQuantity.getText() + "'.");
                    // remember that field jtfQuantityRo is read only!, it cannot gain focus
                }
                else if (moFieldOriginalPriceUnitaryCy.getDouble() == 0d &&
                        miClient.showMsgBoxConfirm("¿Está seguro que se desea dejar sin valor al campo '" + jlOriginalPriceUnitaryCy.getText() + "'?") != JOptionPane.YES_OPTION) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlOriginalPriceUnitaryCy.getText() + "'.");
                    validation.setComponent(jtfOriginalPriceUnitaryCy);
                }
                else if (SLibUtilities.parseDouble(jtfTotalCy_rRo.getText()) == 0d &&
                        miClient.showMsgBoxConfirm("¿Está seguro que se desea dejar sin valor al campo '" + jlTotal_r.getText() + "'?") != JOptionPane.YES_OPTION) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlTotal_r.getText() + "'.");
                    // remember that field jtfTotalCy_rRo is read only!, it cannot gain focus
                }
                else if (moFieldIsSalesFreightRequired.getBoolean() && moFieldSalesPriceUnitaryCy.getDouble() == 0d &&
                        miClient.showMsgBoxConfirm("¿Está seguro que se desea dejar sin valor al campo '" + jlSalesPriceUnitaryCy.getText() + "'?") != JOptionPane.YES_OPTION) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlSalesPriceUnitaryCy.getText() + "'.");
                    validation.setComponent(jtfSalesPriceUnitaryCy);
                }
                else if (moFieldIsSalesFreightRequired.getBoolean() && moFieldSalesFreightUnitaryCy.getDouble() == 0d &&
                        miClient.showMsgBoxConfirm("¿Está seguro que se desea dejar sin valor al campo '" + jlSalesFreightUnitaryCy.getText() + "'?") != JOptionPane.YES_OPTION) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlSalesFreightUnitaryCy.getText() + "'.");
                    validation.setComponent(jtfSalesFreightUnitaryCy);
                }
                else if (moFieldIsSalesFreightRequired.getBoolean() && moFieldSalesFreightUnitaryCy.getDouble() != 0d && !moFieldIsSalesFreightAddPrice.getBoolean() &&
                        miClient.showMsgBoxConfirm("¿Está seguro que se desea dejar deseleccionado el campo '" + jckIsSalesFreightAddPrice.getText() + "'?") != JOptionPane.YES_OPTION) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_OPTION_SELECT + "'" + jckIsSalesFreightAddPrice.getText() + "'.");
                    validation.setComponent(jckIsSalesFreightAddPrice);
                }
                else if (moFieldIsSalesFreightConfirm.getBoolean() &&
                        miClient.showMsgBoxConfirm("Favor de confirmar que el valor del campo '" + jlSalesFreightUnitaryCy.getText() + "' es correcto.") != JOptionPane.YES_OPTION) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlSalesFreightUnitaryCy.getText() + "'.");
                    validation.setComponent(jtfSalesFreightUnitaryCy);
                }
                else if (moPanelFkCostCenterId_n.isEmptyAccountId()) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + moPanelFkCostCenterId_n.getFieldAccountLabel().getText() + "'.");
                    validation.setComponent(moPanelFkCostCenterId_n.getFieldAccount().getComponent());
                }
                else if (moItem.getIsPrepayment() && moParamDps.isDocument() && moFieldOperationsType.getKeyAsIntArray()[0] != SDataConstantsSys.TRNX_OPS_TYPE_OPS_PREPAY) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlOperationsType.getText() + "', "
                            + "debería ser: '" + SDataConstantsSys.OperationsTypesOpsMap.get(SDataConstantsSys.TRNX_OPS_TYPE_OPS_PREPAY) + "'.");
                    validation.setComponent(moFieldOperationsType.getComponent());
                }
                else if (moItem.getIsPrepayment() && moParamDps.isAdjustment() && !SLibUtils.belongsTo(moFieldOperationsType.getKeyAsIntArray()[0], new int[] { SDataConstantsSys.TRNX_OPS_TYPE_ADJ_PREPAY, SDataConstantsSys.TRNX_OPS_TYPE_ADJ_APP_PREPAY })) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlOperationsType.getText() + "', "
                            + "debería ser: '" + SDataConstantsSys.OperationsTypesAdjMap.get(SDataConstantsSys.TRNX_OPS_TYPE_ADJ_PREPAY) + "' o '" + SDataConstantsSys.OperationsTypesAdjMap.get(SDataConstantsSys.TRNX_OPS_TYPE_ADJ_APP_PREPAY) + "'.");
                    validation.setComponent(moFieldOperationsType.getComponent());
                }
                else if (SLibUtils.belongsTo(moFieldOperationsType.getKeyAsIntArray()[0], new int[] { SDataConstantsSys.TRNX_OPS_TYPE_OPS_PREPAY, SDataConstantsSys.TRNX_OPS_TYPE_ADJ_PREPAY, SDataConstantsSys.TRNX_OPS_TYPE_ADJ_APP_PREPAY }) && !moItem.getIsPrepayment()) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFkItemId.getText() + "' debido al valor del campo '" + jlOperationsType.getText() + "'.");
                    validation.setComponent(jcbFkItemId);
                }
                else if (SLibUtils.belongsTo(moFieldOperationsType.getKeyAsIntArray()[0], new int[] { SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS_APP_PREPAY, SDataConstantsSys.TRNX_OPS_TYPE_ADJ_OPS_APP_PREPAY }) && moFieldDiscountDocCy.getDouble() == 0) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDiscountDoc.getText() + "' debido al valor del campo '" + jlOperationsType.getText() + "'.");
                    validation.setComponent(jtfDiscountDocCy);
                }
                else if (jcbFkThirdTaxCausingId_n.getSelectedIndex() > 0 && SLibUtils.belongsTo(moFieldFkThirdTaxCausingId_n.getKeyAsIntArray()[0], new int[] { moParamDps.getFkBizPartnerId_r(), miClient.getSessionXXX().getCurrentCompany().getPkCompanyId() })) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFkThirdTaxCausingId_n.getText() + "'."); // third party cannot be the owner of this document nor the company itself
                    validation.setComponent(jcbFkThirdTaxCausingId_n);
                }
                else if (jcbFkVehicleTypeId_n.getSelectedIndex() <= 0 && SDataDpsEntry.validateShipmentDataValue(moFieldPlate.getString(), (isDataShipDomReq && (moParamDps.isOrderSal() || moParamDps.isDocumentSal())))) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkVehicleTypeId_n.getText() + "'.");
                    validation.setComponent(jcbFkVehicleTypeId_n);
                    jTabbedPane.setSelectedIndex(TAB_MKT);
                }
                else if (SDataDpsEntry.validateShipmentDataValue(moFieldDriver.getString(), (isDataShipDomReq && (moParamDps.isOrderSal() || moParamDps.isDocumentSal())))) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDriver.getText() + "'.");
                    validation.setComponent(jtfDriver);
                    jTabbedPane.setSelectedIndex(TAB_MKT);
                }
                else if (isDataShipDomReq && jcbFkVehicleTypeId_n.getSelectedIndex() > 0 && SDataDpsEntry.validateShipmentDataValue(moFieldPlate.getString(), (isDataShipDomReq && (moParamDps.isOrderSal() || moParamDps.isDocumentSal())))) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlVehicleNumber.getText() + "'.");
                    validation.setComponent(jtfVehicleNumber);
                    jTabbedPane.setSelectedIndex(TAB_MKT);
                }
                else if (SDataDpsEntry.validateShipmentDataValue(moFieldContainerTank.getString(), (isDataShipDomReq && (moParamDps.isOrderSal() || moParamDps.isDocumentSal())))) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlContainerTank.getText() + "'.");
                    validation.setComponent(jtfContTank);
                    jTabbedPane.setSelectedIndex(TAB_MKT);
                }
                else if (SDataDpsEntry.validateShipmentDataValue(moFieldSealQuality.getString(), (isDataShipQltReq && (moParamDps.isOrderSal() || moParamDps.isDocumentSal())))) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlQualitySeal.getText() + "'.");
                    validation.setComponent(jtfSealQuality);
                    jTabbedPane.setSelectedIndex(TAB_MKT);
                }
                else if (SDataDpsEntry.validateShipmentDataValue(moFieldSealSecurity.getString(), (isDataShipQltReq && (moParamDps.isOrderSal() || moParamDps.isDocumentSal())))) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlSecuritySeal.getText() + "'.");
                    validation.setComponent(jtfSecuritySeal);
                    jTabbedPane.setSelectedIndex(TAB_MKT);
                }
                 else if (SDataDpsEntry.validateShipmentDataValue(moFieldTicket.getString(), (isDataShipDomReq && (moParamDps.isOrderSal() || moParamDps.isDocumentSal())))) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlTicket.getText() + "'.");
                    validation.setComponent(jtfTicket);
                    jTabbedPane.setSelectedIndex(TAB_MKT);
                }
                else if (SDataDpsEntry.validateShipmentDataValue(moFieldVgm.getString(), (isDataShipIntReq && (moParamDps.isOrderSal() || moParamDps.isDocumentSal())))) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlVgm.getText() + "'.");
                    validation.setComponent(jtfVgm);
                    jTabbedPane.setSelectedIndex(TAB_MKT);
                } 
                else if (!msgMissingShipData.isEmpty() && 
                        miClient.showMsgBoxConfirm("Los siguientes campos tienen un valor provisional (" + STrnConsts.TXT_FIELD_BLANK + "):\n" + msgMissingShipData + ".\n¿Deseas conservarlos?") != JOptionPane.YES_OPTION) {
                    validation.setMessage("Revisar los valores de la sección '" + ((TitledBorder) jpDataShip.getBorder()).getTitle() + "'.");
                    validation.setComponent(jtfDriver);
                    jTabbedPane.setSelectedIndex(TAB_MKT);
                }
                else if (moItem.getIsPrepayment() && moFieldOriginalQuantity.getDouble() > 0 && !jradAccCashAccount.isSelected() && !jradAccAdvanceBilled.isSelected() && (jradAccAdvanceBilled.isEnabled() || jradAccCashAccount.isEnabled())) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlAccOptions.getText() + "'.");
                    validation.setComponent(jradAccCashAccount);
                    jTabbedPane.setSelectedIndex(TAB_MKT);
                }
                else if (moItem.getIsPrepayment() && moFieldOriginalQuantity.getDouble() > 0 && jradAccCashAccount.isSelected() && jcbFkCashAccountId_n.getSelectedIndex() <= 0) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkCashAccountId_n.getText() + "'.");
                    validation.setComponent(jcbFkCashAccountId_n);
                    jTabbedPane.setSelectedIndex(TAB_MKT);
                }
                else if (moParamDps.isOrder() && moDpsEntry.getContractPriceYear() != SLibConstants.UNDEFINED && moDpsEntry.getContractPriceMonth() != SLibConstants.UNDEFINED && moFieldOriginalQuantity.getDouble() > mdQuantityPrc) {
                    validation.setMessage("De acuerdo con la entrega mensual actual" + (mbIsLastPrc ? ", considerando el excedente permitido en la partida del documento origen" : "") + ":\n" +
                            "el valor máximo permitido para el campo '" + jlOriginalQuantity.getText() + "' es " + SLibUtils.getDecimalFormatQuantity().format(mdQuantityPrc) + " " + jtfOriginalUnitSymbolRo.getText() + ".");
                    validation.setComponent(jtfOriginalQuantity);
                }
                else {
                    // Validate unitary price vs unitary month price in monthly order:

                    if (moFieldOriginalPriceUnitaryCy.getDouble() != 0d) {
                        boolean priceUnitaryDiffers = false;

                        for (int i = 0; i < moPaneGridPrices.getGridRows().size(); i++) {
                            if (!SLibUtils.compareAmount(((SDataDpsEntryPrice) moPaneGridPrices.getGridRows().get(i).getData()).getOriginalPriceUnitaryCy(), moFieldOriginalPriceUnitaryCy.getDouble())) {
                                priceUnitaryDiffers = true;
                                break;
                            }       
                        }

                        if (priceUnitaryDiffers && 
                                miClient.showMsgBoxConfirm("El valor del campo " + jlOriginalPriceUnitaryCy.getText() + " de la partida es diferente al de las entregas mensuales.\n¿Deseas continuar?") == JOptionPane.NO_OPTION) {
                            validation.setMessage("Igualar el valor del campo " + jlOriginalPriceUnitaryCy.getText() + " de la partida al de las entregas mensuales.");
                        }
                    }
                }
            }

            if (!validation.getIsError()) {
                // Validate item's reference:

                if (moItem.getIsReference() && moFieldReference.getString().isEmpty()) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlReference.getText() + "'.");
                    validation.setComponent(jtfReference);
                }
                else {
                    String msgValidation = "";

                    if (!moFieldReference.getString().isEmpty()) {
                        try {
                            // Validate that reference does not exist yet:

                            SDataUtilities.validateDpsEtyReference(miClient.getSession(), moParamDps.getDpsClassKey(), moFieldReference.getString(), (int[]) moParamDps.getPrimaryKey());
                        }
                        catch (Exception e) {
                            validation.setMessage(msgValidation = e.getMessage());
                            validation.setComponent(jtfReference);
                        }
                    }

                    if (msgValidation.isEmpty()) {
                        // Validate cost center:

                        msgValidation = SDataUtilities.validateCostCenter(miClient, moPanelFkCostCenterId_n.getCurrentInputCostCenter(), moParamDps.getDate());

                        if (!msgValidation.isEmpty()) {
                            validation.setMessage(msgValidation);
                            validation.setComponent(moPanelFkCostCenterId_n.getFieldAccount().getComponent());
                        }
                    }
                }
            }

            if (!validation.getIsError() && moParamDps.isForSales()) {
                if (moParamBizPartner.getIsCustomer() && moParamBizPartner.getDbmsCategorySettingsCus().getFkCfdAddendaTypeId() != SDataConstantsSys.BPSS_TP_CFD_ADD_NA) {
                    if (jtfAddBachocoNúmeroPosición.isEnabled() && moFieldAddBachocoNúmeroPosición.getInteger() % 10 != 0) {
                        validation.setMessage("El valor para el campo '" + jlAddBachocoNúmeroPosición.getText() + "' debe ser múltiplo de 10.");
                        validation.setComponent(jtfAddBachocoNúmeroPosición);
                        jTabbedPane.setSelectedIndex(TAB_CFD_ADD); // show CFD Addenda's tab
                    }
                }
            }

            if (!validation.getIsError() && moParamDps.isEstimate()) {
                if (jckIsDpsReqMonthDelivery.isSelected()) {
                    if (moFieldDpsContractBase.getDouble() < 0d) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlDpsContractBase.getText() + "'.");
                        validation.setComponent(jtfDpsContractBase);
                    }
                    else if (moFieldDpsContractFuture.getDouble() < 0d) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlDpsContractFuture.getText() + "'.");
                        validation.setComponent(jtfDpsContractFuture);
                    }
                    else if (moPaneGridPrices.getGridRows().size() <= 0) {
                        validation.setMessage("La partida tiene seleccionada la opción '" + jckIsDpsReqMonthDelivery.getText() + "', pero no se encontró ningúna entrega mensual programada.");
                        validation.setComponent(jckIsDpsReqMonthDelivery);
                    }
                    else if (!validation.getIsError()) {
                        double quantity = 0;

                        for (STableRow row : moPaneGridPrices.getGridRows()) {
                            if (!((SDataDpsEntryPrice) row.getData()).getIsDeleted()) {
                                quantity += ((SDataDpsEntryPrice) row.getData()).getOriginalQuantity();
                            }
                        }

                        if (quantity != moFieldOriginalQuantity.getDouble()) {
                            validation.setMessage("La suma de cantidades de '"+ jTabbedPane.getTitleAt(TAB_PRC) + "' (" + miClient.getSessionXXX().getFormatters().getDecimalsSurfaceFormat().format(quantity) + ") debe coincidir con el valor especificado en el campo '" + jlOriginalQuantity.getText() + "' (" + jtfOriginalQuantity.getText() + ") de la partida.");
                            validation.setComponent(moPaneGridPrices);
                        }
                    }
                }
                else {
                    if (moPaneGridPrices.getGridRows().size() > 0) {
                        if (miClient.showMsgBoxConfirm("¿Está seguro que se desea dejar sin valor al campo '" + jckIsDpsReqMonthDelivery.getText() + "'?") == JOptionPane.NO_OPTION) {
                            validation.setMessage("La partida no tiene seleccionada la opción '" + jckIsDpsReqMonthDelivery.getText() + "', pero se encontraron entregas mensuales programadas.");
                            validation.setComponent(jckIsDpsReqMonthDelivery);
                        }
                    }
                }

                if (validation.getIsError()) {
                    jTabbedPane.setSelectedIndex(TAB_PRC); // show monthly price's tab
                }
            }

            if (!validation.getIsError()) {
                // Check if entry item needs to be added to document from source document:

                try {
                    if (!mbRightTrnOmitSourceDoc) {
                        STrnUtils.checkItemStandaloneDoc(miClient.getSession(), moParamDps.getDpsTypeKey(), moItem.getPkItemId(), moDpsEntry.hasDpsLinksAsDestiny());
                    }
                }
                catch (Exception e) {
                    validation.setMessage(e.getMessage());
                    validation.setComponent(jcbFkItemId);
                    SLibUtilities.printOutException(this, e);
                }
            }

            if (!validation.getIsError()) {
                //validate cfd complement values:
                int values = 0;

                if (!moFieldComplConceptKey.getString().isEmpty()) {
                    values++;
                }
                if (!moFieldComplConcept.getString().isEmpty()) {
                    values++;
                }
                if (!moFieldComplCfdProdServ.getString().isEmpty()) {
                    values++;
                }
                if (!moFieldComplCfdUnit.getString().isEmpty()) {
                    values++;
                }

                if (values != 0 && values != CFD_COMPL_VALS) {
                    validation.setMessage("Se debe especificar un valor para todos los campos de información complementaria.");
                    validation.setComponent(jtfComplConceptKey);
                    validation.setTabbedPaneIndex(TAB_CFD_COMPL);
                }
            }
        }
        
        return validation;
    }

    @Override
    public void setFormStatus(int status) {
        mnFormStatus = status;
        updateFormEditStatus(mnFormStatus == SLibConstants.FORM_STATUS_EDIT);
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

        moDpsEntry = (SDataDpsEntry) registry;

        moFieldFkItemId.setFieldValue(new int[] { moDpsEntry.getFkItemId() });
        itemChangedFkItemId(false);

        moFieldConceptKey.setFieldValue(moDpsEntry.getConceptKey());
        moFieldConcept.setFieldValue(moDpsEntry.getConcept());
        moFieldFkOriginalUnitId.setFieldValue(new int[] { moDpsEntry.getFkOriginalUnitId() });
        moFieldIsDiscountDocApplying.setFieldValue(moDpsEntry.getIsDiscountDocApplying());
        moFieldIsDiscountUnitaryPercentage.setFieldValue(moDpsEntry.getIsDiscountUnitaryPercentage());
        moFieldDiscountUnitaryPercentage.setFieldValue(moDpsEntry.getDiscountUnitaryPercentage());
        moFieldIsDiscountEntryPercentage.setFieldValue(moDpsEntry.getIsDiscountEntryPercentage());
        moFieldDiscountEntryPercentage.setFieldValue(moDpsEntry.getDiscountEntryPercentage());
        moFieldOriginalQuantity.setFieldValue(moDpsEntry.getOriginalQuantity());
        moFieldOriginalPriceUnitaryCy.setFieldValue(moDpsEntry.getOriginalPriceUnitaryCy());
        moFieldOriginalDiscountUnitaryCy.setFieldValue(moDpsEntry.getOriginalDiscountUnitaryCy());
        
        if (isSalesFreightRequirable() && !moDpsEntry.getIsSalesFreightRequired()) {
            moFieldSalesPriceUnitaryCy.setFieldValue(moFieldOriginalPriceUnitaryCy.getDouble()); // copy original price unitary
            moFieldSalesFreightUnitaryCy.setFieldValue(0d);
            moFieldIsSalesFreightRequired.setFieldValue(true);
            moFieldIsSalesFreightAddPrice.setFieldValue(true);
            moFieldIsSalesFreightConfirm.setFieldValue(false);
        }
        else {
            moFieldSalesPriceUnitaryCy.setFieldValue(moDpsEntry.getSalesPriceUnitaryCy());
            moFieldSalesFreightUnitaryCy.setFieldValue(moDpsEntry.getSalesFreightUnitaryCy());
            moFieldIsSalesFreightRequired.setFieldValue(moDpsEntry.getIsSalesFreightRequired());
            moFieldIsSalesFreightConfirm.setFieldValue(moDpsEntry.getIsSalesFreightConfirm());
            moFieldIsSalesFreightAddPrice.setFieldValue(moDpsEntry.getIsSalesFreightAdd());
        }
        
        moFieldDiscountEntryCy.setFieldValue(moDpsEntry.getDiscountEntryCy());
        moFieldDiscountDocCy.setFieldValue(moDpsEntry.getDiscountDocCy());
        // ...
        moFieldLength.setFieldValue(moDpsEntry.getLength());
        moFieldSurface.setFieldValue(moDpsEntry.getSurface());
        moFieldVolume.setFieldValue(moDpsEntry.getVolume());
        moFieldMass.setFieldValue(moDpsEntry.getMass());
        moFieldWeightPackagingExtra.setFieldValue(moDpsEntry.getWeightPackagingExtra());
        moFieldWeightGross.setFieldValue(moDpsEntry.getWeightGross());
        moFieldWeightDelivery.setFieldValue(moDpsEntry.getWeightDelivery());
        moFieldSurplusPercentage.setFieldValue(moDpsEntry.getSurplusPercentage());
        moFieldIsDpsPriceVariable.setFieldValue(moDpsEntry.getIsPriceVariable());
        jckIsPriceConfirm.setSelected(moDpsEntry.getIsPriceConfirm());
        moFieldDpsContractBase.setFieldValue(moDpsEntry.getContractBase());
        moFieldDpsContractFuture.setFieldValue(moDpsEntry.getContractFuture());
        moFieldDpsContractFactor.setFieldValue(moDpsEntry.getContractFactor());
        
        moFieldFkVehicleTypeId_n.setFieldValue(new int[] { moDpsEntry.getFkVehicleTypeId_n() });
        moFieldDriver.setFieldValue(moDpsEntry.getDriver());
        moFieldPlate.setFieldValue(moDpsEntry.getPlate());
        moFieldContainerTank.setFieldValue(moDpsEntry.getContainerTank());
        moFieldSealQuality.setFieldValue(moDpsEntry.getSealQuality());
        moFieldSealSecurity.setFieldValue(moDpsEntry.getSealSecurity());
        moFieldTicket.setFieldValue(moDpsEntry.getTicket());
        moFieldVgm.setFieldValue(moDpsEntry.getVgm());
        
        moFieldIsPrepayment.setFieldValue(moDpsEntry.getIsPrepayment());
        if (moDpsEntry.getIsPrepayment()) {
            if (moDpsEntry.getKeyCashAccount_n() != null) {
                jradAccCashAccount.setSelected(true);
            }
            else {
                jradAccAdvanceBilled.setSelected(true);
            }
            renderAccountOptions();
        }
        moFieldFkBankAccountId_n.setFieldValue(moDpsEntry.getKeyCashAccount_n());
        
        moFieldIsDiscountRetailChain.setFieldValue(moDpsEntry.getIsDiscountRetailChain());
        moFieldIsTaxesAutomaticApplying.setFieldValue(moDpsEntry.getIsTaxesAutomaticApplying());
        moFieldIsInventoriable.setFieldValue(moDpsEntry.getIsInventoriable());
        moFieldIsDeleted.setFieldValue(moDpsEntry.getIsDeleted());
        moFieldFkTaxRegionId.setFieldValue(new int[] { moDpsEntry.getFkTaxRegionId() });
        moFieldFkThirdTaxCausingId_n.setFieldValue(new int[] { moDpsEntry.getFkThirdTaxCausingId_n() });
        moFieldFkItemReferenceId_n.setFieldValue(new int[] { moDpsEntry.getFkItemRefId_n() });
        moFieldReference.setFieldValue(moDpsEntry.getReference());
        moFieldOperationsType.setFieldValue(new int[] { moDpsEntry.getOperationsType() });

        jckIsSurplusPercentageApplying.setSelected(moDpsEntry.getSurplusPercentage() > 0);

        for (SDataDpsEntryTax tax : moDpsEntry.getDbmsEntryTaxes()) {
            moPaneTaxes.addTableRow(new SDataDpsEntryTaxRow(tax.clone()));
        }
        moPaneTaxes.renderTableRows();
        moPaneTaxes.setTableRowSelection(0);

        for (SDataDpsEntryCommissions commissions : moDpsEntry.getDbmsEntryCommissions()) {
            moPaneCommissions.addTableRow(new SDataDpsEntryCommissionsRow(commissions));
        }
        moPaneCommissions.renderTableRows();
        moPaneCommissions.setTableRowSelection(0);

         for (SDataDpsEntryNotes notes : moDpsEntry.getDbmsEntryNotes()) {
            if (notes.getPkNotesId() != SLibConstants.UNDEFINED) {
                moPaneGridNotes.addTableRow(new SDataDpsEntryNotesRow(notes.clone()));
            }
            else if (notes.getIsAllDocs()) {
                moPaneGridNotes.addTableRow(new SDataDpsEntryNotesRow(notes.clone()));
            }
        }
        moPaneGridNotes.renderTableRows();
        moPaneGridNotes.setTableRowSelection(0);

        for (SDataDpsEntryPrice price : moDpsEntry.getDbmsEntryPrices()) {
                moPaneGridPrices.addTableRow(new SDataDpsEntryPriceRow(price.clone()));
        }
        moPaneGridPrices.renderTableRows();
        moPaneGridPrices.setTableRowSelection(0);

        moPanelFkCostCenterId_n.getFieldAccount().setFieldValue(moDpsEntry.getFkCostCenterId_n().isEmpty() ? moPanelFkCostCenterId_n.getEmptyAccountId() : moDpsEntry.getFkCostCenterId_n());
        moPanelFkCostCenterId_n.refreshPanel();
        
        if (moDpsEntry.getDbmsComplement() != null) {
            moFieldComplConceptKey.setFieldValue(moDpsEntry.getDbmsComplement().getConceptKey());
            moFieldComplConcept.setFieldValue(moDpsEntry.getDbmsComplement().getConcept());
            moFieldComplCfdProdServ.setFieldValue(moDpsEntry.getDbmsComplement().getCfdProdServ());
            moFieldComplCfdUnit.setFieldValue(moDpsEntry.getDbmsComplement().getCfdUnit());
        }

        renderDpsEntryValue();
        renderFieldsStatus();
        jckIsDeleted.setEnabled(true);
        
        if (moParamDps.isEstimate()) {
            jTabbedPane.setEnabledAt(TAB_PRC, true);
            enablePriceContractFields(moDpsEntry.getIsPriceVariable());
        }

        if (moParamDps.isDocumentOrAdjustmentSal() && mbEnableDataAddenda) {
            jTabbedPane.setEnabledAt(TAB_CFD_ADD, true);
            setAddendaData();
        }

        mbResetingForm = false;
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        int i = 0;

        if (moDpsEntry.getIsRegistryNew()) {
            moDpsEntry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moDpsEntry.setIsRegistryEdited(true);
            moDpsEntry.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        calculateTotal();

        moDpsEntry.setConceptKey(moFieldConceptKey.getString());
        moDpsEntry.setConcept(moFieldConcept.getString());
        moDpsEntry.setReference(moFieldReference.getString());

        moDpsEntry.setLength(moFieldLength.getDouble());
        moDpsEntry.setSurface(moFieldSurface.getDouble());
        moDpsEntry.setVolume(moFieldVolume.getDouble());
        moDpsEntry.setMass(moFieldMass.getDouble());
        moDpsEntry.setWeightPackagingExtra(moFieldWeightPackagingExtra.getDouble());
        moDpsEntry.setWeightGross(moFieldWeightGross.getDouble());
        moDpsEntry.setWeightDelivery(moFieldWeightDelivery.getDouble());
        moDpsEntry.setSurplusPercentage(moFieldSurplusPercentage.getDouble());
        
        moDpsEntry.setIsPriceVariable(moFieldIsDpsPriceVariable.getBoolean());
        moDpsEntry.setIsPriceConfirm(jckIsPriceConfirm.isSelected());
        moDpsEntry.setContractBase(!jckIsDpsReqMonthDelivery.isSelected() ? 0d : moFieldDpsContractBase.getDouble());
        moDpsEntry.setContractFuture(!jckIsDpsReqMonthDelivery.isSelected() ? 0d : moFieldDpsContractFuture.getDouble());
        moDpsEntry.setContractFactor(!jckIsDpsReqMonthDelivery.isSelected() ? 0d : moFieldDpsContractFactor.getDouble());
        moDpsEntry.setFkVehicleTypeId_n(moFieldFkVehicleTypeId_n.getKeyAsIntArray()[0]);
        moDpsEntry.setFkCashCompanyBranchId_n(jcbFkCashAccountId_n.getSelectedIndex() <= 0 ? SLibConstants.UNDEFINED : moFieldFkBankAccountId_n.getKeyAsIntArray()[0]);
        moDpsEntry.setFkCashAccountId_n(jcbFkCashAccountId_n.getSelectedIndex() <= 0 ? SLibConstants.UNDEFINED : moFieldFkBankAccountId_n.getKeyAsIntArray()[1]);
        
        moDpsEntry.setDriver(moFieldDriver.getString());
        moDpsEntry.setPlate(moFieldPlate.getString());
        moDpsEntry.setContainerTank(moFieldContainerTank.getString());
        moDpsEntry.setSealQuality(moFieldSealQuality.getString());
        moDpsEntry.setSealSecurity(moFieldSealSecurity.getString());
        moDpsEntry.setTicket(moFieldTicket.getString());
        moDpsEntry.setVgm(moFieldVgm.getString());
        
        moDpsEntry.setOperationsType(moFieldOperationsType.getKeyAsIntArray()[0]);

        moDpsEntry.setFkItemRefId_n(moFieldFkItemReferenceId_n.getKeyAsIntArray()[0]);
        moDpsEntry.setFkCostCenterId_n(moPanelFkCostCenterId_n.isEmptyAccountId() ? "" : moPanelFkCostCenterId_n.getFieldAccount().getString());
        moDpsEntry.setIsPrepayment(moFieldIsPrepayment.getBoolean());
        moDpsEntry.setIsInventoriable(moFieldIsInventoriable.getBoolean());
        moDpsEntry.setIsDeleted(moFieldIsDeleted.getBoolean());

        moDpsEntry.getDbmsEntryNotes().clear();
        for (STableRow row : moPaneGridNotes.getGridRows()) {
            moDpsEntry.getDbmsEntryNotes().add((SDataDpsEntryNotes) row.getData());
        }
        
        if (jckIsDpsReqMonthDelivery.isSelected()) {
            moDpsEntry.getDbmsEntryPrices().clear();
            for (STableRow row : moPaneGridPrices.getGridRows()) {
                moDpsEntry.getDbmsEntryPrices().add((SDataDpsEntryPrice) row.getData());
            }
        }
        
        moDpsEntry.getDbmsEntryTaxes().clear();
        for (i = 0; i < moPaneTaxes.getTableGuiRowCount(); i++) {
            moDpsEntry.getDbmsEntryTaxes().add((SDataDpsEntryTax) ((SDataDpsEntryTaxRow) moPaneTaxes.getTableRow(i)).getData());
        }
        
        moDpsEntry.getDbmsEntryCommissions().clear();
        for (i = 0; i < moPaneCommissions.getTableGuiRowCount(); i++) {
            moDpsEntry.getDbmsEntryCommissions().add((SDataDpsEntryCommissions) ((SDataDpsEntryCommissionsRow) moPaneCommissions.getTableRow(i)).getData());
        }
        
        moDpsEntry.setDbmsOriginalUnitSymbol(jtfOriginalUnitSymbolRo.getText());
        moDpsEntry.setDbmsUnitSymbol(jtfUnitSymbolRo.getText());
        moDpsEntry.setDbmsTaxRegion(((SFormComponentItem) jcbFkTaxRegionId.getSelectedItem()).getItem());
        moDpsEntry.setDbmsItemRef_n(!jcbFkItemReferenceId_n.isEnabled() || jcbFkItemReferenceId_n.getSelectedIndex() <= 0 ? "" : SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.ITMU_ITEM, moFieldFkItemReferenceId_n.getKeyAsIntArray()));
        moDpsEntry.setDbmsCostCenter_n(!moPanelFkCostCenterId_n.getFieldAccount().getComponent().isEnabled() ? "" : (moPanelFkCostCenterId_n.getCurrentInputCostCenter() == null ? "" : moPanelFkCostCenterId_n.getCurrentInputCostCenter().getCostCenter()));
        moDpsEntry.setDbmsCostCenterCode(!moPanelFkCostCenterId_n.getFieldAccount().getComponent().isEnabled() ? "" : (moPanelFkCostCenterId_n.getCurrentInputCostCenter() == null ? "" : moPanelFkCostCenterId_n.getCurrentInputCostCenter().getCode()));
        
        // Addenda data row:
        
        if (!isCfdAddendaPosible()) {
            moDpsEntry.setDbmsAddBachocoNumeroPosicion(0);
            moDpsEntry.setDbmsAddBachocoCentro("");
            moDpsEntry.setDbmsAddLorealEntryNumber(0);
            moDpsEntry.setDbmsAddSorianaCodigo("");
            moDpsEntry.setDbmsAddElektraOrder("");
            moDpsEntry.setDbmsAddElektraBarcode("");
            moDpsEntry.setDbmsAddElektraCages(0);
            moDpsEntry.setDbmsAddElektraCagePriceUnitary(0.0);
            moDpsEntry.setDbmsAddElektraParts(0);
            moDpsEntry.setDbmsAddElektraPartPriceUnitary(0.0);
            moDpsEntry.setDbmsAddJsonData("");
        }
        else {
            moDpsEntry.setDbmsAddBachocoNumeroPosicion(moFieldAddBachocoNúmeroPosición.getInteger());
            moDpsEntry.setDbmsAddBachocoCentro(moFieldAddBachocoCentro.getString());
            moDpsEntry.setDbmsAddLorealEntryNumber(moFieldAddLorealEntryNumber.getInteger());
            moDpsEntry.setDbmsAddSorianaCodigo(moFieldAddGenBarcode.getString());
            moDpsEntry.setDbmsAddElektraOrder(moFieldAddElektraOrder.getString());
            moDpsEntry.setDbmsAddElektraBarcode(moFieldAddGenBarcode.getString());
            moDpsEntry.setDbmsAddElektraCages(moFieldAddElektraCages.getInteger());
            moDpsEntry.setDbmsAddElektraCagePriceUnitary(moFieldAddElektraCagePriceUnitary.getDouble());
            moDpsEntry.setDbmsAddElektraParts(moFieldAddElektraParts.getInteger());
            moDpsEntry.setDbmsAddElektraPartPriceUnitary(moFieldAddElektraPartPriceUnitary.getDouble());
            moDpsEntry.setDbmsAddJsonData(composeAddendaJsonData());
        }
        
        if (moFieldComplConceptKey.getString().isEmpty()) {
            moDpsEntry.setDbmsComplement(null);
        }
        else {
            SDataDpsEntryComplement complement = new SDataDpsEntryComplement();
            complement.setConceptKey(moFieldComplConceptKey.getString());
            complement.setConcept(moFieldComplConcept.getString());
            complement.setCfdProdServ(moFieldComplCfdProdServ.getString());
            complement.setCfdUnit(moFieldComplCfdUnit.getString());
            moDpsEntry.setDbmsComplement(complement);
        }
        
        return moDpsEntry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(int type, java.lang.Object value) {
        String currencyCode = "";
        
        mbUpdatingForm = true;

        switch (type) {
            case SDataConstants.TRN_DPS:
                if (value == null) {
                    moParamDps = null;

                    jtfCurrencyKeyRo.setText("");
                    jtfTaxIdentityEmisorRo.setText("");
                    jtfTaxIdentityReceptorRo.setText("");

                    manItemClassFilterKey = null;
                    
                    jcbFkCashAccountId_n.removeAllItems();
                    jcbOperationsType.removeAllItems();
                }
                else {
                    moParamDps = (SDataDps) value;
                    
                    jtfCurrencyKeyRo.setText(currencyCode = SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.CFGU_CUR, new int[] { moParamDps.getFkCurrencyId() }, SLibConstants.DESCRIPTION_CODE));
                    jtfTaxIdentityEmisorRo.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.FINU_TAX_IDY, new int[] { moParamDps.getFkTaxIdentityEmisorTypeId() }));
                    jtfTaxIdentityReceptorRo.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.FINU_TAX_IDY, new int[] { moParamDps.getFkTaxIdentityReceptorTypeId() }));
                    jtfCurrencyKeyRo.setCaretPosition(0);
                    jtfTaxIdentityEmisorRo.setCaretPosition(0);
                    jtfTaxIdentityReceptorRo.setCaretPosition(0);
                    
                    if (moParamDps.isDocumentPur()) {
                        manItemClassFilterKey = SDataConstantsSys.ITMS_CL_ITEM_PUR_CON;
                    }
                    else {
                        manItemClassFilterKey = SDataConstantsSys.ITMS_CL_ITEM_SAL_PRO;
                    }
                    
                    SFormUtilities.populateComboBox(miClient, jcbFkCashAccountId_n, SDataConstants.FIN_ACC_CASH, new int[] { moParamDps.getFkCompanyBranchId() });
                    SFormUtilities.populateComboBox(miClient, jcbFkThirdTaxCausingId_n, moParamDps.isForSales() ? SDataConstants.BPSX_BP_CUS : SDataConstants.BPSX_BP_SUP);
                    
                    jcbOperationsType.removeAllItems();
                    jcbOperationsType.addItem(new SFormComponentItem(null, "(Seleccionar tipo operación)"));
                    if (moParamDps.isAdjustment()) {
                        for (Integer key : SDataConstantsSys.OperationsTypesAdjMap.keySet()) {
                            jcbOperationsType.addItem(new SFormComponentItem(new int[] { key }, SDataConstantsSys.OperationsTypesAdjMap.get(key)));
                        }
                    }
                    else {
                        for (Integer key : SDataConstantsSys.OperationsTypesOpsMap.keySet()) {
                            jcbOperationsType.addItem(new SFormComponentItem(new int[] { key }, SDataConstantsSys.OperationsTypesOpsMap.get(key)));
                        }
                    }
                    jcbOperationsType.setSelectedIndex(1);
                }

                jtfOriginalPriceUnitaryCyCurrencyKeyRo.setText(currencyCode);
                jtfOriginalDiscountUnitaryCyCurrencyKeyRo.setText(currencyCode);
                jtfSalesPriceUnitaryCyCurrencyKeyRo.setText(currencyCode);
                jtfSalesFreightUnitaryCyCurrencyKeyRo.setText(currencyCode);
                jtfPriceCurrencyKeyPriceUnitaryCy.setText(currencyCode);
                
                jtfOriginalPriceUnitaryCyCurrencyKeyRo.setCaretPosition(0);
                jtfOriginalDiscountUnitaryCyCurrencyKeyRo.setCaretPosition(0);
                jtfSalesPriceUnitaryCyCurrencyKeyRo.setCaretPosition(0);
                jtfSalesFreightUnitaryCyCurrencyKeyRo.setCaretPosition(0);
                jtfPriceCurrencyKeyPriceUnitaryCy.setCaretPosition(0);
                break;

            case SDataConstants.BPSU_BP:
                if (value == null) {
                    moParamBizPartner = null;
                }
                else {
                    moParamBizPartner = (SDataBizPartner) value;
                }
                break;

            case SDataConstants.BPSU_BPB:
                if (value == null) {
                    moParamBizPartnerBranch = null;
                    moFieldFkTaxRegionId.setFieldValue(null);
                }
                else {
                    moParamBizPartnerBranch = (SDataBizPartnerBranch) value;
                    moFieldFkTaxRegionId.setFieldValue(new int[] { moParamBizPartnerBranch.getFkTaxRegionId_n() != 0 ? moParamBizPartnerBranch.getFkTaxRegionId_n() : miClient.getSessionXXX().getParamsCompany().getFkDefaultTaxRegionId_n() });
                }
                break;

            case SDataConstants.TRNS_TP_DPS_ADJ:
                if (value == null) {
                    mnParamAdjustmentTypeId = SDataConstantsSys.UNDEFINED;
                }
                else {
                    mnParamAdjustmentTypeId = (Integer) value;
                }
                break;

            case SDataConstants.FINU_TAX_REG:
                if (value == null) {
                    moFieldFkTaxRegionId.setFieldValue(null);
                }
                else {
                    moFieldFkTaxRegionId.setFieldValue(value);
                }
                break;
                
            case SLibConstants.VALUE_POST_EMIT_EDIT:
                mbPostEmissionEdition = (Boolean) value;
                jbEditLogistics.setEnabled(mbPostEmissionEdition);
                jbEditNotes.setEnabled(mbPostEmissionEdition);
                break;
                
            default:
                // do nothing
        }
        
        mbUpdatingForm = false;
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

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbConceptKey) {
                actionKey();
            }
            else if (button == jbConcept) {
                actionConcept();
            }
            else if (button == jbItemBizPartnerDescription) {
                actionItemBizPartnerDescription();
            }
            else if (button == jbFkItemId) {
                actionFkItemId();
            }
            else if (button == jbSetPrepayment) {
                actionSetPrepayment();
            }
            else if (button == jbFkOriginalUnitId) {
                actionFkOriginalUnitId();
            }
            else if (button == jbOriginalPriceUnitaryCyWizard) {
                actionPriceUnitaryCyWizard();
            }
            else if (button == jbPriceHistory) {
                actionPriceHistory();
            }
            else if (button == jbFkItemReferenceId_n) {
                actionFkItemReferenceId_n();
            }
            else if (button == jbFkTaxRegionId) {
                actionFkTaxRegionId();
            }
            else if (button == jbFkThirdTaxCausingId_n) {
                actionFkThirdTaxCausingId_n();
            }
            else if (button == jbEditFkThirdTaxCausingId_n) {
                actionEditFkThirdTaxCausingId_n();
            }
            else if(button == jbEditLogistics){
                actionEditLogistics();
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
            else if(button == jbEditNotes){
                actionEditNotes();
            }
            else if (button == jbPriceNew) {
                actionPriceNew();
            }
            else if (button == jbPriceSave) {
                actionPriceSave();
            }
            else if (button == jbPriceClear) {
                actionPriceClear();
            }
            else if (button == jbGridPriceNew) {
                actionPriceNew();
            }
            else if (button == jbGridPriceEdit) {
                actionPriceEdit();
            }
            else if (button == jbGridPriceDelete) {
                actionPriceDelete();
            }
        }
        if (e.getSource() instanceof javax.swing.JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) e.getSource();

            if (toggleButton == jtbNotesFilter) {
                actionNotesFilter();
            }
            if (toggleButton == jtbGridPricesFilter) {
                actionPricesFilter();
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() instanceof javax.swing.JTextField) {
            JTextField textField = (JTextField) e.getSource();

            if (isTextFieldFoTotal(textField)) {
                mdTempCurrentValue = SLibUtilities.parseDouble(textField.getText()); // set temporal current-value
            }
            else if (textField == jtfWeightPackagingExtra) {
                // nothing to do by now...
            }
            else if (isTextFieldForContract(textField)) {
                // nothing to do by now...
            } 
        }
        else if (e.getSource() instanceof javax.swing.JComboBox) {
            JComboBox comboBox = (JComboBox) e.getSource();

            if (comboBox == jcbFkOriginalUnitId) {
                moTempCurrentUnitKey = moFieldFkOriginalUnitId.getKey(); // set temporal current-unit-key
            }
        }
    }

    @Override
    public void focusLost(java.awt.event.FocusEvent e) {
        if (e.getSource() instanceof javax.swing.JTextField) {
            JTextField textField = (JTextField) e.getSource();

            if (isTextFieldFoTotal(textField)) {
                if (mdTempCurrentValue != SLibUtilities.parseDouble(textField.getText())) {
                    calculateTotal(); // calculate only if temporal current-value has changed:
                }
                mdTempCurrentValue = 0; // clear temporal current-value
            }
            else if (textField == jtfWeightPackagingExtra) {
                calculateWeights();
            }
            else if (isTextFieldForContract(textField)) {
                calculateEntryPrice();
            } 
        }
        else if (e.getSource() instanceof javax.swing.JComboBox) {
            JComboBox comboBox = (JComboBox) e.getSource();

            if (comboBox == jcbFkOriginalUnitId) {
                if (moTempCurrentUnitKey != moFieldFkOriginalUnitId.getKey()) {
                    calculateTotal(); // calculate only if temporal current-unit-key has changed:
                }
                moTempCurrentUnitKey = null; // clear temporal current-unit-key
            }
        }
    }

    @Override
    public void itemStateChanged(java.awt.event.ItemEvent e) {
        if (!mbResetingForm && !mbUpdatingForm) {
            if (e.getSource() instanceof javax.swing.JCheckBox) {
                JCheckBox checkBox = (JCheckBox) e.getSource();

                if (checkBox == jckIsDiscountUnitaryPercentage) {
                    itemStateIsDiscountUnitaryPercentage(true);
                }
                else if (checkBox == jckIsDiscountEntryPercentage) {
                    itemStateIsDiscountEntryPercentage(true);
                }
                else if (checkBox == jckIsDiscountDocApplying) {
                    itemStateIsDiscountDocApplying(true);
                }
                else if (checkBox == jckIsSalesFreightRequired) {
                    itemStateIsSalesFreightRequired(true);
                }
                else if (checkBox == jckIsSalesFreightAddPrice) {
                    itemStateIsSalesFreightAddPrice(true);
                }
                else if (checkBox == jckIsSurplusPercentageApplying) {
                    itemStateIsSurplusPercentageApplying(true);
                }
                else if (checkBox == jckIsTaxesAutomaticApplying) {
                    itemStateIsTaxesAutomaticApplying(true);
                }
                else if (checkBox == jckIsDpsReqMonthDelivery) {
                    enablePriceContractFields(checkBox.isSelected());
                }
                else if (checkBox == jckIsDirectPrice) {
                    enableDeliveryPriceFields(checkBox.isSelected());
                }
                else if (checkBox == jckChangePrice) {
                    enableChangeEntryPrice(checkBox.isSelected());
                }
                else if (checkBox == jckAuxPreserveQuantity) {
                    calculateTotal();
                }
            }
            else if (e.getSource() instanceof javax.swing.JComboBox && e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox comboBox = (JComboBox)  e.getSource();
                
                if (comboBox == jcbFkItemId) {
                    itemChangedFkItemId(true);
                }
                else if (comboBox == jcbFkOriginalUnitId) {
                    itemChangedFkOriginalUnitId();
                }
                else if (comboBox == jcbFkTaxRegionId) {
                    itemChangedFkTaxRegionId();
                }
            }
            else if (e.getSource() instanceof javax.swing.JRadioButton && e.getStateChange() == ItemEvent.SELECTED) {
                JRadioButton radioButton = (JRadioButton) e.getSource();
                
                if (radioButton == jradAccCashAccount || radioButton == jradAccAdvanceBilled) {
                    renderAccountOptions();
                }
            }
        }
    }

    @Override
    public void editingStopped(ChangeEvent e) {
        updateDpsEntryTaxRow();
    }

    @Override
    public void editingCanceled(ChangeEvent e) {

    }
}
