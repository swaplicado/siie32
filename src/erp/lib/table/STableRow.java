/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.lib.table;

import java.util.Vector;

/**
 *
 * @author Sergio Flores
 */
public abstract class STableRow implements sa.lib.grid.SGridRow, java.io.Serializable {

    protected boolean mbIsEditable;
    protected boolean mbIsSummary;
    protected int mnStyle;
    protected java.lang.Object moPrimaryKey;
    protected java.lang.Object moData;
    protected java.util.Vector<java.lang.Object> mvValues;

    protected int[] manRowPrimaryKey;
    protected String msRowCode;
    protected String msRowName;
    protected Boolean mbRowSystem;
    protected Boolean mbRowDeletable;
    protected Boolean mbRowEdited;

    public STableRow() {
        this(null, "", "");
    }

    public STableRow(int[] pk, String code, String name) {
        mbIsEditable = false;
        mbIsSummary = false;
        mnStyle = STableConstants.UNDEFINED;
        moPrimaryKey = null;
        moData = null;
        mvValues = new Vector<>();

        manRowPrimaryKey = pk;
        msRowCode = code;
        msRowName = name;
        mbRowSystem = false;
        mbRowDeletable = true;
        mbRowEdited = false;
    }

    public void setIsEditable(boolean b) { mbIsEditable = b; }
    public void setIsSummary(boolean b) { mbIsSummary = b; }
    public void setStyle(int n) { mnStyle = n; }
    public void setPrimaryKey(java.lang.Object o) { moPrimaryKey = o; }
    public void setData(java.lang.Object o) { moData = o; }

    public boolean getIsEditable() { return mbIsEditable; }
    public boolean getIsSummary() { return mbIsSummary; }
    public int getStyle() { return mnStyle; }
    public java.lang.Object getPrimaryKey() { return moPrimaryKey; }
    public java.lang.Object getData() { return moData; }
    public java.util.Vector<Object> getValues() { return mvValues; }

    public abstract void prepareTableRow();

    /*
     * Public methods
     */

    public void setRowPrimaryKey(int[] key) { manRowPrimaryKey = key; }
    public void setRowCode(String s) { msRowCode = s; }
    public void setRowName(String s) { msRowName = s; }
    public void setRowSystem(boolean b) { mbRowSystem = b; }
    public void setRowDeletable(boolean b) { mbRowDeletable = b; }

    public boolean isCellEditable(int col) {
        return false;
    }

    /*
     * Overriden methods
     */

    @Override
    public int[] getRowPrimaryKey() {
        return manRowPrimaryKey;
    }

    @Override
    public String getRowCode() {
        return msRowCode;
    }

    @Override
    public String getRowName() {
        return msRowName;
    }

    @Override
    public boolean isRowSystem() {
        return mbRowSystem;
    }

    @Override
    public boolean isRowDeletable() {
        return mbRowDeletable;
    }

    @Override
    public boolean isRowEdited() {
        return mbRowEdited;
    }

    @Override
    public void setRowEdited(boolean b) {
        mbRowEdited = b;
    }

    @Override
    public Object getRowValueAt(int col) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
