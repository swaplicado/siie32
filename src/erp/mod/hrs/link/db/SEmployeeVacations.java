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
public class SEmployeeVacations {
    private int employee_id = 0;
    private int employee_number = 0;
    private ArrayList<SDataVacations> rows;
        
    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }
    
    public int getEmployee_number() {
        return employee_number;
    }

    public void setEmployee_number(int employee_number) {
        this.employee_number = employee_number;
    }
    
    public ArrayList<SDataVacations> getRows() {
        return rows;
    }

    public void setRows(ArrayList<SDataVacations> rows) {
        this.rows = rows;
    }
}
