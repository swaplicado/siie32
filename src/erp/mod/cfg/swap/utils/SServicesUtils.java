/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbComImportLog;
import erp.mod.cfg.db.SDbComImportLogEntry;
import erp.mod.cfg.swap.SHttpConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.utils.SAuthJsonUtils;
import erp.mod.fin.db.SDbPayment;
import java.util.Date;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SServicesUtils {
    
    public static final String URL_FORCE_REJECT_RESOURCE = "/api/force-reject-resource";
    public static final String URL_CANCEL_FLOW = "/api/cancel-flow";
    public static final String URL_GET_RESOURCE_FLOW = "/get-resource-flow";
    
    private static SyncSettings getSyncSettings(final SGuiSession session, final ObjectMapper mapper, final String configParamKey, final String elementKey) throws Exception {
        JsonNode config = mapper.readTree(SCfgUtils.getParamValue(session.getStatement(), configParamKey));
        
        String syncUrl = SAuthJsonUtils.getValueOfElementAsText(config, elementKey, SSwapConsts.CFG_ATT_URL);
        String syncToken = SAuthJsonUtils.getValueOfElementAsText(config, elementKey, SSwapConsts.CFG_ATT_TOKEN); // recuperar token genérico del end point
        String syncApiKey = SAuthJsonUtils.getValueOfElementAsText(config, elementKey, SSwapConsts.CFG_ATT_API_KEY); // recuperar API key genérica del end point
        
        return new SyncSettings(syncUrl, syncToken, syncApiKey);
    }
    
    /**
     * Rechazar un recurso autorizado de manera forzada.
     * @param session Sesión de usuario.
     * @param data Datos del recurso autorizado a rechazar.
     * @throws Exception Si ocurre un error en la petición.
     */
    public static void requestRejectResource(final SGuiSession session, final SDataRejectResource data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SyncSettings syncSettings = getSyncSettings(session, mapper, SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG, SSwapConsts.CFG_OBJ_TXN_SRV);
        
        syncSettings.Url += URL_FORCE_REJECT_RESOURCE;
        
        String requestBody = mapper.writeValueAsString(data);
        Date requestDatetime = new Date();
        String responseBody = SExportUtils.requestSwapService("", syncSettings.Url, SHttpConsts.METHOD_PATCH, requestBody, syncSettings.Token, syncSettings.ApiKey, SSwapConsts.TIME_180_SEC);
        Date responseDatetime = new Date();

        // Procesar la respuesta:
        
        boolean ok = false;
        JsonNode responseJson = mapper.readTree(responseBody);
        
        if (SAuthJsonUtils.containsElement(responseJson, "", "status_code")) {
            JsonNode status = responseJson.path("status_code");
            String code = status.asText();
            
            if (code.equals("" + SHttpConsts.RSC_SUCC_OK)) {
                switch (data.id_resource_type) {
                    case SSwapConsts.RESOURCE_TYPE_PUR_INVOICE:
                        SDbComImportLogEntry entry = new SDbComImportLogEntry();
                        entry.setReferenceUuid(data.external_resource_uuid);
                        entry.setResponseCode(code);
                        entry.setResponseBody(SJsonUtils.sanitizeJson(responseBody));
                        entry.setReferenceId("" + data.external_resource_id);
                        entry.setReferenceUuid(data.external_resource_uuid);

                        //int registriesSynced = SExportUtils.computeResponse(session, null, requestBody, requestDatetime, responseBody, responseDatetime);

                        SDbComImportLog log = new SDbComImportLog();
                        log.setSyncType(SDbComImportLog.SYNC_TYPE_PUR_INV);
                        log.setRequestTimestamp(requestDatetime);
                        log.setResponseCode(code);
                        log.setResponseTimestamp(responseDatetime);
                        log.getEntries().add(entry);
                        log.save(session);
                        
                        SExportLogsUtils.safeWriteToLogFile(log.getRequestBodyFileName(), requestBody);
                        SExportLogsUtils.safeWriteToLogFile(log.getResponseBodyFileName(), responseBody);
                        
                        break;
                        
                    case SSwapConsts.RESOURCE_TYPE_PUR_ORDER:
                        String[] sPkDps = data.siie_resource_id.split("_");
                        int[] pkDps = new int[] { SLibUtils.parseInt(sPkDps[0]), SLibUtils.parseInt(sPkDps[1]) };
                        String sqlDpsAuth = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_AUTHORN) + " "
                                + "SET fid_st_authorn = " + (SDataConstantsSys.CFGS_ST_AUTHORN_REJ) + ",  "
                                + "fid_usr_edit = " + data.external_user_id + ", "
                                + "ts_edit = NOW() "
                                + "WHERE id_year = " + pkDps[0] + " AND id_doc = " + pkDps[1] + " AND NOT b_del;";
                        session.getStatement().execute(sqlDpsAuth);
                        
                        break;
                        
                    default:
                }
                
                ok = true;
            }
        }
        
        if (!ok) {
            String error = "No fue posible procesar la solicitud.";
            
            if (SAuthJsonUtils.containsElement(responseJson, "", "message")) {
                JsonNode message = responseJson.path("message");
                error += "\n" + message.asText();
            }
            else if (SAuthJsonUtils.containsElement(responseJson, "", "detail")) {
                JsonNode detail = responseJson.path("detail");
                error += "\n" + detail.asText();
            }
            
            throw new Exception(error);
        }
    }
    
    /**
     * Rechazar un recurso autorizado de manera forzada.
     * @param session Sesión de usuario.
     * @param registryType Tipo de registro (SModConsts...)
     * @param data Datos del recurso autorizado a rechazar.
     * @return 
     * @throws Exception Si ocurre un error en la petición.
     */
    public static boolean requestCancelFlow(final SGuiSession session, final int registryType, final SDataRejectResource data) throws Exception {
        switch (registryType) {
            case SModConsts.FIN_PAY:
            case SModConsts.TRN_DPS:
                break;
                
            default:
                throw new UnsupportedOperationException(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n(Tipo de registro: " + registryType + ")");
        }
        
        ObjectMapper mapper = new ObjectMapper();
        SyncSettings syncSettings = getSyncSettings(session, mapper, SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG, SSwapConsts.CFG_OBJ_TXN_SRV);
        
        syncSettings.Url += URL_CANCEL_FLOW;
        
        String requestBody = mapper.writeValueAsString(data);
        //Date requestDatetime = new Date();
        String responseBody = SExportUtils.requestSwapService("", syncSettings.Url, SHttpConsts.METHOD_PATCH, requestBody, syncSettings.Token, syncSettings.ApiKey, SSwapConsts.TIME_180_SEC);
        //Date responseDatetime = new Date();

        // Procesar la respuesta:
        
        boolean ok = false;
        JsonNode responseJson = mapper.readTree(responseBody);
        
        if (SAuthJsonUtils.containsElement(responseJson, "", "status_code")) {
            JsonNode status = responseJson.path("status_code");
            String code = status.asText();
            
            if (code.equals("" + SHttpConsts.RSC_SUCC_OK)) {
                switch (registryType) {
                    case SModConsts.FIN_PAY:
                        SDbPayment payment = (SDbPayment) session.readRegistry(registryType, new int[] { SLibUtils.parseInt(data.siie_resource_id) });
                        payment.updatePaymentStatus(session, SModSysConsts.FINS_ST_PAY_REJC);
                        break;
                    case SModConsts.TRN_DPS:
                        break;
                        
                    default:
                        // nothing
                }
                
                ok = true;
            }
        }
        
        if (!ok) {
            String error = "No fue posible procesar la solicitud.";
            
            if (SAuthJsonUtils.containsElement(responseJson, "", "message")) {
                JsonNode message = responseJson.path("message");
                error += "\n" + message.asText();
            }
            else if (SAuthJsonUtils.containsElement(responseJson, "", "detail")) {
                JsonNode detail = responseJson.path("detail");
                error += "\n" + detail.asText();
            }
            
            throw new Exception(error);
        }
        
        return ok;
    }
    
    /**
     * Obtener el estatus de un flujo de autorización.
     * @param session Sesión de usuario.
     * @param resourceType Tipo de recurso (SSwapConsts.RESOURCE_TYPE_...)
     * @param resourceId  ID del recurso.
     * @return 
     * @throws java.lang.Exception
     */
    public static AuthFlowStatus getAuthFlowStatus(final SGuiSession session, final int resourceType, final String resourceId) throws Exception {
        switch (resourceType) {
            case SSwapConsts.RESOURCE_TYPE_PUR_ORDER:
            case SSwapConsts.RESOURCE_TYPE_PUR_INVOICE:
            case SSwapConsts.RESOURCE_TYPE_PUR_PAYMENT:
                break;
                
            default:
                throw new UnsupportedOperationException(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n(Tipo de recurso: " + resourceType + ")");
        }
        
        AuthFlowStatus authFlowStatus = null;
        
        ObjectMapper mapper = new ObjectMapper();
        SyncSettings syncSettings = getSyncSettings(session, mapper, SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_AUTH_CONFIG, SSwapConsts.CFG_OBJ_AUTH_SRV);
        
        syncSettings.Url += URL_GET_RESOURCE_FLOW;
        
        String urlQuery = "id_external_system=" + SSwapConsts.SIIE_EXT_SYS_ID + "&"
                + "id_company=" + session.getConfigCompany().getCompanyId() + "&"
                + "id_resource_type=" + resourceType + "&"
                + "siie_resource_id=" + resourceId;
        
        String responseBody = SExportUtils.requestSwapService(urlQuery, syncSettings.Url, SHttpConsts.METHOD_GET, "", syncSettings.Token, syncSettings.ApiKey, SSwapConsts.TIME_30_SEC);
        JsonNode responseJson = new ObjectMapper().readTree(responseBody);
        
        if (responseJson.has("status")) {
            JsonNode status = responseJson.path("status");
            if (status.isInt() && status.asInt() == SHttpConsts.RSC_ERR_SERVER) {
                authFlowStatus = new AuthFlowStatus(status.asInt(), responseJson.path("error").asText());
            }
        }
        
        if (authFlowStatus == null) {
            if (SAuthJsonUtils.containsElement(responseJson, "", "status")) {
                authFlowStatus = new AuthFlowStatus(SHttpConsts.RSC_SUCC_OK, "");

                JsonNode status = responseJson.path("status");
                JsonNode flow = responseJson.path("flow");
                JsonNode currAction = responseJson.path("current_action");
                JsonNode currActionStatus = currAction.path("flow_status");
                JsonNode currActionActionedBy = currAction.path("actioned_by");
                JsonNode currActionActionedAllActors = currAction.path("all_actors");

                authFlowStatus.FlowId = flow.get("id").asInt();
                authFlowStatus.FlowStatusId = status.get("id").asInt();
                authFlowStatus.FlowStatusCode = status.get("code").asText();
                authFlowStatus.FlowStatusName = status.get("name").asText();
                authFlowStatus.FlowPriority = flow.get("priority").asInt();
                authFlowStatus.FlowNotes = flow.get("notes").asText();

                authFlowStatus.CurrActionId = currAction.get("id").asInt();
                authFlowStatus.CurrActionSequence = currAction.get("sequence").asInt();
                authFlowStatus.CurrActionStatusId = currActionStatus.get("id").asInt();
                authFlowStatus.CurrActionStatusCode = currActionStatus.get("code").asText();
                authFlowStatus.CurrActionStatusName = currActionStatus.get("name").asText();
                authFlowStatus.CurrActionNotes = currAction.get("notes").asText();

                if (!currActionActionedBy.isNull()) {
                    authFlowStatus.CurrActionActionedAt = currAction.get("actioned_at").asText(); // if currActionActionedBy is null, then "actioned_at" is also null
                    authFlowStatus.CurrActionActionedById = currActionActionedBy.get("external_id").asInt();
                    authFlowStatus.CurrActionActionedByName = currActionActionedBy.get("full_name").asText();
                }
                else {
                    if (currActionActionedAllActors.isArray()) {
                        for (JsonNode actor : currActionActionedAllActors) {
                            authFlowStatus.CurrActionAllActors += (authFlowStatus.CurrActionAllActors.isEmpty() ? "" : "; ") + actor.path("full_name").asText();
                        }
                    }
                }
            }
            else if (SAuthJsonUtils.containsElement(responseJson, "", "status_code")) {
                int code = responseJson.path("status_code").asInt();
                JsonNode errors = responseJson.path("errors");
                JsonNode messages = errors.path("siie_resource_id");
                String message = messages.get(0).asText();

                authFlowStatus = new AuthFlowStatus(code, message);
            }
        }
        
        return authFlowStatus;
    }
    
    /**
     * Ask for reject data.
     * @param session GUI session.
     * @return Reject data.
     */
    public static RejectData askForRejectData(final SGuiSession session) {
        int userId = session.getUser().getPkUserId();
        String user = "'" + session.getUser().getName() + "'";
        String notes = "";
        
        if (userId == SDataConstantsSys.USRX_USER_SUPER || userId == SDataConstantsSys.USRX_USER_ADMIN) {
            userId = 0;

            String id = JOptionPane.showInputDialog(session.getClient().getFrame().getRootPane(), "ID del usuario que solicita el rechazo:");

            if (id != null) {
                userId = SLibUtils.parseInt(id);
                user += " (ID = " + userId + ")";
            }
        }

        if (userId != 0) {
            notes = JOptionPane.showInputDialog(session.getClient().getFrame().getRootPane(), "Comentarios de rechazo:");
        }
        
        return notes == null || notes.isEmpty() ? null : new RejectData(userId, user, notes);
    }
    
    private static class SyncSettings {
        
        public String Url;
        public String Token;
        public String ApiKey;
        
        public SyncSettings(final String url, final String token, final String apiKey) {
            Url = url;
            Token = token;
            ApiKey = apiKey;
        }
    }
    
    public static class RejectData {
        
        public int UserId;
        public String User;
        public String Notes;
        
        public RejectData(final int userId, final String user, String notes) {
            UserId = userId;
            User = user;
            Notes = notes;
        }
    }
    
    public static class AuthFlowStatus {
        
        public int ResponseCode;
        public String ResponseMessage;
        
        public int FlowId;
        public int FlowStatusId;
        public String FlowStatusCode;
        public String FlowStatusName;
        public int FlowPriority;
        public String FlowNotes;
        
        public int CurrActionId;
        public int CurrActionSequence;
        public int CurrActionStatusId;
        public String CurrActionStatusCode;
        public String CurrActionStatusName;
        public String CurrActionNotes;
        public String CurrActionActionedAt;
        public int CurrActionActionedById;
        public String CurrActionActionedByName;
        public String CurrActionAllActors;
        
        public AuthFlowStatus(final int responseCode, final String responseMessage) {
            ResponseCode = responseCode;
            ResponseMessage = responseMessage;
        
            FlowId = 0;
            FlowStatusId = 0;
            FlowStatusCode = "";
            FlowStatusName = "";
            FlowPriority = 0;
            FlowNotes = "";

            CurrActionId = 0;
            CurrActionSequence = 0;
            CurrActionStatusId = 0;
            CurrActionStatusCode = "";
            CurrActionStatusName = "";
            CurrActionNotes = "";
            CurrActionActionedAt = "";
            CurrActionActionedById = 0;
            CurrActionActionedByName = "";
            CurrActionAllActors = "";
        }
        
        @Override
        public String toString() {
            String string = "";
            
            if (FlowId != 0) {
                string += "FLUJO DE AUTORIZACIÓN:\n"
                        + "- Estatus: " + FlowStatusName + "\n"
                        + "- Prioridad: " + (FlowPriority == SDbPayment.PRIORITY_URGENT ? SDbPayment.DESC_PRIORITY_URGENT : SDbPayment.DESC_PRIORITY_NORMAL) + "\n"
                        + "- Notas: \"" + FlowNotes + "\"\n"
                        + "ACCIÓN ACTUAL:\n"
                        + "- No. secuencia: " + CurrActionSequence + "\n"
                        + "- Estatus: " + CurrActionStatusName+ "\n";
                
                if (CurrActionActionedById != 0) {
                    string += "- Comentarios: \"" + CurrActionNotes + "\"\n"
                            + "- Ejecutada por: " + CurrActionActionedByName + "\n"
                            + "- Ejecutada el : " + CurrActionActionedAt + "";
                }
                else {
                    string += "- Responsable(s): " + (CurrActionAllActors.isEmpty() ? "?" : CurrActionAllActors) + "";
                }
                
                string += ".";
            }
            else {
                string += "RESPUESTA DE CONSULTA DEL FLUJO DE AUTORIZACIÓN:\n"
                        + "- Mensaje: " + ResponseMessage + "\n"
                        + "- Código: " + ResponseCode + "";
            }
            
            return string;
        }
    }
}
