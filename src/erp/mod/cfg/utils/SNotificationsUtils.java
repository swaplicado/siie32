/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.utils;

import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.siieapp.SUserResource;

import static erp.mod.cfg.utils.SAuthorizationUtils.AUTH_TYPE_DPS;
import static erp.mod.cfg.utils.SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public abstract class SNotificationsUtils {
    
    public static final int PUSH_NOTIFICATION_NEW = 1;
    public static final int PUSH_NOTIFICATION_AUTH = 2;
    public static final int PUSH_NOTIFICATION_REJ = 3;
    public static final int PUSH_NOTIFICATION_PEND = 4;
    public static final int PUSH_NOTIFICATION_BADGE = 5;

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
    
    public static ArrayList<SUserResource> getResourcesPendingCounter(java.sql.Statement statement,
            final int authorizationType, final int idUser) {
        String sTable = "";
        switch (authorizationType) {
            case AUTH_TYPE_MAT_REQUEST:
                sTable = SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ);
                break;

            case AUTH_TYPE_DPS:
                sTable = SModConsts.TablesMap.get(SModConsts.TRN_DPS);
                break;
        }

        String sql = "SELECT "
                + "    all_us.fk_usr_step, "
                + "    COALESCE(pen_us._counter, 0) AS _counter "
                + "FROM "
                + "    (SELECT DISTINCT "
                + "        fk_usr_step "
                + "    FROM "
                + "        " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " "
                + "    WHERE "
                + "        res_tab_name_n = '" + sTable + "' AND NOT b_del) AS all_us "
                + "        LEFT JOIN "
                + "    (SELECT  "
                + "    steps1.fk_usr_step, "
                + "    count(CONCAT(steps1.res_pk_n1_n, "
                + "            '_', "
                + "            steps1.res_pk_n2_n)) AS _counter "
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
                + "        LIMIT 1) ";
        sql += "GROUP BY steps1.fk_usr_step) AS pen_us ON all_us.fk_usr_step = pen_us.fk_usr_step ";
        if (idUser > 0) {
            sql += " WHERE all_us.fk_usr_step = " + idUser + " ";
        }

        ArrayList<SUserResource> lResources = new ArrayList<>();
        try {
            ResultSet res = statement.executeQuery(sql);
            SUserResource oResource = null;
            while (res.next()) {
                oResource = new SUserResource();

                oResource.setIdUser(res.getInt("fk_usr_step"));
                oResource.setResourceType(authorizationType);
                oResource.setCounter(res.getInt("_counter"));

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
    
    @SuppressWarnings("unchecked")
    public static void sendPushNotification(final SGuiSession session, int idUser, String folio, int porpouse, int badge) {
        try {
            String url = SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SIIE_SYS_AUTH_PUSH_NOTIF);
            if (url.isEmpty()) {
                return;
            }
            // Crear la conexión
            URL oUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) oUrl.openConnection();

            // Configurar la petición
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.name());
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true); // Importante para permitir enviar datos

            // Crear el JSON con los parámetros
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("idExternalUser", idUser);
            jsonBody.put("folio", folio);
            jsonBody.put("porpouse", porpouse);
            jsonBody.put("counter", badge);

            // Escribir el JSON en la petición
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Leer la respuesta del servidor
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream response = connection.getInputStream();
                     Scanner scanner = new Scanner(response)) {
                    String responseBody = scanner.useDelimiter("\\A").next();
                    Logger.getLogger(SAuthorizationUtils.class.getName()).
                            log(Level.INFO, "Notificación push enviada: {0}", responseBody);
                }
            } else {
                Logger.getLogger(SAuthorizationUtils.class.getName()).
                        log(Level.SEVERE, "Error en la respuesta push HTTP: {0}", responseCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).
                    log(Level.SEVERE, "Error al enviar notificación push: {0}", ex.getMessage());
        } catch (Exception e) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).
                    log(Level.SEVERE, "Error al enviar notificación push: {0}", e.getMessage());
        }
    }
}
