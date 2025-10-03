/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.SErpConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mfin.data.SDataCostCenter;
import erp.mfin.data.SDataTaxRegion;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mitm.data.SItemUtilities;
import java.util.Date;
import java.util.HashSet;
import sa.lib.SLibUtils;

/**
 *
 * @author Isabel Servín, Sergio Flores
 */
public final class SRowCfdiImport40 extends erp.lib.table.STableRow {
    
    private final SClientInterface miClient;
    private final cfd.ver40.DElementConcepto moConcepto;
    private final int mnRow;
    
    private SDataDpsEntry moNewDpsEntry;
    private SDataItem moItem;
    private SDataItem moItemReference;
    private SDataUnit moUnit;
    private SDataTaxRegion moTaxRegion;
    private SDataCostCenter moCostCenter;
    private int mnOperationTypePk;
    private SDataEntryDpsDpsLink moImportedEntryDpsDpsLink;
    private SDataDpsEntry moImportedDpsEntry;
    
    private double mdConvFactor;
    
    private String msClaveUnidadSiie; 
    
    private HashSet<cfd.ver40.DElementConceptoImpuestoTraslado> moTaxChargedMatched;
    private HashSet<cfd.ver40.DElementConceptoImpuestoRetencion> moTaxRetainedMatched;
    
    /**
     * 
     * @param client Cliente GUI.
     * @param concepto Concepto del CFDI.
     * @param row
     */
    public SRowCfdiImport40(SClientInterface client, cfd.ver40.DElementConcepto concepto, int row) {
        miClient = client;
        moConcepto = concepto;
        mnRow = row;
        
        resetMatchingSettings();
        prepareTableRow();
    }

    public void setItem(final SDataItem o) { moItem = o; }
    public void setItemReference(final SDataItem o) { moItemReference = o; }
    public void setUnit(final SDataUnit o) { moUnit = o; }
    public void setTaxRegion(final SDataTaxRegion o) { moTaxRegion = o; }
    public void setCostCenter(final SDataCostCenter o) { moCostCenter = o; }
    public void setOperationTypePk(final int i) { mnOperationTypePk = i; }
    public void setImportedEntryDpsDpsLink(final SDataEntryDpsDpsLink o) { moImportedEntryDpsDpsLink = o; }
    public void setImportedDpsEntry(final SDataDpsEntry o) { moImportedDpsEntry = o; }
    
    public SDataDpsEntry getNewDpsEntry() { return moNewDpsEntry; } 
    public cfd.ver40.DElementConcepto getConcepto() { return moConcepto; }
    public SDataItem getItem() { return moItem; }
    public SDataItem getItemReference() { return moItemReference; }
    public SDataUnit getUnit() { return moUnit; }
    public SDataTaxRegion getTaxRegion() { return moTaxRegion; }
    public SDataCostCenter getCostCenter() { return moCostCenter; }
    public int getOperationTypePk() { return mnOperationTypePk; }
    public SDataEntryDpsDpsLink getImportedEntryDpsDpsLink() { return moImportedEntryDpsDpsLink; }
    public SDataDpsEntry getImportedDpsEntry() { return moImportedDpsEntry; }
    
    public void setConvFactor(final double d) { 
        mdConvFactor = d; 
        if (moNewDpsEntry != null) {
            moNewDpsEntry.setOriginalQuantity(getEquivalentQuantity());
            moNewDpsEntry.setOriginalPriceUnitaryCy(getPriceUnitary());
            moNewDpsEntry.setOriginalPriceUnitarySystemCy(getPriceUnitary());
        }
    }
    
    public double getConvFactor() {
        return mdConvFactor;
    }
    
    public String getClaveUnidadSiie() { return msClaveUnidadSiie; }
    public String getClaveUnidadCfdi() { return moConcepto.getAttClaveUnidad().getString(); } 
    
    public HashSet<cfd.ver40.DElementConceptoImpuestoRetencion> getTaxRetainedMatched() { return moTaxRetainedMatched; }
    public HashSet<cfd.ver40.DElementConceptoImpuestoTraslado> getTaxChargedMatched() { return moTaxChargedMatched; }
    
    public void addTaxChargedMatched(cfd.ver40.DElementConceptoImpuestoTraslado o) { moTaxChargedMatched.add(o); }
    public void addTaxRetainedMatched(cfd.ver40.DElementConceptoImpuestoRetencion o) { moTaxRetainedMatched.add(o); }
    
    public double getEquivalentQuantity() {
        return moConcepto.getAttCantidad().getDouble() * mdConvFactor;
    }
    
    public double getPriceUnitary() {
        return SLibUtils.round((moConcepto.getAttCantidad().getDouble() * moConcepto.getAttValorUnitario().getDouble()) / getEquivalentQuantity(), SErpConsts.VAL_QTY_MAX_DECS);
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
        moNewDpsEntry = new SDataDpsEntry();
        moItem = item;
        moItemReference = reference;
        moUnit = unit;
        moTaxRegion = taxRegion;
        moCostCenter = costCenter;
        moImportedEntryDpsDpsLink = null;
        mnOperationTypePk = SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS;
        mdConvFactor = 1;
    }
    
    /**
     * Borra todos los componentes de la clase
     */
    public void resetMatchingSettings() {
        moNewDpsEntry = new SDataDpsEntry();
        moItem = null;
        moItemReference = null;
        moUnit = null;
        moTaxRegion = null;
        moCostCenter = null;
        moImportedEntryDpsDpsLink = null;
        mnOperationTypePk = 0;
        mdConvFactor = 1;
        
        moTaxChargedMatched = new HashSet<>();
        moTaxRetainedMatched = new HashSet<>();
    }
    
    /**
     * Refresca el registro DpsEntry a partir de los datos y componentes ya asignados.
     */
    public void refreshDpsEntry() {
        moNewDpsEntry.reset();
        
        /*
        moDpsEntry.setPkYearId(...);
        moDpsEntry.setPkDocId(...);
        moDpsEntry.setPkEntryId(...)
        */
        moNewDpsEntry.setConceptKey(moItem == null ? "" : moItem.getCode());
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
        moNewDpsEntry.setOriginalQuantity(getEquivalentQuantity());
        moNewDpsEntry.setOriginalPriceUnitaryCy(getPriceUnitary());
        moNewDpsEntry.setOriginalPriceUnitarySystemCy(getPriceUnitary());
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
        moNewDpsEntry.setSortingPosition(mnRow); 
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
        moNewDpsEntry.setFkCostCenterId_n(moCostCenter == null ? "" : moCostCenter.getPkCostCenterIdXXX());
        moNewDpsEntry.setFkItemRefId_n(moItemReference == null ? 0 : moItemReference.getPkItemId());
        moNewDpsEntry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        
        moNewDpsEntry.setDbmsOriginalUnitSymbol(moUnit == null ? "" : moUnit.getSymbol());
        moNewDpsEntry.setDbmsTaxRegion(moTaxRegion == null ? "" : moTaxRegion.getTaxRegion());
        moNewDpsEntry.setDbmsItemRef_n(moItemReference == null ? "" : moItemReference.getItem()); 
        moNewDpsEntry.setDbmsCostCenterCode(moCostCenter == null ? "" : moCostCenter.getCode());
        moNewDpsEntry.setDbmsCostCenter_n(moCostCenter == null ? "" : moCostCenter.getCostCenter());
        
        updateDpsDpsLinkAsDestiny();
    } 
    
    /**
     * Calcula el total de los impuestos del DpsEntry.
     * @param date
     * @param idyEmisor
     * @param exchangeRate
     * @param idyReceptor
     */
    public void calculateTotalDpsEntry(Date date, int idyEmisor, int idyReceptor, double exchangeRate) {
        if (moNewDpsEntry != null) {
            moNewDpsEntry.calculateTotal(miClient, date, idyEmisor, idyReceptor, false, 0, exchangeRate);
            moNewDpsEntry.setIsTaxesAutomaticApplying(false);
            
            moTaxChargedMatched.clear();
            moTaxRetainedMatched.clear();
        }
    }
    
    private void updateDpsDpsLinkAsDestiny() {
        if (moImportedEntryDpsDpsLink != null) {
            SDataDpsDpsLink dpsLink = new SDataDpsDpsLink();
            dpsLink.setPkSourceYearId(moImportedEntryDpsDpsLink.getPkYearId());
            dpsLink.setPkSourceDocId(moImportedEntryDpsDpsLink.getPkDocId());
            dpsLink.setPkSourceEntryId(moImportedEntryDpsDpsLink.getPkEntryId());
            dpsLink.setOriginalQuantity(moImportedEntryDpsDpsLink.getQuantityToLink()); 
            
            moNewDpsEntry.getDbmsDpsLinksAsDestiny().clear();
            moNewDpsEntry.getDbmsDpsLinksAsDestiny().add(dpsLink);
        }
    }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();        
        
        // CFDI:
        
        mvValues.add(mnRow); //#
        mvValues.add(moConcepto.getAttNoIdentificacion().getString());
        mvValues.add(moConcepto.getAttDescripcion().getString());
        mvValues.add(moConcepto.getAttClaveProdServ().getString());
        mvValues.add(moConcepto.getAttCantidad().getDouble());
        mvValues.add(moConcepto.getAttUnidad().getString());
        mvValues.add(moConcepto.getAttClaveUnidad().getString());
        
        // SIIE:
        
        String claveProdServ = "";
        msClaveUnidadSiie = "";
        
        if (moItem != null && moUnit != null) {
            try {
                claveProdServ = SItemUtilities.getClaveProdServ(miClient.getSession().getStatement(), moItem.getCfdProdServId());
                msClaveUnidadSiie = SItemUtilities.getClaveUnidad(miClient.getSession().getStatement(), moUnit.getFkCfdUnitId());
            }
            catch (Exception e) {
                // no es necesario atrapar la excepción
            }
        }
        
        mvValues.add(moItem == null ? "" : moItem.getKey());    //Código ítem
        mvValues.add(moItem == null ? "" : moItem.getItem());   //Nombre ítem
        mvValues.add(claveProdServ);                            //ProdServ SAT
        mvValues.add(moItem == null ? "" : moItem.getDbmsDataUnit().getSymbol()); //Unidad ítem
        mvValues.add(mdConvFactor);                             //Factor de conversion
        mvValues.add(getEquivalentQuantity());                  //Cantidad equivalente
        mvValues.add(moUnit == null ? "" : moUnit.getSymbol()); //Unidad
        mvValues.add(msClaveUnidadSiie);                        //Unidad SAT
        mvValues.add(moTaxRegion == null ? "" : moTaxRegion.getTaxRegion());    //Región de impuestos
        mvValues.add(mnOperationTypePk == 0 ? "" : SDataConstantsSys.OperationsTypesOpsMap.get(mnOperationTypePk)); //Tipo de operación
        mvValues.add(moCostCenter == null ? "" : moCostCenter.getPkCostCenterIdXXX());//Clave centro costo
        mvValues.add(moCostCenter == null ? "" : moCostCenter.getCostCenter()); //Centro costo
        mvValues.add(moItemReference == null ? "" : moItemReference.getKey());  //Clave ítem de referencia
        mvValues.add(moItemReference == null ? "" : moItemReference.getItem()); //ítem de referencia
        
        // CFDI (complemento):
        
        mvValues.add(getPriceUnitary());                             //Valor unitario
        mvValues.add(moConcepto.getAttImporte().getDouble());        //Importe
        mvValues.add(moConcepto.getAttDescuento().getDouble());      //Descuento
    }
}
