package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;


public class SDataDpsEntryMinorChanges extends erp.lib.data.SDataRegistry{

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected java.lang.String msSealQuality;
    protected java.lang.String msSealSecurity;
    protected int mnFkVehicleTypeId_n;
    protected java.lang.String msDriver;
    protected boolean mbIsEdited;
    protected java.lang.String msPlate;
    protected java.lang.String msTicket;
    protected java.lang.String msContainerTank;
    protected java.lang.String msVgm;
    protected int mnFkUserEditId;
    protected java.util.Vector<erp.mtrn.data.SDataDpsEntryNotes> mvDbmsEntryNotes;
    
    public SDataDpsEntryMinorChanges() {
        super(SDataConstants.TRN_DPS_ETY);
        mvDbmsEntryNotes = new Vector<>();
        reset();
    }
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setSealQuality(java.lang.String s) { msSealQuality = s; }
    public void setSealSecurity(java.lang.String s) { msSealSecurity = s; }
    public void setFkVehicleTypeId_n(int n) { mnFkVehicleTypeId_n = n; }
    public void setDriver(java.lang.String s) { msDriver = s; }
    public void setIsEdited(boolean b) { mbIsEdited = b; }
    public void setPlate(java.lang.String s) { msPlate = s; }
    public void setTicket(java.lang.String s) { msTicket = s; }
    public void setContainerTank(java.lang.String s) { msContainerTank = s; }
    public void setVgm(java.lang.String s) { msVgm = s; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    
    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public java.lang.String getSealQuality() { return msSealQuality; }
    public java.lang.String getSealSecurity() { return msSealSecurity; }
    public int getFkVehicleTypeId_n() { return mnFkVehicleTypeId_n; }
    public java.lang.String getDriver() { return msDriver; }
    public boolean getIsEdited() { return mbIsEdited; }
    public java.lang.String getPlate() { return msPlate; }
    public java.lang.String getTicket() { return msTicket; }
    public java.lang.String getContainerTank() { return msContainerTank; }
    public java.lang.String getVgm() { return msVgm; }
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
        mnFkVehicleTypeId_n = 0;
        msDriver = "";
        mbIsEdited = false;
        msPlate = "";
        msTicket = "";
        msContainerTank = "";
        msVgm = "";
        mnFkUserEditId = 0;
        mvDbmsEntryNotes.clear();     
    }

    public void setData(SDataDpsEntry entry){
        int size = 0;
        
        mnPkYearId = entry.getPkYearId();
        mnPkDocId = entry.getPkDocId();
        mnPkEntryId = entry.getPkEntryId();
        mnFkVehicleTypeId_n = entry.getFkVehicleTypeId_n();
        msDriver = entry.getDriver();
        msPlate = entry.getPlate();
        msTicket = entry.getTicket();
        msContainerTank = entry.getContainerTank();
        msSealQuality = entry.getSealQuality();
        msSealSecurity = entry.getSealSecurity();
        mbIsEdited  = entry.getFlagMinorChangesEdited();
        mnFkUserEditId = entry.getFkUserEditId();
        msVgm = entry.getVgm();
        
        Vector<SDataDpsEntryNotes> notas = entry.getDbmsEntryNotes();
        size = notas.size();
        
        for(int i = 0; i < size; i++){
            SDataDpsEntryNotes notes = new SDataDpsEntryNotes();
            mvDbmsEntryNotes.add(notas.get(i));
        }
        
    }
    
    @Override
    public int read(Object pk, Statement statement) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int save(Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        Statement oStatement = null;
        String sSql = "";
        String veh = "";
        try{
            for (SDataDpsEntryNotes notes : mvDbmsEntryNotes) {
                if (notes.getIsRegistryNew() || notes.getIsRegistryEdited()) {
                    notes.setPkYearId(mnPkYearId);
                    notes.setPkDocId(mnPkDocId);
                    notes.setPkEntryId(mnPkEntryId);

                    if (notes.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }
            }
            
            if(mnFkVehicleTypeId_n != 0){
                veh = " fid_tp_veh_n = " + mnFkVehicleTypeId_n;
            }else{
                veh = " fid_tp_veh_n = NULL";
            }
            
            oStatement = connection.createStatement();
            sSql = "UPDATE trn_dps_ety SET" +
                    veh + "," +
                    " driver = " + '"' + msDriver + '"' + "," +
                    " plate = " + '"' + msPlate + '"' + "," +
                    " ticket = " + '"' + msTicket + '"' + "," +
                    " cont_tank = " + '"' + msContainerTank + '"' + "," +
                    " seal_qlt = " + '"' + msSealQuality + '"' + "," +
                    " seal_sec = " + '"' + msSealSecurity + '"' + "," +
                    " vgm = " + '"' + msVgm + '"' + "," +
                    "fid_usr_edit = " + mnFkUserEditId + "," +
                    "ts_edit = NOW()" +
                    " WHERE id_year = " + mnPkYearId + 
                    " AND id_doc = " + mnPkDocId + 
                    " AND id_ety = " + mnPkEntryId + " ";
                    oStatement.execute(sSql);
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            
        }catch (java.lang.Exception e) {
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
