/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import erp.lib.SLibUtilities;
import javax.swing.JLabel;

/**
 *
 * @author Sergio Flores
 */
public class STableCellRendererBoolean extends javax.swing.table.DefaultTableCellRenderer {

    private javax.swing.ImageIcon moIconOn = new javax.swing.ImageIcon(getClass().getResource("/erp/img/view_bool_on.gif"));
    private javax.swing.ImageIcon moIconOff = new javax.swing.ImageIcon(getClass().getResource("/erp/img/view_bool_off.gif"));
    private javax.swing.JLabel moLabel;

    public STableCellRendererBoolean() {
        moLabel = new JLabel();
        moLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        moLabel.setOpaque(true);
    }

    public void setLabel(javax.swing.JLabel o) { moLabel = o; }

    public javax.swing.JLabel getLabel() { return moLabel; }

    @Override
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        javax.swing.ImageIcon icon = moIconOff;

        if (value != null) {
            try {
                if (((java.lang.Boolean) value).booleanValue()) {
                    icon = moIconOn;
                }
            }
            catch (java.lang.Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }

        moLabel.setIcon(icon);
        moLabel.setAlignmentY(javax.swing.SwingConstants.CENTER);

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

        return moLabel;
    }
}
