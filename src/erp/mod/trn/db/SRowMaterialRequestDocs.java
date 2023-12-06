/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import java.io.Serializable;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Edwin Carmona
 */
public class SRowMaterialRequestDocs implements SGridRow, Serializable {

    public int mnMatReqId;
    public int mnMatReqEtyId;
    public int mnItemId;
    public int mnUnitId;
    public String msItemKey;
    public String msItem;
    public String msPartNumber;
    public String msUnitSymbol;
    public String msCot;
    public String msPed;
    public String msFact;
    
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnMatReqId, mnMatReqEtyId };
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
            case 0: value = mnMatReqEtyId; break;
            case 1: value = msItemKey; break;
            case 2: value = msItem; break;
            case 3: value = msPartNumber; break;
            case 4: value = msUnitSymbol; break;
            case 5: value = msCot; break;
            case 6: value = msPed; break;
            case 7: value = msFact; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        
    }
}
