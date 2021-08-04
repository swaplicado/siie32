/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mloc.data;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public abstract class SLocUtils {
    
    /**
     * Check if given country has states.
     * @param session GUI user session.
     * @param idCountry Country ID.
     * @return <code>true</code> if country has states, otherwise <code>false</code>.
     * @throws SQLException 
     */
    public static boolean hasStates(final SGuiSession session, final int idCountry) throws SQLException {
        boolean hasStates = false;
        
        String sql = "SELECT COUNT(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOCU_STA) + " "
                + "WHERE fid_cty = " + idCountry + " AND NOT b_del;";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                hasStates = true;
            }
        }
        
        return hasStates;
    }
}
