/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

/**
 *
 * @author Edwin Carmona
 */
public class SPhoto {
    
    protected String photo;
    protected int idEmployee;
    protected int numEmployee;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public int getNumEmployee() {
        return numEmployee;
    }

    public void setNumEmployee(int numEmployee) {
        this.numEmployee = numEmployee;
    }
}
