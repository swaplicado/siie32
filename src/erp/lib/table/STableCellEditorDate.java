/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.form.SFormField;

/**
 *
 * @author Juan Barajas
 */
public class STableCellEditorDate extends AbstractCellEditor  implements TableCellEditor, KeyListener{

    private erp.lib.form.SFormField moFieldDate;
    private java.text.SimpleDateFormat moSimpleDateFormat;
    private JFormattedTextField jftDate;

    public STableCellEditorDate(erp.client.SClientInterface client) {
        switch (client.getSessionXXX().getParamsErp().getFkFormatDateTypeId()) {
            case SDataConstantsSys.CFGS_TP_FMT_D_YYYY_MM_DD:
                moSimpleDateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_D_DD_MM_YYYY:
                moSimpleDateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_D_MM_DD_YYYY:
                 moSimpleDateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
                break;
            default:
                client.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        jftDate = new JFormattedTextField(moSimpleDateFormat);
        moFieldDate = new SFormField(client, SLibConstants.DATA_TYPE_DATE, false, jftDate, new JLabel());

        jftDate.addKeyListener(this);
    }

    @Override
    public Object getCellEditorValue() {
        return moFieldDate.getDate();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
        java.lang.String sDate = "?";
        jftDate.setValue(null);

        if (value != null) {
            sDate = moSimpleDateFormat.format((Date) value);
        }

        jftDate.setText(sDate);

        if (table.isCellEditable(row, col)) {
            jftDate.setForeground(STableConstants.FOREGROUND_EDIT);
            jftDate.setBackground(STableConstants.BACKGROUND_PLAIN_EDIT);
        }
        else {
            jftDate.setForeground(STableConstants.FOREGROUND_READ);
            jftDate.setBackground(STableConstants.BACKGROUND_PLAIN_READ);
        }

        return jftDate;
    }

    @Override
    public boolean isCellEditable(EventObject evt) {
        if (evt instanceof MouseEvent) {
            return ((MouseEvent)evt).getClickCount() >= 2;
        }

        return true;
    }

    @Override
    public void keyReleased(KeyEvent evt) {

    }

    @Override
    public void keyTyped(KeyEvent evt) {

    }

    @Override
    public void keyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            fireEditingStopped();
        }
    }
}
