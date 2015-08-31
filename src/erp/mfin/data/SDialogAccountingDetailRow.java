/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SDialogAccountingDetailRow extends erp.lib.table.STableRow {

    protected erp.client.SClientInterface miClient;

    protected int mnPkYearId;
    protected int mnPkPeriodId;
    protected int mnPkBookkeepingCenterId;
    protected java.lang.String msPkRecordTypeId;
    protected int mnPkNumberId;
    protected int mnPkEntryId;
    protected java.util.Date mtDate;
    protected java.lang.String msConcept;
    protected double mdDebit;
    protected double mdCredit;
    protected double mdBalance;
    protected double mdExchangeRate;
    protected int mnSortingPosition;
    protected boolean mbIsSystem;
    protected boolean mbIsDeleted;
    protected java.lang.String msFkAccountId;
    protected int mnFkAccountingMoveTypeId;
    protected int mnFkAccountingMoveClassId;
    protected int mnFkAccountingMoveSubclassId;
    protected int mnFkCurrencyId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsBookkeepingCenterCode;
    protected java.lang.String msDbmsCompanyBranchCode;
    protected java.lang.String msDbmsAccount;
    protected java.lang.String msDbmsCurrencyKey;
    protected java.lang.String msDbmsAccountingMoveType;
    protected java.lang.String msDbmsAccountingMoveClass;
    protected java.lang.String msDbmsAccountingMoveSubclass;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;

    public SDialogAccountingDetailRow(erp.client.SClientInterface client) {
        miClient = client;
        reset();
    }

    public void reset() {
        mnPkYearId = 0;
        mnPkPeriodId = 0;
        mnPkBookkeepingCenterId = 0;
        msPkRecordTypeId = "";
        mnPkNumberId = 0;
        mnPkEntryId = 0;
        mtDate = null;
        msConcept = "";
        mdDebit = 0;
        mdCredit = 0;
        mdBalance = 0;
        mdExchangeRate = 0;
        mnSortingPosition = 0;
        mbIsSystem = false;
        mbIsDeleted = false;
        msFkAccountId = "";
        mnFkAccountingMoveTypeId = 0;
        mnFkAccountingMoveClassId = 0;
        mnFkAccountingMoveSubclassId = 0;
        mnFkCurrencyId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsBookkeepingCenterCode = "";
        msDbmsCompanyBranchCode = "";
        msDbmsAccount = "";
        msDbmsCurrencyKey = "";
        msDbmsAccountingMoveType = "";
        msDbmsAccountingMoveClass = "";
        msDbmsAccountingMoveSubclass = "";
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkPeriodId(int n) { mnPkPeriodId = n; }
    public void setPkBookkeepingCenterId(int n) { mnPkBookkeepingCenterId = n; }
    public void setPkRecordTypeId(java.lang.String s) { msPkRecordTypeId = s; }
    public void setPkNumberId(int n) { mnPkNumberId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setConcept(java.lang.String s) { msConcept = s; }
    public void setDebit(double d) { mdDebit = d; }
    public void setCredit(double d) { mdCredit = d; }
    public void setBalance(double d) { mdBalance = d; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setSortingPosition(int n) { mnSortingPosition = n; }
    public void setIsSystem(boolean b) { mbIsSystem = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkAccountId(java.lang.String s) { msFkAccountId = s; }
    public void setFkAccountingMoveTypeId(int n) { mnFkAccountingMoveTypeId = n; }
    public void setFkAccountingMoveClassId(int n) { mnFkAccountingMoveClassId = n; }
    public void setFkAccountingMoveSubclassId(int n) { mnFkAccountingMoveSubclassId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
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
    public java.util.Date getDate() { return mtDate; }
    public java.lang.String getConcept() { return msConcept; }
    public double getDebit() { return mdDebit; }
    public double getCredit() { return mdCredit; }
    public double getBalance() { return mdBalance; }
    public double getExchangeRate() { return mdExchangeRate; }
    public int getSortingPosition() { return mnSortingPosition; }
    public boolean getIsSystem() { return mbIsSystem; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public java.lang.String getFkAccountId() { return msFkAccountId; }
    public int getFkAccountingMoveTypeId() { return mnFkAccountingMoveTypeId; }
    public int getFkAccountingMoveClassId() { return mnFkAccountingMoveClassId; }
    public int getFkAccountingMoveSubclassId() { return mnFkAccountingMoveSubclassId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsBookkeepingCenterCode(java.lang.String s) { msDbmsBookkeepingCenterCode = s; }
    public void setDbmsCompanyBranchCode(java.lang.String s) { msDbmsCompanyBranchCode = s; }
    public void setDbmsAccount(java.lang.String s) { msDbmsAccount = s; }
    public void setDbmsCurrencyKey(java.lang.String s) { msDbmsCurrencyKey = s; }
    public void setDbmsAccountingMoveType(java.lang.String s) { msDbmsAccountingMoveType = s; }
    public void setDbmsAccountingMoveClass(java.lang.String s) { msDbmsAccountingMoveClass = s; }
    public void setDbmsAccountingMoveSubclass(java.lang.String s) { msDbmsAccountingMoveSubclass = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public java.lang.String getDbmsBookkeepingCenterCode() { return msDbmsBookkeepingCenterCode; }
    public java.lang.String getDbmsCompanyBranchCode() { return msDbmsCompanyBranchCode; }
    public java.lang.String getDbmsAccount() { return msDbmsAccount; }
    public java.lang.String getDbmsCurrencyKey() { return msDbmsCurrencyKey; }
    public java.lang.String getDbmsAccountingMoveType() { return msDbmsAccountingMoveType; }
    public java.lang.String getDbmsAccountingMoveClass() { return msDbmsAccountingMoveClass; }
    public java.lang.String getDbmsAccountingMoveSubclass() { return msDbmsAccountingMoveSubclass; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(mtDate);
        mvValues.add(msFkAccountId);
        mvValues.add(msDbmsAccount);
        mvValues.add(msConcept);
        mvValues.add(mdDebit);
        mvValues.add(mdCredit);
        mvValues.add(mdBalance);
        mvValues.add(mdExchangeRate);
        mvValues.add(msDbmsCurrencyKey);
        mvValues.add(msDbmsAccountingMoveSubclass);
        mvValues.add(mnPkPeriodId == 0 ? "" : miClient.getSessionXXX().getFormatters().getYearFormat().format(mnPkYearId) + "-" + miClient.getSessionXXX().getFormatters().getMonthFormat().format(mnPkPeriodId));
        mvValues.add(msDbmsBookkeepingCenterCode);
        mvValues.add(msDbmsCompanyBranchCode);
        mvValues.add(mnPkNumberId == 0 ? "" : msPkRecordTypeId + "-" + miClient.getSessionXXX().getFormatters().getRecordNumberFormat().format(mnPkNumberId));
        mvValues.add(mnSortingPosition);
        mvValues.add(mbIsSystem);
        mvValues.add(mbIsDeleted);
        mvValues.add(msDbmsUserNew);
        mvValues.add(mtUserNewTs);
        mvValues.add(msDbmsUserEdit);
        mvValues.add(mtUserEditTs);
        mvValues.add(msDbmsUserDelete);
        mvValues.add(mtUserDeleteTs);
    }
}
