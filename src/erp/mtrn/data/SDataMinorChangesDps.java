package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import sa.lib.SLibConsts;

/**
 * Cambios menores para documentos que ya no son modificables.
 * @author Adrián Avilés, Sergio Flores
 */
public class SDataMinorChangesDps extends erp.lib.data.SDataRegistry {
  
    public static final HashMap<Integer, String> AutAuthornRejMap = new HashMap<>();
            
    protected int mnPkYearId;
    protected int mnPkDocId;   
    protected java.lang.String msDriver;
    protected java.lang.String msPlate;
    protected java.lang.String msTicket;
    protected int mnFkSalesAgentId_n;
    protected int mnFkSalesSupervisorId_n;
    protected int mnFkCarrierTypeId;
    protected int mnFkCarrierId_n;
    protected int mnFkVehicleTypeId_n;
    protected int mnFkVehicleId_n;
    protected int mnFkUserEditId;
    
    protected java.util.Vector<SDataDpsNotes> mvDbmsNotes;
    protected java.util.Vector<SDataMinorChangesDpsEntry> mvDbmsEntries;
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }   
    public void setDriver(java.lang.String s) { msDriver = s; }
    public void setPlate(java.lang.String s) { msPlate = s; }
    public void setTicket(java.lang.String s) { msTicket = s; }   
    public void setFkSalesAgentId_n(int n) { mnFkSalesAgentId_n = n; } 
    public void setFkSalesSupervisorId_n(int n) { mnFkSalesSupervisorId_n = n; }   
    public void setFkCarrierTypeId(int n) { mnFkCarrierTypeId = n; }
    public void setFkCarrierId_n(int n) { mnFkCarrierId_n = n; }
    public void setFkVehicleTypeId_n(int n) { mnFkVehicleTypeId_n = n; }
    public void setFkVehicleId_n(int n) { mnFkVehicleId_n = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }   
    public java.lang.String getDriver() { return msDriver; }
    public java.lang.String getPlate() { return msPlate; }
    public java.lang.String getTicket() { return msTicket; }  
    public int getFkSalesAgentId_n() { return mnFkSalesAgentId_n; } 
    public int getFkSalesSupervisorId_n() { return mnFkSalesSupervisorId_n; }  
    public int getFkCarrierTypeId() { return mnFkCarrierTypeId; }
    public int getFkCarrierId_n() { return mnFkCarrierId_n; }
    public int getFkVehicleTypeId_n() { return mnFkVehicleTypeId_n; }
    public int getFkVehicleId_n() { return mnFkVehicleId_n; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    
    public java.util.Vector<erp.mtrn.data.SDataDpsNotes> getDbmsNotes() { return mvDbmsNotes; }
    public java.util.Vector<erp.mtrn.data.SDataMinorChangesDpsEntry> getDbmsEntries() { return mvDbmsEntries; }
    
    public SDataMinorChangesDps() {
        super(SDataConstants.TRN_DPS);
        mlRegistryTimeout = 1000 * 60 * 60 * 2; // 2 hr
        mvDbmsNotes = new Vector<>();
        mvDbmsEntries = new Vector<>();
        reset();
    }
    
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        msDriver = "";
        msPlate = "";
        msTicket = "";
        mnFkSalesAgentId_n = 0;
        mnFkSalesSupervisorId_n = 0;
        mnFkCarrierTypeId = 0;
        mnFkCarrierId_n = 0;
        mnFkVehicleTypeId_n = 0;
        mnFkVehicleId_n = 0;
        mnFkUserEditId = 0;
        
        mvDbmsNotes.clear();    
        mvDbmsEntries.clear();
    }

    public void setData(final SDataDps dps) {
        mnPkYearId = dps.getPkYearId();
        mnPkDocId = dps.getPkDocId();
        msDriver = dps.getDriver();
        msPlate = dps.getPlate();
        msTicket = dps.getTicket();
        mnFkSalesAgentId_n = dps.getFkSalesAgentId_n();
        mnFkSalesSupervisorId_n = dps.getFkSalesSupervisorId_n();
        mnFkCarrierTypeId = dps.getFkCarrierTypeId();
        mnFkCarrierId_n = dps.getFkCarrierId_n();
        mnFkVehicleTypeId_n = dps.getFkVehicleTypeId_n();
        mnFkVehicleId_n = dps.getFkVehicleId_n();
        mnFkUserEditId = dps.getFkUserEditId();
        
        mvDbmsNotes.addAll(dps.getDbmsDpsNotes());
        
        for (SDataDpsEntry dpsEntry : dps.getDbmsDpsEntries()) {
            SDataMinorChangesDpsEntry entry = new SDataMinorChangesDpsEntry();
            entry.setData(dpsEntry);   
            mvDbmsEntries.add(entry);
        }
        
        mbIsRegistryNew = false; // this registry is allways an existing one!
    }
    
    @Override
    public int read(Object pk, Statement statement) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int save(Connection connection) {
        mnLastDbActionResult = SLibConsts.UNDEFINED;
        
        try {
            String sql = "UPDATE trn_dps SET "
                    + "driver = '" + msDriver + "', "
                    + "plate = '" + msPlate + "', "
                    + "ticket = '" + msTicket + "', "
                    + "fid_sal_agt_n = " + (mnFkSalesAgentId_n != 0 ? mnFkSalesAgentId_n : "NULL") + ", "
                    + "fid_sal_sup_n = " + (mnFkSalesSupervisorId_n != 0 ? mnFkSalesSupervisorId_n : "NULL") + ", "
                    + "fid_tp_car = " + mnFkCarrierTypeId + ", "
                    + "fid_car_n = " + (mnFkCarrierId_n != 0 ? mnFkCarrierId_n : "NULL") + ", "
                    + "fid_tp_veh_n = " + (mnFkVehicleTypeId_n != 0 ? mnFkVehicleTypeId_n : "NULL") + ", "
                    + "fid_veh_n = " + (mnFkVehicleId_n != 0 ? mnFkVehicleId_n : "NULL") + ", "
                    + "fid_usr_edit = " + mnFkUserEditId + ", "
                    + "ts_edit = NOW() "
                    + "WHERE id_year = " + mnPkYearId + " "
                    + "AND id_doc = " + mnPkDocId + " "
                    + "AND (" // to prevent unnecessary updates
                    + "driver <> '" + msDriver + "' "
                    + "OR plate <> '" + msPlate + "' "
                    + "OR ticket <> '" + msTicket + "' "
                    + "OR ((fid_sal_agt_n IS NULL AND " + (mnFkSalesAgentId_n != 0) + ") OR (fid_sal_agt_n IS NOT NULL AND fid_sal_agt_n <> " + mnFkSalesAgentId_n + ")) "
                    + "OR ((fid_sal_sup_n IS NULL AND " + (mnFkSalesSupervisorId_n != 0) + ") OR (fid_sal_sup_n IS NOT NULL AND fid_sal_sup_n <> " + mnFkSalesSupervisorId_n + ")) "
                    + "OR fid_tp_car <> " + mnFkCarrierTypeId + " "
                    + "OR ((fid_car_n IS NULL AND " + (mnFkCarrierId_n != 0) + ") OR (fid_car_n IS NOT NULL AND fid_car_n <> " + mnFkCarrierId_n + ")) "
                    + "OR ((fid_tp_veh_n IS NULL AND " + (mnFkVehicleTypeId_n != 0) + ") OR (fid_tp_veh_n IS NOT NULL AND fid_tp_veh_n <> " + mnFkVehicleTypeId_n + ")) "
                    + "OR ((fid_veh_n IS NULL AND " + (mnFkVehicleId_n!= 0) + ") OR (fid_veh_n IS NOT NULL AND fid_veh_n <> " + mnFkVehicleId_n+ ")));";
            
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
            }
            
            for (SDataDpsNotes notes : mvDbmsNotes) {
                if (notes.getIsRegistryNew() || notes.getIsRegistryEdited()) {
                    notes.setPkYearId(mnPkYearId);
                    notes.setPkDocId(mnPkDocId);
                    
                    notes.setFkUserNewId(mnFkUserEditId);
                    notes.setFkUserEditId(mnFkUserEditId);

                    if (notes.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }
            }

            for (SDataMinorChangesDpsEntry entry : mvDbmsEntries) {
                if (entry.getIsRegistryNew() || entry.getIsRegistryEdited()) {
                    entry.setPkYearId(mnPkYearId);
                    entry.setPkDocId(mnPkDocId);
                    
                    entry.setFkUserEditId(mnFkUserEditId);

                    if (entry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }
            }

            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_SAVE;
            }
            msDbmsError += "\n" + e.toString();
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
