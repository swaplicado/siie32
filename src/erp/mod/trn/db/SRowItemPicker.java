/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import java.util.ArrayList;
import sa.lib.grid.SGridRowCustom;

/**
 *
 * @author Isabel Servin
 */
public class SRowItemPicker extends SGridRowCustom {
    
    private Object moMainOption;
    private final ArrayList<Object> maValues;
    private boolean mbInv;
    private boolean mbRef;

    public SRowItemPicker(int[] pk) {
        super(pk, "", "");
        moMainOption = null;
        maValues = new ArrayList<>();
    }

    public void setMainOption(Object o) { moMainOption = o; }
    public void setInv(boolean b) { mbInv = b; }
    public void setRef(boolean b) { mbRef = b; }

    public Object getMainOption() { return moMainOption; }
    public boolean getInv() { return mbInv; }
    public boolean getRef() { return mbRef; }

    public ArrayList<Object> getValues() { return maValues; }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        if (col >= 0 && col < maValues.size()) {
            value = maValues.get(col);
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
