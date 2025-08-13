/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

/**
 * Clase contenedora para exportar datos masivos de áreas funcionales en formato JSON.
 * Utilizada como estructura principal para enviar múltiples áreas funcionales y las instancias de SWAP Services.
 *
 * @author Sergio Flores
 */
public class SRequestFunctionalAreasBody {
    /**
     * Arreglo de identificadores de instancias.
     * Cada elemento representa una instancia relacionada con los datos de la exportación.
     */
    public String[] work_instance;
    
    /**
     * Arreglo de áreas funcionales a exportar.
     * Arreglo de objetos con la información completa de cada área funcional.
     */
    public SExportDataFunctionalArea[] functional_areas;
}
