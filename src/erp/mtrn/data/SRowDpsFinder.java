/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Sergio Flores
 */
public class SRowDpsFinder implements SGridRow {
    
    public String DpsTypeCode;
    public String DpsNumber;
    public String DpsNumberRef;
    public Date DpsDate;
    public String DpsStatusName;
    public String CompanyBranchCode;
    public String BizPartnerName;
    public double DpsTotal;
    public String DpsCurrencyCode;
    public String JournalVoucher;
    public String CfdTypeName;
    public String CfdUuid;
    public String CfdStatusName;
    public boolean DpsDeleted;
    public String UserNew;
    public String UserEdit;
    public String UserDelete;
    public Date TsNew;
    public Date TsEdit;
    public Date TsDelete;
    
    public int[] DpsKey;
    public int BizPartnerId;
    public Object[] JournalVoucherKey;

    @Override
    public int[] getRowPrimaryKey() {
        return DpsKey;
    }

    @Override
    public String getRowCode() {
        return DpsNumber;
    }

    @Override
    public String getRowName() {
        return DpsNumber;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = DpsTypeCode;
                break;
            case 1:
                value = DpsNumber;
                break;
            case 2:
                value = DpsNumberRef;
                break;
            case 3:
                value = DpsDate;
                break;
            case 4:
                value = DpsStatusName;
                break;
            case 5:
                value = CompanyBranchCode;
                break;
            case 6:
                value = BizPartnerName;
                break;
            case 7:
                value = DpsTotal;
                break;
            case 8:
                value = DpsCurrencyCode;
                break;
            case 9:
                value = JournalVoucher;
                break;
            case 10:
                value = CfdTypeName;
                break;
            case 11:
                value = CfdUuid;
                break;
            case 12:
                value = CfdStatusName;
                break;
            case 13:
                value = DpsDeleted;
                break;
            case 14:
                value = UserNew;
                break;
            case 15:
                value = TsNew;
                break;
            case 16:
                value = UserEdit;
                break;
            case 17:
                value = TsEdit;
                break;
            case 18:
                value = UserDelete;
                break;
            case 19:
                value = TsDelete;
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
