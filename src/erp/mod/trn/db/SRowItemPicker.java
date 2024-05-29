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
    private boolean mbSal;
    private boolean mbAss;
    private boolean mbPur;
    private boolean mbExp;

    public SRowItemPicker(int[] pk) {
        super(pk, "", "");
        moMainOption = null;
        maValues = new ArrayList<>();
    }

    public void setMainOption(Object o) { moMainOption = o; }
    public void setInv(boolean b) { mbInv = b; }
    public void setSal(boolean b) { mbSal = b; }
    public void setAss(boolean b) { mbAss = b; }
    public void setPur(boolean b) { mbPur = b; }
    public void setExp(boolean b) { mbExp = b; }

    public Object getMainOption() { return moMainOption; }
    public boolean getInv() { return mbInv; }
    public boolean getSal() { return mbSal; }
    public boolean getAss() { return mbAss; }
    public boolean getPur() { return mbPur; }
    public boolean getExp() { return mbExp; }

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
