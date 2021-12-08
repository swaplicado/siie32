/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mcfg.data;

import erp.cfd.SCceEmisorAddressAux;
import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Statement;
import sa.lib.gui.SGuiConfigCompany;

/**
 *
 * @author Sergio Flores, Uriel Castañeda
 */
public class SDataParamsCompany extends erp.lib.data.SDataRegistry implements java.io.Serializable, SGuiConfigCompany {
    
    public static final String FILES_DIR = "files";
    
    public static final int PRICE_POLICY_NOT_RESTRICTED = 0;
    public static final int PRICE_POLICY_PRICE_REQUIRED = 1;

    protected int mnPkConfigCoId;
    protected int mnDaysOfGraceSupplier;
    protected int mnDaysOfGraceCustomer;
    //protected java.sql.Blob moLogo_n;             // com.msql.jdbc.Blob is not serializable!
    protected int mnMaskAccount;
    protected int mnMaskCostCenter;
    protected java.lang.String msXmlBaseDirectory;
    protected java.lang.String msImagesDirectory;
    protected java.lang.String msFormerSystemOdbc;
    protected java.lang.String msFormerSystemOdbcUser;
    protected java.lang.String msFormerSystemOdbcUserPassword;
    protected java.lang.String msFiscalSettings;
    protected java.lang.String msNotesPurchasesOrder;
    protected java.lang.String msPaymentEmail;
    protected java.lang.String msPaymentNumberSeries;
    protected java.lang.String msRegistrySs;
    protected double mdInterestDelayRate;
    protected int mnPricePolicyForPurchases;
    protected int mnPricePolicyForSales;
    protected int mnInventoryValuationMethod;
    protected int mnVersion;
    protected java.util.Date mtVersionTs;
    protected boolean mbIsAuthorizationPurchasesOrderAutomatic;
    protected boolean mbIsAuthorizationPurchasesDocAutomatic;
    protected boolean mbIsAuthorizationSalesOrderAutomatic;
    protected boolean mbIsAuthorizationSalesDocAutomatic;
    protected boolean mbIsLogisticsOrderAutomatic;
    protected boolean mbIsLastSearchItemPreserved;
    protected boolean mbIsExchangeRatePurPreserved;
    protected boolean mbIsExchangeRateSalPreserved;
    protected boolean mbIsLotApprovalRequired;
    protected boolean mbIsPaymentMonday;
    protected boolean mbIsPaymentTuesday;
    protected boolean mbIsPaymentWednesday;
    protected boolean mbIsPaymentThursday;
    protected boolean mbIsPaymentFriday;
    protected boolean mbIsPaymentSaturday;
    protected boolean mbIsPaymentSunday;
    protected boolean mbIsCfdiProduction;
    protected boolean mbIsCfdiSendingAutomaticSal;
    protected boolean mbIsCfdiSendingAutomaticBol;
    protected boolean mbIsCfdiSendingAutomaticHrs;
    protected boolean mbIsFunctionalAreas;
    protected boolean mbIsReportsBackground;
    protected boolean mbIsCanEdit;
    protected boolean mbIsCanDelete;
    protected boolean mbIsDeleted;
    protected int mnFkDefaultBookkeepingCenterId_n;
    protected int mnFkDefaultTaxRegionId_n;
    protected int mnFkDefaultAddressFormatTypeId_n;
    protected int mnFkDefaultCustomerTypeId_n;
    protected int mnFkDefaultMarketSegmentId_n;
    protected int mnFkDefaultMarketSubsegmentId_n;
    protected int mnFkDefaultDistributionChannelId_n;
    protected int mnFkDefaultSalesRouteId_n;
    protected int mnFkItemDifferenceExpenseId_n;
    protected int mnFkItemDifferenceIncomeId_n;
    protected int mnFkItemPrepaymentPurId_n;
    protected int mnFkItemPrepaymentSalId_n;
    protected int mnFkItemLogisticsDeliveryId_n;
    protected int mnFkItemLogisticsStayId_n;
    protected int mnFkItemLogisticsManeuverId_n;
    protected int mnFkItemLogisticsInsuranceId_n;
    protected int mnFkItemLogisticsCustomsExpId_n;
    protected int mnFkItemLogisticsCustomsImpId_n;
    protected int mnFkItemLogisticsTariffId_n;
    protected int mnFkItemLogisticsOtherId_n;
    protected int mnFkDocNumberSeriesLogisticsId_n;
    protected int mnFkDpsNatureLogisticsId;
    protected java.lang.String msFkAccountDifferenceExpenseId_n;
    protected java.lang.String msFkAccountDifferenceIncomeId_n;
    protected java.lang.String msFkCostCenterDifferenceExpenseId_n;
    protected java.lang.String msFkCostCenterDifferenceIncomeId_n;
    protected java.lang.String msFkCfdPaymentAccountExpensesId_n;
    protected java.lang.String msFkCfdPaymentCostCenterExpensesId_n;
    protected int mnFkCfdPaymentItemBankInterestId_n;
    protected int mnFkCfdPaymentItemBankFeeId_n;
    protected int mnFkCfdPaymentBankFeeTaxBasicId_n;
    protected int mnFkCfdPaymentBankFeeTaxId_n;
    protected int mnFkBasicTaxCharged01Id_n;
    protected int mnFkBasicTaxCharged02Id_n;
    protected int mnFkBasicTaxRetained01Id_n;
    protected int mnFkBasicTaxRetained02Id_n;
    protected int mnFkMfgCostUnitTypeId;
    protected int mnFkCertificateId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsDocNumberSeriesLogistics;

    protected javax.swing.ImageIcon moExtraLogoImageIcon_n;
    protected erp.mcfg.data.SDataCertificate moDbmsCertificate_n;
    protected erp.mcfg.data.SDataCfgCfd moDbmsDataCfgCfd;

    public SDataParamsCompany() {
        super(SDataConstants.CFG_PARAM_CO);
        reset();
    }

    public void setPkConfigCoId(int n) { mnPkConfigCoId = n; }
    public void setDaysOfGraceSupplier(int n) { mnDaysOfGraceSupplier = n; }
    public void setDaysOfGraceCustomer(int n) { mnDaysOfGraceCustomer = n; }
    //public void setLogo_n(java.sql.Blob o) { moLogo_n = o; }
    public void setMaskAccount(int n) { mnMaskAccount = n; }
    public void setMaskCostCenter(int n) { mnMaskCostCenter = n; }
    public void setXmlBaseDirectory(java.lang.String s) { msXmlBaseDirectory = s; }
    public void setImagesDirectory(java.lang.String s) { msImagesDirectory = s; }
    public void setFormerSystemOdbc(java.lang.String s) { msFormerSystemOdbc = s; }
    public void setFormerSystemOdbcUser(java.lang.String s) { msFormerSystemOdbcUser = s; }
    public void setFormerSystemOdbcUserPassword(java.lang.String s) { msFormerSystemOdbcUserPassword = s; }
    public void setFiscalSettings(java.lang.String s) { msFiscalSettings = s; }
    public void setNotesPurchasesOrder(java.lang.String s) { msNotesPurchasesOrder = s; }
    public void setPaymentEmail(java.lang.String s) { msPaymentEmail = s; }
    public void setPaymentNumberSeries(java.lang.String s) { msPaymentNumberSeries = s; }
    public void setRegistrySs(java.lang.String s) { msRegistrySs = s; }
    public void setInterestDelayRate(double d) { mdInterestDelayRate = d; }
    public void setPricePolicyForPurchases(int n) { mnPricePolicyForPurchases = n; }
    public void setPricePolicyForSales(int n) { mnPricePolicyForSales = n; }
    public void setInventoryValuationMethod(int n) { mnInventoryValuationMethod = n; }
    public void setVersion(int n) { mnVersion = n; }
    public void setVersionTs(java.util.Date t) { mtVersionTs = t; }
    public void setIsAuthorizationPurchasesOrderAutomatic(boolean b) { mbIsAuthorizationPurchasesOrderAutomatic = b; }
    public void setIsAuthorizationPurchasesDocAutomatic(boolean b) { mbIsAuthorizationPurchasesDocAutomatic = b; }
    public void setIsAuthorizationSalesOrderAutomatic(boolean b) { mbIsAuthorizationSalesOrderAutomatic = b; }
    public void setIsAuthorizationSalesDocAutomatic(boolean b) { mbIsAuthorizationSalesDocAutomatic = b; }
    public void setIsLogisticsOrderAutomatic(boolean b) { mbIsLogisticsOrderAutomatic = b; }
    public void setIsLastSearchItemPreserved(boolean b) { mbIsLastSearchItemPreserved = b; }
    public void setIsExchangeRatePurPreserved(boolean b) { mbIsExchangeRatePurPreserved = b; }
    public void setIsExchangeRateSalPreserved(boolean b) { mbIsExchangeRateSalPreserved = b; }
    public void setIsLotApprovalRequired(boolean b) { mbIsLotApprovalRequired = b; }
    public void setIsPaymentMonday(boolean b) { mbIsPaymentMonday = b; }
    public void setIsPaymentTuesday(boolean b) { mbIsPaymentTuesday = b; }
    public void setIsPaymentWednesday(boolean b) { mbIsPaymentWednesday = b; }
    public void setIsPaymentThursday(boolean b) { mbIsPaymentThursday = b; }
    public void setIsPaymentFriday(boolean b) { mbIsPaymentFriday = b; }
    public void setIsPaymentSaturday(boolean b) { mbIsPaymentSaturday = b; }
    public void setIsPaymentSunday(boolean b) { mbIsPaymentSunday = b; }
    public void setIsCfdiProduction(boolean b) { mbIsCfdiProduction = b; }
    public void setIsCfdiSendingAutomaticSal(boolean b) { mbIsCfdiSendingAutomaticSal = b; }
    public void setIsCfdiSendingAutomaticBol(boolean b) { mbIsCfdiSendingAutomaticBol = b; }
    public void setIsCfdiSendingAutomaticHrs(boolean b) { mbIsCfdiSendingAutomaticHrs = b; }
    public void setIsFunctionalAreas(boolean b) { mbIsFunctionalAreas = b; }
    public void setIsReportsBackground(boolean b) { mbIsReportsBackground = b; }
    public void setIsCanEdit(boolean b) { mbIsCanEdit = b; }
    public void setIsCanDelete(boolean b) { mbIsCanDelete = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkDefaultBookkeepingCenterId_n(int n) { mnFkDefaultBookkeepingCenterId_n = n; }
    public void setFkDefaultTaxRegionId_n(int n) { mnFkDefaultTaxRegionId_n = n; }
    public void setFkDefaultAddressFormatTypeId_n(int n) { mnFkDefaultAddressFormatTypeId_n = n; }
    public void setFkDefaultCustomerTypeId_n(int n) { mnFkDefaultCustomerTypeId_n = n; }
    public void setFkDefaultMarketSegmentId_n(int n) { mnFkDefaultMarketSegmentId_n = n; }
    public void setFkDefaultMarketSubsegmentId_n(int n) { mnFkDefaultMarketSubsegmentId_n = n; }
    public void setFkDefaultDistributionChannelId_n(int n) { mnFkDefaultDistributionChannelId_n = n; }
    public void setFkDefaultSalesRouteId_n(int n) { mnFkDefaultSalesRouteId_n = n; }
    public void setFkItemDifferenceExpenseId_n(int n) { mnFkItemDifferenceExpenseId_n = n; }
    public void setFkItemDifferenceIncomeId_n(int n) { mnFkItemDifferenceIncomeId_n = n; }
    public void setFkItemPrepaymentPurId_n(int n) { mnFkItemPrepaymentPurId_n = n; }
    public void setFkItemPrepaymentSalId_n(int n) { mnFkItemPrepaymentSalId_n = n; }
    public void setFkItemLogisticsDeliveryId_n(int n) { mnFkItemLogisticsDeliveryId_n = n; }
    public void setFkItemLogisticsStayId_n(int n) { mnFkItemLogisticsStayId_n = n; }
    public void setFkItemLogisticsManeuverId_n(int n) { mnFkItemLogisticsManeuverId_n = n; }
    public void setFkItemLogisticsInsuranceId_n(int n) { mnFkItemLogisticsInsuranceId_n = n; }
    public void setFkItemLogisticsCustomsExpId_n(int n) { mnFkItemLogisticsCustomsExpId_n = n; }
    public void setFkItemLogisticsCustomsImpId_n(int n) { mnFkItemLogisticsCustomsImpId_n = n; }
    public void setFkItemLogisticsTariffId_n(int n) { mnFkItemLogisticsTariffId_n = n; }
    public void setFkItemLogisticsOtherId_n(int n) { mnFkItemLogisticsOtherId_n = n; }
    public void setFkDocNumberSeriesLogisticsId_n(int n) { mnFkDocNumberSeriesLogisticsId_n = n; }
    public void setFkDpsNatureLogisticsId(int n) { mnFkDpsNatureLogisticsId = n; }
    public void setFkAccountDifferenceExpenseId_n(java.lang.String s) { msFkAccountDifferenceExpenseId_n = s; }
    public void setFkAccountDifferenceIncomeId_n(java.lang.String s) { msFkAccountDifferenceIncomeId_n = s; }
    public void setFkCostCenterDifferenceExpenseId_n(java.lang.String s) { msFkCostCenterDifferenceExpenseId_n = s; }
    public void setFkCostCenterDifferenceIncomeId_n(java.lang.String s) { msFkCostCenterDifferenceIncomeId_n = s; }
    public void setFkCfdPaymentAccountExpensesId_n(java.lang.String s) { msFkCfdPaymentAccountExpensesId_n = s; }
    public void setFkCfdPaymentCostCenterExpensesId_n(java.lang.String s) { msFkCfdPaymentCostCenterExpensesId_n = s; }
    public void setFkCfdPaymentItemBankInterestId_n(int n) { mnFkCfdPaymentItemBankInterestId_n = n; }
    public void setFkCfdPaymentItemBankFeeId_n(int n) { mnFkCfdPaymentItemBankFeeId_n = n; }
    public void setFkCfdPaymentBankFeeTaxBasicId_n(int n) { mnFkCfdPaymentBankFeeTaxBasicId_n = n; }
    public void setFkCfdPaymentBankFeeTaxId_n(int n) { mnFkCfdPaymentBankFeeTaxId_n = n; }
    public void setFkBasicTaxCharged01Id_n(int n) { mnFkBasicTaxCharged01Id_n = n; }
    public void setFkBasicTaxCharged02Id_n(int n) { mnFkBasicTaxCharged02Id_n = n; }
    public void setFkBasicTaxRetained01Id_n(int n) { mnFkBasicTaxRetained01Id_n = n; }
    public void setFkBasicTaxRetained02Id_n(int n) { mnFkBasicTaxRetained02Id_n = n; }
    public void setFkMfgCostUnitTypeId(int n) { mnFkMfgCostUnitTypeId = n; }
    public void setFkCertificateId_n(int n) { mnFkCertificateId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }
    
    public void setDbmsDataCfgCfd(erp.mcfg.data.SDataCfgCfd o) { moDbmsDataCfgCfd = o; }

    public int getPkConfigCoId() { return mnPkConfigCoId; }
    public int getDaysOfGraceSupplier() { return mnDaysOfGraceSupplier; }
    public int getDaysOfGraceCustomer() { return mnDaysOfGraceCustomer; }
    //public java.sql.Blob getLogo_n() { return moLogo_n; }
    public int getMaskAccount() { return mnMaskAccount; }
    public int getMaskCostCenter() { return mnMaskCostCenter; }
    public java.lang.String getXmlBaseDirectory() { return msXmlBaseDirectory; }
    public java.lang.String getImagesDirectory() { return msImagesDirectory; }
    public java.lang.String getFormerSystemOdbc() { return msFormerSystemOdbc; }
    public java.lang.String getFormerSystemOdbcUser() { return msFormerSystemOdbcUser; }
    public java.lang.String getFormerSystemOdbcUserPassword() { return msFormerSystemOdbcUserPassword; }
    public java.lang.String getFiscalSettings() { return msFiscalSettings; }
    public java.lang.String getNotesPurchasesOrder() { return msNotesPurchasesOrder; }
    public java.lang.String getPaymentEmail() { return msPaymentEmail; }
    public java.lang.String getPaymentNumberSeries() { return msPaymentNumberSeries; }
    public java.lang.String getRegistrySs() { return msRegistrySs; }
    public double getInterestDelayRate() { return mdInterestDelayRate; }
    public int getPricePolicyForPurchases() { return mnPricePolicyForPurchases; }
    public int getPricePolicyForSales() { return mnPricePolicyForSales; }
    public int getInventoryValuationMethod() { return mnInventoryValuationMethod; }
    public int getVersion() { return mnVersion; }
    public java.util.Date getVersionTs() { return mtVersionTs; }
    public boolean getIsAuthorizationPurchasesOrderAutomatic() { return mbIsAuthorizationPurchasesOrderAutomatic; }
    public boolean getIsAuthorizationPurchasesDocAutomatic() { return mbIsAuthorizationPurchasesDocAutomatic; }
    public boolean getIsAuthorizationSalesOrderAutomatic() { return mbIsAuthorizationSalesOrderAutomatic; }
    public boolean getIsAuthorizationSalesDocAutomatic() { return mbIsAuthorizationSalesDocAutomatic; }
    public boolean getIsLogisticsOrderAutomatic() { return mbIsLogisticsOrderAutomatic; }
    public boolean getIsLastSearchItemPreserved() { return mbIsLastSearchItemPreserved; }
    public boolean getIsExchangeRatePurPreserved() { return mbIsExchangeRatePurPreserved; }
    public boolean getIsExchangeRateSalPreserved() { return mbIsExchangeRateSalPreserved; }
    public boolean getIsLotApprovalRequired() { return mbIsLotApprovalRequired; }    
    public boolean getIsPaymentMonday() { return mbIsPaymentMonday; }
    public boolean getIsPaymentTuesday() { return mbIsPaymentTuesday; }
    public boolean getIsPaymentWednesday() { return mbIsPaymentWednesday; }
    public boolean getIsPaymentThursday() { return mbIsPaymentThursday; }
    public boolean getIsPaymentFriday() { return mbIsPaymentFriday; }
    public boolean getIsPaymentSaturday() { return mbIsPaymentSaturday; }
    public boolean getIsPaymentSunday() { return mbIsPaymentSunday; }
    public boolean getIsCfdiProduction() { return mbIsCfdiProduction; }
    public boolean getIsCfdiSendingAutomaticSal() { return mbIsCfdiSendingAutomaticSal; }
    public boolean getIsCfdiSendingAutomaticBol() { return mbIsCfdiSendingAutomaticBol; }
    public boolean getIsCfdiSendingAutomaticHrs() { return mbIsCfdiSendingAutomaticHrs; }
    public boolean getIsFunctionalAreas() { return mbIsFunctionalAreas; }
    public boolean getIsReportsBackground() { return mbIsReportsBackground; }
    public boolean getIsCanEdit() { return mbIsCanEdit; }
    public boolean getIsCanDelete() { return mbIsCanDelete; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkDefaultBookkeepingCenterId_n() { return mnFkDefaultBookkeepingCenterId_n; }
    public int getFkDefaultTaxRegionId_n() { return mnFkDefaultTaxRegionId_n; }
    public int getFkDefaultAddressFormatTypeId_n() { return mnFkDefaultAddressFormatTypeId_n; }
    public int getFkDefaultCustomerTypeId_n() { return mnFkDefaultCustomerTypeId_n; }
    public int getFkDefaultMarketSegmentId_n() { return mnFkDefaultMarketSegmentId_n; }
    public int getFkDefaultMarketSubsegmentId_n() { return mnFkDefaultMarketSubsegmentId_n; }
    public int getFkDefaultDistributionChannelId_n() { return mnFkDefaultDistributionChannelId_n; }
    public int getFkDefaultSalesRouteId_n() { return mnFkDefaultSalesRouteId_n; }
    public int getFkItemDifferenceExpenseId_n() { return mnFkItemDifferenceExpenseId_n; }
    public int getFkItemDifferenceIncomeId_n() { return mnFkItemDifferenceIncomeId_n; }
    public int getFkItemPrepaymentPurId_n() { return mnFkItemPrepaymentPurId_n; }
    public int getFkItemPrepaymentSalId_n() { return mnFkItemPrepaymentSalId_n; }
    public int getFkItemLogisticsDeliveryId_n() { return mnFkItemLogisticsDeliveryId_n; }
    public int getFkItemLogisticsStayId_n() { return mnFkItemLogisticsStayId_n; }
    public int getFkItemLogisticsManeuverId_n() { return mnFkItemLogisticsManeuverId_n; }
    public int getFkItemLogisticsInsuranceId_n() { return mnFkItemLogisticsInsuranceId_n; }
    public int getFkItemLogisticsCustomsExpId_n() { return mnFkItemLogisticsCustomsExpId_n; }
    public int getFkItemLogisticsCustomsImpId_n() { return mnFkItemLogisticsCustomsImpId_n; }
    public int getFkItemLogisticsTariffId_n() { return mnFkItemLogisticsTariffId_n; }
    public int getFkItemLogisticsOtherId_n() { return mnFkItemLogisticsOtherId_n; }
    public int getFkDocNumberSeriesLogisticsId_n() { return mnFkDocNumberSeriesLogisticsId_n; }
    public int getFkDpsNatureLogisticsId() { return mnFkDpsNatureLogisticsId; }
    public java.lang.String getFkAccountDifferenceExpenseId_n() { return msFkAccountDifferenceExpenseId_n; }
    public java.lang.String getFkAccountDifferenceIncomeId_n() { return msFkAccountDifferenceIncomeId_n; }
    public java.lang.String getFkCostCenterDifferenceExpenseId_n() { return msFkCostCenterDifferenceExpenseId_n; }
    public java.lang.String getFkCostCenterDifferenceIncomeId_n() { return msFkCostCenterDifferenceIncomeId_n; }
    public java.lang.String getFkCfdPaymentAccountExpensesId_n() { return msFkCfdPaymentAccountExpensesId_n; }
    public java.lang.String getFkCfdPaymentCostCenterExpensesId_n() { return msFkCfdPaymentCostCenterExpensesId_n; }
    public int getFkCfdPaymentItemBankInterestId_n() { return mnFkCfdPaymentItemBankInterestId_n; }
    public int getFkCfdPaymentItemBankFeeId_n() { return mnFkCfdPaymentItemBankFeeId_n; }
    public int getFkCfdPaymentBankFeeTaxBasicId_n() { return mnFkCfdPaymentBankFeeTaxBasicId_n; }
    public int getFkCfdPaymentBankFeeTaxId_n() { return mnFkCfdPaymentBankFeeTaxId_n; }
    public int getFkBasicTaxCharged01Id_n() { return mnFkBasicTaxCharged01Id_n; }
    public int getFkBasicTaxCharged02Id_n() { return mnFkBasicTaxCharged02Id_n; }
    public int getFkBasicTaxRetained01Id_n() { return mnFkBasicTaxRetained01Id_n; }
    public int getFkBasicTaxRetained02Id_n() { return mnFkBasicTaxRetained02Id_n; }
    public int getFkMfgCostUnitTypeId() { return mnFkMfgCostUnitTypeId; }
    public int getFkCertificateId_n() { return mnFkCertificateId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsDocNumberSeriesLogistics(java.lang.String s) { msDbmsDocNumberSeriesLogistics = s; }
    public void setExtraLogoImageIcon_n(javax.swing.ImageIcon o) { moExtraLogoImageIcon_n = o; }
    public void setDbmsDataCertificate_n(erp.mcfg.data.SDataCertificate o) { moDbmsCertificate_n = o; }

    public java.lang.String getDbmsDocNumberSeriesLogistics() { return msDbmsDocNumberSeriesLogistics; }
    public javax.swing.ImageIcon getExtraLogoImageIcon_n() { return moExtraLogoImageIcon_n; }
    public erp.mcfg.data.SDataCertificate getDbmsDataCertificate_n() { return moDbmsCertificate_n; }
    
    public erp.mcfg.data.SDataCfgCfd getDbmsDataCfgCfd() { return moDbmsDataCfgCfd; }
    
    public SCceEmisorAddressAux getEmisorAddress(String zipCode) {
        SCceEmisorAddressAux emisorAddress = null;
        
        for (SCceEmisorAddressAux emisor : moDbmsDataCfgCfd.getCceEmisorAddressAux()) {
            if (emisor.getCfdCceEmisorCodigoPostal().compareTo(zipCode) == 0) {
                emisorAddress = emisor;
                break;
            }
        }
        
        return emisorAddress;
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkConfigCoId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkConfigCoId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkConfigCoId = 0;
        mnDaysOfGraceSupplier = 0;
        mnDaysOfGraceCustomer = 0;
        //moLogo_n = null;
        mnMaskAccount = 0;
        mnMaskCostCenter = 0;
        msXmlBaseDirectory = "";
        msImagesDirectory = "";
        msFormerSystemOdbc = "";
        msFormerSystemOdbcUser = "";
        msFormerSystemOdbcUserPassword = "";
        msFiscalSettings = "";
        msNotesPurchasesOrder = "";
        msPaymentEmail = "";
        msPaymentNumberSeries = "";
        msRegistrySs = "";
        mdInterestDelayRate = 0;
        mnPricePolicyForPurchases = 0;
        mnPricePolicyForSales = 0;
        mnInventoryValuationMethod = 0;
        mnVersion = 0;
        mtVersionTs = null;
        mbIsAuthorizationPurchasesOrderAutomatic = false;
        mbIsAuthorizationPurchasesDocAutomatic = false;
        mbIsAuthorizationSalesOrderAutomatic = false;
        mbIsAuthorizationSalesDocAutomatic = false;
        mbIsLogisticsOrderAutomatic = false;
        mbIsLastSearchItemPreserved = false;
        mbIsExchangeRatePurPreserved = false;
        mbIsExchangeRateSalPreserved = false;
        mbIsLotApprovalRequired = false;
        mbIsPaymentMonday = false;
        mbIsPaymentTuesday = false;
        mbIsPaymentWednesday = false;
        mbIsPaymentThursday = false;
        mbIsPaymentFriday = false;
        mbIsPaymentSaturday = false;
        mbIsPaymentSunday = false;
        mbIsCfdiProduction = false;
        mbIsCfdiSendingAutomaticSal = false;
        mbIsCfdiSendingAutomaticBol = false;
        mbIsCfdiSendingAutomaticHrs = false;
        mbIsFunctionalAreas = false;
        mbIsReportsBackground = false;
        mbIsCanEdit = false;
        mbIsCanDelete = false;
        mbIsDeleted = false;
        mnFkDefaultBookkeepingCenterId_n = 0;
        mnFkDefaultTaxRegionId_n = 0;
        mnFkDefaultAddressFormatTypeId_n = 0;
        mnFkDefaultCustomerTypeId_n = 0;
        mnFkDefaultMarketSegmentId_n = 0;
        mnFkDefaultMarketSubsegmentId_n = 0;
        mnFkDefaultDistributionChannelId_n = 0;
        mnFkDefaultSalesRouteId_n = 0;
        mnFkItemDifferenceExpenseId_n = 0;
        mnFkItemDifferenceIncomeId_n = 0;
        mnFkItemPrepaymentPurId_n = 0;
        mnFkItemPrepaymentSalId_n = 0;
        mnFkItemLogisticsDeliveryId_n = 0;
        mnFkItemLogisticsStayId_n = 0;
        mnFkItemLogisticsManeuverId_n = 0;
        mnFkItemLogisticsInsuranceId_n = 0;
        mnFkItemLogisticsCustomsExpId_n = 0;
        mnFkItemLogisticsCustomsImpId_n = 0;
        mnFkItemLogisticsTariffId_n = 0;
        mnFkItemLogisticsOtherId_n = 0;
        mnFkDocNumberSeriesLogisticsId_n = 0;
        mnFkDpsNatureLogisticsId = 0;
        msFkAccountDifferenceExpenseId_n = "";
        msFkAccountDifferenceIncomeId_n = "";
        msFkCostCenterDifferenceExpenseId_n = "";
        msFkCostCenterDifferenceIncomeId_n = "";
        msFkCfdPaymentAccountExpensesId_n = "";
        msFkCfdPaymentCostCenterExpensesId_n = "";
        mnFkCfdPaymentItemBankInterestId_n = 0;
        mnFkCfdPaymentItemBankFeeId_n = 0;
        mnFkCfdPaymentBankFeeTaxBasicId_n = 0;
        mnFkCfdPaymentBankFeeTaxId_n = 0;
        mnFkBasicTaxCharged01Id_n = 0;
        mnFkBasicTaxCharged02Id_n = 0;
        mnFkBasicTaxRetained01Id_n = 0;
        mnFkBasicTaxRetained02Id_n = 0;
        mnFkMfgCostUnitTypeId = 0;
        mnFkCertificateId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsDocNumberSeriesLogistics = "";
        moExtraLogoImageIcon_n = null;
        moDbmsCertificate_n = null;
        
        moDbmsDataCfgCfd = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;
        Statement oStatementAux = null;
        Blob oLogo_n;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT p.*, COALESCE(t.dns, '') AS f_dns " +
                    "FROM cfg_param_co AS p " +
                    "LEFT OUTER JOIN trn_dns_dps AS t ON p.fid_dns_log_n = t.id_dns " +
                    "WHERE id_co=" + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkConfigCoId = resultSet.getInt("id_co");
                mnDaysOfGraceSupplier = resultSet.getInt("days_grace_sup");
                mnDaysOfGraceCustomer = resultSet.getInt("days_grace_cus");
                oLogo_n = resultSet.getBlob("logo_n");
                mnMaskAccount = resultSet.getInt("mask_acc");
                mnMaskCostCenter = resultSet.getInt("mask_cc");
                msXmlBaseDirectory = resultSet.getString("xml_base_dir");
                msImagesDirectory = resultSet.getString("img_dir");
                msFormerSystemOdbc = resultSet.getString("former_sys_odbc");
                msFormerSystemOdbcUser = resultSet.getString("former_sys_odbc_usr");
                msFormerSystemOdbcUserPassword = resultSet.getString("former_sys_odbc_usr_pswd");
                msFiscalSettings = resultSet.getString("fiscal_settings");
                msNotesPurchasesOrder = resultSet.getString("notes_pur_ord");
                msPaymentEmail = resultSet.getString("pay_email");
                msPaymentNumberSeries = resultSet.getString("pay_num_ser");
                msRegistrySs = resultSet.getString("reg_ss");
                mdInterestDelayRate = resultSet.getDouble("int_delay_rate");
                mnPricePolicyForPurchases = resultSet.getInt("price_pol_pur");
                mnPricePolicyForSales = resultSet.getInt("price_pol_sal");
                mnInventoryValuationMethod = resultSet.getInt("ivm");
                mnVersion = resultSet.getInt("ver");
                mtVersionTs = resultSet.getTimestamp("ts_ver");
                mbIsAuthorizationPurchasesOrderAutomatic = resultSet.getBoolean("b_authorn_pur_ord");
                mbIsAuthorizationPurchasesDocAutomatic = resultSet.getBoolean("b_authorn_pur_doc");
                mbIsAuthorizationSalesOrderAutomatic = resultSet.getBoolean("b_authorn_sal_ord");
                mbIsAuthorizationSalesDocAutomatic = resultSet.getBoolean("b_authorn_sal_doc");
                mbIsLogisticsOrderAutomatic = resultSet.getBoolean("b_log_ord_aut");
                mbIsLastSearchItemPreserved = resultSet.getBoolean("b_last_search_item");
                mbIsExchangeRatePurPreserved = resultSet.getBoolean("b_keep_exc_rate_pur");
                mbIsExchangeRateSalPreserved = resultSet.getBoolean("b_keep_exc_rate_sal");
                mbIsLotApprovalRequired = resultSet.getBoolean("b_lot_apr");
                mbIsPaymentMonday = resultSet.getBoolean("b_pay_mon");
                mbIsPaymentTuesday = resultSet.getBoolean("b_pay_tue");
                mbIsPaymentWednesday = resultSet.getBoolean("b_pay_wed");
                mbIsPaymentThursday = resultSet.getBoolean("b_pay_thu");
                mbIsPaymentFriday = resultSet.getBoolean("b_pay_fri");
                mbIsPaymentSaturday = resultSet.getBoolean("b_pay_sat");
                mbIsPaymentSunday = resultSet.getBoolean("b_pay_sun");
                mbIsCfdiProduction = resultSet.getBoolean("b_cfdi_prod");
                mbIsCfdiSendingAutomaticSal = resultSet.getBoolean("b_cfdi_snd_aut_sal");
                mbIsCfdiSendingAutomaticBol = resultSet.getBoolean("b_cfdi_snd_aut_bol");
                mbIsCfdiSendingAutomaticHrs = resultSet.getBoolean("b_cfdi_snd_aut_hrs");
                mbIsFunctionalAreas = resultSet.getBoolean("b_func");
                mbIsReportsBackground = resultSet.getBoolean("b_reps_bg");
                mbIsCanEdit = resultSet.getBoolean("b_can_edit");
                mbIsCanDelete = resultSet.getBoolean("b_can_del");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkDefaultBookkeepingCenterId_n = resultSet.getInt("fid_bkc_n");
                mnFkDefaultTaxRegionId_n = resultSet.getInt("fid_tax_reg_n");
                mnFkDefaultAddressFormatTypeId_n = resultSet.getInt("fid_tp_add_fmt_n");
                mnFkDefaultCustomerTypeId_n = resultSet.getInt("fid_tp_cus_n");
                mnFkDefaultMarketSegmentId_n = resultSet.getInt("fid_mkt_segm_n");
                mnFkDefaultMarketSubsegmentId_n = resultSet.getInt("fid_mkt_sub_n");
                mnFkDefaultDistributionChannelId_n = resultSet.getInt("fid_dist_chan_n");
                mnFkDefaultSalesRouteId_n = resultSet.getInt("fid_sal_route_n");
                mnFkItemDifferenceExpenseId_n = resultSet.getInt("fid_item_diff_exp_n");
                mnFkItemDifferenceIncomeId_n = resultSet.getInt("fid_item_diff_inc_n");
                mnFkItemPrepaymentPurId_n = resultSet.getInt("fid_item_pre_pay_pur_n");
                mnFkItemPrepaymentSalId_n = resultSet.getInt("fid_item_pre_pay_sal_n");
                mnFkItemLogisticsDeliveryId_n = resultSet.getInt("fid_item_log_delivery_n");
                mnFkItemLogisticsStayId_n = resultSet.getInt("fid_item_log_stay_n");
                mnFkItemLogisticsManeuverId_n = resultSet.getInt("fid_item_log_maneuver_n");
                mnFkItemLogisticsInsuranceId_n = resultSet.getInt("fid_item_log_insurance_n");
                mnFkItemLogisticsCustomsExpId_n = resultSet.getInt("fid_item_log_customs_exp_n");
                mnFkItemLogisticsCustomsImpId_n = resultSet.getInt("fid_item_log_customs_imp_n");
                mnFkItemLogisticsTariffId_n = resultSet.getInt("fid_item_log_tariff_n");
                mnFkItemLogisticsOtherId_n = resultSet.getInt("fid_item_log_other_n");
                mnFkDocNumberSeriesLogisticsId_n =  resultSet.getInt("fid_dns_log_n");
                mnFkDpsNatureLogisticsId = resultSet.getInt("fid_dps_nat_log");
                msFkAccountDifferenceExpenseId_n = resultSet.getString("fid_acc_diff_exp_n");
                if (resultSet.wasNull()) {
                    msFkAccountDifferenceExpenseId_n = "";
                }
                msFkAccountDifferenceIncomeId_n = resultSet.getString("p.fid_acc_diff_inc_n");
                if (resultSet.wasNull()) {
                    msFkAccountDifferenceIncomeId_n = "";
                }
                msFkCostCenterDifferenceExpenseId_n = resultSet.getString("fid_cc_diff_exp_n");
                if (resultSet.wasNull()) {
                    msFkCostCenterDifferenceExpenseId_n = "";
                }
                msFkCostCenterDifferenceIncomeId_n = resultSet.getString("fid_cc_diff_inc_n");
                if (resultSet.wasNull()) {
                    msFkCostCenterDifferenceIncomeId_n = "";
                }
                msFkCfdPaymentAccountExpensesId_n = resultSet.getString("fid_cfd_pay_acc_exp_n");
                if (resultSet.wasNull()) {
                    msFkCfdPaymentAccountExpensesId_n = "";
                }
                msFkCfdPaymentCostCenterExpensesId_n = resultSet.getString("fid_cfd_pay_cc_exp_n");
                if (resultSet.wasNull()) {
                    msFkCfdPaymentCostCenterExpensesId_n = "";
                }
                mnFkCfdPaymentItemBankInterestId_n = resultSet.getInt("fid_cfd_pay_item_bank_int_n");
                mnFkCfdPaymentItemBankFeeId_n = resultSet.getInt("fid_cfd_pay_item_bank_fee_n");
                mnFkCfdPaymentBankFeeTaxBasicId_n = resultSet.getInt("fid_cfd_pay_tax_bas_n");
                mnFkCfdPaymentBankFeeTaxId_n = resultSet.getInt("fid_cfd_pay_tax_n");
                mnFkBasicTaxCharged01Id_n = resultSet.getInt("p.fid_tax_bas_charged_01_n");
                mnFkBasicTaxCharged02Id_n = resultSet.getInt("p.fid_tax_bas_charged_02_n");
                mnFkBasicTaxRetained01Id_n = resultSet.getInt("p.fid_tax_bas_retained_01_n");
                mnFkBasicTaxRetained02Id_n = resultSet.getInt("p.fid_tax_bas_retained_02_n");
                mnFkMfgCostUnitTypeId = resultSet.getInt("fid_mfg_cost_tp_unit");
                mnFkCertificateId_n = resultSet.getInt("fid_cert_n");
                mnFkUserNewId = resultSet.getInt("p.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("p.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("p.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("p.ts_new");
                mtUserEditTs = resultSet.getTimestamp("p.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("p.ts_del");

                msDbmsDocNumberSeriesLogistics = resultSet.getString("f_dns");

                if (oLogo_n != null) {
                    moExtraLogoImageIcon_n = SLibUtilities.convertBlobToImageIcon(oLogo_n);
                }

                oStatementAux = statement.getConnection().createStatement();

                if (mnFkCertificateId_n != 0) {
                    // Read aswell current company's certificate:
                    SDataCertificate cert = new SDataCertificate();
                    if (cert.read(new int[] { mnFkCertificateId_n }, oStatementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        moDbmsCertificate_n = cert;
                    }
                }
                
                // Read CFD default configuration information:
                moDbmsDataCfgCfd = new SDataCfgCfd();
                if (moDbmsDataCfgCfd.read(key, oStatementAux) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
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
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL cfg_param_co_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkConfigCoId);
            callableStatement.setInt(nParam++, mnDaysOfGraceSupplier);
            callableStatement.setInt(nParam++, mnDaysOfGraceCustomer);
            //callableStatement.setBlob(nParam++, moLogo_n);
            callableStatement.setInt(nParam++, mnMaskAccount);
            callableStatement.setInt(nParam++, mnMaskCostCenter);
            callableStatement.setString(nParam++, msXmlBaseDirectory);
            callableStatement.setString(nParam++, msImagesDirectory);
            callableStatement.setString(nParam++, msFormerSystemOdbc);
            callableStatement.setString(nParam++, msFormerSystemOdbcUser);
            callableStatement.setString(nParam++, msFormerSystemOdbcUserPassword);
            callableStatement.setString(nParam++, msPaymentEmail);
            callableStatement.setString(nParam++, msRegistrySs);
            callableStatement.setString(nParam++, msFiscalSettings);
            callableStatement.setString(nParam++, msNotesPurchasesOrder);
            callableStatement.setDouble(nParam++, mdInterestDelayRate);
            callableStatement.setInt(nParam++, mnPricePolicyForPurchases);
            callableStatement.setInt(nParam++, mnPricePolicyForSales);
            callableStatement.setInt(nParam++, mnInventoryValuationMethod);
            callableStatement.setInt(nParam++, mnVersion);
            callableStatement.setTimestamp(nParam++, new java.sql.Timestamp(mtVersionTs.getTime()));
            callableStatement.setBoolean(nParam++, mbIsAuthorizationPurchasesOrderAutomatic);
            callableStatement.setBoolean(nParam++, mbIsAuthorizationPurchasesDocAutomatic);
            callableStatement.setBoolean(nParam++, mbIsAuthorizationSalesOrderAutomatic);
            callableStatement.setBoolean(nParam++, mbIsAuthorizationSalesDocAutomatic);
            callableStatement.setBoolean(nParam++, mbIsLogisticsOrderAutomatic);
            callableStatement.setBoolean(nParam++, mbIsLastSearchItemPreserved);
            callableStatement.setBoolean(nParam++, mbIsExchangeRatePurPreserved);
            callableStatement.setBoolean(nParam++, mbIsExchangeRateSalPreserved);
            callableStatement.setBoolean(nParam++, mbIsLotApprovalRequired);
            callableStatement.setBoolean(nParam++, mbIsPaymentMonday);
            callableStatement.setBoolean(nParam++, mbIsPaymentTuesday);
            callableStatement.setBoolean(nParam++, mbIsPaymentWednesday);
            callableStatement.setBoolean(nParam++, mbIsPaymentThursday);
            callableStatement.setBoolean(nParam++, mbIsPaymentFriday);
            callableStatement.setBoolean(nParam++, mbIsPaymentSaturday);
            callableStatement.setBoolean(nParam++, mbIsPaymentSunday);
            callableStatement.setBoolean(nParam++, mbIsCfdiProduction);
            callableStatement.setBoolean(nParam++, mbIsCfdiSendingAutomaticSal);
            callableStatement.setBoolean(nParam++, mbIsCfdiSendingAutomaticBol);
            callableStatement.setBoolean(nParam++, mbIsCfdiSendingAutomaticHrs);
            callableStatement.setBoolean(nParam++, mbIsFunctionalAreas);
            callableStatement.setBoolean(nParam++, mbIsReportsBackground);
            callableStatement.setBoolean(nParam++, mbIsCanEdit);
            callableStatement.setBoolean(nParam++, mbIsCanDelete);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            if (mnFkDefaultBookkeepingCenterId_n != 0) callableStatement.setInt(nParam++, mnFkDefaultBookkeepingCenterId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkDefaultTaxRegionId_n != 0) callableStatement.setInt(nParam++, mnFkDefaultTaxRegionId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkDefaultAddressFormatTypeId_n != 0) callableStatement.setInt(nParam++, mnFkDefaultAddressFormatTypeId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkDefaultCustomerTypeId_n != 0) callableStatement.setInt(nParam++, mnFkDefaultCustomerTypeId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkDefaultMarketSegmentId_n != 0) callableStatement.setInt(nParam++, mnFkDefaultMarketSegmentId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkDefaultMarketSubsegmentId_n != 0) callableStatement.setInt(nParam++, mnFkDefaultMarketSubsegmentId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkDefaultDistributionChannelId_n != 0) callableStatement.setInt(nParam++, mnFkDefaultDistributionChannelId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkDefaultSalesRouteId_n != 0) callableStatement.setInt(nParam++, mnFkDefaultSalesRouteId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkItemDifferenceExpenseId_n != 0) callableStatement.setInt(nParam++, mnFkItemDifferenceExpenseId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkItemDifferenceIncomeId_n != 0) callableStatement.setInt(nParam++, mnFkItemDifferenceIncomeId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkItemPrepaymentPurId_n != 0) callableStatement.setInt(nParam++, mnFkItemPrepaymentPurId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkItemPrepaymentSalId_n != 0) callableStatement.setInt(nParam++, mnFkItemPrepaymentSalId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkItemLogisticsDeliveryId_n != 0) callableStatement.setInt(nParam++, mnFkItemLogisticsDeliveryId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkItemLogisticsStayId_n != 0) callableStatement.setInt(nParam++, mnFkItemLogisticsStayId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkItemLogisticsManeuverId_n != 0) callableStatement.setInt(nParam++, mnFkItemLogisticsManeuverId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkItemLogisticsInsuranceId_n != 0) callableStatement.setInt(nParam++, mnFkItemLogisticsInsuranceId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkItemLogisticsCustomsExpId_n != 0) callableStatement.setInt(nParam++, mnFkItemLogisticsCustomsExpId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkItemLogisticsCustomsImpId_n != 0) callableStatement.setInt(nParam++, mnFkItemLogisticsCustomsImpId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkItemLogisticsTariffId_n != 0) callableStatement.setInt(nParam++, mnFkItemLogisticsTariffId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkItemLogisticsOtherId_n != 0) callableStatement.setInt(nParam++, mnFkItemLogisticsOtherId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkDocNumberSeriesLogisticsId_n != 0) callableStatement.setInt(nParam++, mnFkDocNumberSeriesLogisticsId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mnFkDpsNatureLogisticsId);
            if (!msFkAccountDifferenceExpenseId_n.isEmpty()) callableStatement.setString(nParam++, msFkAccountDifferenceExpenseId_n); else callableStatement.setNull(nParam++, java.sql.Types.CHAR);
            if (!msFkAccountDifferenceIncomeId_n.isEmpty()) callableStatement.setString(nParam++, msFkAccountDifferenceIncomeId_n); else callableStatement.setNull(nParam++, java.sql.Types.CHAR);
            if (!msFkCostCenterDifferenceExpenseId_n.isEmpty()) callableStatement.setString(nParam++, msFkCostCenterDifferenceExpenseId_n); else callableStatement.setNull(nParam++, java.sql.Types.CHAR);
            if (!msFkCostCenterDifferenceIncomeId_n.isEmpty()) callableStatement.setString(nParam++, msFkCostCenterDifferenceIncomeId_n); else callableStatement.setNull(nParam++, java.sql.Types.CHAR);
            if (!msFkCfdPaymentAccountExpensesId_n.isEmpty()) callableStatement.setString(nParam++, msFkCfdPaymentAccountExpensesId_n); else callableStatement.setNull(nParam++, java.sql.Types.CHAR);
            if (!msFkCfdPaymentCostCenterExpensesId_n.isEmpty()) callableStatement.setString(nParam++, msFkCfdPaymentCostCenterExpensesId_n); else callableStatement.setNull(nParam++, java.sql.Types.CHAR);
            if (mnFkCfdPaymentItemBankInterestId_n != 0) callableStatement.setInt(nParam++, mnFkCfdPaymentItemBankInterestId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkCfdPaymentItemBankFeeId_n != 0) callableStatement.setInt(nParam++, mnFkCfdPaymentItemBankFeeId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkCfdPaymentBankFeeTaxBasicId_n != 0) callableStatement.setInt(nParam++, mnFkCfdPaymentBankFeeTaxBasicId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkCfdPaymentBankFeeTaxId_n != 0) callableStatement.setInt(nParam++, mnFkCfdPaymentBankFeeTaxId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkBasicTaxCharged01Id_n != 0) callableStatement.setInt(nParam++, mnFkBasicTaxCharged01Id_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkBasicTaxCharged02Id_n != 0) callableStatement.setInt(nParam++, mnFkBasicTaxCharged02Id_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkBasicTaxRetained01Id_n != 0) callableStatement.setInt(nParam++, mnFkBasicTaxRetained01Id_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkBasicTaxRetained02Id_n != 0) callableStatement.setInt(nParam++, mnFkBasicTaxRetained02Id_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            callableStatement.setInt(nParam++, mnFkMfgCostUnitTypeId);
            if (mnFkCertificateId_n != 0) callableStatement.setInt(nParam++, mnFkCertificateId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
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
    public int getCompanyId() {
        return getPkConfigCoId();
    }
}
