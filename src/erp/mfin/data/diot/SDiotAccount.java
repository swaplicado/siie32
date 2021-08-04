/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.data.diot;

/**
 *
 * @author Sergio Flores
 */
public class SDiotAccount {
    
    public String AccountCode;
    public boolean IsConfigParamAccount;
    
    /**
     * Create an account for DIOT processing.
     * Contains account number and a flag for accounts set up in configuration of company.
     * @param accountCode
     * @param isConfigParamAccount 
     */
    public SDiotAccount(String accountCode, boolean isConfigParamAccount) {
        AccountCode = accountCode;
        IsConfigParamAccount = isConfigParamAccount;
    }
}
