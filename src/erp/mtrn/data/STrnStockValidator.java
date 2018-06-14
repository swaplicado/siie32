/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
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
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores, Edwin Carmona
 */
public class STrnStockValidator {

    protected SClientInterface miClient;
    protected int mnYear;
    protected int[] manWarehouseKey;
    protected int[] manDiogKey;
    protected int mnSegregationType;
    protected int[] manSegregationReferenceKey;
    protected int mnMaintUserId;    // business partner ID acting as maintenance user
    protected Vector<STrnStockMove> mvStockMoves;

    public STrnStockValidator(erp.client.SClientInterface client, int year, int[] warehouseKey, int[] iogKey, int segregationType, final int[] segregationReferenceKey, final int maintUserId) {
        miClient = client;
        mnYear = year;
        manWarehouseKey = warehouseKey;
        manDiogKey = iogKey;
        mnSegregationType = segregationType;
	manSegregationReferenceKey = segregationReferenceKey;
        mnMaintUserId = maintUserId;
        mvStockMoves = new Vector<>();
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

    /**
     * Validates that pretending outgoing items are available in stock.
     * @param isDocBeingDeleted
     * @param dateCutOff
     * @return 
     */
    public java.lang.String validateOutgoingItems(final boolean isDocBeingDeleted, final Date dateCutOff) {
        String msg = "";
        String msgDate = "";
        
        try {
            if (mnYear != SLibTimeUtilities.digestYear(dateCutOff)[0]) {
                msgDate = SLibUtils.DateFormatDate.format(dateCutOff);
                msg = "La fecha de corte " + msgDate + " no pertenece al ejercicio " + mnYear + ".";
            }
            else {
                for (STrnStockMove stockMove : mvStockMoves) {
                    // Validate each stock move:
                    
                    double stock = 0;
                    double stockSegregated = 0;
                    double stockTotal = 0;
                    double stockAvailable = 0;
                    boolean isStockInsufficient = false;
                    boolean isStockAvailableInsufficient = false;
                    STrnStockMove stockMoveSegregation = null;
                    
                    // 1. Check stock to cutoff date:
                    
                    stock = STrnUtilities.obtainStock(miClient, stockMove.getPkYearId(),
                            stockMove.getPkItemId(), stockMove.getPkUnitId(), stockMove.getPkLotId(),
                            stockMove.getPkCompanyBranchId(), stockMove.getPkWarehouseId(), mnMaintUserId, dateCutOff,
                            isDocBeingDeleted || stockMove.getAuxIsMoveBeingDeleted() ? null : manDiogKey);
                    
                    if (stock < stockMove.getQuantity()) {
                        isStockInsufficient = true;
                        msgDate = SLibUtils.DateFormatDate.format(dateCutOff);  // cutoff date
                    }
                    else {
                        // 2. Check stock to end of year of cutoff date:
                        
                        stock = STrnUtilities.obtainStock(miClient, stockMove.getPkYearId(),
                                stockMove.getPkItemId(), stockMove.getPkUnitId(), stockMove.getPkLotId(),
                                stockMove.getPkCompanyBranchId(), stockMove.getPkWarehouseId(), mnMaintUserId, null,
                                isDocBeingDeleted || stockMove.getAuxIsMoveBeingDeleted() ? null : manDiogKey);

                        if (stock < stockMove.getQuantity()) {
                            isStockInsufficient = true;
                            msgDate = SLibUtils.DateFormatDate.format(SLibTimeUtils.getEndOfYear(dateCutOff));  // end of year of cutoff date
                        }
                        else {
                            // 3. Check available stock omitting segregated stock:

                            if (mnSegregationType != SLibConstants.UNDEFINED && manSegregationReferenceKey != null) {
                                // set current segregation in stock-movement to exclude it when validating available stock:
                                stockMove.setSegregationReference(new int[] { manSegregationReferenceKey[0], manSegregationReferenceKey[1] });
                                stockMove.setSegregationType(mnSegregationType);
                                stockMove.setIsCurrentSegregationExcluded(true);
                            }

                            stockSegregated = STrnStockSegregationUtils.getStockSegregated(miClient, stockMove).getSegregatedStock();
                            
                            stockMoveSegregation = stockMove.clone();
                            stockMoveSegregation.setPkLotId(0);
                            stockTotal = STrnStockSegregationUtils.getStock(miClient, stockMoveSegregation, isDocBeingDeleted || stockMove.getAuxIsMoveBeingDeleted() ? null : manDiogKey).getStock();
                            
                            stockAvailable = stockTotal - stockSegregated;
                            
                            if (stockAvailable < stockMove.getQuantity()) {
                                isStockAvailableInsufficient = true;
                                msgDate = SLibUtils.DateFormatDate.format(SLibTimeUtils.getEndOfYear(dateCutOff));  // end of year of cutoff date
                            }
                        }
                    }

                    if (isStockInsufficient || isStockAvailableInsufficient) {
                        SDataItem item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { stockMove.getPkItemId() }, SLibConstants.EXEC_MODE_VERBOSE);
                        SDataUnit unit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { stockMove.getPkUnitId() }, SLibConstants.EXEC_MODE_VERBOSE);
                        SDataStockLot lot = (SDataStockLot) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_LOT, stockMove.getLotKey(), SLibConstants.EXEC_MODE_VERBOSE);
                        
                        if (isStockInsufficient) {
                            msg = "No hay unidades en existencia suficientes del ítem '" + item.getItem() + " (" + item.getKey() + ")', lote '" + lot.getLot() + "', al " + msgDate + ":\n" +
                                    "en existencia: " + SLibUtils.getDecimalFormatQuantity().format(stock) + " " + unit.getSymbol() + ".\n" +
                                    "Para hacer el movimiento solicitado de " + SLibUtils.getDecimalFormatQuantity().format(stockMove.getQuantity()) + " " + unit.getSymbol() + ", " +
                                    "faltan " + SLibUtils.getDecimalFormatQuantity().format(stockMove.getQuantity() - (stock)) + " " + unit.getSymbol() + ".";
                        }
                        else {  // isStockAvailableInsufficient:
                            msg = "No hay unidades disponibles suficientes del ítem '" + item.getItem() + " (" + item.getKey() + ")', al " + msgDate + ":\n" +
                                    "en existencia: " + SLibUtils.getDecimalFormatQuantity().format(stockTotal) + " " + unit.getSymbol() + "\n" +
                                    "- segregadas: " + SLibUtils.getDecimalFormatQuantity().format(stockSegregated) + " " + unit.getSymbol() + "\n" +
                                    "= disponibles: " + SLibUtils.getDecimalFormatQuantity().format(stockAvailable) + " " + unit.getSymbol() + ".\n" +
                                    "Para hacer el movimiento solicitado de " + SLibUtils.getDecimalFormatQuantity().format(stockMove.getQuantity()) + " " + unit.getSymbol() + ", " +
                                    "faltan " + SLibUtils.getDecimalFormatQuantity().format(stockMove.getQuantity() - stockAvailable) + " " + unit.getSymbol() + ".";
                        }
                        
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
            final int[] iogKey, final int[] warehouseKey, final boolean isDocBeingDeleted, final Date dateCutOff,
            final int segregationType, final int[] segregationReferenceKey, final int maintUserId) throws Exception {
        
        STrnStockValidator validator = new STrnStockValidator(client, iogKey[0], warehouseKey, iogKey, segregationType, segregationReferenceKey, maintUserId);

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
