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
    protected double mdUnitaryPrice;
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
        this.mdUnitaryPrice = 0d;
        this.mdExceed = 0d;
        this.mlDpsEntries = dpsEntries;
        
        prepareTableRow();
    }
    
    private void readData() {
        moAuxUnit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { moMatRequestEntry.getFkUserUnitId() }, SLibConstants.EXEC_MODE_VERBOSE);
        //qtyOrOrigQty 1 = qty, 2 = orig_qty
        int qtyOrOrigQty = 2;
        double linkedFromDb = SMaterialRequestUtils.getQuantityLinkedOfReqEty(miClient.getSession(), moMatRequestEntry.getPrimaryKey(), maDpsType, this.maDpsPk == null ? null : this.maDpsPk, qtyOrOrigQty);
        mdQuantityLinked = linkedFromDb + getMemoryLinkedQuantity();
        // qtyOrQtyOrig 1 = qty, 2 = orig_qty
        int qtyOrQtyOrig = 2;
        mdQuantitySupplied = SMaterialRequestUtils.getQuantitySuppliedOfReqEty(miClient.getSession(), moMatRequestEntry.getPrimaryKey(), qtyOrQtyOrig);
        mdUnitaryPrice = moMatRequestEntry.getPriceUnitary();
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
        mvValues.add(moMatRequestEntry.getPkEntryId());
        mvValues.add(moMatRequestEntry.getDataItem().getKey());
        mvValues.add(moMatRequestEntry.getDataItem().getName());
        mvValues.add(moMatRequestEntry.getDataItem().getPartNumber());
        mvValues.add(moMatRequestEntry.getUserQuantity());
        mvValues.add(mdQuantityToLink);
        mvValues.add(moAuxUnit.getSymbol());
        mvValues.add(mdUnitaryPrice);
        double qty = moMatRequestEntry.getUserQuantity() - mdQuantitySupplied - mdQuantityLinked;
        mvValues.add(qty < 0d ? 0d : qty);
        mvValues.add(mdQuantitySupplied);
        mvValues.add(mdQuantityLinked);
        mvValues.add(moAuxUnit.getSymbol());
        mvValues.add(mdExceed);
    }
    
    public double getQuantityRemaining() {
        double qty = moMatRequestEntry.getUserQuantity() - mdQuantitySupplied - mdQuantityLinked;
        return qty < 0d ? 0d : qty;
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
        return (double) mvValues.get(5);
    }

    public void setQuantityToLink(double mdToLinkQuantity) {
        mdQuantityToLink = mdToLinkQuantity;
    }
    
    public double getUnitaryPrice() {
        return (double) mvValues.get(7);
    }
    
    public void setUnitaryPrice(double price) {
        mdUnitaryPrice = price;
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
