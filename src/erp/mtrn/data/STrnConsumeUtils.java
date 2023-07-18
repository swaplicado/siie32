/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

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
public abstract class STrnConsumeUtils {
    
    public static ArrayList<Integer> getEntitysOfUser(SGuiSession session, final int idUser) {
        String sql = "SELECT " +
                "cu.* " +
                "FROM " +
                SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_USR) + " AS cu " +
                "INNER JOIN " +
                SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT) + " ce ON cu.id_mat_cons_ent = ce.id_mat_cons_ent " +
                "WHERE " +
                "NOT b_del " +
                "AND cu.id_usr = " + idUser + " " +
                "ORDER BY b_default DESC, name ASC;";
        
        try {
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            ArrayList<Integer> lEntities = new ArrayList<>();
            while (res.next()) {
                lEntities.add(res.getInt("id_mat_cons_ent"));
            }
            
            return lEntities;
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnConsumeUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();
    }
    
    public static int getDefaultEntityOfUser(SGuiSession session, final int idUser) {
        ArrayList<Integer> lEntities = STrnConsumeUtils.getEntitysOfUser(session, idUser);
        
        if (lEntities.size() > 0) {
            return lEntities.get(0);
        }
        
        return 0;
    }
    
    public static ArrayList<Integer> getEntitysOfBp(SGuiSession session, final int idBp) {
        String sql = "SELECT " +
                "cee.* " +
                "FROM " +
                SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_EMP) + " AS cee " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT) + " ce ON cee.id_mat_cons_ent = ce.id_mat_cons_ent " +
                "WHERE NOT ce.b_del " +
                "AND cee.id_bp = " + idBp + " " +
                "ORDER BY cee.b_default DESC, ce.name ASC;";
        
        try {
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            ArrayList<Integer> lEntities = new ArrayList<>();
            while (res.next()) {
                lEntities.add(res.getInt("id_mat_cons_ent"));
            }
            
            return lEntities;
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnConsumeUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();
    }
    
    public static int getDefaultEntityOfBp(SGuiSession session, final int idBp) {
        ArrayList<Integer> lEntities = STrnConsumeUtils.getEntitysOfBp(session, idBp);
        
        if (lEntities.size() > 0) {
            return lEntities.get(0);
        }
        
        return 0;
    }
}
