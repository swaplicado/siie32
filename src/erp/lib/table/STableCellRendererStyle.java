/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;

/**
 *
 * @author Sergio Flores
 */
public class STableCellRendererStyle extends javax.swing.table.DefaultTableCellRenderer {
    
    private javax.swing.JLabel moLabel;
    private java.awt.Font moFontPlain;
    private java.awt.Font moFontBold;
    private java.awt.Font moFontItalic;
    
    public STableCellRendererStyle() {
        moLabel = new JLabel();
        moLabel.setOpaque(true);
        
        moFontPlain = moLabel.getFont();
        moFontBold = moLabel.getFont().deriveFont(Font.BOLD);
        moFontItalic = moLabel.getFont().deriveFont(Font.ITALIC);
    }
    
    public void setLabel(javax.swing.JLabel o) { moLabel = o; }
    
    public javax.swing.JLabel getLabel() { return moLabel; }
    
    @Override
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        int style = ((STableModel) table.getModel()).getTableRow(row).getStyle();

        moLabel.setText(value != null ? value.toString() : "");

        switch (style) {
            case STableConstants.STYLE_BOLD:
                moLabel.setFont(moFontBold);
                break;
            case STableConstants.STYLE_ITALIC:
                moLabel.setFont(moFontItalic);
                break;
            default:
                moLabel.setFont(moFontPlain);
                break;
        }

        if (isSelected) {
            if (table.isCellEditable(row, col)) {
                moLabel.setForeground(STableConstants.FOREGROUND_EDIT);
                moLabel.setBackground(hasFocus ? STableConstants.BACKGROUND_SELECT_EDIT_FOCUS : STableConstants.BACKGROUND_SELECT_EDIT);
            }
            else {
                moLabel.setForeground(style == STableConstants.STYLE_ITALIC ? Color.BLUE.darker() : STableConstants.FOREGROUND_READ);
                moLabel.setBackground(hasFocus ? STableConstants.BACKGROUND_SELECT_READ_FOCUS : STableConstants.BACKGROUND_SELECT_READ);
            }
        }
        else {
            if (table.isCellEditable(row, col)) {
                moLabel.setForeground(STableConstants.FOREGROUND_EDIT);
                moLabel.setBackground(STableConstants.BACKGROUND_PLAIN_EDIT);
            }
            else {
                moLabel.setForeground(style == STableConstants.STYLE_ITALIC ? Color.BLUE.darker() : STableConstants.FOREGROUND_READ);
                moLabel.setBackground(STableConstants.BACKGROUND_PLAIN_READ);
            }
        }
        
        return moLabel;
    }
}
