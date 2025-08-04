/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

/**
 * Clase que representa la estructura de datos para exportar información de referencias en formato JSON.
 * Utilizada para el intercambio de datos con sistemas externos.
 * 
 * Incluye anotaciones de Jackson para controlar la serialización/deserialización JSON.
 *
 * @author Sergio Flores
 */
public class SExportDataReference implements SExportData {

    /**
     * ID de la empresa de la referencia en el sistema externo.
     */
    public int external_company_id;

    /**
     * ID de la categoría de la transacción de la referencia (1 = Compras; 2 = Ventas).
     */
    public int transaction_class_id;

    /**
     * ID del tipo de referencia (22 = Pedido OC).
     */
    public int document_ref_type_id;

    /**
     * ID del socio de negocios en el sistema externo.
     */
    public int external_partner_id;

    /**
     * Referencia.
     */
    public String reference;

    /**
     * Fecha de la referencia.
     */
    public String date;

    /**
     * Código de moneda (ISO 4217) del monto de la referencia.
     */
    public int currency_code;

    /**
     * Monto de la referencia.
     */
    public double amount;
    
    /**
     * Indicador de si la referencia está borrada.
     */
    public boolean is_deleted;
}
