/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.human;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import sa.lib.gui.SGuiSession;

/**
 *
 * @author César Orozco
 */
public class SHumanConfig {

//    public static final String BASE_URL = "https://api-prod.humand.co/public/api/v1";
//    public static final String USERS = "/users";
//    
//    public static final String API_KEY = "Basic ODU4OTI1NDpEV3kyZ25UX2NaN0FRRWhkQmhSOGRxTmpJZkVodG9kNw==";
    
    private boolean mbLinkUp;
    private String msBaseUrl;
    private String msApiKey;
    
    private Set<Integer> moSkipDepartments;
    private Set<Integer> moSkipEmployees;

    public SHumanConfig(final SGuiSession session) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        String json = SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_HUMAND_CONFIG);

        if (json == null || json.isEmpty()) {
            throw new Exception("No existe configuración de Humand.");
        }

        JsonNode configNode = mapper.readTree(json);

        mbLinkUp = configNode.get("link-up").asInt() == 1;
        msBaseUrl = configNode.get("base-url").asText();
        msApiKey = configNode.get("api-key").asText();
        
        moSkipDepartments = new HashSet<>();

        JsonNode skipDepsNode = configNode.get("skip-deps");

        if (skipDepsNode != null && skipDepsNode.isArray()) {
            Iterator<JsonNode> iterator = skipDepsNode.elements();

            while (iterator.hasNext()) {
                moSkipDepartments.add(iterator.next().asInt());
            }
        }

        moSkipEmployees = new HashSet<>();

        JsonNode skipEmpsNode = configNode.get("skip-emps");

        if (skipEmpsNode != null && skipEmpsNode.isArray()) {
            Iterator<JsonNode> iterator = skipEmpsNode.elements();

            while (iterator.hasNext()) {
                moSkipEmployees.add(iterator.next().asInt());
            }
        }

    }

    public boolean isLinkUp() {
        return mbLinkUp;
    }

    public String getBaseUrl() {
        return msBaseUrl;
    }

    public String getApiKey() {
        return msApiKey;
    }
    
    public Set<Integer> getSkipDepartments() {
        return moSkipDepartments;
    }

    public Set<Integer> getSkipEmployees() {
        return moSkipEmployees;
    }
}
