/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Sergio Flores
 */
public class SRowFiscalChartOfAccounts implements SGridRow {

    public String AccountId;
    public String AccountCode;
    public String AccountName;
    public int Deep;
    public int Level;
    public String SpecialAccountName;
    public String FiscalAccountCode;
    public int BizPartnerId;
    public String BizPartnerFiscalId;
    public boolean IsEmployee;
    public boolean IsShareholder;
    public boolean IsRelatedParty;
    public int ItemId;
    public String ItemFiscalAccountCodeInc;
    public String ItemFiscalAccountCodeExp;

    public SRowFiscalChartOfAccounts() {
        AccountId = "";
        AccountCode = "";
        AccountName = "";
        Deep = 0;
        Level = 0;
        SpecialAccountName = "";
        FiscalAccountCode = "";
        BizPartnerId = 0;
        BizPartnerFiscalId = "";
        IsEmployee = false;
        IsShareholder = false;
        IsRelatedParty = false;
        ItemId = 0;
        ItemFiscalAccountCodeInc = "";
        ItemFiscalAccountCodeExp = "";
    }

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
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = AccountId;
                break;
            case 1:
                value = AccountCode;
                break;
            case 2:
                value = AccountName;
                break;
            case 3:
                value = Deep;
                break;
            case 4:
                value = Level;
                break;
            case 5:
                value = SpecialAccountName;
                break;
            case 6:
                value = FiscalAccountCode;
                break;
            case 7:
                value = BizPartnerId;
                break;
            case 8:
                value = BizPartnerFiscalId;
                break;
            case 9:
                value = IsEmployee;
                break;
            case 10:
                value = IsShareholder;
                break;
            case 11:
                value = IsRelatedParty;
                break;
            case 12:
                value = ItemId;
                break;
            case 13:
                value = ItemFiscalAccountCodeInc;
                break;
            case 14:
                value = ItemFiscalAccountCodeExp;
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
