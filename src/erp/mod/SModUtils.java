/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod;

import sa.lib.SLibConsts;
import sa.lib.gui.SGuiModuleUtils;

/**
 *
 * @author Sergio Flores, Uriel Castañeda
 */
public class SModUtils implements SGuiModuleUtils {

    public SModUtils() {

    }

    /**
     * @param type Constatns defined in <code>SModConsts</code>.
     * @return 
     */
    @Override
    public int getModuleTypeByType(final int type) {
        int moduleType = SLibConsts.UNDEFINED;

        if (type < SModConsts.USRS_TP_USR || type == SModConsts.LOCU_CTY) {
            moduleType = SModConsts.MOD_CFG_N;
        }
        else if (type < SModConsts.LOCS_BOL_NEI_ZIP_CODE) {
            moduleType = SModConsts.MOD_USR_N;
        }
        else if (type < SModConsts.ITMS_CT_ITEM) {
            moduleType = SModConsts.MOD_BPS_N;
        }
        else if (type < SModConsts.FINS_TP_BKR) {
            moduleType = SModConsts.MOD_ITM_N;
        }
        else if (type < SModConsts.TRNS_CT_DPS) {
            moduleType = SModConsts.MOD_FIN_N;
        }
        else if (type < SModConsts.MKTS_TP_PLIST_GRP) {
            moduleType = SModConsts.MOD_TRN_N;
        }
        else if (type < SModConsts.LOGS_TP_SHIP) {
            moduleType = SModConsts.MOD_MKT_N;
        }
        else if (type < SModConsts.MFGS_ST_ORD) {
            moduleType = SModConsts.MOD_LOG_N;
        }
        else if (type < SModConsts.HRSS_CL_HRS_CAT) {
            moduleType = SModConsts.MOD_MFG_N;
        }
        else if (type < SModConsts.QLT_LOT_APR) {
            moduleType = SModConsts.MOD_HRS_N;
        }
        else {
            moduleType = SModConsts.MOD_QLT_N;
        }

        return moduleType;
    }

    /**
     * @param type Constatns defined in <code>SModConsts</code>.
     * @param subtype Constatns defined in <code>SModConsts</code>.
     */
    @Override
    public int getModuleSubtypeBySubtype(final int type, final int subtype) {
        int moduleSubtype = SLibConsts.UNDEFINED;
        
        switch (type) {
            case SModConsts.TRNX_DPS_TANK_CAR:
            case SModConsts.TRNX_DPS_ETY_ACI_PER:
            case SModConsts.TRNX_DPS_ACC_TAG:
                moduleSubtype = subtype;
                break;
            default:
        }
        
        return moduleSubtype;
    }
}
