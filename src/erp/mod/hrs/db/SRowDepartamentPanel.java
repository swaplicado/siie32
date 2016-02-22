/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.hrs.db;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Juan Barajas
 */
public class SRowDepartamentPanel implements SGridRow {

    protected int mnDepartamentId;
    protected String mnDepartamentCode;
    protected String msDepartamentName;
    protected boolean mbIsSelected;

    public SRowDepartamentPanel(int departamentId) {
        mnDepartamentId = departamentId;
        mnDepartamentCode = "";
        msDepartamentName = "";
        mbIsSelected = false;
    }

    public void setDepartamentId(int n) { mnDepartamentId = n; }
    public void setDepartamentCode(String n) { mnDepartamentCode = n; }
    public void setDepartamentName(String n) { msDepartamentName = n; }
    public void setIsSelected(boolean d) { mbIsSelected = d; }

    public int getDepartamentId() { return mnDepartamentId; }
    public String getDepartamentCode() { return mnDepartamentCode; }
    public String getDepartamentName() { return msDepartamentName; }
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
                mbIsSelected = (boolean) value;
                break;
            default:
                break;
        }
    }
}
