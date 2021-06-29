/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormDpsEdit.java
 *
 * Created on 08/09/2020, 04:48:00 PM
 */

package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.form.SFormOptionPicker;
import erp.form.SFormOptionPickerItems;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.mbps.data.SDataBizPartner;
import erp.mfin.data.SDataCostCenter;
import erp.mitm.data.SDataItem;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SDataDpsEntryEdit;
import erp.mtrn.data.SRowDpsEdit;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import sa.lib.SLibUtils;
import sa.lib.srv.SSrvConsts;
import sa.lib.srv.SSrvLock;
import sa.lib.srv.SSrvUtils;

/**
 * Modificar el ítem y el centro de costo de un documento y de todos los documentos asociados a este, sin necesidad de editar cada documento de forma manual.
 * @author Isabel Servín
 */
public class SFormDpsEdit extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {
    
    private final erp.client.SClientInterface miClient;
    private int mnFormResult;
    private boolean mbFirstTime;
    private boolean mbDocuentsLockedError;
    private erp.lib.table.STablePane moConceptTablePane; 
    
    private SDataDps moDps;
    private SFormOptionPickerItems moPickerItems;
    private SFormOptionPicker moPickerCostCenter;
    private SDataDpsEntryEdit moDpsEntryEdit;
    
    private int mnFormStatus;
    private final int mnFormType;
    
    private ArrayList<SDataDps> moDocuments;
    private ArrayList<int[]> moUpdateDocumentEntryKeys;

    /** Creates new form SFormCfdiChangeItem
     * @param client
     */
    public SFormDpsEdit(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        mnFormType = SDataConstants.TRNX_DPS_EDIT;
        miClient =  client;
        initComponents();
     
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpCfdiChangeItem = new javax.swing.JPanel();
        jpCfdiData = new javax.swing.JPanel();
        jpCfdiHeader = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlNameEmisor = new javax.swing.JLabel();
        jtfNameEmisor = new javax.swing.JTextField();
        jlRfcEmisor = new javax.swing.JLabel();
        jtfRfcEmisor = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jlInvoiceCfdi = new javax.swing.JLabel();
        jtfInvoiceCfdi = new javax.swing.JTextField();
        jtfPaymentType = new javax.swing.JTextField();
        jlDateCfdi = new javax.swing.JLabel();
        jtfDateCfdi = new javax.swing.JTextField();
        jpCfdiConcepts = new javax.swing.JPanel();
        jpCfdiConceptsGrid = new javax.swing.JPanel();
        jpCfdiConceptsDataNorth = new javax.swing.JPanel();
        jpCfdiConceptSetup = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jbSelectItem = new javax.swing.JButton();
        jbCostCenter = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jpControls = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cambiar ítem de documento de compras-ventas.");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpCfdiChangeItem.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpCfdiChangeItem.setLayout(new java.awt.BorderLayout());

        jpCfdiData.setLayout(new java.awt.BorderLayout());

        jpCfdiHeader.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos generales del CFDI:"));
        jpCfdiHeader.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNameEmisor.setText("Emisor:");
        jlNameEmisor.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel6.add(jlNameEmisor);

        jtfNameEmisor.setEditable(false);
        jtfNameEmisor.setFocusable(false);
        jtfNameEmisor.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel6.add(jtfNameEmisor);

        jlRfcEmisor.setText("  RFC:");
        jlRfcEmisor.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel6.add(jlRfcEmisor);

        jtfRfcEmisor.setEditable(false);
        jtfRfcEmisor.setFocusable(false);
        jtfRfcEmisor.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jtfRfcEmisor);

        jpCfdiHeader.add(jPanel6);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInvoiceCfdi.setText("Folio CFDI:");
        jlInvoiceCfdi.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel12.add(jlInvoiceCfdi);

        jtfInvoiceCfdi.setEditable(false);
        jtfInvoiceCfdi.setFocusable(false);
        jtfInvoiceCfdi.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel12.add(jtfInvoiceCfdi);

        jtfPaymentType.setEditable(false);
        jtfPaymentType.setFocusable(false);
        jtfPaymentType.setPreferredSize(new java.awt.Dimension(95, 23));
        jPanel12.add(jtfPaymentType);

        jlDateCfdi.setText("  Fecha CFDI:");
        jlDateCfdi.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel12.add(jlDateCfdi);

        jtfDateCfdi.setEditable(false);
        jtfDateCfdi.setFocusable(false);
        jtfDateCfdi.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel12.add(jtfDateCfdi);

        jpCfdiHeader.add(jPanel12);

        jpCfdiData.add(jpCfdiHeader, java.awt.BorderLayout.CENTER);

        jpCfdiChangeItem.add(jpCfdiData, java.awt.BorderLayout.NORTH);

        jpCfdiConcepts.setBorder(javax.swing.BorderFactory.createTitledBorder("Conceptos del CFDI:"));
        jpCfdiConcepts.setLayout(new java.awt.BorderLayout(0, 5));

        jpCfdiConceptsGrid.setName(""); // NOI18N
        jpCfdiConceptsGrid.setLayout(new java.awt.BorderLayout());
        jpCfdiConcepts.add(jpCfdiConceptsGrid, java.awt.BorderLayout.CENTER);

        jpCfdiConceptsDataNorth.setLayout(new java.awt.BorderLayout());

        jpCfdiConceptSetup.setBorder(javax.swing.BorderFactory.createTitledBorder("Opciones del concepto del CFDI:"));
        jpCfdiConceptSetup.setLayout(new java.awt.GridLayout(1, 1, 0, 5));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbSelectItem.setText("Elegir ítem");
        jbSelectItem.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel11.add(jbSelectItem);

        jbCostCenter.setText("Elegir centro costo");
        jbCostCenter.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel11.add(jbCostCenter);

        jpCfdiConceptSetup.add(jPanel11);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpCfdiConceptSetup.add(jPanel15);

        jpCfdiConceptsDataNorth.add(jpCfdiConceptSetup, java.awt.BorderLayout.CENTER);

        jpCfdiConcepts.add(jpCfdiConceptsDataNorth, java.awt.BorderLayout.NORTH);

        jpCfdiChangeItem.add(jpCfdiConcepts, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpCfdiChangeItem, java.awt.BorderLayout.CENTER);

        jpControls.setPreferredSize(new java.awt.Dimension(392, 33));
        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jbCancel.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbCancel);

        getContentPane().add(jpControls, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(1040, 708));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            moConceptTablePane.getTable().requestFocus();
        }
        if(mbDocuentsLockedError) {
            releaseDpsUserLock();
            setVisible(false);
        }
    }
    
    private void initComponentsExtra() {
        // Tabla general (conceptos):
        int i = 0;
        STableColumnForm[] columns;
        
        moConceptTablePane = new STablePane(miClient);
        jpCfdiConceptsGrid.add(moConceptTablePane, BorderLayout.CENTER);

        columns = new STableColumnForm[10];
      
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "#", STableConstants.WIDTH_NUM_TINYINT);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "No. identificación", STableConstants.WIDTH_ITEM_KEY);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Código ítem anterior", STableConstants.WIDTH_ITEM_KEY);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ítem anterior", 250);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Clave centro costo anterior", STableConstants.WIDTH_ITEM_KEY);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Centro costo anterior", STableConstants.WIDTH_ACCOUNT);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Código ítem nuevo", STableConstants.WIDTH_ITEM_KEY);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ítem nuevo", 250);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Clave centro costo nuevo", STableConstants.WIDTH_ITEM_KEY);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Centro costo nuevo", STableConstants.WIDTH_ACCOUNT);
        
        
        for (i = 0; i < columns.length; i++) {
            moConceptTablePane.addTableColumn(columns[i]);
        }
        
        // Listeners:
        
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbSelectItem.addActionListener(this);
        jbCostCenter.addActionListener(this);
        
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
        
        // Activar o desactivar componentes:
        
        jbSelectItem.setEnabled(true);
        jbCostCenter.setEnabled(true); 
    }
    
    private void getAssociatedDocuments(SDataDps dps) {
        boolean contains = false;
        for (SDataDps document : moDocuments) {
            if (SLibUtilities.compareKeys((int[]) document.getPrimaryKey(), (int[]) dps.getPrimaryKey())){
                contains = true;
                break;
            }
        }
        if (!contains) {
            moDocuments.add(dps);
            ArrayList<int[]> assocDocs = new ArrayList<>();
            try {
                for (int i = 0; i < dps.getDbmsDpsEntries().size(); i++) {
                    int dpsClass = dps.getDpsClassKey()[1];
                    int[] entryKey = (int[]) dps.getDbmsDpsEntries().get(i).getPrimaryKey(); 
                    
                    String sql;
                    
                    // Busqueda hacia arriba (trn_dps_dps_supply):
                    if (dpsClass != SDataConstantsSys.TRNS_CL_DPS_EST && dpsClass != SDataConstantsSys.TRNS_CL_DPS_ADJ) {
                        sql = "SELECT id_src_year AS year, id_src_doc AS doc FROM trn_dps_dps_supply AS tdds "
                                + "WHERE id_des_year = " + entryKey[0] + " AND id_des_doc = " + entryKey[1] + " AND id_des_ety = " + entryKey[2] + "";    
                        assocDocs = getDocuments(sql, assocDocs, false);
                    }
                    
                    // Busqueda hacia abajo (trn_dps_dps_supply): 
                    if (dpsClass != SDataConstantsSys.TRNS_CL_DPS_DOC && dpsClass != SDataConstantsSys.TRNS_CL_DPS_ADJ) {
                        sql = "SELECT id_des_year AS year, id_des_doc AS doc FROM trn_dps_dps_supply AS tdds "
                                + "WHERE id_src_year = " + entryKey[0] + " AND id_src_doc = " + entryKey[1] + " AND id_src_ety = " + entryKey[2] + "";  
                        assocDocs = getDocuments(sql, assocDocs, false);
                    }
                    
                    // Busqueda hacia arriba (trn_dps_dps_adj):
                    if (dpsClass == SDataConstantsSys.TRNS_CL_DPS_ADJ) {
                        sql = "SELECT id_dps_year AS year, id_dps_doc AS doc FROM trn_dps_dps_adj AS tdda " 
                                + "WHERE id_adj_year = " + entryKey[0] + " AND id_adj_doc = " + entryKey[1] + " AND id_adj_ety = " + entryKey[2] + "";
                        assocDocs = getDocuments(sql, assocDocs, false);
                    }
                    
                    // Busqueda hacia abajo (trn_dps_dps_adj):
                    if (dpsClass == SDataConstantsSys.TRNS_CL_DPS_DOC) {
                        sql = "SELECT id_adj_year AS year, id_adj_doc AS doc FROM trn_dps_dps_adj AS tdda " 
                                + "WHERE id_dps_year = " + entryKey[0] + " AND id_dps_doc = " + entryKey[1] + " AND id_dps_ety = " + entryKey[2] + "";
                        assocDocs = getDocuments(sql, assocDocs, false);
                    }
                
                assocDocs.stream().map((assocDoc) -> (SDataDps) SDataUtilities.readRegistry(miClient,
                        SDataConstants.TRN_DPS, assocDoc, SLibConstants.EXEC_MODE_SILENT)).forEach((dpsAux) -> {
                            getAssociatedDocuments(dpsAux);
                        });
                }
            }
            catch (Exception e) {
                miClient.showMsgBoxWarning(e.getMessage());
            }
        }
    }
    
    private ArrayList<int[]> getDocuments(String sql, ArrayList<int[]> docs, boolean isToUpdate) throws Exception {
        try (ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                int auxKey[];
                if (isToUpdate) {
                    auxKey = new int[] { resultSet.getInt("year"), resultSet.getInt("doc"), resultSet.getInt("ety") };
                }
                else {
                    auxKey = new int[] { resultSet.getInt("year"), resultSet.getInt("doc") };
                }
                boolean insert = true;
                for (int[] doc : docs) {
                    if (SLibUtilities.compareKeys(doc, auxKey)) {
                        insert = false;
                        break;
                    }
                }
                if (insert) {
                    docs.add(auxKey);
                }
            }
        }
        return docs;
    }
    
    private boolean lockDocuments() {
        boolean error = false;
        for (SDataDps document : moDocuments) {
            error = readDpsUser(document);
            if (error) {
                return error;
            }
        }
        return error;
    }
    
    private boolean readDpsUser(SDataDps dps) {
        boolean error = true;

        if (dps != moDps) {
            if (dps != null) { 
            SSrvLock lock = gainDpsUserLock(dps);

                if (lock != null) {
                    dps.setAuxUserLock(lock);
                    error = false;
                }
            }
        }
        else error = false;

        return error;
    }
    
    private sa.lib.srv.SSrvLock gainDpsUserLock(SDataDps dps) {
        SSrvLock lock;

        try {
            lock = SSrvUtils.gainLock(miClient.getSession(), miClient.getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.TRN_DPS, dps.getPrimaryKey(), dps.getRegistryTimeout());
        }
        catch (Exception e) {
            lock = null;
            miClient.showMsgBoxWarning("No fue posible obtener el acceso exclusivo al registro '" + 
                    SLibUtils.DateFormatDateYearMonth.format(dps.getDateDoc()) + " " + dps.getNumberSeries() + dps.getNumber() + "'.\n" + e);
        }

        return lock;
    }
    
    private void releaseDpsUserLock() {
        moDocuments.stream().forEach((document) -> {
            sa.lib.srv.SSrvLock lock = document.getAuxUserLock();
            if (lock != null) {
                try {
                    SSrvUtils.releaseLock(miClient.getSession(), lock);
                    document.setAuxUserLock(null);
                }
                catch (Exception e) {
                    miClient.showMsgBoxWarning("No fue posible liberar el acceso exclusivo del registro '" + 
                            SLibUtils.DateFormatDateYearMonth.format(document.getDateDoc()) + " " + document.getNumberSeries() + document.getNumber() + "'.\n" + e);
                }
            }
        });
    }
    
    private void populateTable() {
        moConceptTablePane.createTable();
        
        for (int i = 0; i < moDps.getDbmsDpsEntries().size(); i++) {
            SRowDpsEdit row = new SRowDpsEdit(miClient, moDps.getDbmsDpsEntries().get(i));
            moConceptTablePane.addTableRow(row);
        }
        
        moConceptTablePane.renderTableRows();
        moConceptTablePane.setTableRowSelection(0);
        moConceptTablePane.getTable().getTableHeader().setReorderingAllowed(false);
    }
    
    private void actionSelectItem() { 
        if (jbSelectItem.isEnabled()) {
            int selectedRow = moConceptTablePane.getTable().getSelectedRow();
            
            if (selectedRow == -1) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_ROW_UNDEF); 
            }
            else {
                SRowDpsEdit rowDpsEdit = (SRowDpsEdit) moConceptTablePane.getSelectedTableRow();

                if (moPickerItems == null) {
                    moPickerItems = SFormOptionPickerItems.createOptionPicker(miClient, SDataConstants.ITMX_ITEM_IOG, moPickerItems);
                }
                moPickerItems.formReset();
                
                moPickerItems.setFilterKey(SDataConstantsSys.ITMS_CL_ITEM_PUR_CON);
                moPickerItems.formRefreshOptionPane();
                moPickerItems.setSelectedPrimaryKey(rowDpsEdit.getItemNew() == null ? 
                        new int [] { rowDpsEdit.getItemOld().getPkItemId() } : new int [] { rowDpsEdit.getItemNew().getPkItemId() });
                moPickerItems.setFormVisible(true); 

                if (moPickerItems.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    SDataItem item = (SDataItem) SDataUtilities.readRegistry(miClient,
                        SDataConstants.ITMU_ITEM, (int[]) moPickerItems.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                    rowDpsEdit.setItemNew(item);
                    
                    rowDpsEdit.prepareTableRow();
                    moConceptTablePane.renderTableRows();
                    moConceptTablePane.setTableRowSelection(selectedRow);
                }
            }
        }
    }

    private void actionCostCenter() {
        if (jbCostCenter.isEnabled()) {
            int selectedRow = moConceptTablePane.getTable().getSelectedRow();
            if (selectedRow == -1) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_ROW_UNDEF); 
            }
            else {
                SRowDpsEdit rowDpsEdit = (SRowDpsEdit) moConceptTablePane.getSelectedTableRow();
                if (moPickerCostCenter == null) {        
                    moPickerCostCenter = SFormOptionPicker.createOptionPicker(miClient, SDataConstants.FIN_CC, moPickerCostCenter);
                }
                moPickerCostCenter.formReset();
                moPickerCostCenter.formRefreshOptionPane();
                try {
                    moPickerCostCenter.setSelectedPrimaryKey(SDataUtilities.obtainCostCenterItem(miClient, rowDpsEdit.getItemNew() == null ? 
                            rowDpsEdit.getItemOld().getPkItemId() : rowDpsEdit.getItemNew().getPkItemId()));
                }
                catch (Exception e){
                    SLibUtils.printException(this, e);
                }
                moPickerCostCenter.setFormVisible(true); 

                Object key;
                if (moPickerCostCenter.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    key = moPickerCostCenter.getSelectedPrimaryKey();
                    SDataCostCenter costCenter = (SDataCostCenter) SDataUtilities.readRegistry(miClient,
                            SDataConstants.FIN_CC, key, SLibConstants.EXEC_MODE_SILENT);
                    rowDpsEdit.setCostCenterNew(costCenter);
                    rowDpsEdit.prepareTableRow();
                    moConceptTablePane.renderTableRows();
                    moConceptTablePane.setTableRowSelection(selectedRow);
                }
            }
        }
    }
    
    private void updateAssocDocuments(Object primaryKey, SRowDpsEdit rowDpsEdit) {
        int[] entryKey = (int[]) primaryKey;
        moUpdateDocumentEntryKeys = new ArrayList<>();
        getUpdateEntryKeys(entryKey);
        moUpdateDocumentEntryKeys.stream().forEach((int[] updateDocumentEntryKey) -> {
            for (SDataDps document : moDocuments) {
                SDataDpsEntry entry = document.getDbmsDpsEntry(updateDocumentEntryKey);
                if (entry != null) {
                    if (rowDpsEdit.getItemNew() != null) {
                        entry.setConceptKey(rowDpsEdit.getItemNew().getCode());
                        entry.setConcept(rowDpsEdit.getItemNew().getItem());
                        entry.setFkItemId(rowDpsEdit.getItemNew().getPkItemId());
                        entry.setIsRegistryEdited(true);
                    }
                    if (rowDpsEdit.getCostCenterNew() != null) {
                        entry.setFkCostCenterId_n(rowDpsEdit.getCostCenterNew().getPkCostCenterIdXXX());
                        entry.setDbmsCostCenterCode(rowDpsEdit.getCostCenterNew().getCode());
                        entry.setDbmsCostCenter_n(rowDpsEdit.getCostCenterNew().getCostCenter());
                        entry.setIsRegistryEdited(true);
                    }
                    break;
                }
            }
        });
    }
    
    private void getUpdateEntryKeys(int[] entryKey){
        boolean contains = false; 
        for (int[] key : moUpdateDocumentEntryKeys) {
            if (SLibUtilities.compareKeys(key, entryKey)) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            moUpdateDocumentEntryKeys.add(entryKey);
            ArrayList<int[]> assocKeys = new ArrayList<>(); 
            String sql;
            try {
                // Busqueda hacia arriba (trn_dps_dps_supply):
                sql = "SELECT id_src_year AS year, id_src_doc AS doc, id_src_ety AS ety FROM trn_dps_dps_supply AS tdds "
                            + "WHERE id_des_year = " + entryKey[0] + " AND id_des_doc = " + entryKey[1] + " AND id_des_ety = " + entryKey[2] + "";    
                assocKeys = getDocuments(sql, assocKeys, true);

                // Busqueda hacia abajo (trn_dps_dps_supply): 
                sql = "SELECT id_des_year AS year, id_des_doc AS doc, id_des_ety AS ety FROM trn_dps_dps_supply AS tdds "
                        + "WHERE id_src_year = " + entryKey[0] + " AND id_src_doc = " + entryKey[1] + " AND id_src_ety = " + entryKey[2] + "";  
                assocKeys = getDocuments(sql, assocKeys, true);

                // Busqueda hacia arriba (trn_dps_dps_adj):
                sql = "SELECT id_dps_year AS year, id_dps_doc AS doc, id_dps_ety AS ety FROM trn_dps_dps_adj AS tdda " 
                        + "WHERE id_adj_year = " + entryKey[0] + " AND id_adj_doc = " + entryKey[1] + " AND id_adj_ety = " + entryKey[2] + "";
                assocKeys = getDocuments(sql, assocKeys, true);

                // Busqueda hacia abajo (trn_dps_dps_adj):
                sql = "SELECT id_adj_year AS year, id_adj_doc AS doc, id_adj_ety AS ety FROM trn_dps_dps_adj AS tdda " 
                        + "WHERE id_dps_year = " + entryKey[0] + " AND id_dps_doc = " + entryKey[1] + " AND id_dps_ety = " + entryKey[2] + "";
                assocKeys = getDocuments(sql, assocKeys, true);

                assocKeys.stream().forEach((assocKey) -> {
                    getUpdateEntryKeys(assocKey);
                });
            }
            catch (Exception e) {
                miClient.showMsgBoxWarning(e.getMessage());
            }
        }
    }
       
    private void actionOk() {
        SFormValidation validation = formValidate();
        if (validation.getIsError()){
            miClient.showMsgBoxWarning(validation.getMessage());
        }
        else {
            mnFormResult = SLibConstants.FORM_RESULT_OK;
            setVisible(false);
        }
    }

    private void actionCancel() {
        releaseDpsUserLock();
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbCostCenter;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbSelectItem;
    private javax.swing.JLabel jlDateCfdi;
    private javax.swing.JLabel jlInvoiceCfdi;
    private javax.swing.JLabel jlNameEmisor;
    private javax.swing.JLabel jlRfcEmisor;
    private javax.swing.JPanel jpCfdiChangeItem;
    private javax.swing.JPanel jpCfdiConceptSetup;
    private javax.swing.JPanel jpCfdiConcepts;
    private javax.swing.JPanel jpCfdiConceptsDataNorth;
    private javax.swing.JPanel jpCfdiConceptsGrid;
    private javax.swing.JPanel jpCfdiData;
    private javax.swing.JPanel jpCfdiHeader;
    private javax.swing.JPanel jpControls;
    private javax.swing.JTextField jtfDateCfdi;
    private javax.swing.JTextField jtfInvoiceCfdi;
    private javax.swing.JTextField jtfNameEmisor;
    private javax.swing.JTextField jtfPaymentType;
    private javax.swing.JTextField jtfRfcEmisor;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setFormVisible(boolean visible) {
        setVisible(visible);
    }
    
    @Override
    public erp.lib.form.SFormValidation formValidate() { 
        boolean isAnyEntryChanged = false;
        SFormValidation validation = new SFormValidation();
        
        for (int i = 0; i < moConceptTablePane.getTableGuiRowCount(); i++) {
            SRowDpsEdit row = (SRowDpsEdit) moConceptTablePane.getTableRow(i); 
            
            if (row.getItemNew() != null || row.getCostCenterNew() != null) {
                isAnyEntryChanged = true;
            }
        }
        if (!isAnyEntryChanged) {
            validation.setMessage("Ninguna entrada tiene un item o un centro de costo nuevo seleccionado.");
        }
        if (!validation.getIsError()) {
            SDataDps dps = comprobateRegistry();
            SServerRequest request = new SServerRequest(SServerConstants.REQ_DB_CAN_SAVE);
            SServerResponse response;

            request.setPacket(dps);
            response = miClient.getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK || response.getResultType() != SLibConstants.DB_CAN_SAVE_YES) {
                validation.setMessage(response.getMessage().isEmpty() ? SLibConstants.MSG_ERR_UTIL_UNKNOWN_ERR : response.getMessage());
            }
        }
        
        return validation;
    }
    
    private SDataDps comprobateRegistry(){
        for (int i = 0; i < moConceptTablePane.getTableGuiRowCount(); i++) {
            SRowDpsEdit row = (SRowDpsEdit) moConceptTablePane.getTableRow(i);
            SDataDpsEntry entry = moDps.getDbmsDpsEntry((int[]) row.getDpsEntryPK());
            if (row.getItemNew() != null) {
                entry.setConceptKey(row.getItemNew().getCode());
                entry.setConcept(row.getItemNew().getItem());
                entry.setFkItemId(row.getItemNew().getPkItemId());
                entry.setIsRegistryEdited(true);
            }
            if (row.getCostCenterNew() != null) {
                entry.setFkCostCenterId_n(row.getCostCenterNew().getPkCostCenterIdXXX());
                entry.setDbmsCostCenterCode(row.getCostCenterNew().getCode());
                entry.setDbmsCostCenter_n(row.getCostCenterNew().getCostCenter());
                entry.setIsRegistryEdited(true);
            }
        }
        return moDps;
    }
        
    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;
        mbDocuentsLockedError = false;
        
        moDps = null;
        moDocuments = null;
        moUpdateDocumentEntryKeys = null;
        moConceptTablePane.clearTableRows();
    }

    @Override
    public void formRefreshCatalogues() {
    }

    @Override
    public void setFormStatus(int status) {
        mnFormStatus = status;
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
    public void setRegistry(SDataRegistry registry) {
        moDps = (SDataDps) registry;
        
        moDocuments = new ArrayList<>();
        getAssociatedDocuments(moDps);
        
        mbDocuentsLockedError = lockDocuments();
        
        SDataBizPartner bizPartnerEmisor = (SDataBizPartner) SDataUtilities.readRegistry(miClient, 
            SDataConstants.BPSU_BP, new int[] { moDps.getFkBizPartnerId_r() }, SLibConstants.EXEC_MODE_SILENT);

        jtfRfcEmisor.setText(bizPartnerEmisor.getFiscalId());
        jtfNameEmisor.setText(bizPartnerEmisor.getBizPartner());
        jtfInvoiceCfdi.setText(moDps.getNumberSeries() + moDps.getNumber());
        jtfPaymentType.setText(moDps.getFkPaymentTypeId() == SDataConstantsSys.TRNS_TP_PAY_CASH ? 
                SDataConstantsSys.TRNS_CFD_CAT_PAY_MET_PUE : SDataConstantsSys.TRNS_CFD_CAT_PAY_MET_PPD);
        jtfDateCfdi.setText(SLibUtils.DbmsDateFormatDatetime.format(moDps.getDateDoc()));

        jtfRfcEmisor.setCaretPosition(0); 
        jtfNameEmisor.setCaretPosition(0);
        jtfInvoiceCfdi.setCaretPosition(0);
        jtfPaymentType.setCaretPosition(0);
        jtfDateCfdi.setCaretPosition(0);
        populateTable();

    }

    @Override
    public SDataRegistry getRegistry() {
        for (int i = 0; i < moConceptTablePane.getTableGuiRowCount(); i++) {
            SRowDpsEdit row = (SRowDpsEdit) moConceptTablePane.getTableRow(i);
            SDataDpsEntry entry = moDps.getDbmsDpsEntry((int[]) row.getDpsEntryPK());
            if (row.getItemNew() != null || row.getCostCenterNew() != null) {
                updateAssocDocuments(entry.getPrimaryKey(), row);
            }
        }
        moDpsEntryEdit = new SDataDpsEntryEdit(mnFormType);
        moDpsEntryEdit.setDocuments(moDocuments);
        
        return moDpsEntryEdit;
    }

    @Override
    public Object getValue(int type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void setValue(int type, Object value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JLabel getTimeoutLabel() {
        return null;
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbSelectItem) {
                actionSelectItem();
            }
            else if (button == jbCostCenter) {
                actionCostCenter();
            }
        }
    }
}
