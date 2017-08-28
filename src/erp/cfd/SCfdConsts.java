/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.cfd;

/**
 *
 * @author Juan Barajas, Sergio Flores, Alfredo Pérez
 */
public abstract class SCfdConsts {
    
    public static final int UNDEFINED = 0;

    public static final String UNIDENTIFIED = "NO IDENTIFICADO";

    public static final String UUID_ANNUL = "201";
    public static final String UUID_ANNUL_PREV = "202";
    public static final String UUID_STAMP_PREV_NOT = "603";
    public static final String UUID_STAMP_PREV = "307";
    
    public static final int PROC_REQ_STAMP = 1;
    public static final int PROC_REQ_ANNUL = 2;
    public static final int PROC_PRT_DOC = 3;
    public static final int PROC_PRT_ACK_ANNUL = 4;
    public static final int PROC_SND_DOC = 5;
    public static final int PROC_REQ_STAMP_AND_SND = 6;
    public static final int PROC_REQ_ANNUL_AND_SND = 7;
    public static final int PROC_REQ_VERIFY = 8;
    public static final int PROC_REQ_SND_RCP = 9;
    public static final int PROC_PRT_DOCS = 10;
    
    public static final int CFDI_PAYROLL_PERCEPTION = 1;
    public static final int CFDI_PAYROLL_DEDUCTION = 2;

    public static final int CFDI_PAYROLL_VER_OLD = 101;
    public static final int CFDI_PAYROLL_VER_CUR = 102;

    public static final int[] CFDI_PAYROLL_PERCEPTION_PERCEPTION = new int[] { 1, 1 };
    public static final int[] CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE = new int[] { 1, 101 };
    public static final int[] CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_TRIPLE = new int[] { 1, 102 };
    public static final int[] CFDI_PAYROLL_DEDUCTION_DEDUCTION = new int[] { 2, 1 };
    public static final int[] CFDI_PAYROLL_DEDUCTION_INCAPACITY_RISK = new int[] { 2, 101 };
    public static final int[] CFDI_PAYROLL_DEDUCTION_INCAPACITY_DISEASE = new int[] { 2, 102 };
    public static final int[] CFDI_PAYROLL_DEDUCTION_INCAPACITY_MATERNITY = new int[] { 2, 103 };

    public static final String CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE = "Dobles";
    public static final String CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE = "Triples";
    
    public static final String CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE_COD = "01";
    public static final String CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE_COD = "02";

    public static final String PAYROLL_PER_ISR = "001";
    public static final String PAYROLL_PER_SUB_EMP = "002";

    public static final String PAYROLL_EXTRA_TIME = "002";
    public static final String PAYROLL_EXTRA_TIME_TYPE_DOUBLE = "003";
    public static final String PAYROLL_EXTRA_TIME_TYPE_TRIPLE = "004";

    public static final String ERR_MSG_PROCESSING_WEB_SERVICE = "No se obtuvo respuesta del Proveedor Autorizado de Certificación (PAC).";
    public static final String ERR_MSG_PROCESSING_XML_STORAGE = "No se pudo almacenar el archivo XML en el disco.";
    public static final String ERR_MSG_PROCESSING_PDF_STORAGE = "No se pudo almacenar el archivo PDF en el disco.";

    public static final int CER_EXP_DAYS_WARN = 30;     // days before certificate expiration to show warning message

    public static final int CFDI_STAMP_DELAY_DAYS = 3;  // maximum delay days for CFDI stamping

    public static final int CFDI_FILE_XML = 1;
    public static final int CFDI_FILE_PDF = 2;

    public static final int ADD_SORIANA_NOR = 1;
    public static final int ADD_SORIANA_EXT = 2;
    
    public static final int ACTION_SIGN = 1;
    public static final int ACTION_ANNUL = 2;
    public static final int ACTION_RESTORE_SIGN = 3;
    public static final int ACTION_RESTORE_ANNUL = 4;
    public static final int ACTION_DIACTIVATE = 9;

    public static final int STATUS_NA = 0;
    public static final int STATUS_AUTHENTICATION_PAC = 1;
    public static final int STATUS_ACTIVATE = 2;
    public static final int STATUS_SEND_RECEIVE = 3;
    public static final int STATUS_RECEIVE_ERR_PAC = 4;
    public static final int STATUS_SAVE = 5;
    public static final int STATUS_DIACTIVATE_PAC = 6;
    public static final int STATUS_CONSUME_STAMP = 7;

    public static final String ACTION_MSG_SIGN = "Timbrar CFDI.";
    public static final String ACTION_MSG_ANNUL = "Cancelar CFDI.";
    public static final String ACTION_MSG_RESTORE_SIGN = "Verificar timbrado CFDI.";
    public static final String ACTION_MSG_RESTORE_ANNUL = "Verificar cancelación CFDI.";
    public static final String ACTION_MSG_CONSISTENT = "Desactivar banderas CFDI.";

    public static final String STATUS_MSG_NA = "N/A.";
    public static final String STATUS_MSG_AUTHENTICATION_PAC = "Autenticación PAC.";
    public static final String STATUS_MSG_ACTIVATE = "Activación banderas 'pendiente PAC', 'pendiente XML en disco' y 'pendiente PDF en disco'.";
    public static final String STATUS_MSG_SEND_RECEIVE = "Enviar CFDI a timbrar/cancelar y recibir CFDI timbrado/cancelado.";
    public static final String STATUS_MSG_RECEIVE_ERR_PAC = "Recepción mensaje error PAC.";
    public static final String STATUS_MSG_SAVE = "Guardado registro de CFDI timbrado/cancelado.";
    public static final String STATUS_MSG_DIACTIVATE_PAC = "Desactivación bandera 'pendiente PAC'.";
    public static final String STATUS_MSG_CONSUME_STAMP = "Consumo timbre.";
}
