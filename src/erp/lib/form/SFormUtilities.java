/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.lib.form;

import erp.lib.SLibUtilities;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class SFormUtilities {

    /**
     * @param maskErp The following characters can be specified:
     *  9   Any number (Character.isDigit).
     *  A   Any character (Character.isLetter).
     *  X   Any character or number (Character.isLetter or Character.isDigit).
     *  -   Special character '-'.
     *  /   Special character '/'.
     * @return 
     */
    public static java.lang.String createMaskFormatter(java.lang.String maskErp) {
        String mask = SLibUtilities.textTrim(maskErp);

        mask = mask.replaceAll("9", "#");
        mask = mask.replaceAll("A", "?");
        mask = mask.replaceAll("X", "A");

        return mask;
    }

    /**
     * @param maskErp The following characters can be specified:
     *  y   Any number (Character.isDigit).
     *  M   Any number (Character.isDigit).
     *  d   Any number (Character.isDigit).
     *  H   Any number (Character.isDigit).
     *  m   Any number (Character.isDigit).
     *  s   Any number (Character.isDigit).
     *  a   AM or PM (Character.isLetter uppercase).
     * @return 
     */
    public static java.lang.String createMaskFormatterDatetime(java.lang.String maskErp) {
        String mask = SLibUtilities.textTrim(maskErp);

        mask = mask.replaceAll("y", "#");
        mask = mask.replaceAll("M", "#");
        mask = mask.replaceAll("d", "#");
        mask = mask.replaceAll("H", "#");
        mask = mask.replaceAll("m", "#");
        mask = mask.replaceAll("s", "#");
        mask = mask.replaceAll("a", "LL");

        return mask;
    }

    /**
     * Maps provided action in root pane.
     *
     * @param rootPane Root pane.
     * @param action Action to be mapped. Must implement actionPerformed(ActionEvent e) method.
     * @param map Maps' name.
     * @param keyCode Key code.
     * @param modifiers Key code's modifiers.
     */
    public static void putActionMap(javax.swing.JRootPane rootPane, javax.swing.AbstractAction action, java.lang.String map, int keyCode, int modifiers) {
        rootPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(keyCode, modifiers), map);
        rootPane.getActionMap().put(map, action);
    }

    /**
     * Creates and maps action in root pane.
     *
     * @param rootPane Root pane.
     * @param target Target object that will be used for method invocation.
     * @param method Method's name.
     * @param map Map's name.
     * @param keyCode Key code.
     * @param modifiers Key code's modifiers.
     */
    public static void createActionMap(final javax.swing.JRootPane rootPane, final java.lang.Object target, final java.lang.String method, final java.lang.String map, final int keyCode, final int modifiers) {
        createActionMap(rootPane, target, method, null, null, map, keyCode, modifiers);
    }

    /**
     * Creates and maps action in root pane.
     *
     * @param rootPane Root pane.
     * @param target Target object that will be used for method invocation.
     * @param method Method's name.
     * @param argClass Argument class (when not needed provide null).
     * @param arg Argument (when not needed provide null).
     * @param map Map's name.
     * @param keyCode Key code.
     * @param modifiers Key code's modifiers.
     */
    public static void createActionMap(final javax.swing.JRootPane rootPane, final java.lang.Object target, final java.lang.String method, final java.lang.Class<?> argClass, final java.lang.Object arg, final java.lang.String map, final int keyCode, final int modifiers) {
        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    if (argClass == null) {
                        target.getClass().getMethod(method).invoke(target);
                    }
                    else {
                        target.getClass().getMethod(method, argClass).invoke(target, arg);
                    }
                }
                catch (NoSuchMethodException exc) {
                    SLibUtilities.printOutException(this.getClass().getName(), exc);
                }
                catch (SecurityException exc) {
                    SLibUtilities.printOutException(this.getClass().getName(), exc);
                }
                catch (IllegalAccessException exc) {
                    SLibUtilities.printOutException(this.getClass().getName(), exc);
                }
                catch (IllegalArgumentException exc) {
                    SLibUtilities.printOutException(this.getClass().getName(), exc);
                }
                catch (InvocationTargetException exc) {
                    SLibUtilities.printOutException(this.getClass().getName(), exc);
                }
                catch (Exception exc) {
                    SLibUtilities.printOutException(this.getClass().getName(), exc);
                }
            }
        };

        putActionMap(rootPane, action, map, keyCode, modifiers);
    }

    /**
     * Maps provided action in panel.
     *
     * @param panel Panel.
     * @param action Action to be mapped. Must implement actionPerformed(ActionEvent e) method.
     * @param map Map's name.
     * @param keyCode Key code.
     * @param modifiers Key code's modifiers.
     */
    public static void putActionMap(javax.swing.JPanel panel, javax.swing.AbstractAction action, java.lang.String map, int keyCode, int modifiers) {
        panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(keyCode, modifiers), map);
        panel.getActionMap().put(map, action);
    }

    /**
     * Creates and maps action in panel.
     *
     * @param panel Panel.
     * @param target Target object that will be used for method invocation.
     * @param method Method's name.
     * @param map Map's name.
     * @param keyCode Key code.
     * @param modifiers Key code's modifiers.
     */
    public static void createActionMap(final javax.swing.JPanel panel, final java.lang.Object target, final java.lang.String method, final java.lang.String map, final int keyCode, final int modifiers) {
        createActionMap(panel, target, method, null, null, map, keyCode, modifiers);
    }

    /**
     * Creates and maps action in panel.
     *
     * @param panel Panel.
     * @param target Target object that will be used for method invocation.
     * @param method Method's name.
     * @param argClass Argument class (when not needed provide null).
     * @param arg Argument (when not needed provide null).
     * @param map Map's name.
     * @param keyCode Key code.
     * @param modifiers Key code's modifiers.
     */
    public static void createActionMap(final javax.swing.JPanel panel, final java.lang.Object target, final java.lang.String method, final java.lang.Class<?> argClass, final java.lang.Object arg, final java.lang.String map, final int keyCode, final int modifiers) {
        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    if (argClass == null) {
                        target.getClass().getMethod(method).invoke(target);
                    }
                    else {
                        target.getClass().getMethod(method, argClass).invoke(target, arg);
                    }
                }
                catch (NoSuchMethodException exc) {
                    SLibUtilities.printOutException(this.getClass().getName(), exc);
                }
                catch (SecurityException exc) {
                    SLibUtilities.printOutException(this.getClass().getName(), exc);
                }
                catch (IllegalAccessException exc) {
                    SLibUtilities.printOutException(this.getClass().getName(), exc);
                }
                catch (IllegalArgumentException exc) {
                    SLibUtilities.printOutException(this.getClass().getName(), exc);
                }
                catch (InvocationTargetException exc) {
                    SLibUtilities.printOutException(this.getClass().getName(), exc);
                }
                catch (Exception exc) {
                    SLibUtilities.printOutException(this.getClass().getName(), exc);
                }
            }
        };

        putActionMap(panel, action, map, keyCode, modifiers);
    }

    /**
     * @param client Client interface.
     * @param comboBox Visual control (javax.swing.JComboBox object).
     * @param dataType Constant defined in erp.data.SDataConstants.
     */
    public static void populateComboBox(erp.client.SClientInterface client, javax.swing.JComboBox<SFormComponentItem> comboBox, int dataType) {
        populateComboBox(client, comboBox, dataType, null);
    }

    /**
     * @param client Client interface.
     * @param comboBox Visual control (javax.swing.JComboBox object).
     * @param dataType Constant defined in erp.data.SDataConstants.
     * @param filterPk Primary key to be used as filter in combo box.
     */
    @SuppressWarnings("unchecked")
    public static void populateComboBox(erp.client.SClientInterface client, javax.swing.JComboBox<SFormComponentItem> comboBox, int dataType, java.lang.Object filterPk) {
        SServerRequest request = null;
        SServerResponse response = null;
        Vector<SFormComponentItem> items = null;

        try {
            request = new SServerRequest(SServerConstants.REQ_COMP_ITEMS_COMBO_BOX);
            request.setRegistryType(dataType);
            request.setPrimaryKey(filterPk);
            response = client.getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                throw new Exception(response.getMessage());
            }
            else {
                items = (Vector<SFormComponentItem>) response.getPacket();
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFormUtilities.class.getName(), e);
        }

        if (items != null) {
            comboBox.removeAllItems();
            for (int i = 0; i < items.size(); i++) {
                comboBox.addItem(items.get(i));
            }
        }

        if (comboBox.getItemCount() > 0) {
            comboBox.setSelectedIndex(0);
        }
    }

    public static void locateComboBoxItem(javax.swing.JComboBox comboBox, java.lang.Object pk) {
        boolean located = false;

        if (comboBox.getItemCount() > 0) {
            for (int i = 0; i < comboBox.getItemCount(); i++) {
                SFormComponentItem item = (SFormComponentItem) comboBox.getItemAt(i);

                if (SLibUtilities.compareKeys(pk, item.getPrimaryKey())) {
                    comboBox.setSelectedIndex(i);
                    located = true;
                    break;
                }
            }

            if (!located) {
                comboBox.setSelectedIndex(0);
            }
        }
    }

    public static void locateComboBoxItem(javax.swing.JComboBox comboBox, java.lang.String text) {
        boolean located = false;

        if (comboBox.getItemCount() > 0) {
            for (int i = 0; i < comboBox.getItemCount(); i++) {
                SFormComponentItem item = (SFormComponentItem) comboBox.getItemAt(i);

                if (text.compareTo(item.getItem()) == 0) {
                    comboBox.setSelectedIndex(i);
                    located = true;
                    break;
                }
            }

            if (!located) {
                comboBox.setSelectedIndex(0);
            }
        }
    }

    public static void locateComboBoxItemByComplement(javax.swing.JComboBox comboBox, java.lang.Object complement) {
        boolean located = false;

        if (comboBox.getItemCount() > 0) {
            if (complement instanceof Integer) {
                int value = ((Integer) complement).intValue();

                for (int i = 0; i < comboBox.getItemCount(); i++) {
                    SFormComponentItem item = (SFormComponentItem) comboBox.getItemAt(i);

                    if (item.getComplement() != null && item.getComplement() instanceof Integer) {
                        if (value == ((Integer) item.getComplement()).intValue()) {
                            comboBox.setSelectedIndex(i);
                            located = true;
                            break;
                        }
                    }
                }
            }
            else if (complement instanceof Double) {
                double value = ((Double) complement).doubleValue();

                for (int i = 0; i < comboBox.getItemCount(); i++) {
                    SFormComponentItem item = (SFormComponentItem) comboBox.getItemAt(i);

                    if (item.getComplement() != null && item.getComplement() instanceof Double) {
                        if (value == ((Double) item.getComplement()).doubleValue()) {
                            comboBox.setSelectedIndex(i);
                            located = true;
                            break;
                        }
                    }
                }
            }
            else if (complement instanceof Boolean) {
                boolean value = (Boolean) complement;

                for (int i = 0; i < comboBox.getItemCount(); i++) {
                    SFormComponentItem item = (SFormComponentItem) comboBox.getItemAt(i);

                    if (item.getComplement() != null && item.getComplement() instanceof Boolean) {
                        if (value == (Boolean) item.getComplement()) {
                            comboBox.setSelectedIndex(i);
                            located = true;
                            break;
                        }
                    }
                }
            }
            else if (complement instanceof String) {
                String value = (String) complement;

                for (int i = 0; i < comboBox.getItemCount(); i++) {
                    SFormComponentItem item = (SFormComponentItem) comboBox.getItemAt(i);

                    if (item.getComplement() != null && item.getComplement() instanceof String) {
                        if (value.compareTo((String) item.getComplement()) == 0) {
                            comboBox.setSelectedIndex(i);
                            located = true;
                            break;
                        }
                    }
                }
            }
            else if (complement instanceof java.util.Date) {
                java.util.Date value = (java.util.Date) complement;

                for (int i = 0; i < comboBox.getItemCount(); i++) {
                    SFormComponentItem item = (SFormComponentItem) comboBox.getItemAt(i);

                    if (item.getComplement() != null && item.getComplement() instanceof java.util.Date) {
                        if (value.getTime() == ((java.util.Date) item.getComplement()).getTime()) {
                            comboBox.setSelectedIndex(i);
                            located = true;
                            break;
                        }
                    }
                }
            }

            if (!located) {
                comboBox.setSelectedIndex(0);
            }
        }
    }

    /**
     * @param client Client interface.
     * @param list Visual control (javax.swing.JList object).
     * @param dataType Constant defined in erp.data.SDataConstants.
     */
    public static void populateList(erp.client.SClientInterface client, javax.swing.JList list, int dataType) {
        populateList(client, list, dataType, null);
    }

    /**
     * @param client Client interface.
     * @param list Visual control (javax.swing.JList object).
     * @param dataType Constant defined in erp.data.SDataConstants.
     * @param filterPk Primary key to be used as filter in list.
     */
    @SuppressWarnings("unchecked")
    public static void populateList(erp.client.SClientInterface client, javax.swing.JList<SFormComponentItem> list, int dataType, java.lang.Object filterPk) {
        SServerRequest request = null;
        SServerResponse response = null;
        Vector<SFormComponentItem> items = null;

        try {
            request = new SServerRequest(SServerConstants.REQ_COMP_ITEMS_LIST);
            request.setRegistryType(dataType);
            request.setPrimaryKey(filterPk);
            response = client.getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                throw new Exception(response.getMessage());
            }
            else {
                items = (Vector<SFormComponentItem>) response.getPacket();
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFormUtilities.class.getName(), e);
        }

        if (items != null) {
            list.setListData(items);
        }

        if (list.getModel().getSize() > 0) {
            list.setSelectedIndex(0);
        }
    }

    public static void locateListItem(javax.swing.JList list, java.lang.Object pk) {
        boolean located = false;

        if (list.getModel().getSize() > 0) {
            for (int i = 0; i < list.getModel().getSize(); i++) {
                SFormComponentItem item = (SFormComponentItem) list.getModel().getElementAt(i);

                if (SLibUtilities.compareKeys(pk, item.getPrimaryKey())) {
                    list.setSelectedIndex(i);
                    located = true;
                    break;
                }
            }

            if (!located) {
                list.setSelectedIndex(-1);
            }
        }
    }

    public static void locateListItem(javax.swing.JList list, java.lang.String text) {
        boolean located = false;

        if (list.getModel().getSize() > 0) {
            for (int i = 0; i < list.getModel().getSize(); i++) {
                SFormComponentItem item = (SFormComponentItem) list.getModel().getElementAt(i);

                if (text.compareTo(item.getItem()) == 0) {
                    list.setSelectedIndex(i);
                    located = true;
                    break;
                }
            }

            if (!located) {
                list.setSelectedIndex(-1);
            }
        }
    }

    public static void implementMaskFormatter(javax.swing.JFormattedTextField ftf, java.lang.String mask) {
        try {
            ftf.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter(mask)));
        }
        catch (java.text.ParseException e) {
            SLibUtilities.renderException(SFormUtilities.class.getName(), e);
        }
    }

    public static java.util.Vector<erp.lib.form.SFormComponentItem> removeListItems(javax.swing.JList<SFormComponentItem> list, int indexBegin, int indexEnd) {
        int i = 0;
        Vector<SFormComponentItem> vRemovedItems = new Vector<SFormComponentItem>();
        Vector<SFormComponentItem> vListItems = new Vector<SFormComponentItem>();

        for (i = 0; i < list.getModel().getSize(); i++) {
            vListItems.add((SFormComponentItem) list.getModel().getElementAt(i));
        }

        for (i = indexBegin; i <= indexEnd; i++) {
            vRemovedItems.add(vListItems.remove(indexBegin));
        }

        list.setListData(vListItems);

        return vRemovedItems;
    }

    public static erp.lib.form.SFormComponentItem removeListSelectedItem(javax.swing.JList list) {
        Vector<SFormComponentItem> vRemovedItems = new Vector<SFormComponentItem>();

        if (list.getSelectedIndex() != -1) {
            vRemovedItems = removeListItems(list, list.getSelectedIndex(), list.getSelectedIndex());
        }

        return vRemovedItems.size() == 0 ? null : vRemovedItems.get(0);
    }

    public static java.util.Vector<erp.lib.form.SFormComponentItem> removeListAllItems(javax.swing.JList list) {
        return removeListItems(list, 0, list.getModel().getSize() - 1);
    }

    public static void addListItem(javax.swing.JList<SFormComponentItem> list, erp.lib.form.SFormComponentItem item) {
        Vector<SFormComponentItem> listItems = new Vector<SFormComponentItem>();

        for (int i = 0; i < list.getModel().getSize(); i++) {
            listItems.add((SFormComponentItem) list.getModel().getElementAt(i));
        }

        listItems.add(item);
        list.setListData(listItems);
    }

    public static void addListItems(javax.swing.JList<SFormComponentItem> list, java.util.Vector<erp.lib.form.SFormComponentItem> listData) {
        Vector<SFormComponentItem> listItems = new Vector<SFormComponentItem>();

        for (int i = 0; i < list.getModel().getSize(); i++) {
            listItems.add((SFormComponentItem) list.getModel().getElementAt(i));
        }

        listItems.addAll(listData);
        list.setListData(listItems);
    }
}
