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
    /*
        Código	Validación de la cancelación del CFD
        201	UUID Cancelado exitosamente
        202	UUID Previamente cancelado
        203	UUID No corresponde el RFC del Emisor y de quien solicita la cancelación
        205	UUID No existe

        Código	Validación de timbrado y cancelación del CFD
        300	Usuario y contraseña inválidos
        301	XML mas formado
        302	Sello mal formado o inválido
        303	Sello no corresponde a emisor
        304	Certificado Revocado o caduco
        305	La fecha de emisión no esta dentro de la vigencia del CSD del Emisor
        306	El certificado no es de tipo CSD
        307	El CFDI contiene un timbre previo
        308	Certificado no expedido por el SAT

        Código	Validación de negocio de CFD
        401	Fecha y hora de generación fuera de rango
        402	RFC del emisor no se encuentra en el régimen de contribuyentes
        403	La fecha de emisión no es posterior al 01 de enero de 2012

        Código	Validación Finkok 
        501	Autenticación no válida
        703	Cuenta suspendida
        704	Error con la contraseña de la llave Privada
        705	XML estructura inválida
        706	Socio Inválido
        707	XML ya contiene un nodo TimbreFiscalDigital
        708	No se pudo conectar al SAT
	
	En caso de recibir las incidencias con código de error 501 y/o 708, el cfdi fue timbrado y se regresa
	el xml con el nodo TimbreFiscalDigital, pero significa que el CFDI no ha sido enviado al SAT, utilizar 
	los otros metodos query_pending y query_pending_cancellation
    */

    public static final String UUID_CANCEL_OK = "201";
    public static final String UUID_CANCEL_ALREADY = "202";
    public static final String UUID_STAMP_NOT_YET = "603"; // not in the list above!
    public static final String UUID_STAMP_ALREADY = "307";
    
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
    public static final int[] CFDI_PAYROLL_PERCEPTION_TAX_SUBSIDY = new int[] { 1, 103 };
    public static final int[] CFDI_PAYROLL_DEDUCTION_DEDUCTION = new int[] { 2, 1 };
    public static final int[] CFDI_PAYROLL_DEDUCTION_INCAPACITY_RISK = new int[] { 2, 101 };
    public static final int[] CFDI_PAYROLL_DEDUCTION_INCAPACITY_DISEASE = new int[] { 2, 102 };
    public static final int[] CFDI_PAYROLL_DEDUCTION_INCAPACITY_MATERNITY = new int[] { 2, 103 };

    public static final String CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE = "Dobles";
    public static final String CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE = "Triples";
    
    public static final String CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE_CODE = "01";
    public static final String CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE_CODE = "02";

    public static final String CFDI_OTHER_PAY_TAX_SUBSIDY = "Subsidio para el empleo (efectivamente entregado al trabajador).";

    public static final String PAYROLL_PER_ISR = "001";
    public static final String PAYROLL_PER_SUB_EMP = "002";

    public static final String PAYROLL_EXTRA_TIME = "002";
    public static final String PAYROLL_EXTRA_TIME_TYPE_DOUBLE = "003";
    public static final String PAYROLL_EXTRA_TIME_TYPE_TRIPLE = "004";

    public static final String ERR_MSG_PROCESS_WS_PAC = "Una solicitud de timbrado o cancelación está en proceso:\nNo se obtuvo respuesta del Proveedor Autorizado de Certificación (PAC).";
    public static final String ERR_MSG_PROCESS_XML_STORAGE = "Una solicitud de timbrado o cancelación está en proceso:\nNo se pudo almacenar el archivo XML del CFDI en el disco.";
    public static final String ERR_MSG_PROCESS_PDF_STORAGE = "Una solicitud de timbrado o cancelación está en proceso:\nNo se pudo almacenar el archivo PDF del CFDI en el disco.";

    public static final int CER_EXP_DAYS_WARN = 30;     // days before certificate expiration to show warning message

    public static final int CFDI_STAMP_DELAY_DAYS = 3;  // maximum delay days for CFDI stamping

    public static final int CFDI_FILE_XML = 1;
    public static final int CFDI_FILE_PDF = 2;

    public static final int ADD_SORIANA_NOR = 1;
    public static final int ADD_SORIANA_EXT = 2;
    
    public static final int ACTION_CODE_PRC_SIGN = 1;      // process signature
    public static final int ACTION_CODE_PRC_ANNUL = 2;     // process annulment
    public static final int ACTION_CODE_VAL_SIGN = 3;      // restore signature
    public static final int ACTION_CODE_VAL_ANNUL = 4;     // restore annulment
    public static final int ACTION_CODE_RESET_FLAGS = 9;   // reset CFDI processing flags

    public static final int STEP_CODE_NA = 0;
    public static final int STEP_CODE_PAC_AUTH = 1;
    public static final int STEP_CODE_FLAGS_SET = 2;
    public static final int STEP_CODE_SEND_RECV = 3;
    public static final int STEP_CODE_PAC_RECV_ERR = 4;
    public static final int STEP_CODE_CFD_SAVED = 5;
    public static final int STEP_CODE_PAC_FLAG_CLEAR = 6;
    public static final int STEP_CODE_CONSUME_STAMP = 7;
}
