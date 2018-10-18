/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Sergio Flores, Edwin Carmona
 */
public class STrnStockMove implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkLotId;
    protected int mnPkCompanyBranchId;
    protected int mnPkWarehouseId;
    protected ArrayList<Integer> mlPkWarehouseIds;
    protected double mdQuantity;
    protected double mdValue;
    protected String msSerialNumber;

    protected int mnAuxRowNumber;
    protected String msAuxLot;
    protected Date mtAuxLotDateExpiration;
    protected boolean mbAuxIsLotBlocked;
    protected boolean mbAuxIsMoveBeingDeleted;
    protected int mnSegregationId;
    protected int[] manSegregationReference; //reference of the origin of segregation. PO = [ productionOrderId, productionOrderYear ].
    protected int mnSegregationType;
    protected boolean mbIsCurrentSegregationExcluded;
    protected int mnWarehouseType;
    
     /**
     * Creates stock move.
     */
    public STrnStockMove() {}

    /**
     * Creates stock move.
     * @param moveKey Stock move primary key (year, item, unit, lot, company branch, warehouse).
     * @param quantity Stock move quantity.
     */
    public STrnStockMove(int[] moveKey, double quantity) {
        this(moveKey, quantity, 0, "");
    }

    /**
     * Creates stock move.
     * @param moveKey Stock move primary key (year, item, unit, lot, company branch, warehouse).
     * @param quantity Stock move quantity.
     * @param serialNumber Stock move serial number.
     */
    public STrnStockMove(int[] moveKey, double quantity, String serialNumber) {
        this(moveKey, quantity, 0, serialNumber);
    }

    /**
     * Creates stock move.
     * @param moveKey Stock move primary key (year, item, unit, lot, company branch, warehouse).
     * @param quantity Stock move quantity.
     * @param value Stock move total value in local currency.
     */
    public STrnStockMove(int[] moveKey, double quantity, double value) {
        this(moveKey, quantity, value, "");
    }

    /**
     * Creates stock move.
     * @param moveKey Stock move primary key (year, item, unit, lot, company branch, warehouse).
     * @param quantity Stock move quantity.
     * @param value Stock move total value in local currency.
     * @param serialNumber Stock move serial number.
     */
    public STrnStockMove(int[] moveKey, double quantity, double value, String serialNumber) {
        mnPkYearId = moveKey[0];
        mnPkItemId = moveKey[1];
        mnPkUnitId = moveKey[2];
        mnPkLotId = moveKey[3];
        mnPkCompanyBranchId = moveKey[4];
        mnPkWarehouseId = moveKey[5];
        mdQuantity = quantity;
        mdValue = value;
        msSerialNumber = serialNumber;

        mnAuxRowNumber = 0;
        msAuxLot = "";
        mtAuxLotDateExpiration = null;
        mbAuxIsLotBlocked = false;
        mbAuxIsMoveBeingDeleted = false;
        mnSegregationId = 0;
        manSegregationReference = null;
        mnSegregationType = 0;
        mbIsCurrentSegregationExcluded = false;
        mnWarehouseType = 0;
        mlPkWarehouseIds = null;
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkLotId(int n) { mnPkLotId = n; }
    public void setPkCompanyBranchId(int n) { mnPkCompanyBranchId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setQuantity(double f) { mdQuantity = f; }
    public void setValue(double f) { mdValue = f; }
    public void setSerialNumber(String s) { msSerialNumber = s; }

    public void setAuxRowNumber(int n) { mnAuxRowNumber = n; }
    public void setAuxLot(String s) { msAuxLot = s; }
    public void setAuxLotDateExpiration(Date t) { mtAuxLotDateExpiration = t; }
    public void setAuxIsLotBlocked(boolean b) { mbAuxIsLotBlocked = b; }
    public void setAuxIsMoveBeingDeleted(boolean b) { mbAuxIsMoveBeingDeleted = b; }
    public void setSegregationId(int n) { this.mnSegregationId = n; }
    public void setSegregationReference(int[] o) { this.manSegregationReference = o; }
    public void setSegregationType(int n) { this.mnSegregationType = n; }
    public void setIsCurrentSegregationExcluded(boolean b) { this.mbIsCurrentSegregationExcluded = b; }
    public void setWarehouseType(int n) { this.mnWarehouseType = n; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkLotId() { return mnPkLotId; }
    public int getPkCompanyBranchId() { return mnPkCompanyBranchId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public ArrayList<Integer> getLWarehouseIds() { return mlPkWarehouseIds; }
    public double getQuantity() { return mdQuantity; }
    public double getValue() { return mdValue; }
    public String getSerialNumber() { return msSerialNumber; }

    public int getAuxRowNumber() { return mnAuxRowNumber; }
    public String getAuxLot() { return msAuxLot; }
    public Date getAuxLotDateExpiration() { return mtAuxLotDateExpiration; }
    public boolean getAuxIsLotBlocked() { return mbAuxIsLotBlocked; }
    public boolean getAuxIsMoveBeingDeleted() { return mbAuxIsMoveBeingDeleted; }
    public int getSegregationId() { return mnSegregationId; }
    public int[] getSegregationReference() { return manSegregationReference; }
    public int getSegregationType() { return mnSegregationType; }
    public boolean getIsCurrentSegregationExcluded() { return mbIsCurrentSegregationExcluded; }
    public int getWarehouseType() { return mnWarehouseType; }

    public void setLotKey(int[] key) { mnPkItemId = key[0]; mnPkUnitId = key[1]; mnPkLotId = key[2]; }
    public void setWarehouseKey(int[] key) { mnPkCompanyBranchId = key[0]; mnPkWarehouseId = key[1]; }
    public void setWarehousesList(ArrayList<Integer> whss) { mlPkWarehouseIds = whss; }
    public void setStockMoveKey(int[] key) { mnPkYearId = key[0]; mnPkItemId = key[1]; mnPkUnitId = key[2]; mnPkLotId = key[3]; mnPkCompanyBranchId = key[4]; mnPkWarehouseId = key[5]; }

    public int[] getLotKey() { return new int[] { mnPkItemId, mnPkUnitId, mnPkLotId }; }
    public int[] getWarehouseKey() { return new int[] { mnPkCompanyBranchId, mnPkWarehouseId }; }
    public int[] getStockMoveKey() { return new int[] { mnPkYearId, mnPkItemId, mnPkUnitId, mnPkLotId, mnPkCompanyBranchId, mnPkWarehouseId }; }

    @Override
    public String toString() {
        return msSerialNumber + (mdQuantity == 1 ? "" : " (" + mdQuantity + ")");
    }

    @Override
    public STrnStockMove clone() throws CloneNotSupportedException {
        STrnStockMove move = new STrnStockMove(this.getStockMoveKey(), this.getQuantity(), this.getValue(), this.getSerialNumber());

        move.setAuxRowNumber(this.getAuxRowNumber());
        move.setAuxLot(this.getAuxLot());
        move.setAuxLotDateExpiration(this.getAuxLotDateExpiration());
        move.setAuxIsLotBlocked(this.getAuxIsLotBlocked());
        move.setAuxIsMoveBeingDeleted(this.getAuxIsMoveBeingDeleted());
        move.setSegregationId(this.getSegregationId());
        move.setSegregationReference(this.getSegregationReference());
        move.setSegregationType(this.getSegregationType());
        move.setIsCurrentSegregationExcluded(this.getIsCurrentSegregationExcluded());
        move.setWarehouseType(this.getWarehouseType());

        return move;
    }
}
