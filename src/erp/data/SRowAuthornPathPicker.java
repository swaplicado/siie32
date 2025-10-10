/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.data;

import java.io.Serializable;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Isabel Servín
 */
public class SRowAuthornPathPicker implements SGridRow, Serializable {
    
    protected int mnPkId;
    protected String msName;
    protected String msDescription;
    
    public void setPkId(int n) { mnPkId = n; }
    public void setName(String s) { msName = s; }
    public void setDescription(String s) { msDescription = s; }
    
    public int getPkId() { return mnPkId; }
    public String getName() { return msName; }
    public String getDescription() { return msDescription; }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkId };
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
            case 0: value = msName; break;
            case 1: value = msDescription; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) { }
}
