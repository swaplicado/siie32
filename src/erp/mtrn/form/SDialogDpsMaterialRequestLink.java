package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SFinUtils;
import erp.mod.trn.db.SDbMaterialRequest;
import erp.mod.trn.db.SDbMaterialRequestEntry;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsMaterialRequest;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SDataDpsEntryNotes;
import erp.mtrn.data.SDataMaterialRequestEntryLinkRow;
import erp.mtrn.data.STrnFunctionalAreaUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author Edwin Carmona
 */
public class SDialogDpsMaterialRequestLink extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, javax.swing.event.ListSelectionListener, java.awt.event.ItemListener {

    private erp.client.SClientInterface miClient;
    private int mnOptionType;
    private int mnFormResult;
    private int mnFormStatus;
    private erp.lib.table.STablePane moTablePane;

    private int[] maDpsTypeKey;
    private SDbMaterialRequest moMaterialRequest;
    private SDataDps moDps;
    private ArrayList<SDataDpsEntry> mlEtys;
    private erp.mtrn.form.SPanelMaterialRequest moPanelMatRequest;
    private SDialogPickerMatRequest moDialogPickerMatRequest;
    
    private erp.lib.form.SFormField moFieldFinderUser;
    private erp.lib.form.SFormField moFieldFinderConsumeEntity;
    private erp.lib.form.SFormField moFieldFinderConsumeSubEntity;

    /** Creates new form SDialogDpsLink
     * @param client
     * @param dpsType */
    public SDialogDpsMaterialRequestLink(erp.client.SClientInterface client, final int[] dpsType) {
        super(client.getFrame(), true);
        miClient = client;
        mnOptionType = SDataConstants.TRNX_DPS_MAT_REQ_LINKS;
        this.maDpsTypeKey = dpsType;
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

        jpFinder = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlDocType = new javax.swing.JLabel();
        moTextFinderDocumentType = new sa.lib.gui.bean.SBeanFieldText();
        jPanel3 = new javax.swing.JPanel();
        jlConsumeEntity = new javax.swing.JLabel();
        jcbFinderConsEntity = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jlUser = new javax.swing.JLabel();
        jcbFinderUser = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jlConsumeSubEntity = new javax.swing.JLabel();
        jcbFinderConsSubEntity = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        moTextFinderFolio = new sa.lib.gui.bean.SBeanFieldText();
        jbPickerMatRequest = new javax.swing.JButton();
        jbCleanFinder = new javax.swing.JButton();
        jpDps = new javax.swing.JPanel();
        jlPanelMatRequest = new javax.swing.JLabel();
        jpOptions = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jbSetEverything = new javax.swing.JButton();
        jbSetNothing = new javax.swing.JButton();
        jpControls = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Vinculación de documento de compras-ventas con requisiciones");
        setResizable(false);

        jpFinder.setLayout(new java.awt.GridLayout(3, 2));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlDocType.setText("Tipo de documento:");
        jlDocType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel2.add(jlDocType);

        moTextFinderDocumentType.setText("sBeanFieldText1");
        moTextFinderDocumentType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel2.add(moTextFinderDocumentType);

        jpFinder.add(jPanel2);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlConsumeEntity.setText("Centro de consumo:");
        jlConsumeEntity.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel3.add(jlConsumeEntity);

        jcbFinderConsEntity.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFinderConsEntity.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel3.add(jcbFinderConsEntity);

        jpFinder.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlUser.setText("Usuario:");
        jlUser.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlUser);

        jcbFinderUser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFinderUser.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel4.add(jcbFinderUser);

        jpFinder.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlConsumeSubEntity.setText("Sucentro de consumo:");
        jlConsumeSubEntity.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel5.add(jlConsumeSubEntity);

        jcbFinderConsSubEntity.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFinderConsSubEntity.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel5.add(jcbFinderConsSubEntity);

        jpFinder.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel6.setText("Folio:");
        jLabel6.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jLabel6);

        moTextFinderFolio.setText("sBeanFieldText2");
        moTextFinderFolio.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel6.add(moTextFinderFolio);

        jbPickerMatRequest.setText("...");
        jbPickerMatRequest.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel6.add(jbPickerMatRequest);

        jbCleanFinder.setText("Limpiar");
        jPanel6.add(jbCleanFinder);

        jpFinder.add(jPanel6);

        getContentPane().add(jpFinder, java.awt.BorderLayout.PAGE_START);

        jpDps.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpDps.setLayout(new java.awt.BorderLayout());

        jlPanelMatRequest.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlPanelMatRequest.setText("[Panel de documento de requisición]");
        jlPanelMatRequest.setPreferredSize(new java.awt.Dimension(100, 200));
        jpDps.add(jlPanelMatRequest, java.awt.BorderLayout.NORTH);

        jpOptions.setBorder(javax.swing.BorderFactory.createTitledBorder("Partidas del documento disponibles para vinculación:"));
        jpOptions.setLayout(new java.awt.BorderLayout(0, 2));

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbSetEverything.setText("Todo");
        jbSetEverything.setToolTipText("Surtir todo");
        jbSetEverything.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel13.add(jbSetEverything);

        jbSetNothing.setText("Limpiar");
        jbSetNothing.setToolTipText("Limpiar captura");
        jbSetNothing.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel13.add(jbSetNothing);

        jpOptions.add(jPanel13, java.awt.BorderLayout.NORTH);

        jpDps.add(jpOptions, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpDps, java.awt.BorderLayout.CENTER);

        jpControls.setPreferredSize(new java.awt.Dimension(392, 33));
        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jpControls.add(jbCancel);

        getContentPane().add(jpControls, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(900, 600));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsExtra() {
        moFieldFinderUser = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFinderUser, jlUser);
        moFieldFinderConsumeEntity = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFinderConsEntity, jlConsumeEntity);
        moFieldFinderConsumeSubEntity = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFinderConsSubEntity, jlConsumeSubEntity);
        
        int i = 0;
        STableColumnForm[] columns = null;

        moTablePane = new STablePane(miClient);
        jpOptions.add(moTablePane, BorderLayout.CENTER);

        columns = new STableColumnForm[11];
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Clave", STableConstants.WIDTH_ITEM_KEY);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Concepto", 250);
        columns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cant.", STableConstants.WIDTH_QUANTITY);
        columns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        columns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cant. surt.", STableConstants.WIDTH_QUANTITY);
        columns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        columns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cant. vinc.", STableConstants.WIDTH_QUANTITY);
        columns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        columns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cant. a vinc.", STableConstants.WIDTH_QUANTITY);
        columns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        columns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cant. actual", STableConstants.WIDTH_QUANTITY);
        columns[i].setEditable(true);
        columns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        columns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Precio.", STableConstants.WIDTH_VALUE);
        columns[i].setEditable(true);
        columns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererCurrency());
        columns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "% excedente", STableConstants.WIDTH_PERCENTAGE);
        columns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());

        for (i = 0; i < columns.length; i++) {
            moTablePane.addTableColumn(columns[i]);
        }
        
        moDialogPickerMatRequest = new SDialogPickerMatRequest(miClient);

        moPanelMatRequest = new SPanelMaterialRequest(miClient, "de origen");
        jpDps.remove(jlPanelMatRequest);
        jpDps.add(moPanelMatRequest, BorderLayout.NORTH);
        
        jbPickerMatRequest.addActionListener(this);
        jbCleanFinder.addActionListener(this);
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbSetEverything.addActionListener(this);
        jbSetNothing.addActionListener(this);
        
        jcbFinderConsEntity.addItemListener(this);

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
        
        formReset();
    }
    
    private void resetPanelFinder() {
        moTextFinderDocumentType.setValue("REQUISICIÓN DE MATERIALES");
        moTextFinderDocumentType.setEnabled(false);
        jcbFinderUser.setSelectedIndex(-1);
        jcbFinderConsEntity.setSelectedIndex(-1);
        jcbFinderConsSubEntity.setSelectedIndex(-1);
        moTextFinderFolio.setText("");
    }

    private void renderMaterialRequestEntries() {
        moTablePane.createTable();
        moTablePane.clearTableRows();

        if (moMaterialRequest != null) {
            for (SDbMaterialRequestEntry entry : moMaterialRequest.getChildEntries()) {
                if (! entry.isDeleted()) {
                    SDataMaterialRequestEntryLinkRow entryLink = null;
                    if (moDps == null) {
                        entryLink = new SDataMaterialRequestEntryLinkRow(miClient, 
                                                                        entry, 
                                                                        this.maDpsTypeKey, 
                                                                        null, 
                                                                        null);
                    }
                    else {
                        entryLink = new SDataMaterialRequestEntryLinkRow(miClient, 
                                                                        entry, 
                                                                        this.maDpsTypeKey, 
                                                                        new int[] { moDps.getPkYearId(), moDps.getPkDocId() }, 
                                                                        moDps.getDbmsDpsEntries());
                    }

                    moTablePane.addTableRow(entryLink);
                }
            }

            moTablePane.renderTableRows();
            moTablePane.setTableRowSelection(0);
                    
            ListSelectionModel selectionModel = moTablePane.getTable().getSelectionModel();
            selectionModel.addListSelectionListener(this);
            moTablePane.getTable().setSelectionModel(selectionModel);
        }
    }

    private boolean validateQuantitiesToLink() {
        boolean warning = false;
        SDataMaterialRequestEntryLinkRow entry = null;

        for (int i = 0; i < moTablePane.getTableGuiRowCount(); i++) {
            entry = (SDataMaterialRequestEntryLinkRow) moTablePane.getTableRow(i);
            if (entry.getQuantityToLinkV() > entry.getQuantityRemaining()) {
                warning = true;
                break;
            }
        }

        if (warning) {
            return miClient.showMsgBoxConfirm("Algunas partidas rebasan la cantidad de la requisición \n ¿Desea continuar?") == JOptionPane.YES_OPTION;
        }

        return true;
    }
    
    private void actionCleanFinder() {
        jcbFinderUser.setSelectedIndex(0);
        moTextFinderFolio.setText("");
        jcbFinderConsEntity.setSelectedIndex(0);
    }
    
    private void actionPickMatRequest() {
        Object[] filterKey = null;

        moMaterialRequest = null;
        filterKey = new Object[] { 
                                    moFieldFinderUser.getKeyAsIntArray()[0],
                                    moFieldFinderConsumeEntity.getKeyAsIntArray()[0],
                                    moFieldFinderConsumeSubEntity.getKeyAsIntArray(),
                                    moTextFinderFolio.getText()
                                };

        moDialogPickerMatRequest.formReset();
        moDialogPickerMatRequest.setFilterKey(filterKey);
        moDialogPickerMatRequest.formRefreshOptionPane();
        moDialogPickerMatRequest.setFormVisible(true);

        if (moDialogPickerMatRequest.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moMaterialRequest = new SDbMaterialRequest();
            try {
                moMaterialRequest.read(miClient.getSession(), (int[]) moDialogPickerMatRequest.getSelectedPrimaryKey());
            }
            catch (Exception ex) {
                Logger.getLogger(SDialogDpsMaterialRequestLink.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            actionShowMaterialRequest();
        }
    }
    
    private void actionChangeConsumeEntity() {
        if (moFieldFinderConsumeEntity.getKeyAsIntArray() != null && moFieldFinderConsumeEntity.getKeyAsIntArray()[0] > 0) {
            SFormUtilities.populateComboBox(miClient, jcbFinderConsSubEntity, SDataConstants.TRN_MAT_CONS_SUBENT, new int[] { moFieldFinderConsumeEntity.getKeyAsIntArray()[0] });
            jcbFinderConsSubEntity.setEnabled(true);
        }
        else {
            jcbFinderConsSubEntity.setSelectedIndex(0);
            jcbFinderConsSubEntity.setEnabled(false);
        }
    }
    
    private void actionShowMaterialRequest() {
        moPanelMatRequest.setMaterialRequest(moMaterialRequest);
        
        renderMaterialRequestEntries();
    }

    private void actionLinkAll() {
        int index = moTablePane.getTable().getSelectedRow();
        SDataMaterialRequestEntryLinkRow entry = null;

        for (int i = 0; i < moTablePane.getTableGuiRowCount(); i++) {
            entry = (SDataMaterialRequestEntryLinkRow) moTablePane.getTableRow(i);
            entry.setQuantityToLink(entry.getQuantityRemaining());
            entry.prepareTableRow();
        }

        moTablePane.renderTableRows();
        moTablePane.getTable().setRowSelectionInterval(index, index);
    }

    private void actionLinkNothing() {
        int index = moTablePane.getTable().getSelectedRow();
        SDataMaterialRequestEntryLinkRow entry = null;

        for (int i = 0; i < moTablePane.getTableGuiRowCount(); i++) {
            entry = (SDataMaterialRequestEntryLinkRow) moTablePane.getTableRow(i);
            entry.setQuantityToLink(0d);
        }

        moTablePane.renderTableRows();
        moTablePane.getTable().setRowSelectionInterval(index, index);
    }
    
    private void processData() {
        if (moDps == null) {
            moDps = new SDataDps();
            moDps.setIsRecordAutomatic(true);
            moDps.setFkDpsNatureId(SDataConstantsSys.TRNU_DPS_NAT_DEF);
            moDps.setFkIncotermId(SModSysConsts.LOGS_INC_NA);
            moDps.setFkModeOfTransportationTypeId(SModSysConsts.LOGS_TP_MOT_NA);
            moDps.setFkCarrierTypeId(SModSysConsts.LOGS_TP_CAR_NA);
            ArrayList<String> areas = STrnFunctionalAreaUtils.getFunctionalAreasOfUser(miClient, 
                                                                                miClient.getSession().getUser().getPkUserId(), 
                                                                                STrnFunctionalAreaUtils.FUNC_AREA_ID);
            if (areas.size() > 0) {
                moDps.setFkFunctionalAreaId(Integer.parseInt(areas.get(0)));
            }
            else {
                moDps.setFkFunctionalAreaId(SModSysConsts.CFGU_FUNC_NA);
            }
            
            mlEtys = null;
        }
        else {
            mlEtys = new ArrayList<>();
        }
        
        SDataMaterialRequestEntryLinkRow oTableRow;
        for (int i = 0; i < moTablePane.getTableGuiRowCount(); i++) {
            oTableRow = (SDataMaterialRequestEntryLinkRow) moTablePane.getTableRow(i);
            if (oTableRow.getQuantityToLinkV() > 0d) {
                SDataDpsEntry oEntry = new SDataDpsEntry();
                oEntry.setConceptKey(oTableRow.getItem().getKey());
                oEntry.setConcept(oTableRow.getItem().getName());
                oEntry.setOriginalQuantity(oTableRow.getQuantityToLinkV());
                oEntry.setQuantity(SLibUtilities.round(oEntry.getOriginalQuantity() * ((SSessionCustom) miClient.getSession().getSessionCustom()).getUnitsFactorForQuantity(oEntry.getFkItemId(), oEntry.getFkOriginalUnitId(), oEntry.getFkUnitId()), miClient.getSessionXXX().getParamsErp().getDecimalsQuantity()));
                oEntry.setPriceUnitary(oTableRow.getMaterialRequestEntry().getPriceUnitary());
                oEntry.setPriceUnitarySystem(oTableRow.getMaterialRequestEntry().getPriceUnitarySystem());
                oEntry.setOriginalPriceUnitaryCy(oTableRow.getMaterialRequestEntry().getPriceUnitary());
                oEntry.setOriginalPriceUnitarySystemCy(oTableRow.getMaterialRequestEntry().getPriceUnitarySystem());

                oEntry.setFkItemId(oTableRow.getItem().getPkItemId());
                oEntry.setFkUnitId(oTableRow.getItem().getFkUnitId());
                oEntry.setFkOriginalUnitId(oTableRow.getItem().getFkUnitId());
                oEntry.setIsPrepayment(oTableRow.getItem().getIsPrepayment());
                oEntry.setIsInventoriable(oTableRow.getItem().getIsInventoriable());

                oEntry.setDbmsFkItemGenericId(oTableRow.getItem().getFkItemGenericId());
                oEntry.setDbmsOriginalUnitSymbol(oTableRow.getUnit().getSymbol());
                oEntry.setFkDpsEntryTypeId(SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY);
                oEntry.setFkDpsAdjustmentTypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[0]);
                oEntry.setFkDpsAdjustmentSubtypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[1]);
                int idCc = 0;
                if (oTableRow.getMaterialRequestEntry().getCcConfigs().size() > 0) {
                    idCc = oTableRow.getMaterialRequestEntry().getCcConfigs().get(0).getFkCostCenterId();
                }
                else {
                    if (moMaterialRequest.getChildCostCenters() != null && moMaterialRequest.getChildCostCenters().size() > 0) {
                        idCc = moMaterialRequest.getChildCostCenters().get(0).getPkCostCenterId();
                    }
                }
                if (idCc > 0) {
                    oEntry.setFkCostCenterId_n(SFinUtils.getCostCenterFormerIdXXX(miClient.getSession(), idCc));
                }
                oEntry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                oEntry.setIsTaxesAutomaticApplying(true);

                if (oTableRow.getItem() != null) {
                    oEntry.setLength(!oTableRow.getItem().getDbmsDataItemGeneric().getIsLengthApplying() ? 0d : oEntry.getQuantity() * oTableRow.getItem().getLength());
                    oEntry.setSurface(!oTableRow.getItem().getDbmsDataItemGeneric().getIsSurfaceApplying() ? 0d : oEntry.getQuantity() * oTableRow.getItem().getSurface());
                    oEntry.setVolume(!oTableRow.getItem().getDbmsDataItemGeneric().getIsVolumeApplying() ? 0d : oEntry.getQuantity() * oTableRow.getItem().getVolume());
                    oEntry.setMass(!oTableRow.getItem().getDbmsDataItemGeneric().getIsMassApplying() ? 0d : oEntry.getQuantity() * oTableRow.getItem().getMass());
                    oEntry.setWeightGross(!oTableRow.getItem().getDbmsDataItemGeneric().getIsWeightGrossApplying() ? 0d : oEntry.getQuantity() * oTableRow.getItem().getWeightGross());
                    oEntry.setWeightDelivery(!oTableRow.getItem().getDbmsDataItemGeneric().getIsWeightDeliveryApplying() ? 0d : oEntry.getQuantity() * oTableRow.getItem().getWeightDelivery());
                }

                // Links con requisición de materiales
                SDataDpsMaterialRequest oDpsMatReqLink = new SDataDpsMaterialRequest();
                oDpsMatReqLink.setQuantity(oEntry.getOriginalQuantity());
                oDpsMatReqLink.setValue(oEntry.getPriceUnitary());
                oDpsMatReqLink.setValueCy(oEntry.getPriceUnitaryCy());
                oDpsMatReqLink.setFkDpsYearId(oEntry.getPkYearId());
                oDpsMatReqLink.setFkDpsDocId(oEntry.getPkDocId());
                oDpsMatReqLink.setFkDpsEntryId(oEntry.getPkEntryId());
                oDpsMatReqLink.setFkMaterialRequestId(oTableRow.getMaterialRequestEntry().getPkMatRequestId());
                oDpsMatReqLink.setFkMaterialRequestEntryId(oTableRow.getMaterialRequestEntry().getPkEntryId());

                oEntry.setDbmsDpsEntryMatRequest(oDpsMatReqLink);
                
                // Notas de las partidas para indicar visualmente el link con la requisición
                SDataDpsEntryNotes oNote = new SDataDpsEntryNotes();
                String note = "Fol. Req.: " + moMaterialRequest.getNumber() + ", "
                            + "Sol.: " + moPanelMatRequest.getAuxUserRequesterName() + ", "
                            + "Ent. Cons.: " + (oTableRow.getMaterialRequestEntry().getConsumptionInfo().isEmpty() ? 
                                            moMaterialRequest.getConsumptionInfo() : 
                                            oTableRow.getMaterialRequestEntry().getConsumptionInfo());
                oNote.setNotes(note);
                oNote.setIsAllDocs(true);
                oNote.setIsPrintable(true);
                oNote.setIsCfd(false);
                oNote.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                oNote.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                oNote.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                
                oEntry.getDbmsEntryNotes().add(oNote);

                if (mlEtys != null) {
                    mlEtys.add(oEntry);
                }
                else {
                    moDps.getDbmsDpsEntries().add(oEntry);
                }
            }
        }
    }

    private void actionOk() {
        if (validateQuantitiesToLink()) {
            processData();
            mnFormResult = SLibConstants.FORM_RESULT_OK;
            setVisible(false);
        }
    }

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbCleanFinder;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPickerMatRequest;
    private javax.swing.JButton jbSetEverything;
    private javax.swing.JButton jbSetNothing;
    private javax.swing.JComboBox jcbFinderConsEntity;
    private javax.swing.JComboBox jcbFinderConsSubEntity;
    private javax.swing.JComboBox jcbFinderUser;
    private javax.swing.JLabel jlConsumeEntity;
    private javax.swing.JLabel jlConsumeSubEntity;
    private javax.swing.JLabel jlDocType;
    private javax.swing.JLabel jlPanelMatRequest;
    private javax.swing.JLabel jlUser;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpDps;
    private javax.swing.JPanel jpFinder;
    private javax.swing.JPanel jpOptions;
    private sa.lib.gui.bean.SBeanFieldText moTextFinderDocumentType;
    private sa.lib.gui.bean.SBeanFieldText moTextFinderFolio;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;

        moMaterialRequest = null;
        moPanelMatRequest.setMaterialRequest(null);

        resetPanelFinder();
        formRefreshCatalogues();
        renderMaterialRequestEntries();
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbFinderUser, SDataConstants.USRU_USR);
        SFormUtilities.populateComboBox(miClient, jcbFinderConsEntity, SDataConstants.TRN_MAT_CONS_ENT);
        SFormUtilities.populateComboBox(miClient, jcbFinderConsSubEntity, SDataConstants.TRN_MAT_CONS_SUBENT, new int[] { moFieldFinderConsumeEntity.getKeyAsIntArray()[0] });
        
        jcbFinderConsEntity.setSelectedIndex(0);
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        int rows = 0;
        SFormValidation validation = new SFormValidation();

        if (!validation.getIsError()) {
            if (rows == 0) {
                validation.setMessage("Se debe especificar al menos una partida para vinculación.");
                validation.setComponent(moTablePane.getTable());
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case SDataConstants.TRNX_DPS_DES:
                this.maDpsTypeKey = (int[]) value;
                renderMaterialRequestEntries();
                break;
                
            case SDataConstants.TRN_DPS:
                if (value != null) {
                    moDps = (SDataDps) value;
                }
                else {
                    moDps = null;
                }
                break;
                
            default:
            
        }
    }

    @Override
    public java.lang.Object getValue(int type) {
        Object value = null;

        switch (type) {
            case SDataConstants.TRNX_DPS_DES:
                
                break;
            case SDataConstants.TRN_DPS:
                value = moDps;
                break;
            case SDataConstants.TRN_DPS_ETY:
                value = mlEtys;
                break;

            default:
        }

        return value;
    }

    @Override
    public javax.swing.JLabel getTimeoutLabel() {
        return null;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbPickerMatRequest) {
                actionPickMatRequest();
            }
            else if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCleanFinder) {
                actionCleanFinder();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbSetEverything) {
                actionLinkAll();
            }
            else if (button == jbSetNothing) {
                actionLinkNothing();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof javax.swing.JComboBox) {
            JComboBox comboBox = (JComboBox)  e.getSource();
            
            if (comboBox == jcbFinderConsEntity) {
                actionChangeConsumeEntity();
            }
        }
    }
}