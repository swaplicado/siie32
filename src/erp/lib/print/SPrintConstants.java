/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.print;

import java.awt.Font;

/**
 *
 * @author Alfonso Flores
 */
public abstract class SPrintConstants {

    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int ALIGN_CENTER = 3;

    public static final int TRUNC_MOD_TRUNC = 1;
    public static final int TRUNC_MOD_NO_TRUNC = 2;
    public static final int TRUNC_MOD_X_FILL = 3;

    public static final int TAB = 72;
    public static final int TAB_HALF = TAB / 2;
    public static final int TAB_QUARTER = TAB / 4;
    public static final int TAB_EIGHTH = TAB / 8;

    public static final int PRINT_SERIAL_UNDEFINED = 0;
    public static final int PRINT_SERIAL_NO = 1;
    public static final int PRINT_SERIAL = 2;

    public static final Font fontHeaderTitle01 = new Font("sanserif", Font.BOLD, 10);
    public static final Font fontHeaderTitle02 = new Font("sanserif", Font.ITALIC, 10);
    public static final Font fontBodyText = new Font("sanserif", Font.PLAIN, 8);
    public static final Font fontBodyTextBold = new Font("sanserif", Font.BOLD, 8);
    public static final Font fontBodyNumber = new Font("monospaced", Font.PLAIN, 9);
    public static final Font fontBodyNumberBold = new Font("monospaced", Font.BOLD, 9);
    public static final Font fontBodyNumberArial = new Font("arial", Font.PLAIN, 9);
    public static final Font fontBodyNumberBoldArial = new Font("arial", Font.BOLD, 9);
    public static final Font fontText = new Font("sanserif", Font.PLAIN, 8);
    public static final Font fontTextBold = new Font("sanserif", Font.BOLD, 8);
    public static final Font fontTextMedium = new Font("sanserif", Font.PLAIN, 7);
    public static final Font fontTextMediumBold = new Font("sanserif", Font.BOLD, 7);
    public static final Font fontTextSmall = new Font("sanserif", Font.PLAIN, 6);
    public static final Font fontTextSmallBold = new Font("sanserif", Font.BOLD, 6);

    public static final Font fontGraphTitle = new Font("sanserif", Font.BOLD, 15);
    public static final Font fontGraphText = new Font("sanserif", Font.PLAIN, 10);
    public static final Font fontGraphTextBold = new Font("sanserif", Font.BOLD, 10);
}
