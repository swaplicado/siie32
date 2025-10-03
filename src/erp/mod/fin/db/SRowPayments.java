/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import java.io.Serializable;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Isabel Servín
 */
public class SRowPayments implements SGridRow, Serializable {
    
    protected int idYear;
    protected int idDoc;
    protected int idBeneficiary;
    protected int idPayment;
    protected String msBeneficiary;
    protected String msPayNum;
    protected String msDocNum;
    protected double mdAmount;
    protected String msCur;
    protected boolean mbReceptionPayReq;
    protected int mnFuncArea;
    protected int mnFuncSubarea;
    protected boolean mbSelected = false;

    public void setIdYear(int d) { idYear = d; };
    public void setIdDoc(int d) { idDoc = d; };
    public void setIdBeneficiary(int d) { idBeneficiary = d; };
    public void setIdPayment(int d) { idPayment = d; };
    public void setBeneficiary(String s) { msBeneficiary = s; };
    public void setPayNum(String s) { msPayNum = s; };
    public void setDocNum(String s) { msDocNum = s; };
    public void setAmount(double d) { mdAmount = d; };
    public void setCur(String s) { msCur = s; };
    public void setReceptionPayReq(boolean b) { mbReceptionPayReq = b; };
    public void setFuncArea(int n) { mnFuncArea = n; };
    public void setFuncSubarea(int n) { mnFuncSubarea = n; };
    public void setSelected(boolean b) { mbSelected = b; }
    
    public int getIdYear() { return idYear; }
    public int getIdDoc() { return idDoc; }
    public int getIdPayment() { return idPayment; }
    public int getIdBeneficiary() { return idBeneficiary; }
    public String getBeneficiary() { return msBeneficiary; }
    public String getPayNum() { return msPayNum; }
    public String getDocNum() { return msDocNum; }
    public double getAmount() { return mdAmount; }
    public String getCur() { return msCur; }
    public boolean getReceptionPayReq() { return mbReceptionPayReq; };
    public int getFuncArea() { return mnFuncArea; };
    public int getFuncSubarea() { return mnFuncSubarea; };
    public boolean getIsSelected() { return mbSelected; }
    
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { idYear, idDoc };
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
            case 0: value = msBeneficiary; break;
            case 1: value = msPayNum; break;
            case 2: value = msDocNum; break;
            case 3: value = mdAmount; break;
            case 4: value = msCur; break;
            case 5: value = mbSelected; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 5: mbSelected = (boolean) value; break;
        }
    }
}
