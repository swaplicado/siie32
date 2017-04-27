/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.SDbDps;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;
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
 * @author Sergio Flores
 */
public class SViewDeliveryQuery extends SGridPaneView implements ActionListener {

    private JButton mjDpsClose;
    private JButton mjDpsOpen;
    private SGridFilterDatePeriod moFilterDatePeriod;
    
    /**
     * @param client GUI client.
     * @param subtype View's subtype (constants defined in <code>sba.gui.util.SUtilConsts</code>: PROC and PROC_PEND), i.e., processed or procesing pending.
     * @param title View's GUI tab title.
     * @param params View's GUI mode provided as type (constants defined in <code>sba.gui.util.SUtilConsts</code>: QUERY_SUM and QUERY_DET), i.e., summary or detail.
     */
    public SViewDeliveryQuery(SGuiClient client, int subtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRN_DVY, subtype, title, params);
        setRowButtonsEnabled(!isProcessed(), false, false, false, false);
        jtbFilterDeleted.setEnabled(false);

        mjDpsClose = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_close.gif")), SUtilConsts.TXT_CLOSE, this);
        mjDpsClose.setEnabled(!isProcessed());
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjDpsClose);
        
        mjDpsOpen = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_open.gif")), SUtilConsts.TXT_OPEN, this);
        mjDpsOpen.setEnabled(isProcessed());
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjDpsOpen);
        
        if (isProcessed()) { // processed...
            moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
    }
    
    private boolean isProcessed() {
        return mnGridSubtype == SUtilConsts.PROC;
    }
    
    private boolean isSummary() {
        return mnGridMode == SUtilConsts.QRY_SUM;
    }
    
    private void changeDpsLink(SGridRowView gridRow, boolean status) {
        SDbDps dps = new SDbDps();
        SDbDps.Link link = dps.new Link(status, miClient.getSession().getUser().getPkUserId());

        try {
            dps.saveField(miClient.getSession().getStatement(), gridRow.getRowPrimaryKey(), SDbDps.FIELD_LINK, link);
            miClient.getSession().notifySuscriptors(mnGridType);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private void actionPerformedDpsClose() {
        if (mjDpsClose.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (miClient.showMsgBoxConfirm("¿" + SUtilConsts.TXT_CLOSE + " " + SUtilConsts.TXT_DOC.toLowerCase() + "?") == JOptionPane.YES_OPTION) {
                    changeDpsLink(gridRow, true);
                }
            }
        }
    }
    
    private void actionPerformedDpsOpen() {
        if (mjDpsOpen.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (miClient.showMsgBoxConfirm("¿" + SUtilConsts.TXT_OPEN + " " + SUtilConsts.TXT_DOC.toLowerCase() + "?") == JOptionPane.YES_OPTION) {
                    changeDpsLink(gridRow, false);
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String where = "";
        String having = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(isSummary() ? 2 : 3); // DPS' key or DPS Entry's key

        if (isProcessed()) { // processed...
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            if (filter != null) {
                where = SGridUtils.getSqlFilterDate("d.dt", (SGuiDate) filter);
            }
        }
        
        switch (mnGridSubtype) {
            case SUtilConsts.PROC:
                if (isSummary()) {
                    having = "(SUM(t2._net_qty) - _prc_qty_rem = 0 AND SUM(t2._net_orig_qty) - _prc_orig_qty_rem = 0) OR d.b_link = 1 ";
                }
                else {
                    having = "(t2._net_qty - _prc_qty_rem = 0 AND t2._net_orig_qty - _prc_orig_qty_rem = 0) OR d.b_link = 1 ";
                }
                break;
            case SUtilConsts.PROC_PEND:
                if (isSummary()) {
                    having = "(SUM(t2._net_qty) - _prc_qty_rem <> 0 OR SUM(t2._net_orig_qty) - _prc_orig_qty_rem <> 0) AND d.b_link = 0 ";
                }
                else {
                    having = "(t2._net_qty - _prc_qty_rem <> 0 OR t2._net_orig_qty - _prc_orig_qty_rem <> 0) AND d.b_link = 0 ";
                }
                break;
            default:
        }
        
        msSql = "SELECT de.id_year AS " + SDbConsts.FIELD_ID + "1, de.id_doc AS " + SDbConsts.FIELD_ID + "2, "
                + (isSummary() ? "" : "de.id_ety AS " + SDbConsts.FIELD_ID + "3, de.sort_pos, ")
                + "CONCAT(d.num_ser, IF(d.num_ser = '', '', '-'), d.num) AS " + SDbConsts.FIELD_CODE + ", "
                + "CONCAT(d.num_ser, IF(d.num_ser = '', '', '-'), d.num) AS " + SDbConsts.FIELD_NAME + ", "
                + "d.num_ser, d.num, d.num_ref, d.dt, d.b_link, d.ts_link, dt.code, b.bp, i.item, "
                + (isSummary() ? "" : "i.item, i.item_key, u.symbol, ou.symbol, ")
                
                + (isSummary() ?
                    "SUM(t2._net_qty) AS _net_qty, SUM(t2._net_orig_qty) AS _net_orig_qty, " : 
                    "t2._net_qty, t2._net_orig_qty, ")
                
                + "COALESCE((SELECT SUM(dds.qty) FROM trn_dps_dps_supply AS dds WHERE de.id_year = dds.id_des_year AND de.id_doc = dds.id_des_doc ), 0.0) AS _prc_qty, COALESCE((SELECT SUM(dds.orig_qty) FROM trn_dps_dps_supply AS dds WHERE de.id_year = dds.id_des_year AND de.id_doc = dds.id_des_doc), 0.0) AS _prc_orig_qty, "
                
                + (isSummary() ? 
                    "SUM(t2._net_qty) - COALESCE((SELECT SUM(dds.qty) FROM trn_dps_dps_supply AS dds WHERE de.id_year = dds.id_des_year AND de.id_doc = dds.id_des_doc), 0.0) AS _prc_qty_rem, SUM(t2._net_orig_qty) - COALESCE((SELECT SUM(dds.orig_qty) FROM trn_dps_dps_supply AS dds WHERE de.id_year = dds.id_des_year AND de.id_doc = dds.id_des_doc), 0.0) AS _prc_orig_qty_rem " : 
                    "t2._net_qty - COALESCE((SELECT SUM(dds.qty) FROM trn_dps_dps_supply AS dds WHERE de.id_year = dds.id_des_year AND de.id_doc = dds.id_des_doc), 0.0) AS _prc_qty_rem, t2._net_orig_qty - COALESCE((SELECT SUM(dds.orig_qty) FROM trn_dps_dps_supply AS dds WHERE de.id_year = dds.id_des_year AND de.id_doc = dds.id_des_doc), 0.0) AS _prc_orig_qty_rem ")
                
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON d.fid_bp_r = b.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON de.fid_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON de.fid_unit = u.id_unit "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS ou ON de.fid_orig_unit = ou.id_unit "
                + "INNER JOIN ("
                + "  SELECT t1.id_year, t1.id_doc, t1.id_ety, "
                + "  SUM(t1.qty) AS _net_qty, SUM(t1.orig_qty) AS _net_orig_qty "
                + "  FROM ("
                + "      SELECT de.id_year, de.id_doc, de.id_ety, "
                + "      de.qty, de.orig_qty "
                + "      FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "      INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "      WHERE d.fid_ct_dps = " + SModSysConsts.TRNS_CL_DPS_SAL_DOC[0] + " AND d.fid_cl_dps = " + SModSysConsts.TRNS_CL_DPS_SAL_DOC[1] + " AND d.fid_st_dps = " + SModSysConsts.TRNS_ST_DPS_EMITED + " AND "
                + "      d.b_del = 0 AND de.b_del = 0 "
                + "      UNION "
                + "      SELECT dda.id_dps_year AS id_year, dda.id_dps_doc AS id_doc, dda.id_dps_ety AS id_ety, "
                + "      dda.qty * -1 AS _qty, dda.orig_qty * -1 AS _orig_qty "
                + "      FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "      INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "      INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_ADJ) + " AS dda ON de.id_year = dda.id_dps_year AND de.id_doc = dda.id_dps_doc AND de.id_ety = dda.id_dps_ety "
                + "      WHERE d.fid_ct_dps = " + SModSysConsts.TRNS_CL_DPS_SAL_DOC[0] + " AND d.fid_cl_dps = " + SModSysConsts.TRNS_CL_DPS_SAL_DOC[1] + " AND d.fid_st_dps = " + SModSysConsts.TRNS_ST_DPS_EMITED + " AND "
                + "      d.b_del = 0 AND de.b_del = 0 "
                + "      ORDER BY id_year, id_doc, id_ety) AS t1 "
                + "  GROUP BY t1.id_year, t1.id_doc, t1.id_ety "
                + "  ORDER BY t1.id_year, t1.id_doc, t1.id_ety) AS t2 ON de.id_year = t2.id_year AND de.id_doc = t2.id_doc AND de.id_ety = t2.id_ety "
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "GROUP BY de.id_year, de.id_doc, "
                + (isSummary() ? "" : "de.id_ety, de.sort_pos, ")
                + "d.num_ser, d.num, d.num_ref, d.dt, d.ts_link, dt.code "
                + (isSummary() ? "" : ", i.item, i.item_key, u.symbol, ou.symbol, ")
                + (isSummary() ? "" : "t2._net_qty, t2._net_orig_qty ")
                + (having.isEmpty() ? "" : "HAVING " + having)
                + "ORDER BY dt.code, d.num_ser, CAST(d.num AS UNSIGNED), d.num_ref, d.dt, de.id_year, de.id_doc "
                + (isSummary() ? "" : ", de.sort_pos, de.id_ety ");
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView columnView = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "dt.code", SGridConsts.COL_TITLE_TYPE + " doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_CODE, "Folio doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "d.num_ref", "Referencia doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "d.dt", SGridConsts.COL_TITLE_DATE + " doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "b.bp", SGridConsts.COL_TITLE_NAME));
        
        if (mnGridMode == SUtilConsts.QRY_DET) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.item", SGridConsts.COL_TITLE_NAME + " ítem"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "i.item_key", SGridConsts.COL_TITLE_CODE + " ítem"));
        }
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "_net_orig_qty", "Cant. neta"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "_prc_orig_qty", "Cant. procesada"));
        
        if (mnGridMode == SUtilConsts.QRY_DET) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "ou.symbol", "Unidad"));
        }
        
        columnView = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "", "Avance %");
        columnView.getRpnArguments().add(new SLibRpnArgument("_prc_orig_qty", SLibRpnArgumentType.OPERAND));
        columnView.getRpnArguments().add(new SLibRpnArgument("_net_orig_qty", SLibRpnArgumentType.OPERAND));
        columnView.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.DIVISION, SLibRpnArgumentType.OPERATOR));
        gridColumnsViews.add(columnView);
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "_prc_orig_qty_rem", "Cant. pendiente"));
        
        if (mnGridMode == SUtilConsts.QRY_DET) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "ou.symbol", "Unidad"));
        }
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "d.b_link", "Cerrado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "d.ts_link", "Cierre"));
        
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.CFGU_CUR);
        moSuscriptionsSet.add(SModConsts.ITMU_UNIT);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.TRN_DPS);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
    
    @Override
    public void actionRowNew() {
        if (jbRowNew.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    moFormParams = new SGuiParams();
                    moFormParams.getParamsMap().put(SModConsts.TRN_DPS, gridRow.getRowPrimaryKey());
                    super.actionRowNew();
                    
                    ((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_DVY); // former framework data change notification
                }
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == mjDpsClose) {
                actionPerformedDpsClose();
            }
            else if (button == mjDpsOpen) {
                actionPerformedDpsOpen();
            }
        }
    }
}
