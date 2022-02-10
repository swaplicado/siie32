/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

import erp.mod.hrs.link.db.SAbsDelays;
import erp.mod.hrs.link.utils.SPrepayroll;

/**
 *
 * @author Edwin Carmona
 */
public class SCAPResponse {
    public static final int RESPONSE_OK = 200; 
    public static final int RESPONSE_ERROR = 500;
    public static final int RESPONSE_NOT_VOBO = 550;
    
    private int code;
    private String message;
    private SAbsDelays absData;
    private SPrepayroll prepayrollData;

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

    public SAbsDelays getAbsData() {
        return absData;
    }

    public void setAbsData(SAbsDelays absData) {
        this.absData = absData;
    }

    public SPrepayroll getPrepayrollData() {
        return prepayrollData;
    }

    public void setPrepayrollData(SPrepayroll prepayrollData) {
        this.prepayrollData = prepayrollData;
    }
    
    
}
