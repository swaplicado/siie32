/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swapms.utils;

/**
 * Clase contenedora para exportar datos masivos en formato JSON.
 * Utilizada como estructura principal para enviar múltiples usuarios
 * junto con información de instancias de trabajo.
 *
 * @author Edwin Carmona
 */
public class SBodyExport {
    /**
     * Arreglo de identificadores de instancias de trabajo.
     * Cada elemento representa una instancia de trabajo relacionada
     * con los usuarios incluidos en el export.
     */
    public String[] work_instance;
    
    /**
     * Arreglo de usuarios a exportar.
     * Contiene objetos de tipo SUserExport con la información completa
     * de cada usuario.
     */
    public SUserExport[] users;
}