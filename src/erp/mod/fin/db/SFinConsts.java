/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

/**
 *
 * @author Sergio Flores, Edwin Carmona
 */
public class SFinConsts {

    public static final int LAY_BANK_HSBC = 1;
    public static final int LAY_BANK_SANTANDER = 2;
    public static final int LAY_BANK_BANBAJIO = 3;
    public static final int LAY_BANK_BBVA = 4;
    public static final int LAY_BANK_BANAMEX = 5;

    public static final java.lang.String TXT_LAY_BANK_HSBC = "LAYOUT HSBC";
    public static final java.lang.String TXT_LAY_BANK_SANTANDER = "LAYOUT SANTANDER";
    public static final java.lang.String TXT_LAY_BANK_BANBAJIO = "LAYOUT BANBAJIO";
    public static final java.lang.String TXT_LAY_BANK_BBVA = "LAYOUT BBVA";
    public static final java.lang.String TXT_LAY_BANK_BANAMEX = "LAYOUT BANAMEX";
    
    public static final java.lang.String TXT_VAL = "VALUACIÓN";
    
    public static final int LAY_BANK_TYPE_DPS = 1;
    public static final int LAY_BANK_TYPE_ADV = 2;
    
    public static final int LAY_BANK_NEW_ST = 0;
    public static final int LAY_BANK_APPROVED_ST = 1;
    
    public static final int LAY_TP_BANK_DEP_CIE = 11;
    
    public static final String LAY_BANK_NEW_TEXT_ST = "NUEVO";
    public static final String LAY_BANK_APPROVED_TEXT_ST = "APROBADO";

    public static final String TXT_BANK_BANK = "BCO.";
    public static final String TXT_BANK_BRA = "SUC.";
    public static final String TXT_BANK_ACC = "CTA.";
    public static final String TXT_CHECK = "CHQ";
    public static final String TXT_INVOICE = "FAC";

    public static final String RFC_DOM = "XAXX010101000";
    public static final String RFC_INT = "XEXX010101000";
    
    public static final int RFC_PER_LEN = 13;
    public static final int RFC_COM_LEN = 12;
    public static final int CURP_LEN = 18;

    public static final String MSG_ERR_ABP_ENT_NOT_FOUND = "No se encontró un paquete de configuración contable para la entidad: ";
    public static final String MSG_ERR_ABP_BPS_NOT_FOUND = "No se encontró un paquete de configuración contable para el asociado de negocios: ";
    public static final String MSG_ERR_ABP_ITEM_NOT_FOUND = "No se encontró un paquete de configuración contable para el ítem: ";
    public static final String MSG_ERR_ABP_TAX_NOT_FOUND = "No se encontró un paquete de configuración contable para el impuesto: ";
    
    public static final String MSG_ERR_GUI_CFG_DIFF_ACC = "En la configuración de la empresa no se ha definido la cuenta contable para diferencias cambiarias.";
    public static final String MSG_ERR_GUI_CFG_DIFF_CC = "En la configuración de la empresa no se ha definido el centro de costos para diferencias cambiarias.";
    public static final String MSG_ERR_GUI_CFG_DIFF_ITEM = "En la configuración de la empresa no se ha definido el ítem (ingresos/egresos) para diferencias cambiarias.";
}
