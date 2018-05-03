/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import java.util.HashSet;

/**
 *
 * @author Sergio Flores
 */
public abstract class SFiscalAccountUtils {

    public static final String DEF_CODE_SAL = "401";
    public static final String DEF_CODE_SAL_ADJ = "402";
    public static final String DEF_CODE_PUR = "502.01";
    public static final String DEF_CODE_PUR_ADJ = "503.01";
    public static final String DEF_CODE_COGS = "501.01";

    public static final HashSet<String> SetFiscalAccountCodeSal = new HashSet<>();      // sales
    public static final HashSet<String> SetFiscalAccountCodeSalAdj = new HashSet<>();   // sales adjustments
    public static final HashSet<String> SetFiscalAccountCodePur = new HashSet<>();      // purchases
    public static final HashSet<String> SetFiscalAccountCodePurAdj = new HashSet<>();   // purchases adjustments
    public static final HashSet<String> SetFiscalAccountCodeCogs = new HashSet<>();     // cost of goods sold

    static {
        SetFiscalAccountCodeSal.add("401");
        SetFiscalAccountCodeSal.add("401.01");
        SetFiscalAccountCodeSal.add("401.02");
        SetFiscalAccountCodeSal.add("401.03");
        SetFiscalAccountCodeSal.add("401.04");
        SetFiscalAccountCodeSal.add("401.05");
        SetFiscalAccountCodeSal.add("401.06");
        SetFiscalAccountCodeSal.add("401.07");
        SetFiscalAccountCodeSal.add("401.08");
        SetFiscalAccountCodeSal.add("401.09");
        SetFiscalAccountCodeSal.add("401.1");
        SetFiscalAccountCodeSal.add("401.11");
        SetFiscalAccountCodeSal.add("401.12");
        SetFiscalAccountCodeSal.add("401.13");
        SetFiscalAccountCodeSal.add("401.14");
        SetFiscalAccountCodeSal.add("401.15");
        SetFiscalAccountCodeSal.add("401.16");
        SetFiscalAccountCodeSal.add("401.17");
        SetFiscalAccountCodeSal.add("401.18");
        SetFiscalAccountCodeSal.add("401.19");
        SetFiscalAccountCodeSal.add("401.2");
        SetFiscalAccountCodeSal.add("401.21");
        SetFiscalAccountCodeSal.add("401.22");
        SetFiscalAccountCodeSal.add("401.23");
        SetFiscalAccountCodeSal.add("401.24");
        SetFiscalAccountCodeSal.add("401.25");
        SetFiscalAccountCodeSal.add("401.26");
        SetFiscalAccountCodeSal.add("401.27");
        SetFiscalAccountCodeSal.add("401.28");
        SetFiscalAccountCodeSal.add("401.29");
        SetFiscalAccountCodeSal.add("401.3");
        SetFiscalAccountCodeSal.add("401.31");
        SetFiscalAccountCodeSal.add("401.32");
        SetFiscalAccountCodeSal.add("401.33");
        SetFiscalAccountCodeSal.add("401.34");
        SetFiscalAccountCodeSal.add("401.35");
        SetFiscalAccountCodeSal.add("401.36");
        SetFiscalAccountCodeSal.add("401.37");
        SetFiscalAccountCodeSal.add("401.38");

        SetFiscalAccountCodeSalAdj.add("402.01");
        SetFiscalAccountCodeSalAdj.add("402.02");
        SetFiscalAccountCodeSalAdj.add("402.03");
        SetFiscalAccountCodeSalAdj.add("402.04");

        SetFiscalAccountCodePur.add("502.01");
        SetFiscalAccountCodePur.add("502.02");
        SetFiscalAccountCodePur.add("502.03");
        SetFiscalAccountCodePur.add("502.04");

        SetFiscalAccountCodePurAdj.add("503.01");
        
        SetFiscalAccountCodeCogs.add("501.01");
        SetFiscalAccountCodeCogs.add("501.02");
        SetFiscalAccountCodeCogs.add("501.03");
        SetFiscalAccountCodeCogs.add("501.04");
        SetFiscalAccountCodeCogs.add("501.05");
        SetFiscalAccountCodeCogs.add("501.06");
        SetFiscalAccountCodeCogs.add("501.07");
        SetFiscalAccountCodeCogs.add("501.08");
    }
    
    public static boolean isFiscalAccountCodeSal(final String fiscalAccountCode) {
        return SetFiscalAccountCodeSal.contains(fiscalAccountCode);
    }
    
    public static boolean isFiscalAccountCodeSalAdj(final String fiscalAccountCode) {
        return SetFiscalAccountCodeSalAdj.contains(fiscalAccountCode);
    }
    
    public static boolean isFiscalAccountCodePur(final String fiscalAccountCode) {
        return SetFiscalAccountCodePur.contains(fiscalAccountCode);
    }
    
    public static boolean isFiscalAccountCodePurAdj(final String fiscalAccountCode) {
        return SetFiscalAccountCodePurAdj.contains(fiscalAccountCode);
    }
    
    public static boolean isFiscalAccountCodeCogs(final String fiscalAccountCode) {
        return SetFiscalAccountCodeCogs.contains(fiscalAccountCode);
    }
}
