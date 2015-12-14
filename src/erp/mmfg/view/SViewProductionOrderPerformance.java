/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STabFilterDatePeriodRange;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mmfg.data.SDataProductionOrder;
import erp.mtrn.form.SDialogStockCardexProdOrder;
import erp.table.SFilterConstants;
import erp.table.STabFilterCompanyBranchEntity;
import erp.table.STabFilterProductionOrderType;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;

/**
 *
 * @author Juan Barajas
 */
public class SViewProductionOrderPerformance extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private erp.table.STabFilterCompanyBranchEntity moTabFilterCompanyBranchEntity;
    private erp.table.STabFilterProductionOrderType moTabFilterProdOrderType;

    private javax.swing.JButton jbPrintPerformance;
    private javax.swing.JButton jbCardex;

    private erp.mtrn.form.SDialogStockCardexProdOrder moDialogStockCardexProdOrder;

    /**
     * @param client Client interface.
     * @param tabTitle View tab title.
     */
    public SViewProductionOrderPerformance(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.MFGX_ORD_PERF);
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;

        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);

        moTabFilterCompanyBranchEntity = new STabFilterCompanyBranchEntity(miClient, this, SDataConstantsSys.CFGS_CT_ENT_PLANT);
        moTabFilterProdOrderType = new STabFilterProductionOrderType(miClient, this);
        moDialogStockCardexProdOrder = new SDialogStockCardexProdOrder(miClient);

        jbPrintPerformance = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        jbPrintPerformance.setPreferredSize(new Dimension(23, 23));
        jbPrintPerformance.addActionListener(this);
        jbPrintPerformance.setToolTipText("Imprimir rendimiento");

        jbCardex = new JButton(miClient.getImageIcon(SLibConstants.ICON_KARDEX));
        jbCardex.setPreferredSize(new Dimension(23, 23));
        jbCardex.setToolTipText("Ver tarjeta auxiliar de almacén");
        jbCardex.addActionListener(this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        addTaskBarUpperComponent(moTabFilterDatePeriodRange);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranchEntity);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbPrintPerformance);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbCardex);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterProdOrderType);

        // View primary keys:

        aoKeyFields = new STableField[2];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_ord");

        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        // View columns:

        aoTableColumns = new STableColumn[18];

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ord_num", "Folio orden", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt", "Fecha orden", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ent_code", "Planta", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "fid_st_ord", "Estatus", STableConstants.WIDTH_ICON);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp", "Tipo orden", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_item", "Ítem", STableConstants.WIDTH_ITEM_2X);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "qty_ori", "Cantidad original", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "qty_rew", "Cantidad reproceso", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "qty", "Cantidad total", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ref", "Ord. prod. padre", 80);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "", "Índice rendimiento", STableConstants.WIDTH_VALUE_UNITARY);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_fin", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_fin_ret", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("qty", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.DIVISION, SLibRpnArgumentType.OPERATOR));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_asd", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_asd_ret", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_req", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.DIVISION, SLibRpnArgumentType.OPERATOR));
        aoTableColumns[i++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_req", "Cantidad requerida", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_asd_net", "Cantidad asignada", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_asd", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_asd_ret", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "", "Cant. terminada", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_fin", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_fin_ret", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "", "Avance term. %", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_fin", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_fin_ret", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("qty", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.DIVISION, SLibRpnArgumentType.OPERATOR));

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        SFormUtilities.createActionMap(this, this, "actionPrint", "print", KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);
        moTablePane.setDoubleClickAction(this, "actionCardex");

        populateTable();
    }

    public void actionPrintPerformance() {
        SDataProductionOrder oProductionOrder = null;
        Cursor cursor = getCursor();
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;

        if (moTablePane.getSelectedTableRow() != null) {
            oProductionOrder = (SDataProductionOrder) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_ORD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

            try {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));

                map = miClient.createReportParams();
                map.put("bIsView", true);
                map.put("nIdYear", oProductionOrder.getPkYearId());
                map.put("nIdOrd", oProductionOrder.getPkOrdId());
                map.put("sCurCode", SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.CFGU_CUR, new int[] {miClient.getSessionXXX().getParamsErp().getFkCurrencyId() }, SLibConstants.DESCRIPTION_CODE));
                map.put("Ct_In_Mfg_Rm_Asd", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[0]);
                map.put("Cl_In_Mfg_Rm_Asd", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[1]);
                map.put("Tp_In_Mfg_Rm_Asd", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[2]);
                map.put("Ct_Out_Mfg_Rm_Ret", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[0]);
                map.put("Cl_Out_Mfg_Rm_Ret", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[1]);
                map.put("Tp_Out_Mfg_Rm_Ret", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[2]);
                map.put("Ct_In_Mfg_Wp_Asd", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[0]);
                map.put("Cl_In_Mfg_Wp_Asd", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[1]);
                map.put("Tp_In_Mfg_Wp_Asd", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[2]);
                map.put("Ct_Out_Mfg_Wp_Ret", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[0]);
                map.put("Cl_Out_Mfg_Wp_Ret", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[1]);
                map.put("Tp_Out_Mfg_Wp_Ret", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[2]);
                map.put("Ct_Out_Mfg_Wp_Asd", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[0]);
                map.put("Cl_Out_Mfg_Wp_Asd", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[1]);
                map.put("Tp_Out_Mfg_Wp_Asd", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[2]);
                map.put("Ct_In_Mfg_Wp_Ret", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[0]);
                map.put("Cl_In_Mfg_Wp_Ret", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[1]);
                map.put("Tp_In_Mfg_Wp_Ret", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[2]);
                map.put("Ct_Out_Mfg_Fg_Asd", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[0]);
                map.put("Cl_Out_Mfg_Fg_Asd", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[1]);
                map.put("Tp_Out_Mfg_Fg_Asd", SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[2]);
                map.put("Ct_In_Mfg_Fg_Ret", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[0]);
                map.put("Cl_In_Mfg_Fg_Ret", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[1]);
                map.put("Tp_In_Mfg_Fg_Ret", SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[2]);

                jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_MFG_ORD_PERFORMANCE, map);
                jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Rendimiento de orden de producción");
                jasperViewer.setVisible(true);
            }
            catch (Exception e) {
                System.out.println("Mensaje de error " + e.getMessage());
            }
            finally {
                setCursor(cursor);
            }
        }
    }

    public void actionCardex() {
        if (jbCardex.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                moDialogStockCardexProdOrder.formReset();
                moDialogStockCardexProdOrder.setFormParams((int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogStockCardexProdOrder.setVisible(true);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        int[] key = null;
        java.util.Date[] range = null;
        String sqlDatePeriod = "";
        String dateInit = "";
        String dateEnd = "";
        String sqlFilter = "";

        for (STableSetting setting : mvTableSettings) {
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                range = (java.util.Date[])setting.getSetting();
                dateInit = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]);
                dateEnd = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]);
                sqlDatePeriod += "o.dt BETWEEN '" + dateInit + "' AND '" + dateEnd + "' ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB_ENT) {
                key = (int[]) setting.getSetting();
                if (key != null) {
                    if (key[0] != SLibConstants.UNDEFINED) {
                        sqlFilter += "AND o.fid_cob = " + key[0] + " ";
                    }
                    if (key[1] != SLibConstants.UNDEFINED) {
                        sqlFilter += "AND o.fid_ent = " + key[1] + " ";
                    }
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_MFG_ORD_TP) {
                if ((Integer) setting.getSetting() != SLibConstants.UNDEFINED) {
                    sqlFilter += "AND o.fid_tp_ord = " + setting.getSetting() + " ";
                }
            }
        }

        msSql = "SELECT t.id_year, t.id_ord, dt, dt_dly, qty_ori, qty_rew, qty, fid_st_ord, tp, f_ord_num, f_ref, " +
                "f_cob_code, f_ent_code, f_item_key, f_item, f_symbol, " +
                "SUM(f_asd) AS f_asd, " +
                "SUM(f_asd_ret) AS f_asd_ret, " +
                "SUM(f_fin) AS f_fin, " +
                "SUM(f_fin_ret) AS f_fin_ret, " +
                "COUNT(*) AS f_count, " +
                "COALESCE((SELECT SUM(gross_req_r) FROM mfg_ord_chg_ety WHERE id_year = t.id_year AND id_ord = t.id_ord), 0) AS f_req " +
                "FROM (" +
                "SELECT o.id_year, o.id_ord, " +
                "o.dt, o.dt_dly, o.qty_ori, o.qty_rew, o.qty, " + STableConstants.ICON_MFG_ST + " + o.fid_st_ord AS fid_st_ord, " +
                "ot.tp, CONCAT(o.id_year, '-', o.num) AS f_ord_num, CONCAT(po.id_year, '-',po.num, ' ', po.ref) AS f_ref, " +
                "cob.code AS f_cob_code, ent.code AS f_ent_code, i.item_key AS f_item_key, i.item AS f_item, u.symbol AS f_symbol, " +
                "SUM(CASE WHEN " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ") THEN " +
                "(ge.qty) ELSE 0 END) AS f_asd, " +
                "SUM(CASE WHEN " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ") THEN " +
                "(ge.qty) ELSE 0 END) AS f_asd_ret, " +
                "SUM(CASE WHEN " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ") THEN " +
                "(ge.qty) ELSE 0 END) AS f_fin, " +
                "SUM(CASE WHEN " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[2] + ") THEN " +
                "(ge.qty) ELSE 0 END) AS f_fin_ret " +
                "FROM mfg_ord AS o " +
                "INNER JOIN erp.mfgu_tp_ord AS ot ON o.fid_tp_ord = ot.id_tp AND o.b_del = 0 AND o.b_con = 0 " + sqlFilter +
                "INNER JOIN erp.bpsu_bpb AS cob ON o.fid_cob = cob.id_bpb " +
                "INNER JOIN erp.cfgu_cob_ent AS ent ON o.fid_cob = ent.id_cob AND o.fid_ent = ent.id_ent " +
                "INNER JOIN erp.itmu_item AS i ON o.fid_item_r = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON o.fid_unit_r = u.id_unit " +
                "LEFT OUTER JOIN trn_diog AS g ON g.fid_mfg_year_n = o.id_year AND g.fid_mfg_ord_n = o.id_ord AND g.b_del = 0 " +
                "LEFT OUTER JOIN trn_diog_ety AS ge ON g.id_year = ge.id_year AND g.id_doc = ge.id_doc AND ge.b_del = 0 " +
                "LEFT OUTER JOIN mfg_ord AS po ON o.fid_ord_year_n = po.id_year AND o.fid_ord_n = po.id_ord " +
                "WHERE (" +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_CON[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_CON[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_CON[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_CON[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_CON[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_CON[2] + ")) " +
                "AND " + sqlDatePeriod +
                "GROUP BY o.id_year, o.id_ord, o.dt, o.dt_dly, o.qty, ot.tp, " +
                "cob.code, ent.code, i.item_key, i.item, u.symbol " +
                ") AS t " +
                "GROUP BY id_year, id_ord, dt, dt_dly, qty, fid_st_ord, tp, f_ord_num, " +
                "f_cob_code, f_ent_code, f_item_key, f_item, f_symbol " +
                "ORDER BY id_year, id_ord ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbPrintPerformance) {
                actionPrintPerformance();
            }
            else if (button == jbCardex) {
                actionCardex();
            }
        }
        else if (e.getSource() instanceof javax.swing.JToggleButton) {
            JToggleButton toggleButton = (javax.swing.JToggleButton) e.getSource();

        }
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
