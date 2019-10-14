/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores
 */
public class SLayoutBankRecordKey {

    protected int mnPkYearId;
    protected int mnPkPeriodId;
    protected int mnPkBookkeepingCenterId;
    protected java.lang.String msPkRecordTypeId;
    protected int mnPkNumberId;

    public SLayoutBankRecordKey(Object[] recordKey) {
        this((int) recordKey[0], (int) recordKey[1], (int) recordKey[2], (String) recordKey[3], (int) recordKey[4]);
    }

    public SLayoutBankRecordKey(int yearId, int periodId, int bookkeepingCenterId, String recordTypeId, int numberId) {
        mnPkYearId = yearId;
        mnPkPeriodId = periodId;
        mnPkBookkeepingCenterId = bookkeepingCenterId;
        msPkRecordTypeId = recordTypeId;
        mnPkNumberId = numberId;
    }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkPeriodId() { return mnPkPeriodId; }
    public int getPkBookkeepingCenterId() { return mnPkBookkeepingCenterId; }
    public java.lang.String getPkRecordTypeId() { return msPkRecordTypeId; }
    public int getPkNumberId() { return mnPkNumberId; }
    
    /**
     * Composes standard primary key of record.
     * @return 
     */
    public Object[] getPrimaryKey() {
        return new Object[] { mnPkYearId, mnPkPeriodId, mnPkBookkeepingCenterId, msPkRecordTypeId, mnPkNumberId };
    }
    
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

    @Override
    public SLayoutBankRecordKey clone() {
        SLayoutBankRecordKey clone = new SLayoutBankRecordKey(mnPkYearId, mnPkPeriodId, mnPkBookkeepingCenterId, msPkRecordTypeId, mnPkNumberId);
        
        return clone;
    }
}
