/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

import java.util.ArrayList;

/**
 *
 * @author swaplicado
 */
public class SPlanVacations {
    public static final int BIWEEK = 2; 
    public static final int WEEK = 1;
    public static final int BOTH = 0;
    
    String name = "";
    int way_pay = 0;
    private ArrayList<SPlanVacationsAux> rows;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWay_pay() {
        return way_pay;
    }

    public void setWay_pay(int way_pay) {
        this.way_pay = way_pay;
    }
    
    public ArrayList<SPlanVacationsAux> getPlanVacationsAux() {
        return rows;
    }

    public void setPlanVacationsAux(ArrayList<SPlanVacationsAux> rows) {
        this.rows = rows;
    }
}
