/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import java.util.ArrayList;

/**
 * Clase que representa la estructura de datos para exportar información de referencias en formato JSON.
 * Utilizada para el intercambio de datos con sistemas externos.
 * 
 * Incluye anotaciones de Jackson para controlar la serialización/deserialización JSON.
 *
 * @author Edwin Carmona
 */
public class SExportDataDpsContainer implements SExportData {

    public SExportDataDpsContainer() {
        this.file = new ArrayList<>();
    }
    
    /**
     *  Objeto de exportación de DPS
     */
    public SExportDataDps document;

    /**
     * Objeto de exportación de archivo PDF subido a google
     */
    public ArrayList<SExportDataDpsFile> file;
}
