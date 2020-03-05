/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

/**
 *
 * @author Sergio Flores, Edwin Carmona, Sergio Flores
 */
public class SFinConsts {

    public static final int LAY_BANK_HSBC = 1;
    public static final int LAY_BANK_SANT = 2;
    public static final int LAY_BANK_BBAJ = 3;
    public static final int LAY_BANK_BBVA = 4;
    public static final int LAY_BANK_CITI = 5;

    public static final java.lang.String TXT_LAY_BANK_HSBC = "LAYOUT HSBC";
    public static final java.lang.String TXT_LAY_BANK_SANT = "LAYOUT SANTANDER";
    public static final java.lang.String TXT_LAY_BANK_BBAJ = "LAYOUT BANBAJIO";
    public static final java.lang.String TXT_LAY_BANK_BBVA = "LAYOUT BBVA";
    public static final java.lang.String TXT_LAY_BANK_CITI = "LAYOUT CITIBANAMEX";
    
    public static final int LAY_TP_BANK_DEP_CIE = 11;
    
    public static final String TXT_BANK_BANK = "BCO.";
    public static final String TXT_BANK_BRA = "SUC.";
    public static final String TXT_BANK_ACC = "CTA.";
    public static final String TXT_CHECK = "CHQ";
    public static final String TXT_INVOICE = "F";

    public static final String MSG_ERR_ABP_ENT_NOT_FOUND = "No se encontró un paquete de configuración contable para la entidad: ";
    public static final String MSG_ERR_ABP_BPS_NOT_FOUND = "No se encontró un paquete de configuración contable para el asociado de negocios: ";
    public static final String MSG_ERR_ABP_ITEM_NOT_FOUND = "No se encontró un paquete de configuración contable para el ítem: ";
    public static final String MSG_ERR_ABP_TAX_NOT_FOUND = "No se encontró un paquete de configuración contable para el impuesto: ";
    
    public static final String MSG_ERR_GUI_CFG_DIFF_ACC = "En la configuración de la empresa no se ha definido la cuenta contable para diferencias cambiarias.";
    public static final String MSG_ERR_GUI_CFG_DIFF_CC = "En la configuración de la empresa no se ha definido el centro de costos para diferencias cambiarias.";
    public static final String MSG_ERR_GUI_CFG_DIFF_ITEM = "En la configuración de la empresa no se ha definido el ítem (ingresos/egresos) para diferencias cambiarias.";
}
