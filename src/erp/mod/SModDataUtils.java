/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod;

import java.sql.ResultSet;
import sa.lib.gui.SGuiSession;

/**
 * Clase de utileria
 * @author Isabel Servín
 */
public class SModDataUtils {
    
     /**
     * Devuelve el campo nombre de una tabla a traves del código.
     * @param session
     * @param table
     * @param code
     * @return name
     * @throws java.lang.Exception
    */
    public static String getCatalogNameByCode(SGuiSession session, int table, String code) throws Exception {
        String name = "";
        String sql = "";
        ResultSet resultSet;
        
        switch (table) {
            case SModConsts.LOGS_INC:
                sql = "SELECT name FROM erp.logs_inc WHERE code = '" + code + "'";
                break;
            case SModConsts.LOCU_STA:
                sql = "SELECT sta FROM erp.locu_sta WHERE sta_code = '" + code + "' AND NOT b_del";
                break;
            case SModConsts.LOCU_CTY:
                sql = "SELECT cty FROM erp.locu_cty WHERE cty_code = '" + code + "' AND NOT b_del";
                break;
            default:
        }
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            name = resultSet.getString(1);
        }
        
        return name;
    }
    
    /**
     * Devuelve el campo nombre de una tabla a traves del código.
     * @param session
     * @param table
     * @param code
     * @param stateCode
     * @return name
     * @throws java.lang.Exception
    */
    public static String getLocCatalogNameByCode(SGuiSession session, int table, String code, String stateCode) throws Exception {
        String name = "";
        String sql = "";
        ResultSet resultSet;
        
        switch (table) {
            case SModConsts.LOCS_BOL_COUNTY:
                sql = "SELECT description FROM erp.locs_bol_county WHERE id_county_code = '" + code + "' AND id_sta_code = '" + stateCode + "' AND NOT b_del";
                break;
            case SModConsts.LOCS_BOL_LOCALITY:
                sql = "SELECT description FROM erp.locs_bol_locality WHERE id_locality_code = '" + code + "' AND id_sta_code = '" + stateCode + "' AND NOT b_del";
                break;
            default:
        }
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            name = resultSet.getString(1);
        }
        
        return name;
    }
}
