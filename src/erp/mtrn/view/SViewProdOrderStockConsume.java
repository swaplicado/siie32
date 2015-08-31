/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDateCutOff;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDatePeriodRange;
import erp.lib.table.STabFilterYear;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataRawMaterialsConsume;
import erp.mtrn.form.SDialogProdOrderStockConsume;
import erp.mtrn.form.SDialogStockCardexProdOrder;
import erp.table.SFilterConstants;
import erp.table.STabFilterCompanyBranchEntity;
import erp.table.STabFilterProductionOrderType;
import java.awt.Dimension;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Sergio Flores
 */
public class SViewProdOrderStockConsume extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton mjbRmConsume;
    private javax.swing.JButton mjbRmConsumePercentage;
    private javax.swing.JButton mjbRmConsumeDelete;
    private javax.swing.JButton jbCardex;
    private erp.lib.table.STabFilterDateCutOff moTabFilterDateCutOff;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private erp.lib.table.STabFilterYear moTabFilterYear;
    private erp.table.STabFilterCompanyBranchEntity moTabFilterCompanyBranchEntity;
    private erp.table.STabFilterProductionOrderType moTabFilterProdOrderType;
    private erp.mtrn.form.SDialogProdOrderStockConsume moDialogProdOrderStockConsume;
    private erp.mtrn.form.SDialogStockCardexProdOrder moDialogStockCardexProdOrder;

    /**
     * @param client Client interface.
     * @param tabTitle View tab title.
     * @param auxType01 View tab type. Constants defined in SDataConstats (TRNX_MFG_ORD_CONSUME_PEND..., TRNX_MFG_ORD_CONSUMED...).
     */
    public SViewProdOrderStockConsume(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRNX_MFG_ORD, auxType01, auxType02);
        initComponents();
    }

    private boolean isViewForConsume() {
        return mnTabTypeAux01 == SDataConstants.TRNX_MFG_ORD_CONSUME_PEND || mnTabTypeAux01 == SDataConstants.TRNX_MFG_ORD_CONSUME_PEND_ETY ||
                mnTabTypeAux01 == SDataConstants.TRNX_MFG_ORD_CONSUME_PEND_MASS || mnTabTypeAux01 == SDataConstants.TRNX_MFG_ORD_CONSUME_PEND_ETY_MASS;
    }

    private boolean isViewForEntries() {
        return mnTabTypeAux01 == SDataConstants.TRNX_MFG_ORD_CONSUME_PEND_ETY || mnTabTypeAux01 == SDataConstants.TRNX_MFG_ORD_CONSUMED_ETY ||
                mnTabTypeAux01 == SDataConstants.TRNX_MFG_ORD_CONSUME_PEND_ETY_MASS || mnTabTypeAux01 == SDataConstants.TRNX_MFG_ORD_CONSUMED_ETY_MASS;
    }

    private void initComponents() {
        int i = 0;
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;
        boolean hasRightMfgConsume = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_CON).HasRight;

        moTabFilterDateCutOff = null;
        moTabFilterDatePeriod = null;
        moTabFilterDatePeriodRange = null;
        moTabFilterYear = null;

        mjbRmConsume = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_rm_con.gif")));
        mjbRmConsume.setPreferredSize(new Dimension(23, 23));
        mjbRmConsume.setToolTipText("Consumir MP & P");
        mjbRmConsume.addActionListener(this);

        mjbRmConsumePercentage = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_rm_con_per.gif")));
        mjbRmConsumePercentage.setPreferredSize(new Dimension(23, 23));
        mjbRmConsumePercentage.setToolTipText("Consumir MP & P con % avance");
        mjbRmConsumePercentage.addActionListener(this);

        mjbRmConsumeDelete = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_rm_con_del.gif")));
        mjbRmConsumeDelete.setPreferredSize(new Dimension(23, 23));
        mjbRmConsumeDelete.setToolTipText("Eliminar consumo MP & P");
        mjbRmConsumeDelete.addActionListener(this);

        jbCardex = new JButton(miClient.getImageIcon(SLibConstants.ICON_KARDEX));
        jbCardex.setPreferredSize(new Dimension(23, 23));
        jbCardex.setToolTipText("Ver tarjeta auxiliar de almacén");
        jbCardex.addActionListener(this);

        if (isViewForConsume()) {
            if (mnTabTypeAux02 != SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS) {
                moTabFilterDateCutOff = new STabFilterDateCutOff(miClient, this, SLibTimeUtilities.getEndOfYear(miClient.getSessionXXX().getWorkingDate()));
                moTabFilterDatePeriod = null;
                moTabFilterDatePeriodRange = null;
                moTabFilterYear = new STabFilterYear(miClient, this);
                moTabFilterYear.setYearToolTipText("Ejercicio de OP");
            }
            else {
                moTabFilterDateCutOff = null;
                moTabFilterDatePeriod = null;
                moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);
                moTabFilterYear = null;
            }

        }
        else {
            if (mnTabTypeAux02 != SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS) {
                moTabFilterDateCutOff = null;
                moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
                moTabFilterDatePeriodRange = null;
                moTabFilterYear = null;
            }
            else {
                moTabFilterDateCutOff = null;
                moTabFilterDatePeriod = null;
                moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);
                moTabFilterYear = null;
            }
        }

        moTabFilterCompanyBranchEntity = new STabFilterCompanyBranchEntity(miClient, this, SDataConstantsSys.CFGS_CT_ENT_PLANT);
        moTabFilterProdOrderType = new STabFilterProductionOrderType(miClient, this);
        moDialogStockCardexProdOrder = new SDialogStockCardexProdOrder(miClient);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        addTaskBarUpperComponent(mnTabTypeAux02 != SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS ? (isViewForConsume() ? moTabFilterDateCutOff : moTabFilterDatePeriod) : moTabFilterDatePeriodRange);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranchEntity);
        addTaskBarUpperComponent(moTabFilterProdOrderType);

        if (mnTabTypeAux02 != SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS) {
            addTaskBarUpperSeparator();
            addTaskBarUpperComponent(mjbRmConsume);
            addTaskBarUpperComponent(mjbRmConsumePercentage);
            addTaskBarUpperComponent(mjbRmConsumeDelete);

            mjbRmConsume.setEnabled(isViewForConsume() && hasRightMfgConsume);
            mjbRmConsumePercentage.setEnabled(isViewForConsume() && hasRightMfgConsume);
            mjbRmConsumeDelete.setEnabled(!isViewForConsume() && hasRightMfgConsume);

            moDialogProdOrderStockConsume = new SDialogProdOrderStockConsume(miClient);
        }

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbCardex);

        if (moTabFilterYear != null) {
            addTaskBarUpperSeparator();
            addTaskBarUpperComponent(moTabFilterYear);
        }

        // View primary keys:

        aoKeyFields = new STableField[2];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_ord");

        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        // View columns:

        aoTableColumns = new STableColumn[!isViewForEntries() ? 18 : 26];

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ord_num", "Folio orden", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt", "Fecha orden", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt_dly", "Fecha entrega", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ent_code", "Planta", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "fid_st_ord", "Estatus", STableConstants.WIDTH_ICON);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp", "Tipo orden", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_item", "Ítem", STableConstants.WIDTH_ITEM_2X);

        if (mnTabTypeAux02 != SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS) {
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "qty", "Cant. orden", STableConstants.WIDTH_VALUE_2X);
            aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            aoTableColumns[i++].setSumApplying(true);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        }
        else {
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "qty", "Cant. orden" + (mnTabTypeAux02 != SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS ? "" : " (masa)"), STableConstants.WIDTH_VALUE_2X);
            aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            aoTableColumns[i++].setSumApplying(true);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_symbol_mass", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        }

        if (isViewForEntries()) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "f_is_rm", "Insumo", STableConstants.WIDTH_BOOLEAN);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave insumo/producto", STableConstants.WIDTH_ITEM_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Insumo/producto", STableConstants.WIDTH_ITEM_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "lot", "Lote insumo/producto", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt_exp_n", "Caducidad insumo/producto", STableConstants.WIDTH_DATE);
        }

        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_asd", "Cant. asig. entregada", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_asd_ret", "Cant. asig. devuelta", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_asd_net", "Cant. asig. neta", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);

        if (isViewForEntries()) {
            if (mnTabTypeAux02 != SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "symbol", "Unidad insumo/producto", STableConstants.WIDTH_UNIT_SYMBOL);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_symbol_mass", "Unidad insumo/producto", STableConstants.WIDTH_UNIT_SYMBOL);
            }
        }

        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_fin", "Cant. term. entregada", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_fin_ret", "Cant. term. devuelta", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_fin_net", "Cant. term. neta", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);

        if (isViewForEntries()) {
            if (mnTabTypeAux02 != SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "symbol", "Unidad insumo/producto", STableConstants.WIDTH_UNIT_SYMBOL);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_symbol_mass", "Unidad insumo/producto", STableConstants.WIDTH_UNIT_SYMBOL);
            }
        }

        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_dif", "Cant. x consumir", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++].setSumApplying(true);

        if (isViewForEntries()) {
            if (mnTabTypeAux02 != SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "symbol", "Unidad insumo/producto", STableConstants.WIDTH_UNIT_SYMBOL);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_symbol_mass", "Unidad insumo/producto", STableConstants.WIDTH_UNIT_SYMBOL);
            }
        }

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        setIsSummaryApplying(true);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.ITMU_UNIT);
        mvSuscriptors.add(SDataConstants.MFG_ORD);
        mvSuscriptors.add(SDataConstants.TRN_DIOG);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();

        moTablePane.setDoubleClickAction(this, "actionCardex");
    }

    private void actionRmConsume() {
        if (mjbRmConsume.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else if (!SDataUtilities.isPeriodOpen(miClient, miClient.getSessionXXX().getSystemDate())) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_PER_CLOSE + "\nFecha: " + miClient.getSessionXXX().getFormatters().getDateFormat().format(miClient.getSessionXXX().getSystemDate()) + ".");
            }
            else {
                int[] key = null;
                SDataRawMaterialsConsume rawMaterialsConsume = null;

                if (miClient.showMsgBoxConfirm("¿Está seguro que desea hacer el consumo de MP y P de la OP seleccionada con fecha " + miClient.getSessionXXX().getFormatters().getDateFormat().format(miClient.getSessionXXX().getSystemDate()) + "?") == JOptionPane.YES_OPTION) {
                    try {
                        key = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();
                        rawMaterialsConsume = new SDataRawMaterialsConsume();
                        rawMaterialsConsume.setPkYearId(key[0]);
                        rawMaterialsConsume.setPkOrderId(key[1]);
                        rawMaterialsConsume.setDate(miClient.getSessionXXX().getSystemDate());
                        rawMaterialsConsume.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                        SDataUtilities.saveRegistry(miClient, rawMaterialsConsume);

                        miClient.showMsgBoxInformation("El consumo de MP y P de la OP ha sido realizado.");
                        miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(SDataConstants.TRN_DIOG);
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                }
            }
        }
    }

    private void actionRmConsumePercentage() {
        if (mjbRmConsumePercentage.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                moDialogProdOrderStockConsume.formReset();
                moDialogProdOrderStockConsume.setValue(SDataConstants.MFG_ORD, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogProdOrderStockConsume.setFormVisible(true);

                if (moDialogProdOrderStockConsume.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(SDataConstants.TRN_DIOG);
                }
            }
        }
    }

    private void actionRmConsumeDelete() {
        if (mjbRmConsumeDelete.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int[] key = null;
                SDataRawMaterialsConsume rawMaterialsConsume = null;

                if (miClient.showMsgBoxConfirm("¿Está seguro que desea eliminar el consumo de MP y P de la OP seleccionada?") == JOptionPane.YES_OPTION) {
                    try {
                        key = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();
                        rawMaterialsConsume = new SDataRawMaterialsConsume();
                        rawMaterialsConsume.setPkYearId(key[0]);
                        rawMaterialsConsume.setPkOrderId(key[1]);
                        rawMaterialsConsume.setDate(miClient.getSessionXXX().getSystemDate());
                        rawMaterialsConsume.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                        rawMaterialsConsume.setAuxMode(SDataRawMaterialsConsume.MODE_DELETE);
                        SDataUtilities.saveRegistry(miClient, rawMaterialsConsume);

                        miClient.showMsgBoxInformation("El consumo de MP y P de la OP ha sido eliminado.");
                        miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(SDataConstants.TRN_DIOG);
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                }
            }
        }
    }

    public void actionCardex() {
        if (jbCardex.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
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
        Date[] range = null;
        String sqlFilter = "";
        String sqlDateCutOff = "";

        for (STableSetting setting : mvTableSettings) {
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                if (isViewForConsume()) {

                    if (mnTabTypeAux02 != SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS) {
                        sqlDateCutOff += setting.getSetting() == null ? "" : "AND g.dt <= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format((Date) setting.getSetting()) + "' ";
                    }
                    else {
                        range = (java.util.Date[]) setting.getSetting();
                        sqlFilter += "AND o.dt BETWEEN '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]) +
                                "' AND '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]) + "' ";
                    }
                }
                else {
                    if (mnTabTypeAux02 != SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS) {
                        sqlFilter += "AND " + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "o.dt");
                    }
                    else {
                        range = (java.util.Date[]) setting.getSetting();
                        sqlFilter += "AND o.dt BETWEEN '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]) +
                                "' AND '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]) + "' ";
                    }
                }
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_YEAR) {
                if (isViewForConsume()) {
                    sqlFilter += "AND " + SDataSqlUtilities.composePeriodFilter(new int[] { (Integer) setting.getSetting() }, "o.dt");
                }
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

        if (isViewForEntries()) {
            msSql = "";
        }
        else {
            msSql = "SELECT id_year, id_ord, dt, dt_dly, qty, fid_st_ord, tp, f_ord_num, " +
                    "f_cob_code, f_ent_code, f_item_key, f_item, f_symbol, " +
                    "SUM(f_asd) AS f_asd, " +
                    "SUM(f_asd_ret) AS f_asd_ret, " +
                    "SUM(f_asd_net) AS f_asd_net, " +
                    "SUM(f_fin) AS f_fin, " +
                    "SUM(f_fin_ret) AS f_fin_ret, " +
                    "SUM(f_fin_net) AS f_fin_net, " +
                    "SUM(f_dif) AS f_dif, " +
                    "SUM(f_dif_mass) AS f_dif_mass, " +
                    "COUNT(*) AS f_count, " +
                    "SUM(f_dif = 0) AS f_count_dif," +
                    "f_symbol_mass " +
                    "FROM (";
        }

        msSql += "SELECT o.id_year, o.id_ord, " +
                "o.dt, o.dt_dly, o.qty " + (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS ? "* i.mass " : "") + " AS qty, " + STableConstants.ICON_MFG_ST + " + o.fid_st_ord AS fid_st_ord, " +
                "ot.tp, CONCAT(o.id_year, '-', o.num) AS f_ord_num, " +
                "cob.code AS f_cob_code, ent.code AS f_ent_code, i.item_key AS f_item_key, i.item AS f_item, u.symbol AS f_symbol, " +
                "s.id_item, s.id_unit, s.id_lot, si.item_key, si.item, su.symbol, sl.lot, sl.dt_exp_n, " +
                "o.fid_item_r <> si.id_item AS f_is_rm, " +
                "SUM(CASE WHEN " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ") THEN " +
                "(s.mov_in - s.mov_out) " +
                    (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && !isViewForEntries() ? "* i.mass " :
                    mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && isViewForEntries() ? "* si.mass " :
                    "") + " ELSE 0 END) AS f_asd, " +
                "SUM(CASE WHEN " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ") THEN " +
                "(s.mov_in - s.mov_out) " +
                    (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && !isViewForEntries() ? "* i.mass " :
                    mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && isViewForEntries() ? "* si.mass " :
                    "") + " ELSE 0 END) AS f_asd_ret, " +
                "SUM(CASE WHEN " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ") THEN " +
                "(s.mov_in - s.mov_out) " +
                    (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && !isViewForEntries() ? "* i.mass " :
                    mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && isViewForEntries() ? "* si.mass " :
                    "") + " ELSE 0 END) AS f_asd_net, " +
                "SUM(CASE WHEN " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ") THEN " +
                "(s.mov_out - s.mov_in) " +
                    (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && !isViewForEntries() ? "* i.mass " :
                    mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && isViewForEntries() ? "* si.mass " :
                    "") + " ELSE 0 END) AS f_fin, " +
                "SUM(CASE WHEN " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[2] + ") THEN " +
                "(s.mov_out - s.mov_in) " +
                    (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && !isViewForEntries() ? "* i.mass " :
                    mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && isViewForEntries() ? "* si.mass " :
                    "") + " ELSE 0 END) AS f_fin_ret, " +
                "SUM(CASE WHEN " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[2] + ") THEN " +
                "(s.mov_out - s.mov_in) " +
                    (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && !isViewForEntries() ? "* i.mass " :
                    mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && isViewForEntries() ? "* si.mass " :
                    "") + " ELSE 0 END) AS f_fin_net, " +
                "SUM(s.mov_in - s.mov_out) " +
                    (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && !isViewForEntries() ? "* i.mass " :
                    mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && isViewForEntries() ? "* si.mass " :
                    "") + " AS f_dif, " +
                "SUM(s.mov_in - s.mov_out) " +
                    (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && !isViewForEntries() ? "* (IF(i.mass = 0, 1, i.mass)) " :
                    mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS && isViewForEntries() ? "* (IF(si.mass = 0, 1, si.mass)) " :
                    "") + " AS f_dif_mass, " +
                "im.symbol AS f_symbol_mass " +
                "FROM mfg_ord AS o " +
                "INNER JOIN erp.mfgu_tp_ord AS ot ON o.fid_tp_ord = ot.id_tp AND o.b_del = 0 AND o.b_con = 0 " + sqlFilter +
                "INNER JOIN erp.bpsu_bpb AS cob ON o.fid_cob = cob.id_bpb " +
                "INNER JOIN erp.cfgu_cob_ent AS ent ON o.fid_cob = ent.id_cob AND o.fid_ent = ent.id_ent " +
                "INNER JOIN erp.itmu_item AS i ON o.fid_item_r = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON o.fid_unit_r = u.id_unit " +

                "INNER JOIN trn_diog AS g ON g.fid_mfg_year_n = o.id_year AND g.fid_mfg_ord_n = o.id_ord AND g.b_del = 0 " + sqlDateCutOff +
                "INNER JOIN trn_diog_ety AS ge ON g.id_year = ge.id_year AND g.id_doc = ge.id_doc AND ge.b_del = 0 " +
                "INNER JOIN trn_stk AS s ON ge.id_year = s.fid_diog_year AND ge.id_doc = s.fid_diog_doc AND ge.id_ety = s.fid_diog_ety AND s.b_del = 0 " +
                "INNER JOIN erp.itmu_item AS si ON s.id_item = si.id_item " +
                "INNER JOIN erp.itmu_unit AS su ON s.id_unit = su.id_unit " +
                "INNER JOIN trn_lot AS sl ON s.id_item = sl.id_item AND s.id_unit = sl.id_unit AND s.id_lot = sl.id_lot " +
                "LEFT OUTER JOIN erp.itmu_unit AS im ON im.id_unit = " + SModSysConsts.ITMU_UNIT_KG + " " +
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
                "GROUP BY o.id_year, o.id_ord, o.dt, o.dt_dly, o.qty, ot.tp, " +
                "cob.code, ent.code, i.item_key, i.item, u.symbol, " +
                "s.id_item, s.id_unit, s.id_lot, si.item_key, si.item, su.symbol, sl.lot, sl.dt_exp_n " + (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS ? ", si.mass " : "");

        if (isViewForEntries()) {
            if (isViewForConsume()) {
                msSql += "HAVING (f_asd_net <> 0 OR f_fin_net <> 0) AND f_dif <> 0 " + (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS ? " OR f_dif_mass <> 0 " : "");
            }
            else {
                msSql += "HAVING (f_asd_net <> 0 OR f_fin_net <> 0) AND f_dif = 0 " + (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS ? " OR f_dif_mass = 0 " : "");
            }
            msSql += "ORDER BY o.id_year, o.id_ord, f_is_rm, si.item_key, si.item, su.symbol, sl.lot, sl.dt_exp_n, s.id_item, s.id_unit, s.id_lot ";
        }
        else {
            msSql += ") AS T " +
                    "GROUP BY id_year, id_ord, dt, dt_dly, qty, fid_st_ord, tp, f_ord_num, " +
                    "f_cob_code, f_ent_code, f_item_key, f_item, f_symbol ";

            if (isViewForConsume()) {
                msSql += "HAVING " + (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS ? " f_dif_mass <> 0 " : "f_dif <> 0 OR f_count <> f_count_dif ");
            }
            else {
                msSql += "HAVING " + (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS ? " f_dif_mass = 0 " : "f_dif = 0 AND f_count = f_count_dif ");
            }

            msSql += "ORDER BY id_year, id_ord ";
        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == mjbRmConsume) {
                actionRmConsume();
            }
            if (button == mjbRmConsumePercentage) {
                actionRmConsumePercentage();
            }
            if (button == mjbRmConsumeDelete) {
                actionRmConsumeDelete();
            }
            else if (button == jbCardex) {
                actionCardex();
            }
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
