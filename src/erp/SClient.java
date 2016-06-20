package erp;

/*
 * SClient.java
 *
 * Created on 22 de marzo de 2008, 08:42 PM
 */

import cfd.DCfdSignature;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.gui.SGuiGlobalCataloguesBps;
import erp.gui.SGuiGlobalCataloguesItm;
import erp.gui.SGuiGlobalCataloguesLoc;
import erp.gui.SGuiGlobalCataloguesTrn;
import erp.gui.SGuiGlobalCataloguesUsr;
import erp.gui.SGuiModuleCfg;
import erp.gui.SGuiModuleFin;
import erp.gui.SGuiModuleHrs;
import erp.gui.SGuiModuleLog;
import erp.gui.SGuiModuleMfg;
import erp.gui.SGuiModuleMkt;
import erp.gui.SGuiModuleTrnInv;
import erp.gui.SGuiModuleTrnPur;
import erp.gui.SGuiModuleTrnSal;
import erp.gui.mod.cfg.SCfgProcessor;
import erp.gui.mod.xml.SXmlConfig;
import erp.gui.mod.xml.SXmlModConsts;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.form.SFormOptionPickerInterface;
import erp.lib.form.SFormUtilities;
import erp.lib.gui.SGuiDatePicker;
import erp.lib.gui.SGuiDateRangePicker;
import erp.mcfg.data.SDataCertificate;
import erp.mfin.data.SDataExchangeRate;
import erp.mod.SModConsts;
import erp.mod.SModUtils;
import erp.mod.SModuleBps;
import erp.mod.SModuleCfg;
import erp.mod.SModuleFin;
import erp.mod.SModuleHrs;
import erp.mod.SModuleItm;
import erp.mod.SModuleLog;
import erp.mod.SModuleMkt;
import erp.mod.SModuleTrn;
import erp.mod.SModuleUsr;
import erp.mod.usr.db.SDbUserGui;
import erp.server.SLoginRequest;
import erp.server.SLoginResponse;
import erp.server.SServerRemote;
import erp.server.SSessionXXX;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.db.SDbDatabaseMonitor;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiSession;
import sa.lib.gui.SGuiUserGui;
import sa.lib.gui.SGuiYearMonthPicker;
import sa.lib.gui.SGuiYearPicker;
import sa.lib.gui.bean.SBeanDialogReport;
import sa.lib.gui.bean.SBeanForm;
import sa.lib.gui.bean.SBeanFormDialog;
import sa.lib.gui.bean.SBeanFormProcess;
import sa.lib.gui.bean.SBeanOptionPicker;
import sa.lib.img.DImgConsts;
import sa.lib.srv.SSrvCompany;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author  Sergio Flores
 * @version 3.2
 */
public class SClient extends JFrame implements ActionListener, SClientInterface, SGuiClient {

    public static final String APP_NAME = "SIIE 3.2";
    public static final String APP_RELEASE = "3.2 051.01";
    public static final String APP_COPYRIGHT = "2008-2016";
    public static final String APP_PROVIDER = "Software Aplicado SA de CV";

    public static final String VENDOR_COPYRIGHT = APP_NAME + " ©" + APP_COPYRIGHT + " " + APP_PROVIDER;
    public static final String VENDOR_WEBSITE = "www.swaplicado.com.mx";

    private boolean mbFirstActivation;
    private boolean mbLoggedIn;
    private SParamsApp moParamsApp;
    private SLogin moLogin;
    private SLoginSession moLoginSession;
    private SServerRemote moServer;
    private SSessionXXX moSessionXXX;
    private SXmlConfig moXmlConfig;
    private SCfgProcessor moCfgProcessor;
    private erp.lib.gui.SGuiDatePicker moGuiDatePicker;
    private erp.lib.gui.SGuiDatePicker moGuiDatePeriodPicker;
    private erp.lib.gui.SGuiDateRangePicker moGuiDateRangePicker;
    private DCfdSignature moCfdSignature;
    private Vector<DCfdSignature> mvCfdSignatures;
    private JFileChooser moFileChooser;
    private SGuiGlobalCataloguesUsr moGlobalCataloguesUsr;
    private SGuiGlobalCataloguesLoc moGlobalCataloguesLoc;
    private SGuiGlobalCataloguesBps moGlobalCataloguesBps;
    private SGuiGlobalCataloguesItm moGlobalCataloguesItm;
    private SGuiGlobalCataloguesTrn moGlobalCataloguesTrn;
    private SGuiModuleCfg moModuleCfg;
    private SGuiModuleFin moModuleFin;
    private SGuiModuleTrnPur moModulePur;
    private SGuiModuleTrnSal moModuleSal;
    private SGuiModuleTrnInv moModuleInv;
    private SGuiModuleMkt moModuleMkt;
    private SGuiModuleLog moModuleLog;
    private SGuiModuleMfg moModuleMfg;
    private SGuiModuleHrs moModuleHrs;

    private SGuiSession moSession;
    private SDbDatabase moSysDatabase;
    private SDbDatabaseMonitor moSysDatabaseMonitor;
    private Statement miSysStatement;
    private String msCompany;
    private sa.lib.gui.SGuiDatePicker moDatePicker;
    private sa.lib.gui.SGuiDateRangePicker moDateRangePicker;
    private SGuiYearPicker moYearPicker;
    private SGuiYearMonthPicker moYearMonthPicker;

    private ImageIcon moIcon;
    private ImageIcon moIconNew;
    private ImageIcon moIconNewMain;
    private ImageIcon moIconInsert;
    private ImageIcon moIconCopy;
    private ImageIcon moIconEdit;
    private ImageIcon moIconAnnul;
    private ImageIcon moIconDelete;
    private ImageIcon moIconLook;
    private ImageIcon moIconPrint;
    private ImageIcon moIconPrintAckCancel;
    private ImageIcon moIconPrintOrder;
    private ImageIcon moIconPrintPhoto;
    private ImageIcon moIconKardex;
    private ImageIcon moIconNotes;
    private ImageIcon moIconDocAdd;
    private ImageIcon moIconDocRemove;
    private ImageIcon moIconAction;
    private ImageIcon moIconLink;
    private ImageIcon moIconContractAnalysis;
    private ImageIcon moIconSum;
    private ImageIcon moIconDocLink;
    private ImageIcon moIconDocLinkNo;
    private ImageIcon moIconDocSupply;
    private ImageIcon moIconDocSupplyNo;
    private ImageIcon moIconDocType;
    private ImageIcon moIconDocImport;
    private ImageIcon moIconDocOpen;
    private ImageIcon moIconDocClose;
    private ImageIcon moIconDocXml;
    private ImageIcon moIconDocXmlCancel;
    private ImageIcon moIconDocXmlSign;
    private ImageIcon moIconDocDelivery;
    private ImageIcon moIconFilterBp;
    private ImageIcon moIconFilterBpb;
    private ImageIcon moIconFilterDoc;
    private ImageIcon moIconApprove;
    private ImageIcon moIconApproveNo;
    private ImageIcon moIconQueryBizPartner;
    private ImageIcon moIconQueryDocument;
    private ImageIcon moIconQueryRecord;
    private ImageIcon moIconArrowUp;
    private ImageIcon moIconArrowDown;
    private ImageIcon moIconArrowLeft;
    private ImageIcon moIconArrowRight;
    private ImageIcon moIconBizPartnerExport;
    private ImageIcon moIconModuleCfg;
    private ImageIcon moIconModuleFin;
    private ImageIcon moIconModulePur;
    private ImageIcon moIconModuleSal;
    private ImageIcon moIconModuleInv;
    private ImageIcon moIconModuleMkt;
    private ImageIcon moIconModuleLog;
    private ImageIcon moIconModuleMfg;
    private ImageIcon moIconModuleHrs;
    private ImageIcon moIconCloseActive;
    private ImageIcon moIconCloseInactive;
    private ImageIcon moIconCloseBright;
    private ImageIcon moIconCloseDark;

    /** Creates new form SClient */
    public SClient() {
        initComponents();
        initComponentsCustom();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonGroup = new javax.swing.ButtonGroup();
        jpToolBars = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jtbModuleCfg = new javax.swing.JToggleButton();
        jtbModuleFin = new javax.swing.JToggleButton();
        jtbModulePur = new javax.swing.JToggleButton();
        jtbModuleSal = new javax.swing.JToggleButton();
        jtbModuleInv = new javax.swing.JToggleButton();
        jtbModuleMkt = new javax.swing.JToggleButton();
        jtbModuleLog = new javax.swing.JToggleButton();
        jtbModuleMfg = new javax.swing.JToggleButton();
        jtbModuleHrs = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlLogo = new javax.swing.JLabel();
        jTabbedPane = new javax.swing.JTabbedPane();
        jpStatus = new javax.swing.JPanel();
        jtfSystemDate = new javax.swing.JTextField();
        jtfCurrentDate = new javax.swing.JTextField();
        jbCurrentDate = new javax.swing.JButton();
        jtfLoginCompanyBranch = new javax.swing.JTextField();
        jtfLoginEntityCh = new javax.swing.JTextField();
        jtfLoginEntityWh = new javax.swing.JTextField();
        jtfLoginEntityPlt = new javax.swing.JTextField();
        jtfLoginEntityPos = new javax.swing.JTextField();
        jbSession = new javax.swing.JButton();
        jtfUser = new javax.swing.JTextField();
        jtfLoginTimestamp = new javax.swing.JTextField();
        jlAppRelease = new javax.swing.JLabel();
        jMenuBar = new javax.swing.JMenuBar();
        jmFile = new javax.swing.JMenu();
        jmiFileCurrentDate = new javax.swing.JMenuItem();
        jmiFileSession = new javax.swing.JMenuItem();
        jmiFilePassword = new javax.swing.JMenuItem();
        jsFile01 = new javax.swing.JSeparator();
        jmiFileCloseViews = new javax.swing.JMenuItem();
        jmiFileCloseSession = new javax.swing.JMenuItem();
        jsFile02 = new javax.swing.JSeparator();
        jmiFileClose = new javax.swing.JMenuItem();
        jmView = new javax.swing.JMenu();
        jmiViewModuleCfg = new javax.swing.JMenuItem();
        jmiViewModuleFin = new javax.swing.JMenuItem();
        jmiViewModulePur = new javax.swing.JMenuItem();
        jmiViewModuleSal = new javax.swing.JMenuItem();
        jmiViewModuleInv = new javax.swing.JMenuItem();
        jmiViewModuleMkt = new javax.swing.JMenuItem();
        jmiViewModuleLog = new javax.swing.JMenuItem();
        jmiViewModuleMfg = new javax.swing.JMenuItem();
        jmiViewModuleHrs = new javax.swing.JMenuItem();
        jmHelp = new javax.swing.JMenu();
        jmiHelpAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ERP");
        setExtendedState(Frame.MAXIMIZED_BOTH);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jpToolBars.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 2, 1, 2));
        jpToolBars.setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButtonGroup.add(jtbModuleCfg);
        jtbModuleCfg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_cfg_bw.png"))); // NOI18N
        jtbModuleCfg.setToolTipText("Configuración [Ctrl+1]");
        jtbModuleCfg.setFocusable(false);
        jtbModuleCfg.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModuleCfg.setMaximumSize(new java.awt.Dimension(64, 64));
        jtbModuleCfg.setMinimumSize(new java.awt.Dimension(48, 48));
        jtbModuleCfg.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModuleCfg.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_cfg.png"))); // NOI18N
        jtbModuleCfg.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_cfg.png"))); // NOI18N
        jtbModuleCfg.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jtbModuleCfg);

        jButtonGroup.add(jtbModuleFin);
        jtbModuleFin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_fin_bw.png"))); // NOI18N
        jtbModuleFin.setToolTipText("Contabilidad [Ctrl+2]");
        jtbModuleFin.setFocusable(false);
        jtbModuleFin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModuleFin.setMaximumSize(new java.awt.Dimension(64, 64));
        jtbModuleFin.setMinimumSize(new java.awt.Dimension(48, 48));
        jtbModuleFin.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModuleFin.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_fin.png"))); // NOI18N
        jtbModuleFin.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_fin.png"))); // NOI18N
        jtbModuleFin.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jtbModuleFin);

        jButtonGroup.add(jtbModulePur);
        jtbModulePur.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_pur_bw.png"))); // NOI18N
        jtbModulePur.setToolTipText("Egresos y CxP [Ctrl+3]");
        jtbModulePur.setFocusable(false);
        jtbModulePur.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModulePur.setMaximumSize(new java.awt.Dimension(64, 64));
        jtbModulePur.setMinimumSize(new java.awt.Dimension(48, 48));
        jtbModulePur.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModulePur.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_pur.png"))); // NOI18N
        jtbModulePur.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_pur.png"))); // NOI18N
        jtbModulePur.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jtbModulePur);

        jButtonGroup.add(jtbModuleSal);
        jtbModuleSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_sal_bw.png"))); // NOI18N
        jtbModuleSal.setToolTipText("Ingresos y CxC [Ctrl+4]");
        jtbModuleSal.setFocusable(false);
        jtbModuleSal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModuleSal.setMaximumSize(new java.awt.Dimension(64, 64));
        jtbModuleSal.setMinimumSize(new java.awt.Dimension(48, 48));
        jtbModuleSal.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModuleSal.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_sal.png"))); // NOI18N
        jtbModuleSal.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_sal.png"))); // NOI18N
        jtbModuleSal.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jtbModuleSal);

        jButtonGroup.add(jtbModuleInv);
        jtbModuleInv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_inv_bw.png"))); // NOI18N
        jtbModuleInv.setToolTipText("Inventarios [Ctrl+5]");
        jtbModuleInv.setFocusable(false);
        jtbModuleInv.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModuleInv.setMaximumSize(new java.awt.Dimension(64, 64));
        jtbModuleInv.setMinimumSize(new java.awt.Dimension(48, 48));
        jtbModuleInv.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModuleInv.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_inv.png"))); // NOI18N
        jtbModuleInv.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_inv.png"))); // NOI18N
        jtbModuleInv.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jtbModuleInv);

        jButtonGroup.add(jtbModuleMkt);
        jtbModuleMkt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_mkt_bw.png"))); // NOI18N
        jtbModuleMkt.setToolTipText("Comercialización [Ctrl+6]");
        jtbModuleMkt.setFocusable(false);
        jtbModuleMkt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModuleMkt.setMaximumSize(new java.awt.Dimension(64, 64));
        jtbModuleMkt.setMinimumSize(new java.awt.Dimension(48, 48));
        jtbModuleMkt.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModuleMkt.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_mkt.png"))); // NOI18N
        jtbModuleMkt.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_mkt.png"))); // NOI18N
        jtbModuleMkt.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jtbModuleMkt);

        jButtonGroup.add(jtbModuleLog);
        jtbModuleLog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_log_bw.png"))); // NOI18N
        jtbModuleLog.setToolTipText("Embarques [Ctrl+7]");
        jtbModuleLog.setFocusable(false);
        jtbModuleLog.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModuleLog.setMaximumSize(new java.awt.Dimension(64, 64));
        jtbModuleLog.setMinimumSize(new java.awt.Dimension(48, 48));
        jtbModuleLog.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModuleLog.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_log.png"))); // NOI18N
        jtbModuleLog.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_log.png"))); // NOI18N
        jtbModuleLog.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jtbModuleLog);

        jButtonGroup.add(jtbModuleMfg);
        jtbModuleMfg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_mfg_bw.png"))); // NOI18N
        jtbModuleMfg.setToolTipText("Producción [Ctrl+8]");
        jtbModuleMfg.setFocusable(false);
        jtbModuleMfg.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModuleMfg.setMaximumSize(new java.awt.Dimension(64, 64));
        jtbModuleMfg.setMinimumSize(new java.awt.Dimension(48, 48));
        jtbModuleMfg.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModuleMfg.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_mfg.png"))); // NOI18N
        jtbModuleMfg.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_mfg.png"))); // NOI18N
        jtbModuleMfg.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jtbModuleMfg);

        jButtonGroup.add(jtbModuleHrs);
        jtbModuleHrs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_hrs_bw.png"))); // NOI18N
        jtbModuleHrs.setToolTipText("Recursos humanos [Ctrl+9]");
        jtbModuleHrs.setFocusable(false);
        jtbModuleHrs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModuleHrs.setMaximumSize(new java.awt.Dimension(64, 64));
        jtbModuleHrs.setMinimumSize(new java.awt.Dimension(48, 48));
        jtbModuleHrs.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModuleHrs.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_hrs.png"))); // NOI18N
        jtbModuleHrs.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/mod_hrs.png"))); // NOI18N
        jtbModuleHrs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jtbModuleHrs);

        jpToolBars.add(jToolBar1, java.awt.BorderLayout.WEST);
        jpToolBars.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setPreferredSize(new java.awt.Dimension(64, 64));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jlLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/logo21.gif"))); // NOI18N
        jlLogo.setToolTipText("Logotipo de la empresa");
        jlLogo.setMaximumSize(new java.awt.Dimension(64, 64));
        jlLogo.setMinimumSize(new java.awt.Dimension(64, 64));
        jlLogo.setPreferredSize(new java.awt.Dimension(64, 64));
        jPanel2.add(jlLogo);

        jpToolBars.add(jPanel2, java.awt.BorderLayout.EAST);

        getContentPane().add(jpToolBars, java.awt.BorderLayout.NORTH);
        getContentPane().add(jTabbedPane, java.awt.BorderLayout.CENTER);

        jpStatus.setLayout(new java.awt.FlowLayout(0, 3, 5));

        jtfSystemDate.setEditable(false);
        jtfSystemDate.setText("01/01/2000");
        jtfSystemDate.setToolTipText("Fecha de sistema");
        jtfSystemDate.setFocusable(false);
        jtfSystemDate.setPreferredSize(new java.awt.Dimension(65, 20));
        jpStatus.add(jtfSystemDate);

        jtfCurrentDate.setEditable(false);
        jtfCurrentDate.setText("01/01/2000");
        jtfCurrentDate.setToolTipText("Fecha de trabajo");
        jtfCurrentDate.setFocusable(false);
        jtfCurrentDate.setPreferredSize(new java.awt.Dimension(65, 20));
        jpStatus.add(jtfCurrentDate);

        jbCurrentDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/gui_cal.gif"))); // NOI18N
        jbCurrentDate.setToolTipText("Cambiar fecha de trabajo...");
        jbCurrentDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jpStatus.add(jbCurrentDate);

        jtfLoginCompanyBranch.setBackground(java.awt.Color.lightGray);
        jtfLoginCompanyBranch.setEditable(false);
        jtfLoginCompanyBranch.setText("BRANCH");
        jtfLoginCompanyBranch.setToolTipText("Sucursal de la empresa actual");
        jtfLoginCompanyBranch.setFocusable(false);
        jtfLoginCompanyBranch.setPreferredSize(new java.awt.Dimension(150, 20));
        jpStatus.add(jtfLoginCompanyBranch);

        jtfLoginEntityCh.setEditable(false);
        jtfLoginEntityCh.setBackground(java.awt.Color.lightGray);
        jtfLoginEntityCh.setText("CH");
        jtfLoginEntityCh.setToolTipText("Cuenta de efectivo actual");
        jtfLoginEntityCh.setFocusable(false);
        jtfLoginEntityCh.setPreferredSize(new java.awt.Dimension(65, 20));
        jpStatus.add(jtfLoginEntityCh);

        jtfLoginEntityWh.setEditable(false);
        jtfLoginEntityWh.setBackground(java.awt.Color.lightGray);
        jtfLoginEntityWh.setText("WH");
        jtfLoginEntityWh.setToolTipText("Almacén actual");
        jtfLoginEntityWh.setFocusable(false);
        jtfLoginEntityWh.setPreferredSize(new java.awt.Dimension(65, 20));
        jpStatus.add(jtfLoginEntityWh);

        jtfLoginEntityPlt.setEditable(false);
        jtfLoginEntityPlt.setBackground(java.awt.Color.lightGray);
        jtfLoginEntityPlt.setText("PLT");
        jtfLoginEntityPlt.setToolTipText("Planta actual");
        jtfLoginEntityPlt.setFocusable(false);
        jtfLoginEntityPlt.setPreferredSize(new java.awt.Dimension(65, 20));
        jpStatus.add(jtfLoginEntityPlt);

        jtfLoginEntityPos.setEditable(false);
        jtfLoginEntityPos.setBackground(java.awt.Color.lightGray);
        jtfLoginEntityPos.setText("POS");
        jtfLoginEntityPos.setToolTipText("Punto de venta actual");
        jtfLoginEntityPos.setFocusable(false);
        jtfLoginEntityPos.setPreferredSize(new java.awt.Dimension(65, 20));
        jpStatus.add(jtfLoginEntityPos);

        jbSession.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/gui_session.gif"))); // NOI18N
        jbSession.setToolTipText("Cambiar sesión...");
        jbSession.setPreferredSize(new java.awt.Dimension(23, 23));
        jpStatus.add(jbSession);

        jtfUser.setEditable(false);
        jtfUser.setText("user");
        jtfUser.setToolTipText("Usuario actual");
        jtfUser.setFocusable(false);
        jtfUser.setPreferredSize(new java.awt.Dimension(75, 20));
        jpStatus.add(jtfUser);

        jtfLoginTimestamp.setEditable(false);
        jtfLoginTimestamp.setText("01/01/2000 00:00:00 +0000");
        jtfLoginTimestamp.setToolTipText("Marca de tiempo de acceso");
        jtfLoginTimestamp.setFocusable(false);
        jtfLoginTimestamp.setPreferredSize(new java.awt.Dimension(150, 20));
        jpStatus.add(jtfLoginTimestamp);

        jlAppRelease.setText("RELEASE");
        jlAppRelease.setPreferredSize(new java.awt.Dimension(100, 20));
        jpStatus.add(jlAppRelease);

        getContentPane().add(jpStatus, java.awt.BorderLayout.SOUTH);

        jmFile.setText("Archivo");

        jmiFileCurrentDate.setText("Cambiar fecha de trabajo...");
        jmFile.add(jmiFileCurrentDate);

        jmiFileSession.setText("Cambiar sesión...");
        jmFile.add(jmiFileSession);

        jmiFilePassword.setText("Cambiar contraseña...");
        jmFile.add(jmiFilePassword);
        jmFile.add(jsFile01);

        jmiFileCloseViews.setText("Cerrar todas las vistas");
        jmFile.add(jmiFileCloseViews);

        jmiFileCloseSession.setText("Cerrar sesión");
        jmFile.add(jmiFileCloseSession);
        jmFile.add(jsFile02);

        jmiFileClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jmiFileClose.setText("Cerrar");
        jmFile.add(jmiFileClose);

        jMenuBar.add(jmFile);

        jmView.setText("Ver");

        jmiViewModuleCfg.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModuleCfg.setText("Módulo configuración");
        jmView.add(jmiViewModuleCfg);

        jmiViewModuleFin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModuleFin.setText("Módulo contabilidad");
        jmView.add(jmiViewModuleFin);

        jmiViewModulePur.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModulePur.setText("Módulo egresos y CXP");
        jmView.add(jmiViewModulePur);

        jmiViewModuleSal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModuleSal.setText("Módulo ingresos y CXC");
        jmView.add(jmiViewModuleSal);

        jmiViewModuleInv.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModuleInv.setText("Módulo inventarios");
        jmView.add(jmiViewModuleInv);

        jmiViewModuleMkt.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_6, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModuleMkt.setText("Módulo comercialización");
        jmiViewModuleMkt.setEnabled(false);
        jmView.add(jmiViewModuleMkt);

        jmiViewModuleLog.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_7, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModuleLog.setText("Módulo embarques");
        jmiViewModuleLog.setEnabled(false);
        jmView.add(jmiViewModuleLog);

        jmiViewModuleMfg.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_8, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModuleMfg.setText("Módulo producción");
        jmiViewModuleMfg.setEnabled(false);
        jmView.add(jmiViewModuleMfg);

        jmiViewModuleHrs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_9, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModuleHrs.setText("Módulo recursos humanos");
        jmiViewModuleHrs.setEnabled(false);
        jmView.add(jmiViewModuleHrs);

        jMenuBar.add(jmView);

        jmHelp.setText("Ayuda");

        jmiHelpAbout.setText("Acerca de...");
        jmHelp.add(jmiHelpAbout);

        jMenuBar.add(jmHelp);

        setJMenuBar(jMenuBar);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-1000)/2, (screenSize.height-725)/2, 1000, 725);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        windowClosing();
    }//GEN-LAST:event_formWindowClosing

    private void initComponentsCustom() {
        int result = SLibConsts.UNDEFINED;

        mbFirstActivation = true;
        mbLoggedIn = false;

        setExtendedState(Frame.MAXIMIZED_BOTH);
        TimeZone.setDefault(SLibTimeUtilities.SysTimeZone); // XXX Fix this!: time zone must be set by configuration parameter

        SBeanForm.OwnerFrame = this;
        SBeanFormDialog.OwnerFrame = this;
        SBeanFormProcess.OwnerFrame = this;
        SBeanOptionPicker.OwnerFrame = this;
        SBeanDialogReport.OwnerFrame = this;

        moIcon = new ImageIcon(getClass().getResource("/erp/img/logo21.gif"));
        moIconNew = new ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"));
        moIconNewMain = new ImageIcon(getClass().getResource("/erp/img/icon_std_new_main.gif"));
        moIconInsert = new ImageIcon(getClass().getResource("/erp/img/icon_std_insert.gif"));
        moIconCopy = new ImageIcon(getClass().getResource("/erp/img/icon_std_copy.gif"));
        moIconEdit = new ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"));
        moIconAnnul = new ImageIcon(getClass().getResource("/erp/img/icon_std_annul.gif"));
        moIconDelete = new ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"));
        moIconLook = new ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif"));
        moIconPrint = new ImageIcon(getClass().getResource("/erp/img/icon_std_print.gif"));
        moIconPrintAckCancel = new ImageIcon(getClass().getResource("/erp/img/icon_std_print_ack_can.gif"));
        moIconPrintOrder = new ImageIcon(getClass().getResource("/erp/img/icon_std_print_order.gif"));
        moIconPrintPhoto = new ImageIcon(getClass().getResource("/erp/img/icon_std_print_photo.gif"));
        moIconKardex = new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif"));
        moIconNotes = new ImageIcon(getClass().getResource("/erp/img/icon_std_notes.gif"));
        moIconDocAdd = new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_add.gif"));
        moIconDocRemove = new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_rem.gif"));
        moIconAction = new ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"));
        moIconLink = new ImageIcon(getClass().getResource("/erp/img/icon_std_link.gif"));
        moIconContractAnalysis = new ImageIcon(getClass().getResource("/erp/img/icon_std_contract_analysis.gif"));
        moIconSum = new ImageIcon(getClass().getResource("/erp/img/icon_std_sum.gif"));
        moIconDocLink = new ImageIcon(getClass().getResource("/erp/img/icon_std_dps_link.gif"));
        moIconDocLinkNo = new ImageIcon(getClass().getResource("/erp/img/icon_std_dps_link_rev.gif"));
        moIconDocSupply = new ImageIcon(getClass().getResource("/erp/img/icon_std_dps_supply.gif"));
        moIconDocSupplyNo = new ImageIcon(getClass().getResource("/erp/img/icon_std_dps_supply_rev.gif"));
        moIconDocType = new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_type.gif"));
        moIconDocImport = new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_import.gif"));
        moIconDocOpen = new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_open.gif"));
        moIconDocClose = new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_close.gif"));
        moIconDocXml = new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_xml.gif"));
        moIconDocXmlCancel = new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_xml_ack_can.gif"));
        moIconDocXmlSign = new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_xml_sign.gif"));
        moIconDocDelivery = new ImageIcon(getClass().getResource("/erp/img/icon_std_delivery.gif"));
        moIconFilterBp = new ImageIcon(getClass().getResource("/erp/img/icon_std_filter_bp.gif"));
        moIconFilterBpb = new ImageIcon(getClass().getResource("/erp/img/icon_std_filter_bpb.gif"));
        moIconFilterDoc = new ImageIcon(getClass().getResource("/erp/img/icon_std_filter_doc.gif"));
        moIconApprove = new ImageIcon(getClass().getResource("/erp/img/icon_std_thumbs_up.gif"));
        moIconApproveNo = new ImageIcon(getClass().getResource("/erp/img/icon_std_thumbs_down.gif"));
        moIconQueryBizPartner = new ImageIcon(getClass().getResource("/erp/img/icon_std_query_bp.gif"));
        moIconQueryDocument = new ImageIcon(getClass().getResource("/erp/img/icon_std_query_doc.gif"));
        moIconQueryRecord = new ImageIcon(getClass().getResource("/erp/img/icon_std_query_rec.gif"));
        moIconArrowUp = new ImageIcon(getClass().getResource("/erp/img/icon_std_move_up.gif"));
        moIconArrowDown = new ImageIcon(getClass().getResource("/erp/img/icon_std_move_down.gif"));
        moIconArrowLeft = new ImageIcon(getClass().getResource("/erp/img/icon_std_move_left.gif"));
        moIconArrowRight = new ImageIcon(getClass().getResource("/erp/img/icon_std_move_right.gif"));
        moIconBizPartnerExport = new ImageIcon(getClass().getResource("/erp/img/icon_std_bp_export.gif"));
        moIconModuleCfg = new ImageIcon(getClass().getResource("/erp/img/icon_mod_cfg.png"));
        moIconModuleFin = new ImageIcon(getClass().getResource("/erp/img/icon_mod_fin.png"));
        moIconModulePur = new ImageIcon(getClass().getResource("/erp/img/icon_mod_pur.png"));
        moIconModuleSal = new ImageIcon(getClass().getResource("/erp/img/icon_mod_sal.png"));
        moIconModuleInv = new ImageIcon(getClass().getResource("/erp/img/icon_mod_inv.png"));
        moIconModuleMkt = new ImageIcon(getClass().getResource("/erp/img/icon_mod_mkt.png"));
        moIconModuleLog = new ImageIcon(getClass().getResource("/erp/img/icon_mod_log.png"));
        moIconModuleMfg = new ImageIcon(getClass().getResource("/erp/img/icon_mod_mfg.png"));
        moIconModuleHrs = new ImageIcon(getClass().getResource("/erp/img/icon_mod_hrs.png"));
        moIconCloseActive = new ImageIcon(getClass().getResource("/sa/lib/img/gui_close.png"));
        moIconCloseInactive = new ImageIcon(getClass().getResource("/sa/lib/img/gui_close_ina.png"));
        moIconCloseBright = new ImageIcon(getClass().getResource("/sa/lib/img/gui_close_bri.png"));
        moIconCloseDark = new ImageIcon(getClass().getResource("/sa/lib/img/gui_close_dar.png"));

        moParamsApp = new SParamsApp();
        if (!moParamsApp.read()) {
            System.exit(-1);    // there is no way of connecting to an ERP Server
        }

        moLogin = new SLogin(this);

        msCompany = "";
        moDatePicker = new sa.lib.gui.SGuiDatePicker(this, SGuiConsts.DATE_PICKER_DATE);
        moDateRangePicker = new sa.lib.gui.SGuiDateRangePicker(this);
        moYearPicker = new SGuiYearPicker(this);
        moYearMonthPicker = new SGuiYearMonthPicker(this);
        moFileChooser = new JFileChooser();

        setIconImage(moIcon.getImage());
        
        // Prepare SIIE client own DB connection:

        moSysDatabase = new SDbDatabase(SDbConsts.DBMS_MYSQL);
        result = moSysDatabase.connect(moParamsApp.getDatabaseHostClt(), moParamsApp.getDatabasePortClt(),
                moParamsApp.getDatabaseName(), moParamsApp.getDatabaseUser(), moParamsApp.getDatabasePswd());

        if (result != SDbConsts.CONNECTION_OK) {
            showMsgBoxError(SDbConsts.ERR_MSG_DB_CONNECTION);
            System.exit(-1);    // there is no way of connecting to an ERP Server
        }
        else {
            moSysDatabaseMonitor = new SDbDatabaseMonitor(moSysDatabase);
            moSysDatabaseMonitor.startThread();

            try {
                miSysStatement = moSysDatabase.getConnection().createStatement();
            }
            catch (SQLException e) {
                showMsgBoxError(e.toString());
                System.exit(-1);    // there is no way of connecting to an ERP Server
            }
        }
        
        // Get XML configuration:
        
        try {
            moXmlConfig = new SXmlConfig(SXmlModConsts.CONFIG_VER);
            moXmlConfig.parseConfig(SXmlUtils.readXml("config_gui1.xml"));
        }
        catch (Exception e) {
            moXmlConfig = null;
            SLibUtils.printException(this, e);
        }
        
        // Prepare GUI:

        logout();

        jtbModuleCfg.addActionListener(this);
        jtbModuleFin.addActionListener(this);
        jtbModulePur.addActionListener(this);
        jtbModuleSal.addActionListener(this);
        jtbModuleInv.addActionListener(this);
        jtbModuleMkt.addActionListener(this);
        jtbModuleLog.addActionListener(this);
        jtbModuleMfg.addActionListener(this);
        jtbModuleHrs.addActionListener(this);
        jmiFileCurrentDate.addActionListener(this);
        jmiFileSession.addActionListener(this);
        jmiFilePassword.addActionListener(this);
        jmiFileCloseViews.addActionListener(this);
        jmiFileCloseSession.addActionListener(this);
        jmiFileClose.addActionListener(this);
        jmiViewModuleCfg.addActionListener(this);
        jmiViewModuleFin.addActionListener(this);
        jmiViewModulePur.addActionListener(this);
        jmiViewModuleSal.addActionListener(this);
        jmiViewModuleInv.addActionListener(this);
        jmiViewModuleMkt.addActionListener(this);
        jmiViewModuleLog.addActionListener(this);
        jmiViewModuleMfg.addActionListener(this);
        jmiViewModuleHrs.addActionListener(this);
        jmiHelpAbout.addActionListener(this);
        jbCurrentDate.addActionListener(this);
        jbSession.addActionListener(this);

        jlAppRelease.setText(APP_RELEASE);

        SFormUtilities.createActionMap(getRootPane(), this, "closeCurrentTab", "closeCurrentTab", KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
    }

    private void windowActivated() {
        if (mbFirstActivation) {
            mbFirstActivation = false;
            login();
        }
    }

    private void windowClosing() {
        if (mbLoggedIn) {
            logout();
        }
    }

    private ArrayList<SSrvCompany> readCompanies() {
        String sql = "";
        ResultSet resultSet = null;
        ArrayList<SSrvCompany> companies = new ArrayList<SSrvCompany>();

        try {
            sql = "SELECT id_co, co FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_CO) + " WHERE b_del = 0 ORDER BY co, id_co ";
            resultSet = miSysStatement.executeQuery(sql);
            while (resultSet.next()) {
                companies.add(new SSrvCompany(resultSet.getInt(1), resultSet.getString(2)));
            }
        }
        catch (SQLException e) {
            SLibUtils.showException(this, e);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }

        return companies;
    }

    private void destroyCfdSignatures() {
        moCfdSignature = null;
        mvCfdSignatures = new Vector<>();
    }

    private void createCfdSignatures() {
        DCfdSignature cfdSignature = null;
        SDataCertificate companyCertificate = null;

        moCfdSignature = null;
        mvCfdSignatures = new Vector<>();

        try {
            companyCertificate = moSessionXXX.getParamsCompany().getDbmsDataCertificate_n();

            if (companyCertificate != null && companyCertificate.getExtraPrivateKeyBytes_n() != null && companyCertificate.getExtraPublicKeyBytes_n() != null) {
                moCfdSignature = new DCfdSignature(companyCertificate.getExtraPrivateKeyBytes_n(), companyCertificate.getExtraPublicKeyBytes_n(), companyCertificate.getNumber());
                moCfdSignature.setDate(companyCertificate.getDate());
                moCfdSignature.setExpirationDate(companyCertificate.getExpirationDate());

                for (SDataCertificate certificate : moSessionXXX.getDbmsCertificates()) {
                    if (certificate != null && certificate.getExtraPrivateKeyBytes_n() != null && certificate.getExtraPublicKeyBytes_n() != null) {
                        cfdSignature = new DCfdSignature(certificate.getExtraPrivateKeyBytes_n(), certificate.getExtraPublicKeyBytes_n(), certificate.getNumber());
                        cfdSignature.setDate(certificate.getDate());
                        cfdSignature.setExpirationDate(certificate.getExpirationDate());
                        mvCfdSignatures.add(cfdSignature);
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private void prepareGui() {
        if (!mbLoggedIn) {
            msCompany = "";
            setTitle(APP_NAME);
            destroyCfdSignatures();
            moGuiDatePicker = null;
            moGuiDatePeriodPicker = null;
            moGuiDateRangePicker = null;

            jtfSystemDate.setText("");
            jtfCurrentDate.setText("");
            jtfUser.setText("");
            jtfLoginCompanyBranch.setText("");
            jtfLoginEntityCh.setText("");
            jtfLoginEntityWh.setText("");
            jtfLoginEntityPlt.setText("");
            jtfLoginEntityPos.setText("");
            jtfLoginTimestamp.setText("");
            jbCurrentDate.setEnabled(false);
            jbSession.setEnabled(false);

            jmiViewModuleCfg.setEnabled(false);
            jmiViewModuleFin.setEnabled(false);
            jmiViewModulePur.setEnabled(false);
            jmiViewModuleSal.setEnabled(false);
            jmiViewModuleInv.setEnabled(false);
            jmiViewModuleMkt.setEnabled(false);
            jmiViewModuleLog.setEnabled(false);
            jmiViewModuleMfg.setEnabled(false);
            jmiViewModuleHrs.setEnabled(false);

            jButtonGroup.clearSelection();
            jtbModuleCfg.setEnabled(false);
            jtbModuleFin.setEnabled(false);
            jtbModulePur.setEnabled(false);
            jtbModuleSal.setEnabled(false);
            jtbModuleInv.setEnabled(false);
            jtbModuleMkt.setEnabled(false);
            jtbModuleLog.setEnabled(false);
            jtbModuleMfg.setEnabled(false);
            jtbModuleHrs.setEnabled(false);

            jlLogo.setIcon(null);

            renderMenues(null);
        }
        else {
            msCompany = moSessionXXX.getCompany().getCompany();
            setTitle(APP_NAME + " - " + msCompany);

            moGuiDatePicker = new SGuiDatePicker(this, SLibConstants.GUI_DATE_PICKER_DATE);
            moGuiDatePeriodPicker = new SGuiDatePicker(this, SLibConstants.GUI_DATE_PICKER_DATE_PERIOD);
            moGuiDateRangePicker = new SGuiDateRangePicker(this);
            createCfdSignatures();
            moGuiDatePicker = new SGuiDatePicker(this, SLibConstants.GUI_DATE_PICKER_DATE);
            moGuiDatePeriodPicker = new SGuiDatePicker(this, SLibConstants.GUI_DATE_PICKER_DATE_PERIOD);
            moGuiDateRangePicker = new SGuiDateRangePicker(this);

            jtfSystemDate.setText(moSessionXXX.getFormatters().getDateFormat().format(moSessionXXX.getSystemDate()));
            jtfCurrentDate.setText(moSessionXXX.getFormatters().getDateFormat().format(moSessionXXX.getWorkingDate()));
            jtfUser.setText(moSessionXXX.getUser().getUser());
            jtfLoginCompanyBranch.setText("");
            jtfLoginEntityCh.setText("");
            jtfLoginEntityWh.setText("");
            jtfLoginEntityPlt.setText("");
            jtfLoginEntityPos.setText("");
            jtfLoginTimestamp.setText(moSessionXXX.getFormatters().getDatetimeZoneFormat().format(moSessionXXX.getSystemDate()));
            jbCurrentDate.setEnabled(true);
            jbSession.setEnabled(true);

            if (moXmlConfig != null) {
                moCfgProcessor = new SCfgProcessor(moXmlConfig, moSessionXXX.getCompany().getPkCompanyId(), moSessionXXX.getUser().getPkUserId());
            }

            if (moSessionXXX.getUser().hasAccessToModule(SDataConstants.MOD_CFG, moSessionXXX.getCurrentCompany().getPkCompanyId()) && 
                    (moCfgProcessor == null || moCfgProcessor.isModuleVisible("" + SDataConstants.MOD_CFG))) {
                jmiViewModuleCfg.setEnabled(true);
                jtbModuleCfg.setEnabled(true);
            }

            if (moSessionXXX.getUser().hasAccessToModule(SDataConstants.MOD_FIN, moSessionXXX.getCurrentCompany().getPkCompanyId()) && 
                    (moCfgProcessor == null || moCfgProcessor.isModuleVisible("" + SDataConstants.MOD_FIN))) {
                jmiViewModuleFin.setEnabled(true);
                jtbModuleFin.setEnabled(true);
            }

            if (moSessionXXX.getUser().hasAccessToModule(SDataConstants.MOD_PUR, moSessionXXX.getCurrentCompany().getPkCompanyId()) && 
                    (moCfgProcessor == null || moCfgProcessor.isModuleVisible("" + SDataConstants.MOD_PUR))) {
                jmiViewModulePur.setEnabled(true);
                jtbModulePur.setEnabled(true);
            }

            if (moSessionXXX.getUser().hasAccessToModule(SDataConstants.MOD_SAL, moSessionXXX.getCurrentCompany().getPkCompanyId()) && 
                    (moCfgProcessor == null || moCfgProcessor.isModuleVisible("" + SDataConstants.MOD_SAL))) {
                jmiViewModuleSal.setEnabled(true);
                jtbModuleSal.setEnabled(true);
            }

            if (moSessionXXX.getUser().hasAccessToModule(SDataConstants.MOD_INV, moSessionXXX.getCurrentCompany().getPkCompanyId()) && 
                    (moCfgProcessor == null || moCfgProcessor.isModuleVisible("" + SDataConstants.MOD_INV))) {
                jmiViewModuleInv.setEnabled(true);
                jtbModuleInv.setEnabled(true);
            }

            if (moSessionXXX.getUser().hasAccessToModule(SDataConstants.MOD_MKT, moSessionXXX.getCurrentCompany().getPkCompanyId()) && 
                    (moCfgProcessor == null || moCfgProcessor.isModuleVisible("" + SDataConstants.MOD_MKT))) {
                jmiViewModuleMkt.setEnabled(true);
                jtbModuleMkt.setEnabled(true);
            }

            if (moSessionXXX.getUser().hasAccessToModule(SDataConstants.MOD_LOG, moSessionXXX.getCurrentCompany().getPkCompanyId()) && 
                    (moCfgProcessor == null || moCfgProcessor.isModuleVisible("" + SDataConstants.MOD_LOG))) {
                jmiViewModuleLog.setEnabled(true);
                jtbModuleLog.setEnabled(true);
            }

            if (moSessionXXX.getUser().hasAccessToModule(SDataConstants.MOD_MFG, moSessionXXX.getCurrentCompany().getPkCompanyId()) && 
                    (moCfgProcessor == null || moCfgProcessor.isModuleVisible("" + SDataConstants.MOD_MFG))) {
                jmiViewModuleMfg.setEnabled(true);
                jtbModuleMfg.setEnabled(true);
            }

            if (moSessionXXX.getUser().hasAccessToModule(SDataConstants.MOD_HRS, moSessionXXX.getCurrentCompany().getPkCompanyId()) && 
                    (moCfgProcessor == null || moCfgProcessor.isModuleVisible("" + SDataConstants.MOD_HRS))) {
                jmiViewModuleHrs.setEnabled(true);
                jtbModuleHrs.setEnabled(true);
            }

            jlLogo.setIcon(moSessionXXX.getParamsCompany().getExtraLogoImageIcon_n());
        }
    }

    private void createSession() {
        SSessionCustom sessionCustom = null;

        // XXX Improve this!: Session 2.1 must be removed completely.

        SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);

        database.connect(
                moParamsApp.getDatabaseHostClt(),
                moParamsApp.getDatabasePortClt(),
                moSessionXXX.getCompany().getDatabase(),
                moParamsApp.getDatabaseUser(),
                moParamsApp.getDatabasePswd());

        moSession = new SGuiSession(this);

        moSession.setUser(moSessionXXX.getUser());                      // XXX this must be replaced

        moSession.setSystemDate(moSessionXXX.getSystemDate());
        moSession.setCurrentDate(moSessionXXX.getWorkingDate());
        moSession.setUserTs(moSessionXXX.getSystemDate());
        moSession.setDatabase(database);
    
        moSession.setConfigSystem(moSessionXXX.getParamsErp());         // XXX this must be replaced
        moSession.setConfigCompany(moSessionXXX.getParamsCompany());    // XXX this must be replaced
        moSession.setConfigBranch(null);
        moSession.setEdsSignature(null);
        moSession.setSessionServerSide(moSessionXXX.getSessionServer());
        moSession.setModuleUtils(new SModUtils());
        moSession.getModules().add(new SModuleCfg(moSession.getClient()));
        moSession.getModules().add(new SModuleUsr(moSession.getClient()));
        moSession.getModules().add(new SModuleBps(moSession.getClient()));
        moSession.getModules().add(new SModuleItm(moSession.getClient()));
        moSession.getModules().add(new SModuleFin(moSession.getClient()));
        moSession.getModules().add(new SModuleHrs(moSession.getClient()));
        moSession.getModules().add(new SModuleTrn(moSession.getClient(), SModConsts.MOD_TRN_PUR_N));
        moSession.getModules().add(new SModuleTrn(moSession.getClient(), SModConsts.MOD_TRN_SAL_N));
        moSession.getModules().add(new SModuleTrn(moSession.getClient(), SModConsts.MOD_TRN_INV_N));
        moSession.getModules().add(new SModuleLog(moSession.getClient(), this));
        moSession.getModules().add(new SModuleMkt(moSession.getClient()));

        /*
        moSession.getModules().add(new SModuleCfg(moSession));
        moSession.getModules().add(new SModuleMod1(moSession));
        moSession.getModules().add(new SModuleMod2(moSession));
        moSession.getModules().add(new SModuleMod3(moSession));
        */

        //user.computeAccess(moSession);    // not implemented yet!
        //moSession.setSessionCustom(user.createDefaultUserSession(this, mnTerminal));

        /*
        company = new SDbCompany();
        company.read(moSession, new int[] { SUtilConsts.BPR_CO_ID });

        ((SGuiClientSession) moSession.getSessionCustom()).setCompany(company);
        ((SGuiClientSession) moSession.getSessionCustom()).setLocalCurrencyCode(company.getCurrencyCode());
        */

        // Read privilege and module access by user:

        moSessionXXX.getUser().readPrivilegeUser(moSession);

        if (moSession.getUser().hasModuleAccess(SModConsts.MOD_CFG_N)) {
        }
        if (moSession.getUser().hasModuleAccess(SModConsts.MOD_USR_N)) {
        }
        if (moSession.getUser().hasModuleAccess(SModConsts.MOD_BPS_N)) {
        }
        if (moSession.getUser().hasModuleAccess(SModConsts.MOD_ITM_N)) {
        }
        if (moSession.getUser().hasModuleAccess(SModConsts.MOD_FIN_N)) {
        }
        if (moSession.getUser().hasModuleAccess(SModConsts.MOD_TRN_N)) {
        }
        if (moSession.getUser().hasModuleAccess(SModConsts.MOD_MKT_N)) {
        }
        if (moSession.getUser().hasModuleAccess(SModConsts.MOD_LOG_N)) {
        }
        if (moSession.getUser().hasModuleAccess(SModConsts.MOD_MFG_N)) {
        }
        if (moSession.getUser().hasModuleAccess(SModConsts.MOD_HRS_N)) {

            /* Functionality not yet implemented (sflores, 2015-09-28)
            modulesAccessed++;
            jtbModuleSomRm.setEnabled(true);
            if (defaultToggleButton == null) {
                defaultToggleButton = jtbModuleSomRm;
            }
            */
        }

        /* Functionality not yet implemented (sflores, 2015-09-28)
        if (defaultToggleButton != null) {
            defaultToggleButton.requestFocus();
        }

        if (modulesAccessed == 1) {
            defaultToggleButton.doClick();
        }
        */

        sessionCustom = new SSessionCustom(moSession);
        sessionCustom.setCurrentCompanyKey((int[]) moSessionXXX.getCompany().getPrimaryKey());
        sessionCustom.updateSessionSettings();
        moSession.setSessionCustom(sessionCustom); // client database must be set already
    }

    private void logout() {
        Cursor cursor = getCursor();

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            actionFileCloseViews();

            if (mbLoggedIn) {
                mbLoggedIn = false;

                try {
                    moServer.logout(moSessionXXX.getSessionId());
                }
                catch (RemoteException e) {
                    SLibUtils.showException(this, e);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }

                try {
                    moSession.getStatement().close();
                    moSession.getDatabase().getConnection().close();
                }
                catch (SQLException e) {
                    SLibUtils.showException(this, e);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }

            moServer = null;
            moSessionXXX = null;
            moSession = null;
            moCfgProcessor = null;
            moGlobalCataloguesUsr = null;
            moGlobalCataloguesLoc = null;
            moGlobalCataloguesBps = null;
            moGlobalCataloguesItm = null;
            moGlobalCataloguesTrn = null;
            moModuleCfg = null;
            moModuleFin = null;
            moModulePur = null;
            moModuleSal = null;
            moModuleInv = null;
            moModuleMkt = null;
            moModuleLog = null;
            moModuleMfg = null;
            moModuleHrs = null;

            prepareGui();
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            setCursor(cursor);
        }
    }

    private void login() {
        boolean lookup = false;
        Cursor cursor = getCursor();
        SLoginResponse response = null;

        mbLoggedIn = false;

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            moServer = (SServerRemote) Naming.lookup("//" + moParamsApp.getErpHost() + ":" + moParamsApp.getErpRmiRegistryPort() + "/" + moParamsApp.getErpInstance());
            lookup = true;

            moLogin.setCompanies(readCompanies());

            while (!mbLoggedIn) {
                moLogin.reset();
                moLogin.setVisible(true);
                if (moLogin.getFormResult() != SLibConstants.FORM_RESULT_OK) {
                    break;
                }
                else {
                    SLoginRequest request = moLogin.getLoginRequest();
                    response = moServer.login(request);

                    switch (response.getResponseType()) {
                        case SLibConstants.LOGIN_ERROR_USR_INVALID:
                            showMsgBoxWarning(SLibConstants.MSG_ERR_LOGIN_USR_UNKNOWN);
                            break;
                        case SLibConstants.LOGIN_ERROR_USR_INACTIVE:
                            showMsgBoxWarning(SLibConstants.MSG_ERR_LOGIN_USR_INACTIVE);
                            break;
                        case SLibConstants.LOGIN_ERROR_USR_DELETED:
                            showMsgBoxWarning(SLibConstants.MSG_ERR_LOGIN_USR_DELETED);
                            break;
                        case SLibConstants.LOGIN_ERROR_USR_PSWD_INVALID:
                            showMsgBoxWarning(SLibConstants.MSG_ERR_LOGIN_USR_PSWD);
                            break;
                        case SLibConstants.LOGIN_ERROR_USR_CO_INVALID:
                            showMsgBoxWarning(SLibConstants.MSG_ERR_LOGIN_USR_CO);
                            break;
                        case SLibConstants.LOGIN_OK:
                            mbLoggedIn = true;
                            moSessionXXX = response.getSession();
                            moSessionXXX.getFormatters().redefineTableCellRenderers();
                            prepareGui();
                            createSession();
                            actionFileSession(true);
                            break;
                        default:
                            showMsgBoxWarning(SLibConstants.MSG_ERR_LOGIN_UNKNOWN);
                            break;
                    }
                }
            }
        }
        catch (NotBoundException e) {
            SLibUtils.showException(this, e);
        }
        catch (MalformedURLException e) {
            SLibUtils.showException(this, e);
        }
        catch (RemoteException e) {
            SLibUtils.showException(this, e);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            setCursor(cursor);

            if (!lookup) {
                showMsgBoxWarning("No se pudo establecer conexión con el ERP Server: '" + moParamsApp.getErpInstance() + "',\nen el host: '" + moParamsApp.getErpHost() + "'.");
                System.exit(-1);
            }

            if (!mbLoggedIn) {
                System.exit(-1);
            }
            else {
                moGlobalCataloguesUsr = new SGuiGlobalCataloguesUsr(this);
                moGlobalCataloguesLoc = new SGuiGlobalCataloguesLoc(this);
                moGlobalCataloguesBps = new SGuiGlobalCataloguesBps(this);
                moGlobalCataloguesItm = new SGuiGlobalCataloguesItm(this);
                moGlobalCataloguesTrn = new SGuiGlobalCataloguesTrn(this);
                moModuleCfg = new SGuiModuleCfg(this);
                moModuleFin = new SGuiModuleFin(this);
                moModulePur = new SGuiModuleTrnPur(this);
                moModuleSal = new SGuiModuleTrnSal(this);
                moModuleInv = new SGuiModuleTrnInv(this);
                moModuleMkt = new SGuiModuleMkt(this);
                moModuleLog = new SGuiModuleLog(this);
                moModuleMfg = new SGuiModuleMfg(this);
                moModuleHrs = new SGuiModuleHrs(this);
            }
        }
    }

    private void renderMenues(javax.swing.JMenu[] menues) {
        jMenuBar.removeAll();
        validate();

        jMenuBar.add(jmFile);
        jMenuBar.add(jmView);

        if (menues != null) {
            for (int i = 0; i < menues.length; i++) {
                jMenuBar.add(menues[i]);
            }
        }

        jMenuBar.add(jmHelp);
        validate();
    }

    private void actionModuleCfg() {
        jtbModuleCfg.setSelected(true);
        renderMenues(moModuleCfg.getMenues());
    }

    private void actionModuleFin() {
        jtbModuleFin.setSelected(true);
        renderMenues(moModuleFin.getMenues());
    }

    private void actionModulePur() {
        jtbModulePur.setSelected(true);
        renderMenues(moModulePur.getMenues());
    }

    private void actionModuleSal() {
        jtbModuleSal.setSelected(true);
        renderMenues(moModuleSal.getMenues());
    }

    private void actionModuleInv() {
        jtbModuleInv.setSelected(true);
        renderMenues(moModuleInv.getMenues());
    }

    private void actionModuleMkt() {
        jtbModuleMkt.setSelected(true);
        renderMenues(moModuleMkt.getMenues());
    }

    private void actionModuleLog() {
        jtbModuleLog.setSelected(true);
        renderMenues(moModuleLog.getMenues());
    }

    private void actionModuleMfg() {
        jtbModuleMfg.setSelected(true);
        renderMenues(moModuleMfg.getMenues());
    }

    private void actionModuleHrs() {
        jtbModuleHrs.setSelected(true);
        renderMenues(moModuleHrs.getMenues());
    }

    private void actionFileCurrentDate() {
        moGuiDatePicker.formReset();
        moGuiDatePicker.setDate(moSession.getCurrentDate());
        moGuiDatePicker.setVisible(true);

        if (moGuiDatePicker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moSession.setCurrentDate(moGuiDatePicker.getGuiDate());
            moSessionXXX.setWorkingDate(moGuiDatePicker.getGuiDate());
            jtfCurrentDate.setText(moSessionXXX.getFormatters().getDateFormat().format(moSession.getCurrentDate()));
        }
    }

    private void actionFileSession(boolean onLogin) {
        moLoginSession = new SLoginSession(this);   // login session dialog needs an established connection
        moLoginSession.reset();
        moLoginSession.setVisible(true);

        if (moLoginSession.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            if (onLogin || moSessionXXX.getCurrentCompanyBranchId() != moLoginSession.getCurrentCompanyBranchId()) {   // when default company branch is defined, it is allready set in user session on login
                moSessionXXX.setCurrentCompanyBranchId(moLoginSession.getCurrentCompanyBranchId());
                jtfLoginCompanyBranch.setText(moLoginSession.getCurrentCompanyBranchId() == SLibConstants.UNDEFINED ? "" : moSessionXXX.getCurrentCompanyBranchName() + " [" + moSessionXXX.getCurrentCompanyBranchCode() + "]");
                jtfLoginCompanyBranch.setCaretPosition(0);

                ((SSessionCustom) moSession.getSessionCustom()).setCurrentBranchKey(new int[] { moLoginSession.getCurrentCompanyBranchId() });
            }

            jtfLoginEntityCh.setText("");
            jtfLoginEntityWh.setText("");
            jtfLoginEntityPos.setText("");
            jtfLoginEntityPlt.setText("");

            moSessionXXX.getCurrentCompanyBranchEntities().clear();

            if (moSessionXXX.getCurrentCompanyBranchId() != SLibConstants.UNDEFINED) {
                if (moLoginSession.getCurrentCompanyBranchEntityId(SDataConstantsSys.CFGS_CT_ENT_CASH) != null) {
                    moSessionXXX.setCurrentCompanyBranchEntityKey(SDataConstantsSys.CFGS_CT_ENT_CASH,
                            moLoginSession.getCurrentCompanyBranchEntityId(SDataConstantsSys.CFGS_CT_ENT_CASH));
                    jtfLoginEntityCh.setText(moSessionXXX.getCurrentCompanyBranchEntityCode(SDataConstantsSys.CFGS_CT_ENT_CASH));
                    jtfLoginEntityCh.setCaretPosition(0);

                    ((SSessionCustom) moSession.getSessionCustom()).setCurrentCashKey(moLoginSession.getCurrentCompanyBranchEntityId(SDataConstantsSys.CFGS_CT_ENT_CASH));
                }
                if (moLoginSession.getCurrentCompanyBranchEntityId(SDataConstantsSys.CFGS_CT_ENT_WH) != null) {
                    moSessionXXX.setCurrentCompanyBranchEntityKey(SDataConstantsSys.CFGS_CT_ENT_WH,
                            moLoginSession.getCurrentCompanyBranchEntityId(SDataConstantsSys.CFGS_CT_ENT_WH));
                    jtfLoginEntityWh.setText(moSessionXXX.getCurrentCompanyBranchEntityCode(SDataConstantsSys.CFGS_CT_ENT_WH));
                    jtfLoginEntityWh.setCaretPosition(0);

                    ((SSessionCustom) moSession.getSessionCustom()).setCurrentWarehouseKey(moLoginSession.getCurrentCompanyBranchEntityId(SDataConstantsSys.CFGS_CT_ENT_WH));
                }
                if (moLoginSession.getCurrentCompanyBranchEntityId(SDataConstantsSys.CFGS_CT_ENT_POS) != null) {
                    moSessionXXX.setCurrentCompanyBranchEntityKey(SDataConstantsSys.CFGS_CT_ENT_POS,
                            moLoginSession.getCurrentCompanyBranchEntityId(SDataConstantsSys.CFGS_CT_ENT_POS));
                    jtfLoginEntityPos.setText(moSessionXXX.getCurrentCompanyBranchEntityCode(SDataConstantsSys.CFGS_CT_ENT_POS));
                    jtfLoginEntityPos.setCaretPosition(0);
                }
                if (moLoginSession.getCurrentCompanyBranchEntityId(SDataConstantsSys.CFGS_CT_ENT_PLANT) != null) {
                    moSessionXXX.setCurrentCompanyBranchEntityKey(SDataConstantsSys.CFGS_CT_ENT_PLANT,
                            moLoginSession.getCurrentCompanyBranchEntityId(SDataConstantsSys.CFGS_CT_ENT_PLANT));
                    jtfLoginEntityPlt.setText(moSessionXXX.getCurrentCompanyBranchEntityCode(SDataConstantsSys.CFGS_CT_ENT_PLANT));
                    jtfLoginEntityPlt.setCaretPosition(0);

                    ((SSessionCustom) moSession.getSessionCustom()).setCurrentPlantKey(moLoginSession.getCurrentCompanyBranchEntityId(SDataConstantsSys.CFGS_CT_ENT_PLANT));
                }
            }

            moSession.setCurrentDate(moLoginSession.getWorkingDate());
            moSessionXXX.setWorkingDate(moLoginSession.getWorkingDate());
            jtfCurrentDate.setText(moSessionXXX.getFormatters().getDateFormat().format(moSession.getCurrentDate()));
        }
        else if (onLogin) {
            actionFileCloseSession();
        }
    }

    private void actionFilePassword() {
        Vector<Object> params = new Vector<Object>();
        SUserPassword userPassword = new SUserPassword(this);

        while (true) {
            userPassword.reset();
            userPassword.setVisible(true);

            if (userPassword.getFormResult() != SLibConstants.FORM_RESULT_OK) {
                break;
            }
            else {
                params.clear();
                params.add(moSessionXXX.getUser().getUser());
                params.add(userPassword.getPassword());
                params.add(userPassword.getPasswordNew());
                params.add(moSessionXXX.getUser().getPkUserId());
                params = SDataUtilities.callProcedure(this, SProcConstants.USRU_USR_PSWD, params, SLibConstants.EXEC_MODE_VERBOSE);
                if ((Integer) params.get(0) == 0) {
                    break;
                }
                else {
                    showMsgBoxWarning((String) params.get(1));
                }
            }
        }
    }

    private void actionFileCloseViews() {
        jTabbedPane.removeAll();
    }

    private void actionFileCloseSession() {
        logout();
        login();
    }

    private void actionFileClose() {
        logout();
        System.exit(0);
    }

    private void actionHelpAbout() {
        new SDialogHelpAbout(this).setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (java.lang.Exception e) {
            SLibUtils.showException(SClient.class.getName(), e);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SClient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup jButtonGroup;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton jbCurrentDate;
    private javax.swing.JButton jbSession;
    private javax.swing.JLabel jlAppRelease;
    private javax.swing.JLabel jlLogo;
    private javax.swing.JMenu jmFile;
    private javax.swing.JMenu jmHelp;
    private javax.swing.JMenu jmView;
    private javax.swing.JMenuItem jmiFileClose;
    private javax.swing.JMenuItem jmiFileCloseSession;
    private javax.swing.JMenuItem jmiFileCloseViews;
    private javax.swing.JMenuItem jmiFileCurrentDate;
    private javax.swing.JMenuItem jmiFilePassword;
    private javax.swing.JMenuItem jmiFileSession;
    private javax.swing.JMenuItem jmiHelpAbout;
    private javax.swing.JMenuItem jmiViewModuleCfg;
    private javax.swing.JMenuItem jmiViewModuleFin;
    private javax.swing.JMenuItem jmiViewModuleHrs;
    private javax.swing.JMenuItem jmiViewModuleInv;
    private javax.swing.JMenuItem jmiViewModuleLog;
    private javax.swing.JMenuItem jmiViewModuleMfg;
    private javax.swing.JMenuItem jmiViewModuleMkt;
    private javax.swing.JMenuItem jmiViewModulePur;
    private javax.swing.JMenuItem jmiViewModuleSal;
    private javax.swing.JPanel jpStatus;
    private javax.swing.JPanel jpToolBars;
    private javax.swing.JSeparator jsFile01;
    private javax.swing.JSeparator jsFile02;
    private javax.swing.JToggleButton jtbModuleCfg;
    private javax.swing.JToggleButton jtbModuleFin;
    private javax.swing.JToggleButton jtbModuleHrs;
    private javax.swing.JToggleButton jtbModuleInv;
    private javax.swing.JToggleButton jtbModuleLog;
    private javax.swing.JToggleButton jtbModuleMfg;
    private javax.swing.JToggleButton jtbModuleMkt;
    private javax.swing.JToggleButton jtbModulePur;
    private javax.swing.JToggleButton jtbModuleSal;
    private javax.swing.JTextField jtfCurrentDate;
    private javax.swing.JTextField jtfLoginCompanyBranch;
    private javax.swing.JTextField jtfLoginEntityCh;
    private javax.swing.JTextField jtfLoginEntityPlt;
    private javax.swing.JTextField jtfLoginEntityPos;
    private javax.swing.JTextField jtfLoginEntityWh;
    private javax.swing.JTextField jtfLoginTimestamp;
    private javax.swing.JTextField jtfSystemDate;
    private javax.swing.JTextField jtfUser;
    // End of variables declaration//GEN-END:variables

    public void closeCurrentTab() {
        if (jTabbedPane.getSelectedIndex() != -1) {
            jTabbedPane.remove(jTabbedPane.getSelectedIndex());
        }
    }
    
    public SXmlConfig getXmlConfig() {
        return moXmlConfig;
    }

    public SCfgProcessor getCfgProcesor() {
        return moCfgProcessor;
    }

    @Override
    public SSessionXXX getSessionXXX() {
        return moSessionXXX;
    }

    @Override
    public erp.lib.gui.SGuiModule getGuiModule(int moduleType) {
        erp.lib.gui.SGuiModule module = null;

        switch (moduleType) {
            case SDataConstants.GLOBAL_CAT_USR:
                module = moGlobalCataloguesUsr;
                break;
            case SDataConstants.GLOBAL_CAT_LOC:
                module = moGlobalCataloguesLoc;
                break;
            case SDataConstants.GLOBAL_CAT_BPS:
                module = moGlobalCataloguesBps;
                break;
            case SDataConstants.GLOBAL_CAT_ITM:
                module = moGlobalCataloguesItm;
                break;
            case SDataConstants.MOD_CFG:
                module = moModuleCfg;
                break;
            case SDataConstants.MOD_FIN:
                module = moModuleFin;
                break;
            case SDataConstants.MOD_PUR:
                module = moModulePur;
                break;
            case SDataConstants.MOD_SAL:
                module = moModuleSal;
                break;
            case SDataConstants.MOD_INV:
                module = moModuleInv;
                break;
            case SDataConstants.MOD_MKT:
                module = moModuleMkt;
                break;
            case SDataConstants.MOD_LOG:
                module = moModuleLog;
                break;
            case SDataConstants.MOD_MFG:
                module = moModuleMfg;
                break;
            case SDataConstants.MOD_HRS:
                module = moModuleHrs;
                break;
            default:
        }

        return module;
    }

    @Override
    public erp.lib.gui.SGuiDatePicker getGuiDatePickerXXX() {
        return moGuiDatePicker;
    }

    @Override
    public erp.lib.gui.SGuiDatePicker getGuiDatePeriodPickerXXX() {
        return moGuiDatePeriodPicker;
    }

    @Override
    public erp.lib.gui.SGuiDateRangePicker getGuiDateRangePickerXXX() {
        return moGuiDateRangePicker;
    }

    @Override
    public erp.lib.form.SFormOptionPickerInterface getOptionPicker(int optionType) {
        SFormOptionPickerInterface picker = null;

        if (SDataUtilities.isCatalogueCfg(optionType)) {
            picker = moModuleCfg.getOptionPicker(optionType);
        }
        else if (SDataUtilities.isCatalogueUsr(optionType)) {
            picker = moGlobalCataloguesUsr.getOptionPicker(optionType);
        }
        else if (SDataUtilities.isCatalogueLoc(optionType)) {
            picker = moGlobalCataloguesLoc.getOptionPicker(optionType);
        }
        else if (SDataUtilities.isCatalogueBps(optionType)) {
            picker = moGlobalCataloguesBps.getOptionPicker(optionType);
        }
        else if (SDataUtilities.isCatalogueItm(optionType)) {
            picker = moGlobalCataloguesItm.getOptionPicker(optionType);
        }
        else if (SDataUtilities.isCatalogueTrn(optionType)) {
            picker = moGlobalCataloguesTrn.getOptionPicker(optionType);
        }
        else if (SDataUtilities.isCatalogueFin(optionType)) {
            picker = moModuleFin.getOptionPicker(optionType);
        }
        else if (SDataUtilities.isCatalogueTrnPur(optionType)) {
            picker = moModulePur.getOptionPicker(optionType);
        }
        else if (SDataUtilities.isCatalogueTrnSal(optionType)) {
            picker = moModuleSal.getOptionPicker(optionType);
        }
        else if (SDataUtilities.isCatalogueTrnInv(optionType)) {
            picker = moModuleInv.getOptionPicker(optionType);
        }
        else if (SDataUtilities.isCatalogueMkt(optionType)) {
            picker = moModuleMkt.getOptionPicker(optionType);
        }
        else if (SDataUtilities.isCatalogueLog(optionType)) {
            picker = moModuleLog.getOptionPicker(optionType);
        }
        else if (SDataUtilities.isCatalogueMfg(optionType)) {
            picker = moModuleMfg.getOptionPicker(optionType);
        }
        else if (SDataUtilities.isCatalogueHrs(optionType)) {
            picker = moModuleHrs.getOptionPicker(optionType);
        }

        return picker;
    }

    @Override
    public cfd.DCfdSignature getCfdSignature() {
        return moCfdSignature;
    }

    @Override
    public cfd.DCfdSignature getCfdSignature(java.util.Date cfdDate) {
        DCfdSignature properSignature = null;

        for (DCfdSignature signature : mvCfdSignatures) {
            if (signature.getDate().getTime() <= cfdDate.getTime() && signature.getExpirationDate().getTime() >= cfdDate.getTime()) {
                properSignature = signature;
                break;
            }
        }

        return properSignature;
    }

    @Override
    public int pickOption(int optionType, erp.lib.form.SFormFieldInterface field, java.lang.Object filterKey) {
        SFormOptionPickerInterface picker = getOptionPicker(optionType);

        picker.formReset();
        picker.setFilterKey(filterKey);
        picker.formRefreshOptionPane();

        switch (field.getDataType()) {
            case SLibConstants.DATA_TYPE_KEY:
                picker.setSelectedPrimaryKey(field.getKey());
                break;
            case SLibConstants.DATA_TYPE_STRING:
                picker.setSelectedPrimaryKey(new Object[] { field.getString() });
                break;
            default:
        }

        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            switch (field.getDataType()) {
                case SLibConstants.DATA_TYPE_KEY:
                    field.setFieldValue(picker.getSelectedPrimaryKey());
                    break;
                case SLibConstants.DATA_TYPE_STRING:
                    field.setFieldValue((String) ((Object[]) picker.getSelectedPrimaryKey())[0]);
                    break;
                default:
            }

            field.getComponent().requestFocus();
        }

        return picker.getFormResult();
    }

    @Override
    public int pickOption(int optionType, erp.form.SFormFieldAccount field, java.lang.Object filterKey) {
        SFormOptionPickerInterface picker = getOptionPicker(optionType);

        picker.formReset();
        picker.setFilterKey(filterKey);
        picker.formRefreshOptionPane();
        picker.setSelectedPrimaryKey(new Object[] { field.getString() });
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            field.setFieldValue((String) ((Object[]) picker.getSelectedPrimaryKey())[0]);
            field.getComponent().requestFocus();
        }

        return picker.getFormResult();
    }

    @Override
    public String pickOption(int optionType, java.lang.Object filterKey) {
        String option = "";
        SFormOptionPickerInterface picker = getOptionPicker(optionType);

        picker.formReset();
        picker.setFilterKey(filterKey);
        picker.formRefreshOptionPane();
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            option = (String) picker.getSelectedOption().getValues().get(0);
        }

        return option;
    }

    @Override
    public double pickExchangeRate(int currency, java.util.Date date) {
        double rate = 0;
        SFormOptionPickerInterface picker = getOptionPicker(SDataConstants.FIN_EXC_RATE);

        picker.formReset();
        picker.setFilterKey(new int[] { currency });
        picker.formRefreshOptionPane();
        picker.setSelectedPrimaryKey(new Object[] { currency, date });
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            rate = ((SDataExchangeRate) SDataUtilities.readRegistry(this, SDataConstants.FIN_EXC_RATE, picker.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE)).getExchangeRate();
        }

        return rate;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbCurrentDate) {
                actionFileCurrentDate();
            }
            else if (button == jbSession) {
                actionFileSession(false);
            }
        }
        else if (e.getSource() instanceof javax.swing.JToggleButton) {
            javax.swing.JToggleButton toggleButton = (javax.swing.JToggleButton) e.getSource();

            if (toggleButton == jtbModuleCfg) {
                actionModuleCfg();
            }
            else if (toggleButton == jtbModuleFin) {
                actionModuleFin();
            }
            else if (toggleButton == jtbModulePur) {
                actionModulePur();
            }
            else if (toggleButton == jtbModuleSal) {
                actionModuleSal();
            }
            else if (toggleButton == jtbModuleInv) {
                actionModuleInv();
            }
            else if (toggleButton == jtbModuleMkt) {
                actionModuleMkt();
            }
            else if (toggleButton == jtbModuleLog) {
                actionModuleLog();
            }
            else if (toggleButton == jtbModuleMfg) {
                actionModuleMfg();
            }
            else if (toggleButton == jtbModuleHrs) {
                actionModuleHrs();
            }
        }
        else if (e.getSource() instanceof javax.swing.JMenuItem) {
            javax.swing.JMenuItem item = (javax.swing.JMenuItem) e.getSource();

            if (item == jmiFileCurrentDate) {
                actionFileCurrentDate();
            }
            else if (item == jmiFileSession) {
                actionFileSession(false);
            }
            else if (item == jmiFilePassword) {
                actionFilePassword();
            }
            if (item == jmiFileCloseViews) {
                actionFileCloseViews();
            }
            if (item == jmiFileCloseSession) {
                actionFileCloseSession();
            }
            else if (item == jmiFileClose) {
                actionFileClose();
            }
            else if (item == jmiViewModuleCfg) {
                actionModuleCfg();
            }
            else if (item == jmiViewModuleFin) {
                actionModuleFin();
            }
            else if (item == jmiViewModulePur) {
                actionModulePur();
            }
            else if (item == jmiViewModuleSal) {
                actionModuleSal();
            }
            else if (item == jmiViewModuleInv) {
                actionModuleInv();
            }
            else if (item == jmiViewModuleMkt) {
                actionModuleMkt();
            }
            else if (item == jmiViewModuleLog) {
                actionModuleLog();
            }
            else if (item == jmiViewModuleMfg) {
                actionModuleMfg();
            }
            else if (item == jmiViewModuleHrs) {
                actionModuleHrs();
            }
            else if (item == jmiHelpAbout) {
                actionHelpAbout();
            }
        }
    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    @Override
    public JTabbedPane getTabbedPane() {
        return jTabbedPane;
    }

    @Override
    public SDbDatabase getSysDatabase() {
        return moSysDatabase;
    }

    @Override
    public Statement getSysStatement() {
        return miSysStatement;
    }

    @Override
    public SGuiSession getSession() {
        return moSession;
    }

    @Override
    public sa.lib.gui.SGuiDatePicker getDatePicker() {
        return moDatePicker;
    }

    @Override
    public sa.lib.gui.SGuiDateRangePicker getDateRangePicker() {
        return moDateRangePicker;
    }

    @Override
    public SGuiYearPicker getYearPicker() {
        return moYearPicker;
    }

    @Override
    public SGuiYearMonthPicker getYearMonthPicker() {
        return moYearMonthPicker;
    }

    @Override
    public JFileChooser getFileChooser() {
        return moFileChooser;
    }

    @Override
    public javax.swing.ImageIcon getImageIcon(int icon) {
        ImageIcon imageIcon = null;

        switch (icon) {
            case SLibConstants.ICON_NEW:
                imageIcon = moIconNew;
                break;
            case SLibConstants.ICON_NEW_MAIN:
                imageIcon = moIconNewMain;
                break;
            case SLibConstants.ICON_INSERT:
                imageIcon = moIconInsert;
                break;
            case SLibConstants.ICON_COPY:
                imageIcon = moIconCopy;
                break;
            case SLibConstants.ICON_EDIT:
                imageIcon = moIconEdit;
                break;
            case SLibConstants.ICON_ANNUL:
                imageIcon = moIconAnnul;
                break;
            case SLibConstants.ICON_DELETE:
                imageIcon = moIconDelete;
                break;
            case SLibConstants.ICON_LOOK:
                imageIcon = moIconLook;
                break;
            case SLibConstants.ICON_PRINT:
                imageIcon = moIconPrint;
                break;
            case SLibConstants.ICON_PRINT_ACK_CAN:
                imageIcon = moIconPrintAckCancel;
                break;
            case SLibConstants.ICON_PRINT_ORDER:
                imageIcon = moIconPrintOrder;
                break;
            case SLibConstants.ICON_PRINT_PHOTO:
                imageIcon = moIconPrintPhoto;
                break;
            case SLibConstants.ICON_KARDEX:
                imageIcon = moIconKardex;
                break;
            case SLibConstants.ICON_NOTES:
                imageIcon = moIconNotes;
                break;
            case SLibConstants.ICON_DOC_ADD:
                imageIcon = moIconDocAdd;
                break;
            case SLibConstants.ICON_DOC_REM:
                imageIcon = moIconDocRemove;
                break;
            case SLibConstants.ICON_ACTION:
                imageIcon = moIconAction;
                break;
            case SLibConstants.ICON_LINK:
                imageIcon = moIconLink;
                break;
            case SLibConstants.ICON_CONTRACT_ANALYSIS:
                imageIcon = moIconContractAnalysis;
                break;
            case SLibConstants.ICON_SUM:
                imageIcon = moIconSum;
                break;
            case SLibConstants.ICON_DOC_LINK:
                imageIcon = moIconDocLink;
                break;
            case SLibConstants.ICON_DOC_LINK_NO:
                imageIcon = moIconDocLinkNo;
                break;
            case SLibConstants.ICON_DOC_SUPPLY:
                imageIcon = moIconDocSupply;
                break;
            case SLibConstants.ICON_DOC_SUPPLY_NO:
                imageIcon = moIconDocSupplyNo;
                break;
            case SLibConstants.ICON_DOC_TYPE:
                imageIcon = moIconDocType;
                break;
            case SLibConstants.ICON_DOC_IMPORT:
                imageIcon = moIconDocImport;
                break;
            case SLibConstants.ICON_DOC_OPEN:
                imageIcon = moIconDocOpen;
                break;
            case SLibConstants.ICON_DOC_CLOSE:
                imageIcon = moIconDocClose;
                break;
            case SLibConstants.ICON_DOC_XML:
                imageIcon = moIconDocXml;
                break;
            case SLibConstants.ICON_DOC_XML_CANCEL:
                imageIcon = moIconDocXmlCancel;
                break;
            case SLibConstants.ICON_DOC_XML_SIGN:
                imageIcon = moIconDocXmlSign;
                break;
            case SLibConstants.ICON_DOC_DELIVERY:
                imageIcon = moIconDocDelivery;
                break;
            case SLibConstants.ICON_FILTER_BP:
                imageIcon = moIconFilterBp;
                break;
            case SLibConstants.ICON_FILTER_BPB:
                imageIcon = moIconFilterBpb;
                break;
            case SLibConstants.ICON_FILTER_DOC:
                imageIcon = moIconFilterDoc;
                break;
            case SLibConstants.ICON_APPROVE:
                imageIcon = moIconApprove;
                break;
            case SLibConstants.ICON_APPROVE_NO:
                imageIcon = moIconApproveNo;
                break;
            case SLibConstants.ICON_QUERY_BP:
                imageIcon = moIconQueryBizPartner;
                break;
            case SLibConstants.ICON_QUERY_DOC:
                imageIcon = moIconQueryDocument;
                break;
            case SLibConstants.ICON_QUERY_REC:
                imageIcon = moIconQueryRecord;
                break;
            case SLibConstants.ICON_ARROW_UP:
                imageIcon = moIconArrowUp;
                break;
            case SLibConstants.ICON_ARROW_DOWN:
                imageIcon = moIconArrowDown;
                break;
            case SLibConstants.ICON_ARROW_LEFT:
                imageIcon = moIconArrowLeft;
                break;
            case SLibConstants.ICON_ARROW_RIGHT:
                imageIcon = moIconArrowRight;
                break;
            case SLibConstants.ICON_BP_EXPORT:
                imageIcon = moIconBizPartnerExport;
                break;
            case SModConsts.MOD_CFG:
            case SModConsts.MOD_CFG_N:
            case SModConsts.MOD_USR_N:
            case SModConsts.MOD_LOC_N:
            case SModConsts.MOD_BPS_N:
            case SModConsts.MOD_ITM_N:
                imageIcon = moIconModuleCfg;
                break;
            case SModConsts.MOD_FIN:
            case SModConsts.MOD_FIN_N:
                imageIcon = moIconModuleFin;
                break;
            case SModConsts.MOD_PUR:
            case SModConsts.MOD_TRN_PUR_N:
                imageIcon = moIconModulePur;
                break;
            case SModConsts.MOD_SAL:
            case SModConsts.MOD_TRN_SAL_N:
                imageIcon = moIconModuleSal;
                break;
            case SModConsts.MOD_INV:
            case SModConsts.MOD_TRN_INV_N:
                imageIcon = moIconModuleInv;
                break;
            case SModConsts.MOD_MKT:
            case SModConsts.MOD_MKT_N:
                imageIcon = moIconModuleMkt;
                break;
            case SModConsts.MOD_LOG:
            case SModConsts.MOD_LOG_N:
                imageIcon = moIconModuleLog;
                break;
            case SModConsts.MOD_MFG:
            case SModConsts.MOD_MFG_N:
                imageIcon = moIconModuleMfg;
                break;
            case SModConsts.MOD_HRS:
            case SModConsts.MOD_HRS_N:
                imageIcon = moIconModuleHrs;
                break;
            case DImgConsts.ICO_GUI_CLOSE:
                imageIcon = moIconCloseActive;
                break;
            case DImgConsts.ICO_GUI_CLOSE_INA:
                imageIcon = moIconCloseInactive;
                break;
            case DImgConsts.ICO_GUI_CLOSE_BRI:
                imageIcon = moIconCloseBright;
                break;
            case DImgConsts.ICO_GUI_CLOSE_DAR:
                imageIcon = moIconCloseDark;
                break;
            default:
                showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return imageIcon;
    }

    @Override
    public SGuiUserGui readUserGui(int[] key) {
        SDbUserGui userGui = new SDbUserGui();

        try {
            userGui.read(moSession, key);
        }
        catch (SQLException e) {
            userGui = null;
            SLibUtils.printException(this, e);
        }
        catch (Exception e) {
            userGui = null;
            SLibUtils.printException(this, e);
        }

        return userGui;
    }

    @Override
    public SGuiUserGui saveUserGui(int[] key, String gui) {
        SDbUserGui userGui = (SDbUserGui) readUserGui(key);

        if (userGui == null) {
            userGui = new SDbUserGui();
            userGui.setPrimaryKey(key);
        }

        try {
            userGui.setGui(gui);
            userGui.save(moSession);
        }
        catch (SQLException e) {
            userGui = null;
            SLibUtils.printException(this, e);
        }
        catch (Exception e) {
            userGui = null;
            SLibUtils.printException(this, e);
        }

        return userGui;
    }

    @Override
    public HashMap<String, Object> createReportParams() {
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("sCompanyName", moSessionXXX.getCompany().getCompany());
        map.put("sUserName", moSessionXXX.getUser().getUser());
        map.put("sVendorLabel", SClient.VENDOR_COPYRIGHT);
        map.put("sVendorWebsite", SClient.VENDOR_WEBSITE);
        map.put("bShowDetailBackground", false);
        map.put("oDateFormat", SLibUtils.DateFormatDate);
        map.put("oDatetimeFormat", SLibUtils.DateFormatDatetime);
        map.put("oTimeFormat", SLibUtils.DateFormatTime);
        map.put("oValueFormat", SLibUtils.getDecimalFormatAmount());
        map.put("sImageDir", moSessionXXX.getParamsCompany().getImagesDirectory());

        return map;
    }

    @Override
    public String getTableCompany() {
        return SModConsts.TablesMap.get(SModConsts.CFG_PARAM_CO);
    }

    @Override
    public String getTableUser() {
        return SModConsts.TablesMap.get(SModConsts.USRU_USR);
    }

    @Override
    public String getAppName() {
        return APP_NAME;
    }

    @Override
    public String getAppRelease() {
        return APP_RELEASE;
    }

    @Override
    public String getAppCopyright() {
        return APP_COPYRIGHT;
    }

    @Override
    public String getAppProvider() {
        return APP_PROVIDER;
    }

    @Override
    public void computeSessionSettings() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void preserveSessionSettings() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void showMsgBoxError(String msg) {
        JOptionPane.showMessageDialog(this, msg, SGuiConsts.MSG_BOX_ERROR, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showMsgBoxWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, SGuiConsts.MSG_BOX_WARNING, JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void showMsgBoxInformation(String msg) {
        JOptionPane.showMessageDialog(this, msg, SGuiConsts.MSG_BOX_INFORMATION, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public int showMsgBoxConfirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, SGuiConsts.MSG_BOX_CONFIRM, JOptionPane.YES_NO_OPTION);
    }
}
