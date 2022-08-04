package erp.mod.trn.db;

import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SRowIdentifiedCostDpsEntry extends SRowIdentifiedCostDps {
    
    // document entry:

    public int DocEntryYearId;
    public int DocEntryDocId;
    public int DocEntryEntryId;
    public String DocEntryConceptKey;
    public String DocEntryConcept;
    public double DocEntryQuantity;
    public double DocEntrySubtotal;
    public double DocEntrySubtotalCy;
    public int DocEntryItemId;
    public int DocEntryUnitId;
    
    // cost of document entry:
    
    public double DocEntryCost;
    
    public SRowIdentifiedCostDpsEntry(SDbIdentifiedCostCalculation.Supply supply) {
        super(supply);
        
        DocEntryYearId = supply.DocEntryYearId;
        DocEntryDocId = supply.DocEntryDocId;
        DocEntryEntryId = supply.DocEntryEntryId;
        DocEntryConceptKey = supply.DocEntryConceptKey;
        DocEntryConcept = supply.DocEntryConcept;
        DocEntryQuantity = supply.DocEntryQuantity;
        DocEntrySubtotal = supply.DocEntrySubtotal;
        DocEntrySubtotalCy = supply.DocEntrySubtotalCy;
        DocEntryItemId = supply.DocEntryItemId;
        DocEntryUnitId = supply.DocEntryUnitId;
        
        DocEntryCost = 0.0;
    }
    
    public int[] getDocEntryKey() {
        return new int[] { DocEntryYearId, DocEntryDocId, DocEntryEntryId };
    }
    
    public double addDocEntryCost(final double cost) {
        return DocEntryCost = SLibUtils.roundAmount(DocEntryCost + cost);
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getDocEntryKey();
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = DocTypeCode;
                break;
            case 1:
                value = getDocNumber();
                break;
            case 2:
                value = DocDate;
                break;
            case 3:
                value = DocEntryConceptKey;
                break;
            case 4:
                value = DocEntryConcept;
                break;
            case 5:
                value = DocEntryQuantity;
                break;
            case 6:
                value = SLibUtils.roundAmount(DocEntrySubtotalCy * SupplyFactor);
                break;
            case 7:
                value = DocCurrencyCode;
                break;
            case 8:
                value = SLibUtils.roundAmount(DocEntrySubtotal * SupplyFactor);
                break;
            case 9:
                value = SLibUtils.roundAmount(DocEntryCost * SupplyFactor);
                break;
            case 10:
                value = SLibUtils.roundAmount((DocEntrySubtotal - DocEntryCost) * SupplyFactor); // contribution margin
                break;
            case 11:
                value = DocEntrySubtotal == 0.0 ? 0.0 : (DocEntrySubtotal - DocEntryCost) / DocEntrySubtotal; // contribution margin %
                break;
            default:
                // do nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
