/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.gui.session.SSessionCustom;
import erp.mod.log.db.SDbBillOfLading;
import erp.mod.log.db.SDbBizPartnerBranchAddressNeighborhood;
import erp.mod.log.db.SDbBolLocation;
import erp.mod.log.db.SDbBolMerchandise;
import erp.mod.log.db.SDbBolMerchandiseQuantity;
import erp.mod.log.db.SDbBolPerson;
import erp.mod.log.db.SDbBolTransportationMode;
import erp.mod.log.db.SDbBolTransportationModeExtra;
import erp.mod.log.db.SDbDriverCarrier;
import erp.mod.log.db.SDbFreightClass;
import erp.mod.log.db.SDbFreightTariff;
import erp.mod.log.db.SDbInsurer;
import erp.mod.log.db.SDbRate;
import erp.mod.log.db.SDbService;
import erp.mod.log.db.SDbShipment;
import erp.mod.log.db.SDbShipmentDestiny;
import erp.mod.log.db.SDbShipmentDestinyEntry;
import erp.mod.log.db.SDbShipmentNotes;
import erp.mod.log.db.SDbSpot;
import erp.mod.log.db.SDbSpotCompanyBranch;
import erp.mod.log.db.SDbSpotCompanyBranchEntity;
import erp.mod.log.db.SDbTankCar;
import erp.mod.log.db.SDbTrailer;
import erp.mod.log.db.SDbVehicle;
import erp.mod.log.db.SDbVehicleCarrier;
import erp.mod.log.db.SDbVehicleType;
import erp.mod.log.form.SFormBillOfLading;
import erp.mod.log.form.SFormBolPerson;
import erp.mod.log.form.SFormFreightClass;
import erp.mod.log.form.SFormFreightTariff;
import erp.mod.log.form.SFormInsurer;
import erp.mod.log.form.SFormRate;
import erp.mod.log.form.SFormService;
import erp.mod.log.form.SFormShipment;
import erp.mod.log.form.SFormShipmentDelivery;
import erp.mod.log.form.SFormSpot;
import erp.mod.log.form.SFormSpotCompanyBranch;
import erp.mod.log.form.SFormSpotCompanyBranchEntity;
import erp.mod.log.form.SFormTankCar;
import erp.mod.log.form.SFormTrailer;
import erp.mod.log.form.SFormVehicle;
import erp.mod.log.form.SFormVehicleType;
import erp.mod.log.view.SViewBillOfLading;
import erp.mod.log.view.SViewBolPerson;
import erp.mod.log.view.SViewDriverCarrier;
import erp.mod.log.view.SViewFreightClass;
import erp.mod.log.view.SViewFreightTariff;
import erp.mod.log.view.SViewInsurer;
import erp.mod.log.view.SViewRate;
import erp.mod.log.view.SViewService;
import erp.mod.log.view.SViewShipment;
import erp.mod.log.view.SViewShipmentAuthorn;
import erp.mod.log.view.SViewShipmentBill;
import erp.mod.log.view.SViewShipmentDelivery;
import erp.mod.log.view.SViewShipmentDiog;
import erp.mod.log.view.SViewShipmentDps;
import erp.mod.log.view.SViewShipmentDpsAdjustment;
import erp.mod.log.view.SViewShipmentDpsCost;
import erp.mod.log.view.SViewSpot;
import erp.mod.log.view.SViewSpotCompanyBranch;
import erp.mod.log.view.SViewSpotCompanyBranchEntity;
import erp.mod.log.view.SViewTankCar;
import erp.mod.log.view.SViewTrailer;
import erp.mod.log.view.SViewVehicle;
import erp.mod.log.view.SViewVehicleCarrier;
import erp.mod.log.view.SViewVehicleType;
import erp.mtrn.data.SCfdUtils;
import java.sql.ResultSet;
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
 * @author Néstor Ávalos, Isabel Servín, Sergio Flores
 */
public class SModuleLog extends SGuiModule {

    private SFormVehicleType moFormVehicleType;
    private SFormSpot moFormSpot;
    private SFormSpotCompanyBranch moFormSpotCompanyBranch;
    private SFormSpotCompanyBranchEntity moFormSpotCompanyBranchEntity;
    private SFormInsurer moFormInsurer;
    private SFormVehicle moFormVehicle;
    private SFormTrailer moFormTrailer;
    private SFormTankCar moFormTankCar;
    private SFormBolPerson moFormBolPerson;
    private SFormRate moFormRate;
    private SFormService moFormService;
    private SFormFreightClass moFormFreightClass;
    private SFormFreightTariff moFormFreightTariff;
    private SFormShipment moFormShipment;
    private SFormBillOfLading moFormBillOfLadingTypeT;
    private SFormBillOfLading moFormBillOfLadingTypeI;
    private SFormShipmentDelivery moFormShipmentDelivery;

    private final SClientInterface miClient_xxx;

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
    public SDbRegistry getRegistry(final int type, final SGuiParams params) {
        SDbRegistry registry = null;

        switch (type) {
            case SModConsts.LOGS_TP_SPOT:
                registry = new SDbRegistrySysFly(type) {
                    @Override
                    public void initRegistry() { }
                    @Override
                    public String getSqlTable() { return SModConsts.TablesMap.get(type); }
                    @Override
                    public String getSqlWhere(int[] key) { return "WHERE id_tp_spot = " + key[0] + " "; }
                };
                break;
            case SModConsts.LOGS_TP_BOL_PERSON:
                registry = new SDbRegistrySysFly(type) {
                    @Override
                    public void initRegistry() { }
                    @Override
                    public String getSqlTable() { return SModConsts.TablesMap.get(type); }
                    @Override
                    public String getSqlWhere(int[] key) { return "WHERE id_tp_bol_person = " + key[0] + " "; }
                };
                break;
            case SModConsts.LOGS_INC:
                registry = new SDbRegistrySysFly(type) {
                    @Override
                    public void initRegistry() { }
                    @Override
                    public String getSqlTable() { return SModConsts.TablesMap.get(type); }
                    @Override
                    public String getSqlWhere(int[] key) { return "WHERE id_inc = " + key[0] + " "; }
                };
                break;
            case SModConsts.LOGS_TP_SERVICE:
                registry = new SDbRegistrySysFly(type) {
                    @Override
                    public void initRegistry() { }
                    @Override
                    public String getSqlTable() { return SModConsts.TablesMap.get(type); }
                    @Override
                    public String getSqlWhere(int[] key) { return "WHERE id_tp_service = " + key[0] + " "; }
                };
                break;
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
            case SModConsts.LOGU_BPB_ADD_NEI:
                registry = new SDbBizPartnerBranchAddressNeighborhood();
                break;
            case SModConsts.LOG_INSURER:
                registry = new SDbInsurer();
                break;
            case SModConsts.LOG_VEH:
                registry = new SDbVehicle();
                break;
            case SModConsts.LOG_VEH_BP_CAR:
                registry = new SDbVehicleCarrier();
                break;
            case SModConsts.LOG_TRAILER:
                registry = new SDbTrailer();
                break;
            case SModConsts.LOG_TANK_CAR:
                registry = new SDbTankCar();
                break;
            case SModConsts.LOG_BOL_PERSON:
                registry = new SDbBolPerson();
                break;
            case SModConsts.LOG_DRIVER_BP_CAR:
                registry = new SDbDriverCarrier();
                break;
            case SModConsts.LOG_DRIVER_VEH:
                throw new UnsupportedOperationException("Not supported yet.");
            case SModConsts.LOG_DIST_LOCATION:
                throw new UnsupportedOperationException("Not supported yet.");
            case SModConsts.LOG_RATE:
                registry = new SDbRate();
                break;
            case SModConsts.LOG_SERVICE:
                registry = new SDbService();
                break;
            case SModConsts.LOG_FREIGHT_CLASS:
                registry = new SDbFreightClass();
                break;
            case SModConsts.LOG_FREIGHT_TARIFF:
                registry = new SDbFreightTariff();
                break;
            case SModConsts.LOG_SHIP:
                registry = new SDbShipment();
                break;
            case SModConsts.LOG_SHIP_NTS:
                registry = new SDbShipmentNotes();
                break;
            case SModConsts.LOG_SHIP_DEST:
            case SModConsts.LOGX_SHIP_DLY:
                registry = new SDbShipmentDestiny();
                break;
            case SModConsts.LOG_SHIP_DEST_ETY:
                registry = new SDbShipmentDestinyEntry();
                break;
            case SModConsts.LOG_BOL:
                registry = new SDbBillOfLading();
                break;
            case SModConsts.LOG_BOL_TRANSP_MODE:
                registry = new SDbBolTransportationMode();
                break;
            case SModConsts.LOG_BOL_TRANSP_MODE_EXTRA:
                registry = new SDbBolTransportationModeExtra();
                break;
            case SModConsts.LOG_BOL_LOCATION:
                registry = new SDbBolLocation((SDbBillOfLading) params.getParamsMap().get(SModConsts.LOG_BOL));
                break;
            case SModConsts.LOG_BOL_MERCH:
                registry = new SDbBolMerchandise();
                break;
            case SModConsts.LOG_BOL_MERCH_QTY:
                registry = new SDbBolMerchandiseQuantity();
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
            case SModConsts.LOGS_TP_SHIP:
                settings = new SGuiCatalogueSettings("Tipo embarque", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tp_ship AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGS_TP_DLY:
                settings = new SGuiCatalogueSettings("Tipo entrega", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tp_dly AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGS_TP_SPOT:
                settings = new SGuiCatalogueSettings("Tipo lugar", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tp_spot AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGS_TP_MOT:
            case SModConsts.LOGX_TP_MOT_SHIP:
                settings = new SGuiCatalogueSettings("Tipo modo transportación", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tp_mot AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del " + (type == SModConsts.LOGX_TP_MOT_SHIP ? "AND id_tp_mot <> " + SModSysConsts.LOGS_TP_MOT_NA : "") + " "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGS_TP_CAR:
                settings = new SGuiCatalogueSettings("Tipo transporte", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tp_car AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del " + (params != null ? "AND id_tp_car IN (" + params.getKey()[0] +  ", " + params.getKey()[1] + ")" : "") + " "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGS_TP_DOC_SHIP:
                settings = new SGuiCatalogueSettings("Tipo documento embarcar", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tp_doc_ship AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGS_TP_BOL_PERSON: 
                settings = new SGuiCatalogueSettings("Tipo figura transporte", 1);
                sql = "SELECT id_tp_bol_person AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " " 
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGS_INC:
                settings = new SGuiCatalogueSettings("Entrega (Incoterm)", 1, 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_inc AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + ", "
                        + "fid_tp_dly AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del " + (params != null ? "AND fid_tp_dly = " + params.getKey()[0] : "") + " OR id_inc = " + SModSysConsts.LOGS_INC_NA + " "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGS_TP_SERVICE:
                settings = new SGuiCatalogueSettings("Tipo servicio logística", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tp_service AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGS_TP_SHIP_SIZE:
                settings = new SGuiCatalogueSettings("Tipo carga", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tp_ship_size AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY sort, name ";
                break;
            case SModConsts.LOGU_TP_VEH:
                settings = new SGuiCatalogueSettings("Tipo vehículo", 1);
                sql = "SELECT id_tp_veh AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY name, id_tp_veh ";
                break;
            case SModConsts.LOGU_SPOT:
                /* Use of GUI parameters:
                 * params: Optional, can be null.
                 * params.getType(): May contain flag that catalog is needed only for company branch's spots.
                 * params.getSubtype(): May contain current company ID to filter only matching spots.
                 * params.getKey(): May contain more than one type of spot id.
                 */
                settings = new SGuiCatalogueSettings("Lugar", 1, 1);
                settings.setCodeApplying(true);

                boolean isCobSpots = params != null && params.getType() == SModConsts.LOGU_SPOT_COB && params.getSubtype() != 0;
                String spotTypes = "";
                
                if (params != null && params.getKey() != null) {
                    for (int id : params.getKey()) {
                        spotTypes += (spotTypes.isEmpty() ? "" : ", ") + id;
                    }
                }

                sql = "SELECT s.id_spot AS " + SDbConsts.FIELD_ID + "1, s.name AS " + SDbConsts.FIELD_ITEM + ", s.code AS " + SDbConsts.FIELD_CODE + ", "
                        + "s.fk_tp_spot AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " AS s "
                        + (!isCobSpots ? "" :
                            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT_COB) + " AS scob ON scob.fk_spot = s.id_spot AND NOT scob.b_del "
                            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob ON cob.id_bpb = scob.id_cob AND cob.fid_bp = " + params.getSubtype() + " ")
                        + "WHERE NOT s.b_del " +
                        (spotTypes.isEmpty() ? "" : "AND s.fk_tp_spot IN (" +  spotTypes + ")") + " " +
                        (subtype == SLibConsts.UNDEFINED ? "" : "AND s.fk_tp_dly = " + subtype) + " "
                        + "ORDER BY s.name, s.id_spot ";
                break;
            case SModConsts.LOG_INSURER:
                settings = new SGuiCatalogueSettings("Aseguradora", 1);
                sql = "SELECT id_insurer AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY name, id_insurer ";
                break;
            case SModConsts.LOG_VEH:
                /* Use of GUI parameters:
                 * params: Optional, can be null.
                 * params.getType(): Optional, can be zero. Contains the desired vehicle type.
                 * params.getKey(): Optional, can be null. Contain the carrier of the desired vehicles.
                 */
                settings = new SGuiCatalogueSettings("Vehículo", 1, 1);
                sql = "SELECT v.id_veh AS " + SDbConsts.FIELD_ID + "1, v.name AS " + SDbConsts.FIELD_ITEM + ", v.fk_tp_veh AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " AS v "
                        + (params == null || params.getKey() == null ? "" :
                            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_VEH_BP_CAR) + " AS vbp ON vbp.id_veh = v.id_veh AND vbp.id_car = " + params.getKey()[0] + " ")
                        + "WHERE NOT v.b_del " + (params == null || params.getType() == 0 ? "" : "AND v.fk_tp_veh = " + params.getType() + " ")
                        + "ORDER BY v.name, v.id_veh ";
                break;
            case SModConsts.LOG_TRAILER:
                settings = new SGuiCatalogueSettings("Remolque", 1);
                sql = "SELECT id_trailer AS " + SDbConsts.FIELD_ID + "1, name AS "  + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY name, id_trailer ";
                break;
            case SModConsts.LOG_BOL_PERSON:
                String title = "";
                switch (subtype) {
                    case SModSysConsts.LOGS_TP_BOL_PERSON_DRI: title = "Chofer"; break;
                    case SModSysConsts.LOGS_TP_BOL_PERSON_OWN: title = "Propietario"; break;
                    case SModSysConsts.LOGS_TP_BOL_PERSON_LES: title = "Arrendador"; break;
                    case SModSysConsts.LOGS_TP_BOL_PERSON_NOT: title = "Notificado"; break;
                }
                settings = new SGuiCatalogueSettings(title, 1, 1);
                sql = "SELECT id_bol_person AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", fk_tp_bol_person AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE fk_tp_bol_person = " + subtype + " "
                        + "AND NOT b_del "
                        + "ORDER BY name, id_bol_person ";
                break;
            case SModConsts.LOGX_TP_VEH:
                /* Use of GUI parameters:
                 * params.getKey(): If is different of null will contain:
                 *      params.getKey()[0] = fk_src_spot
                 *      params.getKey()[1] = fk_des_spot
                 *      params.getKey()[2] = fk_car
                 */
                settings = new SGuiCatalogueSettings("Tipo vehículo", 1, 1, SLibConsts.DATA_TYPE_BOOL);
                sql = "SELECT tp.id_tp_veh AS " + SDbConsts.FIELD_ID + "1, CONCAT(tp.name, ' (', tp.code, ')', IF(r.b_con = 1, ' - CONSOLIDADO', '')) AS " + SDbConsts.FIELD_ITEM + ", "
                        + "r.id_rate AS " + SDbConsts.FIELD_FK + "1, r.b_con AS " + SDbConsts.FIELD_COMP + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.LOGU_TP_VEH) + " AS tp "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS src ON r.fk_src_spot = src.id_spot "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS des ON r.fk_src_spot = des.id_spot "
                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_RATE) + " AS r ON tp.id_tp_veh = r.fk_tp_veh_n "
                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS car ON r.fk_car_n = car.id_bp "
                        + "WHERE NOT r.b_del " + (params == null ? "" : "AND r.fk_src_spot = " + params.getKey()[0] + " AND r.fk_des_spot = " + params.getKey()[1] + " AND "
                        + "r.fk_car_n = " + params.getKey()[2]) + " "
                        + "GROUP BY tp.id_tp_veh, r.b_con "
                        + "ORDER BY tp.name, tp.id_tp_veh ";
                break;
            case SModConsts.LOGX_RATE:
                /* Use of GUI parameters:
                 * params.getKey(): If is different of null will contain:
                 *      params.getKey()[0] = fk_src_spot
                 *      params.getKey()[1] = fk_des_spot
                 *      params.getKey()[2] = fk_car_n
                 *      params.getKey()[3] = fk_tp_veh_n
                 */
                try {
                    settings = new SGuiCatalogueSettings("Tarifa", 1, 1, SLibConsts.DATA_TYPE_BOOL);
                    String [] wheres = 
                    { 
                        (params == null ? "" : "r.fk_car_n = " + params.getKey()[2] + " AND r.fk_tp_veh_n = " + params.getKey()[3] + " "),
                        (params == null ? "" : "r.fk_car_n = " + params.getKey()[2] + " AND r.fk_tp_veh_n IS NULL "),
                        (params == null ? "" : "r.fk_car_n IS NULL AND r.fk_tp_veh_n = " + params.getKey()[3] + " "),
                        (params == null ? "" : "r.fk_car_n IS NULL AND r.fk_tp_veh_n IS NULL ")
                    };
                    
                    for (String where : wheres) {
                        sql = "SELECT r.id_rate AS " + SDbConsts.FIELD_ID + "1, CONCAT(r.rate, ' ', cur.cur_key, ' ', IF(r.b_con = 1, '(CONS.)', '')) AS " + SDbConsts.FIELD_ITEM
                                + ", r.fk_cur AS " + SDbConsts.FIELD_FK + "1, r.b_con AS " + SDbConsts.FIELD_COMP + " "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_RATE) + " AS r "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS cur ON r.fk_cur = cur.id_cur "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS src ON r.fk_src_spot = src.id_spot "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS des ON r.fk_src_spot = des.id_spot "
                                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS car ON r.fk_car_n = car.id_bp "
                                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_TP_VEH) + " AS tp ON r.fk_tp_veh_n = tp.id_tp_veh "
                                + "WHERE NOT r.b_del "
                                + (params == null ? "" : " AND r.fk_src_spot = " + params.getKey()[0] + " AND r.fk_des_spot = " + params.getKey()[1] + " AND ")
                                + where + "GROUP BY r.id_rate, r.fk_cur "
                                + "ORDER BY r.rate ";
                        ResultSet resultSet =  miClient.getSession().getStatement().executeQuery(sql);
                        if (resultSet.next()) {
                            break;
                        }
                    }
                }
                catch (Exception e){
                    miClient.showMsgBoxError(e.getMessage());
                }
                break;
            case SModConsts.LOG_SERVICE:
                settings = new SGuiCatalogueSettings("Servicio logística", 1);
                sql = "SELECT id_service AS " + SDbConsts.FIELD_ID + "1, name AS "  + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY name, id_service ";
                break;
            case SModConsts.LOG_FREIGHT_CLASS:
                settings = new SGuiCatalogueSettings("Clase flete", 1);
                sql = "SELECT id_freight_class AS " + SDbConsts.FIELD_ID + "1, name AS "  + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY name, id_freight_class ";
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
            case SModConsts.LOG_INSURER:
                view = new SViewInsurer(miClient, "Aseguradoras");
                break;
            case SModConsts.LOG_VEH:
                view = new SViewVehicle(miClient, "Vehículos");
                break;
            case SModConsts.LOG_VEH_BP_CAR:
                view = new SViewVehicleCarrier(miClient, "Vehículos vs. transportistas");
                break;
            case SModConsts.LOG_TRAILER:
                view = new SViewTrailer(miClient, "Remolques");
                break;
            case SModConsts.LOG_TANK_CAR:
                view = new SViewTankCar(miClient, "Carrotanques");
                break;
            case SModConsts.LOG_BOL_PERSON:
                view = new SViewBolPerson(miClient, "Figuras transporte");
                break;
            case SModConsts.LOG_DRIVER_BP_CAR:
                view = new SViewDriverCarrier(miClient, "Choferes vs. transportistas");
                break;
            case SModConsts.LOG_RATE:
                view = new SViewRate(miClient, "Tarifas");
                break;
            case SModConsts.LOG_SERVICE:
                view = new SViewService(miClient, "Servicios logística");
                break;
            case SModConsts.LOG_FREIGHT_CLASS:
                view = new SViewFreightClass(miClient, "Clases flete");
                break;
            case SModConsts.LOG_FREIGHT_TARIFF:
                view = new SViewFreightTariff(miClient, "Tarifas fletes");
                break;
            case SModConsts.LOG_SHIP:
                switch (params.getType()) {
                    case SModConsts.VIEW_SC_DET:
                        title += "(detalle)";
                        break;
                    case SModConsts.VIEW_SC_SUM:
                        title += "";
                        break;
                    default:        
                }
                view = new SViewShipment(miClient, type, subtype, "Embarques " + title, params);
                break;
            case SModConsts.LOG_BOL:
                switch (subtype) {
                    case SDataConstantsSys.TRNS_TP_CFD_BOL:
                        view = new SViewBillOfLading(miClient, subtype, "Carta porte traslados");
                        break;
                    case SDataConstantsSys.TRNS_TP_CFD_INV:
                        view = new SViewBillOfLading(miClient, subtype, "Carta porte facturas");
                        break;
                    default:        
                }
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
                    default:        
                }
                switch (params.getType()) {
                    case SModConsts.VIEW_SC_DET:
                        title += "(detalle)";
                        break;
                    case SModConsts.VIEW_SC_SUM:
                        title += "";
                        break;
                    default:        
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
                    default:        
                }
                switch (params.getType()) {
                    case SModConsts.VIEW_SC_DET:
                        title += "(detalle)";
                        break;
                    case SModConsts.VIEW_SC_SUM:
                        title += "";
                        break;
                    default:        
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
                    default:        
                }
                switch (params.getType()) {
                    case SModConsts.VIEW_SC_DET:
                        title += "(detalle)";
                        break;
                    case SModConsts.VIEW_SC_SUM:
                        title += "";
                        break;
                    default:        
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
                    default:        
                }
                switch (subtype) {
                    case SModConsts.VIEW_ST_PEND:
                        view = new SViewShipmentDiog(miClient, type, subtype, "Docs inventarios x embarcar " + title, params);
                        break;
                    case SModConsts.VIEW_ST_DONE:
                        view = new SViewShipmentDiog(miClient, type, subtype, "Docs. inventarios embarcados " + title, params);
                        break;
                    default:        
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
                    default:        
                }
                switch (subtype) {
                    case SModConsts.VIEW_ST_PEND:
                        view = new SViewShipmentDelivery(miClient, type, subtype, "Embarques x entregar " + title, params);
                        break;
                    case SModConsts.VIEW_ST_DONE:
                        view = new SViewShipmentDelivery(miClient, type, subtype, "Embarques entregados " + title, params);
                        break;
                    default:        
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
                    default:        
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
                    default:        
                }
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
            case SModConsts.LOG_INSURER:
                if (moFormInsurer == null) moFormInsurer = new SFormInsurer(miClient, "Aseguradora");
                form = moFormInsurer;
                break;
            case SModConsts.LOG_VEH:
            case SModConsts.LOG_VEH_BP_CAR:
                if (moFormVehicle == null) moFormVehicle = new SFormVehicle(miClient, "Vehículo");
                form = moFormVehicle;
                break;
            case SModConsts.LOG_TRAILER:
                if (moFormTrailer == null) moFormTrailer = new SFormTrailer(miClient, "Remolque");
                form = moFormTrailer;
                break;
            case SModConsts.LOG_TANK_CAR:
                if (moFormTankCar == null) moFormTankCar = new SFormTankCar(miClient, "Carrotanque");
                form = moFormTankCar;
                break;
            case SModConsts.LOG_BOL_PERSON:
            case SModConsts.LOG_DRIVER_BP_CAR:
                if (moFormBolPerson == null) moFormBolPerson = new SFormBolPerson(miClient, "Figura de transporte");
                form = moFormBolPerson;
                break;
            case SModConsts.LOG_RATE:
                if (moFormRate == null) moFormRate = new SFormRate(miClient, "Tarifa");
                form = moFormRate;
                break;
            case SModConsts.LOG_SERVICE:
                if (moFormService == null) moFormService = new SFormService(miClient, "Servicio de logística");
                form = moFormService;
                break;
            case SModConsts.LOG_FREIGHT_CLASS:
                if (moFormFreightClass == null) moFormFreightClass = new SFormFreightClass(miClient, "Clase de flete");
                form = moFormFreightClass;
                break;
            case SModConsts.LOG_FREIGHT_TARIFF:
                if (moFormFreightTariff == null) moFormFreightTariff = new SFormFreightTariff(miClient, "Tarifa de flete");
                form = moFormFreightTariff;
                break;
            case SModConsts.LOG_SHIP:
                if (moFormShipment == null) moFormShipment = new SFormShipment(miClient, miClient_xxx, "Embarque");
                form = moFormShipment;
                break;
            case SModConsts.LOG_BOL:
                switch (subtype) {
                    case SDataConstantsSys.TRNS_TP_CFD_BOL:
                        if (moFormBillOfLadingTypeT == null) moFormBillOfLadingTypeT = new SFormBillOfLading(miClient, "Carta porte de traslados", subtype);
                        form = moFormBillOfLadingTypeT;
                        break;
                    case SDataConstantsSys.TRNS_TP_CFD_INV:
                        if (moFormBillOfLadingTypeI == null) moFormBillOfLadingTypeI = new SFormBillOfLading(miClient, "Carta porte para facturas", subtype);
                        form = moFormBillOfLadingTypeI;
                    default:        
                }
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
    public SGuiReport getReport(final int type, final int subtype, final SGuiParams params) {
        SGuiReport guiReport = null;

        switch (type) {
            case SModConsts.LOGR_SHIP:
                guiReport = new SGuiReport("reps/log_ship.jasper", "Reporte embarque");
                break;
            case SModConsts.LOG_RATE:
                guiReport = new SGuiReport("reps/log_rate.jasper", "Listado de tarifas");
            default:
        }

        return guiReport;
    }
    
    @Override
    public void afterRegistrySaved() {
        
        if (moLastRegistry instanceof SDbBillOfLading){}
        
        switch (moLastRegistry.getRegistryType()) {
            case SModConsts.LOG_BOL:
                try {
                    SCfdUtils.computeCfdiBol((SClientInterface) miClient, (SDbBillOfLading) moLastRegistry, 
                            ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdTypeXmlTypes().get(SDataConstantsSys.TRNS_TP_CFD_BOL),
                            ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdTypeXmlCompVer().get(SDataConstantsSys.TRNS_TP_CFD_BOL))
                    ;
                }
                catch (java.lang.Exception e) {
                    miClient.showMsgBoxError("Ha ocurrido una excepción al generar el CFD: " + e);
                }
                break;

            default:
            
        }
    }
}
