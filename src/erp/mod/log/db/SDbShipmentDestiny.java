/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.log.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDiog;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos
 */
public class SDbShipmentDestiny extends SDbRegistryUser  implements SGridRow {

    public static final int FIELD_DELIVERY = 1;
    public static final int FIELD_DATE_DELIVERY_REAL = 2;

    protected int mnPkShipmentId;
    protected int mnPkDestinyId;
    protected Date mtDateDelivery;
    protected Date mtDateDeliveryReal;
    protected String msAppointmentNumber;
    protected Date mtAppointmentTime;
    protected String msAppointmentPerson;
    protected boolean mbDelivery;
    protected int mnFkDocShipmentTypeId;
    protected int mnFkSpotTypeId;
    protected int mnFkSpotId;
    protected int mnFkCustomerId_n;
    protected int mnFkCustomerBranchId_n;
    protected int mnFkCustomerBranchAddressId_n;
    protected int mnFkWarehouseCompanyBranchId_n;
    protected int mnFkWarehouseEntityId_n;
    protected int mnFkUserDeliveryId;
    protected Date mtTsUserDelivery;

    protected ArrayList<SDbShipmentDestinyEntry> maShipmentDestinyEntries;

    protected String msXtaDocShipmentType;
    protected String msXtaSpotType;
    protected String msXtaSpot;
    protected String msXtaCustomer;
    protected String msXtaCustomerKey;
    protected String msXtaCustomerBranch;
    protected String msXtaCustomerBranchAddress;
    protected String msXtaWarehouseCompanyBranch;
    protected String msXtaWarehouseEntity;

    public SDbShipmentDestiny() {
        super(SModConsts.LOG_SHIP_DEST);
    }

    public void setPkShipmentId(int n) { mnPkShipmentId = n; }
    public void setPkDestinyId(int n) { mnPkDestinyId = n; }
    public void setDateDelivery(Date t) { mtDateDelivery = t; }
    public void setDateDeliveryReal(Date t) { mtDateDeliveryReal = t; }
    public void setAppointmentNumber(String s) { msAppointmentNumber = s; }
    public void setAppointmentTime(Date t) { mtAppointmentTime = t; }
    public void setAppointmentPerson(String s) { msAppointmentPerson = s; }
    public void setDelivery(boolean b) { mbDelivery = b; }
    public void setFkDocShipmentTypeId(int n) { mnFkDocShipmentTypeId = n; }
    public void setFkSpotTypeId(int n) { mnFkSpotTypeId = n; }
    public void setFkSpotId(int n) { mnFkSpotId = n; }
    public void setFkCustomerId_n(int n) { mnFkCustomerId_n = n; }
    public void setFkCustomerBranchId_n(int n) { mnFkCustomerBranchId_n = n; }
    public void setFkCustomerBranchAddressId_n(int n) { mnFkCustomerBranchAddressId_n = n; }
    public void setFkWarehouseCompanyBranchId_n(int n) { mnFkWarehouseCompanyBranchId_n = n; }
    public void setFkWarehouseEntityId_n(int n) { mnFkWarehouseEntityId_n = n; }
    public void setFkUserDeliveryId(int n) { mnFkUserDeliveryId = n; }
    public void setTsUserDelivery(Date t) { mtTsUserDelivery = t; }

    public int getPkShipmentId() { return mnPkShipmentId; }
    public int getPkDestinyId() { return mnPkDestinyId; }
    public Date getDateDelivery() { return mtDateDelivery; }
    public Date getDateDeliveryReal() { return mtDateDeliveryReal; }
    public String getAppointmentNumber() { return msAppointmentNumber; }
    public Date getAppointmentTime() { return mtAppointmentTime; }
    public String getAppointmentPerson() { return msAppointmentPerson; }
    public boolean isDelivery() { return mbDelivery; }
    public int getFkDocShipmentTypeId() { return mnFkDocShipmentTypeId; }
    public int getFkSpotTypeId() { return mnFkSpotTypeId; }
    public int getFkSpotId() { return mnFkSpotId; }
    public int getFkCustomerId_n() { return mnFkCustomerId_n; }
    public int getFkCustomerBranchId_n() { return mnFkCustomerBranchId_n; }
    public int getFkCustomerBranchAddressId_n() { return mnFkCustomerBranchAddressId_n; }
    public int getFkWarehouseCompanyBranchId_n() { return mnFkWarehouseCompanyBranchId_n; }
    public int getFkWarehouseEntityId_n() { return mnFkWarehouseEntityId_n; }
    public int getFkUserDeliveryId() { return mnFkUserDeliveryId; }
    public Date getTsUserDelivery() { return mtTsUserDelivery; }

    public void setXtaDocShipmentType(String s) { msXtaDocShipmentType = s; }
    public void setXtaSpotType(String s) { msXtaSpotType = s; }
    public void setXtaSpot(String s) { msXtaSpot = s; }
    public void setXtaCustomer(String s) { msXtaCustomer = s; }
    public void setXtaCustomerKey(String s) { msXtaCustomerKey = s; }
    public void setXtaCustomerBranch(String s) { msXtaCustomerBranch = s; }
    public void setXtaCustomerBranchAddress(String s) { msXtaCustomerBranchAddress = s; }
    public void setXtaWarehouseCompanyBranch(String s) { msXtaWarehouseCompanyBranch = s; }
    public void setXtaWarehouseEntity(String s) { msXtaWarehouseEntity = s; }

    public String getXtaDocShipmentType() { return msXtaDocShipmentType; }
    public String getXtaSpotType() { return msXtaSpotType; }
    public String getXtaSpot() { return msXtaSpot; }
    public String getXtaCustomer() { return msXtaCustomer; }
    public String getXtaCustomerKey() { return msXtaCustomerKey; }
    public String getXtaCustomerBranch() { return msXtaCustomerBranch; }
    public String getXtaCustomerBranchAddress() { return msXtaCustomerBranchAddress; }
    public String getXtaWarehouseCompanyBranch() { return msXtaWarehouseCompanyBranch; }
    public String getXtaWarehouseEntity() { return msXtaWarehouseEntity; }

    public void setShipmentDestinyEntries(ArrayList<SDbShipmentDestinyEntry> v) { maShipmentDestinyEntries = v; }

    public ArrayList<SDbShipmentDestinyEntry> getShipmentDestinyEntries() { return maShipmentDestinyEntries; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkShipmentId = pk[0];
        mnPkDestinyId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkShipmentId, mnPkDestinyId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkShipmentId = 0;
        mnPkDestinyId = 0;
        mtDateDelivery = null;
        mtDateDeliveryReal = null;
        msAppointmentNumber = "";
        mtAppointmentTime = null;
        msAppointmentPerson = "";
        mbDelivery = false;
        mnFkDocShipmentTypeId = 0;
        mnFkSpotTypeId = 0;
        mnFkSpotId = 0;
        mnFkCustomerId_n = 0;
        mnFkCustomerBranchId_n = 0;
        mnFkCustomerBranchAddressId_n = 0;
        mnFkWarehouseCompanyBranchId_n = 0;
        mnFkWarehouseEntityId_n = 0;
        mnFkUserDeliveryId = 0;
        mtTsUserDelivery = null;

        msXtaDocShipmentType = "";
        msXtaSpotType = "";
        msXtaSpot = "";
        msXtaCustomer = "";
        msXtaCustomerKey = "";
        msXtaCustomerBranch = "";
        msXtaCustomerBranchAddress = "";
        msXtaWarehouseCompanyBranch = "";
        msXtaWarehouseEntity = "";

        maShipmentDestinyEntries = new ArrayList<SDbShipmentDestinyEntry>();
    }

    public void computeDps(SGuiSession session, int[] anXtaPrimaryKeyDps) throws SQLException, Exception {
        SDbShipmentDestinyEntry entry = new SDbShipmentDestinyEntry();

        entry.computeDpsEntry(session, anXtaPrimaryKeyDps);
        for (SDbShipmentDestinyEntry destinyEntry : entry.getShipmentDestinyEntries()) {
            maShipmentDestinyEntries.add(destinyEntry);
        }
    }

    public void computeDiog(SGuiSession session, int[] anXtaPrimaryKeyDiog) throws SQLException, Exception {
        SDbShipmentDestinyEntry entry = new SDbShipmentDestinyEntry();

        entry.computeDiogEntry(session, anXtaPrimaryKeyDiog);
        for (SDbShipmentDestinyEntry destinyEntry : entry.getShipmentDestinyEntries()) {
            maShipmentDestinyEntries.add(destinyEntry);
        }
    }

    public SDataDiog[] readDiogSourceDestiny(SGuiSession session, int[] nPkDiogSourceId) {
        SDataDiog diogSource = new SDataDiog();
        SDataDiog diogDestiny = new SDataDiog();

        diogSource.read(nPkDiogSourceId, session.getStatement());
        if (diogSource != null) {

            diogDestiny.read(new int[] { diogSource.getFkDiogYearId_n(), diogSource.getFkDiogDocId_n() }, session.getStatement());
        }

        return new SDataDiog[] { diogSource, diogDestiny };
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_ship = " + mnPkShipmentId + " AND id_dest = " + mnPkDestinyId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ship = " + pk[0] + " AND id_dest = " + pk[1];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkDestinyId = 0;

        msSql = "SELECT COALESCE(MAX(id_dest), 0) + 1 FROM " + getSqlTable() + " WHERE id_ship = " + mnPkShipmentId;
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkDestinyId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;
        Statement statement = null;

        SDbShipmentDestinyEntry entry = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT sd.*, tp.code, tps.code, spot.code, bp.bp, bc.bp_key, bpb.bpb, cob.code, " +
                "CONCAT(a.bpb_add, IF(a.b_def, ' (P)', ''), ' - ', a.street, ' ', RTRIM(CONCAT(a.street_num_ext, ' ', a.street_num_int)), '; CP ', a.zip_code, '; ', a.locality, ', ', a.state) AS f_add " +
                "FROM " + getSqlTable() + " AS sd " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_DOC_SHIP) + " AS tp ON sd.fk_tp_doc_ship = tp.id_tp_doc_ship " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_SPOT) + " AS tps ON sd.fk_tp_spot = tps.id_tp_spot " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS spot ON sd.fk_spot = spot.id_spot " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON sd.fk_cus_n = bp.id_bp " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON sd.fk_cus_n = bc.id_bp AND bc.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_CUS + " " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpb ON sd.fk_cus_bpb_n = bpb.id_bpb " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB_ADD) + " AS a ON sd.fk_cus_bpb_n = a.id_bpb AND sd.fk_cus_add_n = a.id_add " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS cob ON sd.fk_wah_cob_n = cob.id_cob AND sd.fk_wah_ent_n = cob.id_ent " +
                "WHERE sd.id_ship = " + pk[0] + "  AND sd.id_dest = " + pk[1];

        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkShipmentId = resultSet.getInt("id_ship");
            mnPkDestinyId = resultSet.getInt("id_dest");
            mtDateDelivery = resultSet.getDate("dt_dly");
            mtDateDeliveryReal = resultSet.getDate("dt_dly_real");
            msAppointmentNumber = resultSet.getString("appt_num");
            mtAppointmentTime = resultSet.getTimestamp("appt_time");
            msAppointmentPerson = resultSet.getString("appt_per");
            mbDelivery = resultSet.getBoolean("b_dly");
            mnFkDocShipmentTypeId = resultSet.getInt("fk_tp_doc_ship");
            mnFkSpotTypeId = resultSet.getInt("fk_tp_spot");
            mnFkSpotId = resultSet.getInt("fk_spot");
            mnFkCustomerId_n = resultSet.getInt("fk_cus_n");
            if (resultSet.wasNull()) { mnFkCustomerId_n = 0; }
            mnFkCustomerBranchId_n = resultSet.getInt("fk_cus_bpb_n");
            if (resultSet.wasNull()) { mnFkCustomerBranchId_n = 0; }
            mnFkCustomerBranchAddressId_n = resultSet.getInt("fk_cus_add_n");
            if (resultSet.wasNull()) { mnFkCustomerBranchAddressId_n = 0; }
            mnFkWarehouseCompanyBranchId_n = resultSet.getInt("fk_wah_cob_n");
            if (resultSet.wasNull()) { mnFkWarehouseCompanyBranchId_n = 0; }
            mnFkWarehouseEntityId_n = resultSet.getInt("fk_wah_ent_n");
            if (resultSet.wasNull()) { mnFkWarehouseEntityId_n = 0; }
            mnFkUserDeliveryId = resultSet.getInt("fk_usr_dly");
            mtTsUserDelivery = resultSet.getTimestamp("ts_usr_dly");

            msXtaDocShipmentType = resultSet.getString("tp.code");
            msXtaSpotType = resultSet.getString("tps.code");
            msXtaSpot = resultSet.getString("spot.code");
            msXtaCustomer = resultSet.getString("bp.bp");
            msXtaCustomerKey = resultSet.getString("bc.bp_key");
            msXtaCustomerBranch = resultSet.getString("bpb.bpb");
            msXtaCustomerBranchAddress = resultSet.getString("f_add");
            msXtaWarehouseCompanyBranch = resultSet.getString("cob.code");

            mbRegistryNew = false;
        }

        // Read destiny entries:

        msSql = "SELECT id_ship, id_dest, id_ety FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " " +
                "WHERE id_ship = " + mnPkShipmentId + " AND id_dest = " + mnPkDestinyId;
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {

            entry = new SDbShipmentDestinyEntry();
            entry.read(session, new int[] { resultSet.getInt("id_ship"), resultSet.getInt("id_dest"), resultSet.getInt("id_ety") } );
            maShipmentDestinyEntries.add(entry);
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mnFkUserDeliveryId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkShipmentId + ", " +
                mnPkDestinyId + ", " +
                "'" + SLibUtils.DbmsDateFormatDate.format(mtDateDelivery) + "', " +
                "'" + SLibUtils.DbmsDateFormatDate.format(mtDateDeliveryReal) + "', " +
                "'" + msAppointmentNumber + "', " +
                "'" + SLibUtils.DbmsDateFormatDatetime.format(mtAppointmentTime) + "', " +
                "'" + msAppointmentPerson + "', " +
                (mbDelivery ? 1 : 0) + ", " +
                mnFkDocShipmentTypeId + ", " +
                mnFkSpotTypeId + ", " +
                mnFkSpotId + ", " +
                (mnFkCustomerId_n > 0 ? mnFkCustomerId_n : "NULL") + ", " +
                (mnFkCustomerBranchId_n > 0 ? mnFkCustomerBranchId_n : "NULL") + ", " +
                (mnFkCustomerBranchAddressId_n > 0 ? mnFkCustomerBranchAddressId_n : "NULL") + ", " +
                (mnFkWarehouseCompanyBranchId_n > 0 ? mnFkWarehouseCompanyBranchId_n : "NULL") + ", " +
                (mnFkWarehouseEntityId_n > 0 ? mnFkWarehouseEntityId_n : "NULL") + ", " +
                mnFkUserDeliveryId + ", " +
                "NOW()" + " " +
                ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                // "id_ship = " + mnPkShipmentId + ", " +
                // "id_dest = " + mnPkDestinyId + ", " +
                "dt_dly = '" + SLibUtils.DbmsDateFormatDate.format(mtDateDelivery) + "', " +
                "dt_dly_real = '" + SLibUtils.DbmsDateFormatDate.format(mtDateDeliveryReal) + "', " +
                "appt_num = '" + msAppointmentNumber + "', " +
                "appt_time = " + "NOW()" + ", " +
                "appt_per = '" + msAppointmentPerson + "', " +
                "b_dly = " + (mbDelivery ? 1 : 0) + ", " +
                "fk_tp_doc_ship = " + mnFkDocShipmentTypeId + ", " +
                "fk_tp_spot = " + mnFkSpotTypeId + ", " +
                "fk_spot = " + mnFkSpotId + ", " +
                "fk_cus_n = " + (mnFkCustomerId_n > 0 ? mnFkCustomerId_n : "NULL") + ", " +
                "fk_cus_bpb_n = " + (mnFkCustomerBranchId_n > 0 ? mnFkCustomerBranchId_n : "NULL") + ", " +
                "fk_cus_add_n = " + (mnFkCustomerBranchAddressId_n > 0 ? mnFkCustomerBranchAddressId_n : "NULL") + ", " +
                "fk_wah_cob_n = " + (mnFkWarehouseCompanyBranchId_n > 0 ? mnFkWarehouseCompanyBranchId_n : "NULL") + ", " +
                "fk_wah_ent_n = " + (mnFkWarehouseEntityId_n > 0 ? mnFkWarehouseEntityId_n : "NULL") + ", " +
                "fk_usr_dly = " + mnFkUserDeliveryId + ", " +
                "ts_usr_dly = " + "NOW()" + " " +

                getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save destiny entries:

        for (SDbShipmentDestinyEntry entry : maShipmentDestinyEntries) {
            entry.setPkShipmentId(mnPkShipmentId);
            entry.setPkDestinyId(mnPkDestinyId);
            entry.setPkEntryId(0);
            entry.setRegistryNew(true);
            entry.save(session);
        }

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbShipmentDestiny clone() throws CloneNotSupportedException {
        SDbShipmentDestiny registry = new SDbShipmentDestiny();

        registry.setPkShipmentId(this.getPkShipmentId());
        registry.setPkDestinyId(this.getPkDestinyId());
        registry.setDateDelivery(this.getDateDelivery());
        registry.setDateDeliveryReal(this.getDateDeliveryReal());
        registry.setAppointmentNumber(this.getAppointmentNumber());
        registry.setAppointmentTime(this.getAppointmentTime());
        registry.setAppointmentPerson(this.getAppointmentPerson());
        registry.setDelivery(this.isDelivery());
        registry.setFkDocShipmentTypeId(this.getFkDocShipmentTypeId());
        registry.setFkSpotTypeId(this.getFkSpotTypeId());
        registry.setFkSpotId(this.getFkSpotId());
        registry.setFkCustomerId_n(this.getFkCustomerId_n());
        registry.setFkCustomerBranchId_n(this.getFkCustomerBranchId_n());
        registry.setFkCustomerBranchAddressId_n(this.getFkCustomerBranchAddressId_n());
        registry.setFkWarehouseCompanyBranchId_n(this.getFkWarehouseCompanyBranchId_n());
        registry.setFkWarehouseEntityId_n(this.getFkWarehouseEntityId_n());
        registry.setFkUserDeliveryId(this.getFkUserDeliveryId());
        registry.setTsUserDelivery(this.getTsUserDelivery());

        registry.setShipmentDestinyEntries(this.getShipmentDestinyEntries());

        return registry;
    }

     @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_DELIVERY:
                msSql += "b_dly = " + (Boolean) value + " ";
                break;
            case FIELD_DATE_DELIVERY_REAL:
                msSql += "dt_dly_real = '" + SLibUtils.DbmsDateFormatDate.format((Date) value) + "' ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return isRowEdited();
    }

    @Override
    public void setRowEdited(final boolean edited) {
        setRowEdited(edited);
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = msXtaDocShipmentType;
                break;
            case 1:
                value = msXtaSpotType;
                break;
            case 2:
                value = msXtaSpot;
                break;
            case 3:
                value = msXtaCustomer;
                break;
            case 4:
                value = msXtaCustomerKey;
                break;
            case 5:
                value = msXtaCustomerBranch;
                break;
            case 6:
                value = msXtaCustomerBranchAddress;
                break;
            case 7:
                value = msXtaWarehouseCompanyBranch;
                break;
            case 8:
                value = msXtaWarehouseEntity;
                break;
            case 9:
                value = mtDateDelivery;
                break;
            case 10:
                value = mtDateDeliveryReal;
                break;
            case 11:
                value = msAppointmentNumber;
                break;
            case 12:
                value = mtAppointmentTime;
                break;
            case 13:
                value = msAppointmentPerson;
                break;
            case 14:
                value = mbDelivery;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch(row) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            default:
        }
    }
}
