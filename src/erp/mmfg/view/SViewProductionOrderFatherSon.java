/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.view;

import javax.swing.JButton;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.SLibConstants;

/**
 *
 * @author Néstor Ávalos
 */

public class SViewProductionOrderFatherSon extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewProductionOrderFatherSon(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.MFGX_ORD_FAT_SON);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[20];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "o.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "o.id_ord");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num_fat", "Folio padre", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpbf.code", "Sucursal emp. padre", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "entf.code", "Planta suc. padre", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tpf.tp", "Tipo padre", 60);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_dt_fat", "F. doc. padre", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ref_fat", "Ref. padre", 60);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "of.qty", "Cantidad padre", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererMass());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uf.symbol", "Unidad padre", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_lot_fat", "Lote padre", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_cad_fat", "F. cad. padre", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num_son", "Folio hijo", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.code", "Sucursal emp. hijo", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ent.code", "Planta suc. hijo", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp.tp", "Tipo hijo", 60);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_dt_son", "F. doc. hijo", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ref_son", "Ref. hijo", 60);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "o.qty", "Cantidad hijo", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererMass());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad hijo", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_lot_son", "Lote hijo", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_cad_son", "F. cad. hijo", STableConstants.WIDTH_DATE);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.BPSU_BP); // ***
        mvSuscriptors.add(SDataConstants.MFG_ORD);
        mvSuscriptors.add(SDataConstantsSys.MFGS_ST_ORD_NEW);
        mvSuscriptors.add(SDataConstantsSys.MFGS_ST_ORD_LOT);
        mvSuscriptors.add(SDataConstantsSys.MFGS_ST_ORD_LOT_ASIG);
        mvSuscriptors.add(SDataConstantsSys.MFGS_ST_ORD_PROC);
        mvSuscriptors.add(SDataConstantsSys.MFGS_ST_ORD_END);
        mvSuscriptors.add(SDataConstantsSys.MFGS_ST_ORD_CLS);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.MFG_BOM);
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
        int[] period = null;
        java.lang.String sqlWhere = "";

        erp.lib.table.STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "of.b_del = FALSE ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                period = (int[]) setting.getSetting();
                switch (period.length) {
                    case 1:
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "YEAR(o.dt) = " + period[0] + " ";
                        break;
                    case 2:
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "YEAR(o.dt) = " + period[0] + " AND MONTH(o.dt) = " + period[1] + " ";
                        break;
                    case 3:
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "YEAR(o.dt) = " + period[0] + " AND MONTH(o.dt) = " + period[1] + " AND DAY(o.dt) = " + period[2] + " ";
                        break;
                    default:
                        break;
                }
            }
        }

        msSql = "SELECT of.id_year, of.id_ord, o.id_year, o.id_ord, " +
            "CONCAT(of.id_year, '-',of.num) AS f_num_fat, bpbf.code, entf.code, tpf.tp, of.ref AS f_ref_fat, of.dt AS f_dt_fat, " +
            "of.qty, uf.symbol, COALESCE(lf.lot, '') AS f_lot_fat, lf.dt_exp_n AS f_cad_fat, " +
            "CONCAT(o.id_year, '-',o.num) AS f_num_son, bpb.code, ent.code, tp.tp, o.ref AS f_ref_son, o.dt AS f_dt_son, " +
            "o.qty, u.symbol, COALESCE(ls.lot, '') AS f_lot_son, ls.dt_exp_n AS f_cad_son " +
            "FROM mfg_ord AS o " +
            "INNER JOIN mfg_ord AS of ON o.fid_ord_year_n = of.id_year AND o.fid_ord_n = of.id_ord AND of.b_del = 0 AND o.b_del = 0 " +
            "INNER JOIN erp.itmu_unit AS u ON o.fid_unit_r = u.id_unit " +
            "INNER JOIN erp.bpsu_bpb AS bpb ON o.fid_cob = bpb.id_bpb " +
            "INNER JOIN erp.cfgu_cob_ent AS ent ON o.fid_cob = ent.id_cob AND o.fid_ent = ent.id_ent " +
            "INNER JOIN erp.mfgu_tp_ord AS tp ON o.fid_tp_ord = tp.id_tp " +
            "INNER JOIN erp.itmu_unit AS uf ON of.fid_unit_r = uf.id_unit " +
            "INNER JOIN erp.bpsu_bpb AS bpbf ON of.fid_cob = bpbf.id_bpb " +
            "INNER JOIN erp.cfgu_cob_ent AS entf ON of.fid_cob = entf.id_cob AND of.fid_ent = entf.id_ent " +
            "INNER JOIN erp.mfgu_tp_ord AS tpf ON of.fid_tp_ord = tpf.id_tp " +
            "LEFT OUTER JOIN trn_lot AS ls ON o.fid_lot_item_nr = ls.id_item AND o.fid_lot_unit_nr = ls.id_unit AND o.fid_lot_n = ls.id_lot " +
            "LEFT OUTER JOIN trn_lot AS lf ON of.fid_lot_item_nr = lf.id_item AND of.fid_lot_unit_nr = lf.id_unit AND of.fid_lot_n = lf.id_lot " +
            (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
            "ORDER BY of.id_year, of.id_ord ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}
