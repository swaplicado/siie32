/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.gui.grid.SGridFilterPanelFunctionalArea;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.trn.db.STrnUtils;
import erp.table.SFilterConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Sergio Flores
 */
public class SViewDelivery extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private SGridFilterPanelFunctionalArea moFilterFunctionalArea;
    private JButton mjPrint;
    
    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     * @param subtype View's subtype (constants defined in <code>sba.gui.util.SUtilConsts</code>: QUERY_SUM and QUERY_DET), i.e., summary or detail.
     */
    public SViewDelivery(SGuiClient client, int subtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRN_DVY, subtype, title, null);
        setRowButtonsEnabled(false, false, true, false, true);

        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);

        mjPrint = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT), SUtilConsts.TXT_PRINT, this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjPrint);
        
        moFilterFunctionalArea = new SGridFilterPanelFunctionalArea(miClient, this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFunctionalArea);
    }
    
    private boolean isSummary() {
        return mnGridSubtype == SUtilConsts.QRY_SUM;
    }
    
    private void actionPerformedPrint() {
        if (mjPrint.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    try {
                        STrnUtils.printDelivery(miClient, gridRow.getRowPrimaryKey()[0]);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(isSummary() ? 1 : 2);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        if (filter != null) {
            where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
        }
        
        filter = (String) (moFiltersMap.get(SFilterConstants.SETTING_FILTER_FUNC_AREA) == null ? null : moFiltersMap.get(SFilterConstants.SETTING_FILTER_FUNC_AREA).getValue());
        if (filter != null && ! ((String) filter).isEmpty()) {
            where += (where.isEmpty() ? "" : "AND ") + "d.fid_func IN ( " + filter + ") ";
        }
        
        msSql = "SELECT v.id_dvy AS " + SDbConsts.FIELD_ID + "1, "
                + (isSummary() ? "" : "ve.id_ety AS " + SDbConsts.FIELD_ID + "2, ")
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "CONCAT(d.num_ser, IF(d.num_ser = '', '', '-'), d.num) AS _doc_num, "
                + "d.num_ser, "
                + "d.num, "
                + "d.num_ref, "
                + "d.dt, "
                + "dt.code, "
                + "b.bp, "
                + (isSummary() ? "" : ""
                + "ve.qty, "
                + "ve.orig_qty, "
                + "CONCAT(o.num_ser, IF(o.num_ser = '', '', '-'), o.num) AS _ord_num, "
                + "o.num_ser, "
                + "o.num, "
                + "o.num_ref, "
                + "o.dt, "
                + "oe.sort_pos, "
                + "i.item_key, "
                + "i.item, "
                + "u.symbol, "
                + "ou.symbol, ")
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DVY) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON "
                + "v.fk_dps_year = d.id_year AND v.fk_dps_doc = d.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON "
                + "d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON "
                + "d.fid_bp_r = b.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (isSummary() ? "" : ""
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DVY_ETY) + " AS ve ON v.id_dvy = ve.id_dvy "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS o ON ve.fk_ord_year = o.id_year AND ve.fk_ord_doc = o.id_doc "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS oe ON ve.fk_ord_year = oe.id_year AND ve.fk_ord_doc = oe.id_doc AND ve.fk_ord_ety = oe.id_ety "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON oe.fid_item = i.id_item "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON oe.fid_unit = u.id_unit "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS ou ON oe.fid_orig_unit = ou.id_unit ")
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY v.num, v.id_dvy ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_NAME, "Folio"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "dt.code", SGridConsts.COL_TITLE_TYPE + " fac."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "_doc_num", "Folio fac."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "d.num_ref", "Referencia fac."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "d.dt", SGridConsts.COL_TITLE_DATE + " fac."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "b.bp", SGridConsts.COL_TITLE_NAME));
        if (!isSummary()) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, SDbConsts.FIELD_ID + "2", "# renglón"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "i.item_key", SGridConsts.COL_TITLE_CODE + " ítem"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.item", SGridConsts.COL_TITLE_NAME + " ítem"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "ve.orig_qty", "Cantidad"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "ou.symbol", "Unidad"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "_ord_num", "Folio ped."));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "o.num_ref", "Referencia ped."));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "o.dt", SGridConsts.COL_TITLE_DATE + " ped."));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "oe.sort_pos", "# renglón ped."));
        }
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.ITMU_UNIT);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.TRN_DPS);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == mjPrint) {
                actionPerformedPrint();
            }
        }
    }
}
