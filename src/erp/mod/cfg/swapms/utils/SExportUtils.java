/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swapms.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.cfg.db.SDbSyncLog;
import erp.mod.cfg.db.SDbSyncLogEntry;
import erp.mod.cfg.utils.SAuthJSONUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;


/**
 * Utilidades para exportación de datos de usuarios y proveedores en formato JSON.
 * 
 * Esta clase contiene métodos para consultar la base de datos y generar estructuras
 * JSON usando Jackson, facilitando la integración y sincronización de información
 * con otros sistemas.
 * 
 * @author Edwin Carmona
 */
public class SExportUtils {

    /** Constante para identificar la exportación de usuarios. */
    public static final String EXPORT_SYNC_USERS = "USUARIO";
    /** Constante para identificar la exportación de proveedores. */
    public static final String EXPORT_SYNC_SUPPLIERS = "PROVEEDOR";

    /** Tipo de usuario interno. */
    public static final int USER_TYPE_INTERNAL = 1;
    /** Tipo de usuario proveedor. */
    public static final int USER_TYPE_SUPPLIER = 2;

    /** Rol comprador. */
    public static final int ROL_BUYER = 1;
    /** Rol proveedor. */
    public static final int ROL_SUPPLIER = 4;

    private static final String BASE_PROVIDER_QUERY = "SELECT "
            + "    bp.*, "
            + "    country.cty_code, "
            + "    bpb_con.email_01, "
            + "    addr.fid_cty_n, "
            + "    ct.tax_regime "
            + "FROM "
            + "    erp.bpsu_bp AS bp "
            + "    LEFT JOIN "
            + "    erp.bpsu_bp_ct AS ct ON bp.id_bp = ct.id_bp "
            + "    LEFT JOIN "
            + "    erp.bpsu_bpb AS bpb ON bp.id_bp = bpb.fid_bp "
            + "    LEFT JOIN "
            + "    erp.bpsu_bp_addee AS addr ON bp.id_bp = addr.fid_bp "
            + "    LEFT JOIN "
            + "    erp.locu_cty AS country ON addr.fid_cty_n = country.id_cty "
            + "    LEFT JOIN "
            + "    erp.bpsu_bpb_con AS bpb_con ON bpb.id_bpb = bpb_con.id_bpb ";

    /**
     * Ejecuta una consulta para obtener usuarios o proveedores y genera un JSON.
     *
     * @param session   Sesión de usuario para acceder a la base de datos.
     * @param sSyncType Tipo de sincronización (USUARIO o PROVEEDOR).
     * @param bSyncAll  Si es true, sincroniza todos los registros; si es false, solo los nuevos/cambiados.
     * @return String con el JSON generado o mensaje de error.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public static String exportJsonData(SGuiSession session, final String sSyncType, final boolean bSyncAll) throws SQLException {
        try {
            // Determina la fecha de la última sincronización exitosa si no es sincronización total
            Date lastSyncDate = bSyncAll ? null : getLastSyncDateTime(session.getStatement(), sSyncType);

            // Obtiene la configuración del servicio de sincronización
            ObjectMapper mapper = new ObjectMapper();
            JsonNode config = mapper.readTree(
                SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICE_CONFIG)
            );

            // Obtiene los datos a exportar según el tipo de sincronización y la fecha de última sincronización
            List<SUserExport> dataToExport = getDataToExport(session.getStatement(), sSyncType, null);

            // Lee parámetros de configuración para la exportación
            String syncUrl = SAuthJSONUtils.getValueOfElement(config, "", "user_sync_url");
            int syncLimit = Integer.parseInt(SAuthJSONUtils.getValueOfElement(config, "", "user_sync_limit"));
            String syncToken = SAuthJSONUtils.getValueOfElement(config, "", "user_sync_token");

            // Si no hay datos para exportar, registra el intento y retorna vacío
            if (dataToExport == null || dataToExport.isEmpty()) {
                logSync(session, sSyncType, "", "204", "No hay datos para exportar.", new ArrayList<>());
                return "";
            }

            // Determina si se debe actualizar la fecha de última sincronización
            boolean updateLastSync = dataToExport.size() <= syncLimit;
            // Limita la cantidad de datos a exportar según el límite configurado
            List<SUserExport> limitedData = dataToExport.size() > syncLimit ? dataToExport.subList(0, syncLimit) : dataToExport;

            // Prepara el cuerpo de la petición en formato JSON
            SBodyExport body = new SBodyExport();
            body.work_instance = new String[] { "1" };
            body.users = limitedData.toArray(new SUserExport[0]);
            String requestBody = mapper.writeValueAsString(body);

            // Realiza la petición HTTP al servicio de sincronización
            String response = requestSwapService("", syncUrl, "POST", requestBody, syncToken);
            JsonNode responseJson = mapper.readTree(response);

            // Procesa la respuesta y genera los registros de log correspondientes
            List<SDbSyncLogEntry> logEntries = parseSyncLogEntries(responseJson);
            String responseCode = getResponseCode(updateLastSync, logEntries, responseJson);

            // Registra la operación de sincronización en la base de datos
            logSync(session, sSyncType, compactJson(requestBody), responseCode, compactJson(response), logEntries);

            return "";
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, ex);
            return "{\"error\": \"Error procesando JSON: " + ex.getMessage() + "\"}";
        } catch (Exception ex) {
            Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, ex);
            return "{\"error\": \"Error en la exportación: " + ex.getMessage() + "\"}";
        }
    }

    /**
     * Obtiene la lista de usuarios o proveedores a exportar según el tipo de sincronización.
     *
     * @param stmt         Statement para ejecutar la consulta.
     * @param syncType     Tipo de sincronización (USUARIO o PROVEEDOR).
     * @param lastSyncDate Fecha de última sincronización exitosa (puede ser null).
     * @return Lista de usuarios/proveedores a exportar.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static List<SUserExport> getDataToExport(Statement stmt, String syncType, Date lastSyncDate) throws SQLException {
        switch (syncType) {
            case EXPORT_SYNC_USERS:
                return getListOfUsersToExport(stmt, lastSyncDate);
            case EXPORT_SYNC_SUPPLIERS:
                return getListSuppliersToExport(stmt, lastSyncDate);
            default:
                throw new IllegalArgumentException("Tipo de sincronización no soportado: " + syncType);
        }
    }

    /**
     * Parsea la respuesta JSON del servicio de sincronización y genera los registros de log.
     *
     * @param responseJson Respuesta JSON del servicio.
     * @return Lista de entradas de log para la sincronización.
     */
    private static List<SDbSyncLogEntry> parseSyncLogEntries(JsonNode responseJson) {
        List<SDbSyncLogEntry> entries = new ArrayList<>();
        if (SAuthJSONUtils.containsElement(responseJson, "", "resultados")) {
            JsonNode results = responseJson.path("resultados");
            if (results.isArray()) {
                for (JsonNode result : results) {
                    JsonNode externalIdNode = getExternalIdNode(result);
                    if (externalIdNode != null && externalIdNode.isInt()) {
                        SDbSyncLogEntry entry = new SDbSyncLogEntry();
                        entry.setResponseCode(result.path("status_code").asText());
                        entry.setReferenceId(String.valueOf(externalIdNode.asInt()));
                        entry.setResponseBody(sanitizeJsonString(result.path("message").asText()));
                        entries.add(entry);
                    }
                }
            }
        }
        return entries;
    }

    /**
     * Obtiene el nodo 'external_id' de un resultado JSON, buscando en diferentes niveles.
     *
     * @param result Nodo JSON de resultado.
     * @return Nodo JSON con el external_id o null si no existe.
     */
    private static JsonNode getExternalIdNode(JsonNode result) {
        JsonNode attributes = result.path("attributes");
        if (attributes.isObject() && attributes.has("external_id")) {
            return attributes.path("external_id");
        }
        JsonNode input = result.path("input");
        if (input.isObject()) {
            JsonNode attrInput = input.path("attributes");
            if (attrInput.isObject() && attrInput.has("external_id")) {
                return attrInput.path("external_id");
            }
        }
        return null;
    }

    /**
     * Determina el código de respuesta a registrar según el resultado de la sincronización.
     *
     * @param updateLastSync Indica si se debe actualizar la última sincronización.
     * @param logEntries     Lista de entradas de log generadas.
     * @param responseJson   Respuesta JSON del servicio.
     * @return Código de respuesta como String.
     */
    private static String getResponseCode(boolean updateLastSync, List<SDbSyncLogEntry> logEntries, JsonNode responseJson) {
        if (logEntries.isEmpty() && SAuthJSONUtils.containsElement(responseJson, "", "resultados")) {
            return "204"; // No content
        }
        return updateLastSync ? "200" : "202";
    }

    /**
     * Registra en la base de datos el log de la operación de sincronización.
     *
     * @param session      Sesión de usuario.
     * @param syncType     Tipo de sincronización.
     * @param requestBody  Cuerpo de la petición (compactado).
     * @param responseCode Código de respuesta.
     * @param responseBody Cuerpo de la respuesta (compactado).
     * @param logEntries   Lista de entradas de log generadas.
     * @throws SQLException Si ocurre un error al guardar el log.
     */
    private static void logSync(SGuiSession session, String syncType, String requestBody, String responseCode, String responseBody, List<SDbSyncLogEntry> logEntries) throws SQLException, Exception {
        SDbSyncLog log = new SDbSyncLog();
        log.setSyncType(syncType);
        log.setRequestBody(sanitizeJsonString(requestBody));
        log.setRequestTimestamp(new Date());
        log.setResponseCode(responseCode);
        log.setResponseBody(sanitizeJsonString(responseBody));
        log.setResponseTimestamp(new Date());
        log.getSyncLogEntries().addAll(logEntries);
        log.save(session);
    }

    /**
     * Obtiene la fecha de la última sincronización exitosa para el tipo de dato indicado.
     * 
     * @param statement Objeto Statement para ejecutar la consulta.
     * @param sSyncType Tipo de sincronización.
     * @return Fecha de la última sincronización exitosa, o null si no existe.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static Date getLastSyncDateTime(Statement statement, final String sSyncType) throws SQLException {
        String query = "SELECT * FROM cfgu_sync_log WHERE response_code = '200' AND sync_type = '" + sSyncType + "';";
        try (ResultSet res = statement.executeQuery(query)) {
            if (res.next()) {
                return res.getTimestamp("ts_usr_ins");
            }
        }
        return null;
    }

    /**
     * Consulta los usuarios internos y los prepara para exportación en JSON.
     * 
     * @param statement Objeto Statement para ejecutar la consulta.
     * @param oLastSyncDateTime Fecha de última sincronización (puede ser null).
     * @return Lista de usuarios exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static List<SUserExport> getListOfUsersToExport(Statement statement, Date oLastSyncDateTime)
            throws SQLException {
        String sql = "SELECT  " +
                "    u.id_usr, " +
                "    u.usr, " +
                "    u.email, " +
                "    u.fid_bp_n, " +
                "    bp.bp, " +
                "    bp.lastname, " +
                "    bp.firstname, " +
                "    u.ts_new, " +
                "    u.ts_edit, " +
                "    sloge.ts_usr_ins " +
                "FROM " +
                "    erp.usru_usr AS u " +
                "        LEFT JOIN " +
                "    erp.bpsu_bp AS bp ON u.fid_bp_n = bp.id_bp " +
                "        LEFT JOIN " +
                "    cfgu_sync_log_ety AS sloge ON sloge.reference_id = u.id_usr " +
                "        AND (sloge.response_code = '200' " +
                "        OR sloge.response_code = '201') " +
                "        LEFT JOIN " +
                "    cfgu_sync_log AS slog ON sloge.id_sync_log = slog.id_sync_log " +
                "         AND slog.sync_type = '" + EXPORT_SYNC_USERS + "' " +
                "WHERE " +
                "    (sloge.ts_usr_ins IS NULL ";
        
        if (oLastSyncDateTime != null) {
            sql += "OR (sloge.ts_usr_ins >= '" + SLibUtils.DbmsDateFormatDatetime.format(oLastSyncDateTime) + "')";
        }
        sql += ") ";
        if (oLastSyncDateTime == null) {
            sql += "AND NOT u.b_del " +
                "AND (bp.b_del IS NULL OR NOT bp.b_del) ";
        }
        else {
            sql += "AND u.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(oLastSyncDateTime) + "' ";
        }

        List<SUserExport> users = new ArrayList<>();
        try (ResultSet res = statement.executeQuery(sql)) {
            String firstName = "";
            String lastName = "";
            while (res.next()) {
                // Validar que el usuario no sea vacío, tenga email en formato válido y si no tiene first_name y last_name, se ponga vacío
                if (res.getString("usr") == null || res.getString("usr").isEmpty() ||
                    res.getString("email") == null || !res.getString("email").contains("@")) {
                    continue; // Saltar usuarios inválidos
                }
                if (res.getString("firstname") == null) {
                    firstName = "";
                }
                else {
                    firstName = res.getString("firstname");
                }
                if (res.getString("lastname") == null) {
                    lastName = "";
                }
                else {
                    lastName = res.getString("lastname");
                }
                // Validar que el usuario tenga un nombre y apellido
                if (firstName.isEmpty() && lastName.isEmpty()) {
                    continue; // Saltar usuarios sin nombre y apellido
                }

                // Crear el objeto SUserExport y asignar los valores
                SUserExport user = new SUserExport();
                user.username = res.getString("usr");
                user.email = res.getString("email");
                user.password = res.getString("usr");
                user.is_active = 1;
                user.first_name = firstName;
                user.last_name = lastName;

                SUserExport.Attributes attr = new SUserExport.Attributes();
                attr.full_name = firstName + " " + lastName;
                attr.user_type = USER_TYPE_INTERNAL;
                attr.external_id = res.getInt("id_usr");
                user.attributes = attr;

                user.groups = new int[] { ROL_BUYER };

                users.add(user);
            }
        }

        return users;
    }

    /**
     * Consulta los proveedores y los prepara para exportación en JSON.
     * 
     * @param statement Objeto Statement para ejecutar la consulta.
     * @param oLastSyncDateTime Fecha de última sincronización (puede ser null).
     * @return Lista de proveedores exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static List<SUserExport> getListSuppliersToExport(Statement statement, Date oLastSyncDateTime)
            throws SQLException {
        String sql = BASE_PROVIDER_QUERY
                + "LEFT JOIN "
                + "    cfgu_sync_log_ety AS sloge ON sloge.reference_id = bp.id_bp "
                + "        AND (sloge.response_code = '200' "
                + "        OR sloge.response_code = '201') "
                + "        LEFT JOIN "
                + "    cfgu_sync_log AS slog ON sloge.id_sync_log = slog.id_sync_log "
                + "         AND slog.sync_type = '" + EXPORT_SYNC_SUPPLIERS + "' "
                + "WHERE bp.b_sup "
                + "AND    (sloge.ts_usr_ins IS NULL ";

        if (oLastSyncDateTime != null) {
            sql += "OR (sloge.ts_usr_ins >= '" + SLibUtils.DbmsDateFormatDatetime.format(oLastSyncDateTime) + "') ";
        }
        sql += ") ";
        if (oLastSyncDateTime == null) {
            sql += "AND NOT bp.b_del ";
        } else {
            sql += "AND bp.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(oLastSyncDateTime) + "' ";
        }

        sql += "GROUP BY bp.id_bp "
                + "ORDER BY bpb.ts_edit DESC;";

        List<SUserExport> users = new ArrayList<>();
        try (ResultSet res = statement.executeQuery(sql)) {
            String countryCode = "";
            String firstName = "";
            String lastName = "";
            String fullName = "";
            while (res.next()) {
                countryCode = sanitizeJsonString(res.getString("country.cty_code"));
                firstName = sanitizeJsonString(res.getString("bp.firstname"));
                lastName = sanitizeJsonString(res.getString("bp.lastname"));
                if (firstName.isEmpty() && lastName.isEmpty()) {
                    fullName = sanitizeJsonString(res.getString("bp.bp"));
                } else {
                    fullName = firstName + " " + lastName;
                }

                SUserExport user = new SUserExport();

                String fiscalId = sanitizeJsonString(res.getString("bp.fiscal_id"));
                if (res.getInt("addr.fid_cty_n") <= 1) {
                    user.username = fiscalId;
                    user.password = fiscalId;
                }
                else {
                    user.username = countryCode + "." + fiscalId;
                    user.password = countryCode + "." + fiscalId;
                }

                user.email = sanitizeJsonString(res.getString("email_01"));
                user.is_active = 1;
                user.first_name = firstName;
                user.last_name = lastName;

                SUserExport.Attributes attr = new SUserExport.Attributes();
                attr.full_name = fullName;
                attr.user_type = USER_TYPE_SUPPLIER;
                attr.external_id = res.getInt("bp.id_bp");
                user.attributes = attr;

                SUserExport.Partner partner = new SUserExport.Partner();
                partner.fiscal_id = fiscalId;
                partner.full_name = sanitizeJsonString(res.getString("bp"));
                partner.entity_type = res.getInt("bp.fid_tp_bp_idy") == 2 ? "ORG" : "PER";
                partner.country = countryCode == null || countryCode.isEmpty() ? "MEX" : countryCode;
                partner.external_id = res.getInt("bp.id_bp");
                partner.bp_comm = sanitizeJsonString(res.getString("bp.bp_comm"));
                partner.tax_regime = sanitizeJsonString(res.getString("ct.tax_regime"));
                partner.b_sup = res.getInt("bp.b_sup");
                partner.partner_mail = sanitizeJsonString(res.getString("email_01"));
                user.partner = partner;

                user.groups = new int[] { ROL_SUPPLIER };

                users.add(user);
            }
        }

        return users;
    }

    /**
     * Obtiene un proveedor específico por su ID fiscal.
     * 
     * @param statement Objeto Statement para ejecutar la consulta.
     * @param fiscalId ID fiscal del proveedor a buscar.
     * @return Un objeto SUserExport con los datos del proveedor, o null si no se encuentra.
     */
    public static SUserExport getSupplierByFiscalId(Statement statement, String fiscalId) {
        String sql = BASE_PROVIDER_QUERY + " WHERE bp.fiscal_id = '" + fiscalId + "' "
                + "AND NOT bp.b_del AND (bp.b_del IS NULL OR NOT bp.b_del) "
                + "GROUP BY bp.id_bp "
                + "ORDER BY bpb.ts_edit DESC;";

        try (ResultSet res = statement.executeQuery(sql)) {
            if (res.next()) {
                SUserExport user = new SUserExport();
                user.username = res.getString("bp.fiscal_id");
                user.email = res.getString("email_01");
                user.is_active = 1;
                user.first_name = res.getString("bp.firstname");
                user.last_name = res.getString("bp.lastname");

                SUserExport.Attributes attr = new SUserExport.Attributes();
                attr.full_name = res.getString("bp.bp");
                attr.user_type = USER_TYPE_SUPPLIER;
                attr.external_id = res.getInt("bp.id_bp");
                user.attributes = attr;

                SUserExport.Partner partner = new SUserExport.Partner();
                partner.fiscal_id = res.getString("bp.fiscal_id");
                partner.full_name = res.getString("bp.bp");
                partner.entity_type = res.getInt("bp.fid_tp_bp_idy") == 2 ? "ORG" : "PER";
                partner.country = res.getString("country.cty_code") == null ? "MEX" : res.getString("country.cty_code");
                partner.external_id = res.getInt("bp.id_bp");
                partner.bp_comm = res.getString("bp.bp_comm");
                partner.tax_regime = res.getString("ct.tax_regime");
                partner.b_sup = res.getInt("bp.b_sup");
                partner.partner_mail = res.getString("email_01");
                user.partner = partner;

                return user;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Realiza una solicitud HTTP a un servicio de intercambio de datos.
     * 
     * @param sQuery Parámetros de consulta para la URL (opcional).
     * @param sURL URL del servicio al que se realiza la solicitud.
     * @param sMethod Método HTTP a utilizar (GET, POST, etc.).
     * @param sBody Cuerpo de la solicitud (para métodos como POST).
     * @param sToken Token de autorización (opcional).
     * @return Respuesta del servicio en formato JSON.
     * @throws Exception Si ocurre un error durante la solicitud.
     */
    private static String requestSwapService(String sQuery, String sURL, String sMethod, String sBody, String sToken) throws Exception {
        String charset = java.nio.charset.StandardCharsets.UTF_8.name();
        HttpURLConnection connection = null;
        String responseBody = "";

        try {
            URL url;
            if ("GET".equalsIgnoreCase(sMethod) && sQuery != null && !sQuery.isEmpty()) {
                url = new URL(sURL + "?" + sQuery);
            } else {
                url = new URL(sURL);
            }

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(60000); // 30 segundos para conectar
            connection.setReadTimeout(60000);    // 30 segundos para leer respuesta
            connection.setRequestMethod(sMethod.toUpperCase());
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            if (sToken != null && !sToken.isEmpty()) {
                connection.setRequestProperty("Authorization", sToken);
            }
            connection.setDoInput(true);

            // Para métodos que envían datos (POST, PUT, etc.)
            boolean isBodySent = false;
            if (!"GET".equalsIgnoreCase(sMethod)) {
                if (sBody != null && !sBody.trim().isEmpty()) {
                    // Validar que el body sea un JSON válido
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.readTree(sBody); // Lanza excepción si no es JSON válido
                    } catch (JsonProcessingException ex) {
                        throw new IllegalArgumentException("El parámetro 'sBody' no es un JSON válido.", ex);
                    }
                    connection.setDoOutput(true);
                    try (java.io.OutputStream os = connection.getOutputStream()) {
                        byte[] input = sBody.getBytes(charset);
                        os.write(input, 0, input.length);
                    }
                    isBodySent = true;
                }
                else if (sQuery != null && !sQuery.isEmpty()) {
                    connection.setDoOutput(true);
                    try (java.io.OutputStream os = connection.getOutputStream()) {
                        byte[] input = sQuery.getBytes(charset);
                        os.write(input, 0, input.length);
                    }
                    isBodySent = true;
                }
            }

            int status = connection.getResponseCode();
            InputStream responseStream = (status >= 200 && status < 400) ? connection.getInputStream() : connection.getErrorStream();

            try (Scanner scanner = new Scanner(responseStream, charset)) {
                responseBody = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            }

            System.out.println("Respuesta desde " + sURL);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        
        return responseBody;
    }

    /**
     * Limpia una cadena para evitar caracteres problemáticos en JSON.
     */
    private static String sanitizeJsonString(String input) {
        if (input == null) {
            return "";
        }
        // Reemplaza comillas dobles y otros caracteres problemáticos
        return input.replace("\"", " ")
                    .replace("'", " ")
                    .replace("\\", "\\\\")
                    .replace("\n", " ")
                    .replace("\r", " ")
                    .trim();
    }

    /**
     * Comprime una cadena de texto usando GZIP y la codifica en Base64.
     */
    public static String compressString(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
            gzip.write(str.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            gzip.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error al comprimir la cadena", e);
        }
    }

    /**
     * Compactar cadena de texto json quitando espacios y saltos de línea.
     * @param json
     * @return 
     */
    public static String compactJson(String json) {
        if (json == null || json.isEmpty()) {
            return json;
        }
        return json.replaceAll("\\s+", "");
    }
}
