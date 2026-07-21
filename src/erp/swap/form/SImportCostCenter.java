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
//    public int company;
    public String name;
    public String code;
//    public boolean is_vehicle;
//    public boolean is_active;
    
    public SImportCostCenter() {
        id = 0;
//        company = 0;
        name = "";
        code = "";
//        is_vehicle = false;
//        is_active = false;
    }
    
    public SImportCostCenter(final JsonNode docNode) {
        id = docNode.get("id").asInt();
//        company = docNode.get("company").asInt();
        name = docNode.get("name").asText();
        code = docNode.get("code").asText();
//        is_vehicle = docNode.get("is_vehicle").asBoolean();
//        is_active = docNode.get("is_active").asBoolean();
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    public int getId() { return id; }
//    public int getCompany() { return company; }
    public String getCode() { return code; }
    public String getName() { return name; }
}
