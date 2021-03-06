/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod;

import erp.mod.cfg.db.SDbCompanyBranchEntity;
import erp.mod.cfg.db.SDbCountry;
import erp.mod.cfg.db.SDbCurrency;
import erp.mod.cfg.db.SDbFunctionalArea;
import erp.mod.cfg.db.SDbLanguage;
import erp.mod.cfg.db.SDbShift;
import erp.mod.cfg.form.SFormFunctionalArea;
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
        SGuiCatalogueSettings settings = null;

        switch (type) {
            case SModConsts.CFGU_COB_ENT:
                /* Use of other parameters:
                 * subtype = Filter: Entity category ID. Can be 'SLibConsts.UNDEFINED' meaning that all categories are requested.
                 */
                settings = new SGuiCatalogueSettings("Entidad", 2, 1);
                sql = "SELECT id_cob AS " + SDbConsts.FIELD_ID + "1, id_ent AS " + SDbConsts.FIELD_ID + "2, ent AS " + SDbConsts.FIELD_ITEM + ", " +
                        "id_cob AS " + SDbConsts.FIELD_FK + "1 " +
                        "FROM " + SModConsts.TablesMap.get(type) + " " +
                        "WHERE b_del = 0 " + (subtype == SLibConsts.UNDEFINED ? "" : "AND fid_ct_ent = " + subtype) + " " +
                        "ORDER BY id_cob, id_ent, ent ";
                break;
            case SModConsts.CFGU_CUR:
                settings = new SGuiCatalogueSettings("Moneda", 1, 1, SLibConsts.DATA_TYPE_TEXT);
                sql = "SELECT id_cur AS " + SDbConsts.FIELD_ID + "1, 1 AS " + SDbConsts.FIELD_FK + "1, cur AS " + SDbConsts.FIELD_ITEM + ", cur_key AS " + SDbConsts.FIELD_COMP + " " +
                        "FROM erp.cfgu_cur " +
                        "WHERE b_del = 0 " +
                        "ORDER BY cur, id_cur ";
                break;
            case SModConsts.CFGS_TP_MMS:
                settings = new SGuiCatalogueSettings("Configuración", 1, 1, SLibConsts.DATA_TYPE_TEXT);
                sql = "SELECT id_tp_mms AS " + SDbConsts.FIELD_ID + "1, 1 AS " + SDbConsts.FIELD_FK + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_COMP + " " +
                        "FROM erp.cfgs_tp_mms " +
                        "WHERE b_del = 0 " +
                        "ORDER BY name, id_tp_mms ";
                break;
            case SModConsts.CFGU_FUNC:
                settings = new SGuiCatalogueSettings("Área funcional", 1, 1, SLibConsts.DATA_TYPE_TEXT);
                sql = "SELECT id_func AS " + SDbConsts.FIELD_ID + "1, 1 AS " + SDbConsts.FIELD_FK + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_COMP + " " +
                        "FROM " + SModConsts.TablesMap.get(type) + " " +
                        "WHERE b_del = 0 " +
                        "ORDER BY name, id_func ";
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
