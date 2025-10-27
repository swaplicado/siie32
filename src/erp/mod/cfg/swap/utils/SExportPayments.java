/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import erp.mbps.data.SDataBizPartnerBranchBankAccount;
import erp.mod.SModSysConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.fin.db.SDbPayment;
import erp.mod.fin.db.SDbPaymentEntry;
import erp.mtrn.data.SThinDps;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Isabel Servín. Sergio Flores
 */
public class SExportPayments extends Thread {
    
    private final SGuiClient miClient;
    private final SDbPayment moPayment;
    private final int mnFlowModel;
    private final int mnPriority;
    private final String msAuthNotes;
    
    private String msError;
    
    public SExportPayments(SGuiClient client, SDbPayment payment, int flowModel, int priority, String notes) {
        miClient = client;
        moPayment = payment;
        mnFlowModel = flowModel;
        mnPriority = priority;
        msAuthNotes = notes;
        
        setDaemon(true);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            updatePaymentAuthData();
            Object[] sendRequest = SAuthorizationUtils.sendPaymentFilesToCloud(miClient, moPayment);
            boolean sendPaymentFilesOk = (boolean) sendRequest[0];
            ArrayList<SExportDataFile> expDataFiles = (ArrayList<SExportDataFile>) sendRequest[1];
            if (sendPaymentFilesOk) {
                System.out.println("Subida al google cloud storage con éxito");
                
                String requestBody = createJSONrequestBody(expDataFiles); // creación del JSON que se envía a la app web 
                
                if (SAuthorizationUtils.computePaymentRequest(miClient.getSession(), SSwapConsts.CFG_OBJ_TXN_PUR_PAY, requestBody)) {
                    moPayment.updatePaymentStatus(miClient.getSession(), SModSysConsts.FINS_ST_PAY_IN_AUTH);
                }
                else {
                    miClient.showMsgBoxInformation("No se puedo comenzar el proceso de autorización, intente nuevamente.");
                }
            }
            else {
                System.out.println("Error al subir archivos al google cloud storage");
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void updatePaymentAuthData() throws Exception {
        moPayment.setPriority(mnPriority);
        moPayment.setNotesAuthorization(msAuthNotes);
        moPayment.updateAuthorizationData(miClient.getSession());
    }
    
    private String createJSONrequestBody(ArrayList<SExportDataFile> expDataFiles) throws Exception {
        String requestBody = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            SRequestPaymentsBody paymentBody = new SRequestPaymentsBody();
            ArrayList<SRequestPaymentsBody.Payment> arrPayments = new ArrayList<>();
            SRequestPaymentsBody.Payment payments = new SRequestPaymentsBody.Payment();
            payments.payment = createExportDataPayment();
            payments.entries = createExportDataPaymentEntry();
            payments.files = expDataFiles.toArray(new SExportDataFile[0]);
            arrPayments.add(payments);
            paymentBody.payments = arrPayments.toArray(new SRequestPaymentsBody.Payment[0]);
            requestBody = mapper.writeValueAsString(paymentBody);
        }
        catch (Exception e) {
            throw new Exception("Ocurrio un error al crear el JSON requestBody. " + e.getMessage());
        }
        return requestBody;
    }
    
    private SExportDataPayment createExportDataPayment() throws Exception {
        SExportDataPayment payment = new SExportDataPayment();
        
        SDataBizPartnerBranchBankAccount benef = 
                getBranchBankAcc(new int[] { moPayment.getFkBeneficiaryBankBizParterBranchId_n(), moPayment.getFkBeneficiaryBankAccountCashId_n()});
        
        payment.company = miClient.getSession().getConfigCompany().getCompanyId();
        payment.payment_id = moPayment.getPkPaymentId();
        payment.functional_area = moPayment.getFkFunctionalAreaId();
        payment.functional_area = moPayment.getFkFunctionalSubareaId();
        payment.benef = moPayment.getFkBeneficiaryId();
        payment.series = moPayment.getSeries();
        payment.number = moPayment.getNumber() + "";
        payment.app_date = SLibUtils.DbmsDateFormatDate.format(moPayment.getDateApplication());
        payment.req_date = SLibUtils.DbmsDateFormatDate.format(moPayment.getDateRequired());
        payment.sched_date_n = moPayment.getDateSchedule_n() == null ? null : SLibUtils.DbmsDateFormatDate.format(moPayment.getDateSchedule_n());
        payment.exec_date_n = moPayment.getDateExecution_n() == null ? null : SLibUtils.DbmsDateFormatDate.format(moPayment.getDateExecution_n());
        payment.currency = moPayment.getDbmsCurrency().getKey();
        payment.amount = SExportUtils.FormatStdAmount.format(SLibUtils.roundAmount(moPayment.getPaymentCy()));
        payment.exchange_rate_app = SExportUtils.FormatPayExchangeRate.format(SLibUtils.round(moPayment.getPaymentExchangeRateApplication(), SExportUtils.DECS_PAY_EXC_RATE));
        payment.amount_loc_app = SExportUtils.FormatStdAmount.format(SLibUtils.roundAmount(moPayment.getPaymentApplication()));
        payment.exchange_rate_exec = SExportUtils.FormatPayExchangeRate.format(SLibUtils.round(moPayment.getPaymentExchangeRate(), SExportUtils.DECS_PAY_EXC_RATE));
        payment.amount_loc_exec = SExportUtils.FormatStdAmount.format(SLibUtils.roundAmount(moPayment.getPayment()));
        payment.payment_way = moPayment.getPaymentWay();
        payment.priority = moPayment.getPriority();
        payment.notes = moPayment.getNotes();
        payment.notes_authz = moPayment.getNotesAuthorization();
        payment.is_receipt_payment_req = moPayment.isReceiptPaymentRequired() ? 1 : 0;
        payment.payment_status = SModSysConsts.FINS_ST_PAY_IN_AUTH;
        payment.authz_authorization_id = SModSysConsts.TRNS_ST_DPS_AUTHORN_NA;
        payment.paying_bank = "";
        payment.paying_bank_fiscal_id = "";
        payment.paying_account = "";
        payment.benef_bank = benef.getDbmsBank() == null ? "" : benef.getDbmsBank();
        payment.benef_bank_fiscal_id = benef.getDbmsBankFiscalId() == null ? "" : benef.getDbmsBankFiscalId();
        payment.benef_account = benef.getBankAccountNumber();
        payment.sched_user = null;
        payment.exec_user = null;
        payment.sched_at = null;
        payment.exec_at = null;
        payment.is_deleted = moPayment.isDeleted() ? 1 : 0;
        payment.user_id = miClient.getSession().getUser().getPkUserId(); 
        payment.flow = mnFlowModel;
        
        return payment;
    }
    
    private SExportDataPaymentEntry[] createExportDataPaymentEntry() throws Exception {
        ArrayList<SExportDataPaymentEntry> entries = new ArrayList<>();
        for (SDbPaymentEntry paymentEty : moPayment.getChildEntries()) {
            SExportDataPaymentEntry entry = new SExportDataPaymentEntry();
            entry.entry_type = paymentEty.getEntryType();
            entry.amount = SExportUtils.FormatStdAmount.format(SLibUtils.roundAmount(paymentEty.getEntryPaymentCy()));
            entry.amount_loc_app = SExportUtils.FormatStdAmount.format(SLibUtils.roundAmount(paymentEty.getEntryPaymentApplication()));
            entry.entry_currency = paymentEty.getEntryCurrency().getKey();
            entry.conv_rate_app = SExportUtils.FormatPayConversionRate.format(SLibUtils.round(paymentEty.getConversionRateApplication(), SExportUtils.DECS_PAY_CONV_RATE));
            entry.entry_amount_app = SExportUtils.FormatStdAmount.format(SLibUtils.roundAmount(paymentEty.getDestinyPaymentApplicationEntryCy()));
            entry.amount_loc_exec = SExportUtils.FormatStdAmount.format(SLibUtils.roundAmount(paymentEty.getEntryPayment()));
            entry.conv_rate_exec = SExportUtils.FormatPayConversionRate.format(SLibUtils.round(paymentEty.getConversionRate(), SExportUtils.DECS_PAY_CONV_RATE));
            entry.entry_amount_exec = SExportUtils.FormatStdAmount.format(SLibUtils.roundAmount(paymentEty.getDestinyPaymentEntryCy()));
            entry.installment = paymentEty.getInstallment();
            entry.document_bal_prev_app = SExportUtils.FormatStdAmount.format(SLibUtils.roundAmount(paymentEty.getDocBalancePreviousApplicationCy()));
            entry.document_bal_unpd_app = SExportUtils.FormatStdAmount.format(SLibUtils.roundAmount(paymentEty.getDocBalanceUnpaidApplicationCy_r()));
            entry.document_bal_prev_exec = SExportUtils.FormatStdAmount.format(SLibUtils.roundAmount(paymentEty.getDocBalancePreviousCy()));
            entry.document_bal_unpd_exec = SExportUtils.FormatStdAmount.format(SLibUtils.roundAmount(paymentEty.getDocBalanceUnpaidCy_r()));
            if (entry.entry_type.equals(SDbPaymentEntry.ENTRY_TYPE_PAYMENT)) {
                int docId = getDocumentId(paymentEty);
                if (docId != 0) {
                    entry.document_n_id = docId;
                }
                else {
                    SThinDps dps = new SThinDps();
                    dps.read(new int[] { paymentEty.getFkDocYearId_n(), paymentEty.getFkDocDocId_n() }, miClient.getSession().getStatement());
                    entry.document_n_id = null;
                    entry.document_uuid = dps.getThinCfd() == null ? "" : dps.getThinCfd().getUuid();
                    entry.document_folio = dps.getDpsNumber();
                    entry.document_date = SLibUtils.DbmsDateFormatDate.format(dps.getDate());

                    entry.document_currency = dps.getDbmsCurrencyCode();
                    entry.document_amount = SExportUtils.FormatStdAmount.format(SLibUtils.roundAmount(dps.getTotalCy_r()));
                }
            }
            entries.add(entry);
        }
        return entries.toArray(new SExportDataPaymentEntry[0]);
    }

    private SDataBizPartnerBranchBankAccount getBranchBankAcc(int[] pk) {
        SDataBizPartnerBranchBankAccount ba = new SDataBizPartnerBranchBankAccount();
        try {
            ba.read(pk, miClient.getSession().getStatement());
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
        return ba;
    }
    
    private int getDocumentId(SDbPaymentEntry paymentEty) {
        int docId = 0;
        try {
            String sql = "SELECT ext_data_id FROM trn_swap_data_prc "
                    + "WHERE fk_dps_year_n = " + paymentEty.getFkDocYearId_n() + " "
                    + "AND fk_dps_doc_n = " + paymentEty.getFkDocDocId_n() + " ";
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                docId = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {
            miClient.showMsgBoxError(e.getMessage());
        }
        return docId;
    }
}
