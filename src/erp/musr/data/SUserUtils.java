package erp.musr.data;

import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.gui.SGuiSession;

/**
 * @author Edwin Carmona
 */
public class SUserUtils {
    
    /**
     * Actualiza la fecha de última sincronización del usuario
     * 
     * @param oSession
     * @param userId 
     */
    public static void updateLastSyncDate(SGuiSession oSession, int userId) {
        // Update the last sync date for the user
        Statement statement = oSession.getStatement();
            
        String sql = "UPDATE erp.usru_usr as u "
                    + "SET u.ts_last_sync_n = NOW(), "
                    + "fid_usr_last_sync_n = " + oSession.getUser().getPkUserId() + " "
                    + "WHERE u.id_usr = " + userId + ";";
        
        try {
            statement.executeUpdate(sql);
        }
        catch (Exception e) {
            Logger.getLogger(SUserUtils.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
