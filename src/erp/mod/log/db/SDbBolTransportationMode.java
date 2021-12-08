/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbBolTransportationMode extends SDbRegistryUser {
    
    protected int mnPkBolId;
    protected int mnPkTransportationMode;
    protected String msTransportationPartOwner;
    protected String msTransportationPartLessee;
    protected int mnFkVehicleId;
    protected int mnFkDriverId;
    protected int mnFkTrailerId_1_n;
    protected int mnFkTrailerId_2_n;
    protected int mnFkVehicleOwnerId_n;
    protected int mnFkVehicleLesseeId_n;
    protected int mnFkNotifiedId_n;
    
    protected SDbVehicle moXtaVehicle;
    protected SDbTrailer moXtaTrailer1;
    protected SDbTrailer moXtaTrailer2;
    protected SDbBolPerson moXtaDriver;
    protected SDbBolPerson moXtaOwner;
    protected SDbBolPerson moXtaLessee;
    protected SDbBolPerson moXtaNotified;

    public SDbBolTransportationMode() {
        super(SModConsts.LOG_BOL_TRANSP_MODE);
    }
    
    private void saveVehicleDriver(SGuiSession session) throws Exception {
        msSql = "SELECT * FROM " + SModConsts.TablesMap.get(SModConsts.LOG_DRIVER_VEH) + " " +
                "WHERE id_driver = " + mnFkDriverId + " AND id_veh = " + mnFkVehicleId;
        ResultSet resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            msSql = "INSERT INTO " + SModConsts.TablesMap.get(SModConsts.LOG_DRIVER_VEH) + " VALUES (" +
                    mnFkDriverId + ", " + 
                    mnFkVehicleId + ")";
            session.getStatement().execute(msSql);
        }
    }
    
    public void setPkBolId(int n) { mnPkBolId = n; }
    public void setPkTransportationMode(int n) { mnPkTransportationMode = n; }
    public void setTransportationPartOwner(String s) { msTransportationPartOwner = s; }
    public void setTransportationPartLessee(String s) { msTransportationPartLessee = s; }
    public void setFkVehicleId(int n) { mnFkVehicleId = n; }
    public void setFkDriverId(int n) { mnFkDriverId = n; }
    public void setFkTrailerId_1_n(int n) { mnFkTrailerId_1_n = n; }
    public void setFkTrailerId_2_n(int n) { mnFkTrailerId_2_n = n; }
    public void setFkVehicleOwnerId_n(int n) { mnFkVehicleOwnerId_n = n; }
    public void setFkVehicleLesseeId_n(int n) { mnFkVehicleLesseeId_n = n; }
    public void setFkNotifiedId_n(int n) { mnFkNotifiedId_n = n; }

    public int getPkBolId() { return mnPkBolId; }
    public int getPkTransportationMode() { return mnPkTransportationMode; }
    public String getTransportationPartOwner() { return msTransportationPartOwner; }
    public String getTransportationPartLessee() { return msTransportationPartLessee; }
    public int getFkVehicleId() { return mnFkVehicleId; }
    public int getFkDriverId() { return mnFkDriverId; }
    public int getFkTrailerId_1_n() { return mnFkTrailerId_1_n; }
    public int getFkTrailerId_2_n() { return mnFkTrailerId_2_n; }
    public int getFkVehicleOwnerId_n() { return mnFkVehicleOwnerId_n; }
    public int getFkVehicleLesseeId_n() { return mnFkVehicleLesseeId_n; }
    public int getFkNotifiedId_n() { return mnFkNotifiedId_n; }
    
    public void setXtaVehicle(SDbVehicle o) { moXtaVehicle = o; }
    public void setXtaTrailer1(SDbTrailer o) { moXtaTrailer1 = o; }
    public void setXtaTrailer2(SDbTrailer o) { moXtaTrailer2 = o; }
    public void setXtaDriver(SDbBolPerson o) { moXtaDriver = o; }
    public void setXtaOwner(SDbBolPerson o) { moXtaOwner = o; }
    public void setXtaLessee(SDbBolPerson o) { moXtaLessee = o; }
    public void setXtaNotified(SDbBolPerson o) { moXtaNotified = o; }
    
    public SDbVehicle getXtaVehicle() { return moXtaVehicle; }
    public SDbTrailer getXtaTrailer1() { return moXtaTrailer1; }
    public SDbTrailer getXtaTrailer2() { return moXtaTrailer2; }
    public SDbBolPerson getXtaDriver() { return moXtaDriver; }
    public SDbBolPerson getXtaOwner() { return moXtaOwner; }
    public SDbBolPerson getXtaLessee() { return moXtaLessee; }
    public SDbBolPerson getXtaNotified() { return moXtaNotified; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBolId = pk[0];
        mnPkTransportationMode = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBolId, mnPkTransportationMode };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkBolId = 0;
        mnPkTransportationMode = 0;
        msTransportationPartOwner = "";
        msTransportationPartLessee = "";
        mnFkVehicleId = 0;
        mnFkDriverId = 0;
        mnFkTrailerId_1_n = 0;
        mnFkTrailerId_2_n = 0;
        mnFkVehicleOwnerId_n = 0;
        mnFkVehicleLesseeId_n = 0;
        mnFkNotifiedId_n = 0;
        
        moXtaVehicle = new SDbVehicle();
        moXtaTrailer1 = null;
        moXtaTrailer2 = null;
        moXtaDriver = new SDbBolPerson();
        moXtaOwner = null;
        moXtaLessee = null;
        moXtaNotified = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bol = " + mnPkBolId + " AND id_transp_mode = " + mnPkTransportationMode + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol = " + pk[0] + " AND id_transp_mode = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkTransportationMode = 0;
        
        msSql = "SELECT COALESCE(MAX(id_transp_mode), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkTransportationMode = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * FROM " + getSqlTable() + " WHERE id_bol = " + pk[0] +
                " AND id_transp_mode = " + pk[1];
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkBolId = resultSet.getInt("id_bol");
            mnPkTransportationMode = resultSet.getInt("id_transp_mode");
            msTransportationPartOwner = resultSet.getString("transp_part_owner");
            msTransportationPartLessee = resultSet.getString("transp_part_lessee");
            mnFkVehicleId = resultSet.getInt("fk_veh_n");
            mnFkDriverId = resultSet.getInt("fk_veh_driver_n");
            mnFkTrailerId_1_n = resultSet.getInt("fk_veh_trailer_1_n");
            mnFkTrailerId_2_n = resultSet.getInt("fk_veh_trailer_2_n");
            mnFkVehicleOwnerId_n = resultSet.getInt("fk_transp_owner_n");
            mnFkVehicleLesseeId_n = resultSet.getInt("fk_transp_lessee_n");
            mnFkNotifiedId_n = resultSet.getInt("fk_notified_n");

            mbRegistryNew = false;
        }
        
        // Read Xtas:
        
        moXtaVehicle.read(session, new int[] { mnFkVehicleId });
        
        if (mnFkTrailerId_1_n != 0) {
            moXtaTrailer1 = new SDbTrailer();
            moXtaTrailer1.read(session, new int[] { mnFkTrailerId_1_n });
        }
        if (mnFkTrailerId_2_n != 0) {
            moXtaTrailer2 = new SDbTrailer();
            moXtaTrailer2.read(session, new int[] { mnFkTrailerId_2_n });
        }
        moXtaDriver.read(session, new int[] { mnFkDriverId });
        if (mnFkVehicleOwnerId_n != 0) {
            moXtaOwner = new SDbBolPerson();
            moXtaOwner.read(session, new int[] { mnFkVehicleOwnerId_n });
        }
        if (mnFkVehicleLesseeId_n != 0) {
            moXtaLessee = new SDbBolPerson();
            moXtaLessee.read(session, new int[] { mnFkVehicleLesseeId_n });
        }
        if (mnFkNotifiedId_n != 0) {
            moXtaNotified = new SDbBolPerson();
            moXtaNotified.read(session, new int[] { mnFkNotifiedId_n });
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
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" + 
                mnPkBolId + ", " + 
                mnPkTransportationMode + ", " + 
                "'" + msTransportationPartOwner + "', " + 
                "'" + msTransportationPartLessee + "', " + 
                mnFkVehicleId + ", " + 
                mnFkDriverId + ", " + 
                (mnFkTrailerId_1_n == 0 ? "NULL, " : + mnFkTrailerId_1_n + ", ") + 
                (mnFkTrailerId_2_n == 0 ? "NULL, " : + mnFkTrailerId_2_n + ", " )+ 
                (mnFkVehicleOwnerId_n == 0 ? "NULL, " : mnFkVehicleOwnerId_n + ", ") + 
                (mnFkVehicleLesseeId_n == 0 ? "NULL, " : mnFkVehicleLesseeId_n + ", ") + 
                (mnFkNotifiedId_n == 0 ? "NULL " : mnFkNotifiedId_n + " ") +
                ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                //"id_bol = " + mnPkBolId + ", " +
                //"id_transp_mode = " + mnPkTransportationMode + ", " +
                "transp_part_owner = '" + msTransportationPartOwner + "', " +
                "transp_part_lessee = '" + msTransportationPartLessee + "', " +
                "fk_veh_n = " + mnFkVehicleId + ", " +
                "fk_veh_driver_n = " + mnFkDriverId + ", " +
                "fk_veh_trailer_1_n = " + (mnFkTrailerId_1_n == 0 ? "NULL, " : mnFkTrailerId_1_n + ", ") +
                "fk_veh_trailer_2_n = " + (mnFkTrailerId_2_n == 0 ? "NULL, " : mnFkTrailerId_2_n + ", ") +
                "fk_transp_owner_n = " + (mnFkVehicleOwnerId_n == 0 ? "NULL, " : mnFkVehicleOwnerId_n + ", ") +
                "fk_transp_lessee_n = " + (mnFkVehicleLesseeId_n == 0 ? "NULL, " : mnFkVehicleLesseeId_n + ", ") +
                "fk_notified_n = " + (mnFkNotifiedId_n == 0 ? "NULL " : mnFkNotifiedId_n + " ") +
                getSqlWhere();
        }
        session.getStatement().execute(msSql);
        
        // Save vehicle driver
        
        saveVehicleDriver(session);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbBolTransportationMode registry = new SDbBolTransportationMode();
        
        registry.setPkBolId(this.getPkBolId());
        registry.setPkTransportationMode(this.getPkTransportationMode());
        registry.setTransportationPartOwner(this.getTransportationPartOwner());
        registry.setTransportationPartLessee(this.getTransportationPartLessee());
        registry.setFkVehicleId(this.getFkVehicleId());
        registry.setFkDriverId(this.getFkDriverId());
        registry.setFkTrailerId_1_n(this.getFkTrailerId_1_n());
        registry.setFkTrailerId_2_n(this.getFkTrailerId_2_n());
        registry.setFkVehicleOwnerId_n(this.getFkVehicleOwnerId_n());
        registry.setFkVehicleLesseeId_n(this.getFkVehicleLesseeId_n());
        registry.setFkNotifiedId_n(this.getFkNotifiedId_n());
        
        registry.setXtaVehicle(this.getXtaVehicle());
        registry.setXtaTrailer1(this.getXtaTrailer1());
        registry.setXtaTrailer2(this.getXtaTrailer2());
        registry.setXtaDriver(this.getXtaDriver());
        registry.setXtaOwner(this.getXtaOwner());
        registry.setXtaLessee(this.getXtaLessee());
        registry.setXtaNotified(this.getXtaNotified());

        return registry;
    }
    
}
