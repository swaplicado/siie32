/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.form;

/**
 *
 * @author Sergio Flores
 */
public interface SFormFieldInterface {

    public void setDataType(int type);
    public void setKey(java.lang.Object key);
    public void setString(java.lang.String string);
    public void setFieldValue(java.lang.Object value);
    public void setComponent(javax.swing.JComponent component);

    public int getDataType();
    public java.lang.Object getKey();
    public java.lang.String getString();
    public java.lang.Object getFieldValue();
    public javax.swing.JComponent getComponent();
}
