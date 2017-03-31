/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mbps.form;

import erp.SErpConsts;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mbps.data.SDataBizPartnerBranchContact;
import erp.mbps.data.SDataBizPartnerCategory;
import erp.mbps.data.SDataEmployee;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SHrsConsts;
import erp.mod.hrs.db.SHrsUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Juan Barajas
 */
public class SFormBizPartnerEmployee extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.ItemListener {

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
    private erp.lib.form.SFormField moFieldNumber;
    private erp.lib.form.SFormField moFieldFirstName;
    private erp.lib.form.SFormField moFieldLastName;
    private erp.lib.form.SFormField moFieldFiscalId;
    private erp.lib.form.SFormField moFieldAlternativeId;
    private erp.lib.form.SFormField moFieldEmail;
    private erp.lib.form.SFormField moFieldIsDeleted;

    // Employee fields:

    private erp.lib.form.SFormField moFieldSocialSecurityNumber;
    private erp.lib.form.SFormField moFieldFileImagePhoto;
    private erp.lib.form.SFormField moFieldFileImageSignature;
    private erp.lib.form.SFormField moFieldFkBank_n;
    private erp.lib.form.SFormField moFieldBankAccount;
    private erp.lib.form.SFormField moFieldIsActive;
    private erp.lib.form.SFormField moFieldDateBirth;
    private erp.lib.form.SFormField moFieldDateBenefits;
    private erp.lib.form.SFormField moFieldDateLastHire;
    private erp.lib.form.SFormField moFieldDateLastDismiss_n;
    private erp.lib.form.SFormField moFieldFkPaymentType;
    private erp.lib.form.SFormField moFieldFkSalaryType;
    private erp.lib.form.SFormField moFieldSalary;
    private erp.lib.form.SFormField moFieldDateChangeSalary;
    private erp.lib.form.SFormField moFieldWage;
    private erp.lib.form.SFormField moFieldDateChangeWage;
    private erp.lib.form.SFormField moFieldSalarySscBase;
    private erp.lib.form.SFormField moFieldDateChangeSalarySscBase;
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
    private erp.lib.form.SFormField moFieldFkRecruitmentSchemeType;
    private erp.lib.form.SFormField moFieldFkPositionRiskType;
    private erp.lib.form.SFormField moFieldFkMwzType;
    private erp.lib.form.SFormField moFieldFkCatalogueSexType;
    private erp.lib.form.SFormField moFieldFkCatalogueBloodTypeType;
    private erp.lib.form.SFormField moFieldFkCatalogueMaritalStatusType;
    private erp.lib.form.SFormField moFieldFkCatalogueEducationType;

    private int mnParamBizPartnerType;

    private erp.mbps.form.SPanelBizPartnerBranchAddress moPanelBizPartnerBranchAddress;

    private int mnPkContactId;
    private int mnFormTypeExport;
    
    private boolean mbPhotoChange;
    private boolean mbSignatureChange;
    private javax.swing.ImageIcon moXtaImageIconPhoto_n;
    private javax.swing.ImageIcon moXtaImageIconSignature_n;

    /** Creates new form SFormBizPartnerEmployee
     * @param client 
    */
    public SFormBizPartnerEmployee(erp.client.SClientInterface client) {
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
        jPanel2 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jlNumber = new javax.swing.JLabel();
        jftNumber = new javax.swing.JFormattedTextField();
        jtfBizPartner_Ro = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jlFirstName = new javax.swing.JLabel();
        jtfFirstName = new javax.swing.JTextField();
        jPanel25 = new javax.swing.JPanel();
        jlLastName = new javax.swing.JLabel();
        jtfLastName = new javax.swing.JTextField();
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
        jPanel6 = new javax.swing.JPanel();
        jlEmail = new javax.swing.JLabel();
        jtfEmail = new javax.swing.JTextField();
        jckIsActive = new javax.swing.JCheckBox();
        jckIsDeleted = new javax.swing.JCheckBox();
        jPanel20 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jlDateBenefits = new javax.swing.JLabel();
        jftDateBenefits = new javax.swing.JFormattedTextField();
        jbDateBenefits = new javax.swing.JButton();
        jbDateBenefitsEdit = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jlDateLastHire = new javax.swing.JLabel();
        jftDateLastHire = new javax.swing.JFormattedTextField();
        jbDateLastHire = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jlDateLastDismiss_n = new javax.swing.JLabel();
        jftDateLastDismiss_n = new javax.swing.JFormattedTextField();
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
        jPanel27 = new javax.swing.JPanel();
        jlFkContractType = new javax.swing.JLabel();
        jcbFkContractType = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel33 = new javax.swing.JPanel();
        jlFkRecruitmentSchemeType = new javax.swing.JLabel();
        jcbFkRecruitmentSchemeType = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel23 = new javax.swing.JPanel();
        jlFkPositionRiskType = new javax.swing.JLabel();
        jcbFkPositionRiskType = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel31 = new javax.swing.JPanel();
        jlFkMwzType = new javax.swing.JLabel();
        jcbFkMwzType = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel35 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jlDateBirth = new javax.swing.JLabel();
        jftDateBirth = new javax.swing.JFormattedTextField();
        jbDateBirth = new javax.swing.JButton();
        jPanel36 = new javax.swing.JPanel();
        jlFkCatalogueSexTypeId = new javax.swing.JLabel();
        jcbFkCatalogueSexTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jlDummy3 = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        jlFkCatalogueBloodTypeTypeId = new javax.swing.JLabel();
        jcbFkCatalogueBloodTypeTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jlDummy4 = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        jlFkCatalogueMaritalStatusTypeId = new javax.swing.JLabel();
        jcbFkCatalogueMaritalStatusTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel39 = new javax.swing.JPanel();
        jlFkCatalogueEducationTypeId = new javax.swing.JLabel();
        jcbFkCatalogueEducationTypeId = new javax.swing.JComboBox<SFormComponentItem>();
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
        jpBranchAddress = new javax.swing.JPanel();
        jpOficialAddress = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
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

        jPanel2.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel42.setLayout(new java.awt.BorderLayout());

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos personales:"));
        jPanel19.setLayout(new java.awt.GridLayout(6, 1, 0, 5));

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNumber.setText("Clave empleado:*");
        jlNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel26.add(jlNumber);

        jftNumber.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        jftNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel26.add(jftNumber);

        jtfBizPartner_Ro.setEditable(false);
        jtfBizPartner_Ro.setToolTipText("Empleado");
        jtfBizPartner_Ro.setFocusable(false);
        jtfBizPartner_Ro.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel26.add(jtfBizPartner_Ro);

        jPanel19.add(jPanel26);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFirstName.setText("Nombre(s):*");
        jlFirstName.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel22.add(jlFirstName);

        jtfFirstName.setPreferredSize(new java.awt.Dimension(200, 23));
        jtfFirstName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfFirstNameFocusLost(evt);
            }
        });
        jPanel22.add(jtfFirstName);

        jPanel19.add(jPanel22);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLastName.setText("Apellido(s):*");
        jlLastName.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel25.add(jlLastName);

        jtfLastName.setPreferredSize(new java.awt.Dimension(200, 23));
        jtfLastName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfLastNameFocusLost(evt);
            }
        });
        jPanel25.add(jtfLastName);

        jPanel19.add(jPanel25);

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFiscalId.setText("RFC:*");
        jlFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel29.add(jlFiscalId);

        jtfFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel29.add(jtfFiscalId);

        jlAlternativeId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlAlternativeId.setText("CURP:*");
        jlAlternativeId.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel29.add(jlAlternativeId);

        jftAlternativeId.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        jftAlternativeId.setPreferredSize(new java.awt.Dimension(125, 23));
        jftAlternativeId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jftAlternativeIdFocusLost(evt);
            }
        });
        jPanel29.add(jftAlternativeId);

        jlSocialSecurityNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlSocialSecurityNumber.setText("NSS:");
        jlSocialSecurityNumber.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel29.add(jlSocialSecurityNumber);

        jtfSocialSecurityNumber.setPreferredSize(new java.awt.Dimension(200, 23));
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

        jtfBankAccount.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel24.add(jtfBankAccount);

        jPanel19.add(jPanel24);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlEmail.setText("Cuenta correo-e:");
        jlEmail.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlEmail);

        jtfEmail.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel6.add(jtfEmail);

        jckIsActive.setText("Empleado activo");
        jckIsActive.setEnabled(false);
        jckIsActive.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel6.add(jckIsActive);

        jckIsDeleted.setText("Asociado de negocios eliminado");
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel6.add(jckIsDeleted);

        jPanel19.add(jPanel6);

        jPanel42.add(jPanel19, java.awt.BorderLayout.NORTH);

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos laborables:"));
        jPanel20.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridLayout(12, 1, 0, 5));

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlDateBenefits.setText("Fecha beneficios:*");
        jlDateBenefits.setPreferredSize(new java.awt.Dimension(125, 23));
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

        jPanel3.add(jPanel8);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlDateLastHire.setText("Fecha última alta:*");
        jlDateLastHire.setPreferredSize(new java.awt.Dimension(125, 23));
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

        jlDateLastDismiss_n.setText("Fecha última baja:");
        jlDateLastDismiss_n.setEnabled(false);
        jlDateLastDismiss_n.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jlDateLastDismiss_n);

        jftDateLastDismiss_n.setEditable(false);
        jftDateLastDismiss_n.setText("yyyy/mm/dd");
        jftDateLastDismiss_n.setFocusable(false);
        jftDateLastDismiss_n.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel9.add(jftDateLastDismiss_n);

        jPanel3.add(jPanel9);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkPaymentType.setText("Período pago:*");
        jlFkPaymentType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel28.add(jlFkPaymentType);

        jcbFkPaymentType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel28.add(jcbFkPaymentType);

        jPanel3.add(jPanel28);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkSalaryType.setText("Tipo salario:*");
        jlFkSalaryType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel13.add(jlFkSalaryType);

        jcbFkSalaryType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel13.add(jcbFkSalaryType);

        jPanel3.add(jPanel13);

        jckChangeSalary.setText("Cambiar salario");
        jckChangeSalary.setFocusable(false);
        jPanel3.add(jckChangeSalary);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlSalary.setText("Salario:*");
        jlSalary.setPreferredSize(new java.awt.Dimension(125, 23));
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
        jlWage.setPreferredSize(new java.awt.Dimension(125, 23));
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
        jlSalarySscBase.setPreferredSize(new java.awt.Dimension(125, 23));
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

        jPanel20.add(jPanel3, java.awt.BorderLayout.WEST);

        jPanel4.setLayout(new java.awt.GridLayout(12, 1, 0, 5));

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkEmployeeType.setText("Tipo empleado:*");
        jlFkEmployeeType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel30.add(jlFkEmployeeType);

        jcbFkEmployeeType.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel30.add(jcbFkEmployeeType);

        jPanel4.add(jPanel30);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkWorkerType.setText("Tipo obrero:*");
        jlFkWorkerType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel15.add(jlFkWorkerType);

        jcbFkWorkerType.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel15.add(jcbFkWorkerType);

        jPanel4.add(jPanel15);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jckIsUnionized.setText("Es sindicalizado");
        jckIsUnionized.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel12.add(jckIsUnionized);

        jckIsMfgOperator.setText("Es operador");
        jckIsMfgOperator.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel12.add(jckIsMfgOperator);

        jPanel4.add(jPanel12);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkDepartment.setText("Departamento:*");
        jlFkDepartment.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel17.add(jlFkDepartment);

        jcbFkDepartment.setPreferredSize(new java.awt.Dimension(350, 23));
        jcbFkDepartment.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbFkDepartmentItemStateChanged(evt);
            }
        });
        jPanel17.add(jcbFkDepartment);

        jPanel4.add(jPanel17);

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkPosition.setText("Puesto:*");
        jlFkPosition.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel32.add(jlFkPosition);

        jcbFkPosition.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel32.add(jcbFkPosition);

        jPanel4.add(jPanel32);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkShift.setText("Turno:*");
        jlFkShift.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel21.add(jlFkShift);

        jcbFkShift.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel21.add(jcbFkShift);

        jPanel4.add(jPanel21);

        jPanel44.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkWorkingDayType.setText("Tipo jornada:");
        jlFkWorkingDayType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel44.add(jlFkWorkingDayType);

        jcbFkWorkingDayType.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel44.add(jcbFkWorkingDayType);

        jPanel4.add(jPanel44);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlWorkingHoursDay.setText("Horas jornada:*");
        jlWorkingHoursDay.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel11.add(jlWorkingHoursDay);

        jtfWorkingHoursDay.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfWorkingHoursDay.setText("0");
        jtfWorkingHoursDay.setToolTipText("Unidades");
        jtfWorkingHoursDay.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jtfWorkingHoursDay);

        jPanel4.add(jPanel11);

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkContractType.setText("Tipo contrato:*");
        jlFkContractType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel27.add(jlFkContractType);

        jcbFkContractType.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel27.add(jcbFkContractType);

        jPanel4.add(jPanel27);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkRecruitmentSchemeType.setText("Régimen contratación:*");
        jlFkRecruitmentSchemeType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel33.add(jlFkRecruitmentSchemeType);

        jcbFkRecruitmentSchemeType.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel33.add(jcbFkRecruitmentSchemeType);

        jPanel4.add(jPanel33);

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkPositionRiskType.setText("Riesgo trabajo:*");
        jlFkPositionRiskType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel23.add(jlFkPositionRiskType);

        jcbFkPositionRiskType.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel23.add(jcbFkPositionRiskType);

        jPanel4.add(jPanel23);

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlFkMwzType.setText("Área geográfica:*");
        jlFkMwzType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel31.add(jlFkMwzType);

        jcbFkMwzType.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel31.add(jcbFkMwzType);

        jPanel4.add(jPanel31);

        jPanel20.add(jPanel4, java.awt.BorderLayout.EAST);

        jPanel42.add(jPanel20, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel42, java.awt.BorderLayout.NORTH);

        jTabbedPane1.addTab("Datos del registro", jPanel2);

        jPanel35.setLayout(new java.awt.BorderLayout());

        jPanel43.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos adicionales:"));
        jPanel43.setLayout(new java.awt.GridLayout(8, 0, 0, 5));

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlDateBirth.setText("Fecha nacimiento:*");
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

        jPanel43.add(jPanel34);

        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCatalogueSexTypeId.setText("Sexo:*");
        jlFkCatalogueSexTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel36.add(jlFkCatalogueSexTypeId);

        jcbFkCatalogueSexTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel36.add(jcbFkCatalogueSexTypeId);

        jlDummy3.setPreferredSize(new java.awt.Dimension(45, 23));
        jPanel36.add(jlDummy3);

        jPanel43.add(jPanel36);

        jPanel37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCatalogueBloodTypeTypeId.setText("Tipo de sangre:*");
        jlFkCatalogueBloodTypeTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel37.add(jlFkCatalogueBloodTypeTypeId);

        jcbFkCatalogueBloodTypeTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel37.add(jcbFkCatalogueBloodTypeTypeId);

        jlDummy4.setPreferredSize(new java.awt.Dimension(45, 23));
        jPanel37.add(jlDummy4);

        jPanel43.add(jPanel37);

        jPanel38.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCatalogueMaritalStatusTypeId.setText("Estado civil:*");
        jlFkCatalogueMaritalStatusTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel38.add(jlFkCatalogueMaritalStatusTypeId);

        jcbFkCatalogueMaritalStatusTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel38.add(jcbFkCatalogueMaritalStatusTypeId);

        jPanel43.add(jPanel38);

        jPanel39.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCatalogueEducationTypeId.setText("Escolaridad:*");
        jlFkCatalogueEducationTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel39.add(jlFkCatalogueEducationTypeId);

        jcbFkCatalogueEducationTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel39.add(jcbFkCatalogueEducationTypeId);

        jPanel43.add(jPanel39);

        jPanel45.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlImagePhoto.setText("Fotografía:");
        jlImagePhoto.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel45.add(jlImagePhoto);

        jtfImagePhoto.setEditable(false);
        jtfImagePhoto.setFocusable(false);
        jtfImagePhoto.setPreferredSize(new java.awt.Dimension(500, 23));
        jPanel45.add(jtfImagePhoto);

        jbImagePhoto.setText("jButton3");
        jbImagePhoto.setToolTipText("Seleccionar fotografía");
        jbImagePhoto.setFocusable(false);
        jbImagePhoto.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel45.add(jbImagePhoto);

        jbImagePhotoView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif"))); // NOI18N
        jbImagePhotoView.setToolTipText("Ver fotografía del empleado");
        jbImagePhotoView.setFocusable(false);
        jbImagePhotoView.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel45.add(jbImagePhotoView);

        jbImagePhotoRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbImagePhotoRemove.setToolTipText("Eliminar fotografía del empleado");
        jbImagePhotoRemove.setFocusable(false);
        jbImagePhotoRemove.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel45.add(jbImagePhotoRemove);

        jPanel43.add(jPanel45);

        jPanel46.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlImageSignature.setText("Firma:");
        jlImageSignature.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel46.add(jlImageSignature);

        jtfImageSignature.setEditable(false);
        jtfImageSignature.setFocusable(false);
        jtfImageSignature.setPreferredSize(new java.awt.Dimension(500, 23));
        jPanel46.add(jtfImageSignature);

        jbImageSignature.setText("jButton3");
        jbImageSignature.setToolTipText("Seleccionar firma");
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

        jPanel43.add(jPanel46);

        jPanel35.add(jPanel43, java.awt.BorderLayout.NORTH);

        jpBranchAddress.setBorder(javax.swing.BorderFactory.createTitledBorder("Información del domicilio:"));
        jpBranchAddress.setLayout(new java.awt.BorderLayout());

        jpOficialAddress.setLayout(new java.awt.BorderLayout());
        jpBranchAddress.add(jpOficialAddress, java.awt.BorderLayout.CENTER);

        jPanel40.setLayout(new java.awt.BorderLayout());
        jpBranchAddress.add(jPanel40, java.awt.BorderLayout.NORTH);

        jPanel35.add(jpBranchAddress, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Datos adicionales", jPanel35);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

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

        setSize(new java.awt.Dimension(976, 639));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivate();
    }//GEN-LAST:event_formWindowActivated

    private void jftAlternativeIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jftAlternativeIdFocusLost
        focusLostAlternativeId();
    }//GEN-LAST:event_jftAlternativeIdFocusLost

    private void jtfFirstNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfFirstNameFocusLost
        focusLostFirstName();
    }//GEN-LAST:event_jtfFirstNameFocusLost

    private void jtfLastNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfLastNameFocusLost
        focusLostLastName();
    }//GEN-LAST:event_jtfLastNameFocusLost

    private void jcbFkDepartmentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbFkDepartmentItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            itemStateChangedDepartament();
        }
    }//GEN-LAST:event_jcbFkDepartmentItemStateChanged

    private void initComponentsExtra() {
        mvFields = new Vector<SFormField>();

        moFieldNumber = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jftNumber, jlNumber);
        moFieldNumber.setLengthMax(10);
        moFieldNumber.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFirstName = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfFirstName, jlFirstName);
        moFieldFirstName.setLengthMax(100);
        moFieldFirstName.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldLastName = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfLastName, jlLastName);
        moFieldLastName.setLengthMax(100);
        moFieldLastName.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFiscalId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfFiscalId, jlFiscalId);
        moFieldFiscalId.setLengthMin(13);
        moFieldFiscalId.setLengthMax(13);
        moFieldFiscalId.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldAlternativeId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jftAlternativeId, jlAlternativeId);
        moFieldAlternativeId.setLengthMin(18);
        moFieldAlternativeId.setLengthMax(18);
        moFieldAlternativeId.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldEmail = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfEmail, jlEmail);
        moFieldEmail.setLengthMax(50);
        moFieldEmail.setAutoCaseType(SLibConstants.UNDEFINED);
        moFieldEmail.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDeleted);
        moFieldIsDeleted.setTabbedPaneIndex(0, jTabbedPane1);

        // Employee:

        moFieldSocialSecurityNumber = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfSocialSecurityNumber, jlSocialSecurityNumber);
        //moFieldSocialSecurityNumber.setLengthMin(11);
        moFieldSocialSecurityNumber.setLengthMax(11);
        moFieldSocialSecurityNumber.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFkBank_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkBank_n, jlFkBank_n);
        moFieldFkBank_n.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldBankAccount = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfBankAccount, jlBankAccount);
        moFieldBankAccount.setLengthMax(20);
        moFieldBankAccount.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldIsActive = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsActive);
        moFieldIsActive.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldDateBenefits = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateBenefits, jlDateBenefits);
        moFieldDateBenefits.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldDateLastHire = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateLastHire, jlDateLastHire);
        moFieldDateLastHire.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldDateLastDismiss_n = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftDateLastDismiss_n, jlDateLastDismiss_n);
        moFieldDateLastDismiss_n.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFkPaymentType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkPaymentType, jlFkPaymentType);
        moFieldFkPaymentType.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFkSalaryType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkSalaryType, jlFkSalaryType);
        moFieldFkSalaryType.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldSalary = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfSalary, jlSalary);
        moFieldSalary.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldDateChangeSalary = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateChangeSalary, jlSalary);
        moFieldDateChangeSalary.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldWage = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfWage, jlWage);
        moFieldWage.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldDateChangeWage = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateChangeWage, jlWage);
        moFieldDateChangeWage.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldSalarySscBase = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfSalarySscBase, jlSalarySscBase);
        moFieldSalarySscBase.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldDateChangeSalarySscBase = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftDateChangeSalarySscBase, jlSalarySscBase);
        moFieldDateChangeSalarySscBase.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFkEmployeeType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkEmployeeType, jlFkEmployeeType);
        moFieldFkEmployeeType.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFkWorkerType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkWorkerType, jlFkWorkerType);
        moFieldFkWorkerType.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldIsUnionized = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsUnionized);
        moFieldIsUnionized.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldIsMfgOperator = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsMfgOperator);
        moFieldIsMfgOperator.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFkDepartment = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkDepartment, jlFkDepartment);
        moFieldFkDepartment.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFkPosition = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkPosition, jlFkPosition);
        moFieldFkPosition.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFkShift = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkShift, jlFkShift);
        moFieldFkShift.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFkWorkingDayType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkWorkingDayType, jlFkWorkingDayType);
        moFieldFkWorkingDayType.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldWorkingHoursDay = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfWorkingHoursDay, jlWorkingHoursDay);
        moFieldWorkingHoursDay.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFkContractType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkContractType, jlFkContractType);
        moFieldFkContractType.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFkRecruitmentSchemeType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkRecruitmentSchemeType, jlFkRecruitmentSchemeType);
        moFieldFkRecruitmentSchemeType.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFkPositionRiskType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkPositionRiskType, jlFkPositionRiskType);
        moFieldFkPositionRiskType.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldFkMwzType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkMwzType, jlFkMwzType);
        moFieldFkMwzType.setTabbedPaneIndex(0, jTabbedPane1);
        moFieldDateBirth = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateBirth, jlDateBirth);
        moFieldDateBirth.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFileImagePhoto = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfImagePhoto, jlImagePhoto);
        moFieldFileImagePhoto.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFileImageSignature = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfImageSignature, jlImageSignature);
        moFieldFileImageSignature.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkCatalogueSexType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCatalogueSexTypeId, jlFkCatalogueSexTypeId);
        moFieldFkCatalogueSexType.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkCatalogueBloodTypeType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCatalogueBloodTypeTypeId, jlFkCatalogueBloodTypeTypeId);
        moFieldFkCatalogueBloodTypeType.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkCatalogueMaritalStatusType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCatalogueMaritalStatusTypeId, jlFkCatalogueMaritalStatusTypeId);
        moFieldFkCatalogueMaritalStatusType.setTabbedPaneIndex(1, jTabbedPane1);
        moFieldFkCatalogueEducationType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCatalogueEducationTypeId, jlFkCatalogueEducationTypeId);
        moFieldFkCatalogueEducationType.setTabbedPaneIndex(1, jTabbedPane1);

        moPanelBizPartnerBranchAddress = new SPanelBizPartnerBranchAddress(miClient);
        jpOficialAddress.add(moPanelBizPartnerBranchAddress, BorderLayout.NORTH);

        mvFields.add(moFieldNumber);
        mvFields.add(moFieldFirstName);
        mvFields.add(moFieldLastName);
        mvFields.add(moFieldFiscalId);
        mvFields.add(moFieldAlternativeId);
        mvFields.add(moFieldSocialSecurityNumber);
        mvFields.add(moFieldFkBank_n);
        mvFields.add(moFieldBankAccount);
        mvFields.add(moFieldIsActive);
        mvFields.add(moFieldEmail);
        mvFields.add(moFieldIsDeleted);
        mvFields.add(moFieldDateBenefits);
        mvFields.add(moFieldDateLastHire);
        mvFields.add(moFieldDateLastDismiss_n);
        mvFields.add(moFieldFkPaymentType);
        mvFields.add(moFieldFkSalaryType);
        mvFields.add(moFieldSalary);
        mvFields.add(moFieldDateChangeSalary);
        mvFields.add(moFieldWage);
        mvFields.add(moFieldDateChangeWage);
        mvFields.add(moFieldSalarySscBase);
        mvFields.add(moFieldDateChangeSalarySscBase);
        mvFields.add(moFieldFkEmployeeType);
        mvFields.add(moFieldFkWorkerType);
        mvFields.add(moFieldIsUnionized);
        mvFields.add(moFieldIsMfgOperator);
        mvFields.add(moFieldFkDepartment);
        mvFields.add(moFieldFkPosition);
        mvFields.add(moFieldFkShift);
        mvFields.add(moFieldFkWorkerType);
        mvFields.add(moFieldWorkingHoursDay);
        mvFields.add(moFieldFkContractType);
        mvFields.add(moFieldFkRecruitmentSchemeType);
        mvFields.add(moFieldFkPositionRiskType);
        mvFields.add(moFieldFkMwzType);
        mvFields.add(moFieldDateBirth);
        mvFields.add(moFieldFileImagePhoto);
        mvFields.add(moFieldFileImageSignature);
        mvFields.add(moFieldFkCatalogueSexType);
        mvFields.add(moFieldFkCatalogueBloodTypeType);
        mvFields.add(moFieldFkCatalogueMaritalStatusType);
        mvFields.add(moFieldFkCatalogueEducationType);

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
        jckChangeSalary.addItemListener(this);
        jckChangeWage.addItemListener(this);
        jckChangeSalarySscBase.addItemListener(this);
        jcbFkPaymentType.addItemListener(this);

        moFieldAlternativeId.setMaskFormatter("UUUU######XXXXXXXX");

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
            jtfFirstName.requestFocus();
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
        String name = moFieldLastName.getString();

        if (!moFieldFirstName.getString().isEmpty()) {
            name += (name.isEmpty() ? "" : ", ") + moFieldFirstName.getString();
        }

        return name;
    }

    private void computeBizPartner_Ro() {
        jtfBizPartner_Ro.setText(composeName());
        jtfBizPartner_Ro.setCaretPosition(0);
    }

    private void focusLostFirstName() {
        computeBizPartner_Ro();
    }

    private void focusLostLastName() {
        computeBizPartner_Ro();
    }

    private void focusLostAlternativeId() {
        if (jftAlternativeId.getText().trim().length() > 0) {
            jftAlternativeId.setText(jftAlternativeId.getText().toUpperCase());
        }
        else {
            jftAlternativeId.setText("");
        }
    }
    private void itemStateChangedDepartament() {
        renderPosition();
    }
    
    private void renderPosition() {
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
        SDataBizPartnerBranchAddress address = null;

        if (moBizPartnerBranch == null) {
            moBizPartnerBranch = new SDataBizPartnerBranch();
            moBizPartnerBranch.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moBizPartnerBranch.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moBizPartnerBranch.getDbmsBizPartnerBranchAddresses().clear();
        moBizPartnerBranch.getDbmsBizPartnerBranchContacts().clear();

        moBizPartnerBranch.setFkBizPartnerBranchTypeId(SDataConstantsSys.BPSS_TP_BPB_HQ);
        moBizPartnerBranch.setBizPartnerBranch(SModSysConsts.TXT_HQ);
        moBizPartnerBranch.setIsAddressPrintable(true);
        moBizPartnerBranch.setIsDeleted(false);

        moBizPartnerBranch.setDbmsTaxRegion("");

        address = (SDataBizPartnerBranchAddress) moPanelBizPartnerBranchAddress.getRegistry();
        address.setFkAddressTypeId(SDataConstantsSys.BPSS_TP_ADD_OFF);
        address.setIsDefault(true);

        moBizPartnerBranch.getDbmsBizPartnerBranchAddresses().add(address);
        moBizPartnerBranch.getDbmsBizPartnerBranchContacts().add(createBizPartnerBranchContact());
        moBizPartner.getDbmsBizPartnerBranches().add(moBizPartnerBranch);
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
            category.setDateStart(SLibTimeUtilities.getBeginOfYear(miClient.getSession().getCurrentDate()));
            category.setDateEnd_n(null);
            category.setPaymentAccount("");
            category.setNumberExporter("");
            category.setIsCreditByUser(true);
            category.setIsGuaranteeInProcess(true);
            category.setIsInsuranceInProcess(true);
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

    private SDataBizPartnerBranch createBizPartnerBranch() {
        SDataBizPartnerBranch branch = null;
        SDataBizPartnerBranchAddress address = null;

        branch = new SDataBizPartnerBranch();
        //branch.setPkBizPartnerBranchId();
        branch.setBizPartnerBranch(SModSysConsts.TXT_HQ);
        branch.setCode("");
        branch.setIsAddressPrintable(true);
        branch.setIsDeleted(false);
        //branch.setFkBizPartnerId();
        branch.setFkBizPartnerBranchTypeId(SDataConstantsSys.BPSS_TP_BPB_HQ);
        branch.setFkTaxRegionId_n(SLibConsts.UNDEFINED);
        branch.setFkAddressFormatTypeId_n(SLibConsts.UNDEFINED);
        branch.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

        address = new SDataBizPartnerBranchAddress();
        //address.setPkBizPartnerBranchId();
        //address.setPkAddressId();
        address.setAddress(SModSysConsts.TXT_OFFICIAL);
        address.setStreet("");
        address.setStreetNumberExt("");
        address.setStreetNumberInt("");
        address.setNeighborhood("");
        address.setReference("");
        address.setLocality("");
        address.setCounty("");
        address.setState("");
        address.setZipCode("");
        address.setPoBox("");
        address.setIsDefault(true);
        address.setIsDeleted(false);
        address.setFkAddressTypeId(SModSysConsts.BPSS_TP_ADD_OFF);
        address.setFkCountryId_n(SLibConsts.UNDEFINED);
        address.setFkStateId_n(SLibConsts.UNDEFINED);
        address.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

        branch.getDbmsBizPartnerBranchAddresses().add(address);

        return branch;
    }

    public SDataBizPartnerBranchContact createBizPartnerBranchContact() {
        SDataBizPartnerBranchContact contact = null;

        contact = new SDataBizPartnerBranchContact();
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
        contact.setPkContactTypeId(SDataConstantsSys.BPSS_TP_CON_ADM);
        contact.setPkTelephoneType01Id(SDataConstantsSys.BPSS_TP_TEL_NA);
        contact.setPkTelephoneType02Id(SDataConstantsSys.BPSS_TP_TEL_NA);
        contact.setPkTelephoneType03Id(SDataConstantsSys.BPSS_TP_TEL_NA);
        contact.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        contact.setFkUserEditId(miClient.getSession().getUser().getPkUserId());

        return contact;
    }

    private void actionOk() {
        SFormValidation validation = formValidate();

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

    private void actionDateBenefitsEdit() {
        updateStatusDateBenefits(true);
        jftDateBenefits.requestFocus();
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

    private void itemStateSalary(boolean focusEnabled) {
        if (jckChangeSalary.isSelected()) {
            jtfSalary.setEnabled(true);
            jftDateChangeSalary.setEnabled(true);
            jbDateChangeSalary.setEnabled(true);

            moFieldDateChangeSalary.setDate(null);

            if (focusEnabled) {
                jtfSalary.requestFocus();
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

    private void itemStateWage(boolean focusEnabled) {
        if (jckChangeWage.isSelected()) {
            jtfWage.setEnabled(true);
            jftDateChangeWage.setEnabled(true);
            jbDateChangeWage.setEnabled(true);

            moFieldDateChangeWage.setDate(null);

            if (focusEnabled) {
                jtfWage.requestFocus();
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

    private void itemStateSalarySscBaseChange(boolean focusEnabled) {
        if (jckChangeSalarySscBase.isSelected()) {
            jtfSalarySscBase.setEnabled(true);
            jftDateChangeSalarySscBase.setEnabled(true);
            jbDateChangeSalarySscBase.setEnabled(true);

            moFieldDateChangeSalarySscBase.setDate(null);

            if (focusEnabled) {
                jtfSalarySscBase.requestFocus();
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jbCancel;
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
    private javax.swing.JButton jbOk;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkBank_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCatalogueBloodTypeTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCatalogueEducationTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCatalogueMaritalStatusTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCatalogueSexTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkContractType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkDepartment;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkEmployeeType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkMwzType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkPaymentType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkPosition;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkPositionRiskType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkRecruitmentSchemeType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSalaryType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkShift;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkWorkerType;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkWorkingDayType;
    private javax.swing.JCheckBox jckChangeSalary;
    private javax.swing.JCheckBox jckChangeSalarySscBase;
    private javax.swing.JCheckBox jckChangeWage;
    private javax.swing.JCheckBox jckIsActive;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JCheckBox jckIsMfgOperator;
    private javax.swing.JCheckBox jckIsUnionized;
    private javax.swing.JFormattedTextField jftAlternativeId;
    private javax.swing.JFormattedTextField jftDateBenefits;
    private javax.swing.JFormattedTextField jftDateBirth;
    private javax.swing.JFormattedTextField jftDateChangeSalary;
    private javax.swing.JFormattedTextField jftDateChangeSalarySscBase;
    private javax.swing.JFormattedTextField jftDateChangeWage;
    private javax.swing.JFormattedTextField jftDateLastDismiss_n;
    private javax.swing.JFormattedTextField jftDateLastHire;
    private javax.swing.JFormattedTextField jftNumber;
    private javax.swing.JLabel jlAlternativeId;
    private javax.swing.JLabel jlBankAccount;
    private javax.swing.JLabel jlDateBenefits;
    private javax.swing.JLabel jlDateBirth;
    private javax.swing.JLabel jlDateLastDismiss_n;
    private javax.swing.JLabel jlDateLastHire;
    private javax.swing.JLabel jlDummy3;
    private javax.swing.JLabel jlDummy4;
    private javax.swing.JLabel jlEmail;
    private javax.swing.JLabel jlFirstName;
    private javax.swing.JLabel jlFiscalId;
    private javax.swing.JLabel jlFkBank_n;
    private javax.swing.JLabel jlFkCatalogueBloodTypeTypeId;
    private javax.swing.JLabel jlFkCatalogueEducationTypeId;
    private javax.swing.JLabel jlFkCatalogueMaritalStatusTypeId;
    private javax.swing.JLabel jlFkCatalogueSexTypeId;
    private javax.swing.JLabel jlFkContractType;
    private javax.swing.JLabel jlFkDepartment;
    private javax.swing.JLabel jlFkEmployeeType;
    private javax.swing.JLabel jlFkMwzType;
    private javax.swing.JLabel jlFkPaymentType;
    private javax.swing.JLabel jlFkPosition;
    private javax.swing.JLabel jlFkPositionRiskType;
    private javax.swing.JLabel jlFkRecruitmentSchemeType;
    private javax.swing.JLabel jlFkSalaryType;
    private javax.swing.JLabel jlFkShift;
    private javax.swing.JLabel jlFkWorkerType;
    private javax.swing.JLabel jlFkWorkingDayType;
    private javax.swing.JLabel jlImagePhoto;
    private javax.swing.JLabel jlImageSignature;
    private javax.swing.JLabel jlLastName;
    private javax.swing.JLabel jlNumber;
    private javax.swing.JLabel jlSalary;
    private javax.swing.JLabel jlSalarySscBase;
    private javax.swing.JLabel jlSocialSecurityNumber;
    private javax.swing.JLabel jlWage;
    private javax.swing.JLabel jlWorkingHoursDay;
    private javax.swing.JPanel jpBranchAddress;
    private javax.swing.JPanel jpOficialAddress;
    private javax.swing.JTextField jtfBankAccount;
    private javax.swing.JTextField jtfBizPartner_Ro;
    private javax.swing.JTextField jtfEmail;
    private javax.swing.JTextField jtfFirstName;
    private javax.swing.JTextField jtfFiscalId;
    private javax.swing.JTextField jtfImagePhoto;
    private javax.swing.JTextField jtfImageSignature;
    private javax.swing.JTextField jtfLastName;
    private javax.swing.JTextField jtfPkBizPartnerId_Ro;
    private javax.swing.JTextField jtfSalary;
    private javax.swing.JTextField jtfSalarySscBase;
    private javax.swing.JTextField jtfSocialSecurityNumber;
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
        moXtaImageIconSignature_n = null;

        moBizPartner = null;
        moBizPartnerBranch = null;
        moEmployee = null;

        mnFormTypeExport = SLibConstants.UNDEFINED;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        computeBizPartner_Ro();

        moFieldIsActive.setFieldValue(true);
        moFieldDateBirth.setFieldValue(miClient.getSessionXXX().getWorkingDate());
        moFieldDateBenefits.setFieldValue(miClient.getSessionXXX().getWorkingDate());
        moFieldDateLastHire.setFieldValue(miClient.getSessionXXX().getWorkingDate());
        moPanelBizPartnerBranchAddress.formReset();
        moPanelBizPartnerBranchAddress.setParamIsInMainWindow(true);
        jTabbedPane1.setSelectedIndex(0);
        jckIsMfgOperator.setSelected(false);
        moPanelBizPartnerBranchAddress.setFieldsEnabled(true);

        mnPkContactId = 0;
        jckIsDeleted.setEnabled(false);
        jckChangeSalary.setSelected(false);
        jckChangeWage.setSelected(false);
        jckChangeSalarySscBase.setEnabled(false);
        jckChangeSalarySscBase.setSelected(true);
        updateStatusDateBenefits(true);
        updateStatusDateLastHire(true);
        updateEmployeeNextNumber();

        itemStateChangePaymentType();
        //itemStateSalary();    // invocked allready by itemStateChangePaymentType()
        //itemStateWage();      // invocked allready by itemStateChangePaymentType()
        itemStateSalarySscBaseChange(false);

        mbUpdatingForm = false;
        jbImagePhotoView.setEnabled(false);
        jbImagePhotoRemove.setEnabled(false);
        jbImageSignatureView.setEnabled(false);
        jbImageSignatureRemove.setEnabled(false);
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbFkCatalogueSexTypeId, SModConsts.HRSS_TP_HRS_CAT, SModSysConsts.HRSS_CL_HRS_CAT_SEX);
        SFormUtilities.populateComboBox(miClient, jcbFkCatalogueBloodTypeTypeId, SModConsts.HRSS_TP_HRS_CAT, SModSysConsts.HRSS_CL_HRS_CAT_BLO);
        SFormUtilities.populateComboBox(miClient, jcbFkCatalogueMaritalStatusTypeId, SModConsts.HRSS_TP_HRS_CAT, SModSysConsts.HRSS_CL_HRS_CAT_MAR);
        SFormUtilities.populateComboBox(miClient, jcbFkCatalogueEducationTypeId, SModConsts.HRSS_TP_HRS_CAT, SModSysConsts.HRSS_CL_HRS_CAT_EDU);
        SFormUtilities.populateComboBox(miClient, jcbFkBank_n, SModConsts.HRSS_BANK);
        SFormUtilities.populateComboBox(miClient, jcbFkPaymentType, SModConsts.HRSS_TP_PAY);
        SFormUtilities.populateComboBox(miClient, jcbFkSalaryType, SModConsts.HRSS_TP_SAL);
        SFormUtilities.populateComboBox(miClient, jcbFkEmployeeType, SModConsts.HRSU_TP_EMP);
        SFormUtilities.populateComboBox(miClient, jcbFkWorkerType, SModConsts.HRSU_TP_WRK);
        SFormUtilities.populateComboBox(miClient, jcbFkDepartment, SModConsts.HRSU_DEP);
        //SFormUtilities.populateComboBox(miClient, jcbFkPosition, SModConsts.HRSU_POS);
        SFormUtilities.populateComboBox(miClient, jcbFkShift, SModConsts.HRSU_SHT);
        SFormUtilities.populateComboBox(miClient, jcbFkWorkingDayType, SModConsts.HRSS_TP_WORK_DAY);
        SFormUtilities.populateComboBox(miClient, jcbFkContractType, SModConsts.HRSS_TP_CON);
        SFormUtilities.populateComboBox(miClient, jcbFkRecruitmentSchemeType, SModConsts.HRSS_TP_REC_SCHE);
        SFormUtilities.populateComboBox(miClient, jcbFkPositionRiskType, SModConsts.HRSS_TP_POS_RISK);
        SFormUtilities.populateComboBox(miClient, jcbFkMwzType, SModConsts.HRSU_TP_MWZ);
        moPanelBizPartnerBranchAddress.formRefreshCatalogues();
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        String msg = "";
        Object[] paramsValidation = null;
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                break;
            }
        }
        
        try {
            if (!validation.getIsError()) {

                paramsValidation = new Object[] { moBizPartner == null ? SLibConsts.UNDEFINED : moBizPartner.getPkBizPartnerId(), composeName() };
                if (SDataUtilities.callProcedureVal(miClient, SProcConstants.BPSU_BP, paramsValidation, SLibConstants.EXEC_MODE_VERBOSE) > 0) {
                    if (miClient.showMsgBoxConfirm("El valor del campo '" + jtfBizPartner_Ro.getToolTipText() + "' ya existe, ¿desea conservalo?") == JOptionPane.NO_OPTION) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + jtfBizPartner_Ro.getToolTipText() + "'.");
                        validation.setComponent(jtfFirstName);
                    }
                }

                if (!validation.getIsError()) {
                    paramsValidation = new Object[] { moBizPartner == null ? SLibConsts.UNDEFINED : moBizPartner.getPkBizPartnerId(), moFieldFiscalId.getString() };
                    if (SDataUtilities.callProcedureVal(miClient, SProcConstants.BPSU_BP_FISCAL_ID, paramsValidation, SLibConstants.EXEC_MODE_VERBOSE) > 0) {
                        if (miClient.showMsgBoxConfirm("El valor del campo '" + jlFiscalId.getText() + "' ya existe, ¿desea conservalo?") == JOptionPane.NO_OPTION) {
                            validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + jlFiscalId.getText() + "'.");
                            validation.setComponent(jtfFiscalId);
                        }
                    }

                    if (!validation.getIsError()) {
                        if (!SLibUtils.belongsTo(moFieldFkRecruitmentSchemeType.getKeyAsIntArray()[0],
                                new int[] { SModSysConsts.HRSS_TP_REC_SCHE_ASS_COO, SModSysConsts.HRSS_TP_REC_SCHE_ASS_CIV, SModSysConsts.HRSS_TP_REC_SCHE_ASS_BRD, 
                                    SModSysConsts.HRSS_TP_REC_SCHE_ASS_SAL, SModSysConsts.HRSS_TP_REC_SCHE_ASS_PRO, SModSysConsts.HRSS_TP_REC_SCHE_ASS_SHA, SModSysConsts.HRSS_TP_REC_SCHE_ASS_OTH })) {
                            if (moFieldSocialSecurityNumber.getString().length() < 11) {
                                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + jlSocialSecurityNumber.getText() + "'.");
                                validation.setComponent(jtfSocialSecurityNumber);                                
                            }
                            else if (moFieldSalarySscBase.getDouble() == 0) {
                                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + jlSalarySscBase.getText() + "'.");
                                validation.setComponent(jtfSalarySscBase);                                
                            }
                            else if (moFieldDateChangeSalarySscBase.getDate() == null) {
                                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + jlSalarySscBase.getText() + "' (fecha).");
                                validation.setComponent(jftDateChangeSalarySscBase);                                
                            }
                        }
                    }
                    
                    if (!validation.getIsError()) {
                        if (!moFieldEmail.getString().isEmpty()) {
                            msg = SLibUtilities.validateEmail(moFieldEmail.getString());
                            if (!msg.isEmpty()) {
                                validation.setMessage(msg);
                                validation.setComponent(jtfEmail);
                            }
                        }
                    }
                }

                if (!validation.getIsError()) {
                    validation = moPanelBizPartnerBranchAddress.formValidate();
                    jTabbedPane1.setSelectedIndex(1);
                }

                if (!validation.getIsError()) {
                    String sDateRfc = moFieldFiscalId.getString().substring(4, 10);
                    int nYearRfc = Integer.parseInt(sDateRfc.substring(0, 2)) + SHrsConsts.YEAR_MAX_BIRTH > SLibTimeUtilities.digestYear(miClient.getSessionXXX().getSystemDate())[0] ? 
                            Integer.parseInt(sDateRfc.substring(0, 2)) + SHrsConsts.YEAR_MIN_BIRTH : Integer.parseInt(sDateRfc.substring(0, 2)) + SHrsConsts.YEAR_MAX_BIRTH;

                    Date tDateRfc = SLibTimeUtilities.createDate(nYearRfc, Integer.parseInt(sDateRfc.substring(2, 4)), Integer.parseInt(sDateRfc.substring(4, 6)));

                    if (tDateRfc.compareTo(moFieldDateBirth.getDate()) != 0) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + jlDateBirth.getText() + "'\n " +
                                "La fecha de nacimiento (" + miClient.getSessionXXX().getFormatters().getDateFormat().format(moFieldDateBirth.getDate()) + ") no corresponde a la " +
                                "fecha del RFC (" + miClient.getSessionXXX().getFormatters().getDateFormat().format(tDateRfc) + ").");
                        jTabbedPane1.setSelectedIndex(1);
                        validation.setComponent(jftDateBirth);
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
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

        moFieldFirstName.setFieldValue(moBizPartner.getFirstname());
        moFieldLastName.setFieldValue(moBizPartner.getLastname());
        computeBizPartner_Ro();
        moFieldFiscalId.setFieldValue(moBizPartner.getFiscalId());
        moFieldAlternativeId.setFieldValue(moBizPartner.getAlternativeId());
        moFieldIsDeleted.setFieldValue(moBizPartner.getIsDeleted());

        if (!moBizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchContacts().isEmpty()) {
            moFieldEmail.setFieldValue(moBizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(0).getEmail01());
        }

        jtfPkBizPartnerId_Ro.setText("" + moBizPartner.getPkBizPartnerId());

        moEmployee = moBizPartner.getDbmsDataEmployee();

        if (moEmployee != null) {
            moFieldNumber.setFieldValue(moEmployee.getNumber());
            moFieldSocialSecurityNumber.setFieldValue(moEmployee.getSocialSecurityNumber());
            moXtaImageIconPhoto_n = moEmployee.getXtaImageIconPhoto_n();
            moXtaImageIconSignature_n = moEmployee.getXtaImageIconSignature_n();
            moFieldFileImagePhoto.setFieldValue("");
            moFieldFileImageSignature.setFieldValue("");
            moFieldFkCatalogueSexType.setFieldValue(new int[] { moEmployee.getFkCatalogueSexCategoryId(), moEmployee.getFkCatalogueSexTypeId() });
            moFieldFkCatalogueBloodTypeType.setFieldValue(new int[] { moEmployee.getFkCatalogueBloodTypeCategoryId(), moEmployee.getFkCatalogueBloodTypeTypeId() });
            moFieldFkCatalogueMaritalStatusType.setFieldValue(new int[] { moEmployee.getFkCatalogueMaritalStatusCategoryId(), moEmployee.getFkCatalogueMaritalStatusTypeId() });
            moFieldFkCatalogueEducationType.setFieldValue(new int[] { moEmployee.getFkCatalogueEducationCategoryId(), moEmployee.getFkCatalogueEducationTypeId() });
            moFieldFkBank_n.setFieldValue(new int[] { moEmployee.getFkBankId_n() });
            moFieldBankAccount.setFieldValue(moEmployee.getBankAccount());
            moFieldIsUnionized.setFieldValue(moEmployee.isUnionized());
            moFieldIsMfgOperator.setFieldValue(moEmployee.isMfgOperator());
            moFieldIsActive.setFieldValue(moEmployee.isActive());
            moFieldDateBirth.setFieldValue(moEmployee.getDateBirth());
            moFieldDateBenefits.setFieldValue(moEmployee.getDateBenefits());
            moFieldDateLastHire.setFieldValue(moEmployee.getDateLastHire());
            moFieldDateLastDismiss_n.setFieldValue(moEmployee.getDateLastDismiss_n());
            moFieldFkPaymentType.setFieldValue(new int[] { moEmployee.getFkPaymentTypeId() });
            moFieldFkSalaryType.setFieldValue(new int[] { moEmployee.getFkSalaryTypeId() });

            moFieldSalary.setFieldValue(moEmployee.getSalary());
            moFieldDateChangeSalary.setFieldValue(moEmployee.getDateSalary());
            moFieldWage.setFieldValue(moEmployee.getWage());
            moFieldDateChangeWage.setFieldValue(moEmployee.getDateWage());
            moFieldSalarySscBase.setFieldValue(moEmployee.getSalarySscBase());
            moFieldDateChangeSalarySscBase.setFieldValue(moEmployee.getDateSalarySscBase());

            moFieldFkEmployeeType.setFieldValue(new int[] { moEmployee.getFkEmployeeTypeId() });
            moFieldFkWorkerType.setFieldValue(new int[] { moEmployee.getFkWorkerTypeId() });
            moFieldFkDepartment.setFieldValue(new int[] { moEmployee.getFkDepartmentId() });
            moFieldFkPosition.setFieldValue(new int[] { moEmployee.getFkPositionId() });
            moFieldFkShift.setFieldValue(new int[] { moEmployee.getFkShiftId() });
            moFieldFkWorkingDayType.setFieldValue(new int[] { moEmployee.getFkWorkingDayTypeId()});
            moFieldWorkingHoursDay.setFieldValue(moEmployee.getWorkingHoursDay());
            moFieldFkContractType.setFieldValue(new int[] { moEmployee.getFkContractTypeId() });
            moFieldFkRecruitmentSchemeType.setFieldValue(new int[] { moEmployee.getFkRecruitmentSchemeTypeId() });
            moFieldFkPositionRiskType.setFieldValue(new int[] { moEmployee.getFkPositionRiskTypeId() });
            moFieldFkMwzType.setFieldValue(new int[] { moEmployee.getFkMwzTypeId() });
        }
        moBizPartnerBranch = moBizPartner.getDbmsHqBranch();
        moPanelBizPartnerBranchAddress.setRegistry(moBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial());

        if (!moBizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchContacts().isEmpty()) {
            mnPkContactId = moBizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(0).getPkContactId();
            moFieldEmail.setFieldValue(moBizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(0).getEmail01());
        }
        
        jckIsDeleted.setEnabled((moEmployee != null && !moEmployee.isActive()) || moEmployee == null);
        jckChangeSalary.setSelected(false);
        jckChangeWage.setSelected(false);
        jckChangeSalarySscBase.setEnabled(moEmployee != null && moFieldSalarySscBase.getDouble() != 0);
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
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        int paymentType = moFieldFkPaymentType.getKeyAsIntArray()[0];
        String formerBizPartner = "";
        HashSet<Integer> requiredCategories = new HashSet<>();

        if (moBizPartner == null) {
            moBizPartner = new SDataBizPartner();
            moBizPartner.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

            moBizPartner.setFkBizPartnerIdentityTypeId(SModSysConsts.BPSS_TP_BP_IDY_PER);
            moBizPartner.setFkTaxIdentityId(SModSysConsts.BPSS_TP_BP_IDY_PER);      // XXX (sflores, 2015-02-19: check if propper constants must be created)
            moBizPartner.setFkBizAreaId(SDataConstantsSys.BPSU_BA_DEFAULT);
            moBizPartner.setFkFiscalBankId(SModSysConsts.FINS_FISCAL_BANK_NA);

            //moBizPartner.getDbmsBizPartnerBranches().add(createBizPartnerBranch());
        }
        else {
            moBizPartner.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        updateBizPartnerBranch();

        //moBizPartner.setPkBizPartnerId(...);

        formerBizPartner = moBizPartner.getBizPartner();
        moBizPartner.setBizPartner(moFieldLastName.getString() + ", " + moFieldFirstName.getString());
        if (moBizPartner.getBizPartnerCommercial().isEmpty() || formerBizPartner.compareTo(moBizPartner.getBizPartnerCommercial()) == 0) {
            moBizPartner.setBizPartnerCommercial(moBizPartner.getBizPartner());
        }
        moBizPartner.setLastname(moFieldLastName.getString());
        moBizPartner.setFirstname(moFieldFirstName.getString());
        moBizPartner.setFiscalId(moFieldFiscalId.getString());
        //moBizPartner.setFiscalFrgId(...);
        moBizPartner.setAlternativeId(moFieldAlternativeId.getString());
        //moBizPartner.setCodeBankSantander(...);
        //moBizPartner.setCodeBankBanBajio(...);
        //moBizPartner.setWeb(...);

        //moBizPartner.setIsCompany(...);

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
                    }
                    break;
                case SModSysConsts.BPSS_CT_BP_DBR:
                    if (!moBizPartner.getIsDebtor()) {
                        moBizPartner.setIsDebtor(true);
                        moBizPartner.setDbmsCategorySettingsDbr(createBizPartnerCategory(category));
                    }
                    else {
                        moBizPartner.getDbmsCategorySettingsDbr().setIsDeleted(false);
                    }
                    break;
                default:
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
        //moBizPartner.setFkBizPartnerIdentityTypeId(...);  already set above!
        //moBizPartner.setFkTaxIdentityId(...)              already set above!
        //moBizPartner.setFkFiscalBankId(...);              already set above!
        //moBizPartner.setFkBizAreaId(...)                  already set above!

        // Update business partner children data:

        moBizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(0).setEmail01(moFieldEmail.getString());

        if (!moBizPartner.getIsRegistryNew()) {
            moBizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(0).setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moEmployee = moBizPartner.getDbmsDataEmployee();

        if (moEmployee == null) {
            moEmployee = new SDataEmployee();
            moEmployee.setFkUserInsertId(miClient.getSession().getUser().getPkUserId());

            moBizPartner.setDbmsDataEmployee(moEmployee);
        }
        else {
            moEmployee = moBizPartner.getDbmsDataEmployee();
            moEmployee.setFkUserUpdateId(miClient.getSession().getUser().getPkUserId());
        }

        //moEmployee.setPkEmployeeId(...);
        moEmployee.setNumber(moFieldNumber.getString());
        moEmployee.setSocialSecurityNumber(moFieldSocialSecurityNumber.getString());
        moEmployee.setDateBirth(moFieldDateBirth.getDate());
        moEmployee.setDateBenefits(moFieldDateBenefits.getDate());
        moEmployee.setDateLastHire(moFieldDateLastHire.getDate());
        moEmployee.setDateLastDismiss_n(moFieldDateLastDismiss_n.getDate());
        
        if (paymentType == SModSysConsts.HRSS_TP_PAY_WEE) {
            moEmployee.setSalary(moFieldSalary.getDouble());
            moEmployee.setWage(0d);
            moEmployee.setDateSalary(moFieldDateChangeSalary.getDate());
            moEmployee.setDateWage(moFieldDateChangeSalary.getDate());
        }
        else {
            moEmployee.setSalary(0d);
            moEmployee.setWage(moFieldWage.getDouble());
            moEmployee.setDateSalary(moFieldDateChangeWage.getDate());
            moEmployee.setDateWage(moFieldDateChangeWage.getDate());
        }
        
        moEmployee.setSalarySscBase(moFieldSalarySscBase.getDouble());
        moEmployee.setDateSalarySscBase(moFieldDateChangeSalarySscBase.getDate() == null ? miClient.getSession().getCurrentDate() : moFieldDateChangeSalarySscBase.getDate());
        
        moEmployee.setWorkingHoursDay(moFieldWorkingHoursDay.getInteger());
        moEmployee.setBankAccount(moFieldBankAccount.getString());
        
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
        moEmployee.setFkRecruitmentSchemeTypeId(moFieldFkRecruitmentSchemeType.getKeyAsIntArray()[0]);
        moEmployee.setFkPositionRiskTypeId(moFieldFkPositionRiskType.getKeyAsIntArray()[0]);
        moEmployee.setFkCatalogueSexCategoryId(moFieldFkCatalogueSexType.getKeyAsIntArray()[0]);
        moEmployee.setFkCatalogueSexTypeId(moFieldFkCatalogueSexType.getKeyAsIntArray()[1]);
        moEmployee.setFkCatalogueBloodTypeCategoryId(moFieldFkCatalogueBloodTypeType.getKeyAsIntArray()[0]);
        moEmployee.setFkCatalogueBloodTypeTypeId(moFieldFkCatalogueBloodTypeType.getKeyAsIntArray()[1]);
        moEmployee.setFkCatalogueMaritalStatusCategoryId(moFieldFkCatalogueMaritalStatusType.getKeyAsIntArray()[0]);
        moEmployee.setFkCatalogueMaritalStatusTypeId(moFieldFkCatalogueMaritalStatusType.getKeyAsIntArray()[1]);
        moEmployee.setFkCatalogueEducationCategoryId(moFieldFkCatalogueEducationType.getKeyAsIntArray()[0]);
        moEmployee.setFkCatalogueEducationTypeId(moFieldFkCatalogueEducationType.getKeyAsIntArray()[1]);
        moEmployee.setFkBankId_n(moFieldFkBank_n.getKeyAsIntArray()[0]);

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
                }
            }
        }
    }
}
