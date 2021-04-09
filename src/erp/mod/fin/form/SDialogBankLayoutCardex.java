/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.form;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.table.STableConstants;
import erp.mcfg.data.SDataCurrency;
import erp.mfin.data.SDataAccountCash;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SDbBankLayout;
import erp.mod.fin.db.SLayoutBankPaymentRow;
import erp.mod.fin.db.SLayoutBankRecord;
import erp.mod.fin.db.SLayoutBankRow;
import erp.mod.fin.db.SLayoutBankXmlRow;
import erp.mod.fin.db.SMoney;
import erp.mtrn.data.SDataDps;
import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
 * @author Uriel Castañeda, Alfredo Pérez, Sergio Flores, Isabel Servín
 */
public class SDialogBankLayoutCardex extends SBeanFormDialog implements ListSelectionListener {
    
    private SDbBankLayout moBankLayout;
    private SGridPaneForm moGridPayments;

    /**
     * Creates new form SDialogBankLayoutCardex
     * @param client
     * @param formSubtype
     * @param title
     */
    public SDialogBankLayoutCardex(SGuiClient client, int formSubtype, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.FIN_LAY_BANK, formSubtype, title);
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jpBenefit = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jlFolio = new javax.swing.JLabel();
        jtfRegistryKey = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        jtfDate = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jlPkBankLayoutTypeId = new javax.swing.JLabel();
        jtfLayoutType = new javax.swing.JTextField();
        jlAccountDebit = new javax.swing.JLabel();
        jtfBankAcount = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jlBankLayoutCurrency = new javax.swing.JLabel();
        jtfBankLayoutCurrency = new javax.swing.JTextField();
        jlDpsCurrency = new javax.swing.JLabel();
        jtfDpsCurrency = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jpSettings = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jtfRows = new javax.swing.JTextField();
        jlDummy1 = new javax.swing.JLabel();
        jlBalanceTot = new javax.swing.JLabel();
        moDecBalanceTot = new sa.lib.gui.bean.SBeanFieldDecimal();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jpBenefit.setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro"));
        jPanel4.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFolio.setText("Folio: ");
        jlFolio.setPreferredSize(new java.awt.Dimension(100, 14));
        jPanel7.add(jlFolio);

        jtfRegistryKey.setMinimumSize(new java.awt.Dimension(100, 23));
        jtfRegistryKey.setName(""); // NOI18N
        jtfRegistryKey.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jtfRegistryKey);

        jPanel6.setAutoscrolls(true);
        jPanel6.setPreferredSize(new java.awt.Dimension(96, 23));
        jPanel7.add(jPanel6);

        jlDate.setText("Fecha apliación:");
        jlDate.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel7.add(jlDate);

        jtfDate.setEditable(false);
        jtfDate.setText("dd/mm/yyyy");
        jtfDate.setFocusable(false);
        jtfDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jtfDate);

        jPanel4.add(jPanel7);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPkBankLayoutTypeId.setText("Tipo layout: ");
        jlPkBankLayoutTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel2.add(jlPkBankLayoutTypeId);

        jtfLayoutType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel2.add(jtfLayoutType);

        jlAccountDebit.setText("Cuenta bancaria: ");
        jlAccountDebit.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel2.add(jlAccountDebit);

        jtfBankAcount.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel2.add(jtfBankAcount);

        jPanel4.add(jPanel2);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBankLayoutCurrency.setText("Moneda origen: ");
        jlBankLayoutCurrency.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlBankLayoutCurrency);

        jtfBankLayoutCurrency.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel8.add(jtfBankLayoutCurrency);

        jlDpsCurrency.setText("Moneda documento:");
        jlDpsCurrency.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel8.add(jlDpsCurrency);

        jtfDpsCurrency.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel8.add(jtfDpsCurrency);

        jPanel4.add(jPanel8);

        jpBenefit.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel1.add(jpBenefit, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jpSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle de pagos:"));
        jpSettings.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jtfRows.setEditable(false);
        jtfRows.setText("000,000/000,000");
        jtfRows.setToolTipText("Renglón actual");
        jtfRows.setFocusable(false);
        jtfRows.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jtfRows);

        jlDummy1.setPreferredSize(new java.awt.Dimension(630, 23));
        jPanel5.add(jlDummy1);

        jlBalanceTot.setText("Total layout:");
        jlBalanceTot.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel5.add(jlBalanceTot);
        jPanel5.add(moDecBalanceTot);

        jpSettings.add(jPanel5, java.awt.BorderLayout.SOUTH);

        jPanel3.add(jpSettings, java.awt.BorderLayout.CENTER);
        jpSettings.getAccessibleContext().setAccessibleDescription("");

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        dispose();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel jlAccountDebit;
    private javax.swing.JLabel jlBalanceTot;
    private javax.swing.JLabel jlBankLayoutCurrency;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlDpsCurrency;
    private javax.swing.JLabel jlDummy1;
    private javax.swing.JLabel jlFolio;
    private javax.swing.JLabel jlPkBankLayoutTypeId;
    private javax.swing.JPanel jpBenefit;
    private javax.swing.JPanel jpSettings;
    private javax.swing.JTextField jtfBankAcount;
    private javax.swing.JTextField jtfBankLayoutCurrency;
    private javax.swing.JTextField jtfDate;
    private javax.swing.JTextField jtfDpsCurrency;
    private javax.swing.JTextField jtfLayoutType;
    private javax.swing.JTextField jtfRegistryKey;
    private javax.swing.JTextField jtfRows;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecBalanceTot;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 960, 600);
        
        jbSave.setText("Cerrar");
        jbCancel.setEnabled(false);

        jtfRegistryKey.setText("");
        jtfRegistryKey.setEditable(false);
        jtfRegistryKey.setFocusable(false);
        jtfLayoutType.setText("");
        jtfLayoutType.setEditable(false);
        jtfLayoutType.setFocusable(false);
        jtfBankAcount.setText("");
        jtfBankAcount.setEditable(false);
        jtfBankAcount.setFocusable(false);
        jtfBankLayoutCurrency.setText("");
        jtfBankLayoutCurrency.setEditable(false);
        jtfBankLayoutCurrency.setEditable(false);
        jtfDpsCurrency.setText("");
        jtfDpsCurrency.setEditable(false);
        jtfDpsCurrency.setEditable(false);
        
        jtfDate.setText("");
        jtfDate.setEditable(false);
        jtfDate.setFocusable(false);
       
        moDecBalanceTot.setDecimalSettings(SGuiUtils.getLabelName(jlBalanceTot), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        moDecBalanceTot.resetField();
        moDecBalanceTot.setEditable(false);
        
        // var for grid consulting
        moGridPayments = new SGridPaneForm(miClient, SModConsts.FIN_LAY_BANK, SModSysConsts.FINX_LAY_BANK_QRY, "Detalle de pagos") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();
                
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Proveedor", 200));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Clave proveedor", 50));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "Tipo doc", 50));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Folio doc", 90));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha doc"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Sucursal empresa", STableConstants.WIDTH_CODE_COB));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Pago mon cta $", STableConstants.WIDTH_VALUE_2X));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda cta"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_EXC_RATE, "TC", 50));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Cuenta/Convenio", 125));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Referencia", 125));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Concepto", 100));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_PER, "Período póliza"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CO, "Centro contable"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CO, "Sucursal empresa"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Folio póliza", 65));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha póliza"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Observaciones", 100));

                return gridColumnsForm;
            }
        };

        jpSettings.add(moGridPayments, BorderLayout.CENTER);
        
        addAllListeners();
    }
        
    private void renderAccountSettings(final int[] keyAccountDebit) {
        SDataAccountCash moDataAccountCash =  null;
        
        if (keyAccountDebit.length > 0) {
            moDataAccountCash = (SDataAccountCash) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.FIN_ACC_CASH, keyAccountDebit, SLibConstants.EXEC_MODE_SILENT);
            jtfBankAcount.setText(moDataAccountCash.getDbmsBizPartnerBranchBankAccount().getBankAccount());
        }
    }
      
    private void loadLayout() throws Exception {
        int numberDocs = 0;
        double total = 0d; 
        boolean found = false;
        ArrayList<SGridRow> gridRows = new ArrayList<>();
        
        jtfRows.setText("0/0");
        
        moBankLayout.parseBankLayoutXml(miClient, false);
        
        SDataCurrency bankCur = (SDataCurrency) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.CFGU_CUR, new int[] { moBankLayout.getXtaBankCurrencyId() }, SLibConstants.EXEC_MODE_SILENT);
        
        jtfRegistryKey.setText(SLibUtils.textKey(moBankLayout.getPrimaryKey()));
        jtfDate.setText(SLibUtils.DateFormatDate.format(moBankLayout.getDateLayout()));
        jtfLayoutType.setText(moBankLayout.getXtaBankLayoutType());
        jtfBankLayoutCurrency.setText(bankCur.getCurrency());
        jtfDpsCurrency.setText(((SDataCurrency) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.CFGU_CUR, new int[] { moBankLayout.getFkDpsCurrencyId() }, SLibConstants.EXEC_MODE_SILENT)).getCurrency());
        
        renderAccountSettings(new int[] { moBankLayout.getFkBankCompanyBranchId(), moBankLayout.getFkBankAccountCashId() });
       
        for (SLayoutBankXmlRow xmlRow : moBankLayout.getAuxLayoutBankXmlRows()) {
            SLayoutBankRow layoutBankRow = new SLayoutBankRow(miClient, SLayoutBankRow.MODE_DIALOG_CARDEX);
            
            String bank_acc = "";
            String sql = "SELECT acc_num FROM erp.bpsu_bank_acc WHERE id_bpb = " + xmlRow.getBizPartnerBranchId() + " AND id_bank_acc = " + xmlRow.getBizPartnerBranchAccountId() + ";";
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                bank_acc = resultSet.getString(1);
            }
            
            layoutBankRow.setBizPartnerId(xmlRow.getBizPartnerId());
            layoutBankRow.setDpsYearId(xmlRow.getDpsYearId());
            layoutBankRow.setDpsDocId(xmlRow.getDpsDocId());
            layoutBankRow.setAgreement(xmlRow.getAgreement());
            layoutBankRow.setAgreementReference(xmlRow.getAgreementReference());
            layoutBankRow.setConceptCie(xmlRow.getConceptCie());
            layoutBankRow.setBankCurrencyKey(bankCur.getKey());
            
            SDataDps dps = (SDataDps) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_DPS, new int[] {xmlRow.getDpsYearId(), xmlRow.getDpsDocId() }, SLibConstants.EXEC_MODE_SILENT);
            
            if (dps != null) {
                layoutBankRow.setDpsCompanyBranchCode(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.BPSU_BPB, new int[] { dps.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
                layoutBankRow.setDpsType(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.TRNU_TP_DPS, dps.getDpsTypeKey(), SLibConstants.DESCRIPTION_CODE));
                layoutBankRow.setDpsNumber(dps.getDpsNumber());
                layoutBankRow.setDpsDate(dps.getDate());
            }
            layoutBankRow.setMoneyPayment(new SMoney(miClient.getSession(), xmlRow.getAmountPayed(), xmlRow.getCurrencyId(), xmlRow.getExchangeRate()));
            layoutBankRow.setObservations(xmlRow.getObservations());
            
            total = total + layoutBankRow.getMoneyPayment().getOriginalAmount();
            for (SLayoutBankPaymentRow layoutBankPaymentRow : moBankLayout.getAuxLayoutBankPaymentRows()) {
                found = false;
                
                if (moBankLayout.getXtaBankPaymentTypeId() == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                    if (layoutBankRow.getAgreement().equals(layoutBankPaymentRow.getAgreement()) && layoutBankRow.getAgreementReference().equals(layoutBankPaymentRow.getAgreementReference())) {
                        found = true;
                    }
                }
                else {
                    if (layoutBankRow.getBizPartnerId() == layoutBankPaymentRow.getBizPartnerId()) {
                        found = true;
                    }
                }
                
                if (found) {
                    layoutBankRow.setBizPartner(layoutBankPaymentRow.getBizPartner());
                    layoutBankRow.setBizPartnerKey(layoutBankPaymentRow.getBizPartnerKey());
                    layoutBankRow.setPayerAccountCurrencyKey(layoutBankPaymentRow.getPayerAccountCurrencyKey());
                    layoutBankRow.setBeneficiaryAccountNumber(bank_acc);
                    layoutBankRow.setAgreement(layoutBankPaymentRow.getAgreement());
                    layoutBankRow.setAgreementReference(layoutBankPaymentRow.getAgreementReference());
                    layoutBankRow.setConceptCie(layoutBankPaymentRow.getAgreementConceptCie());
                    
                    SLayoutBankRecord layoutBankRecord = new SLayoutBankRecord(layoutBankPaymentRow.getLayoutBankRecordKey());
                    layoutBankRecord.setBookkeepingCenterCode(layoutBankPaymentRow.getRecordBkc());
                    layoutBankRecord.setCompanyBranchCode(layoutBankPaymentRow.getRecordCob());
                    layoutBankRecord.setDate(layoutBankPaymentRow.getRecordDate());
                    layoutBankRow.setLayoutBankRecord(layoutBankRecord);
                    break;
                }
            }
           
            gridRows.add(layoutBankRow);
            numberDocs++;
        }
               
        moDecBalanceTot.setValue(total);
        
        moGridPayments.populateGrid(new Vector<>(gridRows));
        moGridPayments.createGridColumns();
        
        jtfRows.setText(SLibUtils.DecimalFormatInteger.format(numberDocs) + "/" + SLibUtils.DecimalFormatInteger.format(moGridPayments.getModel().getRowCount()));
        
        if (moGridPayments.getTable().getRowCount() > 0) {
            moGridPayments.renderGridRows();
            moGridPayments.setSelectedGridRow(0);
        }
    }
    
    public void setFormParams(final int[] bankLayoutPk) throws Exception {
        moBankLayout = (SDbBankLayout) miClient.getSession().readRegistry(SModConsts.FIN_LAY_BANK, bankLayoutPk);
        loadLayout();
    }
    
    @Override
    public void addAllListeners() {
    }

    @Override
    public void removeAllListeners() {
    }

    @Override
    public void reloadCatalogues() {
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {   
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        return validation;
    }

    @Override
    public void setValue(final int type, final Object value) {
    }

    @Override
    public Object getValue(final int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
