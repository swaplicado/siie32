/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod;

import erp.mod.usr.db.SDbUser;
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
 * @author Juan Barajas, Isabel Servín
 */
public class SModuleUsr extends SGuiModule {

    public SModuleUsr(SGuiClient client) {
        super(client, SModConsts.MOD_USR_N, SLibConsts.UNDEFINED);
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
            case SModConsts.USRU_USR:
                registry = new SDbUser();
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public SGuiCatalogueSettings getCatalogueSettings(final int type, final int subtype, final SGuiParams params) {
        String sql = "";
        String sqlWhere = "";
        String name = "";
        SGuiCatalogueSettings settings = null;
        
        switch (type) {
            case SModConsts.USRU_USR:
                settings = new SGuiCatalogueSettings("Usuarios", 1);
                sql = "SELECT id_usr AS " + SDbConsts.FIELD_ID + "1, "
                        + "usr AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY usr ";
                break;
            case SModConsts.USRS_LINK:
                settings = new SGuiCatalogueSettings("Tipo de referencia", 1);
                sql = "SELECT id_link AS " + SDbConsts.FIELD_ID + "1, "
                        + "name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY name ";
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiOptionPicker getOptionPicker(final int type, final int subtype, final SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiForm getForm(final int type, final int subtype, final SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiReport getReport(final int type, final int subtype, final SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
