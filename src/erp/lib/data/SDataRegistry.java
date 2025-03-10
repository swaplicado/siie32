/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.data;

import erp.lib.SLibConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Sergio Flores
 */
public abstract class SDataRegistry implements java.io.Serializable {
    
    public static final int ACTION_NEW = 1;
    public static final int ACTION_EDIT = 2;
    public static final int ACTION_DELETE = 3;

    protected int mnRegistryType;
    protected long mlRegistryTimeout;

    protected int mnLastDbActionResult;
    protected boolean mbIsRegistryNew;
    protected boolean mbIsRegistryEdited;
    protected boolean mbIsRegistryRequestAnnul; // flag used in annul() for annulment action or reverting it
    protected boolean mbIsRegistryRequestDelete; // flag used in delete() for deletion action or reverting it
    protected int mnDbmsErrorId;
    protected java.lang.String msDbmsError;
    protected java.util.Vector<java.lang.Object> mvRegistryComplements;
    
    protected java.lang.Object moPostSaveTarget;
    protected java.lang.reflect.Method moPostSaveMethod;
    protected java.lang.Object[] maoPostSaveMethodArgs;
    protected java.util.Date mtTempFileTimestamp;

    /**
     * @param registryType Constants defined in erp.data.SDataConstants.
     */
    public SDataRegistry(int registryType) {
        mnRegistryType = registryType;
        mlRegistryTimeout = 1000 * 60 * 15;  // 15 min
        
        mvRegistryComplements = new Vector<>();

        resetRegistry();
    }

    public void setRegistryType(int n) { mnRegistryType = n; }
    public void setRegistryTimeout(long l) { mlRegistryTimeout = l; }

    public int getRegistryType() { return mnRegistryType; }
    public long getRegistryTimeout() { return mlRegistryTimeout; }
    
    public void setLastDbActionResult(int n) { mnLastDbActionResult = n; }
    public void setIsRegistryNew(boolean b) { mbIsRegistryNew = b; }
    public void setIsRegistryEdited(boolean b) { mbIsRegistryEdited = b; }
    public void setIsRegistryRequestAnnul(boolean b) { mbIsRegistryRequestAnnul = b; }
    public void setIsRegistryRequestDelete(boolean b) { mbIsRegistryRequestDelete = b; }
    public void setDbmsErrorId(int n) { mnDbmsErrorId = n; }
    public void setDbmsError(java.lang.String s) { msDbmsError = s; }
    
    public int getLastDbActionResult() { return mnLastDbActionResult; }
    public boolean getIsRegistryNew() { return mbIsRegistryNew; }
    public boolean getIsRegistryEdited() { return mbIsRegistryEdited; }
    public boolean getIsRegistryRequestAnnul() { return mbIsRegistryRequestAnnul; }
    public boolean getIsRegistryRequestDelete() { return mbIsRegistryRequestDelete; }
    public int getDbmsErrorId() { return mnDbmsErrorId; }
    public java.lang.String getDbmsError() { return msDbmsError; }
    public java.util.Vector<java.lang.Object> getRegistryComplements() { return mvRegistryComplements; }
    
    public void setPostSaveTarget(java.lang.Object o) { moPostSaveTarget = o; }
    public void setPostSaveMethod(java.lang.reflect.Method o) { moPostSaveMethod = o; }
    public void setPostSaveMethodArgs(java.lang.Object[] ao) { maoPostSaveMethodArgs = ao; }
    
    public java.lang.Object getPostSaveTarget() { return moPostSaveTarget; }
    public java.lang.reflect.Method getPostSaveMethod() { return moPostSaveMethod; }
    public java.lang.Object[] getPostSaveMethodArgs() { return maoPostSaveMethodArgs; }
    public java.util.Date getTempFileTimestamp() { return mtTempFileTimestamp; }

    public void setIsDeleted(boolean b) { throw new UnsupportedOperationException("Not supported yet."); }
    public void setFkUserNewId(int n) { throw new UnsupportedOperationException("Not supported yet."); }
    public void setFkUserEditId(int n) { throw new UnsupportedOperationException("Not supported yet."); }
    public void setFkUserDeleteId(int n) { throw new UnsupportedOperationException("Not supported yet."); }

    public boolean getIsDeleted() { throw new UnsupportedOperationException("Not supported yet."); }
    public int getFkUserNewId() { throw new UnsupportedOperationException("Not supported yet."); }
    public int getFkUserEditId() { throw new UnsupportedOperationException("Not supported yet."); }
    public int getFkUserDeleteId() { throw new UnsupportedOperationException("Not supported yet."); }

    public abstract void setPrimaryKey(java.lang.Object pk);
    public abstract java.lang.Object getPrimaryKey();

    public abstract void reset();
    public abstract int read(java.lang.Object pk, java.sql.Statement statement);
    public abstract int save(java.sql.Connection connection);
    public abstract java.util.Date getLastDbUpdate();

    public void resetRegistry() {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        mbIsRegistryNew = true;
        mbIsRegistryEdited = false;
        mbIsRegistryRequestAnnul = true;
        mbIsRegistryRequestDelete = true;
        mnDbmsErrorId = 0;
        msDbmsError = "";
        mvRegistryComplements.clear();
        
        moPostSaveTarget = null;
        moPostSaveMethod = null;
        maoPostSaveMethodArgs = null;
        mtTempFileTimestamp = null;
    }

    public int canRead(java.sql.Connection connection) {
        return SLibConstants.DB_CAN_READ_YES;
    }

    public int canSave(java.sql.Connection connection) {
        return SLibConstants.DB_CAN_SAVE_YES;
    }

    public int canAnnul(java.sql.Connection connection) {
        return SLibConstants.DB_CAN_ANNUL_YES;
    }

    public int canDelete(java.sql.Connection connection) {
        return SLibConstants.DB_CAN_DELETE_YES;
    }

    public int annul(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        return mnLastDbActionResult;
    }

    public int delete(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        return mnLastDbActionResult;
    }

    public int process(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        return mnLastDbActionResult;
    }
    
    /**
     * Backup and clear non-serializable instance attributes, each identified by its "name".
     * @return Map of non-serializable instance attributes, each identified by its "name".
     */
    public HashMap<String, Object> backupNonSerializableMembers() {
        HashMap<String, Object> map = null;
        
        if (moPostSaveTarget != null || moPostSaveMethod != null || maoPostSaveMethodArgs != null) {
            map = new HashMap<>();
            
            if (moPostSaveTarget != null) {
                map.put("moPostSaveTarget", moPostSaveTarget);
                moPostSaveTarget = null;
            }
            
            if (moPostSaveMethod != null) {
                map.put("moPostSaveMethod", moPostSaveMethod);
                moPostSaveMethod = null;
            }
            
            if (maoPostSaveMethodArgs != null) {
                map.put("maoPostSaveMethodArgs", maoPostSaveMethodArgs);
                maoPostSaveMethodArgs = null;
            }
        }
        
        return map;
    }
    
    /**
     * Restore non-serializable instance attributes, each identified by its "name".
     * @param map Map of non-serializable instance attributes, each identified by its "name".
     */
    public void restoreNonSerializableMembers(final java.util.HashMap<String, Object> map) {
        for (String key : map.keySet()) {
            Object member = map.get(key);
            
            switch (key) {
                case "moPostSaveTarget":
                    moPostSaveTarget = (java.lang.Object) member;
                    break;
                case "moPostSaveMethod":
                    moPostSaveMethod = (java.lang.reflect.Method) member;
                    break;
                case "maoPostSaveMethodArgs":
                    maoPostSaveMethodArgs = (java.lang.Object[]) member;
                    break;
                default:
                    // nothing
            }
        }
    }
    
    /*
     * Temporal file to preserve data when UI chashes.
     */
    
    public java.lang.String getTempFileName(erp.client.SClientInterface client) {
        return System.getenv("TEMP") + "\\" + this.getClass().getName() + "_" + client.getSessionXXX().getCurrentCompany().getKey() + "_" + client.getSessionXXX().getUser().getUser() + ".json";
    }
    
    public boolean existsTempFile(erp.client.SClientInterface client) {
        boolean exists = false;
        String tempFileName = getTempFileName(client);
        
        if (!tempFileName.isEmpty()) {
            File tempFile = new File(tempFileName);
            exists = tempFile.exists();
        }
        
        return exists;
    }
    
    public void deleteTempFile(erp.client.SClientInterface client) throws IOException {
        if (existsTempFile(client)) {
            String tempFileName = getTempFileName(client);
            
            System.out.println("Eliminando el archivo temporal '" + tempFileName + "'...");

            File tempFile = new File(tempFileName);
            if (tempFile.delete()) {
                System.out.println("El archivo temporal '" + tempFileName + "' ha sido eliminado.");
            }
            else {
                System.out.println("El archivo temporal '" + tempFileName + "' no fue eliminado.");
            }
        }
    }
    
    public void readTempFileData(erp.client.SClientInterface client) throws IOException, Exception {
        if (existsTempFile(client)) {
            
            File tempFile = new File(getTempFileName(client));
            
            if (tempFile.length() > 0) {
                String json = "";
                
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile))) {
                    String line;
                    
                    while ((line = bufferedReader.readLine()) != null) {
                        json += line;
                    }
                }

                if (!json.isEmpty()) {
                    decodeJson(client, json);
                }
            }
        }
    }
    
    public void saveTempFileData(erp.client.SClientInterface client) throws IOException, Exception {
        deleteTempFile(client);
        
        File tempFile = new File(getTempFileName(client));
        
        try (FileWriter fileWriter = new FileWriter(tempFile)) {
            fileWriter.write(encodeJson(client));
        }
    }
    
    public java.lang.String encodeJson(erp.client.SClientInterface client) throws ParseException, Exception {
        return ""; // add code if method overridden
    }
    
    public void decodeJson(erp.client.SClientInterface client, java.lang.String json) throws ParseException, Exception {
        // add code if method overridden
    }
}
