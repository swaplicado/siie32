/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogRelatedDocument.java
 *
 * Created on 16/05/2022, 10:35:00 AM
 */

package erp.mtrn.form;

import cfd.ver3.DCfdVer3Consts;
import erp.cfd.SCfdXmlCatalogs;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STablePane;
import erp.lib.table.STableRow;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SRowRelatedDocument;
import erp.mtrn.data.STrnCfdRelated;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import sa.lib.SLibUtils;

/**
 * 
 * @author Isabel Servín
 */
public class SDialogRelatedDocument extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {
    
    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbIsFirstTime;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    
    private erp.lib.form.SFormField moFieldCfdiRelationType;
    private erp.lib.form.SFormField moFieldCfdiCfdiRelated;
    
    private final erp.client.SClientInterface miClient;
    private final SDataDps moDps;
    private STrnCfdRelated moTrnRelatedDocument;

    private erp.lib.table.STablePane moRelatedUuidPane;
    
    private erp.mtrn.form.SDialogPickerDps moDialogPickerDpsForCfdiRelated;
    
    private boolean bIsDpsInvoice;
    private int[] manDpsClassKey;
    private int[] manDpsClassPreviousKey;
    private int[] manFirstRelatedDpsKey;
    private String msRelatedDpsUuid;
    

    /** Creates new form SDialogRelatedUUID
     * @param client
     * @param dps
     */
    public SDialogRelatedDocument(erp.client.SClientInterface client, SDataDps dps) {
        super(client.getFrame(), true);
        miClient = client;
        moDps = dps;
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

        bgCancelationType = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlCfdiRelationType = new javax.swing.JLabel();
        jcbCfdiRelationType = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel10 = new javax.swing.JPanel();
        jlCfdiCfdiRelated = new javax.swing.JLabel();
        jtfCfdiCfdiRelated = new javax.swing.JTextField();
        jbCfdiCfdiRelated = new javax.swing.JButton();
        jlCfdiCfdiRelatedHint = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jbAddRow = new javax.swing.JButton();
        jbDelRow = new javax.swing.JButton();
        jpRelatedUUID = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Validación masiva de estatus de CFDI");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Documentos a relacionar:"));
        jPanel3.setLayout(new java.awt.GridLayout(3, 1, 0, 2));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdiRelationType.setText("Tipo relación:");
        jlCfdiRelationType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlCfdiRelationType);

        jcbCfdiRelationType.setToolTipText("Tipo relación de CFDI relacionados");
        jcbCfdiRelationType.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel6.add(jcbCfdiRelationType);

        jPanel3.add(jPanel6);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdiCfdiRelated.setText("CFDI relacionados:");
        jlCfdiCfdiRelated.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlCfdiCfdiRelated);

        jtfCfdiCfdiRelated.setText("TEXT");
        jtfCfdiCfdiRelated.setToolTipText("UUID de CFDI relacionados");
        jtfCfdiCfdiRelated.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel10.add(jtfCfdiCfdiRelated);

        jbCfdiCfdiRelated.setText("+");
        jbCfdiCfdiRelated.setToolTipText("Agregar");
        jbCfdiCfdiRelated.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbCfdiCfdiRelated.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel10.add(jbCfdiCfdiRelated);

        jlCfdiCfdiRelatedHint.setForeground(java.awt.SystemColor.textInactiveText);
        jlCfdiCfdiRelatedHint.setText("(separar varios UUID con coma, sin espacios intermedios; máximo hasta 100 UUID)");
        jlCfdiCfdiRelatedHint.setPreferredSize(new java.awt.Dimension(450, 23));
        jPanel10.add(jlCfdiCfdiRelatedHint);

        jPanel3.add(jPanel10);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbAddRow.setText("Agregar");
        jbAddRow.setToolTipText("Agrega los CFDI a la tabla");
        jbAddRow.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel16.add(jbAddRow);

        jbDelRow.setText("Eliminar");
        jbDelRow.setToolTipText("Quita el renglón seleccionado de la tabla");
        jbDelRow.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel16.add(jbDelRow);

        jPanel3.add(jPanel16);

        jPanel11.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel2.add(jPanel11, java.awt.BorderLayout.NORTH);

        jpRelatedUUID.setBorder(javax.swing.BorderFactory.createTitledBorder("Documentos relacionados:"));
        jpRelatedUUID.setLayout(new java.awt.BorderLayout());
        jPanel2.add(jpRelatedUUID, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(692, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel1.add(jbOk);

        jbCancel.setText("Cerrar");
        jbCancel.setToolTipText("[Escape]");
        jbCancel.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel1.add(jbCancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(803, 485));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        int i;
        erp.lib.table.STableColumnForm tableColumns[];

        moRelatedUuidPane = new STablePane(miClient);
        jpRelatedUUID.add(moRelatedUuidPane, BorderLayout.CENTER);
        
        i = 0;
        tableColumns = new STableColumnForm[3];
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo de relación", 50);
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Descripción", 200);
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "UUID", 300);
        
        for (i = 0; i < tableColumns.length; i++) {
            moRelatedUuidPane.addTableColumn(tableColumns[i]);
        }

        moFieldCfdiRelationType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbCfdiRelationType, jlCfdiRelationType);
        moFieldCfdiCfdiRelated = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfCfdiCfdiRelated, jlCfdiCfdiRelated);
        moFieldCfdiCfdiRelated.setLengthMax(3700); // (36 + 1) * 100: longitud UUID = 36 + 1 coma, aunque hay espacio de más para una última coma innecesaria
        moFieldCfdiCfdiRelated.setPickerButton(jbCfdiCfdiRelated);
        
        mvFields = new Vector<>();
        mvFields.add(moFieldCfdiRelationType);
        mvFields.add(moFieldCfdiCfdiRelated);
        
        jbCfdiCfdiRelated.addActionListener(this);
        jbAddRow.addActionListener(this);
        jbDelRow.addActionListener(this);
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        
        formReset();
        formRefreshCatalogues();
        
        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), action, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbIsFirstTime) {
            mbIsFirstTime = false;
            jcbCfdiRelationType.requestFocusInWindow();
        }
    }

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void actionCfdiCfdiRelated() {
        if (moDialogPickerDpsForCfdiRelated == null) {
            moDialogPickerDpsForCfdiRelated = new SDialogPickerDps(miClient, SDataConstants.TRN_DPS);
        }
        
        moDialogPickerDpsForCfdiRelated.formReset();
        moDialogPickerDpsForCfdiRelated.setFilterKey(new Object[] { bIsDpsInvoice ? manDpsClassKey : manDpsClassPreviousKey, new int[] { moDps.getFkBizPartnerId_r()} });
        moDialogPickerDpsForCfdiRelated.formRefreshOptionPane();
        moDialogPickerDpsForCfdiRelated.setFormVisible(true);

        if (moDialogPickerDpsForCfdiRelated.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moDialogPickerDpsForCfdiRelated.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
            if (dps.getDbmsDataCfd() == null) {
                miClient.showMsgBoxWarning("El documento " + dps.getDpsNumber() + " no cuenta con CFD.");
            }
            else if (dps.getDbmsDataCfd().getUuid().isEmpty()) {
                miClient.showMsgBoxWarning("El documento " + dps.getDpsNumber() + " no cuenta con UUID.");
            }
            else {
                String cfdi = moFieldCfdiCfdiRelated.getString();
                if (cfdi.isEmpty()) {
                    manFirstRelatedDpsKey = (int[]) dps.getPrimaryKey();
                    msRelatedDpsUuid = dps.getDbmsDataCfd().getUuid();
                }
                moFieldCfdiCfdiRelated.setString((cfdi.isEmpty() ? "" : cfdi + ",") + dps.getDbmsDataCfd().getUuid());
            }
        }
    }
    
    private void renderTable() {
        if (moRelatedUuidPane.getTable().getRowCount() <= 0 && moTrnRelatedDocument != null) {
            for (SRowRelatedDocument row : moTrnRelatedDocument.getRelatedDocuments()) {
                SRowRelatedDocument rowRelated = row;
                moRelatedUuidPane.addTableRow(rowRelated);
            }
            moRelatedUuidPane.renderTableRows();
            moRelatedUuidPane.setTableRowSelection(0);
            moFieldCfdiRelationType.resetField();
            moFieldCfdiCfdiRelated.resetField();
        }
    }
    
    private void actionAddRow() {
        SFormValidation validate = validateRow();
        if (!validate.getIsError()) {
            SRowRelatedDocument row = new SRowRelatedDocument((String) moFieldCfdiRelationType.getKey(), moFieldCfdiCfdiRelated.getString());
            row.setFirstDocKey(manFirstRelatedDpsKey);
            row.setFirstDocUuid(msRelatedDpsUuid);
            moRelatedUuidPane.addTableRow(row);
            moRelatedUuidPane.renderTableRows();
            moRelatedUuidPane.setTableRowSelection(0);
            moFieldCfdiRelationType.resetField();
            moFieldCfdiCfdiRelated.resetField();
        }
        else {
            miClient.showMsgBoxInformation(validate.getMessage());
        }
    }
    
    private void actionDelRow() {
        moRelatedUuidPane.removeTableRow(moRelatedUuidPane.getTable().getSelectedRow());
        moRelatedUuidPane.renderTableRows();
        moRelatedUuidPane.setTableRowSelection(0);
    }
    
    private void actionOk() {
        SFormValidation validation = formValidate();
        
        if (validation.getIsError()) {
            miClient.showMsgBoxInformation(validation.getMessage());
        }
        else {
            try {
                SRowRelatedDocument firstRow = (SRowRelatedDocument) moRelatedUuidPane.getTableRow(0);
                if (firstRow.getFirstDocKey() == null) {
                    SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_CFD, 
                            new int[] { SCfdUtils.getCfdIdByUuid(miClient, firstRow.getDocUuids().trim().split(",")[0]) }, SLibConstants.EXEC_MODE_VERBOSE);
                    firstRow.setFirstDocKey(new int[] { cfd.getFkDpsYearId_n(), cfd.getFkDpsDocId_n() });
                    firstRow.setFirstDocUuid(cfd.getUuid());
                }
            }
            catch(Exception e) {}
            moTrnRelatedDocument = new STrnCfdRelated();
            for (STableRow row : moRelatedUuidPane.getTableModel().getTableRows()) {
                moTrnRelatedDocument.getRelatedDocuments().add((SRowRelatedDocument) row);
            }
            mnFormResult = SLibConstants.FORM_RESULT_OK;
            setVisible(false);
        }
    }
    
    private SFormValidation validateRow() {
        SFormValidation validation = new SFormValidation();
        for (SFormField mvField : mvFields) {
            if (!((erp.lib.form.SFormField) mvField).validateField()) {
                validation.setIsError(true);
                validation.setComponent(((erp.lib.form.SFormField) mvField).getComponent());
                break;
            }
        }
        if (!validation.getIsError()) {
            String[] uuids = SLibUtils.textExplode(moFieldCfdiCfdiRelated.getString(), ",");
            for (String uuid : uuids) {
                if (uuid.trim().length() != DCfdVer3Consts.LEN_UUID) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlCfdiCfdiRelated.getText() + "':\n"
                           + "La longitud del UUID '" + uuid.trim() + "' debe ser de " + DCfdVer3Consts.LEN_UUID + " caracteres.");
                    validation.setComponent(jtfCfdiCfdiRelated);

                    break;
                }
            }
        }
        return validation;
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgCancelationType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton jbAddRow;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbCfdiCfdiRelated;
    private javax.swing.JButton jbDelRow;
    private javax.swing.JButton jbOk;
    private javax.swing.JComboBox<SFormComponentItem> jcbCfdiRelationType;
    private javax.swing.JLabel jlCfdiCfdiRelated;
    private javax.swing.JLabel jlCfdiCfdiRelatedHint;
    private javax.swing.JLabel jlCfdiRelationType;
    private javax.swing.JPanel jpRelatedUUID;
    private javax.swing.JTextField jtfCfdiCfdiRelated;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mbIsFirstTime = true;
        
        mvFields.stream().forEach((mvField) -> {
            ((erp.lib.form.SFormField) mvField).resetField();
        });

        moRelatedUuidPane.createTable(null);
        moRelatedUuidPane.clearTableRows();
        
        manDpsClassKey = new int[] { moDps.getFkDpsCategoryId(), moDps.getFkDpsClassId() }; 
        manDpsClassPreviousKey = new int[] { moDps.getFkDpsCategoryId(), moDps.getFkDpsClassId() - 1 }; 
        bIsDpsInvoice = SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_PUR_DOC, manDpsClassKey) ||
                SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_DOC, manDpsClassKey);
        
        moTrnRelatedDocument = null;
        
        manFirstRelatedDpsKey = null;
        msRelatedDpsUuid = "";
    }

    @Override
    public void formRefreshCatalogues() {
        SCfdXmlCatalogs catalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        catalogs.populateComboBox(jcbCfdiRelationType, SDataConstantsSys.TRNS_CFD_CAT_REL_TP, miClient.getSession().getSystemDate());
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();
        if (!validation.getIsError()) {
            if (moRelatedUuidPane.getTableGuiRowCount() <= 0) {
                validation.setMessage("Se debe de agregar al menos un tipo de relación a la tabla.");
                validation.setComponent(jcbCfdiRelationType);
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
        
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case 1:
                moTrnRelatedDocument = (STrnCfdRelated) value;
                renderTable();
                break;
            default:
        }
    }

    @Override
    public java.lang.Object getValue(int type) {
        Object value = null;
        switch (type) {
            case 1:
                value = moTrnRelatedDocument;
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

            if (button == jbCfdiCfdiRelated) {
                actionCfdiCfdiRelated();
            }
            else if (button == jbAddRow) {
                actionAddRow();
            } 
            else if (button == jbDelRow) {
                actionDelRow();
            }
            else if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            
        }
    }
}