/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.form;

/**
 *
 * @author Sergio Flores
 */
public class SFormValidation {
    
    private int mnTabbedPaneIndex;
    private boolean mbIsError;
    private java.lang.String msMessage;
    private javax.swing.JComponent moComponent;
    
    public SFormValidation() {
        mnTabbedPaneIndex = -1;
        mbIsError = false;
        msMessage = "";
        moComponent = null;
    }
    
    public void setTabbedPaneIndex(int n) { mnTabbedPaneIndex = n; }
    public void setIsError(boolean b) { mbIsError = b; }
    public void setMessage(java.lang.String s) { mbIsError = true; msMessage = s; }
    public void setComponent(javax.swing.JComponent o) { moComponent = o; }
    
    public int getTabbedPaneIndex() { return mnTabbedPaneIndex; }
    public boolean getIsError() { return mbIsError; }
    public java.lang.String getMessage() { return msMessage; }
    public javax.swing.JComponent getComponent() { return moComponent; }
}
