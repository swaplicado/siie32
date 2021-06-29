/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

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
 * @author Edwin Carmona, Sergio Flores
 */
public class STrnFunctionalAreaUtils {
    
    public static final int FUNC_AREA_ID = 1;
    public static final int FUNC_AREA_CODE = 2;
    public static final int FUNC_AREA_NAME = 3;
    
    /**
     * Obtener valores del campo solicitado de las áreas funcionales del usuario.
     * @param client GUI client.
     * @param userId User ID.
     * @param fieldType Field type.
     * @return 
     */
    public static ArrayList<String> getFunctionalAreasOfUser(final SClientInterface client, final int userId, final int fieldType) {
        String query = "SELECT ";
        String field = "";
        
        switch (fieldType) {
            case FUNC_AREA_ID:
                field = "fa.id_func";
                break;
                
            case FUNC_AREA_CODE:
                field = "fa.code";
                break;
                
            case FUNC_AREA_NAME:
                field = "fa.name";
                break;
                
            default:
                field = "fa.id_func";
        }
        
        query += field + " FROM cfgu_func AS fa ";
        query += "INNER JOIN usr_usr_func AS fau ON "
                + "fau.id_func = fa.id_func AND fau.id_usr = " + userId + " ";
        query += "WHERE NOT fa.b_del ";

        ArrayList<String> areas = new ArrayList<>();
        
        try {
            ResultSet resulSet = client.getSession().getStatement().executeQuery(query);
            
            while (resulSet.next()) {
                areas.add(resulSet.getString(field));
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnFunctionalAreaUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return areas;
    }
    
    private static int getNumberOfFuncionalAreas(final SClientInterface client) {
        String query = "SELECT COUNT(*) AS n_areas FROM cfgu_func WHERE NOT b_del;";
        try {
            ResultSet resulSet = client.getSession().getStatement().executeQuery(query);
            
            if (resulSet.next()) {
                return resulSet.getInt("n_areas");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnFunctionalAreaUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }
    
    /**
     * Determina las áreas asignadas de un usuario y devuelve los ids separados por coma [0],
     * y los códigos correspondientes[1], en caso de que el usuario tenga todas las áreas asignadas devuelve la
     * cadena "(TODAS)" [1]
     * @param client
     * @param functionalAreaId ID del área funcional activa.
     * @return 
     */
    public static String[] getTextFilterOfFunctionalAreas(final SClientInterface client, int functionalAreaId) {
        String text = "";
        String codes = "";
        String functionalAreaIds = "";
        ArrayList<String> functionalAreaIdsList = null;
        ArrayList<String> functionalAreaCodesList = null;
        
        if (functionalAreaId == SLibConstants.UNDEFINED) {
            functionalAreaIdsList = STrnFunctionalAreaUtils.getFunctionalAreasOfUser(client, client.getSessionXXX().getUser().getPkUserId(), STrnFunctionalAreaUtils.FUNC_AREA_ID);
            
            if (functionalAreaIdsList.isEmpty()) {
                functionalAreaIds = "0";
                text = "ND";
            }
            else {
                for (String id : functionalAreaIdsList) {
                    functionalAreaIds += id + ", ";
                }

                functionalAreaIds = functionalAreaIds.substring(0, functionalAreaIds.length() - 2);
                
                if (functionalAreaIdsList.size() == STrnFunctionalAreaUtils.getNumberOfFuncionalAreas(client)) {
                    text = "(TODAS)";
                }
                else {
                    functionalAreaCodesList = STrnFunctionalAreaUtils.getFunctionalAreasOfUser(client, client.getSessionXXX().getUser().getPkUserId(), STrnFunctionalAreaUtils.FUNC_AREA_CODE);
                    for (String code : functionalAreaCodesList) {
                        codes += code + ", ";
                    }

                    text = codes.substring(0, codes.length() - 2);
                }
            }
        }
        else {
            text = SDataReadDescriptions.getCatalogueDescription(client, SModConsts.CFGU_FUNC, new int[] { functionalAreaId }, SLibConstants.DESCRIPTION_CODE);
            functionalAreaIds = "" + functionalAreaId;
        }
        
        return new String[] { functionalAreaIds, text };
    }
}
