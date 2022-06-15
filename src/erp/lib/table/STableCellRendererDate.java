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
 * @author  Sergio Flores
 */
public class STableCellRendererDate extends javax.swing.table.DefaultTableCellRenderer {
    
    private java.text.SimpleDateFormat moSimpleDateFormat;
    private javax.swing.JLabel moLabel;
    
    public STableCellRendererDate(java.text.SimpleDateFormat simpleDateFormat) {
        moSimpleDateFormat = simpleDateFormat;
        moLabel = new JLabel();
        moLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        moLabel.setOpaque(true);
    }
    
    public void setSimpleDateFormat(java.text.SimpleDateFormat o) { moSimpleDateFormat = o; }
    public void setLabel(javax.swing.JLabel o) { moLabel = o; }
    
    public java.text.SimpleDateFormat getSimpleDateFormat() { return moSimpleDateFormat; }
    public javax.swing.JLabel getLabel() { return moLabel; }
    
    @Override
    @SuppressWarnings("unchecked")
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        int style = ((STableModel) table.getModel()).getTableRow(row).getStyle();
        java.util.Date tDate = null;
        java.lang.String sDate = "?";
        Map attributes = moLabel.getFont().getAttributes();
        
        try {
            if (value != null) {
                tDate = (java.util.Date) value;
                sDate = moSimpleDateFormat.format(tDate);
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }
        
        moLabel.setText(sDate);

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
