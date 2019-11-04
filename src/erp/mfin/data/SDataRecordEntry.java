/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.SErpConsts;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataCfd;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following classes:
 * - erp.mfin.data.SDataFiscalYearClosing
 * - erp.mfin.data.SDataFiscalYearOpening
 * - erp.util.imp.ImportAccountingRecords
 * - erp.util.imp.ImportAccountingRecordsMicroSip
 * - erp.mod.hrs.db.SHrsFinUtils
 * - erp.mfin.data.SDataRecord
 * All of them execute raw SQL queries and insertions.
 */

/**
 *
 * @author Sergio Flores
 */
public class SDataRecordEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable, sa.lib.grid.SGridRow {
    
    public static final int LEN_CONCEPT = 100;
    public static final int LEN_REFERENCE = 15;

    protected int mnPkYearId;
    protected int mnPkPeriodId;
    protected int mnPkBookkeepingCenterId;
    protected java.lang.String msPkRecordTypeId;
    protected int mnPkNumberId;
    protected int mnPkEntryId;
    protected java.lang.String msConcept;
    protected java.lang.String msReference;
    protected boolean mbIsReferenceTax;
    protected double mdDebit;
    protected double mdCredit;
    protected double mdExchangeRate;
    protected double mdExchangeRateSystem;
    protected double mdDebitCy;
    protected double mdCreditCy;
    protected double mdUnits;
    protected int mnUserId;
    protected int mnSortingPosition;
    protected boolean mbIsExchangeDifference;
    protected boolean mbIsSystem;
    protected boolean mbIsDeleted;
    protected java.lang.String msFkAccountIdXXX;
    protected int mnFkAccountId;
    protected int mnFkCostCenterId_n;
    protected int mnFkAccountingMoveTypeId;
    protected int mnFkAccountingMoveClassId;
    protected int mnFkAccountingMoveSubclassId;
    protected int mnFkSystemMoveClassId;
    protected int mnFkSystemMoveTypeId;
    protected int mnFkSystemAccountClassId;
    protected int mnFkSystemAccountTypeId;
    protected int mnFkSystemMoveCategoryIdXXX;
    protected int mnFkSystemMoveTypeIdXXX;
    protected int mnFkCurrencyId;
    protected java.lang.String msFkCostCenterIdXXX_n;
    protected int mnFkCheckWalletId_n;
    protected int mnFkCheckId_n;
    protected int mnFkBizPartnerId_nr;
    protected int mnFkBizPartnerBranchId_n;
    protected int mnFkReferenceCategoryId_n;
    protected int mnFkCompanyBranchId_n;
    protected int mnFkEntityId_n;
    protected int mnFkPlantCompanyBranchId_n;
    protected int mnFkPlantEntityId_n;
    protected int mnFkTaxBasicId_n;
    protected int mnFkTaxId_n;
    protected int mnFkYearId_n;
    protected int mnFkDpsYearId_n;
    protected int mnFkDpsDocId_n;
    protected int mnFkDpsAdjustmentYearId_n;
    protected int mnFkDpsAdjustmentDocId_n;
    protected int mnFkDiogYearId_n;
    protected int mnFkDiogDocId_n;
    protected int mnFkMfgYearId_n;
    protected int mnFkMfgOrdId_n;
    protected int mnFkCfdId_n;
    protected int mnFkCostGicId_n;
    protected int mnFkPayrollFormerId_n;
    protected int mnFkPayrollId_n;
    protected int mnFkItemId_n;
    protected int mnFkItemAuxId_n;
    protected int mnFkUnitId_n;
    protected int mnFkBookkeepingYearId_n;
    protected int mnFkBookkeepingNumberId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsAccount;
    protected java.lang.String msDbmsAccountComplement;
    protected java.lang.String msDbmsCostCenter_n;
    protected java.lang.String msDbmsCurrencyKey;
    protected java.lang.String msDbmsAccountingMoveType;
    protected java.lang.String msDbmsAccountingMoveClass;
    protected java.lang.String msDbmsAccountingMoveSubclass;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;
    protected erp.mfin.data.SDataCheck moDbmsCheck;
    protected ArrayList<erp.mtrn.data.SDataCfd> maDbmsDataCfd;

    protected java.lang.String msXtaEntityCode;
    protected java.lang.String msXtaEntity;
    protected java.lang.String msXtaBizPartnerCode;
    protected java.lang.String msXtaBizPartner;
    protected java.lang.String msXtaItemCode;
    protected java.lang.String msXtaItem;
    protected java.lang.String msXtaSystemMoveClass;
    protected java.lang.String msXtaSystemMoveType;

    protected int mnAuxCheckNumber;
    protected java.util.Date mtAuxDateCfd;

    /**
     * Overrides java.lang.Object.clone() function.
     */
    public SDataRecordEntry() {
        super(SDataConstants.FIN_REC_ETY);
        reset();
    }
    
    private void sanitizeData() {
        if (msConcept.length() > LEN_CONCEPT) {
            msConcept = msConcept.substring(0, LEN_CONCEPT - SErpConsts.ELLIPSIS.length()).trim() + SErpConsts.ELLIPSIS;
        }
        if (msReference.length() > LEN_REFERENCE) {
            msReference = msReference.substring(0, LEN_REFERENCE - SErpConsts.ELLIPSIS.length()).trim() + SErpConsts.ELLIPSIS;
        }
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkPeriodId(int n) { mnPkPeriodId = n; }
    public void setPkBookkeepingCenterId(int n) { mnPkBookkeepingCenterId = n; }
    public void setPkRecordTypeId(java.lang.String s) { msPkRecordTypeId = s; }
    public void setPkNumberId(int n) { mnPkNumberId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setConcept(java.lang.String s) { msConcept = s; }
    public void setReference(java.lang.String s) { msReference = s; }
    public void setIsReferenceTax(boolean b) { mbIsReferenceTax = b; }
    public void setDebit(double d) { mdDebit = d; }
    public void setCredit(double d) { mdCredit = d; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setExchangeRateSystem(double d) { mdExchangeRateSystem = d; }
    public void setDebitCy(double d) { mdDebitCy = d; }
    public void setCreditCy(double d) { mdCreditCy = d; }
    public void setUnits(double d) { mdUnits = d; }
    public void setUserId(int n) { mnUserId = n; }
    public void setSortingPosition(int n) { mnSortingPosition = n; }
    public void setIsExchangeDifference(boolean b) { mbIsExchangeDifference = b; }
    public void setIsSystem(boolean b) { mbIsSystem = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkAccountIdXXX(java.lang.String s) { msFkAccountIdXXX = s; }
    public void setFkAccountId(int n) { mnFkAccountId = n; }
    public void setFkCostCenterId_n(int n) { mnFkCostCenterId_n = n; }
    public void setFkAccountingMoveTypeId(int n) { mnFkAccountingMoveTypeId = n; }
    public void setFkAccountingMoveClassId(int n) { mnFkAccountingMoveClassId = n; }
    public void setFkAccountingMoveSubclassId(int n) { mnFkAccountingMoveSubclassId = n; }
    public void setFkSystemMoveClassId(int n) { mnFkSystemMoveClassId = n; }
    public void setFkSystemMoveTypeId(int n) { mnFkSystemMoveTypeId = n; }
    public void setFkSystemAccountClassId(int n) { mnFkSystemAccountClassId = n; }
    public void setFkSystemAccountTypeId(int n) { mnFkSystemAccountTypeId = n; }
    public void setFkSystemMoveCategoryIdXXX(int n) { mnFkSystemMoveCategoryIdXXX = n; }
    public void setFkSystemMoveTypeIdXXX(int n) { mnFkSystemMoveTypeIdXXX = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkCostCenterIdXXX_n(java.lang.String s) { msFkCostCenterIdXXX_n = s; }
    public void setFkCheckWalletId_n(int n) { mnFkCheckWalletId_n = n; }
    public void setFkCheckId_n(int n) { mnFkCheckId_n = n; }
    public void setFkBizPartnerId_nr(int n) { mnFkBizPartnerId_nr = n; }
    public void setFkBizPartnerBranchId_n(int n) { mnFkBizPartnerBranchId_n = n; }
    public void setFkReferenceCategoryId_n(int n) { mnFkReferenceCategoryId_n = n; }
    public void setFkCompanyBranchId_n(int n) { mnFkCompanyBranchId_n = n; }
    public void setFkEntityId_n(int n) { mnFkEntityId_n = n; }
    public void setFkPlantCompanyBranchId_n(int n) { mnFkPlantCompanyBranchId_n = n; }
    public void setFkPlantEntityId_n(int n) { mnFkPlantEntityId_n = n; }
    public void setFkTaxBasicId_n(int n) { mnFkTaxBasicId_n = n; }
    public void setFkTaxId_n(int n) { mnFkTaxId_n = n; }
    public void setFkYearId_n(int n) { mnFkYearId_n = n; }
    public void setFkDpsYearId_n(int n) { mnFkDpsYearId_n = n; }
    public void setFkDpsDocId_n(int n) { mnFkDpsDocId_n = n; }
    public void setFkDpsAdjustmentYearId_n(int n) { mnFkDpsAdjustmentYearId_n = n; }
    public void setFkDpsAdjustmentDocId_n(int n) { mnFkDpsAdjustmentDocId_n = n; }
    public void setFkDiogYearId_n(int n) { mnFkDiogYearId_n = n; }
    public void setFkDiogDocId_n(int n) { mnFkDiogDocId_n = n; }
    public void setFkMfgYearId_n(int n) { mnFkMfgYearId_n = n; }
    public void setFkMfgOrdId_n(int n) { mnFkMfgOrdId_n = n; }
    public void setFkCfdId_n(int n) { mnFkCfdId_n = n; }
    public void setFkCostGicId_n(int n) { mnFkCostGicId_n = n; }
    public void setFkPayrollFormerId_n(int n) { mnFkPayrollFormerId_n = n; }
    public void setFkPayrollId_n(int n) { mnFkPayrollId_n = n; }
    public void setFkItemId_n(int n) { mnFkItemId_n = n; }
    public void setFkItemAuxId_n(int n) { mnFkItemAuxId_n = n; }
    public void setFkUnitId_n(int n) { mnFkUnitId_n = n; }
    public void setFkBookkeepingYearId_n(int n) { mnFkBookkeepingYearId_n = n; }
    public void setFkBookkeepingNumberId_n(int n) { mnFkBookkeepingNumberId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkPeriodId() { return mnPkPeriodId; }
    public int getPkBookkeepingCenterId() { return mnPkBookkeepingCenterId; }
    public java.lang.String getPkRecordTypeId() { return msPkRecordTypeId; }
    public int getPkNumberId() { return mnPkNumberId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public java.lang.String getConcept() { return msConcept; }
    public java.lang.String getReference() { return msReference; }
    public boolean getIsReferenceTax() { return mbIsReferenceTax; }
    public double getDebit() { return mdDebit; }
    public double getCredit() { return mdCredit; }
    public double getExchangeRate() { return mdExchangeRate; }
    public double getExchangeRateSystem() { return mdExchangeRateSystem; }
    public double getDebitCy() { return mdDebitCy; }
    public double getCreditCy() { return mdCreditCy; }
    public double getUnits() { return mdUnits; }
    public int getUserId() { return mnUserId; }
    public int getSortingPosition() { return mnSortingPosition; }
    public boolean getIsExchangeDifference() { return mbIsExchangeDifference; }
    public boolean getIsSystem() { return mbIsSystem; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public java.lang.String getFkAccountIdXXX() { return msFkAccountIdXXX; }
    public int getFkAccountId() { return mnFkAccountId; }
    public int getFkCostCenterId_n() { return mnFkCostCenterId_n; }
    public int getFkAccountingMoveTypeId() { return mnFkAccountingMoveTypeId; }
    public int getFkAccountingMoveClassId() { return mnFkAccountingMoveClassId; }
    public int getFkAccountingMoveSubclassId() { return mnFkAccountingMoveSubclassId; }
    public int getFkSystemMoveClassId() { return mnFkSystemMoveClassId; }
    public int getFkSystemMoveTypeId() { return mnFkSystemMoveTypeId; }
    public int getFkSystemAccountClassId() { return mnFkSystemAccountClassId; }
    public int getFkSystemAccountTypeId() { return mnFkSystemAccountTypeId; }
    public int getFkSystemMoveCategoryIdXXX() { return mnFkSystemMoveCategoryIdXXX; }
    public int getFkSystemMoveTypeIdXXX() { return mnFkSystemMoveTypeIdXXX; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public java.lang.String getFkCostCenterIdXXX_n() { return msFkCostCenterIdXXX_n; }
    public int getFkCheckWalletId_n() { return mnFkCheckWalletId_n; }
    public int getFkCheckId_n() { return mnFkCheckId_n; }
    public int getFkBizPartnerId_nr() { return mnFkBizPartnerId_nr; }
    public int getFkBizPartnerBranchId_n() { return mnFkBizPartnerBranchId_n; }
    public int getFkReferenceCategoryId_n() { return mnFkReferenceCategoryId_n; }
    public int getFkCompanyBranchId_n() { return mnFkCompanyBranchId_n; }
    public int getFkEntityId_n() { return mnFkEntityId_n; }
    public int getFkPlantCompanyBranchId_n() { return mnFkPlantCompanyBranchId_n; }
    public int getFkPlantEntityId_n() { return mnFkPlantEntityId_n; }
    public int getFkTaxBasicId_n() { return mnFkTaxBasicId_n; }
    public int getFkTaxId_n() { return mnFkTaxId_n; }
    public int getFkYearId_n() { return mnFkYearId_n; }
    public int getFkDpsYearId_n() { return mnFkDpsYearId_n; }
    public int getFkDpsDocId_n() { return mnFkDpsDocId_n; }
    public int getFkDpsAdjustmentYearId_n() { return mnFkDpsAdjustmentYearId_n; }
    public int getFkDpsAdjustmentDocId_n() { return mnFkDpsAdjustmentDocId_n; }
    public int getFkDiogYearId_n() { return mnFkDiogYearId_n; }
    public int getFkDiogDocId_n() { return mnFkDiogDocId_n; }
    public int getFkMfgYearId_n() { return mnFkMfgYearId_n; }
    public int getFkMfgOrdId_n() { return mnFkMfgOrdId_n; }
    public int getFkCfdId_n() { return mnFkCfdId_n; }
    public int getFkCostGicId_n() { return mnFkCostGicId_n; }
    public int getFkPayrollFormerId_n() { return mnFkPayrollFormerId_n; }
    public int getFkPayrollId_n() { return mnFkPayrollId_n; }
    public int getFkItemId_n() { return mnFkItemId_n; }
    public int getFkItemAuxId_n() { return mnFkItemAuxId_n; }
    public int getFkUnitId_n() { return mnFkUnitId_n; }
    public int getFkBookkeepingYearId_n() { return mnFkBookkeepingYearId_n; }
    public int getFkBookkeepingNumberId_n() { return mnFkBookkeepingNumberId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsAccount(java.lang.String s) { msDbmsAccount = s; }
    public void setDbmsAccountComplement(java.lang.String s) { msDbmsAccountComplement = s; }
    public void setDbmsCostCenter_n(java.lang.String s) { msDbmsCostCenter_n = s; }
    public void setDbmsCurrencyKey(java.lang.String s) { msDbmsCurrencyKey = s; }
    public void setDbmsAccountingMoveType(java.lang.String s) { msDbmsAccountingMoveType = s; }
    public void setDbmsAccountingMoveClass(java.lang.String s) { msDbmsAccountingMoveClass = s; }
    public void setDbmsAccountingMoveSubclass(java.lang.String s) { msDbmsAccountingMoveSubclass = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }
    public void setDbmsCheck(erp.mfin.data.SDataCheck o) { moDbmsCheck = o; }
    public void setDbmsDataCfd(ArrayList<erp.mtrn.data.SDataCfd> a) { maDbmsDataCfd = a; }

    public java.lang.String getDbmsAccount() { return msDbmsAccount; }
    public java.lang.String getDbmsAccountComplement() { return msDbmsAccountComplement; }
    public java.lang.String getDbmsCostCenter_n() { return msDbmsCostCenter_n; }
    public java.lang.String getDbmsCurrencyKey() { return msDbmsCurrencyKey; }
    public java.lang.String getDbmsAccountingMoveType() { return msDbmsAccountingMoveType; }
    public java.lang.String getDbmsAccountingMoveClass() { return msDbmsAccountingMoveClass; }
    public java.lang.String getDbmsAccountingMoveSubclass() { return msDbmsAccountingMoveSubclass; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }
    public erp.mfin.data.SDataCheck getDbmsCheck() { return moDbmsCheck; }
    public ArrayList<erp.mtrn.data.SDataCfd> getDbmsDataCfds() { return maDbmsDataCfd; }

    public void setAuxCheckNumber(int n) { mnAuxCheckNumber = n; }
    public void setAuxDateCfd(java.util.Date t) { mtAuxDateCfd = t; }

    public int getAuxCheckNumber() { return mnAuxCheckNumber; }
    public java.util.Date getAuxDateCfd() { return mtAuxDateCfd; }

    public void setXtaEntityCode(java.lang.String s) { msXtaEntityCode = s; }
    public void setXtaEntity(java.lang.String s) { msXtaEntity = s; }
    public void setXtaBizPartnerCode(java.lang.String s) { msXtaBizPartnerCode = s; }
    public void setXtaBizPartner(java.lang.String s) { msXtaBizPartner = s; }
    public void setXtaItemCode(java.lang.String s) { msXtaItemCode = s; }
    public void setXtaItem(java.lang.String s) { msXtaItem = s; }
    public void setXtaSystemMoveClass(java.lang.String s) { msXtaSystemMoveClass = s; }
    public void setXtaSystemMoveType(java.lang.String s) { msXtaSystemMoveType = s; }

    public java.lang.String getXtaEntityCode() { return msXtaEntityCode; }
    public java.lang.String getXtaEntity() { return msXtaEntity; }
    public java.lang.String getXtaBizPartnerCode() { return msXtaBizPartnerCode; }
    public java.lang.String getXtaBizPartner() { return msXtaBizPartner; }
    public java.lang.String getXtaItemCode() { return msXtaItemCode; }
    public java.lang.String getXtaItem() { return msXtaItem; }
    public java.lang.String getXtaSystemMoveClass() { return msXtaSystemMoveClass; }
    public java.lang.String getXtaSystemMoveType() { return msXtaSystemMoveType; }

    public int[] getKeyCompanyBranch() { return new int[] { mnFkCompanyBranchId_n }; }
    public int[] getKeyCompanyBranchEntity() { return new int[] { mnFkCompanyBranchId_n, mnFkEntityId_n }; }
    public int[] getKeyBookkeepingNumber() { return new int[] { mnFkBookkeepingYearId_n, mnFkBookkeepingNumberId_n }; }
    public int[] getKeySystemMoveClass() { return new int[] { mnFkSystemMoveClassId }; }
    public int[] getKeySystemMoveType() { return new int[] { mnFkSystemMoveClassId, mnFkSystemMoveTypeId }; }
    public int[] getKeySystemMoveTypeXXX() { return new int[] { mnFkSystemMoveCategoryIdXXX, mnFkSystemMoveTypeIdXXX }; }
    
    /**
     * Composes record period in format yyyy-mm (i.e., year-month).
     * @return 
     */
    public java.lang.String getRecordPeriod() {
        return SLibUtils.DecimalFormatCalendarYear.format(mnPkYearId) + "-" + SLibUtils.DecimalFormatCalendarMonth.format(mnPkPeriodId);
    }

    /**
     * Composes record number in format tp-000000 (i.e., type-number).
     * @return 
     */
    public java.lang.String getRecordNumber() {
        return msPkRecordTypeId + "-" + SLibUtils.DecimalNumberFormat.format(mnPkNumberId);
    }

    /**
     * Composes record primary key in format yyyy-mm-0-tp-000000 (i.e., year-month-BKC-type-number).
     * @return 
     */
    public String getRecordPrimaryKey() {
        return getRecordPeriod() + "-" + mnPkBookkeepingCenterId + "-" + getRecordNumber();
    }
    
    /**
     * Composes record entry primary key in format yyyy-mm-0-tp-000000-000000 (i.e., year-month-BKC-type-number-entry).
     * @return 
     */
    public String getRecordEntryPrimaryKey() {
        return getRecordPrimaryKey() + "-" + SLibUtils.DecimalNumberFormat.format(mnPkEntryId);
    }
    
    public int[] getBookkeepingNumberKey_n() { return mnFkBookkeepingYearId_n == 0 && mnFkBookkeepingNumberId_n == 0 ? null : new int[] { mnFkBookkeepingYearId_n, mnFkBookkeepingNumberId_n }; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = (Integer) ((Object[]) pk)[0];
        mnPkPeriodId = (Integer) ((Object[]) pk)[1];
        mnPkBookkeepingCenterId = (Integer) ((Object[]) pk)[2];
        msPkRecordTypeId = (String) ((Object[]) pk)[3];
        mnPkNumberId = (Integer) ((Object[]) pk)[4];
        mnPkEntryId = (Integer) ((Object[]) pk)[5];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new Object[] { mnPkYearId, mnPkPeriodId, mnPkBookkeepingCenterId, msPkRecordTypeId, mnPkNumberId, mnPkEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkPeriodId = 0;
        mnPkBookkeepingCenterId = 0;
        msPkRecordTypeId = "";
        mnPkNumberId = 0;
        mnPkEntryId = 0;
        msConcept = "";
        msReference = "";
        mbIsReferenceTax = false;
        mdDebit = 0;
        mdCredit = 0;
        mdExchangeRate = 0;
        mdExchangeRateSystem = 0;
        mdDebitCy = 0;
        mdCreditCy = 0;
        mdUnits = 0;
        mnUserId = 0;
        mnSortingPosition = 0;
        mbIsExchangeDifference = false;
        mbIsSystem = false;
        mbIsDeleted = false;
        msFkAccountIdXXX = "";
        mnFkAccountId = 0;
        mnFkCostCenterId_n = 0;
        mnFkAccountingMoveTypeId = 0;
        mnFkAccountingMoveClassId = 0;
        mnFkAccountingMoveSubclassId = 0;
        mnFkSystemMoveClassId = 0;
        mnFkSystemMoveTypeId = 0;
        mnFkSystemAccountClassId = 0;
        mnFkSystemAccountTypeId = 0;
        mnFkSystemMoveCategoryIdXXX = 0;
        mnFkSystemMoveTypeIdXXX = 0;
        mnFkCurrencyId = 0;
        msFkCostCenterIdXXX_n = "";
        mnFkCheckWalletId_n = 0;
        mnFkCheckId_n = 0;
        mnFkBizPartnerId_nr = 0;
        mnFkBizPartnerBranchId_n = 0;
        mnFkReferenceCategoryId_n = 0;
        mnFkCompanyBranchId_n = 0;
        mnFkEntityId_n = 0;
        mnFkPlantCompanyBranchId_n = 0;
        mnFkPlantEntityId_n = 0;
        mnFkTaxBasicId_n = 0;
        mnFkTaxId_n = 0;
        mnFkYearId_n = 0;
        mnFkDpsYearId_n = 0;
        mnFkDpsDocId_n = 0;
        mnFkDpsAdjustmentYearId_n = 0;
        mnFkDpsAdjustmentDocId_n = 0;
        mnFkDiogYearId_n = 0;
        mnFkDiogDocId_n = 0;
        mnFkMfgYearId_n = 0;
        mnFkMfgOrdId_n = 0;
        mnFkCfdId_n = 0;
        mnFkCostGicId_n = 0;
        mnFkPayrollFormerId_n = 0;
        mnFkPayrollId_n = 0;
        mnFkItemId_n = 0;
        mnFkItemAuxId_n = 0;
        mnFkUnitId_n = 0;
        mnFkBookkeepingYearId_n = 0;
        mnFkBookkeepingNumberId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsAccount = "";
        msDbmsAccountComplement = "";
        msDbmsCostCenter_n = "";
        msDbmsCurrencyKey = "";
        msDbmsAccountingMoveType = "";
        msDbmsAccountingMoveClass = "";
        msDbmsAccountingMoveSubclass = "";
        moDbmsCheck = null;
        maDbmsDataCfd = new ArrayList<SDataCfd>();

        mnAuxCheckNumber = 0;
        mtAuxDateCfd = null;

        msXtaBizPartnerCode = "";
        msXtaBizPartner = "";
        msXtaItemCode = "";
        msXtaItem = "";
        msXtaSystemMoveClass = "";
        msXtaSystemMoveType = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        Object[] key = (Object[]) pk;
        String sql = "";
        String compEntity = "";
        String compBizPartner = "";
        String compItem = "";
        String compTax = "";
        ResultSet resultSet = null;
        Statement statementAux = null;
        SDataCheck check = null;
        SDataCfd cfd = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT re.*, a.acc, a.fid_tp_acc_sys, cu.cur_key, mtp.tp_acc_mov, mcl.cl_acc_mov, mcls.cls_acc_mov, " +
                    "smcl.name, smtp.name, un.usr, ue.usr, ud.usr, c.cc, e.code, e.ent, b.bp, bcls.bp_key, bclc.bp_key, i.item_key, i.item, t.tax " +
                    "FROM fin_rec_ety AS re " +
                    "INNER JOIN fin_acc AS a ON re.fid_acc = a.id_acc " +
                    "INNER JOIN erp.cfgu_cur AS cu ON re.fid_cur = cu.id_cur " +
                    "INNER JOIN erp.fins_tp_acc_mov AS mtp ON re.fid_tp_acc_mov = mtp.id_tp_acc_mov " +
                    "INNER JOIN erp.fins_cl_acc_mov AS mcl ON re.fid_tp_acc_mov = mcl.id_tp_acc_mov AND re.fid_cl_acc_mov = mcl.id_cl_acc_mov " +
                    "INNER JOIN erp.fins_cls_acc_mov AS mcls ON re.fid_tp_acc_mov = mcls.id_tp_acc_mov AND re.fid_cl_acc_mov = mcls.id_cl_acc_mov AND re.fid_cls_acc_mov = mcls.id_cls_acc_mov " +
                    "INNER JOIN erp.fins_cl_sys_mov AS smcl ON re.fid_cl_sys_mov = smcl.id_cl_sys_mov " +
                    "INNER JOIN erp.fins_tp_sys_mov AS smtp ON re.fid_cl_sys_mov = smtp.id_cl_sys_mov AND re.fid_tp_sys_mov = smtp.id_tp_sys_mov " +
                    "INNER JOIN erp.usru_usr AS un ON re.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON re.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON re.fid_usr_del = ud.id_usr " +
                    "LEFT OUTER JOIN fin_cc AS c ON re.fid_cc_n = c.id_cc " +
                    "LEFT OUTER JOIN erp.cfgu_cob_ent AS e ON re.fid_cob_n = e.id_cob AND re.fid_ent_n = e.id_ent " +
                    "LEFT OUTER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                    "LEFT OUTER JOIN erp.bpsu_bp_ct AS bcls ON re.fid_bp_nr = bcls.id_bp AND bcls.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_SUP + " " +
                    "LEFT OUTER JOIN erp.bpsu_bp_ct AS bclc ON re.fid_bp_nr = bclc.id_bp AND bclc.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_CUS + " " +
                    "LEFT OUTER JOIN erp.itmu_item AS i ON re.fid_item_n = i.id_item " +
                    "LEFT OUTER JOIN erp.finu_tax AS t ON re.fid_tax_bas_n = t.id_tax_bas AND re.fid_tax_n = t.id_tax " +
                    "WHERE re.id_year = " + key[0] + " AND re.id_per = " + key[1] + " AND " +
                    "re.id_bkc = " + key[2] + " AND re.id_tp_rec = '" + key[3] + "' AND " +
                    "re.id_num = " + key[4] + " AND re.id_ety = " + key[5] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("re.id_year");
                mnPkPeriodId = resultSet.getInt("re.id_per");
                mnPkBookkeepingCenterId = resultSet.getInt("re.id_bkc");
                msPkRecordTypeId = resultSet.getString("re.id_tp_rec");
                mnPkNumberId = resultSet.getInt("re.id_num");
                mnPkEntryId = resultSet.getInt("re.id_ety");
                msConcept = resultSet.getString("re.concept");
                msReference = resultSet.getString("re.ref");
                mbIsReferenceTax = resultSet.getBoolean("re.b_ref_tax");
                mdDebit = resultSet.getDouble("re.debit");
                mdCredit = resultSet.getDouble("re.credit");
                mdExchangeRate = resultSet.getDouble("re.exc_rate");
                mdExchangeRateSystem = resultSet.getDouble("re.exc_rate_sys");
                mdDebitCy = resultSet.getDouble("re.debit_cur");
                mdCreditCy = resultSet.getDouble("re.credit_cur");
                mdUnits = resultSet.getDouble("re.units");
                mnUserId = resultSet.getInt("re.usr_id");
                mnSortingPosition = resultSet.getInt("re.sort_pos");
                mbIsExchangeDifference = resultSet.getBoolean("re.b_exc_diff");
                mbIsSystem = resultSet.getBoolean("re.b_sys");
                mbIsDeleted = resultSet.getBoolean("re.b_del");
                msFkAccountIdXXX = resultSet.getString("re.fid_acc");
                mnFkAccountId = resultSet.getInt("re.fk_acc");
                mnFkCostCenterId_n = resultSet.getInt("re.fk_cc_n");
                mnFkAccountingMoveTypeId = resultSet.getInt("re.fid_tp_acc_mov");
                mnFkAccountingMoveClassId = resultSet.getInt("re.fid_cl_acc_mov");
                mnFkAccountingMoveSubclassId = resultSet.getInt("re.fid_cls_acc_mov");
                mnFkSystemMoveClassId = resultSet.getInt("re.fid_cl_sys_mov");
                mnFkSystemMoveTypeId = resultSet.getInt("re.fid_tp_sys_mov");
                mnFkSystemAccountClassId = resultSet.getInt("re.fid_cl_sys_acc");
                mnFkSystemAccountTypeId = resultSet.getInt("re.fid_tp_sys_acc");
                mnFkSystemMoveCategoryIdXXX = resultSet.getInt("re.fid_ct_sys_mov_xxx");
                mnFkSystemMoveTypeIdXXX = resultSet.getInt("re.fid_tp_sys_mov_xxx");
                mnFkCurrencyId = resultSet.getInt("re.fid_cur");
                msFkCostCenterIdXXX_n = resultSet.getString("re.fid_cc_n");
                if (resultSet.wasNull()) msFkCostCenterIdXXX_n = "";
                mnFkCheckWalletId_n = resultSet.getInt("re.fid_check_wal_n");
                mnFkCheckId_n = resultSet.getInt("re.fid_check_n");
                mnFkBizPartnerId_nr = resultSet.getInt("re.fid_bp_nr");
                mnFkBizPartnerBranchId_n = resultSet.getInt("re.fid_bpb_n");
                mnFkReferenceCategoryId_n = resultSet.getInt("re.fid_ct_ref_n");
                mnFkCompanyBranchId_n = resultSet.getInt("re.fid_cob_n");
                mnFkEntityId_n = resultSet.getInt("re.fid_ent_n");
                mnFkPlantCompanyBranchId_n = resultSet.getInt("re.fid_plt_cob_n");
                mnFkPlantEntityId_n = resultSet.getInt("re.fid_plt_ent_n");
                mnFkTaxBasicId_n = resultSet.getInt("re.fid_tax_bas_n");
                mnFkTaxId_n = resultSet.getInt("re.fid_tax_n");
                mnFkYearId_n = resultSet.getInt("re.fid_year_n");
                mnFkDpsYearId_n = resultSet.getInt("re.fid_dps_year_n");
                mnFkDpsDocId_n = resultSet.getInt("re.fid_dps_doc_n");
                mnFkDpsAdjustmentYearId_n = resultSet.getInt("re.fid_dps_adj_year_n");
                mnFkDpsAdjustmentDocId_n = resultSet.getInt("re.fid_dps_adj_doc_n");
                mnFkDiogYearId_n = resultSet.getInt("re.fid_diog_year_n");
                mnFkDiogDocId_n = resultSet.getInt("re.fid_diog_doc_n");
                mnFkMfgYearId_n = resultSet.getInt("re.fid_mfg_year_n");
                mnFkMfgOrdId_n = resultSet.getInt("re.fid_mfg_ord_n");
                mnFkCfdId_n = resultSet.getInt("re.fid_cfd_n");
                mnFkCostGicId_n = resultSet.getInt("re.fid_cost_gic_n");
                mnFkPayrollFormerId_n = resultSet.getInt("re.fid_payroll_n");
                mnFkPayrollId_n = resultSet.getInt("re.fid_pay_n");
                mnFkItemId_n = resultSet.getInt("re.fid_item_n");
                mnFkItemAuxId_n = resultSet.getInt("re.fid_item_aux_n");
                mnFkUnitId_n = resultSet.getInt("re.fid_unit_n");
                mnFkBookkeepingYearId_n = resultSet.getInt("fid_bkk_year_n");
                mnFkBookkeepingNumberId_n = resultSet.getInt("fid_bkk_num_n");
                mnFkUserNewId = resultSet.getInt("re.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("re.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("re.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("re.ts_new");
                mtUserEditTs = resultSet.getTimestamp("re.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("re.ts_del");

                msDbmsAccount = resultSet.getString("a.acc");
                msDbmsCostCenter_n = resultSet.getString("c.cc");
                if (resultSet.wasNull()) {
                    msDbmsCostCenter_n = "";
                }

                msDbmsCurrencyKey = resultSet.getString("cu.cur_key");
                msDbmsAccountingMoveType = resultSet.getString("mtp.tp_acc_mov");
                msDbmsAccountingMoveClass = resultSet.getString("mcl.cl_acc_mov");
                msDbmsAccountingMoveSubclass = resultSet.getString("mcls.cls_acc_mov");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

                msXtaEntityCode = resultSet.getString("e.code");
                if (resultSet.wasNull()) {
                    msXtaEntityCode = "";
                }

                msXtaEntity = resultSet.getString("e.ent");
                if (resultSet.wasNull()) {
                    msXtaEntity = "";
                }

                switch (mnFkSystemAccountClassId) {
                    case SModSysConsts.FINS_CL_SYS_ACC_BPR_SUP:
                        msXtaBizPartnerCode = resultSet.getString("bcls.bp_key");
                        msXtaBizPartner = resultSet.getString("b.bp");
                        break;
                    case SModSysConsts.FINS_CL_SYS_ACC_BPR_CUS:
                        msXtaBizPartnerCode = resultSet.getString("bclc.bp_key");
                        msXtaBizPartner = resultSet.getString("b.bp");
                        break;
                    default:
                }

                msXtaItemCode = resultSet.getString("i.item_key");
                if (resultSet.wasNull()) {
                    msXtaItemCode = "";
                }

                msXtaItem = resultSet.getString("i.item");
                if (resultSet.wasNull()) {
                    msXtaItem = "";
                }

                msXtaSystemMoveClass = resultSet.getString("smcl.name");
                msXtaSystemMoveType = resultSet.getString("smtp.name");

                compBizPartner = resultSet.getString("b.bp");
                if (resultSet.wasNull()) {
                    compBizPartner = "";
                }

                compEntity = resultSet.getString("e.ent");
                if (resultSet.wasNull()) {
                    compEntity = "";
                }

                compItem = resultSet.getString("i.item");
                if (resultSet.wasNull()) {
                    compItem = "";
                }

                compTax = resultSet.getString("t.tax");
                if (resultSet.wasNull()) {
                    compTax = "";
                }

                switch (resultSet.getInt("a.fid_tp_acc_sys")) {
                    case SDataConstantsSys.FINS_TP_ACC_SYS_SUP:
                    case SDataConstantsSys.FINS_TP_ACC_SYS_CUS:
                    case SDataConstantsSys.FINS_TP_ACC_SYS_DBR:
                    case SDataConstantsSys.FINS_TP_ACC_SYS_CDR:
                        msDbmsAccountComplement = compBizPartner;
                        break;

                    case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH:
                    case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK:
                        msDbmsAccountComplement = compEntity;
                        break;

                    case SDataConstantsSys.FINS_TP_ACC_SYS_INV:
                        msDbmsAccountComplement = compEntity + (compItem.isEmpty() ? "" : ", " + compItem);
                        break;

                    case SDataConstantsSys.FINS_TP_ACC_SYS_PUR:
                    case SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ:
                    case SDataConstantsSys.FINS_TP_ACC_SYS_SAL:
                    case SDataConstantsSys.FINS_TP_ACC_SYS_SAL_ADJ:
                        msDbmsAccountComplement = compItem;
                        break;

                    case SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT:
                    case SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT:
                        msDbmsAccountComplement = compTax;
                        break;

                    default:
                }

                // Read entry's bank check, if any:

                if (mnFkCheckWalletId_n != 0 && mnFkCheckId_n != 0) {
                    check = new SDataCheck();
                    if (check.read(new int[] { mnFkCheckWalletId_n, mnFkCheckId_n }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mnAuxCheckNumber = check.getNumber();
                        if (mnFkSystemMoveCategoryIdXXX == SDataConstantsSys.FINS_CT_SYS_MOV_CASH) {
                            moDbmsCheck = check;
                        }
                    }
                }

                // Read XML:
                statementAux = statement.getConnection().createStatement();
                sql = "SELECT id_cfd FROM trn_cfd WHERE fid_rec_year_n = " + key[0] + " AND fid_rec_per_n = " + key[1] + " AND " +
                    "fid_rec_bkc_n = " + key[2] + " AND fid_rec_tp_rec_n = '" + key[3] + "' AND " +
                    "fid_rec_num_n = " + key[4] + " AND fid_rec_ety_n = " + key[5] + " ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    cfd = new SDataCfd();
                    if (cfd.read(new int[] { resultSet.getInt("id_cfd") }, statementAux)!= SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    
                    if (!cfd.getDocXmlName().isEmpty()) {
                        maDbmsDataCfd.add(cfd);
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            sanitizeData();

            if (moDbmsCheck != null) {
                moDbmsCheck.setIsDeleted(mbIsDeleted);
                if (mbIsDeleted) {
                    moDbmsCheck.setFkUserDeleteId(mnFkUserDeleteId);
                }

                if (moDbmsCheck.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
                else {
                    mnFkCheckWalletId_n = moDbmsCheck.getPkCheckWalletId();
                    mnFkCheckId_n = moDbmsCheck.getPkCheckId();
                }
            }
            
            callableStatement = connection.prepareCall(
                    "{ CALL fin_rec_ety_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkPeriodId);
            callableStatement.setInt(nParam++, mnPkBookkeepingCenterId);
            callableStatement.setString(nParam++, msPkRecordTypeId);
            callableStatement.setInt(nParam++, mnPkNumberId);
            callableStatement.setInt(nParam++, mnPkEntryId);
            callableStatement.setString(nParam++, msConcept);
            callableStatement.setString(nParam++, msReference);
            callableStatement.setBoolean(nParam++, mbIsReferenceTax);
            callableStatement.setDouble(nParam++, mdDebit);
            callableStatement.setDouble(nParam++, mdCredit);
            callableStatement.setDouble(nParam++, mdExchangeRate);
            callableStatement.setDouble(nParam++, mdExchangeRateSystem);
            callableStatement.setDouble(nParam++, mdDebitCy);
            callableStatement.setDouble(nParam++, mdCreditCy);
            callableStatement.setDouble(nParam++, mdUnits);
            callableStatement.setInt(nParam++, mnUserId);
            callableStatement.setInt(nParam++, mnSortingPosition);
            callableStatement.setBoolean(nParam++, mbIsExchangeDifference);
            callableStatement.setBoolean(nParam++, mbIsSystem);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setString(nParam++, msFkAccountIdXXX);
            callableStatement.setInt(nParam++, mnFkAccountId);
            if (mnFkCostCenterId_n != SLibConsts.UNDEFINED) callableStatement.setInt(nParam++, mnFkCostCenterId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            callableStatement.setInt(nParam++, mnFkAccountingMoveTypeId);
            callableStatement.setInt(nParam++, mnFkAccountingMoveClassId);
            callableStatement.setInt(nParam++, mnFkAccountingMoveSubclassId);
            callableStatement.setInt(nParam++, mnFkSystemMoveClassId);
            callableStatement.setInt(nParam++, mnFkSystemMoveTypeId);
            callableStatement.setInt(nParam++, mnFkSystemAccountClassId);
            callableStatement.setInt(nParam++, mnFkSystemAccountTypeId);
            callableStatement.setInt(nParam++, mnFkSystemMoveCategoryIdXXX);
            callableStatement.setInt(nParam++, mnFkSystemMoveTypeIdXXX);
            callableStatement.setInt(nParam++, mnFkCurrencyId);
            if (msFkCostCenterIdXXX_n.length() > 0) callableStatement.setString(nParam++, msFkCostCenterIdXXX_n); else callableStatement.setNull(nParam++, java.sql.Types.CHAR);
            if (mnFkCheckWalletId_n > 0) callableStatement.setInt(nParam++, mnFkCheckWalletId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkCheckId_n > 0) callableStatement.setInt(nParam++, mnFkCheckId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkBizPartnerId_nr > 0) callableStatement.setInt(nParam++, mnFkBizPartnerId_nr); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkBizPartnerBranchId_n > 0) callableStatement.setInt(nParam++, mnFkBizPartnerBranchId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkReferenceCategoryId_n > 0) callableStatement.setInt(nParam++, mnFkReferenceCategoryId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkCompanyBranchId_n > 0) callableStatement.setInt(nParam++, mnFkCompanyBranchId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkEntityId_n > 0) callableStatement.setInt(nParam++, mnFkEntityId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkPlantCompanyBranchId_n > 0) callableStatement.setInt(nParam++, mnFkPlantCompanyBranchId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkPlantEntityId_n > 0) callableStatement.setInt(nParam++, mnFkPlantEntityId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkTaxBasicId_n > 0) callableStatement.setInt(nParam++, mnFkTaxBasicId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkTaxId_n > 0) callableStatement.setInt(nParam++, mnFkTaxId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkYearId_n > 0) callableStatement.setInt(nParam++, mnFkYearId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkDpsYearId_n > 0) callableStatement.setInt(nParam++, mnFkDpsYearId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkDpsDocId_n > 0) callableStatement.setInt(nParam++, mnFkDpsDocId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkDpsAdjustmentYearId_n > 0) callableStatement.setInt(nParam++, mnFkDpsAdjustmentYearId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkDpsAdjustmentDocId_n > 0) callableStatement.setInt(nParam++, mnFkDpsAdjustmentDocId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkDiogYearId_n > 0) callableStatement.setInt(nParam++, mnFkDiogYearId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkDiogDocId_n > 0) callableStatement.setInt(nParam++, mnFkDiogDocId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkMfgYearId_n > 0) callableStatement.setInt(nParam++, mnFkMfgYearId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkMfgOrdId_n > 0) callableStatement.setInt(nParam++, mnFkMfgOrdId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkCfdId_n > 0) callableStatement.setInt(nParam++, mnFkCfdId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkCostGicId_n > 0) callableStatement.setInt(nParam++, mnFkCostGicId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkPayrollFormerId_n > 0) callableStatement.setInt(nParam++, mnFkPayrollFormerId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkPayrollId_n > 0) callableStatement.setInt(nParam++, mnFkPayrollId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkItemId_n > 0) callableStatement.setInt(nParam++, mnFkItemId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkItemAuxId_n > 0) callableStatement.setInt(nParam++, mnFkItemAuxId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkUnitId_n > 0) callableStatement.setInt(nParam++, mnFkUnitId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkBookkeepingYearId_n > 0) callableStatement.setInt(nParam++, mnFkBookkeepingYearId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkBookkeepingNumberId_n > 0) callableStatement.setInt(nParam++, mnFkBookkeepingNumberId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnPkEntryId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                // Save XML associated:
                
                for (SDataCfd cfd : maDbmsDataCfd) {
                    cfd.setFkRecordYearId_n(mnPkYearId);
                    cfd.setFkRecordPeriodId_n(mnPkPeriodId);
                    cfd.setFkRecordBookkeepingCenterId_n(mnPkBookkeepingCenterId);
                    cfd.setFkRecordRecordTypeId_n(msPkRecordTypeId);
                    cfd.setFkRecordNumberId_n(mnPkNumberId);
                    cfd.setFkRecordEntryId_n(mnPkEntryId);
                    cfd.setTimestamp(mtAuxDateCfd);

                    if (cfd.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }

    @Override
    public erp.mfin.data.SDataRecordEntry clone() {
        SDataRecordEntry clone = new SDataRecordEntry();

        clone.setIsRegistryNew(mbIsRegistryNew);

        clone.setPkYearId(mnPkYearId);
        clone.setPkPeriodId(mnPkPeriodId);
        clone.setPkBookkeepingCenterId(mnPkBookkeepingCenterId);
        clone.setPkRecordTypeId(msPkRecordTypeId);
        clone.setPkNumberId(mnPkNumberId);
        clone.setPkEntryId(mnPkEntryId);
        clone.setConcept(msConcept);
        clone.setReference(msReference);
        clone.setIsReferenceTax(mbIsReferenceTax);
        clone.setDebit(mdDebit);
        clone.setCredit(mdCredit);
        clone.setExchangeRate(mdExchangeRate);
        clone.setExchangeRateSystem(mdExchangeRateSystem);
        clone.setDebitCy(mdDebitCy);
        clone.setCreditCy(mdCreditCy);
        clone.setUnits(mdUnits);
        clone.setUserId(mnUserId);
        clone.setSortingPosition(mnSortingPosition);
        clone.setIsExchangeDifference(mbIsExchangeDifference);
        clone.setIsSystem(mbIsSystem);
        clone.setIsDeleted(mbIsDeleted);
        clone.setFkAccountIdXXX(msFkAccountIdXXX);
        clone.setFkAccountId(mnFkAccountId);
        clone.setFkCostCenterId_n(mnFkCostCenterId_n);
        clone.setFkAccountingMoveTypeId(mnFkAccountingMoveTypeId);
        clone.setFkAccountingMoveClassId(mnFkAccountingMoveClassId);
        clone.setFkAccountingMoveSubclassId(mnFkAccountingMoveSubclassId);
        clone.setFkSystemMoveClassId(mnFkSystemMoveClassId);
        clone.setFkSystemMoveTypeId(mnFkSystemMoveTypeId);
        clone.setFkSystemAccountClassId(mnFkSystemAccountClassId);
        clone.setFkSystemAccountTypeId(mnFkSystemAccountTypeId);
        clone.setFkSystemMoveCategoryIdXXX(mnFkSystemMoveCategoryIdXXX);
        clone.setFkSystemMoveTypeIdXXX(mnFkSystemMoveTypeIdXXX);
        clone.setFkCurrencyId(mnFkCurrencyId);
        clone.setFkCostCenterIdXXX_n(msFkCostCenterIdXXX_n);
        clone.setFkCheckWalletId_n(mnFkCheckWalletId_n);
        clone.setFkCheckId_n(mnFkCheckId_n);
        clone.setFkBizPartnerId_nr(mnFkBizPartnerId_nr);
        clone.setFkBizPartnerBranchId_n(mnFkBizPartnerBranchId_n);
        clone.setFkReferenceCategoryId_n(mnFkReferenceCategoryId_n);
        clone.setFkCompanyBranchId_n(mnFkCompanyBranchId_n);
        clone.setFkEntityId_n(mnFkEntityId_n);
        clone.setFkPlantCompanyBranchId_n(mnFkPlantCompanyBranchId_n);
        clone.setFkPlantEntityId_n(mnFkPlantEntityId_n);
        clone.setFkTaxBasicId_n(mnFkTaxBasicId_n);
        clone.setFkTaxId_n(mnFkTaxId_n);
        clone.setFkYearId_n(mnFkYearId_n);
        clone.setFkDpsYearId_n(mnFkDpsYearId_n);
        clone.setFkDpsDocId_n(mnFkDpsDocId_n);
        clone.setFkDpsAdjustmentYearId_n(mnFkDpsAdjustmentYearId_n);
        clone.setFkDpsAdjustmentDocId_n(mnFkDpsAdjustmentDocId_n);
        clone.setFkDiogYearId_n(mnFkDiogYearId_n);
        clone.setFkDiogDocId_n(mnFkDiogDocId_n);
        clone.setFkMfgYearId_n(mnFkMfgYearId_n);
        clone.setFkMfgOrdId_n(mnFkMfgOrdId_n);
        clone.setFkCfdId_n(mnFkCfdId_n);
        clone.setFkCostGicId_n(mnFkCostGicId_n);
        clone.setFkPayrollFormerId_n(mnFkPayrollFormerId_n);
        clone.setFkPayrollId_n(mnFkPayrollId_n);
        clone.setFkItemId_n(mnFkItemId_n);
        clone.setFkItemAuxId_n(mnFkItemAuxId_n);
        clone.setFkUnitId_n(mnFkUnitId_n);
        clone.setFkBookkeepingYearId_n(mnFkBookkeepingYearId_n);
        clone.setFkBookkeepingNumberId_n(mnFkBookkeepingNumberId_n);
        clone.setFkUserNewId(mnFkUserNewId);
        clone.setFkUserEditId(mnFkUserEditId);
        clone.setFkUserDeleteId(mnFkUserDeleteId);
        clone.setUserNewTs(mtUserNewTs);
        clone.setUserEditTs(mtUserEditTs);
        clone.setUserDeleteTs(mtUserDeleteTs);

        return clone;
    }

    @Override
    public int[] getRowPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowCode() {
        return msFkAccountIdXXX;
    }

    @Override
    public String getRowName() {
        return msConcept;
    }

    @Override
    public boolean isRowSystem() {
        return getIsSystem();
    }

    @Override
    public boolean isRowDeletable() {
        return !getIsSystem();
    }

    @Override
    public boolean isRowEdited() {
        return getIsRegistryEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        setIsRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = mnSortingPosition;
                break;
            case 1:
                value = msXtaSystemMoveType;
                break;
            case 2:
                value = msConcept;
                break;
            case 3:
                value = mdDebitCy;
                break;
            case 4:
                value = mdCreditCy;
                break;
            case 5:
                value = msDbmsCurrencyKey;
                break;
            case 6:
                value = mdExchangeRate;
                break;
            case 7:
                value = mdDebit;
                break;
            case 8:
                value = mdCredit;
                break;
            case 9:
                value = msFkAccountIdXXX;
                break;
            case 10:
                value = msDbmsAccount;
                break;
            case 11:
                value = msFkCostCenterIdXXX_n;
                break;
            case 12:
                value = msDbmsCostCenter_n;
                break;
            case 13:
                value = msXtaItemCode;
                break;
            case 14:
                value = msXtaItem;
                break;
            case 15:
                value = msXtaEntityCode;
                break;
            case 16:
                value = msXtaEntity;
                break;
            case 17:
                value = msXtaBizPartner;
                break;
            case 18:
                value = msReference;
                break;
            case 19:
                value = moDbmsCheck == null ? null : moDbmsCheck.getNumber();
                break;
            case 20:
                value = mbIsExchangeDifference;
                break;
            case 21:
                value = mbIsSystem;
                break;
            case 22:
                value = mnFkBookkeepingNumberId_n;
                break;
            case 23:
                value = msDbmsUserNew;
                break;
            case 24:
                value = mtUserNewTs;
                break;
            case 25:
                value = msDbmsUserEdit;
                break;
            case 26:
                value = mtUserEditTs;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
