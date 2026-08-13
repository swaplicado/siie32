/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.utils;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mitm.data.SDataItem;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsDpsLink;
import erp.mtrn.data.SDataDpsDpsMerge;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SDataDpsEntryCommissions;
import erp.mtrn.data.SDataDpsEntryNotes;
import erp.mtrn.data.SDataDpsEntryTax;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Edwin Carmona
 */
public class SMergeEntriesUtils {
    
    public static SMergeData mergeEntries(SClientInterface oClient, 
                                                    SDataDps oDps, 
                                                    List<SDataDpsEntry> lDpsEntries, 
                                                    List<SDataEntryDpsMergeRow> selectedEntries,
                                                    double unitaryPriceCur,
                                                    boolean isLocalCurrency,
                                                    boolean isDiscountDocPercentage,
                                                    double dDiscount,
                                                    double dExchangeRate,
                                                    boolean bUndoMerge) {
        final int idYear = oDps.getPkYearId();
        final int idDoc = oDps.getPkDocId();
        // La versión 0 sería la original, preservarla
        int mergeVersion = SDataDpsDpsMerge.getNewVersion(oClient.getSession().getStatement(), idYear, idDoc);
        
        boolean isFirstEntry = true;
        SDataDpsEntry oNewEntry = null;
        double dTotalQuantity = 0;
        double dTotalOriginalQuantity = 0;
        SDataDpsDpsMerge oDpsMergeEty;
        SDataDpsDpsMerge oDpsOriginalLinkEty;
        List<SDataDpsEntry> lDpsSelectedPastEntries = new ArrayList<>();
        List<SDataDpsDpsMerge> lMergedEntries = new ArrayList<>();
        List<SDataDpsDpsLink> lNewLinks = new ArrayList<>();
        List<SDataDpsEntry> lNotSelectedEntries = new ArrayList<>();
        List<SDataDpsEntry> lEntriesToAdd = new ArrayList<>();
        List<SDataDpsEntry> lEntriesToDelete = new ArrayList<>();
        if (bUndoMerge) {
            List<SDataDpsDpsMerge> lOriginalEntries = SDataDpsDpsMerge.getMergedVersionZero(oClient.getSession().getStatement(), idYear, idDoc);
            for (SDataDpsEntry oDpsEntry : lDpsEntries) {
                boolean isOriginal = false;
                for (SDataDpsDpsMerge oDpsOriginalEty : lOriginalEntries) {
                    if (oDpsOriginalEty.getFkDpsNewYearId() == oDpsEntry.getPkYearId() &&
                        oDpsOriginalEty.getFkDpsNewDocId() == oDpsEntry.getPkDocId() &&
                        oDpsOriginalEty.getFkDpsNewEntryId() == oDpsEntry.getPkEntryId()) {
                        oDpsEntry.setIsDeleted(false);
                        oDpsEntry.setAuxUndoMerged(true);
                        
                        SDataDpsDpsLink oOriginalDpsLink = new SDataDpsDpsLink();
                        oOriginalDpsLink.setPkSourceYearId(oDpsOriginalEty.getFkDpsOldYearId());
                        oOriginalDpsLink.setPkSourceDocId(oDpsOriginalEty.getFkDpsOldDocId());
                        oOriginalDpsLink.setPkSourceEntryId(oDpsOriginalEty.getFkDpsOldEntryId());
                        oOriginalDpsLink.setPkDestinyYearId(oDpsOriginalEty.getFkDpsNewYearId());
                        oOriginalDpsLink.setPkDestinyDocId(oDpsOriginalEty.getFkDpsNewDocId());
                        oOriginalDpsLink.setPkDestinyEntryId(oDpsOriginalEty.getFkDpsNewEntryId());
                        oOriginalDpsLink.setQuantity(oDpsOriginalEty.getQuantity());
                        oOriginalDpsLink.setOriginalQuantity(oDpsOriginalEty.getOriginalQuantity());
                        
                        oDpsEntry.getDbmsDpsLinksAsDestiny().clear();
                        oDpsEntry.getDbmsDpsLinksAsDestiny().add(oOriginalDpsLink);
                                
                        lEntriesToAdd.add(oDpsEntry);
                        isOriginal = true;
                        break;
                    }
                }

                if (!isOriginal) {
                    lNotSelectedEntries.add(oDpsEntry);
                }
            }

            List<SDataDpsDpsMerge> lMerged = SDataDpsDpsMerge.getMergedEntries(oClient.getSession().getStatement(), idYear, idDoc);
            for (SDataDpsEntry oNotSelected : lNotSelectedEntries) {
                // si está borrada, se agrega a lista de borradas
                if (oNotSelected.getIsDeleted()) {
                    lEntriesToDelete.add(oNotSelected);
                }
                else {
                    // si no está borrada y está en la configuración de merge, se agrega a lista de borradas
                    boolean isInMergeConfig = false;
                    for (SDataDpsDpsMerge oDpsMerge : lMerged) {
                        if (oDpsMerge.getFkDpsNewYearId() == oNotSelected.getPkYearId() &&
                        oDpsMerge.getFkDpsNewDocId() == oNotSelected.getPkDocId() &&
                        oDpsMerge.getFkDpsNewEntryId() == oNotSelected.getPkEntryId()) {
                            isInMergeConfig = true;
                            break;
                        }
                    }
                    if (isInMergeConfig) {
                        lEntriesToDelete.add(oNotSelected);
                    }
                    // si no está borrada y no está en la configuración de merge, se agrega a lista de agregadas
                    else {
                        lEntriesToAdd.add(oNotSelected);
                    }
                }
            }
            
            lNotSelectedEntries.clear();
        }
        else {
            for (SDataEntryDpsMergeRow selectedEntry : selectedEntries) {
                for (SDataDpsEntry oDpsEntry : lDpsEntries) {
                    if (SLibUtilities.compareKeys(selectedEntry.getDpsEntryKey(), oDpsEntry.getPrimaryKey())) {
                        
                        // crear partida de unificación en tabla de merge
                        oDpsMergeEty = new SDataDpsDpsMerge();
                        oDpsMergeEty.setFkDpsOldYearId(oDpsEntry.getPkYearId());
                        oDpsMergeEty.setFkDpsOldDocId(oDpsEntry.getPkDocId());
                        oDpsMergeEty.setFkDpsOldEntryId(oDpsEntry.getPkEntryId());
                        oDpsMergeEty.setFkDpsNewYearId(idYear);
                        oDpsMergeEty.setFkDpsNewDocId(idDoc);
                        oDpsMergeEty.setFkDpsNewEntryId(0);
                        oDpsMergeEty.setVersion(mergeVersion == 0 ? 1 : mergeVersion);
                        oDpsMergeEty.setQuantity(oDpsEntry.getQuantity());
                        oDpsMergeEty.setOriginalQuantity(oDpsEntry.getOriginalQuantity());
                        oDpsMergeEty.setUnitaryPrice(oDpsEntry.getPriceUnitary());
                        oDpsMergeEty.setUnitaryPriceCy(oDpsEntry.getPriceUnitaryCy());
    
                        lMergedEntries.add(oDpsMergeEty);
    
                        dTotalQuantity += oDpsEntry.getQuantity();
                        dTotalOriginalQuantity += oDpsEntry.getOriginalQuantity();
                        if (isFirstEntry) {
                            
                            // crear nueva partida en base a la primera partida seleccionada
                            oNewEntry = oDpsEntry.clone();
                            isFirstEntry = false;
                        }
                        
                        // vínculos como destino de documentos relacionados
                        if (oDpsEntry.getDbmsDpsLinksAsDestiny() != null && !oDpsEntry.getDbmsDpsLinksAsDestiny().isEmpty()) {
                            for (SDataDpsDpsLink linkedEntry : oDpsEntry.getDbmsDpsLinksAsDestiny()) {
                                
                                // crear nuevo vínculo como destino de documentos relacionados
                                SDataDpsDpsLink oNewLink = new SDataDpsDpsLink();
                                oNewLink.setPkSourceYearId(linkedEntry.getPkSourceYearId());
                                oNewLink.setPkSourceDocId(linkedEntry.getPkSourceDocId());
                                oNewLink.setPkSourceEntryId(linkedEntry.getPkSourceEntryId());
                                oNewLink.setPkDestinyYearId(linkedEntry.getPkDestinyYearId());
                                oNewLink.setPkDestinyDocId(linkedEntry.getPkDestinyDocId());
                                oNewLink.setPkDestinyEntryId(0);
                                oNewLink.setQuantity(linkedEntry.getQuantity());
                                oNewLink.setOriginalQuantity(linkedEntry.getOriginalQuantity());
                                lNewLinks.add(oNewLink);
                                
                                if (mergeVersion == 0) {
                                    
                                    // Si la versión es 0, se debe crear un registro en la tabla de merge para preservar el vínculo original
                                    oDpsOriginalLinkEty = new SDataDpsDpsMerge();
    
                                    oDpsOriginalLinkEty.setFkDpsOldYearId(linkedEntry.getPkSourceYearId());
                                    oDpsOriginalLinkEty.setFkDpsOldDocId(linkedEntry.getPkSourceDocId());
                                    oDpsOriginalLinkEty.setFkDpsOldEntryId(linkedEntry.getPkSourceEntryId());
                                    oDpsOriginalLinkEty.setFkDpsNewYearId(linkedEntry.getPkDestinyYearId());
                                    oDpsOriginalLinkEty.setFkDpsNewDocId(linkedEntry.getPkDestinyDocId());
                                    oDpsOriginalLinkEty.setFkDpsNewEntryId(linkedEntry.getPkDestinyEntryId());
                                    oDpsOriginalLinkEty.setVersion(mergeVersion);
                                    oDpsOriginalLinkEty.setQuantity(linkedEntry.getQuantity());
                                    oDpsOriginalLinkEty.setOriginalQuantity(linkedEntry.getOriginalQuantity());
                                    oDpsOriginalLinkEty.setUnitaryPrice(0d);
                                    oDpsOriginalLinkEty.setUnitaryPriceCy(0d);
    
                                    lMergedEntries.add(oDpsOriginalLinkEty);
                                }
                            }
                        }
                        
                        lDpsSelectedPastEntries.add(oDpsEntry);
                        break;
                    }
                }
            }
    
            if (oNewEntry != null) {
                oNewEntry.setPkYearId(idYear);
                oNewEntry.setPkDocId(idDoc);
                oNewEntry.setPkEntryId(0);
                if (unitaryPriceCur >= 0) {
                    oNewEntry.setSalesPriceUnitaryCy(unitaryPriceCur);
                    oNewEntry.setOriginalPriceUnitaryCy(unitaryPriceCur);
                    oNewEntry.setOriginalPriceUnitarySystemCy(unitaryPriceCur);
                }
                
                // Preparar partida, calcular totales y datos complementarios
                oNewEntry = SMergeEntriesUtils.prepareDpsEntryImport(oClient, 
                                                        oDps, 
                                                        oNewEntry,
                                                        dTotalOriginalQuantity,
                                                        isLocalCurrency,
                                                        isDiscountDocPercentage,
                                                        dDiscount,
                                                        dExchangeRate);
                if (!lNewLinks.isEmpty()) {
                    // si los vínculos provienen de una misma partida de origen, se suman las cantidades de los vínculos y se hace uno solo:
                    for (int i = 0; i < lNewLinks.size() - 1; i++) {
                        for (int j = lNewLinks.size() - 1; j > i; j--) {
                            if (lNewLinks.get(i).getPkSourceYearId() == lNewLinks.get(j).getPkSourceYearId() &&
                                lNewLinks.get(i).getPkSourceDocId() == lNewLinks.get(j).getPkSourceDocId() &&
                                lNewLinks.get(i).getPkSourceEntryId() == lNewLinks.get(j).getPkSourceEntryId()) {
                                lNewLinks.get(i).setQuantity(lNewLinks.get(i).getQuantity() + lNewLinks.get(j).getQuantity());
                                lNewLinks.get(i).setOriginalQuantity(lNewLinks.get(i).getOriginalQuantity() + lNewLinks.get(j).getOriginalQuantity());
                                lNewLinks.remove(j);
                            }
                        }
                    }
                    oNewEntry.getDbmsDpsLinksAsDestiny().addAll(lNewLinks);
                }
                oNewEntry.getDbmsDpsEntryMerges().addAll(lMergedEntries);
            }
            
            lEntriesToAdd.add(oNewEntry);
    
            for (SDataDpsEntry oDpsEntry : lDpsEntries) {
                boolean isSelected = false;
                for (SDataEntryDpsMergeRow selectedEntry : selectedEntries) {
                    if (SLibUtilities.compareKeys(selectedEntry.getDpsEntryKey(), oDpsEntry.getPrimaryKey())) {
                        isSelected = true;
                        break;
                    }
                }
                if (!isSelected) {
                    lNotSelectedEntries.add(oDpsEntry);
                }
            }
        }

        SMergeData oMergeData = new SMergeData();
        oMergeData.setMergeEntries(lMergedEntries);
        oMergeData.setTotalQuantity(dTotalQuantity);
        lEntriesToAdd.addAll(lNotSelectedEntries);
        oMergeData.setEntriesToAdd(lEntriesToAdd);
        lEntriesToDelete.addAll(lDpsSelectedPastEntries);
        oMergeData.setEntriesToDelete(lEntriesToDelete);

        return oMergeData;
    }
    
    private static SDataDpsEntry prepareDpsEntryImport(SClientInterface oClient,
                                                SDataDps oDps,
                                                SDataDpsEntry oNewEntry,
                                                double dTotalQuantity,
                                                boolean isLocalCurrency,
                                                boolean isDiscountDocPercentage,
                                                double dDiscount,
                                                double dExchangeRate) {
        
        int decs = oClient.getSessionXXX().getParamsErp().getDecimalsValue();
        SDataItem oItem = null;
        oNewEntry.setIsRegistryNew(true);
        oNewEntry.setOriginalQuantity(dTotalQuantity);
        oNewEntry.setQuantity(SLibUtilities.round(oNewEntry.getOriginalQuantity() * ((SSessionCustom) oClient.getSession().getSessionCustom()).getUnitsFactorForQuantity(oNewEntry.getFkItemId(), oNewEntry.getFkOriginalUnitId(), oNewEntry.getFkUnitId()), oClient.getSessionXXX().getParamsErp().getDecimalsQuantity()));  // required for physical units
        oNewEntry.setSurplusPercentage(0);

        if (!oNewEntry.getIsDiscountEntryPercentage()) {
            oNewEntry.setDiscountEntryCy(SLibUtilities.round(oNewEntry.getDiscountEntryCy() * dTotalQuantity, decs));
        }

        if (oDps.getIsDiscountDocApplying() && !oDps.getIsDiscountDocPercentage()) {
            oNewEntry.setDiscountDocCy(SLibUtilities.round(oNewEntry.getDiscountDocCy() * dTotalQuantity, decs));
        }

        if (oNewEntry.getIsTaxesAutomaticApplying()) {
            oNewEntry.getDbmsEntryTaxes().clear();  // taxes will be calculated again
        }
        else {
            for (SDataDpsEntryTax tax : oNewEntry.getDbmsEntryTaxes()) {
                tax.setTaxCy(SLibUtilities.round((isLocalCurrency && oDps.getFkCurrencyId() != oDps.getFkCurrencyId() ? tax.getTax() : tax.getTaxCy()) * dTotalQuantity, decs));
            }
        }

        for (SDataDpsEntryCommissions comms : oNewEntry.getDbmsEntryCommissions()) {
            comms.setCommissionsCy(SLibUtilities.round(comms.getCommissionsCy() * dTotalQuantity, decs));
        }

        //oNewEntry.getDbmsEntryNotes().clear();    // notes are imported aswell
        oNewEntry.getDbmsDpsLinksAsSource().clear();
        oNewEntry.getDbmsDpsLinksAsDestiny().clear();
        oNewEntry.getDbmsDpsAdjustmentsAsDps().clear();
        oNewEntry.getDbmsDpsAdjustmentsAsAdjustment().clear();

        // Adjust physical units:
        oItem = (SDataItem) SDataUtilities.readRegistry(oClient, SDataConstants.ITMU_ITEM, new int[]{oNewEntry.getFkItemId()}, SLibConstants.EXEC_MODE_VERBOSE);
        if (oItem != null) {
            oNewEntry.setLength(!oItem.getDbmsDataItemGeneric().getIsLengthApplying() ? 0d : oNewEntry.getQuantity() * oItem.getLength());
            oNewEntry.setSurface(!oItem.getDbmsDataItemGeneric().getIsSurfaceApplying() ? 0d : oNewEntry.getQuantity() * oItem.getSurface());
            oNewEntry.setVolume(!oItem.getDbmsDataItemGeneric().getIsVolumeApplying() ? 0d : oNewEntry.getQuantity() * oItem.getVolume());
            oNewEntry.setMass(!oItem.getDbmsDataItemGeneric().getIsMassApplying() ? 0d : oNewEntry.getQuantity() * oItem.getMass());
            oNewEntry.setWeightGross(!oItem.getDbmsDataItemGeneric().getIsWeightGrossApplying() ? 0d : oNewEntry.getQuantity() * oItem.getWeightGross());
            oNewEntry.setWeightDelivery(!oItem.getDbmsDataItemGeneric().getIsWeightDeliveryApplying() ? 0d : oNewEntry.getQuantity() * oItem.getWeightDelivery());
        }

        for (SDataDpsEntryNotes notes : oNewEntry.getDbmsEntryNotes()) {
            notes.setIsRegistryEdited(true);// force original document entry notes to be attached to new document entry even if they are not edited
            notes.setPkNotesId(0);
        }

        oNewEntry.calculateTotal(oClient,
                oDps.getDate(),
                oDps.getFkTaxIdentityEmisorTypeId(),
                oDps.getFkTaxIdentityReceptorTypeId(),
                isDiscountDocPercentage,
                dDiscount,
                dExchangeRate);
        
        return oNewEntry;
    }
}
