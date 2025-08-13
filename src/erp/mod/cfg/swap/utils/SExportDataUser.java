/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Clase que representa la estructura de datos para exportar información de un usuario en formato JSON.
 * Utilizada para el intercambio de datos con sistemas externos.
 * 
 * Contiene los campos principales de un usuario, sus atributos adicionales,
 * información de asociado de negocios (partner), grupos a los que pertenece,
 * empresas a las que tiene acceso y áreaa funcionales asignadas.
 * 
 * Incluye anotaciones de Jackson para controlar la serialización/deserialización JSON.
 * 
 * @author Edwin Carmona, Sergio Flores
 */
public class SExportDataUser implements SExportData {
    /**
     * Nombre de usuario del sistema.
     */
    public String username;
    
    /**
     * Correo electrónico del usuario.
     */
    public String email;
    
    /**
     * Contraseña del usuario (debe manejarse con cuidado en producción).
     */
    public String password;
    
    /**
     * Indicador de si el usuario está activo en el sistema.
     */
    public boolean is_active;
    
    /**
     * Nombre(s) del usuario.
     */
    public String first_name;
    
    /**
     * Apellido(s) del usuario.
     */
    public String last_name;
    
    /**
     * Atributos adicionales del usuario.
     */
    public Attributes attributes;
    
    /**
     * Información del socio de negocios del usuario.
     * Se incluye en el JSON solo si no es nulo.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Partner partner;
    
    /**
     * Arreglo de ID de grupos a los que pertenece el usuario.
     */
    public int[] groups;
    
    /**
     * Arreglo de ID de empresas a las que tiene acceso el usuario interno.
     * Se incluye en el JSON solo si no es nulo.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public int[] companies;
    
    /**
     * Arreglo de áreas funcionales que tiene asignadas el usuario interno.
     * Se incluye en el JSON solo si no es nulo.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public FunctionalArea[] functional_areas;
    
    /**
     * Clase interna para los atributos adicionales del usuario.
     */
    public static class Attributes {
        /**
         * Nombre completo del usuario.
         */
        public String full_name;
        
        /**
         * Tipo de usuario (1 = internal; 2 = external).
         */
        public int user_type;
        
        /**
         * Otros emails del usuario.
         * Se incluye en el JSON solo si no es nulo.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public String other_emails; // separados entre sí por punto y coma ';'
        
        /**
         * Indicador de si el usuario está borrado.
         */
        public boolean is_deleted;
        
        /**
         * ID del usuario en el sistema externo.
         */
        public int external_id;
    }
    
    /**
     * Clase interna para la información del socio de negocios del usuario.
     */
    public static class Partner {
        /**
         * Indicador de si el socio de negocios es Proveedor.
         */
        public boolean is_vendor;
        
        /**
         * ID fiscal del socio de negocios en el país local (RFC, NIT, etc.).
         */
        public String fiscal_id;
        
        /**
         * ID fiscal enl socio de negocios extranjero.
         */
        public String foreign_fiscal_id;
        
        /**
         * Nombre completo del socio de negocios.
         */
        public String full_name;
        
        /**
         * Nombre comercial del socio de negocios.
         */
        public String trade_name;
        
        /**
         * Tipo de entidad  del socio de negocios: "PERSON" (persona física) u "ORG" (persona moral).
         */
        public String entity_type;
        
        /**
         * Código (ISO 3166) de país del socio de negocios.
         */
        public String country_code;
        
        /**
         * Código (SAT) de régimen fiscal del socio de negocios.
         */
        public String tax_regime_code;
        
        /**
         * Correo electrónico del socio de negocios.
         */
        public String partner_mail;
        
        /**
         * Indicador de si el socio de negocios está borrado.
         */
        public boolean is_deleted;
        
        /**
         * ID del socio de negocios en el sistema externo.
         */
        public int external_id;
    }

    /**
     * Clase interna que representa atributos adicionales del usuario.
     */
    public static class FunctionalArea {
        
        /**
         * ID de la empresa del área funcional en el sistema externo.
         */
        public int external_company_id;
        
        /**
         * ID del área funcional en el sistema externo.
         */
        public int external_id;
    }
}
