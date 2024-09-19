/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

import java.util.ArrayList;

/**
 *
 * @author Cesar Orozco
 */
public class SBreachEmployee {
    private long id_emp;
    private ArrayList<SBreachInfo> breach;
    private ArrayList<SAdmRecInfo> admRec;

    public long getId_emp() {
        return id_emp;
    }

    public ArrayList<SBreachInfo> getBreach() {
        return breach;
    }

    public ArrayList<SAdmRecInfo> getAdmRec() {
        return admRec;
    }

    public void setId_emp(long id_emp) {
        this.id_emp = id_emp;
    }

    public void setBreach(ArrayList<SBreachInfo> breach) {
        this.breach = breach;
    }

    public void setAdmRec(ArrayList<SAdmRecInfo> admRec) {
        this.admRec = admRec;
    }
}
