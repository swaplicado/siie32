/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

/**
 * Clase contenedora para actualizar datos masivos de pagos en formato JSON.
 * Utilizada como estructura principal para enviar múltiples pagos y las instancias de SWAP Services.
 *
 * @author Sergio Flores
 */
public class SRequestPaymentUpdatesBody {
    
    /**
     * Arreglo de identificadores de instancias.
     * Cada elemento representa una instancia relacionada con los datos de la exportación.
     */
    public String[] work_instance;
    
    /**
     * Arreglo de pagos a actualizar.
     * Arreglo de objetos con la información nueva de cada pago.
     */
    public SExportDataPaymentUpdate[] payments;
}
