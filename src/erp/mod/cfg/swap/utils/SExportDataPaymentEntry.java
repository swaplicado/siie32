/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Isabel Servín, Sergio Flores
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
    
    public String document_bal_prev_app;
    
    public String document_bal_unpd_app;
    
    public String document_bal_prev_exec;
    
    public String document_bal_unpd_exec;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer document_id_n;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String document_uuid;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String document_folio;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String document_date;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String document_currency;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String document_amount;
}
