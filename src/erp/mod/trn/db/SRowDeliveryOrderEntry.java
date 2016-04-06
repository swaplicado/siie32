/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Sergio Flores
 */
public class SRowDeliveryOrderEntry implements SGridRow {
    
    public int[] EntryKey;
    public String NumberSeries;
    public String Number;
    public String NumberReference;
    public Date Date;
    public int SortingPosition;
    public double SystemQuantity;
    public double Quantity;  // original units
    public double QuantityProcessed; // original units
    public String UnitCode; // original units
    public double Total;
    public double TotalProcessed;
    public String CurrencyCode;
    
    public SRowDeliveryOrderEntry() {
        EntryKey = null;
        NumberSeries = "";
        Number = "";
        NumberReference = "";
        Date = null;
        SortingPosition = 0;
        SystemQuantity = 0;
        Quantity = 0;
        QuantityProcessed = 0;
        UnitCode = "";
        Total = 0;
        TotalProcessed = 0;
        CurrencyCode = "";
    }

    public double calculateSystemQuantity(final double quantity) {
        double factor = (SystemQuantity == 0 ? 0 : Quantity / SystemQuantity);
        
        return factor == 0 ? 0 : quantity / factor;
    }

    public double computeTotalProcessed() {
        return TotalProcessed = SLibUtils.round(Quantity == 0 ? 0 : (QuantityProcessed / Quantity * Total), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
    }

    public double getQuantityRemaining() {
        return Quantity - QuantityProcessed;
    }
    
    public double getTotalRemaining() {
        return Total - TotalProcessed;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return EntryKey;
    }

    @Override
    public String getRowCode() {
        return STrnUtils.formatDocNumber(NumberSeries, Number);
    }

    @Override
    public String getRowName() {
        return STrnUtils.formatDocNumber(NumberSeries, Number);
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
                value = STrnUtils.formatDocNumber(NumberSeries, Number);
                break;
            case 1:
                value = NumberReference;
                break;
            case 2:
                value = Date;
                break;
            case 3:
                value = SortingPosition;
                break;
            case 4:
                value = Quantity;
                break;
            case 5:
                value = QuantityProcessed;
                break;
            case 6:
                value = getQuantityRemaining();
                break;
            case 7:
                value = UnitCode;
                break;
            case 8:
                value = Total;
                break;
            case 9:
                value = TotalProcessed;
                break;
            case 10:
                value = getTotalRemaining();
                break;
            case 11:
                value = CurrencyCode;
                break;
            default:
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
