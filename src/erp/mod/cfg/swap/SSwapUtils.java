/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap;

import erp.mod.cfg.db.SSyncType;
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
                        translation = "SOCIO_PROVEEDOR";
                        break;
                    case FUNCTIONAL_AREA:
                        translation = "AREA_FUNCIONAL";
                        break;
                    case PURCHASE_ORDER_REF:
                        translation = "REF_PEDIDO_COMPRAS";
                        break;
                    case SCALE_TICKET_REF:
                        translation = "REF_BOLETO_BÁSCULA";
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
}
