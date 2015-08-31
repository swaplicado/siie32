/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import javax.swing.JLabel;

/**
 *
 * @author Sergio Flores
 */
public class STableCellRendererDefaultColor extends javax.swing.table.DefaultTableCellRenderer {

    private java.awt.Color moColor;
    private javax.swing.JLabel moLabel;
    
    public STableCellRendererDefaultColor(java.awt.Color color) {
        moColor = color;
        moLabel = new JLabel();
        moLabel.setOpaque(true);
    }
    
    public void setColor(java.awt.Color o) { moColor = o; }
    public void setLabel(javax.swing.JLabel o) { moLabel = o; }
    
    public java.awt.Color getColor() { return moColor; }
    public javax.swing.JLabel getLabel() { return moLabel; }
    
    @Override
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        moLabel.setText(value != null ? value.toString() : "");

        if (isSelected) {
            if (table.isCellEditable(row, col)) {
                moLabel.setForeground(STableConstants.FOREGROUND_EDIT);
                moLabel.setBackground(hasFocus ? STableConstants.BACKGROUND_SELECT_EDIT_FOCUS : STableConstants.BACKGROUND_SELECT_EDIT);
            }
            else {
                moLabel.setForeground(moColor);
                moLabel.setBackground(hasFocus ? STableConstants.BACKGROUND_SELECT_READ_FOCUS : STableConstants.BACKGROUND_SELECT_READ);
            }
        }
        else {
            if (table.isCellEditable(row, col)) {
                moLabel.setForeground(STableConstants.FOREGROUND_EDIT);
                moLabel.setBackground(STableConstants.BACKGROUND_PLAIN_EDIT);
            }
            else {
                moLabel.setForeground(moColor);
                moLabel.setBackground(STableConstants.BACKGROUND_PLAIN_READ);
            }
        }

        return moLabel;
    }
}
