/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.portalproveedores;

import java.util.ArrayList;

/**
 * Contenedor de respuesta para las partidas de órdenes de compra.
 * <p>
 * Agrupa la lista de partidas ({@link SPurcharseOrderEtyData}) que se serializa
 * como JSON y se envía al portal de proveedores.
 * </p>
 *
 * @author swaplicado
 */
public class SDataEtyResponse {

    /**
     * Lista de partidas de la orden de compra.
     */
    ArrayList<SPurcharseOrderEtyData> lPOEData;

    public void setlPOEData(ArrayList<SPurcharseOrderEtyData> lPOEData) {
        this.lPOEData = lPOEData;
    }

    public ArrayList<SPurcharseOrderEtyData> getlPOEData() {
        return lPOEData;
    }
}
