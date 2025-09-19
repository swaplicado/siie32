/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Isabel Servín
 */
public class SExportDataPaymentEntry implements SExportData {
    
    public String entry_type;
    
    public String amount;
    
    public String amount_loc_app;
    
    public String entry_currency;

    public String conv_rate_app;
    
    public String entry_amount_app;
    
    public String amount_loc_exec;
    
    public String conv_rate_exec;
    
    public String entry_amount_exec;
    
    public int installment;
    
    public double document_bal_prev_app;
    
    public double document_bal_unpd_app;
    
    public double document_bal_prev_exec;
    
    public double document_bal_unpd_exec;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public double document_id_n;
    
    public String document_uuid;
    
    public String document_folio;
    
    public String document_date;
    
    public int document_currency_id;
    
    public double document_amount;
}
