/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogRecordPicker.java
 *
 * Created on 22/09/2009, 04:24:20 PM
 */

package erp.mfin.form;

import erp.data.SDataReadTableRows;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.lib.table.STableRow;
import erp.lib.table.STableUtilities;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTextField;

/**
 *
 * @author Sergio Flores
 */
public class SDialogRecordPicker extends javax.swing.JDialog implements erp.lib.form.SFormOptionPickerInterface, java.awt.event.ActionListener {

    private erp.client.SClientInterface miClient;
    private int mnOptionType;
    private int mnFormResult;
    private int mnRequiredIndex;
    private boolean mbFirstTime;
    private java.lang.Object moFilterKey;
    private erp.lib.table.STablePane moPaneOptions;
    private HashMap<Integer, Object> moParamsMap;

    /** Creates new form SDialogRecordPicker
     @param client ERP Client interface.
     @param type SDataConstants.FIN_REC, SDataConstants.FINX_REC_USER
     */
    public SDialogRecordPicker(erp.client.SClientInterface client, int type) {
        super(client.getFrame(), true);
        miClient =  client;
        mnOptionType = type;    // SDataConstants.FIN_REC, SDataConstants.FINX_REC_USER,
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
        jlPeriod = new javax.swing.JLabel();
        jtfPeriod = new javax.swing.JTextField();
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
        setTitle("Selección de póliza contable");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpOptions.setBorder(javax.swing.BorderFactory.createTitledBorder("Pólizas contables disponibles:"));
        jpOptions.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jlPeriod.setText("Período contable:");
        jlPeriod.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jlPeriod);

        jtfPeriod.setEditable(false);
        jtfPeriod.setText("yyyy-dd");
        jtfPeriod.setFocusable(false);
        jtfPeriod.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jtfPeriod);

        jPanel4.add(jPanel6);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jlOption.setText("Seleccionar una póliza contable:");
        jlOption.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel5.add(jlOption, java.awt.BorderLayout.LINE_START);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 0));

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

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-900)/2, (screenSize.height-600)/2, 900, 600);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        int i = 0;
        STableColumn[] tableColumns = null;

        moPaneOptions = new STablePane(miClient);
        moPaneOptions.setDoubleClickAction(this, "publicActionOk");
        jpOptions.add(moPaneOptions, BorderLayout.CENTER);
        moParamsMap = null;

        tableColumns = new STableColumn[13];
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_per", "Período póliza", STableConstants.WIDTH_YEAR_PERIOD);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bkc.code", "Centro contable", STableConstants.WIDTH_CODE_COB);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cob.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "r.dt", "Fecha póliza", STableConstants.WIDTH_DATE);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "r.concept", "Concepto", 175);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_debit", "Cargos $", STableConstants.WIDTH_VALUE_2X);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_credit", "Abonos $", STableConstants.WIDTH_VALUE_2X);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_balance", "Saldo $", STableConstants.WIDTH_VALUE_2X);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "r.b_sys", "Sistema", STableConstants.WIDTH_BOOLEAN);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.ent", "Cuenta efectivo", 100);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "r.b_audit", "Auditada", STableConstants.WIDTH_BOOLEAN);
        tableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "r.b_authorn", "Autorizada", STableConstants.WIDTH_BOOLEAN);

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

            // Go to required index every first time this form is activated:
            if (mnRequiredIndex != -1) {
                moPaneOptions.setTableRowSelection(mnRequiredIndex);
                mnRequiredIndex = -1;
            }
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
    private javax.swing.JLabel jlOption;
    private javax.swing.JLabel jlPeriod;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpOptions;
    private javax.swing.JTextField jtfPeriod;
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
    
    public void setParams(HashMap hashMap) {
        moParamsMap = hashMap;
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnRequiredIndex = -1;
        mbFirstTime = true;
        moFilterKey = null;

        jtfPeriod.setText("");

        moPaneOptions.setTableRowSelection(0);
    }

    @Override
    public void formRefreshOptionPane() {
        Vector<STableRow> vTableRows = SDataReadTableRows.getTableRows(miClient, mnOptionType, moFilterKey, moParamsMap);

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

    @Override
    public void setFilterKey(Object filterKey) {
        moFilterKey = filterKey;

        jtfPeriod.setText(miClient.getSessionXXX().getFormatters().getDateYearMonthFormat().format((java.util.Date) moFilterKey));
    }

    @Override
    public void setSelectedPrimaryKey(Object pk) {
        for (int i = 0; i < moPaneOptions.getTableGuiRowCount(); i++) {
            if (SLibUtilities.compareKeys(pk, moPaneOptions.getTableModel().getTableRow(i).getPrimaryKey())) {
                mnRequiredIndex = i;
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
