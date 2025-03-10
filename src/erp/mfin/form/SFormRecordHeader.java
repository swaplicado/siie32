/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.form;

import erp.data.SDataConstants;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;

/**
 *
 * @author  Sergio Flores
 */
public class SFormRecordHeader extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.ItemListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private boolean mbIsCopy;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mfin.data.SDataRecord moRecord;
    private erp.lib.form.SFormField moFieldDate;
    private erp.lib.form.SFormField moFieldPkRecordTypeId;
    private erp.lib.form.SFormField moFieldConcept;
    private erp.lib.form.SFormField moFieldFkAccountCashId_n;
    private java.lang.String msDefaultRecordType;

    /** Creates new form SFormRecordHeader */
    public SFormRecordHeader(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.FINX_REC_HEADER;
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        jftDate = new javax.swing.JFormattedTextField();
        jbDate = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jlPeriod = new javax.swing.JLabel();
        jtfPeriod = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jlCompanyBranch = new javax.swing.JLabel();
        jtfCompanyBranch = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jlPkRecordTypeId = new javax.swing.JLabel();
        jcbPkRecordTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jbPkRecordTypeId = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jlPkNumberId = new javax.swing.JLabel();
        jtfPkNumberId = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jlConcept = new javax.swing.JLabel();
        jtfConcept = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jlFkAccountCashId_n = new javax.swing.JLabel();
        jcbFkAccountCashId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkAccountCashId_n = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jckShowRecordAdjYearEnd = new javax.swing.JCheckBox();
        jPanel13 = new javax.swing.JPanel();
        jckShowRecordAdjAudit = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Póliza contable"); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel3.setLayout(new java.awt.BorderLayout(5, 5));

        jPanel4.setLayout(new java.awt.GridLayout(11, 1, 5, 2));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDate.setText("Fecha póliza: *");
        jlDate.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel11.add(jlDate);

        jftDate.setText("yyyy/mm/dd");
        jftDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jftDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jftDateFocusLost(evt);
            }
        });
        jPanel11.add(jftDate);

        jbDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDate.setToolTipText("Seleccionar fecha");
        jbDate.setFocusable(false);
        jbDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbDate);

        jPanel4.add(jPanel11);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlPeriod.setText("Período contable:");
        jlPeriod.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jlPeriod);

        jtfPeriod.setEditable(false);
        jtfPeriod.setText("yyyy-dd");
        jtfPeriod.setFocusable(false);
        jtfPeriod.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jtfPeriod);

        jPanel4.add(jPanel6);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCompanyBranch.setText("Sucursal empresa:");
        jlCompanyBranch.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel12.add(jlCompanyBranch);

        jtfCompanyBranch.setEditable(false);
        jtfCompanyBranch.setText("COMPANY BRANCH");
        jtfCompanyBranch.setFocusable(false);
        jtfCompanyBranch.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel12.add(jtfCompanyBranch);

        jPanel4.add(jPanel12);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlPkRecordTypeId.setForeground(java.awt.Color.blue);
        jlPkRecordTypeId.setText("Tipo póliza: *");
        jlPkRecordTypeId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel1.add(jlPkRecordTypeId);

        jcbPkRecordTypeId.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel1.add(jcbPkRecordTypeId);

        jbPkRecordTypeId.setText("...");
        jbPkRecordTypeId.setToolTipText("Seleccionar tipo de póliza");
        jbPkRecordTypeId.setFocusable(false);
        jbPkRecordTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel1.add(jbPkRecordTypeId);

        jPanel4.add(jPanel1);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlPkNumberId.setText("Número póliza:");
        jlPkNumberId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel5.add(jlPkNumberId);

        jtfPkNumberId.setEditable(false);
        jtfPkNumberId.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPkNumberId.setText("0");
        jtfPkNumberId.setFocusable(false);
        jtfPkNumberId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jtfPkNumberId);

        jPanel4.add(jPanel5);
        jPanel4.add(jPanel9);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlConcept.setText("Concepto póliza: *");
        jlConcept.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel7.add(jlConcept);

        jtfConcept.setText("CONCEPT");
        jtfConcept.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel7.add(jtfConcept);

        jPanel4.add(jPanel7);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkAccountCashId_n.setForeground(java.awt.Color.blue);
        jlFkAccountCashId_n.setText("Cuenta dinero: *");
        jlFkAccountCashId_n.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel15.add(jlFkAccountCashId_n);

        jcbFkAccountCashId_n.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel15.add(jcbFkAccountCashId_n);

        jbFkAccountCashId_n.setText("...");
        jbFkAccountCashId_n.setToolTipText("Seleccionar cuenta de efectivo");
        jbFkAccountCashId_n.setFocusable(false);
        jbFkAccountCashId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel15.add(jbFkAccountCashId_n);

        jPanel4.add(jPanel15);
        jPanel4.add(jPanel10);

        jPanel8.setPreferredSize(new java.awt.Dimension(91, 23));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jckShowRecordAdjYearEnd.setText("Es póliza de ajuste de cierre");
        jckShowRecordAdjYearEnd.setFocusable(false);
        jPanel8.add(jckShowRecordAdjYearEnd);

        jPanel4.add(jPanel8);

        jPanel13.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jckShowRecordAdjAudit.setText("Es póliza de ajuste de auditoría");
        jckShowRecordAdjAudit.setFocusable(false);
        jckShowRecordAdjAudit.setOpaque(false);
        jPanel13.add(jckShowRecordAdjAudit);

        jPanel4.add(jPanel13);

        jPanel3.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar"); // NOI18N
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jbOk);

        jbCancel.setText("Cancelar"); // NOI18N
        jbCancel.setToolTipText("[Escape]");
        jPanel2.add(jbCancel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(600, 400));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jftDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jftDateFocusLost
        if (moRecord == null || mbIsCopy) {
            renderPeriod();
        }
    }//GEN-LAST:event_jftDateFocusLost

    private void initComponentsExtra() {
        mvFields = new Vector<SFormField>();

        moFieldDate = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDate, jlDate);
        moFieldDate.setPickerButton(jbDate);
        moFieldPkRecordTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkRecordTypeId, jlPkRecordTypeId);
        moFieldPkRecordTypeId.setPickerButton(jbPkRecordTypeId);
        moFieldConcept = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfConcept, jlConcept);
        moFieldConcept.setLengthMax(100);
        moFieldFkAccountCashId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkAccountCashId_n, jlFkAccountCashId_n);
        moFieldFkAccountCashId_n.setPickerButton(jbFkAccountCashId_n);

        mvFields.add(moFieldDate);
        mvFields.add(moFieldPkRecordTypeId);
        mvFields.add(moFieldConcept);
        mvFields.add(moFieldFkAccountCashId_n);

        msDefaultRecordType = "";

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbDate.addActionListener(this);
        jbPkRecordTypeId.addActionListener(this);
        jbFkAccountCashId_n.addActionListener(this);

        jcbPkRecordTypeId.addItemListener(this);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionPerformedOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionCancel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionPerformedCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;

            if (miClient.getSessionXXX().getCurrentCompanyBranchId() == 0) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_SESSION_BRANCH);
                actionPerformedCancel();
            }
            else {
                jftDate.requestFocus();
            }
        }
    }

    private void renderPeriod() {
        java.util.Date date = moFieldDate.getDate();

        if (date == null) {
            jtfPeriod.setText("");
        }
        else {
            jtfPeriod.setText(miClient.getSessionXXX().getFormatters().getDateYearMonthFormat().format(date));
        }
    }

    private void renderCompanyBranch() {
        jtfCompanyBranch.setText(moRecord == null ? miClient.getSessionXXX().getCurrentCompanyBranchName() : SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.BPSU_BPB, new int[] { moRecord.getFkCompanyBranchId() }));
        jtfCompanyBranch.setToolTipText(jtfCompanyBranch.getText());
        jtfCompanyBranch.setCaretPosition(0);
    }

    private void itemStateChangedPkRecordTypeId() {
        boolean enable = false;

        if (jcbPkRecordTypeId.getSelectedIndex() > 0) {
            if (((SFormComponentItem) jcbPkRecordTypeId.getSelectedItem()).getComplement() != null) {
                enable = (Boolean) ((SFormComponentItem) jcbPkRecordTypeId.getSelectedItem()).getComplement();
            }
        }

        jlFkAccountCashId_n.setEnabled(enable);
        jcbFkAccountCashId_n.setEnabled(enable);
        jbFkAccountCashId_n.setEnabled(enable);

        jckShowRecordAdjYearEnd.setEnabled(!enable && jcbPkRecordTypeId.getSelectedIndex() > 0);
        jckShowRecordAdjAudit.setEnabled(!enable && jcbPkRecordTypeId.getSelectedIndex() > 0);
        jckShowRecordAdjYearEnd.setSelected(false);
        jckShowRecordAdjAudit.setSelected(false);
    }

    private void actionPerformedDate() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDate.getDate(), moFieldDate);
    }

    private void actionPerformedPkRecordTypeId() {
        miClient.pickOption(SDataConstants.FINX_TP_REC_USER, moFieldPkRecordTypeId, null);
    }

    private void actionPerformedFkAccountCashId_n() {
        miClient.pickOption(SDataConstants.FIN_ACC_CASH, moFieldFkAccountCashId_n, new int[] { miClient.getSessionXXX().getCurrentCompanyBranchId() });
    }

    private void actionEdit(boolean edit) {

    }

    private void actionPerformedOk() {
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

    private void actionPerformedCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDate;
    private javax.swing.JButton jbFkAccountCashId_n;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPkRecordTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkAccountCashId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbPkRecordTypeId;
    private javax.swing.JCheckBox jckShowRecordAdjAudit;
    private javax.swing.JCheckBox jckShowRecordAdjYearEnd;
    private javax.swing.JFormattedTextField jftDate;
    private javax.swing.JLabel jlCompanyBranch;
    private javax.swing.JLabel jlConcept;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlFkAccountCashId_n;
    private javax.swing.JLabel jlPeriod;
    private javax.swing.JLabel jlPkNumberId;
    private javax.swing.JLabel jlPkRecordTypeId;
    private javax.swing.JTextField jtfCompanyBranch;
    private javax.swing.JTextField jtfConcept;
    private javax.swing.JTextField jtfPeriod;
    private javax.swing.JTextField jtfPkNumberId;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        jtfPkNumberId.setText("" + 0);
        jlFkAccountCashId_n.setEnabled(false);
        jcbFkAccountCashId_n.setEnabled(false);
        jbFkAccountCashId_n.setEnabled(false);
    }

    @Override
    public void formReset() {
        mbResetingForm = true;

        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        moRecord = null;

        for (int i = 0; i < mvFields.size(); i++) {
            mvFields.get(i).resetField();
        }

        moFieldDate.setFieldValue(miClient.getSessionXXX().getWorkingDate());
        jtfPkNumberId.setText("" + 0);

        if (msDefaultRecordType.length() > 0) {
            moFieldPkRecordTypeId.setFieldValue(new Object[] { msDefaultRecordType });
        }

        jcbPkRecordTypeId.setEnabled(true);
        jbPkRecordTypeId.setEnabled(true);

        renderPeriod();
        renderCompanyBranch();
        itemStateChangedPkRecordTypeId();

        mbResetingForm = false;
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbPkRecordTypeId, SDataConstants.FINX_TP_REC_USER);
        SFormUtilities.populateComboBox(miClient, jcbFkAccountCashId_n, SDataConstants.FIN_ACC_CASH, new int[] { miClient.getSessionXXX().getCurrentCompanyBranchId() });
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
            // Validate accounting record period:

            if (moRecord == null || mbIsCopy) {
                if (!SDataUtilities.isPeriodOpen(miClient, moFieldDate.getDate())) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
                    validation.setComponent(jftDate);
                }
            }
            else if (!SLibTimeUtilities.isBelongingToPeriod(moFieldDate.getDate(), moRecord.getPkYearId(), moRecord.getPkPeriodId())) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_DATE_REC);
                validation.setComponent(jftDate);
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
        mbResetingForm = true;

        moRecord = (SDataRecord) registry;

        moFieldDate.setDate(moRecord.getDate());
        moFieldPkRecordTypeId.setFieldValue(new Object[] { moRecord.getPkRecordTypeId() });
        moFieldConcept.setFieldValue(moRecord.getConcept());
        moFieldFkAccountCashId_n.setFieldValue(new int[] { moRecord.getFkCompanyBranchId_n(), moRecord.getFkAccountCashId_n() });

        jtfPkNumberId.setText("" + moRecord.getPkNumberId());

        jcbPkRecordTypeId.setEnabled(false);
        jbPkRecordTypeId.setEnabled(false);

        renderPeriod();
        renderCompanyBranch();
        itemStateChangedPkRecordTypeId();

        mbResetingForm = false;
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        int[] period = SLibTimeUtilities.digestYearMonth(moFieldDate.getDate());

        if (moRecord == null) {
            moRecord = new SDataRecord();
            moRecord.setPkYearId(period[0]);
            moRecord.setPkPeriodId(period[1]);
            moRecord.setPkBookkeepingCenterId(miClient.getSessionXXX().getCurrentCompanyBranch().getDbmsDataCompanyBranchBkc().getPkBookkepingCenterId());
            moRecord.setPkRecordTypeId((String) moFieldPkRecordTypeId.getKeyAsObjectArray()[0]);
            moRecord.setPkNumberId(0);
            moRecord.setIsAdjustmentsYearEnd(jckShowRecordAdjYearEnd.isSelected());
            moRecord.setIsAdjustmentsAudit(jckShowRecordAdjAudit.isSelected());
            moRecord.setIsAudited(false);
            moRecord.setIsAuthorized(false);
            moRecord.setIsSystem(false);
            moRecord.setFkCompanyBranchId(miClient.getSessionXXX().getCurrentCompanyBranchId());
            moRecord.setFkCompanyBranchId_n(!jcbFkAccountCashId_n.isEnabled() ? 0 : moFieldFkAccountCashId_n.getKeyAsIntArray()[0]);
            moRecord.setFkAccountCashId_n(!jcbFkAccountCashId_n.isEnabled() ? 0 : moFieldFkAccountCashId_n.getKeyAsIntArray()[1]);
            moRecord.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else if (mbIsCopy) {
            moRecord.setPkYearId(period[0]);
            moRecord.setPkPeriodId(period[1]);
            moRecord.setPkNumberId(0);
            moRecord.setIsAdjustmentsYearEnd(jckShowRecordAdjYearEnd.isSelected());
            moRecord.setIsAdjustmentsAudit(jckShowRecordAdjAudit.isSelected());
            moRecord.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

            for (SDataRecordEntry entry : moRecord.getDbmsRecordEntries()) {
                entry.setPkEntryId(0);
                entry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                entry.setIsSystem(false);
                entry.setIsRegistryNew(true);
            }
        }
        else {
            moRecord.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        mbIsCopy = false;

        moRecord.setDate(moFieldDate.getDate());
        moRecord.setConcept(moFieldConcept.getString());

        return moRecord;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case SDataConstants.FINU_TP_REC:
                msDefaultRecordType = (String) value;
                break;
            case SLibConstants.VALUE_IS_COPY:
                mbIsCopy = (Boolean) value;
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
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbOk) {
                actionPerformedOk();
            }
            else if (button == jbCancel) {
                actionPerformedCancel();
            }
            else if (button == jbDate) {
                actionPerformedDate();
            }
            else if (button == jbPkRecordTypeId) {
                actionPerformedPkRecordTypeId();
            }
            else if (button == jbFkAccountCashId_n) {
                actionPerformedFkAccountCashId_n();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (!mbResetingForm) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getSource() instanceof JComboBox) {
                    JComboBox comboBox = (JComboBox) e.getSource();

                    if (comboBox == jcbPkRecordTypeId) {
                        itemStateChangedPkRecordTypeId();
                    }
                }
            }
        }
    }
}
