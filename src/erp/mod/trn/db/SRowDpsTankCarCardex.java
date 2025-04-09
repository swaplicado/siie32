/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Isabel Servín
 */
public class SRowDpsTankCarCardex implements SGridRow {
    
    public String msDocType;
    public String msNumber;
    public String msReference;
    public Date mtDate;
    public String msBizPartner;
    
    public SRowDpsTankCarCardex(String docType, String number, String reference, Date date, String bizPartner) {
        msDocType = docType;
        msNumber = number;
        msReference = reference;
        mtDate = date;
        msBizPartner = bizPartner;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return null;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0: value = msDocType; break;
            case 1: value = msNumber; break;
            case 2: value = msReference; break;
            case 3: value = mtDate; break;
            case 4: value = msBizPartner; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
