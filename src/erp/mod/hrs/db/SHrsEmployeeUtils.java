/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModSysConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public abstract class SHrsEmployeeUtils {
    
    /**
     * Check if contract type is expirable.
     * @param contractType ID of contract type.
     * @return <code>true</code> if contract type is expirable, otherwise <code>false</code>.
     */
    public static boolean isContractTypeExpirable(final int contractType) {
        return SLibUtils.belongsTo(contractType, new int[] { 
            SModSysConsts.HRSS_TP_CON_WORKING_TIME, 
            SModSysConsts.HRSS_TP_CON_COMMISSION,
            SModSysConsts.HRSS_TP_CON_INSUBORDIN,
            SModSysConsts.HRSS_TP_CON_OTH,
        });
    }
    
    /**
     * Check if contract type is indefinite.
     * @param contractType ID of contract type.
     * @return <code>true</code> if contract type is indefinite, otherwise <code>false</code>.
     */
    public static boolean isContractTypeIndefinite(final int contractType) {
        return SLibUtils.belongsTo(contractType, new int[] { 
            SModSysConsts.HRSS_TP_CON_INDEFINITE, 
            SModSysConsts.HRSS_TP_CON_RETIREMENT
        });
    }
    
    /**
     * Check if contract expiration is required for contract type.
     * @param contractType ID of contract type.
     * @return <code>true</code> if contract expiration is required, otherwise <code>false</code>.
     */
    public static boolean isContractExpirationRequired(final int contractType) {
        return !isContractTypeIndefinite(contractType) && !isContractTypeExpirable(contractType);
    }
    
    /**
     * Check if contract expiration allowed for contract type.
     * @param contractType ID of contract type.
     * @return <code>true</code> if contract expiration is required, otherwise <code>false</code>.
     */
    public static boolean isContractExpirationAllowed(final int contractType) {
        return !isContractTypeIndefinite(contractType) || isContractTypeExpirable(contractType);
    }
}
