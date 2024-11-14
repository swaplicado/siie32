/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Isabel Servín
 */
public class SViewFollowingPurchaseMaterialRequest extends SGridPaneView {
    
    private SGridFilterDatePeriod moFilterDatePeriod;

    /**
     * Create new SViewFollowingPurchaseMaterialRequest view.
     * @param client GUI client.
     * @param gridSubtype SModSysConsts.TRNX_MAT_REQ_FOLL_PUR_DATE (closed MR) or SLibConstants.UNDEFINED (open MR).
     * @param title View tab label.
     */
    public SViewFollowingPurchaseMaterialRequest(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_MAT_REQ_FOLL_PUR, gridSubtype, title);
        setRowButtonsEnabled(false);
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_FOLL_PUR_DATE) {
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
    }

    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        moPaneSettings.setUserApplying(false);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setSystemApplying(false);
        
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_FOLL_PUR_DATE) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            where += "r.b_clo_pur AND " + SGridUtils.getSqlFilterDate("r.dt", (SGuiDate) filter);
        }
        else {
            where += "NOT r.b_clo_pur ";
        }
        
        msSql = "SELECT " 
                + "re.id_mat_req AS " + SDbConsts.FIELD_ID + "1, " 
                + "re.id_ety AS " + SDbConsts.FIELD_ID + "2, "  
                + "pe.name AS " + SDbConsts.FIELD_CODE +", "
                + "LPAD(r.num, 6, 0) AS folio, " 
                + "r.dt AS " + SDbConsts.FIELD_DATE + ", " 
                + "ur.usr AS solicitante, " 
                + "re.dt_req_n AS f_requerida, " 
                + "mp.name AS req_pty, " 
                + "sl.ts AS f_compras, " 
                + "ir.item AS req_gasto, " 
                + "er.ts_usr AS f_cot, " 
                + "err.prov_name, " 
                + "IF(dc.dt IS NULL, NULL, 'COT') AS cot_code, " 
                + "dc.num AS folio_cot, " 
                + "dc.num_ref AS ref_cot, " 
                + "dc.dt AS f_cot_siie, " 
                + "bp.bp AS bp_cot, " 
                + "IF(dp.dt IS NULL, NULL, 'PED') AS ped_code, " 
                + "dp.num AS folio_ped, " 
                + "dp.dt AS f_ped_siie, " 
                + "irp.item AS ped_gasto, " 
                + "dp.ts_authorn AS f_ped_auth, " 
                + "dp.dt_doc_delivery_n AS f_ped_entrega, " 
                + "ip.item AS " + SDbConsts.FIELD_NAME + ", " 
                + "dpe.qty, " 
                + "up.symbol, " 
                + "dpe.ts_new, " 
                + "IF(df.dt IS NULL, NULL, 'FAC') AS fac_code, " 
                + "df.num AS folio_fac, " 
                + "df.dt AS f_fac, " 
                + "df.stot_r AS f_stot, " 
                + "c.cur_key AS f_cur, " 
                + "df.stot_cur_r AS local_stot, " 
                + "IF(df.stot_cur_r IS NULL, NULL, 'MXN') AS local_cur "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS r "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " AS re ON "
                + "r.id_mat_req = re.id_mat_req "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT) + " AS pe ON "
                + "r.fk_mat_prov_ent = pe.id_mat_prov_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON "
                + "r.fk_usr_req = ur.id_usr " 
                + "INNER JOIN ("
                + "  SELECT id_mat_req, MAX(ts_usr) ts FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ST_LOG) + " "
                + "  WHERE fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PUR + " "
                + "  GROUP BY id_mat_req) AS sl ON "
                + "r.id_mat_req = sl.id_mat_req " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS mp ON "
                + "re.fk_mat_req_pty_n = mp.id_mat_req_pty " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS ir ON "
                + "re.fk_item_ref_n = ir.id_item " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_EST_REQ) + " AS er ON "
                + "re.id_mat_req = er.fk_mat_req_n " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_EST_REQ_ETY) + " AS ere ON "
                + "re.id_mat_req = ere.fk_mat_req_n AND re.id_ety = ere.fk_mat_req_ety_n "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_EST_REQ_REC) + " AS err ON "
                + "er.id_est_req = err.id_est_req " 
                + "LEFT JOIN (" 
                + "  SELECT dr.* FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " AS dr "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON "
                + "  dr.fid_dps_year = d.id_year AND dr.fid_dps_doc = d.id_doc AND "
                + "  d.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_EST[0] + " AND d.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_EST[1] + " AND d.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_EST[2] + ") " 
                + "AS cot ON re.id_mat_req = cot.fid_mat_req AND re.id_ety = cot.fid_mat_req_ety "
                + "LEFT JOIN (" 
                + "  SELECT dr.* FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " AS dr " 
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON "
                + "  dr.fid_dps_year = d.id_year AND dr.fid_dps_doc = d.id_doc AND "
                + "  d.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + " AND d.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + " AND d.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + ") " 
                + "AS ped ON re.id_mat_req = ped.fid_mat_req AND re.id_ety = ped.fid_mat_req_ety " 
                + "LEFT JOIN (" 
                + "  SELECT dr.* FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " AS dr " 
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON "
                + "  dr.fid_dps_year = d.id_year AND dr.fid_dps_doc = d.id_doc AND "
                + "  d.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[0] + " AND d.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[0] + " AND d.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[0] + ") " 
                + "AS fac ON re.id_mat_req = fac.fid_mat_req AND re.id_ety = fac.fid_mat_req_ety " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS dc ON "
                + "cot.fid_dps_year = dc.id_year AND cot.fid_dps_doc = dc.id_doc " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS dce ON "
                + "cot.fid_dps_year = dce.id_year and cot.fid_dps_doc = dce.id_doc AND cot.fid_dps_ety = dce.id_ety "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                + "dc.fid_bp_r = bp.id_bp " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS dp ON "
                + "ped.fid_dps_year = dp.id_year AND ped.fid_dps_doc = dp.id_doc " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS dpe ON "
                + "ped.fid_dps_year = dpe.id_year AND ped.fid_dps_doc = dpe.id_doc AND ped.fid_dps_ety = dpe.id_ety " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS irp ON "
                + "dpe.fid_item_ref_n = irp.id_item "  
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS ip ON "
                + "dpe.fid_item = ip.id_item " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS up ON "
                + "dpe.fid_unit = up.id_unit " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS df ON "
                + "fac.fid_dps_year = df.id_year AND fac.fid_dps_doc = df.id_doc " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS dfe ON "
                + "fac.fid_dps_year = dfe.id_year AND fac.fid_dps_doc = dfe.id_doc AND fac.fid_dps_ety = dfe.id_ety "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON "
                + "df.fid_cur = c.id_cur "
                + "WHERE " + where;       
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_CODE, "Cen. Suministro"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "folio", "Folio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha RM"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "solicitante", "Solicitante"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "f_requerida", "Fecha requerida"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "req_pty", "Prioridad RM"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "f_compras", "Ult. fecha compras"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "req_gasto", "Concepto/gasto RM"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "f_cot", "Fecha cotización"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "prov_name", "Proveedor"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "cot_code", "Tipo doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "folio_cot", "Folio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "ref_cot", "Referencia cotización"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "f_cot_siie", "Fecha cotización"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp_cot", "Asociado de negocio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "ped_code", "Tipo doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "folio_ped", "Folio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "f_ped_siie", "Fecha pedido"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "ped_gasto", "Concepto/gasto pedido"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "f_ped_auth", "Fecha autorizado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "f_ped_entrega", "Fecha entrega"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, "Item"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "qty", "Cantidad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "symbol", "Unidad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_new", "Fecha añadido"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "fac_code", "Tipo doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "folio_fac", "Folio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "f_fac", "Fecha factura"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_stot", "Monto mon $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "f_cur", "Moneda"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "local_stot", "Monto $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "local_cur", "Moneda local"));
       
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_REQ);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_REQ_ETY);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_PROV_ENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_REQ_ST_LOG);
        moSuscriptionsSet.add(SModConsts.TRN_EST_REQ);
        moSuscriptionsSet.add(SModConsts.TRN_EST_REQ_ETY);
        moSuscriptionsSet.add(SModConsts.TRN_EST_REQ_REC);
        moSuscriptionsSet.add(SModConsts.TRN_DPS_MAT_REQ);
        moSuscriptionsSet.add(SModConsts.TRN_DPS);
        moSuscriptionsSet.add(SModConsts.TRN_DPS_ETY);
        moSuscriptionsSet.add(SModConsts.TRNU_MAT_REQ_PTY);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.ITMU_UNIT);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.CFGU_CUR);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}
