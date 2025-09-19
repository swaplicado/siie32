/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.mbps.data.SDataBizPartnerBranchBankAccount;
import erp.mod.SModSysConsts;
import erp.mod.cfg.swap.utils.SExportDataPayment;
import erp.mod.cfg.swap.utils.SExportDataPaymentEntry;
import erp.mod.cfg.swap.utils.SRequestPaymentsBody;
import erp.mod.cfg.utils.SAuthorizationUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Isabel Servín
 */
public class SProcSendPaymentsWeb extends Thread {
    
    private final SGuiClient miClient;
    private final SDbPayment moPayment;
    
    private String msError;
    
    public SProcSendPaymentsWeb(SGuiClient client, SDbPayment payment) {
        miClient = client;
        moPayment = payment;
        
        setDaemon(true);
    }
    
    @Override
    public void run() {
        try {
            if (SAuthorizationUtils.sendPaymentFilesToCloud(miClient, moPayment)) {
                System.out.println("Subida al google cloud storage con éxito");
                ObjectMapper mapper = new ObjectMapper();
                SRequestPaymentsBody paymentBody = new SRequestPaymentsBody();
                SExportDataPayment payment = createExportDataPayment();
                paymentBody.payments = new SExportDataPayment[]{payment};
                String requestBody = mapper.writeValueAsString(paymentBody);
            }
            else {
                System.out.println("Error al ssubir archivos al google cloud storage");
            }
        }
        catch (JsonProcessingException e) {
            miClient.showMsgBoxError("Ocurrio un error");
        }
        catch (Exception e) {
            miClient.showMsgBoxError("Ocurrio un error");
        }
    }

    private SExportDataPayment createExportDataPayment() {
        SExportDataPayment payment = new SExportDataPayment();
        
        SDataBizPartnerBranchBankAccount paying = getBranchBankAccByAccCash();
        SDataBizPartnerBranchBankAccount benef = 
                getBranchBankAcc(new int[] { moPayment.getFkBeneficiaryBankBizParterBranchId_n(), moPayment.getFkBeneficiaryBankAccountCashId_n()});
        
        payment.company = miClient.getSession().getConfigCompany().getCompanyId();
        payment.pay_id = moPayment.getPkPaymentId();
        payment.funcional_area = moPayment.getFkFunctionalAreaId();
        payment.benef = moPayment.getFkBeneficiaryId();
        payment.series = moPayment.getSeries();
        payment.number = moPayment.getNumber() + "";
        payment.app_date = SLibUtils.DbmsDateFormatDate.format(moPayment.getDateApplication());
        payment.req_date = SLibUtils.DbmsDateFormatDate.format(moPayment.getDateRequired());
        payment.sched_date_n = moPayment.getDateScheduler_n() == null ? null : SLibUtils.DbmsDateFormatDate.format(moPayment.getDateScheduler_n());
        payment.exec_date_n = moPayment.getDateExecute_n() == null ? null : SLibUtils.DbmsDateFormatDate.format(moPayment.getDateExecute_n());
        payment.currency = moPayment.getFkCurrencyId();
        payment.amount = moPayment.getPaymentCurrency() + "";
        payment.exchange_rate_app = moPayment.getPaymentExchangeRateApplication() + "";
        payment.amount_loc_app = moPayment.getPaymentApplication() + "";
        payment.exchange_rate_exec = moPayment.getPaymentExchangeRate() + "";
        payment.amount_loc_exec = moPayment.getPayment() + "";
        payment.payment_way = moPayment.getPaymentWay();
        payment.notes = moPayment.getNotes();
        payment.payment_status = moPayment.getFkStatusPaymentId();
        payment.authz_authorization_id = SModSysConsts.TRNS_ST_DPS_AUTHORN_NA;
        payment.paying_bank = paying.getDbmsBank();
        payment.paying_bank_fiscal_id = paying.getDbmsBankFiscalId();
        payment.paying_account = paying.getBankAccountNumber();
        payment.benef_bank = benef.getDbmsBank();
        payment.benef_bank_fiscal_id = benef.getDbmsBankFiscalId();
        payment.benef_account = benef.getBankAccountNumber();
        payment.sched_user = moPayment.getFkUserScheduledId();
        payment.exec_user = moPayment.getFkUserExecutedId();
        payment.sched_at = SLibUtils.DbmsDateFormatDate.format(moPayment.getTsUserScheduledId());
        payment.exec_at = SLibUtils.DbmsDateFormatDate.format(moPayment.getTsUserExecuted());
        payment.is_deleted = moPayment.isDeleted() ? 1 : 0;
        payment.user_id = miClient.getSession().getUser().getPkUserId();
        
        ArrayList<SExportDataPaymentEntry> entries = new ArrayList<>();
        for (SDbPaymentEntry paymentEty : moPayment.getChildEntries()) {
            SExportDataPaymentEntry entry = new SExportDataPaymentEntry();
            entry.entry_type = paymentEty.getEntryType();
            entry.amount = paymentEty.getEntryPaymentCurrency() + "";
            entry.amount_loc_app = paymentEty.getEntryPaymentApplication() + "";
            entry.entry_currency = paymentEty.getFkEntryCurrencyId() + "";
            entry.conv_rate_app = paymentEty.getConversionRateApplication() + "";
            entry.entry_amount_app = paymentEty.getDestinyPaymentApplicationEntryCy() + "";
            entry.amount_loc_exec = paymentEty.getEntryPayment() + "";
            entry.conv_rate_exec = paymentEty.getConversionRate() + "";
            entry.entry_amount_exec = paymentEty.getDestinyPaymentEntryCy() + "";
            entry.installment = paymentEty.getInstallment();
            entry.document_bal_prev_app = paymentEty.getDocBalancePreviousApplicationCy();
            entry.document_bal_unpd_app = paymentEty.getDocBalanceUnpaidApplicationCy_r();
            entry.document_bal_prev_exec = paymentEty.getDocBalancePreviousCy();
            entry.document_bal_unpd_exec = paymentEty.getDocBalanceUnpaidCy_r();
        }
        
        return payment;
    }

    private SDataBizPartnerBranchBankAccount getBranchBankAccByAccCash() {
        SDataBizPartnerBranchBankAccount ba = new SDataBizPartnerBranchBankAccount();
        try {
            String sql = "SELECT fid_bpb_n, fid_bank_acc_n FROM fin_acc_cash "
                    + "WHERE id_cob = " + moPayment.getFkPayerCashBizPartnerBranchId_n() + " "
                    + "AND id_acc_cash = " + moPayment.getFkPayerCashAccountingCashId_n() + " ";
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                ba = getBranchBankAcc(new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            }
        }
        catch (SQLException e) {
            miClient.showMsgBoxError(e.getMessage());
        }
        return ba;
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
}
