/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.form;

import cfd.DCfdConsts;
import cfd.ver33.DCfdi33Catalogs;
import erp.cfd.SCfdXmlCatalogs;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
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
import erp.mtrn.data.SCfdPacket;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataCfdPayment;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.STrnUtilities;
import erp.mtrn.data.cfd.SCfdPaymentEntry;
import erp.mtrn.data.cfd.SCfdPaymentEntryDoc;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridConsts;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.srv.SSrvLock;
import sa.lib.srv.SSrvUtils;

/**
 * User form for input of database registry of CFDI of Payments.
 * @author  Sergio Flores
 */
public class SFormCfdPayment extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.ItemListener, java.awt.event.FocusListener, javax.swing.event.ListSelectionListener {
    
    private final int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbIsFirstTime;
    private boolean mbIsFormReady;
    private boolean mbIsFormReadOnly;
    private boolean mbIsFormReseting;
    private boolean mbEditingVoucher;           // payment voucher, that is, the very header of registry
    private boolean mbEditingPaymentEntry;
    private boolean mbEditingPaymentEntryDoc;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private final erp.client.SClientInterface miClient;

    private erp.mtrn.data.SDataCfdPayment moDataCfdPayment;
    private erp.mbps.data.SDataBizPartnerBranch moDataComBranch;
    private erp.mbps.data.SDataBizPartner moDataRecBizPartner;
    private erp.mtrn.data.SDataCfd moDataRecCfdRelated;
    
    private erp.mfin.data.SDataRecord moDataPayRecord;
    private erp.mfin.data.SDataAccountCash moDataPayAccountCash;
    private erp.mtrn.data.SDataDps moDataDocDpsRelated;
    private erp.mtrn.data.cfd.SCfdPaymentEntry moPaymentEntry;
    private erp.mtrn.data.cfd.SCfdPaymentEntryDoc moPaymentEntryDoc;

    private String msXmlRelationType;
    private java.util.HashMap<java.lang.String, sa.lib.srv.SSrvLock> moRecordLocksMap;  // key: record's primary key as string; value: corresponding gained lock
    private erp.mfin.form.SDialogRecordPicker moDialogPayRecordPicker;
    private erp.mtrn.form.SDialogPickerDps moDialogRecDpsRelatedPicker;
    private erp.mtrn.form.SDialogPickerDps moDialogDocDpsRelatedPicker;
    private erp.lib.table.STablePaneGrid moPaneGridPayments;
    private erp.lib.table.STablePaneGrid moPaneGridPaymentDocs;
    
    // CFD:
    private erp.lib.form.SFormField moFieldVouDate;
    private erp.lib.form.SFormField moFieldVouTaxRegime;
    private erp.lib.form.SFormField moFieldVouConfirm;
    
    // business partner:
    private erp.lib.form.SFormField moFieldRecBizPartner;
    private erp.lib.form.SFormField moFieldRecCfdiUse;
    
    // current payment:
    private erp.lib.form.SFormField moFieldPayDate;
    private erp.lib.form.SFormField moFieldPayHour;
    private erp.lib.form.SFormField moFieldPayPaymentWay;
    private erp.lib.form.SFormField moFieldPayCurrency;
    private erp.lib.form.SFormField moFieldPayAmount;
    private erp.lib.form.SFormField moFieldPayExchangeRate;
    private erp.lib.form.SFormField moFieldPayOperation;
    private erp.lib.form.SFormField moFieldPayAccountSrcFiscalId;
    private erp.lib.form.SFormField moFieldPayAccountSrcNumber;
    private erp.lib.form.SFormField moFieldPayAccountSrcEntity;
    private erp.lib.form.SFormField moFieldPayAccountDesFiscalId;
    private erp.lib.form.SFormField moFieldPayAccountDesNumber;
    private erp.lib.form.SFormField moFieldPayAccountDes;
    
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

        jpRegistry = new javax.swing.JPanel();
        jpRegistryHead = new javax.swing.JPanel();
        jpRegistryHeadVou = new javax.swing.JPanel();
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
        jpRegistryHeadRec = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jlRecBizPartner = new javax.swing.JLabel();
        jcbRecBizPartner = new javax.swing.JComboBox();
        jbRecBizPartnerPick = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jlRecFiscalId = new javax.swing.JLabel();
        jtfRecFiscalIdRo = new javax.swing.JTextField();
        jtfRecCountryRo = new javax.swing.JTextField();
        jtfRecForeignFiscalIdRo = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jlRecCfdiUsage = new javax.swing.JLabel();
        jcbRecCfdiUsage = new javax.swing.JComboBox();
        jPanel16 = new javax.swing.JPanel();
        jlRecRelationType = new javax.swing.JLabel();
        jtfRecRelationTypeRo = new javax.swing.JTextField();
        jbRecCfdRelatedAdd = new javax.swing.JButton();
        jbRecCfdRelatedDelete = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jlRecCfdRelated = new javax.swing.JLabel();
        jtfRecCfdRelatedNumberRo = new javax.swing.JTextField();
        jtfRecCfdRelatedUuidRo = new javax.swing.JTextField();
        jtfRecCfdRelatedVersionRo = new javax.swing.JTextField();
        jbRecCfdRelatedPick = new javax.swing.JButton();
        jpRegistryRows = new javax.swing.JPanel();
        jpRegistryRowsPays = new javax.swing.JPanel();
        jpPays = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jlPayDate = new javax.swing.JLabel();
        jftPayDate = new javax.swing.JFormattedTextField();
        jbPayDatePick = new javax.swing.JButton();
        jlPayHour = new javax.swing.JLabel();
        jftPayHour = new javax.swing.JFormattedTextField();
        jlPayHourTip = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jlPayPaymentWay = new javax.swing.JLabel();
        jcbPayPaymentWay = new javax.swing.JComboBox();
        jPanel28 = new javax.swing.JPanel();
        jlPayCurrency = new javax.swing.JLabel();
        jcbPayCurrency = new javax.swing.JComboBox();
        jPanel29 = new javax.swing.JPanel();
        jlPayAmount = new javax.swing.JLabel();
        jtfPayAmount = new javax.swing.JTextField();
        jtfPayAmountCurRo = new javax.swing.JTextField();
        jtfPayExchangeRate = new javax.swing.JTextField();
        jbPayExchangeRatePick = new javax.swing.JButton();
        jtfPayAmountLocalRo = new javax.swing.JTextField();
        jtfPayAmountLocalCurRo = new javax.swing.JTextField();
        jPanel30 = new javax.swing.JPanel();
        jlPayOperation = new javax.swing.JLabel();
        jtfPayOperation = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        jlPayRecord = new javax.swing.JLabel();
        jtfPayRecordDateRo = new javax.swing.JTextField();
        jtfPayRecordBranchCodeRo = new javax.swing.JTextField();
        jtfPayRecordNumberRo = new javax.swing.JTextField();
        jbPayRecordPick = new javax.swing.JButton();
        jbPayRecordView = new javax.swing.JButton();
        jPanel40 = new javax.swing.JPanel();
        jlPayAccount1 = new javax.swing.JLabel();
        jlPayAccountFiscalId = new javax.swing.JLabel();
        jlPayAccountNumber = new javax.swing.JLabel();
        jlPayAccountEntity = new javax.swing.JLabel();
        jlPayAccount2 = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jlPayAccountSrc = new javax.swing.JLabel();
        jtfPayAccountSrcFiscalId = new javax.swing.JTextField();
        jtfPayAccountSrcNumber = new javax.swing.JTextField();
        jtfPayAccountSrcEntity = new javax.swing.JTextField();
        jbPayAccountSrcPick = new javax.swing.JButton();
        jPanel32 = new javax.swing.JPanel();
        jlPayAccountDes = new javax.swing.JLabel();
        jtfPayAccountDesFiscalId = new javax.swing.JTextField();
        jtfPayAccountDesNumber = new javax.swing.JTextField();
        jcbPayAccountDes = new javax.swing.JComboBox<SFormComponentItem>();
        jbPayAccountDesPick = new javax.swing.JButton();
        jPanel33 = new javax.swing.JPanel();
        jbPayPaymentEntryAdd = new javax.swing.JButton();
        jbPayPaymentEntryModify = new javax.swing.JButton();
        jbPayPaymentEntryOk = new javax.swing.JButton();
        jbPayPaymentEntryCancel = new javax.swing.JButton();
        jbPayPaymentEntryDelete = new javax.swing.JButton();
        jpPaysTotals = new javax.swing.JPanel();
        jlVouTotal = new javax.swing.JLabel();
        jtfVouTotalRo = new javax.swing.JTextField();
        jtfVouTotalCurRo = new javax.swing.JTextField();
        jtfVouTotalLocalRo = new javax.swing.JTextField();
        jtfVouTotalLocalCurRo = new javax.swing.JTextField();
        jpRegistryRowsDocs = new javax.swing.JPanel();
        jpDocs = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jlDocDpsRelated = new javax.swing.JLabel();
        jtfDocDpsRelatedNumberRo = new javax.swing.JTextField();
        jtfDocDpsRelatedUuidRo = new javax.swing.JTextField();
        jtfDocDpsRelatedVersionRo = new javax.swing.JTextField();
        jbDocDpsRelatedPick = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jlDocPaymentMethod = new javax.swing.JLabel();
        jtfDocPaymentMethodRo = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        jlDocInstallment = new javax.swing.JLabel();
        jtfDocInstallment = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jlDocCurrency = new javax.swing.JLabel();
        jtfDocCurrencyRo = new javax.swing.JTextField();
        jPanel35 = new javax.swing.JPanel();
        jlDocExchangeRate = new javax.swing.JLabel();
        jtfDocExchangeRate = new javax.swing.JTextField();
        jtfDocExchangeRateCurRo = new javax.swing.JTextField();
        jbDocExchangeRateInvert = new javax.swing.JButton();
        jPanel36 = new javax.swing.JPanel();
        jlDocBalancePrev = new javax.swing.JLabel();
        jtfDocBalancePrev = new javax.swing.JTextField();
        jtfDocBalancePrevCurRo = new javax.swing.JTextField();
        jtfDocBalancePrevPayRo = new javax.swing.JTextField();
        jtfDocBalancePrevPayCurRo = new javax.swing.JTextField();
        jPanel37 = new javax.swing.JPanel();
        jlDocPayment = new javax.swing.JLabel();
        jtfDocPayment = new javax.swing.JTextField();
        jtfDocPaymentCurRo = new javax.swing.JTextField();
        jtfDocPaymentPayRo = new javax.swing.JTextField();
        jtfDocPaymentPayCurRo = new javax.swing.JTextField();
        jbDocPaymentCompute = new javax.swing.JButton();
        jPanel38 = new javax.swing.JPanel();
        jlDocBalancePend = new javax.swing.JLabel();
        jtfDocBalancePend = new javax.swing.JTextField();
        jtfDocBalancePendCurRo = new javax.swing.JTextField();
        jtfDocBalancePendPayRo = new javax.swing.JTextField();
        jtfDocBalancePendPayCurRo = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jbDocPaymentEntryDocAdd = new javax.swing.JButton();
        jbDocPaymentEntryDocModify = new javax.swing.JButton();
        jbDocPaymentEntryDocOk = new javax.swing.JButton();
        jbDocPaymentEntryDocCancel = new javax.swing.JButton();
        jbDocPaymentEntryDocDelete = new javax.swing.JButton();
        jpDocsTotals = new javax.swing.JPanel();
        jlPayTotalPayments = new javax.swing.JLabel();
        jtfPayTotalPaymentsRo = new javax.swing.JTextField();
        jtfPayTotalPaymentsCurRo = new javax.swing.JTextField();
        jtfPayTotalPaymentsLocalRo = new javax.swing.JTextField();
        jtfPayTotalPaymentsLocalCurRo = new javax.swing.JTextField();
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

        jpRegistry.setLayout(new java.awt.BorderLayout(5, 5));

        jpRegistryHead.setLayout(new java.awt.GridLayout(1, 2, 5, 0));

        jpRegistryHeadVou.setBorder(javax.swing.BorderFactory.createTitledBorder("Comprobante:"));
        jpRegistryHeadVou.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVouNumber.setText("Folio:");
        jlVouNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlVouNumber);

        jtfVouNumberRo.setEditable(false);
        jtfVouNumberRo.setText("A-999999");
        jtfVouNumberRo.setToolTipText("Serie y folio");
        jtfVouNumberRo.setFocusable(false);
        jtfVouNumberRo.setPreferredSize(new java.awt.Dimension(103, 23));
        jPanel5.add(jtfVouNumberRo);

        jtfVouUuidRo.setEditable(false);
        jtfVouUuidRo.setText("402A8A38-B980-412A-9485-29222D7095C4");
        jtfVouUuidRo.setToolTipText("UUID");
        jtfVouUuidRo.setFocusable(false);
        jtfVouUuidRo.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel5.add(jtfVouUuidRo);

        jtfVouVersionRo.setEditable(false);
        jtfVouVersionRo.setText("0.0");
        jtfVouVersionRo.setToolTipText("Versión del CFD");
        jtfVouVersionRo.setFocusable(false);
        jtfVouVersionRo.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel5.add(jtfVouVersionRo);

        jpRegistryHeadVou.add(jPanel5);

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

        jpRegistryHeadVou.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVouBranch.setText("Sucursal empresa:");
        jlVouBranch.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlVouBranch);

        jtfVouBranchNameRo.setEditable(false);
        jtfVouBranchNameRo.setText("TEXT");
        jtfVouBranchNameRo.setToolTipText("Nombre de la sucursal");
        jtfVouBranchNameRo.setFocusable(false);
        jtfVouBranchNameRo.setPreferredSize(new java.awt.Dimension(200, 23));
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
        jtfVouPlaceIssueRo.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel7.add(jtfVouPlaceIssueRo);

        jpRegistryHeadVou.add(jPanel7);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVouTaxRegime.setText("Régimen fiscal:*");
        jlVouTaxRegime.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlVouTaxRegime);

        jcbVouTaxRegime.setMaximumRowCount(16);
        jcbVouTaxRegime.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel9.add(jcbVouTaxRegime);

        jpRegistryHeadVou.add(jPanel9);

        jPanel41.setLayout(new java.awt.GridLayout(1, 2));

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVouConfirm.setText("Confirmación:");
        jlVouConfirm.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlVouConfirm);

        jtfVouConfirm.setText("TEXT");
        jtfVouConfirm.setPreferredSize(new java.awt.Dimension(100, 23));
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

        jpRegistryHeadVou.add(jPanel41);

        jpRegistryHead.add(jpRegistryHeadVou);

        jpRegistryHeadRec.setBorder(javax.swing.BorderFactory.createTitledBorder("Receptor:"));
        jpRegistryHeadRec.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRecBizPartner.setText("Receptor:*");
        jlRecBizPartner.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlRecBizPartner);

        jcbRecBizPartner.setMaximumRowCount(16);
        jcbRecBizPartner.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel11.add(jcbRecBizPartner);

        jbRecBizPartnerPick.setText("...");
        jbRecBizPartnerPick.setToolTipText("Seleccionar asociado de negocios");
        jbRecBizPartnerPick.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbRecBizPartnerPick);

        jpRegistryHeadRec.add(jPanel11);

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
        jtfRecForeignFiscalIdRo.setPreferredSize(new java.awt.Dimension(190, 23));
        jPanel12.add(jtfRecForeignFiscalIdRo);

        jpRegistryHeadRec.add(jPanel12);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRecCfdiUsage.setText("Uso CFDI:*");
        jlRecCfdiUsage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel15.add(jlRecCfdiUsage);

        jcbRecCfdiUsage.setMaximumRowCount(16);
        jcbRecCfdiUsage.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel15.add(jcbRecCfdiUsage);

        jpRegistryHeadRec.add(jPanel15);

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

        jpRegistryHeadRec.add(jPanel16);

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

        jtfRecCfdRelatedUuidRo.setEditable(false);
        jtfRecCfdRelatedUuidRo.setText("402A8A38-B980-412A-9485-29222D7095C4");
        jtfRecCfdRelatedUuidRo.setToolTipText("UUID");
        jtfRecCfdRelatedUuidRo.setFocusable(false);
        jtfRecCfdRelatedUuidRo.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel17.add(jtfRecCfdRelatedUuidRo);

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

        jpRegistryHeadRec.add(jPanel17);

        jpRegistryHead.add(jpRegistryHeadRec);

        jpRegistry.add(jpRegistryHead, java.awt.BorderLayout.NORTH);

        jpRegistryRows.setLayout(new java.awt.GridLayout(1, 2, 5, 0));

        jpRegistryRowsPays.setBorder(javax.swing.BorderFactory.createTitledBorder("Pagos:"));
        jpRegistryRowsPays.setLayout(new java.awt.BorderLayout(0, 5));

        jpPays.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel24.setLayout(new java.awt.GridLayout(10, 1, 0, 5));

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

        jlPayHour.setText("Hora pago:*");
        jlPayHour.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel25.add(jlPayHour);

        jftPayHour.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm:ss"))));
        jftPayHour.setText("01:01:01");
        jftPayHour.setToolTipText("Hora");
        jftPayHour.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel25.add(jftPayHour);

        jlPayHourTip.setForeground(java.awt.Color.gray);
        jlPayHourTip.setText("hh:mm:ss");
        jlPayHourTip.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel25.add(jlPayHourTip);

        jPanel24.add(jPanel25);

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayPaymentWay.setText("Forma pago:*");
        jlPayPaymentWay.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel26.add(jlPayPaymentWay);

        jcbPayPaymentWay.setMaximumRowCount(16);
        jcbPayPaymentWay.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel26.add(jcbPayPaymentWay);

        jPanel24.add(jPanel26);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayCurrency.setBackground(new java.awt.Color(204, 255, 204));
        jlPayCurrency.setText("Moneda pago:*");
        jlPayCurrency.setOpaque(true);
        jlPayCurrency.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel28.add(jlPayCurrency);

        jcbPayCurrency.setMaximumRowCount(16);
        jcbPayCurrency.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel28.add(jcbPayCurrency);

        jPanel24.add(jPanel28);

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

        jPanel24.add(jPanel29);

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayOperation.setText("Núm. operación:");
        jlPayOperation.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel30.add(jlPayOperation);

        jtfPayOperation.setText("TEXT");
        jtfPayOperation.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel30.add(jtfPayOperation);

        jPanel24.add(jPanel30);

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

        jPanel24.add(jPanel27);

        jPanel40.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayAccount1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jlPayAccount1.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel40.add(jlPayAccount1);

        jlPayAccountFiscalId.setText("RFC entidad:");
        jlPayAccountFiscalId.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jlPayAccountFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel40.add(jlPayAccountFiscalId);

        jlPayAccountNumber.setText("Número cuenta:");
        jlPayAccountNumber.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jlPayAccountNumber.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel40.add(jlPayAccountNumber);

        jlPayAccountEntity.setText("Banco:");
        jlPayAccountEntity.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jlPayAccountEntity.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel40.add(jlPayAccountEntity);

        jlPayAccount2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jlPayAccount2.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel40.add(jlPayAccount2);

        jPanel24.add(jPanel40);

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayAccountSrc.setText("Cta. ordenante:");
        jlPayAccountSrc.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel31.add(jlPayAccountSrc);

        jtfPayAccountSrcFiscalId.setText("XAXX010101000");
        jtfPayAccountSrcFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel31.add(jtfPayAccountSrcFiscalId);

        jtfPayAccountSrcNumber.setText("072470001837637520");
        jtfPayAccountSrcNumber.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel31.add(jtfPayAccountSrcNumber);

        jtfPayAccountSrcEntity.setText("TEXT");
        jtfPayAccountSrcEntity.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel31.add(jtfPayAccountSrcEntity);

        jbPayAccountSrcPick.setText("...");
        jbPayAccountSrcPick.setToolTipText("Seleccionar cuenta ordenante");
        jbPayAccountSrcPick.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel31.add(jbPayAccountSrcPick);

        jPanel24.add(jPanel31);

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayAccountDes.setText("Cta. beneficiaria:*");
        jlPayAccountDes.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel32.add(jlPayAccountDes);

        jtfPayAccountDesFiscalId.setEditable(false);
        jtfPayAccountDesFiscalId.setText("XAXX010101000");
        jtfPayAccountDesFiscalId.setFocusable(false);
        jtfPayAccountDesFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel32.add(jtfPayAccountDesFiscalId);

        jtfPayAccountDesNumber.setEditable(false);
        jtfPayAccountDesNumber.setText("072470001837637520");
        jtfPayAccountDesNumber.setFocusable(false);
        jtfPayAccountDesNumber.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel32.add(jtfPayAccountDesNumber);

        jcbPayAccountDes.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel32.add(jcbPayAccountDes);

        jbPayAccountDesPick.setText("...");
        jbPayAccountDesPick.setToolTipText("Seleccionar cuenta beneficiaria");
        jbPayAccountDesPick.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel32.add(jbPayAccountDesPick);

        jPanel24.add(jPanel32);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbPayPaymentEntryAdd.setText("Agregar");
        jbPayPaymentEntryAdd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel33.add(jbPayPaymentEntryAdd);

        jbPayPaymentEntryModify.setText("Modificar");
        jPanel33.add(jbPayPaymentEntryModify);

        jbPayPaymentEntryOk.setText("Aceptar"); // NOI18N
        jbPayPaymentEntryOk.setToolTipText("[Ctrl + Enter]");
        jbPayPaymentEntryOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel33.add(jbPayPaymentEntryOk);

        jbPayPaymentEntryCancel.setText("Cancelar"); // NOI18N
        jbPayPaymentEntryCancel.setToolTipText("[Escape]");
        jPanel33.add(jbPayPaymentEntryCancel);

        jbPayPaymentEntryDelete.setText("Eliminar");
        jbPayPaymentEntryDelete.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel33.add(jbPayPaymentEntryDelete);

        jPanel24.add(jPanel33);

        jpPays.add(jPanel24, java.awt.BorderLayout.NORTH);

        jpRegistryRowsPays.add(jpPays, java.awt.BorderLayout.CENTER);

        jpPaysTotals.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVouTotal.setText("Total comprobante:");
        jlVouTotal.setPreferredSize(new java.awt.Dimension(100, 23));
        jpPaysTotals.add(jlVouTotal);

        jtfVouTotalRo.setEditable(false);
        jtfVouTotalRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfVouTotalRo.setText("999,999,999.99");
        jtfVouTotalRo.setFocusable(false);
        jtfVouTotalRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jpPaysTotals.add(jtfVouTotalRo);

        jtfVouTotalCurRo.setEditable(false);
        jtfVouTotalCurRo.setText("XXX");
        jtfVouTotalCurRo.setFocusable(false);
        jtfVouTotalCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jpPaysTotals.add(jtfVouTotalCurRo);

        jtfVouTotalLocalRo.setEditable(false);
        jtfVouTotalLocalRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfVouTotalLocalRo.setText("999,999,999.99");
        jtfVouTotalLocalRo.setFocusable(false);
        jtfVouTotalLocalRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jpPaysTotals.add(jtfVouTotalLocalRo);

        jtfVouTotalLocalCurRo.setEditable(false);
        jtfVouTotalLocalCurRo.setBackground(new java.awt.Color(255, 204, 204));
        jtfVouTotalLocalCurRo.setText("LOC");
        jtfVouTotalLocalCurRo.setFocusable(false);
        jtfVouTotalLocalCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jpPaysTotals.add(jtfVouTotalLocalCurRo);

        jpRegistryRowsPays.add(jpPaysTotals, java.awt.BorderLayout.SOUTH);

        jpRegistryRows.add(jpRegistryRowsPays);

        jpRegistryRowsDocs.setBorder(javax.swing.BorderFactory.createTitledBorder("Documentos:"));
        jpRegistryRowsDocs.setLayout(new java.awt.BorderLayout(0, 5));

        jpDocs.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel8.setLayout(new java.awt.GridLayout(10, 1, 0, 5));

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocDpsRelated.setText("Doc. relacionado:*");
        jlDocDpsRelated.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlDocDpsRelated);

        jtfDocDpsRelatedNumberRo.setEditable(false);
        jtfDocDpsRelatedNumberRo.setText("A-999999");
        jtfDocDpsRelatedNumberRo.setToolTipText("Serie y folio");
        jtfDocDpsRelatedNumberRo.setFocusable(false);
        jtfDocDpsRelatedNumberRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jtfDocDpsRelatedNumberRo);

        jtfDocDpsRelatedUuidRo.setEditable(false);
        jtfDocDpsRelatedUuidRo.setText("402A8A38-B980-412A-9485-29222D7095C4");
        jtfDocDpsRelatedUuidRo.setToolTipText("UUID");
        jtfDocDpsRelatedUuidRo.setFocusable(false);
        jtfDocDpsRelatedUuidRo.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel14.add(jtfDocDpsRelatedUuidRo);

        jtfDocDpsRelatedVersionRo.setEditable(false);
        jtfDocDpsRelatedVersionRo.setText("0.0");
        jtfDocDpsRelatedVersionRo.setToolTipText("Versión del CFD");
        jtfDocDpsRelatedVersionRo.setFocusable(false);
        jtfDocDpsRelatedVersionRo.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel14.add(jtfDocDpsRelatedVersionRo);

        jbDocDpsRelatedPick.setText("...");
        jbDocDpsRelatedPick.setToolTipText("Seleccionar doc. relacionado");
        jbDocDpsRelatedPick.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel14.add(jbDocDpsRelatedPick);

        jPanel8.add(jPanel14);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocPaymentMethod.setText("Método pago:");
        jlDocPaymentMethod.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel21.add(jlDocPaymentMethod);

        jtfDocPaymentMethodRo.setEditable(false);
        jtfDocPaymentMethodRo.setText("TEXT");
        jtfDocPaymentMethodRo.setFocusable(false);
        jtfDocPaymentMethodRo.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel21.add(jtfDocPaymentMethodRo);

        jPanel8.add(jPanel21);

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocInstallment.setText("Parcialidad:*");
        jlDocInstallment.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel34.add(jlDocInstallment);

        jtfDocInstallment.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocInstallment.setText("0");
        jtfDocInstallment.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel34.add(jtfDocInstallment);

        jPanel8.add(jPanel34);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocCurrency.setBackground(new java.awt.Color(255, 255, 204));
        jlDocCurrency.setText("Moneda doc.:");
        jlDocCurrency.setOpaque(true);
        jlDocCurrency.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel22.add(jlDocCurrency);

        jtfDocCurrencyRo.setEditable(false);
        jtfDocCurrencyRo.setText("TEXT");
        jtfDocCurrencyRo.setFocusable(false);
        jtfDocCurrencyRo.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel22.add(jtfDocCurrencyRo);

        jPanel8.add(jPanel22);

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
        jtfDocExchangeRateCurRo.setToolTipText("Sucursal de la empresa");
        jtfDocExchangeRateCurRo.setFocusable(false);
        jtfDocExchangeRateCurRo.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel35.add(jtfDocExchangeRateCurRo);

        jbDocExchangeRateInvert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_exc_rate.gif"))); // NOI18N
        jbDocExchangeRateInvert.setToolTipText("Invertir tipo de cambio (1/x)");
        jbDocExchangeRateInvert.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel35.add(jbDocExchangeRateInvert);

        jPanel8.add(jPanel35);

        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocBalancePrev.setText("Saldo anterior:*");
        jlDocBalancePrev.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel36.add(jlDocBalancePrev);

        jtfDocBalancePrev.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocBalancePrev.setText("999,999,999.99");
        jtfDocBalancePrev.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel36.add(jtfDocBalancePrev);

        jtfDocBalancePrevCurRo.setEditable(false);
        jtfDocBalancePrevCurRo.setBackground(new java.awt.Color(255, 255, 204));
        jtfDocBalancePrevCurRo.setText("DOC");
        jtfDocBalancePrevCurRo.setFocusable(false);
        jtfDocBalancePrevCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel36.add(jtfDocBalancePrevCurRo);

        jtfDocBalancePrevPayRo.setEditable(false);
        jtfDocBalancePrevPayRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocBalancePrevPayRo.setText("999,999,999.99");
        jtfDocBalancePrevPayRo.setFocusable(false);
        jtfDocBalancePrevPayRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel36.add(jtfDocBalancePrevPayRo);

        jtfDocBalancePrevPayCurRo.setEditable(false);
        jtfDocBalancePrevPayCurRo.setBackground(new java.awt.Color(204, 255, 204));
        jtfDocBalancePrevPayCurRo.setText("PAY");
        jtfDocBalancePrevPayCurRo.setFocusable(false);
        jtfDocBalancePrevPayCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel36.add(jtfDocBalancePrevPayCurRo);

        jPanel8.add(jPanel36);

        jPanel37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocPayment.setText("Importe pagado:*");
        jlDocPayment.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel37.add(jlDocPayment);

        jtfDocPayment.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocPayment.setText("999,999,999.99");
        jtfDocPayment.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel37.add(jtfDocPayment);

        jtfDocPaymentCurRo.setEditable(false);
        jtfDocPaymentCurRo.setBackground(new java.awt.Color(255, 255, 204));
        jtfDocPaymentCurRo.setText("DOC");
        jtfDocPaymentCurRo.setFocusable(false);
        jtfDocPaymentCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel37.add(jtfDocPaymentCurRo);

        jtfDocPaymentPayRo.setEditable(false);
        jtfDocPaymentPayRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocPaymentPayRo.setText("999,999,999.99");
        jtfDocPaymentPayRo.setFocusable(false);
        jtfDocPaymentPayRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel37.add(jtfDocPaymentPayRo);

        jtfDocPaymentPayCurRo.setEditable(false);
        jtfDocPaymentPayCurRo.setBackground(new java.awt.Color(204, 255, 204));
        jtfDocPaymentPayCurRo.setText("PAY");
        jtfDocPaymentPayCurRo.setFocusable(false);
        jtfDocPaymentPayCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel37.add(jtfDocPaymentPayCurRo);

        jbDocPaymentCompute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_wizard.gif"))); // NOI18N
        jbDocPaymentCompute.setToolTipText("Asignar importe disponible");
        jbDocPaymentCompute.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel37.add(jbDocPaymentCompute);

        jPanel8.add(jPanel37);

        jPanel38.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocBalancePend.setText("Saldo insoluto:");
        jlDocBalancePend.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel38.add(jlDocBalancePend);

        jtfDocBalancePend.setEditable(false);
        jtfDocBalancePend.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocBalancePend.setText("999,999,999.99");
        jtfDocBalancePend.setFocusable(false);
        jtfDocBalancePend.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel38.add(jtfDocBalancePend);

        jtfDocBalancePendCurRo.setEditable(false);
        jtfDocBalancePendCurRo.setBackground(new java.awt.Color(255, 255, 204));
        jtfDocBalancePendCurRo.setText("DOC");
        jtfDocBalancePendCurRo.setFocusable(false);
        jtfDocBalancePendCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel38.add(jtfDocBalancePendCurRo);

        jtfDocBalancePendPayRo.setEditable(false);
        jtfDocBalancePendPayRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocBalancePendPayRo.setText("999,999,999.99");
        jtfDocBalancePendPayRo.setFocusable(false);
        jtfDocBalancePendPayRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel38.add(jtfDocBalancePendPayRo);

        jtfDocBalancePendPayCurRo.setEditable(false);
        jtfDocBalancePendPayCurRo.setBackground(new java.awt.Color(204, 255, 204));
        jtfDocBalancePendPayCurRo.setText("PAY");
        jtfDocBalancePendPayCurRo.setFocusable(false);
        jtfDocBalancePendPayCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel38.add(jtfDocBalancePendPayCurRo);

        jPanel8.add(jPanel38);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel8.add(jPanel13);

        jPanel39.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbDocPaymentEntryDocAdd.setText("Agregar");
        jbDocPaymentEntryDocAdd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel39.add(jbDocPaymentEntryDocAdd);

        jbDocPaymentEntryDocModify.setText("Modificar");
        jPanel39.add(jbDocPaymentEntryDocModify);

        jbDocPaymentEntryDocOk.setText("Aceptar"); // NOI18N
        jbDocPaymentEntryDocOk.setToolTipText("[Ctrl + Enter]");
        jbDocPaymentEntryDocOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel39.add(jbDocPaymentEntryDocOk);

        jbDocPaymentEntryDocCancel.setText("Cancelar"); // NOI18N
        jbDocPaymentEntryDocCancel.setToolTipText("[Escape]");
        jPanel39.add(jbDocPaymentEntryDocCancel);

        jbDocPaymentEntryDocDelete.setText("Eliminar");
        jbDocPaymentEntryDocDelete.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel39.add(jbDocPaymentEntryDocDelete);

        jPanel8.add(jPanel39);

        jpDocs.add(jPanel8, java.awt.BorderLayout.NORTH);

        jpRegistryRowsDocs.add(jpDocs, java.awt.BorderLayout.CENTER);

        jpDocsTotals.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayTotalPayments.setText("Total pagos:");
        jlPayTotalPayments.setPreferredSize(new java.awt.Dimension(100, 23));
        jpDocsTotals.add(jlPayTotalPayments);

        jtfPayTotalPaymentsRo.setEditable(false);
        jtfPayTotalPaymentsRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayTotalPaymentsRo.setText("999,999,999.99");
        jtfPayTotalPaymentsRo.setFocusable(false);
        jtfPayTotalPaymentsRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jpDocsTotals.add(jtfPayTotalPaymentsRo);

        jtfPayTotalPaymentsCurRo.setEditable(false);
        jtfPayTotalPaymentsCurRo.setBackground(new java.awt.Color(204, 255, 204));
        jtfPayTotalPaymentsCurRo.setText("PAY");
        jtfPayTotalPaymentsCurRo.setFocusable(false);
        jtfPayTotalPaymentsCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jpDocsTotals.add(jtfPayTotalPaymentsCurRo);

        jtfPayTotalPaymentsLocalRo.setEditable(false);
        jtfPayTotalPaymentsLocalRo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayTotalPaymentsLocalRo.setText("999,999,999.99");
        jtfPayTotalPaymentsLocalRo.setFocusable(false);
        jtfPayTotalPaymentsLocalRo.setPreferredSize(new java.awt.Dimension(100, 23));
        jpDocsTotals.add(jtfPayTotalPaymentsLocalRo);

        jtfPayTotalPaymentsLocalCurRo.setEditable(false);
        jtfPayTotalPaymentsLocalCurRo.setBackground(new java.awt.Color(255, 204, 204));
        jtfPayTotalPaymentsLocalCurRo.setText("LOC");
        jtfPayTotalPaymentsLocalCurRo.setFocusable(false);
        jtfPayTotalPaymentsLocalCurRo.setPreferredSize(new java.awt.Dimension(30, 23));
        jpDocsTotals.add(jtfPayTotalPaymentsLocalCurRo);

        jpRegistryRowsDocs.add(jpDocsTotals, java.awt.BorderLayout.SOUTH);

        jpRegistryRows.add(jpRegistryRowsDocs);

        jpRegistry.add(jpRegistryRows, java.awt.BorderLayout.CENTER);

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

        setSize(new java.awt.Dimension(1056, 689));
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
        moFieldPayDate = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftPayDate, jlPayDate);
        moFieldPayDate.setPickerButton(jbPayDatePick);
        moFieldPayHour = new SFormField(miClient, SLibConstants.DATA_TYPE_TIME, true, jftPayHour, jlPayHour);
        moFieldPayPaymentWay = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPayPaymentWay, jlPayPaymentWay);
        moFieldPayCurrency = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPayCurrency, jlPayCurrency);
        moFieldPayAmount = new SFormField(miClient, SLibConstants.DATA_TYPE_FLOAT, true, jtfPayAmount, jlPayAmount);
        moFieldPayAmount.setDecimalFormat(SLibUtils.getDecimalFormatAmount());
        moFieldPayExchangeRate = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfPayExchangeRate, new JLabel(jtfPayExchangeRate.getToolTipText()));
        moFieldPayExchangeRate.setDecimalFormat(SLibUtils.getDecimalFormatExchangeRate());
        moFieldPayExchangeRate.setPickerButton(jbPayExchangeRatePick);
        moFieldPayOperation = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfPayOperation, jlPayOperation);
        moFieldPayOperation.setLengthMax(100);
        moFieldPayAccountSrcFiscalId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfPayAccountSrcFiscalId, jlPayAccountFiscalId);
        moFieldPayAccountSrcFiscalId.setLengthMin(12);
        moFieldPayAccountSrcFiscalId.setLengthMax(13);
        moFieldPayAccountSrcFiscalId.setPickerButton(jbPayAccountSrcPick);
        moFieldPayAccountSrcNumber = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfPayAccountSrcNumber, jlPayAccountNumber);
        moFieldPayAccountSrcNumber.setLengthMin(10);
        moFieldPayAccountSrcNumber.setLengthMax(50);
        moFieldPayAccountSrcNumber.setPickerButton(jbPayAccountSrcPick);
        moFieldPayAccountSrcEntity = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfPayAccountSrcEntity, jlPayAccountEntity);
        moFieldPayAccountSrcEntity.setLengthMax(300);
        moFieldPayAccountDesFiscalId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfPayAccountDesFiscalId, jlPayAccountFiscalId);
        moFieldPayAccountDesFiscalId.setLengthMin(12);
        moFieldPayAccountDesFiscalId.setLengthMax(12);
        moFieldPayAccountDesFiscalId.setPickerButton(jbPayAccountDesPick);
        moFieldPayAccountDesNumber = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfPayAccountDesNumber, jlPayAccountNumber);
        moFieldPayAccountDesNumber.setLengthMin(10);
        moFieldPayAccountDesNumber.setLengthMax(50);
        moFieldPayAccountDesNumber.setPickerButton(jbPayAccountDesPick);
        moFieldPayAccountDes = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPayAccountDes, jlPayAccountDes);
        moFieldPayAccountDes.setPickerButton(jbPayAccountDesPick);
        moFieldDocInstallment = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfDocInstallment, jlDocInstallment);
        moFieldDocExchangeRate = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfDocExchangeRate, jlDocExchangeRate);
        moFieldDocExchangeRate.setDecimalFormat(SLibUtils.getDecimalFormatExchangeRate());
        moFieldDocBalancePrev = new SFormField(miClient, SLibConstants.DATA_TYPE_FLOAT, true, jtfDocBalancePrev, jlDocBalancePrev);
        moFieldDocBalancePrev.setDecimalFormat(SLibUtils.getDecimalFormatAmount());
        moFieldDocPayment = new SFormField(miClient, SLibConstants.DATA_TYPE_FLOAT, true, jtfDocPayment, jlDocPayment);
        moFieldDocPayment.setDecimalFormat(SLibUtils.getDecimalFormatAmount());

        mvFields = new Vector<>();
        mvFields.add(moFieldVouDate);
        mvFields.add(moFieldVouTaxRegime);
        mvFields.add(moFieldVouConfirm);
        mvFields.add(moFieldRecBizPartner);
        mvFields.add(moFieldRecCfdiUse);
        
        // initialize inner forms:
        
        moDialogPayRecordPicker = new SDialogRecordPicker(miClient, SDataConstants.FINX_REC_USER);
        moDialogRecDpsRelatedPicker = new SDialogPickerDps(miClient, SDataConstants.TRN_CFD);
        moDialogDocDpsRelatedPicker = new SDialogPickerDps(miClient, SDataConstants.TRNX_DPS_PAY_PEND);
        
        // initialize payments grid:

        moPaneGridPayments = new STablePaneGrid(miClient);
        moPaneGridPayments.setDoubleClickAction(this, "actionPerformedPayPaymentEntryModify");
        jpPays.add(moPaneGridPayments, BorderLayout.CENTER);

        int colPayment = 0;
        STableColumnForm[] colsPayments = new STableColumnForm[13];
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "# pago", STableConstants.WIDTH_NUM_TINYINT);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Fecha-hora pago", STableConstants.WIDTH_DATE_TIME);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Forma pago", 50);
        colsPayments[colPayment] = new STableColumnForm(SLibConstants.DATA_TYPE_FLOAT, "Monto pago $", STableConstants.WIDTH_VALUE);
        colsPayments[colPayment++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Moneda pago", STableConstants.WIDTH_CURRENCY_KEY);
        colsPayments[colPayment] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Tipo cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        colsPayments[colPayment++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Póliza contable", 100);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Núm. operación", 100);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "RFC emisor ordenante", 100);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Cuenta ordenante", 125);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Banco ordenante", 100);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "RFC emisor beneficiaria", 100);
        colsPayments[colPayment++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Cuenta beneficiaria", 125);

        for (colPayment = 0; colPayment < colsPayments.length; colPayment++) {
            moPaneGridPayments.addTableColumn(colsPayments[colPayment]);
        }
        
        // initialize documents grid:

        moPaneGridPaymentDocs = new STablePaneGrid(miClient);
        moPaneGridPaymentDocs.setDoubleClickAction(this, "actionPerformedDocPaymentEntryDocModify");
        jpDocs.add(moPaneGridPaymentDocs, BorderLayout.CENTER);

        int colPaymentDoc = 0;
        STableColumnForm[] colsPaymentDocs = new STableColumnForm[11];
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
        jbRecCfdRelatedAdd.addActionListener(this);
        jbRecCfdRelatedDelete.addActionListener(this);
        jbRecCfdRelatedPick.addActionListener(this);
        jbPayDatePick.addActionListener(this);
        jbPayRecordPick.addActionListener(this);
        jbPayRecordView.addActionListener(this);
        jbPayExchangeRatePick.addActionListener(this);
        jbPayAccountSrcPick.addActionListener(this);
        jbPayAccountDesPick.addActionListener(this);
        jbPayPaymentEntryAdd.addActionListener(this);
        jbPayPaymentEntryModify.addActionListener(this);
        jbPayPaymentEntryOk.addActionListener(this);
        jbPayPaymentEntryCancel.addActionListener(this);
        jbPayPaymentEntryDelete.addActionListener(this);
        jbDocDpsRelatedPick.addActionListener(this);
        jbDocExchangeRateInvert.addActionListener(this);
        jbDocPaymentCompute.addActionListener(this);
        jbDocPaymentEntryDocAdd.addActionListener(this);
        jbDocPaymentEntryDocModify.addActionListener(this);
        jbDocPaymentEntryDocOk.addActionListener(this);
        jbDocPaymentEntryDocCancel.addActionListener(this);
        jbDocPaymentEntryDocDelete.addActionListener(this);
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        
        jcbPayPaymentWay.addItemListener(this);
        jcbPayCurrency.addItemListener(this);
        jcbPayAccountDes.addItemListener(this);
        jcbRecBizPartner.addItemListener(this);
        
        jtfPayAmount.addFocusListener(this);
        jtfPayExchangeRate.addFocusListener(this);
        jtfDocExchangeRate.addFocusListener(this);
        jtfDocBalancePrev.addFocusListener(this);
        jtfDocPayment.addFocusListener(this);
        
        jtfPayAmountLocalCurRo.setText(miClient.getSession().getSessionCustom().getLocalCurrencyCode());
        jtfVouTotalLocalCurRo.setText(miClient.getSession().getSessionCustom().getLocalCurrencyCode());
        jtfPayTotalPaymentsLocalCurRo.setText(miClient.getSession().getSessionCustom().getLocalCurrencyCode());
        
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
                if (jftVouDate.isEditable()) {
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
        
        SSrvLock lock = moRecordLocksMap.get(record.getRecordPrimaryKey()); // record's primary key as string used as map's key
        
        if (lock == null) {
            lock = SSrvUtils.gainLock(miClient.getSession(), miClient.getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.FIN_REC, record.getPrimaryKey(), record.getRegistryTimeout());
            
            if (lock != null) {
                moRecordLocksMap.put(record.getRecordPrimaryKey(), lock);
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
            
            SSrvLock lock = moRecordLocksMap.get(record.getRecordPrimaryKey());
            
            if (lock != null) {
                SSrvUtils.releaseLock(miClient.getSession(), lock);
                moRecordLocksMap.remove(record.getRecordPrimaryKey());
            }
        }
    }

    /**
     * Releases and clears all existing record locks.
     * @throws Exception 
     */
    private void releaseAllRecordLocks() throws Exception {
        String exception = "";
        ArrayList<SSrvLock> locks = new ArrayList<>(moRecordLocksMap.values());
        
        for (int index = 0; index < locks.size(); index++) {
            try {
                SSrvUtils.releaseLock(miClient.getSession(), locks.get(index));
            }
            catch (Exception e) {
                exception += (exception.isEmpty() ? "" : "\n") + e;
            }
        }
        
        moRecordLocksMap.clear();
        
        if (!exception.isEmpty()) {
            throw new Exception(exception);
        }
    }

    /**
     * Reads and locks desired record.
     * @param primaryKey Primary key of desired record.
     * @return 
     */
    private void setPayRecord(java.lang.Object primaryKey) throws Exception {
        if (moDataPayRecord != null) {
            releaseRecordLock(moDataPayRecord);
            moDataPayRecord = null;
        }
        
        if (!SDataUtilities.isPeriodOpen(miClient, moFieldVouDate.getDate())) {
            throw new Exception(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
        }
        else {
            SDataRecord record = (SDataRecord) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_REC, primaryKey, SLibConstants.EXEC_MODE_VERBOSE);
            
            if (record.getIsSystem()) {
                throw new Exception("No puede seleccionarse esta póliza contable porque es de sistema.");
            }
            else if (record.getIsAudited()) {
                throw new Exception("No puede seleccionarse esta póliza contable porque ya está auditada.");
            }
            else if (record.getIsAuthorized()) {
                throw new Exception("No puede seleccionarse esta póliza contable porque ya está autorizada.");
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
            SFormUtilities.populateComboBox(miClient, jcbPayAccountDes, SDataConstants.FIN_ACC_CASH, new int[] { moDataComBranch.getPkBizPartnerBranchId() });
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
        }
    }

    private void renderPayPaymentEntry() {
        if (moPaymentEntry == null) {
            moFieldPayDate.resetField();
            moFieldPayHour.resetField();
            moFieldPayPaymentWay.resetField();
            itemStateChangedPayPaymentWay();
            moDataPayRecord = null;
            renderPayRecord();
            moFieldPayCurrency.resetField();
            itemStateChangedPayCurrency();
            moFieldPayAmount.resetField();
            moFieldPayExchangeRate.resetField();
            jtfPayAmountLocalRo.setText(SLibUtils.getDecimalFormatAmount().format(0));
            moFieldPayOperation.resetField();
            moFieldPayAccountSrcFiscalId.resetField();
            moFieldPayAccountSrcNumber.resetField();
            moFieldPayAccountSrcEntity.resetField();
            moFieldPayAccountDesFiscalId.resetField();
            moFieldPayAccountDesNumber.resetField();
            moFieldPayAccountDes.resetField();
        }
        else {
            moFieldPayDate.setFieldValue(moPaymentEntry.Date);
            moFieldPayHour.setFieldValue(moPaymentEntry.Date);
            moFieldPayPaymentWay.setFieldValue(moPaymentEntry.PaymentWay);
            itemStateChangedPayPaymentWay();
            moDataPayRecord = moPaymentEntry.DataRecord;
            renderPayRecord();
            moFieldPayCurrency.setFieldValue(new int[] { moPaymentEntry.CurrencyId });
            itemStateChangedPayCurrency();
            moFieldPayAmount.setFieldValue(moPaymentEntry.Amount);
            moFieldPayExchangeRate.setFieldValue(moPaymentEntry.ExchangeRate);
            computePayAmountLocal();
            moFieldPayOperation.setFieldValue(moPaymentEntry.Operation);
            moFieldPayAccountSrcFiscalId.setFieldValue(moPaymentEntry.AccountSrcFiscalId);
            moFieldPayAccountSrcNumber.setFieldValue(moPaymentEntry.AccountSrcNumber);
            moFieldPayAccountSrcEntity.setFieldValue(moPaymentEntry.AccountSrcEntity);
            moFieldPayAccountDesFiscalId.setFieldValue(moPaymentEntry.AccountDesFiscalId);
            moFieldPayAccountDesNumber.setFieldValue(moPaymentEntry.AccountDesNumber);
            moFieldPayAccountDes.setFieldValue(moPaymentEntry.AccountDesKey);
        }
    }

    private void renderRecCfdRelated() {
        if (moDataRecCfdRelated == null) {
            jtfRecCfdRelatedNumberRo.setText("");
            jtfRecCfdRelatedUuidRo.setText("");
            jtfRecCfdRelatedVersionRo.setText("");
            
            msXmlRelationType = "";
            jtfRecRelationTypeRo.setText("");
        }
        else {
            jtfRecCfdRelatedNumberRo.setText(moDataRecCfdRelated.getCfdNumber());
            jtfRecCfdRelatedUuidRo.setText(moDataRecCfdRelated.getUuid());
            jtfRecCfdRelatedVersionRo.setText("" + SCfdUtils.getCfdVersion(moDataRecCfdRelated.getFkXmlTypeId()));
            
            msXmlRelationType = DCfdi33Catalogs.REL_TP_SUSTITUCION; // fixed value in CFDI 3.3!
            jtfRecRelationTypeRo.setText(((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs().composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_REL_TP, msXmlRelationType));
            
            jtfRecCfdRelatedNumberRo.setCaretPosition(0);
            jtfRecCfdRelatedUuidRo.setCaretPosition(0);
            jtfRecCfdRelatedVersionRo.setCaretPosition(0);
            
            jtfRecRelationTypeRo.setCaretPosition(0);
        }
    }

    private void renderDocDpsRelated() {
        if (moDataDocDpsRelated == null) {
            jtfDocDpsRelatedNumberRo.setText("");
            jtfDocDpsRelatedUuidRo.setText("");
            jtfDocDpsRelatedVersionRo.setText("");
            jtfDocPaymentMethodRo.setText("");
            jtfDocCurrencyRo.setText("");
            
            jtfDocBalancePrevCurRo.setText("");
            jtfDocPaymentCurRo.setText("");
            jtfDocBalancePendCurRo.setText("");
            
            jtfDocExchangeRateCurRo.setText("");
        }
        else {
            jtfDocDpsRelatedNumberRo.setText(moDataDocDpsRelated.getDpsNumber());
            jtfDocDpsRelatedUuidRo.setText(moDataDocDpsRelated.getDbmsDataCfd().getUuid());
            jtfDocDpsRelatedVersionRo.setText("" + SCfdUtils.getCfdVersion(moDataDocDpsRelated.getDbmsDataCfd().getFkXmlTypeId()));
            jtfDocPaymentMethodRo.setText(moDataDocDpsRelated.getDbmsDataDpsCfd().getPaymentMethod());
            jtfDocCurrencyRo.setText(moDataDocDpsRelated.getDbmsCurrency());
            
            jtfDocDpsRelatedNumberRo.setCaretPosition(0);
            jtfDocDpsRelatedUuidRo.setCaretPosition(0);
            jtfDocDpsRelatedVersionRo.setCaretPosition(0);
            jtfDocPaymentMethodRo.setCaretPosition(0);
            jtfDocCurrencyRo.setCaretPosition(0);
            
            jtfDocBalancePrevCurRo.setText(moDataDocDpsRelated.getDbmsCurrencyKey());
            jtfDocPaymentCurRo.setText(moDataDocDpsRelated.getDbmsCurrencyKey());
            jtfDocBalancePendCurRo.setText(moDataDocDpsRelated.getDbmsCurrencyKey());
            
            jtfDocExchangeRateCurRo.setText(moDataDocDpsRelated.getDbmsCurrencyKey() + "/" + ((SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow()).CurrencyKey);
        }
    }
    
    private void renderDocPaymentEntryDoc() {
        if (moPaymentEntryDoc == null) {
            moDataDocDpsRelated = null;
            
            moFieldDocInstallment.resetField();
            moFieldDocExchangeRate.resetField();
            moFieldDocBalancePrev.resetField();
            moFieldDocPayment.resetField();
            jtfDocBalancePend.setText(SLibUtils.getDecimalFormatAmount().format(0));
            
            jtfDocBalancePrevPayRo.setText(SLibUtils.getDecimalFormatAmount().format(0));
            jtfDocPaymentPayRo.setText(SLibUtils.getDecimalFormatAmount().format(0));
            jtfDocBalancePendPayRo.setText(SLibUtils.getDecimalFormatAmount().format(0));
        }
        else {
            moDataDocDpsRelated = moPaymentEntryDoc.DataDps;
            
            moFieldDocInstallment.setFieldValue(moPaymentEntryDoc.Installment);
            moFieldDocExchangeRate.setFieldValue(moPaymentEntryDoc.ExchangeRate);
            moFieldDocBalancePrev.setFieldValue(moPaymentEntryDoc.BalancePrev);
            moFieldDocPayment.setFieldValue(moPaymentEntryDoc.Payment);
            jtfDocBalancePend.setText(SLibUtils.getDecimalFormatAmount().format(moPaymentEntryDoc.BalancePend));
            
            jtfDocBalancePrevPayRo.setText(SLibUtils.getDecimalFormatAmount().format(moPaymentEntryDoc.BalancePrevPay));
            jtfDocPaymentPayRo.setText(SLibUtils.getDecimalFormatAmount().format(moPaymentEntryDoc.PaymentPay));
            jtfDocBalancePendPayRo.setText(SLibUtils.getDecimalFormatAmount().format(moPaymentEntryDoc.BalancePendPay));
        }
        
        String docCurrencyKey = moPaymentEntryDoc == null ? "" : moPaymentEntryDoc.DataDps.getDbmsCurrencyKey();
        String payCurrencyKey = moPaneGridPayments.getSelectedTableRow() == null ? "" : ((SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow()).CurrencyKey;
        
        jtfDocBalancePrevPayCurRo.setText(payCurrencyKey);
        jtfDocPaymentPayCurRo.setText(payCurrencyKey);
        jtfDocBalancePendPayCurRo.setText(payCurrencyKey);
        jtfPayTotalPaymentsCurRo.setText(payCurrencyKey);
        
        jtfDocExchangeRateCurRo.setText(docCurrencyKey.isEmpty() || payCurrencyKey.isEmpty() ? "" : docCurrencyKey + "/" + payCurrencyKey);
        
        renderDocDpsRelated();
    }
    
    private void computePayAmountLocal() {
        jtfPayAmountLocalRo.setText(SLibUtils.getDecimalFormatAmount().format(moFieldPayAmount.getDouble() * moFieldPayExchangeRate.getDouble()));
    }
    
    private void computeDocBalancePend() {
        jtfDocBalancePend.setText(SLibUtils.getDecimalFormatAmount().format(moFieldDocBalancePrev.getDouble() - moFieldDocPayment.getDouble()));
        
        computeDocPaymentAmounts();
    }

    private void computeDocPaymentAmounts() {
        if (moFieldDocExchangeRate.getDouble() == 0) {
            jtfDocBalancePrevPayRo.setText(SLibUtils.getDecimalFormatAmount().format(0));
            jtfDocPaymentPayRo.setText(SLibUtils.getDecimalFormatAmount().format(0));
            jtfDocBalancePendPayRo.setText(SLibUtils.getDecimalFormatAmount().format(0));
        }
        else {
            jtfDocBalancePrevPayRo.setText(SLibUtils.getDecimalFormatAmount().format(moFieldDocBalancePrev.getDouble() / moFieldDocExchangeRate.getDouble()));
            jtfDocPaymentPayRo.setText(SLibUtils.getDecimalFormatAmount().format(moFieldDocPayment.getDouble() / moFieldDocExchangeRate.getDouble()));
            jtfDocBalancePendPayRo.setText(SLibUtils.getDecimalFormatAmount().format(SLibUtils.parseDouble(jtfDocBalancePend.getText()) / moFieldDocExchangeRate.getDouble()));
        }
        
        computePayTotalPayments();
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
    }

    private void enableVouFields(final boolean enable) {
        jftVouDate.setEditable(enable);
        jftVouDate.setFocusable(enable);
        jbVouDatePick.setEnabled(enable);
        jcbVouTaxRegime.setEnabled(enable);
        jtfVouConfirm.setEditable(enable);
        jtfVouConfirm.setFocusable(enable);
    }

    private void enableRecFields(final boolean enable) {
        jcbRecBizPartner.setEnabled(enable);
        jbRecBizPartnerPick.setEnabled(enable);
        
        if (enable) {
            boolean cfdRelatedAvailable = moDataRecCfdRelated != null;
            jbRecCfdRelatedAdd.setEnabled(!cfdRelatedAvailable);
            jbRecCfdRelatedDelete.setEnabled(cfdRelatedAvailable);
            jbRecCfdRelatedPick.setEnabled(cfdRelatedAvailable);
        }
        else {
            jbRecCfdRelatedAdd.setEnabled(false);
            jbRecCfdRelatedDelete.setEnabled(false);
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
        jbRecCfdRelatedPick.setEnabled(!activateControlsForAddition);
        
        moDataRecCfdRelated = null;
        renderRecCfdRelated();
    }

    private void enablePayFields(final boolean enable) {
        jftPayDate.setEditable(enable);
        jftPayDate.setFocusable(enable);
        jbPayDatePick.setEnabled(enable);
        jftPayHour.setEditable(enable);
        jftPayHour.setFocusable(enable);
        jcbPayPaymentWay.setEnabled(enable);
        jbPayRecordPick.setEnabled(enable);
        jbPayRecordView.setEnabled(enable);
        jcbPayCurrency.setEnabled(enable);
        jtfPayAmount.setEditable(enable);
        jtfPayAmount.setFocusable(enable);
        jtfPayOperation.setEditable(enable);
        jtfPayOperation.setFocusable(enable);
        jtfPayAccountSrcFiscalId.setEditable(enable);
        jtfPayAccountSrcFiscalId.setFocusable(enable);
        jtfPayAccountSrcNumber.setEditable(enable);
        jtfPayAccountSrcNumber.setFocusable(enable);
        jtfPayAccountSrcEntity.setEditable(enable);
        jtfPayAccountSrcEntity.setFocusable(enable);
        jbPayAccountSrcPick.setEnabled(enable);
        jtfPayAccountDesFiscalId.setEditable(false);
        jtfPayAccountDesFiscalId.setFocusable(false);
        jtfPayAccountDesNumber.setEditable(false);
        jtfPayAccountDesNumber.setFocusable(false);
        jcbPayAccountDes.setEnabled(enable);
        jbPayAccountDesPick.setEnabled(enable);
        
        updatePayCurrencyFields();
    }
    
    private void updatePayCurrencyFields() {
        boolean activate = jcbPayCurrency.isEnabled() && jcbPayCurrency.getSelectedIndex() > 0 && !miClient.getSession().getSessionCustom().isLocalCurrency(moFieldPayCurrency.getKeyAsIntArray());
        
        jtfPayExchangeRate.setEditable(activate);
        jtfPayExchangeRate.setFocusable(activate);
        jbPayExchangeRatePick.setEnabled(activate);
    }
    
    private void enablePayControls(final boolean enable) {
        jbPayPaymentEntryAdd.setEnabled(enable);
        jbPayPaymentEntryModify.setEnabled(enable);
        jbPayPaymentEntryOk.setEnabled(false);
        jbPayPaymentEntryCancel.setEnabled(false);
        jbPayPaymentEntryDelete.setEnabled(enable);
    }
    
    private void enableDocFields(final boolean enable) {
        jbDocDpsRelatedPick.setEnabled(enable);
        jtfDocInstallment.setEditable(enable);
        jtfDocInstallment.setFocusable(enable);
        jtfDocExchangeRate.setEditable(false);
        jtfDocExchangeRate.setFocusable(false);
        jbDocExchangeRateInvert.setEnabled(false);
        jtfDocBalancePrev.setEditable(enable);
        jtfDocBalancePrev.setFocusable(enable);
        jtfDocPayment.setEditable(enable);
        jtfDocPayment.setFocusable(enable);
        jbDocPaymentCompute.setEnabled(false);
    }
    
    private void enableDocControls(final boolean enable) {
        jbDocPaymentEntryDocAdd.setEnabled(enable);
        jbDocPaymentEntryDocModify.setEnabled(enable);
        jbDocPaymentEntryDocOk.setEnabled(false);
        jbDocPaymentEntryDocCancel.setEnabled(false);
        jbDocPaymentEntryDocDelete.setEnabled(enable);
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
            remainder = SLibUtils.roundAmount(remainder + moPaymentEntryDoc.PaymentPay);
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
            
            jftVouDate.requestFocusInWindow();
        }
    }

    private void actionPerformedRecBizPartnerPick() {
        miClient.pickOption(SDataConstants.BPSX_BP_CUS, moFieldRecBizPartner, null);
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
            renderRecCfdRelated();
        }
    }

    private void actionPerformedPayDatePick() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldPayDate.getDate(), moFieldPayDate);
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

    private void actionPerformedPayExchangeRatePick() {
        double rate = miClient.pickExchangeRate(moFieldPayCurrency.getKeyAsIntArray()[0], moFieldPayDate.getDate());

        if (rate != 0d) {
            moFieldPayExchangeRate.setFieldValue(rate);
            jtfPayExchangeRate.requestFocusInWindow();
        }
    }

    /**
     * Pick a bank account of headquarters of receiver.
     */
    private void actionPerformedPayAccountSrcPick() {
        SFormOptionPickerInterface picker = miClient.getOptionPicker(SDataConstants.BPSU_BANK_ACC);

        picker.formReset();
        picker.setFilterKey(moDataRecBizPartner.getDbmsHqBranch().getPrimaryKey());
        picker.formRefreshOptionPane();
        //picker.setSelectedPrimaryKey(...); by now there is no way to set currently selected account cash
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            SDataBizPartnerBranchBankAccount bankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BANK_ACC, picker.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
            SDataBizPartner bank = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { bankAccount.getFkBankId() }, SLibConstants.EXEC_MODE_VERBOSE);
            
            moFieldPayAccountSrcFiscalId.setString(bank.getFiscalId());
            moFieldPayAccountSrcNumber.setString(!bankAccount.getBankAccountNumberStd().isEmpty() ? bankAccount.getBankAccountNumberStd() : SLibUtils.textTrim(bankAccount.getBankAccountBranchNumber() + " " + bankAccount.getBankAccountNumber()));
            
            if (miClient.getSession().getSessionCustom().isLocalCountry(new int[] { bank.getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial().getDbmsDataCountry().getPkCountryId() })) {
                moFieldPayAccountSrcEntity.resetField();
                jtfPayAccountSrcNumber.requestFocusInWindow();
            }
            else {
                moFieldPayAccountSrcEntity.setString(bank.getBizPartner());
                jtfPayAccountSrcEntity.requestFocusInWindow();
            }
        }
    }

    /**
     * Pick a bank account of current company branch of this CFDI.
     */
    private void actionPerformedPayAccountDesPick() {
        SFormOptionPickerInterface picker = miClient.getOptionPicker(SDataConstants.FIN_ACC_CASH);

        picker.formReset();
        picker.setFilterKey(moDataComBranch.getPrimaryKey());
        picker.formRefreshOptionPane();
        picker.setSelectedPrimaryKey(moFieldPayAccountDes.getKey());
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldPayAccountDes.setKey(picker.getSelectedPrimaryKey());    // will throw an item-state-changed event
            jcbPayAccountDes.requestFocusInWindow();
        }
    }

    private void actionPerformedPayPaymentEntryAdd() {
        mbEditingPaymentEntry = true;
        
        clearPayPayment(true, false);
        enableDocControls(false);
        
        jbPayPaymentEntryOk.setEnabled(true);
        jbPayPaymentEntryCancel.setEnabled(true);
        
        // default date: session's current date:
        moFieldPayDate.setFieldValue(miClient.getSession().getCurrentDate());
        // default hour: 12:00:
        moFieldPayHour.setFieldValue(new Date(miClient.getSession().getCurrentDate().getTime() + (1000 * 60 * 60 * 12)));
        // customer's default payment way, if any:
        moFieldPayPaymentWay.setFieldValue(moDataRecBizPartner.getDbmsCategorySettingsCus().getCfdiPaymentWay());
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
                miClient.showMsgBoxWarning("El pago #" + paymentEntry.Number + " no debe tener aplicaciones para poderlo modificar.");
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

    private void actionPerformedPayPaymentEntryOk() {
        boolean valid = true;
        ArrayList<SFormField> fields = new ArrayList<>();
        
        fields.add(moFieldPayDate);
        fields.add(moFieldPayHour);
        fields.add(moFieldPayPaymentWay);
        fields.add(moFieldPayCurrency);
        fields.add(moFieldPayAmount);
        fields.add(moFieldPayExchangeRate);
        fields.add(moFieldPayOperation);
        fields.add(moFieldPayAccountSrcFiscalId);
        fields.add(moFieldPayAccountSrcNumber);
        fields.add(moFieldPayAccountSrcEntity);
        fields.add(moFieldPayAccountDesFiscalId);
        fields.add(moFieldPayAccountDesNumber);
        fields.add(moFieldPayAccountDes);
        
        for (SFormField field : fields) {
            if (!field.validateField()) {
                valid = false;
                field.getComponent().requestFocusInWindow();
                break;
            }
        }
        
        if (valid) {
            boolean isReceptorInt = moDataRecBizPartner.getFiscalId().equals(DCfdConsts.RFC_GEN_INT);
                    
            if (moDataPayRecord == null) {
                valid = false;
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlPayRecord.getText() + "'.");
                jbPayRecordPick.requestFocusInWindow();
            }
            else if (!moFieldPayAccountSrcFiscalId.getString().isEmpty() && moFieldPayAccountSrcFiscalId.getString().length() != 12 && !moFieldPayAccountSrcFiscalId.getString().equals(DCfdConsts.RFC_GEN_INT)) {
                valid = false;
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + SGuiUtils.getLabelName(jlPayAccountFiscalId) + ", " + SGuiUtils.getLabelName(jlPayAccountSrc) + "':\n"
                        + "de longitud 12 o '" + DCfdConsts.RFC_GEN_INT + "'.");
                jtfPayAccountSrcFiscalId.requestFocusInWindow();
            }
            else if (isReceptorInt && moFieldPayAccountSrcEntity.getString().isEmpty()) {
                valid = false;
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlPayAccountEntity.getText() + "'.");
                jtfPayAccountSrcEntity.requestFocusInWindow();
            }
            else if (moDataPayAccountCash == null) {
                valid = false;
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlPayAccountDes.getText() + "'.");
                jcbPayAccountDes.requestFocusInWindow();
            }
            else if (moFieldPayCurrency.getKeyAsIntArray()[0] != moDataPayAccountCash.getFkCurrencyId() && miClient.showMsgBoxConfirm("La moneda del pago no corresponde a la moneda de la cuenta beneficiaria.\n" + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                valid = false;
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlPayAccountDes.getText() + "'.");
                jcbPayAccountDes.requestFocusInWindow();
            }
        }
        
        if (valid) {
            boolean isNew = moPaymentEntry == null;
            
            GregorianCalendar gcDate = new GregorianCalendar();
            GregorianCalendar gcHour = new GregorianCalendar();
            gcDate.setTime(moFieldPayDate.getDate());
            gcHour.setTime(moFieldPayHour.getTime());
            gcDate.add(Calendar.HOUR_OF_DAY, gcHour.get(Calendar.HOUR_OF_DAY));
            gcDate.add(Calendar.MINUTE, gcHour.get(Calendar.MINUTE));
            gcDate.add(Calendar.SECOND, gcHour.get(Calendar.SECOND));
            
            if (isNew) {
                // adding payment...
                moPaymentEntry = new SCfdPaymentEntry(
                        moPaneGridPayments.getTable().getRowCount() + 1, 
                        gcDate.getTime(), 
                        moFieldPayPaymentWay.getFieldValue().toString(), 
                        moFieldPayCurrency.getKeyAsIntArray()[0], 
                        ((SFormComponentItem) jcbPayCurrency.getSelectedItem()).getComplement().toString(), 
                        moFieldPayAmount.getDouble(), 
                        moFieldPayExchangeRate.getDouble(), 
                        moDataPayRecord, 
                        moDataCfdPayment);
            }
            else {
                // modifying payment...
                //moPayment.Number
                moPaymentEntry.Date = gcDate.getTime();
                moPaymentEntry.PaymentWay = moFieldPayPaymentWay.getFieldValue().toString();
                moPaymentEntry.CurrencyId = moFieldPayCurrency.getKeyAsIntArray()[0];
                moPaymentEntry.CurrencyKey = ((SFormComponentItem) jcbPayCurrency.getSelectedItem()).getComplement().toString();
                moPaymentEntry.Amount = moFieldPayAmount.getDouble();
                moPaymentEntry.ExchangeRate = moFieldPayExchangeRate.getDouble();
                moPaymentEntry.DataRecord = moDataPayRecord;
                moPaymentEntry.computeAmountLocal();
            }
            
            moPaymentEntry.Operation = moFieldPayOperation.getString();
            moPaymentEntry.AccountSrcFiscalId = moFieldPayAccountSrcFiscalId.getString();
            moPaymentEntry.AccountSrcNumber = moFieldPayAccountSrcNumber.getString();
            moPaymentEntry.AccountSrcEntity = moFieldPayAccountSrcEntity.getString();
            moPaymentEntry.AccountDesFiscalId = moFieldPayAccountDesFiscalId.getString();
            moPaymentEntry.AccountDesNumber = moFieldPayAccountDesNumber.getString();
            moPaymentEntry.AccountDesKey = moFieldPayAccountDes.getKeyAsIntArray();
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
        
        jbPayPaymentEntryAdd.requestFocusInWindow();
        
        valueChangedPayments(); // to force showing of currency of payment in doc fields
    }

    private void actionPerformedPayPaymentEntryDelete() {
        if (moPaneGridPayments.getSelectedTableRow() == null) {
            miClient.showMsgBoxWarning(SGridConsts.MSG_SELECT_ROW);
            moPaneGridPayments.getTable().requestFocusInWindow();
        }
        else if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
            int index = moPaneGridPayments.getTable().getSelectedRow();
            
            try {
                SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.removeTableRow(moPaneGridPayments.getTable().getSelectedRow());
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
                    paymentEntry.Number = ++number;
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

    private void actionPerformedDocDpsRelatedPick() {
        int year = SLibTimeUtils.digestYear(moFieldVouDate.getDate())[0];
        Object[] filterKey = new Object[] { year, SDataConstantsSys.TRNS_CL_DPS_SAL_DOC, moFieldRecBizPartner.getKeyAsIntArray() };
        
        moDialogDocDpsRelatedPicker.formReset();
        moDialogDocDpsRelatedPicker.setFilterKey(filterKey);
        moDialogDocDpsRelatedPicker.formRefreshOptionPane();
        moDialogDocDpsRelatedPicker.setFormVisible(true);

        if (moDialogDocDpsRelatedPicker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
            
            // validate that doc has not been already added:
            
            boolean isValid = true;
            
            for (SCfdPaymentEntryDoc paymentEntryDoc : paymentEntry.PaymentEntryDocs) {
                if (SLibUtils.compareKeys(paymentEntryDoc.DataDps.getPrimaryKey(), moDialogDocDpsRelatedPicker.getSelectedPrimaryKey())) {
                    isValid = false;
                    miClient.showMsgBoxWarning("El documento " + paymentEntryDoc.DataDps.getDpsNumber() + " ya está agregado en el pago #" + paymentEntry.Number + ".");
                    jbDocDpsRelatedPick.requestFocusInWindow();
                    break;
                }
            }
            
            if (isValid) {
                // read doc:

                moDataDocDpsRelated = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moDialogDocDpsRelatedPicker.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
                renderDocDpsRelated();

                double[] balance = new double[] { 0, 0 };

                try {
                    balance = SDataUtilities.obtainDpsBalance(miClient, (int[]) moDataDocDpsRelated.getPrimaryKey(), year);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }

                // set default doc values:

                moFieldDocInstallment.setFieldValue(STrnUtilities.countDpsPayments(miClient.getSession().getStatement(), (int[]) moDataDocDpsRelated.getPrimaryKey(), moDataCfdPayment == null ? 0 : moDataCfdPayment.getDbmsDataCfd().getPkCfdId()) + 1);

                if (paymentEntry.CurrencyId == moDataDocDpsRelated.getFkCurrencyId()) {
                    moFieldDocExchangeRate.setFieldValue(1d);
                    jtfDocExchangeRate.setEditable(false);
                    jtfDocExchangeRate.setFocusable(false);
                    jbDocExchangeRateInvert.setEnabled(false);
                    jbDocPaymentCompute.setEnabled(false);
                }
                else {
                    moFieldDocExchangeRate.setFieldValue(0d);
                    jtfDocExchangeRate.setEditable(true);
                    jtfDocExchangeRate.setFocusable(true);
                    jbDocExchangeRateInvert.setEnabled(true);
                    jbDocPaymentCompute.setEnabled(true);
                }

                moFieldDocBalancePrev.setFieldValue(balance[1]);

                double remainder = SLibUtils.roundAmount(getPaymentRemainder(paymentEntry) * moFieldDocExchangeRate.getDouble());
                moFieldDocPayment.setFieldValue(balance[1] <= remainder ? balance[1] : remainder);
                computeDocBalancePend();

                jtfDocInstallment.requestFocusInWindow();
            }
        }
    }
    
    private void actionPerformedDocExchangeRateInvert() {
        moFieldDocExchangeRate.setDouble(moFieldDocExchangeRate.getDouble() == 0 ? 0 : SLibUtils.round(1d / moFieldDocExchangeRate.getDouble(), SLibUtils.getDecimalFormatExchangeRate().getMaximumFractionDigits()));
        jtfDocExchangeRate.requestFocusInWindow();
    }
    
    private void actionPerformedDocPaymentCompute() {
        SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
        double remainder = SLibUtils.roundAmount(getPaymentRemainder(paymentEntry) * moFieldDocExchangeRate.getDouble());
        moFieldDocPayment.setFieldValue(moFieldDocBalancePrev.getDouble() <= remainder ? moFieldDocBalancePrev.getDouble() : remainder);
        computeDocBalancePend();
        
        jtfDocPayment.requestFocusInWindow();
    }

    private void actionPerformedDocPaymentEntryDocAdd(boolean shortcutDpsRelatedPick) {
        mbEditingPaymentEntryDoc = true;
        
        clearDocPaymentDoc(true, false);
        enablePayControls(false);
        moPaneGridPayments.getTable().setEnabled(false);
        
        jbDocPaymentEntryDocOk.setEnabled(true);
        jbDocPaymentEntryDocCancel.setEnabled(true);
        
        if (shortcutDpsRelatedPick) {
            jbDocDpsRelatedPick.doClick();  // shortcut to pick doc
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
                
                jbDocDpsRelatedPick.requestFocusInWindow();
            }
        }
    }

    private void actionPerformedDocPaymentEntryDocOk() {
        boolean valid = true;
        
        if (moDataDocDpsRelated == null) {
            valid = false;
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDocDpsRelated.getText() + "'.");
            jbDocDpsRelatedPick.requestFocusInWindow();
        }
        else if (moDataDocDpsRelated.getDbmsDataCfd().getUuid().isEmpty()) {
            valid = false;
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDocDpsRelated.getText() + ": " + jtfDocDpsRelatedUuidRo.getToolTipText() + "'.");
            jbDocDpsRelatedPick.requestFocusInWindow();
        }
        else if (!moDataDocDpsRelated.getDbmsDataDpsCfd().getPaymentMethod().equals(DCfdi33Catalogs.MDP_PPD)) {
            valid = false;
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlDocPaymentMethod.getText() + "'.");
            jbDocDpsRelatedPick.requestFocusInWindow();
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
            
            if (SLibUtils.parseDouble(jtfDocPaymentPayRo.getText()) > remainder) {
                valid = false;
                
                String warning = SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlDocPayment.getText() + "':\n";
                if (paymentEntry.AuxTotalPayments == 0) {
                    warning += "no debe ser mayor que el ";
                }
                else {
                    warning += "no debe ser mayor que el remanente de $" + SLibUtils.getDecimalFormatAmount().format(remainder) + " " + paymentEntry.CurrencyKey + " del ";
                }
                warning += "pago de $" + SLibUtils.getDecimalFormatAmount().format(paymentEntry.Amount) + " " + paymentEntry.CurrencyKey + ".";
                miClient.showMsgBoxWarning(warning);
                
                jtfDocPayment.requestFocusInWindow();
            }
        }
        
        if (valid) {
            boolean isNew = moPaymentEntryDoc == null;
            
            SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
            
            if (isNew) {
                // adding document...
                moPaymentEntryDoc = new SCfdPaymentEntryDoc(
                        moPaneGridPaymentDocs.getTable().getRowCount() + 1,
                        moDataDocDpsRelated,
                        moFieldDocInstallment.getInteger(),
                        moFieldDocBalancePrev.getDouble(),
                        moFieldDocPayment.getDouble(),
                        moFieldDocExchangeRate.getDouble(),
                        paymentEntry);
            }
            else {
                // modifying document...
                //moPaymentDoc.Number
                moPaymentEntryDoc.DataDps = moDataDocDpsRelated;
                moPaymentEntryDoc.Installment = moFieldDocInstallment.getInteger();
                moPaymentEntryDoc.BalancePrev = moFieldDocBalancePrev.getDouble();
                moPaymentEntryDoc.Payment = moFieldDocPayment.getDouble();
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
            
            computePayTotalPayments();
            actionPerformedDocPaymentEntryDocCancel();
        }
    }
    
    private void actionPerformedDocPaymentEntryDocCancel() {
        mbEditingPaymentEntryDoc = false;
        
        clearDocPaymentDoc(false, true);
        enablePayControls(true);
        moPaneGridPayments.getTable().setEnabled(true);
        
        jbDocPaymentEntryDocAdd.requestFocusInWindow();
    }

    private void actionPerformedDocPaymentEntryDocDelete() {
        if (moPaneGridPaymentDocs.getSelectedTableRow() == null) {
            miClient.showMsgBoxWarning(SGridConsts.MSG_SELECT_ROW);
            moPaneGridPaymentDocs.getTable().requestFocusInWindow();
        }
        else if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
            int index = moPaneGridPaymentDocs.getTable().getSelectedRow();
            
            // remove current doc from current payment:
            SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) moPaneGridPayments.getSelectedTableRow();
            paymentEntry.PaymentEntryDocs.remove((SCfdPaymentEntryDoc) moPaneGridPaymentDocs.getSelectedTableRow());
            
            // remove current doc from docs grid:
            moPaneGridPaymentDocs.removeTableRow(moPaneGridPaymentDocs.getTable().getSelectedRow());
            
            // renumber all docs:
            int number = 0;
            for (STableRow row : moPaneGridPaymentDocs.getGridRows()) {
                SCfdPaymentEntryDoc paymentEntryDoc = (SCfdPaymentEntryDoc) row;
                paymentEntryDoc.Number = ++number;
                paymentEntryDoc.prepareTableRow();
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
            moFieldRecCfdiUse.resetField();
            
            jbRecCfdRelatedAdd.setEnabled(false);
        }
        else {
            moDataRecBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, moFieldRecBizPartner.getKeyAsIntArray(), SLibConstants.EXEC_MODE_VERBOSE);
            jtfRecFiscalIdRo.setText(moDataRecBizPartner.getFiscalId());
            jtfRecCountryRo.setText(moDataRecBizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial().getDbmsDataCountry().getCountryCode());
            jtfRecForeignFiscalIdRo.setText(moDataRecBizPartner.getFiscalFrgId());
            moFieldRecCfdiUse.setFieldValue(DCfdi33Catalogs.CFDI_USO_POR_DEF); // fixed value in CFDI 3.3!
            
            jtfRecFiscalIdRo.setCaretPosition(0);
            jtfRecCountryRo.setCaretPosition(0);
            jtfRecForeignFiscalIdRo.setCaretPosition(0);
        }
    }

    private void itemStateChangedPayPaymentWay() {
        
    }

    private void itemStateChangedPayCurrency() {
        moFieldPayAmount.resetField();
        moFieldPayExchangeRate.resetField();
        
        if (jcbPayCurrency.getSelectedIndex() <= 0) {
            jtfPayAmountCurRo.setText("");
            jtfDocBalancePrevPayCurRo.setText("");
            jtfDocPaymentPayCurRo.setText("");
            jtfDocBalancePendPayCurRo.setText("");
            jtfPayTotalPaymentsCurRo.setText("");
        }
        else {
            String payCurrencyKey = ((SFormComponentItem) jcbPayCurrency.getSelectedItem()).getComplement().toString();
            
            jtfPayAmountCurRo.setText(payCurrencyKey);
            jtfDocBalancePrevPayCurRo.setText(payCurrencyKey);
            jtfDocPaymentPayCurRo.setText(payCurrencyKey);
            jtfDocBalancePendPayCurRo.setText(payCurrencyKey);
            jtfPayTotalPaymentsCurRo.setText(payCurrencyKey);
            
            if (miClient.getSession().getSessionCustom().isLocalCurrency(moFieldPayCurrency.getKeyAsIntArray())) {
                moFieldPayExchangeRate.setFieldValue(1d);
            }
        }
        
        jtfDocExchangeRateCurRo.setText(""); // allways clear doc exchange rate when payment currency is  changing
        
        updatePayCurrencyFields();
        computePayAmountLocal();
    }

    private void itemStateChangedPayAccountDes() {
        if (jcbPayAccountDes.getSelectedIndex() <= 0) {
            moDataPayAccountCash = null;
            
            moFieldPayAccountDesFiscalId.resetField();
            moFieldPayAccountDesNumber.resetField();
        }
        else {
            moDataPayAccountCash = (SDataAccountCash) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC_CASH, moFieldPayAccountDes.getKey(), SLibConstants.EXEC_MODE_VERBOSE);
            String fiscalId = "";
            String number = "";
            
            if (moDataPayAccountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_BANK) {
                SDataBizPartnerBranchBankAccount bankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry(
                        miClient, SDataConstants.BPSU_BANK_ACC, new int[] { moDataPayAccountCash.getFkBizPartnerBranchId_n(), moDataPayAccountCash.getFkBankAccountId_n() }, SLibConstants.EXEC_MODE_VERBOSE);
                SDataBizPartner bank = (SDataBizPartner) SDataUtilities.readRegistry(
                        miClient, SDataConstants.BPSU_BP, new int[] { bankAccount.getFkBankId() }, SLibConstants.EXEC_MODE_VERBOSE);
                
                fiscalId = bank.getFiscalId();
                number = !bankAccount.getBankAccountNumberStd().isEmpty() ? bankAccount.getBankAccountNumberStd() : SLibUtils.textTrim(bankAccount.getBankAccountBranchNumber() + " " + bankAccount.getBankAccountNumber());
            }
            
            moFieldPayAccountDesFiscalId.setString(fiscalId);
            moFieldPayAccountDesNumber.setString(number);
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
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
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
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDocDpsRelatedPick;
    private javax.swing.JButton jbDocExchangeRateInvert;
    private javax.swing.JButton jbDocPaymentCompute;
    private javax.swing.JButton jbDocPaymentEntryDocAdd;
    private javax.swing.JButton jbDocPaymentEntryDocCancel;
    private javax.swing.JButton jbDocPaymentEntryDocDelete;
    private javax.swing.JButton jbDocPaymentEntryDocModify;
    private javax.swing.JButton jbDocPaymentEntryDocOk;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPayAccountDesPick;
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
    private javax.swing.JButton jbVouDatePick;
    private javax.swing.JButton jbVouNext;
    private javax.swing.JButton jbVouResume;
    private javax.swing.JComboBox<SFormComponentItem> jcbPayAccountDes;
    private javax.swing.JComboBox jcbPayCurrency;
    private javax.swing.JComboBox jcbPayPaymentWay;
    private javax.swing.JComboBox jcbRecBizPartner;
    private javax.swing.JComboBox jcbRecCfdiUsage;
    private javax.swing.JComboBox jcbVouTaxRegime;
    private javax.swing.JFormattedTextField jftPayDate;
    private javax.swing.JFormattedTextField jftPayHour;
    private javax.swing.JFormattedTextField jftVouDate;
    private javax.swing.JLabel jlDocBalancePend;
    private javax.swing.JLabel jlDocBalancePrev;
    private javax.swing.JLabel jlDocCurrency;
    private javax.swing.JLabel jlDocDpsRelated;
    private javax.swing.JLabel jlDocExchangeRate;
    private javax.swing.JLabel jlDocInstallment;
    private javax.swing.JLabel jlDocPayment;
    private javax.swing.JLabel jlDocPaymentMethod;
    private javax.swing.JLabel jlPayAccount1;
    private javax.swing.JLabel jlPayAccount2;
    private javax.swing.JLabel jlPayAccountDes;
    private javax.swing.JLabel jlPayAccountEntity;
    private javax.swing.JLabel jlPayAccountFiscalId;
    private javax.swing.JLabel jlPayAccountNumber;
    private javax.swing.JLabel jlPayAccountSrc;
    private javax.swing.JLabel jlPayAmount;
    private javax.swing.JLabel jlPayCurrency;
    private javax.swing.JLabel jlPayDate;
    private javax.swing.JLabel jlPayHour;
    private javax.swing.JLabel jlPayHourTip;
    private javax.swing.JLabel jlPayOperation;
    private javax.swing.JLabel jlPayPaymentWay;
    private javax.swing.JLabel jlPayRecord;
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
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpDocs;
    private javax.swing.JPanel jpDocsTotals;
    private javax.swing.JPanel jpPays;
    private javax.swing.JPanel jpPaysTotals;
    private javax.swing.JPanel jpRegistry;
    private javax.swing.JPanel jpRegistryHead;
    private javax.swing.JPanel jpRegistryHeadRec;
    private javax.swing.JPanel jpRegistryHeadVou;
    private javax.swing.JPanel jpRegistryRows;
    private javax.swing.JPanel jpRegistryRowsDocs;
    private javax.swing.JPanel jpRegistryRowsPays;
    private javax.swing.JTextField jtfDocBalancePend;
    private javax.swing.JTextField jtfDocBalancePendCurRo;
    private javax.swing.JTextField jtfDocBalancePendPayCurRo;
    private javax.swing.JTextField jtfDocBalancePendPayRo;
    private javax.swing.JTextField jtfDocBalancePrev;
    private javax.swing.JTextField jtfDocBalancePrevCurRo;
    private javax.swing.JTextField jtfDocBalancePrevPayCurRo;
    private javax.swing.JTextField jtfDocBalancePrevPayRo;
    private javax.swing.JTextField jtfDocCurrencyRo;
    private javax.swing.JTextField jtfDocDpsRelatedNumberRo;
    private javax.swing.JTextField jtfDocDpsRelatedUuidRo;
    private javax.swing.JTextField jtfDocDpsRelatedVersionRo;
    private javax.swing.JTextField jtfDocExchangeRate;
    private javax.swing.JTextField jtfDocExchangeRateCurRo;
    private javax.swing.JTextField jtfDocInstallment;
    private javax.swing.JTextField jtfDocPayment;
    private javax.swing.JTextField jtfDocPaymentCurRo;
    private javax.swing.JTextField jtfDocPaymentMethodRo;
    private javax.swing.JTextField jtfDocPaymentPayCurRo;
    private javax.swing.JTextField jtfDocPaymentPayRo;
    private javax.swing.JTextField jtfPayAccountDesFiscalId;
    private javax.swing.JTextField jtfPayAccountDesNumber;
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
    private javax.swing.JTextField jtfRecCfdRelatedUuidRo;
    private javax.swing.JTextField jtfRecCfdRelatedVersionRo;
    private javax.swing.JTextField jtfRecCountryRo;
    private javax.swing.JTextField jtfRecFiscalIdRo;
    private javax.swing.JTextField jtfRecForeignFiscalIdRo;
    private javax.swing.JTextField jtfRecRelationTypeRo;
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
        
        moDataPayRecord = null;
        moDataPayAccountCash = null;
        moDataDocDpsRelated = null;
        moPaymentEntry = null;
        moPaymentEntryDoc = null;
        
        moPaneGridPayments.createTable(this);   // does need list-selection listener!
        moPaneGridPayments.clearTableRows();
        moPaneGridPaymentDocs.createTable();    // does not need list-selection listener!
        moPaneGridPaymentDocs.clearTableRows();
        
        msXmlRelationType = "";
        moRecordLocksMap = new HashMap<>();

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
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbRecBizPartner, SDataConstants.BPSX_BP_CUS);
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
            if (mbEditingPaymentEntry) {
                validation.setMessage("La captura de un pago está en proceso, se debe cancelar o aceptar.");
                validation.setComponent(jbPayPaymentEntryCancel);
            }
            else if (mbEditingPaymentEntryDoc) {
                validation.setMessage("La captura de un pago a un documento está en proceso, se debe cancelar o aceptar.");
                validation.setComponent(jbDocPaymentEntryDocCancel);
            }
            else if (!SDataUtilities.isPeriodOpen(miClient, moFieldVouDate.getDate())) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
                validation.setComponent(jftVouDate.isEditable() ? jftVouDate : jbVouResume);
            }
            else if (moPaneGridPayments.getTable().getRowCount() == 0) {
                validation.setMessage("El comprobante debe tener pagos.");
                validation.setComponent(moPaneGridPayments.getTable());
            }
            else {
                for (STableRow row : moPaneGridPayments.getGridRows()) {
                    SCfdPaymentEntry paymentEntry = (SCfdPaymentEntry) row;
                    
                    /*
                    For each payment validate that:
                    1. period (month) of date is open
                    2. period (month) of date of accounting voucher (record) is open
                    3. payment is totally applied
                    */
                    
                    if (!SDataUtilities.isPeriodOpen(miClient, paymentEntry.Date)) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_CLOSE + "\n"
                                + "El pago #" + paymentEntry.Number + " tiene fecha " + SLibUtils.DateFormatDate.format(paymentEntry.Date) + ".");
                        validation.setComponent(moPaneGridPayments.getTable());
                        break;
                    }
                    else if (!SDataUtilities.isPeriodOpen(miClient, paymentEntry.DataRecord.getDate())) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_CLOSE + "\n"
                                + "La póliza contable " + paymentEntry.DataRecord.getRecordPrimaryKey() + " del pago #" + paymentEntry.Number + " tiene fecha " + SLibUtils.DateFormatDate.format(paymentEntry.DataRecord.getDate()) + ".");
                        validation.setComponent(moPaneGridPayments.getTable());
                        break;
                    }
                    else if (paymentEntry.Amount != paymentEntry.AuxTotalPayments) {
                        validation.setMessage("El monto del pago #" + paymentEntry.Number + " no ha sido " + (paymentEntry.AuxTotalPayments == 0 ? "" : "totalmente ") + "aplicado.");
                        validation.setComponent(moPaneGridPayments.getTable());
                        break;
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
        moDataCfdPayment = (SDataCfdPayment) registry;
        SDataCfd cfd = moDataCfdPayment.getDbmsDataCfd();
        
        jtfVouNumberRo.setText(cfd.getCfdNumber());
        jtfVouNumberRo.setCaretPosition(0);
        
        jtfVouUuidRo.setText(cfd.getUuid());
        jtfVouUuidRo.setCaretPosition(0);
        
        jtfVouVersionRo.setText(moDataCfdPayment.getComprobanteVersion());
        jtfVouVersionRo.setCaretPosition(0);
        
        moFieldVouDate.setFieldValue(moDataCfdPayment.getComprobanteFecha());
        jtfVouDatetimeRo.setText(SLibUtils.DateFormatDatetime.format(moDataCfdPayment.getComprobanteFecha()));
        jtfVouDatetimeRo.setCaretPosition(0);
        
        jtfVouStatusRo.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_ST_DPS, new int[] { cfd.getFkXmlStatusId() }));
        jtfVouStatusRo.setCaretPosition(0);
        
        moDataComBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { moDataCfdPayment.getDbmsDataCfd().getFkCompanyBranchId_n()}, SLibConstants.EXEC_MODE_VERBOSE);
        SFormUtilities.populateComboBox(miClient, jcbPayAccountDes, SDataConstants.FIN_ACC_CASH, new int[] { moDataComBranch.getPkBizPartnerBranchId() });
        renderComBranch();
        
        moFieldVouTaxRegime.setFieldValue(moDataCfdPayment.getEmisorRegimenFiscal());
        
        moFieldVouConfirm.setFieldValue(moDataCfdPayment.getComprobanteConfirmacion());
        
        if (moDataCfdPayment.getAuxCfdDbmsDataReceptor() != null) {
            moFieldRecBizPartner.setFieldValue(new int[] { moDataCfdPayment.getAuxCfdDbmsDataReceptor().getPkBizPartnerId() });    // event item state changed thrown!

            moDataRecCfdRelated = moDataCfdPayment.getAuxCfdDbmsDataCfdCfdiRelacionado();
            renderRecCfdRelated();
        }
        
        moFieldRecCfdiUse.setFieldValue(moDataCfdPayment.getReceptorUsoCFDI());
        
        for (SCfdPaymentEntry entry : moDataCfdPayment.getAuxCfdPaymentEntries()) {
            moPaneGridPayments.addTableRow(entry);
        }
        moPaneGridPayments.renderTableRows();
        moPaneGridPayments.setTableRowSelection(0);
        computeVouTotal();
        valueChangedPayments();
        
        actionPerformedVouNext(false);
        
        if (!SDataUtilities.isPeriodOpen(miClient, cfd.getTimestamp()) || !cfd.getUuid().isEmpty()) {
            mbIsFormReadOnly = true;
        }
        
        if (mbIsFormReadOnly) {
            jbVouResume.setEnabled(false);
            
            enablePayControls(false);
            enableDocControls(false);
            
            jbOk.setEnabled(false);
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
        
        //cfdPacket.setCfdCertNumber(...);
        //cfdPacket.setCfdStringSigned(...);
        //cfdPacket.setCfdSignature(...);
        //cfdPacket.setXml(...);
        //cfdPacket.setXmlName(...);
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
        moDataCfdPayment.setAuxCfdDbmsDataReceptor(moDataRecBizPartner);
        
        moDataCfdPayment.setAuxCfdCfdiRelacionadosTipoRelacion(msXmlRelationType);
        if (msXmlRelationType.isEmpty()) {
            moDataCfdPayment.setAuxCfdDbmsDataCfdCfdiRelacionado(null);
        }
        else {
            moDataCfdPayment.setAuxCfdDbmsDataCfdCfdiRelacionado(moDataRecCfdRelated);
        }

        try {
            moDataCfdPayment.getAuxCfdPaymentEntries().clear();
            for (STableRow row : moPaneGridPayments.getGridRows()) {
                SCfdPaymentEntry entry = (SCfdPaymentEntry) row;
                entry.DataParentPayment = moDataCfdPayment;
                entry.processRecord(miClient.getSession()); // very important to process journal voucher entries, to do the accounting!
                moDataCfdPayment.getAuxCfdPaymentEntries().add(entry);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }

        // send as well locks of journal vouchers:
        moDataCfdPayment.getRegistryComplements().clear();
        for (SSrvLock lock : moRecordLocksMap.values()) {
            moDataCfdPayment.getRegistryComplements().add(lock);
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
            else if (button == jbPayRecordPick) {
                actionPerformedPayRecordPick();
            }
            else if (button == jbPayRecordView) {
                actionPerformedPayRecordView();
            }
            else if (button == jbPayExchangeRatePick) {
                actionPerformedPayExchangeRatePick();
            }
            else if (button == jbPayAccountSrcPick) {
                actionPerformedPayAccountSrcPick();
            }
            else if (button == jbPayAccountDesPick) {
                actionPerformedPayAccountDesPick();
            }
            else if (button == jbPayPaymentEntryAdd) {
                actionPerformedPayPaymentEntryAdd();
            }
            else if (button == jbPayPaymentEntryModify) {
                actionPerformedPayPaymentEntryModify();
            }
            else if (button == jbPayPaymentEntryOk) {
                actionPerformedPayPaymentEntryOk();
            }
            else if (button == jbPayPaymentEntryCancel) {
                actionPerformedPayPaymentEntryCancel();
            }
            else if (button == jbPayPaymentEntryDelete) {
                actionPerformedPayPaymentEntryDelete();
            }
            else if (button == jbDocDpsRelatedPick) {
                actionPerformedDocDpsRelatedPick();
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
            else if (button == jbDocPaymentEntryDocOk) {
                actionPerformedDocPaymentEntryDocOk();
            }
            else if (button == jbDocPaymentEntryDocCancel) {
                actionPerformedDocPaymentEntryDocCancel();
            }
            else if (button == jbDocPaymentEntryDocDelete) {
                actionPerformedDocPaymentEntryDocDelete();
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
                else if (comboBox == jcbPayAccountDes) {
                    itemStateChangedPayAccountDes();
                }
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof JTextField) {
            JTextField textField = (JTextField) e.getSource();
            
            if (textField == jtfPayAmount || textField == jtfPayExchangeRate) {
                focusLostPayExchangeRate();
            }
            else if (textField == jtfDocExchangeRate) {
                focusLostDocExchangeRate();
            }
            else if (textField == jtfDocBalancePrev || textField == jtfDocPayment) {
                focusLostDocPayment();
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
}
