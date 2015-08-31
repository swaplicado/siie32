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
 * @author Alfonso Flores
 */
public class SViewBizPartnerBranch extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;

    private int mnBizPartnerCategory;

    public SViewBizPartnerBranch(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType) {
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

        STableField[] aoKeyFields = new STableField[1];

        if (mnTabTypeAux01 == SDataConstants.BPSU_BPB) {
            aoTableColumns = new STableColumn[14];
        }
        else {
            aoTableColumns = new STableColumn[16];
        }

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bpb.id_bpb");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (mnTabTypeAux01 == SDataConstants.BPSU_BPB) {
            moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstants.BPSU_BP);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
        }
        else {
            switch (mnTabTypeAux01) {
                case SDataConstants.BPSX_BPB_SUP:
                    moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstantsSys.BPSS_CT_BP_SUP);
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                    }

                    levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_SUP).Level;
                    break;
                case SDataConstants.BPSX_BPB_CUS:
                    moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstantsSys.BPSS_CT_BP_CUS);
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                    }

                    levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_CUS).Level;
                    break;
                case SDataConstants.BPSX_BPB_CDR:
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
                case SDataConstants.BPSX_BPB_DBR:
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
            }
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal asociado", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.code", "Código", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp.tp_bpb", "Tipo Sucursal", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tax.tax_reg", "Región impuestos", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "af.tp_add_fmt", "Formato domicilio", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_del", "Asoc. negocios eliminado", STableConstants.WIDTH_BOOLEAN);
        
        if (mnTabTypeAux01 != SDataConstants.BPSU_BPB) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ct.b_del", "Categoría eliminada", STableConstants.WIDTH_BOOLEAN);
        }
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bpb.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bpb.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bpb.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bpb.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterBizPartner);

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB).Level;

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.FINU_TAX_REG);
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "bp.b_del = FALSE AND bpb.b_del = FALSE ";
                sqlCategoryWhere = " AND ct.b_del = FALSE ";
            }

            if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                sqlWhere += ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : (sqlWhere.length() == 0 ? "" : "AND ") + "bpb.fid_bp = " + (Integer) setting.getSetting() + " ");
            }
        }

        switch (mnTabTypeAux01) {
            case SDataConstants.BPSX_BPB_SUP:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_SUP;
                if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    sqlOrder = "ct.bp_key, bp.bp, ";
                }
                else {
                    sqlOrder = "bp.bp, ct.bp_key, ";
                }
                break;
            case SDataConstants.BPSX_BPB_CUS:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_CUS;
                if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    sqlOrder = "ct.bp_key, bp.bp, ";
                }
                else {
                    sqlOrder = "bp.bp, ct.bp_key, ";
                }
                break;
            case SDataConstants.BPSX_BPB_CDR:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_CDR;
                if (miClient.getSessionXXX().getParamsErp().getFkSortingCreditorTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    sqlOrder = "ct.bp_key, bp.bp, ";
                }
                else {
                    sqlOrder = "bp.bp, ct.bp_key, ";
                }
                break;
            case SDataConstants.BPSX_BPB_DBR:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_DBR;
                if (miClient.getSessionXXX().getParamsErp().getFkSortingDebtorTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    sqlOrder = "ct.bp_key, bp.bp, ";
                }
                else {
                    sqlOrder = "bp.bp, ct.bp_key, ";
                }
                break;
        }

        msSql = "SELECT bpb.id_bpb, bpb.bpb, bpb.code, bp.b_del, bpb.b_del, bpb.ts_new, bpb.ts_edit, bpb.ts_del, " +
                "bp.bp, " + (mnTabTypeAux01 == SDataConstants.BPSU_BPB ? "" : "ct.bp_key, ct.b_del, ") + "tp.tp_bpb, " +
                "tax.tax_reg, af.tp_add_fmt, un.usr, ue.usr, ud.usr " +
                "FROM erp.bpsu_bpb AS bpb " +
                "INNER JOIN erp.bpsu_bp AS bp ON " +
                "bpb.fid_bp = bp.id_bp " +
                (mnTabTypeAux01 == SDataConstants.BPSU_BPB ? "" : "INNER JOIN erp.bpsu_bp_ct AS ct ON bp.id_bp = ct.id_bp AND ct.fid_ct_bp = " + mnBizPartnerCategory + (sqlCategoryWhere.length() == 0 ? "" : sqlCategoryWhere) + " ") +
                "INNER JOIN erp.bpss_tp_bpb AS tp ON " +
                "bpb.fid_tp_bpb = tp.id_tp_bpb " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "bpb.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "bpb.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "bpb.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN erp.finu_tax_reg AS tax ON " +
                "bpb.fid_tax_reg_n = tax.id_tax_reg " +
                "LEFT OUTER JOIN erp.bpss_tp_add_fmt AS af ON " +
                "bpb.fid_tp_add_fmt_n = af.id_tp_add_fmt " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY " + (mnTabTypeAux01 == SDataConstants.BPSU_BPB ? "bp.bp, " : sqlOrder) + "bpb.bpb ";
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
