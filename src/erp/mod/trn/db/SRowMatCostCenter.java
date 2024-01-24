/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mfin.data.SDataCostCenter;
import java.io.Serializable;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SRowMatCostCenter implements SGridRow, Serializable {
    
    protected SDataCostCenter moDataCostCenter = null;
    protected boolean mbSelected = false;

    public void setSelected(boolean b) { mbSelected = b; }
    
    public boolean getIsSelected() { return mbSelected; }
    
    public void readDataCostCenter(String id, SGuiSession session) {
        moDataCostCenter = new SDataCostCenter();
        moDataCostCenter.read(new String[] { id }, session.getStatement());
    } 
    
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { moDataCostCenter.getPkCostCenterId() };
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return moDataCostCenter.getCostCenter();
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
            case 0: value = moDataCostCenter.getPkCostCenterIdXXX(); break;
            case 1: value = moDataCostCenter.getCostCenter(); break;
            case 2: value = mbSelected; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 2: mbSelected = (boolean) value; break;
        }
    }
}
