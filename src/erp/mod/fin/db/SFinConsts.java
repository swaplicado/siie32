/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

/**
 *
 * @author Sergio Flores
 */
public class SFinConsts {

    public static final int LAY_BANK_HSBC = 1;
    public static final int LAY_BANK_SANTANDER = 2;
    public static final int LAY_BANK_BANBAJIO = 3;
    public static final int LAY_BANK_BBVA = 4;

    public static final java.lang.String TXT_LAY_BANK_HSBC = "LAYOUT HSBC";
    public static final java.lang.String TXT_LAY_BANK_SANTANDER = "LAYOUT SANTANDER";
    public static final java.lang.String TXT_LAY_BANK_BANBAJIO = "LAYOUT BANBAJIO";
    public static final java.lang.String TXT_LAY_BANK_BBVA = "LAYOUT BBVA";
    
    public static final int LAY_BANK_TYPE_DPS = 1;
    public static final int LAY_BANK_TYPE_ADV = 2;

    public static final String TXT_BANK_BANK = "BCO.";
    public static final String TXT_BANK_BRA = "SUC.";
    public static final String TXT_BANK_ACC = "CTA.";
    public static final String TXT_CHECK = "CHQ";
    public static final String TXT_INVOICE = "FAC";

    public static final String RFC_DOM = "XAXX010101000";
    public static final String RFC_INT = "XEXX010101000";

    public static final String ERR_MSG_ABP_ENT_NOT_FOUND = "No se encontró un paquete de configuración contable para la entidad: ";
    public static final String ERR_MSG_ABP_BPS_NOT_FOUND = "No se encontró un paquete de configuración contable para el asociado de negocios: ";
    public static final String ERR_MSG_ABP_ITEM_NOT_FOUND = "No se encontró un paquete de configuración contable para el ítem: ";
    public static final String ERR_MSG_ABP_TAX_NOT_FOUND = "No se encontró un paquete de configuración contable para el impuesto: ";
}
