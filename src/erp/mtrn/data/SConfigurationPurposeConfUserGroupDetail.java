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
public class SConfigurationPurposeConfUserGroupDetail {
    ArrayList<Integer> usrGrp;
    ArrayList<Integer> dpsNat;
    String cfdUse;
    
    public SConfigurationPurposeConfUserGroupDetail() {
        this.usrGrp = new ArrayList<>();
        this.dpsNat = new ArrayList<>();
    }
    
    public ArrayList<Integer> getusrGrp() {
        return usrGrp;
    }
    
    public ArrayList<Integer> getdpsNat() {
        return dpsNat;
    }
    
    public String getcfdUse() {
        return cfdUse;
    }
    
    public void setusrGrp(ArrayList<Integer> usrGrp) {
        this.usrGrp = usrGrp;
    }
    
    public void setdpsNat(ArrayList<Integer> dpsNat) {
        this.dpsNat = dpsNat;
    }
    
    public void setcfdUse(String cfdUse) {
        this.cfdUse = cfdUse;
    }
}
