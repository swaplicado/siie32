/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.db;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Isabel Servín
 */
public class SRowBillOfLading implements SGridRow{

    public int mnItemId;
    public String msItem;
    public double mdQuantity;
    public String msUnit;
    
    public void setItemId(int n) { mnItemId = n; }
    public void setItem(String s) { msItem = s; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setUnit(String s) { msUnit = s; }
    
    public int getItemId() { return mnItemId; }
    public String getItem() { return msItem; }
    public double getQuantity() { return mdQuantity; }
    public String getUnit() { return msUnit; }
    
    void updateRowCharge(SDbBolMerchandiseQuantity qty) {
        mdQuantity += qty.mdQuantity;
    }
    
    void updateRowDischarge(SDbBolMerchandiseQuantity qty) {
        mdQuantity -= qty.mdQuantity;
    }
    
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnItemId };
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
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return isRowEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        setRowEdited(edited);
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;
        
        switch(row) {
            case 0: 
                value = msItem;
                break;
            case 1:
                value = mdQuantity;
                break;
            case 2:
                value = msUnit;
                break;
            default:
        }
        return value;
    }

    @Override
    public void setRowValueAt(Object o, int row) {
        switch(row) {
            case 0: 
            case 1:
            case 2:
            default:
        }
    }

    
}
