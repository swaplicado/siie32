/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public abstract class SLibConstants {

    // General purpouse constants:

    public static final int UNDEFINED = 0;

    public static final int DATA_TYPE_BOOLEAN = 1;
    public static final int DATA_TYPE_INTEGER = 2;
    public static final int DATA_TYPE_LONG = 3;
    public static final int DATA_TYPE_FLOAT = 4;
    public static final int DATA_TYPE_DOUBLE = 5;
    public static final int DATA_TYPE_STRING = 6;
    public static final int DATA_TYPE_DATE = 7;
    public static final int DATA_TYPE_DATE_TIME = 8;
    public static final int DATA_TYPE_TIME = 9;
    public static final int DATA_TYPE_KEY = 10;

    public static final int FIELD_TYPE = 0;
    public static final int FIELD_TYPE_TEXT = 1;
    public static final int FIELD_TYPE_TEXT_AREA = 2;
    public static final int FIELD_TYPE_NUM_INTEGER = 3;
    public static final int FIELD_TYPE_NUM_LONG = 4;
    public static final int FIELD_TYPE_NUM_FLOAT = 5;
    public static final int FIELD_TYPE_NUM_DOUBLE = 6;
    public static final int FIELD_TYPE_DATE = 7;
    public static final int FIELD_TYPE_LIST = 8;
    public static final int FIELD_TYPE_COMBO_BOX = 9;
    public static final int FIELD_TYPE_CHECK_BOX = 10;
    public static final int FIELD_TYPE_FORMATTED_TEXT = 11;
    public static final int FIELD_DELETED = 21;

    public static final int CASE_UPPER = 1;
    public static final int CASE_LOWER = 2;

    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int ALIGN_CENTER = 3;

    public static final int TRUNC_MODE_TRUNC = 1;
    public static final int TRUNC_MODE_HIDE = 2;

    public static final int DESCRIPTION_CODE = 1;
    public static final int DESCRIPTION_NAME = 2;
    public static final int DESCRIPTION_NAME_ABBR = 3;
    public static final int DESCRIPTION_NAME_LAN = 4;

    public static final int LAN_SPANISH = 1;
    public static final int LAN_ENGLISH = 2;

    public static final int DBMS_MY_SQL = 1;
    public static final int DBMS_SQL_SERVER_2000 = 2;
    public static final int DBMS_SQL_SERVER_2005 = 3;

    public static final int ICON_NEW = 1;
    public static final int ICON_NEW_MAIN = 2;
    public static final int ICON_INSERT = 3;
    public static final int ICON_COPY = 4;
    public static final int ICON_EDIT = 5;
    public static final int ICON_ANNUL = 6;
    public static final int ICON_DELETE = 7;
    public static final int ICON_LOOK = 8;
    public static final int ICON_PRINT = 11;
    public static final int ICON_PRINT_ACK_CAN = 12;
    public static final int ICON_PRINT_ORDER = 13;
    public static final int ICON_PRINT_PHOTO = 14;
    public static final int ICON_KARDEX = 21;
    public static final int ICON_NOTES = 22;
    public static final int ICON_DOC_ADD = 41;
    public static final int ICON_DOC_REM = 42;
    public static final int ICON_ACTION = 51;
    public static final int ICON_LINK = 61;
    public static final int ICON_CONTRACT_ANALYSIS = 62;
    public static final int ICON_SUM = 63;
    public static final int ICON_DOC_LINK = 1101;
    public static final int ICON_DOC_LINK_NO = 1102;
    public static final int ICON_DOC_SUPPLY = 1103;
    public static final int ICON_DOC_SUPPLY_NO = 1104;
    public static final int ICON_DOC_TYPE = 1105;
    public static final int ICON_DOC_IMPORT = 1106;
    public static final int ICON_DOC_OPEN = 1107;
    public static final int ICON_DOC_CLOSE = 1108;
    public static final int ICON_DOC_XML = 1109;
    public static final int ICON_DOC_XML_CANCEL = 1110;
    public static final int ICON_DOC_XML_SIGN = 1111;
    public static final int ICON_DOC_DELIVERY = 1112;
    public static final int ICON_DOC_IMPORT_CFD = 1113;
    public static final int ICON_DOC_IMPORT_CFD_ORD = 1114;
    public static final int ICON_DOC_IMPORT_MAT_REQ = 1117;
    public static final int ICON_DOC_XML_DIRECT = 1115;
    public static final int ICON_DOC_XML_INDIRECT = 1116;
    public static final int ICON_FILTER_BP = 1201;
    public static final int ICON_FILTER_BPB = 1202;
    public static final int ICON_FILTER_DOC = 1203;
    public static final int ICON_APPROVE = 1301;
    public static final int ICON_APPROVE_NO = 1302;
    public static final int ICON_QUERY_BP = 1401;
    public static final int ICON_QUERY_DOC = 1402;
    public static final int ICON_QUERY_REC = 1403;
    public static final int ICON_ARROW_UP = 1501;
    public static final int ICON_ARROW_DOWN = 1502;
    public static final int ICON_ARROW_LEFT = 1503;
    public static final int ICON_ARROW_RIGHT = 1504;
    public static final int ICON_BP_EXPORT = 1505;
    public static final int ICON_GUI_CAL = 1601;

    public static final int GUI_DATE_PICKER_DATE = 1;
    public static final int GUI_DATE_PICKER_DATE_PERIOD = 2;

    public static final int GUI_DATE_AS_DATE = 1;
    public static final int GUI_DATE_AS_YEAR_MONTH = 2;
    public static final int GUI_DATE_AS_YEAR = 3;

    public static final int EXEC_MODE_STEALTH = 1;
    public static final int EXEC_MODE_SILENT = 2;
    public static final int EXEC_MODE_VERBOSE = 3;

    public static final int MODE_STK_ASD = 1;   // stock assigned
    public static final int MODE_STK_RET = 2;   // stock returned
    /** Quantity. */
    public static final int MODE_QTY = 1;
    /** Extended quantity. */
    public static final int MODE_QTY_EXT = 2;
    public static final int MODE_AS_SRC = 1;    // as source
    public static final int MODE_AS_DES = 2;    // as destiny

    public static final int LIST_VALIDATION_BY_SELECTION = 1;
    public static final int LIST_VALIDATION_BY_CONTENT = 2;

    public static final int VALUE_PRIMARY_KEY = 1;
    public static final int VALUE_FILTER_KEY = 2;
    public static final int VALUE_YEAR = 3;
    public static final int VALUE_STATUS = 4;
    public static final int VALUE_TEXT = 5;
    public static final int VALUE_TYPE = 6;
    public static final int VALUE_EXC_RATE = 7;
    public static final int VALUE_EXC_RATE_SYS = 8;
    public static final int VALUE_CURRENCY = 9;
    public static final int VALUE_CURRENCY_LOCAL = 10;
    public static final int VALUE_IS_MAT_REQ = 15;
    public static final int VALUE_INV_NOT = 11;
    public static final int VALUE_INV_ONLY = 12;
    public static final int VALUE_IS_COPY = 13;
    public static final int VALUE_IS_IMPORTED = 14;
    public static final int VALUE_POST_EMIT_EDIT = 21;
    
    public static final double RES_VAL_DECS = 0.01;

    // GUI constants:

    public static final int FORM_RESULT_OK = 1;
    public static final int FORM_RESULT_CANCEL = 2;

    public static final int FORM_STATUS_READ_ONLY = 1;
    public static final int FORM_STATUS_EDIT = 2;

    // Login constants:

    public static final int LOGIN_OK = 1;
    public static final int LOGIN_ERROR_USR_INVALID = 101;
    public static final int LOGIN_ERROR_USR_INACTIVE = 102;
    public static final int LOGIN_ERROR_USR_DELETED = 103;
    public static final int LOGIN_ERROR_USR_PSWD_INVALID = 104;
    public static final int LOGIN_ERROR_USR_CO_INVALID = 105;

    // DBMS constants:

    public static final int DB_ACTION_READ_OK = 1101;
    public static final int DB_ACTION_READ_ERROR = 1111;
    public static final int DB_ACTION_SAVE_OK = 1201;
    public static final int DB_ACTION_SAVE_ERROR = 1211;
    public static final int DB_ACTION_ANNUL_OK = 1301;
    public static final int DB_ACTION_ANNUL_ERROR = 1311;
    public static final int DB_ACTION_DELETE_OK = 1401;
    public static final int DB_ACTION_DELETE_ERROR = 1411;
    public static final int DB_ACTION_PROCESS_OK = 1501;
    public static final int DB_ACTION_PROCESS_ERROR = 1511;
    public static final int DB_CAN_READ_NO = 3001;
    public static final int DB_CAN_READ_YES = 3002;
    public static final int DB_CAN_SAVE_NO = 3011;
    public static final int DB_CAN_SAVE_YES = 3012;
    public static final int DB_CAN_ANNUL_NO = 3101;
    public static final int DB_CAN_ANNUL_NO_ANNULED = 3111;
    public static final int DB_CAN_ANNUL_NO_DEPENDENCIES = 3112;
    public static final int DB_CAN_ANNUL_YES = 3121;
    public static final int DB_CAN_DELETE_NO = 3201;
    public static final int DB_CAN_DELETE_NO_DELETED = 3211;
    public static final int DB_CAN_DELETE_NO_DEPENDENCIES = 3212;
    public static final int DB_CAN_DELETE_YES = 3221;
    public static final int DB_QUERY_OK = 1501;
    public static final int DB_QUERY_ERROR = 1511;
    public static final int DB_PROCEDURE_OK = 1601;
    public static final int DB_PROCEDURE_ERROR = 1611;
    public static final int DB_CONNECTION_OK = 1901;
    public static final int DB_CONNECTION_ERROR = 1911;
    public static final int DB_CFD_OK = 2001;
    public static final int DB_CFD_ERROR = 2011;

    // Fixed messages:

    public static final java.lang.String TXT_ALL = "(TODOS)";
    public static final java.lang.String TXT_ALL_FEM = "(TODAS)";
    public static final java.lang.String TXT_NONE = "(NINGUNO)";
    public static final java.lang.String TXT_NONE_FEM = "(NINGUNA)";
    public static final java.lang.String TXT_SYS_CTY = "PAÍS LOCAL";
    public static final java.lang.String TXT_SYS_CUR = "MONEDA LOCAL";
    public static final java.lang.String TXT_BENEF_ACC_DEP = "PARA ABONO EN CUENTA DEL BENEFICIARIO";
    public static final java.lang.String TXT_OK = "Aceptar";
    public static final java.lang.String TXT_CANCEL = "Cancelar";
    public static final java.lang.String TXT_CLOSE = "Cerrar";

    public static final java.lang.String MSG_ERR_DB_REG_READ = "Ocurrió un error al leer el registro.";
    public static final java.lang.String MSG_ERR_DB_REG_READ_DEP = "Ocurrió un error al leer el registro dependiente.";
    public static final java.lang.String MSG_ERR_DB_REG_SAVE = "Ocurrió un error al guardar el registro.";
    public static final java.lang.String MSG_ERR_DB_REG_SAVE_DEP = "Ocurrió un error al guardar el registro dependiente.";
    public static final java.lang.String MSG_ERR_DB_REG_DELETE = "Ocurrió un error al eliminar el registro.";
    public static final java.lang.String MSG_ERR_DB_REG_DELETE_DEP = "Ocurrió un error al eliminar el registro dependiente.";
    public static final java.lang.String MSG_ERR_DB_REG_DELETE_CAN = "Ocurrió un error al verificar si se puede eliminar el registro.";
    public static final java.lang.String MSG_ERR_DB_REG_ANNUL = "Ocurrió un error al anular el registro.";
    public static final java.lang.String MSG_ERR_DB_REG_ANNUL_DEP = "Ocurrió un error al anular el registro dependiente.";
    public static final java.lang.String MSG_ERR_DB_REG_ANNUL_CAN = "Ocurrió un error al verificar si se puede anular el registro.";
    public static final java.lang.String MSG_ERR_DB_REG_PROCESS = "Ocurrió un error al procesar el registro.";
    public static final java.lang.String MSG_ERR_DB_REG_CAN_READ = "Ocurrió un error al validar si puede leer el registro.";
    public static final java.lang.String MSG_ERR_DB_REG_CAN_SAVE = "Ocurrió un error al validar si puede guardar el registro.";
    public static final java.lang.String MSG_ERR_DB_REG_CAN_ANNUL = "Ocurrió un error al validar si puede anular el registro.";
    public static final java.lang.String MSG_ERR_DB_REG_CAN_DELETE = "Ocurrió un error al validar si puede eliminar el registro.";
    public static final java.lang.String MSG_ERR_DB_QRY = "Ocurrió un error en la consulta de base de datos.";
    public static final java.lang.String MSG_ERR_DB_STP = "Ocurrió un error en el proceso de base de datos.";
    public static final java.lang.String MSG_ERR_DB_CON = "Ocurrió un error en la conexión de base de datos.";

    public static final java.lang.String MSG_ERR_REG_FOUND_NOT = "No se encontró el registro.";
    public static final java.lang.String MSG_ERR_REG_FOUND_MANY = "Se encontraron varios registros.";
    public static final java.lang.String MSG_ERR_REG_SYSTEM = "El registro es de sistema.";
    public static final java.lang.String MSG_ERR_REG_SYSTEM_DEP = "El registro dependiente es de sistema.";

    public static final java.lang.String MSG_ERR_GUI_DATE = "La fecha es inválida.";
    public static final java.lang.String MSG_ERR_GUI_DATE_TIME = "La fecha-hora es inválida.";
    public static final java.lang.String MSG_ERR_GUI_TIME = "La hora es inválida.";
    public static final java.lang.String MSG_ERR_GUI_PER_YEAR = "La fecha no pertenece al año de creación del documento.";
    public static final java.lang.String MSG_ERR_GUI_PER_DATE = "La fecha no pertenece al período.";
    public static final java.lang.String MSG_ERR_GUI_PER_DATE_REC = "La fecha no pertenece al período de la póliza contable.";
    public static final java.lang.String MSG_ERR_GUI_PER_CLOSE = "El período está cerrado.";
    public static final java.lang.String MSG_ERR_GUI_BAL_CASH = "El efectivo disponible es insuficiente.";
    public static final java.lang.String MSG_ERR_GUI_BAL_STOCK = "Las mercancías en existencia son insuficientes.";
    public static final java.lang.String MSG_ERR_GUI_REG_ALREADY_DELETE = "El registro ya está eliminado.";
    public static final java.lang.String MSG_ERR_GUI_REG_ALREADY_ANNUL = "El registro ya está anulado.";
    public static final java.lang.String MSG_ERR_GUI_FIELD_EMPTY = "Se debe especificar un valor para el campo ";
    public static final java.lang.String MSG_ERR_GUI_FIELD_VALUE_DIF = "Se debe especificar un valor diferente para el campo ";
    public static final java.lang.String MSG_ERR_GUI_FIELD_VALUE_NOT_REQ = "No se debe especificar ningún valor para el campo ";
    public static final java.lang.String MSG_ERR_GUI_FIELD_OPTION_SELECT = "Se debe seleccionar el campo ";
    public static final java.lang.String MSG_ERR_GUI_FIELD_OPTION_UNSELECT = "Se debe deseleccionar el campo ";
    public static final java.lang.String MSG_ERR_GUI_FORM_READ_ONLY = "La forma es de solo lectura.";
    public static final java.lang.String MSG_ERR_GUI_FORM_EDIT_ONLY = "La forma es solamente para modificar registros.";
    public static final java.lang.String MSG_ERR_GUI_OPT_UNDEF = "Se debe seleccionar una opción.";
    public static final java.lang.String MSG_ERR_GUI_ROW_UNDEF = "Se debe seleccionar un renglón.";
    public static final java.lang.String MSG_ERR_GUI_SESSION_BRANCH = "No se ha seleccionado una sucursal de la empresa en la sesión de usuario.";
    public static final java.lang.String MSG_ERR_GUI_CFG_DNC = "No se ha definido un centro de foliado para la sucursal o entidad.";
    public static final java.lang.String MSG_ERR_GUI_CFG_DNS = "No se ha definido una serie de folios para el tipo de documento.";
    public static final java.lang.String MSG_ERR_GUI_CFDI_USE = "El Uso de CFDI del documento debe ser ";
    public static final java.lang.String MSG_ERR_GUI_EMPTY_ACC = "La cuenta contable definida está en blanco.";
    public static final java.lang.String MSG_ERR_GUI_EMPTY_CC = "El centro de costo definido está en blanco.";
    public static final java.lang.String MSG_ERR_GUI_NO_DNS_ = "No hay folios para el documento en el almacén ";

    public static final java.lang.String MSG_ERR_ACC_REC_BAL = "La póliza contable no está saldada en la moneda local.";
    public static final java.lang.String MSG_ERR_ACC_REC_BAL_CUR = "La póliza contable no está saldada en la moneda de la misma.";

    public static final java.lang.String MSG_ERR_UTIL_UNKNOWN_ERR = "Error desconocido.";
    public static final java.lang.String MSG_ERR_UTIL_UNKNOWN_MOD = "El módulo es desconocido.";
    public static final java.lang.String MSG_ERR_UTIL_UNKNOWN_FORM = "La forma es desconocida.";
    public static final java.lang.String MSG_ERR_UTIL_UNKNOWN_FORM_PICK = "La forma de selección de opciones es desconocida.";
    public static final java.lang.String MSG_ERR_UTIL_UNKNOWN_VIEW = "La vista es desconocida.";
    public static final java.lang.String MSG_ERR_UTIL_UNKNOWN_OPTION = "La opción es desconocida.";
    public static final java.lang.String MSG_ERR_UTIL_COMP_ITEM_CBOX = "No fue posible leer las opciones del objeto javax.swing.JComboBox.";
    public static final java.lang.String MSG_ERR_UTIL_COMP_ITEM_LIST = "No fue posible leer las opciones del objeto javax.swing.JList.";

    public static final java.lang.String MSG_ERR_RPN_OPER_UNDEF = "Error en notación polaca inversa: operador desconocido.";
    public static final java.lang.String MSG_ERR_RPN_ARGS_FEW = "Error en notación polaca inversa: insuficientes argumentos.";
    public static final java.lang.String MSG_ERR_RPN_ARGS_MANY = "Error en notación polaca inversa: demasiados argumentos.";

    public static final java.lang.String MSG_ERR_LOGIN_USR_UNKNOWN = "Se denegó el acceso al ERP:\n¡Usuario inválido!";
    public static final java.lang.String MSG_ERR_LOGIN_USR_INACTIVE = "Se denegó el acceso al ERP:\n¡Usuario inactivo!";
    public static final java.lang.String MSG_ERR_LOGIN_USR_DELETED = "Se denegó el acceso al ERP:\n¡Usuario eliminado!";
    public static final java.lang.String MSG_ERR_LOGIN_USR_PSWD = "Se denegó el acceso al ERP:\n¡Contraseña inválida!";
    public static final java.lang.String MSG_ERR_LOGIN_USR_CO = "Se denegó el acceso al ERP:\n¡El usuario no tiene acceso a la empresa!";
    public static final java.lang.String MSG_ERR_LOGIN_UNKNOWN = "Se denegó el acceso al ERP:\n¡Error desconocido!";

    public static final java.lang.String MSG_INF_FILE_CREATE = "El siguiente archivo ha sido creado:\n";
    public static final java.lang.String MSG_INF_PROC_FINISH = "El proceso ha terminado.";
    public static final java.lang.String MSG_INF_NO_LINK_DPS = "El documento no tiene vínculos.";
    public static final java.lang.String MSG_INF_NO_LINK_DPS_ETY = "La partida del documento no tiene vínculos.";
    public static final java.lang.String MSG_INF_NO_NOTES_DPS = "El documento no tiene notas.";
    public static final java.lang.String MSG_INF_NO_NOTES_DPS_ETY = "La partida del documento no tiene notas.";
    public static final java.lang.String MSG_INF_BP_BLOCKED = "El asociado de negocios está bloqueado.";
    public static final java.lang.String MSG_INF_BP_TRIAL_WO_OPS = "El asociado de negocios está en jurídico (sin operaciones).";
    public static final java.lang.String MSG_INF_BP_TRIAL_W_OPS = "El asociado de negocios está en jurídico (con operaciones).";
    public static final java.lang.String MSG_INF_NOT_AUTHORN_ORD = "El pedido no está autorizado.";
    public static final java.lang.String MSG_INF_NOT_AUTHORN_DOC = "El documento no está autorizado.";
    public static final java.lang.String MSG_INF_NOT_AUTHORN_CON = "El contrato no está autorizado.";
    
    public static final java.lang.String MSG_CNF_REG_DELETE = "¿Está seguro que desea eliminar el registro?";
    public static final java.lang.String MSG_CNF_REG_ANNUL = "¿Está seguro que desea anular el registro?";
    public static final java.lang.String MSG_CNF_DOC_CLOSE = "¿Está seguro que desea cerrar el documento?";
    public static final java.lang.String MSG_CNF_DOC_OPEN = "¿Está seguro que desea abrir el documento?";
    public static final java.lang.String MSG_CNF_DOC_AUTHORIZE = "¿Está seguro que desea autorizar el documento?";
    public static final java.lang.String MSG_CNF_DOC_REJECT = "¿Está seguro que desea rechazar el documento?";
    public static final java.lang.String MSG_CNF_DOC_AUDIT_YES = "¿Está seguro que desea marcar el documento como auditado?";
    public static final java.lang.String MSG_CNF_DOC_AUDIT_NO = "¿Está seguro que desea desmarcar el documento como auditado?";
    public static final java.lang.String MSG_CNF_FORM_CLOSE = "¿Está seguro que desea cerrar la forma de captura?";
    public static final java.lang.String MSG_CNF_FILE_OPEN = "¿Desea abrir el archivo?";
    public static final java.lang.String MSG_CNF_MSG_CONT = "¿Está seguro que desea continuar?";
}
