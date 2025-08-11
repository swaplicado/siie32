/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

/**
 * Clase que representa la estructura de datos para exportar información de un área funcional en formato JSON.
 * Utilizada para el intercambio de datos con sistemas externos.
 * 
 * @author Sergio Flores
 */
public class SExportDataFunctionalArea implements SExportData {

    /**
     * Código del área funcional.
     */
    public String code;
    
    /**
     * Nombre del área funcional.
     */
    public String name;
    
    /**
     * Indicador de si el área funcional está borrada.
     */
    public boolean is_deleted;
    
    /**
     * ID de la empresa del área funcional en el sistema externo.
     */
    public int external_company_id;
    
    /**
     * ID del área funcional en el sistema externo.
     */
    public int external_id;
}
