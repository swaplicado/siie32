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
public class SConfigurationPurposeCfdUse {
    @JsonProperty(SDataConstantsSys.TRNU_DPS_NAT_FIX_ASSET)
    ArrayList<String> fixAsset;
    
    public SConfigurationPurposeCfdUse() {
        this.fixAsset = new ArrayList<>();
    }
    
    public ArrayList<String> getfixAsset(){
        return fixAsset;
    }
    
    public void setfixAsset(ArrayList<String> fixAsset) {
        this.fixAsset = fixAsset;
    }
}
