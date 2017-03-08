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
    protected double mdQuantity;
    protected double mdQuantityAdjusted;
    protected double mdQuantitySupplied;
    protected double mdQuantityAboutToSupply;
    protected double mdOriginalQuantity;
    protected double mdOriginalQuantityAdjusted;
    protected double mdOriginalQuantitySupplied;
    protected double mdOriginalQuantityAboutToSupply;
    protected double mdSurplusPercentage;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkOriginalUnitId;
    protected java.lang.String msAuxItem;
    protected java.lang.String msAuxItemKey;
    protected java.lang.String msAuxUnitSymbol;
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
        mdQuantity = 0;
        mdQuantityAdjusted = 0;
        mdQuantitySupplied = 0;
        mdQuantityAboutToSupply = 0;
        mdOriginalQuantity = 0;
        mdOriginalQuantityAdjusted = 0;
        mdOriginalQuantitySupplied = 0;
        mdOriginalQuantityAboutToSupply = 0;
        mdSurplusPercentage = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkOriginalUnitId = 0;
        msAuxItem = "";
        msAuxItemKey = "";
        msAuxUnitSymbol = "";
        msAuxOriginalUnitSymbol = "";
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(mnSortingPosition);
        mvValues.add(msAuxItemKey);
        mvValues.add(msAuxItem);
        mvValues.add(getOriginalQuantityNet());
        mvValues.add(getOriginalQuantityPend());
        mvValues.add(mdOriginalQuantityAboutToSupply);
        mvValues.add(msAuxOriginalUnitSymbol);
        mvValues.add(mdSurplusPercentage);
    }

    public void setPkDpsYearId(int n) { mnPkDpsYearId = n; }
    public void setPkDpsDocId(int n) { mnPkDpsDocId = n; }
    public void setPkDpsEntryId(int n) { mnPkDpsEntryId = n; }
    public void setSortingPosition(int n) { mnSortingPosition = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setQuantityAdjusted(double d) { mdQuantityAdjusted = d; }
    public void setQuantitySupplied(double d) { mdQuantitySupplied = d; }
    public void setQuantityAboutToSupply(double d) { mdQuantityAboutToSupply = d; }
    public void setOriginalQuantity(double d) { mdOriginalQuantity = d; }
    public void setOriginalQuantityAdjusted(double d) { mdOriginalQuantityAdjusted = d; }
    public void setOriginalQuantitySupplied(double d) { mdOriginalQuantitySupplied = d; }
    public void setOriginalQuantityAboutToSupply(double d) { mdOriginalQuantityAboutToSupply = d; }
    public void setSurplusPercentage(double d) { mdSurplusPercentage = d; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkOriginalUnitId(int n) { mnFkOriginalUnitId = n; }
    public void setAuxItem(java.lang.String s) { msAuxItem = s; }
    public void setAuxItemKey(java.lang.String s) { msAuxItemKey = s; }
    public void setAuxUnitSymbol(java.lang.String s) { msAuxUnitSymbol = s; }
    public void setAuxOriginalUnitSymbol(java.lang.String s) { msAuxOriginalUnitSymbol = s; }

    public int getPkDpsYearId() { return mnPkDpsYearId; }
    public int getPkDpsDocId() { return mnPkDpsDocId; }
    public int getPkDpsEntryId() { return mnPkDpsEntryId; }
    public int getSortingPosition() { return mnSortingPosition; }
    public double getQuantity() { return mdQuantity; }
    public double getQuantityAdjusted() { return mdQuantityAdjusted; }
    public double getQuantitySupplied() { return mdQuantitySupplied; }
    public double getQuantityAboutToSupply() { return mdQuantityAboutToSupply; }
    public double getOriginalQuantity() { return mdOriginalQuantity; }
    public double getOriginalQuantityAdjusted() { return mdOriginalQuantityAdjusted; }
    public double getOriginalQuantitySupplied() { return mdOriginalQuantitySupplied; }
    public double getOriginalQuantityAboutToSupply() { return mdOriginalQuantityAboutToSupply; }
    public double getSurplusPercentage() { return mdSurplusPercentage; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkOriginalUnitId() { return mnFkOriginalUnitId; }
    public java.lang.String getAuxItem() { return msAuxItem; }
    public java.lang.String getAuxItemKey() { return msAuxItemKey; }
    public java.lang.String getAuxUnitSymbol() { return msAuxUnitSymbol; }
    public java.lang.String getAuxOriginalUnitSymbol() { return msAuxOriginalUnitSymbol; }

    public int[] getDpsEntryKey() { return new int[] { mnPkDpsYearId, mnPkDpsDocId, mnPkDpsEntryId }; }
    public double getQuantityNet() { return mdQuantity - mdQuantityAdjusted; }
    public double getQuantityPend() { return mdQuantity - mdQuantityAdjusted - mdQuantitySupplied; }
    public double getQuantityPendWithSurplus() { return (mdQuantity * (1d + mdSurplusPercentage)) - mdQuantityAdjusted - mdQuantitySupplied; }
    public double getOriginalQuantityNet() { return mdOriginalQuantity - mdOriginalQuantityAdjusted; }
    public double getOriginalQuantityPend() { return mdOriginalQuantity - mdOriginalQuantityAdjusted - mdOriginalQuantitySupplied; }
    public double getOriginalQuantityPendWithSurplus() { return (mdOriginalQuantity * (1d + mdSurplusPercentage)) - mdOriginalQuantityAdjusted - mdOriginalQuantitySupplied; }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkDpsYearId, mnPkDpsDocId, mnPkDpsEntryId };
    }
}
