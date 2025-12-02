/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import erp.mod.trn.api.data.SWebDps;

/**
 * Clase que representa la estructura de datos para exportar información de referencias en formato JSON.
 * Utilizada para el intercambio de datos con sistemas externos.
 * 
 * Incluye anotaciones de Jackson para controlar la serialización/deserialización JSON.
 *
 * @author Edwin Carmona
 */
public class SExportDataDps implements SExportData {

    public SExportDataDps() {
        this.authz_authorization = 1;
    }
    
    /**
     * ID de la empresa del documento en el sistema externo.
     */
    public int company;

    /**
     * ID YEAR del documento en el sistema externo.
     */
    public int id_year;

    /**
     * ID DOC del documento en el sistema externo.
     */
    public int id_doc;
    
    /**
     * ID de la categoría de la transacción del documento (1 = Compras; 2 = Ventas).
     */
    public int transaction_class;
    
    /**
     * ID del socio de negocios en el sistema externo.
     */
    public int partner;
    
    /**
     * ID del socio de negocios en el sistema externo.
     */
    public String series;
    
    /**
     * ID del socio de negocios en el sistema externo.
     */
    public int number;
    
    /**
     * Código de moneda (ISO 4217) del monto del documento.
     */
    public String currency;
    
    /**
     * Monto del documento.
     */
    public double amount;

    /**
     * Tipo de cambio del documento.
     */
    public double exchange_rate;

    /**
     * ID del área funcional del documento en el sistema externo.
     */
    public int functional_area;
    
    /**
     * Uso de CFDI
     */
    public String fiscal_use;
    
    /**
     * Método de pago
     */
    public String payment_method;
    
    /**
     * Conceptos de gasto de las partidas del documento
     */
    public String concepts;
    
    /**
     * Centros de costo de las partidas del documento
     */
    public String cost_profit_center;
    
    /**
     * Notas del documento a nivel encabezado.
     */
    public String notes;

    /**
     * Fecha del documento.
     */
    public String date;
    
    /**
     * Indicador de borrada del documento.
     */
    public boolean is_deleted;
    
    /**
     * Prioridad de la autorización
     */
    public int priority;
    
    /**
     * Estatus de autorización para el sistema externo
     */
    public int authz_authorization;
    
    /**
     * Notas de quien inició el proceso de autorización
     */
    public String authz_notes;
    
    /**
     * JSON de la OC para el portal de compras.
     */
    public SWebDps document_json;
}
