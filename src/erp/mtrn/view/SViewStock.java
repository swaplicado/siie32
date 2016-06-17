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
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mod.SModSysConsts;
import erp.mtrn.form.SDialogStockCardex;
import erp.table.SFilterConstants;
import erp.table.STabFilterCompanyBranchEntity;
import java.awt.Dimension;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableCellRenderer;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;

/**
 *
 * @author Sergio Flores
 */
public class SViewStock extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    public static final String TXT_DEC_INC = "Ver más decimales";
    public static final String TXT_DEC_DEC = "Ver menos decimales";

    private int mnColIn;
    private int mnColOut;
    private int mnColStock;
    private javax.swing.JButton jbCardex;
    private javax.swing.JToggleButton jtbDecimals;
    private erp.lib.table.STabFilterDate moTabFilterDate;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.table.STabFilterCompanyBranchEntity moTabFilterCompanyBranchEntity;
    private erp.mtrn.form.SDialogStockCardex moDialogStockCardex;

    /*
     * @param auxType01 Constants defined in SDataConstats (TRNX_STK_...).
     */
    public SViewStock(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRN_STK, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;

        moTabFilterDate = new STabFilterDate(miClient, this, SLibTimeUtilities.getEndOfYear(miClient.getSessionXXX().getWorkingDate()));
        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterCompanyBranchEntity = new STabFilterCompanyBranchEntity(miClient, this, SDataConstantsSys.CFGS_CT_ENT_WH);
        moDialogStockCardex = new SDialogStockCardex(miClient);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        jbCardex = new JButton(miClient.getImageIcon(SLibConstants.ICON_KARDEX));
        jbCardex.setPreferredSize(new Dimension(23, 23));
        jbCardex.setToolTipText("Ver tarjeta auxiliar de almacén");
        jbCardex.addActionListener(this);
        addTaskBarUpperComponent(jbCardex);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDate);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranchEntity);
        addTaskBarUpperSeparator();

        jtbDecimals = new JToggleButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_dec_inc.gif")));
        jtbDecimals.setSelectedIcon(new ImageIcon(getClass().getResource("/erp/img/icon_std_dec_dec.gif")));
        jtbDecimals.setPreferredSize(new Dimension(23, 23));
        jtbDecimals.setToolTipText(TXT_DEC_INC);
        jtbDecimals.addActionListener(this);
        addTaskBarUpperComponent(jtbDecimals);

        switch (mnTabTypeAux01) {
            case SDataConstants.TRNX_STK_STK:
                i = 0;
                aoKeyFields = new STableField[2];
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_item");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_unit");

                aoTableColumns = new STableColumn[8];
                break;

            case SDataConstants.TRNX_STK_STK_WH:
                i = 0;
                aoKeyFields = new STableField[4];
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_item");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_unit");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_cob");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_wh");

                aoTableColumns = new STableColumn[14];
                break;

            case SDataConstants.TRNX_STK_LOT:
                i = 0;
                aoKeyFields = new STableField[3];
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_item");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_unit");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_lot");

                aoTableColumns = new STableColumn[11];
                break;

            case SDataConstants.TRNX_STK_LOT_WH:
                i = 0;
                aoKeyFields = new STableField[5];
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_item");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_unit");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_lot");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_cob");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_wh");

                aoTableColumns = new STableColumn[13];
                break;

            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 250);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 250);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
        }

        if (showWarehouses()) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ent.code", "Almacén", STableConstants.WIDTH_CODE_COB_ENT);
        }

        if (showLots()) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "l.lot", "Lote", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "l.dt_exp_n", "Caducidad", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "l.b_block", "Bloqueado", STableConstants.WIDTH_BOOLEAN);
        }

        if (showWarehouses() && !showLots()) {
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico", "Estatus existencias", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "sc.qty_min", "Mínimo", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "sc.rop", "Pto. reorden", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "sc.qty_max", "Máximo", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        }

        mnColIn = i;
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_mov_i", "Entradas", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        mnColOut = i;
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_mov_o", "Salidas", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        mnColStock = i;
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stk", "Existencias", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_val_u", "Val. u. máx. $", STableConstants.WIDTH_VALUE_UNITARY);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "", "Val. máx. $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_stk", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_val_u", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.MULTIPLICATION, SLibRpnArgumentType.OPERATOR));

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.ITMU_UNIT);
        mvSuscriptors.add(SDataConstants.TRN_LOT);
        mvSuscriptors.add(SDataConstants.TRN_DIOG);
        mvSuscriptors.add(SDataConstants.TRN_STK_CFG);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_SUPPLY_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_SUPPLY_PEND_ETY);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_RETURN_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_RETURN_PEND_ETY);

        populateTable();

        moTablePane.setDoubleClickAction(this, "actionCardex");
    }

    private boolean showWarehouses() {
        return mnTabTypeAux01 == SDataConstants.TRNX_STK_STK_WH || mnTabTypeAux01 == SDataConstants.TRNX_STK_LOT_WH;
    }

    private boolean showLots() {
        return mnTabTypeAux01 == SDataConstants.TRNX_STK_LOT || mnTabTypeAux01 == SDataConstants.TRNX_STK_LOT_WH;
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

    public void actionCardex() {
        int mode = jtbDecimals.isSelected() ? SLibConstants.MODE_QTY_EXT : SLibConstants.MODE_QTY;
        if (jbCardex.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                int[] key = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();
                int[] whKey = showWarehouses() ? new int[] { key[key.length - 2], key[key.length - 1] } : moTabFilterCompanyBranchEntity.getCompanyBranchEntityKey();
                int itemId = key[0];
                int unitId = key[1];
                int lotId = showLots() ? key[2] : SLibConstants.UNDEFINED;

                moDialogStockCardex.formReset();
                moDialogStockCardex.setFormParams(moTabFilterDate.getDate(), itemId, unitId, lotId, whKey, mode);
                moDialogStockCardex.setVisible(true);
            }
        }
    }

    public void actionDecimals() {
        String toolTipText = !jtbDecimals.isSelected() ? TXT_DEC_INC : TXT_DEC_DEC;
        DefaultTableCellRenderer tcr = !jtbDecimals.isSelected() ?
            miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity() :
            miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary();

        moTablePane.getTableColumn(mnColIn).setCellRenderer(tcr);
        moTablePane.getTableColumn(mnColOut).setCellRenderer(tcr);
        moTablePane.getTableColumn(mnColStock).setCellRenderer(tcr);

        jtbDecimals.setToolTipText(toolTipText);

        actionRefresh(STableConstants.REFRESH_MODE_RELOAD);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        int year = 0;
        int[] key = null;
        Date date = null;
        String sqlWhere = "";
        String sqlHaving = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlHaving = "HAVING f_stk <> 0 ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                date = (Date) setting.getSetting();
                year = SLibTimeUtilities.digestYear(date)[0];
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "s.id_year = " + year + " AND " +
                        "s.dt <= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(date) + "' ";
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

        msSql = "SELECT s.id_item, s.id_unit, " +
                "i.item_key, i.item, u.symbol, " +
                (!showLots() ? "" : "s.id_lot, l.lot, l.dt_exp_n, l.b_block, ") +
                (!showWarehouses() ? "" : "s.id_cob, s.id_wh, bpb.code, ent.code, sc.qty_min, sc.qty_max, sc.rop, " +
                 "IF(SUM(s.mov_in - s.mov_out) <= sc.qty_min, " + STableConstants.ICON_VIEW_LIG_RED + ", IF(sc.qty_min < SUM(s.mov_in - s.mov_out) AND SUM(s.mov_in - s.mov_out) <= sc.rop, "  + STableConstants.ICON_VIEW_LIG_YEL + ", IF(SUM(s.mov_in - s.mov_out) > sc.rop  AND SUM(s.mov_in - s.mov_out) <= sc.qty_max, "  + STableConstants.ICON_VIEW_LIG_GRE + ", IF(SUM(s.mov_in - s.mov_out) > sc.qty_max, " + STableConstants.ICON_WARN + ", " + STableConstants.ICON_VIEW_LIG_WHI + ")))) AS f_ico, ") +
                "SUM(s.mov_in) AS f_mov_i, SUM(s.mov_out) AS f_mov_o, SUM(s.mov_in - s.mov_out) AS f_stk, " +
                "(SELECT COALESCE(MAX(sx.cost_u), 0.0) FROM trn_stk AS sx WHERE sx.id_year = " + year + " AND sx.id_item = s.id_item AND sx.id_unit = s.id_unit AND NOT sx.b_del AND " +
                "sx.fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_IN_ADJ[0] + " AND sx.fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_IN_ADJ[1] + ") AS f_val_u " +
                "FROM trn_stk AS s " +
                "INNER JOIN erp.itmu_item AS i ON s.id_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON s.id_unit = u.id_unit " +
                (!showLots() ? "" : "INNER JOIN trn_lot AS l ON s.id_item = l.id_item AND s.id_unit = l.id_unit AND s.id_lot = l.id_lot ") +
                (!showWarehouses() ? "" : "INNER JOIN erp.bpsu_bpb AS bpb ON s.id_cob = bpb.id_bpb INNER JOIN erp.cfgu_cob_ent AS ent ON s.id_cob = ent.id_cob AND s.id_wh = ent.id_ent ") +
                (!showWarehouses() ? "" : "INNER JOIN trn_stk_cfg AS sc ON sc.id_item = i.id_item AND sc.id_unit = u.id_unit AND sc.id_cob = bpb.id_bpb AND sc.id_wh = ent.id_ent ") +
                "WHERE s.b_del = 0 " +
                (sqlWhere.length() == 0 ? "" : "AND " + sqlWhere) +
                "GROUP BY s.id_item, s.id_unit, " +
                (!showLots() ? "" : "s.id_lot, l.lot, l.dt_exp_n, l.b_block, ") +
                (!showWarehouses() ? "" : "s.id_cob, s.id_wh, bpb.code, ent.code, ") +
                "i.item_key, i.item, u.symbol " +
                sqlHaving +
                "ORDER BY " + (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "i.item_key, i.item, " : "i.item, i.item_key, ") +
                "s.id_item, u.symbol, s.id_unit " +
                (!showWarehouses() ? "" : ", bpb.code, ent.code, s.id_cob, s.id_wh ") +
                (!showLots() ? "" : ", l.lot, l.dt_exp_n, l.b_block, s.id_lot ");
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbCardex) {
                actionCardex();
            }
        }
        else if (e.getSource() instanceof javax.swing.JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) e.getSource();

            if (toggleButton == jtbDecimals) {
                actionDecimals();
            }
        }
    }
}
