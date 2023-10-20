/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mod.trn.db.SDbMaterialRequestEntry;
import erp.mod.trn.db.SMaterialRequestUtils;

/**
 *
 * @author Edwin Carmona
 */
public class SDataMaterialRequestEntryLinkRow extends erp.lib.table.STableRow {
    
    protected double mdQuantitySupplied;
    protected double mdQuantityToLink;
    protected double mdQuantityLinked;
    protected double mdExceed;
    
    protected final SDbMaterialRequestEntry moMatRequestEntry;
    private final erp.client.SClientInterface miClient;
    private SDataUnit moAuxUnit;
    private int[] maDpsType;
    private int[] maDpsPk;
    java.util.Vector<erp.mtrn.data.SDataDpsEntry> mlDpsEntries;

    public SDataMaterialRequestEntryLinkRow(erp.client.SClientInterface client, 
                                            java.lang.Object data, 
                                            final int[] dpsType,
                                            final int[] dpsPk,
                                            final java.util.Vector<erp.mtrn.data.SDataDpsEntry> dpsEntries) {
        this.miClient = client;
        this.moMatRequestEntry = (SDbMaterialRequestEntry) data;
        this.moData = this.moMatRequestEntry;
        this.maDpsType = dpsType;
        this.maDpsPk = dpsPk;
        this.mdQuantitySupplied = 0d;
        this.mdQuantityLinked = 0d;
        this.mdQuantityToLink = 0d;
        this.mdExceed = 0d;
        this.mlDpsEntries = dpsEntries;
        
        prepareTableRow();
    }
    
    private void readData() {
        moAuxUnit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { moMatRequestEntry.getFkUnitId() }, SLibConstants.EXEC_MODE_VERBOSE);
        double linkedFromDb = SMaterialRequestUtils.getQuantityLinkedOfReqEty(miClient.getSession(), moMatRequestEntry.getPrimaryKey(), maDpsType, this.maDpsPk == null ? null : this.maDpsPk);
        mdQuantityLinked = linkedFromDb + getMemoryLinkedQuantity();
        mdQuantitySupplied = SMaterialRequestUtils.getQuantitySupplied(miClient.getSession(), moMatRequestEntry.getPrimaryKey());
    }
    
    private double getMemoryLinkedQuantity() {
        double dQuantityOnMemory = 0d;
        
        if (this.mlDpsEntries != null && this.mlDpsEntries.size() > 0) {
            for (SDataDpsEntry oDpsEntry : mlDpsEntries) {
                if (oDpsEntry.getDbmsDpsEntryMatRequestLink() != null) {
                    if (oDpsEntry.getDbmsDpsEntryMatRequestLink().getFkMaterialRequestId() == this.moMatRequestEntry.getPkMatRequestId() &&
                            oDpsEntry.getDbmsDpsEntryMatRequestLink().getFkMaterialRequestEntryId() == this.moMatRequestEntry.getPkEntryId()) {
                        dQuantityOnMemory += oDpsEntry.getDbmsDpsEntryMatRequestLink().getQuantity();
                    }
                }
            }
        }
        
        return dQuantityOnMemory;
    }

    @Override
    public void prepareTableRow() {
        readData();
        
        setPrimaryKey(moMatRequestEntry.getPrimaryKey());
        setRowPrimaryKey(moMatRequestEntry.getPrimaryKey());
        
        mvValues.clear();
        mvValues.add(moMatRequestEntry.getDataItem().getCode());
        mvValues.add(moMatRequestEntry.getDataItem().getName());
        mvValues.add(moMatRequestEntry.getQuantity());
        mvValues.add(moAuxUnit.getSymbol());
        mvValues.add(mdQuantitySupplied);
        mvValues.add(mdQuantityLinked);
        double qty = moMatRequestEntry.getQuantity() - mdQuantitySupplied - mdQuantityLinked;
        mvValues.add(qty < 0d ? 0d : qty);
        mvValues.add(mdQuantityToLink);
        mvValues.add(moAuxUnit.getSymbol());
        mvValues.add(moMatRequestEntry.getPriceUnitary());
        mvValues.add(mdExceed);
    }
    
    public double getQuantityRemaining() {
     return moMatRequestEntry.getQuantity() - mdQuantitySupplied - mdQuantityLinked;
    }
    
    public double getQuantitySupplied() {
        return mdQuantitySupplied;
    }

    public void setQuantitySupplied(double quantitySupplied) {
        this.mdQuantitySupplied = quantitySupplied;
    }

    public double getQuantityLinked() {
        return mdQuantityLinked;
    }

    public void setQuantityLinked(double quantityLinked) {
        this.mdQuantityLinked = quantityLinked;
    }

    public double getQuantityToLink() {
        return mdQuantityToLink;
    }
    
    public double getQuantityToLinkV() {
        return (double) mvValues.get(7);
    }

    public void setQuantityToLink(double mdToLinkQuantity) {
        mdQuantityToLink = mdToLinkQuantity;
    }
    
    public SDataItem getItem() {
        return moMatRequestEntry.getDataItem();
    }
    
    public SDataUnit getUnit() {
        return moAuxUnit;
    }
    
    public SDbMaterialRequestEntry getMaterialRequestEntry() {
        return moMatRequestEntry;
    }
}
