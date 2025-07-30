/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swapms.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Clase que representa la estructura de datos para exportar información de referencias en formato JSON.
 * Utilizada para el intercambio de datos con sistemas externos.
 * 
 * Incluye anotaciones de Jackson para controlar la serialización/deserialización JSON.
 *
 * @author Sergio Flores
 */
public class SExportReference implements SExportData {

    /**
     * ID de la empresa
     */
    public int company;

    /**
     * ID de la categoría de la transacción
     */
    public int transaction_class;

    /**
     * ID del tipo de referencia
     */
    public int document_ref_type;

    /**
     * ID del socio de negocios
     */
    public int partner;

    /**
     * Referencia
     */
    public String reference;

    /**
     * Fecha de la referencia
     */
    public String date;

    /**
     * Moneda de la referencia
     */
    public int currency;

    /**
     * Monto de la referencia
     */
    public double amount;

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
}
