/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.STrnDiogComplement;
import erp.mtrn.form.SDialogDpsFinder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Uriel Castañeda
 */
public class SViewOrderSuppliedWhitMovs extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private JButton mjbSupplyOrder;
    private JButton mjbImport;
    private JButton mjbViewDps;
    private JButton mjbViewNotes;
    private JButton mjbViewLinks;
    private erp.mtrn.form.SDialogDpsFinder moDialogDpsFinder;
    
    public SViewOrderSuppliedWhitMovs(SGuiClient client, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_DPS_ORDER_SUP, gridSubtype, title, params);
        initComponentsCustom();
    }

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
            
        mjbSupplyOrder = SGridUtils.createButton(new ImageIcon(getClass().getResource(isViewForPurchases() ? "/erp/img/icon_std_dps_supply.gif" : "/erp/img/icon_std_dps_supply.gif")), "Surtir", this);
        mjbImport = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_IMPORT), "Importar documento", this);
        
        mjbViewDps = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_LOOK), "Ver documento", this);
        mjbViewNotes = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_NOTES), "Ver notas", this);
        mjbViewLinks = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_LINK), "Ver vínculos del documento", this);
        
        moDialogDpsFinder = new SDialogDpsFinder((SClientInterface) miClient, SDataConstants.TRNX_DPS_PEND_LINK);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbSupplyOrder);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbImport);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbViewDps);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbViewNotes);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbViewLinks);
        
    }
    
    private boolean isViewForPurchases() {
        return mnGridSubtype == SDataConstantsSys.TRNS_CT_DPS_PUR;
    }
    
    private void actionSupply() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
            
            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else {
                int[] key = isViewForPurchases() ? SDataConstantsSys.TRNS_TP_IOG_IN_PUR_PUR : SDataConstantsSys.TRNS_TP_IOG_OUT_SAL_SAL;
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_DPS, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
                
                ((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_INV).setFormComplement(new STrnDiogComplement(key, dps));
                if (((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_INV).showForm(SDataConstants.TRN_DIOG, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                    ((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(SDataConstants.TRN_DIOG);
                }
            }
        }
        
        miClient.getSession().notifySuscriptors(mnGridType);
    }
    
    private void actionImport() {
        if (mjbImport.isEnabled()) {
            int[] keyOrder = isViewForPurchases() ?  SDataConstantsSys.TRNS_CL_DPS_PUR_ORD : SDataConstantsSys.TRNS_CL_DPS_SAL_ORD;
            
            moDialogDpsFinder.formReset();
            moDialogDpsFinder.setValue(SLibConstants.VALUE_FILTER_KEY, keyOrder);
            moDialogDpsFinder.setVisible(true);

            if (moDialogDpsFinder.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                int[] keyIog = isViewForPurchases() ? SDataConstantsSys.TRNS_TP_IOG_IN_PUR_PUR : SDataConstantsSys.TRNS_TP_IOG_OUT_SAL_SAL;
                SDataDps dps = (SDataDps) moDialogDpsFinder.getValue(SDataConstants.TRN_DPS);

                ((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_INV).setFormComplement(new STrnDiogComplement(keyIog, dps));
                if (((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_INV).showForm(SDataConstants.TRN_DIOG, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                    ((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(SDataConstants.TRN_DIOG);
                }
            }
        }
        
        miClient.getSession().notifySuscriptors(mnGridType);
    }
    
    private void actionViewDps() {
        int gui = isViewForPurchases() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;

        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else {
                ((SClientInterface) miClient).getGuiModule(gui).setFormComplement(SDataConstantsSys.TRNU_TP_DPS_PUR_ORD);
                ((SClientInterface) miClient).getGuiModule(gui).showForm(SDataConstants.TRNX_DPS_RO, gridRow.getRowPrimaryKey());
            }
        }
    }

    private void actionViewNotes() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else {
                SModuleUtilities.showDocumentNotes((SClientInterface) miClient, SDataConstants.TRN_DPS, gridRow.getRowPrimaryKey());
            }
        }
    }

    private void actionViewLinks() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else {
                SModuleUtilities.showDocumentLinks((SClientInterface) miClient, SDataConstants.TRN_DPS, gridRow.getRowPrimaryKey());
            }
        }
    }
    
    /*
     * Public methods
     */

    /*
     * Overriden methods
     */
    
    @Override
    public void prepareSqlQuery() {
        String sqlBizPartner = "";
        String sqlOrderByDoc = "";
        String sqlDiogPeriod = "";
        Object filter = null;

        if (mnGridMode != SModConsts.VIEW_ST_PEND) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sqlDiogPeriod = filter == null ? "" : SGridUtils.getSqlFilterDate("g.dt", (SGuiDate) filter) + " AND ";
        }
        else {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();
            if (filter != null) {
                sqlDiogPeriod = filter == null ? "" : "g.dt <= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' AND ";
            }
        }
        
        if (mnGridSubtype == SDataConstantsSys.TRNS_CT_DPS_SAL) {
            // Sales & customers:

            sqlBizPartner = "AND bc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_CUS + " ";
            sqlOrderByDoc = "f_dt_code, num_ser, CAST(num AS UNSIGNED INTEGER), num, dt, bp_key, bp, id_bp ";
        }
        else {
            // Purchases & suppliers:

            sqlBizPartner = "AND bc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_SUP + " ";
            sqlOrderByDoc = "bp_key, bp, id_bp, f_dt_code, num_ser, CAST(num AS UNSIGNED INTEGER), num, dt ";
        }
        
        moPaneSettings = new SGridPaneSettings(2);

        msSql = "SELECT " +
                "id_year AS " + SDbConsts.FIELD_ID + "1, " +
                "id_doc AS " + SDbConsts.FIELD_ID + "2, " +
                "'' AS " + SDbConsts.FIELD_CODE + ", " +
                "'' AS " + SDbConsts.FIELD_NAME + ", " +
                "dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_close, ts_close, usr, cur_key, f_num, " +
                "f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb, " +
                "SUM(f_qty) AS f_qty, " +
                "SUM(f_orig_qty) AS f_orig_qty, " +
                "COALESCE(SUM(f_sup_qty), 0) AS f_sup_qty, " +
                "COALESCE(SUM(f_sup_orig_qty), 0) AS f_sup_orig_qty, " +
                "((COALESCE(SUM(f_sup_orig_qty), 0) * 100 ) / SUM(f_orig_qty)) AS f_per_sup, " +
                "SUM(f_orig_qty)- COALESCE(SUM(f_sup_orig_qty), 0) AS f_sup_orig_qty_pend, " +
                "(((SUM(f_orig_qty) - COALESCE(SUM(f_sup_orig_qty), 0)) * 100 ) / SUM(f_orig_qty)) AS f_per_pend " +
                "FROM (";    
        
        msSql += "SELECT de.id_year, de.id_doc, de.id_ety, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                "dt.code AS f_dt_code, cb.code AS f_cb_code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "de.fid_item, de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol AS f_unit, uo.symbol AS f_orig_unit, " +
                "de.qty AS f_qty, " +
                "de.orig_qty AS f_orig_qty, " +
                "COALESCE((SELECT SUM(ge.qty * CASE WHEN ge.fid_dps_adj_year_n IS NULL THEN 1 ELSE -1 END) FROM trn_diog_ety AS ge, trn_diog AS g WHERE " +
                "ge.fid_dps_year_n = de.id_year AND ge.fid_dps_doc_n = de.id_doc AND ge.fid_dps_ety_n = de.id_ety AND " +
                "ge.id_year = g.id_year AND ge.id_doc = g.id_doc AND " + sqlDiogPeriod +
                "ge.b_del = 0 AND g.b_del = 0), 0) AS f_sup_qty, " +
                "COALESCE((SELECT SUM(ge.orig_qty * CASE WHEN ge.fid_dps_adj_year_n IS NULL THEN 1 ELSE -1 END) FROM trn_diog_ety AS ge, trn_diog AS g WHERE " +
                "ge.fid_dps_year_n = de.id_year AND ge.fid_dps_doc_n = de.id_doc AND ge.fid_dps_ety_n = de.id_ety AND " +
                "ge.id_year = g.id_year AND ge.id_doc = g.id_doc AND " + sqlDiogPeriod +
                "ge.b_del = 0 AND g.b_del = 0), 0) AS f_sup_orig_qty " +
                "FROM trn_dps AS d " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND " +
                "d.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND " +
                "d.fid_ct_dps = " + mnGridSubtype + " AND d.fid_cl_dps = " + (mnGridSubtype == SDataConstantsSys.TRNS_CT_DPS_SAL ? SDataConstantsSys.TRNS_CL_DPS_SAL_ORD[1] : SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[1] ) + " " + 
                "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN erp.bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
                "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bc ON d.fid_bp_r = bc.id_bp " + sqlBizPartner +
                "INNER JOIN erp.bpsu_bpb AS bb ON d.fid_bpb = bb.id_bpb " +
                "INNER JOIN erp.usru_usr AS uc ON d.fid_usr_close = uc.id_usr " +
                "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND " +
                "de.b_del = 0 AND de.b_inv = 1 AND de.qty > 0 AND de.orig_qty > 0 " +
                "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON de.fid_unit = u.id_unit " +
                "INNER JOIN erp.itmu_unit AS uo ON de.fid_orig_unit = uo.id_unit " +
                "GROUP BY de.id_year, de.id_doc, de.id_ety, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, " +
                "dt.code, cb.code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "de.fid_item, de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol, uo.symbol, " +
                "de.qty, de.orig_qty " +
                "HAVING f_sup_orig_qty <> 0 AND d.b_close = 0 " +
                ") AS DPS_ETY_TMP " + 
                "GROUP BY id_year, id_doc, dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_close, ts_close, usr, cur_key, f_num, " +
                "f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb " +
                "ORDER BY " + sqlOrderByDoc + "; ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_dt_code", "Tipo doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_num", "Folio doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "num_ref", "Referencia doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_cb_code", "Sucursal empresa"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp", mnGridSubtype == SDataConstantsSys.TRNS_CT_DPS_SAL ? "Cliente" : "Proveedor"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "bp_key", "Clave" + (mnGridSubtype == SDataConstantsSys.TRNS_CT_DPS_SAL ? "Cliente" : "Proveedor")));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "bpb", "Sucursal" + (mnGridSubtype == SDataConstantsSys.TRNS_CT_DPS_SAL ? "Cliente" : "Proveedor")));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "tot_cur_r", "Total mon $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "cur_key", "Moneda doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_orig_qty", "Cant. original"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_sup_orig_qty", "Cant. surtida"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "f_per_sup", "Surtido %"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_sup_orig_qty_pend", "Cant. pendiente"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "f_per_pend", "Pendiente %"));
        
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjbSupplyOrder) {
                actionSupply();
            }
            if (button == mjbImport) {
                actionImport();
            }
            else if (button == mjbViewDps) {
                actionViewDps();
            }
            else if (button == mjbViewNotes) {
                actionViewNotes();
            }
            else if (button == mjbViewLinks) {
                actionViewLinks();
            }
           
        }
    }
}
