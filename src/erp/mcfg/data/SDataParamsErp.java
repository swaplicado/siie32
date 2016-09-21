/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mcfg.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import sa.lib.gui.SGuiConfigSystem;

/**
 *
 * @author Sergio Flores
 */
public class SDataParamsErp extends erp.lib.data.SDataRegistry implements java.io.Serializable, SGuiConfigSystem {

    protected int mnPkErpId;
    protected java.lang.String msErp;
    protected int mnDecimalsValue;
    protected int mnDecimalsValueUnitary;
    protected int mnDecimalsExchangeRate;
    protected int mnDecimalsPercentage;
    protected int mnDecimalsDiscount;
    protected boolean mbIsKeyLocalityApplying;
    protected boolean mbIsKeySupplierApplying;
    protected boolean mbIsKeyCustomerApplying;
    protected boolean mbIsKeyCreditorApplying;
    protected boolean mbIsKeyDebtorApplying;
    protected boolean mbIsKeyEmployeeApplying;
    protected java.lang.String msFormatKeyLocality;
    protected java.lang.String msFormatKeyCounty;
    protected java.lang.String msFormatKeyState;
    protected java.lang.String msFormatKeyCountry;
    protected java.lang.String msFormatKeySupplier;
    protected java.lang.String msFormatKeyCustomer;
    protected java.lang.String msFormatKeyCreditor;
    protected java.lang.String msFormatKeyDebtor;
    protected java.lang.String msFormatKeyEmployee;
    protected java.lang.String msLocaleId;
    protected int mnDeepAccounts;
    protected int mnDeepCostCenters;
    protected java.lang.String msFormatAccountId;
    protected java.lang.String msFormatCostCenterId;
    protected double mdSupplierCreditLimit;
    protected int mnSupplierDaysOfCredit;
    protected int mnSupplierDaysOfGrace;
    protected int mnSupplierDaysOfDocLapsing_n;
    protected double mdCustomerCreditLimit;
    protected int mnCustomerDaysOfCredit;
    protected int mnCustomerDaysOfGrace;
    protected int mnCustomerDaysOfDocLapsing_n;
    protected int mnTaxModel;
    protected int mnLotModel;
    protected boolean mbIsItemShortApplying;
    protected boolean mbIsItemNameWithVarieties;
    protected boolean mbIsItemNameEditable;
    protected boolean mbIsItemKeyApplying;
    protected boolean mbIsItemKeyAutomatic;
    protected boolean mbIsItemKeyWithVarieties;
    protected boolean mbIsItemKeyEditable;
    protected boolean mbIsInventoriable;
    protected boolean mbIsLotApplying;
    protected boolean mbIsBulk;
    protected boolean mbIsUnitsContainedApplying;
    protected boolean mbIsUnitsVirtualApplying;
    protected boolean mbIsUnitsPackageApplying;
    protected boolean mbIsNetContentApplying;
    protected boolean mbIsNetContentUnitaryApplying;
    protected boolean mbIsNetContentVariable;
    protected boolean mbIsLengthApplying;
    protected boolean mbIsLengthUnitaryApplying;
    protected boolean mbIsLengthVariable;
    protected boolean mbIsSurfaceApplying;
    protected boolean mbIsSurfaceUnitaryApplying;
    protected boolean mbIsSurfaceVariable;
    protected boolean mbIsVolumeApplying;
    protected boolean mbIsVolumeUnitaryApplying;
    protected boolean mbIsVolumeVariable;
    protected boolean mbIsMassApplying;
    protected boolean mbIsMassUnitaryApplying;
    protected boolean mbIsMassVariable;
    protected boolean mbIsWeightGrossApplying;
    protected boolean mbIsWeightDeliveryApplying;
    protected boolean mbIsFreePrice;
    protected boolean mbIsFreeDiscount;
    protected boolean mbIsFreeDiscountUnitary;
    protected boolean mbIsFreeDiscountEntry;
    protected boolean mbIsFreeDiscountDoc;
    protected boolean mbIsFreeCommissions;
    protected int mnDecimalsQuantity;
    protected int mnDecimalsUnitsContained;
    protected int mnDecimalsUnitsVirtual;
    protected int mnDecimalsNetContent;
    protected int mnDecimalsLength;
    protected int mnDecimalsSurface;
    protected int mnDecimalsVolume;
    protected int mnDecimalsMass;
    protected int mnDecimalsWeigthGross;
    protected int mnDecimalsWeightDelivery;
    protected boolean mbIsPurchasesLinkPeriod;
    protected boolean mbIsPurchasesWarehouseMultiple;
    protected boolean mbIsPurchasesAdjustmentMultiple;
    protected boolean mbIsPurchasesCreditContract;
    protected boolean mbIsPurchasesCreditOrder;
    protected boolean mbIsPurchasesCreditInvoice;
    protected boolean mbIsSalesLinkPeriod;
    protected boolean mbIsSalesWarehouseMultiple;
    protected boolean mbIsSalesAdjustmentMultiple;
    protected boolean mbIsSalesCreditContract;
    protected boolean mbIsSalesCreditOrder;
    protected boolean mbIsSalesCreditInvoice;
    protected boolean mbIsRecordConceptCopyEnabled;
    protected int mnExclusiveAccessCatalogues;
    protected int mnExclusiveAccessDocsPurchases;
    protected int mnExclusiveAccessDocsSales;
    protected int mnExclusiveAccessPrices;
    protected int mnExclusiveAccessComissions;
    protected int mnVersion;
    protected java.util.Date mtVersionTs;
    protected boolean mbIsCanEdit;
    protected boolean mbIsCanDelete;
    protected boolean mbIsDeleted;
    protected int mnFkLanguageId;
    protected int mnFkCurrencyId;
    protected int mnFkFormatDateTypeId;
    protected int mnFkFormatDatetimeTypeId;
    protected int mnFkFormatTimeTypeId;
    protected int mnFkBalanceAccountsDebitTypeId;
    protected int mnFkBalanceAccountsCreditTypeId;
    protected int mnFkPurchasesRelationEstimateOrderTypeId;
    protected int mnFkPurchasesRelationEstimateDocTypeId;
    protected int mnFkPurchasesRelationOrderDocTypeId;
    protected int mnFkSalesRelationEstimateOrderTypeId;
    protected int mnFkSalesRelationEstimateDocTypeId;
    protected int mnFkSalesRelationOrderDocTypeId;
    protected int mnFkDbmsTypeId;
    protected int mnFkCountryId;
    protected int mnFkSortingLocalityTypeId;
    protected int mnFkSortingSupplierTypeId;
    protected int mnFkSortingCustomerTypeId;
    protected int mnFkSortingCreditorTypeId;
    protected int mnFkSortingDebtorTypeId;
    protected int mnFkSortingEmployeeTypeId;
    protected int mnFkSortingItemTypeId;
    protected int mnFkSortingDpsSupplierTypeId;
    protected int mnFkSortingDpsCustomerTypeId;
    protected int mnFkBizPartnerAnnulmentId;
    protected int mnFkTaxRegionId_n;
    protected int mnFkSupplierCategoryId_n;
    protected int mnFkSupplierTypeId_n;
    protected int mnFkCustomerCategoryId_n;
    protected int mnFkCustomerTypeId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected erp.mcfg.data.SDataCurrency moDbmsDataCurrency;

    public SDataParamsErp() {
        super(SDataConstants.CFG_PARAM_ERP);
        reset();
    }

    public void setPkErpId(int n) { mnPkErpId = n; }
    public void setErp(java.lang.String s) { msErp = s; }
    public void setDecimalsValue(int n) { mnDecimalsValue = n; }
    public void setDecimalsValueUnitary(int n) { mnDecimalsValueUnitary = n; }
    public void setDecimalsExchangeRate(int n) { mnDecimalsExchangeRate = n; }
    public void setDecimalsPercentage(int n) { mnDecimalsPercentage = n; }
    public void setDecimalsDiscount(int n) { mnDecimalsDiscount = n; }
    public void setIsKeyLocalityApplying(boolean b) { mbIsKeyLocalityApplying = b; }
    public void setIsKeySupplierApplying(boolean b) { mbIsKeySupplierApplying = b; }
    public void setIsKeyCustomerApplying(boolean b) { mbIsKeyCustomerApplying = b; }
    public void setIsKeyCreditorApplying(boolean b) { mbIsKeyCreditorApplying = b; }
    public void setIsKeyDebtorApplying(boolean b) { mbIsKeyDebtorApplying = b; }
    public void setIsKeyEmployeeApplying(boolean b) { mbIsKeyEmployeeApplying = b; }
    public void setFormatKeyLocality(java.lang.String s) { msFormatKeyLocality = s; }
    public void setFormatKeyCounty(java.lang.String s) { msFormatKeyCounty = s; }
    public void setFormatKeyState(java.lang.String s) { msFormatKeyState = s; }
    public void setFormatKeyCountry(java.lang.String s) { msFormatKeyCountry = s; }
    public void setFormatKeySupplier(java.lang.String s) { msFormatKeySupplier = s; }
    public void setFormatKeyCustomer(java.lang.String s) { msFormatKeyCustomer = s; }
    public void setFormatKeyCreditor(java.lang.String s) { msFormatKeyCreditor = s; }
    public void setFormatKeyDebtor(java.lang.String s) { msFormatKeyDebtor = s; }
    public void setFormatKeyEmployee(java.lang.String s) { msFormatKeyEmployee = s; }
    public void setLocaleId(java.lang.String s) { msLocaleId = s; }
    public void setDeepAccounts(int n) { mnDeepAccounts = n; }
    public void setDeepCostCenters(int n) { mnDeepCostCenters = n; }
    public void setFormatAccountId(java.lang.String s) { msFormatAccountId = s; }
    public void setFormatCostCenterId(java.lang.String s) { msFormatCostCenterId = s; }
    public void setSupplierCreditLimit(double d) { mdSupplierCreditLimit = d; }
    public void setSupplierDaysOfCredit(int n) { mnSupplierDaysOfCredit = n; }
    public void setSupplierDaysOfGrace(int n) { mnSupplierDaysOfGrace = n; }
    public void setSupplierDaysOfDocLapsing_n(int n) { mnSupplierDaysOfDocLapsing_n = n; }
    public void setCustomerCreditLimit(double d) { mdCustomerCreditLimit = d; }
    public void setCustomerDaysOfCredit(int n) { mnCustomerDaysOfCredit = n; }
    public void setCustomerDaysOfGrace(int n) { mnCustomerDaysOfGrace = n; }
    public void setCustomerDaysOfDocLapsing_n(int n) { mnCustomerDaysOfDocLapsing_n = n; }
    public void setTaxModel(int n) { mnTaxModel = n; }
    public void setLotModel(int n) { mnLotModel = n; }
    public void setIsItemShortApplying(boolean b) { mbIsItemShortApplying = b; }
    public void setIsItemNameWithVarieties(boolean b) { mbIsItemNameWithVarieties = b; }
    public void setIsItemNameEditable(boolean b) { mbIsItemNameEditable = b; }
    public void setIsItemKeyApplying(boolean b) { mbIsItemKeyApplying = b; }
    public void setIsItemKeyAutomatic(boolean b) { mbIsItemKeyAutomatic = b; }
    public void setIsItemKeyWithVarieties(boolean b) { mbIsItemKeyWithVarieties = b; }
    public void setIsItemKeyEditable(boolean b) { mbIsItemKeyEditable = b; }
    public void setIsInventoriable(boolean b) { mbIsInventoriable = b; }
    public void setIsLotApplying(boolean b) { mbIsLotApplying = b; }
    public void setIsBulk(boolean b) { mbIsBulk = b; }
    public void setIsUnitsContainedApplying(boolean b) { mbIsUnitsContainedApplying = b; }
    public void setIsUnitsVirtualApplying(boolean b) { mbIsUnitsVirtualApplying = b; }
    public void setIsUnitsPackageApplying(boolean b) { mbIsUnitsPackageApplying = b; }
    public void setIsNetContentApplying(boolean b) { mbIsNetContentApplying = b; }
    public void setIsNetContentUnitaryApplying(boolean b) { mbIsNetContentUnitaryApplying = b; }
    public void setIsNetContentVariable(boolean b) { mbIsNetContentVariable = b; }
    public void setIsLengthApplying(boolean b) { mbIsLengthApplying = b; }
    public void setIsLengthUnitaryApplying(boolean b) { mbIsLengthUnitaryApplying = b; }
    public void setIsLengthVariable(boolean b) { mbIsLengthVariable = b; }
    public void setIsSurfaceApplying(boolean b) { mbIsSurfaceApplying = b; }
    public void setIsSurfaceUnitaryApplying(boolean b) { mbIsSurfaceUnitaryApplying = b; }
    public void setIsSurfaceVariable(boolean b) { mbIsSurfaceVariable = b; }
    public void setIsVolumeApplying(boolean b) { mbIsVolumeApplying = b; }
    public void setIsVolumeUnitaryApplying(boolean b) { mbIsVolumeUnitaryApplying = b; }
    public void setIsVolumeVariable(boolean b) { mbIsVolumeVariable = b; }
    public void setIsMassApplying(boolean b) { mbIsMassApplying = b; }
    public void setIsMassUnitaryApplying(boolean b) { mbIsMassUnitaryApplying = b; }
    public void setIsMassVariable(boolean b) { mbIsMassVariable = b; }
    public void setIsWeightGrossApplying(boolean b) { mbIsWeightGrossApplying = b; }
    public void setIsWeightDeliveryApplying(boolean b) { mbIsWeightDeliveryApplying = b; }
    public void setIsFreePrice(boolean b) { mbIsFreePrice = b; }
    public void setIsFreeDiscount(boolean b) { mbIsFreeDiscount = b; }
    public void setIsFreeDiscountUnitary(boolean b) { mbIsFreeDiscountUnitary = b; }
    public void setIsFreeDiscountEntry(boolean b) { mbIsFreeDiscountEntry = b; }
    public void setIsFreeDiscountDoc(boolean b) { mbIsFreeDiscountDoc = b; }
    public void setIsFreeCommissions(boolean b) { mbIsFreeCommissions = b; }
    public void setDecimalsQuantity(int n) { mnDecimalsQuantity = n; }
    public void setDecimalsUnitsContained(int n) { mnDecimalsUnitsContained = n; }
    public void setDecimalsUnitsVirtual(int n) { mnDecimalsUnitsVirtual = n; }
    public void setDecimalsNetContent(int n) { mnDecimalsNetContent = n; }
    public void setDecimalsLength(int n) { mnDecimalsLength = n; }
    public void setDecimalsSurface(int n) { mnDecimalsSurface = n; }
    public void setDecimalsVolume(int n) { mnDecimalsVolume = n; }
    public void setDecimalsMass(int n) { mnDecimalsMass = n; }
    public void setDecimalsWeigthGross(int n) { mnDecimalsWeigthGross = n; }
    public void setDecimalsWeightDelivery(int n) { mnDecimalsWeightDelivery = n; }
    public void setIsPurchasesLinkPeriod(boolean b) { mbIsPurchasesLinkPeriod = b; }
    public void setIsPurchasesWarehouseMultiple(boolean b) { mbIsPurchasesWarehouseMultiple = b; }
    public void setIsPurchasesAdjustmentMultiple(boolean b) { mbIsPurchasesAdjustmentMultiple = b; }
    public void setIsPurchasesCreditContract(boolean b) { mbIsPurchasesCreditContract = b; }
    public void setIsPurchasesCreditOrder(boolean b) { mbIsPurchasesCreditOrder = b; }
    public void setIsPurchasesCreditInvoice(boolean b) { mbIsPurchasesCreditInvoice = b; }
    public void setIsSalesLinkPeriod(boolean b) { mbIsSalesLinkPeriod = b; }
    public void setIsSalesWarehouseMultiple(boolean b) { mbIsSalesWarehouseMultiple = b; }
    public void setIsSalesAdjustmentMultiple(boolean b) { mbIsSalesAdjustmentMultiple = b; }
    public void setIsSalesCreditContract(boolean b) { mbIsSalesCreditContract = b; }
    public void setIsSalesCreditOrder(boolean b) { mbIsSalesCreditOrder = b; }
    public void setIsSalesCreditInvoice(boolean b) { mbIsSalesCreditInvoice = b; }
    public void setIsRecordConceptCopyEnabled(boolean b) { mbIsRecordConceptCopyEnabled = b; }
    public void setExclusiveAccessCatalogues(int n) { mnExclusiveAccessCatalogues = n; }
    public void setExclusiveAccessDocsPurchases(int n) { mnExclusiveAccessDocsPurchases = n; }
    public void setExclusiveAccessDocsSales(int n) { mnExclusiveAccessDocsSales = n; }
    public void setExclusiveAccessPrices(int n) { mnExclusiveAccessPrices = n; }
    public void setExclusiveAccessComissions(int n) { mnExclusiveAccessComissions = n; }
    public void setVersion(int n) { mnVersion = n; }
    public void setVersionTs(java.util.Date t) { mtVersionTs = t; }
    public void setIsCanEdit(boolean b) { mbIsCanEdit = b; }
    public void setIsCanDelete(boolean b) { mbIsCanDelete = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkLanguageId(int n) { mnFkLanguageId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkFormatDateTypeId(int n) { mnFkFormatDateTypeId = n; }
    public void setFkFormatDatetimeTypeId(int n) { mnFkFormatDatetimeTypeId = n; }
    public void setFkFormatTimeTypeId(int n) { mnFkFormatTimeTypeId = n; }
    public void setFkBalanceAccountsDebitTypeId(int n) { mnFkBalanceAccountsDebitTypeId = n; }
    public void setFkBalanceAccountsCreditTypeId(int n) { mnFkBalanceAccountsCreditTypeId = n; }
    public void setFkPurchasesRelationEstimateOrderTypeId(int n) { mnFkPurchasesRelationEstimateOrderTypeId = n; }
    public void setFkPurchasesRelationEstimateDocTypeId(int n) { mnFkPurchasesRelationEstimateDocTypeId = n; }
    public void setFkPurchasesRelationOrderDocTypeId(int n) { mnFkPurchasesRelationOrderDocTypeId = n; }
    public void setFkSalesRelationEstimateOrderTypeId(int n) { mnFkSalesRelationEstimateOrderTypeId = n; }
    public void setFkSalesRelationEstimateDocTypeId(int n) { mnFkSalesRelationEstimateDocTypeId = n; }
    public void setFkSalesRelationOrderDocTypeId(int n) { mnFkSalesRelationOrderDocTypeId = n; }
    public void setFkDbmsTypeId(int n) { mnFkDbmsTypeId = n; }
    public void setFkCountryId(int n) { mnFkCountryId = n; }
    public void setFkSortingLocalityTypeId(int n) { mnFkSortingLocalityTypeId = n; }
    public void setFkSortingSupplierTypeId(int n) { mnFkSortingSupplierTypeId = n; }
    public void setFkSortingCustomerTypeId(int n) { mnFkSortingCustomerTypeId = n; }
    public void setFkSortingCreditorTypeId(int n) { mnFkSortingCreditorTypeId = n; }
    public void setFkSortingDebtorTypeId(int n) { mnFkSortingDebtorTypeId = n; }
    public void setFkSortingEmployeeTypeId(int n) { mnFkSortingEmployeeTypeId = n; }
    public void setFkSortingItemTypeId(int n) { mnFkSortingItemTypeId = n; }
    public void setFkSortingDpsSupplierTypeId(int n) { mnFkSortingDpsSupplierTypeId = n; }
    public void setFkSortingDpsCustomerTypeId(int n) { mnFkSortingDpsCustomerTypeId = n; }
    public void setFkBizPartnerAnnulmentId(int n) { mnFkBizPartnerAnnulmentId = n; }
    public void setFkTaxRegionId_n(int n) { mnFkTaxRegionId_n = n; }
    public void setFkSupplierCategoryId_n(int n) { mnFkSupplierCategoryId_n = n; }
    public void setFkSupplierTypeId_n(int n) { mnFkSupplierTypeId_n = n; }
    public void setFkCustomerCategoryId_n(int n) { mnFkCustomerCategoryId_n = n; }
    public void setFkCustomerTypeId_n(int n) { mnFkCustomerTypeId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkErpId() { return mnPkErpId; }
    public java.lang.String getErp() { return msErp; }
    public int getDecimalsValue() { return mnDecimalsValue; }
    public int getDecimalsValueUnitary() { return mnDecimalsValueUnitary; }
    public int getDecimalsExchangeRate() { return mnDecimalsExchangeRate; }
    public int getDecimalsPercentage() { return mnDecimalsPercentage; }
    public int getDecimalsDiscount() { return mnDecimalsDiscount; }
    public boolean getIsKeyLocalityApplying() { return mbIsKeyLocalityApplying; }
    public boolean getIsKeySupplierApplying() { return mbIsKeySupplierApplying; }
    public boolean getIsKeyCustomerApplying() { return mbIsKeyCustomerApplying; }
    public boolean getIsKeyCreditorApplying() { return mbIsKeyCreditorApplying; }
    public boolean getIsKeyDebtorApplying() { return mbIsKeyDebtorApplying; }
    public boolean getIsKeyEmployeeApplying() { return mbIsKeyEmployeeApplying; }
    public java.lang.String getFormatKeyLocality() { return msFormatKeyLocality; }
    public java.lang.String getFormatKeyCounty() { return msFormatKeyCounty; }
    public java.lang.String getFormatKeyState() { return msFormatKeyState; }
    public java.lang.String getFormatKeyCountry() { return msFormatKeyCountry; }
    public java.lang.String getFormatKeySupplier() { return msFormatKeySupplier; }
    public java.lang.String getFormatKeyCustomer() { return msFormatKeyCustomer; }
    public java.lang.String getFormatKeyCreditor() { return msFormatKeyCreditor; }
    public java.lang.String getFormatKeyDebtor() { return msFormatKeyDebtor; }
    public java.lang.String getFormatKeyEmployee() { return msFormatKeyEmployee; }
    public java.lang.String getLocaleId() { return msLocaleId; }
    public int getDeepAccounts() { return mnDeepAccounts; }
    public int getDeepCostCenters() { return mnDeepCostCenters; }
    public java.lang.String getFormatAccountId() { return msFormatAccountId; }
    public java.lang.String getFormatCostCenterId() { return msFormatCostCenterId; }
    public double getSupplierCreditLimit() { return mdSupplierCreditLimit; }
    public int getSupplierDaysOfCredit() { return mnSupplierDaysOfCredit; }
    public int getSupplierDaysOfGrace() { return mnSupplierDaysOfGrace; }
    public int getSupplierDaysOfDocLapsing_n() { return mnSupplierDaysOfDocLapsing_n; }
    public double getCustomerCreditLimit() { return mdCustomerCreditLimit; }
    public int getCustomerDaysOfCredit() { return mnCustomerDaysOfCredit; }
    public int getCustomerDaysOfGrace() { return mnCustomerDaysOfGrace; }
    public int getCustomerDaysOfDocLapsing_n() { return mnCustomerDaysOfDocLapsing_n; }
    public int getTaxModel() { return mnTaxModel; }
    public int getLotModel() { return mnLotModel; }
    public boolean getIsItemShortApplying() { return mbIsItemShortApplying; }
    public boolean getIsItemNameWithVarieties() { return mbIsItemNameWithVarieties; }
    public boolean getIsItemNameEditable() { return mbIsItemNameEditable; }
    public boolean getIsItemKeyApplying() { return mbIsItemKeyApplying; }
    public boolean getIsItemKeyAutomatic() { return mbIsItemKeyAutomatic; }
    public boolean getIsItemKeyWithVarieties() { return mbIsItemKeyWithVarieties; }
    public boolean getIsItemKeyEditable() { return mbIsItemKeyEditable; }
    public boolean getIsInventoriable() { return mbIsInventoriable; }
    public boolean getIsLotApplying() { return mbIsLotApplying; }
    public boolean getIsBulk() { return mbIsBulk; }
    public boolean getIsUnitsContainedApplying() { return mbIsUnitsContainedApplying; }
    public boolean getIsUnitsVirtualApplying() { return mbIsUnitsVirtualApplying; }
    public boolean getIsUnitsPackageApplying() { return mbIsUnitsPackageApplying; }
    public boolean getIsNetContentApplying() { return mbIsNetContentApplying; }
    public boolean getIsNetContentUnitaryApplying() { return mbIsNetContentUnitaryApplying; }
    public boolean getIsNetContentVariable() { return mbIsNetContentVariable; }
    public boolean getIsLengthApplying() { return mbIsLengthApplying; }
    public boolean getIsLengthUnitaryApplying() { return mbIsLengthUnitaryApplying; }
    public boolean getIsLengthVariable() { return mbIsLengthVariable; }
    public boolean getIsSurfaceApplying() { return mbIsSurfaceApplying; }
    public boolean getIsSurfaceUnitaryApplying() { return mbIsSurfaceUnitaryApplying; }
    public boolean getIsSurfaceVariable() { return mbIsSurfaceVariable; }
    public boolean getIsVolumeApplying() { return mbIsVolumeApplying; }
    public boolean getIsVolumeUnitaryApplying() { return mbIsVolumeUnitaryApplying; }
    public boolean getIsVolumeVariable() { return mbIsVolumeVariable; }
    public boolean getIsMassApplying() { return mbIsMassApplying; }
    public boolean getIsMassUnitaryApplying() { return mbIsMassUnitaryApplying; }
    public boolean getIsMassVariable() { return mbIsMassVariable; }
    public boolean getIsWeightGrossApplying() { return mbIsWeightGrossApplying; }
    public boolean getIsWeightDeliveryApplying() { return mbIsWeightDeliveryApplying; }
    public boolean getIsFreePrice() { return mbIsFreePrice; }
    public boolean getIsFreeDiscount() { return mbIsFreeDiscount; }
    public boolean getIsFreeDiscountUnitary() { return mbIsFreeDiscountUnitary; }
    public boolean getIsFreeDiscountEntry() { return mbIsFreeDiscountEntry; }
    public boolean getIsFreeDiscountDoc() { return mbIsFreeDiscountDoc; }
    public boolean getIsFreeCommissions() { return mbIsFreeCommissions; }
    public int getDecimalsQuantity() { return mnDecimalsQuantity; }
    public int getDecimalsUnitsContained() { return mnDecimalsUnitsContained; }
    public int getDecimalsUnitsVirtual() { return mnDecimalsUnitsVirtual; }
    public int getDecimalsNetContent() { return mnDecimalsNetContent; }
    public int getDecimalsLength() { return mnDecimalsLength; }
    public int getDecimalsSurface() { return mnDecimalsSurface; }
    public int getDecimalsVolume() { return mnDecimalsVolume; }
    public int getDecimalsMass() { return mnDecimalsMass; }
    public int getDecimalsWeigthGross() { return mnDecimalsWeigthGross; }
    public int getDecimalsWeightDelivery() { return mnDecimalsWeightDelivery; }
    public boolean getIsPurchasesLinkPeriod() { return mbIsPurchasesLinkPeriod; }
    public boolean getIsPurchasesWarehouseMultiple() { return mbIsPurchasesWarehouseMultiple; }
    public boolean getIsPurchasesAdjustmentMultiple() { return mbIsPurchasesAdjustmentMultiple; }
    public boolean getIsPurchasesCreditContract() { return mbIsPurchasesCreditContract; }
    public boolean getIsPurchasesCreditOrder() { return mbIsPurchasesCreditOrder; }
    public boolean getIsPurchasesCreditInvoice() { return mbIsPurchasesCreditInvoice; }
    public boolean getIsSalesLinkPeriod() { return mbIsSalesLinkPeriod; }
    public boolean getIsSalesWarehouseMultiple() { return mbIsSalesWarehouseMultiple; }
    public boolean getIsSalesAdjustmentMultiple() { return mbIsSalesAdjustmentMultiple; }
    public boolean getIsSalesCreditContract() { return mbIsSalesCreditContract; }
    public boolean getIsSalesCreditOrder() { return mbIsSalesCreditOrder; }
    public boolean getIsSalesCreditInvoice() { return mbIsSalesCreditInvoice; }
    public boolean getIsRecordConceptCopyEnabled() { return mbIsRecordConceptCopyEnabled; }
    public int getExclusiveAccessCatalogues() { return mnExclusiveAccessCatalogues; }
    public int getExclusiveAccessDocsPurchases() { return mnExclusiveAccessDocsPurchases; }
    public int getExclusiveAccessDocsSales() { return mnExclusiveAccessDocsSales; }
    public int getExclusiveAccessPrices() { return mnExclusiveAccessPrices; }
    public int getExclusiveAccessComissions() { return mnExclusiveAccessComissions; }
    public int getVersion() { return mnVersion; }
    public java.util.Date getVersionTs() { return mtVersionTs; }
    public boolean getIsCanEdit() { return mbIsCanEdit; }
    public boolean getIsCanDelete() { return mbIsCanDelete; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkLanguageId() { return mnFkLanguageId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkFormatDateTypeId() { return mnFkFormatDateTypeId; }
    public int getFkFormatDatetimeTypeId() { return mnFkFormatDatetimeTypeId; }
    public int getFkFormatTimeTypeId() { return mnFkFormatTimeTypeId; }
    public int getFkBalanceAccountsDebitTypeId() { return mnFkBalanceAccountsDebitTypeId; }
    public int getFkBalanceAccountsCreditTypeId() { return mnFkBalanceAccountsCreditTypeId; }
    public int getFkPurchasesRelationEstimateOrderTypeId() { return mnFkPurchasesRelationEstimateOrderTypeId; }
    public int getFkPurchasesRelationEstimateDocTypeId() { return mnFkPurchasesRelationEstimateDocTypeId; }
    public int getFkPurchasesRelationOrderDocTypeId() { return mnFkPurchasesRelationOrderDocTypeId; }
    public int getFkSalesRelationEstimateOrderTypeId() { return mnFkSalesRelationEstimateOrderTypeId; }
    public int getFkSalesRelationEstimateDocTypeId() { return mnFkSalesRelationEstimateDocTypeId; }
    public int getFkSalesRelationOrderDocTypeId() { return mnFkSalesRelationOrderDocTypeId; }
    public int getFkDbmsTypeId() { return mnFkDbmsTypeId; }
    public int getFkCountryId() { return mnFkCountryId; }
    public int getFkSortingLocalityTypeId() { return mnFkSortingLocalityTypeId; }
    public int getFkSortingSupplierTypeId() { return mnFkSortingSupplierTypeId; }
    public int getFkSortingCustomerTypeId() { return mnFkSortingCustomerTypeId; }
    public int getFkSortingCreditorTypeId() { return mnFkSortingCreditorTypeId; }
    public int getFkSortingDebtorTypeId() { return mnFkSortingDebtorTypeId; }
    public int getFkSortingEmployeeTypeId() { return mnFkSortingEmployeeTypeId; }
    public int getFkSortingItemTypeId() { return mnFkSortingItemTypeId; }
    public int getFkSortingDpsSupplierTypeId() { return mnFkSortingDpsSupplierTypeId; }
    public int getFkSortingDpsCustomerTypeId() { return mnFkSortingDpsCustomerTypeId; }
    public int getFkBizPartnerAnnulmentId() { return mnFkBizPartnerAnnulmentId; }
    public int getFkTaxRegionId_n() { return mnFkTaxRegionId_n; }
    public int getFkSupplierCategoryId_n() { return mnFkSupplierCategoryId_n; }
    public int getFkSupplierTypeId_n() { return mnFkSupplierTypeId_n; }
    public int getFkCustomerCategoryId_n() { return mnFkCustomerCategoryId_n; }
    public int getFkCustomerTypeId_n() { return mnFkCustomerTypeId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public erp.mcfg.data.SDataCurrency getDbmsDataCurrency() { return moDbmsDataCurrency; }

    @Override
    public void setPrimaryKey(java.lang.Object key) {
        mnPkErpId = ((int[]) key)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkErpId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkErpId = 0;
        msErp = "";
        mnDecimalsValue = 0;
        mnDecimalsValueUnitary = 0;
        mnDecimalsExchangeRate = 0;
        mnDecimalsPercentage = 0;
        mnDecimalsDiscount = 0;
        mbIsKeyLocalityApplying = false;
        mbIsKeySupplierApplying = false;
        mbIsKeyCustomerApplying = false;
        mbIsKeyCreditorApplying = false;
        mbIsKeyDebtorApplying = false;
        mbIsKeyEmployeeApplying = false;
        msFormatKeyLocality = "";
        msFormatKeyCounty = "";
        msFormatKeyState = "";
        msFormatKeyCountry = "";
        msFormatKeySupplier = "";
        msFormatKeyCustomer = "";
        msFormatKeyCreditor = "";
        msFormatKeyDebtor = "";
        msFormatKeyEmployee = "";
        msLocaleId = "";
        mnDeepAccounts = 0;
        mnDeepCostCenters = 0;
        msFormatAccountId = "";
        msFormatCostCenterId = "";
        mdSupplierCreditLimit = 0;
        mnSupplierDaysOfCredit = 0;
        mnSupplierDaysOfGrace = 0;
        mnSupplierDaysOfDocLapsing_n = 0;
        mdCustomerCreditLimit = 0;
        mnCustomerDaysOfCredit = 0;
        mnCustomerDaysOfGrace = 0;
        mnCustomerDaysOfDocLapsing_n = 0;
        mnTaxModel = 0;
        mnLotModel = 0;
        mbIsItemShortApplying = false;
        mbIsItemNameWithVarieties = false;
        mbIsItemNameEditable = false;
        mbIsItemKeyApplying = false;
        mbIsItemKeyAutomatic = false;
        mbIsItemKeyWithVarieties = false;
        mbIsItemKeyEditable = false;
        mbIsInventoriable = false;
        mbIsLotApplying = false;
        mbIsBulk = false;
        mbIsUnitsContainedApplying = false;
        mbIsUnitsVirtualApplying = false;
        mbIsUnitsPackageApplying = false;
        mbIsNetContentApplying = false;
        mbIsNetContentUnitaryApplying = false;
        mbIsNetContentVariable = false;
        mbIsLengthApplying = false;
        mbIsLengthUnitaryApplying = false;
        mbIsLengthVariable = false;
        mbIsSurfaceApplying = false;
        mbIsSurfaceUnitaryApplying = false;
        mbIsSurfaceVariable = false;
        mbIsVolumeApplying = false;
        mbIsVolumeUnitaryApplying = false;
        mbIsVolumeVariable = false;
        mbIsMassApplying = false;
        mbIsMassUnitaryApplying = false;
        mbIsMassVariable = false;
        mbIsWeightGrossApplying = false;
        mbIsWeightDeliveryApplying = false;
        mbIsFreePrice = false;
        mbIsFreeDiscount = false;
        mbIsFreeDiscountUnitary = false;
        mbIsFreeDiscountEntry = false;
        mbIsFreeDiscountDoc = false;
        mbIsFreeCommissions = false;
        mnDecimalsQuantity = 0;
        mnDecimalsUnitsContained = 0;
        mnDecimalsUnitsVirtual = 0;
        mnDecimalsNetContent = 0;
        mnDecimalsLength = 0;
        mnDecimalsSurface = 0;
        mnDecimalsVolume = 0;
        mnDecimalsMass = 0;
        mnDecimalsWeigthGross = 0;
        mnDecimalsWeightDelivery = 0;
        mbIsPurchasesLinkPeriod = false;
        mbIsPurchasesWarehouseMultiple = false;
        mbIsPurchasesAdjustmentMultiple = false;
        mbIsPurchasesCreditContract = false;
        mbIsPurchasesCreditOrder = false;
        mbIsPurchasesCreditInvoice = false;
        mbIsSalesLinkPeriod = false;
        mbIsSalesWarehouseMultiple = false;
        mbIsSalesAdjustmentMultiple = false;
        mbIsSalesCreditContract = false;
        mbIsSalesCreditOrder = false;
        mbIsSalesCreditInvoice = false;
        mbIsRecordConceptCopyEnabled = false;
        mnExclusiveAccessCatalogues = 0;
        mnExclusiveAccessDocsPurchases = 0;
        mnExclusiveAccessDocsSales = 0;
        mnExclusiveAccessPrices = 0;
        mnExclusiveAccessComissions = 0;
        mnVersion = 0;
        mtVersionTs = null;
        mbIsCanEdit = false;
        mbIsCanDelete = false;
        mbIsDeleted = false;
        mnFkLanguageId = 0;
        mnFkCurrencyId = 0;
        mnFkFormatDateTypeId = 0;
        mnFkFormatDatetimeTypeId = 0;
        mnFkFormatTimeTypeId = 0;
        mnFkBalanceAccountsDebitTypeId = 0;
        mnFkBalanceAccountsCreditTypeId = 0;
        mnFkPurchasesRelationEstimateOrderTypeId = 0;
        mnFkPurchasesRelationEstimateDocTypeId = 0;
        mnFkPurchasesRelationOrderDocTypeId = 0;
        mnFkSalesRelationEstimateOrderTypeId = 0;
        mnFkSalesRelationEstimateDocTypeId = 0;
        mnFkSalesRelationOrderDocTypeId = 0;
        mnFkDbmsTypeId = 0;
        mnFkCountryId = 0;
        mnFkSortingLocalityTypeId = 0;
        mnFkSortingSupplierTypeId = 0;
        mnFkSortingCustomerTypeId = 0;
        mnFkSortingCreditorTypeId = 0;
        mnFkSortingDebtorTypeId = 0;
        mnFkSortingEmployeeTypeId = 0;
        mnFkSortingItemTypeId = 0;
        mnFkSortingDpsSupplierTypeId = 0;
        mnFkSortingDpsCustomerTypeId = 0;
        mnFkBizPartnerAnnulmentId = 0;
        mnFkTaxRegionId_n = 0;
        mnFkSupplierCategoryId_n = 0;
        mnFkSupplierTypeId_n = 0;
        mnFkCustomerCategoryId_n = 0;
        mnFkCustomerTypeId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        moDbmsDataCurrency = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.cfg_param_erp WHERE id_erp = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkErpId = resultSet.getInt("id_erp");
                msErp = resultSet.getString("erp");
                mnDecimalsValue = resultSet.getInt("decs_val");
                mnDecimalsValueUnitary = resultSet.getInt("decs_val_u");
                mnDecimalsExchangeRate = resultSet.getInt("decs_exc_rate");
                mnDecimalsPercentage = resultSet.getInt("decs_per");
                mnDecimalsDiscount = resultSet.getInt("decs_disc");
                mbIsKeyLocalityApplying = resultSet.getBoolean("b_key_loc");
                mbIsKeySupplierApplying = resultSet.getBoolean("b_key_sup");
                mbIsKeyCustomerApplying = resultSet.getBoolean("b_key_cus");
                mbIsKeyCreditorApplying = resultSet.getBoolean("b_key_cdr");
                mbIsKeyDebtorApplying = resultSet.getBoolean("b_key_dbr");
                mbIsKeyEmployeeApplying = resultSet.getBoolean("b_key_emp");
                msFormatKeyLocality = resultSet.getString("fmt_key_loc");
                msFormatKeyCounty = resultSet.getString("fmt_key_cty");
                msFormatKeyState = resultSet.getString("fmt_key_ste");
                msFormatKeyCountry = resultSet.getString("fmt_key_cty");
                msFormatKeySupplier = resultSet.getString("fmt_key_sup");
                msFormatKeyCustomer = resultSet.getString("fmt_key_cus");
                msFormatKeyCreditor = resultSet.getString("fmt_key_cdr");
                msFormatKeyDebtor = resultSet.getString("fmt_key_dbr");
                msFormatKeyEmployee = resultSet.getString("fmt_key_emp");
                msLocaleId = resultSet.getString("loc_id");
                mnDeepAccounts = resultSet.getInt("deep_acc");
                mnDeepCostCenters = resultSet.getInt("deep_cc");
                msFormatAccountId = resultSet.getString("fmt_id_acc");
                msFormatCostCenterId = resultSet.getString("fmt_id_cc");
                mdSupplierCreditLimit = resultSet.getDouble("sup_cred_lim");
                mnSupplierDaysOfCredit = resultSet.getInt("sup_days_cred");
                mnSupplierDaysOfGrace = resultSet.getInt("sup_days_grace");
                mnSupplierDaysOfDocLapsing_n = resultSet.getInt("sup_days_doc_lapsing_n");
                if (resultSet.wasNull()) mnSupplierDaysOfDocLapsing_n = -1;
                mdCustomerCreditLimit = resultSet.getDouble("cus_cred_lim");
                mnCustomerDaysOfCredit = resultSet.getInt("cus_days_cred");
                mnCustomerDaysOfGrace = resultSet.getInt("cus_days_grace");
                mnCustomerDaysOfDocLapsing_n = resultSet.getInt("cus_days_doc_lapsing_n");
                if (resultSet.wasNull()) mnCustomerDaysOfDocLapsing_n = -1;
                mnTaxModel = resultSet.getInt("tax_model");
                mnLotModel = resultSet.getInt("lot_model");
                mbIsItemShortApplying = resultSet.getBoolean("b_item_short");
                mbIsItemNameWithVarieties = resultSet.getBoolean("b_item_name_var");
                mbIsItemNameEditable = resultSet.getBoolean("b_item_name_edit");
                mbIsItemKeyApplying = resultSet.getBoolean("b_item_key");
                mbIsItemKeyAutomatic = resultSet.getBoolean("b_item_key_aut");
                mbIsItemKeyWithVarieties = resultSet.getBoolean("b_item_key_var");
                mbIsItemKeyEditable = resultSet.getBoolean("b_item_key_edit");
                mbIsInventoriable = resultSet.getBoolean("b_inv");
                mbIsLotApplying = resultSet.getBoolean("b_lot");
                mbIsBulk = resultSet.getBoolean("b_bulk");
                mbIsUnitsContainedApplying = resultSet.getBoolean("b_units_cont");
                mbIsUnitsVirtualApplying = resultSet.getBoolean("b_units_virt");
                mbIsUnitsPackageApplying = resultSet.getBoolean("b_units_pack");
                mbIsNetContentApplying = resultSet.getBoolean("b_net_cont");
                mbIsNetContentUnitaryApplying = resultSet.getBoolean("b_net_cont_u");
                mbIsNetContentVariable = resultSet.getBoolean("b_net_cont_variable");
                mbIsLengthApplying = resultSet.getBoolean("b_len");
                mbIsLengthUnitaryApplying = resultSet.getBoolean("b_len_u");
                mbIsLengthVariable = resultSet.getBoolean("b_len_variable");
                mbIsSurfaceApplying = resultSet.getBoolean("b_surf");
                mbIsSurfaceUnitaryApplying = resultSet.getBoolean("b_surf_u");
                mbIsSurfaceVariable = resultSet.getBoolean("b_surf_variable");
                mbIsVolumeApplying = resultSet.getBoolean("b_vol");
                mbIsVolumeUnitaryApplying = resultSet.getBoolean("b_vol_u");
                mbIsVolumeVariable = resultSet.getBoolean("b_vol_variable");
                mbIsMassApplying = resultSet.getBoolean("b_mass");
                mbIsMassUnitaryApplying = resultSet.getBoolean("b_mass_u");
                mbIsMassVariable = resultSet.getBoolean("b_mass_variable");
                mbIsWeightGrossApplying = resultSet.getBoolean("b_weight_gross");
                mbIsWeightDeliveryApplying = resultSet.getBoolean("b_weight_delivery");
                mbIsFreePrice = resultSet.getBoolean("b_free_price");
                mbIsFreeDiscount = resultSet.getBoolean("b_free_disc");
                mbIsFreeDiscountUnitary = resultSet.getBoolean("b_free_disc_u");
                mbIsFreeDiscountEntry = resultSet.getBoolean("b_free_disc_ety");
                mbIsFreeDiscountDoc = resultSet.getBoolean("b_free_disc_doc");
                mbIsFreeCommissions = resultSet.getBoolean("b_free_comms");
                mnDecimalsQuantity = resultSet.getInt("decs_qty");
                mnDecimalsUnitsContained = resultSet.getInt("decs_units_cont");
                mnDecimalsUnitsVirtual = resultSet.getInt("decs_units_virt");
                mnDecimalsNetContent = resultSet.getInt("decs_net_cont");
                mnDecimalsLength = resultSet.getInt("decs_len");
                mnDecimalsSurface = resultSet.getInt("decs_surf");
                mnDecimalsVolume = resultSet.getInt("decs_vol");
                mnDecimalsMass = resultSet.getInt("decs_mass");
                mnDecimalsWeigthGross = resultSet.getInt("decs_weight_gross");
                mnDecimalsWeightDelivery = resultSet.getInt("decs_weight_delivery");
                mbIsPurchasesLinkPeriod = resultSet.getBoolean("b_pur_link_per");
                mbIsPurchasesWarehouseMultiple = resultSet.getBoolean("b_pur_wh_mult");
                mbIsPurchasesAdjustmentMultiple = resultSet.getBoolean("b_pur_adj_mult");
                mbIsPurchasesCreditContract = resultSet.getBoolean("b_pur_cred_con");
                mbIsPurchasesCreditOrder = resultSet.getBoolean("b_pur_cred_ord");
                mbIsPurchasesCreditInvoice = resultSet.getBoolean("b_pur_cred_inv");
                mbIsSalesLinkPeriod = resultSet.getBoolean("b_sal_link_per");
                mbIsSalesWarehouseMultiple = resultSet.getBoolean("b_sal_wh_mult");
                mbIsSalesAdjustmentMultiple = resultSet.getBoolean("b_sal_adj_mult");
                mbIsSalesCreditContract = resultSet.getBoolean("b_sal_cred_con");
                mbIsSalesCreditOrder = resultSet.getBoolean("b_sal_cred_ord");
                mbIsSalesCreditInvoice = resultSet.getBoolean("b_sal_cred_inv");
                mbIsRecordConceptCopyEnabled = resultSet.getBoolean("b_copy_rec_concept");
                mnExclusiveAccessCatalogues = resultSet.getInt("exclusive_access_cats");
                mnExclusiveAccessDocsPurchases = resultSet.getInt("exclusive_access_docs_pur");
                mnExclusiveAccessDocsSales = resultSet.getInt("exclusive_access_docs_sal");
                mnExclusiveAccessPrices = resultSet.getInt("exclusive_access_prices");
                mnExclusiveAccessComissions = resultSet.getInt("exclusive_access_comms");
                mnVersion = resultSet.getInt("ver");
                mtVersionTs = resultSet.getTimestamp("ts_ver");
                mbIsCanEdit = resultSet.getBoolean("b_can_edit");
                mbIsCanDelete = resultSet.getBoolean("b_can_del");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkLanguageId = resultSet.getInt("fid_lan");
                mnFkCurrencyId = resultSet.getInt("fid_cur");
                mnFkFormatDateTypeId = resultSet.getInt("fid_tp_fmt_d");
                mnFkFormatDatetimeTypeId = resultSet.getInt("fid_tp_fmt_dt");
                mnFkFormatTimeTypeId = resultSet.getInt("fid_tp_fmt_t");
                mnFkBalanceAccountsDebitTypeId = resultSet.getInt("fid_tp_bal_acc_debit");
                mnFkBalanceAccountsCreditTypeId = resultSet.getInt("fid_tp_bal_acc_credit");
                mnFkPurchasesRelationEstimateOrderTypeId = resultSet.getInt("fid_pur_tp_rel_est_ord");
                mnFkPurchasesRelationEstimateDocTypeId = resultSet.getInt("fid_pur_tp_rel_est_doc");
                mnFkPurchasesRelationOrderDocTypeId = resultSet.getInt("fid_pur_tp_rel_ord_doc");
                mnFkSalesRelationEstimateOrderTypeId = resultSet.getInt("fid_sal_tp_rel_est_ord");
                mnFkSalesRelationEstimateDocTypeId = resultSet.getInt("fid_sal_tp_rel_est_doc");
                mnFkSalesRelationOrderDocTypeId = resultSet.getInt("fid_sal_tp_rel_ord_doc");
                mnFkDbmsTypeId = resultSet.getInt("fid_tp_dbms");
                mnFkCountryId = resultSet.getInt("fid_cty");
                mnFkSortingLocalityTypeId = resultSet.getInt("fid_tp_sort_loc");
                mnFkSortingSupplierTypeId = resultSet.getInt("fid_tp_sort_sup");
                mnFkSortingCustomerTypeId = resultSet.getInt("fid_tp_sort_cus");
                mnFkSortingCreditorTypeId = resultSet.getInt("fid_tp_sort_cdr");
                mnFkSortingDebtorTypeId = resultSet.getInt("fid_tp_sort_dbr");
                mnFkSortingEmployeeTypeId = resultSet.getInt("fid_tp_sort_emp");
                mnFkSortingItemTypeId = resultSet.getInt("fid_tp_sort_item");
                mnFkSortingDpsSupplierTypeId = resultSet.getInt("fid_tp_sort_dps_sup");
                mnFkSortingDpsCustomerTypeId = resultSet.getInt("fid_tp_sort_dps_cus");
                mnFkBizPartnerAnnulmentId = resultSet.getInt("fid_bp_annul");
                mnFkTaxRegionId_n = resultSet.getInt("fid_tax_reg_n");
                mnFkSupplierCategoryId_n = resultSet.getInt("fid_sup_ct_bp_n");
                mnFkSupplierTypeId_n = resultSet.getInt("fid_sup_tp_bp_n");
                mnFkCustomerCategoryId_n = resultSet.getInt("fid_cus_ct_bp_n");
                mnFkCustomerTypeId_n = resultSet.getInt("fid_cus_tp_bp_n");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                // Read dependant registries:

                moDbmsDataCurrency = new SDataCurrency();
                if (moDbmsDataCurrency.read(new int[] { mnFkCurrencyId }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mbIsRegistryNew = false;
                    mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
                }
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
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL erp.cfg_param_erp_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkErpId);
            callableStatement.setString(nParam++, msErp);
            callableStatement.setInt(nParam++, mnDecimalsValue);
            callableStatement.setInt(nParam++, mnDecimalsValueUnitary);
            callableStatement.setInt(nParam++, mnDecimalsExchangeRate);
            callableStatement.setInt(nParam++, mnDecimalsPercentage);
            callableStatement.setInt(nParam++, mnDecimalsDiscount);
            callableStatement.setBoolean(nParam++, mbIsKeyLocalityApplying);
            callableStatement.setBoolean(nParam++, mbIsKeySupplierApplying);
            callableStatement.setBoolean(nParam++, mbIsKeyCustomerApplying);
            callableStatement.setBoolean(nParam++, mbIsKeyCreditorApplying);
            callableStatement.setBoolean(nParam++, mbIsKeyDebtorApplying);
            callableStatement.setBoolean(nParam++, mbIsKeyEmployeeApplying);
            callableStatement.setString(nParam++, msFormatKeyLocality);
            callableStatement.setString(nParam++, msFormatKeyCounty);
            callableStatement.setString(nParam++, msFormatKeyState);
            callableStatement.setString(nParam++, msFormatKeyCountry);
            callableStatement.setString(nParam++, msFormatKeySupplier);
            callableStatement.setString(nParam++, msFormatKeyCustomer);
            callableStatement.setString(nParam++, msFormatKeyCreditor);
            callableStatement.setString(nParam++, msFormatKeyDebtor);
            callableStatement.setString(nParam++, msFormatKeyEmployee);
            callableStatement.setString(nParam++, msLocaleId);
            callableStatement.setInt(nParam++, mnDeepAccounts);
            callableStatement.setInt(nParam++, mnDeepCostCenters);
            callableStatement.setString(nParam++, msFormatAccountId);
            callableStatement.setString(nParam++, msFormatCostCenterId);
            callableStatement.setDouble(nParam++, mdSupplierCreditLimit);
            callableStatement.setInt(nParam++, mnSupplierDaysOfCredit);
            callableStatement.setInt(nParam++, mnSupplierDaysOfGrace);
            if (mnSupplierDaysOfDocLapsing_n >= 0) callableStatement.setInt(nParam++, mnSupplierDaysOfDocLapsing_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            callableStatement.setDouble(nParam++, mdCustomerCreditLimit);
            callableStatement.setInt(nParam++, mnCustomerDaysOfCredit);
            callableStatement.setInt(nParam++, mnCustomerDaysOfGrace);
            if (mnCustomerDaysOfDocLapsing_n >= 0) callableStatement.setInt(nParam++, mnCustomerDaysOfDocLapsing_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            callableStatement.setInt(nParam++, mnTaxModel);
            callableStatement.setInt(nParam++, mnLotModel);
            callableStatement.setBoolean(nParam++, mbIsItemShortApplying);
            callableStatement.setBoolean(nParam++, mbIsItemNameWithVarieties);
            callableStatement.setBoolean(nParam++, mbIsItemNameEditable);
            callableStatement.setBoolean(nParam++, mbIsItemKeyApplying);
            callableStatement.setBoolean(nParam++, mbIsItemKeyAutomatic);
            callableStatement.setBoolean(nParam++, mbIsItemKeyWithVarieties);
            callableStatement.setBoolean(nParam++, mbIsItemKeyEditable);
            callableStatement.setBoolean(nParam++, mbIsInventoriable);
            callableStatement.setBoolean(nParam++, mbIsLotApplying);
            callableStatement.setBoolean(nParam++, mbIsBulk);
            callableStatement.setBoolean(nParam++, mbIsUnitsContainedApplying);
            callableStatement.setBoolean(nParam++, mbIsUnitsVirtualApplying);
            callableStatement.setBoolean(nParam++, mbIsUnitsPackageApplying);
            callableStatement.setBoolean(nParam++, mbIsNetContentApplying);
            callableStatement.setBoolean(nParam++, mbIsNetContentUnitaryApplying);
            callableStatement.setBoolean(nParam++, mbIsNetContentVariable);
            callableStatement.setBoolean(nParam++, mbIsLengthApplying);
            callableStatement.setBoolean(nParam++, mbIsLengthUnitaryApplying);
            callableStatement.setBoolean(nParam++, mbIsLengthVariable);
            callableStatement.setBoolean(nParam++, mbIsSurfaceApplying);
            callableStatement.setBoolean(nParam++, mbIsSurfaceUnitaryApplying);
            callableStatement.setBoolean(nParam++, mbIsSurfaceVariable);
            callableStatement.setBoolean(nParam++, mbIsVolumeApplying);
            callableStatement.setBoolean(nParam++, mbIsVolumeUnitaryApplying);
            callableStatement.setBoolean(nParam++, mbIsVolumeVariable);
            callableStatement.setBoolean(nParam++, mbIsMassApplying);
            callableStatement.setBoolean(nParam++, mbIsMassUnitaryApplying);
            callableStatement.setBoolean(nParam++, mbIsMassVariable);
            callableStatement.setBoolean(nParam++, mbIsWeightGrossApplying);
            callableStatement.setBoolean(nParam++, mbIsWeightDeliveryApplying);
            callableStatement.setBoolean(nParam++, mbIsFreePrice);
            callableStatement.setBoolean(nParam++, mbIsFreeDiscount);
            callableStatement.setBoolean(nParam++, mbIsFreeDiscountUnitary);
            callableStatement.setBoolean(nParam++, mbIsFreeDiscountEntry);
            callableStatement.setBoolean(nParam++, mbIsFreeDiscountDoc);
            callableStatement.setBoolean(nParam++, mbIsFreeCommissions);
            callableStatement.setInt(nParam++, mnDecimalsQuantity);
            callableStatement.setInt(nParam++, mnDecimalsUnitsContained);
            callableStatement.setInt(nParam++, mnDecimalsUnitsVirtual);
            callableStatement.setInt(nParam++, mnDecimalsNetContent);
            callableStatement.setInt(nParam++, mnDecimalsLength);
            callableStatement.setInt(nParam++, mnDecimalsSurface);
            callableStatement.setInt(nParam++, mnDecimalsVolume);
            callableStatement.setInt(nParam++, mnDecimalsMass);
            callableStatement.setInt(nParam++, mnDecimalsWeigthGross);
            callableStatement.setInt(nParam++, mnDecimalsWeightDelivery);
            callableStatement.setBoolean(nParam++, mbIsPurchasesLinkPeriod);
            callableStatement.setBoolean(nParam++, mbIsPurchasesWarehouseMultiple);
            callableStatement.setBoolean(nParam++, mbIsPurchasesAdjustmentMultiple);
            callableStatement.setBoolean(nParam++, mbIsPurchasesCreditContract);
            callableStatement.setBoolean(nParam++, mbIsPurchasesCreditOrder);
            callableStatement.setBoolean(nParam++, mbIsPurchasesCreditInvoice);
            callableStatement.setBoolean(nParam++, mbIsSalesLinkPeriod);
            callableStatement.setBoolean(nParam++, mbIsSalesWarehouseMultiple);
            callableStatement.setBoolean(nParam++, mbIsSalesAdjustmentMultiple);
            callableStatement.setBoolean(nParam++, mbIsSalesCreditContract);
            callableStatement.setBoolean(nParam++, mbIsSalesCreditOrder);
            callableStatement.setBoolean(nParam++, mbIsSalesCreditInvoice);
            callableStatement.setBoolean(nParam++, mbIsRecordConceptCopyEnabled);
            callableStatement.setInt(nParam++, mnExclusiveAccessCatalogues);
            callableStatement.setInt(nParam++, mnExclusiveAccessDocsPurchases);
            callableStatement.setInt(nParam++, mnExclusiveAccessDocsSales);
            callableStatement.setInt(nParam++, mnExclusiveAccessPrices);
            callableStatement.setInt(nParam++, mnExclusiveAccessComissions);
            callableStatement.setInt(nParam++, mnVersion);
            callableStatement.setTimestamp(nParam++, new java.sql.Timestamp(mtVersionTs.getTime()));
            callableStatement.setBoolean(nParam++, mbIsCanEdit);
            callableStatement.setBoolean(nParam++, mbIsCanDelete);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkLanguageId);
            callableStatement.setInt(nParam++, mnFkCurrencyId);
            callableStatement.setInt(nParam++, mnFkFormatDateTypeId);
            callableStatement.setInt(nParam++, mnFkFormatDatetimeTypeId);
            callableStatement.setInt(nParam++, mnFkFormatTimeTypeId);
            callableStatement.setInt(nParam++, mnFkBalanceAccountsDebitTypeId);
            callableStatement.setInt(nParam++, mnFkBalanceAccountsCreditTypeId);
            callableStatement.setInt(nParam++, mnFkPurchasesRelationEstimateOrderTypeId);
            callableStatement.setInt(nParam++, mnFkPurchasesRelationEstimateDocTypeId);
            callableStatement.setInt(nParam++, mnFkPurchasesRelationOrderDocTypeId);
            callableStatement.setInt(nParam++, mnFkSalesRelationEstimateOrderTypeId);
            callableStatement.setInt(nParam++, mnFkSalesRelationEstimateDocTypeId);
            callableStatement.setInt(nParam++, mnFkSalesRelationOrderDocTypeId);
            callableStatement.setInt(nParam++, mnFkDbmsTypeId);
            callableStatement.setInt(nParam++, mnFkCountryId);
            callableStatement.setInt(nParam++, mnFkSortingLocalityTypeId);
            callableStatement.setInt(nParam++, mnFkSortingSupplierTypeId);
            callableStatement.setInt(nParam++, mnFkSortingCustomerTypeId);
            callableStatement.setInt(nParam++, mnFkSortingCreditorTypeId);
            callableStatement.setInt(nParam++, mnFkSortingDebtorTypeId);
            callableStatement.setInt(nParam++, mnFkSortingEmployeeTypeId);
            callableStatement.setInt(nParam++, mnFkSortingItemTypeId);
            callableStatement.setInt(nParam++, mnFkSortingDpsSupplierTypeId);
            callableStatement.setInt(nParam++, mnFkSortingDpsCustomerTypeId);
            callableStatement.setInt(nParam++, mnFkBizPartnerAnnulmentId);
            if (mnFkTaxRegionId_n > 0) callableStatement.setInt(nParam++, mnFkTaxRegionId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkSupplierCategoryId_n > 0) callableStatement.setInt(nParam++, mnFkSupplierCategoryId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkSupplierTypeId_n > 0 ) callableStatement.setInt(nParam++, mnFkSupplierTypeId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkCustomerCategoryId_n > 0) callableStatement.setInt(nParam++, mnFkCustomerCategoryId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkCustomerTypeId_n > 0) callableStatement.setInt(nParam++, mnFkCustomerTypeId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
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

    @Override
    public int getSystemId() {
        return getPkErpId();
    }
}
