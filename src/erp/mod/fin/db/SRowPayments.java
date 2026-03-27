/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import java.io.Serializable;
import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Isabel Servín, Sergio Flores, Edwin Carmona
 */
public class SRowPayments implements SGridRow, Serializable {
    
    protected int idYear;
    protected int idDoc;
    protected int idBeneficiary;
    protected Date mtDateScheduled;
    protected int idPayment;
    protected String msBeneficiary;
    protected String msPayNum;
    protected String msDocNum;
    protected double mdAmount;
    protected String msCur;
    protected double mdExchangeRate;
    protected double mdAmountCurrencyToPay;
    protected String msCurToPay;
    protected boolean mbReceptionPayReq;
    protected int mnFuncArea;
    protected int mnFuncSubarea;
    protected boolean mbSelected = false;
    protected String msNotes;
    protected int mnInsallment;
    protected double mdDocBalancePrevAppCy;
    protected double mdDocBalanceUnpayAppCy;
    protected double mdDocBalancePrevCy;
    protected double mdDocBalanceUnpayCy;

    public void setIdYear(int d) { idYear = d; };
    public void setIdDoc(int d) { idDoc = d; };
    public void setIdBeneficiary(int d) { idBeneficiary = d; };
    public void setDateScheduled(Date t) { mtDateScheduled = t; };
    public void setIdPayment(int d) { idPayment = d; };
    public void setBeneficiary(String s) { msBeneficiary = s; };
    public void setPayNum(String s) { msPayNum = s; };
    public void setDocNum(String s) { msDocNum = s; };
    public void setAmount(double d) { mdAmount = d; };
    public void setCur(String s) { msCur = s; };
    public void setExchangeRate(double d) { mdExchangeRate = d; };
    public void setAmountCurrencyToPay(double d) { mdAmountCurrencyToPay = d; };
    public void setCurToPay(String s) { msCurToPay = s; };
    public void setReceptionPayReq(boolean b) { mbReceptionPayReq = b; };
    public void setFuncArea(int n) { mnFuncArea = n; };
    public void setFuncSubarea(int n) { mnFuncSubarea = n; };
    public void setNotes(String s) { msNotes = s; }
    public void setSelected(boolean b) { mbSelected = b; }
    public void setInstallment(int n) { mnInsallment = n; }
    public void setDocBalancePrevAppCy(double d) { mdDocBalancePrevAppCy = d; }
    public void setDocBalanceUnpayAppCy(double d) { mdDocBalanceUnpayAppCy = d; }
    public void setDocBalancePrevCy(double d) { mdDocBalancePrevCy = d; }
    public void setDocBalanceUnpayCy(double d) { mdDocBalanceUnpayCy = d; }
    
    public int getIdYear() { return idYear; }
    public int getIdDoc() { return idDoc; }
    public int getIdPayment() { return idPayment; }
    public Date getDateScheduled() { return mtDateScheduled; }
    public int getIdBeneficiary() { return idBeneficiary; }
    public String getBeneficiary() { return msBeneficiary; }
    public String getPayNum() { return msPayNum; }
    public String getDocNum() { return msDocNum; }
    public double getAmount() { return mdAmount; }
    public String getCur() { return msCur; }
    public double getExchangeRate() { return mdExchangeRate; }
    public double getAmountCurrencyToPay() { return mdAmountCurrencyToPay; }
    public String getCurToPay() { return msCurToPay; }
    public boolean getReceptionPayReq() { return mbReceptionPayReq; };
    public int getFuncArea() { return mnFuncArea; };
    public int getFuncSubarea() { return mnFuncSubarea; };
    public String getNotes() { return msNotes; }
    public boolean getIsSelected() { return mbSelected; }
    public int getInstallment() { return mnInsallment; }
    public double getDocBalancePrevAppCy() { return mdDocBalancePrevAppCy; }
    public double getDocBalanceUnpayAppCy() { return mdDocBalanceUnpayAppCy; }
    public double getDocBalancePrevCy() { return mdDocBalancePrevCy; }
    public double getDocBalanceUnpayCy() { return mdDocBalanceUnpayCy; }
    
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
            case 0:
                value = msBeneficiary;
                break;
            case 1:
                value = mtDateScheduled;
                break;
            case 2:
                value = msDocNum;
                break;
            case 3:
                value = msPayNum;
                break;
            case 4:
                value = mdAmount;
                break;
            case 5:
                value = msCur;
                break;
            case 6:
                value = mdExchangeRate;
                break;
            case 7:
                value = mdAmountCurrencyToPay;
                break;
            case 8:
                value = msCurToPay;
                break;
            case 9:
                value = mbSelected;
                break;
            case 10:
                value = msNotes;
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 9: mbSelected = (boolean) value; break;
        }
    }
}
