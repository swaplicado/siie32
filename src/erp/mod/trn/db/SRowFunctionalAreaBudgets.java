/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.cfg.db.SDbFunctionalArea;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Sergio Flores
 */
public class SRowFunctionalAreaBudgets implements SGridRow {
    
    public final static int COL_JAN = 1;
    
    private SDbFunctionalArea moFunctionalArea;
    private Double[] madBudgets; // 
    
    /**
     * Create row of monthly budgets for functinoal area.
     * @param functionalArea Functional area.
     * @param budgets 12 monthly budgets.
     * <code>null</code> means no budget set!, 0 means 0 budget, that is, no budget!
     */
    public SRowFunctionalAreaBudgets(final SDbFunctionalArea functionalArea, final Double[] budgets) {
        moFunctionalArea = functionalArea;
        madBudgets = budgets;
    }

    public SDbFunctionalArea getFunctionalArea() { return moFunctionalArea; }
    public Double[] getBudgets() { return madBudgets; }
    
    @Override
    public int[] getRowPrimaryKey() {
        return moFunctionalArea.getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return moFunctionalArea.getCode();
    }

    @Override
    public String getRowName() {
        return moFunctionalArea.getName();
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
        return true;
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0: // functional area name
                value = moFunctionalArea.getName();
                break;
            case COL_JAN: // Jan
            case 2: // Feb
            case 3: // Mar
            case 4: // Apr
            case 5: // May
            case 6: // Jun
            case 7: // Jul
            case 8: // Aug
            case 9: // Sep
            case 10: // Oct
            case 11: // Nov
            case 12: // Dec
                value = madBudgets[col - COL_JAN];
                break;
            default:
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 0: // functional area name
                break;
            case COL_JAN: // Jan
            case 2: // Feb
            case 3: // Mar
            case 4: // Apr
            case 5: // May
            case 6: // Jun
            case 7: // Jul
            case 8: // Aug
            case 9: // Sep
            case 10: // Oct
            case 11: // Nov
            case 12: // Dec
                madBudgets[col - COL_JAN] = (Double) value;
                break;
            default:
        }
    }
}
