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
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;
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
public class SViewOrdersToProcess extends SGridPaneView {
    
    private Date mtDateStart;
    private Date mtDateFinal;
    private int mnYearId;
    private int mnBizPartherId;
    
    private SGridFilterPanelFunctionalArea moFilterFunctionalArea;
    
    public SViewOrdersToProcess(SGuiClient client, int gridType, int gridSubtype, String title, SGuiParams params) {
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
        if (filter != null && !((String) filter).isEmpty()) {
            sql += "AND d.fid_func IN ( " + filter + ") ";
        }
        
        moPaneSettings = new SGridPaneSettings(2);
        msSql = "SELECT id_year " + SDbConsts.FIELD_ID + "1, " +
                "id_doc " + SDbConsts.FIELD_ID + "2, " + 
                "'' AS " + SDbConsts.FIELD_CODE + ", " +
                "'' AS " + SDbConsts.FIELD_NAME + ", " + 
                "num_ref, dt, f_num, fid_cob, fid_bpb, f_cob_code, bpb, " +
                "SUM(f_qty) AS f_qty, SUM(f_orig_qty) AS f_orig_qty, SUM(f_link_qty) AS f_link_qty, SUM(f_link_orig_qty) AS f_link_orig_qty, COUNT(*) AS f_count, COUNT(f_link_orig_qty >= f_orig_qty) AS f_count_link " +
                "FROM (SELECT de.id_year, de.id_doc, d.dt, d.dt_doc_delivery_n, d.dt_doc_lapsing_n, d.num_ref, d.b_link, d.ts_link, CONCAT(d.num_ser, IF(length(d.num_ser) = " + SModSysConsts.FINS_CFD_TAX_NA + ", '', '-'), d.num) " +
                "AS f_num, d.fid_cob, d.fid_bpb, d.fid_bp_r, d.fid_usr_link, dt.code AS f_dt_code, dn.code AS f_dn_code, cob.code AS f_cob_code, bb.bpb, b.bp, bc.bp_key, c.cur_key, ul.usr, de.fid_item, de.fid_unit, " +
                "de.fid_orig_unit, de.surplus_per, de.qty AS f_qty, de.orig_qty AS f_orig_qty, CASE WHEN de.qty = " + SModSysConsts.FINS_CFD_TAX_NA + " THEN " + SModSysConsts.FINS_CFD_TAX_NA + " ELSE de.stot_cur_r / de.qty END AS f_price_u, " +
                "CASE WHEN de.orig_qty = " + SModSysConsts.FINS_CFD_TAX_NA + " THEN " + SModSysConsts.FINS_CFD_TAX_NA + " ELSE de.stot_cur_r / de.orig_qty END AS f_orig_price_u, i.item_key, i.item, ig.igen, u.symbol AS f_unit, uo.symbol AS f_orig_unit, " +
                "COALESCE((SELECT SUM(ds.qty) FROM trn_dps_dps_supply AS ds, trn_dps_ety AS xde, trn_dps AS xd WHERE ds.id_src_year = de.id_year AND ds.id_src_doc = de.id_doc AND ds.id_src_ety = de.id_ety " +
                "AND ds.id_des_year = xde.id_year AND ds.id_des_doc = xde.id_doc AND ds.id_des_ety = xde.id_ety AND xde.id_year = xd.id_year AND xde.id_doc = xd.id_doc AND xde.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " AND xd.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " " +
                "AND xd.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + "), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_link_qty, COALESCE((SELECT SUM(ds.orig_qty) FROM trn_dps_dps_supply AS ds, trn_dps_ety AS xde, trn_dps AS xd WHERE ds.id_src_year = de.id_year " +
                "AND ds.id_src_doc = de.id_doc AND ds.id_src_ety = de.id_ety AND ds.id_des_year = xde.id_year AND ds.id_des_doc = xde.id_doc AND ds.id_des_ety = xde.id_ety AND xde.id_year = xd.id_year " +
                "AND xde.id_doc = xd.id_doc AND xde."
                + "b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " AND xd.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " AND xd.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + "), " + SModSysConsts.FINS_CFD_TAX_NA + ") AS f_link_orig_qty " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON d.id_year = de.id_year " +
                "AND d.id_doc = de.id_doc AND d.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " AND de.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " + sql; 
                if (mnGridMode == SDataConstantsSys.BPSS_CT_BP_CUS) {
                    msSql += "AND d.fid_ct_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " ";
                }
                else {
                    msSql += "AND d.fid_ct_dps = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " ";
                }                
                msSql += " AND d.id_year <= " + mnYearId + " AND d.dt >= '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "' AND d.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtDateFinal) + "' " +
                "AND d.fid_cob = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " AND d.fid_bp_r = " + mnBizPartherId + 
                " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_DPS_NAT) + " AS dn ON d.fid_dps_nat = dn.id_dps_nat " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob ON d.fid_cob = cob.id_bpb " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bb ON d.fid_bpb = bb.id_bpb " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON d.fid_bp_r = bc.id_bp AND bc.id_ct_bp = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ul ON d.fid_usr_link = ul.id_usr " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON de.fid_item = i.id_item " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS ig ON i.fid_igen = ig.id_igen " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON de.fid_unit = u.id_unit " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS uo ON de.fid_orig_unit = uo.id_unit GROUP BY de.id_year, de.id_doc, d.dt, d.dt_doc_delivery_n, " +
                "d.num_ref, d.b_link, d.ts_link, d.num_ser, d.num, d.fid_cob, d.fid_bpb, d.fid_bp_r, d.fid_usr_link, dt.code, cob.code, bb.bpb, b.bp, bc.bp_key, c.cur_key, ul.usr, " +
                "de.fid_item, de.fid_unit, de.fid_orig_unit, de.surplus_per, de.qty, de.orig_qty, de.stot_cur_r, i.item_key, i.item, u.symbol, uo.symbol ) AS T " +
                "GROUP BY id_year, id_doc, dt, dt_doc_delivery_n, num_ref, b_link, ts_link, f_num, fid_cob, fid_bpb, fid_bp_r, fid_usr_link, f_dt_code, f_cob_code, bpb, bp, bp_key, usr " +
                "HAVING (f_link_orig_qty < f_orig_qty AND b_link = 0) OR f_count <> f_count_link ORDER BY f_dt_code, f_num, dt, id_year, id_doc, bp, bp_key, fid_bp_r, bpb, fid_bpb ";
        }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
       
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", "Folio doc", STableConstants.WIDTH_DOC_NUM));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha doc", STableConstants.WIDTH_DATE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "f_orig_qty", "Cantidad", STableConstants.WIDTH_VALUE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "f_link_orig_qty", "Cant. procesada", STableConstants.WIDTH_VALUE));
        
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "", "Avance %", STableConstants.WIDTH_VALUE);
        column.getRpnArguments().add(new SLibRpnArgument("f_link_orig_qty", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument("f_orig_qty", SLibRpnArgumentType.OPERAND ));
        column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.DIVISION, SLibRpnArgumentType.OPERATOR));
        
        gridColumnsViews.add(column);
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "f_orig_qty", "Cant. pendiente", STableConstants.WIDTH_VALUE));
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRNU_TP_DPS);
        moSuscriptionsSet.add(SModConsts.TRNU_DPS_NAT);
        moSuscriptionsSet.add(SModConsts.BPSU_BPB);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.BPSU_BP_CT);
        moSuscriptionsSet.add(SModConsts.CFGU_CUR);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.ITMU_IGEN);
        moSuscriptionsSet.add(SModConsts.ITMU_UNIT);
    }
}