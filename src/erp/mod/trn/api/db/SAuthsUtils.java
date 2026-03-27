/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.api.db;

import erp.mod.SModConsts;
import erp.mod.cfg.utils.SAuthorizationUtils;

/**
 *
 * @author Edwin Carmona
 */
public class SAuthsUtils {
    
    public static final int FIELD_TS = 1;
    public static final int FIELD_USER_NAME = 2;
    
    public static String getSubquerySentAuth(final int authType, final String idPk1, final String idPk2, final int dataType) {
        String sSelect = "";
        String sTable = "";
        String sWhere = "";
        switch (dataType) {
            case FIELD_TS:
                sSelect = "stpsub.ts_usr_ins";
                break;
            case FIELD_USER_NAME:
                sSelect = "stpu.usr";
                break;

            default:
                break;
        }
        switch (authType) {
            case SAuthorizationUtils.AUTH_TYPE_DPS:

                sTable = "" + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + "";
                sWhere = "AND res_pk_n1_n = " + idPk1 + " "
                        + "        AND res_pk_n2_n = " + idPk2 + " ";
                break;

        }
        String sql = "(SELECT  "
                + " COALESCE(" + sSelect + ", 'NA') "
                + "FROM "
                + "    " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS stpsub "
                + "     INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS stpu ON stpsub.fk_usr_ins = stpu.id_usr "
                + "WHERE "
//                + "    NOT stpsub.b_del AND "
                + "stpsub.res_tab_name_n = '" + sTable + "' "
                + "AND stpsub.fk_tp_authorn = " + authType + " "
                + sWhere + "ORDER BY stpsub.ts_usr_ins ASC LIMIT 1)";

        return sql;
    }
}
