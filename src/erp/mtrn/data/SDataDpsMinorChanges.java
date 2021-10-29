package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import sa.lib.SLibConsts;

public class SDataDpsMinorChanges extends erp.lib.data.SDataRegistry{
  
    public static final HashMap<Integer, String> AutAuthornRejMap = new HashMap<>();
            
    protected int mnPkYearId;
    protected int mnPkDocId;   
    protected java.lang.String msDriver;
    protected java.lang.String msPlate;
    protected java.lang.String msTicket;
    protected int mnFkSalesAgentId_n;
    protected int mnFkSalesSupervisorId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Vector<SDataDpsEntryMinorChanges> mvDbmsDpsEntries;
    protected java.util.Vector<SDataDpsNotes> mvDbmsDpsNotes;
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }   
    public void setDriver(java.lang.String s) { msDriver = s; }
    public void setPlate(java.lang.String s) { msPlate = s; }
    public void setTicket(java.lang.String s) { msTicket = s; }   
    public void setFkSalesAgentId_n(int n) { mnFkSalesAgentId_n = n; } 
    public void setFkSalesSupervisorId_n(int n) { mnFkSalesSupervisorId_n = n; }   
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }   
    public java.lang.String getDriver() { return msDriver; }
    public java.lang.String getPlate() { return msPlate; }
    public java.lang.String getTicket() { return msTicket; }  
    public int getFkSalesAgentId_n() { return mnFkSalesAgentId_n; } 
    public int getFkSalesSupervisorId_n() { return mnFkSalesSupervisorId_n; }  
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Vector<erp.mtrn.data.SDataDpsEntryMinorChanges> getDbmsDpsEntries() { return mvDbmsDpsEntries; }
    public java.util.Vector<erp.mtrn.data.SDataDpsNotes> getDbmsDpsNotes() { return mvDbmsDpsNotes; }
    
    
    public SDataDpsMinorChanges() {
        super(SDataConstants.TRN_DPS);
        mlRegistryTimeout = 1000 * 60 * 60 * 2; // 2 hr
        mvDbmsDpsEntries = new Vector<>();
        mvDbmsDpsNotes = new Vector<>();
        reset();
    }
    
    public void setEntry(SDataDpsEntryMinorChanges v){
        mvDbmsDpsEntries.add(v);
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
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mvDbmsDpsEntries.clear();
        mvDbmsDpsNotes.clear();    
    }

    public void setData(SDataDps Data){
        int sizeEntry = 0;
        int sizeNota = 0;
        
        mnPkYearId = Data.getPkYearId();
        mnPkDocId = Data.getPkDocId();
        mnFkUserEditId = Data.getFkUserEditId();
        
        msDriver = Data.getDriver();
        msPlate = Data.getPlate();
        msTicket = Data.getTicket();
        
        mnFkSalesAgentId_n = Data.getFkSalesAgentId_n();
        mnFkSalesSupervisorId_n = Data.getFkSalesSupervisorId_n();
        
        Vector<SDataDpsEntry> entries = Data.getDbmsDpsEntries();
        sizeEntry = entries.size();
        
        Vector<SDataDpsNotes> notas = Data.getDbmsDpsNotes();
        sizeNota = notas.size();
        
        for(int i = 0; i < sizeEntry; i++){
            SDataDpsEntryMinorChanges entry = new SDataDpsEntryMinorChanges();
            entry.setData(entries.get(i));   
            mvDbmsDpsEntries.add(entry);
        }
        
        for(int i = 0; i < sizeNota; i++){
            SDataDpsNotes notes = new SDataDpsNotes();
            mvDbmsDpsNotes.add(notas.get(i));
        }
        
    }
    
    @Override
    public int read(Object pk, Statement statement) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int save(Connection connection) {
        mnLastDbActionResult = SLibConsts.UNDEFINED;
        String Agent;
        String Super;
        Statement oStatement = null;
        String sSql = "";
        ResultSet oResultSet = null;
        
        try {
            oStatement = connection.createStatement();

                for (SDataDpsEntryMinorChanges entry : mvDbmsDpsEntries) {                    
                    if (entry.getIsRegistryNew() || entry.getIsRegistryEdited()) {
                        entry.setPkYearId(mnPkYearId);
                        entry.setPkDocId(mnPkDocId);
                        
                        //if(entry.getIsEdited()){
                            if (entry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                            }
                        //}
                    }
                }
                
                for (SDataDpsNotes notes : mvDbmsDpsNotes) {
                    if (notes.getIsRegistryNew() || notes.getIsRegistryEdited()) {
                        notes.setPkYearId(mnPkYearId);
                        notes.setPkDocId(mnPkDocId);

                        if (notes.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }
            
                if(mnFkSalesAgentId_n != 0){
                   Agent = " fid_sal_agt_n = " + mnFkSalesAgentId_n;
                }else{
                    Agent = "fid_sal_agt_n = NULL";
                }
                
                if(mnFkSalesSupervisorId_n != 0){
                    Super = " fid_sal_sup_n = " + mnFkSalesSupervisorId_n;
                }else{
                    Super = " fid_sal_sup_n = NULL";
                }
                
            sSql = "UPDATE trn_dps SET" +
                    " driver = " + '"' + msDriver +'"' + "," +
                    " plate = " + '"' + msPlate + '"' + "," +
                    " ticket = " + '"' + msTicket + '"' + "," +
                    Agent + "," +
                    Super + "," +
                    "fid_usr_edit = " + mnFkUserEditId + "," +
                    "ts_edit = NOW()" +
                    " WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
                    oStatement.execute(sSql);
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
