/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDatePeriodRange;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import javax.swing.JButton;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewProductionByPeriod extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;

    public SViewProductionByPeriod(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.MFGX_PROD, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        addTaskBarUpperComponent(moTabFilterDatePeriodRange);
        addTaskBarUpperSeparator();

        erp.lib.table.STableField[] aoKeyFields = new STableField[2];
        erp.lib.table.STableColumn[] aoTableColumns = null;
        switch (mnTabTypeAux01) {
            case SDataConstants.MFGX_PROD_BY_ITM:
            case SDataConstants.MFGX_PROD_BY_IGEN:
                aoTableColumns = new STableColumn[8];
                break;
            case SDataConstants.MFGX_PROD_BY_ITM_BIZ:
            case SDataConstants.MFGX_PROD_BY_BIZ_ITM:
                aoTableColumns = new STableColumn[10];
                break;
        }

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "o.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "o.id_ord");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        switch (mnTabTypeAux01) {
            case SDataConstants.MFGX_PROD_BY_ITM:
                if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 300);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 300);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
                }
                break;
            case SDataConstants.MFGX_PROD_BY_IGEN:
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ig.igen", "Ítem genérico", 300);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ig.code", "Código", STableConstants.WIDTH_ITEM_KEY);
                break;
            case SDataConstants.MFGX_PROD_BY_ITM_BIZ:
                if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 300);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 300);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
                }

                if (miClient.getSessionXXX().getParamsErp().getFkSortingEmployeeTypeId() == SDataConstantsSys.CFGS_TP_SORT_NAME_KEY) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_bp", "Operador", 300);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_bp_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_bp_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_bp", "Operador", 300);
                }
                break;
            case SDataConstants.MFGX_PROD_BY_BIZ_ITM:
                if (miClient.getSessionXXX().getParamsErp().getFkSortingEmployeeTypeId() == SDataConstantsSys.CFGS_TP_SORT_NAME_KEY) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_bp", "Operador", 300);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_bp_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_bp_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_bp", "Operador", 300);
                }

                if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 300);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 300);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
                }
                break;
        }
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty", "Cantidad", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_mass", "Masa (kg)", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererMass());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_len", "Longitud (m)", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererLength());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_surf", "Supercie (m²)", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererLength());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_vol", "Volumen (m³)", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererLength());

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_MFG).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_MFG ).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    @Override
    public void createSqlQuery() {
        java.util.Date[] range = null;
        String sqlDatePeriod = "";
        String dateInit = "";
        String dateEnd = "";
        erp.lib.table.STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                range = (java.util.Date[]) setting.getSetting();
                dateInit = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]);
                dateEnd = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]);
                sqlDatePeriod += "o.dt BETWEEN '" + dateInit + "' AND '" + dateEnd + "' ";
            }
        }

        msSql = "SELECT o.id_year, o.id_ord, " +
            (mnTabTypeAux01 == SDataConstants.MFGX_PROD_BY_ITM ? "i.item_key, i.item, u.symbol, " :
            mnTabTypeAux01 == SDataConstants.MFGX_PROD_BY_IGEN ? "ig.code, ig.igen, u.symbol, " :
            "COALESCE(c.bp_key, '(N/A)') AS f_bp_key, COALESCE(b.bp, '(N/A)') AS f_bp, i.item_key, i.item, u.symbol, ") +
            "SUM(s.mov_in) AS f_qty, SUM(i.mass) AS f_mass, SUM(i.len) AS f_len, SUM(i.vol) AS f_vol, SUM(i.surf) AS f_surf " +
            "FROM mfg_ord AS o " +
            "INNER JOIN erp.itmu_item AS i ON o.fid_item_r = i.id_item AND o.b_del = 0 " +
            "INNER JOIN erp.itmu_unit AS u ON o.fid_unit_r = u.id_unit " +
            (mnTabTypeAux01 == SDataConstants.MFGX_PROD_BY_IGEN ? "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " : "") +
            "INNER JOIN trn_diog AS d ON o.id_year = d.fid_mfg_year_n AND o.id_ord = d.fid_mfg_ord_n AND " +
            "d.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_ASD[0] +
                " AND d.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_ASD[1] +
                " AND d.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_ASD[2] + " AND d.b_del = 0 " +
            "INNER JOIN trn_stk AS s ON d.id_year = s.fid_diog_year AND d.id_doc = s.fid_diog_doc " +
            (mnTabTypeAux01 == SDataConstants.MFGX_PROD_BY_ITM_BIZ || mnTabTypeAux01 == SDataConstants.MFGX_PROD_BY_BIZ_ITM ?
            "LEFT OUTER JOIN erp.bpsu_bp AS b ON o.fid_bp_ope_n = b.id_bp AND b.b_att_emp = 1 " +
            "LEFT OUTER JOIN erp.bpsu_bp_ct AS c ON b.id_bp = c.id_bp " : "") +
            "WHERE " + sqlDatePeriod + " " +
            (mnTabTypeAux01 == SDataConstants.MFGX_PROD_BY_ITM ?
            "GROUP BY i.id_item, u.id_unit " +
            "ORDER BY " +
            (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
            "i.item_key, i.item " : "i.item, i.item_key ") :
            mnTabTypeAux01 == SDataConstants.MFGX_PROD_BY_IGEN ?
            "GROUP BY ig.id_igen, u.id_unit " +
            "ORDER BY ig.code, ig.igen; " :
            "GROUP BY o.fid_bp_ope_n, i.id_item, u.id_unit " +
            "ORDER BY " +
            (mnTabTypeAux01 == SDataConstants.MFGX_PROD_BY_BIZ_ITM ?
            (miClient.getSessionXXX().getParamsErp().getFkSortingEmployeeTypeId() == SDataConstantsSys.CFGS_TP_SORT_NAME_KEY ?
            "f_bp, f_bp_key, " : "f_bp_key, f_bp, ") +
            (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
            "i.item_key, i.item " : "i.item, i.item_key; ") :
            (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
            "i.item_key, i.item, " : "i.item, i.item_key, ") +
            (miClient.getSessionXXX().getParamsErp().getFkSortingEmployeeTypeId() == SDataConstantsSys.CFGS_TP_SORT_NAME_KEY ?
            "f_bp, f_bp_key " : "f_bp_key, f_bp; ")));
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}
