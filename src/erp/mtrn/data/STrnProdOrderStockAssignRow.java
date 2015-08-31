/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Sergio Flores
 */
public class STrnProdOrderStockAssignRow extends erp.lib.table.STableRow {

    protected int mnPkYearId;
    protected int mnPkOrderId;
    protected int mnPkChargeId;
    protected double mdQuantityRequired;
    protected double mdQuantityAssigned;
    protected double mdQuantityToAssign;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected java.lang.String msAuxItem;
    protected java.lang.String msAuxItemKey;
    protected java.lang.String msAuxUnit;
    protected java.lang.String msAuxUnitSymbol;

    /**
     * Creates production order materials assignation or return row.
     * @param prodOrderChargeKey Production order charge primary key (i.e. year ID, order ID and charge ID).
     */
    public STrnProdOrderStockAssignRow(int[] prodOrderChargeKey) {
        mnPkYearId = prodOrderChargeKey[0];
        mnPkOrderId = prodOrderChargeKey[1];
        mnPkChargeId = prodOrderChargeKey[2];

        mdQuantityRequired = 0;
        mdQuantityAssigned = 0;
        mdQuantityToAssign = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        msAuxItem = "";
        msAuxItemKey = "";
        msAuxUnit = "";
        msAuxUnitSymbol = "";
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(msAuxItemKey);
        mvValues.add(msAuxItem);
        mvValues.add(mdQuantityRequired);
        mvValues.add(getQuantityPending());
        mvValues.add(mdQuantityToAssign);
        mvValues.add(msAuxUnitSymbol);
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkOrderId(int n) { mnPkOrderId = n; }
    public void setPkChargeId(int n) { mnPkChargeId = n; }
    public void setQuantityRequired(double d) { mdQuantityRequired = d; }
    public void setQuantityAssigned(double d) { mdQuantityAssigned = d; }
    public void setQuantityToAssign(double d) { mdQuantityToAssign = d; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setAuxItem(java.lang.String s) { msAuxItem = s; }
    public void setAuxItemKey(java.lang.String s) { msAuxItemKey = s; }
    public void setAuxUnit(java.lang.String s) { msAuxUnit = s; }
    public void setAuxUnitSymbol(java.lang.String s) { msAuxUnitSymbol = s; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkOrderId() { return mnPkOrderId; }
    public int getPkChargeId() { return mnPkChargeId; }
    public double getQuantityRequired() { return mdQuantityRequired; }
    public double getQuantityAssigned() { return mdQuantityAssigned; }
    public double getQuantityToAssign() { return mdQuantityToAssign; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public java.lang.String getAuxItem() { return msAuxItem; }
    public java.lang.String getAuxItemKey() { return msAuxItemKey; }
    public java.lang.String getAuxUnit() { return msAuxUnit; }
    public java.lang.String getAuxUnitSymbol() { return msAuxUnitSymbol; }

    public int[] getProdOrderChargeKey() { return new int[] { mnPkYearId, mnPkOrderId, mnPkChargeId }; }
    public double getQuantityPending() { return mdQuantityRequired - mdQuantityAssigned; }
}
