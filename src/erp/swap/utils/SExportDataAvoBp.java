/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.utils;

/**
 * Clase que representa la estructura de datos para exportar información de asociados de negocio 
 * (Proveedores y Empleados) en formato JSON hacia Avocado.
 * 
 * Utilizada para la carga masiva y sincronización con microservicios externos.
 * 
 * * @author Rodrigo Ayala
 */
public class SExportDataAvoBp implements SExportData {

    public SExportDataAvoBp() {
        
    }

    /**
     * ID del asociado de negocios (proveedor o empleado) en el sistema ERP.
     */
    public int erp_id;

    /**
     * Nombre completo o razón social del asociado de negocios.
     */
    public String name;

    /**
     * Nombre comercial del asociado de negocios.
     */
    public String trade_name;

    /**
     * RFC o identificador fiscal del asociado de negocios.
     */
    public String fiscal_id;

    /**
     * Arreglo numérico que indica los roles del asociado de negocios en el sistema de destino
     * (por ejemplo: [1] para Proveedor, [2] para Empleado, o [1, 2] para ambos).
     */
    public int[] types;
}
