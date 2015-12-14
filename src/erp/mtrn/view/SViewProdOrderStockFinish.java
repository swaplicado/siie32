/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDateCutOff;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterYear;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mtrn.data.STrnDiogComplement;
import erp.mtrn.data.STrnUtilities;
import erp.mtrn.form.SDialogStockCardexProdOrder;
import erp.table.SFilterConstants;
import erp.table.STabFilterCompanyBranchEntity;
import erp.table.STabFilterProductionOrderType;
import java.awt.Dimension;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;

/**
 *
 * @author Sergio Flores
 */
public class SViewProdOrderStockFinish extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton mjbWpAssign;
    private javax.swing.JButton mjbWpReturn;
    private javax.swing.JButton mjbFgAssign;
    private javax.swing.JButton mjbFgReturn;
    private javax.swing.JButton jbCardex;
    private javax.swing.ButtonGroup jbgStatus;
    private javax.swing.JToggleButton jtbStatusActive;
    private javax.swing.JToggleButton jtbStatusInactive;
    private erp.lib.table.STabFilterDateCutOff moTabFilterDateCutOff;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.lib.table.STabFilterYear moTabFilterYear;
    private erp.table.STabFilterCompanyBranchEntity moTabFilterCompanyBranchEntity;
    private erp.table.STabFilterProductionOrderType moTabFilterProdOrderType;
    private erp.mtrn.form.SDialogStockCardexProdOrder moDialogStockCardexProdOrder;

    /**
     * @param client Client interface.
     * @param tabTitle View tab title.
     * @param auxType01 View tab type. Constants defined in SDataConstats (TRNX_MFG_ORD_FINISH_PEND..., TRNX_MFG_ORD_FINISHED...).
     */
    public SViewProdOrderStockFinish(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_MFG_ORD, auxType01);
        initComponents();
    }

    private boolean isViewForAssign() {
        return mnTabTypeAux01 == SDataConstants.TRNX_MFG_ORD_FINISH_PEND || mnTabTypeAux01 == SDataConstants.TRNX_MFG_ORD_FINISH_PEND_ETY;
    }

    private boolean isViewForEntries() {
        return mnTabTypeAux01 == SDataConstants.TRNX_MFG_ORD_FINISH_PEND_ETY || mnTabTypeAux01 == SDataConstants.TRNX_MFG_ORD_FINISHED_ETY;
    }

    private void initComponents() {
        int i = 0;
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;
        boolean hasRightMfgWpAsg = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_WP_ASG).HasRight;
        boolean hasRightMfgWpRet = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_WP_DEV).HasRight;
        boolean hasRightMfgFgAsg = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_FG_ASG).HasRight;
        boolean hasRightMfgFgRet = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_FG_DEV).HasRight;

        mjbWpAssign = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_wp_asd.gif")));
        mjbWpAssign.setPreferredSize(new Dimension(23, 23));
        mjbWpAssign.setToolTipText("Entregar PP");
        mjbWpAssign.addActionListener(this);

        mjbWpReturn = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_wp_ret.gif")));
        mjbWpReturn.setPreferredSize(new Dimension(23, 23));
        mjbWpReturn.setToolTipText("Devolver PP");
        mjbWpReturn.addActionListener(this);

        mjbFgAssign = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_fg_asd.gif")));
        mjbFgAssign.setPreferredSize(new Dimension(23, 23));
        mjbFgAssign.setToolTipText("Entregar PT");
        mjbFgAssign.addActionListener(this);

        mjbFgReturn = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_fg_ret.gif")));
        mjbFgReturn.setPreferredSize(new Dimension(23, 23));
        mjbFgReturn.setToolTipText("Devolver PT");
        mjbFgReturn.addActionListener(this);

        jbCardex = new JButton(miClient.getImageIcon(SLibConstants.ICON_KARDEX));
        jbCardex.setPreferredSize(new Dimension(23, 23));
        jbCardex.setToolTipText("Ver tarjeta auxiliar de almacén");
        jbCardex.addActionListener(this);

        jtbStatusActive = new JToggleButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_po_act_off.gif")), true);
        jtbStatusActive.setSelectedIcon(new ImageIcon(getClass().getResource("/erp/img/icon_std_po_act_on.gif")));
        jtbStatusActive.setPreferredSize(new Dimension(23, 23));
        jtbStatusActive.setToolTipText("Ver OP activas");
        jtbStatusActive.addActionListener(this);

        jtbStatusInactive = new JToggleButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_po_ina_off.gif")), false);
        jtbStatusInactive.setSelectedIcon(new ImageIcon(getClass().getResource("/erp/img/icon_std_po_ina_on.gif")));
        jtbStatusInactive.setPreferredSize(new Dimension(23, 23));
        jtbStatusInactive.setToolTipText("Ver OP cerradas");
        jtbStatusInactive.addActionListener(this);

        jbgStatus = new ButtonGroup();
        jbgStatus.add(jtbStatusActive);
        jbgStatus.add(jtbStatusInactive);

        if (isViewForAssign()) {
            moTabFilterDateCutOff = new STabFilterDateCutOff(miClient, this, SLibTimeUtilities.getEndOfYear(miClient.getSessionXXX().getWorkingDate()));
            moTabFilterDatePeriod = null;
            moTabFilterYear = new STabFilterYear(miClient, this);
            moTabFilterYear.setYearToolTipText("Ejercicio de OP cerradas");
            moTabFilterYear.setEnable(false);
        }
        else {
            moTabFilterDateCutOff = null;
            moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
            moTabFilterYear = null;
        }

        moTabFilterCompanyBranchEntity = new STabFilterCompanyBranchEntity(miClient, this, SDataConstantsSys.CFGS_CT_ENT_PLANT);
        moTabFilterProdOrderType = new STabFilterProductionOrderType(miClient, this);
        moDialogStockCardexProdOrder = new SDialogStockCardexProdOrder(miClient);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        addTaskBarUpperComponent(isViewForAssign() ? moTabFilterDateCutOff : moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranchEntity);
        addTaskBarUpperComponent(moTabFilterProdOrderType);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbWpAssign);
        addTaskBarUpperComponent(mjbWpReturn);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbFgAssign);
        addTaskBarUpperComponent(mjbFgReturn);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbCardex);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jtbStatusActive);
        addTaskBarUpperComponent(jtbStatusInactive);

        if (moTabFilterYear != null) {
            addTaskBarUpperSeparator();
            addTaskBarUpperComponent(moTabFilterYear);
        }

        mjbWpAssign.setEnabled(hasRightMfgWpAsg);
        mjbWpReturn.setEnabled(hasRightMfgWpRet);
        mjbFgAssign.setEnabled(hasRightMfgFgAsg);
        mjbFgReturn.setEnabled(hasRightMfgFgRet);

        // View primary keys:

        aoKeyFields = new STableField[2];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "o.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "o.id_ord");

        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        // View columns:

        aoTableColumns = new STableColumn[15];

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ord_num", "Folio orden", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "o.dt", "Fecha orden", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "o.dt_dly", "Fecha entrega", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ent_code", "Planta", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "fid_st_ord", "Estatus", STableConstants.WIDTH_ICON);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ot.tp", "Tipo orden", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ord_item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ord_item", "Ítem", STableConstants.WIDTH_ITEM_2X);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "o.qty", "Cant. orden", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ord_unit_sym", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "o.qty", "Cant. x terminar", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_fin", "Cant. terminada", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "", "Avance %", STableConstants.WIDTH_PERCENTAGE);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_qty_fin", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("o.qty", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.DIVISION, SLibRpnArgumentType.OPERATOR));
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "", "Cant. pendiente", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("o.qty", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_qty_fin", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.ITMU_UNIT);
        mvSuscriptors.add(SDataConstants.MFG_ORD);
        mvSuscriptors.add(SDataConstants.TRN_DIOG);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();

        moTablePane.setDoubleClickAction(this, "actionCardex");
    }

    private void actionStockMove(final int[] iogTypeKey) {
        if (moTablePane.getSelectedTableRow() == null) {
            miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
        }
        else {
            miClient.getGuiModule(SDataConstants.MOD_INV).setFormComplement(new STrnDiogComplement(iogTypeKey, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey()));
            if (miClient.getGuiModule(SDataConstants.MOD_INV).showForm(SDataConstants.TRN_DIOG, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(SDataConstants.TRN_DIOG);
            }
        }
    }

    private void actionWpAssign() {
        if (mjbWpAssign.isEnabled()) {
            actionStockMove(SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD);
        }
    }

    private void actionWpReturn() {
        if (mjbWpReturn.isEnabled()) {
            actionStockMove(SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET);
        }
    }

    private void actionFgAssign() {
        if (mjbWpAssign.isEnabled()) {
            actionStockMove(SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD);
        }
    }

    private void actionFgReturn() {
        if (mjbWpReturn.isEnabled()) {
            actionStockMove(SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_RET);
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
        String sqlFilter = "";
        String sqlDateCutOff = "";

        for (STableSetting setting : mvTableSettings) {
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                if (isViewForAssign()) {
                    sqlDateCutOff += setting.getSetting() == null ? "" : "AND g.dt <= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format((Date) setting.getSetting()) + "' ";
                }
                else {
                    sqlFilter += "AND " + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "o.dt");
                }
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_YEAR) {
                if (isViewForAssign() && jtbStatusInactive.isSelected()) {
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

        msSql = "SELECT o.id_year, o.id_ord, " +
                "o.dt, o.dt_dly, o.qty, " + STableConstants.ICON_MFG_ST + " + o.fid_st_ord AS fid_st_ord, " +
                "ot.tp, CONCAT(o.id_year, '-', o.num) AS f_ord_num, " +
                "cob.code AS f_cob_code, ent.code AS f_ent_code, i.item_key AS f_ord_item_key, i.item AS f_ord_item, u.symbol AS f_ord_unit_sym, " +
                "COALESCE(SUM(ge.qty * CASE WHEN (g.fid_ct_iog = " + SDataConstantsSys.TRNS_CT_IOG_OUT + ") THEN 1 ELSE -1 END), 0) AS f_qty_fin " +
                "FROM mfg_ord AS o " +
                "INNER JOIN erp.mfgu_tp_ord AS ot ON o.fid_tp_ord = ot.id_tp AND " +
                "o.fid_st_ord " + (jtbStatusInactive.isSelected() ? "= " + SDataConstantsSys.MFGS_ST_ORD_CLS + " ": "IN (" + STrnUtilities.getProdOrderActiveStatus() + ") ") + "AND " +
                "o.b_del = 0 " + sqlFilter +
                "INNER JOIN erp.bpsu_bpb AS cob ON o.fid_cob = cob.id_bpb " +
                "INNER JOIN erp.cfgu_cob_ent AS ent ON o.fid_cob = ent.id_cob AND o.fid_ent = ent.id_ent " +
                "INNER JOIN erp.itmu_item AS i ON o.fid_item_r = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON o.fid_unit_r = u.id_unit " +
                "LEFT OUTER JOIN trn_diog AS g ON o.id_year = g.fid_mfg_year_n AND o.id_ord = g.fid_mfg_ord_n AND g.b_del = 0 " + sqlDateCutOff + "AND (" +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[2] + ")) " +
                "LEFT OUTER JOIN trn_diog_ety AS ge ON g.id_year = ge.id_year AND g.id_doc = ge.id_doc AND ge.b_del = 0 AND " +
                "o.fid_item_r = ge.fid_item AND o.fid_unit_r = ge.fid_unit " +
                "GROUP BY o.id_year, o.id_ord, " +
                "o.dt, o.dt_dly, o.qty, o.fid_st_ord, ot.tp, " +
                "cob.code, ent.code, i.item_key, i.item, u.symbol ";

        if (isViewForAssign()) {
            msSql += "HAVING o.qty <> f_qty_fin ";
        }
        else {
            msSql += "HAVING o.qty = f_qty_fin ";
        }

        msSql += "ORDER BY o.id_year, o.id_ord; ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == mjbWpAssign) {
                actionWpAssign();
            }
            else if (button == mjbWpReturn) {
                actionWpReturn();
            }
            else if (button == mjbFgAssign) {
                actionFgAssign();
            }
            else if (button == mjbFgReturn) {
                actionFgReturn();
            }
            else if (button == jbCardex) {
                actionCardex();
            }
        }
        else if (e.getSource() instanceof javax.swing.JToggleButton) {
            JToggleButton toggleButton = (javax.swing.JToggleButton) e.getSource();

            if (toggleButton == jtbStatusActive) {
                if (moTabFilterYear != null) {
                    moTabFilterYear.setEnable(false);
                }
                populateTable();
            }
            else if (toggleButton == jtbStatusInactive) {
                if (moTabFilterYear != null) {
                    moTabFilterYear.setEnable(true);
                }
                populateTable();
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
