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
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    protected Date mtDateRequest;
    protected String msNotes;
    //protected boolean mbDeleted;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkConsumeEntityId_n;
    protected int mnFkSubConsumeEntityId_n;
    
    protected String msAuxItemCode;
    protected String msAuxItemName;
    protected String msAuxUnitCode;
    protected String msAuxPartNum;
    protected String msAuxConsumeEntity;
    protected double mdAuxSegregated;
    protected double mdAuxSupplied;
    protected double mdAuxStock;
    protected double mdAuxToSegregate;
    protected double mdAuxToSupply;
    protected double mdAuxToEstimate;
    protected boolean mbAuxBulk;
    protected boolean mbAuxEstimate;
    
    SClientInterface miClient;
    
    protected int mnFormType;
    
    public static int FORM_SEGREGATION = 1;
    public static int FORM_SUPPLY = 2;
    public static int FORM_ESTIMATE = 3;
    
    public SMaterialRequestEntryRow(SClientInterface client, final int formType, final int fkItem, final int fkUnit, String consumeEntity) {
        miClient = client;
        mnFormType = formType;
        mnFkItemId = fkItem;
        mnFkUnitId = fkUnit;
        mnFkConsumeEntityId_n = 0;
        mnFkSubConsumeEntityId_n = 0;
        msAuxConsumeEntity = consumeEntity;
        mtDateRequest = null;
        msNotes = "";
        
        try {
            this.readAuxs();
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestEntryRow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void readAuxs() throws SQLException {
        SDataItem item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { mnFkItemId }, SLibConstants.EXEC_MODE_VERBOSE);
        SDataUnit unit = item.getDbmsDataUnit();
        
        msAuxItemCode = item.getKey();
        msAuxItemName = item.getName();
        msAuxUnitCode = unit.getSymbol();
        msAuxPartNum = item.getPartNumber();
        mbAuxBulk = item.getIsBulk();
    }
    
    public void setPkMatRequestId(int n) { mnPkMatRequestId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setDateRequest(Date t) { mtDateRequest = t; }
    public void setNotes(String s) { msNotes = s; }

    public void setAuxSegregated(double mdAuxSegregated) { this.mdAuxSegregated = mdAuxSegregated; }
    public void setAuxSupplied(double mdAuxSupplied) { this.mdAuxSupplied = mdAuxSupplied; }
    public void setAuxStock(double mdAuxStock) { this.mdAuxStock = mdAuxStock; }
    public void setAuxToSegregate(double mdAuxToSegregate) { this.mdAuxToSegregate = mdAuxToSegregate; }
    public void setAuxToSupply(double mdAuxToSupply) { this.mdAuxToSupply = mdAuxToSupply; }
    public void setAuxToEstimate(double mdAuxToEstimate) { this.mdAuxToEstimate = mdAuxToEstimate; }
    public void setAuxIsToEstimate(boolean mbAuxEstimate) { this.mbAuxEstimate = mbAuxEstimate; }

    public int getPkMatRequestId() { return mnPkMatRequestId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public double getQuantity() { return mdQuantity; }
    public Date getDateRequired() { return mtDateRequest; }
    public String getNotes() { return msNotes; }
    
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkConsumeEntityId_n() { return mnFkConsumeEntityId_n; }
    public int getFkSubConsumeEntityId_n() { return mnFkSubConsumeEntityId_n; }
    
    public String getAuxItemCode() { return msAuxItemCode; }
    public String getAuxItemName() { return msAuxItemName; }
    public String getAuxUnitCode() { return msAuxUnitCode; }
    public String getAuxPartNumber() { return msAuxPartNum; }
    public double getAuxSegregated() { return mdAuxSegregated; }
    public double getAuxSupplied() { return mdAuxSupplied; }
    public double getAuxStock() { return mdAuxStock; }
    public double getAuxToSegregate() { return mdAuxToSegregate; }
    public double getAuxToSupply() { return mdAuxToSupply; }
    public double getAuxQuantityToEstimate() { return mdAuxToEstimate; }
    public boolean isToEstimate() { return mbAuxEstimate; }

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
        
        if (mnFormType == FORM_SEGREGATION) {
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
        }
        else if (mnFormType == FORM_SUPPLY) {
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
                    value = mdAuxSupplied;
                    break;
                case 5:
                    value = mdAuxSegregated;
                    break;
                case 6:
                    value = mdAuxStock;
                    break;
                case 7:
                    value = mdQuantity - mdAuxSupplied;
                    break;
                case 8:
                    value = mdAuxToSupply;
                    break;
                case 9:
                    value = msAuxConsumeEntity;
                    break;
                default:
            }
        }
        else if (mnFormType == FORM_ESTIMATE) {
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
                    value = msAuxPartNum;
                    break;
                case 4:
                    value = msNotes;
                    break;
                case 5:
                    value = mdQuantity;
                    break;
                case 6:
                    value = mdAuxToEstimate;
                    break;
                case 7:
                    value = false;
                    break;
                case 8:
                    value = mbAuxEstimate;
                    break;
                default:
            }
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        if (mnFormType == FORM_SEGREGATION) {
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
        else if (mnFormType == FORM_SUPPLY) {
            switch(col) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    break;
                case 8:
                    if (SMaterialRequestEntryRow.hasDecimals((double) value) && !mbAuxBulk) {
                        JOptionPane.showMessageDialog(null, "El ítem no es a granel.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        mdAuxToSupply = (double) value;
                    }
                    break;
                case 9:
                    break;
                default:
            }
        }
        else if (mnFormType == FORM_ESTIMATE) {
            switch(col) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    break;
                case 6:
                    if (SMaterialRequestEntryRow.hasDecimals((double) value) && !mbAuxBulk) {
                        JOptionPane.showMessageDialog(null, "El ítem no es a granel.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        mdAuxToEstimate = (double) value;
                    }
                    break;
                case 7:
                    break;
                case 8:
                    mbAuxEstimate = (boolean) value;
                    if (mbAuxEstimate) {
                        mdAuxToEstimate = mdQuantity;
                    }
                    break;
                default:
            }
        }
    }
    
    // Método para validar si un double tiene decimales o es un entero
    public static boolean hasDecimals(double number) {
        // Comparamos el número con su versión truncada y redondeada
        return number != Math.floor(number) || number != Math.ceil(number);
    }
}
