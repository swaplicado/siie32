/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.utils;

/**
 *
 * @author Edwin Carmona
 */
public class SDataEntryDpsMergeRow extends erp.lib.table.STableRow {
   
    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected int mnSortingPosition;
    protected java.lang.String msConceptKey;
    protected java.lang.String msConcept;
    protected double mdQuantity;
    protected boolean mbSelected;
    protected java.lang.String msUnitSymbol;
    protected double mdPrice; // to be shown only in SDialogCfdiPurchaseOrder40
    protected java.lang.String msCurrencyCode; // to be shown only in SDialogCfdiPurchaseOrder40
    protected int mnItemId;
    protected int mnUnitId;
    
    public SDataEntryDpsMergeRow() {
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setSortingPosition(int n) { mnSortingPosition = n; }
    public void setConceptKey(java.lang.String s) { msConceptKey = s; }
    public void setConcept(java.lang.String s) { msConcept = s; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setSelected(boolean b) { mbSelected = b; }
    public void setUnitSymbol(java.lang.String s) { msUnitSymbol = s; }
    public void setPrice(double d) { mdPrice = d; }
    public void setCurrencyCode(java.lang.String s) { msCurrencyCode = s; }
    public void setItemId(int n) { mnItemId = n; }
    public void setUnitId(int n) { mnUnitId = n; }
   
    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getSortingPosition() { return mnSortingPosition; }
    public java.lang.String getConceptKey() { return msConceptKey; }
    public java.lang.String getConcept() { return msConcept; }
    public double getQuantity() { return mdQuantity; }
    public boolean isSelected() { return mbSelected; }
    public java.lang.String getUnitSymbol() { return msUnitSymbol; }
    public double getPrice() { return mdPrice; }
    public java.lang.String getCurrencyCode() { return msCurrencyCode; }
    public int getItemId() { return mnItemId; }
    public int getUnitId() { return mnUnitId; }
    
    public int[] getDpsEntryKey() { return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId }; }
    
    public void reset() {
        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        mnSortingPosition = 0;
        msConceptKey = "";
        msConcept = "";
        mdQuantity = 0;
        mbSelected = false;
        msUnitSymbol = "";
        mdPrice = 0;
        msCurrencyCode = "";
        mnItemId = 0;
        mnUnitId = 0;
    }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(mnSortingPosition);
        mvValues.add(mbSelected);
        mvValues.add(msConceptKey);
        mvValues.add(msConcept);
        mvValues.add(mdQuantity);
        mvValues.add(msUnitSymbol);
        mvValues.add(mdPrice);
        mvValues.add(msCurrencyCode);
    }
}
