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
public class SRepScaleTicketsMovementsGroup {
    
    ArrayList<SRepScaleTicketsMovementsGroupCat> cat;
    
    public ArrayList<SRepScaleTicketsMovementsGroupCat> getCat() {
        return cat;
    }
    
    public void setCat(ArrayList<SRepScaleTicketsMovementsGroupCat> cat) {
        this.cat = cat;
    }
}