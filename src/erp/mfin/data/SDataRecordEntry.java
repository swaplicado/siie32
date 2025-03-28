/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.SErpConsts;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.STrnUtils;
import erp.mtrn.data.SDataCfd;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
 * Also update any change to class members in methods encodeJson() and decodeJson()!
 * All of them execute raw SQL queries, insertions or updates.
 */

/**
 *
 * @author Sergio Flores, Isabel Servín
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
    protected java.lang.String msOccasionalFiscalId;
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
    protected java.lang.String msDbmsSystemMoveClass;
    protected java.lang.String msDbmsSystemMoveType;
    protected java.lang.String msDbmsEntityCode;
    protected java.lang.String msDbmsEntity;
    protected java.lang.String msDbmsBizPartnerCode;
    protected java.lang.String msDbmsBizPartner;
    protected java.lang.String msDbmsItemCode;
    protected java.lang.String msDbmsItem;
    protected java.lang.String msDbmsItemAuxCode;
    protected java.lang.String msDbmsItemAux;
    protected java.lang.String msDbmsTax;
    protected java.lang.String msDbmsDps;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;
    protected erp.mfin.data.SDataCheck moDbmsCheck;
    protected int mnDbmsXmlFilesNumber;
    protected int mnDbmsIndirectXmlFilesNumber;
    protected HashSet<erp.mtrn.data.SDataCfd> maDbmsDataCfd;
    protected HashSet<erp.mtrn.data.SDataCfd> maDbmsDataIndirectCfd;
    protected HashSet<erp.mtrn.data.SDataCfd> maAuxDataCfdToDel;
            
    protected int mnAuxCheckNumber;
    protected java.util.Date mtAuxDateCfd;

    protected erp.mfin.data.SDataRecord moParentRecord;
    protected SDataAccount moAccount;
    protected SDataCostCenter moCostCenter;

    /**
     * Overrides java.lang.Object.clone() function.
     */
    public SDataRecordEntry() {
        super(SDataConstants.FIN_REC_ETY);
        reset();
    }
    
    /*
     * Private methods
     */
    
    private void sanitizeData() {
        if (msConcept.length() > LEN_CONCEPT) {
            msConcept = msConcept.substring(0, LEN_CONCEPT - SErpConsts.ELLIPSIS.length()).trim() + SErpConsts.ELLIPSIS;
        }
        if (msReference.length() > LEN_REFERENCE) {
            msReference = msReference.substring(0, LEN_REFERENCE - SErpConsts.ELLIPSIS.length()).trim() + SErpConsts.ELLIPSIS;
        }
    }

    /**
     * Get text for account complement.
     * WARNING: Consider that all DBMS descriptions should first be retrieved!
     * @param accountSystemType
     * @return 
     */
    private java.lang.String getAccountComplement(final int accountSystemType) {
        String accountComplement = "";
        
        switch (accountSystemType) {
            case SDataConstantsSys.FINS_TP_ACC_SYS_SUP:
            case SDataConstantsSys.FINS_TP_ACC_SYS_CUS:
            case SDataConstantsSys.FINS_TP_ACC_SYS_DBR:
            case SDataConstantsSys.FINS_TP_ACC_SYS_CDR:
                accountComplement = msDbmsBizPartner;
                break;

            case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH:
            case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK:
                accountComplement = msDbmsEntity;
                break;

            case SDataConstantsSys.FINS_TP_ACC_SYS_INV:
                accountComplement = msDbmsEntity + (msDbmsItem.isEmpty() ? "" : ", " + msDbmsItem);
                break;

            case SDataConstantsSys.FINS_TP_ACC_SYS_PUR:
            case SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ:
            case SDataConstantsSys.FINS_TP_ACC_SYS_SAL:
            case SDataConstantsSys.FINS_TP_ACC_SYS_SAL_ADJ:
                accountComplement = msDbmsItem;
                break;

            case SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT:
            case SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT:
                accountComplement = msDbmsTax;
                break;

            default:
        }
        
        return accountComplement;
    }
    
    private void readCheck(final java.sql.Statement statement) throws Exception {
        mnAuxCheckNumber = 0;
        moDbmsCheck = null;
        
        if (mnFkCheckWalletId_n != 0 && mnFkCheckId_n != 0) {
            SDataCheck check = new SDataCheck();
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
    }

    /*
     * Public methods
     */
    
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
    public void setOccasionalFiscalId(java.lang.String s) { msOccasionalFiscalId = s; }
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
    public java.lang.String getOccasionalFiscalId() { return msOccasionalFiscalId; }
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
    public void setDbmsSystemMoveClass(java.lang.String s) { msDbmsSystemMoveClass = s; }
    public void setDbmsSystemMoveType(java.lang.String s) { msDbmsSystemMoveType = s; }
    public void setDbmsEntityCode(java.lang.String s) { msDbmsEntityCode = s; }
    public void setDbmsEntity(java.lang.String s) { msDbmsEntity = s; }
    public void setDbmsBizPartnerCode(java.lang.String s) { msDbmsBizPartnerCode = s; }
    public void setDbmsBizPartner(java.lang.String s) { msDbmsBizPartner = s; }
    public void setDbmsItemCode(java.lang.String s) { msDbmsItemCode = s; }
    public void setDbmsItem(java.lang.String s) { msDbmsItem = s; }
    public void setDbmsItemAuxCode(java.lang.String s) { msDbmsItemAuxCode = s; }
    public void setDbmsItemAux(java.lang.String s) { msDbmsItemAux = s; }
    public void setDbmsTax(java.lang.String s) { msDbmsTax = s; }
    public void setDbmsDps(java.lang.String s) { msDbmsDps = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }
    public void setDbmsCheck(erp.mfin.data.SDataCheck o) { moDbmsCheck = o; }
    public void setDbmsXmlFilesNumber(int i) { mnDbmsXmlFilesNumber = i; }
    public void setDbmsIndirectXmlFilesNumber(int i) { mnDbmsIndirectXmlFilesNumber = i; }
    public void setDbmsDataCfd(HashSet<erp.mtrn.data.SDataCfd> a) { maDbmsDataCfd = a; }
    public void setDbmsDataIndirectCfd(HashSet<erp.mtrn.data.SDataCfd> a) { maDbmsDataIndirectCfd = a; }
    public void setAuxDataCfdToDel(HashSet<erp.mtrn.data.SDataCfd> a ) { maAuxDataCfdToDel = a; }
    
    public java.lang.String getDbmsAccount() { return msDbmsAccount; }
    public java.lang.String getDbmsAccountComplement() { return msDbmsAccountComplement; }
    public java.lang.String getDbmsCostCenter_n() { return msDbmsCostCenter_n; }
    public java.lang.String getDbmsCurrencyKey() { return msDbmsCurrencyKey; }
    public java.lang.String getDbmsAccountingMoveType() { return msDbmsAccountingMoveType; }
    public java.lang.String getDbmsAccountingMoveClass() { return msDbmsAccountingMoveClass; }
    public java.lang.String getDbmsAccountingMoveSubclass() { return msDbmsAccountingMoveSubclass; }
    public java.lang.String getDbmsSystemMoveClass() { return msDbmsSystemMoveClass; }
    public java.lang.String getDbmsSystemMoveType() { return msDbmsSystemMoveType; }
    public java.lang.String getDbmsEntityCode() { return msDbmsEntityCode; }
    public java.lang.String getDbmsEntity() { return msDbmsEntity; }
    public java.lang.String getDbmsBizPartnerCode() { return msDbmsBizPartnerCode; }
    public java.lang.String getDbmsBizPartner() { return msDbmsBizPartner; }
    public java.lang.String getDmbsItemCode() { return msDbmsItemCode; }
    public java.lang.String getDbmsItem() { return msDbmsItem; }
    public java.lang.String getDmbsItemAuxCode() { return msDbmsItemAuxCode; }
    public java.lang.String getDbmsItemAux() { return msDbmsItemAux; }
    public java.lang.String getDbmsTax() { return msDbmsTax; }
    public java.lang.String getDbmsDps() { return msDbmsDps; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }
    public erp.mfin.data.SDataCheck getDbmsCheck() { return moDbmsCheck; }
    public int getDbmsXmlFilesNumber() { return mnDbmsXmlFilesNumber; }
    public int getDbmsIndirectXmlFilesNumber() { return mnDbmsIndirectXmlFilesNumber; }
    public HashSet<erp.mtrn.data.SDataCfd> getDbmsDataCfd() { return maDbmsDataCfd; }
    public HashSet<erp.mtrn.data.SDataCfd> getDbmsDataIndirectCfd() { return maDbmsDataIndirectCfd; }
    public HashSet<erp.mtrn.data.SDataCfd> getAuxDataCfdToDel() { return maAuxDataCfdToDel; }

    public void setAuxCheckNumber(int n) { mnAuxCheckNumber = n; }
    public void setAuxDateCfd(java.util.Date t) { mtAuxDateCfd = t; }

    public int getAuxCheckNumber() { return mnAuxCheckNumber; }
    public java.util.Date getAuxDateCfd() { return mtAuxDateCfd; }
    
    public void setParentRecord(erp.mfin.data.SDataRecord o) { moParentRecord = o; }
    public void setAccount(SDataAccount o) { moAccount = o; }
    public void setCostCenter(SDataCostCenter o) { moCostCenter = o; }

    public erp.mfin.data.SDataRecord getParentRecord() { return moParentRecord; }
    public SDataAccount getAccount() { return moAccount; }
    public SDataCostCenter getCostCenter() { return moCostCenter; }

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
        msOccasionalFiscalId = "";
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
        msDbmsSystemMoveClass = "";
        msDbmsSystemMoveType = "";
        msDbmsEntityCode = "";
        msDbmsEntity = "";
        msDbmsBizPartnerCode = "";
        msDbmsBizPartner = "";
        msDbmsItemCode = "";
        msDbmsItem = "";
        msDbmsItemAuxCode = "";
        msDbmsItemAux = "";
        msDbmsTax = "";
        msDbmsDps = "";
        moDbmsCheck = null;
        
        mnDbmsXmlFilesNumber = 0;
        mnDbmsIndirectXmlFilesNumber = 0;
        maDbmsDataCfd = new HashSet<>();
        maDbmsDataIndirectCfd = new HashSet<>();
        maAuxDataCfdToDel = new HashSet<>();

        mnAuxCheckNumber = 0;
        mtAuxDateCfd = null;
        
        //moParentRecord = null; // prevent from clearing this member
        moAccount = null;
        moCostCenter = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        Object[] key = (Object[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT re.*, a.acc, a.fid_tp_acc_sys, cu.cur_key, mtp.tp_acc_mov, mcl.cl_acc_mov, mcls.cls_acc_mov, d.num, d.num_ser, " +
                    "smcl.name, smtp.name, un.usr, ue.usr, ud.usr, c.cc, e.code, e.ent, b.bp, bcls.bp_key, bclc.bp_key, i.item_key, i.item, ia.item_key, ia.item, t.tax, dpstp.code " +
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
                    "LEFT OUTER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc " +
                    "LEFT OUTER JOIN erp.cfgu_cob_ent AS e ON re.fid_cob_n = e.id_cob AND re.fid_ent_n = e.id_ent " +
                    "LEFT OUTER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                    "LEFT OUTER JOIN erp.bpsu_bp_ct AS bcls ON re.fid_bp_nr = bcls.id_bp AND bcls.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_SUP + " " +
                    "LEFT OUTER JOIN erp.bpsu_bp_ct AS bclc ON re.fid_bp_nr = bclc.id_bp AND bclc.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_CUS + " " +
                    "LEFT OUTER JOIN erp.itmu_item AS i ON re.fid_item_n = i.id_item " +
                    "LEFT OUTER JOIN erp.itmu_item AS ia ON re.fid_item_aux_n = ia.id_item " +
                    "LEFT OUTER JOIN erp.finu_tax AS t ON re.fid_tax_bas_n = t.id_tax_bas AND re.fid_tax_n = t.id_tax " +
                    "LEFT OUTER JOIN erp.trnu_tp_dps dpstp ON dpstp.id_ct_dps = d.fid_ct_dps AND dpstp.id_cl_dps = d.fid_cl_dps AND dpstp.id_tp_dps = d.fid_tp_dps " +
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
                msOccasionalFiscalId = resultSet.getString("re.occ_fiscal_id");
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
                msDbmsSystemMoveClass = resultSet.getString("smcl.name");
                msDbmsSystemMoveType = resultSet.getString("smtp.name");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

                msDbmsEntityCode = resultSet.getString("e.code");
                if (resultSet.wasNull()) {
                    msDbmsEntityCode = "";
                }

                msDbmsEntity = resultSet.getString("e.ent");
                if (resultSet.wasNull()) {
                    msDbmsEntity = "";
                }

                msDbmsBizPartner = resultSet.getString("b.bp");
                if (resultSet.wasNull()) {
                    msDbmsBizPartner = "";
                }
                
                switch (mnFkSystemAccountClassId) {
                    case SModSysConsts.FINS_CL_SYS_ACC_BPR_SUP:
                        msDbmsBizPartnerCode = resultSet.getString("bcls.bp_key");
                        break;
                    case SModSysConsts.FINS_CL_SYS_ACC_BPR_CUS:
                        msDbmsBizPartnerCode = resultSet.getString("bclc.bp_key");
                        break;
                    default:
                        msDbmsBizPartnerCode = "";
                }

                msDbmsItemCode = resultSet.getString("i.item_key");
                if (resultSet.wasNull()) {
                    msDbmsItemCode = "";
                }

                msDbmsItem = resultSet.getString("i.item");
                if (resultSet.wasNull()) {
                    msDbmsItem = "";
                }
                
                msDbmsItemAuxCode = resultSet.getString("ia.item_key");
                if (resultSet.wasNull()) {
                    msDbmsItemAuxCode = "";
                }

                msDbmsItemAux = resultSet.getString("ia.item");
                if (resultSet.wasNull()) {
                    msDbmsItemAux = "";
                }

                msDbmsTax = resultSet.getString("t.tax");
                if (resultSet.wasNull()) {
                    msDbmsTax = "";
                }

                String dpsSeries = resultSet.getString("d.num_ser");
                String dpsNumber = resultSet.getString("d.num");
                if (dpsSeries != null && dpsNumber != null) {
                    msDbmsDps = resultSet.getString("dpstp.code") + " " + STrnUtils.formatDocNumber(dpsSeries, dpsNumber);
                }
                else {
                    msDbmsDps = "";
                }

                int accountSystemType = resultSet.getInt("a.fid_tp_acc_sys");
                msDbmsAccountComplement = getAccountComplement(accountSystemType);
                readCheck(statement);
                
                moAccount = new SDataAccount();
                moAccount.read(new Object[] { msFkAccountIdXXX }, statement);
                
                if (!msFkCostCenterIdXXX_n.isEmpty()) {
                    moCostCenter = new SDataCostCenter();
                    moCostCenter.read(new Object[] { msFkCostCenterIdXXX_n }, statement);
                }

                // CFD de manera directa: 
                
                sql = "SELECT id_cfd FROM trn_cfd WHERE fid_rec_year_n = " + key[0] + " AND fid_rec_per_n = " + key[1] + " AND " +
                    "fid_rec_bkc_n = " + key[2] + " AND fid_rec_tp_rec_n = '" + key[3] + "' AND " +
                    "fid_rec_num_n = " + key[4] + " AND fid_rec_ety_n = " + key[5] + " ";
                readXml(statement, sql, SDataConstants.FINX_REC_CFD_DIRECT);
                
                // CFD de documentos de clientes y proveedores:
                
                sql = "SELECT id_cfd FROM trn_cfd WHERE fid_dps_year_n = " + mnFkDpsYearId_n + " AND fid_dps_doc_n = " + mnFkDpsDocId_n + " ";
                readXml(statement, sql, SDataConstants.FINX_REC_CFD_INDIRECT);
                
                // CFD de recepción de pagos:
                
                if (mnFkCfdId_n != 0) {
                    sql = "SELECT " + mnFkCfdId_n + " AS id_cfd;";
                    readXml(statement, sql, SDataConstants.FINX_REC_CFD_INDIRECT);
                }
                
                mnDbmsXmlFilesNumber = maDbmsDataCfd.size();
                mnDbmsIndirectXmlFilesNumber = maDbmsDataIndirectCfd.size();

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
    
    private void readXml(Statement statement, String sql, int typeCfd) throws Exception {
        SDataCfd cfd;
        Statement statementAux = statement.getConnection().createStatement();
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                cfd = new SDataCfd();
                if (cfd.read(new int[] { resultSet.getInt("id_cfd") }, statementAux)!= SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                
                if (!cfd.getDocXmlName().isEmpty()) {
                    switch (typeCfd) {
                        case SDataConstants.FINX_REC_CFD_DIRECT: maDbmsDataCfd.add(cfd); break;
                        case SDataConstants.FINX_REC_CFD_INDIRECT: maDbmsDataIndirectCfd.add(cfd); break;
                    }
                }
            }
        }
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
            
            // sanitize data:
            
            if (msConcept.length() > LEN_CONCEPT) {
                msConcept = SLibUtils.textLeft(msConcept, LEN_CONCEPT);
            }
            
            if (msReference.length() > LEN_REFERENCE) {
                msReference = SLibUtils.textLeft(msReference, LEN_REFERENCE);
            }
            
            // proceed saving data:
            
            callableStatement = connection.prepareCall(
                    "{ CALL fin_rec_ety_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?) }");
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
            callableStatement.setString(nParam++, msOccasionalFiscalId);
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
                    cfd.setFkFinRecordYearId_n(0);
                    cfd.setFkFinRecordPeriodId_n(0);
                    cfd.setFkFinRecordBookkeepingCenterId_n(0);
                    cfd.setFkFinRecordRecordTypeId_n("");
                    cfd.setFkFinRecordNumberId_n(0);
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
                
                // Delete XML:
                
                for (SDataCfd cfd : maAuxDataCfdToDel) {
                    cfd.setFkRecordYearId_n(0);
                    cfd.setFkRecordPeriodId_n(0);
                    cfd.setFkRecordBookkeepingCenterId_n(0);
                    cfd.setFkRecordRecordTypeId_n("");
                    cfd.setFkRecordNumberId_n(0);
                    cfd.setFkRecordEntryId_n(0);
                    
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
        clone.setOccasionalFiscalId(msOccasionalFiscalId);
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
                value = msDbmsSystemMoveType;
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
                value = msDbmsItemCode;
                break;
            case 14:
                value = msDbmsItem;
                break;
            case 15:
                value = msDbmsEntityCode;
                break;
            case 16:
                value = msDbmsEntity;
                break;
            case 17:
                value = msDbmsBizPartner;
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
    
    @Override
    @SuppressWarnings("unchecked")
    public String encodeJson(erp.client.SClientInterface client) throws ParseException, Exception {
        JSONObject jsonEntry = new JSONObject();
        
        jsonEntry.put("id_year", mnPkYearId);
        jsonEntry.put("id_per", mnPkPeriodId);
        jsonEntry.put("id_bkc", mnPkBookkeepingCenterId);
        jsonEntry.put("id_tp_rec", msPkRecordTypeId);
        jsonEntry.put("id_num", mnPkNumberId);
        jsonEntry.put("id_ety", mnPkEntryId);
        jsonEntry.put("concept", msConcept);
        jsonEntry.put("ref", msReference);
        jsonEntry.put("b_ref_tax", mbIsReferenceTax);
        jsonEntry.put("debit", mdDebit);
        jsonEntry.put("credit", mdCredit);
        jsonEntry.put("exc_rate", mdExchangeRate);
        jsonEntry.put("exc_rate_sys", mdExchangeRateSystem);
        jsonEntry.put("debit_cur", mdDebitCy);
        jsonEntry.put("credit_cur", mdCreditCy);
        jsonEntry.put("units", mdUnits);
        jsonEntry.put("usr_id", mnUserId);
        jsonEntry.put("sort_pos", mnSortingPosition);
        jsonEntry.put("occ_fiscal_id", msOccasionalFiscalId);
        jsonEntry.put("b_exc_diff", mbIsExchangeDifference);
        jsonEntry.put("b_sys", mbIsSystem);
        jsonEntry.put("b_del", mbIsDeleted);
        jsonEntry.put("fid_acc", msFkAccountIdXXX);
        jsonEntry.put("fk_acc", mnFkAccountId);
        jsonEntry.put("fk_cc_n", mnFkCostCenterId_n);
        jsonEntry.put("fid_tp_acc_mov", mnFkAccountingMoveTypeId);
        jsonEntry.put("fid_cl_acc_mov", mnFkAccountingMoveClassId);
        jsonEntry.put("fid_cls_acc_mov", mnFkAccountingMoveSubclassId);
        jsonEntry.put("fid_cl_sys_mov", mnFkSystemMoveClassId);
        jsonEntry.put("fid_tp_sys_mov", mnFkSystemMoveTypeId);
        jsonEntry.put("fid_cl_sys_acc", mnFkSystemAccountClassId);
        jsonEntry.put("fid_tp_sys_acc", mnFkSystemAccountTypeId);
        jsonEntry.put("fid_ct_sys_mov_xxx", mnFkSystemMoveCategoryIdXXX);
        jsonEntry.put("fid_tp_sys_mov_xxx", mnFkSystemMoveTypeIdXXX);
        jsonEntry.put("fid_cur", mnFkCurrencyId);
        jsonEntry.put("fid_cc_n", msFkCostCenterIdXXX_n);
        jsonEntry.put("fid_check_wal_n", mnFkCheckWalletId_n);
        jsonEntry.put("fid_check_n", mnFkCheckId_n);
        jsonEntry.put("fid_bp_nr", mnFkBizPartnerId_nr);
        jsonEntry.put("fid_bpb_n", mnFkBizPartnerBranchId_n);
        jsonEntry.put("fid_ct_ref_n", mnFkReferenceCategoryId_n);
        jsonEntry.put("fid_cob_n", mnFkCompanyBranchId_n);
        jsonEntry.put("fid_ent_n", mnFkEntityId_n);
        jsonEntry.put("fid_plt_cob_n", mnFkPlantCompanyBranchId_n);
        jsonEntry.put("fid_plt_ent_n", mnFkPlantEntityId_n);
        jsonEntry.put("fid_tax_bas_n", mnFkTaxBasicId_n);
        jsonEntry.put("fid_tax_n", mnFkTaxId_n);
        jsonEntry.put("fid_year_n", mnFkYearId_n);
        jsonEntry.put("fid_dps_year_n", mnFkDpsYearId_n);
        jsonEntry.put("fid_dps_doc_n", mnFkDpsDocId_n);
        jsonEntry.put("fid_dps_adj_year_n", mnFkDpsAdjustmentYearId_n);
        jsonEntry.put("fid_dps_adj_doc_n", mnFkDpsAdjustmentDocId_n);
        jsonEntry.put("fid_diog_year_n", mnFkDiogYearId_n);
        jsonEntry.put("fid_diog_doc_n", mnFkDiogDocId_n);
        jsonEntry.put("fid_mfg_year_n", mnFkMfgYearId_n);
        jsonEntry.put("fid_mfg_ord_n", mnFkMfgOrdId_n);
        jsonEntry.put("fid_cfd_n", mnFkCfdId_n);
        jsonEntry.put("fid_cost_gic_n", mnFkCostGicId_n);
        jsonEntry.put("fid_payroll_n", mnFkPayrollFormerId_n);
        jsonEntry.put("fid_pay_n", mnFkPayrollId_n);
        jsonEntry.put("fid_item_n", mnFkItemId_n);
        jsonEntry.put("fid_item_aux_n", mnFkItemAuxId_n);
        jsonEntry.put("fid_unit_n", mnFkUnitId_n);
        jsonEntry.put("fid_bkk_year_n", mnFkBookkeepingYearId_n);
        jsonEntry.put("fid_bkk_num_n", mnFkBookkeepingNumberId_n);
        jsonEntry.put("fid_usr_new", mnFkUserNewId);
        jsonEntry.put("fid_usr_edit", mnFkUserEditId);
        jsonEntry.put("fid_usr_del", mnFkUserDeleteId);
        jsonEntry.put("ts_new", SLibUtils.DbmsDateFormatDatetime.format(mtUserNewTs != null ? mtUserNewTs : new Date()));
        jsonEntry.put("ts_edit", SLibUtils.DbmsDateFormatDatetime.format(mtUserEditTs != null ? mtUserEditTs : new Date()));
        jsonEntry.put("ts_del", SLibUtils.DbmsDateFormatDatetime.format(mtUserDeleteTs != null ? mtUserDeleteTs : new Date()));
        
        return jsonEntry.toJSONString();
    }
    
    @Override
    public void decodeJson(erp.client.SClientInterface client, java.lang.String json) throws ParseException, Exception {
        reset();
        
        // recover data:
        
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonEntry = (JSONObject) jsonParser.parse(json);
        
        mnPkYearId = new Long((long) jsonEntry.get("id_year")).intValue();
        mnPkPeriodId = new Long((long) jsonEntry.get("id_per")).intValue();
        mnPkBookkeepingCenterId = new Long((long) jsonEntry.get("id_bkc")).intValue();
        msPkRecordTypeId = (java.lang.String) jsonEntry.get("id_tp_rec");
        mnPkNumberId = new Long((long) jsonEntry.get("id_num")).intValue();
        mnPkEntryId = new Long((long) jsonEntry.get("id_ety")).intValue();
        msConcept = (java.lang.String) jsonEntry.get("concept");
        msReference = (java.lang.String) jsonEntry.get("ref");
        mbIsReferenceTax = (boolean) jsonEntry.get("b_ref_tax");
        mdDebit = (double) jsonEntry.get("debit");
        mdCredit = (double) jsonEntry.get("credit");
        mdExchangeRate = (double) jsonEntry.get("exc_rate");
        mdExchangeRateSystem = (double) jsonEntry.get("exc_rate_sys");
        mdDebitCy = (double) jsonEntry.get("debit_cur");
        mdCreditCy = (double) jsonEntry.get("credit_cur");
        mdUnits = (double) jsonEntry.get("units");
        mnUserId = new Long((long) jsonEntry.get("usr_id")).intValue();
        mnSortingPosition = new Long((long) jsonEntry.get("sort_pos")).intValue();
        msOccasionalFiscalId = (java.lang.String) jsonEntry.get("occ_fiscal_id");
        mbIsExchangeDifference = (boolean) jsonEntry.get("b_exc_diff");
        mbIsSystem = (boolean) jsonEntry.get("b_sys");
        mbIsDeleted = (boolean) jsonEntry.get("b_del");
        msFkAccountIdXXX = (java.lang.String) jsonEntry.get("fid_acc");
        mnFkAccountId = new Long((long) jsonEntry.get("fk_acc")).intValue();
        mnFkCostCenterId_n = new Long((long) jsonEntry.get("fk_cc_n")).intValue();
        mnFkAccountingMoveTypeId = new Long((long) jsonEntry.get("fid_tp_acc_mov")).intValue();
        mnFkAccountingMoveClassId = new Long((long) jsonEntry.get("fid_cl_acc_mov")).intValue();
        mnFkAccountingMoveSubclassId = new Long((long) jsonEntry.get("fid_cls_acc_mov")).intValue();
        mnFkSystemMoveClassId = new Long((long) jsonEntry.get("fid_cl_sys_mov")).intValue();
        mnFkSystemMoveTypeId = new Long((long) jsonEntry.get("fid_tp_sys_mov")).intValue();
        mnFkSystemAccountClassId = new Long((long) jsonEntry.get("fid_cl_sys_acc")).intValue();
        mnFkSystemAccountTypeId = new Long((long) jsonEntry.get("fid_tp_sys_acc")).intValue();
        mnFkSystemMoveCategoryIdXXX = new Long((long) jsonEntry.get("fid_ct_sys_mov_xxx")).intValue();
        mnFkSystemMoveTypeIdXXX = new Long((long) jsonEntry.get("fid_tp_sys_mov_xxx")).intValue();
        mnFkCurrencyId = new Long((long) jsonEntry.get("fid_cur")).intValue();
        msFkCostCenterIdXXX_n = (java.lang.String) jsonEntry.get("fid_cc_n");
        mnFkCheckWalletId_n = new Long((long) jsonEntry.get("fid_check_wal_n")).intValue();
        mnFkCheckId_n = new Long((long) jsonEntry.get("fid_check_n")).intValue();
        mnFkBizPartnerId_nr = new Long((long) jsonEntry.get("fid_bp_nr")).intValue();
        mnFkBizPartnerBranchId_n = new Long((long) jsonEntry.get("fid_bpb_n")).intValue();
        mnFkReferenceCategoryId_n = new Long((long) jsonEntry.get("fid_ct_ref_n")).intValue();
        mnFkCompanyBranchId_n = new Long((long) jsonEntry.get("fid_cob_n")).intValue();
        mnFkEntityId_n = new Long((long) jsonEntry.get("fid_ent_n")).intValue();
        mnFkPlantCompanyBranchId_n = new Long((long) jsonEntry.get("fid_plt_cob_n")).intValue();
        mnFkPlantEntityId_n = new Long((long) jsonEntry.get("fid_plt_ent_n")).intValue();
        mnFkTaxBasicId_n = new Long((long) jsonEntry.get("fid_tax_bas_n")).intValue();
        mnFkTaxId_n = new Long((long) jsonEntry.get("fid_tax_n")).intValue();
        mnFkYearId_n = new Long((long) jsonEntry.get("fid_year_n")).intValue();
        mnFkDpsYearId_n = new Long((long) jsonEntry.get("fid_dps_year_n")).intValue();
        mnFkDpsDocId_n = new Long((long) jsonEntry.get("fid_dps_doc_n")).intValue();
        mnFkDpsAdjustmentYearId_n = new Long((long) jsonEntry.get("fid_dps_adj_year_n")).intValue();
        mnFkDpsAdjustmentDocId_n = new Long((long) jsonEntry.get("fid_dps_adj_doc_n")).intValue();
        mnFkDiogYearId_n = new Long((long) jsonEntry.get("fid_diog_year_n")).intValue();
        mnFkDiogDocId_n = new Long((long) jsonEntry.get("fid_diog_doc_n")).intValue();
        mnFkMfgYearId_n = new Long((long) jsonEntry.get("fid_mfg_year_n")).intValue();
        mnFkMfgOrdId_n = new Long((long) jsonEntry.get("fid_mfg_ord_n")).intValue();
        mnFkCfdId_n = new Long((long) jsonEntry.get("fid_cfd_n")).intValue();
        mnFkCostGicId_n = new Long((long) jsonEntry.get("fid_cost_gic_n")).intValue();
        mnFkPayrollFormerId_n = new Long((long) jsonEntry.get("fid_payroll_n")).intValue();
        mnFkPayrollId_n = new Long((long) jsonEntry.get("fid_pay_n")).intValue();
        mnFkItemId_n = new Long((long) jsonEntry.get("fid_item_n")).intValue();
        mnFkItemAuxId_n = new Long((long) jsonEntry.get("fid_item_aux_n")).intValue();
        mnFkUnitId_n = new Long((long) jsonEntry.get("fid_unit_n")).intValue();
        mnFkBookkeepingYearId_n = new Long((long) jsonEntry.get("fid_bkk_year_n")).intValue();
        mnFkBookkeepingNumberId_n = new Long((long) jsonEntry.get("fid_bkk_num_n")).intValue();
        mnFkUserNewId = new Long((long) jsonEntry.get("fid_usr_new")).intValue();
        mnFkUserEditId = new Long((long) jsonEntry.get("fid_usr_edit")).intValue();
        mnFkUserDeleteId = new Long((long) jsonEntry.get("fid_usr_del")).intValue();
        mtUserNewTs = SLibUtils.DbmsDateFormatDatetime.parse((java.lang.String) jsonEntry.get("ts_new"));
        mtUserEditTs = SLibUtils.DbmsDateFormatDatetime.parse((java.lang.String) jsonEntry.get("ts_edit"));
        mtUserDeleteTs = SLibUtils.DbmsDateFormatDatetime.parse((java.lang.String) jsonEntry.get("ts_del"));
        
        // check if registry is new:
        
        mbIsRegistryNew = mnPkEntryId == 0;
        
        // recover DBMS data:
        
        HashMap<String, Object> tempMap;
        String key;
        String value;
        String[] values;
        
        // account (name):
        
        tempMap = moParentRecord.getTempMap(SDataConstants.FIN_ACC);
        
        key = msFkAccountIdXXX;
        value = (String) tempMap.get(key);
        if (value == null) {
            value = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.FIN_ACC, new Object[] { msFkAccountIdXXX }, SLibConstants.DESCRIPTION_NAME);
            tempMap.put(key, value);
        }
        
        msDbmsAccount = value;
        
        // cost center (name):
        
        if (!msFkCostCenterIdXXX_n.isEmpty()) {
            tempMap = moParentRecord.getTempMap(SDataConstants.FIN_CC);
            
            key = msFkCostCenterIdXXX_n;
            value = (String) tempMap.get(key);
            if (value == null) {
                value = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.FIN_CC, new Object[] { msFkCostCenterIdXXX_n }, SLibConstants.DESCRIPTION_NAME);
                tempMap.put(key, value);
            }
            
            msDbmsCostCenter_n = value;
        }
        
        // currency (code):
        
        tempMap = moParentRecord.getTempMap(SDataConstants.CFGU_CUR);
        
        key = "" + mnFkCurrencyId;
        value = (String) tempMap.get(key);
        if (value == null) {
            value = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGU_CUR, new int[] { mnFkCurrencyId }, SLibConstants.DESCRIPTION_CODE);
            tempMap.put(key, value);
        }
        
        msDbmsCurrencyKey = value;
        
        // accounting movement subclass, class and type (names):
        
        tempMap = moParentRecord.getTempMap(SDataConstants.FINS_CLS_ACC_MOV);
        
        key = SLibUtils.textKey(new int[] { mnFkAccountingMoveTypeId, mnFkAccountingMoveClassId, mnFkAccountingMoveSubclassId });
        values = (String[]) tempMap.get(key);
        if (values == null) {
            values = new String[] {
                SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.FINS_TP_ACC_MOV, new int[] { mnFkAccountingMoveTypeId }, SLibConstants.DESCRIPTION_NAME),
                SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.FINS_CL_ACC_MOV, new int[] { mnFkAccountingMoveTypeId, mnFkAccountingMoveClassId }, SLibConstants.DESCRIPTION_NAME),
                SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.FINS_CLS_ACC_MOV, new int[] { mnFkAccountingMoveTypeId, mnFkAccountingMoveClassId, mnFkAccountingMoveSubclassId }, SLibConstants.DESCRIPTION_NAME)
            };
            tempMap.put(key, values);
        }
        
        msDbmsAccountingMoveType = values[0];
        msDbmsAccountingMoveClass = values[1];
        msDbmsAccountingMoveSubclass = values[2];
        
        // system movement type and class (names):
        
        tempMap = moParentRecord.getTempMap(SDataConstants.FINS_TP_SYS_MOV_32);
        
        key = SLibUtils.textKey(new int[] { mnFkSystemMoveClassId, mnFkSystemMoveTypeId });
        values = (String[]) tempMap.get(key);
        if (values == null) {
            values = new String[] {
                SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.FINS_CL_SYS_MOV_32, new int[] { mnFkSystemMoveClassId }, SLibConstants.DESCRIPTION_NAME),
                SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.FINS_TP_SYS_MOV_32, new int[] { mnFkSystemMoveClassId, mnFkSystemMoveTypeId }, SLibConstants.DESCRIPTION_NAME)
            };
            tempMap.put(key, values);
        }
        
        msDbmsSystemMoveClass = values[0];
        msDbmsSystemMoveType = values[1];
        
        // company-branch entity (code and name):
        
        if (mnFkCompanyBranchId_n != 0 && mnFkEntityId_n != 0) {
            tempMap = moParentRecord.getTempMap(SDataConstants.CFGU_COB_ENT);
            
            key = SLibUtils.textKey(new int[] { mnFkCompanyBranchId_n, mnFkEntityId_n });
            values = (String[]) tempMap.get(key);
            if (values == null) {
                values = new String[] {
                    SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGU_COB_ENT, new int[] { mnFkCompanyBranchId_n, mnFkEntityId_n }, SLibConstants.DESCRIPTION_CODE),
                    SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGU_COB_ENT, new int[] { mnFkCompanyBranchId_n, mnFkEntityId_n }, SLibConstants.DESCRIPTION_NAME)
                };
                tempMap.put(key, values);
            }
            
            msDbmsEntityCode = values[0];
            msDbmsEntity = values[1];
        }
        
        // business partner (code and name):
        
        if (mnFkBizPartnerId_nr != 0) {
            tempMap = moParentRecord.getTempMap(SDataConstants.BPSU_BP);
            
            key = "" + mnFkBizPartnerId_nr;
            values = (String[]) tempMap.get(key);
            if (values == null) {
                values = new String[] {
                    mnFkReferenceCategoryId_n == 0 ? "" + mnFkBizPartnerId_nr : SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.BPSU_BP_CT, new int[] { mnFkBizPartnerId_nr, mnFkReferenceCategoryId_n }, SLibConstants.DESCRIPTION_CODE),
                    SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.BPSU_BP, new int[] { mnFkBizPartnerId_nr }, SLibConstants.DESCRIPTION_NAME)
                };
                tempMap.put(key, values);
            }
            
            msDbmsBizPartnerCode = values[0];
            msDbmsBizPartner = values[1];
        }
        
        // item and auxiliar item (code and name):
        
        if (mnFkItemId_n != 0) {
            tempMap = moParentRecord.getTempMap(SDataConstants.ITMU_ITEM);
            
            key = "" + mnFkItemId_n;
            values = (String[]) tempMap.get(key);
            if (values == null) {
                values = new String[] {
                    SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.ITMU_ITEM, new int[] { mnFkItemId_n }, SLibConstants.DESCRIPTION_CODE),
                    SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.ITMU_ITEM, new int[] { mnFkItemId_n }, SLibConstants.DESCRIPTION_NAME)
                };
                tempMap.put(key, values);
            }
            
            msDbmsItemCode = values[0];
            msDbmsItem = values[1];
            
            if (mnFkItemAuxId_n != 0) {
                key = "" + mnFkItemAuxId_n;
                values = (String[]) tempMap.get(key);
                if (values == null) {
                    values = new String[] {
                        SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.ITMU_ITEM, new int[] { mnFkItemAuxId_n }, SLibConstants.DESCRIPTION_CODE),
                        SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.ITMU_ITEM, new int[] { mnFkItemAuxId_n }, SLibConstants.DESCRIPTION_NAME)
                    };
                    tempMap.put(key, values);
                }
                
                msDbmsItemAuxCode = values[0];
                msDbmsItemAux = values[1];
            }
        }
        
        // tax (name):
        
        if (mnFkTaxBasicId_n != 0 && mnFkTaxId_n != 0) {
            tempMap = moParentRecord.getTempMap(SDataConstants.FINU_TAX);
            
            key = SLibUtils.textKey(new int[] { mnFkTaxBasicId_n, mnFkTaxId_n });
            value = (String) tempMap.get(key);
            if (value == null) {
                value = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.FINU_TAX, new int[] { mnFkTaxBasicId_n, mnFkTaxId_n }, SLibConstants.DESCRIPTION_NAME);
                tempMap.put(key, value);
            }
            
            msDbmsTax = value;
        }
        
        // document:
        
        if (mnFkDpsYearId_n != 0 && mnFkDpsDocId_n != 0) {
            tempMap = moParentRecord.getTempMap(SDataConstants.TRN_DPS);
            
            key = SLibUtils.textKey(new int[] { mnFkDpsYearId_n, mnFkDpsDocId_n });
            value = (String) tempMap.get(key);
            if (value == null) {
                value = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.TRN_DPS, new int[] { mnFkDpsYearId_n, mnFkDpsDocId_n }, SLibConstants.DESCRIPTION_NAME);
                tempMap.put(key, value);
            }
            
            msDbmsDps = value;
        }
        
        // user new, edit and delete (name):
        
        if (mnFkUserNewId != 0 || mnFkUserEditId != 0 || mnFkUserDeleteId != 0) {
            tempMap = moParentRecord.getTempMap(SDataConstants.USRU_USR);
            
            if (mnFkUserNewId != 0) {
                key = "" + mnFkUserNewId;
                value = (String) tempMap.get(key);
                if (value == null) {
                    value = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.USRU_USR, new int[] { mnFkUserNewId }, SLibConstants.DESCRIPTION_NAME);
                    tempMap.put(key, value);
                }
                
                msDbmsUserNew = value;
            }
            
            if (mnFkUserEditId != 0) {
                key = "" + mnFkUserEditId;
                value = (String) tempMap.get(key);
                if (value == null) {
                    value = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.USRU_USR, new int[] { mnFkUserEditId }, SLibConstants.DESCRIPTION_NAME);
                    tempMap.put(key, value);
                }
                
                msDbmsUserEdit = value;
            }
            
            if (mnFkUserDeleteId != 0) {
                key = "" + mnFkUserDeleteId;
                value = (String) tempMap.get(key);
                if (value == null) {
                    value = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.USRU_USR, new int[] { mnFkUserDeleteId }, SLibConstants.DESCRIPTION_NAME);
                    tempMap.put(key, value);
                }
                
                msDbmsUserDelete = value;
            }
        }
        
        int accountSystemType = (int) SDataReadDescriptions.getField(client, SDataConstants.FIN_ACC, new Object[] { msFkAccountIdXXX }, SLibConstants.FIELD_TYPE);
        msDbmsAccountComplement = getAccountComplement(accountSystemType);
        readCheck(client.getSession().getStatement());
    }
}
