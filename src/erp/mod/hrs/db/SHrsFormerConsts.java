/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

/**
 *
 * @author Sergio Flores
 */
public abstract class SHrsFormerConsts {

    /**
     * Former payroll system (a.k.a. "Magic"). Company ID.
     */
    public static int FPS_COM_ID = 1;

    /**
     * Former payroll system (a.k.a. "Magic"). Deduction = Income Tax.
     */
    public static int FPS_DED_INC_TAX = 1;

    /**
     * Former payroll system (a.k.a. "Magic"). Perception = Overtime.
     */
    public static int FPS_PER_OVT = 2;

    /**
     * Former payroll system (a.k.a. "Magic"). Perception = Double overtime.
     */
    public static int FPS_PER_OVT_DBL = 3;

    /**
     * Former payroll system (a.k.a. "Magic"). Perception = Triple overtime.
     */
    public static int FPS_PER_OVT_TRP = 4;

    /**
     * Overtime: maximum hours per day.
     */
    public static int OVT_MAX_HRS_DAY = 3;

    /**
     * Weekly payroll abbreviated.
     */
    public static final String PAY_WEE_ABB = "SEM";

    /**
     * Weekly payroll.
     */
    public static final String PAY_WEE = "SEMANAL";

    /**
     * Biweekly payroll abbreviated.
     */
    public static final String PAY_BIW_ABB = "QNA";

    /**
     * Biweekly payroll.
     */
    public static final String PAY_BIW = "QUINCENAL";

    /**
     * Monthly payroll abbreviated.
     */
    public static final String PAY_MON_ABB = "MEN";

    /**
     * Monthly payroll.
     */
    public static final String PAY_MON = "MENSUAL";
}
