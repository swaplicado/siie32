/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import sa.lib.SLibConsts;

/**
 *
 * @author Sergio Flores
 */
public class STrnProdOrderStockFinishRow extends erp.lib.table.STableRow {

    protected int mnPkSourceYearId;
    protected int mnPkSourceOrderId;
    protected double mdQuantityRequired;
    protected double mdQuantityFinished;
    protected double mdQuantityToFinish;
    protected int mnFkDestinyYearId;
    protected int mnFkDestinyOrderId;
    protected int mnFkDestinyChargeId;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected java.lang.String msAuxUnit;
    protected java.lang.String msAuxUnitSymbol;

    /**
     * Creates production order materials assignation or return row.
     * @param prodOrderSourceKey Source production order primary key (i.e. year ID and order ID).
     */
    public STrnProdOrderStockFinishRow(int[] prodOrderSourceKey) {
        this(prodOrderSourceKey, null);
    }

    /**
     * Creates production order materials assignation or return row.
     * @param prodOrderSourceKey Source production order primary key (i.e. year ID and order ID).
     * @param prodOrderDestinyChargeKey_n Destiny production order charge primary key (i.e. year ID, order ID and charge ID).
     */
    public STrnProdOrderStockFinishRow(int[] prodOrderSourceKey, int[] prodOrderDestityChargeKey_n) {
        mnPkSourceYearId = prodOrderSourceKey[0];
        mnPkSourceOrderId = prodOrderSourceKey[1];

        if (prodOrderDestityChargeKey_n == null) {
            mnFkDestinyYearId = SLibConsts.UNDEFINED;
            mnFkDestinyOrderId = SLibConsts.UNDEFINED;
            mnFkDestinyChargeId = SLibConsts.UNDEFINED;
        }
        else {
            mnFkDestinyYearId = prodOrderDestityChargeKey_n[0];
            mnFkDestinyOrderId = prodOrderDestityChargeKey_n[1];
            mnFkDestinyChargeId = prodOrderDestityChargeKey_n[2];
        }

        mdQuantityRequired = 0;
        mdQuantityFinished = 0;
        mdQuantityToFinish = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        msAuxUnit = "";
        msAuxUnitSymbol = "";
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(mnFkDestinyChargeId);
        mvValues.add(mdQuantityRequired);
        mvValues.add(mdQuantityFinished);
        mvValues.add(getQuantityPending());
        mvValues.add(mdQuantityToFinish);
        mvValues.add(msAuxUnitSymbol);
    }

    public void setPkSourceYearId(int n) { mnPkSourceYearId = n; }
    public void setPkSourceOrderId(int n) { mnPkSourceOrderId = n; }
    public void setFkDestinyYearId(int n) { mnFkDestinyYearId = n; }
    public void setFkDestinyOrderId(int n) { mnFkDestinyOrderId = n; }
    public void setFkDestinyChargeId(int n) { mnFkDestinyChargeId = n; }
    public void setQuantityRequired(double d) { mdQuantityRequired = d; }
    public void setQuantityFinished(double d) { mdQuantityFinished = d; }
    public void setQuantityToFinish(double d) { mdQuantityToFinish = d; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setAuxUnit(java.lang.String s) { msAuxUnit = s; }
    public void setAuxUnitSymbol(java.lang.String s) { msAuxUnitSymbol = s; }

    public int getPkSourceYearId() { return mnPkSourceYearId; }
    public int getPkSourceOrderId() { return mnPkSourceOrderId; }
    public int getFkDestinyYearId() { return mnFkDestinyYearId; }
    public int getFkDestinyOrderId() { return mnFkDestinyOrderId; }
    public int getFkDestinyChargeId() { return mnFkDestinyChargeId; }
    public double getQuantityRequired() { return mdQuantityRequired; }
    public double getQuantityFinished() { return mdQuantityFinished; }
    public double getQuantityToFinish() { return mdQuantityToFinish; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public java.lang.String getAuxUnit() { return msAuxUnit; }
    public java.lang.String getAuxUnitSymbol() { return msAuxUnitSymbol; }

    public int[] getProdOrderSourceKey() { return new int[] { mnPkSourceYearId, mnPkSourceOrderId }; }
    public int[] getProdOrderDestinyChargeKey() { return new int[] { mnFkDestinyYearId, mnFkDestinyOrderId, mnFkDestinyChargeId }; }
    public double getQuantityPending() { return mdQuantityRequired - mdQuantityFinished; }
}
