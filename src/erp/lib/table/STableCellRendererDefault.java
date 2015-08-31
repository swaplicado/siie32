/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import java.awt.Color;
import java.awt.font.TextAttribute;
import java.util.Map;
import javax.swing.JLabel;

/**
 *
 * @author Sergio Flores
 */
public class STableCellRendererDefault extends javax.swing.table.DefaultTableCellRenderer {
    
    private javax.swing.JLabel moLabel;
    
    public STableCellRendererDefault() {
        moLabel = new JLabel();
        moLabel.setOpaque(true);
    }
    
    public void setLabel(javax.swing.JLabel o) { moLabel = o; }
    
    public javax.swing.JLabel getLabel() { return moLabel; }
    
    @Override
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        int style = ((STableModel) table.getModel()).getTableRow(row).getStyle();
        moLabel.setText(value != null ? value.toString() : "");
        Map attributes = moLabel.getFont().getAttributes();       

        if (isSelected) {
            if (table.isCellEditable(row, col)) {
                moLabel.setForeground(STableConstants.FOREGROUND_EDIT);
                moLabel.setBackground(hasFocus ? STableConstants.BACKGROUND_SELECT_EDIT_FOCUS : STableConstants.BACKGROUND_SELECT_EDIT);
            }
            else {
                moLabel.setForeground(STableConstants.FOREGROUND_READ);
                moLabel.setBackground(hasFocus ? STableConstants.BACKGROUND_SELECT_READ_FOCUS : STableConstants.BACKGROUND_SELECT_READ);
            }
        }
        else {
            if (table.isCellEditable(row, col)) {
                moLabel.setForeground(STableConstants.FOREGROUND_EDIT);
                moLabel.setBackground(STableConstants.BACKGROUND_PLAIN_EDIT);
            }
            else {
                moLabel.setForeground(STableConstants.FOREGROUND_READ);
                moLabel.setBackground(STableConstants.BACKGROUND_PLAIN_READ);
            }
        }

        switch (style) {
            case STableConstants.STYLE_DELETE:
                moLabel.setForeground(Color.DARK_GRAY);
                attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                break;
            default:
                attributes.put(TextAttribute.STRIKETHROUGH, !TextAttribute.STRIKETHROUGH_ON);
                break;
        }
        
        moLabel.setFont(moLabel.getFont().deriveFont(attributes));
        
        return moLabel;
    }
}
