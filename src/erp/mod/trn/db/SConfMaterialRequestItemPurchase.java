/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import java.util.ArrayList;

/**
 *
 * @author Isabel Servín
 */
public class SConfMaterialRequestItemPurchase {
    ArrayList<Integer> igen;
    ArrayList<Integer> item;
    
    public SConfMaterialRequestItemPurchase() {
        this.igen = new ArrayList<>();
        this.item = new ArrayList<>();
    }
    
    public ArrayList<Integer> getigen() {
        return igen;
    }
    
    public ArrayList<Integer> getitem() {
        return item;
    }
    
    public void setigen(ArrayList<Integer> igen) {
        this.igen = igen;
    }
    
    public void setitem(ArrayList<Integer> item) {
        this.item = item;
    }
}
