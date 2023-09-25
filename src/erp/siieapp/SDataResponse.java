/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

import java.util.ArrayList;

/**
 *
 * @author AdrianAviles
 */
public class SDataResponse {
    ArrayList<SAuthorizationsData> lAuthData;

    public void setlAuthData(ArrayList<SAuthorizationsData> lAuthData) {
        this.lAuthData = lAuthData;
    }

    public ArrayList<SAuthorizationsData> getlAuthData() {
        return lAuthData;
    }
}
