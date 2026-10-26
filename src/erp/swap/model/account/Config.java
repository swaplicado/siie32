/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * JSON de configuración de contabilizaciión masiva o asistida de facturas de compras y fletes de materias primas.
 * @author Sergio Flores
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    
    private List<Group> groups;
    private List<Tax> taxes;

    public List<Group> getGroups() { return groups; }
    public void setGroups(List<Group> groups) { this.groups = groups; }
    
    public List<Tax> getTaxes() { return taxes; }
    public void setTaxes(List<Tax> taxes) { this.taxes = taxes; }
    
    public Group getGroup(final String docType) {
        Group matchingGroup = null;
        
        for (Group g : groups) {
            if (g.getDocType().equals(docType)) {
                matchingGroup = g;
                break;
            }
        }
        
        return matchingGroup;
    }
    
    public Tax getTax(final String type, final String tax, final String factor, final String rate) {
        Tax matchingTax = null;
        
        for (Tax t : taxes) {
            if (t.getType().equals(type) && t.getTax().equals(tax) && t.getFactor().equals(factor) && t.getRate().equals(rate)) {
                matchingTax = t;
                break;
            }
        }
        
        return matchingTax;
    }
}