package erp.siieapp.portalproveedores;

import java.util.ArrayList;

/**
 * Contenedor de respuesta para el estado de cuenta de un proveedor.
 * <p>
 * Agrupa la lista de renglones ({@link SAccountStatusData}) que se serializa
 * como JSON y se envía al portal de proveedores.
 * </p>
 *
 * @author César Orozco
 */
public class SDataAccountStatusResponse {

    /**
     * Lista de renglones del estado de cuenta.
     */
    ArrayList<SAccountStatusData> lASData;

    public void setlASData(ArrayList<SAccountStatusData> lASData) {
        this.lASData = lASData;
    }

    public ArrayList<SAccountStatusData> getlASData() {
        return lASData;
    }
}
