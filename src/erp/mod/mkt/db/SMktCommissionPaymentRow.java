/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.mkt.db;

import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Néstor Ávalos
 */
public class SMktCommissionPaymentRow implements SGridRow {

    protected int mnPkPaymentId;
    protected int mnNumber;
    protected Date mtDate;
    protected double mdPayment_r;
    protected double mdRefund_r;
    protected double mdTotal_r;
    protected String msSalesAgent;
    protected String msCurrencyKey;

    public SMktCommissionPaymentRow() {
        mnPkPaymentId = 0;
        mnNumber = 0;
        mtDate = null;
        mdPayment_r = 0;
        mdRefund_r = 0;
        mdTotal_r = 0;
        msSalesAgent = "";
        msCurrencyKey = "";
    }

    public void setPkPaymentId(int n) { mnPkPaymentId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setPayment_r(double d) { mdPayment_r = d; }
    public void setRefund_r(double d) { mdRefund_r = d; }
    public void setTotal_r(double d) {mdTotal_r = d; }
    public void setSalesAgent(String s) { msSalesAgent = s; }
    public void setCurrencyKey(String s) { msCurrencyKey = s; }

    public int getPkPaymentId() { return mnPkPaymentId; }
    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public double getPayment_r() { return mdPayment_r; }
    public double getRefund_r() { return mdRefund_r; }
    public double getTotal_r() {return mdTotal_r; }
    public String getSalesAgent() { return msSalesAgent; }
    public String getCurrencyKey() { return msCurrencyKey; }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkPaymentId };
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
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = msSalesAgent;
                break;
            case 1:
                value = mnNumber;
                break;
            case 2:
                value = mtDate;
                break;
            case 3:
                value = mdPayment_r;
                break;
            case 4:
                value = mdRefund_r;
                break;
            case 5:
                value = mdTotal_r;
                break;
            case 6:
                value = msCurrencyKey;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch(row) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            default:
        }
    }
}
