/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view.qi;

import erp.data.SDataConstantsSys;
import erp.gui.grid.SGridFilterPanelFunctionalArea;
import erp.lib.table.STableConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.table.SFilterConstants;
import java.util.ArrayList;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;

/*
 *
 * @author Claudio Peña
 */
public class SViewCreditNotesToReturn extends SGridPaneView {
    
    private SGridFilterPanelFunctionalArea moFilterFunctionalArea;

    private int mnBizPartherId;
    private int mnYearId;
    
    public SViewCreditNotesToReturn(SGuiClient client, int gridType, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, gridType, gridSubtype, title, params);       
        initComponentsCustom();
    }

    /*
     * Private methods
     */

 private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        mnBizPartherId = 0;
        createGridColumns();
        
        moFilterFunctionalArea = new SGridFilterPanelFunctionalArea(miClient, this, SFilterConstants.SETTING_FILTER_FUNC_AREA);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFunctionalArea);
    }
    
     private void setParamsView(final int idBizPartner, final int year ) {
        mnYearId = year;
        mnBizPartherId = idBizPartner;
    }
    
    private void renderView() {
        createGridColumns();
        populateGrid(SGridConsts.REFRESH_MODE_RELOAD);
    }

    /*
     * Public methods
     */
    
    public void initView(final int idBizPartner, final int year) {
        setParamsView(year, idBizPartner);
        renderView();
    }
    
    /*
     * Overriden methods
     */
    
    @Override
    public void prepareSqlQuery() {
        String whereFunAreas = "";
        Object filter = null;
        
        filter = (String) (moFiltersMap.get(SFilterConstants.SETTING_FILTER_FUNC_AREA) == null ? null : moFiltersMap.get(SFilterConstants.SETTING_FILTER_FUNC_AREA).getValue());
        if (filter != null) {
            whereFunAreas = " AND d.fid_func IN ( " + filter + ") ";
        }
       
        moPaneSettings = new SGridPaneSettings(2);
        
        if (mnGridMode == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
         msSql = "SELECT id_year AS f_id_1, id_doc AS f_id_2, num_ref AS f_code, bp AS f_name, id_year, id_doc, dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_close, ts_close, " +
                "usr, cur_key, f_num, f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb, " +
                "SUM(f_qty) AS f_qty, SUM(f_orig_qty) AS f_orig_qty, COALESCE(SUM(f_ret_qty), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_ret_qty, " +
                "COALESCE(SUM(f_ret_orig_qty), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_ret_orig_qty FROM " +
                "( SELECT de.id_year, de.id_doc, de.id_ety, d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = " + SModSysConsts.FINS_CFD_TAX_NA + ", '', '-'), d.num) AS f_num, dt.code AS f_dt_code, cb.code " +
                "AS f_cb_code, b.id_bp, b.bp, bc.bp_key, bb.bpb, de.fid_item, " +
                "de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol AS f_unit, uo.symbol AS f_orig_unit, de.qty AS f_qty, de.orig_qty AS f_orig_qty, " +
                "COALESCE((SELECT SUM(ge.qty) FROM trn_diog_ety AS ge, trn_diog AS g WHERE ge.fid_dps_adj_year_n = de.id_year AND ge.fid_dps_adj_doc_n = de.id_doc " +
                "AND ge.fid_dps_adj_ety_n = de.id_ety AND ge.id_year = g.id_year AND ge.id_doc = g.id_doc AND ge.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " " +
                "AND g.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + "), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_ret_qty, " +
                "COALESCE((SELECT SUM(ge.orig_qty) FROM trn_diog_ety AS ge, trn_diog AS g WHERE ge.fid_dps_adj_year_n = de.id_year AND ge.fid_dps_adj_doc_n = de.id_doc " +
                "AND ge.fid_dps_adj_ety_n = de.id_ety AND ge.id_year = g.id_year AND ge.id_doc = g.id_doc AND ge.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " " +
                "AND g.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + "), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_ret_orig_qty " +
                "FROM trn_dps AS d " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND d.b_del = " + SDataConstantsSys.TRNX_DIOG_CST_ASIG_NA + " " +
                "AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                "AND d.fid_ct_dps = " + SDataConstantsSys.CFGS_CT_ENT_CASH + " AND d.fid_cl_dps = " + SDataConstantsSys.CFGS_TP_REL_N_TO_M + " AND d.fid_cob = " + SDataConstantsSys.CFGS_CT_ENT_CASH + " " +
                "AND d.fid_bp_r = " + mnBizPartherId + " " + whereFunAreas +
                "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN erp.bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
                "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bc ON d.fid_bp_r = bc.id_bp AND bc.id_ct_bp = " + mnGridMode + " " +
                "INNER JOIN erp.bpsu_bpb AS bb ON d.fid_bpb = bb.id_bpb " +
                "INNER JOIN erp.usru_usr AS uc ON d.fid_usr_close = uc.id_usr " +
                "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND de.b_del = " + SDataConstantsSys.TRNX_DIOG_CST_ASIG_NA + " AND de.qty > " + SDataConstantsSys.TRNX_DIOG_CST_ASIG_NA + " " +
                "AND de.b_inv = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " AND de.orig_qty > " + SDataConstantsSys.TRNX_DIOG_CST_ASIG_NA + " AND de.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON de.fid_unit = u.id_unit " +
                "INNER JOIN erp.itmu_unit AS uo ON de.fid_orig_unit = uo.id_unit " +
                "GROUP BY de.id_year, de.id_doc, de.id_ety, d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, dt.code, cb.code, b.id_bp, " +
                "b.bp, bc.bp_key, bb.bpb, de.fid_item, de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol, uo.symbol, de.qty, de.orig_qty " +
                "HAVING (f_orig_qty - f_ret_orig_qty) <> " + SDataConstantsSys.TRNX_DIOG_CST_ASIG_NA + " AND d.b_close = " + SDataConstantsSys.TRNX_DIOG_CST_ASIG_NA + " ) AS DPS_ETY_TMP " +
                "GROUP BY id_year, id_doc, dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_close, ts_close, usr, cur_key, f_num, f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb " +
                "ORDER BY bp, id_bp, bp_key, f_dt_code, num_ser, CAST(num AS UNSIGNED INTEGER), num, dt" ;
            }
            else {
        msSql = "SELECT 1 AS f_id_1, 2 AS f_id_2, 3 AS f_code, 4 AS f_name, id_year, id_doc, dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_close, " +
               "ts_close, usr, cur_key, f_num, f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb, " +
               "SUM(f_qty) AS f_qty, SUM(f_orig_qty) AS f_orig_qty, COALESCE(SUM(f_ret_qty), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_ret_qty, " +
               "COALESCE(SUM(f_ret_orig_qty), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_ret_orig_qty " +
               "FROM ( SELECT de.id_year, de.id_doc, de.id_ety, d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, " +
               "CONCAT(d.num_ser, IF(length(d.num_ser) = " + SModSysConsts.FINS_CFD_TAX_NA + ", '', '-'), d.num) AS f_num, dt.code AS f_dt_code, cb.code " +
               "AS f_cb_code, b.id_bp, b.bp, bc.bp_key, bb.bpb, de.fid_item, de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol " +
               "AS f_unit, uo.symbol AS f_orig_unit, de.qty AS f_qty, de.orig_qty AS f_orig_qty, COALESCE((SELECT SUM(ge.qty) " +
               "FROM trn_diog_ety AS ge, trn_diog AS g " +
               "WHERE ge.fid_dps_adj_year_n = de.id_year AND ge.fid_dps_adj_doc_n = de.id_doc AND ge.fid_dps_adj_ety_n = de.id_ety " +
               "AND ge.id_year = g.id_year AND ge.id_doc = g.id_doc AND ge.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " " +
               "AND g.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + "), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_ret_qty, " +
               "COALESCE((SELECT SUM(ge.orig_qty) " +
               "FROM trn_diog_ety AS ge, trn_diog AS g " +
               "WHERE ge.fid_dps_adj_year_n = de.id_year AND ge.fid_dps_adj_doc_n = de.id_doc AND ge.fid_dps_adj_ety_n = de.id_ety AND ge.id_year = g.id_year AND ge.id_doc = g.id_doc " +
               "AND ge.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " AND g.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + "), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_ret_orig_qty " +
               "FROM trn_dps AS d " +
               "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND d.b_del = " + SDataConstantsSys.TRNX_DIOG_CST_ASIG_NA + " " +
               "AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
               "AND d.fid_ct_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_cl_dps =  " + SDataConstantsSys.CFGS_TP_REL_N_TO_M + " AND d.fid_cob = " + SDataConstantsSys.CFGS_CT_ENT_CASH + " " +
               "AND d.fid_bp_r = " + mnBizPartherId + " " + whereFunAreas +
               "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
               "INNER JOIN erp.bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
               "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
               "INNER JOIN erp.bpsu_bp_ct AS bc ON d.fid_bp_r = bc.id_bp AND bc.id_ct_bp = " + mnGridMode + " " +
               "INNER JOIN erp.bpsu_bpb AS bb ON d.fid_bpb = bb.id_bpb " +
               "INNER JOIN erp.usru_usr AS uc ON d.fid_usr_close = uc.id_usr " +
               "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND de.b_del = " + SDataConstantsSys.TRNX_DIOG_CST_ASIG_NA + " AND de.qty > " + SDataConstantsSys.TRNX_DIOG_CST_ASIG_NA + " " +
               "AND de.b_inv = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " AND de.orig_qty > " + SDataConstantsSys.TRNX_DIOG_CST_ASIG_NA + " AND de.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
               "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item " +
               "INNER JOIN erp.itmu_unit AS u ON de.fid_unit = u.id_unit " +
               "INNER JOIN erp.itmu_unit AS uo ON de.fid_orig_unit = uo.id_unit " +
               "GROUP BY de.id_year, de.id_doc, de.id_ety, d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, dt.code, cb.code, " +
               "b.id_bp, b.bp, bc.bp_key, bb.bpb, de.fid_item, de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol, uo.symbol, de.qty, de.orig_qty " +
               "HAVING (f_orig_qty - f_ret_orig_qty) <> " + SDataConstantsSys.TRNX_DIOG_CST_ASIG_NA + " AND d.b_close = " + SDataConstantsSys.TRNX_DIOG_CST_ASIG_NA + " ) AS DPS_ETY_TMP " +
               "GROUP BY id_year, id_doc, dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_close, ts_close, usr, cur_key, f_num, f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb " +
               "ORDER BY f_dt_code, num_ser, CAST(num AS UNSIGNED INTEGER), num, dt, bp, id_bp, bp_key ";
                }
    }
          

    @Override 
    public ArrayList<SGridColumnView> createGridColumns() {
        
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_dt_code", "Tip doc.", STableConstants.WIDTH_CODE_DOC));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", "Folio doc", STableConstants.WIDTH_DOC_NUM));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num_ref", "Referencia", STableConstants.WIDTH_DOC_NUM_REF));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "bp", "Cliente", 200));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "id_bp", "Clave", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "tot_r", "Total moneda $", STableConstants.WIDTH_VALUE_2X));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "f_qty", "Cant. original", STableConstants.WIDTH_VALUE_2X));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "f_orig_qty", "Cant. devuelta", STableConstants.WIDTH_VALUE_2X));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "f_ret_qty", "Cant. pendiente", STableConstants.WIDTH_VALUE_2X));
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_DPS);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.BPSU_BP_CT);
        moSuscriptionsSet.add(SModConsts.BPSU_BPB);
        moSuscriptionsSet.add(SModConsts.TRNU_TP_DPS);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}