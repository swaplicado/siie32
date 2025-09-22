/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

/**
 *
 * @author Isabel Servín
 */
public class SRequestPaymentsBody implements SExportData {
    
    public Payments[] payments;
    
    public static class Payments {
        public SExportDataPayment payment;
        
        public SExportDataPaymentEntry[] entries;
    
        public SExportDataFile[] files;
    }
}
