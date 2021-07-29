/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDatePeriodRange;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;
import erp.mod.trn.db.STrnConsts;
import erp.table.SFilterConstants;
import erp.table.STabFilterCurrency;
import erp.table.STabFilterFunctionalArea;
import erp.table.STabFilterRelatedParts;
import erp.table.STabFilterUnitType;

/**
 *
 * @author Alfonso Flores, Edwin Carmona, Sergio Flores
 */
public class SViewQueryGlobal extends erp.lib.table.STableTab {

    private erp.lib.table.STableColumn[] maoTableColumns;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private erp.table.STabFilterUnitType moTabFilterUnitType;
    private erp.table.STabFilterCurrency moTabFilterCurrency;
    private erp.table.STabFilterRelatedParts moTabFilterRelatedParts;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;

    private boolean mbIsLocalCurrency = false;

    public SViewQueryGlobal(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_QRY, auxType01);
        initComponents();
    }

    private void initComponents() {
        maoTableColumns = null;
        
        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);
        moTabFilterUnitType = new STabFilterUnitType(miClient, this);
        moTabFilterCurrency = new STabFilterCurrency(miClient, this);
        moTabFilterRelatedParts = new STabFilterRelatedParts(miClient, this);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        addTaskBarUpperComponent(moTabFilterDatePeriodRange);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterUnitType);
        addTaskBarUpperComponent(moTabFilterCurrency);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterRelatedParts);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterFunctionalArea);

        setIsSummaryApplying(true);

        populateTable();
    }

    public void renderTableColumns() {
        int i = 0;

        moTablePane.reset();

        maoTableColumns = new STableColumn[11];

        i = 0;
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb", "Sucursal empresa", 150);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_r", "Total $", STableConstants.WIDTH_VALUE_2X);
        if (mbIsLocalCurrency) {
            maoTableColumns[i].setSumApplying(true);
        }
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_adj_r", "Devs. $", STableConstants.WIDTH_VALUE_2X);
        if (mbIsLocalCurrency) {
            maoTableColumns[i].setSumApplying(true);
        }
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_adj_d", "Descs. $", STableConstants.WIDTH_VALUE_2X);
        if (mbIsLocalCurrency) {
            maoTableColumns[i].setSumApplying(true);
        }
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_net", "Total neto $", STableConstants.WIDTH_VALUE_2X);
        if (mbIsLocalCurrency) {
            maoTableColumns[i].setSumApplying(true);
        }
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);

        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty", "Cant.", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_r", "Cant. devs.", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_net", "Cant. neta", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_avg_price", "Precio promedio $", STableConstants.WIDTH_VALUE_UNITARY);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());

        for (i = 0; i < maoTableColumns.length; i++) {
            moTablePane.addTableColumn(maoTableColumns[i]);
        }

        moTablePane.createTable(this);
    }

    private boolean isPurchase() {
        boolean isPurchase = false;

        if (mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT) {
            isPurchase = true;
        }

        return isPurchase;
    }

    private java.lang.String createColumnsUnits(int n) {
        String columnsUnit = "";
        String columnStot = (mbIsLocalCurrency ? "e.stot_r" : "e.stot_cur_r");

        switch (n) {
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY:
                columnsUnit = "COALESCE(SUM(e.qty), 0) AS f_qty, " +
                        "0 AS f_qty_r, COALESCE(SUM(e.qty), 0) AS f_qty_net, " +
                        "COALESCE(COALESCE(SUM(" + columnStot + "), 0) / COALESCE(SUM(e.qty), 0), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN:
                columnsUnit = "COALESCE(SUM(e.len), 0) AS f_qty, " +
                        "0 AS f_qty_r, COALESCE(SUM(e.len), 0) AS f_qty_net, " +
                        "COALESCE(COALESCE(SUM(" + columnStot + "), 0) / COALESCE(SUM(e.len), 0), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF:
                columnsUnit = "COALESCE(SUM(e.surf), 0) AS f_qty, " +
                        "0 AS f_qty_r, COALESCE(SUM(e.surf), 0) AS f_qty_net, " +
                        "COALESCE(COALESCE(SUM(" + columnStot + "), 0) / COALESCE(SUM(e.surf), 0), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_VOL:
                columnsUnit = "COALESCE(SUM(e.vol), 0) AS f_qty, " +
                        "0 AS f_qty_r, COALESCE(SUM(e.vol), 0) AS f_qty_net, " +
                        "COALESCE(COALESCE(SUM(" + columnStot + "), 0) / COALESCE(SUM(e.vol), 0), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS:
                columnsUnit = "COALESCE(SUM(e.mass), 0) AS f_qty, " +
                        "0 AS f_qty_r, COALESCE(SUM(e.mass), 0) AS f_qty_net, " +
                        "COALESCE(COALESCE(SUM(" + columnStot + "), 0) / COALESCE(SUM(e.mass), 0), 0) AS f_avg_price ";
                break;
            default:
        }

        return columnsUnit;
    }

    private java.lang.String createColumnsUnitsSum(int n) {
        String columnsUnit = "";

        switch (n) {
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY:
                columnsUnit = "SUM(f_qty) AS f_qty, SUM(f_qty_r) AS f_qty_r, SUM(f_qty_net) AS f_qty_net, (SUM(f_stot_net)/SUM(f_qty_net)) AS f_avg_price";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN:
                columnsUnit = "SUM(f_qty) AS f_qty, SUM(f_qty_r) AS f_qty_r, SUM(f_qty_net) AS f_qty_net, (SUM(f_stot_net)/SUM(f_qty_net)) AS f_avg_price";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF:
                columnsUnit = "SUM(f_qty) AS f_qty, SUM(f_qty_r) AS f_qty_r, SUM(f_qty_net) AS f_qty_net, (SUM(f_stot_net)/SUM(f_qty_net)) AS f_avg_price";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_VOL:
                columnsUnit = "SUM(f_qty) AS f_qty, SUM(f_qty_r) AS f_qty_r, SUM(f_qty_net) AS f_qty_net, (SUM(f_stot_net)/SUM(f_qty_net)) AS f_avg_price";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS:
                columnsUnit = "SUM(f_qty) AS f_qty, SUM(f_qty_r) AS f_qty_r, SUM(f_qty_net) AS f_qty_net, (SUM(f_stot_net)/SUM(f_qty_net)) AS f_avg_price";
                break;
            default:
        }

        return columnsUnit;
    }

    private java.lang.String createColumnsUnitsRet(int n) {
        String columnsUnit = "";
        String stot = (mbIsLocalCurrency ? "e.stot_r" : "e.stot_cur_r");

        switch (n) {
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY:
                columnsUnit = ", 0 AS f_qty, " +
                        "COALESCE(SUM(e.qty), 0) AS f_qty_r, " +
                        "0 - COALESCE(SUM(e.qty), 0) AS f_qty_net, " +
                        "COALESCE((0 - COALESCE(SUM(" + stot + "), 0) / 0 - COALESCE(SUM(e.qty), 0)), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN:
                columnsUnit = ", 0 AS f_qty, " +
                        "COALESCE(SUM(e.len), 0) AS f_qty_r, " +
                        "0 - COALESCE(SUM(e.len), 0) AS f_qty_net, " +
                        "COALESCE((0 - COALESCE(SUM(" + stot + "), 0) / 0 - COALESCE(SUM(e.len), 0)), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF:
                columnsUnit = ", 0 AS f_qty, " +
                        "COALESCE(SUM(e.surf), 0) AS f_qty_r, " +
                        "0 - COALESCE(SUM(e.surf), 0) AS f_qty_net, " +
                        "COALESCE((0 - COALESCE(SUM(" + stot + "), 0) / 0 - COALESCE(SUM(e.surf), 0)), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_VOL:
                columnsUnit = ", 0 AS f_qty, " +
                        "COALESCE(SUM(e.vol), 0) AS f_qty_r, " +
                        "0 - COALESCE(SUM(e.vol), 0) AS f_qty_net, " +
                        "COALESCE((0 - COALESCE(SUM(" + stot + "), 0) / 0 - COALESCE(SUM(e.vol), 0)), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS:
                columnsUnit = ", 0 AS f_qty, " +
                        "COALESCE(SUM(e.mass), 0) AS f_qty_r, " +
                        "0 - COALESCE(SUM(e.mass), 0) AS f_qty_net, " +
                        "COALESCE((0 - COALESCE(SUM(" + stot + "), 0) / 0 - COALESCE(SUM(e.mass), 0)), 0) AS f_avg_price ";
                break;
            default:
        }

        return columnsUnit;
    }

    private java.lang.String createColumnsUnitsDis(int n) {
        String columnsUnit = "";

        switch (n) {
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY:
                columnsUnit = ", 0 AS f_qty, 0 AS f_qty_r, 0 AS f_qty_net, 0 AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN:
                columnsUnit = ", 0 AS f_qty, 0 AS f_qty_r, 0 AS f_qty_net, 0 AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF:
                columnsUnit = ", 0 AS f_qty, 0 AS f_qty_r, 0 AS f_qty_net, 0 AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_VOL:
                columnsUnit = ", 0 AS f_qty, 0 AS f_qty_r, 0 AS f_qty_net, 0 AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS:
                columnsUnit = ", 0 AS f_qty, 0 AS f_qty_r, 0 AS f_qty_net, 0 AS f_avg_price ";
                break;
            default:
        }

        return columnsUnit;
    }

    @Override
    public void createSqlQuery() {
        java.util.Date[] range = null;
        boolean withRelatedParts = false;
        int typeUnitTotal = 0;
        String sqlDatePeriod = "";
        String sqlFunctAreas = "";
        String dateInit = "";
        String dateEnd = "";
        String sqlColumnsUnit = "";
        String columnStot = "";
        String sqlCurrency = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
           setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
           if (setting.getType() == SFilterConstants.SETTING_FILTER_CURRENCY) {
                mbIsLocalCurrency = ((Integer)setting.getSetting()) == STabFilterCurrency.TP_SYSTEM_CURRENCY;
            }
        }

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                range = (java.util.Date[])setting.getSetting();
                dateInit = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]);
                dateEnd = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]);
                sqlDatePeriod += " AND doc.dt BETWEEN '" + dateInit + "' AND '" + dateEnd + "' ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    sqlFunctAreas += "AND doc.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_UNIT_TP) {
                typeUnitTotal = (Integer)setting.getSetting();
                sqlColumnsUnit = createColumnsUnits(typeUnitTotal);
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_REL_PARTY) {
                withRelatedParts = ((Integer)setting.getSetting()) == STrnConsts.TRN_BPS_WITH_REL_PARTY;
            }
        }
        
        renderTableColumns();
        
        columnStot = (mbIsLocalCurrency ? "e.stot_r" : "e.stot_cur_r");
        sqlCurrency = (mbIsLocalCurrency ? "" : ", cur_key ");

        msSql = "SELECT bpb, SUM(f_stot_r) AS f_stot_r, SUM(f_adj_r) AS f_adj_r, SUM(f_adj_d) AS f_adj_d, SUM(f_stot_net) AS f_stot_net, symbol, cur_key, " +
                createColumnsUnitsSum(typeUnitTotal) + " FROM (" +
                "(SELECT cob.bpb, COALESCE(SUM(" + columnStot + "), 0) AS f_stot_r, " +
                "0 AS f_adj_r, 0 AS f_adj_d, " +
                "COALESCE(SUM(" + columnStot + "), 0) AS f_stot_net, " +
                sqlColumnsUnit + ", (SELECT unit_base FROM erp.itmu_tp_unit WHERE id_tp_unit = " +
                (typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY ? SDataConstantsSys.ITMU_TP_UNIT_QTY :
                    typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN ? SDataConstantsSys.ITMU_TP_UNIT_LEN :
                        typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS ? SDataConstantsSys.ITMU_TP_UNIT_MASS :
                            typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF ? SDataConstantsSys.ITMU_TP_UNIT_SURF :
                                SDataConstantsSys.ITMU_TP_UNIT_VOL) +
                ") AS symbol, " +
                "(SELECT cur_key FROM erp.cfgu_cur WHERE id_cur = " + (mbIsLocalCurrency ? miClient.getSessionXXX().getParamsErp().getFkCurrencyId() : "doc.fid_cur") + ") AS cur_key " +
                "FROM trn_dps_ety AS e " +
                "INNER JOIN trn_dps AS doc ON " +
                "e.id_year = doc.id_year AND e.id_doc = doc.id_doc " +
                "INNER JOIN erp.bpsu_bp AS bp ON " +
                "doc.fid_bp_r = bp.id_bp " +
                "INNER JOIN erp.bpsu_bpb AS cob ON " +
                "doc.fid_cob = cob.id_bpb " +
                "WHERE e.b_del = FALSE AND doc.b_del = FALSE " +
                "AND doc.fid_ct_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0]) + " " +
                "AND doc.fid_cl_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1]) + " " +
                "AND doc.fid_tp_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2]) + " " +
                "AND doc.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND doc.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " " +
                (withRelatedParts ? "" : " AND bp.b_att_rel_pty = 0 ") +
                sqlDatePeriod + sqlFunctAreas + "GROUP BY cob.bpb" + sqlCurrency + " HAVING f_stot_net <> 0 OR f_qty_net <> 0 " +
                "ORDER BY cob.bpb" + sqlCurrency + ") " +
                "UNION " +
                "(SELECT cob.bpb, 0 AS f_stot_r, COALESCE(SUM(" + columnStot + "), 0) AS f_adj_r, 0 AS f_adj_d, 0 - COALESCE(SUM(" + columnStot + "), 0) AS f_stot_net " +
                createColumnsUnitsRet(typeUnitTotal) + ", (SELECT unit_base FROM erp.itmu_tp_unit WHERE id_tp_unit = " +
                (typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY ? SDataConstantsSys.ITMU_TP_UNIT_QTY :
                    typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN ? SDataConstantsSys.ITMU_TP_UNIT_LEN :
                        typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS ? SDataConstantsSys.ITMU_TP_UNIT_MASS :
                            typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF ? SDataConstantsSys.ITMU_TP_UNIT_SURF :
                                SDataConstantsSys.ITMU_TP_UNIT_VOL) +
                ") AS symbol , " +
                "(SELECT cur_key FROM erp.cfgu_cur WHERE id_cur = " + (mbIsLocalCurrency ? miClient.getSessionXXX().getParamsErp().getFkCurrencyId() : "doc.fid_cur") + ") AS cur_key " +
                "FROM trn_dps_ety AS e " +
                "INNER JOIN trn_dps AS doc ON " +
                "e.id_year = doc.id_year AND e.id_doc = doc.id_doc " +
                "INNER JOIN erp.bpsu_bp AS bp ON " +
                "doc.fid_bp_r = bp.id_bp " +
                "INNER JOIN erp.bpsu_bpb AS cob ON " +
                "doc.fid_cob = cob.id_bpb " +
                "INNER JOIN trn_dps_dps_adj AS j ON " +
                "e.id_year = j.id_adj_year AND e.id_doc = j.id_adj_doc AND e.id_ety = j.id_adj_ety " +
               "INNER JOIN trn_dps_ety AS o ON " +
                "j.id_dps_year = o.id_year AND j.id_dps_doc = o.id_doc AND j.id_dps_ety = o.id_ety " +
                "INNER JOIN erp.itmu_item AS i ON " +
                "o.fid_item = i.id_item " +
                "WHERE e.b_del = FALSE AND doc.b_del = FALSE " +
                "AND doc.fid_ct_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[0]) + " " +
                "AND doc.fid_cl_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[1]) + " " +
                "AND doc.fid_tp_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[2]) + " " +
                "AND doc.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND doc.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " " +
                (withRelatedParts ? "" : " AND bp.b_att_rel_pty = 0 ") +
                "AND e.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " " +
                sqlDatePeriod + sqlFunctAreas + "GROUP BY cob.bpb" + sqlCurrency + " HAVING f_stot_net <> 0 OR f_qty_net <> 0 " +
                "ORDER BY cob.bpb" + sqlCurrency + ") " +
                "UNION " +
                "(SELECT cob.bpb, 0 AS f_stot_r, 0 AS f_adj_r, COALESCE(SUM(" + columnStot + "), 0) AS f_adj_d, 0 - COALESCE(SUM(" + columnStot + "), 0) AS f_stot_net " +
                createColumnsUnitsDis(typeUnitTotal) + ", (SELECT unit_base FROM erp.itmu_tp_unit WHERE id_tp_unit = " +
                (typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY ? SDataConstantsSys.ITMU_TP_UNIT_QTY :
                    typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN ? SDataConstantsSys.ITMU_TP_UNIT_LEN :
                        typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS ? SDataConstantsSys.ITMU_TP_UNIT_MASS :
                            typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF ? SDataConstantsSys.ITMU_TP_UNIT_SURF :
                                SDataConstantsSys.ITMU_TP_UNIT_VOL) +
                ") AS symbol, " +
                "(SELECT cur_key FROM erp.cfgu_cur WHERE id_cur = " + (mbIsLocalCurrency ? miClient.getSessionXXX().getParamsErp().getFkCurrencyId() : "doc.fid_cur") + ") AS cur_key " +
                "FROM trn_dps_ety AS e " +
                "INNER JOIN trn_dps AS doc ON " +
                "e.id_year = doc.id_year AND e.id_doc = doc.id_doc " +
                "INNER JOIN erp.bpsu_bp AS bp ON " +
                "doc.fid_bp_r = bp.id_bp " +
                "INNER JOIN erp.bpsu_bpb AS cob ON " +
                "doc.fid_cob = cob.id_bpb " +
                "INNER JOIN trn_dps_dps_adj AS j ON " +
                "e.id_year = j.id_adj_year AND e.id_doc = j.id_adj_doc AND e.id_ety = j.id_adj_ety " +
                "INNER JOIN trn_dps_ety AS o ON " +
                "j.id_dps_year = o.id_year AND j.id_dps_doc = o.id_doc AND j.id_dps_ety = o.id_ety " +
                "INNER JOIN erp.itmu_item AS i ON " +
                "o.fid_item = i.id_item " +
                "WHERE e.b_del = FALSE AND doc.b_del = FALSE " +
                "AND doc.fid_ct_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[0]) + " " +
                "AND doc.fid_cl_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[1]) + " " +
                "AND doc.fid_tp_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[2]) + " " +
                "AND doc.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND doc.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " " +
                (withRelatedParts ? "" : " AND bp.b_att_rel_pty = 0 ") +
                "AND e.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC + " " +
                sqlDatePeriod + sqlFunctAreas + " GROUP BY cob.bpb" + sqlCurrency + " HAVING f_stot_net <> 0 OR f_qty_net <> 0 " +
                /*(typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY ? " f_qty_net <> 0 " :
                            typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN ? " f_len_net <> 0 " :
                                typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF ? " f_surf_net <> 0 " :
                                    typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_VOL ? " f_vol_net <> 0 " :
                                        " f_mass_net <> 0 ") +*/
                "ORDER BY cob.bpb" + sqlCurrency +
                "))" +
                "AS t " +
                "GROUP BY bpb" + sqlCurrency + " ORDER BY bpb" + sqlCurrency + ";";
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
}
