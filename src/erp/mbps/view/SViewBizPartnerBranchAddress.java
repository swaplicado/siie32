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
import javax.swing.JButton;

/**
 *
 * @author Alfonso Flores, Sergio Flores, Isabel Servín
 */
public class SViewBizPartnerBranchAddress extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;

    private int mnBizPartnerCategory;

    public SViewBizPartnerBranchAddress(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType) {
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

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        STableField[] aoKeyFields = new STableField[2];

        if (mnTabTypeAux01 == SDataConstants.BPSU_BPB_ADD || mnTabTypeAux01 == SDataConstants.BPSX_BPB_ADD_EMP) {
            aoTableColumns = new STableColumn[24];
        }
        else {
            aoTableColumns = new STableColumn[26];
        }

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ad.id_bpb");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ad.id_add");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (mnTabTypeAux01 == SDataConstants.BPSU_BPB_ADD || mnTabTypeAux01 == SDataConstants.BPSX_BPB_ADD_EMP) {
            moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstants.BPSU_BP);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
        }
        else {
            switch (mnTabTypeAux01) {
                case SDataConstants.BPSX_BPB_ADD_SUP:
                    moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstantsSys.BPSS_CT_BP_SUP);
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                    }

                    levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_ADD_SUP).Level;
                    break;
                case SDataConstants.BPSX_BPB_ADD_CUS:
                    moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstantsSys.BPSS_CT_BP_CUS);
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                    }

                    levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_ADD_CUS).Level;
                    break;
                case SDataConstants.BPSX_BPB_ADD_CDR:
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
                case SDataConstants.BPSX_BPB_ADD_DBR:
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
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ad.bpb_add_code", "Código domicilio", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp.tp_add", "Tipo domicilio", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ad.bpb_add", "Id. domicilio", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ad.street", "Calle", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ad.street_num_ext", "Número ext.", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ad.street_num_int", "Número int.", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ad.neighborhood", "Colonia", 150);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ad.zip_code", "CP", 50);
        aoTableColumns[i++].setApostropheOnCsvRequired(true);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ad.po_box", "AP", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ad.locality", "Localidad", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ad.county", "Municipio", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ad.state", "Estado", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cty_abbr", "País", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ad.b_def", "Predeterminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_del", "Asoc. negocios eliminado", STableConstants.WIDTH_BOOLEAN);
        
        if (mnTabTypeAux01 != SDataConstants.BPSU_BPB_ADD && mnTabTypeAux01 != SDataConstants.BPSX_BPB_ADD_EMP) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ct.b_del", "Categoría eliminada", STableConstants.WIDTH_BOOLEAN);
        }
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bpb.b_del", "Sucursal eliminada", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ad.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ad.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ad.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ad.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterBizPartner);

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_ADD).Level;

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        String sqlCategoryWhere = "";
        String sqlOrder = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "bp.b_del = FALSE AND bpb.b_del = FALSE AND ad.b_del = FALSE ";
                sqlCategoryWhere = " AND ct.b_del = FALSE ";
            }

            if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                sqlWhere += ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : (sqlWhere.length() == 0 ? "" : "AND ") + "bpb.fid_bp = " + (Integer) setting.getSetting() + " ");
            }
        }

        if (mnTabTypeAux01 == SDataConstants.BPSU_BPB_ADD || mnTabTypeAux01 == SDataConstants.BPSX_BPB_ADD_EMP) {
            sqlOrder = "bp.bp, bp.id_bp, ";
        }
        else {
            switch (mnTabTypeAux01) {
                case SDataConstants.BPSX_BPB_ADD_SUP:
                    mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_SUP;
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        sqlOrder = "ct.bp_key, bp.bp, bp.id_bp, ";
                    }
                    else {
                        sqlOrder = "bp.bp, ct.bp_key, bp.id_bp, ";
                    }
                    break;
                case SDataConstants.BPSX_BPB_ADD_CUS:
                    mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_CUS;
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        sqlOrder = "ct.bp_key, bp.bp, bp.id_bp, ";
                    }
                    else {
                        sqlOrder = "bp.bp, ct.bp_key, bp.id_bp, ";
                    }
                    break;
                case SDataConstants.BPSX_BPB_ADD_CDR:
                    mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_CDR;
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCreditorTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        sqlOrder = "ct.bp_key, bp.bp, bp.id_bp, ";
                    }
                    else {
                        sqlOrder = "bp.bp, ct.bp_key, bp.id_bp, ";
                    }
                    break;
                case SDataConstants.BPSX_BPB_ADD_DBR:
                    mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_DBR;
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingDebtorTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        sqlOrder = "ct.bp_key, bp.bp, bp.id_bp, ";
                    }
                    else {
                        sqlOrder = "bp.bp, ct.bp_key, bp.id_bp, ";
                    }
                    break;
                default:
            }
        }
        
        if (mnTabTypeAux01 == SDataConstants.BPSX_BPB_ADD_EMP) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "bp.b_att_emp = TRUE ";
        }

        msSql = "SELECT ad.id_bpb, ad.id_add, ad.bpb_add, ad.bpb_add_code, ad.street, ad.street_num_ext, ad.street_num_int, ad.neighborhood, " +
                "ad.zip_code, ad.po_box, ad.b_def, bp.b_del, bpb.b_del, ad.b_del, ad.ts_new, ad.ts_edit, ad.ts_del, " +
                "bpb.bpb, bp.bp, " + (mnTabTypeAux01 == SDataConstants.BPSU_BPB_ADD || mnTabTypeAux01 == SDataConstants.BPSX_BPB_ADD_EMP ? "" :"ct.bp_key, ct.b_del, ") +
                "tp.tp_add, ad.locality, ad.county, ad.state, COALESCE(cty.cty_abbr, '" + miClient.getSession().getSessionCustom().getLocalCountryCode() + "') AS f_cty_abbr, un.usr, ue.usr, ud.usr " +
                "FROM erp.bpsu_bpb_add AS ad " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON " +
                "ad.id_bpb = bpb.id_bpb " +
                "INNER JOIN erp.bpsu_bp AS bp ON " +
                "bpb.fid_bp = bp.id_bp " +
                (mnTabTypeAux01 == SDataConstants.BPSU_BPB_ADD || mnTabTypeAux01 == SDataConstants.BPSX_BPB_ADD_EMP ? "" : "INNER JOIN erp.bpsu_bp_ct AS ct ON bp.id_bp = ct.id_bp AND ct.fid_ct_bp = " + mnBizPartnerCategory + (sqlCategoryWhere.length() == 0 ? "" : sqlCategoryWhere) + " ") +
                "INNER JOIN erp.bpss_tp_add AS tp ON " +
                "ad.fid_tp_add = tp.id_tp_add " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "ad.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "ad.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "ad.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN erp.locu_cty AS cty ON " +
                "ad.fid_cty_n = cty.id_cty " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY " + sqlOrder + "bpb.bpb, bpb.id_bpb, ad.fid_tp_add, ad.bpb_add, ad.street, " +
                "ad.street_num_ext, ad.street_num_int, ad.id_bpb, ad.id_add ";
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

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
}
