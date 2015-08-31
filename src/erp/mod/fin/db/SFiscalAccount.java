/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import erp.mod.SModSysConsts;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Sergio Flores
 */
public class SFiscalAccount implements SGridRow {

    protected int mnPkAccountId;
    protected String msAccountCode;
    protected String msAccountName;

    protected int mnFkFiscalAccountId;
    protected String msFiscalAccountName;
    protected boolean mbRowEdited;

    public SFiscalAccount(int accountId, String accountCode, String accountName) {
        mnPkAccountId = accountId;
        msAccountCode = accountCode;
        msAccountName = accountName;

        setFiscalAccount(SModSysConsts.FINS_FISCAL_ACC_NA, SFiscalUtils.createFiscalAccountNameNotApplicable());
        mbRowEdited = false;
    }

    public void setFkFiscalAccountId(int n) { mnFkFiscalAccountId = n; }
    public void setFiscalAccountName(String s) { msFiscalAccountName = s; }

    public void setFiscalAccount(final int accountId, final String name) {
        mnFkFiscalAccountId = accountId;
        msFiscalAccountName = name;
    }

    public int getPkAccountId() { return mnPkAccountId; }
    public String getAccountCode() { return msAccountCode; }
    public String getAccountName() { return msAccountName; }

    public int getFkFiscalAccountId() { return mnFkFiscalAccountId; }
    public String getFiscalAccountName() { return msFiscalAccountName; }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkAccountId };
    }

    @Override
    public String getRowCode() {
        return msAccountCode;
    }

    @Override
    public String getRowName() {
        return msAccountName;
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
        return mbRowEdited;
    }

    @Override
    public void setRowEdited(boolean edited) {
        mbRowEdited = edited;
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = msAccountCode;
                break;
            case 1:
                value = msAccountName;
                break;
            case 2:
                value = msFiscalAccountName;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
