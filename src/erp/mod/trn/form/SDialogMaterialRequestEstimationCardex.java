/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.form;

import erp.data.SDataConstants;
import erp.mod.SModConsts;
import erp.mod.trn.db.SDbEstimationRequest;
import erp.mod.trn.db.SDbEstimationRequestRecipient;
import erp.mod.trn.db.SDbMaterialRequest;
import erp.mod.trn.db.SDbMaterialRequestEntry;
import erp.mod.trn.db.SProviderMailRow;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sa.lib.SLibConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFormDialog;

/**
 *
 * @author Edwin Carmona
 */
public class SDialogMaterialRequestEstimationCardex extends SBeanFormDialog implements ListSelectionListener, ItemListener, ActionListener {
    
    protected SDbMaterialRequest moMaterialRequest;
    protected ArrayList<SDbMaterialRequestEntry> mlMaterialRequestEntries;
    protected SGridPaneForm moGridMatReqEty;
    protected SGridPaneForm moGridProviderRows;
    protected int mnBizPartnerPicker;
    protected int mnMailNumber;
    protected boolean mbAreSigned;
    protected String msSubjectDefault;
    protected String msBodyDefault;
    protected String msBodyRows;
    
    protected SProviderMailRow moProviderRow;

    /**
     * Creates new form SDialogMaterialRequestEstimation
     * @param client
     * @param title
     */
    public SDialogMaterialRequestEstimationCardex(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.TRNX_MAT_REQ_ESTIMATE, SLibConsts.UNDEFINED, title);
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpDelivery = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jpRequisitionMaterialRows1 = new javax.swing.JPanel();
        jpProviderMailRows = new javax.swing.JPanel();
        jpBenefit1 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jlProviderName = new javax.swing.JLabel();
        moTextProvider = new sa.lib.gui.bean.SBeanFieldText();
        jPanel16 = new javax.swing.JPanel();
        jlTo = new javax.swing.JLabel();
        moTextTo = new sa.lib.gui.bean.SBeanFieldText();
        jPanel24 = new javax.swing.JPanel();
        jlSubject = new javax.swing.JLabel();
        moTextSubject = new sa.lib.gui.bean.SBeanFieldText();
        jPanel17 = new javax.swing.JPanel();
        jlCC = new javax.swing.JLabel();
        moTextCC = new sa.lib.gui.bean.SBeanFieldText();
        jPanel22 = new javax.swing.JPanel();
        jlDateEstimation = new javax.swing.JLabel();
        moFieldDateEstimation = new sa.lib.gui.bean.SBeanFieldDatetime();
        jLabel1 = new javax.swing.JLabel();
        moUserMailRequest = new sa.lib.gui.bean.SBeanFieldText();
        jPanel21 = new javax.swing.JPanel();
        jlCCO = new javax.swing.JLabel();
        moTextCCO = new sa.lib.gui.bean.SBeanFieldText();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtAreaBody = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Solicitud de cotización a proveedores");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jpDelivery.setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel19.setLayout(new java.awt.BorderLayout());

        jpRequisitionMaterialRows1.setBorder(javax.swing.BorderFactory.createTitledBorder("Solicitudes:"));
        jpRequisitionMaterialRows1.setPreferredSize(new java.awt.Dimension(100, 250));
        jpRequisitionMaterialRows1.setLayout(new java.awt.BorderLayout());

        jpProviderMailRows.setLayout(new java.awt.BorderLayout());
        jpRequisitionMaterialRows1.add(jpProviderMailRows, java.awt.BorderLayout.CENTER);

        jPanel19.add(jpRequisitionMaterialRows1, java.awt.BorderLayout.NORTH);

        jPanel8.add(jPanel19, java.awt.BorderLayout.NORTH);

        jpBenefit1.setLayout(new java.awt.BorderLayout());

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del correo:"));
        jPanel10.setPreferredSize(new java.awt.Dimension(1000, 100));
        jPanel10.setLayout(new java.awt.GridLayout(3, 2, 1, 1));

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProviderName.setText("Nombre:");
        jlProviderName.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel12.add(jlProviderName);

        moTextProvider.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel12.add(moTextProvider);

        jPanel10.add(jPanel12);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTo.setText("Para:");
        jlTo.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel16.add(jlTo);

        moTextTo.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel16.add(moTextTo);

        jPanel10.add(jPanel16);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSubject.setText("Asunto:");
        jlSubject.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel24.add(jlSubject);

        moTextSubject.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel24.add(moTextSubject);

        jPanel10.add(jPanel24);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCC.setText("CC:");
        jlCC.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel17.add(jlCC);

        moTextCC.setEditable(false);
        moTextCC.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel17.add(moTextCC);

        jPanel10.add(jPanel17);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEstimation.setText("Fecha cotiz.:");
        jlDateEstimation.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel22.add(jlDateEstimation);

        moFieldDateEstimation.setEnabled(false);
        jPanel22.add(moFieldDateEstimation);

        jLabel1.setText("Usr. sol.:");
        jLabel1.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel22.add(jLabel1);

        moUserMailRequest.setEditable(false);
        moUserMailRequest.setPreferredSize(new java.awt.Dimension(182, 23));
        jPanel22.add(moUserMailRequest);

        jPanel10.add(jPanel22);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCCO.setText("CCO:");
        jlCCO.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel21.add(jlCCO);

        moTextCCO.setEditable(false);
        moTextCCO.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel21.add(moTextCCO);

        jPanel10.add(jPanel21);

        jpBenefit1.add(jPanel10, java.awt.BorderLayout.WEST);

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Cuerpo del correo:*"));
        jPanel18.setPreferredSize(new java.awt.Dimension(178, 250));
        jPanel18.setLayout(new java.awt.BorderLayout());

        jtAreaBody.setEditable(false);
        jtAreaBody.setColumns(20);
        jtAreaBody.setRows(5);
        jScrollPane1.setViewportView(jtAreaBody);

        jPanel18.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jpBenefit1.add(jPanel18, java.awt.BorderLayout.SOUTH);

        jPanel8.add(jpBenefit1, java.awt.BorderLayout.PAGE_END);

        jpDelivery.add(jPanel8, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpDelivery, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        windowClosed();
        mnFormResult = SGuiConsts.FORM_RESULT_CANCEL;
        dispose();
    }//GEN-LAST:event_closeDialog

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlCC;
    private javax.swing.JLabel jlCCO;
    private javax.swing.JLabel jlDateEstimation;
    private javax.swing.JLabel jlProviderName;
    private javax.swing.JLabel jlSubject;
    private javax.swing.JLabel jlTo;
    private javax.swing.JPanel jpBenefit1;
    private javax.swing.JPanel jpDelivery;
    private javax.swing.JPanel jpProviderMailRows;
    private javax.swing.JPanel jpRequisitionMaterialRows1;
    private javax.swing.JTextArea jtAreaBody;
    private sa.lib.gui.bean.SBeanFieldDatetime moFieldDateEstimation;
    private sa.lib.gui.bean.SBeanFieldText moTextCC;
    private sa.lib.gui.bean.SBeanFieldText moTextCCO;
    private sa.lib.gui.bean.SBeanFieldText moTextProvider;
    private sa.lib.gui.bean.SBeanFieldText moTextSubject;
    private sa.lib.gui.bean.SBeanFieldText moTextTo;
    private sa.lib.gui.bean.SBeanFieldText moUserMailRequest;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 640);
        
        moTextProvider.setTextSettings(jlProviderName.getText(), 250);
        moTextTo.setTextSettings(jlTo.getText(), 250);
        moTextTo.setTextCaseType(SGuiConsts.TEXT_CASE_LOWER);
        moTextCC.setTextSettings(jlCC.getText(), 250);
        moTextCC.setTextCaseType(SGuiConsts.TEXT_CASE_LOWER);
        moTextCCO.setTextSettings(jlCCO.getText(), 250);
        moTextCCO.setTextCaseType(SGuiConsts.TEXT_CASE_LOWER);
        
        mnBizPartnerPicker = SDataConstants.BPSX_BP_SUP;
        
        jbSave.setText("Cerrar");
        jbCancel.setEnabled(false);
        
        moGridProviderRows = new SGridPaneForm(miClient, SModConsts.TRNX_MAT_REQ_EST_PROVID_ROW, SProviderMailRow.GRID_KARDEX, "Proveedores para cotización") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, false, false);
            }
            
            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE_DATETIME, "Fecha Cotiz."));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Proveedor"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "Para"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "CC"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "CCO"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Asunto"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Cuerpo del correo"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "Usuario solicitud"));

                return gridColumnsForm;
            }
        };
        jpProviderMailRows.add(moGridProviderRows, BorderLayout.CENTER);
        
        moGridProviderRows.populateGrid(new Vector<>());
        moGridProviderRows.clearSortKeys();
        moGridProviderRows.clearGridRows();
        
        moProviderRow = null;
        
        jbSave.setText("Aceptar");
        
        try {
            removeAllListeners();
            reloadCatalogues();
            addAllListeners();
        }
        catch (NullPointerException ex) {
            Logger.getLogger(SDialogMaterialRequestEstimationCardex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void showRows(int[] pk) {
        String sql = "SELECT id_est_req "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_EST_REQ_ETY) + " "
                + "WHERE NOT b_del AND fk_mat_req_n = " + pk[0] + " AND fk_mat_req_ety_n = " + pk[1] + " ;";
        
        try {
            Vector<SGridRow> lRows = new Vector<>();
            ResultSet res = miClient.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            SProviderMailRow oRow = null;
            while (res.next()) {
                SDbEstimationRequest oRequest = new SDbEstimationRequest();
                oRequest.read(miClient.getSession(), new int[] { res.getInt("id_est_req") });
                
                if (oRequest.getChildRecipients() != null && oRequest.getChildRecipients().size() > 0) {
                    for (SDbEstimationRequestRecipient oMailRow : oRequest.getChildRecipients()) {
                        oRow = new SProviderMailRow(SProviderMailRow.GRID_KARDEX);
                        
                        oRow.setDateEstimation(oRequest.getTsUser());
                        oRow.setProvider(oMailRow.getProviderName());
                        oRow.setTo(oMailRow.getMailsTo());
                        oRow.setCc(oMailRow.getMailsCc());
                        oRow.setCco(oMailRow.getMailsCco());
                        oRow.setSubject(oMailRow.getSubject());
                        oRow.setBody(oMailRow.getBody());
                        oRow.setUserMail(oRequest.getAuxUserName());

                        lRows.add(oRow);
                    }
                }
            }
            
            moGridProviderRows.populateGrid(lRows, this);
        }
        catch (SQLException ex) {
            Logger.getLogger(SDialogMaterialRequestEstimationCardex.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SDialogMaterialRequestEstimationCardex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void valueChangedProviderMail() {
        SProviderMailRow row = (SProviderMailRow) moGridProviderRows.getSelectedGridRow();
        
        this.setProvider(row);
    }
    
    private void setProvider(SProviderMailRow oRow) {
        moFieldDateEstimation.setValue(oRow.getDateEstimation());
        moUserMailRequest.setValue(oRow.getUserMail());
        moTextProvider.setValue(oRow.getProvider());
        moTextTo.setValue(oRow.getTo());
        moTextCC.setValue(oRow.getCc());
        moTextCCO.setValue(oRow.getCco());
        moTextSubject.setValue(oRow.getSubject());
        jtAreaBody.setText(oRow.getBody());
        jtAreaBody.setCaretPosition(0);
        
        moTextProvider.setToolTipText(oRow.getProvider());
        moTextTo.setToolTipText(oRow.getTo());
        moTextCC.setToolTipText(oRow.getCc());
        moTextCCO.setToolTipText(oRow.getCco());
        moTextSubject.setToolTipText(oRow.getSubject());
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
        
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        return validation;
    }

    @Override
    public void setValue(final int type, final Object value) {
        switch (type) {
            case SModConsts.TRN_MAT_REQ_ETY:
                int[]pk = (int[]) value;
                showRows(pk);
                break;
        }
    }

    @Override
    public Object getValue(final int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        valueChangedProviderMail();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof javax.swing.JComboBox && e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox comboBox = (JComboBox) e.getSource();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
    
    @Override
    public void actionSave() {
        windowClosed();
        mnFormResult = SGuiConsts.FORM_RESULT_CANCEL;
        dispose();
    }
}
