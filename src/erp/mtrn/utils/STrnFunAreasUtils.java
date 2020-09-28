/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.utils;

import erp.client.SClientInterface;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Carmona
 */
public class STrnFunAreasUtils {
    
    public static final int FUN_AREA_ID = 1;
    public static final int FUN_AREA_CODE = 2;
    public static final int FUN_AREA_NAME = 3;
    public static final int FUN_AREA_OTHER = 4;
    
    /**
     * 
     * @param client
     * @param userId
     * @param resultType
     * @param customField
     * @return 
     */
    public static ArrayList<String> getFunctionalAreasOfUser(final SClientInterface client, final int userId, final int resultType, String customField) {
        String query = "SELECT ";
        String field = "";
        
        switch (resultType) {
            case FUN_AREA_ID:
                field = "fa.id_func";
                break;
                
            case FUN_AREA_CODE:
                field = "fa.code";
                break;
                
            case FUN_AREA_NAME:
                field = "fa.name";
                break;
                
            case FUN_AREA_OTHER:
                field = customField;
                break;
                
            default:
                field = "fa.id_func";
        }
        
        query += field + " FROM cfgu_func AS fa ";
        query += "INNER JOIN usr_usr_func AS fau ON "
                + "fau.id_func = fa.id_func AND fau.id_usr = " + userId + " ";
        query += "WHERE NOT fa.b_del ";

        ArrayList<String> areas = new ArrayList();
        
        try {
            ResultSet resulSet = client.getSession().getStatement().executeQuery(query);
            
            while (resulSet.next()) {
                areas.add(resulSet.getString(field));
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnFunAreasUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return areas;
    }
}
