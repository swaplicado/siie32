/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataEntryDpsDpsAdjustment extends erp.lib.table.STableRow {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected int mnSortingPosition;
    protected java.lang.String msConceptKey;
    protected java.lang.String msConcept;
    protected double mdQuantity;
    protected double mdQuantityReturned;
    protected double mdQuantityReturnedActual;
    protected double mdQuantityToReturn;
    protected java.lang.String msUnitSymbol;
    protected double mdTotalCy;
    protected double mdAmountReturned;
    protected double mdAmountReturnedActual;
    protected double mdAmountDiscounted;
    protected double mdAmountDiscountedActual;
    protected double mdAmountToReturn;
    protected double mdAmountToDiscount;
    protected java.lang.String msCurrencyKey;

    public SDataEntryDpsDpsAdjustment() {
        reset();
        prepareTableRow();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setSortingPosition(int n) { mnSortingPosition = n; }
    public void setConceptKey(java.lang.String s) { msConceptKey = s; }
    public void setConcept(java.lang.String s) { msConcept = s; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setQuantityReturned(double d) { mdQuantityReturned = d; }
    public void setQuantityReturnedActual(double d) { mdQuantityReturnedActual = d; }
    public void setQuantityToReturn(double d) { mdQuantityToReturn = d; }
    public void setUnitSymbol(java.lang.String s) { msUnitSymbol = s; }
    public void setTotalCy(double d) { mdTotalCy = d; }
    public void setAmountReturned(double d) { mdAmountReturned = d; }
    public void setAmountReturnedActual(double d) { mdAmountReturnedActual = d; }
    public void setAmountDiscounted(double d) { mdAmountDiscounted = d; }
    public void setAmountDiscountedActual(double d) { mdAmountDiscountedActual = d; }
    public void setAmountToReturn(double d) { mdAmountToReturn = d; }
    public void setAmountToDiscount(double d) { mdAmountToDiscount = d; }
    public void setCurrencyKey(java.lang.String s) { msCurrencyKey = s; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getSortingPosition() { return mnSortingPosition; }
    public java.lang.String getConceptKey() { return msConceptKey; }
    public java.lang.String getConcept() { return msConcept; }
    public double getQuantity() { return mdQuantity; }
    public double getQuantityReturned() { return mdQuantityReturned; }
    public double getQuantityReturnedActual() { return mdQuantityReturnedActual; }
    public double getQuantityToReturn() { return mdQuantityToReturn; }
    public java.lang.String getUnitSymbol() { return msUnitSymbol; }
    public double getTotalCy() { return mdTotalCy; }
    public double getAmountReturned() { return mdAmountReturned; }
    public double getAmountReturnedActual() { return mdAmountReturnedActual; }
    public double getAmountDiscounted() { return mdAmountDiscounted; }
    public double getAmountDiscountedActual() { return mdAmountDiscountedActual; }
    public double getAmountToReturn() { return mdAmountToReturn; }
    public double getAmountToDiscount() { return mdAmountToDiscount; }
    public java.lang.String getCurrencyKey() { return msCurrencyKey; }

    public double getQuantityNet() { return mdQuantity - mdQuantityReturned - mdQuantityReturnedActual; }
    public double getTotalNet() { return mdTotalCy - mdAmountReturned - mdAmountReturnedActual - mdAmountDiscounted - mdAmountDiscountedActual; }

    public int[] getDpsEntryKey() { return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId }; }

    public void reset() {
        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        mnSortingPosition = 0;
        msConceptKey = "";
        msConcept = "";
        mdQuantity = 0;
        mdQuantityReturned = 0;
        mdQuantityReturnedActual = 0;
        mdQuantityToReturn = 0;
        msUnitSymbol = "";
        mdTotalCy = 0;
        mdAmountReturned = 0;
        mdAmountReturnedActual = 0;
        mdAmountDiscounted = 0;
        mdAmountDiscountedActual = 0;
        mdAmountToReturn = 0;
        mdAmountToDiscount = 0;
        msCurrencyKey = "";
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(mnSortingPosition);
        mvValues.add(msConceptKey);
        mvValues.add(msConcept);
        mvValues.add(mdQuantity);
        mvValues.add(msUnitSymbol);
        mvValues.add(mdQuantityReturned);
        mvValues.add(mdQuantityReturnedActual);
        mvValues.add(getQuantityNet());
        mvValues.add(mdQuantityToReturn);
        mvValues.add(msUnitSymbol);
        mvValues.add(mdTotalCy);
        mvValues.add(msCurrencyKey);
        mvValues.add(mdAmountReturned);
        mvValues.add(mdAmountReturnedActual);
        mvValues.add(mdAmountDiscounted);
        mvValues.add(mdAmountDiscountedActual);
        mvValues.add(getTotalNet());
        mvValues.add(mdAmountToReturn);
        mvValues.add(mdAmountToDiscount);
        mvValues.add(msCurrencyKey);
    }
}
