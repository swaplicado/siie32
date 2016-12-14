/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.util;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchBankAccount;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataBankLayoutType;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SFinUtilities;
import erp.mod.SModConsts;
import erp.mod.fin.db.SDbBankLayout;
import erp.mod.fin.db.SFinRecordLayout;
import erp.mod.fin.db.SLayoutBankPaymentRow;
import erp.mod.fin.db.SLayoutBankXmlRow;
import erp.mod.fin.db.SXmlBankLayout;
import erp.mod.fin.db.SXmlBankLayoutPayment;
import erp.mod.fin.db.SXmlBankLayoutPaymentDoc;
import erp.mtrn.data.SDataDps;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.srv.SSrvConsts;
import sa.lib.srv.SSrvLock;
import sa.lib.srv.SSrvUtils;
import sa.lib.xml.SXmlElement;

/**
 *
 * @author Edwin Carmona
 */
public class SFinUtils {
    
    public static SDbBankLayout loadPaymentsXml(SGuiClient client, SDbBankLayout layout) {
        String sObservation = "";
        int nDpsYearId = 0;
        int nDpsDocId = 0;
        double dBalancePayment = 0;
        double dAmountPayed = 0;
        double dExchangeRate = 0;
        SXmlBankLayout oGridXml = null;
        SXmlBankLayoutPayment oLayoutPay = null;
        SDataRecord oRecord = null;
        SDataBizPartnerBranch oBizPartnerBranchCob = null;
        SDataBizPartnerBranchBankAccount oBranchBankAccount = null;
        SDataBizPartner oBizPartner = null;
        SLayoutBankPaymentRow oRow = null;
        SFinRecordLayout oRecordLayout = null;
        SXmlBankLayoutPaymentDoc oLayoutPayDoc = null;
        SLayoutBankXmlRow oXmlRow = null;
        SDbBankLayout oLayout = null;
        SDataBankLayoutType oTypeLayout = null;

        
        try {
            oLayout = layout;
            oGridXml = new SXmlBankLayout();
            oGridXml.processXml(oLayout.getLayoutXml());
            
            oLayout.getBankPaymentRows().clear();
            oLayout.getXmlRows().clear();
            oTypeLayout = (SDataBankLayoutType) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.FINU_TP_LAY_BANK, new int[] { oLayout.getFkBankLayoutTypeId() }, SLibConstants.EXEC_MODE_SILENT);
            
            for (SXmlElement element : oGridXml.getXmlElements()) {
                if (element instanceof SXmlBankLayoutPayment) {
                    
                    // Payment:
                    oLayoutPay = (SXmlBankLayoutPayment) element;
                    oBizPartnerBranchCob = (SDataBizPartnerBranch) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.BPSU_BPB, new int[] { (int) oLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).getValue() }, SLibConstants.EXEC_MODE_SILENT);
                    oBranchBankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.BPSU_BANK_ACC, new int[] { (int) oLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).getValue(), (int) oLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).getValue() }, SLibConstants.EXEC_MODE_SILENT);
                    oBizPartner = (SDataBizPartner) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.BPSU_BP, new int[] { oBizPartnerBranchCob.getFkBizPartnerId() }, SLibConstants.EXEC_MODE_SILENT);
                    dBalancePayment = (double) oLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).getValue();
                    
                    oRow = new SLayoutBankPaymentRow(client);
					
                    oRow.setBizPartnerId(oBizPartner.getPkBizPartnerId());
                    oRow.setBizPartnerBranchId(oBranchBankAccount.getPkBizPartnerBranchId());
                    oRow.setBizPartnerBranchAccountId(oBranchBankAccount.getPkBankAccountId());
                    oRow.setBizPartner(oBizPartner.getBizPartner());
                    oRow.setBizPartnerKey(oBizPartner.getDbmsCategorySettingsSup().getKey());
                    oRow.setBalance(dBalancePayment);
                    oRow.setBalanceTot(0);
                    oRow.setCurrencyKey(oBranchBankAccount.getDbmsCurrencyKey());
                    oRow.setAccountCredit(oTypeLayout.getFkBankPaymentTypeId() == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? oBranchBankAccount.getBankAccountNumber() : oBranchBankAccount.getBankAccountNumberStd());
                    oRow.setIsForPayment((boolean) oLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).getValue());
                    oRow.setIsToPayed((boolean) oLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).getValue());
                    
                    if ((Integer) oLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR).getValue() != 0) {
                        oRecordLayout = new SFinRecordLayout((Integer) oLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR).getValue(), (Integer) oLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_PER).getValue(),
                            (Integer) oLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_BKC).getValue(), (String) oLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_REC_TP).getValue(), (Integer) oLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_NUM).getValue());
                        oRow.setFinRecordLayout(oRecordLayout);
                        oRow.setFinRecordLayoutOld(oRecordLayout);
                       
                        oRecord = (SDataRecord) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.FIN_REC, oRecordLayout.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                        oRow.setRecordPeriod(oRecord.getRecordPeriod());
                        oRow.setRecordBkc(SDataReadDescriptions.getCatalogueDescription((SClientInterface) client, SDataConstants.FIN_BKC, new int[] { oRecord.getPkBookkeepingCenterId() }, SLibConstants.DESCRIPTION_CODE));
                        oRow.setRecordCob(SDataReadDescriptions.getCatalogueDescription((SClientInterface) client, SDataConstants.BPSU_BPB, new int[] { oRecord.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
                        oRow.setRecordNumber(oRecord.getRecordNumber());
                        oRow.setRecordDate(oRecord.getDate());
                    }
                    else {
                        oRecordLayout = null;
                    }
                    
                    for (SXmlElement elementDoc : oLayoutPay.getXmlElements()) {
                        if (elementDoc instanceof SXmlBankLayoutPaymentDoc) {
                            oLayoutPayDoc = (SXmlBankLayoutPaymentDoc) elementDoc;
                            nDpsYearId = (Integer) oLayoutPayDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR).getValue();
                            nDpsDocId = (Integer) oLayoutPayDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC).getValue();
                            sObservation = (String) oLayoutPayDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_OBS).getValue();
                            dAmountPayed = (double) oLayoutPayDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).getValue();
                            dExchangeRate = (double) oLayoutPayDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EXT_RATE).getValue();
                            oXmlRow = new SLayoutBankXmlRow();
                            
                            oXmlRow.setDpsYear(nDpsYearId);
                            oXmlRow.setDpsDoc(nDpsDocId);
                            oXmlRow.setAmountPayed(dAmountPayed);
                            oXmlRow.setExchangeRate(dExchangeRate);
                            oXmlRow.setBizPartner(oBizPartner.getPkBizPartnerId());
                            oXmlRow.setBizPartnerBranch(oBranchBankAccount.getPkBizPartnerBranchId());
                            oXmlRow.setBizPartnerBranchAccount(oBranchBankAccount.getPkBankAccountId());
                            oXmlRow.setConcept((String) oLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CPT).getValue());
                            oXmlRow.setDescription((String) oLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_DCRP).getValue());
                            oXmlRow.setBankKey((int) oLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_KEY).getValue());
                            oXmlRow.setObservation(sObservation);
                            
                            if (oRecordLayout != null) {
                                oXmlRow.setRecYear(oRecordLayout.getPkYearId());
                                oXmlRow.setRecPeriod(oRecordLayout.getPkPeriodId());
                                oXmlRow.setRecBookkeepingCenter(oRecordLayout.getPkBookkeepingCenterId());
                                oXmlRow.setRecRecordType(oRecordLayout.getPkRecordTypeId());
                                oXmlRow.setRecNumber(oRecordLayout.getPkNumberId());
                            }
                            else {
                                oXmlRow.setRecYear(SLibConsts.UNDEFINED);
                                oXmlRow.setRecPeriod(SLibConsts.UNDEFINED);
                                oXmlRow.setRecBookkeepingCenter(SLibConsts.UNDEFINED);
                                oXmlRow.setRecRecordType("");
                                oXmlRow.setRecNumber(SLibConsts.UNDEFINED);
                                oXmlRow.setBookkeepingYear(SLibConsts.UNDEFINED);
                                oXmlRow.setBookkeepingNumber(SLibConsts.UNDEFINED);
                            }
                            oLayout.getXmlRows().add(oXmlRow);
                        }
                    }
                    oLayout.getBankPaymentRows().add(oRow);
                }
            } 
        }
        catch (Exception e) {
            oLayout = null;
            SLibUtils.showException(SFinUtils.class, e);
        }
        
        return oLayout;
    }
    
    public static double getTotalAmount(ArrayList<SLayoutBankPaymentRow> layoutBankPayments) {
        double totalAmount = 0;
        
        for (SLayoutBankPaymentRow bankPayment: layoutBankPayments) {
            totalAmount += bankPayment.getBalanceTot();
        }
        return totalAmount;                
    }
    
    public static SLayoutParameters getLayoutParameters(SGuiClient client, SDbBankLayout layout) {
        SDataAccountCash dataAccountCash = null;
        SDataBizPartnerBranchBankAccount dataBizPartnerBranchBankAccount = null;
        SLayoutParameters parameters = new SLayoutParameters();
        
        try {
            dataAccountCash = (SDataAccountCash) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.FIN_ACC_CASH, 
                                new int[] { layout.getFkBankCompanyBranchId(), layout.getFkBankAccountCashId() }, SLibConstants.EXEC_MODE_SILENT);
            dataBizPartnerBranchBankAccount = dataAccountCash.getDbmsBizPartnerBranchBankAccount();

            parameters.setTitle("SOLICITUD DE AUTORIZACIÓN DE DISPERSIÓN DE PAGOS");
            parameters.setCompanyName(((SClientInterface) client).getSessionXXX().getCompany().getDbmsDataCompany().getBizPartner());
            parameters.setDateTimeRequest(new Date());
            parameters.setApplicationDate((Date)layout.getDateLayout());
            parameters.setBank(dataBizPartnerBranchBankAccount.getDbmsBank());
            parameters.setBankAccount(dataBizPartnerBranchBankAccount.getBankAccountNumber());
            parameters.setTypePayment(SFinUtilities.getNameTypePayLayout(client.getSession(), layout.getPkBankLayoutId()));
            parameters.setCurrency(dataBizPartnerBranchBankAccount.getDbmsCurrencyKey());
            parameters.setTotal(layout.getAmount());
            parameters.setFolio(layout.getPkBankLayoutId() + "");
            parameters.setAuthRequests(layout.getAuthorizationRequests() + "");
        }
        catch (Exception e) {
            SLibUtils.showException(SFinUtils.class, e);
        }
        
        return parameters;
    }
    
    public static ArrayList<SDocumentRequestRow> populateRows(SGuiClient client, ArrayList<SLayoutBankPaymentRow> bankPaymentRows, ArrayList<SLayoutBankXmlRow> xmlRows) {
        String observation = "";
        String concept = "";
        SDataDps dps = null;
        SDataBizPartnerBranchBankAccount dataBizPartnerBranchBankAccount = null;
        SDocumentRequestRow payRow = null;
        ArrayList<SDocumentRequestRow> payments = new ArrayList<SDocumentRequestRow>();

        for (SLayoutBankPaymentRow bankPayment: bankPaymentRows) {
            dataBizPartnerBranchBankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.BPSU_BANK_ACC, 
                                                new int[] { bankPayment.getBizPartnerBranchId(), bankPayment.getBizPartnerBranchAccountId() }, SLibConstants.EXEC_MODE_SILENT);
            
            payRow = new SDocumentRequestRow();
            payRow.setBank(dataBizPartnerBranchBankAccount.getDbmsBank());
            payRow.setBankAccount(bankPayment.getAccountCredit());
            payRow.setBeneficiary(bankPayment.getBizPartner());
            payRow.setAmount(bankPayment.getBalance());
            
            observation = "";
            concept = "";
            for (SLayoutBankXmlRow xmlRow: xmlRows) {
                if(xmlRow.getBizPartner() == bankPayment.getBizPartnerId()) {
                   if (xmlRow.getObservation() != null && !xmlRow.getObservation().isEmpty()) {
                       observation += xmlRow.getObservation();
                       
                       if (xmlRow.getObservation().charAt(xmlRow.getObservation().length()-1) == '.') {
                                observation +=  "\n";
                            }
                            else {
                                observation +=  ".\n";
                            }
                    }
                    dps = (SDataDps) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.TRN_DPS, new int[] { xmlRow.getDpsYear(), xmlRow.getDpsDoc() }, SLibConstants.EXEC_MODE_SILENT);
                    if (dps != null) {
                        concept += "#" + dps.getNumber() + "; ";

                        if (dps.getDbmsDpsEntries() != null && !dps.getDbmsDpsEntries().isEmpty()) {
                            concept += dps.getDbmsDpsEntries().get(0).getConcept();

                            if (dps.getDbmsDpsEntries().get(0).getConcept() != null && !dps.getDbmsDpsEntries().get(0).getConcept().isEmpty()) {
                                if (dps.getDbmsDpsEntries().get(0).getConcept().charAt(dps.getDbmsDpsEntries().get(0).getConcept().length()-1) == '.') {
                                    concept +=  " ";
                                }
                                else {
                                    concept +=  ". ";
                                }
                            }
                        }
                    }
                }
            }
            
            if (!observation.isEmpty()) {
                observation = observation.substring(0, observation.length()-1);
            }
            
            payRow.setObservation(observation);
            payRow.setConcept(concept);
            payments.add(payRow);
        }
            
        return payments;
    }
    
    public static boolean changeLayoutStatus(SGuiClient client, SDbBankLayout layout, int status) throws Exception {
        boolean done = false;
        String sql = "";
        SSrvLock lock = null;
        
        try {
            lock = SSrvUtils.gainLock(client.getSession(), ((SClientInterface) client).getSessionXXX().getCompany().getPkCompanyId(), SModConsts.FIN_LAY_BANK, layout.getPrimaryKey(), layout.getTimeout());
            
            if (lock != null && lock.getLockStatus() == SSrvConsts.LOCK_GAINED) {
                sql = "UPDATE " + layout.getSqlTable() + " SET lay_st = " + status + " " + layout.getSqlWhere() + ";";
                client.getSession().getStatement().execute(sql);
                done = true;
            }
        }
        catch (Exception e) {
            SLibUtils.showException(SFinUtils.class, e);
        }
        finally {
            SSrvUtils.releaseLock(client.getSession(), lock);
        }
        
        return done;
    }
    
    public static boolean increaseLayoutRequest(SGuiClient client, SDbBankLayout layout) throws Exception {
        boolean done = false;
        String sql = "";
        SSrvLock lock = null;
        
        try {
            lock = SSrvUtils.gainLock(client.getSession(), ((SClientInterface) client).getSessionXXX().getCompany().getPkCompanyId(), SModConsts.FIN_LAY_BANK, layout.getPrimaryKey(), layout.getTimeout());
            
            if (lock != null && lock.getLockStatus() == SSrvConsts.LOCK_GAINED) {
                sql = "UPDATE " + layout.getSqlTable() + " SET auth_req = auth_req + 1 " + layout.getSqlWhere() + ";";
                client.getSession().getStatement().execute(sql);
                done = true;
            }
        }
        catch (Exception e) {
            SLibUtils.showException(SFinUtils.class, e);
        }
        finally {
            SSrvUtils.releaseLock(client.getSession(), lock);
        }
        
        return done;
    }
}
