/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

import java.util.ArrayList;

/**
 * Estructura de respuesta que encapsula una lista de datos de autorización.
 * 
 * Utilizada para transportar información sobre las autorizaciones de recursos que se consultan
 * desde servicios externos. Actúa como contenedor de múltiples registros de autorización
 * obtenidos de consultas a bases de datos o APIs externas.
 * 
 * @author Adrián Avilés
 * @version 1.0
 */
public class SDataResponse {
    /** Lista de datos de autorizaciones de recursos */
    ArrayList<SAuthorizationsData> lAuthData;

    /**
     * Establece la lista de datos de autorización.
     * @param lAuthData lista de objetos SAuthorizationsData que contienen información de autorizaciones
     */
    public void setlAuthData(ArrayList<SAuthorizationsData> lAuthData) {
        this.lAuthData = lAuthData;
    }

    /**
     * Obtiene la lista de datos de autorización.
     * @return lista de objetos SAuthorizationsData
     */
    public ArrayList<SAuthorizationsData> getlAuthData() {
        return lAuthData;
    }
}
