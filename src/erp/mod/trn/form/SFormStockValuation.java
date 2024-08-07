/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.form;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mfin.data.SDataRecord;
import erp.mfin.form.SDialogRecordPicker;
import erp.mod.SModConsts;
import erp.mod.trn.db.SDbStockValuation;
import erp.mod.trn.db.SStockValuationUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;

/**
 *
 * @author Edwin Carmona
 * 
 */
public class SFormStockValuation extends sa.lib.gui.bean.SBeanForm implements ActionListener {

    private SDbStockValuation moRegistry;
    private erp.mfin.form.SDialogRecordPicker moDialogRecordPicker;
    private erp.mfin.data.SDataRecord moCurrentRecord;

    /**
     * Creates new form SFormItemCost
     * @param client
     * @param title
     */
    public SFormStockValuation(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.TRN_STK_VAL, SLibConstants.UNDEFINED, title);
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
        jPanel1 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        moDateDateEnd = new sa.lib.gui.bean.SBeanFieldDate();
        jpAccountingRecord = new javax.swing.JPanel();
        jlRecord = new javax.swing.JLabel();
        jtfRecordDate = new javax.swing.JTextField();
        jtfRecordBkc = new javax.swing.JTextField();
        jtfRecordBranch = new javax.swing.JTextField();
        jtfRecordNumber = new javax.swing.JTextField();
        jbPickRecord = new javax.swing.JButton();
        jlDummy3 = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setPreferredSize(new java.awt.Dimension(750, 450));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel23.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha de corte:*");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel21.add(jlDateEnd);
        jPanel21.add(moDateDateEnd);

        jPanel23.add(jPanel21);

        jpAccountingRecord.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRecord.setText("Póliza contable:");
        jlRecord.setPreferredSize(new java.awt.Dimension(100, 23));
        jpAccountingRecord.add(jlRecord);

        jtfRecordDate.setEditable(false);
        jtfRecordDate.setText("01/01/2000");
        jtfRecordDate.setToolTipText("Fecha de la póliza contable");
        jtfRecordDate.setFocusable(false);
        jtfRecordDate.setPreferredSize(new java.awt.Dimension(65, 23));
        jpAccountingRecord.add(jtfRecordDate);

        jtfRecordBkc.setEditable(false);
        jtfRecordBkc.setText("BKC");
        jtfRecordBkc.setToolTipText("Centro contable");
        jtfRecordBkc.setFocusable(false);
        jtfRecordBkc.setPreferredSize(new java.awt.Dimension(35, 23));
        jpAccountingRecord.add(jtfRecordBkc);

        jtfRecordBranch.setEditable(false);
        jtfRecordBranch.setText("BRA");
        jtfRecordBranch.setToolTipText("Sucursal de la empresa");
        jtfRecordBranch.setFocusable(false);
        jtfRecordBranch.setPreferredSize(new java.awt.Dimension(35, 23));
        jpAccountingRecord.add(jtfRecordBranch);

        jtfRecordNumber.setEditable(false);
        jtfRecordNumber.setText("TP-000001");
        jtfRecordNumber.setToolTipText("Número de póliza contable");
        jtfRecordNumber.setFocusable(false);
        jtfRecordNumber.setPreferredSize(new java.awt.Dimension(65, 23));
        jpAccountingRecord.add(jtfRecordNumber);

        jbPickRecord.setText("...");
        jbPickRecord.setToolTipText("Seleccionar póliza contable");
        jbPickRecord.setPreferredSize(new java.awt.Dimension(23, 23));
        jpAccountingRecord.add(jbPickRecord);

        jlDummy3.setPreferredSize(new java.awt.Dimension(122, 23));
        jpAccountingRecord.add(jlDummy3);

        jPanel23.add(jpAccountingRecord);

        jPanel1.add(jPanel23, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        renderCurrentRecord();
    }//GEN-LAST:event_formWindowActivated

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton jbPickRecord;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDummy3;
    private javax.swing.JLabel jlRecord;
    private javax.swing.JPanel jpAccountingRecord;
    private javax.swing.JTextField jtfRecordBkc;
    private javax.swing.JTextField jtfRecordBranch;
    private javax.swing.JTextField jtfRecordDate;
    private javax.swing.JTextField jtfRecordNumber;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateEnd;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 480, 250);
        
        moDateDateEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd), true);
        
        moFields.addField(moDateDateEnd);

        moFields.setFormButton(jbSave);
        
        moDialogRecordPicker = new SDialogRecordPicker((SClientInterface) miClient, SDataConstants.FINX_REC_USER);
        moCurrentRecord = null;
        
        jbPickRecord.addActionListener(this);
    }
    
    private void renderCurrentRecord() {
        if (moCurrentRecord == null) {
            jtfRecordDate.setText("");
            jtfRecordBranch.setText("");
            jtfRecordNumber.setText("");
        }
        else {
            jtfRecordDate.setText(((SClientInterface) miClient).getSessionXXX().getFormatters().getDateFormat().format(moCurrentRecord.getDate()));
            jtfRecordBkc.setText(SDataReadDescriptions.getCatalogueDescription(((SClientInterface) miClient), SDataConstants.FIN_BKC, new int[] { moCurrentRecord.getPkBookkeepingCenterId() }, SLibConstants.DESCRIPTION_CODE));
            jtfRecordBranch.setText(SDataReadDescriptions.getCatalogueDescription(((SClientInterface) miClient), SDataConstants.BPSU_BPB, new int[] { moCurrentRecord.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
            jtfRecordNumber.setText(moCurrentRecord.getPkRecordTypeId() + "-" + moCurrentRecord.getPkNumberId());
        }
    }
    
    private void actionPickRecord() {
        Object key = null;
        String message = "";

        moDialogRecordPicker.formReset();
        moDialogRecordPicker.setFilterKey(((SClientInterface) miClient).getSessionXXX().getWorkingDate());
        moDialogRecordPicker.formRefreshOptionPane();

        if (moCurrentRecord != null) {
            moDialogRecordPicker.setSelectedPrimaryKey(moCurrentRecord.getPrimaryKey());
        }

        moDialogRecordPicker.setFormVisible(true);

        if (moDialogRecordPicker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            key = moDialogRecordPicker.getSelectedPrimaryKey();

            // XXX set registry lock to accounting record

            moCurrentRecord = (SDataRecord) SDataUtilities.readRegistry(((SClientInterface) miClient), SDataConstants.FIN_REC, key, SLibConstants.EXEC_MODE_VERBOSE);
            
            if (moCurrentRecord == null) {
                message = "No se encontró la póliza contable seleccionada.";
            }
            else if (moCurrentRecord.getIsSystem()) {
                message = "No puede seleccionarse esta póliza contable porque es de sistema.";
            }
            else if (moCurrentRecord.getIsAudited()) {
                message = "No puede seleccionarse esta póliza contable porque está auditada.";
            }
            else if (moCurrentRecord.getIsAuthorized()) {
                message = "No puede seleccionarse esta póliza contable porque está autorizada.";
            }
            else if (!SDataUtilities.isPeriodOpen((SClientInterface) miClient, moCurrentRecord.getDate())) {
                message = "No puede seleccionarse esta póliza contable porque su período contable correspondiente está cerrado.";
            }

            if (message.length() > 0) {
                miClient.showMsgBoxWarning(message);
                moCurrentRecord = null;
            }
            else {
                renderCurrentRecord();
            }
        }
    }
    
    @Override
    public void addAllListeners() {
    }

    @Override
    public void removeAllListeners() {
    }

    @Override
    public void reloadCatalogues() {
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbStockValuation) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            moRegistry.setDateEnd(miClient.getSession().getCurrentDate());
            jtfRegistryKey.setText("");
            jtfRecordDate.setText("");
            jtfRecordBranch.setText("");
            jtfRecordNumber.setText("");
            moCurrentRecord = null;
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moDateDateEnd.setValue(moRegistry.getDateEnd());
        
        setFormEditable(true);

        if (moRegistry.isRegistryNew()) { }

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbStockValuation registry = (SDbStockValuation) moRegistry.clone();

        if (registry.isRegistryNew()) {}
        
        registry.setDateEnd(moDateDateEnd.getValue());
        registry.setAuxRecordPk((Object[]) moCurrentRecord.getPrimaryKey());

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        if (validation.isValid()) {
            try {
                String res = SStockValuationUtils.arePastValuationsValid(miClient.getSession(), moDateDateEnd.getValue());
                if (! res.isEmpty()) {
                    if (miClient.showMsgBoxConfirm(res + "\n ¿Desea reevaluar todo desde la última fecha?") == JOptionPane.YES_OPTION) {
                        String canDelete = SStockValuationUtils.canDeleteValuations((SClientInterface) miClient, moDateDateEnd.getValue());
                        if (! canDelete.isEmpty()) {
                            validation.setMessage(canDelete);
                        }
                        else {
                            ArrayList<SDbStockValuation> lValuations = SStockValuationUtils.deleteValuations(miClient, moDateDateEnd.getValue());
                            if (lValuations == null) {
                                validation.setMessage("Error al eliminar las valuaciones, contacte a soporte técnico.");
                            }
                            
                            String result = SStockValuationUtils.revaluateValuations(miClient, lValuations);
                            if (! result.isEmpty()) {
                                validation.setMessage(result);
                            }
                        }
                    }
                    else {
                        validation.setMessage(res);
                    }
                }
            }
            catch (SQLException ex) {
                Logger.getLogger(SFormStockValuation.class.getName()).log(Level.SEVERE, null, ex);
                validation.setMessage(ex.getMessage());
            }
            catch (Exception ex) {
                Logger.getLogger(SFormStockValuation.class.getName()).log(Level.SEVERE, null, ex);
                validation.setMessage(ex.getMessage());
            }
        }

        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbPickRecord) {
                actionPickRecord();
            }
        }
    }
}
