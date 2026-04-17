/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.model.account;

import java.util.regex.Pattern;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class Tax {
    
    public static final String TYPE_ADDED = "added";
    public static final String TYPE_WITHHELD = "withheld";
    
    private String type;
    private String tax;
    private String factor;
    private String rate;
    private String pk;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getTax() { return tax; }
    public void setTax(String tax) { this.tax = tax; }
    
    public String getFactor() { return factor; }
    public void setFactor(String factor) { this.factor = factor; }
    
    public String getRate() { return rate; }
    public void setRate(String rate) { this.rate = rate; }
    
    public String getPk() { return pk; }
    public void setPk(String pk) { this.pk = pk; }
    
    public int[] getTaxKey() {
        String[] key = pk.split(Pattern.quote("-"));
        return new int[] { SLibUtils.parseInt(key[0]), SLibUtils.parseInt(key[1]) };
    }
}
