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
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public abstract class SMaterialUtils {
    
    public static ArrayList<SDataAttributeMaterial> getAttributesOfType(java.sql.Statement statement, final int idMaterialType) {
        ResultSet res = null;
        try {
            String sql = "SELECT id_att_mat "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_TP_ATT_MAT) + " "
                    + "WHERE NOT b_del "
                    + (idMaterialType > 0 ? ("AND id_tp_mat = " + idMaterialType + " ") : "")
                    + "ORDER BY sort ASC LIMIT " + SDataAttributeMaterialType.MAX_ATTRIBUTES + ";";
            
            res = statement.getConnection().createStatement().executeQuery(sql);
            ArrayList<SDataAttributeMaterial> lAttributes = new ArrayList<>();
            SDataAttributeMaterial oAttribute;
            while (res.next()) {
                oAttribute = new SDataAttributeMaterial();
                oAttribute.read(new int[] { res.getInt("id_att_mat") }, statement);
                lAttributes.add(oAttribute);
            }
            
            return lAttributes;
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static ArrayList<SDataAttributeMaterial> getAllAttributes(java.sql.Statement statement) {
        ResultSet res = null;
        try {
            String sql = "SELECT id_att_mat "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ATT_MAT) + " "
                    + "WHERE NOT b_del "
                    + "ORDER BY name ASC;";
            
            res = statement.getConnection().createStatement().executeQuery(sql);
            ArrayList<SDataAttributeMaterial> lAttributes = new ArrayList<>();
            SDataAttributeMaterial oAttribute;
            while (res.next()) {
                oAttribute = new SDataAttributeMaterial();
                oAttribute.read(new int[] { res.getInt("id_att_mat") }, statement);
                lAttributes.add(oAttribute);
            }
            
            return lAttributes;
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static void saveSelectedAttributes(SGuiSession session, ArrayList<SDataAttributeMaterial> lNewAttributes, final int idMaterialType) {
        try {
            String sql = "DELETE "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_TP_ATT_MAT) + " "
                    + "WHERE id_tp_mat = " + idMaterialType + ";";

            session.getStatement().getConnection().createStatement().execute(sql);
            
            int sortOrder = 1;
            SDataAttributeMaterialType oCfg;
            for (SDataAttributeMaterial oNewAttribute : lNewAttributes) {
                oCfg = new SDataAttributeMaterialType();
                oCfg.setPkItemMaterialAttributeId(oNewAttribute.getPkMaterialAttributeId());
                oCfg.setPkItemMaterialTypeId(idMaterialType);
                oCfg.setSortingPos(sortOrder);
                oCfg.setFkUserInsertId(session.getUser().getPkUserId());
                oCfg.setFkUserUpdateId(1);
                oCfg.setFkUserDeleteId(1);
                oCfg.setIsRequired(true);
                
                oCfg.save(session.getStatement().getConnection());
                sortOrder++;
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
