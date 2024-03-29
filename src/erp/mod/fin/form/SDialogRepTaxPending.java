/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.form;

import erp.client.SClientInterface;
import erp.form.SFormOptionPickerFunctionalArea;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.STrnFunctionalAreaUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import sa.lib.SLibTimeUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiFieldKeyGroup;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanDialogReport;

/**
 *
 * @author Sergio Flores
 */
public class SDialogRepTaxPending extends SBeanDialogReport implements ActionListener {
    
    private SGuiFieldKeyGroup moFieldKeyGroup;
    private SFormOptionPickerFunctionalArea moFunctionalAreaPicker;
    private String msFunctionalAreaIds;

    /**
     * Creates new form SDialogRepTaxPending
     */
    public SDialogRepTaxPending(SGuiClient client, int dpsCategoryId, String title) {
        setFormSettings(client, SModConsts.FINR_DPS_TAX_PEND, dpsCategoryId, title);
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

        jbgOrderBy = new javax.swing.ButtonGroup();
        jbgFilter = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        moDateDate = new sa.lib.gui.bean.SBeanFieldDate();
        moRadOrderByDoc = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadOrderByBizPartnerDoc = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        moRadFilterDocPayed = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadFilterDocPending = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadFilterDocAll = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel3 = new javax.swing.JPanel();
        jlFunctionalAreas = new javax.swing.JLabel();
        tfFunctionalAreas = new sa.lib.gui.bean.SBeanFieldText();
        jbEditFunctionalArea = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel1.setLayout(new java.awt.GridLayout(3, 0, 0, 5));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlDate.setText("Fecha de corte:*");
        jlDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel2.add(jlDate);
        jPanel2.add(moDateDate);

        jPanel1.add(jPanel2);

        jbgOrderBy.add(moRadOrderByDoc);
        moRadOrderByDoc.setText("Ordenar por folio de documento");
        jPanel1.add(moRadOrderByDoc);

        jbgOrderBy.add(moRadOrderByBizPartnerDoc);
        moRadOrderByBizPartnerDoc.setText("Ordenar por nombre de asociado de negocios y folio de documento");
        jPanel1.add(moRadOrderByBizPartnerDoc);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro del reporte:"));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jbgFilter.add(moRadFilterDocPayed);
        moRadFilterDocPayed.setText("Documentos sin saldo");
        jPanel5.add(moRadFilterDocPayed);

        jbgFilter.add(moRadFilterDocPending);
        moRadFilterDocPending.setText("Documentos con saldo");
        jPanel5.add(moRadFilterDocPending);

        jbgFilter.add(moRadFilterDocAll);
        moRadFilterDocAll.setText("Todos los documentos");
        jPanel5.add(moRadFilterDocAll);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlFunctionalAreas.setText("Áreas funcionales:");
        jlFunctionalAreas.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlFunctionalAreas);

        tfFunctionalAreas.setText("sBeanFieldText1");
        tfFunctionalAreas.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel3.add(tfFunctionalAreas);

        jbEditFunctionalArea.setText("...");
        jbEditFunctionalArea.setToolTipText("Seleccionar");
        jbEditFunctionalArea.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel3.add(jbEditFunctionalArea);

        jPanel5.add(jPanel3);

        jPanel4.add(jPanel5, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton jbEditFunctionalArea;
    private javax.swing.ButtonGroup jbgFilter;
    private javax.swing.ButtonGroup jbgOrderBy;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlFunctionalAreas;
    private sa.lib.gui.bean.SBeanFieldDate moDateDate;
    private sa.lib.gui.bean.SBeanFieldRadio moRadFilterDocAll;
    private sa.lib.gui.bean.SBeanFieldRadio moRadFilterDocPayed;
    private sa.lib.gui.bean.SBeanFieldRadio moRadFilterDocPending;
    private sa.lib.gui.bean.SBeanFieldRadio moRadOrderByBizPartnerDoc;
    private sa.lib.gui.bean.SBeanFieldRadio moRadOrderByDoc;
    private sa.lib.gui.bean.SBeanFieldText tfFunctionalAreas;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 560, 350);

        moDateDate.setDateSettings(miClient, SGuiUtils.getLabelName(jlDate), true);
        moRadOrderByDoc.setBooleanSettings(moRadOrderByDoc.getText(), false);
        moRadOrderByBizPartnerDoc.setBooleanSettings(moRadOrderByBizPartnerDoc.getText(), false);
        moRadFilterDocPayed.setBooleanSettings(moRadFilterDocPayed.getText(), false);
        moRadFilterDocPending.setBooleanSettings(moRadFilterDocPending.getText(), false);
        moRadFilterDocAll.setBooleanSettings(moRadFilterDocAll.getText(), false);

        moFields.addField(moDateDate);
        moFields.addField(moRadOrderByDoc);
        moFields.addField(moRadOrderByBizPartnerDoc);
        moFields.addField(moRadFilterDocPayed);
        moFields.addField(moRadFilterDocPending);
        moFields.addField(moRadFilterDocAll);

        moFields.setFormButton(jbPrint);
        
        moDateDate.setValue(SLibTimeUtils.getEndOfMonth(miClient.getSession().getCurrentDate()));
        
        switch (mnFormSubtype) {
            case SModSysConsts.TRNS_CT_DPS_PUR:
                moRadOrderByBizPartnerDoc.setSelected(true);
                break;
            case SModSysConsts.TRNS_CT_DPS_SAL:
                moRadOrderByDoc.setSelected(true);
                break;
            default:
        }
        
        moRadFilterDocPayed.setSelected(true);
        
        // áreas funcionales
        moFieldKeyGroup = new SGuiFieldKeyGroup(miClient);
        jbEditFunctionalArea.addActionListener(this);
        tfFunctionalAreas.setEditable(false);
        
        updateOptions();
        jbEditFunctionalArea.setEnabled(((SClientInterface) miClient).getSessionXXX().getParamsCompany().getIsFunctionalAreas());
        msFunctionalAreaIds = "";
        moFunctionalAreaPicker = new SFormOptionPickerFunctionalArea((SClientInterface) miClient);
        this.setTextToField(moFunctionalAreaPicker.getSelectedPrimaryKey() == null ? null : ((int[]) moFunctionalAreaPicker.getSelectedPrimaryKey()));
    }
    
    private void setTextToField(int[] key) {
        tfFunctionalAreas.setValue("");
        msFunctionalAreaIds = "";
        int nFunctionalArea = 0;
        
        if (key != null) {
            nFunctionalArea = key[0];
        }
        
        String texts[] = STrnFunctionalAreaUtils.getTextFilterOfFunctionalAreas((SClientInterface) miClient, nFunctionalArea);
        msFunctionalAreaIds = texts[0];
        
        tfFunctionalAreas.setText(texts[1]);
        tfFunctionalAreas.setCaretPosition(0);
    }
    
    public void updateOptions() {
        moFieldKeyGroup.initGroup();
        moFieldKeyGroup.populateCatalogues();
        moFieldKeyGroup.resetGroup();
    }
    
     private void actionEditFunctionalArea() {
        jbEditFunctionalArea.setEnabled(false);
        moFunctionalAreaPicker.setVisible(true);
        
        if (moFunctionalAreaPicker.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
            this.setTextToField(moFunctionalAreaPicker.getSelectedPrimaryKey() == null ? null : ((int[]) moFunctionalAreaPicker.getSelectedPrimaryKey()));
        }
        
        jbEditFunctionalArea.setEnabled(true);
    }

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    @Override
    public void createParamsMap() {
        String orderByTax = "t.id_tax_bas, t.id_tax, t.tax";
        String orderByBizPartner = "b.bp, b.id_bp";
        String orderByDoc = "d.num_ser, d.num, d.id_year, d.id_doc, d.dt, d.stot_r, d.tax_charged_r, d.tax_retained_r, d.tot_r, x.f_dps_bal, d.stot_cur_r, d.tax_charged_cur_r, d.tax_retained_cur_r, d.tot_cur_r, x.f_dps_bal_cur, c.id_cur, c.cur_key";
        
        String areasFilter = "";
        if (((SClientInterface) miClient).getSessionXXX().getParamsCompany().getIsFunctionalAreas()) {
            if (msFunctionalAreaIds.isEmpty()) {
                areasFilter = "";
            }
            else {
                areasFilter = " AND d.fid_func IN ( " + msFunctionalAreaIds + " ) ";
            }
        }
        
        moParamsMap = miClient.createReportParams();
        moParamsMap.put("sTitle", getTitle().toUpperCase());
        moParamsMap.put("nYear", SLibTimeUtils.digestYear(moDateDate.getValue())[0]);
        moParamsMap.put("tDate", moDateDate.getValue());
        
        switch (mnFormSubtype) {
            case SModSysConsts.TRNS_CT_DPS_PUR:
                moParamsMap.put("nSysAccountClassId", SModSysConsts.FINS_CL_SYS_ACC_BPR_SUP);
                moParamsMap.put("nSysMovementClassId", SModSysConsts.FINS_CL_SYS_MOV_PUR);
                moParamsMap.put("nDisplayFactor", -1);  // (debits - credits) * -1
                break;
            case SModSysConsts.TRNS_CT_DPS_SAL:
                moParamsMap.put("nSysAccountClassId", SModSysConsts.FINS_CL_SYS_ACC_BPR_CUS);
                moParamsMap.put("nSysMovementClassId", SModSysConsts.FINS_CL_SYS_MOV_SAL);
                moParamsMap.put("nDisplayFactor", 1);  // (debits - credits) * 1
                break;
            default:
        }
        
        if (moRadFilterDocPayed.isSelected()) {
            moParamsMap.put("sFilterDpsBalance", moRadFilterDocPayed.getText().toUpperCase());
            moParamsMap.put("sSqlDpsBalance", "AND (x.f_dps_bal=0 AND x.f_dps_bal_cur=0) ");
        }
        else if (moRadFilterDocPending.isSelected()) {
            moParamsMap.put("sFilterDpsBalance", moRadFilterDocPending.getText().toUpperCase());
            moParamsMap.put("sSqlDpsBalance", "AND (x.f_dps_bal<>0 OR x.f_dps_bal_cur<>0) ");
        }
        else {
            moParamsMap.put("sFilterDpsBalance", moRadFilterDocAll.getText().toUpperCase());
            moParamsMap.put("sSqlDpsBalance", "");
        }
        
        if (moRadOrderByDoc.isSelected()) {
            moParamsMap.put("sSqlOrderBy", orderByTax + ", " + orderByDoc);
            moParamsMap.put("bShowBizPartnerGroup", false);
        }
        else {
            moParamsMap.put("sSqlOrderBy", orderByTax + ", " + orderByBizPartner + ", " + orderByDoc);
            moParamsMap.put("bShowBizPartnerGroup", true);
        }
        
        moParamsMap.put("sFuncText", tfFunctionalAreas.getText());
        moParamsMap.put("sSqlFunAreas", areasFilter);
    }

    @Override
    public SGuiValidation validateForm() {
        return moFields.validateFields();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbEditFunctionalArea) {
                actionEditFunctionalArea();
            }
        }
    }
}
