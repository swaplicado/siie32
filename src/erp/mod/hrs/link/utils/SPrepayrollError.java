/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.utils;

/**
 *
 * @author Cesar Orozco
 */
public class SPrepayrollError {
    private String employee_id;
    private String employee_name;
    private String error;
    
    public String getEmployee_id() {
        return employee_id;
    }
    
    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }
    
    public String getEmployee_name() {
        return employee_name;
    }
    
    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
}
