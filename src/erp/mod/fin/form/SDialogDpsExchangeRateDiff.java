/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.form;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mcfg.data.SDataParamsCompany;
import erp.mfin.data.SDataRecord;
import erp.mfin.form.SDialogRecordPicker;
import erp.mod.fin.db.SFinDpsExchangeRateDiff;
import erp.mod.fin.db.SFinConsts;
import erp.redis.SRedisLockUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFormDialog;
//import sa.lib.srv.SSrvLock;
import sa.lib.srv.SSrvUtils;
import sa.lib.srv.redis.SRedisLock;

/**
 *
 * @author Gerardo Hernández, Uriel Castañeda, Isabel Servín
 */
public class SDialogDpsExchangeRateDiff extends SBeanFormDialog implements ActionListener {

    private erp.mfin.form.SDialogRecordPicker moDialogRecordPicker;
    private erp.mfin.data.SDataRecord moRecord;
    
    /**
     * Creates new form SDialogValuationBalances
     * @param client
     * @param title
     */
    public SDialogDpsExchangeRateDiff(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, title);
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
        jPanel10 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        moDateDate = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel12 = new javax.swing.JPanel();
        jlRecord = new javax.swing.JLabel();
        moTextRecordDate = new sa.lib.gui.bean.SBeanFieldText();
        moTextRecordBkc = new sa.lib.gui.bean.SBeanFieldText();
        moTextRecordNumber = new sa.lib.gui.bean.SBeanFieldText();
        jbPickRecord = new javax.swing.JButton();
        moTextRecordBranch = new sa.lib.gui.bean.SBeanFieldText();
        jPanel13 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros para generar diferencias cambiarias:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setMinimumSize(new java.awt.Dimension(211, 130));
        jPanel2.setName(""); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel10.setMinimumSize(new java.awt.Dimension(211, 20));
        jPanel10.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate.setText("Fecha corte:*");
        jlDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlDate);
        jPanel10.add(moDateDate);

        jPanel2.add(jPanel10);

        jPanel12.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel12.setRequestFocusEnabled(false);
        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRecord.setText("Póliza contable:*");
        jlRecord.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlRecord);

        moTextRecordDate.setEditable(false);
        moTextRecordDate.setText("01/01/2000");
        moTextRecordDate.setToolTipText("Fecha de la póliza contable");
        moTextRecordDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel12.add(moTextRecordDate);

        moTextRecordBkc.setEditable(false);
        moTextRecordBkc.setText("BKC");
        moTextRecordBkc.setToolTipText("Centro contable de la póliza contable");
        moTextRecordBkc.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel12.add(moTextRecordBkc);

        moTextRecordNumber.setEditable(false);
        moTextRecordNumber.setText("TP-000001");
        moTextRecordNumber.setToolTipText("Número de la póliza contable");
        jPanel12.add(moTextRecordNumber);

        jbPickRecord.setText("...");
        jbPickRecord.setToolTipText("Seleccionar póliza contable");
        jbPickRecord.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel12.add(jbPickRecord);

        moTextRecordBranch.setEditable(false);
        moTextRecordBranch.setText("BRA");
        moTextRecordBranch.setToolTipText("Sucursal de la empresa");
        moTextRecordBranch.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel12.add(moTextRecordBranch);

        jPanel2.add(jPanel12);

        jPanel13.setMinimumSize(new java.awt.Dimension(211, 25));
        jPanel13.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel1.setForeground(java.awt.SystemColor.textInactiveText);
        jLabel1.setText("NOTA 1: se ajustarán todas las cuentas liquidadas hasta la fecha de corte.");
        jLabel1.setPreferredSize(new java.awt.Dimension(450, 23));
        jPanel13.add(jLabel1);

        jPanel2.add(jPanel13);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel2.setForeground(java.awt.SystemColor.textInactiveText);
        jLabel2.setText("NOTA 2: se afectarán cuentas de asociados de negocios.");
        jLabel2.setPreferredSize(new java.awt.Dimension(450, 23));
        jPanel11.add(jLabel2);

        jPanel2.add(jPanel11);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        jPanel1.getAccessibleContext().setAccessibleName("");
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbPickRecord;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlRecord;
    private sa.lib.gui.bean.SBeanFieldDate moDateDate;
    private sa.lib.gui.bean.SBeanFieldText moTextRecordBkc;
    private sa.lib.gui.bean.SBeanFieldText moTextRecordBranch;
    private sa.lib.gui.bean.SBeanFieldText moTextRecordDate;
    private sa.lib.gui.bean.SBeanFieldText moTextRecordNumber;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */
    
    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 480, 300);
        
        try {
            validateCompanyConfig();
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
            actionCancel();
        }
        
        moDialogRecordPicker = new SDialogRecordPicker((SClientInterface) miClient, SDataConstants.FINX_REC_USER);
        
        moDateDate.setDateSettings(miClient, SGuiUtils.getLabelName(jlDate), true);
        
        moFields.addField(moDateDate);
        moFields.setFormButton(jbSave);
        
        moDateDate.setValue(SLibTimeUtils.getEndOfMonth(miClient.getSession().getCurrentDate()));
        
        renderRecord();
        
        addAllListeners();
    }
    
    private void validateCompanyConfig() throws Exception {
        SDataParamsCompany paramsCompany = ((SClientInterface) miClient).getSessionXXX().getParamsCompany(); // convenience variable
        if (!SDataUtilities.validateAccountSyntax(paramsCompany.getFkAccountDifferenceIncomeId_n())) {
            throw new Exception(SFinConsts.MSG_ERR_GUI_CFG_DIFF_ACC + " (Ingresos)");
        }
        else if (!SDataUtilities.validateAccountSyntax(paramsCompany.getFkCostCenterDifferenceIncomeId_n())) {
            throw new Exception(SFinConsts.MSG_ERR_GUI_CFG_DIFF_CC + " (Ingresos)");
        }
        else if (!SDataUtilities.validateAccountSyntax(paramsCompany.getFkAccountDifferenceExpenseId_n())) {
            throw new Exception(SFinConsts.MSG_ERR_GUI_CFG_DIFF_ACC + " (Egresos)");
        }
        else if (!SDataUtilities.validateAccountSyntax(paramsCompany.getFkCostCenterDifferenceExpenseId_n())) {
            throw new Exception(SFinConsts.MSG_ERR_GUI_CFG_DIFF_CC + " (Egresos)");
        }
    }
    
    private void renderRecord() {
        if (moRecord == null) {
            moTextRecordDate.setValue("");
            moTextRecordBkc.setValue("");
            moTextRecordNumber.setValue("");
            moTextRecordBranch.setValue("");
          }
        else {
            moTextRecordDate.setValue(SLibUtils.DateFormatDate.format(moRecord.getDate()));
            moTextRecordBkc.setValue(SDataReadDescriptions.getCatalogueDescription(
                    (SClientInterface) miClient, SDataConstants.FIN_BKC, new int[] { moRecord.getPkBookkeepingCenterId() }, SLibConstants.DESCRIPTION_CODE));
            moTextRecordNumber.setValue(moRecord.getRecordNumber());
            moTextRecordBranch.setValue(SDataReadDescriptions.getCatalogueDescription(
                    (SClientInterface) miClient, SDataConstants.BPSU_BPB, new int[] { moRecord.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
        }
    }

    private void actionPerformedPickRecord() {
        String message = "";
        
        moDialogRecordPicker.formReset();
        moDialogRecordPicker.setFilterKey(moDateDate.getValue());
        moDialogRecordPicker.formRefreshOptionPane();
        if (moRecord != null) {
            moDialogRecordPicker.setSelectedPrimaryKey(moRecord.getPrimaryKey());
        }
        moDialogRecordPicker.setFormVisible(true);
        
        if (moDialogRecordPicker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moRecord = (SDataRecord) SDataUtilities.readRegistry(
                    (SClientInterface) miClient, SDataConstants.FIN_REC, moDialogRecordPicker.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
                    
            if (moRecord != null) {
                if (moRecord.getIsSystem()) {
                    message = "No puede seleccionarse esta póliza contable porque es de sistema.";
                }
                else if (moRecord.getIsAudited()) {
                    message = "No puede seleccionarse esta póliza contable porque está auditada.";
                }
                else if (moRecord.getIsAuthorized()) {
                    message = "No puede seleccionarse esta póliza contable porque está autorizada.";
                }
                else if (!SDataUtilities.isPeriodOpen((SClientInterface) miClient, moRecord.getDate())) {
                    message = "No puede seleccionarse esta póliza contable porque su período contable correspondiente está cerrado.";
                }

                if (!message.isEmpty()) {
                    miClient.showMsgBoxWarning(message);
                    moRecord = null;
                }
                else {
                    renderRecord();
                }
            }
        }
    }

    /*
     * Public methods
     */
    
    /*
     * Implemented and overriden methods
     */
    
    @Override
    public void addAllListeners() {
        jbPickRecord.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbPickRecord.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
        
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
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (moRecord == null) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlRecord.getText()) + "'.");
                validation.setComponent(jbPickRecord);
            }
        }
        
        return validation;
    }

    @Override
    public void actionSave() {
        String msg;
//        SSrvLock lock = null;
        SRedisLock rlock = null;
        SFinDpsExchangeRateDiff dpsExchangeRateDiff;
        
        if (SGuiUtils.computeValidation(miClient, validateForm())) {
            msg = "Se determinarán las diferencias cambiarias con los siguientes parámetros:\n" +
                    "- período contable: " + moRecord.getRecordPeriod() + "\n" + 
                    "- fecha de corte: " + SLibUtils.DateFormatDate.format(moDateDate.getValue()) + "\n" +
                    SGuiConsts.MSG_CNF_CONT;
            
            if (miClient.showMsgBoxConfirm(msg) == JOptionPane.YES_OPTION) {
                try {
//                    lock = SSrvUtils.gainLock(miClient.getSession(), ((SClientInterface) miClient).getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.FIN_REC, moRecord.getPrimaryKey(), moRecord.getRegistryTimeout());
                    rlock = SRedisLockUtils.gainLock((SClientInterface) miClient, SDataConstants.FIN_REC, moRecord.getPrimaryKey(), moRecord.getRegistryTimeout() / 1000);
                    dpsExchangeRateDiff = new SFinDpsExchangeRateDiff(miClient);
                    dpsExchangeRateDiff.setRecYear(SLibTimeUtils.digestYear(moDateDate.getValue())[0]);
                    dpsExchangeRateDiff.setEndOfMonth(moDateDate.getValue());
                    dpsExchangeRateDiff.setRecord(moRecord);
                    dpsExchangeRateDiff.save();
                    
                    miClient.showMsgBoxInformation(SLibConsts.MSG_PROCESS_FINISHED);
                    
                    mnFormResult = SGuiConsts.FORM_RESULT_OK;
                    dispose();
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
                finally {
                    try {
//                        if (lock != null) {
//                            SSrvUtils.releaseLock(miClient.getSession(), lock);                            
//                        }
                        if (rlock != null) {
                            SRedisLockUtils.releaseLock((SClientInterface) miClient, rlock);
                        }
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbPickRecord) {
                actionPerformedPickRecord();
            }
        }
    }
}
