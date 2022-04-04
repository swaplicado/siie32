/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.gui.grid.SGridFilterPanelCompound;
import erp.mcfg.data.SDataParamsErp;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateCutOff;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Cesar Orozco
 */
public class SViewValCost extends SGridPaneView {
    
    private SGridFilterDateCutOff moFilterDateCutOff;
    private SGridFilterPanelCompound moFilterWarehouse;
    
    public SViewValCost(SGuiClient client, int subtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_INV_VAL_COST_QRY, subtype, title, null);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(false, false, false, false, false);
        
        moFilterDateCutOff = new SGridFilterDateCutOff(miClient, this);
        moFilterDateCutOff.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_DATE, SLibTimeUtils.getEndOfYear(miClient.getSession().getCurrentDate()).getTime()));
        moFilterWarehouse = new SGridFilterPanelCompound(miClient, this, SGridFilterPanelCompound.PNL_TP_COB_ENT, SModConsts.BPSU_BPB, SModConsts.CFGU_COB_ENT);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterWarehouse);

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateCutOff);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterWarehouse);
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();
        if (filter != null) {
            sql += (sql.isEmpty() ? "" : "AND ") + "s.id_year = " + SLibTimeUtils.digestYear((SGuiDate) filter)[0] + " AND " +
                        "s.dt <= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.BPSU_BPB)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "s.id_cob = " + ((int[]) filter)[0] + " ";
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.CFGU_COB_ENT)).getValue();
        if (filter != null && ((int[]) filter).length == 2) {
            sql += (sql.isEmpty() ? "" : "AND ") + "s.id_cob = " + ((int[]) filter)[0] + " AND s.id_wh = " + ((int[]) filter)[1] + " ";
        }
       msSql = "SELECT "
                + "s.id_item AS " + SDbConsts.FIELD_ID + "1, "
                + "s.id_unit AS " + SDbConsts.FIELD_ID + "2, "
                + "i.item_key AS " + SDbConsts.FIELD_CODE + ", "
                + "i.item AS " + SDbConsts.FIELD_NAME + ", "
                + "u.symbol, "
                + "u.unit, "
                + "SUM(s.mov_in) AS _mov_in, "
                + "SUM(s.mov_out) AS _mov_out, "
                + "SUM(s.mov_in - s.mov_out) AS _stk, "
                + "SUM(s.debit) AS _dbt, "
                + "SUM(s.credit) AS _cdt, "
                + "SUM(s.debit - s.credit) AS _cst, "
                + "(SUM(s.debit - s.credit)/SUM(s.mov_in - s.mov_out)) AS _unt, "
                + "i.prod_cost AS _prod, "
                + "(SUM((s.mov_in - s.mov_out))*i.prod_cost) AS _teor "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON s.id_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON s.id_unit = u.id_unit "
                + "WHERE s.b_del = 0 AND " + sql + " "
                + "GROUP BY s.id_item, s.id_unit "
                + "HAVING _stk <> 0 OR _cst <> 0 "
                + "ORDER BY "
                + (((SDataParamsErp) miClient.getSession().getConfigSystem()).getFkSortingItemTypeId() == SModSysConsts.CFGS_TP_SORT_KEY_NAME ? "i.item, i.item_key, " : "i.item_key, i.item, ")
                + "s.id_item, u.symbol, s.id_unit ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
         SGridColumnView column = null;
         ArrayList<SGridColumnView> columns = new ArrayList<SGridColumnView>();
         
         if (((SDataParamsErp) miClient.getSession().getConfigSystem()).getFkSortingItemTypeId() == SModSysConsts.CFGS_TP_SORT_KEY_NAME) {
                    columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME + " ítem"));
                    columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE + " ítem"));
                }
                else {
                    columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE + " ítem"));
                    columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME + " ítem"));
                }
         columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "_mov_in", "Entradas"));
         columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "_mov_out", "Salidas"));
         columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "_stk", "Existencias"));
         columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "u.symbol", "Unidad"));
         column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_dbt", "Cargos $");
         column.setSumApplying(true);
         columns.add(column);
         column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_cdt", "Abonos $");
         column.setSumApplying(true);
         columns.add(column);
         column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_cst", "Saldo $");
         column.setSumApplying(true);
         columns.add(column);
         column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_unt", "Valor unitario $");
         column.setSumApplying(true);
         columns.add(column);
         column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_prod", "Valor unitario teórico $");
         column.setSumApplying(true);
         columns.add(column);
         column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_teor", "Valor teórico $");
         column.setSumApplying(true);
         columns.add(column);
         return columns;
    }
}
