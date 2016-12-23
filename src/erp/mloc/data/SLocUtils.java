/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mloc.data;

import java.sql.ResultSet;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public abstract class SLocUtils {
    
    public static boolean hasAssociateStates(final SGuiSession session, final int idCountry) {
        String sql = "";
        ResultSet resultSet = null;
        boolean has = false;
        
        try {
            sql = "SELECT count(*) "
                    + "FROM erp.locu_sta "
                    + "WHERE fid_cty = " + idCountry + " ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                has = true;
            }
        }
        catch (Exception e) {
            SLibUtils.printException(SLocUtils.class, e);
        }
        
        return has;
    }
}
