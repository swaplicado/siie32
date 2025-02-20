/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.utils;

import erp.mod.SModConsts;
import erp.siieapp.SUserResource;

import static erp.mod.cfg.utils.SAuthorizationUtils.AUTH_TYPE_DPS;
import static erp.mod.cfg.utils.SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Carmona
 */
public class SNotificationsUtils {

    public static ArrayList<SUserResource> getResourcesPending(java.sql.Statement statement,
            final int authorizationType) {
        String sTable = "";
        switch (authorizationType) {
            case AUTH_TYPE_MAT_REQUEST:
                sTable = SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ);
                break;

            case AUTH_TYPE_DPS:
                sTable = SModConsts.TablesMap.get(SModConsts.TRN_DPS);
                break;
        }

        String sql = "SELECT  "
                + "    steps1.fk_usr_step, steps1.res_pk_n1_n , steps1.res_pk_n2_n "
                + "FROM "
                + "    " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS steps1 "
                + "WHERE "
                + "    NOT steps1.b_del "
                + "        AND steps1.res_tab_name_n = '" + sTable + "' "
                + "        AND NOT steps1.b_authorn "
                + "        AND NOT steps1.b_reject "
                + "        AND steps1.lev = (SELECT  "
                + "            step2.lev "
                + "        FROM "
                + "            " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS step2 "
                + "        WHERE "
                + "            NOT step2.b_del "
                + "                AND step2.res_tab_name_n = '" + sTable + "' "
                + "                AND NOT step2.b_authorn "
                + "                AND NOT step2.b_reject "
                + "                AND NOT step2.b_authorn "
                + "                AND step2.res_pk_n1_n = steps1.res_pk_n1_n "
                + "                AND step2.res_pk_n2_n = steps1.res_pk_n2_n "
                + "        ORDER BY step2.lev ASC "
                + "        LIMIT 1) "
                + "GROUP BY steps1.res_pk_n1_n , steps1.res_pk_n2_n , steps1.fk_usr_step;";

        ArrayList<SUserResource> lResources = new ArrayList<>();
        try {
            ResultSet res = statement.executeQuery(sql);
            SUserResource oResource = null;
            while (res.next()) {
                oResource = new SUserResource();

                oResource.setIdUser(res.getInt("fk_usr_step"));
                oResource.setResourceType(authorizationType);
                oResource.setPk1(res.getInt("res_pk_n1_n"));
                oResource.setPk2(res.getInt("res_pk_n2_n"));

                lResources.add(oResource);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lResources;
    }

    public static ArrayList<SUserResource> getFoliosFromDps(java.sql.Statement statement, ArrayList<SUserResource> lDpsPending) {
        for (SUserResource oDpsPending : lDpsPending) {
            oDpsPending.setFolio(SAuthorizationUtils.getDpsFolio(statement, new int[]{oDpsPending.getPk1(), oDpsPending.getPk2()}));
        }

        return lDpsPending;
    }
}
