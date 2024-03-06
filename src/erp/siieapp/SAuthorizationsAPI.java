/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.mod.SModConsts;
import erp.mod.cfg.db.SDbAuthorizationStep;
import erp.mod.cfg.utils.SAuthorizationUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridConsts;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author AdrianAviles
 */
public class SAuthorizationsAPI {

    private SGuiSession oSession;

    public SAuthorizationsAPI(SGuiSession session) {
        oSession = session;
    }

    /**
     * Metodo autorizar recurso
     *
     */
    public String approbeResouorce(int typeResource, Object pk, int userId) {
        String res = SAuthorizationUtils.authOrRejResource(oSession, SAuthorizationUtils.AUTH_ACTION_AUTHORIZE, typeResource, pk, userId, "");
        return res;
    }

    public String rejectResource(int typeResource, Object pk, int userId, String comment) {
        String res = SAuthorizationUtils.authOrRejResource(oSession, SAuthorizationUtils.AUTH_ACTION_REJECT, typeResource, pk, userId, comment);
        return res;
    }

    public boolean isAutorized(int pk) {
        boolean res = SAuthorizationUtils.isAuthorized(oSession, 1, new int[]{pk});
        return res;
    }

    public int getStatus(int pk) {
        int res = SAuthorizationUtils.getAuthStatus(oSession, 1, new int[]{pk});
        return res;
    }

    public String getResources() throws SQLException {
        String jsonDr = "";
        ArrayList<SAuthorizationsData> lAuthData = new ArrayList<>();

        String msSql = "SELECT "
                + "    CASE "
                + "        WHEN v.fk_tp_authorn = " + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + " THEN "
                + "             (cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
                + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.res_pk_n1_n, "
                + "NULL, NULL, NULL, NULL)) "
                + "        WHEN v.fk_tp_authorn = " + SAuthorizationUtils.AUTH_TYPE_DPS + " THEN "
                + "            (cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_DPS + ", "
                + "'" + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + "', v.res_pk_n1_n, "
                + "v.res_pk_n2_n, NULL, NULL, NULL)) "
                + "        ELSE '0' "
                + "END AS authorn_status, "
                + "    CASE "
                + "        WHEN v.fk_tp_authorn = " + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + " THEN "
                + "             (SELECT LPAD(tmq.num, 6, '0') AS num_req "
                + "                FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS tmq "
                + "               WHERE tmq.id_mat_req = v.res_pk_n1_n) "
                + "        WHEN v.fk_tp_authorn = " + SAuthorizationUtils.AUTH_TYPE_DPS + " THEN "
                + "            (SELECT  "
                + "                    CONCAT(t.dt_doc, ' ', t.num_ser, ' ', t.num, ' ', t.num_ref) "
                + "                FROM "
                + "                    trn_dps AS t "
                + "                WHERE "
                + "                    t.id_year =  v.res_pk_n1_n "
                + "                        AND t.id_doc =  v.res_pk_n2_n) "
                + "        ELSE '---' "
                + "END AS doc_reference, "
                + "    CASE "
                + "WHEN v.fk_tp_authorn = " + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + " THEN "
                + "           CASE "
                + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
                + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', tmr.id_mat_req, "
                + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_AUTHORIZED + " THEN 'AUTORIZADO' "
                + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
                + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', tmr.id_mat_req, "
                + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_REJECTED + " THEN 'RECHAZADO' "
                + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
                + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', tmr.id_mat_req, "
                + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_PENDING + " THEN 'PENDIENTE' "
                + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
                + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', tmr.id_mat_req, "
                + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_IN_PROCESS + " THEN 'EN PROCESO' "
                + "ELSE '(NO APLICA)' "
                + "END "
                + "        WHEN v.fk_tp_authorn = " + SAuthorizationUtils.AUTH_TYPE_DPS + " THEN 'dps_status'"
                + "        ELSE '---' "
                + "END AS doc_authorn_status, "
                + "v.id_authorn_step AS " + SDbConsts.FIELD_ID + "1, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "cta.name AS authorn_type, "
                + "v.*, "
                + "IF(v.b_authorn, " + SGridConsts.ICON_THUMBS_UP + ", IF(v.b_reject, " + SGridConsts.ICON_THUMBS_DOWN + ", " + SGridConsts.ICON_NULL + ")) AS f_ico_auth_st, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "ua.usr AS auth_user, "
                + "ur.usr AS rej_user, "
                + /**
                 * Requisiciones
                 */
                "tmr.num as folio_req, "
                + "tmr.dt as dt_req, "
                + //                        "tmce.name AS mr_cons_ent, " +
                //                        "tmpe.name AS mr_prov_ent, " +
                /**
                 * DPS
                 */
                "us.usr AS step_user, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS user_creator, "
                + "uu.usr AS user_updator "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGS_TP_AUTHORN) + " AS cta ON "
                + "v.fk_tp_authorn = cta.id_tp_authorn "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS tmr ON "
                + "v.res_pk_n1_n = tmr.id_mat_req "
                + //                        "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT) + " AS tmce ON " +
                //                        "tmr.fk_ent_mat_cons_ent = tmce.id_mat_cons_ent " +
                //                        "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT) + " AS tmpe ON " +
                //                        "tmr.fk_mat_prov_ent = tmpe.id_mat_prov_ent " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ua ON "
                + "v.fk_usr_authorn_n = ua.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON "
                + "v.fk_usr_reject_n = ur.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS us ON "
                + "v.fk_usr_step = us.id_usr "
                + "WHERE "
                + "NOT v.b_del AND v.authorn_grouper_n = (SELECT " +
                                    " stps.authorn_grouper_n " +
                                    "FROM " +
                                    "    cfgu_authorn_step AS stps " +
                                    "WHERE " +
                                    "    stps.res_tab_name_n = v.res_tab_name_n " +
                                    "        AND stps.res_pk_n1_n = v.res_pk_n1_n " +
                                    "        AND stps.res_pk_n2_n = v.res_pk_n2_n " +
                                    "        AND stps.res_pk_n3_n = v.res_pk_n3_n " +
                                    "        AND stps.res_pk_n4_n = v.res_pk_n4_n " +
                                    "        AND stps.res_pk_n5_n = v.res_pk_n5_n " +
                                    "        AND stps.res_pk_len = v.res_pk_len " +
                                    "ORDER BY stps.authorn_grouper_n DESC " +
                                    "LIMIT 1) "
                + "GROUP BY v.fk_tp_authorn, res_pk_n1_n, res_pk_n2_n, res_pk_n3_n, res_pk_n4_n, res_pk_n5_n "
                + "ORDER BY v.fk_tp_authorn ASC, v.lev ASC";

        try (ResultSet res = oSession.getDatabase().getConnection().createStatement().executeQuery(msSql)) {
            SDataResponse dr = new SDataResponse();
            while (res.next()) {
                SAuthorizationsData au = new SAuthorizationsData();

                switch (res.getInt("fk_tp_authorn")) {
                    case SAuthorizationUtils.AUTH_TYPE_DPS:
                        au.setIdData(new int[]{res.getInt("res_pk_n1_n"), res.getInt("res_pk_n2_n")});
                        au.setDate(res.getString("dt"));
                        au.setFolio(res.getString("folio"));
                        au.setFkUserCreator(res.getInt("fk_usr_ins"));
                        au.setFkUserUpdator(res.getInt("fk_usr_upd"));
                        au.setDateInsert(res.getString("ts_usr_ins"));
                        au.setDateUpdate(res.getString("ts_usr_upd"));
                        au.setUserCreator(res.getString("user_creator"));
                        au.setUserUpdator(res.getString("user_updator"));

                        break;
                    case SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST:
                        au.setIdData(new int[]{res.getInt("res_pk_n1_n")});
                        au.setDate(res.getString("dt_req"));
                        au.setFolio(res.getString("folio_req"));
                        au.setFkUserCreator(res.getInt("fk_usr_ins"));
                        au.setFkUserUpdator(res.getInt("fk_usr_upd"));
                        au.setDateInsert(res.getString("ts_usr_ins"));
                        au.setDateUpdate(res.getString("ts_usr_upd"));
                        au.setUserCreator(res.getString("user_creator"));
                        au.setUserUpdator(res.getString("user_updator"));
//                        au.setConsumeEntity(res.getString("mr_cons_ent"));
//                        au.setSupplierEntity(res.getString("mr_prov_ent"));

                        break;
                    default:
                        break;
                }
                au.setAuthorizationStatusName(res.getString("doc_authorn_status"));
                au.setAuthorizationTypeName(res.getString("authorn_type"));
                au.setDataTypeName(res.getString("authorn_type"));
                au.setDataType(res.getInt("fk_tp_authorn"));
                au.setAuthorizationStatus(res.getInt("authorn_status"));
                lAuthData.add(au);
            }

            dr.setlAuthData(lAuthData);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

            jsonDr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dr);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SAuthorizationsAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jsonDr;
    }

    @SuppressWarnings("unchecked")
    public String getSteps(int pk) {
        ArrayList<SDbAuthorizationStep> res = new ArrayList<>();
        res = SAuthorizationUtils.getResourceAuthSteps(oSession, 1, new int[]{pk});
        JSONArray jsonArray = new JSONArray();

        for (SDbAuthorizationStep oSetp : res) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("authUsername", oSetp.getAuxAuthUsername());
            jsonObject.put("stepUsername", oSetp.getAuxStepUsername());
            jsonObject.put("rejectUsername", oSetp.getAuxRejectUsername());
            jsonObject.put("level", oSetp.getUserLevel());
            jsonObject.put("isAuthorized", oSetp.isAuthorized());
            jsonObject.put("isRejected", oSetp.isRejected());
            jsonObject.put("isRequired", oSetp.isRequired());
            jsonObject.put("timeAuthorized", "'" + oSetp.getDateTimeAuthorized_n() + "'");
            jsonObject.put("timeRejected", "'" + oSetp.getDateTimeRejected_n() + "'");
            jsonArray.add(jsonObject);
        }

        return jsonArray.toJSONString();
    }

    public String getRows(int pk) {
        ArrayList<SAuthorizationEty> lAuthEtyData = new ArrayList<>();
        String jsonDr = "";

        String sql = "SELECT t.*, ui.id_item, ui.item_key, ui.item, uu.id_unit, uu.unit, uu.symbol "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " as t "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " as ui ON ui.id_item = t.fk_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " as uu ON uu.id_unit = t.fk_unit "
                + "WHERE t.id_mat_req = " + pk + " AND NOT t.b_del";

        try (ResultSet res = oSession.getDatabase().getConnection().createStatement().executeQuery(sql)) {
            while (res.next()) {
                SAuthorizationEty auEty = new SAuthorizationEty();
                auEty.setIdEty(res.getInt("id_mat_req"));
                auEty.setQty(res.getFloat("qty"));
//                auEty.setFactConv(res.getFloat("fact_conv"));
                auEty.setPriceUnitSys(res.getFloat("price_u_sys"));
                auEty.setPriceUnit(res.getFloat("price_u"));
                auEty.setTotal(res.getFloat("tot_r"));
                auEty.setIdItem(res.getInt("id_item"));
                auEty.setItemKey(res.getString("item_key"));
                auEty.setItem(res.getString("item"));
                auEty.setIdUnit(res.getInt("id_unit"));
                auEty.setUnit(res.getString("unit"));
                auEty.setSymbol(res.getString("symbol"));

                lAuthEtyData.add(auEty);
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

            jsonDr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(lAuthEtyData);
        } catch (SQLException ex) {
            Logger.getLogger(SAuthorizationsAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SAuthorizationsAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jsonDr;
    }
}