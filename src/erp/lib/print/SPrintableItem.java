/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.print;

/**
 *
 * @author Alfonso Flores
 */
public class SPrintableItem {

    public java.awt.Font font;
    public java.lang.String text;
    public int x;
    public int y;
    public int width;
    public int align;
    public int truncPolicy;

    /** Creates a new instance of SPrintableItem */
    public SPrintableItem(java.lang.String t, java.awt.Font f) {

        text = t;
        font = f;

        x = 0;
        y = 0;
        width = 0;

        truncPolicy = SPrintConstants.TRUNC_MOD_TRUNC;
        align = SPrintConstants.ALIGN_LEFT;
    }
}
