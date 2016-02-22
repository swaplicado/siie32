/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod;

import erp.mod.bps.db.SBpsUtils;
import erp.mod.bps.db.SDbBizPartner;
import erp.mod.bps.db.SDbBizPartnerBranch;
import erp.mod.bps.db.SDbBizPartnerBranchBankAccount;
import erp.mod.bps.db.SDbBizPartnerCategory;
import erp.mod.fin.db.SFinConsts;
import java.util.ArrayList;
import javax.swing.JMenu;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistrySysFly;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiCatalogueSettings;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiForm;
import sa.lib.gui.SGuiModule;
import sa.lib.gui.SGuiOptionPicker;
import sa.lib.gui.SGuiOptionPickerSettings;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiReport;
import sa.lib.gui.bean.SBeanOptionPicker;

/**
 *
 * @author Juan Barajas
 */
public class SModuleBps extends SGuiModule {

    private SBeanOptionPicker moPickerBankAccount;

    public SModuleBps(SGuiClient client) {
        super(client, SModConsts.MOD_BPS_N, SLibConsts.UNDEFINED);
        moModuleIcon = miClient.getImageIcon(mnModuleType);
    }

    @Override
    public JMenu[] getMenus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry getRegistry(final int type, final SGuiParams params) {
        SDbRegistry registry = null;

        switch (type) {
            case SModConsts.BPSS_CT_BP:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_ct_bp = " + pk[0] + " "; }
                };
                break;
            case SModConsts.BPSU_BP:
                registry = new SDbBizPartner();
                break;
            case SModConsts.BPSU_BP_CT:
                registry = new SDbBizPartnerCategory();
                break;
            case SModConsts.BPSU_BPB:
                registry = new SDbBizPartnerBranch();
                break;
            case SModConsts.BPSU_BANK_ACC:
                registry = new SDbBizPartnerBranchBankAccount();
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public SGuiCatalogueSettings getCatalogueSettings(final int type, final int subtype, final SGuiParams params) {
        String sql = "";
        SGuiCatalogueSettings settings = null;

        switch (type) {
            case SModConsts.BPSS_LINK:
                settings = new SGuiCatalogueSettings("Tipo de referencia", 1);
                sql = "SELECT id_link AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + " FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = FALSE "
                        + "ORDER BY id_link ";
                break;
            case SModConsts.BPSS_CT_BP:
                settings = new SGuiCatalogueSettings("Categoría de asociado de negocios", 1);
                sql = "SELECT id_ct_bp AS " + SDbConsts.FIELD_ID + "1, ct_bp AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY ct_bp, id_ct_bp ";
                break;
            case SModConsts.BPSU_TP_BP:
                settings = new SGuiCatalogueSettings("Tipo de asociado de negocios", 1);
                sql = "SELECT tp_idx AS " + SDbConsts.FIELD_ID + "1, tp_bp AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0" + (params == null ? "" : " AND id_ct_bp = " + params.getKey()[0]) + " "
                        +"ORDER BY tp_bp, id_ct_bp, id_tp_bp ";
                break;
            case SModConsts.BPSU_BP:
                /* Use of other parameters:
                 * subtype = Filter: Business partner category ID. Can be 'SLibConsts.UNDEFINED' meaning that all categories are requested.
                 */
                settings = new SGuiCatalogueSettings(SBpsUtils.getBizPartnerCategoryName(subtype, SUtilConsts.NUM_SNG), 1, 0, SLibConsts.DATA_TYPE_TEXT);
                settings.setCodeApplying(true);
                sql = "SELECT bp.id_bp AS " + SDbConsts.FIELD_ID + "1, bp.bp AS " + SDbConsts.FIELD_ITEM + ", "
                        + (/*Remove this! XXX >>>*/params == null && /*<<< XXX Remove this!*/subtype == SLibConsts.UNDEFINED ? "'' " : "ct.bp_key ") + "AS " + SDbConsts.FIELD_COMP + ", "   // XXX eliminate this line of code, complement is not required!!!
                        + (/*Remove this! XXX >>>*/params == null && /*<<< XXX Remove this!*/subtype == SLibConsts.UNDEFINED ? "'' " : "ct.bp_key ") + "AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " AS bp "
                        /*Remove this! XXX >>>*/+ (params == null ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS ct ON bp.id_bp = ct.id_bp AND ct.id_ct_bp = " + params.getKey()[0] + " AND ct.b_del = 0 ")/*<<< XXX Remove this!*/
                        + (subtype == SLibConsts.UNDEFINED ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS ct ON bp.id_bp = ct.id_bp AND ct.id_ct_bp = " + subtype + " AND ct.b_del = 0 ")
                        + "WHERE bp.b_del = 0 "
                        + "ORDER BY bp.bp, bp.id_bp ";
                break;
            case SModConsts.BPSU_BPB:
                /* Use of other parameters:
                 * subtype = Filter: Business partner ID.
                 */
                settings = new SGuiCatalogueSettings("Sucursal", 1, 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_bpb AS " + SDbConsts.FIELD_ID + "1, bpb AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + ", "
                        + "fid_bp AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0 "
                        + (subtype == SLibConsts.UNDEFINED ? "" : "AND fid_bp = " + subtype) + " "
                        + "ORDER BY fid_tp_bpb, bpb, id_bpb ";
                break;
            case SModConsts.BPSU_BPB_ADD:
                settings = new SGuiCatalogueSettings("Domicilio", 2, 1, SLibConsts.DATA_TYPE_BOOL);
                sql = "SELECT a.id_bpb AS " + SDbConsts.FIELD_ID + "1, a.id_add AS " + SDbConsts.FIELD_ID + "2, "
                        + "CONCAT(a.bpb_add, IF(a.b_def, ' (P)', ''), ' - ', a.street, ' ', RTRIM(CONCAT(a.street_num_ext, ' ', a.street_num_int)), '; CP ', a.zip_code, '; ', a.locality, ', ', a.state) AS  " + SDbConsts.FIELD_ITEM + ", "
                        + "a.id_bpb AS " + SDbConsts.FIELD_FK + "1, a.b_def AS " + SDbConsts.FIELD_COMP + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " AS a "
                        + "WHERE a.b_del = 0 " + (params == null ? "" : "AND a.id_bpb = " + params.getKey()[0]) + " "
                        + "ORDER BY a.fid_tp_add, a.bpb_add, a.street, a.street_num_ext, a.street_num_int, a.id_bpb, a.id_add ";
                break;
            case SModConsts.BPSU_BANK_ACC:
                /* Use of other parameters:
                 * params.getKey() = Filter: Business partner ID. Must be provided allways.
                 */
                settings = new SGuiCatalogueSettings(SUtilConsts.TXT_BANK, 2);
                sql = "SELECT bbbk.id_bpb AS " + SDbConsts.FIELD_ID + "1, bbbk.id_bank_acc AS " + SDbConsts.FIELD_ID + "2, "
                        + "CONCAT('" + SFinConsts.TXT_BANK_BANK + ": ', bk.bp_comm, '; " + SFinConsts.TXT_BANK_BRA + ": ', bbbk.bankb_num, '; " + SFinConsts.TXT_BANK_ACC + ": ', bbbk.acc_num, ' (', cu.cur_key, ')') AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " AS bbbk "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bb ON bbbk.id_bpb = bb.id_bpb AND bb.fid_bp = " + params.getKey()[0] + " AND bbbk.b_del = 0 "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bk ON bbbk.fid_bank = bk.id_bp "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS cu ON bbbk.fid_cur = cu.id_cur "
                        + "ORDER BY " + SDbConsts.FIELD_ITEM + ", bbbk.id_bpb, bbbk.id_bank_acc ";
                break;
            case SModConsts.BPSX_BP_ATT_CARR:
                settings = new SGuiCatalogueSettings("Transportista", 1);
                sql = "SELECT bp.id_bp AS " + SDbConsts.FIELD_ID + "1, bp.bp AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp "
                        + "WHERE bp.b_del = 0 AND bp.b_att_car = 1  "
                        + "ORDER BY bp.bp, bp.id_bp ";
                break;
            case SModConsts.BPSX_BP_X_SAL_AGT:
                settings = new SGuiCatalogueSettings("Agente de ventas", 1);
                sql = "SELECT id_bp AS " + SDbConsts.FIELD_ID + "1, bp AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " "
                        + "WHERE b_del = 0 AND b_att_sal_agt = TRUE "
                        + "ORDER BY bp, id_bp ";
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        if (settings != null) {
            settings.setSql(sql);
        }

        return settings;
    }

    @Override
    public SGridPaneView getView(final int type, final int subtype, final SGuiParams params) {
        SGridPaneView view = null;

        return view;
    }

    @Override
    public SGuiOptionPicker getOptionPicker(final int type, final int subtype, final SGuiParams params) {
        String sql = "";
        ArrayList<SGridColumnForm> gridColumns = new ArrayList<SGridColumnForm>();
        SGuiOptionPickerSettings settings = null;
        SGuiOptionPicker picker = null;

        switch (type) {
            case SModConsts.BPSU_BP:
                /* Use of other parameters:
                 * subtype = Filter: Business partner category ID. Can be 'SLibConsts.UNDEFINED' meaning that all categories are requested.
                 */
                sql = "SELECT b.id_bp AS " + SDbConsts.FIELD_ID + "1, "
                        + "b.bp AS " + SDbConsts.FIELD_PICK + "1, b.fiscal_id AS " + SDbConsts.FIELD_PICK + "2 "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b "
                        + (subtype == SLibConsts.UNDEFINED ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = " + subtype + " AND bc.b_del = 0 ")
                        + "WHERE b.b_del = 0 "
                        + "ORDER BY b.bp, b.id_bp ";
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, SBpsUtils.getBizPartnerCategoryName(subtype, SUtilConsts.NUM_SNG)));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "RFC"));
                settings = new SGuiOptionPickerSettings(SBpsUtils.getBizPartnerCategoryName(subtype, SUtilConsts.NUM_PLR), sql, gridColumns, 1);

                if (moPickerBankAccount == null) {
                    moPickerBankAccount = new SBeanOptionPicker();
                    moPickerBankAccount.setPickerSettings(miClient, type, subtype, settings);
                }
                picker = moPickerBankAccount;
                break;

            case SModConsts.BPSU_BANK_ACC:
                /* Use of other parameters:
                 * subtype = Filter: Business partner category ID. Can be 'SLibConsts.UNDEFINED' meaning that all categories are requested.
                 */
                sql = "SELECT b.id_bp AS " + SDbConsts.FIELD_ID + "1, bbbk.id_bpb AS " + SDbConsts.FIELD_ID + "2, bbbk.id_bank_acc AS " + SDbConsts.FIELD_ID + "3, "
                        + "b.bp AS " + SDbConsts.FIELD_PICK + "1, bb.bpb AS " + SDbConsts.FIELD_PICK + "2, "
                        + "CONCAT('" + SFinConsts.TXT_BANK_BANK + ": ', bk.bp_comm, '; " + SFinConsts.TXT_BANK_BRA + ": ', bbbk.bankb_num, '; " + SFinConsts.TXT_BANK_ACC + ": ', bbbk.acc_num) AS " + SDbConsts.FIELD_PICK + "3, "
                        + "cu.cur_key AS " + SDbConsts.FIELD_PICK + "4 "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bb ON b.id_bp = bb.fid_bp AND b.b_del = 0 AND bb.b_del = 0 "
                        + (subtype == SLibConsts.UNDEFINED ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = " + subtype + " AND bc.b_del = 0 ")
                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BANK_ACC) + " AS bbbk ON bb.id_bpb = bbbk.id_bpb AND bbbk.fid_ct_acc_cash = " + SModSysConsts.FINS_CT_ACC_CASH_BANK + " AND bbbk.b_del = 0 "
                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bk ON bbbk.fid_bank = bk.id_bp "
                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS cu ON bbbk.fid_cur = cu.id_cur "
                        + "ORDER BY b.bp, b.id_bp, bb.bpb, bbbk.id_bpb, " + SDbConsts.FIELD_PICK + "3, bbbk.id_bank_acc ";
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, SBpsUtils.getBizPartnerCategoryName(subtype, SUtilConsts.NUM_SNG)));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, SUtilConsts.TXT_BRANCH));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SUtilConsts.TXT_BANK));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, SUtilConsts.TXT_CURRENCY));
                settings = new SGuiOptionPickerSettings(SUtilConsts.TXT_BANK_PLR, sql, gridColumns, 3);

                if (moPickerBankAccount == null) {
                    moPickerBankAccount = new SBeanOptionPicker();
                    moPickerBankAccount.setPickerSettings(miClient, type, subtype, settings);
                }
                picker = moPickerBankAccount;
                break;

            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return picker;
    }

    @Override
    public SGuiForm getForm(final int type, final int subtype, final SGuiParams params) {
        SGuiForm form = null;

        return form;
    }

    @Override
    public SGuiReport getReport(final int type, final int subtype, final SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
