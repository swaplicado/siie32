/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModSysConsts;
import erp.mod.fin.db.SFinConsts;
import java.util.HashMap;

/**
 *
 * @author Sergio Flores
 */
public class SHrsConsts {
    
    public static final int YEAR_DAYS = 365;
    public static final int YEAR_DAYS_FORTNIGHTS_FIXED = 360;
    public static final int YEAR_WEEKS = 52;
    public static final int YEAR_WEEKS_EXTENDED = 54;
    public static final int YEAR_FORTNIGHTS = 24;
    public static final int YEAR_MONTHS = 12;
    public static final int MONTH_FORTNIGHTS = 2;
    public static final int WEEK_DAYS = 7;
    public static final int FORTNIGHT_FIXED_DAYS = 15;
    public static final int OVERTIME_2X = 2;
    public static final int OVERTIME_2X_MAX_DAYS = 3;
    public static final int OVERTIME_3X = 3;
    public static final int WORKING_DAY_HOURS = 8;
    public static final float MONTH_DAYS = (float) YEAR_DAYS / YEAR_MONTHS;
    public static final float MONTH_DAYS_FIXED = 30.42f;

    public static final int RET_BONUS_YEARS_MIN = 15;       // retirement bonus minimum years
    public static final int RET_BONUS_DAYS_PER_YEAR = 12;   // retirement bonus days per year
    public static final int RET_BONUS_DMW_LIMIT = 2;        // retirement bonus daily minimum wage limit
    public static final int DIS_COMP_MONTHS = 3;            // dismissal compensantion months
    public static final int DIS_COMP_DAYS_PER_YEAR = 20;    // dismissal compensantion days per year
    
    public static final int SS_INC_MON = 1;         // money
    public static final int SS_INC_KND_SSC_LET = 2; // kind SSC less or equal than limit
    public static final int SS_INC_KND_SSC_GT = 3;  // kind SSC greater than limit
    public static final int SS_INC_PEN = 4;         // pensioner
    public static final int SS_DIS_LIF = 5;         // disability & life
    public static final int SS_CRE = 6;             // creche
    public static final int SS_RSK = 7;             // risk
    public static final int SS_RET = 8;             // retirement
    public static final int SS_SEV = 9;             // severance
    public static final int SS_HOM = 10;            // home
    
    public static final int CAL_NET_AMT_TYPE = 1;   // Calculeted net amout type
    public static final int CAL_GROSS_AMT_TYPE = 2; // Calculeted gross amout type
    
    public static final String TXT_CAL_NET_AMT_TYPE = "Calcular ingreso neto";      // Calculeted net amout type
    public static final String TXT_CAL_GROSS_AMT_TYPE = "Calcular ingreso bruto";   // Calculeted gross amout type
    
    public static final int YEAR_MIN_BIRTH = 1900;
    public static final int YEAR_MAX_BIRTH = 2000;
    
    public static final int PAY_PER_REF_SD = 1;
    public static final int PAY_PER_REF_SBC = 2;
    public static final int PAY_PER_REF_OTRO = 9;
    
    public static final String TXT_PAY_PER_REF_SD = "SD";
    public static final String TXT_PAY_PER_REF_SBC = "SBC";
    public static final String TXT_PAY_PER_REF_OTRO = "OTRO";
    
    public static final int OVERTIME_NEVER = 0;
    public static final int OVERTIME_ALLWAYS = 1;
    public static final int OVERTIME_SOMETIMES = 2;
    
    public static final int CHECKER_POLICY_ESTRICT = 1; // allways
    public static final int CHECKER_POLICY_FREE = 2;    // never
    public static final int CHECKER_POLICY_EVENT = 3;   // sometimes
    
    public static final String TXT_OVERTIME_NEVER = "Nunca";
    public static final String TXT_OVERTIME_ALLWAYS = "Siempre";
    public static final String TXT_OVERTIME_SOMETIMES = "Ocasionalmente";
    
    public static final String EMPTY_BANK = "(SIN BANCO)";
    public static final String CFD_SERIES = "NOM";
    
    public static final String ERR_PERIOD_DATE_INVALID = "Fecha inválida en el período.";
    
    public static final int PPAYROLL_POL_ALL_DATA = 1;
    public static final int PPAYROLL_POL_LIMITED_DATA = 2;
    public static final int PPAYROLL_POL_OTHER_DATA = 3;
    
    public static final String TXT_ACTIVE_NO = "Inactivo";
    public static final String TXT_ACTIVE_YES = "Activo";
    public static final String TXT_INDISTINCT = "Indistinto";
    
    public static final String TXT_UNION_NO = "Confianza";
    public static final String TXT_UNION_YES = "Sindicalizado";
    
    public static final String CODE_UNION_YES = "1";
    public static final String CODE_UNION_NO = "0";
    
    public static final HashMap<Integer, Integer> BanksHrsToFin = new HashMap<>();
    public static final HashMap<Integer, Integer> BanksFinToHrs = new HashMap<>();
    
    static {
        BanksHrsToFin.put(SModSysConsts.HRSS_BANK_HSBC, SFinConsts.LAY_BANK_HSBC);
        BanksHrsToFin.put(SModSysConsts.HRSS_BANK_SANT, SFinConsts.LAY_BANK_SANT);
        BanksHrsToFin.put(SModSysConsts.HRSS_BANK_BBAJ, SFinConsts.LAY_BANK_BBAJ);
        BanksHrsToFin.put(SModSysConsts.HRSS_BANK_BBVA, SFinConsts.LAY_BANK_BBVA);
        BanksHrsToFin.put(SModSysConsts.HRSS_BANK_CITI, SFinConsts.LAY_BANK_CITI);
        
        BanksFinToHrs.put(SFinConsts.LAY_BANK_HSBC, SModSysConsts.HRSS_BANK_HSBC);
        BanksFinToHrs.put(SFinConsts.LAY_BANK_SANT, SModSysConsts.HRSS_BANK_SANT);
        BanksFinToHrs.put(SFinConsts.LAY_BANK_BBAJ, SModSysConsts.HRSS_BANK_BBAJ);
        BanksFinToHrs.put(SFinConsts.LAY_BANK_BBVA, SModSysConsts.HRSS_BANK_BBVA);
        BanksFinToHrs.put(SFinConsts.LAY_BANK_CITI, SModSysConsts.HRSS_BANK_CITI);
    }
}
