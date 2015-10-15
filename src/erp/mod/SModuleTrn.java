/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod;

import erp.data.SDataConstantsSys;
import erp.mod.trn.db.SDbDps;
import erp.mod.trn.db.SDbDpsEntry;
import erp.mod.trn.db.SDbDpsEntryPrice;
import erp.mod.trn.db.SDbInventoryValuation;
import erp.mod.trn.db.SDbItemRequiredDpsConfig;
import erp.mod.trn.db.SDbMmsConfig;
import erp.mod.trn.form.SFormInventoryValuation;
import erp.mod.trn.form.SFormItemRequiredDpsConfig;
import erp.mod.trn.form.SFormMmsConfig;
import erp.mod.trn.view.SViewDpsEntryContractPrice;
import erp.mod.trn.view.SViewDpsSendWebService;
import erp.mod.trn.view.SViewInventoryCost;
import erp.mod.trn.view.SViewInventoryCostByDiogType;
import erp.mod.trn.view.SViewInventoryValuation;
import erp.mod.trn.view.SViewItemRequiredDpsConfig;
import erp.mod.trn.view.SViewMmsConfig;
import javax.swing.JMenu;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistrySysFly;
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
 * @author Sergio Flores
 */
public class SModuleTrn extends SGuiModule {

    private SFormItemRequiredDpsConfig moFormItemRequiredDpsConfig;
    private SFormInventoryValuation moFormInventoryValuation;
    private SFormMmsConfig moFormMmsConfiguration;

    public SModuleTrn(SGuiClient client, int subtype) {
        super(client, SModConsts.MOD_TRN_N, subtype);
        moModuleIcon = miClient.getImageIcon(mnModuleSubtype);
    }

    @Override
    public JMenu[] getMenus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry getRegistry(int type) {
        SDbRegistry registry = null;

        switch (type) {
            case SModConsts.TRNU_TP_DPS_SRC_ITEM:
                registry = new SDbItemRequiredDpsConfig();
                break;
            case SModConsts.TRNU_TP_DPS:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_ct_dps = " + pk[0] + " AND id_cl_dps = " + pk[1] + " AND id_tp_dps = " + pk[2] + " "; }
                };
                break;
            case SModConsts.TRN_DPS:
                registry = new SDbDps();
                break;
            case SModConsts.TRN_DPS_ETY:
                registry = new SDbDpsEntry();
                break;
            case SModConsts.TRN_DPS_ETY_PRC:
                registry = new SDbDpsEntryPrice();
                break;
            case SModConsts.TRN_INV_VAL:
                registry = new SDbInventoryValuation();
                break;
            case SModConsts.TRN_MMS_CFG:
                registry = new SDbMmsConfig();
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public SGuiCatalogueSettings getCatalogueSettings(int type, int subtype, SGuiParams params) {
        String sql = "";
        SGuiCatalogueSettings settings = null;

        switch (type) {
            case SModConsts.TRNU_TP_DPS:
                settings = new SGuiCatalogueSettings("Tipo de documento", 3);
                sql = "SELECT id_ct_dps AS " + SDbConsts.FIELD_ID + "1, id_cl_dps AS " + SDbConsts.FIELD_ID + "2, id_tp_dps AS " + SDbConsts.FIELD_ID + "3, "
                        + "tp_dps AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = FALSE "
                        + "ORDER BY tp_dps, id_ct_dps, id_cl_dps, id_tp_dps ";
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
    public SGridPaneView getView(int type, int subtype, SGuiParams params) {
        SGridPaneView view = null;

        switch (type) {
            case SModConsts.TRNU_TP_DPS_SRC_ITEM:
                view = new SViewItemRequiredDpsConfig(miClient, "Configuración ítems obligatorios documentos origen");
                break;
            case SModConsts.TRN_DPS_ETY_PRC:
                switch (subtype) {
                    case SModConsts.MOD_TRN_SAL_N:
                        view = new SViewDpsEntryContractPrice(miClient, subtype, "VTA - Entregas mensuales contratos" + (params != null && params.getType() == SModConsts.VIEW_ST_PEND ? " x procesar" : " procesados"), params);
                         break;
                    case SModConsts.MOD_TRN_PUR_N:
                        view = new SViewDpsEntryContractPrice(miClient, subtype, "CPA - Entregas mensuales contratos" + (params != null && params.getType() == SModConsts.VIEW_ST_PEND ? " x procesar" : " procesados"), params);
                        break;
                    default:
                }
                break;
            case SModConsts.TRN_CFD:
                switch (subtype) {
                    case SModSysConsts.TRNS_ST_XML_DVY_PENDING:
                        view = new SViewDpsSendWebService(miClient, subtype, "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(params.getType()) + " x enviar x WS", params);
                        break;
                    case SModSysConsts.TRNS_ST_XML_DVY_REJECT:
                        view = new SViewDpsSendWebService(miClient, subtype, "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(params.getType()) + " rechazadas x WS", params);
                        break;
                    case SModSysConsts.TRNS_ST_XML_DVY_APPROVED:
                        view = new SViewDpsSendWebService(miClient, subtype, "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(params.getType()) + " aceptadas x WS", params);
                        break;
                    default:
                }
                break;
            case SModConsts.TRN_INV_VAL:
                view = new SViewInventoryValuation(miClient, subtype, "Valuación inv.", params);
                break;
            case SModConsts.TRN_STK_COST:
                view = new SViewInventoryCost(miClient, subtype, "Costo inv. " + (subtype != SModConsts.TRNX_STK_WAH ? "por ítem" : "por almacén"), params);
                break;
            case SModConsts.TRNX_STK_DIOG_TP:
                view = new SViewInventoryCostByDiogType(miClient, subtype, "Costo inv. por tipo mov.", params);
                break;
            case SModConsts.TRN_MMS_CFG:
                view = new SViewMmsConfig(miClient, "Configuración de ítems para envío por email");
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return view;
    }

    @Override
    public SGuiOptionPicker getOptionPicker(int type, int subtype, SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiForm getForm(int type, int subtype, SGuiParams params) {
         SGuiForm form = null;

        switch (type) {
            case SModConsts.TRNU_TP_DPS_SRC_ITEM:
                if(moFormItemRequiredDpsConfig == null) moFormItemRequiredDpsConfig = new SFormItemRequiredDpsConfig(miClient, "Configuración de ítems obligatorios con documentos origen");
                form = moFormItemRequiredDpsConfig;
                break;
            case SModConsts.TRN_INV_VAL:
                if (moFormInventoryValuation == null) moFormInventoryValuation = new SFormInventoryValuation(miClient, SModConsts.TRN_INV_VAL, "Valuación de inventarios");
                form = moFormInventoryValuation;
                break;
            case SModConsts.TRN_MMS_CFG:
                if(moFormMmsConfiguration == null) moFormMmsConfiguration = new SFormMmsConfig(miClient, "Configuración de ítems para envío por email");
                form = moFormMmsConfiguration;
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return form;
    }

    @Override
    public SGuiReport getReport(int type, int subtype, SGuiParams params) {
        SGuiReport guiReport = null;

        switch (type) {
            case SModConsts.TRNR_CON_STA:
                guiReport = new SGuiReport("reps/trn_con_sta.jasper", "Reporte de estatus de contratos");
                break;
            case SModConsts.TRNR_CON_STA_BP:
                guiReport = new SGuiReport("reps/trn_con_sta_bp.jasper", "Reporte de estatus de contratos");
                break;
            case SModConsts.TRNR_MON_DVY_PROG:
                guiReport = new SGuiReport("reps/trn_mon_dvy_prog.jasper", "Reporte de estatus de contratos");
                break;
            case SModConsts.TRNR_DPS_CON:
                guiReport = new SGuiReport("reps/trn_dps_con.jasper", "Reporte de estatus de contratos");
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return guiReport;
    }
}
