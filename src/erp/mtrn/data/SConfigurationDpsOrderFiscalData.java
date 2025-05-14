/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Isabel Servín
 */
public class SConfigurationDpsOrderFiscalData {
    Integer applyFiscalData;
    @JsonProperty("UsoCFDI")
    String usoCFDI;
    @JsonProperty("MetodoPago")
    String metodoPago;
    
    public Integer getapplyFiscalData() {
        return applyFiscalData;
    }
    
    public String getUsoCFDI() {
        return usoCFDI;
    }
    
    public String getMetodoPago() {
        return metodoPago;
    }
    
    public void setapplyFiscalData(Integer applyFiscalData) {
        this.applyFiscalData = applyFiscalData;
    }
    
    public void setUsoCFDI(String usoCFDI) {
        this.usoCFDI = usoCFDI;
    }
    
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
