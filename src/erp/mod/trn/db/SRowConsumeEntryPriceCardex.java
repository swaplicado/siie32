/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.trn.db;

import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Juan Barajas
 */
public class SRowConsumeEntryPriceCardex implements SGridRow {

    protected String msTypeDoc;
    protected String msNumber;
    protected String msReference;
    protected Date mtDateDoc;
    protected String msCompanyBranch;
    protected String msItem;
    protected String msItemKey;
    protected double mdQuantity;
    protected double mdPriceUnitary;
    protected double mdAmount;
    protected String msCurrency;
    protected String msUserInsert;
    protected Date mtDateUserInsert;
    protected String msUserUpdate;
    protected Date mtDateUserUpdate;

    public SRowConsumeEntryPriceCardex() {
        msTypeDoc = "";
        msNumber = "";
        msReference = "";
        mtDateDoc = null;
        msCompanyBranch = "";
        msItem = "";
        msItemKey = "";
        mdQuantity = 0;
        mdPriceUnitary = 0;
        mdAmount = 0;
        msCurrency = "";
        msUserInsert = "";
        mtDateUserInsert = null;
        msUserUpdate = "";
        mtDateUserUpdate = null;
    }
    
    public void setTypeDoc(String s) { msTypeDoc = s; }
    public void setNumber(String s) { msNumber = s; }
    public void setReference(String s) { msReference = s; }
    public void setDateDoc(Date t) { mtDateDoc = t; }
    public void setCompanyBranch(String s) { msCompanyBranch = s; }
    public void setItem(String s) { msItem = s; }
    public void setItemKey(String s) { msItemKey = s; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setPriceUnitary(double d) { mdPriceUnitary = d; }
    public void setAmount(double d) { mdAmount = d; }
    public void setCurrency(String s) { msCurrency = s; }
    public void setUserInsert(String s) { msUserInsert = s; }
    public void setDateUserInsert(Date t) { mtDateUserInsert = t; }
    public void setUserUpdate(String s) { msUserUpdate = s; }
    public void setDateUserUpdate(Date t) { mtDateUserUpdate = t; }

    public String getPayrollType() { return msTypeDoc; }
    public String getNumber() { return msNumber; }
    public String getReference() { return msReference; }
    public Date getDateStart() { return mtDateDoc; }
    public String getCompanyBranch() { return msCompanyBranch; }
    public String getItem() { return msItem; }
    public String getItemKey() { return msItemKey; }
    public double getQuantity() { return mdQuantity; }
    public double getPriceUnitary() { return mdPriceUnitary; }
    public double getAmount() { return mdAmount; }
    public String getCurrency() { return msCurrency; }
    public String getUserInsert() { return msUserInsert; }
    public Date getDateUserInsert() { return mtDateUserInsert; }
    public String getUserUpdate() { return msUserUpdate; }
    public Date getDateUserUpdate() { return mtDateUserUpdate; }

    @Override
    public int[] getRowPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowEdited(final boolean edited) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;
        
        switch(row) {
            case 0:
                value = msTypeDoc;
                break;
            case 1:
                value = msNumber;
                break;
            case 2:
                value = msReference;
                break;
            case 3:
                value = mtDateDoc;
                break;
            case 4:
                value = msCompanyBranch;
                break;
            case 5:
                value = mdQuantity;
                break;
            case 6:
                value = mdPriceUnitary;
                break;
            case 7:
                value = mdAmount;
                break;
            case 8:
                value = msCurrency;
                break;
            case 9:
                value = msUserInsert;
                break;
            case 10:
                value = mtDateUserInsert;
                break;
            case 11:
                value = msUserUpdate;
                break;
            case 12:
                value = mtDateUserUpdate;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object o, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
