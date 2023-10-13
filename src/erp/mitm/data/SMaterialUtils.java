/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mitm.data;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Carmona
 */
public abstract class SMaterialUtils {
    
    public static ArrayList<SDataMaterialAttribute> getAttributesOfType(java.sql.Statement statement, final int idMaterialType) {
        ResultSet res = null;
        try {
            String sql = "SELECT id_mat_att "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_TP_MAT_ATT) + " "
                    + "WHERE NOT b_del AND id_tp_mat = " + idMaterialType + " "
                    + "ORDER BY sort ASC LIMIT 10;";
            
            res = statement.getConnection().createStatement().executeQuery(sql);
            ArrayList<SDataMaterialAttribute> lAttributes = new ArrayList<>();
            SDataMaterialAttribute oAttribute;
            while (res.next()) {
                oAttribute = new SDataMaterialAttribute();
                oAttribute.read(new int[] { res.getInt("id_mat_att") }, statement);
                lAttributes.add(oAttribute);
            }
            
            return lAttributes;
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
