/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import java.util.Date;
import sa.lib.grid.SGridRowCustom;

/**
 *
 * @author Sergio Flores
 */
public class SRowDocument extends SGridRowCustom {
    
    public int ExternalDocumentId;
    public int BizPartnerId;
    public String BizPartner;
    public String NumberSeries;
    public String Number;
    public Date Date;
    public String Reference;
    public String Description;
    public int FunctionalSubAreaId;
    public String FunctionalSubArea;
    public String CfdiUse;
    public double Total;
    public int CurrencyId;
    public String CurrencyCode;
    public Date RequiredPaymentDate;
    public double RequiredPaymentPct;
    public int StatusId;
    public String Status;
    public boolean Download;
    
    public SRowDocument(int[] pk, String code, String name) {
        super(pk, code, name);
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = BizPartner;
                break;
            case 1:
                value = (NumberSeries.isEmpty() ? "" : NumberSeries + "-") + Number;
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
                value = FunctionalSubArea;
                break;
            case 6:
                value = CfdiUse;
                break;
            case 7:
                value = Total;
                break;
            case 8:
                value = CurrencyCode;
                break;
            case 9:
                value = RequiredPaymentDate;
                break;
            case 10:
                value = RequiredPaymentPct;
                break;
            case 11:
                value = Status;
                break;
            case 12:
                value = Download;
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 12:
                Download = (Boolean) value;
                break;
            default:
                // nothing
        }
    }
}
