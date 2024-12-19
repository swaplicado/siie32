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
public class SDataEstimateRequestResponse {
    ArrayList<SEstimateRequestData> lERData;

    public void setERData(ArrayList<SEstimateRequestData> lERData) {
        this.lERData = lERData;
    }

    public ArrayList<SEstimateRequestData> getlPOData() {
        return lERData;
    }
}
