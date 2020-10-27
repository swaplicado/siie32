/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbEarning;
import erp.mod.hrs.db.SDbPayrollReceiptEarning;
import erp.mod.hrs.db.SDbPayrollReceiptEarningComplement;
import erp.mod.hrs.db.SHrsUtils;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SFormLoanAdjustmentEarning extends SBeanForm implements ItemListener {

    private SDbPayrollReceiptEarning moRegistry;

    /**
     * Creates new form SFormLoanAdjustmentEarning
     * @param client
     * @param title
     */
    public SFormLoanAdjustmentEarning(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.HRS_PAY_RCP_EAR, SLibConsts.UNDEFINED, title);
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
        jPanel13 = new javax.swing.JPanel();
        jlEmployee = new javax.swing.JLabel();
        moKeyEmployee = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel7 = new javax.swing.JPanel();
        jlLoanType = new javax.swing.JLabel();
        moKeyLoanType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel12 = new javax.swing.JPanel();
        jlLoan = new javax.swing.JLabel();
        moKeyLoan = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel4 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        moDateDate = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel8 = new javax.swing.JPanel();
        jlTotalAmount = new javax.swing.JLabel();
        moCurPayment = new sa.lib.gui.bean.SBeanCompoundFieldCurrency();
        jPanel14 = new javax.swing.JPanel();
        jlNotes = new javax.swing.JLabel();
        moTextNotes = new sa.lib.gui.bean.SBeanFieldText();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(6, 1, 0, 5));

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlEmployee.setForeground(new java.awt.Color(0, 0, 255));
        jlEmployee.setText("Empleado:*");
        jlEmployee.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel13.add(jlEmployee);

        moKeyEmployee.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel13.add(moKeyEmployee);

        jPanel2.add(jPanel13);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLoanType.setText("Tipo crédito/préstamo:*");
        jlLoanType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel7.add(jlLoanType);

        moKeyLoanType.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel7.add(moKeyLoanType);

        jPanel2.add(jPanel7);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLoan.setText("Crédito/préstamo:*");
        jlLoan.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel12.add(jlLoan);

        moKeyLoan.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel12.add(moKeyLoan);

        jPanel2.add(jPanel12);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate.setText("Fecha:*");
        jlDate.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel4.add(jlDate);

        moDateDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(moDateDate);

        jPanel2.add(jPanel4);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTotalAmount.setText("Monto:*");
        jlTotalAmount.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel8.add(jlTotalAmount);
        jPanel8.add(moCurPayment);

        jPanel2.add(jPanel8);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNotes.setText("Notas:");
        jlNotes.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel14.add(jlNotes);

        moTextNotes.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel14.add(moTextNotes);

        jPanel2.add(jPanel14);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlEmployee;
    private javax.swing.JLabel jlLoan;
    private javax.swing.JLabel jlLoanType;
    private javax.swing.JLabel jlNotes;
    private javax.swing.JLabel jlTotalAmount;
    private sa.lib.gui.bean.SBeanCompoundFieldCurrency moCurPayment;
    private sa.lib.gui.bean.SBeanFieldDate moDateDate;
    private sa.lib.gui.bean.SBeanFieldKey moKeyEmployee;
    private sa.lib.gui.bean.SBeanFieldKey moKeyLoan;
    private sa.lib.gui.bean.SBeanFieldKey moKeyLoanType;
    private sa.lib.gui.bean.SBeanFieldText moTextNotes;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 560, 350);

        moKeyEmployee.setKeySettings(miClient, SGuiUtils.getLabelName(jlEmployee.getText()), true);
        moKeyLoanType.setKeySettings(miClient, SGuiUtils.getLabelName(jlLoanType.getText()), true);
        moKeyLoan.setKeySettings(miClient, SGuiUtils.getLabelName(jlLoan.getText()), true);
        moDateDate.setDateSettings(miClient, SGuiUtils.getLabelName(jlDate.getText()), true);
        moCurPayment.setCompoundFieldSettings(miClient);
        moCurPayment.getField().setDecimalSettings(SGuiUtils.getLabelName(jlTotalAmount.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, true);
        moTextNotes.setTextSettings(SGuiUtils.getLabelName(jlTotalAmount.getText()), 255, 0);

        moFields.addField(moKeyEmployee);
        moFields.addField(moKeyLoanType);
        moFields.addField(moKeyLoan);
        moFields.addField(moDateDate);
        moFields.addField(moCurPayment.getField());
        moFields.addField(moTextNotes);

        moFields.setFormButton(jbSave);
    }

    private void itemStateKeyLoanType() {
        if (moKeyEmployee.getSelectedIndex() > SLibConsts.UNDEFINED && moKeyLoanType.getSelectedIndex() > SLibConsts.UNDEFINED) {
            moKeyLoan.setEnabled(true);
            miClient.getSession().populateCatalogue(moKeyLoan, SModConsts.HRS_LOAN, SLibConsts.UNDEFINED, new SGuiParams(new int[] { moKeyEmployee.getValue()[0], moKeyLoanType.getValue()[0] }));
        }
        else {
            moKeyLoan.setSelectedIndex(0);
            moKeyLoan.setEnabled(false);
        }
    }
    
    @Override
    public void addAllListeners() {
        moKeyEmployee.addItemListener(this);
        moKeyLoanType.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyEmployee.removeItemListener(this);
        moKeyLoanType.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyEmployee, SModConsts.HRSU_EMP, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyLoanType, SModConsts.HRSS_TP_LOAN, SModConsts.HRS_LOAN, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        int idEmployee = SLibConsts.UNDEFINED;
        SDbPayrollReceiptEarningComplement earningComplement = null;
        moRegistry = (SDbPayrollReceiptEarning) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        earningComplement = moRegistry.getChildEarningComplement();
        
        removeAllListeners();
        reloadCatalogues();

        idEmployee = moRegistry.getPkEmployeeId();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            
            earningComplement = new SDbPayrollReceiptEarningComplement();
            earningComplement.setDate(miClient.getSession().getCurrentDate());
            
            moRegistry.setChildEarningComplement(earningComplement);
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moKeyEmployee.setValue(new int[] { idEmployee });
        moDateDate.setValue(earningComplement.getDate());
        if (moRegistry.getFkLoanLoanId_n() != SLibConsts.UNDEFINED) {
            moKeyLoanType.setValue(new int[] { moRegistry.getFkLoanTypeId_n() });
            itemStateKeyLoanType();
            moKeyLoan.setValue(new int[] { moRegistry.getFkLoanEmployeeId_n(), moRegistry.getFkLoanLoanId_n() });
        }
        moCurPayment.getField().setValue(moRegistry.getAmount_r());
        moTextNotes.setValue(earningComplement.getNotes());

        setFormEditable(true);
        
        if (!moRegistry.isRegistryNew()) {
            moKeyEmployee.setEnabled(false);
        }

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbPayrollReceiptEarning registry = moRegistry.clone();
        SDbEarning earning = SHrsUtils.getEarningForLoanType(miClient, moKeyLoanType.getValue()[0]);

        if (registry.isRegistryNew()) {}

        registry.setPkPayrollId(SLibConsts.UNDEFINED);
        registry.setPkEmployeeId(moKeyEmployee.getValue()[0]);
        registry.setUnitsAlleged(1);
        registry.setUnits(1);
        registry.setAmountUnitary(moCurPayment.getField().getValue());
        registry.setAmount_r(moCurPayment.getField().getValue());
        registry.setAutomatic(true);
        registry.setAlternativeTaxCalculation(earning.isAlternativeTaxCalculation());// XXX (jbarajas, 2016-04-06) articule 174 RLISR
        registry.setFkEarningTypeId(earning.getFkEarningTypeId());
        registry.setFkEarningId(earning.getPkEarningId());
        registry.setFkBonusId(SModSysConsts.HRSS_BONUS_NON);
        registry.setFkBenefitTypeId(earning.getFkBenefitTypeId());
        registry.setFkLoanTypeId_n(moKeyLoanType.getValue()[0]);
        registry.setFkLoanEmployeeId_n(moKeyLoan.getValue()[0]);
        registry.setFkLoanLoanId_n(moKeyLoan.getValue()[1]);
        
        // registry earning complement:
        
        registry.getChildEarningComplement().setDate(moDateDate.getValue());
        registry.getChildEarningComplement().setNotes(moTextNotes.getValue());

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
        }
        
        return validation;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox && e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox comboBox = (JComboBox)  e.getSource();

            if (comboBox == moKeyEmployee) {
                itemStateKeyLoanType();
            }
            else if (comboBox == moKeyLoanType) {
                itemStateKeyLoanType();
            }
        }
    }
}
