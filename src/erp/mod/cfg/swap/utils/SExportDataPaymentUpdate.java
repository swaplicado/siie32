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
public class SExportDataPaymentUpdate implements SExportData {
    
    public int company;
    
    public int payment_id;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String req_date;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String sched_date_n;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String exec_date_n;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String exchange_rate_exec;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String amount_loc_exec;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String payment_way;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String notes;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer is_deleted;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer payment_status;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String paying_bank;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String paying_bank_fiscal_id;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String paying_account;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String benef_bank;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String benef_bank_fiscal_id;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String benef_account;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer sched_user;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer exec_user;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String sched_at;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String exec_at;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer authorized_by;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String authorized_at;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer deleted_by;
}
