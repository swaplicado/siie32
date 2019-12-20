/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data.cfd;

import org.json.simple.parser.ParseException;

/**
 *
 * @author Sergio Flores
 */
public interface SAddenda {
    
    public String encodeJson();
    
    public void decodeJson(final String json) throws ParseException, Exception;
}
