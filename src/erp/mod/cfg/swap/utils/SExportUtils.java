/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbComSyncLog;
import erp.mod.cfg.db.SDbComSyncLogEntry;
import erp.mod.cfg.db.SDbSyncLog;
import erp.mod.cfg.db.SDbSyncLogEntry;
import erp.mod.cfg.swap.SHttpConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.SSwapUtils;
import erp.mod.cfg.swap.SSyncType;
import erp.mod.cfg.utils.SAuthJsonUtils;
import erp.mod.fin.db.SDbPayment;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
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
 * @author Edwin Carmona, Sergio Flores
 */
public abstract class SExportUtils {
    
    public static final DecimalFormat FormatSyncLogId = new DecimalFormat("000000"); // 6 positions
    public static final SimpleDateFormat FormatSyncLogDatetime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    
    public static final int DECS_STD_AMOUNT = 2;
    public static final int DECS_STD_EXC_RATE = 4;
    public static final int DECS_PAY_EXC_RATE = 4;
    public static final int DECS_PAY_CONV_RATE = 8;
    
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
     * @param queryUrl Parámetros de consulta para la URL (opcional).
     * @param serviceUrl URL del servicio al que se realiza la solicitud.
     * @param method Método HTTP a utilizar (GET, POST, PUT, etc.).
     * @param body Cuerpo de la solicitud (para métodos como POST).
     * @param token Token de autorización (opcional).
     * @param apiKey API Key de autorización (opcional).
     * @param timeout Timeout en segundos.
     * @return Respuesta del servicio en formato JSON.
     * @throws Exception
     */
    public static String requestSwapService(final String queryUrl, final String serviceUrl, final String method, final String body, final String token, final String apiKey, final int timeout) throws Exception {
        String responseBody = "";
        HttpURLConnection connection = null;

        try {
            URL url;
            String charset = java.nio.charset.StandardCharsets.UTF_8.name();
            
            if (SHttpConsts.METHOD_GET.equalsIgnoreCase(method) && queryUrl != null && !queryUrl.isEmpty()) {
                url = new URL(serviceUrl + "?" + queryUrl);
            }
            else {
                url = new URL(serviceUrl);
            }

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(timeout); // timeout para conectar
            connection.setReadTimeout(timeout); // timeout para leer la respuesta
            connection.setRequestMethod(method.toUpperCase());
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            if (token != null && !token.isEmpty()) {
                connection.setRequestProperty("Authorization", token);
            }
            if (apiKey != null && !apiKey.isEmpty()) {
                connection.setRequestProperty("x-api-key", apiKey);
            }
            connection.setDoInput(true);

            // Para métodos que envían datos (POST, PUT, etc.)
            
            boolean isBodySent = false; // 2025-08-13, Sergio Flores: ¡no es claro el propósito de esta variable, declarada desde la versión inicial de este método!
            
            if (!SHttpConsts.METHOD_GET.equalsIgnoreCase(method)) {
                if (body != null && !body.trim().isEmpty()) {
                    // Validar que el body sea un JSON válido
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.readTree(body); // Lanza excepción si no es JSON válido
                    }
                    catch (JsonProcessingException ex) {
                        throw new IllegalArgumentException("El parámetro 'sBody' no es un JSON válido.", ex);
                    }
                    connection.setDoOutput(true);
                    try (java.io.OutputStream os = connection.getOutputStream()) {
                        byte[] input = body.getBytes(charset);
                        os.write(input, 0, input.length);
                    }
                    isBodySent = true;
                }
                else if (queryUrl != null && !queryUrl.isEmpty()) {
                    connection.setDoOutput(true);
                    try (java.io.OutputStream os = connection.getOutputStream()) {
                        byte[] input = queryUrl.getBytes(charset);
                        os.write(input, 0, input.length);
                    }
                    isBodySent = true;
                }
            }

            int status = connection.getResponseCode();
            InputStream responseStream = (status >= SHttpConsts.RSC_SUCC_OK && status < SHttpConsts.RSC_ERR_BAD_REQUEST) ? connection.getInputStream() : connection.getErrorStream();

            try (Scanner scanner = new Scanner(responseStream, charset)) {
                responseBody = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            }

            System.out.println("Respuesta desde " + url);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
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
    protected static int logSync(final SGuiSession session, final SSyncType syncType, final String requestBody, final Date requestDatetime, final int httpResponseStatusCode, final String responseBody, final Date responseDatetime, final ArrayList<SDbSyncLogEntry> syncLogEntries) throws SQLException, Exception {
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
                            case PUR_PAYMENT:
                            case PUR_PAYMENT_UPD:
                                log = new SDbComSyncLog();
                                break;

                            default:
                                throw new IllegalArgumentException(ERR_UNSUPPORTED_SYNC_TYPE + "'" + syncType + "'.");
                        }
                        
                        // complete processing before log sync:
                        
                        ArrayList<SDbSyncLogEntry> entries = syncLogEntriesPerDatabaseMap.get(database);
                        
                        if (syncType == SSyncType.PUR_PAYMENT) {
                            Object value = new Object[] { SModSysConsts.FINS_ST_PAY_PRC_AUTH, session.getUser().getPkUserId(), database };
                            complementProcessing(session, syncType, entries, value);
                        }
                        else if (syncType == SSyncType.PUR_PAYMENT_UPD) {
                            complementProcessing(session, syncType, entries, null); // el argumento 'value' se definirá individualmente para cada partida de sincronización
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
            case PUR_PAYMENT:
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
                // cambiar el estatus de los nuevos pagos recién enviados a SWAP Services para su autorización:
                
                SDbPayment payment = new SDbPayment();
                
                for (SDbSyncLogEntry entry : entries) {
                    payment.saveField(session.getStatement(), new int[] { SLibUtils.parseInt(entry.getReferenceId()) }, SDbPayment.FIELD_STATUS_PAYMENT, value);
                }
                break;
                
            case PUR_PAYMENT_UPD:
                // cambiar el estatus de los nuevos pagos recién enviados a SWAP Services para su autorización:
                
                for (SDbSyncLogEntry entry : entries) {
                    int paymentId = SLibUtils.parseInt(entry.getReferenceId());
                    SDbPayment paymentToUpdate = (SDbPayment) session.readRegistry(SModConsts.FIN_PAY, new int[] { paymentId });
                    int newStatusPayment = SDbPayment.getSettledStatusPayment(paymentToUpdate.getFkStatusPaymentId());
                    Object valueToUpdate = new Object[] { newStatusPayment, paymentToUpdate.getFkUserUpdateId(), entry.getAuxDatabase() };
                    
                    paymentToUpdate.saveField(session.getStatement(), new int[] { SLibUtils.parseInt(entry.getReferenceId()) }, SDbPayment.FIELD_STATUS_PAYMENT, valueToUpdate);
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
        
        if (((SClientInterface) session.getClient()).isDev()) {
            // hosts para pruebas:
            
            switch (syncType) {
                case USER:
                case PARTNER_SUPPLIER:
                case PARTNER_CUSTOMER:
                case FUNCTIONAL_AREA:
                    testHost = "https://api-usuarios-test-515680676790.europe-west1.run.app";
                    break;

                case PUR_ORDER:
                case PUR_REF_ORDER:
                    testHost = "https://transaction-backend-test-515680676790.europe-west1.run.app";
                    break;
                    
                case PUR_PAYMENT:
                    testHost = "http://192.168.7.43:8003"; // today host in César Orozco's (30/09/2025)
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
            case PUR_PAYMENT:
                cfgParamKey = SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG;
                jsonBaseKey = SSwapConsts.CFG_OBJ_TXN_SRV;
                
                switch (syncType) {
                    case PUR_ORDER:
                        jsonConfigKey = SSwapConsts.CFG_OBJ_TXN_PUR_DOC;
                        break;

                    case PUR_REF_ORDER:
                        jsonConfigKey = SSwapConsts.CFG_OBJ_TXN_PUR_REF;
                        break;
                        
                    case PUR_PAYMENT:
                        jsonConfigKey = SSwapConsts.CFG_OBJ_TXN_PUR_PAY;
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
            
            syncToken = SAuthJsonUtils.getValueOfElementAsText(config, jsonBaseKey, SSwapConsts.CFG_ATT_TOKEN);
            
            if (!testApyKey.isEmpty()) {
                syncApiKey = testApyKey;
            }
            else {
                syncApiKey = SAuthJsonUtils.getValueOfElementAsText(config, jsonBaseKey, SSwapConsts.CFG_ATT_API_KEY);
            }
        }
        
        // Recuperar la configuración del servicio:
        
        syncUrl += SAuthJsonUtils.getValueOfElementAsText(config, jsonConfigKey, SSwapConsts.CFG_ATT_URL); // complementar la URL
        
        if (syncToken.isEmpty()) {
            syncToken = SAuthJsonUtils.getValueOfElementAsText(config, jsonConfigKey, SSwapConsts.CFG_ATT_TOKEN); // recuperar token específico
        }
        
        if (syncApiKey.isEmpty()) {
            syncApiKey = SAuthJsonUtils.getValueOfElementAsText(config, jsonConfigKey, SSwapConsts.CFG_ATT_API_KEY); // recuperar API key específica
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
            String[] instanceArray = new String[] { "" + ((SClientInterface) session.getClient()).getSwapServicesSetting(SSwapConsts.CFG_NVP_INSTANCE) };

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
     * @param confirmToProceed Si el cliente es gráfico, solicitar confirmación para proceder con la sincronización, si no, solamente informarlo.
     * @param wakeUpServices Indicador para despertar todos los SWAP Services.
     * @return <code>SResponses</code> con la información de laS peticiones a SWAP Services.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public static SResponses exportData(final SGuiSession session, final SSyncType syncType, final boolean confirmToProceed, final boolean wakeUpServices) throws SQLException, Exception {
        SResponses responses = new SResponses(syncType);
        boolean proceed = true; // si el cliente no fuera gráfico, proceder de inmediato!

        if (((SClientInterface) session.getClient()).isGui()) {
            // informar al usuario sobre la demora del proceso:
            
            String message = "La exportación de registros '" + SSwapUtils.translateSyncType(syncType, SLibConsts.LAN_ISO639_ES) + "' puede durar algunos segundos.";
            
            if (confirmToProceed) {
                proceed = session.getClient().showMsgBoxConfirm(message + "\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
            }
            else {
                session.getClient().showMsgBoxInformation(message);
            }
        }
        
        if (proceed) {
            System.out.println(SLibUtils.textRepeat("=", 80));
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
                                // exportar antes referencias de pedidos de compras:
                                syncTypeInProgress = SSyncType.PUR_REF_ORDER;
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
                        break;
                        
                    case PARTNER_SUPPLIER:
                    case PARTNER_CUSTOMER:
                        // exportar antes actores para autorización:
                        syncTypeInProgress = SSyncType.AUTH_ACTOR;
                        info = computeRequest(session, syncTypeInProgress);
                        responses.getInfos().add(info);

                        if (info.isResponseOk()) {
                            if (info.isResponseOk()) {
                                // exportar los datos solicitados:
                                syncTypeInProgress = syncType;
                                info = computeRequest(session, syncTypeInProgress);
                                responses.getInfos().add(info);
                            }
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
                
                if (!((SClientInterface) session.getClient()).isGui()) {
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
                
                if (!((SClientInterface) session.getClient()).isGui()) {
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
     */
    public static String getSwapCompaniesForSqlQuery(final SGuiSession session) {
        int[] companies = (int[]) ((SClientInterface) session.getClient()).getSwapServicesSetting(SSwapConsts.CFG_NVP_COMPANIES);
        return Arrays.stream(companies).mapToObj(String::valueOf).collect(Collectors.joining(", "));
    }

    /**
     * Obtiene un mapa de los nombres de las bases de datos de las emresas configuradas para SWAP Services.
     *
     * @param session Sesión de usuario.
     * @return Mapa de los nombres de las bases de datos: key = company ID; value = database name.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public static HashMap<Integer, String> getSwapCompaniesDatabasesMap(final SGuiSession session) throws SQLException {
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
}
