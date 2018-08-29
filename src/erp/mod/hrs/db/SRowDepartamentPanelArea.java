/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.hrs.db;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Juan Barajas, Claudio Peña
 */
public class SRowDepartamentPanelArea implements SGridRow {

    protected int mnDepartamentId;
    protected String mnDepartamentCode;
    protected String msDepartamentName;
    protected int mnNumNom;

    protected boolean mbIsSelected;

    public SRowDepartamentPanelArea(int departamentId) {
        mnDepartamentId = departamentId;
        mnDepartamentCode = "";
        msDepartamentName = "";
        mnNumNom = 0;
        mbIsSelected = false;
    }

    public void setDepartamentId(int n) { mnDepartamentId = n; }
    public void setDepartamentCode(String n) { mnDepartamentCode = n; }
    public void setDepartamentName(String n) { msDepartamentName = n; }
    public void setNumNom(int n) { mnNumNom = n; }
    public void setIsSelected(boolean d) { mbIsSelected = d; }

    public int getDepartamentId() { return mnDepartamentId; }
    public String getDepartamentCode() { return mnDepartamentCode; }
    public String getDepartamentName() { return msDepartamentName; }
    public int getNumNom() { return mnNumNom; }
    public boolean isSelected() { return mbIsSelected; }

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
                value = mnDepartamentCode;
                break;
            case 1:
                value = msDepartamentName;
                break;
            case 2:
                value = mnNumNom;
                break;
            case 3:
                value = mbIsSelected;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch (row) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:                
                mbIsSelected = (boolean) value;
                break;
            default:
                break;
        }
    }
}
