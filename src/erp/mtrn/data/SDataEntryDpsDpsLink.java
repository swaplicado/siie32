/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataEntryDpsDpsLink extends erp.lib.table.STableRow {
    
    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected int mnSortingPosition;
    protected java.lang.String msConceptKey;
    protected java.lang.String msConcept;
    protected double mdQuantity;
    protected double mdQuantityLinked;
    protected double mdQuantityLinkedActual;
    protected double mdQuantityToLink;
    protected java.lang.String msUnitSymbol;
    protected double mdSurplusPercentage;
    
    protected boolean mbAuxEntryPriceNeeded;
    protected SGuiDpsEntryPrice moAuxSGuiDpsEntryPrice;

    public SDataEntryDpsDpsLink() {
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
    public void setQuantityLinked(double d) { mdQuantityLinked = d; }
    public void setQuantityLinkedActual(double d) { mdQuantityLinkedActual = d; }
    public void setQuantityToLink(double d) { mdQuantityToLink = d; }
    public void setUnitSymbol(java.lang.String s) { msUnitSymbol = s; }
    public void setSurplusPercentage(double d) { mdSurplusPercentage = d; }
   
    public void setAuxIsEntryPriceNeeded(boolean b) { mbAuxEntryPriceNeeded = b; }
    public void setAuxSGuiDpsEntryPrice(SGuiDpsEntryPrice d) { moAuxSGuiDpsEntryPrice = d; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getSortingPosition() { return mnSortingPosition; }
    public java.lang.String getConceptKey() { return msConceptKey; }
    public java.lang.String getConcept() { return msConcept; }
    public double getQuantity() { return mdQuantity; }
    public double getQuantityLinked() { return mdQuantityLinked; }
    public double getQuantityLinkedActual() { return mdQuantityLinkedActual; }
    public double getQuantityToLink() { return mdQuantityToLink; }
    public java.lang.String getUnitSymbol() { return msUnitSymbol; }
    public double getSurplusPercentage() { return mdSurplusPercentage; }
    
    public boolean getAuxIsEntryPriceNeeded() { return mbAuxEntryPriceNeeded; }
    public SGuiDpsEntryPrice getAuxSGuiDpsEntryPrice() { return moAuxSGuiDpsEntryPrice; }
    public double getQuantityToBeLinked() { return mdQuantity - mdQuantityLinked - mdQuantityLinkedActual; }

    public int[] getDpsEntryKey() { return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId }; }

    public void reset() {
        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        mnSortingPosition = 0;
        msConceptKey = "";
        msConcept = "";
        mdQuantity = 0;
        mdQuantityLinked = 0;
        mdQuantityLinkedActual = 0;
        mdQuantityToLink = 0;
        msUnitSymbol = "";
        mdSurplusPercentage = 0;
        moAuxSGuiDpsEntryPrice = null;
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(mnSortingPosition);
        mvValues.add(msConceptKey);
        mvValues.add(msConcept);
        mvValues.add(mdQuantity);
        mvValues.add(msUnitSymbol);
        mvValues.add(mdQuantityLinked);
        mvValues.add(mdQuantityLinkedActual);
        mvValues.add(getQuantityToBeLinked());
        mvValues.add(mdQuantityToLink);
        mvValues.add(msUnitSymbol);
        mvValues.add(mdSurplusPercentage);
    }
}
