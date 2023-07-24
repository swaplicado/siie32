/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import javax.swing.JOptionPane;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Edwin Carmona
 */
public class SMaterialRequestEntryRow implements SGridRow {
    
    protected int mnPkMatRequestId;
    protected int mnPkEntryId;
    protected double mdQuantity;
    //protected boolean mbDeleted;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    
    protected String msAuxItemCode;
    protected String msAuxItemName;
    protected String msAuxUnitCode;
    protected double mdAuxSegregated;
    protected double mdAuxStock;
    protected double mdAuxToSegregate;
    protected boolean mbAuxBulk;
    
    SClientInterface miClient;
    
    public SMaterialRequestEntryRow(SClientInterface client, final int fkItem, final int fkUnit) {
        miClient = client;
        mnFkItemId = fkItem;
        mnFkUnitId = fkUnit;
        
        this.readAuxs();
    }
    
    private void readAuxs() {
        SDataItem item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { mnFkItemId }, SLibConstants.EXEC_MODE_VERBOSE);
        SDataUnit unit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { mnFkUnitId }, SLibConstants.EXEC_MODE_VERBOSE);
        
        msAuxItemCode = item.getKey();
        msAuxItemName = item.getName();
        msAuxUnitCode = unit.getSymbol();
        mbAuxBulk = item.getIsBulk();
    }
    
    public void setPkMatRequestId(int n) { mnPkMatRequestId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setQuantity(double d) { mdQuantity = d; }

    public void setAuxSegregated(double mdAuxSegregated) { this.mdAuxSegregated = mdAuxSegregated; }
    public void setAuxStock(double mdAuxStock) { this.mdAuxStock = mdAuxStock; }
    public void setAuxToSegregate(double mdAuxToSegregate) { this.mdAuxToSegregate = mdAuxToSegregate; }

    public int getPkMatRequestId() { return mnPkMatRequestId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public double getQuantity() { return mdQuantity; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    
    public String getAuxItemCode() { return msAuxItemCode; }
    public String getAuxItemName() { return msAuxItemName; }
    public String getAuxUnitCode() { return msAuxUnitCode; }
    public double getAuxSegregated() { return mdAuxSegregated; }
    public double getAuxStock() { return mdAuxStock; }
    public double getAuxToSegregate() { return mdAuxToSegregate; }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkMatRequestId, mnPkEntryId };
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
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
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {
        
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch(col) {
            case 0:
                value = msAuxItemCode;
                break;
            case 1:
                value = msAuxItemName;
                break;
            case 2:
                value = msAuxUnitCode;
                break;
            case 3:
                value = mdQuantity;
                break;
            case 4:
                value = mdAuxSegregated;
                break;
            case 5:
                value = mdAuxStock;
                break;
            case 6:
                value = mdQuantity - mdAuxSegregated;
                break;
            case 7:
                value = mdAuxToSegregate;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch(col) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                break;
            case 7:
                if (SMaterialRequestEntryRow.hasDecimals((double) value) && !mbAuxBulk) {
                    JOptionPane.showMessageDialog(null, "El ítem no es a granel.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    mdAuxToSegregate = (double) value;
                }
                break;
            default:
        }
    }
    
    // Método para validar si un double tiene decimales o es un entero
    public static boolean hasDecimals(double number) {
        // Comparamos el número con su versión truncada y redondeada
        return number != Math.floor(number) || number != Math.ceil(number);
    }
}
