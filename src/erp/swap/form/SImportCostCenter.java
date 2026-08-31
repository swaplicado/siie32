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
public class SImportCostCenter {
    public int id;
    public String name;
    public String code;
    
    public SImportCostCenter() {
        id = 0;
        name = "";
        code = "";
    }
    
    public SImportCostCenter(final JsonNode docNode) {
        id = docNode.get("id").asInt();
        name = docNode.get("name").asText();
        code = docNode.get("code").asText();
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    public int getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
}
