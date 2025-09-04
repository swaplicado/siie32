/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

/**
 * Clase que representa la estructura de datos para exportar información de un elemento organizacional en formato JSON.
 * Utilizada para el intercambio de datos con sistemas externos.
 * 
 * @author Sergio Flores
 */
public class SExportDataAuthOrgElement implements SExportData {

    /**
     * Código del elemento organizacional.
     */
    public String code;
    
    /**
     * Nombre del elemento organizacional.
     */
    public String name;
    
    /**
     * Tipo del elemento organizacional.
     */
    public int org_element_type;
    
    /**
     * Indicador de borrado del elemento organizacional.
     */
    public boolean is_deleted;
    
    /**
     * ID del elemento organizacional en el sistema externo.
     */
    public int external_id;
}
