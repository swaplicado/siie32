/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableCellRendererNumber;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mitm.form.SPanelFilterItemGeneric;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.SDbMaintConfig;
import erp.mtrn.form.SDialogStockCardexHistoric;
import java.awt.Dimension;
import java.text.DecimalFormat;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Claudio Peña
 */
public class SViewItemHistoric extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.mitm.form.SPanelFilterItemGeneric moPanelFilterItemGeneric;
    private javax.swing.JButton jbStockCardex;
    private erp.mtrn.form.SDialogStockCardexHistoric moDialogStockCardexHistoric;
    private erp.mod.trn.db.SDbMaintConfig moMaintConfig;



    public SViewItemHistoric(erp.client.SClientInterface client, java.lang.String tabTitle,  int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_STK_ITEM_HIS, auxType01);

        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        moTabFilterDeleted = new STabFilterDeleted(this);
        moPanelFilterItemGeneric = new SPanelFilterItemGeneric(miClient, this);
        moDialogStockCardexHistoric = new SDialogStockCardexHistoric(miClient);
        moMaintConfig = (SDbMaintConfig) miClient.getSession().readRegistry(SModConsts.TRN_MAINT_CFG, new int[] { 1 });


        jbStockCardex = new JButton(miClient.getImageIcon(SLibConstants.ICON_KARDEX));
        jbStockCardex.setPreferredSize(new Dimension(23, 23));
        jbStockCardex.setToolTipText("Ver tarjeta auxiliar de almacén");
        jbStockCardex.addActionListener(this);
        addTaskBarUpperComponent(jbStockCardex);
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moPanelFilterItemGeneric);

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_ITM_ITEM).Level;

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[48];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "i.id_item");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 300);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 300);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ig.igen", "Ítem genérico", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "il.line", "Línea ítems", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.part_num", "Número parte", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cfdps_code", "ClaveProdServ SAT", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cfdps_name", "ProdServ SAT", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.tariff", "Fracc. arancelaria", 55);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_bulk", "Granel", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_inv", "Inventariable", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_lot", "Lote", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "i.units_cont", "U. cont.", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uc.symbol", "U. cont.", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "i.units_virt", "U. virt.", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uv.symbol", "U. virt.", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "unc.symbol", "U. cont. neto", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uncu.symbol", "U. cont. neto u.", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tua.tp_unit", "Tipo u. alt.", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tl.tp_lev", "Tipo nivel", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "si.name", "Estatus", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "i.prod_time", "Tiempo producción", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "i.prod_cost", "Costo producción $", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "i.weight_gross", "Peso bruto", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "i.weight_delivery", "Peso flete", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "i.surplus_per", "Excedente por defecto entre doctos.", STableConstants.WIDTH_PERCENTAGE);
        aoTableColumns[i++].setCellRenderer(new STableCellRendererNumber(new DecimalFormat("#,##0" + "." + SLibUtilities.textRepeat("0", 4) + "%")));
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_ref", "Referencia obligatoria", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_pre_pay", "Anticipo", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "brd.brd", "Marca", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "mfr.mfr", "Fabricante", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "emt.emt", "Elemento", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_short", "Ítem (corto)", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.custs_unit", "Unidad aduana", 50);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "i.custs_equiv", "Equiv. aduana", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_free_disc_u", "S/ descto. u.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_free_disc_ety", "S/ descto. part.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_free_disc_doc", "S/ descto. docto.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_free_price", "S/ precio", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_free_disc", "S/ descto. listas precios", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_free_comms", "S/ comisiones", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_sales_freight_req", "Req. flete ventas", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "i.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "i.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "i.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_IGEN);
        mvSuscriptors.add(SDataConstants.ITMU_LINE);
        mvSuscriptors.add(SDataConstants.ITMU_BRD);
        mvSuscriptors.add(SDataConstants.ITMU_MFR);
        mvSuscriptors.add(SDataConstants.ITMU_EMT);
        mvSuscriptors.add(SDataConstants.ITMU_VAR);
        mvSuscriptors.add(SDataConstants.ITMU_TP_LEV);
        mvSuscriptors.add(SDataConstants.ITMU_VAR);
        mvSuscriptors.add(SDataConstants.ITMU_UNIT);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        
    }

    @Override
    public void actionEdit() {
        actionCardex();

    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }
    
    private int[] getWarehouseKey() {
        int[] key = null;
        
        switch (mnTabTypeAux01) {
            case SModSysConsts.TRNX_MAINT_PART:
                key = moMaintConfig.getKeyWarehouseParts();
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_AVL:
                key = moMaintConfig.getKeyWarehouseToolsAvailable();
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_LENT:
                key = moMaintConfig.getKeyWarehouseToolsLent();
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_MAINT:
                key = moMaintConfig.getKeyWarehouseToolsMaint();
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_LOST:
                key = moMaintConfig.getKeyWarehouseToolsLost();
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }
        
        return key;
    }
    
    public void actionCardex() {
        if (jbStockCardex.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                int[] rowKey = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();
                int itemId = rowKey[0];
//                int maintUserId = 0;

                moDialogStockCardexHistoric.formReset();
                moDialogStockCardexHistoric.setFormParams(itemId, SLibConstants.UNDEFINED, SLibConstants.MODE_QTY);
                moDialogStockCardexHistoric.setVisible(true);
            }
        }
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "i.b_del = FALSE ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_ITM_ITEM_GENERIC) {
                if (((int[]) setting.getSetting())[0] != SLibConstants.UNDEFINED) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "i.fid_igen = " + ((int[]) setting.getSetting())[0] + " ";
                }
            }
        }

        msSql = "SELECT i.id_item, i.item, i.item_short, i.item_key, i.b_bulk, i.b_inv, i.b_lot, i.units_cont, i.part_num, " +
                "i.units_virt, i.prod_time, i.prod_cost, i.weight_gross, i.weight_delivery, i.surplus_per, i.b_ref, i.b_pre_pay, i.tariff, i.custs_unit, i.custs_equiv, " +
                "i.b_free_price, i.b_free_disc, i.b_free_disc_u, i.b_free_disc_ety, i.b_free_disc_doc, i.b_free_comms, i.b_sales_freight_req, i.b_del, i.ts_new, i.ts_edit, i.ts_del, " +
                "ig.igen, si.name, u.symbol, uc.symbol, uv.symbol, unc.symbol, uncu.symbol, tua.tp_unit, tl.tp_lev, brd.brd, " +
                "mfr.mfr, emt.emt, un.usr, ue.usr, ud.usr, il.line, cfdps.code AS _cfdps_code, cfdps.name AS _cfdps_name " +
                "FROM erp.itmu_item AS i " +
                "INNER JOIN erp.itmu_igen AS ig ON " +
                "i.fid_igen = ig.id_igen " +
                "INNER JOIN erp.itms_st_item AS si ON " +
                "i.fid_st_item = si.id_st_item " +
                "INNER JOIN erp.itmu_unit AS u ON " +
                "i.fid_unit = u.id_unit " +
                "INNER JOIN erp.itmu_unit AS uc ON " +
                "i.fid_unit_units_cont = uc.id_unit " +
                "INNER JOIN erp.itmu_unit AS uv ON " +
                "i.fid_unit_units_virt = uv.id_unit " +
                "INNER JOIN erp.itmu_unit AS unc ON " +
                "i.fid_unit_net_cont = unc.id_unit " +
                "INNER JOIN erp.itmu_unit AS uncu ON " +
                "i.fid_unit_net_cont_u = uncu.id_unit " +
                "INNER JOIN erp.itmu_brd AS brd ON " +
                "i.fid_brd = brd.id_brd " +
                "INNER JOIN erp.itmu_mfr AS mfr ON " +
                "i.fid_mfr = mfr.id_mfr " +
                "INNER JOIN erp.itmu_emt AS emt ON " +
                "i.fid_emt = emt.id_emt " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "i.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "i.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "i.fid_usr_del = ud.id_usr " +
                "INNER JOIN erp.itmu_tp_unit AS tua ON " +
                "i.fid_tp_unit_alt = tua.id_tp_unit " +
                "INNER JOIN erp.itmu_tp_lev AS tl ON " +
                "i.fid_tp_lev = tl.id_tp_lev " +
                "LEFT OUTER JOIN erp.itmu_line AS il ON " +
                "i.fid_line_n = il.id_line " +
                "LEFT OUTER JOIN erp.itms_cfd_prod_serv AS cfdps ON " +
                "i.fid_cfd_prod_serv_n = cfdps.id_cfd_prod_serv " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY " + (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                    "i.item_key, i.item, " :
                    "i.item, i.item_key, ") + "i.id_item ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();
            if (button == jbStockCardex) {
                actionCardex();
            }
        }
    }
}
