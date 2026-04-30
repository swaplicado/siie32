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
    private final cfd.ver40.DElementConcepto moConcepto;
    private final int mnRowNumber;
    private final boolean mbIsInvoicedAdvance;
    
    private SDataItem moItem;
    private SDataItem moItemReference;
    private SDataUnit moUnit;
    private SDataTaxRegion moTaxRegion;
    private SDataCostCenter moCostCenter;
    private int mnOperationTypePk;
    private int mnCfdLinkType;
    
    private ArrayList<SDataEntryDpsDpsLink> moImportedEntryDpsDpsLinks;
    private ArrayList<SDataDpsEntry> moImportedDpsEntries;
    private ArrayList<SDataDpsEntry> moNewDpsEntries;
    
    private double mdConvFactor;
    private HashSet<cfd.ver40.DElementConceptoImpuestoTraslado> moTaxChargedMatched;
    private HashSet<cfd.ver40.DElementConceptoImpuestoRetencion> moTaxRetainedMatched;
    private double mdLinkedPercentage;
    
    /**
     * 
     * @param client Cliente GUI.
     * @param concepto Concepto del CFDI.
     * @param rowNumber Row number.
     */
    public SRowCfdiImport40(SClientInterface client, cfd.ver40.DElementConcepto concepto, int rowNumber) {
        miClient = client;
        moConcepto = concepto;
        mnRowNumber = rowNumber;
        mbIsInvoicedAdvance = moConcepto.getAttClaveProdServ().getString().equals(DCfdi40Catalogs.ClaveProdServServsFacturacion);
        
        resetMatchingSettings();
        prepareTableRow();
    }

    public void setItem(final SDataItem o) { moItem = o; }
    public void setItemReference(final SDataItem o) { moItemReference = o; }
    public void setUnit(final SDataUnit o) { moUnit = o; }
    public void setTaxRegion(final SDataTaxRegion o) { moTaxRegion = o; }
    public void setCostCenter(final SDataCostCenter o) { moCostCenter = o; }
    public void setOperationTypePk(final int i) { mnOperationTypePk = i; }
    public void setCfdLinkType(final int i) { mnCfdLinkType = i; }
    
    public cfd.ver40.DElementConcepto getConcepto() { return moConcepto; }
    public int getRowNumber() { return mnRowNumber; }
    public boolean isInvoicedAdvance() { return mbIsInvoicedAdvance; }
    
    public SDataItem getItem() { return moItem; }
    public SDataItem getItemReference() { return moItemReference; }
    public SDataUnit getUnit() { return moUnit; }
    public SDataTaxRegion getTaxRegion() { return moTaxRegion; }
    public SDataCostCenter getCostCenter() { return moCostCenter; }
    public int getOperationTypePk() { return mnOperationTypePk; }
    public int getCfdLinkType() { return mnCfdLinkType; }
    
    public ArrayList<SDataEntryDpsDpsLink> getImportedEntryDpsDpsLinks() { return moImportedEntryDpsDpsLinks; }
    public ArrayList<SDataDpsEntry> getImportedDpsEntries() { return moImportedDpsEntries; }
    public ArrayList<SDataDpsEntry> getNewDpsEntries() { return moNewDpsEntries; }
    
    public void setConvFactor(final double d) { 
        mdConvFactor = d; 
        if (!moNewDpsEntries.isEmpty()) {
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
    }
    
    public double getConvFactor() {
        return mdConvFactor;
    }
    
    public HashSet<cfd.ver40.DElementConceptoImpuestoTraslado> getTaxChargedMatched() { return moTaxChargedMatched; }
    public HashSet<cfd.ver40.DElementConceptoImpuestoRetencion> getTaxRetainedMatched() { return moTaxRetainedMatched; }
    
    public void addTaxChargedMatched(cfd.ver40.DElementConceptoImpuestoTraslado o) {
        boolean found = false;
        
        for (cfd.ver40.DElementConceptoImpuestoTraslado tax : moTaxChargedMatched) {
            if (tax.getAttImpuesto().getString().equals(o.getAttImpuesto().getString()) &&
                    tax.getAttTipoFactor().getString().equals(o.getAttTipoFactor().getString()) &&
                    tax.getAttTasaOCuota().getDouble() == o.getAttTasaOCuota().getDouble()) {
                found = true;
                break;
            }
        }
        
        if (!found) {
            moTaxChargedMatched.add(o);
        }
    }
    
    public void addTaxRetainedMatched(cfd.ver40.DElementConceptoImpuestoRetencion o) {
        boolean found = false;
        
        for (cfd.ver40.DElementConceptoImpuestoRetencion tax : moTaxRetainedMatched) {
            if (tax.getAttImpuesto().getString().equals(o.getAttImpuesto().getString()) &&
                    tax.getAttTipoFactor().getString().equals(o.getAttTipoFactor().getString()) &&
                    tax.getAttTasaOCuota().getDouble() == o.getAttTasaOCuota().getDouble()) {
                found = true;
                break;
            }
        }
        
        if (!found) {
            moTaxRetainedMatched.add(o);
        }
    }
    
    public double getEquivalentQuantity() {
        return moConcepto.getAttCantidad().getDouble() * mdConvFactor;
    }
    
    public double getPriceUnitary() {
        return SLibUtils.round((moConcepto.getAttCantidad().getDouble() * moConcepto.getAttValorUnitario().getDouble()) / getEquivalentQuantity(), SErpConsts.VAL_QTY_MAX_DECS);
    }
    
    public double getServicePriceUnitary(SDataDpsEntry importedDps) {
        /* Se toma el subtotal ya que en los servicios solo se puede vincular partidas con cantidad 1, por lo que es equivalente a tomar el precio unitario 
            pero se contempla la posibilidad de un descuento */
        return importedDps != null ? SLibUtils.round(importedDps.getSubtotalCy_r() * mdLinkedPercentage, SErpConsts.VAL_QTY_MAX_DECS) : getPriceUnitary();
    }
    
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
        mnOperationTypePk = 0;
        mdConvFactor = 1;
        
        moTaxChargedMatched = new HashSet<>();
        moTaxRetainedMatched = new HashSet<>();
    }
    
    /**
     * Asigna Asigna todos los valores de la clase.
     * @param item
     * @param unit
     * @param taxRegion
     * @param costCenter
     * @param reference
     */
    public void setMatchingSettings(final SDataItem item, final SDataUnit unit, final SDataTaxRegion taxRegion, final SDataCostCenter costCenter, final SDataItem reference) {
        moNewDpsEntries = new ArrayList<>();
        moItem = item;
        moItemReference = reference;
        moUnit = unit;
        moTaxRegion = taxRegion;
        moCostCenter = costCenter;
        moImportedEntryDpsDpsLinks = new ArrayList<>();
        moImportedDpsEntries = new ArrayList<>();
        mnOperationTypePk = SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS;
        mdConvFactor = 1;
    }
    
    /**
     * Refresca el registro DpsEntry a partir de los datos y componentes ya asignados.
     */
    
    public void refreshDpsEntries() {
        moNewDpsEntries.clear();
        
        if (moImportedDpsEntries.isEmpty()) {
            SDataDpsEntry moNewDpsEntry = createNewDpsEntry(null);
            moNewDpsEntry.setOriginalQuantity(getEquivalentQuantity());
            
            moNewDpsEntry.setFkCostCenterId_n(moCostCenter == null ? "" : moCostCenter.getPkCostCenterIdXXX());
            moNewDpsEntry.setDbmsCostCenterCode(moCostCenter == null ? "" : moCostCenter.getCode());
            moNewDpsEntry.setDbmsCostCenter_n(moCostCenter == null ? "" : moCostCenter.getCostCenter());
            moNewDpsEntries.add(moNewDpsEntry);
        }
        else {
            if (isLinkedAsService()) {
                mdLinkedPercentage = STrnDpsUtilities.calculateLinkedServicePct(moImportedDpsEntries, moConcepto.getAttValorUnitario().getDouble());
            }
            
            for (SDataDpsEntry importedDps : moImportedDpsEntries) {
                SDataDpsEntry moNewDpsEntry = createNewDpsEntry(importedDps);
                SDataEntryDpsDpsLink entryDpsDpsLink = null;
                for (SDataEntryDpsDpsLink dpsLink : moImportedEntryDpsDpsLinks) {
                    if (SLibUtils.compareKeys(dpsLink.getDpsEntryKey(), (int[]) importedDps.getPrimaryKey())) {
                        entryDpsDpsLink = dpsLink;
                        break;
                    }
                }
                moNewDpsEntry.setOriginalQuantity(entryDpsDpsLink == null ? getEquivalentQuantity() : entryDpsDpsLink.getQuantityToLink());
                
                SDataCostCenter costCenter = (SDataCostCenter) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_CC, new String[] { importedDps.getFkCostCenterId_n() }, SLibConstants.EXEC_MODE_SILENT);
                moNewDpsEntry.setFkCostCenterId_n(costCenter == null ? "" : costCenter.getPkCostCenterIdXXX());
                moNewDpsEntry.setDbmsCostCenterCode(costCenter == null ? "" : costCenter.getCode());
                moNewDpsEntry.setDbmsCostCenter_n(costCenter == null ? "" : costCenter.getCostCenter());

                moNewDpsEntry.setDbmsDpsEntryMatRequest(importedDps.getDbmsDpsEntryMatRequestLink());
                moNewDpsEntries.add(moNewDpsEntry);
            }
            updateDpsDpsLinkAsDestiny();
        }
    }
    
    private SDataDpsEntry createNewDpsEntry(SDataDpsEntry importedDps) {
        SDataDpsEntry moNewDpsEntry = new SDataDpsEntry();
        
        /*
        moDpsEntry.setPkYearId(...);
        moDpsEntry.setPkDocId(...);
        moDpsEntry.setPkEntryId(...)
        */
        moNewDpsEntry.setConceptKey(moItem == null ? "" : moItem.getKey());
        moNewDpsEntry.setConcept(moItem == null ? "" : moItem.getItem());
        /*
        moDpsEntry.setReference(...);
        moDpsEntry.setQuantity(...);
        */
        moNewDpsEntry.setIsDiscountDocApplying(moConcepto.getAttDescuento().getDouble() != 0);
        moNewDpsEntry.setIsDiscountUnitaryPercentage(false); 
        moNewDpsEntry.setIsDiscountUnitaryPercentageSystem(false);
        moNewDpsEntry.setIsDiscountEntryPercentage(false);
        moNewDpsEntry.setDiscountUnitaryPercentage(0.0);
        moNewDpsEntry.setDiscountUnitaryPercentageSystem(0.0);
        moNewDpsEntry.setDiscountEntryPercentage(0.0);
        /*
        moDpsEntry.setPriceUnitary(...);
        moDpsEntry.setPriceUnitarySystem(...);
        moDpsEntry.setDiscountUnitary(...);
        moDpsEntry.setDiscountUnitarySystem(...);
        moDpsEntry.setDiscountEntry(...);
        moDpsEntry.setSubtotalProvisional_r(...);
        moDpsEntry.setDiscountDoc(...);
        moDpsEntry.setSubtotal_r(...);
        moDpsEntry.setTaxCharged_r(...);
        moDpsEntry.setTaxRetained_r(...);
        moDpsEntry.setTotal_r(...);
        moDpsEntry.setPriceUnitaryReal_r(...);
        moDpsEntry.setCommissions_r(...);
        moDpsEntry.setPriceUnitaryCy(...);
        moDpsEntry.setPriceUnitarySystemCy(...);
        moDpsEntry.setDiscountUnitaryCy(...);
        moDpsEntry.setDiscountUnitarySystemCy(...);
        moDpsEntry.setDiscountEntryCy(...);
        moDpsEntry.setSubtotalProvisionalCy_r(...);
        */
        moNewDpsEntry.setDiscountDocCy(moConcepto.getAttDescuento().getDouble());
        /*
        moDpsEntry.setSubtotalCy_r(...);
        moDpsEntry.setTaxChargedCy_r(...) 
        moDpsEntry.setTaxRetainedCy_r(...) 
        moDpsEntry.setTotalCy_r(...);
        moDpsEntry.setPriceUnitaryRealCy_r(...);
        moDpsEntry.setCommissionsCy_r(...);
        */
        moNewDpsEntry.setOriginalPriceUnitaryCy(!isLinkedAsService() ? getPriceUnitary() : getServicePriceUnitary(importedDps));
        moNewDpsEntry.setOriginalPriceUnitarySystemCy(!isLinkedAsService() ? getPriceUnitary() : getServicePriceUnitary(importedDps));
        //moDpsEntry.setOriginalDiscountUnitaryCy(moConcepto.getAttDescuento().getDouble());
        //moDpsEntry.setOriginalDiscountUnitarySystemCy(moConcepto.getAttDescuento().getDouble());
        /*
        moDpsEntry.setSalesPriceUnitaryCy(...);
        moDpsEntry.setSalesFreightUnitaryCy(...);
        */
        moNewDpsEntry.setLength(0.0);
        moNewDpsEntry.setSurface(0.0);
        moNewDpsEntry.setVolume(0.0);
        moNewDpsEntry.setMass(0.0);
        moNewDpsEntry.setWeightPackagingExtra(0.0);
        moNewDpsEntry.setWeightGross(0.0);
        moNewDpsEntry.setWeightDelivery(0.0);
        moNewDpsEntry.setSurplusPercentage(0.0);
        moNewDpsEntry.setContractBase(0.0);
        moNewDpsEntry.setContractFuture(0.0);
        moNewDpsEntry.setContractFactor(0.0);
        moNewDpsEntry.setContractPriceYear(0);
        moNewDpsEntry.setContractPriceMonth(0);
        moNewDpsEntry.setSealQuality("");
        moNewDpsEntry.setSealSecurity("");
        moNewDpsEntry.setDriver("");
        moNewDpsEntry.setPlate("");
        moNewDpsEntry.setTicket("");
        moNewDpsEntry.setContainerTank("");
        moNewDpsEntry.setVgm("");
        moNewDpsEntry.setOperationsType(mnOperationTypePk); 
        moNewDpsEntry.setUserId(0);
        moNewDpsEntry.setSortingPosition(mnRowNumber); 
        moNewDpsEntry.setIsPrepayment(false);
        moNewDpsEntry.setIsDiscountRetailChain(false);
        moNewDpsEntry.setIsTaxesAutomaticApplying(true);
        moNewDpsEntry.setIsPriceVariable(false);
        moNewDpsEntry.setIsPriceConfirm(false);
        moNewDpsEntry.setIsSalesFreightRequired(false);
        moNewDpsEntry.setIsSalesFreightConfirm(false);
        moNewDpsEntry.setIsSalesFreightAdd(false);
        moNewDpsEntry.setIsInventoriable(moItem == null ? false : moItem.getIsInventoriable()); 
        moNewDpsEntry.setIsDeleted(false);
        moNewDpsEntry.setFkItemId(moItem == null ? 0 : moItem.getPkItemId());
        moNewDpsEntry.setFkUnitId(moItem == null ? 0 : moItem.getFkUnitId());
        moNewDpsEntry.setFkOriginalUnitId(moUnit == null ? 0 : moUnit.getPkUnitId());
        moNewDpsEntry.setFkTaxRegionId(moTaxRegion == null ? 0 :moTaxRegion.getPkTaxRegionId());
        //moDpsEntry.setFkThirdTaxCausingId_n(...);
        moNewDpsEntry.setFkDpsAdjustmentTypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[0]);
        moNewDpsEntry.setFkDpsAdjustmentSubtypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[1]);
        moNewDpsEntry.setFkDpsEntryTypeId(SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY);
        /*
        moDpsEntry.setFkVehicleTypeId_n(...);
        moDpsEntry.setFkCashCompanyBranchId_n(...);
        moDpsEntry.setFkCashAccountId_n(...);
        */
        moNewDpsEntry.setFkItemRefId_n(moItemReference == null ? 0 : moItemReference.getPkItemId());
        moNewDpsEntry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        moNewDpsEntry.setDbmsOriginalUnitSymbol(moUnit == null ? "" : moUnit.getSymbol());
        moNewDpsEntry.setDbmsTaxRegion(moTaxRegion == null ? "" : moTaxRegion.getTaxRegion());
        moNewDpsEntry.setDbmsItemRef_n(moItemReference == null ? "" : moItemReference.getItem()); 
        
        moNewDpsEntry.setAuxIsLinkedAsService(isLinkedAsService());
        
        return moNewDpsEntry;
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
    
    private void updateDpsDpsLinkAsDestiny() {
        if (!moImportedEntryDpsDpsLinks.isEmpty()) {
            for (int i = 0; i < moImportedEntryDpsDpsLinks.size(); i++) {
                SDataEntryDpsDpsLink moImportedEntryDpsDpsLink = moImportedEntryDpsDpsLinks.get(i);
                SDataDpsDpsLink dpsLink = new SDataDpsDpsLink();
                dpsLink.setPkSourceYearId(moImportedEntryDpsDpsLink.getPkYearId());
                dpsLink.setPkSourceDocId(moImportedEntryDpsDpsLink.getPkDocId());
                dpsLink.setPkSourceEntryId(moImportedEntryDpsDpsLink.getPkEntryId());
                dpsLink.setOriginalQuantity(moImportedEntryDpsDpsLink.getQuantityToLink()); 
                
                try {
                    moNewDpsEntries.get(i).getDbmsDpsLinksAsDestiny().clear();
                    moNewDpsEntries.get(i).getDbmsDpsLinksAsDestiny().add(dpsLink);
                }
                catch (Exception e) {}
            }
        }
    }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();        
        
        // CFDI:
        
        mvValues.add(mnRowNumber); //#
        mvValues.add(moConcepto.getAttNoIdentificacion().getString());
        mvValues.add(moConcepto.getAttDescripcion().getString());
        mvValues.add(moConcepto.getAttClaveProdServ().getString());
        mvValues.add(moConcepto.getAttCantidad().getDouble());
        mvValues.add(moConcepto.getAttUnidad().getString());
        mvValues.add(moConcepto.getAttClaveUnidad().getString());
        
        // SIIE:
        
        String claveProdServ = "";
        
        if (moItem != null && moUnit != null) {
            try {
                claveProdServ = SItemUtilities.getClaveProdServ(miClient.getSession().getStatement(), moItem.getCfdProdServId());
            }
            catch (Exception e) {
                SLibUtils.printException(this, e);
            }
        }
        
        mvValues.add(moItem == null ? "" : moItem.getKey()); // Código ítem
        mvValues.add(moItem == null ? "" : moItem.getItem()); // Nombre ítem
        mvValues.add(claveProdServ); // ProdServ SAT
        mvValues.add(moItem == null ? "" : moItem.getDbmsDataUnit().getSymbol()); // Unidad ítem
        mvValues.add(mdConvFactor); // Factor de conversion
        mvValues.add(getEquivalentQuantity()); // Cantidad equivalente
        mvValues.add(moUnit == null ? "" : moUnit.getSymbol()); // Unidad
        mvValues.add(moUnit == null ? "" : moUnit.getDbmsClaveUnidad()); // Unidad SAT
        mvValues.add(moTaxRegion == null ? "" : moTaxRegion.getTaxRegion()); // Región de impuestos
        mvValues.add(mnOperationTypePk == 0 ? "" : SDataConstantsSys.OperationsTypesOpsMap.get(mnOperationTypePk)); // Tipo de operación
        mvValues.add(moCostCenter == null ? "" : moCostCenter.getPkCostCenterIdXXX()); // Clave centro costo
        mvValues.add(moCostCenter == null ? "" : moCostCenter.getCostCenter()); // Centro costo
        mvValues.add(moItemReference == null ? "" : moItemReference.getKey()); // Clave ítem de referencia
        mvValues.add(moItemReference == null ? "" : moItemReference.getItem()); // ítem de referencia
        
        // CFDI (complemento):
        
        mvValues.add(getPriceUnitary()); // Valor unitario
        mvValues.add(moConcepto.getAttImporte().getDouble()); // Importe
        mvValues.add(moConcepto.getAttDescuento().getDouble()); // Descuento
    }
}
