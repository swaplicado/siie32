/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

/**
 * @author Edwin Carmona
 * 
 * This class is a specific type of exception that is 
 * thrown when there is some error with stock validations.
 */
public class SStockException extends Exception {
    
    public SStockException(String message){
        super(message);
    }
    
}
