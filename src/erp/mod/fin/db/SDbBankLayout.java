/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STableUtilities;
import erp.mcfg.data.SDataParamsErp;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataBookkeepingNumber;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SFinAccountConfigEntry;
import erp.mfin.data.SFinAccountUtilities;
import erp.mfin.data.SFinUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SDbBizPartner;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDsm;
import erp.mtrn.data.SDataDsmEntry;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public class SDbBankLayout extends SDbRegistryUser {
    
    public static final int FIELD_CLOSE = FIELD_BASE + 1;
    public static final int FIELD_CLOSE_USER = FIELD_BASE + 2;
    
    protected int mnPkBankLayoutId;
    protected Date mtDateLayout;
    protected Date mtDateDue;
    protected String msConcept;
    protected int mnConsecutive;
    protected double mdAmount;
    protected double mdAmountPayed;
    protected int mnTransfers;
    protected int mnTransfersPayed;
    protected int mnDocs;
    protected int mnDocsPayed;
    protected String msLayoutText;
    protected String msLayoutXml;
    protected boolean mbClosedPayment;
    //protected boolean mbDeleted;
    protected int mnFkBankLayoutTypeId;
    protected int mnFkBankCompanyBranchId;
    protected int mnFkBankAccountCashId;
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
    
    protected String msAuxTitle;
    protected String msAuxLayoutPath;
    
    protected ArrayList<SLayoutBankPaymentRow> maBankPaymentRows;
    protected ArrayList<SLayoutBankRecord> maBankRecords;
    protected ArrayList<SLayoutBankXmlRow> maXmlRows;
    
    private void computeLayout(SGuiSession session) throws Exception {
        boolean found = false;
        SXmlBankLayout layoutXml = new SXmlBankLayout();
        SXmlBankLayoutPayment xmlLayoutPay = null;
        SXmlBankLayoutPaymentDoc xmlLayoutDoc = null;
        ArrayList<SXmlBankLayoutPayment> xmlLayoutPays = new ArrayList<SXmlBankLayoutPayment>();
        
        layoutXml.getAttribute(SXmlBankLayout.ATT_LAY_ID).setValue(mnPkBankLayoutId);
        
        for (SLayoutBankXmlRow xmlRow : maXmlRows) {
            xmlLayoutDoc = new SXmlBankLayoutPaymentDoc();

            xmlLayoutDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR).setValue(xmlRow.getDpsYear());
            xmlLayoutDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC).setValue(xmlRow.getDpsDoc());
            xmlLayoutDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).setValue(xmlRow.getAmount());
            
            for (SXmlBankLayoutPayment payment : xmlLayoutPays) {
                found = false;
                
                if (SLibUtilities.compareKeys(new int[] { xmlRow.getBizPartnerBranch(), xmlRow.getBizPartnerBranchAccount() }, 
                        new int[] { (int) payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).getValue(), (int) payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).getValue() })) {
                    found = true;
                    payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).setValue(((double) payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).getValue()) + xmlRow.getAmount());
                    payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REF).setValue(((String) payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REF).getValue()) + "," + xmlRow.getReference());
                    payment.getXmlElements().add(xmlLayoutDoc);
                    break;
                }
            }
            
            if (!found) {
                xmlLayoutPay = new SXmlBankLayoutPayment();
            
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).setValue(xmlRow.getAmount());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REF).setValue(xmlRow.getReference());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CPT).setValue(xmlRow.getConcept());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_VOU).setValue(xmlRow.getHsbcFiscalVoucher());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_ACC_TP).setValue(xmlRow.getHsbcAccountType());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_BANK_CODE).setValue(xmlRow.getHsbcBankCode());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_DBT).setValue(xmlRow.getHsbcFiscalIdDebit());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_CRD).setValue(xmlRow.getHsbcFiscalIdCredit());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_DCRP).setValue(xmlRow.getDescription());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_SAN_BANK_CODE).setValue(xmlRow.getSantanderBankCode());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_BANK_CODE).setValue(xmlRow.getBajioBankCode());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_NICK).setValue(xmlRow.getBajioBankNick());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_KEY).setValue(xmlRow.getBankKey());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).setValue(xmlRow.getIsToPayed());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).setValue(xmlRow.getBizPartnerBranch());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).setValue(xmlRow.getBizPartnerBranchAccount());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR).setValue(xmlRow.getRecYear());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_PER).setValue(xmlRow.getRecPeriod());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_BKC).setValue(xmlRow.getRecBookkeepingCenter());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_REC_TP).setValue(xmlRow.getRecRecordType());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_NUM).setValue(xmlRow.getRecNumber());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_YEAR).setValue(xmlRow.getBookkeepingYear());
                xmlLayoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_NUM).setValue(xmlRow.getBookkeepingNumber());
                
                xmlLayoutPay.getXmlElements().add(xmlLayoutDoc);
                
                xmlLayoutPays.add(xmlLayoutPay);
            }
        }
        layoutXml.getXmlElements().addAll(xmlLayoutPays);
        msLayoutXml = layoutXml.getXmlString();
        
        computeLayoutText(session, xmlLayoutPays);
    }
    
    private void computeLayoutText(SGuiSession session, ArrayList<SXmlBankLayoutPayment> xmlLayoutPays) throws Exception {
        int layout = 0;
        int layoutBank = 0;
        int currencyId = 0;
        int bizPartnerId = 0;
        String bizPartner = "";
        String accountDebit = "";
        String accountCredit = "";
        String layoutTitle = "";
        String sql = "";
        ResultSet resultSet = null;
        SLayoutBankPaymentTxt payment = null;
        ArrayList<SLayoutBankPaymentTxt> payments = null;

        // layout name:
        
        sql = "SELECT fid_tp_pay_bank, tp_lay_bank, lay_bank "
                + "FROM erp.finu_tp_lay_bank "
                + "WHERE id_tp_lay_bank = " + mnFkBankLayoutTypeId;
        
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            layout = resultSet.getInt(1);
            layoutTitle = SLibUtils.textToAscii(resultSet.getString(2));
            layoutBank = resultSet.getInt(3);
        }
        
        payments = new ArrayList<SLayoutBankPaymentTxt>();
        
        for (SXmlBankLayoutPayment layoutPayment : xmlLayoutPays) {
            payment = new SLayoutBankPaymentTxt();
            
            // BizPartner:
        
            sql = "SELECT bp.id_bp, bp.bp "
                    + "FROM erp.bpsu_bpb AS bpb "
                    + "INNER JOIN erp.bpsu_bp AS bp ON bpb.fid_bp = bp.id_bp "
                    + "WHERE bpb.id_bpb = " + (Integer) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).getValue();

            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                bizPartnerId = resultSet.getInt(1);
                bizPartner = resultSet.getString(2);
            }
            
            // Account Debit:
        
            sql = "SELECT acc_num "
                    + "FROM fin_acc_cash AS cash "
                    + "INNER JOIN erp.bpsu_bank_acc AS bank ON bank.id_bpb = cash.fid_bpb_n AND bank.id_bank_acc = cash.fid_bank_acc_n "
                    + "WHERE cash.id_cob = " + mnFkBankCompanyBranchId + " "
                    + "AND cash.id_acc_cash = " + mnFkBankAccountCashId;

            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                accountDebit = resultSet.getString(1);
            }
            
            // Account Credit:
        
            sql = "SELECT fid_cur, " + (layout == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? "acc_num " : "acc_num_std ")
                    + "FROM erp.bpsu_bank_acc "
                    + "WHERE id_bpb = " + (Integer) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).getValue() + " "
                    + "AND id_bank_acc = " + (Integer) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).getValue();

            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                currencyId = resultSet.getInt(1);
                accountCredit = resultSet.getString(2);
            }
            
            payment.setTotalAmount((double) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).getValue());
            payment.setReference((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REF).getValue());
            payment.setConcept((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CPT).getValue());
            payment.setDescription((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_DCRP).getValue());
            payment.setBizPartnerId(bizPartnerId);
            payment.setBizPartner(bizPartner);
            payment.setAccountDebit(accountDebit);
            payment.setCurrencyId(currencyId);
            payment.setAccountCredit(accountCredit);
            payment.setHsbcFiscalVoucher((Integer) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_VOU).getValue());
            payment.setHsbcBankCode((Integer) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_BANK_CODE).getValue());
            payment.setHsbcAccountType((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_ACC_TP).getValue());
            payment.setHsbcFiscalIdDebit((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_DBT).getValue());
            payment.setHsbcFiscalIdCredit((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_CRD).getValue());
            payment.setSantanderBankCode((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_SAN_BANK_CODE).getValue());
            payment.setBajioBankCode((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_BANK_CODE).getValue());
            payment.setBajioBankNick((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_NICK).getValue());
            payment.setBankKey((int) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_KEY).getValue());
            
            payments.add(payment);
        }
        
        switch (layout) {
            case SDataConstantsSys.FINS_TP_PAY_BANK_THIRD:
                switch (layoutBank) {
                   case SFinConsts.LAY_BANK_HSBC:
                       msLayoutText = SFinUtilities.createLayoutHsbcThird(payments, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_SANTANDER:
                       msLayoutText = SFinUtilities.createLayoutSantanderThird(payments, mtDateLayout, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_BANBAJIO:
                       msLayoutText = SFinUtilities.createLayoutBanBajioThird(payments, layoutTitle, mtDateLayout, mnConsecutive);
                      break;
                  case SFinConsts.LAY_BANK_BBVA:
                       msLayoutText = SFinUtilities.createLayoutBbvaThird(payments, layoutTitle);
                      break;
                   default :
                       break;
                   }
               break;
           case SDataConstantsSys.FINS_TP_PAY_BANK_TEF:
                switch (layoutBank) {
                   case SFinConsts.LAY_BANK_HSBC:
                       msLayoutText = SFinUtilities.createLayoutHsbcTef(payments, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_SANTANDER:
                       msLayoutText = SFinUtilities.createLayoutSantanderTef(payments, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_BANBAJIO:
                       msLayoutText = SFinUtilities.createLayoutBanBajioTef(payments, layoutTitle, mtDateLayout, mnConsecutive);
                      break;
                   case SFinConsts.LAY_BANK_BBVA:
                       msLayoutText = SFinUtilities.createLayoutBbvaTef(payments, layoutTitle);
                      break;
                   default :
                       break;
                   }
               break;
           case SDataConstantsSys.FINS_TP_PAY_BANK_SPEI_FD_N:
                switch (layoutBank) {
                   case SFinConsts.LAY_BANK_HSBC:
                       msLayoutText = SFinUtilities.createLayoutHsbcSpeiFdN(payments, layoutTitle);
                       break;
                  case SFinConsts.LAY_BANK_SANTANDER:
                       msLayoutText = SFinUtilities.createLayoutSantanderSpeiFdN(payments, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_BANBAJIO:
                       msLayoutText = SFinUtilities.createLayoutBanBajioSpeiFdN(payments, layoutTitle, mtDateLayout, mnConsecutive);
                      break;
                   case SFinConsts.LAY_BANK_BBVA:
                       msLayoutText = SFinUtilities.createLayoutBbvaSpei(payments, layoutTitle);
                      break;
                   default :
                       break;
                   }
               break;
           case SDataConstantsSys.FINS_TP_PAY_BANK_SPEI_FD_Y:
                switch (layoutBank) {
                   case SFinConsts.LAY_BANK_HSBC:
                       msLayoutText = SFinUtilities.createLayoutHsbcSpeiFdY(payments, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_SANTANDER:
                       msLayoutText = SFinUtilities.createLayoutSantanderSpeiFdY(payments, layoutTitle);
                      break;
                   case SFinConsts.LAY_BANK_BBVA:
                       msLayoutText = SFinUtilities.createLayoutBbvaSpei(payments, layoutTitle);
                      break;
                   default :
                       break;
                   }
               break;
           default :
               break;
       }
    }
    
    private void processLayoutBankPayments(final SGuiSession session) throws Exception {
        boolean found = false;
        SLayoutBankRecord bankRecord = null;
        
        for (SLayoutBankPaymentRow paymentRow : maBankPaymentRows) {
            // payment for delete:
            
            found = false;
            if (paymentRow.getFinRecordLayoutOld() != null) {
                if (paymentRow.getFinRecordLayout() == null ||
                        !(SLibUtils.compareKeys(paymentRow.getFinRecordLayout().getPrimaryKey(), paymentRow.getFinRecordLayoutOld().getPrimaryKey()))) {
                    paymentRow.getLayoutBankPayment().setAction(2);// 2: remove payment
                    
                    for (SLayoutBankRecord bankRecordRow : maBankRecords) {
                        if (SLibUtils.compareKeys(bankRecordRow.getFinRecordLayout().getPrimaryKey(), paymentRow.getFinRecordLayoutOld().getPrimaryKey())) {
                            bankRecordRow.getLayoutBankPayments().add(paymentRow.getLayoutBankPayment());
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) {
                        bankRecord = new SLayoutBankRecord(paymentRow.getFinRecordLayoutOld());
                        bankRecord.getLayoutBankPayments().add(paymentRow.getLayoutBankPayment());
                        maBankRecords.add(bankRecord);
                    }
                }
            }
            
            // payment for apply:
            
            if (paymentRow.getFinRecordLayout() != null) {
                if (paymentRow.getFinRecordLayoutOld() == null ||
                        !(SLibUtils.compareKeys(paymentRow.getFinRecordLayout().getPrimaryKey(), paymentRow.getFinRecordLayoutOld().getPrimaryKey()))) {
                    paymentRow.getLayoutBankPayment().setAction(1);// 1: apply payment
                    
                    for (SLayoutBankRecord bankRecordRow : maBankRecords) {
                        if (SLibUtils.compareKeys(bankRecordRow.getFinRecordLayout().getPrimaryKey(), paymentRow.getFinRecordLayout().getPrimaryKey())) {
                            bankRecordRow.getLayoutBankPayments().add(paymentRow.getLayoutBankPayment());
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) {
                        bankRecord = new SLayoutBankRecord(paymentRow.getFinRecordLayout());
                        bankRecord.getLayoutBankPayments().add(paymentRow.getLayoutBankPayment());
                        maBankRecords.add(bankRecord);
                    }
                }
            }
        }
        
        if (!maBankRecords.isEmpty()) {
            processLayoutBankRecord(session);
        }
    }
    
    private void processLayoutBankRecord(final SGuiSession session) throws Exception {
        SDataRecord record = null;
        
        for (SLayoutBankRecord bankRecord : maBankRecords) {
            record = createRecord(bankRecord, session);
            if (record.save(session.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
            }
            if (record.read(bankRecord.getFinRecordLayout().getPrimaryKey(), session.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
            updateLayoutXml(record.getDbmsRecordEntries());
        }
    }
    
    private SDataRecord createRecord(final SLayoutBankRecord bankRecord, final SGuiSession session) throws Exception {
        String sBizPartner = "";
        double amountPayed = 0;
        int nSortingPosition = 0;
        int nBookkeepingYear = 0;
        int nBookkeepingNum = 0;
        Statement statementAux = null;
        SDataDsmEntry oDsmEntry = null;
        SDataDsm oDsm = new SDataDsm();
        SDataDps dps = null;
        SDataRecord record = null;
        SDataRecord recordDsm = null;
        Vector<SDataRecordEntry> recordEntrys = null;
        SDataBookkeepingNumber bookkeepingNumber = null;
        ArrayList<String> aReference = null;
        String reference = "";
        String referenceBank = "";
        
        statementAux = session.getStatement().getConnection().createStatement();
        
        recordEntrys = new Vector<SDataRecordEntry>();
        record = new SDataRecord();
        dps = new SDataDps();
        
        if (record.read(bankRecord.getFinRecordLayout().getPrimaryKey(), statementAux) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
        }
        
        for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
            if (!entry.getIsDeleted()) {
                nSortingPosition = entry.getSortingPosition();
            }
        }

        for (SLayoutBankPayment bankPayment : bankRecord.getLayoutBankPayments()) {
            // Remove payments:
            amountPayed = 0;
            referenceBank = "";
            aReference = new ArrayList<String>();
            
            bookkeepingNumber = new SDataBookkeepingNumber();
            bookkeepingNumber.setPkYearId(record.getPkYearId());
            bookkeepingNumber.setFkUserNewId(session.getUser().getPkUserId());
            if (bookkeepingNumber.save(session.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
            }
            else {
                nBookkeepingYear = bookkeepingNumber.getPkYearId();
                nBookkeepingNum = bookkeepingNumber.getPkNumberId();
            }
            
            if (bankPayment.getAction() == 2) { // remove payment
                /*
                for (SLayoutBankDps bankDps : bankPayment.getLayoutBankDps()) {
                    for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
                        if (SLibUtils.compareKeys(new int[] { entry.getUserId() }, new int[] { bankDps.getUserId() })) {
                            entry.setIsDeleted(true);
                            entry.setIsRegistryEdited(true);
                        }
                    }
                }
                */
                for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
                    if (SLibUtils.compareKeys(new int[] { entry.getFkBookkeepingYearId_n(), entry.getFkBookkeepingNumberId_n() }, new int[] { bankPayment.getFkBookkeepingYearId_n(), bankPayment.getFkBookkeepingNumberId_n() }) /*&&
                            SLibUtils.belongsTo(new int[] { entry.getFkSystemMoveCategoryIdXXX(), entry.getFkSystemMoveTypeIdXXX() },  new int[][] { SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH })*/) {
                        entry.setIsDeleted(true);
                        entry.setIsRegistryEdited(true);
                    }
                }
                mnTransfersPayed--;
            }
            else {
                // Settings of document:
                amountPayed += bankPayment.getAmount();

                for (SLayoutBankDps bankDps : bankPayment.getLayoutBankDps()) {
                    if (dps.read(new int[] { bankDps.getPkYearId(), bankDps.getPkDocId() }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    oDsmEntry = new SDataDsmEntry();
                    
                    sBizPartner = session.readField(SModConsts.BPSU_BP, new int[] { dps.getFkBizPartnerId_r() }, SDbBizPartner.FIELD_NAME_COMM) + "";

                    oDsmEntry.setPkYearId(session.getCurrentYear());
                    oDsmEntry.setFkUserNewId(session.getUser().getPkUserId());
                    
                    oDsmEntry.setSourceReference("");
                    oDsmEntry.setFkSourceCurrencyId(bankPayment.getCurrencyId());
                    oDsmEntry.setSourceValueCy(bankPayment.getAmount());
                    oDsmEntry.setSourceValue(bankPayment.getAmount());
                    oDsmEntry.setSourceExchangeRateSystem(bankPayment.getExcRate());
                    oDsmEntry.setSourceExchangeRate(bankPayment.getExcRate());
                    
                    oDsmEntry.setFkDestinyDpsYearId_n(dps.getPkYearId());
                    oDsmEntry.setFkDestinyDpsDocId_n(dps.getPkDocId());
                    oDsmEntry.setFkDestinyCurrencyId(dps.getFkCurrencyId());
                    oDsmEntry.setDestinyValueCy(bankDps.getDpsAmount());
                    oDsmEntry.setDestinyValue(bankDps.getDpsAmount());
                    oDsmEntry.setDestinyExchangeRateSystem(dps.getExchangeRate());
                    oDsmEntry.setDestinyExchangeRate(dps.getExchangeRate());
                    oDsmEntry.setDbmsFkDpsCategoryId(dps.getFkDpsCategoryId());
                    oDsmEntry.setDbmsDestinyDps((!dps.getNumberSeries().isEmpty() ? dps.getNumberSeries() + "-" : "") + dps.getNumber());
                    oDsmEntry.setDbmsSubclassMove(session.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP, SDbRegistry.FIELD_NAME) + "");
                    oDsmEntry.setDbmsBiz(sBizPartner);
                    oDsmEntry.setDbmsDestinyTpDps(session.readField(SModConsts.TRNU_TP_DPS, new int[] { dps.getFkDpsCategoryId(), dps.getFkDpsClassId(), dps.getFkDpsTypeId() }, SDbRegistry.FIELD_CODE) + "");

                    /*
                    oDsmEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
                    oDsmEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
                    oDsmEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
                    */
                    oDsmEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP[0]);
                    oDsmEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP[1]);
                    oDsmEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP[2]);
                    oDsmEntry.setDbmsCtSysMovId(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0]);
                    oDsmEntry.setDbmsTpSysMovId(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1]);
                    oDsm.setDbmsSubsystemTypeBiz(session.readField(SModConsts.BPSS_CT_BP, new int[] { SDataConstantsSys.BPSS_CT_BP_SUP }, SDbRegistry.FIELD_CODE) + "");
                    oDsmEntry.setFkBizPartnerId(dps.getFkBizPartnerId_r());
                    oDsmEntry.setDbmsFkBizPartnerBranchId_n(dps.getFkBizPartnerBranchId());

                    Vector<SFinAccountConfigEntry> config = SFinAccountUtilities.obtainBizPartnerAccountConfigs((SClientInterface) session.getClient(), dps.getFkBizPartnerId_r(), SDataConstantsSys.BPSS_CT_BP_SUP,
                            record.getPkBookkeepingCenterId(), record.getDate(), SDataConstantsSys.FINS_TP_ACC_BP_OP, dps.getFkDpsCategoryId() == SDataConstantsSys.TRNS_CT_DPS_SAL);
                    if (config.size() > 0) {
                        oDsmEntry.setDbmsAccountOp(config.get(0).getAccountId());
                    }
                    oDsm.getDbmsEntry().add(oDsmEntry);
                    
                    oDsm.setDbmsPkRecordTypeId(SDataConstantsSys.FINU_TP_REC_SUBSYS_SUP);

                    oDsm.setDate(session.getCurrentDate());
                    oDsm.setDbmsErpTaxModel(((SDataParamsErp) session.getConfigSystem()).getTaxModel());
                    oDsm.setFkSubsystemCategoryId(SDataConstantsSys.BPSS_CT_BP_SUP);
                    oDsm.setFkCompanyBranchId(record.getFkCompanyBranchId_n());
                    oDsm.setFkUserNewId(session.getUser().getPkUserId());
                    oDsm.setDbmsFkCompanyBranch(((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getPkBizPartnerBranchId());
                    oDsm.setDbmsCompanyBranchCode(((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDbmsDataCompany().getDbmsBizPartnerBranch(new int[] { record.getFkCompanyBranchId_n() }).getCode());
                    oDsm.setDbmsErpDecimalsValue(((SDataParamsErp) session.getConfigSystem()).getDecimalsValue());
                    oDsm.setDbmsIsRecordSaved(false);
                    
                    oDsm = (SDataDsm) ((SClientInterface) session.getClient()).getGuiModule(SDataConstants.MOD_FIN).processRegistry(oDsm);
                    recordDsm = oDsm.getDbmsRecord();
                    
                    reference = (!dps.getNumberSeries().isEmpty() ? dps.getNumberSeries() + "-" : "") + dps.getNumber();
                    aReference.add(reference);
                     //(oDsmEntry.getDbmsBiz().toString().length() > 30 ? oDsmEntry.getDbmsBiz().toString().substring(0, 27) + "..." : oDsmEntry.getDbmsBiz())
                    for (SDataRecordEntry entry : recordDsm.getDbmsRecordEntries()) {
                        entry.setConcept(createConceptRecordEntry(session, bankPayment.getBizPartnerBranchId(), bankPayment.getBizPartnerBranchAccountId(), reference, sBizPartner));
                        entry.setSortingPosition(++nSortingPosition);
                        entry.setFkBookkeepingYearId_n(nBookkeepingYear);
                        entry.setFkBookkeepingNumberId_n(nBookkeepingNum);
                        recordEntrys.add(entry);
                    }
                    oDsm.getDbmsEntry().clear();
                }
                for (int i = 0; i < aReference.size(); i++) {
                    referenceBank += (referenceBank.isEmpty() ? "" : (i == aReference.size() ? "" : ", ")) + aReference.get(i);
                }
                recordEntrys.insertElementAt(createRecordEntryAccountCash(session, dps, sBizPartner, amountPayed, nBookkeepingYear, nBookkeepingNum, bankPayment.getBizPartnerBranchId(), bankPayment.getBizPartnerBranchAccountId(), referenceBank), 0);
                mnTransfersPayed++;
            }
        }
        for (SDataRecordEntry entry : recordEntrys) {
            entry.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { entry.getFkAccountId() }));
            entry.setDbmsCurrencyKey(session.getSessionCustom().getCurrencyCode(new int[] { entry.getFkCurrencyId() }));



            if (entry.getFkSystemMoveCategoryIdXXX() == SDataConstantsSys.FINS_CT_SYS_MOV_BPS) {
                entry.setDbmsAccountComplement(sBizPartner);
            }
            record.getDbmsRecordEntries().add(entry);
        }
        
        return record;
    }
    
    private erp.mfin.data.SDataRecordEntry createRecordEntryAccountCash(final SGuiSession session, final SDataDps dps, final String bizPartner, double amountPayed, final int bookkeepingYear, final int bookkeepingNum, final int bizPartnerBank, final int bankBank, String reference) throws Exception {
        int[] keySystemMoveType = null;
        int[] keySystemMoveTypeXXX = null;
        SDataAccountCash accountCash = null;
        SDataRecordEntry entry = new SDataRecordEntry();
        
        accountCash = new SDataAccountCash();
        if (accountCash.read(new int[] { mnFkBankCompanyBranchId, mnFkBankAccountCashId }, session.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
        }

        if (accountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH) {
            keySystemMoveTypeXXX = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH;
        }
        else {
            keySystemMoveTypeXXX = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK;
        }

        entry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        entry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        entry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
        entry.setFkUserNewId(session.getUser().getPkUserId());
        entry.setDbmsAccountingMoveSubclass(session.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL, SDbRegistry.FIELD_NAME) + "");

        //entry.setConcept("F " + (dps.getNumberSeries().length() == 0 ? "" : dps.getNumberSeries() + "-") + dps.getNumber() + "; " + bizPartner);
        entry.setConcept(createConceptRecordEntry(session, bizPartnerBank, bankBank, reference, bizPartner));

        entry.setDebit(0);
        entry.setCredit(amountPayed);
        entry.setExchangeRate(1);
        entry.setExchangeRateSystem(1);
        entry.setDebitCy(0);
        entry.setCreditCy(amountPayed);
        keySystemMoveType = SModSysConsts.FINS_TP_SYS_MOV_MO_SUP_PAY;

        entry.setFkCurrencyId(accountCash.getFkCurrencyId());
        entry.setFkAccountIdXXX(accountCash.getFkAccountId());
        entry.setFkCostCenterIdXXX_n("");
        entry.setIsExchangeDifference(false);
        entry.setIsSystem(true);
        entry.setIsDeleted(false);

        entry.setFkSystemMoveClassId(keySystemMoveType[0]);
        entry.setFkSystemMoveTypeId(keySystemMoveType[1]);

        if (accountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH) {
            entry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[0]);
            entry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[1]);
        }
        else {
            entry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[0]);
            entry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[1]);
        }

        entry.setFkSystemMoveCategoryIdXXX(keySystemMoveTypeXXX[0]);
        entry.setFkSystemMoveTypeIdXXX(keySystemMoveTypeXXX[1]);

        entry.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { accountCash.getFkAccountId() }));
        entry.setDbmsAccountComplement(accountCash.getDbmsCompanyBranchEntity().getEntity());
        entry.setDbmsCostCenter_n("");
        entry.setDbmsCurrencyKey(session.getSessionCustom().getCurrencyCode(new int[] { accountCash.getFkCurrencyId() }));

        entry.setReference("");
        entry.setIsReferenceTax(false);
        entry.setFkTaxBasicId_n(0);
        entry.setFkTaxId_n(0);

        entry.setFkBizPartnerId_nr(dps.getFkBizPartnerId_r());
        entry.setFkBizPartnerBranchId_n(dps.getFkBizPartnerBranchId());

        entry.setFkCompanyBranchId_n(accountCash.getPkCompanyBranchId());
        entry.setFkEntityId_n(accountCash.getPkAccountCashId());
        entry.setUnits(0d);
        entry.setFkItemId_n(0);
        entry.setFkItemAuxId_n(0);
        entry.setFkYearId_n(0);
        entry.setFkDpsYearId_n(dps.getPkYearId());
        entry.setFkDpsDocId_n(dps.getPkDocId());
        entry.setFkDpsAdjustmentYearId_n(0);
        entry.setFkDpsAdjustmentDocId_n(0);
        entry.setFkBookkeepingYearId_n(bookkeepingYear);
        entry.setFkBookkeepingNumberId_n(bookkeepingNum);

        return entry;
    }

    private String createConceptRecordEntry(final SGuiSession session, final int bizPartnerBank, final int bankBank, final String reference, final String bizPartner) throws Exception {
        String bank = "";
        String layoutTitle = "";
        String concept = "";
        String sql = "";
        ResultSet resultSet = null;

        // layout name:
        
        sql = "SELECT fid_tp_pay_bank, tp_lay_bank, lay_bank "
                + "FROM erp.finu_tp_lay_bank "
                + "WHERE id_tp_lay_bank = " + mnFkBankLayoutTypeId;
        
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            layoutTitle = resultSet.getString(2);
        }
        
        // Bank to transfer:
        
        sql = "SELECT ct.bp_key "
                + "FROM erp.bpsu_bank_acc AS bank "
                + "INNER JOIN erp.bpsu_bp AS bp ON bp.id_bp = bank.fid_bank "
                + "INNER JOIN erp.bpsu_bp_ct AS ct ON ct.id_bp = bp.id_bp AND ct.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_SUP + " "
                + "WHERE bank.id_bpb = " + bizPartnerBank + " "
                + "AND bank.id_bank_acc = " + bankBank;

        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            bank = resultSet.getString(1);
        }
        
        concept = layoutTitle + " / " + bank + " / F " + reference + " / " + bizPartner;
        
        return (concept.length() > 100 ? SLibUtilities.textLeft(concept, 100) : concept).trim();
    }
    
    private void updateLayoutXml(Vector<SDataRecordEntry> recordEntries) {
        mnDocsPayed = 0;
        mdAmountPayed = 0;
        
        for (SDataRecordEntry entry : recordEntries) {
            for (SLayoutBankXmlRow xmlRow : maXmlRows) {
                if (SLibUtils.compareKeys(xmlRow.getPrimaryKey(), new int[] { entry.getFkDpsYearId_n(), entry.getFkDpsDocId_n() })) {
                    if (entry.getIsDeleted()) {
                        if (SLibUtils.compareKeys(new int[] { xmlRow.getBookkeepingYear(), xmlRow.getBookkeepingNumber() }, new int[] { entry.getFkBookkeepingYearId_n(), entry.getFkBookkeepingNumberId_n()})) {
                            xmlRow.setAmountPayed(SLibConsts.UNDEFINED);
                            xmlRow.setIsToPayed(false);
                            xmlRow.setRecYear(SLibConsts.UNDEFINED);
                            xmlRow.setRecPeriod(SLibConsts.UNDEFINED);
                            xmlRow.setRecBookkeepingCenter(SLibConsts.UNDEFINED);
                            xmlRow.setRecRecordType("");
                            xmlRow.setRecNumber(SLibConsts.UNDEFINED);
                            xmlRow.setBookkeepingYear(SLibConsts.UNDEFINED);
                            xmlRow.setBookkeepingNumber(SLibConsts.UNDEFINED);
                        }
                    }
                    else {
                        xmlRow.setAmountPayed(xmlRow.getAmount());
                        xmlRow.setIsToPayed(true);
                        xmlRow.setRecYear(entry.getPkYearId());
                        xmlRow.setRecPeriod(entry.getPkPeriodId());
                        xmlRow.setRecBookkeepingCenter(entry.getPkBookkeepingCenterId());
                        xmlRow.setRecRecordType(entry.getPkRecordTypeId());
                        xmlRow.setRecNumber(entry.getPkNumberId());
                        xmlRow.setBookkeepingYear(entry.getFkBookkeepingYearId_n());
                        xmlRow.setBookkeepingNumber(entry.getFkBookkeepingNumberId_n());
                    }
                    break;
                }
            }
        }
        for (SLayoutBankXmlRow xmlRow : maXmlRows) {
            if (xmlRow.getIsToPayed()) {
                mnDocsPayed++;
                mdAmountPayed += xmlRow.getAmountPayed();
            }
        }
    }

    private boolean validatePeriodRecordLayout(final SGuiSession session) throws Exception {
        SDataRecord record = null;
        int i = 0;
        String sMsg = "";
        CallableStatement oCallableStatement = null;
        
        record = new SDataRecord();
        for (SLayoutBankRecord bankRecord : maBankRecords) {
            if (record.read(bankRecord.getFinRecordLayout().getPrimaryKey(), session.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
            
            i = 1;
            oCallableStatement = session.getStatement().getConnection().prepareCall("{ CALL fin_year_per_st(?, ?, ?) }");
            oCallableStatement.setInt(i++, record.getPkYearId());
            oCallableStatement.setInt(i++, record.getPkPeriodId());
            oCallableStatement.registerOutParameter(i++, java.sql.Types.INTEGER);
            oCallableStatement.execute();

            if (oCallableStatement.getBoolean(i - 1)) {
                msQueryResult = sMsg + "¡El período contable '" + record.getPkPeriodId() + "' de la póliza '" + record.getPkRecordTypeId() + " - " + record.getPkNumberId() + "' está cerrado!";
                throw new Exception(msQueryResult);
            }
        }
        
        return true;
    }
    
    public void writeLayout(final SGuiClient client) {
        BufferedWriter bw = null;
        
        File file = new File(msAuxLayoutPath);

        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ASCII"));

            bw.write(msLayoutText);
            bw.close();
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(STableUtilities.class.getName(), e);
        }
    }

    public void setPkBankLayoutId(int n) { mnPkBankLayoutId = n; }
    public void setDateLayout(Date t) { mtDateLayout = t; }
    public void setDateDue(Date t) { mtDateDue = t; }
    public void setConcept(String s) { msConcept = s; }
    public void setConsecutive(int n) { mnConsecutive = n; }
    public void setAmount(double d) { mdAmount = d; }
    public void setAmountPayed(double d) { mdAmountPayed = d; }
    public void setTransfers(int n) { mnTransfers = n; }
    public void setTransfersPayed(int n) { mnTransfersPayed = n; }
    public void setDocs(int n) { mnDocs = n; }
    public void setDocsPayed(int n) { mnDocsPayed = n; }
    public void setLayoutText(String s) { msLayoutText = s; }
    public void setLayoutXml(String s) { msLayoutXml = s; }
    public void setClosedPayment(boolean b) { mbClosedPayment = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkBankLayoutTypeId(int n) { mnFkBankLayoutTypeId = n; }
    public void setFkBankCompanyBranchId(int n) { mnFkBankCompanyBranchId = n; }
    public void setFkBankAccountCashId(int n) { mnFkBankAccountCashId = n; }
    public void setFkUserClosedPaymentId(int n) { mnFkUserClosedPaymentId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserClosedPayment(Date t) { mtTsUserClosedPayment = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public void setAuxTitle(String s) { msAuxTitle = s; }
    public void setAuxLayoutPath(String s) { msAuxLayoutPath = s; }
    
    public int getPkBankLayoutId() { return mnPkBankLayoutId; }
    public Date getDateLayout() { return mtDateLayout; }
    public Date getDateDue() { return mtDateDue; }
    public String getConcept() { return msConcept; }
    public int getConsecutive() { return mnConsecutive; }
    public double getAmount() { return mdAmount; }
    public double getAmountPayed() { return mdAmountPayed; }
    public int getTransfers() { return mnTransfers; }
    public int getTransfersPayed() { return mnTransfersPayed; }
    public int getDocs() { return mnDocs; }
    public int getDocsPayed() { return mnDocsPayed; }
    public String getLayoutText() { return msLayoutText; }
    public String getLayoutXml() { return msLayoutXml; }
    public boolean isClosedPayment() { return mbClosedPayment; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkBankLayoutTypeId() { return mnFkBankLayoutTypeId; }
    public int getFkBankCompanyBranchId() { return mnFkBankCompanyBranchId; }
    public int getFkBankAccountCashId() { return mnFkBankAccountCashId; }
    public int getFkUserClosedPaymentId() { return mnFkUserClosedPaymentId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserClosedPayment() { return mtTsUserClosedPayment; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public String getAuxTitle() { return msAuxTitle; }
    public String getAuxLayoutPath() { return msAuxLayoutPath; }
    
    public ArrayList<SLayoutBankPaymentRow> getBankPaymentRows() { return maBankPaymentRows; }
    public ArrayList<SLayoutBankRecord> getBankRecord() { return maBankRecords; }
    public ArrayList<SLayoutBankXmlRow> getXmlRows() { return maXmlRows; }
    
    public SDbBankLayout() {
        super(SModConsts.FIN_LAY_BANK);
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
        mnConsecutive = 0;
        mdAmount = 0;
        mdAmountPayed = 0;
        mnTransfers = 0;
        mnTransfersPayed = 0;
        mnDocs = 0;
        mnDocsPayed = 0;
        msLayoutText = "";
        msLayoutXml = "";
        mbClosedPayment = false;
        mbDeleted = false;
        mnFkBankLayoutTypeId = 0;
        mnFkBankCompanyBranchId = 0;
        mnFkBankAccountCashId = 0;
        mnFkUserClosedPaymentId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserClosedPayment = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        msAuxTitle = "";
        msAuxLayoutPath = "";
        
        maBankPaymentRows = new ArrayList<SLayoutBankPaymentRow>();
        maBankRecords = new ArrayList<SLayoutBankRecord>();
        maXmlRows = new ArrayList<SLayoutBankXmlRow>();
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
            mnTransfers = resultSet.getInt("tra");
            mnTransfersPayed = resultSet.getInt("tra_pay");
            mnDocs = resultSet.getInt("doc");
            mnDocsPayed = resultSet.getInt("doc_pay");
            msLayoutText = resultSet.getString("lay_txt");
            msLayoutXml = resultSet.getString("lay_xml");
            mbClosedPayment = resultSet.getBoolean("b_clo_pay");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkBankLayoutTypeId = resultSet.getInt("fk_tp_lay_bank");
            mnFkBankCompanyBranchId = resultSet.getInt("fk_bank_cob");
            mnFkBankAccountCashId = resultSet.getInt("fk_bank_acc_cash");
            mnFkUserClosedPaymentId = resultSet.getInt("fk_usr_clo_pay");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserClosedPayment = resultSet.getTimestamp("ts_usr_clo_pay");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;                
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }
        
        processLayoutBankPayments(session);
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
        }
        computeLayout(session);

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
                    mnTransfers + ", " + 
                    mnTransfersPayed + ", " + 
                    mnDocs + ", " + 
                    mnDocsPayed + ", " + 
                    "'" + msLayoutText + "', " + 
                    "'" + msLayoutXml + "', " +
                    (mbClosedPayment ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkBankLayoutTypeId + ", " + 
                    mnFkBankCompanyBranchId + ", " + 
                    mnFkBankAccountCashId + ", " + 
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
                    //"id_lay_bank = " + mnPkLayBankId + ", " +
                    "dt_lay = '" + SLibUtils.DbmsDateFormatDate.format(mtDateLayout) + "', " +
                    "dt_due = '" + SLibUtils.DbmsDateFormatDate.format(mtDateDue) + "', " +
                    "cpt = '" + msConcept + "', " +
                    "con = " + mnConsecutive + ", " +
                    "amt = " + mdAmount + ", " +
                    "amt_pay = " + mdAmountPayed + ", " +
                    "tra = " + mnTransfers + ", " +
                    "tra_pay = " + mnTransfersPayed + ", " +
                    "doc = " + mnDocs + ", " +
                    "doc_pay = " + mnDocsPayed + ", " +
                    "lay_txt = '" + msLayoutText + "', " +
                    "lay_xml = '" + msLayoutXml + "', " +
                    "b_clo_pay = " + (mbClosedPayment ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_lay_bank = " + mnFkBankLayoutTypeId + ", " +
                    "fk_bank_cob = " + mnFkBankCompanyBranchId + ", " +
                    "fk_bank_acc_cash = " + mnFkBankAccountCashId + ", " +
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
    public SDbBankLayout clone() throws CloneNotSupportedException {
        SDbBankLayout registry = new SDbBankLayout();

        registry.setPkBankLayoutId(this.getPkBankLayoutId());
        registry.setDateLayout(this.getDateLayout());
        registry.setDateDue(this.getDateDue());
        registry.setConcept(this.getConcept());
        registry.setConsecutive(this.getConsecutive());
        registry.setAmount(this.getAmount());
        registry.setAmountPayed(this.getAmountPayed());
        registry.setTransfers(this.getTransfers());
        registry.setTransfersPayed(this.getTransfersPayed());
        registry.setDocs(this.getDocs());
        registry.setDocsPayed(this.getDocsPayed());
        registry.setLayoutText(this.getLayoutText());
        registry.setLayoutXml(this.getLayoutXml());
        registry.setClosedPayment(this.isClosedPayment());
        registry.setDeleted(this.isDeleted());
        registry.setFkBankLayoutTypeId(this.getFkBankLayoutTypeId());
        registry.setFkBankCompanyBranchId(this.getFkBankCompanyBranchId());
        registry.setFkBankAccountCashId(this.getFkBankAccountCashId());
        registry.setFkUserClosedPaymentId(this.getFkUserClosedPaymentId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserClosedPayment(this.getTsUserClosedPayment());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
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
    public boolean canSave(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);
        
        can = validatePeriodRecordLayout(session);
        
        return can;
    }
    
    @Override
    public boolean canDelete(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);
        
        if (can) {
            can = validatePeriodRecordLayout(session);
            
            if (can && mnTransfersPayed > 0) {
                can = false;
                msQueryResult = "¡Existen documentos con pagos aplicados!";
            }
        }
        
        return can;
    }
}