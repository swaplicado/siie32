/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbLoan;
import erp.mod.hrs.db.SHrsConsts;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFieldDecimal;
import sa.lib.gui.bean.SBeanFieldRadio;
import sa.lib.gui.bean.SBeanForm;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SFormLoan extends SBeanForm implements ItemListener, ChangeListener, FocusListener {
    
    private SDbLoan moRegistry;

    /**
     * Creates new form SFormLoan
     * @param client
     * @param title
     */
    public SFormLoan(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.HRS_LOAN, SLibConsts.UNDEFINED, title);
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

        moRadGroupSalaryType = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jlEmployee = new javax.swing.JLabel();
        moKeyEmployee = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel7 = new javax.swing.JPanel();
        jlLoanType = new javax.swing.JLabel();
        moKeyLoanType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel12 = new javax.swing.JPanel();
        jlLoanPaymentType = new javax.swing.JLabel();
        moKeyLoanPaymentType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel6 = new javax.swing.JPanel();
        jlNumber = new javax.swing.JLabel();
        moTextNumber = new sa.lib.gui.bean.SBeanFieldText();
        jPanel4 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel5 = new javax.swing.JPanel();
        jlDateEnd_n = new javax.swing.JLabel();
        moDateDateEnd_n = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel3 = new javax.swing.JPanel();
        jlCapital = new javax.swing.JLabel();
        moDecCapital = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel8 = new javax.swing.JPanel();
        jlTotalAmount = new javax.swing.JLabel();
        moDecTotalAmount = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel9 = new javax.swing.JPanel();
        jlPaymentAmount = new javax.swing.JLabel();
        moDecPaymentAmount = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlPaymentAmountHelpHint = new javax.swing.JLabel();
        jlPaymentAmountHelp = new javax.swing.JLabel();
        jlPaymentsNumber = new javax.swing.JLabel();
        moDecPaymentsNumber = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel10 = new javax.swing.JPanel();
        jlPaymentFixedFees = new javax.swing.JLabel();
        moDecPaymentFixedFees = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlPaymentFixedFeesHelpHint = new javax.swing.JLabel();
        jlPaymentFixedFeesHelp = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jlPaymentUmas = new javax.swing.JLabel();
        moDecPaymentUmas = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlPaymentUmasHelpHint = new javax.swing.JLabel();
        jlPaymentUmasHelp = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jlPaymentUmis = new javax.swing.JLabel();
        moDecPaymentUmis = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlPaymentUmisHelpHint = new javax.swing.JLabel();
        jlPaymentUmisHelp = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jlPaymentPercentage = new javax.swing.JLabel();
        moDecPaymentPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlPaymentPercentageHelpHint = new javax.swing.JLabel();
        jlPaymentPercentageHelp = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        moRadPaymentPercentageRefSd = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadPaymentPercentageRefSbc = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadPaymentPercentageRefOtro = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel15 = new javax.swing.JPanel();
        jlRadPaymentPercentageAmount = new javax.swing.JLabel();
        moDecRadPaymentPercentageAmount = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlRadPaymentPercentageAmountHint = new javax.swing.JLabel();
        jlRadPaymentPercentageAmountHelp = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(15, 1, 0, 5));

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlEmployee.setForeground(new java.awt.Color(0, 0, 255));
        jlEmployee.setText("Empleado:*");
        jlEmployee.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel13.add(jlEmployee);

        moKeyEmployee.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel13.add(moKeyEmployee);

        jPanel2.add(jPanel13);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLoanType.setText("Tipo crédito/préstamo:*");
        jlLoanType.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel7.add(jlLoanType);

        moKeyLoanType.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel7.add(moKeyLoanType);

        jPanel2.add(jPanel7);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLoanPaymentType.setText("Tipo pago:*");
        jlLoanPaymentType.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel12.add(jlLoanPaymentType);

        moKeyLoanPaymentType.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel12.add(moKeyLoanPaymentType);

        jPanel2.add(jPanel12);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNumber.setText("Número o folio:*");
        jlNumber.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel6.add(jlNumber);

        moTextNumber.setText("sBeanFieldText2");
        jPanel6.add(moTextNumber);

        jPanel2.add(jPanel6);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial:*");
        jlDateStart.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel4.add(jlDateStart);

        moDateDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(moDateDateStart);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd_n.setText("Fecha final:");
        jlDateEnd_n.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel5.add(jlDateEnd_n);

        moDateDateEnd_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(moDateDateEnd_n);

        jPanel2.add(jPanel5);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCapital.setText("Capital:*");
        jlCapital.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel3.add(jlCapital);
        jPanel3.add(moDecCapital);

        jPanel2.add(jPanel3);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTotalAmount.setText("Total a pagar:*");
        jlTotalAmount.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel8.add(jlTotalAmount);
        jPanel8.add(moDecTotalAmount);

        jPanel2.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPaymentAmount.setText("Monto:*");
        jlPaymentAmount.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel9.add(jlPaymentAmount);
        jPanel9.add(moDecPaymentAmount);

        jlPaymentAmountHelpHint.setForeground(new java.awt.Color(109, 109, 109));
        jlPaymentAmountHelpHint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlPaymentAmountHelpHint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlPaymentAmountHelpHint.setToolTipText("Monto directo a pagar");
        jlPaymentAmountHelpHint.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel9.add(jlPaymentAmountHelpHint);

        jlPaymentAmountHelp.setForeground(java.awt.Color.gray);
        jlPaymentAmountHelp.setText("...");
        jlPaymentAmountHelp.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jlPaymentAmountHelp);

        jlPaymentsNumber.setText("Número de pagos:");
        jlPaymentsNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlPaymentsNumber);
        jPanel9.add(moDecPaymentsNumber);

        jPanel2.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPaymentFixedFees.setText("Número salarios mínimos:*");
        jlPaymentFixedFees.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel10.add(jlPaymentFixedFees);
        jPanel10.add(moDecPaymentFixedFees);

        jlPaymentFixedFeesHelpHint.setForeground(new java.awt.Color(109, 109, 109));
        jlPaymentFixedFeesHelpHint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlPaymentFixedFeesHelpHint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlPaymentFixedFeesHelpHint.setToolTipText("Monto a pagar mensual expresado en número de salarios mínimos del área geográfica de referencia");
        jlPaymentFixedFeesHelpHint.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel10.add(jlPaymentFixedFeesHelpHint);

        jlPaymentFixedFeesHelp.setForeground(java.awt.Color.gray);
        jlPaymentFixedFeesHelp.setText("(por mes)");
        jlPaymentFixedFeesHelp.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel10.add(jlPaymentFixedFeesHelp);

        jPanel2.add(jPanel10);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPaymentUmas.setText("Número UMA:*");
        jlPaymentUmas.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel16.add(jlPaymentUmas);
        jPanel16.add(moDecPaymentUmas);

        jlPaymentUmasHelpHint.setForeground(new java.awt.Color(109, 109, 109));
        jlPaymentUmasHelpHint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlPaymentUmasHelpHint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlPaymentUmasHelpHint.setToolTipText("Monto a pagar mensual expresado en número de UMA");
        jlPaymentUmasHelpHint.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel16.add(jlPaymentUmasHelpHint);

        jlPaymentUmasHelp.setForeground(java.awt.Color.gray);
        jlPaymentUmasHelp.setText("(por mes)");
        jlPaymentUmasHelp.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel16.add(jlPaymentUmasHelp);

        jPanel2.add(jPanel16);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPaymentUmis.setText("Número UMI:*");
        jlPaymentUmis.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel17.add(jlPaymentUmis);
        jPanel17.add(moDecPaymentUmis);

        jlPaymentUmisHelpHint.setForeground(new java.awt.Color(109, 109, 109));
        jlPaymentUmisHelpHint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlPaymentUmisHelpHint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlPaymentUmisHelpHint.setToolTipText("Monto a pagar mensual expresado en número de UMI");
        jlPaymentUmisHelpHint.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel17.add(jlPaymentUmisHelpHint);

        jlPaymentUmisHelp.setForeground(java.awt.Color.gray);
        jlPaymentUmisHelp.setText("(por mes)");
        jlPaymentUmisHelp.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel17.add(jlPaymentUmisHelp);

        jPanel2.add(jPanel17);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPaymentPercentage.setText("Porcentaje salario:*");
        jlPaymentPercentage.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel11.add(jlPaymentPercentage);
        jPanel11.add(moDecPaymentPercentage);

        jlPaymentPercentageHelpHint.setForeground(new java.awt.Color(109, 109, 109));
        jlPaymentPercentageHelpHint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlPaymentPercentageHelpHint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlPaymentPercentageHelpHint.setToolTipText("Monto a pagar expresado en porcentaje del salario pagado seleccionado");
        jlPaymentPercentageHelpHint.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jlPaymentPercentageHelpHint);

        jlPaymentPercentageHelp.setForeground(java.awt.Color.gray);
        jlPaymentPercentageHelp.setText("(por nómina)");
        jlPaymentPercentageHelp.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel11.add(jlPaymentPercentageHelp);

        jPanel2.add(jPanel11);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moRadGroupSalaryType.add(moRadPaymentPercentageRefSd);
        moRadPaymentPercentageRefSd.setText("SD");
        moRadPaymentPercentageRefSd.setToolTipText("salario diario");
        jPanel14.add(moRadPaymentPercentageRefSd);

        moRadGroupSalaryType.add(moRadPaymentPercentageRefSbc);
        moRadPaymentPercentageRefSbc.setText("SBC");
        moRadPaymentPercentageRefSbc.setToolTipText("salario base de cotización");
        jPanel14.add(moRadPaymentPercentageRefSbc);

        moRadGroupSalaryType.add(moRadPaymentPercentageRefOtro);
        moRadPaymentPercentageRefOtro.setText("Otro salario");
        moRadPaymentPercentageRefOtro.setToolTipText("salario de referencia");
        jPanel14.add(moRadPaymentPercentageRefOtro);

        jPanel2.add(jPanel14);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRadPaymentPercentageAmount.setText("Otro salario:*");
        jlRadPaymentPercentageAmount.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel15.add(jlRadPaymentPercentageAmount);
        jPanel15.add(moDecRadPaymentPercentageAmount);

        jlRadPaymentPercentageAmountHint.setForeground(new java.awt.Color(109, 109, 109));
        jlRadPaymentPercentageAmountHint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlRadPaymentPercentageAmountHint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlRadPaymentPercentageAmountHint.setToolTipText("Otro salario × días laborados para cálculo de pago mediante porcentaje");
        jlRadPaymentPercentageAmountHint.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel15.add(jlRadPaymentPercentageAmountHint);

        jlRadPaymentPercentageAmountHelp.setForeground(java.awt.Color.gray);
        jlRadPaymentPercentageAmountHelp.setText("(× días laborados)");
        jlRadPaymentPercentageAmountHelp.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel15.add(jlRadPaymentPercentageAmountHelp);

        jPanel2.add(jPanel15);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel jlCapital;
    private javax.swing.JLabel jlDateEnd_n;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlEmployee;
    private javax.swing.JLabel jlLoanPaymentType;
    private javax.swing.JLabel jlLoanType;
    private javax.swing.JLabel jlNumber;
    private javax.swing.JLabel jlPaymentAmount;
    private javax.swing.JLabel jlPaymentAmountHelp;
    private javax.swing.JLabel jlPaymentAmountHelpHint;
    private javax.swing.JLabel jlPaymentFixedFees;
    private javax.swing.JLabel jlPaymentFixedFeesHelp;
    private javax.swing.JLabel jlPaymentFixedFeesHelpHint;
    private javax.swing.JLabel jlPaymentPercentage;
    private javax.swing.JLabel jlPaymentPercentageHelp;
    private javax.swing.JLabel jlPaymentPercentageHelpHint;
    private javax.swing.JLabel jlPaymentUmas;
    private javax.swing.JLabel jlPaymentUmasHelp;
    private javax.swing.JLabel jlPaymentUmasHelpHint;
    private javax.swing.JLabel jlPaymentUmis;
    private javax.swing.JLabel jlPaymentUmisHelp;
    private javax.swing.JLabel jlPaymentUmisHelpHint;
    private javax.swing.JLabel jlPaymentsNumber;
    private javax.swing.JLabel jlRadPaymentPercentageAmount;
    private javax.swing.JLabel jlRadPaymentPercentageAmountHelp;
    private javax.swing.JLabel jlRadPaymentPercentageAmountHint;
    private javax.swing.JLabel jlTotalAmount;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateEnd_n;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateStart;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecCapital;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecPaymentAmount;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecPaymentFixedFees;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecPaymentPercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecPaymentUmas;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecPaymentUmis;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecPaymentsNumber;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecRadPaymentPercentageAmount;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecTotalAmount;
    private sa.lib.gui.bean.SBeanFieldKey moKeyEmployee;
    private sa.lib.gui.bean.SBeanFieldKey moKeyLoanPaymentType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyLoanType;
    private javax.swing.ButtonGroup moRadGroupSalaryType;
    private sa.lib.gui.bean.SBeanFieldRadio moRadPaymentPercentageRefOtro;
    private sa.lib.gui.bean.SBeanFieldRadio moRadPaymentPercentageRefSbc;
    private sa.lib.gui.bean.SBeanFieldRadio moRadPaymentPercentageRefSd;
    private sa.lib.gui.bean.SBeanFieldText moTextNumber;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 800, 500);

        moKeyEmployee.setKeySettings(miClient, SGuiUtils.getLabelName(jlEmployee.getText()), true);
        moKeyLoanType.setKeySettings(miClient, SGuiUtils.getLabelName(jlLoanType.getText()), true);
        moKeyLoanPaymentType.setKeySettings(miClient, SGuiUtils.getLabelName(jlLoanPaymentType.getText()), true);
        moTextNumber.setTextSettings(SGuiUtils.getLabelName(jlNumber.getText()), 25);
        moDateDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart.getText()), true);
        moDateDateEnd_n.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd_n.getText()), false);
        moDecCapital.setDecimalSettings(SGuiUtils.getLabelName(jlCapital.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, true);
        moDecTotalAmount.setDecimalSettings(SGuiUtils.getLabelName(jlTotalAmount.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, true);
        moDecPaymentAmount.setDecimalSettings(SGuiUtils.getLabelName(jlPaymentAmount.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, true);
        moDecPaymentsNumber.setDecimalSettings(SGuiUtils.getLabelName(jlPaymentsNumber.getText()), SGuiConsts.GUI_TYPE_DEC_QTY, false);
        moDecPaymentFixedFees.setDecimalSettings(SGuiUtils.getLabelName(jlPaymentFixedFees.getText()), SGuiConsts.GUI_TYPE_DEC_AMT_UNIT, true);
        moDecPaymentUmas.setDecimalSettings(SGuiUtils.getLabelName(jlPaymentUmas.getText()), SGuiConsts.GUI_TYPE_DEC_AMT_UNIT, true);
        moDecPaymentUmis.setDecimalSettings(SGuiUtils.getLabelName(jlPaymentUmis.getText()), SGuiConsts.GUI_TYPE_DEC_AMT_UNIT, true);
        moDecPaymentPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlPaymentPercentage.getText()), SGuiConsts.GUI_TYPE_DEC_PER_DISC, true);
        moRadPaymentPercentageRefSd.setBooleanSettings(SGuiUtils.getLabelName(moRadPaymentPercentageRefSd.getText()), false);
        moRadPaymentPercentageRefSbc.setBooleanSettings(SGuiUtils.getLabelName(moRadPaymentPercentageRefSbc.getText()), false);
        moRadPaymentPercentageRefOtro.setBooleanSettings(SGuiUtils.getLabelName(moRadPaymentPercentageRefOtro.getText()), false);
        moDecRadPaymentPercentageAmount.setDecimalSettings(SGuiUtils.getLabelName(jlRadPaymentPercentageAmount.getText()), SGuiConsts.GUI_TYPE_DEC_AMT_UNIT, true);

        moFields.addField(moKeyEmployee);
        moFields.addField(moKeyLoanType);
        moFields.addField(moKeyLoanPaymentType);
        moFields.addField(moTextNumber);
        moFields.addField(moDateDateStart);
        moFields.addField(moDateDateEnd_n);
        moFields.addField(moDecCapital);
        moFields.addField(moDecTotalAmount);
        moFields.addField(moDecPaymentAmount);
        moFields.addField(moDecPaymentsNumber);
        moFields.addField(moDecPaymentFixedFees);
        moFields.addField(moDecPaymentUmas);
        moFields.addField(moDecPaymentUmis);
        moFields.addField(moDecPaymentPercentage);
        moFields.addField(moRadPaymentPercentageRefSd);
        moFields.addField(moRadPaymentPercentageRefSbc);
        moFields.addField(moRadPaymentPercentageRefOtro);
        moFields.addField(moDecRadPaymentPercentageAmount);

        moFields.setFormButton(jbSave);
    }
    
    private void computePaymentsNumber() {
        moDecPaymentsNumber.setValue(moDecPaymentAmount.getValue() == 0 ? 0d : moDecTotalAmount.getValue() / moDecPaymentAmount.getValue());
    }

    private void itemStateKeyLoanType() {
        jlPaymentAmountHelp.setText("");
        moDecCapital.setEditable(false);
        moDecTotalAmount.setEditable(false);
        moDecCapital.setValue(0d);
        moDecTotalAmount.setValue(0d);
        
        if (moKeyLoanType.getSelectedIndex() > 0) {
            switch (moKeyLoanType.getValue()[0]) {
                case SModSysConsts.HRSS_TP_LOAN_LOAN_COM:
                case SModSysConsts.HRSS_TP_LOAN_LOAN_UNI:
                case SModSysConsts.HRSS_TP_LOAN_LOAN_3RD:
                    jlPaymentAmountHelp.setText("(por nómina)");
                    moDecCapital.setEditable(true);
                    moDecTotalAmount.setEditable(true);
                    break;
                case SModSysConsts.HRSS_TP_LOAN_HOME:
                case SModSysConsts.HRSS_TP_LOAN_CONS:
                    jlPaymentAmountHelp.setText("(por mes)");
                    break;
                default:
            }
        }
    }
    
    private void itemStateKeyLoanPaymentType() {
        moDecPaymentAmount.setEditable(false);
        moDecPaymentFixedFees.setEditable(false);
        moDecPaymentUmas.setEditable(false);
        moDecPaymentUmis.setEditable(false);
        moDecPaymentPercentage.setEditable(false);
        moDecPaymentAmount.setValue(0d);
        moDecPaymentFixedFees.setValue(0d);
        moDecPaymentUmas.setValue(0d);
        moDecPaymentUmis.setValue(0d);
        moDecPaymentPercentage.setValue(0d);
        moRadPaymentPercentageRefSd.setEnabled(false);
        moRadPaymentPercentageRefSbc.setEnabled(false);
        moRadPaymentPercentageRefOtro.setEnabled(false);
        
        if (moKeyLoanPaymentType.getSelectedIndex() > 0) {
            switch (moKeyLoanPaymentType.getValue()[0]) {
                case SModSysConsts.HRSS_TP_LOAN_PAY_AMT:
                    moDecPaymentAmount.setEditable(true);
                    break;
                case SModSysConsts.HRSS_TP_LOAN_PAY_FACT_SAL:
                    moDecPaymentFixedFees.setEditable(true);
                    break;
                case SModSysConsts.HRSS_TP_LOAN_PAY_FACT_UMA:
                    moDecPaymentUmas.setEditable(true);
                    break;
                case SModSysConsts.HRSS_TP_LOAN_PAY_FACT_UMI:
                    moDecPaymentUmis.setEditable(true);
                    break;
                case SModSysConsts.HRSS_TP_LOAN_PAY_PCT:
                    moDecPaymentPercentage.setEditable(true);
                    moRadPaymentPercentageRefSd.setEnabled(true);
                    moRadPaymentPercentageRefSbc.setEnabled(true);
                    moRadPaymentPercentageRefOtro.setEnabled(true);
                    break;
                default:
            }
        }
        
        stateChangedOtherSalary();
    }
    
    private void stateChangedOtherSalary() {
        moDecRadPaymentPercentageAmount.setEditable(false);
        moDecRadPaymentPercentageAmount.setValue(0d);
        
        if (moRadPaymentPercentageRefOtro.isEnabled() && moRadPaymentPercentageRefOtro.isSelected()) {
            moDecRadPaymentPercentageAmount.setEditable(true);
        }
    }
    
    private void focusGainedTotalAmount() {
        if (moDecTotalAmount.getValue() == 0) {
            moDecTotalAmount.setValue(moDecCapital.getValue());
        }
    }
    
    @Override
    public void addAllListeners() {
        moKeyLoanType.addItemListener(this);
        moKeyLoanPaymentType.addItemListener(this);
        moRadPaymentPercentageRefSd.addChangeListener(this);
        moRadPaymentPercentageRefSbc.addChangeListener(this);
        moRadPaymentPercentageRefOtro.addChangeListener(this);
        moDecTotalAmount.addFocusListener(this);
        moDecPaymentAmount.addFocusListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyLoanType.removeItemListener(this);
        moKeyLoanPaymentType.removeItemListener(this);
        moRadPaymentPercentageRefSd.removeChangeListener(this);
        moRadPaymentPercentageRefSbc.removeChangeListener(this);
        moRadPaymentPercentageRefOtro.removeChangeListener(this);
        moDecTotalAmount.removeFocusListener(this);
        moDecPaymentAmount.removeFocusListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyEmployee, SModConsts.HRSU_EMP, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyLoanType, SModConsts.HRSS_TP_LOAN, SModConsts.HRS_LOAN, null);
        miClient.getSession().populateCatalogue(moKeyLoanPaymentType, SModConsts.HRSS_TP_LOAN_PAY, SLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbLoan) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        int idEmployee = moRegistry.getPkEmployeeId();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            moRegistry.setDateStart(miClient.getSession().getCurrentDate());
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moKeyEmployee.setValue(new int[] { idEmployee });
        moTextNumber.setValue(moRegistry.getNumber());
        moDateDateStart.setValue(moRegistry.getDateStart());
        moDateDateEnd_n.setValue(moRegistry.getDateEnd_n());
        moDecCapital.setValue(moRegistry.getCapital());
        moDecTotalAmount.setValue(moRegistry.getTotalAmount());
        moKeyLoanType.setValue(new int[] { moRegistry.getFkLoanTypeId() });
        moKeyLoanPaymentType.setValue(new int[] { moRegistry.getFkLoanPaymentTypeId() });
        
        switch (moRegistry.getPaymentPercentageReference()) {
            case SHrsConsts.PAY_PER_REF_SD:
                moRadPaymentPercentageRefSd.setSelected(true);
                break;
            case SHrsConsts.PAY_PER_REF_SBC:
                moRadPaymentPercentageRefSbc.setSelected(true);
                break;
            case SHrsConsts.PAY_PER_REF_OTRO:
                moRadPaymentPercentageRefOtro.setSelected(true);
                break;
            default:
                moRadPaymentPercentageRefSbc.setSelected(true); // default option when no other available
        }
        
        setFormEditable(true);
        
        moDecPaymentsNumber.setEnabled(false);
        
        if (moRegistry.isRegistryNew()) {
            moRadPaymentPercentageRefSbc.setSelected(true);
        }
        else {
            moKeyEmployee.setEnabled(false);
        }
        
        itemStateKeyLoanType();
        itemStateKeyLoanPaymentType();
        stateChangedOtherSalary();
        
        moDecPaymentAmount.setValue(moRegistry.getPaymentAmount());
        moDecPaymentFixedFees.setValue(moRegistry.getPaymentFixedFees());
        moDecPaymentUmas.setValue(moRegistry.getPaymentUmas());
        moDecPaymentUmis.setValue(moRegistry.getPaymentUmis());
        moDecPaymentPercentage.setValue(moRegistry.getPaymentPercentage());
        moDecRadPaymentPercentageAmount.setValue(moRegistry.getPaymentPercentageAmount());
        
        computePaymentsNumber();

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbLoan registry = moRegistry.clone();

        if (registry.isRegistryNew()) {}

        registry.setPkEmployeeId(moKeyEmployee.getValue()[0]);
        //registry.setPkLoanId(...);
        registry.setNumber(moTextNumber.getValue());
        registry.setDateStart(moDateDateStart.getValue());
        registry.setDateEnd_n(moDateDateEnd_n.getValue());
        registry.setCapital(moDecCapital.getValue());
        registry.setTotalAmount(moDecTotalAmount.getValue());
        registry.setPaymentAmount(moDecPaymentAmount.getValue());
        registry.setPaymentFixedFees(moDecPaymentFixedFees.getValue());
        registry.setPaymentUmas(moDecPaymentUmas.getValue());
        registry.setPaymentUmis(moDecPaymentUmis.getValue());
        registry.setPaymentPercentage(moDecPaymentPercentage.getValue());
        registry.setPaymentPercentageAmount(moDecRadPaymentPercentageAmount.getValue());
        registry.setFkLoanTypeId(moKeyLoanType.getValue()[0]);
        registry.setFkLoanPaymentTypeId(moKeyLoanPaymentType.getValue()[0]);
        
        if (moRadPaymentPercentageRefSd.isSelected()) {
            registry.setPaymentPercentageReference(SHrsConsts.PAY_PER_REF_SD);
        }
        else if (moRadPaymentPercentageRefSbc.isSelected()) {
            registry.setPaymentPercentageReference(SHrsConsts.PAY_PER_REF_SBC);
        }
        else if (moRadPaymentPercentageRefOtro.isSelected()) {
            registry.setPaymentPercentageReference(SHrsConsts.PAY_PER_REF_OTRO);
        }
        else {
            registry.setPaymentPercentageReference(SHrsConsts.PAY_PER_REF_SBC); // default option when no other available
        }

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (moDateDateEnd_n.getValue() != null) {
                validation = SGuiUtils.validateDateRange(moDateDateStart, moDateDateEnd_n);
            }
        }
        
        return validation;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox && e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox comboBox = (JComboBox)  e.getSource();

            if (comboBox == moKeyLoanType) {
                itemStateKeyLoanType();
            }
            else if (comboBox == moKeyLoanPaymentType) {
                itemStateKeyLoanPaymentType();
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof SBeanFieldRadio) {
            if ((SBeanFieldRadio) e.getSource() == moRadPaymentPercentageRefOtro) {
                stateChangedOtherSalary();
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() instanceof SBeanFieldDecimal) {
            SBeanFieldDecimal field = (SBeanFieldDecimal) e.getSource();
            if (field == moDecTotalAmount) {
                focusGainedTotalAmount();
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof SBeanFieldDecimal) {
            SBeanFieldDecimal field = (SBeanFieldDecimal) e.getSource();
            if (field == moDecTotalAmount || field == moDecPaymentAmount) {
                computePaymentsNumber();
            }
        }
    }
}
