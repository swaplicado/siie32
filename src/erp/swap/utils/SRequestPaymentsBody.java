/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.utils;

/**
 * Clase contenedora para exportar datos masivos de pagos en formato JSON.
 * Utilizada como estructura principal para enviar múltiples pagos y las instancias de SWAP Services.
 *
 * @author Isabel Servín, Sergio Flores
 */
public class SRequestPaymentsBody {
    
    /**
     * Arreglo de identificadores de instancias.
     * Cada elemento representa una instancia relacionada con los datos de la exportación.
     */
    public String[] work_instance;
    
    /**
     * Arreglo de pagos a exportar.
     * Arreglo de objetos con la información completa de cada pago.
     */
    public Payment[] payments;
    
    public static class Payment implements SExportData {
        
        public SExportDataPayment payment;
        
        public SExportDataPaymentEntry[] entries;
    
        public SExportDataFile[] files;
    }
}
