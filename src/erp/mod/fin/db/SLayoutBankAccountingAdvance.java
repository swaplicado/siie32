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
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SFinAccountConfig;
import erp.mfin.data.SFinAccountUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SLayoutBankAccountingAdvance {

    protected SGuiSession moSession;
    
    protected int mnBankLayoutTypeId;
    protected int mnBizPartnerId;
    protected int mnBizPartnerBranchId;
    protected int mnBizPartnerBranchAccountCreditId;
    protected int mnCompanyBranchId;
    protected int mnCompanyBranchAccountDebitId;
    protected double mdAmount;
    protected int mnCurrencyId;
    protected double mdExchangeRate;
    protected double mdExchangeRateSystem;
    protected int mnFkBookkeepingYearId_n;
    protected int mnFkBookkeepingNumberId_n;
    protected int mnBookkeepingCenterId;
    protected Date mtDate;
    protected String msBizPartner;
    protected String msReferenceRecord;
    
    protected ArrayList<SDataRecordEntry> maRecordEntries;

    public SLayoutBankAccountingAdvance(final SGuiSession session, final int bizPartnerId, final int bizPartnerBranchId, final int bizPartnerBranchBankAccountId, final int companyBranchId, final int companyBranchAccountDebitId) {
        moSession = session;
        mnBankLayoutTypeId = 0;
        mnBizPartnerId = bizPartnerId;
        mnBizPartnerBranchId = bizPartnerBranchId;
        mnBizPartnerBranchAccountCreditId = bizPartnerBranchBankAccountId;
        mnCompanyBranchId = companyBranchId;
        mnCompanyBranchAccountDebitId = companyBranchAccountDebitId;
        mdAmount = 0;
        mnCurrencyId = 0;
        mdExchangeRate = 0;
        mdExchangeRateSystem = 0;
        mnFkBookkeepingYearId_n = 0;
        mnFkBookkeepingNumberId_n = 0;
        mnBookkeepingCenterId = 0;
        mtDate = null;
        msBizPartner = "";
        msReferenceRecord = "";
        
        maRecordEntries = new ArrayList<>();
    }

    public void setGuiSession(SGuiSession o) { moSession = o; }
    
    public void setBankLayoutTypeId(int n) { mnBankLayoutTypeId = n; }
    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartnerBranchId(int n) { mnBizPartnerBranchId = n; }
    public void setBizPartnerBranchAccountCreditId(int n) { mnBizPartnerBranchAccountCreditId = n; }
    public void setCompanyBranchId(int n) { mnCompanyBranchId = n; }
    public void setCompanyBranchAccountDebitId(int n) { mnCompanyBranchAccountDebitId = n; }
    public void setAmount(double d) { mdAmount = d; }
    public void setCurrencyId(int n) { mnCurrencyId = n; }
    public void setExcRate(double d) { mdExchangeRate = d; }
    public void setExcRateSystem(double d) { mdExchangeRateSystem = d; }
    public void setBookkeepingYearId_n(int n) { mnFkBookkeepingYearId_n = n; }
    public void setBookkeepingNumberId_n(int n) { mnFkBookkeepingNumberId_n = n; }
    public void setBookkeepingCenterId(int n) { mnBookkeepingCenterId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setBizPartner(String s) { msBizPartner = s; }
    public void setReferenceRecord(String s) { msReferenceRecord = s; }
    
    public void setRecordEntries(ArrayList<SDataRecordEntry> o) { maRecordEntries = o; }

    public SGuiSession getGuiSession() { return moSession; }
    
    public int getBankLayoutTypeId() { return mnBankLayoutTypeId; }
    public int getBizPartnerId() { return mnBizPartnerId; }
    public int getBizPartnerBranchId() { return mnBizPartnerBranchId; }
    public int getBizPartnerBranchAccountCreditId() { return mnBizPartnerBranchAccountCreditId; }
    public int getCompanyBranchId() { return mnCompanyBranchId; }
    public int getCompanyBranchAccountDebitId() { return mnCompanyBranchAccountDebitId; }
    public double getAmount() { return mdAmount; }
    public int getCurrencyId() { return mnCurrencyId; }
    public double getExcRate() { return mdExchangeRate; }
    public double getExcRateSystem() { return mdExchangeRateSystem; }
    public int getBookkeepingYearId_n() { return mnFkBookkeepingYearId_n; }
    public int getBookkeepingNumberId_n() { return mnFkBookkeepingNumberId_n; }
    public int getBookkeepingCenterId() { return mnBookkeepingCenterId; }
    public Date getDate() { return mtDate; }
    public String getBizPartner() { return msBizPartner; }
    public String getReferenceRecord() { return msReferenceRecord; }
    
    public ArrayList<SDataRecordEntry> getRecordEntries() { return maRecordEntries; }
    
    private String createConceptRecordEntry() throws Exception {
        String concept = "";
        String layoutTitle = "";
        String bank = "";
        String sql = "";
        ResultSet resultSet = null;

        // layout name:
        
        sql = "SELECT fid_tp_pay_bank, tp_lay_bank, lay_bank "
                + "FROM erp.finu_tp_lay_bank "
                + "WHERE id_tp_lay_bank = " + mnBankLayoutTypeId;
        
        resultSet = moSession.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            layoutTitle = resultSet.getString(2);
        }
   
        // Bank to transfer:
        
        sql = "SELECT ct.bp_key "
                + "FROM erp.bpsu_bank_acc AS bank "
                + "INNER JOIN erp.bpsu_bp AS bp ON bp.id_bp = bank.fid_bank "
                + "INNER JOIN erp.bpsu_bp_ct AS ct ON ct.id_bp = bp.id_bp AND ct.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_SUP + " "
                + "WHERE bank.id_bpb = " + mnBizPartnerBranchId + " "
                + "AND bank.id_bank_acc = " + mnBizPartnerBranchAccountCreditId;

        resultSet = moSession.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            bank = resultSet.getString(1);
        }
        
        concept = layoutTitle + " / " + bank + " / ANTICIPO / " + msBizPartner;
        
        return (concept.length() > 100 ? SLibUtilities.textLeft(concept, 100) : concept).trim();
    }

    private SDataRecordEntry createAccountingRecordEntry(java.lang.String accountId, java.lang.String costCenterId) {
        SDataRecordEntry entry = null;
        
        entry = new SDataRecordEntry();
        /*
        entry.setPkYearId();
        entry.setPkPeriodId();
        entry.setPkBookkeepingCenterId();
        entry.setPkRecordTypeId();
        entry.setPkNumberId();
        */
        entry.setPkEntryId(SLibConsts.UNDEFINED);
        entry.setConcept("");
        entry.setReference(msReferenceRecord);
        entry.setIsReferenceTax(false);
        entry.setDebit(mdAmount);
        entry.setCredit(0);
        entry.setExchangeRate(mdExchangeRate);
        entry.setExchangeRateSystem(mdExchangeRateSystem);
        entry.setDebitCy(mdAmount);
        entry.setCreditCy(0);
        entry.setUnits(0);
        entry.setSortingPosition(SLibConsts.UNDEFINED);
        entry.setIsSystem(true);
        entry.setIsDeleted(false);
        entry.setFkAccountIdXXX(accountId);
        entry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        entry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        entry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
        entry.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_MO_SUP_PAY[0]);
        entry.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_MO_SUP_PAY[1]);
        entry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL[0]);
        entry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL[1]);
        entry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0]);
        entry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1]);
        entry.setFkCurrencyId(mnCurrencyId);
        entry.setFkCostCenterIdXXX_n(costCenterId);
        entry.setFkCheckWalletId_n(SLibConsts.UNDEFINED);
        entry.setFkCheckId_n(SLibConsts.UNDEFINED);
        
        entry.setFkBizPartnerId_nr(mnBizPartnerId);
        entry.setFkBizPartnerBranchId_n(mnBizPartnerBranchId);
        entry.setFkReferenceCategoryId_n(SLibConsts.UNDEFINED);
        
        entry.setFkCompanyBranchId_n(mnCompanyBranchId);
        entry.setFkEntityId_n(mnCompanyBranchAccountDebitId);
        entry.setFkTaxBasicId_n(SLibConsts.UNDEFINED);
        entry.setFkTaxId_n(SLibConsts.UNDEFINED);
        entry.setFkYearId_n(SLibConsts.UNDEFINED);
        entry.setFkBookkeepingYearId_n(mnFkBookkeepingYearId_n);
        entry.setFkBookkeepingNumberId_n(mnFkBookkeepingNumberId_n);

        entry.setFkDpsYearId_n(SLibConsts.UNDEFINED);
        entry.setFkDpsDocId_n(SLibConsts.UNDEFINED);
        entry.setFkDpsAdjustmentYearId_n(SLibConsts.UNDEFINED);
        entry.setFkDpsAdjustmentDocId_n(SLibConsts.UNDEFINED);

        entry.setFkDiogYearId_n(SLibConsts.UNDEFINED);
        entry.setFkDiogDocId_n(SLibConsts.UNDEFINED);
        entry.setFkItemId_n(SLibConsts.UNDEFINED);
        entry.setFkItemAuxId_n(SLibConsts.UNDEFINED);
        entry.setFkUnitId_n(SLibConsts.UNDEFINED);

        entry.setFkUserNewId(moSession.getUser().getPkUserId());
        //entry.setFkUserEditId(...);
        //entry.setFkUserDeleteId(...);
        
        return entry;
    }

    private SDataRecordEntry createRecordEntryAccountCash() throws Exception {
        int[] keySystemMoveType = null;
        int[] keySystemMoveTypeXXX = null;
        SDataAccountCash accountCash = null;
        SDataRecordEntry entry = null;
        
        accountCash = new SDataAccountCash();
        if (accountCash.read(new int[] { mnCompanyBranchId, mnCompanyBranchAccountDebitId }, moSession.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
        }

        if (accountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH) {
            keySystemMoveTypeXXX = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH;
        }
        else {
            keySystemMoveTypeXXX = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK;
        }
        
        entry = new SDataRecordEntry();

        /*
        entry.setPkYearId();
        entry.setPkPeriodId();
        entry.setPkBookkeepingCenterId();
        entry.setPkRecordTypeId();
        entry.setPkNumberId();
        */
        entry.setPkEntryId(SLibConsts.UNDEFINED);
        entry.setConcept("");
        entry.setReference("");
        entry.setIsReferenceTax(false);
        entry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        entry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        entry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
        entry.setFkUserNewId(moSession.getUser().getPkUserId());
        //entry.setFkUserEditId(...);
        //entry.setFkUserDeleteId(...);
        entry.setDbmsAccountingMoveSubclass(moSession.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL, SDbRegistry.FIELD_NAME) + "");

        entry.setDebit(0);
        entry.setCredit(mdAmount);
        entry.setExchangeRate(1);
        entry.setExchangeRateSystem(1);
        entry.setDebitCy(0);
        entry.setCreditCy(mdAmount);
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

        entry.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription((SClientInterface) moSession.getClient(), SDataConstants.FIN_ACC, new Object[] { accountCash.getFkAccountId() }));
        entry.setDbmsAccountComplement(accountCash.getDbmsCompanyBranchEntity().getEntity());
        entry.setDbmsCostCenter_n("");
        entry.setDbmsCurrencyKey(moSession.getSessionCustom().getCurrencyCode(new int[] { accountCash.getFkCurrencyId() }));

        entry.setFkTaxBasicId_n(SLibConsts.UNDEFINED);
        entry.setFkTaxId_n(SLibConsts.UNDEFINED);

        entry.setFkBizPartnerId_nr(mnBizPartnerId);
        entry.setFkBizPartnerBranchId_n(mnBizPartnerBranchId);
        entry.setFkReferenceCategoryId_n(SLibConsts.UNDEFINED);
        
        entry.setFkCompanyBranchId_n(accountCash.getPkCompanyBranchId());
        entry.setFkEntityId_n(accountCash.getPkAccountCashId());
        entry.setUnits(0d);
        entry.setFkItemId_n(SLibConsts.UNDEFINED);
        entry.setFkItemAuxId_n(SLibConsts.UNDEFINED);
        entry.setFkYearId_n(SLibConsts.UNDEFINED);
        entry.setFkDpsYearId_n(SLibConsts.UNDEFINED);
        entry.setFkDpsDocId_n(SLibConsts.UNDEFINED);
        entry.setFkDpsAdjustmentYearId_n(SLibConsts.UNDEFINED);
        entry.setFkDpsAdjustmentDocId_n(SLibConsts.UNDEFINED);
        entry.setFkBookkeepingYearId_n(mnFkBookkeepingYearId_n);
        entry.setFkBookkeepingNumberId_n(mnFkBookkeepingNumberId_n);
        
        return entry;
    }
    
    private void createRecordEntries() {
        String concept = "";
        SFinAccountConfig oConfigBizPartnerOps = null;
        SDataRecordEntry entry = null;
        
        try {
            concept = createConceptRecordEntry();
            oConfigBizPartnerOps = new SFinAccountConfig(SFinAccountUtilities.obtainBizPartnerAccountConfigs(mnBizPartnerId, SModSysConsts.BPSS_CT_BP_SUP, mnBookkeepingCenterId, 
                                                            mtDate, SDataConstantsSys.FINS_TP_ACC_BP_PAY, false, null, moSession.getStatement()));

            entry = createRecordEntryAccountCash();
            entry.setConcept(concept);

            maRecordEntries.add(entry);
            
            for (int j = 0; j < oConfigBizPartnerOps.getAccountConfigEntries().size(); j++) {
                entry = createAccountingRecordEntry(oConfigBizPartnerOps.getAccountConfigEntries().get(j).getAccountId(),
                        oConfigBizPartnerOps.getAccountConfigEntries().get(j).getCostCenterId());
                entry.setConcept(concept);
                maRecordEntries.add(entry);
                
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    public ArrayList<SDataRecordEntry> getDbmsRecordEntries() { 
        createRecordEntries();
        return maRecordEntries; 
    }
}
