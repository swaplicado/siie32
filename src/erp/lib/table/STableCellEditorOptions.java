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
 * @author Juan Barajas, Alfredo Pérez
 */
public class STableCellEditorOptions extends AbstractCellEditor implements TableCellEditor, KeyListener {

    private JComboBox mjcbOptions;
    public List<ArrayList<SGuiItem>> mlElementsmltElements = null;

    public STableCellEditorOptions(erp.client.SClientInterface client) {
        this(client, false);
    }
    
    public STableCellEditorOptions(erp.client.SClientInterface client, boolean edit) {
        mjcbOptions = new JComboBox();
        mjcbOptions.setEditable(edit);
    }
    
    public void setElements(List<ArrayList<SGuiItem>> ltAccount) {        
        this.mlElementsmltElements = ltAccount;
    }
    
    @Override
    public Object getCellEditorValue() {
        return mjcbOptions.getSelectedItem();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
        if ( mlElementsmltElements == null || mlElementsmltElements.isEmpty() || mlElementsmltElements.size() < 1){
            System.out.println("'mlElementsmltElements':Esta nulo, vacio o con size <= 0.");
        }
        else {
            if (mlElementsmltElements.get(row)== null){
                ArrayList<SGuiItem> empty = new ArrayList<>();
                empty.add(new SGuiItem(""));
                mlElementsmltElements.set(row, empty);
            }
            ArrayList<SGuiItem> items = mlElementsmltElements.get(row);
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
