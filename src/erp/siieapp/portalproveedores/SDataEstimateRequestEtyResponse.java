/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.portalproveedores;

import java.util.ArrayList;

/**
 * Contenedor de respuesta para las partidas de una solicitud de cotización.
 * <p>
 * Agrupa la lista de partidas ({@link SEstimateRequestEtyData}) que se
 * serializa como JSON y se envía al portal de proveedores.
 * </p>
 *
 * @author César Orozco
 */
public class SDataEstimateRequestEtyResponse {

    /**
     * Lista de partidas de la solicitud de cotización.
     */
    ArrayList<SEstimateRequestEtyData> lEREData;

    public void setlEREData(ArrayList<SEstimateRequestEtyData> lEREData) {
        this.lEREData = lEREData;
    }

    public ArrayList<SEstimateRequestEtyData> getlPOEData() {
        return lEREData;
    }
}
