/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

/**
 *
 * @author swaplicado
 */
public class SIncident {
    
    String name = "";
    String ini = "";
    String fin = "";
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getIni() {
        return ini;
    }

    public void setIni(String ini) {
        this.ini = ini;
    }
    
    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }
    
}
