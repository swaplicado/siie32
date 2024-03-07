/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod;

import erp.data.SDataConstantsSys;
import erp.mod.trn.db.SDbConfCostCenterGroupVsItem;
import erp.mod.trn.db.SDbConfCostCenterGroupVsUser;
import erp.mod.trn.db.SDbConfEmployeeVsEntity;
import erp.mod.trn.db.SDbConfMatConsSubentityCCVsCostCenterGroup;
import erp.mod.trn.db.SDbConfMatConsSubentityVsCostCenter;
import erp.mod.trn.db.SDbConfUserVsEntity;
import erp.mod.trn.db.SDbConfWarehouseVsConsEntity;
import erp.mod.trn.db.SDbConfWarehouseVsProvEntity;
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
import erp.mod.trn.db.SDbMaterialConsumptionEntity;
import erp.mod.trn.db.SDbMaterialConsumptionEntityBudget;
import erp.mod.trn.db.SDbMaterialConsumptionSubentity;
import erp.mod.trn.db.SDbMaterialCostCenterGroup;
import erp.mod.trn.db.SDbMaterialProvisionEntity;
import erp.mod.trn.db.SDbMaterialRequest;
import erp.mod.trn.db.SDbMaterialRequestCostCenter;
import erp.mod.trn.db.SDbMmsConfig;
import erp.mod.trn.db.SDbStockValuation;
import erp.mod.trn.form.SFormConfEmployeeVsEntity;
import erp.mod.trn.form.SFormConfMatConsSubentityCCVsCostCenterGroup;
import erp.mod.trn.form.SFormConfMatConsSubentityVsCostCenter;
import erp.mod.trn.form.SFormConfMatCostCenterGroupVsItem;
import erp.mod.trn.form.SFormConfMatCostCenterGroupVsUser;
import erp.mod.trn.form.SFormConfUserVsEntity;
import erp.mod.trn.form.SFormConfWarehouseVsConsEntity;
import erp.mod.trn.form.SFormConfWarehouseVsProvEntity;
import erp.mod.trn.form.SFormDelivery;
import erp.mod.trn.form.SFormFunctionalAreaBudgets;
import erp.mod.trn.form.SFormIdentifiedCostCalculation;
import erp.mod.trn.form.SFormInventoryValuation;
import erp.mod.trn.form.SFormItemCost;
import erp.mod.trn.form.SFormItemRequiredDpsConfig;
import erp.mod.trn.form.SFormMaintArea;
import erp.mod.trn.form.SFormMaintUser;
import erp.mod.trn.form.SFormMaintUserSupervisor;
import erp.mod.trn.form.SFormMaterialConsumptionEntity;
import erp.mod.trn.form.SFormMaterialConsumptionEntityBudget;
import erp.mod.trn.form.SFormMaterialConsumptionSubentity;
import erp.mod.trn.form.SFormMaterialCostCenterGroup;
import erp.mod.trn.form.SFormMaterialProvisionEntity;
import erp.mod.trn.form.SFormMaterialRequest;
import erp.mod.trn.form.SFormMaterialRequestCostCenter;
import erp.mod.trn.form.SFormMmsConfig;
import erp.mod.trn.form.SFormStockValuation;
import erp.mod.trn.view.SViewAccountsPending;
import erp.mod.trn.view.SViewConfEmployeeVsEntity;
import erp.mod.trn.view.SViewConfEmployeeVsEntityDetail;
import erp.mod.trn.view.SViewConfMatConsSubentityCCVsCostCenterGroup;
import erp.mod.trn.view.SViewConfMatConsSubentityCCVsCostCenterGroupDetail;
import erp.mod.trn.view.SViewConfMatConsSubentityVsCostCenter;
import erp.mod.trn.view.SViewConfMatConsSubentityVsCostCenterDetail;
import erp.mod.trn.view.SViewConfMatCostCenterGroupItem;
import erp.mod.trn.view.SViewConfMatCostCenterGroupItemDetail;
import erp.mod.trn.view.SViewConfMatCostCenterGroupUser;
import erp.mod.trn.view.SViewConfMatCostCenterGroupUserDetail;
import erp.mod.trn.view.SViewConfUserVsEntity;
import erp.mod.trn.view.SViewConfUserVsEntityDetail;
import erp.mod.trn.view.SViewConfWarehouseVsConsEntity;
import erp.mod.trn.view.SViewConfWarehouseVsConsEntityDetail;
import erp.mod.trn.view.SViewConfWarehouseVsProvEntity;
import erp.mod.trn.view.SViewConfWarehouseVsProvEntityDetail;
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
import erp.mod.trn.view.SViewMaterialConsumptionEntity;
import erp.mod.trn.view.SViewMaterialConsumptionEntityBudget;
import erp.mod.trn.view.SViewMaterialConsumptionSubentity;
import erp.mod.trn.view.SViewMaterialCostCenterGroup;
import erp.mod.trn.view.SViewMaterialProvisionEntity;
import erp.mod.trn.view.SViewMaterialRequesPendingSupply;
import erp.mod.trn.view.SViewMaterialRequest;
import erp.mod.trn.view.SViewMaterialRequestPending;
import erp.mod.trn.view.SViewMaterialRequestPendingEstimation;
import erp.mod.trn.view.SViewMmsConfig;
import erp.mod.trn.view.SViewOrderLimitMonth;
import erp.mod.trn.view.SViewReportBudgetSummary;
import erp.mod.trn.view.SViewReportMaterialConsuption;
import erp.mod.trn.view.SViewReportMaterialConsuptionCC;
import erp.mod.trn.view.SViewStockValuation;
import erp.mod.trn.view.SViewValCost;
import erp.mod.trn.view.SViewWarehouseConsumptionDetail;
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
    private SFormMaterialCostCenterGroup moFormMaterialCostCenterGroup;
    private SFormMaterialConsumptionEntity moFormMaterialConsumptionEntity;
    private SFormMaterialConsumptionSubentity moFormMaterialConsumptionSubentity;
    private SFormMaterialProvisionEntity moFormMaterialProvisionEntity;
    private SFormConfUserVsEntity moFormUserVsEntity;
    private SFormConfEmployeeVsEntity moFormEmployeeVsEntity;
    private SFormConfWarehouseVsProvEntity moFormWarehouseVsProvEntity;
    private SFormConfWarehouseVsConsEntity moFormWarehouseVsConsEntity;
    private SFormConfMatCostCenterGroupVsItem moFormConfMatCostCenterGroupVsItem;
    private SFormConfMatCostCenterGroupVsUser moFormConfMatCostCenterGroupVsUser;
    private SFormConfMatConsSubentityVsCostCenter moFormConsSubentityVsCostCenter;
    private SFormConfMatConsSubentityCCVsCostCenterGroup moFormConsSubentityCCVsCostCenterGroup;
    private SFormInventoryValuation moFormInventoryValuationPrcCalc;
    private SFormInventoryValuation moFormInventoryValuationUpdCost;
    private SFormIdentifiedCostCalculation moFormIdentifiedCostCalculation;
    private SFormMmsConfig moFormMmsConfiguration;
    private SFormDelivery moFormDelivery;
    private SFormMaintArea moFormMaintArea;
    private SFormItemCost moFormItemCost;
    private SFormMaterialRequest moFormMaterialReq;
    private SFormMaterialRequest moFormMaterialReqSup;
    private SFormMaterialRequestCostCenter moFormMaterialRequestCostCenter;
    private SFormMaintUser moFormMaintUserEmployee;
    private SFormMaintUser moFormMaintUserContractor;
    private SFormMaintUser moFormMaintUserToolMaintProv;
    private SFormMaintUserSupervisor moFormMaintUserSupv;
    private SFormFunctionalAreaBudgets moFormFunctionalAreaBudgets;
    private SFormMaterialConsumptionEntityBudget moFormMaterialConsumptionEntityBudget;
    private SFormStockValuation moFormStockValuation;

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
            case SModConsts.TRN_COST_IDENT_CALC:
                registry = new SDbIdentifiedCostCalculation();
                break;
            case SModConsts.TRN_COST_IDENT_LOT:
                registry = new SDbIdentifiedCostLot();
                break;
            case SModConsts.TRN_MAT_CC_GRP:
                registry = new SDbMaterialCostCenterGroup();
                break;
            case SModConsts.TRN_MAT_REQ:
            case SModConsts.TRNX_MAT_REQ_PEND_SUP:
            case SModConsts.TRNX_MAT_REQ_PEND_PUR:
            case SModConsts.TRNX_MAT_REQ_STK_SUP:
            case SModConsts.TRNX_MAT_REQ_EST:
                registry = new SDbMaterialRequest();
                break;
            case SModConsts.TRN_MAT_REQ_CC:
                registry = new SDbMaterialRequestCostCenter();
                break;
            case SModConsts.TRN_MAT_CONS_ENT:
                registry = new SDbMaterialConsumptionEntity();
                break;
            case SModConsts.TRN_MAT_CONS_SUBENT:
                registry = new SDbMaterialConsumptionSubentity();
                break;
            case SModConsts.TRN_MAT_CONS_ENT_BUDGET:
                registry = new SDbMaterialConsumptionEntityBudget();
                break;
            case SModConsts.TRN_MAT_PROV_ENT:
                registry = new SDbMaterialProvisionEntity();
                break;
            case SModConsts.TRN_STK_VAL:
                registry = new SDbStockValuation();
                break;
            case SModConsts.TRNX_FUNC_BUDGETS:
                registry = new SDbFunctionalAreaBudgets();
                break;
            case SModConsts.TRNX_CONF_USR_VS_ENT:
                registry = new SDbConfUserVsEntity();
                break;
            case SModConsts.TRNX_CONF_EMP_VS_ENT:
                registry = new SDbConfEmployeeVsEntity();
                break;
            case SModConsts.TRNX_CONF_WHS_VS_PRV_ENT:
                registry = new SDbConfWarehouseVsProvEntity();
                break;
            case SModConsts.TRNX_CONF_WHS_VS_CON_ENT:
                registry = new SDbConfWarehouseVsConsEntity();
                break;
            case SModConsts.TRNX_CONF_SUBENT_VS_CC:
                registry = new SDbConfMatConsSubentityVsCostCenter();
                break;
            case SModConsts.TRNX_CONF_SUBENT_VS_CC_GRP:
                registry = new SDbConfMatConsSubentityCCVsCostCenterGroup();
                break;
            case SModConsts.TRNX_CONF_CC_GRP_VS_ITM:
                registry = new SDbConfCostCenterGroupVsItem();
                break;
            case SModConsts.TRNX_CONF_CC_GRP_VS_USR:
                registry = new SDbConfCostCenterGroupVsUser();
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
            case SModConsts.TRNS_ST_MAT_REQ:
                settings = new SGuiCatalogueSettings("Estatus de requisición", 1);
                sql = "SELECT id_st_mat_req AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY id_st_mat_req ";
                break;
            case SModConsts.TRNU_DPS_NAT:
                settings = new SGuiCatalogueSettings("Naturaleza de documento", 1);
                sql = "SELECT id_dps_nat AS " + SDbConsts.FIELD_ID + "1, dps_nat AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY id_dps_nat ";
                break;
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
            case SModConsts.TRNU_MAT_REQ_PTY:
                settings = new SGuiCatalogueSettings("Prioridad", 1);
                sql = "SELECT id_mat_req_pty AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY id_mat_req_pty";
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
            case SModConsts.TRN_MAT_PROV_ENT:
                settings = new SGuiCatalogueSettings("Centro de suministro", 1);
                switch (subtype) {
                    case SModConsts.USRU_USR:
                        sql = "SELECT p.id_mat_prov_ent AS " + SDbConsts.FIELD_ID + "1, CONCAT(p.code, ' - ', p.name) AS " + SDbConsts.FIELD_ITEM + " "
                                + "FROM " + SModConsts.TablesMap.get(type) + " AS p "
                                + "INNER JOIN trn_mat_prov_ent_usr AS pu ON p.id_mat_prov_ent = pu.id_mat_prov_ent "
                                + "WHERE NOT b_del AND pu.id_usr = " + params.getParamsMap().get(SModConsts.USRU_USR) + " "
                                + "ORDER BY pu.b_default DESC, p.name";
                        break;
                    default:
                        sql = "SELECT id_mat_prov_ent AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " "
                                + "FROM " + SModConsts.TablesMap.get(type) + " "
                                + "WHERE NOT b_del "
                                + "ORDER BY name";
                        break;
                }
                break;
            case SModConsts.TRN_MAT_CONS_ENT:
                settings = new SGuiCatalogueSettings("Centro de consumo", 1);
                switch (subtype) {
                    case SModConsts.USRU_USR:
                        sql = "SELECT a.id_mat_cons_ent AS " + SDbConsts.FIELD_ID + "1, CONCAT(a.code, ' - ', a.name) AS " + SDbConsts.FIELD_ITEM + " "
                                + "FROM ("
                                + "SELECT DISTINCT c.* FROM " + SModConsts.TablesMap.get(type) + " AS c "
                                + "INNER JOIN trn_mat_cons_ent_usr AS cu ON c.id_mat_cons_ent = cu.id_mat_cons_ent "
                                + "WHERE cu.id_link = " + SModSysConsts.USRS_LINK_USR + " "
                                + "AND cu.id_ref = " + params.getParamsMap().get(SModConsts.USRU_USR) + " "
                                + "UNION "
                                + "SELECT DISTINCT c.* FROM " + SModConsts.TablesMap.get(type) + " AS c " 
                                + "INNER JOIN trn_mat_cons_subent_usr AS cu ON c.id_mat_cons_ent = cu.id_mat_cons_ent " 
                                + "WHERE cu.id_link = " + SModSysConsts.USRS_LINK_USR + " " 
                                + "AND cu.id_ref = " + params.getParamsMap().get(SModConsts.USRU_USR) + " "
                                + ") AS a "
                                + "WHERE NOT b_del "
                                + "ORDER BY a.name";
                        break;
                    default:
                        sql = "SELECT id_mat_cons_ent AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " "
                                + "FROM " + SModConsts.TablesMap.get(type) + " "
                                + "WHERE NOT b_del "
                                + "ORDER BY name";
                        break;
                }
                break;
            case SModConsts.TRN_MAT_CONS_SUBENT:
                settings = new SGuiCatalogueSettings("Subcentro de consumo", 2, 1);
                switch (subtype) {
                    case SModConsts.USRU_USR:
                        sql = "SELECT a.id_mat_cons_ent AS " + SDbConsts.FIELD_ID + "1, a.id_mat_cons_subent AS " + SDbConsts.FIELD_ID + "2 , CONCAT(a.code, ' - ', a.name) AS " + SDbConsts.FIELD_ITEM + ", "
                                + "a.id_mat_cons_ent AS " + SDbConsts.FIELD_FK + "1 "
                                + "FROM ("
                                + "SELECT DISTINCT c.* FROM " + SModConsts.TablesMap.get(type) + " AS c "
                                + "INNER JOIN trn_mat_cons_ent_usr AS cu ON c.id_mat_cons_ent = cu.id_mat_cons_ent "
                                + "WHERE cu.id_link = " + SModSysConsts.USRS_LINK_USR + " "
                                + "AND cu.id_ref = " + params.getParamsMap().get(SModConsts.USRU_USR) + " "
                                + "UNION "
                                + "SELECT DISTINCT c.* FROM " + SModConsts.TablesMap.get(type) + " AS c " 
                                + "INNER JOIN trn_mat_cons_subent_usr AS cu ON c.id_mat_cons_ent = cu.id_mat_cons_ent AND c.id_mat_cons_subent = cu.id_mat_cons_subent " 
                                + "WHERE cu.id_link = " + SModSysConsts.USRS_LINK_USR + " " 
                                + "AND cu.id_ref = " + params.getParamsMap().get(SModConsts.USRU_USR) + " "
                                + ") AS a "
                                + "WHERE NOT b_del "
                                + "ORDER BY a.name";
                        break;
                    default:
                        sql = "SELECT id_mat_cons_ent AS " + SDbConsts.FIELD_ID + "1, id_mat_cons_subent AS " + SDbConsts.FIELD_ID + "2 , CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + ", "
                                + "id_mat_cons_ent AS " + SDbConsts.FIELD_FK + "1 "
                                + "FROM " + SModConsts.TablesMap.get(type) + " "
                                + "WHERE NOT b_del "
                                + (params != null && params.getKey() != null ? "AND id_mat_cons_ent = " + params.getKey()[0] : "") + " " 
                                + "ORDER BY name";
                        break;
                }
                break;
            case SModConsts.TRN_MAT_CONS_ENT_BUDGET:
                settings = new SGuiCatalogueSettings("Presupuesto", 3);
                sql = "SELECT id_mat_cons_ent AS " + SDbConsts.FIELD_ID + "1, id_year AS " + SDbConsts.FIELD_ID + "2, id_period AS " + SDbConsts.FIELD_ID + "3, "
                        + "CONCAT(id_year, ' - ', id_period) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " " 
                        + "WHERE id_mat_cons_ent = " + ((int[]) params.getParamsMap().get(SModConsts.TRN_MAT_CONS_ENT))[0] + " " 
                        + "AND id_year = " + (int) params.getParamsMap().get(SLibConsts.DATA_TYPE_DATE);
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
            case SModConsts.TRN_MAT_CC_GRP:
                view = new SViewMaterialCostCenterGroup(miClient, "Grupos CC");
                break;
            case SModConsts.TRN_MAT_REQ:
                switch(subtype) {
                    case SModSysConsts.TRNX_MAT_REQ_PET:
                        switch (params.getType()) {
                            case SModSysConsts.TRNS_ST_MAT_REQ_NEW: title = "Mis RM nuevas"; break;
                            case SModSysConsts.TRNS_ST_MAT_REQ_AUTH: title = "Mis RM x autorizar"; break;
                            case SModSysConsts.TRNS_ST_MAT_REQ_PROV: 
                                switch (params.getSubtype()) {
                                    case SModSysConsts.TRNX_ST_MAT_REQ_PROV_PROV: title = "Mis RM x suministrar"; break;
                                    case SModSysConsts.TRNX_ST_MAT_REQ_PROV_PUR: title = "Mis RM x comprar"; break;
                                }
                                break;
                            case SLibConsts.UNDEFINED: title = "Todas mis RM"; break;
                            case SModConsts.TRN_MAT_CONS_ENT_USR: title = "RM mis c. consumo"; break;
                            case SModConsts.TRN_MAT_PROV_ENT_USR: title = "RM mis c. suministro"; break;
                        }
                        break;
                    case SModSysConsts.TRNX_MAT_REQ_REV:
                        switch (params.getType()) {
                            case SModSysConsts.TRNS_ST_MAT_REQ_AUTH: title = "RM x autorizar"; break;
                            case SModSysConsts.TRNX_MAT_REQ_AUTHO: title = "RM autorizadas"; break;
                            case SModSysConsts.TRNX_MAT_REQ_AUTHO_RECH: title = "RM rechazadas"; break;
                        }
                        break;
                }
                view = new SViewMaterialRequest(miClient, subtype, title, params);
                break;
            case SModConsts.TRN_STK_VAL:
                title = "Valuación de inventarios";
                view = new SViewStockValuation(miClient, title);
                break;
            case SModConsts.TRNX_MAT_REQ_PEND_SUP:
                switch(subtype) {
                    case SModSysConsts.TRNX_MAT_REQ_DETAIL: title = "RM consumo x suministrar detalle";
                        break;
                    case SModSysConsts.TRNX_MAT_REQ_PROVIDED: title = "RM consumo suministradas";
                        break;
                    case SModSysConsts.TRNX_MAT_REQ_PROVIDED_DETAIL: title = "RM consumo suministradas detalle";
                        break;
                    case SLibConsts.UNDEFINED: title = "RM consumo x suministrar";
                        break;
                }
                view = new SViewMaterialRequestPending(miClient, SModConsts.TRNX_MAT_REQ_PEND_SUP, subtype, title, params);
                break;
            case SModConsts.TRNX_MAT_REQ_STK_SUP:
                switch(subtype) {
                    case SModSysConsts.TRNX_MAT_REQ_DETAIL: title = "RM resurtido x suministrar a detalle";
                        break;
                    case SModSysConsts.TRNX_MAT_REQ_PROVIDED: title = "RM resutido suministradas";
                        break;
                    case SModSysConsts.TRNX_MAT_REQ_PROVIDED_DETAIL: title = "RM resurtido suministradas detalle";
                        break;
                    case SLibConsts.UNDEFINED: title = "RM resurtido x suministrar";
                        break;
                }
                view = new SViewMaterialRequesPendingSupply(miClient, SModConsts.TRNX_MAT_REQ_STK_SUP, subtype, title, params);
                break;
            case SModConsts.TRNX_MAT_REQ_PEND_PUR:
                switch(subtype) {
                    case SModSysConsts.TRNX_MAT_REQ_DETAIL: title = "RM x pedir detalle";
                        break;
                    case SLibConsts.UNDEFINED: title = "RM x pedir";
                        break;
                }
                view = new SViewMaterialRequestPending(miClient, SModConsts.TRNX_MAT_REQ_PEND_PUR, subtype, title, params);
                break;
            case SModConsts.TRNX_MAT_REQ_CLO_PUR:
                switch(subtype) {
                    case SModSysConsts.TRNX_MAT_REQ_DETAIL: title = "RM pedidas detalle";
                       break;
                    default: title = "RM pedidas";
                        break;
                }
                view = new SViewMaterialRequestPending(miClient, SModConsts.TRNX_MAT_REQ_CLO_PUR, subtype, title, params);
                break;
            case SModConsts.TRNX_MAT_REQ_EST:
                switch(subtype) {
                    case SModSysConsts.TRNX_MAT_REQ_PEND_ESTIMATE: title = "RM x solicitar cotización";
                        break;
                    case SModSysConsts.TRNX_MAT_REQ_ESTIMATED: title = "RM solicitada cotización";
                        break;
                    case SLibConsts.UNDEFINED: title = "RM x solicitar cotización";
                        break;
                }
                view = new SViewMaterialRequestPendingEstimation(miClient, SModConsts.TRNX_MAT_REQ_EST, subtype, title, params);
                break;
            case SModConsts.TRN_MAT_CONS_ENT:
                view = new SViewMaterialConsumptionEntity(miClient, "C. consumo");
                break;
            case SModConsts.TRN_MAT_CONS_SUBENT:
                view = new SViewMaterialConsumptionSubentity(miClient, "Subc. consumo");
                break;
            case SModConsts.TRN_MAT_CONS_ENT_BUDGET:
                view = new SViewMaterialConsumptionEntityBudget(miClient, "Presupuestos c. consumo");
                break;
            case SModConsts.TRN_MAT_PROV_ENT:
                view = new SViewMaterialProvisionEntity(miClient, "C. suministro");
                break;
            case SModConsts.TRN_ITEM_COST:
                view = new SViewItemCost(miClient, "Costos ítems");
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
            case SModConsts.TRNX_CONF_USR_VS_ENT:
                view = new SViewConfUserVsEntity(miClient, "Config. usuarios vs. c. consumo suministro");
                break;
            case SModConsts.TRNX_DET_USR_VS_ENT:
                switch (subtype) {
                    case SModConsts.TRN_MAT_CONS_ENT_USR:
                        title = "Usuarios vs. c. consumo (detalle)";
                        break;
                    case SModConsts.TRN_MAT_PROV_ENT_USR:
                        title = "Usuarios vs. c. suministro (detalle)";
                        break;
                }
                view = new SViewConfUserVsEntityDetail(miClient, subtype, title);
                break;
            case SModConsts.TRNX_CONF_EMP_VS_ENT:
                view = new SViewConfEmployeeVsEntity(miClient, "Config. empleados vs. c. consumo");
                break;
            case SModConsts.TRNX_DET_EMP_VS_ENT:
                view = new SViewConfEmployeeVsEntityDetail(miClient, "Empleados vs. c. consumo (detalle)");
                break;
            case SModConsts.TRNX_CONF_WHS_VS_PRV_ENT:
                view = new SViewConfWarehouseVsProvEntity(miClient, "Config. almacenes vs. c. suministro");
                break;
            case SModConsts.TRNX_DET_WHS_VS_PRV_ENT:
                view = new SViewConfWarehouseVsProvEntityDetail(miClient, "Almacenes vs. c. suministro (detalle)");
                break;
            case SModConsts.TRNX_CONF_WHS_VS_CON_ENT:
                view = new SViewConfWarehouseVsConsEntity(miClient, "Config. almacenes vs. c. consumo");
                break;
            case SModConsts.TRNX_DET_WHS_VS_CON_ENT:
                view = new SViewConfWarehouseVsConsEntityDetail(miClient, "Almacenes vs. c. consumo (detalle)");
                break;
            case SModConsts.TRNX_CONF_SUBENT_VS_CC:
                view = new SViewConfMatConsSubentityVsCostCenter(miClient, "Config. subc. consumo vs. CC");
                break;
            case SModConsts.TRNX_DET_SUBENT_VS_CC:
                view = new SViewConfMatConsSubentityVsCostCenterDetail(miClient, "Subc. consumo vs. CC (detalle)");
                break;
            case SModConsts.TRNX_CONF_SUBENT_VS_CC_GRP:
                view = new SViewConfMatConsSubentityCCVsCostCenterGroup(miClient, "Config. subc. consumo vs. CC vs. grupos CC");
                break;
            case SModConsts.TRNX_DET_SUBENT_VS_CC_GRP:
                view = new SViewConfMatConsSubentityCCVsCostCenterGroupDetail(miClient, "Subc. consumo vs. CC vs. grupos CC (detalle)");
                break;
            case SModConsts.TRNX_CONF_CC_GRP_VS_ITM:
                view = new SViewConfMatCostCenterGroupItem(miClient, "Config. grupos CC vs. ítems");
                break;
            case SModConsts.TRNX_DET_CC_GRP_VS_ITM:
                view = new SViewConfMatCostCenterGroupItemDetail(miClient, "Grupos CC vs. ítems (detalle)");
                break;
            case SModConsts.TRNX_CONF_CC_GRP_VS_USR:
                view = new SViewConfMatCostCenterGroupUser(miClient, "Config. grupos CC vs. usuarios y empleados");
                break;
            case SModConsts.TRNX_DET_CC_GRP_VS_USR:
                view = new SViewConfMatCostCenterGroupUserDetail(miClient, "Grupos CC vs. usuarios y empleados (detalle)");
                break;
            case SModConsts.TRNX_MAT_CONS:
                view = new SViewReportMaterialConsuption(miClient, "Consumo materiales");
                break;
            case SModConsts.TRNX_MAT_CONS_CC:
                switch (subtype) {
                    case SModConsts.TRNX_MAT_CONS_CC_R: title = "CC consumo materiales resumen"; break;
                    default: title = "CC consumo materiales"; break;
                }
                view = new SViewReportMaterialConsuptionCC(miClient, subtype, title);
                break;
            case SModConsts.TRNX_MAT_BUDGET_SUM:
                view = new SViewReportBudgetSummary(miClient, "Resumen presupuestos vs. gastos");
                break;
            case SModConsts.TRNX_WAH_CONS_DET:
                view = new SViewWarehouseConsumptionDetail(miClient, "Consumo almacenes detalle");
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
            case SModConsts.TRN_MAT_CONS_ENT_BUDGET:
                if (moFormMaterialConsumptionEntityBudget == null) moFormMaterialConsumptionEntityBudget = new SFormMaterialConsumptionEntityBudget(miClient, "Presupuesto centros de consumo");
                form = moFormMaterialConsumptionEntityBudget;
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
            case SModConsts.TRN_MAT_CC_GRP:
                if(moFormMaterialCostCenterGroup == null) moFormMaterialCostCenterGroup = new SFormMaterialCostCenterGroup(miClient, "Grupo de centro de costo");
                form = moFormMaterialCostCenterGroup;
                break;
            case SModConsts.TRN_MAT_CONS_ENT:
                if (moFormMaterialConsumptionEntity == null) moFormMaterialConsumptionEntity = new SFormMaterialConsumptionEntity(miClient, "Centro de consumo");
                form = moFormMaterialConsumptionEntity;
                break;
            case SModConsts.TRN_MAT_CONS_SUBENT:
                if (moFormMaterialConsumptionSubentity == null) moFormMaterialConsumptionSubentity = new SFormMaterialConsumptionSubentity(miClient, "Subcentro de consumo");
                form = moFormMaterialConsumptionSubentity;
                break;
            case SModConsts.TRN_MAT_PROV_ENT:
                if (moFormMaterialProvisionEntity == null) moFormMaterialProvisionEntity = new SFormMaterialProvisionEntity(miClient, "Centro de suministro");
                form = moFormMaterialProvisionEntity;
                break;
            case SModConsts.TRN_MAT_REQ: 
            case SModConsts.TRNX_MAT_REQ_STK_SUP:
            case SModConsts.TRNX_MAT_REQ_EST:
                if (moFormMaterialReq == null) moFormMaterialReq = new SFormMaterialRequest(miClient, "Requisición de materiales", type);
                form = moFormMaterialReq;
                break;
            case SModConsts.TRNX_MAT_REQ_PEND_SUP:
            case SModConsts.TRNX_MAT_REQ_PEND_PUR:
                if (moFormMaterialReqSup == null) moFormMaterialReqSup = new SFormMaterialRequest(miClient, "Requisición de materiales", type);
                form = moFormMaterialReqSup;
                break;
            case SModConsts.TRN_MAT_REQ_CC:
                if (moFormMaterialRequestCostCenter == null) moFormMaterialRequestCostCenter = new SFormMaterialRequestCostCenter(miClient, subtype, "Requisición de materiales y centros de costo");
                form = moFormMaterialRequestCostCenter;
                break;
            case SModConsts.TRN_STK_VAL:
                if (moFormStockValuation == null) moFormStockValuation = new SFormStockValuation(miClient, "Valuación de inventario");
                form = moFormStockValuation;
                break;
            case SModConsts.TRNX_FUNC_BUDGETS:
                if (moFormFunctionalAreaBudgets == null) moFormFunctionalAreaBudgets = new SFormFunctionalAreaBudgets(miClient, "Presupuestos mensuales de gastos");
                form = moFormFunctionalAreaBudgets;
                break;
            case SModConsts.TRNX_CONF_USR_VS_ENT:
                if (moFormUserVsEntity == null) moFormUserVsEntity = new SFormConfUserVsEntity(miClient, "Configuración de usuario vs. centros de consumo/suministro");
                form = moFormUserVsEntity;
                break;
            case SModConsts.TRNX_CONF_EMP_VS_ENT:
                if (moFormEmployeeVsEntity == null) moFormEmployeeVsEntity = new SFormConfEmployeeVsEntity(miClient, "Configuración de empleado vs. centros");
                form = moFormEmployeeVsEntity;
                break;
            case SModConsts.TRNX_CONF_WHS_VS_PRV_ENT:
                if (moFormWarehouseVsProvEntity == null) moFormWarehouseVsProvEntity = new SFormConfWarehouseVsProvEntity(miClient, "Configuración de almacén vs. centros de suministro");
                form = moFormWarehouseVsProvEntity;
                break;
            case SModConsts.TRNX_CONF_WHS_VS_CON_ENT:
                if (moFormWarehouseVsConsEntity == null) moFormWarehouseVsConsEntity = new SFormConfWarehouseVsConsEntity(miClient, "Configuración de almacén vs. centros de consumo");
                form = moFormWarehouseVsConsEntity;
                break;
            case SModConsts.TRNX_CONF_SUBENT_VS_CC:
                if (moFormConsSubentityVsCostCenter == null) moFormConsSubentityVsCostCenter = new SFormConfMatConsSubentityVsCostCenter(miClient, "Configuración de centro de consumo vs. centro de costo");
                form = moFormConsSubentityVsCostCenter;
                break;
            case SModConsts.TRNX_CONF_SUBENT_VS_CC_GRP:
                if (moFormConsSubentityCCVsCostCenterGroup == null) moFormConsSubentityCCVsCostCenterGroup = new SFormConfMatConsSubentityCCVsCostCenterGroup(miClient, "Configuración subentidad de consumo centro de costo vs. gpo cc");
                form = moFormConsSubentityCCVsCostCenterGroup;
                break;
            case SModConsts.TRNX_CONF_CC_GRP_VS_ITM:
                if (moFormConfMatCostCenterGroupVsItem == null) moFormConfMatCostCenterGroupVsItem = new SFormConfMatCostCenterGroupVsItem(miClient, "Configuración grupo de centro de costo vs. ítems");
                form = moFormConfMatCostCenterGroupVsItem;
                break;
            case SModConsts.TRNX_CONF_CC_GRP_VS_USR:
                if (moFormConfMatCostCenterGroupVsUser == null) moFormConfMatCostCenterGroupVsUser = new SFormConfMatCostCenterGroupVsUser(miClient, "Configuración grupo de centro de costo vs. usuarios/empleados");
                form = moFormConfMatCostCenterGroupVsUser;
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
            case SModConsts.TRN_MAT_REQ:
                guiReport = new SGuiReport("reps/trn_mat_req.jasper", "Requisición de materiales");
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
            case SModConsts.TRNR_VTAS_FSC:
                guiReport = new SGuiReport("reps/trn_vtas_fsc.jasper", "Reporte de ventas FSC");
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return guiReport;
    }
}
