/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Sergio Flores
 */
public class SRowDocument implements SGridRow, Comparable<SRowDocument> {
    
    public int ExternalDocumentId;
    public int BizPartnerId;
    public String BizPartner;
    public String NumberSeries;
    public String Number;
    public Date Date;
    public int ReferenceType;
    public String Reference;
    public String Description;
    public int FunctionalSubAreaId;
    public String FunctionalSubArea;
    public String FiscalUseCode;
    public double Total;
    public int CurrencyId;
    public String CurrencyCode;
    public Date RequiredPaymentDate;
    public double RequiredPaymentPct;
    public int StatusId;
    public String Status;
    public boolean Download;
    
    public Reference[] References;
    
    public SRowDocument() {
        
    }
    
    public String getFolio() {
        return (NumberSeries.isEmpty() ? "" : NumberSeries + "-") + Number;
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = BizPartner;
                break;
            case 1:
                value = getFolio();
                break;
            case 2:
                value = Date;
                break;
            case 3:
                value = Reference;
                break;
            case 4:
                value = Description;
                break;
            case 5:
                value = Total;
                break;
            case 6:
                value = CurrencyCode;
                break;
            case 7:
                value = Download;
                break;
            case 8:
                value = Status;
                break;
            case 9:
                value = FunctionalSubArea;
                break;
            case 10:
                value = FiscalUseCode;
                break;
            case 11:
                value = RequiredPaymentDate;
                break;
            case 12:
                value = RequiredPaymentPct;
                break;
            case 13:
                value = ExternalDocumentId;
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 7:
                Download = (Boolean) value;
                break;
            default:
                // nothing
        }
    }
    
    @Override
    public String toString() {
        return BizPartner + "; " + getFolio() + "; $" + SLibUtils.getDecimalFormatAmount().format(Total) + " " + CurrencyCode + "; ID" + ExternalDocumentId;
    }

    @Override
    public int compareTo(SRowDocument o) {
        return this.toString().compareTo(o.toString());
    }

    @Override
    public int[] getRowPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public class Reference {
        
        public int ReferenceType;
        public String Reference;
    }
}
