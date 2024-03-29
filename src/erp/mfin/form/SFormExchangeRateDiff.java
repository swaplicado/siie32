/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormExchangeRateDiff.java
 *
 * Created on 22/10/2010, 01:02:32 PM
 */

package erp.mfin.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mod.SModSysConsts;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SFormExchangeRateDiff extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.FocusListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.lib.form.SFormField moFieldConcept;
    private erp.lib.form.SFormField moFieldValue;

    private erp.mfin.data.SDataRecord moParamRecord;
    private boolean mbParamIsMoneyIn;
    private erp.mfin.data.SDataRecordEntry moRecordEntry;
    private int mnFkUser;

    /** Creates new form SFormExchangeRateDiff */
    public SFormExchangeRateDiff(erp.client.SClientInterface client) {
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

        jpInput = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlAccountCash = new javax.swing.JLabel();
        jtfCompanyBranch = new javax.swing.JTextField();
        jtfAccountCash = new javax.swing.JTextField();
        jtfCode = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jlCurrency = new javax.swing.JLabel();
        jtfCurrency = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jlConcept = new javax.swing.JLabel();
        jtfConcept = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jlValue = new javax.swing.JLabel();
        jtfValue = new javax.swing.JTextField();
        jpControls = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ganacia cambiaria");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpInput.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpInput.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridLayout(4, 1, 0, 2));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlAccountCash.setText("Cuenta de dinero:");
        jlAccountCash.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel4.add(jlAccountCash);

        jtfCompanyBranch.setEditable(false);
        jtfCompanyBranch.setText("COMPANY BRANCH");
        jtfCompanyBranch.setFocusable(false);
        jtfCompanyBranch.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel4.add(jtfCompanyBranch);

        jtfAccountCash.setEditable(false);
        jtfAccountCash.setText("ACCOUNT CASH");
        jtfAccountCash.setFocusable(false);
        jtfAccountCash.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel4.add(jtfAccountCash);

        jtfCode.setEditable(false);
        jtfCode.setText("CODE");
        jtfCode.setFocusable(false);
        jtfCode.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel4.add(jtfCode);

        jPanel3.add(jPanel4);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCurrency.setText("Moneda cuenta de dinero:");
        jlCurrency.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel6.add(jlCurrency);

        jtfCurrency.setEditable(false);
        jtfCurrency.setText("CURRENCY");
        jtfCurrency.setFocusable(false);
        jtfCurrency.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jtfCurrency);

        jPanel3.add(jPanel6);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlConcept.setText("Concepto de la partida: *");
        jlConcept.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel5.add(jlConcept);

        jtfConcept.setText("CONCEPT");
        jtfConcept.setPreferredSize(new java.awt.Dimension(375, 23));
        jPanel5.add(jtfConcept);

        jPanel3.add(jPanel5);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlValue.setText("Monto moneda local (ML): *");
        jlValue.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel7.add(jlValue);

        jtfValue.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfValue.setText("0.00");
        jtfValue.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jtfValue);

        jPanel3.add(jPanel7);

        jpInput.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jpInput, java.awt.BorderLayout.CENTER);

        jpControls.setPreferredSize(new java.awt.Dimension(492, 33));
        jpControls.setLayout(new java.awt.GridLayout(1, 2));

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setForeground(java.awt.SystemColor.textInactiveText);
        jLabel1.setText("ferd");
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel1.add(jLabel1);

        jpControls.add(jPanel1);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jPanel2.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jPanel2.add(jbCancel);

        jpControls.add(jPanel2);

        getContentPane().add(jpControls, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(616, 414));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        mvFields = new Vector<>();

        moFieldConcept = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfConcept, jlConcept);
        moFieldConcept.setLengthMax(100);
        moFieldValue = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfValue, jlValue);
        moFieldValue.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat());

        mvFields.add(moFieldConcept);
        mvFields.add(moFieldValue);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);

        mnFkUser = 0;

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), action, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            setTitle(mbParamIsMoneyIn ? "Ganancia cambiaria" : "Pérdida cambiaria");
            jtfConcept.requestFocus();
        }

        renderCurrencySettings();
    }

    private void actionOk() {
        SFormValidation validation = formValidate();

        if (validation.getIsError()) {
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
            if (validation.getMessage().length() > 0) {
                miClient.showMsgBoxWarning(validation.getMessage());
            }
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

    private void renderCurrencySettings() {
        SDataBizPartnerBranch oBranch = null;

        oBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(
                miClient, SDataConstants.BPSU_BPB, new int[] { moParamRecord.getDbmsDataAccountCash().getPkCompanyBranchId() }, SLibConstants.EXEC_MODE_SILENT);

        jtfCompanyBranch.setText(oBranch.getBizPartnerBranch());
        jtfAccountCash.setText(moParamRecord.getDbmsDataAccountCash().getDbmsCompanyBranchEntity().getEntity());
        jtfCode.setText(moParamRecord.getDbmsDataAccountCash().getDbmsCompanyBranchEntity().getCode());
        jtfCurrency.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.CFGU_CUR, new int[] { moParamRecord.getDbmsDataAccountCash().getFkCurrencyId() }));

        jtfCompanyBranch.setCaretPosition(0);
        jtfAccountCash.setCaretPosition(0);
        jtfCode.setCaretPosition(0);
    }

    private erp.mfin.data.SDataRecordEntry[] prepareRecordEntries() {
        SDataRecordEntry oEntryMoney = new SDataRecordEntry();
        SDataRecordEntry oEntryGainLoss = new SDataRecordEntry();

        oEntryMoney.setConcept(moFieldConcept.getString());
        oEntryMoney.setDebit(mbParamIsMoneyIn ? moFieldValue.getDouble() : 0);
        oEntryMoney.setCredit(mbParamIsMoneyIn ? 0 : moFieldValue.getDouble());
        oEntryMoney.setExchangeRate(0);
        oEntryMoney.setExchangeRateSystem(0);
        oEntryMoney.setDebitCy(0);
        oEntryMoney.setCreditCy(0);
        oEntryMoney.setIsExchangeDifference(true);
        oEntryMoney.setFkAccountIdXXX(moParamRecord.getDbmsDataAccountCash().getFkAccountId());
        oEntryMoney.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        oEntryMoney.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        oEntryMoney.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);

        if (mbParamIsMoneyIn) {
            oEntryMoney.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_MI_ADJ[0]);
            oEntryMoney.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_MI_ADJ[1]);
        }
        else {
            oEntryMoney.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_MO_ADJ[0]);
            oEntryMoney.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_MO_ADJ[1]);
        }

        if (moParamRecord.getDbmsDataAccountCash().getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH) {
            oEntryMoney.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[0]);
            oEntryMoney.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[1]);
            oEntryMoney.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH[0]);
            oEntryMoney.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH[1]);
        }
        else {
            oEntryMoney.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[0]);
            oEntryMoney.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[1]);
            oEntryMoney.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK[0]);
            oEntryMoney.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK[1]);
        }

        oEntryMoney.setFkCurrencyId(moParamRecord.getDbmsDataAccountCash().getFkCurrencyId());
        oEntryMoney.setFkCompanyBranchId_n(moParamRecord.getDbmsDataAccountCash().getPkCompanyBranchId());
        oEntryMoney.setFkEntityId_n(moParamRecord.getDbmsDataAccountCash().getPkAccountCashId());
        oEntryMoney.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.FIN_ACC, new Object[] { oEntryMoney.getFkAccountIdXXX() }));
        oEntryMoney.setDbmsAccountComplement(moParamRecord.getDbmsDataAccountCash().getAuxEntity());
        oEntryMoney.setDbmsAccountingMoveSubclass(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL));
        oEntryMoney.setDbmsCurrencyKey(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.CFGU_CUR, new int[] { moParamRecord.getDbmsDataAccountCash().getFkCurrencyId() }, SLibConstants.DESCRIPTION_CODE));
        oEntryMoney.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        oEntryMoney.setFkUserEditId(mnFkUser > 0 ? miClient.getSession().getUser().getPkUserId() : SDataConstantsSys.USRX_USER_NA);

        oEntryGainLoss.setConcept(moFieldConcept.getString());
        oEntryGainLoss.setDebit(mbParamIsMoneyIn ? 0 : moFieldValue.getDouble());
        oEntryGainLoss.setCredit(mbParamIsMoneyIn ? moFieldValue.getDouble() : 0);
        oEntryGainLoss.setExchangeRate(1);
        oEntryGainLoss.setExchangeRateSystem(1);
        oEntryGainLoss.setDebitCy(mbParamIsMoneyIn ? 0 : moFieldValue.getDouble());
        oEntryGainLoss.setCreditCy(mbParamIsMoneyIn ? moFieldValue.getDouble() : 0);
        oEntryGainLoss.setFkAccountIdXXX(mbParamIsMoneyIn ? miClient.getSessionXXX().getParamsCompany().getFkAccountDifferenceIncomeId_n() :
            miClient.getSessionXXX().getParamsCompany().getFkAccountDifferenceExpenseId_n());
        oEntryGainLoss.setFkItemId_n(mbParamIsMoneyIn ? miClient.getSessionXXX().getParamsCompany().getFkItemDifferenceIncomeId_n() :
            miClient.getSessionXXX().getParamsCompany().getFkItemDifferenceExpenseId_n());
        oEntryGainLoss.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        oEntryGainLoss.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        oEntryGainLoss.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);

        if (mbParamIsMoneyIn) {
            oEntryGainLoss.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_MI_ADJ[0]);
            oEntryGainLoss.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_MI_ADJ[1]);
        }
        else {
            oEntryGainLoss.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_MO_ADJ[0]);
            oEntryGainLoss.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_MO_ADJ[1]);
        }

        oEntryGainLoss.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[0]);
        oEntryGainLoss.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[1]);
        oEntryGainLoss.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_NA[0]);
        oEntryGainLoss.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_NA[1]);
        oEntryGainLoss.setFkCurrencyId(miClient.getSessionXXX().getParamsErp().getFkCurrencyId());
        oEntryGainLoss.setFkCostCenterIdXXX_n(mbParamIsMoneyIn ? miClient.getSessionXXX().getParamsCompany().getFkCostCenterDifferenceIncomeId_n() :
            miClient.getSessionXXX().getParamsCompany().getFkCostCenterDifferenceExpenseId_n());
        oEntryGainLoss.setFkCompanyBranchId_n(moParamRecord.getDbmsDataAccountCash().getPkCompanyBranchId());
        oEntryGainLoss.setFkEntityId_n(moParamRecord.getDbmsDataAccountCash().getPkAccountCashId());
        oEntryGainLoss.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.FIN_ACC, new Object[] { mbParamIsMoneyIn ? miClient.getSessionXXX().getParamsCompany().getFkAccountDifferenceIncomeId_n() :
            miClient.getSessionXXX().getParamsCompany().getFkAccountDifferenceExpenseId_n() }));
        oEntryGainLoss.setDbmsAccountingMoveSubclass(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL));
        oEntryGainLoss.setDbmsCurrencyKey(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.CFGU_CUR, new int[] { miClient.getSessionXXX().getParamsErp().getFkCurrencyId() }, SLibConstants.DESCRIPTION_CODE));
        oEntryGainLoss.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        oEntryGainLoss.setFkUserEditId(mnFkUser > 0 ? miClient.getSession().getUser().getPkUserId() : SDataConstantsSys.USRX_USER_NA);

        return new SDataRecordEntry[] { oEntryMoney, oEntryGainLoss };
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JLabel jlAccountCash;
    private javax.swing.JLabel jlConcept;
    private javax.swing.JLabel jlCurrency;
    private javax.swing.JLabel jlValue;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpInput;
    private javax.swing.JTextField jtfAccountCash;
    private javax.swing.JTextField jtfCode;
    private javax.swing.JTextField jtfCompanyBranch;
    private javax.swing.JTextField jtfConcept;
    private javax.swing.JTextField jtfCurrency;
    private javax.swing.JTextField jtfValue;
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

        moRecordEntry = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        jtfCompanyBranch.setText("");
        jtfAccountCash.setText("");
        jtfCode.setText("");
    }

    @Override
    public void formRefreshCatalogues() {

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
        SDataRecord oRecord = new SDataRecord();
        SDataRecordEntry oEntries[] = null;

        oEntries = prepareRecordEntries();

        oRecord.getDbmsRecordEntries().add(oEntries[0]);
        oRecord.getDbmsRecordEntries().add(oEntries[1]);

        return oRecord;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        if (type == SDataConstants.FIN_REC) {
            moParamRecord = (SDataRecord) value;
        }
        else if (type == SDataConstantsSys.FINS_CT_SYS_MOV_CASH) {
            mbParamIsMoneyIn = (Boolean) value;
        }
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

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
        }
    }

    @Override
    public void focusGained(java.awt.event.FocusEvent e) {

    }

    @Override
    public void focusLost(java.awt.event.FocusEvent e) {

    }
}
