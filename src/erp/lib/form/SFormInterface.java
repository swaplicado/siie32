/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.form;

/**
 *
 * @author Sergio Flores
 */
public interface SFormInterface {

    public void formClearRegistry();
    public void formReset();
    public void formRefreshCatalogues();
    public erp.lib.form.SFormValidation formValidate();
    public void setFormStatus(int status);
    public void setFormVisible(boolean visible);
    public int getFormStatus();
    public int getFormResult();
    public void setRegistry(erp.lib.data.SDataRegistry registry);
    public erp.lib.data.SDataRegistry getRegistry();
    public void setValue(int type, java.lang.Object value);
    public java.lang.Object getValue(int type);
    public javax.swing.JLabel getTimeoutLabel();
}
