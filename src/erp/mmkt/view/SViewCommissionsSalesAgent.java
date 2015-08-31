/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewCommissionsSalesAgent extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewCommissionsSalesAgent(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.MKT_COMMS_SAL_AGT, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        moTabFilterDeleted = new STabFilterDeleted(this);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        //jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[4];
        STableColumn[] aoTableColumns = new STableColumn[18];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, isSalesType() ? "cm.id_tp_sal_agt" : "cm.id_sal_agt");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "cm.id_tp_link");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "cm.id_ref");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cm.id_dt_start");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }
        
        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, (isSalesType() ? "tp.tp_sal_agt" : "b.bp"), (isSalesType() ? "Tipo agente ventas" : "Agente ventas"), 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "link.tp_link", "Tipo referencia", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ref", "Referencia", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "cm.id_dt_start", "Ini. vigencia", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "t.tp_comms", "Tipo comisión", 75);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "cm.agt_per", "Porcentaje agente", STableConstants.WIDTH_PERCENTAGE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "cm.agt_val_u", "Valor unitario agente", STableConstants.WIDTH_VALUE_UNITARY);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "cm.agt_val", "Valor agente", STableConstants.WIDTH_VALUE);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "cm.sup_per", "Porcentaje supervisor", STableConstants.WIDTH_PERCENTAGE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "cm.sup_val_u", "Valor unitario supervisor", STableConstants.WIDTH_VALUE_UNITARY);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "cm.sup_val", "Valor supervisor", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "cm.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "cm.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "cm.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "cm.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MKT_COMMS).Level;

        jbNew.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.MKTX_COMMS_SAL_AGTS);
        mvSuscriptors.add(SDataConstants.MKTX_COMMS_SAL_AGT_CONS);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }
    
    private boolean isSalesType() {
        return mnTabTypeAux01 == SDataConstants.MKT_COMMS_SAL_AGT_TP;
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_MKT).showForm(mnTabTypeAux01, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MKT).refreshCatalogues(mnTabType);
            }
	}
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_MKT).showForm(mnTabTypeAux01, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MKT).refreshCatalogues(mnTabType);
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "cm.b_del = FALSE ";
            }
        }

        msSql = "SELECT cm.*, " + (isSalesType() ? "tp.tp_sal_agt," : "b.bp,") + " t.tp_comms, un.usr, ue.usr, ud.usr, " +
                "CASE cm.id_tp_link " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ALL + " THEN " +
                "('" + SDataConstantsSys.TXT_TRNS_TP_LINK_ALL + "') " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CT_ITEM + " THEN (" +
                "SELECT c.ct_item FROM erp.itms_ct_item c WHERE c.ct_idx = cm.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CL_ITEM + " THEN (" +
                "SELECT c.cl_item FROM erp.itms_cl_item c WHERE c.cl_idx = cm.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_TP_ITEM + " THEN (" +
                "SELECT tp.tp_item FROM erp.itms_tp_item tp WHERE tp.tp_idx = cm.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IFAM + " THEN (" +
                "SELECT ifam.ifam FROM erp.itmu_ifam ifam WHERE ifam.id_ifam = cm.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGRP + " THEN (" +
                "SELECT igrp.igrp FROM erp.itmu_igrp igrp WHERE igrp.id_igrp = cm.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGEN + " THEN (" +
                "SELECT CONCAT(igen.igen, ' (', igen.code, ')') AS igen FROM erp.itmu_igen igen WHERE igen.id_igen = cm.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_LINE + " THEN (" +
                "SELECT line.line FROM erp.itmu_line line WHERE line.id_line = cm.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_BRD + " THEN (" +
                "SELECT CONCAT(brd.brd, ' (', brd.code, ')') AS brd FROM erp.itmu_brd brd WHERE brd.id_brd = cm.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_MFR + " THEN (" +
                "SELECT CONCAT(mfr.mfr, ' (', mfr.code, ')') AS mfr FROM erp.itmu_mfr mfr WHERE mfr.id_mfr = cm.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ITEM + " THEN (" +
                "SELECT " + (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "CONCAT(item.item_key, ' - ', item.item)" : "CONCAT(item.item, ' - ', item.item_key)") + " " +
                "FROM erp.itmu_item item WHERE item.id_item = cm.id_ref) " +
                "ELSE 'No existe tipo' " +
                "END AS ref, link.tp_link " +
                "FROM " + (isSalesType() ? "mkt_comms_sal_agt_tp" : "mkt_comms_sal_agt") + " AS cm " +
                (isSalesType() ?
                "INNER JOIN mktu_tp_sal_agt AS tp ON cm.id_tp_sal_agt = tp.id_tp_sal_agt " :
                "INNER JOIN erp.bpsu_bp AS b ON cm.id_sal_agt = b.id_bp ") +
                "INNER JOIN erp.trns_tp_link AS link ON cm.id_tp_link = link.id_tp_link " +
                "INNER JOIN erp.mkts_tp_comms AS t ON cm.fid_tp_comms = t.id_tp_comms " +
                "INNER JOIN erp.usru_usr AS un ON cm.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON cm.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON cm.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) + " " +
                "ORDER BY " + (isSalesType() ? "tp.tp_sal_agt" : "b.bp");
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}
