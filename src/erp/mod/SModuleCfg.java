/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod;

import erp.mod.cfg.db.SDbCompanyBranchEntity;
import erp.mod.cfg.db.SDbCountry;
import erp.mod.cfg.db.SDbCurrency;
import erp.mod.cfg.db.SDbDocument;
import erp.mod.cfg.db.SDbFunctionalArea;
import erp.mod.cfg.db.SDbLanguage;
import erp.mod.cfg.db.SDbShift;
import erp.mod.cfg.form.SFormFunctionalArea;
import erp.mod.cfg.view.SViewAuthorizations;
import erp.mod.cfg.view.SViewFunctionalArea;
import javax.swing.JMenu;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiCatalogueSettings;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiForm;
import sa.lib.gui.SGuiModule;
import sa.lib.gui.SGuiOptionPicker;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiReport;

/**
 *
 * @author Juan Barajas, Claudio Peña, Sergio Flores
 */
public class SModuleCfg extends SGuiModule {

    private SFormFunctionalArea moFunctionalsAreas;
    
    public SModuleCfg(SGuiClient client) {
        super(client, SModConsts.MOD_CFG_N, SLibConsts.UNDEFINED);
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
            case SModConsts.CFGU_CUR:
                registry = new SDbCurrency();
                break;
            case SModConsts.CFGU_LAN:
                registry = new SDbLanguage();
                break;
            case SModConsts.CFGU_COB_ENT:
                registry = new SDbCompanyBranchEntity();
                break;
            case SModConsts.CFGU_FUNC:
                registry = new SDbFunctionalArea();
                break;
            case SModConsts.CFGU_SHIFT:
                registry = new SDbShift();
                break;
            case SModConsts.CFGU_DOC:
                registry = new SDbDocument();
                break;
            case SModConsts.LOCU_CTY:
                registry = new SDbCountry();
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public SGuiCatalogueSettings getCatalogueSettings(final int type, final int subtype, final SGuiParams params) {
        String sql = "";
        String where = "";
        SGuiCatalogueSettings settings = null;

        switch (type) {
            case SModConsts.CFGU_CO:
                /* Use of other parameters:
                 * subtype = Filter: Module ID. Can be 'SLibConsts.UNDEFINED' meaning that all companies are requested.
                 */
                switch (subtype) {
                    case SModConsts.MOD_CFG:
                        where = "AND b_mod_cfg ";
                        break;
                    case SModConsts.MOD_FIN:
                        where = "AND b_mod_fin ";
                        break;
                    case SModConsts.MOD_PUR:
                        where = "AND b_mod_pur ";
                        break;
                    case SModConsts.MOD_SAL:
                        where = "AND b_mod_sal ";
                        break;
                    case SModConsts.MOD_INV:
                        where = "AND b_mod_inv ";
                        break;
                    case SModConsts.MOD_MKT:
                        where = "AND b_mod_mkt ";
                        break;
                    case SModConsts.MOD_LOG:
                        where = "AND b_mod_log ";
                        break;
                    case SModConsts.MOD_MFG:
                        where = "AND b_mod_mfg ";
                        break;
                    case SModConsts.MOD_HRS:
                        where = "AND b_mod_hrs ";
                        break;
                    case SModConsts.MOD_QLT:
                        where = "AND b_mod_qlt ";
                        break;
                    default:
                        // do nothing
                }
                settings = new SGuiCatalogueSettings("Empresa", 1, 0, SLibConsts.DATA_TYPE_TEXT);
                sql = "SELECT id_co AS " + SDbConsts.FIELD_ID + "1, co AS " + SDbConsts.FIELD_ITEM + ", bd AS " + SDbConsts.FIELD_COMP + " " +
                        "FROM " + SModConsts.TablesMap.get(type) + " " +
                        "WHERE NOT b_del " + where +
                        "ORDER BY co, id_co ";
                break;
            case SModConsts.CFGU_COB_ENT:
                /* Use of other parameters:
                 * subtype = Filter: Entity category ID. Can be 'SLibConsts.UNDEFINED' meaning that all categories are requested.
                 */
                if (params == null) {
                    settings = new SGuiCatalogueSettings("Entidad", 2, 1);
                    sql = "SELECT id_cob AS " + SDbConsts.FIELD_ID + "1, id_ent AS " + SDbConsts.FIELD_ID + "2, ent AS " + SDbConsts.FIELD_ITEM + ", " +
                            "id_cob AS " + SDbConsts.FIELD_FK + "1 " +
                            "FROM " + SModConsts.TablesMap.get(type) + " " +
                            "WHERE NOT b_del " + (subtype == SLibConsts.UNDEFINED ? "" : "AND fid_ct_ent = " + subtype) + " " +
                            "ORDER BY id_cob, id_ent, ent ";
                }
                else { // Requisicion de materiales, relacionado a entidad de consumo
                    settings = new SGuiCatalogueSettings("Almacén", 2, 1);
                    sql = "SELECT v.id_cob AS " + SDbConsts.FIELD_ID + "1, v.id_ent AS " + SDbConsts.FIELD_ID + "2, v.ent AS " + SDbConsts.FIELD_ITEM + ", " +
                            "v.id_cob AS " + SDbConsts.FIELD_FK + "1 " +
                            "FROM " + SModConsts.TablesMap.get(type) + " AS v " +
                            "INNER JOIN trn_mat_cons_ent_whs AS c ON v.id_cob = c.id_cob AND v.id_ent = c.id_whs " +
                            "WHERE NOT b_del AND c.id_mat_cons_ent = " + params.getType() + " " + 
                            "ORDER BY v.id_cob, v.id_ent, v.ent ";
                }
                break;
            case SModConsts.CFGU_CUR:
                settings = new SGuiCatalogueSettings("Moneda", 1, 1, SLibConsts.DATA_TYPE_TEXT);
                sql = "SELECT id_cur AS " + SDbConsts.FIELD_ID + "1, 1 AS " + SDbConsts.FIELD_FK + "1, cur AS " + SDbConsts.FIELD_ITEM + ", cur_key AS " + SDbConsts.FIELD_COMP + " " +
                        "FROM erp.cfgu_cur " +
                        "WHERE NOT b_del " +
                        "ORDER BY cur, id_cur ";
                break;
            case SModConsts.CFGS_TP_MMS:
                settings = new SGuiCatalogueSettings("Configuración", 1, 1, SLibConsts.DATA_TYPE_TEXT);
                sql = "SELECT id_tp_mms AS " + SDbConsts.FIELD_ID + "1, 1 AS " + SDbConsts.FIELD_FK + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_COMP + " " +
                        "FROM erp.cfgs_tp_mms " +
                        "WHERE NOT b_del " +
                        "ORDER BY name, id_tp_mms ";
                break;
            case SModConsts.CFGU_FUNC:
                settings = new SGuiCatalogueSettings("Área funcional", 1, 1, SLibConsts.DATA_TYPE_TEXT);
                sql = "SELECT fa.id_func AS " + SDbConsts.FIELD_ID + "1, 1 AS " + SDbConsts.FIELD_FK + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_COMP + " " +
                        "FROM " + SModConsts.TablesMap.get(type) + " AS fa ";
                if (subtype != 0) {
                    sql += "INNER JOIN usr_usr_func AS fau ON " +
                            "fau.id_func = fa.id_func AND fau.id_usr = " + subtype + " ";
                }
                sql += "WHERE NOT b_del " +
                        "ORDER BY name, fa.id_func ";
                break;
            case SModConsts.CFGU_SCA:
                settings = new SGuiCatalogueSettings("Báscula", 1);
                sql = "SELECT id_sca AS " + SDbConsts.FIELD_ID + "1, CONCAT('(', sca_key,') - ', sca) AS " + SDbConsts.FIELD_ITEM + " " +
                        "FROM " + SModConsts.TablesMap.get(type) + " ";
                break;
            case SModConsts.LOCU_CTY:
                settings = new SGuiCatalogueSettings("País", 1);
                sql = "SELECT id_cty AS " + SDbConsts.FIELD_ID + "1, cty AS " + SDbConsts.FIELD_ITEM + " " +
                        "FROM " + SModConsts.TablesMap.get(type) + " " +
                        "WHERE NOT b_del " + 
                        "ORDER BY cty, id_cty ";
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

        switch (type) {
            case SModConsts.CFGU_FUNC:
                view = new SViewFunctionalArea(miClient, "Áreas funcionales");
                break;
            case SModConsts.CFGU_AUTHORN_STEP:
                view = new SViewAuthorizations(miClient, "Autorizaciones");
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return view;
    }

    @Override
    public SGuiOptionPicker getOptionPicker(final int type, final int subtype, final SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiForm getForm(final int type, final int subtype, final SGuiParams params) {
        SGuiForm form = null;
        
        switch (type) {
            case SModConsts.CFGU_FUNC:
                if (moFunctionalsAreas == null) moFunctionalsAreas = new SFormFunctionalArea(miClient, "Área funcional");
                form = moFunctionalsAreas;
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return form;
    }

    @Override
    public SGuiReport getReport(final int type, final int subtype, final SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
