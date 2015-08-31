/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import erp.lib.SLibUtilities;

/**
 *
 * @author Sergio Flores
 */
public final class STableToolTipHeader extends javax.swing.table.JTableHeader {

    private java.lang.String[] masToolTips;

    public STableToolTipHeader(javax.swing.table.TableColumnModel tableColumnModel, java.lang.String[] toolTips) {
        super(tableColumnModel);
        masToolTips = toolTips;
    }

    public void setToolTips(java.lang.String[] as) { masToolTips = as; }

    public java.lang.String[] getToolTips() { return masToolTips; }

    @Override
    public java.lang.String getToolTipText(java.awt.event.MouseEvent event) {
        int col = 0;
        int colModel = 0;
        String toolTip = "";

        col = columnAtPoint(event.getPoint());

        if (col != -1 && col < getColumnModel().getColumnCount()) {
            colModel = getTable().convertColumnIndexToModel(col);
            try {
                toolTip = masToolTips[colModel];
            }
            catch (NullPointerException e) {
                SLibUtilities.printOutException(this, e);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                SLibUtilities.printOutException(this, e);
            }
        }
        
        return toolTip.length() == 0 ? null : toolTip;
    }
}
