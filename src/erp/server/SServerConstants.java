/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.server;

/**
 *
 * @author Sergio Flores
 */
public abstract class SServerConstants {

    public static final int REQ_COMP_ITEMS_COMBO_BOX = 1210;
    public static final int REQ_COMP_ITEMS_LIST = 1220;

    public static final int REQ_REGS = 1510;

    public static final int REQ_REPS = 1610;
    public static final int REQ_CHECK = 1611;

    public static final int REQ_DB_ACTION_READ = 2110;
    public static final int REQ_DB_ACTION_SAVE = 2120;
    public static final int REQ_DB_ACTION_ANNUL = 2130;
    public static final int REQ_DB_ACTION_DELETE = 2140;
    public static final int REQ_DB_ACTION_PROCESS = 2150;
    public static final int REQ_DB_CAN_READ = 2210;
    public static final int REQ_DB_CAN_SAVE = 2220;
    public static final int REQ_DB_CAN_ANNUL = 2230;
    public static final int REQ_DB_CAN_DELETE = 2240;
    public static final int REQ_DB_QUERY = 2310;
    public static final int REQ_DB_QUERY_SIMPLE = 2320;
    public static final int REQ_DB_PROCEDURE = 2410;
    public static final int REQ_DB_CATALOGUE_DESCRIPTION = 2510;
    public static final int REQ_OBJ_FIN_ACC_TAX_ID = 3101;
    public static final int REQ_OBJ_FIN_ACC_BP = 3102;
    public static final int REQ_OBJ_FIN_ACC_ITEM = 3103;

    public static final int REQ_CFD = 4110;

    public static final long TIMEOUT_SESSION = 1000 * 60 * 60 * 12;             // 12 hr
    public static final long TIMEOUT_DATA_REGISTRY = 1000 * 60 * 15;            // 15 min
}
