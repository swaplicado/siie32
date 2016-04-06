/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Sergio Flores
 */
public class SRowDeliveryInvoiceEntry implements SGridRow {
    
    public int[] EntryKey;
    public int ItemId;
    public int UnitId;
    public int SortingPosition;
    public String EntryCode;
    public String EntryName;
    public double SystemQuantity;
    public double Quantity; // original units
    public double QuantityDelivered; // original units
    public String UnitCode; // original units
    public double Total;
    public double TotalDelivered;
    public String CurrencyCode;

    public SRowDeliveryInvoiceEntry() {
        EntryKey = null;
        ItemId = 0;
        UnitId = 0;
        SortingPosition = 0;
        EntryCode = "";
        EntryName = "";
        SystemQuantity = 0;
        Quantity = 0;
        QuantityDelivered = 0;
        UnitCode = "";
        Total = 0;
        TotalDelivered = 0;
        CurrencyCode = "";
    }

    public double calculateSystemQuantity(final double quantity) {
        double factor = (SystemQuantity == 0 ? 0 : Quantity / SystemQuantity);
        
        return factor == 0 ? 0 : quantity / factor;
    }

    public double computeTotalDelivered() {
        return TotalDelivered = SLibUtils.round(Quantity == 0 ? 0 : (QuantityDelivered / Quantity * Total), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
    }
    
    public double getQuantityRemaining() {
        return Quantity - QuantityDelivered;
    }
    
    public double getTotalRemaining() {
        return Total - TotalDelivered;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return EntryKey;
    }

    @Override
    public String getRowCode() {
        return EntryCode;
    }

    @Override
    public String getRowName() {
        return EntryName;
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
                value = SortingPosition;
                break;
            case 1:
                value = EntryCode;
                break;
            case 2:
                value = EntryName;
                break;
            case 3:
                value = Quantity;
                break;
            case 4:
                value = QuantityDelivered;
                break;
            case 5:
                value = getQuantityRemaining();
                break;
            case 6:
                value = UnitCode;
                break;
            case 7:
                value = Total;
                break;
            case 8:
                value = TotalDelivered;
                break;
            case 9:
                value = getTotalRemaining();
                break;
            case 10:
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
