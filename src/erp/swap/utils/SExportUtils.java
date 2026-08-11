/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.cfg.db.SDbComSyncLog;
import erp.mod.cfg.db.SDbComSyncLogEntry;
import erp.mod.cfg.db.SDbSyncLog;
import erp.mod.cfg.db.SDbSyncLogEntry;
import erp.mod.cfg.utils.SAuthJsonUtils;
import erp.mod.fin.db.SDbPayment;
import erp.swap.SHttpConsts;
import erp.swap.SHttpStatusCodeException;
import erp.swap.SSwapConsts;
import erp.swap.SSwapUtils;
import erp.swap.SSyncType;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiSession;

/**
 * Utilidades para exportación de datos a SWAP Services.
 * 
 * Esta clase contiene métodos para consultar la base de datos y generar 
 * estructuras JSON usando Jackson, facilitando la integración y exportación de 
 * información con otros sistemas.
 * 
 * @author Edwin Carmona, Sergio Flores, Claudio Peña
 */
public abstract class SExportUtils {
    
    public static final DecimalFormat FormatSyncLogId = new DecimalFormat("000000"); // 6 positions
    public static final SimpleDateFormat FormatSyncLogDatetime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    
    public static final int DECS_STD_AMOUNT = 2;
    public static final int DECS_STD_EXC_RATE = 4;
    public static final int DECS_PAY_EXC_RATE = 4;
    public static final int DECS_PAY_CONV_RATE = 8;
    
    public static final int EXPORT_MODE_SILENT = 0;
    public static final int EXPORT_MODE_INFORM = 1; // has effect only if client has GUI
    public static final int EXPORT_MODE_CONFIRM = 2; // has effect only if client has GUI
    
    public static final DecimalFormat FormatStdAmount = new DecimalFormat("#0." + SLibUtils.textRepeat("0", DECS_STD_AMOUNT));
    public static final DecimalFormat FormatStdExchangeRate = new DecimalFormat("#0." + SLibUtils.textRepeat("0", DECS_STD_EXC_RATE));
    public static final DecimalFormat FormatPayExchangeRate = new DecimalFormat("#0." + SLibUtils.textRepeat("0", DECS_PAY_EXC_RATE));
    public static final DecimalFormat FormatPayConversionRate = new DecimalFormat("#0." + SLibUtils.textRepeat("0", DECS_PAY_CONV_RATE));
    
    public static final String ERR_UNSUPPORTED_SYNC_TYPE = "Tipo de sincronización no soportado: ";
    
    private static final int SEC_PSWD_LEN = 10;
    
    /**
     * Genera una contraseña segura de 10 caracteres aleatorios.
     * Incluye letras ASCII mayúsculas, minúsculas, números y carácteres especiales.
     *
     * @return Contraseña segura.
     */
    public static String generateSecurePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.!?@#$%^&*()-_=+";
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();
        
        for (int i = 0; i < SEC_PSWD_LEN; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }
        
        return password.toString();
    }
    
    /**
     * Realiza una solicitud HTTP a un servicio de intercambio de datos.
     * 
     * @param urlQuery Parámetros de consulta para la URL (opcional).
     * @param serviceUrl URL del servicio al que se realiza la solicitud.
     * @param httpMethod Método HTTP a utilizar (GET, POST, PUT, PATCH, etc.).
     * @param jsonBody Cuerpo de la solicitud (para métodos como POST, PUT y PATCH).
     * @param token Token de autorización (opcional).
     * @param apiKey API Key de autorización (opcional).
     * @param timeout Timeout en segundos.
     * @return Respuesta del servicio en formato JSON.
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public static String requestSwapService(final String urlQuery, final String serviceUrl, final String httpMethod, final String jsonBody, final String token, final String apiKey, final int timeout) throws Exception {
        String responseBody = "";
        HttpURLConnection connection = null;
        boolean isHttpMethodPatch = httpMethod.equalsIgnoreCase(SHttpConsts.METHOD_PATCH);
        
        try {
            String charset = StandardCharsets.UTF_8.name();
            
            if (isHttpMethodPatch) {
                // PATCH HTTP method request:
                
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(timeout, TimeUnit.MILLISECONDS)
                        .setResponseTimeout(timeout, TimeUnit.MILLISECONDS)
                        .build();
                
                try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build()) {
                    HttpPatch patch = new HttpPatch(serviceUrl);
                    
                    StringEntity entity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON.withCharset(charset));
                    
                    patch.setEntity(entity);
                    
                    //patch.setHeader("User-Agent", "MyJavaClient/1.0");
                    patch.setHeader("User-Agent", SSwapConsts.SIIE_USER_AGENT);
                    patch.setHeader("Accept", "application/json");
                    
                    if (token != null && !token.isEmpty()) {
                        patch.setHeader("Authorization", token);
                    }
                    else if (apiKey != null && !apiKey.isEmpty()) {
                        patch.setHeader("X-API-Key", apiKey);
                    }
                    
                     try (CloseableHttpResponse response = httpClient.execute(patch)) {
                        int status = response.getCode();
                        
                        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                        StringBuilder sb = new StringBuilder();
                        String line;

                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }

                        responseBody = sb.toString();

                        System.out.println("HTTP Status (PATCH method): " + status);
                        System.out.println("Response: (PATCH method) " + responseBody);
                        
                        if (status < 200 || status >= 300) {
                            throw new SHttpStatusCodeException("HTTP Error " + status + ": " + responseBody);
                        }
                    }
                }
            }
            else {
                // Non PATCH HTTP method request (i.e., GET, POST, PUT, etc.):
                
                String method = httpMethod.toUpperCase();
                boolean isHttpMethodGet = method.equals(SHttpConsts.METHOD_GET);
                URL url;

                if (isHttpMethodGet && urlQuery != null && !urlQuery.isEmpty()) {
                    url = new URL(serviceUrl + "?" + urlQuery);
                }
                else {
                    url = new URL(serviceUrl);
                }

                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(timeout); // timeout para conectar
                connection.setReadTimeout(timeout); // timeout para leer la respuesta
                connection.setRequestMethod(method);
                connection.setRequestProperty("User-Agent", SSwapConsts.SIIE_USER_AGENT);
                connection.setRequestProperty("Accept-Charset", charset);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");

                if (token != null && !token.isEmpty()) {
                    connection.setRequestProperty("Authorization", token);
                }
                else if (apiKey != null && !apiKey.isEmpty()) {
                    connection.setRequestProperty("x-api-key", apiKey);
                }

                connection.setDoInput(true);

                // Para métodos que envían datos (POST, PUT, PATCH, etc.)

                if (!isHttpMethodGet) {
                    String request = "";

                    if (jsonBody != null && !jsonBody.isEmpty()) {
                        // validar que el body sea un JSON válido:

                        try {
                            new ObjectMapper().readTree(jsonBody);
                        }
                        catch (JsonProcessingException ex) {
                            throw new IllegalArgumentException("El parámetro 'body' no es un JSON válido.", ex);
                        }

                        request = jsonBody;
                    }
                    else if (urlQuery != null && !urlQuery.isEmpty()) {
                        request = urlQuery;
                    }

                    if (!request.isEmpty()) {
                        connection.setDoOutput(true);

                        try (java.io.OutputStream os = connection.getOutputStream()) {
                            byte[] input = request.getBytes(charset);
                            os.write(input, 0, input.length);
                        }
                    }
                    else {
                        throw new Exception("El cuerpo de la petición '" + method + "' está vacío.");
                    }
                }

                int status = connection.getResponseCode();
                InputStream responseStream = (status >= SHttpConsts.RSC_SUCC_OK && status < SHttpConsts.RSC_ERR_BAD_REQUEST) ? connection.getInputStream() : connection.getErrorStream();

                try (Scanner scanner = new Scanner(responseStream, charset)) {
                    responseBody = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                }
                
                if (status < 200 || status >= 300) {
                    throw new SHttpStatusCodeException("HTTP Error " + status + ": " + responseBody);
                }

                /*
                System.out.println("HTTP Status (non PATCH method): " + status);
                System.out.println("Response: (non PATCH method) " + responseBody);
                */
            }
        }
        catch (JsonProcessingException | URISyntaxException e) {
            try {
                e.printStackTrace();
            }
            catch (Exception e1) {
                // nothing at all!
            }
            
            Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, e);
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        
        return responseBody;
    }
    
    /**
     * Process Logger with result JSON node.
     * @param result 
     */
    private static void processEntriesNotFound(final JsonNode result) {
        String text = "";

        if (result.has("message")) {
            JsonNode message = result.path("message");
            text = "message: " + message.toString();
        }
        else {
            text = "<unknown>: " + result.toString();
        }

        Logger.getLogger(SExportUtils.class.getName()).log(Level.WARNING, null, new Exception(text));
        System.err.println(text);
    }

    /**
     * Analiza gramaticalmente la respuesta JSON del servicio de sincronización y genera los entradas de la bitácora de sincronización.
     *
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param responseJson Respuesta JSON del servicio.
     * @return Lista de entradas de log para la sincronización.
     * @throws Exception
     */
    private static ArrayList<SDbSyncLogEntry> createSyncLogEntries(final SGuiSession session, final SSyncType syncType, final JsonNode responseJson) throws Exception {
        ArrayList<SDbSyncLogEntry> entries = new ArrayList<>();
        HashMap<Integer, String> databasesMap;
        
        if (SAuthJsonUtils.containsElement(responseJson, "", "results")) {
            JsonNode results = responseJson.path("results");
            
            if (results.isArray()) {
                switch (syncType) {
                    case USER:
                    case PARTNER_SUPPLIER:
                    case PARTNER_CUSTOMER:
                        for (JsonNode result : results) {
                            boolean entriesFound = false;
                            
                            if (result.has("user")) {
                                JsonNode user = result.path("user");

                                if (user.isObject() && user.has("attributes")) {
                                    JsonNode attributes = user.path("attributes");
                                    
                                    if (attributes.isObject()) {
                                        JsonNode externalId = attributes.path("external_id");

                                        SDbSyncLogEntry entry = new SDbSyncLogEntry();
                                        entry.setResponseCode(result.path("status_code").asText());
                                        entry.setResponseBody(SJsonUtils.sanitizeJson(result.path("message").asText()));
                                        entry.setReferenceId("" + externalId.asInt());
                                        entries.add(entry);
                                        
                                        entriesFound = true;
                                    }
                                }
                            }
                            
                            if (!entriesFound){
                                processEntriesNotFound(result);
                            }
                        }
                        break;

                    case AUTH_ACTOR:
                        for (JsonNode result : results) {
                            boolean entriesFound = false;
                            
                            if (result.has("data")) {
                                JsonNode data = result.path("data");

                                if (data.isObject()) {
                                    JsonNode externalId = data.path("external_id");
                                    int actorType = data.path("actor_type").asInt();
                                    String prefix;
                                    
                                    switch (actorType) {
                                        case SExportDataAuthActor.ACTOR_TYPE_USER:
                                            prefix = SExportDataAuthActor.ACTOR_CODE_PREFIX_USER;
                                            break;
                                        case SExportDataAuthActor.ACTOR_TYPE_THIRD_PARTY:
                                            prefix = SExportDataAuthActor.ACTOR_CODE_PREFIX_SUPPLIER;
                                            break;
                                        default:
                                            prefix = "";
                                    }

                                    SDbSyncLogEntry entry = new SDbSyncLogEntry();
                                    entry.setResponseCode(result.path("status_code").asText());
                                    entry.setResponseBody(SJsonUtils.sanitizeJson(result.path("message").asText()));
                                    entry.setReferenceId((prefix.isEmpty() ? "" : prefix + "-") + externalId.asInt());
                                    entries.add(entry);

                                    entriesFound = true;
                                }
                            }
                            
                            if (!entriesFound){
                                processEntriesNotFound(result);
                            }
                        }
                        break;
                        
                    case AUTH_JOB_TITLE:
                        for (JsonNode result : results) {
                            boolean entriesFound = false;
                            
                            if (result.has("data")) {
                                JsonNode data = result.path("data");

                                if (data.isObject()) {
                                    JsonNode externalId = data.path("external_id");

                                    SDbSyncLogEntry entry = new SDbSyncLogEntry();
                                    entry.setResponseCode(result.path("status_code").asText());
                                    entry.setResponseBody(SJsonUtils.sanitizeJson(result.path("message").asText()));
                                    entry.setReferenceId("" + externalId.asInt());
                                    entries.add(entry);

                                    entriesFound = true;
                                }
                            }
                            
                            if (!entriesFound){
                                processEntriesNotFound(result);
                            }
                        }
                        break;

                    case FUNCTIONAL_AREA:
                    case PUR_REF_ORDER:
                    case PUR_REF_SCALE_TICKET:
                        databasesMap = getSwapCompaniesDatabasesMap(session);
                        
                        for (JsonNode result : results) {
                            boolean entriesFound = false;
                            
                            if (result.has("data")) {
                                JsonNode data = result.path("data");

                                if (data.isObject()) {
                                    JsonNode externalId = syncType == SSyncType.FUNCTIONAL_AREA ? data.path("external_id") : data.path("reference");

                                    SDbComSyncLogEntry entry = new SDbComSyncLogEntry();
                                    entry.setResponseCode(result.path("status_code").asText());
                                    entry.setResponseBody(SJsonUtils.sanitizeJson(result.path("message").asText()));
                                    entry.setReferenceId(syncType == SSyncType.FUNCTIONAL_AREA ? "" + externalId.asInt() : externalId.asText());
                                    entry.setAuxDatabase(databasesMap.get(data.path("company_id").asInt()));
                                    entries.add(entry);
                                    
                                    entriesFound = true;
                                }
                            }
                            
                            if (!entriesFound){
                                processEntriesNotFound(result);
                            }
                        }
                        break;
                        
                    case PUR_ORDER:
                    case PUR_PAYMENT:
                    case PUR_PAYMENT_UPD:
                        String attributeId = "";
                        databasesMap = getSwapCompaniesDatabasesMap(session);
                        
                        if (syncType == SSyncType.PUR_ORDER) {
                            attributeId = "document_id"; 
                        }
                        else {
                            attributeId = "payment_id";
                        }
                        
                        for (JsonNode result : results) {
                            boolean entriesFound = false;

                            if (result.has(attributeId)) {
                                JsonNode referenceId = result.path(attributeId);
                                
                                SDbComSyncLogEntry entry = new SDbComSyncLogEntry();
                                entry.setResponseCode(result.path("status_code").asText());
                                
                                if (Integer.parseInt(entry.getResponseCode()) != HttpURLConnection.HTTP_OK && Integer.parseInt(entry.getResponseCode()) != HttpURLConnection.HTTP_CREATED) {
                                    entry.setResponseBody(SJsonUtils.sanitizeJson(result.path("message").asText()) + (result.has("error") ? " " + SJsonUtils.sanitizeJson(result.path("error").toPrettyString()) : ""));
                                }
                                else {
                                    entry.setResponseBody(SJsonUtils.sanitizeJson(result.path("message").asText()));
                                }
                                
                                int companyId;
                                
                                if (result.has("company_id")) {
                                    companyId = result.path("company_id").asInt();
                                }
                                else {
                                    companyId = session.getConfigCompany().getCompanyId();
                                }
                                
                                entry.setAuxDatabase(databasesMap.get(companyId));
                                entry.setReferenceId(referenceId.asText());
                                entries.add(entry);

                                entriesFound = true;
                            }

                            if (!entriesFound){
                                processEntriesNotFound(result);
                            }
                        }
                        break;

                    default:
                        throw new IllegalArgumentException(ERR_UNSUPPORTED_SYNC_TYPE + "'" + syncType + "'.");
                }
            }
        }
        
        return entries;
    }
    
    /**
     * Hace las entradas en las bitácoras de sincronización.
     *
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param message Mensaje para las bitácoras de sincronización.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static int logEmptySync(final SGuiSession session, final SSyncType syncType, final String message) throws SQLException, Exception {
        return logSync(session, syncType, "", null, SHttpConsts.RSC_SUCC_NO_CONTENT, message, null, null);
    }
    
    /**
     * Hace las entradas en las bitácoras de sincronización.
     *
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param requestBody Cuerpo de la petición (compactado).
     * @param requestDatetime Fecha-hora de la petición en el dispositivo cliente.
     * @param httpResponseStatusCode Código de estatus de respuesta HTTP.
     * @param responseBody Cuerpo de la respuesta (compactado).
     * @param responseDatetime Fecha-hora de la respuesta en el dispositivo cliente.
     * @param syncLogEntries Lista de entradas de log generadas.
     * @return Number of entries logged.
     * @throws SQLException Si ocurre un error en las actualizaciones.
     */
    public static int logSync(final SGuiSession session, final SSyncType syncType, final String requestBody, final Date requestDatetime, final int httpResponseStatusCode, final String responseBody, final Date responseDatetime, final ArrayList<SDbSyncLogEntry> syncLogEntries) throws SQLException, Exception {
        int entriesLogged = 0;
        String fileNameRequestBody = "";
        String fileNameResponseBody = "";
        HashMap<String, ArrayList<SDbSyncLogEntry>> syncLogEntriesPerDatabaseMap = new HashMap<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            boolean logDone = false;
            
            try {
                statement.execute("START TRANSACTION");
                
                if (syncLogEntries == null || syncLogEntries.isEmpty()) {
                    // empty sync:

                    SDbSyncLog log;

                    switch (syncType) {
                        case USER:
                        case PARTNER_SUPPLIER:
                        case PARTNER_CUSTOMER:
                        case AUTH_ACTOR:
                        case AUTH_JOB_TITLE:
                            log = new SDbSyncLog();
                            break;

                        case FUNCTIONAL_AREA:
                        case PUR_ORDER:
                        case PUR_ORDER_FILE:
                        case PUR_REF_ORDER:
                        case PUR_REF_SCALE_TICKET:
                        case PUR_PAYMENT:
                        case PUR_PAYMENT_UPD:
                            log = new SDbComSyncLog();
                            break;

                        default:
                            throw new IllegalArgumentException(ERR_UNSUPPORTED_SYNC_TYPE + "'" + syncType + "'.");
                    }

                    log.setSyncType(syncType.toString());
                    log.setRequestTimestamp(requestDatetime);
                    log.setResponseCode("" + httpResponseStatusCode);
                    log.setResponseTimestamp(responseDatetime);
                    log.save(session);

                    fileNameRequestBody = log.getRequestBodyFileName();
                    fileNameResponseBody = log.getResponseBodyFileName();
                }
                else {
                    // effective sync:

                    for (SDbSyncLogEntry entry : syncLogEntries) {
                        ArrayList<SDbSyncLogEntry> entries = syncLogEntriesPerDatabaseMap.get(entry.getAuxDatabase());

                        if (entries == null) {
                            entries = new ArrayList<>();
                            syncLogEntriesPerDatabaseMap.put(entry.getAuxDatabase(), entries);
                        }

                        entries.add(entry);
                    }

                    boolean fileNamesAlreadySet = false;
                    
                    for (String database : syncLogEntriesPerDatabaseMap.keySet()) {
                        SDbSyncLog log;

                        switch (syncType) {
                            case USER:
                            case PARTNER_SUPPLIER:
                            case PARTNER_CUSTOMER:
                            case AUTH_ACTOR:
                            case AUTH_JOB_TITLE:
                                log = new SDbSyncLog();
                                break;

                            case FUNCTIONAL_AREA:
                            case PUR_ORDER:
                            case PUR_ORDER_FILE:
                            case PUR_REF_ORDER:
                            case PUR_REF_SCALE_TICKET:
                            case PUR_PAYMENT:
                            case PUR_PAYMENT_UPD:
                                log = new SDbComSyncLog();
                                break;

                            default:
                                throw new IllegalArgumentException(ERR_UNSUPPORTED_SYNC_TYPE + "'" + syncType + "'.");
                        }
                        
                        // complete processing before log sync:
                        
                        ArrayList<SDbSyncLogEntry> entries = syncLogEntriesPerDatabaseMap.get(database);
                        
                        switch (syncType) {
                            case PUR_PAYMENT:
                            case PUR_PAYMENT_UPD:
                                complementProcessing(session, syncType, entries, null);
                                break;
                            default:
                            // NA
                        }

                        // log sync:
                        
                        log.setSyncType(syncType.toString());
                        log.setRequestTimestamp(requestDatetime);
                        log.setResponseCode("" + httpResponseStatusCode);
                        log.setResponseTimestamp(responseDatetime);
                        log.getEntries().addAll(entries);
                        log.setAuxDatabase(database);
                        log.save(session);
                        
                        entriesLogged += log.getEntries().size();

                        if (!fileNamesAlreadySet) {
                            fileNameRequestBody = log.getRequestBodyFileName();
                            fileNameResponseBody = log.getResponseBodyFileName();
                            fileNamesAlreadySet = true;
                        }
                    }
                }
                
                logDone = true;

                SExportLogsUtils.safeWriteToLogFile(fileNameRequestBody, requestBody);
                SExportLogsUtils.safeWriteToLogFile(fileNameResponseBody, responseBody);
            }
            catch (Exception eExe) {
                if (!logDone) {
                    try {
                        statement.execute("ROLLBACK");
                    }
                    catch (SQLException eTxn) {
                        Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, eTxn);
                    }
                }
                
                Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, eExe);
                throw eExe; // focus on execution issues!
            }
            finally {
                if (logDone) {
                    try {
                        statement.execute("COMMIT");
                    }
                    catch (SQLException eTxn) {
                        Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, eTxn);
                        throw eTxn; // focus on transaction issues!
                    }
                }
            }
        }
        
        return entriesLogged;
    }
    
    /**
     * Cerrar las bitácoras de sincronización completando los últimos detalles de las entradas:
     * marcar la última entrada en bitácora de sincronización con estatus "CREADA" como "OK" para indicar la culminación del proceso de sincronización.
     *
     * @param session Sesión de usuario.
     * @param statement Statement para ejecutar la consulta.
     * @param syncType Tipo de sincronización.
     * @param firstRequestDatetime Fecha-hora de la primer petición en el dispositivo cliente del conjunto actual de iteraciones de sincronización.
     * @throws SQLException Si ocurre un error en las actualizaciones.
     */
    private static void closeLogSync(final SGuiSession session, final SSyncType syncType, final Date firstRequestDatetime) throws SQLException, Exception {
        switch (syncType) {
            case USER:
            case PARTNER_SUPPLIER:
            case PARTNER_CUSTOMER:
            case AUTH_ACTOR:
            case AUTH_JOB_TITLE:
                SExportDataUtils.markLastSyncCreatedAsOk(session.getStatement(), syncType, firstRequestDatetime, "");
                break;

            case FUNCTIONAL_AREA:
            case PUR_ORDER:
            case PUR_ORDER_FILE:
            case PUR_REF_ORDER:
            case PUR_REF_SCALE_TICKET:
            case PUR_PAYMENT:
            case PUR_PAYMENT_UPD:
                HashMap<Integer, String> databasesMap = getSwapCompaniesDatabasesMap(session);
                for (Integer companyId : databasesMap.keySet()) {
                    String database = databasesMap.get(companyId);
                    SExportDataUtils.markLastSyncCreatedAsOk(session.getStatement(), syncType, firstRequestDatetime, database);
                }
                break;

            default:
                throw new IllegalArgumentException(ERR_UNSUPPORTED_SYNC_TYPE + "'" + syncType + "'.");
        }
    }
    
    /**
     * Leer obtener pago en modalidad simplificada para acceder a sus datos elementales para procesarse en sincronizaciones.
     * @param database Base de datos de la empresa en la que existe el pago.
     * @param paymentId ID del pago.
     * @return
     * @throws SQLException 
     */
    private static BarePayment readBarePayment(final SGuiSession session, final String database, final int paymentId) throws SQLException {
        BarePayment payment = null;
        String sql = "SELECT fk_st_pay, fk_usr_upd "
                + "FROM " + (database.isEmpty() ? "" : database + ".") + SModConsts.TablesMap.get(SModConsts.FIN_PAY) + " "
                + "WHERE id_pay = " + paymentId + ";";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                payment = new BarePayment(database, paymentId, resultSet.getInt("fk_st_pay"), resultSet.getInt("fk_usr_upd"));
            }
        }
        
        return payment;
    }
    
    /**
     * Complementar el procesamiento de las partidas de la bitácora de sincronización, si el tipo de sincronización lo requiere.
     * 
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param entries Partidas de la bitácora de sincronización a procesar.
     * @param value Nuevo valor para complementar el procesamiento de las partidas. Puede ser nulo.
     * @throws SQLException Si ocurre un error en las actualizaciones.
     * @throws Exception 
     */
    public static void complementProcessing(final SGuiSession session, final SSyncType syncType, ArrayList<SDbSyncLogEntry> entries, Object value) throws SQLException, Exception {
        switch (syncType) {
            case PUR_PAYMENT:
            case PUR_PAYMENT_UPD:
                // cambiar el estatus de los nuevos pagos recién enviados a SWAP Services para su autorización:
                
                SDbPayment payment = new SDbPayment(); // is a dummy instances, just for field update purmposes
                
                for (SDbSyncLogEntry entry : entries) {
                    int paymentId = SLibUtils.parseInt(entry.getReferenceId());
                    BarePayment barePayment = readBarePayment(session, entry.getAuxDatabase(), paymentId);
                    int newStatusPaymentId = SDbPayment.getSettledStatusPaymentId(barePayment.StatusId);
                    
                    if (newStatusPaymentId != 0) {
                        Object valueToUpdate = new Object[] { newStatusPaymentId, barePayment.UserId, entry.getAuxDatabase() }; // nuevo estatus, usuario, base de datos:
                        
                        payment.saveField(session.getStatement(), new int[] { SLibUtils.parseInt(entry.getReferenceId()) }, SDbPayment.FIELD_STATUS_PAYMENT, valueToUpdate);
                    }
                }
                break;
                
            default:
                throw new IllegalArgumentException(ERR_UNSUPPORTED_SYNC_TYPE + "'" + syncType + "'.");
        }
    }
    
    /**
     * Procesa la respuesta de SWAP Services y hace las entradas en las bitácoras de sincronización.
     * 
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param requestBody Cuerpo de la petición (compactado).
     * @param requestDatetime Fecha-hora de la petición en el dispositivo cliente.
     * @param responseBody Respuesta de SWAP Services.
     * @param responseDatetime Fecha-hora de la respuesta en el dispositivo cliente.
     * @throws Exception
     */
    private static int computeResponse(final SGuiSession session, final SSyncType syncType, final String requestBody, final Date requestDatetime, final String responseBody, final Date responseDatetime) throws Exception {
        final JsonNode responseJson = new ObjectMapper().readTree(responseBody);
        
        // Procesar la respuesta y generar las entradas de bitácora correspondientes:
        
        int httpResponseStatusCode;
        ArrayList<SDbSyncLogEntry> syncLogEntries = createSyncLogEntries(session, syncType, responseJson);
        
        if (syncLogEntries.isEmpty() && SAuthJsonUtils.containsElement(responseJson, "", "results")) {
            httpResponseStatusCode = SHttpConsts.RSC_SUCC_NO_CONTENT;
        }
        else {
            httpResponseStatusCode = SHttpConsts.RSC_SUCC_CREATED;
        }
        
        // Registrar la operación de exportación en las bitácoras de sincronización:
        
        return logSync(session, syncType, requestBody, requestDatetime, httpResponseStatusCode, responseBody, responseDatetime, syncLogEntries);
    }
    
    /**
     * Procesa la exportación de datos a SWAP Services.
     * 
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param period Periodo de la sincronización (fechas inicial y final.)
     * @return <code>SResponseInfo</code> con la información de la petición a SWAP Services.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static SResponseInfo computeRequest(final SGuiSession session, final SSyncType syncType) throws SQLException, Exception {
        // Obtener datos a exportar según el tipo de sincronización:
        ArrayList<SExportData> allExportDatas = SExportDataUtils.getDataToExport(session, syncType);

        // Si no hay datos para exportar, registrar el intento y retornar:
        if (allExportDatas == null || allExportDatas.isEmpty()) {
            String message = "No hay registros nuevos o modificados '" + SSwapUtils.translateSyncType(syncType, SLibConsts.LAN_ISO639_ES) + "' para exportar.";
            logEmptySync(session, syncType, message);
            
            SResponseInfo responseInfo = new SResponseInfo(syncType, message, true);
            responseInfo.setRegistriesRetrieved(0);
            return responseInfo;
        }

        // Leer la configuración de la sincronización:

        String cfgParamKey = "";
        String jsonBaseKey = "";
        String jsonConfigKey = "";
        String testHost = "";
        String testApyKey = "";
        
        if (session.getClient() != null && ((SClientInterface) session.getClient()).isDev()) {
            // hosts para pruebas:
            
            System.out.println("*** Running in dev mode! ***");
            
            switch (syncType) {
                case USER:
                case PARTNER_SUPPLIER:
                case PARTNER_CUSTOMER:
                case FUNCTIONAL_AREA:
                    testHost = "https://api-usuarios-test-515680676790.europe-west1.run.app";
                    break;

                case PUR_ORDER:
                case PUR_REF_ORDER:
                case PUR_REF_SCALE_TICKET:
                    testHost = "https://transaction-backend-test-515680676790.europe-west1.run.app";
                    break;
                    
                case PUR_PAYMENT:
                case PUR_PAYMENT_UPD:
                    testHost = "http://192.168.1.87:8003"; // today host in César Orozco's (30/09/2025)
                    break;

                case AUTH_ACTOR:
                case AUTH_JOB_TITLE:
                    testHost = "https://gateway-autorizaciones-test-6kweyks6.uc.gateway.dev";
                    testApyKey = "AIzaSyCs6HMWX_OE8Pr1M8ycQ3IHwFfNX81ZyIE";
                    break;

                default:
                    throw new IllegalArgumentException(ERR_UNSUPPORTED_SYNC_TYPE + "'" + syncType + "'.");
            }
        }
        
        switch (syncType) {
            case USER:
            case PARTNER_SUPPLIER:
            case PARTNER_CUSTOMER:
            case FUNCTIONAL_AREA:
                cfgParamKey = SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG;
                jsonBaseKey = SSwapConsts.CFG_OBJ_USER_SRV;
                
                switch (syncType) {
                    case USER:
                    case PARTNER_SUPPLIER:
                    case PARTNER_CUSTOMER:
                        jsonConfigKey = SSwapConsts.CFG_OBJ_USER_USER;
                        break;

                    case FUNCTIONAL_AREA:
                        jsonConfigKey = SSwapConsts.CFG_OBJ_USER_AREA;
                        break;

                    default:
                        // nothing
                }
                break;
                
            case PUR_ORDER:
            case PUR_REF_ORDER:
            case PUR_REF_SCALE_TICKET:
            case PUR_PAYMENT:
            case PUR_PAYMENT_UPD:
                cfgParamKey = SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG;
                jsonBaseKey = SSwapConsts.CFG_OBJ_TXN_SRV;
                
                switch (syncType) {
                    case PUR_ORDER:
                        jsonConfigKey = SSwapConsts.CFG_OBJ_TXN_PUR_ORD;
                        break;

                    case PUR_REF_ORDER:
                    case PUR_REF_SCALE_TICKET:
                        jsonConfigKey = SSwapConsts.CFG_OBJ_TXN_PUR_REF;
                        break;
                        
                    case PUR_PAYMENT:
                        jsonConfigKey = SSwapConsts.CFG_OBJ_TXN_PUR_PAY;
                        break;
                    
                    case PUR_PAYMENT_UPD:
                        jsonConfigKey = SSwapConsts.CFG_OBJ_TXN_PUR_PAY_UPD;
                        break;
                        
                    default:
                        // nothing
                }
                break;
                
            case AUTH_ACTOR:
            case AUTH_JOB_TITLE:
                cfgParamKey = SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_AUTH_CONFIG;
                jsonBaseKey = SSwapConsts.CFG_OBJ_AUTH_SRV;
                
                switch (syncType) {
                    case AUTH_ACTOR:
                        jsonConfigKey = SSwapConsts.CFG_OBJ_AUTH_ACTOR;
                        break;

                    case AUTH_JOB_TITLE:
                        jsonConfigKey = SSwapConsts.CFG_OBJ_AUTH_ORG_ELEMENT;
                        break;

                    default:
                        // nothing
                }
                break;
                
            default:
                throw new IllegalArgumentException(ERR_UNSUPPORTED_SYNC_TYPE + "'" + syncType + "'.");
        }
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode config = mapper.readTree(SCfgUtils.getParamValue(session.getStatement(), cfgParamKey));
        
        String syncUrl = "";
        String syncToken = "";
        String syncApiKey = "";
        int syncLimit = 0;
        
        // Recuperar la configuración base:
        
        if (!jsonBaseKey.isEmpty()) {
            if (!testHost.isEmpty()) {
                syncUrl = testHost;
            }
            else {
                syncUrl = SAuthJsonUtils.getValueOfElementAsText(config, jsonBaseKey, SSwapConsts.CFG_ATT_URL);
            }
            
            syncToken = SAuthJsonUtils.getValueOfElementAsText(config, jsonBaseKey, SSwapConsts.CFG_ATT_TOKEN); // recuperar token genérico del end point
            
            if (!testApyKey.isEmpty()) {
                syncApiKey = testApyKey;
            }
            else {
                syncApiKey = SAuthJsonUtils.getValueOfElementAsText(config, jsonBaseKey, SSwapConsts.CFG_ATT_API_KEY); // recuperar API key genérica del end point
            }
        }
        
        // Recuperar la configuración del servicio:
        
        syncUrl += SAuthJsonUtils.getValueOfElementAsText(config, jsonConfigKey, SSwapConsts.CFG_ATT_URL); // complementar la URL
        
        if (syncToken.isEmpty()) {
            syncToken = SAuthJsonUtils.getValueOfElementAsText(config, jsonConfigKey, SSwapConsts.CFG_ATT_TOKEN); // recuperar token específico del end point
        }
        
        if (syncApiKey.isEmpty()) {
            syncApiKey = SAuthJsonUtils.getValueOfElementAsText(config, jsonConfigKey, SSwapConsts.CFG_ATT_API_KEY); // recuperar API key específica del end point
        }
        
        syncLimit = SLibUtils.parseInt(SAuthJsonUtils.getValueOfElementAsText(config, jsonConfigKey, SSwapConsts.CFG_ATT_LIMIT));
        
        // Procesar la exportación de datos, iterando hasta completar la exportación de todos los registros recuperados:

        SResponseInfo info = new SResponseInfo(syncType);
        info.setRegistriesRetrieved(allExportDatas.size());
        Date firstRequestDatetime = null;
        
        do {
            // Determinar si los datos a sincronizar están dentro del límite permitido:
            
            boolean isSyncWithinBounds = info.getRegistriesToProcess() <= syncLimit;

            // Acotar la cantidad de datos a exportar según el límite configurado:
            
            int fromIndex = info.getIterations() * syncLimit;
            int toIndex = isSyncWithinBounds ? fromIndex + info.getRegistriesToProcess() : (info.getIterations() + 1) * syncLimit;
            ArrayList<SExportData> currentExportDatas = new ArrayList<>(allExportDatas.subList(fromIndex, toIndex));

            // Preparar el cuerpo de la petición en formato JSON:

            String requestBody = "";
            JsonNode nodeConfig = new ObjectMapper().readTree(SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG));
            String[] instanceArray = new String[] { SAuthJsonUtils.getValueOfElementAsText(nodeConfig, "", SSwapConsts.CFG_NVP_INSTANCE) };

            switch (syncType) {
                case USER:
                case PARTNER_SUPPLIER:
                case PARTNER_CUSTOMER:
                    SRequestUsersBody usersBody = new SRequestUsersBody();
                    usersBody.work_instance = instanceArray;
                    usersBody.users = (SExportDataUser[]) currentExportDatas.toArray(new SExportDataUser[0]);
                    requestBody = mapper.writeValueAsString(usersBody);
                    break;

                case AUTH_ACTOR:
                    SRequestAuthActorsBody actorsBody = new SRequestAuthActorsBody();
                    actorsBody.id_external_system = SSwapConsts.SIIE_EXT_SYS_ID;
                    actorsBody.actors = (SExportDataAuthActor[]) currentExportDatas.toArray(new SExportDataAuthActor[0]);
                    requestBody = mapper.writeValueAsString(actorsBody);
                    break;

                case AUTH_JOB_TITLE:
                    SRequestAuthOrgElementsBody orgElementsBody = new SRequestAuthOrgElementsBody();
                    orgElementsBody.id_external_system = SSwapConsts.SIIE_EXT_SYS_ID;
                    orgElementsBody.elements = (SExportDataAuthOrgElement[]) currentExportDatas.toArray(new SExportDataAuthOrgElement[0]);
                    requestBody = mapper.writeValueAsString(orgElementsBody);
                    break;
                    
                case FUNCTIONAL_AREA:
                    SRequestFunctionalAreasBody functionalAreasBody = new SRequestFunctionalAreasBody();
                    functionalAreasBody.work_instance = instanceArray;
                    functionalAreasBody.functional_areas = (SExportDataFunctionalArea[]) currentExportDatas.toArray(new SExportDataFunctionalArea[0]);
                    requestBody = mapper.writeValueAsString(functionalAreasBody);
                    break;

                case PUR_ORDER:
                    SRequestDpsBody purchaseOrderBody = new SRequestDpsBody();
                    purchaseOrderBody.work_instance = instanceArray;
                    purchaseOrderBody.documents = (SExportDataDpsContainer[]) currentExportDatas.toArray(new SExportDataDpsContainer[0]);
                    requestBody = mapper.writeValueAsString(purchaseOrderBody);
                    break;

                case PUR_REF_ORDER:
                case PUR_REF_SCALE_TICKET:
                    SRequestReferencesBody referencesBody = new SRequestReferencesBody();
                    referencesBody.work_instance = instanceArray;
                    referencesBody.references = (SExportDataReference[]) currentExportDatas.toArray(new SExportDataReference[0]);
                    requestBody = mapper.writeValueAsString(referencesBody);
                    break;
                    
                case PUR_PAYMENT:
                    SRequestPaymentsBody paymentsBody = new SRequestPaymentsBody();
                    paymentsBody.work_instance = instanceArray;
                    paymentsBody.payments = (SRequestPaymentsBody.Payment[]) currentExportDatas.toArray(new SRequestPaymentsBody.Payment[0]);
                    requestBody = mapper.writeValueAsString(paymentsBody);
                    break;
                
                case PUR_PAYMENT_UPD:
                    SRequestPaymentsUpdateBody paymentUpdatesBody = new SRequestPaymentsUpdateBody();
                    paymentUpdatesBody.work_instance = instanceArray;
                    paymentUpdatesBody.payments = (SExportDataPaymentUpdate[]) currentExportDatas.toArray(new SExportDataPaymentUpdate[0]);
                    requestBody = mapper.writeValueAsString(paymentUpdatesBody);
                    break;
                    
                default:
                    // nada
            }

            // Realizar la petición HTTP a SWAP Services:
            
            Date requestDatetime = new Date();
            String responseBody = requestSwapService("", syncUrl, SHttpConsts.METHOD_POST, requestBody, syncToken, syncApiKey, SSwapConsts.TIME_180_SEC);
            Date responseDatetime = new Date();

            if (firstRequestDatetime == null) {
                firstRequestDatetime = requestDatetime;
            }
            
            // Procesar la respuesta:
            int registriesSynced = computeResponse(session, syncType, requestBody, requestDatetime, responseBody, responseDatetime);
            info.updateIteration(currentExportDatas.size(), registriesSynced);
            
            // Mostrar el progreso de la sincronización:
            System.out.println(info.getProgress());
        } while (!info.isProcessingComplete());
        
        // Cerrar las bitácoras de sincronización:
        if (info.isProcessingComplete() && info.getRegistriesSynced() > 0) {
            closeLogSync(session, syncType, firstRequestDatetime);
        }

        return info;
    }
    
    /**
     * Despierta todos los SWAP Services, para asegurar su disponibilidad al requerirse servicios específicos.
     * @param session
     * @throws Exceptio 
     */
    private static void wakeUpServices(final SGuiSession session) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode config = mapper.readTree(SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG));
        
        String syncUrl = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_USER_SRV, SSwapConsts.CFG_ATT_URL);
        String syncToken = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_USER_SRV, SSwapConsts.CFG_ATT_TOKEN);
        String syncApiKey = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_USER_SRV, SSwapConsts.CFG_ATT_API_KEY);
        
        syncUrl += SSwapConsts.API_WAKE_UP;
        
        System.out.println("Waking-up Services...");
        String response = requestSwapService("", syncUrl, SHttpConsts.METHOD_GET, "", syncToken, syncApiKey, SSwapConsts.TIME_60_SEC);
        System.out.println("Wake-up Services Response:\n" + response);
    }
    
    /**
     * Ejecuta una consulta para obtener datos, generar un JSON y exportarlos a SWAP Services.
     * 
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param wakeUpServices Indicador para despertar todos los SWAP Services.
     * @param exportMode Modo de exportación (EXPORT_MODE_...) Tiene efecto solo si el cliente tiene GUI.
     * @return <code>SResponses</code> con la información de laS peticiones a SWAP Services.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public static SResponses exportData(final SGuiSession session, final SSyncType syncType, final boolean wakeUpServices, final int exportMode) throws SQLException, Exception {
        SResponses responses = new SResponses(syncType);
        boolean proceed = true; // si el cliente no fuera gráfico, proceder de inmediato!

        if (session.getClient() != null && ((SClientInterface) session.getClient()).isGui()) {
            // informar al usuario sobre la demora del proceso:
            
            String message = "La exportación de registros '" + SSwapUtils.translateSyncType(syncType, SLibConsts.LAN_ISO639_ES) + "' puede durar algunos segundos.";
            
            switch (exportMode) {
                case EXPORT_MODE_INFORM:
                    session.getClient().showMsgBoxInformation(message);
                    break;
                case EXPORT_MODE_CONFIRM:
                    proceed = session.getClient().showMsgBoxConfirm(message + "\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
                    break;
                default:
                    // nothing
            }
        }
        
        if (proceed) {
            System.out.println(SLibUtils.textRepeat("=", 80)); // 80: "standard" text-based screen width
            System.out.println("Exporting " + syncType + "...");
            
            if (wakeUpServices) {
                // despertar todos los servicios, para evitar excepciones por esperas excesivas:
                wakeUpServices(session);
            }
            
            SSyncType syncTypeInProgress = null;
            SResponseInfo info = null;

            try {
                switch (syncType) {
                    case USER:
                    case PUR_ORDER:
                    case PUR_REF_ORDER:
                    case PUR_REF_SCALE_TICKET:
                    case PUR_PAYMENT:
                    case PUR_PAYMENT_UPD:
                        // exportar antes áreas funcionales:
                        syncTypeInProgress = SSyncType.FUNCTIONAL_AREA;
                        info = computeRequest(session, syncTypeInProgress);
                        responses.getInfos().add(info);

                        if (syncType == SSyncType.USER || syncType == SSyncType.PUR_PAYMENT || syncType == SSyncType.PUR_PAYMENT_UPD) {
                            // para todas las exportaciones: exportar antes puestos laborales para autorización:
                            syncTypeInProgress = SSyncType.AUTH_JOB_TITLE;
                            info = computeRequest(session, syncTypeInProgress);
                            responses.getInfos().add(info);

                            if (info.isResponseOk()) {
                                // para todas las exportaciones: exportar antes actores para autorización:
                                syncTypeInProgress = SSyncType.AUTH_ACTOR;
                                info = computeRequest(session, syncTypeInProgress);
                                responses.getInfos().add(info);
                                
                                if (info.isResponseOk()) {
                                    // exportar usuarios:
                                    syncTypeInProgress = SSyncType.USER;
                                    info = computeRequest(session, syncTypeInProgress);
                                    responses.getInfos().add(info);
                                }
                            }
                        }
                        
                        if (syncType == SSyncType.PUR_ORDER || syncType == SSyncType.PUR_REF_ORDER || syncType == SSyncType.PUR_PAYMENT || syncType == SSyncType.PUR_PAYMENT_UPD) {
                            // exportar antes proveedores:
                            syncTypeInProgress = SSyncType.PARTNER_SUPPLIER;
                            info = computeRequest(session, syncTypeInProgress);
                            responses.getInfos().add(info);
                            
                            if (syncType == SSyncType.PUR_ORDER) {
                                // exportar también referencias de pedidos de compras:
                                syncTypeInProgress = SSyncType.PUR_REF_ORDER;
                                info = computeRequest(session, syncTypeInProgress);
                                responses.getInfos().add(info);
                            }
                            else if (syncType == SSyncType.PUR_PAYMENT) {
                                // exportar también actualizaciones de pagos de compras:
                                syncTypeInProgress = SSyncType.PUR_PAYMENT_UPD;
                                info = computeRequest(session, syncTypeInProgress);
                                responses.getInfos().add(info);
                            }

                            if (info.isResponseOk()) {
                                // exportar los datos solicitados:
                                syncTypeInProgress = syncType;
                                info = computeRequest(session, syncTypeInProgress);
                                responses.getInfos().add(info);
                            }
                        }
                        
                        if (syncType == SSyncType.PUR_REF_SCALE_TICKET) {
                            syncTypeInProgress = SSyncType.PUR_REF_SCALE_TICKET;
                            info = computeRequest(session, syncTypeInProgress);
                            responses.getInfos().add(info);
                        }
                        break;
                        
                    case PARTNER_SUPPLIER:
                    case PARTNER_CUSTOMER:
                        // exportar antes actores para autorización:
                        syncTypeInProgress = SSyncType.AUTH_ACTOR;
                        info = computeRequest(session, syncTypeInProgress);
                        responses.getInfos().add(info);

                        if (info.isResponseOk()) {
                            // exportar los datos solicitados:
                            syncTypeInProgress = syncType;
                            info = computeRequest(session, syncTypeInProgress);
                            responses.getInfos().add(info);
                        }
                        break;
                        
                    case AUTH_ACTOR:
                    case AUTH_JOB_TITLE:
                        if (syncType == SSyncType.AUTH_ACTOR) {
                            // exportar antes puestos laborales:
                            syncTypeInProgress = SSyncType.AUTH_JOB_TITLE;
                            info = computeRequest(session, syncTypeInProgress);
                            responses.getInfos().add(info);
                        }

                        if (info == null || info.isResponseOk()) {
                            // exportar los datos solicitados:
                            syncTypeInProgress = syncType;
                            info = computeRequest(session, syncTypeInProgress);
                            responses.getInfos().add(info);
                        }
                        break;

                    default:
                        throw new IllegalArgumentException(ERR_UNSUPPORTED_SYNC_TYPE + "'" + syncType + "'.");
                }
            }
            catch (Exception e) {
                Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, e);
                info = new SResponseInfo(syncTypeInProgress != null ? syncTypeInProgress : syncType,
                        "Error en la exportación '" + SSwapUtils.translateSyncType(syncType, SLibConsts.LAN_ISO639_ES) + "':\n"
                        + "'" + e.getMessage() + "'", false);
                responses.getInfos().add(info);
            }
        }
        
        return responses;
    }
    
    /**
     * Procesa visualmente al usuario del cliente SIIE o en consola las respuestas de SWAP Services.
     * 
     * @param session Sesión de usuario.
     * @param responses Respuestas de SWAP Services
     * @param module ID del módulo de la vista de los datos exportados.
     * @param view ID de la vista de los datos exportados.
     * @throws java.lang.Exception
     */
    public static void processResponses(final SGuiSession session, final SResponses responses, final int module, final int view) throws Exception {
        if (!responses.getInfos().isEmpty()) {
            if (responses.isResponsesOk()) {
                String message = "Los registros '" + SSwapUtils.translateSyncType(responses.getSyncType(), SLibConsts.LAN_ISO639_ES) + "' fueron exportados correctamente "
                        + "a " + SSwapConsts.SWAP_SERVICES + ":\n\n" + responses;
                
                if (session.getClient() == null || !((SClientInterface) session.getClient()).isGui()) {
                    System.out.println(message);
                }
                else {
                    session.getClient().showMsgBoxInformation(message);

                    if (module != 0 && view != 0) {
                        ((SClientInterface) session.getClient()).getGuiModule(module).refreshCatalogues(view);
                    }
                }
            }
            else {
                String message = "Ocurrió un problema al exportar los registros '" + SSwapUtils.translateSyncType(responses.getSyncType(), SLibConsts.LAN_ISO639_ES) + "' "
                        + "a " + SSwapConsts.SWAP_SERVICES + ":\n" + responses;
                
                if (session.getClient() == null || !((SClientInterface) session.getClient()).isGui()) {
                    System.out.println(message);
                }
                else {
                    session.getClient().showMsgBoxInformation(message);
                }
            }
        }
    }
    
    /**
     * Obtiene una cadena de texto con los ID de las emresas configuradas para SWAP Services para consultas SQL.
     *
     * @param session Sesión de usuario.
     * @return Cadena de texto con los ID de las emresas.
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public static String getSwapCompaniesForSqlQuery(final SGuiSession session) throws JsonProcessingException, Exception {
        JsonNode config = new ObjectMapper().readTree(SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG));
        boolean bSwapServicesLinkUp = SLibUtils.parseInt(SAuthJsonUtils.getValueOfElementAsText(config, "", SSwapConsts.CFG_NVP_LINK_UP)) == 1;
        
        String companies = "";
        if (bSwapServicesLinkUp) {
            companies = SAuthJsonUtils.getValueOfElementAsText(config, "", SSwapConsts.CFG_NVP_COMPANIES);
        }
        return companies;
    }

    /**
     * Obtiene un mapa de los nombres de las bases de datos de las emresas configuradas para SWAP Services.
     *
     * @param session Sesión de usuario.
     * @return Mapa de los nombres de las bases de datos: key = company ID; value = database name.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public static HashMap<Integer, String> getSwapCompaniesDatabasesMap(final SGuiSession session) throws SQLException, Exception {
        HashMap<Integer, String> databasesMap = new HashMap<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String sql = "SELECT id_co, bd "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_CO) + " "
                    + "WHERE id_co IN (" + getSwapCompaniesForSqlQuery(session) + ") "
                    + "ORDER BY id_co;";
            
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                databasesMap.put(resultSet.getInt("id_co"), resultSet.getString("bd"));
            }
        }
        
        return databasesMap;
    }
    
    /**
     * Obtener los ID de todas las empresas habilitadas para SWAWP Services en el ERP.
     * @param session Sesión de usuario.
     * @return Arreglo de enteros de los ID de todas las empresas habilitadas para SWAWP Services.
     * @throws Exception
     */
    public static int[] getLinkedUpSwapCompanies(final SGuiSession session) throws Exception {
        ArrayList<Integer> companies = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            ObjectMapper mapper = new ObjectMapper();
            
            String sql = "SELECT id_co, bd "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_CO) + " "
                    + "WHERE NOT b_del "
                    + "ORDER BY id_co;";
            
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String paramValue = SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG, resultSet.getString("bd"));
                JsonNode config = mapper.readTree(paramValue);
                boolean linkedUp = SLibUtils.parseInt(SAuthJsonUtils.getValueOfElementAsText(config, "", SSwapConsts.CFG_NVP_LINK_UP)) == 1;
                if (linkedUp) {
                    companies.add(resultSet.getInt("id_co"));
                }
            }
        }
        
        return companies.stream().mapToInt(Integer::intValue).toArray();
    }

    public static void requestSwapServiceToFile(String user, String url, String method, String jsonBody, String token, String apiKey, String filePath, int timeout, JProgressBar progressBar) throws Exception {
        HttpURLConnection connection = null;

        try {
            URL obj = new URL(url);
            connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod(method);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestProperty("x-api-key", apiKey);

            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonBody.getBytes("UTF-8"));
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                int totalSize = connection.getContentLength();

                try (InputStream is = connection.getInputStream(); FileOutputStream fos = new FileOutputStream(filePath)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    int totalRead = 0;

                    while ((bytesRead = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                        totalRead += bytesRead;

                        if (totalSize > 0 && progressBar != null) {
                            int progress = (int) ((totalRead * 100L) / totalSize);
                            SwingUtilities.invokeLater(() -> {progressBar.setValue(progress);});
                        }
                    }
                    SwingUtilities.invokeLater(() -> progressBar.setValue(100));
                }
            }
            else {
                InputStream errorStream = connection.getErrorStream();
                StringBuilder errorMsg = new StringBuilder();

                if (errorStream != null) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(errorStream));
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorMsg.append(line);
                    }
                    br.close();
                }
                throw new Exception("Error HTTP: " + responseCode + " -> " + errorMsg.toString());
            }

        } finally {
            if (connection != null) connection.disconnect();
        }
    }
    
    public static String getExtDataId(SClientInterface miClient, int year, int doc) throws Exception {
        String extDataId = null;

        String sql = "SELECT ext_data_id "
                + "FROM TRN_SWAP_DATA_PRC "
                + "WHERE fk_dps_year_n = " + year + " "
                + "AND fk_dps_doc_n = " + doc + " "
                + "AND b_del = 0 "
                + "LIMIT 1";

        try (Statement stmt = miClient.getSession().getStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                extDataId = rs.getString("ext_data_id");
            }
        }

        return extDataId;
    }
    
    public static String getDpsFolio(SClientInterface miClient, int year, int doc) throws Exception {
    String folio = null;

    String sql = "SELECT dt.code, d.num_ser, d.num " +
                 "FROM trn_dps AS d " +
                 "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                 "WHERE d.id_year = " + year + " " +
                 "AND d.id_doc = " + doc + " " +
                 "AND d.b_del = 0 " +
                 "LIMIT 1";

        try (Statement stmt = miClient.getSession().getStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String tip = rs.getString("dt.code");
                String numSer = rs.getString("d.num_ser");
                String num = rs.getString("d.num");
                tip = tip != null ? tip : "";
                numSer = numSer != null ? numSer : "";
                num = num != null ? num : "";
                if (numSer == null || numSer.isEmpty()) {
                    folio = tip + "/" +num;
                }
                else{
                    folio = tip + "/" + numSer + "-" + num;
                }
            }
        }

        return folio;
    }
    
    private static class BarePayment {
        
        String Database;
        int PaymentId;
        int StatusId;
        int UserId;
        
        public BarePayment(final String database, final int paymentId, final int statusId, final int userId) {
            Database = database;
            PaymentId = paymentId;
            StatusId = statusId;
            UserId = userId;
        }
    }
    
    /**
    * Muestra un diálogo modal de progreso mientras se ejecuta un proceso
    * en segundo plano.
    * El método crea una ventana con una barra de progreso indeterminada
    * para indicar al usuario que una operación está en ejecución.
    * 
    * @param parent Ventana padre sobre la cual se centrará el diálogo.
    * @param title Título del diálogo de progreso.
    * @param message Mensaje descriptivo mostrado al usuario.
    * @param process Proceso o tarea a ejecutar en segundo plano.
    */ 
    public static void showProcessDialog(Frame parent, String title, String message, Runnable process) {
        JDialog progressDialog = new JDialog(parent, title, true);
        progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        progressDialog.getRootPane().registerKeyboardAction(e -> { }, KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Procesando...");
        progressBar.setPreferredSize(new Dimension(300, 10));
        progressDialog.setLayout(new BorderLayout());
        progressDialog.add(new JLabel(message), BorderLayout.NORTH);
        progressDialog.add(progressBar, BorderLayout.CENTER);
        progressDialog.setSize(300, 85);
        progressDialog.setLocationRelativeTo(parent);

        new Thread(() -> {
            try {
                process.run();
            }
            finally {
                SwingUtilities.invokeLater(progressDialog::dispose);
            }
        }).start();

        progressDialog.setVisible(true);
    }
}

