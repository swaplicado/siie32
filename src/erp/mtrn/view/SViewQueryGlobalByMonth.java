/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.table.STabFilterYear;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;
import erp.table.SFilterConstants;
import erp.table.STabFilterUnitType;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.swing.JToggleButton;

/**
 *
 * @author Juan Barajas, Edwin Carmona
 */
public class SViewQueryGlobalByMonth extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    protected static final int MONTH_JAN = 1;
    protected static final int MONTH_FEB = 2;
    protected static final int MONTH_MAR = 3;
    protected static final int MONTH_APR = 4;
    protected static final int MONTH_MAY = 5;
    protected static final int MONTH_JUN = 6;
    protected static final int MONTH_JUL = 7;
    protected static final int MONTH_AUG = 8;
    protected static final int MONTH_SEP = 9;
    protected static final int MONTH_OCT = 10;
    protected static final int MONTH_NOV = 11;
    protected static final int MONTH_DEC = 12;

    protected int mnYear;
    protected int mnType;
    protected String msColumnUnit = "";
    protected String msHeaderTitle = "";
    protected erp.lib.table.STableColumn[] maoTableColumns;
    protected erp.lib.table.STabFilterYear moFilterYear;
    private erp.table.STabFilterUnitType moTabFilterUnitType;
    
    private javax.swing.JToggleButton jtbRelatedParty;

    public SViewQueryGlobalByMonth(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_QRY, auxType01);
        initComponents();
    }

    private void initComponents() {
        maoTableColumns = null;

        moFilterYear = new STabFilterYear(miClient, this);
        moTabFilterUnitType = new STabFilterUnitType(miClient, this);
        mnYear = miClient.getSessionXXX().getWorkingYear();
        mnType = 0;
        
        jtbRelatedParty = new javax.swing.JToggleButton();
        
        jtbRelatedParty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_rel_pty_off.gif")));
        jtbRelatedParty.setToolTipText("Filtrar partes relacionadas");
        jtbRelatedParty.setPreferredSize(new java.awt.Dimension(23, 23));
        jtbRelatedParty.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_rel_pty_on.gif")));
        
        jtbRelatedParty.addActionListener(this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        addTaskBarUpperComponent(moFilterYear);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterUnitType);
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jtbRelatedParty);
        
        jtbRelatedParty.setSelected(true);

        renderTableColumns();
        setIsSummaryApplying(true);

        populateTable();
    }

    private void renderTableColumns() {
        int i = 0;
        String[] asMonths = null;

        moTablePane.reset();

        asMonths = SLibTimeUtilities.createMonthsOfYear(Locale.getDefault(), Calendar.SHORT);
        maoTableColumns = new STableColumn[27];

        i = 0;
        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 200);
        }
        else {
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 200);
            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
        }

        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_01", msHeaderTitle + asMonths[0] + ".", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_01", "Total neto " + asMonths[0] + ". $ ", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_02", msHeaderTitle + asMonths[1] + ".", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_02", "Total neto " + asMonths[1] + ". $ ", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_03", msHeaderTitle + asMonths[2] + ".", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_03", "Total neto " + asMonths[2] + ". $ ", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_04", msHeaderTitle + asMonths[3] + ".", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_04", "Total neto " + asMonths[3] + ". $ ", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_05", msHeaderTitle + asMonths[4] + ".", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_05", "Total neto " + asMonths[4] + ". $ ", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_06", msHeaderTitle + asMonths[5] + ".", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_06", "Total neto " + asMonths[5] + ". $ ", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_07", msHeaderTitle + asMonths[6] + ".", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_07", "Total neto " + asMonths[6] + ". $ ", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_08", msHeaderTitle + asMonths[7] + ".", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_08", "Total neto " + asMonths[7] + ". $ ", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_09", msHeaderTitle + asMonths[8] + ".", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_09", "Total neto " + asMonths[8] + ". $ ", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_10", msHeaderTitle + asMonths[9] + ".", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_10", "Total neto " + asMonths[9] + ". $ ", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_11", msHeaderTitle + asMonths[10] + ".", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_11", "Total neto " + asMonths[10] + ". $ ", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_12", msHeaderTitle + asMonths[11] + ".", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_12", "Total neto " + asMonths[11] + ". $ ", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());

        for (i = 0; i < maoTableColumns.length; i++) {
            moTablePane.addTableColumn(maoTableColumns[i]);
        }

        moTablePane.createTable(this);
    }

    private boolean isPurchase() {
        boolean isPurchase = false;

        if (mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_MONTH) {
            isPurchase = true;
        }

        return isPurchase;
    }

    private void createColumnUnit(int n) {
        switch (n) {
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY:
                msColumnUnit = "de.qty ";
                msHeaderTitle = "Cant. neta ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN:
                msColumnUnit = "de.len ";
                msHeaderTitle = "Long. neta ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF:
                msColumnUnit = "de.surf ";
                msHeaderTitle = "Superf. neta ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_VOL:
                msColumnUnit = "de.vol ";
                msHeaderTitle = "Vol. neta ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS:
                msColumnUnit = "de.mass ";
                msHeaderTitle = "Masa neta ";
                break;
            default:
        }
    }

    private String createQueryUnit(int nMonth) {
        String sSql = "";

        sSql = "COALESCE(COALESCE(SUM(CASE WHEN (" +
                "d.fid_ct_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0]) + " " +
                "AND d.fid_cl_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1]) + " " +
                "AND d.fid_tp_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2]) + " " +
                "AND de.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_NA + " " +
                "AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF +
                ") AND MONTH(d.dt) = " + nMonth + " THEN " + msColumnUnit + "ELSE 0 END), 0) - " +
                "COALESCE(SUM(CASE WHEN (" +
                "d.fid_ct_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[0]) + " " +
                "AND d.fid_cl_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[1]) + " " +
                "AND d.fid_tp_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[2]) + " " +
                "AND de.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " " +
                "AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF +
                ") AND MONTH(d.dt) = " + nMonth + " THEN " + msColumnUnit + "ELSE 0 END), 0), 0) ";

        return sSql;
    }

    private String createQueryBalance(int nMonth) {
        String sSql = "";

        sSql = "COALESCE(COALESCE(SUM(CASE WHEN (" +
                "d.fid_ct_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0]) + " " +
                "AND d.fid_cl_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1]) + " " +
                "AND d.fid_tp_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2]) + " " +
                "AND de.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_NA + " " +
                "AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF +
                ") AND MONTH(d.dt) = " + nMonth + " THEN de.stot_r ELSE 0 END), 0) - " +
                "COALESCE(SUM(CASE WHEN (" +
                "d.fid_ct_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[0]) + " " +
                "AND d.fid_cl_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[1]) + " " +
                "AND d.fid_tp_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[2]) + " " +
                "AND de.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " " +
                "AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF +
                ") AND MONTH(d.dt) = " + nMonth + " THEN de.stot_r ELSE 0 END), 0) - " +
                "COALESCE(SUM(CASE WHEN (" +
                "d.fid_ct_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[0]) + " " +
                "AND d.fid_cl_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[1]) + " " +
                "AND d.fid_tp_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[2]) + " " +
                "AND de.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC + " " +
                "AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF +
                ") AND MONTH(d.dt) = " + nMonth + " THEN de.stot_r ELSE 0 END), 0), 0) ";

        return sSql;
    }

    @Override
    public void createSqlQuery() {
        STableSetting setting = null;
        Date tDate = null;
        int typeUnitTotal = 0;
        String sqlDatePeriod = "";
        String dateInit = "";
        String dateEnd = "";

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_YEAR) {
                mnYear = ((Integer) setting.getSetting());
                tDate = SLibTimeUtilities.createDate(mnYear);
                dateInit = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(SLibTimeUtilities.getBeginOfYear(tDate));
                dateEnd = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(SLibTimeUtilities.getEndOfYear(tDate));
                sqlDatePeriod += " AND d.dt BETWEEN '" + dateInit + "' AND '" + dateEnd + "' ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_UNIT_TP) {
                typeUnitTotal = (Integer)setting.getSetting();
                mnType = typeUnitTotal;
                createColumnUnit(typeUnitTotal);
            }
        }

//        renderTableColumns();

        msSql = "SELECT de.fid_item, de.fid_unit, i.item_key, i.item, " +
                    createQueryUnit(MONTH_JAN) + " AS f_qty_01, " +
                    createQueryBalance(MONTH_JAN) + " AS f_stot_01, " +
                    createQueryUnit(MONTH_FEB) + " AS f_qty_02, " +
                    createQueryBalance(MONTH_FEB) + " AS f_stot_02, " +
                    createQueryUnit(MONTH_MAR) + " AS f_qty_03, " +
                    createQueryBalance(MONTH_MAR) + " AS f_stot_03, " +
                    createQueryUnit(MONTH_APR) + " AS f_qty_04, " +
                    createQueryBalance(MONTH_APR) + " AS f_stot_04, " +
                    createQueryUnit(MONTH_MAY) + " AS f_qty_05, " +
                    createQueryBalance(MONTH_MAY) + " AS f_stot_05, " +
                    createQueryUnit(MONTH_JUN) + " AS f_qty_06, " +
                    createQueryBalance(MONTH_JUN) + " AS f_stot_06, " +
                    createQueryUnit(MONTH_JUL) + " AS f_qty_07, " +
                    createQueryBalance(MONTH_JUL) + " AS f_stot_07, " +
                    createQueryUnit(MONTH_AUG) + " AS f_qty_08, " +
                    createQueryBalance(MONTH_AUG) + " AS f_stot_08, " +
                    createQueryUnit(MONTH_SEP) + " AS f_qty_09, " +
                    createQueryBalance(MONTH_SEP) + " AS f_stot_09, " +
                    createQueryUnit(MONTH_OCT) + " AS f_qty_10, " +
                    createQueryBalance(MONTH_OCT) + " AS f_stot_10, " +
                    createQueryUnit(MONTH_NOV) + " AS f_qty_11, " +
                    createQueryBalance(MONTH_NOV) + " AS f_stot_11, " +
                    createQueryUnit(MONTH_DEC) + " AS f_qty_12, " +
                    createQueryBalance(MONTH_DEC) + " AS f_stot_12, (SELECT unit_base FROM erp.itmu_tp_unit WHERE id_tp_unit = " +
                    (typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY ? SDataConstantsSys.ITMU_TP_UNIT_QTY :
                        typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN ? SDataConstantsSys.ITMU_TP_UNIT_LEN :
                            typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS ? SDataConstantsSys.ITMU_TP_UNIT_MASS :
                                typeUnitTotal == SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF ? SDataConstantsSys.ITMU_TP_UNIT_SURF :
                                    SDataConstantsSys.ITMU_TP_UNIT_VOL) +
                    ") AS symbol " +
                    "FROM trn_dps AS d " +
                    "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                    "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item " +
                    "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " +
                    "WHERE d.b_del = 0 AND de.b_del = 0 " + sqlDatePeriod +
                    (jtbRelatedParty.isSelected() ? "" : " AND bp.b_att_rel_pty = 0 ") +
                    "GROUP BY i.item_key, i.item " +
                    "HAVING f_stot_01 <> 0 OR f_qty_01 <> 0 OR f_stot_02 <> 0 OR f_qty_02 <> 0 OR f_stot_03 <> 0 OR f_qty_03 <> 0 OR f_stot_04 <> 0 OR f_qty_04 <> 0 OR f_stot_05 <> 0 OR f_qty_05 <> 0 OR " +
                    "f_stot_06 <> 0 OR f_qty_06 <> 0 OR f_stot_07 <> 0 OR f_qty_07 <> 0 OR f_stot_08 <> 0 OR f_qty_08 <> 0 OR f_stot_09 <> 0 OR f_qty_09 <> 0 OR f_stot_10 <> 0 OR f_qty_10 <> 0 OR " +
                    "f_stot_11 <> 0 OR f_qty_11 <> 0 OR f_stot_12 <> 0 OR f_qty_12 <> 0 " +
                    "ORDER BY i.item_key, i.item ";
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

        if (e.getSource() instanceof javax.swing.JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) e.getSource();

            if (toggleButton == jtbRelatedParty) {
                actionReload();
            }
        }
    }
}
