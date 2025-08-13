/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

/**
 * Clase contenedora para exportar datos masivos de usuarios en formato JSON.
 * Utilizada como estructura principal para enviar múltiples usuarios y las instancias de SWAP Services.
 *
 * @author Edwin Carmona, Sergio Flores
 */
public class SRequestUsersBody {
    /**
     * Arreglo de identificadores de instancias.
     * Cada elemento representa una instancia relacionada con los datos de la exportación.
     */
    public String[] work_instance;
    
    /**
     * Arreglo de usuarios a exportar.
     * Arreglo de objetos con la información completa de cada usuario.
     */
    public SExportDataUser[] users;
}
