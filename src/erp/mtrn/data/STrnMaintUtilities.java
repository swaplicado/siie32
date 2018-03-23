/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.lib.SLibConstants;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Sergio Flores
 */
public abstract class STrnMaintUtilities {
    
    public static int getLastMaintDiogSignature(final Statement statement, final int[] diogKey) throws Exception {
        int id = SLibConstants.UNDEFINED;
        
        String sql = "SELECT MAX(id_maint_diog_sig) "
                + "FROM trn_maint_diog_sig "
                + "WHERE fid_diog_year = " + diogKey[0] + " AND fid_diog_doc = " + diogKey[1] + " ";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
                
        return id;
    }
}
