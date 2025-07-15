/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import erp.data.SDataConstantsSys;
import java.util.ArrayList;

/**
 *
 * @author Isabel Servín
 */
public class SConfigurationPurposeDpsNature {
    @JsonProperty(SDataConstantsSys.TRNU_DPS_NAT_FIX_ASSET)
    ArrayList<Integer> fixAsset;
    
    public SConfigurationPurposeDpsNature() {
        this.fixAsset = new ArrayList<>();
    }
    
    public ArrayList<Integer> getfixAsset(){
        return fixAsset;
    }
    
    public void setfixAsset(ArrayList<Integer> fixAsset) {
        this.fixAsset = fixAsset;
    }
}
