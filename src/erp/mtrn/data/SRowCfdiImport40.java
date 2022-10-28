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
 * @author Isabel Servín
 */
public final class SRowCfdiImport40 extends erp.lib.table.STableRow {
    
    private final cfd.ver40.DElementConcepto moConcepto;
    private final SClientInterface miClient;
    private SDataItem moItem;
    private SDataItem moReferenceItem;
    private SDataUnit moUnit;
    private SDataTaxRegion moTaxRegion;
    private SDataCostCenter moCostCenter;
    private SDataDpsEntry moDpsEntry;
    private SDataEntryDpsDpsLink moEntryDpsDpsLink;
    private int mnOperationTypePk;
    private double mdConvFactor;
    private final int mnRowCount;
    private String msClaveUnidadSiie; 
    
    private HashSet<cfd.ver40.DElementConceptoImpuestoTraslado> moTaxChargedMatched;
    private HashSet<cfd.ver40.DElementConceptoImpuestoRetencion> moTaxRetainedMatched;
    
    /**
     * 
     * @param client Cliente GUI.
     * @param concepto Concepto del CFDI.
     * @param row
     */
    public SRowCfdiImport40(SClientInterface client, cfd.ver40.DElementConcepto concepto, int row){
        moConcepto = concepto;
        miClient = client;
        mnRowCount = row;
        resetMatchingSettings();
        prepareTableRow();
    }

    public void setItem(final SDataItem o) { moItem = o; }
    public void setReferenceItem(final SDataItem o) { moReferenceItem = o; }
    public void setUnit(final SDataUnit o) { moUnit = o; }
    public void setTaxRegion(final SDataTaxRegion o) { moTaxRegion = o; }
    public void setCostCenter(final SDataCostCenter o) { moCostCenter = o; }
    public void setEntryDpsDpsLink(final SDataEntryDpsDpsLink o) { moEntryDpsDpsLink = o; }
    public void setOperationTypePk(final int i) { mnOperationTypePk = i; }
    public void setConvFactor(final double d) { 
        mdConvFactor = d; 
        if (moDpsEntry != null) {
            moDpsEntry.setOriginalQuantity(getEquivalentQuantity());
            moDpsEntry.setOriginalPriceUnitaryCy(getPriceUnitary());
            moDpsEntry.setOriginalPriceUnitarySystemCy(getPriceUnitary());
        }
    }
    
    public cfd.ver40.DElementConcepto getConcepto() { return moConcepto; }
    public SDataItem getItem() { return moItem; }
    public SDataItem getReferenceItem() { return moReferenceItem; }
    public SDataUnit getUnit() { return moUnit; }
    public SDataTaxRegion getTaxRegion() { return moTaxRegion; }
    public SDataCostCenter getCostCenter() { return moCostCenter; }
    public SDataDpsEntry getDpsEntry() { return moDpsEntry; } 
    public SDataEntryDpsDpsLink getEntryDpsDpsLink() { return moEntryDpsDpsLink; }
    public int getOperationTypePk() { return mnOperationTypePk; }
    public double getConvFactor() { return mdConvFactor; }
    
    public String getClaveUnidadSiie() { return msClaveUnidadSiie; }
    public String getClaveUnidadCfdi() { return moConcepto.getAttClaveUnidad().getString(); } 
    
    public HashSet<cfd.ver40.DElementConceptoImpuestoRetencion> getTaxRetainedMatched(){ return moTaxRetainedMatched; }
    public HashSet<cfd.ver40.DElementConceptoImpuestoTraslado> getTaxChargedMatched(){ return moTaxChargedMatched; }
    
    public void addTaxChargedMatched(cfd.ver40.DElementConceptoImpuestoTraslado o){ moTaxChargedMatched.add(o); }
    public void addTaxRetainedMatched(cfd.ver40.DElementConceptoImpuestoRetencion o){ moTaxRetainedMatched.add(o); }
    
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
        moItem = item;
        moReferenceItem = reference;
        moUnit = unit;
        moTaxRegion = taxRegion;
        moCostCenter = costCenter;
        moDpsEntry = new SDataDpsEntry();
        moEntryDpsDpsLink = null;
        mdConvFactor = 1;
        mnOperationTypePk = SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS;
    }
    
    /**
     * Borra todos los componentes de la clase
     */
    public void resetMatchingSettings() {
        moItem = null;
        moReferenceItem = null;
        moUnit = null;
        moTaxRegion = null;
        moCostCenter = null;
        moDpsEntry = new SDataDpsEntry();
        mnOperationTypePk = 0;
        mdConvFactor = 1.0;
        moTaxChargedMatched = new HashSet<>();
        moTaxRetainedMatched = new HashSet<>();
    }
    
    /**
     * Crea un nuevo DpsEntry a partir de los componentes ya asignados.
     */
    public void setDpsEntry(){
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
        moDpsEntry.setIsDiscountDocApplying(moConcepto.getAttDescuento().getDouble() != 0);
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
        */
        moDpsEntry.setDiscountDocCy(moConcepto.getAttDescuento().getDouble());
        /*
        moDpsEntry.setSubtotalCy_r(...);
        moDpsEntry.setTaxChargedCy_r(...) 
        moDpsEntry.setTaxRetainedCy_r(...) 
        moDpsEntry.setTotalCy_r(...);
        moDpsEntry.setPriceUnitaryRealCy_r(...);
        moDpsEntry.setCommissionsCy_r(...);
        */
        moDpsEntry.setOriginalQuantity(getEquivalentQuantity());
        moDpsEntry.setOriginalPriceUnitaryCy(getPriceUnitary());
        moDpsEntry.setOriginalPriceUnitarySystemCy(getPriceUnitary());
        //moDpsEntry.setOriginalDiscountUnitaryCy(moConcepto.getAttDescuento().getDouble());
        //moDpsEntry.setOriginalDiscountUnitarySystemCy(moConcepto.getAttDescuento().getDouble());
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
        moDpsEntry.setOperationsType(mnOperationTypePk); 
        moDpsEntry.setUserId(0);
        moDpsEntry.setSortingPosition(mnRowCount); 
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
        moDpsEntry.setFkUnitId(moItem == null ? 0 : moItem.getFkUnitId());
        moDpsEntry.setFkOriginalUnitId(moUnit == null ? 0 : moUnit.getPkUnitId());
        moDpsEntry.setFkTaxRegionId(moTaxRegion == null ? 0 :moTaxRegion.getPkTaxRegionId());
        
        //moDpsEntry.setFkThirdTaxCausingId_n(...);
        
        moDpsEntry.setFkDpsAdjustmentTypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[0]);
        moDpsEntry.setFkDpsAdjustmentSubtypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[1]);
        moDpsEntry.setFkDpsEntryTypeId(SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY);
        
        /*
        moDpsEntry.setFkVehicleTypeId_n(...);
        moDpsEntry.setFkCashCompanyBranchId_n(...);
        moDpsEntry.setFkCashAccountId_n(...);
        */
        moDpsEntry.setFkCostCenterId_n(moCostCenter == null ? "" : moCostCenter.getPkCostCenterIdXXX());
        moDpsEntry.setFkItemRefId_n(moReferenceItem == null ? 0 : moReferenceItem.getPkItemId());
        moDpsEntry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        
        moDpsEntry.setDbmsOriginalUnitSymbol(moUnit == null ? "" : moUnit.getSymbol());
        moDpsEntry.setDbmsTaxRegion(moTaxRegion == null ? "" : moTaxRegion.getTaxRegion());
        moDpsEntry.setDbmsItemRef_n(moReferenceItem == null ? "" : moReferenceItem.getItem()); 
        moDpsEntry.setDbmsCostCenterCode(moCostCenter == null ? "" : moCostCenter.getCode());
        moDpsEntry.setDbmsCostCenter_n(moCostCenter == null ? "" : moCostCenter.getCostCenter());
        
        updateDpsDpsLinkAsDestiny();
    } 
    
    /**
     * Calcula el total de los impuestos del DpsEntry.
     * @param date
     * @param idyEmisor
     * @param exchangeRate
     * @param idyReceptor
     */
    public void calculateTotalDpsEntry(Date date, int idyEmisor, int idyReceptor, double exchangeRate){
        if (moDpsEntry != null) {
            moDpsEntry.calculateTotal(miClient, date, idyEmisor, idyReceptor, false, 0, exchangeRate);
            moDpsEntry.setIsTaxesAutomaticApplying(false);
            moTaxChargedMatched.clear();
            moTaxRetainedMatched.clear();
        }
    }
    
    private void updateDpsDpsLinkAsDestiny() {
        if (moEntryDpsDpsLink != null) {
            SDataDpsDpsLink dpsLink = new SDataDpsDpsLink();
            dpsLink.setPkSourceYearId(moEntryDpsDpsLink.getPkYearId());
            dpsLink.setPkSourceDocId(moEntryDpsDpsLink.getPkDocId());
            dpsLink.setPkSourceEntryId(moEntryDpsDpsLink.getPkEntryId());
            dpsLink.setOriginalQuantity(moEntryDpsDpsLink.getQuantityToLink()); 
            moDpsEntry.getDbmsDpsLinksAsDestiny().clear();
            moDpsEntry.getDbmsDpsLinksAsDestiny().add(dpsLink);
        }
    }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();        
        
        // CFDI:
        
        mvValues.add(mnRowCount); //#
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
        mvValues.add(moReferenceItem == null ? "" : moReferenceItem.getKey());  //Clave ítem de referencia
        mvValues.add(moReferenceItem == null ? "" : moReferenceItem.getItem()); //ítem de referencia
        
        // CFDI (complemento):
        
        mvValues.add(getPriceUnitary());                             //Valor unitario
        mvValues.add(moConcepto.getAttImporte().getDouble());        //Importe
        mvValues.add(moConcepto.getAttDescuento().getDouble());      //Descuento
    }
}
