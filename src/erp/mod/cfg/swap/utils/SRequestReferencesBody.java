/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

/**
 * Clase contenedora para exportar datos masivos de referencias de transacciones en formato JSON.
 * Utilizada como estructura principal para enviar múltiples referencias de transacciones y las instancias de SWAP Services.
 *
 * @author Sergio Flores
 */
public class SRequestReferencesBody {
    /**
     * Arreglo de identificadores de instancias.
     * Cada elemento representa una instancia relacionada con los datos de la exportación.
     */
    public String[] work_instance;
    
    /**
     * Arreglo de referencias de transacciones a exportar.
     * Arreglo de objetos con la información completa de cada referencia de transacciones.
     */
    public SExportDataReference[] references;
}
