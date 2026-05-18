/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import cfd.ver40.DCfdi40Catalogs;
import erp.SErpConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mfin.data.SDataCostCenter;
import erp.mfin.data.SDataTaxRegion;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mitm.data.SItemUtilities;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import sa.lib.SLibUtils;

/**
 *
 * @author Isabel Servín, Sergio Flores
 */
public final class SRowCfdiImport40 extends erp.lib.table.STableRow {

    public static final int LINK_1_ON_1 = 1;
    public static final int LINK_AS_SERVICE = 2;
    
    private final SClientInterface miClient;
    private final int mnDocumentType;
    private final cfd.ver40.DElementConcepto moConcepto;
    private final int mnRowNumber;
    private final boolean mbIsInvoicedAdvance;
    
    private SDataItem moItem;
    private SDataItem moItemReference;
    private SDataUnit moUnit;
    private SDataTaxRegion moTaxRegion;
    private SDataCostCenter moCostCenter;
    private int mnOperationsType;
    private int mnCfdLinkType;
    private int[] manAdjustmentSubtypeKey;
    private String msAdjustmentSubtypeName;
    
    private ArrayList<SDataEntryDpsDpsLink> moImportedEntryDpsDpsLinks;
    private ArrayList<SDataDpsEntry> moImportedDpsEntries;
    private ArrayList<SDataDpsEntry> moNewDpsEntries;
    
    private double mdConvFactor;
    private HashSet<cfd.ver40.DElementConceptoImpuestoTraslado> moTaxChargedMatched;
    private HashSet<cfd.ver40.DElementConceptoImpuestoRetencion> moTaxRetainedMatched;
    private double mdLinkedPercentage;
    
    /**
     * Creates new CFDI 4.0 row to be imported.
     * @param client GUI client.
     * @param documentType GUI document type. Either SDataConstantsSys.TRNX_TP_DPS_DOC (invoice) or SDataConstantsSys.TRNX_TP_DPS_ADJ (credit note)
     * @param concepto CFDI Concepto.
     * @param rowNumber Row number.
     */
    public SRowCfdiImport40(final SClientInterface client, final int documentType, final cfd.ver40.DElementConcepto concepto, final int rowNumber) {
        miClient = client;
        mnDocumentType = documentType;
        moConcepto = concepto;
        mnRowNumber = rowNumber;
        mbIsInvoicedAdvance = moConcepto.getAttClaveProdServ().getString().equals(DCfdi40Catalogs.ClaveProdServServsFacturacion);
        
        resetMatchingSettings();
        prepareTableRow();
    }

    private boolean isInvoice() {
        return mnDocumentType == SDataConstantsSys.TRNX_TP_DPS_DOC;
    }
    
    public void setItem(final SDataItem o) { moItem = o; }
    public void setItemReference(final SDataItem o) { moItemReference = o; }
    public void setUnit(final SDataUnit o) { moUnit = o; }
    public void setTaxRegion(final SDataTaxRegion o) { moTaxRegion = o; }
    public void setCostCenter(final SDataCostCenter o) { moCostCenter = o; }
    public void setOperationsType(final int i) { mnOperationsType = i; }
    public void setCfdLinkType(final int i) { mnCfdLinkType = i; }
    public void setAdjustmentSubtypeKey(int[] key) { manAdjustmentSubtypeKey = key != null ? key.clone() : null; }
    public void setAdjustmentSubtypeName(String s) { msAdjustmentSubtypeName = s; }
    
    public cfd.ver40.DElementConcepto getConcepto() { return moConcepto; }
    public int getRowNumber() { return mnRowNumber; }
    public boolean isInvoicedAdvance() { return mbIsInvoicedAdvance; }
    
    public SDataItem getItem() { return moItem; }
    public SDataItem getItemReference() { return moItemReference; }
    public SDataUnit getUnit() { return moUnit; }
    public SDataTaxRegion getTaxRegion() { return moTaxRegion; }
    public SDataCostCenter getCostCenter() { return moCostCenter; }
    public int getOperationsType() { return mnOperationsType; }
    public int getCfdLinkType() { return mnCfdLinkType; }
    public int[] getAdjustmentSubtypeKey() { return manAdjustmentSubtypeKey != null ? manAdjustmentSubtypeKey.clone() : null; }
    public String getAdjustmentSubtypeName() { return msAdjustmentSubtypeName; }
    
    public ArrayList<SDataEntryDpsDpsLink> getImportedEntryDpsDpsLinks() { return moImportedEntryDpsDpsLinks; }
    public ArrayList<SDataDpsEntry> getImportedDpsEntries() { return moImportedDpsEntries; }
    public ArrayList<SDataDpsEntry> getNewDpsEntries() { return moNewDpsEntries; }
    
    public void setConvFactor(final double d) { 
        mdConvFactor = d;
        
        for (SDataDpsEntry newDpsEty : moNewDpsEntries) {
            SDataEntryDpsDpsLink entryDpsDpsLink = null;

            for (SDataEntryDpsDpsLink dpsLink : moImportedEntryDpsDpsLinks) {
                if (SLibUtils.compareKeys(dpsLink.getDpsEntryKey(), (int[]) newDpsEty.getPrimaryKey())) {
                    entryDpsDpsLink = dpsLink;
                    break;
                }
            }

            newDpsEty.setOriginalQuantity(entryDpsDpsLink == null ? getEquivalentQuantity() : entryDpsDpsLink.getQuantityToLink());
            newDpsEty.setOriginalPriceUnitaryCy(getPriceUnitary());
            newDpsEty.setOriginalPriceUnitarySystemCy(getPriceUnitary());
        }
    }
    
    public double getConvFactor() {
        return mdConvFactor;
    }
    
    public HashSet<cfd.ver40.DElementConceptoImpuestoTraslado> getTaxChargedMatched() { return moTaxChargedMatched; }
    public HashSet<cfd.ver40.DElementConceptoImpuestoRetencion> getTaxRetainedMatched() { return moTaxRetainedMatched; }
    
    public void addTaxChargedMatched(cfd.ver40.DElementConceptoImpuestoTraslado traslado) {
        boolean found = false;
        
        for (cfd.ver40.DElementConceptoImpuestoTraslado tax : moTaxChargedMatched) {
            if (tax.getAttImpuesto().getString().equals(traslado.getAttImpuesto().getString()) &&
                    tax.getAttTipoFactor().getString().equals(traslado.getAttTipoFactor().getString()) &&
                    tax.getAttTasaOCuota().getDouble() == traslado.getAttTasaOCuota().getDouble()) {
                found = true;
                break;
            }
        }
        
        if (!found) {
            moTaxChargedMatched.add(traslado);
        }
    }
    
    public void addTaxRetainedMatched(cfd.ver40.DElementConceptoImpuestoRetencion retención) {
        boolean found = false;
        
        for (cfd.ver40.DElementConceptoImpuestoRetencion tax : moTaxRetainedMatched) {
            if (tax.getAttImpuesto().getString().equals(retención.getAttImpuesto().getString()) &&
                    tax.getAttTipoFactor().getString().equals(retención.getAttTipoFactor().getString()) &&
                    tax.getAttTasaOCuota().getDouble() == retención.getAttTasaOCuota().getDouble()) {
                found = true;
                break;
            }
        }
        
        if (!found) {
            moTaxRetainedMatched.add(retención);
        }
    }
    
    public double getEquivalentQuantity() {
        return moConcepto.getAttCantidad().getDouble() * mdConvFactor;
    }
    
    public double getPriceUnitary() {
        return SLibUtils.round((moConcepto.getAttCantidad().getDouble() * moConcepto.getAttValorUnitario().getDouble()) / getEquivalentQuantity(), SErpConsts.VAL_QTY_MAX_DECS);
    }
    
    public double getServicePriceUnitary(SDataDpsEntry importedDps) {
        /*
        Se toma el subtotal, ya que en los servicios solo se puede vincular partidas con cantidad 1,
        por lo que es equivalente a tomar el precio unitario, pero se contempla la posibilidad de un descuento.
        */
        return importedDps != null ? SLibUtils.round(importedDps.getSubtotalCy_r() * mdLinkedPercentage, SErpConsts.VAL_QTY_MAX_DECS) : getPriceUnitary();
    }
    
    /**
     * Verifica si el renglón está vinculado como servicio.
     * @return 
     */
    public boolean isLinkedAsService() {
        return mnCfdLinkType == LINK_AS_SERVICE;
    }
    
    /**
     * Borra todos los componentes de la clase
     */
    public void resetMatchingSettings() {
        moNewDpsEntries = new ArrayList<>();
        moItem = null;
        moItemReference = null;
        moUnit = null;
        moTaxRegion = null;
        moCostCenter = null;
        moImportedEntryDpsDpsLinks = new ArrayList<>();
        moImportedDpsEntries = new ArrayList<>();
        mnOperationsType = 0;
        mnCfdLinkType = LINK_1_ON_1;
        manAdjustmentSubtypeKey = null;
        msAdjustmentSubtypeName = "";

        mdConvFactor = 1;
        
        moTaxChargedMatched = new HashSet<>();
        moTaxRetainedMatched = new HashSet<>();
    }
    
    private void updateDpsDpsSupplyAsDestiny() {
        if (!moImportedEntryDpsDpsLinks.isEmpty()) {
            for (int i = 0; i < moImportedEntryDpsDpsLinks.size(); i++) {
                SDataEntryDpsDpsLink importedEntryDpsDpsLink = moImportedEntryDpsDpsLinks.get(i);
                
                SDataDpsDpsLink link = new SDataDpsDpsLink();
                link.setPkSourceYearId(importedEntryDpsDpsLink.getPkYearId());
                link.setPkSourceDocId(importedEntryDpsDpsLink.getPkDocId());
                link.setPkSourceEntryId(importedEntryDpsDpsLink.getPkEntryId());
                //link.setPkDestinyYearId(...);
                //link.setPkDestinyDocId(...);
                //link.setPkDestinyEntryId(...);
                link.setOriginalQuantity(importedEntryDpsDpsLink.getQuantityToLink()); 
                
                try {
                    SDataDpsEntry entry = moNewDpsEntries.get(i);
                    entry.getDbmsDpsLinksAsDestiny().clear();
                    entry.getDbmsDpsLinksAsDestiny().add(link);
                }
                catch (Exception e) {
                    SLibUtils.printException(this, e);
                }
            }
        }
    }
    
    private void updateDpsDpsAdjustmentAsAdjustment() {
        if (!moImportedEntryDpsDpsLinks.isEmpty()) {
            for (int i = 0; i < moImportedEntryDpsDpsLinks.size(); i++) {
                SDataEntryDpsDpsLink importedEntryDpsDpsLink = moImportedEntryDpsDpsLinks.get(i);
                
                SDataDpsDpsAdjustment adjustment = new SDataDpsDpsAdjustment();
                adjustment.setPkDpsYearId(importedEntryDpsDpsLink.getPkYearId());
                adjustment.setPkDpsDocId(importedEntryDpsDpsLink.getPkDocId());
                adjustment.setPkDpsEntryId(importedEntryDpsDpsLink.getPkEntryId());
                //adjustment.setPkAdjustmentYearId(...);
                //adjustment.setPkAdjustmentDocId(...);
                //adjustment.setPkAdjustmentEntryId(...);
                adjustment.setOriginalQuantity(importedEntryDpsDpsLink.getQuantityToLink()); 
                
                try {
                    SDataDpsEntry entry = moNewDpsEntries.get(i);
                    entry.getDbmsDpsAdjustmentsAsAdjustment().clear();
                    entry.getDbmsDpsAdjustmentsAsAdjustment().add(adjustment);
                }
                catch (Exception e) {
                    SLibUtils.printException(this, e);
                }
            }
        }
    }
    
    /**
     * Refresca el registro DpsEntry a partir de los datos y componentes ya asignados.
     */
    public void refreshDpsEntries() {
        moNewDpsEntries.clear();
        
        if (moImportedDpsEntries.isEmpty()) {
            SDataDpsEntry newDpsEntry = createNewDpsEntry(null);
            newDpsEntry.setOriginalQuantity(getEquivalentQuantity());
            
            newDpsEntry.setFkCostCenterId_n(moCostCenter == null ? "" : moCostCenter.getPkCostCenterIdXXX());
            newDpsEntry.setDbmsCostCenterCode(moCostCenter == null ? "" : moCostCenter.getCode());
            newDpsEntry.setDbmsCostCenter_n(moCostCenter == null ? "" : moCostCenter.getCostCenter());
            moNewDpsEntries.add(newDpsEntry);
        }
        else {
            if (isLinkedAsService()) {
                mdLinkedPercentage = STrnDpsUtilities.calculateLinkedServicePct(moImportedDpsEntries, moConcepto.getAttValorUnitario().getDouble());
            }
            
            for (SDataDpsEntry importedDps : moImportedDpsEntries) {
                SDataEntryDpsDpsLink entryDpsDpsLink = null;
                
                for (SDataEntryDpsDpsLink dpsLink : moImportedEntryDpsDpsLinks) {
                    if (SLibUtils.compareKeys(dpsLink.getDpsEntryKey(), (int[]) importedDps.getPrimaryKey())) {
                        entryDpsDpsLink = dpsLink;
                        break;
                    }
                }
                
                SDataDpsEntry newDpsEntry = createNewDpsEntry(importedDps);
                newDpsEntry.setOriginalQuantity(entryDpsDpsLink == null ? getEquivalentQuantity() : entryDpsDpsLink.getQuantityToLink());
                
                SDataCostCenter costCenter = (SDataCostCenter) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_CC, new String[] { importedDps.getFkCostCenterId_n() }, SLibConstants.EXEC_MODE_SILENT);
                newDpsEntry.setFkCostCenterId_n(costCenter == null ? "" : costCenter.getPkCostCenterIdXXX());
                newDpsEntry.setDbmsCostCenterCode(costCenter == null ? "" : costCenter.getCode());
                newDpsEntry.setDbmsCostCenter_n(costCenter == null ? "" : costCenter.getCostCenter());

                newDpsEntry.setDbmsDpsEntryMatRequest(importedDps.getDbmsDpsEntryMatRequestLink());
                moNewDpsEntries.add(newDpsEntry);
            }
            
            if (isInvoice()) {
                updateDpsDpsSupplyAsDestiny();
            }
            else {
                updateDpsDpsAdjustmentAsAdjustment();
            }
        }
    }
    
    private SDataDpsEntry createNewDpsEntry(SDataDpsEntry importedDps) {
        SDataDpsEntry newDpsEntry = new SDataDpsEntry();
        
        /*
        newDpsEntry.setPkYearId(...);
        newDpsEntry.setPkDocId(...);
        newDpsEntry.setPkEntryId(...)
        */
        newDpsEntry.setConceptKey(moItem == null ? "" : moItem.getKey());
        newDpsEntry.setConcept(moItem == null ? "" : moItem.getItem());
        /*
        newDpsEntry.setReference(...);
        newDpsEntry.setQuantity(...);
        */
        newDpsEntry.setIsDiscountDocApplying(moConcepto.getAttDescuento().getDouble() != 0);
        newDpsEntry.setIsDiscountUnitaryPercentage(false); 
        newDpsEntry.setIsDiscountUnitaryPercentageSystem(false);
        newDpsEntry.setIsDiscountEntryPercentage(false);
        newDpsEntry.setDiscountUnitaryPercentage(0.0);
        newDpsEntry.setDiscountUnitaryPercentageSystem(0.0);
        newDpsEntry.setDiscountEntryPercentage(0.0);
        /*
        newDpsEntry.setPriceUnitary(...);
        newDpsEntry.setPriceUnitarySystem(...);
        newDpsEntry.setDiscountUnitary(...);
        newDpsEntry.setDiscountUnitarySystem(...);
        newDpsEntry.setDiscountEntry(...);
        newDpsEntry.setSubtotalProvisional_r(...);
        newDpsEntry.setDiscountDoc(...);
        newDpsEntry.setSubtotal_r(...);
        newDpsEntry.setTaxCharged_r(...);
        newDpsEntry.setTaxRetained_r(...);
        newDpsEntry.setTotal_r(...);
        newDpsEntry.setPriceUnitaryReal_r(...);
        newDpsEntry.setCommissions_r(...);
        newDpsEntry.setPriceUnitaryCy(...);
        newDpsEntry.setPriceUnitarySystemCy(...);
        newDpsEntry.setDiscountUnitaryCy(...);
        newDpsEntry.setDiscountUnitarySystemCy(...);
        newDpsEntry.setDiscountEntryCy(...);
        newDpsEntry.setSubtotalProvisionalCy_r(...);
        */
        newDpsEntry.setDiscountDocCy(moConcepto.getAttDescuento().getDouble());
        /*
        newDpsEntry.setSubtotalCy_r(...);
        newDpsEntry.setTaxChargedCy_r(...) 
        newDpsEntry.setTaxRetainedCy_r(...) 
        newDpsEntry.setTotalCy_r(...);
        newDpsEntry.setPriceUnitaryRealCy_r(...);
        newDpsEntry.setCommissionsCy_r(...);
        */
        newDpsEntry.setOriginalPriceUnitaryCy(!isLinkedAsService() ? getPriceUnitary() : getServicePriceUnitary(importedDps));
        newDpsEntry.setOriginalPriceUnitarySystemCy(!isLinkedAsService() ? getPriceUnitary() : getServicePriceUnitary(importedDps));
        //newDpsEntry.setOriginalDiscountUnitaryCy(moConcepto.getAttDescuento().getDouble());
        //newDpsEntry.setOriginalDiscountUnitarySystemCy(moConcepto.getAttDescuento().getDouble());
        /*
        newDpsEntry.setSalesPriceUnitaryCy(...);
        newDpsEntry.setSalesFreightUnitaryCy(...);
        */
        newDpsEntry.setLength(0.0);
        newDpsEntry.setSurface(0.0);
        newDpsEntry.setVolume(0.0);
        newDpsEntry.setMass(0.0);
        newDpsEntry.setWeightPackagingExtra(0.0);
        newDpsEntry.setWeightGross(0.0);
        newDpsEntry.setWeightDelivery(0.0);
        newDpsEntry.setSurplusPercentage(0.0);
        newDpsEntry.setContractBase(0.0);
        newDpsEntry.setContractFuture(0.0);
        newDpsEntry.setContractFactor(0.0);
        newDpsEntry.setContractPriceYear(0);
        newDpsEntry.setContractPriceMonth(0);
        newDpsEntry.setSealQuality("");
        newDpsEntry.setSealSecurity("");
        newDpsEntry.setDriver("");
        newDpsEntry.setPlate("");
        newDpsEntry.setTicket("");
        newDpsEntry.setContainerTank("");
        newDpsEntry.setVgm("");
        newDpsEntry.setOperationsType(mnOperationsType); 
        newDpsEntry.setUserId(0);
        newDpsEntry.setSortingPosition(mnRowNumber); 
        newDpsEntry.setIsPrepayment(moItem == null ? false : moItem.getIsPrepayment());
        newDpsEntry.setIsDiscountRetailChain(false);
        newDpsEntry.setIsTaxesAutomaticApplying(true);
        newDpsEntry.setIsPriceVariable(false);
        newDpsEntry.setIsPriceConfirm(false);
        newDpsEntry.setIsSalesFreightRequired(false);
        newDpsEntry.setIsSalesFreightConfirm(false);
        newDpsEntry.setIsSalesFreightAdd(false);
        newDpsEntry.setIsInventoriable(moItem == null ? false : moItem.getIsInventoriable()); 
        newDpsEntry.setIsDeleted(false);
        newDpsEntry.setFkItemId(moItem == null ? 0 : moItem.getPkItemId());
        newDpsEntry.setFkUnitId(moItem == null ? 0 : moItem.getFkUnitId());
        newDpsEntry.setFkOriginalUnitId(moUnit == null ? 0 : moUnit.getPkUnitId());
        newDpsEntry.setFkTaxRegionId(moTaxRegion == null ? 0 :moTaxRegion.getPkTaxRegionId());
        //newDpsEntry.setFkThirdTaxCausingId_n(...);
        if (manAdjustmentSubtypeKey != null) {
            newDpsEntry.setFkDpsAdjustmentTypeId(manAdjustmentSubtypeKey[0]);
            newDpsEntry.setFkDpsAdjustmentSubtypeId(manAdjustmentSubtypeKey[1]);
        }
        else {
            newDpsEntry.setFkDpsAdjustmentTypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[0]);
            newDpsEntry.setFkDpsAdjustmentSubtypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[1]);
        }
        newDpsEntry.setFkDpsEntryTypeId(SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY);
        /*
        newDpsEntry.setFkVehicleTypeId_n(...);
        newDpsEntry.setFkCashCompanyBranchId_n(...);
        newDpsEntry.setFkCashAccountId_n(...);
        */
        newDpsEntry.setFkItemRefId_n(moItemReference == null ? 0 : moItemReference.getPkItemId());
        newDpsEntry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        newDpsEntry.setDbmsOriginalUnitSymbol(moUnit == null ? "" : moUnit.getSymbol());
        newDpsEntry.setDbmsTaxRegion(moTaxRegion == null ? "" : moTaxRegion.getTaxRegion());
        newDpsEntry.setDbmsItemRef_n(moItemReference == null ? "" : moItemReference.getItem()); 
        
        newDpsEntry.setAuxIsLinkedAsService(isLinkedAsService());
        
        return newDpsEntry;
    }
    
    /**
     * Calcula el total de los impuestos del DpsEntry.
     * @param date
     * @param idyEmisor
     * @param exchangeRate
     * @param idyReceptor
     */
    public void calculateTotalDpsEntries(Date date, int idyEmisor, int idyReceptor, double exchangeRate) {
        if (!moNewDpsEntries.isEmpty()) {
            for (SDataDpsEntry moNewDpsEntry : moNewDpsEntries) {
                moNewDpsEntry.calculateTotal(miClient, date, idyEmisor, idyReceptor, false, 0, exchangeRate);
                moNewDpsEntry.setIsTaxesAutomaticApplying(false);
            }
            moTaxChargedMatched.clear();
            moTaxRetainedMatched.clear();
        }
    }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();        
        
        // CFDI cols:
        
        mvValues.add(mnRowNumber); //#
        mvValues.add(moConcepto.getAttNoIdentificacion().getString());
        mvValues.add(moConcepto.getAttDescripcion().getString());
        mvValues.add(moConcepto.getAttClaveProdServ().getString());
        mvValues.add(moConcepto.getAttCantidad().getDouble());
        mvValues.add(moConcepto.getAttUnidad().getString());
        mvValues.add(moConcepto.getAttClaveUnidad().getString());
        
        // Matching cols:
        
        String claveProdServ = "";
        
        if (moItem != null && moUnit != null) {
            try {
                claveProdServ = SItemUtilities.getClaveProdServ(miClient.getSession().getStatement(), moItem.getCfdProdServId());
            }
            catch (Exception e) {
                SLibUtils.printException(this, e);
            }
        }
        
        mvValues.add(moItem == null ? "" : moItem.getKey()); // Clave ítem
        mvValues.add(moItem == null ? "" : moItem.getItem()); // Nombre ítem
        mvValues.add(claveProdServ); // ProdServ SAT
        mvValues.add(moItem == null ? "" : moItem.getDbmsDataUnit().getSymbol()); // Unidad ítem
        mvValues.add(mdConvFactor); // Factor de conversion
        mvValues.add(getEquivalentQuantity()); // Cantidad equivalente
        mvValues.add(moUnit == null ? "" : moUnit.getSymbol()); // Unidad
        mvValues.add(moUnit == null ? "" : moUnit.getDbmsClaveUnidad()); // Unidad SAT
        mvValues.add(moTaxRegion == null ? "" : moTaxRegion.getTaxRegion()); // Región de impuestos
        mvValues.add(mnOperationsType == 0 ? "" : (isInvoice() ? SDataConstantsSys.OperationsTypesOpsMap.get(mnOperationsType) : SDataConstantsSys.OperationsTypesAdjMap.get(mnOperationsType))); // Tipo de operación
        mvValues.add(moCostCenter == null ? "" : moCostCenter.getPkCostCenterIdXXX()); // Clave centro costo
        mvValues.add(moCostCenter == null ? "" : moCostCenter.getCostCenter()); // Centro costo
        mvValues.add(moItemReference == null ? "" : moItemReference.getKey()); // Clave ítem de referencia
        mvValues.add(moItemReference == null ? "" : moItemReference.getItem()); // ítem de referencia
        
        // CFDI (complemento) cols:
        
        mvValues.add(getPriceUnitary()); // Valor unitario
        mvValues.add(moConcepto.getAttImporte().getDouble()); // Importe
        mvValues.add(moConcepto.getAttDescuento().getDouble()); // Descuento
        
        // Other cols:
        
        mvValues.add(msAdjustmentSubtypeName); // Subtipo de ajuste
    }
}
