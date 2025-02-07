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
public class SIncidentResponse {
    
    public static final int RESPONSE_OK_AVA = 200;
    public static final int RESPONSE_OK_INS = 300;
    public static final int RESPONSE_ERROR = 500;
    public static final int RESPONSE_OTHER_INC = 550;
    
    private int code;
    private String message;
    private ArrayList<SIncident> incidents; 

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public ArrayList getIncidents(){
        return incidents;
    }
    
    public void setIncidents(ArrayList<SIncident> incidents){
        this.incidents = incidents;
    }
      
}
