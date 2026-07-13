/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.cfg.utils.SAuthJsonUtils;
import erp.swap.SHttpConsts;
import erp.swap.SSwapConsts;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Adrian Aviles
 */
public class SAmeUtils {
    public static String loginToAmeOperationControl (SGuiSession session) throws Exception {
        String token = "";
        String username = "cesar.orozco";
        String password = "123456";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode config = mapper.readTree(SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_AME_CONFIG));
        String baseUrl = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AME_SRV, SSwapConsts.CFG_ATT_URL);
        String url = baseUrl + SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AME_SRV_LOGIN, SSwapConsts.CFG_ATT_URL);
        String jsonBody = "{"
                + "\"username\":\"" + username + "\", "
                + "\"password\":\"" + password 
                + "\"}";

        String responseBody = SExportUtils.requestSwapService("", url, SHttpConsts.METHOD_POST, jsonBody, "", "", SSwapConsts.TIME_30_SEC);

        JsonNode responseJson = mapper.readTree(responseBody);
        
        if (!responseJson.has("error")) {
            token = responseJson.get("access").toString();
        }
        
        return token;
    }
    
}
