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
 * @author Edwin Carmona
 */
public class SExportDataDpsFile implements SExportData {

    public SExportDataDpsFile() {
        filename_storage = "";
        filename_original = "";
        title = "";
        url_storage = "#";
        url_database = "#";
        bucket_name = "";
        project_id = "";
    }
    
    /*
     * nombre del archivo en cloud storage
     */
    public String filename_storage;

    /**
     * nombre original del archivo
     */
    public String filename_original;
    
    /**
     * título o descripción del archivo
     */
    public String title;

    /**
     * url del archivo en cloud storage
     */
    public String url_storage;
    
    /**
     * url del archivo en la base de datos
     */
    public String url_database;
    
    /**
     * nombre del bucket en cloud storage
     */
    public String bucket_name;

    /**
     * ID del proyecto en cloud storage
     */
    public String project_id;
}
