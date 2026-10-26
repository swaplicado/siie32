/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Isabel Servín, Edwin Carmona
 */
public class SExportDataPaymentUpdate extends SExportDataPaymentBase implements SExportData {
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer deleted_by;
}
