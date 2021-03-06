/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.form;

import erp.mod.fin.db.SFiscalUtils;
import erp.mod.fin.db.SRowFiscalChartOfAccounts;
import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFormDialog;

/**
 *
 * @author Sergio Flores
 */
public class SDialogFiscalXmlFileChartOfAccounts extends SBeanFormDialog {

    private int mnYear;
    private int mnMonth;
    private SGridPaneForm moGridChartOfAcounts;

    /**
     * Creates new form SDialogFiscalXmlFile
     */
    public SDialogFiscalXmlFileChartOfAccounts(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, title);
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpGrid = new javax.swing.JPanel();
        jpPeriod = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlYear = new javax.swing.JLabel();
        jtfYear = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jlMonth = new javax.swing.JLabel();
        jtfMonth = new javax.swing.JTextField();

        jpGrid.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración actual del catálogo de cuentas utilizado en el período:"));
        jpGrid.setLayout(new java.awt.BorderLayout(0, 5));

        jpPeriod.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel2.setLayout(new java.awt.FlowLayout(3, 5, 0));

        jlYear.setText("Ejercicio:");
        jlYear.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jlYear);

        jtfYear.setEditable(false);
        jtfYear.setFocusable(false);
        jtfYear.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel2.add(jtfYear);

        jpPeriod.add(jPanel2);

        jPanel3.setLayout(new java.awt.FlowLayout(3, 5, 0));

        jlMonth.setText("Período:");
        jlMonth.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel3.add(jlMonth);

        jtfMonth.setEditable(false);
        jtfMonth.setFocusable(false);
        jtfMonth.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jtfMonth);

        jpPeriod.add(jPanel3);

        jpGrid.add(jpPeriod, java.awt.BorderLayout.NORTH);

        getContentPane().add(jpGrid, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel jlMonth;
    private javax.swing.JLabel jlYear;
    private javax.swing.JPanel jpGrid;
    private javax.swing.JPanel jpPeriod;
    private javax.swing.JTextField jtfMonth;
    private javax.swing.JTextField jtfYear;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 960, 600);

        moGridChartOfAcounts = new SGridPaneForm(miClient, SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, "") {

            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> columns = new ArrayList<>();

                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "No. cuenta contable"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "Código cuenta contable"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "Cuenta contable"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_1B, "Profundidad"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_1B, "Nivel"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Cuenta especial"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "Código agrupador SAT"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_RAW, "ID asociado negocios"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "RFC asociado negocios"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Empleado"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Socio/accionista"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Parte relacionada"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_RAW, "ID ítem"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "Código agrupador SAT (ingresos)"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "Código agrupador SAT (egresos)"));

                return columns;
            }
        };

        jpGrid.add(moGridChartOfAcounts, BorderLayout.CENTER);

        jbSave.setText(SGuiConsts.TXT_BTN_OK);

        moFields.setFormButton(jbSave);
    }

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    @Override
    protected void windowActivated() {
        if (mbFirstActivation) {
            mbFirstActivation = false;
            jbSave.requestFocus();
        }
    }

    @Override
    public void setValue(final int type, final Object value) {
        switch (type) {
            case SGuiConsts.PARAM_DATE:
                mnYear = ((int[]) value) [0];
                mnMonth = ((int[]) value) [1];
                jtfYear.setText(SLibUtils.DecimalFormatCalendarYear.format(mnYear));
                jtfMonth.setText(SLibUtils.textProperCase(SLibTimeUtils.createMonthsOfYearStd(Calendar.LONG)[mnMonth - 1]));
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
    }

    @Override
    public void initForm() {
        String sql = "";
        ResultSet resultSet = null;
        SRowFiscalChartOfAccounts account = null;
        ArrayList<SRowFiscalChartOfAccounts> accounts = new ArrayList<>();

        try {
            sql = SFiscalUtils.createQueryCatalogo13(miClient.getSession(), mnYear, mnMonth);
            resultSet = miClient.getSession().getStatement().executeQuery(sql);
            while (resultSet.next()) {
                account = new SRowFiscalChartOfAccounts();

                account.AccountId = resultSet.getString("f_acc_id");
                account.AccountCode = resultSet.getString("f_acc_code");
                account.AccountName = resultSet.getString("f_acc_name");
                account.Deep = resultSet.getInt("f_deep");
                account.Level = resultSet.getInt("f_lev");
                account.SpecialAccountName = resultSet.getString("f_tp_spe_name");
                account.FiscalAccountCode = resultSet.getString("f_fis_acc_code");
                account.BizPartnerId = resultSet.getInt("f_bnk_cob_id") != 0 ? SLibUtils.parseInt("" + resultSet.getInt("f_bnk_cob_id") + resultSet.getInt("f_bnk_ent_id")) : resultSet.getInt("f_bpr_id");
                account.BizPartnerFiscalId = resultSet.getInt("f_bnk_cob_id") != 0 ? resultSet.getString("f_bnk_fis_id") : resultSet.getString("f_bpr_fis_id");
                account.IsEmployee = resultSet.getBoolean("f_bpr_emp");
                account.IsShareholder = resultSet.getBoolean("f_bpr_shh");
                account.IsRelatedParty = resultSet.getBoolean("f_bpr_rel");
                account.ItemId = resultSet.getInt("f_itm_id");
                account.ItemFiscalAccountCodeInc = resultSet.getString("f_itm_fis_acc_inc");
                account.ItemFiscalAccountCodeExp = resultSet.getString("f_itm_fis_acc_exp");

                accounts.add(account);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }

        moGridChartOfAcounts.populateGrid(new Vector<SGridRow>(accounts));
    }

    @Override
    public void addAllListeners() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeAllListeners() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reloadCatalogues() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiValidation validateForm() {
        return new SGuiValidation();
    }
}
