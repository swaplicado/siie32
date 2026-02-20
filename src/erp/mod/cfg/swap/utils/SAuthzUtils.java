package erp.mod.cfg.swap.utils;

import com.fasterxml.jackson.databind.JsonNode;
import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.utils.SAuthDBUtils;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mtrn.data.SDataDps;
import erp.siieapp.SAppLinkResponse;
import erp.siieapp.SAuthorizationsAPI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.gui.SGuiSession;

/**
 * Utilidades para la gestión de autorizaciones en el sistema SWAP.
 * Proporciona métodos para sincronizar y validar el estado de autorizaciones
 * entre el microservicio de autorizaciones y la base de datos local.
 *
 * @author Edwin Carmona
 * @version 3.2
 */
public class SAuthzUtils {
    
    private static final Logger LOGGER = Logger.getLogger(SAuthzUtils.class.getName());
    
    /**
     * Fuerza la verificación y sincronización del estado de autorización de un recurso
     * entre el microservicio de autorizaciones y la base de datos local.
     * 
     * Este método consulta el estado actual del flujo de autorización en el microservicio
     * y actualiza el estado local si existe una discrepancia, garantizando la consistencia
     * de datos entre ambos sistemas.
     *
     * @param session Sesión activa de la GUI con conexión a base de datos
     * @param authzResourceType Tipo de recurso a autorizar (ej: RESOURCE_TYPE_PUR_ORDER)
     * @param pk Clave primaria del recurso, debe ser un array de int[] para DPS
     * @return Cadena vacía si la operación fue exitosa, mensaje de error en caso contrario
     */
    public static String forceCheckAuthStatus(SGuiSession session, final int authzResourceType, final Object pk) {
        // Validación de parámetros de entrada
        if (session == null) {
            return "Error: La sesión no puede ser nula";
        }
        if (pk == null) {
            return "Error: La clave primaria no puede ser nula";
        }
        
        try {
            // Solo procesar órdenes de compra por ahora
            if (authzResourceType != SSwapConsts.RESOURCE_TYPE_PUR_ORDER) {
                return "";
            }
            
            // Validar que pk sea del tipo esperado
            if (!(pk instanceof int[])) {
                return "Error: La clave primaria debe ser un array de enteros";
            }
            
            final int[] pkArray = (int[]) pk;
            if (pkArray.length < 2) {
                return "Error: La clave primaria debe contener al menos 2 elementos";
            }
            
            // Inicializar API y objetos de datos
            final SAuthorizationsAPI authApi = new SAuthorizationsAPI(session);
            final SDataDps dpsData = new SDataDps();
            
            // Construir identificador del objeto para el microservicio
            final List<String> objectPk = new ArrayList<>(1);
            objectPk.add(pkArray[0] + "_" + pkArray[1]);
            
            // Consultar detalles del flujo desde el microservicio de autorizaciones
            final JsonNode flowNode = SAuthDBUtils.fetchFlowDetailsFromMsAuth(session, authzResourceType, objectPk);
            
            if (flowNode != null && flowNode.isArray() && flowNode.size() > 0) {
                // El recurso existe en el sistema de autorizaciones
                processExistingAuthFlow(session, authApi, dpsData, pk, flowNode);
            } else {
                // El recurso no existe en el sistema de autorizaciones
                resetAuthorizationStatus(session, authApi, pk);
            }
            
            return "";
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al verificar estado de autorización para recurso tipo " 
                    + authzResourceType + ": " + ex.getMessage(), ex);
            return "Error al consultar estatus de autorización: " + ex.getMessage();
        }
    }
    
    /**
     * Procesa un flujo de autorización existente, sincronizando el estado local
     * con el estado del microservicio de autorizaciones.
     *
     * @param session Sesión activa de la GUI
     * @param authApi API de autorizaciones
     * @param dpsData Objeto de datos DPS
     * @param pk Clave primaria del recurso
     * @param flowNode Nodo JSON con los detalles del flujo de autorización
     * @throws Exception Si ocurre un error durante el procesamiento
     */
    private static String processExistingAuthFlow(SGuiSession session, SAuthorizationsAPI authApi, 
            SDataDps dpsData, Object pk, JsonNode flowNode) throws Exception {
        
        // Leer datos actuales del DPS
        dpsData.read(pk, session.getStatement());
        
        final JsonNode firstFlow = flowNode.get(0);
        final int flowStatus = firstFlow.get("flow_status").asInt();
        final String notes = firstFlow.has("notes") ? firstFlow.get("notes").asText() : "";
        
        // Extraer ID del actor que realizó la última acción
        int actorExternalId = extractActorExternalId(firstFlow);
        actorExternalId = SAuthDBUtils.isValidUserId(session, actorExternalId);
        
        // Sincronizar estado según el flujo de autorización
        final List<String> objectPk = new ArrayList<>(1);
        objectPk.add(((int[]) pk)[0] + "_" + ((int[]) pk)[1]);
        
        switch (flowStatus) {
            case SSwapConsts.AUTHZ_STATUS_OK:
                // Autorización aprobada: actualizar si el estado local difiere
                if (dpsData.getFkDpsAuthorizationStatusId() != SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN) {
                    SAppLinkResponse oResponse =authApi.approbeResource(SAuthorizationUtils.AUTH_TYPE_DPS, pk, actorExternalId, notes, true);
                    if (oResponse.getCode() > 201) {
                        LOGGER.log(Level.WARNING, "Error al aprobar recurso en microservicio de autorizaciones: {0}", oResponse.getMessage());
                        return oResponse.getMessage();
                    }
                }
                break;
            
            case SSwapConsts.AUTHZ_STATUS_REJECTED:
                // Autorización rechazada: actualizar si el estado local difiere
                if (dpsData.getFkDpsAuthorizationStatusId() != SDataConstantsSys.TRNS_ST_DPS_AUTHORN_REJECT) {
                    String sResponse = authApi.rejectResource(SAuthorizationUtils.AUTH_TYPE_DPS, pk, actorExternalId, notes, true);
                    if (!sResponse.isEmpty()) {
                        LOGGER.log(Level.WARNING, "Error al rechazar recurso en microservicio de autorizaciones: {0}", sResponse);
                        return sResponse;
                    }
                }
                break;

            case SSwapConsts.AUTHZ_STATUS_IN_PROGRESS:
                if (dpsData.getFkDpsAuthorizationStatusId() != SDataConstantsSys.TRNS_ST_DPS_AUTHORN_PENDING) {
                    int idUser = session.getUser().getPkUserId();
                    String cancelNotes = "Cancelación de flujo desde siie por inconsistencia";
                    SServicesUtils.RejectData rejectData = new SServicesUtils.RejectData(idUser, "", cancelNotes);
                    SDataRejectResource data = new SDataRejectResource();

                    data.id_external_system = SSwapConsts.SIIE_EXT_SYS_ID;
                    data.id_company = session.getConfigCompany().getCompanyId();
                    data.id_resource_type = SSwapConsts.RESOURCE_TYPE_PUR_ORDER;
                    data.siie_resource_id = "" + dpsData.getPkYearId() + "_" + dpsData.getPkDocId();
                    data.id_actor_type = SExportDataAuthActor.ACTOR_TYPE_USER;
                    data.external_user_id = rejectData.UserId;
                    data.notes = rejectData.Notes;
                    
                    boolean wasCancelled = SServicesUtils.requestCancelFlow(session, SModConsts.TRN_DPS, data);

                    if (wasCancelled) {
                        // Actualizar estatus de autorización:
                        int status = 0;
                        switch (dpsData.getFkDpsAuthorizationStatusId()) {
                            case SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN:
                                status = SDataConstantsSys.CFGS_ST_AUTHORN_AUTH;
                                break;
                            
                            case SDataConstantsSys.TRNS_ST_DPS_AUTHORN_REJECT:
                                status = SDataConstantsSys.CFGS_ST_AUTHORN_REJ;
                                break;

                            case SDataConstantsSys.TRNS_ST_DPS_AUTHORN_NA:
                                status = SDataConstantsSys.CFGS_ST_AUTHORN_SNDF;
                                break;
                        
                            default:
                                break;
                        }
                        final int systemUserId = 0;
                        authApi.updateDpsAuthornStatus(pk, status, systemUserId);
                    }
                    else {
                        LOGGER.log(Level.WARNING, "No se pudo cancelar el flujo de autorización pendiente para DPS[{0},{1}]", new Object[]{((int[]) pk)[0], ((int[]) pk)[1]});
                        return "No se pudo sincronizar el estatus del registro, "
                                + "se necesita cancelar el flujo en servicio de autorizaciones. \n"
                                + "Contacte a soporte técnico.";
                    }
                }
                break;
            
            default:
                // Estados intermedios o desconocidos: no requieren acción
                break;
        }

        return "";
    }
    
    /**
     * Extrae el ID externo del actor que realizó la última acción en el flujo.
     *
     * @param flowNode Nodo JSON con los detalles del flujo
     * @return ID externo del actor, o 0 si no se encuentra
     */
    private static int extractActorExternalId(JsonNode flowNode) {
        if (flowNode.has("last_turn_action")) {
            final JsonNode lastAction = flowNode.get("last_turn_action");
            if (lastAction.has("actors_of_action")) {
                final JsonNode actors = lastAction.get("actors_of_action");
                if (actors.isArray() && actors.size() > 0 && actors.get(0).has("external_id")) {
                    return actors.get(0).get("external_id").asInt();
                }
            }
        }
        return 0;
    }
    
    /**
     * Resetea el estado de autorización de un recurso que no existe en el
     * microservicio de autorizaciones pero tiene estado local.
     * 
     * Esto ocurre cuando un registro fue eliminado del sistema de autorizaciones
     * pero aún mantiene estado en la base de datos local.
     *
     * @param session Sesión activa de la GUI
     * @param authApi API de autorizaciones
     * @param pk Clave primaria del recurso
     * @throws Exception Si ocurre un error durante el reseteo
     */
    private static void resetAuthorizationStatus(SGuiSession session, SAuthorizationsAPI authApi, 
            Object pk) throws Exception {
        
        // Verificar si el recurso tiene estado de autorización local
        if (SAuthorizationUtils.hasAuthornStatus(session, (int[]) pk)) {
            // Resetear a estado "Sin Definir" con usuario sistema (ID 0)
            final int systemUserId = 0;
            authApi.updateDpsAuthornStatus(pk, SDataConstantsSys.CFGS_ST_AUTHORN_SNDF, systemUserId);
        }
    }
}
