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
import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbAuthorizationStep;
import erp.mod.cfg.utils.SAuthorizationUtils;
import static erp.mod.cfg.utils.SAuthorizationUtils.AUTH_MAIL_AUTH_PEND;
import static erp.mod.cfg.utils.SAuthorizationUtils.AUTH_TYPE_DPS;
import static erp.mod.cfg.utils.SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST;
import erp.mod.trn.db.SDbMaterialRequest;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridConsts;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Adrián Avilés, Edwin Carmona
 */
public class SAuthorizationsAPI {

    private SGuiSession oSession;

    public SAuthorizationsAPI(SGuiSession session) {
        oSession = session;
    }

    /**
     * Autorizar recurso
     * 
     * @param typeResource
     * @param pk
     * @param userId
     * @param comments
     * @return 
     */
    public SAppLinkResponse approbeResource(int typeResource, Object pk, int userId, String comments) {
        SAppLinkResponse oResponse = new SAppLinkResponse();
        try {
            oResponse.setMessage(SAuthorizationUtils.authOrRejResource(oSession, SAuthorizationUtils.AUTH_ACTION_AUTHORIZE, typeResource, pk, userId, comments));
            String actionUserName = SAuthorizationUtils.getUserName(oSession.getStatement().getConnection().createStatement(), userId);
            if (oResponse.getMessage() != null && oResponse.getMessage().isEmpty()) {
                switch(typeResource) {
                    case AUTH_TYPE_MAT_REQUEST:
                        try {
                            SDbMaterialRequest req = new SDbMaterialRequest();
                            req.read(oSession, (int[]) pk);
                            req.save(oSession);
                        }
                        catch (Exception ex) {
                            Logger.getLogger(SAuthorizationsAPI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case AUTH_TYPE_DPS:
                        if (SAuthorizationUtils.isAuthorized(oSession, typeResource, pk)) {
                            Logger.getLogger(SAuthorizationsAPI.class.getName()).log(Level.INFO, "DPS[{0},{1}] autorizado", new Object[]{((int[]) pk)[0], ((int[]) pk)[1]});
                            try {
                                updateDpsAuthStatus(pk,
                                        SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN,
                                        userId);
                                SAuthorizationUtils.sendAuthornMails(oSession, SAuthorizationUtils.AUTH_MAIL_AUTH_DONE, "", "", "", ((int[]) pk), actionUserName, comments);
                            }
                            catch (Exception ex) {
                                Logger.getLogger(SAuthorizationsAPI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else {
                            /**
                             * Envia una notificación mail cuando al recurso aún le faltan pasos por autorizar.
                             */
                            ArrayList<Integer> lUsers;
                            ArrayList<Integer> lUsersNotified;
                            try {
                                boolean toNotification = true;
                                lUsers = SAuthorizationUtils.getUsersInTurnAuth(oSession.getStatement().getConnection().createStatement(), typeResource, ((int[]) pk), toNotification);
                                if (! lUsers.isEmpty()) {
                                    HashMap<String, String> lMails = SAuthorizationUtils.getMailsOfUsers(oSession.getStatement().getConnection().createStatement(), lUsers);
                                    if (! lMails.isEmpty()) {
                                        for (Map.Entry<String, String> oRow : lMails.entrySet()) {
                                            String userMail = oRow.getValue();
                                            Logger.getLogger(SAuthorizationsAPI.class.getName()).log(Level.INFO, "Enviando mail a: {0}", userMail);
                                            SAuthorizationUtils.sendAuthornMails(oSession, AUTH_MAIL_AUTH_PEND, userMail, "", "", ((int[]) pk), "", "");
                                        }
                                    }
                                }
                                
                                boolean toNotificationPush = false;
                                lUsersNotified = SAuthorizationUtils.getUsersInTurnAuth(oSession.getStatement().getConnection().createStatement(), typeResource, ((int[]) pk), toNotificationPush);
                                oResponse.getNextUsers().addAll(lUsersNotified);
                                
                                String folio = SAuthorizationUtils.getDpsFolio(oSession.getStatement().getConnection().createStatement(), ((int[]) pk));
                                oResponse.setFolio(folio);
                            }
                            catch (SQLException ex) {
                                Logger.getLogger(SAuthorizationsAPI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            catch (Exception ex) {
                                Logger.getLogger(SAuthorizationsAPI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        break;
                }
            }
            
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationsAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return oResponse;
    }
    
    /**
     * Rechazar recurso
     * 
     * @param typeResource
     * @param pk
     * @param userId
     * @param comment
     * @return 
     */
    public String rejectResource(int typeResource, Object pk, int userId, String comment) {
        String res = "";
        try {
            String actionUserName = SAuthorizationUtils.getUserName(oSession.getStatement().getConnection().createStatement(), userId);
            res = SAuthorizationUtils.authOrRejResource(oSession, SAuthorizationUtils.AUTH_ACTION_REJECT, typeResource, pk, userId, comment);
            if (res != null && res.isEmpty()) {
                switch(typeResource) {
                    case AUTH_TYPE_MAT_REQUEST:
                        try {
                            SDbMaterialRequest req = new SDbMaterialRequest();
                            req.read(oSession, (int[]) pk);
                            req.save(oSession);
                        }
                        catch (Exception ex) {
                            Logger.getLogger(SAuthorizationsAPI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case AUTH_TYPE_DPS:
                        try {
                            System.out.println("DPS["+((int[]) pk)[0]+","+((int[]) pk)[1]+"] a rechazo");
                            updateDpsAuthStatus(pk,
                                    SDataConstantsSys.TRNS_ST_DPS_AUTHORN_REJECT,
                                    userId);
                            SAuthorizationUtils.sendAuthornMails(oSession, SAuthorizationUtils.AUTH_MAIL_AUTH_REJ, "", "", "", ((int[]) pk), actionUserName, comment);
                        }
                        catch (Exception ex) {
                            Logger.getLogger(SAuthorizationsAPI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                }
            }
            
            return res;
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationsAPI.class.getName()).log(Level.SEVERE, null, ex);
            res = ex.getMessage();
        }
        
        return res;
    }
    
    private void updateDpsAuthStatus(Object pk, final int authAction, final int userId) {
        String sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " "
                + "SET b_authorn = " + (authAction == SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN) + ", "
                + "fid_st_dps_authorn = " + authAction + ", "
                + "fid_usr_authorn = " + userId + ", "
                + "ts_authorn = NOW() "
                + "WHERE id_year = " + ((int[]) pk)[0] + " AND id_doc = " + ((int[]) pk)[1] + ";";
        
        String sqlDpsAuth = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_AUTHORN) + " "
                + "SET fid_st_authorn = " + 
                (authAction == SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN ? SDataConstantsSys.CFGS_ST_AUTHORN_AUTH : SDataConstantsSys.CFGS_ST_AUTHORN_REJ) + ",  "
                + "fid_usr_edit = " + userId + ", "
                + "ts_edit = NOW() "
                + "WHERE id_year = " + ((int[]) pk)[0] + " AND id_doc = " + ((int[]) pk)[1] + " AND NOT b_del;";

        try {
            int res = oSession.getDatabase().getConnection().createStatement().executeUpdate(sql);
            int resAuth = oSession.getDatabase().getConnection().createStatement().executeUpdate(sqlDpsAuth);
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationsAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                + "        ELSE 'NO APLICA' "
                + "END AS doc_authorn_status, "
                + "v.id_authorn_step AS " + SDbConsts.FIELD_ID + "1, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "  IF((cta.name IS NULL), 'REQUISICIÓN DE MATERIALES', cta.name) AS authorn_type,"
                + "v.*, "
                + "IF(v.b_authorn, " + SGridConsts.ICON_THUMBS_UP + ", IF(v.b_reject, " + SGridConsts.ICON_THUMBS_DOWN + ", " + SGridConsts.ICON_NULL + ")) AS f_ico_auth_st, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "tmr.num as folio_req, "
                + "tmr.dt as dt_req, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "pe.name AS prov_ent, "
                + "ur.usr AS usr_req, "
                + "tmr.id_mat_req, "
                + " (IF(v.id_authorn_step IS NULL, "
                + "        'NO APLICA', "
                + "        (SELECT "
                + "                usr "
                + "            FROM "
                + "                " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " "
                + "            WHERE "
                + "                id_usr = v.fk_usr_step))) AS authorn_usr, "
                + "tmr.fk_mat_req_pty, "
                + "pty.name as priority, "
                + "trn_get_cons_info(tmr.id_mat_req, 1) AS ent_cons, "
                + "trn_get_cons_info(tmr.id_mat_req, 2) AS s_ent_cons, "
                + "trn_get_cons_info(tmr.id_mat_req, 3) AS f_cc "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS tmr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT) + " AS pe ON "
                + "tmr.fk_mat_prov_ent = pe.id_mat_prov_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON "
                + "tmr.fk_usr_req = ur.id_usr "
                + "INNER JOIN erp.trnu_mat_req_pty as pty ON tmr.fk_mat_req_pty = pty.id_mat_req_pty "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS v ON "
                + "v.res_pk_n1_n = tmr.id_mat_req AND NOT v.b_del "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CFGS_TP_AUTHORN) + " AS cta ON "
                + "v.fk_tp_authorn = cta.id_tp_authorn AND NOT v.b_del "
                + "WHERE "
                + "NOT tmr.b_del AND tmr.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_AUTH + " AND ( " +
                "    v.res_pk_n1_n IS NULL OR ( " +
                "        v.res_pk_n1_n IS NOT NULL AND ( " +
//                "            -- Cuando authorn_grouper_n no es nulo, obtener el más alto " +
                "            v.authorn_grouper_n = ( " +
                "                SELECT MAX(stps.authorn_grouper_n) " +
                "                FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS stps " +
                "                WHERE stps.res_tab_name_n = v.res_tab_name_n " +
                "                    AND stps.res_pk_n1_n = v.res_pk_n1_n " +
                "                    AND stps.res_pk_len = v.res_pk_len " +
                "                    AND NOT stps.b_del " +
                "            ) " +
//                "            -- Cuando authorn_grouper_n es nulo, igualar a id_authorn_step más alto " +
                "            OR v.id_authorn_step = ( " +
                "                SELECT MAX(stps.id_authorn_step) " +
                "                FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS stps " +
                "                WHERE stps.res_tab_name_n = v.res_tab_name_n " +
                "                    AND stps.res_pk_n1_n = v.res_pk_n1_n " +
                "                    AND stps.res_pk_len = v.res_pk_len " +
                "                    AND NOT stps.b_del))));";

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
                        au.setUserCreator(res.getString("usr_req"));
                        au.setConsumeEntity(res.getString("ent_cons"));
                        au.setSubConsumeEntity(res.getString("s_ent_cons"));
                        au.setSupplierEntity(res.getString("prov_ent"));
                        au.setFkPriority(res.getInt("fk_mat_req_pty"));
                        au.setPriority(res.getString("priority"));
                        au.setDataTypeName("DOCUMENTO");
                        au.setDataType(2);

                        break;
                    case SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST:
                        au.setIdData(new int[]{res.getInt("id_mat_req")});
                        au.setDate(res.getString("dt_req"));
                        au.setFolio(res.getString("folio_req"));
                        au.setFkUserCreator(res.getInt("fk_usr_ins"));
                        au.setFkUserUpdator(res.getInt("fk_usr_upd"));
                        au.setDateInsert(res.getString("ts_usr_ins"));
                        au.setDateUpdate(res.getString("ts_usr_upd"));
                        au.setUserCreator(res.getString("usr_req"));
                        au.setConsumeEntity(res.getString("ent_cons"));
                        au.setSubConsumeEntity(res.getString("s_ent_cons"));
                        au.setSupplierEntity(res.getString("prov_ent"));
                        au.setFkPriority(res.getInt("fk_mat_req_pty"));
                        au.setPriority(res.getString("priority"));
                        au.setDataTypeName("REQUISICIÓN");
                        au.setDataType(1);
                        
                        break;
                    default:
                        au.setIdData(new int[]{res.getInt("id_mat_req")});
                        au.setDate(res.getString("dt_req"));
                        au.setFolio(res.getString("folio_req"));
                        au.setFkUserCreator(res.getInt("fk_usr_ins"));
                        au.setFkUserUpdator(res.getInt("fk_usr_upd"));
                        au.setDateInsert(res.getString("ts_usr_ins"));
                        au.setDateUpdate(res.getString("ts_usr_upd"));
                        au.setUserCreator(res.getString("usr_req"));
                        au.setConsumeEntity(res.getString("ent_cons"));
                        au.setSubConsumeEntity(res.getString("s_ent_cons"));
                        au.setSupplierEntity(res.getString("prov_ent"));
                        au.setFkPriority(res.getInt("fk_mat_req_pty"));
                        au.setPriority(res.getString("priority"));
                        au.setDataTypeName("REQUISICIÓN");
                        au.setDataType(1);
                        
                    break;
                }
                au.setAuthorizationStatusName(res.getString("doc_authorn_status"));
                au.setAuthorizationTypeName(res.getString("authorn_type"));
                au.setAuthorizationStatus(res.getInt("authorn_status"));
                au.setAuthorizationUser(res.getString("authorn_usr"));
                
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

        String sql = "SELECT t.*, ui.id_item, ui.item_key, ui.item, uu.id_unit, uu.unit, uu.symbol, "
                    + "IF(ISNULL(entc.name), trn_get_cons_info(t.id_mat_req, 1), entc.name) AS ent_cons, "
                    + "IF(ISNULL(sentc.name), trn_get_cons_info(t.id_mat_req, 2), sentc.name) AS s_ent_cons, "
                    + "IF(ISNULL(fcc.id_cc), trn_get_cons_info(t.id_mat_req, 3), fcc.id_cc) AS f_cc "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " as t "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " as ui ON ui.id_item = t.fk_item "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " as uu ON uu.id_unit = t.fk_unit "
                    + "LEFT JOIN trn_mat_cons_ent AS entc ON t.fk_ent_mat_cons_ent_n = entc.id_mat_cons_ent "
                    + "LEFT JOIN trn_mat_cons_subent AS sentc ON t.fk_subent_mat_cons_ent_n = sentc.id_mat_cons_ent "
                    + "AND t.fk_subent_mat_cons_subent_n = sentc.id_mat_cons_subent "
                    + "LEFT JOIN fin_cc AS fcc ON t.fk_cc_n = fcc.pk_cc "
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
                auEty.setConsumeEntity(res.getString("ent_cons"));
                auEty.setSubConsumeEntity(res.getString("s_ent_cons"));
                auEty.setFcc(res.getString("f_cc"));

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
    
    public void executeTrnDpsUpd(
            Connection connection,
            int idYear,
            int idDoc,
            int req,
            int reqVal,
            int fidUsr,
            OutputHolder<Integer> idError,
            OutputHolder<String> errorMessage) throws SQLException {

        String storedProcedure = "{CALL trn_dps_upd(?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(storedProcedure)) {
            // Establecer los parámetros de entrada
            callableStatement.setInt(1, idYear);
            callableStatement.setInt(2, idDoc);
            callableStatement.setInt(3, req);
            callableStatement.setInt(4, reqVal);
            callableStatement.setInt(5, fidUsr);

            // Registrar los parámetros de salida
            callableStatement.registerOutParameter(6, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);

            // Ejecutar el procedimiento almacenado
            callableStatement.execute();

            // Obtener los valores de salida
            idError.setValue(callableStatement.getInt(6));
            errorMessage.setValue(callableStatement.getString(7));
        }
    }

    // Clase para manejar parámetros de salida
    public static class OutputHolder<T> {
        private T value;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}
