package erp.mod.trn.db;

import java.util.Date;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SRowIdentifiedCostDpsEntrySupplyLot extends SRowIdentifiedCostDpsEntry {

    // external supplied document:

    public int SupplyDocId;
    public String SupplyDocSeries;
    public String SupplyDocNumber;
    public Date SupplyDocDate;

    // external supplied document entry:

    public int SupplyDocRowId;
    public double SupplyDocRowQuantity;
    public String SupplyDocRowItemCode;
    public String SupplyDocRowItemName;
    public String SupplyDocRowUnitCode;
    public String SupplyDocRowUnitName;

    // external supply movement:

    public int SupplyMovementId;
    public String SupplyMovementFolio;
    public Date SupplyMovementDate;

    // external supply movement row:

    public int SupplyMovementRowId;
    public double SupplyMovementRowQuantity;

    // external supply movement row lot:

    public int SupplyMovementRowLotId;
    public double SupplyMovementRowLotQuantity;
    public int SupplyLotId;
    public String SupplyLotLot;
    public Date SupplyLotExpiration;

    // lot:

    public int LotItemId;
    public int LotUnitId;
    public int LotLotId;
    public String LotLot;
    public Date LotExpiration;
    public boolean LotBlocked;
    public boolean LotDeleted;

    // lot unit cost:

    public double LotUnitCost;
    public String CostUnitType;
    public String CalculationIssue;

    public SRowIdentifiedCostDpsEntrySupplyLot(SDbIdentifiedCostCalculation.Supply supply) {
        super(supply);
        
        SupplyFactor = supply.SupplyFactor;

        SupplyDocId = supply.SupplyDocId;
        SupplyDocSeries = supply.SupplyDocSeries;
        SupplyDocNumber = supply.SupplyDocNumber;
        SupplyDocDate = supply.SupplyDocDate;

        SupplyDocRowId = supply.SupplyDocRowId;
        SupplyDocRowQuantity = supply.SupplyDocRowQuantity;
        SupplyDocRowItemCode = supply.SupplyDocRowItemCode;
        SupplyDocRowItemName = supply.SupplyDocRowItemName;
        SupplyDocRowUnitCode = supply.SupplyDocRowUnitCode;
        SupplyDocRowUnitName = supply.SupplyDocRowUnitName;

        SupplyMovementId = supply.SupplyMovementId;
        SupplyMovementFolio = supply.SupplyMovementFolio;
        SupplyMovementDate = supply.SupplyMovementDate;

        SupplyMovementRowId = supply.SupplyMovementRowId;
        SupplyMovementRowQuantity = supply.SupplyDocRowQuantity;

        SupplyMovementRowLotId = supply.SupplyMovementRowLotId;
        SupplyMovementRowLotQuantity = supply.SupplyMovementRowLotQuantity;
        SupplyLotId = supply.SupplyLotId;
        SupplyLotLot = supply.SupplyLotLot;
        SupplyLotExpiration = supply.SupplyLotExpiration;

        LotItemId = supply.LotItemId;
        LotUnitId = supply.LotUnitId;
        LotLotId = supply.LotLotId;
        LotLot = supply.LotLot;
        LotExpiration = supply.LotExpiration;
        LotBlocked = supply.LotBlocked;
        LotDeleted = supply.LotDeleted;

        LotUnitCost = supply.LotCostUnit;
        CostUnitType = supply.CostUnitType;
        CalculationIssue = supply.CalculationIssue;
    }
    
    public double getSupplyMovementRowLotSubtotal() {
        return SLibUtils.roundAmount(((DocEntryQuantity == 0.0 ? 0.0 : DocEntrySubtotal / DocEntryQuantity) * SupplyMovementRowLotQuantity) * SupplyFactor);
    }
    
    public double getSupplyMovementRowLotCost() {
        return SLibUtils.roundAmount(LotUnitCost * SupplyMovementRowLotQuantity * SupplyFactor);
    }
    
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { SupplyMovementRowLotId };
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
                value = DocTypeCode;
                break;
            case 1:
                value = getDocNumber();
                break;
            case 2:
                value = DocDate;
                break;
            case 3:
                value = SupplyDocRowItemCode;
                break;
            case 4:
                value = SupplyDocRowItemName;
                break;
            case 5:
                value = SupplyMovementRowLotQuantity;
                break;
            case 6:
                value = SupplyDocRowUnitCode;
                break;
            case 7:
                value = getSupplyMovementRowLotSubtotal();
                break;
            case 8:
                value = getSupplyMovementRowLotCost();
                break;
            case 9:
                value = getSupplyMovementRowLotSubtotal() - getSupplyMovementRowLotCost(); // contribution margin
                break;
            case 10:
                value = LotLot;
                break;
            case 11:
                value = LotExpiration;
                break;
            case 12:
                value = LotUnitCost;
                break;
            case 13:
                value = CostUnitType;
                break;
            case 14:
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
