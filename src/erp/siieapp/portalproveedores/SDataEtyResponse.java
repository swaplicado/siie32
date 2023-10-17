/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.portalproveedores;

import java.util.ArrayList;

/**
 *
 * @author swaplicado
 */
public class SDataEtyResponse {
    ArrayList<SPurcharseOrderEtyData> lPOEData;

    public void setlPOEData(ArrayList<SPurcharseOrderEtyData> lPOEData) {
        this.lPOEData = lPOEData;
    }

    public ArrayList<SPurcharseOrderEtyData> getlPOEData() {
        return lPOEData;
    }
}
