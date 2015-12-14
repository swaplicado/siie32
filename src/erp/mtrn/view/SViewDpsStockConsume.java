/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.table.STabFilterDate;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;
import erp.table.SFilterConstants;
import erp.table.STabFilterCompanyBranchEntity;
import java.util.Date;
import javax.swing.JButton;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;

/**
 *
 * @author Juan Barajas
 */
public class SViewDpsStockConsume extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STableColumn[] maoTableColumns;
    private erp.lib.table.STabFilterDate moTabFilterDate;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.table.STabFilterCompanyBranchEntity moTabFilterCompanyBranchEntity;

    public SViewDpsStockConsume(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_STK_COMSUME, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        maoTableColumns = null;

        moTabFilterDate = new STabFilterDate(miClient, this, SLibTimeUtilities.getEndOfMonth(miClient.getSessionXXX().getWorkingDate()));
        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterCompanyBranchEntity = new STabFilterCompanyBranchEntity(miClient, this, SDataConstantsSys.CFGS_CT_ENT_WH);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        addTaskBarUpperComponent(moTabFilterDate);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranchEntity);

        moTablePane.reset();

        maoTableColumns = new STableColumn[9];

        i = 0;
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_lev", "Tipo nivel", STableConstants.WIDTH_ITEM_KEY);
        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
        }
        else {
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
        }

        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "", "Rotación mes anterior", STableConstants.WIDTH_QUANTITY_2X);
        maoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitaryFixed4());
        maoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_inv_i", SLibRpnArgumentType.OPERAND));
        maoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_mov_i", SLibRpnArgumentType.OPERAND));
        maoTableColumns[i].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.ADDITION, SLibRpnArgumentType.OPERATOR));
        maoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_stock", SLibRpnArgumentType.OPERAND));
        maoTableColumns[i].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.DIVISION, SLibRpnArgumentType.OPERATOR));
        maoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_const", SLibRpnArgumentType.OPERAND));
        maoTableColumns[i++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty", "Cant." + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? "compra " : "venta " ) + "neta mes anterior", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_min", "Mínimo ", STableConstants.WIDTH_QUANTITY_2X);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_max", "Máximo ", STableConstants.WIDTH_QUANTITY_2X);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stock", "Existencias", STableConstants.WIDTH_QUANTITY_2X);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);


        for (i = 0; i < maoTableColumns.length; i++) {
            moTablePane.addTableColumn(maoTableColumns[i]);
        }

        moTablePane.createTable(this);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.ITMU_TP_LEV);

        populateTable();
    }

    private boolean isPurchase() {
        boolean isPurchase = false;

        if (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR) {
            isPurchase = true;
        }

        return isPurchase;
    }

    @Override
    public void createSqlQuery() {
        int[] key = null;
        int nYearPrevPer = 0;
        Date tDateEnd = null;
        Date tDateBegin = null;
        Date tDatePrevPerBegin = null;
        Date tDatePrevPerEnd = null;
        String sqlWhere = "";
        String sqlHaving = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlHaving = "HAVING f_stock <> 0 ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                tDateEnd = (Date) setting.getSetting();
                tDateBegin = (Date) SLibTimeUtilities.getBeginOfMonth(tDateEnd);
                tDatePrevPerBegin = SLibTimeUtilities.getBeginOfMonth(SLibTimeUtilities.addDate(tDateBegin, 0, - 1, 0));
                tDatePrevPerEnd = SLibTimeUtilities.getEndOfMonth(SLibTimeUtilities.addDate(tDateBegin, 0, -1, 0));
                nYearPrevPer = SLibTimeUtilities.digestYear(tDatePrevPerBegin)[0];
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "s.id_year = " + SLibTimeUtilities.digestYear(tDateEnd)[0] + " AND " +
                        "s.dt <= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tDateEnd) + "' ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB_ENT) {
                key = (int[]) setting.getSetting();
                if (key != null) {
                    if (key[0] != SLibConstants.UNDEFINED) {
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "s.id_cob = " + key[0] + " ";
                    }
                    if (key[1] != SLibConstants.UNDEFINED) {
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "s.id_wh = " + key[1] + " ";
                    }
                }
            }
        }

        msSql = "SELECT t.id_item, t.id_unit, item_key, item, symbol, t.id_cob, t.id_wh, id_tp_lev, tp_lev, f_inv_i, f_mov_i, f_stock, f_qty, 1 AS f_const, " +
                "SUM(cf.qty_min) AS f_min, SUM(cf.qty_max) AS f_max " +
                "FROM ( " +
                "SELECT s.id_item, s.id_unit, i.item_key, i.item, u.symbol, s.id_cob, s.id_wh, tl.id_tp_lev, tl.tp_lev," +
                "COALESCE(SUM(CASE WHEN s.dt < '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tDatePrevPerBegin) + "' THEN s.mov_in - s.mov_out ELSE 0 END), 0) AS f_inv_i, " +
                "COALESCE(SUM(CASE WHEN s.dt >= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tDatePrevPerBegin) + "' AND s.dt <= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tDatePrevPerEnd) + "' THEN s.mov_in ELSE 0 END), 0) AS f_mov_i, " +
                "COALESCE(SUM(CASE WHEN s.dt <= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tDatePrevPerEnd) + "' THEN s.mov_in - s.mov_out ELSE 0 END), 0) AS f_stock, " +
                "COALESCE(COALESCE(SUM(CASE WHEN (" +
                "s.fid_ct_iog = " + (isPurchase() ? SDataConstantsSys.TRNS_TP_IOG_IN_PUR_PUR[0] : SDataConstantsSys.TRNS_TP_IOG_OUT_SAL_SAL[0]) + " " +
                "AND s.fid_cl_iog = " + (isPurchase() ? SDataConstantsSys.TRNS_TP_IOG_IN_PUR_PUR[1] : SDataConstantsSys.TRNS_TP_IOG_OUT_SAL_SAL[1]) + " " +
                "AND s.fid_tp_iog = " + (isPurchase() ? SDataConstantsSys.TRNS_TP_IOG_IN_PUR_PUR[2] : SDataConstantsSys.TRNS_TP_IOG_OUT_SAL_SAL[2]) + " " +
                ") AND " +
                "(s.id_year = " + nYearPrevPer + " AND s.dt >= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tDatePrevPerBegin) + "' AND s.dt <= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tDatePrevPerEnd) + "') THEN " + (isPurchase() ? "s.mov_in " : "s.mov_out ") + "ELSE 0 END), 0) - " +
                "COALESCE(SUM(CASE WHEN (" +
                "s.fid_ct_iog = " + (isPurchase() ? SDataConstantsSys.TRNS_TP_IOG_OUT_PUR_PUR[0] : SDataConstantsSys.TRNS_TP_IOG_IN_SAL_SAL[0]) + " " +
                "AND s.fid_cl_iog = " + (isPurchase() ? SDataConstantsSys.TRNS_TP_IOG_OUT_PUR_PUR[1] : SDataConstantsSys.TRNS_TP_IOG_IN_SAL_SAL[1]) + " " +
                "AND s.fid_tp_iog = " + (isPurchase() ? SDataConstantsSys.TRNS_TP_IOG_OUT_PUR_PUR[2] : SDataConstantsSys.TRNS_TP_IOG_IN_SAL_SAL[2]) + " " +
                ") AND " +
                "(s.id_year = " + nYearPrevPer + " AND s.dt >= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tDatePrevPerBegin) + "' AND s.dt <= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tDatePrevPerEnd) + "') THEN " + (isPurchase() ? "s.mov_out " : "s.mov_in ") + "ELSE 0 END), 0), 0) AS f_qty " +
                "FROM trn_stk AS s " +
                "INNER JOIN erp.itmu_item AS i ON s.id_item = i.id_item AND i.b_inv = TRUE AND i.b_del = 0 " +
                "INNER JOIN erp.itmu_unit AS u ON s.id_unit = u.id_unit " +
                "INNER JOIN erp.itmu_tp_lev AS tl ON i.fid_tp_lev = tl.id_tp_lev " +
                "WHERE s.b_del = 0 AND " + sqlWhere +
                "GROUP BY s.id_item, s.id_unit, i.item_key, i.item, u.symbol, tl.id_tp_lev, tl.tp_lev " +
                sqlHaving +
                "ORDER BY s.id_item, s.id_unit, i.item_key, i.item, u.symbol, tl.id_tp_lev, tl.tp_lev " +
                ") AS t " +
                "INNER JOIN trn_stk_cfg AS cf ON t.id_item = cf.id_item AND t.id_unit = cf.id_unit AND " +
                "t.id_cob = cf.id_cob " +
                (key[1] == SLibConstants.UNDEFINED ? "" : "AND t.id_wh = cf.id_wh ") +
                "GROUP BY t.id_item, t.id_unit, t.item_key, t.item, t.symbol, t.id_tp_lev, t.tp_lev " +
                "ORDER BY t.item_key, t.item, t.symbol, t.id_tp_lev, t.tp_lev; ";
    }

    public void actionRefresh() {
        createSqlQuery();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {

        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {

        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
}
