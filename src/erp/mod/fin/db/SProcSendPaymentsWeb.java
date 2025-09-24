/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.data.SDataConstantsSys;
import erp.mbps.data.SDataBizPartnerBranchBankAccount;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModSysConsts;
import erp.mod.cfg.swap.SHttpConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.utils.SExportDataFile;
import erp.mod.cfg.swap.utils.SExportDataPayment;
import erp.mod.cfg.swap.utils.SExportDataPaymentEntry;
import erp.mod.cfg.swap.utils.SExportUtils;
import erp.mod.cfg.swap.utils.SRequestPaymentsBody;
import erp.mod.cfg.utils.SAuthJsonUtils;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mtrn.data.SThinDps;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Isabel Servín
 */
public class SProcSendPaymentsWeb extends Thread {
    
    private final int TIME_180_SEC = 180 * 1000; // 180 segundos en milisegundos
    private final DecimalFormat amountFormat = new DecimalFormat("#0.00");
    private final DecimalFormat excRateFormat = new DecimalFormat("#0.0000");
    private final int excRateDecimals = 4;
    
    private final SGuiClient miClient;
    private final SDbPayment moPayment;
    
    private String msError;
    
    public SProcSendPaymentsWeb(SGuiClient client, SDbPayment payment) {
        miClient = client;
        moPayment = payment;
        
        setDaemon(true);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            Object[] sendRequest = SAuthorizationUtils.sendPaymentFilesToCloud(miClient, moPayment);
            boolean sendPaymentFilesOk = (boolean) sendRequest[0];
            ArrayList<SExportDataFile> expDataFiles = (ArrayList<SExportDataFile>) sendRequest[1];
            if (sendPaymentFilesOk) {
                System.out.println("Subida al google cloud storage con éxito");
                
                String requestBody = createJSONrequestBody(expDataFiles); // creación del JSON que se envía a la app web 
                
                if (computeRequest(requestBody)) {
                    moPayment.updatePaymentStatus(miClient.getSession(), SModSysConsts.FINS_ST_PAY_PRC_AUTH);
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
    
    private String createJSONrequestBody(ArrayList<SExportDataFile> expDataFiles) throws Exception {
        String requestBody = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            SRequestPaymentsBody paymentBody = new SRequestPaymentsBody();
            ArrayList<SRequestPaymentsBody.Payments> arrPayments = new ArrayList<>();
            SRequestPaymentsBody.Payments payments = new SRequestPaymentsBody.Payments();
            payments.payment = createExportDataPayment();
            payments.entries = createExportDataPaymentEntry();
            payments.files = expDataFiles.toArray(new SExportDataFile[0]);
            arrPayments.add(payments);
            paymentBody.payments = arrPayments.toArray(new SRequestPaymentsBody.Payments[0]);
            requestBody = mapper.writeValueAsString(paymentBody);
            System.out.println("");
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
        payment.pay_id = moPayment.getPkPaymentId();
        payment.functional_area = moPayment.getFkFunctionalAreaId();
        payment.benef = moPayment.getFkBeneficiaryId();
        payment.series = moPayment.getSeries();
        payment.number = moPayment.getNumber() + "";
        payment.app_date = SLibUtils.DbmsDateFormatDate.format(moPayment.getDateApplication());
        payment.req_date = SLibUtils.DbmsDateFormatDate.format(moPayment.getDateRequired());
        payment.sched_date_n = moPayment.getDateSchedule_n() == null ? null : SLibUtils.DbmsDateFormatDate.format(moPayment.getDateSchedule_n());
        payment.exec_date_n = moPayment.getDateExecution_n() == null ? null : SLibUtils.DbmsDateFormatDate.format(moPayment.getDateExecution_n());
        payment.currency = moPayment.getDbmsCurrency().getKey();
        payment.amount = amountFormat.format(SLibUtils.roundAmount(moPayment.getPaymentCy()));
        payment.exchange_rate_app = excRateFormat.format(SLibUtils.round(moPayment.getPaymentExchangeRateApplication(), excRateDecimals));
        payment.amount_loc_app = amountFormat.format(SLibUtils.roundAmount(moPayment.getPaymentApplication()));
        payment.exchange_rate_exec = excRateFormat.format(SLibUtils.round(moPayment.getPaymentExchangeRate(), excRateDecimals));
        payment.amount_loc_exec = amountFormat.format(SLibUtils.roundAmount(moPayment.getPayment()));
        payment.payment_way = moPayment.getPaymentWay();
        payment.notes = moPayment.getNotes();
        payment.payment_status = moPayment.getFkStatusPaymentId();
        payment.authz_authorization_id = SModSysConsts.TRNS_ST_DPS_AUTHORN_NA;
        payment.paying_bank = "";
        payment.paying_bank_fiscal_id = "";
        payment.paying_account = "";
        payment.benef_bank = benef.getDbmsBank() == null ? "" : benef.getDbmsBank();
        payment.benef_bank_fiscal_id = benef.getDbmsBankFiscalId() == null ? "" : benef.getDbmsBankFiscalId();
        payment.benef_account = benef.getBankAccountNumber();
        payment.sched_user = moPayment.getFkUserScheduledId();
        payment.exec_user = moPayment.getFkUserExecutedId();
        payment.sched_at = SLibUtils.DbmsDateFormatDate.format(moPayment.getTsUserScheduledId());
        payment.exec_at = SLibUtils.DbmsDateFormatDate.format(moPayment.getTsUserExecuted());
        payment.is_deleted = moPayment.isDeleted() ? 1 : 0;
        payment.user_id = miClient.getSession().getUser().getPkUserId(); 
        
        return payment;
    }
    
    private SExportDataPaymentEntry[] createExportDataPaymentEntry() throws Exception {
        ArrayList<SExportDataPaymentEntry> entries = new ArrayList<>();
        for (SDbPaymentEntry paymentEty : moPayment.getChildEntries()) {
            SExportDataPaymentEntry entry = new SExportDataPaymentEntry();
            entry.entry_type = paymentEty.getEntryType();
            entry.amount = amountFormat.format(SLibUtils.roundAmount(paymentEty.getEntryPaymentCy()));
            entry.amount_loc_app = amountFormat.format(SLibUtils.roundAmount(paymentEty.getEntryPaymentApplication()));
            entry.entry_currency = paymentEty.moEntryCurrency.getKey();
            entry.conv_rate_app = excRateFormat.format(SLibUtils.round(paymentEty.getConversionRateApplication(), excRateDecimals));
            entry.entry_amount_app = amountFormat.format(SLibUtils.roundAmount(paymentEty.getDestinyPaymentApplicationEntryCy()));
            entry.amount_loc_exec = amountFormat.format(SLibUtils.roundAmount(paymentEty.getEntryPayment()));
            entry.conv_rate_exec = excRateFormat.format(SLibUtils.round(paymentEty.getConversionRate(), excRateDecimals));
            entry.entry_amount_exec = amountFormat.format(SLibUtils.roundAmount(paymentEty.getDestinyPaymentEntryCy()));
            entry.installment = paymentEty.getInstallment();
            entry.document_bal_prev_app = amountFormat.format(SLibUtils.roundAmount(paymentEty.getDocBalancePreviousApplicationCy()));
            entry.document_bal_unpd_app = amountFormat.format(SLibUtils.roundAmount(paymentEty.getDocBalanceUnpaidApplicationCy_r()));
            entry.document_bal_prev_exec = amountFormat.format(SLibUtils.roundAmount(paymentEty.getDocBalancePreviousCy()));
            entry.document_bal_unpd_exec = amountFormat.format(SLibUtils.roundAmount(paymentEty.getDocBalanceUnpaidCy_r()));
            if (entry.entry_type.equals(SDbPaymentEntry.ENTRY_TYPE_PAYMENT)) {
                int docId = getDocumentId(paymentEty);
                if (docId != 0) {
                    entry.document_id_n = docId;
                }
                else {
                    SThinDps dps = new SThinDps();
                    dps.read(new int[] { paymentEty.getFkDocYearId_n(), paymentEty.getFkDocDocId_n() }, miClient.getSession().getStatement());
                    entry.document_id_n = null;
                    entry.document_uuid = dps.getThinCfd() == null ? "" : dps.getThinCfd().getUuid();
                    entry.document_folio = dps.getDpsNumber();
                    entry.document_date = SLibUtils.DbmsDateFormatDate.format(dps.getDate());
                    entry.document_currency_id = dps.getDbmsCurrencyKey();
                    entry.document_amount = amountFormat.format(SLibUtils.roundAmount(dps.getTotalCy_r()));
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
                    + "WHERE fid_dps_year_n = " + paymentEty.getFkDocYearId_n() + " "
                    + "AND fid_dps_doc_n = " + paymentEty.getFkDocDocId_n() + " ";
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
    
    private boolean computeRequest(String requestBody) throws Exception {
        boolean sent = false;
        String cfgParamKey = SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG;
        String jsonBaseKey = SSwapConsts.CFG_OBJ_TXN_SRV;
        String jsonConfigKey = SSwapConsts.CFG_OBJ_TXN_PUR_PAY;
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode config = mapper.readTree(SCfgUtils.getParamValue(miClient.getSession().getStatement(), cfgParamKey));
        
        String syncUrl = SAuthJsonUtils.getValueOfElementAsText(config, jsonBaseKey, SSwapConsts.CFG_ATT_URL);
        String syncToken = SAuthJsonUtils.getValueOfElementAsText(config, jsonBaseKey, SSwapConsts.CFG_ATT_TOKEN);
        String syncApiKey = SAuthJsonUtils.getValueOfElementAsText(config, jsonBaseKey, SSwapConsts.CFG_ATT_API_KEY);
        
        // Recuperar la configuración del servicio:
        
        syncUrl += SAuthJsonUtils.getValueOfElementAsText(config, jsonConfigKey, SSwapConsts.CFG_ATT_URL); // complementar la URL
        
        String responseBody = SExportUtils.requestSwapService("", syncUrl, SHttpConsts.METHOD_POST, requestBody, syncToken, syncApiKey, TIME_180_SEC);
       
        JsonNode responseJson = new ObjectMapper().readTree(responseBody);
        
        if (SAuthJsonUtils.containsElement(responseJson, "", "results")) {
            JsonNode results = responseJson.path("results");
            if (results.isArray()) {
                for (JsonNode result : results) {
                    if (result.has("status_code")) {
                        if (result.path("status_code").asInt() == 200){
                            sent = true; 
                        }
                    }
                }
            }
        }
        return sent;
    }
}
