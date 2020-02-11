/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbEarning;
import erp.mod.hrs.db.SDbPayrollReceiptEarning;
import erp.mod.hrs.db.SHrsEmployeeDays;
import erp.mod.hrs.db.SHrsReceiptEarning;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import sa.lib.SLibConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFieldDecimal;
import sa.lib.gui.bean.SBeanFormDialog;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDialogPayrollEarning extends SBeanFormDialog implements FocusListener {
    
    private static final String LABEL_AUX_AMT = "Monto auxiliar";
    
    private final SHrsReceiptEarning moHrsReceiptEarning;
    private final SHrsEmployeeDays moHrsEmployeeDays;
    private double mdOriginalUnitsAlleged;
    private double mdOriginalAmount;
    private double mdOriginalAuxAmount;

    /**
     * Creates new form SDialogPayrollEarning
     * @param client
     * @param hrsReceiptEarning
     * @param title
     */

    public SDialogPayrollEarning(SGuiClient client, SHrsReceiptEarning hrsReceiptEarning, String title) {
        setFormSettings(client, SModConsts.HRS_PAY_RCP_EAR, SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, title);
        moHrsReceiptEarning = hrsReceiptEarning;
        moHrsEmployeeDays = moHrsReceiptEarning.getHrsReceipt().getHrsEmployee().createEmployeeDays();
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
        jPanel7 = new javax.swing.JPanel();
        jlEarning = new javax.swing.JLabel();
        moTextEarningCode = new sa.lib.gui.bean.SBeanFieldText();
        moTextEarningName = new sa.lib.gui.bean.SBeanFieldText();
        jPanel16 = new javax.swing.JPanel();
        jlOtherPayment = new javax.swing.JLabel();
        moKeyOtherPayment = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel13 = new javax.swing.JPanel();
        jlUnitsAlleged = new javax.swing.JLabel();
        moCompUnitsAlleged = new sa.lib.gui.bean.SBeanCompoundField();
        jPanel4 = new javax.swing.JPanel();
        jlUnits = new javax.swing.JLabel();
        moCompUnits = new sa.lib.gui.bean.SBeanCompoundField();
        jPanel15 = new javax.swing.JPanel();
        jlAmountUnit = new javax.swing.JLabel();
        moCurAmountUnit = new sa.lib.gui.bean.SBeanCompoundFieldCurrency();
        jPanel5 = new javax.swing.JPanel();
        jlFactorAmount = new javax.swing.JLabel();
        moDecFactorAmount = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel14 = new javax.swing.JPanel();
        jlAmount = new javax.swing.JLabel();
        moCurAmount = new sa.lib.gui.bean.SBeanCompoundFieldCurrency();
        jPanel17 = new javax.swing.JPanel();
        jlAuxAmount = new javax.swing.JLabel();
        moCurAuxAmount = new sa.lib.gui.bean.SBeanCompoundFieldCurrency();
        jlAuxAmountHint = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jckUserEdited = new javax.swing.JCheckBox();
        jckAutomatic = new javax.swing.JCheckBox();

        setTitle("Percepción");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(9, 1, 0, 5));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlEarning.setText("Percepción:");
        jlEarning.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlEarning);

        moTextEarningCode.setEditable(false);
        moTextEarningCode.setText("sBeanFieldText1");
        moTextEarningCode.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel7.add(moTextEarningCode);

        moTextEarningName.setEditable(false);
        moTextEarningName.setText("sBeanFieldText1");
        moTextEarningName.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel7.add(moTextEarningName);

        jPanel2.add(jPanel7);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOtherPayment.setText("Tipo otro pago:*");
        jlOtherPayment.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel16.add(jlOtherPayment);

        moKeyOtherPayment.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel16.add(moKeyOtherPayment);

        jPanel2.add(jPanel16);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlUnitsAlleged.setText("Valor:*");
        jlUnitsAlleged.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.add(jlUnitsAlleged);
        jPanel13.add(moCompUnitsAlleged);

        jPanel2.add(jPanel13);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlUnits.setText("Valor ajustado:*");
        jlUnits.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlUnits);
        jPanel4.add(moCompUnits);

        jPanel2.add(jPanel4);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAmountUnit.setText("Monto unitario:");
        jlAmountUnit.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel15.add(jlAmountUnit);

        moCurAmountUnit.setEditable(false);
        jPanel15.add(moCurAmountUnit);

        jPanel2.add(jPanel15);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFactorAmount.setText("Factor ajuste:");
        jlFactorAmount.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlFactorAmount);

        moDecFactorAmount.setEditable(false);
        jPanel5.add(moDecFactorAmount);

        jPanel2.add(jPanel5);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAmount.setText("Monto:*");
        jlAmount.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlAmount);
        jPanel14.add(moCurAmount);

        jPanel2.add(jPanel14);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAuxAmount.setText("Monto auxiliar:");
        jlAuxAmount.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel17.add(jlAuxAmount);
        jPanel17.add(moCurAuxAmount);

        jlAuxAmountHint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAuxAmountHint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlAuxAmountHint.setToolTipText("Monto auxiliar");
        jlAuxAmountHint.setPreferredSize(new java.awt.Dimension(20, 23));
        jPanel17.add(jlAuxAmountHint);

        jPanel2.add(jPanel17);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckUserEdited.setText("Registro modificado por el usuario");
        jckUserEdited.setEnabled(false);
        jckUserEdited.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel3.add(jckUserEdited);

        jckAutomatic.setText("Registro creado automáticamente");
        jckAutomatic.setEnabled(false);
        jckAutomatic.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel3.add(jckAutomatic);

        jPanel2.add(jPanel3);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JCheckBox jckAutomatic;
    private javax.swing.JCheckBox jckUserEdited;
    private javax.swing.JLabel jlAmount;
    private javax.swing.JLabel jlAmountUnit;
    private javax.swing.JLabel jlAuxAmount;
    private javax.swing.JLabel jlAuxAmountHint;
    private javax.swing.JLabel jlEarning;
    private javax.swing.JLabel jlFactorAmount;
    private javax.swing.JLabel jlOtherPayment;
    private javax.swing.JLabel jlUnits;
    private javax.swing.JLabel jlUnitsAlleged;
    private sa.lib.gui.bean.SBeanCompoundField moCompUnits;
    private sa.lib.gui.bean.SBeanCompoundField moCompUnitsAlleged;
    private sa.lib.gui.bean.SBeanCompoundFieldCurrency moCurAmount;
    private sa.lib.gui.bean.SBeanCompoundFieldCurrency moCurAmountUnit;
    private sa.lib.gui.bean.SBeanCompoundFieldCurrency moCurAuxAmount;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecFactorAmount;
    private sa.lib.gui.bean.SBeanFieldKey moKeyOtherPayment;
    private sa.lib.gui.bean.SBeanFieldText moTextEarningCode;
    private sa.lib.gui.bean.SBeanFieldText moTextEarningName;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 560, 350);

        jbSave.setText("Aceptar");

        moTextEarningName.setTextSettings(SGuiUtils.getLabelName(jlEarning.getText()), 255, 0);
        moKeyOtherPayment.setKeySettings(miClient, SGuiUtils.getLabelName(jlOtherPayment), true);
        moCompUnitsAlleged.setCompoundFieldSettings(miClient);
        moCompUnitsAlleged.getField().setDecimalSettings(SGuiUtils.getLabelName(jlUnitsAlleged.getText()), SGuiConsts.GUI_TYPE_DEC_QTY, true);
        moCompUnitsAlleged.getField().setValue(0d);
        moCompUnitsAlleged.setCompoundText("");
        moCompUnits.setCompoundFieldSettings(miClient);
        moCompUnits.getField().setDecimalSettings(SGuiUtils.getLabelName(jlUnits.getText()), SGuiConsts.GUI_TYPE_DEC_AMT_UNIT, true);
        moCompUnits.getField().setValue(0d);
        moCompUnits.setCompoundText("");
        moCurAmountUnit.setCompoundFieldSettings(miClient);
        moCurAmountUnit.getField().setDecimalSettings(SGuiUtils.getLabelName(jlAmountUnit.getText()), SGuiConsts.GUI_TYPE_DEC_AMT_UNIT, false);
        moDecFactorAmount.setDecimalSettings(SGuiUtils.getLabelName(jlFactorAmount.getText()), SGuiConsts.GUI_TYPE_DEC, false);
        moCurAmount.setCompoundFieldSettings(miClient);
        moCurAmount.getField().setDecimalSettings(SGuiUtils.getLabelName(jlAmount.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, true);
        moCurAuxAmount.setCompoundFieldSettings(miClient);
        moCurAuxAmount.getField().setDecimalSettings(SGuiUtils.getLabelName(jlAuxAmount.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, false);

        moFields.addField(moKeyOtherPayment);
        moFields.addField(moCompUnitsAlleged.getField());
        moFields.addField(moCompUnits.getField());
        moFields.addField(moCurAmountUnit.getField());
        moFields.addField(moDecFactorAmount);
        moFields.addField(moCurAmount.getField());
        moFields.addField(moCurAuxAmount.getField());

        moFields.setFormButton(jbSave);

        reloadCatalogues();
        
        renderEarning();
        
        addAllListeners();
    }
    
    private void renderEarning() {
        SDbEarning earning = moHrsReceiptEarning.getEarning(); // convenience variable
        SDbPayrollReceiptEarning payrollReceiptEarning = moHrsReceiptEarning.getPayrollReceiptEarning(); // convenience variable
        
        mdOriginalUnitsAlleged = payrollReceiptEarning.getUnitsAlleged();
        mdOriginalAmount = payrollReceiptEarning.getAmount_r();
        mdOriginalAuxAmount = payrollReceiptEarning.getAuxiliarAmount();
        
        moTextEarningCode.setValue(earning.getCode());
        moTextEarningName.setValue(earning.getName());
        
        moKeyOtherPayment.setValue(new int[] { payrollReceiptEarning.getFkOtherPaymentTypeId() });
        moKeyOtherPayment.setEnabled(earning.getFkEarningTypeId() == SModSysConsts.HRSS_TP_EAR_OTH);
        
        boolean editable = moHrsReceiptEarning.getEarning().areUnitsModifiable();
        String earningUnit = moHrsReceiptEarning.getHrsReceipt().getHrsPayroll().getEarningComputationTypesMap().get(earning.getFkEarningComputationTypeId());
        
        moCompUnitsAlleged.getField().setValue(payrollReceiptEarning.getUnitsAlleged());
        moCompUnitsAlleged.setCompoundText(earningUnit);
        moCompUnitsAlleged.getField().setEditable(editable);
        
        moCompUnits.getField().setValue(payrollReceiptEarning.getUnits());
        moCompUnits.setCompoundText(earningUnit);
        moCompUnits.getField().setEditable(editable);
        
        moCurAmountUnit.getField().setValue(payrollReceiptEarning.getAmountUnitary());
        moDecFactorAmount.setValue(payrollReceiptEarning.getFactorAmount());
        moCurAmount.getField().setValue(payrollReceiptEarning.getAmount_r());
        
        moCurAuxAmount.getField().setValue(payrollReceiptEarning.getAuxiliarAmount());
        if (earning.getFkEarningTypeId() == SModSysConsts.HRSS_TP_EAR_TAX_SUB) {
            jlAuxAmount.setText(SDbEarning.AuxAmountLabels.get(SModSysConsts.HRSS_TP_EAR_TAX_SUB) + ":");
            jlAuxAmountHint.setToolTipText(SDbEarning.AuxAmountHints.get(SModSysConsts.HRSS_TP_EAR_TAX_SUB));
            jlAuxAmountHint.setEnabled(true);
            moCurAuxAmount.setEditable(true);
        }
        else {
            jlAuxAmount.setText(LABEL_AUX_AMT + ":");
            jlAuxAmountHint.setToolTipText(null);
            jlAuxAmountHint.setEnabled(false);
            moCurAuxAmount.setEditable(false);
        }
        
        jckUserEdited.setSelected(payrollReceiptEarning.isUserEdited());
        jckAutomatic.setSelected(payrollReceiptEarning.isAutomatic());
    }
    
    private void actionCalculateUnits() {
        double units = moHrsEmployeeDays.computeEarningUnits(moCompUnitsAlleged.getField().getValue(), moHrsReceiptEarning.getEarning());
        moCompUnits.getField().setValue(units);
        
        actionCalculateAmount();
    }
    
    private void actionCalculateAmount() {
        double amount = SHrsEmployeeDays.computeEarningAmount(moCompUnits.getField().getValue(), moCurAmountUnit.getField().getValue(), moHrsReceiptEarning.getEarning());
        moCurAmount.getField().setValue(amount);
    }
    
    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyOtherPayment, SModConsts.HRSS_TP_OTH_PAY, 0, null);
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (moCompUnits.getField().getValue() < moCompUnitsAlleged.getField().getValue()) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + SGuiUtils.getLabelName(jlUnits) + "'" + SGuiConsts.ERR_MSG_FIELD_VAL_GREAT_EQUAL + "'" + SGuiUtils.getLabelName(jlUnitsAlleged) + "'.");
                validation.setComponent(moCompUnits.getField().getComponent());
            }
            else if (moKeyOtherPayment.isEnabled()) {
                if (moKeyOtherPayment.getSelectedIndex() <= 0) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlOtherPayment) + "'.");
                    validation.setComponent(moKeyOtherPayment);
                }
                else if (moKeyOtherPayment.getValue()[0] == SModSysConsts.HRSS_TP_OTH_PAY_NON) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + SGuiUtils.getLabelName(jlOtherPayment) + "'.");
                    validation.setComponent(moKeyOtherPayment);
                }
            }
        }
        
        return validation;
    }
    
    @Override
    public void actionSave() {
        if (jbSave.isEnabled()) {
            if (SGuiUtils.computeValidation(miClient, validateForm())) {
                SDbEarning earning = moHrsReceiptEarning.getEarning(); // convenience variable
                SDbPayrollReceiptEarning payrollReceiptEarning = moHrsReceiptEarning.getPayrollReceiptEarning(); // convenience variable
                
                if (!earning.isBasedOnUnits()) {
                    if (earning.getFkBenefitTypeId() != SModSysConsts.HRSS_TP_BEN_VAC_BON) {
                        payrollReceiptEarning.setAmountUnitary(moCurAmount.getField().getValue());
                    }
                }
                else {
                    payrollReceiptEarning.setUnitsAlleged(moCompUnitsAlleged.getField().getValue());
                    payrollReceiptEarning.setUnits(moCompUnits.getField().getValue());
                }

                payrollReceiptEarning.setAmount_r(moCurAmount.getField().getValue());
                payrollReceiptEarning.setAuxiliarAmount(moCurAuxAmount.getField().getValue());
                
                if (!payrollReceiptEarning.isUserEdited()) {
                    payrollReceiptEarning.setUserEdited(
                            mdOriginalUnitsAlleged != moCompUnitsAlleged.getField().getValue() || 
                            mdOriginalAmount != moCurAmount.getField().getValue() ||
                            mdOriginalAuxAmount != moCurAuxAmount.getField().getValue()
                    );
                }
                
                moHrsReceiptEarning.getHrsReceipt().replaceHrsReceiptEarning(payrollReceiptEarning.getPkMoveId(), moHrsReceiptEarning);

                if (!payrollReceiptEarning.isAutomatic() && payrollReceiptEarning.getUnits()== 0) { 
                    moHrsReceiptEarning.getHrsReceipt().removeHrsReceiptEarning(payrollReceiptEarning.getPkMoveId());
                }
                
                mnFormResult = SGuiConsts.FORM_RESULT_OK;
                dispose();
            }
        }
    }

    @Override
    public void addAllListeners() {
        moCompUnitsAlleged.getField().getComponent().addFocusListener(this);
        moCompUnits.getField().getComponent().addFocusListener(this);
    }

    @Override
    public void removeAllListeners() {
        moCompUnitsAlleged.getField().getComponent().removeFocusListener(this);
        moCompUnits.getField().getComponent().removeFocusListener(this);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof SBeanFieldDecimal) {
            SBeanFieldDecimal decimalField = (SBeanFieldDecimal) e.getSource();

            if (decimalField == moCompUnits.getField().getComponent()) {
                actionCalculateAmount();
            }
            else if (decimalField == moCompUnitsAlleged.getField().getComponent()) {
                actionCalculateUnits();
            }
        }
    }
}
