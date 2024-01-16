/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mqlt.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.mtrn.form.SDialogAnalysisDocumentKardex;
import java.awt.Dimension;
import javax.swing.JButton;

/**
 *
 * @author Edwin Carmona
 */
public class SViewDocumentAnalysis extends erp.lib.table.STableTab implements java.awt.event.ActionListener {
    
    private JButton jbAnalysisKardex;
    private SDialogAnalysisDocumentKardex moDialogStockCardexHistoric;

    public SViewDocumentAnalysis(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.QLTX_DOCUMENT_ANALYSIS);
        initComponents();
    }

    private void initComponents() {
        
        addTaskBarUpperSeparator();
        
        jbAnalysisKardex = new JButton(miClient.getImageIcon(SLibConstants.ICON_KARDEX));
        jbAnalysisKardex.setPreferredSize(new Dimension(23, 23));
        jbAnalysisKardex.setToolTipText("Ver análisis configurados");
        jbAnalysisKardex.addActionListener(this);
        addTaskBarUpperComponent(jbAnalysisKardex);
        
        moDialogStockCardexHistoric = new SDialogAnalysisDocumentKardex(miClient);
        
        erp.lib.table.STableField[] aoKeyFields = new STableField[2];

        int i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "v.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "v.id_doc");
        
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }
        
        erp.lib.table.STableColumn[] aoTableColumns = new STableColumn[9];

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo Doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "num", "Num", STableConstants.WIDTH_ITEM_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "num_ser", "Serie", STableConstants.WIDTH_ITEM_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "num_ref", "Referencia", STableConstants.WIDTH_ITEM_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt", "Fecha", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt_doc", "Fecha Doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Cliente", STableConstants.WIDTH_ITEM_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "concept_key", "Clave", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "concept", "Concepto", STableConstants.WIDTH_ITEM_2X);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }
    
    public void actionViewAnalysisKardex() {
        if (jbAnalysisKardex.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                int[] rowKey = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();
                int yearId = rowKey[0];
                int docId = rowKey[1];

                moDialogStockCardexHistoric.formReset();
                moDialogStockCardexHistoric.setFormParams(yearId, docId);
                moDialogStockCardexHistoric.setVisible(true);
            }
        }
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_QLT).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_QLT).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_QLT ).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_QLT).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    @Override
    public void createSqlQuery() {
        java.lang.String sqlWhere = "";
        
        msSql = "SELECT  "
                + "    v.id_year, "
                + "    v.id_doc, "
                + "    v.dt, "
                + "    v.dt_doc, "
                + "    v.num, "
                + "    v.num_ser, "
                + "    v.num_ref, "
                + "    de.concept_key, "
                + "    de.concept, "
                + "    dt.code, "
                + "    bp.bp, "
                + "    bp.fiscal_id "
                + "FROM "
                + "    " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS) + " AS v "
                + "        INNER JOIN "
                + "    " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_ETY) + " AS de ON v.id_year = de.id_year AND v.id_doc = de.id_doc "
                + "         INNER JOIN " 
                + "    " + SDataConstants.TablesMap.get(SDataConstants.TRNU_TP_DPS) + " AS dt ON v.fid_ct_dps = dt.id_ct_dps "
                + "         AND v.fid_cl_dps = dt.id_cl_dps AND v.fid_tp_dps = dt.id_tp_dps "
                + "         AND v.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_EST[0] + " AND v.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1] + " "
                + "         AND v.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CON[2] + " "
                + "        INNER JOIN "
                + "     " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_ETY_ANALYSIS) + " AS dea ON de.id_year = dea.fid_dps_year_n "
                + "        AND de.id_doc = dea.fid_dps_doc_n "
                + "        AND de.id_ety = dea.fid_dps_ety_n "
                + "        INNER JOIN "
                + "     " + SDataConstants.TablesMap.get(SDataConstants.BPSU_BP) + " AS bp ON v.fid_bp_r = bp.id_bp "
                + (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) + " "
                + "GROUP BY v.id_year , v.id_doc";
                
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();
            if (button == jbAnalysisKardex) {
                actionViewAnalysisKardex();
            }
        }
    }
}