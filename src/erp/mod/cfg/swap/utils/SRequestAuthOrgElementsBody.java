/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

/**
 * Clase contenedora para exportar datos masivos de elementos organizacionales en formato JSON.
 * Utilizada como estructura principal para enviar múltiples elementos organizacionales.
 *
 * @author Sergio Flores
 */
public class SRequestAuthOrgElementsBody {
    
    /**
     * ID del sistema externo.
     */
    public int id_external_system;
    
    /**
     * Arreglo de elementos organizacionales a exportar.
     */
    public SExportDataAuthOrgElement[] elements;
}
