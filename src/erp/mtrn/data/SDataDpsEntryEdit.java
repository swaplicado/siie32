/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.srv.SLock;

/**
 * Modificar el ítem de referencia y el centro de costo de un documento y de todos los documentos asociados a este, sin necesidad de editar cada documento de forma manual.
 * @author Isabel Servín, Rodrigo Ayala
 */
public class SDataDpsEntryEdit extends erp.lib.data.SDataRegistry {
    
    // Creamos un objeto DTO ligero para viajar por la red y transferir  sin causar MarshalException
    public static class MatReqEtyData implements java.io.Serializable {
        public int idMatReq;
        public int idEty;
        //public double qty;
        public int fkItemRefId_n;
        public int fkCostCenterId_n;
    }
    
    // Objeto DTO para la bitacora de cambios
    public static class HistLog implements java.io.Serializable {
        public SDataDpsEntry entry;
        public int itemRefNew;
        public ArrayList<int[]> allDocs;
    }
    
    protected ArrayList<SDataDps> moDocuments;
    //protected ArrayList<SDataDpsMaterialRequest> moDpsMaterialRequest; // Clase puente de DPS-MR
    protected ArrayList<MatReqEtyData> moMatReqsData;
    protected ArrayList<HistLog> moHistLogs;
    protected java.util.Vector<SLock> moMatReqLocks;
    protected int mnUserEditId;

    public SDataDpsEntryEdit() {
        super(SDataConstants.TRNX_DPS_EDIT);
        reset();
    }
    
    public void setUserEditId(int n) { mnUserEditId = n; }
    public void setMatReqsData(ArrayList<MatReqEtyData> data) { moMatReqsData = data; }
    public void setHistLogs(ArrayList<HistLog> logs) { moHistLogs = logs; }
    public void setMatReqLocks(java.util.Vector<SLock> locks) { moMatReqLocks = locks; }
    
    public ArrayList<SDataDps> getDocuments() { return moDocuments; }
    public int getUserEditId() { return mnUserEditId; }
    
    @Override
    @SuppressWarnings("unchecked")
    public java.util.Vector<java.lang.Object> getRegistryComplements(){
        java.util.Vector<java.lang.Object> locks = new java.util.Vector<>();
        moDocuments.stream().forEach((document) -> {
            locks.add(document.getAuxUserSLock());
        });
        
        if (moMatReqLocks != null) {
            locks.addAll(moMatReqLocks);
        }
        return locks;
    }
    
    @Override
    public int save(java.sql.Connection connection) {
        try {
            for (SDataDps document : moDocuments) {
                boolean isQtyEdited = false;
                for (SDataDpsEntry docEty : document.getDbmsDpsEntries()) {
                    if (docEty.getIsRegistryEdited()) {
                        if (docEty.getAuxOriginalQuantityOld() != docEty.getOriginalQuantity()) {
                            isQtyEdited = true;
                            break;
                        }
                    }
                }
                if (!isQtyEdited) {
                    for (SDataDpsEntry docEty : document.getDbmsDpsEntries()) {
                        if (docEty.getIsRegistryEdited()) {
                            String sql = "UPDATE trn_dps_ety SET "
                                    + "fid_item_ref_n = " + (docEty.getFkItemRefId_n() == 0 ? "NULL" : docEty.getFkItemRefId_n()) + ", "
                                    + "fid_cc_n = " + (docEty.getFkCostCenterId_n().isEmpty() ? "NULL" : "'" + docEty.getFkCostCenterId_n() + "'") + ", "
                                    + "fid_usr_edit = " + mnUserEditId + ", "
                                    + "ts_edit = NOW() "
                                    + "WHERE id_year = " + docEty.getPkYearId() + " AND id_doc = " + docEty.getPkDocId() + " AND id_ety = " + docEty.getPkEntryId();
                            
                            connection.createStatement().execute(sql);
                            
                            // Guardado de notas por partida:
                            java.util.Vector<erp.mtrn.data.SDataDpsEntryNotes> notesVector = docEty.getDbmsEntryNotes();
                            if (notesVector != null) {
                                for (erp.mtrn.data.SDataDpsEntryNotes notes : notesVector) {
                                    if (notes.getIsRegistryNew() || notes.getIsRegistryEdited()) {
                                        notes.setPkYearId(docEty.getPkYearId());
                                        notes.setPkDocId(docEty.getPkDocId());
                                        notes.setPkEntryId(docEty.getPkEntryId());

                                        if (notes.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP + " (Notas)");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    // Forzado de guardado del usuario que editó
                    document.setIsRegistryEdited(true);
                    document.setFkUserEditId(mnUserEditId);
                    
                    for (SDataDpsEntry docEty : document.getDbmsDpsEntries()) {
                        if (docEty.getIsRegistryEdited()) {
                            docEty.setFkUserEditId(mnUserEditId);
                            
                            if (docEty.getDbmsDpsEntryQuantityChange() != null) {
                                for (SDataDpsEntryQuantityChange qtyChg : docEty.getDbmsDpsEntryQuantityChange()) {
                                    if (qtyChg.getIsRegistryNew()) {
                                        qtyChg.setFkUserNewId(mnUserEditId);  
                                    }
                                }
                            }
                        }
                    }
                    document.save(connection);
                }
            }
            
            // Guardado de requisiciones:
            
            if (moMatReqsData != null) {
                for (MatReqEtyData data : moMatReqsData) {
                    String sql = "UPDATE trn_mat_req_ety SET "
                            //+ "user_qty = " + data.qty + ", " 
                            + "fk_item_ref_n = " + (data.fkItemRefId_n == 0 ? "NULL" : data.fkItemRefId_n) + ", "
                            + "fk_cc_n = " + (data.fkCostCenterId_n == 0 ? "NULL" : data.fkCostCenterId_n) + " " 
                            + "WHERE id_mat_req = " + data.idMatReq + " AND id_ety = " + data.idEty;                        
                    connection.createStatement().execute(sql);
                
                    sql = "UPDATE trn_mat_req SET "
                            + "fk_usr_upd = " + mnUserEditId + ", "
                            + "ts_usr_upd = NOW() "
                            + "WHERE id_mat_req = " + data.idMatReq;

                    connection.createStatement().execute(sql);
                }
            }
            
            if (moHistLogs != null && !moHistLogs.isEmpty()) {
                for (HistLog log : moHistLogs) {
                    erp.mtrn.data.STrnUtilities.insertDpsEtyHist(connection, mnUserEditId, log.entry, "", "", log.itemRefNew, log.allDocs); 
                }
            }
            
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            
            
        }
        catch (Exception e) { 
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            if (msDbmsError == null || msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_SAVE;
            }
            msDbmsError += "\n" + e.toString();
            SLibUtilities.printOutException(this, e);
        }
        return mnLastDbActionResult;
    }

    @Override
    public void setPrimaryKey(Object pk) { }

    @Override
    public Object getPrimaryKey() {
        return null;
    }

    @Override
    public void reset() { 
        moDocuments = new ArrayList<>();
        moMatReqsData = new ArrayList<>();
        moMatReqLocks = new java.util.Vector<>();
        mnUserEditId = 0;
    }

    @Override
    public int read(Object pk, Statement statement) {
        return 0;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}