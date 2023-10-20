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
 * @author Isabel Servin
 */
public class SRowMaterialRequestItemSupply implements SGridRow, Serializable {

    public int mnItemId;
    public int mnUnitId;
    public int mnIGenId;
    public String msItemKey;
    public String msItem;
    public double mdMin;
    public double mdPo;
    public double mdMax;
    public double mdStk;
    public double mdStkSeg;
    public double mdStkDisp;
    public double mdReq;
    public String msUnit;
    public double mdQty;
    
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnItemId, mnUnitId };
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
            case 0: value = msItemKey; break;
            case 1: value = msItem; break;
            case 2: value = mdMin; break;
            case 3: value = mdPo; break;
            case 4: value = mdMax; break;
            case 5: value = mdStk; break;
            case 6: value = mdStkSeg; break;
            case 7: value = mdStkDisp; break;
            case 8: value = mdReq; break;
            case 9: value = msUnit; break;
            case 10: value = mdQty; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 10: mdQty = (double) value; break;
        }
    }
}
