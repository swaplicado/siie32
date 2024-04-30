/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import cfd.ver40.DCfdi40Catalogs;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mitm.data.SItemUtilities;
import erp.mod.SModSysConsts;
import erp.mod.itm.db.SDbPriceCommercialLog;
import erp.mod.trn.db.STrnConsts;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following classes:
 * - erp.mod.trn.db.SDbDpsEntry
 * All of them also make raw SQL insertions.
 */

/**
 * WARNING: Every change that affects the structure of this registry must be reflected in SIIE/ETL Avista classes and methods!
 * @author Sergio Flores, Alfonso Flores, Uriel Castañeda, Juan Barajas, Daniel López, Isabel Servín, Edwin Carmona
 */
public class SDataDpsEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected java.lang.String msConceptKey;
    protected java.lang.String msConcept;
    protected java.lang.String msReference;
    protected double mdQuantity;
    protected boolean mbIsDiscountDocApplying;
    protected boolean mbIsDiscountUnitaryPercentage;
    protected boolean mbIsDiscountUnitaryPercentageSystem;
    protected boolean mbIsDiscountEntryPercentage;
    protected double mdDiscountUnitaryPercentage;
    protected double mdDiscountUnitaryPercentageSystem;
    protected double mdDiscountEntryPercentage;
    protected double mdPriceUnitary;
    protected double mdPriceUnitarySystem;
    protected double mdDiscountUnitary;
    protected double mdDiscountUnitarySystem;
    protected double mdDiscountEntry;
    protected double mdSubtotalProvisional_r;
    protected double mdDiscountDoc;
    protected double mdSubtotal_r;
    protected double mdTaxCharged_r;
    protected double mdTaxRetained_r;
    protected double mdTotal_r;
    protected double mdPriceUnitaryReal_r;
    protected double mdCommissions_r;
    protected double mdPriceUnitaryCy;
    protected double mdPriceUnitarySystemCy;
    protected double mdDiscountUnitaryCy;
    protected double mdDiscountUnitarySystemCy;
    protected double mdDiscountEntryCy;
    protected double mdSubtotalProvisionalCy_r;
    protected double mdDiscountDocCy;
    protected double mdSubtotalCy_r;
    protected double mdTaxChargedCy_r;
    protected double mdTaxRetainedCy_r;
    protected double mdTotalCy_r;
    protected double mdPriceUnitaryRealCy_r;
    protected double mdCommissionsCy_r;
    protected double mdOriginalQuantity;
    protected double mdOriginalPriceUnitaryCy;
    protected double mdOriginalPriceUnitarySystemCy;
    protected double mdOriginalDiscountUnitaryCy;
    protected double mdOriginalDiscountUnitarySystemCy;
    protected double mdSalesPriceUnitaryCy;
    protected double mdSalesFreightUnitaryCy;
    protected double mdLength;
    protected double mdSurface;
    protected double mdVolume;
    protected double mdMass;
    protected double mdWeightPackagingExtra;
    protected double mdWeightGross;
    protected double mdWeightDelivery;
    protected double mdSurplusPercentage;    
    protected double mdContractBase;
    protected double mdContractFuture;
    protected double mdContractFactor;
    protected int mnContractPriceYear;
    protected int mnContractPriceMonth;
    protected java.lang.String msSealQuality;
    protected java.lang.String msSealSecurity;
    protected java.lang.String msDriver;
    protected java.lang.String msPlate;
    protected java.lang.String msTicket;
    protected java.lang.String msContainerTank;
    protected java.lang.String msVgm;
    protected int mnOperationsType;
    protected int mnUserId;
    protected int mnSortingPosition;
    protected boolean mbIsPrepayment;
    protected boolean mbIsDiscountRetailChain;
    protected boolean mbIsTaxesAutomaticApplying;
    protected boolean mbIsPriceVariable;
    protected boolean mbIsPriceConfirm;
    protected boolean mbIsSalesFreightRequired;
    protected boolean mbIsSalesFreightConfirm;
    protected boolean mbIsSalesFreightAdd;
    protected boolean mbIsInventoriable;
    protected boolean mbIsDeleted;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkOriginalUnitId;
    protected int mnFkTaxRegionId;
    protected int mnFkThirdTaxCausingId_n;
    protected int mnFkDpsAdjustmentTypeId;
    protected int mnFkDpsAdjustmentSubtypeId;
    protected int mnFkDpsEntryTypeId;
    protected int mnFkVehicleTypeId_n;
    protected int mnFkCashCompanyBranchId_n;
    protected int mnFkCashAccountId_n;
    protected java.lang.String msFkCostCenterId_n;
    protected int mnFkItemRefId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected int mnDbmsFkItemGenericId;
    protected boolean mbDbmsItemGenDataShipDomesticReq;
    protected boolean mbDbmsItemGenDataShipInternationalReq;
    protected boolean mbDbmsItemGenDataShipQualityReq;
    protected java.lang.String msDbmsItem;
    protected java.lang.String msDbmsUnitSymbol;
    protected java.lang.String msDbmsOriginalUnitSymbol;
    protected double mdDbmsOriginalUnitBaseEquivalence;
    protected java.lang.String msDbmsTaxRegion;
    protected java.lang.String msDbmsDpsAdjustmentType;
    protected java.lang.String msDbmsDpsEntryType;
    protected java.lang.String msDbmsCostCenterCode;
    protected java.lang.String msDbmsCostCenter_n;
    protected java.lang.String msDbmsItemRef_n;
    protected int mnDbmsItemBrandId;
    protected java.lang.String msDbmsItemBrand;
    protected java.lang.String msDbmsItemClaveProdServ;
    protected java.lang.String msDbmsUnidadClave;
    protected java.lang.String msDbmsOriginalUnidadClave;
    protected java.lang.String msDbmsTariff;
    protected java.lang.String msDbmsCustomsUnit;
    protected java.lang.String msDbmsCustomsUnitSymbol;
    protected java.lang.String msDbmsItemCustomsUnitSymbol;
    protected double mdDbmsItemCustomsUnitEquivalence;
    protected int mnDbmsAddBachocoNumeroPosicion;
    protected java.lang.String msDbmsAddBachocoCentro;
    protected int mnDbmsAddLorealEntryNumber;
    protected java.lang.String msDbmsAddSorianaCodigo;
    protected java.lang.String msDbmsAddElektraOrder;
    protected java.lang.String msDbmsAddElektraBarcode;
    protected int mnDbmsAddElektraCages;
    protected double mdDbmsAddElektraCagePriceUnitary;
    protected int mnDbmsAddElektraParts;
    protected double mdDbmsAddElektraPartPriceUnitary;
    protected java.lang.String msDbmsAddJsonData;

    protected java.util.Vector<erp.mtrn.data.SDataDpsEntryNotes> mvDbmsEntryNotes;
    protected java.util.Vector<erp.mtrn.data.SDataDpsEntryTax> mvDbmsEntryTaxes;
    protected java.util.Vector<erp.mtrn.data.SDataDpsEntryCommissions> mvDbmsEntryCommissions;
    protected java.util.Vector<erp.mtrn.data.SDataDpsEntryPrice> mvDbmsEntryPrices;
    protected ArrayList<SDataDpsEntryAnalysis> mlDbmsDpsEntryAnalysis;
    protected ArrayList<SDataDpsEntryQuantityChange> mlDbmsDpsEntryQuantityChange;
    protected SDataDpsMaterialRequest moDbmsDpsEntryMatRequest;
    protected SDataDpsEntryItemComposition moDbmsDpsEntryItemComposition;

    protected java.util.Vector<erp.mtrn.data.SDataDpsDpsLink> mvDbmsDpsLinksAsSource;
    protected java.util.Vector<erp.mtrn.data.SDataDpsDpsLink> mvDbmsDpsLinksAsDestiny;
    protected java.util.Vector<erp.mtrn.data.SDataDpsDpsAdjustment> mvDbmsDpsAdjustmentsAsDps;
    protected java.util.Vector<erp.mtrn.data.SDataDpsDpsAdjustment> mvDbmsDpsAdjustmentsAsAdjustment;
    
    protected erp.mtrn.data.SDataDpsCfdEntry moDbmsDpsCfdEntry;

    protected int mnAuxPkDpsYearId; // DPS for adjustment DPS in record entries
    protected int mnAuxPkDpsDocId; // DPS for adjustment DPS in record entries
    protected int[] manAuxPkDpsEntryPrice;
    protected boolean mbAuxPreserveQuantity; // preserve customized quantity

    protected boolean mbFlagReadLinksAswell; // Read aswell links and adjustments
    protected boolean mbFlagMinorChangesEdited;
    protected boolean mbFlagOpenedByMatRequestImport;
    
    protected int mnXtaPkDpsYearConId; // DPS for adjustment DPS in contract
    protected int mnXtaPkDpsDocConId; // DPS for adjustment DPS in contract
    protected int mnXtaPkDpsDocEtyConId; // DPS for adjustment DPS in contract
    
    protected boolean mbXtaIsPurInvoiceInventoriable; 
    protected double mnXtaPriceCommUnitary;
    
    protected int mnAuxFkItemOld;
    protected double mdAuxQuantityOld;
    protected double mdAuxOriginalQuantityOld;
    protected int mnAuxFkUnitOld;
    protected int mnAuxFkOriginalUnitOld;
    
    /**
     * Overrides java.lang.Object.clone() function.
     */
    public SDataDpsEntry() {
        super(SDataConstants.TRN_DPS_ETY);

        mvDbmsEntryNotes = new Vector<>();
        mvDbmsEntryTaxes = new Vector<>();
        mvDbmsEntryCommissions = new Vector<>();
        mvDbmsEntryPrices = new Vector<>();
        mlDbmsDpsEntryAnalysis = new ArrayList<>();
        mlDbmsDpsEntryQuantityChange = new ArrayList<>();
        moDbmsDpsEntryMatRequest = null;
        moDbmsDpsEntryItemComposition = null;

        mvDbmsDpsLinksAsSource = new Vector<>();
        mvDbmsDpsLinksAsDestiny = new Vector<>();
        mvDbmsDpsAdjustmentsAsDps = new Vector<>();
        mvDbmsDpsAdjustmentsAsAdjustment = new Vector<>();

        mbFlagReadLinksAswell = true; // must be independent of a reset!

        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setConceptKey(java.lang.String s) { msConceptKey = s; }
    public void setConcept(java.lang.String s) { msConcept = s; }
    public void setReference(java.lang.String s) { msReference = s; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setIsDiscountDocApplying(boolean b) { mbIsDiscountDocApplying = b; }
    public void setIsDiscountUnitaryPercentage(boolean b) { mbIsDiscountUnitaryPercentage = b; }
    public void setIsDiscountUnitaryPercentageSystem(boolean b) { mbIsDiscountUnitaryPercentageSystem = b; }
    public void setIsDiscountEntryPercentage(boolean b) { mbIsDiscountEntryPercentage = b; }
    public void setDiscountUnitaryPercentage(double d) { mdDiscountUnitaryPercentage = d; }
    public void setDiscountUnitaryPercentageSystem(double d) { mdDiscountUnitaryPercentageSystem = d; }
    public void setDiscountEntryPercentage(double d) { mdDiscountEntryPercentage = d; }
    public void setPriceUnitary(double d) { mdPriceUnitary = d; }
    public void setPriceUnitarySystem(double d) { mdPriceUnitarySystem = d; }
    public void setDiscountUnitary(double d) { mdDiscountUnitary = d; }
    public void setDiscountUnitarySystem(double d) { mdDiscountUnitarySystem = d; }
    public void setDiscountEntry(double d) { mdDiscountEntry = d; }
    public void setSubtotalProvisional_r(double d) { mdSubtotalProvisional_r = d; }
    public void setDiscountDoc(double d) { mdDiscountDoc = d; }
    public void setSubtotal_r(double d) { mdSubtotal_r = d; }
    public void setTaxCharged_r(double d) { mdTaxCharged_r = d; }
    public void setTaxRetained_r(double d) { mdTaxRetained_r = d; }
    public void setTotal_r(double d) { mdTotal_r = d; }
    public void setPriceUnitaryReal_r(double d) { mdPriceUnitaryReal_r = d; }
    public void setCommissions_r(double d) { mdCommissions_r = d; }
    public void setPriceUnitaryCy(double d) { mdPriceUnitaryCy = d; }
    public void setPriceUnitarySystemCy(double d) { mdPriceUnitarySystemCy = d; }
    public void setDiscountUnitaryCy(double d) { mdDiscountUnitaryCy = d; }
    public void setDiscountUnitarySystemCy(double d) { mdDiscountUnitarySystemCy = d; }
    public void setDiscountEntryCy(double d) { mdDiscountEntryCy = d; }
    public void setSubtotalProvisionalCy_r(double d) { mdSubtotalProvisionalCy_r = d; }
    public void setDiscountDocCy(double d) { mdDiscountDocCy = d; }
    public void setSubtotalCy_r(double d) { mdSubtotalCy_r = d; }
    public void setTaxChargedCy_r(double d) { mdTaxChargedCy_r = d; }
    public void setTaxRetainedCy_r(double d) { mdTaxRetainedCy_r = d; }
    public void setTotalCy_r(double d) { mdTotalCy_r = d; }
    public void setPriceUnitaryRealCy_r(double d) { mdPriceUnitaryRealCy_r = d; }
    public void setCommissionsCy_r(double d) { mdCommissionsCy_r = d; }
    public void setOriginalQuantity(double d) { mdOriginalQuantity = d; }
    public void setOriginalPriceUnitaryCy(double d) { mdOriginalPriceUnitaryCy = d; }
    public void setOriginalPriceUnitarySystemCy(double d) { mdOriginalPriceUnitarySystemCy = d; }
    public void setOriginalDiscountUnitaryCy(double d) { mdOriginalDiscountUnitaryCy = d; }
    public void setOriginalDiscountUnitarySystemCy(double d) { mdOriginalDiscountUnitarySystemCy = d; }
    public void setSalesPriceUnitaryCy(double d) { mdSalesPriceUnitaryCy = d; }
    public void setSalesFreightUnitaryCy(double d) { mdSalesFreightUnitaryCy = d; }
    public void setLength(double d) { mdLength = d; }
    public void setSurface(double d) { mdSurface = d; }
    public void setVolume(double d) { mdVolume = d; }
    public void setMass(double d) { mdMass = d; }
    public void setWeightPackagingExtra(double d) { mdWeightPackagingExtra = d; }
    public void setWeightGross(double d) { mdWeightGross = d; }
    public void setWeightDelivery(double d) { mdWeightDelivery = d; }
    public void setSurplusPercentage(double d) { mdSurplusPercentage = d; }
    public void setContractBase(double d) { mdContractBase = d; }
    public void setContractFuture(double d) { mdContractFuture = d; }
    public void setContractFactor(double d) { mdContractFactor = d; }
    public void setContractPriceYear(int n) { mnContractPriceYear = n; }
    public void setContractPriceMonth(int n) { mnContractPriceMonth = n; }
    public void setSealQuality(java.lang.String s) { msSealQuality = s; }
    public void setSealSecurity(java.lang.String s) { msSealSecurity = s; }
    public void setDriver(java.lang.String s) { msDriver = s; }
    public void setPlate(java.lang.String s) { msPlate = s; }
    public void setTicket(java.lang.String s) { msTicket = s; }
    public void setContainerTank(java.lang.String s) { msContainerTank = s; }
    public void setVgm(java.lang.String s) { msVgm = s; }
    public void setOperationsType(int n) { mnOperationsType = n; }
    public void setUserId(int n) { mnUserId = n; }
    public void setSortingPosition(int n) { mnSortingPosition = n; }
    public void setIsPrepayment(boolean b) { mbIsPrepayment = b; }
    public void setIsDiscountRetailChain(boolean b) { mbIsDiscountRetailChain = b; }
    public void setIsTaxesAutomaticApplying(boolean b) { mbIsTaxesAutomaticApplying = b; }
    public void setIsPriceVariable(boolean b) { mbIsPriceVariable = b; }
    public void setIsPriceConfirm(boolean b) { mbIsPriceConfirm = b; }
    public void setIsSalesFreightRequired(boolean b) { mbIsSalesFreightRequired = b; }
    public void setIsSalesFreightConfirm(boolean b) { mbIsSalesFreightConfirm = b; }
    public void setIsSalesFreightAdd(boolean b) { mbIsSalesFreightAdd = b; }
    public void setIsInventoriable(boolean b) { mbIsInventoriable = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkOriginalUnitId(int n) { mnFkOriginalUnitId = n; }
    public void setFkTaxRegionId(int n) { mnFkTaxRegionId = n; }
    public void setFkThirdTaxCausingId_n(int n) { mnFkThirdTaxCausingId_n = n; }
    public void setFkDpsAdjustmentTypeId(int n) { mnFkDpsAdjustmentTypeId = n; }
    public void setFkDpsAdjustmentSubtypeId(int n) { mnFkDpsAdjustmentSubtypeId = n; }
    public void setFkDpsEntryTypeId(int n) { mnFkDpsEntryTypeId = n; }
    public void setFkVehicleTypeId_n(int n) { mnFkVehicleTypeId_n = n; }
    public void setFkCashCompanyBranchId_n(int n) { mnFkCashCompanyBranchId_n = n; }
    public void setFkCashAccountId_n(int n) { mnFkCashAccountId_n = n; }
    public void setFkCostCenterId_n(java.lang.String s) { msFkCostCenterId_n = s; }
    public void setFkItemRefId_n(int n) { mnFkItemRefId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public java.lang.String getConceptKey() { return msConceptKey; }
    public java.lang.String getConcept() { return msConcept; }
    public java.lang.String getReference() { return msReference; }
    public double getQuantity() { return mdQuantity; }
    public boolean getIsDiscountDocApplying() { return mbIsDiscountDocApplying; }
    public boolean getIsDiscountUnitaryPercentage() { return mbIsDiscountUnitaryPercentage; }
    public boolean getIsDiscountUnitaryPercentageSystem() { return mbIsDiscountUnitaryPercentageSystem; }
    public boolean getIsDiscountEntryPercentage() { return mbIsDiscountEntryPercentage; }
    public double getDiscountUnitaryPercentage() { return mdDiscountUnitaryPercentage; }
    public double getDiscountUnitaryPercentageSystem() { return mdDiscountUnitaryPercentageSystem; }
    public double getDiscountEntryPercentage() { return mdDiscountEntryPercentage; }
    public double getPriceUnitary() { return mdPriceUnitary; }
    public double getPriceUnitarySystem() { return mdPriceUnitarySystem; }
    public double getDiscountUnitary() { return mdDiscountUnitary; }
    public double getDiscountUnitarySystem() { return mdDiscountUnitarySystem; }
    public double getDiscountEntry() { return mdDiscountEntry; }
    public double getSubtotalProvisional_r() { return mdSubtotalProvisional_r; }
    public double getDiscountDoc() { return mdDiscountDoc; }
    public double getSubtotal_r() { return mdSubtotal_r; }
    public double getTaxCharged_r() { return mdTaxCharged_r; }
    public double getTaxRetained_r() { return mdTaxRetained_r; }
    public double getTotal_r() { return mdTotal_r; }
    public double getPriceUnitaryReal_r() { return mdPriceUnitaryReal_r; }
    public double getCommissions_r() { return mdCommissions_r; }
    public double getPriceUnitaryCy() { return mdPriceUnitaryCy; }
    public double getPriceUnitarySystemCy() { return mdPriceUnitarySystemCy; }
    public double getDiscountUnitaryCy() { return mdDiscountUnitaryCy; }
    public double getDiscountUnitarySystemCy() { return mdDiscountUnitarySystemCy; }
    public double getDiscountEntryCy() { return mdDiscountEntryCy; }
    public double getSubtotalProvisionalCy_r() { return mdSubtotalProvisionalCy_r; }
    public double getDiscountDocCy() { return mdDiscountDocCy; }
    public double getSubtotalCy_r() { return mdSubtotalCy_r; }
    public double getTaxChargedCy_r() { return mdTaxChargedCy_r; }
    public double getTaxRetainedCy_r() { return mdTaxRetainedCy_r; }
    public double getTotalCy_r() { return mdTotalCy_r; }
    public double getPriceUnitaryRealCy_r() { return mdPriceUnitaryRealCy_r; }
    public double getCommissionsCy_r() { return mdCommissionsCy_r; }
    public double getOriginalQuantity() { return mdOriginalQuantity; }
    public double getOriginalPriceUnitaryCy() { return mdOriginalPriceUnitaryCy; }
    public double getOriginalPriceUnitarySystemCy() { return mdOriginalPriceUnitarySystemCy; }
    public double getOriginalDiscountUnitaryCy() { return mdOriginalDiscountUnitaryCy; }
    public double getOriginalDiscountUnitarySystemCy() { return mdOriginalDiscountUnitarySystemCy; }
    public double getSalesPriceUnitaryCy() { return mdSalesPriceUnitaryCy; }
    public double getSalesFreightUnitaryCy() { return mdSalesFreightUnitaryCy; }
    public double getLength() { return mdLength; }
    public double getSurface() { return mdSurface; }
    public double getVolume() { return mdVolume; }
    public double getMass() { return mdMass; }
    public double getWeightPackagingExtra() { return mdWeightPackagingExtra; }
    public double getWeightGross() { return mdWeightGross; }
    public double getWeightDelivery() { return mdWeightDelivery; }
    public double getSurplusPercentage() { return mdSurplusPercentage; }
    public double getContractBase() { return mdContractBase; }
    public double getContractFuture() { return mdContractFuture; }
    public double getContractFactor() { return mdContractFactor; }
    public int getContractPriceYear() { return mnContractPriceYear; }
    public int getContractPriceMonth() { return mnContractPriceMonth; }
    public java.lang.String getSealQuality() { return msSealQuality; }
    public java.lang.String getSealSecurity() { return msSealSecurity; }
    public java.lang.String getDriver() { return msDriver; }
    public java.lang.String getPlate() { return msPlate; }
    public java.lang.String getTicket() { return msTicket; }
    public java.lang.String getContainerTank() { return msContainerTank; }
    public java.lang.String getVgm() { return msVgm; }
    public int getOperationsType() { return mnOperationsType; }
    public int getUserId() { return mnUserId; }
    public int getSortingPosition() { return mnSortingPosition; }
    public boolean getIsPrepayment() { return mbIsPrepayment; }
    public boolean getIsDiscountRetailChain() { return mbIsDiscountRetailChain; }
    public boolean getIsTaxesAutomaticApplying() { return mbIsTaxesAutomaticApplying; }
    public boolean getIsPriceVariable() { return mbIsPriceVariable; }
    public boolean getIsPriceConfirm() { return mbIsPriceConfirm; }
    public boolean getIsSalesFreightRequired() { return mbIsSalesFreightRequired; }
    public boolean getIsSalesFreightConfirm() { return mbIsSalesFreightConfirm; }
    public boolean getIsSalesFreightAdd() { return mbIsSalesFreightAdd; }
    public boolean getIsInventoriable() { return mbIsInventoriable; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkOriginalUnitId() { return mnFkOriginalUnitId; }
    public int getFkTaxRegionId() { return mnFkTaxRegionId; }
    public int getFkThirdTaxCausingId_n() { return mnFkThirdTaxCausingId_n; }
    public int getFkDpsAdjustmentTypeId() { return mnFkDpsAdjustmentTypeId; }
    public int getFkDpsAdjustmentSubtypeId() { return mnFkDpsAdjustmentSubtypeId; }
    public int getFkDpsEntryTypeId() { return mnFkDpsEntryTypeId; }
    public int getFkVehicleTypeId_n() { return mnFkVehicleTypeId_n; }
    public int getFkCashCompanyBranchId_n() { return mnFkCashCompanyBranchId_n; }
    public int getFkCashAccountId_n() { return mnFkCashAccountId_n; }
    public java.lang.String getFkCostCenterId_n() { return msFkCostCenterId_n; }
    public int getFkItemRefId_n() { return mnFkItemRefId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsFkItemGenericId(int n) { mnDbmsFkItemGenericId = n; }
    public void setDbmsItemGenDataShipDomesticReq(boolean b) { mbDbmsItemGenDataShipDomesticReq = b; }
    public void setDbmsItemGenDataShipInternationalReq(boolean b) { mbDbmsItemGenDataShipInternationalReq = b; }
    public void setDbmsItemGenDataShipQualityReq(boolean b) { mbDbmsItemGenDataShipQualityReq = b; }
    public void setDbmsItem(java.lang.String s) { msDbmsItem = s; }
    public void setDbmsUnitSymbol(java.lang.String s) { msDbmsUnitSymbol = s; }
    public void setDbmsOriginalUnitSymbol(java.lang.String s) { msDbmsOriginalUnitSymbol = s; }
    public void setDbmsOriginalUnitBaseEquivalence(double d) { mdDbmsOriginalUnitBaseEquivalence = d; }
    public void setDbmsTaxRegion(java.lang.String s) { msDbmsTaxRegion = s; }
    public void setDbmsDpsAdjustmentType(java.lang.String s) { msDbmsDpsAdjustmentType = s; }
    public void setDbmsDpsEntryType(java.lang.String s) { msDbmsDpsEntryType = s; }
    public void setDbmsCostCenterCode(java.lang.String s) { msDbmsCostCenterCode = s; }
    public void setDbmsCostCenter_n(java.lang.String s) { msDbmsCostCenter_n = s; }
    public void setDbmsItemRef_n(java.lang.String s) { msDbmsItemRef_n = s; }
    public void setDbmsItemBrandId(int n) { mnDbmsItemBrandId = n; }
    public void setDbmsItemBrand(java.lang.String s) { msDbmsItemBrand = s; }
    public void setDbmsItemClaveProdServ(java.lang.String s) { msDbmsItemClaveProdServ = s; }
    public void setDbmsUnidadClave(java.lang.String s) { msDbmsUnidadClave = s; }
    public void setDbmsOriginalUnidadClave(java.lang.String s) { msDbmsOriginalUnidadClave = s; }
    public void setDbmsTariff(java.lang.String s) { msDbmsTariff = s; }
    public void setDbmsCustomsUnit(java.lang.String s) { msDbmsCustomsUnit = s; }
    public void setDbmsCustomsUnitSymbol(java.lang.String s) { msDbmsCustomsUnitSymbol = s; }
    public void setDbmsItemCustomsUnitSymbol(java.lang.String s) { msDbmsItemCustomsUnitSymbol = s; }
    public void setDbmsItemCustomsUnitEquivalence(double d) { mdDbmsItemCustomsUnitEquivalence = d; }
    public void setDbmsAddBachocoNumeroPosicion(int n) { mnDbmsAddBachocoNumeroPosicion = n; }
    public void setDbmsAddBachocoCentro(java.lang.String s) { msDbmsAddBachocoCentro = s; }
    public void setDbmsAddLorealEntryNumber(int n) { mnDbmsAddLorealEntryNumber = n; }
    public void setDbmsAddSorianaCodigo(java.lang.String s) { msDbmsAddSorianaCodigo = s; }
    public void setDbmsAddElektraOrder(java.lang.String s) { msDbmsAddElektraOrder = s; }
    public void setDbmsAddElektraBarcode(java.lang.String s) { msDbmsAddElektraBarcode = s; }
    public void setDbmsAddElektraCages(int n) { mnDbmsAddElektraCages = n; }
    public void setDbmsAddElektraCagePriceUnitary(double d) { mdDbmsAddElektraCagePriceUnitary = d; }
    public void setDbmsAddElektraParts(int n) { mnDbmsAddElektraParts = n; }
    public void setDbmsAddElektraPartPriceUnitary(double d) { mdDbmsAddElektraPartPriceUnitary = d; }
    public void setDbmsAddJsonData(java.lang.String s) { msDbmsAddJsonData = s; }
    public void setDbmsDpsEntryMatRequest(SDataDpsMaterialRequest o) { moDbmsDpsEntryMatRequest = o; }
    public void setDbmsDpsEntryItemComposition(SDataDpsEntryItemComposition o) { moDbmsDpsEntryItemComposition = o; }

    public int getDbmsFkItemGenericId() { return mnDbmsFkItemGenericId; }
    public boolean getDbmsItemGenDataShipDomesticReq() { return mbDbmsItemGenDataShipDomesticReq; }
    public boolean getDbmsItemGenDataShipInternationalReq() { return mbDbmsItemGenDataShipInternationalReq; }
    public boolean getDbmsItemGenDataShipQualityReq() { return mbDbmsItemGenDataShipQualityReq; }
    public java.lang.String getDbmsItem() { return msDbmsItem; }
    public java.lang.String getDbmsUnitSymbol() { return msDbmsUnitSymbol; }
    public java.lang.String getDbmsOriginalUnitSymbol() { return msDbmsOriginalUnitSymbol; }
    public double getDbmsOriginalUnitBaseEquivalence() { return mdDbmsOriginalUnitBaseEquivalence; }
    public java.lang.String getDbmsTaxRegion() { return msDbmsTaxRegion; }
    public java.lang.String getDbmsDpsAdjustmentType() { return msDbmsDpsAdjustmentType; }
    public java.lang.String getDbmsDpsEntryType() { return msDbmsDpsEntryType; }
    public java.lang.String getDbmsCostCenterCode() { return msDbmsCostCenterCode; }
    public java.lang.String getDbmsCostCenter_n() { return msDbmsCostCenter_n; }
    public java.lang.String getDbmsItemRef_n() { return msDbmsItemRef_n; }
    public int getDbmsItemBrandId() { return mnDbmsItemBrandId; }
    public java.lang.String getDbmsItemBrand() { return msDbmsItemBrand; }
    public java.lang.String getDbmsItemClaveProdServ() { return msDbmsItemClaveProdServ; }
    public java.lang.String getDbmsUnidadClave() { return msDbmsUnidadClave; }
    public java.lang.String getDbmsOriginalUnidadClave() { return msDbmsOriginalUnidadClave; }
    public java.lang.String getDbmsTariff() { return msDbmsTariff; }
    public java.lang.String getDbmsCustomsUnit() { return msDbmsCustomsUnit; }
    public java.lang.String getDbmsCustomsUnitSymbol() { return msDbmsCustomsUnitSymbol; }
    public java.lang.String getDbmsItemCustomsUnitSymbol() { return msDbmsItemCustomsUnitSymbol; }
    public double getDbmsItemCustomsUnitEquivalence() { return mdDbmsItemCustomsUnitEquivalence; }
    public int getDbmsAddBachocoNumeroPosicion() { return mnDbmsAddBachocoNumeroPosicion; }
    public java.lang.String getDbmsAddBachocoCentro() { return msDbmsAddBachocoCentro; }
    public int getDbmsAddLorealEntryNumber() { return mnDbmsAddLorealEntryNumber; }
    public java.lang.String getDbmsAddSorianaCodigo() { return msDbmsAddSorianaCodigo; }
    public java.lang.String getDbmsAddElektraOrder() { return msDbmsAddElektraOrder; }
    public java.lang.String getDbmsAddElektraBarcode() { return msDbmsAddElektraBarcode; }
    public int getDbmsAddElektraCages() { return mnDbmsAddElektraCages; }
    public double getDbmsAddElektraCagePriceUnitary() { return mdDbmsAddElektraCagePriceUnitary; }
    public int getDbmsAddElektraParts() { return mnDbmsAddElektraParts; }
    public double getDbmsAddElektraPartPriceUnitary() { return mdDbmsAddElektraPartPriceUnitary; }
    public java.lang.String getDbmsAddJsonData() { return msDbmsAddJsonData; }

    public java.util.Vector<erp.mtrn.data.SDataDpsEntryNotes> getDbmsEntryNotes() { return mvDbmsEntryNotes; }
    public java.util.Vector<erp.mtrn.data.SDataDpsEntryTax> getDbmsEntryTaxes() { return mvDbmsEntryTaxes; }
    public java.util.Vector<erp.mtrn.data.SDataDpsEntryCommissions> getDbmsEntryCommissions() { return mvDbmsEntryCommissions; }
    public java.util.Vector<erp.mtrn.data.SDataDpsEntryPrice> getDbmsEntryPrices() { return mvDbmsEntryPrices; }
    public ArrayList<SDataDpsEntryAnalysis> getDbmsDpsEntryAnalysis() { return mlDbmsDpsEntryAnalysis; }
    public ArrayList<SDataDpsEntryQuantityChange> getDbmsDpsEntryQuantityChange() { return mlDbmsDpsEntryQuantityChange; }
    public SDataDpsMaterialRequest getDbmsDpsEntryMatRequestLink() { return moDbmsDpsEntryMatRequest; }
    public SDataDpsEntryItemComposition getDbmsDpsEntryItemComposition() { return moDbmsDpsEntryItemComposition; }

    public java.util.Vector<erp.mtrn.data.SDataDpsDpsLink> getDbmsDpsLinksAsSource() { return mvDbmsDpsLinksAsSource; }
    public java.util.Vector<erp.mtrn.data.SDataDpsDpsLink> getDbmsDpsLinksAsDestiny() { return mvDbmsDpsLinksAsDestiny; }
    public java.util.Vector<erp.mtrn.data.SDataDpsDpsAdjustment> getDbmsDpsAdjustmentsAsDps() { return mvDbmsDpsAdjustmentsAsDps; }
    public java.util.Vector<erp.mtrn.data.SDataDpsDpsAdjustment> getDbmsDpsAdjustmentsAsAdjustment() { return mvDbmsDpsAdjustmentsAsAdjustment; }

    public void setDbmsDpsCfdEntry(erp.mtrn.data.SDataDpsCfdEntry o) { moDbmsDpsCfdEntry = o; }

    public erp.mtrn.data.SDataDpsCfdEntry getDbmsDpsCfdEntry() { return moDbmsDpsCfdEntry; }

    public void setAuxPkDpsYearId(int n) { mnAuxPkDpsYearId = n; }
    public void setAuxPkDpsDocId(int n) { mnAuxPkDpsDocId = n; }
    public void setAuxPkDpsEntryPrice(int[] n) { manAuxPkDpsEntryPrice = n; }
    public void setAuxPreserveQuantity(boolean b) { mbAuxPreserveQuantity = b; }

    public int getAuxPkDpsYearId() { return mnAuxPkDpsYearId; }
    public int getAuxPkDpsDocId() { return mnAuxPkDpsDocId; }
    public int[] getAuxPkDpsEntryPriceId() { return manAuxPkDpsEntryPrice; }
    public boolean isAuxPreserveQuantity() { return mbAuxPreserveQuantity; }

    public void setFlagReadLinksAswell(boolean b) { mbFlagReadLinksAswell = b; }
    public void setFlagMinorChangesEdited(boolean b) { mbFlagMinorChangesEdited = b; }
    public void setFlagOpenedByMatRequestImport(boolean b) { mbFlagOpenedByMatRequestImport = b; }
    
    public boolean getFlagReadLinksAswell() { return mbFlagReadLinksAswell; }
    public boolean getFlagMinorChangesEdited() { return mbFlagMinorChangesEdited; }
    public boolean getFlagOpenedByMatRequestImport() { return mbFlagOpenedByMatRequestImport; }

    public int[] getKeyDps() { return new int[] { mnPkYearId, mnPkDocId }; }
    public int[] getKeyDpsAdjustmentType() { return new int[] { mnFkDpsAdjustmentTypeId }; }
    public int[] getKeyDpsAdjustmentSubtype() { return new int[] { mnFkDpsAdjustmentTypeId, mnFkDpsAdjustmentSubtypeId }; }
    public int[] getKeyCashAccount_n() { return mnFkCashCompanyBranchId_n == SLibConsts.UNDEFINED ? null : new int[] { mnFkCashCompanyBranchId_n, mnFkCashAccountId_n }; }
    public int[] getKeyAuxDps() { return new int[] { mnAuxPkDpsYearId, mnAuxPkDpsDocId }; }
    
    public void setXtaIsPurInvoiceInventoriable(boolean b) { mbXtaIsPurInvoiceInventoriable = b; }
    public void setXtaPriceCommUnitary(double n) { mnXtaPriceCommUnitary = n; }
    
    public boolean getXtaIsPurInvoiceInventoriable() { return mbXtaIsPurInvoiceInventoriable; }
    public double getXtaPriceCommUnitary() { return mnXtaPriceCommUnitary; }
    
    public void setAuxFkItemOld(int i) { mnAuxFkItemOld = i; }
    public void setAuxQuantityOld(double n) { mdAuxQuantityOld = n; }
    public void setAuxOriginalQuantityOld(double n) { mdAuxOriginalQuantityOld = n; }
    public void setAuxFkUnitOld(int i) { mnAuxFkUnitOld = i; }
    public void setAuxFkOriginalUnitOld(int i) { mnAuxFkOriginalUnitOld = i; }
    
    public int getAuxFkItemOld() { return mnAuxFkItemOld; }
    public double getAuxQuantityOld() { return mdAuxQuantityOld; }
    public double getAuxOriginalQuantityOld() { return mdAuxOriginalQuantityOld; }
    public int getAuxFkUnitOld() { return mnAuxFkUnitOld; }
    public int getAuxFkOriginalUnitOld() { return mnAuxFkOriginalUnitOld; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkEntryId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        msConceptKey = "";
        msConcept = "";
        msReference = "";
        mdQuantity = 0;
        mbIsDiscountDocApplying = false;
        mbIsDiscountUnitaryPercentage = false;
        mbIsDiscountUnitaryPercentageSystem = false;
        mbIsDiscountEntryPercentage = false;
        mdDiscountUnitaryPercentage = 0;
        mdDiscountUnitaryPercentageSystem = 0;
        mdDiscountEntryPercentage = 0;
        mdPriceUnitary = 0;
        mdPriceUnitarySystem = 0;
        mdDiscountUnitary = 0;
        mdDiscountUnitarySystem = 0;
        mdDiscountEntry = 0;
        mdSubtotalProvisional_r = 0;
        mdDiscountDoc = 0;
        mdSubtotal_r = 0;
        mdTaxCharged_r = 0;
        mdTaxRetained_r = 0;
        mdTotal_r = 0;
        mdPriceUnitaryReal_r = 0;
        mdCommissions_r = 0;
        mdPriceUnitaryCy = 0;
        mdPriceUnitarySystemCy = 0;
        mdDiscountUnitaryCy = 0;
        mdDiscountUnitarySystemCy = 0;
        mdDiscountEntryCy = 0;
        mdSubtotalProvisionalCy_r = 0;
        mdDiscountDocCy = 0;
        mdSubtotalCy_r = 0;
        mdTaxChargedCy_r = 0;
        mdTaxRetainedCy_r = 0;
        mdTotalCy_r = 0;
        mdPriceUnitaryRealCy_r = 0;
        mdCommissionsCy_r = 0;
        mdOriginalQuantity = 0;
        mdOriginalPriceUnitaryCy = 0;
        mdOriginalPriceUnitarySystemCy = 0;
        mdOriginalDiscountUnitaryCy = 0;
        mdOriginalDiscountUnitarySystemCy = 0;
        mdSalesPriceUnitaryCy = 0;
        mdSalesFreightUnitaryCy = 0;
        mdLength = 0;
        mdSurface = 0;
        mdVolume = 0;
        mdMass = 0;
        mdWeightPackagingExtra = 0;
        mdWeightGross = 0;
        mdWeightDelivery = 0;
        mdSurplusPercentage = 0;
        mdContractBase = 0;
        mdContractFuture = 0;
        mdContractFactor = 0;
        mnContractPriceYear = 0;
        mnContractPriceMonth = 0;
        msSealQuality = "";
        msSealSecurity = "";
        msDriver = "";
        msPlate = "";
        msTicket = "";
        msContainerTank = "";
        msVgm = "";
        mnOperationsType = SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS; // just for keeping consistence with existing source code
        mnUserId = 0;
        mnSortingPosition = 0;
        mbIsPrepayment = false;
        mbIsDiscountRetailChain = false;
        mbIsTaxesAutomaticApplying = false;
        mbIsPriceVariable = false;
        mbIsPriceConfirm = false;
        mbIsSalesFreightRequired = false;
        mbIsSalesFreightConfirm = false;
        mbIsSalesFreightAdd = false;
        mbIsInventoriable = false;
        mbIsDeleted = false;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkOriginalUnitId = 0;
        mnFkTaxRegionId = 0;
        mnFkThirdTaxCausingId_n = 0;
        mnFkDpsAdjustmentTypeId = 0;
        mnFkDpsAdjustmentSubtypeId = 0;
        mnFkDpsEntryTypeId = 0;
        mnFkVehicleTypeId_n = 0;
        mnFkCashCompanyBranchId_n = 0;
        mnFkCashAccountId_n = 0;
        msFkCostCenterId_n = "";
        mnFkItemRefId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mnDbmsFkItemGenericId = 0;
        mbDbmsItemGenDataShipDomesticReq = false;
        mbDbmsItemGenDataShipInternationalReq = false;
        mbDbmsItemGenDataShipQualityReq = false;
        msDbmsItem = "";
        msDbmsUnitSymbol = "";
        msDbmsOriginalUnitSymbol = "";
        mdDbmsOriginalUnitBaseEquivalence = 0;
        msDbmsTaxRegion = "";
        msDbmsDpsAdjustmentType = "";
        msDbmsDpsEntryType = "";
        msDbmsCostCenterCode = "";
        msDbmsCostCenter_n = "";
        msDbmsItemRef_n = "";
        mnDbmsItemBrandId = 0;
        msDbmsItemBrand = "";
        msDbmsItemClaveProdServ = "";
        msDbmsUnidadClave = "";
        msDbmsOriginalUnidadClave = "";
        msDbmsTariff = "";
        msDbmsCustomsUnit = "";
        msDbmsCustomsUnitSymbol = "";
        msDbmsItemCustomsUnitSymbol = "";
        mdDbmsItemCustomsUnitEquivalence = 0;
        mnDbmsAddBachocoNumeroPosicion = 0;
        msDbmsAddBachocoCentro = "";
        mnDbmsAddLorealEntryNumber = 0;
        msDbmsAddSorianaCodigo = "";
        msDbmsAddElektraOrder = "";
        msDbmsAddElektraBarcode = "";
        mnDbmsAddElektraCages = 0;
        mdDbmsAddElektraCagePriceUnitary = 0;
        mnDbmsAddElektraParts = 0;
        mdDbmsAddElektraPartPriceUnitary = 0;
        msDbmsAddJsonData = "";

        mvDbmsEntryNotes.clear();
        mvDbmsEntryTaxes.clear();
        mvDbmsEntryCommissions.clear();
        mvDbmsEntryPrices.clear();
        mlDbmsDpsEntryAnalysis.clear();
        mlDbmsDpsEntryQuantityChange.clear();
        moDbmsDpsEntryMatRequest = null;
        moDbmsDpsEntryItemComposition = null;

        mvDbmsDpsLinksAsSource.clear();
        mvDbmsDpsLinksAsDestiny.clear();
        mvDbmsDpsAdjustmentsAsDps.clear();
        mvDbmsDpsAdjustmentsAsAdjustment.clear();
        
        moDbmsDpsCfdEntry = null;

        mnAuxPkDpsYearId = 0;
        mnAuxPkDpsDocId = 0;
        manAuxPkDpsEntryPrice = null;
        mbAuxPreserveQuantity = false;
        
        //mbFlagReadLinksAswell = false; // must be independent of a reset!
        mbFlagMinorChangesEdited = false;
        mbFlagOpenedByMatRequestImport = false;
    
        mnXtaPkDpsYearConId = 0;
        mnXtaPkDpsDocConId = 0;
        mnXtaPkDpsDocEtyConId = 0;
        
        mbXtaIsPurInvoiceInventoriable = false;
        mnXtaPriceCommUnitary = 0;
        
        mnAuxFkItemOld = 0;
        mdAuxQuantityOld = 0;
        mdAuxOriginalQuantityOld = 0;
        mnAuxFkUnitOld = 0;
        mnAuxFkOriginalUnitOld = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;
        Statement statementAux = null;
        int nCveProdSerItem_n = 0;
        int nCveProdSerIgen = 0;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT de.*, " +
                    "i.item, i.fid_cfd_prod_serv_n, i.tariff, i.custs_unit, i.custs_equiv, i.fid_igen, i.fid_brd, " +
                    "igen.b_ship_dom, igen.b_ship_int, igen.b_ship_qlt, igen.fid_cfd_prod_serv, " +
                    "u.symbol, ou.symbol, ou.unit_base_equiv, ou.symbol_custs, ou.unit_custs, cu.code, ocu.code, brd.brd, " +
                    "tr.tax_reg, tda.stp_dps_adj, tde.tp_dps_ety, cc.code, cc.cc, ir.item, ade.bac_num_pos, ade.bac_cen, ade.lor_num_ety, ade.sor_cod, " +
                    "ade.ele_ord, ade.ele_barc, ade.ele_cag, ade.ele_cag_price_u, ade.ele_par, ade.ele_par_price_u, ade.json_data " +
                    "FROM trn_dps_ety AS de " +
                    "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item " +
                    "INNER JOIN erp.itmu_igen AS igen ON i.fid_igen = igen.id_igen " +
                    "INNER JOIN erp.itmu_unit as u ON de.fid_unit = u.id_unit " +
                    "INNER JOIN erp.itmu_unit as ou ON de.fid_orig_unit = ou.id_unit " +
                    "INNER JOIN erp.itms_cfd_unit AS cu ON u.fid_cfd_unit = cu.id_cfd_unit " +
                    "INNER JOIN erp.itms_cfd_unit AS ocu ON ou.fid_cfd_unit = ocu.id_cfd_unit " +
                    "INNER JOIN erp.itmu_brd AS brd ON i.fid_brd = brd.id_brd " +
                    "INNER JOIN erp.finu_tax_reg AS tr ON de.fid_tax_reg = tr.id_tax_reg " +
                    "INNER JOIN erp.trns_stp_dps_adj AS tda ON de.fid_tp_dps_adj = tda.id_tp_dps_adj AND de.fid_stp_dps_adj = tda.id_stp_dps_adj " +
                    "INNER JOIN erp.trns_tp_dps_ety AS tde ON de.fid_tp_dps_ety = tde.id_tp_dps_ety " +
                    "LEFT OUTER JOIN fin_cc AS cc ON de.fid_cc_n = cc.id_cc " +
                    "LEFT OUTER JOIN erp.itmu_item AS ir ON de.fid_item_ref_n = ir.id_item " +
                    "LEFT OUTER JOIN trn_dps_add_ety AS ade ON de.id_year = ade.id_year AND de.id_doc = ade.id_doc AND de.id_ety = ade.id_ety " +
                    "WHERE de.id_year = " + key[0] + " AND de.id_doc = " + key[1] + " AND de.id_ety = " + key[2] +  " ";

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("de.id_year");
                mnPkDocId = resultSet.getInt("de.id_doc");
                mnPkEntryId = resultSet.getInt("de.id_ety");
                msConceptKey = resultSet.getString("de.concept_key");
                msConcept = resultSet.getString("de.concept");
                msReference = resultSet.getString("de.ref");
                mdQuantity = resultSet.getDouble("de.qty");
                mbIsDiscountDocApplying = resultSet.getBoolean("de.b_disc_doc");
                mbIsDiscountUnitaryPercentage = resultSet.getBoolean("de.b_disc_u_per");
                mbIsDiscountUnitaryPercentageSystem = resultSet.getBoolean("de.b_disc_u_per_sys");
                mbIsDiscountEntryPercentage = resultSet.getBoolean("de.b_disc_ety_per");
                mdDiscountUnitaryPercentage = resultSet.getDouble("de.disc_u_per");
                mdDiscountUnitaryPercentageSystem = resultSet.getDouble("de.disc_u_per_sys");
                mdDiscountEntryPercentage = resultSet.getDouble("de.disc_ety_per");
                mdPriceUnitary = resultSet.getDouble("de.price_u");
                mdPriceUnitarySystem = resultSet.getDouble("de.price_u_sys");
                mdDiscountUnitary = resultSet.getDouble("de.disc_u");
                mdDiscountUnitarySystem = resultSet.getDouble("de.disc_u_sys");
                mdDiscountEntry = resultSet.getDouble("de.disc_ety");
                mdSubtotalProvisional_r = resultSet.getDouble("de.stot_prov_r");
                mdDiscountDoc = resultSet.getDouble("de.disc_doc");
                mdSubtotal_r = resultSet.getDouble("de.stot_r");
                mdTaxCharged_r = resultSet.getDouble("de.tax_charged_r");
                mdTaxRetained_r = resultSet.getDouble("de.tax_retained_r");
                mdTotal_r = resultSet.getDouble("de.tot_r");
                mdPriceUnitaryReal_r = resultSet.getDouble("de.price_u_real_r");
                mdCommissions_r = resultSet.getDouble("de.comms_r");
                mdPriceUnitaryCy = resultSet.getDouble("de.price_u_cur");
                mdPriceUnitarySystemCy = resultSet.getDouble("de.price_u_sys_cur");
                mdDiscountUnitaryCy = resultSet.getDouble("de.disc_u_cur");
                mdDiscountUnitarySystemCy = resultSet.getDouble("de.disc_u_sys_cur");
                mdDiscountEntryCy = resultSet.getDouble("de.disc_ety_cur");
                mdSubtotalProvisionalCy_r = resultSet.getDouble("de.stot_prov_cur_r");
                mdDiscountDocCy = resultSet.getDouble("de.disc_doc_cur");
                mdSubtotalCy_r = resultSet.getDouble("de.stot_cur_r");
                mdTaxChargedCy_r = resultSet.getDouble("de.tax_charged_cur_r");
                mdTaxRetainedCy_r = resultSet.getDouble("de.tax_retained_cur_r");
                mdTotalCy_r = resultSet.getDouble("de.tot_cur_r");
                mdPriceUnitaryRealCy_r = resultSet.getDouble("de.price_u_real_cur_r");
                mdCommissionsCy_r = resultSet.getDouble("de.comms_cur_r");
                mdOriginalQuantity = resultSet.getDouble("de.orig_qty");
                mdOriginalPriceUnitaryCy = resultSet.getDouble("de.orig_price_u_cur");
                mdOriginalPriceUnitarySystemCy = resultSet.getDouble("de.orig_price_u_sys_cur");
                mdOriginalDiscountUnitaryCy = resultSet.getDouble("de.orig_disc_u_cur");
                mdOriginalDiscountUnitarySystemCy = resultSet.getDouble("de.orig_disc_u_sys_cur");
                mdSalesPriceUnitaryCy = resultSet.getDouble("de.sales_price_u_cur");
                mdSalesFreightUnitaryCy = resultSet.getDouble("de.sales_freight_u_cur");
                mdLength = resultSet.getDouble("de.len");
                mdSurface = resultSet.getDouble("de.surf");
                mdVolume = resultSet.getDouble("de.vol");
                mdMass = resultSet.getDouble("de.mass");
                mdWeightPackagingExtra = resultSet.getDouble("de.weight_pack_extra");
                mdWeightGross = resultSet.getDouble("de.weight_gross");
                mdWeightDelivery = resultSet.getDouble("de.weight_delivery");
                mdSurplusPercentage = resultSet.getDouble("de.surplus_per");
                mdContractBase = resultSet.getDouble("de.con_base");
                mdContractFuture = resultSet.getDouble("de.con_future");
                mdContractFactor = resultSet.getDouble("de.con_factor");
                mnContractPriceYear = resultSet.getInt("de.con_prc_year");
                mnContractPriceMonth = resultSet.getInt("de.con_prc_mon");
                msSealQuality = resultSet.getString("de.seal_qlt");
                msSealSecurity = resultSet.getString("de.seal_sec");
                msDriver = resultSet.getString("de.driver");
                msPlate = resultSet.getString("de.plate");
                msTicket = resultSet.getString("de.ticket");
                msContainerTank = resultSet.getString("de.cont_tank");
                msVgm = resultSet.getString("de.vgm");
                mnOperationsType = resultSet.getInt("de.ops_type");
                mnUserId = resultSet.getInt("de.usr_id");
                mnSortingPosition = resultSet.getInt("de.sort_pos");
                mbIsPrepayment = resultSet.getBoolean("de.b_pre_pay");
                mbIsDiscountRetailChain = resultSet.getBoolean("de.b_disc_retail_chain");
                mbIsTaxesAutomaticApplying = resultSet.getBoolean("de.b_tax_aut");
                mbIsPriceVariable = resultSet.getBoolean("de.b_prc_var");
                mbIsPriceConfirm = resultSet.getBoolean("de.b_prc_cnf");
                mbIsSalesFreightRequired = resultSet.getBoolean("de.b_sales_freight_req");
                mbIsSalesFreightConfirm = resultSet.getBoolean("de.b_sales_freight_cnf");
                mbIsSalesFreightAdd = resultSet.getBoolean("de.b_sales_freight_add");
                mbIsInventoriable = resultSet.getBoolean("de.b_inv");
                mbIsDeleted = resultSet.getBoolean("de.b_del");
                mnFkItemId = resultSet.getInt("de.fid_item");
                mnFkUnitId = resultSet.getInt("de.fid_unit");
                mnFkOriginalUnitId = resultSet.getInt("de.fid_orig_unit");
                mnFkTaxRegionId = resultSet.getInt("de.fid_tax_reg");
                mnFkThirdTaxCausingId_n = resultSet.getInt("de.fid_third_tax_n");
                mnFkDpsAdjustmentTypeId = resultSet.getInt("de.fid_tp_dps_adj");
                mnFkDpsAdjustmentSubtypeId = resultSet.getInt("de.fid_stp_dps_adj");
                mnFkDpsEntryTypeId = resultSet.getInt("de.fid_tp_dps_ety");
                mnFkVehicleTypeId_n = resultSet.getInt("de.fid_tp_veh_n");
                mnFkCashCompanyBranchId_n = resultSet.getInt("de.fid_cash_cob_n");
                mnFkCashAccountId_n = resultSet.getInt("de.fid_cash_acc_n");
                msFkCostCenterId_n = resultSet.getString("de.fid_cc_n");
                if (resultSet.wasNull()) {
                    msFkCostCenterId_n = "";
                }
                mnFkItemRefId_n = resultSet.getInt("de.fid_item_ref_n");
                mnFkUserNewId = resultSet.getInt("de.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("de.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("de.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("de.ts_new");
                mtUserEditTs = resultSet.getTimestamp("de.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("de.ts_del");

                mnAuxFkItemOld = resultSet.getInt("de.fid_item");
                mdAuxQuantityOld = resultSet.getDouble("de.qty");
                mdAuxOriginalQuantityOld = resultSet.getDouble("de.orig_qty");
                mnAuxFkUnitOld = resultSet.getInt("de.fid_unit");
                mnAuxFkOriginalUnitOld = resultSet.getInt("de.fid_orig_unit");
                
                nCveProdSerItem_n = resultSet.getInt("i.fid_cfd_prod_serv_n");
                nCveProdSerIgen = resultSet.getInt("igen.fid_cfd_prod_serv");
                
                mnDbmsFkItemGenericId = resultSet.getInt("i.fid_igen");
                mbDbmsItemGenDataShipDomesticReq = resultSet.getBoolean("igen.b_ship_dom");
                mbDbmsItemGenDataShipInternationalReq = resultSet.getBoolean("igen.b_ship_int");
                mbDbmsItemGenDataShipQualityReq = resultSet.getBoolean("igen.b_ship_qlt");
                msDbmsItem = resultSet.getString("i.item");
                msDbmsUnitSymbol = resultSet.getString("u.symbol");
                msDbmsOriginalUnitSymbol = resultSet.getString("ou.symbol");
                mdDbmsOriginalUnitBaseEquivalence = resultSet.getDouble("ou.unit_base_equiv");
                msDbmsTaxRegion = resultSet.getString("tr.tax_reg");
                msDbmsDpsAdjustmentType = resultSet.getString("tda.stp_dps_adj");
                msDbmsDpsEntryType = resultSet.getString("tde.tp_dps_ety");
                msDbmsCostCenterCode = resultSet.getString("cc.code");
                if (resultSet.wasNull()) {
                    msDbmsCostCenterCode = "";
                }
                msDbmsCostCenter_n = resultSet.getString("cc.cc");
                if (resultSet.wasNull()) {
                    msDbmsCostCenter_n = "";
                }
                msDbmsItemRef_n = resultSet.getString("ir.item");
                if (resultSet.wasNull()) {
                    msDbmsItemRef_n = "";
                }
                mnDbmsAddBachocoNumeroPosicion  = resultSet.getInt("ade.bac_num_pos");
                if (resultSet.wasNull()) {
                    mnDbmsAddBachocoNumeroPosicion = 0;
                }
                msDbmsAddBachocoCentro = resultSet.getString("ade.bac_cen");
                if (resultSet.wasNull()) {
                    msDbmsAddBachocoCentro = "";
                }
                mnDbmsAddLorealEntryNumber = resultSet.getInt("ade.lor_num_ety");
                if (resultSet.wasNull()) {
                    mnDbmsAddLorealEntryNumber = 0;
                }
                msDbmsAddSorianaCodigo = resultSet.getString("ade.sor_cod");
                if (resultSet.wasNull()) {
                    msDbmsAddSorianaCodigo = "";
                }
                msDbmsAddElektraOrder = resultSet.getString("ade.ele_ord");
                if (resultSet.wasNull()) {
                    msDbmsAddElektraOrder = "";
                }
                msDbmsAddElektraBarcode = resultSet.getString("ade.ele_barc");
                if (resultSet.wasNull()) {
                    msDbmsAddElektraBarcode = "";
                }
                mnDbmsAddElektraCages = resultSet.getInt("ade.ele_cag");
                if (resultSet.wasNull()) {
                    mnDbmsAddElektraCages = 0;
                }
                mdDbmsAddElektraCagePriceUnitary = resultSet.getDouble("ade.ele_cag_price_u");
                if (resultSet.wasNull()) {
                    mdDbmsAddElektraCagePriceUnitary = 0;
                }
                mnDbmsAddElektraParts = resultSet.getInt("ade.ele_par");
                if (resultSet.wasNull()) {
                    mnDbmsAddElektraParts = 0;
                }
                mdDbmsAddElektraPartPriceUnitary = resultSet.getDouble("ade.ele_par_price_u");
                if (resultSet.wasNull()) {
                    mdDbmsAddElektraPartPriceUnitary = 0;
                }
                msDbmsAddJsonData = resultSet.getString("ade.json_data");
                if (resultSet.wasNull()) {
                    msDbmsAddJsonData = "";
                }

                msDbmsUnidadClave = resultSet.getString("cu.code");
                msDbmsOriginalUnidadClave = resultSet.getString("ocu.code");
                mnDbmsItemBrandId = resultSet.getInt("i.fid_brd");
                msDbmsItemBrand = resultSet.getString("brd.brd");
                msDbmsTariff = resultSet.getString("i.tariff");
                msDbmsCustomsUnit = resultSet.getString("ou.unit_custs");
                msDbmsCustomsUnitSymbol = resultSet.getString("ou.symbol_custs");
                msDbmsItemCustomsUnitSymbol = resultSet.getString("i.custs_unit");
                mdDbmsItemCustomsUnitEquivalence = resultSet.getDouble("i.custs_equiv");
                
                statementAux = statement.getConnection().createStatement();
                
                // Read code SAT item:
                
                msDbmsItemClaveProdServ = SItemUtilities.getClaveProdServ(statementAux, nCveProdSerItem_n != 0 ? nCveProdSerItem_n : nCveProdSerIgen);

                // Read aswell entry notes:

                sql = "SELECT id_nts FROM trn_dps_ety_nts " +
                        "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + " ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataDpsEntryNotes notes = new SDataDpsEntryNotes();
                    if (notes.read(new int[] { mnPkYearId, mnPkDocId, mnPkEntryId, resultSet.getInt("id_nts") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsEntryNotes.add(notes);
                    }
                }

                // Read aswell entry taxes:

                sql = "SELECT id_tax_bas, id_tax FROM trn_dps_ety_tax " +
                        "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + " ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataDpsEntryTax tax = new SDataDpsEntryTax();
                    if (tax.read(new int[] { mnPkYearId, mnPkDocId, mnPkEntryId, resultSet.getInt("id_tax_bas"), resultSet.getInt("id_tax") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsEntryTaxes.add(tax);
                    }
                }

                // Read aswell entry commissions:

                sql = "SELECT id_comms FROM trn_dps_ety_comms " +
                        "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + " ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataDpsEntryCommissions commissions = new SDataDpsEntryCommissions();
                    if (commissions.read(new int[] { mnPkYearId, mnPkDocId, mnPkEntryId, resultSet.getInt("id_comms") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsEntryCommissions.add(commissions);
                    }
                }
                
                // Read aswell entry prices:

                sql = "SELECT id_prc FROM trn_dps_ety_prc " +
                        "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + " AND b_del = 0 ORDER BY con_prc_year, con_prc_mon";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataDpsEntryPrice prices = new SDataDpsEntryPrice();
                    if (prices.read(new int[] { mnPkYearId, mnPkDocId, mnPkEntryId, resultSet.getInt("id_prc") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsEntryPrices.add(prices);
                    }
                }
                
                // Read aswell entry item composition
                
                sql = "SELECT id_item FROM trn_dps_ety_item_comp " +
                        "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId;
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    moDbmsDpsEntryItemComposition = new SDataDpsEntryItemComposition();
                    moDbmsDpsEntryItemComposition.read(new int[] { mnPkYearId, mnPkDocId, mnPkEntryId, resultSet.getInt(1) }, statementAux);
                }

                if (mbFlagReadLinksAswell) {
                    // Read aswell DPS links as a source:

                    sql = "SELECT id_des_year, id_des_doc, id_des_ety, qty FROM trn_dps_dps_supply " +
                            "WHERE id_src_year = " + mnPkYearId + " AND id_src_doc = " + mnPkDocId + " AND id_src_ety = " + mnPkEntryId + " ";
                    resultSet = statement.executeQuery(sql);
                    while (resultSet.next()) {
                        SDataDpsDpsLink link = new SDataDpsDpsLink();
                        if (link.read(new Object[] { mnPkYearId, mnPkDocId, mnPkEntryId, resultSet.getInt("id_des_year"), resultSet.getInt("id_des_doc"), resultSet.getInt("id_des_ety"), resultSet.getDouble("qty") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                        else {
                            mvDbmsDpsLinksAsSource.add(link);
                        }
                    }

                    // Read aswell DPS links as a destiny:

                    sql = "SELECT id_src_year, id_src_doc, id_src_ety, qty FROM trn_dps_dps_supply " +
                            "WHERE id_des_year = " + mnPkYearId + " AND id_des_doc = " + mnPkDocId + " AND id_des_ety = " + mnPkEntryId + " ";
                    resultSet = statement.executeQuery(sql);
                    while (resultSet.next()) {
                        SDataDpsDpsLink link = new SDataDpsDpsLink();
                        if (link.read(new Object[] { resultSet.getInt("id_src_year"), resultSet.getInt("id_src_doc"), resultSet.getInt("id_src_ety"), mnPkYearId, mnPkDocId, mnPkEntryId, resultSet.getDouble("qty") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                        else {
                            mvDbmsDpsLinksAsDestiny.add(link);
                        }
                    }
                    
                    //Read aswell DPS Entry price if has:
                    if (mnContractPriceYear != SLibConstants.UNDEFINED && mnContractPriceMonth != SLibConstants.UNDEFINED && !mvDbmsDpsLinksAsDestiny.isEmpty()) {
                        sql = "SELECT ds.id_src_year, ds.id_src_doc, ds.id_src_ety, dep.id_prc, de.con_prc_year, de.con_prc_mon "
                                + "FROM trn_dps_dps_supply ds "
                                + "INNER JOIN trn_dps_ety de ON ds.id_des_year = de.id_year AND ds.id_des_doc = de.id_doc AND ds.id_des_ety = de.id_ety  AND de.b_del = 0 "
                                + "INNER JOIN trn_dps_ety_prc dep ON ds.id_src_year = dep.id_year AND ds.id_src_doc = dep.id_doc AND ds.id_src_ety = dep.id_ety and de.con_prc_year = dep.con_prc_year AND de.con_prc_mon = dep.con_prc_mon "
                                + "WHERE id_des_year = " + mnPkYearId + " AND id_des_doc = " + mnPkDocId + " AND id_des_ety = " + mnPkEntryId + " ;";
                        resultSet = statement.executeQuery(sql);
                        while (resultSet.next()) {
                            manAuxPkDpsEntryPrice = new int[4]; 
                            
                            manAuxPkDpsEntryPrice[0] = resultSet.getInt("ds.id_src_year");
                            manAuxPkDpsEntryPrice[1] = resultSet.getInt("ds.id_src_doc");
                            manAuxPkDpsEntryPrice[2] = resultSet.getInt("ds.id_src_ety");
                            manAuxPkDpsEntryPrice[3] = resultSet.getInt("dep.id_prc");                        
                        }
                    }

                    // Read aswell DPS adjustments as a document:

                    sql = "SELECT id_adj_year, id_adj_doc, id_adj_ety FROM trn_dps_dps_adj " +
                            "WHERE id_dps_year = " + mnPkYearId + " AND id_dps_doc = " + mnPkDocId + " AND id_dps_ety = " + mnPkEntryId + " ";
                    resultSet = statement.executeQuery(sql);
                    while (resultSet.next()) {
                        SDataDpsDpsAdjustment adjustment = new SDataDpsDpsAdjustment();
                        if (adjustment.read(new int[] { mnPkYearId, mnPkDocId, mnPkEntryId, resultSet.getInt("id_adj_year"), resultSet.getInt("id_adj_doc"), resultSet.getInt("id_adj_ety") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                        else {
                            mvDbmsDpsAdjustmentsAsDps.add(adjustment);
                        }
                    }

                    // Read aswell DPS adjustments as a document adjustment:

                    sql = "SELECT id_dps_year, id_dps_doc, id_dps_ety FROM trn_dps_dps_adj " +
                            "WHERE id_adj_year = " + mnPkYearId + " AND id_adj_doc = " + mnPkDocId + " AND id_adj_ety = " + mnPkEntryId + " ";
                    resultSet = statement.executeQuery(sql);
                    while (resultSet.next()) {
                        SDataDpsDpsAdjustment adjustment = new SDataDpsDpsAdjustment();
                        if (adjustment.read(new int[] { resultSet.getInt("id_dps_year"), resultSet.getInt("id_dps_doc"), resultSet.getInt("id_dps_ety"), mnPkYearId, mnPkDocId, mnPkEntryId }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                        else {
                            mnAuxPkDpsYearId = adjustment.getPkDpsYearId();
                            mnAuxPkDpsDocId = adjustment.getPkDpsDocId();

                            mvDbmsDpsAdjustmentsAsAdjustment.add(adjustment);
                        }
                    }

                    // Read aswell document entry complement, if any:

                    sql = "SELECT COUNT(*) FROM trn_dps_cfd_ety " +
                            "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + " ";
                    resultSet = statement.executeQuery(sql);

                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        moDbmsDpsCfdEntry = new SDataDpsCfdEntry();
                        if (moDbmsDpsCfdEntry.read(new int[] { mnPkYearId, mnPkDocId, mnPkEntryId }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                    }
                    
                    // Read configured quality analysis of Dps Ety
                    
                    sql = "SELECT "
                            + " id_ety_analysis "
                            + "FROM "
                            + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_ETY_ANALYSIS) + " "
                            + "WHERE "
                            + "fid_dps_year_n = " + mnPkYearId + " AND "
                            + "fid_dps_doc_n = " + mnPkDocId + " AND "
                            + "fid_dps_ety_n = " + mnPkEntryId + " AND "
                            + "fid_item_id = " + mnFkItemId + " "
                            + "ORDER BY sort_pos ASC, id_ety_analysis ASC;";
        
                    mlDbmsDpsEntryAnalysis = new ArrayList<>();
                    resultSet = statement.executeQuery(sql);
                    while (resultSet.next()) {
                        SDataDpsEntryAnalysis oEtyAnalysis = new SDataDpsEntryAnalysis();
                        oEtyAnalysis.read(new int[] { resultSet.getInt("id_ety_analysis") }, statement.getConnection().createStatement());
                        mlDbmsDpsEntryAnalysis.add(oEtyAnalysis);
                    }
                    
                    // Read Quantity changes of Dps Ety
                    
                    sql = "SELECT " 
                            + "id_chg " 
                            + "FROM " 
                            + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_ETY_QTY_CHG) + " "
                            + "WHERE "
                            + "id_year = " + mnPkYearId + " AND "
                            + "id_doc = " + mnPkDocId + " AND "
                            + "id_ety = " + mnPkEntryId + " AND "
                            + "NOT b_del "
                            + "ORDER BY id_year, id_doc, id_ety, id_chg;";
                    
                    mlDbmsDpsEntryQuantityChange = new ArrayList<>();
                    resultSet = statement.executeQuery(sql);
                    while (resultSet.next()) {
                        SDataDpsEntryQuantityChange o = new SDataDpsEntryQuantityChange();
                        o.read(new int[] { mnPkYearId, mnPkDocId, mnPkEntryId, resultSet.getInt(1) }, statement.getConnection().createStatement());
                        mlDbmsDpsEntryQuantityChange.add(o);
                    }
                    
                    // Read Dps Ety links to Material Requests
                    sql = "SELECT "
                            + " id_dps_mat_req "
                            + "FROM "
                            + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_MAT_REQ) + " "
                            + "WHERE "
                            + "fid_dps_year = " + mnPkYearId + " AND "
                            + "fid_dps_doc = " + mnPkDocId + " AND "
                            + "fid_dps_ety = " + mnPkEntryId + " "
                            + "ORDER BY fid_mat_req ASC, fid_mat_req_ety ASC, id_dps_mat_req ASC "
                            + "LIMIT 1;";
        
                    moDbmsDpsEntryMatRequest = null;
                    ResultSet resultLinks = statement.executeQuery(sql);
                    if (resultLinks.next()) {
                        SDataDpsMaterialRequest oEtyMatReqLink = new SDataDpsMaterialRequest();
                        oEtyMatReqLink.read(new int[] { resultLinks.getInt("id_dps_mat_req") }, statement.getConnection().createStatement());
                        moDbmsDpsEntryMatRequest = oEtyMatReqLink;
                    }
                    
                    mbFlagOpenedByMatRequestImport = true;
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int nParam = 1;
        String aSql[] = null;
        Statement statement = null;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            callableStatement = connection.prepareCall(
                    "{ CALL trn_dps_ety_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + 
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkDocId);
            callableStatement.setInt(nParam++, mnPkEntryId);
            callableStatement.setString(nParam++, msConceptKey);
            callableStatement.setString(nParam++, msConcept);
            callableStatement.setString(nParam++, msReference);
            callableStatement.setDouble(nParam++, mdQuantity);
            callableStatement.setBoolean(nParam++, mbIsDiscountDocApplying);
            callableStatement.setBoolean(nParam++, mbIsDiscountUnitaryPercentage);
            callableStatement.setBoolean(nParam++, mbIsDiscountUnitaryPercentageSystem);
            callableStatement.setBoolean(nParam++, mbIsDiscountEntryPercentage);
            callableStatement.setDouble(nParam++, mdDiscountUnitaryPercentage);
            callableStatement.setDouble(nParam++, mdDiscountUnitaryPercentageSystem);
            callableStatement.setDouble(nParam++, mdDiscountEntryPercentage);
            callableStatement.setDouble(nParam++, mdPriceUnitary);
            callableStatement.setDouble(nParam++, mdPriceUnitarySystem);
            callableStatement.setDouble(nParam++, mdDiscountUnitary);
            callableStatement.setDouble(nParam++, mdDiscountUnitarySystem);
            callableStatement.setDouble(nParam++, mdDiscountEntry);
            callableStatement.setDouble(nParam++, mdSubtotalProvisional_r);
            callableStatement.setDouble(nParam++, mdDiscountDoc);
            callableStatement.setDouble(nParam++, mdSubtotal_r);
            callableStatement.setDouble(nParam++, mdTaxCharged_r);
            callableStatement.setDouble(nParam++, mdTaxRetained_r);
            callableStatement.setDouble(nParam++, mdTotal_r);
            callableStatement.setDouble(nParam++, mdPriceUnitaryReal_r);
            callableStatement.setDouble(nParam++, mdCommissions_r);
            callableStatement.setDouble(nParam++, mdPriceUnitaryCy);
            callableStatement.setDouble(nParam++, mdPriceUnitarySystemCy);
            callableStatement.setDouble(nParam++, mdDiscountUnitaryCy);
            callableStatement.setDouble(nParam++, mdDiscountUnitarySystemCy);
            callableStatement.setDouble(nParam++, mdDiscountEntryCy);
            callableStatement.setDouble(nParam++, mdSubtotalProvisionalCy_r);
            callableStatement.setDouble(nParam++, mdDiscountDocCy);
            callableStatement.setDouble(nParam++, mdSubtotalCy_r);
            callableStatement.setDouble(nParam++, mdTaxChargedCy_r);
            callableStatement.setDouble(nParam++, mdTaxRetainedCy_r);
            callableStatement.setDouble(nParam++, mdTotalCy_r);
            callableStatement.setDouble(nParam++, mdPriceUnitaryRealCy_r);
            callableStatement.setDouble(nParam++, mdCommissionsCy_r);
            callableStatement.setDouble(nParam++, mdOriginalQuantity);
            callableStatement.setDouble(nParam++, mdOriginalPriceUnitaryCy);
            callableStatement.setDouble(nParam++, mdOriginalPriceUnitarySystemCy);
            callableStatement.setDouble(nParam++, mdOriginalDiscountUnitaryCy);
            callableStatement.setDouble(nParam++, mdOriginalDiscountUnitarySystemCy);
            callableStatement.setDouble(nParam++, mdSalesPriceUnitaryCy);
            callableStatement.setDouble(nParam++, mdSalesFreightUnitaryCy);
            callableStatement.setDouble(nParam++, mdLength);
            callableStatement.setDouble(nParam++, mdSurface);
            callableStatement.setDouble(nParam++, mdVolume);
            callableStatement.setDouble(nParam++, mdMass);
            callableStatement.setDouble(nParam++, mdWeightPackagingExtra);
            callableStatement.setDouble(nParam++, mdWeightGross);
            callableStatement.setDouble(nParam++, mdWeightDelivery);
            callableStatement.setDouble(nParam++, mdSurplusPercentage);
            callableStatement.setDouble(nParam++, mdContractBase);
            callableStatement.setDouble(nParam++, mdContractFuture);
            callableStatement.setDouble(nParam++, mdContractFactor);
            callableStatement.setInt(nParam++, mnContractPriceYear);
            callableStatement.setInt(nParam++, mnContractPriceMonth);
            callableStatement.setString(nParam++, msSealQuality);
            callableStatement.setString(nParam++, msSealSecurity);
            callableStatement.setString(nParam++, msDriver);
            callableStatement.setString(nParam++, msPlate);
            callableStatement.setString(nParam++, msTicket);
            callableStatement.setString(nParam++, msContainerTank);
            callableStatement.setString(nParam++, msVgm);
            callableStatement.setInt(nParam++, mnOperationsType);
            callableStatement.setInt(nParam++, mnUserId);
            callableStatement.setInt(nParam++, mnSortingPosition);
            callableStatement.setBoolean(nParam++, mbIsPrepayment);
            callableStatement.setBoolean(nParam++, mbIsDiscountRetailChain);
            callableStatement.setBoolean(nParam++, mbIsTaxesAutomaticApplying);
            callableStatement.setBoolean(nParam++, mbIsPriceVariable);
            callableStatement.setBoolean(nParam++, mbIsPriceConfirm);
            callableStatement.setBoolean(nParam++, mbIsSalesFreightRequired);
            callableStatement.setBoolean(nParam++, mbIsSalesFreightConfirm);
            callableStatement.setBoolean(nParam++, mbIsSalesFreightAdd);
            callableStatement.setBoolean(nParam++, mbIsInventoriable);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkItemId);
            callableStatement.setInt(nParam++, mnFkUnitId);
            callableStatement.setInt(nParam++, mnFkOriginalUnitId);
            callableStatement.setInt(nParam++, mnFkTaxRegionId);
            if (mnFkThirdTaxCausingId_n > SLibConsts.UNDEFINED) callableStatement.setInt(nParam++, mnFkThirdTaxCausingId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mnFkDpsAdjustmentTypeId);
            callableStatement.setInt(nParam++, mnFkDpsAdjustmentSubtypeId);
            callableStatement.setInt(nParam++, mnFkDpsEntryTypeId);
            if (mnFkVehicleTypeId_n > SLibConsts.UNDEFINED) callableStatement.setInt(nParam++, mnFkVehicleTypeId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkCashCompanyBranchId_n > SLibConsts.UNDEFINED) callableStatement.setInt(nParam++, mnFkCashCompanyBranchId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkCashAccountId_n > SLibConsts.UNDEFINED) callableStatement.setInt(nParam++, mnFkCashAccountId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (!msFkCostCenterId_n.isEmpty()) callableStatement.setString(nParam++, msFkCostCenterId_n); else callableStatement.setNull(nParam++, java.sql.Types.CHAR);
            if (mnFkItemRefId_n > SLibConsts.UNDEFINED) callableStatement.setInt(nParam++, mnFkItemRefId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkEntryId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                // Save aswell entry notes:

                for (SDataDpsEntryNotes notes : mvDbmsEntryNotes) {
                    if (notes.getIsRegistryNew() || notes.getIsRegistryEdited()) {
                        notes.setPkYearId(mnPkYearId);
                        notes.setPkDocId(mnPkDocId);
                        notes.setPkEntryId(mnPkEntryId);

                        if (notes.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // Clear entry dependent registries:

                statement = connection.createStatement();

                aSql = new String[] {
                    "DELETE FROM trn_dps_ety_tax WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + " ",
                    "DELETE FROM trn_dps_ety_comms WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + " ",
                    "DELETE FROM trn_dps_dps_supply WHERE id_src_year = " + mnPkYearId + " AND id_src_doc = " + mnPkDocId + " AND id_src_ety = " + mnPkEntryId + " ",
                    "DELETE FROM trn_dps_dps_supply WHERE id_des_year = " + mnPkYearId + " AND id_des_doc = " + mnPkDocId + " AND id_des_ety = " + mnPkEntryId + " ",
                    "DELETE FROM trn_dps_dps_adj WHERE id_dps_year = " + mnPkYearId + " AND id_dps_doc = " + mnPkDocId + " AND id_dps_ety = " + mnPkEntryId + " ",
                    "DELETE FROM trn_dps_dps_adj WHERE id_adj_year = " + mnPkYearId + " AND id_adj_doc = " + mnPkDocId + " AND id_adj_ety = " + mnPkEntryId + " " };

                for (String sql : aSql) {
                    statement.execute(sql);
                }

                // Save aswell entry taxes:

                for (SDataDpsEntryTax tax : mvDbmsEntryTaxes) {
                    tax.setPkYearId(mnPkYearId);
                    tax.setPkDocId(mnPkDocId);
                    tax.setPkEntryId(mnPkEntryId);

                    if (tax.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell entry commissions:

                for (SDataDpsEntryCommissions commissions : mvDbmsEntryCommissions) {
                    commissions.setPkYearId(mnPkYearId);
                    commissions.setPkDocId(mnPkDocId);
                    commissions.setPkEntryId(mnPkEntryId);

                    if (commissions.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell entry prices:

                for (SDataDpsEntryPrice prices : mvDbmsEntryPrices) {
                    if (!mbIsPriceVariable) {
                        prices.setIsDeleted(true);
                    }
                    prices.setPkYearId(mnPkYearId);
                    prices.setPkDocId(mnPkDocId);
                    prices.setPkEntryId(mnPkEntryId);

                    if (prices.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }
                
                if (mbIsDeleted) {
                    mvDbmsDpsLinksAsSource.clear();
                    mvDbmsDpsLinksAsDestiny.clear();
                    mvDbmsDpsAdjustmentsAsDps.clear();
                    mvDbmsDpsAdjustmentsAsAdjustment.clear();
                    mlDbmsDpsEntryAnalysis.clear();
                    mlDbmsDpsEntryQuantityChange.clear();
                }
                else {
                    // Save aswell DPS entry links as source:

                    for (SDataDpsDpsLink link : mvDbmsDpsLinksAsSource) {
                        link.setPkSourceYearId(mnPkYearId);
                        link.setPkSourceDocId(mnPkDocId);
                        link.setPkSourceEntryId(mnPkEntryId);

                        if (link.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }

                    // Save aswell DPS entry links as destiny:

                    for (SDataDpsDpsLink link : mvDbmsDpsLinksAsDestiny) {
                        link.setPkDestinyYearId(mnPkYearId);
                        link.setPkDestinyDocId(mnPkDocId);
                        link.setPkDestinyEntryId(mnPkEntryId);
                                               
                        if (link.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                        
                        // Redirect inventory movements references
                        
                        if (link.getDbmsIsSourceOrderSupplied()) {
                            aSql = new String[] { 
                                "UPDATE trn_diog SET fid_dps_year_n = " + mnPkYearId + ", fid_dps_doc_n = " + mnPkDocId + " " +
                                "WHERE fid_dps_year_n = " + link.getPkSourceYearId() + " AND fid_dps_doc_n = " + link.getPkSourceDocId() + " ",
                                "UPDATE trn_diog_ety SET fid_dps_year_n = " + mnPkYearId + ", fid_dps_doc_n = " + mnPkDocId + ", fid_dps_ety_n = " + mnPkEntryId + " " +
                                "WHERE fid_dps_year_n = " + link.getPkSourceYearId() + " AND fid_dps_doc_n = " + link.getPkSourceDocId() + " " +
                                "AND fid_dps_ety_n = " + link.getPkSourceEntryId() + " ",
                                "UPDATE trn_stk SET fid_dps_year_n = " + mnPkYearId + ", fid_dps_doc_n = " + mnPkDocId + ", fid_dps_ety_n = " + mnPkEntryId + " " +
                                "WHERE fid_dps_year_n = " + link.getPkSourceYearId() + " AND fid_dps_doc_n = " + link.getPkSourceDocId() + " " +
                                "AND fid_dps_ety_n = " + link.getPkSourceEntryId() + " "
                            };

                            for (String sql : aSql) {
                               statement.execute(sql);
                            }
                        }
                    }

                    // Save aswell document adjustment links as document:

                    for (SDataDpsDpsAdjustment adjustment : mvDbmsDpsAdjustmentsAsDps) {
                        adjustment.setPkDpsYearId(mnPkYearId);
                        adjustment.setPkDpsDocId(mnPkDocId);
                        adjustment.setPkDpsEntryId(mnPkEntryId);

                        if (adjustment.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }

                    // Save aswell document adjustment links as document adjustment:

                    for (SDataDpsDpsAdjustment adjustment : mvDbmsDpsAdjustmentsAsAdjustment) {
                        adjustment.setPkDpsAdjustmentYearId(mnPkYearId);
                        adjustment.setPkDpsAdjustmentDocId(mnPkDocId);
                        adjustment.setPkDpsAdjustmentEntryId(mnPkEntryId);

                        if (adjustment.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                    
                    // Save aswell document entry complement, if any:

                    if (moDbmsDpsCfdEntry != null) {
                        String sql = "DELETE FROM trn_dps_cfd_ety " +
                                "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + ";";
                        statement.execute(sql);
                        
                        moDbmsDpsCfdEntry.setPkYearId(mnPkYearId);
                        moDbmsDpsCfdEntry.setPkDocId(mnPkDocId);
                        moDbmsDpsCfdEntry.setPkEntryId(mnPkEntryId);
                        moDbmsDpsCfdEntry.save(connection);
                    }
                    else {
                        String sql = "DELETE FROM trn_dps_cfd_ety " +
                                "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + ";";
                        statement.execute(sql);
                        moDbmsDpsCfdEntry = new SDataDpsCfdEntry();
                        moDbmsDpsCfdEntry.setPkYearId(mnPkYearId);
                        moDbmsDpsCfdEntry.setPkDocId(mnPkDocId);
                        moDbmsDpsCfdEntry.setPkEntryId(mnPkEntryId);
                        if (!mvDbmsEntryTaxes.isEmpty()){
                            moDbmsDpsCfdEntry.setTaxObject(DCfdi40Catalogs.ClaveObjetoImpSí);
                        }
                        else {
                            moDbmsDpsCfdEntry.setTaxObject(DCfdi40Catalogs.ClaveObjetoImpNo);
                        }
                        moDbmsDpsCfdEntry.save(connection);
                    }
                    
                    // Save quality configurations
                    if (mlDbmsDpsEntryAnalysis.size() > 0) {
                        String sql = "DELETE FROM trn_dps_ety_analysis " +
                                "WHERE fid_dps_year_n = " + mnPkYearId + " AND fid_dps_doc_n = " + mnPkDocId + " AND fid_dps_ety_n = " + mnPkEntryId + ";";
                        statement.execute(sql);
                        
                        for (SDataDpsEntryAnalysis oEtyAnalysis : mlDbmsDpsEntryAnalysis) {
                            if (oEtyAnalysis.getFkDpsYearId_n() == SLibConsts.UNDEFINED) {
                                oEtyAnalysis.setFkDpsYearId_n(mnPkYearId);
                                oEtyAnalysis.setFkDpsDocId_n(mnPkDocId);
                                oEtyAnalysis.setFkDpsEtyId_n(mnPkEntryId);    
                            }
                            
                            oEtyAnalysis.setIsRegistryNew(true);
                            oEtyAnalysis.setPkEntryAnalysisId(0);
                            
                            oEtyAnalysis.save(connection);
                        }
                    }
                    
                    if (mlDbmsDpsEntryQuantityChange.size() > 0) {
                        if (mnAuxFkItemOld != mnFkItemId) {
                            String sql = "UPDATE trn_dps_ety_qty_chg " +
                                "SET b_del = 1 WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + ";";
                            statement.execute(sql);
                            mlDbmsDpsEntryQuantityChange.clear();
                        }
                        for (SDataDpsEntryQuantityChange chg : mlDbmsDpsEntryQuantityChange) {
                            if (chg.getIsRegistryNew()) {
                                chg.setPkYearId(mnPkYearId);
                                chg.setPkDocId(mnPkDocId);
                                chg.setPkEntryId(mnPkEntryId);
                                chg.setFkUserNewId(mnFkUserEditId);
                                chg.save(connection);
                                
                                if (chg.getLastDbActionResult() == SLibConstants.DB_ACTION_SAVE_ERROR) {
                                    throw new Exception("Ocurrió un error al guardar el cambio de la cantidad, contacte a soporte técnico");
                                }
                            }
                        }
                    }
                    
                    // Save dps mat reqs links
                    if (moDbmsDpsEntryMatRequest != null) {
                        String sql = "DELETE FROM trn_dps_mat_req " +
                                "WHERE fid_dps_year = " + mnPkYearId + " "
                                + "AND fid_dps_doc = " + mnPkDocId + " "
                                + "AND fid_dps_ety = " + mnPkEntryId + ";";
                        statement.execute(sql);
                        moDbmsDpsEntryMatRequest.setPkDpsMatReqId(0);
                        moDbmsDpsEntryMatRequest.setFkDpsYearId(mnPkYearId);
                        moDbmsDpsEntryMatRequest.setFkDpsDocId(mnPkDocId);
                        moDbmsDpsEntryMatRequest.setFkDpsEntryId(mnPkEntryId);
                        moDbmsDpsEntryMatRequest.setIsRegistryNew(true);
                        moDbmsDpsEntryMatRequest.save(connection);

                        if (moDbmsDpsEntryMatRequest.getLastDbActionResult() == SLibConstants.DB_ACTION_SAVE_ERROR) {
                            throw new Exception("Ocurrió un error al guardar el vínculo con la requisición, contacte a soporte técnico");
                        }
                    }
                    
                    // Save dps entry item composition
                    
                    if (moDbmsDpsEntryItemComposition != null) {
                        String sql = "DELETE FROM trn_dps_ety_item_comp " + 
                                "WHERE id_year = " + mnPkYearId + " " +
                                "AND id_doc = " + mnPkDocId + " " + 
                                "AND id_ety = " + mnPkEntryId + ";";
                        statement.execute(sql);
                        moDbmsDpsEntryItemComposition.setPkYearId(mnPkYearId);
                        moDbmsDpsEntryItemComposition.setPkDocId(mnPkDocId);
                        moDbmsDpsEntryItemComposition.setPkEntryId(mnPkEntryId);
                        moDbmsDpsEntryItemComposition.setIsRegistryNew(true);
                        moDbmsDpsEntryItemComposition.save(connection);    

                        if (moDbmsDpsEntryItemComposition.getLastDbActionResult() == SLibConstants.DB_ACTION_SAVE_ERROR) {
                            throw new Exception("Ocurrió un error al guardar la composición del ítem, contacte a soporte técnico");
                        }
                    }
                    
                    // Save item price commercial if dps purchase invoice
                    if (mbXtaIsPurInvoiceInventoriable) {
                        SDbPriceCommercialLog log = new SDbPriceCommercialLog();
                        log.setPkItemId(mnFkItemId);
                        log.setPkUnitId(mnFkOriginalUnitId);
                        log.setPrice(mnXtaPriceCommUnitary);
                        log.setFkDpsYear_n(mnPkYearId);
                        log.setFkDpsDoc_n(mnPkDocId);
                        log.setFkDpsEntry_n(mnPkEntryId);
                        log.setSystem(true);
                        log.saveFromDps(connection);
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }

    public void calculateTotal(erp.client.SClientInterface piClient, java.util.Date ptDate, int pnIdyEmisorId, int pnIdyReceptorId,
            boolean pbIsDiscDocPercentage, double pdDiscDocPercentage, double pdExchangeRate) {

        int nDecsQty = 0;
        int nDecsVal = 0;
        int nDecsValUnit = 0;
        double dFactQty = 0d;
        double dFactValUnit = 0d;
        double dSubtotalCy = 0d;

        /* Algorithm:
         * 1. Define number of decimals and factors for converting quantities and values.
         * 2. Calculate DPS entry's value in original currency.
         * 3. Calculate DPS entry's value in local currency.
         * 4. Calculate aswell link and adjustment registries.
         */

        /*
         * 1. Define number of decimals and factors for converting quantities and values:
         */

        nDecsQty = piClient.getSessionXXX().getParamsErp().getDecimalsQuantity();
        nDecsVal = piClient.getSessionXXX().getParamsErp().getDecimalsValue();
        nDecsValUnit = piClient.getSessionXXX().getParamsErp().getDecimalsValueUnitary();

        if (!mbAuxPreserveQuantity) {
            // Common case: quantity must be calculated
            
            dFactQty = ((SSessionCustom) piClient.getSession().getSessionCustom()).getUnitsFactorForQuantity(mnFkItemId, mnFkOriginalUnitId, mnFkUnitId);
            dFactValUnit = ((SSessionCustom) piClient.getSession().getSessionCustom()).getUnitsFactorForUnitaryValue(mnFkItemId, mnFkOriginalUnitId, mnFkUnitId);
        }
        else {
            // Seldom case: quantity must be preserved
            
            dFactQty = mdOriginalQuantity == 0 ? 0 : mdQuantity / mdOriginalQuantity;
            dFactValUnit = dFactQty == 0 ? 0 : 1d / dFactQty;
        }

        /*
         * 2. Calculate DPS entry's value in original currency:
         */

        // Quantity, unitary prices and discounts:

        mdQuantity = SLibUtilities.round(mdOriginalQuantity * dFactQty, nDecsQty);
        
        if (mbIsSalesFreightRequired) {
            if (mbIsSalesFreightAdd) {
                mdOriginalPriceUnitaryCy = SLibUtilities.round(mdSalesPriceUnitaryCy + mdSalesFreightUnitaryCy, nDecsValUnit);
            }
            else {
                mdOriginalPriceUnitaryCy = mdSalesPriceUnitaryCy;
            }
        }

        mdPriceUnitaryCy = SLibUtilities.round(mdOriginalPriceUnitaryCy * dFactValUnit, nDecsValUnit);
        mdPriceUnitarySystemCy = SLibUtilities.round(mdOriginalPriceUnitarySystemCy * dFactValUnit, nDecsValUnit);

        if (mbIsDiscountUnitaryPercentage) {
            mdOriginalDiscountUnitaryCy = SLibUtilities.round(mdOriginalPriceUnitaryCy * mdDiscountUnitaryPercentage, nDecsValUnit);
        }
        mdDiscountUnitaryCy = SLibUtilities.round(mdOriginalDiscountUnitaryCy * dFactValUnit, nDecsValUnit);

        if (mbIsDiscountUnitaryPercentageSystem) {
            mdOriginalDiscountUnitarySystemCy = SLibUtilities.round(mdOriginalPriceUnitarySystemCy * mdDiscountUnitaryPercentageSystem, nDecsValUnit);
        }
        mdDiscountUnitarySystemCy = SLibUtilities.round(mdOriginalDiscountUnitarySystemCy * dFactValUnit, nDecsValUnit);

        // Subtotal:

        dSubtotalCy = SLibUtilities.round(mdQuantity * (mdPriceUnitaryCy - mdDiscountUnitaryCy), nDecsVal);

        if (mbIsDiscountEntryPercentage) {
            mdDiscountEntryCy = SLibUtilities.round(dSubtotalCy * mdDiscountEntryPercentage, nDecsVal);
        }

        mdSubtotalProvisionalCy_r = SLibUtilities.round(dSubtotalCy - mdDiscountEntryCy, nDecsVal);

        if (!mbIsDiscountDocApplying) {
            mdDiscountDocCy = 0d;
        }
        else if (pbIsDiscDocPercentage) {
            mdDiscountDocCy = SLibUtilities.round(mdSubtotalProvisionalCy_r * pdDiscDocPercentage, nDecsVal);
        }

        mdSubtotalCy_r = SLibUtilities.round(mdSubtotalProvisionalCy_r - mdDiscountDocCy, nDecsVal);
        mdPriceUnitaryRealCy_r = SLibUtilities.round(mdQuantity == 0d ? 0d : mdSubtotalCy_r / mdQuantity, nDecsValUnit);

        // Taxes:

        if (mbIsTaxesAutomaticApplying) {
            try {
                mvDbmsEntryTaxes.clear();
                mvDbmsEntryTaxes.addAll(((SSessionCustom) piClient.getSession().getSessionCustom()).getDpsEntryTaxes(
                        mnFkItemId, mbIsPrepayment, mnFkTaxRegionId, pnIdyEmisorId, pnIdyReceptorId, ptDate, mdQuantity, mdSubtotalCy_r, pdExchangeRate));
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }

        mdTaxChargedCy_r = 0;
        mdTaxRetainedCy_r = 0;

        for (SDataDpsEntryTax tax : mvDbmsEntryTaxes) {
            if (tax.getFkTaxTypeId() == SModSysConsts.FINS_TP_TAX_CHARGED) {
                mdTaxChargedCy_r += tax.getTaxCy();
            }
            else {
                mdTaxRetainedCy_r += tax.getTaxCy();
            }
        }

        // Total:

        mdTotalCy_r = SLibUtilities.round(mdSubtotalCy_r + mdTaxChargedCy_r - mdTaxRetainedCy_r, nDecsVal);

        // Commissions:

        mdCommissionsCy_r = 0d;
        mvDbmsEntryCommissions.clear();
        //mvDbmsEntryCommissions.addAll(...);   XXX

        /*
         * 3. Calculate DPS entry's value in local currency:
         */

        // Total:

        mdTotal_r = SLibUtilities.round(mdTotalCy_r * pdExchangeRate, nDecsVal);

        // Taxes:

        mdTaxCharged_r = 0;
        mdTaxRetained_r = 0;

        for (SDataDpsEntryTax tax : mvDbmsEntryTaxes) {
            tax.setTax(SLibUtilities.round(tax.getTaxCy() * pdExchangeRate, nDecsVal));

            if (tax.getFkTaxTypeId() == SModSysConsts.FINS_TP_TAX_CHARGED) {
                mdTaxCharged_r += tax.getTax();
            }
            else {
                mdTaxRetained_r += tax.getTax();
            }
        }

        // Subtotal:

        mdSubtotal_r = SLibUtilities.round(mdTotal_r - mdTaxCharged_r + mdTaxRetained_r, nDecsVal);
        mdPriceUnitaryReal_r = SLibUtilities.round(mdQuantity == 0d ? 0d : mdSubtotal_r / mdQuantity, nDecsValUnit);

        mdDiscountDoc = SLibUtilities.round(mdDiscountDocCy * pdExchangeRate, nDecsVal);
        mdSubtotalProvisional_r = SLibUtilities.round(mdSubtotal_r + mdDiscountDoc, nDecsVal);
        mdDiscountEntry = SLibUtilities.round(mdDiscountEntryCy * pdExchangeRate, nDecsVal);

        // Quantity, unitary prices and discounts:

        mdPriceUnitary = SLibUtilities.round(mdPriceUnitaryCy * pdExchangeRate, nDecsValUnit);
        mdPriceUnitarySystem = SLibUtilities.round(mdPriceUnitarySystemCy * pdExchangeRate, nDecsValUnit);
        mdDiscountUnitary = SLibUtilities.round(mdDiscountUnitaryCy * pdExchangeRate, nDecsValUnit);
        mdDiscountUnitarySystem = SLibUtilities.round(mdDiscountUnitarySystemCy * pdExchangeRate, nDecsValUnit);

        // Commissions:

        mdCommissions_r = SLibUtilities.round(mdCommissionsCy_r * pdExchangeRate, nDecsVal);

        /*
         * 4. Calculate aswell link and adjustment registries:
         */

        for (SDataDpsDpsLink link : mvDbmsDpsLinksAsSource) {
            link.setQuantity(SLibUtilities.round(link.getOriginalQuantity() * dFactQty, nDecsQty));
        }

        for (SDataDpsDpsLink link : mvDbmsDpsLinksAsDestiny) {
            link.setOriginalQuantity(mdOriginalQuantity);
            link.setQuantity(SLibUtilities.round(link.getOriginalQuantity() * dFactQty, nDecsQty));
        }

        for (SDataDpsDpsAdjustment adjustment : mvDbmsDpsAdjustmentsAsDps) {
            adjustment.setQuantity(SLibUtilities.round(adjustment.getOriginalQuantity() * dFactQty, nDecsQty));
            adjustment.setValue(SLibUtilities.round(adjustment.getValueCy() * pdExchangeRate, nDecsVal));
        }

        for (SDataDpsDpsAdjustment adjustment : mvDbmsDpsAdjustmentsAsAdjustment) {
            switch (mnFkDpsAdjustmentTypeId) {
                case SDataConstantsSys.TRNS_TP_DPS_ADJ_RET:
                    adjustment.setOriginalQuantity(mdOriginalQuantity);
                    break;
                case SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC:
                    adjustment.setOriginalQuantity(0);
                    break;
                default:
            }

            adjustment.setQuantity(SLibUtilities.round(adjustment.getOriginalQuantity() * dFactQty, nDecsQty));
            adjustment.setValueCy(mdTotalCy_r);
            adjustment.setValue(SLibUtilities.round(adjustment.getValueCy() * pdExchangeRate, nDecsVal));
        }
    }

    public void resetValue() {
        super.resetRegistry();

        // User defined members:

        mbIsDiscountDocApplying = false;
        mbIsDiscountUnitaryPercentage = false;
        mbIsDiscountUnitaryPercentageSystem = false;
        mbIsDiscountEntryPercentage = false;
        mdDiscountUnitaryPercentage = 0;
        mdDiscountUnitaryPercentageSystem = 0;
        mdDiscountEntryPercentage = 0;

        mdOriginalQuantity = 0;
        mdOriginalPriceUnitaryCy = 0;
        mdOriginalPriceUnitarySystemCy = 0;
        mdOriginalDiscountUnitaryCy = 0;
        mdOriginalDiscountUnitarySystemCy = 0;
        
        mdSalesPriceUnitaryCy = 0;
        mdSalesFreightUnitaryCy = 0;
        mbIsSalesFreightRequired = false;
        mbIsSalesFreightConfirm = false;
        mbIsSalesFreightAdd = false;

        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkOriginalUnitId = 0;
        mnFkTaxRegionId = 0;

        // Class calculated members:

        mdQuantity = 0;

        mdPriceUnitary = 0;
        mdPriceUnitarySystem = 0;
        mdDiscountUnitary = 0;
        mdDiscountUnitarySystem = 0;
        mdDiscountEntry = 0;
        mdSubtotalProvisional_r = 0;
        mdDiscountDoc = 0;
        mdSubtotal_r = 0;
        mdTaxCharged_r = 0;
        mdTaxRetained_r = 0;
        mdTotal_r = 0;
        mdPriceUnitaryReal_r = 0;
        mdCommissions_r = 0;

        mdPriceUnitaryCy = 0;
        mdPriceUnitarySystemCy = 0;
        mdDiscountUnitaryCy = 0;
        mdDiscountUnitarySystemCy = 0;
        mdDiscountEntryCy = 0;
        mdSubtotalProvisionalCy_r = 0;
        mdDiscountDocCy = 0;
        mdSubtotalCy_r = 0;
        mdTaxChargedCy_r = 0;
        mdTaxRetainedCy_r = 0;
        mdTotalCy_r = 0;
        mdPriceUnitaryRealCy_r = 0;
        mdCommissionsCy_r = 0;

        mdLength = 0;
        mdSurface = 0;
        mdVolume = 0;
        mdMass = 0;
        mdWeightPackagingExtra = 0;
        mdWeightGross = 0;
        mdWeightDelivery = 0;

        // Other members:

        mvDbmsEntryTaxes.clear();
        mvDbmsEntryCommissions.clear();
        mvDbmsEntryPrices.clear();

        mnDbmsFkItemGenericId = 0;
        mbDbmsItemGenDataShipDomesticReq = false;
        mbDbmsItemGenDataShipInternationalReq = false;
        mbDbmsItemGenDataShipQualityReq = false;
    }

    public boolean hasDpsLinksAsSource() { return mvDbmsDpsLinksAsSource.size() > 0; }
    public boolean hasDpsLinksAsDestiny() { return mvDbmsDpsLinksAsDestiny.size() > 0; }
    public boolean hasDpsAdjustmentsAsDoc() { return mvDbmsDpsAdjustmentsAsDps.size() > 0; }
    public boolean hasDpsAdjustmentsAsAdjustment() { return mvDbmsDpsAdjustmentsAsAdjustment.size() > 0; }
    public boolean isAccountable() { return !mbIsDeleted && (mnFkDpsEntryTypeId == SDataConstantsSys.TRNS_TP_DPS_ETY_SYS || mnFkDpsEntryTypeId == SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY); }

    @Override
    public erp.mtrn.data.SDataDpsEntry clone() {
        SDataDpsEntry clone = new SDataDpsEntry();

        clone.setIsRegistryNew(mbIsRegistryNew);

        clone.setPkYearId(mnPkYearId);
        clone.setPkDocId(mnPkDocId);
        clone.setPkEntryId(mnPkEntryId);
        clone.setConceptKey(msConceptKey);
        clone.setConcept(msConcept);
        clone.setReference(msReference);
        clone.setQuantity(mdQuantity);
        clone.setIsDiscountDocApplying(mbIsDiscountDocApplying);
        clone.setIsDiscountUnitaryPercentage(mbIsDiscountUnitaryPercentage);
        clone.setIsDiscountUnitaryPercentageSystem(mbIsDiscountUnitaryPercentageSystem);
        clone.setIsDiscountEntryPercentage(mbIsDiscountEntryPercentage);
        clone.setDiscountUnitaryPercentage(mdDiscountUnitaryPercentage);
        clone.setDiscountUnitaryPercentageSystem(mdDiscountUnitaryPercentageSystem);
        clone.setDiscountEntryPercentage(mdDiscountEntryPercentage);
        clone.setPriceUnitary(mdPriceUnitary);
        clone.setPriceUnitarySystem(mdPriceUnitarySystem);
        clone.setDiscountUnitary(mdDiscountUnitary);
        clone.setDiscountUnitarySystem(mdDiscountUnitarySystem);
        clone.setDiscountEntry(mdDiscountEntry);
        clone.setSubtotalProvisional_r(mdSubtotalProvisional_r);
        clone.setDiscountDoc(mdDiscountDoc);
        clone.setSubtotal_r(mdSubtotal_r);
        clone.setTaxCharged_r(mdTaxCharged_r);
        clone.setTaxRetained_r(mdTaxRetained_r);
        clone.setTotal_r(mdTotal_r);
        clone.setPriceUnitaryReal_r(mdPriceUnitaryReal_r);
        clone.setCommissions_r(mdCommissions_r);
        clone.setPriceUnitaryCy(mdPriceUnitaryCy);
        clone.setPriceUnitarySystemCy(mdPriceUnitarySystemCy);
        clone.setDiscountUnitaryCy(mdDiscountUnitaryCy);
        clone.setDiscountUnitarySystemCy(mdDiscountUnitarySystemCy);
        clone.setDiscountEntryCy(mdDiscountEntryCy);
        clone.setSubtotalProvisionalCy_r(mdSubtotalProvisionalCy_r);
        clone.setDiscountDocCy(mdDiscountDocCy);
        clone.setSubtotalCy_r(mdSubtotalCy_r);
        clone.setTaxChargedCy_r(mdTaxChargedCy_r);
        clone.setTaxRetainedCy_r(mdTaxRetainedCy_r);
        clone.setTotalCy_r(mdTotalCy_r);
        clone.setPriceUnitaryRealCy_r(mdPriceUnitaryRealCy_r);
        clone.setCommissionsCy_r(mdCommissionsCy_r);
        clone.setOriginalQuantity(mdOriginalQuantity);
        clone.setOriginalPriceUnitaryCy(mdOriginalPriceUnitaryCy);
        clone.setOriginalPriceUnitarySystemCy(mdOriginalPriceUnitarySystemCy);
        clone.setOriginalDiscountUnitaryCy(mdOriginalDiscountUnitaryCy);
        clone.setOriginalDiscountUnitarySystemCy(mdOriginalDiscountUnitarySystemCy);
        clone.setSalesPriceUnitaryCy(mdSalesPriceUnitaryCy);
        clone.setSalesFreightUnitaryCy(mdSalesFreightUnitaryCy);
        clone.setLength(mdLength);
        clone.setSurface(mdSurface);
        clone.setVolume(mdVolume);
        clone.setMass(mdMass);
        clone.setWeightPackagingExtra(mdWeightPackagingExtra);
        clone.setWeightGross(mdWeightGross);
        clone.setWeightDelivery(mdWeightDelivery);
        clone.setSurplusPercentage(mdSurplusPercentage);
        clone.setContractBase(mdContractBase);
        clone.setContractFuture(mdContractFuture);
        clone.setContractFactor(mdContractFactor);
        clone.setContractPriceYear(mnContractPriceYear);
        clone.setContractPriceMonth(mnContractPriceMonth);
        clone.setSealQuality(msSealQuality);
        clone.setSealSecurity(msSealSecurity);
        clone.setDriver(msDriver);
        clone.setPlate(msPlate);
        clone.setTicket(msTicket);
        clone.setContainerTank(msContainerTank);
        clone.setVgm(msVgm);
        clone.setOperationsType(mnOperationsType);
        clone.setUserId(mnUserId);
        clone.setSortingPosition(mnSortingPosition);
        clone.setIsPrepayment(mbIsPrepayment);
        clone.setIsDiscountRetailChain(mbIsDiscountRetailChain);
        clone.setIsTaxesAutomaticApplying(mbIsTaxesAutomaticApplying);
        clone.setIsPriceVariable(mbIsPriceVariable);
        clone.setIsPriceConfirm(mbIsPriceConfirm);
        clone.setIsSalesFreightRequired(mbIsSalesFreightRequired);
        clone.setIsSalesFreightConfirm(mbIsSalesFreightConfirm);
        clone.setIsSalesFreightAdd(mbIsSalesFreightAdd);
        clone.setIsInventoriable(mbIsInventoriable);
        clone.setIsDeleted(mbIsDeleted);
        clone.setFkItemId(mnFkItemId);
        clone.setFkUnitId(mnFkUnitId);
        clone.setFkOriginalUnitId(mnFkOriginalUnitId);
        clone.setFkTaxRegionId(mnFkTaxRegionId);
        clone.setFkThirdTaxCausingId_n(mnFkThirdTaxCausingId_n);
        clone.setFkDpsAdjustmentTypeId(mnFkDpsAdjustmentTypeId);
        clone.setFkDpsAdjustmentSubtypeId(mnFkDpsAdjustmentSubtypeId);
        clone.setFkDpsEntryTypeId(mnFkDpsEntryTypeId);
        clone.setFkVehicleTypeId_n(mnFkVehicleTypeId_n);
        clone.setFkCashCompanyBranchId_n(mnFkCashCompanyBranchId_n);
        clone.setFkCashAccountId_n(mnFkCashAccountId_n);
        clone.setFkCostCenterId_n(msFkCostCenterId_n);
        clone.setFkItemRefId_n(mnFkItemRefId_n);
        clone.setFkUserNewId(mnFkUserNewId);
        clone.setFkUserEditId(mnFkUserEditId);
        clone.setFkUserDeleteId(mnFkUserDeleteId);
        clone.setUserNewTs(mtUserNewTs);
        clone.setUserEditTs(mtUserEditTs);
        clone.setUserDeleteTs(mtUserDeleteTs);

        clone.setDbmsFkItemGenericId(mnDbmsFkItemGenericId);
        clone.setDbmsItemGenDataShipDomesticReq(mbDbmsItemGenDataShipDomesticReq);
        clone.setDbmsItemGenDataShipInternationalReq(mbDbmsItemGenDataShipInternationalReq);
        clone.setDbmsItemGenDataShipQualityReq(mbDbmsItemGenDataShipQualityReq);
        clone.setDbmsItem(msDbmsItem);
        clone.setDbmsUnitSymbol(msDbmsUnitSymbol);
        clone.setDbmsOriginalUnitSymbol(msDbmsOriginalUnitSymbol);
        clone.setDbmsOriginalUnitBaseEquivalence(mdDbmsOriginalUnitBaseEquivalence);
        clone.setDbmsTaxRegion(msDbmsTaxRegion);
        clone.setDbmsDpsAdjustmentType(msDbmsDpsAdjustmentType);
        clone.setDbmsDpsEntryType(msDbmsDpsEntryType);
        clone.setDbmsCostCenter_n(msDbmsCostCenter_n);
        clone.setDbmsItemRef_n(msDbmsItemRef_n);
        clone.setDbmsItemBrandId(mnDbmsItemBrandId);
        clone.setDbmsItemBrand(msDbmsItemBrand);
        clone.setDbmsItemClaveProdServ(msDbmsItemClaveProdServ);
        clone.setDbmsUnidadClave(msDbmsUnidadClave);
        clone.setDbmsOriginalUnidadClave(msDbmsOriginalUnidadClave);
        clone.setDbmsCustomsUnit(msDbmsCustomsUnit);
        clone.setDbmsCustomsUnitSymbol(msDbmsCustomsUnitSymbol);
        clone.setDbmsItemCustomsUnitSymbol(msDbmsItemCustomsUnitSymbol);
        clone.setDbmsItemCustomsUnitEquivalence(mdDbmsItemCustomsUnitEquivalence);
        clone.setDbmsAddBachocoNumeroPosicion(mnDbmsAddBachocoNumeroPosicion);
        clone.setDbmsAddBachocoCentro(msDbmsAddBachocoCentro);
        clone.setDbmsAddLorealEntryNumber(mnDbmsAddLorealEntryNumber);
        clone.setDbmsAddSorianaCodigo(msDbmsAddSorianaCodigo);
        clone.setDbmsAddElektraOrder(msDbmsAddElektraOrder);
        clone.setDbmsAddElektraBarcode(msDbmsAddElektraBarcode);
        clone.setDbmsAddElektraCages(mnDbmsAddElektraCages);
        clone.setDbmsAddElektraCagePriceUnitary(mdDbmsAddElektraCagePriceUnitary);
        clone.setDbmsAddElektraParts(mnDbmsAddElektraParts);
        clone.setDbmsAddElektraPartPriceUnitary(mdDbmsAddElektraPartPriceUnitary);
        clone.setDbmsAddJsonData(msDbmsAddJsonData);

        clone.getDbmsEntryNotes().addAll(mvDbmsEntryNotes);
        clone.getDbmsEntryTaxes().addAll(mvDbmsEntryTaxes);
        clone.getDbmsEntryCommissions().addAll(mvDbmsEntryCommissions);
        clone.getDbmsEntryPrices().addAll(mvDbmsEntryPrices);
        clone.getDbmsDpsEntryAnalysis().addAll(mlDbmsDpsEntryAnalysis);
        clone.setDbmsDpsEntryMatRequest(moDbmsDpsEntryMatRequest);
        clone.setDbmsDpsEntryItemComposition(moDbmsDpsEntryItemComposition);

        clone.getDbmsDpsLinksAsSource().addAll(mvDbmsDpsLinksAsSource);
        clone.getDbmsDpsLinksAsDestiny().addAll(mvDbmsDpsLinksAsDestiny);
        clone.getDbmsDpsAdjustmentsAsDps().addAll(mvDbmsDpsAdjustmentsAsDps);
        clone.getDbmsDpsAdjustmentsAsAdjustment().addAll(mvDbmsDpsAdjustmentsAsAdjustment);
        
        clone.getDbmsDpsEntryQuantityChange().addAll(mlDbmsDpsEntryQuantityChange);

        clone.setDbmsDpsCfdEntry(moDbmsDpsCfdEntry == null ? null : moDbmsDpsCfdEntry.clone());

        clone.setAuxPkDpsYearId(mnAuxPkDpsYearId);
        clone.setAuxPkDpsDocId(mnAuxPkDpsDocId);
        clone.setAuxPkDpsEntryPrice(manAuxPkDpsEntryPrice);
        clone.setAuxPreserveQuantity(mbAuxPreserveQuantity);
        
        clone.setFlagReadLinksAswell(mbFlagReadLinksAswell);
        clone.setFlagMinorChangesEdited(mbFlagMinorChangesEdited);
        clone.setFlagOpenedByMatRequestImport(mbFlagOpenedByMatRequestImport);

        clone.setXtaIsPurInvoiceInventoriable(mbXtaIsPurInvoiceInventoriable);
        clone.setXtaPriceCommUnitary(mnXtaPriceCommUnitary);
        
        clone.setAuxFkItemOld(mnAuxFkItemOld);
        clone.setAuxQuantityOld(mdAuxQuantityOld);
        clone.setAuxOriginalQuantityOld(mdAuxOriginalQuantityOld);
        clone.setAuxFkUnitOld(mnAuxFkUnitOld);
        clone.setAuxFkOriginalUnitOld(mnAuxFkOriginalUnitOld);
        
        return clone;
    }
    
    /**
     * Validates if a field is requierd and has a valid value
     * @param value
     * @param requiered
     * @return 
     */
    public static boolean validateShipmentDataValue(final String value, final boolean requiered) {
        return ((value.isEmpty() || value.equals(STrnConsts.TXT_FIELD_BLANK)) && requiered);
    }
    
    /**
     * Verify if the value is equal to blank value
     * @param value
     * @return 
     */
    public static boolean isProvisionalValue(final String value) {
        return value.compareTo(STrnConsts.TXT_FIELD_BLANK) == 0;
    }
    
    /**
     * Verify if shipment data is needed
     * @return 
     */
    public boolean isMissingRequiredShipmentData() {
        if (validateShipmentDataValue(msDriver, mbDbmsItemGenDataShipDomesticReq)) {
            return true;
        }
        else if (validateShipmentDataValue(msPlate, mbDbmsItemGenDataShipDomesticReq)) {
            return true;
        }
        else if (validateShipmentDataValue(msContainerTank, mbDbmsItemGenDataShipDomesticReq)) {
            return true;
        }
        else if (validateShipmentDataValue(msSealQuality, mbDbmsItemGenDataShipQualityReq)) {
            return true;
        }
        else if (validateShipmentDataValue(msSealSecurity, mbDbmsItemGenDataShipQualityReq)) {
            return true;
        }
        else if (validateShipmentDataValue(msTicket, mbDbmsItemGenDataShipDomesticReq)) {
            return true;
        }
        else if (validateShipmentDataValue(msVgm, mbDbmsItemGenDataShipInternationalReq)) {
            return true;
        }
 
        return false;
    }
    
     /**
     * Verify if shipment data is needed
     * @return 
     */
    public boolean isValueProvisionalShipmentData() {
        if (mbDbmsItemGenDataShipDomesticReq && isProvisionalValue(msDriver)) {
            return true;
        }
        else if (mbDbmsItemGenDataShipDomesticReq && isProvisionalValue(msPlate)) {
            return true;
        }
        else if (mbDbmsItemGenDataShipDomesticReq && isProvisionalValue(msContainerTank)) {
            return true;
        }
        else if (mbDbmsItemGenDataShipQualityReq && isProvisionalValue(msSealSecurity)) {
            return true;
        }
        else if (mbDbmsItemGenDataShipQualityReq && isProvisionalValue(msSealQuality)) {
            return true;
        }
        else if (mbDbmsItemGenDataShipDomesticReq && isProvisionalValue(msTicket)) {
            return true;
        }
        else if (mbDbmsItemGenDataShipInternationalReq && isProvisionalValue(msVgm)) {
            return true;
        }
 
        return false;
    }
}
