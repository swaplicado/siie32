/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.form;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import sa.lib.gui.SGuiUtils;

/**
 *
 * @author Sergio Flores
 */
public class SFormField implements SFormFieldInterface {

    private erp.client.SClientInterface miClient;
    private int mnDataType;
    private boolean mbIsMandatory;
    private javax.swing.JComponent mjComponent;

    private int mnFieldType;
    private int mnOptionPickerType;
    private javax.swing.JLabel mjLabelName;
    private java.lang.String msFieldName;
    private java.lang.Object moRawValue;
    private java.lang.Object moDefaultValue;

    private long mlLongMin;
    private long mlLongMax;
    private double mdDoubleMin;
    private double mdDoubleMax;
    private boolean mbIsPercent;

    private int mnLengthMin;
    private int mnLengthMax;
    private int mnAutoCaseType;
    private boolean mbAutoTrim;
    private java.lang.String moMaskFormatter;

    private java.util.Date mtDateMin;
    private java.util.Date mtDateMax;

    private boolean mbMinInclusive;
    private boolean mbMaxInclusive;
    private boolean mbIsSelectionItemApplying;

    private int mnListValidationType;

    private int mnTabbedPaneIndex;
    private java.text.DecimalFormat moDecimalFormat;
    private javax.swing.JTabbedPane mjTabbedPane;
    private javax.swing.JButton mjPickerButton;

    public SFormField(erp.client.SClientInterface client, int dataType, boolean isMandatory, javax.swing.JCheckBox checkBox) {
        this(client, dataType, isMandatory, checkBox, null, checkBox.getText(), -1);
    }

    public SFormField(erp.client.SClientInterface client, int dataType, boolean isMandatory, javax.swing.JCheckBox checkBox, int tabIndex) {
        this(client, dataType, isMandatory, checkBox, null, checkBox.getText(), tabIndex);
    }

    public SFormField(erp.client.SClientInterface client, int dataType, boolean isMandatory, javax.swing.JComponent component, javax.swing.JLabel label) {
        this(client, dataType, isMandatory, component, label, "", -1);
    }

    public SFormField(erp.client.SClientInterface client, int dataType, boolean isMandatory, javax.swing.JComponent component, javax.swing.JLabel label, int tabIndex) {
        this(client, dataType, isMandatory, component, label, "", tabIndex);
    }

    public SFormField(erp.client.SClientInterface client, int dataType, boolean isMandatory, javax.swing.JComponent component, javax.swing.JCheckBox checkBox) {
        this(client, dataType, isMandatory, component, null, checkBox.getText(), -1);
    }

    public SFormField(erp.client.SClientInterface client, int dataType, boolean isMandatory, javax.swing.JComponent component, javax.swing.JCheckBox checkBox, int tabIndex) {
        this(client, dataType, isMandatory, component, null, checkBox.getText(), tabIndex);
    }

    /**
     * 
     * @param client GUI client.
     * @param dataType Data type. Valid options defined in SLibConstants.DATA_TYPE_....
     * @param isMandatory Is field mandatory?
     * @param component Field's <code>JComponent</code>.
     * @param label Field's <code>JLabel</code>, if any, otherwise <code>null</code>.
     * @param fieldName Field's name, if any, otherwise <code>""</code>.
     * @param tabIndex  Field's tab, if any, otherwise <code>-1</code>.
     */
    private SFormField(erp.client.SClientInterface client, int dataType, boolean isMandatory, javax.swing.JComponent component, javax.swing.JLabel label, java.lang.String fieldName, int tabIndex) {
        miClient = client;
        mnDataType = dataType;
        mbIsMandatory = isMandatory;
        mjComponent = component;

        mnFieldType = SLibConstants.UNDEFINED;
        mnOptionPickerType = SLibConstants.UNDEFINED;
        mjLabelName = label;
        msFieldName = SGuiUtils.getLabelName(fieldName);
        moRawValue = null;
        moDefaultValue = null;

        mlLongMin = 0;
        mlLongMax = 0;
        mdDoubleMin = 0;
        mdDoubleMax = 0;
        mbIsPercent = false;

        mnLengthMin = 0;
        mnLengthMax = 255;
        mnAutoCaseType = SLibConstants.CASE_UPPER;
        mbAutoTrim = true;
        moMaskFormatter = "";

        mtDateMin = null;
        mtDateMax = null;

        mbMinInclusive = true;
        mbMaxInclusive = true;
        mbIsSelectionItemApplying = true;

        mnListValidationType = SLibConstants.LIST_VALIDATION_BY_SELECTION;

        mnTabbedPaneIndex = tabIndex;
        moDecimalFormat = null;
        mjTabbedPane = null;
        mjPickerButton = null;

        switch (mnDataType) {
            case SLibConstants.DATA_TYPE_BOOLEAN:
                if (mjComponent instanceof javax.swing.JCheckBox) {
                    mnFieldType = SLibConstants.FIELD_TYPE_CHECK_BOX;
                }
                else {
                    miClient.showMsgBoxWarning("El control debe ser un objeto de la clase " +
                            javax.swing.JCheckBox.class.getName() + ".");
                }
                break;

            case SLibConstants.DATA_TYPE_INTEGER:
            case SLibConstants.DATA_TYPE_LONG:
            case SLibConstants.DATA_TYPE_FLOAT:
            case SLibConstants.DATA_TYPE_DOUBLE:
                if (mjComponent instanceof javax.swing.JTextField) {
                    mnFieldType = SLibConstants.FIELD_TYPE_TEXT;
                }
                else {
                    miClient.showMsgBoxWarning("El control debe ser un objeto de la clase " +
                            javax.swing.JTextField.class.getName() + ".");
                }
                break;

            case SLibConstants.DATA_TYPE_STRING:
                if (mjComponent instanceof javax.swing.JFormattedTextField) {
                    mnFieldType = SLibConstants.FIELD_TYPE_FORMATTED_TEXT;
                }
                else if (mjComponent instanceof javax.swing.JTextField) {
                    mnFieldType = SLibConstants.FIELD_TYPE_TEXT;
                }
                else if (mjComponent instanceof javax.swing.JTextArea) {
                    mnFieldType = SLibConstants.FIELD_TYPE_TEXT_AREA;
                }
                else if (mjComponent instanceof javax.swing.JComboBox && ((JComboBox) mjComponent).isEditable()) {
                    mnFieldType = SLibConstants.FIELD_TYPE_TEXT;
                }
                else {
                    miClient.showMsgBoxWarning("El control debe ser un objeto de la clase " +
                            javax.swing.JTextField.class.getName() + " o " +
                            javax.swing.JTextArea.class.getName() + " o " +
                            javax.swing.JFormattedTextField.class.getName() + " o " +
                            javax.swing.JComboBox.class.getName() + " (modificable).");
                }
                break;

            case SLibConstants.DATA_TYPE_DATE:
            case SLibConstants.DATA_TYPE_DATE_TIME:
            case SLibConstants.DATA_TYPE_TIME:
                if (mjComponent instanceof javax.swing.JFormattedTextField) {
                    mnFieldType = SLibConstants.FIELD_TYPE_FORMATTED_TEXT;
                }
                else {
                    miClient.showMsgBoxWarning("El control debe ser un objeto de la clase " +
                            javax.swing.JFormattedTextField.class.getName() + ".");
                }
                break;

            case SLibConstants.DATA_TYPE_KEY:
                if (mjComponent instanceof javax.swing.JList) {
                    mnFieldType = SLibConstants.FIELD_TYPE_LIST;
                }
                else if (mjComponent instanceof javax.swing.JComboBox) {
                    mnFieldType = SLibConstants.FIELD_TYPE_COMBO_BOX;
                }
                else {
                    miClient.showMsgBoxWarning("El control debe ser un objeto de la clase " +
                            javax.swing.JList.class.getName() + " o " +
                            javax.swing.JComboBox.class.getName() + ".");
                }
                break;

            default:
                miClient.showMsgBoxWarning("El tipo del campo es desconocido.");
        }

        switch (mnDataType) {
            case SLibConstants.DATA_TYPE_BOOLEAN:
                moDefaultValue = false;
                break;

            case SLibConstants.DATA_TYPE_INTEGER:
                moDefaultValue = 0;
                mlLongMin = 0;
                mlLongMax = Integer.MAX_VALUE;
                createFocusListenerNumber();
                break;

            case SLibConstants.DATA_TYPE_LONG:
                moDefaultValue = 0L;
                mlLongMin = 0L;
                mlLongMax = Long.MAX_VALUE;
                createFocusListenerNumber();
                break;

            case SLibConstants.DATA_TYPE_FLOAT:
                moDefaultValue = 0f;
                mdDoubleMin = 0f;
                mdDoubleMax = Float.MAX_VALUE;
                createFocusListenerNumber();
                break;

            case SLibConstants.DATA_TYPE_DOUBLE:
                moDefaultValue = 0d;
                mdDoubleMin = 0d;
                mdDoubleMax = Double.MAX_VALUE;
                createFocusListenerNumber();
                break;

            case SLibConstants.DATA_TYPE_STRING:
                moDefaultValue = "";
                if (mnFieldType == SLibConstants.FIELD_TYPE_FORMATTED_TEXT) {
                    createFocusListenerStringFormatted();
                }
                else {
                    createFocusListenerString();
                }
                break;

            case SLibConstants.DATA_TYPE_DATE:
                moDefaultValue = null;
                SFormUtilities.implementMaskFormatter((JFormattedTextField) mjComponent, SFormUtilities.createMaskFormatterDatetime(miClient.getSessionXXX().getFormatters().getDateFormat().toPattern()));
                createFocusListenerDatetime();
                break;

            case SLibConstants.DATA_TYPE_DATE_TIME:
                moDefaultValue = null;
                SFormUtilities.implementMaskFormatter((JFormattedTextField) mjComponent, SFormUtilities.createMaskFormatterDatetime(miClient.getSessionXXX().getFormatters().getDatetimeFormat().toPattern()));
                createFocusListenerDatetime();
                break;

            case SLibConstants.DATA_TYPE_TIME:
                moDefaultValue = null;
                SFormUtilities.implementMaskFormatter((JFormattedTextField) mjComponent, SFormUtilities.createMaskFormatterDatetime(miClient.getSessionXXX().getFormatters().getTimeFormat().toPattern()));
                createFocusListenerDatetime();
                break;

            case SLibConstants.DATA_TYPE_KEY:
                moDefaultValue = null;
                break;

            default:
        }

        switch (mnDataType) {
            case SLibConstants.DATA_TYPE_INTEGER:
            case SLibConstants.DATA_TYPE_LONG:
                moDecimalFormat = miClient.getSessionXXX().getFormatters().getNumberLongFormat();
                if (isMandatory) {
                    mbMinInclusive = false;
                }
                break;

            case SLibConstants.DATA_TYPE_FLOAT:
            case SLibConstants.DATA_TYPE_DOUBLE:
                moDecimalFormat = miClient.getSessionXXX().getFormatters().getNumberDoubleFormat();
                if (isMandatory) {
                    mbMinInclusive = false;
                }
                break;

            case SLibConstants.DATA_TYPE_STRING:
                if (isMandatory) {
                    mnLengthMin = 1;
                }
                break;

            default:
        }

        moRawValue = moDefaultValue;
    }

    private void createPickerLauncher() {
        java.awt.event.KeyListener[] listeners = mjComponent.getKeyListeners();

        for (int i = 0; i < listeners.length; i++) {
            mjComponent.removeKeyListener(listeners[i]);
        }

        mjComponent.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_F5) {
                    processPickerLauncher();
                }
            }
        });
    }

    private void createButtonDispatcher() {
        mjComponent.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_F5) {
                    if (mjPickerButton.isEnabled()) {
                        mjPickerButton.doClick();
                    }
                }
            }
        });
    }

    private void createKeyListener() {
        java.awt.event.KeyListener[] listeners = mjComponent.getKeyListeners();

        for (int i = 0; i < listeners.length; i++) {
            mjComponent.removeKeyListener(listeners[i]);
        }

        if (mnLengthMax > 0) {
            mjComponent.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    processKeyReleased((JTextComponent) mjComponent);
                }
            });
        }
    }

    private void createFocusListenerNumber() {
        mjComponent.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                processFocusGainedNumber();
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                processFocusLostNumber();
            }
        });
    }

    private void createFocusListenerString() {
        if (mjComponent instanceof JComboBox && ((JComboBox) mjComponent).isEditable()) {
            // Editable combo boxes have subcomponents, so:

            for (Component c : mjComponent.getComponents()) {
                c.addFocusListener(new java.awt.event.FocusAdapter() {
                    @Override
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        processFocusLostString();
                    }
                });
            }
        }
        else {
            mjComponent.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    processFocusLostString();
                }
            });
        }
    }

    private void createFocusListenerStringFormatted() {
        mjComponent.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                processFocusGainedStringFormatted();
            }
        });
    }

    private void createFocusListenerDatetime() {
        mjComponent.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                processFocusGainedDatetime();
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                processFocusLostDatetime();
            }
        });
    }

    private void processPickerLauncher() {
        SFormOptionPickerInterface picker = miClient.getOptionPicker(mnOptionPickerType);

        if (picker != null) {
            picker.formReset();
            picker.formRefreshOptionPane();
            picker.setFormVisible(true);

            if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                SFormUtilities.locateComboBoxItem((JComboBox) mjComponent, picker.getSelectedPrimaryKey());
            }
        }
    }

    private void processKeyReleased(javax.swing.text.JTextComponent textComponent) {
        if (textComponent.getText().length() > mnLengthMax) {
            int position = textComponent.getCaretPosition();
            textComponent.setText(textComponent.getText().substring(0, mnLengthMax));
            textComponent.setCaretPosition(position > mnLengthMax ? mnLengthMax : position);
        }
    }

    private String processString(final String stringToCompute) {
        String string = "";

        if (mbAutoTrim) {
            string = SLibUtilities.textTrim(stringToCompute);
        }

        switch (mnAutoCaseType) {
            case SLibConstants.CASE_LOWER:
                string = string.toLowerCase();
                break;
            case SLibConstants.CASE_UPPER:
                string = string.toUpperCase();
                break;
            default:
        }

        return string;
    }

    private void processFocusGainedNumber() {
        switch (mnDataType) {
            case SLibConstants.DATA_TYPE_INTEGER:
                if (SLibUtilities.parseInt(((JTextField) mjComponent).getText()) == 0) {
                    ((JTextField) mjComponent).setText("");
                }
                break;
            case SLibConstants.DATA_TYPE_LONG:
                if (SLibUtilities.parseLong(((JTextField) mjComponent).getText()) == 0) {
                    ((JTextField) mjComponent).setText("");
                }
                break;
            case SLibConstants.DATA_TYPE_FLOAT:
                if (SLibUtilities.parseFloat(((JTextField) mjComponent).getText()) == 0) {
                    ((JTextField) mjComponent).setText("");
                }
                break;
            case SLibConstants.DATA_TYPE_DOUBLE:
                if (SLibUtilities.parseDouble(((JTextField) mjComponent).getText()) == 0) {
                    ((JTextField) mjComponent).setText("");
                }
                break;
            default:
        }
    }

    private void processFocusLostNumber() {
        if (moDecimalFormat != null) {
            switch (mnDataType) {
                case SLibConstants.DATA_TYPE_INTEGER:
                    ((JTextField) mjComponent).setText(moDecimalFormat.format(SLibUtilities.parseInt(((JTextField) mjComponent).getText())));
                    break;
                case SLibConstants.DATA_TYPE_LONG:
                    ((JTextField) mjComponent).setText(moDecimalFormat.format(SLibUtilities.parseLong(((JTextField) mjComponent).getText())));
                    break;
                case SLibConstants.DATA_TYPE_FLOAT:
                    ((JTextField) mjComponent).setText(moDecimalFormat.format(SLibUtilities.parseFloat(((JTextField) mjComponent).getText()) / (mbIsPercent ? 100f : 1f)));
                    break;
                case SLibConstants.DATA_TYPE_DOUBLE:
                    ((JTextField) mjComponent).setText(moDecimalFormat.format(SLibUtilities.parseDouble(((JTextField) mjComponent).getText()) / (mbIsPercent ? 100d : 1d)));
                    break;
                default:
            }
        }
    }

    private void processFocusLostString() {
        if (mjComponent instanceof javax.swing.JComboBox) {
            ((JComboBox) mjComponent).setSelectedItem(processString(((JComboBox) mjComponent).getSelectedItem() == null ? "" : ((JComboBox) mjComponent).getSelectedItem().toString()));
        }
        else {
            ((JTextComponent) mjComponent).setText(processString(((JTextComponent) mjComponent).getText()));
        }
    }

    private void processFocusGainedStringFormatted() {
        ((JFormattedTextField) mjComponent).setCaretPosition(0);
    }

    private void processFocusGainedDatetime() {
        ((JFormattedTextField) mjComponent).setCaretPosition(0);
    }

    private void processFocusLostDatetime() {
        java.util.Date date = completeDatetime();

        setFieldValue(date);    // corrects any wrong date

        if (date == null && mjComponent.isEnabled()) {
            if (mbIsMandatory) {
                switch (mnDataType) {
                    case SLibConstants.DATA_TYPE_DATE:
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_DATE);
                        break;
                    case SLibConstants.DATA_TYPE_DATE_TIME:
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_DATE_TIME);
                        break;
                    case SLibConstants.DATA_TYPE_TIME:
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_TIME);
                        break;
                    default:
                }
            }
        }
    }

    private java.util.Date completeDatetime() {
        if (mnDataType == SLibConstants.DATA_TYPE_DATE && miClient.getSessionXXX().getFormatters().getDateFormat().toPattern().endsWith("yyyy")) { // XXX not proper use of miClient in this library class context
            String sDate = ((JFormattedTextField) mjComponent).getText().replaceAll(" ", "");

            if (sDate.length() == 7 || sDate.length() == 8) {
                int year = SLibUtilities.parseInt(sDate.substring(6));

                // date:  01/01/01 >> 01/01/2001
                // index: 01234567

                sDate = sDate.substring(0, 5) + (year > 30 ? "19" : "20") + (year > 9 ? "" : "0") + year;
                ((JFormattedTextField) mjComponent).setValue(sDate);
            }
        }

        return (java.util.Date) getFieldValue();
    }

    private boolean validate(boolean force) {
        boolean bValidation = true;
        long lValue = 0;
        double dValue = 0;
        String sValue = "";
        String sMessage = "";
        java.util.Date tValue;

        switch (mnDataType) {
            case SLibConstants.DATA_TYPE_BOOLEAN:
                break;

            case SLibConstants.DATA_TYPE_INTEGER:
            case SLibConstants.DATA_TYPE_LONG:
                processFocusLostNumber();

                lValue = SLibUtilities.parseLong(((JTextField) mjComponent).getText());
                break;

            case SLibConstants.DATA_TYPE_FLOAT:
            case SLibConstants.DATA_TYPE_DOUBLE:
                processFocusLostNumber();

                dValue = SLibUtilities.parseDouble(((JTextField) mjComponent).getText()) / (mbIsPercent ? 100d : 1d);
                break;

            case SLibConstants.DATA_TYPE_STRING:
                processFocusLostString();

                if (mjComponent instanceof javax.swing.JComboBox) {
                    sValue = ((JComboBox) mjComponent).getSelectedItem() == null ? "" : (String) ((JComboBox) mjComponent).getSelectedItem();
                }
                else {
                    sValue = SLibUtilities.textTrim(((JTextComponent) mjComponent).getText());
                }
                break;

            case SLibConstants.DATA_TYPE_DATE:
            case SLibConstants.DATA_TYPE_DATE_TIME:
            case SLibConstants.DATA_TYPE_TIME:
                processFocusLostDatetime();
                break;

            case SLibConstants.DATA_TYPE_KEY:
                break;

            default:
        }

        if ((mbIsMandatory && mjComponent.isEnabled()) || (!mbIsMandatory && mnDataType == SLibConstants.DATA_TYPE_STRING && !sValue.isEmpty()) || force) {
            switch (mnDataType) {
                case SLibConstants.DATA_TYPE_BOOLEAN:
                    break;

                case SLibConstants.DATA_TYPE_INTEGER:
                case SLibConstants.DATA_TYPE_LONG:
                    if (mbMinInclusive && lValue < mlLongMin) {
                        sMessage = "El valor para el campo '" + getFieldName() + "' no puede ser menor que " + (moDecimalFormat == null ? "" + mlLongMin : moDecimalFormat.format(mlLongMin)) + ".";
                    }
                    else if (!mbMinInclusive && lValue <= mlLongMin) {
                        sMessage = "El valor para el campo '" + getFieldName() + "' no puede ser menor o igual que " + (moDecimalFormat == null ? "" + mlLongMin : moDecimalFormat.format(mlLongMin)) + ".";
                    }
                    else if (mbMaxInclusive && lValue > mlLongMax) {
                        sMessage = "El valor para el campo '" + getFieldName() + "' no puede ser mayor que " + (moDecimalFormat == null ? "" + mlLongMax : moDecimalFormat.format(mlLongMax)) + ".";
                    }
                    else if (!mbMaxInclusive && lValue >= mlLongMax) {
                        sMessage = "El valor para el campo '" + getFieldName() + "' no puede ser mayor o igual que " + (moDecimalFormat == null ? "" + mlLongMax : moDecimalFormat.format(mlLongMax)) + ".";
                    }
                    break;

                case SLibConstants.DATA_TYPE_FLOAT:
                case SLibConstants.DATA_TYPE_DOUBLE:
                    if (mbMinInclusive && dValue < mdDoubleMin) {
                        sMessage = "El valor para el campo '" + getFieldName() + "' no puede ser menor que " + (moDecimalFormat == null ? "" + mdDoubleMin : moDecimalFormat.format(mdDoubleMin)) + ".";
                    }
                    else if (!mbMinInclusive && dValue <= mdDoubleMin) {
                        sMessage = "El valor para el campo '" + getFieldName() + "' no puede ser menor o igual que " + (moDecimalFormat == null ? "" + mdDoubleMin : moDecimalFormat.format(mdDoubleMin)) + ".";
                    }
                    else if (mbMaxInclusive && dValue > mdDoubleMax) {
                        sMessage = "El valor para el campo '" + getFieldName() + "' no puede ser mayor que " + (moDecimalFormat == null ? "" + mdDoubleMax : moDecimalFormat.format(mdDoubleMax)) + ".";
                    }
                    else if (!mbMaxInclusive && dValue >= mdDoubleMax) {
                        sMessage = "El valor para el campo '" + getFieldName() + "' no puede ser mayor o igual que " + (moDecimalFormat == null ? "" + mdDoubleMax : moDecimalFormat.format(mdDoubleMax)) + ".";
                    }
                    break;

                case SLibConstants.DATA_TYPE_STRING:
                    if (sValue.length() < mnLengthMin) {
                        sMessage = "La longitud del valor para el campo '" + getFieldName() + "' no puede ser menor que " + mnLengthMin + ".";
                    }
                    else if (sValue.length() > mnLengthMax) {
                        sMessage = "La longitud del valor para el campo '" + getFieldName() + "' no puede ser mayor que " + mnLengthMax + ".";
                    }
                    break;

                case SLibConstants.DATA_TYPE_DATE:
                case SLibConstants.DATA_TYPE_DATE_TIME:
                case SLibConstants.DATA_TYPE_TIME:
                    String period = "";
                    SimpleDateFormat dateFormat = null;

                    if (mnDataType == SLibConstants.DATA_TYPE_DATE) {
                        period = "fecha";
                        dateFormat = miClient.getSessionXXX().getFormatters().getDateFormat();
                    }
                    else if (mnDataType == SLibConstants.DATA_TYPE_DATE_TIME) {
                        period = "fecha-hora";
                        dateFormat = miClient.getSessionXXX().getFormatters().getDatetimeFormat();
                    }
                    else {
                        period = "hora";
                        dateFormat = miClient.getSessionXXX().getFormatters().getTimeFormat();
                    }

                    if (dateFormat == null) {
                        sMessage = "No se puede validar la " + period + " para el campo '" + getFieldName() + "' porque no existe un objeto de formato adecuado.";
                    }
                    else {
                        tValue = dateFormat.parse(((JFormattedTextField) mjComponent).getText(), new ParsePosition(0));

                        if (tValue == null) {
                            sMessage = "La " + period + " para el campo '" + getFieldName() + "' se debe expresar con el patrón de formato '" + dateFormat.toPattern() + "'.";
                        }
                        else if (mtDateMin != null && mbMinInclusive && tValue.compareTo(mtDateMin) < 0) {
                            sMessage = "La " + period + " para el campo '" + getFieldName() + "' no puede ser menor que " + dateFormat.format(mtDateMin) + ".";
                        }
                        else if (mtDateMin != null && !mbMinInclusive && tValue.compareTo(mtDateMin) <= 0) {
                            sMessage = "La " + period + " para el campo '" + getFieldName() + "' no puede ser menor o igual que " + dateFormat.format(mtDateMin) + ".";
                        }
                        else if (mtDateMax != null && mbMaxInclusive && tValue.compareTo(mtDateMax) > 0) {
                            sMessage = "La " + period + " para el campo '" + getFieldName() + "' no puede ser mayor que " + dateFormat.format(mtDateMax) + ".";
                        }
                        else if (mtDateMax != null && !mbMaxInclusive && tValue.compareTo(mtDateMax) >= 0) {
                            sMessage = "La " + period + " para el campo '" + getFieldName() + "' no puede ser mayor o igual que " + dateFormat.format(mtDateMax) + ".";
                        }
                    }
                    break;

                case SLibConstants.DATA_TYPE_KEY:
                    if (mnFieldType == SLibConstants.FIELD_TYPE_LIST) {
                        if (mnListValidationType == SLibConstants.LIST_VALIDATION_BY_SELECTION) {
                            if (((JList) mjComponent).getSelectedIndex() < 0) {
                                sMessage = "Se debe seleccionar una opción para el campo '" + getFieldName() + "'.";
                            }
                        }
                        else {
                            if (((JList) mjComponent).getModel().getSize() == 0) {
                                sMessage = "Se deben agregar opciones para el campo '" + getFieldName() + "'.";
                            }
                        }
                    }
                    else {
                        if (mbIsSelectionItemApplying) {
                            if (((JComboBox) mjComponent).getSelectedIndex() <= 0) {
                                sMessage = "Se debe seleccionar una opción para el campo '" + getFieldName() + "'.";
                            }
                        }
                        else {
                            if (((JComboBox) mjComponent).getSelectedIndex() < 0) {
                                sMessage = "Se debe seleccionar una opción para el campo '" + getFieldName() + "'.";
                            }
                        }
                    }
                    break;

                default:
            }

            if (sMessage.length() > 0) {
                bValidation = false;
                if (mjTabbedPane != null) {
                    mjTabbedPane.setSelectedIndex(mnTabbedPaneIndex);
                }
                mjComponent.requestFocus();
                miClient.showMsgBoxWarning(sMessage);
            }
        }

        return bValidation;
    }

    @Override
    public void setDataType(int n) { mnDataType = n; }
    public void setIsMandatory(boolean b) { mbIsMandatory = b; }
    @Override
    public void setComponent(javax.swing.JComponent o) { mjComponent = o; }
    public void setFieldType(int n) { mnFieldType = n; }
    public void setOptionPickerType(int n) { mnOptionPickerType = n; if (mjComponent instanceof JComboBox) createPickerLauncher(); }
    public void setFieldName(java.lang.String s) { msFieldName = s; }
    public void setRawValue(java.lang.Object o) { moRawValue = o; }
    public void setDefaultValue(java.lang.Object o) { moDefaultValue = o; }
    public void setIntegerMin(int n) { mlLongMin = n; }
    public void setIntegerMax(int n) { mlLongMax = n; }
    public void setLongMin(long l) { mlLongMin = l; }
    public void setLongMax(long l) { mlLongMax = l; }
    public void setFloatMin(float f) { mdDoubleMin = f; }
    public void setFloatMax(float f) { mdDoubleMax = f; }
    public void setDoubleMin(double d) { mdDoubleMin = d; }
    public void setDoubleMax(double d) { mdDoubleMax = d; }
    public void setIsPercent(boolean b) { mbIsPercent = b; }
    public void setLengthMin(int n) { mnLengthMin = n; }
    public void setLengthMax(int n) { mnLengthMax = n; createKeyListener(); }
    public void setAutoCaseType(int n) { mnAutoCaseType = n; }
    public void setAutoTrim(boolean b) { mbAutoTrim = b; }
    public void setMaskFormatter(java.lang.String s) { moMaskFormatter = s; SFormUtilities.implementMaskFormatter((JFormattedTextField) mjComponent, SFormUtilities.createMaskFormatter(s)); }
    public void setDateMin(java.util.Date t) { mtDateMin = t; }
    public void setDateMax(java.util.Date t) { mtDateMax = t; }
    public void setMinInclusive(boolean b) { mbMinInclusive = b; }
    public void setMaxInclusive(boolean b) { mbMaxInclusive = b; }
    public void setIsSelectionItemApplying(boolean b) { mbIsSelectionItemApplying = b; }
    public void setListValidationType(int n) { mnListValidationType = n; }
    public void setTabbedPaneIndex(int n) { mnTabbedPaneIndex = n; }
    public void setDecimalFormat(java.text.DecimalFormat o) { moDecimalFormat = o; }
    public void setTabbedPane(javax.swing.JTabbedPane o) { mjTabbedPane = o; }
    public void setPickerButton(javax.swing.JButton o) { mjPickerButton = o; createButtonDispatcher(); }

    @Override
    public int getDataType() { return mnDataType; }
    public boolean getIsMandatory() { return mbIsMandatory; }
    @Override
    public javax.swing.JComponent getComponent() { return mjComponent; }
    public int getFieldType() { return mnFieldType; }
    public int getOptionPickerType() { return mnOptionPickerType; }
    public java.lang.String getFieldName() { return mjLabelName != null ? SGuiUtils.getLabelName(mjLabelName.getText()) : msFieldName; }
    public java.lang.Object getRawValue() { return moRawValue; }
    public java.lang.Object getDefaultValue() { return moDefaultValue; }
    public long getLongMin() { return mlLongMin; }
    public long getLongMax() { return mlLongMax; }
    public double getDoubleMin() { return mdDoubleMin; }
    public double getDoubleMax() { return mdDoubleMax; }
    public boolean getIsPercent() { return mbIsPercent; }
    public int getLengthMin() { return mnLengthMin; }
    public int getLengthMax() { return mnLengthMax; }
    public int getAutoCaseType() { return mnAutoCaseType; }
    public boolean getAutoTrim() { return mbAutoTrim; }
    public java.lang.String getMaskFormatter() { return moMaskFormatter; }
    public java.util.Date getDateMin() { return mtDateMin; }
    public java.util.Date getDateMax() { return mtDateMax; }
    public boolean getMinInclusive() { return mbMinInclusive; }
    public boolean getMaxInclusive() { return mbMaxInclusive; }
    public boolean getIsSelectionItemApplying() { return mbIsSelectionItemApplying; }
    public int getListValidationType() { return mnListValidationType; }
    public int getTabbedPaneIndex() { return mnTabbedPaneIndex; }
    public java.text.DecimalFormat getDecimalFormat() { return moDecimalFormat; }
    public javax.swing.JTabbedPane getTabbedPane() { return mjTabbedPane; }
    public javax.swing.JButton getPickerButton() { return mjPickerButton; }

    public void setBoolean(java.lang.Boolean o) {
        moRawValue = new Boolean(o);
        ((JCheckBox) mjComponent).setSelected(o);
    }

    public void setInteger(java.lang.Integer o) {
        moRawValue = new Integer(o);
        ((JTextField) mjComponent).setText(moDecimalFormat == null ? "" + o : moDecimalFormat.format(o));
    }

    public void setLong(java.lang.Long o) {
        moRawValue = new Long(o);
        ((JTextField) mjComponent).setText(moDecimalFormat == null ? "" + o : moDecimalFormat.format(o));
    }

    public void setFloat(java.lang.Float o) {
        moRawValue = new Float(o);
        ((JTextField) mjComponent).setText(moDecimalFormat == null ? "" + o : moDecimalFormat.format(o));
    }

    public void setDouble(java.lang.Double o) {
        moRawValue = new Double(o);
        ((JTextField) mjComponent).setText(moDecimalFormat == null ? "" + o : moDecimalFormat.format(o));
    }

    @Override
    public void setString(java.lang.String s) {
        moRawValue = new String(s);

        if (mjComponent instanceof javax.swing.JComboBox) {
            ((JComboBox) mjComponent).setSelectedItem(s);
        }
        else {
            ((JTextComponent) mjComponent).setText(s);
            ((JTextComponent) mjComponent).setCaretPosition(0);
        }
    }

    public void setDate(java.util.Date t) {
        moRawValue = t == null ? null : new java.util.Date(t.getTime());
        ((JFormattedTextField) mjComponent).setValue(t == null ? "" : miClient.getSessionXXX().getFormatters().getDateFormat().format(t));
    }

    public void setDatetime(java.util.Date t) {
        moRawValue = t == null ? null : new java.util.Date(t.getTime());
        ((JFormattedTextField) mjComponent).setValue(t == null ? "" : miClient.getSessionXXX().getFormatters().getDatetimeFormat().format(t));
    }

    public void setTime(java.util.Date t) {
        moRawValue = t == null ? null : new java.util.Date(t.getTime());
        ((JFormattedTextField) mjComponent).setValue(t == null ? "" : miClient.getSessionXXX().getFormatters().getTimeFormat().format(t));
    }

    @Override
    public void setKey(java.lang.Object key) {
        moRawValue = key;

        if (mnFieldType == SLibConstants.FIELD_TYPE_LIST) {
            JList list = (JList) mjComponent;
            if (moRawValue == null) {
                list.setSelectedIndex(-1);
            }
            else {
                if (moRawValue instanceof java.lang.String) {
                    SFormUtilities.locateListItem(list, (String) moRawValue);
                }
                else {
                    SFormUtilities.locateListItem(list, moRawValue);
                }
            }
        }
        else {
            JComboBox comboBox = (JComboBox) mjComponent;
            if (moRawValue == null) {
                comboBox.setSelectedIndex(comboBox.getItemCount() == 0 ? -1 : 0);
            }
            else {
                if (moRawValue instanceof java.lang.String) {
                    SFormUtilities.locateComboBoxItem(comboBox, (String) moRawValue);
                }
                else {
                    SFormUtilities.locateComboBoxItem(comboBox, moRawValue);
                }
            }
        }
    }

    public java.lang.Boolean getBoolean() {
        moRawValue = new Boolean(((JCheckBox) mjComponent).isSelected());
        return (Boolean) moRawValue;
    }

    public java.lang.Integer getInteger() {
        moRawValue = new Integer(SLibUtilities.parseInt(((JTextField) mjComponent).getText()));
        return (Integer) moRawValue;
    }

    public java.lang.Long getLong() {
        moRawValue = new Long(SLibUtilities.parseLong(((JTextField) mjComponent).getText()));
        return (Long) moRawValue;
    }

    public java.lang.Float getFloat() {
        moRawValue = new Float(SLibUtilities.parseFloat(((JTextField) mjComponent).getText()) / (mbIsPercent ? 100f : 1f));
        return (Float) moRawValue;
    }

    public java.lang.Double getDouble() {
        moRawValue = new Double(SLibUtilities.parseDouble(((JTextField) mjComponent).getText()) / (mbIsPercent ? 100d : 1d));
        return (Double) moRawValue;
    }

    @Override
    public java.lang.String getString() {
        if (mjComponent instanceof javax.swing.JComboBox) {
            moRawValue = processString(((JComboBox) mjComponent).getSelectedItem().toString());
        }
        else {
            moRawValue = processString(SLibUtilities.textTrim(((JTextComponent) mjComponent).getText()));
        }

        return (String) moRawValue;
    }

    public java.util.Date getDate() {
        moRawValue = miClient.getSessionXXX().getFormatters().getDateFormat().parse(((JFormattedTextField) mjComponent).getText(), new ParsePosition(0));
        return (java.util.Date) moRawValue;
    }

    public java.util.Date getDatetime() {
        moRawValue = miClient.getSessionXXX().getFormatters().getDatetimeFormat().parse(((JFormattedTextField) mjComponent).getText(), new ParsePosition(0));
        return (java.util.Date) moRawValue;
    }

    public java.util.Date getTime() {
        moRawValue = miClient.getSessionXXX().getFormatters().getTimeFormat().parse(((JFormattedTextField) mjComponent).getText(), new ParsePosition(0));
        return (java.util.Date) moRawValue;
    }

    @Override
    public java.lang.Object getKey() {
        if (mnFieldType == SLibConstants.FIELD_TYPE_LIST) {
            moRawValue = ((JList) mjComponent).getSelectedValue() == null ? null : ((SFormComponentItem) ((JList) mjComponent).getSelectedValue()).getPrimaryKey();
        }
        else {
            moRawValue = ((JComboBox) mjComponent).getSelectedItem() == null ? null : ((SFormComponentItem) ((JComboBox) mjComponent).getSelectedItem()).getPrimaryKey();
        }

        return moRawValue;
    }

    public int[] getKeyAsIntArray() {
        return (int[]) getKey();
    }

    public java.lang.Object[] getKeyAsObjectArray() {
        return (Object[]) getKey();
    }

    @Override
    public void setFieldValue(java.lang.Object value) {
        switch (mnDataType) {
            case SLibConstants.DATA_TYPE_BOOLEAN:
                setBoolean((Boolean) value);
                break;
            case SLibConstants.DATA_TYPE_INTEGER:
                setInteger(((Number) value).intValue());
                break;
            case SLibConstants.DATA_TYPE_LONG:
                setLong(((Number) value).longValue());
                break;
            case SLibConstants.DATA_TYPE_FLOAT:
                setFloat(((Number) value).floatValue());
                break;
            case SLibConstants.DATA_TYPE_DOUBLE:
                setDouble(((Number) value).doubleValue());
                break;
            case SLibConstants.DATA_TYPE_STRING:
                setString((String) value);
                break;
            case SLibConstants.DATA_TYPE_DATE:
                setDate((java.util.Date) value);
                break;
            case SLibConstants.DATA_TYPE_DATE_TIME:
                setDatetime((java.util.Date) value);
                break;
            case SLibConstants.DATA_TYPE_TIME:
                setTime((java.util.Date) value);
                break;
            case SLibConstants.DATA_TYPE_KEY:
                setKey(value);
                break;
            default:
        }
    }

    @Override
    public java.lang.Object getFieldValue() {
        Object value = null;

        switch (mnDataType) {
            case SLibConstants.DATA_TYPE_BOOLEAN:
                value = getBoolean();
                break;
            case SLibConstants.DATA_TYPE_INTEGER:
                value = getInteger();
                break;
            case SLibConstants.DATA_TYPE_LONG:
                value = getLong();
                break;
            case SLibConstants.DATA_TYPE_FLOAT:
                value = getFloat();
                break;
            case SLibConstants.DATA_TYPE_DOUBLE:
                value = getDouble();
                break;
            case SLibConstants.DATA_TYPE_STRING:
                value = getString();
                break;
            case SLibConstants.DATA_TYPE_DATE:
                value = getDate();
                break;
            case SLibConstants.DATA_TYPE_DATE_TIME:
                value = getDatetime();
                break;
            case SLibConstants.DATA_TYPE_TIME:
                value = getTime();
                break;
            case SLibConstants.DATA_TYPE_KEY:
                value = getKey();
                break;
            default:
        }

        return value;
    }

    public void resetField() {
        setFieldValue(moDefaultValue);
    }

    public void setTabbedPaneIndex(int index, javax.swing.JTabbedPane tabbedPane) {
        mnTabbedPaneIndex = index;
        mjTabbedPane = tabbedPane;
    }

    public boolean validateField() {
        return validate(false);
    }

    public boolean validateFieldForcing() {
        return validate(true);
    }
}
