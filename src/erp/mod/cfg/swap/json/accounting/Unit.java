/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.json.accounting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 *
 * @author Sergio Flores
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Unit {

    private String key;
    private String keyDesc;
    private Double kg;

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getKeyDesc() { return keyDesc; }
    public void setKeyDesc(String keyDesc) { this.keyDesc = keyDesc; }

    public Double getKg() { return kg; }

    @JsonSetter("kg")
    public void setKg(Object kg) {
        if (kg == null || "null".equals(kg)) {
            this.kg = null;
        }
        else {
            this.kg = Double.valueOf(kg.toString());
        }
    }
}
