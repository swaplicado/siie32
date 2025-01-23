package erp.siieapp.portalproveedores;

import java.util.ArrayList;

/**
 *
 * @author César Orozco
 */
public class SDataAccountStatusResponse {
    ArrayList<SAccountStatusData> lASData;

    public void setlASData(ArrayList<SAccountStatusData> lASData) {
        this.lASData = lASData;
    }

    public ArrayList<SAccountStatusData> getlASData() {
        return lASData;
    }
}
