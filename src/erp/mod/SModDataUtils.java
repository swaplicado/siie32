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
            default:
        }
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            name = resultSet.getString(1);
        }
        
        return name;
    }
}
