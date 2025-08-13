/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

/**
 * Utilerías JSON para SWAP Services.
 * 
 * @author Sergio Flores
 */
public abstract class SJsonUtils {

    /**
     * Sanitiza una cadena JSON para evitar caracteres problemáticos.
     * 
     * @param json Cadena JSON.
     * @return Cadena JSON sanitizada.
     */
    public static String sanitizeJson(final String json) {
        if (json == null || json.isEmpty()) {
            return json;
        }
        
        return json.replace("\"", " ").replace("'", " ").replace("\\", "\\\\").replace("\n", " ").replace("\r", " ").trim();
    }

    /**
     * Remueve todos los espacios en blanco (espacios, tabuladores, saltos de línea, etc.) de la cadena JSON.
     * 
     * @param json Cadena JSON.
     * @return Cadena JSON compactada.
     */
    public static String removeWhiteSpaces(final String json) {
        if (json == null || json.isEmpty()) {
            return json;
        }
        
        return json.replaceAll("\\s+", "");
    }
    
}
