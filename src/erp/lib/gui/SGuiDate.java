/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.gui;

/**
 *
 * @author Sergio Flores
 */
public final class SGuiDate extends java.util.Date {
    
    private int mnDataType;

    /**
     * @param dataType Constants defined in erp.lib.SLibConstants.
     */
    public SGuiDate(int dataType) {
        this(0, dataType);
    }

    public SGuiDate(long date, int dataType) {
        super(date);
        mnDataType = dataType;
    }

    public void setDataType(int n) { mnDataType = n; }
    
    public int getDataType() { return mnDataType; }
}
