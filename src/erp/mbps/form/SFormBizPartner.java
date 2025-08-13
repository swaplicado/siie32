/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormBizPartner.java
 *
 * Created on 22/09/2009, 12:35:19 PM
 */

package erp.mbps.form;

import cfd.DCfdConsts;
import cfd.ver40.DCfdi40Catalogs;
import erp.cfd.SCfdXmlCatalogs;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import erp.lib.form.SFormComboBoxGroup;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STableModel;
import erp.lib.table.STablePane;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchRow;
import erp.mbps.data.SDataBizPartnerCategory;
import erp.mbps.data.SDataBizPartnerNote;
import erp.mbps.data.SDataBizPartnerType;
import erp.mcfg.data.SCfgUtils;
import erp.mcfg.data.SDataCurrency;
import erp.mcfg.data.SDataLanguage;
import erp.mfin.data.SFinUtilities;
import erp.mfin.data.diot.SDiotConsts;
import erp.mmkt.data.SDataCustomerBranchConfig;
import erp.mmkt.data.SDataCustomerBranchConfigRow;
import erp.mmkt.data.SDataCustomerConfig;
import erp.mmkt.form.SFormCustomerBranchConfig;
import erp.mod.SModSysConsts;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibMethod;
import sa.lib.SLibUtils;

/**
 *
 * @author Alfonso Flores, Isabel Servín, Sergio Flores, Claudio Peña, Sergio Flores
 */
public class SFormBizPartner extends javax.swing.JDialog implements erp.lib.form.SFormExtendedInterface, java.awt.event.ActionListener {
    
    private static final int TAB_MAIN = 0;
    private static final int TAB_ADIT = 1;
    private static final int TAB_MKT = 2;

    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private java.util.Vector<erp.lib.form.SFormField> mvFieldsCategory;
    private java.util.Vector<erp.lib.form.SFormField> mvFieldsCustomerConfig;
    private erp.client.SClientInterface miClient;
    
    private int mnCfgParamCfdOrgNames;

    private erp.mbps.data.SDataBizPartner moBizPartner;
    private erp.mbps.data.SDataBizPartnerType moBizPartnerType;
    private erp.lib.form.SFormField moFieldFkBizPartnerIdentityTypeId;
    private erp.lib.form.SFormField moFieldLastName;
    private erp.lib.form.SFormField moFieldFirstName;
    private erp.lib.form.SFormField moFieldBizPartner;
    private erp.lib.form.SFormField moFieldBizPartnerCommercial;
    private erp.lib.form.SFormField moFieldBizPartnerFiscal;
    private erp.lib.form.SFormField moFieldBizPartnerCapitalRegime;
    private erp.lib.form.SFormField moFieldFiscalId;
    private erp.lib.form.SFormField moFieldFiscalFrgId;
    private erp.lib.form.SFormField moFieldAlternativeId;
    private erp.lib.form.SFormField moFieldFkTaxIdentityTypeId;
    private erp.lib.form.SFormField moFieldIsDeleted;
    private erp.lib.form.SFormField moFieldIsAttRelatedParty;
    private erp.lib.form.SFormField moFieldIsAttBank;
    private erp.lib.form.SFormField moFieldIsAttCarrier;
    private erp.lib.form.SFormField moFieldIsAttSalesAgent;
    private erp.lib.form.SFormField moFieldIsAttEmployee;
    private erp.lib.form.SFormField moFieldIsAttPartnerShareholder;
    private erp.lib.form.SFormField moFieldFkBizArea;
    private erp.lib.form.SFormField moFieldIsCategoryDeleted;

    private erp.lib.form.SFormField moFieldFkBizPartnerCategoryId;
    private erp.lib.form.SFormField moFieldFkBizPartnerTypeId;
    private erp.lib.form.SFormField moFieldKey;
    private erp.lib.form.SFormField moFieldCompanyKey;
    private erp.lib.form.SFormField moFieldNumberExporter;
    private erp.lib.form.SFormField moFieldFkCreditTypeId_n;
    private erp.lib.form.SFormField moFieldFkRiskTypeId;
    private erp.lib.form.SFormField moFieldGuaranteeAmount;
    private erp.lib.form.SFormField moFieldInsuranceAmount;
    private erp.lib.form.SFormField moFieldIsGuaranteeInProcess;
    private erp.lib.form.SFormField moFieldIsInsuranceInProcess;
    private erp.lib.form.SFormField moFieldCreditLimit;
    private erp.lib.form.SFormField moFieldDaysOfCredit;
    private erp.lib.form.SFormField moFieldDaysOfGrace;
    private erp.lib.form.SFormField moFieldIsCreditByUser;
    private erp.lib.form.SFormField moFieldFkLanguageId_n;
    private erp.lib.form.SFormField moFieldFkCurrencyId_n;
    private erp.lib.form.SFormField moFieldDiotOperation;
    private erp.lib.form.SFormField moFieldDateStart;
    private erp.lib.form.SFormField moFieldDateEnd;
    private erp.lib.form.SFormField moFieldLeadTime;
    private erp.lib.form.SFormField moFieldCfdiPaymentWay;
    private erp.lib.form.SFormField moFieldCfdiCfdiUsage;
    private erp.lib.form.SFormField moFieldTaxRegime;
    private erp.lib.form.SFormField moFieldFkCfdAddendaTypeId;
    private erp.lib.form.SFormField moFieldFkUserAnalystId;
    private erp.lib.form.SFormField moFieldWeb;
    private erp.lib.form.SFormField moFieldNotes;

    private erp.lib.form.SFormComboBoxGroup moComboboxGrpMarketSegment;
    private erp.lib.form.SFormField moFieldFkCustomerTypeId;
    private erp.lib.form.SFormField moFieldFkMarketSegmentId;
    private erp.lib.form.SFormField moFieldFkMarketSubsegmentId;
    private erp.lib.form.SFormField moFieldFkDistributionChannelId;
    private erp.lib.form.SFormField moFieldFkSalesRouteId;
    private erp.lib.form.SFormField moFieldFkSalesAgentId_n;
    private erp.lib.form.SFormField moFieldFkSalesSupervisorId_n;
    private erp.lib.form.SFormField moFieldIsFreeDiscountDoc;
    private erp.lib.form.SFormField moFieldIsFreeCommissions;
    private erp.lib.form.SFormField moFieldIsSignRestricted;
    private erp.lib.form.SFormField moFieldIsSignImmex;
    private erp.lib.form.SFormField moFieldIsSkipSignForeignCurrencyRestriction;

    private erp.mbps.data.SDataBizPartnerCategory moBizPartnerCategory;
    private erp.mbps.form.SFormBizPartnerBranch moFormBizPartnerBranch;

    private erp.mbps.form.SPanelBizPartnerBranch moPanelBizPartnerBranch;
    private erp.lib.table.STablePane moBizPartnerBranchPane;

    private erp.lib.form.SFormComboBoxGroup moComboBoxBizPartnerIdentity;
    private erp.mbps.data.SDataBizPartnerCategory[] maoDbmsCategorySettings;
    private erp.mmkt.data.SDataCustomerConfig moCustomerConfig;
    private erp.mmkt.data.SDataCustomerBranchConfig moCustomerBranchConfig;
    private erp.lib.table.STablePane moCusBranchConfigPane;

    private ImageIcon moIconHasCategorySup;
    private ImageIcon moIconHasCategoryCus;
    private ImageIcon moIconHasCategoryCdr;
    private ImageIcon moIconHasCategoryDbr;
    private ImageIcon moIconHasNotCategory;

    private int mnParamBizPartnerTypeX;
    private int mnParamBizPartnerCategory;
    private boolean mbCanEditCredit;

    private int mnFormTypeExport;
    private boolean mbIsNeededPosSave;
    
    private String msTempPaymentAccount;
    private int mnTempFkPaymentSystemTypeId;
    
    private SCfdXmlCatalogs moXmlCatalogs;

    /** Creates new form SFormBizPartner
     * @param client GUI client.
     */
    public SFormBizPartner(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;

        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgGuaranteeType = new javax.swing.ButtonGroup();
        bgOrgNamesPolicy = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jpBizPartner = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jpBizPartner11 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jlFkBizPartnerIdentityTypeId = new javax.swing.JLabel();
        jcbFkBizPartnerIdentityTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jLabel1 = new javax.swing.JLabel();
        jckIsDeleted = new javax.swing.JCheckBox();
        jPanel18 = new javax.swing.JPanel();
        jlFkTaxIdentityTypeId = new javax.swing.JLabel();
        jcbFkTaxIdentityTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jLabel2 = new javax.swing.JLabel();
        jckIsAttRelatedParty = new javax.swing.JCheckBox();
        jPanel28 = new javax.swing.JPanel();
        jlLastName = new javax.swing.JLabel();
        jtfLastName = new javax.swing.JTextField();
        jlFirstName = new javax.swing.JLabel();
        jtfFirstName = new javax.swing.JTextField();
        jPanel32 = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        jtfBizPartner = new javax.swing.JTextField();
        jrbOrgNamesPolicyFullName = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jlBizPartnerFiscal = new javax.swing.JLabel();
        jtfBizPartnerFiscal = new javax.swing.JTextField();
        jtfBizPartnerCapitalRegime = new javax.swing.JTextField();
        jrbOrgNamesPolicyFiscalName = new javax.swing.JRadioButton();
        jPanel34 = new javax.swing.JPanel();
        jlBizPartnerCommercial = new javax.swing.JLabel();
        jtfBizPartnerCommercial = new javax.swing.JTextField();
        jbRecreateBizPartnerCommercial = new javax.swing.JButton();
        jPanel33 = new javax.swing.JPanel();
        jlFiscalId = new javax.swing.JLabel();
        jtfFiscalId = new javax.swing.JTextField();
        jlAlternativeId = new javax.swing.JLabel();
        jftAlternativeId = new javax.swing.JFormattedTextField();
        jlFiscalFrgId = new javax.swing.JLabel();
        jtfFiscalFrgId = new javax.swing.JTextField();
        jpBizPartner12 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jlDummy1 = new javax.swing.JLabel();
        jlIconCustomer = new javax.swing.JLabel();
        jlDummy2 = new javax.swing.JLabel();
        jlIconSupplier = new javax.swing.JLabel();
        jlDummy3 = new javax.swing.JLabel();
        jlIconDebtor = new javax.swing.JLabel();
        jlDummy4 = new javax.swing.JLabel();
        jlIconCreditor = new javax.swing.JLabel();
        jlDummy5 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jbCustomer = new javax.swing.JButton();
        jbSupplier = new javax.swing.JButton();
        jbDebtor = new javax.swing.JButton();
        jbCreditor = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jckIsAttBank = new javax.swing.JCheckBox();
        jckIsAttCarrier = new javax.swing.JCheckBox();
        jckIsAttSalesAgent = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        jckIsAttEmployee = new javax.swing.JCheckBox();
        jckIsAttPartnerShareholder = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jckIsCategoryDeleted = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jlBizArea = new javax.swing.JLabel();
        jcbFkBizArea = new javax.swing.JComboBox();
        jpAdditional = new javax.swing.JPanel();
        jpAdditional1 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jlFkBizPartnerCategoryId = new javax.swing.JLabel();
        jcbFkBizPartnerCategoryId = new javax.swing.JComboBox();
        jPanel36 = new javax.swing.JPanel();
        jlFkBizPartnerTypeId = new javax.swing.JLabel();
        jcbFkBizPartnerTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkBizPartnerTypeId = new javax.swing.JButton();
        jPanel37 = new javax.swing.JPanel();
        jlKey = new javax.swing.JLabel();
        jftKey = new javax.swing.JFormattedTextField();
        jlCompanyKey2 = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        jlCompanyKey = new javax.swing.JLabel();
        jtfCompanyKey = new javax.swing.JTextField();
        jlCompanyKey1 = new javax.swing.JLabel();
        jPanel54 = new javax.swing.JPanel();
        jlNumberExporter = new javax.swing.JLabel();
        jtfNumberExporter = new javax.swing.JTextField();
        jlCompanyKey3 = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        jlDummy01 = new javax.swing.JLabel();
        jckIsCreditByUser = new javax.swing.JCheckBox();
        jPanel40 = new javax.swing.JPanel();
        jlFkCreditTypeId_n = new javax.swing.JLabel();
        jcbFkCreditTypeId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel43 = new javax.swing.JPanel();
        jlCreditLimit = new javax.swing.JLabel();
        jtfCreditLimit = new javax.swing.JTextField();
        jtfCurrencyKey = new javax.swing.JTextField();
        jPanel48 = new javax.swing.JPanel();
        jlDaysOfCredit = new javax.swing.JLabel();
        jtfDaysOfCredit = new javax.swing.JTextField();
        jPanel49 = new javax.swing.JPanel();
        jlDaysOfGrace = new javax.swing.JLabel();
        jtfDaysOfGrace = new javax.swing.JTextField();
        jPanel50 = new javax.swing.JPanel();
        jlFkRiskId = new javax.swing.JLabel();
        jcbFkRiskId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel51 = new javax.swing.JPanel();
        jlGuarantee = new javax.swing.JLabel();
        jtfGuarantee = new javax.swing.JTextField();
        jtfCurrencyKeyGuar = new javax.swing.JTextField();
        jckGuaranteeInProcess = new javax.swing.JCheckBox();
        jPanel62 = new javax.swing.JPanel();
        jlGuaranteeType = new javax.swing.JLabel();
        jrbGuaranteeTypePay = new javax.swing.JRadioButton();
        jrbGuaranteeTypeProp = new javax.swing.JRadioButton();
        jrbGuaranteeTypePayProp = new javax.swing.JRadioButton();
        jPanel52 = new javax.swing.JPanel();
        jlInsurance = new javax.swing.JLabel();
        jtfInsurance = new javax.swing.JTextField();
        jtfCurrencyKeyInsur = new javax.swing.JTextField();
        jckInsuranceInProcess = new javax.swing.JCheckBox();
        jPanel46 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jlFkLanguageId_n = new javax.swing.JLabel();
        jcbFkLanguageId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkLanguageId_n = new javax.swing.JButton();
        jbEditLanguage = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jlFkCurrencyId_n = new javax.swing.JLabel();
        jcbFkCurrencyId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkCurrencyId_n = new javax.swing.JButton();
        jbEditCurrency = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jlDiotOperation = new javax.swing.JLabel();
        jcbDiotOperation = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel9 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        jftDateStart = new javax.swing.JFormattedTextField();
        jbSetDateStart = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jlDateEnd_n = new javax.swing.JLabel();
        jftDateEnd_n = new javax.swing.JFormattedTextField();
        jbSetDateEnd_n = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jlLeadTime = new javax.swing.JLabel();
        jtfLeadTime = new javax.swing.JTextField();
        jlLeadTimeDays = new javax.swing.JLabel();
        jlLeadTimeHint = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jlCfdiPaymentWay = new javax.swing.JLabel();
        jcbCfdiPaymentWay = new javax.swing.JComboBox<SFormComponentItem>();
        jbEditCfdiPaymentWay = new javax.swing.JButton();
        jPanel53 = new javax.swing.JPanel();
        jlCfdiCfdiUsage = new javax.swing.JLabel();
        jcbCfdiCfdiUsage = new javax.swing.JComboBox<SFormComponentItem>();
        jbEditCfdiCfdiUsage = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jlTaxRegime = new javax.swing.JLabel();
        jcbTaxRegime = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel35 = new javax.swing.JPanel();
        jPanel60 = new javax.swing.JPanel();
        jlFkCfdAddendaTypeId = new javax.swing.JLabel();
        jcbFkCfdAddendaTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel61 = new javax.swing.JPanel();
        jlFkUserAnalystId = new javax.swing.JLabel();
        jcbFkUserAnalystId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel5 = new javax.swing.JPanel();
        jlWeb = new javax.swing.JLabel();
        jtfWeb = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jlNotes = new javax.swing.JLabel();
        jtfNotes = new javax.swing.JTextField();
        jpBizPartnerBranches = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jbAddBranch = new javax.swing.JButton();
        jbModifyBranch = new javax.swing.JButton();
        jpMarketing = new javax.swing.JPanel();
        jpMarketing1 = new javax.swing.JPanel();
        jpCusConfig = new javax.swing.JPanel();
        jPanel64 = new javax.swing.JPanel();
        jlFkCustomerTypeId = new javax.swing.JLabel();
        jcbFkCustomerTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkCustomerTypeId = new javax.swing.JButton();
        jPanel65 = new javax.swing.JPanel();
        jlFkMarketSegmentId = new javax.swing.JLabel();
        jcbFkMarketSegmentId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkMarketSegmentId = new javax.swing.JButton();
        jPanel66 = new javax.swing.JPanel();
        jlFkMarketSubsegmentId = new javax.swing.JLabel();
        jcbFkMarketSubsegmentId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkMarketSubsegmentId = new javax.swing.JButton();
        jPanel67 = new javax.swing.JPanel();
        jlFkDistributionChannelId = new javax.swing.JLabel();
        jcbFkDistributionChannelId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkDistributionChannelId = new javax.swing.JButton();
        jPanel68 = new javax.swing.JPanel();
        jlFkSalesRouteId = new javax.swing.JLabel();
        jcbFkSalesRouteId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkSalesRouteId = new javax.swing.JButton();
        jPanel69 = new javax.swing.JPanel();
        jlFkSalesAgentId_n = new javax.swing.JLabel();
        jcbFkSalesAgentId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkSalesAgentId_n = new javax.swing.JButton();
        jPanel70 = new javax.swing.JPanel();
        jlFkSalesSupervisorId_n = new javax.swing.JLabel();
        jcbFkSalesSupervisorId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkSalesSupervisorId_n = new javax.swing.JButton();
        jPanel71 = new javax.swing.JPanel();
        jckIsFreeDiscountDoc = new javax.swing.JCheckBox();
        jckIsFreeCommissions = new javax.swing.JCheckBox();
        jPanel72 = new javax.swing.JPanel();
        jckIsSignRestricted = new javax.swing.JCheckBox();
        jlIsSignRestrictedHint = new javax.swing.JLabel();
        jPanel73 = new javax.swing.JPanel();
        jckIsSignImmex = new javax.swing.JCheckBox();
        jlIsSignImmexHint = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jckIsSkipSignForeignCurrencyRestriction = new javax.swing.JCheckBox();
        jpCusBranchConfig = new javax.swing.JPanel();
        jpNotesAction = new javax.swing.JPanel();
        jbNewCusBranchConfig = new javax.swing.JButton();
        jbModifyCusBranchConfig = new javax.swing.JButton();
        jbDeleteCusBranchConfig = new javax.swing.JButton();
        jbAddCusBranchConfig = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jtfPkBizPartnerId_Ro = new javax.swing.JTextField();
        jPanel42 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Asociado de negocios");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(799, 599));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(916, 590));

        jpBizPartner.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jpBizPartner11.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpBizPartner11.setLayout(new java.awt.GridLayout(7, 1, 0, 2));

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkBizPartnerIdentityTypeId.setText("Tipo identidad: *");
        jlFkBizPartnerIdentityTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel24.add(jlFkBizPartnerIdentityTypeId);

        jcbFkBizPartnerIdentityTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jcbFkBizPartnerIdentityTypeId.setRequestFocusEnabled(false);
        jcbFkBizPartnerIdentityTypeId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbFkBizPartnerIdentityTypeIdItemStateChanged(evt);
            }
        });
        jPanel24.add(jcbFkBizPartnerIdentityTypeId);

        jLabel1.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel24.add(jLabel1);

        jckIsDeleted.setForeground(new java.awt.Color(204, 0, 0));
        jckIsDeleted.setText("Asociado negocios eliminado");
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel24.add(jckIsDeleted);

        jpBizPartner11.add(jPanel24);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkTaxIdentityTypeId.setText("Identidad imptos.: *");
        jlFkTaxIdentityTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel18.add(jlFkTaxIdentityTypeId);

        jcbFkTaxIdentityTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jcbFkTaxIdentityTypeId.setRequestFocusEnabled(false);
        jPanel18.add(jcbFkTaxIdentityTypeId);

        jLabel2.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel18.add(jLabel2);

        jckIsAttRelatedParty.setText("Es parte relacionada");
        jckIsAttRelatedParty.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel18.add(jckIsAttRelatedParty);

        jpBizPartner11.add(jPanel18);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLastName.setText("Apellido(s): *");
        jlLastName.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel28.add(jlLastName);

        jtfLastName.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel28.add(jtfLastName);

        jlFirstName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlFirstName.setText("Nombre(s): *");
        jlFirstName.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel28.add(jlFirstName);

        jtfFirstName.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel28.add(jtfFirstName);

        jpBizPartner11.add(jPanel28);

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner.setText("Razón social: *");
        jlBizPartner.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel32.add(jlBizPartner);

        jtfBizPartner.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel32.add(jtfBizPartner);

        bgOrgNamesPolicy.add(jrbOrgNamesPolicyFullName);
        jrbOrgNamesPolicyFullName.setText("Usar en PDF");
        jrbOrgNamesPolicyFullName.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel32.add(jrbOrgNamesPolicyFullName);

        jpBizPartner11.add(jPanel32);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerFiscal.setText("Nombre fiscal: *");
        jlBizPartnerFiscal.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel2.add(jlBizPartnerFiscal);

        jtfBizPartnerFiscal.setToolTipText("Nombre fiscal");
        jtfBizPartnerFiscal.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel2.add(jtfBizPartnerFiscal);

        jtfBizPartnerCapitalRegime.setToolTipText("Régimen capital");
        jtfBizPartnerCapitalRegime.setPreferredSize(new java.awt.Dimension(95, 23));
        jPanel2.add(jtfBizPartnerCapitalRegime);

        bgOrgNamesPolicy.add(jrbOrgNamesPolicyFiscalName);
        jrbOrgNamesPolicyFiscalName.setText("Usar en PDF");
        jrbOrgNamesPolicyFiscalName.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel2.add(jrbOrgNamesPolicyFiscalName);

        jpBizPartner11.add(jPanel2);

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerCommercial.setText("Nombre comercial:");
        jlBizPartnerCommercial.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel34.add(jlBizPartnerCommercial);

        jtfBizPartnerCommercial.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel34.add(jtfBizPartnerCommercial);

        jbRecreateBizPartnerCommercial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbRecreateBizPartnerCommercial.setToolTipText("Crear nombre comercial");
        jbRecreateBizPartnerCommercial.setFocusable(false);
        jbRecreateBizPartnerCommercial.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel34.add(jbRecreateBizPartnerCommercial);

        jpBizPartner11.add(jPanel34);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFiscalId.setText("RFC: *");
        jlFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel33.add(jlFiscalId);

        jtfFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel33.add(jtfFiscalId);

        jlAlternativeId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAlternativeId.setText("CURP:");
        jlAlternativeId.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel33.add(jlAlternativeId);

        jftAlternativeId.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        jftAlternativeId.setPreferredSize(new java.awt.Dimension(125, 23));
        jftAlternativeId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jftAlternativeIdFocusLost(evt);
            }
        });
        jPanel33.add(jftAlternativeId);

        jlFiscalFrgId.setForeground(new java.awt.Color(0, 102, 102));
        jlFiscalFrgId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlFiscalFrgId.setText("ID fiscal:");
        jlFiscalFrgId.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel33.add(jlFiscalFrgId);

        jtfFiscalFrgId.setPreferredSize(new java.awt.Dimension(130, 23));
        jPanel33.add(jtfFiscalFrgId);

        jpBizPartner11.add(jPanel33);

        jPanel1.add(jpBizPartner11, java.awt.BorderLayout.WEST);

        jpBizPartner12.setBorder(javax.swing.BorderFactory.createTitledBorder("Categorías del asociado de negocios:"));
        jpBizPartner12.setPreferredSize(new java.awt.Dimension(385, 200));
        jpBizPartner12.setLayout(new java.awt.GridLayout(6, 1, 0, 2));

        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDummy1.setPreferredSize(new java.awt.Dimension(21, 23));
        jPanel20.add(jlDummy1);

        jlIconCustomer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlIconCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/bp_cus.png"))); // NOI18N
        jPanel20.add(jlIconCustomer);

        jlDummy2.setPreferredSize(new java.awt.Dimension(47, 23));
        jPanel20.add(jlDummy2);

        jlIconSupplier.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlIconSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/bp_sup.png"))); // NOI18N
        jPanel20.add(jlIconSupplier);

        jlDummy3.setPreferredSize(new java.awt.Dimension(47, 23));
        jPanel20.add(jlDummy3);

        jlIconDebtor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlIconDebtor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/bp_dbr.png"))); // NOI18N
        jPanel20.add(jlIconDebtor);

        jlDummy4.setPreferredSize(new java.awt.Dimension(47, 23));
        jPanel20.add(jlDummy4);

        jlIconCreditor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlIconCreditor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/bp_cdr.png"))); // NOI18N
        jPanel20.add(jlIconCreditor);

        jlDummy5.setPreferredSize(new java.awt.Dimension(21, 23));
        jPanel20.add(jlDummy5);

        jpBizPartner12.add(jPanel20);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbCustomer.setText("Cliente");
        jbCustomer.setMargin(new java.awt.Insets(2, 7, 2, 7));
        jbCustomer.setPreferredSize(new java.awt.Dimension(84, 23));
        jPanel15.add(jbCustomer);

        jbSupplier.setText("Proveedor");
        jbSupplier.setMargin(new java.awt.Insets(2, 7, 2, 7));
        jbSupplier.setPreferredSize(new java.awt.Dimension(84, 23));
        jPanel15.add(jbSupplier);

        jbDebtor.setText("Deudor div.");
        jbDebtor.setMargin(new java.awt.Insets(2, 7, 2, 7));
        jbDebtor.setPreferredSize(new java.awt.Dimension(84, 23));
        jPanel15.add(jbDebtor);

        jbCreditor.setText("Acreedor div.");
        jbCreditor.setMargin(new java.awt.Insets(2, 7, 2, 7));
        jbCreditor.setPreferredSize(new java.awt.Dimension(84, 23));
        jPanel15.add(jbCreditor);

        jpBizPartner12.add(jPanel15);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsAttBank.setText("Es banco");
        jckIsAttBank.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel11.add(jckIsAttBank);

        jckIsAttCarrier.setText("Es transportista");
        jckIsAttCarrier.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel11.add(jckIsAttCarrier);

        jckIsAttSalesAgent.setText("Es agente ventas");
        jckIsAttSalesAgent.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel11.add(jckIsAttSalesAgent);

        jpBizPartner12.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsAttEmployee.setText("Es empleado");
        jckIsAttEmployee.setEnabled(false);
        jckIsAttEmployee.setPreferredSize(new java.awt.Dimension(115, 23));
        jckIsAttEmployee.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jckIsAttEmployeeItemStateChanged(evt);
            }
        });
        jPanel12.add(jckIsAttEmployee);

        jckIsAttPartnerShareholder.setText("Es socio/accionista");
        jPanel12.add(jckIsAttPartnerShareholder);

        jpBizPartner12.add(jPanel12);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jckIsCategoryDeleted.setForeground(new java.awt.Color(204, 0, 0));
        jckIsCategoryDeleted.setText("Categoría eliminada");
        jckIsCategoryDeleted.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jckIsCategoryDeleted.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel3.add(jckIsCategoryDeleted);

        jpBizPartner12.add(jPanel3);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizArea.setText("Área de negocios: *");
        jlBizArea.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlBizArea);

        jcbFkBizArea.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkBizArea.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel6.add(jcbFkBizArea);

        jpBizPartner12.add(jPanel6);

        jPanel1.add(jpBizPartner12, java.awt.BorderLayout.CENTER);

        jpBizPartner.add(jPanel1, java.awt.BorderLayout.NORTH);

        jTabbedPane1.addTab("Información general", jpBizPartner);

        jpAdditional.setLayout(new java.awt.BorderLayout());

        jpAdditional1.setLayout(new java.awt.BorderLayout());

        jPanel45.setBorder(javax.swing.BorderFactory.createTitledBorder("Categoría y tipo de asociado de negocios e información de crédito:"));
        jPanel45.setLayout(new java.awt.GridLayout(14, 1, 0, 2));

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkBizPartnerCategoryId.setText("Categoría AN: *");
        jlFkBizPartnerCategoryId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel29.add(jlFkBizPartnerCategoryId);

        jcbFkBizPartnerCategoryId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkBizPartnerCategoryId.setEnabled(false);
        jcbFkBizPartnerCategoryId.setFocusable(false);
        jcbFkBizPartnerCategoryId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel29.add(jcbFkBizPartnerCategoryId);

        jPanel45.add(jPanel29);

        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkBizPartnerTypeId.setText("Tipo AN: *");
        jlFkBizPartnerTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel36.add(jlFkBizPartnerTypeId);

        jcbFkBizPartnerTypeId.setPreferredSize(new java.awt.Dimension(250, 23));
        jcbFkBizPartnerTypeId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbFkBizPartnerTypeIdItemStateChanged(evt);
            }
        });
        jPanel36.add(jcbFkBizPartnerTypeId);

        jbFkBizPartnerTypeId.setText("jButton3");
        jbFkBizPartnerTypeId.setToolTipText("Seleccionar tipo de asociado de negocios");
        jbFkBizPartnerTypeId.setFocusable(false);
        jbFkBizPartnerTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel36.add(jbFkBizPartnerTypeId);

        jPanel45.add(jPanel36);

        jPanel37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlKey.setText("Clave AN: *");
        jlKey.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel37.add(jlKey);

        jftKey.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        jftKey.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel37.add(jftKey);

        jlCompanyKey2.setForeground(java.awt.Color.gray);
        jlCompanyKey2.setText("(asignada por la empresa)");
        jlCompanyKey2.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel37.add(jlCompanyKey2);

        jPanel45.add(jPanel37);

        jPanel38.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCompanyKey.setText("Clave empresa:");
        jlCompanyKey.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel38.add(jlCompanyKey);

        jtfCompanyKey.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel38.add(jtfCompanyKey);

        jlCompanyKey1.setForeground(java.awt.Color.gray);
        jlCompanyKey1.setText("(asignada por el AN)");
        jlCompanyKey1.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel38.add(jlCompanyKey1);

        jPanel45.add(jPanel38);

        jPanel54.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlNumberExporter.setText("No. exportador:");
        jlNumberExporter.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel54.add(jlNumberExporter);

        jtfNumberExporter.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel54.add(jtfNumberExporter);

        jlCompanyKey3.setForeground(java.awt.Color.gray);
        jlCompanyKey3.setText("(número exportador confiable)");
        jlCompanyKey3.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel54.add(jlCompanyKey3);

        jPanel45.add(jPanel54);

        jPanel39.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDummy01.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel39.add(jlDummy01);

        jckIsCreditByUser.setText("Configuración manual de crédito");
        jckIsCreditByUser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jckIsCreditByUserItemStateChanged(evt);
            }
        });
        jPanel39.add(jckIsCreditByUser);

        jPanel45.add(jPanel39);

        jPanel40.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkCreditTypeId_n.setText("Tipo crédito: *");
        jlFkCreditTypeId_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel40.add(jlFkCreditTypeId_n);

        jcbFkCreditTypeId_n.setPreferredSize(new java.awt.Dimension(250, 23));
        jcbFkCreditTypeId_n.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbFkCreditTypeId_nItemStateChanged(evt);
            }
        });
        jPanel40.add(jcbFkCreditTypeId_n);

        jPanel45.add(jPanel40);

        jPanel43.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCreditLimit.setText("Límite crédito: *");
        jlCreditLimit.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel43.add(jlCreditLimit);

        jtfCreditLimit.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfCreditLimit.setText("0.00");
        jtfCreditLimit.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel43.add(jtfCreditLimit);

        jtfCurrencyKey.setEditable(false);
        jtfCurrencyKey.setFocusable(false);
        jtfCurrencyKey.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel43.add(jtfCurrencyKey);

        jPanel45.add(jPanel43);

        jPanel48.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDaysOfCredit.setText("Días crédito: *");
        jlDaysOfCredit.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel48.add(jlDaysOfCredit);

        jtfDaysOfCredit.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDaysOfCredit.setText("0");
        jtfDaysOfCredit.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel48.add(jtfDaysOfCredit);

        jPanel45.add(jPanel48);

        jPanel49.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDaysOfGrace.setText("Días gracia:");
        jlDaysOfGrace.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel49.add(jlDaysOfGrace);

        jtfDaysOfGrace.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDaysOfGrace.setText("0");
        jtfDaysOfGrace.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel49.add(jtfDaysOfGrace);

        jPanel45.add(jPanel49);

        jPanel50.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkRiskId.setText("Riesgo: *");
        jlFkRiskId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel50.add(jlFkRiskId);

        jcbFkRiskId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel50.add(jcbFkRiskId);

        jPanel45.add(jPanel50);

        jPanel51.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlGuarantee.setText("Monto garantía:");
        jlGuarantee.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel51.add(jlGuarantee);

        jtfGuarantee.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfGuarantee.setText("0.00");
        jtfGuarantee.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel51.add(jtfGuarantee);

        jtfCurrencyKeyGuar.setEditable(false);
        jtfCurrencyKeyGuar.setFocusable(false);
        jtfCurrencyKeyGuar.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel51.add(jtfCurrencyKeyGuar);

        jckGuaranteeInProcess.setText("En trámite");
        jckGuaranteeInProcess.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel51.add(jckGuaranteeInProcess);

        jPanel45.add(jPanel51);

        jPanel62.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlGuaranteeType.setText("Tipo garantía:");
        jlGuaranteeType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel62.add(jlGuaranteeType);

        bgGuaranteeType.add(jrbGuaranteeTypePay);
        jrbGuaranteeTypePay.setText("Pagaré");
        jrbGuaranteeTypePay.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel62.add(jrbGuaranteeTypePay);

        bgGuaranteeType.add(jrbGuaranteeTypeProp);
        jrbGuaranteeTypeProp.setText("Inmueble");
        jrbGuaranteeTypeProp.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel62.add(jrbGuaranteeTypeProp);

        bgGuaranteeType.add(jrbGuaranteeTypePayProp);
        jrbGuaranteeTypePayProp.setText("Pagaré + Inmueble");
        jrbGuaranteeTypePayProp.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel62.add(jrbGuaranteeTypePayProp);

        jPanel45.add(jPanel62);

        jPanel52.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlInsurance.setText("Monto seguro:");
        jlInsurance.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel52.add(jlInsurance);

        jtfInsurance.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfInsurance.setText("0.00");
        jtfInsurance.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel52.add(jtfInsurance);

        jtfCurrencyKeyInsur.setEditable(false);
        jtfCurrencyKeyInsur.setFocusable(false);
        jtfCurrencyKeyInsur.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel52.add(jtfCurrencyKeyInsur);

        jckInsuranceInProcess.setText("En trámite");
        jckInsuranceInProcess.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel52.add(jckInsuranceInProcess);

        jPanel45.add(jPanel52);

        jpAdditional1.add(jPanel45, java.awt.BorderLayout.WEST);

        jPanel46.setBorder(javax.swing.BorderFactory.createTitledBorder("Información adicional:"));
        jPanel46.setLayout(new java.awt.GridLayout(14, 1, 0, 2));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkLanguageId_n.setText("Idioma predeterminado:");
        jlFkLanguageId_n.setPreferredSize(new java.awt.Dimension(155, 23));
        jPanel7.add(jlFkLanguageId_n);

        jcbFkLanguageId_n.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel7.add(jcbFkLanguageId_n);

        jbFkLanguageId_n.setText("jButton1");
        jbFkLanguageId_n.setToolTipText("Seleccionar idioma");
        jbFkLanguageId_n.setFocusable(false);
        jbFkLanguageId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jbFkLanguageId_n);

        jbEditLanguage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditLanguage.setToolTipText("Modificar idioma");
        jbEditLanguage.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEditLanguage.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jbEditLanguage);

        jPanel46.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCurrencyId_n.setText("Moneda predeterminada:");
        jlFkCurrencyId_n.setPreferredSize(new java.awt.Dimension(155, 23));
        jPanel8.add(jlFkCurrencyId_n);

        jcbFkCurrencyId_n.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel8.add(jcbFkCurrencyId_n);

        jbFkCurrencyId_n.setText("jButton2");
        jbFkCurrencyId_n.setToolTipText("Seleccionar moneda");
        jbFkCurrencyId_n.setFocusable(false);
        jbFkCurrencyId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel8.add(jbFkCurrencyId_n);

        jbEditCurrency.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditCurrency.setToolTipText("Modificar moneda");
        jbEditCurrency.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEditCurrency.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel8.add(jbEditCurrency);

        jPanel46.add(jPanel8);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDiotOperation.setText("Tipo operación para DIOT:*");
        jlDiotOperation.setPreferredSize(new java.awt.Dimension(155, 23));
        jPanel14.add(jlDiotOperation);

        jcbDiotOperation.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel14.add(jcbDiotOperation);

        jPanel46.add(jPanel14);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Inicio de operaciones: *");
        jlDateStart.setPreferredSize(new java.awt.Dimension(155, 23));
        jPanel9.add(jlDateStart);

        jftDateStart.setText("dd/mm/yyyy");
        jftDateStart.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel9.add(jftDateStart);

        jbSetDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_date_day.gif"))); // NOI18N
        jbSetDateStart.setToolTipText("Seleccionar fecha");
        jbSetDateStart.setFocusable(false);
        jbSetDateStart.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel9.add(jbSetDateStart);

        jPanel46.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd_n.setText("Fin de operaciones:");
        jlDateEnd_n.setPreferredSize(new java.awt.Dimension(155, 23));
        jPanel10.add(jlDateEnd_n);

        jftDateEnd_n.setText("dd/mm/yyyy");
        jftDateEnd_n.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jftDateEnd_n);

        jbSetDateEnd_n.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_date_day.gif"))); // NOI18N
        jbSetDateEnd_n.setToolTipText("Seleccionar fecha");
        jbSetDateEnd_n.setFocusable(false);
        jbSetDateEnd_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel10.add(jbSetDateEnd_n);

        jLabel3.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel10.add(jLabel3);

        jlLeadTime.setText("Plazo de entrega:");
        jlLeadTime.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel10.add(jlLeadTime);

        jtfLeadTime.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfLeadTime.setText("0");
        jtfLeadTime.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel10.add(jtfLeadTime);

        jlLeadTimeDays.setForeground(java.awt.SystemColor.textInactiveText);
        jlLeadTimeDays.setText("días");
        jlLeadTimeDays.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel10.add(jlLeadTimeDays);

        jlLeadTimeHint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlLeadTimeHint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlLeadTimeHint.setToolTipText("Plazo de entrega en días naturales promedio del asociado de negocios");
        jlLeadTimeHint.setPreferredSize(new java.awt.Dimension(15, 23));
        jPanel10.add(jlLeadTimeHint);

        jPanel46.add(jPanel10);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel46.add(jPanel17);

        jPanel47.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdiPaymentWay.setText("Forma pago predeterminada:");
        jlCfdiPaymentWay.setPreferredSize(new java.awt.Dimension(155, 23));
        jPanel47.add(jlCfdiPaymentWay);

        jcbCfdiPaymentWay.setPreferredSize(new java.awt.Dimension(374, 23));
        jPanel47.add(jcbCfdiPaymentWay);

        jbEditCfdiPaymentWay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditCfdiPaymentWay.setToolTipText("Modificar forma de pago predeterminada");
        jbEditCfdiPaymentWay.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEditCfdiPaymentWay.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel47.add(jbEditCfdiPaymentWay);

        jPanel46.add(jPanel47);

        jPanel53.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdiCfdiUsage.setText("Uso CFDI predeterminado:");
        jlCfdiCfdiUsage.setPreferredSize(new java.awt.Dimension(155, 23));
        jPanel53.add(jlCfdiCfdiUsage);

        jcbCfdiCfdiUsage.setPreferredSize(new java.awt.Dimension(374, 23));
        jPanel53.add(jcbCfdiCfdiUsage);

        jbEditCfdiCfdiUsage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditCfdiCfdiUsage.setToolTipText("Modificar uso de CFDI predeterminado");
        jbEditCfdiCfdiUsage.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEditCfdiCfdiUsage.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel53.add(jbEditCfdiCfdiUsage);

        jPanel46.add(jPanel53);

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTaxRegime.setText("Régimen fiscal para CFDI: *");
        jlTaxRegime.setPreferredSize(new java.awt.Dimension(155, 23));
        jPanel23.add(jlTaxRegime);

        jcbTaxRegime.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel23.add(jcbTaxRegime);

        jPanel46.add(jPanel23);

        jPanel35.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel46.add(jPanel35);

        jPanel60.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCfdAddendaTypeId.setText("Tipo addenda para CFDI: *");
        jlFkCfdAddendaTypeId.setPreferredSize(new java.awt.Dimension(155, 23));
        jPanel60.add(jlFkCfdAddendaTypeId);

        jcbFkCfdAddendaTypeId.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel60.add(jcbFkCfdAddendaTypeId);

        jPanel46.add(jPanel60);

        jPanel61.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkUserAnalystId.setText("Analista del AN:");
        jlFkUserAnalystId.setPreferredSize(new java.awt.Dimension(155, 23));
        jPanel61.add(jlFkUserAnalystId);

        jcbFkUserAnalystId.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel61.add(jcbFkUserAnalystId);

        jPanel46.add(jPanel61);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlWeb.setText("Sitio web del AN:");
        jlWeb.setPreferredSize(new java.awt.Dimension(155, 23));
        jPanel5.add(jlWeb);

        jtfWeb.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel5.add(jtfWeb);

        jPanel46.add(jPanel5);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNotes.setText("Notas del AN:");
        jlNotes.setPreferredSize(new java.awt.Dimension(155, 23));
        jPanel22.add(jlNotes);

        jtfNotes.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel22.add(jtfNotes);

        jPanel46.add(jPanel22);

        jpAdditional1.add(jPanel46, java.awt.BorderLayout.CENTER);

        jpAdditional.add(jpAdditional1, java.awt.BorderLayout.NORTH);

        jpBizPartnerBranches.setBorder(javax.swing.BorderFactory.createTitledBorder("Sucursales del asociado de negocios:"));
        jpBizPartnerBranches.setPreferredSize(new java.awt.Dimension(787, 250));
        jpBizPartnerBranches.setLayout(new java.awt.BorderLayout());

        jPanel31.setPreferredSize(new java.awt.Dimension(871, 23));
        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 0));

        jbAddBranch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbAddBranch.setToolTipText("Crear [Ctrl+N]");
        jbAddBranch.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel31.add(jbAddBranch);

        jbModifyBranch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbModifyBranch.setToolTipText("Modificar [Ctrl+M]");
        jbModifyBranch.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel31.add(jbModifyBranch);

        jpBizPartnerBranches.add(jPanel31, java.awt.BorderLayout.NORTH);

        jpAdditional.add(jpBizPartnerBranches, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Información adicional", jpAdditional);

        jpMarketing.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración de comercialización para clientes:"));
        jpMarketing.setLayout(new java.awt.BorderLayout(0, 10));

        jpMarketing1.setLayout(new java.awt.BorderLayout());

        jpCusConfig.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración del cliente:"));
        jpCusConfig.setLayout(new java.awt.GridLayout(11, 1, 0, 2));

        jPanel64.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCustomerTypeId.setText("Tipo de cliente: *");
        jlFkCustomerTypeId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel64.add(jlFkCustomerTypeId);

        jcbFkCustomerTypeId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel64.add(jcbFkCustomerTypeId);

        jbFkCustomerTypeId.setText("...");
        jbFkCustomerTypeId.setToolTipText("Seleccionar tipo de cliente");
        jbFkCustomerTypeId.setFocusable(false);
        jbFkCustomerTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel64.add(jbFkCustomerTypeId);

        jpCusConfig.add(jPanel64);

        jPanel65.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkMarketSegmentId.setText("Segmento de mercado: *");
        jlFkMarketSegmentId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel65.add(jlFkMarketSegmentId);

        jcbFkMarketSegmentId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel65.add(jcbFkMarketSegmentId);

        jbFkMarketSegmentId.setText("...");
        jbFkMarketSegmentId.setToolTipText("Seleccionar segmento de mercado");
        jbFkMarketSegmentId.setFocusable(false);
        jbFkMarketSegmentId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel65.add(jbFkMarketSegmentId);

        jpCusConfig.add(jPanel65);

        jPanel66.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkMarketSubsegmentId.setText("Subsegmento de mercado: *");
        jlFkMarketSubsegmentId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel66.add(jlFkMarketSubsegmentId);

        jcbFkMarketSubsegmentId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel66.add(jcbFkMarketSubsegmentId);

        jbFkMarketSubsegmentId.setText("...");
        jbFkMarketSubsegmentId.setToolTipText("Seleccionar subsegmento de mercado");
        jbFkMarketSubsegmentId.setFocusable(false);
        jbFkMarketSubsegmentId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel66.add(jbFkMarketSubsegmentId);

        jpCusConfig.add(jPanel66);

        jPanel67.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkDistributionChannelId.setText("Canal de distribución: *");
        jlFkDistributionChannelId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel67.add(jlFkDistributionChannelId);

        jcbFkDistributionChannelId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel67.add(jcbFkDistributionChannelId);

        jbFkDistributionChannelId.setText("...");
        jbFkDistributionChannelId.setToolTipText("Seleccionar canal de distribución");
        jbFkDistributionChannelId.setFocusable(false);
        jbFkDistributionChannelId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel67.add(jbFkDistributionChannelId);

        jpCusConfig.add(jPanel67);

        jPanel68.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkSalesRouteId.setText("Ruta de ventas: *");
        jlFkSalesRouteId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel68.add(jlFkSalesRouteId);

        jcbFkSalesRouteId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel68.add(jcbFkSalesRouteId);

        jbFkSalesRouteId.setText("...");
        jbFkSalesRouteId.setToolTipText("Seleccionar ruta de ventas");
        jbFkSalesRouteId.setFocusable(false);
        jbFkSalesRouteId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel68.add(jbFkSalesRouteId);

        jpCusConfig.add(jPanel68);

        jPanel69.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkSalesAgentId_n.setText("Agente de ventas:");
        jlFkSalesAgentId_n.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel69.add(jlFkSalesAgentId_n);

        jcbFkSalesAgentId_n.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel69.add(jcbFkSalesAgentId_n);

        jbFkSalesAgentId_n.setText("...");
        jbFkSalesAgentId_n.setToolTipText("Seleccionar agente de ventas");
        jbFkSalesAgentId_n.setFocusable(false);
        jbFkSalesAgentId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel69.add(jbFkSalesAgentId_n);

        jpCusConfig.add(jPanel69);

        jPanel70.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkSalesSupervisorId_n.setText("Supervisor de ventas:");
        jlFkSalesSupervisorId_n.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel70.add(jlFkSalesSupervisorId_n);

        jcbFkSalesSupervisorId_n.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel70.add(jcbFkSalesSupervisorId_n);

        jbFkSalesSupervisorId_n.setText("...");
        jbFkSalesSupervisorId_n.setToolTipText("Seleccionar supervisor de ventas");
        jbFkSalesSupervisorId_n.setFocusable(false);
        jbFkSalesSupervisorId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel70.add(jbFkSalesSupervisorId_n);

        jpCusConfig.add(jPanel70);

        jPanel71.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsFreeDiscountDoc.setText("Sin descuento");
        jckIsFreeDiscountDoc.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel71.add(jckIsFreeDiscountDoc);

        jckIsFreeCommissions.setText("Sin comisiones");
        jckIsFreeCommissions.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel71.add(jckIsFreeCommissions);

        jpCusConfig.add(jPanel71);

        jPanel72.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsSignRestricted.setText("Es cliente restringido al timbrar CFDI");
        jckIsSignRestricted.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel72.add(jckIsSignRestricted);

        jlIsSignRestrictedHint.setForeground(java.awt.SystemColor.textInactiveText);
        jlIsSignRestrictedHint.setText("(Validar permiso especial al timbrar CFDI de facturas.)");
        jlIsSignRestrictedHint.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel72.add(jlIsSignRestrictedHint);

        jpCusConfig.add(jPanel72);

        jPanel73.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsSignImmex.setText("Es cliente IMMEX al timbrar CFDI");
        jckIsSignImmex.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel73.add(jckIsSignImmex);

        jlIsSignImmexHint.setForeground(java.awt.SystemColor.textInactiveText);
        jlIsSignImmexHint.setText("(Validar permiso especial al timbrar CFDI de facturas.)");
        jlIsSignImmexHint.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel73.add(jlIsSignImmexHint);

        jpCusConfig.add(jPanel73);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsSkipSignForeignCurrencyRestriction.setText("Omitir restricción de timbrado de CFDI en moneda extranjera");
        jckIsSkipSignForeignCurrencyRestriction.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel4.add(jckIsSkipSignForeignCurrencyRestriction);

        jpCusConfig.add(jPanel4);

        jpMarketing1.add(jpCusConfig, java.awt.BorderLayout.NORTH);

        jpCusBranchConfig.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración de las sucursales del cliente:"));
        jpCusBranchConfig.setPreferredSize(new java.awt.Dimension(783, 22));
        jpCusBranchConfig.setLayout(new java.awt.BorderLayout());

        jpNotesAction.setPreferredSize(new java.awt.Dimension(771, 23));
        jpNotesAction.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jbNewCusBranchConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbNewCusBranchConfig.setToolTipText("Crear");
        jbNewCusBranchConfig.setEnabled(false);
        jbNewCusBranchConfig.setFocusable(false);
        jbNewCusBranchConfig.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesAction.add(jbNewCusBranchConfig);

        jbModifyCusBranchConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbModifyCusBranchConfig.setToolTipText("Modificar");
        jbModifyCusBranchConfig.setFocusable(false);
        jbModifyCusBranchConfig.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesAction.add(jbModifyCusBranchConfig);

        jbDeleteCusBranchConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbDeleteCusBranchConfig.setToolTipText("Eliminar");
        jbDeleteCusBranchConfig.setFocusable(false);
        jbDeleteCusBranchConfig.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesAction.add(jbDeleteCusBranchConfig);

        jbAddCusBranchConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_insert.gif"))); // NOI18N
        jbAddCusBranchConfig.setToolTipText("Agregar sucursales");
        jbAddCusBranchConfig.setFocusable(false);
        jbAddCusBranchConfig.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesAction.add(jbAddCusBranchConfig);

        jpCusBranchConfig.add(jpNotesAction, java.awt.BorderLayout.PAGE_START);

        jpMarketing1.add(jpCusBranchConfig, java.awt.BorderLayout.CENTER);

        jpMarketing.add(jpMarketing1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Comercialización", jpMarketing);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel16.setLayout(new java.awt.GridLayout(1, 2));

        jPanel41.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jtfPkBizPartnerId_Ro.setEditable(false);
        jtfPkBizPartnerId_Ro.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPkBizPartnerId_Ro.setToolTipText("ID del registro");
        jtfPkBizPartnerId_Ro.setFocusable(false);
        jtfPkBizPartnerId_Ro.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel41.add(jtfPkBizPartnerId_Ro);

        jPanel16.add(jPanel41);

        jPanel42.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel42.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jPanel42.add(jbCancel);

        jPanel16.add(jPanel42);

        getContentPane().add(jPanel16, java.awt.BorderLayout.SOUTH);

        getAccessibleContext().setAccessibleParent(this);

        setSize(new java.awt.Dimension(1032, 684));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivate();
    }//GEN-LAST:event_formWindowActivated

    private void jcbFkBizPartnerIdentityTypeIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbFkBizPartnerIdentityTypeIdItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            itemStateChangedBizPartnerIdentityType();
        }
    }//GEN-LAST:event_jcbFkBizPartnerIdentityTypeIdItemStateChanged

    private void jftAlternativeIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jftAlternativeIdFocusLost
        focusLostAlternativeId();
    }//GEN-LAST:event_jftAlternativeIdFocusLost

    private void jcbFkCreditTypeId_nItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbFkCreditTypeId_nItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            itemStateChangedCreditTypeId();
        }
}//GEN-LAST:event_jcbFkCreditTypeId_nItemStateChanged

    private void jckIsCreditByUserItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jckIsCreditByUserItemStateChanged
        itemStateChangedIsCreditByUser();
}//GEN-LAST:event_jckIsCreditByUserItemStateChanged

    private void jckIsAttEmployeeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jckIsAttEmployeeItemStateChanged
       itemStateChangedIsAttEmployee();
    }//GEN-LAST:event_jckIsAttEmployeeItemStateChanged

    private void jcbFkBizPartnerTypeIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbFkBizPartnerTypeIdItemStateChanged
        renderBizPartnerType();
    }//GEN-LAST:event_jcbFkBizPartnerTypeIdItemStateChanged
    
    private void initComponentsExtra() {
        // get configuration parameter of organization names for CFD:
        
        try {
            mnCfgParamCfdOrgNames = SLibUtilities.parseInt(SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_CFD_ORG_NAMES));
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        
        if (mnCfgParamCfdOrgNames == 0) {
            mnCfgParamCfdOrgNames = SDataConstantsSys.CFG_PARAM_CFD_ORG_NAMES_ALL_FULL_NAME;
        }
        
        // initialize extra components:
        
        mvFields = new Vector<>();
        mvFieldsCategory = new Vector<>();
        mvFieldsCustomerConfig = new Vector<>();

        moIconHasCategorySup = new ImageIcon(getClass().getResource("/erp/img/bp_sup.png"));
        moIconHasCategoryCus = new ImageIcon(getClass().getResource("/erp/img/bp_cus.png"));
        moIconHasCategoryCdr = new ImageIcon(getClass().getResource("/erp/img/bp_cdr.png"));
        moIconHasCategoryDbr = new ImageIcon(getClass().getResource("/erp/img/bp_dbr.png"));
        moIconHasNotCategory = new ImageIcon(getClass().getResource("/erp/img/bp_bw.png"));

        moComboBoxBizPartnerIdentity = new SFormComboBoxGroup(miClient);
        moComboboxGrpMarketSegment = new SFormComboBoxGroup(miClient);

        moCusBranchConfigPane = new STablePane(miClient);
        moCusBranchConfigPane.setDoubleClickAction(this, "publicActionModifyCusBranchConfig");
        jpCusBranchConfig.add(moCusBranchConfigPane, BorderLayout.CENTER);

        erp.lib.table.STableColumnForm tableColumnsBizPartnerBranch[];
        erp.lib.table.STableColumnForm tableColumnsCob[];

        moPanelBizPartnerBranch = new SPanelBizPartnerBranch(miClient);
        jpBizPartner.add(moPanelBizPartnerBranch, BorderLayout.CENTER);

        moBizPartnerBranchPane = new STablePane(miClient);
        moBizPartnerBranchPane.setDoubleClickAction(this, "publicActionModifyBranch");
        jpBizPartnerBranches.add(moBizPartnerBranchPane, BorderLayout.CENTER);

        maoDbmsCategorySettings = new SDataBizPartnerCategory[SDataConstantsSys.BPSX_CT_BP_QTY];

        moFieldFkBizPartnerIdentityTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkBizPartnerIdentityTypeId, jlFkBizPartnerIdentityTypeId);
        moFieldFkBizPartnerIdentityTypeId.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldLastName = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfLastName, jlLastName);
        moFieldLastName.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldLastName.setLengthMax(100);
        moFieldFirstName = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfFirstName, jlFirstName);
        moFieldFirstName.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFirstName.setLengthMax(100);
        moFieldBizPartner = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfBizPartner, jlBizPartner);
        moFieldBizPartner.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldBizPartner.setLengthMax(202);
        moFieldBizPartnerCommercial = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfBizPartnerCommercial, jlBizPartnerCommercial);
        moFieldBizPartnerCommercial.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldBizPartnerCommercial.setLengthMax(202);
        moFieldBizPartnerFiscal = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfBizPartnerFiscal, jlBizPartnerFiscal);
        moFieldBizPartnerFiscal.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldBizPartnerFiscal.setLengthMax(202);
        moFieldBizPartnerCapitalRegime = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfBizPartnerCapitalRegime, jlBizPartnerFiscal);
        moFieldBizPartnerCapitalRegime.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldBizPartnerCapitalRegime.setLengthMax(50);
        moFieldFiscalId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfFiscalId, jlFiscalId);
        moFieldFiscalId.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFiscalId.setLengthMin(DCfdConsts.LEN_RFC_ORG);
        moFieldFiscalId.setLengthMax(DCfdConsts.LEN_RFC_PER);
        moFieldFiscalFrgId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfFiscalFrgId, jlFiscalFrgId);
        moFieldFiscalFrgId.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFiscalFrgId.setLengthMax(25);
        moFieldFiscalFrgId.setAutoCaseType(SLibConstants.UNDEFINED);
        moFieldAlternativeId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jftAlternativeId, jlAlternativeId);
        moFieldAlternativeId.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldAlternativeId.setLengthMax(25);
        moFieldFkTaxIdentityTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTaxIdentityTypeId, jlFkTaxIdentityTypeId);
        moFieldFkTaxIdentityTypeId.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDeleted);
        moFieldIsDeleted.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldIsAttRelatedParty = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsAttRelatedParty);
        moFieldIsAttRelatedParty.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldIsAttBank = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsAttBank);
        moFieldIsAttBank.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldIsAttCarrier = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsAttCarrier);
        moFieldIsAttCarrier.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldIsAttSalesAgent = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsAttSalesAgent);
        moFieldIsAttSalesAgent.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldIsAttEmployee = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsAttEmployee);
        moFieldIsAttEmployee.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldIsAttPartnerShareholder = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsAttPartnerShareholder);
        moFieldIsAttPartnerShareholder.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFkBizArea = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkBizArea, jlBizArea);
        moFieldFkBizArea.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldIsCategoryDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsCategoryDeleted);
        moFieldIsCategoryDeleted.setTabbedPaneIndex(0, jTabbedPane1);

        moFieldFkBizPartnerCategoryId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkBizPartnerCategoryId, jlFkBizPartnerCategoryId);
        moFieldFkBizPartnerCategoryId.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkBizPartnerTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkBizPartnerTypeId, jlFkBizPartnerTypeId);
        moFieldFkBizPartnerTypeId.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkBizPartnerTypeId.setPickerButton(jbFkBizPartnerTypeId);
        moFieldKey = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jftKey, jlKey);
        moFieldKey.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldKey.setLengthMax(25);
        moFieldCompanyKey = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfCompanyKey, jlCompanyKey);
        moFieldCompanyKey.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldCompanyKey.setLengthMax(25);
        moFieldNumberExporter = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfNumberExporter, jlNumberExporter);
        moFieldNumberExporter.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldNumberExporter.setLengthMax(20);
        moFieldFkCreditTypeId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCreditTypeId_n, jlFkCreditTypeId_n);
        moFieldFkCreditTypeId_n.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkRiskTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkRiskId, jlFkRiskId);
        moFieldFkRiskTypeId.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldGuaranteeAmount = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfGuarantee, jlGuarantee);
        moFieldGuaranteeAmount.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldIsGuaranteeInProcess = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckGuaranteeInProcess);
        moFieldIsGuaranteeInProcess.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldInsuranceAmount = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfInsurance, jlInsurance);
        moFieldInsuranceAmount.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldIsInsuranceInProcess = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckInsuranceInProcess);
        moFieldIsInsuranceInProcess.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldCreditLimit = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfCreditLimit, jlCreditLimit);
        moFieldCreditLimit.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldDaysOfCredit = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfDaysOfCredit, jlDaysOfCredit);
        moFieldDaysOfCredit.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldDaysOfGrace = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfDaysOfGrace, jlDaysOfGrace);
        moFieldDaysOfGrace.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldIsCreditByUser = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsCreditByUser);
        moFieldIsCreditByUser.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkLanguageId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkLanguageId_n, jlFkLanguageId_n);
        moFieldFkLanguageId_n.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkLanguageId_n.setPickerButton(jbFkLanguageId_n);
        moFieldFkCurrencyId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkCurrencyId_n, jlFkCurrencyId_n);
        moFieldFkCurrencyId_n.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkCurrencyId_n.setPickerButton(jbFkCurrencyId_n);
        moFieldDiotOperation = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbDiotOperation, jlDiotOperation);
        moFieldDiotOperation.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldDateStart = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateStart, jlDateStart);
        moFieldDateStart.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldDateStart.setPickerButton(jbSetDateStart);
        moFieldDateEnd = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftDateEnd_n, jlDateEnd_n);
        moFieldDateEnd.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldDateEnd.setPickerButton(jbSetDateEnd_n);
        moFieldLeadTime = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfLeadTime, jlLeadTime);
        moFieldLeadTime.setTabbedPaneIndex(1, jTabbedPane1);

        moFieldCfdiPaymentWay = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCfdiPaymentWay, jlCfdiPaymentWay);
        moFieldCfdiPaymentWay.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldCfdiCfdiUsage = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCfdiCfdiUsage, jlCfdiCfdiUsage);
        moFieldCfdiCfdiUsage.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldTaxRegime = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbTaxRegime, jlTaxRegime);
        moFieldTaxRegime.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkCfdAddendaTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCfdAddendaTypeId, jlFkCfdAddendaTypeId);
        moFieldFkCfdAddendaTypeId.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkUserAnalystId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkUserAnalystId, jlFkUserAnalystId);
        moFieldFkUserAnalystId.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldWeb = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfWeb, jlWeb);
        moFieldWeb.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldWeb.setLengthMax(100);
        moFieldWeb.setAutoCaseType(SLibConstants.UNDEFINED);
        moFieldNotes = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfNotes, jlNotes);
        moFieldNotes.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldNotes.setLengthMax(255);

        moFieldFkCustomerTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCustomerTypeId, jlFkCustomerTypeId);
        moFieldFkCustomerTypeId.setTabbedPaneIndex(2, jTabbedPane1);
        moFieldFkMarketSegmentId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkMarketSegmentId, jlFkMarketSegmentId);
        moFieldFkMarketSegmentId.setTabbedPaneIndex(2, jTabbedPane1);
        moFieldFkMarketSubsegmentId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkMarketSubsegmentId, jlFkMarketSubsegmentId);
        moFieldFkMarketSubsegmentId.setTabbedPaneIndex(2, jTabbedPane1);
        moFieldFkDistributionChannelId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkDistributionChannelId, jlFkDistributionChannelId);
        moFieldFkDistributionChannelId.setTabbedPaneIndex(2, jTabbedPane1);
        moFieldFkSalesRouteId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkSalesRouteId, jlFkSalesRouteId);
        moFieldFkSalesRouteId.setTabbedPaneIndex(2, jTabbedPane1);
        moFieldFkSalesAgentId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkSalesAgentId_n, jlFkSalesAgentId_n);
        moFieldFkSalesAgentId_n.setTabbedPaneIndex(2, jTabbedPane1);
        moFieldFkSalesSupervisorId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkSalesSupervisorId_n, jlFkSalesSupervisorId_n);
        moFieldFkSalesSupervisorId_n.setTabbedPaneIndex(2, jTabbedPane1);
        moFieldIsFreeDiscountDoc = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsFreeDiscountDoc);
        moFieldIsFreeDiscountDoc.setTabbedPaneIndex(2, jTabbedPane1);
        moFieldIsFreeCommissions = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsFreeCommissions);
        moFieldIsFreeCommissions.setTabbedPaneIndex(2, jTabbedPane1);
        moFieldIsSignRestricted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsSignRestricted);
        moFieldIsSignRestricted.setTabbedPaneIndex(2, jTabbedPane1);
        moFieldIsSignImmex = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsSignImmex);
        moFieldIsSignImmex.setTabbedPaneIndex(2, jTabbedPane1);
        moFieldIsSkipSignForeignCurrencyRestriction = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsSkipSignForeignCurrencyRestriction);
        moFieldIsSkipSignForeignCurrencyRestriction.setTabbedPaneIndex(2, jTabbedPane1);

        mvFields.add(moFieldFkBizPartnerIdentityTypeId);
        mvFields.add(moFieldBizPartnerCommercial);
        mvFields.add(moFieldBizPartnerFiscal);
        mvFields.add(moFieldBizPartnerCapitalRegime);
        mvFields.add(moFieldIsDeleted);
        mvFields.add(moFieldIsAttRelatedParty);
        mvFields.add(moFieldIsAttBank);
        mvFields.add(moFieldIsAttCarrier);
        mvFields.add(moFieldIsAttSalesAgent);
        mvFields.add(moFieldIsAttEmployee);
        mvFields.add(moFieldIsAttPartnerShareholder);
        mvFields.add(moFieldFkBizArea);
        mvFields.add(moFieldIsCategoryDeleted);

        mvFieldsCategory.add(moFieldFkBizPartnerCategoryId);
        mvFieldsCategory.add(moFieldFkBizPartnerTypeId);
        mvFieldsCategory.add(moFieldKey);
        mvFieldsCategory.add(moFieldCompanyKey);
        mvFieldsCategory.add(moFieldNumberExporter);
        mvFieldsCategory.add(moFieldFkCreditTypeId_n);
        mvFieldsCategory.add(moFieldCreditLimit);
        mvFieldsCategory.add(moFieldDaysOfCredit);
        mvFieldsCategory.add(moFieldDaysOfGrace);
        mvFieldsCategory.add(moFieldFkRiskTypeId);
        mvFieldsCategory.add(moFieldGuaranteeAmount);
        mvFieldsCategory.add(moFieldIsGuaranteeInProcess);
        mvFieldsCategory.add(moFieldInsuranceAmount);
        mvFieldsCategory.add(moFieldIsInsuranceInProcess);
        mvFieldsCategory.add(moFieldIsCreditByUser);
        mvFieldsCategory.add(moFieldFkLanguageId_n);
        mvFieldsCategory.add(moFieldFkCurrencyId_n);
        mvFieldsCategory.add(moFieldDiotOperation);
        mvFieldsCategory.add(moFieldDateStart);
        mvFieldsCategory.add(moFieldDateEnd);
        mvFieldsCategory.add(moFieldLeadTime);
        mvFieldsCategory.add(moFieldCfdiPaymentWay);
        mvFieldsCategory.add(moFieldCfdiCfdiUsage);
        mvFieldsCategory.add(moFieldTaxRegime);
        mvFieldsCategory.add(moFieldFkCfdAddendaTypeId);
        mvFieldsCategory.add(moFieldFkUserAnalystId);
        mvFieldsCategory.add(moFieldWeb);
        mvFieldsCategory.add(moFieldNotes);

        mvFieldsCustomerConfig.add(moFieldFkCustomerTypeId);
        mvFieldsCustomerConfig.add(moFieldFkMarketSegmentId);
        mvFieldsCustomerConfig.add(moFieldFkMarketSubsegmentId);
        mvFieldsCustomerConfig.add(moFieldFkDistributionChannelId);
        mvFieldsCustomerConfig.add(moFieldFkSalesRouteId);
        mvFieldsCustomerConfig.add(moFieldFkSalesAgentId_n);
        mvFieldsCustomerConfig.add(moFieldFkSalesSupervisorId_n);
        mvFieldsCustomerConfig.add(moFieldIsFreeDiscountDoc);
        mvFieldsCustomerConfig.add(moFieldIsFreeCommissions);
        mvFieldsCustomerConfig.add(moFieldIsSignRestricted);
        mvFieldsCustomerConfig.add(moFieldIsSignImmex);
        mvFieldsCustomerConfig.add(moFieldIsSkipSignForeignCurrencyRestriction);

        moFormBizPartnerBranch = new SFormBizPartnerBranch(miClient);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbAddBranch.addActionListener(this);
        jbModifyBranch.addActionListener(this);
        jbRecreateBizPartnerCommercial.addActionListener(this);

        jbSetDateStart.addActionListener(this);
        jbSetDateEnd_n.addActionListener(this);
        jbCancel.addActionListener(this);
        jbFkLanguageId_n.addActionListener(this);
        jbFkCurrencyId_n.addActionListener(this);
        jbEditLanguage.addActionListener(this);
        jbEditCurrency.addActionListener(this);
        jbEditCfdiPaymentWay.addActionListener(this);
        jbEditCfdiCfdiUsage.addActionListener(this);
        jbFkBizPartnerTypeId.addActionListener(this);

        jbFkCustomerTypeId.addActionListener(this);
        jbFkSalesRouteId.addActionListener(this);
        jbFkSalesAgentId_n.addActionListener(this);
        jbFkSalesSupervisorId_n.addActionListener(this);
        jbFkMarketSegmentId.addActionListener(this);
        jbFkMarketSubsegmentId.addActionListener(this);
        jbFkDistributionChannelId.addActionListener(this);
        jbAddCusBranchConfig.addActionListener(this);
        jbModifyCusBranchConfig.addActionListener(this);
        jbDeleteCusBranchConfig.addActionListener(this);

        jbSupplier.addActionListener(this);
        jbCustomer.addActionListener(this);
        jbCreditor.addActionListener(this);
        jbDebtor.addActionListener(this);
        
        jcbDiotOperation.removeAllItems();
        jcbDiotOperation.addItem(new SFormComponentItem("", "(Seleccionar tipo operación DIOT)"));
        jcbDiotOperation.addItem(new SFormComponentItem(SDiotConsts.OPER_GOODS, SDiotConsts.Operations.get(SDiotConsts.OPER_GOODS))); // since DIOT 2025
        jcbDiotOperation.addItem(new SFormComponentItem(SDiotConsts.OPER_SERVS, SDiotConsts.Operations.get(SDiotConsts.OPER_SERVS)));
        jcbDiotOperation.addItem(new SFormComponentItem(SDiotConsts.OPER_RENTS, SDiotConsts.Operations.get(SDiotConsts.OPER_RENTS)));
        jcbDiotOperation.addItem(new SFormComponentItem(SDiotConsts.OPER_IMP_INT, SDiotConsts.Operations.get(SDiotConsts.OPER_IMP_INT))); // since DIOT 2025
        jcbDiotOperation.addItem(new SFormComponentItem(SDiotConsts.OPER_IMP_NAC, SDiotConsts.Operations.get(SDiotConsts.OPER_IMP_NAC))); // since DIOT 2025
        jcbDiotOperation.addItem(new SFormComponentItem(SDiotConsts.OPER_OTHER, SDiotConsts.Operations.get(SDiotConsts.OPER_OTHER)));

        int i = 0;
        tableColumnsBizPartnerBranch = new STableColumnForm[10];
        tableColumnsBizPartnerBranch[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Sucursal asociado", 150);
        tableColumnsBizPartnerBranch[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo sucursal", 100);
        tableColumnsBizPartnerBranch[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Región impuestos", 100);
        tableColumnsBizPartnerBranch[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Eliminado", STableConstants.WIDTH_BOOLEAN);
        tableColumnsBizPartnerBranch[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. creación", STableConstants.WIDTH_USER);
        tableColumnsBizPartnerBranch[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Creación", STableConstants.WIDTH_DATE_TIME);
        tableColumnsBizPartnerBranch[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. modificación", STableConstants.WIDTH_USER);
        tableColumnsBizPartnerBranch[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Modificación", STableConstants.WIDTH_DATE_TIME);
        tableColumnsBizPartnerBranch[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. eliminación", STableConstants.WIDTH_USER);
        tableColumnsBizPartnerBranch[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Eliminación", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < tableColumnsBizPartnerBranch.length; i++) {
            moBizPartnerBranchPane.addTableColumn(tableColumnsBizPartnerBranch[i]);
        }

        SFormUtilities.createActionMap(rootPane, this, "publicActionAddBranch", "add", KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionModifyBranch", "modify", KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);

        i = 0;
        tableColumnsCob = new STableColumnForm[11];
        tableColumnsCob[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Sucursal empresa", 150);
        tableColumnsCob[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ruta de ventas", 200);
        tableColumnsCob[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Agente de ventas", 200);
        tableColumnsCob[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Supervisor de ventas", 200);
        tableColumnsCob[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Eliminado", STableConstants.WIDTH_BOOLEAN);
        tableColumnsCob[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. creación", STableConstants.WIDTH_USER);
        tableColumnsCob[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Creación", STableConstants.WIDTH_DATE_TIME);
        tableColumnsCob[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. modificación", STableConstants.WIDTH_USER);
        tableColumnsCob[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Modificación", STableConstants.WIDTH_DATE_TIME);
        tableColumnsCob[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. eliminación", STableConstants.WIDTH_USER);
        tableColumnsCob[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Eliminación", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < tableColumnsCob.length; i++) {
            moCusBranchConfigPane.addTableColumn(tableColumnsCob[i]);
        }
        moCusBranchConfigPane.createTable(null);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), action, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivate() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jcbFkBizPartnerIdentityTypeId.requestFocusInWindow();
        }
    }
    
    private boolean isCompany() {
        return mnParamBizPartnerCategory == SDataConstantsSys.BPSS_CT_BP_CO;
    }

    private boolean isSupplier() {
        return mnParamBizPartnerCategory == SDataConstantsSys.BPSS_CT_BP_SUP;
    }

    private boolean isCustomer() {
        return mnParamBizPartnerCategory == SDataConstantsSys.BPSS_CT_BP_CUS;
    }

    private boolean isCustomerOrSupplier() {
        return isSupplier() || isCustomer();
    }
    
    private boolean applyOrgNamesPolicy() {
        return isCompany() || isCustomer();
    }
    
    private boolean enableOrgNamesPolicyControls() {
        return applyOrgNamesPolicy() && mnCfgParamCfdOrgNames == SDataConstantsSys.CFG_PARAM_CFD_ORG_NAMES_RECEPTOR_CHOICE;
    }
    
    private String composeBizPartnerName() {
        String name = "";

        if (jcbFkBizPartnerIdentityTypeId.getSelectedIndex() > 0) {
            switch (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0]) {
                case SDataConstantsSys.BPSS_TP_BP_IDY_PER:
                    name = moFieldLastName.getString() + (moFieldLastName.getString().length() == 0 ? "" : ", ") + moFieldFirstName.getString();
                    break;
                case SDataConstantsSys.BPSS_TP_BP_IDY_ORG:
                    name = moFieldBizPartner.getString();
                    break;
                default:
                    // do nothing
            }
        }

        return name;
    }

    private void renderBizPartnerIdentityType() {
        if (jcbFkBizPartnerIdentityTypeId.getSelectedIndex() <= 0) {
            // business partner name settings:
            
            jtfFirstName.setEnabled(false);
            jtfLastName.setEnabled(false);
            jftAlternativeId.setEnabled(false);
            jtfBizPartner.setEnabled(false);
            
            jrbOrgNamesPolicyFullName.setEnabled(false);
            jrbOrgNamesPolicyFiscalName.setEnabled(false);
            bgOrgNamesPolicy.clearSelection();
            
            jtfBizPartnerFiscal.setEnabled(false);
            jtfBizPartnerCapitalRegime.setEnabled(false);
            
            moFieldAlternativeId.setMaskFormatter("");
            
            // tax identity:
            
            jcbFkTaxIdentityTypeId.setEnabled(false);
            
            jcbFkTaxIdentityTypeId.removeAllItems();
        }
        else {
            // business partner name settings:
            
            if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER) {
                jtfFirstName.setEnabled(true);
                jtfLastName.setEnabled(true);
                jftAlternativeId.setEnabled(true);
                jtfBizPartner.setEnabled(false);
            
                jrbOrgNamesPolicyFullName.setEnabled(false);
                jrbOrgNamesPolicyFiscalName.setEnabled(false);
                bgOrgNamesPolicy.clearSelection();
                
                jtfBizPartnerFiscal.setEnabled(false);
                jtfBizPartnerCapitalRegime.setEnabled(false);

                moFieldAlternativeId.setMaskFormatter("UUUU######XXXXXXXX");
            }
            else {
                jtfFirstName.setEnabled(false);
                jtfLastName.setEnabled(false);
                jftAlternativeId.setEnabled(false);
                jtfBizPartner.setEnabled(true);
                
                boolean applyPolicy = applyOrgNamesPolicy();
                boolean enablePolicyControls = enableOrgNamesPolicyControls();
                
                jrbOrgNamesPolicyFullName.setEnabled(enablePolicyControls);
                jrbOrgNamesPolicyFiscalName.setEnabled(enablePolicyControls);
                bgOrgNamesPolicy.clearSelection();
                
                if (applyPolicy) {
                    if (mnCfgParamCfdOrgNames == SDataConstantsSys.CFG_PARAM_CFD_ORG_NAMES_ALL_FISCAL_NAME) {
                        jrbOrgNamesPolicyFiscalName.setSelected(true);
                    }
                    else {
                        jrbOrgNamesPolicyFullName.setSelected(true);
                    }
                }
                
                jtfBizPartnerFiscal.setEnabled(applyPolicy);
                jtfBizPartnerCapitalRegime.setEnabled(applyPolicy);

                moFieldAlternativeId.setMaskFormatter("");
            }
            
            // tax identity:
            
            jcbFkTaxIdentityTypeId.setEnabled(true);
            
            SFormUtilities.populateComboBox(miClient, jcbFkTaxIdentityTypeId, SDataConstants.FINU_TAX_IDY, moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray());
            if (jcbFkTaxIdentityTypeId.getItemCount() <= 2) {
                jcbFkTaxIdentityTypeId.setSelectedIndex(jcbFkTaxIdentityTypeId.getItemCount() - 1);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void renderBizPartnerCategory() {
        if (mnParamBizPartnerTypeX == SDataConstants.BPSU_BP) {
            // do nothing
        }
        else if (mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_CO) {
            setFieldEnableCategory(false);
            jbSupplier.setEnabled(true);
            jbCustomer.setEnabled(true);
            jbCreditor.setEnabled(true);
            jbDebtor.setEnabled(true);
            
            jTabbedPane1.setEnabledAt(TAB_MKT, false);
        }
        else if (mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_SUP) {
            setFieldEnableCategory(true);
            jbSupplier.setEnabled(false);
            jbCustomer.setEnabled(true);
            jbCreditor.setEnabled(true);
            jbDebtor.setEnabled(true);
            
            jTabbedPane1.setEnabledAt(TAB_MKT, false);
        }
        else if (mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_CUS) {
            setFieldEnableCategory(true);
            jbSupplier.setEnabled(true);
            jbCustomer.setEnabled(false);
            jbCreditor.setEnabled(true);
            jbDebtor.setEnabled(true);
            
            jTabbedPane1.setEnabledAt(TAB_MKT, true);
        }
        
        SFormUtilities.populateComboBox(miClient, jcbFkBizPartnerCategoryId, SDataConstants.BPSS_CT_BP);
        
        if (mnParamBizPartnerTypeX == SDataConstants.BPSU_BP) {
            moFieldFkBizPartnerCategoryId.setFieldValue(new int[] { SDataConstants.BPSX_BPB_CON_SUP });
        }
        else {
            moFieldFkBizPartnerCategoryId.setKey(new int [] { mnParamBizPartnerCategory });
        }
        
        jlIconSupplier.setIcon(moBizPartner == null ? mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_SUP ? moIconHasCategorySup : moIconHasNotCategory : moBizPartner.getIsSupplier() || mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_SUP ? moIconHasCategorySup : moIconHasNotCategory);
        jlIconCustomer.setIcon(moBizPartner == null ? mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_CUS ? moIconHasCategoryCus : moIconHasNotCategory : moBizPartner.getIsCustomer() || mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_CUS ? moIconHasCategoryCus : moIconHasNotCategory);
        jlIconCreditor.setIcon(moBizPartner == null ? mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_CDR ? moIconHasCategoryCdr : moIconHasNotCategory : moBizPartner.getIsCreditor() || mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_CDR ? moIconHasCategoryCdr : moIconHasNotCategory);
        jlIconDebtor.setIcon(moBizPartner == null ? mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_DBR ? moIconHasCategoryDbr : moIconHasNotCategory : moBizPartner.getIsDebtor() || mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_DBR ? moIconHasCategoryDbr : moIconHasNotCategory);
    }

    private void renderBizPartnerType() {
        moBizPartnerType = null;
        
        if (jcbFkBizPartnerTypeId.getSelectedIndex() > 0) {
            moBizPartnerType = new SDataBizPartnerType();
            moBizPartnerType.read(((SFormComponentItem) jcbFkBizPartnerTypeId.getSelectedItem()).getPrimaryKey(), miClient.getSession().getStatement());
        }
        
        if (moBizPartnerType == null) {
            // clear all credit settings:
            moFieldFkCreditTypeId_n.setFieldValue(new int[] { SModSysConsts.BPSS_TP_CRED_CRED_NO });
            moFieldCreditLimit.setFieldValue(0d);
            moFieldDaysOfCredit.setFieldValue(0);
            moFieldDaysOfGrace.setFieldValue(0);
            moFieldFkRiskTypeId.setFieldValue(new int[] { SModSysConsts.BPSS_RISK_C_RISK_HIGH });
            moFieldGuaranteeAmount.setFieldValue(0d);
            moFieldInsuranceAmount.setFieldValue(0d);
            moFieldIsGuaranteeInProcess.setFieldValue(false);
            moFieldIsInsuranceInProcess.setFieldValue(false);
            
            bgGuaranteeType.clearSelection();
        }
        else {
            if (!jckIsCreditByUser.isSelected()) {
                // credit settings of business partner type:
                moFieldFkCreditTypeId_n.setFieldValue(new int[] { moBizPartnerType.getFkCreditTypeId() });
                moFieldCreditLimit.setFieldValue(moBizPartnerType.getCreditLimit());
                moFieldDaysOfCredit.setFieldValue(moBizPartnerType.getDaysOfCredit());
                moFieldDaysOfGrace.setFieldValue( moBizPartnerType.getDaysOfGrace());
                moFieldFkRiskTypeId.setFieldValue(new int[] { moBizPartnerType.getFkRiskTypeId()});
                //moFieldGuaranteeAmount.setFieldValue(...);
                //moFieldInsuranceAmount.setFieldValue(...);
                //moFieldIsInGuarProcess.setFieldValue(...);
                //moFieldIsInInsurProcess.setFieldValue(...);
            }
            else {
                // credit settings of category from current business partner:
                moFieldFkCreditTypeId_n.setFieldValue(new int[] { moBizPartnerCategory.getFkCreditTypeId_n() });
                moFieldCreditLimit.setFieldValue(moBizPartnerCategory.getCreditLimit());
                moFieldDaysOfCredit.setFieldValue(moBizPartnerCategory.getDaysOfCredit());
                moFieldDaysOfGrace.setFieldValue( moBizPartnerCategory.getDaysOfGrace());
                moFieldFkRiskTypeId.setFieldValue(new int[] { moBizPartnerCategory.getFkRiskId_n()});
                moFieldGuaranteeAmount.setFieldValue(moBizPartnerCategory.getGuarantee());
                moFieldInsuranceAmount.setFieldValue(moBizPartnerCategory.getInsurance());
                moFieldIsGuaranteeInProcess.setFieldValue(moBizPartnerCategory.getIsGuaranteeInProcess());
                moFieldIsInsuranceInProcess.setFieldValue(moBizPartnerCategory.getIsInsuranceInProcess());
                
                switch (moBizPartnerCategory.getGuaranteeType()) {
                    case SDataBizPartnerCategory.GARNT_TP_PAY:
                        jrbGuaranteeTypePay.setSelected(true);
                        break;
                    case SDataBizPartnerCategory.GARNT_TP_PROP:
                        jrbGuaranteeTypeProp.setSelected(true);
                        break;
                    case SDataBizPartnerCategory.GARNT_TP_PAY_PROP:
                        jrbGuaranteeTypePayProp.setSelected(true);
                        break;
                    default:
                        bgGuaranteeType.clearSelection();
                }
            }
        }
    }

    private void setFieldEnableCategory(boolean enable) {
        jlFkBizPartnerCategoryId.setEnabled(enable);
        jcbFkBizPartnerTypeId.setEnabled(enable);
        jlFkBizPartnerTypeId.setEnabled(enable);
        jbFkBizPartnerTypeId.setEnabled(enable);
        jlCompanyKey.setEnabled(enable);
        jtfCompanyKey.setEnabled(enable);
        jlKey.setEnabled(enable);
        jftKey.setEnabled(enable);
        jlDiotOperation.setEnabled(enable && mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_SUP);
        jcbDiotOperation.setEnabled(enable && mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_SUP);
        jlFkLanguageId_n.setEnabled(enable);
        jcbFkLanguageId_n.setEnabled(enable);
        jbFkLanguageId_n.setEnabled(enable);
        jbEditLanguage.setEnabled(enable);
        jlFkCurrencyId_n.setEnabled(enable);
        jcbFkCurrencyId_n.setEnabled(enable);
        jbFkCurrencyId_n.setEnabled(enable);
        jbEditCurrency.setEnabled(enable);
        jlDateStart.setEnabled(enable);
        jftDateStart.setEnabled(enable);
        jbSetDateStart.setEnabled(enable);
        jlDateEnd_n.setEnabled(enable);
        jftDateEnd_n.setEnabled(enable);
        jbSetDateEnd_n.setEnabled(enable);
        jlLeadTime.setEnabled(enable && mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_SUP);
        jtfLeadTime.setEnabled(enable && mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_SUP);
        jlLeadTimeDays.setEnabled(enable && mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_SUP);
        jlLeadTimeHint.setEnabled(enable && mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_SUP);
        jlWeb.setEnabled(enable);
        jtfWeb.setEnabled(enable);
        jlNotes.setEnabled(enable);
        jtfNotes.setEnabled(enable);
        
        jckIsCreditByUser.setEnabled(enable && mbCanEditCredit);
    }
    
    private void updateSettingsBizPartnerKey() {
        switch (mnParamBizPartnerCategory) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
                jftKey.setEnabled(miClient.getSessionXXX().getParamsErp().getIsKeySupplierApplying());
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
                jftKey.setEnabled(miClient.getSessionXXX().getParamsErp().getIsKeyCustomerApplying());
                break;
            default:
                jftKey.setEnabled(false);
        }
    }

    private void updateSettingsBizPartnerCredit() {
        if (!jckIsCreditByUser.isSelected() || jcbFkCreditTypeId_n.getSelectedIndex() <= 0 || moFieldFkCreditTypeId_n.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_CRED_CRED_NO) {
            jtfCreditLimit.setEditable(false);
            jtfCreditLimit.setFocusable(false);
            jtfDaysOfCredit.setEditable(false);
            jtfDaysOfCredit.setFocusable(false);
            jtfDaysOfGrace.setEditable(false);
            jtfDaysOfGrace.setFocusable(false);
            
            if (jckIsCreditByUser.isSelected()) {
                 moFieldCreditLimit.setDouble(0d);
                 moFieldDaysOfCredit.setInteger(0);
                 moFieldDaysOfGrace.setInteger(0);
            }
        }
        else if (moFieldFkCreditTypeId_n.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_CRED_CRED_UNLIM) {
            jtfCreditLimit.setEditable(false);
            jtfCreditLimit.setFocusable(false);
            jtfDaysOfCredit.setEditable(mbCanEditCredit);
            jtfDaysOfCredit.setFocusable(mbCanEditCredit);
            jtfDaysOfGrace.setEditable(mbCanEditCredit);
            jtfDaysOfGrace.setFocusable(mbCanEditCredit);
            
            moFieldCreditLimit.setDouble(0d);
        }
        else if (moFieldFkCreditTypeId_n.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_CRED_CRED_LIM) {
            jtfCreditLimit.setEditable(mbCanEditCredit);
            jtfCreditLimit.setFocusable(mbCanEditCredit);
            jtfDaysOfCredit.setEditable(mbCanEditCredit);
            jtfDaysOfCredit.setFocusable(mbCanEditCredit);
            jtfDaysOfGrace.setEditable(mbCanEditCredit);
            jtfDaysOfGrace.setFocusable(mbCanEditCredit);
        }
    }

    private void updateSettingsBizPartnerCategory() {
        boolean isSupplier = isSupplier();
        boolean isCustomer = isCustomer();
        
        if (isSupplier || isCustomer) {
            jlNumberExporter.setEnabled(false);
            jtfNumberExporter.setEnabled(false);
            jckIsCreditByUser.setEnabled(mbCanEditCredit);
            jlFkCreditTypeId_n.setEnabled(true);
            jcbFkCreditTypeId_n.setEnabled(mbCanEditCredit);
            jlCreditLimit.setEnabled(true);
            jlDaysOfCredit.setEnabled(true);
            jlDaysOfGrace.setEnabled(true);
            jlFkRiskId.setEnabled(true);
            
            jlGuarantee.setEnabled(isCustomer);
            jtfGuarantee.setEnabled(isCustomer);
            jckGuaranteeInProcess.setEnabled(isCustomer);
            jlGuaranteeType.setEnabled(isCustomer);
            jrbGuaranteeTypePay.setEnabled(isCustomer);
            jrbGuaranteeTypeProp.setEnabled(isCustomer);
            jrbGuaranteeTypePayProp.setEnabled(isCustomer);
            jlInsurance.setEnabled(isCustomer);
            jtfInsurance.setEnabled(isCustomer);
            jckInsuranceInProcess.setEnabled(isCustomer);
            
            jlTaxRegime.setEnabled(true);
            jcbTaxRegime.setEnabled(true);
            jlFkCfdAddendaTypeId.setEnabled(isCustomer);
            jcbFkCfdAddendaTypeId.setEnabled(isCustomer);
            jlFkUserAnalystId.setEnabled(isCustomer);
            jcbFkUserAnalystId.setEnabled(isCustomer);
            jlDiotOperation.setEnabled(isSupplier);
            jcbDiotOperation.setEnabled(isSupplier);
            jlFkLanguageId_n.setEnabled(true);
            jcbFkLanguageId_n.setEnabled(true);
            jbFkLanguageId_n.setEnabled(true);
            jbAddBranch.setEnabled(true);
        }
        else {
            jlNumberExporter.setEnabled(mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_CO);
            jtfNumberExporter.setEnabled(mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_CO);
            jckIsCreditByUser.setEnabled(false);
            jlFkCreditTypeId_n.setEnabled(false);
            jcbFkCreditTypeId_n.setEnabled(false);
            jlCreditLimit.setEnabled(false);
            jlDaysOfCredit.setEnabled(false);
            jlDaysOfGrace.setEnabled(false);
            jlFkRiskId.setEnabled(false);
            
            jlGuarantee.setEnabled(false);
            jtfGuarantee.setEnabled(false);
            jckGuaranteeInProcess.setEnabled(false);
            jlGuaranteeType.setEnabled(false);
            jrbGuaranteeTypePay.setEnabled(false);
            jrbGuaranteeTypeProp.setEnabled(false);
            jrbGuaranteeTypePayProp.setEnabled(false);
            jlInsurance.setEnabled(false);
            jtfInsurance.setEnabled(false);
            jckInsuranceInProcess.setEnabled(false);
            
            jlCfdiPaymentWay.setEnabled(false);
            jcbCfdiPaymentWay.setEnabled(false);
            jlCfdiCfdiUsage.setEnabled(false);
            jcbCfdiCfdiUsage.setEnabled(false);
            jlTaxRegime.setEnabled(false);
            jcbTaxRegime.setEnabled(false);
            jlFkCfdAddendaTypeId.setEnabled(false);
            jcbFkCfdAddendaTypeId.setEnabled(false);
            jlFkUserAnalystId.setEnabled(false);
            jcbFkUserAnalystId.setEnabled(false);
            jlDiotOperation.setEnabled(false);
            jcbDiotOperation.setEnabled(false);
            jlFkLanguageId_n.setEnabled(false);
            jcbFkLanguageId_n.setEnabled(false);
            jbFkLanguageId_n.setEnabled(false);
            jbAddBranch.setEnabled(mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_CO);

            moFieldFkCreditTypeId_n.setFieldValue(new int[] { SDataConstantsSys.BPSS_TP_CRED_CRED_NO });
            moFieldFkRiskTypeId.setFieldValue(new int[] { SModSysConsts.BPSS_RISK_C_RISK_HIGH });
            moFieldGuaranteeAmount.setFieldValue(0d);
            moFieldInsuranceAmount.setFieldValue(0d);
            moFieldIsGuaranteeInProcess.setFieldValue(false);
            moFieldIsInsuranceInProcess.setFieldValue(false);
            moFieldCreditLimit.setFieldValue(0d);
            moFieldDaysOfCredit.setFieldValue(0);
            moFieldDaysOfGrace.setFieldValue(0);
            moFieldIsCreditByUser.setFieldValue(false);
        }
        
        updateSettingsBizPartnerCredit();
    }

    private void updateSettingsCustomerConfig() {
         if (isCustomer()) {
            jlFkCustomerTypeId.setEnabled(true);
            jcbFkCustomerTypeId.setEnabled(true);
            jbFkCustomerTypeId.setEnabled(true);
            jlFkMarketSegmentId.setEnabled(true);
            jcbFkMarketSegmentId.setEnabled(true);
            jbFkMarketSegmentId.setEnabled(true);
            jlFkMarketSubsegmentId.setEnabled(true);
            jcbFkMarketSubsegmentId.setEnabled(true);
            jbFkMarketSubsegmentId.setEnabled(true);
            jlFkDistributionChannelId.setEnabled(true);
            jcbFkDistributionChannelId.setEnabled(true);
            jbFkDistributionChannelId.setEnabled(true);
            jlFkSalesRouteId.setEnabled(true);
            jcbFkSalesRouteId.setEnabled(true);
            jbFkSalesRouteId.setEnabled(true);
            jlFkSalesAgentId_n.setEnabled(true);
            jcbFkSalesAgentId_n.setEnabled(true);
            jbFkSalesAgentId_n.setEnabled(true);
            jcbFkSalesSupervisorId_n.setEnabled(true);
            jbFkSalesSupervisorId_n.setEnabled(true);
            jckIsFreeDiscountDoc.setEnabled(true);
            jckIsFreeCommissions.setEnabled(true);
            jckIsSignRestricted.setEnabled(true);
            jckIsSignImmex.setEnabled(true);
            jckIsSkipSignForeignCurrencyRestriction.setEnabled(true);
            jlFkCfdAddendaTypeId.setEnabled(true);
            jcbFkCfdAddendaTypeId.setEnabled(true);
            jbModifyCusBranchConfig.setEnabled(true);
            jbDeleteCusBranchConfig.setEnabled(true);
            jbAddCusBranchConfig.setEnabled(true);
         }
         else {
            jlFkCustomerTypeId.setEnabled(false);
            jcbFkCustomerTypeId.setEnabled(false);
            jbFkCustomerTypeId.setEnabled(false);
            jlFkMarketSegmentId.setEnabled(false);
            jcbFkMarketSegmentId.setEnabled(false);
            jbFkMarketSegmentId.setEnabled(false);
            jlFkMarketSubsegmentId.setEnabled(false);
            jcbFkMarketSubsegmentId.setEnabled(false);
            jbFkMarketSubsegmentId.setEnabled(false);
            jlFkDistributionChannelId.setEnabled(false);
            jcbFkDistributionChannelId.setEnabled(false);
            jbFkDistributionChannelId.setEnabled(false);
            jlFkSalesRouteId.setEnabled(false);
            jcbFkSalesRouteId.setEnabled(false);
            jbFkSalesRouteId.setEnabled(false);
            jlFkSalesAgentId_n.setEnabled(false);
            jcbFkSalesAgentId_n.setEnabled(false);
            jbFkSalesAgentId_n.setEnabled(false);
            jcbFkSalesSupervisorId_n.setEnabled(false);
            jbFkSalesSupervisorId_n.setEnabled(false);
            jckIsFreeDiscountDoc.setEnabled(false);
            jckIsFreeCommissions.setEnabled(false);
            jckIsSignRestricted.setEnabled(false);
            jckIsSignImmex.setEnabled(false);
            jckIsSkipSignForeignCurrencyRestriction.setEnabled(false);
            jlFkCfdAddendaTypeId.setEnabled(false);
            jcbFkCfdAddendaTypeId.setEnabled(false);
            jbModifyCusBranchConfig.setEnabled(false);
            jbDeleteCusBranchConfig.setEnabled(false);
            jbAddCusBranchConfig.setEnabled(false);
         }
     }

    private void renderAdditionalInformation() {
        if (moBizPartnerCategory.getFkBizPartnerCategoryId() == SDataConstantsSys.BPSS_CT_BP_CDR || moBizPartnerCategory.getFkBizPartnerCategoryId() == SDataConstantsSys.BPSS_CT_BP_DBR) {
            jbEditLanguage.setEnabled(true);
            jcbFkLanguageId_n.setEnabled(false);
            jbFkLanguageId_n.setEnabled(false);
            jckIsCreditByUser.setEnabled(false);
        }
        else {
            jbEditLanguage.setEnabled(false);
            jcbFkLanguageId_n.setEnabled(true);
            jbFkLanguageId_n.setEnabled(true);
            jckIsCreditByUser.setEnabled(mbCanEditCredit);
        }
    }

    private void renderDaysOfGrace() {
        if (isSupplier()) {
            if (miClient.getSessionXXX().getParamsErp().getSupplierDaysOfGrace() == 0) {
                jtfDaysOfGrace.setEditable(false);
            }
        }
        else if (isCustomer()) {
            if (miClient.getSessionXXX().getParamsErp().getCustomerDaysOfGrace() == 0) {
                jtfDaysOfGrace.setEditable(false);
            }
        }
    }

    private java.lang.String readLanguageKey(int pk) {
        SDataLanguage lang = (SDataLanguage) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_LAN, new int[] { pk }, SLibConstants.EXEC_MODE_SILENT);
        return lang.getKey();
    }

    private java.lang.String readCurrencyKey(int pk) {
        SDataCurrency cur = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR, new int[] { pk }, SLibConstants.EXEC_MODE_SILENT);
        return cur.getKey();
    }

    private void setLanguageEnabled(boolean enable) {
        jbEditLanguage.setEnabled(!enable);
        jcbFkLanguageId_n.setEnabled(enable);
        jbFkLanguageId_n.setEnabled(enable);
        jcbFkLanguageId_n.requestFocus();
    }

    private void setCurrencyEnabled(boolean enable) {
        jbEditCurrency.setEnabled(!enable);
        jcbFkCurrencyId_n.setEnabled(enable);
        jbFkCurrencyId_n.setEnabled(enable);
        jcbFkCurrencyId_n.requestFocus();
    }
    
    private void setCfdiPaymentWayEnabled(boolean enable) {
        jbEditCfdiPaymentWay.setEnabled(!enable);
        jcbCfdiPaymentWay.setEnabled(enable);
        jcbCfdiPaymentWay.requestFocus();
    }
    
    private void setCfdiUsageEnabled(boolean enable) {
        jbEditCfdiCfdiUsage.setEnabled(!enable);
        jcbCfdiCfdiUsage.setEnabled(enable);
        jcbCfdiCfdiUsage.requestFocus();
    }

    private void actionOk() {
        SFormValidation validation = formValidate();

        if (validation.getIsError()) {
            if (validation.getTabbedPaneIndex() >= 0) {
                jTabbedPane1.setSelectedIndex(validation.getTabbedPaneIndex());
            }
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocusInWindow();
            }
            if (!validation.getMessage().isEmpty()) {
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

    private void actionAddBranch() {
        if (jbAddBranch.isEnabled()) {
            moFormBizPartnerBranch.setParamBizPartnerDescription(composeBizPartnerName());
            moFormBizPartnerBranch.formRefreshCatalogues();
            moFormBizPartnerBranch.formReset();
            moFormBizPartnerBranch.setVisible(true);

            if (moFormBizPartnerBranch.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                SDataBizPartnerBranch branch = (SDataBizPartnerBranch) moFormBizPartnerBranch.getRegistry();
                branch.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                branch.setFkUserEditId(SUtilConsts.USR_NA_ID);
                branch.setFkUserDeleteId(SUtilConsts.USR_NA_ID);
                branch.setDbmsUserNew(miClient.getSessionXXX().getUser().getUser());
                branch.setDbmsUserEdit("");
                branch.setDbmsUserDelete("");
                branch.setUserNewTs(new Date());
                branch.setUserEditTs(null);
                branch.setUserDeleteTs(null);

                moBizPartnerBranchPane.addTableRow(new SDataBizPartnerBranchRow(branch));
                moBizPartnerBranchPane.renderTableRows();
                moBizPartnerBranchPane.setTableRowSelection(moBizPartnerBranchPane.getTableGuiRowCount() - 1);
            }
        }
    }

    private void actionModifyBranch() {
        if (jbModifyBranch.isEnabled()) {
            int index = moBizPartnerBranchPane.getTable().getSelectedRow();

            if (index != -1) {
                SDataBizPartnerBranch branch = (SDataBizPartnerBranch) moBizPartnerBranchPane.getTableRow(index).getData();
                
                moFormBizPartnerBranch.setParamBizPartnerDescription(composeBizPartnerName());
                moFormBizPartnerBranch.formRefreshCatalogues();
                moFormBizPartnerBranch.formReset();
                moFormBizPartnerBranch.setRegistry(branch);
                moFormBizPartnerBranch.setVisible(true);

                if (moFormBizPartnerBranch.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                    branch = (erp.mbps.data.SDataBizPartnerBranch) moFormBizPartnerBranch.getRegistry();
                    branch.setFkUserEditId(miClient.getSessionXXX().getUser().getPkUserId());
                    branch.setDbmsUserEdit(miClient.getSessionXXX().getUser().getUser()); 
                    branch.setUserEditTs(new Date());

                    moBizPartnerBranchPane.setTableRow(new SDataBizPartnerBranchRow(branch), index);
                    ((STableModel) moBizPartnerBranchPane.getTable().getModel()).getTableRow(index).setStyle(branch.getIsDeleted() ? STableConstants.STYLE_DELETE : 0);
                    
                    moBizPartnerBranchPane.renderTableRows();
                    moBizPartnerBranchPane.setTableRowSelection(index);
                }
            }
        }
    }

    private void actionRecreateBizPartnerCommercial() {
        jtfBizPartnerCommercial.setText(composeBizPartnerName());
        jtfBizPartnerCommercial.setCaretPosition(0);
        jtfBizPartnerCommercial.requestFocusInWindow();
    }

    private void actionSetDateStart() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldDateStart.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldDateStart.setFieldValue(miClient.getGuiDatePickerXXX().getGuiDate());
            jftDateStart.requestFocusInWindow();
        }
    }

    private void actionSetDateEnd_n() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldDateEnd.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldDateEnd.setFieldValue(miClient.getGuiDatePickerXXX().getGuiDate());
            jftDateEnd_n.requestFocusInWindow();
        }
    }

    private void actionFkLanguageId() {
        miClient.pickOption(SDataConstants.CFGU_LAN, moFieldFkLanguageId_n, null);
    }

    private void actionFkCurrencyId() {
        miClient.pickOption(SDataConstants.CFGU_CUR, moFieldFkCurrencyId_n, null);
    }

    private void actionEditLanguage() {
        setLanguageEnabled(true);
    }

    private void actionEditCurrency() {
        setCurrencyEnabled(true);
    }
    
    private void actionEditCfdiPaymentWay() {
        setCfdiPaymentWayEnabled(true);
    }
    
    private void actionEditCfdiUsage() {
        setCfdiUsageEnabled(true);
    }

    private void actionFkBizPartnerTypeId() {
        miClient.pickOption(SDataConstants.BPSU_TP_BP, moFieldFkBizPartnerTypeId, moFieldFkBizPartnerCategoryId.getKeyAsIntArray());
    }

    private void actionFkCustomerTypeId() {
        miClient.pickOption(SDataConstants.MKTU_TP_CUS, moFieldFkCustomerTypeId, null);
    }

    private void actionFkMarketSegmentId() {
        miClient.pickOption(SDataConstants.MKTU_MKT_SEGM, moFieldFkMarketSegmentId, null);
    }

    private void actionFkMarketSubsegmentId() {
        miClient.pickOption(SDataConstants.MKTU_MKT_SEGM_SUB, moFieldFkMarketSubsegmentId, moFieldFkMarketSegmentId.getKey());
    }

    private void actionFkDistributionChannelId() {
        miClient.pickOption(SDataConstants.MKTU_DIST_CHAN, moFieldFkDistributionChannelId, null);
    }

    private void actionFkSalesRouteId() {
        miClient.pickOption(SDataConstants.MKTU_SAL_ROUTE, moFieldFkSalesRouteId, null);
    }

    private void actionFkSalesAgentId_n() {
        miClient.pickOption(SDataConstants.BPSX_BP_ATT_SAL_AGT, moFieldFkSalesAgentId_n, null);
    }

    private void actionFkSalesSupervisorId_n() {
        miClient.pickOption(SDataConstants.BPSX_BP_ATT_SAL_AGT, moFieldFkSalesSupervisorId_n, null);
    }

    private void actionModifyCusBranchConfig() {
        int index = moCusBranchConfigPane.getTable().getSelectedRow();
        
        if (index != -1) {
            SDataCustomerBranchConfig oldCustomerBranchConfig = (SDataCustomerBranchConfig) moCusBranchConfigPane.getTableRow(index).getData();
            
            SFormCustomerBranchConfig form = new SFormCustomerBranchConfig(miClient);
            form.formRefreshCatalogues();
            form.formReset();
            form.setRegistry(oldCustomerBranchConfig);
            form.setVisible(true);
            
            if (form.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                // create new configuration:
                
                SDataCustomerBranchConfig newCustomerBranchConfig = (SDataCustomerBranchConfig) form.getRegistry();
                newCustomerBranchConfig.setFkUserEditId(miClient.getSessionXXX().getUser().getPkUserId());
                newCustomerBranchConfig.setDbmsUserEdit(miClient.getSessionXXX().getUser().getUser()); 
                newCustomerBranchConfig.setUserEditTs(new Date());

                moCusBranchConfigPane.setTableRow(new SDataCustomerBranchConfigRow(newCustomerBranchConfig), index);
                moCusBranchConfigPane.renderTableRows();
                moCusBranchConfigPane.setTableRowSelection(index);
                
                // replace old configuration with new one into corresponding branch:
                
                for (int branchIndex = 0; branchIndex < moBizPartnerBranchPane.getTableGuiRowCount(); branchIndex++) {
                    SDataBizPartnerBranch branch = ((SDataBizPartnerBranch) moBizPartnerBranchPane.getTableRow(branchIndex).getData());
                    
                    if ((branch.getPkBizPartnerBranchId() != 0 && branch.getPkBizPartnerBranchId() == oldCustomerBranchConfig.getPkCustomerBranchId()) || (branch.getDbmsDataCustomerBranchConfig() == oldCustomerBranchConfig)) {
                        branch.setDbmsDataCustomerBranchConfig(newCustomerBranchConfig);
                        break;
                    }
                }
            }
        }
    }

    private void actionDeleteCusBranchConfig() {
        int index = moCusBranchConfigPane.getTable().getSelectedRow();

        if (index != -1) {
            if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                // update existing configuration:
                
                SDataCustomerBranchConfig customerBranchConfig = (SDataCustomerBranchConfig) moCusBranchConfigPane.getTableRow(index).getData();
                
                customerBranchConfig.setIsDeleted(!customerBranchConfig.getIsDeleted());
                customerBranchConfig.setFkUserDeleteId(miClient.getSessionXXX().getUser().getPkUserId());
                customerBranchConfig.setDbmsUserDelete(miClient.getSessionXXX().getUser().getUser()); 
                customerBranchConfig.setUserDeleteTs(new Date());
                
                customerBranchConfig.setIsRegistryEdited(true);

                moCusBranchConfigPane.setTableRow(new SDataCustomerBranchConfigRow(customerBranchConfig), index);
                ((STableModel) moCusBranchConfigPane.getTable().getModel()).getTableRow(index).setStyle(customerBranchConfig.getIsDeleted() ? STableConstants.STYLE_DELETE : 0);
                
                moCusBranchConfigPane.renderTableRows();
                moCusBranchConfigPane.setTableRowSelection(index);
            }
        }
    }

    private void actionAddCusBranchConfig() {
        if (moBizPartnerBranchPane.getTableGuiRowCount() > 0 && moBizPartnerBranchPane.getTableGuiRowCount() != moCusBranchConfigPane.getTableGuiRowCount()) {
            moCusBranchConfigPane.clearTableRows();
            
            for (int index = 0; index < moBizPartnerBranchPane.getTableGuiRowCount(); index++) {
                SDataBizPartnerBranch branch = ((SDataBizPartnerBranch) moBizPartnerBranchPane.getTableRow(index).getData());
                SDataCustomerBranchConfig customerBranchConfig = branch.getDbmsDataCustomerBranchConfig();
                
                if (customerBranchConfig != null) {
                    if (branch.getIsDeleted() && !customerBranchConfig.getIsDeleted()) {
                        customerBranchConfig.setIsDeleted(true); // homogenize deletion status, only if branch is deleted
                        customerBranchConfig.setIsRegistryEdited(true); // allow to save this customer configuration
                    }
                }
                else {
                    customerBranchConfig = new SDataCustomerBranchConfig();
                    customerBranchConfig.setPkCustomerBranchId(branch.getPkBizPartnerBranchId());
                    customerBranchConfig.setIsDeleted(false);
                    customerBranchConfig.setFkSalesRouteId(SDataConstantsSys.MKTU_SAL_ROUTE_DEFAULT);
                    customerBranchConfig.setFkSalesAgentId_n(0);
                    customerBranchConfig.setFkSalesSupervisorId_n(0);
                    customerBranchConfig.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                    customerBranchConfig.setFkUserEditId(SUtilConsts.USR_NA_ID);
                    customerBranchConfig.setFkUserDeleteId(SUtilConsts.USR_NA_ID);
                    customerBranchConfig.setDbmsUserNew(miClient.getSessionXXX().getUser().getUser());
                    customerBranchConfig.setDbmsUserEdit("");
                    customerBranchConfig.setDbmsUserDelete("");
                    customerBranchConfig.setUserNewTs(new Date());
                    customerBranchConfig.setUserEditTs(null);
                    customerBranchConfig.setUserDeleteTs(null);
                    
                    customerBranchConfig.setDbmsCustomerBranch(branch.getBizPartnerBranch());
                    customerBranchConfig.setDbmsSalesRoute("");
                    customerBranchConfig.setDbmsSalesAgent("");
                    customerBranchConfig.setDbmsSalesSupervisor("");
                    
                    branch.setDbmsDataCustomerBranchConfig(customerBranchConfig);
                }
                
                moCusBranchConfigPane.addTableRow(new SDataCustomerBranchConfigRow(customerBranchConfig));
                ((STableModel) moCusBranchConfigPane.getTable().getModel()).getTableRow(index).setStyle(customerBranchConfig.getIsDeleted() ? STableConstants.STYLE_DELETE : 0);
            }
            
            moCusBranchConfigPane.renderTableRows();
            moCusBranchConfigPane.setTableRowSelection(0);
        }
    }

    private void actionSupplier() {
        if (jbSupplier.isEnabled()) {
            mnFormTypeExport = SDataConstants.BPSX_BP_SUP;

            if (moBizPartner != null && moBizPartner.getIsSupplier()) {
                if (miClient.showMsgBoxConfirm("Para abrir la categoría de proveedor para este asociado de negocios necesita guardar los cambios,\n¿Desea continuar?") == JOptionPane.YES_OPTION) {
                    actionOk();
                    mbIsNeededPosSave = true;
                }
            }
            else if (miClient.showMsgBoxConfirm("Para crear la categoría de proveedor para este asociado de negocios necesita guardar los cambios,\n¿Desea continuar?") == JOptionPane.YES_OPTION) {
                actionOk();
                mbIsNeededPosSave = true;
            }
        }
    }

    private void actionCustomer() {
        if (jbCustomer.isEnabled()) {
            mnFormTypeExport = SDataConstants.BPSX_BP_CUS;

            if (moBizPartner != null && moBizPartner.getIsCustomer()) {
                if (miClient.showMsgBoxConfirm("Para abrir la categoría de cliente para este asociado de negocios necesita guardar los cambios,\n¿Desea continuar?") == JOptionPane.YES_OPTION) {
                    actionOk();
                    mbIsNeededPosSave = true;
                }
            }
            else if (miClient.showMsgBoxConfirm("Para crear la categoría de cliente para este asociado de negocios necesita guardar los cambios,\n¿Desea continuar?") == JOptionPane.YES_OPTION) {
                actionOk();
                mbIsNeededPosSave = true;
            }
        }
    }

    private void actionCreditor() {
        if (jbCreditor.isEnabled()) {
            mnFormTypeExport = SDataConstants.BPSX_BP_CDR;

            if (moBizPartner != null && moBizPartner.getIsCreditor()) {
                if (miClient.showMsgBoxConfirm("Para abrir la categoría de acreedor diverso para este asociado de negocios necesita guardar los cambios,\n¿Desea continuar?") == JOptionPane.YES_OPTION) {
                    actionOk();
                    mbIsNeededPosSave = true;
                }
            }
            else if (miClient.showMsgBoxConfirm("Para crear la categoría de acreedor diverso para este asociado de negocios necesita guardar los cambios,\n¿Desea continuar?") == JOptionPane.YES_OPTION) {
                actionOk();
                mbIsNeededPosSave = true;
            }
        }
    }

    private void actionDebtor() {
        if (jbDebtor.isEnabled()) {
            mnFormTypeExport = SDataConstants.BPSX_BP_DBR;

            if (moBizPartner != null && moBizPartner.getIsDebtor()) {
                if (miClient.showMsgBoxConfirm("Para abrir la categoría de deudor diverso para este asociado de negocios necesita guardar los cambios,\n¿Desea continuar?") == JOptionPane.YES_OPTION) {
                    actionOk();
                    mbIsNeededPosSave = true;
                }
            }
            else if (miClient.showMsgBoxConfirm("Para crear la categoría de deudor diverso para este asociado de negocios necesita guardar los cambios,\n¿Desea continuar?") == JOptionPane.YES_OPTION) {
                actionOk();
                mbIsNeededPosSave = true;
            }
        }
    }

    private void focusLostFiscalId() {
        if (!jtfFiscalId.getText().trim().isEmpty()) {
            jtfFiscalId.setText(jtfFiscalId.getText().toUpperCase());
        }
        else {
            jtfFiscalId.setText("");
        }
    }

    private void focusLostAlternativeId() {
        if (!jftAlternativeId.getText().trim().isEmpty()) {
            jftAlternativeId.setText(jftAlternativeId.getText().toUpperCase());
        }
        else {
            jftAlternativeId.setText("");
        }
    }

    private void itemStateChangedBizPartnerIdentityType() {
        renderBizPartnerIdentityType();
        updateSettingsBizPartnerKey();
    }
    
    private void itemStateChangedCreditTypeId() {
        updateSettingsBizPartnerCredit();
    }

    private void itemStateChangedIsCreditByUser() {
        boolean enable = jckIsCreditByUser.isSelected() && mbCanEditCredit;
        jcbFkCreditTypeId_n.setEnabled(enable);
        jcbFkRiskId.setEnabled(enable);
        renderBizPartnerType();
        updateSettingsBizPartnerCredit();
    }

    private void itemStateChangedIsAttEmployee() {
        if (jckIsAttEmployee.isEnabled()) {
            mnFormTypeExport = SDataConstants.BPSX_BP_EMP;
            mbIsNeededPosSave = jckIsAttEmployee.isSelected();
        }
    }

    public void publicActionModifyCusBranchConfig() {
        actionModifyCusBranchConfig();
    }

    public void publicActionAddBranch() {
        if (jTabbedPane1.getSelectedIndex() == 1 && jbAddBranch.isEnabled()) {
            actionAddBranch();
        }
    }

    public void publicActionModifyBranch() {
        if (jTabbedPane1.getSelectedIndex() == 1 && jbModifyBranch.isEnabled()) {
            actionModifyBranch();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgGuaranteeType;
    private javax.swing.ButtonGroup bgOrgNamesPolicy;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
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
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jbAddBranch;
    private javax.swing.JButton jbAddCusBranchConfig;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbCreditor;
    private javax.swing.JButton jbCustomer;
    private javax.swing.JButton jbDebtor;
    private javax.swing.JButton jbDeleteCusBranchConfig;
    private javax.swing.JButton jbEditCfdiCfdiUsage;
    private javax.swing.JButton jbEditCfdiPaymentWay;
    private javax.swing.JButton jbEditCurrency;
    private javax.swing.JButton jbEditLanguage;
    private javax.swing.JButton jbFkBizPartnerTypeId;
    private javax.swing.JButton jbFkCurrencyId_n;
    private javax.swing.JButton jbFkCustomerTypeId;
    private javax.swing.JButton jbFkDistributionChannelId;
    private javax.swing.JButton jbFkLanguageId_n;
    private javax.swing.JButton jbFkMarketSegmentId;
    private javax.swing.JButton jbFkMarketSubsegmentId;
    private javax.swing.JButton jbFkSalesAgentId_n;
    private javax.swing.JButton jbFkSalesRouteId;
    private javax.swing.JButton jbFkSalesSupervisorId_n;
    private javax.swing.JButton jbModifyBranch;
    private javax.swing.JButton jbModifyCusBranchConfig;
    private javax.swing.JButton jbNewCusBranchConfig;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbRecreateBizPartnerCommercial;
    private javax.swing.JButton jbSetDateEnd_n;
    private javax.swing.JButton jbSetDateStart;
    private javax.swing.JButton jbSupplier;
    private javax.swing.JComboBox<SFormComponentItem> jcbCfdiCfdiUsage;
    private javax.swing.JComboBox<SFormComponentItem> jcbCfdiPaymentWay;
    private javax.swing.JComboBox<SFormComponentItem> jcbDiotOperation;
    private javax.swing.JComboBox jcbFkBizArea;
    private javax.swing.JComboBox jcbFkBizPartnerCategoryId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkBizPartnerIdentityTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkBizPartnerTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCfdAddendaTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCreditTypeId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCurrencyId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCustomerTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkDistributionChannelId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkLanguageId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkMarketSegmentId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkMarketSubsegmentId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkRiskId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSalesAgentId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSalesRouteId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSalesSupervisorId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkTaxIdentityTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkUserAnalystId;
    private javax.swing.JComboBox<SFormComponentItem> jcbTaxRegime;
    private javax.swing.JCheckBox jckGuaranteeInProcess;
    private javax.swing.JCheckBox jckInsuranceInProcess;
    private javax.swing.JCheckBox jckIsAttBank;
    private javax.swing.JCheckBox jckIsAttCarrier;
    private javax.swing.JCheckBox jckIsAttEmployee;
    private javax.swing.JCheckBox jckIsAttPartnerShareholder;
    private javax.swing.JCheckBox jckIsAttRelatedParty;
    private javax.swing.JCheckBox jckIsAttSalesAgent;
    private javax.swing.JCheckBox jckIsCategoryDeleted;
    private javax.swing.JCheckBox jckIsCreditByUser;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JCheckBox jckIsFreeCommissions;
    private javax.swing.JCheckBox jckIsFreeDiscountDoc;
    private javax.swing.JCheckBox jckIsSignImmex;
    private javax.swing.JCheckBox jckIsSignRestricted;
    private javax.swing.JCheckBox jckIsSkipSignForeignCurrencyRestriction;
    private javax.swing.JFormattedTextField jftAlternativeId;
    private javax.swing.JFormattedTextField jftDateEnd_n;
    private javax.swing.JFormattedTextField jftDateStart;
    private javax.swing.JFormattedTextField jftKey;
    private javax.swing.JLabel jlAlternativeId;
    private javax.swing.JLabel jlBizArea;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlBizPartnerCommercial;
    private javax.swing.JLabel jlBizPartnerFiscal;
    private javax.swing.JLabel jlCfdiCfdiUsage;
    private javax.swing.JLabel jlCfdiPaymentWay;
    private javax.swing.JLabel jlCompanyKey;
    private javax.swing.JLabel jlCompanyKey1;
    private javax.swing.JLabel jlCompanyKey2;
    private javax.swing.JLabel jlCompanyKey3;
    private javax.swing.JLabel jlCreditLimit;
    private javax.swing.JLabel jlDateEnd_n;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlDaysOfCredit;
    private javax.swing.JLabel jlDaysOfGrace;
    private javax.swing.JLabel jlDiotOperation;
    private javax.swing.JLabel jlDummy01;
    private javax.swing.JLabel jlDummy1;
    private javax.swing.JLabel jlDummy2;
    private javax.swing.JLabel jlDummy3;
    private javax.swing.JLabel jlDummy4;
    private javax.swing.JLabel jlDummy5;
    private javax.swing.JLabel jlFirstName;
    private javax.swing.JLabel jlFiscalFrgId;
    private javax.swing.JLabel jlFiscalId;
    private javax.swing.JLabel jlFkBizPartnerCategoryId;
    private javax.swing.JLabel jlFkBizPartnerIdentityTypeId;
    private javax.swing.JLabel jlFkBizPartnerTypeId;
    private javax.swing.JLabel jlFkCfdAddendaTypeId;
    private javax.swing.JLabel jlFkCreditTypeId_n;
    private javax.swing.JLabel jlFkCurrencyId_n;
    private javax.swing.JLabel jlFkCustomerTypeId;
    private javax.swing.JLabel jlFkDistributionChannelId;
    private javax.swing.JLabel jlFkLanguageId_n;
    private javax.swing.JLabel jlFkMarketSegmentId;
    private javax.swing.JLabel jlFkMarketSubsegmentId;
    private javax.swing.JLabel jlFkRiskId;
    private javax.swing.JLabel jlFkSalesAgentId_n;
    private javax.swing.JLabel jlFkSalesRouteId;
    private javax.swing.JLabel jlFkSalesSupervisorId_n;
    private javax.swing.JLabel jlFkTaxIdentityTypeId;
    private javax.swing.JLabel jlFkUserAnalystId;
    private javax.swing.JLabel jlGuarantee;
    private javax.swing.JLabel jlGuaranteeType;
    private javax.swing.JLabel jlIconCreditor;
    private javax.swing.JLabel jlIconCustomer;
    private javax.swing.JLabel jlIconDebtor;
    private javax.swing.JLabel jlIconSupplier;
    private javax.swing.JLabel jlInsurance;
    private javax.swing.JLabel jlIsSignImmexHint;
    private javax.swing.JLabel jlIsSignRestrictedHint;
    private javax.swing.JLabel jlKey;
    private javax.swing.JLabel jlLastName;
    private javax.swing.JLabel jlLeadTime;
    private javax.swing.JLabel jlLeadTimeDays;
    private javax.swing.JLabel jlLeadTimeHint;
    private javax.swing.JLabel jlNotes;
    private javax.swing.JLabel jlNumberExporter;
    private javax.swing.JLabel jlTaxRegime;
    private javax.swing.JLabel jlWeb;
    private javax.swing.JPanel jpAdditional;
    private javax.swing.JPanel jpAdditional1;
    private javax.swing.JPanel jpBizPartner;
    private javax.swing.JPanel jpBizPartner11;
    private javax.swing.JPanel jpBizPartner12;
    private javax.swing.JPanel jpBizPartnerBranches;
    private javax.swing.JPanel jpCusBranchConfig;
    private javax.swing.JPanel jpCusConfig;
    private javax.swing.JPanel jpMarketing;
    private javax.swing.JPanel jpMarketing1;
    private javax.swing.JPanel jpNotesAction;
    private javax.swing.JRadioButton jrbGuaranteeTypePay;
    private javax.swing.JRadioButton jrbGuaranteeTypePayProp;
    private javax.swing.JRadioButton jrbGuaranteeTypeProp;
    private javax.swing.JRadioButton jrbOrgNamesPolicyFiscalName;
    private javax.swing.JRadioButton jrbOrgNamesPolicyFullName;
    private javax.swing.JTextField jtfBizPartner;
    private javax.swing.JTextField jtfBizPartnerCapitalRegime;
    private javax.swing.JTextField jtfBizPartnerCommercial;
    private javax.swing.JTextField jtfBizPartnerFiscal;
    private javax.swing.JTextField jtfCompanyKey;
    private javax.swing.JTextField jtfCreditLimit;
    private javax.swing.JTextField jtfCurrencyKey;
    private javax.swing.JTextField jtfCurrencyKeyGuar;
    private javax.swing.JTextField jtfCurrencyKeyInsur;
    private javax.swing.JTextField jtfDaysOfCredit;
    private javax.swing.JTextField jtfDaysOfGrace;
    private javax.swing.JTextField jtfFirstName;
    private javax.swing.JTextField jtfFiscalFrgId;
    private javax.swing.JTextField jtfFiscalId;
    private javax.swing.JTextField jtfGuarantee;
    private javax.swing.JTextField jtfInsurance;
    private javax.swing.JTextField jtfLastName;
    private javax.swing.JTextField jtfLeadTime;
    private javax.swing.JTextField jtfNotes;
    private javax.swing.JTextField jtfNumberExporter;
    private javax.swing.JTextField jtfPkBizPartnerId_Ro;
    private javax.swing.JTextField jtfWeb;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        moBizPartnerCategory = null;
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        moBizPartner = null;
        moBizPartnerCategory = null;

        mnFormTypeExport = SLibConstants.UNDEFINED;
        mbIsNeededPosSave = false;
        
        msTempPaymentAccount = "";
        mnTempFkPaymentSystemTypeId = SLibConstants.UNDEFINED;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }
        for (int i = 0; i < mvFieldsCategory.size(); i++) {
            ((erp.lib.form.SFormField) mvFieldsCategory.get(i)).resetField();
        }
        for (int i = 0; i < mvFieldsCustomerConfig.size(); i++) {
            ((erp.lib.form.SFormField) mvFieldsCustomerConfig.get(i)).resetField();
        }

        jtfFirstName.setText("");
        jtfLastName.setText("");
        jtfBizPartner.setText("");
        jtfBizPartnerFiscal.setText("");
        jtfBizPartnerCapitalRegime.setText("");
        bgOrgNamesPolicy.clearSelection();
        jtfFiscalId.setText("");
        jtfFiscalFrgId.setText("");
        jftAlternativeId.setText("");
        jtfPkBizPartnerId_Ro.setText("");
        moBizPartnerBranchPane.createTable(null);
        moBizPartnerBranchPane.clearTableRows();
        moPanelBizPartnerBranch.formReset();
        moPanelBizPartnerBranch.setParamIsInMainWindow(true);
        moPanelBizPartnerBranch.setParamFormBizPartner(this);
        moPanelBizPartnerBranch.setParamIsCompany(mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_CO);
        jtfFirstName.setEnabled(false);
        jtfLastName.setEnabled(false);
        jtfBizPartner.setEnabled(false);
        jtfBizPartnerFiscal.setEnabled(false);
        jtfBizPartnerCapitalRegime.setEnabled(false);
        jrbOrgNamesPolicyFullName.setEnabled(false);
        jrbOrgNamesPolicyFiscalName.setEnabled(false);
        jftAlternativeId.setEnabled(false);
        jTabbedPane1.setSelectedIndex(0);
        jckIsAttEmployee.setSelected(false);

        moComboBoxBizPartnerIdentity.reset();
        maoDbmsCategorySettings = new SDataBizPartnerCategory[SDataConstantsSys.BPSX_CT_BP_QTY];

        moFieldDateStart.setFieldValue(SLibTimeUtilities.getBeginOfMonth(miClient.getSessionXXX().getWorkingDate()));
        moFieldFkCfdAddendaTypeId.setFieldValue(new int[] { SDataConstantsSys.BPSS_TP_CFD_ADD_NA });

        jckIsCreditByUser.setSelected(false);
        jtfCurrencyKey.setText(miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
        jtfCurrencyKeyGuar.setText(miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
        jtfCurrencyKeyInsur.setText(miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
        bgGuaranteeType.clearSelection();

        updateSettingsBizPartnerKey();
        renderBizPartnerCategory();

        updateSettingsBizPartnerCategory();
        renderDaysOfGrace();
        updateSettingsCustomerConfig();
        itemStateChangedIsCreditByUser();
        if (mnParamBizPartnerTypeX != SDataConstants.BPSX_BP_CO) {
            setLanguageEnabled(false);
            setCurrencyEnabled(false);
        }
        setCfdiPaymentWayEnabled(false);
        setCfdiUsageEnabled(false);

        moCustomerConfig = null;
        moCustomerBranchConfig = null;
        moCusBranchConfigPane.createTable(null);
        moCusBranchConfigPane.clearTableRows();

        moComboboxGrpMarketSegment.reset();

        jckIsDeleted.setEnabled(false);
        jckIsCategoryDeleted.setEnabled(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbFkBizPartnerIdentityTypeId, SDataConstants.BPSS_TP_BP_IDY);
        
        int bizCategory = 0;

        switch (mnParamBizPartnerCategory) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
                bizCategory = SDataConstants.BPSX_TP_BP_SUP;
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
                bizCategory = SDataConstants.BPSX_TP_BP_CUS;
                break;
            default:
                break;
        }

        SFormUtilities.populateComboBox(miClient, jcbFkBizPartnerTypeId, bizCategory);
        SFormUtilities.populateComboBox(miClient, jcbFkCreditTypeId_n, SDataConstants.BPSS_TP_CRED);
        SFormUtilities.populateComboBox(miClient, jcbFkRiskId, SDataConstants.BPSS_RISK);
        SFormUtilities.populateComboBox(miClient, jcbFkLanguageId_n, SDataConstants.CFGU_LAN);
        SFormUtilities.populateComboBox(miClient, jcbFkCurrencyId_n, SDataConstants.CFGU_CUR);
        SFormUtilities.populateComboBox(miClient, jcbFkUserAnalystId, SDataConstants.USRX_QRY_SAL_AGT);
        SFormUtilities.populateComboBox(miClient, jcbFkCfdAddendaTypeId, SDataConstants.BPSS_TP_CFD_ADD);

        // XML catalogs for CFD:
        
        moXmlCatalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        moXmlCatalogs.populateComboBox(jcbCfdiPaymentWay, SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY, miClient.getSession().getSystemDate());
        moXmlCatalogs.populateComboBox(jcbCfdiCfdiUsage, SDataConstantsSys.TRNS_CFD_CAT_CFD_USE, miClient.getSession().getSystemDate());
        moXmlCatalogs.populateComboBox(jcbTaxRegime, SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, miClient.getSession().getSystemDate());
        
        // marketing settings:
        
        moComboboxGrpMarketSegment.clear();
        moComboboxGrpMarketSegment.addComboBox(SDataConstants.MKTU_MKT_SEGM, jcbFkMarketSegmentId, jbFkMarketSegmentId);
        moComboboxGrpMarketSegment.addComboBox(SDataConstants.MKTU_MKT_SEGM_SUB, jcbFkMarketSubsegmentId, jbFkMarketSubsegmentId);

        SFormUtilities.populateComboBox(miClient, jcbFkCustomerTypeId, SDataConstants.MKTU_TP_CUS);
        SFormUtilities.populateComboBox(miClient, jcbFkDistributionChannelId, SDataConstants.MKTU_DIST_CHAN);
        SFormUtilities.populateComboBox(miClient, jcbFkSalesRouteId, SDataConstants.MKTU_SAL_ROUTE);
        SFormUtilities.populateComboBox(miClient, jcbFkSalesAgentId_n, SDataConstants.BPSX_BP_ATT_SAL_AGT);
        SFormUtilities.populateComboBox(miClient, jcbFkSalesSupervisorId_n, SDataConstants.BPSX_BP_ATT_SAL_AGT);
        SFormUtilities.populateComboBox(miClient, jcbFkBizArea, SDataConstants.BPSU_BA);

        moPanelBizPartnerBranch.formRefreshCatalogues();
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        focusLostAlternativeId();
        focusLostFiscalId();
        
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                break;
            }
        }
        
        if (!validation.getIsError()) {
            String fiscalId = jtfFiscalId.getText().trim();
            String alternativeId = jftAlternativeId.getText().trim();
            int fiscalIdLength = moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER ? DCfdConsts.LEN_RFC_PER : DCfdConsts.LEN_RFC_ORG;
            
            int[] countryKey = (int[]) moPanelBizPartnerBranch.getValue(SDataConstants.LOCU_CTY);
            boolean isForeign = countryKey != null && countryKey[0] != 0 && !SLibUtils.compareKeys(countryKey, miClient.getSession().getSessionCustom().getLocalCountryKey());
            
            if (moFieldFkTaxIdentityTypeId.getKeyAsIntArray()[0] == 0) {
                validation.setTabbedPaneIndex(TAB_MAIN);
                validation.setComponent(jcbFkTaxIdentityTypeId);
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkTaxIdentityTypeId.getText() + "'.");
            }
            else if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER && jtfLastName.getText().isEmpty()) {
                validation.setTabbedPaneIndex(TAB_MAIN);
                validation.setComponent(jtfLastName);
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlLastName.getText() + "'.");
            }
            else if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER && jtfFirstName.getText().isEmpty()) {
                validation.setTabbedPaneIndex(TAB_MAIN);
                validation.setComponent(jtfFirstName);
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFirstName.getText() + "'.");
            }
            else if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_ORG && jtfBizPartner.getText().isEmpty()) {
                validation.setTabbedPaneIndex(TAB_MAIN);
                validation.setComponent(jtfBizPartner);
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlBizPartner.getText() + "'.");
            }
            else if (fiscalId.isEmpty()) {
                validation.setTabbedPaneIndex(TAB_MAIN);
                validation.setComponent(jtfFiscalId);
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFiscalId.getText() + "'.");
            }
            else if (!fiscalId.equals(DCfdConsts.RFC_GEN_NAC) && !fiscalId.equals(DCfdConsts.RFC_GEN_INT) && fiscalId.length() != fiscalIdLength) {
                validation.setTabbedPaneIndex(TAB_MAIN);
                validation.setComponent(jtfFiscalId);
                validation.setMessage("El valor del campo '" + jlFiscalId.getText() + "', '" + fiscalId + "', debe tener " + fiscalIdLength + " caracteres.");
            }
            else if (!alternativeId.isEmpty() && alternativeId.length() != DCfdConsts.LEN_CURP) {
                validation.setTabbedPaneIndex(TAB_MAIN);
                validation.setComponent(jftAlternativeId);
                validation.setMessage("El valor del campo '" + jlAlternativeId.getText() + "', '" + alternativeId + "', debe tener " + DCfdConsts.LEN_CURP + " caracteres.");
            }
            else {
                if (jckIsDeleted.isSelected() || jckIsCategoryDeleted.isSelected()) {
                    try {
                        if (SFinUtilities.hasBizPartnerMovesFinance(miClient, miClient.getSessionXXX().getSystemYear(), moBizPartner.getPkBizPartnerId())) {
                            validation.setTabbedPaneIndex(TAB_MAIN);
                            validation.setComponent(jckIsDeleted);
                            validation.setMessage("No se puede eliminar al '" + getTitle() + "' o a su categoría debido a que tiene movimientos en el ejercicio actual " + miClient.getSessionXXX().getSystemYear() + ".");
                        }
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                    
                    if (!validation.getIsError()) {
                        if (jckIsDeleted.isSelected() && miClient.showMsgBoxConfirm("¿Está seguro que desea eliminar al '" + getTitle() + "'?") != JOptionPane.YES_OPTION) {
                            validation.setTabbedPaneIndex(TAB_MAIN);
                            validation.setComponent(jckIsDeleted);
                            validation.setMessage("Se debe deseleccionar el campo '" + jckIsDeleted.getText() + "' para no eliminar al '" + getTitle() + "'.");
                        }
                        
                        if (!validation.getIsError()) {
                            if (jckIsCategoryDeleted.isSelected() && miClient.showMsgBoxConfirm("¿Está seguro que desea eliminar la categoría del '" + getTitle() + "'?") != JOptionPane.YES_OPTION) {
                                validation.setTabbedPaneIndex(TAB_MAIN);
                                validation.setComponent(jckIsCategoryDeleted);
                                validation.setMessage("Se debe deseleccionar el campo '" + jckIsCategoryDeleted.getText() + "' para no eliminar la categoría del '" + getTitle() + "'.");
                            }
                        }
                    }
                }

                if (!validation.getIsError()) {
                    Object[] valParams = new Object[] { moBizPartner == null ? 0 : moBizPartner.getPkBizPartnerId(), moFieldBizPartner.getString() }; // odd parameters passing
                    int valCount = SDataUtilities.callProcedureVal(miClient, SProcConstants.BPSU_BP, valParams, SLibConstants.EXEC_MODE_VERBOSE);
                    
                    if (valCount > 0 && miClient.showMsgBoxConfirm("El valor del campo '" + jlBizPartner.getText() + "', '" + moFieldBizPartner.getString() + "',"
                            + "\nya existe " + valCount + " " + (valCount == 1 ? "vez" : "veces") + " en el sistema, ¿desea conservarlo?") != JOptionPane.YES_OPTION) {
                        validation.setTabbedPaneIndex(TAB_MAIN);
                        validation.setComponent(jtfBizPartner);
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlBizPartner.getText() + "'.");
                    }
                    
                    if (!validation.getIsError()) {
                        valParams = new Object[] { moBizPartner == null ? 0 : moBizPartner.getPkBizPartnerId(), fiscalId }; // odd parameters passing
                        valCount = SDataUtilities.callProcedureVal(miClient, SProcConstants.BPSU_BP_FISCAL_ID, valParams, SLibConstants.EXEC_MODE_VERBOSE);
                        
                        if (valCount > 0 && miClient.showMsgBoxConfirm("El valor del campo '" + jlFiscalId.getText() + "', '" + fiscalId + "',"
                                + "\nya existe " + valCount + " " + (valCount == 1 ? "vez" : "veces") + " en el sistema, ¿desea conservarlo?") != JOptionPane.YES_OPTION) {
                            validation.setTabbedPaneIndex(TAB_MAIN);
                            validation.setComponent(jtfFiscalId);
                            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFiscalId.getText() + "'.");
                        }
                    }
                }
                
                if (!validation.getIsError()) {
                    validation = moPanelBizPartnerBranch.formValidate();
                    validation.setTabbedPaneIndex(TAB_MAIN);
                }
                
                if (!validation.getIsError() && !isCompany()) {
                    validation.setTabbedPaneIndex(TAB_ADIT);
                    
                    for (int i = 0; i < mvFieldsCategory.size(); i++) {
                        if (!((erp.lib.form.SFormField) mvFieldsCategory.get(i)).validateField()) {
                            validation.setIsError(true);
                            validation.setComponent(((erp.lib.form.SFormField) mvFieldsCategory.get(i)).getComponent());
                            break;
                        }
                    }
                    
                    if (!validation.getIsError()) {
                        if (moFieldDateEnd.getFieldValue() != null && moFieldDateStart.getDate().after(moFieldDateEnd.getDate())) {
                            validation.setComponent(jftDateEnd_n);
                            validation.setMessage("La fecha del campo '" + jlDateStart.getText() + "' no puede ser posterior a la del campo '" + jlDateEnd_n.getText() + "'.");
                        }
                        else if (moFieldKey.getString().isEmpty() && (
                                isSupplier() && miClient.getSessionXXX().getParamsErp().getIsKeySupplierApplying() || 
                                isCustomer() && miClient.getSessionXXX().getParamsErp().getIsKeyCustomerApplying())) {
                            validation.setComponent(jftKey);
                            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlKey.getText() + "'.");
                        }
                        else if (isCustomerOrSupplier()) { // redundant if statement
                            if (jcbFkCreditTypeId_n.isEnabled() && jcbFkCreditTypeId_n.getSelectedIndex() <= 0) {
                                validation.setComponent(jcbFkCreditTypeId_n);
                                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkCreditTypeId_n.getText() + "'.");
                            }
                            else if (jcbFkCreditTypeId_n.isEnabled() && moFieldFkCreditTypeId_n.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_CRED_CRED_LIM && moFieldCreditLimit.getDouble() == 0) {
                                validation.setComponent(jtfCreditLimit);
                                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlCreditLimit.getText() + "'.");
                            }
                            else if (jcbFkCreditTypeId_n.isEnabled() && moFieldFkCreditTypeId_n.getKeyAsIntArray()[0] != SDataConstantsSys.BPSS_TP_CRED_CRED_NO && moFieldDaysOfCredit.getDouble() == 0) {
                                validation.setComponent(jtfDaysOfCredit);
                                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDaysOfCredit.getText() + "'.");
                            }
                            else if (moFieldGuaranteeAmount.getDouble() == 0 && moFieldIsGuaranteeInProcess.getBoolean()) {
                                validation.setComponent(jtfGuarantee);
                                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlGuarantee.getText() + "'.");
                            }
                            else if (moFieldGuaranteeAmount.getDouble() != 0 && bgGuaranteeType.getSelection() == null) {
                                validation.setComponent(jrbGuaranteeTypePay);
                                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlGuaranteeType.getText() + "'.");
                            }
                            else if (moFieldInsuranceAmount.getDouble() == 0 && moFieldIsInsuranceInProcess.getBoolean()) {
                                validation.setComponent(jtfInsurance);
                                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlInsurance.getText() + "'.");
                            }
                        }
                    }
                }
                
                if (!validation.getIsError() && isForeign) {
                    if (jcbCfdiPaymentWay.getSelectedIndex() > 0) {
                        validation.setComponent(jcbCfdiPaymentWay);
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_NOT_REQ + "'" + jlCfdiPaymentWay.getText() + "'");
                    }
                    else if (jcbCfdiCfdiUsage.getSelectedIndex() > 0) {
                        validation.setComponent(jcbCfdiCfdiUsage);
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_NOT_REQ + "'" + jlCfdiCfdiUsage.getText() + "'");
                    }
                    else if (!((String) moFieldTaxRegime.getKey()).equals(DCfdi40Catalogs.ClaveRégimenFiscalResidentesExtranjeros) && 
                            !((String) moFieldTaxRegime.getKey()).equals(DCfdi40Catalogs.ClaveRégimenFiscalSinObligacionesFiscales)) {
                        validation.setComponent(jcbTaxRegime);
                        validation.setMessage("El valor para el campo '" + jlTaxRegime.getText() + "' debe ser " +
                                DCfdi40Catalogs.ClaveRégimenFiscalResidentesExtranjeros + " o " + DCfdi40Catalogs.ClaveRégimenFiscalSinObligacionesFiscales + " para asociados de negocio extranjeros.");
                    }
                }
                
                if (!validation.getIsError() && !isForeign) {
                    if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER) {
                        if (!moXmlCatalogs.getEntryIsTaxpayerPerson(SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, (String) moFieldTaxRegime.getKey())){
                            validation.setComponent(jcbTaxRegime);
                            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlTaxRegime.getText() + "', ya que el valor seleccionado no corresponde a una persona fisica.");
                        }
                    }
                    else if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_ORG) {
                        if (!moXmlCatalogs.getEntryIsTaxpayerOrganization(SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, (String) moFieldTaxRegime.getKey())){
                            validation.setComponent(jcbTaxRegime);
                            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlTaxRegime.getText() + "', ya que el valor seleccionado no corresponde a una persona moral.");
                        }
                    }
                }
                
                if (!validation.getIsError() && isCustomer()) {
                    validation.setTabbedPaneIndex(TAB_MKT);
                    
                    for (int index = 0; index < mvFieldsCustomerConfig.size(); index++) {
                        if (!((erp.lib.form.SFormField) mvFieldsCustomerConfig.get(index)).validateField()) {
                            validation.setIsError(true);
                            validation.setComponent(((erp.lib.form.SFormField) mvFieldsCustomerConfig.get(index)).getComponent());
                            break;
                        }
                    }
                    
                    if (!validation.getIsError()) {
                        for (int index = 0; index < moCusBranchConfigPane.getTableGuiRowCount(); index++) {
                            SDataCustomerBranchConfig customerBranchConfig = (SDataCustomerBranchConfig) moCusBranchConfigPane.getTableRow(index).getData();
                            if (customerBranchConfig.getDbmsSalesRoute().isEmpty() && 
                                    miClient.showMsgBoxConfirm("Falta completar la configuración de la sucursal del cliente '" + customerBranchConfig.getDbmsCustomerBranch() + "', del renglón " + (index + 1) + "."
                                            + "\n" + SLibConstants.MSG_CNF_MSG_CONT) != JOptionPane.YES_OPTION) {
                                validation.setMessage("Completar la configuración de la sucursal del cliente '" + customerBranchConfig.getDbmsCustomerBranch() + "', del renglón " + (index + 1) + ".");
                                break;
                            }
                        }
                        
                        if (!validation.getIsError()) {
                            if (moFieldFkSalesSupervisorId_n.getKeyAsIntArray()[0] != 0 && moFieldFkSalesAgentId_n.getKeyAsIntArray()[0] == 0) {
                                validation.setComponent(jcbFkSalesAgentId_n);
                                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkSalesAgentId_n.getText() + "'.");
                            }
                            else if (moFieldFkSalesSupervisorId_n.getKeyAsIntArray()[0] != 0 && moFieldFkSalesSupervisorId_n.getKeyAsIntArray()[0] == moFieldFkSalesAgentId_n.getKeyAsIntArray()[0]) {
                                validation.setComponent(jcbFkSalesAgentId_n);
                                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFkSalesAgentId_n.getText() + "'.");
                            }
                        }
                    }
                }
                
                if (!validation.getIsError() && isSupplier()) {
                    if (jcbCfdiPaymentWay.isEnabled()){
                        if (((String) moFieldCfdiPaymentWay.getKey()).equals(DCfdi40Catalogs.FDP_POR_DEF)) {
                            if (miClient.showMsgBoxConfirm("El campo " + jlCfdiPaymentWay.getText() + " tiene el valor " + jcbCfdiPaymentWay.getSelectedItem().toString() + "\n¿Desea continuar?") != JOptionPane.OK_OPTION) {
                                validation.setComponent(jcbCfdiPaymentWay);
                                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlCfdiPaymentWay.getText() + "'.");
                            }
                        }
                    }
                    
                    if (!validation.getIsError()) {
                        if (jcbCfdiCfdiUsage.isEnabled()) {
                            if (((String) moFieldCfdiCfdiUsage.getKey()).equals(DCfdi40Catalogs.ClaveUsoCfdiSinEfectosFiscales)) {
                                if (miClient.showMsgBoxConfirm("El campo " + jlCfdiCfdiUsage.getText() + " tiene el valor " + jcbCfdiCfdiUsage.getSelectedItem().toString() + "\n¿Desea continuar?") != JOptionPane.OK_OPTION) {
                                    validation.setComponent(jcbCfdiCfdiUsage);
                                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlCfdiCfdiUsage.getText() + "'.");
                                }
                            }
                        }
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
        moBizPartner = (SDataBizPartner) registry;

        moFieldFkBizPartnerIdentityTypeId.setFieldValue(new int[] { moBizPartner.getFkBizPartnerIdentityTypeId() });
        renderBizPartnerIdentityType();
        
        moFieldLastName.setFieldValue(moBizPartner.getLastname());
        moFieldFirstName.setFieldValue(moBizPartner.getFirstname());
        moFieldBizPartner.setFieldValue(moBizPartner.getBizPartner());
        
        if (applyOrgNamesPolicy()) {
            switch (SLibUtilities.parseInt(moBizPartner.getBizPartnerFiscalPolicy())) {
                case SDataBizPartner.CFD_ORG_NAMES_FULL_NAME:
                    jrbOrgNamesPolicyFullName.setSelected(true);
                    break;
                case SDataBizPartner.CFD_ORG_NAMES_FISCAL_NAME:
                    jrbOrgNamesPolicyFiscalName.setSelected(true);
                    break;
                default:
                    // nothing
            }

            moFieldBizPartnerFiscal.setFieldValue(moBizPartner.getBizPartnerFiscal());
            moFieldBizPartnerCapitalRegime.setFieldValue(moBizPartner.getBizPartnerCapitalRegime());
        }
        
        moFieldBizPartnerCommercial.setFieldValue(moBizPartner.getBizPartner().equals(moBizPartner.getBizPartnerCommercial()) ? "" : moBizPartner.getBizPartnerCommercial());
        moFieldFiscalId.setFieldValue(moBizPartner.getFiscalId());
        moFieldFiscalFrgId.setFieldValue(moBizPartner.getFiscalFrgId());
        moFieldAlternativeId.setFieldValue(moBizPartner.getAlternativeId()); // CURP
        moFieldFkTaxIdentityTypeId.setFieldValue(new int[] { moBizPartner.getFkTaxIdentityId() });
        moFieldFkBizArea.setFieldValue(new int[] { moBizPartner.getFkBizAreaId() });
        moFieldWeb.setFieldValue(moBizPartner.getWeb());
        moFieldIsAttBank.setFieldValue(moBizPartner.getIsAttributeBank());
        moFieldIsAttCarrier.setFieldValue(moBizPartner.getIsAttributeCarrier());
        moFieldIsAttSalesAgent.setFieldValue(moBizPartner.getIsAttributeSalesAgent());
        moFieldIsAttEmployee.setFieldValue(moBizPartner.getIsAttributeEmployee());
        moFieldIsAttPartnerShareholder.setFieldValue(moBizPartner.getIsAttributePartnerShareholder());
        moFieldIsAttRelatedParty.setFieldValue(moBizPartner.getIsAttributeRelatedParty());
        moFieldIsDeleted.setFieldValue(moBizPartner.getIsDeleted());
        jtfPkBizPartnerId_Ro.setText("" + moBizPartner.getPkBizPartnerId());

        // headquarters:
        
        SDataBizPartnerBranch branchHq = moBizPartner.getDbmsBizPartnerBranchHq();
        if (branchHq != null) {
            moPanelBizPartnerBranch.setRegistry(branchHq);
        }

        // additional branches:
        
        for (int index = 1; index < moBizPartner.getDbmsBizPartnerBranches().size(); index++) { // start from index 1 to skip headquarters
            SDataBizPartnerBranch branch = moBizPartner.getDbmsBizPartnerBranches().get(index);
            moBizPartnerBranchPane.addTableRow(new SDataBizPartnerBranchRow(branch));
            ((STableModel) moBizPartnerBranchPane.getTable().getModel()).getTableRow(index - 1).setStyle(branch.getIsDeleted() ? STableConstants.STYLE_DELETE : 0);
        }

        moBizPartnerBranchPane.renderTableRows();
        moBizPartnerBranchPane.setTableRowSelection(0);
        
        // business partner categories:

        maoDbmsCategorySettings[0] = moBizPartner.getDbmsCategorySettingsCo();
        maoDbmsCategorySettings[1] = moBizPartner.getDbmsCategorySettingsSup();
        maoDbmsCategorySettings[2] = moBizPartner.getDbmsCategorySettingsCus();

        // read business partner notes:

        if (moBizPartner.getDbmsBizPartnerNotes().size() > 0) {
            moFieldNotes.setString(moBizPartner.getDbmsBizPartnerNotes().get(0).getNotes());
        }
        
        // get business partner category:

        if(isCompany()) {
            moBizPartnerCategory = maoDbmsCategorySettings[0];
        }
        else if(isSupplier()) {
            moBizPartnerCategory = maoDbmsCategorySettings[1];
        }
        else if (isCustomer()) {
            moBizPartnerCategory = maoDbmsCategorySettings[2];
        }
        
        moFieldIsCreditByUser.setFieldValue(moBizPartnerCategory != null ? moBizPartnerCategory.getIsCreditByUser() : false);
        itemStateChangedIsCreditByUser();

        if (moBizPartnerCategory != null) {
            moFieldFkBizPartnerCategoryId.setFieldValue(new int[] { moBizPartnerCategory.getFkBizPartnerCategoryId() });

            if (isSupplier()) {
                if (miClient.getSessionXXX().getParamsErp().getFkSupplierTypeId_n() > 0 && moBizPartnerCategory.getFkBizPartnerTypeId() == 0) {
                    moFieldFkBizPartnerTypeId.setFieldValue(new int[] { miClient.getSessionXXX().getParamsErp().getFkSupplierCategoryId_n(),
                    miClient.getSessionXXX().getParamsErp().getFkSupplierTypeId_n() });
                }
                else {
                    moFieldFkBizPartnerTypeId.setFieldValue(new int[] { moBizPartnerCategory.getFkBizPartnerCategoryId(), moBizPartnerCategory.getFkBizPartnerTypeId() });
                }
            }
            else if (isCustomer()) {
                if (miClient.getSessionXXX().getParamsErp().getFkCustomerTypeId_n() > 0 && moBizPartnerCategory.getFkBizPartnerTypeId() == 0) {
                    moFieldFkBizPartnerTypeId.setFieldValue(new int[] { miClient.getSessionXXX().getParamsErp().getFkCustomerCategoryId_n(),
                    miClient.getSessionXXX().getParamsErp().getFkCustomerTypeId_n() });
                }
                else {
                    moFieldFkBizPartnerTypeId.setFieldValue(new int[] { moBizPartnerCategory.getFkBizPartnerCategoryId(), moBizPartnerCategory.getFkBizPartnerTypeId() });
                }
            }
            else {
                moFieldFkBizPartnerTypeId.setFieldValue(new int[] { moBizPartnerCategory.getFkBizPartnerCategoryId(), moBizPartnerCategory.getFkBizPartnerTypeId() });
            }

            if (moBizPartnerCategory.getFkBizPartnerCategoryId() == SDataConstantsSys.BPSS_CT_BP_SUP) {
                moFieldKey.setMaskFormatter(miClient.getSessionXXX().getParamsErp().getFormatKeySupplier());
            }
            else if (moBizPartnerCategory.getFkBizPartnerCategoryId() == SDataConstantsSys.BPSS_CT_BP_CUS) {
                moFieldKey.setMaskFormatter(miClient.getSessionXXX().getParamsErp().getFormatKeyCustomer());
            }

            moFieldKey.setFieldValue(moBizPartnerCategory.getKey());
            moFieldCompanyKey.setFieldValue(moBizPartnerCategory.getCompanyKey());
            moFieldNumberExporter.setFieldValue(moBizPartnerCategory.getNumberExporter());
            
            bgGuaranteeType.clearSelection();
                    
            if (moBizPartnerCategory.getIsCreditByUser()) {
                moFieldFkCreditTypeId_n.setFieldValue(new int[] { moBizPartnerCategory.getEffectiveCreditTypeId() });
                moFieldCreditLimit.setFieldValue(moBizPartnerCategory.getEffectiveCreditLimit());
                moFieldDaysOfCredit.setFieldValue(moBizPartnerCategory.getEffectiveDaysOfCredit());
                moFieldDaysOfGrace.setFieldValue(moBizPartnerCategory.getEffectiveDaysOfGrace());
                moFieldFkRiskTypeId.setFieldValue(new int[] { moBizPartnerCategory.getEffectiveRiskTypeId() });
                moFieldGuaranteeAmount.setFieldValue(moBizPartnerCategory.getGuarantee());
                moFieldInsuranceAmount.setFieldValue(moBizPartnerCategory.getInsurance());
                moFieldIsGuaranteeInProcess.setFieldValue(moBizPartnerCategory.getIsGuaranteeInProcess());
                moFieldIsInsuranceInProcess.setFieldValue(moBizPartnerCategory.getIsInsuranceInProcess());
                
                switch (moBizPartnerCategory.getGuaranteeType()) {
                    case SDataBizPartnerCategory.GARNT_TP_PAY:
                        jrbGuaranteeTypePay.setSelected(true);
                        break;
                    case SDataBizPartnerCategory.GARNT_TP_PROP:
                        jrbGuaranteeTypeProp.setSelected(true);
                        break;
                    case SDataBizPartnerCategory.GARNT_TP_PAY_PROP:
                        jrbGuaranteeTypePayProp.setSelected(true);
                        break;
                    default:
                        // do nothing
                }
            }
            
            moFieldFkLanguageId_n.setFieldValue(new int[] { moBizPartnerCategory.getFkLanguageId_n() });
            moFieldFkCurrencyId_n.setFieldValue(new int[] { moBizPartnerCategory.getFkCurrencyId_n() });
            moFieldDiotOperation.setFieldValue(moBizPartnerCategory.getDiotOperation());
            moFieldDateStart.setFieldValue(moBizPartnerCategory.getDateStart());
            moFieldDateEnd.setFieldValue(moBizPartnerCategory.getDateEnd_n());
            moFieldLeadTime.setFieldValue(moBizPartnerCategory.getLeadTime());
            moFieldCfdiPaymentWay.setFieldValue(moBizPartnerCategory.getCfdiPaymentWay());
            moFieldCfdiCfdiUsage.setFieldValue(moBizPartnerCategory.getCfdiCfdiUsage());
            moFieldTaxRegime.setFieldValue(moBizPartnerCategory.getTaxRegime());
            moFieldFkCfdAddendaTypeId.setFieldValue(new int[] { moBizPartnerCategory.getFkCfdAddendaTypeId() });
            moFieldFkUserAnalystId.setFieldValue(new int[] { moBizPartnerCategory.getFkUserAnalystId_n() });
            moFieldIsCategoryDeleted.setFieldValue(moBizPartnerCategory.getIsDeleted());
            renderAdditionalInformation();
            
            msTempPaymentAccount = moBizPartnerCategory.getPaymentAccount();
            mnTempFkPaymentSystemTypeId = moBizPartnerCategory.getFkPaymentSystemTypeId_n();
        }

        updateSettingsBizPartnerKey();
        updateSettingsBizPartnerCategory();
        
        if (mnParamBizPartnerTypeX != SDataConstants.BPSX_BP_CO) {
            setLanguageEnabled(moBizPartnerCategory != null && moBizPartnerCategory.getFkLanguageId_n() > 0 && moBizPartnerCategory.getFkLanguageId_n() != miClient.getSessionXXX().getParamsErp().getFkLanguageId());
            setCurrencyEnabled(moBizPartnerCategory != null && moBizPartnerCategory.getFkCurrencyId_n() > 0 && moBizPartnerCategory.getFkCurrencyId_n() != miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId());
        }

        moPanelBizPartnerBranch.setParamIsCompany(moBizPartner.getIsCompany());
        
        // read customer marketing configuration:

        if (isCustomer() && moBizPartnerCategory != null) {
            moCustomerConfig = moBizPartner.getDbmsDataCustomerConfig();

            if (moCustomerConfig != null) {
                if (branchHq != null) {
                    moCustomerBranchConfig = branchHq.getDbmsDataCustomerBranchConfig();
                }
                
                moFieldFkCustomerTypeId.setKey(new int[] { moCustomerConfig.getFkCustomerTypeId() });
                moFieldFkMarketSegmentId.setKey(new int[] { moCustomerConfig.getFkMarketSegmentId() });
                moFieldFkMarketSubsegmentId.setKey(new int[] { moCustomerConfig.getFkMarketSegmentId(), moCustomerConfig.getFkMarketSubsegmentId() });
                moFieldFkDistributionChannelId.setKey(new int[] { moCustomerConfig.getFkDistributionChannelId() });
                moFieldFkSalesRouteId.setKey(new int[] { moCustomerBranchConfig != null ? moCustomerBranchConfig.getFkSalesRouteId() : 0 });
                moFieldFkSalesAgentId_n.setKey(new int[] { moCustomerConfig.getFkSalesAgentId_n() });
                moFieldFkSalesSupervisorId_n.setKey(new int[] { moCustomerConfig.getFkSalesSupervisorId_n() });
                moFieldIsFreeDiscountDoc.setFieldValue(moCustomerConfig.getIsFreeDiscountDoc());
                moFieldIsFreeCommissions.setFieldValue(moCustomerConfig.getIsFreeCommissions());
                moFieldIsSignRestricted.setFieldValue(moCustomerConfig.getIsSignRestricted());
                moFieldIsSignImmex.setFieldValue(moCustomerConfig.getIsSignImmex());
                moFieldIsSkipSignForeignCurrencyRestriction.setFieldValue(moCustomerConfig.getIsSkipSignForeignCurrencyRestriction());
            }

            int row = 0;
            for (int index = 1; index < moBizPartner.getDbmsBizPartnerBranches().size(); index++) { // start from index 1 to skip headquarters
                SDataCustomerBranchConfig customerBranchConfig = moBizPartner.getDbmsBizPartnerBranches().get(index).getDbmsDataCustomerBranchConfig();
                
                if (customerBranchConfig != null) {
                    moCusBranchConfigPane.addTableRow(new SDataCustomerBranchConfigRow(customerBranchConfig));
                    ((STableModel) moCusBranchConfigPane.getTable().getModel()).getTableRow(row++).setStyle(customerBranchConfig.getIsDeleted() ? STableConstants.STYLE_DELETE : 0);
                }
            }
            
            moCusBranchConfigPane.renderTableRows();
            moCusBranchConfigPane.setTableRowSelection(0);
        }

        jckIsDeleted.setEnabled(true);
        jckIsCategoryDeleted.setEnabled(moBizPartner.getDbmsDataEmployee() == null || (moBizPartner.getDbmsDataEmployee() != null && (isCustomerOrSupplier())));
        
        renderBizPartnerCategory();
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moBizPartner == null) {
            moBizPartner = new SDataBizPartner();
            moBizPartner.setFkFiscalBankId(SModSysConsts.FINS_FISCAL_BANK_NA);
            moBizPartner.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moBizPartner.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moBizPartner.setFkBizPartnerIdentityTypeId(moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0]);
        moBizPartner.setFkTaxIdentityId(moFieldFkTaxIdentityTypeId.getKeyAsIntArray()[0]);
        moBizPartner.setFkBizAreaId(moFieldFkBizArea.getKeyAsIntArray()[0]);

        if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER) {
            // business partner is a person
            
            // keep name for persons:
            
            moBizPartner.setLastname(moFieldLastName.getString());
            moBizPartner.setFirstname(moFieldFirstName.getString());
            moBizPartner.setAlternativeId(moFieldAlternativeId.getString().toUpperCase()); // CURP
            
            // clear name fiscal data for organizations:
            
            moBizPartner.setBizPartnerFiscalPolicy("");
            moBizPartner.setBizPartnerFiscal("");
            moBizPartner.setBizPartnerCapitalRegime("");
        }
        else {
            // business partner is an organization
            
            // clear name for persons:
            
            moBizPartner.setLastname("");
            moBizPartner.setFirstname("");
            moBizPartner.setAlternativeId(""); // CURP
            
            // keep name fiscal data for organizations only if applies:
            
            if (applyOrgNamesPolicy()) {
                if (mnCfgParamCfdOrgNames == SDataConstantsSys.CFG_PARAM_CFD_ORG_NAMES_RECEPTOR_CHOICE) {
                    moBizPartner.setBizPartnerFiscalPolicy("" + (jrbOrgNamesPolicyFiscalName.isSelected() ? SDataBizPartner.CFD_ORG_NAMES_FISCAL_NAME : SDataBizPartner.CFD_ORG_NAMES_FULL_NAME));
                }
                else {
                    moBizPartner.setBizPartnerFiscalPolicy("");
                }
                
                moBizPartner.setBizPartnerFiscal(moFieldBizPartnerFiscal.getString());
                moBizPartner.setBizPartnerCapitalRegime(moFieldBizPartnerCapitalRegime.getString());
            }
        }
        
        moBizPartner.setBizPartner(composeBizPartnerName());
        moBizPartner.setBizPartnerCommercial(moFieldBizPartnerCommercial.getString().isEmpty() ? moBizPartner.getBizPartner() : moFieldBizPartnerCommercial.getString());

        moBizPartner.setFiscalId(moFieldFiscalId.getString().toUpperCase());
        moBizPartner.setFiscalFrgId(moFieldFiscalFrgId.getString());
        //moBizPartner.setCodeBankSantander();
        //moBizPartner.setCodeBankBanBajio();
        moBizPartner.setWeb(moFieldWeb.getString());
        
        switch (mnParamBizPartnerTypeX) {
            case SDataConstants.BPSX_BP_CO:
                moBizPartner.setIsCompany(true);
                break;
            case SDataConstants.BPSX_BP_SUP:
                moBizPartner.setIsSupplier(true);
                break;
            case SDataConstants.BPSX_BP_CUS:
                moBizPartner.setIsCustomer(true);
                break;
            default:
        }
        
        moBizPartner.setIsAttributeBank(moFieldIsAttBank.getBoolean());
        moBizPartner.setIsAttributeCarrier(moFieldIsAttCarrier.getBoolean());
        moBizPartner.setIsAttributeSalesAgent(moFieldIsAttSalesAgent.getBoolean());
        moBizPartner.setIsAttributeEmployee(moFieldIsAttEmployee.getBoolean() && !jckIsAttEmployee.isEnabled());
        moBizPartner.setIsAttributePartnerShareholder(moFieldIsAttPartnerShareholder.getBoolean());
        moBizPartner.setIsAttributeRelatedParty(moFieldIsAttRelatedParty.getBoolean());
        moBizPartner.setIsDeleted(moFieldIsDeleted.getBoolean());

        // Save business partner category:

        if (moBizPartnerCategory == null) {
            moBizPartnerCategory = new SDataBizPartnerCategory();
            moBizPartnerCategory.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moBizPartnerCategory.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moBizPartnerCategory.setPkBizPartnerCategoryId(mnParamBizPartnerCategory);
        moBizPartnerCategory.setKey(moFieldKey.getString());
        moBizPartnerCategory.setCompanyKey(moFieldCompanyKey.getString());
        
        if (isCompany() || !jckIsCreditByUser.isSelected()) {
            moBizPartnerCategory.setCreditLimit(0);
            moBizPartnerCategory.setDaysOfCredit(0);
            moBizPartnerCategory.setDaysOfGrace(0);
            moBizPartnerCategory.setGuarantee(0);
            moBizPartnerCategory.setInsurance(0);
            moBizPartnerCategory.setIsGuaranteeInProcess(false);
            moBizPartnerCategory.setIsInsuranceInProcess(false);

            moBizPartnerCategory.setGuaranteeType("");
        }
        else {
            moBizPartnerCategory.setCreditLimit(moFieldFkCreditTypeId_n.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_CRED_CRED_LIM ? moFieldCreditLimit.getDouble() : 0);
            moBizPartnerCategory.setDaysOfCredit(moFieldDaysOfCredit.getInteger());
            moBizPartnerCategory.setDaysOfGrace(moFieldDaysOfGrace.getInteger());
            moBizPartnerCategory.setGuarantee(moFieldGuaranteeAmount.getDouble());
            moBizPartnerCategory.setInsurance(moFieldInsuranceAmount.getDouble());
            moBizPartnerCategory.setIsGuaranteeInProcess(moFieldIsGuaranteeInProcess.getBoolean());
            moBizPartnerCategory.setIsInsuranceInProcess(moFieldIsInsuranceInProcess.getBoolean());

            if (moFieldGuaranteeAmount.getDouble() == 0) {
                moBizPartnerCategory.setGuaranteeType(""); // there is not guarantee, so clear any type of guarantee!
            }
            else {
                if (jrbGuaranteeTypePay.isSelected()) {
                    moBizPartnerCategory.setGuaranteeType(SDataBizPartnerCategory.GARNT_TP_PAY);
                }
                else if (jrbGuaranteeTypeProp.isSelected()) {
                    moBizPartnerCategory.setGuaranteeType(SDataBizPartnerCategory.GARNT_TP_PROP);
                }
                else if (jrbGuaranteeTypePayProp.isSelected()) {
                    moBizPartnerCategory.setGuaranteeType(SDataBizPartnerCategory.GARNT_TP_PAY_PROP);
                }
                else {
                    moBizPartnerCategory.setGuaranteeType("");
                }
            }
        }

        moBizPartnerCategory.setDateStart(moFieldDateStart.getDate());
        moBizPartnerCategory.setDateEnd_n(moFieldDateEnd.getDate());
        moBizPartnerCategory.setPaymentAccount(msTempPaymentAccount);
        moBizPartnerCategory.setNumberExporter(moFieldNumberExporter.getString());
        moBizPartnerCategory.setDiotOperation(!jcbDiotOperation.isEnabled() ? "" : (String) moFieldDiotOperation.getFieldValue());
        moBizPartnerCategory.setCfdiPaymentWay(jcbCfdiPaymentWay.getSelectedIndex() <= 0 ? "" : moFieldCfdiPaymentWay.getKey().toString());
        moBizPartnerCategory.setCfdiCfdiUsage(jcbCfdiCfdiUsage.getSelectedIndex() <= 0 ? "" : moFieldCfdiCfdiUsage.getKey().toString());
        moBizPartnerCategory.setTaxRegime(jcbTaxRegime.getSelectedIndex() <= 0 ? "" : moFieldTaxRegime.getKey().toString());
        moBizPartnerCategory.setLeadTime(moFieldLeadTime.getInteger()); 

        moBizPartnerCategory.setIsCreditByUser(jckIsCreditByUser.isSelected());
        moBizPartnerCategory.setFkBizPartnerCategoryId(mnParamBizPartnerCategory);
        moBizPartnerCategory.setFkBizPartnerTypeId(jcbFkBizPartnerTypeId.getSelectedIndex() <= 0 ? SDataConstantsSys.BPSU_TP_BP_DEFAULT : moFieldFkBizPartnerTypeId.getKeyAsIntArray()[1]);
        moBizPartnerCategory.setFkCreditTypeId_n(!jckIsCreditByUser.isSelected() ? SDataConstantsSys.BPSS_TP_CRED_CRED_NO : moFieldFkCreditTypeId_n.getKeyAsIntArray()[0]);
        moBizPartnerCategory.setFkRiskId_n(!jckIsCreditByUser.isSelected() ? SModSysConsts.BPSS_RISK_C_RISK_HIGH : moFieldFkRiskTypeId.getKeyAsIntArray()[0]);
        moBizPartnerCategory.setFkPaymentSystemTypeId_n(mnTempFkPaymentSystemTypeId);
        moBizPartnerCategory.setFkCfdAddendaTypeId(moFieldFkCfdAddendaTypeId.getKeyAsIntArray()[0]);

        moBizPartnerCategory.setFkLanguageId_n(jcbFkLanguageId_n.getSelectedIndex() <= 0 ? -1 : moFieldFkLanguageId_n.getKeyAsIntArray()[0]);
        moBizPartnerCategory.setFkCurrencyId_n(jcbFkCurrencyId_n.getSelectedIndex() <= 0 ? -1 : moFieldFkCurrencyId_n.getKeyAsIntArray()[0]);
        moBizPartnerCategory.setAuxLanguageSysId(miClient.getSessionXXX().getParamsErp().getFkLanguageId());
        moBizPartnerCategory.setAuxCurrencySysId(miClient.getSessionXXX().getParamsErp().getFkCurrencyId());
        moBizPartnerCategory.setFkUserAnalystId_n(jcbFkUserAnalystId.getSelectedIndex() <= 0 ? 0 : moFieldFkUserAnalystId.getKeyAsIntArray()[0]);
        moBizPartnerCategory.setIsDeleted(moFieldIsCategoryDeleted.getBoolean());

        moBizPartnerCategory.setDbmBizPartnerType(jcbFkBizPartnerTypeId.getSelectedIndex() <= 0 ? "" : jcbFkBizPartnerTypeId.getSelectedItem().toString());
        moBizPartnerCategory.setDbmsCreditType(jcbFkCreditTypeId_n.getSelectedIndex() <= 0 ? "" : jcbFkCreditTypeId_n.getSelectedItem().toString());
        moBizPartnerCategory.setDbmsLanguage(jcbFkLanguageId_n.getSelectedIndex() <= 0 ? "" : readLanguageKey(moFieldFkLanguageId_n.getKeyAsIntArray()[0]));
        moBizPartnerCategory.setDbmsCurrency(jcbFkCurrencyId_n.getSelectedIndex() <= 0 ? "" : readCurrencyKey(moFieldFkCurrencyId_n.getKeyAsIntArray()[0]));

        moBizPartnerCategory.setIsRegistryEdited(true);

        switch (mnParamBizPartnerCategory) {
            case SDataConstantsSys.BPSS_CT_BP_CO:
                moBizPartner.setDbmsCategorySettingsCo(moBizPartnerCategory);
                break;
            case SDataConstantsSys.BPSS_CT_BP_SUP:
                moBizPartner.setDbmsCategorySettingsSup(moBizPartnerCategory);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
                moBizPartner.setDbmsCategorySettingsCus(moBizPartnerCategory);
                break;
            default:
        }

        // Save business partner branches:

        moBizPartner.getDbmsBizPartnerBranches().clear();
        
        SDataBizPartnerBranch branchHq = (SDataBizPartnerBranch) moPanelBizPartnerBranch.getRegistry();
        branchHq.setAuxSaveBkc(mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_CO && branchHq.getIsRegistryNew());
        moBizPartner.getDbmsBizPartnerBranches().add(branchHq);

        for (int index = 0; index < moBizPartnerBranchPane.getTable().getRowCount(); index++) {
            SDataBizPartnerBranch branch = (SDataBizPartnerBranch) moBizPartnerBranchPane.getTableRow(index).getData();
            branch.setAuxSaveBkc(mnParamBizPartnerTypeX == SDataConstants.BPSX_BP_CO && branch.getIsRegistryNew());
            moBizPartner.getDbmsBizPartnerBranches().add(branch);
        }

        // Save business partner notes:

        moBizPartner.getDbmsBizPartnerNotes().clear();
        
        if (!moFieldNotes.getString().isEmpty()) {
            SDataBizPartnerNote note = new SDataBizPartnerNote();
            note.setNotes(moFieldNotes.getString());
            moBizPartner.getDbmsBizPartnerNotes().add(note);
        }

        // Save customer configuration:

        if (mnParamBizPartnerCategory == SDataConstantsSys.BPSS_CT_BP_CUS) {
            // Customer headquarters:
            
            if (moCustomerConfig == null) {
                moCustomerConfig = new SDataCustomerConfig();
                moCustomerConfig.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
            }
            else {
                moCustomerConfig.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
            }

            moCustomerConfig.setPkCustomerId(moBizPartner.getPkBizPartnerId());
            moCustomerConfig.setFkCustomerTypeId(moFieldFkCustomerTypeId.getKeyAsIntArray()[0]);
            moCustomerConfig.setFkMarketSegmentId(moFieldFkMarketSegmentId.getKeyAsIntArray()[0]);
            moCustomerConfig.setFkMarketSubsegmentId(moFieldFkMarketSubsegmentId.getKeyAsIntArray()[1]);
            moCustomerConfig.setFkDistributionChannelId(moFieldFkDistributionChannelId.getKeyAsIntArray()[0]);
            moCustomerConfig.setFkSalesAgentId_n(moFieldFkSalesAgentId_n.getKeyAsIntArray()[0]);
            moCustomerConfig.setFkSalesSupervisorId_n(moFieldFkSalesSupervisorId_n.getKeyAsIntArray()[0]);
            moCustomerConfig.setIsFreeDiscountDoc(moFieldIsFreeDiscountDoc.getBoolean());
            moCustomerConfig.setIsFreeCommissions(moFieldIsFreeCommissions.getBoolean());
            moCustomerConfig.setIsSignRestricted(moFieldIsSignRestricted.getBoolean());
            moCustomerConfig.setIsSignImmex(moFieldIsSignImmex.getBoolean());
            moCustomerConfig.setIsSkipSignForeignCurrencyRestriction(moFieldIsSkipSignForeignCurrencyRestriction.getBoolean());

            SDataCustomerBranchConfig customerBranchConfigHq = moBizPartner.getDbmsBizPartnerBranchHq().getDbmsDataCustomerBranchConfig();
            
            if (customerBranchConfigHq == null) {
                customerBranchConfigHq = new SDataCustomerBranchConfig();
                customerBranchConfigHq.setDbmsCustomerBranch(moBizPartner.getDbmsBizPartnerBranchHq().getBizPartnerBranch());
                customerBranchConfigHq.setDbmsSalesRoute(moFieldFkSalesRouteId.getString());
                customerBranchConfigHq.setDbmsSalesAgent("");
                customerBranchConfigHq.setDbmsSalesSupervisor("");
                customerBranchConfigHq.setFkSalesRouteId(moFieldFkSalesRouteId.getKeyAsIntArray()[0]);
                customerBranchConfigHq.setFkSalesAgentId_n(0);
                customerBranchConfigHq.setFkSalesSupervisorId_n(0);
                customerBranchConfigHq.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                customerBranchConfigHq.setFkUserEditId(SUtilConsts.USR_NA_ID);
                customerBranchConfigHq.setFkUserDeleteId(SUtilConsts.USR_NA_ID);
                customerBranchConfigHq.setDbmsUserNew(miClient.getSessionXXX().getUser().getUser());
                customerBranchConfigHq.setDbmsUserEdit("");
                customerBranchConfigHq.setDbmsUserDelete("");
                customerBranchConfigHq.setUserNewTs(new Date());
                customerBranchConfigHq.setUserEditTs(null);
                customerBranchConfigHq.setUserDeleteTs(null);
            }
            else {
                customerBranchConfigHq.setFkSalesRouteId(moFieldFkSalesRouteId.getKeyAsIntArray()[0]);
                customerBranchConfigHq.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
            }
            
            customerBranchConfigHq.setIsRegistryEdited(true);
            moBizPartner.getDbmsBizPartnerBranchHq().setDbmsDataCustomerBranchConfig(customerBranchConfigHq);

            moCustomerConfig.setIsRegistryEdited(true);
            moBizPartner.setDbmsDataCustomerConfig(moCustomerConfig);
            
            // Other customer branches:
            
            for (int index = 0; index < moCusBranchConfigPane.getTableGuiRowCount(); index++) {
                SDataCustomerBranchConfig customerBranchConfig = (SDataCustomerBranchConfig) moCusBranchConfigPane.getTableRow(index).getData();
                
                if (customerBranchConfig.getIsRegistryEdited()) { // discard new registries, if a new registry has been modified, then it will appear as edited as well
                    SDataBizPartnerBranch branch = null;

                    // get customer branch by matching branch ID:

                    if (customerBranchConfig.getPkCustomerBranchId() != 0) {
                        branch = moBizPartner.getDbmsBizPartnerBranch(new int[] { customerBranchConfig.getPkCustomerBranchId() });
                    }

                    if (branch == null) {
                        // get customer branch by matching object's references of branch configuration:

                        for (SDataBizPartnerBranch bpb : moBizPartner.getDbmsBizPartnerBranches()) {
                            if (bpb.getDbmsDataCustomerBranchConfig() == customerBranchConfig) {
                                branch = bpb;
                                break;
                            }
                        }
                    }

                    if (branch == null) {
                        miClient.showMsgBoxWarning("No se encontró la sucursal correspondiente a la configuración de comercialización de la sucursal del cliente del renglón " + (index + 1) + ".");
                    }
                    else {
                        customerBranchConfig.setIsRegistryEdited(true);
                        branch.setDbmsDataCustomerBranchConfig(customerBranchConfig);
                        branch.setIsRegistryEdited(true); // allow to save the customer configuration
                    }
                }
            }
        }
        
        moBizPartner.setIsRegistryEdited(true);

        return moBizPartner;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case SDataConstantsSys.VALUE_BIZ_PARTNER_TYPE:
                mnParamBizPartnerTypeX = ((int[]) value)[0];
                mbCanEditCredit = false;
                
                switch (mnParamBizPartnerTypeX) {
                    case SDataConstants.BPSX_BP_CO:
                        setTitle("Empresa");
                        jckIsCategoryDeleted.setText("Empresa eliminada");
                        mnParamBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_CO;
                        break;
                        
                    case SDataConstants.BPSX_BP_SUP:
                        setTitle("Proveedor");
                        jckIsCategoryDeleted.setText("Proveedor eliminado");
                        mnParamBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_SUP;
                        mbCanEditCredit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_CRED_CONFIG).HasRight;
                        break;
                        
                    case SDataConstants.BPSX_BP_CUS:
                        setTitle("Cliente");
                        jckIsCategoryDeleted.setText("Cliente eliminado");
                        mnParamBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_CUS;
                        mbCanEditCredit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_CRED_CONFIG).HasRight;
                        break;
                        
                    default:
                        setTitle("Asociado de negocios");
                        jckIsCategoryDeleted.setText("Asociado de negocios eliminado");
                }
                break;
                
            default:
        }
    }

    @Override
    public java.lang.Object getValue(int type) {
        Object value = null;

        switch (type) {
            case SDataConstantsSys.VALUE_BIZ_PARTNER:
                value = composeBizPartnerName();
                break;
            default:
                // do nothing
        }

        return value;
    }

    @Override
    public javax.swing.JLabel getTimeoutLabel() {
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbAddBranch) {
                actionAddBranch();
            }
            else if (button == jbModifyBranch) {
                actionModifyBranch();
            }
            else if (button == jbRecreateBizPartnerCommercial) {
                actionRecreateBizPartnerCommercial();
            }
            if (button == jbSetDateStart) {
                actionSetDateStart();
            }
            else if (button == jbSetDateEnd_n) {
                actionSetDateEnd_n();
            }
            else if (button == jbFkLanguageId_n) {
                actionFkLanguageId();
            }
            else if (button == jbFkCurrencyId_n) {
                actionFkCurrencyId();
            }
            else if (button == jbEditLanguage) {
                actionEditLanguage();
            }
            else if (button == jbEditCurrency) {
                actionEditCurrency();
            }
            else if (button == jbEditCfdiPaymentWay) {
                actionEditCfdiPaymentWay();
            }
            else if (button == jbEditCfdiCfdiUsage) {
                actionEditCfdiUsage();
            }
            else if (button == jbFkBizPartnerTypeId) {
                actionFkBizPartnerTypeId();
            }
            else if (button == jbFkCustomerTypeId) {
                actionFkCustomerTypeId();
            }
            else if (button == jbFkMarketSegmentId) {
                actionFkMarketSegmentId();
            }
            else if (button == jbFkMarketSubsegmentId) {
                actionFkMarketSubsegmentId();
            }
            else if (button == jbFkDistributionChannelId) {
                actionFkDistributionChannelId();
            }
            else if (button == jbFkSalesRouteId) {
                actionFkSalesRouteId();
            }
            else if (button == jbFkSalesAgentId_n) {
                actionFkSalesAgentId_n();
            }
            else if (button == jbFkSalesSupervisorId_n) {
                actionFkSalesSupervisorId_n();
            }
            else if (button == jbNewCusBranchConfig) {
                // not implemented
            }
            else if (button == jbModifyCusBranchConfig) {
                actionModifyCusBranchConfig();
            }
            else if (button == jbDeleteCusBranchConfig) {
                actionDeleteCusBranchConfig();
            }
            else if (button == jbAddCusBranchConfig) {
                actionAddCusBranchConfig();
            }
            else if (button == jbSupplier) {
                actionSupplier();
            }
            else if (button == jbCustomer) {
                actionCustomer();
            }
            else if (button == jbCreditor) {
                actionCreditor();
            }
            else if (button == jbDebtor) {
                actionDebtor();
            }
        }
    }

    @Override
    public SLibMethod getPostSaveMethod(SDataRegistry registry) {
        SLibMethod method = null;

        if (mbIsNeededPosSave) {
            try {
                method = new SLibMethod(registry, registry.getClass().getMethod("openCategory", new Class[] { SClientInterface.class, int.class, Object.class }), new Object[] { miClient, mnFormTypeExport, registry.getPrimaryKey() });
            }
            catch (NoSuchMethodException | SecurityException e) {
                SLibUtils.showException(this, e);
            }
        }

        return method;
    }
}
