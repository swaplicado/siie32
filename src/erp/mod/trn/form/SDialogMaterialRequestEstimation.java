/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.form;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormUtilities;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchContact;
import erp.mod.SModConsts;
import erp.mod.bps.db.SDbBizPartner;
import erp.mod.trn.db.SDbMaterialRequest;
import erp.mod.trn.db.SDbMaterialRequestEntry;
import erp.mod.trn.db.SDbMaterialRequestEntryNote;
import erp.mod.trn.db.SMaterialRequestEntryRow;
import erp.mod.trn.db.SMaterialRequestEstimationUtils;
import erp.mod.trn.db.SProviderMailRow;
import erp.mtrn.form.SPanelMaterialRequest;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
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
public class SDialogMaterialRequestEstimation extends SBeanFormDialog implements ListSelectionListener, ItemListener, ActionListener {
    
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
    
    private erp.mtrn.form.SPanelMaterialRequest moPanelMatRequest;
    private erp.lib.form.SFormField moFieldBizPartner;

    /**
     * Creates new form SDialogMaterialRequestEstimation
     * @param client
     * @param title
     */
    public SDialogMaterialRequestEstimation(SGuiClient client, String title) {
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

        jTPGeneralPanel = new javax.swing.JTabbedPane();
        jpMaterialRequest = new javax.swing.JPanel();
        jlPanelMatRequest = new javax.swing.JLabel();
        jpOptions = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jbSetEverything = new javax.swing.JButton();
        jbSetNothing = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jpCommands2 = new javax.swing.JPanel();
        jbNext = new javax.swing.JButton();
        jpDelivery = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jpBenefit1 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jlProviderName = new javax.swing.JLabel();
        jcbBizPartner = new javax.swing.JComboBox<SFormComponentItem>();
        jbPickBizPartner = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jlProviderName1 = new javax.swing.JLabel();
        moTextProvider = new sa.lib.gui.bean.SBeanFieldText();
        jPanel16 = new javax.swing.JPanel();
        jlTo = new javax.swing.JLabel();
        moTextTo = new sa.lib.gui.bean.SBeanFieldText();
        jPanel17 = new javax.swing.JPanel();
        jlCC = new javax.swing.JLabel();
        moTextCC = new sa.lib.gui.bean.SBeanFieldText();
        jPanel21 = new javax.swing.JPanel();
        jlCCO = new javax.swing.JLabel();
        moTextCCO = new sa.lib.gui.bean.SBeanFieldText();
        jPanel24 = new javax.swing.JPanel();
        jlSubject = new javax.swing.JLabel();
        moTextSubject = new sa.lib.gui.bean.SBeanFieldText();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtAreaBody = new javax.swing.JTextArea();
        jPanel25 = new javax.swing.JPanel();
        jlAux2 = new javax.swing.JLabel();
        jbAddProviderRow = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jpProviderMailRows = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jpCommands4 = new javax.swing.JPanel();
        jbPrevious = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Solicitud de cotización a proveedores");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jTPGeneralPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTPGeneralPanelStateChanged(evt);
            }
        });

        jpMaterialRequest.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpMaterialRequest.setLayout(new java.awt.BorderLayout());

        jlPanelMatRequest.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlPanelMatRequest.setText("[Panel de documento de requisición]");
        jlPanelMatRequest.setPreferredSize(new java.awt.Dimension(100, 200));
        jpMaterialRequest.add(jlPanelMatRequest, java.awt.BorderLayout.NORTH);

        jpOptions.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpOptions.setPreferredSize(new java.awt.Dimension(169, 325));
        jpOptions.setLayout(new java.awt.BorderLayout(0, 2));

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbSetEverything.setText("Todo");
        jbSetEverything.setToolTipText("Surtir todo");
        jbSetEverything.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel23.add(jbSetEverything);

        jbSetNothing.setText("Limpiar");
        jbSetNothing.setToolTipText("Limpiar captura");
        jbSetNothing.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel23.add(jbSetNothing);

        jpOptions.add(jPanel23, java.awt.BorderLayout.NORTH);

        jpMaterialRequest.add(jpOptions, java.awt.BorderLayout.CENTER);
        jpOptions.getAccessibleContext().setAccessibleName("Partidas de la requisición:");

        jPanel2.setPreferredSize(new java.awt.Dimension(592, 33));
        jPanel2.setLayout(new java.awt.GridLayout(1, 2));

        jpCommands2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbNext.setText("Siguiente");
        jbNext.setToolTipText("[Ctrl + Enter]");
        jbNext.setPreferredSize(new java.awt.Dimension(100, 23));
        jpCommands2.add(jbNext);

        jPanel2.add(jpCommands2);

        jpMaterialRequest.add(jPanel2, java.awt.BorderLayout.SOUTH);

        jTPGeneralPanel.addTab("Requisición de materiales", jpMaterialRequest);

        jpDelivery.setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new java.awt.BorderLayout());

        jpBenefit1.setPreferredSize(new java.awt.Dimension(1005, 290));
        jpBenefit1.setLayout(new java.awt.BorderLayout());

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del correo:"));
        jPanel10.setPreferredSize(new java.awt.Dimension(400, 180));
        jPanel10.setLayout(new java.awt.GridLayout(10, 1, 0, 1));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProviderName.setText("Proveedor:");
        jlProviderName.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel11.add(jlProviderName);

        jcbBizPartner.setPreferredSize(new java.awt.Dimension(272, 23));
        jPanel11.add(jcbBizPartner);

        jbPickBizPartner.setText("...");
        jbPickBizPartner.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbPickBizPartner);

        jPanel10.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProviderName1.setText("Nombre:*");
        jlProviderName1.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel12.add(jlProviderName1);

        moTextProvider.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel12.add(moTextProvider);

        jPanel10.add(jPanel12);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTo.setText("Para:*");
        jlTo.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel16.add(jlTo);

        moTextTo.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel16.add(moTextTo);

        jPanel10.add(jPanel16);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCC.setText("CC:");
        jlCC.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel17.add(jlCC);

        moTextCC.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel17.add(moTextCC);

        jPanel10.add(jPanel17);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCCO.setText("CCO:");
        jlCCO.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel21.add(jlCCO);

        moTextCCO.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel21.add(moTextCCO);

        jPanel10.add(jPanel21);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSubject.setText("Asunto:*");
        jlSubject.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel24.add(jlSubject);

        moTextSubject.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel24.add(moTextSubject);

        jPanel10.add(jPanel24);

        jpBenefit1.add(jPanel10, java.awt.BorderLayout.WEST);

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Cuerpo del correo:*"));
        jPanel18.setLayout(new java.awt.BorderLayout());

        jtAreaBody.setColumns(20);
        jtAreaBody.setRows(5);
        jScrollPane1.setViewportView(jtAreaBody);

        jPanel18.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jpBenefit1.add(jPanel18, java.awt.BorderLayout.CENTER);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAux2.setPreferredSize(new java.awt.Dimension(865, 23));
        jPanel25.add(jlAux2);

        jbAddProviderRow.setText("Agregar");
        jbAddProviderRow.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbAddProviderRow.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel25.add(jbAddProviderRow);

        jpBenefit1.add(jPanel25, java.awt.BorderLayout.SOUTH);

        jPanel8.add(jpBenefit1, java.awt.BorderLayout.NORTH);

        jPanel19.setLayout(new java.awt.BorderLayout());

        jpProviderMailRows.setBorder(javax.swing.BorderFactory.createTitledBorder("Proveedores:"));
        jpProviderMailRows.setPreferredSize(new java.awt.Dimension(100, 220));
        jpProviderMailRows.setLayout(new java.awt.BorderLayout());
        jPanel19.add(jpProviderMailRows, java.awt.BorderLayout.NORTH);

        jPanel8.add(jPanel19, java.awt.BorderLayout.CENTER);

        jpDelivery.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel22.setPreferredSize(new java.awt.Dimension(592, 33));
        jPanel22.setLayout(new java.awt.GridLayout(1, 2));

        jpCommands4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbPrevious.setText("Anterior");
        jbPrevious.setToolTipText("[Ctrl + Enter]");
        jbPrevious.setPreferredSize(new java.awt.Dimension(100, 23));
        jpCommands4.add(jbPrevious);

        jPanel22.add(jpCommands4);

        jpDelivery.add(jPanel22, java.awt.BorderLayout.SOUTH);

        jTPGeneralPanel.addTab("Envío", jpDelivery);

        getContentPane().add(jTPGeneralPanel, java.awt.BorderLayout.PAGE_START);

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

    private void jTPGeneralPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTPGeneralPanelStateChanged
        ArrayList<SMaterialRequestEntryRow> lRows = new ArrayList<>();
        if (moGridMatReqEty != null) {
            SMaterialRequestEntryRow row;
            for (int i = 0; i < moGridMatReqEty.getTable().getRowCount(); i++) {
                row = (SMaterialRequestEntryRow) moGridMatReqEty.getGridRow(i);
                if (row.isToEstimate()) {
                    lRows.add(row);
                }
            }

            msBodyRows = SMaterialRequestEstimationUtils.getBodyEntries(lRows);
        }
        else {
            msBodyRows = "---";
        }
    }//GEN-LAST:event_jTPGeneralPanelStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTPGeneralPanel;
    private javax.swing.JButton jbAddProviderRow;
    private javax.swing.JButton jbNext;
    private javax.swing.JButton jbPickBizPartner;
    private javax.swing.JButton jbPrevious;
    private javax.swing.JButton jbSetEverything;
    private javax.swing.JButton jbSetNothing;
    private javax.swing.JComboBox<SFormComponentItem> jcbBizPartner;
    private javax.swing.JLabel jlAux2;
    private javax.swing.JLabel jlCC;
    private javax.swing.JLabel jlCCO;
    private javax.swing.JLabel jlPanelMatRequest;
    private javax.swing.JLabel jlProviderName;
    private javax.swing.JLabel jlProviderName1;
    private javax.swing.JLabel jlSubject;
    private javax.swing.JLabel jlTo;
    private javax.swing.JPanel jpBenefit1;
    private javax.swing.JPanel jpCommands2;
    private javax.swing.JPanel jpCommands4;
    private javax.swing.JPanel jpDelivery;
    private javax.swing.JPanel jpMaterialRequest;
    private javax.swing.JPanel jpOptions;
    private javax.swing.JPanel jpProviderMailRows;
    private javax.swing.JTextArea jtAreaBody;
    private sa.lib.gui.bean.SBeanFieldText moTextCC;
    private sa.lib.gui.bean.SBeanFieldText moTextCCO;
    private sa.lib.gui.bean.SBeanFieldText moTextProvider;
    private sa.lib.gui.bean.SBeanFieldText moTextSubject;
    private sa.lib.gui.bean.SBeanFieldText moTextTo;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 640);
        
        moFieldBizPartner = new erp.lib.form.SFormField((SClientInterface) miClient, SLibConstants.DATA_TYPE_KEY, true, jcbBizPartner, jlProviderName);
        moFieldBizPartner.setPickerButton(jbPickBizPartner);
        
        moTextProvider.setTextSettings(jlProviderName1.getText(), 250);
        moTextTo.setTextSettings(jlTo.getText(), 250);
        moTextTo.setTextCaseType(SGuiConsts.TEXT_CASE_LOWER);
        moTextCC.setTextSettings(jlCC.getText(), 250);
        moTextCC.setTextCaseType(SGuiConsts.TEXT_CASE_LOWER);
        moTextCCO.setTextSettings(jlCCO.getText(), 250);
        moTextCCO.setTextCaseType(SGuiConsts.TEXT_CASE_LOWER);
        moTextSubject.setTextSettings(jlSubject.getText(), 250);
        moTextSubject.setTextCaseType(0);
        
        mnBizPartnerPicker = SDataConstants.BPSX_BP_SUP;
        
        jbSave.setText("Cerrar");
        jbCancel.setEnabled(false);

        moGridMatReqEty = new SGridPaneForm(miClient, SModConsts.TRNX_MAT_REQ_ETY_ROW, SLibConsts.UNDEFINED, "Renglones de la requisición") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "Código"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Concepto"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "# Parte"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "Especificaciones"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_3D, "Requerido"));
                SGridColumnForm col = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_3D, "A Cotizar");
                col.setEditable(true);
                gridColumnsForm.add(col);
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Cotizado"));
                SGridColumnForm col1 = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Cotizar");
                col1.setEditable(true);
                gridColumnsForm.add(col1);

                return gridColumnsForm;
            }
        };
        jpOptions.add(moGridMatReqEty, BorderLayout.CENTER);
        
        moGridProviderRows = new SGridPaneForm(miClient, SModConsts.TRNX_MAT_REQ_EST_PROVID_ROW, SLibConsts.UNDEFINED, "Proveedores para cotización") {
            @Override
            public void initGrid() {
                jbRowNew.setEnabled(false);
                jbRowEdit.setEnabled(true);
                jbRowDelete.setEnabled(true);
            }
            
            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Proveedor"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "Para"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "CC"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "CCO"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Asunto"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Cuerpo del correo"));

                return gridColumnsForm;
            }
            
            @Override
            public void actionRowEdit() {
                if (jbRowEdit.isEnabled()) {
                    if (jtTable.getSelectedRowCount() != 1) {
                        miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
                    }
                    else {
                        SProviderMailRow registry = null;
                        SGridRow gridRow = getSelectedGridRow();
                        int row = jtTable.getSelectedRow();

                        if (gridRow.isRowSystem()) {
                            miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                        }
                        else {
                            registry = (SProviderMailRow) gridRow;
                            moProviderRow = registry;
                            moFieldBizPartner.setKey(new int[] { moProviderRow.getFkProviderId_n() });
                            moTextTo.setValue(moProviderRow.getTo());
                            moTextCC.setValue(moProviderRow.getCc());
                            moTextCCO.setValue(moProviderRow.getCco());
                            moTextSubject.setValue(moProviderRow.getSubject());
                            jtAreaBody.setText(moProviderRow.getBody());
                        }
                    }
                }
            }
            
            @Override
            public void actionRowDelete() {
                if (jbRowDelete.isEnabled()) {
                    if (jtTable.getSelectedRowCount() == 0) {
                        miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROWS);
                    }
                    else if (miClient.showMsgBoxConfirm(SGridConsts.MSG_CONFIRM_REG_DEL) == JOptionPane.YES_OPTION) {
                        SGridRow gridRow = null;
                        SGridRow[] gridRows = getSelectedGridRows();
                        int[] rows = jtTable.getSelectedRows();

                        for (int i = 0; i < gridRows.length; i++) {
                            gridRow = gridRows[i];

                            if (gridRow.isRowSystem()) {
                                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                            }
                            else if (!gridRow.isRowDeletable()) {
                                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_DELETABLE);
                            }
                            else {
                                moModel.getGridRows().remove(moModel.getGridRows().indexOf(gridRow));
                                moModel.renderGridRows();
                                mnMailNumber--;

                                setSelectedGridRow(rows[i] < moModel.getRowCount() ? rows[i] : moModel.getRowCount() - 1);

                                mvDeletedRows.add(gridRow);
                                if (miPaneFormOwner != null) {
                                    miPaneFormOwner.notifyRowDelete(mnGridType, mnGridSubtype, rows[i], gridRow);
                                }
                            }
                        }
                    }
                }
            }
        };
        jpProviderMailRows.add(moGridProviderRows, BorderLayout.CENTER);
        mnMailNumber = 1;
        
        moPanelMatRequest = new SPanelMaterialRequest((SClientInterface) miClient, "de origen");
        jpMaterialRequest.remove(jlPanelMatRequest);
        jpMaterialRequest.add(moPanelMatRequest, BorderLayout.NORTH);
        
        msSubjectDefault = SMaterialRequestEstimationUtils.getSubjectOfEstimate(miClient);
        msBodyDefault = SMaterialRequestEstimationUtils.getBodyOfEstimate(miClient);
        msBodyRows = "";
        
        moGridProviderRows.populateGrid(new Vector<>());
        moGridProviderRows.clearSortKeys();
        moGridProviderRows.clearGridRows();
        
        moProviderRow = null;
        
        jbSave.setText("Enviar");
        
        try {
            removeAllListeners();
            reloadCatalogues();
            addAllListeners();
        }
        catch (NullPointerException ex) {
            Logger.getLogger(SDialogMaterialRequestEstimation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Mostrar registros en la tabla superior
     */
    @SuppressWarnings("unchecked")
    private void showMaterialRequestEntries() {
        Vector<SGridRow> rows = new Vector<>();

        try {
            moGridMatReqEty.clearGridRows();
            
            mlMaterialRequestEntries = moMaterialRequest.getChildEntries();
            for (SDbMaterialRequestEntry oMaterialRequestEntry : mlMaterialRequestEntries) {
                if (! oMaterialRequestEntry.isDeleted()) {
                    SMaterialRequestEntryRow oRow = new SMaterialRequestEntryRow((SClientInterface) miClient, 
                                                                                SMaterialRequestEntryRow.FORM_ESTIMATE,
                                                                                oMaterialRequestEntry.getFkItemId(), 
                                                                                oMaterialRequestEntry.getFkUnitId(),
                                                                                oMaterialRequestEntry.getConsumptionInfo().isEmpty() ? 
                                                                                        moMaterialRequest.getConsumptionInfo() : 
                                                                                        oMaterialRequestEntry.getConsumptionInfo()
                                                                                );

                    oRow.setPkMatRequestId(oMaterialRequestEntry.getPkMatRequestId());
                    oRow.setPkEntryId(oMaterialRequestEntry.getPkEntryId());
                    oRow.setQuantity(oMaterialRequestEntry.getQuantity());
                    oRow.setDateRequest(oMaterialRequestEntry.getDateRequest_n());
                    String notes = "";
                    for (SDbMaterialRequestEntryNote oNote : oMaterialRequestEntry.getChildNotes()) {
                        notes += oNote.getNotes();
                    }

                    oRow.setNotes(notes);

                    rows.add(oRow);
                }
            }

            moGridMatReqEty.populateGrid(rows, this);
            moGridMatReqEty.clearSortKeys();
            moGridMatReqEty.setSelectedGridRow(0);
            
            if (rows.isEmpty()) {
                moGridMatReqEty.clearGridRows();
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    /**
     * Cotizar todo
     */
    private void actionEstimateAll() {
        SMaterialRequestEntryRow oMatReqRow;
        for (int i = 0; i < moGridMatReqEty.getTable().getRowCount(); i++) {
            oMatReqRow = (SMaterialRequestEntryRow) moGridMatReqEty.getGridRow(i);
            oMatReqRow.setAuxToEstimate(oMatReqRow.getQuantity());
            oMatReqRow.setAuxIsToEstimate(true);
        }
        
        moGridMatReqEty.renderGridRows();
        moGridMatReqEty.setSelectedGridRow(0);
    }
    
    /**
     * Cotizar
     */
    private void actionClean() {
        SMaterialRequestEntryRow oMatReqRow;
        for (int i = 0; i < moGridMatReqEty.getTable().getRowCount(); i++) {
            oMatReqRow = (SMaterialRequestEntryRow) moGridMatReqEty.getGridRow(i);
            oMatReqRow.setAuxToEstimate(0d);
            oMatReqRow.setAuxIsToEstimate(false);
        }
        
        moGridMatReqEty.renderGridRows();
        moGridMatReqEty.setSelectedGridRow(0);
    }
    
    private void actionPickBizPartner() {
        ((SClientInterface) miClient).pickOption(mnBizPartnerPicker, moFieldBizPartner, null);
        this.setProvider();
    }
    
    private void setProvider() {
        int providerId = moFieldBizPartner.getKeyAsIntArray() != null ? moFieldBizPartner.getKeyAsIntArray()[0] : 0;
        String bpName = "";
        String to = "";
        String cc = "";
        String cco = "";
        String subject = msSubjectDefault + " REQ.-" + String.format("%05d", moMaterialRequest.getNumber()) + "-" + mnMailNumber;
        String body = msBodyDefault.replace("---", msBodyRows);
        
        if (providerId > 0) {
            try {
                SDbBizPartner oBp = new SDbBizPartner();
                oBp.read(miClient.getSession(), moFieldBizPartner.getKeyAsIntArray());
                SDataBizPartnerBranch oBpB = new SDataBizPartnerBranch();
                oBpB.read(oBp.getRegHeadquarters().getPrimaryKey(), miClient.getSession().getStatement());
                SDataBizPartnerBranchContact contact = oBpB.getDbmsBizPartnerBranchContactOfficial();
        
                if (contact != null) {
                    to = contact.getEmail01() == null || contact.getEmail01().isEmpty() ? "" : contact.getEmail01();
                }
                else {
                    to = "";
                }
                
                bpName = oBp.getBizPartner();
                
            }
            catch (Exception ex) {
                Logger.getLogger(SDialogMaterialRequestEstimation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        moTextProvider.setValue(bpName);
        moTextTo.setValue(to);
        moTextSubject.setValue(subject);
        jtAreaBody.setText(body);
    }
    
    private void actionAddProviderRow() {
        boolean bEstimate = false;
        SMaterialRequestEntryRow oMatReqRow;
        for (int i = 0; i < moGridMatReqEty.getTable().getRowCount(); i++) {
            oMatReqRow = (SMaterialRequestEntryRow) moGridMatReqEty.getGridRow(i);
            if (oMatReqRow.getAuxQuantityToEstimate() > 0 && oMatReqRow.isToEstimate()) {
                bEstimate = true;
                break;
            }
        }
        if (! bEstimate) {
            miClient.showMsgBoxError("No hay partidas marcadas para la cotización");
            return;
        }
        
        if (moTextTo.getValue().isEmpty()) {
            miClient.showMsgBoxError("Debe indicar al menos un correo para destinatario");
            return;
        }
        if (moTextTo.getValue().isEmpty()) {
            miClient.showMsgBoxError("Debe indicar al menos un correo para destinatario");
            return;
        }
        if (moTextSubject.getValue().isEmpty()) {
            miClient.showMsgBoxError("Debe indicar un asunto para el correo");
            return;
        }

        if (! SLibUtilities.validateEmails(moTextTo.getValue())) {
            miClient.showMsgBoxError("El campo destinatario no es válido");
            return;
        }
        if (moTextCC.getValue().isEmpty()) {
            if (miClient.showMsgBoxConfirm("¿Desea dejar el campo CC vacío?") != JOptionPane.YES_OPTION) {
                return;
            }
        }
        else {
            if (! SLibUtilities.validateEmails(moTextCC.getValue())) {
                miClient.showMsgBoxError("El campo CC destinatario no es válido");
                return;
            } 
        }
        if (! moTextCCO.getValue().isEmpty()) {
            if (! SLibUtilities.validateEmails(moTextCCO.getValue())) {
                miClient.showMsgBoxError("El campo CCO destinatario no es válido");
                return;
            } 
        }
        
        SProviderMailRow oRow;
        if (moProviderRow == null) {
            oRow = new SProviderMailRow();
        }
        else {
            oRow = moProviderRow;
        }
        oRow.setProvider(moTextProvider.getValue());
        oRow.setTo(moTextTo.getValue());
        oRow.setCc(moTextCC.getValue());
        oRow.setCco(moTextCCO.getValue());
        oRow.setSubject(moTextSubject.getValue());
        oRow.setBody(jtAreaBody.getText());
        oRow.setFkProviderId_n(moFieldBizPartner.getKeyAsIntArray() != null ? moFieldBizPartner.getKeyAsIntArray()[0] : 0);
        
        if (moProviderRow == null) {
            moGridProviderRows.addGridRow(oRow);
            mnMailNumber++;
        }
        moGridProviderRows.renderGridRows();
        
        jcbBizPartner.setSelectedIndex(0);
        moProviderRow = null;
        setProvider();
    }
    
    @Override
    public void addAllListeners() {
        jbPickBizPartner.addActionListener(this);
        jbAddProviderRow.addActionListener(this);
        jbSetEverything.addActionListener(this);
        jbSetNothing.addActionListener(this);
        jbPrevious.addActionListener(this);
        jbNext.addActionListener(this);
        
        jcbBizPartner.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbPickBizPartner.removeActionListener(this);
        jbAddProviderRow.removeActionListener(this);
        jbSetEverything.removeActionListener(this);
        jbSetNothing.removeActionListener(this);
        jbPrevious.removeActionListener(this);
        jbNext.removeActionListener(this);
        
        jcbBizPartner.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        SFormUtilities.populateComboBox((SClientInterface) miClient, jcbBizPartner, mnBizPartnerPicker);
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
            case SModConsts.TRN_MAT_REQ:
                int[]pk = (int[]) value;
                moMaterialRequest = new SDbMaterialRequest();
                try {
                    moMaterialRequest.read(miClient.getSession(), pk);
                    if (moMaterialRequest.getQueryResultId() == SDbConsts.READ_OK) {
                        moPanelMatRequest.setMaterialRequest(moMaterialRequest);
                        mnMailNumber = SMaterialRequestEstimationUtils.getNextMailNumberOfMatRequest(miClient.getSession().getStatement(), moMaterialRequest.getPkMatRequestId());
                        showMaterialRequestEntries();
                    }
                }
                catch (Exception ex) {
                    Logger.getLogger(SDialogMaterialRequestEstimation.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }
    }

    @Override
    public Object getValue(final int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof javax.swing.JComboBox && e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox comboBox = (JComboBox) e.getSource();
            
            if (comboBox == jcbBizPartner) {
                this.setProvider();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPickBizPartner) {
                actionPickBizPartner();
            }
            else if (button == jbSetEverything) {
                actionEstimateAll();
            }
            else if (button == jbSetNothing) {
                actionClean();
            }
            else if (button == jbAddProviderRow) {
                actionAddProviderRow();
            }
            else if (button == jbNext) {
                jTPGeneralPanel.setSelectedIndex(1);
            }
            else if (button == jbPrevious) {
                jTPGeneralPanel.setSelectedIndex(0);
            }
        }
    }
    
    @Override
    public void actionSave() {
        if (moGridProviderRows.getTable().getRowCount() == 0) {
            miClient.showMsgBoxError("No hay destinatarios para la cotización");
            return;
        }
        
        try {
            SGuiUtils.setCursorWait(miClient);
            jbSave.setEnabled(false);

            ArrayList<SMaterialRequestEntryRow> lEtyRows = new ArrayList<>();
            SMaterialRequestEntryRow etyRow;
            for (int i = 0; i < moGridMatReqEty.getTable().getRowCount(); i++) {
                etyRow = (SMaterialRequestEntryRow) moGridMatReqEty.getGridRow(i);
                if (etyRow.isToEstimate()) {
                    lEtyRows.add(etyRow);
                }
            }

            ArrayList<SProviderMailRow> lRows = new ArrayList<>();
            SProviderMailRow oProviderRow;
            for (int i = 0; i < moGridProviderRows.getTable().getRowCount(); i++) {
                oProviderRow = (SProviderMailRow) moGridProviderRows.getGridRow(i);
                lRows.add(oProviderRow);
            }

            SMaterialRequestEstimationUtils.sendMails(miClient, lRows);
            String res = SMaterialRequestEstimationUtils.saveEstimationRequest(miClient, moMaterialRequest.getPkMatRequestId(), lEtyRows, lRows);
            mnMailNumber = SMaterialRequestEstimationUtils.getNextMailNumberOfMatRequest(miClient.getSession().getStatement(), moMaterialRequest.getPkMatRequestId());
            moGridProviderRows.clearGridRows();
        }
        catch (Exception e) {
            
        }
        finally {
            SGuiUtils.setCursorDefault(miClient);
            jbSave.setEnabled(true);
        }
    }
}
