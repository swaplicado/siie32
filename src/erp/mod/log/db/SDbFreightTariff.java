/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.log.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbFreightTariff extends SDbRegistryUser {
    
    public static final int ID_LEN = 9;
    
    public static final String TYPE_BASE = "B";
    public static final String TYPE_CARRIER = "C";
    public static final String TYPE_VEHICLE = "V";
    
    public static final HashMap<String, String> Types = new HashMap<>();
    
    static {
        Types.put(TYPE_BASE, "Base");
        Types.put(TYPE_CARRIER, "Transportista");
        Types.put(TYPE_VEHICLE, "Vehículo");
    }
    
    public static final DecimalFormat CodeFormat = new DecimalFormat(SLibUtils.textRepeat("0", ID_LEN));

    protected int mnPkFreightTariffId;
    protected String msTariffType;
    protected double mdTariff;
    protected Date mtStart;
    protected boolean mbByBase;
    //protected boolean mbDeleted;
    protected int mnFkServiceId;
    protected int mnFkDeliveryTypeId;
    protected int mnFkSourceSpotId;
    protected int mnFkDestinySpotId_n;
    protected int mnFkModeOfTransportTypeId;
    protected int mnFkShippingSizeTypeId;
    protected int mnFkVehicleTypeId_n;
    protected int mnFkFreightClassId;
    protected int mnFkCurrencyId;
    protected int mnFkCarrierId_n;
    protected int mnFkVehicleId_n;
    protected int mnFkBaseTariffId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbFreightTariff() {
        super(SModConsts.LOG_FREIGHT_TARIFF);
    }

    public void setPkFreightTariffId(int n) { mnPkFreightTariffId = n; }
    public void setTariffType(String s) { msTariffType = s; }
    public void setTariff(double d) { mdTariff = d; }
    public void setStart(Date t) { mtStart = t; }
    public void setByBase(boolean b) { mbByBase = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkServiceId(int n) { mnFkServiceId = n; }
    public void setFkDeliveryTypeId(int n) { mnFkDeliveryTypeId = n; }
    public void setFkSourceSpotId(int n) { mnFkSourceSpotId = n; }
    public void setFkDestinySpotId_n(int n) { mnFkDestinySpotId_n = n; }
    public void setFkModeOfTransportTypeId(int n) { mnFkModeOfTransportTypeId = n; }
    public void setFkShippingSizeTypeId(int n) { mnFkShippingSizeTypeId = n; }
    public void setFkVehicleTypeId_n(int n) { mnFkVehicleTypeId_n = n; }
    public void setFkFreightClassId(int n) { mnFkFreightClassId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkCarrierId_n(int n) { mnFkCarrierId_n = n; }
    public void setFkVehicleId_n(int n) { mnFkVehicleId_n = n; }
    public void setFkBaseTariffId_n(int n) { mnFkBaseTariffId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkFreightTariffId() { return mnPkFreightTariffId; }
    public String getTariffType() { return msTariffType; }
    public double getTariff() { return mdTariff; }
    public Date getStart() { return mtStart; }
    public boolean isByBase() { return mbByBase; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkServiceId() { return mnFkServiceId; }
    public int getFkDeliveryTypeId() { return mnFkDeliveryTypeId; }
    public int getFkSourceSpotId() { return mnFkSourceSpotId; }
    public int getFkDestinySpotId_n() { return mnFkDestinySpotId_n; }
    public int getFkModeOfTransportTypeId() { return mnFkModeOfTransportTypeId; }
    public int getFkShippingSizeTypeId() { return mnFkShippingSizeTypeId; }
    public int getFkVehicleTypeId_n() { return mnFkVehicleTypeId_n; }
    public int getFkFreightClassId() { return mnFkFreightClassId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkCarrierId_n() { return mnFkCarrierId_n; }
    public int getFkVehicleId_n() { return mnFkVehicleId_n; }
    public int getFkBaseTariffId_n() { return mnFkBaseTariffId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    @Override
    public String getCode() {
        return CodeFormat.format(mnPkFreightTariffId);
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkFreightTariffId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkFreightTariffId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkFreightTariffId = 0;
        msTariffType = "";
        mdTariff = 0;
        mtStart = null;
        mbByBase = false;
        mbDeleted = false;
        mnFkServiceId = 0;
        mnFkDeliveryTypeId = 0;
        mnFkSourceSpotId = 0;
        mnFkDestinySpotId_n = 0;
        mnFkModeOfTransportTypeId = 0;
        mnFkShippingSizeTypeId = 0;
        mnFkVehicleTypeId_n = 0;
        mnFkFreightClassId = 0;
        mnFkCurrencyId = 0;
        mnFkCarrierId_n = 0;
        mnFkVehicleId_n = 0;
        mnFkBaseTariffId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_freight_tariff = " + mnPkFreightTariffId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_freight_tariff = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkFreightTariffId = 0;

        msSql = "SELECT COALESCE(MAX(id_freight_tariff), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkFreightTariffId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkFreightTariffId = resultSet.getInt("id_freight_tariff");
            msTariffType = resultSet.getString("tariff_tp");
            mdTariff = resultSet.getDouble("tariff");
            mtStart = resultSet.getDate("start");
            mbByBase = resultSet.getBoolean("b_by_base");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkServiceId = resultSet.getInt("fk_service");
            mnFkDeliveryTypeId = resultSet.getInt("fk_tp_dly");
            mnFkSourceSpotId = resultSet.getInt("fk_src_spot");
            mnFkDestinySpotId_n = resultSet.getInt("fk_des_spot_n");
            mnFkModeOfTransportTypeId = resultSet.getInt("fk_tp_mot");
            mnFkShippingSizeTypeId = resultSet.getInt("fk_tp_ship_size");
            mnFkVehicleTypeId_n = resultSet.getInt("fk_tp_veh_n");
            mnFkFreightClassId = resultSet.getInt("fk_freight_class");
            mnFkCurrencyId = resultSet.getInt("fk_cur");
            mnFkCarrierId_n = resultSet.getInt("fk_car_n");
            mnFkVehicleId_n = resultSet.getInt("fk_veh_n");
            mnFkBaseTariffId_n = resultSet.getInt("fk_base_tariff_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkFreightTariffId + ", " + 
                    "'" + msTariffType + "', " + 
                    mdTariff + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "', " + 
                    (mbByBase ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkServiceId + ", " + 
                    mnFkDeliveryTypeId + ", " + 
                    mnFkSourceSpotId + ", " + 
                    (mnFkDestinySpotId_n == 0 ? "NULL" : mnFkDestinySpotId_n) + ", " + 
                    mnFkModeOfTransportTypeId + ", " + 
                    mnFkShippingSizeTypeId + ", " + 
                    (mnFkVehicleTypeId_n == 0 ? "NULL" : mnFkVehicleTypeId_n) + ", " + 
                    mnFkFreightClassId + ", " + 
                    mnFkCurrencyId + ", " + 
                    (mnFkCarrierId_n == 0 ? "NULL" : mnFkCarrierId_n) + ", " + 
                    (mnFkVehicleId_n == 0 ? "NULL" : mnFkVehicleId_n) + ", " + 
                    (mnFkBaseTariffId_n == 0 ? "NULL" : mnFkBaseTariffId_n) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_freight_tariff = " + mnPkFreightTariffId + ", " +
                    "tariff_tp = '" + msTariffType + "', " +
                    "tariff = " + mdTariff + ", " +
                    "start = '" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "', " +
                    "b_by_base = " + (mbByBase ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_service = " + mnFkServiceId + ", " +
                    "fk_tp_dly = " + mnFkDeliveryTypeId + ", " +
                    "fk_src_spot = " + mnFkSourceSpotId + ", " +
                    "fk_des_spot_n = " + (mnFkDestinySpotId_n == 0 ? "NULL" : mnFkDestinySpotId_n) + ", " +
                    "fk_tp_mot = " + mnFkModeOfTransportTypeId + ", " +
                    "fk_tp_ship_size = " + mnFkShippingSizeTypeId + ", " +
                    "fk_tp_veh_n = " + (mnFkVehicleTypeId_n == 0 ? "NULL" : mnFkVehicleTypeId_n) + ", " +
                    "fk_freight_class = " + mnFkFreightClassId + ", " +
                    "fk_cur = " + mnFkCurrencyId + ", " +
                    "fk_car_n = " + (mnFkCarrierId_n == 0 ? "NULL" : mnFkCarrierId_n) + ", " +
                    "fk_veh_n = " + (mnFkVehicleId_n == 0 ? "NULL" : mnFkVehicleId_n) + ", " +
                    "fk_base_tariff_n = " + (mnFkBaseTariffId_n == 0 ? "NULL" : mnFkBaseTariffId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        // delete obsolete tariffs:

        SDbFreightTariff[] obsoleteTariffs = SDbFreightTariff.findTariffs(session,
                mnPkFreightTariffId, msTariffType,
                mnFkServiceId, mnFkDeliveryTypeId,
                mnFkSourceSpotId, mnFkDestinySpotId_n,
                mnFkModeOfTransportTypeId, mnFkShippingSizeTypeId,
                mnFkVehicleTypeId_n, mnFkFreightClassId,
                mnFkCarrierId_n, mnFkVehicleId_n);

        for (SDbFreightTariff tariff : obsoleteTariffs) {
            tariff.delete(session);
        }
        
        // complete saving:

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbFreightTariff clone() throws CloneNotSupportedException {
        SDbFreightTariff registry = new SDbFreightTariff();

        registry.setPkFreightTariffId(this.getPkFreightTariffId());
        registry.setTariffType(this.getTariffType());
        registry.setTariff(this.getTariff());
        registry.setStart(this.getStart());
        registry.setByBase(this.isByBase());
        registry.setDeleted(this.isDeleted());
        registry.setFkServiceId(this.getFkServiceId());
        registry.setFkDeliveryTypeId(this.getFkDeliveryTypeId());
        registry.setFkSourceSpotId(this.getFkSourceSpotId());
        registry.setFkDestinySpotId_n(this.getFkDestinySpotId_n());
        registry.setFkModeOfTransportTypeId(this.getFkModeOfTransportTypeId());
        registry.setFkShippingSizeTypeId(this.getFkShippingSizeTypeId());
        registry.setFkVehicleTypeId_n(this.getFkVehicleTypeId_n());
        registry.setFkFreightClassId(this.getFkFreightClassId());
        registry.setFkCurrencyId(this.getFkCurrencyId());
        registry.setFkCarrierId_n(this.getFkCarrierId_n());
        registry.setFkVehicleId_n(this.getFkVehicleId_n());
        registry.setFkBaseTariffId_n(this.getFkBaseTariffId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    /**
     * Find all active tariffs.
     * @param session
     * @param excludeId
     * @param tariffType
     * @param service
     * @param deliveryType
     * @param sourceSpot
     * @param destinySpot Can be zero.
     * @param modeOfTransportType
     * @param shippingSizeType
     * @param vehicleType Can be zero.
     * @param freightClass
     * @param carrier Can be zero.
     * @param vehicle Can be zero.
     * @return
     * @throws Exception 
     */
    public static SDbFreightTariff[] findTariffs(final SGuiSession session,
            final int excludeId, final String tariffType,
            final int service, final int deliveryType,
            final int sourceSpot, final int destinySpot,
            final int modeOfTransportType, final int shippingSizeType,
            final int vehicleType, final int freightClass,
            final int carrier, final int vehicle) throws Exception {
        ArrayList<SDbFreightTariff> tariffs = new ArrayList<>();
        
        String sql = "SELECT id_freight_tariff "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_FREIGHT_TARIFF) + " "
                + "WHERE id_freight_tariff <> " + excludeId + " AND NOT b_del AND tariff_tp = '" + tariffType + "' "
                + "AND fk_service = " + service + " AND fk_tp_dly = " + deliveryType + " "
                + "AND fk_src_spot = " + sourceSpot + " AND fk_des_spot_n " + (destinySpot == 0 ? "IS NULL" : "= " + destinySpot) + " "
                + "AND fk_tp_mot = " + modeOfTransportType + " AND fk_tp_ship_size = " + shippingSizeType + " "
                + "AND fk_tp_veh_n " + (vehicleType == 0 ? "IS NULL" : "= " + vehicleType) + " AND fk_freight_class = " + freightClass + " "
                + "AND fk_car_n " + (carrier == 0 ? "IS NULL" : "= " + carrier) + " AND fk_veh_n " + (vehicle == 0 ? "IS NULL" : "= " + vehicle) + " "
                + "ORDER BY start DESC, id_freight_tariff DESC "
                + "LIMIT 1;";
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                tariffs.add((SDbFreightTariff) session.readRegistry(SModConsts.LOG_FREIGHT_TARIFF, new int[] { resultSet.getInt(1) }));
            }
        }
        
        return tariffs.toArray(new SDbFreightTariff[0]);
    }
    
    /**
     * Lookup fot the most recent base tariff.
     * @param session
     * @param excludeId
     * @param service
     * @param deliveryType
     * @param sourceSpot
     * @param destinySpot Can be zero.
     * @param modeOfTransportType
     * @param shippingSizeType
     * @param vehicleType Can be zero.
     * @param freightClass
     * @return
     * @throws Exception 
     */
    public static SDbFreightTariff lookupBaseTariff(final SGuiSession session,
            final int excludeId,
            final int service, final int deliveryType,
            final int sourceSpot, final int destinySpot,
            final int modeOfTransportType, final int shippingSizeType,
            final int vehicleType, final int freightClass) throws Exception {
        SDbFreightTariff tariff = null;
        
        String sql = "SELECT id_freight_tariff "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_FREIGHT_TARIFF) + " "
                + "WHERE id_freight_tariff <> " + excludeId + " AND NOT b_del AND tariff_tp = '" + TYPE_BASE + "' "
                + "AND fk_service = " + service + " AND fk_tp_dly = " + deliveryType + " "
                + "AND fk_src_spot = " + sourceSpot + " AND fk_des_spot_n " + (destinySpot == 0 ? "IS NULL" : "= " + destinySpot) + " "
                + "AND fk_tp_mot = " + modeOfTransportType + " AND fk_tp_ship_size = " + shippingSizeType + " "
                + "AND fk_tp_veh_n " + (vehicleType == 0 ? "IS NULL" : "= " + vehicleType) + " AND fk_freight_class = " + freightClass + " "
                + "ORDER BY start DESC, id_freight_tariff DESC "
                + "LIMIT 1;";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                tariff = (SDbFreightTariff) session.readRegistry(SModConsts.LOG_FREIGHT_TARIFF, new int[] { resultSet.getInt(1) });
            }
        }
        
        return tariff;
    }
}
