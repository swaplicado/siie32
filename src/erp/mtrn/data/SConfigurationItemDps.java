/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import java.util.ArrayList;

/**
 *
 * @author Isabel Servín
 */
public class SConfigurationItemDps {
    ArrayList<Integer> ifam;
    ArrayList<Integer> igen;
    
    public SConfigurationItemDps() {
        this.ifam = new ArrayList<>();
        this.igen = new ArrayList<>();
    }
    
    public ArrayList<Integer> getifam() {
        return ifam;
    }
    
    public ArrayList<Integer> getigen() {
        return igen;
    }
    
    public void setifam(ArrayList<Integer> ifam) {
        this.ifam = ifam;
    }
    
    public void setigen(ArrayList<Integer> igen) {
        this.igen = igen;
    }
}
