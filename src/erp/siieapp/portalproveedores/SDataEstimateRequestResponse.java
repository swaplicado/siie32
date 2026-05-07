/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.portalproveedores;

import java.util.ArrayList;

/**
 * Contenedor de respuesta para las solicitudes de cotización.
 * <p>
 * Agrupa la lista de solicitudes ({@link SEstimateRequestData}) que se
 * serializa como JSON y se envía al portal de proveedores.
 * </p>
 *
 * @author swaplicado
 */
public class SDataEstimateRequestResponse {

    /**
     * Lista de solicitudes de cotización.
     */
    ArrayList<SEstimateRequestData> lERData;

    public void setERData(ArrayList<SEstimateRequestData> lERData) {
        this.lERData = lERData;
    }

    public ArrayList<SEstimateRequestData> getlPOData() {
        return lERData;
    }
}
