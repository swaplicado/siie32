/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Isabel Servín
 */
public class SRowDocumentAuthornComments implements SGridRow {

    private Date mtDateMov;
    private String msNum;
    private String msComments;
    private String msUser;
    private boolean mbAuthorn;
    private boolean mbReject;
    private boolean mbDeleted;
    
    public void setDateMov(Date t) { mtDateMov = t; }
    public void setNum(String s) { msNum = s; }
    public void setComments(String s) { msComments = s; }
    public void setUser(String s) { msUser = s; }
    public void setAuthorn(boolean b) { mbAuthorn = b; }
    public void setReject(boolean b) { mbReject = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] {};
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
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {
    
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0: value = mtDateMov; break;
            case 1: value = msNum; break;
            case 2: value = msComments; break;
            case 3: value = msUser; break;
            case 4: value = mbAuthorn; break;
            case 5: value = mbReject; break;
            case 6: value = mbDeleted; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
