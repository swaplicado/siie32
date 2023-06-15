/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod;

import erp.data.SDataConstantsSys;
import erp.mod.trn.db.SDbDelivery;
import erp.mod.trn.db.SDbDeliveryEntry;
import erp.mod.trn.db.SDbDps;
import erp.mod.trn.db.SDbDpsEntry;
import erp.mod.trn.db.SDbDpsEntryPrice;
import erp.mod.trn.db.SDbFunctionalAreaBudget;
import erp.mod.trn.db.SDbFunctionalAreaBudgets;
import erp.mod.trn.db.SDbIdentifiedCostCalculation;
import erp.mod.trn.db.SDbIdentifiedCostLot;
import erp.mod.trn.db.SDbInventoryMfgCost;
import erp.mod.trn.db.SDbInventoryValuation;
import erp.mod.trn.db.SDbItemCost;
import erp.mod.trn.db.SDbItemRequiredDpsConfig;
import erp.mod.trn.db.SDbMaintArea;
import erp.mod.trn.db.SDbMaintConfig;
import erp.mod.trn.db.SDbMaintDiogSignature;
import erp.mod.trn.db.SDbMaintUser;
import erp.mod.trn.db.SDbMaintUserSupervisor;
import erp.mod.trn.db.SDbMaterialRequest;
import erp.mod.trn.db.SDbMmsConfig;
import erp.mod.trn.form.SFormDelivery;
import erp.mod.trn.form.SFormFunctionalAreaBudgets;
import erp.mod.trn.form.SFormIdentifiedCostCalculation;
import erp.mod.trn.form.SFormInventoryValuation;
import erp.mod.trn.form.SFormItemCost;
import erp.mod.trn.form.SFormItemRequiredDpsConfig;
import erp.mod.trn.form.SFormMaintArea;
import erp.mod.trn.form.SFormMaintUser;
import erp.mod.trn.form.SFormMaintUserSupervisor;
import erp.mod.trn.form.SFormMaterialRequest;
import erp.mod.trn.form.SFormMmsConfig;
import erp.mod.trn.view.SViewAccountsPending;
import erp.mod.trn.view.SViewCurrencyBalance;
import erp.mod.trn.view.SViewDelivery;
import erp.mod.trn.view.SViewDeliveryQuery;
import erp.mod.trn.view.SViewDpsEntryContractPrice;
import erp.mod.trn.view.SViewDpsSendWebService;
import erp.mod.trn.view.SViewFunctionalAreaBudgets;
import erp.mod.trn.view.SViewFunctionalAreaExpenses;
import erp.mod.trn.view.SViewIdentifiedCostCalculation;
import erp.mod.trn.view.SViewInventoryCost;
import erp.mod.trn.view.SViewInventoryMfgCost;
import erp.mod.trn.view.SViewInventoryValuation;
import erp.mod.trn.view.SViewItemCost;
import erp.mod.trn.view.SViewItemRequiredDpsConfig;
import erp.mod.trn.view.SViewMaintArea;
import erp.mod.trn.view.SViewMaintUser;
import erp.mod.trn.view.SViewMaintUserSupervisor;
import erp.mod.trn.view.SViewMaterialRequest;
import erp.mod.trn.view.SViewMmsConfig;
import erp.mod.trn.view.SViewOrderLimitMonth;
import erp.mod.trn.view.SViewValCost;
import javax.swing.JMenu;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistrySysFly;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiCatalogueSettings;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiForm;
import sa.lib.gui.SGuiModule;
import sa.lib.gui.SGuiOptionPicker;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiReport;

/**
 *
 * @author Sergio Flores, Uriel Castañeda, Cesar Orozco, Gil De Jesús, Sergio Flores, Isabel Servín
 */
public class SModuleTrn extends SGuiModule {

    private SFormItemRequiredDpsConfig moFormItemRequiredDpsConfig;
    private SFormInventoryValuation moFormInventoryValuationPrcCalc;
    private SFormInventoryValuation moFormInventoryValuationUpdCost;
    private SFormIdentifiedCostCalculation moFormIdentifiedCostCalculation;
    private SFormMmsConfig moFormMmsConfiguration;
    private SFormDelivery moFormDelivery;
    private SFormMaintArea moFormMaintArea;
    private SFormItemCost moFormItemCost;
    private SFormMaintUser moFormMaintUserEmployee;
    private SFormMaintUser moFormMaintUserContractor;
    private SFormMaintUser moFormMaintUserToolMaintProv;
    private SFormMaintUserSupervisor moFormMaintUserSupv;
    private SFormFunctionalAreaBudgets moFormFunctionalAreaBudgets;
    private SFormMaterialRequest moFormMaterialRequest;

    public SModuleTrn(SGuiClient client, int subtype) {
        super(client, SModConsts.MOD_TRN_N, subtype);
        moModuleIcon = miClient.getImageIcon(mnModuleSubtype);
    }

    @Override
    public JMenu[] getMenus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry getRegistry(final int type, final SGuiParams params) {
        int[] key = null;
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
            case SModConsts.TRNS_TP_MAINT_MOV:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_maint_mov = " + pk[0] + " "; }
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
            case SModConsts.TRNX_INV_VAL_COST_UPD:
                registry = new SDbInventoryValuation();
                break;
            case SModConsts.TRN_INV_MFG_CST:
                registry = new SDbInventoryMfgCost();
                break;
            case SModConsts.TRN_ITEM_COST:
                registry = new SDbItemCost();
                break;
            case SModConsts.TRN_MMS_CFG:
                registry = new SDbMmsConfig();
                break;
            case SModConsts.TRN_DVY:
                registry = new SDbDelivery();
                if (params != null) {
                    key = (int[]) params.getParamsMap().get(SModConsts.TRN_DPS);
                    if (key != null) {
                        ((SDbDelivery) registry).setFkDpsYearId(key[0]);
                        ((SDbDelivery) registry).setFkDpsDocId(key[1]);
                    }
                }
                break;
            case SModConsts.TRN_DVY_ETY:
                registry = new SDbDeliveryEntry();
                break;
            case SModConsts.TRN_MAINT_CFG:
                registry = new SDbMaintConfig();
                break;
            case SModConsts.TRN_MAINT_AREA:
                registry = new SDbMaintArea();
                break;
            case SModConsts.TRN_MAINT_USER:
                registry = new SDbMaintUser();
                break;
            case SModConsts.TRN_MAINT_USER_SUPV:
                registry = new SDbMaintUserSupervisor();
                break;
            case SModConsts.TRN_MAINT_DIOG_SIG:
                registry = new SDbMaintDiogSignature();
                break;
            case SModConsts.TRN_FUNC_BUDGET:
                registry = new SDbFunctionalAreaBudget();
                break;
            case SModConsts.TRNX_FUNC_BUDGETS:
                registry = new SDbFunctionalAreaBudgets();
                break;
            case SModConsts.TRN_COST_IDENT_CALC:
                registry = new SDbIdentifiedCostCalculation();
                break;
            case SModConsts.TRN_COST_IDENT_LOT:
                registry = new SDbIdentifiedCostLot();
                break;
            case SModConsts.TRN_MAT_REQ:
                registry = new SDbMaterialRequest();
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
            case SModConsts.TRNU_TP_DPS:
                settings = new SGuiCatalogueSettings("Tipo de documento", 3);
                sql = "SELECT id_ct_dps AS " + SDbConsts.FIELD_ID + "1, id_cl_dps AS " + SDbConsts.FIELD_ID + "2, id_tp_dps AS " + SDbConsts.FIELD_ID + "3, "
                        + "tp_dps AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY tp_dps, id_ct_dps, id_cl_dps, id_tp_dps ";
                break;
            case SModConsts.TRNU_TP_DPS_ANN:
                settings = new SGuiCatalogueSettings("Cancelación", 1);
                sql = "SELECT id_tp_dps_ann AS " + SDbConsts.FIELD_ID + "1, tp_dps_ann AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY id_tp_dps_ann ";
                break;
            case SModConsts.TRN_MAINT_AREA:
                settings = new SGuiCatalogueSettings("Área de mantenimiento", 1);
                sql = "SELECT id_maint_area AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY name, id_maint_area ";
                break;
            case SModConsts.TRN_MAINT_USER:
                switch (subtype) {
                    case SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE:
                        sqlWhere = "mu.b_employee ";
                        name = SModSysConsts.TXT_TRNX_TP_MAINT_USER_EMPLOYEE;
                        break;
                    case SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR:
                        sqlWhere = "mu.b_contractor ";
                        name = SModSysConsts.TXT_TRNX_TP_MAINT_USER_CONTRACTOR;
                        break;
                    case SModSysConsts.TRNX_TP_MAINT_USER_TOOLS_MAINT_PROV:
                        sqlWhere = "mu.b_tool_maint_prov ";
                        name = SModSysConsts.TXT_TRNX_TP_MAINT_USER_TOOLS_MAINT_PROV;
                        break;
                    default:
                }
                settings = new SGuiCatalogueSettings(name, 1);
                sql = "SELECT mu.id_maint_user AS " + SDbConsts.FIELD_ID + "1, b.bp AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " AS mu "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = mu.id_maint_user "
                        + "WHERE NOT mu.b_del " + (sqlWhere.isEmpty() ? "" : "AND " + sqlWhere)
                        + "ORDER BY b.bp, mu.id_maint_user ";
                break;
            case SModConsts.TRN_MAINT_USER_SUPV:
                settings = new SGuiCatalogueSettings("Residente de contratista", 1);
                sql = "SELECT id_maint_user_supv AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del AND fk_maint_user_n = " + params.getKey()[0] + " "
                        + "ORDER BY name, id_maint_user_supv ";
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
        String title = "";
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
                view = new SViewInventoryValuation(miClient, "Valuación inventarios");
                break;
            case SModConsts.TRN_INV_MFG_CST:
                view = new SViewInventoryMfgCost(miClient, "Costos producción");
                break;
            case SModConsts.TRNX_STK_COST:
                view = new SViewInventoryCost(miClient, subtype, "Valor inventarios " + (subtype == SModConsts.CFGU_COB_ENT ? "x almacén" : "x ítem"));
                break;
            case SModConsts.TRNX_INV_VAL_COST_QRY:
                view = new SViewValCost(miClient, subtype, "Valor valuación vs. valor teórico");
                break;
            case SModConsts.TRNX_ORD_LIM_MAX:
                view = new SViewOrderLimitMonth(miClient, "Límite máx. mensual " + (subtype == SModConsts.USRU_USR ? "x usuario" : "x área funcional"), subtype, params);
                break;
            case SModConsts.TRN_MMS_CFG:
                view = new SViewMmsConfig(miClient, "Configuración de ítems para envío por correo-e");
                break;
            case SModConsts.TRN_DVY:
                if (params == null) {
                    title = "Entregas ventas";
                    title += subtype == SUtilConsts.QRY_SUM ? "" : " (detalle)";
                    view = new SViewDelivery(miClient, subtype, title);
                }
                else {
                    title = "Ventas ";
                    title += subtype == SUtilConsts.PROC ? "entregadas" : "x entregar";
                    title += params.getType() == SUtilConsts.QRY_SUM ? "" : " (detalle)";
                    view = new SViewDeliveryQuery(miClient, subtype, title, params);
                }
                break;
            case SModConsts.TRN_MAINT_AREA:
                view = new SViewMaintArea(miClient, "Áreas mantenimiento");
                break;
            case SModConsts.TRN_MAINT_USER:
                switch(subtype){
                    case SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE:
                        view = new SViewMaintUser(miClient, subtype, "Mantto. - Empleados");
                        break;
                    case SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR:
                        view = new SViewMaintUser(miClient, subtype, "Mantto. - Contratistas");
                        break;
                    case SModSysConsts.TRNX_TP_MAINT_USER_TOOLS_MAINT_PROV:
                        view = new SViewMaintUser(miClient, subtype, "Mantto. - Proveedores mantto. herramientas");
                        break;
                    default:
                }
                break;
            case SModConsts.TRN_MAINT_USER_SUPV:
                view = new SViewMaintUserSupervisor(miClient, "Mantto. - Residentes contratistas");
                break;
            case SModConsts.TRN_MAT_REQ:
                view = new SViewMaterialRequest(miClient, "Requisiciones");
                break;
            case SModConsts.TRN_ITEM_COST:
                view = new SViewItemCost(miClient, "Costos de ítems");
                break;
            case SModConsts.TRNX_BP_BAL_CUR:
                title = (subtype == SDataConstantsSys.TRNS_CT_DPS_SAL ? "CXC" : "CXP") + " x moneda";
                if (params == null) {
                    view = new SViewCurrencyBalance(miClient, subtype, title, new SGuiParams(SDataConstantsSys.UNDEFINED));
                }
                else {
                    title += params.getType() == SGuiConsts.PARAM_BPR ? ("-" + (subtype == SDataConstantsSys.TRNS_CT_DPS_SAL ? "cliente" : "proveedor")) : "";
                    view = new SViewCurrencyBalance(miClient, subtype, title, params);
                }
                break;             
            case SModConsts.TRNX_ACC_PEND:
                view = new SViewAccountsPending(miClient, subtype, (subtype == SModSysConsts.BPSS_CT_BP_CUS ? "CXC" : "CXP"));
                break;
            case SModConsts.TRNX_FUNC_BUDGETS:
                view = new SViewFunctionalAreaBudgets(miClient, "Presupuestos mensuales gastos");
                break;
            case SModConsts.TRNX_FUNC_EXPENSES:
                view = new SViewFunctionalAreaExpenses(miClient, subtype, "Control presupuestos mensuales gastos");
                break;
            case SModConsts.TRN_COST_IDENT_CALC:
                view = new SViewIdentifiedCostCalculation(miClient, "Costos identificados ventas");
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
            case SModConsts.TRNU_TP_DPS_SRC_ITEM:
                if(moFormItemRequiredDpsConfig == null) moFormItemRequiredDpsConfig = new SFormItemRequiredDpsConfig(miClient, "Configuración de ítems obligatorios con documentos origen");
                form = moFormItemRequiredDpsConfig;
                break;
            case SModConsts.TRN_INV_VAL:
                if (moFormInventoryValuationPrcCalc == null) moFormInventoryValuationPrcCalc = new SFormInventoryValuation(miClient, SModConsts.TRNX_INV_VAL_PRC_CALC, "Valuación de inventarios");
                form = moFormInventoryValuationPrcCalc;
                break;
            case SModConsts.TRNX_INV_VAL_COST_UPD:
                if (moFormInventoryValuationUpdCost == null) moFormInventoryValuationUpdCost = new SFormInventoryValuation(miClient, SModConsts.TRNX_INV_VAL_UPD_COST, "Actualización de costos de inventarios");
                form = moFormInventoryValuationUpdCost;
                break;
            case SModConsts.TRN_COST_IDENT_CALC:
                if (moFormIdentifiedCostCalculation == null) moFormIdentifiedCostCalculation = new SFormIdentifiedCostCalculation(miClient, "Costos identificados de ventas");
                form = moFormIdentifiedCostCalculation;
                break;
            case SModConsts.TRN_MMS_CFG:
                if (moFormMmsConfiguration == null) moFormMmsConfiguration = new SFormMmsConfig(miClient, "Configuración de ítems para envío por correo-e");
                form = moFormMmsConfiguration;
                break;
            case SModConsts.TRN_DVY:
                if (moFormDelivery == null) moFormDelivery = new SFormDelivery(miClient, "Entrega");
                form = moFormDelivery;
                break;
            case SModConsts.TRN_MAINT_AREA:
                if (moFormMaintArea == null) moFormMaintArea = new SFormMaintArea(miClient, "Área de mantenimiento");
                form = moFormMaintArea;
                break;
            case SModConsts.TRN_MAINT_USER:
                switch (subtype){
                    case SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE:
                        if (moFormMaintUserEmployee == null) moFormMaintUserEmployee = new SFormMaintUser(miClient, SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE, SModSysConsts.TXT_TRNX_TP_MAINT_USER_EMPLOYEE);
                        form = moFormMaintUserEmployee;
                        break;
                    case SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR:
                        if (moFormMaintUserContractor == null) moFormMaintUserContractor = new SFormMaintUser(miClient, SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR, SModSysConsts.TXT_TRNX_TP_MAINT_USER_CONTRACTOR);
                        form = moFormMaintUserContractor;
                        break;
                    case SModSysConsts.TRNX_TP_MAINT_USER_TOOLS_MAINT_PROV:
                        if (moFormMaintUserToolMaintProv == null) moFormMaintUserToolMaintProv = new SFormMaintUser(miClient, SModSysConsts.TRNX_TP_MAINT_USER_TOOLS_MAINT_PROV, SModSysConsts.TXT_TRNX_TP_MAINT_USER_TOOLS_MAINT_PROV);
                        form = moFormMaintUserToolMaintProv;
                        break;
                    default:
                }
                break;
            case SModConsts.TRN_MAINT_USER_SUPV:
                if (moFormMaintUserSupv == null) moFormMaintUserSupv = new SFormMaintUserSupervisor(miClient, "Residente de contratista");
                form = moFormMaintUserSupv;
                break;
            case SModConsts.TRN_ITEM_COST:
                if (moFormItemCost == null) moFormItemCost = new SFormItemCost(miClient, "Costos de ítems");
                form = moFormItemCost;
                break;
            case SModConsts.TRNX_FUNC_BUDGETS:
                if (moFormFunctionalAreaBudgets == null) moFormFunctionalAreaBudgets = new SFormFunctionalAreaBudgets(miClient, "Presupuestos mensuales de gastos");
                form = moFormFunctionalAreaBudgets;
                break;
            case SModConsts.TRN_MAT_REQ:
                if (moFormMaterialRequest == null) moFormMaterialRequest = new SFormMaterialRequest(miClient, "Requisición");
                form = moFormMaterialRequest;
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return form;
    }

    @Override
    public SGuiReport getReport(final int type, final int subtype, final SGuiParams params) {
        SGuiReport guiReport = null;

        switch (type) {
            case SModConsts.TRN_DVY:
                guiReport = new SGuiReport("reps/trn_dvy.jasper", "Entrega de ventas");
                break;
            case SModConsts.TRN_ITEM_COST:
                guiReport = new SGuiReport("reps/trn_cont_marg.jasper", "Reporte margen de contribución");
                break;
            case SModConsts.TRNR_CON_STA:
                guiReport = new SGuiReport("reps/trn_con_sta.jasper", "Reporte de estatus de contratos");
                break;
            case SModConsts.TRNR_CON_STA_BP:
                guiReport = new SGuiReport("reps/trn_con_sta_bp.jasper", "Reporte de estatus de contratos");
                break;
            case SModConsts.TRNR_CON_MON_DVY_PROG:
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
