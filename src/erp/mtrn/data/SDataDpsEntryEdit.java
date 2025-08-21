/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 * Modificar el ítem de referencia y el centro de costo de un documento y de todos los documentos asociados a este, sin necesidad de editar cada documento de forma manual.
 * @author Isabel Servín
 */
public class SDataDpsEntryEdit extends erp.lib.data.SDataRegistry {
    
    protected ArrayList<SDataDps> moDocuments;
    protected int mnUserEditId;

    public SDataDpsEntryEdit() {
        super(SDataConstants.TRNX_DPS_EDIT);
        reset();
    }
    
    public void setUserEditId(int n) { mnUserEditId = n; }
    
    public ArrayList<SDataDps> getDocuments() { return moDocuments; }
    public int getUserEditId() { return mnUserEditId; }
    
    @Override
    @SuppressWarnings("unchecked")
    public java.util.Vector<java.lang.Object> getRegistryComplements(){
        java.util.Vector<java.lang.Object> locks = new java.util.Vector<>();
        moDocuments.stream().forEach((document) -> {
            locks.add(document.getAuxUserSLock());
        });
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
                        }
                    }
                }
                else {
                    document.save(connection);
                }
            }
            
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (SQLException e) {
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
    public void setPrimaryKey(Object pk) { }

    @Override
    public Object getPrimaryKey() {
        return null;
    }

    @Override
    public void reset() { 
        moDocuments = new ArrayList<>();
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
