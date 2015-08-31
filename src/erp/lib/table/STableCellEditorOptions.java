/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import sa.lib.gui.SGuiItem;

/**
 *
 * @author Juan Barajas
 */
public class STableCellEditorOptions extends AbstractCellEditor implements TableCellEditor, KeyListener {

    private JComboBox mjcbOptions;
    List<ArrayList<SGuiItem>> mltAccountmltAccount = null;

    public STableCellEditorOptions(erp.client.SClientInterface client) {
        mjcbOptions = new JComboBox();
    }
    
    public void setAccounts(List<ArrayList<SGuiItem>> ltAccount) {        
        this.mltAccountmltAccount = ltAccount;
    }
    
    @Override
    public Object getCellEditorValue() {
        return mjcbOptions.getSelectedItem();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
        ArrayList<SGuiItem> items = mltAccountmltAccount.get(row);

        mjcbOptions.removeAllItems();
        for (SGuiItem item : items) {
            mjcbOptions.addItem(item);
        }
        
        mjcbOptions.setSelectedItem(value);

        if (table.isCellEditable(row, col)) {
            mjcbOptions.setForeground(STableConstants.FOREGROUND_EDIT);
            mjcbOptions.setBackground(STableConstants.BACKGROUND_PLAIN_EDIT);
        }
        else {
            mjcbOptions.setForeground(STableConstants.FOREGROUND_READ);
            mjcbOptions.setBackground(STableConstants.BACKGROUND_PLAIN_READ);
        }

        return mjcbOptions;
    }
    
    @Override
    public boolean isCellEditable(EventObject evt) {
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
