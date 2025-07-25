/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swapms.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Clase que representa la estructura de datos para exportar información de usuario en formato JSON.
 * Utilizada para el intercambio de datos con sistemas externos.
 * 
 * Contiene los campos principales de un usuario, sus atributos adicionales,
 * información de socio/compañero (partner) y grupos a los que pertenece.
 * 
 * Incluye anotaciones de Jackson para controlar la serialización/deserialización JSON.
 * 
 * @author Edwin Carmona
 */
public class SUserExport {
    /**
     * Nombre de usuario del sistema
     */
    public String username;
    
    /**
     * Correo electrónico del usuario
     */
    public String email;
    
    /**
     * Contraseña del usuario (debería manejarse con cuidado en producción)
     */
    public String password;
    
    /**
     * Indicador de si el usuario está activo (1) o inactivo (0)
     */
    public int is_active;
    
    /**
     * Primer nombre del usuario
     */
    public String first_name;
    
    /**
     * Apellido del usuario
     */
    public String last_name;
    
    /**
     * Atributos adicionales del usuario
     */
    public Attributes attributes;

    /**
     * Información del socio/compañero asociado al usuario.
     * Se incluye en el JSON solo si no es nulo.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Partner partner;
    
    /**
     * Arreglo de IDs de grupos a los que pertenece el usuario
     */
    public int[] groups;

    /**
     * Código de estado para respuestas (solo se incluye si no es nulo)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public int status_code;
    
    /**
     * Mensaje descriptivo para respuestas (solo se incluye si no es nulo)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String message;
    
    /**
     * Campo de registro (ignorado en la serialización JSON)
     */
    @JsonIgnore
    public String register;

    /**
     * Clase interna que representa atributos adicionales del usuario
     */
    public static class Attributes {
        /**
         * Nombre completo del usuario
         */
        public String full_name;
        
        /**
         * Tipo de usuario (valor numérico)
         */
        public int user_type;
        
        /**
         * ID externo del usuario (para integración con otros sistemas)
         */
        public int external_id;
    }

    /**
     * Clase interna que representa información de socio/compañero
     */
    public static class Partner {
        /**
         * Indicador si es proveedor
         */
        public int b_sup;
        
        /**
         * ID fiscal (RFC, NIT, etc.)
         */
        public String fiscal_id;
        
        /**
         * Nombre completo del socio
         */
        public String full_name;
        
        /**
         * Tipo de entidad (persona física/moral, etc.)
         */
        public String entity_type;
        
        /**
         * País del socio
         */
        public String country;
        
        /**
         * Método de comunicación preferido
         */
        public String bp_comm;
        
        /**
         * Régimen fiscal
         */
        public String tax_regime;
        
        /**
         * ID externo del socio (para integración con otros sistemas)
         */
        public int external_id;
        
        /**
         * Correo electrónico del socio
         */
        public String partner_mail;
    }
}
