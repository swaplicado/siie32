/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.form;

import erp.cfd.SCfdXmlCatalogs;
import erp.data.SDataConstantsSys;
import erp.gui.session.SSessionCustom;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.log.db.SDbBolPerson;
import erp.mod.log.db.SDbBolTransportationModeExtra;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;
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
import sa.lib.gui.bean.SBeanForm;

/**
 *
 * @author Isabel Servín
 */
public class SFormBolTransportationModeExtra extends SBeanForm implements ActionListener, ItemListener {
    
    private ArrayList<SDbBolTransportationModeExtra> maTranspExtra;
    
    private SDbBolPerson moBolPerson;
    
    private SGridPaneForm moGridFigures;
    
    /**
     * Creates new form SFormMerchandise
     * @param client
     * @param title
     */
    public SFormBolTransportationModeExtra(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.LOG_BOL_TRANSP_MODE_EXTRA, 0, title);
        miClient = client;
        initComponents();
        initComponentsCustom();
    }
    
    private void enableComponents() {
        jbEdit.setEnabled(false);
        jbReadInfo.setEnabled(false);
        moKeyTpBolPerson.setEnabled(true);
        moKeyPerson.setEnabled(true);
        moTextFiscalId.setEnabled(false);
        moTextFiscalIdExt.setEnabled(false);
        moTextDriverLic.setEnabled(false);
        moKeyTransportationPart.setEnabled(false);

    }     
    
    private void actionTpBolPerson() {
        if (moKeyTpBolPerson.getSelectedIndex() > 0) {
            miClient.getSession().populateCatalogue(moKeyPerson, SModConsts.LOG_BOL_PERSON, moKeyTpBolPerson.getValue()[0], null);
            if (moKeyTpBolPerson.getValue()[0] == SModSysConsts.LOGS_TP_BOL_PERSON_NOT || moKeyTpBolPerson.getValue()[0] == SModSysConsts.LOGS_TP_BOL_PERSON_DRI) {
                moKeyTransportationPart.setEnabled(false);
            }
            else {
                moKeyTransportationPart.setEnabled(true);
            }
        }
        else {
            moKeyPerson.removeAllItems();
            moKeyTransportationPart.setEnabled(false);
        }
    }
    
    private void actionPerson() {
        try {
            if (moKeyPerson.getSelectedIndex() > 0) { 
                moBolPerson = new SDbBolPerson();
                moBolPerson.read(miClient.getSession(), moKeyPerson.getValue());
                moTextFiscalId.setValue(moBolPerson.getFiscalId());
                moTextDriverLic.setValue(moBolPerson.getDriverLicense());
            }
            else {
                moBolPerson = null;
                moTextFiscalId.setValue("");
                moTextDriverLic.setValue("");
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionAdd() {
        try {
            if (moKeyPerson.getSelectedIndex() > 0 ) {
                if ((moKeyTpBolPerson.getValue()[0] != SModSysConsts.LOGS_TP_BOL_PERSON_NOT && moKeyTpBolPerson.getValue()[0] != SModSysConsts.LOGS_TP_BOL_PERSON_DRI)
                        && moKeyTransportationPart.getSelectedIndex() <= 0) {
                    miClient.showMsgBoxWarning("Se debe seleccionar la parte del transporte.");
                }
                else {
                    SDbBolTransportationModeExtra extra = new SDbBolTransportationModeExtra();
                    extra.setTransportationPart(moKeyTransportationPart.getSelectedItem().getCode());
                    extra.setFkBillOfLadingPersonId(moKeyPerson.getValue()[0]);
                    extra.readBolPerson(miClient.getSession());
                    extra.readXtaTransportationPartDescription(miClient.getSession());
                    
                    maTranspExtra.add(extra);
                    
                    Vector<SGridRow> vRows = new Vector<>();
                    vRows.addAll(maTranspExtra);
                    moGridFigures.populateGrid(vRows);
                    moGridFigures.setSelectedGridRow(0);
                    
                    moKeyTpBolPerson.setSelectedIndex(0);
                    moKeyTransportationPart.setSelectedIndex(0);
                    moTextFiscalId.setValue("");
                    moTextDriverLic.setValue("");
                }
            }
            else {
                miClient.showMsgBoxWarning("Se debe seleccionar un nombre.");
            }
            
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionDelete() {
        moGridFigures.removeGridRow(moGridFigures.getTable().getSelectedRow());
        moGridFigures.renderGridRows();
        
        maTranspExtra = new ArrayList<>();
        moGridFigures.getModel().getGridRows().stream().forEach((row) -> {
            maTranspExtra.add((SDbBolTransportationModeExtra) row);
        });
    }
    
    
    private void populateTable() {
        Vector<SGridRow> vRows = new Vector<>();
        vRows.addAll(maTranspExtra);
        moGridFigures.populateGrid(vRows);

        moGridFigures.clearSortKeys();
        moGridFigures.setSelectedGridRow(0);   
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jlTpBolPerson = new javax.swing.JLabel();
        moKeyTpBolPerson = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel9 = new javax.swing.JPanel();
        jlPerson = new javax.swing.JLabel();
        moKeyPerson = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel13 = new javax.swing.JPanel();
        jlFiscalId = new javax.swing.JLabel();
        moTextFiscalId = new sa.lib.gui.bean.SBeanFieldText();
        jPanel5 = new javax.swing.JPanel();
        jlFiscalIdExt = new javax.swing.JLabel();
        moTextFiscalIdExt = new sa.lib.gui.bean.SBeanFieldText();
        jPanel12 = new javax.swing.JPanel();
        jlDriverLic = new javax.swing.JLabel();
        moTextDriverLic = new sa.lib.gui.bean.SBeanFieldText();
        jPanel14 = new javax.swing.JPanel();
        jlTransportationPart = new javax.swing.JLabel();
        moKeyTransportationPart = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel15 = new javax.swing.JPanel();
        jbAdd = new javax.swing.JButton();
        jbDelete = new javax.swing.JButton();
        jpFigures = new javax.swing.JPanel();

        setName(""); // NOI18N

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros figura transporte:"));
        jPanel17.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel6.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel7.setLayout(new java.awt.GridLayout(7, 0, 0, 5));

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTpBolPerson.setText("Tipo de figura*:");
        jlTpBolPerson.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlTpBolPerson);

        moKeyTpBolPerson.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel8.add(moKeyTpBolPerson);

        jPanel7.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPerson.setText("Nombre*:");
        jlPerson.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlPerson);

        moKeyPerson.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel9.add(moKeyPerson);

        jPanel7.add(jPanel9);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFiscalId.setText("RFC:");
        jlFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.add(jlFiscalId);
        jPanel13.add(moTextFiscalId);

        jPanel7.add(jPanel13);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFiscalIdExt.setText("RFC extranjero:");
        jlFiscalIdExt.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlFiscalIdExt);
        jPanel5.add(moTextFiscalIdExt);

        jPanel7.add(jPanel5);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDriverLic.setText("Licencia:");
        jlDriverLic.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlDriverLic);
        jPanel12.add(moTextDriverLic);

        jPanel7.add(jPanel12);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTransportationPart.setText("Parte transporte:");
        jlTransportationPart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlTransportationPart);

        moKeyTransportationPart.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel14.add(moKeyTransportationPart);

        jPanel7.add(jPanel14);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbAdd.setText("Agregar");
        jbAdd.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel15.add(jbAdd);

        jbDelete.setText("Eliminar");
        jbDelete.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel15.add(jbDelete);

        jPanel7.add(jPanel15);

        jPanel6.add(jPanel7, java.awt.BorderLayout.NORTH);

        jPanel17.add(jPanel6, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel17, java.awt.BorderLayout.NORTH);

        jpFigures.setBorder(javax.swing.BorderFactory.createTitledBorder("Figuras cargadas:"));
        jpFigures.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jpFigures, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbDelete;
    private javax.swing.JLabel jlDriverLic;
    private javax.swing.JLabel jlFiscalId;
    private javax.swing.JLabel jlFiscalIdExt;
    private javax.swing.JLabel jlPerson;
    private javax.swing.JLabel jlTpBolPerson;
    private javax.swing.JLabel jlTransportationPart;
    private javax.swing.JPanel jpFigures;
    private sa.lib.gui.bean.SBeanFieldKey moKeyPerson;
    private sa.lib.gui.bean.SBeanFieldKey moKeyTpBolPerson;
    private sa.lib.gui.bean.SBeanFieldKey moKeyTransportationPart;
    private sa.lib.gui.bean.SBeanFieldText moTextDriverLic;
    private sa.lib.gui.bean.SBeanFieldText moTextFiscalId;
    private sa.lib.gui.bean.SBeanFieldText moTextFiscalIdExt;
    // End of variables declaration//GEN-END:variables

    @SuppressWarnings("unchecked")
    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 720, 450);
        
        mnFormResult = SGuiConsts.FORM_RESULT_CANCEL;
        
        moKeyTpBolPerson.setKeySettings(miClient, SGuiUtils.getLabelName(jlTpBolPerson.getText()), true);
        moKeyPerson.setKeySettings(miClient, SGuiUtils.getLabelName(jlFiscalId.getText()), true);
        moTextFiscalId.setTextSettings(SGuiUtils.getLabelName(jlFiscalId.getText()), 0);
        moTextDriverLic.setTextSettings(SGuiUtils.getLabelName(jlDriverLic.getText()), 0);
        moKeyPerson.setKeySettings(miClient, SGuiUtils.getLabelName(jlFiscalId.getText()), true);
        
        moGridFigures = new SGridPaneForm(miClient, SModConsts.LOG_BOL_TRANSP_MODE_EXTRA, SLibConsts.UNDEFINED, "Figuras") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                int col = 0;
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();
                SGridColumnForm[] columns = new SGridColumnForm[6];
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Tipo de figura");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Nombre");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "RFC");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "RFC extranjero");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Licencia");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Parte transporte");
                
                gridColumnsForm.addAll(Arrays.asList((SGridColumnForm[]) columns));
                
                return gridColumnsForm;
            }
        };
        
        jpFigures.add(moGridFigures, BorderLayout.CENTER);
        
        removeAllListeners();
        reloadCatalogues();
        enableComponents();
        addAllListeners();
    }
    
    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyTpBolPerson, SModConsts.LOGS_TP_BOL_PERSON, SLibConsts.UNDEFINED, null);
        
        SCfdXmlCatalogs catalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        catalogs.populateComboBox(moKeyTransportationPart, SDataConstantsSys.TRNS_CFD_CAT_BOL_TRANSP_PART, miClient.getSession().getSystemDate());
        
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        return validation;
    }

    @Override
    public void addAllListeners() {
        moKeyTpBolPerson.addItemListener(this);
        moKeyPerson.addItemListener(this);
        jbAdd.addActionListener(this);
        jbDelete.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyTpBolPerson.removeItemListener(this);
        moKeyPerson.removeItemListener(this);
        jbAdd.removeActionListener(this);
        jbDelete.removeActionListener(this);
    }

    @Override
    public void setRegistry(SDbRegistry regitry) throws Exception { }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        return null;
    }
    
    @Override
    public void actionSave() {
        mnFormResult = SGuiConsts.FORM_RESULT_OK;
        dispose();
    }
    
    @Override
    public void actionCancel() {
        mnFormResult = SGuiConsts.FORM_RESULT_CANCEL;
        dispose();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case 1: 
                maTranspExtra = (ArrayList<SDbBolTransportationModeExtra>) value;
                populateTable();
                break;
            default:
        }
    }
    
    @Override
    public Object getValue(final int type) {
        Object value = null;
        switch (type) {
            case 1:
                maTranspExtra = new ArrayList<>();
                moGridFigures.getModel().getGridRows().stream().forEach((row) -> {
                    maTranspExtra.add((SDbBolTransportationModeExtra) row);
                });
                value = maTranspExtra;
            default:
        }
        return value;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
            if (button == jbAdd) {
                actionAdd();
            }
            else if (button == jbDelete) {
                actionDelete();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof javax.swing.JComboBox) {
            JComboBox comboBox = (JComboBox)  e.getSource();
            if (comboBox == moKeyTpBolPerson) {
                actionTpBolPerson();
            }
            else if (comboBox == moKeyPerson) {
                actionPerson();
            }
        }
    }    
}
