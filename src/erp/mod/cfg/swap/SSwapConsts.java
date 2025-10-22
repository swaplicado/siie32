/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap;

/**
 *
 * @author Sergio Flores
 */
public abstract class SSwapConsts {

    public static int SIIE_EXT_SYS_ID = 1;
    public static final String SIIE = "SIIE";
    public static final String SWAP_SERVICES = "SWAP Services";
    public static final String PURCHASE_PORTAL = "Portal de Compras";
    
    public static final int TIME_30_SEC = 60 * 1000; // 30 segundos en milisegundos
    public static final int TIME_60_SEC = 60 * 1000; // 60 segundos en milisegundos
    public static final int TIME_180_SEC = 180 * 1000; // 180 segundos en milisegundos
    
    /*
     * SWAP Services.
     */
    
    public static final String API_WAKE_UP = "/api/wake-up-every-one";

    /**
     * JSON name/value pair for link-up status (enabled/disabled) of SWAP
     * Services in current company. ("NVP" = name/value pair.)
     */
    public static final String CFG_NVP_LINK_UP = "link-up";

    /**
     * JSON name/value pair for list of ID's of ERP companies integrated into
     * SWAP Services. ("NVP" = name/value pair.)
     */
    public static final String CFG_NVP_COMPANIES = "companies";

    /**
     * JSON name/value pair for ID of SWAP-Services instance. ("NVP" = name/value pair.)
     */
    public static final String CFG_NVP_INSTANCE = "instance";

    /*
     * JSON object for request attributes of users service.
     */
    
    public static final String CFG_OBJ_USER_SRV = "user-srv";
    public static final String CFG_OBJ_USER_USER = "user";
    public static final String CFG_OBJ_USER_AREA = "area";

    /*
     * JSON object for request attributes of transaction-references service.
     */
    
    public static final String CFG_OBJ_TXN_SRV = "txn-srv";
    public static final String CFG_OBJ_TXN_PUR_ORD = "pur-ord";
    public static final String CFG_OBJ_TXN_PUR_REF = "pur-ref";
    public static final String CFG_OBJ_TXN_PUR_DOC = "pur-doc";
    public static final String CFG_OBJ_TXN_PUR_DOC_DWNLD = "pur-doc-dwnld";
    public static final String CFG_OBJ_TXN_PUR_PAY = "pur-pay";
    public static final String CFG_OBJ_TXN_PUR_PAY_UPD = "pur-pay-upd";
    
    public static final String QRY_START_DATE = "start_date";
    public static final String QRY_END_DATE = "end_date";
    public static final String QRY_DOCUMENT_TYPE = "document_type";
    
    // JSON names for configuration attributes:
    public static final String CFG_ATT_URL = "url";
    public static final String CFG_ATT_TOKEN = "token";
    public static final String CFG_ATT_API_KEY = "api-key";
    public static final String CFG_ATT_LIMIT = "limit";

    // SWAP Services constants:
    public static final int TXN_CAT_PURCHASE = 1;
    public static final int TXN_DOC_TYPE_ORDER = 22;
    public static final int TXN_DOC_TYPE_INVOICE = 41;
    public static final int TXN_DOC_TYPE_RECEIPT_PAYMENT = 51;
    public static final String TXN_DOC_REF_TYPE_ORDER_CODE = "OC";
    
    /*
     * Sistema de Autorizaciones:
     */
    
    public static final String CFG_OBJ_AUTH_SRV = "auth-srv";
    public static final String CFG_OBJ_AUTH_ACTOR = "actor";
    public static final String CFG_OBJ_AUTH_ORG_ELEMENT = "org-element";
    public static final String CFG_OBJ_AUTH_START_AUTH = "start-auth";
    
    public static final int AUTHZ_STATUS_PENDING = 1;
    public static final int AUTHZ_STATUS_IN_PROGRESS = 2;
    public static final int AUTHZ_STATUS_REJECTED = 8;
    public static final int AUTHZ_STATUS_OK = 9;
    
    public static final int RESOURCE_TYPE_PUR_INVOICE = 4;
    public static final int RESOURCE_TYPE_PUR_PAYMENT = 11;
    
    public static final int FLOW_MODEL_TYPE = 1;
    public static final int FLOW_TYPE = 2;

    /**
     * Separator for foreign fiscal ID separator from country code.
     */
    public static final String SEPARATOR_FRG_FISCAL_ID = "-";
    
    /**
     * Separator for transaction reference from document-type code.
     */
    public static final String SEPARATOR_DOC_REF = "/";

    /**
     * Longitud de UUID.
     */
    public static final int LEN_UUID = 36;

    /**
     * Tamaño máximo a preservar de las respuestas de los servicios.
     */
    public static final int SIZE_64_KB = 65536; // 2 ^ 16
    
    /**
     * Tipo de usuario interno.
     */
    public static final int USER_TYPE_INTERNAL = 1;
    
    /**
     * Tipo de usuario externo.
     */
    public static final int USER_TYPE_EXTERNAL = 2;
    
    /**
     * Tipo de entidad persona (persona física).
     */
    public static final String PARTNER_ENTITY_TYPE_PER = "PERSON";
    
    /**
     * Tipo de entidad organización (persona moral).
     */
    public static final String PARTNER_ENTITY_TYPE_ORG = "ORG";
    
    /**
     * Rol Administrdor.
     */
    public static final int ROL_ADMINISTRATOR = 1;
    
    /**
     * Rol Comprador.
     */
    public static final int ROL_PURCHASER = 2;
    
    /**
     * Rol Contador.
     */
    public static final int ROL_ACCOUNTANT = 3;
    
    /**
     * Rol Pagador.
     */
    public static final int ROL_PAYER = 4;
    
    /**
     * Rol Proveedor.
     */
    public static final int ROL_SUPPLIER = 5;
    
    /**
     * Rol Agente Comprador.
     */
    public static final int ROL_PURCHASER_AGENT = 12;
    
    /**
     * Tipo de entidad organización (persona moral).
     */
    public static final String PURCHASER_AGENT = "PURCHASER_AGENT";
}
