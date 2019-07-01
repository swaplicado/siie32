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
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Sergio Flores
 */
public class SViewBizPartnerAddressee extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;

    private int mnBizPartnerCategory;
    private int mnSortingType;

    /**
     * 
     * @param client GUI client.
     * @param tabTitle Tab title.
     * @param auxType Auxiliar type (constants defined in SDataConstants.BPSX_BP_...);
     */
    public SViewBizPartnerAddressee(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType) {
        super(client, tabTitle, SDataConstants.BPSU_BP_ADDEE, auxType);
        mnTabTypeAux01 = auxType;
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEditCategory = 0;
        
        // Columns:

        STableField[] aoKeyFields = new STableField[1];
        i = 0;
        
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "addee.id_bp_addee");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        mnBizPartnerCategory = 0;
        mnSortingType = 0;
        
        switch (mnTabTypeAux01) {
            case SDataConstants.BPSX_BP_SUP:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_SUP;
                mnSortingType = miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId();
                levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_SUP).Level;
                break;
                
            case SDataConstants.BPSX_BP_CUS:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_CUS;
                mnSortingType = miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId();
                levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_CUS).Level;
                break;
                
            case SDataConstants.BPSX_BP_CDR:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_CDR;
                mnSortingType = miClient.getSessionXXX().getParamsErp().getFkSortingCreditorTypeId();
                levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_CDR).Level;
                break;
                
            case SDataConstants.BPSX_BP_DBR:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_DBR;
                mnSortingType = miClient.getSessionXXX().getParamsErp().getFkSortingDebtorTypeId();
                levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_DBR).Level;
                break;
                
            default:
        }
        
        STableColumn[] aoTableColumns = new STableColumn[23];
        i = 0;
        
        if (mnSortingType == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpct.bp_key", "Clave", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpct.bp_key", "Clave", 100);
        }
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addee.name", "Nombre destinatario", 250);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addee.fiscal_id", "ID fiscal destinatario", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addee.address", "Id. domicilio", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addee.street", "Calle", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addee.street_num_ext", "Número ext.", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addee.street_num_int", "Número int.", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addee.neighborhood", "Colonia", 150);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addee.zip_code", "CP", 50);
        aoTableColumns[i++].setApostropheOnCsvRequired(true);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addee.locality", "Localidad", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addee.county", "Municipio", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addee.state", "Estado", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cty_abbr", "País", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_del", "Asoc. negocios eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bpct.b_del", "Categoría eliminada", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "addee.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "addee.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "addee.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "addee.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
        
        // Filters:

        moTabFilterDeleted = new STabFilterDeleted(this);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, mnBizPartnerCategory);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterBizPartner);
        
        // Permissions:

        int levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP).Level;

        jbNew.setEnabled(levelRightEditCategory >= SUtilConsts.LEV_AUTHOR || levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightEditCategory >= SUtilConsts.LEV_AUTHOR || levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbDelete.setEnabled(false);
        
        // Suscriptions:

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        String sqlOrder = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere = "NOT bp.b_del AND NOT addee.b_del ";
            }

            if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                int filter = (Integer) setting.getSetting();
                sqlWhere += (filter == 0 ? "" : (sqlWhere.isEmpty() ? "" : "AND ") + "addee.fid_bp = " + filter + " ");
            }
        }

        if (mnSortingType == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            sqlOrder = "bpct.bp_key, bp.bp, bp.id_bp, ";
        }
        else {
            sqlOrder = "bp.bp, bpct.bp_key, bp.id_bp, ";
        }

        msSql = "SELECT addee.id_bp_addee, addee.name, addee.fiscal_id, addee.address, " +
                "addee.street, addee.street_num_ext, addee.street_num_int, addee.neighborhood, addee.zip_code, " +
                "addee.b_del, addee.ts_new, addee.ts_edit, addee.ts_del, " +
                "bp.bp, bp.b_del, bpct.bp_key, bpct.b_del, " +
                "addee.locality, addee.county, addee.state, " +
                "COALESCE(cty.cty_abbr, '" + miClient.getSession().getSessionCustom().getLocalCountryCode() + "') AS f_cty_abbr, " +
                "un.usr, ue.usr, ud.usr " +
                "FROM erp.bpsu_bp_addee AS addee " +
                "INNER JOIN erp.bpsu_bp AS bp ON addee.fid_bp = bp.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bpct ON bp.id_bp = bpct.id_bp AND bpct.fid_ct_bp = " + mnBizPartnerCategory + " AND NOT bpct.b_del " +
                "INNER JOIN erp.usru_usr AS un ON addee.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON addee.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON addee.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN erp.locu_cty AS cty ON addee.fid_cty_n = cty.id_cty " +
                (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere) +
                "ORDER BY " + sqlOrder + "addee.name, addee.fiscal_id, addee.address, addee.id_bp_addee;";
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showForm(mnTabType, mnTabTypeAux01, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showForm(mnTabType, mnTabTypeAux01, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabType);
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
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
}
