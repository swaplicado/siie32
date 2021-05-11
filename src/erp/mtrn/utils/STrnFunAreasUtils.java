/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.utils;

import erp.client.SClientInterface;
import erp.data.SDataReadDescriptions;
import erp.lib.SLibConstants;
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
    
    private static int getNumberFunctionalAreas(final SClientInterface client) {
        String query = "SELECT COUNT(*) AS n_areas FROM cfgu_func WHERE NOT b_del";
        try {
            ResultSet resulSet = client.getSession().getStatement().executeQuery(query);
            
            if (resulSet.next()) {
                return resulSet.getInt("n_areas");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnFunAreasUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }
    
    /**
     * Determina las áreas asignadas de un usuario y devuelve los ids separados por coma [0],
      y los códigos correspondientes[1], en caso de que el usuario tenga todas las áreas asignadas devuelve la
      cadena "(TODAS)" [1]
     * @param client
     * @param id del área funcional activa
     * @return 
     */
    public static String[] getFunAreasTextFilter(final SClientInterface client, int nFunctionalAreaId) {
        String sText = "";
        String codes = "";
        String sFunctionalAreasIds = "";
        ArrayList<String> lFunctionalAreasIds = null;
        ArrayList<String> lFunctionalAreasCodes = null;
        
        if (nFunctionalAreaId == SLibConstants.UNDEFINED) {
            lFunctionalAreasIds = STrnFunAreasUtils.getFunctionalAreasOfUser(client, client.getSessionXXX().getUser().getPkUserId(), STrnFunAreasUtils.FUN_AREA_ID, "");
            
            if (lFunctionalAreasIds.isEmpty()) {
                sFunctionalAreasIds = "0";
                sText = "";
            }
            else {
                for (String id : lFunctionalAreasIds) {
                    sFunctionalAreasIds += id + ", ";
                }

                sFunctionalAreasIds = sFunctionalAreasIds.substring(0, sFunctionalAreasIds.length() - 2);
                
                if (lFunctionalAreasIds.size() == STrnFunAreasUtils.getNumberFunctionalAreas(client)) {
                    sText = "(TODAS)";
                }
                else {
                    lFunctionalAreasCodes = STrnFunAreasUtils.getFunctionalAreasOfUser(client, client.getSessionXXX().getUser().getPkUserId(), STrnFunAreasUtils.FUN_AREA_CODE, "");
                    for (String code : lFunctionalAreasCodes) {
                        codes += code + ", ";
                    }

                    sText = codes.substring(0, codes.length() - 2);
                }
            }
        }
        else {
            sText = SDataReadDescriptions.getCatalogueDescription(client, SModConsts.CFGU_FUNC, new int[] { nFunctionalAreaId }, SLibConstants.DESCRIPTION_CODE);
            sFunctionalAreasIds = "" + nFunctionalAreaId;
        }
        
        return new String[] { sFunctionalAreasIds, sText };
    }
}
