package erp.siieapp.portalproveedores;

import java.util.ArrayList;

/**
 *
 * @author César Orozco
 */
public class SDataResponse {
    ArrayList<SPurcharseOrdersData> lPOData;

    public void setlPOData(ArrayList<SPurcharseOrdersData> lPOData) {
        this.lPOData = lPOData;
    }

    public ArrayList<SPurcharseOrdersData> getlPOData() {
        return lPOData;
    }
}