/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

/**
 *
 * @author Juan Barajas
 */
public class SFinRecordLayout {

    protected int mnPkYearId;
    protected int mnPkPeriodId;
    protected int mnPkBookkeepingCenterId;
    protected java.lang.String msPkRecordTypeId;
    protected int mnPkNumberId;

    public SFinRecordLayout(int year, int period, int bookkeepingCenter, String recordType, int number) {
        mnPkYearId = year;
        mnPkPeriodId = period;
        mnPkBookkeepingCenterId = bookkeepingCenter;
        msPkRecordTypeId = recordType;
        mnPkNumberId = number;
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkPeriodId(int n) { mnPkPeriodId = n; }
    public void setPkBookkeepingCenterId(int n) { mnPkBookkeepingCenterId = n; }
    public void setPkRecordTypeId(java.lang.String s) { msPkRecordTypeId = s; }
    public void setPkNumberId(int n) { mnPkNumberId = n; }

    public Object[] getPrimaryKey() {
        return new Object[] { mnPkYearId, mnPkPeriodId, mnPkBookkeepingCenterId, msPkRecordTypeId, mnPkNumberId };
    }
    
    public int getPkYearId() { return mnPkYearId; }
    public int getPkPeriodId() { return mnPkPeriodId; }
    public int getPkBookkeepingCenterId() { return mnPkBookkeepingCenterId; }
    public java.lang.String getPkRecordTypeId() { return msPkRecordTypeId; }
    public int getPkNumberId() { return mnPkNumberId; }
}
