/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

/**
 *
 * @author swaplicado
 */
public class SCancelResponse {
    public static final int RESPONSE_OK_CAN = 200;
    public static final int RESPONSE_ERROR = 500;
    public static final int RESPONSE_CONSUME = 550;
    
    private int code;
    private String message;
    
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
    
}
