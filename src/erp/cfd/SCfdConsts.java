/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.cfd;

import cfd.DAttributeOptionFormaPago;
import erp.data.SDataConstantsSys;
import java.util.HashMap;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public abstract class SCfdConsts {

    public static final HashMap<Integer, String> RegimenMap = new HashMap<Integer, String>();
    public static final HashMap<Integer, String> BancoMap = new HashMap<Integer, String>();
    public static final HashMap<Integer, String> RiesgoMap = new HashMap<Integer, String>();
    public static final HashMap<Integer, String> PercepcionMap = new HashMap<Integer, String>();
    public static final HashMap<Integer, String> DeduccionMap = new HashMap<Integer, String>();
    public static final HashMap<Integer, String> IncapacidadMap = new HashMap<Integer, String>();
    
    public static final HashMap<String, Integer> FormaPagoIdsMap = new HashMap<>(); // method of payment

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

    public static final int CFD_TYPE_DPS = 1;
    public static final int CFD_TYPE_PAYROLL = 2;
    public static final int CFD_TYPE_RECORD = 3;

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

    public static final int CFDI_STAMP_DELAY_DAYS = 2;  // maximum dealy days to CFDI stamping

    public static final int CFDI_FILE_XML = 1;
    public static final int CFDI_FILE_PDF = 2;

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

    public static final int REG_SAL = 2;
    public static final int REG_JUB = 3;
    public static final int REG_PEN = 4;
    public static final int REG_ASI_SAL_COO = 5;
    public static final int REG_ASI_SAL_ASO = 6;
    public static final int REG_ASI_SAL_CON = 7;
    public static final int REG_ASI_SAL_EMP = 8;
    public static final int REG_ASI_SAL_HON = 9;
    public static final int REG_ASI_SAL_TIT = 10;

    public static final int BCO_BMX = 2;
    public static final int BCO_BBV = 12;
    public static final int BCO_SAN = 14;
    public static final int BCO_HSB = 21;
    public static final int BCO_BAJ = 30;
    public static final int BCO_INB = 36;
    public static final int BCO_SCO = 44;
    public static final int BCO_AFI = 62;
    public static final int BCO_BNT = 72;
    public static final int BCO_AME = 103;

    public static final int RGO_CLA_1 = 1;
    public static final int RGO_CLA_2 = 2;
    public static final int RGO_CLA_3 = 3;
    public static final int RGO_CLA_4 = 4;
    public static final int RGO_CLA_5 = 5;

    public static final int PER_SAL = 1;
    public static final int PER_GRA_ANU = 2;
    public static final int PER_PTU = 3;
    public static final int PER_RMB_MED = 4;
    public static final int PER_AHO_FON = 5;
    public static final int PER_AHO_CAJ = 6;
    public static final int PER_PAT_CON = 9;
    public static final int PER_PRE_PUN = 10;
    public static final int PER_PRI_VID = 11;
    public static final int PER_SEG_MED = 12;
    public static final int PER_PAT_CTA_SIN = 13;
    public static final int PER_SUB_INC = 14;
    public static final int PER_BEC = 15;
    public static final int PER_OTR = 16;
    public static final int PER_SUB_EMP = 17;
    public static final int PER_HRS_EXT = 19;
    public static final int PER_PRI_DOM = 20;
    public static final int PER_PRI_VAC = 21;
    public static final int PER_PRI_ANT = 22;
    public static final int PER_FIN = 23;
    public static final int PER_SEG_RET = 24;
    public static final int PER_IND = 25;
    public static final int PER_RMB_FUN = 26;
    public static final int PER_PAT_SEG_SOC = 27;
    public static final int PER_COM = 28;
    public static final int PER_VAL_DES = 29;
    public static final int PER_VAL_RES = 30;
    public static final int PER_VAL_GAS = 31;
    public static final int PER_VAL_ROP = 32;
    public static final int PER_AYU_REN = 33;
    public static final int PER_AYU_ESC = 34;
    public static final int PER_AYU_ANT = 35;
    public static final int PER_AYU_TRA = 36;
    public static final int PER_AYU_FUN = 37;
    public static final int PER_OTR_ING = 38;
    public static final int PER_JUB = 39;

    public static final int DED_SEG_SOC = 1;
    public static final int DED_ISR = 2;
    public static final int DED_RET = 3;
    public static final int DED_OTH = 4;
    public static final int DED_INF = 5;
    public static final int DED_INC = 6;
    public static final int DED_PEN = 7;
    public static final int DED_REN = 8;
    public static final int DED_PRE_INF = 9;
    public static final int DED_ABO_INF = 10;
    public static final int DED_ABO_FON = 11;
    public static final int DED_ANT_SAL = 12;
    public static final int DED_PAG_EXC = 13;
    public static final int DED_ERR = 14;
    public static final int DED_PER = 15;
    public static final int DED_AVE = 16;
    public static final int DED_CPA = 17;
    public static final int DED_CTA_CON_COO = 18;
    public static final int DED_CTA_SIN = 19;
    public static final int DED_AUS = 20;
    public static final int DED_CUO_OBR_PAT = 21;

    public static final int INC_RGO = 1;
    public static final int INC_ENF = 2;
    public static final int INC_MAT = 3;

    static {
        RegimenMap.put(REG_SAL, "Sueldos y salarios");
        RegimenMap.put(REG_JUB, "Jubilados");
        RegimenMap.put(REG_PEN, "Pensionados");
        RegimenMap.put(REG_ASI_SAL_COO, "Asimilados a salarios, Miembros de las Sociedades Cooperativas de Producción.");
        RegimenMap.put(REG_ASI_SAL_ASO, "Asimilados a salarios, Integrantes de Sociedades y Asociaciones Civiles.");
        RegimenMap.put(REG_ASI_SAL_CON, "Asimilados a salarios, Miembros de consejos directivos, de vigilancia, consultivos, honorarios a administradores, comisarios y gerentes generales.");
        RegimenMap.put(REG_ASI_SAL_EMP, "Asimilados a salarios, Actividad empresarial (comisionistas)");
        RegimenMap.put(REG_ASI_SAL_HON, "Asimilados a salarios, Honorarios asimilados a salarios");
        RegimenMap.put(REG_ASI_SAL_TIT, "Asimilados a salarios, Ingresos acciones o títulos valor");

        BancoMap.put(BCO_BMX, "BANAMEX");
        BancoMap.put(BCO_BBV, "BBVA BANCOMER");
        BancoMap.put(BCO_SAN, "SANTANDER");
        BancoMap.put(BCO_HSB, "HSBC");
        BancoMap.put(BCO_BAJ, "BAJIO");
        BancoMap.put(BCO_INB, "INBURSA");
        BancoMap.put(BCO_SCO, "SCOTIABANK");
        BancoMap.put(BCO_AFI, "AFIRME");
        BancoMap.put(BCO_BNT, "BANORTE");
        BancoMap.put(BCO_AME, "AMERICAN EXPRESS");

        RiesgoMap.put(RGO_CLA_1, "Clase I");
        RiesgoMap.put(RGO_CLA_2, "Clase II");
        RiesgoMap.put(RGO_CLA_3, "Clase III");
        RiesgoMap.put(RGO_CLA_4, "Clase IV");
        RiesgoMap.put(RGO_CLA_5, "Clase V");

        PercepcionMap.put(PER_SAL, "Sueldos, Salarios  Rayas y Jornales");
        PercepcionMap.put(PER_GRA_ANU, "Gratificación Anual (Aguinaldo)");
        PercepcionMap.put(PER_PTU, "Participación de los Trabajadores en las Utilidades PTU");
        PercepcionMap.put(PER_RMB_MED, "Reembolso de Gastos Médicos Dentales y Hospitalarios");
        PercepcionMap.put(PER_AHO_FON, "Fondo de Ahorro");
        PercepcionMap.put(PER_AHO_CAJ, "Caja de ahorro");
        PercepcionMap.put(PER_PAT_CON, "Contribuciones a Cargo del Trabajador Pagadas por el Patrón");
        PercepcionMap.put(PER_PRE_PUN, "Premios por puntualidad");
        PercepcionMap.put(PER_PRI_VID, "Prima de Seguro de vida");
        PercepcionMap.put(PER_SEG_MED, "Seguro de Gastos Médicos Mayores");
        PercepcionMap.put(PER_PAT_CTA_SIN, "Cuotas Sindicales Pagadas por el Patrón");
        PercepcionMap.put(PER_SUB_INC, "Subsidios por incapacidad");
        PercepcionMap.put(PER_BEC, "Becas para trabajadores y/o hijos");
        PercepcionMap.put(PER_OTR, "Otros");
        PercepcionMap.put(PER_SUB_EMP, "Subsidio para el empleo");
        PercepcionMap.put(PER_HRS_EXT, "Horas extra");
        PercepcionMap.put(PER_PRI_DOM, "Prima dominical");
        PercepcionMap.put(PER_PRI_VAC, "Prima vacacional");
        PercepcionMap.put(PER_PRI_ANT, "Prima por antigüedad");
        PercepcionMap.put(PER_FIN, "Pagos por separación");
        PercepcionMap.put(PER_SEG_RET, "Seguro de retiro");
        PercepcionMap.put(PER_IND, "Indemnizaciones");
        PercepcionMap.put(PER_RMB_FUN, "Reembolso por funeral");
        PercepcionMap.put(PER_PAT_SEG_SOC, "Cuotas de seguridad social pagadas por el patrón");
        PercepcionMap.put(PER_COM, "Comisiones");
        PercepcionMap.put(PER_VAL_DES, "Vales de despensa");
        PercepcionMap.put(PER_VAL_RES, "Vales de restaurante");
        PercepcionMap.put(PER_VAL_GAS, "Vales de gasolina");
        PercepcionMap.put(PER_VAL_ROP, "Vales de ropa");
        PercepcionMap.put(PER_AYU_REN, "Ayuda para renta");
        PercepcionMap.put(PER_AYU_ESC, "Ayuda para artículos escolares");
        PercepcionMap.put(PER_AYU_ANT, "Ayuda para anteojos");
        PercepcionMap.put(PER_AYU_TRA, "Ayuda para transporte");
        PercepcionMap.put(PER_AYU_FUN, "Ayuda para gastos de funeral");
        PercepcionMap.put(PER_OTR_ING, "Otros ingresos por salarios");
        PercepcionMap.put(PER_JUB, "Jubilaciones, pensiones o haberes de retiro");

        DeduccionMap.put(DED_SEG_SOC, "Seguridad social");
        DeduccionMap.put(DED_ISR, "ISR");
        DeduccionMap.put(DED_RET, "Aportaciones a retiro, cesantía en edad avanzada y vejez.");
        DeduccionMap.put(DED_OTH, "Otros");
        DeduccionMap.put(DED_INF, "Aportaciones a Fondo de vivienda");
        DeduccionMap.put(DED_INC, "Descuento por incapacidad");
        DeduccionMap.put(DED_PEN, "Pensión alimenticia");
        DeduccionMap.put(DED_REN, "Renta");
        DeduccionMap.put(DED_PRE_INF, "Préstamos provenientes del Fondo Nacional de la Vivienda para los Trabajadores");
        DeduccionMap.put(DED_ABO_INF, "Pago por crédito de vivienda");
        DeduccionMap.put(DED_ABO_FON, "Pago de abonos INFONACOT");
        DeduccionMap.put(DED_ANT_SAL, "Anticipo de salarios");
        DeduccionMap.put(DED_PAG_EXC, "Pagos hechos con exceso al trabajador");
        DeduccionMap.put(DED_ERR, "Errores");
        DeduccionMap.put(DED_PER, "Pérdidas");
        DeduccionMap.put(DED_AVE, "Averías");
        DeduccionMap.put(DED_CPA, "Adquisición de artículos producidos por la empresa o establecimiento");
        DeduccionMap.put(DED_CTA_CON_COO, "Cuotas para la constitución y fomento de sociedades cooperativas y de cajas de ahorro");
        DeduccionMap.put(DED_CTA_SIN, "Cuotas sindicales");
        DeduccionMap.put(DED_AUS, "Ausencia (Ausentismo)");
        DeduccionMap.put(DED_CUO_OBR_PAT, "Cuotas obrero patronales");

        IncapacidadMap.put(INC_RGO, "Riesgo de trabajo");
        IncapacidadMap.put(INC_ENF, "Enfermedad en general");
        IncapacidadMap.put(INC_MAT, "Maternidad");
        
        DAttributeOptionFormaPago formaPago = new DAttributeOptionFormaPago("", false);
        
        FormaPagoIdsMap.put(formaPago.getOptions().get(DAttributeOptionFormaPago.CFD_NO_APLICA), SDataConstantsSys.TRNU_TP_PAY_SYS_NA);
        FormaPagoIdsMap.put(formaPago.getOptions().get(DAttributeOptionFormaPago.CFD_EFECTIVO), SDataConstantsSys.TRNU_TP_PAY_SYS_CASH);
        FormaPagoIdsMap.put(formaPago.getOptions().get(DAttributeOptionFormaPago.CFD_CHEQUE_NOMINATIVO), SDataConstantsSys.TRNU_TP_PAY_SYS_CHECK);
        FormaPagoIdsMap.put(formaPago.getOptions().get(DAttributeOptionFormaPago.CFD_TRANSFERENCIA_ELECTRONICA), SDataConstantsSys.TRNU_TP_PAY_SYS_TRANSF);
        FormaPagoIdsMap.put(formaPago.getOptions().get(DAttributeOptionFormaPago.CFD_TARJETA_DEBITO), SDataConstantsSys.TRNU_TP_PAY_SYS_CARD_DBT);
        FormaPagoIdsMap.put(formaPago.getOptions().get(DAttributeOptionFormaPago.CFD_TARJETA_CREDITO), SDataConstantsSys.TRNU_TP_PAY_SYS_CARD_CDT);
        FormaPagoIdsMap.put(formaPago.getOptions().get(DAttributeOptionFormaPago.CFD_TARJETA_SERVICIO), SDataConstantsSys.TRNU_TP_PAY_SYS_CARD_SRV);
        FormaPagoIdsMap.put(formaPago.getOptions().get(DAttributeOptionFormaPago.CFD_MONEDERO_ELECTRONICO), SDataConstantsSys.TRNU_TP_PAY_SYS_E_PURSE);
        FormaPagoIdsMap.put(formaPago.getOptions().get(DAttributeOptionFormaPago.CFD_DINERO_ELECTRONICO), SDataConstantsSys.TRNU_TP_PAY_SYS_E_MONEY);
        FormaPagoIdsMap.put(formaPago.getOptions().get(DAttributeOptionFormaPago.CFD_VALES_DESPENSA), SDataConstantsSys.TRNU_TP_PAY_SYS_FOOD_STP);
        FormaPagoIdsMap.put(formaPago.getOptions().get(DAttributeOptionFormaPago.CFD_NO_IDENTIFICADO), SDataConstantsSys.TRNU_TP_PAY_SYS_UNDEF);
        FormaPagoIdsMap.put(formaPago.getOptions().get(DAttributeOptionFormaPago.CFD_OTROS), SDataConstantsSys.TRNU_TP_PAY_SYS_OTHER);
    }
}
