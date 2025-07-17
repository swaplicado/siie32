/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateRange;
import sa.lib.grid.SGridFilterValue;
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
public class SViewWarehouseConsumptionDetail extends SGridPaneView  {

    private final SGridFilterDateRange moFilterDateRange;
    
    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     */
    public SViewWarehouseConsumptionDetail(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRN_DVY, SLibConstants.UNDEFINED, title, null);
        setRowButtonsEnabled(false);

        moFilterDateRange = new SGridFilterDateRange(miClient, this);
        moFilterDateRange.initFilter(new Date[] {new SGuiDate(SGuiConsts.GUI_DATE_DATE, miClient.getSession().getCurrentDate().getTime()), new SGuiDate(SGuiConsts.GUI_DATE_DATE, miClient.getSession().getCurrentDate().getTime())});
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);

    }
    
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter = null;
        Date[] dateRange = null;

        moPaneSettings = new SGridPaneSettings(7);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DATE_RANGE)).getValue();
        if (filter != null) {
            dateRange = (Date[]) filter;
            where += SGridUtils.getSqlFilterDateRange("s.dt", dateRange);
        }
        
        String sqlPrice = "(SELECT pcl.* " 
                + "FROM itmu_price_comm_log AS pcl " 
                + "INNER JOIN ( " 
                + " SELECT p.id_item, p.id_unit, MAX(p.id_log) AS _max_id_log " 
                + " FROM itmu_price_comm_log AS p " 
                + " INNER JOIN ( " 
                + "  SELECT id_item, id_unit, MAX(dt) AS _max_dt " 
                + "  FROM itmu_price_comm_log " 
                + "  WHERE NOT b_del " 
                + "  GROUP BY id_item, id_unit " 
                + "  ORDER BY id_item, id_unit) AS t1 ON t1.id_item = p.id_item AND t1.id_unit = p.id_unit AND t1._max_dt = p.dt " 
                + " WHERE NOT p.b_del " 
                + " GROUP BY p.id_item, p.id_unit " 
                + " ORDER BY p.id_item, p.id_unit) AS t2 ON t2.id_item = pcl.id_item AND t2.id_unit = pcl.id_unit AND t2._max_id_log = pcl.id_log " 
                + "WHERE NOT pcl.b_del "  
                + "ORDER BY pcl.id_item, pcl.id_unit)";
        
        msSql = "SELECT " 
                + "s.id_year AS " + SDbConsts.FIELD_ID + "1, "
                + "s.id_item AS " + SDbConsts.FIELD_ID + "2, "
                + "s.id_unit AS " + SDbConsts.FIELD_ID + "3, "
                + "s.id_lot AS " + SDbConsts.FIELD_ID + "4, "
                + "s.id_cob AS " + SDbConsts.FIELD_ID + "5, "
                + "s.id_wh AS " + SDbConsts.FIELD_ID + "6, "
                + "s.id_mov AS " + SDbConsts.FIELD_ID + "7, "
                + "s.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "i.item AS " + SDbConsts.FIELD_NAME + ", "
                + "u.symbol AS " + SDbConsts.FIELD_CODE + ", "
                + "s.mov_out - s.mov_in mov, "
                + "pcl.price, "
                + "(s.mov_out - s.mov_in) * pcl.price val_mov, "
                + "IF(d.num_ser <> '', CONCAT(d.num_ser, '-', d.num), d.num) folio_doc, "
                + "ce.ent, "
                + "ce.code almacen, "
                + "ti.tp_iog, " 
                + "mus.name supr_mant, "
                + "bp.bp usr_mant, "
                + "r.num req, "
                + "r.dt fecha_req, " 
                + "IF(ire.id_item IS NULL, ir.item, ire.item) concepto_gasto, "
                + "mce.name consumo, "
                + "mcse.name subconsumo, "
                + "cc.id_cc, "
                + "cc.cc " 
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON "
                + "s.id_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON "
                + "s.id_unit = u.id_unit " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS ce ON "
                + "s.id_cob = ce.id_cob AND s.id_wh = ce.id_ent " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_TP_IOG) + " AS ti ON "
                + "s.fid_ct_iog = ti.id_ct_iog AND s.fid_cl_iog = ti.id_cl_iog AND s.fid_tp_iog = ti.id_tp_iog " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON "
                + "s.fid_diog_year = de.id_year AND s.fid_diog_doc = de.id_doc AND s.fid_diog_ety = de.id_ety " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d ON "
                + "de.id_year = d.id_year AND de.id_doc = d.id_doc " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAINT_USER_SUPV) + " AS mus ON "
                + "s.fid_maint_user_supv = mus.id_maint_user_supv " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS r ON "
                + "s.fid_mat_req_n = r.id_mat_req " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " AS re ON "
                + "s.fid_mat_req_n = re.id_mat_req AND s.fid_mat_req_ety_n = re.id_ety " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS ir ON "
                + "r.fk_item_ref_n = ir.id_item " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS ire ON "
                + "re.fk_item_ref_n = ire.id_item " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAINT_USER) + " AS mu ON "
                + "s.fid_maint_user_n = mu.id_maint_user " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                + "mu.id_maint_user = bp.id_bp " 
                + "LEFT JOIN trn_diog_ety_cons_ent_cc AS dece ON "
                + "de.id_year = dece.fid_diog_year AND de.id_doc = dece.fid_diog_doc AND de.id_ety = dece.fid_diog_ety " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT) + " AS mce ON "
                + "dece.fid_mat_sub_cons_ent = mce.id_mat_cons_ent " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT) + " AS mcse ON "
                + "dece.fid_mat_sub_cons_ent = mcse.id_mat_cons_ent AND dece.fid_mat_sub_cons_sub_ent = mcse.id_mat_cons_subent " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cc ON "
                + "dece.fid_cc = cc.pk_cc " 
                + "LEFT JOIN " + sqlPrice + " AS pcl ON "
                + "s.id_item = pcl.id_item AND s.id_unit = pcl.id_unit AND NOT pcl.b_del " 
                + "WHERE " + where + " " 
                + "AND NOT s.b_del AND NOT d.b_del AND NOT de.b_del";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, "Ítem"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, SDbConsts.FIELD_CODE, "Unidad"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "mov", "Consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "pcl.price", "Precio com. u."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "val_mov", "Precio consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "folio_doc", "Folio doc."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "almacen", "Almacén"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "tp_iog", "Tipo movimiento"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "supr_mant", "Supervisor mantto."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "usr_mant", "Usr. mantto."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "fecha_req", "Fecha requisición"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "req", "Folio requisición"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "concepto_gasto", "Concepto/gasto"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "consumo", "Centro consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "subconsumo", "Subcentro consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "id_cc", "Número centro costro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "cc", "Centro costo"));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.ITMU_UNIT);
        moSuscriptionsSet.add(SModConsts.CFGU_COB_ENT);
        moSuscriptionsSet.add(SModConsts.TRNS_TP_IOG);
        moSuscriptionsSet.add(SModConsts.TRN_DIOG_ETY);
        moSuscriptionsSet.add(SModConsts.TRN_DIOG);
        moSuscriptionsSet.add(SModConsts.TRN_MAINT_USER_SUPV);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_REQ);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_REQ_ETY);
        moSuscriptionsSet.add(SModConsts.TRN_MAINT_USER);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_ENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_SUBENT);
        moSuscriptionsSet.add(SModConsts.FIN_CC);
    }
}
