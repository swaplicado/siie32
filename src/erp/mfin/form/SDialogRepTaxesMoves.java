/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogRepTaxesMoves.java
 *
 * Created on 11/05/2010, 04:30:45 PM
 */

package erp.mfin.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JFormattedTextField;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Néstor Ávalos
 */
public class SDialogRepTaxesMoves extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.FocusListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.lib.form.SFormField moFieldDateStart;
    private erp.lib.form.SFormField moFieldDateEnd;
    private erp.lib.form.SFormField moFieldCompanyBranch;

    private erp.mfin.form.SPanelAccount moPanelAccountInitial;
    private erp.mfin.form.SPanelAccount moPanelAccountEnd;

    /** Creates new form SDialogRepAccountingAux */
    public SDialogRepTaxesMoves(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;

        initComponents();
        initComponentsExtra();
        
        formRefreshCatalogues();
        formReset();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jlCompanyBranch = new javax.swing.JLabel();
        jcbCompanyBranch = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jpAccountInitial = new javax.swing.JPanel();
        jlDummyPaneAccountInitial = new javax.swing.JLabel();
        jpAccountEnd = new javax.swing.JPanel();
        jlDummyPaneAccountEnd = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Movimientos contables de impuestos");
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
        jPanel3.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDateStart.setText("Fecha inicial: *");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlDateStart);

        jftDateStart.setText("dd/mm/yyyy");
        jftDateStart.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel5.add(jftDateStart);

        jbDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateStart.setToolTipText("Seleccionar fecha inicial");
        jbDateStart.setFocusable(false);
        jbDateStart.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbDateStart);

        jPanel3.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDateEnd.setText("Fecha final: *");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlDateEnd);

        jftDateEnd.setText("dd/mm/yyyy");
        jftDateEnd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel6.add(jftDateEnd);

        jbDateEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateEnd.setToolTipText("Seleccionar fecha final");
        jbDateEnd.setFocusable(false);
        jbDateEnd.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbDateEnd);

        jPanel3.add(jPanel6);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros del reporte:"));
        jPanel7.setPreferredSize(new java.awt.Dimension(506, 80));
        jPanel7.setLayout(new java.awt.GridLayout(1, 1, 0, 1));

        jPanel8.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCompanyBranch.setText("Sucursal de la empresa:");
        jlCompanyBranch.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel10.add(jlCompanyBranch);

        jcbCompanyBranch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbCompanyBranch.setPreferredSize(new java.awt.Dimension(355, 23));
        jPanel10.add(jcbCompanyBranch);

        jPanel8.add(jPanel10);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));
        jPanel8.add(jPanel9);

        jPanel7.add(jPanel8);

        jPanel2.add(jPanel7, java.awt.BorderLayout.SOUTH);

        jPanel4.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        jpAccountInitial.setBorder(javax.swing.BorderFactory.createTitledBorder("Cuenta contable inicial:"));
        jpAccountInitial.setLayout(new java.awt.BorderLayout());

        jlDummyPaneAccountInitial.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlDummyPaneAccountInitial.setText("[Panel cuenta contable]");
        jlDummyPaneAccountInitial.setPreferredSize(new java.awt.Dimension(100, 50));
        jpAccountInitial.add(jlDummyPaneAccountInitial, java.awt.BorderLayout.CENTER);

        jPanel4.add(jpAccountInitial);

        jpAccountEnd.setBorder(javax.swing.BorderFactory.createTitledBorder("Cuenta contable final:"));
        jpAccountEnd.setLayout(new java.awt.BorderLayout());

        jlDummyPaneAccountEnd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlDummyPaneAccountEnd.setText("[Panel cuenta contable]");
        jlDummyPaneAccountEnd.setPreferredSize(new java.awt.Dimension(100, 50));
        jpAccountEnd.add(jlDummyPaneAccountEnd, java.awt.BorderLayout.CENTER);

        jPanel4.add(jpAccountEnd);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(550, 425));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    public void initComponentsExtra() {
        mvFields = new Vector<SFormField>();

        try {
            moPanelAccountInitial = new SPanelAccount(miClient, SDataConstants.FIN_ACC, false, false, false);
            moPanelAccountEnd = new SPanelAccount(miClient, SDataConstants.FIN_ACC, false, false, false);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        jpAccountInitial.remove(jlDummyPaneAccountInitial);
        jpAccountInitial.add(moPanelAccountInitial, BorderLayout.CENTER);

        jpAccountEnd.remove(jlDummyPaneAccountEnd);
        jpAccountEnd.add(moPanelAccountEnd, BorderLayout.CENTER);

        moFieldDateStart = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateStart, jlDateStart);
        moFieldDateStart.setPickerButton(jbDateStart);
        moFieldDateEnd = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateEnd, jlDateEnd);
        moFieldDateEnd.setPickerButton(jbDateEnd);
        moFieldCompanyBranch = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCompanyBranch, jlCompanyBranch);

        mvFields.add(moFieldDateStart);
        mvFields.add(moFieldDateEnd);
        mvFields.add(moFieldCompanyBranch);

        jbPrint.addActionListener(this);
        jbExit.addActionListener(this);
        jbDateStart.addActionListener(this);
        jbDateEnd.addActionListener(this);
        moPanelAccountInitial.getFieldAccount().getComponent().addFocusListener(this);

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
                map.put("tDtInitial", moFieldDateStart.getDate());
                map.put("tDtEnd", moFieldDateEnd.getDate());
                map.put("sDtInitialText", jftDateStart.getText());
                map.put("sDtEndText", jftDateEnd.getText());
                map.put("sAccountInitial", moPanelAccountInitial.getFieldAccount().getString());
                map.put("sAccountEnd", moPanelAccountEnd.getFieldAccount().getString());
                map.put("nCompanyBranchId", moFieldCompanyBranch.getKeyAsIntArray()[0]);
                map.put("sCompanyBranch", moFieldCompanyBranch.getKeyAsIntArray()[0] == 0 ? "(TODAS)" : jcbCompanyBranch.getSelectedItem().toString());
                map.put("sEmptyAccount", SDataUtilities.createNewFormattedAccountId(miClient, miClient.getSessionXXX().getParamsErp().getDeepAccounts()));
                map.put("nNumRecordLength", SDataConstantsSys.NUM_LEN_FIN_REC);
                map.put("sCurrency", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getCurrency());
                map.put("nSortingItemType", miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId());
                map.put("nFkCtSysMovId", SDataConstantsSys.FINS_CT_SYS_MOV_TAX);
                map.put("nFkTpAccMovTaxId", SDataConstantsSys.FINS_TP_ACC_MOV_TAX);
                map.put("nTAX_DBT", SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT);
                map.put("nTAX_CDT", SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT);
                map.put("sSql", createParamSql());

                jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_FIN_TAX_MOV, map);
                jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Reporte de movimientos de impuestos");
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

    private java.lang.String createSqlMayorAccount() {
        String sql = "";
        String account = miClient.getSessionXXX().getParamsErp().getFormatAccountId().replace('9', '0');
        Vector<Integer> levels = SDataUtilities.getAccountLevels(account);

        sql = "INNER JOIN fin_acc AS am ON " +
                "am.id_acc = CONCAT(LEFT(re.fid_acc, " + (levels.get(1) - 1) + "), '" + account.substring(levels.get(1) - 1) + "') ";

        return  sql;
    }

    private java.lang.String createParamSql() {
        int len = 0;
        int year = SLibTimeUtilities.digestYear(moFieldDateStart.getDate())[0];
        String sql = "";
        String txtDateStart = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(moFieldDateStart.getDate());
        String txtDateEnd = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(moFieldDateEnd.getDate());
        String account = miClient.getSessionXXX().getParamsErp().getFormatAccountId().replace('9', '0');
        Vector<Integer> levels = SDataUtilities.getAccountLevels(account);

        for (int i = 1; i <= miClient.getSessionXXX().getParamsErp().getDeepAccounts(); i++) {
            len = i < levels.size() ? levels.get(i) - 1 : account.length();

            /*
             * 'a' stands for account
             * 'am' stands for major account
             */

            sql += (sql.length() == 0 ? "" : "UNION ") +
                    "SELECT CONCAT(LEFT(re.fid_acc, " + len + "), '" + account.substring(len) + "')" + " AS f_id_acc, " +
                    "re.fid_acc, am.id_acc AS id_am, am.acc AS am, am.deep, am.fid_tp_acc_sys, " + i + " AS f_lev, " +
                    "(SELECT a.acc FROM fin_acc AS a WHERE a.id_acc = CONCAT(LEFT(re.fid_acc, " + len + "), '" + account.substring(len) + "')) AS f_acc, " +
                    "SUM(CASE WHEN r.dt < '" + txtDateStart + "' THEN re.debit ELSE 0 END) - " +
                    "SUM(CASE WHEN r.dt < '" + txtDateStart + "' THEN re.credit ELSE 0 END) AS f_ob, " +
                    "SUM(CASE WHEN r.dt >= '" + txtDateStart + "' THEN re.debit ELSE 0 END) AS f_dbt, " +
                    "SUM(CASE WHEN r.dt >= '" + txtDateStart + "' THEN re.credit ELSE 0 END) AS f_cdt " +
                    "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                    "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                    "r.id_year = " + year + " AND r.dt <= '" + txtDateEnd + "' AND r.b_del = 0 AND re.b_del = 0 AND (re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_CT_SYS_MOV_TAX + " OR " +
                    "re.fid_tp_acc_mov = " + SDataConstantsSys.FINS_TP_ACC_MOV_TAX + " OR (re.fid_tax_bas_n IS NOT NULL AND re.fid_tax_n IS NOT NULL)) " +
                    "INNER JOIN fin_acc AS a ON " +
                    "a.id_acc = re.fid_acc AND re.fid_acc >= '" + moPanelAccountInitial.getFieldAccount().getString() +
                    "' AND re.fid_acc <= '" + (
                    moPanelAccountInitial.isEmptyAccountId() &&  moPanelAccountEnd.isEmptyAccountId() ? moPanelAccountEnd.getFieldAccount().getString().replace('0', '9') :
                    moPanelAccountEnd.getFieldAccount().getString()) + "' " +
                    "INNER JOIN fin_acc AS am ON " +
                    "am.id_acc = CONCAT(LEFT(re.fid_acc, " + (levels.get(1) - 1) + "), '" + account.substring(levels.get(1) - 1) + "') " +
                    "GROUP BY CONCAT(LEFT(re.fid_acc, " + len + "), '" + account.substring(len) + "') ";
        }

        sql += "ORDER BY f_id_acc, f_lev, deep ";

        return sql;
    }

    private void actionClose() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void actionDateInitial() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldDateStart.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldDateStart.setDate(miClient.getGuiDatePickerXXX().getGuiDate());
            jftDateStart.requestFocus();
        }
    }

    private void actionDateEnd() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldDateEnd.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldDateEnd.setDate(miClient.getGuiDatePickerXXX().getGuiDate());
            jftDateEnd.requestFocus();
        }
    }

    private void actionAccountIdFocusGained() {
    }

    private void actionAccountIdFocusLost() {
        if (!moPanelAccountInitial.isEmptyAccountId() && moPanelAccountEnd.isEmptyAccountId()) {
            moPanelAccountEnd.getFieldAccount().setFieldValue(moPanelAccountInitial.getFieldAccount().getFieldValue());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
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
    private javax.swing.JButton jbExit;
    private javax.swing.JButton jbPrint;
    private javax.swing.JComboBox jcbCompanyBranch;
    private javax.swing.JFormattedTextField jftDateEnd;
    private javax.swing.JFormattedTextField jftDateStart;
    private javax.swing.JLabel jlCompanyBranch;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlDummyPaneAccountEnd;
    private javax.swing.JLabel jlDummyPaneAccountInitial;
    private javax.swing.JPanel jpAccountEnd;
    private javax.swing.JPanel jpAccountInitial;
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
        moPanelAccountInitial.resetPanel();
        moPanelAccountEnd.resetPanel();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbCompanyBranch, SDataConstants.BPSU_BPB, new int[] { miClient.getSessionXXX().getCurrentCompany().getPkCompanyId() });
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
            if (moPanelAccountInitial.getFieldAccount().getString().compareTo(moPanelAccountEnd.getFieldAccount().getString()) > 1) {
                validation.setComponent(moPanelAccountEnd);
                validation.setMessage("El valor de la cuenta contable final debe ser mayor o igual al valor de la cuenta contable inicial.");
            }
            else if (moPanelAccountInitial.getFieldAccount().getString().compareTo(SDataUtilities.createNewFormattedAccountId(miClient, miClient.getSessionXXX().getParamsErp().getDeepAccounts())) != 0 &&
                    moPanelAccountEnd.getFieldAccount().getString().compareTo(SDataUtilities.createNewFormattedAccountId(miClient, miClient.getSessionXXX().getParamsErp().getDeepAccounts())) == 0) {
                validation.setComponent(moPanelAccountEnd);
                validation.setMessage("Se debe seleccionar el valor de la cuenta contable final.");
            }
            else if (moPanelAccountInitial.getFieldAccount().getString().compareTo(SDataUtilities.createNewFormattedAccountId(miClient, miClient.getSessionXXX().getParamsErp().getDeepAccounts())) == 0 &&
                    moPanelAccountEnd.getFieldAccount().getString().compareTo(SDataUtilities.createNewFormattedAccountId(miClient, miClient.getSessionXXX().getParamsErp().getDeepAccounts())) != 0) {
                validation.setComponent(moPanelAccountInitial);
                validation.setMessage("Se debe seleccionar el valor de la cuenta contable inicial.");
            }
            else if (moFieldDateEnd.getDate().compareTo(moFieldDateStart.getDate()) < 0) {
                validation.setMessage("La fecha final debe ser mayor o igual a la fecha inicial.");
                validation.setComponent(jftDateEnd);
            }
            else if (SLibTimeUtilities.digestYear(moFieldDateEnd.getDate())[0] != SLibTimeUtilities.digestYear(moFieldDateStart.getDate())[0]) {
                validation.setMessage("La fecha inicial y final deben pertenecer al mismo ejercicio.");
                validation.setComponent(jftDateStart);
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
        return null;
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
                actionDateInitial();
            }
            else if (button == jbDateEnd) {
                actionDateEnd();
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() instanceof javax.swing.JFormattedTextField) {
            JFormattedTextField formattedTextField = (JFormattedTextField) e.getSource();

            if (formattedTextField == moPanelAccountInitial.getFieldAccount().getComponent()) {
                actionAccountIdFocusGained();
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof javax.swing.JFormattedTextField) {
            JFormattedTextField formattedTextField = (JFormattedTextField) e.getSource();

            if (formattedTextField == moPanelAccountInitial.getFieldAccount().getComponent()) {
                actionAccountIdFocusLost();
            }
        }
    }
}
