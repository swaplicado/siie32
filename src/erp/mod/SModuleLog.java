/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod;

import erp.client.SClientInterface;
import erp.mod.log.db.*;
import erp.mod.log.form.*;
import erp.mod.log.view.*;
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
 * @author Néstor Ávalos
 */
public class SModuleLog extends SGuiModule {

    private SFormVehicleType moFormVehicleType;
    private SFormSpot moFormSpot;
    private SFormSpotCompanyBranch moFormSpotCompanyBranch;
    private SFormSpotCompanyBranchEntity moFormSpotCompanyBranchEntity;
    private SFormRate moFormRate;
    private SFormVehicle moFormVehicle;
    private SFormShipment moFormShipment;
    private SFormShipmentDelivery moFormShipmentDelivery;

    private SClientInterface miClient_xxx;

    public SModuleLog(SGuiClient client, SClientInterface client_xxx) {
        super(client, SModConsts.MOD_LOG_N, SLibConsts.UNDEFINED);
        moModuleIcon = miClient.getImageIcon(mnModuleType);
        miClient_xxx = client_xxx;
    }

    @Override
    public JMenu[] getMenus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry getRegistry(int type) {
        SDbRegistry registry = null;

        switch (type) {
            case SModConsts.LOGU_TP_VEH:
                registry = new SDbVehicleType();
                break;
            case SModConsts.LOGU_SPOT:
                registry = new SDbSpot();
                break;
            case SModConsts.LOGU_SPOT_COB:
                registry = new SDbSpotCompanyBranch();
                break;
            case SModConsts.LOGU_SPOT_COB_ENT:
                registry = new SDbSpotCompanyBranchEntity();
                break;
            case SModConsts.LOG_RATE:
                registry = new SDbRate();
                break;
            case SModConsts.LOG_VEH:
                registry = new SDbVehicle();
                break;
            case SModConsts.LOG_SHIP:
                registry = new SDbShipment();
                break;
            case SModConsts.LOG_SHIP_DEST:
            case SModConsts.LOGX_SHIP_DLY:
                registry = new SDbShipmentDestiny();
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
            case SModConsts.LOGS_TP_SHIP:
                settings = new SGuiCatalogueSettings("Tipo embarque", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tp_ship AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0 "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGS_TP_DLY:
                settings = new SGuiCatalogueSettings("Tipo entrega", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tp_dly AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0 "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGS_TP_SPOT:
                settings = new SGuiCatalogueSettings("Tipo lugar", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tp_spot AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0 "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGS_TP_MOT:
            case SModConsts.LOGX_TP_MOT_SHIP:
                settings = new SGuiCatalogueSettings("Tipo modo transportación", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tp_mot AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0 " + (type == SModConsts.LOGX_TP_MOT_SHIP ? "AND id_tp_mot <> " + SModSysConsts.LOGS_TP_MOT_NA : "") + " "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGS_TP_CAR:
                settings = new SGuiCatalogueSettings("Tipo transporte", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tp_car AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0 " + (params != null ? "AND id_tp_car IN (" + params.getKey()[0] +  ", " + params.getKey()[1] + ")" : "") + " "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGS_TP_DOC_SHIP:
                settings = new SGuiCatalogueSettings("Tipo documento embarcar", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tp_doc_ship AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0 "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGS_INC:
                settings = new SGuiCatalogueSettings("Incoterm", 1, 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_inc AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + ", "
                        + "fid_tp_dly AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0 " + (params != null ? "AND fid_tp_dly = " + params.getKey()[0] : "") + " OR id_inc = " + SModSysConsts.LOGS_INC_NA + " "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGU_TP_VEH:
                settings = new SGuiCatalogueSettings("Tipo vehículo", 1);
                sql = "SELECT id_tp_veh AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0 "
                        + "ORDER BY name ";
                break;
            case SModConsts.LOGU_SPOT:
                /* Use of other parameters:
                 * params.getKey(): May contain more than one type of spot id.
                 */

                String sKeysTypeSpot = "";
                settings = new SGuiCatalogueSettings("Lugar", 1, 1);
                settings.setCodeApplying(true);

                if (params != null) {
                    for (int key : params.getKey()) {
                        sKeysTypeSpot += key + ",";
                    }
                    sKeysTypeSpot = sKeysTypeSpot.isEmpty() ? "" : sKeysTypeSpot.substring(0, sKeysTypeSpot.length()-1);
                }

                sql = "SELECT id_spot AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " " +
                        ", fk_tp_spot AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0 " +
                        (!sKeysTypeSpot.isEmpty() ? "AND fk_tp_spot IN (" +  sKeysTypeSpot + ")" : "") + " " +
                        (subtype != SLibConsts.UNDEFINED ? "AND fk_tp_dly = " + subtype : "") + " "
                        + "ORDER BY name ";
                break;
            case SModConsts.LOG_VEH:
                settings = new SGuiCatalogueSettings("Vehículo", 1, 1);
                sql = "SELECT id_veh AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", fk_tp_veh AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0 "
                        + "ORDER BY name ";
                break;
            case SModConsts.LOGX_TP_VEH:
                /* Use of other parameters:
                 * params.getKey(): If is different of null will contain:
                 *      params.getKey()[0] = fk_src_spot
                 *      params.getKey()[1] = fk_des_spot
                 *      params.getKey()[2] = fk_car
                 */

                settings = new SGuiCatalogueSettings("Tipo vehículo", 1, 1, SLibConsts.DATA_TYPE_BOOL);
                sql = "SELECT tp.id_tp_veh AS " + SDbConsts.FIELD_ID + "1, CONCAT(tp.name, ' (', tp.code, ')', IF(r.b_con = 1, ' - CONSOLIDADO', '')) AS " + SDbConsts.FIELD_ITEM + ", "
                        + "r.id_rate AS " + SDbConsts.FIELD_FK + "1, r.b_con AS " + SDbConsts.FIELD_COMP + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.LOGU_TP_VEH) + " AS tp "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_RATE) + " AS r ON tp.id_tp_veh = r.fk_tp_veh "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS src ON r.fk_src_spot = src.id_spot "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS des ON r.fk_src_spot = des.id_spot "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS car ON r.fk_car = car.id_bp "
                        + "WHERE r.b_del = 0 " + (params == null ? "" : "AND r.fk_src_spot = " + params.getKey()[0] + " AND r.fk_des_spot = " + params.getKey()[1] + " AND "
                        + "r.fk_car = " + params.getKey()[2]) + " "
                        + "GROUP BY tp.id_tp_veh, r.b_con "
                        + "ORDER BY tp.name ";
                break;
            case SModConsts.LOGX_RATE:
                /* Use of other parameters:
                 * params.getKey(): If is different of null will contain:
                 *      params.getKey()[0] = fk_src_spot
                 *      params.getKey()[1] = fk_des_spot
                 *      params.getKey()[2] = fk_car
                 *      params.getKey()[3] = fk_tp_veh
                 */

                settings = new SGuiCatalogueSettings("Tarifa", 1, 1, SLibConsts.DATA_TYPE_BOOL);
                sql = "SELECT r.id_rate AS " + SDbConsts.FIELD_ID + "1, CONCAT(r.rate, ' ', cur.cur_key, ' ', IF(r.b_con = 1, '(CONS.)', '')) AS " + SDbConsts.FIELD_ITEM
                        + ", r.fk_cur AS " + SDbConsts.FIELD_FK + "1, r.b_con AS " + SDbConsts.FIELD_COMP + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_RATE) + " AS r "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS cur ON r.fk_cur = cur.id_cur "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS src ON r.fk_src_spot = src.id_spot "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS des ON r.fk_src_spot = des.id_spot "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS car ON r.fk_car = car.id_bp "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_TP_VEH) + " AS tp ON r.fk_tp_veh = tp.id_tp_veh "
                        + "WHERE r.b_del = 0 " + (params == null ? "" : " AND r.fk_src_spot = " + params.getKey()[0] + " AND r.fk_des_spot = " + params.getKey()[1] + " AND "
                        + "r.fk_car = " + params.getKey()[2] + " AND r.fk_tp_veh = " + params.getKey()[3]) + " "  //+ AND r.b_con = " + (params.getKey()[4] > 0 ? "1" : "0" )) + " "
                        + "GROUP BY r.id_rate, r.fk_cur "
                        + "ORDER BY r.rate ";
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
        String title = "";

        switch (type) {
            case SModConsts.LOGU_TP_VEH:
                view = new SViewVehicleType(miClient, "Tipos vehículo");
                break;
            case SModConsts.LOGU_SPOT:
                view = new SViewSpot(miClient, "Lugares");
                break;
            case SModConsts.LOGU_SPOT_COB:
                view = new SViewSpotCompanyBranch(miClient, "Lugares sucursales empresa");
                break;
            case SModConsts.LOGU_SPOT_COB_ENT:
                view = new SViewSpotCompanyBranchEntity(miClient, "Lugares entidades sucursales");
                break;
            case SModConsts.LOG_RATE:
                view = new SViewRate(miClient, "Tarifas");
                break;
            case SModConsts.LOG_VEH:
                view = new SViewVehicle(miClient, "Vehículos");
                break;
            case SModConsts.LOG_SHIP:
                switch (params.getType()) {
                    case SModConsts.VIEW_SC_DET:
                        title += "(detalle)";
                        break;

                    case SModConsts.VIEW_SC_SUM:
                        title += "";
                        break;
                }

                view = new SViewShipment(miClient, type, subtype, "Embarques " + title, params);

                break;

            case SModConsts.LOGX_SHIP_AUTH:
                switch (subtype) {
                    case SModSysConsts.TRNS_ST_DPS_AUTHORN_AUTHORN:
                        title = "autorizados ";
                        break;

                    case SModSysConsts.TRNS_ST_DPS_AUTHORN_PENDING:
                        title = "x autorizar ";
                        break;

                    case SModSysConsts.TRNS_ST_DPS_AUTHORN_REJECT:
                        title = "rechazados ";
                        break;
                }

                switch (params.getType()) {
                    case SModConsts.VIEW_SC_DET:
                        title += "(detalle)";
                        break;

                    case SModConsts.VIEW_SC_SUM:
                        title += "";
                        break;
                }

                view = new SViewShipmentAuthorn(miClient, type, subtype, "Embarques " + title, params);

                break;
            case SModConsts.LOGX_SHIP_DPS:
                title = "Facturas ventas ";

                switch (subtype) {
                    case SModConsts.VIEW_ST_PEND:
                        title += "x embarcar ";
                        break;

                    case SModConsts.VIEW_ST_DONE:
                        title += "embarcadas ";
                        break;
                }

                switch (params.getType()) {
                    case SModConsts.VIEW_SC_DET:
                        title += "(detalle)";
                        break;

                    case SModConsts.VIEW_SC_SUM:
                        title += "";
                        break;
                }

                view = new SViewShipmentDps(miClient, type, subtype, title, params);
                break;

            case SModConsts.LOGX_SHIP_DPS_ADJ:
                title = "NC ventas ";

                switch (subtype) {
                    case SModConsts.VIEW_ST_PEND:
                        title += "x embarcar ";
                        break;

                    case SModConsts.VIEW_ST_DONE:
                        title += "embarcadas ";
                        break;
                }

                switch (params.getType()) {
                    case SModConsts.VIEW_SC_DET:
                        title += "(detalle)";
                        break;

                    case SModConsts.VIEW_SC_SUM:
                        title += "";
                        break;
                }

                view = new SViewShipmentDpsAdjustment(miClient, type, subtype, title, params);
                break;

            case SModConsts.LOGX_SHIP_DIOG:
                switch (params.getType()) {
                    case SModConsts.VIEW_SC_DET:
                        title = "(detalle)";
                        break;

                    case SModConsts.VIEW_SC_SUM:
                        title = "";
                        break;
                }

                switch (subtype) {
                    case SModConsts.VIEW_ST_PEND:
                        view = new SViewShipmentDiog(miClient, type, subtype, "Docs inventarios x embarcar " + title, params);
                        break;
                    case SModConsts.VIEW_ST_DONE:
                        view = new SViewShipmentDiog(miClient, type, subtype, "Docs. inventarios embarcados " + title, params);
                        break;
                }
                break;
            case SModConsts.LOGX_SHIP_DLY:
                switch (params.getType()) {
                    case SModConsts.VIEW_SC_DET:
                        title = "(detalle)";
                        break;

                    case SModConsts.VIEW_SC_SUM:
                        title = "";
                        break;
                }

                switch (subtype) {
                    case SModConsts.VIEW_ST_PEND:
                        view = new SViewShipmentDelivery(miClient, type, subtype, "Embarques x entregar " + title, params);
                        break;
                    case SModConsts.VIEW_ST_DONE:
                        view = new SViewShipmentDelivery(miClient, type, subtype, "Embarques entregados " + title, params);
                        break;
                }
                break;
            case SModConsts.LOGX_SHIP_BOL:
                switch (subtype) {
                    case SModConsts.VIEW_ST_PEND:
                        view = new SViewShipmentBill(miClient, miClient_xxx, type, subtype, "Embarques x facturar" + title, params);
                        break;
                    case SModConsts.VIEW_ST_DONE:
                        view = new SViewShipmentBill(miClient, miClient_xxx, type, subtype, "Embarques facturados" + title, params);
                        break;
                }
                break;

            case SModConsts.LOGX_SHIP_DPS_SAL:
                switch (subtype) {
                    case SModConsts.VIEW_ST_PEND:
                        view = new SViewShipmentDpsCost(miClient, type, subtype, "Embarques vs facs vtas x facturar" + title, params);
                        break;
                    case SModConsts.VIEW_ST_DONE:
                        view = new SViewShipmentDpsCost(miClient, type, subtype, "Embarques vs facs vtas facturadas" + title, params);
                        break;
                }
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
            case SModConsts.LOGU_TP_VEH:
                if (moFormVehicleType == null) moFormVehicleType = new SFormVehicleType(miClient, "Tipo de vehículo");
                form = moFormVehicleType;
                break;
            case SModConsts.LOGU_SPOT:
                if (moFormSpot == null) moFormSpot = new SFormSpot(miClient, "Lugar");
                form = moFormSpot;
                break;
            case SModConsts.LOGU_SPOT_COB:
                if (moFormSpotCompanyBranch == null) moFormSpotCompanyBranch = new SFormSpotCompanyBranch(miClient, "Lugar de sucursal de la empresa");
                form = moFormSpotCompanyBranch;
                break;
            case SModConsts.LOGU_SPOT_COB_ENT:
                if (moFormSpotCompanyBranchEntity == null) moFormSpotCompanyBranchEntity = new SFormSpotCompanyBranchEntity(miClient, "Lugar de la entidad de la empresa");
                form = moFormSpotCompanyBranchEntity;
                break;
            case SModConsts.LOG_RATE:
                if (moFormRate == null) moFormRate = new SFormRate(miClient, "Tarifa");
                form = moFormRate;
                break;
            case SModConsts.LOG_VEH:
                if (moFormVehicle == null) moFormVehicle = new SFormVehicle(miClient, "Vehículo");
                form = moFormVehicle;
                break;
            case SModConsts.LOG_SHIP:
                if (moFormShipment == null) moFormShipment = new SFormShipment(miClient, miClient_xxx, "Embarque");
                form = moFormShipment;
                break;
            case SModConsts.LOGX_SHIP_DLY:
                if (moFormShipmentDelivery == null) moFormShipmentDelivery = new SFormShipmentDelivery(miClient, "Asignación fecha de entrega real");
                form = moFormShipmentDelivery;
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
            case SModConsts.LOGR_SHIP:
                guiReport = new SGuiReport("reps/log_ship.jasper", "Reporte embarque");
                break;
            default:
        }

        return guiReport;
    }
}
