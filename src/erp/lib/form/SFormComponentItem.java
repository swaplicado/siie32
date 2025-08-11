/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.form;

/**
 *
 * @author Sergio Flores
 */
public class SFormComponentItem implements java.io.Serializable, java.lang.Comparable<SFormComponentItem> {
    
    private java.lang.Object moPrimaryKey;
    private java.lang.String msItem;
    private java.lang.Object moForeignKey;
    private java.lang.Object moComplement;
    
    public SFormComponentItem(java.lang.Object pk, java.lang.String item) {
        this(pk, item, null);
    }

    public SFormComponentItem(java.lang.Object pk, java.lang.String item, java.lang.Object fk) {
        moPrimaryKey = pk;
        msItem = item;
        moForeignKey = fk;

        moComplement = null;
    }

    public void setPrimaryKey(java.lang.Object o) { moPrimaryKey = o; }
    public void setForeignKey(java.lang.Object o) { moForeignKey = o; }
    public void setItem(java.lang.String s) { msItem = s; }
    public void setComplement(java.lang.Object o) { moComplement = o; }
    
    public java.lang.Object getPrimaryKey() { return moPrimaryKey; }
    public java.lang.Object getForeignKey() { return moForeignKey; }
    public java.lang.String getItem() { return msItem; }
    public java.lang.Object getComplement() { return moComplement; }
    
    @Override
    public java.lang.String toString() {
        return getItem();
    }

    @Override
    public int compareTo(SFormComponentItem o) {
        return this.toString().compareTo(o.toString());
    }
}
