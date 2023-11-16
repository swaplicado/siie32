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
public class SEmployeeEar {
    private int id_emp;
    private int id_company;
    private boolean have_bonus;
    private ArrayList<SEarning> earnings;

    public int getId_emp() {
        return id_emp;
    }

    public int getId_company() {
        return id_company;
    }

    public boolean getHave_bonus() {
        return have_bonus;
    }

    public ArrayList<SEarning> getEarnings() {
        return earnings;
    }

    public void setId_emp(int id_emp) {
        this.id_emp = id_emp;
    }

    public void setId_company(int id_company) {
        this.id_company = id_company;
    }

    public void setHave_bonus(boolean have_bonus) {
        this.have_bonus = have_bonus;
    }
    
    public void setEarnings(ArrayList<SEarning> earnings) {
        this.earnings = earnings;
    }
    
    
}
