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
import erp.mod.cfg.db.SDbComImportLog;
import erp.mod.cfg.db.SDbComImportLogEntry;
import erp.mod.cfg.swap.SHttpConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.utils.SAuthJsonUtils;
import java.util.Date;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SServicesUtils {
    
    public static final String URL_FORCE_REJECT_RESOURCE = "/api/force-reject-resource";
    
    /**
     * Rechazar un recurso autorizado de manera forzada.
     * @param session Sesión de usuario.
     * @param data Datos del recurso autorizado a rechazar.
     * @throws Exception Si ocurre un error en la petición.
     */
    public static void requestRejectResource(final SGuiSession session, final SDataRejectResource data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode config = mapper.readTree(SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG));
        
        String syncUrl = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_SRV, SSwapConsts.CFG_ATT_URL);
        String syncToken = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_SRV, SSwapConsts.CFG_ATT_TOKEN); // recuperar token genérico del end point
        String syncApiKey = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_SRV, SSwapConsts.CFG_ATT_API_KEY); // recuperar API key genérica del end point
        
        syncUrl += URL_FORCE_REJECT_RESOURCE;
        
        String requestBody = mapper.writeValueAsString(data);
        Date requestDatetime = new Date();
        String responseBody = SExportUtils.requestSwapService("", syncUrl, SHttpConsts.METHOD_PATCH, requestBody, syncToken, syncApiKey, SSwapConsts.TIME_180_SEC);
        Date responseDatetime = new Date();

        // Procesar la respuesta:
        
        boolean ok = false;
        JsonNode responseJson = mapper.readTree(responseBody);
        
        if (SAuthJsonUtils.containsElement(responseJson, "", "status_code")) {
            JsonNode status = responseJson.path("status_code");
            String code = status.asText();
            
            if (code.equals("" + SHttpConsts.RSC_SUCC_OK)) {
                ok = true;
                
                SDbComImportLogEntry entry = new SDbComImportLogEntry();
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
}
