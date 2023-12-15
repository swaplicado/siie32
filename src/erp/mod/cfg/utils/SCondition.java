/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.utils;

/**
 *
 * @author Edwin Carmona
 */
public class SCondition {

    private String keyName;
    private String operator;
    private String strValue;

 // Getter Methods 
    public String getKeyName() {
        return keyName;
    }

    public String getOperator() {
        return operator;
    }

    public String getStrValue() {
        return strValue;
    }

 // Setter Methods 
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }
}
