/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap;

import erp.mod.SModSysConsts;
import sa.lib.SLibConsts;

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
}
