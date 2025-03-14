/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mcfg.data.SCfgUtils;
import erp.mcfg.data.SDataCompany;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbAbsenceConsumption;
import erp.mod.hrs.db.SDbConfig;
import erp.mod.hrs.db.SDbEarning;
import erp.mod.hrs.db.SDbPayroll;
import erp.mod.hrs.db.SDbPayrollReceipt;
import erp.mod.hrs.db.SDbPayrollReceiptEarning;
import erp.mod.hrs.db.SDbPayrollReceiptIssue;
import erp.mod.hrs.db.SDbPaysheetCustomType;
import erp.mod.hrs.db.SDbWorkingDaySettings;
import erp.mod.hrs.db.SHrsConsts;
import erp.mod.hrs.db.SHrsPayroll;
import erp.mod.hrs.db.SHrsPayrollDataProvider;
import erp.mod.hrs.db.SHrsPayrollUtils;
import erp.mod.hrs.db.SHrsReceipt;
import erp.mod.hrs.db.SHrsReceiptDeduction;
import erp.mod.hrs.db.SHrsReceiptEarning;
import erp.mod.hrs.db.SHrsUtils;
import erp.mod.hrs.db.SRowBonus;
import erp.mod.hrs.db.SRowPayrollEmployee;
import erp.mod.hrs.db.SRowTimeClock;
import erp.mod.hrs.link.pub.SShareData;
import erp.mod.hrs.link.utils.SPrepayroll;
import erp.mod.hrs.link.utils.SPrepayrollRow;
import erp.mod.hrs.utils.SEarnConfiguration;
import erp.mod.hrs.utils.SPayrollBonusUtils;
import erp.mod.hrs.utils.SPayrollUtils;
import erp.mod.hrs.utils.SPrepayrollUtils;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.joda.time.DateTime;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.cell.SGridCellRendererIconCircle;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiField;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFieldInteger;
import sa.lib.gui.bean.SBeanFieldRadio;
import sa.lib.gui.bean.SBeanForm;

/**
 *
 * @author Juan Barajas, Néstor Ávalos, Sergio Flores, Claudio Peña, Edwin Carmona
 */
public class SFormPayroll extends SBeanForm implements ActionListener, ItemListener, FocusListener, ChangeListener {

    private final static String TXT_WITH_TAX_SUB_PAY = "CON pago de subsidio para el empleo";
    private final static String TXT_WITHOUT_TAX_SUB_PAY = "SIN pago de subsidio para el empleo";
    
    private SDbPayroll moRegistry;
    private SDbConfig moModuleConfig;
    private SDbWorkingDaySettings moWorkingDaySettings;
    private SHrsPayroll moHrsPayroll;

    private SGridPaneForm moGridPaneEmployeesAvailable;
    private SGridPaneForm moGridPanePayrollReceipts;
    private ArrayList<SDbPayrollReceipt> maPayrollReceiptsDeleted;
    private ArrayList<Integer> maBonusPayed;
    private ArrayList<Integer> maEmployeesWithCurrentBonus;

    private int mnDefaultPeriodYear;
    private int mnDefaultNumber;
    private int mnDefaultPeriod;
    private Date mtDefaultDateStart;
    private Date mtDefaultDateEnd;
    
    private int mnAuxCurrentPeriodYear;
    private int mnAuxCurrentNumber;
    private int mnAuxCurrentPeriod;
    private Date mtAuxCurrentDateStart;
    private Date mtAuxCurrentDateEnd;
    
    private int mnCalendarDays;
    private boolean mbIsReadOnly;
    private boolean mbIsBeingCopied;
    private boolean mbIsWithTaxSubsidy;
    private boolean mbIsGoingToReceipts;
    private boolean mbAuxReopen;
    
    /**
     * Creates new form SFormPayroll
     * @param client
     * @param title
     * @param subtype
     */
    public SFormPayroll(SGuiClient client, String title, int subtype) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.HRS_PAY, subtype, title);
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        bgPayrollType = new javax.swing.ButtonGroup();
        bgViewEmployees = new javax.swing.ButtonGroup();
        jpMain = new javax.swing.JPanel();
        jtpPayroll = new javax.swing.JTabbedPane();
        jpSettings = new javax.swing.JPanel();
        jpSettingsC = new javax.swing.JPanel();
        jpSettingsCN = new javax.swing.JPanel();
        jpSettingsCNW = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jlPaymentType = new javax.swing.JLabel();
        jtfPaymentType = new javax.swing.JTextField();
        jPanel35 = new javax.swing.JPanel();
        jlYear = new javax.swing.JLabel();
        moIntPeriodYear = new sa.lib.gui.bean.SBeanFieldInteger();
        jbEditPeriodYear = new javax.swing.JButton();
        jPanel42 = new javax.swing.JPanel();
        jlFiscalYear = new javax.swing.JLabel();
        moIntFiscalYear = new sa.lib.gui.bean.SBeanFieldInteger();
        jbEditFiscalYear = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jlNumber = new javax.swing.JLabel();
        moIntNumber = new sa.lib.gui.bean.SBeanFieldInteger();
        jbEditNumber = new javax.swing.JButton();
        jbGetNextNumber = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jbEditDates = new javax.swing.JButton();
        jtfDefaultDateStart = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        moDateDateEnd = new sa.lib.gui.bean.SBeanFieldDate();
        jLabel1 = new javax.swing.JLabel();
        jtfDefaultDateEnd = new javax.swing.JTextField();
        jPanel36 = new javax.swing.JPanel();
        jlPeriod = new javax.swing.JLabel();
        moIntPeriod = new sa.lib.gui.bean.SBeanFieldInteger();
        jbEditPeriod = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jlReceiptDays = new javax.swing.JLabel();
        moIntReceiptDays = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel13 = new javax.swing.JPanel();
        jlWorkingDays = new javax.swing.JLabel();
        moIntWorkingDays = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel17 = new javax.swing.JPanel();
        jlPaysheetType = new javax.swing.JLabel();
        moRadNormal = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadSpecial = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadExtraordinary = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel31 = new javax.swing.JPanel();
        jlPaysheetCustomType = new javax.swing.JLabel();
        moKeyPaysheetCustomType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel47 = new javax.swing.JPanel();
        jlRecruitmentSchemaType = new javax.swing.JLabel();
        moKeyRecruitmentSchemaType = new sa.lib.gui.bean.SBeanFieldKey();
        jbEditRecruitmentSchemaType = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jlHint = new javax.swing.JLabel();
        moTextHint = new sa.lib.gui.bean.SBeanFieldText();
        jbEditHint = new javax.swing.JButton();
        jlHintHint = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jlNotes = new javax.swing.JLabel();
        moTextNotes = new sa.lib.gui.bean.SBeanFieldText();
        jpSettingsCNE = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jlTaxComputationType = new javax.swing.JLabel();
        moKeyTaxComputationType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel25 = new javax.swing.JPanel();
        jlTax = new javax.swing.JLabel();
        moKeyTax = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel26 = new javax.swing.JPanel();
        jlTaxSubsidy = new javax.swing.JLabel();
        moKeyTaxSubsidy = new sa.lib.gui.bean.SBeanFieldKey();
        jlTaxSubsidyHint = new javax.swing.JLabel();
        jPanel43 = new javax.swing.JPanel();
        jlEmploymentSubsidy = new javax.swing.JLabel();
        moKeyEmploymentSubsidy = new sa.lib.gui.bean.SBeanFieldKey();
        jlEmploymentSubsidyHint = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jlSsContribution = new javax.swing.JLabel();
        moKeySsContribution = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel19 = new javax.swing.JPanel();
        moBoolSsContribution = new sa.lib.gui.bean.SBeanFieldBoolean();
        jPanel18 = new javax.swing.JPanel();
        jlTaxSubsidyOption = new javax.swing.JLabel();
        jtfTaxSubsidyOption = new javax.swing.JTextField();
        jbTaxSubsidyOptionChange = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        jlMwzType = new javax.swing.JLabel();
        moKeyMwzType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel14 = new javax.swing.JPanel();
        jlMwzWage = new javax.swing.JLabel();
        moDecMwzWage = new sa.lib.gui.bean.SBeanCompoundFieldCurrency();
        jPanel23 = new javax.swing.JPanel();
        jlMwzReferenceType = new javax.swing.JLabel();
        moKeyMwzReferenceType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel15 = new javax.swing.JPanel();
        jlMwzReferenceWage = new javax.swing.JLabel();
        moDecMwzReferenceWage = new sa.lib.gui.bean.SBeanCompoundFieldCurrency();
        jPanel34 = new javax.swing.JPanel();
        jlUmaAmount = new javax.swing.JLabel();
        moDecUmaAmount = new sa.lib.gui.bean.SBeanCompoundFieldCurrency();
        jPanel46 = new javax.swing.JPanel();
        jlUmiAmount = new javax.swing.JLabel();
        moDecUmiAmount = new sa.lib.gui.bean.SBeanCompoundFieldCurrency();
        jPanel44 = new javax.swing.JPanel();
        moBoolClosed = new sa.lib.gui.bean.SBeanFieldBoolean();
        jpSettingsS = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jlTotalEarnings = new javax.swing.JLabel();
        moDecTotalEarnings = new sa.lib.gui.bean.SBeanCompoundFieldCurrency();
        jPanel29 = new javax.swing.JPanel();
        jlTotalDeductions = new javax.swing.JLabel();
        moDecTotalDeductions = new sa.lib.gui.bean.SBeanCompoundFieldCurrency();
        jPanel30 = new javax.swing.JPanel();
        jlTotalNet = new javax.swing.JLabel();
        moDecTotalNet = new sa.lib.gui.bean.SBeanCompoundFieldCurrency();
        jPanel37 = new javax.swing.JPanel();
        jbGoTabReceipts = new javax.swing.JButton();
        jpReceipts = new javax.swing.JPanel();
        jpReceiptsC = new javax.swing.JPanel();
        jpReceiptsControls = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jlView = new javax.swing.JLabel();
        moRadViewEmployeesActive = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadViewEmployeesAll = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel4 = new javax.swing.JPanel();
        jlFilter = new javax.swing.JLabel();
        moBoolFilterWages = new sa.lib.gui.bean.SBeanFieldBoolean();
        moBoolFilterAssimilated = new sa.lib.gui.bean.SBeanFieldBoolean();
        moBoolFilterRetirees = new sa.lib.gui.bean.SBeanFieldBoolean();
        moBoolFilterOthers = new sa.lib.gui.bean.SBeanFieldBoolean();
        jPanel40 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jbReceiptCaptureEarnings = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jbLoadPrepayroll = new javax.swing.JButton();
        jPanel33 = new javax.swing.JPanel();
        jbReceiptCaptureDeductions = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jbClearPrepayroll = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jbAddPayrollBonus = new javax.swing.JButton();
        jpReceiptsAvailableEmployees = new javax.swing.JPanel();
        jlTotalAvailables = new javax.swing.JLabel();
        jpReceiptsControlsReceipts = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jlDummy01 = new javax.swing.JLabel();
        jbReceiptAdd = new javax.swing.JButton();
        jbReceiptAddAll = new javax.swing.JButton();
        jbReceiptRemove = new javax.swing.JButton();
        jbReceiptRemoveAll = new javax.swing.JButton();
        jpReceiptsPayrollReceipts = new javax.swing.JPanel();
        jlTotalSelected = new javax.swing.JLabel();

        jpMain.setLayout(new java.awt.BorderLayout());

        jtpPayroll.setAutoscrolls(true);
        jtpPayroll.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jpSettings.setLayout(new java.awt.BorderLayout());

        jpSettingsC.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpSettingsC.setLayout(new java.awt.BorderLayout());

        jpSettingsCN.setLayout(new java.awt.BorderLayout());

        jpSettingsCNW.setLayout(new java.awt.GridLayout(14, 1, 0, 5));

        jPanel8.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPaymentType.setText("Período de pago:");
        jlPaymentType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel8.add(jlPaymentType);

        jtfPaymentType.setEditable(false);
        jtfPaymentType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfPaymentType.setText("TEXT");
        jtfPaymentType.setFocusable(false);
        jtfPaymentType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jtfPaymentType);

        jpSettingsCNW.add(jPanel8);

        jPanel35.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlYear.setText("Año nómina:*");
        jlYear.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel35.add(jlYear);
        jPanel35.add(moIntPeriodYear);

        jbEditPeriodYear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditPeriodYear.setToolTipText("Modificar");
        jbEditPeriodYear.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel35.add(jbEditPeriodYear);

        jpSettingsCNW.add(jPanel35);

        jPanel42.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFiscalYear.setText("Año fiscal:*");
        jlFiscalYear.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel42.add(jlFiscalYear);
        jPanel42.add(moIntFiscalYear);

        jbEditFiscalYear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditFiscalYear.setToolTipText("Modificar");
        jbEditFiscalYear.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel42.add(jbEditFiscalYear);

        jpSettingsCNW.add(jPanel42);

        jPanel9.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNumber.setText("Número nómina:*");
        jlNumber.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jlNumber);
        jPanel9.add(moIntNumber);

        jbEditNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditNumber.setToolTipText("Modificar");
        jbEditNumber.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel9.add(jbEditNumber);

        jbGetNextNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbGetNextNumber.setToolTipText("Obtener número nómina");
        jbGetNextNumber.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel9.add(jbGetNextNumber);

        jpSettingsCNW.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial:*");
        jlDateStart.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel10.add(jlDateStart);
        jPanel10.add(moDateDateStart);

        jbEditDates.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditDates.setToolTipText("Modificar");
        jbEditDates.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel10.add(jbEditDates);

        jtfDefaultDateStart.setEditable(false);
        jtfDefaultDateStart.setText("jTextField1");
        jtfDefaultDateStart.setToolTipText("Fecha inicial por default");
        jtfDefaultDateStart.setFocusable(false);
        jtfDefaultDateStart.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jtfDefaultDateStart);

        jpSettingsCNW.add(jPanel10);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final:*");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel11.add(jlDateEnd);
        jPanel11.add(moDateDateEnd);

        jLabel1.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jLabel1);

        jtfDefaultDateEnd.setEditable(false);
        jtfDefaultDateEnd.setText("jTextField2");
        jtfDefaultDateEnd.setToolTipText("Fecha final por default");
        jtfDefaultDateEnd.setFocusable(false);
        jtfDefaultDateEnd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel11.add(jtfDefaultDateEnd);

        jpSettingsCNW.add(jPanel11);

        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPeriod.setText("Período nómina:*");
        jlPeriod.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel36.add(jlPeriod);
        jPanel36.add(moIntPeriod);

        jbEditPeriod.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditPeriod.setToolTipText("Modificar");
        jbEditPeriod.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel36.add(jbEditPeriod);

        jpSettingsCNW.add(jPanel36);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReceiptDays.setText("Días nómina:");
        jlReceiptDays.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel12.add(jlReceiptDays);

        moIntReceiptDays.setEditable(false);
        jPanel12.add(moIntReceiptDays);

        jpSettingsCNW.add(jPanel12);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlWorkingDays.setText("Días laborables:");
        jlWorkingDays.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel13.add(jlWorkingDays);

        moIntWorkingDays.setEditable(false);
        jPanel13.add(moIntWorkingDays);

        jpSettingsCNW.add(jPanel13);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPaysheetType.setText("Tipo nómina:*");
        jlPaysheetType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel17.add(jlPaysheetType);

        bgPayrollType.add(moRadNormal);
        moRadNormal.setText("Normal");
        moRadNormal.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel17.add(moRadNormal);

        bgPayrollType.add(moRadSpecial);
        moRadSpecial.setText("Especial");
        moRadSpecial.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel17.add(moRadSpecial);

        bgPayrollType.add(moRadExtraordinary);
        moRadExtraordinary.setText("Extraordinaria");
        moRadExtraordinary.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel17.add(moRadExtraordinary);

        jpSettingsCNW.add(jPanel17);

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPaysheetCustomType.setText("Tipo nómina empresa:*");
        jlPaysheetCustomType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel31.add(jlPaysheetCustomType);

        moKeyPaysheetCustomType.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel31.add(moKeyPaysheetCustomType);

        jpSettingsCNW.add(jPanel31);

        jPanel47.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRecruitmentSchemaType.setText("Tipo régimen nómina:*");
        jlRecruitmentSchemaType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel47.add(jlRecruitmentSchemaType);

        moKeyRecruitmentSchemaType.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel47.add(moKeyRecruitmentSchemaType);

        jbEditRecruitmentSchemaType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditRecruitmentSchemaType.setToolTipText("Modificar");
        jbEditRecruitmentSchemaType.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel47.add(jbEditRecruitmentSchemaType);

        jpSettingsCNW.add(jPanel47);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlHint.setForeground(new java.awt.Color(0, 102, 102));
        jlHint.setText("Texto aclaratorio:");
        jlHint.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel5.add(jlHint);
        jPanel5.add(moTextHint);

        jbEditHint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditHint.setToolTipText("Modificar");
        jbEditHint.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbEditHint);

        jlHintHint.setForeground(java.awt.Color.gray);
        jlHintHint.setText("(Para el asunto del correo-e de recibos.)");
        jlHintHint.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel5.add(jlHintHint);

        jpSettingsCNW.add(jPanel5);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNotes.setText("Notas nómina:");
        jlNotes.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel16.add(jlNotes);

        moTextNotes.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel16.add(moTextNotes);

        jpSettingsCNW.add(jPanel16);

        jpSettingsCN.add(jpSettingsCNW, java.awt.BorderLayout.WEST);

        jpSettingsCNE.setLayout(new java.awt.GridLayout(14, 1, 0, 5));

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTaxComputationType.setText("Tipo cálculo impuesto:*");
        jlTaxComputationType.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel24.add(jlTaxComputationType);

        moKeyTaxComputationType.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel24.add(moKeyTaxComputationType);

        jpSettingsCNE.add(jPanel24);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTax.setText("Tabla impuesto:*");
        jlTax.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel25.add(jlTax);

        moKeyTax.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel25.add(moKeyTax);

        jpSettingsCNE.add(jPanel25);

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTaxSubsidy.setText("Tabla subsidio para el empleo (obsoleto):*");
        jlTaxSubsidy.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel26.add(jlTaxSubsidy);

        moKeyTaxSubsidy.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel26.add(moKeyTaxSubsidy);

        jlTaxSubsidyHint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTaxSubsidyHint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlTaxSubsidyHint.setToolTipText("Vigente hasta 31/04/2024");
        jlTaxSubsidyHint.setPreferredSize(new java.awt.Dimension(15, 23));
        jPanel26.add(jlTaxSubsidyHint);

        jpSettingsCNE.add(jPanel26);

        jPanel43.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlEmploymentSubsidy.setText("Configuración subsidio para el empleo:*");
        jlEmploymentSubsidy.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel43.add(jlEmploymentSubsidy);

        moKeyEmploymentSubsidy.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel43.add(moKeyEmploymentSubsidy);

        jlEmploymentSubsidyHint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlEmploymentSubsidyHint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlEmploymentSubsidyHint.setToolTipText("Vigente desde 01/05/2024");
        jlEmploymentSubsidyHint.setPreferredSize(new java.awt.Dimension(15, 23));
        jPanel43.add(jlEmploymentSubsidyHint);

        jpSettingsCNE.add(jPanel43);

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSsContribution.setText("Tabla retención SS:*");
        jlSsContribution.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel27.add(jlSsContribution);

        moKeySsContribution.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel27.add(moKeySsContribution);

        jpSettingsCNE.add(jPanel27);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolSsContribution.setText("Con cálculo de retención SS");
        moBoolSsContribution.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel19.add(moBoolSsContribution);

        jpSettingsCNE.add(jPanel19);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTaxSubsidyOption.setText("Pago del subsidio para el empleo:");
        jlTaxSubsidyOption.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel18.add(jlTaxSubsidyOption);

        jtfTaxSubsidyOption.setEditable(false);
        jtfTaxSubsidyOption.setFocusable(false);
        jtfTaxSubsidyOption.setPreferredSize(new java.awt.Dimension(222, 23));
        jPanel18.add(jtfTaxSubsidyOption);

        jbTaxSubsidyOptionChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/gui/img/icon_upd.gif"))); // NOI18N
        jbTaxSubsidyOptionChange.setToolTipText("Cambiar preferencia de pago de subsidio para el empleo");
        jbTaxSubsidyOptionChange.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel18.add(jbTaxSubsidyOptionChange);

        jpSettingsCNE.add(jPanel18);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMwzType.setText("Área geográfica:*");
        jlMwzType.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel22.add(jlMwzType);

        moKeyMwzType.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel22.add(moKeyMwzType);

        jpSettingsCNE.add(jPanel22);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMwzWage.setText("Salario mínimo área geográfica:");
        jlMwzWage.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel14.add(jlMwzWage);

        moDecMwzWage.setEditable(false);
        jPanel14.add(moDecMwzWage);

        jpSettingsCNE.add(jPanel14);

        jPanel23.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMwzReferenceType.setText("Área geográfica referencia:*");
        jlMwzReferenceType.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel23.add(jlMwzReferenceType);

        moKeyMwzReferenceType.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel23.add(moKeyMwzReferenceType);

        jpSettingsCNE.add(jPanel23);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMwzReferenceWage.setText("Salario mínimo área geográfica referencia:");
        jlMwzReferenceWage.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel15.add(jlMwzReferenceWage);

        moDecMwzReferenceWage.setEditable(false);
        jPanel15.add(moDecMwzReferenceWage);

        jpSettingsCNE.add(jPanel15);

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlUmaAmount.setText("Monto UMA:");
        jlUmaAmount.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel34.add(jlUmaAmount);

        moDecUmaAmount.setEditable(false);
        jPanel34.add(moDecUmaAmount);

        jpSettingsCNE.add(jPanel34);

        jPanel46.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlUmiAmount.setText("Monto UMI:");
        jlUmiAmount.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel46.add(jlUmiAmount);

        moDecUmiAmount.setEditable(false);
        jPanel46.add(moDecUmiAmount);

        jpSettingsCNE.add(jPanel46);

        jPanel44.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolClosed.setText("La nómina está cerrada");
        moBoolClosed.setEnabled(false);
        moBoolClosed.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel44.add(moBoolClosed);

        jpSettingsCNE.add(jPanel44);

        jpSettingsCN.add(jpSettingsCNE, java.awt.BorderLayout.EAST);

        jpSettingsC.add(jpSettingsCN, java.awt.BorderLayout.NORTH);

        jpSettings.add(jpSettingsC, java.awt.BorderLayout.CENTER);

        jpSettingsS.setBorder(javax.swing.BorderFactory.createTitledBorder("Totales de la nómina:"));
        jpSettingsS.setLayout(new java.awt.GridLayout(4, 1, 5, 5));

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jlTotalEarnings.setText("Percepciones:");
        jlTotalEarnings.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel28.add(jlTotalEarnings);

        moDecTotalEarnings.setEditable(false);
        jPanel28.add(moDecTotalEarnings);

        jpSettingsS.add(jPanel28);

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jlTotalDeductions.setText("Deducciones:");
        jlTotalDeductions.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel29.add(jlTotalDeductions);

        moDecTotalDeductions.setEditable(false);
        jPanel29.add(moDecTotalDeductions);

        jpSettingsS.add(jPanel29);

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jlTotalNet.setText("Total neto:");
        jlTotalNet.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel30.add(jlTotalNet);

        moDecTotalNet.setEditable(false);
        jPanel30.add(moDecTotalNet);

        jpSettingsS.add(jPanel30);

        jPanel37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbGoTabReceipts.setText("Ir a recibos");
        jbGoTabReceipts.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel37.add(jbGoTabReceipts);

        jpSettingsS.add(jPanel37);

        jpSettings.add(jpSettingsS, java.awt.BorderLayout.SOUTH);

        jtpPayroll.addTab("Nómina", jpSettings);

        jpReceipts.setLayout(new java.awt.BorderLayout());

        jpReceiptsC.setBorder(javax.swing.BorderFactory.createTitledBorder("Recibos de la nómina:"));
        jpReceiptsC.setLayout(new java.awt.BorderLayout(5, 5));

        jpReceiptsControls.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel39.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlView.setText("Ver:");
        jlView.setPreferredSize(new java.awt.Dimension(25, 23));
        jlView.setRequestFocusEnabled(false);
        jPanel39.add(jlView);

        bgViewEmployees.add(moRadViewEmployeesActive);
        moRadViewEmployeesActive.setText("Empleados activos");
        moRadViewEmployeesActive.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel39.add(moRadViewEmployeesActive);

        bgViewEmployees.add(moRadViewEmployeesAll);
        moRadViewEmployeesAll.setText("Todos los empleados");
        moRadViewEmployeesAll.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel39.add(moRadViewEmployeesAll);

        jPanel2.add(jPanel39);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFilter.setText("Agregar:");
        jlFilter.setToolTipText("Tipos de regímenes de contratación");
        jlFilter.setPreferredSize(new java.awt.Dimension(50, 23));
        jlFilter.setRequestFocusEnabled(false);
        jPanel4.add(jlFilter);

        moBoolFilterWages.setForeground(java.awt.Color.blue);
        moBoolFilterWages.setText("Sueldos");
        moBoolFilterWages.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel4.add(moBoolFilterWages);

        moBoolFilterAssimilated.setForeground(java.awt.Color.magenta);
        moBoolFilterAssimilated.setText("Asimilados");
        moBoolFilterAssimilated.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel4.add(moBoolFilterAssimilated);

        moBoolFilterRetirees.setForeground(java.awt.Color.red);
        moBoolFilterRetirees.setText("Jubilados");
        moBoolFilterRetirees.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(moBoolFilterRetirees);

        moBoolFilterOthers.setText("Otros");
        moBoolFilterOthers.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel4.add(moBoolFilterOthers);

        jPanel2.add(jPanel4);

        jpReceiptsControls.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel40.setPreferredSize(new java.awt.Dimension(550, 33));
        jPanel40.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbReceiptCaptureEarnings.setText("Capturar percepciones");
        jbReceiptCaptureEarnings.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbReceiptCaptureEarnings.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel1.add(jbReceiptCaptureEarnings);

        jLabel2.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel1.add(jLabel2);

        jbLoadPrepayroll.setText("Cargar prenómina");
        jbLoadPrepayroll.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbLoadPrepayroll.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel1.add(jbLoadPrepayroll);

        jPanel40.add(jPanel1);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbReceiptCaptureDeductions.setText("Capturar deducciones");
        jbReceiptCaptureDeductions.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbReceiptCaptureDeductions.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel33.add(jbReceiptCaptureDeductions);

        jLabel3.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel33.add(jLabel3);

        jbClearPrepayroll.setText("Quitar prenómina");
        jbClearPrepayroll.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbClearPrepayroll.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel33.add(jbClearPrepayroll);

        jLabel4.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel33.add(jLabel4);

        jbAddPayrollBonus.setText("Agregar bonos nómina");
        jbAddPayrollBonus.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel33.add(jbAddPayrollBonus);

        jPanel40.add(jPanel33);

        jpReceiptsControls.add(jPanel40, java.awt.BorderLayout.EAST);

        jpReceiptsC.add(jpReceiptsControls, java.awt.BorderLayout.NORTH);

        jpReceiptsAvailableEmployees.setBorder(javax.swing.BorderFactory.createTitledBorder("Empleados disponibles:"));
        jpReceiptsAvailableEmployees.setPreferredSize(new java.awt.Dimension(375, 100));
        jpReceiptsAvailableEmployees.setLayout(new java.awt.BorderLayout());

        jlTotalAvailables.setText("[number of available employees]");
        jpReceiptsAvailableEmployees.add(jlTotalAvailables, java.awt.BorderLayout.SOUTH);

        jpReceiptsC.add(jpReceiptsAvailableEmployees, java.awt.BorderLayout.LINE_START);

        jpReceiptsControlsReceipts.setLayout(new java.awt.BorderLayout());

        jPanel32.setLayout(new java.awt.GridLayout(5, 1, 0, 5));
        jPanel32.add(jlDummy01);

        jbReceiptAdd.setText(">");
        jbReceiptAdd.setToolTipText("Agregar");
        jbReceiptAdd.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbReceiptAdd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel32.add(jbReceiptAdd);

        jbReceiptAddAll.setText(">>");
        jbReceiptAddAll.setToolTipText("Agregar todos");
        jbReceiptAddAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbReceiptAddAll.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel32.add(jbReceiptAddAll);

        jbReceiptRemove.setText("<");
        jbReceiptRemove.setToolTipText("Remover");
        jbReceiptRemove.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbReceiptRemove.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel32.add(jbReceiptRemove);

        jbReceiptRemoveAll.setText("<<");
        jbReceiptRemoveAll.setToolTipText("Remover todos");
        jbReceiptRemoveAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbReceiptRemoveAll.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel32.add(jbReceiptRemoveAll);

        jpReceiptsControlsReceipts.add(jPanel32, java.awt.BorderLayout.PAGE_START);

        jpReceiptsC.add(jpReceiptsControlsReceipts, java.awt.BorderLayout.CENTER);

        jpReceiptsPayrollReceipts.setBorder(javax.swing.BorderFactory.createTitledBorder("Recibos de nómina:"));
        jpReceiptsPayrollReceipts.setPreferredSize(new java.awt.Dimension(545, 100));
        jpReceiptsPayrollReceipts.setLayout(new java.awt.BorderLayout());

        jlTotalSelected.setText("[number of selected employees]");
        jpReceiptsPayrollReceipts.add(jlTotalSelected, java.awt.BorderLayout.PAGE_END);

        jpReceiptsC.add(jpReceiptsPayrollReceipts, java.awt.BorderLayout.LINE_END);

        jpReceipts.add(jpReceiptsC, java.awt.BorderLayout.CENTER);

        jtpPayroll.addTab("Recibos", jpReceipts);

        jpMain.add(jtpPayroll, java.awt.BorderLayout.CENTER);
        jtpPayroll.getAccessibleContext().setAccessibleName("Configuración");

        getContentPane().add(jpMain, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgPayrollType;
    private javax.swing.ButtonGroup bgViewEmployees;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbAddPayrollBonus;
    private javax.swing.JButton jbClearPrepayroll;
    private javax.swing.JButton jbEditDates;
    private javax.swing.JButton jbEditFiscalYear;
    private javax.swing.JButton jbEditHint;
    private javax.swing.JButton jbEditNumber;
    private javax.swing.JButton jbEditPeriod;
    private javax.swing.JButton jbEditPeriodYear;
    private javax.swing.JButton jbEditRecruitmentSchemaType;
    private javax.swing.JButton jbGetNextNumber;
    private javax.swing.JButton jbGoTabReceipts;
    private javax.swing.JButton jbLoadPrepayroll;
    private javax.swing.JButton jbReceiptAdd;
    private javax.swing.JButton jbReceiptAddAll;
    private javax.swing.JButton jbReceiptCaptureDeductions;
    private javax.swing.JButton jbReceiptCaptureEarnings;
    private javax.swing.JButton jbReceiptRemove;
    private javax.swing.JButton jbReceiptRemoveAll;
    private javax.swing.JButton jbTaxSubsidyOptionChange;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlDummy01;
    private javax.swing.JLabel jlEmploymentSubsidy;
    private javax.swing.JLabel jlEmploymentSubsidyHint;
    private javax.swing.JLabel jlFilter;
    private javax.swing.JLabel jlFiscalYear;
    private javax.swing.JLabel jlHint;
    private javax.swing.JLabel jlHintHint;
    private javax.swing.JLabel jlMwzReferenceType;
    private javax.swing.JLabel jlMwzReferenceWage;
    private javax.swing.JLabel jlMwzType;
    private javax.swing.JLabel jlMwzWage;
    private javax.swing.JLabel jlNotes;
    private javax.swing.JLabel jlNumber;
    private javax.swing.JLabel jlPaymentType;
    private javax.swing.JLabel jlPaysheetCustomType;
    private javax.swing.JLabel jlPaysheetType;
    private javax.swing.JLabel jlPeriod;
    private javax.swing.JLabel jlReceiptDays;
    private javax.swing.JLabel jlRecruitmentSchemaType;
    private javax.swing.JLabel jlSsContribution;
    private javax.swing.JLabel jlTax;
    private javax.swing.JLabel jlTaxComputationType;
    private javax.swing.JLabel jlTaxSubsidy;
    private javax.swing.JLabel jlTaxSubsidyHint;
    private javax.swing.JLabel jlTaxSubsidyOption;
    private javax.swing.JLabel jlTotalAvailables;
    private javax.swing.JLabel jlTotalDeductions;
    private javax.swing.JLabel jlTotalEarnings;
    private javax.swing.JLabel jlTotalNet;
    private javax.swing.JLabel jlTotalSelected;
    private javax.swing.JLabel jlUmaAmount;
    private javax.swing.JLabel jlUmiAmount;
    private javax.swing.JLabel jlView;
    private javax.swing.JLabel jlWorkingDays;
    private javax.swing.JLabel jlYear;
    private javax.swing.JPanel jpMain;
    private javax.swing.JPanel jpReceipts;
    private javax.swing.JPanel jpReceiptsAvailableEmployees;
    private javax.swing.JPanel jpReceiptsC;
    private javax.swing.JPanel jpReceiptsControls;
    private javax.swing.JPanel jpReceiptsControlsReceipts;
    private javax.swing.JPanel jpReceiptsPayrollReceipts;
    private javax.swing.JPanel jpSettings;
    private javax.swing.JPanel jpSettingsC;
    private javax.swing.JPanel jpSettingsCN;
    private javax.swing.JPanel jpSettingsCNE;
    private javax.swing.JPanel jpSettingsCNW;
    private javax.swing.JPanel jpSettingsS;
    private javax.swing.JTextField jtfDefaultDateEnd;
    private javax.swing.JTextField jtfDefaultDateStart;
    private javax.swing.JTextField jtfPaymentType;
    private javax.swing.JTextField jtfTaxSubsidyOption;
    private javax.swing.JTabbedPane jtpPayroll;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolClosed;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolFilterAssimilated;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolFilterOthers;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolFilterRetirees;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolFilterWages;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolSsContribution;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateStart;
    private sa.lib.gui.bean.SBeanCompoundFieldCurrency moDecMwzReferenceWage;
    private sa.lib.gui.bean.SBeanCompoundFieldCurrency moDecMwzWage;
    private sa.lib.gui.bean.SBeanCompoundFieldCurrency moDecTotalDeductions;
    private sa.lib.gui.bean.SBeanCompoundFieldCurrency moDecTotalEarnings;
    private sa.lib.gui.bean.SBeanCompoundFieldCurrency moDecTotalNet;
    private sa.lib.gui.bean.SBeanCompoundFieldCurrency moDecUmaAmount;
    private sa.lib.gui.bean.SBeanCompoundFieldCurrency moDecUmiAmount;
    private sa.lib.gui.bean.SBeanFieldInteger moIntFiscalYear;
    private sa.lib.gui.bean.SBeanFieldInteger moIntNumber;
    private sa.lib.gui.bean.SBeanFieldInteger moIntPeriod;
    private sa.lib.gui.bean.SBeanFieldInteger moIntPeriodYear;
    private sa.lib.gui.bean.SBeanFieldInteger moIntReceiptDays;
    private sa.lib.gui.bean.SBeanFieldInteger moIntWorkingDays;
    private sa.lib.gui.bean.SBeanFieldKey moKeyEmploymentSubsidy;
    private sa.lib.gui.bean.SBeanFieldKey moKeyMwzReferenceType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyMwzType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyPaysheetCustomType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyRecruitmentSchemaType;
    private sa.lib.gui.bean.SBeanFieldKey moKeySsContribution;
    private sa.lib.gui.bean.SBeanFieldKey moKeyTax;
    private sa.lib.gui.bean.SBeanFieldKey moKeyTaxComputationType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyTaxSubsidy;
    private sa.lib.gui.bean.SBeanFieldRadio moRadExtraordinary;
    private sa.lib.gui.bean.SBeanFieldRadio moRadNormal;
    private sa.lib.gui.bean.SBeanFieldRadio moRadSpecial;
    private sa.lib.gui.bean.SBeanFieldRadio moRadViewEmployeesActive;
    private sa.lib.gui.bean.SBeanFieldRadio moRadViewEmployeesAll;
    private sa.lib.gui.bean.SBeanFieldText moTextHint;
    private sa.lib.gui.bean.SBeanFieldText moTextNotes;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */
    
    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 672); // aspect ratio: 0.65625 instead of standard 0.625

        jtfPaymentType.setText((String) miClient.getSession().readField(SModConsts.HRSS_TP_PAY, new int[] { mnFormSubtype }, SDbRegistry.FIELD_NAME));
        jtfPaymentType.setCaretPosition(0);

        maPayrollReceiptsDeleted = new ArrayList<>();

        moIntPeriodYear.setIntegerSettings(SGuiUtils.getLabelName(jlYear.getText()), SGuiConsts.GUI_TYPE_INT_CAL_YEAR, true);
        moIntPeriodYear.setMinInteger(2000);
        moIntPeriodYear.setMaxInteger(2100);
        moIntFiscalYear.setIntegerSettings(SGuiUtils.getLabelName(jlFiscalYear.getText()), SGuiConsts.GUI_TYPE_INT_CAL_YEAR, true);
        moIntFiscalYear.setMinInteger(2000);
        moIntFiscalYear.setMaxInteger(2100);
        moIntNumber.setIntegerSettings(SGuiUtils.getLabelName(jlNumber.getText()), SGuiConsts.GUI_TYPE_INT_RAW, true);
        moIntNumber.setMaxInteger(SHrsConsts.YEAR_WEEKS_EXTENDED);
        moDateDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart.getText()), true);
        moDateDateEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd.getText()), true);
        moIntPeriod.setIntegerSettings(SGuiUtils.getLabelName(jlPeriod.getText()), SGuiConsts.GUI_TYPE_INT_CAL_MONTH, true);
        moIntPeriod.setMaxInteger(SHrsConsts.YEAR_MONTHS);
        moIntReceiptDays.setIntegerSettings(SGuiUtils.getLabelName(jlReceiptDays.getText()), SGuiConsts.GUI_TYPE_INT, true);
        moIntWorkingDays.setIntegerSettings(SGuiUtils.getLabelName(jlWorkingDays.getText()), SGuiConsts.GUI_TYPE_INT, true);
        moRadNormal.setBooleanSettings(SGuiUtils.getLabelName(moRadNormal.getText()), false);
        moRadSpecial.setBooleanSettings(SGuiUtils.getLabelName(moRadSpecial.getText()), false);
        moRadExtraordinary.setBooleanSettings(SGuiUtils.getLabelName(moRadExtraordinary.getText()), false);
        moKeyPaysheetCustomType.setKeySettings(miClient, SGuiUtils.getLabelName(jlPaysheetCustomType.getText()), true);
        moKeyRecruitmentSchemaType.setKeySettings(miClient, SGuiUtils.getLabelName(jlRecruitmentSchemaType.getText()), true);
        moTextHint.setTextSettings(SGuiUtils.getLabelName(jlHint.getText()), 10, 0);
        moTextHint.setTextCaseType(0); // preserve case of text in user input
        moTextNotes.setTextSettings(SGuiUtils.getLabelName(jlNotes.getText()), 255, 0);
        moKeyTaxComputationType.setKeySettings(miClient, SGuiUtils.getLabelName(jlTaxComputationType.getText()), true);
        moKeyTax.setKeySettings(miClient, SGuiUtils.getLabelName(jlTax.getText()), true);
        moKeyTaxSubsidy.setKeySettings(miClient, SGuiUtils.getLabelName(jlTaxSubsidy.getText()), true);
        moKeyEmploymentSubsidy.setKeySettings(miClient, SGuiUtils.getLabelName(jlEmploymentSubsidy.getText()), false); // obligatoriness enforced in method validateForm()
        moKeySsContribution.setKeySettings(miClient, SGuiUtils.getLabelName(jlSsContribution.getText()), true);
        moBoolSsContribution.setBooleanSettings(SGuiUtils.getLabelName(moBoolSsContribution.getText()), false);
        moKeyMwzType.setKeySettings(miClient, SGuiUtils.getLabelName(jlMwzType.getText()), true);
        moDecMwzWage.setCompoundFieldSettings(miClient);
        moDecMwzWage.getField().setDecimalSettings(SGuiUtils.getLabelName(jlMwzWage.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, true);
        moKeyMwzReferenceType.setKeySettings(miClient, SGuiUtils.getLabelName(jlMwzReferenceType.getText()), true);
        moDecMwzReferenceWage.setCompoundFieldSettings(miClient);
        moDecMwzReferenceWage.getField().setDecimalSettings(SGuiUtils.getLabelName(jlMwzReferenceWage.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, true);
        moDecUmaAmount.setCompoundFieldSettings(miClient);
        moDecUmaAmount.getField().setDecimalSettings(SGuiUtils.getLabelName(jlUmaAmount.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, true);
        moDecUmiAmount.setCompoundFieldSettings(miClient);
        moDecUmiAmount.getField().setDecimalSettings(SGuiUtils.getLabelName(jlUmiAmount.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, true);
        moBoolClosed.setBooleanSettings(SGuiUtils.getLabelName(moBoolClosed.getText()), false);
        moDecTotalEarnings.setCompoundFieldSettings(miClient);
        moDecTotalEarnings.getField().setDecimalSettings(SGuiUtils.getLabelName(jlTotalEarnings.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        moDecTotalDeductions.setCompoundFieldSettings(miClient);
        moDecTotalDeductions.getField().setDecimalSettings(SGuiUtils.getLabelName(jlTotalDeductions.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        moDecTotalNet.setCompoundFieldSettings(miClient);
        moDecTotalNet.getField().setDecimalSettings(SGuiUtils.getLabelName(jlTotalNet.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        moRadViewEmployeesActive.setBooleanSettings(moRadViewEmployeesActive.getText(), false);
        moRadViewEmployeesAll.setBooleanSettings(moRadViewEmployeesAll.getText(), false);
        moBoolFilterWages.setBooleanSettings(moBoolFilterWages.getText(), false);
        moBoolFilterAssimilated.setBooleanSettings(moBoolFilterAssimilated.getText(), false);
        moBoolFilterRetirees.setBooleanSettings(moBoolFilterRetirees.getText(), false);
        moBoolFilterOthers.setBooleanSettings(moBoolFilterOthers.getText(), false);

        moFields.addField(moIntPeriodYear);
        moFields.addField(moIntFiscalYear);
        moFields.addField(moIntNumber);
        moFields.addField(moDateDateStart);
        moFields.addField(moDateDateEnd);
        moFields.addField(moIntPeriod);
        moFields.addField(moIntReceiptDays); // is read-only
        moFields.addField(moIntWorkingDays); // is read-only
        moFields.addField(moRadNormal);
        moFields.addField(moRadSpecial);
        moFields.addField(moRadExtraordinary);
        moFields.addField(moKeyPaysheetCustomType);
        moFields.addField(moKeyRecruitmentSchemaType);
        moFields.addField(moTextHint);
        moFields.addField(moTextNotes);
        moFields.addField(moKeyTaxComputationType);
        moFields.addField(moKeyTax);
        moFields.addField(moKeyTaxSubsidy);
        moFields.addField(moKeyEmploymentSubsidy);
        moFields.addField(moKeySsContribution);
        moFields.addField(moBoolSsContribution);
        moFields.addField(moKeyMwzType);
        moFields.addField(moDecMwzWage.getField()); // is read-only
        moFields.addField(moKeyMwzReferenceType);
        moFields.addField(moDecMwzReferenceWage.getField()); // is read-only
        moFields.addField(moDecUmaAmount.getField()); // is read-only
        moFields.addField(moDecUmiAmount.getField()); // is read-only
        moFields.addField(moBoolClosed); // is read-only
        moFields.addField(moDecTotalEarnings.getField()); // is read-only
        moFields.addField(moDecTotalDeductions.getField()); // is read-only
        moFields.addField(moDecTotalNet.getField()); // is read-only
        moFields.addField(moRadViewEmployeesActive);
        moFields.addField(moRadViewEmployeesAll);
        moFields.addField(moBoolFilterWages);
        moFields.addField(moBoolFilterAssimilated);
        moFields.addField(moBoolFilterRetirees);
        moFields.addField(moBoolFilterOthers);
        moFields.setFormButton(jbSave);

        jtpPayroll.setSelectedIndex(0);

        moGridPaneEmployeesAvailable = new SGridPaneForm(miClient, SModConsts.HRSX_PAY_REC_EMP, SLibConsts.UNDEFINED, "Empleados disponibles", null) {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "Nombre empleado", 200));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "Número empleado"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Activo"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_ICON_CIRC, "Tipo régimen (empleado)"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Tipo régimen (empleado)"));

                return gridColumnsForm;
            }
        };

        moGridPaneEmployeesAvailable.setForm(null);
        moGridPaneEmployeesAvailable.setPaneFormOwner(null);
        jpReceiptsAvailableEmployees.add(moGridPaneEmployeesAvailable, BorderLayout.CENTER);

        moGridPanePayrollReceipts = new SGridPaneForm(miClient, SModConsts.HRSX_PAY_REC_REC, SLibConsts.UNDEFINED, "Recibos de nómina", null) {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, true, false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "Nombre empleado", 200));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "Número empleado"));
                SGridColumnForm columnForm = new SGridColumnForm(SGridConsts.COL_TYPE_INT_ICON, "Tipo régimen (recibo)");
                columnForm.setCellRenderer(new SGridCellRendererIconCircle());
                gridColumnsForm.add(columnForm);
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Total percepciones $"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Total deducciones $"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Total neto $"));

                return gridColumnsForm;
            }

            @Override
            public void actionRowEdit() {
                try {
                    if (jtTable.getSelectedRowCount() != 1) {
                        miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
                    }
                    else {
                        SRowPayrollEmployee rowReceiptBeingEdited = (SRowPayrollEmployee) moGridPanePayrollReceipts.getSelectedGridRow();
                        SHrsReceipt hrsReceipt = rowReceiptBeingEdited.getHrsReceipt().clone();
                        
                        SDbPayrollReceiptIssue payrollReceiptIssue = hrsReceipt.getPayrollReceipt().getChildPayrollReceiptIssue(); // convenience variable
                        boolean isReceiptEditable = !mbIsReadOnly && (payrollReceiptIssue == null || payrollReceiptIssue.isCfdEditable());
                        
                        if (isReceiptEditable) {
                            hrsReceipt.computeReceipt();
                        }

                        SDialogPayrollReceipt dlgPayrollReceipt = new SDialogPayrollReceipt(miClient, "Recibo de nómina");
                        dlgPayrollReceipt.setValue(SDialogPayrollReceipt.PARAM_IS_EDITABLE, isReceiptEditable);
                        dlgPayrollReceipt.setValue(SModConsts.HRS_PAY_RCP, hrsReceipt);
                        dlgPayrollReceipt.setValue(SModConsts.HRS_EAR, moHrsPayroll.getEarnings());
                        dlgPayrollReceipt.setValue(SModConsts.HRS_DED, moHrsPayroll.getDeductions());
                        dlgPayrollReceipt.setFormVisible(true);

                        if (dlgPayrollReceipt.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                            moHrsPayroll.replaceHrsReceipt((SHrsReceipt) dlgPayrollReceipt.getValue(SModConsts.HRS_PAY_RCP), true);
                            int index = moGridPanePayrollReceipts.getTable().getSelectedRow();
                            populateRowPayrollEmployeesReceipts();
                            moGridPanePayrollReceipts.setSelectedGridRow(index);
                            computeTotals();
                        }
                    }
                }
                catch (Exception e) {
                    SLibUtils.printException(this, e);
                }
            }
        };

        moGridPanePayrollReceipts.setForm(null);
        moGridPanePayrollReceipts.setPaneFormOwner(null);
        jpReceiptsPayrollReceipts.add(moGridPanePayrollReceipts, BorderLayout.CENTER);

        /* 2020-02-21, Sergio Flores: Prevent from preserving user preferences of grids in this dialog.
        mvFormGrids.add(moGridPaneEmployeesAvailable);
        mvFormGrids.add(moGridPanePayrollReceipts);
        */
        
        maEmployeesWithCurrentBonus = new ArrayList<>();
    }

    private void showEmployeesAndReceiptsNumbers() {
        jlTotalAvailables.setText("Empleados disponibles: " + moGridPaneEmployeesAvailable.getTable().getRowCount());
        jlTotalSelected.setText("Recibos de nómina: " + moGridPanePayrollReceipts.getTable().getRowCount());
    }

    private void clearAuxCurrentValues() {
        mnAuxCurrentPeriodYear = 0;
        mnAuxCurrentNumber = 0;
        mnAuxCurrentPeriod = 0;
        mtAuxCurrentDateStart = null;
        mtAuxCurrentDateEnd = null;
    }
    
    private void enableFieldsStatusRelatedToPayroll() {
        boolean enable = true;
        
        try {
            if (mbIsReadOnly) {
                enable = false;
            }
            else {
                for (SHrsReceipt hrsReceipt : moHrsPayroll.getHrsReceipts()) {
                    SDbPayrollReceiptIssue payrollReceiptIssue = hrsReceipt.getPayrollReceipt().getChildPayrollReceiptIssue(); // convenience variable
                    
                    if (payrollReceiptIssue != null && !payrollReceiptIssue.isCfdEditable()) {
                        enable = false;
                        break;
                    }
                }
            }
            
            moTextHint.setEnabled(false);
            jbEditHint.setEnabled(!mbIsReadOnly);
            moTextNotes.setEnabled(!mbIsReadOnly);
            
            moKeyTaxComputationType.setEnabled(enable);
            moKeyTax.setEnabled(enable);
            moKeyTaxSubsidy.setEnabled(enable);
            moKeyEmploymentSubsidy.setEnabled(enable);
            moKeySsContribution.setEnabled(enable);
            moBoolSsContribution.setEnabled(enable);
            moKeyMwzType.setEnabled(enable);
            moKeyMwzReferenceType.setEnabled(enable);
            
            jbTaxSubsidyOptionChange.setEnabled(enable);
            
            jbReceiptCaptureEarnings.setEnabled(enable);
            jbReceiptCaptureDeductions.setEnabled(enable);
            jbLoadPrepayroll.setEnabled(enable);
            jbClearPrepayroll.setEnabled(enable);
            jbAddPayrollBonus.setEnabled(enable);
            
            jbReceiptAdd.setEnabled(!mbIsReadOnly);
            jbReceiptAddAll.setEnabled(!mbIsReadOnly);
            jbReceiptRemove.setEnabled(!mbIsReadOnly);
            jbReceiptRemoveAll.setEnabled(!mbIsReadOnly);
            
            moRadViewEmployeesActive.setEnabled(!mbIsReadOnly);
            moRadViewEmployeesAll.setEnabled(!mbIsReadOnly);
            moBoolFilterWages.setEnabled(!mbIsReadOnly);
            moBoolFilterAssimilated.setEnabled(!mbIsReadOnly);
            moBoolFilterRetirees.setEnabled(!mbIsReadOnly);
            moBoolFilterOthers.setEnabled(!mbIsReadOnly);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private void enableFieldsStatusRelatedToReceipts() {
        moIntPeriodYear.setEditable(false);
        moIntFiscalYear.setEditable(false);
        moIntNumber.setEditable(false);
        moDateDateStart.setEditable(false);
        moDateDateEnd.setEditable(false);
        moIntPeriod.setEditable(false);
        moKeyRecruitmentSchemaType.setEnabled(false);

        if (moGridPanePayrollReceipts.getTable().getRowCount() == 0) {
            jbEditPeriodYear.setEnabled(true);
            jbEditFiscalYear.setEnabled(true);
            jbEditNumber.setEnabled(true);
            jbGetNextNumber.setEnabled(true);
            jbEditDates.setEnabled(true);
            jbEditPeriod.setEnabled(true);
            jbEditRecruitmentSchemaType.setEnabled(true);
            moRadNormal.setEnabled(true);
            moRadSpecial.setEnabled(true);
            moRadExtraordinary.setEnabled(true);
            moKeyPaysheetCustomType.setEnabled(true);
        }
        else {
            jbEditPeriodYear.setEnabled(mbIsBeingCopied);
            jbEditFiscalYear.setEnabled(mbIsBeingCopied);
            jbEditNumber.setEnabled(mbIsBeingCopied);
            jbGetNextNumber.setEnabled(mbIsBeingCopied);
            jbEditDates.setEnabled(mbIsBeingCopied);
            jbEditPeriod.setEnabled(mbIsBeingCopied);
            jbEditRecruitmentSchemaType.setEnabled(mbIsBeingCopied);
            moRadNormal.setEnabled(mbIsBeingCopied);
            moRadSpecial.setEnabled(mbIsBeingCopied);
            moRadExtraordinary.setEnabled(mbIsBeingCopied);
            moKeyPaysheetCustomType.setEnabled(mbIsBeingCopied);
        }
        
        // permanent read-only fields:
        moIntReceiptDays.setEditable(false); 
        moIntWorkingDays.setEditable(false);
        moDecMwzWage.setEditable(false);
        moDecMwzReferenceWage.setEditable(false);
        moDecUmaAmount.setEditable(false);
        moDecUmiAmount.setEditable(false);
        moBoolClosed.setEnabled(false);
        moDecTotalEarnings.setEditable(false);
        moDecTotalDeductions.setEditable(false);
        moDecTotalNet.setEditable(false);
    }
    
    private void setPaysheetTypeId(int type) {
        switch(type) {
            case SModSysConsts.HRSS_TP_PAY_SHT_NOR:
                moRadNormal.setSelected(true);
                break;
            case SModSysConsts.HRSS_TP_PAY_SHT_SPE:
                moRadSpecial.setSelected(true);
                break;
            case SModSysConsts.HRSS_TP_PAY_SHT_EXT:
                moRadExtraordinary.setSelected(true);
                break;
            default:
                moRadNormal.setSelected(true);
        }
    }
    
    private int getPaysheetTypeId() {
        int type = SLibConsts.UNDEFINED;
        
        if (moRadNormal.isSelected()) {
            type = SModSysConsts.HRSS_TP_PAY_SHT_NOR;
        }
        else if (moRadSpecial.isSelected()) {
            type = SModSysConsts.HRSS_TP_PAY_SHT_SPE;
        }
        else if (moRadExtraordinary.isSelected()) {
            type = SModSysConsts.HRSS_TP_PAY_SHT_EXT;
        }
        
        return type;
    }
    
    private void validateRowReceiptRemoval(SRowPayrollEmployee rowReceipt) throws Exception {
        SDbPayrollReceiptIssue payrollReceiptIssue = rowReceipt.getHrsReceipt().getPayrollReceipt().getChildPayrollReceiptIssue(); // convenience variable
        
        if (payrollReceiptIssue != null && !payrollReceiptIssue.isCfdEditable()) {
            throw new Exception("No se puede remover el recibo de '" + rowReceipt.getName() + "', porque " + (payrollReceiptIssue.isCfdStamped() ? "está timbrado" : "no es modificable") + "."); // "no es modificable" = isAnnuled
        }
    }

    private void updatePayroll(final SDbPayroll payroll, final boolean populateReceipts) {
        payroll.setFiscalYear(moIntFiscalYear.getValue());
        payroll.setPeriodYear(moIntPeriodYear.getValue());
        payroll.setPeriod(moIntPeriod.getValue());
        payroll.setNumber(moIntNumber.getValue());
        payroll.setDateStart(moDateDateStart.getValue());
        payroll.setDateEnd(moDateDateEnd.getValue());
        payroll.setCalendarDays_r(mnCalendarDays);
        payroll.setReceiptDays(moIntReceiptDays.getValue());
        payroll.setWorkingDays(moIntWorkingDays.getValue());
        payroll.setUmaAmount(moDecUmaAmount.getField().getValue());
        payroll.setUmiAmount(moDecUmiAmount.getField().getValue());
        payroll.setMwzWage(moDecMwzWage.getField().getValue());
        payroll.setMwzReferenceWage(moDecMwzReferenceWage.getField().getValue());
        payroll.setHint(moTextHint.getValue());
        payroll.setNotes(moTextNotes.getValue());
        payroll.setTaxSubsidy(mbIsWithTaxSubsidy);
        payroll.setSsContribution(moBoolSsContribution.getValue());
        payroll.setFortnightStandard(moModuleConfig.isFortnightStandard());
        //payroll.setAccountingPartial(...); // value set outside this form in another user case
        //payroll.setAccounting(...); // value set outside this form in another user case
        //payroll.setClosed(moBoolClosed.getValue()); // value set outside this form in another user case
        
        payroll.clearPayrollForeingKeys();
        
        payroll.setFkPaymentTypeId(mnFormSubtype);
        payroll.setFkPaysheetTypeId(getPaysheetTypeId());

        if (moKeyPaysheetCustomType.getSelectedIndex() > 0) {
            payroll.setFkPaysheetCustomTypeId(moKeyPaysheetCustomType.getValue()[0]);
        }
        
        if (moKeyRecruitmentSchemaType.getSelectedIndex() > 0) {
            payroll.setFkRecruitmentSchemaTypeId(moKeyRecruitmentSchemaType.getValue()[0]);
        }
        
        if (moKeyMwzType.getSelectedIndex() > 0) {
            payroll.setFkMwzTypeId(moKeyMwzType.getValue()[0]);
        }

        if (moKeyMwzReferenceType.getSelectedIndex() > 0) {
            payroll.setFkMwzReferenceTypeId(moKeyMwzReferenceType.getValue()[0]);
        }
        
        if (moKeyTaxComputationType.getSelectedIndex() > 0) {
            payroll.setFkTaxComputationTypeId(moKeyTaxComputationType.getValue()[0]);
        }

        if (moKeyTax.getSelectedIndex() > 0) {
            payroll.setFkTaxId(moKeyTax.getValue()[0]);
        }

        if (moKeyTaxSubsidy.getSelectedIndex() > 0) {
            payroll.setFkTaxSubsidyId(moKeyTaxSubsidy.getValue()[0]);
        }

        if (moKeyEmploymentSubsidy.getSelectedIndex() > 0) {
            payroll.setFkEmploymentSubsidyId_n(moKeyEmploymentSubsidy.getValue()[0]);
        }

        if (moKeySsContribution.getSelectedIndex() > 0) {
            payroll.setFkSsContributionId(moKeySsContribution.getValue()[0]);
        }
        
        //payroll.setFkUserClosedId(...); // value set outside this form in another user case
        
        if (moHrsPayroll != null) {
            moHrsPayroll.setPayroll(payroll);
            
            if (populateReceipts) {
                populateRowPayrollEmployeesReceipts();
            }
        }
    }
    
    private void computeReceipts() {
        try {
            updatePayroll(moRegistry, true); // update registry with current UI data
            moHrsPayroll.computeReceipts();
            computeTotals();
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void computeTotals() {
        moDecTotalEarnings.getField().setValue(moHrsPayroll.getTotalEarnings());
        moDecTotalDeductions.getField().setValue(moHrsPayroll.getTotalDeductions());
        moDecTotalNet.getField().setValue(SLibUtils.roundAmount(moDecTotalEarnings.getField().getValue() - moDecTotalDeductions.getField().getValue()));
    }

    /**
     * Reset days.
     */
    private void resetPayrollDays() {
        if (moDateDateStart.getValue() != null && moDateDateEnd.getValue() != null) {
            if (moDateDateStart.getValue().compareTo(moDateDateEnd.getValue()) > 0) {
                mnCalendarDays = 0;
            }
            else {
                mnCalendarDays = SLibTimeUtils.countPeriodDays(moDateDateStart.getValue(), moDateDateEnd.getValue());
            }
        }

        if (mnCalendarDays > 0) {
            int workingDays = (mnFormSubtype == SModSysConsts.HRSS_TP_PAY_WEE ? moWorkingDaySettings.getWorkingDaysWeek() : moModuleConfig.isFortnightStandard() ? SHrsConsts.FORTNIGHT_FIXED_DAYS : mnCalendarDays);
            
            moIntReceiptDays.setValue(mnFormSubtype == SModSysConsts.HRSS_TP_PAY_FOR && moModuleConfig.isFortnightStandard() ? SHrsConsts.FORTNIGHT_FIXED_DAYS : mnCalendarDays);
            moIntWorkingDays.setValue(workingDays <= 0 ? 0 : workingDays);
        }
    }
    
    /**
     * Reset salaries amounts.
     * @throws Exception 
     */
    private void resetSalariesAmounts() throws Exception {
        moDecMwzWage.getField().setValue(moKeyMwzType.getSelectedIndex() <= 0 ? 0d : SHrsUtils.getRecentMinimumWage(miClient.getSession(), moKeyMwzType.getValue()[0], moDateDateEnd.getValue()));
        moDecMwzReferenceWage.getField().setValue(moKeyMwzReferenceType.getSelectedIndex() <= 0 ? 0d : SHrsUtils.getRecentMinimumWage(miClient.getSession(), moKeyMwzReferenceType.getValue()[0], moDateDateEnd.getValue()));
    }
    
    /**
     * Reset UMA & UMI amounts.
     * @throws Exception 
     */
    private void resetUmaUmiAmounts() throws Exception {
        moDecUmaAmount.getField().setValue(SHrsUtils.getRecentUma(miClient.getSession(), moDateDateEnd.getValue()));
        moDecUmiAmount.getField().setValue(SHrsUtils.getRecentUmi(miClient.getSession(), moDateDateEnd.getValue()));
    }

    /**
     * Ret withholding tables.
     * @throws Exception 
     */
    private void resetWithholdingTables() throws Exception {
        moKeyTax.setValue(new int[] { SHrsUtils.getRecentTaxTable(miClient.getSession(), moDateDateEnd.getValue()) });
        moKeyTaxSubsidy.setValue(new int[] { SHrsUtils.getRecentTaxSubsidyTable(miClient.getSession(), moDateDateEnd.getValue()) });
        moKeyEmploymentSubsidy.setValue(new int[] { SHrsUtils.getRecentEmploymentSubsidy(miClient.getSession(), moDateDateEnd.getValue()) });
        moKeySsContribution.setValue(new int[] { SHrsUtils.getRecentSsContributionTable(miClient.getSession(), moDateDateEnd.getValue()) });
    }

    private void setDefaultPeriodYear(final int year, final boolean setPeriodAndTriggerResets, final boolean populateEmployeesAvailabe) throws Exception {
        mnDefaultPeriodYear = year;
        
        moIntPeriodYear.setValue(mnDefaultPeriodYear);
        moIntFiscalYear.setValue(mnDefaultPeriodYear);

        mnDefaultNumber = SHrsUtils.getPayrollNextNumber(miClient.getSession(), mnDefaultPeriodYear, mnFormSubtype, getPaysheetTypeId());
        
        moIntNumber.setValue(mnDefaultNumber);

        setDefaultPeriod(mnDefaultPeriodYear, mnDefaultNumber, setPeriodAndTriggerResets, populateEmployeesAvailabe);
    }

    private void setDefaultPeriod(final int year, final int number, final boolean setPeriodAndTriggerResets, final boolean populateEmployeesAvailabe) throws Exception {
        Date[] period = SHrsUtils.getPayrollPeriod(miClient.getSession(), year, number, mnFormSubtype);
        int[] periodEnd = SLibTimeUtils.digestMonth(period[1]);

        if (periodEnd[0] == year) {
            mnDefaultPeriod = periodEnd[1];
        }
        else if (periodEnd[0] == year + 1) {
            int[] periodStart = SLibTimeUtils.digestMonth(period[0]);
            mnDefaultPeriod = periodStart[1]; // on last week period, period end date may belong to next year
        }
        else {
            throw new Exception(SHrsConsts.ERR_PERIOD_DATE_INVALID);
        }

        mtDefaultDateStart = period[0];
        mtDefaultDateEnd = period[1];

        jtfDefaultDateStart.setText(SLibUtils.DateFormatDate.format(mtDefaultDateStart));
        jtfDefaultDateEnd.setText(SLibUtils.DateFormatDate.format(mtDefaultDateEnd));
        
        if (setPeriodAndTriggerResets) {
            moDateDateStart.setValue(mtDefaultDateStart);
            moDateDateEnd.setValue(mtDefaultDateEnd);
            moIntPeriod.setValue(mnDefaultPeriod);

            triggerResets(populateEmployeesAvailabe);
        }
    }
    
    private void applyCurrentPeriodYear() throws Exception {
        setDefaultPeriodYear(moIntPeriodYear.getValue(), true, true);
    }

    private void applyCurrentNumber() throws Exception {
        setDefaultPeriod(moIntPeriodYear.getValue(), moIntNumber.getValue(), true, true);
    }

    private void triggerResets(boolean populateEmployeesAvailabe) throws Exception {
        resetPayrollDays();
        resetSalariesAmounts();
        resetUmaUmiAmounts();
        resetWithholdingTables();
        
        if (populateEmployeesAvailabe) {
            populateRowPayrollEmployeesAvailable(true);
        }
    }

    private void populateRowPayrollEmployeesAvailable(final boolean activesOnly) throws Exception {
        ArrayList<Integer> selectedEmployeesIds = new ArrayList<>();
        
        for (int i = 0; i < moGridPanePayrollReceipts.getModel().getRowCount(); i++) {
            selectedEmployeesIds.add(((SRowPayrollEmployee) moGridPanePayrollReceipts.getGridRow(i)).getPkEmployeeId());
        }

        moGridPaneEmployeesAvailable.populateGrid(new Vector<>(
                SHrsPayrollUtils.obtainRowPayrollEmployeesAvailable(miClient.getSession(), mnFormSubtype, moDateDateStart.getValue(), moDateDateEnd.getValue(), activesOnly, selectedEmployeesIds)));
        
        showEmployeesAndReceiptsNumbers();
    }

    private void populateRowPayrollEmployeesReceipts() {
        moGridPanePayrollReceipts.populateGrid(new Vector<>(
                SHrsPayrollUtils.createRowPayrollEmployees(miClient.getSession(), moHrsPayroll.getHrsReceipts())));
        
        maBonusPayed = new ArrayList<>();
        
        for (SHrsReceipt hrsReceipt : moHrsPayroll.getHrsReceipts()) {
            for (SHrsReceiptEarning hrsReceiptEarning : hrsReceipt.getHrsReceiptEarnings()) {
                int bonus = hrsReceiptEarning.getPayrollReceiptEarning().getFkBonusId();
                if (!maBonusPayed.contains(bonus) && bonus != SModSysConsts.HRSS_BONUS_NA) {
                    maBonusPayed.add(bonus);
                }
            }
        }
    }

    private void actionEditPeriodYear() {
        jbEditPeriodYear.setEnabled(false);
        moIntPeriodYear.setEditable(true);
        moIntPeriodYear.requestFocusInWindow();
    }
    
    private void actionEditFiscalYear() {
        jbEditFiscalYear.setEnabled(false);
        moIntFiscalYear.setEditable(true);
        moIntFiscalYear.requestFocusInWindow();
    }
    
    private void actionEditNumber() {
        jbEditNumber.setEnabled(false);
        moIntNumber.setEditable(true);
        moIntNumber.requestFocusInWindow();
    }

    private void actionGetNextNumber() throws Exception {
        applyCurrentPeriodYear();
        moIntNumber.requestFocusInWindow();
    }

    private void actionEditDates() {
        jbEditDates.setEnabled(false);
        moDateDateStart.setEditable(true);
        moDateDateEnd.setEditable(true);
        moDateDateStart.requestFocusInWindow();
    }

    private void actionEditPeriod() {
        jbEditPeriod.setEnabled(false);
        moIntPeriod.setEditable(true);
        moIntPeriod.requestFocusInWindow();
    }
    
    private void actionEditRecruitmentSchemaType() {
        jbEditRecruitmentSchemaType.setEnabled(false);
        moKeyRecruitmentSchemaType.setEnabled(true);
        moKeyRecruitmentSchemaType.requestFocusInWindow();
    }
    
    private void actionEditHint() {
        jbEditHint.setEnabled(false);
        moTextHint.setEditable(true);
        moTextHint.requestFocusInWindow();
    }

    private void updateTaxSubsidyOptionText() {
        jtfTaxSubsidyOption.setText(mbIsWithTaxSubsidy ? TXT_WITH_TAX_SUB_PAY : TXT_WITHOUT_TAX_SUB_PAY);
        jtfTaxSubsidyOption.setCaretPosition(0);
    }
    
    private void actionTaxSubsidyChange() {
        boolean changed = false;
        
        if (mbIsWithTaxSubsidy) {
            if (moHrsPayroll.getHrsReceipts().isEmpty() || miClient.showMsgBoxConfirm("Se removerá el pago de subsidio para el empleo a todos los recibos que lo tengan.\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                mbIsWithTaxSubsidy = false;
                changed = true;
            }
        }
        else {
            if (moHrsPayroll.getHrsReceipts().isEmpty() || miClient.showMsgBoxConfirm("Se agregará el pago de subsidio para el empleo a todos los recibos que lo requieran.\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                mbIsWithTaxSubsidy = true;
                changed = true;
            }
        }

        if (changed) {
            updateTaxSubsidyOptionText();
            computeReceipts();
        }
    }

    private void actionGoTabReceipts() {
        mbIsGoingToReceipts = true; // yes!, believe it!, going to receipts!
        SGuiValidation validation = validateForm();

        if (!SGuiUtils.computeValidation(miClient, validation)) {
            jtpPayroll.setSelectedIndex(0);
        }
        else {
            jtpPayroll.setSelectedIndex(1);

            if (!mbIsReadOnly) {
                updatePayroll(moRegistry, true); // update registry with current UI data
            }
        }
    }
    
    private void itemStateChangedPaysheetType() {
        moBoolSsContribution.setSelected(moRadNormal.isSelected() || moRadExtraordinary.isSelected());
        
        // update payroll period and number:
        
        if (jbEditNumber.isEnabled()) {
            try {
                applyCurrentPeriodYear();
            }
            catch (Exception ex) {
                SLibUtils.printException(this, ex);
            }
        }
    }

    private void itemStateChangedPaysheetCustomType() {
        if (moKeyPaysheetCustomType.getSelectedIndex() <= 0) {
            moTextHint.resetField();
        }
        else {
            SDbPaysheetCustomType type = (SDbPaysheetCustomType) miClient.getSession().readRegistry(SModConsts.HRSU_TP_PAY_SHT_CUS, moKeyPaysheetCustomType.getValue());
            moTextHint.setValue(type.getHint());
        }
    }
    
    private void itemStateChangedEmployeesViewActive() {
        try {
            populateRowPayrollEmployeesAvailable(true);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private void itemStateChangedEmployeesViewAll() {
        try {
            populateRowPayrollEmployeesAvailable(false);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private HashSet<Integer> createRecruitmentSchemaTypesSet() {
        HashSet<Integer> recruitmentSchemaTypesSet = new HashSet<>();

        if (moBoolFilterWages.isSelected()) {
            for (int type : SHrsUtils.RecruitmentSchemaTypesForWages) {
                recruitmentSchemaTypesSet.add(type);
            }
        }
        
        if (moBoolFilterAssimilated.isSelected()) {
            for (int type : SHrsUtils.RecruitmentSchemaTypesForAssimilated) {
                recruitmentSchemaTypesSet.add(type);
            }
        }
        
        if (moBoolFilterRetirees.isSelected()) {
            for (int type : SHrsUtils.RecruitmentSchemaTypesForRetirees) {
                recruitmentSchemaTypesSet.add(type);
            }
        }
        
        if (moBoolFilterOthers.isSelected()) {
            for (int type : SHrsUtils.RecruitmentSchemaTypesForOthers) {
                recruitmentSchemaTypesSet.add(type);
            }
        }
        
        return recruitmentSchemaTypesSet;
    }

    /**
     * Add receipt.
     * @param recruitmentSchemaTypesSet Set of types of recruitment schemas. Can be <code>null</code>.
     * @return <code>true</code> if receipt added, otherwise <code>false</code>.
     */
    private boolean actionReceiptAdd(final HashSet<Integer> recruitmentSchemaTypesSet) {
        boolean added = true;

        if (mbIsReadOnly) {
            miClient.showMsgBoxWarning("No se puede agregar el empleado, la nómina es de solo lectura.");
        }
        else if (moGridPaneEmployeesAvailable.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(SGridConsts.MSG_SELECT_ROW);
            moGridPaneEmployeesAvailable.getTable().requestFocusInWindow();
        }
        else if (recruitmentSchemaTypesSet != null && recruitmentSchemaTypesSet.isEmpty()) {
            miClient.showMsgBoxInformation(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlFilter) + ": (" + jlFilter.getToolTipText() + ")'.");
            moBoolFilterWages.requestFocusInWindow();
        }
        else if (recruitmentSchemaTypesSet != null && !recruitmentSchemaTypesSet.contains(((SRowPayrollEmployee) moGridPaneEmployeesAvailable.getSelectedGridRow()).getEmployeeRecruitmentSchemaTypeId())) {
            miClient.showMsgBoxInformation("¡No se agregó el empleado, no cumple con los filtros seleccionados!");
            moBoolFilterWages.requestFocusInWindow();
        }
        else {
            try {
                updatePayroll(moRegistry, false); // update registry with current UI data
                
                int index = moGridPaneEmployeesAvailable.getTable().getSelectedRow();
                int recruitmentSchemaType = moKeyRecruitmentSchemaType.getValue()[0];
                SRowPayrollEmployee rowEmployee = (SRowPayrollEmployee) moGridPaneEmployeesAvailable.getSelectedGridRow();
                SHrsReceipt hrsReceipt = moHrsPayroll.createHrsReceipt(rowEmployee.getPkEmployeeId(), recruitmentSchemaType);

                // recover receipt if it was already deleted:
                
                for (int i = 0; i < maPayrollReceiptsDeleted.size(); i++) {
                    SDbPayrollReceipt payrollReceipt = maPayrollReceiptsDeleted.get(i);

                    if (hrsReceipt.getPayrollReceipt().getPkEmployeeId() == payrollReceipt.getPkEmployeeId()) {
                        hrsReceipt.getPayrollReceipt().setRegistryNew(false);
                        maPayrollReceiptsDeleted.remove(i);
                        break;
                    }
                }
                
                // add receipt:
                
                moGridPaneEmployeesAvailable.removeGridRow(moGridPaneEmployeesAvailable.getTable().getSelectedRow());
                moGridPaneEmployeesAvailable.renderGridRows();
                moGridPaneEmployeesAvailable.setSelectedGridRow(index < moGridPaneEmployeesAvailable.getModel().getRowCount() ? index : moGridPaneEmployeesAvailable.getModel().getRowCount() - 1);

                SRowPayrollEmployee rowReceiptBeingAdded = new SRowPayrollEmployee(rowEmployee);
                rowReceiptBeingAdded.setRowCase(SRowPayrollEmployee.CASE_RECEIPT);
                rowReceiptBeingAdded.setTotalEarnings(hrsReceipt.getTotalEarnings());
                rowReceiptBeingAdded.setTotalDeductions(hrsReceipt.getTotalDeductions());
                rowReceiptBeingAdded.setHrsReceipt(hrsReceipt);
                
                if (recruitmentSchemaType != SModSysConsts.HRSS_TP_REC_SCHE_NA && recruitmentSchemaType != rowReceiptBeingAdded.getReceiptRecruitmentSchemaTypeId()) {
                    String code = (String) miClient.getSession().readField(SModConsts.HRSS_TP_REC_SCHE, new int[] { recruitmentSchemaType }, SDbRegistry.FIELD_CODE);
                    String name = (String) miClient.getSession().readField(SModConsts.HRSS_TP_REC_SCHE, new int[] { recruitmentSchemaType }, SDbRegistry.FIELD_NAME);
                    rowReceiptBeingAdded.setReceiptRecruitmentSchemaTypeId(recruitmentSchemaType);
                    rowReceiptBeingAdded.setReceiptRecruitmentSchemaType(code + " - " + name);
                }

                moGridPanePayrollReceipts.addGridRow(rowReceiptBeingAdded);
                moGridPanePayrollReceipts.renderGridRows();
                moGridPanePayrollReceipts.setSelectedGridRow(moGridPanePayrollReceipts.getTable().convertRowIndexToView(moGridPanePayrollReceipts.getModel().getRowCount() - 1));

                enableFieldsStatusRelatedToReceipts();
                computeTotals();
                showEmployeesAndReceiptsNumbers();
            }
            catch (Exception e) {
                added = false;
                SLibUtils.showException(this, e);
            }
        }

        return added;
    }
    
    private void actionReceiptAddAll() {
        int rows = moGridPaneEmployeesAvailable.getTable().getRowCount();
        
        if (rows > 0) {
            // prepare filters of types of recruitment schemas:
            
            HashSet<Integer> recruitmentSchemaTypesSet = createRecruitmentSchemaTypesSet();

            if (recruitmentSchemaTypesSet.isEmpty()) {
                miClient.showMsgBoxInformation(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlFilter) + "'.");
                moBoolFilterWages.requestFocusInWindow();
            }
            else {
                int processed = 0;
                
                try {
                    jtpPayroll.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    
                    // add all available employees:

                    int index = 0;

                    for (int row = 0; row < rows; row++) {
                        // select top row:
                        moGridPaneEmployeesAvailable.setSelectedGridRow(index);

                        if (!recruitmentSchemaTypesSet.contains(((SRowPayrollEmployee) moGridPaneEmployeesAvailable.getSelectedGridRow()).getEmployeeRecruitmentSchemaTypeId())) {
                            index++; // skip current row and go next
                        }
                        else {
                            if (actionReceiptAdd(null)) {
                                processed++;
                            }
                            else {
                                index++; // skip current row and go next
                            }
                        }
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
                finally {
                    jtpPayroll.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }

                if (processed == 0) {
                    miClient.showMsgBoxWarning("¡No se agregó ningún empleado!");
                }
            }
        }
    }

    private boolean actionReceiptRemove() {
        boolean removed = true;
        int employeeId = 0;
        
        if (mbIsReadOnly) {
            miClient.showMsgBoxWarning("No se puede remover el recibo, la nómina es de solo lectura.");
        }
        else if (moGridPanePayrollReceipts.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(SGridConsts.MSG_SELECT_ROW);
            moGridPanePayrollReceipts.getTable().requestFocusInWindow();
        }
        else {
            try {
                int index = moGridPanePayrollReceipts.getTable().getSelectedRow();
                SRowPayrollEmployee rowReceiptBeingRemoved = (SRowPayrollEmployee) moGridPanePayrollReceipts.getSelectedGridRow();
                
                validateRowReceiptRemoval(rowReceiptBeingRemoved);
                
                if (!rowReceiptBeingRemoved.getHrsReceipt().getPayrollReceipt().isRegistryNew()) {
                    maPayrollReceiptsDeleted.add(rowReceiptBeingRemoved.getHrsReceipt().getPayrollReceipt());
                }
                moHrsPayroll.removeHrsReceipt(rowReceiptBeingRemoved.getPkEmployeeId());
                employeeId = rowReceiptBeingRemoved.getPkEmployeeId();

                moGridPanePayrollReceipts.removeGridRow(moGridPanePayrollReceipts.getTable().getSelectedRow());
                moGridPanePayrollReceipts.renderGridRows();
                moGridPanePayrollReceipts.setSelectedGridRow(index < moGridPanePayrollReceipts.getModel().getRowCount() ? index : moGridPanePayrollReceipts.getModel().getRowCount() - 1);
                
                if (mnFormSubtype == rowReceiptBeingRemoved.getFkPaymentTypeId()) {
                    // payment type of receipt being removed matches payment type of current payroll
                    
                    SRowPayrollEmployee rowEmployeeToAdd = new SRowPayrollEmployee(rowReceiptBeingRemoved);
                    rowEmployeeToAdd.setRowCase(SRowPayrollEmployee.CASE_EMPLOYEE);
                    rowEmployeeToAdd.clearReceipt();

                    moGridPaneEmployeesAvailable.addGridRow(rowEmployeeToAdd);
                    moGridPaneEmployeesAvailable.renderGridRows();
                    moGridPaneEmployeesAvailable.setSelectedGridRow(moGridPaneEmployeesAvailable.getTable().convertRowIndexToView(moGridPaneEmployeesAvailable.getModel().getRowCount() - 1));
                }
                
                enableFieldsStatusRelatedToReceipts();
                computeTotals();
                showEmployeesAndReceiptsNumbers();
            }
            catch (Exception e) {
                removed = false;
                SLibUtils.showException(this, e);
            }
        }
        
        if (removed) {
            int rows = moGridPanePayrollReceipts.getTable().getRowCount();
            if (maBonusPayed.contains(SPayrollBonusUtils.BONUS) && employeeId > 0 && maEmployeesWithCurrentBonus.contains(employeeId)) {
                maEmployeesWithCurrentBonus.remove(employeeId);
            }
            if (rows == 0) {
                maBonusPayed.clear();
                maEmployeesWithCurrentBonus.clear();
            }
        }

        return removed;
    }

    private void actionReceiptRemoveAll() {
        int rows = moGridPanePayrollReceipts.getTable().getRowCount();
        
        if (rows > 0) {
            int processed = 0;
            
            try {
                jtpPayroll.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                
                // remove all payroll receipts:

                int index = 0;

                for (int row = 0; row < rows; row++) {
                    // select top row:
                    moGridPanePayrollReceipts.setSelectedGridRow(index);

                    if (actionReceiptRemove()) {
                        processed++;
                    }
                    else {
                        index++; // skip current row and go next
                    }
                }
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
            finally {
                jtpPayroll.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
            if (processed == 0) {
                miClient.showMsgBoxWarning("¡No se removió ningún recibo!");
            }
            
            maBonusPayed.clear();
            maEmployeesWithCurrentBonus.clear();
        }
    }

    @SuppressWarnings("unchecked")
    private void actionReceiptCaptureEarnings() {
        try {
            ArrayList<SHrsReceipt> hrsReceipts = new ArrayList<>();

            for (int i = 0; i < moGridPanePayrollReceipts.getTable().getRowCount(); i++) {
                SRowPayrollEmployee rowReceipt = (SRowPayrollEmployee) moGridPanePayrollReceipts.getGridRow(i);
                hrsReceipts.add(rowReceipt.getHrsReceipt().clone());
            }

            SDialogPayrollEarnings dlgPayrollEarnings = new SDialogPayrollEarnings(miClient, "Capturar percepciones");
            dlgPayrollEarnings.setValue(SModConsts.HRS_PAY, moHrsPayroll);
            dlgPayrollEarnings.setValue(SModConsts.HRS_PAY_RCP, hrsReceipts);
            dlgPayrollEarnings.setFormVisible(true);

            if (dlgPayrollEarnings.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                for (SHrsReceipt hrsReceipt : (ArrayList<SHrsReceipt>) dlgPayrollEarnings.getValue(SModConsts.HRS_PAY_RCP)) {
                    moHrsPayroll.replaceHrsReceipt(hrsReceipt, true);
                }
                populateRowPayrollEmployeesReceipts();
                computeTotals();
            }
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void actionReceiptCaptureDeductions() {
        try {
            ArrayList<SHrsReceipt> hrsReceipts = new ArrayList<>();

            for (int i = 0; i < moGridPanePayrollReceipts.getTable().getRowCount(); i++) {
                SRowPayrollEmployee rowReceipt = (SRowPayrollEmployee) moGridPanePayrollReceipts.getGridRow(i);
                hrsReceipts.add(rowReceipt.getHrsReceipt().clone());
            }

            SDialogPayrollDeductions dlgPayrollDeductions = new SDialogPayrollDeductions(miClient, "Capturar deducciones");
            dlgPayrollDeductions.setValue(SModConsts.HRS_PAY, moHrsPayroll);
            dlgPayrollDeductions.setValue(SModConsts.HRS_PAY_RCP, hrsReceipts);
            dlgPayrollDeductions.setFormVisible(true);

            if (dlgPayrollDeductions.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                for (SHrsReceipt hrsReceipt : (ArrayList<SHrsReceipt>) dlgPayrollDeductions.getValue(SModConsts.HRS_PAY_RCP)) {
                    moHrsPayroll.replaceHrsReceipt(hrsReceipt, true);
                }
                populateRowPayrollEmployeesReceipts();
                computeTotals();
            }
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    /**
     * Llama al método que realiza la ptición al checador y muestra en pantalla lo que ha obtenido
     */
    private void actionLoadPrepayroll() {
        HashMap<Integer, SRowPayrollEmployee> selectedEmployeesIds = new HashMap<>();
        
        for (int i = 0; i < moGridPanePayrollReceipts.getModel().getRowCount(); i++) {
            SRowPayrollEmployee row = (SRowPayrollEmployee) moGridPanePayrollReceipts.getGridRow(i);
            selectedEmployeesIds.put(row.getPkEmployeeId(), row);
        }
        
        if (selectedEmployeesIds.isEmpty()) {
            miClient.showMsgBoxError("No hay empleados seleccionados");
            return;
        }
        
        if (moRegistry.getPkPayrollId() == 0) {
            if (miClient.showMsgBoxConfirm("Debe guardar la nómina para continuar.\n"
                    + "¿Desea guardarla y abrirla de nuevo?") == JOptionPane.YES_OPTION) {
                mbAuxReopen = true;
                actionSave();
            }
            
            return;
        }
        
        ArrayList<Integer> list = new ArrayList<>();
        list.addAll(selectedEmployeesIds.keySet());
        
        Date dates[] = null;
        if (mnFormSubtype == SModSysConsts.HRSS_TP_PAY_WEE) {
            int cutDay = moModuleConfig.getPrePayrollWeeklyCutoffDayWeek();
            int weekLag = moModuleConfig.getPrePayrollWeeklyWeeksLag();
            
            if (cutDay == 0) {
                miClient.showMsgBoxError("No existe configuración para día de corte");
                return;
            }
            
            dates = SPrepayrollUtils.getPrepayrollDateRangeByCutDay(cutDay, moDateDateEnd.getValue(), weekLag);
        }
        else {
            dates = SPrepayrollUtils.getPrepayrollDateRangeByTable(miClient, mnFormSubtype, moIntPeriodYear.getValue(), moIntNumber.getValue());
            if (dates == null || dates[0] == null || dates[1] == null) {
                miClient.showMsgBoxError("No se pudieron obtener fechas para prenómina");
                return;
            }
        }
        
        try {
            SGuiUtils.setCursorWait(miClient);
            
            SDataCompany company = (SDataCompany) SDataUtilities.readRegistry((SClientInterface) miClient, 
                                                        SDataConstants.CFGU_CO, new int[] { miClient.getSession().getConfigCompany().getCompanyId()}, 
                                                        SLibConstants.EXEC_MODE_SILENT);
            String sCompanyKey = company.getKey();

            String urls = "";
            String url = "";
            try {
                //localhost:8080/CAP/public/api/prepayroll

                urls = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_HRS_CAP);
                String arrayUrls[] = urls.split(";");
                url = arrayUrls[0];
            }
            catch (Exception ex) {
                Logger.getLogger(SFormPayroll.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (miClient.showMsgBoxConfirm("Se cargará la prenómina correspondiente al periodo: " + 
                    SLibUtils.DateFormatDate.format(dates[0]) + " - " + SLibUtils.DateFormatDate.format(dates[1]) + 
                    ".\n¿Desea continuar?\nEste proceso puede demorar algunos minutos.") != JOptionPane.YES_OPTION) {
                return;
            }

            SShareData sd = new SShareData();
            SPrepayroll ppayroll = sd.getCAPData(url, dates[0], dates[1], list, mnFormSubtype, moModuleConfig.getTimeClockPolicy(), sCompanyKey);

            if (ppayroll == null) {
                miClient.showMsgBoxError("Ocurrió un error al importar la prenómina");
                return;
            }

            // Acomodar la lista para los periodos distintos de faltas y percepciones
            if (mnFormSubtype == SModSysConsts.HRSS_TP_PAY_WEE && moModuleConfig.getPrePayrollWeeklyCutoffDayWeek() != moModuleConfig.PrePayrollWeeklyVarCutoffDayWeek()) {
                int cutDay = moModuleConfig.PrePayrollWeeklyVarCutoffDayWeek();
                int weekLag = moModuleConfig.PrePayrollWeeklyVarWeeksLag();
                Date datesVar[] = null;

                if (cutDay == 0) {
                    miClient.showMsgBoxError("No existe configuración para día de corte alterno");
                    return;
                }

                // obtener faltas
                datesVar = SPrepayrollUtils.getPrepayrollDateRangeByCutDay(cutDay, moDateDateEnd.getValue(), weekLag);
                SPrepayroll ppayrollVar = sd.getCAPData(url, datesVar[0], datesVar[1], list, mnFormSubtype, moModuleConfig.getTimeClockPolicy(), sCompanyKey);

                if (ppayrollVar == null) {
                    miClient.showMsgBoxError("Ocurrió un problema al realizar la petición al sistema externo.");
                    return;
                }

                for (SPrepayrollRow row : ppayroll.getRows()) {
                    for (SPrepayrollRow rowVar : ppayrollVar.getRows()) {
                        if (!SPrepayrollUtils.isNumber(rowVar.getDouble_overtime()) || !SPrepayrollUtils.isNumber(rowVar.getTriple_overtime())) {
                            miClient.showMsgBoxError("El valor de la etiqueta de tiempo extra no es correcto (" + rowVar.getDouble_overtime() + ", " + rowVar.getTriple_overtime() + "), "
                                    + "por favor contacte a soporte técnico.");
                            return;
                        }
                        if (row.getEmployee_id() == rowVar.getEmployee_id()) {
                            row.getDays().clear();
                            row.getDays().addAll(rowVar.getDays());
                            break;
                        }
                    }
                }
            }
            
            SGuiUtils.setCursorDefault(miClient);

            // Vista previa de la importación
            SDialogTimeClockImport dialog = new SDialogTimeClockImport(miClient, "Importación de prenómina desde reloj checador");

            dialog.setlPpRows(ppayroll.getRows());
            dialog.setlReceiptRows(selectedEmployeesIds);
            dialog.setStartDate(SLibUtils.DbmsDateFormatDate.format(dates[0]));
            dialog.setEndDate(SLibUtils.DbmsDateFormatDate.format(dates[1]));
            dialog.setPrepayrollMode(moModuleConfig.getTimeClockPolicy());
            dialog.setCutOffDay(moModuleConfig.getPrePayrollWeeklyCutoffDayWeek());
            dialog.setCompanyKey(sCompanyKey);
            dialog.initView();
            dialog.setVisible(true);

            if (dialog.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                removeByImportation(false);
                ArrayList<SRowTimeClock> rows = dialog.getlGridRows();
                addPerceptAndDeductByImportation(rows, ppayroll.getRows());

                computeReceipts();
                populateRowPayrollEmployeesReceipts();
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            SGuiUtils.setCursorDefault(miClient);
        }
    }
    
    /**
     * Obtiene los datos de la prenómina de los renglones recibidos desde CAP
     * 
     * @param lImportedprepayrollRows@param selectedEmployeesIds
     * @param ppRows 
     */
    private void addPerceptAndDeductByImportation(ArrayList<SRowTimeClock> timeClockRows, List<SPrepayrollRow> ppRows) throws Exception {
        boolean configured = true;

        if (moModuleConfig.getFkEarningHolidayId_n() == 0) {
            miClient.showMsgBoxError("No se ha configurado en este módulo la percepción de días festivos.");
            configured = false;
        }
        if (moModuleConfig.getFkEarningOvertime2Id_n() == 0) {
            miClient.showMsgBoxError("No se ha configurado en este módulo la percepción de horas extras.");
            configured = false;
        }
        if (moModuleConfig.getFkEarningOvertime3Id_n() == 0) {
            miClient.showMsgBoxError("No se ha configurado en este módulo la percepción de horas extras triples.");
            configured = false;
        }
        if (moModuleConfig.getFkEarningDayOffId_n() == 0) {
            miClient.showMsgBoxError("No se ha configurado en este módulo la percepción de días de descanso trabajados.");
            configured = false;
        }

        if (!configured) {
            return;
        }

        for (SRowTimeClock timeClockRow : timeClockRows) {
            SHrsReceipt receipt = null;
            boolean receiptFound = false;

            for (int i = 0; i < moGridPanePayrollReceipts.getModel().getRowCount(); i++) {
                SRowPayrollEmployee row = (SRowPayrollEmployee) moGridPanePayrollReceipts.getGridRow(i);
                if (timeClockRow.getEmployeeId() == row.getPkEmployeeId()) {
                    receipt = row.getHrsReceipt();
                    receiptFound = true;
                    break;
                }
            }
            if (! receiptFound) {
                throw new Exception("El empleado " + timeClockRow.getEmployee() + " no se pudo procesar, intente de nuevo la carga de prenómina.");
            }

            double doubleOvertimeValue = ((Number) timeClockRow.getOvertime()).doubleValue();

            if (doubleOvertimeValue > 0d) {
                int perceptionId = 0;
                double factor = doubleOvertimeValue;

                if (moModuleConfig.getTimeClockPolicy() == SHrsConsts.PPAYROLL_POL_LIMITED_DATA) {
                    perceptionId = moModuleConfig.getFkEarningOvertime2Id_n();
                }
                else {
                    perceptionId = moModuleConfig.getFkEarningOvertime3Id_n();
                }

                addPerception(receipt, perceptionId, factor, true, 0);
            }

            if (timeClockRow.getHolidays() > 0) {
                addPerception(receipt, moModuleConfig.getFkEarningHolidayId_n(), timeClockRow.getHolidays(), true, 0);
            }

            if (timeClockRow.getSundays() > 0) {
                addPerception(receipt, moModuleConfig.getFkEarningSunBonusId_n(), timeClockRow.getSundays(), true, 0);
            }

            if (timeClockRow.getDaysOff() > 0) {
                addPerception(receipt, moModuleConfig.getFkEarningDayOffId_n(), timeClockRow.getDaysOff(), true, 0);
            }

            if (timeClockRow.getAbsences() > 0) {
                SPrepayrollRow row = null;

                for (SPrepayrollRow ppRow : ppRows) {
                    if (ppRow.getEmployee_id() == timeClockRow.getEmployeeId()) {
                        row = ppRow;
                        break;
                    }
                }

                adjustNormalPerception(receipt, row);
            }
        }
    }
    
    /**
     * Ajusta la percepción normal en base a las faltas que el CAP haya detectado
     * 
     * @param receipt
     * @param ppRow 
     */
    private void adjustNormalPerception(SHrsReceipt receipt, SPrepayrollRow ppRow) {
        for (SHrsReceiptEarning hrsReceiptEarning : receipt.getHrsReceiptEarnings()) {
            if (hrsReceiptEarning.getEarning().getPkEarningId() == moModuleConfig.getFkEarningEarningId_n()) {
                double units = hrsReceiptEarning.getPayrollReceiptEarning().getUnitsAlleged();

                if (units >= ppRow.getAbsences()) {
                    hrsReceiptEarning.getPayrollReceiptEarning().setTimeClockSourced(true);
                    hrsReceiptEarning.getPayrollReceiptEarning().setUnitsAlleged(units - ppRow.getAbsences());

                    double unitsPayed = hrsReceiptEarning.getEarning().computeEarningUnits(units - ppRow.getAbsences(), moRegistry);
                    hrsReceiptEarning.getPayrollReceiptEarning().setUnits(unitsPayed);
                }
            }
        }
    }
    
    /**
     * Agregar Percepción
     * 
     * @param receipt
     * @param earningId
     * @param dFactor 
     */
    private void addPerception(SHrsReceipt receipt, int earningId, double dFactor, boolean sourcedByClock, int bonusId) {
        SDbEarning earning = (SDbEarning) miClient.getSession().readRegistry(SModConsts.HRS_EAR, new int[] { earningId });

        double unitsAlleged = 0d;
        double amountUnitAlleged = 0d;

        if (earning.isBasedOnUnits()) {
            unitsAlleged = dFactor;
            amountUnitAlleged = 0d;
        }
        else {
            unitsAlleged = 1;
            amountUnitAlleged = dFactor;
        }

        SDbPayrollReceiptEarning prearning = receipt.getHrsPayroll().createPayrollReceiptEarning(
                receipt, earning, null, 
                unitsAlleged, amountUnitAlleged, false, 
                0, 0, receipt.getHrsReceiptEarnings().size() + 1);

        // consider specialized inputs:
        prearning.setTimeClockSourced(sourcedByClock);
        prearning.setFkOtherPaymentTypeId(0);
        prearning.setAuxiliarValue(0d);
        prearning.setAuxiliarAmount1(0d);
        prearning.setAuxiliarAmount2(0d);
        prearning.setFkBonusId(bonusId > 1 ? bonusId : 1);

        SHrsReceiptEarning hrsReceiptEarning = new SHrsReceiptEarning();
        hrsReceiptEarning.setHrsReceipt(receipt);
        hrsReceiptEarning.setEarning(earning);
        hrsReceiptEarning.setPayrollReceiptEarning(prearning);

        receipt.addHrsReceiptEarning(hrsReceiptEarning);
    }
    
    /**
     * "Limpia" los datos importados desde CAP para solo dejar la nómina tal cual la realiza siie
     * 
     * @param selectedEmployeesIds 
     */
    private void removeByImportation(boolean showMessage) {
        if (mbIsReadOnly) {
            miClient.showMsgBoxWarning("No se puede agregar el empleado, la nómina es de solo lectura.");
            return;
        }
        
        ArrayList<SRowPayrollEmployee> lRowAux = new ArrayList<>();
        for (int i = 0; i < moGridPanePayrollReceipts.getModel().getRowCount(); i++) {
            SRowPayrollEmployee row = (SRowPayrollEmployee) moGridPanePayrollReceipts.getGridRow(i);
            int moveId = -1;
            boolean again;
            
            // remover deducciones
            again = true;
            while (again) {
                moveId = -1;
                for (SHrsReceiptDeduction hrsReceiptDeductionToRemove : row.getHrsReceipt().getHrsReceiptDeductions()) {
                    if (hrsReceiptDeductionToRemove.getPayrollReceiptDeduction().isTimeClockSourced()) {
                        moveId = hrsReceiptDeductionToRemove.getPayrollReceiptDeduction().getPkMoveId();
                        break;
                    }
                }

                if (moveId > -1) {
                    row.getHrsReceipt().removeHrsReceiptDeduction(moveId);
                }
                else {
                    again = false;
                }
            }
            
            again = true;
            // remover percepciones
            while (again) {
                moveId = -1;
                boolean hasNormalPerception = false;
                for (SHrsReceiptEarning hrsReceiptEarningToRemove : row.getHrsReceipt().getHrsReceiptEarnings()) {
                    if (hrsReceiptEarningToRemove.getPayrollReceiptEarning().isTimeClockSourced()) {
                        moveId = hrsReceiptEarningToRemove.getPayrollReceiptEarning().getPkMoveId();
                        if (hrsReceiptEarningToRemove.getEarning().getPkEarningId() == moModuleConfig.getFkEarningEarningId_n()) {
                            lRowAux.add(row);
                            hasNormalPerception = true;
                        }
                        break;
                    }
                }
                
                /**
                 * Cuando el recibo tiene modificada la percepción normal se opta por eliminar las percepciones
                 * y agregarlas de nuevo.
                 */
                if (hasNormalPerception) {
                    break;
                }

                if (moveId > -1) {
                    row.getHrsReceipt().removeHrsReceiptEarning(moveId);
                }
                else {
                    again = false;
                }
            }
        }
        
        // por cada recibo con la percepción normal modificada se resetean las percepciones
        for (SRowPayrollEmployee oRowAux : lRowAux) {
            try {
                if (oRowAux != null && oRowAux.getHrsReceipt() != null) {
                    oRowAux.getHrsReceipt().getHrsReceiptEarnings().clear();
                    oRowAux.getHrsReceipt().getHrsReceiptEarnings().addAll(moHrsPayroll.createHrsReceiptEarnings(oRowAux.getHrsReceipt(), moDateDateStart.getValue(), moDateDateEnd.getValue()));
                }
            }
            catch (Exception ex) {
                Logger.getLogger(SFormPayroll.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (showMessage) {
            miClient.showMsgBoxInformation("Realizado");
        }
    }
    
    private void actionClearPrepayroll() {
        boolean showMessage = true;
        removeByImportation(showMessage);
        computeReceipts();
    }
    
    private void actionAddPayrollBonus() {
        SDialogPayrollConditionalEarnings dialog = new SDialogPayrollConditionalEarnings(miClient, "Bonos de nómina");
        dialog.setFormVisible(true);
        
        if (dialog.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
            ArrayList<Integer> bonusIds = dialog.getBonusIds();

            for (Integer bonusId : bonusIds) {
                if (maBonusPayed.contains(bonusId)) {
                    miClient.showMsgBoxError("Este bono ya está contemplado en la nómina.\n"
                            + "Si desea calcularlo de nuevo, pase todos los recibos al lado izquierdo e inténtelo de nuevo.");
                    return;
                }
            }

            ArrayList<Integer> empIds = new ArrayList<>();

            for (int i = 0; i < moGridPaneEmployeesAvailable.getModel().getRowCount(); i++) {
                SRowPayrollEmployee row = (SRowPayrollEmployee) moGridPaneEmployeesAvailable.getGridRow(i);
                empIds.add(row.getPkEmployeeId());
            }
            for (int i = 0; i < moGridPanePayrollReceipts.getModel().getRowCount(); i++) {
                SRowPayrollEmployee row = (SRowPayrollEmployee) moGridPanePayrollReceipts.getGridRow(i);
                row.getHrsReceipt().getHrsReceiptEarnings();
                for (SHrsReceiptEarning hrsReceiptEarning : row.getHrsReceipt().getHrsReceiptEarnings()) {
                    if (hrsReceiptEarning.getPayrollReceiptEarning().getFkBonusId() == SPayrollBonusUtils.BONUS) {
                        maEmployeesWithCurrentBonus.add(row.getPkEmployeeId());
                    }
                }
                
                empIds.add(row.getPkEmployeeId());
            }

            int cutDay = 0;
            int weekLag = 0;
            try {
                Date dates[] = null;
                if (mnFormSubtype == SModSysConsts.HRSS_TP_PAY_WEE) {
                    cutDay = moModuleConfig.getPrePayrollWeeklyCutoffDayWeek();
                    weekLag = moModuleConfig.getPrePayrollWeeklyWeeksLag();
                    
                    if (cutDay == 0) {
                        miClient.showMsgBoxError("No existe configuración para día de corte");
                        return;
                    }

                    dates = SPrepayrollUtils.getPrepayrollDateRangeByCutDay(cutDay, moDateDateEnd.getValue(), weekLag);
                    DateTime dateTime = new DateTime(moDateDateEnd.getValue());
                    Date endDatePrevious = dateTime.plusDays(-7).toDate();
                    Date[] dates1 = SPrepayrollUtils.getPrepayrollDateRangeByCutDay(cutDay, endDatePrevious, weekLag);
                    dates[0] = dates1[0];
                }
                else {
                    dates = SPrepayrollUtils.getPrepayrollDateRangeByTable(miClient, mnFormSubtype, moIntPeriodYear.getValue(), moIntNumber.getValue());
                    if (dates == null || dates[0] == null || dates[1] == null) {
                        miClient.showMsgBoxError("No se pudieron obtener fechas para prenómina");
                        return;
                    }
                }

                SDataCompany company = (SDataCompany) SDataUtilities.readRegistry((SClientInterface) miClient, 
                                                        SDataConstants.CFGU_CO, new int[] { miClient.getSession().getConfigCompany().getCompanyId()}, 
                                                        SLibConstants.EXEC_MODE_SILENT);
                String sCompanyKey = company.getKey();
                
                if (dialog.getCurrentBonus() == SPayrollBonusUtils.PANTRY && dialog.getWithPreviousPayments() && !maBonusPayed.contains(SPayrollBonusUtils.BONUS)) {
                    miClient.showMsgBoxError("Debe agregar el pago de vales de despensa antes que la despensa en especie.");
                    return;
                }
                
                if (dialog.getCurrentBonus() == SPayrollBonusUtils.SUPER_BONUS && dialog.getWithPreviousPayments() && !maBonusPayed.contains(SPayrollBonusUtils.BONUS)) {
                    miClient.showMsgBoxError("Debe agregar el pago de vales de despensa antes que el superbono.");
                    return;
                }
                
                if (miClient.showMsgBoxConfirm("Se cargará la prenómina correspondiente al periodo: " + 
                        SLibUtils.DateFormatDate.format(dates[0]) + " - " + SLibUtils.DateFormatDate.format(dates[1]) + 
                        ".\n¿Desea continuar?\nEste proceso puede demorar algunos minutos.") != JOptionPane.YES_OPTION) {
                    return;
                }
                    
                HashMap<Integer, ArrayList<SEarnConfiguration>> list;
                list = SPayrollUtils.getBonusPayments(miClient,
                                                        empIds, 
                                                        mnFormSubtype, 
                                                        moDateDateEnd.getValue(),
                                                        cutDay,
                                                        weekLag, 
                                                        moIntPeriodYear.getValue(), 
                                                        moIntNumber.getValue(), 
                                                        bonusIds, 
                                                        sCompanyKey,
                                                        maEmployeesWithCurrentBonus);

                if (list == null) {
                    miClient.showMsgBoxError("Ocurrió un problema al realizar la petición al sistema externo.");
                    return;
                }

                // Mostrar pantalla de info previa
                SDialogCalculatedBonus dialogB = new SDialogCalculatedBonus(miClient, "Importación de prenómina desde reloj checador");

                dialogB.setlReceiptRows(list);
                dialogB.setStartDate(SLibUtils.DbmsDateFormatDate.format(dates[0]));
                dialogB.setEndDate(SLibUtils.DbmsDateFormatDate.format(dates[1]));
                dialogB.setCutOffDay(moModuleConfig.getPrePayrollWeeklyCutoffDayWeek());
                dialogB.setBonus(dialog.getCurrentBonusText());
                dialogB.setBonusId(dialog.getCurrentBonus());
                dialogB.setCompanyKey(sCompanyKey);
                dialogB.initView();
                dialogB.setVisible(true);

                if (dialogB.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                    for (Integer bonusId : bonusIds) {
                        if (bonusId != 1) {
                            maBonusPayed.add(bonusId);
                        }
                    }
                    for (Map.Entry<Integer, ArrayList<SEarnConfiguration>> entry : list.entrySet()) {
                        // Determinar si el empleado se agrega o no
                        int idEmployee = entry.getKey();
                        ArrayList<SEarnConfiguration> gainedEarnings = new ArrayList<>();
                        for (SEarnConfiguration earnConf : entry.getValue()) {
                            if (dialog.getCurrentBonus() == earnConf.getIdBonus()) {
                                for (SRowBonus rowDialog : dialogB.getlGridRows()) {
                                    if (idEmployee == rowDialog.getEmployeeId()) {
                                        if (rowDialog.isHasBonus()) {
                                            gainedEarnings.add(earnConf);
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        if (!gainedEarnings.isEmpty()) {
                            addReceiptAndPerception(entry.getKey(), gainedEarnings);
                        }
                    }

                    computeReceipts();
                }
            }
            catch (SQLException ex) {
                Logger.getLogger(SFormPayroll.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void addReceiptAndPerception(int employeeId, ArrayList<SEarnConfiguration> configs) {
        boolean exists = false;
        for (int i = 0; i < moGridPaneEmployeesAvailable.getModel().getRowCount(); i++) {
            SRowPayrollEmployee row = (SRowPayrollEmployee) moGridPaneEmployeesAvailable.getGridRow(i);
            if (row.getPkEmployeeId() == employeeId) {
                moGridPaneEmployeesAvailable.setSelectedGridRow(i);
                exists = true;
                addToRigth(employeeId, configs);
                break;
            }
        }
        
        if (exists) {
            return;
        }
        
        addEarning(employeeId, configs);
    }
    
    private void addToRigth(int employeeId, ArrayList<SEarnConfiguration> configs) {
        actionReceiptAdd(createRecruitmentSchemaTypesSet());
        
        addEarning(employeeId, configs);
    }
    
    private void addEarning(int employeeId, ArrayList<SEarnConfiguration> configs) {
        SRowPayrollEmployee row = null;
        boolean isSourcedByClock = true;
        
        for (int i = 0; i < moGridPanePayrollReceipts.getModel().getRowCount(); i++) {
            row = (SRowPayrollEmployee) moGridPanePayrollReceipts.getGridRow(i);
            if (row.getPkEmployeeId() == employeeId) {
                break;
            }
        }
        
        if (row != null) {
            for (SEarnConfiguration config : configs) {
                addPerception(row.getHrsReceipt(), config.getIdEarning(), config.getAmount(), isSourcedByClock, config.getIdBonus());
            }
        }
        else {
            miClient.showMsgBoxError("Error!!");
        }
    }

    private void focusLostPeriodYear() throws Exception {
        if (moIntPeriodYear.getValue() < SLibTimeConsts.YEAR_MIN) {
            moIntPeriodYear.setValue(SLibTimeConsts.YEAR_MIN);
        }
        else if (moIntPeriodYear.getValue() > SLibTimeConsts.YEAR_MAX) {
            moIntPeriodYear.setValue(SLibTimeConsts.YEAR_MAX);
        }

        if (mnAuxCurrentPeriodYear != moIntPeriodYear.getValue()) {
            applyCurrentPeriodYear();
        }
        
        mnAuxCurrentPeriodYear = 0;
    }

    private void focusLostNumber() throws Exception {
        if (moIntNumber.getValue() < 1) {
            moIntNumber.setValue(1);
        }
        else if (mnFormSubtype == SModSysConsts.HRSS_TP_PAY_WEE && moIntNumber.getValue() > SHrsConsts.YEAR_WEEKS_EXTENDED) {
            moIntNumber.setValue(SHrsConsts.YEAR_WEEKS_EXTENDED);
        }
        else if (mnFormSubtype == SModSysConsts.HRSS_TP_PAY_FOR && moIntNumber.getValue() > SHrsConsts.YEAR_FORTNIGHTS) {
            moIntNumber.setValue(SHrsConsts.YEAR_FORTNIGHTS);
        }

        if (mnAuxCurrentNumber != moIntNumber.getValue()) {
            applyCurrentNumber();
        }
        
        mnAuxCurrentNumber = 0;
    }

    private void focusLostDateStart() throws Exception {
        if (moDateDateStart.getValue() == null || moDateDateStart.getValue().compareTo(mtDefaultDateStart) < 0) {
            moDateDateStart.setValue(mtDefaultDateStart);
        }

        if (mtAuxCurrentDateStart != moDateDateStart.getValue()) {
            triggerResets(true);
        }
        
        mtAuxCurrentDateStart = null;
    }

    private void focusLostDateEnd() throws Exception {
        if (moDateDateEnd.getValue() == null || moDateDateEnd.getValue().compareTo(mtDefaultDateEnd) > 0) {
            moDateDateEnd.setValue(mtDefaultDateEnd);
        }

        if (mtAuxCurrentDateEnd != moDateDateEnd.getValue()) {
            triggerResets(true);
        }

        mtAuxCurrentDateEnd = null;
    }

    private void focusLostPeriod() throws Exception {
        int[] dateStart = null;
        int[] dateEnd = null;

        if (mnAuxCurrentPeriod != moIntPeriod.getValue()) {
            dateStart = SLibTimeUtils.digestMonth(moDateDateStart.getValue());
            dateEnd = SLibTimeUtils.digestMonth(moDateDateEnd.getValue());

            if (moIntPeriodYear.getValue() == dateStart[0] && moIntPeriod.getValue() < dateStart[1]) {
                moIntPeriod.setValue(dateStart[1]);
            }
            else if (moIntPeriodYear.getValue() == dateEnd[0] && moIntPeriod.getValue() > dateEnd[1]) {
                moIntPeriod.setValue(dateEnd[1]);
            }
            else if (moIntPeriod.getValue() <= 0) {
                moIntPeriod.setValue(dateEnd[1]);
            }
        }

        mnAuxCurrentPeriod = 0;
    }

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    @Override
    public void addAllListeners() {
        jbEditPeriodYear.addActionListener(this);
        jbEditFiscalYear.addActionListener(this);
        jbEditNumber.addActionListener(this);
        jbGetNextNumber.addActionListener(this);
        jbEditDates.addActionListener(this);
        jbEditPeriod.addActionListener(this);
        jbEditRecruitmentSchemaType.addActionListener(this);
        jbEditHint.addActionListener(this);
        jbGoTabReceipts.addActionListener(this);
        jbReceiptAdd.addActionListener(this);
        jbReceiptAddAll.addActionListener(this);
        jbReceiptRemove.addActionListener(this);
        jbReceiptRemoveAll.addActionListener(this);
        jbReceiptCaptureEarnings.addActionListener(this);
        jbReceiptCaptureDeductions.addActionListener(this);
        jbLoadPrepayroll.addActionListener(this);
        jbClearPrepayroll.addActionListener(this);
        jbAddPayrollBonus.addActionListener(this);
        jbTaxSubsidyOptionChange.addActionListener(this);
        
        moRadNormal.addItemListener(this);
        moRadSpecial.addItemListener(this);
        moRadExtraordinary.addItemListener(this);
        moRadViewEmployeesActive.addItemListener(this);
        moRadViewEmployeesAll.addItemListener(this);

        moKeyPaysheetCustomType.addItemListener(this);
        moKeyTaxComputationType.addItemListener(this);
        moKeyTax.addItemListener(this);
        moKeyTaxSubsidy.addItemListener(this);
        moKeyEmploymentSubsidy.addItemListener(this);
        moKeySsContribution.addItemListener(this);
        moKeyMwzType.addItemListener(this);
        moKeyMwzReferenceType.addItemListener(this);
        moBoolSsContribution.addItemListener(this);

        moIntPeriodYear.addFocusListener(this);
        moIntNumber.addFocusListener(this);
        moDateDateStart.getComponent().addFocusListener(this);
        moDateDateEnd.getComponent().addFocusListener(this);
        moIntPeriod.addFocusListener(this);

        jtpPayroll.addChangeListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbEditPeriodYear.removeActionListener(this);
        jbEditFiscalYear.removeActionListener(this);
        jbEditNumber.removeActionListener(this);
        jbGetNextNumber.removeActionListener(this);
        jbEditDates.removeActionListener(this);
        jbEditPeriod.removeActionListener(this);
        jbEditRecruitmentSchemaType.removeActionListener(this);
        jbEditHint.removeActionListener(this);
        jbGoTabReceipts.removeActionListener(this);
        jbReceiptAdd.removeActionListener(this);
        jbReceiptAddAll.removeActionListener(this);
        jbReceiptRemove.removeActionListener(this);
        jbReceiptRemoveAll.removeActionListener(this);
        jbReceiptCaptureEarnings.removeActionListener(this);
        jbReceiptCaptureDeductions.removeActionListener(this);
        jbLoadPrepayroll.removeActionListener(this);
        jbClearPrepayroll.removeActionListener(this);
        jbAddPayrollBonus.removeActionListener(this);
        jbTaxSubsidyOptionChange.removeActionListener(this);

        moRadNormal.removeItemListener(this);
        moRadSpecial.removeItemListener(this);
        moRadExtraordinary.removeItemListener(this);
        moRadViewEmployeesActive.removeItemListener(this);
        moRadViewEmployeesAll.removeItemListener(this);
        
        moKeyPaysheetCustomType.removeItemListener(this);
        moKeyMwzType.removeItemListener(this);
        moKeyMwzReferenceType.removeItemListener(this);
        moKeyTaxComputationType.removeItemListener(this);
        moKeyTax.removeItemListener(this);
        moKeyTaxSubsidy.removeItemListener(this);
        moKeyEmploymentSubsidy.removeItemListener(this);
        moKeySsContribution.removeItemListener(this);
        moBoolSsContribution.removeItemListener(this);

        moIntPeriodYear.removeFocusListener(this);
        moIntNumber.removeFocusListener(this);
        moDateDateStart.getComponent().removeFocusListener(this);
        moDateDateEnd.getComponent().removeFocusListener(this);
        moIntPeriod.removeFocusListener(this);

        jtpPayroll.removeChangeListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyPaysheetCustomType, SModConsts.HRSU_TP_PAY_SHT_CUS, 0, null);
        miClient.getSession().populateCatalogue(moKeyRecruitmentSchemaType, SModConsts.HRSS_TP_REC_SCHE, 0, null);
        miClient.getSession().populateCatalogue(moKeyMwzType, SModConsts.HRSU_TP_MWZ, 0, null);
        miClient.getSession().populateCatalogue(moKeyMwzReferenceType, SModConsts.HRSU_TP_MWZ, 0, null);
        miClient.getSession().populateCatalogue(moKeyTaxComputationType, SModConsts.HRSS_TP_TAX_COMP, 0, null);
        miClient.getSession().populateCatalogue(moKeyTax, SModConsts.HRS_TAX, 0, null);
        miClient.getSession().populateCatalogue(moKeyTaxSubsidy, SModConsts.HRS_TAX_SUB, 0, null);
        miClient.getSession().populateCatalogue(moKeyEmploymentSubsidy, SModConsts.HRS_EMPL_SUB, 0, null);
        miClient.getSession().populateCatalogue(moKeySsContribution, SModConsts.HRS_SSC, 0, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbPayroll) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;
        
        mnCalendarDays = 0;
        mbIsReadOnly = false;
        mbIsBeingCopied = false;
        mbIsWithTaxSubsidy = true;
        mbIsGoingToReceipts = false;
        mbAuxReopen = false;
        
        maPayrollReceiptsDeleted.clear();
        
        removeAllListeners();
        reloadCatalogues();

        moModuleConfig = (SDbConfig) miClient.getSession().readRegistry(SModConsts.HRS_CFG, new int[] { SUtilConsts.BPR_CO_ID });
        moWorkingDaySettings = SHrsUtils.getPayrollWorkingDaySettings(miClient.getSession(), mnFormSubtype);
                
        if (SHrsUtils.validatePayroll(miClient.getSession(), moModuleConfig, moWorkingDaySettings)) {
            // all basic information required for payroll capture is already validated
            
            // Set payroll:
            
            mbIsReadOnly = moRegistry.isReadOnly();
            mbIsBeingCopied = moRegistry.isRegistryNew() && moRegistry.getPkPayrollId() != 0;

            if (moRegistry.isRegistryNew()) {
                jtfRegistryKey.setText("");

                // Set default values:

                moRegistry.setFkPaymentTypeId(mnFormSubtype);
                
                moRegistry.setFkPaysheetTypeId(SModSysConsts.HRSS_TP_PAY_SHT_NOR);
                moRegistry.setFkPaysheetCustomTypeId(0);
                moRegistry.setFkRecruitmentSchemaTypeId(SModSysConsts.HRSS_TP_REC_SCHE_NA);
                
                moRegistry.setClosed(false);
                moRegistry.setSsContribution(true);
                moRegistry.setTaxSubsidy(mbIsWithTaxSubsidy);

                // Set default payroll settings:

                if (!mbIsBeingCopied) {
                    moRegistry.setPkPayrollId(0); // registry is new and is not being copied
                    
                    moRegistry.setFkTaxComputationTypeId(moModuleConfig.getFkTaxComputationTypeId());
                    moRegistry.setFkMwzTypeId(moModuleConfig.getFkMwzTypeId());
                    moRegistry.setFkMwzReferenceTypeId(moModuleConfig.getFkMwzReferenceTypeId());
                }
            }
            else {
                jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
            }

            // Set payroll settings:

            setPaysheetTypeId(moRegistry.getFkPaysheetTypeId());
            moKeyPaysheetCustomType.setValue(new int[] { moRegistry.getFkPaysheetCustomTypeId() });
            moKeyRecruitmentSchemaType.setValue(new int[] { moRegistry.getFkRecruitmentSchemaTypeId()});
            
            moKeyTaxComputationType.setValue(new int[] { moRegistry.getFkTaxComputationTypeId() });
            moKeyMwzType.setValue(new int[] { moRegistry.getFkMwzTypeId() });
            moKeyMwzReferenceType.setValue(new int[] { moRegistry.getFkMwzReferenceTypeId() });

            if (moRegistry.getPkPayrollId() == 0) {
                // registry is new and is not being copied
                
                // Set a new period:
                
                setDefaultPeriodYear(miClient.getSession().getCurrentYear(), true, false);
                
                moRegistry.setPeriodYear(mnDefaultPeriodYear);
                moRegistry.setNumber(mnDefaultNumber);
                moRegistry.setDateStart(mtDefaultDateStart);
                moRegistry.setDateEnd(mtDefaultDateEnd);
                moRegistry.setPeriod(mnDefaultPeriod);
            }
            else {
                // Set registry's period:
                
                mnDefaultPeriodYear = moRegistry.getPeriodYear();

                moIntPeriodYear.setValue(moRegistry.getPeriodYear());
                moIntFiscalYear.setValue(moRegistry.getFiscalYear());

                mnDefaultNumber = moRegistry.getNumber();
                
                moIntNumber.setValue(moRegistry.getNumber());

                setDefaultPeriod(mnDefaultPeriodYear, mnDefaultNumber, false, false);
                
                moDateDateStart.setValue(moRegistry.getDateStart());
                moDateDateEnd.setValue(moRegistry.getDateEnd());
                moIntPeriod.setValue(moRegistry.getPeriod());

                // Set days:
                resetPayrollDays(); // REQUIRED for proper payroll calendar days computation!
                moIntReceiptDays.setValue(moRegistry.getReceiptDays());
                moIntWorkingDays.setValue(moRegistry.getWorkingDays());
                
                // Set salaries amounts:
                moDecMwzWage.getField().setValue(moRegistry.getMwzWage());
                moDecMwzReferenceWage.getField().setValue(moRegistry.getMwzReferenceWage());
                
                // Set UMA & UMI amounts:
                moDecUmaAmount.getField().setValue(moRegistry.getUmaAmount());
                moDecUmiAmount.getField().setValue(moRegistry.getUmiAmount());

                // Set withholding tables:
                moKeyTax.setValue(new int[] { moRegistry.getFkTaxId() });
                moKeyTaxSubsidy.setValue(new int[] { moRegistry.getFkTaxSubsidyId() });
                moKeyEmploymentSubsidy.setValue(new int[] { moRegistry.getFkEmploymentSubsidyId_n()});
                moKeySsContribution.setValue(new int[] { moRegistry.getFkSsContributionId() });
            }

            moTextHint.setValue(moRegistry.getHint());
            moTextNotes.setValue(moRegistry.getNotes());
            moBoolClosed.setValue(moRegistry.isClosed());
            moBoolSsContribution.setValue(moRegistry.isSsContribution());
            mbIsWithTaxSubsidy = moRegistry.isTaxSubsidy();
            updateTaxSubsidyOptionText();

            moDecTotalEarnings.getField().setValue(moRegistry.getAuxTotalEarnings());
            moDecTotalDeductions.getField().setValue(moRegistry.getAuxTotalDeductions());
            moDecTotalNet.getField().setValue(moRegistry.getAuxTotalNet());
            
            if (!mbIsReadOnly) {
                updatePayroll(moRegistry, true); // update registry with current UI data, JUST BEFORE instantiating HRS payroll
            }

            moHrsPayroll = (new SHrsPayrollDataProvider(miClient.getSession())).createHrsPayroll(moModuleConfig, moWorkingDaySettings, moRegistry);

            jtpPayroll.setSelectedIndex(0);

            setFormEditable(true);
            
            populateRowPayrollEmployeesReceipts();      // This method MUST be invoked JUST BEFORE populateRowPayrollEmployeesAvailable()! Improve this!
            populateRowPayrollEmployeesAvailable(true); // This method MUST be invoked JUST AFTER populateRowPayrollEmployeesReceipts()! Improve this!

            clearAuxCurrentValues();
            enableFieldsStatusRelatedToPayroll();
            enableFieldsStatusRelatedToReceipts();
            
            // reset filters in receipts tab:
            moRadViewEmployeesActive.setSelected(true);
            moBoolFilterWages.setSelected(true);
            moBoolFilterAssimilated.setSelected(false);
            moBoolFilterRetirees.setSelected(false);
            moBoolFilterOthers.setSelected(false);
            
            maBonusPayed = new ArrayList<>();
            
            jbSave.setEnabled(!mbIsReadOnly);

            addAllListeners();
        }
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbPayroll registry = moRegistry.clone();

        if (registry.isRegistryNew()) {
            registry.initPrimaryKey();
        }

        updatePayroll(registry, false); // update registry with current UI data
        
        registry.getChildPayrollReceipts().clear();
        
        for (SHrsReceipt hrsReceipt : moHrsPayroll.getHrsReceipts()) {
            SDbPayrollReceipt payrollReceipt = hrsReceipt.getPayrollReceipt();

            // Obtain payrollReceiptEarnings:

            payrollReceipt.getChildPayrollReceiptEarnings().clear();
            for (SHrsReceiptEarning hrsReceiptEarning : hrsReceipt.getHrsReceiptEarnings()) {
                payrollReceipt.getChildPayrollReceiptEarnings().add(hrsReceiptEarning.getPayrollReceiptEarning());
            }

            // Obtain payrollReceiptDeductions:

            payrollReceipt.getChildPayrollReceiptDeductions().clear();
            for (SHrsReceiptDeduction hrsReceiptDeduction : hrsReceipt.getHrsReceiptDeductions()) {
                payrollReceipt.getChildPayrollReceiptDeductions().add(hrsReceiptDeduction.getPayrollReceiptDeduction());
            }
            
            // Obtain absenceConsumption:

            payrollReceipt.getChildAbsenceConsumptions().clear();
            for (SDbAbsenceConsumption absenceConsumption : hrsReceipt.getAbsenceConsumptions()) {
                payrollReceipt.getChildAbsenceConsumptions().add(absenceConsumption);
            }

            registry.getChildPayrollReceipts().add(payrollReceipt);
        }
        
        registry.getChildPayrollReceiptsToDelete().clear();
        registry.getChildPayrollReceiptsToDelete().addAll(maPayrollReceiptsDeleted);
        
        if (mbAuxReopen) {
            SGuiParams params = new SGuiParams();
            registry.computePrimaryKey(miClient.getSession());
            params.setKey(registry.getPrimaryKey());
            
            registry.setPostSaveTarget(miClient.getSession().getModule(SModConsts.MOD_HRS_N, SLibConsts.UNDEFINED));
            registry.setPostSaveMethod(registry.getPostSaveTarget().getClass().getMethod("showForm", int.class, int.class, SGuiParams.class));
            registry.setPostSaveMethodArgs(new Object[] { mnFormType, mnFormSubtype, params });
        }

        return registry;
    }

    @Override
    public void setValue(final int type, final Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        try {
            if (validation.isValid()) {
                if (!mbIsReadOnly) {
                    // Validate applicability or not of selecting some employment subsidy:
                    
                    int configId = SHrsUtils.getRecentEmploymentSubsidy(miClient.getSession(), moDateDateEnd.getValue());

                    if (configId == 0 && moKeyEmploymentSubsidy.getSelectedIndex() > 0) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ_NOT + "'" + moKeyEmploymentSubsidy.getFieldName() + "'.");
                        validation.setComponent(moKeyEmploymentSubsidy);
                    }
                    else if (configId != 0 && moKeyEmploymentSubsidy.getSelectedIndex() <= 0) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + moKeyEmploymentSubsidy.getFieldName() + "'.");
                        validation.setComponent(moKeyEmploymentSubsidy);
                    }
                }
                
                if (validation.isValid()) {
                    if (!mbIsGoingToReceipts && !mbIsReadOnly && moGridPanePayrollReceipts.getTable().getRowCount() == 0 && miClient.showMsgBoxConfirm("¡La nómina no tiene recibos!\n" + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                        validation.setMessage("Crear los recibos de nómina que sean necesarios.");
                        validation.setComponent(jbReceiptAdd);
                    }
                    else {
                        mbIsGoingToReceipts = false; // not going to receipts any more!

                        // validate number of payroll:

                        int num = SHrsUtils.validatePayrollNumber(miClient.getSession(), moIntPeriodYear.getValue(), moIntNumber.getValue(), mnFormSubtype);

                        if (moIntNumber.getValue() > num) {
                            validation.setMessage(SGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + SGuiUtils.getLabelName(jlNumber) + "'" + SGuiConsts.ERR_MSG_FIELD_VAL_LESS_EQUAL + num + ".");
                            validation.setComponent(moIntNumber);
                        }
                        else {
                            // validate period of payroll:

                            try {
                                SLibTimeUtils.validatePeriod(moDateDateStart.getValue(), moDateDateEnd.getValue());
                                SHrsUtils.validatePayrollDate(moModuleConfig, moDateDateStart.getValue(), moDateDateEnd.getValue());
                            }
                            catch (Exception exception) {
                                validation.setMessage(exception.getMessage());
                            }

                            if (validation.isValid()) {
                                if (!SLibTimeUtils.isBelongingToPeriod(moDateDateStart.getValue(), mtDefaultDateStart, mtDefaultDateEnd)) {
                                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DATE_ + "'" + SGuiUtils.getLabelName(jlDateStart.getText()) + "' "
                                            + SGuiConsts.ERR_MSG_FIELD_DATE_GREAT_EQUAL + "'" + SLibUtils.DateFormatDate.format(mtDefaultDateStart) + "', "
                                            + SGuiConsts.ERR_MSG_FIELD_DATE_LESS_EQUAL + "'" + SLibUtils.DateFormatDate.format(mtDefaultDateEnd) + "'.");
                                    validation.setComponent(moDateDateStart);
                                }
                                else if (!SLibTimeUtils.isBelongingToPeriod((moDateDateEnd.getValue() != null ? moDateDateEnd.getValue() : mtDefaultDateEnd), mtDefaultDateStart, mtDefaultDateEnd)) {
                                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DATE_ + "'" + SGuiUtils.getLabelName(jlDateEnd.getText()) + "' "
                                            + SGuiConsts.ERR_MSG_FIELD_DATE_GREAT_EQUAL + "'" + SLibUtils.DateFormatDate.format(mtDefaultDateStart) + "', "
                                            + SGuiConsts.ERR_MSG_FIELD_DATE_LESS_EQUAL + "'" + SLibUtils.DateFormatDate.format(mtDefaultDateEnd) + "'.");
                                    validation.setComponent(moDateDateEnd);
                                }
                            }

                            if (validation.isValid()) {
                                if (SLibTimeUtils.digestDate(moDateDateStart.getValue())[0] != moIntFiscalYear.getValue() &&
                                        SLibTimeUtils.digestDate(moDateDateEnd.getValue())[0] != moIntFiscalYear.getValue()) {
                                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + SGuiUtils.getLabelName(jlFiscalYear) + "' " +
                                            SGuiConsts.ERR_MSG_FIELD_DATE_YEAR + "de al menos una de las fechas inicial o final.");
                                    validation.setComponent(moIntFiscalYear);
                                }
                                else {
                                    ArrayList<SGuiField> fields = new ArrayList<>();
                                    fields.add(moDecMwzWage.getField());
                                    fields.add(moDecMwzReferenceWage.getField());
                                    fields.add(moDecUmaAmount.getField());
                                    fields.add(moDecUmiAmount.getField());

                                    for (SGuiField field : fields) {
                                        validation = field.validateField();
                                        if (!validation.isValid()) {
                                            break;
                                        }
                                    }
                                }
                            }

                            if (validation.isValid()) {
                                if (!SHrsUtils.isPayrollUniquenessFullfilled(miClient.getSession(), 
                                        moRegistry.getPkPayrollId(), moIntPeriodYear.getValue(), moIntNumber.getValue(), 
                                        mnFormSubtype, getPaysheetTypeId(), moKeyPaysheetCustomType.getValue()[0])) {
                                    validation.setMessage("Ya existe una nómina del mismo año, número, periodicidad y tipo.");
                                    validation.setComponent(moKeyPaysheetCustomType);
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            validation.setMessage("" + e);
        }

        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        try {
            if (evt.getSource() instanceof JButton) {
                JButton button = (JButton) evt.getSource();

                if (button == jbEditPeriodYear) {
                    actionEditPeriodYear();
                }
                else if (button == jbEditFiscalYear) {
                    actionEditFiscalYear();
                }
                else if (button == jbEditNumber) {
                    actionEditNumber();
                }
                else if (button == jbGetNextNumber) {
                    actionGetNextNumber();
                }
                else if (button == jbEditDates) {
                    actionEditDates();
                }
                else if (button == jbEditPeriod) {
                    actionEditPeriod();
                }
                else if (button == jbEditRecruitmentSchemaType) {
                    actionEditRecruitmentSchemaType();
                }
                else if (button == jbEditHint) {
                    actionEditHint();
                }
                else if (button == jbTaxSubsidyOptionChange) {
                    actionTaxSubsidyChange();
                }
                else if (button == jbGoTabReceipts) {
                    actionGoTabReceipts();
                }
                else if (button == jbReceiptAdd) {
                    actionReceiptAdd(createRecruitmentSchemaTypesSet());
                }
                else if (button == jbReceiptAddAll) {
                    actionReceiptAddAll();
                }
                else if (button == jbReceiptRemove) {
                    actionReceiptRemove();
                }
                else if (button == jbReceiptRemoveAll) {
                    actionReceiptRemoveAll();
                }
                else if (button == jbReceiptCaptureEarnings) {
                    actionReceiptCaptureEarnings();
                }
                else if (button == jbReceiptCaptureDeductions) {
                    actionReceiptCaptureDeductions();
                }
                else if (button == jbLoadPrepayroll) {
                    actionLoadPrepayroll();
                }
                else if (button == jbClearPrepayroll) {
                    actionClearPrepayroll();
                }
                else if (button == jbAddPayrollBonus) {
                    actionAddPayrollBonus();
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent evt) {
        try {
            if (evt.getSource() instanceof JComboBox && evt.getStateChange() == ItemEvent.SELECTED) {
                JComboBox comboBox = (JComboBox)  evt.getSource();

                if (comboBox == moKeyPaysheetCustomType) {
                    itemStateChangedPaysheetCustomType();
                }
                else if (comboBox == moKeyTaxComputationType || comboBox == moKeyTax || comboBox == moKeyTaxSubsidy || comboBox == moKeyEmploymentSubsidy || comboBox == moKeySsContribution) {
                    computeReceipts();
                }
                else if (comboBox == moKeyMwzType || comboBox == moKeyMwzReferenceType) {
                    resetSalariesAmounts();
                    computeReceipts();
                }
            }
            else if (evt.getSource() instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) evt.getSource();

                if (checkBox == moBoolSsContribution.getComponent()) {
                    computeReceipts();
                }
            }
            else if (evt.getSource() instanceof SBeanFieldRadio && evt.getStateChange() == ItemEvent.SELECTED) {
                SBeanFieldRadio field = (SBeanFieldRadio) evt.getSource();

                if (field == moRadSpecial || field == moRadNormal || field == moRadExtraordinary) {
                    itemStateChangedPaysheetType();
                }
                else if (field == moRadViewEmployeesActive) {
                    itemStateChangedEmployeesViewActive();
                }
                else if (field == moRadViewEmployeesAll) {
                    itemStateChangedEmployeesViewAll();
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    @Override
    public void focusGained(FocusEvent evt) {
        if (evt.getSource() instanceof SBeanFieldInteger) {
            SBeanFieldInteger field = (SBeanFieldInteger) evt.getSource();

            if (field == moIntPeriodYear){
                mnAuxCurrentPeriodYear = moIntPeriodYear.getValue();
            }
            else if (field == moIntNumber) {
                mnAuxCurrentNumber = moIntNumber.getValue();
            }
            else if (field == moIntPeriod) {
                mnAuxCurrentPeriod = moIntPeriod.getValue();
            }
        }
        else if (evt.getSource() instanceof JFormattedTextField) {
            JFormattedTextField formattedTextField = (JFormattedTextField) evt.getSource();

            if (formattedTextField == moDateDateStart.getComponent()) {
                mtAuxCurrentDateStart = moDateDateStart.getValue();
            }
            else if (formattedTextField == moDateDateEnd.getComponent()) {
                mtAuxCurrentDateEnd = moDateDateEnd.getValue();
            }
        }
    }

    @Override
    public void focusLost(FocusEvent evt) {
        try {
            if (evt.getSource() instanceof SBeanFieldInteger) {
                SBeanFieldInteger field = (SBeanFieldInteger) evt.getSource();

                if (field == moIntPeriodYear){
                    focusLostPeriodYear();
                }
                else if (field == moIntNumber) {
                    focusLostNumber();
                }
                else if (field == moIntPeriod) {
                    focusLostPeriod();
                }
            }
            else if (evt.getSource() instanceof JFormattedTextField) {
                JFormattedTextField formattedTextField = (JFormattedTextField) evt.getSource();

                if (formattedTextField == moDateDateStart.getComponent()) {
                    focusLostDateStart();
                }
                else if (formattedTextField == moDateDateEnd.getComponent()) {
                    focusLostDateEnd();
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            clearAuxCurrentValues();
        }
    }

    @Override
    public void stateChanged(ChangeEvent evt) {
        if (evt.getSource() instanceof JTabbedPane) {
            JTabbedPane tabbedPane = (JTabbedPane) evt.getSource();

            if (tabbedPane == jtpPayroll) {
                if (tabbedPane.getSelectedIndex() == 1) {
                    actionGoTabReceipts();
                }
            }
        }
    }
}
