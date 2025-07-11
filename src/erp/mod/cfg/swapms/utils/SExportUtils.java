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
     * @param session Sesión de usuario para acceder a la base de datos.
     * @param sSyncType Tipo de sincronización (USUARIO o PROVEEDOR).
     * @param bSyncAll Si es true, sincroniza todos los registros; si es false, solo los nuevos/cambiados.
     * @return String con el JSON generado.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public static String exportJsonData(SGuiSession session, final String sSyncType, final boolean bSyncAll) throws SQLException {
        try {
            Date oDate;
            if (! bSyncAll) {
                oDate = SExportUtils.getLastSyncDateTime(session.getStatement(), sSyncType);
            } else {
                oDate = null; // Si se sincroniza todo, no hay fecha de última sincronización
            }
            String sSwapServiceConfig = SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICE_CONFIG);
            // Transformar la configuración en un objeto JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(sSwapServiceConfig);
            String sUrl = "";

            List<SUserExport> lData = null;
            switch (sSyncType) {
                case SExportUtils.EXPORT_SYNC_USERS:
                    lData = SExportUtils.getListOfUsersToExport(session.getStatement(), oDate);
                    sUrl = SAuthJSONUtils.getValueOfElement(rootNode, "", "user_sync_url");
                    break;
                case SExportUtils.EXPORT_SYNC_SUPPLIERS:
                    lData = SExportUtils.getListSuppliersToExport(session.getStatement(), oDate);
                    sUrl = SAuthJSONUtils.getValueOfElement(rootNode, "", "user_sync_url");
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de sincronización no soportado: " + sSyncType);
            }

            String sData = "";
            if (lData != null && !lData.isEmpty()) {
                SBodyExport body = new SBodyExport();
                body.work_instance = new String[] { "1" }; // Asumiendo que siempre es la instancia 1
                body.users = lData.toArray(new SUserExport[0]);

                try {
                    sData = mapper.writeValueAsString(body);
                } catch (Exception ex) {
                    throw new RuntimeException("Error serializando JSON", ex);
                }
                String sToken = SAuthJSONUtils.getValueOfElement(rootNode, "", "user_sync_token");
                
                SDbSyncLog oDbLog = new SDbSyncLog();
                oDbLog.setSyncType(sSyncType);
                oDbLog.setRequestBody(sData);
                oDbLog.setRequestTimestamp(new Date());
                
                String sBodyResponse = SExportUtils.requestSwapService("", sUrl, "POST", sData, sToken);
                
                oDbLog.setResponseCode("200");
                oDbLog.setResponseBody(sBodyResponse);
                oDbLog.setResponseTimestamp(new Date());
                oDbLog.save(session);
            }
            else {
                SDbSyncLog oDbLog = new SDbSyncLog();
                oDbLog.setSyncType(sSyncType);
                oDbLog.setRequestBody("");
                oDbLog.setRequestTimestamp(new Date());
                oDbLog.setResponseCode("204"); // No content
                oDbLog.setResponseBody("No hay datos para exportar.");
                oDbLog.setResponseTimestamp(new Date());
                oDbLog.save(session);
                
                return ""; // No hay datos para exportar
            }
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
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
        String query = "SELECT  "
                + "    * "
                + "FROM "
                + "    cfgu_sync_log "
                + "WHERE "
                + "    response_code = '200' AND sync_type = '" + sSyncType + "';";

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
        String sql = "SELECT "
                + "    u.id_usr, "
                + "    u.usr, "
                + "    u.email, "
                + "    u.fid_bp_n, "
                + "    bp.bp, "
                + "    bp.lastname, "
                + "    bp.firstname, "
                + "    u.ts_edit "
                + "FROM"
                + "    erp.usru_usr AS u "
                + "        LEFT JOIN "
                + "    erp.bpsu_bp AS bp ON u.fid_bp_n = bp.id_bp "
                + (oLastSyncDateTime != null
                        ? "WHERE u.ts_usr_ins > '" + oLastSyncDateTime
                                + "' AND NOT u.b_del AND (bp.b_del IS NULL OR NOT bp.b_del)"
                        : "WHERE NOT u.b_del AND (bp.b_del IS NULL OR NOT bp.b_del)");

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
        String sql = BASE_PROVIDER_QUERY + 
            "WHERE bp.b_sup ";
        
        if (oLastSyncDateTime != null) {
            sql += " AND bp.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(oLastSyncDateTime) + "' ";
        }
        else {
            sql += "AND NOT bp.b_del ";
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

            ObjectMapper mapper = new ObjectMapper();
            JsonNode resp = mapper.readTree(responseBody);
            if (SAuthJSONUtils.containsElement(resp, "", "resultados")) {
//                JsonNode resultadosNode = resp.path("resultados");
//                if (resultadosNode.isArray()) {
//                    List<SUserExport> users = new ArrayList<>();
//                    for (JsonNode userNode : resultadosNode) {
//                        SUserExport user = mapper.treeToValue(userNode, SUserExport.class);
//                        users.add(user);
//                    }
//                    System.out.println("Usuarios exportados: " + users.size());
//                } else {
//                    System.out.println("No se encontraron resultados en la respuesta.");
//                }
            }
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
}
