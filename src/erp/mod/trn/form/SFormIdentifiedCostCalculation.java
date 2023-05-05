package erp.mod.trn.form;

import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.trn.db.SDbIdentifiedCostCalculation;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;

/**
 *
 * @author Sergio Flores
 */
public class SFormIdentifiedCostCalculation extends SBeanForm implements ActionListener, PropertyChangeListener {
    
    private static final int MODE_CALC_PEND = 1;
    private static final int MODE_CALC_DONE = 2;
    
    private SDbIdentifiedCostCalculation moRegistry;
    protected String msSiieWebDatabase;
    protected SGridPaneForm moGridDpsRows;
    protected SGridPaneForm moGridDpsEntryRows;
    protected SGridPaneForm moGridDpsEntrySupplyLotRows;
    protected SGridPaneForm moGridLotRows;
    protected int mnMode;
    
    /**
     * Creates new form SFormIdentifiedCostCalculation
     * @param client GUI client.
     * @param title Form title.
     */
    public SFormIdentifiedCostCalculation(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, 0, 0, title);
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

        jpDirectCosts = new javax.swing.JPanel();
        jpSettings = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlStart = new javax.swing.JLabel();
        moDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel4 = new javax.swing.JPanel();
        jlEnd = new javax.swing.JLabel();
        moDateEnd = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel7 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        moBoolRecalculate = new sa.lib.gui.bean.SBeanFieldBoolean();
        jPanel8 = new javax.swing.JPanel();
        jbCalculate = new javax.swing.JButton();
        jbReset = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlFolio = new javax.swing.JLabel();
        jtfFolio = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jProgressBar = new javax.swing.JProgressBar();
        jpResults = new javax.swing.JPanel();
        jTabbedPane = new javax.swing.JTabbedPane();
        jpResultsDpsRows = new javax.swing.JPanel();
        jpResultsDpsEntryRows = new javax.swing.JPanel();
        jpResultsDpsEntrySupplyLotRows = new javax.swing.JPanel();
        jpResultsLotRows = new javax.swing.JPanel();
        jpResultsLog = new javax.swing.JPanel();
        jspCalculationLog = new javax.swing.JScrollPane();
        jtaCalculationLog = new javax.swing.JTextArea();

        jpDirectCosts.setLayout(new java.awt.BorderLayout());

        jpSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del cálculo:"));
        jpSettings.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlStart.setText("Fecha inicial:*");
        jlStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlStart);
        jPanel3.add(moDateStart);

        jPanel6.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlEnd.setText("Fecha final:*");
        jlEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlEnd);
        jPanel4.add(moDateEnd);

        jPanel6.add(jPanel4);

        jpSettings.add(jPanel6, java.awt.BorderLayout.WEST);

        jPanel7.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolRecalculate.setText("Recalcular costos identificados de insumos y productos anteriormente determinados");
        moBoolRecalculate.setPreferredSize(new java.awt.Dimension(500, 23));
        jPanel5.add(moBoolRecalculate);

        jPanel7.add(jPanel5);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbCalculate.setText("Calcular");
        jbCalculate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jbCalculate);

        jbReset.setText("Reiniciar");
        jbReset.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jbReset);

        jPanel7.add(jPanel8);

        jpSettings.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jlFolio.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlFolio.setText("Folio:");
        jlFolio.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jlFolio);

        jtfFolio.setEditable(false);
        jtfFolio.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfFolio.setText("0");
        jtfFolio.setFocusable(false);
        jtfFolio.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jtfFolio);

        jPanel1.add(jPanel2);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jProgressBar.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel9.add(jProgressBar);

        jPanel1.add(jPanel9);

        jpSettings.add(jPanel1, java.awt.BorderLayout.EAST);

        jpDirectCosts.add(jpSettings, java.awt.BorderLayout.PAGE_START);

        jpResults.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultados del cálculo:"));
        jpResults.setLayout(new java.awt.BorderLayout());

        jpResultsDpsRows.setLayout(new java.awt.BorderLayout());
        jTabbedPane.addTab("Costos documentos", jpResultsDpsRows);

        jpResultsDpsEntryRows.setLayout(new java.awt.BorderLayout());
        jTabbedPane.addTab("Costos partidas documentos", jpResultsDpsEntryRows);

        jpResultsDpsEntrySupplyLotRows.setLayout(new java.awt.BorderLayout());
        jTabbedPane.addTab("Costos surtidos partidas documentos", jpResultsDpsEntrySupplyLotRows);

        jpResultsLotRows.setLayout(new java.awt.BorderLayout());
        jTabbedPane.addTab("Costos unitarios lotes", jpResultsLotRows);

        jpResultsLog.setLayout(new java.awt.BorderLayout());

        jtaCalculationLog.setEditable(false);
        jtaCalculationLog.setBackground(java.awt.SystemColor.control);
        jtaCalculationLog.setColumns(20);
        jtaCalculationLog.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jtaCalculationLog.setRows(5);
        jspCalculationLog.setViewportView(jtaCalculationLog);

        jpResultsLog.add(jspCalculationLog, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Bitácora cálculo", jpResultsLog);

        jpResults.add(jTabbedPane, java.awt.BorderLayout.CENTER);

        jpDirectCosts.add(jpResults, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpDirectCosts, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JButton jbCalculate;
    private javax.swing.JButton jbReset;
    private javax.swing.JLabel jlEnd;
    private javax.swing.JLabel jlFolio;
    private javax.swing.JLabel jlStart;
    private javax.swing.JPanel jpDirectCosts;
    private javax.swing.JPanel jpResults;
    private javax.swing.JPanel jpResultsDpsEntryRows;
    private javax.swing.JPanel jpResultsDpsEntrySupplyLotRows;
    private javax.swing.JPanel jpResultsDpsRows;
    private javax.swing.JPanel jpResultsLog;
    private javax.swing.JPanel jpResultsLotRows;
    private javax.swing.JPanel jpSettings;
    private javax.swing.JScrollPane jspCalculationLog;
    private javax.swing.JTextArea jtaCalculationLog;
    private javax.swing.JTextField jtfFolio;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolRecalculate;
    private sa.lib.gui.bean.SBeanFieldDate moDateEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDateStart;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 650);
        
        moDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlStart), true);
        moDateEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlEnd), true);
        moBoolRecalculate.setBooleanSettings(moBoolRecalculate.getText(), false);
        
        moFields.addField(moDateStart);
        moFields.addField(moDateEnd);
        moFields.addField(moBoolRecalculate);
        moFields.setFormButton(jbCalculate);
        
        try {
            msSiieWebDatabase = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_SIIE_WEB_DB);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        moGridDpsRows = new SGridPaneForm(miClient, SModConsts.TRN_COST_IDENT_CALC, SModConsts.TRN_DPS, "Costos documentos") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, true, true);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Tipo doc"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "Folio doc"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha doc"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "Cliente"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Subtotal $ moneda"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Subtotal $ ML"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Costo $ ML"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Margen $ ML"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_PER_2D, "% margen"));
                
                return gridColumnsForm;
            }
        };

        jpResultsDpsRows.add(moGridDpsRows, BorderLayout.CENTER);
        
        moGridDpsEntryRows = new SGridPaneForm(miClient, SModConsts.TRN_COST_IDENT_CALC, SModConsts.TRN_DPS_ETY, "Costos partidas documentos") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, true, true);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Tipo doc"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "Folio doc"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha doc"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "Concepto clave"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Concepto"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Cantidad"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Subtotal $ moneda"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Subtotal $ ML"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Costo $ ML"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Margen $ ML"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_PER_2D, "% margen"));
                
                return gridColumnsForm;
            }
        };

        jpResultsDpsEntryRows.add(moGridDpsEntryRows, BorderLayout.CENTER);
        
        moGridDpsEntrySupplyLotRows = new SGridPaneForm(miClient, SModConsts.TRN_COST_IDENT_CALC, SModConsts.TRNX_INV_VAL_COST_QRY, "Costos surtidos partidas documentos") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, true, true);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Tipo doc"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "Folio doc"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha doc"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "Ítem código"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Ítem nombre"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Cantidad"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Subtotal $ ML"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Costo $ ML"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Margen $ ML"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Lote"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Expiración lote"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT_UNIT, "Costo unitario lote $ ML"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Tipo costo unitario"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Problema cálculo"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Problema lote"));
                
                return gridColumnsForm;
            }
        };

        jpResultsDpsEntrySupplyLotRows.add(moGridDpsEntrySupplyLotRows, BorderLayout.CENTER);
        
        moGridLotRows = new SGridPaneForm(miClient, SModConsts.TRN_COST_IDENT_CALC, SModConsts.TRN_LOT, "Costos unitarios lotes") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, true, true);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "Ítem código"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Ítem nombre"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Lote"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Expiración lote"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT_UNIT, "Costo unitario lote $ ML"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Tipo costo unitario"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Problema cálculo"));
                
                return gridColumnsForm;
            }
        };

        jpResultsLotRows.add(moGridLotRows, BorderLayout.CENTER);
    }
    
    @Override
    protected void windowActivated() {
        if (msSiieWebDatabase.isEmpty()) {
            miClient.showMsgBoxError("Se requiere especificar la base de datos de SIIE Web en la configuración de la empresa.");
            actionCancel();
        }
        else {
            super.windowActivated();
        }
    }
    
    private void enableFields(final boolean enable) {
        moDateStart.setEditable(enable);
        moDateEnd.setEditable(enable);
        moBoolRecalculate.setEditable(enable);
        jbCalculate.setEnabled(enable);
        jbReset.setEnabled(!enable);
        
        mnMode = enable ? MODE_CALC_PEND : MODE_CALC_DONE;
    }
    
    private void validateGridDps() throws Exception {
        if (moGridDpsRows.getTable().getRowCount() == 0) {
            Date start = moDateStart.getValue();
            Date end = moDateEnd.getValue();
            
            throw new Exception("No se encontraron surtidos de documentos "
                    + "del " + SLibUtils.DateFormatDate.format(start) + (SLibTimeUtils.isSameDate(start, end) ? "" : " al " + SLibUtils.DateFormatDate.format(end)) + ".");
        }
    }
    
    private void actionPerformedCalculate() {
        if (SGuiUtils.computeValidation(miClient, SGuiUtils.validateDateRange(moDateStart, moDateEnd))) {
            Date start = moDateStart.getValue();
            Date end = moDateEnd.getValue();

            if (miClient.showMsgBoxConfirm("Se realizará el cálculo de los costos identificados de surtidos de documentos de ventas "
                    + "del " + SLibUtils.DateFormatDate.format(start) + (SLibTimeUtils.isSameDate(start, end) ? "" : " al " + SLibUtils.DateFormatDate.format(end)) + "."
                    + "\nEl proceso puede demorar varios segundos."
                    + "\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                Calculator calculator = new Calculator();
                calculator.addPropertyChangeListener(this);
                calculator.execute();
            }
        }
    }
    
    private void actionPerformedReset() {
        moGridDpsRows.clearGridRows();
        moGridDpsEntryRows.clearGridRows();
        moGridDpsEntrySupplyLotRows.clearGridRows();
        moGridLotRows.clearGridRows();
        jtaCalculationLog.setText("");
        jTabbedPane.setSelectedIndex(0);

        enableFields(true);
        
        moDateStart.getComponent().requestFocusInWindow();
    }
    
    @Override
    public void addAllListeners() {
        jbCalculate.addActionListener(this);
        jbReset.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbCalculate.removeActionListener(this);
        jbReset.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
        
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbIdentifiedCostCalculation) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();
        
        actionPerformedReset();
        
        if (moRegistry.isRegistryNew()) {
            moRegistry.setDateStart(miClient.getSession().getCurrentDate());
            moRegistry.setDateEnd(miClient.getSession().getCurrentDate());
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        jtfFolio.setText("" + moRegistry.getPkCalculationId());
        moDateStart.setValue(moRegistry.getDateStart());
        moDateEnd.setValue(moRegistry.getDateEnd());
        moBoolRecalculate.setValue(moRegistry.isRecalculate());
        
        setFormEditable(true);
        
        if (moRegistry.isRegistryNew()) {
            enableFields(true);
        }
        else {
            enableFields(false);
        }
        
        addAllListeners();
    }

    @Override
    public SDbIdentifiedCostCalculation getRegistry() throws Exception {
        SDbIdentifiedCostCalculation registry = moRegistry.clone();

        if (registry.isRegistryNew()) {
            //registry.setPkCalculationId(...);
        }

        registry.setDateStart(moDateStart.getValue());
        registry.setDateEnd(moDateEnd.getValue());
        registry.setRecalculate(moBoolRecalculate.getValue());
        //registry.setDeleted(...);

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = null;
        
        if (mnMode == MODE_CALC_PEND) {
            validation = new SGuiValidation();
            validation.setMessage("Se debe realizar el cálculo de costos identificados.");
            validation.setComponent(jbCalculate);
        }
        else {
            validation = moFields.validateFields();

            if (validation.isValid()) {
                validation = SGuiUtils.validateDateRange(moDateStart, moDateEnd);

                if (validation.isValid()) {
                    try {
                        validateGridDps();
                    }
                    catch (Exception e) {
                        validation.setMessage(e.getMessage());
                    }
                }
            }
        }
        
        return validation;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbCalculate) {
                actionPerformedCalculate();
            }
            else if (button == jbReset) {
                actionPerformedReset();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("progress")) {
            int progress = (Integer) evt.getNewValue();

            if (progress > 0 && progress < 100) {
                jProgressBar.setIndeterminate(true);
            }
            else {
                jProgressBar.setIndeterminate(false);
            }
        }
    }
    
    class Calculator extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            try {
                if (SGuiUtils.computeValidation(miClient, SGuiUtils.validateDateRange(moDateStart, moDateEnd))) {
                    enableFields(false);
                    
                    setProgress(1); // start
                    jbReset.setEnabled(false);
                    jbSave.setEnabled(false);
                    
                    moRegistry.calculate(miClient.getSession(), moDateStart.getValue(), moDateEnd.getValue(), moBoolRecalculate.getValue(), msSiieWebDatabase);

                    moGridDpsRows.populateGrid(new Vector<>(moRegistry.getAuxIdentifiedCostDpsRows()));
                    moGridDpsEntryRows.populateGrid(new Vector<>(moRegistry.getAuxIdentifiedCostDpsEntryRows()));
                    moGridDpsEntrySupplyLotRows.populateGrid(new Vector<>(moRegistry.getAuxIdentifiedCostDpsEntrySupplyLotRows()));
                    moGridLotRows.populateGrid(new Vector<>(moRegistry.getAuxIdentifiedCostLotRows()));
                    jtaCalculationLog.setText(moRegistry.getAuxCalculationLog());

                    validateGridDps();

                    jbCancel.requestFocusInWindow();
                }
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
            
            return null;
        }
        
        @Override
        public void done() {
            setProgress(100); // end
            jbReset.setEnabled(true);
            jbSave.setEnabled(true);
        }
    }
}
