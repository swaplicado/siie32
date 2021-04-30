/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mmfg.data;

/**
 *
 * @author Edwin Carmona
 */
public class SMfgInputData {
    
    protected String msItemKey;
    protected double mnQuantity;
    
    public SMfgInputData() {}
    
    public SMfgInputData(final String sItemKey, final double dQuantity) {
        this.msItemKey = sItemKey;
        this.mnQuantity = dQuantity;
    }
    
    public void setMsItemKey(String sItemKey) {
        this.msItemKey = sItemKey;
    }
    
    public void setMnQuantity(double mnQuantity) {
        this.mnQuantity = mnQuantity;
    }

    public String getMsItemKey() {
        return msItemKey;
    }

    public double getMnQuantity() {
        return mnQuantity;
    }

}
