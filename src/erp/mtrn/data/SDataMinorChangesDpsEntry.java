package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;

/**
 * Cambios menores para partidas de documentos que ya no son modificables.
 * @author Adrián Avilés, Sergio Flores, Isabel Servín
 */
public class SDataMinorChangesDpsEntry extends erp.lib.data.SDataRegistry {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected java.lang.String msSealQuality;
    protected java.lang.String msSealSecurity;
    protected java.lang.String msDriver;
    protected java.lang.String msPlate;
    protected java.lang.String msTicket;
    protected java.lang.String msContainerTank;
    protected java.lang.String msVgm;
    protected Double mdAcidityPercentage_n;
    protected int mnFkVehicleTypeId_n;
    protected int mnFkUserEditId;
    
    protected java.util.Vector<erp.mtrn.data.SDataDpsEntryNotes> mvDbmsEntryNotes;
    
    public SDataMinorChangesDpsEntry() {
        super(SDataConstants.TRN_DPS_ETY);
        mvDbmsEntryNotes = new Vector<>();
        reset();
    }
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setSealQuality(java.lang.String s) { msSealQuality = s; }
    public void setSealSecurity(java.lang.String s) { msSealSecurity = s; }
    public void setDriver(java.lang.String s) { msDriver = s; }
    public void setPlate(java.lang.String s) { msPlate = s; }
    public void setTicket(java.lang.String s) { msTicket = s; }
    public void setContainerTank(java.lang.String s) { msContainerTank = s; }
    public void setVgm(java.lang.String s) { msVgm = s; }
    public void setAcidityPercentage_n(Double d) { mdAcidityPercentage_n = d; }
    public void setFkVehicleTypeId_n(int n) { mnFkVehicleTypeId_n = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    
    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public java.lang.String getSealQuality() { return msSealQuality; }
    public java.lang.String getSealSecurity() { return msSealSecurity; }
    public java.lang.String getDriver() { return msDriver; }
    public java.lang.String getPlate() { return msPlate; }
    public java.lang.String getTicket() { return msTicket; }
    public java.lang.String getContainerTank() { return msContainerTank; }
    public java.lang.String getVgm() { return msVgm; }
    public Double getAcidityPercentage_n() { return mdAcidityPercentage_n; }
    public int getFkVehicleTypeId_n() { return mnFkVehicleTypeId_n; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    
    public java.util.Vector<erp.mtrn.data.SDataDpsEntryNotes> getDbmsEntryNotes() { return mvDbmsEntryNotes; }
       
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkEntryId = ((int[]) pk)[2];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        msSealQuality = "";
        msSealSecurity = "";
        msDriver = "";
        msPlate = "";
        msTicket = "";
        msContainerTank = "";
        msVgm = "";
        mdAcidityPercentage_n = null;
        mnFkVehicleTypeId_n = 0;
        mnFkUserEditId = 0;
        
        mvDbmsEntryNotes.clear();     
    }

    public void setData(final SDataDpsEntry dpsEntry) {
        mnPkYearId = dpsEntry.getPkYearId();
        mnPkDocId = dpsEntry.getPkDocId();
        mnPkEntryId = dpsEntry.getPkEntryId();
        msSealQuality = dpsEntry.getSealQuality();
        msSealSecurity = dpsEntry.getSealSecurity();
        msDriver = dpsEntry.getDriver();
        msPlate = dpsEntry.getPlate();
        msTicket = dpsEntry.getTicket();
        msContainerTank = dpsEntry.getContainerTank();
        msVgm = dpsEntry.getVgm();
        mdAcidityPercentage_n = dpsEntry.getAcidityPercentage_n();
        mnFkVehicleTypeId_n = dpsEntry.getFkVehicleTypeId_n();
        mnFkUserEditId = dpsEntry.getFkUserEditId();
        
        mvDbmsEntryNotes.addAll(dpsEntry.getDbmsEntryNotes());
        
        mbIsRegistryNew = false;
        mbIsRegistryEdited = dpsEntry.getFlagMinorChangesEdited();
    }
    
    @Override
    public int read(Object pk, Statement statement) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int save(Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            String sql = "UPDATE trn_dps_ety SET "
                    + "seal_qlt = '" + msSealQuality + "', "
                    + "seal_sec = '" + msSealSecurity + "', "
                    + "driver = '" + msDriver + "', "
                    + "plate = '" + msPlate + "', "
                    + "ticket = '" + msTicket + "', "
                    + "cont_tank = '" + msContainerTank + "', "
                    + "vgm = '" + msVgm + "', "
                    + "acidity_per_n = " + (mdAcidityPercentage_n == null ? "NULL" : mdAcidityPercentage_n) + ", "
                    + "fid_tp_veh_n = " + (mnFkVehicleTypeId_n != 0 ? mnFkVehicleTypeId_n : "NULL") + ", "
                    + "fid_usr_edit = " + mnFkUserEditId + ", "
                    + "ts_edit = NOW() "
                    + "WHERE id_year = " + mnPkYearId + " "
                    + "AND id_doc = " + mnPkDocId + " "
                    + "AND id_ety = " + mnPkEntryId + " "
                    + "AND ("
                    + "seal_qlt <> '" + msSealQuality + "' "
                    + "OR seal_sec <> '" + msSealSecurity + "' "
                    + "OR driver <> '" + msDriver + "' "
                    + "OR plate <> '" + msPlate + "' "
                    + "OR ticket <> '" + msTicket + "' "
                    + "OR cont_tank <> '" + msContainerTank + "' "
                    + "OR vgm <> '" + msVgm + "' "
                    + "OR acidity_per_n <> " + (mdAcidityPercentage_n == null ? "NULL" : mdAcidityPercentage_n) + " "
                    + "OR ((fid_tp_veh_n IS NULL AND " + (mnFkVehicleTypeId_n != 0) + ") OR (fid_tp_veh_n IS NOT NULL AND fid_tp_veh_n <> " + mnFkVehicleTypeId_n + ")));";
                    
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
            }
            
            for (SDataDpsEntryNotes entryNotes : mvDbmsEntryNotes) {
                if (entryNotes.getIsRegistryNew() || entryNotes.getIsRegistryEdited()) {
                    entryNotes.setPkYearId(mnPkYearId);
                    entryNotes.setPkDocId(mnPkDocId);
                    entryNotes.setPkEntryId(mnPkEntryId);

                    entryNotes.setFkUserNewId(mnFkUserEditId);
                    entryNotes.setFkUserEditId(mnFkUserEditId);
                    
                    if (entryNotes.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }
            }
            
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
