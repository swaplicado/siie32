package erp.musr.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
            statement.getConnection().createStatement().executeUpdate(sql);
        }
        catch (Exception e) {
            Logger.getLogger(SUserUtils.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    /**
     * Devuelve los grupos de usuario a los que pertenece un usuario a partir de su Id
     * @param statement
     * @param userId
     * @return 
     */
    public static ArrayList<Integer> getUserGroupsIds(Statement statement, int userId) {
        ArrayList<Integer> userGroups = new ArrayList<>();
        try {
            String sql = "SELECT id_usr_grp FROM erp.usru_usr_grp_usr WHERE id_usr = " + userId + " ORDER BY id_usr_grp";
            ResultSet resultSet = statement.getConnection().createStatement().executeQuery(sql);
            while (resultSet.next()) {
                userGroups.add(resultSet.getInt(1));
            }
        }
        catch (SQLException e) {
            Logger.getLogger(SUserUtils.class.getName()).log(Level.SEVERE, null, e);
        }
        return userGroups;
    }
}
