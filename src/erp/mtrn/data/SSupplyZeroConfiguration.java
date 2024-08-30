/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import java.util.ArrayList;

/**
 *
 * @author Edwin Carmona
 */
public class SSupplyZeroConfiguration {
    ArrayList<Integer> items;

    public SSupplyZeroConfiguration() {
        this.items = new ArrayList<>();
    }

    public ArrayList<Integer> getItems() {
        return items;
    }

    public void setItems(ArrayList<Integer> items) {
        this.items = items;
    }
    
}
