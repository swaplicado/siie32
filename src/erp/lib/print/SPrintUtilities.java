/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.print;

import java.util.Vector;

import erp.lib.*;
import java.awt.Font;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Sergio Flores
 */
public abstract class SPrintUtilities {
    
    public static java.lang.String formatText(java.lang.String text, int length, int align, int truncMode) {
        java.lang.String s = "";
        
        if (text.length() <= length) {
            switch (align) {
                case SLibConstants.ALIGN_LEFT:
                    s = text + SLibUtilities.textRepeat(" ", length - text.length());
                    break;
                case SLibConstants.ALIGN_RIGHT:
                    s = SLibUtilities.textRepeat(" ", length - text.length()) + text;
                    break;
                case SLibConstants.ALIGN_CENTER:
                    s = SLibUtilities.textRepeat(" ", ((length - text.length()) / 2)) + text + SLibUtilities.textRepeat(" ", ((length - text.length()) / 2) + ((length - text.length()) % 2 == 0 ? 0 : 1));
                    break;
                default:
            }
        }
        else {
            if (truncMode == SLibConstants.TRUNC_MODE_HIDE) {
                s = SLibUtilities.textRepeat("*", length);
            }
            else if (truncMode == SLibConstants.TRUNC_MODE_TRUNC) {
                switch (align) {
                    case SLibConstants.ALIGN_LEFT:
                        s = text.substring(0, length);
                        break;
                    case SLibConstants.ALIGN_RIGHT:
                        s = text.substring(text.length() - length);
                        break;
                    case SLibConstants.ALIGN_CENTER:
                        s = text.substring((text.length() - length) / 2, ((text.length() - length) / 2) + length);
                        break;
                    default:
                }
            }
        }
        
        return s;
    }

    /** Takes a string and separes it in several rows, preserving words. */
    public static java.lang.String[] formatTextSeveralRows(final java.lang.String value, final int width, final int align) {

        int indexBlank;
        String aux = null;
        String text = SLibUtilities.textTrim(value);
        String[] values = null;
        Vector<String> vecValues = new Vector<String>();

	if (text.length() <= width) {
            vecValues.add(text);
        }
        else {

            while (text.length() > width) {

                aux = text.substring(0, width);
                indexBlank = aux.lastIndexOf(" ");

                if (indexBlank != -1) {
                    aux = aux.substring(0, indexBlank);
                }

                vecValues.add(aux);
                text = SLibUtilities.textTrim(text.substring(aux.length(), text.length()));
            }

            if (text.length() > 0) {
                vecValues.add(text);
            }
        }

        values = new String[vecValues.size()];
        for (int i = 0; i < vecValues.size(); i++) {
            values[i] = formatText((String) vecValues.get(i), width, align, SPrintConstants.TRUNC_MOD_TRUNC);
        }

        return values;

    } // public static ...String[] formatTextSeveralRows(...

    /*
     * Methods to format true type font text:
     */

    /** Takes a string and separes it in several rows, preserving words. */
    public static java.lang.String[] formatTextSeveralRows2D(final java.lang.String value, final int width, java.awt.FontMetrics fontMetrics) {

        int i;
        int indexBlank;
        String aux = null;
        String text = SLibUtilities.textTrim(value);
        String[] values = null;
        Vector<String> vecValues = new Vector<String>();

	if (fontMetrics.stringWidth(text) <= width) {
            vecValues.add(text);
        }
        else {

            /*
            123456789012345
            Este es un texto que debe ser truncado por ser parangacutirimícuaro.

            Este es un
            texto que debe
            ser truncado
            por ser
            parangacutirimí
            cuaro.
             */

            while (fontMetrics.stringWidth(text) > width) {

                i = 0;
                aux = "";

                for (i = 0; fontMetrics.stringWidth(aux + text.substring(i, i + 1)) <= width && i < text.length(); i++) {
                    aux += text.substring(i, i + 1);
                }

                indexBlank = aux.lastIndexOf(" ");

                if (indexBlank != -1) {
                    aux = aux.substring(0, indexBlank);
                }

                vecValues.add(aux);
                text = SLibUtilities.textTrim(text.substring(aux.length(), text.length()));
            }

            if (text.length() > 0) {
                vecValues.add(text);
            }
        }

        values = new String[vecValues.size()];
        for (i = 0; i < vecValues.size(); i++) {
            values[i] = (String) vecValues.get(i);
        }

        return values;

    } // public static ...String[] formatTextSeveralRows(...

    /** Displays text in a Graphics context */
    private static void drawString2D(java.lang.String text, java.awt.Graphics2D g2D,
        int align, int marginLeft, int marginRight, int posY) {

        int width = marginRight - marginLeft;
        String textFormated = text;
        java.awt.FontMetrics fm = g2D.getFontMetrics();

        switch (align) {

            case SPrintConstants.ALIGN_LEFT:

                while (fm.stringWidth(textFormated) > width && textFormated.length() > 0) {

                    // String graphics width exceeds limit, so remove, char by char right most characters:
                    textFormated = textFormated.substring(0, textFormated.length() - 1);
                }
                g2D.drawString(textFormated, marginLeft, posY);
                break;

            case SPrintConstants.ALIGN_CENTER:

                while (fm.stringWidth(textFormated) > width && textFormated.length() > 0) {

                    // String graphics width exceeds limit, so remove, char by char right most characters:
                    textFormated = textFormated.substring(0, textFormated.length() - 1);
                }
                g2D.drawString(textFormated, marginLeft + ((width - fm.stringWidth(textFormated)) / 2), posY);
                break;

            case SPrintConstants.ALIGN_RIGHT:

                while (fm.stringWidth(textFormated) > width && textFormated.length() > 0) {

                    // String width exceeds limit, so remove, char by char left most characters:
                    textFormated = textFormated.substring(1);
                }
                g2D.drawString(textFormated, marginLeft + (width - fm.stringWidth(textFormated)), posY);
                break;
        }

    } // private void drawString2D(...

    /** Prints out a DPrintableItem object */
    public static void printPrintableItem(erp.lib.print.SPrintableItem item, java.awt.Graphics2D g2D, boolean bRotate) {

        if (!bRotate) {
            g2D.setFont(item.font);
        }
        else {
            // Create a rotation transformation for the font.
            
            AffineTransform oFontAT = new AffineTransform();
            oFontAT.rotate(Math.PI / 2.0);
            Font oFontRotated = item.font.deriveFont(oFontAT);        
            g2D.setFont(oFontRotated);
        }
                
        printText2D(item.text, g2D, item.align, item.x, item.x + item.width, item.y, 1, item.truncPolicy);
        
        // Put the original font back
        
        g2D.setFont(item.font);
        
    } // public void printPrintableItem(...

    /** Formats and displays text in a Graphics context */
    public static void printText2D(java.lang.String text, java.awt.Graphics2D g2D,
        int align, int marginLeft, int marginRight, int posY, int rows, int trunc) {

        int width = marginRight - marginLeft;
        String textFormated = text;
        java.awt.FontMetrics fm = g2D.getFontMetrics();

        if (width > 0 && textFormated.length() > 0) {

            if (rows == 1) {

                /*
                 * Only one row of text is going to be printed
                 */

                if (trunc == SPrintConstants.TRUNC_MOD_X_FILL && fm.stringWidth(textFormated) > width) {

                    textFormated = "";

                    while (fm.stringWidth(textFormated) < width) {
                        if (fm.stringWidth(textFormated) + fm.stringWidth("X") <= width) {
                            textFormated += "X";
                        }
                        else {
                            break;
                        }
                    }
                }

                // Display text:
                drawString2D(textFormated, g2D, align, marginLeft, marginRight, posY);
            }
            else {

                /*
                 * More than one row of text is going to be printed
                 */

                int y = posY;
                int row = 1;
                int index = 0;
                int curIndex = 0;
                String textRow = null;      // text to be displayed in a row

                while (row <= rows && curIndex < text.length()) {

                    textRow = "";           // starting a new row

                    // Determine text to display for current row:

                    while (curIndex < text.length()) {

                        if (fm.stringWidth(textRow) + fm.stringWidth("" + textFormated.charAt(curIndex)) <= width) {
                            textRow += textFormated.charAt(curIndex++);
                        }
                        else {
                            break;
                        }
                    }

                    // Attempt to not display truncated words:
                    index = textRow.lastIndexOf(" ");
                    if (index != -1) {
                        textRow = textRow.substring(0, index).trim();
                        curIndex = index + 1;
                    }

                    // Display text:
                    drawString2D(textRow, g2D, align, marginLeft, marginRight, y);

                    row++;
                    y += fm.getHeight();

                } // while (row...

            } // if (rows... else...

        } // if (width...

    } // public void printText(...
}
