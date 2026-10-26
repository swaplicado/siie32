package erp.siieapp.portalproveedores;

import java.util.ArrayList;

/**
 * Contenedor de respuesta para las órdenes de compra.
 * <p>
 * Agrupa la lista de órdenes ({@link SPurcharseOrdersData}) que se serializa
 * como JSON y se envía al portal de proveedores.
 * </p>
 *
 * @author César Orozco
 */
public class SDataResponse {

    /**
     * Lista de órdenes de compra.
     */
    ArrayList<SPurcharseOrdersData> lPOData;

    public void setlPOData(ArrayList<SPurcharseOrdersData> lPOData) {
        this.lPOData = lPOData;
    }

    public ArrayList<SPurcharseOrdersData> getlPOData() {
        return lPOData;
    }
}
