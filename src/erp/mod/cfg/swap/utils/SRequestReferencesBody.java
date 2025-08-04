/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

/**
 * Clase contenedora para exportar datos masivos en formato JSON.
 * Utilizada como estructura principal para enviar múltiples usuarios junto con información de instancias de trabajo.
 *
 * @author Sergio Flores
 */
public class SRequestReferencesBody {
    /**
     * Arreglo de identificadores de instancias de trabajo.
     * Cada elemento representa una instancia de trabajo relacionada con las referencias incluidas en el export.
     */
    public String[] work_instance;
    
    /**
     * Arreglo de referencias a exportar.
     * Contiene objetos de tipo SReference con la información completa de cada referencia.
     */
    public SExportDataUser[] users;
}
