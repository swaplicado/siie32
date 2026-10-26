package erp.mtrn.utils;

import erp.mtrn.data.SDataDpsDpsMerge;
import erp.mtrn.data.SDataDpsEntry;
import java.util.List;

public class SMergeData {
    
    List<SDataDpsDpsMerge> lMergeEntries;
    double dTotalQuantity;
    List<SDataDpsEntry> lEntriesToAdd;
    List<SDataDpsEntry> lEntriesToDelete;

    public List<SDataDpsDpsMerge> getMergeEntries() {
        return lMergeEntries;
    }

    public void setMergeEntries(List<SDataDpsDpsMerge> lMergeEntries) {
        this.lMergeEntries = lMergeEntries;
    }

    public double getTotalQuantity() {
        return dTotalQuantity;
    }

    public void setTotalQuantity(double dTotalQuantity) {
        this.dTotalQuantity = dTotalQuantity;
    }

    public List<SDataDpsEntry> getEntriesToAdd() {
        return lEntriesToAdd;
    }

    public void setEntriesToAdd(List<SDataDpsEntry> lEntriesToAdd) {
        this.lEntriesToAdd = lEntriesToAdd;
    }

    public List<SDataDpsEntry> getEntriesToDelete() {
        return lEntriesToDelete;
    }

    public void setEntriesToDelete(List<SDataDpsEntry> lEntriesToDelete) {
        this.lEntriesToDelete = lEntriesToDelete;
    }
}
