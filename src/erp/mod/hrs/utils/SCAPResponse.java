/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

/**
 *
 * @author Edwin Carmona
 */
public class SCAPResponse {
    public static final int RESPONSE_OK = 200; 
    public static final int RESPONSE_ERROR = 500;
    public static final int RESPONSE_NOT_VOBO = 550;
    
    private int code;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
