/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.db;

import erp.mod.SModConsts;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbBolTransportationMode extends SDbRegistryUser implements Serializable {
    
    protected int mnPkBillOfLadingId;
    protected int mnPkTransportationModeId;
    protected String msTransportationPartOwner1;
    protected String msTransportationPartOwner2;
    protected String msTransportationPartLessor1;
    protected String msTransportationPartLessor2;
    protected int mnFkVehicleId;
    protected int mnFkDriverId;
    protected int mnFkTrailerId1_n;
    protected int mnFkTrailerId2_n;
    protected int mnFkVehicleOwnerId1_n;
    protected int mnFkVehicleOwnerId2_n;
    protected int mnFkVehicleLessorId1_n;
    protected int mnFkVehicleLessorId2_n;
    protected int mnFkNotifiedId_n;
    
    protected ArrayList<SDbBolTransportationModeExtra> maBolTransportationModeExtra;
    
    protected SDbVehicle moDataVehicle;
    protected SDbTrailer moDataTrailer1;
    protected SDbTrailer moDataTrailer2;
    protected SDbBolPerson moDataDriver;
    protected SDbBolPerson moDataOwner1;
    protected SDbBolPerson moDataOwner2;
    protected SDbBolPerson moDataLessor1;
    protected SDbBolPerson moDataLessor2;
    protected SDbBolPerson moDataNotified;

    public SDbBolTransportationMode() {
        super(SModConsts.LOG_BOL_TRANSP_MODE);
    }
    
    private void saveVehicleDriver(SGuiSession session) throws Exception {
        msSql = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_DRIVER_VEH) + " "
                + "WHERE id_driver = " + mnFkDriverId + " AND id_veh = " + mnFkVehicleId + ";";
        ResultSet resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            msSql = "INSERT INTO " + SModConsts.TablesMap.get(SModConsts.LOG_DRIVER_VEH) + " "
                    + "VALUES (" + mnFkDriverId + ", " + mnFkVehicleId + ");";
            session.getStatement().execute(msSql);
        }
    }
    
    public void setPkBillOfLadingId(int n) { mnPkBillOfLadingId = n; }
    public void setPkTransportationModeId(int n) { mnPkTransportationModeId = n; }
    public void setTransportationPartOwner1(String s) { msTransportationPartOwner1 = s; }
    public void setTransportationPartOwner2(String s) { msTransportationPartOwner2 = s; }
    public void setTransportationPartLessor1(String s) { msTransportationPartLessor1 = s; }
    public void setTransportationPartLessor2(String s) { msTransportationPartLessor2 = s; }
    public void setFkVehicleId(int n) { mnFkVehicleId = n; }
    public void setFkDriverId(int n) { mnFkDriverId = n; }
    public void setFkTrailerId1_n(int n) { mnFkTrailerId1_n = n; }
    public void setFkTrailerId2_n(int n) { mnFkTrailerId2_n = n; }
    public void setFkVehicleOwnerId1_n(int n) { mnFkVehicleOwnerId1_n = n; }
    public void setFkVehicleOwnerId2_n(int n) { mnFkVehicleOwnerId2_n = n; }
    public void setFkVehicleLessorId1_n(int n) { mnFkVehicleLessorId1_n = n; }
    public void setFkVehicleLessorId2_n(int n) { mnFkVehicleLessorId2_n = n; }
    public void setFkNotifiedId_n(int n) { mnFkNotifiedId_n = n; }

    public int getPkBillOfLadingId() { return mnPkBillOfLadingId; }
    public int getPkTransportationModeId() { return mnPkTransportationModeId; }
    public String getTransportationPartOwner1() { return msTransportationPartOwner1; }
    public String getTransportationPartOwner2() { return msTransportationPartOwner2; }
    public String getTransportationPartLessor1() { return msTransportationPartLessor1; }
    public String getTransportationPartLessor2() { return msTransportationPartLessor2; }
    public int getFkVehicleId() { return mnFkVehicleId; }
    public int getFkDriverId() { return mnFkDriverId; }
    public int getFkTrailerId1_n() { return mnFkTrailerId1_n; }
    public int getFkTrailerId2_n() { return mnFkTrailerId2_n; }
    public int getFkVehicleOwnerId1_n() { return mnFkVehicleOwnerId1_n; }
    public int getFkVehicleOwnerId2_n() { return mnFkVehicleOwnerId2_n; }
    public int getFkVehicleLessorId1_n() { return mnFkVehicleLessorId1_n; }
    public int getFkVehicleLessorId2_n() { return mnFkVehicleLessorId2_n; }
    public int getFkNotifiedId_n() { return mnFkNotifiedId_n; }
    
    public ArrayList<SDbBolTransportationModeExtra> getBolTransportationModeExtra() { return maBolTransportationModeExtra; }
    
    public void setDataVehicle(SDbVehicle o) { moDataVehicle = o; }
    public void setDataTrailer1(SDbTrailer o) { moDataTrailer1 = o; }
    public void setDataTrailer2(SDbTrailer o) { moDataTrailer2 = o; }
    public void setDataDriver(SDbBolPerson o) { moDataDriver = o; }
    public void setDataOwner1(SDbBolPerson o) { moDataOwner1 = o; }
    public void setDataOwner2(SDbBolPerson o) { moDataOwner2 = o; }
    public void setDataLessee1(SDbBolPerson o) { moDataLessor1 = o; }
    public void setDataLessee2(SDbBolPerson o) { moDataLessor2 = o; }
    public void setDataNotified(SDbBolPerson o) { moDataNotified = o; }
    
    public SDbVehicle getDataVehicle() { return moDataVehicle; }
    public SDbTrailer getDataTrailer1() { return moDataTrailer1; }
    public SDbTrailer getDataTrailer2() { return moDataTrailer2; }
    public SDbBolPerson getDataDriver() { return moDataDriver; }
    public SDbBolPerson getDataOwner1() { return moDataOwner1; }
    public SDbBolPerson getDataOwner2() { return moDataOwner2; }
    public SDbBolPerson getDataLessor1() { return moDataLessor1; }
    public SDbBolPerson getDataLessor2() { return moDataLessor2; }
    public SDbBolPerson getDataNotified() { return moDataNotified; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBillOfLadingId = pk[0];
        mnPkTransportationModeId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBillOfLadingId, mnPkTransportationModeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkBillOfLadingId = 0;
        mnPkTransportationModeId = 0;
        msTransportationPartOwner1 = "";
        msTransportationPartOwner2 = "";
        msTransportationPartLessor1 = "";
        msTransportationPartLessor2 = "";
        mnFkVehicleId = 0;
        mnFkDriverId = 0;
        mnFkTrailerId1_n = 0;
        mnFkTrailerId2_n = 0;
        mnFkVehicleOwnerId1_n = 0;
        mnFkVehicleOwnerId2_n = 0;
        mnFkVehicleLessorId1_n = 0;
        mnFkVehicleLessorId2_n = 0;
        mnFkNotifiedId_n = 0;
        
        maBolTransportationModeExtra = new ArrayList<>();
        
        moDataVehicle = null;
        moDataTrailer1 = null;
        moDataTrailer2 = null;
        moDataDriver = null;
        moDataOwner1 = null;
        moDataOwner2 = null;
        moDataLessor1 = null;
        moDataLessor2 = null;
        moDataNotified = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bol = " + mnPkBillOfLadingId + " AND id_transp_mode = " + mnPkTransportationModeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol = " + pk[0] + " AND id_transp_mode = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkTransportationModeId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_transp_mode), 0) + 1 FROM " + getSqlTable() + " WHERE id_bol = " + mnPkBillOfLadingId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkTransportationModeId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * FROM " + getSqlTable() + " WHERE id_bol = " + pk[0] +
                " AND id_transp_mode = " + pk[1];
        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkBillOfLadingId = resultSet.getInt("id_bol");
            mnPkTransportationModeId = resultSet.getInt("id_transp_mode");
            msTransportationPartOwner1 = resultSet.getString("transp_part_owner_1");
            msTransportationPartOwner2 = resultSet.getString("transp_part_owner_2");
            msTransportationPartLessor1 = resultSet.getString("transp_part_lessee_1");
            msTransportationPartLessor2 = resultSet.getString("transp_part_lessee_2");
            mnFkVehicleId = resultSet.getInt("fk_veh_n");
            mnFkDriverId = resultSet.getInt("fk_veh_driver_n");
            mnFkTrailerId1_n = resultSet.getInt("fk_veh_trailer_1_n");
            mnFkTrailerId2_n = resultSet.getInt("fk_veh_trailer_2_n");
            mnFkVehicleOwnerId1_n = resultSet.getInt("fk_transp_owner_1_n");
            mnFkVehicleOwnerId2_n = resultSet.getInt("fk_transp_owner_2_n");
            mnFkVehicleLessorId1_n = resultSet.getInt("fk_transp_lessee_1_n");
            mnFkVehicleLessorId2_n = resultSet.getInt("fk_transp_lessee_2_n");
            mnFkNotifiedId_n = resultSet.getInt("fk_notified_n");

            mbRegistryNew = false;
        }
        
        // Read transportation mode extra:
        
        msSql = "SELECT id_extra "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_TRANSP_MODE_EXTRA) + " "
                + "WHERE id_bol = " + mnPkBillOfLadingId + " AND id_transp_mode = " + mnPkTransportationModeId + " "
                + "ORDER BY id_extra ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            SDbBolTransportationModeExtra extra = new SDbBolTransportationModeExtra();
            extra.read(session, new int[] { mnPkBillOfLadingId, mnPkTransportationModeId, resultSet.getInt("id_extra") });
            maBolTransportationModeExtra.add(extra);
        }
        
        // Read Xtas:
        
        moDataVehicle = new SDbVehicle();
        moDataVehicle.read(session, new int[] { mnFkVehicleId });
        
        if (mnFkTrailerId1_n != 0) {
            moDataTrailer1 = new SDbTrailer();
            moDataTrailer1.read(session, new int[] { mnFkTrailerId1_n });
        }
        if (mnFkTrailerId2_n != 0) {
            moDataTrailer2 = new SDbTrailer();
            moDataTrailer2.read(session, new int[] { mnFkTrailerId2_n });
        }
        
        moDataDriver = new SDbBolPerson();
        moDataDriver.read(session, new int[] { mnFkDriverId });
        
        if (mnFkVehicleOwnerId1_n != 0) {
            moDataOwner1 = new SDbBolPerson();
            moDataOwner1.read(session, new int[] { mnFkVehicleOwnerId1_n });
        }
        if (mnFkVehicleOwnerId2_n != 0) {
            moDataOwner2 = new SDbBolPerson();
            moDataOwner2.read(session, new int[] { mnFkVehicleOwnerId2_n });
        }
        
        if (mnFkVehicleLessorId1_n != 0) {
            moDataLessor1 = new SDbBolPerson();
            moDataLessor1.read(session, new int[] { mnFkVehicleLessorId1_n });
        }
        if (mnFkVehicleLessorId2_n != 0) {
            moDataLessor2 = new SDbBolPerson();
            moDataLessor2.read(session, new int[] { mnFkVehicleLessorId2_n });
        }
        
        if (mnFkNotifiedId_n != 0) {
            moDataNotified = new SDbBolPerson();
            moDataNotified.read(session, new int[] { mnFkNotifiedId_n });
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
                mnPkBillOfLadingId + ", " + 
                mnPkTransportationModeId + ", " + 
                "'" + msTransportationPartOwner1 + "', " + 
                "'" + msTransportationPartOwner2 + "', " + 
                "'" + msTransportationPartLessor1 + "', " + 
                "'" + msTransportationPartLessor2 + "', " + 
                mnFkVehicleId + ", " + 
                mnFkDriverId + ", " + 
                (mnFkTrailerId1_n == 0 ? "NULL, " : + mnFkTrailerId1_n + ", ") + 
                (mnFkTrailerId2_n == 0 ? "NULL, " : + mnFkTrailerId2_n + ", " )+ 
                (mnFkVehicleOwnerId1_n == 0 ? "NULL, " : mnFkVehicleOwnerId1_n + ", ") + 
                (mnFkVehicleOwnerId2_n == 0 ? "NULL, " : mnFkVehicleOwnerId2_n + ", ") + 
                (mnFkVehicleLessorId1_n == 0 ? "NULL, " : mnFkVehicleLessorId1_n + ", ") + 
                (mnFkVehicleLessorId2_n == 0 ? "NULL, " : mnFkVehicleLessorId2_n + ", ") + 
                (mnFkNotifiedId_n == 0 ? "NULL " : mnFkNotifiedId_n + " ") +
                ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                //"id_bol = " + mnPkBillOfLadingId + ", " +
                //"id_transp_mode = " + mnPkTransportationModeId + ", " +
                "transp_part_owner_1 = '" + msTransportationPartOwner1 + "', " +
                "transp_part_owner_2 = '" + msTransportationPartOwner2 + "', " +
                "transp_part_lessee_1 = '" + msTransportationPartLessor1 + "', " +
                "transp_part_lessee_2 = '" + msTransportationPartLessor2 + "', " +
                "fk_veh_n = " + mnFkVehicleId + ", " +
                "fk_veh_driver_n = " + mnFkDriverId + ", " +
                "fk_veh_trailer_1_n = " + (mnFkTrailerId1_n == 0 ? "NULL, " : mnFkTrailerId1_n + ", ") +
                "fk_veh_trailer_2_n = " + (mnFkTrailerId2_n == 0 ? "NULL, " : mnFkTrailerId2_n + ", ") +
                "fk_transp_owner_1_n = " + (mnFkVehicleOwnerId1_n == 0 ? "NULL, " : mnFkVehicleOwnerId1_n + ", ") +
                "fk_transp_owner_2_n = " + (mnFkVehicleOwnerId2_n == 0 ? "NULL, " : mnFkVehicleOwnerId2_n + ", ") +
                "fk_transp_lessee_1_n = " + (mnFkVehicleLessorId1_n == 0 ? "NULL, " : mnFkVehicleLessorId1_n + ", ") +
                "fk_transp_lessee_2_n = " + (mnFkVehicleLessorId2_n == 0 ? "NULL, " : mnFkVehicleLessorId2_n + ", ") +
                "fk_notified_n = " + (mnFkNotifiedId_n == 0 ? "NULL " : mnFkNotifiedId_n + " ") +
                getSqlWhere();
        }
        session.getStatement().execute(msSql);
        
        // Save Transportation mode extra:
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_TRANSP_MODE_EXTRA) + 
                " WHERE id_bol = " + mnPkBillOfLadingId + " AND id_transp_mode = " + mnPkTransportationModeId + " ";
        session.getStatement().execute(msSql);
        
        for (SDbBolTransportationModeExtra extra : maBolTransportationModeExtra) {
            extra.setPkBillOfLadingId(mnPkBillOfLadingId);
            extra.setPkTransportationModeId(mnPkTransportationModeId);
            extra.setRegistryNew(true);
            extra.save(session);
        }
        
        // Save vehicle driver
        
        saveVehicleDriver(session);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbBolTransportationMode clone() throws CloneNotSupportedException {
        SDbBolTransportationMode registry = new SDbBolTransportationMode();
        
        registry.setPkBillOfLadingId(this.getPkBillOfLadingId());
        registry.setPkTransportationModeId(this.getPkTransportationModeId());
        registry.setTransportationPartOwner1(this.getTransportationPartOwner1());
        registry.setTransportationPartOwner2(this.getTransportationPartOwner2());
        registry.setTransportationPartLessor1(this.getTransportationPartLessor1());
        registry.setTransportationPartLessor2(this.getTransportationPartLessor2());
        registry.setFkVehicleId(this.getFkVehicleId());
        registry.setFkDriverId(this.getFkDriverId());
        registry.setFkTrailerId1_n(this.getFkTrailerId1_n());
        registry.setFkTrailerId2_n(this.getFkTrailerId2_n());
        registry.setFkVehicleOwnerId1_n(this.getFkVehicleOwnerId1_n());
        registry.setFkVehicleOwnerId2_n(this.getFkVehicleOwnerId2_n());
        registry.setFkVehicleLessorId1_n(this.getFkVehicleLessorId1_n());
        registry.setFkVehicleLessorId2_n(this.getFkVehicleLessorId2_n());
        registry.setFkNotifiedId_n(this.getFkNotifiedId_n());
        
        for (SDbBolTransportationModeExtra tme : this.getBolTransportationModeExtra()) {
            registry.getBolTransportationModeExtra().add(tme.clone());
        }
        
        registry.setDataVehicle(this.getDataVehicle()); // el clon comparte este registro que es de sólo lectura
        registry.setDataTrailer1(this.getDataTrailer1()); // el clon comparte este registro que es de sólo lectura
        registry.setDataTrailer2(this.getDataTrailer2()); // el clon comparte este registro que es de sólo lectura
        registry.setDataDriver(this.getDataDriver()); // el clon comparte este registro que es de sólo lectura
        registry.setDataOwner1(this.getDataOwner1()); // el clon comparte este registro que es de sólo lectura
        registry.setDataOwner2(this.getDataOwner2()); // el clon comparte este registro que es de sólo lectura
        registry.setDataLessee1(this.getDataLessor1()); // el clon comparte este registro que es de sólo lectura
        registry.setDataLessee2(this.getDataLessor2()); // el clon comparte este registro que es de sólo lectura
        registry.setDataNotified(this.getDataNotified()); // el clon comparte este registro que es de sólo lectura

        return registry;
    }
    
}
