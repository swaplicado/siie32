/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.view;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import javax.swing.JButton;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewExplotionMaterialsProductionOrder extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewExplotionMaterialsProductionOrder(erp.client.SClientInterface client, java.lang.String tabTitle, int nAuxTabType1) {
        super(client, tabTitle, SDataConstants.MFG_EXP_ORD, nAuxTabType1);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[4];
        STableColumn[] aoTableColumns = null;

        switch (mnTabTypeAux01) {
            case SDataConstants.MFGX_ORD_FOR:
                aoTableColumns = new STableColumn[14];
                break;
            default:
                aoTableColumns = new STableColumn[13];
                break;
        }

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "eo.id_exp_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "eo.id_exp");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "eo.id_ord_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "eo.id_ord");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.ref", "Referencia exp. mats.", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "e.dt_doc", "F. exp. mats.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ord", "Folio ord. prod.", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ent.code", "Planta", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "o.dt", "F. doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp.tp", "Tipo", 60);

        switch (mnTabTypeAux01) {
            case SDataConstants.MFGX_ORD_FOR:
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "o.b_for", "Pronóstico", STableConstants.WIDTH_BOOLEAN);
                break;
        }

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", 65);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Producto", 150);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "o.qty", "Cantidad", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "o.chgs", "Cargas prod.", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bom", "Fórmula", 200);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.MFG_ORD);
        mvSuscriptors.add(SDataConstants.MFG_EXP);

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
                if (miClient.getGuiModule(SDataConstants.MOD_MFG).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
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
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "e.b_del = FALSE ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "e.dt_doc");
            }
        }

        msSql = "SELECT eo.*, e.ref, e.dt_doc, CONCAT(o.id_year, '-',erp.lib_fix_int(o.id_ord,6)) AS f_ord, o.dt, o.qty, o.chgs, bpb.bpb, bpb.code, ent.ent, ent.code, tp.tp, " +
                "b.bom, i.item_key, i.item, u.symbol, st.st " +
            "FROM mfg_exp_ord AS eo " +
            "INNER JOIN mfg_exp AS e ON eo.id_exp_year = e.id_year AND eo.id_exp = e.id_exp AND e.b_del = 0 " +
            "INNER JOIN mfg_ord AS o ON eo.id_ord_year = o.id_year AND eo.id_ord = o.id_ord AND o.b_del = 0 AND o.b_for = " + (mnTabTypeAux01 != SDataConstants.MFGX_ORD_FOR ? "0 " : "1 ") +
            "INNER JOIN erp.mfgu_tp_ord AS tp ON o.fid_tp_ord = tp.id_tp " +
            "INNER JOIN mfg_bom AS b ON o.fid_bom = b.id_bom " +
            "INNER JOIN erp.itmu_item AS i ON o.fid_item_r = i.id_item " +
            "INNER JOIN erp.itmu_unit AS u ON i.fid_unit = u.id_unit " +
            "INNER JOIN erp.bpsu_bpb AS bpb ON o.fid_cob = bpb.id_bpb " +
            "INNER JOIN erp.cfgu_cob_ent AS ent ON o.fid_cob = ent.id_cob AND o.fid_ent = ent.id_ent " +
            "INNER JOIN erp.mfgs_st_ord AS st ON o.fid_st_ord = st.id_st " +
            (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
            "ORDER BY e.ref ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}
