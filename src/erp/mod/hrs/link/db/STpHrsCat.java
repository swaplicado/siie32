/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

/**
 *
 * @author Cesar Orozco
 */
public class STpHrsCat {
    private int idCl = 0;
    private int idTp = 0;
    private String name = "";

    public int getIdCl() {
        return idCl;
    }

    public int getIdTp() {
        return idTp;
    }

    public String getName() {
        return name;
    }

    public void setIdCl(int idCl) {
        this.idCl = idCl;
    }

    public void setIdTp(int idTp) {
        this.idTp = idTp;
    }

    public void setName(String name) {
        this.name = name;
    }
}
