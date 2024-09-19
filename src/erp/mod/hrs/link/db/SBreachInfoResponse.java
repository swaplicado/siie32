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
public class SBreachInfoResponse {
    public static final int RESPONSE_OK = 200;
    public static final int RESPONSE_ERROR = 500;
    
    private int code;
    private String message;
    private ArrayList<SBreachEmployee> breach;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<SBreachEmployee> getBreach() {
        return breach;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBreach(ArrayList<SBreachEmployee> breach) {
        this.breach = breach;
    }

}
