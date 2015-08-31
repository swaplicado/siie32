/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod;

import erp.mod.usr.db.SDbUser;
import javax.swing.JMenu;
import sa.lib.SLibConsts;
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
 * @author Juan Barajas
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
    public SDbRegistry getRegistry(int type) {
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
    public SGuiCatalogueSettings getCatalogueSettings(int type, int subtype, SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGridPaneView getView(int type, int subtype, SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiOptionPicker getOptionPicker(int type, int subtype, SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiForm getForm(int type, int subtype, SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiReport getReport(int type, int subtype, SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
