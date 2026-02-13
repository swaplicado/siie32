package erp.mod.cfg.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.cfg.db.SDbAuthorizationStep;
import erp.mod.cfg.swap.model.FlowResponse;
import erp.mod.cfg.swap.SHttpConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.utils.SExportUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.gui.SGuiSession;

/**
 * Clase de utilidades para gestionar la autorización de documentos mediante integración con MS Auth.
 * Proporciona métodos para sincronizar flujos de autorización, consultar historial y gestionar pasos de autorización.
 * 
 * @author Edwin Carmona
 */
public class SAuthDBUtils {

    /** Logger para registrar eventos y errores de la clase */
    private static final Logger LOGGER = Logger.getLogger(SAuthDBUtils.class.getName());
    
    /** ID de usuario por defecto cuando no se encuentra un usuario válido */
    private static final int DEFAULT_USER_ID = 1;
    
    /** Tiempo de vida del caché en milisegundos (60 segundos) */
    private static final long CACHE_TTL_MS = 60000;
    
    /** Caché para almacenar detalles de flujos de autorización */
    private static final Map<String, CacheEntry> CACHE = new ConcurrentHashMap<>();
    
    /** Mapa de IDs de usuarios válidos en el sistema */
    private static Map<Integer, Boolean> validUserIds = null;

    /**
     * Clase interna para representar una entrada en el caché.
     * Almacena los detalles del flujo y su marca de tiempo.
     */
    static class CacheEntry {
        /** Detalles del flujo de autorización en formato JSON */
        JsonNode flowDetails;
        
        /** Marca de tiempo de creación de la entrada */
        long timestamp;
        
        /**
         * Constructor de la entrada de caché.
         * @param flowDetails Detalles del flujo a almacenar
         */
        CacheEntry(JsonNode flowDetails) {
            this.flowDetails = flowDetails;
            this.timestamp = System.currentTimeMillis();
        }
        
        /**
         * Verifica si la entrada del caché ha expirado.
         * @return true si ha expirado, false en caso contrario
         */
        boolean isExpired() {
            return (System.currentTimeMillis() - timestamp) > CACHE_TTL_MS;
        }
    }

    /**
     * Obtiene el ID del último usuario que insertó una autorización para un documento específico.
     * 
     * @param session Sesión de la aplicación
     * @param idYear Año del documento
     * @param idDoc ID del documento
     * @return ID del último usuario que insertó, o 0 si no se encuentra
     */
    public static int getLastUserInsert(SGuiSession session, final int idYear, final int idDoc) {
        String sql = "SELECT fid_usr_new FROM trn_dps_authorn WHERE NOT b_del AND id_year = " + idYear
                + " AND id_doc = " + idDoc + " ORDER BY id_authorn DESC, ts_edit DESC LIMIT 1";
        try (Statement stmt = session.getStatement().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("fid_usr_new");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener último usuario", e);
        }
        return 0;
    }

    /**
     * Refresca los datos de autorización desde el microservicio MS Auth.
     * Extrae los recursos de la consulta, obtiene los flujos de autorización y actualiza la base de datos.
     * Utiliza caché para evitar consultas repetitivas.
     * 
     * @param session Sesión de la aplicación
     * @param query Consulta SQL para extraer los recursos a sincronizar
     */
    public static void refreshAuthMsAuthData(SGuiSession session, final String query) {
        try {
            List<String> resourceIds = extractResourceIds(session, query);
            if (resourceIds.isEmpty()) {
                return;
            }

            String cacheKey = session.getConfigCompany().getCompanyId() + "_auth_" 
                    + SAuthorizationUtils.AUTH_TYPE_GOOGLE_DPS + "_" + String.join(",", resourceIds).hashCode();
            CacheEntry cached = CACHE.get(cacheKey);
            
            if (cached != null && !cached.isExpired()) {
                return;
            }

            JsonNode flowDetails = fetchFlowDetailsFromMsAuth(session, SSwapConsts.RESOURCE_TYPE_PUR_ORDER, resourceIds);
            if (flowDetails == null) {
                return;
            }

            loadValidUserIds(session);
            deleteAuthStepsByResources(session, resourceIds);
            List<SDbAuthorizationStep> authSteps = parseAuthorizationSteps(flowDetails);
            insertAuthorizationSteps(session, authSteps);
            CACHE.put(cacheKey, new CacheEntry(flowDetails));
        }
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al refrescar datos de autorización", ex);
        }
    }

    /**
     * Carga en memoria los IDs de usuarios válidos del sistema.
     * Se ejecuta una sola vez y almacena los resultados en un mapa estático.
     * 
     * @param session Sesión de la aplicación
     * @throws SQLException Si ocurre un error al consultar la base de datos
     */
    private static void loadValidUserIds(SGuiSession session) throws SQLException {
        if (validUserIds != null) return;
        
        validUserIds = new ConcurrentHashMap<>();
        String sql = "SELECT id_usr FROM erp.usru_usr WHERE NOT b_del";
        
        try (Statement stmt = session.getStatement().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                validUserIds.put(rs.getInt("id_usr"), Boolean.TRUE);
            }
        }
    }

    /**
     * Extrae los IDs de recursos (documentos) de una consulta SQL.
     * Los IDs se devuelven en formato "año_documento".
     * 
     * @param session Sesión de la aplicación
     * @param query Consulta SQL que contiene los documentos
     * @return Lista de IDs de recursos en formato "año_documento"
     */
    private static List<String> extractResourceIds(SGuiSession session, String query) {
        List<String> resourceIds = new ArrayList<>();
        String sql = "SELECT d.id_year, d.id_doc " + query.substring(query.toLowerCase().indexOf("from trn_dps as d"));

        try (Statement stmt = session.getStatement().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resourceIds.add(rs.getInt("id_year") + "_" + rs.getInt("id_doc"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al extraer IDs de recursos", e);
        }
        return resourceIds;
    }

    /**
     * Obtiene los detalles de flujos de autorización desde el microservicio MS Auth.
     * Realiza una petición HTTP POST al servicio externo.
     * 
     * @param session Sesión de la aplicación
     * @param resourceIds Lista de IDs de recursos a consultar
     * @return Nodo JSON con los detalles de los flujos, o null si hay error
     * @throws Exception Si ocurre un error en la comunicación con el servicio
     */
    private static JsonNode fetchFlowDetailsFromMsAuth(SGuiSession session, final int resourceType, List<String> resourceIds) throws Exception {
        System.out.println("Consultando autorizaciones en google...");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode config = mapper.readTree(SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_AUTH_CONFIG));

        String baseUrl = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AUTH_SRV, SSwapConsts.CFG_ATT_URL);
        String token = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AUTH_SRV, SSwapConsts.CFG_ATT_TOKEN);
        String apiKey = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AUTH_SRV, SSwapConsts.CFG_ATT_API_KEY);

        String url = baseUrl + "/get-flows-by-resources?id_resource_type=" + resourceType
                + "&id_external_system=1&id_company=" + session.getConfigCompany().getCompanyId() + "&all_data=0";
        String jsonBody = "{\"external_siie_ids\":\"" + String.join(";", resourceIds) + "\"}";

        String responseBody = SExportUtils.requestSwapService("", url, SHttpConsts.METHOD_POST, jsonBody, token, apiKey, SSwapConsts.TIME_180_SEC);
        JsonNode responseJson = mapper.readTree(responseBody);

        if (responseJson.has("error")) {
            LOGGER.log(Level.SEVERE, "Error desde MS Auth: " + responseJson.get("error").asText());
            return null;
        }

        return responseJson.get("flow_details");
    }
    
    /**
     * Obtiene el historial de flujo de autorización de un recurso específico desde MS Auth.
     * 
     * @param session Sesión de la aplicación
     * @param resourceTypeId Tipo de recurso (ej: orden de compra)
     * @param idResource ID del recurso en formato "año_documento"
     * @param idCompany ID de la compañía
     * @return Objeto FlowResponse con el historial del flujo, o null si hay error
     * @throws Exception Si ocurre un error en la comunicación con el servicio
     */
    public static FlowResponse fetchFlowHistoryFromMsAuth(SGuiSession session, final int resourceTypeId, final String idResource, final int idCompany) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode config = mapper.readTree(SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_AUTH_CONFIG));

        String baseUrl = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AUTH_SRV, SSwapConsts.CFG_ATT_URL);
        String token = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AUTH_SRV, SSwapConsts.CFG_ATT_TOKEN);
        String apiKey = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AUTH_SRV, SSwapConsts.CFG_ATT_API_KEY);

        String url = baseUrl + "/get-resource-flow?id_resource_type=" + resourceTypeId
                + "&id_external_system=1"
                + "&id_company=" + idCompany + ""
                + "&siie_resource_id=" + idResource;

        String responseBody = SExportUtils.requestSwapService("", url, SHttpConsts.METHOD_GET, "", token, apiKey, SSwapConsts.TIME_30_SEC);
        JsonNode responseJson = mapper.readTree(responseBody);

        if (responseJson.has("error")) {
            LOGGER.log(Level.SEVERE, "Error desde MS Auth: " + responseJson.get("error").asText());
            return null;
        }

        FlowResponse oFlow = mapper.treeToValue(responseJson.get("flow"), FlowResponse.class);

        return oFlow;
    }

    /**
     * Parsea los detalles de flujos JSON y los convierte en objetos de pasos de autorización.
     * 
     * @param flowDetails Nodo JSON con los detalles de los flujos
     * @return Lista de pasos de autorización parseados
     */
    private static List<SDbAuthorizationStep> parseAuthorizationSteps(JsonNode flowDetails) {
        List<SDbAuthorizationStep> authSteps = new ArrayList<>();

        for (JsonNode flowDetail : flowDetails) {
            String[] pk = flowDetail.get("resource_external_id").asText().split("_");
            int idYear = Integer.parseInt(pk[0]);
            int idDoc = Integer.parseInt(pk[1]);

            SDbAuthorizationStep authStep = new SDbAuthorizationStep();
            authStep.setResourceTableName_n(SModConsts.TablesMap.get(SModConsts.TRN_DPS));
            authStep.setResourcePkNum1_n(idYear);
            authStep.setResourcePkNum2_n(idDoc);
            authStep.setUserLevel(1);
            authStep.setComments(flowDetail.get("notes").asText());
            authStep.setPriority(flowDetail.get("priority").asInt());
            authStep.setFkAuthorizationTypeId(SAuthorizationUtils.AUTH_TYPE_GOOGLE_DPS);
            authStep.setFkUserStepId(extractActorId(flowDetail));

            authSteps.add(authStep);
        }

        return authSteps;
    }

    /**
     * Extrae el ID del actor (usuario) de un detalle de flujo.
     * Valida que el usuario exista en el sistema, de lo contrario retorna el usuario por defecto.
     * 
     * @param flowDetail Nodo JSON con el detalle del flujo
     * @return ID del usuario actor, o DEFAULT_USER_ID si no es válido
     */
    private static int extractActorId(JsonNode flowDetail) {
        JsonNode actorsOfAction = flowDetail.get("last_turn_action").get("actors_of_action");
        if (actorsOfAction.isArray() && actorsOfAction.size() > 0) {
            int userId = Integer.parseInt(actorsOfAction.get(0).get("external_id").asText());
            return validUserIds.containsKey(userId) ? userId : DEFAULT_USER_ID;
        }
        return DEFAULT_USER_ID;
    }

    /**
     * Elimina los pasos de autorización existentes para los recursos especificados.
     * 
     * @param session Sesión de la aplicación
     * @param resourceIds Lista de IDs de recursos en formato "año_documento"
     * @throws SQLException Si ocurre un error al ejecutar la eliminación
     */
    private static void deleteAuthStepsByResources(SGuiSession session, List<String> resourceIds) throws SQLException {
        if (resourceIds.isEmpty()) return;
        
        StringBuilder sql = new StringBuilder("DELETE FROM cfgu_authorn_step WHERE fk_tp_authorn = ")
                .append(SAuthorizationUtils.AUTH_TYPE_GOOGLE_DPS)
                .append(" AND (res_pk_n1_n, res_pk_n2_n) IN (");
        
        for (int i = 0; i < resourceIds.size(); i++) {
            String[] pk = resourceIds.get(i).split("_");
            sql.append("(").append(pk[0]).append(",").append(pk[1]).append(")");
            if (i < resourceIds.size() - 1) sql.append(",");
        }
        sql.append(")");
        
        try (Statement stmt = session.getStatement().getConnection().createStatement()) {
            stmt.executeUpdate(sql.toString());
        }
    }

    /**
     * Inserta los pasos de autorización en la base de datos.
     * 
     * @param session Sesión de la aplicación
     * @param authSteps Lista de pasos de autorización a insertar
     * @throws Exception Si ocurre un error al guardar los pasos
     */
    private static void insertAuthorizationSteps(SGuiSession session, List<SDbAuthorizationStep> authSteps) throws Exception {
        if (authSteps.isEmpty()) return;
        
        for (SDbAuthorizationStep authStep : authSteps) {
            authStep.save(session);
        }
    }
}
