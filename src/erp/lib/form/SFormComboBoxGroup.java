/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.form;

import erp.lib.SLibUtilities;
import java.awt.event.ItemEvent;
import java.util.Vector;
import javax.swing.JComboBox;

/**
 *
 * @author Sergio Flores
 */
public class SFormComboBoxGroup implements java.awt.event.ItemListener {

    private boolean mbAdjustingComboBoxes;
    private erp.client.SClientInterface miClient;
    private java.util.Vector<javax.swing.JComboBox> mvComboBoxes;
    private java.util.Vector<javax.swing.JButton> mvButtons;
    private java.util.Vector<java.lang.Integer> mvDataTypes;
    private java.util.Vector<java.util.Vector<erp.lib.form.SFormComponentItem>> mvComponentItemVectors;

    public SFormComboBoxGroup(erp.client.SClientInterface client) {
        miClient = client;
        mvComboBoxes = new Vector<>();
        mvButtons = new Vector<>();
        mvDataTypes = new Vector<>();
        mvComponentItemVectors = new Vector<>();
        clear();
    }

    @SuppressWarnings("unchecked")
    private void adjustComboBoxes(java.awt.event.ItemEvent event) {
        int i = 0;
        int j = 0;
        Object pk = null;
        Vector<SFormComponentItem> childComponentItems = null;
        JComboBox<SFormComponentItem> comboBox = null;
        JComboBox<SFormComponentItem> comboBoxChild = null;
        SFormComponentItem item = null;

        for (i = 0; i < mvComboBoxes.size() - 1; i++) {     // last combo box does not have child combo boxes
            comboBox = mvComboBoxes.get(i);
            if (comboBox == event.getSource()) {
                for (j = i + 1; j < mvComboBoxes.size(); j++) {
                    comboBoxChild = mvComboBoxes.get(j);
                    comboBoxChild.removeAllItems();
                    comboBoxChild.setEnabled(false);

                    if (mvButtons.size() == mvComboBoxes.size()) {
                        mvButtons.get(j).setEnabled(false);
                    }
                }

                if (comboBox.getSelectedIndex() > 0) {
                    childComponentItems = mvComponentItemVectors.get(i + 1);
                    comboBoxChild = mvComboBoxes.get(i + 1);
                    comboBoxChild.setEnabled(true);
                    comboBoxChild.addItem(childComponentItems.get(0));
                    comboBoxChild.setSelectedIndex(0);

                    pk = ((SFormComponentItem) comboBox.getSelectedItem()).getPrimaryKey();
                    for (j = 1; j < childComponentItems.size(); j++) {   // first item allready added
                        item = childComponentItems.get(j);
                        if (SLibUtilities.compareKeys(pk, item.getForeignKey())) {
                            comboBoxChild.addItem(childComponentItems.get(j));
                        }
                    }

                    if (mvButtons.size() == mvComboBoxes.size()) {
                        mvButtons.get(i + 1).setEnabled(true);
                    }
                }

                break;
            }
        }
    }

    public void clear() {
        mbAdjustingComboBoxes = false;
        mvComboBoxes.clear();
        mvButtons.clear();
        mvDataTypes.clear();
        mvComponentItemVectors.clear();
    }

    /**
     * @param comboBox Combo box control.
     * @param dataType Constant defined in erp.data.SDataConstants.
     */
    public void addComboBox(int dataType, javax.swing.JComboBox comboBox) {
        addComboBox(dataType, comboBox, null, null);
    }

    /**
     * @param comboBox Combo box control.
     * @param dataType Constant defined in erp.data.SDataConstants.
     */
    public void addComboBox(int dataType, javax.swing.JComboBox comboBox, java.lang.Object filterPk) {
        addComboBox(dataType, comboBox, null, filterPk);
    }

    /**
     * @param comboBox Combo box control.
     * @param dataType Constant defined in erp.data.SDataConstants.
     */
    public void addComboBox(int dataType, javax.swing.JComboBox comboBox, javax.swing.JButton button) {
        addComboBox(dataType, comboBox, button, null);
    }

    /**
     * @param comboBox Combo box control.
     * @param dataType Constant defined in erp.data.SDataConstants.
     * @param button
     * @param filterPk
     */
    @SuppressWarnings("unchecked")
    public void addComboBox(int dataType, javax.swing.JComboBox comboBox, javax.swing.JButton button, java.lang.Object filterPk) {
        Vector<SFormComponentItem> vector = new Vector<>();

        SFormUtilities.populateComboBox(miClient, comboBox, dataType, filterPk);

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            vector.add((SFormComponentItem) comboBox.getItemAt(i));
        }

        mvComboBoxes.add(comboBox);
        mvDataTypes.add(dataType);
        mvComponentItemVectors.add(vector);
        comboBox.addItemListener(this);

        if (button != null) {
            mvButtons.add(button);
        }
    }

    public void reset() {
        JComboBox comboBox = null;

        if (mvComboBoxes.size() > 0) {
            comboBox = mvComboBoxes.get(0);
            if (comboBox.getSelectedIndex() != 0) {
                comboBox.setSelectedIndex(0);
            }
            else {
                adjustComboBoxes(new ItemEvent(comboBox, 0, comboBox.getSelectedItem(), ItemEvent.SELECTED));
            }
        }
    }

    @Override
    public void itemStateChanged(java.awt.event.ItemEvent event) {
        if (!mbAdjustingComboBoxes && event.getStateChange() == ItemEvent.SELECTED) {
            mbAdjustingComboBoxes = true;
            adjustComboBoxes(event);
            mbAdjustingComboBoxes = false;
        }
    }
}
