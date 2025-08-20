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
import java.util.stream.Collectors;

/**
 *
 * @author Edwin Carmona, Sergio Flores
 */
public class STrnFunctionalAreaUtils {
    
    public static final int FIELD_ID = 1;
    public static final int FIELD_CODE = 2;
    
    /**
     * Obtener los ID de las áreas y subáreas funcionales del usuario.
     * 
     * @param client GUI client.
     * @return <code>ArrayList<int[]></code> en el que cada elemento es un arreglo de enteros de dos posiciones:
     * index 0: ID del área funcional;
     * index 1: ID del subárea funcional.
     */
    public static ArrayList<int[]> getUserFunctionalSubAreaIds(final SClientInterface client) {
        int userId = client.getSessionXXX().getUser().getPkUserId();
        
        String sql = "SELECT f.id_func, fs.id_func_sub "
                + "FROM cfgu_func AS f "
                + "INNER JOIN cfgu_func_sub AS fs ON fs.fk_func = f.id_func "
                + "INNER JOIN usr_usr_func_sub AS ufs ON ufs.id_func_sub = fs.id_func_sub AND ufs.id_usr = " + userId + " "
                + "WHERE NOT f.b_del AND NOT fs.b_del "
                + "ORDER BY f.id_func, fs.id_func_sub;";

        ArrayList<int[]> ids = new ArrayList<>();
        
        try {
            try (ResultSet resulSet = client.getSession().getStatement().executeQuery(sql)) {
                while (resulSet.next()) {
                    ids.add(new int[] { resulSet.getInt(1), resulSet.getInt(2) });
                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnFunctionalAreaUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ids;
    }

    
    /**
     * Obtener los valores del campo solicitado de las áreas funcionales del usuario.
     * 
     * @param client GUI client.
     * @param fieldType Field type.
     * @return 
     */
    private static ArrayList<Object> getUserFunctionalAreaData(final SClientInterface client, final int fieldType) {
        String field = "";
        int userId = client.getSessionXXX().getUser().getPkUserId();
        
        switch (fieldType) {
            case FIELD_CODE:
                field = "f.code";
                break;
            default:
                field = "f.id_func";
        }
        
        String sql = "SELECT " + field + " "
                + "FROM cfgu_func AS f "
                + "INNER JOIN usr_usr_func AS uf ON uf.id_func = f.id_func AND uf.id_usr = " + userId + " "
                + "WHERE NOT f.b_del "
                + "ORDER BY f.id_func;";

        ArrayList<Object> data = new ArrayList<>();
        
        try {
            try (ResultSet resulSet = client.getSession().getStatement().executeQuery(sql)) {
                while (resulSet.next()) {
                    switch (fieldType) {
                        case FIELD_CODE:
                            data.add(resulSet.getString(1));
                            break;
                        default:
                            data.add(resulSet.getInt(1));
                    }
                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnFunctionalAreaUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }

    /**
     * Obtener los ID de las áreas funcionales del usuario.
     * @param client GUI client.
     * @return 
     */
    private static ArrayList<Integer> getUserFunctionalAreaIds(final SClientInterface client) {
        ArrayList<Object> data = getUserFunctionalAreaData(client, FIELD_ID);
        return new ArrayList<>(data.stream()
                .map(o -> (Integer) o) // safe cast since all are Integers
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    /**
     * Obtener los códigos de las áreas funcionales del usuario.
     * @param client GUI client.
     * @return 
     */
    private static ArrayList<String> getUserFunctionalAreaCodes(final SClientInterface client) {
        ArrayList<Object> data = getUserFunctionalAreaData(client, FIELD_CODE);
        return new ArrayList<>(data.stream()
                .map(o -> (String) o) // safe cast since all are Strings
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    private static int getCountOfFuncionalAreas(final SClientInterface client) {
        int count = 0;
        
        try {
            String sql = "SELECT COUNT(*) "
                    + "FROM cfgu_func "
                    + "WHERE NOT b_del;";
            try (ResultSet resulSet = client.getSession().getStatement().executeQuery(sql)) {
                if (resulSet.next()) {
                    count = resulSet.getInt(1);
                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnFunctionalAreaUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return count;
    }
    
    /**
     * Determina las áreas funcionales asignadas de un usuario y devuelve:
     * - index 0: los ID separados por coma;
     * - index 1: los códigos correspondientes, o, si el usuario tiene asignadas todas las áreas, el texto "(TODAS)".
     * @param client
     * @param currentFunctionalAreaId ID del área funcional activa.
     * @return Arreglo <code>String[]</code> con lista de ID y códigos de las áreas funcionales asignadas al usuario actual.
     */
    public static String[] getTextFilterOfFunctionalAreas(final SClientInterface client, int currentFunctionalAreaId) {
        String ids = "";
        String codes = "";
        
        if (!client.getSessionXXX().getParamsCompany().getIsFunctionalAreas()) { // functional areas not supported!
            ids = "";
            codes = "(ND)";
        }
        else if (currentFunctionalAreaId != 0) { // current functional area
            ids = "" + currentFunctionalAreaId;
            codes = SDataReadDescriptions.getCatalogueDescription(client, SModConsts.CFGU_FUNC, new int[] { currentFunctionalAreaId }, SLibConstants.DESCRIPTION_CODE);
        }
        else { // all functional areas of session user
            ArrayList<Integer> userFunctionalAreaIds = STrnFunctionalAreaUtils.getUserFunctionalAreaIds(client);
            
            if (userFunctionalAreaIds.isEmpty()) {
                ids = "0";
                codes = "(NINGUNA)";
            }
            else {
                for (Integer id : userFunctionalAreaIds) {
                    ids += (ids.isEmpty() ? "" : ", ") + id;
                }

                if (userFunctionalAreaIds.size() == STrnFunctionalAreaUtils.getCountOfFuncionalAreas(client)) {
                    codes = "(TODAS)";
                }
                else {
                    ArrayList<String> userFunctionalAreaCodes = STrnFunctionalAreaUtils.getUserFunctionalAreaCodes(client);
                    for (String code : userFunctionalAreaCodes) {
                        if (!code.isEmpty()) {
                            codes += (codes.isEmpty() ? "" : ", ") + code;
                        }
                    }
                }
            }
        }
        
        return new String[] { ids, codes };
    }
}
