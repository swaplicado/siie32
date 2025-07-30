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
     * JSON configuration object for instance.
     */
    public static final String CFG_OBJ_INSTANCE = "instance";

    /**
     * JSON configuration object for users "synchronization" (exportation actually!)
     */
    public static final String CFG_OBJ_USER_SYNC = "user-sync";

    /**
     * JSON configuration object for purchase orders "synchronization" (exportation actually!)
     */
    public static final String CFG_OBJ_PUR_ORD_SYNC = "pur-ord-sync";

    public static final String CFG_ATT_URL = "url";
    public static final String CFG_ATT_TOKEN = "token";
    public static final String CFG_ATT_API_KEY = "api-key";
    public static final String CFG_ATT_LIMIT = "limit";

    public static final int TXN_CAT_PURCHASE = 1;
    public static final int TXN_TYPE_ORDER = 2;
}
