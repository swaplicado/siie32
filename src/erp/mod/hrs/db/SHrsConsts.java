/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

/**
 *
 * @author Sergio Flores
 */
public class SHrsConsts {
    
    public static final int YEAR_DAYS = 365;
    public static final int YEAR_DAYS_FORNIGHTS_FIXED = 360;
    public static final int YEAR_WEEKS = 52;
    public static final int YEAR_WEEKS_EXTENDED = 54;
    public static final int YEAR_FORNIGHTS = 24;
    public static final int YEAR_MONTHS = 12;
    public static final int WEEK_DAYS = 7;
    public static final int FORNIGHT_FIXED_DAYS = 15;
    public static final int OVER_TIME_2X = 2;
    public static final int OVER_TIME_2X_MAX_DAY = 3;
    public static final int OVER_TIME_3X = 3;
    public static final float MONTH_DAYS = (float) YEAR_DAYS / YEAR_MONTHS;
    public static final float MONTH_DAYS_FIXED = 30.42f;

    public static final int RET_BONUS_YEARS_MIN = 15; // retirement bonus minimum years
    public static final int RET_BONUS_DAYS_PER_YEAR = 12; // retirement bonus days per year
    public static final int RET_BONUS_DMW_LIMIT = 2; // retirement bonus daily minimum wage limit
    public static final int DIS_COMP_MONTHS = 3; // dismissal compensantion months
    public static final int DIS_COMP_DAYS_PER_YEAR = 20; // dismissal compensantion days per year
    
    public static final int SS_INC_MON = 1; // money
    public static final int SS_INC_KND_SSC_LET = 2; // kind SSC less or equal than limit
    public static final int SS_INC_KND_SSC_GT = 3; // kind SSC greater than limit
    public static final int SS_INC_PEN = 4; // pensioner
    public static final int SS_DIS_LIF = 5; // disability & life
    public static final int SS_CRE = 6; // creche
    public static final int SS_RSK = 7; // risk
    public static final int SS_RET = 8; // retirement
    public static final int SS_SEV = 9; // severance
    public static final int SS_HOM = 10; // home
    
    public static final int CAL_NET_AMT_TYPE = 1; // Calculeted net amout type
    public static final int CAL_GROSS_AMT_TYPE = 2; // Calculeted gross amout type
    
    public static final String TXT_CAL_NET_AMT_TYPE = "Calcular ingreso neto"; // Calculeted net amout type
    public static final String TXT_CAL_GROSS_AMT_TYPE = "Calcular ingreso bruto"; // Calculeted gross amout type
    
    public static final int YEAR_MIN_BIRTH = 1900;
    public static final int YEAR_MAX_BIRTH = 2000;
    
    public static final int SAL_REF_SAL = 1;
    public static final int SAL_REF_SAL_SS = 2;
    public static final int SAL_REF_SAL_FIX = 3;
    
    public static final String TXT_SAL_REF_SAL = "SD";
    public static final String TXT_SAL_REF_SAL_SS = "SBC";
    public static final String TXT_SAL_REF_SAL_FIX = "OTRO";

    public static final String ERR_PERIOD_DATE_INVALID = "Fecha inválida en el período.";
}
