/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadRegistries;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.data.SDataRegistry;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.lib.table.STableRow;
import erp.lib.table.STableRowCustom;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mcfg.data.SDataCompany;
import erp.mcfg.data.SDataCompanyBranchEntity;
import erp.mod.SModSysConsts;
import erp.musr.data.SDataAccessCompany;
import erp.musr.data.SDataAccessCompanyBranch;
import erp.musr.data.SDataAccessCompanyBranchEntity;
import erp.musr.data.SDataAccessCompanyBranchEntityUniversal;
import erp.musr.data.SDataUser;
import erp.musr.data.SProcUserNameVal;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.siieapp.SUserExportUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import org.json.simple.parser.ParseException;
import sa.lib.SLibMethod;
import sa.lib.gui.SGuiClient;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Sergio Flores, Alfonso Flores, Sergio Flores
 */
public class SFormUser extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, erp.lib.form.SFormExtendedInterface {
    
    private static final int TAB_IDX_BRANCHES = 2;

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private boolean mbResetCompanyBranchEntities;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.musr.data.SDataUser moUser;
    private erp.lib.form.SFormField moFieldUser;
    private erp.lib.form.SFormField moFieldEmail;
    private erp.lib.form.SFormField moFieldIsUniversalCompanies;
    private erp.lib.form.SFormField moFieldIsDeleted;
    private erp.lib.form.SFormField moFieldIsActive;
    private erp.lib.form.SFormField moFieldFkBizPartnerId;
    private erp.lib.form.SFormField moFieldCompany;
    private erp.lib.form.SFormField moFieldFkCompanyId;
    private erp.lib.form.SFormField moFieldFkCompanyEntityId;
    private erp.lib.form.SFormField moFieldFkCompanyBranchEntityId;
    private erp.lib.form.SFormField moFieldBranch;
    private erp.lib.form.SFormField moFieldCashAccount;
    private erp.lib.form.SFormField moFieldUserCashAccount;
    private erp.lib.form.SFormField moFieldWarehouse;
    private erp.lib.form.SFormField moFieldUserWarehouse;
    private erp.lib.form.SFormField moFieldPos;
    private erp.lib.form.SFormField moFieldUserPos;
    private erp.lib.form.SFormField moFieldPlant;
    private erp.lib.form.SFormField moFieldUserPlant;

    private java.util.Vector<erp.mcfg.data.SDataCompany> mvDbmsCompanies;
    private java.util.Vector<erp.mbps.data.SDataBizPartnerBranch> mvDbmsCompanyBranches;
    private java.util.Vector<erp.mcfg.data.SDataCompanyBranchEntity> mvDbmsCashAccounts;
    private java.util.Vector<erp.mcfg.data.SDataCompanyBranchEntity> mvDbmsWarehouses;
    private java.util.Vector<erp.mcfg.data.SDataCompanyBranchEntity> mvDbmsPos;
    private java.util.Vector<erp.mcfg.data.SDataCompanyBranchEntity> mvDbmsPlants;
    private java.util.Vector<java.util.Vector<erp.lib.table.STableRowCustom>> mvUserBranches;
    private java.util.Vector<java.util.Vector<erp.lib.form.SFormComponentItem>> mvUserCashAccounts;
    private java.util.Vector<java.util.Vector<erp.lib.form.SFormComponentItem>> mvUserWarehouses;
    private java.util.Vector<java.util.Vector<erp.lib.form.SFormComponentItem>> mvUserPos;
    private java.util.Vector<java.util.Vector<erp.lib.form.SFormComponentItem>> mvUserPlants;
    private java.util.Vector<erp.lib.form.SFormComponentItem> mvEmptyListItems;

    private erp.lib.table.STablePane moUserCompaniesPane;
    private erp.lib.table.STablePane moUserCompanyBranchesPane;

    /**
     * Creates new form SFormUser
     * @param client GUI client.
     */
    public SFormUser(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.USRU_USR;

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

        jTabbedPane = new javax.swing.JTabbedPane();
        jpUser = new javax.swing.JPanel();
        jpUser1 = new javax.swing.JPanel();
        jPanel78 = new javax.swing.JPanel();
        jlUser = new javax.swing.JLabel();
        jtfUser = new javax.swing.JTextField();
        jckIsUniversal = new javax.swing.JCheckBox();
        jPanel79 = new javax.swing.JPanel();
        jlUserPassword = new javax.swing.JLabel();
        jpfUserPassword = new javax.swing.JPasswordField();
        jbEditUserPassword = new javax.swing.JButton();
        jPanel80 = new javax.swing.JPanel();
        jlUserPasswordConfirm = new javax.swing.JLabel();
        jpfUserPasswordConfirm = new javax.swing.JPasswordField();
        jPanel1 = new javax.swing.JPanel();
        jlEmail = new javax.swing.JLabel();
        jtfEmail = new javax.swing.JTextField();
        jPanel81 = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        jcbFkBizPartnerId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkBizPartnerId = new javax.swing.JButton();
        jPanel82 = new javax.swing.JPanel();
        jckIsActive = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel83 = new javax.swing.JPanel();
        jckIsDeleted = new javax.swing.JCheckBox();
        jpCompanies = new javax.swing.JPanel();
        jpCompanies1 = new javax.swing.JPanel();
        jpCompaniesAvailable = new javax.swing.JPanel();
        jlCompaniesAvailable = new javax.swing.JLabel();
        jspCompaniesAvailable = new javax.swing.JScrollPane();
        jltCompaniesAvailable = new javax.swing.JList<SFormComponentItem>();
        jpCompaniesControls = new javax.swing.JPanel();
        jpCompaniesControls1 = new javax.swing.JPanel();
        jpCompaniesControls1N = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jpCompaniesControls1E = new javax.swing.JPanel();
        jpCompaniesControls1W = new javax.swing.JPanel();
        jpCompaniesControls1C = new javax.swing.JPanel();
        jbTransferCompany = new javax.swing.JButton();
        jbTransferAllCompany = new javax.swing.JButton();
        jbReturnCompany = new javax.swing.JButton();
        jbReturnAllCompany = new javax.swing.JButton();
        jpCompaniesUser = new javax.swing.JPanel();
        jlCompanyUser = new javax.swing.JLabel();
        jpBranches = new javax.swing.JPanel();
        jpBranches0 = new javax.swing.JPanel();
        jlCompany = new javax.swing.JLabel();
        jcbCompanyBranch = new javax.swing.JComboBox<SFormComponentItem>();
        jLabel8 = new javax.swing.JLabel();
        jckIsUniversalCompanyBranch = new javax.swing.JCheckBox();
        jpBranches1 = new javax.swing.JPanel();
        jpBranches11 = new javax.swing.JPanel();
        jpBranchesAvailable = new javax.swing.JPanel();
        jlBranchesAvailable = new javax.swing.JLabel();
        jspBranchesAvailable = new javax.swing.JScrollPane();
        jltBranchesAvailable = new javax.swing.JList<SFormComponentItem>();
        jpBranchesControls = new javax.swing.JPanel();
        jpBranchesControls1 = new javax.swing.JPanel();
        jpBranchesControls1N = new javax.swing.JPanel();
        jpBranchesControls1E = new javax.swing.JPanel();
        jpBranchesControls1W = new javax.swing.JPanel();
        jpBranchesControls1C = new javax.swing.JPanel();
        jbTransferBranch = new javax.swing.JButton();
        jbTransferAllBranch = new javax.swing.JButton();
        jbReturnBranch = new javax.swing.JButton();
        jbReturnAllBranch = new javax.swing.JButton();
        jpBranchesUser = new javax.swing.JPanel();
        jlBranchUser = new javax.swing.JLabel();
        jpEntities = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jckIsUniversalCashAccount = new javax.swing.JCheckBox();
        jbSetDefaultCashAccount = new javax.swing.JButton();
        jPanel35 = new javax.swing.JPanel();
        jPanel36 = new javax.swing.JPanel();
        jlCompanyBranchCashAccount = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jltCompanyBranchCashAccount = new javax.swing.JList<SFormComponentItem>();
        jPanel38 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
        jbTransferCashAccount = new javax.swing.JButton();
        jbTransferAllCashAccounts = new javax.swing.JButton();
        jbReturnCashAccount = new javax.swing.JButton();
        jbReturnAllCashAccounts = new javax.swing.JButton();
        jPanel42 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jlUserCashAccount = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jltUserCashAccount = new javax.swing.JList<SFormComponentItem>();
        jPanel9 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        jckIsUniversalWarehouse = new javax.swing.JCheckBox();
        jbSetDefaultWarehouse = new javax.swing.JButton();
        jPanel46 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jlCompanyBranchWarehouses = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jltCompanyBranchWarehouses = new javax.swing.JList<SFormComponentItem>();
        jPanel49 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel52 = new javax.swing.JPanel();
        jPanel53 = new javax.swing.JPanel();
        jPanel54 = new javax.swing.JPanel();
        jbTransferWarehouse = new javax.swing.JButton();
        jbTransferAllWarehouses = new javax.swing.JButton();
        jbReturnWarehouse = new javax.swing.JButton();
        jbReturnAllWarehouses = new javax.swing.JButton();
        jPanel48 = new javax.swing.JPanel();
        jlUserWarehouses = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jltUserWarehouses = new javax.swing.JList<SFormComponentItem>();
        jPanel12 = new javax.swing.JPanel();
        jPanel55 = new javax.swing.JPanel();
        jckIsUniversalPos = new javax.swing.JCheckBox();
        jbSetDefaultPos = new javax.swing.JButton();
        jPanel56 = new javax.swing.JPanel();
        jPanel57 = new javax.swing.JPanel();
        jlCompanyBranchPos = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jltCompanyBranchPos = new javax.swing.JList<SFormComponentItem>();
        jPanel59 = new javax.swing.JPanel();
        jPanel60 = new javax.swing.JPanel();
        jPanel61 = new javax.swing.JPanel();
        jPanel62 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel63 = new javax.swing.JPanel();
        jPanel64 = new javax.swing.JPanel();
        jPanel65 = new javax.swing.JPanel();
        jbTransferPos = new javax.swing.JButton();
        jbTransferAllPos = new javax.swing.JButton();
        jbReturnPos = new javax.swing.JButton();
        jbReturnAllPos = new javax.swing.JButton();
        jPanel58 = new javax.swing.JPanel();
        jlUserPos = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jltUserPos = new javax.swing.JList<SFormComponentItem>();
        jPanel24 = new javax.swing.JPanel();
        jPanel66 = new javax.swing.JPanel();
        jckIsUniversalPlant = new javax.swing.JCheckBox();
        jbSetDefaultPlant = new javax.swing.JButton();
        jPanel67 = new javax.swing.JPanel();
        jPanel68 = new javax.swing.JPanel();
        jlCompanyBranchPlants = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jltCompanyBranchPlants = new javax.swing.JList<SFormComponentItem>();
        jPanel72 = new javax.swing.JPanel();
        jPanel73 = new javax.swing.JPanel();
        jPanel74 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jPanel75 = new javax.swing.JPanel();
        jPanel76 = new javax.swing.JPanel();
        jPanel77 = new javax.swing.JPanel();
        jbTransferPlant = new javax.swing.JButton();
        jbTransferAllPlants = new javax.swing.JButton();
        jbReturnPlant = new javax.swing.JButton();
        jbReturnAllPlants = new javax.swing.JButton();
        jPanel69 = new javax.swing.JPanel();
        jlUserPlants = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jltUserPlants = new javax.swing.JList<SFormComponentItem>();
        jPanel31 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jlCompanyEntity = new javax.swing.JLabel();
        jcbCompanyEntity = new javax.swing.JComboBox<SFormComponentItem>();
        Dummy5 = new javax.swing.JLabel();
        jckIsUniversalCompanyEntity = new javax.swing.JCheckBox();
        jPanel33 = new javax.swing.JPanel();
        jlCompanyBranchEntity = new javax.swing.JLabel();
        jcbCompanyBranchEntity = new javax.swing.JComboBox<SFormComponentItem>();
        Dummy6 = new javax.swing.JLabel();
        jckIsUniversalBranch = new javax.swing.JCheckBox();
        jpControls = new javax.swing.JPanel();
        jpControls1 = new javax.swing.JPanel();
        jtfPkUserId_Ro = new javax.swing.JTextField();
        jpControls2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Usuario"); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpUser.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpUser.setLayout(new java.awt.BorderLayout());

        jpUser1.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jPanel78.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlUser.setText("Usuario: *"); // NOI18N
        jlUser.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel78.add(jlUser);

        jtfUser.setText("name"); // NOI18N
        jtfUser.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel78.add(jtfUser);

        jckIsUniversal.setText("Con acceso universal a todas las empresas y sucursales"); // NOI18N
        jckIsUniversal.setPreferredSize(new java.awt.Dimension(300, 23));
        jckIsUniversal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jckIsUniversalItemStateChanged(evt);
            }
        });
        jPanel78.add(jckIsUniversal);

        jpUser1.add(jPanel78);

        jPanel79.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlUserPassword.setText("Contraseña: *"); // NOI18N
        jlUserPassword.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel79.add(jlUserPassword);

        jpfUserPassword.setText("pass");
        jpfUserPassword.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel79.add(jpfUserPassword);

        jbEditUserPassword.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditUserPassword.setToolTipText("Cambiar contraseña");
        jbEditUserPassword.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEditUserPassword.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel79.add(jbEditUserPassword);

        jpUser1.add(jPanel79);

        jPanel80.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlUserPasswordConfirm.setText("Confirmar: *"); // NOI18N
        jlUserPasswordConfirm.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel80.add(jlUserPasswordConfirm);

        jpfUserPasswordConfirm.setText("pass");
        jpfUserPasswordConfirm.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel80.add(jpfUserPasswordConfirm);

        jpUser1.add(jPanel80);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlEmail.setText("Correo-e usuario:");
        jlEmail.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel1.add(jlEmail);

        jtfEmail.setText("email");
        jtfEmail.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel1.add(jtfEmail);

        jpUser1.add(jPanel1);

        jPanel81.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner.setText("Empleado usuario:");
        jlBizPartner.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel81.add(jlBizPartner);

        jcbFkBizPartnerId.setMaximumRowCount(16);
        jcbFkBizPartnerId.setPreferredSize(new java.awt.Dimension(500, 23));
        jPanel81.add(jcbFkBizPartnerId);

        jbFkBizPartnerId.setText("jButton1");
        jbFkBizPartnerId.setToolTipText("Seleccionar asociado de negocios");
        jbFkBizPartnerId.setFocusable(false);
        jbFkBizPartnerId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel81.add(jbFkBizPartnerId);

        jpUser1.add(jPanel81);

        jPanel82.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsActive.setText("Cuenta de usuario activa");
        jckIsActive.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel82.add(jckIsActive);

        jLabel1.setForeground(java.awt.Color.gray);
        jLabel1.setText("(Si la cuenta de usuario está inactiva, se denegará el acceso a SIIE.)");
        jLabel1.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel82.add(jLabel1);

        jpUser1.add(jPanel82);

        jPanel83.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsDeleted.setForeground(java.awt.Color.red);
        jckIsDeleted.setText("Registro eliminado");
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel83.add(jckIsDeleted);

        jpUser1.add(jPanel83);

        jpUser.add(jpUser1, java.awt.BorderLayout.NORTH);

        jTabbedPane.addTab("Datos generales", jpUser);

        jpCompanies.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración de acceso:"));
        jpCompanies.setLayout(new java.awt.BorderLayout());

        jpCompanies1.setLayout(new java.awt.BorderLayout());

        jpCompaniesAvailable.setPreferredSize(new java.awt.Dimension(300, 200));
        jpCompaniesAvailable.setLayout(new java.awt.BorderLayout());

        jlCompaniesAvailable.setText("Empresas disponibles:");
        jlCompaniesAvailable.setPreferredSize(new java.awt.Dimension(100, 23));
        jpCompaniesAvailable.add(jlCompaniesAvailable, java.awt.BorderLayout.PAGE_START);

        jltCompaniesAvailable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jltCompaniesAvailableMouseClicked(evt);
            }
        });
        jspCompaniesAvailable.setViewportView(jltCompaniesAvailable);

        jpCompaniesAvailable.add(jspCompaniesAvailable, java.awt.BorderLayout.CENTER);

        jpCompanies1.add(jpCompaniesAvailable, java.awt.BorderLayout.WEST);

        jpCompaniesControls.setPreferredSize(new java.awt.Dimension(200, 100));
        jpCompaniesControls.setLayout(new java.awt.BorderLayout());

        jpCompaniesControls1.setPreferredSize(new java.awt.Dimension(100, 130));
        jpCompaniesControls1.setLayout(new java.awt.BorderLayout());

        jpCompaniesControls1N.setPreferredSize(new java.awt.Dimension(100, 23));
        jpCompaniesControls1N.add(jLabel4);

        jpCompaniesControls1.add(jpCompaniesControls1N, java.awt.BorderLayout.NORTH);

        jpCompaniesControls1E.setPreferredSize(new java.awt.Dimension(25, 100));
        jpCompaniesControls1.add(jpCompaniesControls1E, java.awt.BorderLayout.EAST);

        jpCompaniesControls1W.setPreferredSize(new java.awt.Dimension(25, 100));
        jpCompaniesControls1.add(jpCompaniesControls1W, java.awt.BorderLayout.WEST);

        jpCompaniesControls1C.setLayout(new java.awt.GridLayout(4, 1, 5, 5));

        jbTransferCompany.setText(">");
        jbTransferCompany.setToolTipText("Agregar");
        jpCompaniesControls1C.add(jbTransferCompany);

        jbTransferAllCompany.setText(">>");
        jbTransferAllCompany.setToolTipText("Agregar todos");
        jpCompaniesControls1C.add(jbTransferAllCompany);

        jbReturnCompany.setText("<");
        jbReturnCompany.setToolTipText("Remover");
        jpCompaniesControls1C.add(jbReturnCompany);

        jbReturnAllCompany.setText("<<");
        jbReturnAllCompany.setToolTipText("Remover todos");
        jpCompaniesControls1C.add(jbReturnAllCompany);

        jpCompaniesControls1.add(jpCompaniesControls1C, java.awt.BorderLayout.CENTER);

        jpCompaniesControls.add(jpCompaniesControls1, java.awt.BorderLayout.NORTH);

        jpCompanies1.add(jpCompaniesControls, java.awt.BorderLayout.CENTER);

        jpCompaniesUser.setPreferredSize(new java.awt.Dimension(350, 200));
        jpCompaniesUser.setLayout(new java.awt.BorderLayout());

        jlCompanyUser.setText("Empresas asignadas al usuario:");
        jlCompanyUser.setPreferredSize(new java.awt.Dimension(105, 23));
        jpCompaniesUser.add(jlCompanyUser, java.awt.BorderLayout.PAGE_START);

        jpCompanies1.add(jpCompaniesUser, java.awt.BorderLayout.EAST);

        jpCompanies.add(jpCompanies1, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Acceso a empresas", jpCompanies);

        jpBranches.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración de acceso:"));
        jpBranches.setLayout(new java.awt.BorderLayout(0, 5));

        jpBranches0.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jlCompany.setText("Empresa:");
        jlCompany.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBranches0.add(jlCompany);

        jcbCompanyBranch.setPreferredSize(new java.awt.Dimension(350, 23));
        jcbCompanyBranch.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbCompanyBranchItemStateChanged(evt);
            }
        });
        jpBranches0.add(jcbCompanyBranch);

        jLabel8.setPreferredSize(new java.awt.Dimension(50, 23));
        jpBranches0.add(jLabel8);

        jckIsUniversalCompanyBranch.setText("Con acceso universal a la empresa");
        jckIsUniversalCompanyBranch.setEnabled(false);
        jckIsUniversalCompanyBranch.setFocusPainted(false);
        jckIsUniversalCompanyBranch.setPreferredSize(new java.awt.Dimension(250, 22));
        jpBranches0.add(jckIsUniversalCompanyBranch);

        jpBranches.add(jpBranches0, java.awt.BorderLayout.PAGE_START);

        jpBranches1.setLayout(new java.awt.BorderLayout());

        jpBranches11.setLayout(new java.awt.BorderLayout());

        jpBranchesAvailable.setPreferredSize(new java.awt.Dimension(300, 200));
        jpBranchesAvailable.setLayout(new java.awt.BorderLayout());

        jlBranchesAvailable.setText("Sucursales de disponibles:");
        jlBranchesAvailable.setPreferredSize(new java.awt.Dimension(129, 23));
        jpBranchesAvailable.add(jlBranchesAvailable, java.awt.BorderLayout.NORTH);

        jltBranchesAvailable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jltBranchesAvailableMouseClicked(evt);
            }
        });
        jspBranchesAvailable.setViewportView(jltBranchesAvailable);

        jpBranchesAvailable.add(jspBranchesAvailable, java.awt.BorderLayout.CENTER);

        jpBranches11.add(jpBranchesAvailable, java.awt.BorderLayout.WEST);

        jpBranchesControls.setLayout(new java.awt.BorderLayout());

        jpBranchesControls1.setPreferredSize(new java.awt.Dimension(100, 130));
        jpBranchesControls1.setLayout(new java.awt.BorderLayout());

        jpBranchesControls1N.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBranchesControls1N.setLayout(new java.awt.BorderLayout());
        jpBranchesControls1.add(jpBranchesControls1N, java.awt.BorderLayout.NORTH);

        jpBranchesControls1E.setPreferredSize(new java.awt.Dimension(25, 100));
        jpBranchesControls1E.setLayout(new java.awt.BorderLayout());
        jpBranchesControls1.add(jpBranchesControls1E, java.awt.BorderLayout.EAST);

        jpBranchesControls1W.setPreferredSize(new java.awt.Dimension(25, 100));
        jpBranchesControls1W.setLayout(new java.awt.BorderLayout());
        jpBranchesControls1.add(jpBranchesControls1W, java.awt.BorderLayout.LINE_START);

        jpBranchesControls1C.setLayout(new java.awt.GridLayout(4, 1, 5, 5));

        jbTransferBranch.setText(">");
        jbTransferBranch.setToolTipText("Agregar");
        jpBranchesControls1C.add(jbTransferBranch);

        jbTransferAllBranch.setText(">>");
        jbTransferAllBranch.setToolTipText("Agregar todos");
        jpBranchesControls1C.add(jbTransferAllBranch);

        jbReturnBranch.setText("<");
        jbReturnBranch.setToolTipText("Remover");
        jpBranchesControls1C.add(jbReturnBranch);

        jbReturnAllBranch.setText("<<");
        jbReturnAllBranch.setToolTipText("Remover todos");
        jpBranchesControls1C.add(jbReturnAllBranch);

        jpBranchesControls1.add(jpBranchesControls1C, java.awt.BorderLayout.CENTER);

        jpBranchesControls.add(jpBranchesControls1, java.awt.BorderLayout.NORTH);

        jpBranches11.add(jpBranchesControls, java.awt.BorderLayout.CENTER);

        jpBranchesUser.setPreferredSize(new java.awt.Dimension(350, 200));
        jpBranchesUser.setLayout(new java.awt.BorderLayout());

        jlBranchUser.setText("Sucursales asignadas al usuario:");
        jlBranchUser.setPreferredSize(new java.awt.Dimension(110, 23));
        jpBranchesUser.add(jlBranchUser, java.awt.BorderLayout.PAGE_START);

        jpBranches11.add(jpBranchesUser, java.awt.BorderLayout.EAST);

        jpBranches1.add(jpBranches11, java.awt.BorderLayout.CENTER);

        jpBranches.add(jpBranches1, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Acceso a sucursales de empresas", jpBranches);

        jpEntities.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración de acceso:"));
        jpEntities.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel8.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));

        jckIsUniversalCashAccount.setText("Con acceso universal a cuentas de efectivo");
        jckIsUniversalCashAccount.setPreferredSize(new java.awt.Dimension(250, 23));
        jckIsUniversalCashAccount.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jckIsUniversalCashAccountItemStateChanged(evt);
            }
        });
        jPanel34.add(jckIsUniversalCashAccount);

        jbSetDefaultCashAccount.setText("Marcar default");
        jbSetDefaultCashAccount.setToolTipText("Marcar entidad como predeterminada");
        jbSetDefaultCashAccount.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel34.add(jbSetDefaultCashAccount);

        jPanel8.add(jPanel34, java.awt.BorderLayout.NORTH);

        jPanel35.setLayout(new java.awt.BorderLayout());

        jPanel36.setPreferredSize(new java.awt.Dimension(300, 100));
        jPanel36.setLayout(new java.awt.BorderLayout());

        jlCompanyBranchCashAccount.setText("Cuentas de efectivo disponibles:");
        jlCompanyBranchCashAccount.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel36.add(jlCompanyBranchCashAccount, java.awt.BorderLayout.NORTH);

        jltCompanyBranchCashAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jltCompanyBranchCashAccountMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jltCompanyBranchCashAccount);

        jPanel36.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel35.add(jPanel36, java.awt.BorderLayout.WEST);

        jPanel38.setLayout(new java.awt.BorderLayout());

        jPanel39.setPreferredSize(new java.awt.Dimension(100, 130));
        jPanel39.setLayout(new java.awt.BorderLayout());

        jPanel40.setLayout(new java.awt.BorderLayout());

        jLabel13.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel40.add(jLabel13, java.awt.BorderLayout.CENTER);

        jPanel39.add(jPanel40, java.awt.BorderLayout.NORTH);

        jPanel41.setPreferredSize(new java.awt.Dimension(25, 100));
        jPanel41.setLayout(new java.awt.BorderLayout());
        jPanel39.add(jPanel41, java.awt.BorderLayout.WEST);

        jPanel43.setPreferredSize(new java.awt.Dimension(25, 100));
        jPanel43.setLayout(new java.awt.BorderLayout());
        jPanel39.add(jPanel43, java.awt.BorderLayout.EAST);

        jPanel44.setLayout(new java.awt.GridLayout(4, 1, 5, 5));

        jbTransferCashAccount.setText(">");
        jbTransferCashAccount.setToolTipText("Agregar");
        jPanel44.add(jbTransferCashAccount);

        jbTransferAllCashAccounts.setText(">>");
        jbTransferAllCashAccounts.setToolTipText("Agregar todos");
        jPanel44.add(jbTransferAllCashAccounts);

        jbReturnCashAccount.setText("<");
        jbReturnCashAccount.setToolTipText("Remover");
        jPanel44.add(jbReturnCashAccount);

        jbReturnAllCashAccounts.setText("<<");
        jbReturnAllCashAccounts.setToolTipText("Remover todos");
        jPanel44.add(jbReturnAllCashAccounts);

        jPanel39.add(jPanel44, java.awt.BorderLayout.CENTER);

        jPanel38.add(jPanel39, java.awt.BorderLayout.NORTH);
        jPanel38.add(jPanel42, java.awt.BorderLayout.CENTER);

        jPanel35.add(jPanel38, java.awt.BorderLayout.CENTER);

        jPanel37.setPreferredSize(new java.awt.Dimension(350, 100));
        jPanel37.setLayout(new java.awt.BorderLayout());

        jlUserCashAccount.setText("Cuentas de efectivo asignadas al usuario:");
        jlUserCashAccount.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel37.add(jlUserCashAccount, java.awt.BorderLayout.PAGE_START);

        jltUserCashAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jltUserCashAccountMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jltUserCashAccount);

        jPanel37.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jPanel35.add(jPanel37, java.awt.BorderLayout.EAST);

        jPanel8.add(jPanel35, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("Acceso a cuentas de efectivo", jPanel8);

        jPanel9.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel45.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));

        jckIsUniversalWarehouse.setText("Con acceso universal a almacenes");
        jckIsUniversalWarehouse.setPreferredSize(new java.awt.Dimension(250, 23));
        jckIsUniversalWarehouse.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jckIsUniversalWarehouseItemStateChanged(evt);
            }
        });
        jPanel45.add(jckIsUniversalWarehouse);

        jbSetDefaultWarehouse.setText("Marcar default");
        jbSetDefaultWarehouse.setToolTipText("Marcar entidad como predeterminada");
        jbSetDefaultWarehouse.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel45.add(jbSetDefaultWarehouse);

        jPanel9.add(jPanel45, java.awt.BorderLayout.NORTH);

        jPanel46.setLayout(new java.awt.BorderLayout());

        jPanel47.setPreferredSize(new java.awt.Dimension(300, 100));
        jPanel47.setLayout(new java.awt.BorderLayout());

        jlCompanyBranchWarehouses.setText("Almacenes disponibles:");
        jlCompanyBranchWarehouses.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel47.add(jlCompanyBranchWarehouses, java.awt.BorderLayout.NORTH);

        jltCompanyBranchWarehouses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jltCompanyBranchWarehousesMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jltCompanyBranchWarehouses);

        jPanel47.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        jPanel46.add(jPanel47, java.awt.BorderLayout.WEST);

        jPanel49.setLayout(new java.awt.BorderLayout());

        jPanel50.setPreferredSize(new java.awt.Dimension(100, 130));
        jPanel50.setLayout(new java.awt.BorderLayout());

        jPanel51.setLayout(new java.awt.BorderLayout());

        jLabel17.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel51.add(jLabel17, java.awt.BorderLayout.CENTER);

        jPanel50.add(jPanel51, java.awt.BorderLayout.NORTH);

        jPanel52.setPreferredSize(new java.awt.Dimension(25, 100));
        jPanel50.add(jPanel52, java.awt.BorderLayout.WEST);

        jPanel53.setPreferredSize(new java.awt.Dimension(25, 100));
        jPanel50.add(jPanel53, java.awt.BorderLayout.EAST);

        jPanel54.setLayout(new java.awt.GridLayout(4, 1, 5, 5));

        jbTransferWarehouse.setText(">");
        jbTransferWarehouse.setToolTipText("Agregar");
        jPanel54.add(jbTransferWarehouse);

        jbTransferAllWarehouses.setText(">>");
        jbTransferAllWarehouses.setToolTipText("Agregar todos");
        jPanel54.add(jbTransferAllWarehouses);

        jbReturnWarehouse.setText("<");
        jbReturnWarehouse.setToolTipText("Remover");
        jPanel54.add(jbReturnWarehouse);

        jbReturnAllWarehouses.setText("<<");
        jbReturnAllWarehouses.setToolTipText("Remover todos");
        jPanel54.add(jbReturnAllWarehouses);

        jPanel50.add(jPanel54, java.awt.BorderLayout.CENTER);

        jPanel49.add(jPanel50, java.awt.BorderLayout.NORTH);

        jPanel46.add(jPanel49, java.awt.BorderLayout.CENTER);

        jPanel48.setPreferredSize(new java.awt.Dimension(350, 100));
        jPanel48.setLayout(new java.awt.BorderLayout());

        jlUserWarehouses.setText("Almacenes asignados al usuario:");
        jlUserWarehouses.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel48.add(jlUserWarehouses, java.awt.BorderLayout.NORTH);

        jltUserWarehouses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jltUserWarehousesMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jltUserWarehouses);

        jPanel48.add(jScrollPane6, java.awt.BorderLayout.CENTER);

        jPanel46.add(jPanel48, java.awt.BorderLayout.EAST);

        jPanel9.add(jPanel46, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("Acceso a almacenes", jPanel9);

        jPanel12.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel55.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));

        jckIsUniversalPos.setText("Con acceso universal a puntos de venta");
        jckIsUniversalPos.setPreferredSize(new java.awt.Dimension(250, 23));
        jckIsUniversalPos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jckIsUniversalPosItemStateChanged(evt);
            }
        });
        jPanel55.add(jckIsUniversalPos);

        jbSetDefaultPos.setText("Marcar default");
        jbSetDefaultPos.setToolTipText("Marcar entidad como predeterminada");
        jbSetDefaultPos.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel55.add(jbSetDefaultPos);

        jPanel12.add(jPanel55, java.awt.BorderLayout.NORTH);

        jPanel56.setLayout(new java.awt.BorderLayout());

        jPanel57.setPreferredSize(new java.awt.Dimension(300, 100));
        jPanel57.setLayout(new java.awt.BorderLayout());

        jlCompanyBranchPos.setText("Puntos de venta disponibles:");
        jlCompanyBranchPos.setPreferredSize(new java.awt.Dimension(151, 23));
        jPanel57.add(jlCompanyBranchPos, java.awt.BorderLayout.PAGE_START);

        jltCompanyBranchPos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jltCompanyBranchPosMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(jltCompanyBranchPos);

        jPanel57.add(jScrollPane7, java.awt.BorderLayout.CENTER);

        jPanel56.add(jPanel57, java.awt.BorderLayout.WEST);

        jPanel59.setLayout(new java.awt.BorderLayout());

        jPanel60.setLayout(new java.awt.BorderLayout());

        jPanel61.setPreferredSize(new java.awt.Dimension(100, 130));
        jPanel61.setLayout(new java.awt.BorderLayout());

        jPanel62.setLayout(new java.awt.BorderLayout());

        jLabel21.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel62.add(jLabel21, java.awt.BorderLayout.CENTER);

        jPanel61.add(jPanel62, java.awt.BorderLayout.NORTH);

        jPanel63.setPreferredSize(new java.awt.Dimension(25, 100));
        jPanel61.add(jPanel63, java.awt.BorderLayout.WEST);

        jPanel64.setPreferredSize(new java.awt.Dimension(25, 100));
        jPanel61.add(jPanel64, java.awt.BorderLayout.EAST);

        jPanel65.setLayout(new java.awt.GridLayout(4, 1, 5, 5));

        jbTransferPos.setText(">");
        jbTransferPos.setToolTipText("Agregar");
        jPanel65.add(jbTransferPos);

        jbTransferAllPos.setText(">>");
        jbTransferAllPos.setToolTipText("Agregar todos");
        jPanel65.add(jbTransferAllPos);

        jbReturnPos.setText("<");
        jbReturnPos.setToolTipText("Remover");
        jPanel65.add(jbReturnPos);

        jbReturnAllPos.setText("<<");
        jbReturnAllPos.setToolTipText("Remover todos");
        jPanel65.add(jbReturnAllPos);

        jPanel61.add(jPanel65, java.awt.BorderLayout.CENTER);

        jPanel60.add(jPanel61, java.awt.BorderLayout.NORTH);

        jPanel59.add(jPanel60, java.awt.BorderLayout.CENTER);

        jPanel56.add(jPanel59, java.awt.BorderLayout.CENTER);

        jPanel58.setPreferredSize(new java.awt.Dimension(350, 100));
        jPanel58.setLayout(new java.awt.BorderLayout());

        jlUserPos.setText("Puntos de venta asignados al usuario:");
        jlUserPos.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel58.add(jlUserPos, java.awt.BorderLayout.NORTH);

        jltUserPos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jltUserPosMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(jltUserPos);

        jPanel58.add(jScrollPane8, java.awt.BorderLayout.CENTER);

        jPanel56.add(jPanel58, java.awt.BorderLayout.EAST);

        jPanel12.add(jPanel56, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("Acceso a puntos de venta", jPanel12);

        jPanel24.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel66.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));

        jckIsUniversalPlant.setText("Con acceso universal a plantas");
        jckIsUniversalPlant.setPreferredSize(new java.awt.Dimension(250, 23));
        jckIsUniversalPlant.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jckIsUniversalPlantItemStateChanged(evt);
            }
        });
        jPanel66.add(jckIsUniversalPlant);

        jbSetDefaultPlant.setText("Marcar default");
        jbSetDefaultPlant.setToolTipText("Marcar entidad como predeterminada");
        jbSetDefaultPlant.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel66.add(jbSetDefaultPlant);

        jPanel24.add(jPanel66, java.awt.BorderLayout.PAGE_START);

        jPanel67.setLayout(new java.awt.BorderLayout());

        jPanel68.setPreferredSize(new java.awt.Dimension(300, 100));
        jPanel68.setLayout(new java.awt.BorderLayout());

        jlCompanyBranchPlants.setText("Plantas disponibles:");
        jlCompanyBranchPlants.setPreferredSize(new java.awt.Dimension(107, 23));
        jPanel68.add(jlCompanyBranchPlants, java.awt.BorderLayout.NORTH);

        jltCompanyBranchPlants.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jltCompanyBranchPlantsMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(jltCompanyBranchPlants);

        jPanel68.add(jScrollPane9, java.awt.BorderLayout.CENTER);

        jPanel67.add(jPanel68, java.awt.BorderLayout.WEST);

        jPanel72.setLayout(new java.awt.BorderLayout());

        jPanel73.setPreferredSize(new java.awt.Dimension(100, 130));
        jPanel73.setLayout(new java.awt.BorderLayout());

        jPanel74.setLayout(new java.awt.BorderLayout());

        jLabel25.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel74.add(jLabel25, java.awt.BorderLayout.CENTER);

        jPanel73.add(jPanel74, java.awt.BorderLayout.NORTH);

        jPanel75.setPreferredSize(new java.awt.Dimension(25, 100));
        jPanel73.add(jPanel75, java.awt.BorderLayout.WEST);

        jPanel76.setPreferredSize(new java.awt.Dimension(25, 100));
        jPanel73.add(jPanel76, java.awt.BorderLayout.EAST);

        jPanel77.setLayout(new java.awt.GridLayout(4, 1, 5, 5));

        jbTransferPlant.setText(">");
        jbTransferPlant.setToolTipText("Agregar");
        jPanel77.add(jbTransferPlant);

        jbTransferAllPlants.setText(">>");
        jbTransferAllPlants.setToolTipText("Agregar todos");
        jPanel77.add(jbTransferAllPlants);

        jbReturnPlant.setText("<");
        jbReturnPlant.setToolTipText("Remover");
        jPanel77.add(jbReturnPlant);

        jbReturnAllPlants.setText("<<");
        jbReturnAllPlants.setToolTipText("Remover todos");
        jPanel77.add(jbReturnAllPlants);

        jPanel73.add(jPanel77, java.awt.BorderLayout.CENTER);

        jPanel72.add(jPanel73, java.awt.BorderLayout.NORTH);

        jPanel67.add(jPanel72, java.awt.BorderLayout.CENTER);

        jPanel69.setPreferredSize(new java.awt.Dimension(350, 100));
        jPanel69.setLayout(new java.awt.BorderLayout());

        jlUserPlants.setText("Plantas asignadas al usuario:");
        jlUserPlants.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel69.add(jlUserPlants, java.awt.BorderLayout.PAGE_START);

        jltUserPlants.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jltUserPlantsMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(jltUserPlants);

        jPanel69.add(jScrollPane10, java.awt.BorderLayout.CENTER);

        jPanel67.add(jPanel69, java.awt.BorderLayout.EAST);

        jPanel24.add(jPanel67, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("Acceso a plantas", jPanel24);

        jpEntities.add(jTabbedPane2, java.awt.BorderLayout.CENTER);

        jPanel31.setLayout(new java.awt.GridLayout(2, 1, 5, 5));

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jlCompanyEntity.setText("Empresa:");
        jlCompanyEntity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel32.add(jlCompanyEntity);

        jcbCompanyEntity.setPreferredSize(new java.awt.Dimension(350, 23));
        jcbCompanyEntity.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbCompanyEntityItemStateChanged(evt);
            }
        });
        jPanel32.add(jcbCompanyEntity);

        Dummy5.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel32.add(Dummy5);

        jckIsUniversalCompanyEntity.setText("Con acceso universal a la empresa");
        jckIsUniversalCompanyEntity.setEnabled(false);
        jckIsUniversalCompanyEntity.setFocusable(false);
        jckIsUniversalCompanyEntity.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel32.add(jckIsUniversalCompanyEntity);

        jPanel31.add(jPanel32);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jlCompanyBranchEntity.setText("Sucursal empresa:");
        jlCompanyBranchEntity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel33.add(jlCompanyBranchEntity);

        jcbCompanyBranchEntity.setPreferredSize(new java.awt.Dimension(350, 23));
        jcbCompanyBranchEntity.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbCompanyBranchEntityItemStateChanged(evt);
            }
        });
        jPanel33.add(jcbCompanyBranchEntity);

        Dummy6.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel33.add(Dummy6);

        jckIsUniversalBranch.setText("Con acceso universal a la sucursal");
        jckIsUniversalBranch.setEnabled(false);
        jckIsUniversalBranch.setFocusable(false);
        jckIsUniversalBranch.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel33.add(jckIsUniversalBranch);

        jPanel31.add(jPanel33);

        jpEntities.add(jPanel31, java.awt.BorderLayout.PAGE_START);

        jTabbedPane.addTab("Acceso a entidades de sucursales", jpEntities);

        getContentPane().add(jTabbedPane, java.awt.BorderLayout.CENTER);

        jpControls.setLayout(new java.awt.GridLayout(1, 2));

        jpControls1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jtfPkUserId_Ro.setEditable(false);
        jtfPkUserId_Ro.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPkUserId_Ro.setToolTipText("ID del registro");
        jtfPkUserId_Ro.setFocusable(false);
        jtfPkUserId_Ro.setPreferredSize(new java.awt.Dimension(65, 23));
        jpControls1.add(jtfPkUserId_Ro);

        jpControls.add(jpControls1);

        jpControls2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar"); // NOI18N
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls2.add(jbOk);

        jbCancel.setText("Cancelar"); // NOI18N
        jbCancel.setToolTipText("[Escape]");
        jpControls2.add(jbCancel);

        jpControls.add(jpControls2);

        getContentPane().add(jpControls, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(808, 534));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jcbCompanyBranchEntityItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbCompanyBranchEntityItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            itemStateChangedCompanyBranchEntity();
        }
}//GEN-LAST:event_jcbCompanyBranchEntityItemStateChanged

    private void jcbCompanyEntityItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbCompanyEntityItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            itemStateChangedCompanyEntity();
        }
}//GEN-LAST:event_jcbCompanyEntityItemStateChanged

    private void jltBranchesAvailableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jltBranchesAvailableMouseClicked
        if (evt.getClickCount() == 2) {
            mouseClickedCompanyBranches();
        }
}//GEN-LAST:event_jltBranchesAvailableMouseClicked

    private void jcbCompanyBranchItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbCompanyBranchItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            itemStateChangedCompanyBranch();
        }
}//GEN-LAST:event_jcbCompanyBranchItemStateChanged

    private void jltCompaniesAvailableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jltCompaniesAvailableMouseClicked
        if (evt.getClickCount() == 2) {
            mouseClickedCompanySystem();
        }
}//GEN-LAST:event_jltCompaniesAvailableMouseClicked

    private void jckIsUniversalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jckIsUniversalItemStateChanged
        itemStateChangedIsUniversal();
}//GEN-LAST:event_jckIsUniversalItemStateChanged

    private void jltCompanyBranchCashAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jltCompanyBranchCashAccountMouseClicked
        if (evt.getClickCount() == 2) {
            mouseClickedCompanyBranchCashAccounts();
        }
    }//GEN-LAST:event_jltCompanyBranchCashAccountMouseClicked

    private void jltUserCashAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jltUserCashAccountMouseClicked
        if (evt.getClickCount() == 2) {
            mouseClickedUserCashAccounts();
        }
    }//GEN-LAST:event_jltUserCashAccountMouseClicked

    private void jltCompanyBranchWarehousesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jltCompanyBranchWarehousesMouseClicked
        if (evt.getClickCount() == 2) {
            mouseClickedCompanyBranchWarehouses();
        }
    }//GEN-LAST:event_jltCompanyBranchWarehousesMouseClicked

    private void jltUserWarehousesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jltUserWarehousesMouseClicked
        if (evt.getClickCount() == 2) {
            mouseClickedUserWarehouses();
        }
    }//GEN-LAST:event_jltUserWarehousesMouseClicked

    private void jltCompanyBranchPosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jltCompanyBranchPosMouseClicked
        if (evt.getClickCount() == 2) {
            mouseClickedCompanyBranchPos();
        }
    }//GEN-LAST:event_jltCompanyBranchPosMouseClicked

    private void jltUserPosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jltUserPosMouseClicked
        if (evt.getClickCount() == 2) {
            mouseClickedUserPos();
        }
    }//GEN-LAST:event_jltUserPosMouseClicked

    private void jltCompanyBranchPlantsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jltCompanyBranchPlantsMouseClicked
        if (evt.getClickCount() == 2) {
            mouseClickedCompanyBranchPlants();
        }
    }//GEN-LAST:event_jltCompanyBranchPlantsMouseClicked

    private void jltUserPlantsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jltUserPlantsMouseClicked
        if (evt.getClickCount() == 2) {
            mouseClickedUserPlants();
        }
    }//GEN-LAST:event_jltUserPlantsMouseClicked

    private void jckIsUniversalCashAccountItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jckIsUniversalCashAccountItemStateChanged
        if (!mbResetingForm) {
            itemStateChangedIsUniversalCashAccount();
        }
    }//GEN-LAST:event_jckIsUniversalCashAccountItemStateChanged

    private void jckIsUniversalWarehouseItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jckIsUniversalWarehouseItemStateChanged
        if (!mbResetingForm) {
            itemStateChangedIsUniversalWarehouse();
        }
    }//GEN-LAST:event_jckIsUniversalWarehouseItemStateChanged

    private void jckIsUniversalPosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jckIsUniversalPosItemStateChanged
        if (!mbResetingForm) {
            itemStateChangedIsUniversalPos();
        }
    }//GEN-LAST:event_jckIsUniversalPosItemStateChanged

    private void jckIsUniversalPlantItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jckIsUniversalPlantItemStateChanged
        if (!mbResetingForm) {
            itemStateChangedIsUniversalPlant();
        }
    }//GEN-LAST:event_jckIsUniversalPlantItemStateChanged

    private void initComponentsExtra() {
        int i;

        erp.lib.table.STableColumnForm tableColumnsUserCompanies[];
        erp.lib.table.STableColumnForm tableColumnsUserCompanyBranches[];

        mvEmptyListItems = new Vector<>();

        moUserCompaniesPane = new STablePane(miClient);
        moUserCompaniesPane.setDoubleClickAction(this, "publicActionReturnCompany");
        jpCompaniesUser.add(moUserCompaniesPane, BorderLayout.CENTER);

        moUserCompanyBranchesPane = new STablePane(miClient);
        moUserCompanyBranchesPane.setDoubleClickAction(this, "publicActionReturnBranch");
        jpBranchesUser.add(moUserCompanyBranchesPane, BorderLayout.CENTER);

        moFieldUser = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfUser, jlUser);
        moFieldUser.setLengthMin(5);
        moFieldUser.setLengthMax(50);
        moFieldUser.setAutoCaseType(SLibConstants.CASE_LOWER);
        moFieldUser.setTabbedPaneIndex(0, jTabbedPane);
        moFieldEmail = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfEmail, jlEmail);
        moFieldEmail.setAutoCaseType(SLibConstants.CASE_LOWER);
        moFieldIsUniversalCompanies = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsUniversal);
        moFieldIsUniversalCompanies.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsDeleted = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDeleted);
        moFieldIsDeleted.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsActive = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsActive);
        moFieldIsActive.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkBizPartnerId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkBizPartnerId, jlBizPartner);
        moFieldFkBizPartnerId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkBizPartnerId.setPickerButton(jbFkBizPartnerId);
        moFieldCompany = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jltCompaniesAvailable, jlCompaniesAvailable);
        moFieldCompany.setTabbedPaneIndex(1, jTabbedPane);
        moFieldFkCompanyId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCompanyBranch, jlCompany);
        moFieldFkCompanyId.setTabbedPaneIndex(2, jTabbedPane);
        moFieldBranch = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jltBranchesAvailable, jlBranchesAvailable);
        moFieldBranch.setTabbedPaneIndex(2, jTabbedPane);
        moFieldWarehouse = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jltCompanyBranchWarehouses, jlCompanyBranchWarehouses);
        moFieldFkCompanyEntityId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCompanyEntity, jlCompanyEntity);
        moFieldFkCompanyEntityId.setTabbedPaneIndex(3, jTabbedPane);
        moFieldFkCompanyBranchEntityId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCompanyBranchEntity, jlCompanyBranchEntity);
        moFieldFkCompanyBranchEntityId.setTabbedPaneIndex(3, jTabbedPane);
        moFieldCashAccount = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jltCompanyBranchCashAccount, jlCompanyBranchCashAccount);
        moFieldCashAccount.setTabbedPaneIndex(3, jTabbedPane);
        moFieldUserCashAccount = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jltUserCashAccount, jlUserCashAccount);
        moFieldUserCashAccount.setListValidationType(SLibConstants.LIST_VALIDATION_BY_CONTENT);
        moFieldUserCashAccount.setTabbedPaneIndex(3, jTabbedPane);
        moFieldWarehouse = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jltCompanyBranchWarehouses, jlCompanyBranchWarehouses);
        moFieldWarehouse.setTabbedPaneIndex(3, jTabbedPane);
        moFieldUserWarehouse = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jltUserWarehouses, jlUserWarehouses);
        moFieldUserWarehouse.setListValidationType(SLibConstants.LIST_VALIDATION_BY_CONTENT);
        moFieldUserWarehouse.setTabbedPaneIndex(3, jTabbedPane);
        moFieldPos = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jltCompanyBranchPos, jlCompanyBranchPos);
        moFieldPos.setTabbedPaneIndex(3, jTabbedPane);
        moFieldUserPos = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jltUserPos, jlUserPos);
        moFieldUserPos.setListValidationType(SLibConstants.LIST_VALIDATION_BY_CONTENT);
        moFieldUserPos.setTabbedPaneIndex(3, jTabbedPane);
        moFieldPlant = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jltCompanyBranchPlants, jlCompanyBranchPlants);
        moFieldPlant.setTabbedPaneIndex(3, jTabbedPane);
        moFieldUserPlant = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jltUserPlants, jlUserPlants);
        moFieldUserPlant.setListValidationType(SLibConstants.LIST_VALIDATION_BY_CONTENT);
        moFieldUserPlant.setTabbedPaneIndex(3, jTabbedPane);

        mvFields = new Vector<>();
        mvFields.add(moFieldUser);
        mvFields.add(moFieldEmail);
        mvFields.add(moFieldIsUniversalCompanies);
        mvFields.add(moFieldIsDeleted);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbEditUserPassword.addActionListener(this);
        jbFkBizPartnerId.addActionListener(this);
        jbTransferCompany.addActionListener(this);
        jbTransferAllCompany.addActionListener(this);
        jbReturnCompany.addActionListener(this);
        jbReturnAllCompany.addActionListener(this);
        jbTransferBranch.addActionListener(this);
        jbTransferAllBranch.addActionListener(this);
        jbReturnBranch.addActionListener(this);
        jbReturnAllBranch.addActionListener(this);
        jbTransferCashAccount.addActionListener(this);
        jbTransferAllCashAccounts.addActionListener(this);
        jbReturnCashAccount.addActionListener(this);
        jbReturnAllCashAccounts.addActionListener(this);
        jbTransferWarehouse.addActionListener(this);
        jbTransferAllWarehouses.addActionListener(this);
        jbReturnWarehouse.addActionListener(this);
        jbReturnAllWarehouses.addActionListener(this);
        jbTransferPos.addActionListener(this);
        jbTransferAllPos.addActionListener(this);
        jbReturnPos.addActionListener(this);
        jbReturnAllPos.addActionListener(this);
        jbTransferPlant.addActionListener(this);
        jbTransferAllPlants.addActionListener(this);
        jbReturnPlant.addActionListener(this);
        jbReturnAllPlants.addActionListener(this);
        jbSetDefaultCashAccount.addActionListener(this);
        jbSetDefaultWarehouse.addActionListener(this);
        jbSetDefaultPos.addActionListener(this);
        jbSetDefaultPlant.addActionListener(this);

        mvUserBranches = new Vector<>();
        mvUserCashAccounts = new Vector<>();
        mvUserWarehouses = new Vector<>();
        mvUserPos = new Vector<>();
        mvUserPlants = new Vector<>();

        i = 0;
        tableColumnsUserCompanies = new STableColumnForm[3];
        tableColumnsUserCompanies[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Empresa", 225);
        tableColumnsUserCompanies[i] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Predeterminado", STableConstants.WIDTH_BOOLEAN);
        tableColumnsUserCompanies[i++].setEditable(true);
        tableColumnsUserCompanies[i] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Universal", STableConstants.WIDTH_BOOLEAN);
        tableColumnsUserCompanies[i++].setEditable(true);

        for (i = 0; i < tableColumnsUserCompanies.length; i++) {
            moUserCompaniesPane.addTableColumn(tableColumnsUserCompanies[i]);
        }

        i = 0;
        tableColumnsUserCompanyBranches = new STableColumnForm[3];
        tableColumnsUserCompanyBranches[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Sucursal", 225);
        tableColumnsUserCompanyBranches[i] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Predeterminado", STableConstants.WIDTH_BOOLEAN);
        tableColumnsUserCompanyBranches[i++].setEditable(true);
        tableColumnsUserCompanyBranches[i] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Universal", STableConstants.WIDTH_BOOLEAN);
        tableColumnsUserCompanyBranches[i++].setEditable(true);

        for (i = 0; i < tableColumnsUserCompanyBranches.length; i++) {
            moUserCompanyBranchesPane.addTableColumn(tableColumnsUserCompanyBranches[i]);
        }

        readErpInformation();

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
        mbResetCompanyBranchEntities = false;
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jTabbedPane.setSelectedIndex(0);
            jtfUser.requestFocus();
        }
    }

    private void actionEditUserPassword() {
        jbEditUserPassword.setEnabled(false);
        jpfUserPassword.setEnabled(true);
        jpfUserPasswordConfirm.setEnabled(true);
        jpfUserPassword.requestFocusInWindow();
    }

    private void actionEdit(boolean edit) {
        
    }

    private void actionOk() {
        erp.lib.form.SFormValidation validation = formValidate();

        if (validation.getIsError()) {
            if (validation.getTabbedPaneIndex() >= 0) {
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

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void actionFkBizPartnerId() {
        miClient.pickOption(SDataConstants.BPSU_BP, moFieldFkBizPartnerId, null);
    }

    private void readErpInformation() {
        Vector<SDataRegistry> vector = null;
        int i = 0;
        int j = 0;

        mvDbmsCompanies = new Vector<>();
        mvDbmsCompanyBranches = new Vector<>();
        mvDbmsCashAccounts = new Vector<>();
        mvDbmsWarehouses = new Vector<>();
        mvDbmsPos = new Vector<>();
        mvDbmsPlants = new Vector<>();

        // Read the companies:

        vector = SDataReadRegistries.readRegistries(miClient, SDataConstants.CFGU_CO, null);
        mvDbmsCompanies.clear();
        for (i = 0; i < vector.size(); i++) {
            mvDbmsCompanies.add((SDataCompany)vector.get(i));
        }

        // Read the branches of the companies:

        mvDbmsCompanyBranches.clear();
        for (i = 0; i < mvDbmsCompanies.size(); i++) {
            vector = SDataReadRegistries.readRegistries(miClient, SDataConstants.BPSU_BPB, new int[] { mvDbmsCompanies.get(i).getPkCompanyId() });
            for (j = 0; j < vector.size(); j++) {
                mvDbmsCompanyBranches.add((SDataBizPartnerBranch)vector.get(j));
            }
        }

        // Read the entities of the branches:

        mvDbmsCashAccounts.clear();
        mvDbmsWarehouses.clear();
        mvDbmsPos.clear();
        mvDbmsPlants.clear();
        for (i = 0; i < mvDbmsCompanyBranches.size(); i++) {
            vector = SDataReadRegistries.readRegistries(miClient, SDataConstants.CFGU_COB_ENT, new int[] { mvDbmsCompanyBranches.get(i).getPkBizPartnerBranchId() });
            for (j = 0; j < vector.size(); j++) {
                if (((SDataCompanyBranchEntity)vector.get(j)).getFkEntityCategoryId() == SDataConstantsSys.CFGS_CT_ENT_CASH) {
                    mvDbmsCashAccounts.add((SDataCompanyBranchEntity)vector.get(j));
                }
                else if (((SDataCompanyBranchEntity)vector.get(j)).getFkEntityCategoryId() == SDataConstantsSys.CFGS_CT_ENT_WH) {
                    mvDbmsWarehouses.add((SDataCompanyBranchEntity)vector.get(j));
                }
                else if (((SDataCompanyBranchEntity)vector.get(j)).getFkEntityCategoryId() == SDataConstantsSys.CFGS_CT_ENT_POS) {
                    mvDbmsPos.add((SDataCompanyBranchEntity)vector.get(j));
                }
                else if (((SDataCompanyBranchEntity)vector.get(j)).getFkEntityCategoryId() == SDataConstantsSys.CFGS_CT_ENT_PLANT) {
                    mvDbmsPlants.add((SDataCompanyBranchEntity)vector.get(j));
                }
            }
        }
    }

    private void createDataVectors() {
        int i = 0;
        STableRowCustom row = null;
        Vector<STableRowCustom> rows = null;
        Vector<SFormComponentItem> items = null;

        mvUserBranches.clear();
        for (i = 0; i < mvDbmsCompanies.size(); i++) {
            rows = new Vector<>();
            row = new STableRowCustom();
            row.setPrimaryKey(new int[] { mvDbmsCompanies.get(i).getPkCompanyId() });
            row.getValues().add(mvDbmsCompanies.get(i).getCompany());
            rows.add(row);
            mvUserBranches.add(rows);
        }

        mvUserCashAccounts.clear();
        for (i = 0; i < mvDbmsCompanyBranches.size(); i++) {
            items = new Vector<>();
            items.add(new SFormComponentItem(new int[]{ mvDbmsCompanyBranches.get(i).getPkBizPartnerBranchId() }, mvDbmsCompanyBranches.get(i).getBizPartnerBranch()));
            mvUserCashAccounts.add(items);
        }

        mvUserWarehouses.clear();
        for (i = 0; i < mvDbmsCompanyBranches.size(); i++) {
            items = new Vector<>();
            items.add(new SFormComponentItem(new int[]{ mvDbmsCompanyBranches.get(i).getPkBizPartnerBranchId() }, mvDbmsCompanyBranches.get(i).getBizPartnerBranch()));
            mvUserWarehouses.add(items);
        }

        mvUserPos.clear();
        for (i = 0; i < mvDbmsCompanyBranches.size(); i++) {
            items = new Vector<>();
            items.add(new SFormComponentItem(new int[]{ mvDbmsCompanyBranches.get(i).getPkBizPartnerBranchId() }, mvDbmsCompanyBranches.get(i).getBizPartnerBranch()));
            mvUserPos.add(items);
        }

        mvUserPlants.clear();
        for (i = 0; i < mvDbmsCompanyBranches.size(); i++) {
            items = new Vector<>();
            items.add(new SFormComponentItem(new int[]{ mvDbmsCompanyBranches.get(i).getPkBizPartnerBranchId() }, mvDbmsCompanyBranches.get(i).getBizPartnerBranch()));
            mvUserPlants.add(items);
        }
    }

    private void itemStateChangedCompanyBranchEntity() {
        mbResetCompanyBranchEntities = true;
        populateListUserCashAccounts();
        populateListCompanyBranchCashAccounts();
        populateListUserWarehouses();
        populateListCompanyBranchWarehouses();
        populateListUserPos();
        populateListCompanyBranchPos();
        populateListUserPlants();
        populateListCompanyBranchPlants();
        populateEntityCheckBox(moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]);
        jckIsUniversalBranch.setSelected(getIsUniversalCompanyBranch(moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]));
        mbResetCompanyBranchEntities = false;
    }

   private void itemStateChangedCompanyEntity() {
        renderComboBoxBranch(moFieldFkCompanyEntityId.getKeyAsIntArray()[0], jcbCompanyBranchEntity);
        jckIsUniversalCompanyEntity.setSelected(getIsUniversalCompany(moFieldFkCompanyEntityId.getKeyAsIntArray()[0]));
   }

   private void itemStateChangedCompanyBranch() {
        populateTableUserBranches();
        populateListCompanyBranches();
        jckIsUniversalCompanyBranch.setSelected(getIsUniversalCompany(moFieldFkCompanyId.getKeyAsIntArray()[0]));
   }

   private void itemStateChangedIsUniversal() {
       renderButtons();
   }

   private void itemStateChangedIsUniversalCashAccount() {
       if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
           for (int i = 0; i < mvUserBranches.size(); i++) {
                for (int j = 1; j < mvUserBranches.get(i).size(); j++) {
                    if (((int[]) mvUserBranches.get(i).get(j).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                        for (int k = 0; k < mvUserCashAccounts.size(); k++) {
                            if (((int[]) mvUserCashAccounts.get(k).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] && mvUserCashAccounts.get(k).size() > 1 && jckIsUniversalCashAccount.isSelected()) {
                                if (miClient.showMsgBoxConfirm("Si define un acceso universal se eliminará la configuración actual para las cuentas de efectivo de la sucursal '" + ((SFormComponentItem)jcbCompanyBranchEntity.getSelectedItem()).getItem() + "'.\n" +
                                        "¿Desea continuar?") == JOptionPane.YES_OPTION) {
                                    actionReturnAllCashAccounts();
                                }
                                else {
                                    jckIsUniversalCashAccount.setSelected(false);
                                }
                            }
                        }
                        
                        renderButtonsCashAccountIsUniversal();
                        if (!mbResetCompanyBranchEntities) {
                            mvUserBranches.get(i).get(j).getValues().set(3, jckIsUniversalCashAccount.isSelected());
                        }
                    }
                }
            }
       }
   }

   private void itemStateChangedIsUniversalWarehouse() {
       if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
           for (int i = 0; i < mvUserBranches.size(); i++) {
                for (int j = 1; j < mvUserBranches.get(i).size(); j++) {
                    if (((int[])mvUserBranches.get(i).get(j).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                        for (int k = 0; k < mvUserWarehouses.size(); k++) {
                            if (((int[])mvUserWarehouses.get(k).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] && mvUserWarehouses.get(k).size() > 1 && jckIsUniversalWarehouse.isSelected()) {
                                if (miClient.showMsgBoxConfirm("Si define un acceso universal se eliminará la configuración actual para los almacenes de la sucursal '" + ((SFormComponentItem)jcbCompanyBranchEntity.getSelectedItem()).getItem() + "'.\n" +
                                        "¿Desea continuar?") == JOptionPane.YES_OPTION) {
                                    actionReturnAllWarehouses();
                                }
                                else {
                                    jckIsUniversalWarehouse.setSelected(false);
                                }
                            }
                        }
                        
                        renderButtonsWarehouseIsUniversal();
                        if (!mbResetCompanyBranchEntities) {
                            mvUserBranches.get(i).get(j).getValues().set(4, jckIsUniversalWarehouse.isSelected());
                        }
                    }
                }
            }
       }
   }

   private void itemStateChangedIsUniversalPos() {
       if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
           for (int i = 0; i < mvUserBranches.size(); i++) {
                for (int j = 1; j < mvUserBranches.get(i).size(); j++) {
                    if (((int[])mvUserBranches.get(i).get(j).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                        for (int k = 0; k < mvUserPos.size(); k++) {
                            if (((int[])mvUserPos.get(k).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] && mvUserPos.get(k).size() > 1 && jckIsUniversalPos.isSelected()) {
                                if (miClient.showMsgBoxConfirm("Si define un acceso universal se eliminará la configuración actual para los puntos de venta de la sucursal '" + ((SFormComponentItem)jcbCompanyBranchEntity.getSelectedItem()).getItem() + "'.\n" +
                                        "¿Desea continuar?") == JOptionPane.YES_OPTION) {
                                    actionReturnAllPos();
                                }
                                else {
                                    jckIsUniversalPos.setSelected(false);
                                }
                            }
                        }
                        
                        renderButtonsPosIsUniversal();
                        if (!mbResetCompanyBranchEntities) {
                            mvUserBranches.get(i).get(j).getValues().set(5, jckIsUniversalPos.isSelected());
                        }
                    }
                }
            }
       }
   }

   private void itemStateChangedIsUniversalPlant() {
       if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
           for (int i = 0; i < mvUserBranches.size(); i++) {
                for (int j = 1; j < mvUserBranches.get(i).size(); j++) {
                    if (((int[])mvUserBranches.get(i).get(j).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                        for (int k = 0; k < mvUserPlants.size(); k++) {
                            if (((int[])mvUserPlants.get(k).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] && mvUserPlants.get(k).size() > 1 && jckIsUniversalPlant.isSelected()) {
                                if (miClient.showMsgBoxConfirm("Si define un acceso universal se eliminará la configuración actual para las plantas de la sucursal '" + ((SFormComponentItem)jcbCompanyBranchEntity.getSelectedItem()).getItem() + "'.\n" +
                                        "¿Desea continuar?") == JOptionPane.YES_OPTION) {
                                    actionReturnAllPlants();
                                }
                                else {
                                    jckIsUniversalPlant.setSelected(false);
                                }
                            }
                        }
                        
                        renderButtonsPlantsIsUniversal();
                        if (!mbResetCompanyBranchEntities) {
                            mvUserBranches.get(i).get(j).getValues().set(6, jckIsUniversalPlant.isSelected());
                        }
                    }
                }
            }
       }
   }

   private void mouseClickedCompanyBranches() {
       actionTransferBranch();
   }

   private void mouseClickedCompanySystem() {
       actionTransferCompany();
   }

   private void mouseClickedCompanyBranchCashAccounts() {
       actionTransferCashAccount();
   }

   private void mouseClickedUserCashAccounts() {
       actionReturnCashAccount();
   }

   private void mouseClickedCompanyBranchWarehouses() {
       actionTransferWarehouse();
   }

   private void mouseClickedUserWarehouses() {
       actionReturnWarehouse();
   }

   private void mouseClickedCompanyBranchPos() {
       actionTransferPos();
   }

   private void mouseClickedUserPos() {
       actionReturnPos();
   }

   private void mouseClickedCompanyBranchPlants() {
       actionTransferPlant();
   }

   private void mouseClickedUserPlants() {
       actionReturnPlant();
   }

   private void actionTransferCompany() {
        int pkCompany = 0;
        String company = "";
        STableRowCustom row = null;

        if (jltCompaniesAvailable.getSelectedIndex() != -1) {
            pkCompany = ((int[])((SFormComponentItem)jltCompaniesAvailable.getModel().getElementAt(jltCompaniesAvailable.getSelectedIndex())).getPrimaryKey())[0];
            company = ((SFormComponentItem)jltCompaniesAvailable.getModel().getElementAt(jltCompaniesAvailable.getSelectedIndex())).getItem();
            SFormUtilities.removeListSelectedItem(jltCompaniesAvailable);
            row = new STableRowCustom();
            row.getValues().add(company);
            row.getValues().add(false);
            row.getValues().add(false);
            row.setPrimaryKey(new int[] { pkCompany });
            moUserCompaniesPane.addTableRow(row);
            moUserCompaniesPane.renderTableRows();
            if (jltCompaniesAvailable.getModel().getSize() >=0) {
                jltCompaniesAvailable.setSelectedIndex(0);
            }
            
            populateComboBoxCompany();
            renderButtonsBranch();
        }
        
        renderComboBoxCompany();
    }

    private void actionTransferAllCompany() {
        int pkCompany = 0;
        String company = "";
        STableRowCustom row = null;

        if (jltCompaniesAvailable.getModel().getSize() > 0) {
            for (int i = 0; i < jltCompaniesAvailable.getModel().getSize(); i++) {
                jltCompaniesAvailable.setSelectedIndex(i);
                pkCompany = ((int[]) ((SFormComponentItem) jltCompaniesAvailable.getModel().getElementAt(jltCompaniesAvailable.getSelectedIndex())).getPrimaryKey())[0];
                company = ((SFormComponentItem)jltCompaniesAvailable.getModel().getElementAt(jltCompaniesAvailable.getSelectedIndex())).getItem();
                row = new STableRowCustom();
                row.getValues().add(company);
                row.getValues().add(false);
                row.getValues().add(false);
                row.setPrimaryKey(new int[] { pkCompany });
                moUserCompaniesPane.addTableRow(row);
            }
            
            populateComboBoxCompany();
            renderButtonsBranch();
            moUserCompaniesPane.renderTableRows();
            jltCompaniesAvailable.setListData(mvEmptyListItems);
        }
        
        renderComboBoxCompany();
    }

    private void actionReturnCompany() {
        SFormComponentItem item = null;

        if (moUserCompaniesPane.getSelectedTableRow() != null) {
            if (validateReturnCompany(((int[]) moUserCompaniesPane.getSelectedTableRow().getPrimaryKey())[0])) {
                if (miClient.showMsgBoxConfirm("Ya existe una configuración para la empresa \n" + "'" +(String) moUserCompaniesPane.getSelectedTableRow().getValues().get(0) + "'" +
                        ". \nEste movimiento borrará dicha configuración. ¿Desea continuar?") == JOptionPane.YES_OPTION) {
                    removeBranches(((int[])moUserCompaniesPane.getSelectedTableRow().getPrimaryKey())[0]);
                    item = new SFormComponentItem(new int[] { ((int[])moUserCompaniesPane.getSelectedTableRow().getPrimaryKey())[0] }, (String) moUserCompaniesPane.getSelectedTableRow().getValues().get(0));
                    moUserCompaniesPane.removeTableRow(moUserCompaniesPane.getTable().getSelectedRow());
                    SFormUtilities.addListItem(jltCompaniesAvailable, item);
                    moUserCompaniesPane.renderTableRows();
                }
            }
            else {
                item = new SFormComponentItem(new int[] { ((int[]) moUserCompaniesPane.getSelectedTableRow().getPrimaryKey())[0] }, (String) moUserCompaniesPane.getSelectedTableRow().getValues().get(0));
                moUserCompaniesPane.removeTableRow(moUserCompaniesPane.getTable().getSelectedRow());
                SFormUtilities.addListItem(jltCompaniesAvailable, item);
                moUserCompaniesPane.renderTableRows();
            }
            
            populateComboBoxCompany();
        }
        
        populateTableUserBranches();
        renderComboBoxCompany();
    }

    private void actionReturnAllCompany() {
        Vector<SFormComponentItem> vListCompanies = new Vector<>();
        STableRow row = null;
        int i;

        vListCompanies.clear();
        for (i = 0; i < moUserCompaniesPane.getTableModel().getRowCount(); i++) {
            row = moUserCompaniesPane.getTableRow(i);
            if (validateReturnCompany(((int[])row.getPrimaryKey())[0])) {
                if (miClient.showMsgBoxConfirm("Ya existe una configuración para la empresa \n" + "'" +(String) row.getValues().get(0) + "'" +
                        ". \nEste movimiento borrará dicha configuración. ¿Desea continuar?") == JOptionPane.YES_OPTION) {
                    removeBranches(((int[])row.getPrimaryKey())[0]);
                    vListCompanies.add(new SFormComponentItem(new int[] { ((int[])row.getPrimaryKey())[0] }, (String) row.getValues().get(0)));
                }
            }
            else {
                vListCompanies.add(new SFormComponentItem(new int[] { ((int[])row.getPrimaryKey())[0] }, (String) row.getValues().get(0)));
            }
        }
        for (i = 0; i < vListCompanies.size(); i++) {
            SFormUtilities.addListItem(jltCompaniesAvailable, vListCompanies.get(i));

            for (int j = 0; j < moUserCompaniesPane.getTableModel().getRowCount(); j++) {
                if (((int []) moUserCompaniesPane.getTableRow(j).getPrimaryKey())[0] == ((int []) vListCompanies.get(i).getPrimaryKey())[0]) {
                    moUserCompaniesPane.removeTableRow(j);
                    moUserCompaniesPane.renderTableRows();
                }
            }
        }
        
        //moUserCompaniesPane.clearTableRows();
        populateTableUserBranches();
        populateComboBoxCompany();
        renderComboBoxCompany();
    }

    private void actionTransferBranch() {
        int pkBranch = 0;
        int pkCompany = 0;
        String branch = "";
        STableRowCustom row = null;

        if (jltBranchesAvailable.getSelectedIndex() != -1) {
            pkBranch = ((int[]) ((SFormComponentItem) jltBranchesAvailable.getModel().getElementAt(jltBranchesAvailable.getSelectedIndex())).getPrimaryKey())[0];
            pkCompany = ((int[]) ((SFormComponentItem) jltBranchesAvailable.getModel().getElementAt(jltBranchesAvailable.getSelectedIndex())).getPrimaryKey())[1];
            branch = ((SFormComponentItem) jltBranchesAvailable.getModel().getElementAt(jltBranchesAvailable.getSelectedIndex())).getItem();
            SFormUtilities.removeListSelectedItem(jltBranchesAvailable);
            row = new STableRowCustom();
            row.setPrimaryKey(new int[] { pkBranch, pkCompany });
            row.getValues().add(branch);
            row.getValues().add(false);
            row.getValues().add(false);
            row.getValues().add(false);
            row.getValues().add(false);
            row.getValues().add(false);
            row.getValues().add(false);
            moUserCompanyBranchesPane.addTableRow(row);
            for (int i = 0; i < mvUserBranches.size(); i++) {
                if (((int[]) mvUserBranches.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyId.getKeyAsIntArray()[0]) {
                    mvUserBranches.get(i).add(row);
                    populateTableUserBranches();
                }
            }

            moUserCompanyBranchesPane.renderTableRows();
        }
    }

    private void actionTransferAllBranch() {
        STableRowCustom row = null;
        int pkBranch = 0;
        int pkCompany = 0;

        if (jltBranchesAvailable.getModel().getSize() > 0) {
            for (int i = 0; i < jltBranchesAvailable.getModel().getSize(); i++) {
                row = new STableRowCustom();
                pkBranch = ((int[]) ((SFormComponentItem) jltBranchesAvailable.getModel().getElementAt(i)).getPrimaryKey())[0];
                pkCompany = ((int[]) ((SFormComponentItem) jltBranchesAvailable.getModel().getElementAt(i)).getPrimaryKey())[1];
                row.setPrimaryKey(new int[] { pkBranch, pkCompany });
                row.getValues().add(((SFormComponentItem) jltBranchesAvailable.getModel().getElementAt(i)).getItem());
                row.getValues().add(false);
                row.getValues().add(false);
                row.getValues().add(false);
                row.getValues().add(false);
                row.getValues().add(false);
                row.getValues().add(false);
                moUserCompanyBranchesPane.addTableRow(row);
                for (int j = 0; j < mvUserBranches.size(); j++) {
                    if (((int[]) mvUserBranches.get(j).get(0).getPrimaryKey())[0] == moFieldFkCompanyId.getKeyAsIntArray()[0]) {
                        mvUserBranches.get(j).add(row);
                    }
                }
            }
            
            populateTableUserBranches();
            jltBranchesAvailable.setListData(mvEmptyListItems);
            moUserCompanyBranchesPane.renderTableRows();
        }
    }

    private void actionReturnBranch() {
        SFormComponentItem item = null;

        if (moUserCompanyBranchesPane.getSelectedTableRow() != null) {
            if (validateReturnBranch(((int[]) moUserCompanyBranchesPane.getSelectedTableRow().getPrimaryKey())[0])) {
                if (miClient.showMsgBoxConfirm("Ya existe una configuración para la sucursal " + "'" + (String) moUserCompanyBranchesPane.getSelectedTableRow().getValues().get(0) + "'" +
                        ". \nEste movimiento borrará dicha configuración. ¿Desea continuar?") == JOptionPane.YES_OPTION) {
                    removeBranchesDependencies(((int[]) moUserCompanyBranchesPane.getSelectedTableRow().getPrimaryKey())[0]);
                    for (int i = 0; i < mvUserBranches.size(); i++) {
                        if (((int[]) mvUserBranches.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyId.getKeyAsIntArray()[0]) {
                            item = new SFormComponentItem(new int[] { ((int[]) moUserCompanyBranchesPane.getSelectedTableRow().getPrimaryKey())[0] }, (String) moUserCompanyBranchesPane.getSelectedTableRow().getValues().get(0));
                            mvUserBranches.get(i).remove(moUserCompanyBranchesPane.getTable().getSelectedRow() + 1);
                            moUserCompanyBranchesPane.removeTableRow(moUserCompanyBranchesPane.getTable().getSelectedRow());
                            SFormUtilities.addListItem(jltBranchesAvailable, item);
                        }
                    }

                    moUserCompanyBranchesPane.renderTableRows();
                }
            }
            else {
                removeBranchesDependencies(((int[]) moUserCompanyBranchesPane.getSelectedTableRow().getPrimaryKey())[0]);
                for (int i = 0; i < mvUserBranches.size(); i++) {
                    if (((int[]) mvUserBranches.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyId.getKeyAsIntArray()[0]) {
                        item = new SFormComponentItem(moUserCompanyBranchesPane.getSelectedTableRow().getPrimaryKey(), (String)moUserCompanyBranchesPane.getSelectedTableRow().getValues().get(0));
                        mvUserBranches.get(i).remove(moUserCompanyBranchesPane.getTable().getSelectedRow() + 1);
                        moUserCompanyBranchesPane.removeTableRow(moUserCompanyBranchesPane.getTable().getSelectedRow());
                        SFormUtilities.addListItem(jltBranchesAvailable, item);
                    }
                }

                moUserCompanyBranchesPane.renderTableRows();
            }
            
            populateTableUserBranches();
        }
    }

    private void actionReturnAllBranch() {
        Vector<SFormComponentItem> vListBranches = new Vector<>();
        STableRow row = null;
        int x;

        vListBranches.clear();
        for (x = 0; x < moUserCompanyBranchesPane.getTableModel().getRowCount(); x++) {
            row = moUserCompanyBranchesPane.getTableModel().getTableRow(x);
            if (validateReturnBranch(((int[]) row.getPrimaryKey())[0])) {
                if (miClient.showMsgBoxConfirm("Ya existe una configuración para la sucursal \n" + "'" +(String) row.getValues().get(0) + "'" +
                        ". \nEste movimiento borrará dicha configuración. ¿Desea continuar?") == JOptionPane.YES_OPTION) {
                    removeBranchesDependencies(((int[])row.getPrimaryKey())[0]);
                    vListBranches.add(new SFormComponentItem(row.getPrimaryKey(), (String) row.getValues().get(0)));
                }
            }
            else {
                vListBranches.add(new SFormComponentItem(row.getPrimaryKey(), (String) row.getValues().get(0)));
            }
        }

        for (x = 0; x < vListBranches.size(); x++) {
            SFormUtilities.addListItem(jltBranchesAvailable, vListBranches.get(x));

            for (int j = 0; j < moUserCompanyBranchesPane.getTableModel().getRowCount(); j++) {
                if (((int []) moUserCompanyBranchesPane.getTableRow(j).getPrimaryKey())[0] == ((int []) vListBranches.get(x).getPrimaryKey())[0]) {
                    moUserCompanyBranchesPane.removeTableRow(j);
                    moUserCompanyBranchesPane.renderTableRows();
                }
            }

            for (int i = 0; i < mvUserBranches.size(); i++) {
                if (((int[]) mvUserBranches.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyId.getKeyAsIntArray()[0]) {
                    for (int j = 0; j < mvUserBranches.get(i).size(); j++) {
                        if (((int[]) mvUserBranches.get(i).get(j).getPrimaryKey())[0] == ((int []) vListBranches.get(x).getPrimaryKey())[0]) {
                            mvUserBranches.get(i).remove(j);
                        }
                    }
                }
            }
        }
        
        populateListCompanyBranches();
        jltCompanyBranchWarehouses.setListData(mvEmptyListItems);
        jltCompanyBranchPos.setListData(mvEmptyListItems);
        jltCompanyBranchPlants.setListData(mvEmptyListItems);
    }

    private void actionTransferCashAccount() {
        SFormComponentItem item = null;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            if (!jckIsUniversalCashAccount.isSelected() && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                if (jltCompanyBranchCashAccount.getSelectedIndex() != -1) {
                    item = SFormUtilities.removeListSelectedItem(jltCompanyBranchCashAccount);
                    for (int i = 0; i < mvUserCashAccounts.size(); i++) {
                        if (((int[]) mvUserCashAccounts.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                            mvUserCashAccounts.get(i).add(item);
                            populateListUserCashAccounts();
                        }
                    }
                    
                    if (jltCompanyBranchCashAccount.getModel().getSize() >=0) {
                        jltCompanyBranchCashAccount.setSelectedIndex(0);
                    }
                }
            }
        }
    }

    private void actionTransferAllCashAccounts() {
        SFormComponentItem item = null;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            if (!jckIsUniversalCashAccount.isSelected() && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                if (jltCompanyBranchCashAccount.getModel().getSize() > 0) {
                    for (int i = 0; i < jltCompanyBranchCashAccount.getModel().getSize(); i++) {
                        item = (SFormComponentItem)jltCompanyBranchCashAccount.getModel().getElementAt(i);
                        for (int j = 0; j < mvUserCashAccounts.size(); j++) {
                            if (((int[]) mvUserCashAccounts.get(j).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                                mvUserCashAccounts.get(j).add(item);
                            }
                        }
                    }
                    
                    populateListUserCashAccounts();
                    jltCompanyBranchCashAccount.setListData(mvEmptyListItems);
                }
            }
        }
    }

    private void actionReturnCashAccount() {
        int index = 0;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            if (jltUserCashAccount.getSelectedIndex() != -1 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                for (int i = 0; i < mvUserCashAccounts.size(); i++) {
                    if (((int[]) mvUserCashAccounts.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                        index = mvUserCashAccounts.get(i).get(jltUserCashAccount.getSelectedIndex() + 1).getItem().indexOf("*");
                        if (index != -1) {
                            mvUserCashAccounts.get(i).get(jltUserCashAccount.getSelectedIndex() + 1).setItem(takeOffIsDefault(mvUserCashAccounts.get(i).get(jltUserCashAccount.getSelectedIndex() + 1).getItem(), index - 1));
                        }
                        SFormUtilities.addListItem(jltCompanyBranchCashAccount, mvUserCashAccounts.get(i).remove(jltUserCashAccount.getSelectedIndex() + 1));
                        populateListUserCashAccounts();
                    }
                }
                
                if (jltUserCashAccount.getModel().getSize() >= 0) {
                    jltUserCashAccount.setSelectedIndex(0);
                }
            }
        }
    }

    private void actionReturnAllCashAccounts() {
        Vector<SFormComponentItem> vListCashAccounts = new Vector<SFormComponentItem>();
        int index = 0;
        int x = 0;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            vListCashAccounts.clear();
            for (x = 0; x < jltUserCashAccount.getModel().getSize(); x++) {
                jltUserCashAccount.setSelectedIndex(x);
                if (jltUserCashAccount.getSelectedIndex() != -1 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                    for (int i = 0; i < mvUserCashAccounts.size(); i++) {
                        if (((int[]) mvUserCashAccounts.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                            index = mvUserCashAccounts.get(i).get(jltUserCashAccount.getSelectedIndex() + 1).getItem().indexOf("*");
                            if (index != -1) {
                                mvUserCashAccounts.get(i).get(jltUserCashAccount.getSelectedIndex() + 1).setItem(takeOffIsDefault(mvUserCashAccounts.get(i).get(jltUserCashAccount.getSelectedIndex() + 1).getItem(), index -1));
                            }
                            vListCashAccounts.add(mvUserCashAccounts.get(i).get(jltUserCashAccount.getSelectedIndex() + 1));
                        }
                    }
                }
            }

            for (x = 0; x < vListCashAccounts.size(); x++) {
                SFormUtilities.addListItem(jltCompanyBranchCashAccount, vListCashAccounts.get(x));
            }

            for (int i = 0; i < mvUserCashAccounts.size(); i++) {
                if (((int[]) mvUserCashAccounts.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                    for (x = mvUserCashAccounts.get(i).size()-1; x > 0; x--) {
                        mvUserCashAccounts.get(i).remove(x);
                    }
                }
            }
            populateListUserCashAccounts();
        }
    }

    private void actionTransferWarehouse() {
        SFormComponentItem item = null;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            if (!jckIsUniversalWarehouse.isSelected() && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                if (jltCompanyBranchWarehouses.getSelectedIndex() != -1) {
                    item = SFormUtilities.removeListSelectedItem(jltCompanyBranchWarehouses);
                    for (int i = 0; i < mvUserWarehouses.size(); i++) {
                        if (((int[]) mvUserWarehouses.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                            mvUserWarehouses.get(i).add(item);
                            populateListUserWarehouses();
                        }
                    }
                    if (jltCompanyBranchWarehouses.getModel().getSize() >=0) {
                        jltCompanyBranchWarehouses.setSelectedIndex(0);
                    }
                }
            }
        }
    }

    private void actionTransferAllWarehouse() {
        SFormComponentItem item = null;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            if (!jckIsUniversalWarehouse.isSelected() && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                if (jltCompanyBranchWarehouses.getModel().getSize() > 0) {
                    for (int i = 0; i < jltCompanyBranchWarehouses.getModel().getSize(); i++) {
                        item = (SFormComponentItem)jltCompanyBranchWarehouses.getModel().getElementAt(i);
                        for (int j = 0; j < mvUserWarehouses.size(); j++) {
                            if (((int[]) mvUserWarehouses.get(j).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                                mvUserWarehouses.get(j).add(item);
                            }
                        }
                    }
                    populateListUserWarehouses();
                    jltCompanyBranchWarehouses.setListData(mvEmptyListItems);
                }
            }
        }
    }

    private void actionReturnWarehouse() {
        int index = 0;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            if (jltUserWarehouses.getSelectedIndex() != -1 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                for (int i = 0; i < mvUserWarehouses.size(); i++) {
                    if (((int[]) mvUserWarehouses.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                        index = mvUserWarehouses.get(i).get(jltUserWarehouses.getSelectedIndex() + 1).getItem().indexOf("*");
                        if (index != -1) {
                            mvUserWarehouses.get(i).get(jltUserWarehouses.getSelectedIndex() + 1).setItem(takeOffIsDefault(mvUserWarehouses.get(i).get(jltUserWarehouses.getSelectedIndex() + 1).getItem(), index - 1));
                        }
                        SFormUtilities.addListItem(jltCompanyBranchWarehouses, mvUserWarehouses.get(i).remove(jltUserWarehouses.getSelectedIndex() + 1));
                        populateListUserWarehouses();
                    }
                }
                if (jltUserWarehouses.getModel().getSize() >= 0) {
                    jltUserWarehouses.setSelectedIndex(0);
                }
            }
        }
    }

    private void actionReturnAllWarehouses() {
        Vector<SFormComponentItem> vListWarehouses = new Vector<>();
        int index = 0;
        int x = 0;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            vListWarehouses.clear();
            for (x = 0; x < jltUserWarehouses.getModel().getSize(); x++) {
                jltUserWarehouses.setSelectedIndex(x);
                if (jltUserWarehouses.getSelectedIndex() != -1 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                    for (int i = 0; i < mvUserWarehouses.size(); i++) {
                        if (((int[]) mvUserWarehouses.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                            index = mvUserWarehouses.get(i).get(jltUserWarehouses.getSelectedIndex() + 1).getItem().indexOf("*");
                            if (index != -1) {
                                mvUserWarehouses.get(i).get(jltUserWarehouses.getSelectedIndex() + 1).setItem(takeOffIsDefault(mvUserWarehouses.get(i).get(jltUserWarehouses.getSelectedIndex() + 1).getItem(), index - 1));
                            }
                            vListWarehouses.add(mvUserWarehouses.get(i).get(jltUserWarehouses.getSelectedIndex() + 1));
                        }
                    }
                }
            }

            for (x = 0; x < vListWarehouses.size(); x++) {
                SFormUtilities.addListItem(jltCompanyBranchWarehouses, vListWarehouses.get(x));
            }

            for (int i = 0; i < mvUserWarehouses.size(); i++) {
                if (((int[])mvUserWarehouses.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                    for (x = mvUserWarehouses.get(i).size()-1; x > 0; x--) {
                        mvUserWarehouses.get(i).remove(x);
                    }
                }
            }
            populateListUserWarehouses();
        }
    }

    private void actionTransferPos() {
        SFormComponentItem item = null;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            if (!jckIsUniversalPos.isSelected() && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                if (jltCompanyBranchPos.getSelectedIndex() != -1) {
                    item = SFormUtilities.removeListSelectedItem(jltCompanyBranchPos);
                    for (int i = 0; i < mvUserPos.size(); i++) {
                        if (((int[]) mvUserPos.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                            mvUserPos.get(i).add(item);
                            populateListUserPos();
                        }
                    }
                    if (jltCompanyBranchPos.getModel().getSize() >= 0) {
                        jltCompanyBranchPos.setSelectedIndex(0);
                    }
                }
            }
        }
    }

    private void actionTransferAllPos() {
        SFormComponentItem item = null;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            if (!jckIsUniversalPos.isSelected() && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                if (jltCompanyBranchPos.getModel().getSize() > 0) {
                    for (int i = 0; i < jltCompanyBranchPos.getModel().getSize(); i++) {
                        item = (SFormComponentItem)jltCompanyBranchPos.getModel().getElementAt(i);
                        for (int j = 0; j < mvUserPos.size(); j++) {
                            if (((int[]) mvUserPos.get(j).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                                mvUserPos.get(j).add(item);
                            }
                        }
                    }
                    populateListUserPos();
                    jltCompanyBranchPos.setListData(mvEmptyListItems);
                }
            }
        }
    }

    private void actionReturnPos() {
        int index = 0;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            if (jltUserPos.getSelectedIndex() != -1 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                for (int i = 0; i < mvUserPos.size(); i++) {
                    if (((int[]) mvUserPos.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                        index = mvUserPos.get(i).get(jltUserPos.getSelectedIndex() + 1).getItem().indexOf("*");
                        if (index != -1) {
                            mvUserPos.get(i).get(jltUserPos.getSelectedIndex() + 1).setItem(takeOffIsDefault(mvUserPos.get(i).get(jltUserPos.getSelectedIndex() + 1).getItem(), index - 1));
                        }
                        SFormUtilities.addListItem(jltCompanyBranchPos, mvUserPos.get(i).remove(jltUserPos.getSelectedIndex() + 1));
                        populateListUserPos();
                    }
                }
                if (jltUserPos.getModel().getSize() >= 0) {
                    jltUserPos.setSelectedIndex(0);
                }
            }
        }
    }

    private void actionReturnAllPos() {
        Vector<SFormComponentItem> vListPos = new Vector<>();
        int index = 0;
        int x = 0;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            vListPos.clear();
            for (x = 0; x < jltUserPos.getModel().getSize(); x++) {
                jltUserPos.setSelectedIndex(x);
                if (jltUserPos.getSelectedIndex() != -1 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                    for (int i = 0; i < mvUserPos.size(); i++) {
                        if (((int[]) mvUserPos.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                            index = mvUserPos.get(i).get(jltUserPos.getSelectedIndex() + 1).getItem().indexOf("*");
                            if (index != -1) {
                                mvUserPos.get(i).get(jltUserPos.getSelectedIndex() + 1).setItem(takeOffIsDefault(mvUserPos.get(i).get(jltUserPos.getSelectedIndex() + 1).getItem(), index - 1));
                            }
                            vListPos.add(mvUserPos.get(i).get(jltUserPos.getSelectedIndex() + 1));
                        }
                    }
                }
            }

            for (x = 0; x < vListPos.size(); x++) {
                SFormUtilities.addListItem(jltCompanyBranchPos, vListPos.get(x));
            }

            for (int i = 0; i < mvUserPos.size(); i++) {
                if (((int[]) mvUserPos.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                    for (x = mvUserPos.get(i).size()-1; x > 0; x--) {
                        mvUserPos.get(i).remove(x);
                    }
                }
            }
            populateListUserPos();
        }
    }

    private void actionTransferPlant() {
        SFormComponentItem item = null;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            if (!jckIsUniversalPlant.isSelected() && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                if (jltCompanyBranchPlants.getSelectedIndex() != -1) {
                    item = SFormUtilities.removeListSelectedItem(jltCompanyBranchPlants);
                    for (int i = 0; i < mvUserPlants.size(); i++) {
                        if (((int[]) mvUserPlants.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                            mvUserPlants.get(i).add(item);
                            populateListUserPlants();
                        }
                    }
                    if (jltCompanyBranchPlants.getModel().getSize() >=0) {
                        jltCompanyBranchPlants.setSelectedIndex(0);
                    }
                }
            }
        }
    }

    private void actionTransferAllPlant() {
        SFormComponentItem item = null;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            if (!jckIsUniversalPlant.isSelected() && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                if (jltCompanyBranchPlants.getModel().getSize() > 0) {
                    for (int i = 0; i < jltCompanyBranchPlants.getModel().getSize(); i++) {
                        item = (SFormComponentItem)jltCompanyBranchPlants.getModel().getElementAt(i);
                        for (int j = 0; j < mvUserPlants.size(); j++) {
                            if (((int[]) mvUserPlants.get(j).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                                mvUserPlants.get(j).add(item);
                            }
                        }
                    }
                    populateListUserPlants();
                    jltCompanyBranchPlants.setListData(mvEmptyListItems);
                }
            }
        }
    }

    private void actionReturnPlant() {
        int index = 0;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            if (jltUserPlants.getSelectedIndex() != -1 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                for (int i = 0; i < mvUserPlants.size(); i++) {
                    if (((int[]) mvUserPlants.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                        index = mvUserPlants.get(i).get(jltUserPlants.getSelectedIndex() + 1).getItem().indexOf("*");
                        if (index != -1) {
                            mvUserPlants.get(i).get(jltUserPlants.getSelectedIndex() + 1).setItem(takeOffIsDefault(mvUserPlants.get(i).get(jltUserPlants.getSelectedIndex() + 1).getItem(), index - 1));
                        }
                        SFormUtilities.addListItem(jltCompanyBranchPlants, mvUserPlants.get(i).remove(jltUserPlants.getSelectedIndex() + 1));
                        populateListUserPlants();
                    }
                }
                if (jltUserPlants.getModel().getSize() >= 0) {
                    jltUserPlants.setSelectedIndex(0);
                }
            }
        }
    }

    private void actionReturnAllPlants() {
        Vector<SFormComponentItem> vListPlants = new Vector<>();
        int index = 0;
        int x = 0;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0) {
            vListPlants.clear();
            for (x = 0; x < jltUserPlants.getModel().getSize(); x++) {
                jltUserPlants.setSelectedIndex(x);
                if (jltUserPlants.getSelectedIndex() != -1 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                    for (int i = 0; i < mvUserPlants.size(); i++) {
                        if (((int[]) mvUserPlants.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                            index = mvUserPlants.get(i).get(jltUserPlants.getSelectedIndex() + 1).getItem().indexOf("*");
                            if (index != -1) {
                                mvUserPlants.get(i).get(jltUserPlants.getSelectedIndex() + 1).setItem(takeOffIsDefault(mvUserPlants.get(i).get(jltUserPlants.getSelectedIndex() + 1).getItem(), index - 1));
                            }
                            vListPlants.add(mvUserPlants.get(i).get(jltUserPlants.getSelectedIndex() + 1));
                        }
                    }
                }
            }

            for (x = 0; x < vListPlants.size(); x++) {
                SFormUtilities.addListItem(jltCompanyBranchPlants, vListPlants.get(x));
            }

            for (int i = 0; i < mvUserPlants.size(); i++) {
                if (((int[]) mvUserPlants.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                    for (x = mvUserPlants.get(i).size()-1; x > 0; x--) {
                        mvUserPlants.get(i).remove(x);
                    }
                }
            }
            populateListUserPlants();
        }
    }

    private void renderButtonsBranch() {
        if (jckIsUniversal.isSelected()) {
            jbTransferBranch.setEnabled(false);
            jbTransferAllBranch.setEnabled(false);
            jbReturnBranch.setEnabled(false);
            jbReturnAllBranch.setEnabled(false);
        }
        else {
            jbTransferBranch.setEnabled(true);
            jbTransferAllBranch.setEnabled(true);
            jbReturnBranch.setEnabled(true);
            jbReturnAllBranch.setEnabled(true);
        }
    }

    private void renderButtonsCashAccount() {
        if (jckIsUniversal.isSelected()) {
            jbTransferCashAccount.setEnabled(false);
            jbTransferAllCashAccounts.setEnabled(false);
            jbReturnCashAccount.setEnabled(false);
            jbReturnAllCashAccounts.setEnabled(false);
            jbSetDefaultCashAccount.setEnabled(false);
            jckIsUniversalCashAccount.setEnabled(false);
        }
        else {
            jbTransferCashAccount.setEnabled(true);
            jbTransferAllCashAccounts.setEnabled(true);
            jbReturnCashAccount.setEnabled(true);
            jbReturnAllCashAccounts.setEnabled(true);
            jbSetDefaultCashAccount.setEnabled(true);
            jckIsUniversalCashAccount.setEnabled(true);
        }
    }

    private void renderButtonsCashAccountIsUniversal() {
        if (jckIsUniversalCashAccount.isSelected()) {
            jbTransferCashAccount.setEnabled(false);
            jbTransferAllCashAccounts.setEnabled(false);
            jbReturnCashAccount.setEnabled(false);
            jbReturnAllCashAccounts.setEnabled(false);
            jbSetDefaultCashAccount.setEnabled(false);
        }
        else {
            jbTransferCashAccount.setEnabled(true);
            jbTransferAllCashAccounts.setEnabled(true);
            jbReturnCashAccount.setEnabled(true);
            jbReturnAllCashAccounts.setEnabled(true);
            jbSetDefaultCashAccount.setEnabled(true);
        }
    }

    private void renderButtonsWarehouse() {
        if (jckIsUniversal.isSelected()) {
            jbTransferWarehouse.setEnabled(false);
            jbTransferAllWarehouses.setEnabled(false);
            jbReturnWarehouse.setEnabled(false);
            jbReturnAllWarehouses.setEnabled(false);
            jbSetDefaultWarehouse.setEnabled(false);
            jckIsUniversalWarehouse.setEnabled(false);
        }
        else {
            jbTransferWarehouse.setEnabled(true);
            jbTransferAllWarehouses.setEnabled(true);
            jbReturnWarehouse.setEnabled(true);
            jbReturnAllWarehouses.setEnabled(true);
            jbSetDefaultWarehouse.setEnabled(true);
            jckIsUniversalWarehouse.setEnabled(true);
        }
    }

    private void renderButtonsWarehouseIsUniversal() {
        if (jckIsUniversalWarehouse.isSelected()) {
            jbTransferWarehouse.setEnabled(false);
            jbTransferAllWarehouses.setEnabled(false);
            jbReturnWarehouse.setEnabled(false);
            jbReturnAllWarehouses.setEnabled(false);
            jbSetDefaultWarehouse.setEnabled(false);
        }
        else {
            jbTransferWarehouse.setEnabled(true);
            jbTransferAllWarehouses.setEnabled(true);
            jbReturnWarehouse.setEnabled(true);
            jbReturnAllWarehouses.setEnabled(true);
            jbSetDefaultWarehouse.setEnabled(true);
        }
    }

    private void renderButtonsPos() {
        if (jckIsUniversal.isSelected()) {
            jbTransferPos.setEnabled(false);
            jbTransferAllPos.setEnabled(false);
            jbReturnPos.setEnabled(false);
            jbReturnAllPos.setEnabled(false);
            jbSetDefaultPos.setEnabled(false);
            jckIsUniversalPos.setEnabled(false);
        }
        else {
            jbTransferPos.setEnabled(true);
            jbTransferAllPos.setEnabled(true);
            jbReturnPos.setEnabled(true);
            jbReturnAllPos.setEnabled(true);
            jbSetDefaultPos.setEnabled(true);
            jckIsUniversalPos.setEnabled(true);
        }
    }

    private void renderButtonsPosIsUniversal() {
        if (jckIsUniversalPos.isSelected()) {
            jbTransferPos.setEnabled(false);
            jbTransferAllPos.setEnabled(false);
            jbReturnPos.setEnabled(false);
            jbReturnAllPos.setEnabled(false);
            jbSetDefaultPos.setEnabled(false);
        }
        else {
            jbTransferPos.setEnabled(true);
            jbTransferAllPos.setEnabled(true);
            jbReturnPos.setEnabled(true);
            jbReturnAllPos.setEnabled(true);
            jbSetDefaultPos.setEnabled(true);
        }
    }

    private void renderButtonsPlants() {
        if (jckIsUniversal.isSelected()) {
            jbTransferPlant.setEnabled(false);
            jbTransferAllPlants.setEnabled(false);
            jbReturnPlant.setEnabled(false);
            jbReturnAllPlants.setEnabled(false);
            jbSetDefaultPlant.setEnabled(false);
            jckIsUniversalPlant.setEnabled(false);
        }
        else {
            jbTransferPlant.setEnabled(true);
            jbTransferAllPlants.setEnabled(true);
            jbReturnPlant.setEnabled(true);
            jbReturnAllPlants.setEnabled(true);
            jbSetDefaultPlant.setEnabled(true);
            jckIsUniversalPlant.setEnabled(true);
        }
    }

    private void renderButtonsPlantsIsUniversal() {
        if (jckIsUniversalPlant.isSelected()) {
            jbTransferPlant.setEnabled(false);
            jbTransferAllPlants.setEnabled(false);
            jbReturnPlant.setEnabled(false);
            jbReturnAllPlants.setEnabled(false);
            jbSetDefaultPlant.setEnabled(false);
        }
        else {
            jbTransferPlant.setEnabled(true);
            jbTransferAllPlants.setEnabled(true);
            jbReturnPlant.setEnabled(true);
            jbReturnAllPlants.setEnabled(true);
            jbSetDefaultPlant.setEnabled(true);
        }
    }

    private void renderButtons() {
        if (jckIsUniversal.isSelected()) {
            if (moUserCompaniesPane.getTableModel().getRowCount() > 0) {
                if (miClient.showMsgBoxConfirm("Ya existe una configuración para el usuario de acceso al sistema. " +
                        "\nEste movimiento borrará dicha configuración. ¿Desea continuar?") == JOptionPane.YES_OPTION) {
                    jckIsUniversal.setSelected(true);
                    jbTransferCompany.setEnabled(false);
                    jbTransferAllCompany.setEnabled(false);
                    jbReturnCompany.setEnabled(false);
                    jbReturnAllCompany.setEnabled(false);
                    renderButtonsBranch();
                    renderButtonsCashAccount();
                    renderButtonsWarehouse();
                    renderButtonsPos();
                    renderButtonsPlants();
                    removeCompanies();
                }
                else {
                    jckIsUniversal.setSelected(false);
                    jbTransferCompany.setEnabled(true);
                    jbTransferAllCompany.setEnabled(true);
                    jbReturnCompany.setEnabled(true);
                    jbReturnAllCompany.setEnabled(true);
                    renderButtonsBranch();
                    renderButtonsCashAccount();
                    renderButtonsWarehouse();
                    renderButtonsPos();
                    renderButtonsPlants();
                }
            }
            else {
                jbTransferCompany.setEnabled(false);
                jbTransferAllCompany.setEnabled(false);
                jbReturnCompany.setEnabled(false);
                jbReturnAllCompany.setEnabled(false);
                renderButtonsBranch();
                renderButtonsCashAccount();
                renderButtonsWarehouse();
                renderButtonsPos();
                renderButtonsPlants();
                jltCompaniesAvailable.setListData(mvEmptyListItems);
            }
        }
        else {
            jbTransferCompany.setEnabled(true);
            jbTransferAllCompany.setEnabled(true);
            jbReturnCompany.setEnabled(true);
            jbReturnAllCompany.setEnabled(true);
            renderButtonsBranch();
            renderButtonsCashAccount();
            renderButtonsWarehouse();
            renderButtonsPos();
            renderButtonsPlants();
            populateListCompany();
        }
    }

    private void populateListCompany() {
        Vector<SFormComponentItem> vListCompanies = new Vector<SFormComponentItem>();
        boolean exists = false;

        if (!jckIsUniversal.isSelected()) {
            vListCompanies.clear();
            for (int i = 0; i < mvDbmsCompanies.size(); i++) {
                if (moUserCompaniesPane.getTableModel().getRowCount() == 0) {
                    vListCompanies.add(new SFormComponentItem(new int[] { mvDbmsCompanies.get(i).getPkCompanyId() }, mvDbmsCompanies.get(i).getCompany()));
                }
                else {
                    exists = false;
                    for (int j = 0; j < moUserCompaniesPane.getTableModel().getRowCount(); j++) {
                       if (mvDbmsCompanies.get(i).getPkCompanyId() == ((int[]) moUserCompaniesPane.getTableRow(j).getPrimaryKey())[0]) {
                            exists = true;
                            break;
                       }
                    }
                    if (!exists) {
                           vListCompanies.add(new SFormComponentItem(new int[] { mvDbmsCompanies.get(i).getPkCompanyId() }, mvDbmsCompanies.get(i).getCompany()));
                       }
                }
            }

            jltCompaniesAvailable.setListData(vListCompanies);
        }
    }

    private void populateComboBoxCompany() {
        SFormComponentItem item = null;

        jcbCompanyBranch.removeAllItems();
        jcbCompanyEntity.removeAllItems();

        if (!jckIsUniversal.isSelected()) {
            if (moUserCompaniesPane.getTableModel().getRowCount() > 0) {
                jcbCompanyBranch.addItem(new SFormComponentItem(new int[] { 0 }, "(Seleccionar una empresa)"));
                jcbCompanyEntity.addItem(new SFormComponentItem(new int[] { 0 }, "(Seleccionar una empresa)"));
                for (int i = 0; i < moUserCompaniesPane.getTableModel().getRowCount(); i++ ) {
                    item = new SFormComponentItem((int[]) moUserCompaniesPane.getTableRow(i).getPrimaryKey(), (String) moUserCompaniesPane.getTableRow(i).getValues().get(0));
                    jcbCompanyBranch.addItem(item);
                    jcbCompanyEntity.addItem(item);
                }
            }
        }
    }

    private void populateTableUserBranches() {
        moUserCompanyBranchesPane.clearTableRows();

        if (!jckIsUniversal.isSelected()) {
            if (moUserCompaniesPane.getTableModel().getRowCount() > 0) {
                if (moFieldFkCompanyId.getKeyAsIntArray()[0] > 0) {
                    for (int i = 0; i < mvUserBranches.size(); i++) {
                        if (((int[]) mvUserBranches.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyId.getKeyAsIntArray()[0]) {
                            if (mvUserBranches.get(i).size() > 1) {
                                for (int j = 1; j < mvUserBranches.get(i).size(); j++) {
                                    moUserCompanyBranchesPane.addTableRow(mvUserBranches.get(i).get(j));
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void populateListCompanyBranches() {
        Vector<SFormComponentItem> vListBranches = new Vector<>();
        boolean exists = false;

        if (!jckIsUniversal.isSelected()) {
            if (moUserCompaniesPane.getTableModel().getRowCount() > 0) {
                vListBranches.clear();
                if (moFieldFkCompanyId.getKeyAsIntArray()[0] > 0) {
                    for (int i = 0; i < mvDbmsCompanyBranches.size(); i++) {
                        if (moFieldFkCompanyId.getKeyAsIntArray()[0] == mvDbmsCompanyBranches.get(i).getFkBizPartnerId()) {
                            if (moUserCompanyBranchesPane.getTableModel().getRowCount() == 0) {
                                vListBranches.add(new SFormComponentItem(new int[] { mvDbmsCompanyBranches.get(i).getPkBizPartnerBranchId(), mvDbmsCompanyBranches.get(i).getFkBizPartnerId() }, mvDbmsCompanyBranches.get(i).getBizPartnerBranch()));
                            }
                            else {
                                exists = false;
                                for (int j = 0; j < moUserCompanyBranchesPane.getTableModel().getRowCount(); j++) {
                                   if (mvDbmsCompanyBranches.get(i).getPkBizPartnerBranchId() == ((int[]) moUserCompanyBranchesPane.getTableRow(j).getPrimaryKey())[0]) {
                                        exists = true;
                                        break;
                                   }
                                }
                                if (!exists) {
                                       vListBranches.add(new SFormComponentItem(new int[] { mvDbmsCompanyBranches.get(i).getPkBizPartnerBranchId(), mvDbmsCompanyBranches.get(i).getFkBizPartnerId() }, mvDbmsCompanyBranches.get(i).getBizPartnerBranch()));
                                   }
                            }
                        }
                    }
                }

                jltBranchesAvailable.setListData(vListBranches);
            }
        }
    }

    private void populateComboBoxCompanyBranchEntity() {
        jcbCompanyBranchEntity.removeAllItems();

        if (!jckIsUniversal.isSelected()) {
            jcbCompanyBranchEntity.addItem(new SFormComponentItem(new int[] {0}, "(Seleccionar una sucursal)"));
            for (int i = 0; i < mvUserBranches.size(); i++) {
                for (int j = 1; j < mvUserBranches.get(i).size(); j++) {
                    if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0]  == ((int []) mvUserBranches.get(i).get(0).getPrimaryKey())[0]) {
                        jcbCompanyBranchEntity.addItem(new SFormComponentItem((int[]) mvUserBranches.get(i).get(j).getPrimaryKey(),(String) mvUserBranches.get(i).get(j).getValues().get(0).toString()));
                    }
                }
            }
         }
    }

    private void populateListUserCashAccounts() {
        jltUserCashAccount.setListData(mvEmptyListItems);
        jckIsUniversalCashAccount.setEnabled(false);
        jckIsUniversalCashAccount.setSelected(false);

        if (!jckIsUniversal.isSelected()) {
            if (moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                jckIsUniversalCashAccount.setEnabled(true);
                for (int i = 0; i < mvUserCashAccounts.size(); i++) {
                    if (((int[]) ((SFormComponentItem) mvUserCashAccounts.get(i).get(0)).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                        for (int j = 1; j < mvUserCashAccounts.get(i).size(); j++) {
                            SFormUtilities.addListItem(jltUserCashAccount, mvUserCashAccounts.get(i).get(j));
                        }
                        break;
                    }
                }
            }
        }
    }

    private void populateListCompanyBranchCashAccounts() {
        Vector<SFormComponentItem> vListItems = new Vector<>();
        boolean exists = false;

         if (!jckIsUniversal.isSelected()) {
             vListItems.clear();
             if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                 for (int i = 0; i < mvDbmsCashAccounts.size(); i++) {
                     if (mvDbmsCashAccounts.get(i).getPkCompanyBranchId() == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                         if (jltUserCashAccount.getModel().getSize() == 0) {
                             vListItems.add(new SFormComponentItem(new int[] { mvDbmsCashAccounts.get(i).getPkCompanyBranchId(), mvDbmsCashAccounts.get(i).getPkEntityId() }, mvDbmsCashAccounts.get(i).getEntity()));
                         }
                         else {
                             exists = false;
                             for (int j = 0; j < jltUserCashAccount.getModel().getSize(); j++) {
                                  if (mvDbmsCashAccounts.get(i).getPkEntityId() == ((int[]) ((SFormComponentItem) jltUserCashAccount.getModel().getElementAt(j)).getPrimaryKey())[1]) {
                                      exists = true;
                                      break;
                                  }
                             }
                             if (!exists) {
                                 vListItems.add(new SFormComponentItem(new int[] { mvDbmsCashAccounts.get(i).getPkCompanyBranchId(), mvDbmsCashAccounts.get(i).getPkEntityId() }, mvDbmsCashAccounts.get(i).getEntity()));
                             }
                         }
                     }
                 }

                 jltCompanyBranchCashAccount.setListData(vListItems);
             }
             else {
                 jltCompanyBranchCashAccount.setListData(mvEmptyListItems);
             }
        }
    }

    private void populateListUserWarehouses() {
        jltUserWarehouses.setListData(mvEmptyListItems);
        jckIsUniversalWarehouse.setEnabled(false);
        jckIsUniversalWarehouse.setSelected(false);

        if (!jckIsUniversal.isSelected()) {
            if (moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                jckIsUniversalWarehouse.setEnabled(true);
                for (int i = 0; i < mvUserWarehouses.size(); i++) {
                    if (((int[]) ((SFormComponentItem) mvUserWarehouses.get(i).get(0)).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                        for (int j = 1; j < mvUserWarehouses.get(i).size(); j++) {
                            SFormUtilities.addListItem(jltUserWarehouses, mvUserWarehouses.get(i).get(j));
                        }
                        break;
                    }
                }
            }
        }
    }

    private void populateListCompanyBranchWarehouses() {
        Vector<SFormComponentItem> vListItems = new Vector<>();
        boolean exists = false;

        if (!jckIsUniversal.isSelected()) {
            vListItems.clear();
            if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                for (int i = 0; i < mvDbmsWarehouses.size(); i++) {
                     if (mvDbmsWarehouses.get(i).getPkCompanyBranchId() == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                         if (jltUserWarehouses.getModel().getSize() == 0) {
                             vListItems.add(new SFormComponentItem(new int[] { mvDbmsWarehouses.get(i).getPkCompanyBranchId(), mvDbmsWarehouses.get(i).getPkEntityId() }, mvDbmsWarehouses.get(i).getEntity()));
                         }
                         else {
                             exists = false;
                             for (int j = 0; j < jltUserWarehouses.getModel().getSize(); j++) {
                                  if (mvDbmsWarehouses.get(i).getPkEntityId() == ((int[]) ((SFormComponentItem) jltUserWarehouses.getModel().getElementAt(j)).getPrimaryKey())[1]) {
                                      exists = true;
                                      break;
                                  }
                             }
                             if (!exists) {
                                 vListItems.add(new SFormComponentItem(new int[] { mvDbmsWarehouses.get(i).getPkCompanyBranchId(), mvDbmsWarehouses.get(i).getPkEntityId() }, mvDbmsWarehouses.get(i).getEntity()));
                             }
                         }
                     }
                }

                jltCompanyBranchWarehouses.setListData(vListItems);
            }
            else {
                jltCompanyBranchWarehouses.setListData(mvEmptyListItems);
            }
        }
    }

    private void populateListUserPos() {
        jltUserPos.setListData(mvEmptyListItems);
        jckIsUniversalPos.setEnabled(false);
        jckIsUniversalPos.setSelected(false);

        if (!jckIsUniversal.isSelected()) {
            if (moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                jckIsUniversalPos.setEnabled(true);
                for (int i = 0; i < mvUserPos.size(); i++) {
                    if (((int[]) ((SFormComponentItem) mvUserPos.get(i).get(0)).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                        for (int j = 1; j < mvUserPos.get(i).size(); j++) {
                            SFormUtilities.addListItem(jltUserPos, mvUserPos.get(i).get(j));
                        }
                        break;
                    }
                }
            }
        }
    }

    private void populateListCompanyBranchPos() {
        Vector<SFormComponentItem> vListItems = new Vector<>();
        boolean exists = false;

        if (!jckIsUniversal.isSelected()) {
            if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                vListItems.clear();
                for (int i = 0; i < mvDbmsPos.size(); i++) {
                    if (moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] == mvDbmsPos.get(i).getPkCompanyBranchId()) {
                        if (jltUserPos.getModel().getSize() == 0) {
                            vListItems.add(new SFormComponentItem(new int[] { mvDbmsPos.get(i).getPkCompanyBranchId(), mvDbmsPos.get(i).getPkEntityId() }, mvDbmsPos.get(i).getEntity()));
                        }
                        else {
                            exists = false;
                            for (int j = 0; j < jltUserPos.getModel().getSize(); j++) {
                                if (mvDbmsPos.get(i).getPkEntityId() == ((int[]) ((SFormComponentItem) jltUserPos.getModel().getElementAt(j)).getPrimaryKey())[1]) {
                                    exists = true;
                                    break;
                                }
                            }
                            if (!exists) {
                                vListItems.add(new SFormComponentItem(new int[] { mvDbmsPos.get(i).getPkCompanyBranchId(), mvDbmsPos.get(i).getPkEntityId() }, mvDbmsPos.get(i).getEntity()));
                            }
                        }
                    }
                }

                jltCompanyBranchPos.setListData(vListItems);
            }
            else {
                jltCompanyBranchPos.setListData(mvEmptyListItems);
            }
        }
    }

    private void populateListUserPlants() {
        jltUserPlants.setListData(mvEmptyListItems);
        jckIsUniversalPlant.setEnabled(false);
        jckIsUniversalPlant.setSelected(false);

        if (!jckIsUniversal.isSelected()) {
            if (moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                jckIsUniversalPlant.setEnabled(true);
                for (int i = 0; i < mvUserPlants.size(); i++) {
                     if (((int[]) ((SFormComponentItem) mvUserPlants.get(i).get(0)).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                         for (int j = 1; j < mvUserPlants.get(i).size(); j++) {
                              SFormUtilities.addListItem(jltUserPlants, mvUserPlants.get(i).get(j));
                         }
                         break;
                     }
                }
            }
        }
    }

    private void populateListCompanyBranchPlants() {
        Vector<SFormComponentItem> vListItems = new Vector<>();
        boolean exists = false;

        if (!jckIsUniversal.isSelected()) {
            if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
                vListItems.clear();
                for (int i = 0; i < mvDbmsPlants.size(); i++) {
                     if (mvDbmsPlants.get(i).getPkCompanyBranchId() == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                         if (jltUserPlants.getModel().getSize() == 0) {
                             vListItems.add(new SFormComponentItem(new int[] { mvDbmsPlants.get(i).getPkCompanyBranchId(), mvDbmsPlants.get(i).getPkEntityId() }, mvDbmsPlants.get(i).getEntity()));
                         }
                         else {
                             exists = false;
                             for (int j = 0; j < jltUserPlants.getModel().getSize(); j++) {
                                  if (mvDbmsPlants.get(i).getPkEntityId() == ((int[]) ((SFormComponentItem) jltUserPlants.getModel().getElementAt(j)).getPrimaryKey())[1]) {
                                      exists = true;
                                      break;
                                  }
                             }
                             if (!exists) {
                                 vListItems.add(new SFormComponentItem(new int[] { mvDbmsPlants.get(i).getPkCompanyBranchId(), mvDbmsPlants.get(i).getPkEntityId() }, mvDbmsPlants.get(i).getEntity()));
                             }
                         }
                     }
                }

                jltCompanyBranchPlants.setListData(vListItems);
            }
            else {
                jltCompanyBranchPlants.setListData(mvEmptyListItems);
            }
        }
    }

    private boolean validateReturnCompany(int pk) {
        Vector<SDataBizPartnerBranch> vListBranches = new Vector<>();
        boolean bExistConfig = false;
        int pkBranch;
        int x;

        vListBranches.clear();

        for (x = 0; x < mvDbmsCompanyBranches.size(); x++) {
            if (pk == mvDbmsCompanyBranches.get(x).getFkBizPartnerId()) {
                vListBranches.add(mvDbmsCompanyBranches.get(x));
            }
        }

        for (x = 0; x < moUserCompanyBranchesPane.getTableModel().getRowCount(); x++) {
            bExistConfig = false;
            pkBranch = ((int[]) moUserCompanyBranchesPane.getTableModel().getTableRow(x).getPrimaryKey())[0];
            for (int y = 0; y < vListBranches.size(); y++) {
                if (pkBranch == ((int[]) moUserCompanyBranchesPane.getTableRow(y).getPrimaryKey())[0]) {
                    bExistConfig = true;
                    break;
                }
            }
        }
        for (x = 0; x < mvUserBranches.size(); x++) {
            if (((int[]) mvUserBranches.get(x).get(0).getPrimaryKey())[0] == pk && mvUserBranches.get(x).size() > 1) {
                bExistConfig = true;
                break;
            }
        }

        return bExistConfig;
    }

    private boolean validateReturnBranch(int pk) {
        boolean bExistConfig = false;
        int x;

        if (jltUserWarehouses.getModel().getSize() > 0) {
            for (x = 0; x < jltUserWarehouses.getModel().getSize(); x++) {
                if (pk == ((int[]) ((SFormComponentItem) jltUserWarehouses.getModel().getElementAt(x)).getPrimaryKey())[0]) {
                    bExistConfig = true;
                    break;
                }
            }
        }

        if (jltUserPos.getModel().getSize() > 0) {
            for (x = 0; x < jltUserPos.getModel().getSize(); x++) {
                if (pk == ((int[]) ((SFormComponentItem) jltUserPos.getModel().getElementAt(x)).getPrimaryKey())[0]) {
                    bExistConfig = true;
                    break;
                }
            }
        }

        if (jltUserPlants.getModel().getSize() > 0) {
            for (x = 0; x < jltUserPlants.getModel().getSize(); x++) {
                if (pk == ((int[]) ((SFormComponentItem) jltUserPlants.getModel().getElementAt(x)).getPrimaryKey())[0]) {
                    bExistConfig = true;
                    break;
                }
            }
        }

        for (x = 0; x < mvUserWarehouses.size(); x++) {
            if (((int[]) mvUserWarehouses.get(x).get(0).getPrimaryKey())[0] == pk && mvUserWarehouses.get(x).size() > 1) {
                bExistConfig = true;
                break;
            }
        }
        for (x = 0; x < mvUserPos.size(); x++) {
            if (((int[]) mvUserPos.get(x).get(0).getPrimaryKey())[0] == pk && mvUserPos.get(x).size() > 1) {
                bExistConfig = true;
                break;
            }
        }
        for (x = 0; x < mvUserPlants.size(); x++) {
            if (((int[]) mvUserPlants.get(x).get(0).getPrimaryKey())[0] == pk && mvUserPlants.get(x).size() > 1) {
                bExistConfig = true;
                break;
            }
        }

        return bExistConfig;
    }

    private void removeCompanies() {
        for (int x = 0; x < moUserCompaniesPane.getTableModel().getRowCount(); x++) {
            removeBranches(((int[]) moUserCompaniesPane.getTableRow(x).getPrimaryKey())[0]);
            moUserCompaniesPane.removeTableRow(x);
            jcbCompanyBranch.removeAllItems();
            jcbCompanyEntity.removeAllItems();
            jcbCompanyBranch.setEnabled(false);
            jcbCompanyEntity.setEnabled(false);
            populateTableUserBranches();
        }
    }

    private void removeBranches(int pkCompany) {
        Vector<SDataBizPartnerBranch> vListBranches = new Vector<>();
        Vector<SFormComponentItem> vListItems = new Vector<>();
        STableRow row = null;
        boolean exists;
        int pkBranch;
        int x;
        int y;

        vListBranches.clear();
        vListItems.clear();
        for (x = 0; x < mvDbmsCompanyBranches.size(); x++) {
            if (pkCompany == mvDbmsCompanyBranches.get(x).getFkBizPartnerId()) {
                vListBranches.add(mvDbmsCompanyBranches.get(x));
            }
        }

        for (x = 0; x < moUserCompanyBranchesPane.getTableModel().getRowCount(); x++) {
            exists = false;
            pkBranch = ((int[]) moUserCompanyBranchesPane.getTableRow(x).getPrimaryKey())[0];
            for (y = 0; y < vListBranches.size(); y++) {
                if (pkBranch == vListBranches.get(y).getPkBizPartnerBranchId()) {
                    exists = true;
                }
            }
            if (!exists ) {
                row = moUserCompanyBranchesPane.getTableRow(x);
                moUserCompanyBranchesPane.removeTableRow(x);
                vListItems.add(new SFormComponentItem(row.getPrimaryKey(), (String) row.getValues().get(0)));
            }
            else {
                removeBranchesDependencies(((int[]) moUserCompanyBranchesPane.getTableRow(x).getPrimaryKey())[0]);
            }
        }

        for (x = 0; x < mvUserBranches.size(); x++) {
            if (((int[]) mvUserBranches.get(x).get(0).getPrimaryKey())[0] == pkCompany) {
                for (y = mvUserBranches.get(x).size()-1; y > 0; y--) {
                    removeBranchesDependencies(((int[]) mvUserBranches.get(x).get(y).getPrimaryKey())[0]);
                    mvUserBranches.get(x).remove(y);
                }
            }
        }
    }

    private void removeBranchesDependencies(int pk) {
        Vector<SFormComponentItem> vListPlants = new Vector<>();
        int x;
        int y;

        vListPlants.clear();

        for (x = 0; x < mvUserCashAccounts.size(); x++) {
            if (((int[]) mvUserCashAccounts.get(x).get(0).getPrimaryKey())[0] == pk) {
                for (y = mvUserCashAccounts.get(x).size()-1; y > 0; y--) {
                    mvUserCashAccounts.get(x).remove(y);
                }
            }
        }

        for (x = 0; x < mvUserWarehouses.size(); x++) {
            if (((int[]) mvUserWarehouses.get(x).get(0).getPrimaryKey())[0] == pk) {
                for (y = mvUserWarehouses.get(x).size()-1; y > 0; y--) {
                    mvUserWarehouses.get(x).remove(y);
                }
            }
        }

        jcbCompanyBranchEntity.removeAllItems();
        jcbCompanyBranchEntity.setEnabled(false);

        for (x = 0; x < mvUserPos.size(); x++) {
            if (((int[]) mvUserPos.get(x).get(0).getPrimaryKey())[0] == pk) {
                for (y = mvUserPos.get(x).size()-1; y > 0; y--) {
                    mvUserPos.get(x).remove(y);
                }
            }
        }

        for (x = 0; x < mvUserPlants.size(); x++) {
            if (((int[]) mvUserPlants.get(x).get(0).getPrimaryKey())[0] == pk) {
                for (y = mvUserPlants.get(x).size()-1; y > 0; y--) {
                    mvUserPlants.get(x).remove(y);
                }
            }
        }
    }

    private void renderComboBoxCompany() {
        if (moUserCompaniesPane.getTableModel().getRowCount() == 0) {
            jcbCompanyBranch.setEnabled(false);
            jcbCompanyEntity.setEnabled(false);
            jltBranchesAvailable.setListData(mvEmptyListItems);
        }
        else {
            jcbCompanyBranch.setEnabled(true);
            jcbCompanyEntity.setEnabled(true);
        }
    }

    private void renderComboBoxBranch(int pkCompany, javax.swing.JComboBox comboBox) {
        if (pkCompany > 0) {
            if (comboBox == jcbCompanyBranchEntity) {
                jcbCompanyBranchEntity.setEnabled(true);
                populateComboBoxCompanyBranchEntity();
            }
        }
        else {
            if (comboBox == jcbCompanyBranchEntity) {
                jcbCompanyBranchEntity.setEnabled(false);
                jcbCompanyBranchEntity.removeAllItems();
            }
        }
    }

    private boolean getIsUniversalCompany(int pkCompany) {
        boolean isUniv = false;

        for (int i = 0; i < moUserCompaniesPane.getTableModel().getRowCount(); i++) {
            if (((int[]) moUserCompaniesPane.getTableModel().getTableRow(i).getPrimaryKey())[0] == pkCompany) {
                isUniv = (Boolean) moUserCompaniesPane.getTableModel().getTableRow(i).getValues().get(2);
            }
        }

        return isUniv;
    }

    private boolean getIsUniversalCompanyBranch(int pkCompanyBranch) {
        boolean isUniv = false;

        for (int i = 0; i < moUserCompanyBranchesPane.getTableModel().getRowCount(); i++) {
            if (((int[]) moUserCompanyBranchesPane.getTableModel().getTableRow(i).getPrimaryKey())[0] == pkCompanyBranch) {
                isUniv = (Boolean) moUserCompanyBranchesPane.getTableModel().getTableRow(i).getValues().get(2);
            }
        }
        return isUniv;
    }

    private void setDefualtEntity(int categoryEnt) {
        String entityDefualt = "";
        int index = 0;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
            switch (categoryEnt) {
                case SDataConstantsSys.CFGS_CT_ENT_CASH:
                    if (jltUserCashAccount.getSelectedIndex() != -1) {
                        unsetDefaultEntity(categoryEnt);
                        for (int i = 0; i < mvUserCashAccounts.size(); i++) {
                            if (((int[]) mvUserCashAccounts.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                                entityDefualt = mvUserCashAccounts.get(i).get(jltUserCashAccount.getSelectedIndex() + 1).getItem();
                                entityDefualt = entityDefualt + " *";
                                mvUserCashAccounts.get(i).get(jltUserCashAccount.getSelectedIndex() + 1).setItem(entityDefualt);
                                index = jltUserCashAccount.getSelectedIndex();
                            }
                        }
                    }
                    populateListUserCashAccounts();
                    jltUserCashAccount.setSelectedIndex(index);
                    break;
                    
                case SDataConstantsSys.CFGS_CT_ENT_WH:
                    if (jltUserWarehouses.getSelectedIndex() != -1) {
                        unsetDefaultEntity(categoryEnt);
                        for (int i = 0; i < mvUserWarehouses.size(); i++) {
                            if (((int[]) mvUserWarehouses.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                                entityDefualt = mvUserWarehouses.get(i).get(jltUserWarehouses.getSelectedIndex() + 1).getItem();
                                entityDefualt = entityDefualt + " *";
                                mvUserWarehouses.get(i).get(jltUserWarehouses.getSelectedIndex() + 1).setItem(entityDefualt);
                                index = jltUserWarehouses.getSelectedIndex();
                            }
                        }
                    }
                    populateListUserWarehouses();
                    jltUserWarehouses.setSelectedIndex(index);
                    break;
                    
                case SDataConstantsSys.CFGS_CT_ENT_POS:
                    if (jltUserPos.getSelectedIndex() != -1) {
                        unsetDefaultEntity(categoryEnt);
                        for (int i = 0; i < mvUserPos.size(); i++) {
                            if (((int[]) mvUserPos.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                                entityDefualt = mvUserPos.get(i).get(jltUserPos.getSelectedIndex() + 1).getItem();
                                entityDefualt = entityDefualt + " *";
                                mvUserPos.get(i).get(jltUserPos.getSelectedIndex() + 1).setItem(entityDefualt);
                                index = jltUserPos.getSelectedIndex();
                            }
                        }
                    }
                    populateListUserPos();
                    jltUserPos.setSelectedIndex(index);
                    break;
                    
                case SDataConstantsSys.CFGS_CT_ENT_PLANT:
                    if (jltUserPlants.getSelectedIndex() != -1) {
                        unsetDefaultEntity(categoryEnt);
                        for (int i = 0; i < mvUserPlants.size(); i++) {
                            if (((int[]) mvUserPlants.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                                entityDefualt = mvUserPlants.get(i).get(jltUserPlants.getSelectedIndex() + 1).getItem();
                                entityDefualt = entityDefualt + " *";
                                mvUserPlants.get(i).get(jltUserPlants.getSelectedIndex() + 1).setItem(entityDefualt);
                                index = jltUserPlants.getSelectedIndex();
                            }
                        }
                    }
                    populateListUserPlants();
                    jltUserPlants.setSelectedIndex(index);
                    break;
                    
                default:
            }
        }
    }

    private void unsetDefaultEntity(int categoryEnt) {
        String entity = "";
        int index = 0;

        if (moFieldFkCompanyEntityId.getKeyAsIntArray()[0] > 0 && moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0] > 0) {
            switch (categoryEnt) {
                case SDataConstantsSys.CFGS_CT_ENT_CASH:
                    for (int i = 0; i < mvUserCashAccounts.size(); i++) {
                        if (((int[]) mvUserCashAccounts.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                            for (int j = 0; j < mvUserCashAccounts.get(i).size(); j++) {
                                index = mvUserCashAccounts.get(i).get(j).getItem().indexOf(" *");
                                if (index != -1) {
                                    entity = mvUserCashAccounts.get(i).get(j).getItem().substring(0, index);
                                    mvUserCashAccounts.get(i).get(j).setItem(entity);
                                }
                            }
                        }
                    }
                    break;
                case SDataConstantsSys.CFGS_CT_ENT_WH:
                    for (int i = 0; i < mvUserWarehouses.size(); i++) {
                        if (((int[]) mvUserWarehouses.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                            for (int j = 0; j < mvUserWarehouses.get(i).size(); j++) {
                                index = mvUserWarehouses.get(i).get(j).getItem().indexOf(" *");
                                if (index != -1) {
                                    entity = mvUserWarehouses.get(i).get(j).getItem().substring(0, index);
                                    mvUserWarehouses.get(i).get(j).setItem(entity);
                                }
                            }
                        }
                    }
                    break;
                case SDataConstantsSys.CFGS_CT_ENT_POS:
                    for (int i = 0; i < mvUserPos.size(); i++) {
                        if (((int[]) mvUserPos.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                            for (int j = 0; j < mvUserPos.get(i).size(); j++) {
                                index = mvUserPos.get(i).get(j).getItem().indexOf(" *");
                                if (index != -1) {
                                    entity = mvUserPos.get(i).get(j).getItem().substring(0, index);
                                    mvUserPos.get(i).get(j).setItem(entity);
                                }
                            }
                        }
                    }
                    break;
                case SDataConstantsSys.CFGS_CT_ENT_PLANT:
                    for (int i = 0; i < mvUserPlants.size(); i++) {
                        if (((int[]) mvUserPlants.get(i).get(0).getPrimaryKey())[0] == moFieldFkCompanyBranchEntityId.getKeyAsIntArray()[0]) {
                            for (int j = 0; j < mvUserPlants.get(i).size(); j++) {
                                index = mvUserPlants.get(i).get(j).getItem().indexOf(" *");
                                if (index != -1) {
                                    entity = mvUserPlants.get(i).get(j).getItem().substring(0, index);
                                    mvUserPlants.get(i).get(j).setItem(entity);
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private java.lang.String takeOffIsDefault(java.lang.String s, int index) {
        String entity = "";

        entity = s.substring(0, index);

        return entity;
    }

    private boolean isDefaultEntity(String str) {
        boolean isDefault = false;

        if (str.indexOf(" *") != -1) {
            isDefault = true;
        }

        return isDefault;
    }

    private void populateEntityCheckBox(int pkCompanyBranch) {
        for (int i = 0; i < mvUserBranches.size(); i++) {
            for (int j = 1; j < mvUserBranches.get(i).size(); j++) {
                if (((int[]) mvUserBranches.get(i).get(j).getPrimaryKey())[0] == pkCompanyBranch) {
                    jckIsUniversalCashAccount.setSelected((Boolean) mvUserBranches.get(i).get(j).getValues().get(3));
                    jckIsUniversalWarehouse.setSelected((Boolean) mvUserBranches.get(i).get(j).getValues().get(4));
                    jckIsUniversalPos.setSelected((Boolean) mvUserBranches.get(i).get(j).getValues().get(5));
                    jckIsUniversalPlant.setSelected((Boolean) mvUserBranches.get(i).get(j).getValues().get(6));
                }
            }
        }
    }

    private boolean isUniversalEntity(int categoryEnt, int companyBranch) {
        boolean isUniv = false;

        switch (categoryEnt) {
            case SDataConstantsSys.CFGS_CT_ENT_CASH:
                for (int k = 0; k < moUser.getDbmsAccessCompanyBranchEntitiesUniversal().size(); k++) {
                    if (moUser.getDbmsAccessCompanyBranchEntitiesUniversal().get(k).getPkCompanyBranchId() == companyBranch && moUser.getDbmsAccessCompanyBranchEntitiesUniversal().get(k).getPkEntityCategoryId() == SDataConstantsSys.CFGS_CT_ENT_CASH) {
                        isUniv = true;
                        break;
                    }
                    else {
                        isUniv = false;
                    }
                }
                break;
            case SDataConstantsSys.CFGS_CT_ENT_WH:
                for (int k = 0; k < moUser.getDbmsAccessCompanyBranchEntitiesUniversal().size(); k++) {
                    if (moUser.getDbmsAccessCompanyBranchEntitiesUniversal().get(k).getPkCompanyBranchId() == companyBranch && moUser.getDbmsAccessCompanyBranchEntitiesUniversal().get(k).getPkEntityCategoryId() == SDataConstantsSys.CFGS_CT_ENT_WH) {
                        isUniv = true;
                        break;
                    }
                    else {
                        isUniv = false;
                    }
                }
                break;
            case SDataConstantsSys.CFGS_CT_ENT_POS:
                for (int k = 0; k < moUser.getDbmsAccessCompanyBranchEntitiesUniversal().size(); k++) {
                    if (moUser.getDbmsAccessCompanyBranchEntitiesUniversal().get(k).getPkCompanyBranchId() == companyBranch && moUser.getDbmsAccessCompanyBranchEntitiesUniversal().get(k).getPkEntityCategoryId() == SDataConstantsSys.CFGS_CT_ENT_POS) {
                        isUniv = true;
                        break;
                    }
                    else {
                        isUniv = false;
                    }
                }
                break;
            case SDataConstantsSys.CFGS_CT_ENT_PLANT:
                for (int k = 0; k < moUser.getDbmsAccessCompanyBranchEntitiesUniversal().size(); k++) {
                    if (moUser.getDbmsAccessCompanyBranchEntitiesUniversal().get(k).getPkCompanyBranchId() == companyBranch && moUser.getDbmsAccessCompanyBranchEntitiesUniversal().get(k).getPkEntityCategoryId() == SDataConstantsSys.CFGS_CT_ENT_PLANT) {
                        isUniv = true;
                        break;
                    }
                    else {
                        isUniv = false;
                    }
                }
                break;
            default:
                break;
        }

        return isUniv;
    }

    private java.lang.String validateDefaultBranchOnEachCompany() {
        String companyWithMoreThanOneDefaultBranch = "";
        
        for (Vector<STableRowCustom> vectorOfBranchTableRows : mvUserBranches) {
            /* IMPORTANT NOTE FOR CLARIFYING BIZARRE DATA STRUCTURE OF SETTINGS OF EACH COMPANY AND ITS BRANCHES:
             * Table row at index 0 is the company!!!
             *  On each table row for company, data in vector values per index is: 0 = "company name", and that's it!!!
             * Table rows starting at index 1 are the branches of the company!!!
             *  On each table row for branch, data in vector values per index is: 0 = "branch name"; 1 = flag "is default"; 2 = flag "is universal"; and in the following indexes more other flags for universal access to branch entities!!!
             */

            if (vectorOfBranchTableRows.size() > 2) { // (2 = 1 company + 1 branch) current company has more than one branch
                int defaultBranchesCount = 0;
                String currentCompany = (String) vectorOfBranchTableRows.get(0).getValues().get(0);

                for (int branchIndex = 1; branchIndex < vectorOfBranchTableRows.size(); branchIndex++) { // remember that branches start at index 1 in vector of table rows, bizarre!!!
                    if ((Boolean) vectorOfBranchTableRows.get(branchIndex).getValues().get(1)) { // flag "is default" is at index 1, bizarre!!!
                        defaultBranchesCount++;
                    }
                }

                if (defaultBranchesCount > 1) {
                    companyWithMoreThanOneDefaultBranch = currentCompany;
                    break;
                }
            }
        }
        
        return companyWithMoreThanOneDefaultBranch;
    }

    public void publicActionReturnCompany() {
        actionReturnCompany();
    }

    public void publicActionReturnBranch() {
        actionReturnBranch();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Dummy5;
    private javax.swing.JLabel Dummy6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel24;
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
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
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
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbEditUserPassword;
    private javax.swing.JButton jbFkBizPartnerId;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbReturnAllBranch;
    private javax.swing.JButton jbReturnAllCashAccounts;
    private javax.swing.JButton jbReturnAllCompany;
    private javax.swing.JButton jbReturnAllPlants;
    private javax.swing.JButton jbReturnAllPos;
    private javax.swing.JButton jbReturnAllWarehouses;
    private javax.swing.JButton jbReturnBranch;
    private javax.swing.JButton jbReturnCashAccount;
    private javax.swing.JButton jbReturnCompany;
    private javax.swing.JButton jbReturnPlant;
    private javax.swing.JButton jbReturnPos;
    private javax.swing.JButton jbReturnWarehouse;
    private javax.swing.JButton jbSetDefaultCashAccount;
    private javax.swing.JButton jbSetDefaultPlant;
    private javax.swing.JButton jbSetDefaultPos;
    private javax.swing.JButton jbSetDefaultWarehouse;
    private javax.swing.JButton jbTransferAllBranch;
    private javax.swing.JButton jbTransferAllCashAccounts;
    private javax.swing.JButton jbTransferAllCompany;
    private javax.swing.JButton jbTransferAllPlants;
    private javax.swing.JButton jbTransferAllPos;
    private javax.swing.JButton jbTransferAllWarehouses;
    private javax.swing.JButton jbTransferBranch;
    private javax.swing.JButton jbTransferCashAccount;
    private javax.swing.JButton jbTransferCompany;
    private javax.swing.JButton jbTransferPlant;
    private javax.swing.JButton jbTransferPos;
    private javax.swing.JButton jbTransferWarehouse;
    private javax.swing.JComboBox<SFormComponentItem> jcbCompanyBranch;
    private javax.swing.JComboBox<SFormComponentItem> jcbCompanyBranchEntity;
    private javax.swing.JComboBox<SFormComponentItem> jcbCompanyEntity;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkBizPartnerId;
    private javax.swing.JCheckBox jckIsActive;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JCheckBox jckIsUniversal;
    private javax.swing.JCheckBox jckIsUniversalBranch;
    private javax.swing.JCheckBox jckIsUniversalCashAccount;
    private javax.swing.JCheckBox jckIsUniversalCompanyBranch;
    private javax.swing.JCheckBox jckIsUniversalCompanyEntity;
    private javax.swing.JCheckBox jckIsUniversalPlant;
    private javax.swing.JCheckBox jckIsUniversalPos;
    private javax.swing.JCheckBox jckIsUniversalWarehouse;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlBranchUser;
    private javax.swing.JLabel jlBranchesAvailable;
    private javax.swing.JLabel jlCompaniesAvailable;
    private javax.swing.JLabel jlCompany;
    private javax.swing.JLabel jlCompanyBranchCashAccount;
    private javax.swing.JLabel jlCompanyBranchEntity;
    private javax.swing.JLabel jlCompanyBranchPlants;
    private javax.swing.JLabel jlCompanyBranchPos;
    private javax.swing.JLabel jlCompanyBranchWarehouses;
    private javax.swing.JLabel jlCompanyEntity;
    private javax.swing.JLabel jlCompanyUser;
    private javax.swing.JLabel jlEmail;
    private javax.swing.JLabel jlUser;
    private javax.swing.JLabel jlUserCashAccount;
    private javax.swing.JLabel jlUserPassword;
    private javax.swing.JLabel jlUserPasswordConfirm;
    private javax.swing.JLabel jlUserPlants;
    private javax.swing.JLabel jlUserPos;
    private javax.swing.JLabel jlUserWarehouses;
    private javax.swing.JList<SFormComponentItem> jltBranchesAvailable;
    private javax.swing.JList<SFormComponentItem> jltCompaniesAvailable;
    private javax.swing.JList<SFormComponentItem> jltCompanyBranchCashAccount;
    private javax.swing.JList<SFormComponentItem> jltCompanyBranchPlants;
    private javax.swing.JList<SFormComponentItem> jltCompanyBranchPos;
    private javax.swing.JList<SFormComponentItem> jltCompanyBranchWarehouses;
    private javax.swing.JList<SFormComponentItem> jltUserCashAccount;
    private javax.swing.JList<SFormComponentItem> jltUserPlants;
    private javax.swing.JList<SFormComponentItem> jltUserPos;
    private javax.swing.JList<SFormComponentItem> jltUserWarehouses;
    private javax.swing.JPanel jpBranches;
    private javax.swing.JPanel jpBranches0;
    private javax.swing.JPanel jpBranches1;
    private javax.swing.JPanel jpBranches11;
    private javax.swing.JPanel jpBranchesAvailable;
    private javax.swing.JPanel jpBranchesControls;
    private javax.swing.JPanel jpBranchesControls1;
    private javax.swing.JPanel jpBranchesControls1C;
    private javax.swing.JPanel jpBranchesControls1E;
    private javax.swing.JPanel jpBranchesControls1N;
    private javax.swing.JPanel jpBranchesControls1W;
    private javax.swing.JPanel jpBranchesUser;
    private javax.swing.JPanel jpCompanies;
    private javax.swing.JPanel jpCompanies1;
    private javax.swing.JPanel jpCompaniesAvailable;
    private javax.swing.JPanel jpCompaniesControls;
    private javax.swing.JPanel jpCompaniesControls1;
    private javax.swing.JPanel jpCompaniesControls1C;
    private javax.swing.JPanel jpCompaniesControls1E;
    private javax.swing.JPanel jpCompaniesControls1N;
    private javax.swing.JPanel jpCompaniesControls1W;
    private javax.swing.JPanel jpCompaniesUser;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpControls1;
    private javax.swing.JPanel jpControls2;
    private javax.swing.JPanel jpEntities;
    private javax.swing.JPanel jpUser;
    private javax.swing.JPanel jpUser1;
    private javax.swing.JPasswordField jpfUserPassword;
    private javax.swing.JPasswordField jpfUserPasswordConfirm;
    private javax.swing.JScrollPane jspBranchesAvailable;
    private javax.swing.JScrollPane jspCompaniesAvailable;
    private javax.swing.JTextField jtfEmail;
    private javax.swing.JTextField jtfPkUserId_Ro;
    private javax.swing.JTextField jtfUser;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        moUser.setPkUserId(SLibConstants.UNDEFINED);
        moUser.setIsRegistryNew(true);
        jpfUserPassword.setEnabled(true);
        jpfUserPasswordConfirm.setEnabled(true);
        jbEditUserPassword.setEnabled(false);
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        moUser = null;

        jpfUserPassword.setText("");
        jpfUserPasswordConfirm.setText("");
        jtfPkUserId_Ro.setText("");

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        createDataVectors();
        moUserCompaniesPane.createTable(null);
        moUserCompaniesPane.clearTableRows();
        moUserCompanyBranchesPane.createTable(null);
        moUserCompanyBranchesPane.clearTableRows();
        jpfUserPassword.setEnabled(true);
        jpfUserPasswordConfirm.setEnabled(true);
        jckIsDeleted.setEnabled(false);
        jckIsActive.setEnabled(false);
        jbEditUserPassword.setEnabled(false);
        jltBranchesAvailable.setListData(mvEmptyListItems);
        jltCompanyBranchCashAccount.setListData(mvEmptyListItems);
        jltUserCashAccount.setListData(mvEmptyListItems);
        jltCompanyBranchWarehouses.setListData(mvEmptyListItems);
        jltUserWarehouses.setListData(mvEmptyListItems);
        jltCompanyBranchPos.setListData(mvEmptyListItems);
        jltUserPos.setListData(mvEmptyListItems);
        jltCompanyBranchPlants.setListData(mvEmptyListItems);
        jltUserPlants.setListData(mvEmptyListItems);
        jTabbedPane.setSelectedIndex(0);
        jTabbedPane2.setSelectedIndex(0);
        jcbCompanyBranch.removeAllItems();
        jcbCompanyBranch.setEnabled(false);
        jcbCompanyEntity.removeAllItems();
        jcbCompanyEntity.setEnabled(false);
        jcbCompanyBranchEntity.removeAllItems();
        jcbCompanyBranchEntity.setEnabled(false);
        jckIsUniversalCashAccount.setSelected(false);
        jckIsUniversalWarehouse.setSelected(false);
        jckIsUniversalPos.setSelected(false);
        jckIsUniversalPlant.setSelected(false);
        renderButtons();
        readErpInformation();
        mbResetCompanyBranchEntities = false;
        mbResetingForm = false;
    }

    @Override
    public void formRefreshCatalogues() {
        mbResetingForm = true;
        SFormUtilities.populateComboBox(miClient, jcbFkBizPartnerId, SDataConstants.BPSX_BP_EMP);
        SFormUtilities.populateList(miClient, jltCompaniesAvailable, SDataConstants.CFGU_CO);
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        java.lang.String password = "";
        erp.server.SServerRequest request = null;
        erp.server.SServerResponse response = null;
        erp.musr.data.SProcUserNameVal procUserNameVal = null;
        erp.lib.form.SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                break;
            }
        }

        if (!validation.getIsError()) {
            procUserNameVal = new SProcUserNameVal();
            procUserNameVal.getParamsIn().add(moUser == null ? 0 : moUser.getPkUserId());
            procUserNameVal.getParamsIn().add(moFieldUser.getString());
            request = new SServerRequest(SServerConstants.REQ_DB_PROCEDURE, procUserNameVal);
            response = miClient.getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                validation.setMessage(SLibConstants.MSG_ERR_DB_STP);
            }
            else {
                String companyWithMoreThanOneDefaultBranch = validateDefaultBranchOnEachCompany();
                
                if (!companyWithMoreThanOneDefaultBranch.isEmpty()) {
                    validation.setTabbedPaneIndex(TAB_IDX_BRANCHES);
                    validation.setMessage("Solo se puede definir una sucursal predeterminada para la empresa:\n" + "'" + companyWithMoreThanOneDefaultBranch + "'");
                }
                else {
                    procUserNameVal = (erp.musr.data.SProcUserNameVal) response.getPacket();

                    if ((java.lang.Integer) procUserNameVal.getParamsOut().get(0) > 0) {
                        validation.setMessage("El valor del campo '" + jlUser.getText() + "' ya existe.");
                        validation.setComponent(jtfUser);
                    }
                    else if (jpfUserPassword.isEnabled()) {
                        password = new String(jpfUserPassword.getPassword());

                        if (password.length() < 1) {
                            validation.setMessage("La longitud de la contraseña no puede ser menor a 1.");
                        }
                        else if (password.length() > 16) {
                            validation.setMessage("La longitud de la contraseña no puede ser mayor a 16.");
                        }
                        else if (password.compareTo(new String(jpfUserPasswordConfirm.getPassword())) != 0) {
                            validation.setMessage("La confirmación de la contraseña no coincide.");
                        }

                        if (validation.getIsError()) {
                            jpfUserPassword.setText("");
                            jpfUserPasswordConfirm.setText("");
                            validation.setComponent(jpfUserPassword);
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
        moUser = (erp.musr.data.SDataUser) registry;
        STableRowCustom row = null;
        SFormComponentItem item = null;
        String entity = "";
        int i;
        int j;

        moFieldUser.setFieldValue(moUser.getUser());
        moFieldEmail.setFieldValue(moUser.getEmail());
        moFieldIsUniversalCompanies.setFieldValue(moUser.getIsUniversal());
        moFieldIsActive.setFieldValue(moUser.getIsActive());
        moFieldIsDeleted.setFieldValue(moUser.getIsDeleted());
        moFieldFkBizPartnerId.setFieldValue(new int[] { moUser.getFkBizPartnerId_n() });
        jtfPkUserId_Ro.setText("" + moUser.getPkUserId());

        jpfUserPassword.setEnabled(false);
        jpfUserPasswordConfirm.setEnabled(false);
        jbEditUserPassword.setEnabled(true);
        jckIsActive.setEnabled(true);
        jckIsDeleted.setEnabled(moUser.getIsCanDelete());

        for (i = 0; i < moUser.getDbmsAccessCompanies().size(); i++) {
            for (j = 0; j < jltCompaniesAvailable.getModel().getSize(); j++ ) {
                if(((int[]) ((SFormComponentItem) jltCompaniesAvailable.getModel().getElementAt(j)).getPrimaryKey())[0] == moUser.getDbmsAccessCompanies().get(i).getPkCompanyId()) {
                    jltCompaniesAvailable.setSelectedIndex(j);
                    row = new STableRowCustom();
                    row.setPrimaryKey(new int[] { moUser.getDbmsAccessCompanies().get(i).getPkCompanyId() });
                    row.getValues().add(((SFormComponentItem) jltCompaniesAvailable.getModel().getElementAt(jltCompaniesAvailable.getSelectedIndex())).getItem());
                    row.getValues().add(moUser.getDbmsAccessCompanies().get(i).getIsDefault());
                    row.getValues().add(moUser.getDbmsAccessCompanies().get(i).getIsUniversal());
                    moUserCompaniesPane.addTableRow(row);
                    SFormUtilities.removeListSelectedItem(jltCompaniesAvailable);
                }
            }
        }

        // Read the branches of the companies:

        for (i = 0; i < moUser.getDbmsAccessCompanyBranches().size(); i ++) {
            for (j = 0; j < mvUserBranches.size(); j++) {
                if (((int[]) mvUserBranches.get(j).get(0).getPrimaryKey())[0] == moUser.getDbmsAccessCompanyBranches().get(i).getDbmsFkCompanyId()) {
                    row = new STableRowCustom();
                    row.setPrimaryKey(new int[] { moUser.getDbmsAccessCompanyBranches().get(i).getPkCompanyBranchId(), moUser.getDbmsAccessCompanyBranches().get(i).getDbmsFkCompanyId() });
                    row.getValues().add(moUser.getDbmsAccessCompanyBranches().get(i).getDbmsCompanyBranch());
                    row.getValues().add(moUser.getDbmsAccessCompanyBranches().get(i).getIsDefault());
                    row.getValues().add(moUser.getDbmsAccessCompanyBranches().get(i).getIsUniversal());
                    if (moUser.getDbmsAccessCompanyBranchEntitiesUniversal().size() > 0) {
                        row.getValues().add(isUniversalEntity(SDataConstantsSys.CFGS_CT_ENT_CASH, moUser.getDbmsAccessCompanyBranches().get(i).getPkCompanyBranchId()));
                        row.getValues().add(isUniversalEntity(SDataConstantsSys.CFGS_CT_ENT_WH, moUser.getDbmsAccessCompanyBranches().get(i).getPkCompanyBranchId()));
                        row.getValues().add(isUniversalEntity(SDataConstantsSys.CFGS_CT_ENT_POS, moUser.getDbmsAccessCompanyBranches().get(i).getPkCompanyBranchId()));
                        row.getValues().add(isUniversalEntity(SDataConstantsSys.CFGS_CT_ENT_PLANT, moUser.getDbmsAccessCompanyBranches().get(i).getPkCompanyBranchId()));
                        mvUserBranches.get(j).add(row);
                    }
                    else {
                        row.getValues().add(false);
                        row.getValues().add(false);
                        row.getValues().add(false);
                        row.getValues().add(false);
                        mvUserBranches.get(j).add(row);
                    }
                }
            }
        }

        // Read the access to the cash accounts:

        for (i = 0; i < moUser.getDbmsAccessCompanyBranchEntities().size(); i ++) {
            for (j = 0; j < mvUserCashAccounts.size(); j++) {
                if (((int[]) mvUserCashAccounts.get(j).get(0).getPrimaryKey())[0] == moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkCompanyBranchId()) {
                    if (moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getFkEntityCategoryId() == SDataConstantsSys.CFGS_CT_ENT_CASH) {
                        if (moUser.getDbmsAccessCompanyBranchEntities().get(i).getIsDefault()) {
                            entity = moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getEntity() + " *";
                        }
                        else {
                            entity = moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getEntity();
                        }
                        item = new SFormComponentItem(new int[] { moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkCompanyBranchId(), moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkEntityId() }, entity);
                        mvUserCashAccounts.get(j).add(item);
                    }
                }
            }
        }

        // Read the access to the warehouses:

        for (i = 0; i < moUser.getDbmsAccessCompanyBranchEntities().size(); i ++) {
            for (j = 0; j < mvUserWarehouses.size(); j++) {
                if (((int[]) mvUserWarehouses.get(j).get(0).getPrimaryKey())[0] == moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkCompanyBranchId()) {
                    if (moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getFkEntityCategoryId() == SDataConstantsSys.CFGS_CT_ENT_WH) {
                        if (moUser.getDbmsAccessCompanyBranchEntities().get(i).getIsDefault()) {
                            entity = moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getEntity() + " *";
                        }
                        else {
                            entity = moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getEntity();
                        }
                        item = new SFormComponentItem(new int[] { moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkCompanyBranchId(), moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkEntityId() }, entity);
                        mvUserWarehouses.get(j).add(item);
                    }
                }
            }
        }

        // Read the access to the pos:

        for (i = 0; i < moUser.getDbmsAccessCompanyBranchEntities().size(); i ++) {
            for (j = 0; j < mvUserPos.size(); j++) {
                if (((int[]) mvUserPos.get(j).get(0).getPrimaryKey())[0] == moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkCompanyBranchId()) {
                    if (moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getFkEntityCategoryId() == SDataConstantsSys.CFGS_CT_ENT_POS) {
                        if (moUser.getDbmsAccessCompanyBranchEntities().get(i).getIsDefault()) {
                            entity = moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getEntity() + " *";
                        }
                        else {
                            entity = moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getEntity();
                        }
                        item = new SFormComponentItem(new int[] { moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkCompanyBranchId(), moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkEntityId() }, entity);
                        mvUserPos.get(j).add(item);
                    }
                }
            }
        }

        // Read the access to the plants:

        for (i = 0; i < moUser.getDbmsAccessCompanyBranchEntities().size(); i ++) {
            for (j = 0; j < mvUserPlants.size(); j++) {
                if (((int[]) mvUserPlants.get(j).get(0).getPrimaryKey())[0] == moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkCompanyBranchId()) {
                    if (moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getFkEntityCategoryId() == SDataConstantsSys.CFGS_CT_ENT_PLANT) {
                        if (moUser.getDbmsAccessCompanyBranchEntities().get(i).getIsDefault()) {
                            entity = moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getEntity() + " *";
                        }
                        else {
                            entity = moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getEntity();
                        }
                        item = new SFormComponentItem(new int[] { moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkCompanyBranchId(), moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkEntityId() }, entity);
                        mvUserPlants.get(j).add(item);
                    }
                }
            }
        }

        renderComboBoxCompany();
        populateComboBoxCompany();
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        int x;
        int y;
        SDataAccessCompany company = null;
        SDataAccessCompanyBranch branch = null;
        SDataAccessCompanyBranchEntity entity = null;
        SDataAccessCompanyBranchEntityUniversal univEnt = null;

        if (moUser == null) {
            moUser = new SDataUser();
            moUser.setIsCanEdit(true);
            moUser.setIsCanDelete(true);
            moUser.setIsActive(true);
            moUser.setFkUserTypeId(SModSysConsts.USRS_TP_USR_USR);  // XXX make this assignation by GUI, sflores 2013-08-02
            moUser.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moUser.setIsActive(moFieldIsActive.getBoolean());
            moUser.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moUser.setUser(moFieldUser.getString());
        moUser.setEmail(moFieldEmail.getString());
        moUser.setIsUniversal(moFieldIsUniversalCompanies.getBoolean());
        moUser.setIsDeleted(moFieldIsDeleted.getBoolean());
        moUser.setFkBizPartnerId_n(moFieldFkBizPartnerId.getKeyAsIntArray()[0] == 0 ? -1 : moFieldFkBizPartnerId.getKeyAsIntArray()[0]);
        moUser.setAuxCompanyId(miClient.getSessionXXX().getCurrentCompany().getPkCompanyId());

        if (jpfUserPassword.isEnabled()) {
            moUser.setUserPassword(new String(jpfUserPassword.getPassword()));
            moUser.setExtraIsPasswordUpdateRequired(true);
        }
        else {
            moUser.setUserPassword(moUser.getUserPassword());
        }

        // Save the access configuration for the companies:

        moUser.getDbmsAccessCompanies().clear();
        if (moUserCompaniesPane.getTableModel().getRowCount() > 0) {
            for (x = 0 ; x < moUserCompaniesPane.getTableModel().getRowCount(); x++) {
                company = new SDataAccessCompany();
                company.setPkCompanyId(((int[]) moUserCompaniesPane.getTableRow(x).getPrimaryKey())[0]);
                company.setIsDefault((Boolean) moUserCompaniesPane.getTableRow(x).getValues().get(1));
                company.setIsUniversal((Boolean) moUserCompaniesPane.getTableRow(x).getValues().get(2));
                moUser.getDbmsAccessCompanies().add(company);
            }
        }

        // Save the access configuration for the company branches:

        moUser.getDbmsAccessCompanyBranchEntitiesUniversal().clear();
        moUser.getDbmsAccessCompanyBranches().clear();
        for (x = 0; x < mvUserBranches.size(); x++) {
            for(y = 1; y < mvUserBranches.get(x).size(); y++) {
                branch = new SDataAccessCompanyBranch();
                branch.setPkCompanyBranchId(((int[]) mvUserBranches.get(x).get(y).getPrimaryKey())[0]);
                branch.setIsDefault((Boolean) mvUserBranches.get(x).get(y).getValues().get(1));
                branch.setIsUniversal((Boolean) mvUserBranches.get(x).get(y).getValues().get(2));
                moUser.getDbmsAccessCompanyBranches().add(branch);

                if ((Boolean)mvUserBranches.get(x).get(y).getValues().get(3)) {
                    univEnt = new SDataAccessCompanyBranchEntityUniversal();
                    univEnt.setPkCompanyBranchId(((int[]) mvUserBranches.get(x).get(y).getPrimaryKey())[0]);
                    univEnt.setPkEntityCategoryId(SDataConstantsSys.CFGS_CT_ENT_CASH);
                    moUser.getDbmsAccessCompanyBranchEntitiesUniversal().add(univEnt);
                }
                if ((Boolean)mvUserBranches.get(x).get(y).getValues().get(4)) {
                    univEnt = new SDataAccessCompanyBranchEntityUniversal();
                    univEnt.setPkCompanyBranchId(((int[]) mvUserBranches.get(x).get(y).getPrimaryKey())[0]);
                    univEnt.setPkEntityCategoryId(SDataConstantsSys.CFGS_CT_ENT_WH);
                    moUser.getDbmsAccessCompanyBranchEntitiesUniversal().add(univEnt);
                }
                if ((Boolean)mvUserBranches.get(x).get(y).getValues().get(5)) {
                    univEnt = new SDataAccessCompanyBranchEntityUniversal();
                    univEnt.setPkCompanyBranchId(((int[]) mvUserBranches.get(x).get(y).getPrimaryKey())[0]);
                    univEnt.setPkEntityCategoryId(SDataConstantsSys.CFGS_CT_ENT_POS);
                    moUser.getDbmsAccessCompanyBranchEntitiesUniversal().add(univEnt);
                }
                if ((Boolean)mvUserBranches.get(x).get(y).getValues().get(6)) {
                    univEnt = new SDataAccessCompanyBranchEntityUniversal();
                    univEnt.setPkCompanyBranchId(((int[]) mvUserBranches.get(x).get(y).getPrimaryKey())[0]);
                    univEnt.setPkEntityCategoryId(SDataConstantsSys.CFGS_CT_ENT_PLANT);
                    moUser.getDbmsAccessCompanyBranchEntitiesUniversal().add(univEnt);
                }
            }
        }

        // Save the access configuration for the company branch entities:

        moUser.getDbmsAccessCompanyBranchEntities().clear();

        for (x = 0; x < mvUserCashAccounts.size(); x++) {
            for(y = 1; y < mvUserCashAccounts.get(x).size(); y++) {
                entity = new SDataAccessCompanyBranchEntity();
                entity.setPkCompanyBranchId(((int[]) mvUserCashAccounts.get(x).get(y).getPrimaryKey())[0]);
                entity.setPkEntityId(((int[]) mvUserCashAccounts.get(x).get(y).getPrimaryKey())[1]);
                entity.setIsDefault(isDefaultEntity(mvUserCashAccounts.get(x).get(y).getItem()));
                moUser.getDbmsAccessCompanyBranchEntities().add(entity);
            }
        }

        for (x = 0; x < mvUserWarehouses.size(); x++) {
            for(y = 1; y < mvUserWarehouses.get(x).size(); y++) {
                entity = new SDataAccessCompanyBranchEntity();
                entity.setPkCompanyBranchId(((int[]) mvUserWarehouses.get(x).get(y).getPrimaryKey())[0]);
                entity.setPkEntityId(((int[]) mvUserWarehouses.get(x).get(y).getPrimaryKey())[1]);
                entity.setIsDefault(isDefaultEntity(mvUserWarehouses.get(x).get(y).getItem()));
                moUser.getDbmsAccessCompanyBranchEntities().add(entity);
            }
        }

        for (x = 0; x < mvUserPos.size(); x++) {
            for(y = 1; y < mvUserPos.get(x).size(); y++) {
                entity = new SDataAccessCompanyBranchEntity();
                entity.setPkCompanyBranchId(((int[]) mvUserPos.get(x).get(y).getPrimaryKey())[0]);
                entity.setPkEntityId(((int[]) mvUserPos.get(x).get(y).getPrimaryKey())[1]);
                entity.setIsDefault(isDefaultEntity(mvUserPos.get(x).get(y).getItem()));
                moUser.getDbmsAccessCompanyBranchEntities().add(entity);
            }
        }

        for (x = 0; x < mvUserPlants.size(); x++) {
            for(y = 1; y < mvUserPlants.get(x).size(); y++) {
                entity = new SDataAccessCompanyBranchEntity();
                entity.setPkCompanyBranchId(((int[]) mvUserPlants.get(x).get(y).getPrimaryKey())[0]);
                entity.setPkEntityId(((int[]) mvUserPlants.get(x).get(y).getPrimaryKey())[1]);
                entity.setIsDefault(isDefaultEntity(mvUserPlants.get(x).get(y).getItem()));
                moUser.getDbmsAccessCompanyBranchEntities().add(entity);
            }
        }

        return moUser;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
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
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbEditUserPassword) {
                actionEditUserPassword();
            }
            else if (button == jbFkBizPartnerId) {
                actionFkBizPartnerId();
            }
            else if (button == jbTransferCompany) {
                actionTransferCompany();
            }
            else if (button == jbTransferAllCompany) {
                actionTransferAllCompany();
            }
            else if (button == jbReturnCompany) {
                actionReturnCompany();
            }
            else if (button == jbReturnAllCompany) {
                actionReturnAllCompany();
            }
            else if (button == jbTransferBranch) {
                actionTransferBranch();
            }
            else if (button == jbTransferAllBranch) {
                actionTransferAllBranch();
            }
            else if (button == jbReturnBranch) {
                actionReturnBranch();
            }
            else if (button == jbReturnAllBranch) {
                actionReturnAllBranch();
            }
            else if (button == jbTransferCashAccount) {
                actionTransferCashAccount();
            }
            else if (button == jbTransferAllCashAccounts) {
                actionTransferAllCashAccounts();
            }
            else if (button == jbReturnCashAccount) {
                actionReturnCashAccount();
            }
            else if (button == jbReturnAllCashAccounts) {
                actionReturnAllCashAccounts();
            }
            else if (button == jbTransferWarehouse) {
                actionTransferWarehouse();
            }
            else if (button == jbTransferAllWarehouses) {
                actionTransferAllWarehouse();
            }
            else if (button == jbReturnWarehouse) {
                actionReturnWarehouse();
            }
            else if (button == jbReturnAllWarehouses) {
                actionReturnAllWarehouses();
            }
            else if (button == jbTransferPos) {
                actionTransferPos();
            }
            else if (button == jbTransferAllPos) {
                actionTransferAllPos();
            }
            else if (button == jbReturnPos) {
                actionReturnPos();
            }
            else if (button == jbReturnAllPos) {
                actionReturnAllPos();
            }
            else if (button == jbTransferPlant) {
                actionTransferPlant();
            }
            else if (button == jbTransferAllPlants) {
                actionTransferAllPlant();
            }
            else if (button == jbReturnPlant) {
                actionReturnPlant();
            }
            else if (button == jbReturnAllPlants) {
                actionReturnAllPlants();
            }
            else if (button == jbSetDefaultCashAccount) {
                setDefualtEntity(SDataConstantsSys.CFGS_CT_ENT_CASH);
            }
            else if (button == jbSetDefaultWarehouse) {
                setDefualtEntity(SDataConstantsSys.CFGS_CT_ENT_WH);
            }
            else if (button == jbSetDefaultPos) {
                setDefualtEntity(SDataConstantsSys.CFGS_CT_ENT_POS);
            }
            else if (button == jbSetDefaultPlant) {
                setDefualtEntity(SDataConstantsSys.CFGS_CT_ENT_PLANT);
            }
        }
    }

    @Override
    public SLibMethod getPostSaveMethod(SDataRegistry registry) {
        SLibMethod method = null;
        SDataUser oDataUser = (SDataUser) SDataUtilities.readRegistry(miClient, SDataConstants.USRU_USR, registry.getPrimaryKey(), SLibConstants.EXEC_MODE_STEALTH);
        
        if(!moUser.getIsActive() || moUser.getIsDeleted()){
            SUserExportUtils oExport = new SUserExportUtils((SGuiClient) miClient);
            boolean res = false;
            try {
                res = oExport.unactiveUser(moUser.getPkUserId(), moUser.getIsActive(), moUser.getIsDeleted());
            } catch (SQLException ex) {
                Logger.getLogger(SFormUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(SFormUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return method;
    }
}
