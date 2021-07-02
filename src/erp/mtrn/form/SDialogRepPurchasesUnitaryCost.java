/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogRepPurchasesUnitaryCost.java
 *
 * Created on 24/08/2010, 04:49:58 PM
 */

package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataAccountRow;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JRadioButton;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDialogRepPurchasesUnitaryCost extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.ItemListener, CellEditorListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.lib.table.STablePane moPaneAccounts;
    private erp.lib.form.SFormField moFieldDateStart;
    private erp.lib.form.SFormField moFieldDateEnd;
    private erp.lib.form.SFormField moFieldCompanyBranch;
    
    protected int mnNumberAccountsSelects;
    protected boolean mbIsSelectedAll;
    protected String msAccountsSelectedsId;
    protected String msAccountsSelectedsName;

    private final int COL_SEL = 2;

    /** Creates new form SDialogRepPurchasesUnitaryCost */
    public SDialogRepPurchasesUnitaryCost(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;

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

        gbtSelect = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jbPrint = new javax.swing.JButton();
        jbExit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        jftDateStart = new javax.swing.JFormattedTextField();
        jbDateStart = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        jftDateEnd = new javax.swing.JFormattedTextField();
        jbDateEnd = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jlCompanyBranch = new javax.swing.JLabel();
        jcbCompanyBranch = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel12 = new javax.swing.JPanel();
        jrbAllAccounts = new javax.swing.JRadioButton();
        jrbSelectAccounts = new javax.swing.JRadioButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jpAccounts = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jbSelectAll = new javax.swing.JButton();
        jbDeselectAll = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Reporte de costos unitarios de compras");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(392, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbPrint.setText("Imprimir");
        jbPrint.setToolTipText("[Ctrl + Enter]");
        jbPrint.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbPrint);

        jbExit.setText("Cerrar");
        jbExit.setToolTipText("[Escape]");
        jbExit.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbExit);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración del reporte:"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Período:"));
        jPanel3.setPreferredSize(new java.awt.Dimension(376, 77));
        jPanel3.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial: *");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlDateStart);

        jftDateStart.setText("dd/mm/yyyy");
        jftDateStart.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel5.add(jftDateStart);

        jbDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_date_day.gif"))); // NOI18N
        jbDateStart.setToolTipText("Seleccionar fecha inicial");
        jbDateStart.setFocusable(false);
        jbDateStart.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbDateStart);

        jPanel3.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final: *");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlDateEnd);

        jftDateEnd.setText("dd/mm/yyyy");
        jftDateEnd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel6.add(jftDateEnd);

        jbDateEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_date_day.gif"))); // NOI18N
        jbDateEnd.setToolTipText("Seleccionar fecha final");
        jbDateEnd.setFocusable(false);
        jbDateEnd.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbDateEnd);

        jPanel3.add(jPanel6);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros del reporte:"));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel7.setLayout(new java.awt.GridLayout(1, 1, 0, 1));

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCompanyBranch.setText("Sucursal empresa:");
        jlCompanyBranch.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlCompanyBranch);

        jcbCompanyBranch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbCompanyBranch.setEnabled(false);
        jcbCompanyBranch.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel8.add(jcbCompanyBranch);

        jPanel7.add(jPanel8);

        jPanel4.add(jPanel7, java.awt.BorderLayout.NORTH);

        jPanel12.setLayout(new java.awt.GridLayout(2, 1));

        gbtSelect.add(jrbAllAccounts);
        jrbAllAccounts.setSelected(true);
        jrbAllAccounts.setText("Todas las cuentas contables.");
        jPanel12.add(jrbAllAccounts);

        gbtSelect.add(jrbSelectAccounts);
        jrbSelectAccounts.setText("Solo cuentas contables seleccionadas.");
        jPanel12.add(jrbSelectAccounts);

        jPanel4.add(jPanel12, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel11.setLayout(new java.awt.BorderLayout());
        jPanel10.add(jPanel11, java.awt.BorderLayout.NORTH);

        jpAccounts.setBorder(javax.swing.BorderFactory.createTitledBorder("Cuentas contables de compras:"));
        jpAccounts.setLayout(new java.awt.BorderLayout());

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbSelectAll.setText("Seleccionar todas");
        jPanel13.add(jbSelectAll);

        jbDeselectAll.setText("Deseleccionar todas");
        jPanel13.add(jbDeselectAll);

        jpAccounts.add(jPanel13, java.awt.BorderLayout.NORTH);

        jPanel10.add(jpAccounts, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel9, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(576, 389));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        int i = 0;
        STableColumnForm[] columns = null;
        mvFields = new Vector<SFormField>();
        
        moFieldDateStart = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateStart, jlDateStart);
        moFieldDateStart.setPickerButton(jbDateStart);
        moFieldDateEnd = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateEnd, jlDateEnd);
        moFieldDateEnd.setPickerButton(jbDateEnd);
        moFieldCompanyBranch = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCompanyBranch, jlCompanyBranch);
        
        moPaneAccounts = new STablePane(miClient);
        jpAccounts.add(moPaneAccounts, BorderLayout.CENTER);
        i = 0;
        columns = new STableColumnForm[3];
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "No. cuenta contable", 75);
        columns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Cuenta contable", 150);
        columns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Seleccionada", STableConstants.WIDTH_BOOLEAN);
        columns[i++].setEditable(true);

        for (i = 0; i < columns.length; i++) {
            moPaneAccounts.addTableColumn(columns[i]);
        }
        
        moPaneAccounts.createTable();
        moPaneAccounts.getTable().getTableHeader().setReorderingAllowed(false);
        //moPaneAccounts.getTableModel().getTableRows().addAll(mvTableRows);
        moPaneAccounts.renderTable();
        moPaneAccounts.layoutTable();

        moPaneAccounts.setTableRowSelection(0);
        
        moPaneAccounts.getTable().getColumnModel().getColumn(COL_SEL).setCellEditor(moPaneAccounts.getTable().getDefaultEditor(Boolean.class));
        moPaneAccounts.getTable().getColumnModel().getColumn(COL_SEL).getCellEditor().addCellEditorListener(this);
        
        mvFields.add(moFieldDateStart);
        mvFields.add(moFieldDateEnd);
        mvFields.add(moFieldCompanyBranch);

        jbPrint.addActionListener(this);
        jbExit.addActionListener(this);
        jbDateStart.addActionListener(this);
        jbDateEnd.addActionListener(this);
        jbSelectAll.addActionListener(this);
        jbDeselectAll.addActionListener(this);
        jrbAllAccounts.addItemListener(this);
        jrbSelectAccounts.addItemListener(this);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionPrint(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "print", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionClose(); }
        };

        SFormUtilities.putActionMap(getRootPane(), action, "exit", KeyEvent.VK_ESCAPE, 0);

        setModalityType(ModalityType.MODELESS);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jftDateStart.requestFocus();
        }
    }

    private void actionPrint() {
        Cursor cursor = getCursor();
        SFormValidation validation = formValidate();
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;
        
        if (validation.getIsError()) {
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
            if (validation.getMessage().length() > 0) {
                miClient.showMsgBoxWarning(validation.getMessage());
            }
        }
        else {
            try {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                
                map = miClient.createReportParams();
                map.put("tStart", moFieldDateStart.getDate());
                map.put("tEnd", moFieldDateEnd.getDate());
                map.put("nAccMovPurTp", SDataConstantsSys.FINS_CLS_ACC_PUR[0]);
                map.put("nAccMovPurCl", SDataConstantsSys.FINS_CLS_ACC_PUR[1]);
                map.put("nAccMovPurCls", SDataConstantsSys.FINS_CLS_ACC_PUR[2]);
                map.put("nAccMovPurAdjTp", SDataConstantsSys.FINS_CLS_ACC_PUR_ADJ[0]);
                map.put("nAccMovPurAdjCl", SDataConstantsSys.FINS_CLS_ACC_PUR_ADJ[1]);
                map.put("nAccMovPurAdjCls", SDataConstantsSys.FINS_CLS_ACC_PUR_ADJ[2]);
                map.put("sSqlWhereAcc", msAccountsSelectedsId.isEmpty() ? "" : "WHERE fid_acc IN(" + msAccountsSelectedsId + ")");
                map.put("sAccount", jrbAllAccounts.isSelected() || mbIsSelectedAll ? "(TODAS)" : msAccountsSelectedsName);

                jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_PUR_UNIT_COST, map);
                jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Reporte de costos unitarios de compras");
                jasperViewer.setVisible(true);
            }
            catch(Exception e) {
                SLibUtilities.renderException(this, e);
            }
            finally {
                setCursor(cursor);
            }
        }
    }

    private void actionClose() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void actionDateStart() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldDateStart.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldDateStart.setFieldValue(miClient.getGuiDatePickerXXX().getGuiDate());
            jftDateStart.requestFocus();
        }
    }

    private void actionDateEnd() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldDateEnd.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldDateEnd.setFieldValue(miClient.getGuiDatePickerXXX().getGuiDate());
            jftDateEnd.requestFocus();
        }
    }
    
    private void loadAccountsPurchases() {
        try {
            ArrayList<SDataAccount> accounts = SDataUtilities.obtainAccountsByType(miClient, SDataConstantsSys.FINS_CLS_ACC_PUR);
            accounts.addAll(SDataUtilities.obtainAccountsByType(miClient, SDataConstantsSys.FINS_CLS_ACC_PUR_ADJ));
            
            moPaneAccounts.clearTableRows();
            for (SDataAccount account : accounts) {
                moPaneAccounts.addTableRow(new SDataAccountRow(account));
            }
            moPaneAccounts.renderTableRows();
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }
    
    private void getAccounts() {
        int i = 0;
        SDataAccountRow payRow = null;
        msAccountsSelectedsId = "";
        msAccountsSelectedsName = "";
        mnNumberAccountsSelects = 0;
        mbIsSelectedAll = false;
        
        for (; i < moPaneAccounts.getTableGuiRowCount(); i++) {
            payRow = (SDataAccountRow) moPaneAccounts.getTableRow(i);
            
            if (payRow.isSelected()) {
                msAccountsSelectedsId += (msAccountsSelectedsId.length() == 0 ? "'" : ", '") + ((SDataAccount) payRow.getData()).getPkAccountIdXXX() + "'";
                msAccountsSelectedsName += (msAccountsSelectedsName.length() == 0 ? "" : ", ") + ((SDataAccount) payRow.getData()).getPkAccountIdXXX();
                mnNumberAccountsSelects++;
            }
        }
        
        mbIsSelectedAll = mnNumberAccountsSelects == moPaneAccounts.getTableGuiRowCount();
    }
    
    private void actionEntrySelect() {
        SDataAccountRow payRow = null;
        int index = moPaneAccounts.getTable().getSelectedRow();

        if (index != -1) {
            payRow = (SDataAccountRow) moPaneAccounts.getTableRow(index);
            payRow.setSelected(!payRow.isSelected());
            
            payRow.prepareTableRow();
        }
    }
    
    private void actionSelectAll(boolean selected) {
        int i = 0;
        SDataAccountRow payRow = null;
        
        for (; i < moPaneAccounts.getTableGuiRowCount(); i++) {
            payRow = (SDataAccountRow) moPaneAccounts.getTableRow(i);
            payRow.setSelected(selected);
            payRow.prepareTableRow();
        }
        moPaneAccounts.renderTableRows();
        moPaneAccounts.setTableRowSelection(0);
    }

    private void itemStateChangedCurrencyOptions() {
        if (jrbAllAccounts.isSelected()) {
            jbSelectAll.setEnabled(false);
            jbDeselectAll.setEnabled(false);
            actionSelectAll(false);
        }
        else {
            jbSelectAll.setEnabled(true);
            jbDeselectAll.setEnabled(true);
            actionSelectAll(true);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup gbtSelect;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbDateEnd;
    private javax.swing.JButton jbDateStart;
    private javax.swing.JButton jbDeselectAll;
    private javax.swing.JButton jbExit;
    private javax.swing.JButton jbPrint;
    private javax.swing.JButton jbSelectAll;
    private javax.swing.JComboBox<SFormComponentItem> jcbCompanyBranch;
    private javax.swing.JFormattedTextField jftDateEnd;
    private javax.swing.JFormattedTextField jftDateStart;
    private javax.swing.JLabel jlCompanyBranch;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JPanel jpAccounts;
    private javax.swing.JRadioButton jrbAllAccounts;
    private javax.swing.JRadioButton jrbSelectAccounts;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        moFieldDateStart.setFieldValue(SLibTimeUtilities.getBeginOfMonth(miClient.getSessionXXX().getWorkingDate()));
        moFieldDateEnd.setFieldValue(SLibTimeUtilities.getEndOfMonth(miClient.getSessionXXX().getWorkingDate()));
        
        jrbAllAccounts.setSelected(true);
        itemStateChangedCurrencyOptions();
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbCompanyBranch, SDataConstants.BPSU_BPB, new int[] { miClient.getSessionXXX().getCurrentCompany().getPkCompanyId() });
        loadAccountsPurchases();
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                break;
            }
        }

        if (!validation.getIsError()) {
            if (moFieldDateEnd.getDate().compareTo(moFieldDateStart.getDate()) < 0) {
                validation.setMessage("La fecha final debe ser mayor o igual a la fecha inicial.");
                validation.setComponent(jftDateEnd);
            }
        }
        
        if (!validation.getIsError()) {
            getAccounts();
            
            if (jrbSelectAccounts.isSelected() && mnNumberAccountsSelects == 0) {
                validation.setMessage("Se debe al menos especificar una cuenta contable.");
                validation.setComponent(jrbSelectAccounts);
            }
        }

        return validation;
    }

    @Override
    public void setFormStatus(int status) {
        mnFormStatus = status;
    }

    @Override
    public void setFormVisible(boolean visible) {
        setVisible(visible);
    }

    @Override
    public int getFormStatus() {
        return mnFormStatus;
    }

    @Override
    public int getFormResult() {
        return mnFormResult;
    }

    @Override
    public void setRegistry(erp.lib.data.SDataRegistry registry) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public java.lang.Object getValue(int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JLabel getTimeoutLabel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbPrint) {
                actionPrint();
            }
            else if (button == jbExit) {
                actionClose();
            }
            else if (button == jbDateStart) {
                actionDateStart();
            }
            else if (button == jbDateEnd) {
                actionDateEnd();
            }
            else if (button == jbSelectAll) {
                actionSelectAll(true);
            }
            else if (button == jbDeselectAll) {
                actionSelectAll(false);
            }
        }
    }
    
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JRadioButton) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JRadioButton radioButton = (JRadioButton) e.getSource();

                if (radioButton == jrbAllAccounts || radioButton == jrbSelectAccounts) {
                    itemStateChangedCurrencyOptions();
                }
            }
        }
    }

    @Override
    public void editingStopped(ChangeEvent e) {
        actionEntrySelect();
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
