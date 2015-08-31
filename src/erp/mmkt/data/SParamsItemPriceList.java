/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

/**
 *
 * @author Néstor Ávalos
 */
public class SParamsItemPriceList {

    protected boolean mbItemPriceFound;
    protected double mdItemPrice;
    protected double mdItemDiscount;

    public SParamsItemPriceList() {
        mbItemPriceFound = false;
        mdItemPrice = 0d;
        mdItemDiscount = 0d;
    }

    public void setItemPriceFound(boolean b) { mbItemPriceFound = b; }
    public void setItemPrice(double d) { mdItemPrice = d; }
    public void setItemDiscount(double d) { mdItemDiscount = d; }

    public boolean getItemPriceFound() { return mbItemPriceFound; }
    public double getItemPrice() { return mdItemPrice; }
    public double getItemDiscount() { return mdItemDiscount; }
}
