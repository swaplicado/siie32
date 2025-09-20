/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

/**
 *
 * @author Edwin Carmona
 */
public class SResourceStatusResponse {

    public SResourceStatusResponse() {
        status_code = 0;
        message = "";
        data = "";
        error = "";
    }

    public int status_code;
    public String message;
    public String data;
    public String error;
}
