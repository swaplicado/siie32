package erp.mod.trn.form;

import erp.mfin.data.SDataBookkeepingNumber;
import erp.mod.SModConsts;
import erp.mod.trn.db.SDbInventoryValuation;
import erp.mod.trn.db.STrnCostsUpdate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiFieldCalendar;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;

/**
 *
 * @author Sergio Flores
 */
public class SFormInventoryValuation extends SBeanForm implements ActionListener, ChangeListener, ItemListener {
    
    private SDbInventoryValuation moRegistry;
    private String[] masMonths;
    private String msFileCostsCsvPath;
    
    /**
     * Creates new form SFormInventoryValuation
     * @param client GUI client.
     * @param subtype Form subtype (SModConsts.TRNX_INV_VAL_PRC_CALC or SModConsts.TRNX_INV_VAL_COST_UPD).
     * @param title Form title.
     */
    public SFormInventoryValuation(SGuiClient client, int subtype, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.TRN_INV_VAL, subtype, title);
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

        bgCosts = new javax.swing.ButtonGroup();
        bgCutoff = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlYear = new javax.swing.JLabel();
        moCalYear = new sa.lib.gui.bean.SBeanFieldCalendarYear();
        jPanel4 = new javax.swing.JPanel();
        jlPeriod = new javax.swing.JLabel();
        moCalPeriod = new sa.lib.gui.bean.SBeanFieldCalendarMonth();
        jlPeriodMonth = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jlCostsOption = new javax.swing.JLabel();
        jrbCostsInventory = new javax.swing.JRadioButton();
        jrbCostsPurchase = new javax.swing.JRadioButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jlCostsHint = new javax.swing.JLabel();
        jpFileCostsCsv = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlFileCostsCsvPath = new javax.swing.JLabel();
        jtfFileCostsCsvPath = new javax.swing.JTextField();
        jbFileCostsCsvOpen = new javax.swing.JButton();
        jlFileCostsCsvPathHelp = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jlFileCostsCsvPathHint = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jlCuttofOption = new javax.swing.JLabel();
        jrbCutoffStart = new javax.swing.JRadioButton();
        jrbCutoffEnd = new javax.swing.JRadioButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jlCutoffHint = new javax.swing.JLabel();

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del cálculo:"));
        jPanel2.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlYear.setText("Año de cálculo:*");
        jlYear.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlYear);
        jPanel3.add(moCalYear);

        jPanel2.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPeriod.setText("Mes de cálculo:*");
        jlPeriod.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlPeriod);
        jPanel4.add(moCalPeriod);

        jlPeriodMonth.setText("mes");
        jlPeriodMonth.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlPeriodMonth);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCostsOption.setText("Costos a utilizar:*");
        jlCostsOption.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlCostsOption);

        bgCosts.add(jrbCostsInventory);
        jrbCostsInventory.setSelected(true);
        jrbCostsInventory.setText("Costos de inventarios");
        jrbCostsInventory.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel5.add(jrbCostsInventory);

        bgCosts.add(jrbCostsPurchase);
        jrbCostsPurchase.setText("Costos de compras");
        jrbCostsPurchase.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel5.add(jrbCostsPurchase);

        jPanel2.add(jPanel5);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel1.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jLabel1);

        jlCostsHint.setForeground(java.awt.SystemColor.textInactiveText);
        jlCostsHint.setText("Ayuda...");
        jlCostsHint.setPreferredSize(new java.awt.Dimension(425, 23));
        jPanel10.add(jlCostsHint);

        jPanel2.add(jPanel10);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jpFileCostsCsv.setBorder(javax.swing.BorderFactory.createTitledBorder("Archivo CSV con los costos unitarios requeridos:"));
        jpFileCostsCsv.setLayout(new java.awt.BorderLayout());

        jPanel12.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFileCostsCsvPath.setText("Archivo CSV:");
        jlFileCostsCsvPath.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlFileCostsCsvPath);

        jtfFileCostsCsvPath.setEditable(false);
        jtfFileCostsCsvPath.setText("archivo.csv");
        jtfFileCostsCsvPath.setPreferredSize(new java.awt.Dimension(355, 23));
        jPanel6.add(jtfFileCostsCsvPath);

        jbFileCostsCsvOpen.setText("Abrir");
        jbFileCostsCsvOpen.setMargin(new java.awt.Insets(2, 10, 2, 10));
        jbFileCostsCsvOpen.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel6.add(jbFileCostsCsvOpen);

        jlFileCostsCsvPathHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/gui/img/icon_view_info.png"))); // NOI18N
        jlFileCostsCsvPathHelp.setToolTipText("Formato CSV: código ítem, nombre ítem, símbolo unidad medida, costo unitario");
        jlFileCostsCsvPathHelp.setPreferredSize(new java.awt.Dimension(15, 23));
        jPanel6.add(jlFileCostsCsvPathHelp);

        jPanel12.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFileCostsCsvPathHint.setForeground(java.awt.Color.gray);
        jlFileCostsCsvPathHint.setText("Ruta archivo CSV...");
        jlFileCostsCsvPathHint.setPreferredSize(new java.awt.Dimension(535, 23));
        jPanel7.add(jlFileCostsCsvPathHint);

        jPanel12.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCuttofOption.setText("Actualizar al corte:*");
        jlCuttofOption.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlCuttofOption);

        bgCutoff.add(jrbCutoffStart);
        jrbCutoffStart.setSelected(true);
        jrbCutoffStart.setText("Costos iniciales del mes");
        jrbCutoffStart.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel8.add(jrbCutoffStart);

        bgCutoff.add(jrbCutoffEnd);
        jrbCutoffEnd.setText("Costos finales del mes");
        jrbCutoffEnd.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel8.add(jrbCutoffEnd);

        jPanel12.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel2.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jLabel2);

        jlCutoffHint.setForeground(java.awt.SystemColor.textInactiveText);
        jlCutoffHint.setText("Ayuda...");
        jlCutoffHint.setPreferredSize(new java.awt.Dimension(425, 23));
        jPanel9.add(jlCutoffHint);

        jPanel12.add(jPanel9);

        jpFileCostsCsv.add(jPanel12, java.awt.BorderLayout.NORTH);

        jPanel1.add(jpFileCostsCsv, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgCosts;
    private javax.swing.ButtonGroup bgCutoff;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbFileCostsCsvOpen;
    private javax.swing.JLabel jlCostsHint;
    private javax.swing.JLabel jlCostsOption;
    private javax.swing.JLabel jlCutoffHint;
    private javax.swing.JLabel jlCuttofOption;
    private javax.swing.JLabel jlFileCostsCsvPath;
    private javax.swing.JLabel jlFileCostsCsvPathHelp;
    private javax.swing.JLabel jlFileCostsCsvPathHint;
    private javax.swing.JLabel jlPeriod;
    private javax.swing.JLabel jlPeriodMonth;
    private javax.swing.JLabel jlYear;
    private javax.swing.JPanel jpFileCostsCsv;
    private javax.swing.JRadioButton jrbCostsInventory;
    private javax.swing.JRadioButton jrbCostsPurchase;
    private javax.swing.JRadioButton jrbCutoffEnd;
    private javax.swing.JRadioButton jrbCutoffStart;
    private javax.swing.JTextField jtfFileCostsCsvPath;
    private sa.lib.gui.bean.SBeanFieldCalendarMonth moCalPeriod;
    private sa.lib.gui.bean.SBeanFieldCalendarYear moCalYear;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 560, 350);
        
        moCalYear.setCalendarSettings(SGuiUtils.getLabelName(jlYear));
        moCalPeriod.setCalendarSettings(SGuiUtils.getLabelName(jlPeriod));
        
        moFields.addField(moCalYear);
        moFields.addField(moCalPeriod);
        moFields.setFormButton(jbSave);
        
        masMonths = SLibTimeUtils.createMonthsOfYearStd(Calendar.LONG);
    }
    
    /**
     * Show period's month.
     * Note that member masMonths must be instanciated before invoking this method.
     */
    private void showPeriodMonth() {
        if (moCalPeriod.getValue() >= SLibTimeConsts.MONTH_MIN && moCalPeriod.getValue() <= SLibTimeConsts.MONTH_MAX) {
            jlPeriodMonth.setText(masMonths[moCalPeriod.getValue() - 1]);
        }
        else {
            jlPeriodMonth.setText("");
        }
    }
    
    private void itemStateChangedCosts() {
        if (jrbCostsInventory.isSelected()) {
            jlCostsHint.setText("(Valores actuales de los costos de inventarios por insumo.)");
        }
        else if (jrbCostsPurchase.isSelected()) {
            jlCostsHint.setText("(Valores de los últimos costos de compras por lote de insumos.)");
        }
        else {
            jlCostsHint.setText("");
        }
    }
    
    private void itemStateChangedCutoff() {
        if (jrbCutoffStart.isSelected()) {
            jlCutoffHint.setText("(La actualización se realizará con corte al primer día del mes seleccionado.)");
        }
        else if (jrbCutoffEnd.isSelected()) {
            jlCutoffHint.setText("(La actualización se realizará con corte al último día del mes seleccionado.)");
        }
        else {
            jlCutoffHint.setText("");
        }
    }
    
    private void resetFileCostsCsvFields() {
        bgCosts.clearSelection();
        
        bgCutoff.clearSelection();
        msFileCostsCsvPath = "";
        jtfFileCostsCsvPath.setText("");
        jlFileCostsCsvPathHint.setText("");
        
        switch (mnFormSubtype) {
            case SModConsts.TRNX_INV_VAL_PRC_CALC:
                jlCostsOption.setEnabled(true);
                jrbCostsInventory.setEnabled(true);
                jrbCostsPurchase.setEnabled(true);
                
                jpFileCostsCsv.setEnabled(false);
                jlFileCostsCsvPath.setEnabled(false);
                jtfFileCostsCsvPath.setEnabled(false);
                jbFileCostsCsvOpen.setEnabled(false);
                jlFileCostsCsvPathHelp.setEnabled(false);
                jlFileCostsCsvPathHint.setEnabled(false);
                jlCuttofOption.setEnabled(false);
                jrbCutoffStart.setEnabled(false);
                jrbCutoffEnd.setEnabled(false);
                break;
                
            case SModConsts.TRNX_INV_VAL_UPD_COST:
                jlCostsOption.setEnabled(false);
                jrbCostsInventory.setEnabled(false);
                jrbCostsPurchase.setEnabled(false);
                
                jpFileCostsCsv.setEnabled(true);
                jlFileCostsCsvPath.setEnabled(true);
                jtfFileCostsCsvPath.setEnabled(true);
                jbFileCostsCsvOpen.setEnabled(true);
                jlFileCostsCsvPathHelp.setEnabled(true);
                jlFileCostsCsvPathHint.setEnabled(true);
                jlCuttofOption.setEnabled(false);
                jrbCutoffStart.setEnabled(true);
                jrbCutoffEnd.setEnabled(true);
                
                jlFileCostsCsvPathHint.setText("(Seleccionar archivo fuente CSV con los costos deseados.)");
                break;
                
            default:
                jlCostsOption.setEnabled(false);
                jrbCostsInventory.setEnabled(false);
                jrbCostsPurchase.setEnabled(false);
                
                jpFileCostsCsv.setEnabled(false);
                jlFileCostsCsvPath.setEnabled(false);
                jtfFileCostsCsvPath.setEnabled(false);
                jbFileCostsCsvOpen.setEnabled(false);
                jlFileCostsCsvPathHelp.setEnabled(false);
                jlFileCostsCsvPathHint.setEnabled(false);
                jlCuttofOption.setEnabled(false);
                jrbCutoffStart.setEnabled(false);
                jrbCutoffEnd.setEnabled(false);
                
                jbSave.setEnabled(false);
        }
        
        itemStateChangedCosts();
        itemStateChangedCutoff();
    }
    
    private void actionPerformedFileCostsCsvOpen() {
        if (miClient.getFileChooser().showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            msFileCostsCsvPath = miClient.getFileChooser().getSelectedFile().getAbsolutePath();
            
            jtfFileCostsCsvPath.setText(miClient.getFileChooser().getSelectedFile().getName());
            jtfFileCostsCsvPath.setToolTipText(jtfFileCostsCsvPath.getText());
            jtfFileCostsCsvPath.setCaretPosition(0);
            
            jlFileCostsCsvPathHint.setText(msFileCostsCsvPath);
            jlFileCostsCsvPathHint.setToolTipText(jlFileCostsCsvPathHint.getText());
        }
    }
    
    @Override
    public void addAllListeners() {
        jbFileCostsCsvOpen.addActionListener(this);
        moCalYear.addChangeListener(this);
        moCalPeriod.addChangeListener(this);
        jrbCostsInventory.addItemListener(this);
        jrbCostsPurchase.addItemListener(this);
        jrbCutoffStart.addItemListener(this);
        jrbCutoffEnd.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbFileCostsCsvOpen.removeActionListener(this);
        moCalYear.removeChangeListener(this);
        moCalPeriod.removeChangeListener(this);
        jrbCostsInventory.removeItemListener(this);
        jrbCostsPurchase.removeItemListener(this);
        jrbCutoffStart.removeItemListener(this);
        jrbCutoffEnd.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbInventoryValuation) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.setFkYearYearId(miClient.getSession().getCurrentYear());
            moRegistry.setFkYearPeriodId(SLibTimeUtils.digestMonth(miClient.getSession().getCurrentDate())[1]);
            
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moCalYear.setValue(moRegistry.getFkYearYearId());
        moCalPeriod.setValue(moRegistry.getFkYearPeriodId());
        showPeriodMonth(); // member masMonths must be instanciated before invoking this method
        
        setFormEditable(true);
        
        if (moRegistry.isRegistryNew()) {
            
        }
        else {
            
        }
        
        resetFileCostsCsvFields();
        
        addAllListeners();
    }

    @Override
    public SDbInventoryValuation getRegistry() throws Exception {
        SDbInventoryValuation registry = moRegistry.clone();

        if (registry.isRegistryNew()) {
            //registry.setPkInventoryValuationId(...);
        }

        registry.setFinished(false);
        //registry.setDeleted(...);
        registry.setFkYearYearId(moCalYear.getValue());
        registry.setFkYearPeriodId(moCalPeriod.getValue());
        
        if (jrbCostsInventory.isSelected()) {
            registry.setAuxCostsOption(SDbInventoryValuation.COSTS_OPTION_INVENTORY);
        }
        else if (jrbCostsPurchase.isSelected()) {
            registry.setAuxCostsOption(SDbInventoryValuation.COSTS_OPTION_PURCHASE);
        }

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        if (validation.isValid()) {
            switch (mnFormSubtype) {
                case SModConsts.TRNX_INV_VAL_PRC_CALC:
                    if (bgCosts.getSelection() == null) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jrbCostsInventory.getText()) + "' o '" + SGuiUtils.getLabelName(jrbCostsPurchase.getText()) + "'.");
                        validation.setComponent(jrbCostsInventory);
                    }
                    break;
                    
                case SModConsts.TRNX_INV_VAL_UPD_COST:
                    if (msFileCostsCsvPath.isEmpty()) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlFileCostsCsvPath) + "'.");
                        validation.setComponent(jbFileCostsCsvOpen);
                    }
                    else if (bgCutoff.getSelection() == null) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jrbCutoffStart.getText()) + "' o '" + SGuiUtils.getLabelName(jrbCutoffEnd.getText()) + "'.");
                        validation.setComponent(jrbCutoffStart);
                    }
                    break;
                    
                default:
            }
        }
        
        return validation;
    }
    
    @Override
    public void actionSave() {
        if (SGuiUtils.computeValidation(miClient, validateForm())) {
            switch (mnFormSubtype) {
                case SModConsts.TRNX_INV_VAL_PRC_CALC:
                    if (miClient.showMsgBoxConfirm(
                            "Se realizará la valuación de inventarios para " + masMonths[moCalPeriod.getValue() - 1] + " de " + moCalYear.getValue() + "."
                                    + "\nIMPORTANTE: ¡La valuación depende totalmente de la precisión de los costos actuales de insumos y productos en sistema!"
                                    + "\nLos costos actuales para el período solicitado debieron ser revisados previamente a todo detalle."
                                    + "\nSi es necesario, se puede optar por actualizar masivamente los costos de inventarios a partir de un archivo fuente CSV."
                                    + "\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        super.actionSave();
                    }
                    break;
                    
                case SModConsts.TRNX_INV_VAL_UPD_COST:
                    Date period = SLibTimeUtils.createDate(moCalYear.getValue(), moCalPeriod.getValue());
                    Date cutOff = jrbCutoffStart.isSelected() ? SLibTimeUtils.getBeginOfMonth(period) : SLibTimeUtils.getEndOfMonth(period);
                    
                    if (miClient.showMsgBoxConfirm(
                            "Se realizará la actualización de costos de inventarios para " + masMonths[moCalPeriod.getValue() - 1] + " de " + moCalYear.getValue() + " con corte al " + SLibUtils.DateFormatDate.format(cutOff) + "."
                                    + "\nIMPORTANTE: ¡Favor de proceder con precaución!"
                                    + "\n¡La afectación al valor de los inventarios mediante este proceso no se puede revertir!"
                                    + "\nFavor de conservar el número de procesamiento que se mostrará al final para cualquier referencia futura."
                                    + "\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        try {
                            // update costs, then close dialog:
                            STrnCostsUpdate costsUpdate = new STrnCostsUpdate(miClient.getSession(), moCalYear.getValue(), moCalPeriod.getValue(), jrbCutoffStart.isSelected() ? STrnCostsUpdate.MOMENT_START : STrnCostsUpdate.MOMENT_END, msFileCostsCsvPath);
                            SDataBookkeepingNumber bookkeepingNumber = costsUpdate.updateCosts();
                            
                            if (bookkeepingNumber != null) {
                                miClient.showMsgBoxInformation(SLibConsts.MSG_PROCESS_FINISHED
                                        + "\nFavor de conservar este número de procesamiento para cualquier referencia futura: "
                                        + bookkeepingNumber.getPkYearId() + "-" + bookkeepingNumber.getPkNumberId() + "."
                                        + "\n" + costsUpdate.getUpdateReport());
                            }
                            else {
                                miClient.showMsgBoxInformation(SLibConsts.MSG_PROCESS_FINISHED
                                        + "\nNo se realizó ninguna actualización a los costos de inventarios para " + masMonths[moCalPeriod.getValue() - 1] + " de " + moCalYear.getValue() + "."
                                        + "\nSi el archivo fuente CSV proporcionado contiene costos para actualizar, los costos actuales ya son iguales a ellos."
                                        + "\n" + costsUpdate.getUpdateReport());
                            }
                            
                            actionCancel(); // just close this form
                        }
                        catch (Exception e) {
                            SLibUtils.showException(this, e);
                        }
                    }
                    break;
                    
                default:
                    miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbFileCostsCsvOpen) {
                actionPerformedFileCostsCsvOpen();
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof SGuiFieldCalendar) {
            bgCutoff.clearSelection();
            showPeriodMonth();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JRadioButton) {
            JRadioButton radioButton = (JRadioButton) e.getSource();
            
            if (radioButton == jrbCostsInventory || radioButton == jrbCostsPurchase) {
                itemStateChangedCosts();
            }
            else if (radioButton == jrbCutoffStart || radioButton == jrbCutoffEnd) {
                itemStateChangedCutoff();
            }
        }
    }
}
