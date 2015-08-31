/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Sergio Flores
 */
public class STrnItemFound {

    protected double mdQuantity;
    protected String msItemKey;
    protected int[] manItemFoundIds;

    public STrnItemFound(double quantity, String itemKey) {
        mdQuantity = quantity;
        msItemKey = itemKey;
        manItemFoundIds = null;
    }

    public void setQuantity(double d) { mdQuantity = d; }
    public void setItemKey(String s) { msItemKey = s; }
    public void setItemFoundIds(int[] an) { manItemFoundIds = an; }

    public double getQuantity() { return mdQuantity; }
    public String getItemKey() { return msItemKey; }
    public int[] getItemFoundIds() { return manItemFoundIds; }
}
