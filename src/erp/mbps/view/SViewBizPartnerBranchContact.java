/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.table.SFilterConstants;
import erp.table.STabFilterBizPartner;
import erp.table.STabFilterContactType;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SViewBizPartnerBranchContact extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.table.STabFilterContactType moTabFilterContactType;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;

    private int mnBizPartnerCategory;

    public SViewBizPartnerBranchContact(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType) {
        super(client, tabTitle, auxType);
        mnTabTypeAux01 = auxType;
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;
        int levelRightEditCategory = SDataConstantsSys.UNDEFINED;
        STableColumn[] aoTableColumns = null;

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterContactType = new STabFilterContactType(miClient, this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterContactType);

        STableField[] aoKeyFields = new STableField[2];

        if (mnTabTypeAux01 == SDataConstants.BPSU_BPB_CON) {
            aoTableColumns = new STableColumn[26];
        }
        else {
            aoTableColumns = new STableColumn[28];
        }

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bpb_con.id_bpb");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bpb_con.id_con");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (mnTabTypeAux01 == SDataConstants.BPSU_BPB_CON) {
            moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstants.BPSU_BP);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
        }
        else {
            switch (mnTabTypeAux01) {
                case SDataConstants.BPSX_BPB_CON_SUP:
                    moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstantsSys.BPSS_CT_BP_SUP);
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                    }

                    levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_CON_SUP).Level;
                    break;
                case SDataConstants.BPSX_BPB_CON_CUS:
                    moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstantsSys.BPSS_CT_BP_CUS);
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                    }

                    levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_CON_CUS).Level;
                    break;
                case SDataConstants.BPSX_BPB_CON_CDR:
                    moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstantsSys.BPSS_CT_BP_CDR);
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCreditorTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                    }
                    break;
                case SDataConstants.BPSX_BPB_CON_DBR:
                    moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstantsSys.BPSS_CT_BP_DBR);
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingDebtorTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                    }
                    break;
                default:
            }
        }

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal asociado", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_con.tp_con", "Tipo contacto", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb_con.charge", "Puesto", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb_con.bpb_con", "Contacto", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tel_01", "Teléfono 01", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_tel1.tp_tel", "Tipo teléfono 01", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tel_02", "Teléfono 02", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_tel2.tp_tel", "Tipo teléfono 02", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tel_03", "Teléfono 03", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_tel3.tp_tel", "Tipo teléfono 03", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb_con.nextel_id_01", "Nextel 01", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb_con.nextel_id_02", "Nextel 02", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb_con.email_01", "Cuenta correo-e 01", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb_con.email_02", "Cuenta correo-e 02", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb_con.skype_01", "Skype 01", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb_con.skype_02", "Skype 02", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_del", "Asoc. negocios eliminado", STableConstants.WIDTH_BOOLEAN);
        
        if (mnTabTypeAux01 != SDataConstants.BPSU_BPB_CON) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ct.b_del", "Categoría eliminada", STableConstants.WIDTH_BOOLEAN);
        }
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bpb.b_del", "Sucursal eliminada", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bpb_con.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bpb_con.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bpb_con.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bpb_con.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterBizPartner);

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_CON).Level;

        jbNew.setEnabled((levelRightEditCategory >=  SUtilConsts.LEV_AUTHOR || levelRightEdit >=  SUtilConsts.LEV_AUTHOR) && mnTabTypeAux01 != SDataConstants.BPSU_BPB_CON);
        jbEdit.setEnabled(mnTabTypeAux01 != SDataConstants.BPSU_BPB_CON);
        jbDelete.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSX_BP_UPD);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[1] == 1) {
                    miClient.showMsgBoxWarning(STableConstants.MSG_WAR_REGISTRY_NO_EDITABLE);
                }
                else{
                    if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                        miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabType);
                    }
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
        int sortType = SLibConstants.UNDEFINED;
        String sqlWhere = "";
        String sqlCategoryWhere = "";
        String sqlOrder = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += "bp.b_del = 0 AND bpb.b_del = FALSE AND bpb_con.b_del = FALSE ";
                sqlCategoryWhere = " AND ct.b_del = FALSE ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP_CON_TP) {
                sqlWhere += ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : (sqlWhere.isEmpty() ? "" : "AND ") + "bpb_con.fid_tp_con = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                sqlWhere += ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : (sqlWhere.isEmpty() ? "" : "AND ") + "bpb.fid_bp = " + (Integer) setting.getSetting() + " ");
            }
        }

        switch (mnTabTypeAux01) {
            case SDataConstants.BPSU_BPB_CON:
                break;
            case SDataConstants.BPSX_BPB_CON_SUP:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_SUP;
                sortType = miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId();
                break;
            case SDataConstants.BPSX_BPB_CON_CUS:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_CUS;
                sortType = miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId();
                break;
            case SDataConstants.BPSX_BPB_CON_CDR:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_CDR;
                sortType = miClient.getSessionXXX().getParamsErp().getFkSortingCreditorTypeId();
                break;
            case SDataConstants.BPSX_BPB_CON_DBR:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_DBR;
                sortType = miClient.getSessionXXX().getParamsErp().getFkSortingDebtorTypeId();
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_OPT_UNDEF);
        }

        if (sortType == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            sqlOrder = (mnTabTypeAux01 == SDataConstants.BPSU_BPB_CON ? "" : "ct.bp_key,") + " bp.bp, ";
        }
        else {
            sqlOrder = "bp.bp, " + (mnTabTypeAux01 == SDataConstants.BPSU_BPB_CON ? "" : "ct.bp_key, ");
        }

        msSql = "SELECT bpb_con.id_bpb, bpb_con.id_con, bpb_con.bpb_con, bpb_con.charge, " +
                "CONCAT(bpb_con.tel_area_code_01, CASE WHEN LENGTH(bpb_con.tel_area_code_01) = 0 OR LENGTH(bpb_con.tel_num_01) = 0 THEN '' ELSE '-' END, bpb_con.tel_num_01, CASE WHEN LENGTH(bpb_con.tel_num_01) = 0 OR LENGTH(bpb_con.tel_ext_01) = 0 THEN '' ELSE '-' END, bpb_con.tel_ext_01) AS tel_01, " +
                "CONCAT(bpb_con.tel_area_code_02, CASE WHEN LENGTH(bpb_con.tel_area_code_02) = 0 OR LENGTH(bpb_con.tel_num_02) = 0 THEN '' ELSE '-' END, bpb_con.tel_num_02, CASE WHEN LENGTH(bpb_con.tel_num_02) = 0 OR LENGTH(bpb_con.tel_ext_02) = 0 THEN '' ELSE '-' END, bpb_con.tel_ext_02) AS tel_02, " +
                "CONCAT(bpb_con.tel_area_code_03, CASE WHEN LENGTH(bpb_con.tel_area_code_03) = 0 OR LENGTH(bpb_con.tel_num_03) = 0 THEN '' ELSE '-' END, bpb_con.tel_num_03, CASE WHEN LENGTH(bpb_con.tel_num_03) = 0 OR LENGTH(bpb_con.tel_ext_03) = 0 THEN '' ELSE '-' END, bpb_con.tel_ext_03) AS tel_03, " +
                "bpb_con.nextel_id_01, bpb_con.nextel_id_02, bpb_con.email_01, bpb_con.email_02, bpb_con.skype_01, bpb_con.skype_02, bp.b_del, bpb.b_del, bpb_con.b_del, " +
                "bp.bp, " + (mnTabTypeAux01 == SDataConstants.BPSU_BPB_CON ? "" :"ct.bp_key, ct.b_del, ") + "bpb.bpb, tp_con.tp_con, tp_tel1.tp_tel, tp_tel2.tp_tel, tp_tel3.tp_tel, " +
                "bpb_con.fid_usr_new, bpb_con.fid_usr_edit, bpb_con.fid_usr_del, bpb_con.ts_new, bpb_con.ts_edit, bpb_con.ts_del, un.usr, ue.usr, ud.usr " +
                "FROM erp.bpsu_bpb_con AS bpb_con " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON " +
                "bpb_con.id_bpb = bpb.id_bpb " +
                "INNER JOIN erp.bpsu_bp AS bp ON " +
                "bpb.fid_bp = bp.id_bp " +
                (mnTabTypeAux01 == SDataConstants.BPSU_BPB_CON ? "" : "INNER JOIN erp.bpsu_bp_ct AS ct ON bp.id_bp = ct.id_bp AND ct.fid_ct_bp = " + mnBizPartnerCategory + (sqlCategoryWhere.length() == 0 ? "" : sqlCategoryWhere) + " ") +
                "INNER JOIN erp.bpss_tp_con AS tp_con ON " +
                "bpb_con.fid_tp_con = tp_con.id_tp_con " +
                "INNER JOIN erp.bpss_tp_tel AS tp_tel1 ON " +
                "bpb_con.fid_tp_tel_01 = tp_tel1.id_tp_tel " +
                "INNER JOIN erp.bpss_tp_tel AS tp_tel2 ON " +
                "bpb_con.fid_tp_tel_02 = tp_tel2.id_tp_tel " +
                "INNER JOIN erp.bpss_tp_tel AS tp_tel3 ON " +
                "bpb_con.fid_tp_tel_03 = tp_tel3.id_tp_tel " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "bpb_con.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "bpb_con.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "bpb_con.fid_usr_del = ud.id_usr " +
                (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere) +
                "ORDER BY "  + (mnTabTypeAux01 == SDataConstants.BPSU_BPB ? "bp.bp, " : sqlOrder) + "bpb_con.id_bpb, bpb_con.bpb_con, bpb_con.id_con ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
}
