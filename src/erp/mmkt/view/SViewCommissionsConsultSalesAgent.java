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

/**
 *
 * @author Néstor Ávalos
 */
public class SViewCommissionsConsultSalesAgent extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewCommissionsConsultSalesAgent(erp.client.SClientInterface client, java.lang.String tabTitle, int nAuxType1) {
        super(client, tabTitle, SDataConstants.MKTX_COMMS_SAL_AGT_CONS, nAuxType1);
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
        STableColumn[] aoTableColumns = new STableColumn[5];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, isSalesType() ? "cm.id_tp_sal_agt" : "cm.id_sal_agt");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "cm.id_tp_link");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "cm.id_ref");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cm.id_dt_start");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "link.tp_link", "Tipo referencia", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ref", "Referencia", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, (isSalesType() ? "tp.tp_sal_agt" : "b.bp"), (isSalesType() ? "Tipo agente ventas" : "Agente ventas"), 200);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "cm.agt_per", "% agente", STableConstants.WIDTH_PERCENTAGE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "cm.sup_per", "% supervisor", STableConstants.WIDTH_PERCENTAGE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MKT_COMMS).Level;

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.MKTX_COMMS_SAL_AGTS);
        mvSuscriptors.add(SDataConstants.MKT_COMMS_SAL_AGT);
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
                miClient.getGuiModule(SDataConstants.MOD_MKT).refreshCatalogues(mnTabTypeAux01);
            }
	}
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_MKT).showForm(mnTabTypeAux01, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MKT).refreshCatalogues(mnTabTypeAux01);
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
                "ORDER BY link.tp_link, ref, " + (isSalesType() ? "tp.tp_sal_agt" : "b.bp");
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}
