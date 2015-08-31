/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Sergio Flores
 */
public class STrnDpsStockReturnRow extends erp.lib.table.STableRow {

    protected int mnPkDpsAdjustmentYearId;
    protected int mnPkDpsAdjustmentDocId;
    protected int mnPkDpsAdjustmentEntryId;
    protected int mnSortingPosition;
    protected double mdQuantityBase;
    protected double mdQuantityReturned;
    protected double mdQuantityToReturn;
    protected double mdOriginalQuantityBase;
    protected double mdOriginalQuantityReturned;
    protected double mdOriginalQuantityToReturn;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkOriginalUnitId;
    protected int mnFkDpsYearId;
    protected int mnFkDpsDocId;
    protected int mnFkDpsEntryId;
    protected java.lang.String msAuxItem;
    protected java.lang.String msAuxItemKey;
    protected java.lang.String msAuxUnit;
    protected java.lang.String msAuxUnitSymbol;
    protected java.lang.String msAuxOriginalUnit;
    protected java.lang.String msAuxOriginalUnitSymbol;
    protected java.lang.String msAuxDocNumberSeries;
    protected java.lang.String msAuxDocNumber;
    protected java.lang.String msAuxDocType;
    protected java.util.Date mtAuxDocDate;

    /**
     * Creates DPS stock supply row.
     * @param dpsEntryKey DPS entry primary key.
     */
    public STrnDpsStockReturnRow(int[] dpsAdjustmentEntryKey, int[] dpsEntryKey) {
        mnPkDpsAdjustmentYearId = dpsAdjustmentEntryKey[0];
        mnPkDpsAdjustmentDocId = dpsAdjustmentEntryKey[1];
        mnPkDpsAdjustmentEntryId = dpsAdjustmentEntryKey[2];

        mnSortingPosition = 0;
        mdQuantityBase = 0;
        mdQuantityReturned = 0;
        mdQuantityToReturn = 0;
        mdOriginalQuantityBase = 0;
        mdOriginalQuantityReturned = 0;
        mdOriginalQuantityToReturn = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkOriginalUnitId = 0;
        mnFkDpsYearId = dpsEntryKey[0];
        mnFkDpsDocId = dpsEntryKey[1];
        mnFkDpsEntryId = dpsEntryKey[2];
        msAuxItem = "";
        msAuxItemKey = "";
        msAuxUnit = "";
        msAuxUnitSymbol = "";
        msAuxOriginalUnit = "";
        msAuxOriginalUnitSymbol = "";
        msAuxDocNumberSeries = "";
        msAuxDocNumber = "";
        msAuxDocType = "";
        mtAuxDocDate = null;
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(mnSortingPosition);
        mvValues.add(msAuxItemKey);
        mvValues.add(msAuxItem);
        mvValues.add(getOriginalQuantityPending());
        mvValues.add(mdOriginalQuantityToReturn);
        mvValues.add(msAuxOriginalUnitSymbol);
        mvValues.add(msAuxDocType);
        mvValues.add((msAuxDocNumberSeries.length() == 0 ? "" : msAuxDocNumberSeries + "-") + msAuxDocNumber);
        mvValues.add(mtAuxDocDate);
    }

    public void setPkDpsAdjustmentYearId(int n) { mnPkDpsAdjustmentYearId = n; }
    public void setPkDpsAdjustmentDocId(int n) { mnPkDpsAdjustmentDocId = n; }
    public void setPkDpsAdjustmentEntryId(int n) { mnPkDpsAdjustmentEntryId = n; }
    public void setSortingPosition(int n) { mnSortingPosition = n; }
    public void setQuantityBase(double d) { mdQuantityBase = d; }
    public void setQuantityReturned(double d) { mdQuantityReturned = d; }
    public void setQuantityToReturn(double d) { mdQuantityToReturn = d; }
    public void setOriginalQuantityBase(double d) { mdOriginalQuantityBase = d; }
    public void setOriginalQuantityReturned(double d) { mdOriginalQuantityReturned = d; }
    public void setOriginalQuantityToReturn(double d) { mdOriginalQuantityToReturn = d; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkOriginalUnitId(int n) { mnFkOriginalUnitId = n; }
    public void setFkDpsYearId(int n) { mnFkDpsYearId = n; }
    public void setFkDpsDocId(int n) { mnFkDpsDocId = n; }
    public void setFkDpsEntryId(int n) { mnFkDpsEntryId = n; }
    public void setAuxItem(java.lang.String s) { msAuxItem = s; }
    public void setAuxItemKey(java.lang.String s) { msAuxItemKey = s; }
    public void setAuxUnit(java.lang.String s) { msAuxUnit = s; }
    public void setAuxUnitSymbol(java.lang.String s) { msAuxUnitSymbol = s; }
    public void setAuxOriginalUnit(java.lang.String s) { msAuxOriginalUnit = s; }
    public void setAuxOriginalUnitSymbol(java.lang.String s) { msAuxOriginalUnitSymbol = s; }
    public void setAuxDocNumberSeries(java.lang.String s) { msAuxDocNumberSeries = s; }
    public void setAuxDocNumber(java.lang.String s) { msAuxDocNumber = s; }
    public void setAuxDocType(java.lang.String s) { msAuxDocType = s; }
    public void setAuxDocDate(java.util.Date t) { mtAuxDocDate = t; }

    public int getPkDpsAdjustmentYearId() { return mnPkDpsAdjustmentYearId; }
    public int getPkDpsAdjustmentDocId() { return mnPkDpsAdjustmentDocId; }
    public int getPkDpsAdjustmentEntryId() { return mnPkDpsAdjustmentEntryId; }
    public int getSortingPosition() { return mnSortingPosition; }
    public double getQuantityBase() { return mdQuantityBase; }
    public double getQuantityReturned() { return mdQuantityReturned; }
    public double getQuantityToReturn() { return mdQuantityToReturn; }
    public double getOriginalQuantityBase() { return mdOriginalQuantityBase; }
    public double getOriginalQuantityReturned() { return mdOriginalQuantityReturned; }
    public double getOriginalQuantityToReturn() { return mdOriginalQuantityToReturn; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkOriginalUnitId() { return mnFkOriginalUnitId; }
    public int getFkDpsYearId() { return mnFkDpsYearId; }
    public int getFkDpsDocId() { return mnFkDpsDocId; }
    public int getFkDpsEntryId() { return mnFkDpsEntryId; }
    public java.lang.String getAuxItem() { return msAuxItem; }
    public java.lang.String getAuxItemKey() { return msAuxItemKey; }
    public java.lang.String getAuxUnit() { return msAuxUnit; }
    public java.lang.String getAuxUnitSymbol() { return msAuxUnitSymbol; }
    public java.lang.String getAuxOriginalUnit() { return msAuxOriginalUnit; }
    public java.lang.String getAuxOriginalUnitSymbol() { return msAuxOriginalUnitSymbol; }
    public java.lang.String getAuxDocNumberSeries() { return msAuxDocNumberSeries; }
    public java.lang.String getAuxDocNumber() { return msAuxDocNumber; }
    public java.lang.String getAuxDocType() { return msAuxDocType; }
    public java.util.Date getAuxDocDate() { return mtAuxDocDate; }

    public int[] getDpsAdjustmentEntryKey() { return new int[] { mnPkDpsAdjustmentYearId, mnPkDpsAdjustmentDocId, mnPkDpsAdjustmentEntryId }; }
    public int[] getDpsEntryKey() { return new int[] { mnFkDpsYearId, mnFkDpsDocId, mnFkDpsEntryId }; }
    public double getQuantityPending() { return mdQuantityBase - mdQuantityReturned; }
    public double getOriginalQuantityPending() { return mdOriginalQuantityBase - mdOriginalQuantityReturned; }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkDpsAdjustmentYearId, mnPkDpsAdjustmentDocId, mnPkDpsAdjustmentEntryId };
    }
}
