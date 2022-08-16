package erp.mod.trn.db;

import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Sergio Flores
 */
public class SRowIdentifiedCostLot implements SGridRow {

    // item & unit:
    
    public String ItemCode;
    public String ItemName;
    public String UnitCode;
    public String UnitName;
    
    // lot:

    public int LotItemId;
    public int LotUnitId;
    public int LotLotId;
    public String LotLot;
    public Date LotExpiration;
    public boolean LotBlocked;
    public boolean LotDeleted;

    // lot unit cost:

    public double LotCostUnit;
    public String CostUnitType;
    public String CalculationIssue;

    public SRowIdentifiedCostLot(SDbIdentifiedCostCalculation.Supply supply) {
        ItemCode = supply.SupplyDocRowItemCode;
        ItemName = supply.SupplyDocRowItemName;
        UnitCode = supply.SupplyDocRowUnitCode;
        UnitName = supply.SupplyDocRowUnitName;
        
        LotItemId = supply.LotItemId;
        LotUnitId = supply.LotUnitId;
        LotLotId = supply.LotLotId;
        LotLot = supply.LotLot;
        LotExpiration = supply.LotExpiration;
        LotBlocked = supply.LotBlocked;
        LotDeleted = supply.LotDeleted;

        LotCostUnit = supply.LotCostUnit;
        CostUnitType = supply.CostUnitType;
        CalculationIssue = supply.CalculationIssue;
    }
    
    public int[] getLotKey() {
        return new int[] { LotItemId, LotUnitId, LotLotId };
    }
    
    @Override
    public int[] getRowPrimaryKey() {
        return getLotKey();
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

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = ItemCode;
                break;
            case 1:
                value = ItemName;
                break;
            case 2:
                value = UnitCode;
                break;
            case 3:
                value = LotLot;
                break;
            case 4:
                value = LotExpiration;
                break;
            case 5:
                value = LotCostUnit;
                break;
            case 6:
                value = CostUnitType;
                break;
            case 7:
                value = CalculationIssue;
                break;
            default:
                // do nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
