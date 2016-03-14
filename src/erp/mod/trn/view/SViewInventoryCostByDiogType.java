/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.trn.view;

import erp.data.SDataConstants;
import erp.lib.table.STableConstants;
import erp.mod.SModConsts;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateCutOff;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewInventoryCostByDiogType extends SGridPaneView implements ActionListener {

    private SGridFilterDateCutOff moFilterDateCutOff;

    public SViewInventoryCostByDiogType(SGuiClient client, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_STK_DIOG_TP, gridSubtype, title, params);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false, false, false, false, false);

        moFilterDateCutOff = new SGridFilterDateCutOff(miClient, this);
        moFilterDateCutOff.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_DATE, SLibTimeUtils.getEndOfYear(miClient.getSession().getCurrentDate()).getTime()));

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateCutOff);
    }

    private boolean showWarehouses() {
        return true;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

        }
        else if (e.getSource() instanceof javax.swing.JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) e.getSource();

        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + "s.id_year = " + SLibTimeUtils.digestYear((SGuiDate) filter)[0] + " AND " +
                        "s.dt <= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
            sql += (sql.isEmpty() ? "" : "AND ") + " s.dt <= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
        }

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "s.b_del = 0 ";
        }

        msSql = "SELECT "
                + "s.id_item AS " + SDbConsts.FIELD_ID + "1, "
                + "s.id_unit AS " + SDbConsts.FIELD_ID + "2, "
                + "i.item_key AS " + SDbConsts.FIELD_CODE + ", "
                + "i.item AS " + SDbConsts.FIELD_NAME + ", "
                + "u.symbol, "
                + "t.code, "
                + "t.tp_iog, "
                + (!showWarehouses() ? "" : "s.id_cob, s.id_wh, bpb.code, ent.code, sc.qty_min, sc.qty_max, sc.rop, "
                + "IF(SUM(s.mov_in - s.mov_out) <= sc.qty_min, " + STableConstants.ICON_VIEW_LIG_RED + ", IF(sc.qty_min < SUM(s.mov_in - s.mov_out) AND SUM(s.mov_in - s.mov_out) <= sc.rop, "  + STableConstants.ICON_VIEW_LIG_YEL + ", IF(SUM(s.mov_in - s.mov_out) > sc.rop, "  + STableConstants.ICON_VIEW_LIG_GRE + ", " + STableConstants.ICON_VIEW_LIG_WHI + "))) AS f_ico, ")
                + "SUM(s.mov_in) AS f_mov_i, "
                + "SUM(s.mov_out) AS f_mov_o, "
                + "SUM(s.mov_in - s.mov_out) AS f_stk, "
                + "SUM(s.debit) AS f_debit, "
                + "SUM(s.credit) AS f_credit, "
                + "SUM(s.debit - s.credit) AS f_stk_cost, "
                + "0 AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                // + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "'' AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "0 AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                //+ "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "'' AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON "
                + "s.id_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON "
                + "s.id_unit = u.id_unit "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_TP_IOG) + " AS t ON "
                + "s.fid_ct_iog = t.id_ct_iog AND s.fid_cl_iog = t.id_cl_iog AND s.fid_tp_iog = t.id_tp_iog "
                + (!showWarehouses() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpb ON "
                + "s.id_cob = bpb.id_bpb "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS ent ON s.id_cob = ent.id_cob AND s.id_wh = ent.id_ent ")
                + (!showWarehouses() ? "" :
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK_CFG) + " AS sc ON "
                + "sc.id_item = i.id_item AND sc.id_unit = u.id_unit AND sc.id_cob = bpb.id_bpb AND sc.id_wh = ent.id_ent ")
                + "WHERE " + sql + " "
                + "GROUP BY s.fid_ct_iog, s.fid_cl_iog, s.fid_tp_iog, " + (mnGridSubtype != SModConsts.TRNX_STK_WAH ? "s.id_item, s.id_unit, s.id_cob, s.id_wh " : "s.id_cob, s.id_wh ")
                + "HAVING f_stk <> 0 "
                + "ORDER BY s.fid_ct_iog, s.fid_cl_iog, s.fid_tp_iog, i.item_key, i.item, s.id_item, u.symbol, s.id_unit "
                + (!showWarehouses() ? "" : ", bpb.code, ent.code, s.id_cob, s.id_wh ") + " ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView gridColumnView = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "t.code", "Código"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "t.tp_iog", "Tipo movimiento"));

        if (mnGridSubtype != SModConsts.TRNX_STK_WAH) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_CODE, "Clave"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, "Ítem"));
        }

        if (showWarehouses()) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bpb.code", "Sucursal empresa"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "ent.code", "Almacén"));
        }

        if (mnGridSubtype != SModConsts.TRNX_STK_WAH) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_mov_i", "Entradas"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_mov_o", "Salidas"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_stk", "Existencias"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "u.symbol", "Unidad"));
        }

        gridColumnView = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_debit", "Cargos $");
        gridColumnView.setSumApplying(true);
        gridColumnsViews.add(gridColumnView);
        gridColumnView = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_credit", "Abonos $");
        gridColumnView.setSumApplying(true);
        gridColumnsViews.add(gridColumnView);
        gridColumnView = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_stk_cost", "Saldo $");
        gridColumnView.setSumApplying(true);
        gridColumnsViews.add(gridColumnView);

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_INV_VAL);
        moSuscriptionsSet.add(SModConsts.TRNX_STK_COST);
        moSuscriptionsSet.add(SDataConstants.ITMU_ITEM);
        moSuscriptionsSet.add(SDataConstants.ITMU_UNIT);
        moSuscriptionsSet.add(SDataConstants.TRN_LOT);
        moSuscriptionsSet.add(SDataConstants.TRN_DIOG);
        moSuscriptionsSet.add(SDataConstants.TRN_STK_CFG);
        moSuscriptionsSet.add(SDataConstants.TRNX_DPS_SUPPLY_PEND);
        moSuscriptionsSet.add(SDataConstants.TRNX_DPS_SUPPLY_PEND_ETY);
        moSuscriptionsSet.add(SDataConstants.TRNX_DPS_RETURN_PEND);
        moSuscriptionsSet.add(SDataConstants.TRNX_DPS_RETURN_PEND_ETY);
    }

}
