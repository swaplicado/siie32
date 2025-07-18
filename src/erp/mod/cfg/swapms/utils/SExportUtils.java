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
import erp.mod.SModConsts;
import erp.mod.cfg.db.SDbSyncLog;
import erp.mod.cfg.db.SDbSyncLogEntry;
import erp.mod.cfg.utils.SAuthJsonUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    /** Tipo de usuario interno. */
    public static final int USER_TYPE_INTERNAL = 1;
    /** Tipo de usuario proveedor. */
    public static final int USER_TYPE_SUPPLIER = 2;

    /** Rol comprador. */
    public static final int ROL_BUYER = 1;
    /** Rol contador. */
    public static final int ROL_ACCOUNTANT = 2;
    /** Rol pagador. */
    public static final int ROL_PAYER = 3;
    /** Rol proveedor. */
    public static final int ROL_SUPPLIER = 4;
    
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    private static final ZoneId MEXICO = java.time.ZoneId.of("America/Mexico_City");
    private static final int HTTP_CODE_OK = 200;
    private static final int HTTP_CODE_CREATED = 201;
    private static final int HTTP_CODE_PENDING = 202;
    private static final int HTTP_CODE_NO_CONTENT = 204;
    private static final int HTTP_CODE_INVALID_REQUEST = 400;
    private static final String PARTNER_ENTITY_TYPE_ORG = "ORG";
    private static final String PARTNER_ENTITY_TYPE_PER = "PERSON";
    
    private static final String LOCAL_COUNTRY_MEX = "MEX";

    private static final int TIME_60_SECONDS = 60 * 1000; // 60 segundos en milisegundos

    private static final String BASE_PROVIDER_QUERY = "SELECT "
            + "    bp.*, "
            + "    country.cty_code, "
            + "    bpb_con.email_01, "
            + "    addr.fid_cty_n, "
            + "    ct.tax_regime "
            + "FROM "
            + "    " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp "
            + "    LEFT JOIN "
            + "    " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS ct ON bp.id_bp = ct.id_bp "
            + "    LEFT JOIN "
            + "    " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpb ON bp.id_bp = bpb.fid_bp "
            + "    LEFT JOIN "
            + "    " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_ADDEE) + " AS addr ON bp.id_bp = addr.fid_bp "
            + "    LEFT JOIN "
            + "    " + SModConsts.TablesMap.get(SModConsts.LOCU_CTY) + " AS country ON addr.fid_cty_n = country.id_cty "
            + "    LEFT JOIN "
            + "    " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB_CON) + " AS bpb_con ON bpb.id_bpb = bpb_con.id_bpb ";

    /**
     * Ejecuta una consulta para obtener usuarios o proveedores y genera un JSON.
     *
     * @param session   Sesión de usuario para acceder a la base de datos.
     * @param sSyncType Tipo de sincronización (USUARIO o PROVEEDOR).
     * @param bSyncAll  Si es true, sincroniza todos los registros; si es false, solo los nuevos/cambiados.
     * @return String con el JSON generado o mensaje de error.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public static String exportJsonData(final SGuiSession session, final String sSyncType, final boolean bSyncAll) throws SQLException {
        try {
            // Determina la fecha de la última sincronización exitosa si no es sincronización total
            Date lastSyncDate = bSyncAll ? null : getLastSyncDateTime(session.getStatement(), sSyncType);

            // Obtiene la configuración del servicio de sincronización
            ObjectMapper mapper = new ObjectMapper();
            JsonNode config = mapper.readTree(
                SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG)
            );

            // Obtiene los datos a exportar según el tipo de sincronización y la fecha de última sincronización
            List<SUserExport> dataToExport = getDataToExport(session.getStatement(), sSyncType, lastSyncDate, session.getConfigCompany().getCompanyId());

            // Lee parámetros de configuración para la exportación
            String syncUrl = SAuthJsonUtils.getValueOfElement(config, "", "user_sync_url");
            int syncLimit = Integer.parseInt(SAuthJsonUtils.getValueOfElement(config, "", "user_sync_limit"));
            String syncToken = SAuthJsonUtils.getValueOfElement(config, "", "user_sync_token");

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
        }
        catch (JsonProcessingException ex) {
            Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, ex);
            return "{\"error\": \"Error procesando JSON: " + ex.getMessage() + "\"}";
        }
        catch (Exception ex) {
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
    private static List<SUserExport> getDataToExport(final Statement stmt, final String syncType, final Date lastSyncDate, final int companyId) throws SQLException {
        switch (syncType) {
            case SDbSyncLog.EXPORT_SYNC_USERS:
                return getListOfUsersToExport(stmt, lastSyncDate, companyId);
            case SDbSyncLog.EXPORT_SYNC_SUPPLIERS:
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
    private static List<SDbSyncLogEntry> parseSyncLogEntries(final JsonNode responseJson) {
        List<SDbSyncLogEntry> entries = new ArrayList<>();
        if (SAuthJsonUtils.containsElement(responseJson, "", "results")) {
            JsonNode results = responseJson.path("results");
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
    private static JsonNode getExternalIdNode(final JsonNode result) {
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
    private static String getResponseCode(final boolean updateLastSync, final List<SDbSyncLogEntry> logEntries, final JsonNode responseJson) {
        if (logEntries.isEmpty() && SAuthJsonUtils.containsElement(responseJson, "", "results")) {
            return HTTP_CODE_NO_CONTENT + ""; // No content
        }
        return updateLastSync ? HTTP_CODE_OK + "" : HTTP_CODE_PENDING + "";
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
    private static void logSync(final SGuiSession session, final String syncType, final String requestBody, final String responseCode, final String responseBody, final List<SDbSyncLogEntry> logEntries) throws SQLException, Exception {
        String sTimeStamp = TIMESTAMP_FORMATTER.format(
                java.time.LocalDateTime.now().atZone(MEXICO)
        );
        SDbSyncLog log = new SDbSyncLog();
        
        String logSufix = syncType + "_" + log.getPk(session) + "_" + sTimeStamp;
        
        log.setSyncType(syncType);
        log.setRequestBody(logSufix + "_request_body");
        log.setRequestTimestamp(new Date());
        log.setResponseCode(responseCode);
        log.setResponseBody(logSufix + "_response_body");
        log.setResponseTimestamp(new Date());
        log.getSyncLogEntries().addAll(logEntries);
        log.save(session);
        
        SExportLogUtils.safeWriteToLogFile(logSufix + "_request_body", requestBody);
        SExportLogUtils.safeWriteToLogFile(logSufix + "_response_body", responseBody);
    }

    /**
     * Obtiene la fecha de la última sincronización exitosa para el tipo de dato indicado.
     * 
     * @param statement Objeto Statement para ejecutar la consulta.
     * @param sSyncType Tipo de sincronización.
     * @return Fecha de la última sincronización exitosa, o null si no existe.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static Date getLastSyncDateTime(final Statement statement, final String sSyncType) throws SQLException {
        String query = "SELECT * FROM " + SModConsts.TablesMap.get(SModConsts.CFG_SYNC_LOG) + " WHERE response_code = '200' AND sync_type = '" + sSyncType + "';";
        try (ResultSet res = statement.executeQuery(query)) {
            if (res.next()) {
                return res.getTimestamp("request_timestamp");
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
    private static List<SUserExport> getListOfUsersToExport(final Statement statement, final Date oLastSyncDateTime, final int companyId)
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
                "    sloge.ts_sync " +
                "FROM " +
                "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS u " +
                "        LEFT JOIN " +
                "    " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON u.fid_bp_n = bp.id_bp " +
                "        LEFT JOIN " +
                "    " + SModConsts.TablesMap.get(SModConsts.CFG_SYNC_LOG_ETY) + " AS sloge ON sloge.reference_id = u.id_usr " +
                "        AND (sloge.response_code = '" + HTTP_CODE_OK + "' " +
                "        OR sloge.response_code = '" + HTTP_CODE_CREATED + "') " +
                "        LEFT JOIN " +
                "    " + SModConsts.TablesMap.get(SModConsts.CFG_SYNC_LOG) + " AS slog ON sloge.id_sync_log = slog.id_sync_log " +
                "         AND slog.sync_type = '" + SDbSyncLog.EXPORT_SYNC_USERS + "' " +
                "WHERE " +
                "    (sloge.ts_sync IS NULL ";
        
        if (oLastSyncDateTime != null) {
            sql += "OR (sloge.ts_sync >= '" + SLibUtils.DbmsDateFormatDatetime.format(oLastSyncDateTime) + "')";
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
        try (ResultSet res = statement.getConnection().createStatement().executeQuery(sql)) {
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
                user.password = generateSecurePassword();
                user.is_active = 1;
                user.first_name = firstName;
                user.last_name = lastName;

                SUserExport.Attributes attr = new SUserExport.Attributes();
                attr.full_name = firstName + " " + lastName;
                attr.user_type = USER_TYPE_INTERNAL;
                attr.external_id = res.getInt("id_usr");
                user.attributes = attr;
                
                List<Integer> lRoles = SUserRolesUtils.getRolesOfUser(statement, attr.external_id, companyId);
                user.groups = lRoles.stream().mapToInt(Integer::intValue).toArray();

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
    private static List<SUserExport> getListSuppliersToExport(final Statement statement, final Date oLastSyncDateTime)
            throws SQLException {
        String sql = BASE_PROVIDER_QUERY
                + "LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.CFG_SYNC_LOG_ETY) + " AS sloge ON sloge.reference_id = bp.id_bp "
                + "        AND (sloge.response_code = '" + HTTP_CODE_OK + "' "
                + "        OR sloge.response_code = '" + HTTP_CODE_CREATED + "') "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.CFG_SYNC_LOG) + " AS slog ON sloge.id_sync_log = slog.id_sync_log "
                + "         AND slog.sync_type = '" + SDbSyncLog.EXPORT_SYNC_SUPPLIERS + "' "
                + "WHERE bp.b_sup "
                + "AND    (sloge.ts_sync IS NULL ";

        if (oLastSyncDateTime != null) {
            sql += "OR (sloge.ts_sync >= '" + SLibUtils.DbmsDateFormatDatetime.format(oLastSyncDateTime) + "') ";
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
                }
                else {
                    user.username = countryCode + "." + fiscalId;
                }

                user.password = generateSecurePassword();
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
                partner.entity_type = res.getInt("bp.fid_tp_bp_idy") == 2 ? PARTNER_ENTITY_TYPE_ORG : PARTNER_ENTITY_TYPE_PER;
                partner.country = countryCode == null || countryCode.isEmpty() ? LOCAL_COUNTRY_MEX : countryCode;
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
    public static SUserExport getSupplierByFiscalId(final Statement statement, final String fiscalId) {
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
                partner.entity_type = res.getInt("bp.fid_tp_bp_idy") == 2 ? PARTNER_ENTITY_TYPE_ORG : PARTNER_ENTITY_TYPE_PER;
                partner.country = res.getString("country.cty_code") == null ? LOCAL_COUNTRY_MEX : res.getString("country.cty_code");
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
    private static String requestSwapService(final String sQuery, final String sURL, final String sMethod, final String sBody, final String sToken) throws Exception {
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
            connection.setConnectTimeout(TIME_60_SECONDS); // 30 segundos para conectar
            connection.setReadTimeout(TIME_60_SECONDS);    // 30 segundos para leer respuesta
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
            InputStream responseStream = (status >= HTTP_CODE_OK && status < HTTP_CODE_INVALID_REQUEST) ? connection.getInputStream() : connection.getErrorStream();

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
    private static String sanitizeJsonString(final String input) {
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
     * Compactar cadena de texto json quitando espacios y saltos de línea.
     * @param json
     * @return 
     */
    public static String compactJson(final String json) {
        if (json == null || json.isEmpty()) {
            return json;
        }
        return json.replaceAll("\\s+", "");
    }

    /**
     * Genera una contraseña segura de 10 caracteres aleatorios.
     * Incluye letras mayúsculas, minúsculas, números y caracteres especiales.
     * @return Contraseña generada.
     */
    public static String generateSecurePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        StringBuilder password = new StringBuilder();
        java.security.SecureRandom random = new java.security.SecureRandom();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }
}
