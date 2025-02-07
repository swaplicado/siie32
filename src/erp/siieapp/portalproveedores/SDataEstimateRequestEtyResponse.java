/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.portalproveedores;

import java.util.ArrayList;

/**
 *
 * @author César Orozco
 */
public class SDataEstimateRequestEtyResponse {
    ArrayList<SEstimateRequestEtyData> lEREData;

    public void setlEREData(ArrayList<SEstimateRequestEtyData> lEREData) {
        this.lEREData = lEREData;
    }

    public ArrayList<SEstimateRequestEtyData> getlPOEData() {
        return lEREData;
    }
}
