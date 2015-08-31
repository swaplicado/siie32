/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import erp.lib.SLibUtilities;
import java.awt.Color;
import java.awt.font.TextAttribute;
import java.util.Map;
import javax.swing.JLabel;

/**
 *
 * @author Sergio Flores
 */
public class STableCellRendererNumber extends javax.swing.table.DefaultTableCellRenderer {
    
    private java.text.NumberFormat moNumberFormat;
    private javax.swing.JLabel moLabel;
    
    public STableCellRendererNumber(java.text.NumberFormat numberFormat) {
        moNumberFormat = numberFormat;
        moLabel = new JLabel();
        moLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        moLabel.setOpaque(true);
    }
    
    public void setNumberFormat(java.text.NumberFormat o) { moNumberFormat = o; }
    public void setLabel(javax.swing.JLabel o) { moLabel = o; }
    
    public java.text.NumberFormat getNumberFormat() { return moNumberFormat; }
    public javax.swing.JLabel getLabel() { return moLabel; }
    
    @Override
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        int style = ((STableModel) table.getModel()).getTableRow(row).getStyle();
        double dNumber = 0;
        java.lang.String sNumber = "?";
        Map attributes = moLabel.getFont().getAttributes();
        
        if (value == null) {
            sNumber = table.isCellEditable(row, col) ? "0" : "NaN";
            value = new Integer(0);
        }
        else {
            try {
                dNumber = ((Number) value).doubleValue();
                sNumber = moNumberFormat.format(dNumber);
            }
            catch (java.lang.Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
        
        moLabel.setText(sNumber);
        
        if (isSelected) {
            if (table.isCellEditable(row, col)) {
                moLabel.setForeground(dNumber < 0 ? STableConstants.FOREGROUND_EDIT_NEG : STableConstants.FOREGROUND_EDIT);
                moLabel.setBackground(hasFocus ? STableConstants.BACKGROUND_SELECT_EDIT_FOCUS : STableConstants.BACKGROUND_SELECT_EDIT);
            }
            else {
                moLabel.setForeground(dNumber < 0 ? STableConstants.FOREGROUND_READ_NEG : STableConstants.FOREGROUND_READ);
                moLabel.setBackground(hasFocus ? STableConstants.BACKGROUND_SELECT_READ_FOCUS : STableConstants.BACKGROUND_SELECT_READ);
            }
        }
        else {
            if (table.isCellEditable(row, col)) {
                moLabel.setForeground(dNumber < 0 ? STableConstants.FOREGROUND_EDIT_NEG : STableConstants.FOREGROUND_EDIT);
                moLabel.setBackground(STableConstants.BACKGROUND_PLAIN_EDIT);
            }
            else {
                moLabel.setForeground(dNumber < 0 ? STableConstants.FOREGROUND_READ_NEG : STableConstants.FOREGROUND_READ);
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
