/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.form;

import cfd.DCfdConsts;
import cfd.ver33.DCfdi33Catalogs;
import cfd.ver33.DCfdi33Utils;
import cfd.ver40.DCfdi40Catalogs;
import erp.cfd.SCfdXmlCatalogs;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
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
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchBankAccount;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataRecord;
import erp.mfin.form.SDialogRecordPicker;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SFinConsts;
import erp.mtrn.data.SCfdPacket;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataCfdPayment;
import erp.mtrn.data.SThinDps;
import erp.mtrn.data.STrnUtilities;
import erp.mtrn.data.cfd.SCfdPaymentEntry;
import erp.mtrn.data.cfd.SCfdPaymentEntryDoc;
import erp.redis.SLockUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridConsts;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.srv.SLock;

/**
 * User form for input of database registry of CFDI of Payments.
 * @author  Sergio Flores, Isabel Servín, Adrián Avilés
 */
public class SFormCfdPayment extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.ItemListener, java.awt.event.FocusListener, javax.swing.event.ListSelectionListener {
    
    private static final int MODE_DPS_PICK_PEND = 1;
    private static final int MODE_DPS_PICK_ALL = 2;
    private static final String CONCEPT_DOCS = "Documentos concepto contable";
    
    private final int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbIsFirstTime;
    private boolean mbIsFormReady;
    private boolean mbIsFormReadOnly;
    private boolean mbIsFormReseting;
    private boolean mbEditingVoucher; // payment voucher, that is, the very header of registry
    private boolean mbEditingPaymentEntry;
    private boolean mbEditingPaymentEntryDoc;
    private boolean mbDpsValidationFailed;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private final erp.client.SClientInterface miClient;

    private erp.mtrn.data.SDataCfdPayment moDataCfdPayment;
    private erp.mbps.data.SDataBizPartnerBranch moDataComBranch;
    private erp.mbps.data.SDataBizPartner moDataRecBizPartner;
    private erp.mtrn.data.SDataCfd moDataRecCfdRelated;
    private java.lang.String msRecCfdRelatedUuid;
    private java.lang.String msRecCfdRelatedUuidOnFocusGained;
    private java.text.DecimalFormat moExchangeRateFormat;
    
    private erp.mfin.data.SDataRecord moDataPayRecord;
    private erp.mfin.data.SDataAccountCash moDataPayAccountCashDest;
    private erp.mtrn.data.SThinDps moThinDocDpsRelated;
    private erp.mtrn.data.cfd.SCfdPaymentEntry moPaymentEntry;
    private erp.mtrn.data.cfd.SCfdPaymentEntryDoc moPaymentEntryDoc;

    private java.lang.String msXmlRelationType;
    private java.awt.Color moBackgroundDefaultColor;
    
    private SCfdXmlCatalogs moXmlCatalogs;
    
    /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
    private java.util.HashMap<java.lang.String, sa.lib.srv.SSrvLock> moRecordLocksMap;
    */
    /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
    private java.util.HashMap<java.lang.String, sa.lib.srv.redis.SRedisLock> moRecordRedisLocksMap;  
    */
    private java.util.HashMap<java.lang.String, sa.lib.srv.SLock> moRecordSLocksMap;
    private erp.mfin.form.SDialogRecordPicker moDialogPayRecordPicker;
    private erp.mtrn.form.SDialogPickerDps moDialogRecDpsRelatedPicker;
    private erp.mtrn.form.SDialogPickerDps moDialogDocDpsRelatedPickerPend;
    private erp.mtrn.form.SDialogPickerDps moDialogDocDpsRelatedPickerAll;
    private erp.mtrn.form.SDialogCfdPaymentFactoringFees moDialogCfdPaymentFactoringFees;
    private erp.lib.table.STablePaneGrid moPaneGridPayments;
    private erp.lib.table.STablePaneGrid moPaneGridPaymentDocs;
    
    // business partner:
    private erp.lib.form.SFormField moFieldRecBizPartner;
    private erp.lib.form.SFormField moFieldRecCfdiUse;
    private erp.lib.form.SFormField moFieldRecCfdRelatedUuid;
    
    // CFD:
    private erp.lib.form.SFormField moFieldVouDate;
    private erp.lib.form.SFormField moFieldVouTaxRegime;
    private erp.lib.form.SFormField moFieldVouConfirm;
    
    // current payment:
    private erp.lib.form.SFormField moFieldPayDate;
    private erp.lib.form.SFormField moFieldPayTime;
    private erp.lib.form.SFormField moFieldPayPaymentWay;
    private erp.lib.form.SFormField moFieldPayCurrency;
    private erp.lib.form.SFormField moFieldPayFactoringBank;
    private erp.lib.form.SFormField moFieldPayAmount;
    private erp.lib.form.SFormField moFieldPayExchangeRate;
    private erp.lib.form.SFormField moFieldPayOperation;
    private erp.lib.form.SFormField moFieldPayAccountSrcFiscalId;
    private erp.lib.form.SFormField moFieldPayAccountSrcNumber;
    private erp.lib.form.SFormField moFieldPayAccountSrcEntity;
    private erp.lib.form.SFormField moFieldPayAccountDestFiscalId;
    private erp.lib.form.SFormField moFieldPayAccountDestNumber;
    private erp.lib.form.SFormField moFieldPayAccountDest;
    
    // current payment document:
    private erp.lib.form.SFormField moFieldDocInstallment;
    private erp.lib.form.SFormField moFieldDocExchangeRate;
    private erp.lib.form.SFormField moFieldDocBalancePrev;
    private erp.lib.form.SFormField moFieldDocPayment;

    /**
     * Creates user form for input of database registry of CFDI of Payments.
     * @param client GUI user interface.
     */
    public SFormCfdPayment(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.TRNX_CFD_PAY_REC;
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

        bgFactoring = new javax.swing.ButtonGroup();
        jpDialog = new javax.swing.JPanel();
        jpRegistry = new javax.swing.JPanel();
        jpRegistryReceptor = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jlRecBizPartner = new javax.swing.JLabel();
        jcbRecBizPartner = new javax.swing.JComboBox();
        jbRecBizPartnerPick = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jlRecFiscalId = new javax.swing.JLabel();
        jtfRecFiscalIdRo = new javax.swing.JTextField();
        jtfRecCountryRo = new javax.swing.JTextField();
        jtfRecForeignFiscalIdRo = new javax.swing.JTextField();
        jtfRecTaxRegime = new javax.swing.JTextField();
        jbRecTaxRegime = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jlRecCfdiUsage = new javax.swing.JLabel();
        jcbRecCfdiUsage = new javax.swing.JComboBox();
        jPanel17 = new javax.swing.JPanel();
        jlRecCfdRelated = new javax.swing.JLabel();
        jtfRecCfdRelatedNumberRo = new javax.swing.JTextField();
        jtfRecCfdRelatedUuid = new javax.swing.JTextField();
        jtfRecCfdRelatedVersionRo = new javax.swing.JTextField();
        jbRecCfdRelatedPick = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jlRecRelationType = new javax.swing.JLabel();
        jtfRecRelationTypeRo = new javax.swing.JTextField();
        jbRecCfdRelatedAdd = new javax.swing.JButton();
        jbRecCfdRelatedDelete = new javax.swing.JButton();
        jpRegistryVoucher = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jlVouNumber = new javax.swing.JLabel();
        jtfVouNumberRo = new javax.swing.JTextField();
        jtfVouUuidRo = new javax.swing.JTextField();
        jtfVouVersionRo = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jlVouDate = new javax.swing.JLabel();
        jftVouDate = new javax.swing.JFormattedTextField();
        jbVouDatePick = new javax.swing.JButton();
        jtfVouDatetimeRo = new javax.swing.JTextField();
        jtfVouStatusRo = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jlVouBranch = new javax.swing.JLabel();
        jtfVouBranchNameRo = new javax.swing.JTextField();
        jtfVouBranchCodeRo = new javax.swing.JTextField();
        jtfVouPlaceIssueRo = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jlVouTaxRegime = new javax.swing.JLabel();
        jcbVouTaxRegime = new javax.swing.JComboBox();
        jPanel41 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jlVouConfirm = new javax.swing.JLabel();
        jtfVouConfirm = new javax.swing.JTextField();
        jPanel42 = new javax.swing.JPanel();
        jbVouNext = new javax.swing.JButton();
        jbVouResume = new javax.swing.JButton();
        jpRegistryRows = new javax.swing.JPanel();
        jpPayments = new javax.swing.JPanel();
        jpPayment = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jlPayDate = new javax.swing.JLabel();
        jftPayDate = new javax.swing.JFormattedTextField();
        jbPayDatePick = new javax.swing.JButton();
        jlPayTime = new javax.swing.JLabel();
        jftPayTime = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jlPayPaymentWay = new javax.swing.JLabel();
        jcbPayPaymentWay = new javax.swing.JComboBox();
        jckPayFactoring = new javax.swing.JCheckBox();
        jPanel28 = new javax.swing.JPanel();
        jlPayCurrency = new javax.swing.JLabel();
        jcbPayCurrency = new javax.swing.JComboBox();
        jrbPayFactoringPay = new javax.swing.JRadioButton();
        jrbPayFactoringFee = new javax.swing.JRadioButton();
        jPanel30 = new javax.swing.JPanel();
        jlPayOperation = new javax.swing.JLabel();
        jtfPayOperation = new javax.swing.JTextField();
        jlPayFactoringBank = new javax.swing.JLabel();
        jcbPayFactoringBank = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel29 = new javax.swing.JPanel();
        jlPayAmount = new javax.swing.JLabel();
        jtfPayAmount = new javax.swing.JTextField();
        jtfPayAmountCurRo = new javax.swing.JTextField();
        jtfPayExchangeRate = new javax.swing.JTextField();
        jbPayExchangeRatePick = new javax.swing.JButton();
        jtfPayAmountLocalRo = new javax.swing.JTextField();
        jtfPayAmountLocalCurRo = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        jlPayRecord = new javax.swing.JLabel();
        jtfPayRecordDateRo = new javax.swing.JTextField();
        jtfPayRecordBranchCodeRo = new javax.swing.JTextField();
        jtfPayRecordNumberRo = new javax.swing.JTextField();
        jbPayRecordPick = new javax.swing.JButton();
        jbPayRecordView = new javax.swing.JButton();
        jPanel40 = new javax.swing.JPanel();
        jlPayAccount = new javax.swing.JLabel();
        jlPayAccountFiscalId = new javax.swing.JLabel();
        jlPayAccountNumber = new javax.swing.JLabel();
        jlPayAccountEntity = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jlPayAccountSrc = new javax.swing.JLabel();
        jtfPayAccountSrcFiscalId = new javax.swing.JTextField();
        jtfPayAccountSrcNumber = new javax.swing.JTextField();
        jtfPayAccountSrcEntity = new javax.swing.JTextField();
        jbPayAccountSrcPick = new javax.swing.JButton();
        jPanel32 = new javax.swing.JPanel();
        jlPayAccountDest = new javax.swing.JLabel();
        jtfPayAccountDestFiscalId = new javax.swing.JTextField();
        jtfPayAccountDestNumber = new javax.swing.JTextField();
        jcbPayAccountDest = new javax.swing.JComboBox<SFormComponentItem>();
        jbPayAccountDestPick = new javax.swing.JButton();
        jPanel33 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jbPayPaymentEntryAdd = new javax.swing.JButton();
        jbPayPaymentEntryModify = new javax.swing.JButton();
        jbPayPaymentEntryDelete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jlPayCounter = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jbPayPaymentEntryOk = new javax.swing.JButton();
        jbPayPaymentEntryCancel = new javax.swing.JButton();
        jpVoucherTotals = new javax.swing.JPanel();
        jlVouTotal = new javax.swing.JLabel();
        jtfVouTotalRo = new javax.swing.JTextField();
        jtfVouTotalCurRo = new javax.swing.JTextField();
        jtfVouTotalLocalRo = new javax.swing.JTextField();
        jtfVouTotalLocalCurRo = new javax.swing.JTextField();
        jpDocuments = new javax.swing.JPanel();
        jpDocument = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jlDocDpsRelated = new javax.swing.JLabel();
        jtfDocDpsRelatedNumberRo = new javax.swing.JTextField();
        jtfDocDpsRelatedUuid = new javax.swing.JTextField();
        jtfDocDpsRelatedVersionRo = new javax.swing.JTextField();
        jbDocDpsRelatedPickPend = new javax.swing.JButton();
        jbDocDpsRelatedPickAll = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jlDocPaymentMethod = new javax.swing.JLabel();
        jtfDocPaymentMethodRo = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jlDocCurrency = new javax.swing.JLabel();
        jtfDocCurrencyRo = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        jlDocInstallment = new javax.swing.JLabel();
        jtfDocInstallment = new javax.swing.JTextField();
        jPanel35 = new javax.swing.JPanel();
        jlDocExchangeRate = new javax.swing.JLabel();
        jtfDocExchangeRate = new javax.swing.JTextField();
        jtfDocExchangeRateCurRo = new javax.swing.JTextField();
        jbDocExchangeRateInvert = new javax.swing.JButton();
        jPanel36 = new javax.swing.JPanel();
        jlDocBalancePrev = new javax.swing.JLabel();
        jtfDocDocBalancePrev = new javax.swing.JTextField();
        jtfDocDocBalancePrevCurRo = new javax.swing.JTextField();
        jtfDocPayBalancePrevRo = new javax.swing.JTextField();
        jtfDocPayBalancePrevCurRo = new javax.swing.JTextField();
        jPanel37 = new javax.swing.JPanel();
        jlDocPayment = new javax.swing.JLabel();
        jtfDocDocPayment = new javax.swing.JTextField();
        jtfDocDocPaymentCurRo = new javax.swing.JTextField();
        jtfDocPayPaymentRo = new javax.swing.JTextField();
        jtfDocPayPaymentCurRo = new javax.swing.JTextField();
        jbDocPaymentCompute = new javax.swing.JButton();
        jPanel38 = new javax.swing.JPanel();
        jlDocBalancePend = new javax.swing.JLabel();
        jtfDocDocBalancePend = new javax.swing.JTextField();
        jtfDocDocBalancePendCurRo = new javax.swing.JTextField();
        jtfDocPayBalancePendRo = new javax.swing.JTextField();
        jtfDocPayBalancePendCurRo = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jlDocPaymentType = new javax.swing.JLabel();
        jtfDocPaymentTypeRo = new javax.swing.JTextField();
        jPanel39 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jbDocPaymentEntryDocAdd = new javax.swing.JButton();
        jbDocPaymentEntryDocModify = new javax.swing.JButton();
        jbDocPaymentEntryDocDelete = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jlDocCounter = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jbDocPaymentEntryDocOk = new javax.swing.JButton();
        jbDocPaymentEntryDocCancel = new javax.swing.JButton();
        jpPaymentTotals = new javax.swing.JPanel();
        jlPayTotalPayments = new javax.swing.JLabel();
        jtfPayTotalPaymentsRo = new javax.swing.JTextField();
        jtfPayTotalPaymentsCurRo = new javax.swing.JTextField();
        jtfPayTotalPaymentsLocalRo = new javax.swing.JTextField();
        jtfPayTotalPaymentsLocalCurRo = new javax.swing.JTextField();
        jpAccountingConcept = new javax.swing.JPanel();
        jtfAccConceptPrefixRo = new javax.swing.JTextField();
        jtfAccConceptDocsRo = new javax.swing.JTextField();
        jbAccConceptDocsEdit = new javax.swing.JButton();
        jpControls = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("CFDI recepción de pagos"); // NOI18N
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

        jpDialog.setLayout(new java.awt.BorderLayout());

        jpRegistry.setLayout(new java.awt.BorderLayout(5, 0));

        jpRegistryReceptor.setBorder(javax.swing.BorderFactory.createTitledBorder("Deudor:"));
        jpRegistryReceptor.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRecBizPartner.setText("Deudor:*");
        jlRecBizPartner.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlRecBizPartner);

        jcbRecBizPartner.setMaximumRowCount(16);
        jcbRecBizPartner.setPreferredSize(new java.awt.Dimension(360, 23));
        jPanel11.add(jcbRecBizPartner);

        jbRecBizPartnerPick.setText("...");
        jbRecBizPartnerPick.setToolTipText("Seleccionar asociado de negocios");
        jbRecBizPartnerPick.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbRecBizPartnerPick);

        jpRegistryReceptor.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRecFiscalId.setText("RFC:");
        jlRecFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlRecFiscalId);

        jtfRecFiscalIdRo.setEditable(false);
        jtfRecFiscalIdRo.setText("XAXX010101000");
        jtfRecFiscalIdRo.setToolTipText("RFC");
        jtfRecFiscalIdRo.setFocusable(false);
        jtfRecFiscalIdRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jtfRecFiscalIdRo);

        jtfRecCountryRo.setEditable(false);
        jtfRecCountryRo.setText("XXX");
        jtfRecCountryRo.setToolTipText("País de residencia");
        jtfRecCountryRo.setFocusable(false);
        jtfRecCountryRo.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel12.add(jtfRecCountryRo);

        jtfRecForeignFiscalIdRo.setEditable(false);
        jtfRecForeignFiscalIdRo.setText("TEXT");
        jtfRecForeignFiscalIdRo.setToolTipText("ID tributario");
        jtfRecForeignFiscalIdRo.setFocusable(false);
        jtfRecForeignFiscalIdRo.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel12.add(jtfRecForeignFiscalIdRo);

        jtfRecTaxRegime.setEditable(false);
        jtfRecTaxRegime.setText("TEXT");
        jtfRecTaxRegime.setToolTipText("Regimén fiscal receptor");
        jtfRecTaxRegime.setEnabled(false);
        jtfRecTaxRegime.setFocusable(false);
        jtfRecTaxRegime.setPreferredSize(new java.awt.Dimension(45, 23));
        jPanel12.add(jtfRecTaxRegime);

        jbRecTaxRegime.setText("...");
        jbRecTaxRegime.setToolTipText("Habilitar regimen fiscal receptor");
        jbRecTaxRegime.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel12.add(jbRecTaxRegime);

        jpRegistryReceptor.add(jPanel12);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRecCfdiUsage.setText("Uso CFDI:*");
        jlRecCfdiUsage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel15.add(jlRecCfdiUsage);

        jcbRecCfdiUsage.setMaximumRowCount(16);
        jcbRecCfdiUsage.setPreferredSize(new java.awt.Dimension(360, 23));
        jPanel15.add(jcbRecCfdiUsage);

        jpRegistryReceptor.add(jPanel15);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRecCfdRelated.setText("Doc. relacionado:*");
        jlRecCfdRelated.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel17.add(jlRecCfdRelated);

        jtfRecCfdRelatedNumberRo.setEditable(false);
        jtfRecCfdRelatedNumberRo.setText("A-999999");
        jtfRecCfdRelatedNumberRo.setToolTipText("Serie y folio");
        jtfRecCfdRelatedNumberRo.setFocusable(false);
        jtfRecCfdRelatedNumberRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel17.add(jtfRecCfdRelatedNumberRo);

        jtfRecCfdRelatedUuid.setText("402A8A38-B980-412A-9485-29222D7095C4");
        jtfRecCfdRelatedUuid.setToolTipText("UUID");
        jtfRecCfdRelatedUuid.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel17.add(jtfRecCfdRelatedUuid);

        jtfRecCfdRelatedVersionRo.setEditable(false);
        jtfRecCfdRelatedVersionRo.setText("0.0");
        jtfRecCfdRelatedVersionRo.setToolTipText("Versión del CFD");
        jtfRecCfdRelatedVersionRo.setFocusable(false);
        jtfRecCfdRelatedVersionRo.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel17.add(jtfRecCfdRelatedVersionRo);

        jbRecCfdRelatedPick.setText("...");
        jbRecCfdRelatedPick.setToolTipText("Seleccionar doc. relacionado");
        jbRecCfdRelatedPick.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel17.add(jbRecCfdRelatedPick);

        jpRegistryReceptor.add(jPanel17);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRecRelationType.setText("Tipo relación:");
        jlRecRelationType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel16.add(jlRecRelationType);

        jtfRecRelationTypeRo.setEditable(false);
        jtfRecRelationTypeRo.setText("TEXT");
        jtfRecRelationTypeRo.setFocusable(false);
        jtfRecRelationTypeRo.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel16.add(jtfRecRelationTypeRo);

        jbRecCfdRelatedAdd.setText("Agregar");
        jbRecCfdRelatedAdd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel16.add(jbRecCfdRelatedAdd);

        jbRecCfdRelatedDelete.setText("Eliminar");
        jbRecCfdRelatedDelete.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel16.add(jbRecCfdRelatedDelete);

        jpRegistryReceptor.add(jPanel16);

        jpRegistry.add(jpRegistryReceptor, java.awt.BorderLayout.CENTER);

        jpRegistryVoucher.setBorder(javax.swing.BorderFactory.createTitledBorder("Comprobante:"));
        jpRegistryVoucher.setPreferredSize(new java.awt.Dimension(525, 1));
        jpRegistryVoucher.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVouNumber.setText("Folio:");
        jlVouNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlVouNumber);

        jtfVouNumberRo.setEditable(false);
        jtfVouNumberRo.setText("A-999999");
        jtfVouNumberRo.setToolTipText("Serie y folio");
        jtfVouNumberRo.setFocusable(false);
        jtfVouNumberRo.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel5.add(jtfVouNumberRo);

        jtfVouUuidRo.setEditable(false);
        jtfVouUuidRo.setText("402A8A38-B980-412A-9485-29222D7095C4");
        jtfVouUuidRo.setToolTipText("UUID");
        jtfVouUuidRo.setFocusable(false);
        jtfVouUuidRo.setPreferredSize(new java.awt.Dimension(230, 23));
        jPanel5.add(jtfVouUuidRo);

        jtfVouVersionRo.setEditable(false);
        jtfVouVersionRo.setText("0.0");
        jtfVouVersionRo.setToolTipText("Versión del CFD");
        jtfVouVersionRo.setFocusable(false);
        jtfVouVersionRo.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel5.add(jtfVouVersionRo);

        jpRegistryVoucher.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVouDate.setText("Fecha:");
        jlVouDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlVouDate);

        jftVouDate.setText("01/01/2001");
        jftVouDate.setToolTipText("Fecha");
        jftVouDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel6.add(jftVouDate);

        jbVouDatePick.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbVouDatePick.setToolTipText("Seleccionar fecha");
        jbVouDatePick.setFocusable(false);
        jbVouDatePick.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbVouDatePick);

        jtfVouDatetimeRo.setEditable(false);
        jtfVouDatetimeRo.setText("01/01/2001 00:00:00");
        jtfVouDatetimeRo.setFocusable(false);
        jtfVouDatetimeRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jtfVouDatetimeRo);

        jtfVouStatusRo.setEditable(false);
        jtfVouStatusRo.setText("TEXT");
        jtfVouStatusRo.setToolTipText("Estatus");
        jtfVouStatusRo.setFocusable(false);
        jtfVouStatusRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jtfVouStatusRo);

        jpRegistryVoucher.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVouBranch.setText("Sucursal empresa:");
        jlVouBranch.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlVouBranch);

        jtfVouBranchNameRo.setEditable(false);
        jtfVouBranchNameRo.setText("TEXT");
        jtfVouBranchNameRo.setToolTipText("Nombre de la sucursal");
        jtfVouBranchNameRo.setFocusable(false);
        jtfVouBranchNameRo.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel7.add(jtfVouBranchNameRo);

        jtfVouBranchCodeRo.setEditable(false);
        jtfVouBranchCodeRo.setText("XXX");
        jtfVouBranchCodeRo.setToolTipText("Clave de la sucursal");
        jtfVouBranchCodeRo.setFocusable(false);
        jtfVouBranchCodeRo.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel7.add(jtfVouBranchCodeRo);

        jtfVouPlaceIssueRo.setEditable(false);
        jtfVouPlaceIssueRo.setText("99999");
        jtfVouPlaceIssueRo.setToolTipText("Lugar de expedición");
        jtfVouPlaceIssueRo.setFocusable(false);
        jtfVouPlaceIssueRo.setPreferredSize(new java.awt.Dimension(78, 23));
        jPanel7.add(jtfVouPlaceIssueRo);

        jpRegistryVoucher.add(jPanel7);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVouTaxRegime.setText("Régimen fiscal:*");
        jlVouTaxRegime.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlVouTaxRegime);

        jcbVouTaxRegime.setMaximumRowCount(16);
        jcbVouTaxRegime.setPreferredSize(new java.awt.Dimension(363, 23));
        jPanel9.add(jcbVouTaxRegime);

        jpRegistryVoucher.add(jPanel9);

        jPanel41.setLayout(new java.awt.GridLayout(1, 2));

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVouConfirm.setText("Confirmación:");
        jlVouConfirm.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlVouConfirm);

        jtfVouConfirm.setText("TEXT");
        jtfVouConfirm.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jtfVouConfirm);

        jPanel41.add(jPanel10);

        jPanel42.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbVouNext.setText("Continuar");
        jbVouNext.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbVouNext.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel42.add(jbVouNext);

        jbVouResume.setText("Reiniciar");
        jbVouResume.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbVouResume.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel42.add(jbVouResume);

        jPanel41.add(jPanel42);

        jpRegistryVoucher.add(jPanel41);

        jpRegistry.add(jpRegistryVoucher, java.awt.BorderLayout.EAST);

        jpDialog.add(jpRegistry, java.awt.BorderLayout.NORTH);

        jpRegistryRows.setLayout(new java.awt.BorderLayout(5, 0));

        jpPayments.setBorder(javax.swing.BorderFactory.createTitledBorder("Pagos:"));
        jpPayments.setLayout(new java.awt.BorderLayout(0, 5));

        jpPayment.setLayout(new java.awt.GridLayout(10, 1, 0, 5));

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayDate.setText("Fecha pago:*");
        jlPayDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel25.add(jlPayDate);

        jftPayDate.setText("01/01/2001");
        jftPayDate.setToolTipText("Fecha");
        jftPayDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel25.add(jftPayDate);

        jbPayDatePick.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbPayDatePick.setToolTipText("Seleccionar fecha");
        jbPayDatePick.setFocusable(false);
        jbPayDatePick.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel25.add(jbPayDatePick);

        jlPayTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlPayTime.setText("Hora pago:*");
        jlPayTime.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel25.add(jlPayTime);

        jftPayTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm:ss"))));
        jftPayTime.setText("01:01:01");
        jftPayTime.setToolTipText("Hora (hh:mm:ss, 24h)");
        jftPayTime.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel25.add(jftPayTime);

        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("hh:mm:ss (24 h)");
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel25.add(jLabel2);

        jpPayment.add(jPanel25);

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayPaymentWay.setText("Forma pago:*");
        jlPayPaymentWay.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel26.add(jlPayPaymentWay);

        jcbPayPaymentWay.setMaximumRowCount(16);
        jcbPayPaymentWay.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel26.add(jcbPayPaymentWay);

        jckPayFactoring.setText("Pago con factoraje:");
        jckPayFactoring.setPreferredSize(new java.awt.Dimension(205, 23));
        jPanel26.add(jckPayFactoring);

        jpPayment.add(jPanel26);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayCurrency.setBackground(new java.awt.Color(204, 255, 204));
        jlPayCurrency.setText("Moneda pago:*");
        jlPayCurrency.setOpaque(true);
        jlPayCurrency.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel28.add(jlPayCurrency);

        jcbPayCurrency.setMaximumRowCount(16);
        jcbPayCurrency.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel28.add(jcbPayCurrency);

        bgFactoring.add(jrbPayFactoringPay);
        jrbPayFactoringPay.setText("Pago");
        jrbPayFactoringPay.setToolTipText("Pago");
        jrbPayFactoringPay.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel28.add(jrbPayFactoringPay);

        bgFactoring.add(jrbPayFactoringFee);
        jrbPayFactoringFee.setText("Intereses y comisiones");
        jrbPayFactoringFee.setToolTipText("Intereses y comisiones");
        jrbPayFactoringFee.setPreferredSize(new java.awt.Dimension(145, 23));
        jPanel28.add(jrbPayFactoringFee);

        jpPayment.add(jPanel28);

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayOperation.setText("Núm. operación:");
        jlPayOperation.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel30.add(jlPayOperation);

        jtfPayOperation.setText("TEXT");
        jtfPayOperation.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel30.add(jtfPayOperation);

        jlPayFactoringBank.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlPayFactoringBank.setText("Banco:");
        jlPayFactoringBank.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel30.add(jlPayFactoringBank);

        jcbPayFactoringBank.setToolTipText("Banco factoraje");
        jcbPayFactoringBank.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel30.add(jcbPayFactoringBank);

        jpPayment.add(jPanel30);

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayAmount.setText("Monto pago:*");
        jlPayAmount.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel29.add(jlPayAmount);

        jtfPayAmount.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayAmount.setText("999,999,999.99");
        jtfPayAmount.setToolTipText("Monto de pago");
        jtfPayAmount.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel29.add(jtfPayAmount);

        jtfPayAmountCurRo.setEditable(false);
        jtfPayAmountCurRo.setBackground(new java.awt.Color(204, 255, 204));
        jtfPayAmountCurRo.setText("PAY");
        jtfPayAmountCurRo.setFocusable(false);
        jtfPayAmountCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel29.add(jtfPayAmountCurRo);

        jtfPayExchangeRate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayExchangeRate.setText("999,999.99");
        jtfPayExchangeRate.setToolTipText("Tipo de cambio");
        jtfPayExchangeRate.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel29.add(jtfPayExchangeRate);

        jbPayExchangeRatePick.setText("...");
        jbPayExchangeRatePick.setToolTipText("Seleccionar tipo de cambio");
        jbPayExchangeRatePick.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel29.add(jbPayExchangeRatePick);

        jtfPayAmountLocalRo.setEditable(false);
        jtfPayAmountLocalRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayAmountLocalRo.setText("999,999,999.99");
        jtfPayAmountLocalRo.setToolTipText("Monto de pago");
        jtfPayAmountLocalRo.setFocusable(false);
        jtfPayAmountLocalRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel29.add(jtfPayAmountLocalRo);

        jtfPayAmountLocalCurRo.setEditable(false);
        jtfPayAmountLocalCurRo.setBackground(new java.awt.Color(255, 204, 204));
        jtfPayAmountLocalCurRo.setText("LOC");
        jtfPayAmountLocalCurRo.setFocusable(false);
        jtfPayAmountLocalCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel29.add(jtfPayAmountLocalCurRo);

        jpPayment.add(jPanel29);

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayRecord.setText("Póliza contable:*");
        jlPayRecord.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel27.add(jlPayRecord);

        jtfPayRecordDateRo.setEditable(false);
        jtfPayRecordDateRo.setText("01/01/2001");
        jtfPayRecordDateRo.setToolTipText("Fecha de la póliza contable");
        jtfPayRecordDateRo.setFocusable(false);
        jtfPayRecordDateRo.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel27.add(jtfPayRecordDateRo);

        jtfPayRecordBranchCodeRo.setEditable(false);
        jtfPayRecordBranchCodeRo.setText("XXX");
        jtfPayRecordBranchCodeRo.setToolTipText("Sucursal de la empresa");
        jtfPayRecordBranchCodeRo.setFocusable(false);
        jtfPayRecordBranchCodeRo.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel27.add(jtfPayRecordBranchCodeRo);

        jtfPayRecordNumberRo.setEditable(false);
        jtfPayRecordNumberRo.setText("TP-000001");
        jtfPayRecordNumberRo.setToolTipText("Número de póliza contable");
        jtfPayRecordNumberRo.setFocusable(false);
        jtfPayRecordNumberRo.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel27.add(jtfPayRecordNumberRo);

        jbPayRecordPick.setText("...");
        jbPayRecordPick.setToolTipText("Seleccionar póliza contable");
        jbPayRecordPick.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel27.add(jbPayRecordPick);

        jbPayRecordView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif"))); // NOI18N
        jbPayRecordView.setToolTipText("Ver póliza contable");
        jbPayRecordView.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel27.add(jbPayRecordView);

        jpPayment.add(jPanel27);

        jPanel40.setBackground(java.awt.SystemColor.controlHighlight);
        jPanel40.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayAccount.setText("Cta. bancaria:");
        jlPayAccount.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel40.add(jlPayAccount);

        jlPayAccountFiscalId.setText("RFC banco:");
        jlPayAccountFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel40.add(jlPayAccountFiscalId);

        jlPayAccountNumber.setText("Núm. cta. bancaria:");
        jlPayAccountNumber.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel40.add(jlPayAccountNumber);

        jlPayAccountEntity.setText("Banco:");
        jlPayAccountEntity.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel40.add(jlPayAccountEntity);

        jpPayment.add(jPanel40);

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayAccountSrc.setText("Ordenante:");
        jlPayAccountSrc.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel31.add(jlPayAccountSrc);

        jtfPayAccountSrcFiscalId.setText("XAXX010101000");
        jtfPayAccountSrcFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel31.add(jtfPayAccountSrcFiscalId);

        jtfPayAccountSrcNumber.setText("072470001837637520");
        jtfPayAccountSrcNumber.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel31.add(jtfPayAccountSrcNumber);

        jtfPayAccountSrcEntity.setText("TEXT");
        jtfPayAccountSrcEntity.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel31.add(jtfPayAccountSrcEntity);

        jbPayAccountSrcPick.setText("...");
        jbPayAccountSrcPick.setToolTipText("Seleccionar cuenta ordenante");
        jbPayAccountSrcPick.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel31.add(jbPayAccountSrcPick);

        jpPayment.add(jPanel31);

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayAccountDest.setText("Beneficiaria:*");
        jlPayAccountDest.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel32.add(jlPayAccountDest);

        jtfPayAccountDestFiscalId.setEditable(false);
        jtfPayAccountDestFiscalId.setText("XAXX010101000");
        jtfPayAccountDestFiscalId.setFocusable(false);
        jtfPayAccountDestFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel32.add(jtfPayAccountDestFiscalId);

        jtfPayAccountDestNumber.setEditable(false);
        jtfPayAccountDestNumber.setText("072470001837637520");
        jtfPayAccountDestNumber.setFocusable(false);
        jtfPayAccountDestNumber.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel32.add(jtfPayAccountDestNumber);

        jcbPayAccountDest.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel32.add(jcbPayAccountDest);

        jbPayAccountDestPick.setText("...");
        jbPayAccountDestPick.setToolTipText("Seleccionar cuenta beneficiaria");
        jbPayAccountDestPick.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel32.add(jbPayAccountDestPick);

        jpPayment.add(jPanel32);

        jPanel33.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbPayPaymentEntryAdd.setText("Agregar");
        jbPayPaymentEntryAdd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbPayPaymentEntryAdd);

        jbPayPaymentEntryModify.setText("Modificar");
        jPanel1.add(jbPayPaymentEntryModify);

        jbPayPaymentEntryDelete.setText("Eliminar");
        jbPayPaymentEntryDelete.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbPayPaymentEntryDelete);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setPreferredSize(new java.awt.Dimension(5, 23));
        jPanel1.add(jSeparator1);

        jlPayCounter.setText("Pagos: 0");
        jlPayCounter.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jlPayCounter);

        jPanel33.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbPayPaymentEntryOk.setText("Aceptar"); // NOI18N
        jbPayPaymentEntryOk.setToolTipText("[Ctrl + Enter]");
        jbPayPaymentEntryOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel3.add(jbPayPaymentEntryOk);

        jbPayPaymentEntryCancel.setText("Cancelar"); // NOI18N
        jbPayPaymentEntryCancel.setToolTipText("[Escape]");
        jPanel3.add(jbPayPaymentEntryCancel);

        jPanel33.add(jPanel3, java.awt.BorderLayout.EAST);

        jpPayment.add(jPanel33);

        jpPayments.add(jpPayment, java.awt.BorderLayout.NORTH);

        jpVoucherTotals.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlVouTotal.setText("Total:");
        jlVouTotal.setPreferredSize(new java.awt.Dimension(50, 23));
        jpVoucherTotals.add(jlVouTotal);

        jtfVouTotalRo.setEditable(false);
        jtfVouTotalRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfVouTotalRo.setText("999,999,999.99");
        jtfVouTotalRo.setFocusable(false);
        jtfVouTotalRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jpVoucherTotals.add(jtfVouTotalRo);

        jtfVouTotalCurRo.setEditable(false);
        jtfVouTotalCurRo.setText("XXX");
        jtfVouTotalCurRo.setFocusable(false);
        jtfVouTotalCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jpVoucherTotals.add(jtfVouTotalCurRo);

        jtfVouTotalLocalRo.setEditable(false);
        jtfVouTotalLocalRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfVouTotalLocalRo.setText("999,999,999.99");
        jtfVouTotalLocalRo.setFocusable(false);
        jtfVouTotalLocalRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jpVoucherTotals.add(jtfVouTotalLocalRo);

        jtfVouTotalLocalCurRo.setEditable(false);
        jtfVouTotalLocalCurRo.setBackground(new java.awt.Color(255, 204, 204));
        jtfVouTotalLocalCurRo.setText("LOC");
        jtfVouTotalLocalCurRo.setFocusable(false);
        jtfVouTotalLocalCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jpVoucherTotals.add(jtfVouTotalLocalCurRo);

        jpPayments.add(jpVoucherTotals, java.awt.BorderLayout.SOUTH);

        jpRegistryRows.add(jpPayments, java.awt.BorderLayout.CENTER);

        jpDocuments.setBorder(javax.swing.BorderFactory.createTitledBorder("Documentos relacionados del pago:"));
        jpDocuments.setPreferredSize(new java.awt.Dimension(525, 1));
        jpDocuments.setLayout(new java.awt.BorderLayout(0, 5));

        jpDocument.setLayout(new java.awt.GridLayout(10, 1, 0, 5));

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocDpsRelated.setText("Doc. relacionado:*");
        jlDocDpsRelated.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlDocDpsRelated);

        jtfDocDpsRelatedNumberRo.setEditable(false);
        jtfDocDpsRelatedNumberRo.setText("A-999999");
        jtfDocDpsRelatedNumberRo.setToolTipText("Serie y folio");
        jtfDocDpsRelatedNumberRo.setFocusable(false);
        jtfDocDpsRelatedNumberRo.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel14.add(jtfDocDpsRelatedNumberRo);

        jtfDocDpsRelatedUuid.setEditable(false);
        jtfDocDpsRelatedUuid.setText("402A8A38-B980-412A-9485-29222D7095C4");
        jtfDocDpsRelatedUuid.setToolTipText("UUID");
        jtfDocDpsRelatedUuid.setFocusable(false);
        jtfDocDpsRelatedUuid.setPreferredSize(new java.awt.Dimension(230, 23));
        jPanel14.add(jtfDocDpsRelatedUuid);

        jtfDocDpsRelatedVersionRo.setEditable(false);
        jtfDocDpsRelatedVersionRo.setText("0.0");
        jtfDocDpsRelatedVersionRo.setToolTipText("Versión del CFD");
        jtfDocDpsRelatedVersionRo.setFocusable(false);
        jtfDocDpsRelatedVersionRo.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel14.add(jtfDocDpsRelatedVersionRo);

        jbDocDpsRelatedPickPend.setText("...");
        jbDocDpsRelatedPickPend.setToolTipText("Seleccionar doc. relacionado con saldo");
        jbDocDpsRelatedPickPend.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel14.add(jbDocDpsRelatedPickPend);

        jbDocDpsRelatedPickAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif"))); // NOI18N
        jbDocDpsRelatedPickAll.setToolTipText("Seleccionar doc. relacionado (cualquiera)");
        jbDocDpsRelatedPickAll.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel14.add(jbDocDpsRelatedPickAll);

        jpDocument.add(jPanel14);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocPaymentMethod.setText("Método pago:");
        jlDocPaymentMethod.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel21.add(jlDocPaymentMethod);

        jtfDocPaymentMethodRo.setEditable(false);
        jtfDocPaymentMethodRo.setText("TEXT");
        jtfDocPaymentMethodRo.setFocusable(false);
        jtfDocPaymentMethodRo.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel21.add(jtfDocPaymentMethodRo);

        jpDocument.add(jPanel21);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocCurrency.setBackground(new java.awt.Color(255, 255, 204));
        jlDocCurrency.setText("Moneda doc.:");
        jlDocCurrency.setOpaque(true);
        jlDocCurrency.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel22.add(jlDocCurrency);

        jtfDocCurrencyRo.setEditable(false);
        jtfDocCurrencyRo.setText("TEXT");
        jtfDocCurrencyRo.setFocusable(false);
        jtfDocCurrencyRo.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel22.add(jtfDocCurrencyRo);

        jpDocument.add(jPanel22);

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocInstallment.setText("Parcialidad:*");
        jlDocInstallment.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel34.add(jlDocInstallment);

        jtfDocInstallment.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocInstallment.setText("0");
        jtfDocInstallment.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel34.add(jtfDocInstallment);

        jpDocument.add(jPanel34);

        jPanel35.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocExchangeRate.setText("Tipo cambio:*");
        jlDocExchangeRate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel35.add(jlDocExchangeRate);

        jtfDocExchangeRate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocExchangeRate.setText("999,999.99");
        jtfDocExchangeRate.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel35.add(jtfDocExchangeRate);

        jtfDocExchangeRateCurRo.setEditable(false);
        jtfDocExchangeRateCurRo.setText("DOC/PAY");
        jtfDocExchangeRateCurRo.setToolTipText("");
        jtfDocExchangeRateCurRo.setFocusable(false);
        jtfDocExchangeRateCurRo.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel35.add(jtfDocExchangeRateCurRo);

        jbDocExchangeRateInvert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_exc_rate.gif"))); // NOI18N
        jbDocExchangeRateInvert.setToolTipText("Invertir tipo de cambio (1/x)");
        jbDocExchangeRateInvert.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel35.add(jbDocExchangeRateInvert);

        jpDocument.add(jPanel35);

        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocBalancePrev.setText("Saldo anterior:*");
        jlDocBalancePrev.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel36.add(jlDocBalancePrev);

        jtfDocDocBalancePrev.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocDocBalancePrev.setText("999,999,999.99");
        jtfDocDocBalancePrev.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel36.add(jtfDocDocBalancePrev);

        jtfDocDocBalancePrevCurRo.setEditable(false);
        jtfDocDocBalancePrevCurRo.setBackground(new java.awt.Color(255, 255, 204));
        jtfDocDocBalancePrevCurRo.setText("DOC");
        jtfDocDocBalancePrevCurRo.setFocusable(false);
        jtfDocDocBalancePrevCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel36.add(jtfDocDocBalancePrevCurRo);

        jtfDocPayBalancePrevRo.setEditable(false);
        jtfDocPayBalancePrevRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocPayBalancePrevRo.setText("999,999,999.99");
        jtfDocPayBalancePrevRo.setFocusable(false);
        jtfDocPayBalancePrevRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel36.add(jtfDocPayBalancePrevRo);

        jtfDocPayBalancePrevCurRo.setEditable(false);
        jtfDocPayBalancePrevCurRo.setBackground(new java.awt.Color(204, 255, 204));
        jtfDocPayBalancePrevCurRo.setText("PAY");
        jtfDocPayBalancePrevCurRo.setFocusable(false);
        jtfDocPayBalancePrevCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel36.add(jtfDocPayBalancePrevCurRo);

        jpDocument.add(jPanel36);

        jPanel37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocPayment.setText("Importe pagado:*");
        jlDocPayment.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel37.add(jlDocPayment);

        jtfDocDocPayment.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocDocPayment.setText("999,999,999.99");
        jtfDocDocPayment.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel37.add(jtfDocDocPayment);

        jtfDocDocPaymentCurRo.setEditable(false);
        jtfDocDocPaymentCurRo.setBackground(new java.awt.Color(255, 255, 204));
        jtfDocDocPaymentCurRo.setText("DOC");
        jtfDocDocPaymentCurRo.setFocusable(false);
        jtfDocDocPaymentCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel37.add(jtfDocDocPaymentCurRo);

        jtfDocPayPaymentRo.setEditable(false);
        jtfDocPayPaymentRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocPayPaymentRo.setText("999,999,999.99");
        jtfDocPayPaymentRo.setFocusable(false);
        jtfDocPayPaymentRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel37.add(jtfDocPayPaymentRo);

        jtfDocPayPaymentCurRo.setEditable(false);
        jtfDocPayPaymentCurRo.setBackground(new java.awt.Color(204, 255, 204));
        jtfDocPayPaymentCurRo.setText("PAY");
        jtfDocPayPaymentCurRo.setFocusable(false);
        jtfDocPayPaymentCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel37.add(jtfDocPayPaymentCurRo);

        jbDocPaymentCompute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_wizard.gif"))); // NOI18N
        jbDocPaymentCompute.setToolTipText("Asignar importe disponible");
        jbDocPaymentCompute.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel37.add(jbDocPaymentCompute);

        jpDocument.add(jPanel37);

        jPanel38.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocBalancePend.setText("Saldo insoluto:");
        jlDocBalancePend.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel38.add(jlDocBalancePend);

        jtfDocDocBalancePend.setEditable(false);
        jtfDocDocBalancePend.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocDocBalancePend.setText("999,999,999.99");
        jtfDocDocBalancePend.setFocusable(false);
        jtfDocDocBalancePend.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel38.add(jtfDocDocBalancePend);

        jtfDocDocBalancePendCurRo.setEditable(false);
        jtfDocDocBalancePendCurRo.setBackground(new java.awt.Color(255, 255, 204));
        jtfDocDocBalancePendCurRo.setText("DOC");
        jtfDocDocBalancePendCurRo.setFocusable(false);
        jtfDocDocBalancePendCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel38.add(jtfDocDocBalancePendCurRo);

        jtfDocPayBalancePendRo.setEditable(false);
        jtfDocPayBalancePendRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocPayBalancePendRo.setText("999,999,999.99");
        jtfDocPayBalancePendRo.setFocusable(false);
        jtfDocPayBalancePendRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel38.add(jtfDocPayBalancePendRo);

        jtfDocPayBalancePendCurRo.setEditable(false);
        jtfDocPayBalancePendCurRo.setBackground(new java.awt.Color(204, 255, 204));
        jtfDocPayBalancePendCurRo.setText("PAY");
        jtfDocPayBalancePendCurRo.setFocusable(false);
        jtfDocPayBalancePendCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel38.add(jtfDocPayBalancePendCurRo);

        jpDocument.add(jPanel38);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocPaymentType.setText("Tipo pago:");
        jlDocPaymentType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.add(jlDocPaymentType);

        jtfDocPaymentTypeRo.setEditable(false);
        jtfDocPaymentTypeRo.setText("TEXT");
        jtfDocPaymentTypeRo.setFocusable(false);
        jtfDocPaymentTypeRo.setPreferredSize(new java.awt.Dimension(275, 23));
        jPanel13.add(jtfDocPaymentTypeRo);

        jpDocument.add(jPanel13);

        jPanel39.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbDocPaymentEntryDocAdd.setText("Agregar");
        jbDocPaymentEntryDocAdd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jbDocPaymentEntryDocAdd);

        jbDocPaymentEntryDocModify.setText("Modificar");
        jPanel2.add(jbDocPaymentEntryDocModify);

        jbDocPaymentEntryDocDelete.setText("Eliminar");
        jbDocPaymentEntryDocDelete.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jbDocPaymentEntryDocDelete);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setPreferredSize(new java.awt.Dimension(5, 23));
        jPanel2.add(jSeparator2);

        jlDocCounter.setText("Docs. 0");
        jlDocCounter.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jlDocCounter);

        jPanel39.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbDocPaymentEntryDocOk.setText("Aceptar"); // NOI18N
        jbDocPaymentEntryDocOk.setToolTipText("[Ctrl + Enter]");
        jbDocPaymentEntryDocOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(jbDocPaymentEntryDocOk);

        jbDocPaymentEntryDocCancel.setText("Cancelar"); // NOI18N
        jbDocPaymentEntryDocCancel.setToolTipText("[Escape]");
        jPanel4.add(jbDocPaymentEntryDocCancel);

        jPanel39.add(jPanel4, java.awt.BorderLayout.EAST);

        jpDocument.add(jPanel39);

        jpDocuments.add(jpDocument, java.awt.BorderLayout.NORTH);

        jpPaymentTotals.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayTotalPayments.setText("Pagos:");
        jlPayTotalPayments.setPreferredSize(new java.awt.Dimension(50, 23));
        jpPaymentTotals.add(jlPayTotalPayments);

        jtfPayTotalPaymentsRo.setEditable(false);
        jtfPayTotalPaymentsRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayTotalPaymentsRo.setText("999,999,999.99");
        jtfPayTotalPaymentsRo.setFocusable(false);
        jtfPayTotalPaymentsRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jpPaymentTotals.add(jtfPayTotalPaymentsRo);

        jtfPayTotalPaymentsCurRo.setEditable(false);
        jtfPayTotalPaymentsCurRo.setBackground(new java.awt.Color(204, 255, 204));
        jtfPayTotalPaymentsCurRo.setText("PAY");
        jtfPayTotalPaymentsCurRo.setFocusable(false);
        jtfPayTotalPaymentsCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jpPaymentTotals.add(jtfPayTotalPaymentsCurRo);

        jtfPayTotalPaymentsLocalRo.setEditable(false);
        jtfPayTotalPaymentsLocalRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayTotalPaymentsLocalRo.setText("999,999,999.99");
        jtfPayTotalPaymentsLocalRo.setFocusable(false);
        jtfPayTotalPaymentsLocalRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jpPaymentTotals.add(jtfPayTotalPaymentsLocalRo);

        jtfPayTotalPaymentsLocalCurRo.setEditable(false);
        jtfPayTotalPaymentsLocalCurRo.setBackground(new java.awt.Color(255, 204, 204));
        jtfPayTotalPaymentsLocalCurRo.setText("LOC");
        jtfPayTotalPaymentsLocalCurRo.setFocusable(false);
        jtfPayTotalPaymentsLocalCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jpPaymentTotals.add(jtfPayTotalPaymentsLocalCurRo);

        jpAccountingConcept.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        jtfAccConceptPrefixRo.setEditable(false);
        jtfAccConceptPrefixRo.setText("F");
        jtfAccConceptPrefixRo.setToolTipText("Prefijo documentos concepto contable");
        jtfAccConceptPrefixRo.setFocusable(false);
        jtfAccConceptPrefixRo.setPreferredSize(new java.awt.Dimension(15, 23));
        jpAccountingConcept.add(jtfAccConceptPrefixRo);

        jtfAccConceptDocsRo.setEditable(false);
        jtfAccConceptDocsRo.setText("TEXT");
        jtfAccConceptDocsRo.setToolTipText("<computed>");
        jtfAccConceptDocsRo.setFocusable(false);
        jtfAccConceptDocsRo.setPreferredSize(new java.awt.Dimension(125, 23));
        jpAccountingConcept.add(jtfAccConceptDocsRo);

        jpPaymentTotals.add(jpAccountingConcept);

        jbAccConceptDocsEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbAccConceptDocsEdit.setToolTipText("Personalizar documentos concepto contable");
        jbAccConceptDocsEdit.setPreferredSize(new java.awt.Dimension(23, 23));
        jpPaymentTotals.add(jbAccConceptDocsEdit);

        jpDocuments.add(jpPaymentTotals, java.awt.BorderLayout.SOUTH);

        jpRegistryRows.add(jpDocuments, java.awt.BorderLayout.EAST);

        jpDialog.add(jpRegistryRows, java.awt.BorderLayout.CENTER);

        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar"); // NOI18N
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbOk);

        jbCancel.setText("Cancelar"); // NOI18N
        jbCancel.setToolTipText("[Escape]");
        jpControls.add(jbCancel);

        jpDialog.add(jpControls, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jpDialog, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(1116, 689));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        actionPerformedCancel();
    }//GEN-LAST:event_formWindowClosing

    /*
     * Private methods:
     */

    private void initComponentsExtra() {
        moExchangeRateFormat = new DecimalFormat("#,#0.000000");
        
        // initialize input fields:
        
        moFieldVouDate = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftVouDate, jlVouDate);
        moFieldVouDate.setPickerButton(jbVouDatePick);
        moFieldVouTaxRegime = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbVouTaxRegime, jlVouTaxRegime);
        moFieldVouConfirm = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfVouConfirm, jlVouConfirm);
        moFieldVouConfirm.setLengthMin(5);
        moFieldVouConfirm.setLengthMax(5);
        moFieldRecBizPartner = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbRecBizPartner, jlRecBizPartner);
        moFieldRecBizPartner.setPickerButton(jbRecBizPartnerPick);
        moFieldRecCfdiUse = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbRecCfdiUsage, jlRecCfdiUsage);
        moFieldRecCfdRelatedUuid = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfRecCfdRelatedUuid, jlRecCfdRelated);
        moFieldRecCfdRelatedUuid.setLengthMin(36);
        moFieldRecCfdRelatedUuid.setLengthMax(36);
        moFieldPayDate = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftPayDate, jlPayDate);
        moFieldPayDate.setPickerButton(jbPayDatePick);
        moFieldPayTime = new SFormField(miClient, SLibConstants.DATA_TYPE_TIME, true, jftPayTime, jlPayTime);
        moFieldPayPaymentWay = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPayPaymentWay, jlPayPaymentWay);
        moFieldPayCurrency = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPayCurrency, jlPayCurrency);
        moFieldPayFactoringBank = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPayFactoringBank, jckPayFactoring);
        moFieldPayAmount = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfPayAmount, jlPayAmount);
        moFieldPayAmount.setDecimalFormat(SLibUtils.getDecimalFormatAmount());
        moFieldPayExchangeRate = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfPayExchangeRate, new JLabel(jtfPayExchangeRate.getToolTipText()));
        moFieldPayExchangeRate.setDecimalFormat(SLibUtils.getDecimalFormatExchangeRate());
        moFieldPayExchangeRate.setPickerButton(jbPayExchangeRatePick);
        moFieldPayOperation = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfPayOperation, jlPayOperation);
        moFieldPayOperation.setLengthMax(100);
        moFieldPayAccountSrcFiscalId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfPayAccountSrcFiscalId, jlPayAccountFiscalId);
        moFieldPayAccountSrcFiscalId.setLengthMin(DCfdConsts.LEN_RFC_ORG);
        moFieldPayAccountSrcFiscalId.setLengthMax(DCfdConsts.LEN_RFC_PER);
        moFieldPayAccountSrcFiscalId.setPickerButton(jbPayAccountSrcPick);
        moFieldPayAccountSrcNumber = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfPayAccountSrcNumber, jlPayAccountNumber);
        moFieldPayAccountSrcNumber.setLengthMin(10);
        moFieldPayAccountSrcNumber.setLengthMax(50);
        moFieldPayAccountSrcNumber.setPickerButton(jbPayAccountSrcPick);
        moFieldPayAccountSrcEntity = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfPayAccountSrcEntity, jlPayAccountEntity);
        moFieldPayAccountSrcEntity.setLengthMax(300);
        moFieldPayAccountDestFiscalId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfPayAccountDestFiscalId, jlPayAccountFiscalId);
        moFieldPayAccountDestFiscalId.setLengthMin(DCfdConsts.LEN_RFC_ORG);
        moFieldPayAccountDestFiscalId.setLengthMax(DCfdConsts.LEN_RFC_PER);
        moFieldPayAccountDestFiscalId.setPickerButton(jbPayAccountDestPick);
        moFieldPayAccountDestNumber = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfPayAccountDestNumber, jlPayAccountNumber);
        moFieldPayAccountDestNumber.setLengthMin(10);
        moFieldPayAccountDestNumber.setLengthMax(50);
        moFieldPayAccountDestNumber.setPickerButton(jbPayAccountDestPick);
        moFieldPayAccountDest = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPayAccountDest, jlPayAccountDest);
        moFieldPayAccountDest.setPickerButton(jbPayAccountDestPick);
        moFieldDocInstallment = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfDocInstallment, jlDocInstallment);
        moFieldDocExchangeRate = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfDocExchangeRate, jlDocExchangeRate);
        moFieldDocExchangeRate.setDecimalFormat(moExchangeRateFormat);
        moFieldDocBalancePrev = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfDocDocBalancePrev, jlDocBalancePrev);
        moFieldDocBalancePrev.setDecimalFormat(SLibUtils.getDecimalFormatAmount());
        moFieldDocPayment = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfDocDocPayment, jlDocPayment);
        moFieldDocPayment.setDecimalFormat(SLibUtils.getDecimalFormatAmount());

        mvFields = new Vector<>();
        mvFields.add(moFieldRecBizPartner);
        mvFields.add(moFieldRecCfdiUse);
        mvFields.add(moFieldRecCfdRelatedUuid);
        mvFields.add(moFieldVouDate);
        mvFields.add(moFieldVouTaxRegime);
        mvFields.add(moFieldVouConfirm);
        
        // initialize inner forms:
        
        moDialogPayRecordPicker = new SDialogRecordPicker(miClient, SDataConstants.FINX_REC_USER);
        moDialogRecDpsRelatedPicker = new SDialogPickerDps(miClient, SDataConstants.TRN_CFD);
        moDialogDocDpsRelatedPickerPend = new SDialogPickerDps(miClient, SDataConstants.TRNX_DPS_PAY_PEND);
        moDialogDocDpsRelatedPickerAll = new SDialogPickerDps(miClient, SDataConstants.TRN_DPS);
        
        // initialize payments grid:

        moPaneGridPayments = new STablePaneGrid(miClient);
        moPaneGridPayments.setDoubleClickAction(this, "actionPerformedPayPaymentEntryModify");
        jpPayments.add(moPaneGridPayments, BorderLayout.CENTER);

        int colPayment = 0;
        STableColumnForm[] colsPayments = new STableColumnForm[16];
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "# pago", STableConstants.WIDTH_NUM_TINYINT);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Fecha-hora pago", STableConstants.WIDTH_DATE_TIME);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Forma pago", 50);
        colsPayments[colPayment] = new STableColumnForm(SLibConstants.DATA_TYPE_FLOAT, "Monto pago $", STableConstants.WIDTH_VALUE);
        colsPayments[colPayment++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Moneda pago", STableConstants.WIDTH_CURRENCY_KEY);
        colsPayments[colPayment] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Tipo cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        colsPayments[colPayment++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Póliza contable", 125);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Núm. operación", 100);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "RFC emisor ordenante", 100);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Cuenta ordenante", 125);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Banco ordenante", 100);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "RFC emisor beneficiaria", 100);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Cuenta beneficiaria", 125);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo pago", 150);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "RFC banco factoraje", 100);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Documentos concepto contable", 150);

        for (colPayment = 0; colPayment < colsPayments.length; colPayment++) {
            moPaneGridPayments.addTableColumn(colsPayments[colPayment]);
        }
        
        // initialize documents grid:

        moPaneGridPaymentDocs = new STablePaneGrid(miClient);
        moPaneGridPaymentDocs.setDoubleClickAction(this, "actionPerformedDocPaymentEntryDocModify");
        jpDocuments.add(moPaneGridPaymentDocs, BorderLayout.CENTER);

        int colPaymentDoc = 0;
        STableColumnForm[] colsPaymentDocs = new STableColumnForm[12];
        colsPaymentDocs[colPaymentDoc++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "# doc", STableConstants.WIDTH_NUM_TINYINT);
        colsPaymentDocs[colPaymentDoc++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Folio doc", 75);
        colsPaymentDocs[colPaymentDoc++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "UUID doc", 125);
        colsPaymentDocs[colPaymentDoc++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Parcialidad", STableConstants.WIDTH_NUM_TINYINT);
        colsPaymentDocs[colPaymentDoc] = new STableColumnForm(SLibConstants.DATA_TYPE_FLOAT, "Saldo anterior $", STableConstants.WIDTH_VALUE);
        colsPaymentDocs[colPaymentDoc++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        colsPaymentDocs[colPaymentDoc] = new STableColumnForm(SLibConstants.DATA_TYPE_FLOAT, "Importe pagado $", STableConstants.WIDTH_VALUE);
        colsPaymentDocs[colPaymentDoc++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        colsPaymentDocs[colPaymentDoc] = new STableColumnForm(SLibConstants.DATA_TYPE_FLOAT, "Saldo insoluto $", STableConstants.WIDTH_VALUE);
        colsPaymentDocs[colPaymentDoc++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        colsPaymentDocs[colPaymentDoc++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Moneda doc", STableConstants.WIDTH_CURRENCY_KEY);
        colsPaymentDocs[colPaymentDoc] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Tipo cambio", STableConstants.WIDTH_VALUE);
        colsPaymentDocs[colPaymentDoc++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        colsPaymentDocs[colPaymentDoc] = new STableColumnForm(SLibConstants.DATA_TYPE_FLOAT, "Importe aplicado pago $", STableConstants.WIDTH_VALUE);
        colsPaymentDocs[colPaymentDoc++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        colsPaymentDocs[colPaymentDoc++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Moneda pago", STableConstants.WIDTH_CURRENCY_KEY);
        colsPaymentDocs[colPaymentDoc++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo pago", 150);

        for (colPaymentDoc = 0; colPaymentDoc < colsPaymentDocs.length; colPaymentDoc++) {
            moPaneGridPaymentDocs.addTableColumn(colsPaymentDocs[colPaymentDoc]);
        }
        
        // populate XML catalogs:

        SCfdXmlCatalogs xmlCatalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        xmlCatalogs.populateComboBox(jcbVouTaxRegime, SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, miClient.getSession().getSystemDate());
        xmlCatalogs.populateComboBox(jcbPayPaymentWay, SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY, miClient.getSession().getSystemDate());
        xmlCatalogs.populateComboBox(jcbRecCfdiUsage, SDataConstantsSys.TRNS_CFD_CAT_CFD_USE, miClient.getSession().getSystemDate());
      
        // add listeners:
        
        jbVouDatePick.addActionListener(this);
        jbVouNext.addActionListener(this);
        jbVouResume.addActionListener(this);
        
        jbRecBizPartnerPick.addActionListener(this);
        jbRecTaxRegime.addActionListener(this);
        jbRecCfdRelatedAdd.addActionListener(this);
        jbRecCfdRelatedDelete.addActionListener(this);
        jbRecCfdRelatedPick.addActionListener(this);
        jbPayDatePick.addActionListener(this);
        jbPayExchangeRatePick.addActionListener(this);
        jbPayRecordPick.addActionListener(this);
        jbPayRecordView.addActionListener(this);
        jbPayAccountSrcPick.addActionListener(this);
        jbPayAccountDestPick.addActionListener(this);
        jbPayPaymentEntryAdd.addActionListener(this);
        jbPayPaymentEntryModify.addActionListener(this);
        jbPayPaymentEntryDelete.addActionListener(this);
        jbPayPaymentEntryOk.addActionListener(this);
        jbPayPaymentEntryCancel.addActionListener(this);
        jbDocDpsRelatedPickPend.addActionListener(this);
        jbDocDpsRelatedPickAll.addActionListener(this);
        jbDocExchangeRateInvert.addActionListener(this);
        jbDocPaymentCompute.addActionListener(this);
        jbDocPaymentEntryDocAdd.addActionListener(this);
        jbDocPaymentEntryDocModify.addActionListener(this);
        jbDocPaymentEntryDocDelete.addActionListener(this);
        jbDocPaymentEntryDocOk.addActionListener(this);
        jbDocPaymentEntryDocCancel.addActionListener(this);
        jbAccConceptDocsEdit.addActionListener(this);
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        
        jcbPayPaymentWay.addItemListener(this);
        jcbPayCurrency.addItemListener(this);
        jcbPayAccountDest.addItemListener(this);
        jcbRecBizPartner.addItemListener(this);
        jckPayFactoring.addItemListener(this);
        
        jtfRecCfdRelatedUuid.addFocusListener(this);
        jtfPayAmount.addFocusListener(this);
        jtfPayExchangeRate.addFocusListener(this);
        jtfDocExchangeRate.addFocusListener(this);
        jtfDocDocBalancePrev.addFocusListener(this);
        jtfDocDocPayment.addFocusListener(this);
        jtfRecTaxRegime.addFocusListener(this);
        
        jtfAccConceptPrefixRo.setText(SFinConsts.TXT_INVOICE);
        jtfPayAmountLocalCurRo.setText(miClient.getSession().getSessionCustom().getLocalCurrencyCode());
        jtfVouTotalLocalCurRo.setText(miClient.getSession().getSessionCustom().getLocalCurrencyCode());
        jtfPayTotalPaymentsLocalCurRo.setText(miClient.getSession().getSessionCustom().getLocalCurrencyCode());
        
        try {
            moXmlCatalogs = new SCfdXmlCatalogs(miClient.getSession());
        }
        catch (Exception ex) {
            miClient.showMsgBoxInformation(ex.getMessage());
        }
        
        // create and asign action maps:

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionPerformedOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionCancel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionPerformedCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    /**
     * Prepares form when window is activated.
     */
    private void windowActivated() {
        if (mbIsFirstTime) {
            mbIsFirstTime = false;
            
            if (moDataCfdPayment == null) {
                if (miClient.getSessionXXX().getCurrentCompanyBranch() == null) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_SESSION_BRANCH);   // no company branch selected
                    mbIsFormReady = false;
                    actionPerformedCancel();
                }
                
                renderComBranch();
            }
            
            if (mbIsFormReady) {
                if (jcbRecBizPartner.isEnabled()) {
                    jcbRecBizPartner.requestFocusInWindow();
                }
                else if (jftVouDate.isEditable()) {
                    jftVouDate.requestFocusInWindow();
                }
                else if (jcbVouTaxRegime.isEnabled()) {
                    jcbVouTaxRegime.requestFocusInWindow();
                }
                else {
                    jbCancel.requestFocusInWindow();
                }
            }
        }
    }
    
    /**
     * Gains lock on desired record, if possible and only if it does not already exist.
     * @param record Desired record to lock.
     * @throws Exception 
     */
    private void gainRecordLock(final erp.mfin.data.SDataRecord record) throws Exception {
        // check if lock of desired record already exists:
        
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
        SSrvLock lock = moRecordLocksMap.get(record.getRecordPrimaryKey()); // record's primary key as string used as map's key        
        if (lock == null) {
            lock = SSrvUtils.gainLock(miClient.getSession(), miClient.getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.FIN_REC, record.getPrimaryKey(), record.getRegistryTimeout());
            if (lock != null) {
                moRecordLocksMap.put(record.getRecordPrimaryKey(), lock);
            }
        }
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        SRedisLock rlock = moRecordRedisLocksMap.get(record.getRecordPrimaryKey());
        if (rlock == null) {
            rlock = SRedisLockUtils.gainLock(miClient, SDataConstants.FIN_REC, record.getPrimaryKey(), record.getRegistryTimeout() / 1000);
            if (rlock != null) {
                moRecordRedisLocksMap.put(record.getRecordPrimaryKey(), rlock);
            }
        }
        */
        SLock slock = moRecordSLocksMap.get(record.getRecordPrimaryKey()); // record's primary key as string used as map's key        
        if (slock == null) {
            slock = SLockUtils.gainLock(miClient, SDataConstants.FIN_REC, record.getPrimaryKey(), record.getRegistryTimeout());
            if (slock != null) {
                moRecordSLocksMap.put(record.getRecordPrimaryKey(), slock);
            }
        }
    }

    /**
     * Releases lock of desired record, if exists and only if it is no longer needed by other payments.
     * @param record Desired record whose lock is about to be released.
     * @throws Exception 
     */
    private void releaseRecordLock(final erp.mfin.data.SDataRecord record) throws Exception {
        // check if lock of desired record already exists and if it is no longer needed by other payments:
        
        int count = 0;
        
        for (STableRow row : moPaneGridPayments.getGridRows()) {
            if (SLibUtils.compareKeys(((SCfdPaymentEntry) row).DataRecord.getPrimaryKey(), record.getPrimaryKey())) {
                if (++count > 1) {
                    break;
                }
            }
        }
        
        if (count == 1) {
            // lock used only once, proceed to release it:
            
            /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro   
            SSrvLock lock = moRecordLocksMap.get(record.getRecordPrimaryKey());            
            if (lock != null) {
                SSrvUtils.releaseLock(miClient.getSession(), lock);
                moRecordLocksMap.remove(record.getRecordPrimaryKey());
            }
            */
            /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
            SRedisLock rlock = moRecordRedisLocksMap.get(record.getRecordPrimaryKey());
            if (rlock != null) {
                SRedisLockUtils.releaseLock(miClient, rlock);
                moRecordRedisLocksMap.remove(record.getRecordPrimaryKey());
            }
            */
            SLock slock = moRecordSLocksMap.get(record.getRecordPrimaryKey());            
            if (slock != null) {
                SLockUtils.releaseLock(miClient, slock);
                moRecordSLocksMap.remove(record.getRecordPrimaryKey());
            }
        }
    }

    /**
     * Releases and clears all existing record locks.
     * @throws Exception 
     */
    private void releaseAllRecordLocks() throws Exception {
        String exception = "";
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
        ArrayList<SSrvLock> locks = new ArrayList<>(moRecordLocksMap.values());
        for (int index = 0; index < locks.size(); index++) {
            try {
                SSrvUtils.releaseLock(miClient.getSession(), locks.get(index));
            }
            catch (Exception e) {
                exception += (exception.isEmpty() ? "" : "\n") + e;
            }
        }
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        ArrayList<SRedisLock> rlocks = new ArrayList<>(moRecordRedisLocksMap.values());
        for (int index = 0; index < rlocks.size(); index++) {
            try {
                SRedisLockUtils.releaseLock(miClient, rlocks.get(index));
            }
            catch (Exception e) {
                exception += (exception.isEmpty() ? "" : "\n") + e;
            }
        }
        */
        ArrayList<SLock> slocks = new ArrayList<>(moRecordSLocksMap.values());
        for (int index = 0; index < slocks.size(); index++) {
            try {
                SLockUtils.releaseLock(miClient, slocks.get(index));
            }
            catch (Exception e) {
                exception += (exception.isEmpty() ? "" : "\n") + e;
            }
        }
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
        moRecordLocksMap.clear();
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        moRecordRedisLocksMap.clear();
        */
        if (!exception.isEmpty()) {
            throw new Exception(exception);
        }
    }

    /**
     * Reads and locks desired record.
     * @param recordKey Primary key of desired record.
     * @return 
     */
    private void setPayRecord(final java.lang.Object recordKey) throws Exception {
        if (moDataPayRecord != null) {
            releaseRecordLock(moDataPayRecord);
            moDataPayRecord = null;
        }
        
        if (!SDataUtilities.isPeriodOpen(miClient, moFieldVouDate.getDate())) {
            throw new Exception(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
        }
        else {
            SDataRecord record = (SDataRecord) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_REC, recordKey, SLibConstants.EXEC_MODE_VERBOSE);
            
            if (record.getIsSystem()) {
                throw new Exception("No se puede seleccionar esta póliza contable porque es de sistema.");
            }
            else if (record.getIsAudited()) {
                throw new Exception("No se puede seleccionar esta póliza contable porque ya está auditada.");
            }
            else if (record.getIsAuthorized()) {
                throw new Exception("No se puede seleccionar esta póliza contable porque ya está autorizada.");
            }

            gainRecordLock(record);
            moDataPayRecord = record;
        }
        
        renderPayRecord();
    }
    
    /**
     * Renders company-branch info. Session's when registry is new, CFD's when registriy is being modified.
     * When registry is new should not be called in method formReset(), instead in method windowActivated().
     */
    private void renderComBranch() {
        if (moDataCfdPayment == null) {
            moDataComBranch = miClient.getSessionXXX().getCurrentCompanyBranch();
            SFormUtilities.populateComboBox(miClient, jcbPayAccountDest, SDataConstants.FIN_ACC_CASH, new int[] { moDataComBranch.getPkBizPartnerBranchId() });
        }
        
        jtfVouBranchNameRo.setText(moDataComBranch.getBizPartnerBranch());
        jtfVouBranchCodeRo.setText(moDataComBranch.getCode());
        
        jtfVouBranchNameRo.setCaretPosition(0);
        jtfVouBranchCodeRo.setCaretPosition(0);
    }

    private void renderPayRecord() {
        if (moDataPayRecord == null) {
            jtfPayRecordDateRo.setText("");
            jtfPayRecordBranchCodeRo.setText("");
            jtfPayRecordNumberRo.setText("");
            jbPayRecordView.setEnabled(false);
        }
        else {
            jtfPayRecordDateRo.setText(SLibUtils.DateFormatDate.format(moDataPayRecord.getDate()));
            jtfPayRecordBranchCodeRo.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.BPSU_BPB, new int[] { moDataPayRecord.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
            jtfPayRecordNumberRo.setText(moDataPayRecord.getRecordNumber());
            jbPayRecordView.setEnabled(true);
            
            jtfPayRecordDateRo.setCaretPosition(0);
            jtfPayRecordBranchCodeRo.setCaretPosition(0);
            jtfPayRecordNumberRo.setCaretPosition(0);
            
            selectPayAccountDest();
        }
    }
    
    private void selectPayAccountDest() {
        SDataAccountCash account = moDataPayRecord.getDbmsDataAccountCash();
        if (account != null && SLibUtilities.compareKeys(moFieldPayAccountDest.getKey(), new int[] { 0, 0 })) {
            moFieldPayAccountDest.setKey(account.getPrimaryKey());    // will throw an item-state-changed event
        }        
    }
    
    private void validateDpsRelated() throws Exception {
        mbDpsValidationFailed = false;
        
        try {
            if (moThinDocDpsRelated == null) {
                throw new Exception("El documento relacionado no existe.");
            }
            else {
                String msg = "El documento relacionado " + moThinDocDpsRelated.getDpsNumber() + " ";

                if (moThinDocDpsRelated.getThinCfd() == null) {
                    throw new Exception(msg + "carece de CFDI.");
                }
                else if (moThinDocDpsRelated.getThinCfd().getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFDI_33
                        && moThinDocDpsRelated.getThinCfd().getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFDI_40) {
                    throw new Exception(msg + "debe ser CFDI versión " + DCfdConsts.CFDI_VER_33 + " o " + DCfdConsts.CFDI_VER_40);
                }
                else if (!moThinDocDpsRelated.getThinCfd().isStamped()) {
                    throw new Exception(msg + "no está timbrado.");
                }
                else if (moThinDocDpsRelated.getThinCfd().getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                    throw new Exception(msg + "está cancelado.");
                }
                else if (moThinDocDpsRelated.getThinCfd().getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                    throw new Exception(msg + "no está emitido.");
                }
            }
        }
        catch (Exception e) {
            mbDpsValidationFailed = true;
            throw e;
        }
    }

    private void renderPayPaymentEntry() {
        if (moPaymentEntry == null) {
            moFieldPayDate.resetField();
            moFieldPayTime.resetField();
            moFieldPayPaymentWay.resetField();
            itemStateChangedPayPaymentWay();
            moFieldPayCurrency.resetField();
            itemStateChangedPayCurrency();
            moFieldPayAmount.resetField();
            moFieldPayExchangeRate.resetField();
            computePayAmountLocal();
            moFieldPayOperation.resetField();
            moDataPayRecord = null;
            renderPayRecord();
            renderAccConceptDocs(null);
            
            moFieldPayAccountSrcFiscalId.resetField();
            moFieldPayAccountSrcNumber.resetField();
            moFieldPayAccountSrcEntity.resetField();
            moFieldPayAccountDestFiscalId.resetField();
            moFieldPayAccountDestNumber.resetField();
            moFieldPayAccountDest.resetField();
            
            jckPayFactoring.setSelected(false);
            itemStateChangedPayFactoring();
            jrbPayFactoringPay.setSelected(true);
        }
        else {
            moFieldPayDate.setFieldValue(moPaymentEntry.PaymentDate);
            moFieldPayTime.setFieldValue(moPaymentEntry.PaymentDate);
            moFieldPayPaymentWay.setFieldValue(moPaymentEntry.PaymentWay);
            itemStateChangedPayPaymentWay();
            moFieldPayCurrency.setFieldValue(new int[] { moPaymentEntry.CurrencyId });
            itemStateChangedPayCurrency();
            moFieldPayAmount.setFieldValue(moPaymentEntry.Amount);
            moFieldPayExchangeRate.setFieldValue(moPaymentEntry.ExchangeRate);
            computePayAmountLocal();
            moFieldPayOperation.setFieldValue(moPaymentEntry.Operation);
            moDataPayRecord = moPaymentEntry.DataRecord;
            renderPayRecord();
            renderAccConceptDocs(moPaymentEntry);
            
            moFieldPayAccountSrcFiscalId.setFieldValue(moPaymentEntry.AccountSrcFiscalId);
            moFieldPayAccountSrcNumber.setFieldValue(moPaymentEntry.AccountSrcNumber);
            moFieldPayAccountSrcEntity.setFieldValue(moPaymentEntry.AccountSrcEntity);
            moFieldPayAccountDestFiscalId.setFieldValue(moPaymentEntry.AccountDestFiscalId);
            moFieldPayAccountDestNumber.setFieldValue(moPaymentEntry.AccountDestNumber);
            moFieldPayAccountDest.setFieldValue(moPaymentEntry.AccountDestKey);
            
            jckPayFactoring.setSelected(moPaymentEntry.isFactoring());
            itemStateChangedPayFactoring();
            jrbPayFactoringPay.setSelected(moPaymentEntry.EntryType == SCfdPaymentEntry.TYPE_FACTORING_PAY);
        }
    }

    private void renderDocPaymentEntryDoc() {
        if (moPaymentEntryDoc == null) {
            moThinDocDpsRelated = null;
            
            moFieldDocInstallment.resetField();
            moFieldDocExchangeRate.resetField();
            moFieldDocBalancePrev.resetField();
            moFieldDocPayment.resetField();
            jtfDocDocBalancePend.setText(SLibUtils.getDecimalFormatAmount().format(0));
            
            jtfDocPayBalancePrevRo.setText(SLibUtils.getDecimalFormatAmount().format(0));
            jtfDocPayPaymentRo.setText(SLibUtils.getDecimalFormatAmount().format(0));
            jtfDocPayBalancePendRo.setText(SLibUtils.getDecimalFormatAmount().format(0));
            
            jtfDocPaymentTypeRo.setText("");
        }
        else {
            moThinDocDpsRelated = moPaymentEntryDoc.ThinDps;
            
            moFieldDocInstallment.setFieldValue(moPaymentEntryDoc.Installment);
            moFieldDocExchangeRate.setFieldValue(moPaymentEntryDoc.ExchangeRate);
            moFieldDocBalancePrev.setFieldValue(moPaymentEntryDoc.DocBalancePrev);
            moFieldDocPayment.setFieldValue(moPaymentEntryDoc.DocPayment);
            jtfDocDocBalancePend.setText(SLibUtils.getDecimalFormatAmount().format(moPaymentEntryDoc.DocBalancePend));
            
            jtfDocPayBalancePrevRo.setText(SLibUtils.getDecimalFormatAmount().format(moPaymentEntryDoc.PayBalancePrev));
            jtfDocPayPaymentRo.setText(SLibUtils.getDecimalFormatAmount().format(moPaymentEntryDoc.PayPayment));
            jtfDocPayBalancePendRo.setText(SLibUtils.getDecimalFormatAmount().format(moPaymentEntryDoc.PayBalancePend));
            
            jtfDocPaymentTypeRo.setText(moPaymentEntryDoc.getTypeDescription());
        }
        
        SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
        String docCurrencyKey = moPaymentEntryDoc == null ? "" : moPaymentEntryDoc.ThinDps.getDbmsCurrencyKey();
        String payCurrencyKey = moPaneGridPayments.getSelectedTableRow() == null ? "" : paymentEntry.CurrencyKey;
        
        jtfDocPayBalancePrevCurRo.setText(payCurrencyKey);
        jtfDocPayPaymentCurRo.setText(payCurrencyKey);
        jtfDocPayBalancePendCurRo.setText(payCurrencyKey);
        jtfPayTotalPaymentsCurRo.setText(payCurrencyKey);
        
        jtfDocExchangeRateCurRo.setText(docCurrencyKey.isEmpty() || payCurrencyKey.isEmpty() ? "" : docCurrencyKey + "/" + payCurrencyKey);
        
        try {
            renderDocDpsRelated();
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void renderRecCfdRelated() {
        if (moDataRecCfdRelated == null) {
            jtfRecCfdRelatedNumberRo.setText("");
            jtfRecCfdRelatedVersionRo.setText("");
        }
        else {
            jtfRecCfdRelatedNumberRo.setText(moDataRecCfdRelated.getCfdNumber());
            jtfRecCfdRelatedVersionRo.setText("" + SCfdUtils.getCfdVersion(moDataRecCfdRelated.getFkXmlTypeId()));
            
            jtfRecCfdRelatedNumberRo.setCaretPosition(0);
            jtfRecCfdRelatedVersionRo.setCaretPosition(0);
        }
        
        jtfRecCfdRelatedUuid.setText(msRecCfdRelatedUuid); // can be empty when related CFDI is not stored in SIIE, e.g., third-party CFDI!
        jtfRecCfdRelatedUuid.setCaretPosition(0);
        
        if (moDataRecCfdRelated == null && msRecCfdRelatedUuid.isEmpty()) {
            msXmlRelationType = "";
            jtfRecRelationTypeRo.setText("");
        }
        else {
            msXmlRelationType = DCfdi33Catalogs.ClaveTipoRelaciónSustitución; // fixed value in CFDI 3.3!
            jtfRecRelationTypeRo.setText(((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs().composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_REL_TP, msXmlRelationType));
            
            jtfRecRelationTypeRo.setCaretPosition(0);
        }
    }

    private void renderDocDpsRelated() throws Exception {
        if (moThinDocDpsRelated == null) {
            jtfDocDpsRelatedNumberRo.setText("");
            jtfDocDpsRelatedUuid.setText("");
            jtfDocDpsRelatedVersionRo.setText("");
            jtfDocPaymentMethodRo.setText("");
            jtfDocCurrencyRo.setText("");
            
            jtfDocDocBalancePrevCurRo.setText("");
            jtfDocDocPaymentCurRo.setText("");
            jtfDocDocBalancePendCurRo.setText("");
            
            jtfDocExchangeRateCurRo.setText("");
            
            jtfDocExchangeRate.setEditable(false);
            jtfDocExchangeRate.setFocusable(false);
            jbDocExchangeRateInvert.setEnabled(false);
            jbDocPaymentCompute.setEnabled(false);
        }
        else {
            validateDpsRelated();
            
            jtfDocDpsRelatedNumberRo.setText(moThinDocDpsRelated.getDpsNumber());
            jtfDocDpsRelatedUuid.setText(moThinDocDpsRelated.getThinCfd().getUuid());
            jtfDocDpsRelatedVersionRo.setText("" + SCfdUtils.getCfdVersion(moThinDocDpsRelated.getThinCfd().getFkXmlTypeId()));
            jtfDocPaymentMethodRo.setText(moThinDocDpsRelated.getThinDpsCfd().getPaymentMethod());
            jtfDocCurrencyRo.setText(moThinDocDpsRelated.getDbmsCurrency());
            
            jtfDocDpsRelatedNumberRo.setCaretPosition(0);
            jtfDocDpsRelatedUuid.setCaretPosition(0);
            jtfDocDpsRelatedVersionRo.setCaretPosition(0);
            jtfDocPaymentMethodRo.setCaretPosition(0);
            jtfDocCurrencyRo.setCaretPosition(0);
            
            jtfDocDocBalancePrevCurRo.setText(moThinDocDpsRelated.getDbmsCurrencyKey());
            jtfDocDocPaymentCurRo.setText(moThinDocDpsRelated.getDbmsCurrencyKey());
            jtfDocDocBalancePendCurRo.setText(moThinDocDpsRelated.getDbmsCurrencyKey());
            
            SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
            jtfDocExchangeRateCurRo.setText(moThinDocDpsRelated.getDbmsCurrencyKey() + "/" + paymentEntry.CurrencyKey);
            
            if (paymentEntry.CurrencyId == moThinDocDpsRelated.getFkCurrencyId()) {
                jtfDocExchangeRate.setEditable(false);
                jtfDocExchangeRate.setFocusable(false);
                jbDocExchangeRateInvert.setEnabled(false);
                jbDocPaymentCompute.setEnabled(false);
            }
            else {
                jtfDocExchangeRate.setEditable(true);
                jtfDocExchangeRate.setFocusable(true);
                jbDocExchangeRateInvert.setEnabled(true);
                jbDocPaymentCompute.setEnabled(true);
            }
        }
    }
    
    private void renderAccConceptDocs(final erp.mtrn.data.cfd.SCfdPaymentEntry paymentEntry) {
        boolean reset;
        
        if (paymentEntry == null) {
            reset = true;
            jtfAccConceptDocsRo.setText("");
        }
        else {
            if (!paymentEntry.AuxConceptDocsCustom.isEmpty() && !paymentEntry.AuxConceptDocsCustom.equals(paymentEntry.AuxConceptDocs)) {
                reset = false;
                jtfAccConceptDocsRo.setText(paymentEntry.AuxConceptDocsCustom);
            }
            else {
                reset = true;
                jtfAccConceptDocsRo.setText(paymentEntry.AuxConceptDocs);
                
                paymentEntry.AuxConceptDocsCustom = "";
            }
        }
        
        jtfAccConceptDocsRo.setCaretPosition(0);
        jtfAccConceptDocsRo.setBackground(reset ? moBackgroundDefaultColor : java.awt.Color.cyan);
        jtfAccConceptDocsRo.setToolTipText(jtfAccConceptDocsRo.getText().isEmpty() ? CONCEPT_DOCS: jtfAccConceptDocsRo.getText());
    }

    private void computePayAmountLocal() {
        jtfPayAmountLocalRo.setText(SLibUtils.getDecimalFormatAmount().format(moFieldPayAmount.getDouble() * moFieldPayExchangeRate.getDouble()));
    }
    
    private void computeDocBalancePend() {
        jtfDocDocBalancePend.setText(SLibUtils.getDecimalFormatAmount().format(moFieldDocBalancePrev.getDouble() - moFieldDocPayment.getDouble()));
        
        computeDocPaymentAmounts();
    }

    private void computeDocPaymentAmounts() {
        if (moFieldDocExchangeRate.getDouble() == 0) {
            jtfDocPayBalancePrevRo.setText(SLibUtils.getDecimalFormatAmount().format(0));
            jtfDocPayPaymentRo.setText(SLibUtils.getDecimalFormatAmount().format(0));
            jtfDocPayBalancePendRo.setText(SLibUtils.getDecimalFormatAmount().format(0));
        }
        else {
            jtfDocPayBalancePrevRo.setText(SLibUtils.getDecimalFormatAmount().format(moFieldDocBalancePrev.getDouble() / moFieldDocExchangeRate.getDouble()));
            jtfDocPayPaymentRo.setText(SLibUtils.getDecimalFormatAmount().format(moFieldDocPayment.getDouble() / moFieldDocExchangeRate.getDouble()));
            jtfDocPayBalancePendRo.setText(SLibUtils.getDecimalFormatAmount().format(SLibUtils.parseDouble(jtfDocDocBalancePend.getText()) / moFieldDocExchangeRate.getDouble()));
        }
        
        computePayTotalPayments();
    }
    
    private void computePayTotalPayments() {
        double total = 0;
        double totalLocal = 0;
        
        SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
        
        if (paymentEntry != null) {
            paymentEntry.computeTotalPayments();
            total = paymentEntry.AuxTotalPayments;
            totalLocal = paymentEntry.AuxTotalPaymentsLocal;
        }
        
        // payment's original currency:
        jtfPayTotalPaymentsRo.setText(SLibUtils.getDecimalFormatAmount().format(total));
        
        // local currency:
        jtfPayTotalPaymentsLocalRo.setText(SLibUtils.getDecimalFormatAmount().format(totalLocal));
        
        // accounting documents:
        renderAccConceptDocs(paymentEntry);
        
        // counter:
        jlDocCounter.setText("Docs.: " + SLibUtils.DecimalFormatInteger.format(paymentEntry == null ? 0 : paymentEntry.PaymentEntryDocs.size()));
    }
    
    private void computeVouTotal() {
        double total = 0;
        double totalLocal = 0;
        String currencyKey = "";
        HashSet<Integer> currenciesSet = new HashSet<>();
        
        for (STableRow row : moPaneGridPayments.getGridRows()) {
            SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) row;
            total = SLibUtils.roundAmount(total + paymentEntry.Amount);
            totalLocal = SLibUtils.roundAmount(totalLocal + paymentEntry.AmountLocal);
            currencyKey = paymentEntry.CurrencyKey;
            currenciesSet.add(paymentEntry.CurrencyId);
        }
        
        // voucher's original currency (if posible, that is, if only one currency used in voucher):
        if (currenciesSet.size() != 1) {
            jtfVouTotalRo.setText("");
            jtfVouTotalCurRo.setText("");
        }
        else {
            jtfVouTotalRo.setText(SLibUtils.getDecimalFormatAmount().format(total));
            jtfVouTotalCurRo.setText(currencyKey);
        }
        
        // local currency:
        jtfVouTotalLocalRo.setText(SLibUtils.getDecimalFormatAmount().format(totalLocal));
        
        // counter:
        jlPayCounter.setText("Pagos: " + SLibUtils.DecimalFormatInteger.format(moPaneGridPayments.getGridRows().size()));
    }

    private boolean isPayerForeign() {
        return moDataRecBizPartner != null && moDataRecBizPartner.getFiscalId().equals(DCfdConsts.RFC_GEN_INT);
    }

    private void enableVouFields(final boolean enable) {
        jftVouDate.setEditable(!mbIsFormReadOnly);
        jftVouDate.setFocusable(!mbIsFormReadOnly);
        jbVouDatePick.setEnabled(!mbIsFormReadOnly);
        jcbVouTaxRegime.setEnabled(enable);
        jtfVouConfirm.setEditable(enable);
        jtfVouConfirm.setFocusable(enable);
    }

    private void enableRecFields(final boolean enable) {
        jcbRecBizPartner.setEnabled(enable);
        jbRecBizPartnerPick.setEnabled(enable);
        
        if (enable) {
            boolean cfdRelatedAvailable = moDataRecCfdRelated != null || !msRecCfdRelatedUuid.isEmpty();
            jbRecCfdRelatedAdd.setEnabled(!cfdRelatedAvailable);
            jbRecCfdRelatedDelete.setEnabled(cfdRelatedAvailable);
            jtfRecCfdRelatedUuid.setEditable(cfdRelatedAvailable);
            jtfRecCfdRelatedUuid.setFocusable(cfdRelatedAvailable);
            jbRecCfdRelatedPick.setEnabled(cfdRelatedAvailable);
        }
        else {
            jbRecCfdRelatedAdd.setEnabled(false);
            jbRecCfdRelatedDelete.setEnabled(false);
            jtfRecCfdRelatedUuid.setEditable(false);
            jtfRecCfdRelatedUuid.setFocusable(false);
            jbRecCfdRelatedPick.setEnabled(false);
        }
        
        updateRecBizPartnerFields();
    }
    
    private void updateRecBizPartnerFields() {
        jcbRecCfdiUsage.setEnabled(false); // allways remains disabled
    }
    
    private void activateRecCfdRelated(final boolean activateControlsForAddition) {
        jbRecCfdRelatedAdd.setEnabled(activateControlsForAddition);
        jbRecCfdRelatedDelete.setEnabled(!activateControlsForAddition);
        jtfRecCfdRelatedUuid.setEditable(!activateControlsForAddition);
        jtfRecCfdRelatedUuid.setFocusable(!activateControlsForAddition);
        jbRecCfdRelatedPick.setEnabled(!activateControlsForAddition);
        
        moDataRecCfdRelated = null;
        msRecCfdRelatedUuid = "";
        renderRecCfdRelated();
    }

    private void enablePayFields(final boolean enable) {
        jftPayDate.setEditable(enable);
        jftPayDate.setFocusable(enable);
        jbPayDatePick.setEnabled(enable);
        jftPayTime.setEditable(enable);
        jftPayTime.setFocusable(enable);
        jcbPayPaymentWay.setEnabled(enable);
        jckPayFactoring.setEnabled(enable);
        jcbPayCurrency.setEnabled(enable);
        jtfPayAmount.setEditable(enable);
        jtfPayAmount.setFocusable(enable);
        jtfPayOperation.setEditable(enable);
        jtfPayOperation.setFocusable(enable);
        jbPayRecordPick.setEnabled(enable);
        jbPayRecordView.setEnabled(enable);
        
        updatePayPaymentWayFields();
        updatePayFactoringFields();
        updatePayCurrencyFields();
    }
    
    private void updatePayPaymentWayFields() {
        boolean enableSrc;
        boolean enableSrcEntity;
        boolean enableDest;
        
        if (!jcbPayPaymentWay.isEnabled() || jcbPayPaymentWay.getSelectedIndex() <= 0) {
            enableSrc = false;
            enableSrcEntity = false;
            enableDest = false;
        }
        else {
            String paymentWayCode = moFieldPayPaymentWay.getFieldValue().toString(); // convenience variable
            enableSrc = !DCfdi33Utils.notRequiredAccountPayer(paymentWayCode);
            enableSrcEntity = !DCfdi33Utils.notRequiredBankPayer(paymentWayCode) || isPayerForeign();
            enableDest = !DCfdi33Utils.notRequiredAccountReceipt(paymentWayCode);
        }
        
        jtfPayAccountSrcFiscalId.setEditable(enableSrc);
        jtfPayAccountSrcFiscalId.setFocusable(enableSrc);
        jtfPayAccountSrcNumber.setEditable(enableSrc);
        jtfPayAccountSrcNumber.setFocusable(enableSrc);
        jtfPayAccountSrcEntity.setEditable(enableSrcEntity);
        jtfPayAccountSrcEntity.setFocusable(enableSrcEntity);
        jbPayAccountSrcPick.setEnabled(enableSrc);
        jtfPayAccountDestFiscalId.setEditable(false);
        jtfPayAccountDestFiscalId.setFocusable(false);
        jtfPayAccountDestNumber.setEditable(false);
        jtfPayAccountDestNumber.setFocusable(false);
        jcbPayAccountDest.setEnabled(enableDest);
        jbPayAccountDestPick.setEnabled(enableDest);
        
        if (!enableSrc) {
            moFieldPayAccountSrcFiscalId.resetField();
            moFieldPayAccountSrcNumber.resetField();
        }
        
        if (!enableSrcEntity) {
            moFieldPayAccountSrcEntity.resetField();
        }
        
        if (!enableDest) {
            moFieldPayAccountDestFiscalId.resetField();
            moFieldPayAccountDestNumber.resetField();
            moFieldPayAccountDest.resetField();
        }
    }
    
    private void updatePayFactoringFields() {
        boolean enable = jckPayFactoring.isEnabled() && jckPayFactoring.isSelected();
        
        jcbPayFactoringBank.setEnabled(enable);
        jrbPayFactoringPay.setEnabled(enable);
        jrbPayFactoringFee.setEnabled(enable);
        
        if (!enable) {
            jrbPayFactoringPay.setSelected(true);
            moFieldPayFactoringBank.resetField();
        }
    }
    
    private void updatePayCurrencyFields() {
        boolean enable = jcbPayCurrency.isEnabled() && jcbPayCurrency.getSelectedIndex() > 0 && !miClient.getSession().getSessionCustom().isLocalCurrency(moFieldPayCurrency.getKeyAsIntArray());
        
        jtfPayExchangeRate.setEditable(enable);
        jtfPayExchangeRate.setFocusable(enable);
        jbPayExchangeRatePick.setEnabled(enable);
    }
    
    private void enablePayControls(final boolean enable) {
        jbPayPaymentEntryAdd.setEnabled(enable);
        jbPayPaymentEntryModify.setEnabled(enable);
        jbPayPaymentEntryOk.setEnabled(false);
        jbPayPaymentEntryCancel.setEnabled(false);
        jbPayPaymentEntryDelete.setEnabled(enable);
    }
    
    private void enableDocFields(final boolean enable) {
        jbDocDpsRelatedPickPend.setEnabled(enable);
        jbDocDpsRelatedPickAll.setEnabled(enable);
        jtfDocInstallment.setEditable(enable);
        jtfDocInstallment.setFocusable(enable);
        jtfDocExchangeRate.setEditable(false);
        jtfDocExchangeRate.setFocusable(false);
        jbDocExchangeRateInvert.setEnabled(false);
        jtfDocDocBalancePrev.setEditable(enable);
        jtfDocDocBalancePrev.setFocusable(enable);
        jtfDocDocPayment.setEditable(enable);
        jtfDocDocPayment.setFocusable(enable);
        jbDocPaymentCompute.setEnabled(false);
    }
    
    private void enableDocControls(final boolean enable) {
        jbDocPaymentEntryDocAdd.setEnabled(enable);
        jbDocPaymentEntryDocModify.setEnabled(enable);
        jbDocPaymentEntryDocOk.setEnabled(false);
        jbDocPaymentEntryDocCancel.setEnabled(false);
        jbDocPaymentEntryDocDelete.setEnabled(enable);
        jbAccConceptDocsEdit.setEnabled(enable);
    }
    
    private void clearPayPayment(final boolean enableFields, final boolean enableControls) {
        enablePayFields(enableFields);
        enablePayControls(enableControls);
        
        moPaymentEntry = null;
        renderPayPaymentEntry();
    }

    private void clearDocPaymentDoc(final boolean enableFields, final boolean enableControls) {
        enableDocFields(enableFields);
        enableDocControls(enableControls);
        
        moPaymentEntryDoc = null;
        renderDocPaymentEntryDoc();
    }
    
    /**
     * Checks if docs are subject to be edited.
     * @return 
     */
    private boolean arePaymentDocsEditable() {
        return moPaneGridPayments.getSelectedTableRow() != null && !mbEditingVoucher && !mbEditingPaymentEntry;
    }
    
    /**
     * Gets remainder of provided payment in original payment currency.
     * @param paymentEntry
     * @return 
     */
    private double getPaymentRemainder(SCfdPaymentEntry paymentEntry) {
        double remainder = SLibUtils.roundAmount(paymentEntry.getRemainder());
        
        if (moPaymentEntryDoc != null) {
            // a doc is being edited, so add its payment application to the remainder:
            remainder = SLibUtils.roundAmount(remainder + moPaymentEntryDoc.PayPayment);
        }
        
        return remainder;
    }
    
    /*
     * Private and public listener methods:
     */

    private void actionPerformedVouDatePick() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldVouDate.getDate(), moFieldVouDate);
    }

    private void actionPerformedVouNext(boolean callPaymentEntryAdd) {
        boolean goNext = true;
        ArrayList<SFormField> fields = new ArrayList<>();
        
        fields.add(moFieldVouDate);
        fields.add(moFieldVouTaxRegime);
        fields.add(moFieldVouConfirm);
        fields.add(moFieldRecBizPartner);
        fields.add(moFieldRecCfdiUse);
        fields.add(moFieldRecCfdRelatedUuid);
        
        for (SFormField field : fields) {
            if (!field.validateField()) {
                goNext = false;
                field.getComponent().requestFocusInWindow();
                break;
            }
        }
        
        if (goNext) {
            mbEditingVoucher = false;
            
            jbVouNext.setEnabled(false);
            jbVouResume.setEnabled(true);
            
            enableVouFields(false);
            enableRecFields(false);
            
            clearPayPayment(false, true);
            clearDocPaymentDoc(false, arePaymentDocsEditable());
            
            if (callPaymentEntryAdd) {
                jbPayPaymentEntryAdd.doClick();  // shortcut to add new payment
            }
        }
    }

    private void actionPerformedVouResume() {
        boolean resume = true;
        
        if (!moPaneGridPayments.getGridRows().isEmpty()) {
            resume = false;
            miClient.showMsgBoxWarning("El comprobante no debe tener pagos.");
            jbPayPaymentEntryDelete.requestFocusInWindow();
        }
        
        if (resume) {
            mbEditingVoucher = true;
            
            jbVouNext.setEnabled(true);
            jbVouResume.setEnabled(false);
            
            enableVouFields(true);
            enableRecFields(true);
            
            clearPayPayment(false, false);
            clearDocPaymentDoc(false, false);
            
            if (jcbRecBizPartner.isEnabled()) {
                jcbRecBizPartner.requestFocusInWindow();
            }
            else if (jftVouDate.isEditable()) {
                jftVouDate.requestFocusInWindow();
            }
            else if (jcbVouTaxRegime.isEnabled()) {
                jcbVouTaxRegime.requestFocusInWindow();
            }
        }
    }

    private void actionPerformedRecBizPartnerPick() {
        miClient.pickOption(SDataConstants.BPSX_BP_CUS, moFieldRecBizPartner, null);
    }
    
    private void actionPerformedRecTaxRegime() {
        jtfRecTaxRegime.setEnabled(true);
        jtfRecTaxRegime.setEditable(true);
        jtfRecTaxRegime.setFocusable(true);
    }

    private void actionPerformedRecCfdRelatedAdd() {
        activateRecCfdRelated(false);
        jbRecCfdRelatedPick.requestFocusInWindow();
        jbRecCfdRelatedPick.doClick();
    }

    private void actionPerformedRecCfdRelatedDelete() {
        activateRecCfdRelated(true);
        jbRecCfdRelatedAdd.requestFocusInWindow();
    }

    private void actionPerformedRecCfdRelatedPick() {
        Object[] filterKey = new Object[] { SDataConstantsSys.TRNS_TP_CFD_PAY_REC, moDataRecBizPartner.getFiscalId() };
        
        moDialogRecDpsRelatedPicker.formReset();
        moDialogRecDpsRelatedPicker.setFilterKey(filterKey);
        moDialogRecDpsRelatedPicker.formRefreshOptionPane();
        moDialogRecDpsRelatedPicker.setFormVisible(true);

        if (moDialogRecDpsRelatedPicker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moDataRecCfdRelated = (SDataCfd) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_CFD, moDialogRecDpsRelatedPicker.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
            msRecCfdRelatedUuid = moDataRecCfdRelated.getUuid();
            renderRecCfdRelated();
        }
    }

    private void actionPerformedPayDatePick() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldPayDate.getDate(), moFieldPayDate);
    }

    private void actionPerformedPayExchangeRatePick() {
        double rate = miClient.pickExchangeRate(moFieldPayCurrency.getKeyAsIntArray()[0], moFieldPayDate.getDate());

        if (rate != 0d) {
            moFieldPayExchangeRate.setFieldValue(rate);
            jtfPayExchangeRate.requestFocusInWindow();
        }
    }

    private void actionPerformedPayRecordPick() {
        moDialogPayRecordPicker.formReset();
        moDialogPayRecordPicker.setFilterKey(moFieldPayDate.getDate());
        moDialogPayRecordPicker.formRefreshOptionPane();
        moDialogPayRecordPicker.setSelectedPrimaryKey(moDataPayRecord == null ? null : moDataPayRecord.getPrimaryKey());
        moDialogPayRecordPicker.setFormVisible(true);

        if (moDialogPayRecordPicker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            try {
                setPayRecord(moDialogPayRecordPicker.getSelectedPrimaryKey());
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
    }

    private void actionPerformedPayRecordView() {
        if (moDataPayRecord == null) {
            miClient.showMsgBoxInformation("No se ha seleccionado una póliza contable.");
        }
        else {
            miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(SDataConstants.FINX_REC_RO, moDataPayRecord.getPrimaryKey());
        }
    }
    
    /**
     * Pick a bank account of headquarters of receiver.
     */
    private void actionPerformedPayAccountSrcPick() {
        SFormOptionPickerInterface picker = miClient.getOptionPicker(SDataConstants.BPSU_BANK_ACC);

        picker.formReset();
        picker.setFilterKey(moDataRecBizPartner.getDbmsBizPartnerBranchHq().getPrimaryKey());
        picker.formRefreshOptionPane();
        //picker.setSelectedPrimaryKey(...); by now there is no way to set currently selected account cash
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            SDataBizPartnerBranchBankAccount bankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BANK_ACC, picker.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
            SDataBizPartner bank = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { bankAccount.getFkBankId() }, SLibConstants.EXEC_MODE_VERBOSE);
            
            moFieldPayAccountSrcFiscalId.setString(bank.getFiscalId());
            moFieldPayAccountSrcNumber.setString(!bankAccount.getBankAccountNumberStd().isEmpty() ? bankAccount.getBankAccountNumberStd() : SLibUtils.textTrim(bankAccount.getBankAccountBranchNumber() + " " + bankAccount.getBankAccountNumber()));
            
            if (miClient.getSession().getSessionCustom().isLocalCountry(new int[] { bank.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial().getDbmsDataCountry().getPkCountryId() })) {
                moFieldPayAccountSrcEntity.resetField(); // not required for local banks
            }
            else {
                moFieldPayAccountSrcEntity.setString(bank.getBizPartner()); // required for foreign banks
            }
            
            if (jtfPayAccountSrcEntity.isEditable()) {
                jtfPayAccountSrcEntity.requestFocusInWindow();
            }
            else {
                jtfPayAccountSrcNumber.requestFocusInWindow();
            }
        }
    }

    /**
     * Pick a bank account of current company branch of this CFDI.
     */
    private void actionPerformedPayAccountDestPick() {
        SFormOptionPickerInterface picker = miClient.getOptionPicker(SDataConstants.FIN_ACC_CASH);

        picker.formReset();
        picker.setFilterKey(moDataComBranch.getPrimaryKey());
        picker.formRefreshOptionPane();
        picker.setSelectedPrimaryKey(moFieldPayAccountDest.getKey());
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldPayAccountDest.setKey(picker.getSelectedPrimaryKey());    // will throw an item-state-changed event
            jcbPayAccountDest.requestFocusInWindow();
        }
    }

    private void actionPerformedPayPaymentEntryAdd() {
        mbEditingPaymentEntry = true;
        
        clearPayPayment(true, false);
        enableDocControls(false);
        moPaneGridPayments.getTable().setEnabled(false);
        
        jbPayPaymentEntryOk.setEnabled(true);
        jbPayPaymentEntryCancel.setEnabled(true);
        
        // default date: session's current date:
        moFieldPayDate.setFieldValue(miClient.getSession().getCurrentDate());
        
        // default hour: 12:00:
        moFieldPayTime.setFieldValue(new Date(miClient.getSession().getCurrentDate().getTime() + (1000 * 60 * 60 * 12)));
        
        // customer's default payment way, if any:
        if (!moDataRecBizPartner.getDbmsCategorySettingsCus().getCfdiPaymentWay().equals(DCfdi33Catalogs.FDP_POR_DEF)) {
            moFieldPayPaymentWay.setFieldValue(moDataRecBizPartner.getDbmsCategorySettingsCus().getCfdiPaymentWay());
            itemStateChangedPayPaymentWay();
        }
        
        // customer/s default currency, if any:
        int currency = moDataRecBizPartner.getDbmsCategorySettingsCus().getFkCurrencyId_n();
        moFieldPayCurrency.setFieldValue(currency != SLibConstants.UNDEFINED ? new int[] { currency } : miClient.getSession().getSessionCustom().getLocalCurrencyKey());
        
        jftPayDate.requestFocusInWindow();
    }

    /**
     * Public method so that it can be attachedd and called in corresponding grid's double-click-action method.
     */
    public void actionPerformedPayPaymentEntryModify() {
        if (jbPayPaymentEntryModify.isEnabled()) {
            SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
            
            if (paymentEntry == null) {
                miClient.showMsgBoxWarning(SGridConsts.MSG_SELECT_ROW);
                moPaneGridPayments.getTable().requestFocusInWindow();
            }
            else if (!paymentEntry.PaymentEntryDocs.isEmpty()) {
                miClient.showMsgBoxWarning("El pago #" + paymentEntry.EntryNumber + " no debe tener documentos relacionados para poderlo modificar.");
                moPaneGridPaymentDocs.getTable().requestFocusInWindow();
            }
            else {
                actionPerformedPayPaymentEntryAdd();
                moPaymentEntry = paymentEntry;
                moPaymentEntry.AuxGridIndex = moPaneGridPayments.getTable().getSelectedRow();
                renderPayPaymentEntry();
            }
        }
    }

    private void actionPerformedPayPaymentEntryDelete() {
        if (moPaneGridPayments.getSelectedTableRow() == null) {
            miClient.showMsgBoxWarning(SGridConsts.MSG_SELECT_ROW);
            moPaneGridPayments.getTable().requestFocusInWindow();
        }
        else if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
            int index = moPaneGridPayments.getTable().getSelectedRow();
            
            try {
                // remove current payment from grid of payments:
                SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.removeTableRow(index);
                releaseRecordLock(paymentEntry.DataRecord);
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
            finally {
                // renumber all payments:
                int number = 0;
                for (STableRow row : moPaneGridPayments.getGridRows()) {
                    SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) row;
                    paymentEntry.EntryNumber = ++number;
                    paymentEntry.prepareTableRow();
                }
                
                // update grid of payments:
                moPaneGridPayments.renderTableRows();
                if (index >= moPaneGridPayments.getTableGuiRowCount()) {
                    index = moPaneGridPayments.getTableGuiRowCount() - 1;
                }
                moPaneGridPayments.setTableRowSelection(index);
                
                computeVouTotal();
            }
        }
    }

    private void actionPerformedPayPaymentEntryOk() {
        boolean valid = true;
        SDataBizPartner factoringBank = null;
        ArrayList<SFormField> fields = new ArrayList<>();
        
        fields.add(moFieldPayDate);
        fields.add(moFieldPayTime);
        fields.add(moFieldPayPaymentWay);
        fields.add(moFieldPayCurrency);
        fields.add(moFieldPayAmount);
        fields.add(moFieldPayExchangeRate);
        fields.add(moFieldPayOperation);
        fields.add(moFieldPayAccountSrcFiscalId);
        fields.add(moFieldPayAccountSrcNumber);
        fields.add(moFieldPayAccountSrcEntity);
        fields.add(moFieldPayAccountDestFiscalId);
        fields.add(moFieldPayAccountDestNumber);
        fields.add(moFieldPayAccountDest);
        
        for (SFormField field : fields) {
            if (!field.validateField()) {
                valid = false;
                field.getComponent().requestFocusInWindow();
                break;
            }
        }
        
        String paymentWayCode = moFieldPayPaymentWay.getFieldValue().toString(); // convenience variable
        int paymentCurrencyId = moFieldPayCurrency.getKeyAsIntArray()[0]; // convenience variable
        
        if (valid) {
            if (paymentWayCode.equals(DCfdi33Catalogs.FDP_POR_DEF)) {
                valid = false;
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlPayPaymentWay.getText() + "'.");
                jcbPayPaymentWay.requestFocusInWindow();
            }
            else if (jckPayFactoring.isSelected() && jcbPayFactoringBank.getSelectedIndex() <= 0) {
                valid = false;
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jcbPayFactoringBank.getToolTipText() + "'.");
                jcbPayFactoringBank.requestFocusInWindow();
            }
            else if (jckPayFactoring.isSelected() && jrbPayFactoringFee.isSelected() && !paymentWayCode.equals(DCfdi33Catalogs.FDP_COMPENSACION)) {
                valid = false;
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlPayPaymentWay.getText() + "'.\n"
                        + "Se debe elegir la opción '" + ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs().composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY, DCfdi33Catalogs.FDP_COMPENSACION) + "'");
                jcbPayPaymentWay.requestFocusInWindow();
            }
            else if (moDataPayRecord == null) {
                valid = false;
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlPayRecord.getText() + "'.");
                jbPayRecordPick.requestFocusInWindow();
            }
            else if (!moFieldPayAccountSrcFiscalId.getString().isEmpty() && moFieldPayAccountSrcFiscalId.getString().length() != DCfdConsts.LEN_RFC_ORG && !moFieldPayAccountSrcFiscalId.getString().equals(DCfdConsts.RFC_GEN_INT)) {
                valid = false;
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + SGuiUtils.getLabelName(jlPayAccountFiscalId) + ", " + SGuiUtils.getLabelName(jlPayAccountSrc) + "':\n"
                        + "debe ser de longitud " + DCfdConsts.LEN_RFC_ORG + " o contener el valor '" + DCfdConsts.RFC_GEN_INT + "'.");
                jtfPayAccountSrcFiscalId.requestFocusInWindow();
            }
            else if (isPayerForeign() && moFieldPayAccountSrcEntity.getString().isEmpty()) {
                valid = false;
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlPayAccountEntity.getText() + "'.");
                jtfPayAccountSrcEntity.requestFocusInWindow();
            }
            else if (jcbPayAccountDest.isEnabled() && moDataPayAccountCashDest == null) {
                valid = false;
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlPayAccountDest.getText() + "'.");
                jcbPayAccountDest.requestFocusInWindow();
            }
            else if (moDataPayAccountCashDest != null && moDataPayAccountCashDest.getFkCurrencyId() != paymentCurrencyId && miClient.showMsgBoxConfirm( "La moneda del pago ("
                    + miClient.getSession().getSessionCustom().getCurrency(new int[] { paymentCurrencyId }) + ") "
                    + "no corresponde a la moneda de la cuenta beneficiaria ("
                    + miClient.getSession().getSessionCustom().getCurrency(new int[] { moDataPayAccountCashDest.getFkCurrencyId() }) + ").\n"
                    + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                valid = false;
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlPayAccountDest.getText() + "'.");
                jcbPayAccountDest.requestFocusInWindow();
            }
            else {
                if (jckPayFactoring.isSelected()) {
                    factoringBank = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, moFieldPayFactoringBank.getKeyAsIntArray(), SLibConstants.EXEC_MODE_VERBOSE);
                    
                    if (factoringBank == null) {
                        valid = false;
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_READ + "'" + jcbPayFactoringBank.getToolTipText() + "'.\n");
                        jcbPayFactoringBank.requestFocusInWindow();
                    }
                    else if (factoringBank.getFiscalId().isEmpty()) {
                        valid = false;
                        miClient.showMsgBoxWarning("El RFC de " + jcbPayFactoringBank.getToolTipText() + " está vacio.");
                        jcbPayFactoringBank.requestFocusInWindow();
                    }
                    else if (factoringBank.getFiscalId().length() != DCfdConsts.LEN_RFC_ORG) {
                        valid = false;
                        miClient.showMsgBoxWarning("El RFC de " + jcbPayFactoringBank.getToolTipText() + " (" + factoringBank.getFiscalId() + ") debe ser de longitud " + DCfdConsts.LEN_RFC_ORG + ".");
                        jcbPayFactoringBank.requestFocusInWindow();
                    }
                    else {
                        // factoring bank must be the same as other payments:
                        for (STableRow row : moPaneGridPayments.getGridRows()) {
                            SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) row;
                            if (paymentEntry.isFactoring() && paymentEntry != moPaymentEntry) {
                                if (paymentEntry.AuxFactoringBankId != factoringBank.getPkBizPartnerId()) {
                                    valid = false;
                                    miClient.showMsgBoxWarning("El " + jcbPayFactoringBank.getToolTipText() + " (" + factoringBank.getBizPartner() + ") debe ser el mismo que el de los demás pagos del comprobante.");
                                    jcbPayFactoringBank.requestFocusInWindow();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if (valid) {
            boolean isNew = moPaymentEntry == null;
            
            GregorianCalendar gcDate = new GregorianCalendar();
            GregorianCalendar gcHour = new GregorianCalendar();
            gcDate.setTime(moFieldPayDate.getDate());
            gcHour.setTime(moFieldPayTime.getTime());
            gcDate.add(Calendar.HOUR_OF_DAY, gcHour.get(Calendar.HOUR_OF_DAY));
            gcDate.add(Calendar.MINUTE, gcHour.get(Calendar.MINUTE));
            gcDate.add(Calendar.SECOND, gcHour.get(Calendar.SECOND));
            
            int paymentEntryType;
            
            if (jckPayFactoring.isSelected()) {
                if (jrbPayFactoringPay.isSelected()) {
                    paymentEntryType = SCfdPaymentEntry.TYPE_FACTORING_PAY;
                }
                else {
                    paymentEntryType = SCfdPaymentEntry.TYPE_FACTORING_FEE;
                }
            }
            else {
                paymentEntryType = SCfdPaymentEntry.TYPE_STANDARD;
            }
            
            if (isNew) {
                // adding payment...
                
                moPaymentEntry = new SCfdPaymentEntry(
                        moDataCfdPayment,
                        moPaneGridPayments.getTable().getRowCount() + 1, 
                        paymentEntryType, 
                        gcDate.getTime(), 
                        paymentWayCode, 
                        paymentCurrencyId, 
                        ((SFormComponentItem) jcbPayCurrency.getSelectedItem()).getComplement().toString(), 
                        moFieldPayAmount.getDouble(), 
                        moFieldPayExchangeRate.getDouble(), 
                        moDataPayRecord);
            }
            else {
                // modifying payment...
                
                //moPaymentEntry.EntryDocNumber
                moPaymentEntry.EntryType = paymentEntryType;
                moPaymentEntry.PaymentDate = gcDate.getTime();
                moPaymentEntry.PaymentWay = paymentWayCode;
                moPaymentEntry.CurrencyId = paymentCurrencyId;
                moPaymentEntry.CurrencyKey = ((SFormComponentItem) jcbPayCurrency.getSelectedItem()).getComplement().toString();
                moPaymentEntry.Amount = moFieldPayAmount.getDouble();
                moPaymentEntry.ExchangeRate = moFieldPayExchangeRate.getDouble();
                moPaymentEntry.DataRecord = moDataPayRecord;
                moPaymentEntry.computeAmountLocal();
            }
            
            moPaymentEntry.Operation = moFieldPayOperation.getString();
            
            if (DCfdi33Utils.notRequiredAccountPayer(moPaymentEntry.PaymentWay)) {
                moPaymentEntry.AccountSrcFiscalId = "";
                moPaymentEntry.AccountSrcNumber = "";
            }
            else {
                moPaymentEntry.AccountSrcFiscalId = moFieldPayAccountSrcFiscalId.getString();
                if (moPaymentEntry.PaymentWay.equals(DCfdi33Catalogs.FDP_CHEQUE) && moFieldPayAccountSrcNumber.getString().length() == 10) {
                    moPaymentEntry.AccountSrcNumber = "0" + moFieldPayAccountSrcNumber.getString();
                }
                else {
                    moPaymentEntry.AccountSrcNumber = moFieldPayAccountSrcNumber.getString();
                }
            }
            
            if (DCfdi33Utils.notRequiredBankPayer(moPaymentEntry.PaymentWay)) {
                moPaymentEntry.AccountSrcEntity = "";
            }
            else {
                moPaymentEntry.AccountSrcEntity = moFieldPayAccountSrcEntity.getString();
            }
            
            if (DCfdi33Utils.notRequiredAccountReceipt(moPaymentEntry.PaymentWay)) {
                moPaymentEntry.AccountDestFiscalId = "";
                moPaymentEntry.AccountDestNumber = "";
                moPaymentEntry.AccountDestKey = null;
            }
            else {
                moPaymentEntry.AccountDestFiscalId = moFieldPayAccountDestFiscalId.getString().length() != DCfdConsts.LEN_RFC_ORG ? "" : moFieldPayAccountDestFiscalId.getString();
                moPaymentEntry.AccountDestNumber = moFieldPayAccountDestNumber.getString();
                moPaymentEntry.AccountDestKey = moFieldPayAccountDest.getKeyAsIntArray();
            }
            
            if (factoringBank == null) {
                moPaymentEntry.AuxFactoringBankId = 0;
                moPaymentEntry.AuxFactoringBankFiscalId = "";
            }
            else {
                moPaymentEntry.AuxFactoringBankId = factoringBank.getPkBizPartnerId();
                moPaymentEntry.AuxFactoringBankFiscalId = factoringBank.getFiscalId();
            }
            
            moPaymentEntry.prepareTableRow();
            
            if (isNew) {
                moPaneGridPayments.addTableRow(moPaymentEntry);
                moPaneGridPayments.renderTableRows();
                moPaneGridPayments.setTableRowSelection(moPaneGridPayments.getTable().getRowCount() - 1);
            }
            else {
                moPaneGridPayments.setTableRow(moPaymentEntry, moPaymentEntry.AuxGridIndex);
                moPaneGridPayments.renderTableRows();
                moPaneGridPayments.setTableRowSelection(moPaymentEntry.AuxGridIndex);
            }
            
            computeVouTotal();
            actionPerformedPayPaymentEntryCancel();
        }
    }
    
    private void actionPerformedPayPaymentEntryCancel() {
        mbEditingPaymentEntry = false;
        
        clearPayPayment(false, true);
        enableDocControls(arePaymentDocsEditable());
        moPaneGridPayments.getTable().setEnabled(true);
        
        jbPayPaymentEntryAdd.requestFocusInWindow();
        
        valueChangedPayments(); // to force showing of currency of payment in doc fields
    }

    /**
     * 
     * @param mode DPS pending or DPS all.
     */
    private void actionPerformedDocDpsRelatedPick(int mode) {
        mbDpsValidationFailed = false;
        moThinDocDpsRelated = null;
        
        SDialogPickerDps pickerDps;
        Object[] filterKey;
        int year = SLibTimeUtils.digestYear(moFieldVouDate.getDate())[0];
        
        switch (mode) {
            case MODE_DPS_PICK_PEND:
                pickerDps = moDialogDocDpsRelatedPickerPend;
                filterKey = new Object[] { year, SDataConstantsSys.TRNS_CL_DPS_SAL_DOC, moFieldRecBizPartner.getKeyAsIntArray(), moDataCfdPayment == null ? 0 : moDataCfdPayment.getDbmsDataCfd().getPkCfdId() };
                break;
            case MODE_DPS_PICK_ALL:
                pickerDps = moDialogDocDpsRelatedPickerAll;
                filterKey = new Object[] { SDataConstantsSys.TRNS_CL_DPS_SAL_DOC, moFieldRecBizPartner.getKeyAsIntArray() };
                break;
            default:
                miClient.showMsgBoxWarning(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                return;
        }
        
        pickerDps.formReset();
        pickerDps.setFilterKey(filterKey);
        pickerDps.formRefreshOptionPane();
        pickerDps.setFormVisible(true);

        if (pickerDps.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
            
            // validate that doc has not been already added:
            
            boolean isValid = true;
            double docPayments = 0;
            int installments = 0;
            
            for (SCfdPaymentEntryDoc paymentEntryDoc : paymentEntry.PaymentEntryDocs) {
                if (SLibUtils.compareKeys(paymentEntryDoc.ThinDps.getPrimaryKey(), pickerDps.getSelectedPrimaryKey())) {
                    if (miClient.showMsgBoxConfirm("El documento relacionado " + paymentEntryDoc.ThinDps.getDpsNumber() + " ya está agregado en el pago #" + paymentEntry.EntryNumber + ".\n"
                            + "¿Está seguro que desea agregarlo otra vez?") != JOptionPane.YES_OPTION) {
                        isValid = false;
                        jbDocDpsRelatedPickPend.requestFocusInWindow();
                        break;
                    }
                    docPayments = SLibUtils.roundAmount(docPayments + paymentEntryDoc.DocPayment);
                    installments++;
                }
            }
            
            if (isValid) {
                try {
                    // read doc:

                    double[] balance = new double[] { 0, 0 };

                    moThinDocDpsRelated = new SThinDps();
                    moThinDocDpsRelated.read(pickerDps.getSelectedPrimaryKey(), miClient.getSession().getStatement());

                    renderDocDpsRelated();
                    
                    balance = SDataUtilities.obtainDpsBalance(miClient, (int[]) moThinDocDpsRelated.getPrimaryKey(), year);
                    if (docPayments != 0) {
                        balance[1] = SLibUtils.roundAmount(balance[1] + docPayments);
                    }

                    // set default doc values:
                    int installment = STrnUtilities.countDpsPayments(miClient.getSession().getStatement(), (int[]) moThinDocDpsRelated.getPrimaryKey(), moDataCfdPayment == null ? 0 : moDataCfdPayment.getDbmsDataCfd().getPkCfdId()) + installments;
                    moFieldDocInstallment.setFieldValue(installment + 1);

                    if (paymentEntry.CurrencyId == moThinDocDpsRelated.getFkCurrencyId()) {
                        moFieldDocExchangeRate.setFieldValue(1d);
                    }
                    else {
                        moFieldDocExchangeRate.setFieldValue(0d);
                    }

                    moFieldDocBalancePrev.setFieldValue(balance[1]);

                    double remainder = SLibUtils.roundAmount(getPaymentRemainder(paymentEntry) * moFieldDocExchangeRate.getDouble());
                    moFieldDocPayment.setFieldValue(balance[1] <= remainder ? balance[1] : remainder);
                    computeDocBalancePend();

                    jtfDocInstallment.requestFocusInWindow();
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionPerformedDocExchangeRateInvert() {
        moFieldDocExchangeRate.setDouble(moFieldDocExchangeRate.getDouble() == 0 ? 0 : SLibUtils.round(1d / moFieldDocExchangeRate.getDouble(), moExchangeRateFormat.getMaximumFractionDigits()));
        jtfDocExchangeRate.requestFocusInWindow();
        computeDocPaymentAmounts();
    }
    
    private void actionPerformedDocPaymentCompute() {
        SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
        double remainder = SLibUtils.roundAmount(getPaymentRemainder(paymentEntry) * moFieldDocExchangeRate.getDouble());
        
        moFieldDocPayment.setFieldValue(moFieldDocBalancePrev.getDouble() <= remainder ? moFieldDocBalancePrev.getDouble() : remainder);
        computeDocBalancePend();
        
        jtfDocDocPayment.requestFocusInWindow();
    }

    private void actionPerformedDocPaymentEntryDocAdd(boolean adding) {
        if (jbDocPaymentEntryDocAdd.isEnabled()) {
            SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
            
            if (adding && paymentEntry.EntryType == SCfdPaymentEntry.TYPE_FACTORING_FEE) {
                if (moDialogCfdPaymentFactoringFees == null) {
                    moDialogCfdPaymentFactoringFees = new SDialogCfdPaymentFactoringFees(miClient, this);
                }

                int year = SLibTimeUtils.digestYear(moFieldVouDate.getDate())[0];

                moDialogCfdPaymentFactoringFees.formReset();
                moDialogCfdPaymentFactoringFees.setParams(year, moFieldRecBizPartner.getKeyAsIntArray()[0], moDataCfdPayment == null ? 0 : moDataCfdPayment.getDbmsDataCfd().getPkCfdId(), paymentEntry);
                moDialogCfdPaymentFactoringFees.setVisible(true);

                if (moDialogCfdPaymentFactoringFees.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    // add documents of payment #1:

                    ArrayList<SCfdPaymentEntryDoc> paymentEntryDocs1 = moDialogCfdPaymentFactoringFees.getPaymentEntryDocs();

                    for (SCfdPaymentEntryDoc paymentEntryDoc : paymentEntryDocs1) {
                        paymentEntryDoc.prepareTableRow();

                        moPaneGridPaymentDocs.addTableRow(paymentEntryDoc);

                        paymentEntry.PaymentEntryDocs.add(paymentEntryDoc);
                    }

                    moPaneGridPaymentDocs.renderTableRows();
                    moPaneGridPaymentDocs.setTableRowSelection(moPaneGridPaymentDocs.getTable().getRowCount() - 1);

                    computePayTotalPayments();
                }
                else {
                    actionPerformedDocPaymentEntryDocCancel();
                }
            }
            else {
                mbEditingPaymentEntryDoc = true;

                clearDocPaymentDoc(true, false);
                enablePayControls(false);
                moPaneGridPayments.getTable().setEnabled(false);
                moPaneGridPaymentDocs.getTable().setEnabled(false);

                jbDocPaymentEntryDocOk.setEnabled(true);
                jbDocPaymentEntryDocCancel.setEnabled(true);

                if (adding) {
                    jbDocDpsRelatedPickPend.doClick(); // shortcut to pick doc
                    if (!mbDpsValidationFailed) {
                        if (moDialogDocDpsRelatedPickerPend.getFormResult() != SLibConstants.FORM_RESULT_OK) {
                            if (miClient.showMsgBoxConfirm("¿Desea buscar entre todos los documentos del deudor?") == JOptionPane.YES_OPTION) {
                                jbDocDpsRelatedPickAll.doClick(); // shortcut to pick doc
                                if (moDialogDocDpsRelatedPickerAll.getFormResult() != SLibConstants.FORM_RESULT_OK) {
                                    actionPerformedDocPaymentEntryDocCancel();
                                }
                            }
                            else {
                                actionPerformedDocPaymentEntryDocCancel();
                            }
                        }
                    }
                    else {
                        actionPerformedDocPaymentEntryDocCancel();
                    }
                }
                else {
                    jtfDocInstallment.requestFocusInWindow();
                }
            }
        }
    }

    /**
     * Public method so that it can be attachedd and called in corresponding grid's double-click-action method.
     */
    public void actionPerformedDocPaymentEntryDocModify() {
        if (jbDocPaymentEntryDocModify.isEnabled()) {
            if (moPaneGridPaymentDocs.getSelectedTableRow() == null) {
                miClient.showMsgBoxWarning(SGridConsts.MSG_SELECT_ROW);
                moPaneGridPaymentDocs.getTable().requestFocusInWindow();
            }
            else {
                actionPerformedDocPaymentEntryDocAdd(false);
                moPaymentEntryDoc = (SCfdPaymentEntryDoc) moPaneGridPaymentDocs.getSelectedTableRow();
                moPaymentEntryDoc.AuxGridIndex = moPaneGridPaymentDocs.getTable().getSelectedRow();
                renderDocPaymentEntryDoc();
            }
        }
    }

    private void actionPerformedDocPaymentEntryDocDelete() {
        if (jbDocPaymentEntryDocDelete.isEnabled()) {
            SCfdPaymentEntryDoc paymentEntryDoc = (SCfdPaymentEntryDoc) moPaneGridPaymentDocs.getSelectedTableRow();
            
            if (paymentEntryDoc == null) {
                miClient.showMsgBoxWarning(SGridConsts.MSG_SELECT_ROW);
                moPaneGridPaymentDocs.getTable().requestFocusInWindow();
            }
            else if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                int index = moPaneGridPaymentDocs.getTable().getSelectedRow();

                // remove current doc from grid of docs:
                moPaneGridPaymentDocs.removeTableRow(index);

                // remove current doc from current payment:
                SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
                paymentEntry.PaymentEntryDocs.remove(paymentEntryDoc);

                // renumber all docs:
                int number = 0;
                for (STableRow row : moPaneGridPaymentDocs.getGridRows()) {
                    SCfdPaymentEntryDoc doc = (SCfdPaymentEntryDoc) row;
                    doc.EntryDocNumber = ++number;
                    doc.prepareTableRow();
                }

                // update grid of docs:
                moPaneGridPaymentDocs.renderTableRows();
                if (index >= moPaneGridPaymentDocs.getTable().getRowCount()) {
                    index = moPaneGridPaymentDocs.getTable().getRowCount() - 1;
                }
                moPaneGridPaymentDocs.setTableRowSelection(index);

                computePayTotalPayments();
            }
        }
    }

    private void actionPerformedDocPaymentEntryDocOk() {
        boolean valid = true;
        
        if (moThinDocDpsRelated == null) {
            valid = false;
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDocDpsRelated.getText() + "'.");
            jbDocDpsRelatedPickPend.requestFocusInWindow();
        }
        else if (moThinDocDpsRelated.getThinCfd().getUuid().isEmpty()) {
            valid = false;
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDocDpsRelated.getText() + ": " + jtfDocDpsRelatedUuid.getToolTipText() + "'.");
            jbDocDpsRelatedPickPend.requestFocusInWindow();
        }
        else if (!moThinDocDpsRelated.getThinDpsCfd().getPaymentMethod().equals(DCfdi33Catalogs.MDP_PPD)) {
            valid = false;
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlDocPaymentMethod.getText() + "'.");
            jbDocDpsRelatedPickPend.requestFocusInWindow();
        }
        
        if (valid) {
            ArrayList<SFormField> fields = new ArrayList<>();

            fields.add(moFieldDocInstallment);
            fields.add(moFieldDocExchangeRate);
            fields.add(moFieldDocBalancePrev);
            fields.add(moFieldDocPayment);

            for (SFormField field : fields) {
                if (!field.validateField()) {
                    valid = false;
                    field.getComponent().requestFocusInWindow();
                    break;
                }
            }
        }
        
        if (valid) {
            // validate payment remainder:
            
            SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
            double remainder = getPaymentRemainder(paymentEntry);
            
            if (SLibUtils.parseDouble(jtfDocPayPaymentRo.getText()) > remainder) {
                valid = false;
                
                String warning = SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlDocPayment.getText() + "':\n"
                        + "no debe ser mayor que el ";
                if (paymentEntry.AuxTotalPayments > 0) {
                    // there are already related documents...
                    warning += "remanente de $" + SLibUtils.getDecimalFormatAmount().format(remainder) + " " + paymentEntry.CurrencyKey + " del ";
                }
                warning += "pago de $" + SLibUtils.getDecimalFormatAmount().format(paymentEntry.Amount) + " " + paymentEntry.CurrencyKey + ".";
                miClient.showMsgBoxWarning(warning);
                
                jtfDocDocPayment.requestFocusInWindow();
            }
        }
        
        if (valid) {
            boolean isNew = moPaymentEntryDoc == null;
            
            SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
            
            if (isNew) {
                // adding document...
                moPaymentEntryDoc = new SCfdPaymentEntryDoc(
                        paymentEntry, 
                        moThinDocDpsRelated, 
                        moPaneGridPaymentDocs.getTable().getRowCount() + 1, 
                        SCfdPaymentEntryDoc.TYPE_PAY, 
                        moFieldDocInstallment.getInteger(), 
                        moFieldDocBalancePrev.getDouble(), 
                        moFieldDocPayment.getDouble(), 
                        moFieldDocExchangeRate.getDouble());
            }
            else {
                // modifying document...
                //moPaymentEntryDoc.EntryDocNumber
                //moPaymentEntryDoc.EntryDocType
                moPaymentEntryDoc.ThinDps = moThinDocDpsRelated;
                moPaymentEntryDoc.Installment = moFieldDocInstallment.getInteger();
                moPaymentEntryDoc.DocBalancePrev = moFieldDocBalancePrev.getDouble();
                moPaymentEntryDoc.DocPayment = moFieldDocPayment.getDouble();
                moPaymentEntryDoc.ExchangeRate = moFieldDocExchangeRate.getDouble();
                
                moPaymentEntryDoc.computePaymentAmounts();
            }
            
            moPaymentEntryDoc.prepareTableRow();
            
            if (isNew) {
                moPaneGridPaymentDocs.addTableRow(moPaymentEntryDoc);
                moPaneGridPaymentDocs.renderTableRows();
                moPaneGridPaymentDocs.setTableRowSelection(moPaneGridPaymentDocs.getTable().getRowCount() - 1);
                
                paymentEntry.PaymentEntryDocs.add(moPaymentEntryDoc);
            }
            else {
                moPaneGridPaymentDocs.setTableRow(moPaymentEntryDoc, moPaymentEntryDoc.AuxGridIndex);
                moPaneGridPaymentDocs.renderTableRows();
                moPaneGridPaymentDocs.setTableRowSelection(moPaymentEntryDoc.AuxGridIndex);
                
                paymentEntry.PaymentEntryDocs.set(moPaymentEntryDoc.AuxGridIndex, moPaymentEntryDoc);
            }
            
            paymentEntry.AuxConceptDocsCustom = ""; // reset customized document numbers
            
            computePayTotalPayments();
            actionPerformedDocPaymentEntryDocCancel();
        }
    }
    
    private void actionPerformedDocPaymentEntryDocCancel() {
        mbEditingPaymentEntryDoc = false;
        
        clearDocPaymentDoc(false, true);
        enablePayControls(true);
        moPaneGridPayments.getTable().setEnabled(true);
        moPaneGridPaymentDocs.getTable().setEnabled(true);
        
        jbDocPaymentEntryDocAdd.requestFocusInWindow();
    }

    private void actionPerformedAccConceptDocsEdit() {
        if (jbAccConceptDocsEdit.isEnabled()) {
            SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
            
            if (paymentEntry == null) {
                miClient.showMsgBoxWarning(SGridConsts.MSG_SELECT_ROW);
                moPaneGridPayments.getTable().requestFocusInWindow();
            }
            else {
                String old = paymentEntry.AuxConceptDocsCustom;
                
                Object option = JOptionPane.showInputDialog(rootPane, 
                            "Especificar folios de " + CONCEPT_DOCS.toLowerCase() + ":\n(Dejar en blanco para restaurar valor predeterminado)", CONCEPT_DOCS, 
                            JOptionPane.PLAIN_MESSAGE, null, null, paymentEntry.getConceptDocs());
                
                if (option != null) {
                    paymentEntry.AuxConceptDocsCustom = option.toString().toUpperCase();

                    renderAccConceptDocs(paymentEntry);

                    if (!old.equals(paymentEntry.AuxConceptDocsCustom)) {
                        paymentEntry.prepareTableRow();
                        int index = moPaneGridPayments.getTable().getSelectedRow();
                        moPaneGridPayments.renderTableRows(); // show edition
                        moPaneGridPayments.setTableRowSelection(index);
                    }
                }
            }
        }
    }

    private void actionPerformedOk() {
        SFormValidation validation = null;

        if (jbOk.isEnabled()) {
            if (this.getFocusOwner() != null) {
                focusLost(new FocusEvent(this.getFocusOwner(), FocusEvent.FOCUS_LOST));
            }

            jbOk.requestFocusInWindow();    // this forces all pending focus lost function to be called

            validation = formValidate();

            if (validation.getIsError()) {
                if (validation.getComponent() != null) {
                    validation.getComponent().requestFocusInWindow();
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

    private void actionPerformedCancel() {
        if (jbCancel.isEnabled()) {
            if (!mbIsFormReady || mbIsFormReadOnly || mnFormStatus == SLibConstants.FORM_STATUS_READ_ONLY || miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_FORM_CLOSE) == JOptionPane.YES_OPTION) {
                try {
                    releaseAllRecordLocks();
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
                mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
                setVisible(false);
            }
        }
    }

    private void itemStateChangedRecBizPartner() {
        activateRecCfdRelated(true);    // clears receptor CFD related, and allows user to add it, MUST be called here!
        
        if (jcbRecBizPartner.getSelectedIndex() <= 0) {
            moDataRecBizPartner = null;
            jtfRecFiscalIdRo.setText("");
            jtfRecCountryRo.setText("");
            jtfRecForeignFiscalIdRo.setText("");
            jtfRecTaxRegime.setText("");
            moFieldRecCfdiUse.resetField();
            moFieldRecCfdRelatedUuid.resetField();
            
            jbRecCfdRelatedAdd.setEnabled(false);
        }
        else {
            moDataRecBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, moFieldRecBizPartner.getKeyAsIntArray(), SLibConstants.EXEC_MODE_VERBOSE);
            
            jtfRecFiscalIdRo.setText(moDataRecBizPartner.getFiscalId());
            jtfRecCountryRo.setText(moDataRecBizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial().getDbmsDataCountry().getCountryCode());
            jtfRecForeignFiscalIdRo.setText(moDataRecBizPartner.getFiscalFrgId());
            jtfRecTaxRegime.setText(moDataRecBizPartner.getDbmsCategorySettingsCus().getTaxRegime());
            
            jtfRecFiscalIdRo.setCaretPosition(0);
            jtfRecCountryRo.setCaretPosition(0);
            jtfRecForeignFiscalIdRo.setCaretPosition(0);
            jtfRecTaxRegime.setCaretPosition(0); 
            
            switch (((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdTypeXmlTypes().get(moDataCfdPayment.getDbmsDataCfd().getFkCfdTypeId())) {
                case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                    moFieldRecCfdiUse.setFieldValue(DCfdi33Catalogs.CFDI_USO_POR_DEF);
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                    moFieldRecCfdiUse.setFieldValue(DCfdi40Catalogs.ClaveUsoCfdiPagos);
                    break;
                default:
                    // do nothing
            }
            
            moFieldRecCfdRelatedUuid.setFieldValue("");
        }
    }

    private void itemStateChangedPayPaymentWay() {
        updatePayPaymentWayFields();
    }

    private void itemStateChangedPayCurrency() {
        moFieldPayAmount.resetField();
        moFieldPayExchangeRate.resetField();
        
        if (jcbPayCurrency.getSelectedIndex() <= 0) {
            jtfPayAmountCurRo.setText("");
            jtfDocPayBalancePrevCurRo.setText("");
            jtfDocPayPaymentCurRo.setText("");
            jtfDocPayBalancePendCurRo.setText("");
            jtfPayTotalPaymentsCurRo.setText("");
        }
        else {
            String payCurrencyKey = ((SFormComponentItem) jcbPayCurrency.getSelectedItem()).getComplement().toString();
            
            jtfPayAmountCurRo.setText(payCurrencyKey);
            jtfDocPayBalancePrevCurRo.setText(payCurrencyKey);
            jtfDocPayPaymentCurRo.setText(payCurrencyKey);
            jtfDocPayBalancePendCurRo.setText(payCurrencyKey);
            jtfPayTotalPaymentsCurRo.setText(payCurrencyKey);
            
            if (miClient.getSession().getSessionCustom().isLocalCurrency(moFieldPayCurrency.getKeyAsIntArray())) {
                moFieldPayExchangeRate.setFieldValue(1d);
            }
        }
        
        jtfDocExchangeRateCurRo.setText(""); // allways clear doc exchange rate when payment currency is changing
        
        updatePayCurrencyFields();
        computePayAmountLocal();
    }

    private void itemStateChangedPayAccountDest() {
        if (jcbPayAccountDest.getSelectedIndex() <= 0) {
            moDataPayAccountCashDest = null;
            
            moFieldPayAccountDestFiscalId.resetField();
            moFieldPayAccountDestNumber.resetField();
        }
        else {
            moDataPayAccountCashDest = (SDataAccountCash) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC_CASH, moFieldPayAccountDest.getKey(), SLibConstants.EXEC_MODE_VERBOSE);
            String fiscalId = "";
            String number = "";
            
            if (moDataPayAccountCashDest.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_BANK) {
                SDataBizPartnerBranchBankAccount bankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry(
                        miClient, SDataConstants.BPSU_BANK_ACC, new int[] { moDataPayAccountCashDest.getFkBizPartnerBranchId_n(), moDataPayAccountCashDest.getFkBankAccountId_n() }, SLibConstants.EXEC_MODE_VERBOSE);
                SDataBizPartner bank = (SDataBizPartner) SDataUtilities.readRegistry(
                        miClient, SDataConstants.BPSU_BP, new int[] { bankAccount.getFkBankId() }, SLibConstants.EXEC_MODE_VERBOSE);
                
                fiscalId = bank.getFiscalId();
                number = !bankAccount.getBankAccountNumberStd().isEmpty() ? bankAccount.getBankAccountNumberStd() : SLibUtils.textTrim(bankAccount.getBankAccountBranchNumber() + " " + bankAccount.getBankAccountNumber());
            }
            
            moFieldPayAccountDestFiscalId.setString(fiscalId);
            moFieldPayAccountDestNumber.setString(number);
        }
    }
    
    private void itemStateChangedPayFactoring() {
        updatePayFactoringFields();
    }
    
    private void focusGainedRecCfdRelatedUuid() {
        msRecCfdRelatedUuidOnFocusGained = jtfRecCfdRelatedUuid.getText();
    }

    private void focusLostRecCfdRelatedUuid() {
        msRecCfdRelatedUuid = jtfRecCfdRelatedUuid.getText();
        if (!msRecCfdRelatedUuid.equals(msRecCfdRelatedUuidOnFocusGained)) {
            moDataRecCfdRelated = null;
            renderRecCfdRelated();
        }
    }

    private void focusLostPayExchangeRate() {
        computePayAmountLocal();
    }

    private void focusLostDocExchangeRate() {
        computeDocPaymentAmounts();
    }

    private void focusLostDocPayment() {
        computeDocBalancePend();
    }
    
    private void focusLostRecTaxRegime() {
        String description = moXmlCatalogs.getEntryName(SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, jtfRecTaxRegime.getText());
        if (description.isEmpty()) {
            miClient.showMsgBoxWarning("El régimen fiscal del receptor no existe, favor de verificar.");
        }
    }
    
    private void valueChangedPayments() {
        // show docs of current payment, if any:
        
        moPaneGridPaymentDocs.clearTableRows();
        
        SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
        
        if (paymentEntry != null) {
            for (SCfdPaymentEntryDoc paymentEntryDoc : paymentEntry.PaymentEntryDocs) {
                moPaneGridPaymentDocs.addTableRow(paymentEntryDoc);
            }
            moPaneGridPaymentDocs.renderTableRows();
            moPaneGridPaymentDocs.setTableRowSelection(0);
        }
        
        // update doc fields and controls, as required:
        clearDocPaymentDoc(false, arePaymentDocsEditable());
        computePayTotalPayments();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgFactoring;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton jbAccConceptDocsEdit;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDocDpsRelatedPickAll;
    private javax.swing.JButton jbDocDpsRelatedPickPend;
    private javax.swing.JButton jbDocExchangeRateInvert;
    private javax.swing.JButton jbDocPaymentCompute;
    private javax.swing.JButton jbDocPaymentEntryDocAdd;
    private javax.swing.JButton jbDocPaymentEntryDocCancel;
    private javax.swing.JButton jbDocPaymentEntryDocDelete;
    private javax.swing.JButton jbDocPaymentEntryDocModify;
    private javax.swing.JButton jbDocPaymentEntryDocOk;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPayAccountDestPick;
    private javax.swing.JButton jbPayAccountSrcPick;
    private javax.swing.JButton jbPayDatePick;
    private javax.swing.JButton jbPayExchangeRatePick;
    private javax.swing.JButton jbPayPaymentEntryAdd;
    private javax.swing.JButton jbPayPaymentEntryCancel;
    private javax.swing.JButton jbPayPaymentEntryDelete;
    private javax.swing.JButton jbPayPaymentEntryModify;
    private javax.swing.JButton jbPayPaymentEntryOk;
    private javax.swing.JButton jbPayRecordPick;
    private javax.swing.JButton jbPayRecordView;
    private javax.swing.JButton jbRecBizPartnerPick;
    private javax.swing.JButton jbRecCfdRelatedAdd;
    private javax.swing.JButton jbRecCfdRelatedDelete;
    private javax.swing.JButton jbRecCfdRelatedPick;
    private javax.swing.JButton jbRecTaxRegime;
    private javax.swing.JButton jbVouDatePick;
    private javax.swing.JButton jbVouNext;
    private javax.swing.JButton jbVouResume;
    private javax.swing.JComboBox<SFormComponentItem> jcbPayAccountDest;
    private javax.swing.JComboBox jcbPayCurrency;
    private javax.swing.JComboBox<SFormComponentItem> jcbPayFactoringBank;
    private javax.swing.JComboBox jcbPayPaymentWay;
    private javax.swing.JComboBox jcbRecBizPartner;
    private javax.swing.JComboBox jcbRecCfdiUsage;
    private javax.swing.JComboBox jcbVouTaxRegime;
    private javax.swing.JCheckBox jckPayFactoring;
    private javax.swing.JFormattedTextField jftPayDate;
    private javax.swing.JFormattedTextField jftPayTime;
    private javax.swing.JFormattedTextField jftVouDate;
    private javax.swing.JLabel jlDocBalancePend;
    private javax.swing.JLabel jlDocBalancePrev;
    private javax.swing.JLabel jlDocCounter;
    private javax.swing.JLabel jlDocCurrency;
    private javax.swing.JLabel jlDocDpsRelated;
    private javax.swing.JLabel jlDocExchangeRate;
    private javax.swing.JLabel jlDocInstallment;
    private javax.swing.JLabel jlDocPayment;
    private javax.swing.JLabel jlDocPaymentMethod;
    private javax.swing.JLabel jlDocPaymentType;
    private javax.swing.JLabel jlPayAccount;
    private javax.swing.JLabel jlPayAccountDest;
    private javax.swing.JLabel jlPayAccountEntity;
    private javax.swing.JLabel jlPayAccountFiscalId;
    private javax.swing.JLabel jlPayAccountNumber;
    private javax.swing.JLabel jlPayAccountSrc;
    private javax.swing.JLabel jlPayAmount;
    private javax.swing.JLabel jlPayCounter;
    private javax.swing.JLabel jlPayCurrency;
    private javax.swing.JLabel jlPayDate;
    private javax.swing.JLabel jlPayFactoringBank;
    private javax.swing.JLabel jlPayOperation;
    private javax.swing.JLabel jlPayPaymentWay;
    private javax.swing.JLabel jlPayRecord;
    private javax.swing.JLabel jlPayTime;
    private javax.swing.JLabel jlPayTotalPayments;
    private javax.swing.JLabel jlRecBizPartner;
    private javax.swing.JLabel jlRecCfdRelated;
    private javax.swing.JLabel jlRecCfdiUsage;
    private javax.swing.JLabel jlRecFiscalId;
    private javax.swing.JLabel jlRecRelationType;
    private javax.swing.JLabel jlVouBranch;
    private javax.swing.JLabel jlVouConfirm;
    private javax.swing.JLabel jlVouDate;
    private javax.swing.JLabel jlVouNumber;
    private javax.swing.JLabel jlVouTaxRegime;
    private javax.swing.JLabel jlVouTotal;
    private javax.swing.JPanel jpAccountingConcept;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpDialog;
    private javax.swing.JPanel jpDocument;
    private javax.swing.JPanel jpDocuments;
    private javax.swing.JPanel jpPayment;
    private javax.swing.JPanel jpPaymentTotals;
    private javax.swing.JPanel jpPayments;
    private javax.swing.JPanel jpRegistry;
    private javax.swing.JPanel jpRegistryReceptor;
    private javax.swing.JPanel jpRegistryRows;
    private javax.swing.JPanel jpRegistryVoucher;
    private javax.swing.JPanel jpVoucherTotals;
    private javax.swing.JRadioButton jrbPayFactoringFee;
    private javax.swing.JRadioButton jrbPayFactoringPay;
    private javax.swing.JTextField jtfAccConceptDocsRo;
    private javax.swing.JTextField jtfAccConceptPrefixRo;
    private javax.swing.JTextField jtfDocCurrencyRo;
    private javax.swing.JTextField jtfDocDocBalancePend;
    private javax.swing.JTextField jtfDocDocBalancePendCurRo;
    private javax.swing.JTextField jtfDocDocBalancePrev;
    private javax.swing.JTextField jtfDocDocBalancePrevCurRo;
    private javax.swing.JTextField jtfDocDocPayment;
    private javax.swing.JTextField jtfDocDocPaymentCurRo;
    private javax.swing.JTextField jtfDocDpsRelatedNumberRo;
    private javax.swing.JTextField jtfDocDpsRelatedUuid;
    private javax.swing.JTextField jtfDocDpsRelatedVersionRo;
    private javax.swing.JTextField jtfDocExchangeRate;
    private javax.swing.JTextField jtfDocExchangeRateCurRo;
    private javax.swing.JTextField jtfDocInstallment;
    private javax.swing.JTextField jtfDocPayBalancePendCurRo;
    private javax.swing.JTextField jtfDocPayBalancePendRo;
    private javax.swing.JTextField jtfDocPayBalancePrevCurRo;
    private javax.swing.JTextField jtfDocPayBalancePrevRo;
    private javax.swing.JTextField jtfDocPayPaymentCurRo;
    private javax.swing.JTextField jtfDocPayPaymentRo;
    private javax.swing.JTextField jtfDocPaymentMethodRo;
    private javax.swing.JTextField jtfDocPaymentTypeRo;
    private javax.swing.JTextField jtfPayAccountDestFiscalId;
    private javax.swing.JTextField jtfPayAccountDestNumber;
    private javax.swing.JTextField jtfPayAccountSrcEntity;
    private javax.swing.JTextField jtfPayAccountSrcFiscalId;
    private javax.swing.JTextField jtfPayAccountSrcNumber;
    private javax.swing.JTextField jtfPayAmount;
    private javax.swing.JTextField jtfPayAmountCurRo;
    private javax.swing.JTextField jtfPayAmountLocalCurRo;
    private javax.swing.JTextField jtfPayAmountLocalRo;
    private javax.swing.JTextField jtfPayExchangeRate;
    private javax.swing.JTextField jtfPayOperation;
    private javax.swing.JTextField jtfPayRecordBranchCodeRo;
    private javax.swing.JTextField jtfPayRecordDateRo;
    private javax.swing.JTextField jtfPayRecordNumberRo;
    private javax.swing.JTextField jtfPayTotalPaymentsCurRo;
    private javax.swing.JTextField jtfPayTotalPaymentsLocalCurRo;
    private javax.swing.JTextField jtfPayTotalPaymentsLocalRo;
    private javax.swing.JTextField jtfPayTotalPaymentsRo;
    private javax.swing.JTextField jtfRecCfdRelatedNumberRo;
    private javax.swing.JTextField jtfRecCfdRelatedUuid;
    private javax.swing.JTextField jtfRecCfdRelatedVersionRo;
    private javax.swing.JTextField jtfRecCountryRo;
    private javax.swing.JTextField jtfRecFiscalIdRo;
    private javax.swing.JTextField jtfRecForeignFiscalIdRo;
    private javax.swing.JTextField jtfRecRelationTypeRo;
    private javax.swing.JTextField jtfRecTaxRegime;
    private javax.swing.JTextField jtfVouBranchCodeRo;
    private javax.swing.JTextField jtfVouBranchNameRo;
    private javax.swing.JTextField jtfVouConfirm;
    private javax.swing.JTextField jtfVouDatetimeRo;
    private javax.swing.JTextField jtfVouNumberRo;
    private javax.swing.JTextField jtfVouPlaceIssueRo;
    private javax.swing.JTextField jtfVouStatusRo;
    private javax.swing.JTextField jtfVouTotalCurRo;
    private javax.swing.JTextField jtfVouTotalLocalCurRo;
    private javax.swing.JTextField jtfVouTotalLocalRo;
    private javax.swing.JTextField jtfVouTotalRo;
    private javax.swing.JTextField jtfVouUuidRo;
    private javax.swing.JTextField jtfVouVersionRo;
    // End of variables declaration//GEN-END:variables

    /*
     * Public methods
     */
    
    public DpsRelatedInfo getDpsRelatedInfo(final String uuid) {
        int lastInstallment = 0;
        double payments = 0;
        
        for (STableRow row : moPaneGridPayments.getGridRows()) {
            SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) row;
            for (SCfdPaymentEntryDoc paymentEntryDoc : paymentEntry.PaymentEntryDocs) {
                if (paymentEntryDoc.ThinDps.getThinCfd().getUuid().equals(uuid)) {
                    payments = SLibUtils.roundAmount(payments + paymentEntryDoc.DocPayment);
                    if (paymentEntryDoc.Installment > lastInstallment) {
                        lastInstallment = paymentEntryDoc.Installment;
                    }
                }
            }
        }
        
        return new DpsRelatedInfo(uuid, lastInstallment, payments);
    }
    
    /*
     * Public overriden methods
     */
    
    @Override
    public void formClearRegistry() {

    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbIsFirstTime = true;
        mbIsFormReady = true;
        mbIsFormReadOnly = false;
        mbIsFormReseting = false;
        mbEditingVoucher = true;
        mbEditingPaymentEntry = false;
        mbEditingPaymentEntryDoc = false;
        
        moDataCfdPayment = null;
        moDataComBranch = null;
        moDataRecBizPartner = null;
        moDataRecCfdRelated = null;
        msRecCfdRelatedUuid = "";
        msRecCfdRelatedUuidOnFocusGained = "";
        
        moDataPayRecord = null;
        moDataPayAccountCashDest = null;
        moThinDocDpsRelated = null;
        moPaymentEntry = null;
        moPaymentEntryDoc = null;
        
        moPaneGridPayments.createTable(this);   // does need list-selection listener!
        moPaneGridPayments.clearTableRows();
        moPaneGridPaymentDocs.createTable();    // does not need list-selection listener!
        moPaneGridPaymentDocs.clearTableRows();
        
        msXmlRelationType = "";
        moBackgroundDefaultColor = jtfAccConceptDocsRo.getBackground();
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
        moRecordLocksMap = new HashMap<>();
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        moRecordRedisLocksMap = new HashMap<>();
        */
        moRecordSLocksMap = new HashMap<>();
        for (int i = 0; i < mvFields.size(); i++) {
            mvFields.get(i).resetField();
        }
        
        moFieldVouDate.setFieldValue(miClient.getSession().getCurrentDate());
        moFieldVouTaxRegime.setFieldValue(miClient.getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdRegimenFiscal());
                
        jtfVouNumberRo.setText("");
        jtfVouUuidRo.setText("");
        jtfVouVersionRo.setText("");
        jtfVouBranchNameRo.setText("");
        jtfVouBranchCodeRo.setText("");
        jtfVouPlaceIssueRo.setText("");
        jtfVouDatetimeRo.setText("");
        jtfVouStatusRo.setText("");
        
        actionPerformedVouResume();
        itemStateChangedRecBizPartner();
        
        computeVouTotal();
        computePayTotalPayments();
        
        jbOk.setEnabled(true);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbRecBizPartner, SDataConstants.BPSX_BP_CUS);
        SFormUtilities.populateComboBox(miClient, jcbPayFactoringBank, SDataConstants.BPSX_BP_ATT_BANK);
        SFormUtilities.populateComboBox(miClient, jcbPayCurrency, SDataConstants.CFGU_CUR);
    }
    
    @Override
    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();
        
        for (SFormField field : mvFields) {
            if (!((erp.lib.form.SFormField) field).validateField()) {
                validation.setIsError(true);
                validation.setComponent(field.getComponent());
                break;
            }
        }
        
        if (!validation.getIsError()) {
            String description;
            description = moXmlCatalogs.getEntryName(SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, jtfRecTaxRegime.getText());
            if (description.isEmpty()) {
                validation.setMessage("El régimen fiscal del receptor no existe, favor de verificar.");
                validation.setComponent(jtfRecTaxRegime);
            }
        }
        
        if (!validation.getIsError()) {
            if (mbEditingPaymentEntry) {
                validation.setMessage("La captura de un pago está en proceso.\n"
                        + "Se debe aceptar o cancelar antes de guardar este comprobante.");
                validation.setComponent(jbPayPaymentEntryCancel);
            }
            else if (mbEditingPaymentEntryDoc) {
                validation.setMessage("La captura de un pago a un documento relacionado está en proceso.\n"
                        + "Se debe aceptar o cancelar antes de guardar este comprobante.");
                validation.setComponent(jbDocPaymentEntryDocCancel);
            }
            else if (!SDataUtilities.isPeriodOpen(miClient, moFieldVouDate.getDate())) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
                validation.setComponent(jftVouDate.isEditable() ? jftVouDate : jbVouResume);
            }
            else if (moPaneGridPayments.getTable().getRowCount() == 0 && miClient.showMsgBoxConfirm(
                    "El comprobante no tiene pagos.\n"
                    + SLibConstants.MSG_CNF_MSG_CONT) != JOptionPane.YES_OPTION) {
                validation.setMessage("El comprobante debe tener pagos.");
                validation.setComponent(moPaneGridPayments.getTable());
            }
            else {
                int index = 0;
                int factoringPayments = 0;
                HashSet<String> factoringBankFiscalIdsSet = new HashSet<>();
                
                for (STableRow row : moPaneGridPayments.getGridRows()) {
                    SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) row;
                    
                    // validate factoring:
                    if (paymentEntry.isFactoring()) {
                        factoringPayments++;
                        
                        // even though fiscal ID is not mandatory, is need to issue payment receipt of factoring:
                        if (paymentEntry.AuxFactoringBankFiscalId.isEmpty()) {
                            validation.setMessage("En el pago #" + paymentEntry.EntryNumber + " el RFC de " + jcbPayFactoringBank.getToolTipText() + " está vacio.");
                            validation.setComponent(jcbPayFactoringBank);
                            moPaneGridPayments.setTableRowSelection(index);
                            break;
                        }
                        else if (paymentEntry.AuxFactoringBankFiscalId.length() != DCfdConsts.LEN_RFC_ORG) {
                            validation.setMessage("En el pago #" + paymentEntry.EntryNumber + " el RFC de " + jcbPayFactoringBank.getToolTipText() + " (" + paymentEntry.AuxFactoringBankFiscalId + ") debe ser de longitud " + DCfdConsts.LEN_RFC_ORG + ".");
                            validation.setComponent(jcbPayFactoringBank);
                            moPaneGridPayments.setTableRowSelection(index);
                            break;
                        }
                        
                        factoringBankFiscalIdsSet.add(paymentEntry.AuxFactoringBankFiscalId);
                    }
                    
                    /*
                    For each payment validate that:
                    1. period (month) of date is open
                    2. period (month) of date of accounting voucher (record) is open
                    3. payment is totally applied
                    */
                    
                    if (!SDataUtilities.isPeriodOpen(miClient, paymentEntry.PaymentDate)) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_CLOSE + "\n"
                                + "El pago #" + paymentEntry.EntryNumber + " tiene fecha " + SLibUtils.DateFormatDate.format(paymentEntry.PaymentDate) + ".");
                        validation.setComponent(moPaneGridPayments.getTable());
                        moPaneGridPayments.setTableRowSelection(index);
                        break;
                    }
                    else if (!SDataUtilities.isPeriodOpen(miClient, paymentEntry.DataRecord.getDate())) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_CLOSE + "\n"
                                + "En el pago #" + paymentEntry.EntryNumber + " la póliza contable '" + paymentEntry.DataRecord.getRecordPrimaryKey() + "' "
                                + "tiene fecha " + SLibUtils.DateFormatDate.format(paymentEntry.DataRecord.getDate()) + ".");
                        validation.setComponent(moPaneGridPayments.getTable());
                        moPaneGridPayments.setTableRowSelection(index);
                        break;
                    }
                    else if (paymentEntry.PaymentEntryDocs.isEmpty()) {
                        validation.setMessage("El pago #" + paymentEntry.EntryNumber + " no tiene documentos relacionados.");
                        validation.setComponent(moPaneGridPayments.getTable());
                        moPaneGridPayments.setTableRowSelection(index);
                        break;
                    }
                    else {
                        String currency;
                        String message;
                        String confirm;
                        
                        paymentEntry.resetAllowances();
                        
                        if (!SLibUtils.compareAmount(paymentEntry.AuxTotalPayments, paymentEntry.Amount)) {
                            currency = paymentEntry.CurrencyKey;

                            if (paymentEntry.AuxTotalPayments > paymentEntry.Amount) {
                                /*
                                The sum of aplication of individual payments to documents CANNOT be greater than the amount of payment.
                                Note that due to the validations in each individual payment, this case should not happen never!
                                */

                                message = "En el pago #" + paymentEntry.EntryNumber + " "
                                        + "la suma total de pagos a documentos relacionados en la moneda del PAGO (" + currency + "), $"
                                        + SLibUtils.getDecimalFormatAmount().format(paymentEntry.AuxTotalPayments) + " " + currency + ",\n"
                                        + "es MAYOR que el monto del pago mismo, $"
                                        + SLibUtils.getDecimalFormatAmount().format(paymentEntry.Amount) + " " + currency + ", "
                                        + "por $" + SLibUtils.getDecimalFormatAmountUnitary().format(Math.abs(paymentEntry.AuxTotalPayments - paymentEntry.Amount)) + " " + currency + ".";

                                validation.setMessage(message);
                                validation.setComponent(moPaneGridPayments.getTable());
                                moPaneGridPayments.setTableRowSelection(index);
                                break;
                            }
                            else if (paymentEntry.AuxTotalPayments < paymentEntry.Amount) {
                                /*
                                The sum of aplication of individual payments to documents CAN be less than the amount of payment, only if approved.
                                */

                                confirm = "En el pago #" + paymentEntry.EntryNumber + " "
                                        + "la suma total de pagos a documentos relacionados en la moneda del PAGO (" + currency + "), $"
                                        + SLibUtils.getDecimalFormatAmount().format(paymentEntry.AuxTotalPayments) + " " + currency + ",\n"
                                        + "es MENOR que el monto del pago mismo, $"
                                        + SLibUtils.getDecimalFormatAmount().format(paymentEntry.Amount) + " " + currency + ", "
                                        + "por $" + SLibUtils.getDecimalFormatAmountUnitary().format(Math.abs(paymentEntry.AuxTotalPayments - paymentEntry.Amount)) + " " + currency + ".\n"
                                        + "¡ADVERTENCIA: Será necesario cuadrar manualmente la contabilización de este pago!\n"
                                        + SLibConstants.MSG_CNF_MSG_CONT;

                                if (miClient.showMsgBoxConfirm(confirm) == JOptionPane.YES_OPTION) {
                                    paymentEntry.AuxAllowTotalPaymentsLessThanAmount = true;
                                }
                                else {
                                    message = "Modificar los pagos a los documentos relacionados del pago #" + paymentEntry.EntryNumber + " en la moneda del PAGO (" + currency + ").";

                                    validation.setMessage(message);
                                    validation.setComponent(moPaneGridPayments.getTable());
                                    moPaneGridPayments.setTableRowSelection(index);
                                    break;
                                }
                            }
                        }
                        
                        if (!validation.getIsError()) {
                            boolean isPaymentLocal = miClient.getSession().getSessionCustom().isLocalCurrency(new int[] { paymentEntry.CurrencyId });
                            boolean isPaymentCompletelyLocal = isPaymentLocal;

                            if (isPaymentCompletelyLocal) {
                                for (SCfdPaymentEntryDoc doc : paymentEntry.PaymentEntryDocs) {
                                    if (!miClient.getSession().getSessionCustom().isLocalCurrency(new int[] { doc.ThinDps.getFkCurrencyId() })) {
                                        isPaymentCompletelyLocal = false;
                                        break;
                                    }
                                }
                            }
                            
                            if (!isPaymentCompletelyLocal) {
                                if (!SLibUtils.compareAmount(paymentEntry.AuxTotalPaymentsLocal, paymentEntry.AmountLocal)) {
                                    // check if payment and related documents are all in local currency:

                                    currency = miClient.getSession().getSessionCustom().getLocalCurrencyCode();

                                    if (paymentEntry.AuxTotalPaymentsLocal > paymentEntry.AmountLocal) {
                                        /*
                                        if payment IS completely local, then the sum of aplication of individual payments to documents CANNOT be greater than the amount of payment:
                                        if payment IS NOT completely local, then the sum of aplication of individual payments to documents CAN be greater than the amount of payment, only if approved:
                                        */

                                        confirm = "En el pago #" + paymentEntry.EntryNumber + " "
                                                + "la suma total de pagos a documentos relacionados en la moneda LOCAL (" + currency + "), $"
                                                + SLibUtils.getDecimalFormatAmount().format(paymentEntry.AuxTotalPaymentsLocal) + " " + currency + ",\n"
                                                + "es MAYOR que el monto del pago mismo, $"
                                                + SLibUtils.getDecimalFormatAmount().format(paymentEntry.AmountLocal) + " " + currency + ", "
                                                + "por $" + SLibUtils.getDecimalFormatAmountUnitary().format(Math.abs(paymentEntry.AuxTotalPaymentsLocal - paymentEntry.AmountLocal)) + " " + currency + "\n."
                                                + "!ADVERTENCIA: "
                                                + (paymentEntry.isAmountTotallyApplied() ?
                                                    "Se agregará un ajuste contable para compensar esta diferencia cambiaria!" :
                                                    "Será necesario cuadrar manualmente la contabilización de este pago!") + "\n"
                                                + SLibConstants.MSG_CNF_MSG_CONT;

                                        if (miClient.showMsgBoxConfirm(confirm) == JOptionPane.YES_OPTION) {
                                            paymentEntry.AuxAllowTotalPaymentsLocalGreaterThanAmountLocal = true;
                                        }
                                        else {
                                            message = "Modificar los pagos a los documentos relacionados del pago #" + paymentEntry.EntryNumber + " en la moneda LOCAL (" + currency + ").";

                                            validation.setMessage(message);
                                            validation.setComponent(moPaneGridPayments.getTable());
                                            moPaneGridPayments.setTableRowSelection(index);
                                            break;
                                        }
                                    }
                                    else if (paymentEntry.AuxTotalPaymentsLocal < paymentEntry.AmountLocal && (!isPaymentLocal || (isPaymentLocal && !paymentEntry.AuxAllowTotalPaymentsLessThanAmount))) {
                                        /*
                                        The sum of aplication of individual payments to documents CAN be less than the amount of payment, only if approved.
                                        */

                                        confirm = "En el pago #" + paymentEntry.EntryNumber + " "
                                                + "la suma total de pagos a documentos relacionados en la moneda LOCAL (" + currency + "), $"
                                                + SLibUtils.getDecimalFormatAmount().format(paymentEntry.AuxTotalPaymentsLocal) + " " + currency + ",\n"
                                                + "es MENOR que el monto del pago mismo, $"
                                                + SLibUtils.getDecimalFormatAmount().format(paymentEntry.AmountLocal) + " " + currency + ", "
                                                + "por $" + SLibUtils.getDecimalFormatAmountUnitary().format(Math.abs(paymentEntry.AuxTotalPaymentsLocal - paymentEntry.AmountLocal)) + " " + currency + "\n."
                                                + "!ADVERTENCIA: "
                                                + (paymentEntry.isAmountTotallyApplied() ?
                                                    "Se agregará un ajuste contable para compensar esta diferencia cambiaria!" :
                                                    "Será necesario cuadrar manualmente la contabilización de este pago!") + "\n"
                                                + SLibConstants.MSG_CNF_MSG_CONT;

                                        if (miClient.showMsgBoxConfirm(confirm) == JOptionPane.YES_OPTION) {
                                            paymentEntry.AuxAllowTotalPaymentsLocalLessThanAmountLocal = true;
                                        }
                                        else {
                                            message = "Modificar los pagos a los documentos relacionados del pago #" + paymentEntry.EntryNumber + " en la moneda LOCAL (" + currency + ").";

                                            validation.setMessage(message);
                                            validation.setComponent(moPaneGridPayments.getTable());
                                            moPaneGridPayments.setTableRowSelection(index);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // warn user about manual accounting required for payments without associated bank account (all payments of this kind come without it):
                    
                    if (paymentEntry.AccountDestKey == null) {
                        SCfdXmlCatalogs xmlCatalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
                        String message = "La forma de pago del pago #" + paymentEntry.EntryNumber + ", '" + xmlCatalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY, paymentEntry.PaymentWay) + "', no requiere de cuenta bancaria.\n"
                                + "¡ADVERTENCIA: Será necesario cuadrar manualmente la contabilización de este pago!\n"
                                + SLibConstants.MSG_CNF_MSG_CONT;
                        if (miClient.showMsgBoxConfirm(message) != JOptionPane.YES_OPTION) {
                            validation.setMessage("En el pago #" + paymentEntry.EntryNumber + " " + SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF.toLowerCase() + "'" + jlPayPaymentWay.getText() + "'");
                            validation.setComponent(moPaneGridPayments.getTable());
                            moPaneGridPayments.setTableRowSelection(index);
                            break;
                        }
                    }
                    
                    index++;
                }
                
                if (!validation.getIsError()) {
                    if (factoringPayments > 0) {
                        if (factoringPayments != moPaneGridPayments.getGridRows().size()) {
                            validation.setMessage("Todos los pagos del comprobante deben ser con factoraje y con la misma entidad (banco o institución financiera).");
                            validation.setComponent(moPaneGridPayments.getTable());
                            moPaneGridPayments.setTableRowSelection(index);
                        }
                        else if (factoringBankFiscalIdsSet.size() > 1) {
                            validation.setMessage("Todos los pagos del comprobante por ser con factoraje deben ser con la misma entidad (banco o institución financiera).");
                            validation.setComponent(moPaneGridPayments.getTable());
                            moPaneGridPayments.setTableRowSelection(0); // if conditions is true, there is at least one payment
                        }
                    }
                }
            }
        }
        
        // process journal voucher movements:
        
        if (!validation.getIsError()) {
            int index = 0;
            SDataCfdPayment dummyPayment = new SDataCfdPayment();
            dummyPayment.getDbmsDataCfd().setTimestamp(moFieldVouDate.getDate());
            dummyPayment.setAuxCfdDbmsDataReceptor(moDataRecBizPartner);
            
            try {
                for (STableRow row : moPaneGridPayments.getGridRows()) {
                    SCfdPaymentEntry dummyPaymentEntry = (SCfdPaymentEntry) row;
                    dummyPaymentEntry.DataParentPayment = dummyPayment;
                    dummyPaymentEntry.generateRecordEntries(miClient.getSession()); // very important to process journal voucher entries, to do the accounting!
                    index++;
                }
            }
            catch (Exception e) {
                SLibUtils.printException(this, e);
                
                validation.setMessage(e.getMessage() != null ? e.getMessage() : e.toString());
                validation.setComponent(moPaneGridPayments.getTable());
                moPaneGridPayments.setTableRowSelection(index);
            }
        }
        
        if (!validation.getIsError() && moDataCfdPayment != null && !moDataCfdPayment.getIsRegistryNew()) {
            if (miClient.showMsgBoxConfirm("La contabilización previa del CFDI será descartada y reemplazada de acuerdo a sus datos actuales.\n" + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                validation.setMessage("Asegurarse que los datos actuales del CFDI son correctos.");
                validation.setComponent(jbCancel);
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
        moDataCfdPayment = (SDataCfdPayment) registry;
        SDataCfd cfd = moDataCfdPayment.getDbmsDataCfd();
        
        // set voucher fields:
        
        jtfVouNumberRo.setText(cfd.getCfdNumber());
        jtfVouNumberRo.setCaretPosition(0);
        
        jtfVouUuidRo.setText(cfd.getUuid());
        jtfVouUuidRo.setCaretPosition(0);
        
        jtfVouVersionRo.setText(cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33 ? DCfdConsts.CFDI_VER_33 + "" : 
                cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_40 ? DCfdConsts.CFDI_VER_40 + "" : "?");
        jtfVouVersionRo.setCaretPosition(0);
        
        moFieldVouDate.setFieldValue(moDataCfdPayment.getComprobanteFecha());
        jtfVouDatetimeRo.setText(SLibUtils.DateFormatDatetime.format(moDataCfdPayment.getComprobanteFecha()));
        jtfVouDatetimeRo.setCaretPosition(0);
        
        jtfVouStatusRo.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_ST_DPS, new int[] { cfd.getFkXmlStatusId() }));
        jtfVouStatusRo.setCaretPosition(0);
        
        moDataComBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { moDataCfdPayment.getDbmsDataCfd().getFkCompanyBranchId_n()}, SLibConstants.EXEC_MODE_VERBOSE);
        SFormUtilities.populateComboBox(miClient, jcbPayAccountDest, SDataConstants.FIN_ACC_CASH, new int[] { moDataComBranch.getPkBizPartnerBranchId() });
        renderComBranch();
        
        moFieldVouTaxRegime.setFieldValue(moDataCfdPayment.getEmisorRegimenFiscal());
        
        moFieldVouConfirm.setFieldValue(moDataCfdPayment.getComprobanteConfirmacion());
        
        if (moDataCfdPayment.getAuxCfdDbmsDataReceptor() != null) {
            moFieldRecBizPartner.setFieldValue(new int[] { moDataCfdPayment.getAuxCfdDbmsDataReceptor().getPkBizPartnerId() });    // event item state changed thrown!
            
            moDataRecCfdRelated = moDataCfdPayment.getAuxCfdDbmsDataCfdCfdiRelacionado();
            msRecCfdRelatedUuid = moDataCfdPayment.getAuxCfdCfdiRelacionadoUuid();
            renderRecCfdRelated();
        }
        
        moFieldRecCfdiUse.setFieldValue(moDataCfdPayment.getReceptorUsoCFDI());
        
        actionPerformedVouNext(false);
        
        // add payment entries:
        
        for (SCfdPaymentEntry entry : moDataCfdPayment.getAuxCfdPaymentEntries()) {
            moPaneGridPayments.addTableRow(entry);
        }
        moPaneGridPayments.renderTableRows();
        moPaneGridPayments.setTableRowSelection(0);
        
        computeVouTotal();
        valueChangedPayments();
        
        if (!SDataUtilities.isPeriodOpen(miClient, cfd.getTimestamp()) || cfd.isStamped()) {
            mbIsFormReadOnly = true;
        }
        
        if (mbIsFormReadOnly) {
            jbVouResume.setEnabled(false);
            
            enableVouFields(false);
            enablePayControls(false);
            enableDocControls(false);
            
            jbOk.setEnabled(true);
            jbCancel.requestFocusInWindow();
        }
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        SCfdPacket cfdPacket = new SCfdPacket();
        
        if (moDataCfdPayment == null) {
            moDataCfdPayment = new SDataCfdPayment();
            moDataCfdPayment.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
            
            //cfdPacket.setCfdId(...);
            cfdPacket.setCfdSeries(miClient.getSessionXXX().getParamsCompany().getPaymentNumberSeries());
            //cfdPacket.setCfdNumber(...);
        }
        else {
            moDataCfdPayment.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
            
            cfdPacket.setCfdId(moDataCfdPayment.getDbmsDataCfd().getPkCfdId());
            cfdPacket.setCfdSeries(moDataCfdPayment.getDbmsDataCfd().getSeries());
            cfdPacket.setCfdNumber(moDataCfdPayment.getDbmsDataCfd().getNumber());
        }
        
        // get business partner ID for factoring bank, if any:
        
        int factoringBankId = 0;
        
        if (!moPaneGridPayments.getGridRows().isEmpty()) {
            SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getGridRows().get(0);
            factoringBankId = paymentEntry.AuxFactoringBankId; // there is only a factoring bank, it has been validated already
        }
        
        // create CFD packet:
        
        //cfdPacket.setCfdCertNumber(...);
        //cfdPacket.setCfdStringSigned(...);
        //cfdPacket.setCfdSignature(...);
        //cfdPacket.setDocXmlUuid(...);
        //cfdPacket.setDocXml(...);
        //cfdPacket.setDocXmlName(...);
        cfdPacket.setXmlDate(SCfdUtils.composeDatetime(moFieldVouDate.getDate()));
        //cfdPacket.setXmlRfcEmisor(...);
        //cfdPacket.setXmlRfcReceptor(...);
        //cfdPacket.setXmlTotalCy(...);
        //cfdPacket.setCfdUuid(...);
        //cfdPacket.setAcknowledgmentCancellationXml(...);
        //cfdPacket.setIsCfdConsistent(...);
        cfdPacket.setFkCfdTypeId(SDataConstantsSys.TRNS_TP_CFD_PAY_REC);
        cfdPacket.setFkXmlTypeId(((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdTypeXmlTypes().get(cfdPacket.getFkCfdTypeId()));
        cfdPacket.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_EMITED);
        cfdPacket.setFkXmlDeliveryTypeId(SModSysConsts.TRNS_TP_XML_DVY_NA);
        cfdPacket.setFkXmlDeliveryStatusId(SModSysConsts.TRNS_ST_XML_DVY_PENDING);
        cfdPacket.setFkUserDeliveryId(miClient.getSession().getUser().getPkUserId());
        cfdPacket.setFkCompanyBranchId(moDataComBranch.getPkBizPartnerBranchId());
        cfdPacket.setFkFactoringBankId(factoringBankId);
        //cfdPacket.setDpsYearId(...);
        //cfdPacket.setDpsDocId(...);
        //cfdPacket.setRecordEntryYearId(...);
        //cfdPacket.setRecordEntryPeriodId(...);
        //cfdPacket.setRecordEntryBookkeepingCenterId(...);
        //cfdPacket.setRecordEntryRecordTypeId(...);
        //cfdPacket.setRecordEntryNumberId(...);
        //cfdPacket.setRecordEntryEntryId(...);
        //cfdPacket.setPayrollPayrollId(...);
        //cfdPacket.setPayrollEmployeeId(...);
        //cfdPacket.setPayrollBizPartnerId(...);
        //cfdPacket.setPayrollReceiptPayrollId(...);
        //cfdPacket.setPayrollReceiptEmployeeId(...);
        //cfdPacket.setPayrollReceiptIssueId(...);
        
        if (moDataCfdPayment.getDbmsReceiptPayment() != null) {
            cfdPacket.setReceiptPaymentId(moDataCfdPayment.getDbmsReceiptPayment().getPkReceiptId());
        }
        
        //cfdPacket.setBillOfLadingId(...);
        
        try {
            moDataCfdPayment.setDbmsDataCfd(cfdPacket.createDataCfd());
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        moDataCfdPayment.setAuxDbmsDataEmisor(miClient.getSessionXXX().getCompany().getDbmsDataCompany());
        moDataCfdPayment.setAuxDbmsDataEmisorSucursal(moDataComBranch);
        
        moDataCfdPayment.setAuxCfdConfirmacion(moFieldVouConfirm.getString());
        moDataCfdPayment.setAuxCfdEmisorRegimenFiscal(moFieldVouTaxRegime.getFieldValue().toString());
        moDataCfdPayment.setAuxCfdReceptorRegimenFiscal(jtfRecTaxRegime.getText());
        moDataCfdPayment.setAuxCfdDbmsDataReceptor(moDataRecBizPartner);
        
        SDataBizPartner factoringBank = null;
        if (factoringBankId != 0) {
            factoringBank = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { factoringBankId }, SLibConstants.EXEC_MODE_VERBOSE);
        }
        moDataCfdPayment.setAuxCfdDbmsDataReceptorFactoring(factoringBank);
        
        moDataCfdPayment.setAuxCfdCfdiRelacionadosTipoRelacion(msXmlRelationType);
        if (msXmlRelationType.isEmpty()) {
            moDataCfdPayment.setAuxCfdDbmsDataCfdCfdiRelacionado(null);
        }
        else {
            moDataCfdPayment.setAuxCfdDbmsDataCfdCfdiRelacionado(moDataRecCfdRelated);
            moDataCfdPayment.setAuxCfdCfdiRelacionadoUuid(msRecCfdRelatedUuid);
        }
        
        try {
            if (!moDataCfdPayment.getIsRegistryNew()) {
                // NOTE: This is very seldom to occur, no data updates made in Client, but are necessary due to tax calculations in invoices payments:
                moDataCfdPayment.deleteRecordFin(miClient.getSession().getStatement().getConnection());
            }
            
            // add payment entries:
            moDataCfdPayment.getAuxCfdPaymentEntries().clear();
            for (STableRow row : moPaneGridPayments.getGridRows()) {
                SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) row;
                paymentEntry.DataParentPayment = moDataCfdPayment;
                paymentEntry.generateRecordEntries(miClient.getSession()); // very important to process journal voucher entries, to do the accounting!
                moDataCfdPayment.getAuxCfdPaymentEntries().add(paymentEntry);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        // send as well locks of journal vouchers:
        moDataCfdPayment.getRegistryComplements().clear();
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
        for (SSrvLock lock : moRecordLocksMap.values()) {
            moDataCfdPayment.getRegistryComplements().add(lock);
        }
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        for (SRedisLock rlock : moRecordRedisLocksMap.values()) {
            moDataCfdPayment.getRegistryComplements().add(rlock);
        }
        */
        for (SLock slock : moRecordSLocksMap.values()) {
            moDataCfdPayment.getRegistryComplements().add(slock);
        }
        
        return moDataCfdPayment;
    }
    
    @Override
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case SLibConstants.VALUE_STATUS:
                mbIsFormReadOnly = (Boolean) value;
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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbVouDatePick) {
                actionPerformedVouDatePick();
            }
            else if (button == jbVouNext) {
                actionPerformedVouNext(true);
            }
            else if (button == jbVouResume) {
                actionPerformedVouResume();
            }
            else if (button == jbRecBizPartnerPick) {
                actionPerformedRecBizPartnerPick();
            }
            else if (button == jbRecTaxRegime) {
                actionPerformedRecTaxRegime();
            }
            else if (button == jbRecCfdRelatedAdd) {
                actionPerformedRecCfdRelatedAdd();
            }
            else if (button == jbRecCfdRelatedDelete) {
                actionPerformedRecCfdRelatedDelete();
            }
            else if (button == jbRecCfdRelatedPick) {
                actionPerformedRecCfdRelatedPick();
            }
            else if (button == jbPayDatePick) {
                actionPerformedPayDatePick();
            }
            else if (button == jbPayExchangeRatePick) {
                actionPerformedPayExchangeRatePick();
            }
            else if (button == jbPayRecordPick) {
                actionPerformedPayRecordPick();
            }
            else if (button == jbPayRecordView) {
                actionPerformedPayRecordView();
            }
            else if (button == jbPayAccountSrcPick) {
                actionPerformedPayAccountSrcPick();
            }
            else if (button == jbPayAccountDestPick) {
                actionPerformedPayAccountDestPick();
            }
            else if (button == jbPayPaymentEntryAdd) {
                actionPerformedPayPaymentEntryAdd();
            }
            else if (button == jbPayPaymentEntryModify) {
                actionPerformedPayPaymentEntryModify();
            }
            else if (button == jbPayPaymentEntryDelete) {
                actionPerformedPayPaymentEntryDelete();
            }
            else if (button == jbPayPaymentEntryOk) {
                actionPerformedPayPaymentEntryOk();
            }
            else if (button == jbPayPaymentEntryCancel) {
                actionPerformedPayPaymentEntryCancel();
            }
            else if (button == jbDocDpsRelatedPickPend) {
                actionPerformedDocDpsRelatedPick(MODE_DPS_PICK_PEND);
            }
            else if (button == jbDocDpsRelatedPickAll) {
                actionPerformedDocDpsRelatedPick(MODE_DPS_PICK_ALL);
            }
            else if (button == jbDocExchangeRateInvert) {
                actionPerformedDocExchangeRateInvert();
            }
            else if (button == jbDocPaymentCompute) {
                actionPerformedDocPaymentCompute();
            }
            else if (button == jbDocPaymentEntryDocAdd) {
                actionPerformedDocPaymentEntryDocAdd(true);
            }
            else if (button == jbDocPaymentEntryDocModify) {
                actionPerformedDocPaymentEntryDocModify();
            }
            else if (button == jbDocPaymentEntryDocDelete) {
                actionPerformedDocPaymentEntryDocDelete();
            }
            else if (button == jbDocPaymentEntryDocOk) {
                actionPerformedDocPaymentEntryDocOk();
            }
            else if (button == jbDocPaymentEntryDocCancel) {
                actionPerformedDocPaymentEntryDocCancel();
            }
            else if (button == jbAccConceptDocsEdit) {
                actionPerformedAccConceptDocsEdit();
            }
            else if (button == jbOk) {
                actionPerformedOk();
            }
            else if (button == jbCancel) {
                actionPerformedCancel();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox comboBox = (JComboBox) e.getSource();

                if (comboBox == jcbRecBizPartner) {
                    itemStateChangedRecBizPartner();
                }
                else if (comboBox == jcbPayPaymentWay) {
                    itemStateChangedPayPaymentWay();
                }
                else if (comboBox == jcbPayCurrency) {
                    itemStateChangedPayCurrency();
                }
                else if (comboBox == jcbPayAccountDest) {
                    itemStateChangedPayAccountDest();
                }
            }
        }
        else if (e.getSource() instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            
            if (checkBox == jckPayFactoring) {
                itemStateChangedPayFactoring();
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() instanceof JTextField) {
            JTextField textField = (JTextField) e.getSource();
            
            if (textField == jtfRecCfdRelatedUuid) {
                focusGainedRecCfdRelatedUuid();
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof JTextField) {
            JTextField textField = (JTextField) e.getSource();
            
            if (textField == jtfRecCfdRelatedUuid) {
                focusLostRecCfdRelatedUuid();
            }
            else if (textField == jtfPayAmount || textField == jtfPayExchangeRate) {
                focusLostPayExchangeRate();
            }
            else if (textField == jtfDocExchangeRate) {
                focusLostDocExchangeRate();
            }
            else if (textField == jtfDocDocBalancePrev || textField == jtfDocDocPayment) {
                focusLostDocPayment();
            }
            else if (textField == jtfRecTaxRegime) {
                focusLostRecTaxRegime();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (e.getSource() instanceof DefaultListSelectionModel) {
                valueChangedPayments();
            }
        }
    }
    
    public class DpsRelatedInfo {
        public String Uuid;
        public int LastInstallment;
        public double Payments;
        
        public DpsRelatedInfo(String uuid, int lastInstallment, double payments) {
            Uuid = uuid;
            LastInstallment = lastInstallment;
            Payments = payments;
        }
    }
}
