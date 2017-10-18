/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

/**
 *
 * @author Néstor Ávalos, Claudio Peña
 */
public class SParamsItemPriceList {

    protected boolean mbItemPriceFound;
    protected double mdItemPrice;
    protected double mdItemDiscount;
    protected int mnCurrencyId;
    
    public SParamsItemPriceList() {
        mbItemPriceFound = false;
        mdItemPrice = 0d;
        mdItemDiscount = 0d;
        mnCurrencyId = 0;
    }

    public void setItemPriceFound(boolean b) { mbItemPriceFound = b; }
    public void setItemPrice(double d) { mdItemPrice = d; }
    public void setItemDiscount(double d) { mdItemDiscount = d; }
    public void setCurrencyId(int n) { mnCurrencyId = n; }

    public boolean isItemPriceFound() { return mbItemPriceFound; }
    public double getItemPrice() { return mdItemPrice; }
    public double getItemDiscount() { return mdItemDiscount; }
    public int getCurrencyId() { return mnCurrencyId; }
}
