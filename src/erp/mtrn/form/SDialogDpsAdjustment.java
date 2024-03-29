/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogDpsAdjustment.java
 *
 * Created on 22/09/2009, 04:24:20 PM
 */

package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsDpsAdjustment;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SDataEntryDpsDpsAdjustment;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 *
 * @author Sergio Flores
 */
public class SDialogDpsAdjustment extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private static final int COL_QTY_NET = 7;
    private static final int COL_QTY_TO_RETURN = 8;
    private static final int COL_TOT_NET = 16;
    private static final int COL_AMT_TO_RETURN = 17;
    private static final int COL_AMT_TO_DISCOUNT = 18;

    private erp.client.SClientInterface miClient;
    private int mnOptionType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private erp.lib.table.STablePane moTablePane;

    private int[] manParamDpsAdjustmentKey;
    private int[] manParamDpsAdjustmentSubtypeKey;
    private erp.mtrn.data.SDataDps moParamDps;
    private erp.mtrn.form.SPanelDps moPanelDps;

    /** Creates new form SDialogDpsAdjustment
     * @param client */
    public SDialogDpsAdjustment(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnOptionType = SDataConstants.TRNX_DPS_ADJ;
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

        jpDps = new javax.swing.JPanel();
        jlPanelDps = new javax.swing.JLabel();
        jpOptions = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtfAdjustmentType = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jbSetEverything = new javax.swing.JButton();
        jbSetNothing = new javax.swing.JButton();
        jbCalculate = new javax.swing.JButton();
        jpControls = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ajuste de documento de compras-ventas");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpDps.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpDps.setLayout(new java.awt.BorderLayout());

        jlPanelDps.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlPanelDps.setText("[Panel de documento de compras-ventas]");
        jlPanelDps.setPreferredSize(new java.awt.Dimension(100, 200));
        jpDps.add(jlPanelDps, java.awt.BorderLayout.NORTH);

        jpOptions.setBorder(javax.swing.BorderFactory.createTitledBorder("Partidas del documento disponibles para ajuste:"));
        jpOptions.setLayout(new java.awt.BorderLayout());

        jPanel17.setLayout(new java.awt.BorderLayout());

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jLabel1.setText("Tipo de ajuste:");
        jLabel1.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel13.add(jLabel1);

        jtfAdjustmentType.setEditable(false);
        jtfAdjustmentType.setText("ADJUSTMENT TYPE");
        jtfAdjustmentType.setFocusable(false);
        jtfAdjustmentType.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel13.add(jtfAdjustmentType);

        jPanel17.add(jPanel13, java.awt.BorderLayout.WEST);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbSetEverything.setText("Todo");
        jbSetEverything.setToolTipText("Ajustar todo");
        jbSetEverything.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel18.add(jbSetEverything);

        jbSetNothing.setText("Limpiar");
        jbSetNothing.setToolTipText("Limpiar todo");
        jbSetNothing.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel18.add(jbSetNothing);

        jbCalculate.setText("Calcular");
        jbCalculate.setToolTipText("Calcular todo");
        jbCalculate.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel18.add(jbCalculate);

        jPanel17.add(jPanel18, java.awt.BorderLayout.CENTER);

        jpOptions.add(jPanel17, java.awt.BorderLayout.NORTH);

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

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-900)/2, (screenSize.height-600)/2, 900, 600);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        int i = 0;
        STableColumnForm[] columns = null;

        moTablePane = new STablePane(miClient);
        jpOptions.add(moTablePane, BorderLayout.CENTER);

        columns = new STableColumnForm[20];
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "#", STableConstants.WIDTH_NUM_TINYINT);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Clave", STableConstants.WIDTH_ITEM_KEY);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Concepto", STableConstants.WIDTH_ITEM);
        columns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cant.", STableConstants.WIDTH_QUANTITY);
        columns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        columns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cant. dev.", STableConstants.WIDTH_QUANTITY);
        columns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        columns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Dev. actual", STableConstants.WIDTH_QUANTITY);
        columns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        columns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cant. neta", STableConstants.WIDTH_QUANTITY);
        columns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        columns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "A devolver", STableConstants.WIDTH_QUANTITY);
        columns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Total mon $", STableConstants.WIDTH_VALUE);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Dev. mon $", STableConstants.WIDTH_VALUE);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Dev. actual mon $", STableConstants.WIDTH_VALUE);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Desc. mon $", STableConstants.WIDTH_VALUE);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Desc. actual mon $", STableConstants.WIDTH_VALUE);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Total neto mon $", STableConstants.WIDTH_VALUE);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "A devolver mon $", STableConstants.WIDTH_VALUE);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "A descontar mon $", STableConstants.WIDTH_VALUE);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Moneda", STableConstants.WIDTH_CURRENCY_KEY);

        for (i = 0; i < columns.length; i++) {
            moTablePane.addTableColumn(columns[i]);
        }

        moPanelDps = new SPanelDps(miClient, "de origen");
        jpDps.remove(jlPanelDps);
        jpDps.add(moPanelDps, BorderLayout.NORTH);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbSetEverything.addActionListener(this);
        jbSetNothing.addActionListener(this);
        jbCalculate.addActionListener(this);

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
            moTablePane.getTable().requestFocus();
        }
    }

    private boolean isAdjustmentReturn() {
        return manParamDpsAdjustmentSubtypeKey[0] == SDataConstantsSys.TRNS_TP_DPS_ADJ_RET;
    }

    private void renderDpsEntries() {
        double qtyRet = 0;
        double qtyRetActual = 0;
        double amtRet = 0;
        double amtRetActual = 0;
        double amtDisc = 0;
        double amtDiscActual = 0;

        moTablePane.createTable();
        moTablePane.clearTableRows();

        moTablePane.getTableColumn(COL_QTY_TO_RETURN).setEditable(false);
        moTablePane.getTableColumn(COL_AMT_TO_RETURN).setEditable(false);
        moTablePane.getTableColumn(COL_AMT_TO_DISCOUNT).setEditable(false);

        // Render original (being adjusted) document remaining entries:

        if (moParamDps != null) {
            for (SDataDpsEntry sourceEntry : moParamDps.getDbmsDpsEntries()) {
                if (!sourceEntry.getIsDeleted()) {
                    SDataEntryDpsDpsAdjustment entryAdjustment = null;

                    qtyRet = 0;
                    qtyRetActual = 0;
                    amtRet = 0;
                    amtRetActual = 0;
                    amtDisc = 0;
                    amtDiscActual = 0;

                    for (SDataDpsDpsAdjustment adjustment : sourceEntry.getDbmsDpsAdjustmentsAsDps()) {
                        if (SLibUtilities.compareKeys(manParamDpsAdjustmentKey, adjustment.getDbmsDpsAdjustmentKey())) {
                            // Current adjustment movements:

                            if (adjustment.getOriginalQuantity() != 0d) {
                                qtyRetActual += adjustment.getOriginalQuantity();
                                amtRetActual += adjustment.getValueCy();
                            }
                            else {
                                amtDiscActual += adjustment.getValueCy();
                            }
                        }
                        else {
                            // Previously saved adjustment movements:

                            if (!adjustment.getDbmsIsDpsAdjustmentDeleted() && !adjustment.getDbmsIsDpsAdjustmentEntryDeleted() &&
                                adjustment.getDbmsFkDpsAdjustmentStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED) {

                                if (adjustment.getOriginalQuantity() != 0d) {
                                    qtyRet += adjustment.getOriginalQuantity();
                                    amtRet += adjustment.getValueCy();
                                }
                                else {
                                    amtDisc += adjustment.getValueCy();
                                }
                            }
                        }
                    }

                    entryAdjustment = new SDataEntryDpsDpsAdjustment();
                    entryAdjustment.setPkYearId(sourceEntry.getPkYearId());
                    entryAdjustment.setPkDocId(sourceEntry.getPkDocId());
                    entryAdjustment.setPkEntryId(sourceEntry.getPkEntryId());
                    entryAdjustment.setSortingPosition(sourceEntry.getSortingPosition());
                    entryAdjustment.setConceptKey(sourceEntry.getConceptKey());
                    entryAdjustment.setConcept(sourceEntry.getConcept());
                    entryAdjustment.setQuantity(sourceEntry.getOriginalQuantity());
                    entryAdjustment.setQuantityReturned(qtyRet);
                    entryAdjustment.setQuantityReturnedActual(qtyRetActual);
                    entryAdjustment.setUnitSymbol(sourceEntry.getDbmsOriginalUnitSymbol());
                    entryAdjustment.setTotalCy(sourceEntry.getTotalCy_r());
                    entryAdjustment.setAmountReturned(amtRet);
                    entryAdjustment.setAmountReturnedActual(amtRetActual);
                    entryAdjustment.setAmountDiscounted(amtDisc);
                    entryAdjustment.setAmountDiscountedActual(amtDiscActual);
                    entryAdjustment.setCurrencyKey(moParamDps.getDbmsCurrencyKey());
                    entryAdjustment.prepareTableRow();

                    moTablePane.addTableRow(entryAdjustment);
                }
            }

            moTablePane.renderTableRows();
            moTablePane.setTableRowSelection(0);
        }
    }

    private void updateAdjustment() {
        SDataEntryDpsDpsAdjustment entry = null;

        for (int i = 0; i < moTablePane.getTableGuiRowCount(); i++) {
            entry = (SDataEntryDpsDpsAdjustment) moTablePane.getTableRow(i);
            entry.setQuantityToReturn(entry.getValues().get(COL_QTY_TO_RETURN) == null ? 0d : ((Number) entry.getValues().get(COL_QTY_TO_RETURN)).doubleValue());
            entry.setAmountToReturn(entry.getValues().get(COL_AMT_TO_RETURN) == null ? 0d : ((Number) entry.getValues().get(COL_AMT_TO_RETURN)).doubleValue());
            entry.setAmountToDiscount(entry.getValues().get(COL_AMT_TO_DISCOUNT) == null ? 0d : ((Number) entry.getValues().get(COL_AMT_TO_DISCOUNT)).doubleValue());
        }
    }
    
    private void actionSetEverything() {
        int index = moTablePane.getTable().getSelectedRow();
        SDataEntryDpsDpsAdjustment entry = null;

        for (int i = 0; i < moTablePane.getTableGuiRowCount(); i++) {
            entry = (SDataEntryDpsDpsAdjustment) moTablePane.getTableRow(i);

            if (isAdjustmentReturn()) {
                entry.setQuantityToReturn(entry.getQuantityNet());
                entry.setAmountToReturn(entry.getTotalNet());
            }
            else {
                entry.setAmountToDiscount(entry.getTotalNet());
            }

            entry.prepareTableRow();
        }

        moTablePane.renderTableRows();
        moTablePane.getTable().setRowSelectionInterval(index, index);
    }

    private void actionSetNothing() {
        int index = moTablePane.getTable().getSelectedRow();
        SDataEntryDpsDpsAdjustment entry = null;

        for (int i = 0; i < moTablePane.getTableGuiRowCount(); i++) {
            entry = (SDataEntryDpsDpsAdjustment) moTablePane.getTableRow(i);
            entry.setQuantityToReturn(0d);
            entry.setAmountToReturn(0d);
            entry.setAmountToDiscount(0d);
            entry.prepareTableRow();
        }

        moTablePane.renderTableRows();
        moTablePane.getTable().setRowSelectionInterval(index, index);
    }

    private void actionCalculate() {
        if (isAdjustmentReturn()) {
            int index = moTablePane.getTable().getSelectedRow();
            SDataEntryDpsDpsAdjustment entry = null;

            updateAdjustment();

            for (int i = 0; i < moTablePane.getTableGuiRowCount(); i++) {
                entry = (SDataEntryDpsDpsAdjustment) moTablePane.getTableRow(i);

                if (entry.getQuantity() != 0) {
                    entry.setAmountToReturn(SLibUtilities.round(entry.getQuantityToReturn() * (entry.getTotalCy() / entry.getQuantity()), miClient.getSessionXXX().getParamsErp().getDecimalsValue()));
                }

                entry.prepareTableRow();
            }

            moTablePane.renderTableRows();
            moTablePane.getTable().setRowSelectionInterval(index, index);
        }
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JButton jbCalculate;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbSetEverything;
    private javax.swing.JButton jbSetNothing;
    private javax.swing.JLabel jlPanelDps;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpDps;
    private javax.swing.JPanel jpOptions;
    private javax.swing.JTextField jtfAdjustmentType;
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

        manParamDpsAdjustmentKey = null;
        manParamDpsAdjustmentSubtypeKey = null;
        moParamDps = null;
        moPanelDps.setDps(null, null);

        jtfAdjustmentType.setText("");

        renderDpsEntries();
    }

    @Override
    public void formRefreshCatalogues() {

    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        int rows = 0;
        SFormValidation validation = new SFormValidation();

        updateAdjustment();

        for (int i = 0; i < moTablePane.getTableGuiRowCount(); i++) {
            SDataEntryDpsDpsAdjustment entry = (SDataEntryDpsDpsAdjustment) moTablePane.getTableRow(i);
            
            if (isAdjustmentReturn()) {
                if (entry.getQuantityToReturn() > 0) {
                    if (entry.getQuantityToReturn() > entry.getQuantityNet()) {
                        validation.setMessage("En la partida # " + entry.getSortingPosition() + ", " +
                                "el valor de la columna '" + moTablePane.getTableColumn(COL_QTY_TO_RETURN).getColumnTitle() + "' " +
                                "(" + miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(entry.getQuantityToReturn()) + ") " +
                                "no puede ser mayor al del columna '" + moTablePane.getTableColumn(COL_QTY_NET).getColumnTitle() + "' " +
                                "(" + miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(entry.getQuantityNet()) + ").");
                        break;
                    }
                    else if (entry.getAmountToReturn() > entry.getTotalNet()) {
                        validation.setMessage("En la partida # " + entry.getSortingPosition() + ", " +
                                "el valor de la columna '" + moTablePane.getTableColumn(COL_AMT_TO_RETURN).getColumnTitle() + "' " +
                                "(" + miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(entry.getAmountToReturn()) + ") " +
                                "no puede ser mayor al del columna '" + moTablePane.getTableColumn(COL_TOT_NET).getColumnTitle() + "' " +
                                "(" + miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(entry.getTotalNet()) + ").");
                        break;
                    }
                    else if (entry.getAmountToReturn() == 0d &&
                            miClient.showMsgBoxConfirm("En la partida # " + entry.getSortingPosition() + ", " +
                            "¿es correcto que el valor de la columna '" + moTablePane.getTableColumn(COL_AMT_TO_RETURN).getColumnTitle() + "' " +
                            "sea igual a " + miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(0d) + "?") != JOptionPane.YES_OPTION) {
                        validation.setMessage("Se debe especificar un valor para la columna '" + moTablePane.getTableColumn(COL_TOT_NET).getColumnTitle() + "', " +
                                "en la partida # " + entry.getSortingPosition() + ". ");
                        break;
                    }
                    else {
                        rows++;
                    }
                }
            }
            else {
                if (entry.getAmountToDiscount() > 0) {
                    if (entry.getAmountToDiscount() > entry.getTotalNet()) {
                        validation.setMessage("En la partida # " + entry.getSortingPosition() + ", " +
                                "el valor de la columna '" + moTablePane.getTableColumn(COL_AMT_TO_DISCOUNT).getColumnTitle() + "' " +
                                "(" + miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(entry.getAmountToDiscount()) + ") " +
                                "no puede ser mayor al del columna '" + moTablePane.getTableColumn(COL_TOT_NET).getColumnTitle() + "' " +
                                "(" + miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(entry.getTotalNet()) + ").");
                        break;
                    }
                    else {
                        rows++;
                    }
                }
            }
        }

        if (!validation.getIsError()) {
            if (rows == 0) {
                validation.setMessage("Se debe especificar al menos una partida para el ajuste.");
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
            case SDataConstants.TRNX_DPS_SRC:
                moParamDps = (SDataDps) value;
                moPanelDps.setDps(moParamDps, null);
                break;

            case SDataConstants.TRNX_DPS_DES:
                manParamDpsAdjustmentKey = (int[]) value;
                renderDpsEntries();
                break;

            case SDataConstants.TRNS_STP_DPS_ADJ:
                manParamDpsAdjustmentSubtypeKey = (int[]) value;
                jtfAdjustmentType.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_TP_DPS_ADJ, manParamDpsAdjustmentSubtypeKey));

                switch (manParamDpsAdjustmentSubtypeKey[0]) {
                    case SDataConstantsSys.TRNS_TP_DPS_ADJ_RET:
                        jbCalculate.setEnabled(true);
                        moTablePane.getTableColumn(COL_QTY_TO_RETURN).setEditable(true);
                        moTablePane.getTableColumn(COL_AMT_TO_RETURN).setEditable(true);
                        break;
                    case SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC:
                        jbCalculate.setEnabled(false);
                        moTablePane.getTableColumn(COL_AMT_TO_DISCOUNT).setEditable(true);
                        break;
                    default:
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
                Vector<SDataEntryDpsDpsAdjustment> entries = new Vector<SDataEntryDpsDpsAdjustment>();
                for (int i = 0; i < moTablePane.getTableGuiRowCount(); i++) {
                    SDataEntryDpsDpsAdjustment entry = (SDataEntryDpsDpsAdjustment) moTablePane.getTableRow(i);
                    if (entry.getQuantityToReturn() > 0d || entry.getAmountToReturn() > 0d || entry.getAmountToDiscount()> 0d ) {
                        entries.add(entry);
                    }
                }
                value = entries;
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

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbSetEverything) {
                actionSetEverything();
            }
            else if (button == jbSetNothing) {
                actionSetNothing();
            }
            else if (button == jbCalculate) {
                actionCalculate();
            }
        }
    }
}
