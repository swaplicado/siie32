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
public class SImportAccountingAccount {
    public int id;
    public String code;
    public String name;
    
    public SImportAccountingAccount() {
        id = 0;
        code = "";
        name = "";
    }
    
    public SImportAccountingAccount(final JsonNode docNode) {
        id = docNode.get("id").asInt();
        code = docNode.get("code").asText();
        name = docNode.get("name").asText();
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    public int getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
}
