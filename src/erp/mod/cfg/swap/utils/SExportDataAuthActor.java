/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Clase que representa la estructura de datos para exportar información de un actor del Sistema de Autorizaciones en formato JSON.
 * Utilizada para el intercambio de datos con sistemas externos.
 * 
 * @author Sergio Flores
 */
public class SExportDataAuthActor implements SExportData {
    
    public static final int ACTOR_TYPE_SYSTEM = 1;
    public static final int ACTOR_TYPE_USER = 2;
    public static final int ACTOR_TYPE_THIRD_PARTY = 3;
    
    public static final String ACTOR_CODE_PREFIX_USER = "U";
    public static final String ACTOR_CODE_PREFIX_SUPPLIER = "S";
    public static final String ACTOR_CODE_PREFIX_CUSTOMER = "C";
    
    public static final String ENTITY_TYPE_NA = "N";
    public static final String ENTITY_TYPE_PER = "P";
    public static final String ENTITY_TYPE_ORG = "O";
    
    public static final int ORG_ELEMENT_TYPE_JOB_TITLE = 1;
    public static final int ORG_ELEMENT_TYPE_DEPARTMENT = 2;
    public static final int ORG_ELEMENT_TYPE_FUNCTIONAL_AREA = 3;
    
    /**
     * ID del actor en el sistema externo.
     * Puede ser el ID del usuario o el ID del socio de negocios (proveedor o cliente).
     */
    public int external_id;
    
    /**
     * Tipo de actor.
     */
    public int actor_type_id;
    
    /**
     * Indicador de ser proveedor.
     */
    public boolean is_vendor;
    
    /**
     * Indicador de ser cliente.
     */
    public boolean is_customer;
    
    /**
     * Tipo de entidad del actor.
     */
    public String entity_type;
    
    /**
     * Código del actor.
     */
    public String code;
    
    /**
     * Nombre(s) del actor.
     */
    public String first_name;
    
    /**
     * Apellido(s) del actor.
     */
    public String last_name;
    
    /**
     * Nombre completo del actor.
     */
    public String full_name;
    
    /**
     * Correos electrónicos del actor. Si son varios, separados entre sí por punto y coma.
     */
    public String email;
    
    /**
     * Teléfono del actor.
     */
    public String phone;

    /**
     * Indicador de borrado del actor.
     */
    public boolean is_deleted;
    
    /**
     * Arreglo de ID de empresas a las que tiene acceso el actor. (2025-09-02, Sergio Flores: ¿es en verdad necesario este atributo?)
     * Se incluye en el JSON solo si no es nulo.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public int[] companies;
    
    /**
     * Arreglo de elementos organizacionales que tiene asignadas el actor.
     * Se incluye en el JSON solo si no es nulo.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public OrgElement[] org_elements;
    
    /**
     * Clase interna para los elementos organizacionales.
     */
    public static class OrgElement {
        
        /**
         * Tipo de elemento.
         */
        public int element_type_id;
        
        /**
         * ID del elemento organizacional en el sistema externo.
         */
        public int external_id;
    }
}
