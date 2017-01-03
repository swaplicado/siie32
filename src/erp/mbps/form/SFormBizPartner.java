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

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
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
import erp.mcfg.data.SDataCurrency;
import erp.mcfg.data.SDataLanguage;
import erp.mfin.data.SFinUtilities;
import erp.mmkt.data.SDataCustomerBranchConfig;
import erp.mmkt.data.SDataCustomerBranchConfigRow;
import erp.mmkt.data.SDataCustomerConfig;
import erp.mmkt.form.SFormCustomerConfigurationCob;
import erp.mod.SModSysConsts;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import sa.lib.SLibMethod;
import sa.lib.SLibUtils;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SFormBizPartner extends javax.swing.JDialog implements erp.lib.form.SFormExtendedInterface, java.awt.event.ActionListener {

    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private java.util.Vector<erp.lib.form.SFormField> mvFieldsCategory;
    private java.util.Vector<erp.lib.form.SFormField> mvFieldsCusConfig;
    private erp.client.SClientInterface miClient;

    private erp.mbps.data.SDataBizPartner moBizPartner;
    private erp.mbps.data.SDataBizPartnerType moBizPartnerType;
    private erp.lib.form.SFormField moFieldFkBizPartnerIdentityTypeId;
    private erp.lib.form.SFormField moFieldLastName;
    private erp.lib.form.SFormField moFieldFirstName;
    private erp.lib.form.SFormField moFieldBizPartner;
    private erp.lib.form.SFormField moFieldBizPartnerCommercial;
    private erp.lib.form.SFormField moFieldFiscalId;
    private erp.lib.form.SFormField moFieldFiscalFrgId;
    private erp.lib.form.SFormField moFieldAlternativeId;
    private erp.lib.form.SFormField moFieldFkTaxIdentityTypeId;
    private erp.lib.form.SFormField moFieldIsDeleted;
    private erp.lib.form.SFormField moFieldIsAttBank;
    private erp.lib.form.SFormField moFieldIsAttCarrier;
    private erp.lib.form.SFormField moFieldIsAttSalesAgent;
    private erp.lib.form.SFormField moFieldIsAttEmployee;
    private erp.lib.form.SFormField moFieldIsAttPartnerShareholder;
    private erp.lib.form.SFormField moFieldIsAttRelatedParty;
    private erp.lib.form.SFormField moFieldFkBizArea;
    private erp.lib.form.SFormField moFieldWeb;
    private erp.lib.form.SFormField moFieldNotes;

    private erp.lib.form.SFormField moFieldFkBizPartnerCategoryId;
    private erp.lib.form.SFormField moFieldFkBizPartnerTypeId;
    private erp.lib.form.SFormField moFieldKey;
    private erp.lib.form.SFormField moFieldCompanyKey;
    private erp.lib.form.SFormField moFieldFkCreditTypeId_n;
    private erp.lib.form.SFormField moFieldFkRiskTypeId;
    private erp.lib.form.SFormField moFieldGuaranteeAmount;
    private erp.lib.form.SFormField moFieldInsuranceAmount;
    private erp.lib.form.SFormField moFieldIsInInsurProcess;
    private erp.lib.form.SFormField moFieldIsInGuarProcess;
    private erp.lib.form.SFormField moFieldCreditLimit;
    private erp.lib.form.SFormField moFieldDaysOfCredit;
    private erp.lib.form.SFormField moFieldDaysOfGrace;
    private erp.lib.form.SFormField moFieldIsCreditByUser;
    private erp.lib.form.SFormField moFieldFkLanguageId;
    private erp.lib.form.SFormField moFieldFkCurrencyId;
    private erp.lib.form.SFormField moFieldDateStart;
    private erp.lib.form.SFormField moFieldDateEnd;
    private erp.lib.form.SFormField moFieldFkCfdAddendaTypeId;
    private erp.lib.form.SFormField moFieldIsCategoryDeleted;

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

    private erp.mbps.data.SDataBizPartnerCategory moBizPartnerCategory;
    private erp.mbps.form.SFormBizPartnerBranch moFormBizPartnerBranch;

    private erp.mbps.form.SPanelBizPartnerBranch moPanelBizPartnerBranch;
    private erp.lib.table.STablePane moBizPartnerBranchPane;

    private erp.lib.form.SFormComboBoxGroup moComboBoxBizPartnerIdentity;
    private erp.mbps.data.SDataBizPartnerCategory[] maoDbmsCategorySettings;
    private erp.mmkt.data.SDataCustomerConfig moCustomerConfig;

    private erp.lib.table.STablePane moCustomerConfigCobPane;

    private ImageIcon moIconHasCategorySup;
    private ImageIcon moIconHasCategoryCus;
    private ImageIcon moIconHasCategoryCdr;
    private ImageIcon moIconHasCategoryDbr;
    private ImageIcon moIconHasNotCategory;

    private int mnParamBizPartnerType;
    private int mnParamBizPartnerCategoryFilter;

    private int mnFormTypeExport;
    private boolean mbIsNeededPosSave;

    /** Creates new form SFormBizPartner */
    public SFormBizPartner(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient =  client;

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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jpBizPartner = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jlFkBizPartnerIdentityTypeId = new javax.swing.JLabel();
        jcbFkBizPartnerIdentityTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jckIsDeleted = new javax.swing.JCheckBox();
        jPanel18 = new javax.swing.JPanel();
        jlFkTaxIdentityTypeId = new javax.swing.JLabel();
        jcbFkTaxIdentityTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jckIsCategoryDeleted = new javax.swing.JCheckBox();
        jPanel28 = new javax.swing.JPanel();
        jlLastName = new javax.swing.JLabel();
        jtfLastName = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jlFirstName = new javax.swing.JLabel();
        jtfFirstName = new javax.swing.JTextField();
        jPanel32 = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        jtfBizPartner = new javax.swing.JTextField();
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
        jPanel4 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jlDummy1 = new javax.swing.JLabel();
        jlIconCustomer = new javax.swing.JLabel();
        jlDummy2 = new javax.swing.JLabel();
        jlIconSupplier = new javax.swing.JLabel();
        jlDummy3 = new javax.swing.JLabel();
        jlIconDebtor = new javax.swing.JLabel();
        jlDummy4 = new javax.swing.JLabel();
        jlIconCreditor = new javax.swing.JLabel();
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
        jckIsAttRelatedParty = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jlBizArea = new javax.swing.JLabel();
        jcbFkBizArea = new javax.swing.JComboBox();
        jpCategory = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
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
        jPanel38 = new javax.swing.JPanel();
        jlCompanyKey = new javax.swing.JLabel();
        jtfCompanyKey = new javax.swing.JTextField();
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
        jlFkRiskTypeId = new javax.swing.JLabel();
        jcbFkRiskTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel51 = new javax.swing.JPanel();
        jlGuarantee = new javax.swing.JLabel();
        jtfGuarantee = new javax.swing.JTextField();
        jtfCurrencyKeyGuar = new javax.swing.JTextField();
        jckGuarInProcess = new javax.swing.JCheckBox();
        jPanel52 = new javax.swing.JPanel();
        jlInsurance = new javax.swing.JLabel();
        jtfInsurance = new javax.swing.JTextField();
        jtfCurrencyKeyInsur = new javax.swing.JTextField();
        jckInsurInProcess = new javax.swing.JCheckBox();
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
        jPanel9 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        jftDateStart = new javax.swing.JFormattedTextField();
        jbSetDateStart = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        jftDateEnd = new javax.swing.JFormattedTextField();
        jbSetDateEnd = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jlFkCfdAddendaTypeId = new javax.swing.JLabel();
        jcbFkCfdAddendaTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel5 = new javax.swing.JPanel();
        jlWeb = new javax.swing.JLabel();
        jtfWeb = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jlNotes = new javax.swing.JLabel();
        jtfNotes = new javax.swing.JTextField();
        jpBizPartnerBranch = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jbAddBranch = new javax.swing.JButton();
        jbModifyBranch = new javax.swing.JButton();
        jpMarketing = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jlFkCustomerTypeId = new javax.swing.JLabel();
        jPanel55 = new javax.swing.JPanel();
        jcbFkCustomerTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkCustomerTypeId = new javax.swing.JButton();
        jlFkMarketSegmentId = new javax.swing.JLabel();
        jPanel56 = new javax.swing.JPanel();
        jcbFkMarketSegmentId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkMarketSegmentId = new javax.swing.JButton();
        jlFkMarketSubsegmentId = new javax.swing.JLabel();
        jPanel57 = new javax.swing.JPanel();
        jcbFkMarketSubsegmentId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkMarketSubsegmentId = new javax.swing.JButton();
        jlFkDistributionChannelId = new javax.swing.JLabel();
        jPanel58 = new javax.swing.JPanel();
        jcbFkDistributionChannelId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkDistributionChannelId = new javax.swing.JButton();
        jlFkSalesRouteId = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jcbFkSalesRouteId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkSalesRouteId = new javax.swing.JButton();
        jlFkSalesAgentId_n = new javax.swing.JLabel();
        jPanel59 = new javax.swing.JPanel();
        jcbFkSalesAgentId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkSalesAgentId_n = new javax.swing.JButton();
        jlFkSalesSupervisorId_n = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jcbFkSalesSupervisorId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkSalesSupervisorId_n = new javax.swing.JButton();
        jckIsFreeDiscountDoc = new javax.swing.JCheckBox();
        jckIsFreeCommissions = new javax.swing.JCheckBox();
        jpCob = new javax.swing.JPanel();
        jpNotesAction = new javax.swing.JPanel();
        jbNew = new javax.swing.JButton();
        jbEdit = new javax.swing.JButton();
        jbDel = new javax.swing.JButton();
        jbAddCob = new javax.swing.JButton();
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

        jPanel19.setPreferredSize(new java.awt.Dimension(1008, 220));
        jPanel19.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setPreferredSize(new java.awt.Dimension(611, 200));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel13.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkBizPartnerIdentityTypeId.setText("Tipo de identidad: *");
        jlFkBizPartnerIdentityTypeId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel24.add(jlFkBizPartnerIdentityTypeId);

        jcbFkBizPartnerIdentityTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jcbFkBizPartnerIdentityTypeId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbFkBizPartnerIdentityTypeIdItemStateChanged(evt);
            }
        });
        jPanel24.add(jcbFkBizPartnerIdentityTypeId);

        jckIsDeleted.setText("Asociado de negocios eliminado");
        jckIsDeleted.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel24.add(jckIsDeleted);

        jPanel13.add(jPanel24);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkTaxIdentityTypeId.setText("Identidad impuestos: *");
        jlFkTaxIdentityTypeId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel18.add(jlFkTaxIdentityTypeId);

        jcbFkTaxIdentityTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel18.add(jcbFkTaxIdentityTypeId);

        jckIsCategoryDeleted.setText("Categoría eliminada");
        jckIsCategoryDeleted.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel18.add(jckIsCategoryDeleted);

        jPanel13.add(jPanel18);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLastName.setText("Apellido(s): *");
        jlLastName.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel28.add(jlLastName);

        jtfLastName.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel28.add(jtfLastName);

        jPanel13.add(jPanel28);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFirstName.setText("Nombre(s): *");
        jlFirstName.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel2.add(jlFirstName);

        jtfFirstName.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel2.add(jtfFirstName);

        jPanel13.add(jPanel2);

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner.setText("Razón social: *");
        jlBizPartner.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel32.add(jlBizPartner);

        jtfBizPartner.setPreferredSize(new java.awt.Dimension(354, 23));
        jPanel32.add(jtfBizPartner);

        jPanel13.add(jPanel32);

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerCommercial.setText("Nombre comercial:");
        jlBizPartnerCommercial.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel34.add(jlBizPartnerCommercial);

        jtfBizPartnerCommercial.setPreferredSize(new java.awt.Dimension(354, 23));
        jPanel34.add(jtfBizPartnerCommercial);

        jbRecreateBizPartnerCommercial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbRecreateBizPartnerCommercial.setToolTipText("Crear nombre comercial");
        jbRecreateBizPartnerCommercial.setFocusable(false);
        jbRecreateBizPartnerCommercial.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel34.add(jbRecreateBizPartnerCommercial);

        jPanel13.add(jPanel34);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFiscalId.setText("RFC: *");
        jlFiscalId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel33.add(jlFiscalId);

        jtfFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel33.add(jtfFiscalId);

        jlAlternativeId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAlternativeId.setText("CURP:");
        jlAlternativeId.setPreferredSize(new java.awt.Dimension(54, 23));
        jPanel33.add(jlAlternativeId);

        jftAlternativeId.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        jftAlternativeId.setPreferredSize(new java.awt.Dimension(125, 23));
        jftAlternativeId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jftAlternativeIdFocusLost(evt);
            }
        });
        jPanel33.add(jftAlternativeId);

        jlFiscalFrgId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlFiscalFrgId.setText("ID fiscal:");
        jlFiscalFrgId.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel33.add(jlFiscalFrgId);

        jtfFiscalFrgId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel33.add(jtfFiscalFrgId);

        jPanel13.add(jPanel33);

        jPanel1.add(jPanel13, java.awt.BorderLayout.NORTH);

        jPanel19.add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Categorías:"));
        jPanel4.setMinimumSize(new java.awt.Dimension(355, 220));
        jPanel4.setPreferredSize(new java.awt.Dimension(397, 210));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel26.setPreferredSize(new java.awt.Dimension(385, 200));
        jPanel26.setLayout(new java.awt.GridLayout(6, 1, 0, 5));

        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDummy1.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel20.add(jlDummy1);

        jlIconCustomer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlIconCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/bp_cus.png"))); // NOI18N
        jPanel20.add(jlIconCustomer);

        jlDummy2.setPreferredSize(new java.awt.Dimension(52, 23));
        jPanel20.add(jlDummy2);

        jlIconSupplier.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlIconSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/bp_sup.png"))); // NOI18N
        jPanel20.add(jlIconSupplier);

        jlDummy3.setPreferredSize(new java.awt.Dimension(52, 23));
        jPanel20.add(jlDummy3);

        jlIconDebtor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlIconDebtor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/bp_dbr.png"))); // NOI18N
        jPanel20.add(jlIconDebtor);

        jlDummy4.setPreferredSize(new java.awt.Dimension(52, 23));
        jPanel20.add(jlDummy4);

        jlIconCreditor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlIconCreditor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/bp_cdr.png"))); // NOI18N
        jPanel20.add(jlIconCreditor);

        jPanel26.add(jPanel20);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbCustomer.setText("Cliente");
        jbCustomer.setMargin(new java.awt.Insets(2, 7, 2, 7));
        jbCustomer.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel15.add(jbCustomer);

        jbSupplier.setText("Proveedor");
        jbSupplier.setMargin(new java.awt.Insets(2, 7, 2, 7));
        jbSupplier.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel15.add(jbSupplier);

        jbDebtor.setText("Deudor div.");
        jbDebtor.setMargin(new java.awt.Insets(2, 7, 2, 7));
        jbDebtor.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel15.add(jbDebtor);

        jbCreditor.setText("Acreedor div.");
        jbCreditor.setMargin(new java.awt.Insets(2, 7, 2, 7));
        jbCreditor.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel15.add(jbCreditor);

        jPanel26.add(jPanel15);

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

        jPanel26.add(jPanel11);

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

        jckIsAttRelatedParty.setText("Es parte relacionada");
        jckIsAttRelatedParty.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel12.add(jckIsAttRelatedParty);

        jPanel26.add(jPanel12);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlBizArea.setText("Área de negocios: *");
        jlBizArea.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlBizArea);

        jcbFkBizArea.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkBizArea.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel6.add(jcbFkBizArea);

        jPanel26.add(jPanel6);

        jPanel4.add(jPanel26, java.awt.BorderLayout.NORTH);

        jPanel19.add(jPanel4, java.awt.BorderLayout.CENTER);

        jpBizPartner.add(jPanel19, java.awt.BorderLayout.NORTH);

        jTabbedPane1.addTab("Información general", jpBizPartner);

        jpCategory.setLayout(new java.awt.BorderLayout());

        jPanel44.setLayout(new java.awt.BorderLayout());

        jPanel45.setBorder(javax.swing.BorderFactory.createTitledBorder("Información de crédito"));
        jPanel45.setLayout(new java.awt.GridLayout(12, 1, 0, 2));

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkBizPartnerCategoryId.setText("Categoría de asociado de negocios: *");
        jlFkBizPartnerCategoryId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel29.add(jlFkBizPartnerCategoryId);

        jcbFkBizPartnerCategoryId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkBizPartnerCategoryId.setEnabled(false);
        jcbFkBizPartnerCategoryId.setFocusable(false);
        jcbFkBizPartnerCategoryId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel29.add(jcbFkBizPartnerCategoryId);

        jPanel45.add(jPanel29);

        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkBizPartnerTypeId.setText("Tipo de asociado de negocios: *");
        jlFkBizPartnerTypeId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel36.add(jlFkBizPartnerTypeId);

        jcbFkBizPartnerTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
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

        jlKey.setText("Clave del asociado de negocios: *");
        jlKey.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel37.add(jlKey);

        jftKey.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        jftKey.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel37.add(jftKey);

        jPanel45.add(jPanel37);

        jPanel38.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCompanyKey.setText("Clave empresa (asignada por el AN):");
        jlCompanyKey.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel38.add(jlCompanyKey);

        jtfCompanyKey.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel38.add(jtfCompanyKey);

        jPanel45.add(jPanel38);

        jPanel39.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDummy01.setPreferredSize(new java.awt.Dimension(150, 23));
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

        jlFkCreditTypeId_n.setText("Tipo de crédito: *");
        jlFkCreditTypeId_n.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel40.add(jlFkCreditTypeId_n);

        jcbFkCreditTypeId_n.setPreferredSize(new java.awt.Dimension(200, 23));
        jcbFkCreditTypeId_n.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbFkCreditTypeId_nItemStateChanged(evt);
            }
        });
        jPanel40.add(jcbFkCreditTypeId_n);

        jPanel45.add(jPanel40);

        jPanel43.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCreditLimit.setText("Límite de crédito: *");
        jlCreditLimit.setPreferredSize(new java.awt.Dimension(150, 23));
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

        jlDaysOfCredit.setText("Días de crédito: *");
        jlDaysOfCredit.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel48.add(jlDaysOfCredit);

        jtfDaysOfCredit.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDaysOfCredit.setText("0");
        jtfDaysOfCredit.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel48.add(jtfDaysOfCredit);

        jPanel45.add(jPanel48);

        jPanel49.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDaysOfGrace.setText("Días de gracia:");
        jlDaysOfGrace.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel49.add(jlDaysOfGrace);

        jtfDaysOfGrace.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDaysOfGrace.setText("0");
        jtfDaysOfGrace.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel49.add(jtfDaysOfGrace);

        jPanel45.add(jPanel49);

        jPanel50.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkRiskTypeId.setText("Tipo de riesgo: *");
        jlFkRiskTypeId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel50.add(jlFkRiskTypeId);

        jcbFkRiskTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel50.add(jcbFkRiskTypeId);

        jPanel45.add(jPanel50);

        jPanel51.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlGuarantee.setText("Monto garantía:");
        jlGuarantee.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel51.add(jlGuarantee);

        jtfGuarantee.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfGuarantee.setText("0.00");
        jtfGuarantee.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel51.add(jtfGuarantee);

        jtfCurrencyKeyGuar.setEditable(false);
        jtfCurrencyKeyGuar.setFocusable(false);
        jtfCurrencyKeyGuar.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel51.add(jtfCurrencyKeyGuar);

        jckGuarInProcess.setText("En trámite");
        jckGuarInProcess.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel51.add(jckGuarInProcess);

        jPanel45.add(jPanel51);

        jPanel52.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlInsurance.setText("Monto seguro:");
        jlInsurance.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel52.add(jlInsurance);

        jtfInsurance.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfInsurance.setText("0.00");
        jtfInsurance.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel52.add(jtfInsurance);

        jtfCurrencyKeyInsur.setEditable(false);
        jtfCurrencyKeyInsur.setFocusable(false);
        jtfCurrencyKeyInsur.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel52.add(jtfCurrencyKeyInsur);

        jckInsurInProcess.setText("En trámite");
        jckInsurInProcess.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel52.add(jckInsurInProcess);

        jPanel45.add(jPanel52);

        jPanel44.add(jPanel45, java.awt.BorderLayout.WEST);

        jPanel46.setBorder(javax.swing.BorderFactory.createTitledBorder("Información adicional"));
        jPanel46.setLayout(new java.awt.GridLayout(12, 1, 0, 2));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkLanguageId_n.setText("Idioma predeterminado:");
        jlFkLanguageId_n.setPreferredSize(new java.awt.Dimension(175, 23));
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
        jlFkCurrencyId_n.setPreferredSize(new java.awt.Dimension(175, 23));
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

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial de operaciones: *");
        jlDateStart.setPreferredSize(new java.awt.Dimension(175, 23));
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

        jlDateEnd.setText("Fecha final de operaciones:");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel10.add(jlDateEnd);

        jftDateEnd.setText("dd/mm/yyyy");
        jftDateEnd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jftDateEnd);

        jbSetDateEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_date_day.gif"))); // NOI18N
        jbSetDateEnd.setToolTipText("Seleccionar fecha");
        jbSetDateEnd.setFocusable(false);
        jbSetDateEnd.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel10.add(jbSetDateEnd);

        jPanel46.add(jPanel10);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel46.add(jPanel14);

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCfdAddendaTypeId.setText("Tipo de addenda para CFD: *");
        jlFkCfdAddendaTypeId.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel23.add(jlFkCfdAddendaTypeId);

        jcbFkCfdAddendaTypeId.setPreferredSize(new java.awt.Dimension(208, 23));
        jPanel23.add(jcbFkCfdAddendaTypeId);

        jPanel46.add(jPanel23);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlWeb.setText("Sitio web: ");
        jlWeb.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel5.add(jlWeb);

        jtfWeb.setPreferredSize(new java.awt.Dimension(340, 23));
        jPanel5.add(jtfWeb);

        jPanel46.add(jPanel5);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNotes.setText("Notas del asociado de negocios: ");
        jlNotes.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel22.add(jlNotes);

        jtfNotes.setPreferredSize(new java.awt.Dimension(340, 23));
        jPanel22.add(jtfNotes);

        jPanel46.add(jPanel22);

        jPanel44.add(jPanel46, java.awt.BorderLayout.CENTER);

        jpCategory.add(jPanel44, java.awt.BorderLayout.NORTH);

        jpBizPartnerBranch.setBorder(javax.swing.BorderFactory.createTitledBorder("Sucursales del asociado de negocios:"));
        jpBizPartnerBranch.setPreferredSize(new java.awt.Dimension(787, 250));
        jpBizPartnerBranch.setLayout(new java.awt.BorderLayout());

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

        jpBizPartnerBranch.add(jPanel31, java.awt.BorderLayout.NORTH);

        jpCategory.add(jpBizPartnerBranch, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Información adicional", jpCategory);

        jpMarketing.setLayout(new java.awt.BorderLayout(0, 10));

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración de comercialización para clientes:"));
        jPanel21.setPreferredSize(new java.awt.Dimension(100, 250));
        jPanel21.setLayout(new java.awt.BorderLayout());

        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración del cliente:"));
        jPanel30.setPreferredSize(new java.awt.Dimension(381, 150));
        jPanel30.setLayout(new java.awt.GridLayout(8, 2, 5, 2));

        jlFkCustomerTypeId.setText("Tipo de cliente: *");
        jPanel30.add(jlFkCustomerTypeId);

        jPanel55.setLayout(new java.awt.BorderLayout(5, 5));
        jPanel55.add(jcbFkCustomerTypeId, java.awt.BorderLayout.CENTER);

        jbFkCustomerTypeId.setText("...");
        jbFkCustomerTypeId.setToolTipText("Seleccionar tipo de cliente");
        jbFkCustomerTypeId.setFocusable(false);
        jbFkCustomerTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel55.add(jbFkCustomerTypeId, java.awt.BorderLayout.EAST);

        jPanel30.add(jPanel55);

        jlFkMarketSegmentId.setText("Segmento de mercado: *");
        jPanel30.add(jlFkMarketSegmentId);

        jPanel56.setLayout(new java.awt.BorderLayout(5, 5));
        jPanel56.add(jcbFkMarketSegmentId, java.awt.BorderLayout.CENTER);

        jbFkMarketSegmentId.setText("...");
        jbFkMarketSegmentId.setToolTipText("Seleccionar segmento de mercado del cliente");
        jbFkMarketSegmentId.setFocusable(false);
        jbFkMarketSegmentId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel56.add(jbFkMarketSegmentId, java.awt.BorderLayout.EAST);

        jPanel30.add(jPanel56);

        jlFkMarketSubsegmentId.setText("Subsegmento de mercado: *");
        jPanel30.add(jlFkMarketSubsegmentId);

        jPanel57.setLayout(new java.awt.BorderLayout(5, 5));
        jPanel57.add(jcbFkMarketSubsegmentId, java.awt.BorderLayout.CENTER);

        jbFkMarketSubsegmentId.setText("...");
        jbFkMarketSubsegmentId.setToolTipText("Seleccionar subsegmento de mercado del cliente");
        jbFkMarketSubsegmentId.setFocusable(false);
        jbFkMarketSubsegmentId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel57.add(jbFkMarketSubsegmentId, java.awt.BorderLayout.EAST);

        jPanel30.add(jPanel57);

        jlFkDistributionChannelId.setText("Canal de distribución: *");
        jPanel30.add(jlFkDistributionChannelId);

        jPanel58.setLayout(new java.awt.BorderLayout(5, 5));
        jPanel58.add(jcbFkDistributionChannelId, java.awt.BorderLayout.CENTER);

        jbFkDistributionChannelId.setText("...");
        jbFkDistributionChannelId.setToolTipText("Seleccionar canal de distribución del cliente");
        jbFkDistributionChannelId.setFocusable(false);
        jbFkDistributionChannelId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel58.add(jbFkDistributionChannelId, java.awt.BorderLayout.EAST);

        jPanel30.add(jPanel58);

        jlFkSalesRouteId.setText("Ruta de ventas: *");
        jPanel30.add(jlFkSalesRouteId);

        jPanel25.setLayout(new java.awt.BorderLayout(5, 5));
        jPanel25.add(jcbFkSalesRouteId, java.awt.BorderLayout.CENTER);

        jbFkSalesRouteId.setText("jButton1");
        jbFkSalesRouteId.setFocusable(false);
        jbFkSalesRouteId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel25.add(jbFkSalesRouteId, java.awt.BorderLayout.LINE_END);

        jPanel30.add(jPanel25);

        jlFkSalesAgentId_n.setText("Agente de ventas:");
        jPanel30.add(jlFkSalesAgentId_n);

        jPanel59.setLayout(new java.awt.BorderLayout(5, 5));
        jPanel59.add(jcbFkSalesAgentId_n, java.awt.BorderLayout.CENTER);

        jbFkSalesAgentId_n.setText("...");
        jbFkSalesAgentId_n.setToolTipText("Seleccionar agente de ventas del cliente");
        jbFkSalesAgentId_n.setFocusable(false);
        jbFkSalesAgentId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel59.add(jbFkSalesAgentId_n, java.awt.BorderLayout.EAST);

        jPanel30.add(jPanel59);

        jlFkSalesSupervisorId_n.setText("Supervisor de ventas:");
        jPanel30.add(jlFkSalesSupervisorId_n);

        jPanel27.setLayout(new java.awt.BorderLayout(5, 5));
        jPanel27.add(jcbFkSalesSupervisorId_n, java.awt.BorderLayout.CENTER);

        jbFkSalesSupervisorId_n.setText("jButton1");
        jbFkSalesSupervisorId_n.setFocusable(false);
        jbFkSalesSupervisorId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel27.add(jbFkSalesSupervisorId_n, java.awt.BorderLayout.LINE_END);

        jPanel30.add(jPanel27);

        jckIsFreeDiscountDoc.setText("Sin descuento");
        jPanel30.add(jckIsFreeDiscountDoc);

        jckIsFreeCommissions.setText("Sin comisiones");
        jPanel30.add(jckIsFreeCommissions);

        jPanel21.add(jPanel30, java.awt.BorderLayout.WEST);

        jpCob.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración de las sucursales del cliente:"));
        jpCob.setPreferredSize(new java.awt.Dimension(783, 22));
        jpCob.setLayout(new java.awt.BorderLayout());

        jpNotesAction.setPreferredSize(new java.awt.Dimension(771, 23));
        jpNotesAction.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jbNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbNew.setToolTipText("Crear");
        jbNew.setEnabled(false);
        jbNew.setFocusable(false);
        jbNew.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesAction.add(jbNew);

        jbEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEdit.setToolTipText("Modificar");
        jbEdit.setFocusable(false);
        jbEdit.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesAction.add(jbEdit);

        jbDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbDel.setToolTipText("Eliminar");
        jbDel.setFocusable(false);
        jbDel.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesAction.add(jbDel);

        jbAddCob.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_insert.gif"))); // NOI18N
        jbAddCob.setToolTipText("Agregar sucursales");
        jbAddCob.setFocusable(false);
        jbAddCob.setPreferredSize(new java.awt.Dimension(23, 23));
        jpNotesAction.add(jbAddCob);

        jpCob.add(jpNotesAction, java.awt.BorderLayout.PAGE_START);

        jPanel21.add(jpCob, java.awt.BorderLayout.CENTER);

        jpMarketing.add(jPanel21, java.awt.BorderLayout.NORTH);

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

        setSize(new java.awt.Dimension(1032, 678));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivate();
    }//GEN-LAST:event_formWindowActivated

    private void jcbFkBizPartnerIdentityTypeIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbFkBizPartnerIdentityTypeIdItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            itemStateChangedBizPartenrIdentityType();
        }
    }//GEN-LAST:event_jcbFkBizPartnerIdentityTypeIdItemStateChanged

    private void jftAlternativeIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jftAlternativeIdFocusLost
        focusLostAlternativeId();
    }//GEN-LAST:event_jftAlternativeIdFocusLost

    private void jcbFkCreditTypeId_nItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbFkCreditTypeId_nItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED && jckIsCreditByUser.isSelected()) {
            itemStateChangedCreditLimit();
        }
}//GEN-LAST:event_jcbFkCreditTypeId_nItemStateChanged

    private void jckIsCreditByUserItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jckIsCreditByUserItemStateChanged
         itemStateChangedIsCreditApplying();
}//GEN-LAST:event_jckIsCreditByUserItemStateChanged

    private void jckIsAttEmployeeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jckIsAttEmployeeItemStateChanged
       itemStateChangedIsAttEmployee();
    }//GEN-LAST:event_jckIsAttEmployeeItemStateChanged

    private void jcbFkBizPartnerTypeIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbFkBizPartnerTypeIdItemStateChanged
        renderBussinesPartnerType();
    }//GEN-LAST:event_jcbFkBizPartnerTypeIdItemStateChanged
    
    private void itemStateChangedCreditLimit() {
            renderCreditLimit();    
    }

    private void itemStateChangedIsCreditApplying() {
        boolean enable = jckIsCreditByUser.isSelected();
        jcbFkCreditTypeId_n.setEnabled(enable);
        jcbFkRiskTypeId.setEnabled(enable);
        renderBussinesPartnerType();
        renderCreditLimit();
    }

    private void itemStateChangedIsAttEmployee() {
        if (jckIsAttEmployee.isEnabled()) {
            mnFormTypeExport = SDataConstants.BPSX_BP_EMP;
            mbIsNeededPosSave = jckIsAttEmployee.isSelected();
        }
    }

    private void actionSetDateStart() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldDateStart.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldDateStart.setFieldValue(miClient.getGuiDatePickerXXX().getGuiDate());
            jftDateStart.requestFocus();
        }
    }

    private void actionSetDateEnd() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldDateEnd.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldDateEnd.setFieldValue(miClient.getGuiDatePickerXXX().getGuiDate());
            jftDateEnd.requestFocus();
        }
    }

    private void initComponentsExtra() {
        int i;

        mvFields = new Vector<>();
        mvFieldsCategory = new Vector<>();
        mvFieldsCusConfig = new Vector<>();

        moIconHasCategorySup = new ImageIcon(getClass().getResource("/erp/img/bp_sup.png"));
        moIconHasCategoryCus = new ImageIcon(getClass().getResource("/erp/img/bp_cus.png"));
        moIconHasCategoryCdr = new ImageIcon(getClass().getResource("/erp/img/bp_cdr.png"));
        moIconHasCategoryDbr = new ImageIcon(getClass().getResource("/erp/img/bp_dbr.png"));
        moIconHasNotCategory = new ImageIcon(getClass().getResource("/erp/img/bp_bw.png"));

        moComboBoxBizPartnerIdentity = new SFormComboBoxGroup(miClient);
        moComboboxGrpMarketSegment = new SFormComboBoxGroup(miClient);

        moCustomerConfigCobPane = new STablePane(miClient);
        moCustomerConfigCobPane.setDoubleClickAction(this, "publicActionConfigCobEdit");
        jpCob.add(moCustomerConfigCobPane, BorderLayout.CENTER);

        erp.lib.table.STableColumnForm tableColumnsBizPartnerBranch[];
        erp.lib.table.STableColumnForm tableColumnsCob[];

        moPanelBizPartnerBranch = new SPanelBizPartnerBranch(miClient);
        jpBizPartner.add(moPanelBizPartnerBranch, BorderLayout.CENTER);

        moBizPartnerBranchPane = new STablePane(miClient);
        moBizPartnerBranchPane.setDoubleClickAction(this, "publicActionModify");
        jpBizPartnerBranch.add(moBizPartnerBranchPane, BorderLayout.CENTER);

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
        moFieldFiscalId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfFiscalId, jlFiscalId);
        moFieldFiscalId.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFiscalId.setLengthMax(25);
        moFieldFiscalId.setAutoCaseType(SLibConstants.CASE_UPPER);
        moFieldFiscalFrgId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfFiscalFrgId, jlFiscalFrgId);
        moFieldFiscalFrgId.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFiscalFrgId.setLengthMax(25);
        moFieldFiscalFrgId.setAutoCaseType(SLibConstants.CASE_UPPER);
        moFieldAlternativeId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jftAlternativeId, jlAlternativeId);
        moFieldAlternativeId.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldAlternativeId.setLengthMax(25);
        moFieldAlternativeId.setAutoCaseType(SLibConstants.CASE_UPPER);
        moFieldFkTaxIdentityTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTaxIdentityTypeId, jlFkTaxIdentityTypeId);
        moFieldFkTaxIdentityTypeId.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDeleted);
        moFieldIsDeleted.setTabbedPaneIndex(0, jTabbedPane1);
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
        moFieldIsAttRelatedParty = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsAttRelatedParty);
        moFieldIsAttRelatedParty.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFkBizArea = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkBizArea, jlBizArea);
        moFieldFkBizArea.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldWeb = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfWeb, jlWeb);
        moFieldWeb.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldWeb.setLengthMax(100);
        moFieldWeb.setAutoCaseType(SLibConstants.UNDEFINED);
        moFieldNotes = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfNotes, jlNotes);
        moFieldNotes.setLengthMax(255);

        moFieldFkBizPartnerCategoryId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkBizPartnerCategoryId, jlFkBizPartnerCategoryId);
        moFieldFkBizPartnerCategoryId.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkBizPartnerTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkBizPartnerTypeId, jlFkBizPartnerTypeId);
        moFieldFkBizPartnerTypeId.setPickerButton(jbFkBizPartnerTypeId);
        moFieldFkBizPartnerTypeId.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldKey = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jftKey, jlKey);
        moFieldKey.setLengthMax(25);
        moFieldCompanyKey = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfCompanyKey, jlCompanyKey);
        moFieldCompanyKey.setLengthMax(25);
        moFieldFkCreditTypeId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCreditTypeId_n, jlFkCreditTypeId_n);
        moFieldFkCreditTypeId_n.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkRiskTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkRiskTypeId, jlFkRiskTypeId);
        moFieldFkRiskTypeId.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldGuaranteeAmount = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfGuarantee, jlGuarantee);
        moFieldGuaranteeAmount.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldIsInGuarProcess  = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckGuarInProcess);
        moFieldIsInGuarProcess.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldInsuranceAmount = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfInsurance, jlInsurance);
        moFieldInsuranceAmount.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldIsInInsurProcess  = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckInsurInProcess);
        moFieldIsInInsurProcess.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldCreditLimit = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfCreditLimit, jlCreditLimit);
        moFieldCreditLimit.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldDaysOfCredit = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfDaysOfCredit, jlDaysOfCredit);
        moFieldDaysOfCredit.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldDaysOfGrace = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfDaysOfGrace, jlDaysOfGrace);
        moFieldIsCreditByUser = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsCreditByUser);
        moFieldIsCreditByUser.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkLanguageId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkLanguageId_n, jlFkLanguageId_n);
        moFieldFkLanguageId.setPickerButton(jbFkLanguageId_n);
        moFieldFkCurrencyId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkCurrencyId_n, jlFkCurrencyId_n);
        moFieldFkCurrencyId.setPickerButton(jbFkCurrencyId_n);
        moFieldDateStart = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateStart, jlDateStart);
        moFieldDateStart.setPickerButton(jbSetDateStart);
        moFieldDateStart.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldDateEnd = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftDateEnd, jlDateEnd);
        moFieldDateEnd.setPickerButton(jbSetDateEnd);
        moFieldDateEnd.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkCfdAddendaTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCfdAddendaTypeId, jlFkCfdAddendaTypeId);
        moFieldFkCfdAddendaTypeId.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldIsCategoryDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsCategoryDeleted);
        moFieldIsCategoryDeleted.setTabbedPaneIndex(0, jTabbedPane1);

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

        mvFields.add(moFieldFkBizPartnerIdentityTypeId);
        mvFields.add(moFieldBizPartnerCommercial);
        mvFields.add(moFieldIsAttBank);
        mvFields.add(moFieldIsAttCarrier);
        mvFields.add(moFieldIsAttSalesAgent);
        mvFields.add(moFieldIsAttEmployee);
        mvFields.add(moFieldIsAttPartnerShareholder);
        mvFields.add(moFieldIsAttRelatedParty);
        mvFields.add(moFieldFkBizArea);
        mvFields.add(moFieldWeb);
        mvFields.add(moFieldNotes);

        mvFieldsCategory.add(moFieldFkBizPartnerCategoryId);
        mvFieldsCategory.add(moFieldFkBizPartnerTypeId);
        mvFieldsCategory.add(moFieldKey);
        mvFieldsCategory.add(moFieldCompanyKey);
        mvFieldsCategory.add(moFieldFkCreditTypeId_n);
        mvFieldsCategory.add(moFieldCreditLimit);
        mvFieldsCategory.add(moFieldDaysOfCredit);
        mvFieldsCategory.add(moFieldDaysOfGrace);
        mvFieldsCategory.add(moFieldFkRiskTypeId);
        mvFieldsCategory.add(moFieldGuaranteeAmount);
        mvFieldsCategory.add(moFieldInsuranceAmount);
        mvFieldsCategory.add(moFieldIsInGuarProcess);
        mvFieldsCategory.add(moFieldIsInInsurProcess);
        mvFieldsCategory.add(moFieldIsCreditByUser);
        mvFieldsCategory.add(moFieldFkLanguageId);
        mvFieldsCategory.add(moFieldFkCurrencyId);
        mvFieldsCategory.add(moFieldDateStart);
        mvFieldsCategory.add(moFieldDateEnd);
        mvFieldsCategory.add(moFieldFkCfdAddendaTypeId);
        mvFieldsCategory.add(moFieldIsCategoryDeleted);

        mvFieldsCusConfig.add(moFieldFkCustomerTypeId);
        mvFieldsCusConfig.add(moFieldFkMarketSegmentId);
        mvFieldsCusConfig.add(moFieldFkMarketSubsegmentId);
        mvFieldsCusConfig.add(moFieldFkDistributionChannelId);
        mvFieldsCusConfig.add(moFieldFkSalesRouteId);
        mvFieldsCusConfig.add(moFieldFkSalesAgentId_n);
        mvFieldsCusConfig.add(moFieldFkSalesSupervisorId_n);
        mvFieldsCusConfig.add(moFieldIsFreeDiscountDoc);
        mvFieldsCusConfig.add(moFieldIsFreeCommissions);

        moFormBizPartnerBranch = new SFormBizPartnerBranch(miClient);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbAddBranch.addActionListener(this);
        jbModifyBranch.addActionListener(this);
        jbRecreateBizPartnerCommercial.addActionListener(this);

        jbSetDateStart.addActionListener(this);
        jbSetDateEnd.addActionListener(this);
        jbCancel.addActionListener(this);
        jbFkLanguageId_n.addActionListener(this);
        jbFkCurrencyId_n.addActionListener(this);
        jbEditLanguage.addActionListener(this);
        jbEditCurrency.addActionListener(this);
        jbFkBizPartnerTypeId.addActionListener(this);

        jbFkCustomerTypeId.addActionListener(this);
        jbFkSalesRouteId.addActionListener(this);
        jbFkSalesAgentId_n.addActionListener(this);
        jbFkSalesSupervisorId_n.addActionListener(this);
        jbFkMarketSegmentId.addActionListener(this);
        jbFkMarketSubsegmentId.addActionListener(this);
        jbFkDistributionChannelId.addActionListener(this);
        jbAddCob.addActionListener(this);
        jbEdit.addActionListener(this);
        jbDel.addActionListener(this);

        jbSupplier.addActionListener(this);
        jbCustomer.addActionListener(this);
        jbCreditor.addActionListener(this);
        jbDebtor.addActionListener(this);

        i = 0;
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

        SFormUtilities.createActionMap(rootPane, this, "publicActionAdd", "add", KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionModify", "modify", KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionDelete", "delete", KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK);

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
            moCustomerConfigCobPane.addTableColumn(tableColumnsCob[i]);
        }
        moCustomerConfigCobPane.createTable(null);

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
            jcbFkBizPartnerIdentityTypeId.requestFocus();
        }
    }

    private void actionOk() {
        erp.lib.form.SFormValidation validation = formValidate();

        if (validation.getIsError()) {
            if (validation.getTabbedPaneIndex() >= 0) {
                jTabbedPane1.setSelectedIndex(validation.getTabbedPaneIndex());
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

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void renderBizPartnerSettings() {
        if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER) {
            jtfFirstName.setEnabled(true);
            jtfLastName.setEnabled(true);
            jftAlternativeId.setEnabled(true);
            jtfBizPartner.setEnabled(false);
            moFieldAlternativeId.setMaskFormatter("UUUU######XXXXXXXX");
        }
        else {
            jtfFirstName.setEnabled(false);
            jtfLastName.setEnabled(false);
            jftAlternativeId.setEnabled(false);
            jtfBizPartner.setEnabled(true);
        }
    }

    private void renderBizPartnerCategories() {
        if (mnParamBizPartnerType == SDataConstants.BPSU_BP) {
        }
        else if (mnParamBizPartnerType == SDataConstants.BPSX_BP_SUP) {
            setFieldEnableCategory(true);
            jbSupplier.setEnabled(false);
            jbCustomer.setEnabled(true);
            jbCreditor.setEnabled(true);
            jbDebtor.setEnabled(true);
            jTabbedPane1.setEnabledAt(2, false);
            setParamBizPartnerCategoryFilter(SDataConstantsSys.BPSS_CT_BP_SUP);
        }
        else if (mnParamBizPartnerType == SDataConstants.BPSX_BP_CUS) {
            setFieldEnableCategory(true);
            jbSupplier.setEnabled(true);
            jbCustomer.setEnabled(false);
            jbCreditor.setEnabled(true);
            jbDebtor.setEnabled(true);
            jTabbedPane1.setEnabledAt(2, true);
            setParamBizPartnerCategoryFilter(SDataConstantsSys.BPSS_CT_BP_CUS);
        }
        else if (mnParamBizPartnerType == SDataConstants.BPSX_BP_CO) {
            setFieldEnableCategory(false);
            jTabbedPane1.setEnabledAt(2, false);
            jbAddBranch.setEnabled(true);
            setParamBizPartnerCategoryFilter(SDataConstantsSys.BPSS_CT_BP_CO);
        }
        setBizPartnerCategory();
        setIconCategory();
    }

    private void setFieldEnableCategory(boolean enable) {
        jlFkBizPartnerCategoryId.setEnabled(enable);
        jlFkBizPartnerTypeId.setEnabled(enable);
        jlCompanyKey.setEnabled(enable);
        jlKey.setEnabled(enable);
        jckIsCreditByUser.setEnabled(enable);
        jlDateStart.setEnabled(enable);
        jlDateEnd.setEnabled(enable);
        jlWeb.setEnabled(enable);
        jlNotes.setEnabled(enable);

        jcbFkBizPartnerTypeId.setEnabled(enable);
        jbFkBizPartnerTypeId.setEnabled(enable);
        jtfCompanyKey.setEnabled(enable);
        jftKey.setEnabled(enable);
        jcbFkLanguageId_n.setEnabled(enable);
        jbFkLanguageId_n.setEnabled(enable);
        jbEditLanguage.setEnabled(enable);
        jcbFkCurrencyId_n.setEnabled(enable);
        jbFkCurrencyId_n.setEnabled(enable);
        jbEditCurrency.setEnabled(enable);
        jftDateStart.setEnabled(enable);
        jftDateEnd.setEnabled(enable);
        jbSetDateStart.setEnabled(enable);
        jbSetDateEnd.setEnabled(enable);
        jtfWeb.setEnabled(enable);
        jtfNotes.setEnabled(enable);
    }
    
    private void setIconCategory() {
        jlIconSupplier.setIcon(moBizPartner == null ? mnParamBizPartnerType == SDataConstants.BPSX_BP_SUP ? moIconHasCategorySup : moIconHasNotCategory : moBizPartner.getIsSupplier() || mnParamBizPartnerType == SDataConstants.BPSX_BP_SUP ? moIconHasCategorySup : moIconHasNotCategory);
        jlIconCustomer.setIcon(moBizPartner == null ? mnParamBizPartnerType == SDataConstants.BPSX_BP_CUS ? moIconHasCategoryCus : moIconHasNotCategory : moBizPartner.getIsCustomer() || mnParamBizPartnerType == SDataConstants.BPSX_BP_CUS ? moIconHasCategoryCus : moIconHasNotCategory);
        jlIconCreditor.setIcon(moBizPartner == null ? mnParamBizPartnerType == SDataConstants.BPSX_BP_CDR ? moIconHasCategoryCdr : moIconHasNotCategory : moBizPartner.getIsCreditor() || mnParamBizPartnerType == SDataConstants.BPSX_BP_CDR ? moIconHasCategoryCdr : moIconHasNotCategory);
        jlIconDebtor.setIcon(moBizPartner == null ? mnParamBizPartnerType == SDataConstants.BPSX_BP_DBR ? moIconHasCategoryDbr : moIconHasNotCategory : moBizPartner.getIsDebtor() || mnParamBizPartnerType == SDataConstants.BPSX_BP_DBR ? moIconHasCategoryDbr : moIconHasNotCategory);
    }

    private void setBizPartnerCategory() {
        SFormUtilities.populateComboBox(miClient, jcbFkBizPartnerCategoryId, SDataConstants.BPSS_CT_BP);
        if (mnParamBizPartnerType == SDataConstants.BPSU_BP) {
            moFieldFkBizPartnerCategoryId.setFieldValue(new int[] {SDataConstants.BPSX_BPB_CON_SUP});
        }
        else if (mnParamBizPartnerType == SDataConstants.BPSX_BP_SUP) {
            moFieldFkBizPartnerCategoryId.setKey(new int [] {SDataConstantsSys.BPSS_CT_BP_SUP});
         }
        else if (mnParamBizPartnerType == SDataConstants.BPSX_BP_CUS) {
            moFieldFkBizPartnerCategoryId.setKey(new int [] {SDataConstantsSys.BPSS_CT_BP_CUS});
        }
    }

    private void setCategoryCompanySettings() {
        moBizPartnerCategory.setPkBizPartnerCategoryId(1);
        moBizPartnerCategory.setKey(moFieldKey.getString());
        moBizPartnerCategory.setCompanyKey(moFieldCompanyKey.getString());
        moBizPartnerCategory.setCreditLimit(0);
        moBizPartnerCategory.setDaysOfCredit(0);
        moBizPartnerCategory.setDaysOfGrace(0);
        moBizPartnerCategory.setGuarantee(0);
        moBizPartnerCategory.setInsurance(0);
        moBizPartnerCategory.setIsGuaranteeInProcess(jckGuarInProcess.isSelected());
        moBizPartnerCategory.setIsInsuranceInProcess(jckInsurInProcess.isSelected());
        moBizPartnerCategory.setDateStart(moFieldDateStart.getDate());
        moBizPartnerCategory.setDateEnd_n(moFieldDateEnd.getDate());
        moBizPartnerCategory.setIsCreditByUser(jckIsCreditByUser.isSelected());
        moBizPartnerCategory.setFkBizPartnerCategoryId(1);
        moBizPartnerCategory.setFkBizPartnerTypeId(1);
        moBizPartnerCategory.setFkCreditTypeId_n(1);
        moBizPartnerCategory.setFkCfdAddendaTypeId(1);

        moBizPartnerCategory.setDbmBizPartnerType("");
        moBizPartnerCategory.setDbmsCreditType("");
        moBizPartnerCategory.setDbmsLanguage("");
        moBizPartnerCategory.setDbmsCurrency("");
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

    private void actionCustomerConfigCobEdit() {
        int index = moCustomerConfigCobPane.getTable().getSelectedRow();
        SFormCustomerConfigurationCob formCustomerConfigCob = new SFormCustomerConfigurationCob(miClient);
        SDataCustomerBranchConfig dataCustomerConfigCob = null;
        SDataCustomerBranchConfigRow dataCustomerConfigCobRow = null;

        formCustomerConfigCob.formRefreshCatalogues();
        formCustomerConfigCob.formReset();
        if (index != -1) {
            dataCustomerConfigCob = (SDataCustomerBranchConfig) moCustomerConfigCobPane.getTableRow(index).getData();
            formCustomerConfigCob.setRegistry(dataCustomerConfigCob);
            formCustomerConfigCob.setVisible(true);
            if (formCustomerConfigCob.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                dataCustomerConfigCob = (SDataCustomerBranchConfig) formCustomerConfigCob.getRegistry();

                moBizPartner.getDbmsBizPartnerBranches().get(index + 1).getDbmsDataCustomerBranchConfig().add(dataCustomerConfigCob);
                moCustomerConfigCobPane.setTableRow(dataCustomerConfigCobRow = new SDataCustomerBranchConfigRow(dataCustomerConfigCob), index);
                moCustomerConfigCobPane.renderTableRows();
            }
        }
    }

    private void actionAddBranch() {
        SDataBizPartnerBranch dataBranch = null;

        if (jbAddBranch.isEnabled()) {
            moFormBizPartnerBranch.setParamBizPartnerDescription(moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER ?
            moFieldLastName.getString() + ", " + moFieldFirstName.getString() : moFieldBizPartner.getString());
            moFormBizPartnerBranch.formRefreshCatalogues();
            moFormBizPartnerBranch.formReset();
            moFormBizPartnerBranch.setVisible(true);

            if (moFormBizPartnerBranch.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                dataBranch = (SDataBizPartnerBranch) moFormBizPartnerBranch.getRegistry();

                dataBranch.setDbmsUserNew(miClient.getSessionXXX().getUser().getUser());
                dataBranch.setUserNewTs(miClient.getSessionXXX().getSystemDate());
                dataBranch.setDbmsUserEdit("(n/a)");
                dataBranch.setUserEditTs(miClient.getSessionXXX().getSystemDate());
                dataBranch.setDbmsUserDelete("(n/a)");
                dataBranch.setUserDeleteTs(miClient.getSessionXXX().getSystemDate());

                moBizPartnerBranchPane.addTableRow(new SDataBizPartnerBranchRow(dataBranch));
                moBizPartnerBranchPane.renderTableRows();
                moBizPartnerBranchPane.setTableRowSelection(moBizPartnerBranchPane.getTableGuiRowCount() - 1);
            }
        }
    }

    private void actionModifyBranch() {
        int index = moBizPartnerBranchPane.getTable().getSelectedRow();
        SDataBizPartnerBranch dataBranch = null;

        if (jbModifyBranch.isEnabled()) {
            moFormBizPartnerBranch.setParamBizPartnerDescription(moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER ?
            moFieldLastName.getString() + ", " + moFieldFirstName.getString() : moFieldBizPartner.getString());
            moFormBizPartnerBranch.formRefreshCatalogues();
            moFormBizPartnerBranch.formReset();

            if (index != -1) {
                dataBranch = (SDataBizPartnerBranch) moBizPartnerBranchPane.getTableRow(index).getData();
                moFormBizPartnerBranch.setRegistry(dataBranch);
                moFormBizPartnerBranch.setVisible(true);

                if (moFormBizPartnerBranch.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                    dataBranch = (erp.mbps.data.SDataBizPartnerBranch) moFormBizPartnerBranch.getRegistry();
                    dataBranch.setDbmsUserEdit(miClient.getSessionXXX().getUser().getUser());
                    dataBranch.setUserEditTs(miClient.getSessionXXX().getSystemDate());

                    moBizPartnerBranchPane.getTableModel().getTableRows().set(index, new SDataBizPartnerBranchRow(dataBranch));
                    moBizPartnerBranchPane.renderTableRows();
                    moBizPartnerBranchPane.setTableRowSelection(index);
                }
            }
        }
    }

    private void actionAddCob() {
        int index = 0;
        SDataCustomerBranchConfig cusConfigCob = null;
        SDataCustomerBranchConfigRow cusConfigRow = null;
        SDataBizPartnerBranch dataBranch = null;

        if (moBizPartnerBranchPane.getTableGuiRowCount() > 0 && moCustomerConfigCobPane.getTableGuiRowCount() < moBizPartnerBranchPane.getTableGuiRowCount()) {
            moCustomerConfigCobPane.clearTableRows();
            for (int i = 0; i < moBizPartnerBranchPane.getTableGuiRowCount(); i++) {
                dataBranch = ((SDataBizPartnerBranch) moBizPartnerBranchPane.getTableRow(i).getData());
                if (dataBranch.getDbmsDataCustomerBranchConfig().size() > 0) {
                    moBizPartner.getDbmsBizPartnerBranches().get(i + 1).getDbmsDataCustomerBranchConfig().get(0).setIsDeleted(false);
                    moCustomerConfigCobPane.addTableRow(cusConfigRow = new SDataCustomerBranchConfigRow(dataBranch.getDbmsDataCustomerBranchConfig().get(0)));
                }
                else {
                    if (dataBranch.getPkBizPartnerBranchId() != SDataConstantsSys.UNDEFINED) {
                        cusConfigCob = new SDataCustomerBranchConfig();
                        cusConfigCob.setDbmsCob(dataBranch.getBizPartnerBranch());
                        cusConfigCob.setDbmsSalesRoute("");
                        cusConfigCob.setDbmsSalesAgent("");
                        cusConfigCob.setFkSalesRouteId(1);
                        cusConfigCob.setFkSalesAgentId_n(0);
                        cusConfigCob.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                        cusConfigCob.setFkUserEditId(1);
                        cusConfigCob.setFkUserDeleteId(1);
                        cusConfigCob.setDbmsUserNew(miClient.getSessionXXX().getUser().getUser());
                        cusConfigCob.setDbmsUserEdit("(n/a)");
                        cusConfigCob.setDbmsUserDelete("(n/a)");
                        cusConfigCob.setUserNewTs(miClient.getSessionXXX().getWorkingDate());

                        moCustomerConfigCobPane.addTableRow(cusConfigRow = new SDataCustomerBranchConfigRow(cusConfigCob));
                    }
                }
                moCustomerConfigCobPane.renderTableRows();
                index = moCustomerConfigCobPane.getTableGuiRowCount() - 1;
                moCustomerConfigCobPane.getTable().setRowSelectionInterval(index, index);
                moCustomerConfigCobPane.getVerticalScrollBar().setValue((index + 1) * moCustomerConfigCobPane.getTable().getRowHeight());
            }
        }
    }

    private void actionDelCob() {
        int nPosition = 0;
        int nPkCustomerVector = 0;
        int nPkCustomerGrid = 0;
        SDataCustomerBranchConfig cusConfigCob = null;
        int index = moCustomerConfigCobPane.getTable().getSelectedRow();

        if (index != -1) {
            if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                cusConfigCob = (SDataCustomerBranchConfig) moCustomerConfigCobPane.getTableRow(index).getData();
                if (cusConfigCob.getDbmsSalesAgent().length() > 0) {
                    for (int i = 0; i<moBizPartner.getDbmsBizPartnerBranches().size(); i++) {
                        nPkCustomerVector = moBizPartner.getDbmsBizPartnerBranches().get(i).getDbmsDataCustomerBranchConfig().get(0).getPkCustomerBranchId();
                        nPkCustomerGrid = cusConfigCob.getPkCustomerBranchId();
                        if (nPkCustomerVector == nPkCustomerGrid) {
                            nPosition = i;
                            break;
                        }
                    }
                    moBizPartner.getDbmsBizPartnerBranches().get(nPosition).getDbmsDataCustomerBranchConfig().get(0).setIsDeleted(true);
                }
                moCustomerConfigCobPane.removeTableRow(index);
                moCustomerConfigCobPane.renderTableRows();

                if (moCustomerConfigCobPane.getTableGuiRowCount() > 0) {
                    moCustomerConfigCobPane.setTableRowSelection(index < moCustomerConfigCobPane.getTableGuiRowCount() ? index : moCustomerConfigCobPane.getTableGuiRowCount() - 1);
                }
            }
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

    private void actionRecreateBizPartnerCommercial() {
        jtfBizPartnerCommercial.setText("");
        if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER) {
            jtfBizPartnerCommercial.setText(jtfLastName.getText() + ", " + jtfFirstName.getText());
            jtfBizPartnerCommercial.requestFocus();
        }
        else {
            jtfBizPartnerCommercial.setText(jtfBizPartner.getText());
            jtfBizPartnerCommercial.requestFocus();
        }
    }

    private void focusLostFiscalId() {
        if (jtfFiscalId.getText().trim().length() > 0) {
            jtfFiscalId.setText(jtfFiscalId.getText().toUpperCase());
        }
        else {
            jtfFiscalId.setText("");
        }
    }

    private void focusLostAlternativeId() {
        if (jftAlternativeId.getText().trim().length() > 0) {
            jftAlternativeId.setText(jftAlternativeId.getText().toUpperCase());
        }
        else {
            jftAlternativeId.setText("");
        }
    }

    private void itemStateChangedBizPartenrIdentityType() {
        renderBizPartnerSettings();
        renderTaxIdentity();
        renderBizPartnerCategories();
        renderKey();
    }

    private void renderTaxIdentity() {
        if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] > 0) {
            jcbFkTaxIdentityTypeId.setEnabled(true);
            SFormUtilities.populateComboBox(miClient, jcbFkTaxIdentityTypeId, SDataConstants.FINU_TAX_IDY, moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray());
            if (jcbFkTaxIdentityTypeId.getItemCount() <= 2) {
                jcbFkTaxIdentityTypeId.setSelectedIndex(jcbFkTaxIdentityTypeId.getItemCount() - 1);
            }
        }
        else {
            jcbFkTaxIdentityTypeId.setEnabled(false);
            jcbFkTaxIdentityTypeId.removeAllItems();
        }
    }

    public void publicActionConfigCobEdit() {
        actionCustomerConfigCobEdit();
    }

    public void publicActionAdd() {
        if (jTabbedPane1.getSelectedIndex() == 1 && jbAddBranch.isEnabled()) {
            actionAddBranch();
        }
    }

    public void publicActionModify() {
        if (jTabbedPane1.getSelectedIndex() == 1 && jbModifyBranch.isEnabled()) {
            actionModifyBranch();
        }
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
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
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
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jbAddBranch;
    private javax.swing.JButton jbAddCob;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbCreditor;
    private javax.swing.JButton jbCustomer;
    private javax.swing.JButton jbDebtor;
    private javax.swing.JButton jbDel;
    private javax.swing.JButton jbEdit;
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
    private javax.swing.JButton jbNew;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbRecreateBizPartnerCommercial;
    private javax.swing.JButton jbSetDateEnd;
    private javax.swing.JButton jbSetDateStart;
    private javax.swing.JButton jbSupplier;
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
    private javax.swing.JComboBox<SFormComponentItem> jcbFkRiskTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSalesAgentId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSalesRouteId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSalesSupervisorId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkTaxIdentityTypeId;
    private javax.swing.JCheckBox jckGuarInProcess;
    private javax.swing.JCheckBox jckInsurInProcess;
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
    private javax.swing.JFormattedTextField jftAlternativeId;
    private javax.swing.JFormattedTextField jftDateEnd;
    private javax.swing.JFormattedTextField jftDateStart;
    private javax.swing.JFormattedTextField jftKey;
    private javax.swing.JLabel jlAlternativeId;
    private javax.swing.JLabel jlBizArea;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlBizPartnerCommercial;
    private javax.swing.JLabel jlCompanyKey;
    private javax.swing.JLabel jlCreditLimit;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlDaysOfCredit;
    private javax.swing.JLabel jlDaysOfGrace;
    private javax.swing.JLabel jlDummy01;
    private javax.swing.JLabel jlDummy1;
    private javax.swing.JLabel jlDummy2;
    private javax.swing.JLabel jlDummy3;
    private javax.swing.JLabel jlDummy4;
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
    private javax.swing.JLabel jlFkRiskTypeId;
    private javax.swing.JLabel jlFkSalesAgentId_n;
    private javax.swing.JLabel jlFkSalesRouteId;
    private javax.swing.JLabel jlFkSalesSupervisorId_n;
    private javax.swing.JLabel jlFkTaxIdentityTypeId;
    private javax.swing.JLabel jlGuarantee;
    private javax.swing.JLabel jlIconCreditor;
    private javax.swing.JLabel jlIconCustomer;
    private javax.swing.JLabel jlIconDebtor;
    private javax.swing.JLabel jlIconSupplier;
    private javax.swing.JLabel jlInsurance;
    private javax.swing.JLabel jlKey;
    private javax.swing.JLabel jlLastName;
    private javax.swing.JLabel jlNotes;
    private javax.swing.JLabel jlWeb;
    private javax.swing.JPanel jpBizPartner;
    private javax.swing.JPanel jpBizPartnerBranch;
    private javax.swing.JPanel jpCategory;
    private javax.swing.JPanel jpCob;
    private javax.swing.JPanel jpMarketing;
    private javax.swing.JPanel jpNotesAction;
    private javax.swing.JTextField jtfBizPartner;
    private javax.swing.JTextField jtfBizPartnerCommercial;
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
    private javax.swing.JTextField jtfNotes;
    private javax.swing.JTextField jtfPkBizPartnerId_Ro;
    private javax.swing.JTextField jtfWeb;
    // End of variables declaration//GEN-END:variables

    private void renderKey() {
        switch (mnParamBizPartnerCategoryFilter) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
                if (miClient.getSessionXXX().getParamsErp().getIsKeySupplierApplying()) {
                    jftKey.setEnabled(true);
                }
                else {
                    jftKey.setEnabled(false);
                }
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
                if (miClient.getSessionXXX().getParamsErp().getIsKeyCustomerApplying()) {
                    jftKey.setEnabled(true);
                }
                else {
                    jftKey.setEnabled(false);
                }
                break;
            default:
        }
    }

    private void renderCreditLimit() {
        if ((moFieldFkCreditTypeId_n.getKeyAsIntArray()[0] == 0 || moFieldFkCreditTypeId_n.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_CRED_CRED_NO) || 
                !jckIsCreditByUser.isSelected()) {
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
            moFieldCreditLimit.setDouble(0d);
            jtfDaysOfCredit.setEditable(true);
            jtfDaysOfCredit.setFocusable(true);
            jtfDaysOfGrace.setEditable(true);
            jtfDaysOfGrace.setFocusable(true);
        }
        else {
            jtfCreditLimit.setEditable(true);
            jtfCreditLimit.setFocusable(true);
            jtfDaysOfCredit.setEditable(true);
            jtfDaysOfCredit.setFocusable(true);
            jtfDaysOfGrace.setEditable(true);
            jtfDaysOfGrace.setFocusable(true);
        }
    }

     private void renderCategorySettings() {
        if (moFieldFkBizPartnerCategoryId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_CT_BP_SUP ||
                moFieldFkBizPartnerCategoryId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_CT_BP_CUS) {
            jlFkCreditTypeId_n.setEnabled(true);
            jlCreditLimit.setEnabled(true);
            jlDaysOfCredit.setEnabled(true);
            jlDaysOfGrace.setEnabled(true);
            jlFkRiskTypeId.setEnabled(true);
            jcbFkCreditTypeId_n.setEnabled(true);
            jlFkCfdAddendaTypeId.setEnabled(true);
            jckIsCreditByUser.setEnabled(true);
            jcbFkLanguageId_n.setEnabled(true);
            jbFkLanguageId_n.setEnabled(true);
            jbAddBranch.setEnabled(true);
        }
        else if (mnParamBizPartnerType == SDataConstants.BPSX_BP_CO) {
            jbAddBranch.setEnabled(true);
        }
        else {
            jlCreditLimit.setEnabled(false);
            jlDaysOfCredit.setEnabled(false);
            jlDaysOfGrace.setEnabled(false);
            jlFkRiskTypeId.setEnabled(true);
            jcbFkCreditTypeId_n.setEnabled(false);
            jlFkCfdAddendaTypeId.setEnabled(false);
            jckIsCreditByUser.setEnabled(false);
            jcbFkLanguageId_n.setEnabled(false);
            jbFkLanguageId_n.setEnabled(false);
            jbAddBranch.setEnabled(false);

            moFieldFkCreditTypeId_n.setFieldValue(new int[] { SDataConstantsSys.BPSS_TP_CRED_CRED_NO });
            moFieldFkRiskTypeId.setFieldValue(new int[] { SModSysConsts.BPSS_RISK_C_RSK_H });
            moFieldGuaranteeAmount.setFieldValue(0d);
            moFieldInsuranceAmount.setFieldValue(0d);
            moFieldIsInGuarProcess.setFieldValue(false);
            moFieldIsInInsurProcess.setFieldValue(false);
            moFieldCreditLimit.setFieldValue(0d);
            moFieldDaysOfCredit.setFieldValue(0);
            moFieldDaysOfGrace.setFieldValue(0);
            moFieldIsCreditByUser.setFieldValue(false);
        }

        renderCreditLimit();
    }

    private void renderBussinesPartnerType() {
        if (jcbFkBizPartnerTypeId.getSelectedIndex() <= 0) {
            moBizPartnerType = null;
        }
        else {
            moBizPartnerType = new SDataBizPartnerType();
            moBizPartnerType.read(((SFormComponentItem) jcbFkBizPartnerTypeId.getSelectedItem()).getPrimaryKey(), miClient.getSession().getStatement());
        }
        
        if (!jckIsCreditByUser.isSelected()) {
            if (moBizPartnerType == null) {
                moFieldFkCreditTypeId_n.setFieldValue(new int[] { SModSysConsts.BPSS_TP_CRED_CRED_NO });
                moFieldCreditLimit.setFieldValue(0d);
                moFieldDaysOfCredit.setFieldValue(0);
                moFieldDaysOfGrace.setFieldValue(0);
                moFieldFkRiskTypeId.setFieldValue(new int[] { SModSysConsts.BPSS_RISK_C_RSK_H });
                moFieldGuaranteeAmount.setFieldValue(0d);
                moFieldInsuranceAmount.setFieldValue(0d);
                moFieldIsInGuarProcess.setFieldValue(false);
                moFieldIsInInsurProcess.setFieldValue(false);
            }
            else {
                moFieldFkCreditTypeId_n.setFieldValue(new int[] { moBizPartnerType.getFkCreditTypeId() });
                moFieldCreditLimit.setFieldValue(moBizPartnerType.getCreditLimit());
                moFieldDaysOfCredit.setFieldValue(moBizPartnerType.getDaysOfCredit());
                moFieldDaysOfGrace.setFieldValue( moBizPartnerType.getDaysOfGrace());
                moFieldFkRiskTypeId.setFieldValue(new int[] { moBizPartnerType.getFkRiskTypeId()});
//                moFieldGuaranteeAmount.setFieldValue(moBizPartnerType.getGuarantee());
//                moFieldGuaranteeAmount.setFieldValue(moBizPartnerType.getInsurance());
            }
        }
        else {
            if (moBizPartnerCategory == null) {
                moFieldFkCreditTypeId_n.setFieldValue(new int[] { SModSysConsts.BPSS_TP_CRED_CRED_NO });
                moFieldCreditLimit.setFieldValue(0d);
                moFieldDaysOfCredit.setFieldValue(0);
                moFieldDaysOfGrace.setFieldValue(0);
                moFieldFkRiskTypeId.setFieldValue(new int[] { SModSysConsts.BPSS_RISK_C_RSK_H });
                moFieldGuaranteeAmount.setFieldValue(0d);
                moFieldInsuranceAmount.setFieldValue(0d);
                moFieldIsInGuarProcess.setFieldValue(false);
                moFieldIsInInsurProcess.setFieldValue(false);
            }
            else {
                moFieldFkCreditTypeId_n.setFieldValue(new int[] { moBizPartnerCategory.getFkCreditTypeId_n() });
                moFieldCreditLimit.setFieldValue(moBizPartnerCategory.getCreditLimit());
                moFieldDaysOfCredit.setFieldValue(moBizPartnerCategory.getDaysOfCredit());
                moFieldDaysOfGrace.setFieldValue( moBizPartnerCategory.getDaysOfGrace());
                moFieldFkRiskTypeId.setFieldValue(new int[] { moBizPartnerCategory.getFkRiskTypeId_n()});
                moFieldGuaranteeAmount.setFieldValue(moBizPartnerCategory.getGuarantee());
                moFieldInsuranceAmount.setFieldValue(moBizPartnerCategory.getInsurance());
                moFieldIsInGuarProcess.setFieldValue(moBizPartnerCategory.getIsGuaranteeInProcess());
                moFieldIsInInsurProcess.setFieldValue(moBizPartnerCategory.getIsInsuranceInProcess());
            }
        }
    }

     private void renderCustomerConfig() {
         if (moFieldFkBizPartnerCategoryId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_CT_BP_CUS) {
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
            jlFkCfdAddendaTypeId.setEnabled(true);
            jcbFkCfdAddendaTypeId.setEnabled(true);
            jbEdit.setEnabled(true);
            jbDel.setEnabled(true);
            jbAddCob.setEnabled(true);
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
            jlFkCfdAddendaTypeId.setEnabled(false);
            jcbFkCfdAddendaTypeId.setEnabled(false);
            jbEdit.setEnabled(false);
            jbDel.setEnabled(false);
            jbAddCob.setEnabled(false);
         }
     }

     private void renderAdditionalInformartion() {
        if (moBizPartnerCategory.getFkBizPartnerCategoryId() == SDataConstantsSys.BPSS_CT_BP_CDR || moBizPartnerCategory.getFkBizPartnerCategoryId() == SDataConstantsSys.BPSS_CT_BP_DBR) {
            jcbFkLanguageId_n.setEnabled(false);
            jbFkLanguageId_n.setEnabled(false);
            jckIsCreditByUser.setEnabled(false);
        }
        else {
            jcbFkLanguageId_n.setEnabled(true);
            jbFkLanguageId_n.setEnabled(true);
            jckIsCreditByUser.setEnabled(true);
        }
    }

    private void renderDaysOfGrace() {
        if (mnParamBizPartnerCategoryFilter == SDataConstantsSys.BPSS_CT_BP_SUP) {
            if (miClient.getSessionXXX().getParamsErp().getSupplierDaysOfGrace() == 0) {
                jtfDaysOfGrace.setEditable(false);
            }
        }
        else if (mnParamBizPartnerCategoryFilter == SDataConstantsSys.BPSS_CT_BP_CUS) {
            if (miClient.getSessionXXX().getParamsErp().getCustomerDaysOfGrace() == 0) {
                jtfDaysOfGrace.setEditable(false);
            }
        }
    }

    private java.lang.String readLanguageLey(int pk) {
        SDataLanguage lang = null;

        lang = (SDataLanguage) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_LAN, new int[] { pk }, SLibConstants.EXEC_MODE_SILENT);

        return lang.getKey();
    }

    private java.lang.String readCurrencyKey(int pk) {
        SDataCurrency cur = null;

        cur = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR, new int[] { pk }, SLibConstants.EXEC_MODE_SILENT);

        return cur.getKey();
    }

    private void actionFkBizPartnerTypeId() {
        miClient.pickOption(SDataConstants.BPSU_TP_BP, moFieldFkBizPartnerTypeId, moFieldFkBizPartnerCategoryId.getKeyAsIntArray());
    }

    private void actionFkLanguageId() {
        miClient.pickOption(SDataConstants.CFGU_LAN, moFieldFkLanguageId, null);
    }

    private void actionFkCurrencyId() {
        miClient.pickOption(SDataConstants.CFGU_CUR, moFieldFkCurrencyId, null);
    }

    private void actionEditLanguage() {
        setLanguageEnabled(true);
    }

    private void actionEditCurrency() {
        setCurrencyEnabled(true);
    }

    private void setLanguageEnabled(boolean enable) {
        jbEditLanguage.setEnabled(!enable);
        jcbFkLanguageId_n.setEnabled(enable);
        jbFkLanguageId_n.setEnabled(enable);
    }

    private void setCurrencyEnabled(boolean enable) {
        jbEditCurrency.setEnabled(!enable);
        jcbFkCurrencyId_n.setEnabled(enable);
        jbFkCurrencyId_n.setEnabled(enable);
    }

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

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }
        for (int i = 0; i < mvFieldsCategory.size(); i++) {
            ((erp.lib.form.SFormField) mvFieldsCategory.get(i)).resetField();
        }
        for (int i = 0; i < mvFieldsCusConfig.size(); i++) {
            ((erp.lib.form.SFormField) mvFieldsCusConfig.get(i)).resetField();
        }

        jtfFirstName.setText("");
        jtfLastName.setText("");
        jtfBizPartner.setText("");
        jtfFiscalId.setText("");
        jtfFiscalFrgId.setText("");
        jftAlternativeId.setText("");
        jtfPkBizPartnerId_Ro.setText("");
        moBizPartnerBranchPane.createTable(null);
        moBizPartnerBranchPane.clearTableRows();
        moPanelBizPartnerBranch.formReset();
        moPanelBizPartnerBranch.setParamIsInMainWindow(true);
        moPanelBizPartnerBranch.setParamFormBizPartner(this);
        moPanelBizPartnerBranch.setParamIsCompany(mnParamBizPartnerType == SDataConstants.BPSX_BP_CO);
        jtfFirstName.setEnabled(false);
        jtfLastName.setEnabled(false);
        jtfBizPartner.setEnabled(false);
        jftAlternativeId.setEnabled(false);
        jTabbedPane1.setSelectedIndex(0);
        jTabbedPane1.setEnabledAt(1, true);
        jckIsAttEmployee.setSelected(false);

        moComboBoxBizPartnerIdentity.reset();
        maoDbmsCategorySettings = new SDataBizPartnerCategory[SDataConstantsSys.BPSX_CT_BP_QTY];

        moFieldDateStart.setFieldValue(SLibTimeUtilities.getBeginOfMonth(miClient.getSessionXXX().getWorkingDate()));
        moFieldFkCfdAddendaTypeId.setFieldValue(new int[] { SDataConstantsSys.BPSS_TP_CFD_ADD_NA });

        jckIsCreditByUser.setSelected(false);
        jtfCurrencyKey.setText(miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
        jtfCurrencyKeyGuar.setText(miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
        jtfCurrencyKeyInsur.setText(miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());

        renderBizPartnerCategories();

        renderKey();
        renderCategorySettings();
        renderDaysOfGrace();
        renderCustomerConfig();
        itemStateChangedIsCreditApplying();
        if (mnParamBizPartnerType != SDataConstants.BPSX_BP_CO) {
            setLanguageEnabled(false);
            setCurrencyEnabled(false);
        }

        moCustomerConfig = null;
        moCustomerConfigCobPane.createTable(null);
        moCustomerConfigCobPane.clearTableRows();

        moComboboxGrpMarketSegment.reset();

        jckIsDeleted.setEnabled(false);
        jckIsCategoryDeleted.setEnabled(false);
    }

    @Override
    public void formRefreshCatalogues() {
        int bizCategory = 0;

        renderBizPartnerCategories();
        SFormUtilities.populateComboBox(miClient, jcbFkBizPartnerIdentityTypeId, SDataConstants.BPSS_TP_BP_IDY);

        switch (mnParamBizPartnerCategoryFilter) {
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
        SFormUtilities.populateComboBox(miClient, jcbFkRiskTypeId, SDataConstants.BPSS_TP_RISK);
        SFormUtilities.populateComboBox(miClient, jcbFkLanguageId_n, SDataConstants.CFGU_LAN);
        SFormUtilities.populateComboBox(miClient, jcbFkCurrencyId_n, SDataConstants.CFGU_CUR);
        SFormUtilities.populateComboBox(miClient, jcbFkCfdAddendaTypeId, SDataConstants.BPSS_TP_CFD_ADD);

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
            Object[] oParamsIn = new Object[] { moBizPartner == null ? 0 : moBizPartner.getPkBizPartnerId(), moFieldBizPartner.getString() };
            Object[] oParamsInFiscal = new Object[] { moBizPartner == null ? 0 : moBizPartner.getPkBizPartnerId(), jtfFiscalId.getText().trim()};

            if (moFieldFkTaxIdentityTypeId.getKeyAsIntArray()[0] == 0) {
                validation.setTabbedPaneIndex(0);
                validation.setComponent(jcbFkTaxIdentityTypeId);
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkTaxIdentityTypeId.getText() + "'.");
            }
            else if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER && jtfLastName.getText().length() == 0) {
                validation.setTabbedPaneIndex(0);
                validation.setComponent(jtfLastName);
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlLastName.getText() + "'.");
            }
            else if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER && jtfFirstName.getText().length() == 0) {
                validation.setTabbedPaneIndex(0);
                validation.setComponent(jtfFirstName);
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFirstName.getText() + "'.");
            }
            else if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_ORG && jtfBizPartner.getText().length() == 0) {
                validation.setTabbedPaneIndex(0);
                validation.setComponent(jtfBizPartner);
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlBizPartner.getText() + "'.");
            }
            else if (jtfFiscalId.getText().trim().length() > 0 && jtfFiscalId.getText().trim().length() < (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER ? 13 : 12)) {
                validation.setTabbedPaneIndex(0);
                validation.setComponent(jtfFiscalId);
                validation.setMessage("El valor para el campo '" + jlFiscalId.getText() + "' debe estar completo.");
            }
            else {
                try {
                    if (!validation.getIsError() && jckIsDeleted.isSelected() && SFinUtilities.hasBizPartnerMovesFinance(miClient, miClient.getSessionXXX().getSystemYear(), moBizPartner.getPkBizPartnerId())) {
                        validation.setMessage("No se puede eliminar el '" + getTitle() + "' debido a que tiene movimientos en el ejercicio actual.");
                        validation.setComponent(jckIsDeleted);
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }

                if (SDataUtilities.callProcedureVal(miClient, SProcConstants.BPSU_BP, oParamsIn, SLibConstants.EXEC_MODE_VERBOSE) > 0) {
                    if (miClient.showMsgBoxConfirm("El valor del campo '" + jlBizPartner.getText() + "' ya existe, ¿desea conservalo?") == JOptionPane.NO_OPTION) {
                        validation.setTabbedPaneIndex(0);
                        validation.setComponent(jtfBizPartner);
                        validation.setIsError(true);
                    }
                }

                if (!validation.getIsError()) {
                    if (SDataUtilities.callProcedureVal(miClient, SProcConstants.BPSU_BP_FISCAL_ID, oParamsInFiscal, SLibConstants.EXEC_MODE_VERBOSE) > 0) {
                        if (miClient.showMsgBoxConfirm("El valor del campo '" + jlFiscalId.getText() + "' ya existe, ¿desea conservalo?") == JOptionPane.NO_OPTION) {
                            validation.setTabbedPaneIndex(0);
                            validation.setComponent(jtfFiscalId);
                            validation.setIsError(true);
                        }
                    }
                }

                if (!validation.getIsError()) {
                    validation = moPanelBizPartnerBranch.formValidate();
                    validation.setTabbedPaneIndex(0);
                }

                if (!validation.getIsError() && mnParamBizPartnerCategoryFilter != SDataConstantsSys.BPSS_CT_BP_CO) {
                    validation.setTabbedPaneIndex(1);
                    for (int i = 0; i < mvFieldsCategory.size(); i++) {
                        if (!((erp.lib.form.SFormField) mvFieldsCategory.get(i)).validateField()) {
                            validation.setIsError(true);
                            validation.setComponent(((erp.lib.form.SFormField) mvFieldsCategory.get(i)).getComponent());
                            break;
                        }
                    }
                    if (!validation.getIsError()) {
                        if (moFieldDateEnd.getFieldValue() != null) {
                            if (moFieldDateStart.getDate().after(moFieldDateEnd.getDate())) {
                                validation.setMessage("La fecha del campo '" + jlDateStart.getText() + "' no puede ser posterior a la del campo '" + jlDateEnd.getText() + "'.");
                                validation.setComponent(jftDateEnd);
                            }
                        }
                    }
                    if (!validation.getIsError()) {
                        if (mnParamBizPartnerCategoryFilter == SDataConstantsSys.BPSS_CT_BP_SUP && miClient.getSessionXXX().getParamsErp().getIsKeySupplierApplying() && moFieldKey.getString().length() == 0) {
                            validation.setComponent(jftKey);
                            validation.setMessage("Se debe ingresar un valor para el campo '" + jlKey.getText() + "'.");
                        }
                        else if (mnParamBizPartnerCategoryFilter == SDataConstantsSys.BPSS_CT_BP_CUS && miClient.getSessionXXX().getParamsErp().getIsKeyCustomerApplying() && moFieldKey.getString().length() == 0) {
                            validation.setComponent(jftKey);
                            validation.setMessage("Se debe ingresar un valor para el campo '" + jlKey.getText() + "'.");
                        }
                        else if (jcbFkCreditTypeId_n.isEnabled() && moFieldFkCreditTypeId_n.getKeyAsIntArray()[0] == 0 && (moBizPartnerCategory.getFkBizPartnerCategoryId() == SDataConstantsSys.BPSS_CT_BP_SUP ||
                                moBizPartnerCategory.getFkBizPartnerCategoryId() == SDataConstantsSys.BPSS_CT_BP_CUS)) {
                            validation.setComponent(jcbFkCreditTypeId_n);
                            validation.setMessage("Se debe seleccionar una opción para el campo '" + jlFkCreditTypeId_n.getText() + "'.");
                        }
                        else if ((moFieldFkBizPartnerCategoryId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_CT_BP_SUP || moFieldFkBizPartnerCategoryId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_CT_BP_CUS) &&
                                jcbFkCreditTypeId_n.isEnabled() && moFieldFkCreditTypeId_n.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_CRED_CRED_LIM && moFieldCreditLimit.getDouble() == 0) {
                                validation.setComponent(jtfCreditLimit);
                                validation.setMessage("Se debe ingresar un valor para el campo '" + jlCreditLimit.getText() + "'.");
                        }
                        else if ((moFieldFkBizPartnerCategoryId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_CT_BP_SUP || moFieldFkBizPartnerCategoryId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_CT_BP_CUS) &&
                                jcbFkCreditTypeId_n.isEnabled() && moFieldFkCreditTypeId_n.getKeyAsIntArray()[0] != SDataConstantsSys.BPSS_TP_CRED_CRED_NO && moFieldDaysOfCredit.getDouble() == 0) {
                                validation.setComponent(jtfDaysOfCredit);
                                validation.setMessage("Se debe ingresar un valor para el campo '" + jlDaysOfCredit.getText() + "'.");
                        }
                    }
                }

                if (!validation.getIsError() && mnParamBizPartnerCategoryFilter == SDataConstantsSys.BPSS_CT_BP_CUS) {
                    validation.setTabbedPaneIndex(2);
                    for (int i = 0; i < mvFieldsCusConfig.size(); i++) {
                        if (!((erp.lib.form.SFormField) mvFieldsCusConfig.get(i)).validateField()) {
                            validation.setIsError(true);
                            validation.setComponent(((erp.lib.form.SFormField) mvFieldsCusConfig.get(i)).getComponent());
                            break;
                        }
                    }
                    if (!validation.getIsError()) {
                        for (int i = 0; i < moCustomerConfigCobPane.getTableGuiRowCount(); i++) {
                            SDataCustomerBranchConfig oDataCustomerBranchConfig = (SDataCustomerBranchConfig) moCustomerConfigCobPane.getTableRow(i).getData();
                            if (oDataCustomerBranchConfig.getDbmsSalesRoute().length() > 0) {
                            }
                            else {
                                validation.setMessage("No se ha especificado la configuración de comercialización para algunas sucursales del cliente..");
                            }
                        }
                    }
                }

                if (!validation.getIsError() && moFieldFkSalesSupervisorId_n.getKeyAsIntArray()[0] != 0 && moFieldFkSalesAgentId_n.getKeyAsIntArray()[0] == 0) {
                    validation.setMessage("Se debe ingresar un valor para el campo '" + jlFkSalesAgentId_n.getText() + "'.");
                    validation.setTabbedPaneIndex(2);
                    validation.setComponent(jcbFkSalesAgentId_n);
                }

                if (!validation.getIsError() && moFieldFkSalesSupervisorId_n.getKeyAsIntArray()[0] != 0 && moFieldFkSalesSupervisorId_n.getKeyAsIntArray()[0] == moFieldFkSalesAgentId_n.getKeyAsIntArray()[0]) {
                    validation.setMessage("Se debe ingresar un valor diferente para el campo '" + jlFkSalesAgentId_n.getText() + "'.");
                    validation.setTabbedPaneIndex(2);
                    validation.setComponent(jcbFkSalesAgentId_n);
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
        int i = 0;
        moBizPartner = (SDataBizPartner) registry;

        moFieldFkBizPartnerIdentityTypeId.setFieldValue(new int[] { moBizPartner.getFkBizPartnerIdentityTypeId() });
        renderBizPartnerSettings();
        renderTaxIdentity();
        moFieldLastName.setFieldValue(moBizPartner.getLastname());
        moFieldFirstName.setFieldValue(moBizPartner.getFirstname());
        if (moBizPartner.getFkBizPartnerIdentityTypeId() == SDataConstantsSys.BPSS_TP_BP_IDY_PER) {
            moFieldBizPartner.setFieldValue("");
        }
        else {
            moFieldBizPartner.setFieldValue(moBizPartner.getBizPartner());
        }
        moFieldBizPartnerCommercial.setFieldValue(moBizPartner.getBizPartner().compareTo(moBizPartner.getBizPartnerCommercial()) == 0 ? "" : moBizPartner.getBizPartnerCommercial());
        moFieldFiscalId.setFieldValue(moBizPartner.getFiscalId());
        moFieldFiscalFrgId.setFieldValue(moBizPartner.getFiscalFrgId());
        moFieldAlternativeId.setFieldValue(moBizPartner.getAlternativeId());
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

        if (moBizPartner.getDbmsBizPartnerBranches().size() > 0) {
            moPanelBizPartnerBranch.setRegistry(moBizPartner.getDbmsBizPartnerBranches().get(0));
        }

        for (i = 1; i < moBizPartner.getDbmsBizPartnerBranches().size(); i++) {
            moBizPartnerBranchPane.addTableRow(
                    new SDataBizPartnerBranchRow(moBizPartner.getDbmsBizPartnerBranches().get(i)));
            if (moBizPartner.getDbmsBizPartnerBranches().get(i).getIsDeleted()) {
                ((STableModel) moBizPartnerBranchPane.getTable().getModel()).getTableRow(i - 1).setStyle(STableConstants.STYLE_DELETE);
            }
        }

        moBizPartnerBranchPane.renderTableRows();
        if (moBizPartnerBranchPane.getTableGuiRowCount() > 0) {
            moBizPartnerBranchPane.setTableRowSelection(0);
        }

        maoDbmsCategorySettings[0] = moBizPartner.getDbmsCategorySettingsCo();
        maoDbmsCategorySettings[1] = moBizPartner.getDbmsCategorySettingsSup();
        maoDbmsCategorySettings[2] = moBizPartner.getDbmsCategorySettingsCus();

        // Read business partner notes:

        if (moBizPartner.getDbmsBizPartnerNotes().size() > 0) {
            moFieldNotes.setString(moBizPartner.getDbmsBizPartnerNotes().get(0).getNotes());
        }

        if(mnParamBizPartnerCategoryFilter == SDataConstantsSys.BPSS_CT_BP_CO) {
            moBizPartnerCategory = maoDbmsCategorySettings[0];
        }
        else if(mnParamBizPartnerCategoryFilter == SDataConstantsSys.BPSS_CT_BP_SUP) {
            moBizPartnerCategory = maoDbmsCategorySettings[1];
        }
        else if (mnParamBizPartnerCategoryFilter == SDataConstantsSys.BPSS_CT_BP_CUS) {
            moBizPartnerCategory = maoDbmsCategorySettings[2];
        }

        if (moBizPartnerCategory != null) {
            moFieldFkBizPartnerCategoryId.setFieldValue(new int[] { moBizPartnerCategory.getFkBizPartnerCategoryId()});

            if (mnParamBizPartnerCategoryFilter == SDataConstantsSys.BPSS_CT_BP_SUP) {
                if (miClient.getSessionXXX().getParamsErp().getFkSupplierTypeId_n() > 0 && moBizPartnerCategory.getFkBizPartnerTypeId() == 0) {
                    moFieldFkBizPartnerTypeId.setFieldValue(new int[] { miClient.getSessionXXX().getParamsErp().getFkSupplierCategoryId_n(),
                    miClient.getSessionXXX().getParamsErp().getFkSupplierTypeId_n() });
                }
                else {
                    moFieldFkBizPartnerTypeId.setFieldValue(new int[] { moBizPartnerCategory.getFkBizPartnerCategoryId(), moBizPartnerCategory.getFkBizPartnerTypeId() });
                }
            }
            else if (mnParamBizPartnerCategoryFilter == SDataConstantsSys.BPSS_CT_BP_CUS) {
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
                    
            if (moBizPartnerCategory.getIsCreditByUser()) {
                moFieldFkCreditTypeId_n.setFieldValue(new int[] { moBizPartnerCategory.getEffectiveCreditTypeId() });
                moFieldCreditLimit.setFieldValue(moBizPartnerCategory.getEffectiveCreditLimit());
                moFieldDaysOfCredit.setFieldValue(moBizPartnerCategory.getEffectiveDaysOfCredit());
                moFieldDaysOfGrace.setFieldValue(moBizPartnerCategory.getEffectiveDaysOfGrace());
                moFieldFkRiskTypeId.setFieldValue(new int[] { moBizPartnerCategory.getEffectiveRiskTypeId() });
                moFieldGuaranteeAmount.setFieldValue(moBizPartnerCategory.getGuarantee());
                moFieldInsuranceAmount.setFieldValue(moBizPartnerCategory.getInsurance());
                moFieldIsInGuarProcess.setFieldValue(moBizPartnerCategory.getIsGuaranteeInProcess());
                moFieldIsInInsurProcess.setFieldValue(moBizPartnerCategory.getIsInsuranceInProcess());
            }
            
            moFieldFkLanguageId.setFieldValue(new int[] { moBizPartnerCategory.getFkLanguageId_n() });
            moFieldFkCurrencyId.setFieldValue(new int[] { moBizPartnerCategory.getFkCurrencyId_n() });
            moFieldDateStart.setFieldValue(moBizPartnerCategory.getDateStart());
            moFieldDateEnd.setFieldValue(moBizPartnerCategory.getDateEnd_n());
            
            moFieldFkCfdAddendaTypeId.setFieldValue(new int[] { moBizPartnerCategory.getFkCfdAddendaTypeId() });
            moFieldIsCategoryDeleted.setFieldValue(moBizPartnerCategory.getIsDeleted());
            renderAdditionalInformartion();
        }

        renderKey();
        renderCreditLimit();
        renderCategorySettings();
        renderBizPartnerCategories();
        moFieldIsCreditByUser.setFieldValue(moBizPartnerCategory != null ? moBizPartnerCategory.getIsCreditByUser() : false);
        itemStateChangedIsCreditApplying();
        if (mnParamBizPartnerType != SDataConstants.BPSX_BP_CO) {
            setLanguageEnabled(moBizPartnerCategory != null && moBizPartnerCategory.getFkLanguageId_n() > 0 && moBizPartnerCategory.getFkLanguageId_n() != miClient.getSessionXXX().getParamsErp().getFkLanguageId());
            setCurrencyEnabled(moBizPartnerCategory != null && moBizPartnerCategory.getFkCurrencyId_n() > 0 && moBizPartnerCategory.getFkCurrencyId_n() != miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId());
        }

        moPanelBizPartnerBranch.setParamIsCompany(moBizPartner.getIsCompany());

        SDataCustomerBranchConfigRow dataCustomerConfigCobRow = null;

        if (mnParamBizPartnerCategoryFilter == SDataConstantsSys.BPSS_CT_BP_CUS && moBizPartnerCategory != null) {
            moCustomerConfig = moBizPartner.getDbmsDataCustomerConfig();

            if (moCustomerConfig != null) {
                moFieldFkCustomerTypeId.setKey(new int[] { moCustomerConfig.getFkCustomerTypeId() });
                moFieldFkMarketSegmentId.setKey(new int[] { moCustomerConfig.getFkMarketSegmentId() });
                moFieldFkMarketSubsegmentId.setKey(new int[] { moCustomerConfig.getFkMarketSegmentId(), moCustomerConfig.getFkMarketSubsegmentId() });
                moFieldFkDistributionChannelId.setKey(new int[] { moCustomerConfig.getFkDistributionChannelId() });
                moFieldFkSalesRouteId.setKey(new int[] {moBizPartner.getDbmsBizPartnerBranches().get(0).getDbmsDataCustomerBranchConfig().get(0).getFkSalesRouteId()});
                moFieldFkSalesAgentId_n.setKey(new int[] { moCustomerConfig.getFkSalesAgentId_n() });
                moFieldFkSalesSupervisorId_n.setKey(new int[] { moCustomerConfig.getFkSalesSupervisorId_n() });
                moFieldIsFreeDiscountDoc.setFieldValue(moCustomerConfig.getIsFreeDiscountDoc());
                moFieldIsFreeCommissions.setFieldValue(moCustomerConfig.getIsFreeCommissions());
            }

            for (i = 1; i < moBizPartner.getDbmsBizPartnerBranches().size(); i++) {
                if (moBizPartner.getDbmsBizPartnerBranches().get(i).getDbmsDataCustomerBranchConfig().size() > 0) {
                    if (!moBizPartner.getDbmsBizPartnerBranches().get(i).getDbmsDataCustomerBranchConfig().get(0).getIsDeleted()) {
                        dataCustomerConfigCobRow = new SDataCustomerBranchConfigRow(moBizPartner.getDbmsBizPartnerBranches().get(i).getDbmsDataCustomerBranchConfig().get(0));
                        moCustomerConfigCobPane.addTableRow(dataCustomerConfigCobRow);
                    }
                }
            }
        }

        jckIsDeleted.setEnabled(true);
        jckIsCategoryDeleted.setEnabled(moBizPartner.getDbmsDataEmployee() == null || (moBizPartner.getDbmsDataEmployee() != null && (mnParamBizPartnerCategoryFilter == SDataConstantsSys.BPSS_CT_BP_SUP || mnParamBizPartnerCategoryFilter == SDataConstantsSys.BPSS_CT_BP_CUS)));
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        int i = 0;
        SDataBizPartnerNote bizNote = null;
        SDataCustomerBranchConfig cusConfigCob = null;
        SDataBizPartnerBranch branch = null;

        if (moBizPartner == null) {
            moBizPartner = new SDataBizPartner();
            moBizPartner.setFkFiscalBankId(SModSysConsts.FINS_FISCAL_BANK_NA);
            moBizPartner.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moBizPartner.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moBizPartner.setFkBizPartnerIdentityTypeId(moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0]);

        if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER) {
            moBizPartner.setLastname(moFieldLastName.getString());
            moBizPartner.setFirstname(moFieldFirstName.getString());
            moBizPartner.setBizPartner(moFieldLastName.getString().trim() + ", " + moFieldFirstName.getString().trim());
        }
        else {
            moBizPartner.setLastname("");
            moBizPartner.setFirstname("");
            moBizPartner.setBizPartner(moFieldBizPartner.getString());
        }

        moBizPartner.setBizPartnerCommercial(moFieldBizPartnerCommercial.getString().isEmpty() ? moBizPartner.getBizPartner() : moFieldBizPartnerCommercial.getString());

        moBizPartner.setFiscalId(moFieldFiscalId.getString().toUpperCase());
        moBizPartner.setFiscalFrgId(moFieldFiscalFrgId.getString().toUpperCase());
        //moBizPartner.setCodeBankSantander();
        //moBizPartner.setCodeBankBanBajio();
        moBizPartner.setWeb(moFieldWeb.getString());

        if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER) {
            moBizPartner.setAlternativeId(moFieldAlternativeId.getString().toUpperCase());
        }
        else {
            moBizPartner.setAlternativeId("");
        }

        moBizPartner.setFkTaxIdentityId(moFieldFkTaxIdentityTypeId.getKeyAsIntArray()[0]);
        moBizPartner.setFkBizAreaId(moFieldFkBizArea.getKeyAsIntArray()[0]);

        switch (mnParamBizPartnerType) {
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

        // Save business partner branches:

        moBizPartner.getDbmsBizPartnerBranches().removeAllElements();
        branch = (SDataBizPartnerBranch) moPanelBizPartnerBranch.getRegistry();
        branch.setAuxSaveBkc(mnParamBizPartnerType == SDataConstants.BPSX_BP_CO && branch.getIsRegistryNew() ? true : false);

        moBizPartner.getDbmsBizPartnerBranches().add(branch);

        for (i = 0; i < moBizPartnerBranchPane.getTable().getRowCount(); i++) {
            branch = (SDataBizPartnerBranch) moBizPartnerBranchPane.getTableRow(i).getData();
            branch.setAuxSaveBkc(mnParamBizPartnerType == SDataConstants.BPSX_BP_CO && branch.getIsRegistryNew() ? true : false);
            moBizPartner.getDbmsBizPartnerBranches().add(branch);
        }

        // Save business partner notes:

        moBizPartner.getDbmsBizPartnerNotes().clear();
        if (moFieldNotes.getString().length() > 0) {
            bizNote = new SDataBizPartnerNote();
            bizNote.setNotes(moFieldNotes.getString());
            moBizPartner.getDbmsBizPartnerNotes().add(bizNote);
        }

        // Save business partner category:

        if (moBizPartnerCategory == null) {
            moBizPartnerCategory = new SDataBizPartnerCategory();
            moBizPartnerCategory.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moBizPartnerCategory.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        if (mnParamBizPartnerCategoryFilter == SDataConstantsSys.BPSS_CT_BP_CO) {
            setCategoryCompanySettings();
        }
        else {
            moBizPartnerCategory.setPkBizPartnerCategoryId(moFieldFkBizPartnerCategoryId.getKeyAsIntArray()[0]);
            moBizPartnerCategory.setKey(moFieldKey.getString());
            moBizPartnerCategory.setCompanyKey(moFieldCompanyKey.getString());
            
            if (!jckIsCreditByUser.isSelected()) {
                moBizPartnerCategory.setCreditLimit(0);
                moBizPartnerCategory.setDaysOfCredit(0);
                moBizPartnerCategory.setDaysOfGrace(0);
                moBizPartnerCategory.setGuarantee(0);
                moBizPartnerCategory.setInsurance(0);
                moBizPartnerCategory.setIsGuaranteeInProcess(false);
                moBizPartnerCategory.setIsInsuranceInProcess(false);
            }
            else {
                moBizPartnerCategory.setCreditLimit(moFieldCreditLimit.getDouble());
                moBizPartnerCategory.setDaysOfCredit(moFieldDaysOfCredit.getInteger());
                moBizPartnerCategory.setDaysOfGrace(moFieldDaysOfGrace.getInteger());
                moBizPartnerCategory.setGuarantee(moFieldGuaranteeAmount.getDouble());
                moBizPartnerCategory.setInsurance(moFieldInsuranceAmount.getDouble());
                moBizPartnerCategory.setIsGuaranteeInProcess(moFieldIsInGuarProcess.getBoolean());
                moBizPartnerCategory.setIsInsuranceInProcess(moFieldIsInInsurProcess.getBoolean());
            }
            moBizPartnerCategory.setDateStart(moFieldDateStart.getDate());
            moBizPartnerCategory.setDateEnd_n(moFieldDateEnd.getDate());
            moBizPartnerCategory.setIsCreditByUser(jckIsCreditByUser.isSelected());
            moBizPartnerCategory.setFkBizPartnerCategoryId(moFieldFkBizPartnerCategoryId.getKeyAsIntArray()[0]);
            moBizPartnerCategory.setFkBizPartnerTypeId(moFieldFkBizPartnerTypeId.getKeyAsIntArray()[1]);
            moBizPartnerCategory.setFkCreditTypeId_n(!jckIsCreditByUser.isSelected() ? SDataConstantsSys.BPSS_TP_CRED_CRED_NO : moFieldFkCreditTypeId_n.getKeyAsIntArray()[0]);
            moBizPartnerCategory.setFkRiskTypeId_n(!jckIsCreditByUser.isSelected() ? SModSysConsts.BPSS_RISK_C_RSK_H : moFieldFkRiskTypeId.getKeyAsIntArray()[0]);
            moBizPartnerCategory.setFkCfdAddendaTypeId(moFieldFkCfdAddendaTypeId.getKeyAsIntArray()[0]);
            moBizPartnerCategory.setFkLanguageId_n(moBizPartnerCategory.getPkBizPartnerCategoryId() == SDataConstantsSys.BPSS_CT_BP_SUP || moBizPartnerCategory.getPkBizPartnerCategoryId() == SDataConstantsSys.BPSS_CT_BP_CUS ? moFieldFkLanguageId.getKeyAsIntArray()[0] : -1);
            moBizPartnerCategory.setFkCurrencyId_n(moFieldFkCurrencyId.getKeyAsIntArray()[0] <= 0 ? -1 : moFieldFkCurrencyId.getKeyAsIntArray()[0]);
            moBizPartnerCategory.setAuxLanguageSysId(miClient.getSessionXXX().getParamsErp().getFkLanguageId());
            moBizPartnerCategory.setAuxCurrencySysId(miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId());
            moBizPartnerCategory.setIsDeleted(moFieldIsCategoryDeleted.getBoolean());

            moBizPartnerCategory.setDbmBizPartnerType(jcbFkBizPartnerTypeId.getSelectedItem().toString());
            moBizPartnerCategory.setDbmsCreditType(moFieldFkCreditTypeId_n.getKeyAsIntArray()[0] == 0 ? "" : jcbFkCreditTypeId_n.getSelectedItem().toString());
            moBizPartnerCategory.setDbmsLanguage(moFieldFkLanguageId.getKeyAsIntArray()[0] == 0 ? "" : readLanguageLey(moFieldFkLanguageId.getKeyAsIntArray()[0]));
            moBizPartnerCategory.setDbmsCurrency(moFieldFkCurrencyId.getKeyAsIntArray()[0] == 0 ? "" : readCurrencyKey(moFieldFkCurrencyId.getKeyAsIntArray()[0]));
        }

        switch (mnParamBizPartnerCategoryFilter) {
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

        // Save Customer Configuration

        if (mnParamBizPartnerCategoryFilter == SDataConstantsSys.BPSS_CT_BP_CUS) {
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

            if (moBizPartner.getDbmsBizPartnerBranches().get(0).getDbmsDataCustomerBranchConfig().isEmpty()) {
                cusConfigCob = new SDataCustomerBranchConfig();
                cusConfigCob.setDbmsCob(moBizPartner.getDbmsBizPartnerBranches().get(0).getBizPartnerBranch());
                cusConfigCob.setDbmsSalesRoute(moFieldFkSalesRouteId.getString());
                cusConfigCob.setDbmsSalesAgent("");
                cusConfigCob.setFkSalesRouteId(moFieldFkSalesRouteId.getKeyAsIntArray()[0]);
                cusConfigCob.setFkSalesAgentId_n(0);
                cusConfigCob.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                cusConfigCob.setFkUserEditId(1);
                cusConfigCob.setFkUserDeleteId(1);
                cusConfigCob.setDbmsUserNew(miClient.getSessionXXX().getUser().getUser());
                cusConfigCob.setDbmsUserEdit("(n/a)");
                cusConfigCob.setDbmsUserDelete("(n/a)");
                cusConfigCob.setUserNewTs(miClient.getSessionXXX().getWorkingDate());

                moBizPartner.getDbmsBizPartnerBranches().get(0).getDbmsDataCustomerBranchConfig().add((SDataCustomerBranchConfig) cusConfigCob);
            }
            else {
                moBizPartner.getDbmsBizPartnerBranches().get(0).getDbmsDataCustomerBranchConfig().get(0).setFkSalesRouteId(moFieldFkSalesRouteId.getKeyAsIntArray()[0]);
                moBizPartner.getDbmsBizPartnerBranches().get(0).getDbmsDataCustomerBranchConfig().get(0).setFkUserEditId(miClient.getSession().getUser().getPkUserId());
            }

            moBizPartner.getDbmsBizPartnerBranches().get(0).getDbmsDataCustomerBranchConfig().add(moBizPartner.getDbmsBizPartnerBranches().get(0).getDbmsDataCustomerBranchConfig().get(0));

            for (i = 1; i < moBizPartner.getDbmsBizPartnerBranches().size(); i++) {
                if (moBizPartner.getDbmsBizPartnerBranches().get(i).getDbmsDataCustomerBranchConfig().size() > 0) {
                    moBizPartner.getDbmsBizPartnerBranches().get(i).getDbmsDataCustomerBranchConfig().add(moBizPartner.getDbmsBizPartnerBranches().get(i).getDbmsDataCustomerBranchConfig().get(0));
                }
            }

            moBizPartner.setDbmsDataCustomerConfig(moCustomerConfig);
        }


        return moBizPartner;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case SDataConstantsSys.VALUE_BIZ_PARTNER_TYPE:
                mnParamBizPartnerType = ((int[]) value)[0];

                switch (mnParamBizPartnerType) {
                    case SDataConstants.BPSX_BP_CO:
                        setTitle("Empresa");
                        jckIsCategoryDeleted.setText("Empresa eliminada");
                        break;
                    case SDataConstants.BPSX_BP_SUP:
                        setTitle("Proveedor");
                        jckIsCategoryDeleted.setText("Proveedor eliminado");
                        break;
                    case SDataConstants.BPSX_BP_CUS:
                        setTitle("Cliente");
                        jckIsCategoryDeleted.setText("Cliente eliminado");
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
        Object o = null;

        if (type == SDataConstantsSys.VALUE_BIZ_PARTNER_ID) {
            if (moFieldFkBizPartnerIdentityTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.BPSS_TP_BP_IDY_PER) {
                o = moFieldLastName.getString() + (moFieldLastName.getString().length() == 0 ? "" : ", ") + moFieldFirstName.getString();
            }
            else {
                o = moFieldBizPartner.getString();
            }
        }

        return o;
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
            else if (button == jbSetDateEnd) {
                actionSetDateEnd();
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
            else if (button == jbAddCob) {
                actionAddCob();
            }
            else if (button == jbEdit) {
                actionCustomerConfigCobEdit();
            }
            else if (button == jbDel) {
                actionDelCob();
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

    public void setParamBizPartnerCategoryFilter(int n) {
        mnParamBizPartnerCategoryFilter = n;
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
