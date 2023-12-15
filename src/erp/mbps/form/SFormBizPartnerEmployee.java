/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mbps.form;

import cfd.DCfdConsts;
import erp.SErpConsts;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.gui.SPanelQueryIntegralEmployee;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mbps.data.SDataBizPartnerBranchContact;
import erp.mbps.data.SDataBizPartnerCategory;
import erp.mbps.data.SDataEmployee;
import erp.mbps.data.SRowEmployeeBenefit;
import erp.mbps.data.SRowEmployeeWageFactor;
import erp.mcfg.data.SDataCompany;
import erp.mhrs.data.SDataEmployeeRelatives;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbBenefitTable;
import erp.mod.hrs.db.SDbEmployeeBenefitTables;
import erp.mod.hrs.db.SDbEmployeeBenefitTablesAnnum;
import erp.mod.hrs.db.SDbEmployeeWageFactorAnnum;
import erp.mod.hrs.db.SHrsBenefitUtils;
import erp.mod.hrs.db.SHrsConsts;
import erp.mod.hrs.db.SHrsEmployeeUtils;
import erp.mod.hrs.db.SHrsUtils;
import erp.mod.hrs.utils.SAnniversary;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiSession;
import sa.lib.gui.SGuiUtils;

/**
 *
 * @author Juan Barajas, Edwin Carmona, Sergio Flores, Claudio Peña, Sergio Flores
 */
public class SFormBizPartnerEmployee extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.ItemListener, java.awt.event.FocusListener, javax.swing.event.ChangeListener {
    
    private static final int TAB_DATA_EMP = 0;
    private static final int TAB_DATA_PER = 1;
    private static final int TAB_BENEFITS = 2;
    private static final int TAB_WAGE_FACTORS = 3;

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbUpdatingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    // Business partner fields:

    private erp.mbps.data.SDataBizPartner moBizPartner;
    private erp.mbps.data.SDataBizPartnerBranch moBizPartnerBranch;
    private erp.mbps.data.SDataEmployee moEmployee;
    private erp.mod.hrs.db.SDbEmployeeBenefitTables moEmployeeBenefitTables; // NOTE: This class belongs to SA Lib 1.0 framework!

    // Employee fields:

    private erp.lib.form.SFormField moFieldNumber;
    private erp.lib.form.SFormField moFieldLastname1;
    private erp.lib.form.SFormField moFieldLastname2;
    private erp.lib.form.SFormField moFieldFirstname;
    private erp.lib.form.SFormField moFieldFiscalId;
    private erp.lib.form.SFormField moFieldAlternativeId;
    private erp.lib.form.SFormField moFieldSocialSecurityNumber;
    private erp.lib.form.SFormField moFieldFkBank_n;
    private erp.lib.form.SFormField moFieldBankAccount;
    private erp.lib.form.SFormField moFieldFkGroceryService;
    private erp.lib.form.SFormField moFieldGroceryServiceAccount;
    private erp.lib.form.SFormField moFieldMailOwn;
    private erp.lib.form.SFormField moFieldMailCompany;
    private erp.lib.form.SFormField moFieldFileImagePhoto;
    private erp.lib.form.SFormField moFieldFileImageSignature;
    private erp.lib.form.SFormField moFieldIsActive;
    private erp.lib.form.SFormField moFieldIsDeleted;
    
    private erp.lib.form.SFormField moFieldDateBenefits;
    private erp.lib.form.SFormField moFieldDateLastHire;
    private erp.lib.form.SFormField moFieldDateLastDismissal_n;
    private erp.lib.form.SFormField moFieldFkPaymentType;
    private erp.lib.form.SFormField moFieldFkSalaryType;
    private erp.lib.form.SFormField moFieldSalary;
    private erp.lib.form.SFormField moFieldDateChangeSalary;
    private erp.lib.form.SFormField moFieldWage;
    private erp.lib.form.SFormField moFieldDateChangeWage;
    private erp.lib.form.SFormField moFieldSalarySscBase;
    private erp.lib.form.SFormField moFieldDateChangeSalarySscBase;
    private erp.lib.form.SFormField moFieldFkMwzType; // mininum wage zone
    
    private erp.lib.form.SFormField moFieldFkEmployeeType;
    private erp.lib.form.SFormField moFieldFkWorkerType;
    private erp.lib.form.SFormField moFieldIsUnionized;
    private erp.lib.form.SFormField moFieldIsMfgOperator;
    private erp.lib.form.SFormField moFieldFkDepartment;
    private erp.lib.form.SFormField moFieldFkPosition;
    private erp.lib.form.SFormField moFieldFkShift;
    private erp.lib.form.SFormField moFieldFkWorkingDayType;
    private erp.lib.form.SFormField moFieldWorkingHoursDay;
    private erp.lib.form.SFormField moFieldFkContractType;
    private erp.lib.form.SFormField moFieldContractExpiration;
    private erp.lib.form.SFormField moFieldFkRecruitmentSchemaType;
    private erp.lib.form.SFormField moFieldFkPositionRiskType;
    
    // Personal data:
    
    private erp.lib.form.SFormField moFieldDateBirth;
    private erp.lib.form.SFormField moFieldPlaceOfBirth;
    private erp.lib.form.SFormField moFieldUmf;
    private erp.lib.form.SFormField moFieldFkCatalogueSexType;
    private erp.lib.form.SFormField moFieldFkCatalogueBloodTypeType;
    private erp.lib.form.SFormField moFieldFkCatalogueEducationType;
    private erp.lib.form.SFormField moFieldFkCatalogueMaritalStatusType;
    private erp.lib.form.SFormField moFieldPhone;
    
    private erp.lib.form.SFormField moFieldMateName;
    private erp.lib.form.SFormField moFieldMateDateBirth;
    private erp.lib.form.SFormField moFieldMateSex;
    private erp.lib.form.SFormField moFieldMateDeceased;
    private erp.lib.form.SFormField moFieldSonName1;
    private erp.lib.form.SFormField moFieldSonDateBirth1;
    private erp.lib.form.SFormField moFieldSonSex1;
    private erp.lib.form.SFormField moFieldSonDeceased1;
    private erp.lib.form.SFormField moFieldSonName2;
    private erp.lib.form.SFormField moFieldSonDateBirth2;
    private erp.lib.form.SFormField moFieldSonSex2;
    private erp.lib.form.SFormField moFieldSonDeceased2;
    private erp.lib.form.SFormField moFieldSonName3;
    private erp.lib.form.SFormField moFieldSonDateBirth3;
    private erp.lib.form.SFormField moFieldSonSex3;
    private erp.lib.form.SFormField moFieldSonDeceased3;
    private erp.lib.form.SFormField moFieldSonName4;
    private erp.lib.form.SFormField moFieldSonDateBirth4;
    private erp.lib.form.SFormField moFieldSonSex4;
    private erp.lib.form.SFormField moFieldSonDeceased4;
    private erp.lib.form.SFormField moFieldSonName5;
    private erp.lib.form.SFormField moFieldSonDateBirth5;
    private erp.lib.form.SFormField moFieldSonSex5;
    private erp.lib.form.SFormField moFieldSonDeceased5;
    
    // Benefits:
    
    private erp.lib.form.SFormField moFieldBenVacTable;
    private erp.lib.form.SFormField moFieldBenVacBonTable;
    private erp.lib.form.SFormField moFieldBenAnnBonTable;

    private erp.mbps.form.SPanelBizPartnerBranchAddress moPanelBizPartnerBranchAddress;
    private erp.lib.table.STablePane moBenefitsVacPane;
    private erp.lib.table.STablePane moBenefitsVacBonPane;
    private erp.lib.table.STablePane moBenefitsAnnBonPane;
    private erp.lib.table.STablePane moWageFactorsPane;

    private int mnParamBizPartnerType;
    private int mnPkContactId;
    
    private boolean mbPhotoChange;
    private boolean mbSignatureChange;
    private javax.swing.ImageIcon moXtaImageIconPhoto_n;
    private javax.swing.ImageIcon moXtaImageIconSignature_n;

    /**
     * Creates new form SFormBizPartnerEmployee
     * @param client 
     */
    public SFormBizPartnerEmployee(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;

        initComponents();
        initComponentsExtra();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgOvertime = new javax.swing.ButtonGroup();
        bgCheckerPolicy = new javax.swing.ButtonGroup();
        jTabbedPane = new javax.swing.JTabbedPane();
        jpEmployee = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jlNumber = new javax.swing.JLabel();
        jftNumber = new javax.swing.JFormattedTextField();
        jtfBizPartner_Ro = new javax.swing.JTextField();
        jPanel25 = new javax.swing.JPanel();
        jlLastname = new javax.swing.JLabel();
        jtfLastname1 = new javax.swing.JTextField();
        jlLastname1 = new javax.swing.JLabel();
        jtfLastname2 = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jlFirstname = new javax.swing.JLabel();
        jtfFirstname = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jlFiscalId = new javax.swing.JLabel();
        jtfFiscalId = new javax.swing.JTextField();
        jlAlternativeId = new javax.swing.JLabel();
        jftAlternativeId = new javax.swing.JFormattedTextField();
        jlSocialSecurityNumber = new javax.swing.JLabel();
        jtfSocialSecurityNumber = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        jlFkBank_n = new javax.swing.JLabel();
        jcbFkBank_n = new javax.swing.JComboBox<SFormComponentItem>();
        jlBankAccount = new javax.swing.JLabel();
        jtfBankAccount = new javax.swing.JTextField();
        jPanel42 = new javax.swing.JPanel();
        jlFkGroceryService = new javax.swing.JLabel();
        jcbFkGroceryService = new javax.swing.JComboBox<SFormComponentItem>();
        jlGroceryServiceAccount = new javax.swing.JLabel();
        jtfGroceryServiceAccount = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jlMailOwn = new javax.swing.JLabel();
        jtfMailOwn = new javax.swing.JTextField();
        jlMailCompany = new javax.swing.JLabel();
        jtfMailCompany = new javax.swing.JTextField();
        jPanel48 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jlImgPhoto = new javax.swing.JLabel();
        jlImgSignature = new javax.swing.JLabel();
        jPanel49 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        jlImagePhoto = new javax.swing.JLabel();
        jtfImagePhoto = new javax.swing.JTextField();
        jbImagePhoto = new javax.swing.JButton();
        jbImagePhotoView = new javax.swing.JButton();
        jbImagePhotoRemove = new javax.swing.JButton();
        jPanel46 = new javax.swing.JPanel();
        jlImageSignature = new javax.swing.JLabel();
        jtfImageSignature = new javax.swing.JTextField();
        jbImageSignature = new javax.swing.JButton();
        jbImageSignatureView = new javax.swing.JButton();
        jbImageSignatureRemove = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jckIsActive = new javax.swing.JCheckBox();
        jckIsDeleted = new javax.swing.JCheckBox();
        jPanel60 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jlDateBenefits = new javax.swing.JLabel();
        jftDateBenefits = new javax.swing.JFormattedTextField();
        jbDateBenefits = new javax.swing.JButton();
        jbDateBenefitsEdit = new javax.swing.JButton();
        jtfSeniority = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jlDateLastHire = new javax.swing.JLabel();
        jftDateLastHire = new javax.swing.JFormattedTextField();
        jbDateLastHire = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jlDateLastDismissal_n = new javax.swing.JLabel();
        jftDateLastDismissal_n = new javax.swing.JFormattedTextField();
        jPanel28 = new javax.swing.JPanel();
        jlFkPaymentType = new javax.swing.JLabel();
        jcbFkPaymentType = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel13 = new javax.swing.JPanel();
        jlFkSalaryType = new javax.swing.JLabel();
        jcbFkSalaryType = new javax.swing.JComboBox<SFormComponentItem>();
        jckChangeSalary = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        jlSalary = new javax.swing.JLabel();
        jtfSalary = new javax.swing.JTextField();
        jftDateChangeSalary = new javax.swing.JFormattedTextField();
        jbDateChangeSalary = new javax.swing.JButton();
        jckChangeWage = new javax.swing.JCheckBox();
        jPanel16 = new javax.swing.JPanel();
        jlWage = new javax.swing.JLabel();
        jtfWage = new javax.swing.JTextField();
        jftDateChangeWage = new javax.swing.JFormattedTextField();
        jbDateChangeWage = new javax.swing.JButton();
        jckChangeSalarySscBase = new javax.swing.JCheckBox();
        jPanel18 = new javax.swing.JPanel();
        jlSalarySscBase = new javax.swing.JLabel();
        jtfSalarySscBase = new javax.swing.JTextField();
        jftDateChangeSalarySscBase = new javax.swing.JFormattedTextField();
        jbDateChangeSalarySscBase = new javax.swing.JButton();
        jPanel31 = new javax.swing.JPanel();
        jlFkMwzType = new javax.swing.JLabel();
        jcbFkMwzType = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel4 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jlFkEmployeeType = new javax.swing.JLabel();
        jcbFkEmployeeType = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel15 = new javax.swing.JPanel();
        jlFkWorkerType = new javax.swing.JLabel();
        jcbFkWorkerType = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel12 = new javax.swing.JPanel();
        jckIsUnionized = new javax.swing.JCheckBox();
        jckIsMfgOperator = new javax.swing.JCheckBox();
        jlCheckerPolicy = new javax.swing.JLabel();
        jradCheckerNever = new javax.swing.JRadioButton();
        jradCheckerAllways = new javax.swing.JRadioButton();
        jradCheckerSometimes = new javax.swing.JRadioButton();
        jPanel17 = new javax.swing.JPanel();
        jlFkDepartment = new javax.swing.JLabel();
        jcbFkDepartment = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel32 = new javax.swing.JPanel();
        jlFkPosition = new javax.swing.JLabel();
        jcbFkPosition = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel21 = new javax.swing.JPanel();
        jlFkShift = new javax.swing.JLabel();
        jcbFkShift = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel44 = new javax.swing.JPanel();
        jlFkWorkingDayType = new javax.swing.JLabel();
        jcbFkWorkingDayType = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel11 = new javax.swing.JPanel();
        jlWorkingHoursDay = new javax.swing.JLabel();
        jtfWorkingHoursDay = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jlOvertimePolicy = new javax.swing.JLabel();
        jradOvertimeNever = new javax.swing.JRadioButton();
        jradOvertimeAllways = new javax.swing.JRadioButton();
        jradOvertimeSometimes = new javax.swing.JRadioButton();
        jPanel27 = new javax.swing.JPanel();
        jlFkContractType = new javax.swing.JLabel();
        jcbFkContractType = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel59 = new javax.swing.JPanel();
        jlContractExpiration = new javax.swing.JLabel();
        jftContractExpiration = new javax.swing.JFormattedTextField();
        jbContractExpiration = new javax.swing.JButton();
        jlContractExpirationHint = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jlFkRecruitmentSchemaType = new javax.swing.JLabel();
        jcbFkRecruitmentSchemaType = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel23 = new javax.swing.JPanel();
        jlFkPositionRiskType = new javax.swing.JLabel();
        jcbFkPositionRiskType = new javax.swing.JComboBox<SFormComponentItem>();
        jpPersonalData = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jlDateBirth = new javax.swing.JLabel();
        jftDateBirth = new javax.swing.JFormattedTextField();
        jbDateBirth = new javax.swing.JButton();
        jtfAge = new javax.swing.JTextField();
        jPanel61 = new javax.swing.JPanel();
        jlBirthPlace = new javax.swing.JLabel();
        jtfBirthPlace = new javax.swing.JTextField();
        jPanel62 = new javax.swing.JPanel();
        jlUmf = new javax.swing.JLabel();
        jtfUmf = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jlFkCatalogueSexTypeId = new javax.swing.JLabel();
        jcbFkCatalogueSexTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel37 = new javax.swing.JPanel();
        jlFkCatalogueBloodTypeTypeId = new javax.swing.JLabel();
        jcbFkCatalogueBloodTypeTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel39 = new javax.swing.JPanel();
        jlFkCatalogueEducationTypeId = new javax.swing.JLabel();
        jcbFkCatalogueEducationTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel38 = new javax.swing.JPanel();
        jlFkCatalogueMaritalStatusTypeId = new javax.swing.JLabel();
        jcbFkCatalogueMaritalStatusTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel63 = new javax.swing.JPanel();
        jlPhone = new javax.swing.JLabel();
        jtfPhone = new javax.swing.JTextField();
        jPanel40 = new javax.swing.JPanel();
        jPanel58 = new javax.swing.JPanel();
        jlRelative = new javax.swing.JLabel();
        jlRelativeName = new javax.swing.JLabel();
        jlRelativeDateBirth = new javax.swing.JLabel();
        jlRelativeSex1 = new javax.swing.JLabel();
        jlRelativeSex = new javax.swing.JLabel();
        jPanel52 = new javax.swing.JPanel();
        jlMate = new javax.swing.JLabel();
        jtfMateName = new javax.swing.JTextField();
        jftMateDateBirth = new javax.swing.JFormattedTextField();
        jbMateDateBirth = new javax.swing.JButton();
        jtfMateAge = new javax.swing.JTextField();
        jcbFkMateCatalogueSexTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jckMateDeceased = new javax.swing.JCheckBox();
        jPanel53 = new javax.swing.JPanel();
        jlSon1 = new javax.swing.JLabel();
        jtfSonName1 = new javax.swing.JTextField();
        jftSonDateBirth1 = new javax.swing.JFormattedTextField();
        jbSonDateBirth1 = new javax.swing.JButton();
        jtfSonAge1 = new javax.swing.JTextField();
        jcbFkSonCatalogueSexTypeId1 = new javax.swing.JComboBox<SFormComponentItem>();
        jckSonDeceased1 = new javax.swing.JCheckBox();
        jPanel54 = new javax.swing.JPanel();
        jlSon2 = new javax.swing.JLabel();
        jtfSonName2 = new javax.swing.JTextField();
        jftSonDateBirth2 = new javax.swing.JFormattedTextField();
        jbSonDateBirth2 = new javax.swing.JButton();
        jtfSonAge2 = new javax.swing.JTextField();
        jcbFkSonCatalogueSexTypeId2 = new javax.swing.JComboBox<SFormComponentItem>();
        jckSonDeceased2 = new javax.swing.JCheckBox();
        jPanel55 = new javax.swing.JPanel();
        jlSon3 = new javax.swing.JLabel();
        jtfSonName3 = new javax.swing.JTextField();
        jftSonDateBirth3 = new javax.swing.JFormattedTextField();
        jbSonDateBirth3 = new javax.swing.JButton();
        jtfSonAge3 = new javax.swing.JTextField();
        jcbFkSonCatalogueSexTypeId3 = new javax.swing.JComboBox<SFormComponentItem>();
        jckSonDeceased3 = new javax.swing.JCheckBox();
        jPanel56 = new javax.swing.JPanel();
        jlSon4 = new javax.swing.JLabel();
        jtfSonName4 = new javax.swing.JTextField();
        jftSonDateBirth4 = new javax.swing.JFormattedTextField();
        jbSonDateBirth4 = new javax.swing.JButton();
        jtfSonAge4 = new javax.swing.JTextField();
        jcbFkSonCatalogueSexTypeId4 = new javax.swing.JComboBox<SFormComponentItem>();
        jckSonDeceased4 = new javax.swing.JCheckBox();
        jPanel57 = new javax.swing.JPanel();
        jlSon5 = new javax.swing.JLabel();
        jtfSonName5 = new javax.swing.JTextField();
        jftSonDateBirth5 = new javax.swing.JFormattedTextField();
        jbSonDateBirth5 = new javax.swing.JButton();
        jtfSonAge5 = new javax.swing.JTextField();
        jcbFkSonCatalogueSexTypeId5 = new javax.swing.JComboBox<SFormComponentItem>();
        jckSonDeceased5 = new javax.swing.JCheckBox();
        jpBranchAddress = new javax.swing.JPanel();
        jpOficialAddress = new javax.swing.JPanel();
        jpBenefits = new javax.swing.JPanel();
        jpBenefitsEmployee = new javax.swing.JPanel();
        jpBenefitsEmployee1 = new javax.swing.JPanel();
        jlBenEmp = new javax.swing.JLabel();
        jtfBenEmpName = new javax.swing.JTextField();
        jtfBenEmpNumber = new javax.swing.JTextField();
        jlBenPaymentType = new javax.swing.JLabel();
        jtfBenPaymentType = new javax.swing.JTextField();
        jckBenIsUnionized = new javax.swing.JCheckBox();
        jpBenefitsEmployee2 = new javax.swing.JPanel();
        jlBenDateBenefits = new javax.swing.JLabel();
        jtfBenDateBenefits = new javax.swing.JTextField();
        jlBenDateCutoff = new javax.swing.JLabel();
        jtfBenDateCutoff = new javax.swing.JTextField();
        jlBenSeniority = new javax.swing.JLabel();
        jtfBenSeniorityYears = new javax.swing.JTextField();
        jlBenSeniorityYears = new javax.swing.JLabel();
        jtfBenSeniorityDays = new javax.swing.JTextField();
        jlBenSeniorityDays = new javax.swing.JLabel();
        jlBenSeniorityProp = new javax.swing.JLabel();
        jtfBenSeniorityProp = new javax.swing.JTextField();
        jpBenefitsTables = new javax.swing.JPanel();
        jpBenefitsVac = new javax.swing.JPanel();
        jpBenefitsVacEdit = new javax.swing.JPanel();
        jpBenefitsVac1 = new javax.swing.JPanel();
        jpBenefitsVac11 = new javax.swing.JPanel();
        jlVacTable = new javax.swing.JLabel();
        jcbVacTable = new javax.swing.JComboBox<SFormComponentItem>();
        jpBenefitsVac12 = new javax.swing.JPanel();
        jlVacPeriod = new javax.swing.JLabel();
        jtfVacPeriod = new javax.swing.JTextField();
        jpBenefitsVac13 = new javax.swing.JPanel();
        jlVacPaymentType = new javax.swing.JLabel();
        jtfVacPaymentType = new javax.swing.JTextField();
        jtfVacUnionized = new javax.swing.JTextField();
        jpBenefitsVac2 = new javax.swing.JPanel();
        jpBenefitsVac21 = new javax.swing.JPanel();
        jlVacSeniorityStart = new javax.swing.JLabel();
        jspVacSeniorityStart = new javax.swing.JSpinner();
        jlVacSeniorityStartHelp = new javax.swing.JLabel();
        jpBenefitsVac22 = new javax.swing.JPanel();
        jlVacLastUpdate = new javax.swing.JLabel();
        jtfVacLastUpdate = new javax.swing.JTextField();
        jtfVacLastUpdater = new javax.swing.JTextField();
        jpBenefitsVac23 = new javax.swing.JPanel();
        jbVacModify = new javax.swing.JButton();
        jbVacUpdate = new javax.swing.JButton();
        jbVacCancel = new javax.swing.JButton();
        jpBenefitsVacPane = new javax.swing.JPanel();
        jpBenefitsVacBon = new javax.swing.JPanel();
        jpBenefitsVacBonEdit = new javax.swing.JPanel();
        jpBenefitsVacBon1 = new javax.swing.JPanel();
        jpBenefitsVacBon11 = new javax.swing.JPanel();
        jlVacBonTable = new javax.swing.JLabel();
        jcbVacBonTable = new javax.swing.JComboBox<SFormComponentItem>();
        jpBenefitsVacBon12 = new javax.swing.JPanel();
        jlVacBonPeriod = new javax.swing.JLabel();
        jtfVacBonPeriod = new javax.swing.JTextField();
        jpBenefitsVacBon13 = new javax.swing.JPanel();
        jlVacBonPaymentType = new javax.swing.JLabel();
        jtfVacBonPaymentType = new javax.swing.JTextField();
        jtfVacBonUnionized = new javax.swing.JTextField();
        jpBenefitsVacBon2 = new javax.swing.JPanel();
        jpBenefitsVacBon21 = new javax.swing.JPanel();
        jlVacBonSeniorityStart = new javax.swing.JLabel();
        jspVacBonSeniorityStart = new javax.swing.JSpinner();
        jlVacBonSeniorityStartHellp = new javax.swing.JLabel();
        jpBenefitsVacBon22 = new javax.swing.JPanel();
        jlVacBonLastUpdate = new javax.swing.JLabel();
        jtfVacBonLastUpdate = new javax.swing.JTextField();
        jtfVacBonLastUpdater = new javax.swing.JTextField();
        jpBenefitsVacBon23 = new javax.swing.JPanel();
        jbVacBonModify = new javax.swing.JButton();
        jbVacBonUpdate = new javax.swing.JButton();
        jbVacBonCancel = new javax.swing.JButton();
        jpBenefitsVacBonPane = new javax.swing.JPanel();
        jpBenefitsAnnBon = new javax.swing.JPanel();
        jpBenefitsAnnBonEdit = new javax.swing.JPanel();
        jpBenefitsAnnBon1 = new javax.swing.JPanel();
        jpBenefitsAnnBon11 = new javax.swing.JPanel();
        jlAnnBonTable = new javax.swing.JLabel();
        jcbAnnBonTable = new javax.swing.JComboBox<SFormComponentItem>();
        jpBenefitsAnnBon12 = new javax.swing.JPanel();
        jlAnnBonPeriod = new javax.swing.JLabel();
        jtfAnnBonPeriod = new javax.swing.JTextField();
        jpBenefitsAnnBon13 = new javax.swing.JPanel();
        jlAnnBonPaymentType = new javax.swing.JLabel();
        jtfAnnBonPaymentType = new javax.swing.JTextField();
        jtfAnnBonUnionized = new javax.swing.JTextField();
        jpBenefitsAnnBon2 = new javax.swing.JPanel();
        jpBenefitsAnnBon21 = new javax.swing.JPanel();
        jlAnnBonSeniorityStart = new javax.swing.JLabel();
        jspAnnBonSeniorityStart = new javax.swing.JSpinner();
        jlAnnBonSeniorityStartHelp = new javax.swing.JLabel();
        jpBenefitsAnnBon22 = new javax.swing.JPanel();
        jlAnnBonLastUpdate = new javax.swing.JLabel();
        jtfAnnBonLastUpdate = new javax.swing.JTextField();
        jtfAnnBonLastUpdater = new javax.swing.JTextField();
        jpBenefitsAnnBon23 = new javax.swing.JPanel();
        jbAnnBonModify = new javax.swing.JButton();
        jbAnnBonUpdate = new javax.swing.JButton();
        jbAnnBonCancel = new javax.swing.JButton();
        jpBenefitsAnnBonPane = new javax.swing.JPanel();
        jpWageFactors = new javax.swing.JPanel();
        jpWageFactorsEmployee = new javax.swing.JPanel();
        jpWageFactorsEmployee1 = new javax.swing.JPanel();
        jlFacEmp = new javax.swing.JLabel();
        jtfFacEmpName = new javax.swing.JTextField();
        jtfFacEmpNumber = new javax.swing.JTextField();
        jpFacPaymentType = new javax.swing.JLabel();
        jtfFacPaymentType = new javax.swing.JTextField();
        jckFacIsUnionized = new javax.swing.JCheckBox();
        jpWageFactorsEmployee2 = new javax.swing.JPanel();
        jlFacDateBenefits = new javax.swing.JLabel();
        jtfFacDateBenefits = new javax.swing.JTextField();
        jlFacDateCutoff = new javax.swing.JLabel();
        jtfFacDateCutoff = new javax.swing.JTextField();
        jlFacSeniority = new javax.swing.JLabel();
        jtfFacSeniorityYears = new javax.swing.JTextField();
        jlFacSeniorityYears = new javax.swing.JLabel();
        jtfFacSeniorityDays = new javax.swing.JTextField();
        jlFacSeniorityDays = new javax.swing.JLabel();
        jlFacSeniorityProp = new javax.swing.JLabel();
        jtfFacSeniorityProp = new javax.swing.JTextField();
        jpWageFactorsPane = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jtfPkBizPartnerId_Ro = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Asociado de negocios");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpEmployee.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del empleado:"));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel19.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNumber.setText("Número empleado:*");
        jlNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel26.add(jlNumber);

        jftNumber.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        jftNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel26.add(jftNumber);

        jtfBizPartner_Ro.setEditable(false);
        jtfBizPartner_Ro.setToolTipText("Nombre empleado");
        jtfBizPartner_Ro.setFocusable(false);
        jtfBizPartner_Ro.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel26.add(jtfBizPartner_Ro);

        jPanel19.add(jPanel26);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLastname.setText("Apellido paterno:*");
        jlLastname.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel25.add(jlLastname);

        jtfLastname1.setToolTipText("Apellido paterno");
        jtfLastname1.setPreferredSize(new java.awt.Dimension(200, 23));
        jtfLastname1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfLastname1FocusLost(evt);
            }
        });
        jPanel25.add(jtfLastname1);

        jlLastname1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlLastname1.setText("Apellido materno:*");
        jlLastname1.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel25.add(jlLastname1);

        jtfLastname2.setToolTipText("Apellido materno");
        jtfLastname2.setPreferredSize(new java.awt.Dimension(200, 23));
        jtfLastname2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfLastname2FocusLost(evt);
            }
        });
        jPanel25.add(jtfLastname2);

        jPanel19.add(jPanel25);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFirstname.setText("Nombre(s):*");
        jlFirstname.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel22.add(jlFirstname);

        jtfFirstname.setPreferredSize(new java.awt.Dimension(200, 23));
        jtfFirstname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfFirstnameFocusLost(evt);
            }
        });
        jPanel22.add(jtfFirstname);

        jPanel19.add(jPanel22);

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFiscalId.setText("RFC:*");
        jlFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel29.add(jlFiscalId);

        jtfFiscalId.setText("XAXX010101000");
        jtfFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel29.add(jtfFiscalId);

        jlAlternativeId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlAlternativeId.setText("CURP:*");
        jlAlternativeId.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel29.add(jlAlternativeId);

        jftAlternativeId.setText("XAXX010101XXXXXX00");
        jftAlternativeId.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        jftAlternativeId.setPreferredSize(new java.awt.Dimension(150, 23));
        jftAlternativeId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jftAlternativeIdFocusLost(evt);
            }
        });
        jPanel29.add(jftAlternativeId);

        jlSocialSecurityNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlSocialSecurityNumber.setText("NSS:");
        jlSocialSecurityNumber.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel29.add(jlSocialSecurityNumber);

        jtfSocialSecurityNumber.setText("00000000000");
        jtfSocialSecurityNumber.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel29.add(jtfSocialSecurityNumber);

        jPanel19.add(jPanel29);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkBank_n.setText("Banco:");
        jlFkBank_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel24.add(jlFkBank_n);

        jcbFkBank_n.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel24.add(jcbFkBank_n);

        jlBankAccount.setText("Cuenta bancaria:");
        jlBankAccount.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel24.add(jlBankAccount);

        jtfBankAccount.setText("000000000000000000");
        jtfBankAccount.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel24.add(jtfBankAccount);

        jPanel19.add(jPanel24);

        jPanel42.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkGroceryService.setText("Prov. despensa:*");
        jlFkGroceryService.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel42.add(jlFkGroceryService);

        jcbFkGroceryService.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel42.add(jcbFkGroceryService);

        jlGroceryServiceAccount.setText("Cuenta despensa:");
        jlGroceryServiceAccount.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel42.add(jlGroceryServiceAccount);

        jtfGroceryServiceAccount.setText("000000000000000000");
        jtfGroceryServiceAccount.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel42.add(jtfGroceryServiceAccount);

        jPanel19.add(jPanel42);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlMailOwn.setForeground(new java.awt.Color(0, 102, 102));
        jlMailOwn.setText("Correo-e personal:");
        jlMailOwn.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlMailOwn);

        jtfMailOwn.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel6.add(jtfMailOwn);

        jlMailCompany.setForeground(new java.awt.Color(0, 102, 102));
        jlMailCompany.setText("Correo-e empresa:");
        jlMailCompany.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlMailCompany);

        jtfMailCompany.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel6.add(jtfMailCompany);

        jPanel19.add(jPanel6);

        jPanel7.add(jPanel19, java.awt.BorderLayout.CENTER);

        jPanel48.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel47.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlImgPhoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlImgPhoto.setText("Foto");
        jlImgPhoto.setToolTipText("Foto (tamaño sugerido: 100×100 px)");
        jlImgPhoto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jlImgPhoto.setPreferredSize(new java.awt.Dimension(100, 100));
        jPanel47.add(jlImgPhoto);

        jlImgSignature.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlImgSignature.setText("Firma");
        jlImgSignature.setToolTipText("Firma (tamaño sugerido: 250×100 px)");
        jlImgSignature.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jlImgSignature.setPreferredSize(new java.awt.Dimension(250, 100));
        jPanel47.add(jlImgSignature);

        jPanel48.add(jPanel47, java.awt.BorderLayout.NORTH);

        jPanel49.setLayout(new java.awt.BorderLayout());

        jPanel50.setLayout(new java.awt.GridLayout(3, 0, 0, 5));

        jPanel45.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlImagePhoto.setText("Foto:");
        jlImagePhoto.setToolTipText("Tamaño sugerido: 100×100 px");
        jlImagePhoto.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel45.add(jlImagePhoto);

        jtfImagePhoto.setEditable(false);
        jtfImagePhoto.setToolTipText("Tamaño sugerido: 100×100 px");
        jtfImagePhoto.setFocusable(false);
        jtfImagePhoto.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel45.add(jtfImagePhoto);

        jbImagePhoto.setText("jButton3");
        jbImagePhoto.setToolTipText("Seleccionar foto (tamaño sugerido: 100×100 px)");
        jbImagePhoto.setFocusable(false);
        jbImagePhoto.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel45.add(jbImagePhoto);

        jbImagePhotoView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif"))); // NOI18N
        jbImagePhotoView.setToolTipText("Ver foto del empleado");
        jbImagePhotoView.setFocusable(false);
        jbImagePhotoView.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel45.add(jbImagePhotoView);

        jbImagePhotoRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbImagePhotoRemove.setToolTipText("Eliminar foto del empleado");
        jbImagePhotoRemove.setFocusable(false);
        jbImagePhotoRemove.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel45.add(jbImagePhotoRemove);

        jPanel50.add(jPanel45);

        jPanel46.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlImageSignature.setText("Firma:");
        jlImageSignature.setToolTipText("Tamaño sugerido: 250×100 px");
        jlImageSignature.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel46.add(jlImageSignature);

        jtfImageSignature.setEditable(false);
        jtfImageSignature.setToolTipText("Tamaño sugerido: 250×100 px");
        jtfImageSignature.setFocusable(false);
        jtfImageSignature.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel46.add(jtfImageSignature);

        jbImageSignature.setText("jButton3");
        jbImageSignature.setToolTipText("Seleccionar firma (tamaño sugerido: 250×100 px)");
        jbImageSignature.setFocusable(false);
        jbImageSignature.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel46.add(jbImageSignature);

        jbImageSignatureView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif"))); // NOI18N
        jbImageSignatureView.setToolTipText("Ver firma del empleado");
        jbImageSignatureView.setFocusable(false);
        jbImageSignatureView.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel46.add(jbImageSignatureView);

        jbImageSignatureRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbImageSignatureRemove.setToolTipText("Eliminar firma del empleado");
        jbImageSignatureRemove.setFocusable(false);
        jbImageSignatureRemove.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel46.add(jbImageSignatureRemove);

        jPanel50.add(jPanel46);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jckIsActive.setText("Está activo");
        jckIsActive.setEnabled(false);
        jckIsActive.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jckIsActive.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel2.add(jckIsActive);

        jckIsDeleted.setForeground(new java.awt.Color(204, 0, 0));
        jckIsDeleted.setText("AN eliminado");
        jckIsDeleted.setToolTipText("Asociado de negocios eliminado");
        jckIsDeleted.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel2.add(jckIsDeleted);

        jPanel50.add(jPanel2);

        jPanel49.add(jPanel50, java.awt.BorderLayout.NORTH);

        jPanel48.add(jPanel49, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel48, java.awt.BorderLayout.EAST);

        jpEmployee.add(jPanel7, java.awt.BorderLayout.NORTH);

        jPanel60.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos laborales:"));
        jPanel60.setLayout(new java.awt.BorderLayout());

        jPanel20.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridLayout(13, 1, 0, 5));

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlDateBenefits.setText("Inicio prestaciones:*");
        jlDateBenefits.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlDateBenefits);

        jftDateBenefits.setText("yyyy/mm/dd");
        jftDateBenefits.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel8.add(jftDateBenefits);

        jbDateBenefits.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateBenefits.setToolTipText("Seleccionar fecha");
        jbDateBenefits.setFocusable(false);
        jbDateBenefits.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel8.add(jbDateBenefits);

        jbDateBenefitsEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbDateBenefitsEdit.setToolTipText("Modificar fecha");
        jbDateBenefitsEdit.setFocusable(false);
        jbDateBenefitsEdit.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel8.add(jbDateBenefitsEdit);

        jtfSeniority.setEditable(false);
        jtfSeniority.setText("99 a, 99 m");
        jtfSeniority.setToolTipText("Antigüedad");
        jtfSeniority.setFocusable(false);
        jtfSeniority.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel8.add(jtfSeniority);

        jPanel3.add(jPanel8);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlDateLastHire.setText("Última alta:*");
        jlDateLastHire.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlDateLastHire);

        jftDateLastHire.setText("yyyy/mm/dd");
        jftDateLastHire.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel14.add(jftDateLastHire);

        jbDateLastHire.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateLastHire.setToolTipText("Seleccionar fecha");
        jbDateLastHire.setFocusable(false);
        jbDateLastHire.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel14.add(jbDateLastHire);

        jPanel3.add(jPanel14);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlDateLastDismissal_n.setText("Última baja:");
        jlDateLastDismissal_n.setEnabled(false);
        jlDateLastDismissal_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlDateLastDismissal_n);

        jftDateLastDismissal_n.setEditable(false);
        jftDateLastDismissal_n.setText("yyyy/mm/dd");
        jftDateLastDismissal_n.setFocusable(false);
        jftDateLastDismissal_n.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel9.add(jftDateLastDismissal_n);

        jPanel3.add(jPanel9);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkPaymentType.setText("Período pago:*");
        jlFkPaymentType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel28.add(jlFkPaymentType);

        jcbFkPaymentType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel28.add(jcbFkPaymentType);

        jPanel3.add(jPanel28);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkSalaryType.setText("Tipo salario:*");
        jlFkSalaryType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.add(jlFkSalaryType);

        jcbFkSalaryType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel13.add(jcbFkSalaryType);

        jPanel3.add(jPanel13);

        jckChangeSalary.setText("Cambiar salario diario");
        jckChangeSalary.setFocusable(false);
        jPanel3.add(jckChangeSalary);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlSalary.setText("Salario diario:*");
        jlSalary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlSalary);

        jtfSalary.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSalary.setText("0.0000");
        jtfSalary.setToolTipText("Unidades");
        jtfSalary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jtfSalary);

        jftDateChangeSalary.setText("yyyy/mm/dd");
        jftDateChangeSalary.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jftDateChangeSalary);

        jbDateChangeSalary.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateChangeSalary.setToolTipText("Seleccionar fecha");
        jbDateChangeSalary.setFocusable(false);
        jbDateChangeSalary.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel10.add(jbDateChangeSalary);

        jPanel3.add(jPanel10);

        jckChangeWage.setText("Cambiar sueldo mensual");
        jckChangeWage.setFocusable(false);
        jPanel3.add(jckChangeWage);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlWage.setText("Sueldo mensual:*");
        jlWage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel16.add(jlWage);

        jtfWage.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfWage.setText("0.0000");
        jtfWage.setToolTipText("Unidades");
        jtfWage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel16.add(jtfWage);

        jftDateChangeWage.setText("yyyy/mm/dd");
        jftDateChangeWage.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel16.add(jftDateChangeWage);

        jbDateChangeWage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateChangeWage.setToolTipText("Seleccionar fecha");
        jbDateChangeWage.setFocusable(false);
        jbDateChangeWage.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel16.add(jbDateChangeWage);

        jPanel3.add(jPanel16);

        jckChangeSalarySscBase.setText("Cambiar Salario Base de Cotización (SBC)");
        jckChangeSalarySscBase.setFocusable(false);
        jPanel3.add(jckChangeSalarySscBase);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlSalarySscBase.setText("SBC:*");
        jlSalarySscBase.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel18.add(jlSalarySscBase);

        jtfSalarySscBase.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSalarySscBase.setText("0.0000");
        jtfSalarySscBase.setToolTipText("Unidades");
        jtfSalarySscBase.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel18.add(jtfSalarySscBase);

        jftDateChangeSalarySscBase.setText("yyyy/mm/dd");
        jftDateChangeSalarySscBase.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel18.add(jftDateChangeSalarySscBase);

        jbDateChangeSalarySscBase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateChangeSalarySscBase.setToolTipText("Seleccionar fecha");
        jbDateChangeSalarySscBase.setFocusable(false);
        jbDateChangeSalarySscBase.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel18.add(jbDateChangeSalarySscBase);

        jPanel3.add(jPanel18);

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkMwzType.setText("Área geográfica:*");
        jlFkMwzType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel31.add(jlFkMwzType);

        jcbFkMwzType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel31.add(jcbFkMwzType);

        jPanel3.add(jPanel31);

        jPanel20.add(jPanel3, java.awt.BorderLayout.WEST);

        jPanel4.setLayout(new java.awt.GridLayout(13, 1, 0, 5));

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkEmployeeType.setText("Tipo empleado:*");
        jlFkEmployeeType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel30.add(jlFkEmployeeType);

        jcbFkEmployeeType.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel30.add(jcbFkEmployeeType);

        jPanel4.add(jPanel30);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkWorkerType.setText("Tipo obrero:*");
        jlFkWorkerType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel15.add(jlFkWorkerType);

        jcbFkWorkerType.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel15.add(jcbFkWorkerType);

        jPanel4.add(jPanel15);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jckIsUnionized.setText("Es sindicalizado");
        jckIsUnionized.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel12.add(jckIsUnionized);

        jckIsMfgOperator.setText("Es operador");
        jckIsMfgOperator.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jckIsMfgOperator);

        jlCheckerPolicy.setText("Reloj checador:");
        jlCheckerPolicy.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlCheckerPolicy);

        bgCheckerPolicy.add(jradCheckerNever);
        jradCheckerNever.setText("Nunca");
        jradCheckerNever.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel12.add(jradCheckerNever);

        bgCheckerPolicy.add(jradCheckerAllways);
        jradCheckerAllways.setText("Siempre");
        jradCheckerAllways.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel12.add(jradCheckerAllways);

        bgCheckerPolicy.add(jradCheckerSometimes);
        jradCheckerSometimes.setText("Ocasionalmente");
        jradCheckerSometimes.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel12.add(jradCheckerSometimes);

        jPanel4.add(jPanel12);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkDepartment.setText("Departamento:*");
        jlFkDepartment.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel17.add(jlFkDepartment);

        jcbFkDepartment.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel17.add(jcbFkDepartment);

        jPanel4.add(jPanel17);

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkPosition.setText("Puesto:*");
        jlFkPosition.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel32.add(jlFkPosition);

        jcbFkPosition.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel32.add(jcbFkPosition);

        jPanel4.add(jPanel32);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkShift.setText("Turno:*");
        jlFkShift.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel21.add(jlFkShift);

        jcbFkShift.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel21.add(jcbFkShift);

        jPanel4.add(jPanel21);

        jPanel44.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkWorkingDayType.setText("Tipo jornada:");
        jlFkWorkingDayType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel44.add(jlFkWorkingDayType);

        jcbFkWorkingDayType.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel44.add(jcbFkWorkingDayType);

        jPanel4.add(jPanel44);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlWorkingHoursDay.setText("Horas jornada:*");
        jlWorkingHoursDay.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel11.add(jlWorkingHoursDay);

        jtfWorkingHoursDay.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfWorkingHoursDay.setText("0");
        jtfWorkingHoursDay.setToolTipText("Unidades");
        jtfWorkingHoursDay.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel11.add(jtfWorkingHoursDay);

        jLabel1.setPreferredSize(new java.awt.Dimension(45, 23));
        jPanel11.add(jLabel1);

        jlOvertimePolicy.setText("Tiempo extra:");
        jlOvertimePolicy.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlOvertimePolicy);

        bgOvertime.add(jradOvertimeNever);
        jradOvertimeNever.setText("Nunca");
        jradOvertimeNever.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel11.add(jradOvertimeNever);

        bgOvertime.add(jradOvertimeAllways);
        jradOvertimeAllways.setText("Siempre");
        jradOvertimeAllways.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel11.add(jradOvertimeAllways);

        bgOvertime.add(jradOvertimeSometimes);
        jradOvertimeSometimes.setText("Ocasionalmente");
        jradOvertimeSometimes.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel11.add(jradOvertimeSometimes);

        jPanel4.add(jPanel11);

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkContractType.setText("Tipo contrato:*");
        jlFkContractType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel27.add(jlFkContractType);

        jcbFkContractType.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel27.add(jcbFkContractType);

        jPanel4.add(jPanel27);

        jPanel59.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlContractExpiration.setText("Terminación contrato:");
        jlContractExpiration.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel59.add(jlContractExpiration);

        jftContractExpiration.setText("yyyy/mm/dd");
        jftContractExpiration.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel59.add(jftContractExpiration);

        jbContractExpiration.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbContractExpiration.setToolTipText("Seleccionar fecha");
        jbContractExpiration.setFocusable(false);
        jbContractExpiration.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel59.add(jbContractExpiration);

        jlContractExpirationHint.setForeground(java.awt.SystemColor.textInactiveText);
        jlContractExpirationHint.setText("(requerido | opcional)");
        jlContractExpirationHint.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel59.add(jlContractExpirationHint);

        jPanel4.add(jPanel59);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkRecruitmentSchemaType.setText("Tipo régimen:*");
        jlFkRecruitmentSchemaType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel33.add(jlFkRecruitmentSchemaType);

        jcbFkRecruitmentSchemaType.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel33.add(jcbFkRecruitmentSchemaType);

        jPanel4.add(jPanel33);

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkPositionRiskType.setText("Riesgo trabajo:*");
        jlFkPositionRiskType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel23.add(jlFkPositionRiskType);

        jcbFkPositionRiskType.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel23.add(jcbFkPositionRiskType);

        jPanel4.add(jPanel23);

        jPanel20.add(jPanel4, java.awt.BorderLayout.EAST);

        jPanel60.add(jPanel20, java.awt.BorderLayout.NORTH);

        jpEmployee.add(jPanel60, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Datos laborales", jpEmployee);

        jpPersonalData.setLayout(new java.awt.BorderLayout());

        jPanel51.setLayout(new java.awt.BorderLayout(5, 0));

        jPanel43.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del empleado:"));
        jPanel43.setLayout(new java.awt.GridLayout(8, 0, 0, 5));

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlDateBirth.setText("Nacimiento:*");
        jlDateBirth.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel34.add(jlDateBirth);

        jftDateBirth.setText("yyyy/mm/dd");
        jftDateBirth.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel34.add(jftDateBirth);

        jbDateBirth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateBirth.setToolTipText("Seleccionar fecha");
        jbDateBirth.setFocusable(false);
        jbDateBirth.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel34.add(jbDateBirth);

        jtfAge.setEditable(false);
        jtfAge.setText("99 a, 99 m");
        jtfAge.setToolTipText("Edad");
        jtfAge.setFocusable(false);
        jtfAge.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel34.add(jtfAge);

        jPanel43.add(jPanel34);

        jPanel61.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlBirthPlace.setText("Edo. nacimiento:");
        jlBirthPlace.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel61.add(jlBirthPlace);

        jtfBirthPlace.setToolTipText("Estado nacimiento para IDSE");
        jtfBirthPlace.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel61.add(jtfBirthPlace);

        jPanel43.add(jPanel61);

        jPanel62.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlUmf.setText("Clave UMF:");
        jlUmf.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel62.add(jlUmf);
        jlUmf.getAccessibleContext().setAccessibleDescription("");

        jtfUmf.setToolTipText("Clave UMF para IDSE");
        jtfUmf.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel62.add(jtfUmf);

        jLabel2.setForeground(java.awt.SystemColor.textInactiveText);
        jLabel2.setText("(Unidad de Medicina Familiar)");
        jLabel2.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel62.add(jLabel2);

        jPanel43.add(jPanel62);

        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCatalogueSexTypeId.setText("Sexo:*");
        jlFkCatalogueSexTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel36.add(jlFkCatalogueSexTypeId);

        jcbFkCatalogueSexTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel36.add(jcbFkCatalogueSexTypeId);

        jPanel43.add(jPanel36);

        jPanel37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCatalogueBloodTypeTypeId.setText("Tipo sangre:*");
        jlFkCatalogueBloodTypeTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel37.add(jlFkCatalogueBloodTypeTypeId);

        jcbFkCatalogueBloodTypeTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel37.add(jcbFkCatalogueBloodTypeTypeId);

        jPanel43.add(jPanel37);

        jPanel39.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCatalogueEducationTypeId.setText("Escolaridad:*");
        jlFkCatalogueEducationTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel39.add(jlFkCatalogueEducationTypeId);

        jcbFkCatalogueEducationTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel39.add(jcbFkCatalogueEducationTypeId);

        jPanel43.add(jPanel39);

        jPanel38.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCatalogueMaritalStatusTypeId.setText("Estado civil:*");
        jlFkCatalogueMaritalStatusTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel38.add(jlFkCatalogueMaritalStatusTypeId);

        jcbFkCatalogueMaritalStatusTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel38.add(jcbFkCatalogueMaritalStatusTypeId);

        jPanel43.add(jPanel38);

        jPanel63.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlPhone.setForeground(new java.awt.Color(0, 102, 102));
        jlPhone.setText("Teléfono:");
        jlPhone.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel63.add(jlPhone);

        jtfPhone.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel63.add(jtfPhone);

        jPanel43.add(jPanel63);

        jPanel51.add(jPanel43, java.awt.BorderLayout.CENTER);

        jPanel40.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos familiares del empleado:"));
        jPanel40.setLayout(new java.awt.GridLayout(8, 1, 0, 5));

        jPanel58.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRelative.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel58.add(jlRelative);

        jlRelativeName.setText("Nombre:");
        jlRelativeName.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel58.add(jlRelativeName);

        jlRelativeDateBirth.setText("Nacimiento:");
        jlRelativeDateBirth.setPreferredSize(new java.awt.Dimension(103, 23));
        jPanel58.add(jlRelativeDateBirth);

        jlRelativeSex1.setText("Edad:");
        jlRelativeSex1.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel58.add(jlRelativeSex1);

        jlRelativeSex.setText("Sexo:");
        jlRelativeSex.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel58.add(jlRelativeSex);

        jPanel40.add(jPanel58);

        jPanel52.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMate.setText("Cónyuge:");
        jlMate.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel52.add(jlMate);

        jtfMateName.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel52.add(jtfMateName);

        jftMateDateBirth.setText("yyyy/mm/dd");
        jftMateDateBirth.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel52.add(jftMateDateBirth);

        jbMateDateBirth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbMateDateBirth.setToolTipText("Seleccionar fecha");
        jbMateDateBirth.setFocusable(false);
        jbMateDateBirth.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel52.add(jbMateDateBirth);

        jtfMateAge.setEditable(false);
        jtfMateAge.setText("99 a, 99 m");
        jtfMateAge.setToolTipText("Edad");
        jtfMateAge.setFocusable(false);
        jtfMateAge.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel52.add(jtfMateAge);

        jcbFkMateCatalogueSexTypeId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel52.add(jcbFkMateCatalogueSexTypeId);

        jckMateDeceased.setText("Finado(a)");
        jckMateDeceased.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel52.add(jckMateDeceased);

        jPanel40.add(jPanel52);

        jPanel53.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSon1.setText("Hijo(a) 1:");
        jlSon1.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel53.add(jlSon1);

        jtfSonName1.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel53.add(jtfSonName1);

        jftSonDateBirth1.setText("yyyy/mm/dd");
        jftSonDateBirth1.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel53.add(jftSonDateBirth1);

        jbSonDateBirth1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbSonDateBirth1.setToolTipText("Seleccionar fecha");
        jbSonDateBirth1.setFocusable(false);
        jbSonDateBirth1.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel53.add(jbSonDateBirth1);

        jtfSonAge1.setEditable(false);
        jtfSonAge1.setText("99 a, 99 m");
        jtfSonAge1.setToolTipText("Edad");
        jtfSonAge1.setFocusable(false);
        jtfSonAge1.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel53.add(jtfSonAge1);

        jcbFkSonCatalogueSexTypeId1.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel53.add(jcbFkSonCatalogueSexTypeId1);

        jckSonDeceased1.setText("Finado(a)");
        jckSonDeceased1.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel53.add(jckSonDeceased1);

        jPanel40.add(jPanel53);

        jPanel54.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSon2.setText("Hijo(a) 2:");
        jlSon2.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel54.add(jlSon2);

        jtfSonName2.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel54.add(jtfSonName2);

        jftSonDateBirth2.setText("yyyy/mm/dd");
        jftSonDateBirth2.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel54.add(jftSonDateBirth2);

        jbSonDateBirth2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbSonDateBirth2.setToolTipText("Seleccionar fecha");
        jbSonDateBirth2.setFocusable(false);
        jbSonDateBirth2.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel54.add(jbSonDateBirth2);

        jtfSonAge2.setEditable(false);
        jtfSonAge2.setText("99 a, 99 m");
        jtfSonAge2.setToolTipText("Edad");
        jtfSonAge2.setFocusable(false);
        jtfSonAge2.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel54.add(jtfSonAge2);

        jcbFkSonCatalogueSexTypeId2.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel54.add(jcbFkSonCatalogueSexTypeId2);

        jckSonDeceased2.setText("Finado(a)");
        jckSonDeceased2.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel54.add(jckSonDeceased2);

        jPanel40.add(jPanel54);

        jPanel55.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSon3.setText("Hijo(a) 3:");
        jlSon3.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel55.add(jlSon3);

        jtfSonName3.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel55.add(jtfSonName3);

        jftSonDateBirth3.setText("yyyy/mm/dd");
        jftSonDateBirth3.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel55.add(jftSonDateBirth3);

        jbSonDateBirth3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbSonDateBirth3.setToolTipText("Seleccionar fecha");
        jbSonDateBirth3.setFocusable(false);
        jbSonDateBirth3.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel55.add(jbSonDateBirth3);

        jtfSonAge3.setEditable(false);
        jtfSonAge3.setText("99 a, 99 m");
        jtfSonAge3.setToolTipText("Edad");
        jtfSonAge3.setFocusable(false);
        jtfSonAge3.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel55.add(jtfSonAge3);

        jcbFkSonCatalogueSexTypeId3.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel55.add(jcbFkSonCatalogueSexTypeId3);

        jckSonDeceased3.setText("Finado(a)");
        jckSonDeceased3.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel55.add(jckSonDeceased3);

        jPanel40.add(jPanel55);

        jPanel56.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSon4.setText("Hijo(a) 4:");
        jlSon4.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel56.add(jlSon4);

        jtfSonName4.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel56.add(jtfSonName4);

        jftSonDateBirth4.setText("yyyy/mm/dd");
        jftSonDateBirth4.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel56.add(jftSonDateBirth4);

        jbSonDateBirth4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbSonDateBirth4.setToolTipText("Seleccionar fecha");
        jbSonDateBirth4.setFocusable(false);
        jbSonDateBirth4.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel56.add(jbSonDateBirth4);

        jtfSonAge4.setEditable(false);
        jtfSonAge4.setText("99 a, 99 m");
        jtfSonAge4.setToolTipText("Edad");
        jtfSonAge4.setFocusable(false);
        jtfSonAge4.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel56.add(jtfSonAge4);

        jcbFkSonCatalogueSexTypeId4.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel56.add(jcbFkSonCatalogueSexTypeId4);

        jckSonDeceased4.setText("Finado(a)");
        jckSonDeceased4.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel56.add(jckSonDeceased4);

        jPanel40.add(jPanel56);

        jPanel57.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSon5.setText("Hijo(a) 5:");
        jlSon5.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel57.add(jlSon5);

        jtfSonName5.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel57.add(jtfSonName5);

        jftSonDateBirth5.setText("yyyy/mm/dd");
        jftSonDateBirth5.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel57.add(jftSonDateBirth5);

        jbSonDateBirth5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbSonDateBirth5.setToolTipText("Seleccionar fecha");
        jbSonDateBirth5.setFocusable(false);
        jbSonDateBirth5.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel57.add(jbSonDateBirth5);

        jtfSonAge5.setEditable(false);
        jtfSonAge5.setText("99 a, 99 m");
        jtfSonAge5.setToolTipText("Edad");
        jtfSonAge5.setFocusable(false);
        jtfSonAge5.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel57.add(jtfSonAge5);

        jcbFkSonCatalogueSexTypeId5.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel57.add(jcbFkSonCatalogueSexTypeId5);

        jckSonDeceased5.setText("Finado(a)");
        jckSonDeceased5.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel57.add(jckSonDeceased5);

        jPanel40.add(jPanel57);

        jPanel51.add(jPanel40, java.awt.BorderLayout.EAST);

        jpPersonalData.add(jPanel51, java.awt.BorderLayout.NORTH);

        jpBranchAddress.setBorder(javax.swing.BorderFactory.createTitledBorder("Información del domicilio:"));
        jpBranchAddress.setLayout(new java.awt.BorderLayout());

        jpOficialAddress.setLayout(new java.awt.BorderLayout());
        jpBranchAddress.add(jpOficialAddress, java.awt.BorderLayout.CENTER);

        jpPersonalData.add(jpBranchAddress, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Datos personales", jpPersonalData);

        jpBenefits.setLayout(new java.awt.BorderLayout());

        jpBenefitsEmployee.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del empleado:"));
        jpBenefitsEmployee.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jpBenefitsEmployee1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBenEmp.setText("Empleado:");
        jlBenEmp.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsEmployee1.add(jlBenEmp);

        jtfBenEmpName.setEditable(false);
        jtfBenEmpName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfBenEmpName.setText("TEXT");
        jtfBenEmpName.setFocusable(false);
        jtfBenEmpName.setPreferredSize(new java.awt.Dimension(360, 23));
        jpBenefitsEmployee1.add(jtfBenEmpName);

        jtfBenEmpNumber.setEditable(false);
        jtfBenEmpNumber.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfBenEmpNumber.setText("TEXT");
        jtfBenEmpNumber.setToolTipText("Número empleado");
        jtfBenEmpNumber.setFocusable(false);
        jtfBenEmpNumber.setPreferredSize(new java.awt.Dimension(75, 23));
        jpBenefitsEmployee1.add(jtfBenEmpNumber);

        jlBenPaymentType.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBenPaymentType.setText("Período pago:");
        jlBenPaymentType.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsEmployee1.add(jlBenPaymentType);

        jtfBenPaymentType.setEditable(false);
        jtfBenPaymentType.setText("TEXT");
        jtfBenPaymentType.setFocusable(false);
        jtfBenPaymentType.setPreferredSize(new java.awt.Dimension(200, 23));
        jpBenefitsEmployee1.add(jtfBenPaymentType);

        jckBenIsUnionized.setText("Es sindicalizado");
        jckBenIsUnionized.setEnabled(false);
        jckBenIsUnionized.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jckBenIsUnionized.setPreferredSize(new java.awt.Dimension(125, 23));
        jpBenefitsEmployee1.add(jckBenIsUnionized);

        jpBenefitsEmployee.add(jpBenefitsEmployee1);

        jpBenefitsEmployee2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBenDateBenefits.setText("Inicio prestaciones:");
        jlBenDateBenefits.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsEmployee2.add(jlBenDateBenefits);

        jtfBenDateBenefits.setEditable(false);
        jtfBenDateBenefits.setText("01/01/2001");
        jtfBenDateBenefits.setFocusable(false);
        jtfBenDateBenefits.setPreferredSize(new java.awt.Dimension(75, 23));
        jpBenefitsEmployee2.add(jtfBenDateBenefits);

        jlBenDateCutoff.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBenDateCutoff.setText("Fecha corte:");
        jlBenDateCutoff.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsEmployee2.add(jlBenDateCutoff);

        jtfBenDateCutoff.setEditable(false);
        jtfBenDateCutoff.setText("01/01/2001");
        jtfBenDateCutoff.setFocusable(false);
        jtfBenDateCutoff.setPreferredSize(new java.awt.Dimension(75, 23));
        jpBenefitsEmployee2.add(jtfBenDateCutoff);

        jlBenSeniority.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBenSeniority.setText("Antigüedad:");
        jlBenSeniority.setPreferredSize(new java.awt.Dimension(95, 23));
        jpBenefitsEmployee2.add(jlBenSeniority);

        jtfBenSeniorityYears.setEditable(false);
        jtfBenSeniorityYears.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfBenSeniorityYears.setText("0");
        jtfBenSeniorityYears.setFocusable(false);
        jtfBenSeniorityYears.setPreferredSize(new java.awt.Dimension(50, 23));
        jpBenefitsEmployee2.add(jtfBenSeniorityYears);

        jlBenSeniorityYears.setText("años");
        jlBenSeniorityYears.setPreferredSize(new java.awt.Dimension(30, 23));
        jpBenefitsEmployee2.add(jlBenSeniorityYears);

        jtfBenSeniorityDays.setEditable(false);
        jtfBenSeniorityDays.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfBenSeniorityDays.setText("0");
        jtfBenSeniorityDays.setFocusable(false);
        jtfBenSeniorityDays.setPreferredSize(new java.awt.Dimension(50, 23));
        jpBenefitsEmployee2.add(jtfBenSeniorityDays);

        jlBenSeniorityDays.setText("días");
        jlBenSeniorityDays.setPreferredSize(new java.awt.Dimension(30, 23));
        jpBenefitsEmployee2.add(jlBenSeniorityDays);

        jlBenSeniorityProp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBenSeniorityProp.setText("Parte proporcional:");
        jlBenSeniorityProp.setPreferredSize(new java.awt.Dimension(125, 23));
        jpBenefitsEmployee2.add(jlBenSeniorityProp);

        jtfBenSeniorityProp.setEditable(false);
        jtfBenSeniorityProp.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfBenSeniorityProp.setText("0.0");
        jtfBenSeniorityProp.setFocusable(false);
        jtfBenSeniorityProp.setPreferredSize(new java.awt.Dimension(75, 23));
        jpBenefitsEmployee2.add(jtfBenSeniorityProp);

        jpBenefitsEmployee.add(jpBenefitsEmployee2);

        jpBenefits.add(jpBenefitsEmployee, java.awt.BorderLayout.NORTH);

        jpBenefitsTables.setLayout(new java.awt.GridLayout(1, 3, 5, 0));

        jpBenefitsVac.setBorder(javax.swing.BorderFactory.createTitledBorder("Vacaciones:"));
        jpBenefitsVac.setLayout(new java.awt.BorderLayout());

        jpBenefitsVacEdit.setLayout(new java.awt.GridLayout(2, 1, 0, 10));

        jpBenefitsVac1.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jpBenefitsVac11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVacTable.setText("Tabla prestación:*");
        jlVacTable.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVac11.add(jlVacTable);

        jcbVacTable.setPreferredSize(new java.awt.Dimension(205, 23));
        jpBenefitsVac11.add(jcbVacTable);

        jpBenefitsVac1.add(jpBenefitsVac11);

        jpBenefitsVac12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVacPeriod.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jlVacPeriod.setText("Período vigencia:");
        jlVacPeriod.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVac12.add(jlVacPeriod);

        jtfVacPeriod.setEditable(false);
        jtfVacPeriod.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jtfVacPeriod.setText("01/01/2001 - 01/01/2001");
        jtfVacPeriod.setFocusable(false);
        jtfVacPeriod.setPreferredSize(new java.awt.Dimension(150, 23));
        jpBenefitsVac12.add(jtfVacPeriod);

        jpBenefitsVac1.add(jpBenefitsVac12);

        jpBenefitsVac13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVacPaymentType.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jlVacPaymentType.setText("Período pago:");
        jlVacPaymentType.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVac13.add(jlVacPaymentType);

        jtfVacPaymentType.setEditable(false);
        jtfVacPaymentType.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jtfVacPaymentType.setText("TEXT");
        jtfVacPaymentType.setFocusable(false);
        jtfVacPaymentType.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVac13.add(jtfVacPaymentType);

        jtfVacUnionized.setEditable(false);
        jtfVacUnionized.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jtfVacUnionized.setText("TEXT");
        jtfVacUnionized.setToolTipText("Situación sindical");
        jtfVacUnionized.setFocusable(false);
        jtfVacUnionized.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVac13.add(jtfVacUnionized);

        jpBenefitsVac1.add(jpBenefitsVac13);

        jpBenefitsVacEdit.add(jpBenefitsVac1);

        jpBenefitsVac2.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jpBenefitsVac21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVacSeniorityStart.setText("Aniversario inicial:*");
        jlVacSeniorityStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVac21.add(jlVacSeniorityStart);

        jspVacSeniorityStart.setModel(new javax.swing.SpinnerNumberModel(1, 1, 50, 1));
        jspVacSeniorityStart.setPreferredSize(new java.awt.Dimension(75, 23));
        jpBenefitsVac21.add(jspVacSeniorityStart);

        jlVacSeniorityStartHelp.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jlVacSeniorityStartHelp.setText("(Para la tabla elegida)");
        jlVacSeniorityStartHelp.setPreferredSize(new java.awt.Dimension(125, 23));
        jpBenefitsVac21.add(jlVacSeniorityStartHelp);

        jpBenefitsVac2.add(jpBenefitsVac21);

        jpBenefitsVac22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVacLastUpdate.setText("Última actualización:");
        jlVacLastUpdate.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVac22.add(jlVacLastUpdate);

        jtfVacLastUpdate.setEditable(false);
        jtfVacLastUpdate.setText("01/01/2001 00:00:00");
        jtfVacLastUpdate.setFocusable(false);
        jtfVacLastUpdate.setPreferredSize(new java.awt.Dimension(125, 23));
        jpBenefitsVac22.add(jtfVacLastUpdate);

        jtfVacLastUpdater.setEditable(false);
        jtfVacLastUpdater.setText("text");
        jtfVacLastUpdater.setFocusable(false);
        jtfVacLastUpdater.setPreferredSize(new java.awt.Dimension(75, 23));
        jpBenefitsVac22.add(jtfVacLastUpdater);

        jpBenefitsVac2.add(jpBenefitsVac22);

        jpBenefitsVac23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbVacModify.setText("Modificar");
        jbVacModify.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVac23.add(jbVacModify);

        jbVacUpdate.setText("Actualizar");
        jbVacUpdate.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVac23.add(jbVacUpdate);

        jbVacCancel.setText("Cancelar");
        jbVacCancel.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVac23.add(jbVacCancel);

        jpBenefitsVac2.add(jpBenefitsVac23);

        jpBenefitsVacEdit.add(jpBenefitsVac2);

        jpBenefitsVac.add(jpBenefitsVacEdit, java.awt.BorderLayout.NORTH);

        jpBenefitsVacPane.setLayout(new java.awt.BorderLayout());
        jpBenefitsVac.add(jpBenefitsVacPane, java.awt.BorderLayout.CENTER);

        jpBenefitsTables.add(jpBenefitsVac);

        jpBenefitsVacBon.setBorder(javax.swing.BorderFactory.createTitledBorder("Prima vacacional:"));
        jpBenefitsVacBon.setLayout(new java.awt.BorderLayout());

        jpBenefitsVacBonEdit.setLayout(new java.awt.GridLayout(2, 1, 0, 10));

        jpBenefitsVacBon1.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jpBenefitsVacBon11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVacBonTable.setText("Tabla prestación:*");
        jlVacBonTable.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVacBon11.add(jlVacBonTable);

        jcbVacBonTable.setPreferredSize(new java.awt.Dimension(205, 23));
        jpBenefitsVacBon11.add(jcbVacBonTable);

        jpBenefitsVacBon1.add(jpBenefitsVacBon11);

        jpBenefitsVacBon12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVacBonPeriod.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jlVacBonPeriod.setText("Período vigencia:");
        jlVacBonPeriod.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVacBon12.add(jlVacBonPeriod);

        jtfVacBonPeriod.setEditable(false);
        jtfVacBonPeriod.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jtfVacBonPeriod.setText("01/01/2001 - 01/01/2001");
        jtfVacBonPeriod.setFocusable(false);
        jtfVacBonPeriod.setPreferredSize(new java.awt.Dimension(150, 23));
        jpBenefitsVacBon12.add(jtfVacBonPeriod);

        jpBenefitsVacBon1.add(jpBenefitsVacBon12);

        jpBenefitsVacBon13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVacBonPaymentType.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jlVacBonPaymentType.setText("Período pago:");
        jlVacBonPaymentType.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVacBon13.add(jlVacBonPaymentType);

        jtfVacBonPaymentType.setEditable(false);
        jtfVacBonPaymentType.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jtfVacBonPaymentType.setText("TEXT");
        jtfVacBonPaymentType.setFocusable(false);
        jtfVacBonPaymentType.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVacBon13.add(jtfVacBonPaymentType);

        jtfVacBonUnionized.setEditable(false);
        jtfVacBonUnionized.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jtfVacBonUnionized.setText("TEXT");
        jtfVacBonUnionized.setToolTipText("Situación sindical");
        jtfVacBonUnionized.setFocusable(false);
        jtfVacBonUnionized.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVacBon13.add(jtfVacBonUnionized);

        jpBenefitsVacBon1.add(jpBenefitsVacBon13);

        jpBenefitsVacBonEdit.add(jpBenefitsVacBon1);

        jpBenefitsVacBon2.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jpBenefitsVacBon21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVacBonSeniorityStart.setText("Aniversario inicial:*");
        jlVacBonSeniorityStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVacBon21.add(jlVacBonSeniorityStart);

        jspVacBonSeniorityStart.setModel(new javax.swing.SpinnerNumberModel(1, 1, 50, 1));
        jspVacBonSeniorityStart.setPreferredSize(new java.awt.Dimension(75, 23));
        jpBenefitsVacBon21.add(jspVacBonSeniorityStart);

        jlVacBonSeniorityStartHellp.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jlVacBonSeniorityStartHellp.setText("(Para la tabla elegida)");
        jlVacBonSeniorityStartHellp.setPreferredSize(new java.awt.Dimension(125, 23));
        jpBenefitsVacBon21.add(jlVacBonSeniorityStartHellp);

        jpBenefitsVacBon2.add(jpBenefitsVacBon21);

        jpBenefitsVacBon22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVacBonLastUpdate.setText("Última actualización:");
        jlVacBonLastUpdate.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVacBon22.add(jlVacBonLastUpdate);

        jtfVacBonLastUpdate.setEditable(false);
        jtfVacBonLastUpdate.setText("01/01/2001 00:00:00");
        jtfVacBonLastUpdate.setFocusable(false);
        jtfVacBonLastUpdate.setPreferredSize(new java.awt.Dimension(125, 23));
        jpBenefitsVacBon22.add(jtfVacBonLastUpdate);

        jtfVacBonLastUpdater.setEditable(false);
        jtfVacBonLastUpdater.setText("text");
        jtfVacBonLastUpdater.setFocusable(false);
        jtfVacBonLastUpdater.setPreferredSize(new java.awt.Dimension(75, 23));
        jpBenefitsVacBon22.add(jtfVacBonLastUpdater);

        jpBenefitsVacBon2.add(jpBenefitsVacBon22);

        jpBenefitsVacBon23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbVacBonModify.setText("Modificar");
        jbVacBonModify.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVacBon23.add(jbVacBonModify);

        jbVacBonUpdate.setText("Actualizar");
        jbVacBonUpdate.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVacBon23.add(jbVacBonUpdate);

        jbVacBonCancel.setText("Cancelar");
        jbVacBonCancel.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsVacBon23.add(jbVacBonCancel);

        jpBenefitsVacBon2.add(jpBenefitsVacBon23);

        jpBenefitsVacBonEdit.add(jpBenefitsVacBon2);

        jpBenefitsVacBon.add(jpBenefitsVacBonEdit, java.awt.BorderLayout.NORTH);

        jpBenefitsVacBonPane.setLayout(new java.awt.BorderLayout());
        jpBenefitsVacBon.add(jpBenefitsVacBonPane, java.awt.BorderLayout.CENTER);

        jpBenefitsTables.add(jpBenefitsVacBon);

        jpBenefitsAnnBon.setBorder(javax.swing.BorderFactory.createTitledBorder("Gratificación anual:"));
        jpBenefitsAnnBon.setLayout(new java.awt.BorderLayout());

        jpBenefitsAnnBonEdit.setLayout(new java.awt.GridLayout(2, 1, 0, 10));

        jpBenefitsAnnBon1.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jpBenefitsAnnBon11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAnnBonTable.setText("Tabla prestación:*");
        jlAnnBonTable.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsAnnBon11.add(jlAnnBonTable);

        jcbAnnBonTable.setPreferredSize(new java.awt.Dimension(205, 23));
        jpBenefitsAnnBon11.add(jcbAnnBonTable);

        jpBenefitsAnnBon1.add(jpBenefitsAnnBon11);

        jpBenefitsAnnBon12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAnnBonPeriod.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jlAnnBonPeriod.setText("Período vigencia:");
        jlAnnBonPeriod.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsAnnBon12.add(jlAnnBonPeriod);

        jtfAnnBonPeriod.setEditable(false);
        jtfAnnBonPeriod.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jtfAnnBonPeriod.setText("01/01/2001 - 01/01/2001");
        jtfAnnBonPeriod.setFocusable(false);
        jtfAnnBonPeriod.setPreferredSize(new java.awt.Dimension(150, 23));
        jpBenefitsAnnBon12.add(jtfAnnBonPeriod);

        jpBenefitsAnnBon1.add(jpBenefitsAnnBon12);

        jpBenefitsAnnBon13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAnnBonPaymentType.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jlAnnBonPaymentType.setText("Período pago:");
        jlAnnBonPaymentType.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsAnnBon13.add(jlAnnBonPaymentType);

        jtfAnnBonPaymentType.setEditable(false);
        jtfAnnBonPaymentType.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jtfAnnBonPaymentType.setText("TEXT");
        jtfAnnBonPaymentType.setFocusable(false);
        jtfAnnBonPaymentType.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsAnnBon13.add(jtfAnnBonPaymentType);

        jtfAnnBonUnionized.setEditable(false);
        jtfAnnBonUnionized.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jtfAnnBonUnionized.setText("TEXT");
        jtfAnnBonUnionized.setToolTipText("Situación sindical");
        jtfAnnBonUnionized.setFocusable(false);
        jtfAnnBonUnionized.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsAnnBon13.add(jtfAnnBonUnionized);

        jpBenefitsAnnBon1.add(jpBenefitsAnnBon13);

        jpBenefitsAnnBonEdit.add(jpBenefitsAnnBon1);

        jpBenefitsAnnBon2.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jpBenefitsAnnBon21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAnnBonSeniorityStart.setText("Aniversario inicial:*");
        jlAnnBonSeniorityStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsAnnBon21.add(jlAnnBonSeniorityStart);

        jspAnnBonSeniorityStart.setModel(new javax.swing.SpinnerNumberModel(1, 1, 50, 1));
        jspAnnBonSeniorityStart.setPreferredSize(new java.awt.Dimension(75, 23));
        jpBenefitsAnnBon21.add(jspAnnBonSeniorityStart);

        jlAnnBonSeniorityStartHelp.setForeground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jlAnnBonSeniorityStartHelp.setText("(Para la tabla elegida)");
        jlAnnBonSeniorityStartHelp.setPreferredSize(new java.awt.Dimension(125, 23));
        jpBenefitsAnnBon21.add(jlAnnBonSeniorityStartHelp);

        jpBenefitsAnnBon2.add(jpBenefitsAnnBon21);

        jpBenefitsAnnBon22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAnnBonLastUpdate.setText("Última actualización:");
        jlAnnBonLastUpdate.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsAnnBon22.add(jlAnnBonLastUpdate);

        jtfAnnBonLastUpdate.setEditable(false);
        jtfAnnBonLastUpdate.setText("01/01/2001 00:00:00");
        jtfAnnBonLastUpdate.setFocusable(false);
        jtfAnnBonLastUpdate.setPreferredSize(new java.awt.Dimension(125, 23));
        jpBenefitsAnnBon22.add(jtfAnnBonLastUpdate);

        jtfAnnBonLastUpdater.setEditable(false);
        jtfAnnBonLastUpdater.setText("text");
        jtfAnnBonLastUpdater.setFocusable(false);
        jtfAnnBonLastUpdater.setPreferredSize(new java.awt.Dimension(75, 23));
        jpBenefitsAnnBon22.add(jtfAnnBonLastUpdater);

        jpBenefitsAnnBon2.add(jpBenefitsAnnBon22);

        jpBenefitsAnnBon23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbAnnBonModify.setText("Modificar");
        jbAnnBonModify.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsAnnBon23.add(jbAnnBonModify);

        jbAnnBonUpdate.setText("Actualizar");
        jbAnnBonUpdate.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsAnnBon23.add(jbAnnBonUpdate);

        jbAnnBonCancel.setText("Cancelar");
        jbAnnBonCancel.setPreferredSize(new java.awt.Dimension(100, 23));
        jpBenefitsAnnBon23.add(jbAnnBonCancel);

        jpBenefitsAnnBon2.add(jpBenefitsAnnBon23);

        jpBenefitsAnnBonEdit.add(jpBenefitsAnnBon2);

        jpBenefitsAnnBon.add(jpBenefitsAnnBonEdit, java.awt.BorderLayout.NORTH);

        jpBenefitsAnnBonPane.setLayout(new java.awt.BorderLayout());
        jpBenefitsAnnBon.add(jpBenefitsAnnBonPane, java.awt.BorderLayout.CENTER);

        jpBenefitsTables.add(jpBenefitsAnnBon);

        jpBenefits.add(jpBenefitsTables, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Prestaciones laborales", jpBenefits);

        jpWageFactors.setLayout(new java.awt.BorderLayout());

        jpWageFactorsEmployee.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del empleado:"));
        jpWageFactorsEmployee.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jpWageFactorsEmployee1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFacEmp.setText("Empleado:");
        jlFacEmp.setPreferredSize(new java.awt.Dimension(100, 23));
        jpWageFactorsEmployee1.add(jlFacEmp);

        jtfFacEmpName.setEditable(false);
        jtfFacEmpName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfFacEmpName.setText("TEXT");
        jtfFacEmpName.setFocusable(false);
        jtfFacEmpName.setPreferredSize(new java.awt.Dimension(360, 23));
        jpWageFactorsEmployee1.add(jtfFacEmpName);

        jtfFacEmpNumber.setEditable(false);
        jtfFacEmpNumber.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfFacEmpNumber.setText("TEXT");
        jtfFacEmpNumber.setToolTipText("Número empleado");
        jtfFacEmpNumber.setFocusable(false);
        jtfFacEmpNumber.setPreferredSize(new java.awt.Dimension(75, 23));
        jpWageFactorsEmployee1.add(jtfFacEmpNumber);

        jpFacPaymentType.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jpFacPaymentType.setText("Período pago:");
        jpFacPaymentType.setPreferredSize(new java.awt.Dimension(100, 23));
        jpWageFactorsEmployee1.add(jpFacPaymentType);

        jtfFacPaymentType.setEditable(false);
        jtfFacPaymentType.setText("TEXT");
        jtfFacPaymentType.setFocusable(false);
        jtfFacPaymentType.setPreferredSize(new java.awt.Dimension(200, 23));
        jpWageFactorsEmployee1.add(jtfFacPaymentType);

        jckFacIsUnionized.setText("Es sindicalizado");
        jckFacIsUnionized.setEnabled(false);
        jckFacIsUnionized.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jckFacIsUnionized.setPreferredSize(new java.awt.Dimension(125, 23));
        jpWageFactorsEmployee1.add(jckFacIsUnionized);

        jpWageFactorsEmployee.add(jpWageFactorsEmployee1);

        jpWageFactorsEmployee2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFacDateBenefits.setText("Inicio prestaciones:");
        jlFacDateBenefits.setPreferredSize(new java.awt.Dimension(100, 23));
        jpWageFactorsEmployee2.add(jlFacDateBenefits);

        jtfFacDateBenefits.setEditable(false);
        jtfFacDateBenefits.setText("01/01/2001");
        jtfFacDateBenefits.setFocusable(false);
        jtfFacDateBenefits.setPreferredSize(new java.awt.Dimension(75, 23));
        jpWageFactorsEmployee2.add(jtfFacDateBenefits);

        jlFacDateCutoff.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlFacDateCutoff.setText("Fecha corte:");
        jlFacDateCutoff.setPreferredSize(new java.awt.Dimension(100, 23));
        jpWageFactorsEmployee2.add(jlFacDateCutoff);

        jtfFacDateCutoff.setEditable(false);
        jtfFacDateCutoff.setText("01/01/2001");
        jtfFacDateCutoff.setFocusable(false);
        jtfFacDateCutoff.setPreferredSize(new java.awt.Dimension(75, 23));
        jpWageFactorsEmployee2.add(jtfFacDateCutoff);

        jlFacSeniority.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlFacSeniority.setText("Antigüedad:");
        jlFacSeniority.setPreferredSize(new java.awt.Dimension(95, 23));
        jpWageFactorsEmployee2.add(jlFacSeniority);

        jtfFacSeniorityYears.setEditable(false);
        jtfFacSeniorityYears.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfFacSeniorityYears.setText("0");
        jtfFacSeniorityYears.setFocusable(false);
        jtfFacSeniorityYears.setPreferredSize(new java.awt.Dimension(50, 23));
        jpWageFactorsEmployee2.add(jtfFacSeniorityYears);

        jlFacSeniorityYears.setText("años");
        jlFacSeniorityYears.setPreferredSize(new java.awt.Dimension(30, 23));
        jpWageFactorsEmployee2.add(jlFacSeniorityYears);

        jtfFacSeniorityDays.setEditable(false);
        jtfFacSeniorityDays.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfFacSeniorityDays.setText("0");
        jtfFacSeniorityDays.setFocusable(false);
        jtfFacSeniorityDays.setPreferredSize(new java.awt.Dimension(50, 23));
        jpWageFactorsEmployee2.add(jtfFacSeniorityDays);

        jlFacSeniorityDays.setText("días");
        jlFacSeniorityDays.setPreferredSize(new java.awt.Dimension(30, 23));
        jpWageFactorsEmployee2.add(jlFacSeniorityDays);

        jlFacSeniorityProp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlFacSeniorityProp.setText("Parte proporcional:");
        jlFacSeniorityProp.setPreferredSize(new java.awt.Dimension(125, 23));
        jpWageFactorsEmployee2.add(jlFacSeniorityProp);

        jtfFacSeniorityProp.setEditable(false);
        jtfFacSeniorityProp.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfFacSeniorityProp.setText("0.0");
        jtfFacSeniorityProp.setFocusable(false);
        jtfFacSeniorityProp.setPreferredSize(new java.awt.Dimension(75, 23));
        jpWageFactorsEmployee2.add(jtfFacSeniorityProp);

        jpWageFactorsEmployee.add(jpWageFactorsEmployee2);

        jpWageFactors.add(jpWageFactorsEmployee, java.awt.BorderLayout.NORTH);

        jpWageFactorsPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Factores de integración:"));
        jpWageFactorsPane.setLayout(new java.awt.BorderLayout());
        jpWageFactors.add(jpWageFactorsPane, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Factores de integración", jpWageFactors);

        getContentPane().add(jTabbedPane, java.awt.BorderLayout.CENTER);

        jPanel5.setLayout(new java.awt.GridLayout(1, 2));

        jPanel41.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jtfPkBizPartnerId_Ro.setEditable(false);
        jtfPkBizPartnerId_Ro.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPkBizPartnerId_Ro.setToolTipText("ID del asociado de negocios");
        jtfPkBizPartnerId_Ro.setFocusable(false);
        jtfPkBizPartnerId_Ro.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel41.add(jtfPkBizPartnerId_Ro);

        jPanel5.add(jPanel41);

        jPanel1.setPreferredSize(new java.awt.Dimension(392, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jPanel1.add(jbCancel);

        jPanel5.add(jPanel1);

        getContentPane().add(jPanel5, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(1056, 689));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivate();
    }//GEN-LAST:event_formWindowActivated

    private void jftAlternativeIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jftAlternativeIdFocusLost
        focusLostAlternativeId();
    }//GEN-LAST:event_jftAlternativeIdFocusLost

    private void jtfFirstnameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfFirstnameFocusLost
        computeBizPartner_Ro();
    }//GEN-LAST:event_jtfFirstnameFocusLost

    private void jtfLastname1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfLastname1FocusLost
        computeBizPartner_Ro();
    }//GEN-LAST:event_jtfLastname1FocusLost

    private void jtfLastname2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfLastname2FocusLost
        computeBizPartner_Ro();
    }//GEN-LAST:event_jtfLastname2FocusLost

    private void initComponentsExtra() {
        mvFields = new Vector<>();

        // Employee fields:
        
        moFieldNumber = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jftNumber, jlNumber);
        moFieldNumber.setLengthMax(10);
        moFieldNumber.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldLastname1 = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfLastname1, jlLastname);
        moFieldLastname1.setLengthMax(50);
        moFieldLastname1.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldLastname2 = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfLastname2, jlLastname);
        moFieldLastname2.setLengthMax(49);
        moFieldLastname2.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldFirstname = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfFirstname, jlFirstname);
        moFieldFirstname.setLengthMax(100);
        moFieldFirstname.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldFiscalId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfFiscalId, jlFiscalId);
        moFieldFiscalId.setLengthMin(DCfdConsts.LEN_RFC_PER);
        moFieldFiscalId.setLengthMax(DCfdConsts.LEN_RFC_PER);
        moFieldFiscalId.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldAlternativeId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jftAlternativeId, jlAlternativeId);
        moFieldAlternativeId.setLengthMin(DCfdConsts.LEN_CURP);
        moFieldAlternativeId.setLengthMax(DCfdConsts.LEN_CURP);
        moFieldAlternativeId.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldSocialSecurityNumber = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfSocialSecurityNumber, jlSocialSecurityNumber);
        moFieldSocialSecurityNumber.setLengthMin(DCfdConsts.LEN_SS_NUM);
        moFieldSocialSecurityNumber.setLengthMax(DCfdConsts.LEN_SS_NUM);
        moFieldSocialSecurityNumber.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldFkBank_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkBank_n, jlFkBank_n);
        moFieldFkBank_n.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldBankAccount = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfBankAccount, jlBankAccount);
        moFieldBankAccount.setLengthMax(20);
        moFieldBankAccount.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldFkGroceryService = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkGroceryService, jlFkGroceryService);
        moFieldFkGroceryService.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldGroceryServiceAccount = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfGroceryServiceAccount, jlGroceryServiceAccount);
        moFieldGroceryServiceAccount.setLengthMax(20);
        moFieldGroceryServiceAccount.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldMailOwn = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfMailOwn, jlMailOwn);
        moFieldMailOwn.setLengthMax(50);
        moFieldMailOwn.setAutoCaseType(0);
        moFieldMailOwn.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldMailCompany = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfMailCompany, jlMailCompany);
        moFieldMailCompany.setLengthMax(50);
        moFieldMailCompany.setAutoCaseType(0);
        moFieldMailCompany.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldFileImagePhoto = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfImagePhoto, jlImagePhoto);
        moFieldFileImagePhoto.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldFileImageSignature = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfImageSignature, jlImageSignature);
        moFieldFileImageSignature.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldIsActive = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsActive);
        moFieldIsActive.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDeleted);
        moFieldIsDeleted.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);

        moFieldDateBenefits = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateBenefits, jlDateBenefits);
        moFieldDateBenefits.setPickerButton(jbDateBenefits);
        moFieldDateBenefits.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldDateLastHire = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateLastHire, jlDateLastHire);
        moFieldDateLastHire.setPickerButton(jbDateLastHire);
        moFieldDateLastHire.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldDateLastDismissal_n = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftDateLastDismissal_n, jlDateLastDismissal_n);
        moFieldDateLastDismissal_n.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldFkPaymentType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkPaymentType, jlFkPaymentType);
        moFieldFkPaymentType.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldFkSalaryType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkSalaryType, jlFkSalaryType);
        moFieldFkSalaryType.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldSalary = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfSalary, jlSalary);
        moFieldSalary.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldDateChangeSalary = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateChangeSalary, jlSalary);
        moFieldDateChangeSalary.setPickerButton(jbDateChangeSalary);
        moFieldDateChangeSalary.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldWage = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfWage, jlWage);
        moFieldWage.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldDateChangeWage = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateChangeWage, jlWage);
        moFieldDateChangeWage.setPickerButton(jbDateChangeWage);
        moFieldDateChangeWage.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldSalarySscBase = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfSalarySscBase, jlSalarySscBase);
        moFieldSalarySscBase.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldDateChangeSalarySscBase = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftDateChangeSalarySscBase, jlSalarySscBase);
        moFieldDateChangeSalarySscBase.setPickerButton(jbDateChangeSalarySscBase);
        moFieldDateChangeSalarySscBase.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldFkMwzType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkMwzType, jlFkMwzType);
        moFieldFkMwzType.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        
        moFieldFkEmployeeType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkEmployeeType, jlFkEmployeeType);
        moFieldFkEmployeeType.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldFkWorkerType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkWorkerType, jlFkWorkerType);
        moFieldFkWorkerType.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldIsUnionized = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsUnionized);
        moFieldIsUnionized.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldIsMfgOperator = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsMfgOperator);
        moFieldIsMfgOperator.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldFkDepartment = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkDepartment, jlFkDepartment);
        moFieldFkDepartment.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldFkPosition = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkPosition, jlFkPosition);
        moFieldFkPosition.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldFkShift = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkShift, jlFkShift);
        moFieldFkShift.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldFkWorkingDayType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkWorkingDayType, jlFkWorkingDayType);
        moFieldFkWorkingDayType.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldWorkingHoursDay = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfWorkingHoursDay, jlWorkingHoursDay);
        moFieldWorkingHoursDay.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldFkContractType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkContractType, jlFkContractType);
        moFieldFkContractType.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldContractExpiration = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftContractExpiration, jlContractExpiration);
        moFieldContractExpiration.setPickerButton(jbContractExpiration);
        moFieldFkRecruitmentSchemaType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkRecruitmentSchemaType, jlFkRecruitmentSchemaType);
        moFieldFkRecruitmentSchemaType.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        moFieldFkPositionRiskType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkPositionRiskType, jlFkPositionRiskType);
        moFieldFkPositionRiskType.setTabbedPaneIndex(TAB_DATA_EMP, jTabbedPane);
        
        // Personal data:
        
        moFieldDateBirth = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateBirth, jlDateBirth);
        moFieldDateBirth.setPickerButton(jbDateBirth);
        moFieldDateBirth.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldPlaceOfBirth = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfBirthPlace, jlBirthPlace);
        moFieldPlaceOfBirth.setLengthMax(25);
        moFieldPlaceOfBirth.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldUmf =  new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfUmf, jlUmf);
        moFieldUmf.setLengthMax(3);
        moFieldUmf.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldFkCatalogueSexType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCatalogueSexTypeId, jlFkCatalogueSexTypeId);
        moFieldFkCatalogueSexType.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldFkCatalogueBloodTypeType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCatalogueBloodTypeTypeId, jlFkCatalogueBloodTypeTypeId);
        moFieldFkCatalogueBloodTypeType.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldFkCatalogueEducationType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCatalogueEducationTypeId, jlFkCatalogueEducationTypeId);
        moFieldFkCatalogueEducationType.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldFkCatalogueMaritalStatusType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCatalogueMaritalStatusTypeId, jlFkCatalogueMaritalStatusTypeId);
        moFieldFkCatalogueMaritalStatusType.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldPhone = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfPhone, jlPhone);
        moFieldPhone.setLengthMax(50);
        moFieldPhone.setAutoCaseType(0);
        moFieldPhone.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        
        moFieldMateName = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfMateName, jlMate);
        moFieldMateName.setLengthMax(50);
        moFieldMateName.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldMateDateBirth = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftMateDateBirth, jlMate);
        moFieldMateDateBirth.setPickerButton(jbMateDateBirth);
        moFieldMateDateBirth.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldMateSex = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkMateCatalogueSexTypeId, jlMate);
        moFieldMateSex.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldMateDeceased = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckMateDeceased);
        moFieldMateDeceased.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonName1 = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfSonName1, jlSon1);
        moFieldSonName1.setLengthMax(50);
        moFieldSonName1.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonDateBirth1 = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftSonDateBirth1, jlSon1);
        moFieldSonDateBirth1.setPickerButton(jbSonDateBirth1);
        moFieldSonDateBirth1.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonSex1 = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkSonCatalogueSexTypeId1, jlSon1);
        moFieldSonSex1.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonDeceased1 = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckSonDeceased1);
        moFieldSonDeceased1.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonName2 = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfSonName2, jlSon2);
        moFieldSonName2.setLengthMax(50);
        moFieldSonName2.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonDateBirth2 = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftSonDateBirth2, jlSon2);
        moFieldSonDateBirth2.setPickerButton(jbSonDateBirth2);
        moFieldSonDateBirth2.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonSex2 = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkSonCatalogueSexTypeId2, jlSon2);
        moFieldSonSex2.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonDeceased2 = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckSonDeceased2);
        moFieldSonDeceased2.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonName3 = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfSonName3, jlSon3);
        moFieldSonName3.setLengthMax(50);
        moFieldSonName3.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonDateBirth3 = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftSonDateBirth3, jlSon3);
        moFieldSonDateBirth3.setPickerButton(jbSonDateBirth3);
        moFieldSonDateBirth3.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonSex3 = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkSonCatalogueSexTypeId3, jlSon3);
        moFieldSonSex3.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonDeceased3 = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckSonDeceased3);
        moFieldSonDeceased3.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonName4 = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfSonName4, jlSon4);
        moFieldSonName4.setLengthMax(50);
        moFieldSonName4.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonDateBirth4 = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftSonDateBirth4, jlSon4);
        moFieldSonDateBirth4.setPickerButton(jbSonDateBirth4);
        moFieldSonDateBirth4.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonSex4 = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkSonCatalogueSexTypeId4, jlSon4);
        moFieldSonSex4.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonDeceased4 = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckSonDeceased4);
        moFieldSonDeceased4.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonName5 = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfSonName5, jlSon5);
        moFieldSonName5.setLengthMax(50);
        moFieldSonName5.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonDateBirth5 = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftSonDateBirth5, jlSon5);
        moFieldSonDateBirth5.setPickerButton(jbSonDateBirth5);
        moFieldSonDateBirth5.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonSex5 = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkSonCatalogueSexTypeId5, jlSon5);
        moFieldSonSex5.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        moFieldSonDeceased5 = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckSonDeceased5);
        moFieldSonDeceased5.setTabbedPaneIndex(TAB_DATA_PER, jTabbedPane);
        
        // Benefits:

        moFieldBenVacTable = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbVacTable, jlVacTable);
        moFieldBenVacTable.setTabbedPaneIndex(TAB_BENEFITS, jTabbedPane);
        moFieldBenVacBonTable = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbVacBonTable, jlVacBonTable);
        moFieldBenVacBonTable.setTabbedPaneIndex(TAB_BENEFITS, jTabbedPane);
        moFieldBenAnnBonTable = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbAnnBonTable, jlAnnBonTable);
        moFieldBenAnnBonTable.setTabbedPaneIndex(TAB_BENEFITS, jTabbedPane);
        
        // Employee fields:
        
        mvFields.add(moFieldNumber);
        mvFields.add(moFieldLastname1);
        mvFields.add(moFieldLastname2);
        mvFields.add(moFieldFirstname);
        mvFields.add(moFieldFiscalId);
        mvFields.add(moFieldAlternativeId);
        mvFields.add(moFieldSocialSecurityNumber);
        mvFields.add(moFieldFkBank_n);
        mvFields.add(moFieldBankAccount);
        mvFields.add(moFieldFkGroceryService);
        mvFields.add(moFieldGroceryServiceAccount);
        mvFields.add(moFieldMailOwn);
        mvFields.add(moFieldMailCompany);
        mvFields.add(moFieldFileImagePhoto);
        mvFields.add(moFieldFileImageSignature);
        mvFields.add(moFieldIsActive);
        mvFields.add(moFieldIsDeleted);
        
        mvFields.add(moFieldDateBenefits);
        mvFields.add(moFieldDateLastHire);
        mvFields.add(moFieldDateLastDismissal_n);
        mvFields.add(moFieldFkPaymentType);
        mvFields.add(moFieldFkSalaryType);
        mvFields.add(moFieldSalary);
        mvFields.add(moFieldDateChangeSalary);
        mvFields.add(moFieldWage);
        mvFields.add(moFieldDateChangeWage);
        mvFields.add(moFieldSalarySscBase);
        mvFields.add(moFieldDateChangeSalarySscBase);
        mvFields.add(moFieldFkMwzType);
        
        mvFields.add(moFieldFkEmployeeType);
        mvFields.add(moFieldFkWorkerType);
        mvFields.add(moFieldIsUnionized);
        mvFields.add(moFieldIsMfgOperator);
        mvFields.add(moFieldFkDepartment);
        mvFields.add(moFieldFkPosition);
        mvFields.add(moFieldFkShift);
        mvFields.add(moFieldFkWorkingDayType);
        mvFields.add(moFieldWorkingHoursDay);
        mvFields.add(moFieldFkContractType);
        mvFields.add(moFieldContractExpiration);
        mvFields.add(moFieldFkRecruitmentSchemaType);
        mvFields.add(moFieldFkPositionRiskType);
        
        // Personal data:
        
        mvFields.add(moFieldDateBirth);
        mvFields.add(moFieldPlaceOfBirth);
        mvFields.add(moFieldUmf);
        mvFields.add(moFieldFkCatalogueSexType);
        mvFields.add(moFieldFkCatalogueBloodTypeType);
        mvFields.add(moFieldFkCatalogueEducationType);
        mvFields.add(moFieldFkCatalogueMaritalStatusType);
        mvFields.add(moFieldPhone);
        
        mvFields.add(moFieldMateName);
        mvFields.add(moFieldMateDateBirth);
        mvFields.add(moFieldMateSex);
        mvFields.add(moFieldMateDeceased);
        mvFields.add(moFieldSonName1);
        mvFields.add(moFieldSonDateBirth1);
        mvFields.add(moFieldSonSex1);
        mvFields.add(moFieldSonDeceased1);
        mvFields.add(moFieldSonName2);
        mvFields.add(moFieldSonDateBirth2);
        mvFields.add(moFieldSonSex2);
        mvFields.add(moFieldSonDeceased2);
        mvFields.add(moFieldSonName3);
        mvFields.add(moFieldSonDateBirth3);
        mvFields.add(moFieldSonSex3);
        mvFields.add(moFieldSonDeceased3);
        mvFields.add(moFieldSonName4);
        mvFields.add(moFieldSonDateBirth4);
        mvFields.add(moFieldSonSex4);
        mvFields.add(moFieldSonDeceased4);
        mvFields.add(moFieldSonName5);
        mvFields.add(moFieldSonDateBirth5);
        mvFields.add(moFieldSonSex5);
        mvFields.add(moFieldSonDeceased5);
        
        // Benefits:
        
        mvFields.add(moFieldBenVacTable);
        mvFields.add(moFieldBenVacBonTable);
        mvFields.add(moFieldBenAnnBonTable);
        
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbDateBirth.addActionListener(this);
        jbImagePhoto.addActionListener(this);
        jbImagePhotoView.addActionListener(this);
        jbImagePhotoRemove.addActionListener(this);
        jbImageSignature.addActionListener(this);
        jbImageSignatureView.addActionListener(this);
        jbImageSignatureRemove.addActionListener(this);
        jbDateBenefitsEdit.addActionListener(this);
        jbDateBenefits.addActionListener(this);
        jbDateLastHire.addActionListener(this);
        jbDateChangeSalary.addActionListener(this);
        jbDateChangeWage.addActionListener(this);
        jbDateChangeSalarySscBase.addActionListener(this);
        jbContractExpiration.addActionListener(this);
        jbVacModify.addActionListener(this);
        jbVacUpdate.addActionListener(this);
        jbVacCancel.addActionListener(this);
        jbVacBonModify.addActionListener(this);
        jbVacBonUpdate.addActionListener(this);
        jbVacBonCancel.addActionListener(this);
        jbAnnBonModify.addActionListener(this);
        jbAnnBonUpdate.addActionListener(this);
        jbAnnBonCancel.addActionListener(this);
        jckChangeSalary.addItemListener(this);
        jckChangeWage.addItemListener(this);
        jckChangeSalarySscBase.addItemListener(this);
        jcbFkPaymentType.addItemListener(this);
        jcbFkDepartment.addItemListener(this);
        jcbFkContractType.addItemListener(this);
        jcbVacTable.addItemListener(this);
        jcbVacBonTable.addItemListener(this);
        jcbAnnBonTable.addItemListener(this);
        jftDateBenefits.addFocusListener(this);
        jftDateBirth.addFocusListener(this);
        jftMateDateBirth.addFocusListener(this);
        jftSonDateBirth1.addFocusListener(this);
        jftSonDateBirth2.addFocusListener(this);
        jftSonDateBirth3.addFocusListener(this);
        jftSonDateBirth4.addFocusListener(this);
        jftSonDateBirth5.addFocusListener(this);

        moFieldAlternativeId.setMaskFormatter("UUUU######XXXXXXXX");

        moPanelBizPartnerBranchAddress = new SPanelBizPartnerBranchAddress(miClient);
        jpOficialAddress.add(moPanelBizPartnerBranchAddress, BorderLayout.NORTH);
        
        moBenefitsVacPane = new STablePane(miClient);
        jpBenefitsVacPane.add(moBenefitsVacPane, BorderLayout.CENTER);

        moBenefitsVacBonPane = new STablePane(miClient);
        jpBenefitsVacBonPane.add(moBenefitsVacBonPane, BorderLayout.CENTER);

        moBenefitsAnnBonPane = new STablePane(miClient);
        jpBenefitsAnnBonPane.add(moBenefitsAnnBonPane, BorderLayout.CENTER);
        
        int col;
        erp.lib.table.STableColumnForm tableColumns[];
        STablePane[] panes = new STablePane[] { moBenefitsVacPane, moBenefitsVacBonPane, moBenefitsAnnBonPane };
        
        for (STablePane pane : panes) {
            col = 0;
            tableColumns = new STableColumnForm[6];
            tableColumns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Aniversario", 50);
            tableColumns[col] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Año", 40);
            tableColumns[col++].setCellRenderer(SGridUtils.CellRendererIntegerRaw);
            if (pane == moBenefitsVacBonPane) {
                tableColumns[col] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Prima", 50);
                tableColumns[col++].setCellRenderer(SGridUtils.CellRendererPercentage1D);
            }
            else {
                tableColumns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Días", 50);
            }
            tableColumns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Prestación", 175);
            tableColumns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usuario", STableConstants.WIDTH_USER);
            tableColumns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Usuario TS", STableConstants.WIDTH_DATE_TIME);

            for (col = 0; col < tableColumns.length; col++) {
                pane.addTableColumn(tableColumns[col]);
            }
        }
        
        moWageFactorsPane = new STablePane(miClient);
        jpWageFactorsPane.add(moWageFactorsPane, BorderLayout.CENTER);
        
        col = 0;
        tableColumns = new STableColumnForm[9];
        tableColumns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Aniversario", 50);
        tableColumns[col] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Año", 40);
        tableColumns[col++].setCellRenderer(SGridUtils.CellRendererIntegerRaw);
        tableColumns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Días vacaciones", 75);
        tableColumns[col] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Prima vacacional", 75);
        tableColumns[col++].setCellRenderer(SGridUtils.CellRendererPercentage1D);
        tableColumns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Días gratificación anual", 75);
        tableColumns[col] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Días totales", 125);
        tableColumns[col++].setCellRenderer(SGridUtils.CellRendererValue8D);
        tableColumns[col] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Factor integración", 125);
        tableColumns[col++].setCellRenderer(SGridUtils.CellRendererValue8D);
        tableColumns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usuario", STableConstants.WIDTH_USER);
        tableColumns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Usuario TS", STableConstants.WIDTH_DATE_TIME);

        for (col = 0; col < tableColumns.length; col++) {
            moWageFactorsPane.addTableColumn(tableColumns[col]);
        }
        
        jTabbedPane.addChangeListener(this);

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
        if (moBizPartner != null && moEmployee != null && moEmployee.getFkSourceCompanyId() != miClient.getSessionXXX().getCurrentCompany().getPkCompanyId()) {
            // employee cannot be updated in current user session:
            
            SDataCompany sourceCompany = (SDataCompany) SDataUtilities.readRegistry(
                    miClient, SDataConstants.CFGU_CO, new int[] { moEmployee.getFkSourceCompanyId() }, SLibConstants.EXEC_MODE_VERBOSE);
            
            miClient.showMsgBoxWarning("No se puede modificar a '" + moBizPartner.getBizPartner() + "' en esta sesión de usuario,\n"
                    + "porque pertenece a '" + sourceCompany.getCompany() + "'.");
            
            actionCancel(); // close form
        }
        else {
            if (mbFirstTime) {
                mbFirstTime = false;
                jtfLastname1.requestFocusInWindow();
            }
        }
    }
    
    private void enableButtonsPhoto(boolean enable) {
        jbImagePhotoView.setEnabled(enable);
        jbImagePhotoRemove.setEnabled(enable);
    }
    
    private void enableButtonsSignature(boolean enable) {
        jbImageSignatureView.setEnabled(enable);
        jbImageSignatureRemove.setEnabled(enable);
    }

    private String composeName() {
        String name = moFieldLastname1.getString();

        if (!moFieldLastname2.getString().isEmpty()) {
            name += (name.isEmpty() ? "" : " ") + moFieldLastname2.getString();
        }

        if (!moFieldFirstname.getString().isEmpty()) {
            name += (name.isEmpty() ? "" : ", ") + moFieldFirstname.getString();
        }

        return name;
    }

    private void computeBizPartner_Ro() {
        jtfBizPartner_Ro.setText(composeName());
        jtfBizPartner_Ro.setCaretPosition(0);
    }

    private void focusLostAlternativeId() {
        if (jftAlternativeId.getText().trim().length() > 0) {
            jftAlternativeId.setText(jftAlternativeId.getText().toUpperCase());
        }
        else {
            jftAlternativeId.setText("");
        }
    }
    
    private void updateStatusDateBenefits(boolean enable) {
        jftDateBenefits.setEditable(enable);
        jftDateBenefits.setFocusable(enable);
        jbDateBenefits.setEnabled(enable);
        jbDateBenefitsEdit.setEnabled(!enable);
    }

    private void updateStatusDateLastHire(boolean enable) {
        jftDateLastHire.setEditable(enable);
        jftDateLastHire.setFocusable(enable);
        jbDateLastHire.setEnabled(enable);
    }
    
    private void updateBizPartnerBranch() {
        if (moBizPartnerBranch == null) {
            moBizPartnerBranch = new SDataBizPartnerBranch();
            moBizPartnerBranch.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moBizPartnerBranch.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moBizPartnerBranch.setFkBizPartnerBranchTypeId(SDataConstantsSys.BPSS_TP_BPB_HQ);
        moBizPartnerBranch.setBizPartnerBranch(SModSysConsts.TXT_HQ);
        moBizPartnerBranch.setIsAddressPrintable(true);
        moBizPartnerBranch.setIsDeleted(false);
        moBizPartnerBranch.setDbmsTaxRegion("");

        // official address:
        
        moBizPartnerBranch.getDbmsBizPartnerBranchAddresses().clear();

        SDataBizPartnerBranchAddress address = (SDataBizPartnerBranchAddress) moPanelBizPartnerBranchAddress.getRegistry();
        address.setIsDefault(true);
        address.setFkAddressTypeId(SDataConstantsSys.BPSS_TP_ADD_OFF); // official address

        address.setIsRegistryEdited(true);
        moBizPartnerBranch.getDbmsBizPartnerBranchAddresses().add(address);
        
        // official contact:
        
        moBizPartnerBranch.getDbmsBizPartnerBranchContacts().clear();
        
        SDataBizPartnerBranchContact contact = moBizPartnerBranch.getDbmsBizPartnerBranchContactOfficial();
        
        if (contact == null) {
            contact = createBizPartnerBranchContact();
        }
        
        contact.setEmail01(moFieldMailOwn.getString());
        contact.setEmail02(moFieldMailCompany.getString());
        contact.setTelNumber01(moFieldPhone.getString());
        contact.setFkTelephoneType01Id(SDataConstantsSys.BPSS_TP_TEL_TEL);
        
        contact.setIsRegistryEdited(true);
        moBizPartnerBranch.getDbmsBizPartnerBranchContacts().add(contact);
        
        if (moBizPartner.getDbmsBizPartnerBranches().isEmpty()) {
            moBizPartner.getDbmsBizPartnerBranches().add(moBizPartnerBranch);
        }
        else {
            moBizPartner.getDbmsBizPartnerBranches().set(0, moBizPartnerBranch);
        }
        
        moBizPartnerBranch.setIsRegistryEdited(true);
    }
    
    private void updateEmployeeNextNumber() {
        try {
            jftNumber.setValue(SHrsUtils.getEmployeeNextNumber(miClient.getSession().getStatement().getConnection()));
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    private SDataBizPartnerCategory createBizPartnerCategory(final int categoryId) {
        SDataBizPartnerCategory category = null;

        if (categoryId >= SModSysConsts.BPSS_CT_BP_SUP && categoryId <= SModSysConsts.BPSS_CT_BP_DBR) {
            category = new SDataBizPartnerCategory();

            //category.setPkBizPartnerId();
            category.setPkBizPartnerCategoryId(categoryId);
            category.setKey("");
            category.setCompanyKey("");
            category.setCreditLimit(0);
            category.setDaysOfCredit(0);
            category.setDaysOfGrace(0);
            category.setGuarantee(0);
            category.setInsurance(0);
            category.setDateStart(SLibTimeUtils.getBeginOfYear(miClient.getSession().getCurrentDate()));
            category.setDateEnd_n(null);
            category.setPaymentAccount("");
            category.setNumberExporter("");
            category.setIsCreditByUser(true);
            category.setIsGuaranteeInProcess(false);
            category.setIsInsuranceInProcess(false);
            category.setIsDeleted(false);
            category.setFkBizPartnerCategoryId(categoryId);
            category.setFkBizPartnerTypeId(SModSysConsts.BPSU_TP_BP_DEF);
            category.setFkCreditTypeId_n(SModSysConsts.BPSS_TP_CRED_CRED_NO);
            category.setFkRiskId_n(SLibConsts.UNDEFINED);
            category.setFkCfdAddendaTypeId(SModSysConsts.BPSS_TP_CFD_ADD_CFD_ADD_NA);
            category.setFkLanguageId_n(SLibConsts.UNDEFINED);
            category.setFkCurrencyId_n(SLibConsts.UNDEFINED);
            category.setFkPaymentSystemTypeId_n(SLibConsts.UNDEFINED);
            category.setFkUserAnalystId_n(SLibConsts.UNDEFINED);
            category.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }

        return category;
    }

    public SDataBizPartnerBranchContact createBizPartnerBranchContact() {
        SDataBizPartnerBranchContact contact = new SDataBizPartnerBranchContact();
        //contact.setPkBizPartnerBranchId();
        contact.setPkContactId(mnPkContactId);
        contact.setContact("");
        contact.setContactPrefix("");
        contact.setContactSuffix("");
        contact.setLastname("");
        contact.setFirstname("");
        contact.setCharge("");
        contact.setTelAreaCode01("");
        contact.setTelNumber01("");
        contact.setTelExt01("");
        contact.setTelAreaCode02("");
        contact.setTelNumber02("");
        contact.setTelExt02("");
        contact.setTelAreaCode03("");
        contact.setTelNumber03("");
        contact.setTelExt03("");
        contact.setNextelId01("");
        contact.setNextelId02("");
        contact.setEmail01("");
        contact.setEmail02("");
        contact.setSkype01("");
        contact.setSkype02("");
        contact.setIsDeleted(false);
        contact.setFkContactTypeId(SDataConstantsSys.BPSS_TP_CON_ADM);
        contact.setFkTelephoneType01Id(SDataConstantsSys.BPSS_TP_TEL_NA);
        contact.setFkTelephoneType02Id(SDataConstantsSys.BPSS_TP_TEL_NA);
        contact.setFkTelephoneType03Id(SDataConstantsSys.BPSS_TP_TEL_NA);
        contact.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        contact.setFkUserEditId(miClient.getSession().getUser().getPkUserId());

        return contact;
    }
    
    private SFormValidation validateNumber() {
        SFormValidation validation = new SFormValidation();
        
        try {
            int count = 0;
            String employees = "";
            String sql = "SELECT b.bp "
                    + "FROM erp.hrsu_emp AS e "
                    + "INNER JOIN erp.bpsu_bp AS b ON e.id_emp = b.id_bp "
                    + "WHERE e.num = '" + moFieldNumber.getString() + "' "
                    + (moEmployee == null ? "" : "AND e.id_emp <> " + moEmployee.getPkEmployeeId() + " ")
                    + "AND NOT e.b_del;";
            try (ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql)) {
                while (resultSet.next()) {
                    count++;
                    employees += (employees.isEmpty() ? "" : "; ") + resultSet.getString(1);
                }
            }
            
            if (count > 0) {
                validation.setMessage("El número de empleado '" + moFieldNumber.getString() + "' ya esta siendo usado por " + (count == 1 ? "el empleado:\n" : "los empleados:\n") + employees);
                validation.setComponent(jftNumber); 
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        return validation;
    }
    
    private void renderBenefits(final int benefitType) {
        STablePane pane = null;
        
        switch (benefitType) {
            case SModSysConsts.HRSS_TP_BEN_VAC:
                pane = moBenefitsVacPane;
                break;
            case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                pane = moBenefitsVacBonPane;
                break;
            case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                pane = moBenefitsAnnBonPane;
                break;
            default:
                // nothing
        }
        
        if (pane != null) {
            pane.clearTableRows();
            
            if (moEmployeeBenefitTables != null) {
                try {
                    int yearBenefits = SLibTimeUtils.digestYear(moFieldDateBenefits.getDate())[0];
                    HashMap<Integer, String> benefitNames = new HashMap<>();
                    HashMap<Integer, String> userNames = new HashMap<>();
                    ArrayList<SDbEmployeeBenefitTablesAnnum> annums = moEmployeeBenefitTables.getBenefitAnnums(miClient.getSession(), benefitType);
                    
                    for (SDbEmployeeBenefitTablesAnnum annum : annums) {
                        String benefitName = benefitNames.get(annum.getFkBenefitId());
                        if (benefitName == null) {
                            benefitName = (String) ((SGuiClient) miClient).getSession().readField(SModConsts.HRS_BEN, new int[] { annum.getFkBenefitId() }, SDbRegistry.FIELD_NAME);
                            benefitNames.put(annum.getFkBenefitId(), benefitName);
                        }
                        
                        String userName = annum.getFkUserId() == 0 ? "" : userNames.get(annum.getFkUserId());
                        if (userName == null) {
                            userName = (String) ((SGuiClient) miClient).getSession().readField(SModConsts.USRU_USR, new int[] { annum.getFkUserId()}, SDbRegistry.FIELD_NAME);
                            userNames.put(annum.getFkUserId(), userName);
                        }
                        
                        pane.addTableRow(new SRowEmployeeBenefit(
                                annum.getPkAnnumId(),
                                yearBenefits + (annum.getPkAnnumId() - 1),
                                annum.getNormalizedBenefit(),
                                annum.getFkBenefitId(),
                                benefitName,
                                annum.getFkUserId(),
                                userName,
                                annum.getTsUser()));
                    }
                    
                    pane.renderTableRows();
                    pane.setTableRowSelection(0);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }
    
    private void renderWageFactors() {
        moWageFactorsPane.clearTableRows();
        
        if (moEmployeeBenefitTables != null) {
            int yearBenefits = SLibTimeUtils.digestYear(moFieldDateBenefits.getDate())[0];
            HashMap<Integer, String> userNames = new HashMap<>();
            
            for (SDbEmployeeWageFactorAnnum factor : moEmployeeBenefitTables.getChildWageFactors()) {
                String userName = factor.getFkUserId() == 0 ? "" : userNames.get(factor.getFkUserId());
                if (userName == null) {
                    userName = (String) ((SGuiClient) miClient).getSession().readField(SModConsts.USRU_USR, new int[] { factor.getFkUserId()}, SDbRegistry.FIELD_NAME);
                    userNames.put(factor.getFkUserId(), userName);
                }
                
                moWageFactorsPane.addTableRow(new SRowEmployeeWageFactor(
                        factor.getPkAnnumId(),
                        yearBenefits + (factor.getPkAnnumId() - 1),
                        factor.getVacationDays(),
                        factor.getVacationBonusPct(),
                        factor.getAnnualBonusDays(),
                        factor.getFkUserId(),
                        userName,
                        factor.getTsUser()));
            }
            
            moWageFactorsPane.renderTableRows();
            moWageFactorsPane.setTableRowSelection(0);
        }
    }

    private void renderBenefitsAndWageFactors() {
        for (int benefitType : SHrsBenefitUtils.createBenefitTypes()) {
            restoreBenTableLastSettings(benefitType);
            renderBenefits(benefitType);
        }
        
        renderWageFactors();
    }

    private void itemStateChangedDepartament() {
        if (moFieldFkDepartment.getKeyAsIntArray()[0] > 0) {
            jcbFkPosition.setEnabled(true);
            SFormUtilities.populateComboBox(miClient, jcbFkPosition, SModConsts.HRSU_POS, moFieldFkDepartment.getKeyAsIntArray()[0]);
            if (jcbFkPosition.getItemCount() <= 2) {
                jcbFkPosition.setSelectedIndex(jcbFkPosition.getItemCount() - 1);
            }
        }
        else {
            jcbFkPosition.setEnabled(false);
            jcbFkPosition.removeAllItems();
        }
    }
    
    private void itemStateChangedContractType() {
        jftContractExpiration.setEditable(false);
        jftContractExpiration.setFocusable(false);
        jbContractExpiration.setEnabled(false);
        jlContractExpirationHint.setText("");
        
        if (jcbFkContractType.getSelectedIndex() > 0) {
            int contractType = moFieldFkContractType.getKeyAsIntArray()[0];
            
            if (SHrsEmployeeUtils.isContractExpirationAllowed(contractType)) {
                jftContractExpiration.setEditable(true);
                jftContractExpiration.setFocusable(true);
                jbContractExpiration.setEnabled(true);
                jlContractExpirationHint.setText(SHrsEmployeeUtils.isContractExpirationRequired(contractType) ? "(fecha requerida)" : "(fecha opcional)");
            }
        }
    }
    
    private void renderBenTable(final int benefitTable, final JTextField jPeriod, final JTextField jPaymentType, final JTextField jUnionized) {
        if (benefitTable == 0) {
            jPeriod.setText("");
            jPaymentType.setText("");
            jUnionized.setText("");
        }
        else {
            SDbBenefitTable table = (SDbBenefitTable) miClient.getSession().readRegistry(SModConsts.HRS_BEN, new int[] { benefitTable });

            jPeriod.setText(SLibTimeUtils.dateFormatDatePeriod(table.getDateStart(), table.getDateEnd_n()));
            jPaymentType.setText(table.getFkPaymentTypeId_n() == 0 ? SHrsConsts.TXT_INDISTINCT : (String) miClient.getSession().readField(SModConsts.HRSS_TP_PAY, new int[] { table.getFkPaymentTypeId_n() }, SDbRegistry.FIELD_NAME));
            jUnionized.setText(table.composeUnionizedDescription());

            jPeriod.setCaretPosition(0);
            jPaymentType.setCaretPosition(0);
            jUnionized.setCaretPosition(0);
        }
    }
    
    private void renderBenTable(final int benefitType) {
        switch (benefitType) {
            case SModSysConsts.HRSS_TP_BEN_VAC:
                renderBenTable(moFieldBenVacTable.getKeyAsIntArray()[0], jtfVacPeriod, jtfVacPaymentType, jtfVacUnionized);
                break;
            case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                renderBenTable(moFieldBenVacBonTable.getKeyAsIntArray()[0], jtfVacBonPeriod, jtfVacBonPaymentType, jtfVacBonUnionized);
                break;
            case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                renderBenTable(moFieldBenAnnBonTable.getKeyAsIntArray()[0], jtfAnnBonPeriod, jtfAnnBonPaymentType, jtfAnnBonUnionized);
                break;
            default:
                // nothing
        }
    }
    
    private void restoreBenTableLastSettings(final int benefitType) {
        switch (benefitType) {
            case SModSysConsts.HRSS_TP_BEN_VAC:
                if (moEmployeeBenefitTables == null || moEmployeeBenefitTables.getFkBenefitVacationId() == 0) {
                    moFieldBenVacTable.resetField(); // triggers item-state-change event if needed
                    jspVacSeniorityStart.setValue(1);
                    jtfVacLastUpdate.setText("");
                    jtfVacLastUpdater.setText("");
                }
                else {
                    moFieldBenVacTable.setKey(new int[] { moEmployeeBenefitTables.getFkBenefitVacationId() }); // triggers item-state-change event if needed
                    jspVacSeniorityStart.setValue(moEmployeeBenefitTables.getStartingAnniversaryVacation());
                    jtfVacLastUpdate.setText(SLibUtils.DateFormatDatetime.format(moEmployeeBenefitTables.getTsUserUpdateVacation()));
                    jtfVacLastUpdater.setText((String) miClient.getSession().readField(SModConsts.USRU_USR, new int[] { moEmployeeBenefitTables.getFkUserUpdateVacationId() }, SDbRegistry.FIELD_NAME));
                    
                    jtfVacLastUpdate.setCaretPosition(0);
                    jtfVacLastUpdater.setCaretPosition(0);
                }
                break;
                
            case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                if (moEmployeeBenefitTables == null || moEmployeeBenefitTables.getFkBenefitVacationBonusId() == 0) {
                    moFieldBenVacBonTable.resetField(); // triggers item-state-change event if needed
                    jspVacBonSeniorityStart.setValue(1);
                    jtfVacBonLastUpdate.setText("");
                    jtfVacBonLastUpdater.setText("");
                }
                else {
                    moFieldBenVacBonTable.setKey(new int[] { moEmployeeBenefitTables.getFkBenefitVacationBonusId() }); // triggers item-state-change event if needed
                    jspVacBonSeniorityStart.setValue(moEmployeeBenefitTables.getStartingAnniversaryVacationBonus());
                    jtfVacBonLastUpdate.setText(SLibUtils.DateFormatDatetime.format(moEmployeeBenefitTables.getTsUserUpdateVacationBonus()));
                    jtfVacBonLastUpdater.setText((String) miClient.getSession().readField(SModConsts.USRU_USR, new int[] { moEmployeeBenefitTables.getFkUserUpdateVacationBonusId()}, SDbRegistry.FIELD_NAME));
                    
                    jtfVacBonLastUpdate.setCaretPosition(0);
                    jtfVacBonLastUpdater.setCaretPosition(0);
                }
                break;
                
            case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                if (moEmployeeBenefitTables == null || moEmployeeBenefitTables.getFkBenefitAnnualBonusId() == 0) {
                    moFieldBenAnnBonTable.resetField(); // triggers item-state-change event if needed
                    jspAnnBonSeniorityStart.setValue(1);
                    jtfAnnBonLastUpdate.setText("");
                    jtfAnnBonLastUpdater.setText("");
                }
                else {
                    moFieldBenAnnBonTable.setKey(new int[] { moEmployeeBenefitTables.getFkBenefitAnnualBonusId()}); // triggers item-state-change event if needed
                    jspAnnBonSeniorityStart.setValue(moEmployeeBenefitTables.getStartingAnniversaryAnnualBonus());
                    jtfAnnBonLastUpdate.setText(SLibUtils.DateFormatDatetime.format(moEmployeeBenefitTables.getTsUserUpdateAnnualBonus()));
                    jtfAnnBonLastUpdater.setText((String) miClient.getSession().readField(SModConsts.USRU_USR, new int[] { moEmployeeBenefitTables.getFkUserUpdateAnnualBonusId()}, SDbRegistry.FIELD_NAME));
                    
                    jtfAnnBonLastUpdate.setCaretPosition(0);
                    jtfAnnBonLastUpdater.setCaretPosition(0);
                }
                break;
                
            default:
                // nothing
        }
    }
    
    private void itemStateChangedBenTable(final int benefitType) {
        renderBenTable(benefitType);
    }

    private void actionOk() {
        SFormValidation validation = formValidate();

        if (validation.getIsError()) {
            if (validation.getTabbedPaneIndex() != -1) {
                jTabbedPane.setSelectedIndex(validation.getTabbedPaneIndex());
            }
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

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void actionDateBenefitsEdit() {
        updateStatusDateBenefits(true);
        jftDateBenefits.requestFocusInWindow();
    }

    private void actionDateBirth() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateBirth.getDate(), moFieldDateBirth);
    }

    private void actionLoadFileImagePhoto() {
        File file = null;
        ByteArrayOutputStream byteArrayOSImagePhoto = null;
        FileFilter filter = new FileNameExtensionFilter("JPG, PNG, GIF & BMP Images", "jpg", "jpeg", "png", "gif", "bmp");
        miClient.getFileChooser().repaint();
        miClient.getFileChooser().setAcceptAllFileFilterUsed(false);
        miClient.getFileChooser().addChoosableFileFilter(filter);

        if (miClient.getFileChooser().showOpenDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
            file = new File(miClient.getFileChooser().getSelectedFile().getAbsolutePath());
            
            if (file.length() > SErpConsts.IMG_MAX_SIZE) {
                miClient.showMsgBoxWarning("El tamaño de la imagen seleccionada no puede ser mayor a 512 MB.");
            }
            else {
                moFieldFileImagePhoto.setFieldValue(miClient.getFileChooser().getSelectedFile().getName());
                try {
                    byteArrayOSImagePhoto = new ByteArrayOutputStream();
                    ImageIO.write(ImageIO.read(new File(miClient.getFileChooser().getSelectedFile().getAbsolutePath())), "jpg", byteArrayOSImagePhoto);

                    moXtaImageIconPhoto_n = new ImageIcon(byteArrayOSImagePhoto.toByteArray());
                    mbPhotoChange = true;
                    enableButtonsPhoto(true);
                    
                    if (moXtaImageIconPhoto_n.getIconHeight() > 300) {
                        moXtaImageIconPhoto_n = new ImageIcon(moXtaImageIconPhoto_n.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                    }
                    jlImgPhoto.setIcon(moXtaImageIconPhoto_n);
                    jlImgPhoto.setText("");
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
        
        miClient.getFileChooser().resetChoosableFileFilters();
        miClient.getFileChooser().setAcceptAllFileFilterUsed(true);
    }

    private void actionFileImagePhotoRemove() {
        moXtaImageIconPhoto_n = null;
        mbPhotoChange = true;
        enableButtonsPhoto(false);
        
        jlImgPhoto.setIcon(null);
        jlImgPhoto.setText(SPanelQueryIntegralEmployee.NO_PHOTO);
    }
    
    private void actionFileImagePhotoView() {
        new SDialogViewEmployeeImages(miClient, moXtaImageIconPhoto_n, "Fotografía del empleado").setVisible(true);
    }

    private void actionLoadFileImageSignature() {
        File file = null;
        ByteArrayOutputStream byteArrayOSSignatureSignature = null;
        FileFilter filter = new FileNameExtensionFilter("JPG, PNG, GIF & BMP Images", "jpg", "jpeg", "png", "gif", "bmp");
        miClient.getFileChooser().repaint();
        miClient.getFileChooser().setAcceptAllFileFilterUsed(false);
        miClient.getFileChooser().addChoosableFileFilter(filter);

        if (miClient.getFileChooser().showOpenDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
            file = new File(miClient.getFileChooser().getSelectedFile().getAbsolutePath());
            
            if (file.length() > SErpConsts.IMG_MAX_SIZE) {
                miClient.showMsgBoxWarning("El tamaño de la imagen seleccionada no puede ser mayor a 512 MB.");
            }
            else {
                moFieldFileImageSignature.setFieldValue(miClient.getFileChooser().getSelectedFile().getName());
                try {
                    byteArrayOSSignatureSignature = new ByteArrayOutputStream();
                    ImageIO.write(ImageIO.read(new File(miClient.getFileChooser().getSelectedFile().getAbsolutePath())), "jpg", byteArrayOSSignatureSignature);

                    moXtaImageIconSignature_n = new ImageIcon(byteArrayOSSignatureSignature.toByteArray());
                    mbSignatureChange = true;
                    enableButtonsSignature(true);
                    
                    jlImgSignature.setIcon(moXtaImageIconSignature_n);
                    jlImgSignature.setText("");
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
        
        miClient.getFileChooser().resetChoosableFileFilters();
        miClient.getFileChooser().setAcceptAllFileFilterUsed(true);
    }
    
    private void actionFileImageSignatureRemove() {
        moXtaImageIconSignature_n = null;
        mbSignatureChange = true;
        enableButtonsSignature(false);
                    
        jlImgSignature.setIcon(null);
        jlImgSignature.setText(SPanelQueryIntegralEmployee.NO_SIGNATURE);
    }
    
    private void actionFileImageSignatureView() {
        new SDialogViewEmployeeImages(miClient, moXtaImageIconSignature_n, "Firma del empleado").setVisible(true);
    }
    
    private void actionDateBenefits() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateBenefits.getDate(), moFieldDateBenefits);
    }

    private void actionDateLastHire() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateLastHire.getDate(), moFieldDateLastHire);
    }

    private void actionDateSalaryChange() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateChangeSalary.getDate(), moFieldDateChangeSalary);
    }

    private void actionDateWageChange() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateChangeWage.getDate(), moFieldDateChangeWage);
    }

    private void actionDateSalarySscBaseChange() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateChangeSalarySscBase.getDate(), moFieldDateChangeSalarySscBase);
    }

    private void actionContractExpiration() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldContractExpiration.getDate(), moFieldContractExpiration);
    }
    
    private void computeBenModify(final int benefitType, final SFormField fieldBenTable, final JSpinner jAnniversary, final JButton jModify, final JButton jUpdate, final JButton jCancel, final boolean focusRequired) {
        if (moFieldDateBenefits.getDate() == null) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + SGuiUtils.getLabelName(jlDateBenefits) + "'.");
            jTabbedPane.setSelectedIndex(TAB_DATA_EMP);
            jftDateBenefits.requestFocusInWindow();
        }
        else if (jcbFkPaymentType.getSelectedIndex() <= 0) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + SGuiUtils.getLabelName(jlFkPaymentType) + "'.");
            jTabbedPane.setSelectedIndex(TAB_DATA_EMP);
            jcbFkPaymentType.requestFocusInWindow();
        }
        else {
            fieldBenTable.getComponent().setEnabled(true);
            jAnniversary.setEnabled(moEmployee != null); // new employees cannot choose anniversary

            jbVacModify.setEnabled(false);
            jbVacBonModify.setEnabled(false);
            jbAnnBonModify.setEnabled(false);

            jUpdate.setEnabled(true);
            jCancel.setEnabled(true);

            if (focusRequired) {
               fieldBenTable.getComponent().requestFocusInWindow();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void computeBenUpdate(final int benefitType, final SFormField fieldBenTable, final JSpinner jAnniversary, final boolean focusRequired) {
        JComboBox<SFormComponentItem> comboBoxBenTable = (JComboBox<SFormComponentItem>) fieldBenTable.getComponent();
        boolean isFirstTimeUpdate = moEmployeeBenefitTables == null || moEmployeeBenefitTables.isRegistryNew() || !moEmployeeBenefitTables.isBenefitSet(benefitType);
        
        if (comboBoxBenTable.getSelectedIndex() <= 0) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + SGuiUtils.getLabelName(jlVacTable) + "'."); // note that all these set of labels have the same text!
            fieldBenTable.getComponent().requestFocusInWindow();
        }
        else if ((int) jAnniversary.getValue() < 1 || (int) jAnniversary.getValue() > SDbEmployeeBenefitTables.MAX_ANNUMS) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + SGuiUtils.getLabelName(jlVacSeniorityStart) + "': de 1 a " + SDbEmployeeBenefitTables.MAX_ANNUMS + "."); // note that all these set of labels have the same text!
            jAnniversary.requestFocusInWindow();
        }
        else if (isFirstTimeUpdate && (int) jAnniversary.getValue() != 1) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + SGuiUtils.getLabelName(jlVacSeniorityStart) + "': 1."); // note that all these set of labels have the same text!
            jAnniversary.requestFocusInWindow();
        }
        else {
            if (isFirstTimeUpdate || miClient.showMsgBoxConfirm("¿Está seguro que desea asignar la prestación '" + ((SFormComponentItem) comboBoxBenTable.getSelectedItem()).getItem() + "',\n"
                    + "a '" + composeName() + "', a partir del aniversario " + jAnniversary.getValue() + "?") == JOptionPane.YES_OPTION) {
                try {
                    int benefitTable = fieldBenTable.getKeyAsIntArray()[0];
                    int startingAnniversary = (int) jAnniversary.getValue();
                    boolean wageFactorsComputed = false;
                    
                    if (moEmployeeBenefitTables == null) {
                        moEmployeeBenefitTables = new SDbEmployeeBenefitTables();
                    }
                    
                    moEmployeeBenefitTables.assignBenefitTable(miClient.getSession(), benefitType, benefitTable, startingAnniversary);
                    wageFactorsComputed = moEmployeeBenefitTables.computeWageFactors(miClient.getSession(), false);
                    
                    switch (benefitType) {
                        case SModSysConsts.HRSS_TP_BEN_VAC:
                            jtfVacLastUpdate.setText("(Ahora mismo)");
                            jtfVacLastUpdater.setText(miClient.getSession().getUser().getName());

                            jtfVacLastUpdate.setCaretPosition(0);
                            jtfVacLastUpdater.setCaretPosition(0);
                            break;

                        case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                            jtfVacBonLastUpdate.setText("(Ahora mismo)");
                            jtfVacBonLastUpdater.setText(miClient.getSession().getUser().getName());

                            jtfVacBonLastUpdate.setCaretPosition(0);
                            jtfVacBonLastUpdater.setCaretPosition(0);
                            break;

                        case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                            jtfAnnBonLastUpdate.setText("(Ahora mismo)");
                            jtfAnnBonLastUpdater.setText(miClient.getSession().getUser().getName());

                            jtfAnnBonLastUpdate.setCaretPosition(0);
                            jtfAnnBonLastUpdater.setCaretPosition(0);
                            break;

                        default:
                            // nothing
                    }
                    
                    renderBenefits(benefitType);
                    
                    if (wageFactorsComputed) {
                        renderWageFactors();
                    }
                    
                    actionBenCancel(benefitType, true, focusRequired);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void computeBenCancel(final int benefitType, final SFormField fieldBenTable, final JSpinner jAnniversary, final JButton jModify, final JButton jUpdate, final JButton jCancel, final boolean justUpdated, final boolean focusRequired) {
        if (!justUpdated) {
            restoreBenTableLastSettings(benefitType);
            renderBenTable(benefitType);
        }

        fieldBenTable.getComponent().setEnabled(false);
        jAnniversary.setEnabled(false);

        jbVacModify.setEnabled(true);
        jbVacBonModify.setEnabled(true);
        jbAnnBonModify.setEnabled(true);
        
        jUpdate.setEnabled(false);
        jCancel.setEnabled(false);

        if (focusRequired) {
           jModify.requestFocusInWindow();
        }
    }

    private void actionBenModify(final int benefitType, final boolean focusRequired) {
        switch (benefitType) {
            case SModSysConsts.HRSS_TP_BEN_VAC:
                computeBenModify(benefitType, moFieldBenVacTable, jspVacSeniorityStart, jbVacModify, jbVacUpdate, jbVacCancel, focusRequired);
                break;
            case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                computeBenModify(benefitType, moFieldBenVacBonTable, jspVacBonSeniorityStart, jbVacBonModify, jbVacBonUpdate, jbVacBonCancel, focusRequired);
                break;
            case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                computeBenModify(benefitType, moFieldBenAnnBonTable, jspAnnBonSeniorityStart, jbAnnBonModify, jbAnnBonUpdate, jbAnnBonCancel, focusRequired);
                break;
            default:
                // nothing
        }
    }

    private void actionBenUpdate(final int benefitType, final boolean focusRequired) {
        switch (benefitType) {
            case SModSysConsts.HRSS_TP_BEN_VAC:
                computeBenUpdate(benefitType, moFieldBenVacTable, jspVacSeniorityStart, focusRequired);
                break;
            case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                computeBenUpdate(benefitType, moFieldBenVacBonTable, jspVacBonSeniorityStart, focusRequired);
                break;
            case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                computeBenUpdate(benefitType, moFieldBenAnnBonTable, jspAnnBonSeniorityStart, focusRequired);
                break;
            default:
                // nothing
        }
    }
    
    private void actionBenCancel(final int benefitType, final boolean justUpdated, final boolean focusRequired) {
        switch (benefitType) {
            case SModSysConsts.HRSS_TP_BEN_VAC:
                computeBenCancel(benefitType, moFieldBenVacTable, jspVacSeniorityStart, jbVacModify, jbVacUpdate, jbVacCancel, justUpdated, focusRequired);
                break;
            case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                computeBenCancel(benefitType, moFieldBenVacBonTable, jspVacBonSeniorityStart, jbVacBonModify, jbVacBonUpdate, jbVacBonCancel, justUpdated, focusRequired);
                break;
            case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                computeBenCancel(benefitType, moFieldBenAnnBonTable, jspAnnBonSeniorityStart, jbAnnBonModify, jbAnnBonUpdate, jbAnnBonCancel, justUpdated, focusRequired);
                break;
            default:
                // nothing
        }
    }

    private void itemStateSalary(final boolean focusRequired) {
        if (jckChangeSalary.isSelected()) {
            jtfSalary.setEnabled(true);
            jftDateChangeSalary.setEnabled(true);
            jbDateChangeSalary.setEnabled(true);

            moFieldDateChangeSalary.setDate(null);

            if (focusRequired) {
                jtfSalary.requestFocusInWindow();
            }
        }
        else {
            jtfSalary.setEnabled(false);
            jftDateChangeSalary.setEnabled(false);
            jbDateChangeSalary.setEnabled(false);

            // If no changes, then restore values:

            moFieldSalary.setFieldValue(moEmployee == null ? 0d : moEmployee.getSalary());
            moFieldDateChangeSalary.setFieldValue(moEmployee == null ? null : moEmployee.getDateSalary());
        }
    }

    private void itemStateWage(final boolean focusRequired) {
        if (jckChangeWage.isSelected()) {
            jtfWage.setEnabled(true);
            jftDateChangeWage.setEnabled(true);
            jbDateChangeWage.setEnabled(true);

            moFieldDateChangeWage.setDate(null);

            if (focusRequired) {
                jtfWage.requestFocusInWindow();
            }
        }
        else {
            jtfWage.setEnabled(false);
            jftDateChangeWage.setEnabled(false);
            jbDateChangeWage.setEnabled(false);

            // If no changes, then restore values:

            moFieldWage.setFieldValue(moEmployee == null ? 0d : moEmployee.getWage());
            moFieldDateChangeWage.setFieldValue(moEmployee == null ? null : moEmployee.getDateWage());
        }
    }

    private void itemStateSalarySscBaseChange(final boolean focusRequired) {
        if (jckChangeSalarySscBase.isSelected()) {
            jtfSalarySscBase.setEnabled(true);
            jftDateChangeSalarySscBase.setEnabled(true);
            jbDateChangeSalarySscBase.setEnabled(true);

            moFieldDateChangeSalarySscBase.setDate(null);

            if (focusRequired) {
                jtfSalarySscBase.requestFocusInWindow();
            }
        }
        else {
            jtfSalarySscBase.setEnabled(false);
            jftDateChangeSalarySscBase.setEnabled(false);
            jbDateChangeSalarySscBase.setEnabled(false);

            // If no changes, then restore values:

            moFieldSalarySscBase.setFieldValue(moEmployee == null ? 0d : moEmployee.getSalarySscBase());
            moFieldDateChangeSalarySscBase.setFieldValue(moEmployee == null ? null : moEmployee.getDateSalarySscBase());
        }
    }

    private void itemStateChangePaymentType() {
        mbUpdatingForm = true;

        switch (moFieldFkPaymentType.getKeyAsIntArray()[0]) {
            case SModSysConsts.HRSS_TP_PAY_WEE:
                jckChangeSalary.setEnabled(moEmployee != null && moFieldSalary.getDouble() != 0);
                jckChangeSalary.setSelected(moEmployee == null || moFieldSalary.getDouble() == 0);
                jckChangeWage.setEnabled(false);
                jckChangeWage.setSelected(false);
                break;

            case SModSysConsts.HRSS_TP_PAY_FOR:
                jckChangeSalary.setEnabled(false);
                jckChangeSalary.setSelected(false);
                jckChangeWage.setEnabled(moEmployee != null && moFieldWage.getDouble() != 0);
                jckChangeWage.setSelected(moEmployee == null || moFieldWage.getDouble() == 0);
                break;

            default:
                jckChangeSalary.setEnabled(false);
                jckChangeSalary.setSelected(false);
                jckChangeWage.setEnabled(false);
                jckChangeWage.setSelected(false);
        }

        itemStateSalary(false);
        itemStateWage(false);

        mbUpdatingForm = false;
    }
    
    private void focusLostDateBenefits() {
        jtfSeniority.setText(SLibTimeUtils.formatAge(moFieldDateBenefits.getDate(), miClient.getSession().getSystemDate()));
    }
    
    private void focusLostDateBirth() {
        jtfAge.setText(SLibTimeUtils.formatAge(moFieldDateBirth.getDate(), miClient.getSession().getSystemDate()));
    }
     
    private void focusLostMateDateBirth() {
        jtfMateAge.setText(SLibTimeUtils.formatAge(moFieldMateDateBirth.getDate(), miClient.getSession().getSystemDate()));
    }
    
    private void focusLostSonDateBirth1() {
        jtfSonAge1.setText(SLibTimeUtils.formatAge(moFieldSonDateBirth1.getDate(), miClient.getSession().getSystemDate()));
    }
    
    private void focusLostSonDateBirth2() {
        jtfSonAge2.setText(SLibTimeUtils.formatAge(moFieldSonDateBirth2.getDate(), miClient.getSession().getSystemDate()));
    }
    
    private void focusLostSonDateBirth3() {
        jtfSonAge3.setText(SLibTimeUtils.formatAge(moFieldSonDateBirth3.getDate(), miClient.getSession().getSystemDate()));
    }
    
    private void focusLostSonDateBirth4() {
        jtfSonAge4.setText(SLibTimeUtils.formatAge(moFieldSonDateBirth4.getDate(), miClient.getSession().getSystemDate()));
    }
    
    private void focusLostSonDateBirth5() {
        jtfSonAge5.setText(SLibTimeUtils.formatAge(moFieldSonDateBirth5.getDate(), miClient.getSession().getSystemDate()));
    }
    
    private void renderEmployee(final JTextField jEmpName, final JTextField jEmpNumber, final JTextField jPaymentType, final JCheckBox jIsUnionized,
            final JTextField jBenefitsDate, final JTextField jCutOffDate, final JTextField jSeniorityYears, final JTextField jSeniorityDays, final JTextField jSeniorityProp) {
        Date benefits = moFieldDateBenefits.getDate();
        SAnniversary anniversary = new SAnniversary(benefits != null ? benefits : miClient.getSession().getSystemDate(), miClient.getSession().getSystemDate());
        
        jEmpName.setText(composeName());
        jEmpNumber.setText(moFieldNumber.getString());
        jPaymentType.setText(jcbFkPaymentType.getSelectedItem().toString());
        jIsUnionized.setSelected(moFieldIsUnionized.getBoolean());
        jBenefitsDate.setText(SLibUtils.DateFormatDate.format(benefits));
        jCutOffDate.setText(SLibUtils.DateFormatDate.format(miClient.getSession().getSystemDate()));
        jSeniorityYears.setText("" + anniversary.getElapsedYears());
        jSeniorityDays.setText("" + anniversary.getElapsedYearDaysForBenefits());
        jSeniorityProp.setText(SLibUtils.DecimalFormatValue8D.format(anniversary.getCurrentAnniversaryPropPartForBenefits()));
        
        jEmpName.setCaretPosition(0);
        jEmpNumber.setCaretPosition(0);
        jPaymentType.setCaretPosition(0);
        jBenefitsDate.setCaretPosition(0);
        jCutOffDate.setCaretPosition(0);
        jSeniorityYears.setCaretPosition(0);
        jSeniorityDays.setCaretPosition(0);
        jSeniorityProp.setCaretPosition(0);
    }
    
    private void renderEmployee() {
        switch (jTabbedPane.getSelectedIndex()) {
            case TAB_BENEFITS:
                renderEmployee(jtfBenEmpName, jtfBenEmpNumber, jtfBenPaymentType, jckBenIsUnionized, jtfBenDateBenefits, jtfBenDateCutoff, jtfBenSeniorityYears, jtfBenSeniorityDays, jtfBenSeniorityProp);
                break;
            case TAB_WAGE_FACTORS:
                renderEmployee(jtfFacEmpName, jtfFacEmpNumber, jtfFacPaymentType, jckFacIsUnionized, jtfFacDateBenefits, jtfFacDateCutoff, jtfFacSeniorityYears, jtfFacSeniorityDays, jtfFacSeniorityProp);
                break;
            default:
                // nothing
        }
    }
    
    private void stateChangedTabbedPane() {
        if (jTabbedPane.getSelectedIndex() == TAB_BENEFITS || jTabbedPane.getSelectedIndex() == TAB_WAGE_FACTORS) {
            renderEmployee();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgCheckerPolicy;
    private javax.swing.ButtonGroup bgOvertime;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JButton jbAnnBonCancel;
    private javax.swing.JButton jbAnnBonModify;
    private javax.swing.JButton jbAnnBonUpdate;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbContractExpiration;
    private javax.swing.JButton jbDateBenefits;
    private javax.swing.JButton jbDateBenefitsEdit;
    private javax.swing.JButton jbDateBirth;
    private javax.swing.JButton jbDateChangeSalary;
    private javax.swing.JButton jbDateChangeSalarySscBase;
    private javax.swing.JButton jbDateChangeWage;
    private javax.swing.JButton jbDateLastHire;
    private javax.swing.JButton jbImagePhoto;
    private javax.swing.JButton jbImagePhotoRemove;
    private javax.swing.JButton jbImagePhotoView;
    private javax.swing.JButton jbImageSignature;
    private javax.swing.JButton jbImageSignatureRemove;
    private javax.swing.JButton jbImageSignatureView;
    private javax.swing.JButton jbMateDateBirth;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbSonDateBirth1;
    private javax.swing.JButton jbSonDateBirth2;
    private javax.swing.JButton jbSonDateBirth3;
    private javax.swing.JButton jbSonDateBirth4;
    private javax.swing.JButton jbSonDateBirth5;
    private javax.swing.JButton jbVacBonCancel;
    private javax.swing.JButton jbVacBonModify;
    private javax.swing.JButton jbVacBonUpdate;
    private javax.swing.JButton jbVacCancel;
    private javax.swing.JButton jbVacModify;
    private javax.swing.JButton jbVacUpdate;
    private javax.swing.JComboBox<SFormComponentItem> jcbAnnBonTable;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkBank_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCatalogueBloodTypeTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCatalogueEducationTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCatalogueMaritalStatusTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCatalogueSexTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkContractType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkDepartment;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkEmployeeType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkGroceryService;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkMateCatalogueSexTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkMwzType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkPaymentType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkPosition;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkPositionRiskType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkRecruitmentSchemaType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSalaryType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkShift;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSonCatalogueSexTypeId1;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSonCatalogueSexTypeId2;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSonCatalogueSexTypeId3;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSonCatalogueSexTypeId4;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSonCatalogueSexTypeId5;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkWorkerType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkWorkingDayType;
    private javax.swing.JComboBox<SFormComponentItem> jcbVacBonTable;
    private javax.swing.JComboBox<SFormComponentItem> jcbVacTable;
    private javax.swing.JCheckBox jckBenIsUnionized;
    private javax.swing.JCheckBox jckChangeSalary;
    private javax.swing.JCheckBox jckChangeSalarySscBase;
    private javax.swing.JCheckBox jckChangeWage;
    private javax.swing.JCheckBox jckFacIsUnionized;
    private javax.swing.JCheckBox jckIsActive;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JCheckBox jckIsMfgOperator;
    private javax.swing.JCheckBox jckIsUnionized;
    private javax.swing.JCheckBox jckMateDeceased;
    private javax.swing.JCheckBox jckSonDeceased1;
    private javax.swing.JCheckBox jckSonDeceased2;
    private javax.swing.JCheckBox jckSonDeceased3;
    private javax.swing.JCheckBox jckSonDeceased4;
    private javax.swing.JCheckBox jckSonDeceased5;
    private javax.swing.JFormattedTextField jftAlternativeId;
    private javax.swing.JFormattedTextField jftContractExpiration;
    private javax.swing.JFormattedTextField jftDateBenefits;
    private javax.swing.JFormattedTextField jftDateBirth;
    private javax.swing.JFormattedTextField jftDateChangeSalary;
    private javax.swing.JFormattedTextField jftDateChangeSalarySscBase;
    private javax.swing.JFormattedTextField jftDateChangeWage;
    private javax.swing.JFormattedTextField jftDateLastDismissal_n;
    private javax.swing.JFormattedTextField jftDateLastHire;
    private javax.swing.JFormattedTextField jftMateDateBirth;
    private javax.swing.JFormattedTextField jftNumber;
    private javax.swing.JFormattedTextField jftSonDateBirth1;
    private javax.swing.JFormattedTextField jftSonDateBirth2;
    private javax.swing.JFormattedTextField jftSonDateBirth3;
    private javax.swing.JFormattedTextField jftSonDateBirth4;
    private javax.swing.JFormattedTextField jftSonDateBirth5;
    private javax.swing.JLabel jlAlternativeId;
    private javax.swing.JLabel jlAnnBonLastUpdate;
    private javax.swing.JLabel jlAnnBonPaymentType;
    private javax.swing.JLabel jlAnnBonPeriod;
    private javax.swing.JLabel jlAnnBonSeniorityStart;
    private javax.swing.JLabel jlAnnBonSeniorityStartHelp;
    private javax.swing.JLabel jlAnnBonTable;
    private javax.swing.JLabel jlBankAccount;
    private javax.swing.JLabel jlBenDateBenefits;
    private javax.swing.JLabel jlBenDateCutoff;
    private javax.swing.JLabel jlBenEmp;
    private javax.swing.JLabel jlBenPaymentType;
    private javax.swing.JLabel jlBenSeniority;
    private javax.swing.JLabel jlBenSeniorityDays;
    private javax.swing.JLabel jlBenSeniorityProp;
    private javax.swing.JLabel jlBenSeniorityYears;
    private javax.swing.JLabel jlBirthPlace;
    private javax.swing.JLabel jlCheckerPolicy;
    private javax.swing.JLabel jlContractExpiration;
    private javax.swing.JLabel jlContractExpirationHint;
    private javax.swing.JLabel jlDateBenefits;
    private javax.swing.JLabel jlDateBirth;
    private javax.swing.JLabel jlDateLastDismissal_n;
    private javax.swing.JLabel jlDateLastHire;
    private javax.swing.JLabel jlFacDateBenefits;
    private javax.swing.JLabel jlFacDateCutoff;
    private javax.swing.JLabel jlFacEmp;
    private javax.swing.JLabel jlFacSeniority;
    private javax.swing.JLabel jlFacSeniorityDays;
    private javax.swing.JLabel jlFacSeniorityProp;
    private javax.swing.JLabel jlFacSeniorityYears;
    private javax.swing.JLabel jlFirstname;
    private javax.swing.JLabel jlFiscalId;
    private javax.swing.JLabel jlFkBank_n;
    private javax.swing.JLabel jlFkCatalogueBloodTypeTypeId;
    private javax.swing.JLabel jlFkCatalogueEducationTypeId;
    private javax.swing.JLabel jlFkCatalogueMaritalStatusTypeId;
    private javax.swing.JLabel jlFkCatalogueSexTypeId;
    private javax.swing.JLabel jlFkContractType;
    private javax.swing.JLabel jlFkDepartment;
    private javax.swing.JLabel jlFkEmployeeType;
    private javax.swing.JLabel jlFkGroceryService;
    private javax.swing.JLabel jlFkMwzType;
    private javax.swing.JLabel jlFkPaymentType;
    private javax.swing.JLabel jlFkPosition;
    private javax.swing.JLabel jlFkPositionRiskType;
    private javax.swing.JLabel jlFkRecruitmentSchemaType;
    private javax.swing.JLabel jlFkSalaryType;
    private javax.swing.JLabel jlFkShift;
    private javax.swing.JLabel jlFkWorkerType;
    private javax.swing.JLabel jlFkWorkingDayType;
    private javax.swing.JLabel jlGroceryServiceAccount;
    private javax.swing.JLabel jlImagePhoto;
    private javax.swing.JLabel jlImageSignature;
    private javax.swing.JLabel jlImgPhoto;
    private javax.swing.JLabel jlImgSignature;
    private javax.swing.JLabel jlLastname;
    private javax.swing.JLabel jlLastname1;
    private javax.swing.JLabel jlMailCompany;
    private javax.swing.JLabel jlMailOwn;
    private javax.swing.JLabel jlMate;
    private javax.swing.JLabel jlNumber;
    private javax.swing.JLabel jlOvertimePolicy;
    private javax.swing.JLabel jlPhone;
    private javax.swing.JLabel jlRelative;
    private javax.swing.JLabel jlRelativeDateBirth;
    private javax.swing.JLabel jlRelativeName;
    private javax.swing.JLabel jlRelativeSex;
    private javax.swing.JLabel jlRelativeSex1;
    private javax.swing.JLabel jlSalary;
    private javax.swing.JLabel jlSalarySscBase;
    private javax.swing.JLabel jlSocialSecurityNumber;
    private javax.swing.JLabel jlSon1;
    private javax.swing.JLabel jlSon2;
    private javax.swing.JLabel jlSon3;
    private javax.swing.JLabel jlSon4;
    private javax.swing.JLabel jlSon5;
    private javax.swing.JLabel jlUmf;
    private javax.swing.JLabel jlVacBonLastUpdate;
    private javax.swing.JLabel jlVacBonPaymentType;
    private javax.swing.JLabel jlVacBonPeriod;
    private javax.swing.JLabel jlVacBonSeniorityStart;
    private javax.swing.JLabel jlVacBonSeniorityStartHellp;
    private javax.swing.JLabel jlVacBonTable;
    private javax.swing.JLabel jlVacLastUpdate;
    private javax.swing.JLabel jlVacPaymentType;
    private javax.swing.JLabel jlVacPeriod;
    private javax.swing.JLabel jlVacSeniorityStart;
    private javax.swing.JLabel jlVacSeniorityStartHelp;
    private javax.swing.JLabel jlVacTable;
    private javax.swing.JLabel jlWage;
    private javax.swing.JLabel jlWorkingHoursDay;
    private javax.swing.JPanel jpBenefits;
    private javax.swing.JPanel jpBenefitsAnnBon;
    private javax.swing.JPanel jpBenefitsAnnBon1;
    private javax.swing.JPanel jpBenefitsAnnBon11;
    private javax.swing.JPanel jpBenefitsAnnBon12;
    private javax.swing.JPanel jpBenefitsAnnBon13;
    private javax.swing.JPanel jpBenefitsAnnBon2;
    private javax.swing.JPanel jpBenefitsAnnBon21;
    private javax.swing.JPanel jpBenefitsAnnBon22;
    private javax.swing.JPanel jpBenefitsAnnBon23;
    private javax.swing.JPanel jpBenefitsAnnBonEdit;
    private javax.swing.JPanel jpBenefitsAnnBonPane;
    private javax.swing.JPanel jpBenefitsEmployee;
    private javax.swing.JPanel jpBenefitsEmployee1;
    private javax.swing.JPanel jpBenefitsEmployee2;
    private javax.swing.JPanel jpBenefitsTables;
    private javax.swing.JPanel jpBenefitsVac;
    private javax.swing.JPanel jpBenefitsVac1;
    private javax.swing.JPanel jpBenefitsVac11;
    private javax.swing.JPanel jpBenefitsVac12;
    private javax.swing.JPanel jpBenefitsVac13;
    private javax.swing.JPanel jpBenefitsVac2;
    private javax.swing.JPanel jpBenefitsVac21;
    private javax.swing.JPanel jpBenefitsVac22;
    private javax.swing.JPanel jpBenefitsVac23;
    private javax.swing.JPanel jpBenefitsVacBon;
    private javax.swing.JPanel jpBenefitsVacBon1;
    private javax.swing.JPanel jpBenefitsVacBon11;
    private javax.swing.JPanel jpBenefitsVacBon12;
    private javax.swing.JPanel jpBenefitsVacBon13;
    private javax.swing.JPanel jpBenefitsVacBon2;
    private javax.swing.JPanel jpBenefitsVacBon21;
    private javax.swing.JPanel jpBenefitsVacBon22;
    private javax.swing.JPanel jpBenefitsVacBon23;
    private javax.swing.JPanel jpBenefitsVacBonEdit;
    private javax.swing.JPanel jpBenefitsVacBonPane;
    private javax.swing.JPanel jpBenefitsVacEdit;
    private javax.swing.JPanel jpBenefitsVacPane;
    private javax.swing.JPanel jpBranchAddress;
    private javax.swing.JPanel jpEmployee;
    private javax.swing.JLabel jpFacPaymentType;
    private javax.swing.JPanel jpOficialAddress;
    private javax.swing.JPanel jpPersonalData;
    private javax.swing.JPanel jpWageFactors;
    private javax.swing.JPanel jpWageFactorsEmployee;
    private javax.swing.JPanel jpWageFactorsEmployee1;
    private javax.swing.JPanel jpWageFactorsEmployee2;
    private javax.swing.JPanel jpWageFactorsPane;
    private javax.swing.JRadioButton jradCheckerAllways;
    private javax.swing.JRadioButton jradCheckerNever;
    private javax.swing.JRadioButton jradCheckerSometimes;
    private javax.swing.JRadioButton jradOvertimeAllways;
    private javax.swing.JRadioButton jradOvertimeNever;
    private javax.swing.JRadioButton jradOvertimeSometimes;
    private javax.swing.JSpinner jspAnnBonSeniorityStart;
    private javax.swing.JSpinner jspVacBonSeniorityStart;
    private javax.swing.JSpinner jspVacSeniorityStart;
    private javax.swing.JTextField jtfAge;
    private javax.swing.JTextField jtfAnnBonLastUpdate;
    private javax.swing.JTextField jtfAnnBonLastUpdater;
    private javax.swing.JTextField jtfAnnBonPaymentType;
    private javax.swing.JTextField jtfAnnBonPeriod;
    private javax.swing.JTextField jtfAnnBonUnionized;
    private javax.swing.JTextField jtfBankAccount;
    private javax.swing.JTextField jtfBenDateBenefits;
    private javax.swing.JTextField jtfBenDateCutoff;
    private javax.swing.JTextField jtfBenEmpName;
    private javax.swing.JTextField jtfBenEmpNumber;
    private javax.swing.JTextField jtfBenPaymentType;
    private javax.swing.JTextField jtfBenSeniorityDays;
    private javax.swing.JTextField jtfBenSeniorityProp;
    private javax.swing.JTextField jtfBenSeniorityYears;
    private javax.swing.JTextField jtfBirthPlace;
    private javax.swing.JTextField jtfBizPartner_Ro;
    private javax.swing.JTextField jtfFacDateBenefits;
    private javax.swing.JTextField jtfFacDateCutoff;
    private javax.swing.JTextField jtfFacEmpName;
    private javax.swing.JTextField jtfFacEmpNumber;
    private javax.swing.JTextField jtfFacPaymentType;
    private javax.swing.JTextField jtfFacSeniorityDays;
    private javax.swing.JTextField jtfFacSeniorityProp;
    private javax.swing.JTextField jtfFacSeniorityYears;
    private javax.swing.JTextField jtfFirstname;
    private javax.swing.JTextField jtfFiscalId;
    private javax.swing.JTextField jtfGroceryServiceAccount;
    private javax.swing.JTextField jtfImagePhoto;
    private javax.swing.JTextField jtfImageSignature;
    private javax.swing.JTextField jtfLastname1;
    private javax.swing.JTextField jtfLastname2;
    private javax.swing.JTextField jtfMailCompany;
    private javax.swing.JTextField jtfMailOwn;
    private javax.swing.JTextField jtfMateAge;
    private javax.swing.JTextField jtfMateName;
    private javax.swing.JTextField jtfPhone;
    private javax.swing.JTextField jtfPkBizPartnerId_Ro;
    private javax.swing.JTextField jtfSalary;
    private javax.swing.JTextField jtfSalarySscBase;
    private javax.swing.JTextField jtfSeniority;
    private javax.swing.JTextField jtfSocialSecurityNumber;
    private javax.swing.JTextField jtfSonAge1;
    private javax.swing.JTextField jtfSonAge2;
    private javax.swing.JTextField jtfSonAge3;
    private javax.swing.JTextField jtfSonAge4;
    private javax.swing.JTextField jtfSonAge5;
    private javax.swing.JTextField jtfSonName1;
    private javax.swing.JTextField jtfSonName2;
    private javax.swing.JTextField jtfSonName3;
    private javax.swing.JTextField jtfSonName4;
    private javax.swing.JTextField jtfSonName5;
    private javax.swing.JTextField jtfUmf;
    private javax.swing.JTextField jtfVacBonLastUpdate;
    private javax.swing.JTextField jtfVacBonLastUpdater;
    private javax.swing.JTextField jtfVacBonPaymentType;
    private javax.swing.JTextField jtfVacBonPeriod;
    private javax.swing.JTextField jtfVacBonUnionized;
    private javax.swing.JTextField jtfVacLastUpdate;
    private javax.swing.JTextField jtfVacLastUpdater;
    private javax.swing.JTextField jtfVacPaymentType;
    private javax.swing.JTextField jtfVacPeriod;
    private javax.swing.JTextField jtfVacUnionized;
    private javax.swing.JTextField jtfWage;
    private javax.swing.JTextField jtfWorkingHoursDay;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {

    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;
        mbUpdatingForm = true;
        mbPhotoChange = false;
        mbSignatureChange = false;
        moXtaImageIconPhoto_n = null;
        jlImgPhoto.setIcon(null);
        jlImgPhoto.setText(SPanelQueryIntegralEmployee.NO_PHOTO);
        moXtaImageIconSignature_n = null;
        jlImgSignature.setIcon(null);
        jlImgSignature.setText(SPanelQueryIntegralEmployee.NO_SIGNATURE);

        moBizPartner = null;
        moBizPartnerBranch = null;
        moEmployee = null;
        moEmployeeBenefitTables = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        computeBizPartner_Ro();

        moFieldIsActive.setFieldValue(true);
        moFieldDateBenefits.setFieldValue(miClient.getSessionXXX().getWorkingDate());
        moFieldDateLastHire.setFieldValue(miClient.getSessionXXX().getWorkingDate());
        moFieldWorkingHoursDay.setFieldValue(SHrsConsts.WORKING_DAY_HOURS);
        moPanelBizPartnerBranchAddress.formReset();
        moPanelBizPartnerBranchAddress.setParamIsInMainWindow(true);
        moPanelBizPartnerBranchAddress.setFieldsEnabled(true);
        jradOvertimeNever.setSelected(true);
        jradCheckerNever.setSelected(true);
        jTabbedPane.setSelectedIndex(0);

        mnPkContactId = 0;
        jckIsDeleted.setEnabled(false);
        jckChangeSalary.setSelected(false);
        jckChangeWage.setSelected(false);
        jckChangeSalarySscBase.setSelected(false);
        updateStatusDateBenefits(true);
        updateStatusDateLastHire(true);
        updateEmployeeNextNumber();

        itemStateChangePaymentType();
        //itemStateSalary();    // invocked allready by itemStateChangePaymentType()
        //itemStateWage();      // invocked allready by itemStateChangePaymentType()
        itemStateSalarySscBaseChange(false);
        
        jtfSeniority.setText("");
        jtfAge.setText("");
        jtfMateAge.setText("");
        jtfSonAge1.setText("");
        jtfSonAge2.setText("");
        jtfSonAge3.setText("");
        jtfSonAge4.setText("");
        jtfSonAge5.setText("");
        
        int[] na = new int[] { SModSysConsts.HRSS_CL_HRS_CAT_SEX, 1 };  // non applying
        moFieldMateSex.setFieldValue(na);
        moFieldSonSex1.setFieldValue(na);
        moFieldSonSex2.setFieldValue(na);
        moFieldSonSex3.setFieldValue(na);
        moFieldSonSex4.setFieldValue(na);
        moFieldSonSex5.setFieldValue(na);

        mbUpdatingForm = false;
        jbImagePhotoView.setEnabled(false);
        jbImagePhotoRemove.setEnabled(false);
        jbImageSignatureView.setEnabled(false);
        jbImageSignatureRemove.setEnabled(false);
        
        actionBenCancel(SModSysConsts.HRSS_TP_BEN_VAC, false, false);
        actionBenCancel(SModSysConsts.HRSS_TP_BEN_VAC_BON, false, false);
        actionBenCancel(SModSysConsts.HRSS_TP_BEN_ANN_BON, false, false);
        
        moBenefitsVacPane.createTable(null);
        moBenefitsVacBonPane.createTable(null);
        moBenefitsAnnBonPane.createTable(null);
        moWageFactorsPane.createTable(null);
        
        renderBenefitsAndWageFactors();
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbFkBank_n, SModConsts.HRSS_BANK);
        SFormUtilities.populateComboBox(miClient, jcbFkGroceryService, SModConsts.HRSS_GROCERY_SRV);
        SFormUtilities.populateComboBox(miClient, jcbFkPaymentType, SModConsts.HRSS_TP_PAY);
        SFormUtilities.populateComboBox(miClient, jcbFkSalaryType, SModConsts.HRSS_TP_SAL);
        SFormUtilities.populateComboBox(miClient, jcbFkEmployeeType, SModConsts.HRSU_TP_EMP);
        SFormUtilities.populateComboBox(miClient, jcbFkWorkerType, SModConsts.HRSU_TP_WRK);
        SFormUtilities.populateComboBox(miClient, jcbFkDepartment, SModConsts.HRSU_DEP);
        //SFormUtilities.populateComboBox(miClient, jcbFkPosition, SModConsts.HRSU_POS); // will be populated on the fly
        SFormUtilities.populateComboBox(miClient, jcbFkShift, SModConsts.HRSU_SHT);
        SFormUtilities.populateComboBox(miClient, jcbFkWorkingDayType, SModConsts.HRSS_TP_WORK_DAY);
        SFormUtilities.populateComboBox(miClient, jcbFkContractType, SModConsts.HRSS_TP_CON);
        SFormUtilities.populateComboBox(miClient, jcbFkRecruitmentSchemaType, SModConsts.HRSS_TP_REC_SCHE);
        SFormUtilities.populateComboBox(miClient, jcbFkPositionRiskType, SModConsts.HRSS_TP_POS_RISK);
        SFormUtilities.populateComboBox(miClient, jcbFkMwzType, SModConsts.HRSU_TP_MWZ);
        SFormUtilities.populateComboBox(miClient, jcbFkCatalogueSexTypeId, SModConsts.HRSS_TP_HRS_CAT, SModSysConsts.HRSS_CL_HRS_CAT_SEX);
        SFormUtilities.populateComboBox(miClient, jcbFkCatalogueBloodTypeTypeId, SModConsts.HRSS_TP_HRS_CAT, SModSysConsts.HRSS_CL_HRS_CAT_BLO);
        SFormUtilities.populateComboBox(miClient, jcbFkCatalogueEducationTypeId, SModConsts.HRSS_TP_HRS_CAT, SModSysConsts.HRSS_CL_HRS_CAT_EDU);
        SFormUtilities.populateComboBox(miClient, jcbFkCatalogueMaritalStatusTypeId, SModConsts.HRSS_TP_HRS_CAT, SModSysConsts.HRSS_CL_HRS_CAT_MAR);
        SFormUtilities.populateComboBox(miClient, jcbFkMateCatalogueSexTypeId, SModConsts.HRSS_TP_HRS_CAT, SModSysConsts.HRSS_CL_HRS_CAT_SEX);
        SFormUtilities.populateComboBox(miClient, jcbFkSonCatalogueSexTypeId1, SModConsts.HRSS_TP_HRS_CAT, SModSysConsts.HRSS_CL_HRS_CAT_SEX);
        SFormUtilities.populateComboBox(miClient, jcbFkSonCatalogueSexTypeId2, SModConsts.HRSS_TP_HRS_CAT, SModSysConsts.HRSS_CL_HRS_CAT_SEX);
        SFormUtilities.populateComboBox(miClient, jcbFkSonCatalogueSexTypeId3, SModConsts.HRSS_TP_HRS_CAT, SModSysConsts.HRSS_CL_HRS_CAT_SEX);
        SFormUtilities.populateComboBox(miClient, jcbFkSonCatalogueSexTypeId4, SModConsts.HRSS_TP_HRS_CAT, SModSysConsts.HRSS_CL_HRS_CAT_SEX);
        SFormUtilities.populateComboBox(miClient, jcbFkSonCatalogueSexTypeId5, SModConsts.HRSS_TP_HRS_CAT, SModSysConsts.HRSS_CL_HRS_CAT_SEX);
        SFormUtilities.populateComboBox(miClient, jcbVacTable, SModConsts.HRS_BEN, SModSysConsts.HRSS_TP_BEN_VAC);
        SFormUtilities.populateComboBox(miClient, jcbVacBonTable, SModConsts.HRS_BEN, SModSysConsts.HRSS_TP_BEN_VAC_BON);
        SFormUtilities.populateComboBox(miClient, jcbAnnBonTable, SModConsts.HRS_BEN, SModSysConsts.HRSS_TP_BEN_ANN_BON);
        moPanelBizPartnerBranchAddress.formRefreshCatalogues();
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                validation.setTabbedPaneIndex(((erp.lib.form.SFormField) mvFields.get(i)).getTabbedPaneIndex());
                break;
            }
        }
        
        if (!validation.getIsError()) {
            validation = validateNumber();
        }
        
        if (!validation.getIsError()) {
            String name = composeName(); // convenience variable
            Object[] valParams = new Object[] { moBizPartner == null ? SLibConsts.UNDEFINED : moBizPartner.getPkBizPartnerId(), name };
            int valCount = SDataUtilities.callProcedureVal(miClient, SProcConstants.BPSU_BP, valParams, SLibConstants.EXEC_MODE_VERBOSE);

            if (valCount > 0) {
                if (miClient.showMsgBoxConfirm("El valor del campo '" + jtfBizPartner_Ro.getToolTipText() + "', '" + name + "',"
                        + "\nya existe " + valCount + " " + (valCount == 1 ? "vez" : "veces") + " en el sistema, ¿desea conservarlo?") != JOptionPane.YES_OPTION) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + jtfBizPartner_Ro.getToolTipText() + "'.");
                    validation.setComponent(jtfFirstname);
                    validation.setTabbedPaneIndex(TAB_DATA_EMP);
                }
            }

            if (!validation.getIsError()) {
                valParams = new Object[] { moBizPartner == null ? SLibConsts.UNDEFINED : moBizPartner.getPkBizPartnerId(), moFieldFiscalId.getString() };
                valCount = SDataUtilities.callProcedureVal(miClient, SProcConstants.BPSU_BP_FISCAL_ID, valParams, SLibConstants.EXEC_MODE_VERBOSE);
                
                if (valCount > 0) {
                    if (miClient.showMsgBoxConfirm("El valor del campo '" + jlFiscalId.getText() + "', '" + moFieldFiscalId.getString() + "',"
                            + "\nya existe " + valCount + " " + (valCount == 1 ? "vez" : "veces") + " en el sistema, ¿desea conservarlo?") != JOptionPane.YES_OPTION) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + jlFiscalId.getText() + "'.");
                        validation.setComponent(jtfFiscalId);
                        validation.setTabbedPaneIndex(TAB_DATA_EMP);
                    }
                }
            }
        }

        if (!validation.getIsError()) {
            String fiscalId = jtfFiscalId.getText().trim();
            
            if (!moFieldBankAccount.getString().isEmpty() && jcbFkBank_n.getSelectedIndex() <= 0) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + jlFkBank_n.getText() + "'.");
                validation.setComponent(jcbFkBank_n);
                validation.setTabbedPaneIndex(TAB_DATA_EMP);
            }
            else if (!moFieldGroceryServiceAccount.getString().isEmpty() && jcbFkGroceryService.getSelectedIndex() <= 0) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + jlFkGroceryService.getText() + "'.");
                validation.setComponent(jcbFkGroceryService);
                validation.setTabbedPaneIndex(TAB_DATA_EMP);
            }
            else if (fiscalId.isEmpty()) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFiscalId.getText() + "'.");
                validation.setComponent(jtfFiscalId);
                validation.setTabbedPaneIndex(TAB_DATA_EMP);
            }
            else if (fiscalId.equals(DCfdConsts.RFC_GEN_NAC) || fiscalId.equals(DCfdConsts.RFC_GEN_INT)) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFiscalId.getText() + "'.");
                validation.setComponent(jtfFiscalId);
                validation.setTabbedPaneIndex(TAB_DATA_EMP);
            }
            else if (fiscalId.length() != DCfdConsts.LEN_RFC_PER) {
                validation.setMessage("El valor del campo '" + jlFiscalId.getText() + "', '" + fiscalId + "', debe tener " + DCfdConsts.LEN_RFC_PER + " caracteres.");
                validation.setComponent(jtfFiscalId);
                validation.setTabbedPaneIndex(TAB_DATA_EMP);
            }
            else if (moFieldFkRecruitmentSchemaType.getKeyAsIntArray()[0] == SModSysConsts.HRSS_TP_REC_SCHE_NA) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFkRecruitmentSchemaType.getText() + "'.");
                validation.setComponent(jcbFkRecruitmentSchemaType);
                validation.setTabbedPaneIndex(TAB_DATA_EMP);
            }
            else {
                if (!SLibUtilities.belongsTo(moFieldFkRecruitmentSchemaType.getKeyAsIntArray()[0],
                        new int[] { SModSysConsts.HRSS_TP_REC_SCHE_ASS_COO, SModSysConsts.HRSS_TP_REC_SCHE_ASS_CIV, SModSysConsts.HRSS_TP_REC_SCHE_ASS_BRD, 
                            SModSysConsts.HRSS_TP_REC_SCHE_ASS_SAL, SModSysConsts.HRSS_TP_REC_SCHE_ASS_PRO, SModSysConsts.HRSS_TP_REC_SCHE_ASS_SHA, SModSysConsts.HRSS_TP_REC_SCHE_ASS_OTH })) {
                    if (moFieldSocialSecurityNumber.getString().isEmpty()) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + jlSocialSecurityNumber.getText() + "'.");
                        validation.setComponent(jtfSocialSecurityNumber);                                
                        validation.setTabbedPaneIndex(TAB_DATA_EMP);
                    }
                    else if (moFieldSalarySscBase.getDouble() == 0) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + jlSalarySscBase.getText() + "'.");
                        validation.setComponent(jtfSalarySscBase);                                
                        validation.setTabbedPaneIndex(TAB_DATA_EMP);
                    }
                    else if (moFieldDateChangeSalarySscBase.getDate() == null) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + jlSalarySscBase.getText() + "' (fecha).");
                        validation.setComponent(jftDateChangeSalarySscBase);                                
                        validation.setTabbedPaneIndex(TAB_DATA_EMP);
                    }
                }

                if (!validation.getIsError()) {
                    if (!moFieldMailOwn.getString().isEmpty()) {
                        String message = SLibUtilities.validateEmail(moFieldMailOwn.getString());
                        if (!message.isEmpty()) {
                            validation.setMessage(message);
                            validation.setComponent(jtfMailOwn);
                            validation.setTabbedPaneIndex(TAB_DATA_EMP);
                        }
                    }

                    if (!validation.getIsError()) {
                        if (!moFieldMailCompany.getString().isEmpty()) {
                            String message = SLibUtilities.validateEmail(moFieldMailCompany.getString());
                            if (!message.isEmpty()) {
                                validation.setMessage(message);
                                validation.setComponent(jtfMailCompany);
                                validation.setTabbedPaneIndex(TAB_DATA_EMP);
                            }
                        }
                        
                        if (!validation.getIsError()) {
                            if (SHrsEmployeeUtils.isContractExpirationRequired(moFieldFkContractType.getKeyAsIntArray()[0]) && moFieldContractExpiration.getDate() == null) {
                                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + jlContractExpiration.getText() + "'.");
                                validation.setComponent(jftContractExpiration);
                                validation.setTabbedPaneIndex(TAB_DATA_EMP);
                            }
                        }
                    }
                }
            }
        }

        if (!validation.getIsError()) {
            validation = moPanelBizPartnerBranchAddress.formValidate();
            validation.setTabbedPaneIndex(TAB_DATA_PER);
        }

        if (!validation.getIsError()) {
            // Validate information of employeee's relatives:

            ArrayList<JLabel> relatives = new ArrayList<>();
            relatives.add(jlMate);
            relatives.add(jlSon1);
            relatives.add(jlSon2);
            relatives.add(jlSon3);
            relatives.add(jlSon4);
            relatives.add(jlSon5);

            ArrayList<SFormField> names = new ArrayList<>();
            names.add(moFieldMateName);
            names.add(moFieldSonName1);
            names.add(moFieldSonName2);
            names.add(moFieldSonName3);
            names.add(moFieldSonName4);
            names.add(moFieldSonName5);

            ArrayList<SFormField> dates = new ArrayList<>();
            dates.add(moFieldMateDateBirth);
            dates.add(moFieldSonDateBirth1);
            dates.add(moFieldSonDateBirth2);
            dates.add(moFieldSonDateBirth3);
            dates.add(moFieldSonDateBirth4);
            dates.add(moFieldSonDateBirth5);

            ArrayList<JComboBox> sexes = new ArrayList<>();
            sexes.add(jcbFkMateCatalogueSexTypeId);
            sexes.add(jcbFkSonCatalogueSexTypeId1);
            sexes.add(jcbFkSonCatalogueSexTypeId2);
            sexes.add(jcbFkSonCatalogueSexTypeId3);
            sexes.add(jcbFkSonCatalogueSexTypeId4);
            sexes.add(jcbFkSonCatalogueSexTypeId5);

            ArrayList<SFormField> deceaseds = new ArrayList<>();
            deceaseds.add(moFieldMateDeceased);
            deceaseds.add(moFieldSonDeceased1);
            deceaseds.add(moFieldSonDeceased2);
            deceaseds.add(moFieldSonDeceased3);
            deceaseds.add(moFieldSonDeceased4);
            deceaseds.add(moFieldSonDeceased5);

            for (int field = 0; field < 6; field++) {
                if (!names.get(field).getString().isEmpty()) {
                    if (dates.get(field).getDate() == null) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlRelativeDateBirth) + "' " + SGuiUtils.getLabelName(relatives.get(field)) + ".");
                        validation.setComponent(dates.get(field).getComponent());
                        validation.setTabbedPaneIndex(TAB_DATA_PER);
                        break;
                    }
                }
                if (dates.get(field).getDate() != null) {
                    if (names.get(field).getString().isEmpty()) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlRelativeName) + "' " + SGuiUtils.getLabelName(relatives.get(field)) + ".");
                        validation.setComponent(names.get(field).getComponent());
                        validation.setTabbedPaneIndex(TAB_DATA_PER);
                        break;
                    }
                }
                if (sexes.get(field).getSelectedIndex() > 1) {  // option '(N/A)' is index 1
                    if (names.get(field).getString().isEmpty()) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlRelativeName) + "' " + SGuiUtils.getLabelName(relatives.get(field)) + ".");
                        validation.setComponent(names.get(field).getComponent());
                        validation.setTabbedPaneIndex(TAB_DATA_PER);
                        break;
                    }
                }
                if (deceaseds.get(field).getBoolean()) {
                    if (names.get(field).getString().isEmpty()) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlRelativeName) + "' " + SGuiUtils.getLabelName(relatives.get(field)) + ".");
                        validation.setComponent(names.get(field).getComponent());
                        validation.setTabbedPaneIndex(TAB_DATA_PER);
                        break;
                    }
                }
            }
        }

        if (!validation.getIsError()) {
            String sDateRfc = moFieldFiscalId.getString().substring(4, 10);
            int nYearRfc = Integer.parseInt(sDateRfc.substring(0, 2)) + SHrsConsts.YEAR_MAX_BIRTH > SLibTimeUtils.digestYear(miClient.getSessionXXX().getSystemDate())[0] ? 
                    Integer.parseInt(sDateRfc.substring(0, 2)) + SHrsConsts.YEAR_MIN_BIRTH : Integer.parseInt(sDateRfc.substring(0, 2)) + SHrsConsts.YEAR_MAX_BIRTH;

            Date dateRfc = SLibTimeUtils.createDate(nYearRfc, Integer.parseInt(sDateRfc.substring(2, 4)), Integer.parseInt(sDateRfc.substring(4, 6)));

            if (!SLibTimeUtils.isSameDate(dateRfc, moFieldDateBirth.getDate())) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + SGuiUtils.getLabelName(jlDateBirth) + "':\n"
                        + "la fecha de este campo, " + SLibUtils.DateFormatDate.format(moFieldDateBirth.getDate()) + ", "
                        + "no coincide con la fecha implícita del campo '" + SGuiUtils.getLabelName(jlFiscalId).toUpperCase() + "', " + SLibUtils.DateFormatDate.format(dateRfc) + ", correspondiente al valor '" + moFieldFiscalId.getString() + "'.");
                validation.setComponent(jftDateBirth);
                validation.setTabbedPaneIndex(TAB_DATA_PER);
            }
        }
        
        if (!validation.getIsError()) {
            if (jbVacUpdate.isEnabled() || jbVacBonUpdate.isEnabled() || jbAnnBonUpdate.isEnabled()) {
                validation.setMessage("Hay capturas pendientes de completar de prestaciones laborales.");
                validation.setTabbedPaneIndex(TAB_BENEFITS);
            }
            else if (moEmployeeBenefitTables == null) {
                validation.setMessage("No se han definido las prestaciones laborales.");
                validation.setTabbedPaneIndex(TAB_BENEFITS);
            }
            else {
                if (!moEmployeeBenefitTables.isBenefitSet(SModSysConsts.HRSS_TP_BEN_VAC)) {
                    validation.setMessage("No se han definido las prestaciones laborales '" + ((TitledBorder) jpBenefitsVac.getBorder()).getTitle() + "'.");
                    validation.setComponent(jbVacModify);
                    validation.setTabbedPaneIndex(TAB_BENEFITS);
                }
                else if (!moEmployeeBenefitTables.isBenefitSet(SModSysConsts.HRSS_TP_BEN_VAC_BON)) {
                    validation.setMessage("No se han definido las prestaciones laborales '" + ((TitledBorder) jpBenefitsVacBon.getBorder()).getTitle() + "'.");
                    validation.setComponent(jbVacBonModify);
                    validation.setTabbedPaneIndex(TAB_BENEFITS);
                }
                else if (!moEmployeeBenefitTables.isBenefitSet(SModSysConsts.HRSS_TP_BEN_ANN_BON)) {
                    validation.setMessage("No se han definido las prestaciones laborales '" + ((TitledBorder) jpBenefitsAnnBon.getBorder()).getTitle() + "'.");
                    validation.setComponent(jbAnnBonModify);
                    validation.setTabbedPaneIndex(TAB_BENEFITS);
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
        mbUpdatingForm = true;

        moBizPartner = (SDataBizPartner) registry;
        moEmployee = moBizPartner.getDbmsDataEmployee();

        moFieldFiscalId.setFieldValue(moBizPartner.getFiscalId());
        moFieldAlternativeId.setFieldValue(moBizPartner.getAlternativeId());
        moFieldIsDeleted.setFieldValue(moBizPartner.getIsDeleted());

        jtfPkBizPartnerId_Ro.setText("" + moBizPartner.getPkBizPartnerId());

        moFieldLastname1.setFieldValue(moEmployee.getLastname1());
        moFieldLastname2.setFieldValue(moEmployee.getLastname2());
        moFieldFirstname.setFieldValue(moBizPartner.getFirstname());
        computeBizPartner_Ro();

        moFieldNumber.setFieldValue(moEmployee.getNumber());
        moFieldSocialSecurityNumber.setFieldValue(moEmployee.getSocialSecurityNumber());
        moXtaImageIconPhoto_n = moEmployee.getXtaImageIconPhoto_n();
        moXtaImageIconSignature_n = moEmployee.getXtaImageIconSignature_n();
        moFieldFileImagePhoto.setFieldValue("");
        moFieldFileImageSignature.setFieldValue("");
        moFieldFkCatalogueSexType.setFieldValue(new int[] { moEmployee.getFkCatalogueSexClassId(), moEmployee.getFkCatalogueSexTypeId() });
        moFieldPlaceOfBirth.setFieldValue(moEmployee.getPlaceOfBirth());
        moFieldUmf.setFieldValue(moEmployee.getUmf());
        moFieldFkCatalogueBloodTypeType.setFieldValue(new int[] { moEmployee.getFkCatalogueBloodTypeClassId(), moEmployee.getFkCatalogueBloodTypeTypeId() });
        moFieldFkCatalogueEducationType.setFieldValue(new int[] { moEmployee.getFkCatalogueEducationClassId(), moEmployee.getFkCatalogueEducationTypeId() });
        moFieldFkCatalogueMaritalStatusType.setFieldValue(new int[] { moEmployee.getFkCatalogueMaritalStatusClassId(), moEmployee.getFkCatalogueMaritalStatusTypeId() });
        moFieldFkBank_n.setFieldValue(new int[] { moEmployee.getFkBankId_n() });
        moFieldBankAccount.setFieldValue(moEmployee.getBankAccount());
        moFieldFkGroceryService.setFieldValue(new int[] { moEmployee.getFkGroceryServiceId() });
        moFieldGroceryServiceAccount.setFieldValue(moEmployee.getGroceryServiceAccount());
        moFieldIsActive.setFieldValue(moEmployee.isActive());
        moFieldDateBirth.setFieldValue(moEmployee.getDateBirth());
        focusLostDateBirth();
        moFieldDateBenefits.setFieldValue(moEmployee.getDateBenefits());
        focusLostDateBenefits();
        moFieldDateLastHire.setFieldValue(moEmployee.getDateLastHire());
        moFieldDateLastDismissal_n.setFieldValue(moEmployee.getDateLastDismissal_n());
        moFieldFkPaymentType.setFieldValue(new int[] { moEmployee.getFkPaymentTypeId() });
        itemStateChangePaymentType();
        moFieldFkSalaryType.setFieldValue(new int[] { moEmployee.getFkSalaryTypeId() });

        moFieldSalary.setFieldValue(moEmployee.getSalary());
        moFieldDateChangeSalary.setFieldValue(moEmployee.getDateSalary());
        moFieldWage.setFieldValue(moEmployee.getWage());
        moFieldDateChangeWage.setFieldValue(moEmployee.getDateWage());
        moFieldSalarySscBase.setFieldValue(moEmployee.getSalarySscBase());
        moFieldDateChangeSalarySscBase.setFieldValue(moEmployee.getDateSalarySscBase());
        moFieldFkMwzType.setFieldValue(new int[] { moEmployee.getFkMwzTypeId() });

        moFieldFkEmployeeType.setFieldValue(new int[] { moEmployee.getFkEmployeeTypeId() });
        moFieldFkWorkerType.setFieldValue(new int[] { moEmployee.getFkWorkerTypeId() });
        moFieldIsUnionized.setFieldValue(moEmployee.isUnionized());
        moFieldIsMfgOperator.setFieldValue(moEmployee.isMfgOperator());
        moFieldFkDepartment.setFieldValue(new int[] { moEmployee.getFkDepartmentId() });
        itemStateChangedDepartament();
        moFieldFkPosition.setFieldValue(new int[] { moEmployee.getFkPositionId() });
        moFieldFkShift.setFieldValue(new int[] { moEmployee.getFkShiftId() });
        moFieldFkWorkingDayType.setFieldValue(new int[] { moEmployee.getFkWorkingDayTypeId()});

        moFieldWorkingHoursDay.setFieldValue(moEmployee.getWorkingHoursDay());
        switch (moEmployee.getOvertimePolicy()) {
            case SHrsConsts.OVERTIME_NEVER:
                jradOvertimeNever.setSelected(true);
                break;
            case SHrsConsts.OVERTIME_ALLWAYS:
                jradOvertimeAllways.setSelected(true);
                break;
            case SHrsConsts.OVERTIME_SOMETIMES:
                jradOvertimeSometimes.setSelected(true);
                break;
            default:
        }

        switch (moEmployee.getCheckerPolicy()) {
            case SHrsConsts.CHECKER_POLICY_ESTRICT:
                jradCheckerAllways.setSelected(true);
                break;
            case SHrsConsts.CHECKER_POLICY_FREE:
                jradCheckerNever.setSelected(true);
                break;
            case SHrsConsts.CHECKER_POLICY_EVENT:
                jradCheckerSometimes.setSelected(true);
                break;
            default:
        }

        moFieldFkContractType.setFieldValue(new int[] { moEmployee.getFkContractTypeId() });
        itemStateChangedContractType();
        moFieldContractExpiration.setFieldValue(moEmployee.getContractExpiration_n());
        moFieldFkRecruitmentSchemaType.setFieldValue(new int[] { moEmployee.getFkRecruitmentSchemaTypeId() });
        moFieldFkPositionRiskType.setFieldValue(new int[] { moEmployee.getFkPositionRiskTypeId() });

        if (moEmployee.getXtaImageIconPhoto_n() != null) {
            ImageIcon photo = moEmployee.getXtaImageIconPhoto_n();
            if (photo.getIconHeight() > 300) {
                photo = new ImageIcon(photo.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
            }
            jlImgPhoto.setIcon(photo);
            jlImgPhoto.setText("");
        }
        else {
            jlImgPhoto.setIcon(null);
            jlImgPhoto.setText(SPanelQueryIntegralEmployee.NO_PHOTO);
        }

        if (moEmployee.getXtaImageIconSignature_n() != null) {
            jlImgSignature.setIcon(moEmployee.getXtaImageIconSignature_n());
            jlImgSignature.setText("");
        }
        else {
            jlImgSignature.setIcon(null);
            jlImgSignature.setText(SPanelQueryIntegralEmployee.NO_SIGNATURE);
        }
        
        moBizPartnerBranch = moBizPartner.getDbmsBizPartnerBranchHq();
        moPanelBizPartnerBranchAddress.setRegistry(moBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial());

        if (!moBizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchContacts().isEmpty()) {
            SDataBizPartnerBranchContact contact = moBizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchContacts().get(0);
            
            mnPkContactId = contact.getPkContactId();
            moFieldMailOwn.setFieldValue(contact.getEmail01());
            moFieldMailCompany.setFieldValue(contact.getEmail02());
            moFieldPhone.setFieldValue(contact.getTelNumber01());
        }
        
        jckChangeSalary.setSelected(false);
        jckChangeWage.setSelected(false);
        jckChangeSalarySscBase.setSelected(moFieldSalarySscBase.getDouble() == 0);
        updateStatusDateBenefits(false);
        updateStatusDateLastHire(false);

        itemStateChangePaymentType();
        //itemStateSalary();    // invocked allready by itemStateChangePaymentType()
        //itemStateWage();      // invocked allready by itemStateChangePaymentType()
        itemStateSalarySscBaseChange(false);

        mbUpdatingForm = false;
        enableButtonsPhoto(moXtaImageIconPhoto_n != null);
        enableButtonsSignature(moXtaImageIconSignature_n != null);
        
        if (moEmployee.getChildRelatives() != null) {
            SDataEmployeeRelatives relatives = moEmployee.getChildRelatives();
            moFieldMateName.setFieldValue(relatives.getMate());
            moFieldMateDateBirth.setFieldValue(relatives.getMateDateBirth());
            focusLostMateDateBirth();
            moFieldMateSex.setFieldValue(new int[] { relatives.getFkCatSexClassIdMate(), relatives.getFkCatSexTypeIdMate() });
            moFieldMateDeceased.setFieldValue(relatives.isMateDeceased());
            moFieldSonName1.setFieldValue(relatives.getSon1());
            moFieldSonDateBirth1.setFieldValue(relatives.getSonDateBirth1());
            focusLostSonDateBirth1();
            moFieldSonSex1.setFieldValue(new int[] { relatives.getFkCatSexClassIdSon1(), relatives.getFkCatSexTypeIdSon1() });
            moFieldSonDeceased1.setFieldValue(relatives.isSonDeceased1());
            moFieldSonName2.setFieldValue(relatives.getSon2());
            moFieldSonDateBirth2.setFieldValue(relatives.getSonDateBirth2());
            focusLostSonDateBirth2();
            moFieldSonSex2.setFieldValue(new int[] { relatives.getFkCatSexClassIdSon2(), relatives.getFkCatSexTypeIdSon2() });
            moFieldSonDeceased2.setFieldValue(relatives.isSonDeceased2());
            moFieldSonName3.setFieldValue(relatives.getSon3());
            moFieldSonDateBirth3.setFieldValue(relatives.getSonDateBirth3());
            focusLostSonDateBirth3();
            moFieldSonSex3.setFieldValue(new int[] { relatives.getFkCatSexClassIdSon3(), relatives.getFkCatSexTypeIdSon3() });
            moFieldSonDeceased3.setFieldValue(relatives.isSonDeceased3());
            moFieldSonName4.setFieldValue(relatives.getSon4());
            moFieldSonDateBirth4.setFieldValue(relatives.getSonDateBirth4());
            focusLostSonDateBirth4();
            moFieldSonSex4.setFieldValue(new int[] { relatives.getFkCatSexClassIdSon4(), relatives.getFkCatSexTypeIdSon4() });
            moFieldSonDeceased4.setFieldValue(relatives.isSonDeceased4());
            moFieldSonName5.setFieldValue(relatives.getSon5());
            moFieldSonDateBirth5.setFieldValue(relatives.getSonDateBirth5());
            focusLostSonDateBirth5();
            moFieldSonSex5.setFieldValue(new int[] { relatives.getFkCatSexClassIdSon5(), relatives.getFkCatSexTypeIdSon5() });
            moFieldSonDeceased5.setFieldValue(relatives.isSonDeceased5());
        }
        
        // Benefit tables of employee:
        
        try {
            moEmployeeBenefitTables = SHrsBenefitUtils.getCurrentBenefitTables(((SGuiClient) miClient).getSession(), moEmployee.getPkEmployeeId());
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
        }
        
        if (moEmployeeBenefitTables == null) {
            miClient.showMsgBoxWarning("El empleado no cuenta aún con su registro de prestaciones.");
        }
        renderBenefitsAndWageFactors();
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        int paymentType = moFieldFkPaymentType.getKeyAsIntArray()[0];
        HashSet<Integer> requiredCategories = new HashSet<>();

        if (moBizPartner == null) {
            moBizPartner = new SDataBizPartner();
            moBizPartner.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

            moBizPartner.setFkBizPartnerIdentityTypeId(SModSysConsts.BPSS_TP_BP_IDY_PER);
            moBizPartner.setFkTaxIdentityId(SModSysConsts.BPSS_TP_BP_IDY_PER);
            moBizPartner.setFkBizAreaId(SDataConstantsSys.BPSU_BA_DEFAULT);
            moBizPartner.setFkFiscalBankId(SModSysConsts.FINS_FISCAL_BANK_NA);
        }
        else {
            moBizPartner.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        //moBizPartner.setPkBizPartnerId(...);
        moBizPartner.setFiscalId(moFieldFiscalId.getString());
        //moBizPartner.setFiscalFrgId(...);
        moBizPartner.setAlternativeId(moFieldAlternativeId.getString());
        //moBizPartner.setCodeBankSantander(...);
        //moBizPartner.setCodeBankBanBajio(...);
        //moBizPartner.setWeb(...);

        //moBizPartner.setIsCompany(...);
        //moBizPartner.setIsSupplier(...);
        //moBizPartner.setIsCustomer(...);

        requiredCategories.add(SDataConstantsSys.BPSS_CT_BP_CDR);
        requiredCategories.add(SDataConstantsSys.BPSS_CT_BP_DBR);

        for (Integer category : requiredCategories) {
            switch (category) {
                case SModSysConsts.BPSS_CT_BP_CDR:
                    if (!moBizPartner.getIsCreditor()) {
                        moBizPartner.setIsCreditor(true);
                        moBizPartner.setDbmsCategorySettingsCdr(createBizPartnerCategory(category));
                    }
                    else {
                        moBizPartner.getDbmsCategorySettingsCdr().setIsDeleted(false);
                        moBizPartner.getDbmsCategorySettingsCdr().setIsRegistryEdited(true);
                    }
                    break;
                    
                case SModSysConsts.BPSS_CT_BP_DBR:
                    if (!moBizPartner.getIsDebtor()) {
                        moBizPartner.setIsDebtor(true);
                        moBizPartner.setDbmsCategorySettingsDbr(createBizPartnerCategory(category));
                    }
                    else {
                        moBizPartner.getDbmsCategorySettingsDbr().setIsDeleted(false);
                        moBizPartner.getDbmsCategorySettingsDbr().setIsRegistryEdited(true);
                    }
                    break;
                    
                default:
                    // do nothing
            }
        }

        //moBizPartner.setIsAttributeBank(...);
        //moBizPartner.setIsAttributeCarrier(...);
        moBizPartner.setIsAttributeEmployee(!jckIsDeleted.isSelected());
        //moBizPartner.setIsAttributeSalesAgent(...);
        //moBizPartner.setIsAttributePartnerShareholder(...);
        //moBizPartner.setIsAttributeRelatedParty(...);
        if (!jckIsDeleted.isSelected()) {
            moBizPartner.setIsDeleted(false);
        }

        // Update business partner children data:

        updateBizPartnerBranch();
        
        // Update employee:

        moEmployee = moBizPartner.getDbmsDataEmployee();

        if (moEmployee == null) {
            moEmployee = new SDataEmployee();
            moEmployee.setFkSourceCompanyId(miClient.getSessionXXX().getCurrentCompany().getPkCompanyId()); // set source company
            moEmployee.setFkUserInsertId(miClient.getSession().getUser().getPkUserId());

            moBizPartner.setDbmsDataEmployee(moEmployee);
        }
        else {
            //moEmployee.setFkSourceCompanyId(...); // source company only set when employee is created (this use case) or transferred to another company (use case addressed in another functionality)
            moEmployee.setFkUserUpdateId(miClient.getSession().getUser().getPkUserId());
        }

        //moEmployee.setPkEmployeeId(...);
        moEmployee.setNumber(moFieldNumber.getString());
        moEmployee.setLastname1(moFieldLastname1.getString());
        moEmployee.setLastname2(moFieldLastname2.getString());
        moEmployee.setSocialSecurityNumber(moFieldSocialSecurityNumber.getString());
        moEmployee.setDateBirth(moFieldDateBirth.getDate());
        moEmployee.setDateBenefits(moFieldDateBenefits.getDate());
        moEmployee.setDateLastHire(moFieldDateLastHire.getDate());
        moEmployee.setDateLastDismissal_n(moFieldDateLastDismissal_n.getDate());
        
        // update of business partner name MUST BE SET HERE, DO NOT MOVE!, that is just after father and mother surenames and forename already has been set in SDataEmployee!
        String formerBizPartner = moBizPartner.getBizPartner();
        moBizPartner.setBizPartner(moEmployee.composeLastname() + ", " + moFieldFirstname.getString());
        if (moBizPartner.getBizPartnerCommercial().isEmpty() || formerBizPartner.compareTo(moBizPartner.getBizPartnerCommercial()) == 0) {
            // former business partner name was equal to commercial name, so, set commercial name equal to business partner name again:
            moBizPartner.setBizPartnerCommercial(moBizPartner.getBizPartner());
        }
        //moBizPartner.setLastname(...);    // will be superseded by employee's father and mother surenames consigned in SDataEmployee
        moBizPartner.setFirstname(moFieldFirstname.getString());
        // end of update of business partner name
        
        if (paymentType == SModSysConsts.HRSS_TP_PAY_WEE) {
            moEmployee.setSalary(moFieldSalary.getDouble());
            moEmployee.setWage(0);
            moEmployee.setDateSalary(moFieldDateChangeSalary.getDate());
            moEmployee.setDateWage(moFieldDateChangeSalary.getDate());
        }
        else {
            moEmployee.setSalary(0);
            moEmployee.setWage(moFieldWage.getDouble());
            moEmployee.setDateSalary(moFieldDateChangeWage.getDate());
            moEmployee.setDateWage(moFieldDateChangeWage.getDate());
        }
        
        moEmployee.setSalarySscBase(moFieldSalarySscBase.getDouble());
        moEmployee.setDateSalarySscBase(moFieldDateChangeSalarySscBase.getDate() == null ? miClient.getSession().getCurrentDate() : moFieldDateChangeSalarySscBase.getDate());
        
        moEmployee.setWorkingHoursDay(moFieldWorkingHoursDay.getInteger());
        if (jradOvertimeNever.isSelected()) {
            moEmployee.setOvertimePolicy(SHrsConsts.OVERTIME_NEVER);
        }
        else if (jradOvertimeAllways.isSelected()) {
            moEmployee.setOvertimePolicy(SHrsConsts.OVERTIME_ALLWAYS);
        }
        else if (jradOvertimeSometimes.isSelected()) {
            moEmployee.setOvertimePolicy(SHrsConsts.OVERTIME_SOMETIMES);
        }
        
        if (jradCheckerAllways.isSelected()) {
            moEmployee.setCheckerPolicy(SHrsConsts.CHECKER_POLICY_ESTRICT);
        }
        else if (jradCheckerNever.isSelected()) {
            moEmployee.setCheckerPolicy(SHrsConsts.CHECKER_POLICY_FREE);
        }
        else if (jradCheckerSometimes.isSelected()) {
            moEmployee.setCheckerPolicy(SHrsConsts.CHECKER_POLICY_EVENT);
        }
        
        moEmployee.setContractExpiration_n(SHrsEmployeeUtils.isContractTypeIndefinite(moFieldFkContractType.getKeyAsIntArray()[0]) ? null : moFieldContractExpiration.getDate());
        moEmployee.setBankAccount(moFieldBankAccount.getString());
        moEmployee.setGroceryServiceAccount(moFieldGroceryServiceAccount.getString());
        
        try {
            // Image of employee's photo:
            
            if (mbPhotoChange) {
                moEmployee.setXtaImageIconPhoto_n(moXtaImageIconPhoto_n);
            }

            // Image of employee's signature:

            if (mbSignatureChange) {
                moEmployee.setXtaImageIconSignature_n(moXtaImageIconSignature_n);
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
        }
        
        moEmployee.setUnionized(moFieldIsUnionized.getBoolean());
        moEmployee.setMfgOperator(moFieldIsMfgOperator.getBoolean());
        moEmployee.setActive(moFieldIsActive.getBoolean());
        moEmployee.setDeleted(moFieldIsDeleted.getBoolean());
        //moEmployee.setSystem(...);
        
        moEmployee.setFkPaymentTypeId(moFieldFkPaymentType.getKeyAsIntArray()[0]);
        moEmployee.setFkSalaryTypeId(moFieldFkSalaryType.getKeyAsIntArray()[0]);
        moEmployee.setFkEmployeeTypeId(moFieldFkEmployeeType.getKeyAsIntArray()[0]);
        moEmployee.setFkWorkerTypeId(moFieldFkWorkerType.getKeyAsIntArray()[0]);
        moEmployee.setFkMwzTypeId(moFieldFkMwzType.getKeyAsIntArray()[0]);
        moEmployee.setFkDepartmentId(moFieldFkDepartment.getKeyAsIntArray()[0]);
        moEmployee.setFkPositionId(moFieldFkPosition.getKeyAsIntArray()[0]);
        moEmployee.setFkShiftId(moFieldFkShift.getKeyAsIntArray()[0]);
        moEmployee.setFkWorkingDayTypeId(moFieldFkWorkingDayType.getKeyAsIntArray()[0]);
        moEmployee.setFkContractTypeId(moFieldFkContractType.getKeyAsIntArray()[0]);
        moEmployee.setFkRecruitmentSchemaTypeId(moFieldFkRecruitmentSchemaType.getKeyAsIntArray()[0]);
        moEmployee.setFkPositionRiskTypeId(moFieldFkPositionRiskType.getKeyAsIntArray()[0]);
        moEmployee.setFkCatalogueSexClassId(moFieldFkCatalogueSexType.getKeyAsIntArray()[0]);
        moEmployee.setFkCatalogueSexTypeId(moFieldFkCatalogueSexType.getKeyAsIntArray()[1]);
        moEmployee.setFkCatalogueBloodTypeClassId(moFieldFkCatalogueBloodTypeType.getKeyAsIntArray()[0]);
        moEmployee.setFkCatalogueBloodTypeTypeId(moFieldFkCatalogueBloodTypeType.getKeyAsIntArray()[1]);
        moEmployee.setFkCatalogueMaritalStatusClassId(moFieldFkCatalogueMaritalStatusType.getKeyAsIntArray()[0]);
        moEmployee.setFkCatalogueMaritalStatusTypeId(moFieldFkCatalogueMaritalStatusType.getKeyAsIntArray()[1]);
        moEmployee.setFkCatalogueEducationClassId(moFieldFkCatalogueEducationType.getKeyAsIntArray()[0]);
        moEmployee.setFkCatalogueEducationTypeId(moFieldFkCatalogueEducationType.getKeyAsIntArray()[1]);
        //moEmployee.setFkSourceCompanyId(...); // source company set above in this method!
        moEmployee.setFkBankId_n(moFieldFkBank_n.getKeyAsIntArray()[0]);
        moEmployee.setFkGroceryServiceId(moFieldFkGroceryService.getKeyAsIntArray()[0]);
        moEmployee.setPlaceOfBirth(moFieldPlaceOfBirth.getString());
        moEmployee.setUmf(moFieldUmf.getString());
        
        if (jckChangeSalary.isSelected()) {
            moEmployee.setAuxSalary(moFieldSalary.getDouble());
            moEmployee.setAuxDateSalary(moFieldDateChangeSalary.getDate());
        }

        if (jckChangeWage.isSelected()) {
            moEmployee.setAuxWage(moFieldWage.getDouble());
            moEmployee.setAuxDateWage(moFieldDateChangeWage.getDate());
        }

        if (jckChangeSalarySscBase.isSelected()) {
            moEmployee.setAuxSalarySscBase(moFieldSalarySscBase.getDouble());
            moEmployee.setAuxDateSalarySscBase(moFieldDateChangeSalarySscBase.getDate());
        }
        
        SDataEmployeeRelatives relatives = new SDataEmployeeRelatives();
        //relatives.setPkEmployeeId(...);
        relatives.setMate(moFieldMateName.getString());
        relatives.setMateDateBirth(moFieldMateDateBirth.getDate());
        relatives.setMateDeceased(moFieldMateDeceased.getBoolean());
        relatives.setSon1(moFieldSonName1.getString());
        relatives.setSonDateBirth1(moFieldSonDateBirth1.getDate());
        relatives.setSonDeceased1(moFieldSonDeceased1.getBoolean());
        relatives.setSon2(moFieldSonName2.getString());
        relatives.setSonDateBirth2(moFieldSonDateBirth2.getDate());
        relatives.setSonDeceased2(moFieldSonDeceased2.getBoolean());
        relatives.setSon3(moFieldSonName3.getString());
        relatives.setSonDateBirth3(moFieldSonDateBirth3.getDate());
        relatives.setSonDeceased3(moFieldSonDeceased3.getBoolean());
        relatives.setSon4(moFieldSonName4.getString());
        relatives.setSonDateBirth4(moFieldSonDateBirth4.getDate());
        relatives.setSonDeceased4(moFieldSonDeceased4.getBoolean());
        relatives.setSon5(moFieldSonName5.getString());
        relatives.setSonDateBirth5(moFieldSonDateBirth5.getDate());
        relatives.setSonDeceased5(moFieldSonDeceased5.getBoolean());
        relatives.setFkCatSexClassIdMate(moFieldMateSex.getKeyAsIntArray()[0]);
        relatives.setFkCatSexTypeIdMate(moFieldMateSex.getKeyAsIntArray()[1]);
        relatives.setFkCatSexClassIdSon1(moFieldSonSex1.getKeyAsIntArray()[0]);
        relatives.setFkCatSexTypeIdSon1(moFieldSonSex1.getKeyAsIntArray()[1]);
        relatives.setFkCatSexClassIdSon2(moFieldSonSex2.getKeyAsIntArray()[0]);
        relatives.setFkCatSexTypeIdSon2(moFieldSonSex2.getKeyAsIntArray()[1]);
        relatives.setFkCatSexClassIdSon3(moFieldSonSex3.getKeyAsIntArray()[0]);
        relatives.setFkCatSexTypeIdSon3(moFieldSonSex3.getKeyAsIntArray()[1]);
        relatives.setFkCatSexClassIdSon4(moFieldSonSex4.getKeyAsIntArray()[0]);
        relatives.setFkCatSexTypeIdSon4(moFieldSonSex4.getKeyAsIntArray()[1]);
        relatives.setFkCatSexClassIdSon5(moFieldSonSex5.getKeyAsIntArray()[0]);
        relatives.setFkCatSexTypeIdSon5(moFieldSonSex5.getKeyAsIntArray()[1]);
        moEmployee.setChildRelatives(relatives);
        
        moEmployee.setIsRegistryEdited(true);
        
        moBizPartner.setIsRegistryEdited(true);
        
        // Save benefit tables of employee in a post-save request:
        
        try {
            moBizPartner.setAuxDbEmployeeBenefitTables(moEmployeeBenefitTables);
            
            moBizPartner.setPostSaveTarget(moBizPartner);
            moBizPartner.setPostSaveMethod(SDataBizPartner.class.getMethod("saveEmployeeBenefitTables", new Class[] { SGuiSession.class }));
            moBizPartner.setPostSaveMethodArgs(new Object[] { miClient.getSession() });
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        
        return moBizPartner;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case SDataConstantsSys.VALUE_BIZ_PARTNER_TYPE:
                mnParamBizPartnerType = ((int[]) value)[0];

                switch (mnParamBizPartnerType) {
                    case SDataConstants.BPSX_BP_EMP:
                        setTitle("Empleado");
                        break;
                    default:
                        setTitle("Asociado de negocios");
                }
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
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbDateBirth) {
                actionDateBirth();
            }
            else if (button == jbImagePhoto) {
                actionLoadFileImagePhoto();
            }
            else if (button == jbImagePhotoView) {
                actionFileImagePhotoView();
            }
            else if (button == jbImagePhotoRemove) {
                actionFileImagePhotoRemove();
            }
            else if (button == jbImageSignature) {
                actionLoadFileImageSignature();
            }
            else if (button == jbImageSignatureView) {
                actionFileImageSignatureView();
            }
            else if (button == jbImageSignatureRemove) {
                actionFileImageSignatureRemove();
            }
            else if (button == jbDateBenefitsEdit) {
                actionDateBenefitsEdit();
            }
            else if (button == jbDateBenefits) {
                actionDateBenefits();
            }
            else if (button == jbDateLastHire) {
                actionDateLastHire();
            }
            else if (button == jbDateChangeSalary) {
                actionDateSalaryChange();
            }
            else if (button == jbDateChangeWage) {
                actionDateWageChange();
            }
            else if (button == jbDateChangeSalarySscBase) {
                actionDateSalarySscBaseChange();
            }
            else if (button == jbContractExpiration) {
                actionContractExpiration();
            }
            else if (button == jbVacModify) {
                actionBenModify(SModSysConsts.HRSS_TP_BEN_VAC, true);
            }
            else if (button == jbVacUpdate) {
                actionBenUpdate(SModSysConsts.HRSS_TP_BEN_VAC, true);
            }
            else if (button == jbVacCancel) {
                actionBenCancel(SModSysConsts.HRSS_TP_BEN_VAC, false, true);
            }
            else if (button == jbVacBonModify) {
                actionBenModify(SModSysConsts.HRSS_TP_BEN_VAC_BON, true);
            }
            else if (button == jbVacBonUpdate) {
                actionBenUpdate(SModSysConsts.HRSS_TP_BEN_VAC_BON, true);
            }
            else if (button == jbVacBonCancel) {
                actionBenCancel(SModSysConsts.HRSS_TP_BEN_VAC_BON, false, true);
            }
            else if (button == jbAnnBonModify) {
                actionBenModify(SModSysConsts.HRSS_TP_BEN_ANN_BON, true);
            }
            else if (button == jbAnnBonUpdate) {
                actionBenUpdate(SModSysConsts.HRSS_TP_BEN_ANN_BON, true);
            }
            else if (button == jbAnnBonCancel) {
                actionBenCancel(SModSysConsts.HRSS_TP_BEN_ANN_BON, false, true);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (!mbUpdatingForm) {
            if (e.getSource() instanceof javax.swing.JCheckBox) {
                JCheckBox checkBox = (JCheckBox) e.getSource();

                if (checkBox == jckChangeSalary) {
                    itemStateSalary(true);
                }
                else if (checkBox == jckChangeWage) {
                    itemStateWage(true);
                }
                else if (checkBox == jckChangeSalarySscBase) {
                    itemStateSalarySscBaseChange(true);
                }
            }
            else if (e.getSource() instanceof javax.swing.JComboBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JComboBox comboBox = (JComboBox) e.getSource();

                    if (comboBox == jcbFkPaymentType) {
                        itemStateChangePaymentType();
                    }
                    else if (comboBox == jcbFkDepartment) {
                        itemStateChangedDepartament();
                    }
                    else if (comboBox == jcbFkContractType) {
                        itemStateChangedContractType();
                    }
                    else if (comboBox == jcbVacTable) {
                        itemStateChangedBenTable(SModSysConsts.HRSS_TP_BEN_VAC);
                    }
                    else if (comboBox == jcbVacBonTable) {
                        itemStateChangedBenTable(SModSysConsts.HRSS_TP_BEN_VAC_BON);
                    }
                    else if (comboBox == jcbAnnBonTable) {
                        itemStateChangedBenTable(SModSysConsts.HRSS_TP_BEN_ANN_BON);
                    }
                }
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof JFormattedTextField) {
            JFormattedTextField field = (JFormattedTextField) e.getSource();
            
            if (field == jftDateBenefits) {
                focusLostDateBenefits();
            }
            else if (field == jftDateBirth) {
                focusLostDateBirth();
            }
            else if (field == jftMateDateBirth) {
                focusLostMateDateBirth();
            }
            else if (field == jftSonDateBirth1) {
                focusLostSonDateBirth1();
            }
            else if (field == jftSonDateBirth2) {
                focusLostSonDateBirth2();
            }
            else if (field == jftSonDateBirth3) {
                focusLostSonDateBirth3();
            }
            else if (field == jftSonDateBirth4) {
                focusLostSonDateBirth4();
            }
            else if (field == jftSonDateBirth5) {
                focusLostSonDateBirth5();
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JTabbedPane) {
            stateChangedTabbedPane();
        }
    }
}
