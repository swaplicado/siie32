/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

import java.util.ArrayList;

/**
 *
 * @author César Orozco
 */
public class SEarningResponse {
    
    public static final int RESPONSE_OK = 200;
    public static final int RESPONSE_ERROR = 500;
    
    private int code;
    private String message;
    private ArrayList<SEmployeeEar> empEar;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<SEmployeeEar> getEmpEar() {
        return empEar;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setEmpEar(ArrayList<SEmployeeEar> empEar) {
        this.empEar = empEar;
    }
    
}
