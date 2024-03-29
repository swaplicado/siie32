/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.gui.account.SAccountUtils;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mfin.data.SDataAccount;
import erp.mod.SModSysConsts;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;

/**
 *
 * @author  Sergio Flores
 */
public class SFormAccount extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mfin.data.SDataAccount moAccount;
    private erp.mfin.data.SDataAccount moAccountMajor;
    private erp.lib.form.SFormField moFieldAccount;
    private erp.lib.form.SFormField moFieldDateStart;
    private erp.lib.form.SFormField moFieldDateEnd_n;
    private erp.lib.form.SFormField moFieldIsActive;
    private erp.lib.form.SFormField moFieldIsDeleted;
    private erp.lib.form.SFormField moFieldFkAccountEbitdaTypeId;
    private erp.lib.form.SFormField moFieldFkAdministrativeConceptTypeId;
    private erp.lib.form.SFormField moFieldFkTaxableConceptTypeId;
    private erp.lib.form.SFormField moFieldFkFiscalAccountId;
    private erp.mfin.form.SPanelAccount moPanelPkAccountId;
    private erp.mfin.form.SPanelAccountChangeDeep moPanelPkAccountChangeDeep;

    /** Creates new form DFormAccount */
    public SFormAccount(erp.client.SClientInterface client, int formType) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = formType;
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

        jPanel3 = new javax.swing.JPanel();
        jpRegistry = new javax.swing.JPanel();
        jlDummyAccount = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlAccount = new javax.swing.JLabel();
        jtfAccount = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        jftDateStart = new javax.swing.JFormattedTextField();
        jbDateStart = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jlDateEnd_n = new javax.swing.JLabel();
        jftDateEnd_n = new javax.swing.JFormattedTextField();
        jbDateEnd_n = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jckIsActive = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jckIsDeleted = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jlFkAccountEbitdaTypeId = new javax.swing.JLabel();
        jcbFkAccountEbitdaTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkAccountEbitdaTypeId = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jlFkAdministrativeConceptTypeId = new javax.swing.JLabel();
        jcbFkAdministrativeConceptTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkAdministrativeConceptTypeId = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jlFkTaxableConceptTypeId = new javax.swing.JLabel();
        jcbFkTaxableConceptTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkTaxableConceptTypeId = new javax.swing.JButton();
        jPanel45 = new javax.swing.JPanel();
        jlFkFiscalAccountId = new javax.swing.JLabel();
        jcbFkFiscalAccountId = new javax.swing.JComboBox();
        jbFkFiscalAccountId = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cuenta contable"); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel3.setLayout(new java.awt.BorderLayout(5, 5));

        jpRegistry.setLayout(new java.awt.BorderLayout(0, 5));

        jlDummyAccount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlDummyAccount.setText("[Panel cuenta contable]");
        jlDummyAccount.setPreferredSize(new java.awt.Dimension(146, 50));
        jpRegistry.add(jlDummyAccount, java.awt.BorderLayout.NORTH);

        jPanel4.setLayout(new java.awt.BorderLayout(0, 1));

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 2, 0, 0));
        jPanel6.setLayout(new java.awt.BorderLayout(2, 0));

        jlAccount.setText("Cuenta contable: *");
        jlAccount.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel6.add(jlAccount, java.awt.BorderLayout.WEST);

        jtfAccount.setText("ACCOUNT");
        jPanel6.add(jtfAccount, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel6, java.awt.BorderLayout.NORTH);

        jPanel13.setLayout(new java.awt.GridLayout(4, 1, 0, 2));

        jPanel11.setLayout(new java.awt.FlowLayout(0, 2, 0));

        jlDateStart.setText("Fecha inicial vigencia: *");
        jlDateStart.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel11.add(jlDateStart);

        jftDateStart.setText("yyyy/mm/dd");
        jftDateStart.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel11.add(jftDateStart);

        jbDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateStart.setToolTipText("Seleccionar fecha");
        jbDateStart.setFocusable(false);
        jbDateStart.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbDateStart);

        jPanel13.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(0, 2, 0));

        jlDateEnd_n.setText("Fecha final vigencia:"); // NOI18N
        jlDateEnd_n.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel12.add(jlDateEnd_n);

        jftDateEnd_n.setText("yyyy/mm/dd");
        jftDateEnd_n.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel12.add(jftDateEnd_n);

        jbDateEnd_n.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateEnd_n.setToolTipText("Seleccionar fecha");
        jbDateEnd_n.setFocusable(false);
        jbDateEnd_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel12.add(jbDateEnd_n);

        jPanel13.add(jPanel12);

        jPanel1.setLayout(new java.awt.FlowLayout(0, 2, 0));

        jckIsActive.setText("Cuenta activa");
        jckIsActive.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel1.add(jckIsActive);

        jPanel13.add(jPanel1);

        jPanel5.setLayout(new java.awt.FlowLayout(0, 2, 0));

        jckIsDeleted.setText("Registro eliminado");
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel5.add(jckIsDeleted);

        jPanel13.add(jPanel5);

        jPanel4.add(jPanel13, java.awt.BorderLayout.CENTER);

        jpRegistry.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel3.add(jpRegistry, java.awt.BorderLayout.NORTH);

        jPanel8.setLayout(new java.awt.GridLayout(4, 1, 0, 2));

        jPanel15.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlFkAccountEbitdaTypeId.setText("Tipo cuenta EBITDA: *");
        jlFkAccountEbitdaTypeId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel15.add(jlFkAccountEbitdaTypeId);

        jcbFkAccountEbitdaTypeId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel15.add(jcbFkAccountEbitdaTypeId);

        jbFkAccountEbitdaTypeId.setText("...");
        jbFkAccountEbitdaTypeId.setToolTipText("Seleccionar tipo de cuenta EBITDA");
        jbFkAccountEbitdaTypeId.setFocusable(false);
        jbFkAccountEbitdaTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel15.add(jbFkAccountEbitdaTypeId);

        jPanel8.add(jPanel15);

        jPanel24.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlFkAdministrativeConceptTypeId.setText("Tipo concepto admtivo.: *");
        jlFkAdministrativeConceptTypeId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel24.add(jlFkAdministrativeConceptTypeId);

        jcbFkAdministrativeConceptTypeId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel24.add(jcbFkAdministrativeConceptTypeId);

        jbFkAdministrativeConceptTypeId.setText("...");
        jbFkAdministrativeConceptTypeId.setToolTipText("Seleccionar tipo de concepto administrativo");
        jbFkAdministrativeConceptTypeId.setFocusable(false);
        jbFkAdministrativeConceptTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel24.add(jbFkAdministrativeConceptTypeId);

        jPanel8.add(jPanel24);

        jPanel25.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlFkTaxableConceptTypeId.setText("Tipo concepto impuestos: *");
        jlFkTaxableConceptTypeId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel25.add(jlFkTaxableConceptTypeId);

        jcbFkTaxableConceptTypeId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel25.add(jcbFkTaxableConceptTypeId);

        jbFkTaxableConceptTypeId.setText("...");
        jbFkTaxableConceptTypeId.setToolTipText("Seleccionar tipo de concepto de impuestos");
        jbFkTaxableConceptTypeId.setFocusable(false);
        jbFkTaxableConceptTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel25.add(jbFkTaxableConceptTypeId);

        jPanel8.add(jPanel25);

        jPanel45.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel45.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlFkFiscalAccountId.setText("Código agrupador SAT: *");
        jlFkFiscalAccountId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel45.add(jlFkFiscalAccountId);

        jcbFkFiscalAccountId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel45.add(jcbFkFiscalAccountId);

        jbFkFiscalAccountId.setText("jButton1");
        jbFkFiscalAccountId.setToolTipText("Seleccionar código agrupador SAT");
        jbFkFiscalAccountId.setFocusable(false);
        jbFkFiscalAccountId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel45.add(jbFkFiscalAccountId);

        jPanel8.add(jPanel45);

        jPanel3.add(jPanel8, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.FlowLayout(2));

        jbOk.setText("Aceptar"); // NOI18N
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jbOk);

        jbCancel.setText("Cancelar"); // NOI18N
        jbCancel.setToolTipText("[Escape]");
        jPanel2.add(jbCancel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-600)/2, (screenSize.height-400)/2, 600, 400);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        try {
            if (mnFormType == SDataConstantsSys.FINX_ACC) {
                moPanelPkAccountId = new SPanelAccount(miClient, SDataConstants.FIN_ACC, true, true, true);
            }
            else if (mnFormType == SDataConstantsSys.FINX_ACC_DEEP) {
                moPanelPkAccountChangeDeep = new SPanelAccountChangeDeep(miClient);
            }         
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        jpRegistry.remove(jlDummyAccount);
        if (mnFormType == SDataConstantsSys.FINX_ACC) {
            jpRegistry.add(moPanelPkAccountId, BorderLayout.NORTH);
        }
        else if (mnFormType == SDataConstantsSys.FINX_ACC_DEEP) {
            jpRegistry.add(moPanelPkAccountChangeDeep, BorderLayout.NORTH);
        }
        
        moFieldAccount = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfAccount, jlAccount);
        moFieldAccount.setLengthMax(100);
        moFieldDateStart = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateStart, jlDateStart);
        moFieldDateStart.setPickerButton(jbDateStart);
        moFieldDateEnd_n = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftDateEnd_n, jlDateEnd_n);
        moFieldDateEnd_n.setPickerButton(jbDateEnd_n);
        moFieldIsActive = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsActive);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDeleted);
        moFieldFkAccountEbitdaTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkAccountEbitdaTypeId, jlFkAccountEbitdaTypeId);
        moFieldFkAccountEbitdaTypeId.setPickerButton(jbFkAccountEbitdaTypeId);
        moFieldFkAdministrativeConceptTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkAdministrativeConceptTypeId, jlFkAdministrativeConceptTypeId);
        moFieldFkAdministrativeConceptTypeId.setPickerButton(jbFkAdministrativeConceptTypeId);
        moFieldFkTaxableConceptTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTaxableConceptTypeId, jlFkTaxableConceptTypeId);
        moFieldFkTaxableConceptTypeId.setPickerButton(jbFkTaxableConceptTypeId);
        moFieldFkFiscalAccountId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkFiscalAccountId, jlFkFiscalAccountId);
        moFieldFkFiscalAccountId.setPickerButton(jbFkFiscalAccountId);

        mvFields = new Vector<SFormField>();
        mvFields.add(moFieldAccount);
        mvFields.add(moFieldDateStart);
        mvFields.add(moFieldDateEnd_n);
        mvFields.add(moFieldIsActive);
        mvFields.add(moFieldIsDeleted);
        mvFields.add(moFieldFkAccountEbitdaTypeId);
        mvFields.add(moFieldFkAdministrativeConceptTypeId);
        mvFields.add(moFieldFkTaxableConceptTypeId);
        mvFields.add(moFieldFkFiscalAccountId);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbDateStart.addActionListener(this);
        jbDateEnd_n.addActionListener(this);
        jbFkAccountEbitdaTypeId.addActionListener(this);
        jbFkAdministrativeConceptTypeId.addActionListener(this);
        jbFkTaxableConceptTypeId.addActionListener(this);
        jbFkFiscalAccountId.addActionListener(this);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionCancel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            if (mnFormType == SDataConstantsSys.FINX_ACC) {
                if (moPanelPkAccountId.getFieldAccount().getComponent().isEnabled()) {
                    moPanelPkAccountId.getFieldAccount().getComponent().requestFocus();
                }
                else {
                    jtfAccount.requestFocus();
                }
            }
            else if (mnFormType == SDataConstantsSys.FINX_ACC_DEEP) {
                    moPanelPkAccountChangeDeep.getFieldAccountNumber1().requestFocus();      
            }
        }
    }

    private void actionDateStart() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateStart.getDate(), moFieldDateStart);
    }

    private void actionDateEnd_n() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateEnd_n.getDate(), moFieldDateEnd_n);
    }

    private void actionFkAccountEbitdaTypeId() {
        miClient.pickOption(SDataConstants.FINU_TP_ACC_EBITDA, moFieldFkAccountEbitdaTypeId, null);
    }

    private void actionFkAdministrativeConceptTypeId() {
        miClient.pickOption(SDataConstants.FINU_TP_ADM_CPT, moFieldFkAdministrativeConceptTypeId, null);
    }

    private void actionFkTaxableConceptTypeId() {
        miClient.pickOption(SDataConstants.FINU_TP_TAX_CPT, moFieldFkTaxableConceptTypeId, null);
    }

    private void actionFkFiscalAccountId() {
        miClient.pickOption(SDataConstants.FINS_FISCAL_ACC, moFieldFkFiscalAccountId, null);
    }

    private void actionEdit(boolean edit) {

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDateEnd_n;
    private javax.swing.JButton jbDateStart;
    private javax.swing.JButton jbFkAccountEbitdaTypeId;
    private javax.swing.JButton jbFkAdministrativeConceptTypeId;
    private javax.swing.JButton jbFkFiscalAccountId;
    private javax.swing.JButton jbFkTaxableConceptTypeId;
    private javax.swing.JButton jbOk;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkAccountEbitdaTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkAdministrativeConceptTypeId;
    private javax.swing.JComboBox jcbFkFiscalAccountId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkTaxableConceptTypeId;
    private javax.swing.JCheckBox jckIsActive;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JFormattedTextField jftDateEnd_n;
    private javax.swing.JFormattedTextField jftDateStart;
    private javax.swing.JLabel jlAccount;
    private javax.swing.JLabel jlDateEnd_n;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlDummyAccount;
    private javax.swing.JLabel jlFkAccountEbitdaTypeId;
    private javax.swing.JLabel jlFkAdministrativeConceptTypeId;
    private javax.swing.JLabel jlFkFiscalAccountId;
    private javax.swing.JLabel jlFkTaxableConceptTypeId;
    private javax.swing.JPanel jpRegistry;
    private javax.swing.JTextField jtfAccount;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        moAccount = null;
        
        if (mnFormType == SDataConstantsSys.FINX_ACC) {
            jpRegistry.remove(moPanelPkAccountId);
            jpRegistry.add(moPanelPkAccountId, BorderLayout.NORTH);  
        }
        else if (mnFormType == SDataConstantsSys.FINX_ACC_DEEP) {
            jpRegistry.remove(moPanelPkAccountChangeDeep);
            jpRegistry.add(moPanelPkAccountChangeDeep, BorderLayout.NORTH);  
        
        }
        
        for (int i = 0; i < mvFields.size(); i++) {
            mvFields.get(i).resetField();
        }

        moFieldDateStart.setFieldValue(SLibTimeUtils.getBeginOfYear(miClient.getSessionXXX().getWorkingDate()));
        moFieldDateEnd_n.setFieldValue(null);
        moFieldIsActive.setFieldValue(true);
        moFieldFkAccountEbitdaTypeId.setFieldValue(new int[] { SDataConstantsSys.NA });
        moFieldFkAdministrativeConceptTypeId.setFieldValue(new int[] { SDataConstantsSys.NA });
        moFieldFkTaxableConceptTypeId.setFieldValue(new int[] { SDataConstantsSys.NA });
        moFieldFkFiscalAccountId.setFieldValue(new int[] { SModSysConsts.FINS_FISCAL_ACC_NA });
    
        if (mnFormType == SDataConstantsSys.FINX_ACC) {
            moPanelPkAccountId.enableFields(true);
            moPanelPkAccountId.resetPanel();
        }
        else if (mnFormType == SDataConstantsSys.FINX_ACC_DEEP) {
            moPanelPkAccountChangeDeep.resetPanel();
        }
        
        jckIsDeleted.setEnabled(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbFkAccountEbitdaTypeId, SDataConstants.FINU_TP_ACC_EBITDA);
        SFormUtilities.populateComboBox(miClient, jcbFkAdministrativeConceptTypeId, SDataConstants.FINU_TP_ADM_CPT);
        SFormUtilities.populateComboBox(miClient, jcbFkTaxableConceptTypeId, SDataConstants.FINU_TP_TAX_CPT);
        SFormUtilities.populateComboBox(miClient, jcbFkFiscalAccountId, SDataConstants.FINS_FISCAL_ACC);
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(mvFields.get(i).getComponent());
                break;
            }
        }

        if (!validation.getIsError()) {
            // Validate major account:
            if (mnFormType == SDataConstantsSys.FINX_ACC) {
               if (moAccount == null) {
                    if (moPanelPkAccountId.getDataAccountMajor() == null) {
                        validation.setMessage("La cuenta contable de mayor no existe.");
                        validation.setComponent(moPanelPkAccountId.getFieldAccount().getComponent());
                    }
                    else if (moPanelPkAccountId.getFieldAccount().getUsedLevelsCount() > moPanelPkAccountId.getDataAccountMajor().getDeep()) {
                        validation.setMessage("La profundidad de la cuenta contable (" + moPanelPkAccountId.getFieldAccount().getUsedLevelsCount() + ") " +
                                "es mayor a la profundidad de la cuenta de mayor (" + moPanelPkAccountId.getDataAccountMajor().getDeep() + ").");
                        validation.setComponent(moPanelPkAccountId.getFieldAccount().getComponent());
                    }
                    else if (SDataUtilities.callProcedureVal(miClient, SProcConstants.FIN_ACC_VAL,
                            new Object[] { moPanelPkAccountId.getFieldAccount().getString() },
                            SLibConstants.EXEC_MODE_VERBOSE) > 0) {
                        validation.setMessage("Ya existe un registro para el valor especificado en el campo 'No. cuenta contable'.");
                        validation.setComponent(moPanelPkAccountId.getFieldAccount().getComponent());
                    }
                    else if (moPanelPkAccountId.getDataAccountMajor().getDateStart().after(moFieldDateStart.getDate())) {
                        validation.setMessage("La fecha del campo '" + jlDateStart.getText() + "' no puede ser anterior a la fecha " + miClient.getSessionXXX().getFormatters().getDateFormat().format(moPanelPkAccountId.getDataAccountMajor().getDateStart()) + ".");
                        validation.setComponent(jftDateStart);
                    }
                    else if (moPanelPkAccountId.getDataAccountMajor().getDateEnd_n() != null && (moFieldDateEnd_n.getDate() == null || moPanelPkAccountId.getDataAccountMajor().getDateEnd_n().before(moFieldDateEnd_n.getDate()))) {
                        validation.setMessage("La fecha del campo '" + jlDateEnd_n.getText() + "' no puede ser posterior a la fecha " + miClient.getSessionXXX().getFormatters().getDateFormat().format(moPanelPkAccountId.getDataAccountMajor().getDateEnd_n()) + ".");
                        validation.setComponent(jftDateEnd_n);
                    }
                }
            }
            else if (mnFormType == SDataConstantsSys.FINX_ACC_DEEP) {
                 if (moAccount == null) { 
                     if (SLibUtils.parseInt(moPanelPkAccountChangeDeep.getFieldAccountNumber2().getValue()) <= 0) {
                        validation.setMessage("El valor especificado en el campo 'No. cuenta contable' debe ser mayor a 0.");
                        validation.setComponent(moPanelPkAccountChangeDeep.getFieldAccountNumber2());
                     }
                     else if (SDataUtilities.callProcedureVal(miClient, SProcConstants.FIN_ACC_VAL,
                            new Object[] { moPanelPkAccountChangeDeep.getStringAccountId() },
                            SLibConstants.EXEC_MODE_VERBOSE) > 0) {
                        validation.setMessage("El valor especificado en el campo 'No. cuenta contable' ya existe.");
                        validation.setComponent(moPanelPkAccountChangeDeep.getFieldAccountNumber2());
                    }
                    else if (moPanelPkAccountChangeDeep.getDataAccountMajor().getDateStart().after(moFieldDateStart.getDate())) {
                        validation.setMessage("La fecha del campo '" + jlDateStart.getText() + "' no puede ser anterior a la fecha " + miClient.getSessionXXX().getFormatters().getDateFormat().format(moPanelPkAccountChangeDeep.getDataAccountMajor().getDateStart()) + ".");
                        validation.setComponent(jftDateStart);
                    }
                    else if (moPanelPkAccountChangeDeep.getDataAccountMajor().getDateEnd_n() != null && (moFieldDateEnd_n.getDate() == null || moPanelPkAccountChangeDeep.getDataAccountMajor().getDateEnd_n().before(moFieldDateEnd_n.getDate()))) {
                        validation.setMessage("La fecha del campo '" + jlDateEnd_n.getText() + "' no puede ser posterior a la fecha " + miClient.getSessionXXX().getFormatters().getDateFormat().format(moPanelPkAccountChangeDeep.getDataAccountMajor().getDateEnd_n()) + ".");
                        validation.setComponent(jftDateEnd_n);
                    }
                }
               
            }  

            if (!validation.getIsError()) {
                if (moFieldDateEnd_n.getDate() != null && moFieldDateEnd_n.getDate().before(moFieldDateStart.getDate())) {
                    validation.setMessage("La fecha del campo '" + jlDateEnd_n.getText() + "' no puede ser anterior a la fecha del campo '" + jlDateStart.getText() + "'.");
                    validation.setComponent(jftDateEnd_n);
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
        moAccount = (SDataAccount) registry;
        if (mnFormType == SDataConstantsSys.FINX_ACC) {
            moPanelPkAccountId.getFieldAccount().setFieldValue(moAccount.getPkAccountIdXXX());
        }
        else if (mnFormType == SDataConstantsSys.FINX_ACC_DEEP) {
            //moPanelPkAccountChangeDeep.getFieldAccount().setFieldValue(moAccount.getPkAccountIdXXX());
        }
        
        moFieldAccount.setFieldValue(moAccount.getAccount());
        moFieldDateStart.setFieldValue(moAccount.getDateStart());
        moFieldDateEnd_n.setFieldValue(moAccount.getDateEnd_n());
        moFieldIsActive.setFieldValue(moAccount.getIsActive());
        moFieldIsDeleted.setFieldValue(moAccount.getIsDeleted());
        moFieldFkAccountEbitdaTypeId.setFieldValue(new int[] { moAccount.getFkAccountEbitdaTypeId() });
        moFieldFkAdministrativeConceptTypeId.setFieldValue(new int[] { moAccount.getFkAdministrativeConceptTypeId() });
        moFieldFkTaxableConceptTypeId.setFieldValue(new int[] { moAccount.getFkTaxableConceptTypeId() });
        moFieldFkFiscalAccountId.setFieldValue(new int[] { moAccount.getFkFiscalAccountId() });
        
        if (mnFormType == SDataConstantsSys.FINX_ACC) {
            moPanelPkAccountId.enableFields(false);
            moPanelPkAccountId.refreshPanel();
        }
        else if (mnFormType == SDataConstantsSys.FINX_ACC_DEEP) {
            moPanelPkAccountChangeDeep.refreshPanel();
        }
        jckIsDeleted.setEnabled(true);
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moAccount == null) {
            moAccount = new SDataAccount();
            
            if (mnFormType == SDataConstantsSys.FINX_ACC) {
                moAccount.setPkAccountIdXXX(moPanelPkAccountId.getFieldAccount().getString());
            }
            else if (mnFormType == SDataConstantsSys.FINX_ACC_DEEP) {
                moAccount.setPkAccountIdXXX(moPanelPkAccountChangeDeep.getStringAccountId());
            }            

            moAccount.setFkAccountUserTypeId(SDataConstantsSys.FINU_CLS_ACC_USR_NA[0]);
            moAccount.setFkAccountUserClassId(SDataConstantsSys.FINU_CLS_ACC_USR_NA[1]);
            moAccount.setFkAccountUserSubclassId(SDataConstantsSys.FINU_CLS_ACC_USR_NA[2]);

            moAccount.setFkAccountSpecializedTypeId(SDataConstantsSys.FINS_TP_ACC_SPE_NA);
            moAccount.setFkAccountSystemTypeId(SDataConstantsSys.FINS_TP_ACC_SYS_NA);
            moAccount.setFkAccountLedgerTypeId(SDataConstantsSys.NA);

            moAccount.setFkAssetFixedTypeId(SDataConstantsSys.NA);
            moAccount.setFkAssetDifferredTypedId(SDataConstantsSys.NA);
            moAccount.setFkLiabilityDifferredTypeId(SDataConstantsSys.NA);
            moAccount.setFkExpenseOperativeTypeId(SDataConstantsSys.NA);

            moAccount.setDeep(0);   // for all non major accounts deep is 0
            
            if (mnFormType == SDataConstantsSys.FINX_ACC) {
                moAccount.setLevel(moPanelPkAccountId.getFieldAccount().getUsedLevelsCount());
            }
            else if (mnFormType == SDataConstantsSys.FINX_ACC_DEEP) {
                moAccount.setLevel(2);
            }

            moAccount.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moAccount.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        if (mnFormType == SDataConstantsSys.FINX_ACC) {
            moAccount.setCode(SAccountUtils.convertCodeStd(moPanelPkAccountId.getFieldAccount().getString()));
        }
        else if (mnFormType == SDataConstantsSys.FINX_ACC_DEEP) {
            moAccount.setCode(SAccountUtils.convertCodeStd(moPanelPkAccountChangeDeep.getStringAccountId()));
        }
        
        moAccount.setAccount(moFieldAccount.getString());
        moAccount.setDateStart(moFieldDateStart.getDate());
        moAccount.setDateEnd_n(moFieldDateEnd_n.getDate());
        moAccount.setIsActive(moFieldIsActive.getBoolean());
        moAccount.setIsDeleted(moFieldIsDeleted.getBoolean());
        moAccount.setFkAccountEbitdaTypeId(moFieldFkAccountEbitdaTypeId.getKeyAsIntArray()[0]);
        moAccount.setFkAdministrativeConceptTypeId(moFieldFkAdministrativeConceptTypeId.getKeyAsIntArray()[0]);
        moAccount.setFkTaxableConceptTypeId(moFieldFkTaxableConceptTypeId.getKeyAsIntArray()[0]);
        moAccount.setFkFiscalAccountId(moFieldFkFiscalAccountId.getKeyAsIntArray()[0]);

        return moAccount;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case SDataConstants.FIN_ACC:
                moAccountMajor = (SDataAccount) value;
                break;
            default:
        }
        moPanelPkAccountChangeDeep.setDataAccountMajor(moAccountMajor);
        moPanelPkAccountChangeDeep.refreshPanel();
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
            JButton button = (JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbDateStart) {
                actionDateStart();
            }
            else if (button == jbDateEnd_n) {
                actionDateEnd_n();
            }
            else if (button == jbFkAccountEbitdaTypeId) {
                actionFkAccountEbitdaTypeId();
            }
            else if (button == jbFkAdministrativeConceptTypeId) {
                actionFkAdministrativeConceptTypeId();
            }
            else if (button == jbFkTaxableConceptTypeId) {
                actionFkTaxableConceptTypeId();
            }
            else if (button == jbFkFiscalAccountId) {
                actionFkFiscalAccountId();
            }
        }
    }
}
