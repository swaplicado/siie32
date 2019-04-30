/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbDeduction;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiFieldKeyGroup;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFieldBoolean;
import sa.lib.gui.bean.SBeanFieldKey;
import sa.lib.gui.bean.SBeanForm;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SFormDeduction extends SBeanForm implements ItemListener {

    private SDbDeduction moRegistry;
    private SGuiFieldKeyGroup moFieldKeyGroup;

    /**
     * Creates new form SFormDeduction.
     * @param client
     * @param title 
     */
    public SFormDeduction(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.HRS_DED, SLibConsts.UNDEFINED, title);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlCode = new javax.swing.JLabel();
        moTextCode = new sa.lib.gui.bean.SBeanFieldText();
        jPanel4 = new javax.swing.JPanel();
        jlName = new javax.swing.JLabel();
        moTextName = new sa.lib.gui.bean.SBeanFieldText();
        jPanel22 = new javax.swing.JPanel();
        jlNameAbbreviated = new javax.swing.JLabel();
        moTextNameAbbreviated = new sa.lib.gui.bean.SBeanFieldText();
        jlNameAbbreviatedHelp = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jlDeductionComputationType = new javax.swing.JLabel();
        moKeyDeductionComputationType = new sa.lib.gui.bean.SBeanFieldKey();
        jlDeductionComputationTypeHelp = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jlRetPercentage = new javax.swing.JLabel();
        moDecRetPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlRetPercentageHelp = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        moBoolLoan = new sa.lib.gui.bean.SBeanFieldBoolean();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jlDeductionType = new javax.swing.JLabel();
        moKeyDeductionType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel7 = new javax.swing.JPanel();
        jlLoanType = new javax.swing.JLabel();
        moKeyLoanType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel19 = new javax.swing.JPanel();
        jlBenefitType = new javax.swing.JLabel();
        moKeyBenefitType = new sa.lib.gui.bean.SBeanFieldKey();
        jlBenefitTypeHelp = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jlAccountingConfigurationType = new javax.swing.JLabel();
        moKeyAccountingConfigurationType = new sa.lib.gui.bean.SBeanFieldKey();
        jlAccountingConfigurationTypeHelp = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jlAccountingRecordType = new javax.swing.JLabel();
        moKeyAccountingRecordType = new sa.lib.gui.bean.SBeanFieldKey();
        jlAccountingRecordTypeHelp = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jlAbsenceClass = new javax.swing.JLabel();
        moKeyAbsenceClass = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel20 = new javax.swing.JPanel();
        jlAbsenceType = new javax.swing.JLabel();
        moKeyAbsenceType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel15 = new javax.swing.JPanel();
        moBoolWithholding = new sa.lib.gui.bean.SBeanFieldBoolean();
        jPanel16 = new javax.swing.JPanel();
        moBoolPayrollTax = new sa.lib.gui.bean.SBeanFieldBoolean();
        jPanel24 = new javax.swing.JPanel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCode.setText("Código:*");
        jlCode.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel6.add(jlCode);

        moTextCode.setText("sBeanFieldText2");
        jPanel6.add(moTextCode);

        jPanel2.add(jPanel6);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlName.setText("Nombre:*");
        jlName.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel4.add(jlName);

        moTextName.setText("sBeanFieldText1");
        moTextName.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel4.add(moTextName);

        jPanel2.add(jPanel4);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNameAbbreviated.setText("Nombre corto:*");
        jlNameAbbreviated.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel22.add(jlNameAbbreviated);

        moTextNameAbbreviated.setText("sBeanFieldText1");
        moTextNameAbbreviated.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel22.add(moTextNameAbbreviated);

        jlNameAbbreviatedHelp.setForeground(java.awt.Color.gray);
        jlNameAbbreviatedHelp.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlNameAbbreviatedHelp.setText("para contabilización y para concepto del CFDI (XML)");
        jlNameAbbreviatedHelp.setToolTipText("");
        jlNameAbbreviatedHelp.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel22.add(jlNameAbbreviatedHelp);

        jPanel2.add(jPanel22);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDeductionComputationType.setText("Tipo cálculo deducción:*");
        jlDeductionComputationType.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel8.add(jlDeductionComputationType);

        moKeyDeductionComputationType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel8.add(moKeyDeductionComputationType);

        jlDeductionComputationTypeHelp.setForeground(java.awt.Color.gray);
        jlDeductionComputationTypeHelp.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlDeductionComputationTypeHelp.setText("ayuda...");
        jlDeductionComputationTypeHelp.setToolTipText("");
        jlDeductionComputationTypeHelp.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel8.add(jlDeductionComputationTypeHelp);

        jPanel2.add(jPanel8);

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRetPercentage.setText("Porcentaje retención:");
        jlRetPercentage.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel23.add(jlRetPercentage);
        jPanel23.add(moDecRetPercentage);

        jlRetPercentageHelp.setForeground(java.awt.Color.gray);
        jlRetPercentageHelp.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlRetPercentageHelp.setText("(Porcentaje de la retención en función del tipo de cálculo de deducción elegido)");
        jlRetPercentageHelp.setToolTipText("");
        jlRetPercentageHelp.setPreferredSize(new java.awt.Dimension(500, 23));
        jPanel23.add(jlRetPercentageHelp);

        jPanel2.add(jPanel23);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolLoan.setText("Es crédito/préstamo");
        moBoolLoan.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel3.add(moBoolLoan);

        jPanel2.add(jPanel3);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel13.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDeductionType.setText("Tipo deducción:*");
        jlDeductionType.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel5.add(jlDeductionType);

        moKeyDeductionType.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel5.add(moKeyDeductionType);

        jPanel13.add(jPanel5);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLoanType.setText("Tipo crédito/préstamo:*");
        jlLoanType.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel7.add(jlLoanType);

        moKeyLoanType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel7.add(moKeyLoanType);

        jPanel13.add(jPanel7);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBenefitType.setText("Tipo prestación:*");
        jlBenefitType.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel19.add(jlBenefitType);

        moKeyBenefitType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel19.add(moKeyBenefitType);

        jlBenefitTypeHelp.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel19.add(jlBenefitTypeHelp);

        jPanel13.add(jPanel19);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAccountingConfigurationType.setText("Tipo configuración contable:*");
        jlAccountingConfigurationType.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel17.add(jlAccountingConfigurationType);

        moKeyAccountingConfigurationType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel17.add(moKeyAccountingConfigurationType);

        jlAccountingConfigurationTypeHelp.setForeground(java.awt.Color.gray);
        jlAccountingConfigurationTypeHelp.setText("para configurar cuentas contables");
        jlAccountingConfigurationTypeHelp.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel17.add(jlAccountingConfigurationTypeHelp);

        jPanel13.add(jPanel17);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAccountingRecordType.setText("Tipo registro contable:*");
        jlAccountingRecordType.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel18.add(jlAccountingRecordType);

        moKeyAccountingRecordType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel18.add(moKeyAccountingRecordType);

        jlAccountingRecordTypeHelp.setForeground(java.awt.Color.gray);
        jlAccountingRecordTypeHelp.setText("para agrupar asientos contables");
        jlAccountingRecordTypeHelp.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel18.add(jlAccountingRecordTypeHelp);

        jPanel13.add(jPanel18);

        jPanel11.add(jPanel13, java.awt.BorderLayout.NORTH);

        jPanel10.add(jPanel11, java.awt.BorderLayout.CENTER);

        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel14.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAbsenceClass.setText("Clase incidencia:");
        jlAbsenceClass.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel21.add(jlAbsenceClass);

        moKeyAbsenceClass.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel21.add(moKeyAbsenceClass);

        jPanel14.add(jPanel21);

        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAbsenceType.setText("Tipo incidencia:");
        jlAbsenceType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel20.add(jlAbsenceType);

        moKeyAbsenceType.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel20.add(moKeyAbsenceType);

        jPanel14.add(jPanel20);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolWithholding.setText("Es retención de ley");
        moBoolWithholding.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel15.add(moBoolWithholding);

        jPanel14.add(jPanel15);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolPayrollTax.setText("Aplica para impto. sobre nóminas");
        moBoolPayrollTax.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel16.add(moBoolPayrollTax);

        jPanel14.add(jPanel16);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel14.add(jPanel24);

        jPanel12.add(jPanel14, java.awt.BorderLayout.NORTH);

        jPanel10.add(jPanel12, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanel10, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel jlAbsenceClass;
    private javax.swing.JLabel jlAbsenceType;
    private javax.swing.JLabel jlAccountingConfigurationType;
    private javax.swing.JLabel jlAccountingConfigurationTypeHelp;
    private javax.swing.JLabel jlAccountingRecordType;
    private javax.swing.JLabel jlAccountingRecordTypeHelp;
    private javax.swing.JLabel jlBenefitType;
    private javax.swing.JLabel jlBenefitTypeHelp;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlDeductionComputationType;
    private javax.swing.JLabel jlDeductionComputationTypeHelp;
    private javax.swing.JLabel jlDeductionType;
    private javax.swing.JLabel jlLoanType;
    private javax.swing.JLabel jlName;
    private javax.swing.JLabel jlNameAbbreviated;
    private javax.swing.JLabel jlNameAbbreviatedHelp;
    private javax.swing.JLabel jlRetPercentage;
    private javax.swing.JLabel jlRetPercentageHelp;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolLoan;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolPayrollTax;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolWithholding;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecRetPercentage;
    private sa.lib.gui.bean.SBeanFieldKey moKeyAbsenceClass;
    private sa.lib.gui.bean.SBeanFieldKey moKeyAbsenceType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyAccountingConfigurationType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyAccountingRecordType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBenefitType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDeductionComputationType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDeductionType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyLoanType;
    private sa.lib.gui.bean.SBeanFieldText moTextCode;
    private sa.lib.gui.bean.SBeanFieldText moTextName;
    private sa.lib.gui.bean.SBeanFieldText moTextNameAbbreviated;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 960, 600);

        moFieldKeyGroup = new SGuiFieldKeyGroup(miClient);
        moFieldKeyGroup.addFieldKey(moKeyAbsenceClass, SModConsts.HRSU_CL_ABS, SLibConsts.UNDEFINED, null);
        moFieldKeyGroup.addFieldKey(moKeyAbsenceType, SModConsts.HRSU_TP_ABS, SLibConsts.UNDEFINED, null);

        moTextCode.setTextSettings(SGuiUtils.getLabelName(jlCode.getText()), 10);
        moTextName.setTextSettings(SGuiUtils.getLabelName(jlName.getText()), 100);
        moTextNameAbbreviated.setTextSettings(SGuiUtils.getLabelName(jlNameAbbreviated.getText()), 25);
        moKeyDeductionType.setKeySettings(miClient, SGuiUtils.getLabelName(jlDeductionType.getText()), true);
        moKeyDeductionComputationType.setKeySettings(miClient, SGuiUtils.getLabelName(jlDeductionComputationType.getText()), true);
        moDecRetPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlRetPercentage.getText()), SGuiConsts.GUI_TYPE_DEC_PER_DISC, true);
        moBoolWithholding.setBooleanSettings(SGuiUtils.getLabelName(moBoolWithholding.getText()), false);
        moBoolPayrollTax.setBooleanSettings(SGuiUtils.getLabelName(moBoolPayrollTax.getText()), false);
        moBoolLoan.setBooleanSettings(SGuiUtils.getLabelName(moBoolLoan.getText()), false);
        moKeyLoanType.setKeySettings(miClient, SGuiUtils.getLabelName(jlLoanType.getText()), true);
        moKeyBenefitType.setKeySettings(miClient, SGuiUtils.getLabelName(jlBenefitType.getText()), true);
        moKeyAccountingConfigurationType.setKeySettings(miClient, SGuiUtils.getLabelName(jlAccountingConfigurationType.getText()), true);
        moKeyAccountingRecordType.setKeySettings(miClient, SGuiUtils.getLabelName(jlAccountingRecordType.getText()), true);
        moKeyAbsenceClass.setKeySettings(miClient, SGuiUtils.getLabelName(jlAbsenceClass.getText()), false);
        moKeyAbsenceType.setKeySettings(miClient, SGuiUtils.getLabelName(jlAbsenceType.getText()), false);

        moFields.addField(moTextCode);
        moFields.addField(moTextName);
        moFields.addField(moTextNameAbbreviated);
        moFields.addField(moKeyDeductionType);
        moFields.addField(moKeyDeductionComputationType);
        moFields.addField(moDecRetPercentage);
        moFields.addField(moBoolWithholding);
        moFields.addField(moBoolPayrollTax);
        moFields.addField(moBoolLoan);
        moFields.addField(moKeyLoanType);
        moFields.addField(moKeyBenefitType);
        moFields.addField(moKeyAccountingConfigurationType);
        moFields.addField(moKeyAccountingRecordType);
        moFields.addField(moKeyAbsenceClass);
        moFields.addField(moKeyAbsenceType);

        moFields.setFormButton(jbSave);
    }

    private void showDeductionComputationTypeHelp() {
        if (moKeyDeductionComputationType.getSelectedIndex() <= 0) {
            jlDeductionComputationTypeHelp.setText(SGuiConsts.TXT_BTN_SELECT + " " + SGuiUtils.getLabelName(jlDeductionComputationType).toLowerCase() + "...");
        }
        else {
            switch (moKeyDeductionComputationType.getValue()[0]) {
                case SModSysConsts.HRSS_TP_DED_COMP_AMT:
                    jlDeductionComputationTypeHelp.setText("Monto directo");
                    break;
                case SModSysConsts.HRSS_TP_DED_COMP_PCT_INCOME:
                    jlDeductionComputationTypeHelp.setText("En función de un porcentaje sobre el monto de sueldos y salarios");
                    break;
                default:
                    jlDeductionComputationTypeHelp.setText("?");
            }
        }
    }

    private void itemStateChangedLoan() {
        boolean enable = moBoolLoan.getValue();
        moKeyLoanType.setEnabled(enable);
        if (!enable) {
            moKeyLoanType.setValue(new int[] { SModSysConsts.HRSS_TP_LOAN_NON });
        }
    }

    private void itemStateChangedDeductionComputationType() {
        if (moKeyDeductionComputationType.getSelectedIndex() <= 0) {
            moDecRetPercentage.setEnabled(false);
        }
        else {
            moDecRetPercentage.setEnabled(moKeyDeductionComputationType.getValue()[0] == SModSysConsts.HRSS_TP_DED_COMP_PCT_INCOME);
        }
        
        if (!moDecRetPercentage.isEnabled()) {
            moDecRetPercentage.resetField();
        }
        
        showDeductionComputationTypeHelp();
    }

    @Override
    public void addAllListeners() {
        moBoolLoan.addItemListener(this);
        moKeyDeductionComputationType.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        moBoolLoan.removeItemListener(this);
        moKeyDeductionComputationType.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyDeductionType, SModConsts.HRSS_TP_DED, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyDeductionComputationType, SModConsts.HRSS_TP_DED_COMP, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyLoanType, SModConsts.HRSS_TP_LOAN, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyBenefitType, SModConsts.HRSS_TP_BEN, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyAccountingConfigurationType, SModConsts.HRSS_TP_ACC, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyAccountingRecordType, SModConsts.HRSS_TP_ACC, SLibConsts.UNDEFINED, null);
        moFieldKeyGroup.populateCatalogues();
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbDeduction) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            boolean isBeingCopied = moRegistry.getPkDeductionId() != 0;
            moRegistry.initPrimaryKey();
            jtfRegistryKey.setText("");

            if (!isBeingCopied) {
                moRegistry.setFkDeductionComputationTypeId(SModSysConsts.HRSS_TP_DED_COMP_AMT);
                moRegistry.setFkLoanTypeId(SModSysConsts.HRSS_TP_LOAN_NON);
                moRegistry.setFkBenefitTypeId(SModSysConsts.HRSS_TP_BEN_NON);
            }
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moTextCode.setValue(moRegistry.getCode());
        moTextName.setValue(moRegistry.getName());
        moTextNameAbbreviated.setValue(moRegistry.getNameAbbreviated());
        moKeyDeductionType.setValue(new int[] { moRegistry.getFkDeductionTypeId() });
        moKeyDeductionComputationType.setValue(new int[] { moRegistry.getFkDeductionComputationTypeId() });
        moDecRetPercentage.setValue(moRegistry.getRetPercentage());
        moBoolWithholding.setValue(moRegistry.isWithholding());
        moBoolPayrollTax.setValue(moRegistry.isPayrollTax());
        moBoolLoan.setValue(moRegistry.isLoan());
        moKeyLoanType.setValue(new int[] { moRegistry.getFkLoanTypeId() });
        moKeyBenefitType.setValue(new int[] { moRegistry.getFkBenefitTypeId() });
        moKeyAccountingConfigurationType.setValue(new int[] { moRegistry.getFkAccountingConfigurationTypeId() });
        moKeyAccountingRecordType.setValue(new int[] { moRegistry.getFkAccountingRecordTypeId() });
        moKeyAbsenceClass.setValue(new int[] { moRegistry.getFkAbsenceClassId_n() });
        moKeyAbsenceType.setValue(new int[] { moRegistry.getFkAbsenceClassId_n(), moRegistry.getFkAbsenceTypeId_n() });

        setFormEditable(true);
        moBoolPayrollTax.setEditable(false);

        if (moRegistry.isRegistryNew()) {

        }

        itemStateChangedLoan();
        itemStateChangedDeductionComputationType();

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbDeduction registry = moRegistry.clone();

        if (registry.isRegistryNew()) {}

        registry.setCode(moTextCode.getValue());
        registry.setName(moTextName.getValue());
        registry.setNameAbbreviated(moTextNameAbbreviated.getValue());
        registry.setRetPercentage(moDecRetPercentage.getValue());
        registry.setFkDeductionTypeId(moKeyDeductionType.getValue()[0]);
        registry.setFkDeductionComputationTypeId(moKeyDeductionComputationType.getValue()[0]);
        registry.setWithholding(moBoolWithholding.getValue());
        registry.setPayrollTax(moBoolPayrollTax.getValue());
        registry.setFkLoanTypeId(moKeyLoanType.getValue()[0]);
        registry.setFkBenefitTypeId(moKeyBenefitType.getValue()[0]);
        registry.setFkAccountingConfigurationTypeId(moKeyAccountingConfigurationType.getValue()[0]);
        registry.setFkAccountingRecordTypeId(moKeyAccountingRecordType.getValue()[0]);
        registry.setFkAbsenceClassId_n(moKeyAbsenceClass.getSelectedIndex() == 0 || moKeyAbsenceType.getSelectedIndex() == 0 ? SLibConsts.UNDEFINED : moKeyAbsenceClass.getValue()[0]);
        registry.setFkAbsenceTypeId_n(moKeyAbsenceClass.getSelectedIndex() == 0 || moKeyAbsenceType.getSelectedIndex() == 0 ? SLibConsts.UNDEFINED : moKeyAbsenceType.getValue()[1]);

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (moBoolLoan.getValue() && moKeyLoanType.getValue()[0] <= SModSysConsts.HRSS_TP_LOAN_NON) { // should not be less, just in case
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + SGuiUtils.getLabelName(jlLoanType) + "'.");
                validation.setComponent(moKeyLoanType);
            }
            else if (moKeyAccountingConfigurationType.getValue()[0] == SModSysConsts.HRSS_TP_ACC_GBL &&
                    !SLibUtils.belongsTo(moKeyAccountingRecordType.getValue()[0], new int[] { SModSysConsts.HRSS_TP_ACC_GBL, SModSysConsts.HRSS_TP_ACC_DEP, SModSysConsts.HRSS_TP_ACC_EMP })) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + SGuiUtils.getLabelName(jlAccountingRecordType.getText()) + "'.");
                validation.setComponent(moKeyAccountingRecordType);
            }
            else if (moKeyAccountingConfigurationType.getValue()[0] == SModSysConsts.HRSS_TP_ACC_DEP &&
                    !SLibUtils.belongsTo(moKeyAccountingRecordType.getValue()[0], new int[] { SModSysConsts.HRSS_TP_ACC_DEP, SModSysConsts.HRSS_TP_ACC_EMP })) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + SGuiUtils.getLabelName(jlAccountingRecordType.getText()) + "'.");
                validation.setComponent(moKeyAccountingRecordType);
            }
            else if (moKeyAccountingConfigurationType.getValue()[0] == SModSysConsts.HRSS_TP_ACC_EMP &&
                    !SLibUtils.belongsTo(moKeyAccountingRecordType.getValue()[0], new int[] { SModSysConsts.HRSS_TP_ACC_EMP })) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + SGuiUtils.getLabelName(jlAccountingRecordType.getText()) + "'.");
                validation.setComponent(moKeyAccountingRecordType);
            }
        }
        return validation;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof SBeanFieldKey && e.getStateChange() == ItemEvent.SELECTED) {
            SBeanFieldKey field = (SBeanFieldKey) e.getSource();
            
            if (field == moKeyDeductionComputationType) {
                itemStateChangedDeductionComputationType();
            }
        }
        else if (e.getSource() instanceof SBeanFieldBoolean && e.getStateChange() == ItemEvent.SELECTED) {
            SBeanFieldBoolean field = (SBeanFieldBoolean) e.getSource();

            if (field == moBoolLoan) {
                itemStateChangedLoan();
            }
        }
    }
}
