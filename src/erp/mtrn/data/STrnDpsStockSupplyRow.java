/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Sergio Flores
 */
public class STrnDpsStockSupplyRow extends erp.lib.table.STableRow {

    protected int mnPkDpsYearId;
    protected int mnPkDpsDocId;
    protected int mnPkDpsEntryId;
    protected int mnSortingPosition;
    protected double mdQuantityBase;
    protected double mdQuantityAdjusted;
    protected double mdQuantitySupplied;
    protected double mdQuantityToSupply;
    protected double mdOriginalQuantityBase;
    protected double mdOriginalQuantityAdjusted;
    protected double mdOriginalQuantitySupplied;
    protected double mdOriginalQuantityToSupply;
    protected double mdSurplusPercentage;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkOriginalUnitId;
    protected java.lang.String msAuxItem;
    protected java.lang.String msAuxItemKey;
    protected java.lang.String msAuxUnit;
    protected java.lang.String msAuxUnitSymbol;
    protected java.lang.String msAuxOriginalUnit;
    protected java.lang.String msAuxOriginalUnitSymbol;

    /**
     * Creates DPS stock supply row.
     * @param dpsEntryKey DPS entry primary key.
     */
    public STrnDpsStockSupplyRow(int[] dpsEntryKey) {
        mnPkDpsYearId = dpsEntryKey[0];
        mnPkDpsDocId = dpsEntryKey[1];
        mnPkDpsEntryId = dpsEntryKey[2];

        mnSortingPosition = 0;
        mdQuantityBase = 0;
        mdQuantityAdjusted = 0;
        mdQuantitySupplied = 0;
        mdQuantityToSupply = 0;
        mdOriginalQuantityBase = 0;
        mdOriginalQuantityAdjusted = 0;
        mdOriginalQuantitySupplied = 0;
        mdOriginalQuantityToSupply = 0;
        mdSurplusPercentage = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkOriginalUnitId = 0;
        msAuxItem = "";
        msAuxItemKey = "";
        msAuxUnit = "";
        msAuxUnitSymbol = "";
        msAuxOriginalUnit = "";
        msAuxOriginalUnitSymbol = "";
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(mnSortingPosition);
        mvValues.add(msAuxItemKey);
        mvValues.add(msAuxItem);
        mvValues.add(getOriginalQuantityNet());
        mvValues.add(getOriginalQuantityPending());
        mvValues.add(mdOriginalQuantityToSupply);
        mvValues.add(msAuxOriginalUnitSymbol);
    }

    public void setPkDpsYearId(int n) { mnPkDpsYearId = n; }
    public void setPkDpsDocId(int n) { mnPkDpsDocId = n; }
    public void setPkDpsEntryId(int n) { mnPkDpsEntryId = n; }
    public void setSortingPosition(int n) { mnSortingPosition = n; }
    public void setQuantityBase(double d) { mdQuantityBase = d; }
    public void setQuantityAdjusted(double d) { mdQuantityAdjusted = d; }
    public void setQuantitySupplied(double d) { mdQuantitySupplied = d; }
    public void setQuantityToSupply(double d) { mdQuantityToSupply = d; }
    public void setOriginalQuantityBase(double d) { mdOriginalQuantityBase = d; }
    public void setOriginalQuantityAdjusted(double d) { mdOriginalQuantityAdjusted = d; }
    public void setOriginalQuantitySupplied(double d) { mdOriginalQuantitySupplied = d; }
    public void setOriginalQuantityToSupply(double d) { mdOriginalQuantityToSupply = d; }
    public void setSurplusPercentage(double d) { mdSurplusPercentage = d; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkOriginalUnitId(int n) { mnFkOriginalUnitId = n; }
    public void setAuxItem(java.lang.String s) { msAuxItem = s; }
    public void setAuxItemKey(java.lang.String s) { msAuxItemKey = s; }
    public void setAuxUnit(java.lang.String s) { msAuxUnit = s; }
    public void setAuxUnitSymbol(java.lang.String s) { msAuxUnitSymbol = s; }
    public void setAuxOriginalUnit(java.lang.String s) { msAuxOriginalUnit = s; }
    public void setAuxOriginalUnitSymbol(java.lang.String s) { msAuxOriginalUnitSymbol = s; }

    public int getPkDpsYearId() { return mnPkDpsYearId; }
    public int getPkDpsDocId() { return mnPkDpsDocId; }
    public int getPkDpsEntryId() { return mnPkDpsEntryId; }
    public int getSortingPosition() { return mnSortingPosition; }
    public double getQuantityBase() { return mdQuantityBase; }
    public double getQuantityAdjusted() { return mdQuantityAdjusted; }
    public double getQuantitySupplied() { return mdQuantitySupplied; }
    public double getQuantityToSupply() { return mdQuantityToSupply; }
    public double getOriginalQuantityBase() { return mdOriginalQuantityBase; }
    public double getOriginalQuantityAdjusted() { return mdOriginalQuantityAdjusted; }
    public double getOriginalQuantitySupplied() { return mdOriginalQuantitySupplied; }
    public double getOriginalQuantityToSupply() { return mdOriginalQuantityToSupply; }
    public double getSurplusPercentage() { return mdSurplusPercentage; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkOriginalUnitId() { return mnFkOriginalUnitId; }
    public java.lang.String getAuxItem() { return msAuxItem; }
    public java.lang.String getAuxItemKey() { return msAuxItemKey; }
    public java.lang.String getAuxUnit() { return msAuxUnit; }
    public java.lang.String getAuxUnitSymbol() { return msAuxUnitSymbol; }
    public java.lang.String getAuxOriginalUnit() { return msAuxOriginalUnit; }
    public java.lang.String getAuxOriginalUnitSymbol() { return msAuxOriginalUnitSymbol; }

    public int[] getDpsEntryKey() { return new int[] { mnPkDpsYearId, mnPkDpsDocId, mnPkDpsEntryId }; }
    public double getQuantityNet() { return mdQuantityBase - mdQuantityAdjusted; }
    public double getQuantityPending() { return mdQuantityBase - mdQuantityAdjusted - mdQuantitySupplied; }
    public double getOriginalQuantityNet() { return mdOriginalQuantityBase - mdOriginalQuantityAdjusted; }
    public double getOriginalQuantityPending() { return mdOriginalQuantityBase - mdOriginalQuantityAdjusted - mdOriginalQuantitySupplied; }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkDpsYearId, mnPkDpsDocId, mnPkDpsEntryId };
    }
}
