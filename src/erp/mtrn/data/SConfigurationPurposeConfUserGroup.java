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
public class SConfigurationPurposeConfUserGroup {
    ArrayList<SConfigurationPurposeConfUserGroupDetail> cfg;
    
    public SConfigurationPurposeConfUserGroup() {
        this.cfg = new ArrayList<>();
    }
    
    public ArrayList<SConfigurationPurposeConfUserGroupDetail> getcfg() {
        return cfg;
    }
    
    public void setcfg(ArrayList<SConfigurationPurposeConfUserGroupDetail> cfg) {
        this.cfg = cfg;
    }
}
