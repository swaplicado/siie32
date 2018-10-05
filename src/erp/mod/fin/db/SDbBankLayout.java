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
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores
 */
public class SDbBankLayout extends SDbRegistryUser {
    
    public static final int FIELD_CLOSE = FIELD_BASE + 1;
    public static final int FIELD_CLOSE_USER = FIELD_BASE + 2;
    
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
    
    protected String msAuxTitle;
    protected String msAuxLayoutPath;
    protected String msAuxLayoutType;
    protected double mdAuxLocalLayoutAmount;
    protected int mnXtaLayoutBank;
    protected int mnXtaBankCurrencyId;
    protected int mnXtaBankPaymentType;
    
    protected ArrayList<SLayoutBankPaymentRow> maLayoutBankPaymentRows;
    protected ArrayList<SLayoutBankRecord> maLayoutBankRecords;
    protected ArrayList<SLayoutBankXmlRow> maLayoutBankXmlRows;
    
    /*
     * Private methods
     */
    
    private void updateLayoutXml(Vector<SDataRecordEntry> recordEntries) {
        int[] key = new int[] { SLibConsts.UNDEFINED };
        mnDocsPayed = 0;
        mdAmountPayed = 0;
        
        for (SDataRecordEntry recordEntry : recordEntries) {
            for (SLayoutBankXmlRow layoutBankXmlRow : maLayoutBankXmlRows) {
                if (layoutBankXmlRow.getLayoutXmlRowType() == SModSysConsts.FIN_LAY_BANK_PAY) {
                    key = new int[] { recordEntry.getFkDpsYearId_n(), recordEntry.getFkDpsDocId_n() };
                }
                else if (layoutBankXmlRow.getLayoutXmlRowType() == SModSysConsts.FIN_LAY_BANK_PREPAY) {
                    key = new int[] { recordEntry.getFkBizPartnerId_nr() };
                }
                
                if (SLibUtils.compareKeys(layoutBankXmlRow.getPrimaryKey(), key)) {
                    if (recordEntry.getIsDeleted()) {
                        if (SLibUtils.compareKeys(new int[] { layoutBankXmlRow.getBookkeepingYear(), layoutBankXmlRow.getBookkeepingNumber() }, new int[] { recordEntry.getFkBookkeepingYearId_n(), recordEntry.getFkBookkeepingNumberId_n()})) {
                            layoutBankXmlRow.setAmountPayed(SLibConsts.UNDEFINED);
                            layoutBankXmlRow.setIsToPayed(false);
                            layoutBankXmlRow.setRecYear(SLibConsts.UNDEFINED);
                            layoutBankXmlRow.setRecPeriod(SLibConsts.UNDEFINED);
                            layoutBankXmlRow.setRecBookkeepingCenter(SLibConsts.UNDEFINED);
                            layoutBankXmlRow.setRecRecordType("");
                            layoutBankXmlRow.setRecNumber(SLibConsts.UNDEFINED);
                            layoutBankXmlRow.setBookkeepingYear(SLibConsts.UNDEFINED);
                            layoutBankXmlRow.setBookkeepingNumber(SLibConsts.UNDEFINED);
                        }
                    }
                    else {
                        layoutBankXmlRow.setAmountPayed(layoutBankXmlRow.getAmount());
                        layoutBankXmlRow.setIsToPayed(true);
                        layoutBankXmlRow.setRecYear(recordEntry.getPkYearId());
                        layoutBankXmlRow.setRecPeriod(recordEntry.getPkPeriodId());
                        layoutBankXmlRow.setRecBookkeepingCenter(recordEntry.getPkBookkeepingCenterId());
                        layoutBankXmlRow.setRecRecordType(recordEntry.getPkRecordTypeId());
                        layoutBankXmlRow.setRecNumber(recordEntry.getPkNumberId());
                        layoutBankXmlRow.setBookkeepingYear(recordEntry.getFkBookkeepingYearId_n());
                        layoutBankXmlRow.setBookkeepingNumber(recordEntry.getFkBookkeepingNumberId_n());
                    }
                    break;
                }
            }
        }
        
        for (SLayoutBankXmlRow xmlRow : maLayoutBankXmlRows) {
            if (xmlRow.getIsToPayed()) {
                if (xmlRow.getLayoutXmlRowType() == SModSysConsts.FIN_LAY_BANK_PAY) {
                    mnDocsPayed++;
                }
                mdAmountPayed += xmlRow.getAmountPayed();
            }
        }
    }

    private String composeRecordEntryConcept(final SGuiSession session, final int bizPartnerBank, final int bankBank, final String reference, final String bizPartner) throws Exception {
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
                + "WHERE bank.id_bpb = " + bizPartnerBank + " AND bank.id_bank_acc = " + bankBank;

        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            bank = resultSet.getString(1);
        }
        
        concept = layoutTitle + " / " + bank + " / F " + reference + " / " + bizPartner;
        
        return (concept.length() > 100 ? SLibUtilities.textLeft(concept, 100) : concept).trim();
    }
    
    private erp.mfin.data.SDataRecordEntry createRecordEntryAccountCash(final SGuiSession session, final SDataDps dps, final String bizPartner, double amountPayed, final int bookkeepingYear, final int bookkeepingNum, final int bizPartnerBank, final int bankBank, String reference) throws Exception {
        int[] keySystemMoveType = null;
        int[] keySystemMoveTypeXXX = null;
        
        SDataAccountCash accountCash = new SDataAccountCash();
        
        if (accountCash.read(new int[] { mnFkBankCompanyBranchId, mnFkBankAccountCashId }, session.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
        }

        if (accountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH) {
            keySystemMoveTypeXXX = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH;
        }
        else {
            keySystemMoveTypeXXX = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK;
        }

        SDataRecordEntry recordEntry = new SDataRecordEntry();
        
        recordEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        recordEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        recordEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
        recordEntry.setFkUserNewId(session.getUser().getPkUserId());
        recordEntry.setDbmsAccountingMoveSubclass(session.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL, SDbRegistry.FIELD_NAME) + "");
        recordEntry.setConcept(composeRecordEntryConcept(session, bizPartnerBank, bankBank, reference, bizPartner));
        recordEntry.setDebit(0);
        recordEntry.setCredit(amountPayed);
        recordEntry.setExchangeRate(1);
        recordEntry.setExchangeRateSystem(1);
        recordEntry.setDebitCy(0);
        recordEntry.setCreditCy(amountPayed);
        recordEntry.setFkCurrencyId(accountCash.getFkCurrencyId());
        recordEntry.setFkAccountIdXXX(accountCash.getFkAccountId());
        recordEntry.setFkCostCenterIdXXX_n("");
        recordEntry.setIsExchangeDifference(false);
        recordEntry.setIsSystem(true);
        recordEntry.setIsDeleted(false);

        keySystemMoveType = SModSysConsts.FINS_TP_SYS_MOV_MO_SUP_PAY;
        recordEntry.setFkSystemMoveClassId(keySystemMoveType[0]);
        recordEntry.setFkSystemMoveTypeId(keySystemMoveType[1]);

        if (accountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH) {
            recordEntry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[0]);
            recordEntry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[1]);
        }
        else {
            recordEntry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[0]);
            recordEntry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[1]);
        }

        recordEntry.setFkSystemMoveCategoryIdXXX(keySystemMoveTypeXXX[0]);
        recordEntry.setFkSystemMoveTypeIdXXX(keySystemMoveTypeXXX[1]);

        recordEntry.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { accountCash.getFkAccountId() }));
        recordEntry.setDbmsAccountComplement(accountCash.getDbmsCompanyBranchEntity().getEntity());
        recordEntry.setDbmsCostCenter_n("");
        recordEntry.setDbmsCurrencyKey(session.getSessionCustom().getCurrencyCode(new int[] { accountCash.getFkCurrencyId() }));

        recordEntry.setReference("");
        recordEntry.setIsReferenceTax(false);
        recordEntry.setFkTaxBasicId_n(0);
        recordEntry.setFkTaxId_n(0);

        recordEntry.setFkBizPartnerId_nr(dps.getFkBizPartnerId_r());
        recordEntry.setFkBizPartnerBranchId_n(dps.getFkBizPartnerBranchId());

        recordEntry.setFkCompanyBranchId_n(accountCash.getPkCompanyBranchId());
        recordEntry.setFkEntityId_n(accountCash.getPkAccountCashId());
        recordEntry.setUnits(0d);
        recordEntry.setFkItemId_n(0);
        recordEntry.setFkItemAuxId_n(0);
        recordEntry.setFkYearId_n(0);
        recordEntry.setFkDpsYearId_n(dps.getPkYearId());
        recordEntry.setFkDpsDocId_n(dps.getPkDocId());
        recordEntry.setFkDpsAdjustmentYearId_n(0);
        recordEntry.setFkDpsAdjustmentDocId_n(0);
        recordEntry.setFkBookkeepingYearId_n(bookkeepingYear);
        recordEntry.setFkBookkeepingNumberId_n(bookkeepingNum);

        return recordEntry;
    }

    private SDataRecord createRecord(final SGuiSession session, final SLayoutBankRecord bankRecord) throws Exception {
        String sBizPartner = "";
        double amountPayed = 0;
        int nSortingPosition = 0;
        int nBookkeepingYear = 0;
        int nBookkeepingNum = 0;
        Statement statementAux = null;
        SDataDsmEntry oDsmEntry = null;
        SDataDsm oDsm = new SDataDsm();
        SLayoutBankAccountingAdvance accountingAdvance = null;
        SDataDps dps = null;
        SDataRecord record = null;
        SDataRecord recordDsm = null;
        Vector<SDataRecordEntry> recordEntriesToProcess = null;
        Vector<SDataRecordEntry> recordEntrys = null;
        SDataBookkeepingNumber bookkeepingNumber = null;
        ArrayList<String> aReference = null;
        String reference = "";
        String referenceBank = "";
        
        statementAux = session.getStatement().getConnection().createStatement();
        
        recordEntriesToProcess = new Vector<>();
        recordEntrys = new Vector<>();
        record = new SDataRecord();
        dps = new SDataDps();
        
        if (record.read(bankRecord.getLayoutBankRecordKey().getPrimaryKey(), statementAux) != SLibConstants.DB_ACTION_READ_OK) {
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
            aReference = new ArrayList<>();
            
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
            
            if (bankPayment.getAction() == SLayoutBankPayment.ACTION_PAY_REMOVE) {
                for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
                    if (SLibUtils.compareKeys(new int[] { entry.getFkBookkeepingYearId_n(), entry.getFkBookkeepingNumberId_n() }, new int[] { bankPayment.getFkBookkeepingYearId_n(), bankPayment.getFkBookkeepingNumberId_n() })) {
                        entry.setIsDeleted(true);
                        entry.setIsRegistryEdited(true);
                        recordEntriesToProcess.add(entry);
                    }
                }
                mnTransfersPayed--;
            }
            else {
                // Settings of document:

                amountPayed = SLibUtils.round((amountPayed + bankPayment.getAmount().getAmountOriginal()), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());

                sBizPartner = session.readField(SModConsts.BPSU_BP, new int[] { bankPayment.getBizPartnerId() }, SDbBizPartner.FIELD_NAME_COMM) + "";

                if (bankPayment.getLayoutPaymentType() == SModSysConsts.FIN_LAY_BANK_PAY) {
                    for (SLayoutBankDps bankDps : bankPayment.getLayoutBankDps()) {
                        if (dps.read(new int[] { bankDps.getPkYearId(), bankDps.getPkDocId() }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                        oDsmEntry = new SDataDsmEntry();

                        oDsmEntry.setPkYearId(session.getCurrentYear());
                        oDsmEntry.setFkUserNewId(session.getUser().getPkUserId());
                        
                        oDsmEntry.setSourceReference("");
                        oDsmEntry.setFkSourceCurrencyId(dps.getFkCurrencyId());
                        oDsmEntry.setSourceValueCy(bankDps.getDpsAmount());
                        oDsmEntry.setSourceValue(new SMoney(bankDps.getDpsAmount(),bankDps.getDpsCurId(),bankDps.getDpsExcRate(),bankPayment.getAmount().getCurrencyLocalId()).getAmountLocal());
                        oDsmEntry.setSourceExchangeRateSystem(mdExchangeRate);
                        oDsmEntry.setSourceExchangeRate(bankDps.getDpsExcRate());

                        oDsmEntry.setFkDestinyDpsYearId_n(dps.getPkYearId());
                        oDsmEntry.setFkDestinyDpsDocId_n(dps.getPkDocId());
                        oDsmEntry.setFkDestinyCurrencyId(dps.getFkCurrencyId());
                        oDsmEntry.setDestinyValueCy(bankDps.getDpsAmount());
                        oDsmEntry.setDestinyValue(new SMoney(bankDps.getDpsAmount(),bankDps.getDpsCurId(),bankDps.getDpsExcRate(),bankPayment.getAmount().getCurrencyLocalId()).getAmountLocal());
                        oDsmEntry.setDestinyExchangeRateSystem(mdExchangeRate);
                        oDsmEntry.setDestinyExchangeRate(bankDps.getDpsExcRate());
                        oDsmEntry.setDbmsFkDpsCategoryId(dps.getFkDpsCategoryId());
                        oDsmEntry.setDbmsDestinyDps((!dps.getNumberSeries().isEmpty() ? dps.getNumberSeries() + "-" : "") + dps.getNumber());
                        oDsmEntry.setDbmsSubclassMove(session.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP, SDbRegistry.FIELD_NAME) + "");
                        oDsmEntry.setDbmsBizPartner(sBizPartner);
                        oDsmEntry.setDbmsDestinyTpDps(session.readField(SModConsts.TRNU_TP_DPS, new int[] { dps.getFkDpsCategoryId(), dps.getFkDpsClassId(), dps.getFkDpsTypeId() }, SDbRegistry.FIELD_CODE) + "");

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

                        reference = dps.getDpsNumber();
                        aReference.add(reference);
                        
                        for (SDataRecordEntry entry : recordDsm.getDbmsRecordEntries()) {
                            entry.setConcept(composeRecordEntryConcept(session, bankPayment.getBizPartnerBranchId(), bankPayment.getBizPartnerBranchAccountId(), reference, sBizPartner));
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
                }
                else if (bankPayment.getLayoutPaymentType() == SModSysConsts.FIN_LAY_BANK_PREPAY) {
                    accountingAdvance = new SLayoutBankAccountingAdvance(session, bankPayment.getBizPartnerId(), bankPayment.getBizPartnerBranchId(), bankPayment.getBizPartnerBranchAccountId(), mnFkBankCompanyBranchId, mnFkBankAccountCashId);
                    
                    accountingAdvance.setBankLayoutTypeId(mnFkBankLayoutTypeId);
                    accountingAdvance.setBizPartnerId(bankPayment.getBizPartnerId());
                    accountingAdvance.setBizPartnerBranchId(bankPayment.getBizPartnerBranchId());
                    accountingAdvance.setBizPartnerBranchAccountCreditId(bankPayment.getBizPartnerBranchAccountId());
                    accountingAdvance.setCompanyBranchId(mnFkBankCompanyBranchId);
                    accountingAdvance.setCompanyBranchAccountDebitId(mnFkBankAccountCashId);
                    accountingAdvance.setAmount(bankPayment.getAmount().getAmountOriginal());
                    accountingAdvance.setCurrencyId(bankPayment.getAmount().getCurrencyOriginalId());
                    accountingAdvance.setExcRate(1);
                    accountingAdvance.setExcRateSystem(1);
                    accountingAdvance.setBookkeepingYearId_n(nBookkeepingYear);
                    accountingAdvance.setBookkeepingNumberId_n(nBookkeepingNum);
                    accountingAdvance.setBookkeepingCenterId(record.getPkBookkeepingCenterId());
                    accountingAdvance.setDate(mtDateLayout);
                    accountingAdvance.setBizPartner(sBizPartner);
                    accountingAdvance.setReferenceRecord(bankPayment.getReferenceRecord());
                    
                    for (SDataRecordEntry entry : accountingAdvance.getDbmsRecordEntries()) {
                        entry.setSortingPosition(++nSortingPosition);
                        recordEntrys.add(entry);
                    }
                    
                    recordEntrys.addAll(recordEntrys);
                }
                mnTransfersPayed++;
            }
        }
        
        for (SDataRecordEntry entry : recordEntrys) {
            entry.setPkYearId(record.getPkYearId());
            entry.setPkPeriodId(record.getPkPeriodId());
            entry.setPkBookkeepingCenterId(record.getPkBookkeepingCenterId());
            entry.setPkRecordTypeId(record.getPkRecordTypeId());
            entry.setPkNumberId(record.getPkNumberId());
            entry.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { entry.getFkAccountId() }));
            entry.setDbmsCurrencyKey(session.getSessionCustom().getCurrencyCode(new int[] { entry.getFkCurrencyId() }));

            if (entry.getFkSystemMoveCategoryIdXXX() == SDataConstantsSys.FINS_CT_SYS_MOV_BPS) {
                entry.setDbmsAccountComplement(sBizPartner);
            }
            
            record.getDbmsRecordEntries().add(entry);
        }
        
        recordEntriesToProcess.addAll(recordEntrys);
        updateLayoutXml(recordEntriesToProcess);
        
        return record;
    }
    
    private void processLayoutBankRecords(final SGuiSession session) throws Exception {
        for (SLayoutBankRecord record : maLayoutBankRecords) {
            SDataRecord dataRecord = createRecord(session, record);
            
            if (dataRecord.save(session.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
            }
            
            if (dataRecord.read(record.getLayoutBankRecordKey().getPrimaryKey(), session.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
        }
    }
    
    private void processLayoutBankPaymentRows(final SGuiSession session) throws Exception {
        // go through all rows of bank payments:
        
        for (SLayoutBankPaymentRow paymentRow : maLayoutBankPaymentRows) {
            boolean found = false;
            
            // payment to remove:
            
            if (paymentRow.getLayoutBankRecordKeyOld() != null) {
                if (paymentRow.getLayoutBankRecordKey() == null ||
                        !(SLibUtils.compareKeys(paymentRow.getLayoutBankRecordKeyOld().getPrimaryKey(), paymentRow.getLayoutBankRecordKey().getPrimaryKey()))) {
                    SLayoutBankPayment lbpClonedToRemove = paymentRow.getLayoutBankPayment().clone();
                    
                    lbpClonedToRemove.setAction(SLayoutBankPayment.ACTION_PAY_REMOVE);
                    
                    for (SLayoutBankRecord record : maLayoutBankRecords) {
                        if (SLibUtils.compareKeys(record.getLayoutBankRecordKey().getPrimaryKey(), paymentRow.getLayoutBankRecordKeyOld().getPrimaryKey())) {
                            record.getLayoutBankPayments().add(lbpClonedToRemove);
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) {
                        SLayoutBankRecord layoutBankRecord = new SLayoutBankRecord(paymentRow.getLayoutBankRecordKeyOld());
                        layoutBankRecord.getLayoutBankPayments().add(lbpClonedToRemove);
                        maLayoutBankRecords.add(layoutBankRecord);
                    }
                }
            }
            
            // payment to apply:
            
            if (paymentRow.getLayoutBankRecordKey() != null) {
                if (paymentRow.getLayoutBankRecordKeyOld() == null ||
                        !(SLibUtils.compareKeys(paymentRow.getLayoutBankRecordKey().getPrimaryKey(), paymentRow.getLayoutBankRecordKeyOld().getPrimaryKey()))) {
                    SLayoutBankPayment lbpClonedToApply = paymentRow.getLayoutBankPayment().clone();
                    
                    lbpClonedToApply.setAction(SLayoutBankPayment.ACTION_PAY_APPLY);
                    
                    for (SLayoutBankRecord record : maLayoutBankRecords) {
                        if (SLibUtils.compareKeys(record.getLayoutBankRecordKey().getPrimaryKey(), paymentRow.getLayoutBankRecordKey().getPrimaryKey())) {
                            record.getLayoutBankPayments().add(lbpClonedToApply);
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) {
                        SLayoutBankRecord layoutBankRecord = new SLayoutBankRecord(paymentRow.getLayoutBankRecordKey());
                        layoutBankRecord.getLayoutBankPayments().add(lbpClonedToApply);
                        maLayoutBankRecords.add(layoutBankRecord);
                    }
                }
            }
        }
        
        if (!maLayoutBankRecords.isEmpty()) {
            processLayoutBankRecords(session);
        }
    }
    
    private void generateXmlBankLayoutText(final SGuiSession session, final ArrayList<SXmlBankLayoutPayment> layoutPayments) throws Exception {
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
        SLayoutBankPaymentTxt layoutBankPaymentTxt = null;
        ArrayList<SLayoutBankPaymentTxt> layoutBankPaymentTxts = null;

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
        
        layoutBankPaymentTxts = new ArrayList<>();
        
        for (SXmlBankLayoutPayment layoutPayment : layoutPayments) {
            layoutBankPaymentTxt = new SLayoutBankPaymentTxt();
            
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
                    + "WHERE id_bpb = " + (Integer) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).getValue() + " "
                    + "AND id_bank_acc = " + (Integer) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).getValue();

            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                currencyId = resultSet.getInt(1);
                accountCredit = resultSet.getString(2);
                accountBranchCredit = resultSet.getString(3);
            }
            
            layoutBankPaymentTxt.setTotalAmount((double) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).getValue());
            layoutBankPaymentTxt.setReference((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REF_ALP).getValue());
            layoutBankPaymentTxt.setConcept((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CPT).getValue());
            layoutBankPaymentTxt.setDescription((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_DCRP).getValue());
            layoutBankPaymentTxt.setBizPartnerId(bizPartnerId);
            layoutBankPaymentTxt.setBizPartner(bizPartner);
            layoutBankPaymentTxt.setAccountDebit(accountDebit);
            layoutBankPaymentTxt.setAccountBranchDebit(accountBranchDebit);
            layoutBankPaymentTxt.setCurrencyId(currencyId);
            layoutBankPaymentTxt.setAgreement((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE).getValue());
            layoutBankPaymentTxt.setAgreementReference((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_REF).getValue());
            layoutBankPaymentTxt.setConceptCie((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_CON).getValue());
            layoutBankPaymentTxt.setAccountCredit(accountCredit);
            layoutBankPaymentTxt.setAccountBranchCredit(accountBranchCredit);
            layoutBankPaymentTxt.setHsbcFiscalVoucher((Integer) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_VOU).getValue());
            layoutBankPaymentTxt.setHsbcBankCode((Integer) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_BANK_CODE).getValue());
            layoutBankPaymentTxt.setHsbcAccountType((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_ACC_TP).getValue());
            layoutBankPaymentTxt.setHsbcFiscalIdDebit((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_DBT).getValue());
            layoutBankPaymentTxt.setHsbcFiscalIdCredit((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_CRD).getValue());
            layoutBankPaymentTxt.setSantanderBankCode((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_SAN_BANK_CODE).getValue());
            layoutBankPaymentTxt.setBajioBankCode((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_BANK_CODE).getValue());
            layoutBankPaymentTxt.setBajioBankNick((String) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_NICK).getValue());
            layoutBankPaymentTxt.setBankKey((int) layoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_KEY).getValue());
            
            layoutBankPaymentTxts.add(layoutBankPaymentTxt);
        }
        
        switch (layout) {
            case SDataConstantsSys.FINS_TP_PAY_BANK_THIRD:
                switch (layoutBank) {
                   case SFinConsts.LAY_BANK_HSBC:
                       msLayoutText = SFinUtilities.createLayoutHsbcThird(layoutBankPaymentTxts, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_SANTANDER:
                       msLayoutText = SFinUtilities.createLayoutSantanderThird(layoutBankPaymentTxts, mtDateLayout, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_BANBAJIO:
                       msLayoutText = SFinUtilities.createLayoutBanBajioThird(layoutBankPaymentTxts, layoutTitle, mtDateLayout, mnConsecutive);
                      break;
                  case SFinConsts.LAY_BANK_BBVA:
                       msLayoutText = SFinUtilities.createLayoutBbvaThird(layoutBankPaymentTxts, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_BANAMEX:
                       msLayoutText = SFinUtilities.createLayoutBanamexThird(layoutBankPaymentTxts, layoutTitle);
                      break;
                   default:
                       break;
                }
                break;
                
           case SDataConstantsSys.FINS_TP_PAY_BANK_TEF:
                switch (layoutBank) {
                   case SFinConsts.LAY_BANK_HSBC:
                       msLayoutText = SFinUtilities.createLayoutHsbcTef(layoutBankPaymentTxts, layoutTitle, mtDateLayout);
                      break;
                  case SFinConsts.LAY_BANK_SANTANDER:
                       msLayoutText = SFinUtilities.createLayoutSantanderTef(layoutBankPaymentTxts, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_BANBAJIO:
                       msLayoutText = SFinUtilities.createLayoutBanBajioTef(layoutBankPaymentTxts, layoutTitle, mtDateLayout, mnConsecutive);
                      break;
                   case SFinConsts.LAY_BANK_BBVA:
                       msLayoutText = SFinUtilities.createLayoutBbvaTef(layoutBankPaymentTxts, layoutTitle);
                      break;
                   case SFinConsts.LAY_BANK_BANAMEX:
                       msLayoutText = SFinUtilities.createLayoutBanamexTef(layoutBankPaymentTxts, layoutTitle, session);
                      break;
                   default:
                      break;
                }
                break;
               
           case SDataConstantsSys.FINS_TP_PAY_BANK_SPEI_FD_N:
                switch (layoutBank) {
                   case SFinConsts.LAY_BANK_HSBC:
                       msLayoutText = SFinUtilities.createLayoutHsbcSpeiFdN(layoutBankPaymentTxts, layoutTitle);
                       break;
                  case SFinConsts.LAY_BANK_SANTANDER:
                       msLayoutText = SFinUtilities.createLayoutSantanderSpeiFdN(layoutBankPaymentTxts, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_BANBAJIO:
                       msLayoutText = SFinUtilities.createLayoutBanBajioSpeiFdN(layoutBankPaymentTxts, layoutTitle, mtDateLayout, mnConsecutive);
                      break;
                   case SFinConsts.LAY_BANK_BBVA:
                       msLayoutText = SFinUtilities.createLayoutBbvaSpei(layoutBankPaymentTxts, layoutTitle);
                      break;
                   case SFinConsts.LAY_BANK_BANAMEX:
                       msLayoutText = SFinUtilities.createLayoutBanamexSpei(layoutBankPaymentTxts, layoutTitle, session);
                      break;
                   default:
                       break;
                }
                break;
               
            case SDataConstantsSys.FINS_TP_PAY_BANK_SPEI_FD_Y:
                switch (layoutBank) {
                   case SFinConsts.LAY_BANK_HSBC:
                       msLayoutText = SFinUtilities.createLayoutHsbcSpeiFdY(layoutBankPaymentTxts, layoutTitle);
                      break;
                  case SFinConsts.LAY_BANK_SANTANDER:
                       msLayoutText = SFinUtilities.createLayoutSantanderSpeiFdY(layoutBankPaymentTxts, layoutTitle);
                      break;
                   case SFinConsts.LAY_BANK_BBVA:
                       msLayoutText = SFinUtilities.createLayoutBbvaSpei(layoutBankPaymentTxts, layoutTitle);
                      break;
                   case SFinConsts.LAY_BANK_BANAMEX:
                       msLayoutText = SFinUtilities.createLayoutBanamexSpei(layoutBankPaymentTxts, layoutTitle, session);
                      break;
                   default:
                       break;
                }
                break;
                
            case SDataConstantsSys.FINS_TP_PAY_BANK_AGREE:
                switch (layoutBank) {
                    case SFinConsts.LAY_BANK_BBVA:
                        msLayoutText = SFinUtilities.createLayoutBbvaCie(layoutBankPaymentTxts);
                        break;
                    default:
                }
                break;
                
            default:
        }
    }
    
    private void generateXmlBankLayout(final SGuiSession session) throws Exception {
        boolean found = false;
        SXmlBankLayout xmlBankLayout = new SXmlBankLayout();
        SXmlBankLayoutPayment xmlBankLayoutPayment = null;
        SXmlBankLayoutPaymentDoc xmlBankLayoutPaymentDoc = null;
        ArrayList<SXmlBankLayoutPayment> xmlBankLayoutPayments = new ArrayList<>();
        
        xmlBankLayout.getAttribute(SXmlBankLayout.ATT_LAY_ID).setValue(mnPkBankLayoutId);
        
        for (SLayoutBankXmlRow xmlRow : maLayoutBankXmlRows) {
            xmlBankLayoutPaymentDoc = new SXmlBankLayoutPaymentDoc();

            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR).setValue(xmlRow.getDpsYear());
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC).setValue(xmlRow.getDpsDoc());
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).setValue(xmlRow.getAmount());
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT_CY).setValue(xmlRow.getAmountCy());
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_CUR).setValue(xmlRow.getCurrencyId());
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EXT_RATE).setValue(xmlRow.getExchangeRate());
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_REF_REC).setValue(xmlRow.getReferenceRecord());
            xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_OBS).setValue(xmlRow.getObservations());
            
            for (SXmlBankLayoutPayment payment : xmlBankLayoutPayments) {
                found = false;
                
                if (mnXtaBankPaymentType == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                    if (xmlRow.getAgreement().trim().equals((String) payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE).getValue()) && xmlRow.getAgreementReference().trim().equals((String) payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_REF).getValue())) {
                        found = true;
                    }
                }
                else {
                    if (SLibUtilities.compareKeys(new int[] { xmlRow.getBizPartnerBranch(), xmlRow.getBizPartnerBranchAccount() }, 
                            new int[] { (int) payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).getValue(), (int) payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).getValue() })) {
                        found = true;
                    }
                }
                
                if (found) {
                    payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).setValue(((double) payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).getValue()) + xmlRow.getAmount());
                    payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT_CY).setValue(((double) payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT_CY).getValue()) + xmlRow.getAmountCy());
                    payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REF_ALP).setValue(((String) payment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REF_ALP).getValue()) + "," + xmlRow.getReference());
                    payment.getXmlElements().add(xmlBankLayoutPaymentDoc);
                    break;
                }
            }
            
            if (!found) {
                xmlBankLayoutPayment = new SXmlBankLayoutPayment();
            
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).setValue(xmlRow.getAmount());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT_CY).setValue(xmlRow.getAmount());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REF_ALP).setValue(xmlRow.getReference());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CUR).setValue(xmlRow.getCurrencyId());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE).setValue(xmlRow.getAgreement());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_REF).setValue(xmlRow.getAgreementReference());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_CON).setValue(xmlRow.getConceptCie());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_EXT_RATE).setValue(xmlRow.getExchangeRate());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CPT).setValue(xmlRow.getConcept());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_VOU).setValue(xmlRow.getHsbcFiscalVoucher());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_ACC_TP).setValue(xmlRow.getHsbcAccountType());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_BANK_CODE).setValue(xmlRow.getHsbcBankCode());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_DBT).setValue(xmlRow.getHsbcFiscalIdDebit());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_CRD).setValue(xmlRow.getHsbcFiscalIdCredit());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_DCRP).setValue(xmlRow.getDescription());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_SAN_BANK_CODE).setValue(xmlRow.getSantanderBankCode());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_BANK_CODE).setValue(xmlRow.getBajioBankCode());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_NICK).setValue(xmlRow.getBajioBankNick());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_KEY).setValue(xmlRow.getBankKey());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).setValue(xmlRow.getIsToPayed());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BP).setValue(xmlRow.getBizPartner());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).setValue(xmlRow.getBizPartnerBranch());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).setValue(xmlRow.getBizPartnerBranchAccount());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR).setValue(xmlRow.getRecYear());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_PER).setValue(xmlRow.getRecPeriod());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_BKC).setValue(xmlRow.getRecBookkeepingCenter());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_REC_TP).setValue(xmlRow.getRecRecordType());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_NUM).setValue(xmlRow.getRecNumber());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_YEAR).setValue(xmlRow.getBookkeepingYear());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_NUM).setValue(xmlRow.getBookkeepingNumber());
                xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_ROW_REF_REC).setValue(xmlRow.getReferenceRecord());
                
                xmlBankLayoutPayment.getXmlElements().add(xmlBankLayoutPaymentDoc);
                
                xmlBankLayoutPayments.add(xmlBankLayoutPayment);
            }
        }
        
        xmlBankLayout.getXmlElements().addAll(xmlBankLayoutPayments);
        msLayoutXml = xmlBankLayout.getXmlString();
        mnTransfers = xmlBankLayoutPayments.size();
        
        generateXmlBankLayoutText(session, xmlBankLayoutPayments);
    }
    
    private boolean validateLayoutBankRecordsPeriods(final SGuiSession session) throws Exception {
        for (SLayoutBankRecord bankRecord : maLayoutBankRecords) {
            SDataRecord record = new SDataRecord();
            
            if (record.read(bankRecord.getLayoutBankRecordKey().getPrimaryKey(), session.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
            
            record.checkIsEditable(session.getStatement().getConnection());
        }
        
        return true;
    }
    
    private boolean validateLayoutBankPaymentDpsStatus(final SGuiSession session) throws Exception {
        for (SLayoutBankPaymentRow paymentRow : maLayoutBankPaymentRows) {
            SLayoutBankPayment bankPayment = paymentRow.getLayoutBankPayment();
            
            if (bankPayment.getLayoutPaymentType() == SModSysConsts.FIN_LAY_BANK_PAY) {
                for (SLayoutBankDps bankDps : bankPayment.getLayoutBankDps()) {
                    SDataDps dps = new SDataDps();
                    
                    if (dps.read(new int[] { bankDps.getPkYearId(), bankDps.getPkDocId() }, session.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    
                    dps.checkIsReferable();
                }
            }
        }
        
        return true;
    }
    
    /*
     * Public methods
     */
    
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
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setAmount(double d) { mdAmount = d; }
    public void setAmountPayed(double d) { mdAmountPayed = d; }
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

    public void setAuxTitle(String s) { msAuxTitle = s; }
    public void setAuxLayoutPath(String s) { msAuxLayoutPath = s; }
    public void setAuxLayoutType(String s) { msAuxLayoutType = s; }
    public void setAuxLocalLayoutAmount(double d) { mdAuxLocalLayoutAmount = d; }
    public void setXtaLayoutBank(int n) { mnXtaLayoutBank = n; }
    public void setXtaBankCurrencyId(int n) { mnXtaBankCurrencyId = n; }
    public void setXtaBankPaymentType(int n) { mnXtaBankPaymentType = n; }

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

    public String getAuxTitle() { return msAuxTitle; }
    public String getAuxLayoutPath() { return msAuxLayoutPath; }
    public String getAuxLayoutType() { return msAuxLayoutType; }
    public double getAuxLocalLayoutAmount() { return mdAuxLocalLayoutAmount; }
    public int getXtaLayoutBank() { return mnXtaLayoutBank; }
    public int getXtaBankCurrencyId() { return mnXtaBankCurrencyId; }
    public int getXtaBankPaymentType() { return mnXtaBankPaymentType; }
    
    public ArrayList<SLayoutBankPaymentRow> getLayoutBankPaymentRows() { return maLayoutBankPaymentRows; }
    public ArrayList<SLayoutBankRecord> getLayoutBankRecords() { return maLayoutBankRecords; }
    public ArrayList<SLayoutBankXmlRow> getLayoutBankXmlRows() { return maLayoutBankXmlRows; }
    
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
        msAgreement = "";
        msAgreementReference = "";
        mnConsecutive = 0;
        mdExchangeRate = 0;
        mdAmount = 0;
        mdAmountPayed = 0;
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

        msAuxTitle = "";
        msAuxLayoutPath = "";
        msAuxLayoutType = "";
        mdAuxLocalLayoutAmount = 0;
        mnXtaLayoutBank = 0;
        mnXtaBankCurrencyId = 0;
        mnXtaBankPaymentType = 0;
        
        maLayoutBankPaymentRows = new ArrayList<>();
        maLayoutBankRecords = new ArrayList<>();
        maLayoutBankXmlRows = new ArrayList<>();
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
            
            msSql = "SELECT lay_bank, fid_tp_pay_bank FROM " + SModConsts.TablesMap.get(SModConsts.FINU_TP_LAY_BANK) + " WHERE id_tp_lay_bank = " + mnFkBankLayoutTypeId + " ";
            resultSet = session.getStatement().executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                mnXtaLayoutBank = resultSet.getInt(1);
                mnXtaBankPaymentType = resultSet.getInt(2);
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
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }
        
        processLayoutBankPaymentRows(session);
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
        }
        
        generateXmlBankLayout(session);

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

        registry.setAuxTitle(this.getAuxTitle());
        registry.setAuxLayoutPath(this.getAuxLayoutPath());
        registry.setAuxLayoutType(this.getAuxLayoutType());
        registry.setAuxLocalLayoutAmount(this.getAuxLocalLayoutAmount());
        registry.setXtaLayoutBank(this.getXtaLayoutBank());
        registry.setXtaBankCurrencyId(this.getXtaBankCurrencyId());

        for (SLayoutBankPaymentRow child : maLayoutBankPaymentRows) {
            registry.getLayoutBankPaymentRows().add(child.clone());
        }
        
        for (SLayoutBankRecord child : maLayoutBankRecords) {
            registry.getLayoutBankRecords().add(child.clone());
        }
        
        for (SLayoutBankXmlRow child : maLayoutBankXmlRows) {
            registry.getLayoutBankXmlRows().add(child.clone());
        }
        
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
        
        if (can) {
            can = validateLayoutBankRecordsPeriods(session);

            if (can) {
                can = validateLayoutBankPaymentDpsStatus(session);
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
}
