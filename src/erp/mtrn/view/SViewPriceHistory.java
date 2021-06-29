/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import javax.swing.JButton;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;
import erp.mtrn.form.SFormOptionPickerPriceHistory;
import erp.table.SFilterConstants;
import erp.table.STabFilterFunctionalArea;
import java.awt.Dimension;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SViewPriceHistory extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbCardex;
    private erp.mtrn.form.SFormOptionPickerPriceHistory moFormOptionPickerPriceHistory;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;

    public SViewPriceHistory(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_PRICE_HIST, auxType01, 0);
        initComponents();
    }

    private void initComponents() {
        int i;

        jbCardex = new JButton(miClient.getImageIcon(SLibConstants.ICON_KARDEX));
        jbCardex.setPreferredSize(new Dimension(23, 23));
        jbCardex.setToolTipText("Ver historial de precios");
        jbCardex.addActionListener(this);

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbCardex);

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[7];

        moFormOptionPickerPriceHistory = new SFormOptionPickerPriceHistory(miClient, SDataConstants.TRNX_PRICE_HIST, mnTabTypeAux01);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this, new int[] { miClient.getSession().getUser().getPkUserId() });
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterFunctionalArea);

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
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ig.igen", "Ítem genérico", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "line", "Línea ítem", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_last_price_u", "Últ. precio u. $", STableConstants.WIDTH_VALUE_UNITARY);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_last_date", "F. últ. precio u.", STableConstants.WIDTH_DATE);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_IGEN);
        mvSuscriptors.add(SDataConstants.ITMU_LINE);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);

        populateTable();

        moTablePane.setDoubleClickAction(this, "actionCardex");
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
        if (jbCardex.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {

                moFormOptionPickerPriceHistory.formReset();
                moFormOptionPickerPriceHistory.setFilterKey(new int[] { mnTabTypeAux01, ((int []) moTablePane.getSelectedTableRow().getPrimaryKey())[0] });
                moFormOptionPickerPriceHistory.formRefreshOptionPane();
                moFormOptionPickerPriceHistory.setItem(new String[] {
                    (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                        moTablePane.getSelectedTableRow().getValues().get(1).toString() : moTablePane.getSelectedTableRow().getValues().get(0).toString()),
                    (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                        moTablePane.getSelectedTableRow().getValues().get(0).toString() : moTablePane.getSelectedTableRow().getValues().get(1).toString()),
                     moTablePane.getSelectedTableRow().getValues().get(4).toString()
                     });
                moFormOptionPickerPriceHistory.setFormVisible(true);
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "item.b_del = FALSE ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (! ((String) setting.getSetting()).isEmpty()) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
        }

        msSql = "SELECT i.id_item, i.item, i.item_key, ig.igen, COALESCE(l.line, '(N/A)') AS line, u.symbol, MAX(d.dt) AS f_last_date, " +
            "(SELECT MAX(ets.price_u) " +
            "FROM trn_dps AS ds " +
            "INNER JOIN trn_dps_ety AS ets ON ds.id_year = ets.id_year AND ds.id_doc = ets.id_doc AND ds.b_del = 0 AND ets.b_del = 0 AND " +
            "ds.fid_ct_dps = " +  mnTabTypeAux01 + " AND ds.fid_cl_dps = " +
            (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1] : SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] ) + " " +
            "WHERE ds.dt = MAX(d.dt) AND ets.fid_item = i.id_item) AS f_last_price_u, d.* " +
            "FROM trn_dps AS d " +
            "INNER JOIN trn_dps_ety AS et ON d.id_year = et.id_year AND d.id_doc = et.id_doc AND d.b_del = 0 AND et.b_del = 0 AND " +
            "d.fid_ct_dps = " +  mnTabTypeAux01 + " AND d.fid_cl_dps = " +
            (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1] : SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] ) + " " +
            "INNER JOIN erp.itmu_item AS i ON et.fid_item = i.id_item " +
            "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " +
            "INNER JOIN erp.itmu_unit AS u ON i.fid_unit = u.id_unit " +
            "LEFT OUTER JOIN erp.itmu_line AS l ON i.fid_line_n = l.id_line " +
            "GROUP BY i.id_item, i.item, i.item_key, ig.igen, l.line, u.symbol " +
            "ORDER BY " + (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                    "i.item_key, i.item, " : "i.item, i.item_key, ") + "i.id_item ";
    }


    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbCardex) {
                actionCardex();
            }
        }
    }
}
