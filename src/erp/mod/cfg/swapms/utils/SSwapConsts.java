/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swapms.utils;

/**
 *
 * @author Sergio Flores
 */
public abstract class SSwapConsts {

    public static final String SWAP_SERVICES = "SWAP Services";

    /**
     * JSON name/value pair for link-up status (enabled/disabled) of SWAP Services in current company.
     * ("NVP" = name/value pair.)
     */
    public static final String CFG_NVP_LINK_UP = "link-up";

    /**
     * JSON name/value pair for list of ID's of ERP companies integrated into SWAP Services.
     * ("NVP" = name/value pair.)
     */
    public static final String CFG_NVP_COMPANIES = "companies";

    /**
     * JSON name/value pair for ID of SWAP-Services instance.
     * ("NVP" = name/value pair.)
     */
    public static final String CFG_NVP_INSTANCE = "instance";

    /**
     * JSON object for request attributes of users service.
     */
    public static final String CFG_OBJ_USER_SRV = "user-srv";

    /**
     * JSON object for request attributes of purchase-orders service.
     */
    public static final String CFG_OBJ_PUR_ORD_SRV = "pur-ord-srv";

    public static final String CFG_ATT_URL = "url";
    public static final String CFG_ATT_TOKEN = "token";
    public static final String CFG_ATT_API_KEY = "api-key";
    public static final String CFG_ATT_LIMIT = "limit";

    public static final int TXN_CAT_PURCHASE = 1;
    public static final int TXN_TYPE_ORDER = 2;
}
