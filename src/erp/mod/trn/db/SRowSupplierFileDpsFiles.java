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
 * @author Isabel Servín
 */
public class SRowSupplierFileDpsFiles implements  SGridRow, Serializable {
    
    protected int mnPos;
    protected String msType;
    protected String msName;
    protected String msBizPartner;
    protected String msNumber;
//    protected double mdTotalDoc;
//    protected String msCurDoc;
//    protected double mdExR;
    protected double mdTotal;
    protected String msCur;
    protected boolean mbExtemp;

    public void setPos(int n) { mnPos = n; }
    public void setType(String s) { msType = s; }
    public void setName(String s) { msName = s; }
    public void setBizPartner(String s) { msBizPartner = s; }
    public void setNumber(String s) { msNumber = s; }
//    public void setTotalDoc(double d) { mdTotalDoc = d; }
//    public void setCurDoc(String s) { msCurDoc = s; }
//    public void setExR(double d) { mdExR = d; }
    public void setTotal(double d) { mdTotal = d; }
    public void setCur(String s) { msCur = s; }
    public void setIsExtemp(boolean b) { mbExtemp = b; }
    
    public int getPos() { return mnPos; }
    public String getType() { return msType; }
    public String getName() { return msName; }
    public String getBizPartner() { return msBizPartner; }
    public String getNumber() { return msNumber; }
    public double getTotal() { return mdTotal; }
    public String getCur() { return msCur; }
    public boolean getIsExtemp() { return mbExtemp; }
    
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
            case 0: value = mnPos; break;
            case 1: value = msType; break;
            case 2: value = msName; break;
            case 3: value = msBizPartner; break;
            case 4: value = msNumber; break;
            case 5: value = mdTotal; break;
            case 6: value = msCur; break;
            case 7: value = mbExtemp; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
       
    }
}
