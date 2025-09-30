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
public class SExportDataPayment implements SExportData {
    
    public int company;
    
    public int payment_id;
    
    public int functional_area;
    
    public int benef;
    
    public String series;
    
    public String number;
    
    public String app_date;
    
    public String req_date;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String sched_date_n;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String exec_date_n;
    
    public String currency;
    
    public String amount;
    
    public String exchange_rate_app;
    
    public String amount_loc_app;
    
    public String exchange_rate_exec;
    
    public String amount_loc_exec;
    
    public String payment_way;
    
    public int priority;
    
    public String notes;
    
    public int is_receipt_payment_req;
    
    public int payment_status;
    
    public int authz_authorization_id;
    
    // cuenta pagadora
    public String paying_bank;
    
    public String paying_bank_fiscal_id;
    
    public String paying_account;
    
    // cuenta beneficiaria
    public String benef_bank;
    
    public String benef_bank_fiscal_id;
    
    public String benef_account;
    
    public Integer sched_user;
    
    public Integer exec_user;
    
    public String sched_at;
    
    public String exec_at;
    
    public int is_deleted;
    
    //public int created_by;
    
    //public int updated_by;
    
    //public int deleted_by;
    
    //public String created_at;
    
    //public String updated_at;
    
    //public String deleted_at;
    
    public int user_id;
}
