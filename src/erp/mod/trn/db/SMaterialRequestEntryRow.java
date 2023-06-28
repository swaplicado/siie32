/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Edwin Carmona
 */
public class SMaterialRequestEntryRow implements SGridRow {
    
    protected int mnPkMatRequestId;
    protected int mnPkEntryId;
    protected double mdQuantity;
    //protected boolean mbDeleted;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    
    protected double mdAuxSegregated;
    protected double mdAuxStock;
    protected double mdAuxToSegregate;
    
    public void setPkMatRequestId(int n) { mnPkMatRequestId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }

    public void setAuxSegregated(double mdAuxSegregated) { this.mdAuxSegregated = mdAuxSegregated; }
    public void setAuxStock(double mdAuxStock) { this.mdAuxStock = mdAuxStock; }
    public void setAuxToSegregate(double mdAuxToSegregate) { this.mdAuxToSegregate = mdAuxToSegregate; }

    public int getPkMatRequestId() { return mnPkMatRequestId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public double getQuantity() { return mdQuantity; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public double getAuxSegregated() { return mdAuxSegregated; }
    public double getAuxStock() { return mdAuxStock; }
    public double getAuxToSegregate() { return mdAuxToSegregate; }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkMatRequestId, mnPkEntryId };
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return false;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {
        
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch(col) {
            case 0:
                value = "Codigo";
                break;
            case 1:
                value = "Item";
                break;
            case 2:
                value = "Unidad";
                break;
            case 3:
                value = mdQuantity;
                break;
            case 4:
                value = mdAuxSegregated;
                break;
            case 5:
                value = mdAuxStock;
                break;
            case 6:
                value = mdQuantity - mdAuxSegregated;
                break;
            case 7:
                value = mdAuxToSegregate;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch(col) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                break;
            case 7:
                mdAuxToSegregate = (double) value;
                break;
            default:
        }
    }
    
}
