/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap;

/**
 *
 * @author Sergio Flores
 */
public abstract class SHttpConsts {
    
    // HTTP Response Status Codes:
    
    public static final int RSC_SUCC_OK = 200;
    public static final int RSC_SUCC_CREATED = 201;
    public static final int RSC_SUCC_ACCEPTED = 202;
    public static final int RSC_SUCC_NO_AUTH_INFO = 203;
    public static final int RSC_SUCC_NO_CONTENT = 204;
    public static final int RSC_ERR_BAD_REQUEST = 400;
    
    // HTTP Methods:
    
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    
}
