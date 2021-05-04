/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STableUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranchBankAccount;
import erp.mcfg.data.SDataCompany;
import erp.mcfg.data.SDataParamsErp;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataBankLayoutType;
import erp.mfin.data.SDataBookkeepingNumber;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SFinAccountConfigEntry;
import erp.mfin.data.SFinAccountUtilities;
import erp.mfin.data.SFinUtilities;
import erp.mfin.utils.SBalanceTax;
import erp.mfin.utils.SMfinUtils;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SDbBizPartner;
import erp.mod.fin.util.SBankLayoutCourier;
import erp.mod.fin.util.SBankLayoutParams;
import erp.mod.fin.util.SDialogBankLayoutSendingConfirmation;
import erp.mod.fin.util.SDocumentRequestRow;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDsm;
import erp.mtrn.data.SDataDsmEntry;
import erp.mtrn.data.STrnUtilities;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiSession;
import sa.lib.srv.SSrvConsts;
import sa.lib.srv.SSrvLock;
import sa.lib.srv.SSrvUtils;
import sa.lib.xml.SXmlElement;

/**
 *
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores, Isabel Servín
 */
public class SDbBankLayout extends SDbRegistryUser {
    
    public static final int FIELD_CLOSE = FIELD_BASE + 1;
    public static final int FIELD_CLOSE_USER = FIELD_BASE + 2;
    
    public static final int STATUS_NEW = 0;
    public static final int STATUS_APPROVED = 1;
    
    public static final String STATUS_NEW_TEXT = "NUEVO";
    public static final String STATUS_APPROVED_TEXT = "APROBADO";
    
    protected int mnPkBankLayoutId;
    protected Date mtDateLayout;
    protected Date mtDateDue;
    protected String msConcept;
    protected String msAgreement;
    protected String msAgreementReference;
    protected int mnConsecutive;
    protected double mdExchangeRate;
    protected double mdAmount;
    protected double mdAmountPayed;
    protected double mdExchangeRateAcc;
    protected int mnTransfers;
    protected int mnTransfersPayed;
    protected int mnDocs;
    protected int mnDocsPayed;
    protected int mnLayoutStatus;
    protected String msLayoutText;
    protected String msLayoutXml;
    protected int mnTransactionType;
    protected int mnAuthorizationRequests;
    protected boolean mbClosedPayment;
    //protected boolean mbDeleted;
    protected int mnFkBankLayoutTypeId;
    protected int mnFkBankCompanyBranchId;
    protected int mnFkBankAccountCashId;
    protected int mnFkDpsCurrencyId;
    protected int mnFkUserClosedPaymentId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserClosedPayment;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected int mnXtaLayoutBank;
    protected int mnXtaBankPaymentTypeId;
    protected int mnXtaBankCurrencyId;
    protected String msXtaBankLayoutType;
    
    protected String msAuxLayoutPath;
    protected ArrayList<SLayoutBankPaymentRow> maAuxLayoutBankPaymentRows; // once bank layout created, for processing or accounting it
    protected ArrayList<SLayoutBankXmlRow> maAuxLayoutBankXmlRows;
    protected ArrayList<SLayoutBankRecord> maAuxLayoutBankRecords;
    
    /*
     * Private methods
     */
    
    private void updateLayoutXml(Vector<SDataRecordEntry> recordEntries) {
        mnDocsPayed = 0;
        mdAmountPayed = 0;
        
        for (SDataRecordEntry recordEntry : recordEntries) {
            for (SLayoutBankXmlRow layoutBankXmlRow : maAuxLayoutBankXmlRows) {
                int[] key = null;
                
                if (layoutBankXmlRow.getTransactionType() == SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY) {
                    key = new int[] { recordEntry.getFkDpsYearId_n(), recordEntry.getFkDpsDocId_n() };
                }
                else if (layoutBankXmlRow.getTransactionType() == SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY) {
                    key = new int[] { recordEntry.getFkBizPartnerId_nr() };
                }
                
                if (SLibUtils.compareKeys(layoutBankXmlRow.getPrimaryKey(), key)) {
                    if (recordEntry.getIsDeleted()) {
                        if (SLibUtils.compareKeys(layoutBankXmlRow.getBookkeepingNumberKey(), recordEntry.getBookkeepingNumberKey_n())) {
                            layoutBankXmlRow.setAmountPayed(SLibConsts.UNDEFINED);
                            layoutBankXmlRow.setPayed(false);
                            layoutBankXmlRow.setRecYearId(SLibConsts.UNDEFINED);
                            layoutBankXmlRow.setRecPeriodId(SLibConsts.UNDEFINED);
                            layoutBankXmlRow.setRecBookkeepingCenterId(SLibConsts.UNDEFINED);
                            layoutBankXmlRow.setRecRecordTypeId("");
                            layoutBankXmlRow.setRecNumberId(SLibConsts.UNDEFINED);
                            layoutBankXmlRow.setBookkeepingYearId(SLibConsts.UNDEFINED);
                            layoutBankXmlRow.setBookkeepingNumberId(SLibConsts.UNDEFINED);
                        }
                    }
                    else {
                        layoutBankXmlRow.setAmountPayed(layoutBankXmlRow.getAmount());
                        layoutBankXmlRow.setPayed(true);
                        layoutBankXmlRow.setRecYearId(recordEntry.getPkYearId());
                        layoutBankXmlRow.setRecPeriodId(recordEntry.getPkPeriodId());
                        layoutBankXmlRow.setRecBookkeepingCenterId(recordEntry.getPkBookkeepingCenterId());
                        layoutBankXmlRow.setRecRecordTypeId(recordEntry.getPkRecordTypeId());
                        layoutBankXmlRow.setRecNumberId(recordEntry.getPkNumberId());
                        layoutBankXmlRow.setBookkeepingYearId(recordEntry.getFkBookkeepingYearId_n());
                        layoutBankXmlRow.setBookkeepingNumberId(recordEntry.getFkBookkeepingNumberId_n());
                    }
                    break;
                }
            }
        }
        
        for (SLayoutBankXmlRow layoutBankXmlRow : maAuxLayoutBankXmlRows) {
            if (layoutBankXmlRow.isPayed()) {
                if (layoutBankXmlRow.getTransactionType() == SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY) {
                    mnDocsPayed++;
                }
                mdAmountPayed = SLibUtils.roundAmount(mdAmountPayed + layoutBankXmlRow.getAmountPayed());
            }
        }
    }

    private void generateBankLayoutText(final SGuiSession session, final ArrayList<SXmlBankLayoutPayment> layoutPayments) throws Exception {
        int layout = 0;
        int layoutBank = 0;
        int currencyId = 0;
        int bizPartnerId = 0;
        String bizPartner = "";
        String accountDebit = "";
        String accountBranchDebit = "";
        String accountCredit = "";
        String accountBranchCredit = "";
        String layoutTitle = "";
        String sql = "";
        ResultSet resultSet = null;
        SLayoutBankPaymentText layoutBankPaymentText = null;
        ArrayList<SLayoutBankPaymentText> layoutBankPaymentTexts = null;

        // layout name:
        
        sql = "SELECT fid_tp_pay_bank, file_name, lay_bank "
                + "FROM erp.finu_tp_lay_bank "
                + "WHERE id_tp_lay_bank = " + mnFkBankLayoutTypeId;
        
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            layout = resultSet.getInt(1);
            layoutTitle = SLibUtils.textToAscii(resultSet.getString(2));
            layoutBank = resultSet.getInt(3);
        }
        
        layoutBankPaymentTexts = new ArrayList<>();
        
        for (SXmlBankLayoutPayment layoutPayment : layoutPayments) {
            layoutBankPaymentText = new SLayoutBankPaymentText();
            
            // BizPartner:
        
            sql = "SELECT bp.id_bp, bp.bp "
                    + "FROM erp.bpsu_bpb AS bpb "
                    + "INNER JOIN erp.bpsu_bp AS bp ON bpb.fid_bp = bp.id_bp "
                    + "WHERE bpb.id_bpb = " + (int) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BPB).getValue();

            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                bizPartnerId = resultSet.getInt(1);
                bizPartner = resultSet.getString(2);
            }
            
            // Account Debit:
        
            sql = "SELECT acc_num, bankb_num "
                    + "FROM fin_acc_cash AS cash "
                    + "INNER JOIN erp.bpsu_bank_acc AS bank ON bank.id_bpb = cash.fid_bpb_n AND bank.id_bank_acc = cash.fid_bank_acc_n "
                    + "WHERE cash.id_cob = " + mnFkBankCompanyBranchId + " "
                    + "AND cash.id_acc_cash = " + mnFkBankAccountCashId;

            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                accountDebit = resultSet.getString(1);
                accountBranchDebit = resultSet.getString(2);
            }
            
            // Account Credit:
        
            sql = "SELECT fid_cur, " + (layout == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? "acc_num " : "acc_num_std ") + ", bankb_num "
                    + "FROM erp.bpsu_bank_acc "
                    + "WHERE id_bpb = " + (int) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BPB).getValue() + " "
                    + "AND id_bank_acc = " + (int) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).getValue();

            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                currencyId = resultSet.getInt(1);
                accountCredit = resultSet.getString(2);
                accountBranchCredit = resultSet.getString(3);
            }
            
            layoutBankPaymentText.setTotalAmount((double) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).getValue());
            layoutBankPaymentText.setReference((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REF_ALPHA).getValue());
            layoutBankPaymentText.setConcept((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CON).getValue());
            layoutBankPaymentText.setDescription((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_DESCRIP).getValue());
            layoutBankPaymentText.setBizPartnerId(bizPartnerId);
            layoutBankPaymentText.setBizPartner(bizPartner);
            layoutBankPaymentText.setAccountDebit(accountDebit);
            layoutBankPaymentText.setAccountBranchDebit(accountBranchDebit);
            layoutBankPaymentText.setCurrencyId(currencyId);
            layoutBankPaymentText.setAgreement((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE).getValue());
            layoutBankPaymentText.setAgreementReference((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_REF).getValue());
            layoutBankPaymentText.setConceptCie((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_CON).getValue());
            layoutBankPaymentText.setAccountCredit(accountCredit);
            layoutBankPaymentText.setAccountBranchCredit(accountBranchCredit);
            layoutBankPaymentText.setHsbcFiscalVoucher((int) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_VOU).getValue());
            layoutBankPaymentText.setHsbcBankCode((int) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_BANK_CODE).getValue());
            layoutBankPaymentText.setHsbcAccountType((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_ACC_TP).getValue());
            layoutBankPaymentText.setHsbcFiscalIdDebit((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_DBT).getValue());
            layoutBankPaymentText.setHsbcFiscalIdCredit((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_CRD).getValue());
            layoutBankPaymentText.setSantanderBankCode((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_SANT_BANK_CODE).getValue());
            layoutBankPaymentText.setBajioBankCode((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_BANK_CODE).getValue());
            layoutBankPaymentText.setBajioBankNick((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_NICK).getValue());
            layoutBankPaymentText.setBankKey((int) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_KEY).getValue());
            
            layoutBankPaymentTexts.add(layoutBankPaymentText);
        }
        
        switch (layout) {
            case SDataConstantsSys.FINS_TP_PAY_BANK_THIRD:
                switch (layoutBank) {
                   case SFinConsts.LAY_BANK_HSBC:
                       msLayoutText = SFinUtilities.createLayoutHsbcThird(layoutBankPaymentTexts, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_SANT:
                       msLayoutText = SFinUtilities.createLayoutSantanderThird(layoutBankPaymentTexts, mtDateLayout, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_BBAJ:
                       msLayoutText = SFinUtilities.createLayoutBanBajioThird(layoutBankPaymentTexts, layoutTitle, mtDateLayout, mnConsecutive);
                      break;
                  case SFinConsts.LAY_BANK_BBVA:
                       msLayoutText = SFinUtilities.createLayoutBbvaThird(layoutBankPaymentTexts, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_CITI:
                       msLayoutText = SFinUtilities.createLayoutCitibanamexThird(layoutBankPaymentTexts, layoutTitle);
                      break;
                   default:
                       break;
                }
                break;
                
           case SDataConstantsSys.FINS_TP_PAY_BANK_TEF:
                switch (layoutBank) {
                   case SFinConsts.LAY_BANK_HSBC:
                       msLayoutText = SFinUtilities.createLayoutHsbcTef(layoutBankPaymentTexts, layoutTitle, mtDateLayout);
                      break;
                  case SFinConsts.LAY_BANK_SANT:
                       msLayoutText = SFinUtilities.createLayoutSantanderTef(layoutBankPaymentTexts, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_BBAJ:
                       msLayoutText = SFinUtilities.createLayoutBanBajioTef(layoutBankPaymentTexts, layoutTitle, mtDateLayout, mnConsecutive);
                      break;
                   case SFinConsts.LAY_BANK_BBVA:
                       msLayoutText = SFinUtilities.createLayoutBbvaTef(layoutBankPaymentTexts, layoutTitle);
                      break;
                   case SFinConsts.LAY_BANK_CITI:
                       msLayoutText = SFinUtilities.createLayoutCitibanamexTef(layoutBankPaymentTexts, layoutTitle, session);
                      break;
                   default:
                      break;
                }
                break;
                
           case SDataConstantsSys.FINS_TP_PAY_BANK_SPEI_FD_N:
                switch (layoutBank) {
                   case SFinConsts.LAY_BANK_HSBC:
                       msLayoutText = SFinUtilities.createLayoutHsbcSpeiFdN(layoutBankPaymentTexts, layoutTitle);
                       break;
                  case SFinConsts.LAY_BANK_SANT:
                       msLayoutText = SFinUtilities.createLayoutSantanderSpeiFdN(layoutBankPaymentTexts, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_BBAJ:
                       msLayoutText = SFinUtilities.createLayoutBanBajioSpeiFdN(layoutBankPaymentTexts, layoutTitle, mtDateLayout, mnConsecutive);
                      break;
                   case SFinConsts.LAY_BANK_BBVA:
                       msLayoutText = SFinUtilities.createLayoutBbvaSpei(layoutBankPaymentTexts, layoutTitle);
                      break;
                   case SFinConsts.LAY_BANK_CITI:
                       msLayoutText = SFinUtilities.createLayoutCitibanamexSpei(layoutBankPaymentTexts, layoutTitle, session);
                      break;
                   default:
                       break;
                }
                break;
                
            case SDataConstantsSys.FINS_TP_PAY_BANK_SPEI_FD_Y:
                switch (layoutBank) {
                   case SFinConsts.LAY_BANK_HSBC:
                       msLayoutText = SFinUtilities.createLayoutHsbcSpeiFdY(layoutBankPaymentTexts, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_SANT:
                       msLayoutText = SFinUtilities.createLayoutSantanderSpeiFdY(layoutBankPaymentTexts, layoutTitle);
                      break;
                   case SFinConsts.LAY_BANK_BBVA:
                       msLayoutText = SFinUtilities.createLayoutBbvaSpei(layoutBankPaymentTexts, layoutTitle);
                      break;
                   case SFinConsts.LAY_BANK_CITI:
                       msLayoutText = SFinUtilities.createLayoutCitibanamexSpei(layoutBankPaymentTexts, layoutTitle, session);
                      break;
                   default:
                       break;
                }
                break;
                
            case SDataConstantsSys.FINS_TP_PAY_BANK_AGREE:
                switch (layoutBank) {
                    case SFinConsts.LAY_BANK_BBVA:
                        msLayoutText = SFinUtilities.createLayoutBbvaCie(layoutBankPaymentTexts);
                        break;
                    default:
                }
                break;
                
            default:
        }
    }
    
    /**
     * Generate XML of bank layout, when generating it (payments or prepayments) or when accounting it.
     * This method is invoked by private method processLayoutBankPaymentRows().
     * @param session GUI session.
     * @throws Exception 
     */
    private void generateBankLayoutXml(final SGuiSession session) throws Exception {
        SXmlBankLayout xmlBankLayout = new SXmlBankLayout();
        ArrayList<SXmlBankLayoutPayment> xmlBankLayoutPayments = new ArrayList<>();
        
        xmlBankLayout.getAttribute(SXmlBankLayout.ATT_LAY_ID).setValue(mnPkBankLayoutId);
        
        for (SLayoutBankXmlRow layoutBankXmlRow : maAuxLayoutBankXmlRows) {
            SXmlBankLayoutPaymentDoc xmlBankLayoutPaymentDoc = new SXmlBankLayoutPaymentDoc();
            
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR).setValue(layoutBankXmlRow.getDpsYearId());
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC).setValue(layoutBankXmlRow.getDpsDocId());
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).setValue(layoutBankXmlRow.getAmount());
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT_CY).setValue(layoutBankXmlRow.getAmountCy());
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_CUR).setValue(layoutBankXmlRow.getCurrencyId());
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EXR).setValue(layoutBankXmlRow.getExchangeRate());
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_REF_REC).setValue(layoutBankXmlRow.getReferenceRecord());
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_OBS).setValue(layoutBankXmlRow.getObservations());
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EMAIL).setValue(layoutBankXmlRow.getEmail());
            
            boolean found = false;
            
            for (SXmlBankLayoutPayment xmlBankLayoutPayment : xmlBankLayoutPayments) {
                found = false;
                
                if (mnXtaBankPaymentTypeId == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                    if (layoutBankXmlRow.getAgreement().trim().equals((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE).getValue()) && layoutBankXmlRow.getAgreementReference().trim().equals((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_REF).getValue())) {
                        found = true;
                    }
                }
                else {
                    if (SLibUtilities.compareKeys(new int[] { layoutBankXmlRow.getBizPartnerBranchId(), layoutBankXmlRow.getBizPartnerBranchAccountId() }, 
                            new int[] { (int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BPB).getValue(), (int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).getValue() }) &&
                            SLibUtils.compareAmount((double) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_EXR).getValue() * 100, layoutBankXmlRow.getExchangeRate() * 100)) {
                        found = true;
                    }
                }
                
                if (found) {
                    xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).setValue(((double) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).getValue()) + layoutBankXmlRow.getAmount());
                    xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT_CY).setValue(((double) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT_CY).getValue()) + layoutBankXmlRow.getAmountCy());
                    xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REF_ALPHA).setValue(((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REF_ALPHA).getValue()) + "," + layoutBankXmlRow.getReference());
                    xmlBankLayoutPayment.getXmlElements().add(xmlBankLayoutPaymentDoc);
                    break;
                }
            }
            
            if (!found) {
                SXmlBankLayoutPayment xmlBankLayoutPayment = new SXmlBankLayoutPayment();
                
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).setValue(layoutBankXmlRow.getAmount());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT_CY).setValue(layoutBankXmlRow.getAmountCy());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REF_ALPHA).setValue(layoutBankXmlRow.getReference());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CUR).setValue(layoutBankXmlRow.getCurrencyId());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE).setValue(layoutBankXmlRow.getAgreement());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_REF).setValue(layoutBankXmlRow.getAgreementReference());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_CON).setValue(layoutBankXmlRow.getConceptCie());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_EXR).setValue(layoutBankXmlRow.getExchangeRate());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CON).setValue(layoutBankXmlRow.getConcept());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_VOU).setValue(layoutBankXmlRow.getHsbcFiscalVoucher());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_ACC_TP).setValue(layoutBankXmlRow.getHsbcAccountType());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_BANK_CODE).setValue(layoutBankXmlRow.getHsbcBankCode());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_DBT).setValue(layoutBankXmlRow.getHsbcFiscalIdDebit());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_CRD).setValue(layoutBankXmlRow.getHsbcFiscalIdCredit());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_DESCRIP).setValue(layoutBankXmlRow.getDescription());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_SANT_BANK_CODE).setValue(layoutBankXmlRow.getSantanderBankCode());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_BANK_CODE).setValue(layoutBankXmlRow.getBajioBankCode());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_NICK).setValue(layoutBankXmlRow.getBajioBankNick());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_KEY).setValue(layoutBankXmlRow.getBankKey());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).setValue(layoutBankXmlRow.isPayed());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BP).setValue(layoutBankXmlRow.getBizPartnerId());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BPB).setValue(layoutBankXmlRow.getBizPartnerBranchId());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).setValue(layoutBankXmlRow.getBizPartnerBranchAccountId());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR).setValue(layoutBankXmlRow.getRecYearId());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_PER).setValue(layoutBankXmlRow.getRecPeriodId());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_BKC).setValue(layoutBankXmlRow.getRecBookkeepingCenterId());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_REC_TP).setValue(layoutBankXmlRow.getRecRecordTypeId());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_NUM).setValue(layoutBankXmlRow.getRecNumberId());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_YEAR).setValue(layoutBankXmlRow.getBookkeepingYearId());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_NUM).setValue(layoutBankXmlRow.getBookkeepingNumberId());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_ROW_REF_REC).setValue(layoutBankXmlRow.getReferenceRecord());
                
                xmlBankLayoutPayment.getXmlElements().add(xmlBankLayoutPaymentDoc);
                
                xmlBankLayoutPayments.add(xmlBankLayoutPayment);
            }
        }
        
        xmlBankLayout.getXmlElements().addAll(xmlBankLayoutPayments);
        
        msLayoutXml = xmlBankLayout.getXmlString();
        mnTransfers = xmlBankLayoutPayments.size();
        
        generateBankLayoutText(session, xmlBankLayoutPayments);
    }
    
    private String composeRecordEntryConcept(final SGuiSession session, final String bizPartnerName, final int[] bpbBankAccountKey, final String reference) throws Exception {
        String bank = "";
        
        String sql = "SELECT ct.bp_key "
                + "FROM erp.bpsu_bank_acc AS bank "
                + "INNER JOIN erp.bpsu_bp AS bp ON bp.id_bp = bank.fid_bank "
                + "INNER JOIN erp.bpsu_bp_ct AS ct ON ct.id_bp = bp.id_bp AND ct.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_SUP + " "
                + "WHERE bank.id_bpb = " + bpbBankAccountKey[0] + " AND bank.id_bank_acc = " + bpbBankAccountKey[1];

        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                bank = resultSet.getString(1);
            }
        }
        
        String concept = msXtaBankLayoutType + " / " + bank + " / F " + reference + " / " + bizPartnerName;
        
        return SLibUtils.textLeft(concept, SDataRecordEntry.LEN_CONCEPT);
    }
    
    private SDataRecordEntry createRecordEntryAccountCash(final SGuiSession session, final int bizPartnerId, final String bizPartnerName, final int[] bpbBankAccountKey, String reference, double amountPayed, int paymentCurrencyId, int dpsCurrencyId, double exchangeRate, final int sortingPosition, final int[] bookkeepingNumberKey) throws Exception {
        SDataAccountCash accountCash = new SDataAccountCash();
        
        if (accountCash.read(new int[] { mnFkBankCompanyBranchId, mnFkBankAccountCashId }, session.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
        }
        
        exchangeRate = session.getSessionCustom().isLocalCurrency(new int[] { paymentCurrencyId } ) ? 1.0 : exchangeRate;
        
        SDataRecordEntry recordEntry = new SDataRecordEntry();
        
        recordEntry.setConcept(composeRecordEntryConcept(session, bizPartnerName, bpbBankAccountKey, reference));
        //recordEntry.setReference(...);
        //recordEntry.setIsReferenceTax(...);
        recordEntry.setDebit(0);
        recordEntry.setCredit(SLibUtils.roundAmount(amountPayed * exchangeRate));
        recordEntry.setExchangeRate(exchangeRate);
        recordEntry.setExchangeRateSystem(exchangeRate);
        recordEntry.setDebitCy(0);
        recordEntry.setCreditCy(amountPayed);
        recordEntry.setUnits(0d);
        recordEntry.setUserId(0);
        recordEntry.setSortingPosition(sortingPosition);
        recordEntry.setIsExchangeDifference(false);
        recordEntry.setIsSystem(true);
        recordEntry.setIsDeleted(false);
        recordEntry.setFkAccountIdXXX(accountCash.getFkAccountId());
        //recordEntry.setFkAccountId(...);
        //recordEntry.setFkCostCenterId_n(...);
        recordEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        recordEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        recordEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
        recordEntry.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_MO_SUP_PAY[0]);
        recordEntry.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_MO_SUP_PAY[1]);
        
        if (accountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH) {
            recordEntry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[0]);
            recordEntry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[1]);
            recordEntry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH[0]);
            recordEntry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH[1]);
        }
        else {
            recordEntry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[0]);
            recordEntry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[1]);
            recordEntry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK[0]);
            recordEntry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK[1]);
        }
        
        recordEntry.setFkCurrencyId(accountCash.getFkCurrencyId());
        recordEntry.setFkCostCenterIdXXX_n("");
        //recordEntry.setFkCheckWalletId_n(...);
        //recordEntry.setFkCheckId_n(...);
        recordEntry.setFkBizPartnerId_nr(bizPartnerId);
        recordEntry.setFkBizPartnerBranchId_n(bpbBankAccountKey[0]);
        recordEntry.setFkCompanyBranchId_n(accountCash.getPkCompanyBranchId());
        recordEntry.setFkEntityId_n(accountCash.getPkAccountCashId());
        //recordEntry.setFkPlantCompanyBranchId_n(...);
        //recordEntry.setFkPlantEntityId_n(...);
        //recordEntry.setFkTaxBasicId_n(...);
        //recordEntry.setFkTaxId_n(...);
        //recordEntry.setFkYearId_n(...);
        recordEntry.setFkDpsYearId_n(0);
        recordEntry.setFkDpsDocId_n(0);
        //recordEntry.setFkDpsAdjustmentYearId_n(...);
        //recordEntry.setFkDpsAdjustmentDocId_n(...);
        //recordEntry.setFkDiogYearId_n(...);
        //recordEntry.setFkDiogDocId_n(...);
        //recordEntry.setFkMfgYearId_n(...);
        //recordEntry.setFkMfgOrdId_n(...);
        //recordEntry.setFkCfdId_n(...);
        //recordEntry.setFkCostGicId_n(...);
        //recordEntry.setFkPayrollFormerId_n(...);
        //recordEntry.setFkPayrollId_n(...);
        //recordEntry.setFkItemId_n(...);
        //recordEntry.setFkItemAuxId_n(...);
        //recordEntry.setFkUnitId_n(...);
        recordEntry.setFkBookkeepingYearId_n(bookkeepingNumberKey[0]);
        recordEntry.setFkBookkeepingNumberId_n(bookkeepingNumberKey[1]);
        recordEntry.setFkUserNewId(session.getUser().getPkUserId());
        //recordEntry.setFkUserEditId(...);
        //recordEntry.setFkUserDeleteId(...);
        
        return recordEntry;
    }

    private SDataRecord createRecord(final SGuiSession session, final SLayoutBankRecord layoutBankRecord) throws Exception {
        SDataDsm dsm = new SDataDsm();
        Vector<SDataRecordEntry> recordEntries = new Vector<>();
        Vector<SDataRecordEntry> recordEntriesToUpdateInLayoutXml = new Vector<>();
        
        Statement statement = session.getStatement().getConnection().createStatement();
        
        SDataRecord record = new SDataRecord();
        if (record.read(layoutBankRecord.getLayoutBankRecordKey().getPrimaryKey(), statement) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
        }
        
        int sortingPosition = record.getLastSortingPosition();
        
        // Se juntan todos los pagos que fueron a la misma cuenta bancaria beneficiaria:
        Vector<SLayoutPayments> layoutPayments = new Vector<>();
        for (SLayoutBankPayment layoutBankPayment : layoutBankRecord.getLayoutBankPayments()) {
            boolean found = false;
            for (SLayoutPayments layoutPayment : layoutPayments) {
                if (SLibUtils.compareKeys(new int[] { layoutBankPayment.getBizPartnerBranchId(), layoutBankPayment.getBizPartnerBranchBankAccountId() }, layoutPayment.moAccountId)) {
                    layoutPayment.getLayoutBankPayments().add(layoutBankPayment);
                    found = true;
                    break;
                }
            }
            if (!found) {
                SLayoutPayments payments = new SLayoutPayments(new int[] { layoutBankPayment.getBizPartnerBranchId(), layoutBankPayment.getBizPartnerBranchBankAccountId() }, layoutBankRecord.getLayoutBankRecordKey());
                payments.getLayoutBankPayments().add(layoutBankPayment);
                layoutPayments.add(payments);
            }
        }
        
        for (SLayoutPayments layoutPayment : layoutPayments) {
            for (SLayoutBankPayment layoutBankPayment : layoutPayment.moLayoutBankPayments) {
                SDataBookkeepingNumber bookkeepingNumber = new SDataBookkeepingNumber();
                bookkeepingNumber.setPkYearId(record.getPkYearId());
                bookkeepingNumber.setFkUserNewId(session.getUser().getPkUserId());

                if (bookkeepingNumber.save(session.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }

                ArrayList<String> references = new ArrayList<>();

                if (layoutBankPayment.getAction() == SLayoutBankPayment.ACTION_PAY_REMOVE) {
                    // remove payment or prepayment:

                    for (SDataRecordEntry recordEntry : record.getDbmsRecordEntries()) {
                        if (SLibUtils.compareKeys(recordEntry.getBookkeepingNumberKey_n(), layoutBankPayment.getBookkeepingNumberKey_n())) {
                            recordEntry.setIsDeleted(true);
                            recordEntry.setIsRegistryEdited(true);
                            recordEntry.setFkUserDeleteId(session.getUser().getPkUserId());
                            recordEntriesToUpdateInLayoutXml.add(recordEntry);
                        }
                    }
                    mnTransfersPayed--;
                }
                else {
                    // apply payment or prepayment:

                    String bizPartnerName = (String) session.readField(SModConsts.BPSU_BP, new int[] { layoutBankPayment.getBizPartnerId() }, SDbBizPartner.FIELD_NAME_COMM);

                    if (layoutBankPayment.getTransactionType() == SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY) {
                        int sortingPositionBankAccount = ++sortingPosition;
                        
                        // Movimientos de bancos    
                        if (!layoutPayment.mbAdded) {
                            String referenceBankAccount = "";
                            double amountPayed = 0;
                            
                            for (SLayoutBankPayment layoutBankPaymentAux : layoutPayment.moLayoutBankPayments){
                                for (SLayoutBankDps layoutBankDps : layoutBankPaymentAux.getLayoutBankDpss()) {
                                    String reference = layoutBankDps.getDps().getDpsNumber();
                                    references.add(reference);
                                }
                                amountPayed += layoutBankPaymentAux.getMoneyAmount().getOriginalAmount();
                            }
                            
                            for (String reference : references) {
                                referenceBankAccount += (referenceBankAccount.isEmpty() ? "" : ", ") + reference;
                            }
                            recordEntries.add(createRecordEntryAccountCash(session, layoutBankPayment.getBizPartnerId(), bizPartnerName, layoutBankPayment.getBizPartnerBranchBankAccountKey(), 
                                    referenceBankAccount, amountPayed, layoutBankPayment.getMoneyAmount().getOriginalCurrencyId(), layoutBankPayment.getLayoutBankDpss().get(0).moDps.getFkCurrencyId(), mdExchangeRateAcc, sortingPositionBankAccount, (int[]) bookkeepingNumber.getPrimaryKey()));
                            layoutPayment.mbAdded = true;
                        }
                        
                        for (SLayoutBankDps layoutBankDps : layoutBankPayment.getLayoutBankDpss()) {
                            /* TIPOS DE CAMBIO:
                            ** Monedas iguales -> Tipo de cambio asignado en la forma
                            ** Moneda extranjera vs. MXN -> 1.0
                            ** Monedas diferentes extranjeras -> Tipo de cambio asignado en la forma * tipo de cambio del layout
                            ** Resto de casos: Tipo de cambio del layout
                            */
                            double exchangeRate = layoutBankPayment.getMoneyAmount().getOriginalCurrencyId() == layoutBankDps.moDps.getFkCurrencyId() ? mdExchangeRateAcc : 
                                    !session.getSessionCustom().isLocalCurrency(new int[] { layoutBankPayment.getMoneyAmount().getOriginalCurrencyId() } ) && layoutBankDps.moDps.getFkCurrencyId() == SModSysConsts.CFGU_CUR_MXN ? 1.0 :
                                    !session.getSessionCustom().isLocalCurrency(new int[] { layoutBankPayment.getMoneyAmount().getOriginalCurrencyId() } ) && !session.getSessionCustom().isLocalCurrency(new int[] { layoutBankDps.moDps.getFkCurrencyId() } ) ? layoutBankPayment.getExchangeRateForPayment() * mdExchangeRateAcc : layoutBankPayment.getExchangeRateForPayment();
                            
                            double paymentCy = layoutBankDps.getPaymentCy();
                            double payment = (new SMoney(session, layoutBankDps.getPaymentCy(), layoutBankDps.getDps().getFkCurrencyId(), exchangeRate)).getLocalAmount();
                            
                            ArrayList<SBalanceTax> balances = SMfinUtils.getBalanceByTax(session.getDatabase().getConnection(), layoutBankDps.getDps().getPkDocId(), layoutBankDps.getDps().getPkYearId(), 
                                                    SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0], 
                                                    SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1]);
        
							double dTotalBalance = 0d;
							double dTotalBalanceCur = 0d;
							for (SBalanceTax balance : balances) {
								dTotalBalance += balance.getBalance();
								dTotalBalanceCur += balance.getBalanceCurrency();
							}

                            HashMap<String, double[]> taxBalances = new HashMap();
                            String tax;
                            double perc;
                            double percCur;
                            double amtToPay = 0;
                            double amtToPayCur = 0;
                            int[] taxMax = new int[] { 0, 0 };
                            double amtMaj = 0d;
                            for (SBalanceTax balance : balances) {
                                tax = balance.getTaxBasId() + "_" + balance.getTaxId();
                                perc = balance.getBalance() / dTotalBalance;
                                percCur = balance.getBalanceCurrency() / dTotalBalanceCur;

                                taxBalances.put(tax, new double[] { perc, percCur });

                                amtToPay += SLibUtils.roundAmount(payment * perc);
                                amtToPayCur += SLibUtils.roundAmount(paymentCy * percCur);

                                if (balance.getBalanceCurrency() > amtMaj) {
                                    amtMaj = balance.getBalanceCurrency();
                                    taxMax = new int[] { balance.getTaxBasId(), balance.getTaxId() };
                                }
                            }

                            double diffCur = 0;
                            if (paymentCy != amtToPayCur) {
                                diffCur = SLibUtils.roundAmount(paymentCy - amtToPayCur);
                            }
                            double diff = 0;
                            if (payment != amtToPay) {
                                diff = SLibUtils.roundAmount(payment - amtToPay);
                            }
                            
                            for (SBalanceTax balance : balances) {
                                SDataDsmEntry dsmEntry = new SDataDsmEntry();
                                
                                tax = balance.getTaxBasId() + "_" + balance.getTaxId();

                                dsmEntry.setSourceValue(SLibUtils.roundAmount(payment * taxBalances.get(tax)[0]));
                                dsmEntry.setSourceValueCy(SLibUtils.roundAmount(paymentCy * taxBalances.get(tax)[1]));
                                
                                dsmEntry.setDestinyValue(SLibUtils.roundAmount(payment * taxBalances.get(tax)[0]));
                                dsmEntry.setDestinyValueCy(SLibUtils.roundAmount(paymentCy * taxBalances.get(tax)[1]));
                                
                                if (balance.getTaxBasId() == taxMax[0] && balance.getTaxId() == taxMax[1]) {
                                    dsmEntry.setSourceValue(SLibUtils.roundAmount(dsmEntry.getSourceValue() + diff));
                                    dsmEntry.setSourceValueCy(SLibUtils.roundAmount(dsmEntry.getSourceValueCy() + diffCur));

                                    dsmEntry.setDestinyValue(SLibUtils.roundAmount(dsmEntry.getDestinyValue() + diff));
                                    dsmEntry.setDestinyValueCy(SLibUtils.roundAmount(dsmEntry.getDestinyValueCy() + diffCur));
                                }
                                
                                dsmEntry.setFkTaxBasId_n(balance.getTaxBasId());
                                dsmEntry.setFkTaxId_n(balance.getTaxId());
                                
                                dsmEntry.setPkYearId(session.getCurrentYear());
                                dsmEntry.setFkUserNewId(session.getUser().getPkUserId());

                                dsmEntry.setSourceReference("");
                                dsmEntry.setFkSourceCurrencyId(layoutBankDps.getDps().getFkCurrencyId());
                                dsmEntry.setSourceExchangeRateSystem(mdExchangeRate);
                                dsmEntry.setSourceExchangeRate(exchangeRate);

                                dsmEntry.setFkDestinyDpsYearId_n(layoutBankDps.getDps().getPkYearId());
                                dsmEntry.setFkDestinyDpsDocId_n(layoutBankDps.getDps().getPkDocId());
                                dsmEntry.setFkDestinyCurrencyId(layoutBankDps.getDps().getFkCurrencyId());
                                dsmEntry.setDestinyExchangeRateSystem(mdExchangeRate);
                                dsmEntry.setDestinyExchangeRate(exchangeRate);
                                dsmEntry.setDbmsFkDpsCategoryId(layoutBankDps.getDps().getFkDpsCategoryId());
                                dsmEntry.setDbmsDestinyDps(layoutBankDps.getDps().getDpsNumber());
                                //dsmEntry.setDbmsSubclassMove((String) session.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP, SDbRegistry.FIELD_NAME)); // 2019-10-09, Sergio Flores: please remove if not really needed!
                                dsmEntry.setDbmsBizPartner(bizPartnerName);
                                //dsmEntry.setDbmsDestinyTpDps((String) session.readField(SModConsts.TRNU_TP_DPS, new int[] { layoutBankDps.getDps().getFkDpsCategoryId(), layoutBankDps.getDps().getFkDpsClassId(), layoutBankDps.getDps().getFkDpsTypeId() }, SDbRegistry.FIELD_CODE)); // 2019-10-09, Sergio Flores: please remove if not really needed!

                                dsmEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP[0]);
                                dsmEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP[1]);
                                dsmEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP[2]);
                                dsmEntry.setDbmsCtSysMovId(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0]);
                                dsmEntry.setDbmsTpSysMovId(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1]);
                                //dsm.setDbmsSubsystemTypeBiz((String) session.readField(SModConsts.BPSS_CT_BP, new int[] { SDataConstantsSys.BPSS_CT_BP_SUP }, SDbRegistry.FIELD_CODE)); // 2019-10-09, Sergio Flores: please remove if not really needed!
                                dsmEntry.setFkBizPartnerId(layoutBankDps.getDps().getFkBizPartnerId_r());
                                dsmEntry.setDbmsFkBizPartnerBranchId_n(layoutBankDps.getDps().getFkBizPartnerBranchId());

                                Vector<SFinAccountConfigEntry> configEntries = SFinAccountUtilities.obtainBizPartnerAccountConfigs((SClientInterface) session.getClient(), layoutBankDps.getDps().getFkBizPartnerId_r(), SDataConstantsSys.BPSS_CT_BP_SUP,
                                        record.getPkBookkeepingCenterId(), record.getDate(), SDataConstantsSys.FINS_TP_ACC_BP_OP, layoutBankDps.getDps().getFkDpsCategoryId() == SDataConstantsSys.TRNS_CT_DPS_SAL, dsmEntry.getTaxPk());
                                if (configEntries.size() > 0) {
                                    dsmEntry.setDbmsAccountOp(configEntries.get(0).getAccountId());
                                }
                                
                                dsm.getDbmsEntries().add(dsmEntry);
                            }

                            dsm.setDbmsPkRecordTypeId(SDataConstantsSys.FINU_TP_REC_SUBSYS_SUP);

                            dsm.setDate(session.getCurrentDate());
                            dsm.setDbmsErpTaxModel(((SDataParamsErp) session.getConfigSystem()).getTaxModel()); // PRESERVE!!!
                            dsm.setFkSubsystemCategoryId(SDataConstantsSys.BPSS_CT_BP_SUP);
                            dsm.setFkCompanyBranchId(record.getFkCompanyBranchId_n());
                            dsm.setFkUserNewId(session.getUser().getPkUserId());
                            dsm.setDbmsFkCompanyBranch(((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getPkBizPartnerBranchId());
                            //dsm.setDbmsCompanyBranchCode(((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDbmsDataCompany().getDbmsBizPartnerBranch(new int[] { record.getFkCompanyBranchId_n() }).getCode()); // 2019-10-09, Sergio Flores: please remove if not really needed!
                            dsm.setDbmsErpDecimalsValue(((SDataParamsErp) session.getConfigSystem()).getDecimalsValue()); // PRESERVE!!!
                            dsm.setDbmsIsRecordSaved(false);

                            dsm = (SDataDsm) ((SClientInterface) session.getClient()).getGuiModule(SDataConstants.MOD_FIN).processRegistry(dsm);
                            SDataRecord recordDsm = dsm.getDbmsRecord();

                            String reference = layoutBankDps.getDps().getDpsNumber();
                            String concept = composeRecordEntryConcept(session, bizPartnerName, layoutBankPayment.getBizPartnerBranchBankAccountKey(), reference);

                            for (SDataRecordEntry recordEntry : recordDsm.getDbmsRecordEntries()) {
                                recordEntry.setConcept(concept);
                                recordEntry.setSortingPosition(++sortingPosition);
                                recordEntry.setFkBookkeepingYearId_n(bookkeepingNumber.getPkYearId());
                                recordEntry.setFkBookkeepingNumberId_n(bookkeepingNumber.getPkNumberId());
                                recordEntries.add(recordEntry);
                            }

                            dsm.getDbmsEntries().clear();
                        }
                    }
                    else if (layoutBankPayment.getTransactionType() == SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY) {
                        SLayoutBankAccountingAdvance accountingAdvance = new SLayoutBankAccountingAdvance(session, layoutBankPayment.getBizPartnerId(), layoutBankPayment.getBizPartnerBranchId(), layoutBankPayment.getBizPartnerBranchBankAccountId(), mnFkBankCompanyBranchId, mnFkBankAccountCashId);
                    
                        accountingAdvance.setBankLayoutTypeId(mnFkBankLayoutTypeId);
                        accountingAdvance.setBizPartnerId(layoutBankPayment.getBizPartnerId());
                        accountingAdvance.setBizPartnerBranchId(layoutBankPayment.getBizPartnerBranchId());
                        accountingAdvance.setBizPartnerBranchAccountCreditId(layoutBankPayment.getBizPartnerBranchBankAccountId());
                        accountingAdvance.setCompanyBranchId(mnFkBankCompanyBranchId);
                        accountingAdvance.setCompanyBranchAccountDebitId(mnFkBankAccountCashId);
                        accountingAdvance.setAmount(layoutBankPayment.getMoneyAmount().getOriginalAmount());
                        accountingAdvance.setCurrencyId(layoutBankPayment.getMoneyAmount().getOriginalCurrencyId());
                        accountingAdvance.setExcRate(mdExchangeRateAcc);
                        accountingAdvance.setExcRateSystem(1);
                        accountingAdvance.setBookkeepingYearId_n(bookkeepingNumber.getPkYearId());
                        accountingAdvance.setBookkeepingNumberId_n(bookkeepingNumber.getPkNumberId());
                        accountingAdvance.setBookkeepingCenterId(record.getPkBookkeepingCenterId());
                        accountingAdvance.setDate(mtDateLayout);
                        accountingAdvance.setBizPartner(bizPartnerName);
                        accountingAdvance.setReferenceRecord(layoutBankPayment.getReferenceRecord());

                        for (SDataRecordEntry recordEntry : accountingAdvance.getDbmsRecordEntries()) {
                            recordEntry.setSortingPosition(++sortingPosition);
                            recordEntries.add(recordEntry);
                        }

                        recordEntries.addAll(recordEntries);
                    }

                    mnTransfersPayed++;
                }
            }
        }
        
        statement.close();
        
        for (SDataRecordEntry recordEntry : recordEntries) {
            recordEntry.setPkYearId(record.getPkYearId());
            recordEntry.setPkPeriodId(record.getPkPeriodId());
            recordEntry.setPkBookkeepingCenterId(record.getPkBookkeepingCenterId());
            recordEntry.setPkRecordTypeId(record.getPkRecordTypeId());
            recordEntry.setPkNumberId(record.getPkNumberId());
            
            /* 2019-10-09, Sergio Flores: Please remove this code snippet if not really needed!
            entry.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { entry.getFkAccountId() }));
            entry.setDbmsCurrencyKey(session.getSessionCustom().getCurrencyCode(new int[] { entry.getFkCurrencyId() }));

            if (entry.getFkSystemMoveCategoryIdXXX() == SDataConstantsSys.FINS_CT_SYS_MOV_BPS) {
                entry.setDbmsAccountComplement(bizPartnerName);
            }
            */
            
            record.getDbmsRecordEntries().add(recordEntry);
        }
        
        recordEntriesToUpdateInLayoutXml.addAll(recordEntries);
        updateLayoutXml(recordEntriesToUpdateInLayoutXml);
        
        return record;
    }
    
    private void processLayoutBankRecords(final SGuiSession session) throws Exception {
        for (SLayoutBankRecord layoutBankRecord : maAuxLayoutBankRecords) {
            SDataRecord record = createRecord(session, layoutBankRecord);
            
            if (record.save(session.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
            }
            
            if (record.read(layoutBankRecord.getLayoutBankRecordKey().getPrimaryKey(), session.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
        }
    }
    
    private void processLayoutBankPaymentRows(final SGuiSession session) throws Exception {
        // go through all rows of bank payments:
        
        for (SLayoutBankPaymentRow paymentRow : maAuxLayoutBankPaymentRows) {
            boolean found = false;
            
            // payment to remove:
            
            if (paymentRow.getLayoutBankRecordKeyOld() != null) {
                if (paymentRow.getLayoutBankRecordKey() == null ||
                        !(SLibUtils.compareKeys(paymentRow.getLayoutBankRecordKeyOld().getPrimaryKey(), paymentRow.getLayoutBankRecordKey().getPrimaryKey()))) {
                    SLayoutBankPayment layoutBankPaymentToRemove = paymentRow.getLayoutBankPayment().clone();
                    
                    layoutBankPaymentToRemove.setAction(SLayoutBankPayment.ACTION_PAY_REMOVE);
                    
                    for (SLayoutBankRecord record : maAuxLayoutBankRecords) {
                        if (SLibUtils.compareKeys(record.getLayoutBankRecordKey().getPrimaryKey(), paymentRow.getLayoutBankRecordKeyOld().getPrimaryKey())) {
                            record.getLayoutBankPayments().add(layoutBankPaymentToRemove);
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) {
                        SLayoutBankRecord layoutBankRecord = new SLayoutBankRecord(paymentRow.getLayoutBankRecordKeyOld());
                        layoutBankRecord.getLayoutBankPayments().add(layoutBankPaymentToRemove);
                        maAuxLayoutBankRecords.add(layoutBankRecord);
                    }
                }
            }
            
            // payment to apply:
            
            if (paymentRow.getLayoutBankRecordKey() != null) {
                if (paymentRow.getLayoutBankRecordKeyOld() == null ||
                        !(SLibUtils.compareKeys(paymentRow.getLayoutBankRecordKey().getPrimaryKey(), paymentRow.getLayoutBankRecordKeyOld().getPrimaryKey()))) {
                    SLayoutBankPayment layoutBankPaymentToApply = paymentRow.getLayoutBankPayment().clone();
                    
                    layoutBankPaymentToApply.setAction(SLayoutBankPayment.ACTION_PAY_APPLY);
                    
                    for (SLayoutBankRecord record : maAuxLayoutBankRecords) {
                        if (SLibUtils.compareKeys(record.getLayoutBankRecordKey().getPrimaryKey(), paymentRow.getLayoutBankRecordKey().getPrimaryKey())) {
                            record.getLayoutBankPayments().add(layoutBankPaymentToApply);
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) {
                        SLayoutBankRecord layoutBankRecord = new SLayoutBankRecord(paymentRow.getLayoutBankRecordKey());
                        layoutBankRecord.getLayoutBankPayments().add(layoutBankPaymentToApply);
                        maAuxLayoutBankRecords.add(layoutBankRecord);
                    }
                }
            }
        }
        
        if (!maAuxLayoutBankRecords.isEmpty()) {
            processLayoutBankRecords(session);
        }
        
        generateBankLayoutXml(session);
    }
    
    private boolean validateLayoutBankRecordsPeriods(final SGuiSession session) throws Exception {
        for (SLayoutBankRecord layoutBankRecord : maAuxLayoutBankRecords) {
            SDataRecord record = new SDataRecord();
            
            if (record.read(layoutBankRecord.getLayoutBankRecordKey().getPrimaryKey(), session.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
            
            record.checkIsEditable(session.getStatement().getConnection());
        }
        
        return true;
    }
    
    private boolean validateLayoutBankPaymentDpsStatus(/*final SGuiSession session*/) throws Exception {
        for (SLayoutBankPaymentRow paymentRow : maAuxLayoutBankPaymentRows) {
            SLayoutBankPayment layoutBankPayment = paymentRow.getLayoutBankPayment();
            
            if (layoutBankPayment.getTransactionType() == SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY) {
                for (SLayoutBankDps layoutBankDps : layoutBankPayment.getLayoutBankDpss()) {
                    layoutBankDps.getDps().checkIsReferable();
                }
            }
        }
        
        return true;
    }
    
    /*
     * Public methods
     */
    
    public SDbBankLayout() {
        super(SModConsts.FIN_LAY_BANK);
    }
    
    public void setPkBankLayoutId(int n) { mnPkBankLayoutId = n; }
    public void setDateLayout(Date t) { mtDateLayout = t; }
    public void setDateDue(Date t) { mtDateDue = t; }
    public void setConcept(String s) { msConcept = s; }
    public void setConsecutive(int n) { mnConsecutive = n; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setAmount(double d) { mdAmount = d; }
    public void setAmountPayed(double d) { mdAmountPayed = d; }
    public void setExchangeRateAcc(double d) { mdExchangeRateAcc = d; }
    public void setTransfers(int n) { mnTransfers = n; }
    public void setTransfersPayed(int n) { mnTransfersPayed = n; }
    public void setDocs(int n) { mnDocs = n; }
    public void setDocsPayed(int n) { mnDocsPayed = n; }
    public void setLayoutStatus(int n) { mnLayoutStatus = n; }
    public void setLayoutText(String s) { msLayoutText = s; }
    public void setLayoutXml(String s) { msLayoutXml = s; }
    public void setTransactionType(int n) { mnTransactionType = n; }
    public void setAuthorizationRequests(int n) { mnAuthorizationRequests = n; }
    public void setClosedPayment(boolean b) { mbClosedPayment = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkBankLayoutTypeId(int n) { mnFkBankLayoutTypeId = n; }
    public void setFkBankCompanyBranchId(int n) { mnFkBankCompanyBranchId = n; }
    public void setFkBankAccountCashId(int n) { mnFkBankAccountCashId = n; }
    public void setFkDpsCurrencyId(int n) { mnFkDpsCurrencyId = n; }
    public void setFkUserClosedPaymentId(int n) { mnFkUserClosedPaymentId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserClosedPayment(Date t) { mtTsUserClosedPayment = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public void setXtaLayoutBank(int n) { mnXtaLayoutBank = n; }
    public void setXtaBankPaymentTypeId(int n) { mnXtaBankPaymentTypeId = n; }
    public void setXtaBankCurrencyId(int n) { mnXtaBankCurrencyId = n; }
    public void setXtaBankLayoutType(String s) { msXtaBankLayoutType = s; }
    
    public void setAuxLayoutPath(String s) { msAuxLayoutPath = s; }
    
    public int getPkBankLayoutId() { return mnPkBankLayoutId; }
    public Date getDateLayout() { return mtDateLayout; }
    public Date getDateDue() { return mtDateDue; }
    public String getConcept() { return msConcept; }
    public String getAgreement() { return msConcept; }
    public String getAgreementReference() { return msConcept; }
    public int getConsecutive() { return mnConsecutive; }
    public double getExchangeRate() { return mdExchangeRate; }
    public double getAmount() { return mdAmount; }
    public double getAmountPayed() { return mdAmountPayed; }
    public double getExchangeRateAcc() { return mdExchangeRateAcc; }
    public int getTransfers() { return mnTransfers; }
    public int getTransfersPayed() { return mnTransfersPayed; }
    public int getDocs() { return mnDocs; }
    public int getDocsPayed() { return mnDocsPayed; }
    public int getLayoutStatus() { return mnLayoutStatus; }
    public String getLayoutText() { return msLayoutText; }
    public String getLayoutXml() { return msLayoutXml; }
    public int getAuthorizationRequests() { return mnAuthorizationRequests; }
    public int getTransactionType() { return mnTransactionType; }
    public boolean isClosedPayment() { return mbClosedPayment; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkBankLayoutTypeId() { return mnFkBankLayoutTypeId; }
    public int getFkBankCompanyBranchId() { return mnFkBankCompanyBranchId; }
    public int getFkBankAccountCashId() { return mnFkBankAccountCashId; }
    public int getFkDpsCurrencyId() { return mnFkDpsCurrencyId; }
    public int getFkUserClosedPaymentId() { return mnFkUserClosedPaymentId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserClosedPayment() { return mtTsUserClosedPayment; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public int getXtaLayoutBank() { return mnXtaLayoutBank; }
    public int getXtaBankPaymentTypeId() { return mnXtaBankPaymentTypeId; }
    public int getXtaBankCurrencyId() { return mnXtaBankCurrencyId; }
    public String getXtaBankLayoutType() { return msXtaBankLayoutType; }
    
    public String getAuxLayoutPath() { return msAuxLayoutPath; }
    
    public ArrayList<SLayoutBankPaymentRow> getAuxLayoutBankPaymentRows() { return maAuxLayoutBankPaymentRows; }
    public ArrayList<SLayoutBankXmlRow> getAuxLayoutBankXmlRows() { return maAuxLayoutBankXmlRows; }
    public ArrayList<SLayoutBankRecord> getAuxLayoutBankRecords() { return maAuxLayoutBankRecords; }
    
    public String getBankLayoutNumber(boolean virtuallyIncrementRequests) { return "" + mnPkBankLayoutId + "-" + (mnAuthorizationRequests + (virtuallyIncrementRequests ? 1 : 0)); }
    
    public String getTransactionTypeName() {
        String type = "";
        
        switch (mnTransactionType) {
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
                type = "TRANSFERENCIAS";
                break;
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                type = "ANTICIPOS";
                break;
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_OWN_TRANSFER:
                type = "TRANSFERENCIAS PROPIAS";
                break;
            default:
        }
        
        return type;
    }

    /**
     * Writes bank layout in post save method in objects of this class, so, be careful when renaming this method!
     * Set to post save method in SFormBankLayout.getRegistry().
     * @param client GUI client
     */
    public void writeBankLayout(final SGuiClient client) {
        File file = new File(msAuxLayoutPath);

        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ASCII"));

            bw.write(msLayoutText);
            bw.close();
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(STableUtilities.class.getName(), e);
        }
    }

    /**
     * Parse XML of bank layout.
     * @param client GUI client.
     * @param isMail
     * @throws Exception 
     */
    public void parseBankLayoutXml(SGuiClient client, boolean isMail) throws Exception {
        HashMap<String, SDataRecord> recordsMap = new HashMap<>(); // to optimize record retrieving
        
        maAuxLayoutBankPaymentRows.clear();
        maAuxLayoutBankXmlRows.clear();
        mnTransfersPayed = 0;
        
        SDataBankLayoutType bankLayoutType = (SDataBankLayoutType) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.FINU_TP_LAY_BANK, new int[] { mnFkBankLayoutTypeId }, SLibConstants.EXEC_MODE_SILENT);
        SXmlBankLayout xmlBankLayout = new SXmlBankLayout();
        if (isMail) {
            xmlBankLayout.processXmlMail(msLayoutXml);
        }
        else {
            xmlBankLayout.processXml(msLayoutXml);
        }
    
        for (SXmlElement elementPay : xmlBankLayout.getXmlElements()) {
            if (elementPay instanceof SXmlBankLayoutPayment) {
                // parse payment:
                
                SXmlBankLayoutPayment xmlBankLayoutPayment = (SXmlBankLayoutPayment) elementPay;
                
                SDataBizPartnerBranchBankAccount bizPartnerBranchBankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.BPSU_BANK_ACC, 
                        new int[] { (int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BPB).getValue(), (int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).getValue() }, SLibConstants.EXEC_MODE_SILENT);
                SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.BPSU_BP, 
                        new int[] { (int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BP).getValue() }, SLibConstants.EXEC_MODE_SILENT);

                SLayoutBankPaymentRow layoutBankPaymentRow = new SLayoutBankPaymentRow(client);
                
                layoutBankPaymentRow.setBizPartnerId(bizPartner.getPkBizPartnerId());
                layoutBankPaymentRow.setBizPartnerBranchId(bizPartnerBranchBankAccount.getPkBizPartnerBranchId());
                layoutBankPaymentRow.setBizPartnerBranchAccountId(bizPartnerBranchBankAccount.getPkBankAccountId());
                layoutBankPaymentRow.setBizPartner(bizPartner.getBizPartner());
                layoutBankPaymentRow.setBizPartnerKey(bizPartner.getDbmsCategorySettingsSup().getKey());
                
                double payment = (double) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).getValue();
                int currencyId = (int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CUR).getValue();
                double exchangeRate = (double) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_EXR).getValue();
                
                layoutBankPaymentRow.setPayment(payment);
                //layoutBankPaymentRow.setPaymentCur(...); set forward
                layoutBankPaymentRow.setCurrencyId(currencyId);
                layoutBankPaymentRow.setExchangeRate(exchangeRate);
                
                layoutBankPaymentRow.setPayerAccountCurrencyKey(bizPartnerBranchBankAccount.getDbmsCurrencyKey());
                layoutBankPaymentRow.seBeneficiaryAccountBankName(bizPartnerBranchBankAccount.getDbmsBank());
                layoutBankPaymentRow.setBeneficiaryAccountNumber(bankLayoutType.getFkBankPaymentTypeId() == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? bizPartnerBranchBankAccount.getBankAccountNumber() : bizPartnerBranchBankAccount.getBankAccountNumberStd());
                layoutBankPaymentRow.setBeneficiaryAccountNumberShort(SLibUtils.textRight(bizPartnerBranchBankAccount.getBankAccountNumber(), SDataBizPartnerBranchBankAccount.ACCOUNT_NUMBER_VISIBLE_RIGHT_LEN));
                layoutBankPaymentRow.setAgreement((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE).getValue());
                layoutBankPaymentRow.setAgreementReference((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_REF).getValue());
                layoutBankPaymentRow.setAgreementConceptCie((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_CON).getValue());
                layoutBankPaymentRow.setForPayment((boolean) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).getValue());
                layoutBankPaymentRow.setPayed((boolean) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).getValue());

                SLayoutBankRecordKey layoutBankRecordKey = null;
                
                if ((int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR).getValue() != 0) {
                    layoutBankRecordKey = new SLayoutBankRecordKey(
                            (int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR).getValue(), 
                            (int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_PER).getValue(), 
                            (int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_BKC).getValue(), 
                            (String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_REC_TP).getValue(), 
                            (int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_NUM).getValue());
                    
                    layoutBankPaymentRow.setLayoutBankRecordKey(layoutBankRecordKey);
                    layoutBankPaymentRow.setLayoutBankRecordKeyOld(layoutBankRecordKey);
                    
                    SDataRecord record = recordsMap.get(layoutBankRecordKey.getRecordPrimaryKey()); // recover record, if already retrieved
                    
                    if (record == null) {
                        // record not yet retrieved, so get it:
                        record = (SDataRecord) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.FIN_REC, layoutBankRecordKey.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                        recordsMap.put(layoutBankRecordKey.getRecordPrimaryKey(), record);
                    }
                    
                    layoutBankPaymentRow.setRecordPeriod(record.getRecordPeriod());
                    layoutBankPaymentRow.setRecordBkc(record.getDbmsBookkeepingCenterCode());
                    layoutBankPaymentRow.setRecordCob(record.getDbmsCompanyBranchCode());
                    layoutBankPaymentRow.setRecordNumber(record.getRecordNumber());
                    layoutBankPaymentRow.setRecordDate(record.getDate());
                }

                SLayoutBankPayment layoutBankPayment = new SLayoutBankPayment(0, bizPartner.getPkBizPartnerId(), bizPartnerBranchBankAccount.getPkBizPartnerBranchId(), bizPartnerBranchBankAccount.getPkBankAccountId());

                layoutBankPayment.setMoneyAmount(new SMoney(client.getSession(), payment, bizPartnerBranchBankAccount.getFkCurrencyId(), (double) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_EXR).getValue()));
                layoutBankPayment.setExchangeRateForPayment((double) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_EXR).getValue());
                layoutBankPayment.setBookkeepingYearId_n((int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_YEAR).getValue());
                layoutBankPayment.setBookkeepingNumberId_n((int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_NUM).getValue());
                //layoutBankPayment.setAction(...);

                // create layout bank document:
                
                double paymentCur = 0;

                for (SXmlElement elementDoc : xmlBankLayoutPayment.getXmlElements()) {
                    if (elementDoc instanceof SXmlBankLayoutPaymentDoc) {
                        // parse individual payments of payment:
                        
                        SXmlBankLayoutPaymentDoc xmlBankLayoutPaymentDoc = (SXmlBankLayoutPaymentDoc) elementDoc;
                        
                        int dpsYearId = (int) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR).getValue();
                        int dpsDocId = (int) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC).getValue();
                        double dpsAmount = (double) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).getValue();
                        double dpsAmountCur = (double) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT_CY).getValue();
                        int dpsCurrencyId = (int) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_CUR).getValue();
                        double dpsExchangeRate = (double) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EXR).getValue();
                        String referenceRecord = (String) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_REF_REC).getValue();
                        String observations = (String) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_OBS).getValue();
                        String email = (String) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EMAIL).getValue();
                        
                        paymentCur = SLibUtils.roundAmount(paymentCur + dpsAmountCur);
                        
                        SDataDps dps = null;

                        if (dpsYearId == 0 && dpsDocId == 0) {
                            // is a prepayment:
                            
                            layoutBankPayment.setTransactionType(SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY);
                            layoutBankPayment.setPrepaymentObservations(observations);
                            layoutBankPayment.setPrepaymentEmail(email);
                        }
                        else {
                            // is a payment:
                            
                            dps = (SDataDps) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.TRN_DPS, new int[] { dpsYearId, dpsDocId }, SLibConstants.EXEC_MODE_SILENT);
                            
                            SLayoutBankDps layoutBankDps = new SLayoutBankDps(dps, dpsAmount, dpsAmountCur, email);
                            
                            layoutBankPayment.setTransactionType(SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY);
                            layoutBankPayment.getLayoutBankDpss().add(layoutBankDps);
                        }

                        layoutBankPayment.getMoneyAmount().setExchangeRate(dps == null ? 1d : dps.getExchangeRate());
                        layoutBankPayment.setReferenceRecord(referenceRecord);

                        SLayoutBankXmlRow layoutBankXmlRow = new SLayoutBankXmlRow();

                        layoutBankXmlRow.setTransactionType(layoutBankPayment.getTransactionType());
                        layoutBankXmlRow.setDpsYearId(dpsYearId);
                        layoutBankXmlRow.setDpsDocId(dpsDocId);
                        layoutBankXmlRow.setAmount(dpsAmount);
                        layoutBankXmlRow.setAmountCy(dpsAmountCur);
                        layoutBankXmlRow.setAmountPayed(dpsAmount);
                        layoutBankXmlRow.setExchangeRate(dpsExchangeRate);
                        layoutBankXmlRow.setCurrencyId(dpsCurrencyId);
                        layoutBankXmlRow.setPayed(layoutBankPaymentRow.isPayed());
                        layoutBankXmlRow.setBizPartnerId(bizPartner.getPkBizPartnerId());
                        layoutBankXmlRow.setBizPartnerBranchId(bizPartnerBranchBankAccount.getPkBizPartnerBranchId());
                        layoutBankXmlRow.setBizPartnerBranchAccountId(bizPartnerBranchBankAccount.getPkBankAccountId());
                        
                        layoutBankXmlRow.setAgreement(layoutBankPaymentRow.getAgreement());
                        layoutBankXmlRow.setAgreementReference(layoutBankPaymentRow.getAgreementReference());
                        layoutBankXmlRow.setConceptCie(layoutBankPaymentRow.getAgreementConceptCie());
                        
                        layoutBankXmlRow.setHsbcBankCode(SLibUtils.parseInt((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_BANK_CODE).getValue()));
                        layoutBankXmlRow.setHsbcAccountType((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_ACC_TP).getValue());
                        layoutBankXmlRow.setHsbcFiscalIdDebit((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_DBT).getValue());
                        layoutBankXmlRow.setHsbcFiscalIdCredit((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_CRD).getValue());
                        layoutBankXmlRow.setHsbcFiscalVoucher(SLibUtils.parseInt((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_VOU).getValue()));
                        layoutBankXmlRow.setSantanderBankCode((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_SANT_BANK_CODE).getValue());
                        layoutBankXmlRow.setConcept((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CON).getValue());
                        layoutBankXmlRow.setDescription((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_DESCRIP).getValue());
                        layoutBankXmlRow.setReference(dps == null ? "" : dps.getDpsNumber());
                        layoutBankXmlRow.setBajioBankCode((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_BANK_CODE).getValue());
                        layoutBankXmlRow.setBajioBankNick((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_NICK).getValue());
                        layoutBankXmlRow.setBankKey((int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_KEY).getValue());
                        layoutBankXmlRow.setReferenceRecord(referenceRecord);
                        layoutBankXmlRow.setObservations(observations);
                        layoutBankXmlRow.setEmail(email);

                        if (layoutBankRecordKey != null) {
                            layoutBankXmlRow.setRecYearId(layoutBankRecordKey.getPkYearId());
                            layoutBankXmlRow.setRecPeriodId(layoutBankRecordKey.getPkPeriodId());
                            layoutBankXmlRow.setRecBookkeepingCenterId(layoutBankRecordKey.getPkBookkeepingCenterId());
                            layoutBankXmlRow.setRecRecordTypeId(layoutBankRecordKey.getPkRecordTypeId());
                            layoutBankXmlRow.setRecNumberId(layoutBankRecordKey.getPkNumberId());
                            layoutBankXmlRow.setBookkeepingYearId(layoutBankPayment.getBookkeepingYearId_n());
                            layoutBankXmlRow.setBookkeepingNumberId(layoutBankPayment.getBookkeepingNumberId_n());
                        }
                        else {
                            layoutBankXmlRow.setRecYearId(0);
                            layoutBankXmlRow.setRecPeriodId(0);
                            layoutBankXmlRow.setRecBookkeepingCenterId(0);
                            layoutBankXmlRow.setRecRecordTypeId("");
                            layoutBankXmlRow.setRecNumberId(0);
                            layoutBankXmlRow.setBookkeepingYearId(0);
                            layoutBankXmlRow.setBookkeepingNumberId(0);
                        }
                        
                        maAuxLayoutBankXmlRows.add(layoutBankXmlRow);
                    }
                }
                
                layoutBankPaymentRow.setPaymentCur(paymentCur);
                layoutBankPaymentRow.setLayoutBankPayment(layoutBankPayment);
                
                maAuxLayoutBankPaymentRows.add(layoutBankPaymentRow);
                
                if (layoutBankPaymentRow.isForPayment()) {
                    mnTransfersPayed++;
                }
            }
        }
    }
    
    /**
     * Create bank layout params for generation of PDF and mail of authorization request.
     * @param client
     * @return
     * @throws Exception 
     */
    private SBankLayoutParams createBankLayoutParams(SGuiClient client) throws Exception {
        SBankLayoutParams params = new SBankLayoutParams();
        
        parseBankLayoutXml(client, true);
        
        SDataAccountCash accountCash = (SDataAccountCash) SDataUtilities.readRegistry(
                (SClientInterface) client, 
                SDataConstants.FIN_ACC_CASH, 
                new int[] { mnFkBankCompanyBranchId, mnFkBankAccountCashId }, 
                SLibConstants.EXEC_MODE_SILENT);
        
        SDataBizPartnerBranchBankAccount bizPartnerBranchBankAccount = accountCash.getDbmsBizPartnerBranchBankAccount();
        
        params.setTitle("SOLICITUD DE AUTORIZACIÓN DE DISPERSIÓN DE PAGOS");
        params.setDatetimeRequest(new Date());
        params.setApplicationDate(mtDateLayout);
        params.setBank(bizPartnerBranchBankAccount.getDbmsBank());
        params.setBankAccount(bizPartnerBranchBankAccount.getBankAccountNumber());
        params.setPaymentType(SFinUtilities.getNameTypePayLayout(client.getSession(), mnPkBankLayoutId));
        params.setCurrency(bizPartnerBranchBankAccount.getDbmsCurrencyKey());
        params.setCurrencyDps(client.getSession().getSessionCustom().getCurrencyCode(new int[] { mnFkDpsCurrencyId }));
        params.setOriginalTotal(mdAmount);
        params.setBankLayoutNumber(getBankLayoutNumber(true));
        params.setBankLayoutType(getTransactionTypeName());
        params.setIsDifferentCurrency(accountCash.getFkCurrencyId() != mnFkDpsCurrencyId);
        
        return params;
    }
    
    /**
     * Create array of objects to be used as a JasperReports collection data source for generating PDF.
     * @param client GUI client
     * @return Array of objects of payments.
     */
    private ArrayList<SDocumentRequestRow> createDocumentRequestRows(SGuiClient client) {
        ArrayList<SDocumentRequestRow> payments = new ArrayList<>();

        for (SLayoutBankPaymentRow paymentRow : maAuxLayoutBankPaymentRows) {
            SDataBizPartnerBranchBankAccount dataBizPartnerBranchBankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.BPSU_BANK_ACC, 
                 new int[] { paymentRow.getBizPartnerBranchId(), paymentRow.getBizPartnerBranchAccountId() }, SLibConstants.EXEC_MODE_SILENT);
            
            SDocumentRequestRow documentRequestRow = new SDocumentRequestRow();
            documentRequestRow.setBank(dataBizPartnerBranchBankAccount.getDbmsBank());
            
            if (mnXtaBankPaymentTypeId == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                documentRequestRow.setBankAccount(paymentRow.getAgreement() + "/" + paymentRow.getAgreementReference());
            }
            else {
                documentRequestRow.setBankAccount(paymentRow.getBeneficiaryAccountNumber());
            }
            
            documentRequestRow.setBeneficiary(paymentRow.getBizPartner());
            documentRequestRow.setOriginalCurrencyAmount(paymentRow.getPayment());
            documentRequestRow.setCurrencyAmount(paymentRow.getPaymentCur());
            
            String observation = "";
            String concept = "";
            
            for (SLayoutBankXmlRow xmlRow : maAuxLayoutBankXmlRows) {
                if(xmlRow.getBizPartnerId() == paymentRow.getBizPartnerId()) {
                    if (xmlRow.getObservations() != null && !xmlRow.getObservations().isEmpty()) {
                        observation += xmlRow.getObservations();
                       
                        if (xmlRow.getObservations().charAt(xmlRow.getObservations().length() - 1) == '.') {
                                observation += "\n";
                        }
                        else {
                            observation +=  ".\n";
                        }
                    }
                    
                    SDataDps dps = (SDataDps) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.TRN_DPS, new int[] { xmlRow.getDpsYearId(), xmlRow.getDpsDocId() }, SLibConstants.EXEC_MODE_SILENT);
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
            
            documentRequestRow.setObservation(observation);
            documentRequestRow.setConcept(concept);
            payments.add(documentRequestRow);
        }
        
        return payments;
    }
    
    /**
     * Generate PDF of bank layout.
     * @param client
     * @param bankLayoutParams
     * @return
     * @throws Exception 
     */
    private File generatePdfTreasury(final SGuiClient client, SBankLayoutParams bankLayoutParams) throws Exception {
        ArrayList<SDocumentRequestRow> documentRequestRows = createDocumentRequestRows(client);

        HashMap<String, Object> reportParams = client.createReportParams();
        reportParams.put("sTitle", bankLayoutParams.getTitle());
        reportParams.put("sDateStamp", SLibUtils.DateFormatDatetime.format(bankLayoutParams.getDatetimeRequest()));
        reportParams.put("sApplicationDate", SLibUtils.DateFormatDate.format(bankLayoutParams.getApplicationDate()));
        reportParams.put("sLayoutType", bankLayoutParams.getBankLayoutType());
        reportParams.put("nTransfers", documentRequestRows.size());                
        reportParams.put("sBank", bankLayoutParams.getBank());
        reportParams.put("sBankAccount", bankLayoutParams.getBankAccount());
        reportParams.put("sPaymentType", bankLayoutParams.getPaymentType());
        reportParams.put("sCurrency", bankLayoutParams.getCurrency());
        reportParams.put("dOriginalTotal", bankLayoutParams.getOriginalTotal());
        reportParams.put("bIsDifferentCurrency", bankLayoutParams.getIsDifferentCurrency());
        reportParams.put("sCurrencyDps", bankLayoutParams.getCurrencyDps());

        File fileTemplate = new File("reps/fin_lay_bank.jasper");
        JasperReport report = (JasperReport) JRLoader.loadObject(fileTemplate);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, reportParams, new JRBeanCollectionDataSource(documentRequestRows));
        byte[] reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);
        File fileTemporal = File.createTempFile("document", ".pdf");
        FileOutputStream fos = new FileOutputStream(fileTemporal);
        fos.write(reportBytes);
        fos.close();

        File file = new File(fileTemporal.getParentFile() + "\\" + SLibUtilities.textToAlphanumeric(((SClientInterface) client).getSessionXXX().getCompany().getCompany()) + "_" + bankLayoutParams.getBankLayoutNumber() + ".pdf");
        fos = new FileOutputStream(file);
        fos.write(reportBytes);
        fos.close();
        
        return file;
    }
    
    public boolean sendMailTreasuryRequest(final SGuiClient client) throws Exception {
        boolean sent = false;
        
        SDialogBankLayoutSendingConfirmation dlgSendingConfirmation = new SDialogBankLayoutSendingConfirmation(client, "Confirmación de envío de solicitud");
        dlgSendingConfirmation.setVisible(true);
        
        if (dlgSendingConfirmation.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
            SBankLayoutParams bankLayoutParams = createBankLayoutParams(client);
            File pdf = generatePdfTreasury(client, bankLayoutParams);

            if (pdf != null) {
                SDataCompany company = ((SClientInterface) client).getSessionXXX().getCompany();
                
                String subject = "SOLIC. AUT. " + company.getDbmsDataCompany().getBizPartnerCommercial() + " #" + bankLayoutParams.getBankLayoutNumber();

                String body = SLibUtils.textToAscii(company.getCompany() + "\n" +
                       "Fecha y hora de solicitud: " + SLibUtils.DateFormatDatetime.format(bankLayoutParams.getDatetimeRequest()) + "\n\n" +
                       "FECHA DE APLICACION: " + SLibUtils.DateFormatDate.format(bankLayoutParams.getApplicationDate()) + "\n\n" +
                       "Banco: " + bankLayoutParams.getBank() + "\n" +
                       "Cuenta bancaria: " + bankLayoutParams.getBankAccount() + "\n" +
                       "Tipo de pago: " + bankLayoutParams.getPaymentType() + "\n" +
                       "Total: " + SLibUtils.DecimalFormatValue2D.format(bankLayoutParams.getOriginalTotal()) + " " + bankLayoutParams.getCurrency() +
                       (dlgSendingConfirmation.getComment().isEmpty() ? "" : "\n\n" + dlgSendingConfirmation.getComment()));
                
                sent = STrnUtilities.sendMailPdf((SClientInterface) client, SModSysConsts.CFGS_TP_MMS_FIN_PAY_AUTH_REQ, pdf, subject, body);

                if (sent) {
                    try {
                        if (mnLayoutStatus == SDbBankLayout.STATUS_NEW) {
                            updateLayoutStatus(client, SDbBankLayout.STATUS_APPROVED);
                        }

                        if (dlgSendingConfirmation.isSendMailToBeneficiariesChecked()) {
                            sendMailBeneficiaries(client);
                        }
                        
                        client.showMsgBoxInformation("La solicitud de autorización del layout bancario ha sido enviada.");
                    }
                    catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }
                }
            }
        }
        
        return sent;
    }
    
    public void sendMailBeneficiaries(final SGuiClient client) throws Exception {
        new SBankLayoutCourier(client, this).start();
    }
    
    /**
     * Update bank layout status.
     * @param client GUI client.
     * @param newLayoutStatus Accepted values: STATUS_NEW or STATUS_APPROVED.
     * @return
     * @throws Exception 
     */
    public boolean updateLayoutStatus(SGuiClient client, int newLayoutStatus) throws Exception {
        boolean done = false;
        SSrvLock lock = null;
        
        try {
            mnLayoutStatus = newLayoutStatus;
            
            lock = SSrvUtils.gainLock(client.getSession(), 
                    ((SClientInterface) client).getSessionXXX().getCompany().getPkCompanyId(), 
                    SModConsts.FIN_LAY_BANK, getPrimaryKey(), getTimeout());
            
            if (lock != null && lock.getLockStatus() == SSrvConsts.LOCK_GAINED) {
                String sql = "UPDATE " + getSqlTable() + " SET "
                        + "lay_st = " + mnLayoutStatus + "" + 
                        (mnLayoutStatus == SDbBankLayout.STATUS_APPROVED ? ", auth_req = " + ++mnAuthorizationRequests : "") + " " + // if needed, increment count of requests
                        getSqlWhere() + ";";
                client.getSession().getStatement().execute(sql);
                done = true;
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            if (lock != null) {
                SSrvUtils.releaseLock(client.getSession(), lock);
            }
        }
        
        return done;
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBankLayoutId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBankLayoutId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mlTimeout = 1000 * 60 * 120; // 2 hrs

        mnPkBankLayoutId = 0;
        mtDateLayout = null;
        mtDateDue = null;
        msConcept = "";
        msAgreement = "";
        msAgreementReference = "";
        mnConsecutive = 0;
        mdExchangeRate = 0;
        mdAmount = 0;
        mdAmountPayed = 0;
        mdExchangeRateAcc = 0;
        mnTransfers = 0;
        mnTransfersPayed = 0;
        mnDocs = 0;
        mnDocsPayed = 0;
	mnLayoutStatus = 0;
        msLayoutText = "";
        msLayoutXml = "";
        mnTransactionType = 0;
	mnAuthorizationRequests = 0;
        mbClosedPayment = false;
        mbDeleted = false;
        mnFkBankLayoutTypeId = 0;
        mnFkBankCompanyBranchId = 0;
        mnFkBankAccountCashId = 0;
        mnFkDpsCurrencyId = 0;
        mnFkUserClosedPaymentId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserClosedPayment = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mnXtaLayoutBank = 0;
        mnXtaBankPaymentTypeId = 0;
        mnXtaBankCurrencyId = 0;
        msXtaBankLayoutType = "";
        
        msAuxLayoutPath = "";    
        maAuxLayoutBankPaymentRows = new ArrayList<>();
        maAuxLayoutBankXmlRows = new ArrayList<>();
        maAuxLayoutBankRecords = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_lay_bank = " + mnPkBankLayoutId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_lay_bank = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        String sql = "";
        ResultSet resultSet = null;

        mnPkBankLayoutId = 0;

        sql = "SELECT COALESCE(MAX(id_lay_bank), 0) + 1 FROM fin_lay_bank " + " ";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            mnPkBankLayoutId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkBankLayoutId = resultSet.getInt("id_lay_bank");
            mtDateLayout = resultSet.getDate("dt_lay");
            mtDateDue = resultSet.getDate("dt_due");
            msConcept = resultSet.getString("cpt");
            mnConsecutive = resultSet.getInt("con");
            mdAmount = resultSet.getDouble("amt");
            mdAmountPayed = resultSet.getDouble("amt_pay");
            mdExchangeRateAcc = resultSet.getDouble("exc_rate_acc");
            mnTransfers = resultSet.getInt("tra");
            mnTransfersPayed = resultSet.getInt("tra_pay");
            mnDocs = resultSet.getInt("dps");
            mnDocsPayed = resultSet.getInt("dps_pay");
            mnLayoutStatus = resultSet.getInt("lay_st");
            msLayoutText = resultSet.getString("lay_txt");
            msLayoutXml = resultSet.getString("lay_xml");
            mnTransactionType = resultSet.getInt("trn_tp");
            mnAuthorizationRequests = resultSet.getInt("auth_req");
            mbClosedPayment = resultSet.getBoolean("b_clo_pay");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkBankLayoutTypeId = resultSet.getInt("fk_tp_lay_bank");
            mnFkBankCompanyBranchId = resultSet.getInt("fk_bank_cob");
            mnFkBankAccountCashId = resultSet.getInt("fk_bank_acc_cash");
            mnFkDpsCurrencyId = resultSet.getInt("fk_dps_cur");
            mnFkUserClosedPaymentId = resultSet.getInt("fk_usr_clo_pay");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserClosedPayment = resultSet.getTimestamp("ts_usr_clo_pay");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // read aswell extra members:
            
            msSql = "SELECT lay_bank, tp_lay_bank, fid_tp_pay_bank FROM " + SModConsts.TablesMap.get(SModConsts.FINU_TP_LAY_BANK) + " WHERE id_tp_lay_bank = " + mnFkBankLayoutTypeId + " ";
            resultSet = session.getStatement().executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                mnXtaLayoutBank = resultSet.getInt("lay_bank");
                mnXtaBankPaymentTypeId = resultSet.getInt("fid_tp_pay_bank");
                msXtaBankLayoutType = resultSet.getString("tp_lay_bank");
            }
            
            msSql = "SELECT fid_cur FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ACC_CASH) + " WHERE id_cob = " + mnFkBankCompanyBranchId + " AND id_acc_cash = " + mnFkBankAccountCashId + " ";
            resultSet = session.getStatement().executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                mnXtaBankCurrencyId = resultSet.getInt(1);
            }
            
            // finish registry reading:

            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }
    
    @Override
    public boolean canSave(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);
        
        if (can) {
            can = validateLayoutBankRecordsPeriods(session);

            if (can) {
                can = validateLayoutBankPaymentDpsStatus(/*session*/);
            }
        }
        
        return can;
    }
    
    @Override
    public boolean canDelete(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);
        
        if (can) {
            can = validateLayoutBankRecordsPeriods(session);
            
            if (can && mnTransfersPayed > 0) {
                can = false;
                msQueryResult = "¡Existen documentos con pagos aplicados!";
            }
        }
        
        return can;
    }
    
    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
        }

        processLayoutBankPaymentRows(session);
        
        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            mnFkUserClosedPaymentId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO fin_lay_bank VALUES (" +
                    mnPkBankLayoutId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLayout) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateDue) + "', " + 
                    "'" + msConcept + "', " + 
                    mnConsecutive + ", " + 
                    mdAmount + ", " + 
                    mdAmountPayed + ", " + 
                    mdExchangeRateAcc + ", " + 
                    mnTransfers + ", " + 
                    mnTransfersPayed + ", " + 
                    mnDocs + ", " + 
                    mnDocsPayed + ", " + 
		    mnLayoutStatus + ", " + 
                    "'" + msLayoutText + "', " + 
                    "'" + msLayoutXml + "', " +
                    mnTransactionType + ", " + 
                    mnAuthorizationRequests + ", " + 
                    (mbClosedPayment ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkBankLayoutTypeId + ", " + 
                    mnFkBankCompanyBranchId + ", " + 
                    mnFkBankAccountCashId + ", " + 
                    mnFkDpsCurrencyId + ", " +
                    mnFkUserClosedPaymentId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            msSql = "UPDATE fin_lay_bank SET " +
                    "dt_lay = '" + SLibUtils.DbmsDateFormatDate.format(mtDateLayout) + "', " +
                    "dt_due = '" + SLibUtils.DbmsDateFormatDate.format(mtDateDue) + "', " +
                    "cpt = '" + msConcept + "', " +
                    "con = " + mnConsecutive + ", " +
                    "amt = " + mdAmount + ", " +
                    "amt_pay = " + mdAmountPayed + ", " +
                    "exc_rate_acc = " + mdExchangeRateAcc + ", " +
                    "tra = " + mnTransfers + ", " +
                    "tra_pay = " + mnTransfersPayed + ", " +
                    "dps = " + mnDocs + ", " +
                    "dps_pay = " + mnDocsPayed + ", " +
                    "lay_st = " + mnLayoutStatus + ", " +
                    "lay_txt = '" + msLayoutText + "', " +
                    "lay_xml = '" + msLayoutXml + "', " +
                    "trn_tp = " + mnTransactionType + ", " +
                    "auth_req = " + mnAuthorizationRequests + ", " +
                    "b_clo_pay = " + (mbClosedPayment ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_lay_bank = " + mnFkBankLayoutTypeId + ", " +
                    "fk_bank_cob = " + mnFkBankCompanyBranchId + ", " +
                    "fk_bank_acc_cash = " + mnFkBankAccountCashId + ", " +
                    "fk_dps_cur = " + mnFkDpsCurrencyId + ", " +
                    "fk_usr_clo_pay = " + mnFkUserClosedPaymentId + ", " +
                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_clo_pay = " + "NOW()" + ", " +
                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    "WHERE id_lay_bank = " + mnPkBankLayoutId + " ";
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public void saveField(Statement statement, int[] pk, int field, Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";
        switch (field) {
            case FIELD_CLOSE:
                msSql += "b_clo_pay = " + (Boolean) value + " ";

                break;
            case FIELD_CLOSE_USER:
                msSql += "fk_usr_clo_pay = " + (int) value + ", ts_usr_clo_pay = NOW() ";

                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);
        
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbBankLayout clone() throws CloneNotSupportedException {
        SDbBankLayout registry = new SDbBankLayout();

        registry.setPkBankLayoutId(this.getPkBankLayoutId());
        registry.setDateLayout(this.getDateLayout());
        registry.setDateDue(this.getDateDue());
        registry.setConcept(this.getConcept());
        registry.setConsecutive(this.getConsecutive());
        registry.setExchangeRate(this.getExchangeRate());
        registry.setAmount(this.getAmount());
        registry.setAmountPayed(this.getAmountPayed());
        registry.setExchangeRateAcc(this.getExchangeRateAcc());
        registry.setTransfers(this.getTransfers());
        registry.setTransfersPayed(this.getTransfersPayed());
        registry.setDocs(this.getDocs());
        registry.setDocsPayed(this.getDocsPayed());
        registry.setLayoutStatus(this.getLayoutStatus());
        registry.setLayoutText(this.getLayoutText());
        registry.setLayoutXml(this.getLayoutXml());
        registry.setTransactionType(this.getTransactionType());
        registry.setAuthorizationRequests(this.getAuthorizationRequests());
        registry.setClosedPayment(this.isClosedPayment());
        registry.setDeleted(this.isDeleted());
        registry.setFkBankLayoutTypeId(this.getFkBankLayoutTypeId());
        registry.setFkBankCompanyBranchId(this.getFkBankCompanyBranchId());
        registry.setFkBankAccountCashId(this.getFkBankAccountCashId());
        registry.setFkDpsCurrencyId(this.getFkDpsCurrencyId());
        registry.setFkUserClosedPaymentId(this.getFkUserClosedPaymentId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserClosedPayment(this.getTsUserClosedPayment());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setXtaLayoutBank(this.getXtaLayoutBank());
        registry.setXtaBankPaymentTypeId(this.getXtaBankPaymentTypeId());
        registry.setXtaBankCurrencyId(this.getXtaBankCurrencyId());
        registry.setXtaBankLayoutType(this.getXtaBankLayoutType());
        
        registry.setAuxLayoutPath(this.getAuxLayoutPath());
        registry.setExchangeRateAcc(this.getExchangeRateAcc());

        for (SLayoutBankPaymentRow child : maAuxLayoutBankPaymentRows) {
            registry.getAuxLayoutBankPaymentRows().add(child.clone());
        }
        
        for (SLayoutBankXmlRow child : maAuxLayoutBankXmlRows) {
            registry.getAuxLayoutBankXmlRows().add(child.clone());
        }
        
        for (SLayoutBankRecord child : maAuxLayoutBankRecords) {
            registry.getAuxLayoutBankRecords().add(child.clone());
        }
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}

/*
 * Clase auxiliar para juntar todos los pagos de bancos correspondientes a una sola cuenta
 */

class SLayoutPayments {
    public int[] moAccountId;
    public SLayoutBankRecordKey moLayoutBankRecordKey;
    public boolean mbAdded;
    public Vector <SLayoutBankPayment> moLayoutBankPayments;
    
    public SLayoutPayments(int[] accountId, SLayoutBankRecordKey layoutBankRecordKey){
        moAccountId = accountId;
        moLayoutBankRecordKey = layoutBankRecordKey;
        mbAdded = false;
        moLayoutBankPayments = new Vector<>();
    }
    
    public Vector getLayoutBankPayments() { return moLayoutBankPayments; }
}
