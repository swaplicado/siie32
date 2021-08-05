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
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Claudio Peña, Sergio Flores
 */
public class SViewInvoicesToSupply extends SGridPaneView {
    
    private Date mtDateStart;
    private Date mtDateFinal;
    private int mnYearId;
    private int mnBizPartherId;
    
    private SGridFilterPanelFunctionalArea moFilterFunctionalArea;
    
    public SViewInvoicesToSupply(SGuiClient client, int gridType, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, gridType, gridSubtype, title, params);
        initComponentsCustom();
    }

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        mtDateStart = null;
        mnBizPartherId = 0;
        createGridColumns();
        
        moFilterFunctionalArea = new SGridFilterPanelFunctionalArea(miClient, this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFunctionalArea);
    }

    private void setParamsView(final Date dateStart, final Date dateFinal, final int year, final int idBizPartner ) {
        mtDateStart = dateStart;
        mtDateFinal = dateFinal;
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
    
    public void initView(final Date dateStart, final Date dateFinal, final int year, final int idBizPartner) {
        setParamsView(dateStart, dateFinal, year, idBizPartner);
        renderView();
    }
       
    /*
     * Overriden methods
     */
    
    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;
        
        filter = (String) (moFiltersMap.get(SFilterConstants.SETTING_FILTER_FUNC_AREA) == null ? null : moFiltersMap.get(SFilterConstants.SETTING_FILTER_FUNC_AREA).getValue());
        if (filter != null && ! ((String) filter).isEmpty()) {
            sql += " AND da.fid_func IN ( " + filter + ") ";
        }
        
        moPaneSettings = new SGridPaneSettings(2);
        msSql= "SELECT id_year " + SDbConsts.FIELD_ID + "1, " +
                "id_doc " + SDbConsts.FIELD_ID + "2, " + 
                "'' AS " + SDbConsts.FIELD_CODE + ", " +
                "'' AS " + SDbConsts.FIELD_NAME + ", " + 
                " dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_close, ts_close, usr, cur_key, f_num, f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb, SUM(f_qty) AS f_qty, " +
                "SUM(f_orig_qty) AS f_orig_qty, COALESCE(SUM(f_adj_qty), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_adj_qty, COALESCE(SUM(f_adj_orig_qty), " + SModSysConsts.FINS_CFD_TAX_NA + ") " +
                "AS f_adj_orig_qty, COALESCE(SUM(f_sup_qty), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_sup_qty, " +
                "COALESCE(SUM(f_sup_orig_qty), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_sup_orig_qty FROM (SELECT de.id_year, de.id_doc, de.id_ety, d.dt, d.num_ser, " +
                "d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, " +
                "d.ts_close, uc.usr, c.cur_key, CONCAT(d.num_ser, IF(length(d.num_ser) = " + SModSysConsts.FINS_CFD_TAX_NA + ", '', '-'), d.num) AS f_num, dt.code " +
                "AS f_dt_code, cb.code AS f_cb_code, b.id_bp, b.bp, bc.bp_key, " +
                "bb.bpb, de.fid_item, de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol AS f_unit, uo.symbol AS f_orig_unit, de.qty AS f_qty, de.orig_qty AS f_orig_qty, " +
                "COALESCE((SELECT SUM(ddd.qty) FROM trn_dps_dps_adj AS ddd, trn_dps_ety AS dae, trn_dps AS da WHERE ddd.id_dps_year = de.id_year " +
                "AND ddd.id_dps_doc = de.id_doc AND ddd.id_dps_ety = de.id_ety AND ddd.id_adj_year = dae.id_year AND ddd.id_adj_doc = dae.id_doc AND ddd.id_adj_ety = dae.id_ety " +
                "AND dae.id_year = da.id_year AND dae.id_doc = da.id_doc AND dae.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " " +
                "AND dae.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " AND da.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " AND " +
                "da.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + sql + "), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_adj_qty, " +
                "COALESCE((SELECT SUM(ddd.orig_qty) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_ADJ) + " AS ddd, trn_dps_ety AS dae, trn_dps " +
                "AS da WHERE ddd.id_dps_year = de.id_year AND ddd.id_dps_doc = de.id_doc " +
                "AND ddd.id_dps_ety = de.id_ety AND ddd.id_adj_year = dae.id_year AND ddd.id_adj_doc = dae.id_doc AND ddd.id_adj_ety = dae.id_ety AND dae.id_year = da.id_year " +
                "AND dae.id_doc = da.id_doc AND dae.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " AND dae.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " " +
                "AND da.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " AND da.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + sql + "), 0) AS f_adj_orig_qty, " +
                "COALESCE((SELECT SUM(ge.qty * CASE WHEN ge.fid_dps_adj_year_n IS NULL THEN " + SModSysConsts.FINS_TP_ACC_NA + " ELSE -1 END) " +
                "FROM trn_diog_ety AS ge, trn_diog AS g WHERE ge.fid_dps_year_n = de.id_year " +
                "AND ge.fid_dps_doc_n = de.id_doc AND ge.fid_dps_ety_n = de.id_ety AND ge.id_year = g.id_year AND ge.id_doc = g.id_doc " +
                "AND d.id_year = " + mnYearId + " AND d.dt >= '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "' " +
                "AND d.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtDateFinal) + "' AND ge.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " " +
                "AND g.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + "), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_sup_qty, " +
                "COALESCE((SELECT SUM(ge.orig_qty * CASE WHEN ge.fid_dps_adj_year_n IS NULL THEN " + SModSysConsts.FINS_TP_ACC_NA + " ELSE -1 END) FROM trn_diog_ety AS ge, trn_diog AS g " +
                "WHERE ge.fid_dps_year_n = de.id_year AND ge.fid_dps_doc_n = de.id_doc AND ge.fid_dps_ety_n = de.id_ety AND ge.id_year = g.id_year AND ge.id_doc = g.id_doc " +
                "AND d.id_year = " + mnYearId + " AND d.dt >= '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "' AND d.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtDateFinal) + "' " +
                "AND ge.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " AND g.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + "), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_sup_orig_qty FROM trn_dps AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps " +
                "AND d.fid_tp_dps = dt.id_tp_dps AND d.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " ";
                if (mnGridMode == SDataConstantsSys.BPSS_CT_BP_CUS) {
                    msSql += "AND d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] + " ";
                }
                else {
                    msSql += "AND d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1] + " ";
                }
                msSql += "AND d.fid_cob = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " AND d.fid_bp_r = " + mnBizPartherId +
                " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cb ON d.fid_cob = cb.id_bpb " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON d.fid_bp_r = bc.id_bp AND bc.id_ct_bp = " + mnGridMode + " " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bb ON d.fid_bpb = bb.id_bpb " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON d.fid_usr_close = uc.id_usr " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "AND de.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " AND de.b_inv = " + SModSysConsts.FINS_TP_ACC_NA + " AND de.qty > " + SModSysConsts.FINS_CFD_TAX_NA + " " +
                "AND de.orig_qty > " + SModSysConsts.FINS_CFD_TAX_NA + " " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON de.fid_item = i.id_item " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON de.fid_unit = u.id_unit " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS uo ON de.fid_orig_unit = uo.id_unit GROUP BY de.id_year, de.id_doc, de.id_ety, d.dt, " +
                "d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, dt.code, cb.code, b.id_bp, b.bp, bc.bp_key, bb.bpb, de.fid_item, " +
                "de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol, uo.symbol, de.qty, de.orig_qty " +
                "HAVING (f_orig_qty - f_adj_orig_qty - f_sup_orig_qty) <> " + SModSysConsts.FINS_CFD_TAX_NA + " " +
                "AND d.b_close = " + SModSysConsts.FINS_CFD_TAX_NA + " ) AS DPS_ETY_TMP " +
                "GROUP BY id_year, id_doc, dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_close, ts_close, usr, cur_key, f_num, f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb " +
                "ORDER BY f_dt_code, num_ser, CAST(num AS UNSIGNED INTEGER), num, dt, bp, id_bp, bp_key ";
  
        }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
       
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", "Folio doc", STableConstants.WIDTH_CODE_DOC));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num_ref", "Referencia", STableConstants.WIDTH_DOC_NUM));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha doc", STableConstants.WIDTH_DATE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "tot_r", "Total mon $", STableConstants.WIDTH_VALUE_2X));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "cur_key", "Moneda ", STableConstants.WIDTH_CURRENCY_KEY));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "f_orig_qty", "Cant. original ", STableConstants.WIDTH_VALUE_2X));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "f_adj_orig_qty", "Cant. ajustada ", STableConstants.WIDTH_VALUE_2X));
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CFGU_CUR);
        moSuscriptionsSet.add(SModConsts.BPSU_BPB);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.BPSU_BP_CT);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
        moSuscriptionsSet.add(SModConsts.TRN_DPS_ETY);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.ITMU_UNIT);       
    }
}