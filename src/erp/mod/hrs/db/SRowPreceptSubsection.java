/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Sergio Flores
 */
public class SRowPreceptSubsection implements SGridRow {
    
    protected int[] maPreceptSubsectionKey;
    protected String msPerceptCode;
    protected String msPreceptSectionCode;
    protected String msPreceptSubsectionCode;
    protected String msPreceptSubsectionName;
    protected int mnPositionGlobal;
    protected int mnPosition;
    
    public SRowPreceptSubsection() {
        this(null, "", "", "", "", 0, 0);
    }

    public SRowPreceptSubsection(final int[] subsectionKey, final String perceptCode, final String preceptSectionCode, final String preceptSubsectionCode, final String preceptSubsectionName, final int positonGeneral, final int positon) {
        maPreceptSubsectionKey = subsectionKey;
        msPerceptCode = perceptCode;
        msPreceptSectionCode = preceptSectionCode;
        msPreceptSubsectionCode = preceptSubsectionCode;
        msPreceptSubsectionName = preceptSubsectionName;
        mnPositionGlobal = positonGeneral;
        mnPosition = positon;
    }

    public void getPreceptSubsectionKey(int[] key) { maPreceptSubsectionKey = key; }
    public void setPerceptCode(String s) { msPerceptCode = s; }
    public void setPreceptSectionCode(String s) { msPreceptSectionCode = s; }
    public void setPreceptSubsectionCode(String s) { msPreceptSubsectionCode = s; }
    public void setPreceptSubsectionName(String s) { msPreceptSubsectionName = s; }
    public void setPositionGlobal(int n) { mnPositionGlobal = n; }
    public void setPosition(int n) { mnPosition = n; }
    
    public int[] getPreceptSubsectionKey() { return maPreceptSubsectionKey; }
    public String getPerceptCode() { return msPerceptCode; }
    public String getPreceptSectionCode() { return msPreceptSectionCode; }
    public String getPreceptSubsectionCode() { return msPreceptSubsectionCode; }
    public String getPreceptSubsectionName() { return msPreceptSubsectionName; }
    public int getPositionGlobal() { return mnPositionGlobal; }
    public int getPosition() { return mnPosition; }
    
    @Override
    public int[] getRowPrimaryKey() {
        return getPreceptSubsectionKey();
    }

    @Override
    public String getRowCode() {
        return getPreceptSubsectionCode();
    }

    @Override
    public String getRowName() {
        return getPreceptSubsectionName();
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {
        
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = msPerceptCode;
                break;
            case 1:
                value = msPreceptSectionCode;
                break;
            case 2:
                value = msPreceptSubsectionCode;
                break;
            case 3:
                value = msPreceptSubsectionName;
                break;
            case 4:
                value = mnPosition;
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        
    }
}
