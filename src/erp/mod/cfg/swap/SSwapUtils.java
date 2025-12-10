/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap;

import erp.mod.SModSysConsts;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public abstract class SSwapUtils {
    
    public static String translateSyncType(final SSyncType syncType, final String languageIso639) {
        String translation = "";
        
        switch (languageIso639) {
            case SLibConsts.LAN_ISO639_ES:
                switch (syncType) {
                    case USER:
                        translation = "USUARIO";
                        break;
                    case PARTNER_SUPPLIER:
                        translation = "SOCIO PROVEEDOR";
                        break;
                    case PARTNER_CUSTOMER:
                        translation = "SOCIO CLIENTE";
                        break;
                    case AUTH_ACTOR:
                        translation = "AUTH ACTOR";
                        break;
                    case AUTH_JOB_TITLE:
                        translation = "AUTH PUESTO LABORAL";
                        break;
                    case FUNCTIONAL_AREA:
                        translation = "AREA FUNCIONAL";
                        break;
                    case PUR_ORDER:
                        translation = "PEDIDO COMPRAS";
                        break;
                    case PUR_REF_ORDER:
                        translation = "REF PEDIDO COMPRAS";
                        break;
                    case PUR_REF_SCALE_TICKET:
                        translation = "REF BOLETO BÁSCULA";
                        break;
                    case PUR_PAYMENT:
                        translation = "PAGO COMPRAS";
                        break;
                    case PUR_PAYMENT_UPD:
                        translation = "PAGO COMPRAS ACTUALIZACIÓN";
                        break;
                    default:
                        // nothing
                }
                break;
                
            default:
                translation = syncType.toString();
        }
        
        return translation;
    }
    
    public static String sanitizeUsername(final String username, final String languageIso639) {
        String sanitized = "";
        
        switch (languageIso639) {
            case SLibConsts.LAN_ISO639_ES:
                sanitized = username.replace('&', 'Y');
                break;
            case SLibConsts.LAN_ISO639_EN:
                sanitized = username.replace('/', '_');
                break;
            default:
                sanitized = username;
        }
        
        return sanitized;
    }
    
    public static int getCurrencyId(final int swapServicesCurrencyId) {
        int currencyId = 0;
        
        switch (swapServicesCurrencyId) {
            case 102:
                currencyId = SModSysConsts.CFGU_CUR_MXN;
                break;
            case 152:
                currencyId = SModSysConsts.CFGU_CUR_USD;
                break;
            case 51:
                currencyId = SModSysConsts.CFGU_CUR_EUR;
                break;
            case 54:
                currencyId = SModSysConsts.CFGU_CUR_GBP;
                break;
            default:
                // nothing
        }
        
        return currencyId;
    }
    
    /**
     * SOM Settings.
     */
    public static class SomSettings {
        
        /** Number of settings. */
        private static final int SETTINGS = 8;
        private static final int INPUT_CATEGORY_SETTINGS = 3;
        
        public boolean LinkUp;
        public String DbmsHost;
        public String DbmsPort;
        public String DbName;
        public String DbmsUser;
        public String DbmsPswd;
        public Date Start;
        public int InputCategoryId;
        public int FunctionalSubAreaId;
        public String CfdiUsage;
        public int OwnerUserId;
        
        /**
         * Create a new SOM Settings instance.
         * @param configParamValue Configuration parameter value in format: link-up=0; host=host; port=port; db=db; user=user; pswd=pswd; start=yyyy-mm-dd; inp_ct:1=func_sub:1,usage:S01,owner:1
         * @throws java.lang.Exception
         */
        public SomSettings(final String configParamValue) throws Exception {
            reset();
            
            String[] values = configParamValue.replaceAll(" ", "").split(";");
            
            if (values.length != SETTINGS) {
                throw new Exception("El número esperado de configuraciones SOM es " + SETTINGS + ", pero se " + (values.length == 1 ? "obtuvo 1" : "obtuvieron " + values.length) + ".");
            }
            else {
                VALUES:
                for (int index = 0; index < values.length; index++) {
                    String[] keyValuePair = values[index].split("=");
                    String key = keyValuePair[0];
                    String value = keyValuePair[1];
                    String config = "?";
                    boolean isValid = false;

                    switch (index) {
                        case 0:
                            config = "link-up";
                            if (isValid = key.equalsIgnoreCase(config)) {
                                LinkUp = value.equals("1");
                                if (!LinkUp) {
                                    break VALUES; // abort processing, it's worthless
                                }
                            }
                            break;
                        case 1:
                            config = "host";
                            if (isValid = key.equalsIgnoreCase(config)) {
                                DbmsHost = value;
                            }
                            break;
                        case 2:
                            config = "port";
                            if (isValid = key.equalsIgnoreCase(config)) {
                                DbmsPort = value;
                            }
                            break;
                        case 3:
                            config = "db";
                            if (isValid = key.equalsIgnoreCase(config)) {
                                DbName = value;
                            }
                            break;
                        case 4:
                            config = "user";
                            if (isValid = key.equalsIgnoreCase(config)) {
                                DbmsUser = value;
                            }
                            break;
                        case 5:
                            config = "pswd";
                            if (isValid = key.equalsIgnoreCase(config)) {
                                DbmsPswd = value;
                            }
                            break;
                        case 6:
                            config = "start";
                            if (isValid = key.equalsIgnoreCase(config)) {
                                Start = SLibUtils.IsoFormatDate.parse(value);
                            }
                            break;
                        case 7:
                            config = "inp_ct";
                            if (isValid = key.startsWith(config)) {
                                keyValuePair = key.split(":");
                                InputCategoryId = SLibUtils.parseInt(keyValuePair[1]);

                                String[] valuesInpCt = value.split(",");

                                if (valuesInpCt.length != INPUT_CATEGORY_SETTINGS) {
                                    throw new Exception("El número esperado de configuraciones Categoría de Insumo SOM es " + INPUT_CATEGORY_SETTINGS + ", pero se " + (valuesInpCt.length == 1 ? "obtuvo 1" : "obtuvieron " + valuesInpCt.length) + ".");
                                }
                                else {
                                    for (int indexInpCt = 0; indexInpCt < valuesInpCt.length; indexInpCt++) {
                                        keyValuePair = valuesInpCt[indexInpCt].split(":");
                                        String keyInpCt = keyValuePair[0];
                                        String valueInpCt = keyValuePair[1];
                                        String configInpCt = "?";
                                        boolean isValidInpCt = false;
                                        
                                        switch (indexInpCt) {
                                            case 0:
                                                configInpCt = "func_sub";
                                                if (isValidInpCt = keyInpCt.equalsIgnoreCase(configInpCt)) {
                                                    FunctionalSubAreaId = SLibUtils.parseInt(valueInpCt);
                                                }
                                                break;
                                            case 1:
                                                configInpCt = "usage";
                                                if (isValidInpCt = keyInpCt.equalsIgnoreCase(configInpCt)) {
                                                    CfdiUsage = valueInpCt;
                                                }
                                                break;
                                            case 2:
                                                configInpCt = "owner";
                                                if (isValidInpCt = keyInpCt.equalsIgnoreCase(configInpCt)) {
                                                    OwnerUserId = SLibUtils.parseInt(valueInpCt);
                                                }
                                                break;
                                            default:
                                                // nothing
                                        }
                                        
                                        if (!isValidInpCt) {
                                            throw new Exception("Configuración Categoría Insumo SOM inválida en la posición " + (indexInpCt + 1) + ": '" + keyInpCt + "' en vez de '" + configInpCt + "'.");
                                        }
                                    }
                                }
                            }
                            break;
                        default:
                            // nothing
                    }

                    if (!isValid) {
                        throw new Exception("Configuración SOM inválida en la posición " + (index + 1) + ": '" + key + "' en vez de '" + config + "'.");
                    }
                }
            }
        }
        
        private void reset() {
            LinkUp = false;
            DbmsHost = "";
            DbmsPort = "";
            DbName = "";
            DbmsUser = "";
            DbmsPswd = "";
            Start = null;
            InputCategoryId = 0;
            FunctionalSubAreaId = 0;
            CfdiUsage = "";
            OwnerUserId = 0;
        }
    }
}
