/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod;

import erp.mod.mkt.db.SDbCommission;
import erp.mod.mkt.db.SDbCommissionPayment;
import erp.mod.mkt.db.SDbCommissionPaymentEntry;
import erp.mod.mkt.view.SViewCommissionPayment;
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
 * @author Néstor Ávalos
 */
public class SModuleMkt extends SGuiModule {

    public SModuleMkt(SGuiClient client) {
        super(client, SModConsts.MOD_MKT_N, SLibConsts.UNDEFINED);
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
            case SModConsts.MKT_COMMS:
                registry = new SDbCommission();
                break;
            case SModConsts.MKT_COMMS_PAY:
                registry = new SDbCommissionPayment();
                break;
            case SModConsts.MKT_COMMS_PAY_ETY:
                registry = new SDbCommissionPaymentEntry();
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
            case SModConsts.MKT_COMMS_PAY:
                view = new SViewCommissionPayment(miClient, subtype, "Pagos comisiones " + (subtype == SModConsts.VIEW_SC_SUM ? "" : " (detalle)"), params);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiReport getReport(final int type, final int subtype, final SGuiParams params) {
        SGuiReport guiReport = null;

        switch (type) {
            case SModConsts.MKTR_COMMS_PAY:
                guiReport = new SGuiReport("reps/trn_comms_pay.jasper", "Pago comisiones");
                break;
            default:
        }

        return guiReport;
    }
}
