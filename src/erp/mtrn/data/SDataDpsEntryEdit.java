/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 * Modificar el ítem y el centro de costo de un documento y de todos los documentos asociados a este, sin necesidad de editar cada documento de forma manual.
 * @author Isabel Servín
 */
public class SDataDpsEntryEdit extends erp.lib.data.SDataRegistry {
    
    protected ArrayList<SDataDps> moDocuments;

    public SDataDpsEntryEdit(int registryType) {
        super(registryType);
    }
    
    public void setDocuments(ArrayList<SDataDps> o) { moDocuments = o; }
    
    public ArrayList<SDataDps> getDocuments() { return moDocuments; }
    
    @Override
    public java.util.Vector<java.lang.Object> getRegistryComplements(){
        java.util.Vector<java.lang.Object> locks = new java.util.Vector();
        moDocuments.stream().forEach((document) -> {
            locks.add(document.getAuxUserLock());
        });
        return locks;
    }
    
    @Override
    public int save(java.sql.Connection connection) {
        try {
            moDocuments.stream().forEach((document) -> {
                document.save(connection);
            });
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
    public void setPrimaryKey(Object pk) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getPrimaryKey() {
        return null;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int read(Object pk, Statement statement) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Date getLastDbUpdate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
