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

    public static final String SWAP_SERVICES = "SWAP Services";

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
     * JSON name/value pair for ID of SWAP-Services instance. ("NVP" =
     * name/value pair.)
     */
    public static final String CFG_NVP_INSTANCE = "instance";

    /**
     * JSON object for request attributes of users service.
     */
    public static final String CFG_OBJ_USERS_SRV = "users-srv";

    /**
     * JSON object for request attributes of functional-areas service.
     */
    public static final String CFG_OBJ_AREAS_SRV = "areas-srv";

    /**
     * JSON object for request attributes of transaction-references service
     * (e.g., purchase-orders references).
     */
    public static final String CFG_OBJ_TXN_REFS_SRV = "txn-refs-srv";

    // JSON names for configuration attributes:
    public static final String CFG_ATT_URL = "url";
    public static final String CFG_ATT_TOKEN = "token";
    public static final String CFG_ATT_API_KEY = "api-key";
    public static final String CFG_ATT_LIMIT = "limit";

    // SWAP Services constants:
    public static final int TXN_CAT_PURCHASE = 1;
    public static final int TXN_DOC_REF_TYPE_ORDER = 22;
    public static final String TXN_DOC_REF_TYPE_ORDER_CODE = "OC";
    
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
    public static final int ROL_BUYER = 2;
    
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
}
