/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogPayrollCfdiPicker.java
 *
 * Created on 22/09/2009, 04:24:20 PM
 */

package erp.mhrs.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadTableRows;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.lib.table.STableRow;
import erp.lib.table.STableUtilities;
import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbEmployee;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTextField;

/**
 *
 * @author Sergio Flores, Edwin Carmona, Sergio Flores
 */
public class SDialogPayrollCfdiPicker extends javax.swing.JDialog implements erp.lib.form.SFormOptionPickerInterface, java.awt.event.ActionListener {

    private erp.client.SClientInterface miClient;
    private int mnOptionType;
    private int mnFormResult;
    private boolean mbFirstTime;
    private int mnEmployeeId;
    private int mnCfdType;
    private int mnCfdStatus;
    private java.lang.Object moFilterKey;
    private erp.lib.table.STablePane moPaneOptions;

    /** Creates new form SDialogPayrollCfdiPicker
     * @param client ERP Client interface.
     */
    public SDialogPayrollCfdiPicker(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnOptionType = SDataConstants.TRN_CFD;
        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpOptions = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlEmployee = new javax.swing.JLabel();
        jtfEmployeeName = new javax.swing.JTextField();
        jtfEmployeeNumber = new javax.swing.JTextField();
        jtfEmployeeFiscalId = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jlOption = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jtfSeek = new javax.swing.JTextField();
        jbSeek = new javax.swing.JButton();
        jbExportCsv = new javax.swing.JButton();
        jbRefresh = new javax.swing.JButton();
        jpControls = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Selección de documento de compras-ventas");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpOptions.setBorder(javax.swing.BorderFactory.createTitledBorder("Documentos disponibles:"));
        jpOptions.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlEmployee.setText("Empleado:");
        jlEmployee.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlEmployee);

        jtfEmployeeName.setEditable(false);
        jtfEmployeeName.setText("NAME");
        jtfEmployeeName.setFocusable(false);
        jtfEmployeeName.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel6.add(jtfEmployeeName);

        jtfEmployeeNumber.setEditable(false);
        jtfEmployeeNumber.setText("NUMBER");
        jtfEmployeeNumber.setFocusable(false);
        jtfEmployeeNumber.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel6.add(jtfEmployeeNumber);

        jtfEmployeeFiscalId.setEditable(false);
        jtfEmployeeFiscalId.setText("FOGS770121V38");
        jtfEmployeeFiscalId.setFocusable(false);
        jtfEmployeeFiscalId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jtfEmployeeFiscalId);

        jPanel4.add(jPanel6);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jlOption.setText("Seleccionar un CFDI:");
        jlOption.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel5.add(jlOption, java.awt.BorderLayout.LINE_START);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfSeek.setText("Seek");
        jtfSeek.setToolTipText("Texto a buscar [Ctrl+B]");
        jtfSeek.setEnabled(false);
        jtfSeek.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel1.add(jtfSeek);

        jbSeek.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_seek.gif"))); // NOI18N
        jbSeek.setToolTipText("Buscar");
        jbSeek.setEnabled(false);
        jbSeek.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel1.add(jbSeek);

        jbExportCsv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_file_csv.gif"))); // NOI18N
        jbExportCsv.setToolTipText("Exportar CSV [Ctrl+E]");
        jbExportCsv.setEnabled(false);
        jbExportCsv.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel1.add(jbExportCsv);

        jbRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_reload.gif"))); // NOI18N
        jbRefresh.setToolTipText("Refrescar [Ctrl+R]");
        jbRefresh.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel1.add(jbRefresh);

        jPanel5.add(jPanel1, java.awt.BorderLayout.EAST);

        jPanel4.add(jPanel5);

        jpOptions.add(jPanel4, java.awt.BorderLayout.NORTH);

        getContentPane().add(jpOptions, java.awt.BorderLayout.CENTER);

        jpControls.setPreferredSize(new java.awt.Dimension(392, 33));
        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jpControls.add(jbCancel);

        getContentPane().add(jpControls, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(800, 600));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        int i = 0;
        STableColumnForm[] tableColumns = null;

        moPaneOptions = new STablePane(miClient);
        moPaneOptions.setDoubleClickAction(this, "publicActionOk");
        jpOptions.add(moPaneOptions, BorderLayout.CENTER);

        tableColumns = new STableColumnForm[8];
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Fecha-hora CFD", STableConstants.WIDTH_DATE_TIME);
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo CFD", 100);
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Versión CFD", 50);
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Folio CFD", STableConstants.WIDTH_DOC_NUM);
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "UUID", 100);
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "RFC receptor", 100);
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Total $", STableConstants.WIDTH_VALUE_2X);
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Moneda", STableConstants.WIDTH_CURRENCY_KEY);

        for (i = 0; i < tableColumns.length; i++) {
            moPaneOptions.addTableColumn(tableColumns[i]);
        }

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbSeek.addActionListener(this);
        jbExportCsv.addActionListener(this);
        jbRefresh.addActionListener(this);
        jtfSeek.addActionListener(this);

        SFormUtilities.createActionMap(rootPane, this, "focusSeek", "seek", KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionExportCsv", "exportCsv", KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionRefresh", "refresh", KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionCancel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            moPaneOptions.getTable().requestFocus();
        }
    }
    
    private void renderEmployee() {
        if (mnEmployeeId == 0) {
            jtfEmployeeName.setText("");
            jtfEmployeeNumber.setText("");
            jtfEmployeeFiscalId.setText("");
        }
        else {
            SDbEmployee employee = (SDbEmployee) miClient.getSession().readRegistry(SModConsts.HRSU_EMP, new int[] { mnEmployeeId });
            jtfEmployeeName.setText(employee.getXtaEmployeeName());
            jtfEmployeeNumber.setText(employee.getNumber());
            jtfEmployeeFiscalId.setText(employee.getXtaEmployeeRfc());
            jtfEmployeeName.setCaretPosition(0);
            jtfEmployeeNumber.setCaretPosition(0);
            jtfEmployeeFiscalId.setCaretPosition(0);
        }
    }

    private void actionOk() {
        if (moPaneOptions.getSelectedTableRow() == null) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            moPaneOptions.getTable().requestFocus();
        }
        else {
            mnFormResult = SLibConstants.FORM_RESULT_OK;
            setVisible(false);
        }
    }

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    public void publicActionOk() {
        actionOk();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbExportCsv;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbRefresh;
    private javax.swing.JButton jbSeek;
    private javax.swing.JLabel jlEmployee;
    private javax.swing.JLabel jlOption;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpOptions;
    private javax.swing.JTextField jtfEmployeeFiscalId;
    private javax.swing.JTextField jtfEmployeeName;
    private javax.swing.JTextField jtfEmployeeNumber;
    private javax.swing.JTextField jtfSeek;
    // End of variables declaration//GEN-END:variables

    public void focusSeek() {
        if (jtfSeek.isEnabled()) {
            jtfSeek.requestFocus();
        }
    }

    public void actionSeek() {
        if (jbSeek.isEnabled()) {
            STableUtilities.actionSeek(miClient, moPaneOptions, jtfSeek.getText().trim());
        }
    }

    public void actionExportCsv() {
        if (jbExportCsv.isEnabled()) {
            STableUtilities.actionExportCsv(miClient, moPaneOptions, getTitle());
        }
    }

    public void actionRefresh() {
        formRefreshOptionPane();
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mbFirstTime = true;
        mnEmployeeId = 0;
        mnCfdType = SDataConstantsSys.TRNS_TP_CFD_PAYROLL;
        mnCfdStatus = SDataConstantsSys.TRNS_ST_DPS_ANNULED;
        moFilterKey = null;

        moPaneOptions.setTableRowSelection(0);
        
        renderEmployee();
    }

    @Override
    public void formRefreshOptionPane() {
        Vector<STableRow> vTableRows = SDataReadTableRows.getTableRows(miClient, mnOptionType, moFilterKey);

        jtfSeek.setText("");
        jtfSeek.setEnabled(false);
        jbSeek.setEnabled(false);
        jbExportCsv.setEnabled(false);

        moPaneOptions.createTable();
        moPaneOptions.clearTableRows();

        for (int i = 0; i < vTableRows.size(); i++) {
            moPaneOptions.addTableRow(vTableRows.get(i));
        }
        moPaneOptions.renderTableRows();
        moPaneOptions.setTableRowSelection(0);

        if (moPaneOptions.getTableGuiRowCount() > 0) {
            jtfSeek.setEnabled(true);
            jbSeek.setEnabled(true);
            jbExportCsv.setEnabled(true);
        }

        moPaneOptions.getTable().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    evt.consume();
                    actionOk();
                }
            }
        });
    }

    @Override
    public int getOptionType() {
        return mnOptionType;
    }

    @Override
    public void setFormVisible(boolean visible) {
        setVisible(visible);
    }

    @Override
    public int getFormResult() {
        return mnFormResult;
    }

    /**
     * 
     * @param filterKey An object array of three elements: employee ID, type of CFDI and status of CFD.
     */
    @Override
    public void setFilterKey(Object filterKey) {
        moFilterKey = null;
        
        switch (mnOptionType) {
            case SDataConstants.TRN_CFD:
                if (((Object[]) filterKey).length >= 3) {
                    mnEmployeeId = (int) ((Object[]) filterKey)[0];
                    mnCfdType = (int) ((Object[]) filterKey)[1];
                    mnCfdStatus = (int) ((Object[]) filterKey)[2];

                    renderEmployee();        
                    moFilterKey = new Object[] { mnCfdType, jtfEmployeeFiscalId.getText(), mnCfdStatus };
                }
                else {
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
                }
                break;

            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }
    }

    @Override
    public void setSelectedPrimaryKey(Object pk) {
        moPaneOptions.renderTableRows();

        for (int i = 0; i < moPaneOptions.getTableGuiRowCount(); i++) {
            if (SLibUtilities.compareKeys(pk, moPaneOptions.getTableModel().getTableRow(i).getPrimaryKey())) {
                moPaneOptions.setTableRowSelection(i);
                break;
            }
        }
    }

    @Override
    public java.lang.Object getSelectedPrimaryKey() {
        Object pk = null;

        if (moPaneOptions.getSelectedTableRow() != null) {
            pk = moPaneOptions.getSelectedTableRow().getPrimaryKey();
        }

        return pk;
    }

    @Override
    public erp.lib.table.STableRow getSelectedOption() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbSeek) {
                actionSeek();
            }
            else if (button == jbExportCsv) {
                actionExportCsv();
            }
            else if (button == jbRefresh) {
                actionRefresh();
            }
        }
        else if (e.getSource() instanceof javax.swing.JTextField) {
            JTextField textField = (JTextField) e.getSource();

            if (textField == jtfSeek) {
                actionSeek();
            }
        }
    }
}
