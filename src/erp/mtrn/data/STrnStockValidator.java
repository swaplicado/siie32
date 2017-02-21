/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mmfg.data.SDataProductionOrderChargeEntry;
import erp.mmfg.data.SDataProductionOrderChargeEntryLot;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Sergio Flores, Edwin Carmona
 */
public class STrnStockValidator {

    protected SClientInterface miClient;
    protected int mnYear;
    protected int[] manWarehouseKey;
    protected int[] manDiogKey;
    protected int[] manReferenceKey;
    protected int mnSegregationType;
    protected Vector<STrnStockMove> mvStockMoves;

    public STrnStockValidator(erp.client.SClientInterface client, int year, int[] warehouseKey, int[] iogKey, final int[] referenceKey, int segregationType) {
        miClient = client;
        mnYear = year;
        manWarehouseKey = warehouseKey;
        manDiogKey = iogKey;
	manReferenceKey = referenceKey;
        mnSegregationType = segregationType;
        mvStockMoves = new Vector<STrnStockMove>();
    }

    public void addStockMove(final int[] lotKey, final double quantity, final boolean isMoveBeingDeleted) {
        boolean found = false;

        for (STrnStockMove stockMove : mvStockMoves) {
            if (SLibUtilities.compareKeys(lotKey, stockMove.getLotKey()) && isMoveBeingDeleted == stockMove.getAuxIsMoveBeingDeleted()) {
                stockMove.setQuantity(stockMove.getQuantity() + quantity);
                found = true;
                break;
            }
        }

        if (!found) {
            STrnStockMove stockMove = new STrnStockMove(new int[] { mnYear, lotKey[0], lotKey[1], lotKey[2], manWarehouseKey[0], manWarehouseKey[1] }, quantity);
            stockMove.setAuxIsMoveBeingDeleted(isMoveBeingDeleted);
            mvStockMoves.add(stockMove);
        }
    }

    public java.lang.String validateOutgoingItems(final boolean isDocBeingDeleted, final Date dateCutOff) {
        double stock = 0;
        double segregated = 0;
        double available = 0;
        String msg = "";
        SDataItem item = null;
        SDataUnit unit = null;
        SDataStockLot lot = null;
        STrnStock objStock = null;

        try {
            if (mnYear != SLibTimeUtilities.digestYear(dateCutOff)[0]) {
                msg = "La fecha de corte " + miClient.getSessionXXX().getFormatters().getDateFormat().format(dateCutOff) + " " +
                        "no pertenece al ejercicio " + mnYear + ".";
            }
            else {
                for (STrnStockMove stockMove : mvStockMoves) {
                    if (manReferenceKey != null && mnSegregationType != SLibConstants.UNDEFINED) {
                        stockMove.setSegregationReference(new int[] { manReferenceKey[0], manReferenceKey[1] });
                        stockMove.setSegregationType(SDataConstantsSys.TRNS_TP_STK_SEG_MFG_ORD);
                        stockMove.setIsCurrentSegregationExcluded(true);
                    }

                    // The STrnStockMove object is sent as parameters for the filter
                    objStock = STrnStockSegregationUtils.getStkSegregated(miClient, stockMove);
                    segregated = objStock.getSegregatedStock();
                    	
                    stock = STrnUtilities.obtainStock(miClient, stockMove.getPkYearId(),
                            stockMove.getPkItemId(), stockMove.getPkUnitId(), stockMove.getPkLotId(),
                            stockMove.getPkCompanyBranchId(), stockMove.getPkWarehouseId(), dateCutOff,
                            isDocBeingDeleted || stockMove.getAuxIsMoveBeingDeleted() ? null : manDiogKey);

                    available = stock - segregated;
                    
                    if (available < stockMove.getQuantity()) {
                        msg = miClient.getSessionXXX().getFormatters().getDateFormat().format(dateCutOff);
                    }
                    else {
                        stock = STrnUtilities.obtainStock(miClient, stockMove.getPkYearId(),
                                stockMove.getPkItemId(), stockMove.getPkUnitId(), stockMove.getPkLotId(),
                                stockMove.getPkCompanyBranchId(), stockMove.getPkWarehouseId(),
                                isDocBeingDeleted || stockMove.getAuxIsMoveBeingDeleted() ? null : manDiogKey);

                        if (available < stockMove.getQuantity()) {
                            msg = miClient.getSessionXXX().getFormatters().getDateFormat().format(SLibTimeUtilities.getEndOfYear(dateCutOff));
                        }
                    }

                    if (!msg.isEmpty()) {
                        item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { stockMove.getPkItemId() }, SLibConstants.EXEC_MODE_VERBOSE);
                        unit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { stockMove.getPkUnitId() }, SLibConstants.EXEC_MODE_VERBOSE);
                        lot = (SDataStockLot) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_LOT, stockMove.getLotKey(), SLibConstants.EXEC_MODE_VERBOSE);

                        msg = "No hay unidades disponibles suficientes del ítem '" + item.getItem() + "', clave '" + item.getKey() + "', lote '" + lot.getLot() + "' al " + msg + ":\n" +
                                "  " + miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(stock) + " " + unit.getSymbol() + " (en existencia)\n" +
                                " -" + miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(segregated) + " " + unit.getSymbol() + " (segregadas)\n" +
                                "= " + miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(available) + " " + unit.getSymbol() + " (disponibles)\n" +
                                "para hacer el movimiento solicitado " +
                                "(" + miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(stockMove.getQuantity()) + " " + unit.getSymbol() + "), " +
                                "hacen falta " + miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(stockMove.getQuantity() - (available)) + " " + unit.getSymbol() + ".";
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        return msg;
    }

    public static java.lang.String validateStockLots(final erp.client.SClientInterface client, final java.util.Vector<erp.mtrn.data.SDataDiogEntry> iogEntries, final int iogCategoryId, final boolean isDocBeingDeleted) throws Exception {
        String msg = "";

        for (SDataDiogEntry entry : iogEntries) {
            msg = entry.validateStockLots(client, iogCategoryId, isDocBeingDeleted);
            if (msg.length() > 0) {
                break;
            }
        }

        return msg;
    }

    public static java.lang.String validateStockLots(final erp.client.SClientInterface client, final java.util.Vector<erp.mmfg.data.SDataProductionOrderChargeEntry> chargeEntries) throws Exception {
        String msg = "";
        SDataStockLot lot = null;

        ENTRY:
        for (SDataProductionOrderChargeEntry entry : chargeEntries) {
            ENTRY_LOT:
            for (SDataProductionOrderChargeEntryLot entryLot : entry.getDbmsProductionOrderChargeEntryLots()) {
                if (entryLot.getPkLotId() == SLibConstants.UNDEFINED && entryLot.getDbmsLot().length() == 0) {
                    continue ENTRY_LOT;
                }
                else if (entryLot.getPkLotId() == SLibConstants.UNDEFINED) {
                    lot = STrnUtilities.readLot(client, entryLot.getPkItemId(), entryLot.getPkUnitId(), entryLot.getDbmsLot());
                }
                else {
                    lot = (SDataStockLot) SDataUtilities.readRegistry(client, SDataConstants.TRN_LOT, new int[] { entryLot.getPkItemId(), entryLot.getPkUnitId(), entryLot.getPkLotId() }, SLibConstants.EXEC_MODE_SILENT);
                }

                if (lot == null) {
                    msg = "El lote '" + entryLot.getDbmsLot() + "' del ítem '" + entryLot.getDbmsItem() + "', clave '" + entryLot.getDbmsItemKey() + "' no existe o está eliminado.";
                    break ENTRY;
                }
                else {
                    if (lot.getIsBlocked()) {
                        msg = "El lote '" + entryLot.getDbmsLot() + "' del ítem '" + entryLot.getDbmsItem() + "', clave '" + entryLot.getDbmsItemKey() + "' está bloqueado.";
                        break ENTRY;
                    }
                    else if (lot.getIsDeleted()) {
                        msg = "El lote '" + entryLot.getDbmsLot() + "' del ítem '" + entryLot.getDbmsItem() + "', clave '" + entryLot.getDbmsItemKey() + "' está eliminado.";
                        break ENTRY;
                    }
                    else {
                        entryLot.setPkLotId(lot.getPkLotId());
                    }
                }
            }
        }

        return msg;
    }

    public static java.lang.String validateStockMoves(final erp.client.SClientInterface client,
            final java.util.Vector<erp.mtrn.data.SDataDiogEntry> iogEntries, final int iogCategoryId,
            final int[] iogKey, final int[] warehouseKey, final boolean isDocBeingDeleted, final Date dateCutOff, final int[] segregationReference, final int segregationType) throws Exception {
        
        STrnStockValidator validator = new STrnStockValidator(client, iogKey[0], warehouseKey, iogKey, segregationReference, segregationType);

        for (SDataDiogEntry entry : iogEntries) {
            if (entry.shouldValidateStockLots() && entry.shouldValidateOutgoingItems(iogCategoryId, isDocBeingDeleted)) {
                for (STrnStockMove stockMove : entry.getAuxStockMoves()) {
                    validator.addStockMove(stockMove.getLotKey(), stockMove.getQuantity(), entry.getIsRegistryEdited() && entry.getIsRegistryRequestDelete());
                }
            }
        }

        return validator.validateOutgoingItems(isDocBeingDeleted, dateCutOff);
    }
}
