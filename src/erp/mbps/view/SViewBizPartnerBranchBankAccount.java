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
 * @author Alfonso Flores, Sergio Flores
 */
public class SViewBizPartnerBranchBankAccount extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;
    private int mnBizPartnerCategory;
    private boolean mbIsViewForEmployees;
    private boolean mbIsViewForAllOrEmployees;

    public SViewBizPartnerBranchBankAccount(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType) {
        super(client, tabTitle, auxType);
        mnTabTypeAux01 = auxType;
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;
        int levelRightEditByCat = SDataConstantsSys.UNDEFINED;
        STableColumn[] aoTableColumns = null;

        moTabFilterDeleted = new STabFilterDeleted(this);
        mbIsViewForEmployees = mnTabTypeAux01 == SDataConstants.BPSX_BANK_ACC_EMP;
        mbIsViewForAllOrEmployees = mnTabTypeAux01 == SDataConstants.BPSU_BANK_ACC || mnTabTypeAux01 == SDataConstants.BPSX_BANK_ACC_EMP;

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        STableField[] aoKeyFields = new STableField[2];

        if (mbIsViewForAllOrEmployees) {
            aoTableColumns = new STableColumn[27];
        }
        else {
            aoTableColumns = new STableColumn[29];
        }

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bank_acc.id_bpb");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bank_acc.id_bank_acc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (mbIsViewForAllOrEmployees) {
            moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstants.BPSU_BP);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
        }
        else {
            switch (mnTabTypeAux01) {
                case SDataConstants.BPSX_BANK_ACC_SUP:
                    levelRightEditByCat = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BK_ACC_SUP).Level;
                    moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstantsSys.BPSS_CT_BP_SUP);
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                    }
                    break;
                case SDataConstants.BPSX_BANK_ACC_CUS:
                    levelRightEditByCat = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BK_ACC_CUS).Level;
                    moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstantsSys.BPSS_CT_BP_CUS);
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave", 100);
                    }
                    break;
                case SDataConstants.BPSX_BANK_ACC_CDR:
                    levelRightEditByCat = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BK_ACC_CDR).Level;
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
                case SDataConstants.BPSX_BANK_ACC_DBR:
                    levelRightEditByCat = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BK_ACC_DBR).Level;
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
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_acc_cash.tp_acc_cash", "Tipo cuenta efectivo", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bank_acc.bank_acc", "Nombre cuenta", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bank.bp", "Banco", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cur.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bank_acc.bankb_num", "No. sucursal", 100);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bank_acc.acc_num", "No. cuenta", 100);
        aoTableColumns[i++].setApostropheOnCsvRequired(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "acc_num_std", "ClaBE", 100);
        aoTableColumns[i++].setApostropheOnCsvRequired(true);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bank_acc.agree", "Convenio", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bank_acc.code", "Código", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bank_acc.ref", "Referencia", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bank_acc.code_aba", "ABA", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bank_acc.code_swift", "SWIFT", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bank_acc.alias_baj", "Alias", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bank_acc.b_card", "Aplica tarjeta", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c_issuer.card_iss", "Emisor tarjeta", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bank_acc.b_def", "Predeterminada", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_del", "Asoc. negocios eliminado", STableConstants.WIDTH_BOOLEAN);
        
        if (mnTabTypeAux01 != SDataConstants.BPSU_BANK_ACC && mnTabTypeAux01 != SDataConstants.BPSX_BANK_ACC_EMP) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ct.b_del", "Categoría eliminada", STableConstants.WIDTH_BOOLEAN);
        }
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bpb.b_del", "Sucursal eliminada", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bank_acc.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bank_acc.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bank_acc.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bank_acc.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterBizPartner);

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BK_ACC).Level;

        jbNew.setEnabled((levelRightEditByCat >= SUtilConsts.LEV_AUTHOR || levelRightEdit >= SUtilConsts.LEV_AUTHOR) && mnTabTypeAux01 != SDataConstants.BPSU_BANK_ACC);
        jbEdit.setEnabled(mnTabTypeAux01 != SDataConstants.BPSU_BANK_ACC);
        jbDelete.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
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
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
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
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        String sqlWhere = "";
        String sqlCategoryWhere = "";
        String sqlOrder = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "bp.b_del = FALSE AND bpb.b_del = FALSE AND bank_acc.b_del = FALSE ";
                sqlCategoryWhere = " AND ct.b_del = FALSE ";
                
                if (mbIsViewForEmployees) {
                    sqlWhere += "AND e.b_act ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                sqlWhere += ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : (sqlWhere.length() == 0 ? "" : "AND ") + "bpb.fid_bp = " + (Integer) setting.getSetting() + " ");
            }
        }

        switch (mnTabTypeAux01) {
            case SDataConstants.BPSX_BANK_ACC_SUP:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_SUP;
                if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    sqlOrder = "ct.bp_key, bp.bp, ";
                }
                else {
                    sqlOrder = "bp.bp, ct.bp_key, ";
                }
                break;
            case SDataConstants.BPSX_BANK_ACC_CUS:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_CUS;
                if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    sqlOrder = "ct.bp_key, bp.bp, ";
                }
                else {
                    sqlOrder = "bp.bp, ct.bp_key, ";
                }
                break;
            case SDataConstants.BPSX_BANK_ACC_CDR:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_CDR;
                if (miClient.getSessionXXX().getParamsErp().getFkSortingCreditorTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    sqlOrder = "ct.bp_key, bp.bp, ";
                }
                else {
                    sqlOrder = "bp.bp, ct.bp_key, ";
                }
                break;
            case SDataConstants.BPSX_BANK_ACC_DBR:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_DBR;
                if (miClient.getSessionXXX().getParamsErp().getFkSortingDebtorTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    sqlOrder = "ct.bp_key, bp.bp, ";
                }
                else {
                    sqlOrder = "bp.bp, ct.bp_key, ";
                }
                break;
            default:
        }
        
        if (mbIsViewForEmployees) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "bp.b_att_emp = TRUE ";
        }

        msSql = "SELECT bank_acc.id_bpb, bank_acc.id_bank_acc, bank_acc.bank_acc, bank_acc.bankb_num, bank_acc.acc_num, erp.f_bank_acc_num_std(bank_acc.acc_num_std) as acc_num_std, bank_acc.agree, bank_acc.ref, " +
                "bank_acc.code, bank_acc.code_aba, bank_acc.code_swift, bank_acc.alias_baj, bank_acc.b_card, bp.b_del, bpb.b_del, bank_acc.b_def, bank_acc.b_del, " +
                "bp.bp, " + (mbIsViewForAllOrEmployees ? "" : "ct.bp_key, ct.b_del, ") + "bpb.bpb, ct_acc_cash.ct_acc_cash, tp_acc_cash.tp_acc_cash, " +
                "bank.bp, cur.cur_key, c_issuer.card_iss, " +
                "bank_acc.fid_usr_new, bank_acc.fid_usr_edit, bank_acc.fid_usr_del, bank_acc.ts_new, bank_acc.ts_edit, bank_acc.ts_del, un.usr, ue.usr, ud.usr " +
                "FROM erp.bpsu_bank_acc AS bank_acc " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON " +
                "bank_acc.id_bpb = bpb.id_bpb " +
                "INNER JOIN erp.bpsu_bp AS bp ON " +
                "bpb.fid_bp = bp.id_bp " +
                (mbIsViewForAllOrEmployees ? "" : "INNER JOIN erp.bpsu_bp_ct AS ct ON bp.id_bp = ct.id_bp AND ct.fid_ct_bp = " + mnBizPartnerCategory + (sqlCategoryWhere.length() == 0 ? "" : sqlCategoryWhere) + " ") +
                (mbIsViewForEmployees ? "INNER JOIN erp.hrsu_emp AS e ON bp.id_bp = e.id_emp INNER JOIN hrs_emp_member AS em ON e.id_emp = em.id_emp " : "") +
                "INNER JOIN erp.fins_ct_acc_cash AS ct_acc_cash ON " +
                "bank_acc.fid_ct_acc_cash = ct_acc_cash.id_ct_acc_cash " +
                "INNER JOIN erp.fins_tp_acc_cash AS tp_acc_cash ON " +
                "bank_acc.fid_tp_acc_cash = tp_acc_cash.id_tp_acc_cash AND bank_acc.fid_ct_acc_cash = tp_acc_cash.id_ct_acc_cash " +
                "INNER JOIN erp.bpsu_bp AS bank ON " +
                "bank_acc.fid_bank = bank.id_bp " +
                "INNER JOIN erp.cfgu_cur AS cur ON " +
                "bank_acc.fid_cur = cur.id_cur " +
                "INNER JOIN erp.finu_card_iss AS c_issuer ON " +
                "bank_acc.fid_card_iss = c_issuer.id_card_iss " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "bank_acc.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "bank_acc.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "bank_acc.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY " + (mbIsViewForAllOrEmployees ? "bp.bp, " : sqlOrder) + "bpb.bpb, bank_acc.bank_acc, bank_acc.id_bank_acc ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
}
