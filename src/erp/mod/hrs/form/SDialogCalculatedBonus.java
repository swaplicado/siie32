/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbAbsence;
import erp.mod.hrs.db.SRowBonus;
import erp.mod.hrs.link.utils.SUtilsJSON;
import erp.mod.hrs.utils.SEarnConfiguration;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
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
public class SDialogCalculatedBonus extends SBeanFormDialog {
    
    protected SDbAbsence moAbsence;
    private SGridPaneForm moGridBonusRows;
    private HashMap<Integer, ArrayList<SEarnConfiguration>> lBonusConfigRows;
    private ArrayList<SRowBonus> lGridRows;
    private String msStartDate;
    private String msEndDate;
    private String msCompanyKey;
    private String msBonus;
    private int mnBonusId;

    /**
     * Creates new form SDialogAbsenceMovesCardex
     * @param client
     * @param title
     */
    public SDialogCalculatedBonus(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.HRSX_IMPORT_CAP, SLibConsts.UNDEFINED, title);
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
        jPanel12 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moTextDateStart = new sa.lib.gui.bean.SBeanFieldText();
        jlDateEnd = new javax.swing.JLabel();
        moTextDateEnd = new sa.lib.gui.bean.SBeanFieldText();
        jlBonus = new javax.swing.JLabel();
        moTextBonus = new sa.lib.gui.bean.SBeanFieldText();
        jPanel6 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jlCutoffDay = new javax.swing.JLabel();
        moTextCutoffDay = new sa.lib.gui.bean.SBeanFieldText();
        jpPrepayroll = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Rango Checador"));
        jPanel4.setLayout(new java.awt.GridLayout(1, 1, 0, 5));

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial:");
        jlDateStart.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel10.add(jlDateStart);

        moTextDateStart.setEditable(false);
        moTextDateStart.setText("sBeanFieldText7");
        moTextDateStart.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel10.add(moTextDateStart);

        jlDateEnd.setText("Fecha final:");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jlDateEnd);

        moTextDateEnd.setEditable(false);
        moTextDateEnd.setText("sBeanFieldText7");
        moTextDateEnd.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel10.add(moTextDateEnd);

        jlBonus.setText("Bono a pagar:");
        jlBonus.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jlBonus);

        moTextBonus.setEditable(false);
        moTextBonus.setText("sBeanFieldText7");
        moTextBonus.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel10.add(moTextBonus);

        jPanel4.add(jPanel10);

        jPanel12.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Corte:"));
        jPanel6.setLayout(new java.awt.GridLayout(1, 1, 0, 5));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCutoffDay.setText("Días:");
        jlCutoffDay.setPreferredSize(new java.awt.Dimension(75, 23));
        jlCutoffDay.setRequestFocusEnabled(false);
        jPanel11.add(jlCutoffDay);

        moTextCutoffDay.setEditable(false);
        moTextCutoffDay.setText("sBeanFieldText1");
        jPanel11.add(moTextCutoffDay);

        jPanel6.add(jPanel11);

        jPanel12.add(jPanel6, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanel12, java.awt.BorderLayout.NORTH);

        jpPrepayroll.setBorder(javax.swing.BorderFactory.createTitledBorder("Bonos:"));
        jpPrepayroll.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jpPrepayroll, java.awt.BorderLayout.CENTER);
        jpPrepayroll.getAccessibleContext().setAccessibleName("Prenómina");

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        dispose();
        mnFormResult = SGuiConsts.FORM_RESULT_CANCEL;
    }//GEN-LAST:event_closeDialog
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel jlBonus;
    private javax.swing.JLabel jlCutoffDay;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JPanel jpPrepayroll;
    private sa.lib.gui.bean.SBeanFieldText moTextBonus;
    private sa.lib.gui.bean.SBeanFieldText moTextCutoffDay;
    private sa.lib.gui.bean.SBeanFieldText moTextDateEnd;
    private sa.lib.gui.bean.SBeanFieldText moTextDateStart;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 800, 500);
        
        jbSave.setText("Continuar");
        jbCancel.setEnabled(false);

        moTextDateStart.setTextSettings(SGuiUtils.getLabelName(jlDateStart.getText()), 25);
        moTextDateEnd.setTextSettings(SGuiUtils.getLabelName(jlDateEnd.getText()), 25);
        moTextCutoffDay.setTextSettings(SGuiUtils.getLabelName(jlCutoffDay.getText()), 25);

        moGridBonusRows = new SGridPaneForm(miClient, SModConsts.HRSX_ABS_MOV, SLibConsts.UNDEFINED, "Importación desde CAP") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<SGridColumnForm>();
                SGridColumnForm column = null;

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CO, "Num", 50));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "Empleado", 200));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Percepción", 100));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "T. Bono", 100));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Monto", 100));
                column = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Con bono", moGridBonusRows.getTable().getDefaultEditor(Boolean.class));
                column.setEditable(true);
                gridColumnsForm.add(column);
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Comentarios", 200));

                return gridColumnsForm;
            }
        };

        jpPrepayroll.add(moGridBonusRows, BorderLayout.CENTER);
        
        moTextDateStart.setEditable(false);
        moTextDateEnd.setEditable(false);
        moTextCutoffDay.setEditable(false);
        
        reloadCatalogues();
        addAllListeners();
    }

    @SuppressWarnings("unchecked")
    private void showImportations() {
        Vector<SGridRow> rows = new Vector<>();
        lGridRows = new ArrayList();
        
        try {
            for (Map.Entry<Integer, ArrayList<SEarnConfiguration>> entry : lBonusConfigRows.entrySet()) {
                ArrayList<SEarnConfiguration> configs = entry.getValue();
                
                for (SEarnConfiguration sEarnConfiguration : configs) {
                    SRowBonus row = new SRowBonus();
                    
                    row.setEmployeeId(entry.getKey());
                    row.setNumEmployee(sEarnConfiguration.getNumEmployee());
                    row.setEmployee(sEarnConfiguration.getEmployee());
                    row.setEarning(sEarnConfiguration.getEarning());
                    row.setBonus(sEarnConfiguration.getBonus());
                    row.setAmount(sEarnConfiguration.getAmount());
                    row.setHasBonus(sEarnConfiguration.isHasWon() > 0d);
                    row.setComments(sEarnConfiguration.getComments());
                    
                    rows.add(row);
                    lGridRows.add(row);
                }
            }

            moGridBonusRows.populateGrid(rows);
            moGridBonusRows.clearSortKeys();
            moGridBonusRows.setSelectedGridRow(0);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    public void initView() {
        showImportations();
    }

    public ArrayList<SRowBonus> getlGridRows() {
        return lGridRows;
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
            case SModConsts.HRSX_IMPORT_CAP:
                moAbsence = (SDbAbsence) value;
                initView();
                break;
            default:
                break;
        }
    }

    @Override
    public Object getValue(final int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setlReceiptRows(HashMap<Integer, ArrayList<SEarnConfiguration>> lBcRows) {
        this.lBonusConfigRows = lBcRows;
    }

    public void setStartDate(String msStartDate) {
        this.msStartDate = msStartDate;
        moTextDateStart.setText(msStartDate);
    }

    public void setEndDate(String msEndDate) {
        this.msEndDate = msEndDate;
        moTextDateEnd.setText(msEndDate);
    }

    public void setCompanyKey(String msCompanyKey) {
        this.msCompanyKey = msCompanyKey;
    }

    public void setBonus(String msBonus) {
        this.msBonus = msBonus;
        moTextBonus.setValue(this.msBonus);
    }
    
    public void setBonusId(int mnBonusId) {
        this.mnBonusId = mnBonusId;
    }
    
    public void setCutOffDay(int day) {
        String sDay = "";
        switch (day) {
            case 1:
                sDay = "Domingo";
                break;
            case 2:
                sDay = "Lunes";
                break;
            case 3:
                sDay = "Martes";
                break;
            case 4:
                sDay = "Miércoles";
                break;
            case 5:
                sDay = "Jueves";
                break;
            case 6:
                sDay = "Viernes";
                break;
            case 7:
                sDay = "Sábado";
                break;
        }
        
        moTextCutoffDay.setValue(sDay);
    }
    
    @Override
    public void windowClosed() {
    }
    
    @Override
    public void actionSave() {
        if (jbSave.isEnabled()) {
            if (SGuiUtils.computeValidation(miClient, validateForm())) {
                boolean save = true;
                SDbRegistry registry = null;
                if (miFormOwner != null) {
                    try {
                        registry = getRegistry();
                        if (registry != null) {
                            if (registry.getFormAction() == SGuiConsts.FORM_ACTION_NEW) {
                                save = miFormOwner.validateRegistryNew(registry);
                            }
                            else {
                                save = miFormOwner.validateRegistryEdit(registry);
                            }
                        }
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
                if (save) {
                    List<String> dataLines = new ArrayList<>();
                    
                    for (Map.Entry<Integer, ArrayList<SEarnConfiguration>> entry : lBonusConfigRows.entrySet()) {
                        Integer idEmployee = entry.getKey();
                        ArrayList<SEarnConfiguration> earnCfgs = entry.getValue();
                        
                        for (SEarnConfiguration oEarnCfg : earnCfgs) {
                            if (oEarnCfg.getIdBonus() == this.mnBonusId) {
                                dataLines.add(idEmployee + "," +
                                        oEarnCfg.getNumEmployee() + "," +
                                        oEarnCfg.getEmployee().replaceAll(",", "") + "," +
                                        oEarnCfg.getEarning() + "," +
                                        oEarnCfg.getBonus()+ "," +
                                        oEarnCfg.getAmount()+ "," +
                                        oEarnCfg.getComments().replaceAll(",", "")
                                                );
                                break;
                            }
                        }
                    }

                    String fileHeader = "idEmpleado,Num,Empleado,Percepcion,Bono,Monto,Comentarios";

                    SUtilsJSON.writeCSV(msStartDate, msEndDate, dataLines, fileHeader, msCompanyKey, SUtilsJSON.VOUCHER);
                    
                    mnFormResult = SGuiConsts.FORM_RESULT_OK;
                    dispose();
                }
            }
        }
    }
}
