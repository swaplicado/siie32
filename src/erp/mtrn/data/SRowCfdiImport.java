/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mfin.data.SDataCostCenter;
import erp.mfin.data.SDataTaxRegion;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mitm.data.SItemUtilities;
import java.util.Date;
import java.util.HashSet;

/**
 *
 * @author Isabel Servín
 */
public final class SRowCfdiImport extends erp.lib.table.STableRow {
    
    private final cfd.ver33.DElementConcepto moConcepto;
    private final SClientInterface miClient;
    private SDataItem moItem;
    private SDataItem moReferenceItem;
    private SDataUnit moUnit;
    private SDataTaxRegion moTaxRegion;
    private SDataEntryDpsDpsLink moDpsLink;
    private SDataCostCenter moCostCenter;
    private SDataDpsEntry moDpsEntry;
    private double mdConvFactor;
    private int miOperationTypePk;
    
    private HashSet<cfd.ver33.DElementConceptoImpuestoTraslado> moTaxChargedMatched;
    private HashSet<cfd.ver33.DElementConceptoImpuestoRetencion> moTaxRetainedMatched;
    
    /**
     * 
     * @param client Cliente GUI.
     * @param concepto Concepto del CFDI.
     */
    public SRowCfdiImport(SClientInterface client, cfd.ver33.DElementConcepto concepto){
        moConcepto = concepto;
        miClient = client;
        resetMatchingSettings();
        prepareTableRow();
    }

    public void setItem(final SDataItem o) { moItem = o; }
    public void setReferenceItem(final SDataItem o) { moReferenceItem = o; }
    public void setUnit(final SDataUnit o) { moUnit = o; }
    public void setTaxRegion(final SDataTaxRegion o) { moTaxRegion = o; }
    public void setEntryDpsDpsLink (final SDataEntryDpsDpsLink o) { moDpsLink = o; }
    public void setCostCenter (final SDataCostCenter o) { moCostCenter = o; }
    public void setConvFactor(final double d) { mdConvFactor = d; }
    public void setOperationTypePk(final int i) { miOperationTypePk = i; }
    
    public cfd.ver33.DElementConcepto getConcepto() { return moConcepto; }
    public SDataItem getItem() { return moItem; }
    public SDataItem getReferenceItem() { return moReferenceItem; }
    public SDataUnit getUnit() { return moUnit; }
    public SDataTaxRegion getTaxRegion() { return moTaxRegion; }
    public SDataEntryDpsDpsLink getEntryDpsDpsLink() { return moDpsLink; }
    public SDataCostCenter getCostCenter() { return moCostCenter; }
    public SDataDpsEntry getDpsEntry() { return moDpsEntry; } 
    public double getConvFactor() { return mdConvFactor; }
    public int getOperationTypePk() { return miOperationTypePk; }
    public HashSet<cfd.ver33.DElementConceptoImpuestoRetencion> getTaxRetainedMatched(){ return moTaxRetainedMatched; }
    public HashSet<cfd.ver33.DElementConceptoImpuestoTraslado> getTaxChargedMatched(){ return moTaxChargedMatched; }
    
    public void addTaxChargedMatched(cfd.ver33.DElementConceptoImpuestoTraslado o){ moTaxChargedMatched.add(o); }
    public void addTaxRetainedMatched(cfd.ver33.DElementConceptoImpuestoRetencion o){ moTaxRetainedMatched.add(o); }
    
    public double getQuantity() {
        return moConcepto.getAttCantidad().getDouble() * mdConvFactor;
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
        moItem = item;
        moUnit = unit;
        moTaxRegion = taxRegion;
        moCostCenter = costCenter;
        mdConvFactor = 1;
        moReferenceItem = reference;
        miOperationTypePk = SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS;
    }
    
    /**
     * Borra todos los componentes de la clase
     */
    public void resetMatchingSettings() {
        moItem = null;
        moUnit = null;
        moTaxRegion = null;
        moCostCenter = null;
        mdConvFactor = 1.0;
        moReferenceItem = null;
        miOperationTypePk = 0;
        moTaxChargedMatched = new HashSet<>();
        moTaxRetainedMatched = new HashSet<>();
    }
    
    /**
     * Crea un nuevo DpsEntry a partir de los componentes ya asignados.
     * @param sortingPosition
     */
    public void setDpsEntry(int sortingPosition){
        if (moDpsEntry == null) {
            moDpsEntry = new SDataDpsEntry();
        }
        moDpsEntry.reset();
        /*
        moDpsEntry.setPkYearId(...);
        moDpsEntry.setPkDocId(...);
        moDpsEntry.setPkEntryId(...)
        */
        moDpsEntry.setConceptKey(moItem == null ? "" : moItem.getCode());
        moDpsEntry.setConcept(moItem == null ? "" : moItem.getItem());
        /*
        moDpsEntry.setReference(...);
        moDpsEntry.setQuantity(...);
        */
        moDpsEntry.setIsDiscountDocApplying(false);//moDpsNew.getIsDiscountDocApplying()
        moDpsEntry.setIsDiscountUnitaryPercentage(false); 
        moDpsEntry.setIsDiscountUnitaryPercentageSystem(false);
        moDpsEntry.setIsDiscountEntryPercentage(false);
        moDpsEntry.setDiscountUnitaryPercentage(0.0);
        moDpsEntry.setDiscountUnitaryPercentageSystem(0.0);
        moDpsEntry.setDiscountEntryPercentage(0.0);
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
        moDpsEntry.setDiscountDocCy(...);
        moDpsEntry.setSubtotalCy_r(...);
        moDpsEntry.setTaxChargedCy_r(...) 
        moDpsEntry.setTaxRetainedCy_r(...) 
        moDpsEntry.setTotalCy_r(...);
        moDpsEntry.setPriceUnitaryRealCy_r(...);
        moDpsEntry.setCommissionsCy_r(...);
        */
        moDpsEntry.setOriginalQuantity(getQuantity());
        moDpsEntry.setOriginalPriceUnitaryCy(moConcepto.getAttValorUnitario().getDouble());
        moDpsEntry.setOriginalPriceUnitarySystemCy(moConcepto.getAttValorUnitario().getDouble());
        moDpsEntry.setOriginalDiscountUnitaryCy(moConcepto.getAttDescuento().getDouble());
        moDpsEntry.setOriginalDiscountUnitarySystemCy(moConcepto.getAttDescuento().getDouble());
        /*
        moDpsEntry.setSalesPriceUnitaryCy(...);
        moDpsEntry.setSalesFreightUnitaryCy(...);
        */
        moDpsEntry.setLength(0.0);
        moDpsEntry.setSurface(0.0);
        moDpsEntry.setVolume(0.0);
        moDpsEntry.setMass(0.0);
        moDpsEntry.setWeightPackagingExtra(0.0);
        moDpsEntry.setWeightGross(0.0);
        moDpsEntry.setWeightDelivery(0.0);
        moDpsEntry.setSurplusPercentage(0.0);
        moDpsEntry.setContractBase(0.0);
        moDpsEntry.setContractFuture(0.0);
        moDpsEntry.setContractFactor(0.0);
        moDpsEntry.setContractPriceYear(0);
        moDpsEntry.setContractPriceMonth(0);
        moDpsEntry.setSealQuality("");
        moDpsEntry.setSealSecurity("");
        moDpsEntry.setDriver("");
        moDpsEntry.setPlate("");
        moDpsEntry.setTicket("");
        moDpsEntry.setContainerTank("");
        moDpsEntry.setVgm("");
        moDpsEntry.setOperationsType(miOperationTypePk); 
        moDpsEntry.setUserId(0);
        moDpsEntry.setSortingPosition(sortingPosition); 
        moDpsEntry.setIsPrepayment(false);
        moDpsEntry.setIsDiscountRetailChain(false);
        moDpsEntry.setIsTaxesAutomaticApplying(true);
        moDpsEntry.setIsPriceVariable(false);
        moDpsEntry.setIsPriceConfirm(false);
        moDpsEntry.setIsSalesFreightRequired(false);
        moDpsEntry.setIsSalesFreightConfirm(false);
        moDpsEntry.setIsSalesFreightAdd(false);
        moDpsEntry.setIsInventoriable(moItem == null ? false : moItem.getIsInventoriable()); 
        moDpsEntry.setIsDeleted(false);
        moDpsEntry.setFkItemId(moItem == null ? 0 : moItem.getPkItemId());
        moDpsEntry.setFkUnitId(moUnit == null ? 0 : moUnit.getPkUnitId());
        moDpsEntry.setFkOriginalUnitId(moUnit == null ? 0 : moUnit.getPkUnitId());
        moDpsEntry.setFkTaxRegionId(moTaxRegion == null ? 0 :moTaxRegion.getPkTaxRegionId());
        /*
        moDpsEntry.setFkThirdTaxCausingId_n(...);
        moDpsEntry.setFkDpsAdjustmentTypeId(...)
        moDpsEntry.setFkDpsAdjustmentSubtypeId(...)
        */
        moDpsEntry.setFkDpsEntryTypeId(0);
        /*
        moDpsEntry.setFkVehicleTypeId_n(...);
        moDpsEntry.setFkCashCompanyBranchId_n(...);
        moDpsEntry.setFkCashAccountId_n(...);
        */
        moDpsEntry.setFkCostCenterId_n(moCostCenter == null ? "" : moCostCenter.getPkCostCenterIdXXX());
        moDpsEntry.setFkItemRefId_n(moReferenceItem == null ? 0 : moReferenceItem.getPkItemId());
        moDpsEntry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
    } 
    
    /**
     * Calcula el total de los impuestos del DpsEntry.
     * @param date
     * @param idyEmisor
     * @param exchangeRate
     * @param idyReceptor
     */
    public void calculateTotalDpsEntry(Date date, int idyEmisor, int idyReceptor, double exchangeRate){
        moDpsEntry.calculateTotal(miClient, date, idyEmisor, idyReceptor, false, 0, exchangeRate);
        moDpsEntry.setIsTaxesAutomaticApplying(false);
        moTaxChargedMatched.clear();
        moTaxRetainedMatched.clear();
    }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();        
        
        // CFDI:
        
        mvValues.add(moConcepto.getAttNoIdentificacion().getString());
        mvValues.add(moConcepto.getAttDescripcion().getString());
        mvValues.add(moConcepto.getAttClaveProdServ().getString());
        mvValues.add(moConcepto.getAttUnidad().getString());
        mvValues.add(moConcepto.getAttClaveUnidad().getString());
        mvValues.add(moConcepto.getAttCantidad().getDouble());
        
        // SIIE:
        
        String claveProdServ = "";
        String claveUnidad = "";
        
        if (moItem != null && moUnit != null) {
            try {
                claveProdServ = SItemUtilities.getClaveProdServ(miClient.getSession().getStatement(), moItem.getCfdProdServId());
                claveUnidad = SItemUtilities.getClaveUnidad(miClient.getSession().getStatement(), moUnit.getFkCfdUnitId());
            }
            catch (Exception e) {
                // no es necesario atrapar la excepción
            }
        }
        
        mvValues.add(moItem == null ? "" : moItem.getKey());
        mvValues.add(moItem == null ? "" : moItem.getItem());
        mvValues.add(claveProdServ);
        mvValues.add(moUnit == null ? "" : moUnit.getUnit());
        mvValues.add(claveUnidad);
        mvValues.add(mdConvFactor);
        mvValues.add(getQuantity());
        mvValues.add(moTaxRegion == null ? "" : moTaxRegion.getTaxRegion());
        mvValues.add(miOperationTypePk == 0 ? "" : SDataConstantsSys.OperationsTypesOpsMap.get(miOperationTypePk));
        mvValues.add(moCostCenter == null ? "" : moCostCenter.getCostCenter());
        mvValues.add(moReferenceItem == null ? "" : moReferenceItem.getKey());
        mvValues.add(moReferenceItem == null ? "" : moReferenceItem.getItem());
    }
}
