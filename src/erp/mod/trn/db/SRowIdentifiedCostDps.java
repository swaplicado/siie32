package erp.mod.trn.db;

import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Sergio Flores
 */
public class SRowIdentifiedCostDps implements SGridRow {
    
    // document:
        
    public int DocYearId;
    public int DocDocId;
    public int DocCategoryId;
    public int DocClassId;
    public int DocTypeId;
    public String DocTypeCode;
    public String DocSeries;
    public String DocNumber;
    public Date DocDate;
    public double DocSubtotal;
    public double DocSubtotalCy;
    public int DocCurrencyId;
    public String DocCurrencyCode;
    public int DocBizPartnerId;
    public String DocBizPartnerName;
    
    // cost of document:
    
    public double DocCost;
    
    // external supply:

    public int SupplyFactor;
    
    public SRowIdentifiedCostDps(SDbIdentifiedCostCalculation.Supply supply) {
        DocYearId = supply.DocYearId;
        DocDocId = supply.DocDocId;
        DocCategoryId = supply.DocCategoryId;
        DocClassId = supply.DocClassId;
        DocTypeId = supply.DocTypeId;
        DocTypeCode = supply.DocTypeCode;
        DocSeries = supply.DocSeries;
        DocNumber = supply.DocNumber;
        DocDate = supply.DocDate;
        DocSubtotal = supply.DocSubtotal;
        DocSubtotalCy = supply.DocSubtotalCy;
        DocCurrencyId = supply.DocCurrencyId;
        DocCurrencyCode = supply.DocCurrencyCode;
        DocBizPartnerId = supply.DocBizPartnerId;
        DocBizPartnerName = supply.DocBizPartnerName;
        
        DocCost = 0.0;
        
        SupplyFactor = supply.SupplyFactor;
    }
    
    public int[] getDocKey() {
        return new int[] { DocYearId, DocDocId };
    }
    
    public String getDocNumber() {
        return STrnUtils.formatDocNumber(DocSeries, DocNumber);
    }
    
    public double addDocCost(final double cost) {
        return DocCost = SLibUtils.roundAmount(DocCost + cost);
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getDocKey();
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
                value = DocBizPartnerName;
                break;
            case 4:
                value = SLibUtils.roundAmount(DocSubtotalCy * SupplyFactor);
                break;
            case 5:
                value = DocCurrencyCode;
                break;
            case 6:
                value = SLibUtils.roundAmount(DocSubtotal * SupplyFactor);
                break;
            case 7:
                value = SLibUtils.roundAmount(DocCost * SupplyFactor);
                break;
            case 8:
                value = SLibUtils.roundAmount((DocSubtotal - DocCost) * SupplyFactor); // contribution margin
                break;
            case 9:
                value = DocSubtotal == 0.0 ? 0.0 : (DocSubtotal - DocCost) / DocSubtotal; // contribution margin %
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
