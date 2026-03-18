/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.json.accounting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * JSON de configuración de contabilizaciión masiva o asistida de facturas de compras y fletes de materias primas.
 * @author Sergio Flores
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    
    private List<Group> groups;

    public List<Group> getGroups() { return groups; }
    public void setGroups(List<Group> groups) { this.groups = groups; }
}