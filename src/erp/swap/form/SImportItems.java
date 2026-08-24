/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.form;

import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 * @author Adrian Aviles
 */
public class SImportItems {
    public int id;
    public String name;
    public String code;
    public int erpId;
//    public boolean is_vehicle_required;
//    public boolean is_active;
    
    public SImportItems() {
        id = 0;
        name = "";
        code = "";
        erpId = 0;
//        is_vehicle_required = false;
//        is_active = false;
    }
    
    public SImportItems(final JsonNode docNode) {
        id = docNode.get("id").asInt();
        name = docNode.get("name").asText();
        code = docNode.get("code").asText();
        erpId = docNode.get("erp_id").asInt();
//        is_vehicle_required = docNode.get("is_vehicle_required").asBoolean();
//        is_active = docNode.get("is_active").asBoolean();
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    public int getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public int getErpId() { return erpId; }
}
