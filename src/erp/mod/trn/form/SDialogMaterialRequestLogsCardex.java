/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.form;

import erp.mod.SModConsts;
import erp.mod.trn.db.SDbMaterialRequestStatusLog;
import erp.mod.trn.db.SMaterialRequestUtils;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Vector;
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
public class SDialogMaterialRequestLogsCardex extends SBeanFormDialog implements ListSelectionListener{
    
    protected SGridPaneForm moGridLogs;
    protected ArrayList<SDbMaterialRequestStatusLog> mlLogs;

    /**
     * Creates new form SDialogMaterialRequestLogsCardex
     * @param client
     * @param title
     */
    public SDialogMaterialRequestLogsCardex(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.TRN_MAT_REQ_ST_LOG, SLibConsts.UNDEFINED, title);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jpAuthorizationRoute = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Bitácora de cambios");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        jpAuthorizationRoute.setBorder(javax.swing.BorderFactory.createTitledBorder("Bitácora:"));
        jpAuthorizationRoute.setPreferredSize(new java.awt.Dimension(100, 200));
        jpAuthorizationRoute.setLayout(new java.awt.BorderLayout());
        jPanel3.add(jpAuthorizationRoute, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleName("Bitácora de cambios");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
       actionSave();
    }//GEN-LAST:event_closeDialog

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jpAuthorizationRoute;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 640, 400);
        
        jbSave.setText("Cerrar");
        jbCancel.setEnabled(false);

        moGridLogs = new SGridPaneForm(miClient, SModConsts.CFGU_AUTHORN_STEP, SLibConsts.UNDEFINED, "Bitácora") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_2B, "#", 50));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Status", 150));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE_DATETIME, "Fecha", 125));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Usuario", 75));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Comentario/Observación", 250));

                return gridColumnsForm;
            }
        };
        
        jpAuthorizationRoute.add(moGridLogs, BorderLayout.CENTER);
    }
    
    private void showLog() {
        Vector<SGridRow> rows = new Vector<>();

        for (SDbMaterialRequestStatusLog oStep : mlLogs) {
            rows.add(oStep);
        }
        
        moGridLogs.populateGrid(rows, this);
        moGridLogs.clearSortKeys();
        moGridLogs.setSelectedGridRow(0);
    }
    
    public void setFormParams(final int matReqId) {
        ArrayList<SDbMaterialRequestStatusLog> lLogs = SMaterialRequestUtils.getMaterialRequestLogs(miClient.getSession(), matReqId);
        this.mlLogs = lLogs;
        showLog();
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
    }

    @Override
    public Object getValue(final int type) {
        return new Object();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        
    }
}
