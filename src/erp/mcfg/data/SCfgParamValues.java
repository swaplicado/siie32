/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mcfg.data;

import java.util.HashMap;
import sa.lib.SLibUtils;

/**
 * SCfgParamValues permite obtener valores individuales desde un valor de parámetro de configuración de SIIE (cfg_params).
 * Los valores individuales vienen en duplas de valores separados entre sí mediante punto y coma en el formato key=value.
 * @author Sergio Flores
 */
public class SCfgParamValues {
    
    protected final String msParamValue;
    protected final HashMap<String, String> moKeyValuesMap;
    
    public SCfgParamValues(final String paramValue) {
        msParamValue = paramValue;
        moKeyValuesMap = new HashMap<>();
        
        String[] keyValues = SLibUtils.textExplode(msParamValue, ";");
        for (String keyValue : keyValues) {
            String[] keyAndValue = SLibUtils.textExplode(keyValue, "=");
            moKeyValuesMap.put(keyAndValue[0], keyAndValue[1]);
        }
    }
    
    /**
     * Obtener el valor de parámetro de configuración de SIIE (cfg_params) original.
     * @return 
     */
    public String getParamValue() {
        return msParamValue;
    }
    
    /**
     * Obtener el valor individual correspondiente a la clave proporcionada.
     * @param key La clave correspondiente al valor individual deseado.
     * @return El valor individual deseado. Si la clave proporcionada no existe, devuelve <code>null</code>.
     */
    public String getKeyValue(final String key) {
        return moKeyValuesMap.get(key);
    }
}
