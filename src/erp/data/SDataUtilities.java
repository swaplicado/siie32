/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.data;

import erp.SClientUtils;
import erp.client.SClientInterface;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataProcedure;
import erp.lib.data.SDataRegistry;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerAddressee;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mbps.data.SDataBizPartnerBranchBankAccount;
import erp.mbps.data.SDataBizPartnerBranchContact;
import erp.mbps.data.SDataEmployee;
import erp.mbps.data.SProcBizPartnerBizAreaVal;
import erp.mbps.data.SProcBizPartnerBranchAddressVal;
import erp.mbps.data.SProcBizPartnerFiscalIdVal;
import erp.mbps.data.SProcBizPartnerTypeVal;
import erp.mbps.data.SProcBizPartnerVal;
import erp.mcfg.data.SDataCompany;
import erp.mcfg.data.SDataCompanyBranchEntity;
import erp.mcfg.data.SDataCurrency;
import erp.mcfg.data.SDataLanguage;
import erp.mcfg.data.SProcCompanyBranchEntityCodeVal;
import erp.mcfg.data.SProcCompanyBranchEntityVal;
import erp.mcfg.data.SProcCurrencyKeyVal;
import erp.mcfg.data.SProcLanguageKeyVal;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataAccountUserSubclass;
import erp.mfin.data.SDataBankLayoutType;
import erp.mfin.data.SDataCheck;
import erp.mfin.data.SDataCheckPrintingFormat;
import erp.mfin.data.SDataCheckPrintingFormatGraphic;
import erp.mfin.data.SDataCheckWallet;
import erp.mfin.data.SDataCostCenter;
import erp.mfin.data.SDataCostCenterItem;
import erp.mfin.data.SDataExchangeRate;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SDataRecordType;
import erp.mfin.data.SDataTax;
import erp.mfin.data.SProcAccountBizPartnerBpTypeVal;
import erp.mfin.data.SProcAccountBizPartnerBpVal;
import erp.mfin.data.SProcAccountBizPartnerGet;
import erp.mfin.data.SProcAccountItemItemVal;
import erp.mfin.data.SProcAccountRecordVal;
import erp.mfin.data.SProcAccountTaxVal;
import erp.mfin.data.SProcAccountVal;
import erp.mfin.data.SProcAdministrativeConceptTypeVal;
import erp.mfin.data.SProcCardIssuerVal;
import erp.mfin.data.SProcCheckCancel;
import erp.mfin.data.SProcCheckNumberVal;
import erp.mfin.data.SProcCheckPrintingFormatVal;
import erp.mfin.data.SProcCheckWalletVal;
import erp.mfin.data.SProcCostCenterRecordVal;
import erp.mfin.data.SProcCostCenterVal;
import erp.mfin.data.SProcExchangeRateVal;
import erp.mfin.data.SProcGetAccountDpsWithBalance;
import erp.mfin.data.SProcGetAccountFirstLevel;
import erp.mfin.data.SProcGetAccountItemId;
import erp.mfin.data.SProcGetBizPartnerBalance;
import erp.mfin.data.SProcGetCheckNextNumber;
import erp.mfin.data.SProcGetCheckNumberRecord;
import erp.mfin.data.SProcGetCheckWalletCount;
import erp.mfin.data.SProcGetCompanyBranchBookKeepingCenterId;
import erp.mfin.data.SProcGetIdCheckPrintingFormat;
import erp.mfin.data.SProcGetRecordTypeId;
import erp.mfin.data.SProcRecordNewId;
import erp.mfin.data.SProcTaxGroupItemGenericVal;
import erp.mfin.data.SProcTaxGroupItemVal;
import erp.mfin.data.SProcTaxIdentityVal;
import erp.mfin.data.SProcTaxRegionVal;
import erp.mfin.data.SProcTaxableConceptTypeVal;
import erp.mfin.data.SProcYearPeriodStatus;
import erp.mfin.data.SProcYearVal;
import erp.mhrs.data.SDataFormerPayroll;
import erp.mhrs.data.SDataFormerPayrollEmp;
import erp.mitm.data.SDataBrand;
import erp.mitm.data.SDataElement;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataItemBizPartnerDescription;
import erp.mitm.data.SDataItemFamily;
import erp.mitm.data.SDataItemGeneric;
import erp.mitm.data.SDataItemGroup;
import erp.mitm.data.SDataItemLine;
import erp.mitm.data.SDataManufacturer;
import erp.mitm.data.SDataUnit;
import erp.mitm.data.SDataUnitType;
import erp.mitm.data.SDataVariety;
import erp.mitm.data.SDataVarietyType;
import erp.mitm.data.SProcBrandTypeVal;
import erp.mitm.data.SProcBrandVal;
import erp.mitm.data.SProcElementTypeVal;
import erp.mitm.data.SProcElementVal;
import erp.mitm.data.SProcItemBarcodeVal;
import erp.mitm.data.SProcItemFamilyVal;
import erp.mitm.data.SProcItemGenericVal;
import erp.mitm.data.SProcItemGroupVal;
import erp.mitm.data.SProcItemKeyVal;
import erp.mitm.data.SProcItemVal;
import erp.mitm.data.SProcManufacturerTypeVal;
import erp.mitm.data.SProcManufacturerVal;
import erp.mitm.data.SProcUnitTypeVal;
import erp.mitm.data.SProcUnitVal;
import erp.mitm.data.SProcVarietyTypeVal;
import erp.mitm.data.SProcVarietyVal;
import erp.mloc.data.SProcCountryKeyVal;
import erp.mloc.data.SProcCountyKeyVal;
import erp.mloc.data.SProcLocalityKeyVal;
import erp.mloc.data.SProcStateKeyVal;
import erp.mmfg.data.SDataBom;
import erp.mmfg.data.SDataBomSubstitute;
import erp.mmfg.data.SDataExplotionMaterials;
import erp.mmfg.data.SDataExplotionMaterialsEntry;
import erp.mmfg.data.SDataProductionOrder;
import erp.mmfg.data.SProcBomExplotionMaterialsReq;
import erp.mmfg.data.SProcBomVal;
import erp.mmfg.data.SProcLeadtimeCoGet;
import erp.mmfg.data.SProcLeadtimeCoVal;
import erp.mmfg.data.SProcLeadtimeCobGet;
import erp.mmfg.data.SProcLeadtimeCobVal;
import erp.mmfg.data.SProcLeadtimeLinkCoVal;
import erp.mmfg.data.SProcLeadtimeLinkCobVal;
import erp.mmfg.data.SProcProductionOrderItemQuantity;
import erp.mmfg.data.SProcProductionOrderPerVal;
import erp.mmfg.data.SProcProductionOrderUpdate;
import erp.mmfg.data.SProcProductionOrderVal;
import erp.mmkt.data.SDataCommissionsSalesAgent;
import erp.mmkt.data.SDataCommissionsSalesAgentType;
import erp.mmkt.data.SParamsItemPriceList;
import erp.mmkt.data.SProcCustomerConfigVal;
import erp.mmkt.data.SProcFilterItem;
import erp.mmkt.data.SProcPriceListCustomerTypeVal;
import erp.mmkt.data.SProcPriceListCustomerVal;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SBpsUtils;
import erp.mod.trn.db.STrnUtils;
import erp.mtrn.data.SDataBizPartnerBlocking;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataCfdPacType;
import erp.mtrn.data.SDataCfdPayment;
import erp.mtrn.data.SDataCfdSignLog;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDiogDocumentNumberSeries;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsDocumentNumberSeries;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SDataDpsType;
import erp.mtrn.data.SDataPac;
import erp.mtrn.data.SDataStockLot;
import erp.mtrn.data.SDataSystemNotes;
import erp.mtrn.data.SProcBizPartnerLastPayment;
import erp.mtrn.data.SProcDiogDncCompanyBranchEntityVal;
import erp.mtrn.data.SProcDiogDncCompanyBranchVal;
import erp.mtrn.data.SProcDiogDncDocumentNumberSeriesVal;
import erp.mtrn.data.SProcDiogDocumentNumberSerieVal;
import erp.mtrn.data.SProcDiogDocumentNumberingCenterVal;
import erp.mtrn.data.SProcDiogNumSerVal;
import erp.mtrn.data.SProcDiogUpdate;
import erp.mtrn.data.SProcDpsBalanceGet;
import erp.mtrn.data.SProcDpsDncCompanyBranchEntityVal;
import erp.mtrn.data.SProcDpsDncCompanyBranchVal;
import erp.mtrn.data.SProcDpsDncDocumentNumberSeriesVal;
import erp.mtrn.data.SProcDpsDocumentNumberSerieVal;
import erp.mtrn.data.SProcDpsDocumentNumberingCenterVal;
import erp.mtrn.data.SProcDpsEntryCountVal;
import erp.mtrn.data.SProcDpsEntryDateVal;
import erp.mtrn.data.SProcDpsNumBalanceGet;
import erp.mtrn.data.SProcDpsTypeCode;
import erp.mtrn.data.SProcDpsUpdate;
import erp.mtrn.data.SProcDsmReferenceBalanceGet;
import erp.mtrn.data.SProcGetDpsEntries;
import erp.mtrn.data.SProcGetDpsEntriesPendingSupply;
import erp.mtrn.data.SProcGetDpsEntrySuppliedInformation;
import erp.mtrn.data.SProcGetDpsInformation;
import erp.mtrn.data.SProcGetItemStock;
import erp.mtrn.data.SProcGetOrderRawMaterialBalance;
import erp.mtrn.data.SProcGetQuantityDelivered;
import erp.mtrn.data.SProcGetRawMaterialBalance;
import erp.mtrn.data.SProcOpenCloseDps;
import erp.mtrn.data.SProcStampsAvailables;
import erp.mtrn.data.SProcStockGet;
import erp.mtrn.data.SProcStockSafetyGet;
import erp.mtrn.data.SProcUpdateCtrStatus;
import erp.mtrn.data.SProcUpdateDpsLogistics;
import erp.musr.data.SDataAccessCompanyBranch;
import erp.musr.data.SDataRole;
import erp.musr.data.SDataUser;
import erp.musr.data.SProcUserPassword;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import sa.lib.srv.SSrvConsts;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author Sergio Flores, Uriel Castañeda, Sergio Flores, Isabel Servín, Claudio Peña
 */
public abstract class SDataUtilities {

    /**
     * @param maskErp The following characters can be specified:
     *  9   Any number (Character.isDigit).
     *  -   Special character '-'.
     * @param level Level of the accounting ID.
     */
    private static java.lang.String createMaskFormatterAccount(java.lang.String maskErp, int level) {
        boolean quit = false;
        String mask = SLibUtilities.textTrim(maskErp);
        String maskLevel = "";
        String maskFormatted = "";

        for (int i = 0; i < level && !quit; i++) {
            if (mask.indexOf('-', i == 0 ? 0 : 1) != -1) {
                maskLevel = mask.substring(0, mask.indexOf('-', i == 0 ? 0 : 1));
            }
            else {
                maskLevel = mask;
                quit = true;
            }

            maskFormatted += maskLevel;
            mask = mask.substring(maskLevel.length());
        }

        maskFormatted = maskFormatted.replaceAll("9", "#");

        return maskFormatted;
    }

    /**
     * @param client ERP Client interface.
     * @param level Level of the account ID.
     */
    public static java.lang.String createMaskFormatterAccountId(erp.client.SClientInterface client, int level) {
        return createMaskFormatterAccount(client.getSessionXXX().getParamsErp().getFormatAccountId(), level);
    }

    /**
     * @param client ERP Client interface.
     * @param level Level of the cost center ID.
     */
    public static java.lang.String createMaskFormatterCostCenterId(erp.client.SClientInterface client, int level) {
        return createMaskFormatterAccount(client.getSessionXXX().getParamsErp().getFormatCostCenterId(), level);
    }

    /**
     * @param client ERP Client interface.
     * @param level Level of the account ID.
     */
    public static java.lang.String createNewFormattedAccountId(erp.client.SClientInterface client, int level) {
        return createMaskFormatterAccount(client.getSessionXXX().getParamsErp().getFormatAccountId(), level).replaceAll("#", "0");
    }

    /**
     * @param client ERP Client interface.
     * @param level Level of the cost center ID.
     */
    public static java.lang.String createNewFormattedCostCenterId(erp.client.SClientInterface client, int level) {
        return createMaskFormatterAccount(client.getSessionXXX().getParamsErp().getFormatCostCenterId(), level).replaceAll("#", "0");
    }

    /**
     * Composes an account ID.
     * Given an account ID, this function completes it with zeros according to ERP's format configuration.
     *
     * @param client ERP Client interface.
     * @param accountId Account ID that needs to be composed according to 'maskErp' parameter.
     */
    public static java.lang.String composeAccountId(erp.client.SClientInterface client, java.lang.String accountId) {
        return accountId + client.getSessionXXX().getParamsErp().getFormatAccountId().substring(accountId.length()).replaceAll("9", "0");
    }

    /**
     * Composes a cost center ID.
     * Given a cost center ID, this function completes it with zeros according to ERP's format configuration.
     *
     * @param client ERP Client interface.
     * @param costCenterId Cost center ID that needs to be composed according to 'maskErp' parameter.
     */
    public static java.lang.String composeCostCenterId(erp.client.SClientInterface client, java.lang.String costCenterId) {
        return costCenterId + client.getSessionXXX().getParamsErp().getFormatCostCenterId().substring(costCenterId.length()).replaceAll("9", "0");
    }

    /**
     * Obtains account ID by level, in complete ERP's format configuration, from given account ID.
     * Example:
     * Obtaining level 1 of 9999-999-999-99-99 results in 9999-000-000-00-00.
     *
     * @param client ERP Client interface.
     * @param accountId Account ID.
     * @param level Level.
     * @return Complete account ID by level.
     */
    public static java.lang.String obtainAccountIdByLevel(erp.client.SClientInterface client, java.lang.String accountId, int level) {
        return composeAccountId(client, accountId.substring(0, createMaskFormatterAccountId(client, level).length()));
    }

    /**
     * Obtains cost center ID by level, in complete ERP's format configuration, from given cost center ID.
     * Example:
     * Obtaining level 1 of 9999-999-999-99-99 results in 9999-000-000-00-00.
     *
     * @param client ERP Client interface.
     * @param costCenterId Cost center ID.
     * @param level Level.
     * @return Complete cost center ID by level.
     */
    public static java.lang.String obtainCostCenterIdByLevel(erp.client.SClientInterface client, java.lang.String costCenterId, int level) {
        return composeCostCenterId(client, costCenterId.substring(0, createMaskFormatterCostCenterId(client, level).length()));
    }

    /**
     * Given an account ID, obtains its levels count.
     *
     * @param psAccountId Account ID.
     * @return Levels found in account ID.
     */
    public static int getAccountLevelsCount(java.lang.String psAccountId) {
        int index = -1;
        int count = 0;

        do {
            count++;
            index = psAccountId.indexOf('-', index + 1);
        } while (index != -1);

        return count;
    }

    /**
     * Obtains account ID's levels.
     *
     * @param psAccountId Account ID.
     * @return A java.util.Vector<java.lang.Integer> object containing all beginning caret dots of each level.
     */
    public static java.util.Vector<java.lang.Integer> getAccountLevels(java.lang.String psAccountId) {
        int index = -1;
        Vector<Integer> levels = new Vector<Integer>();

        do {
            levels.add(index + 1);
            index = psAccountId.indexOf('-', index + 1);
        } while (index != -1);

        return levels;
    }

    /**
     * Given an account ID, obtains its used levels count.
     *
     * @param psAccountId Account ID.
     * @param pvLevels An object of type java.util.Vector<java.lang.Integer> containing all beginning caret dots of each level.
     * @return Used levels count.
     */
    public static int getAccountUsedLevelsCount(java.lang.String psAccountId, java.util.Vector<java.lang.Integer> pvLevels) {
        int count = 0;
        String aux = "";

        /*
         * If user selects all characters in JFormatedTextField control, and then press backspace, input mask disapears.
         * After that psAccountId is an empty String object, so it is necesary to prevent String index out of range.
         */

        for (int i = 0; i < pvLevels.size(); i++) {
            if ((i + 1) < pvLevels.size()) {
                if (psAccountId.length() < pvLevels.get(i + 1) + 1) {
                    aux = "";   // preventing String out of range
                }
                else {
                    aux = psAccountId.substring(pvLevels.get(i), pvLevels.get(i + 1) - 1);
                }
            }
            else {
                if (psAccountId.length() < pvLevels.get(i) + 1) {
                    aux = "";   // preventing String out of range
                }
                else {
                    aux = psAccountId.substring(pvLevels.get(i));
                }
            }

            if (SLibUtilities.parseLong(aux) > 0L) {
                count++;
            }
            else {
                break;
            }
        }

        return count;
    }

    /**
     * Given an account ID, obtains its used levels.
     *
     * @param psAccountId Account ID.
     * @return Used levels.
     */
    public static java.lang.String getAccountUsedLevels(java.lang.String psAccountId) {
        return getAccountUsedLevels(psAccountId, null);
    }

    /**
     * Given an account ID, obtains its used levels.
     *
     * @param psAccountId Account ID.
     * @param pvLevels An object of type java.util.Vector<java.lang.Integer> containing all beginning caret dots of each level.
     * @return Used levels.
     */
    public static java.lang.String getAccountUsedLevels(java.lang.String psAccountId, java.util.Vector<java.lang.Integer> pvLevels) {
        int used = 0;
        String account = "";
        Vector<Integer> levels = pvLevels != null ? pvLevels : getAccountLevels(psAccountId);

        used = getAccountUsedLevelsCount(psAccountId, levels);

        if (used > 0) {
            if (used < levels.size()) {
                account = psAccountId.substring(0, levels.get(used) - 1);
            }
            else {
                account = psAccountId;
            }
        }

        return account;
    }

    /**
     * Sets account complete name of given account ID in javax.swing.JTextField and returns corresponding major account and account of input as erp.mfin.data.SDataAccount objects.
     *
     * @param piClient ERP Client interface.
     * @param psAccountId Account ID.
     * @param pvLevels An object of type java.util.Vector<java.lang.Integer> containing all beginning caret dots of each level.
     * @return Array of type java.lang.Object[]:
     * 1. Major account object as erp.mfin.data.SDataAccount.
     * 2. Input account object as erp.mfin.data.SDataAccount.
     * 3. Account complete name as java.lang.String.
     */
    public static java.lang.Object[] getInputAccountsAndDescription(erp.client.SClientInterface piClient, java.lang.String psAccountId, java.util.Vector<java.lang.Integer> pvLevels) {
        int nCurrentLevels = getAccountUsedLevelsCount(psAccountId, pvLevels);
        String sAuxId = "";
        String sDescrip = "";
        SDataAccount oAccountMajor = null;
        SDataAccount oAccount = null;

        for (int i = 1; i <= nCurrentLevels; i++) {
            sAuxId = obtainAccountIdByLevel(piClient, psAccountId, i);

            if (i == 1) {
                // Major account ID is in level 1:

                oAccountMajor = (SDataAccount) readRegistry(piClient, SDataConstants.FIN_ACC, new Object[] { sAuxId }, SLibConstants.EXEC_MODE_SILENT);
                if (oAccountMajor != null) {
                    sDescrip = oAccountMajor.getAccount();
                }
            }
            else {
                oAccount = (SDataAccount) readRegistry(piClient, SDataConstants.FIN_ACC, new Object[] { sAuxId }, SLibConstants.EXEC_MODE_SILENT);
                if (oAccount != null) {
                    sDescrip += (sDescrip.length() == 0 ? "" : ", ") + oAccount.getAccount();
                }
            }
        }

        return new Object[] { oAccountMajor, oAccount, sDescrip };
    }

    /**
     * Sets cost center complete name of given cost center ID in javax.swing.JTextField and returns corresponding major cost center and cost center of input as erp.mfin.data.SDataCostCenter objects.
     *
     * @param piClient ERP Client interface.
     * @param psCostCenterId Cost center ID.
     * @param pvLevels An object of type java.util.Vector<java.lang.Integer> containing all beginning caret dots of each level.
     * @return Array of type java.lang.Object[]:
     * 1. Major cost center object as erp.mfin.data.SDataCostCenter.
     * 2. Input cost center object as erp.mfin.data.SDataCostCenter.
     * 3. Cost center complete name as java.lang.String.
     */
    public static java.lang.Object[] getInputCostCentersAndDescription(erp.client.SClientInterface piClient, java.lang.String psCostCenterId, java.util.Vector<java.lang.Integer> pvLevels) {
        int nCurrentLevels = getAccountUsedLevelsCount(psCostCenterId, pvLevels);
        String sAuxId = "";
        String sDescrip = "";
        SDataCostCenter oCostCenterMajor = null;
        SDataCostCenter oCostCenter = null;

        for (int i = 1; i <= nCurrentLevels; i++) {
            sAuxId = obtainCostCenterIdByLevel(piClient, psCostCenterId, i);

            if (i == 1) {
                // Major account ID is in level 1:

                oCostCenterMajor = (SDataCostCenter) readRegistry(piClient, SDataConstants.FIN_CC, new Object[] { sAuxId }, SLibConstants.EXEC_MODE_SILENT);
                if (oCostCenterMajor != null) {
                    sDescrip += oCostCenterMajor.getCostCenter();
                }
            }
            else {
                oCostCenter = (SDataCostCenter) readRegistry(piClient, SDataConstants.FIN_CC, new Object[] { sAuxId }, SLibConstants.EXEC_MODE_SILENT);
                if (oCostCenter != null) {
                    sDescrip += (sDescrip.length() == 0 ? "" : ", ") + oCostCenter.getCostCenter();
                }
            }
        }

        return new Object[] { oCostCenterMajor, oCostCenter, sDescrip };
    }

    /**
     * Validate supplied account ID syntax (i.e. account ID or cost center ID).
     * Example:
     * 000-000  invalid
     * 000-999  invalid
     * 999-000  valid
     * 999-999  valid
     *
     * @param accountId Account ID.
     */
    public static boolean validateAccountSyntax(java.lang.String accountId) {
        int beginIndex = -1;
        int endIndex = -1;
        boolean isError = false;
        boolean hasLevelZero = false;
        boolean hasLevelNonZero = false;

        while (accountId.indexOf('-', beginIndex + 1) != -1) {
            endIndex = accountId.indexOf('-', beginIndex + 1);

            if (SLibUtilities.parseInt(accountId.substring(beginIndex + 1, endIndex)) == 0) {
                hasLevelZero = true;
            }
            else {
                hasLevelNonZero = true;
                if (hasLevelZero) {
                    isError = true;
                    break;
                }
            }
            beginIndex = endIndex;
        }

        return hasLevelNonZero && !isError;
    }

    /**
     * Checks if fiscal period is opened.
     * @param client ERP Client interface.
     * @param period Period as an int array (index 0: year; index 1: period).
     */
    public static boolean isPeriodOpen(erp.client.SClientInterface client, int[] period) {
        Vector<Object> params = new Vector<>();

        params.add(period[0]);  // year
        params.add(period[1]);  // period
        params = callProcedure(client, SProcConstants.FIN_YEAR_PER_ST, params, SLibConstants.EXEC_MODE_SILENT);

        return !((Boolean) params.get(0));  // return not closed status as valid
    }

    /**
     * Checks if fiscal period is opened.
     * @param client ERP Client interface.
     * @param date Date.
     */
    public static boolean isPeriodOpen(erp.client.SClientInterface client, java.util.Date date) {
        return isPeriodOpen(client, SLibTimeUtilities.digestYearMonth(date));
    }

    /**
     * Reads the requested registry.
     * @param client ERP Client interface.
     * @param dataType Data type (constants defined in erp.data.SDataConstants).
     * @param pk Primary key of desired registry.
     * @param executionMode  Execution mode (constants defined in SLibConstants.EXEC_MODE_...).
     * @return SDataRegistry
     */
    public static erp.lib.data.SDataRegistry readRegistry(erp.client.SClientInterface client, int dataType, java.lang.Object pk, int executionMode) {
        SServerRequest request = null;
        SServerResponse response = null;
        SDataRegistry registry = null;

        switch (dataType) {
            case SDataConstants.CFGU_CO:
                registry = new SDataCompany();
                break;
            case SDataConstants.CFGU_COB_ENT:
                registry = new SDataCompanyBranchEntity();
                break;
            case SDataConstants.CFGU_LAN:
                registry = new SDataLanguage();
                break;
            case SDataConstants.CFGU_CUR:
                registry = new SDataCurrency();
                break;

            case SDataConstants.BPSU_BP:
                registry = new SDataBizPartner();
                break;
            case SDataConstants.BPSU_BPB:
                registry = new SDataBizPartnerBranch();
                break;
            case SDataConstants.BPSU_BPB_ADD:
                registry = new SDataBizPartnerBranchAddress();
                break;
            case SDataConstants.BPSU_BPB_CON:
                registry = new SDataBizPartnerBranchContact();
                break;
            case SDataConstants.BPSU_BANK_ACC:
                registry = new SDataBizPartnerBranchBankAccount();
                break;
            case SDataConstants.BPSU_BP_ADDEE:
                registry = new SDataBizPartnerAddressee();
                break;

            case SDataConstants.ITMU_CFG_ITEM_BP:
                registry = new SDataItemBizPartnerDescription();
                break;
            case SDataConstants.ITMU_TP_UNIT:
                registry = new SDataUnitType();
                break;
            case SDataConstants.ITMU_UNIT:
                registry = new SDataUnit();
                break;
            case SDataConstants.ITMU_TP_VAR:
                registry = new SDataVarietyType();
                break;
            case SDataConstants.ITMU_VAR:
                registry = new SDataVariety();
                break;
            case SDataConstants.ITMU_BRD:
                registry = new SDataBrand();
                break;
            case SDataConstants.ITMU_MFR:
                registry = new SDataManufacturer();
                break;
            case SDataConstants.ITMU_EMT:
                registry = new SDataElement();
                break;
            case SDataConstants.ITMU_IFAM:
                registry = new SDataItemFamily();
                break;
            case SDataConstants.ITMU_IGRP:
                registry = new SDataItemGroup();
                break;
            case SDataConstants.ITMU_IGEN:
                registry = new SDataItemGeneric();
                break;
            case SDataConstants.ITMU_LINE:
                registry = new SDataItemLine();
                break;
            case SDataConstants.ITMU_ITEM:
                registry = new SDataItem();
                break;

            case SDataConstants.FINU_TP_REC:
                registry = new SDataRecordType();
                break;
            case SDataConstants.FINU_TAX:
                registry = new SDataTax();
                break;
            case SDataConstants.FINU_CLS_ACC_USR:
                registry = new SDataAccountUserSubclass();
                break;
            case SDataConstants.FINU_TP_LAY_BANK:
                registry = new SDataBankLayoutType();
                break;
            case SDataConstants.FINU_CHECK_FMT:
                registry = new SDataCheckPrintingFormat();
                break;
            case SDataConstants.FINU_CHECK_FMT_GP:
                registry = new SDataCheckPrintingFormatGraphic();
                break;
            case SDataConstants.FIN_EXC_RATE:
                registry = new SDataExchangeRate();
                break;
            case SDataConstants.FIN_ACC_CASH:
                registry = new SDataAccountCash();
                break;
            case SDataConstants.FIN_CHECK_WAL:
                registry = new SDataCheckWallet();
                break;
            case SDataConstants.FIN_CHECK:
                registry = new SDataCheck();
                break;
            case SDataConstants.FIN_ACC:
                registry = new SDataAccount();
                break;
            case SDataConstants.FIN_CC:
                registry = new SDataCostCenter();
                break;
            case SDataConstants.FIN_CC_ITEM:
                registry = new SDataCostCenterItem();
                break;
            case SDataConstants.FIN_REC:
                registry = new SDataRecord();
                break;
            case SDataConstants.FIN_REC_ETY:
                registry = new SDataRecordEntry();
                break;

            case SDataConstants.TRNU_TP_DPS:
                registry = new SDataDpsType();
                break;
            case SDataConstants.TRN_SYS_NTS:
                registry = new SDataSystemNotes();
                break;
            case SDataConstants.TRN_DPS:
                registry = new SDataDps();
                break;
            case SDataConstants.TRN_CFD:
                registry = new SDataCfd();
                break;
            case SDataConstants.TRN_CFD_SIGN_LOG:
                registry = new SDataCfdSignLog();
                break;
            case SDataConstants.TRNX_CFD_PAY_REC:
                registry = new SDataCfdPayment();
                break;
            case SDataConstants.TRN_PAC:
                registry = new SDataPac();
                break;
            case SDataConstants.TRN_TP_CFD_PAC:
                registry = new SDataCfdPacType();
                break;
            case SDataConstants.TRN_DPS_ETY:
                registry = new SDataDpsEntry();
                break;
            case SDataConstants.TRN_DIOG:
                registry = new SDataDiog();
                break;
            case SDataConstants.TRN_LOT:
                registry = new SDataStockLot();
                break;
            case SDataConstants.TRN_DNS_DPS:
                registry = new SDataDpsDocumentNumberSeries();
                break;
            case SDataConstants.TRN_DNS_DIOG:
                registry = new SDataDiogDocumentNumberSeries();
                break;
            case SDataConstants.TRN_BP_BLOCK:
                registry = new SDataBizPartnerBlocking();
                break;
                
            case SDataConstants.MKT_CFG_CUS:
                registry = new erp.mmkt.data.SDataCustomerConfig();
                break;
            case SDataConstants.MKT_CFG_CUSB:
                registry = new erp.mmkt.data.SDataCustomerBranchConfig();
                break;
                
            case SDataConstants.MFG_BOM:
                registry = new SDataBom();
                break;
            case SDataConstants.MFG_BOM_SUB:
                registry = new SDataBomSubstitute();
                break;
            case SDataConstants.MFG_ORD:
                registry = new SDataProductionOrder();
                break;
            case SDataConstants.MFG_EXP:
                registry = new SDataExplotionMaterials();
                break;
            case SDataConstants.MFG_EXP_ETY:
                registry = new SDataExplotionMaterialsEntry();
                break;
            case SModConsts.HRSU_EMP:
                registry = new SDataEmployee();
                break;
            case SDataConstants.HRS_SIE_PAY:
                registry = new SDataFormerPayroll();
                break;
            case SDataConstants.HRS_SIE_PAY_EMP:
                registry = new SDataFormerPayrollEmp();
                break;

            case SDataConstants.USRU_USR:
                registry = new SDataUser();
                break;
            case SDataConstants.USRS_ROL:
                registry = new SDataRole();
                break;
            case SDataConstants.USRU_ACCESS_COB:
                registry = new SDataAccessCompanyBranch();
                break;

            default:
                switch (executionMode) {
                    case SLibConstants.EXEC_MODE_STEALTH:
                        break;
                    case SLibConstants.EXEC_MODE_SILENT:
                        SLibUtilities.printOutException(SDataUtilities.class.getName(), new Exception(SDataConstants.MSG_ERR_DATA_NOT_FOUND));
                        break;
                    case SLibConstants.EXEC_MODE_VERBOSE:
                        SLibUtilities.renderException(SDataUtilities.class.getName(), new Exception(SDataConstants.MSG_ERR_DATA_NOT_FOUND));
                        break;
                    default:
                }
        }

        if (registry != null) {
            request = new SServerRequest(SServerConstants.REQ_DB_ACTION_READ);
            request.setPrimaryKey(pk);
            request.setPacket(registry);
            response = client.getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK || response.getResultType() != SLibConstants.DB_ACTION_READ_OK) {
                registry = null;
                switch (executionMode) {
                    case SLibConstants.EXEC_MODE_STEALTH:
                        break;
                    case SLibConstants.EXEC_MODE_SILENT:
                        SLibUtilities.printOutException(SDataUtilities.class.getName(), new Exception(SLibConstants.MSG_ERR_DB_REG_READ + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage())));
                        break;
                    case SLibConstants.EXEC_MODE_VERBOSE:
                        SLibUtilities.renderException(SDataUtilities.class.getName(), new Exception(SLibConstants.MSG_ERR_DB_REG_READ + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage())));
                        break;
                    default:
                }
            }
            else {
                registry = (SDataRegistry) response.getPacket();
            }
        }

        return registry;
    }

    @SuppressWarnings("unchecked")
    public static erp.lib.data.SDataRegistry[] readRegistriesByKey(erp.client.SClientInterface client, int dataType, java.lang.String key, int executionMode) throws java.lang.Exception {
        int i = 0;
        int j = 0;
        int count = 0;
        int pkLength = 0;
        int[] pk = null;
        String sqlCount = "";
        String sqlQuery = "";
        ResultSet resultSet = null;
        SDataRegistry[] registries = null;

        switch (dataType) {
            case SDataConstants.ITMU_ITEM:
                sqlCount = "SELECT COUNT(*) AS f_count FROM erp.itmu_item WHERE item_key = '" + key + "' AND b_del = 0; ";
                sqlQuery = "SELECT id_item FROM erp.itmu_item WHERE item_key = '" + key + "' AND b_del = 0; ";
                pkLength = 1;
                break;
            default:
        }

        resultSet = client.getSession().getStatement().executeQuery(sqlCount);
        if (resultSet.next()) {
            count = resultSet.getInt("f_count");
            if (count > 0) {
                registries = new SDataRegistry[count];

                resultSet = client.getSession().getStatement().executeQuery(sqlQuery);
                while (resultSet.next()) {
                    pk = new int[pkLength];

                    for (i = 0; i < pkLength; i++) {
                        pk[i] = resultSet.getInt("id_item");
                    }

                    registries[j++] = readRegistry(client, dataType, pk, executionMode);
                    if (j == registries.length) {
                        break;  // just in case, a new registry can be created between two queries execution
                    }
                }
            }
        }

        return registries;
    }

    /**
     * @param client ERP Client interface.
     * @param procValType Validation process type. Constants defined in erp.data.SProcConstants.
     * @param pk In-parameters.
     * @param executionMode  Execution mode. Constants defined in erp.lib.SLibConstants.
     */
    public static int callProcedureVal(erp.client.SClientInterface client, int procValType, java.lang.Object pk, int executionMode) {
        int count = 0;
        SServerRequest request = null;
        SServerResponse response = null;
        SDataProcedure proc = null;

        switch (procValType) {
            case SProcConstants.FINU_TAX_REG_VAL:
                proc = new SProcTaxRegionVal();
                break;
            case SProcConstants.FINU_TAX_IDY_VAL:
                proc = new SProcTaxIdentityVal();
                break;
            case SProcConstants.FINU_CARD_ISS_VAL:
                proc = new SProcCardIssuerVal();
                break;
            case SProcConstants.FINU_CHECK_FMT_VAL:
                proc = new SProcCheckPrintingFormatVal();
                break;
            case SProcConstants.FIN_TAX_GRP_ITEM_VAL:
                proc = new SProcTaxGroupItemVal();
                break;
            case SProcConstants.FIN_TAX_GRP_IGEN_VAL:
                proc = new SProcTaxGroupItemGenericVal();
                break;
            case SProcConstants.FIN_YEAR_VAL:
                proc = new SProcYearVal();
                break;
            case SProcConstants.FIN_EXC_RATE_VAL:
                proc = new SProcExchangeRateVal();
                break;
            case SProcConstants.FIN_ACC_VAL:
                proc = new SProcAccountVal();
                break;
            case SProcConstants.FIN_ACC_REC_VAL:
                proc = new SProcAccountRecordVal();
                break;
            case SProcConstants.FIN_CC_VAL:
                proc = new SProcCostCenterVal();
                break;
            case SProcConstants.FIN_CC_REC_VAL:
                proc = new SProcCostCenterRecordVal();
                break;
            case SProcConstants.FIN_ACC_BP_TP_BP_VAL:
                proc = new SProcAccountBizPartnerBpTypeVal();
                break;
            case SProcConstants.FIN_ACC_BP_BP_VAL:
                proc = new SProcAccountBizPartnerBpVal();
                break;
            case SProcConstants.FIN_ACC_ITEM_ITEM_VAL:
                proc = new SProcAccountItemItemVal();
                break;
            case SProcConstants.FIN_ACC_TAX_VAL:
                proc = new SProcAccountTaxVal();
                break;
            case SProcConstants.FIN_CHECK_WAL_VAL:
                proc = new SProcCheckWalletVal();
                break;
            case SProcConstants.FIN_CHECK_NUM_VAL:
                proc = new SProcCheckNumberVal();
                break;
            case SProcConstants.FINU_TP_ADM_CPT_VAL:
                proc = new SProcAdministrativeConceptTypeVal();
                break;
            case SProcConstants.FINS_TP_TAX_CPT_VAL:
                proc = new SProcTaxableConceptTypeVal();
                break;

            case SProcConstants.CFGU_CUR_VAL:
                proc = new SProcCurrencyKeyVal();
                break;
            case SProcConstants.CFGU_LAN_VAL:
                proc = new SProcLanguageKeyVal();
                break;
            case SProcConstants.CFGU_COB_ENT_VAL:
                proc = new SProcCompanyBranchEntityVal();
                break;
            case SProcConstants.CFGU_COB_ENT_CODE_VAL:
                proc = new SProcCompanyBranchEntityCodeVal();
                break;

            case SProcConstants.BPSU_BP:
                proc = new SProcBizPartnerVal();
                break;
            case SProcConstants.BPSU_BP_FISCAL_ID:
                proc = new SProcBizPartnerFiscalIdVal();
                break;
            case SProcConstants.BPSU_TP_BP:
                proc = new SProcBizPartnerTypeVal();
                break;
            case SProcConstants.BPSU_BA:
                proc = new SProcBizPartnerBizAreaVal();
                break;
            case SProcConstants.BPSU_BPB_ADD:
                proc = new SProcBizPartnerBranchAddressVal();
                break;

            case SProcConstants.ITMU_IFAM_VAL:
                proc = new SProcItemFamilyVal();
                break;
            case SProcConstants.ITMU_IGRP_VAL:
                proc = new SProcItemGroupVal();
                break;
            case SProcConstants.ITMU_IGEN_VAL:
                proc = new SProcItemGenericVal();
                break;
            case SProcConstants.ITMU_ITEM_VAL:
                proc = new SProcItemVal();
                break;
            case SProcConstants.ITMU_ITEM_KEY_VAL:
                proc = new SProcItemKeyVal();
                break;
            case SProcConstants.ITMU_ITEM_BARC_VAL:
                proc = new SProcItemBarcodeVal();
                break;
            case SProcConstants.ITMU_TP_UNIT_VAL:
                proc = new SProcUnitTypeVal();
                break;
            case SProcConstants.ITMU_TP_BRD_VAL:
                proc = new SProcBrandTypeVal();
                break;
            case SProcConstants.ITMU_TP_MFR_VAL:
                proc = new SProcManufacturerTypeVal();
                break;
            case SProcConstants.ITMU_TP_EMT_VAL:
                proc = new SProcElementTypeVal();
                break;
            case SProcConstants.ITMU_TP_VAR_VAL:
                proc = new SProcVarietyTypeVal();
                break;
            case SProcConstants.ITMU_UNIT_VAL:
                proc = new SProcUnitVal();
                break;
            case SProcConstants.ITMU_BRD_VAL:
                proc = new SProcBrandVal();
                break;
            case SProcConstants.ITMU_MFR_VAL:
                proc = new SProcManufacturerVal();
                break;
            case SProcConstants.ITMU_ELT_VAL:
                proc = new SProcElementVal();
                break;
            case SProcConstants.ITMU_VAR_VAL:
                proc = new SProcVarietyVal();
                break;

            case SProcConstants.LOCU_CTRY_VAL:
                proc = new SProcCountryKeyVal();
                break;
            case SProcConstants.LOCU_CTY_VAL:
                proc = new SProcCountyKeyVal();
                break;
            case SProcConstants.LOCU_LOC_VAL:
                proc = new SProcLocalityKeyVal();
                break;
            case SProcConstants.LOCU_STE_VAL:
                proc = new SProcStateKeyVal();
                break;

            case SProcConstants.TRN_DPS_ETY_COUNT_LINK_DPS_SRC:
            case SProcConstants.TRN_DPS_ETY_COUNT_LINK_DPS_DES:
            case SProcConstants.TRN_DPS_ETY_COUNT_ADJ_DOC:
            case SProcConstants.TRN_DPS_ETY_COUNT_ADJ_ADJ:
            case SProcConstants.TRN_DPS_ETY_COUNT_COMMS:
            case SProcConstants.TRN_DPS_ETY_COUNT_DIOG:
            case SProcConstants.TRN_DPS_ETY_COUNT_SHIP:
            case SProcConstants.TRN_DIOG_ETY_COUNT_SHIP:
                proc = new SProcDpsEntryCountVal(procValType);
                break;
            case SProcConstants.TRN_DNC_DPS_VAL:
                proc = new SProcDpsDocumentNumberingCenterVal();
                break;
            case SProcConstants.TRN_DNC_DIOG_VAL:
                proc = new SProcDiogDocumentNumberingCenterVal();
                break;
            case SProcConstants.TRN_DNS_DPS_VAL:
                proc = new SProcDpsDocumentNumberSerieVal();
                break;
            case SProcConstants.TRN_DNS_DIOG_VAL:
                proc = new SProcDiogDocumentNumberSerieVal();
                break;
            case SProcConstants.TRN_DNC_DPS_COB_VAL:
                proc = new SProcDpsDncCompanyBranchVal();
                break;
            case SProcConstants.TRN_DNC_DIOG_COB_VAL:
                proc = new SProcDiogDncCompanyBranchVal();
                break;
            case SProcConstants.TRN_DNC_DPS_COB_ENT_VAL:
                proc = new SProcDpsDncCompanyBranchEntityVal();
                break;
            case SProcConstants.TRN_DNC_DIOG_COB_ENT_VAL:
                proc = new SProcDiogDncCompanyBranchEntityVal();
                break;
            case SProcConstants.TRN_DNC_DPS_DNS_VAL:
                proc = new SProcDpsDncDocumentNumberSeriesVal();
                break;
            case SProcConstants.TRN_DNC_DIOG_DNS_VAL:
                proc = new SProcDiogDncDocumentNumberSeriesVal();
                break;

            case SProcConstants.MFG_BOM_VAL:
                proc = new SProcBomVal();
                break;
            case SProcConstants.MFG_ORD_VAL:
                proc = new SProcProductionOrderVal();
                break;
            case SProcConstants.MFG_ORD_PER_VAL:
                proc = new SProcProductionOrderPerVal();
                break;
            case SProcConstants.MFG_LTIME_CO_VAL:
                proc = new SProcLeadtimeCoVal();
                break;
            case SProcConstants.MFG_LTIME_LINK_CO_VAL:
                proc = new SProcLeadtimeLinkCoVal();
                break;
            case SProcConstants.MFG_LTIME_COB_VAL:
                proc = new SProcLeadtimeCobVal();
                break;
            case SProcConstants.MFG_LTIME_LINK_COB_VAL:
                proc = new SProcLeadtimeLinkCobVal();
                break;

            case SProcConstants.MKT_PLIST_CUS_VAL:
                proc = new SProcPriceListCustomerVal();
                break;
            case SProcConstants.MKT_PLIST_CUS_TP_VAL:
                proc = new SProcPriceListCustomerTypeVal();
                break;
            case SProcConstants.MKT_CFG_CUS_VAL:
                proc = new SProcCustomerConfigVal();
                break;

            default:
                if (executionMode == SLibConstants.EXEC_MODE_SILENT) {
                    SLibUtilities.printOutException(SDataUtilities.class.getName(), new Exception(SProcConstants.MSG_ERR_PROC_NOT_FOUND));
                }
                else {
                    SLibUtilities.printOutException(SDataUtilities.class.getName(), new Exception(SProcConstants.MSG_ERR_PROC_NOT_FOUND));
                }
        }

        if (proc != null) {
            switch (procValType) {
                case SProcConstants.FIN_YEAR_VAL:
                case SProcConstants.FIN_TAX_GRP_ITEM_VAL:
                case SProcConstants.FIN_TAX_GRP_IGEN_VAL:
                    proc.getParamsIn().add(((int[]) pk)[0]);
                    break;
                case SProcConstants.FIN_ACC_VAL:
                case SProcConstants.FIN_ACC_REC_VAL:
                case SProcConstants.FIN_CC_VAL:
                case SProcConstants.FIN_CC_REC_VAL:
                case SProcConstants.MFG_ORD_VAL:
                case SProcConstants.MFG_ORD_PER_VAL:
                case SProcConstants.MKT_CFG_CUS_VAL:
                case SProcConstants.MFG_LTIME_CO_VAL:
                case SProcConstants.TRN_DNC_DPS_COB_VAL:
                case SProcConstants.TRN_DNC_DIOG_COB_VAL:
                    proc.getParamsIn().add(((Object[]) pk)[0]);
                    break;
                case SProcConstants.CFGU_CUR_VAL:
                case SProcConstants.CFGU_LAN_VAL:
                case SProcConstants.BPSU_BA:
                case SProcConstants.BPSU_BP:
                case SProcConstants.BPSU_BP_FISCAL_ID:
                case SProcConstants.FINU_TAX_REG_VAL:
                case SProcConstants.FINU_TAX_IDY_VAL:
                case SProcConstants.FINU_CARD_ISS_VAL:
                case SProcConstants.FIN_EXC_RATE_VAL:
                case SProcConstants.FINU_TP_ADM_CPT_VAL:
                case SProcConstants.FINS_TP_TAX_CPT_VAL:
                case SProcConstants.FINU_CHECK_FMT_VAL:
                case SProcConstants.ITMU_IFAM_VAL:
                case SProcConstants.ITMU_IGEN_VAL:
                case SProcConstants.ITMU_ITEM_VAL:
                case SProcConstants.ITMU_ITEM_KEY_VAL:
                case SProcConstants.ITMU_ITEM_BARC_VAL:
                case SProcConstants.ITMU_TP_UNIT_VAL:
                case SProcConstants.ITMU_TP_BRD_VAL:
                case SProcConstants.ITMU_TP_MFR_VAL:
                case SProcConstants.ITMU_TP_EMT_VAL:
                case SProcConstants.ITMU_TP_VAR_VAL:
                case SProcConstants.LOCU_CTRY_VAL:
                case SProcConstants.LOCU_STE_VAL:
                case SProcConstants.LOCU_CTY_VAL:
                case SProcConstants.LOCU_LOC_VAL:
                case SProcConstants.MKT_PLIST_CUS_VAL:
                case SProcConstants.MKT_PLIST_CUS_TP_VAL:
                case SProcConstants.MFG_LTIME_COB_VAL:
                case SProcConstants.MFG_BOM_VAL:
                case SProcConstants.TRN_DNC_DPS_VAL:
                case SProcConstants.TRN_DNC_DIOG_VAL:
                case SProcConstants.TRN_DNC_DPS_COB_ENT_VAL:
                case SProcConstants.TRN_DNC_DIOG_COB_ENT_VAL:
                case SProcConstants.TRN_DNC_DPS_DNS_VAL:
                case SProcConstants.TRN_DNC_DIOG_DNS_VAL:
                    proc.getParamsIn().add(((Object[]) pk)[0]);
                    proc.getParamsIn().add(((Object[]) pk)[1]);
                    break;
                case SProcConstants.CFGU_COB_ENT_VAL:
                case SProcConstants.CFGU_COB_ENT_CODE_VAL:
                case SProcConstants.BPSU_TP_BP:
                case SProcConstants.BPSU_BPB_ADD:
                case SProcConstants.ITMU_VAR_VAL:
                case SProcConstants.ITMU_BRD_VAL:
                case SProcConstants.ITMU_MFR_VAL:
                case SProcConstants.ITMU_ELT_VAL:
                case SProcConstants.ITMU_IGRP_VAL:
                case SProcConstants.ITMU_UNIT_VAL:
                case SProcConstants.FIN_CHECK_NUM_VAL:
                case SProcConstants.MFG_LTIME_LINK_CO_VAL:
                    proc.getParamsIn().add(((Object[]) pk)[0]);
                    proc.getParamsIn().add(((Object[]) pk)[1]);
                    proc.getParamsIn().add(((Object[]) pk)[2]);
                    break;
                case SProcConstants.FIN_ACC_TAX_VAL:
                case SProcConstants.MFG_LTIME_LINK_COB_VAL:
                case SProcConstants.TRN_DNS_DIOG_VAL:
                    proc.getParamsIn().add(((Object[]) pk)[0]);
                    proc.getParamsIn().add(((Object[]) pk)[1]);
                    proc.getParamsIn().add(((Object[]) pk)[2]);
                    proc.getParamsIn().add(((Object[]) pk)[3]);
                    break;
                case SProcConstants.FIN_ACC_BP_TP_BP_VAL:
                case SProcConstants.FIN_ACC_BP_BP_VAL:
                case SProcConstants.FIN_ACC_ITEM_ITEM_VAL:
                case SProcConstants.FIN_CHECK_WAL_VAL:
                case SProcConstants.TRN_DNS_DPS_VAL:
                    proc.getParamsIn().add(((Object[]) pk)[0]);
                    proc.getParamsIn().add(((Object[]) pk)[1]);
                    proc.getParamsIn().add(((Object[]) pk)[2]);
                    proc.getParamsIn().add(((Object[]) pk)[3]);
                    proc.getParamsIn().add(((Object[]) pk)[4]);
                    break;
                case SProcConstants.TRN_DPS_ETY_COUNT_LINK_DPS_SRC:
                case SProcConstants.TRN_DPS_ETY_COUNT_LINK_DPS_DES:
                case SProcConstants.TRN_DPS_ETY_COUNT_ADJ_DOC:
                case SProcConstants.TRN_DPS_ETY_COUNT_ADJ_ADJ:
                case SProcConstants.TRN_DPS_ETY_COUNT_COMMS:
                case SProcConstants.TRN_DPS_ETY_COUNT_DIOG:
                case SProcConstants.TRN_DPS_ETY_COUNT_SHIP:
                case SProcConstants.TRN_DIOG_ETY_COUNT_SHIP:
                    proc.getParamsIn().add((int[]) pk);
                    break;
                default:
                    if (executionMode == SLibConstants.EXEC_MODE_SILENT) {
                        SLibUtilities.printOutException(SDataUtilities.class.getName(), new Exception(SProcConstants.MSG_ERR_PROC_NOT_FOUND));
                    }
                    else {
                        SLibUtilities.printOutException(SDataUtilities.class.getName(), new Exception(SProcConstants.MSG_ERR_PROC_NOT_FOUND));
                    }
            }

            request = new SServerRequest(SServerConstants.REQ_DB_PROCEDURE, proc);
            response = client.getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                if (executionMode == SLibConstants.EXEC_MODE_SILENT) {
                    SLibUtilities.printOutException(SDataUtilities.class.getName(), new Exception(SLibConstants.MSG_ERR_DB_STP));
                }
                else {
                    SLibUtilities.printOutException(SDataUtilities.class.getName(), new Exception(SLibConstants.MSG_ERR_DB_STP));
                }
            }
            else {
                proc = (SDataProcedure) response.getPacket();
            }
            count = (Integer) proc.getParamsOut().get(0);
        }

        return count;
    }

    /**
     * @param client ERP Client interface.
     * @param procType Process type. Constants defined in erp.data.SProcConstants.
     * @param inParams Vector of in-parameters.
     * @param executionMode  Execution mode. Constants defined in erp.lib.SLibConstants.
     * @return Vector of out-parameters.
     */
    public static java.util.Vector<java.lang.Object> callProcedure(erp.client.SClientInterface client, int procType, java.util.Vector<java.lang.Object> inParams, int executionMode) {
        Vector<Object> outParams = new Vector<Object>();
        SServerRequest request = null;
        SServerResponse response = null;
        SDataProcedure proc = null;

        switch (procType) {
            case SProcConstants.USRU_USR_PSWD:
                proc = new SProcUserPassword();
                break;
            case SProcConstants.FIN_YEAR_PER_ST:
                proc = new SProcYearPeriodStatus();
                break;
            case SProcConstants.FIN_REC_NEW_ID:
                proc = new SProcRecordNewId();
                break;
            case SProcConstants.FIN_GET_BP_BAL:
                proc = new SProcGetBizPartnerBalance();
                break;
            case SProcConstants.ITMU_ITEM_STOCK:
                proc = new SProcGetItemStock();
                break;
            case SProcConstants.MFG_ORD_ITEM_QRY:
                proc = new SProcProductionOrderItemQuantity();
                break;
            case SProcConstants.MFG_LTIME_COB_GET:
                proc = new SProcLeadtimeCobGet();
                break;
            case SProcConstants.MFG_LTIME_CO_GET:
                proc = new SProcLeadtimeCoGet();
                break;
            case SProcConstants.MFG_BOM_EXP_GREQ:
                proc = new SProcBomExplotionMaterialsReq();
                break;
            case SProcConstants.MFG_ORD_UPD:
                proc = new SProcProductionOrderUpdate();
                break;
            case SProcConstants.MKT_PLIST_PRICE_FILTER:
                proc = new SProcFilterItem();
                break;
            case SProcConstants.FIN_ACC_BP_GET:
                proc = new SProcAccountBizPartnerGet();
                break;
            case SProcConstants.FIN_GET_CHECK_NEXT_NUM:
                proc = new SProcGetCheckNextNumber();
                break;
            case SProcConstants.FIN_GET_CHECK_WAL_COUNT:
                proc = new SProcGetCheckWalletCount();
                break;
            case SProcConstants.FIN_CHECK_CANCEL:
                proc = new SProcCheckCancel();
                break;
            case SProcConstants.FIN_ID_TP_REC:
                proc = new SProcGetRecordTypeId();
                break;
            case SProcConstants.FIN_ACC_ITEM:
                proc = new SProcGetAccountItemId();
                break;
            case SProcConstants.FIN_COB_BKC:
                proc = new SProcGetCompanyBranchBookKeepingCenterId();
                break;
            case SProcConstants.FIN_GET_ACC_REP_DPS_UNP:
                proc = new SProcGetAccountDpsWithBalance();
                break;
            case SProcConstants.FIN_GET_CHECK_NUM_REC:
                proc = new SProcGetCheckNumberRecord();
                break;
            case SProcConstants.FIN_GET_ID_CHECK_FMT:
                proc = new SProcGetIdCheckPrintingFormat();
                break;
            case SProcConstants.FIN_GET_ACC_FST_LEV:
                proc = new SProcGetAccountFirstLevel();
                break;
            case SProcConstants.TRN_STK_GET:
                proc = new SProcStockGet();
                break;
            case SProcConstants.TRN_STK_SFTY_GET:
                proc = new SProcStockSafetyGet();
                break;
            case SProcConstants.TRN_DSM_REF_BAL_GET:
                proc = new SProcDsmReferenceBalanceGet();
                break;
            case SProcConstants.TRN_GET_DPS_ETY_PEND_SUP:
                proc = new SProcGetDpsEntriesPendingSupply();
                break;
            case SProcConstants.TRN_GET_DPS_ETY:
                proc = new SProcGetDpsEntries();
                break;
            case SProcConstants.TRN_GET_DPS_ETY_SUP_INFO:
                proc = new SProcGetDpsEntrySuppliedInformation();
                break;
            case SProcConstants.TRN_DPS_ETY_DATE_VAL:
                proc = new SProcDpsEntryDateVal();
                break;
            case SProcConstants.TRN_GET_DPS_INFO:
                proc = new SProcGetDpsInformation();
                break;
            case SProcConstants.TRN_OPEN_CLOSE_DPS:
                proc = new SProcOpenCloseDps();
                break;
            case SProcConstants.TRN_DPS_UPD:
                proc = new SProcDpsUpdate();
                break;
            case SProcConstants.TRN_TP_DPS_CODE:
                proc = new SProcDpsTypeCode();
                break;
            case SProcConstants.TRN_DPS_BAL_GET:
                proc = new SProcDpsBalanceGet();
                break;
            case SProcConstants.TRN_DPS_NUM_BAL_GET:
                proc = new SProcDpsNumBalanceGet();
                break;
            case SProcConstants.TRN_DIOG_NUM_SER_VAL:
                proc = new SProcDiogNumSerVal();
                break;
            case SProcConstants.TRN_GET_QTY_DVRD:
                proc = new SProcGetQuantityDelivered();
                break;
            case SProcConstants.TRN_CTR_UPD_ST:
                proc = new SProcUpdateCtrStatus();
                break;
            case SProcConstants.TRN_GET_RM_BAL:
                proc = new SProcGetRawMaterialBalance();
                break;
            case SProcConstants.TRN_GET_ORD_RM_BAL:
                proc = new SProcGetOrderRawMaterialBalance();
                break;
            case SProcConstants.TRN_DPS_LOG_UPD:
                proc = new SProcUpdateDpsLogistics();
                break;
            case SProcConstants.TRN_STAMP_AVAILABLE:
                proc = new SProcStampsAvailables();
                break;
            case SProcConstants.TRN_BPS_LAST_PAY:
                proc = new SProcBizPartnerLastPayment();
                break;
            case SProcConstants.TRN_DIOG_UPD:
                proc = new SProcDiogUpdate();
                break;
            default:
                if (executionMode == SLibConstants.EXEC_MODE_SILENT) {
                    SLibUtilities.printOutException(SDataUtilities.class.getName(), new Exception(SProcConstants.MSG_ERR_PROC_NOT_FOUND));
                }
                else {
                    SLibUtilities.printOutException(SDataUtilities.class.getName(), new Exception(SProcConstants.MSG_ERR_PROC_NOT_FOUND));
                }
                break;
        }

        if (proc != null) {
            proc.getParamsIn().addAll(inParams);
            request = new SServerRequest(SServerConstants.REQ_DB_PROCEDURE, proc);
            response = client.getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                if (executionMode == SLibConstants.EXEC_MODE_SILENT) {
                    SLibUtilities.printOutException(SDataUtilities.class.getName(), new Exception(SLibConstants.MSG_ERR_DB_STP));
                }
                else {
                    SLibUtilities.renderException(SDataUtilities.class.getName(), new Exception(SLibConstants.MSG_ERR_DB_STP));
                }
            }
            else {
                proc = (SDataProcedure) response.getPacket();
            }
            outParams.addAll(proc.getParamsOut());
        }

        return outParams;
    }

    @SuppressWarnings("unchecked")
    public static double obtainExchangeRate(erp.client.SClientInterface client, int currencyId, java.util.Date date) throws java.lang.Exception {
        double rate = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT exc_rate FROM fin_exc_rate WHERE id_cur = " + currencyId + " AND " +
                "id_dt = '" + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(date) + "' AND b_del = FALSE ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            rate = resultSet.getDouble("exc_rate");
        }

        return rate;
    }

    /**
     * Obtains cash account balance at cut-off date.
     * @returns Returns an array of double, where:
     * index 0: balance in domestic currency.
     * index 1: balance in cash account provided currency.
     */
    @SuppressWarnings("unchecked")
    public static double[] obtainAccountCashBalance(erp.client.SClientInterface client, int entityCurrencyId, java.util.Date cutOffDate,
            java.lang.Object entityKey, java.lang.Object systemMoveTypeKey, java.lang.Object recordKey_n) throws java.lang.Exception {

        int year = SLibTimeUtilities.digestYear(cutOffDate)[0];
        double bal = 0;
        double balCy = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT SUM(re.debit - re.credit) AS f_bal, " +
                "SUM(IF(re.fid_cur <> " + entityCurrencyId + ", 0, re.debit_cur - re.credit_cur)) AS f_bal_cur " +
                "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                "r.id_year = " + year + " AND r.dt <= '" + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(cutOffDate) + "' AND r.b_del = FALSE AND re.b_del = FALSE AND " +
                "re.fid_ct_sys_mov_xxx = " + ((int[]) systemMoveTypeKey)[0] + " AND re.fid_tp_sys_mov_xxx = " + ((int[]) systemMoveTypeKey)[1] + " AND " +
                "re.fid_cob_n = " + ((int[]) entityKey)[0] + " AND re.fid_ent_n = " + ((int[]) entityKey)[1] + " " +
                (recordKey_n == null ? "" : "AND NOT (r.id_year = " + ((Object[]) recordKey_n)[0] + " AND r.id_per = " + ((Object[]) recordKey_n)[1] + " AND " +
                "r.id_bkc = " + ((Object[]) recordKey_n)[2] + " AND r.id_tp_rec = '" + ((Object[]) recordKey_n)[3] + "' AND r.id_num = " + ((Object[]) recordKey_n)[4] + ") ");

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            bal = resultSet.getObject("f_bal") == null ? 0d : resultSet.getDouble("f_bal");
            balCy = resultSet.getObject("f_bal_cur") == null ? 0d : resultSet.getDouble("f_bal_cur");
        }

        return new double[] { bal, balCy };
    }

    /**
     * Obtains cash account balance at cut-off date, updating current record entries.
     * @returns Returns an array of double, where:
     * index 0: balance in domestic currency.
     * index 1: balance in cash account provided currency.
     */
    @SuppressWarnings("unchecked")
    public static double[] obtainAccountCashBalanceUpdated(erp.client.SClientInterface client, int entityCurrencyId, java.util.Date cutOffDate, java.lang.Object entityKey, 
            java.lang.Object systemMoveTypeKey, erp.mfin.data.SDataRecord record, erp.mfin.data.SDataRecordEntry currentRecordEntry) throws java.lang.Exception {

        double[] balance = obtainAccountCashBalance(client, entityCurrencyId, cutOffDate, entityKey, systemMoveTypeKey, record.getPrimaryKey());

        for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
            if ((currentRecordEntry == null || !SLibUtilities.compareKeys(currentRecordEntry.getPrimaryKey(), entry.getPrimaryKey())) &&
                    (!entry.getIsDeleted() && entry.getFkSystemMoveCategoryIdXXX() == SDataConstantsSys.FINS_CT_SYS_MOV_CASH &&
                    entry.getFkCompanyBranchId_n() == record.getFkCompanyBranchId_n() &&
                    entry.getFkEntityId_n() == record.getFkAccountCashId_n())) {

                /*
                 * If form's entry is new or iterator's entry is different from form's entry, and
                 * iterator's entry belongs to record's cash account, update account cash balance:
                 */

                balance[0] += (entry.getDebit() - entry.getCredit());
                balance[1] += (entry.getFkCurrencyId() != entityCurrencyId ? 0d : entry.getDebitCy() - entry.getCreditCy());
            }
        }

        return balance;
    }

    /**
     * @param client ERP Client interface.
     * @param keyDps Purchases-Sales Document key.
     * @param year Fiscal year.
     * @returns Returns an array of double, where:
     * index 0: balance in domestic currency.
     * index 1: balance in document's currency.
     */
    @SuppressWarnings("unchecked")
    public static double[] obtainDpsBalance(erp.client.SClientInterface client, int[] dpsKey, int year) throws java.lang.Exception {
        return obtainDpsBalance(client, dpsKey, year, null);
    }

    /**
     * @param client ERP Client interface.
     * @param keyDps Purchases-Sales Document key.
     * @param year Fiscal year.
     * @param dateCutOff_n If needed, cut-off date.
     * @returns Returns an array of double, where:
     * index 0: balance in domestic currency.
     * index 1: balance in document's currency.
     */
    @SuppressWarnings("unchecked")
    public static double[] obtainDpsBalance(erp.client.SClientInterface client, int[] dpsKey, int year, java.util.Date dateCutOff_n) throws java.lang.Exception {
        double bal = 0;
        double balCy = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT d.fid_ct_dps, " +
                "SUM(re.debit - re.credit) AS f_bal_d, " +
                "SUM(IF(re.fid_cur <> d.fid_cur, 0, re.debit_cur - re.credit_cur)) AS f_bal_d_cur, " +
                "SUM(re.credit - re.debit) AS f_bal_c, " +
                "SUM(IF(re.fid_cur <> d.fid_cur, 0, re.credit_cur - re.debit_cur)) AS f_bal_c_cur " +
                "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                "r.id_year = " + year + " " + (dateCutOff_n == null ? "" : "AND r.dt <= '" + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(dateCutOff_n) + "' ") + " AND " +
                "re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_CT_SYS_MOV_BPS + " AND re.fid_dps_year_n = " + dpsKey[0] + " AND re.fid_dps_doc_n = " + dpsKey[1] + " AND r.b_del = 0 AND re.b_del = 0 " +
                "INNER JOIN trn_dps AS d ON " +
                "re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc " +
                "GROUP BY d.fid_ct_dps ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            if (resultSet.getInt("d.fid_ct_dps") == SDataConstantsSys.TRNS_CT_DPS_SAL) {
                bal = resultSet.getDouble("f_bal_d");
                balCy = resultSet.getDouble("f_bal_d_cur");
            }
            else {
                bal = resultSet.getDouble("f_bal_c");
                balCy = resultSet.getDouble("f_bal_c_cur");
            }
        }

        return new double[] { bal, balCy };
    }

    @SuppressWarnings("unchecked")
    public static int obtainNextNumberForDps(erp.client.SClientInterface client, java.lang.String series, java.lang.Object docClassTypeKey) throws java.lang.Exception {
        int number = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT MAX(CONVERT(num, UNSIGNED INTEGER)) + 1 AS f_num FROM trn_dps " +
                "WHERE fid_ct_dps = " + ((int[]) docClassTypeKey)[0] + " AND fid_cl_dps = " + ((int[]) docClassTypeKey)[1] + " AND " +
                (((int[]) docClassTypeKey).length <= 2 ? "" : "fid_tp_dps = " + ((int[]) docClassTypeKey)[2] + " AND ") +
                "num_ser = '" + series + "' AND b_del = 0 ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            if (resultSet.getObject("f_num") != null) {
                number = resultSet.getInt("f_num");
            }
        }

        return number;
    }

    @SuppressWarnings("unchecked")
    public static int obtainNextNumberForCtr(erp.client.SClientInterface client) throws java.lang.Exception {
        int number = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COALESCE(MAX(CONVERT(num, UNSIGNED INTEGER)), 0) + 1 AS f_num FROM trn_ctr " +
                "WHERE b_del = 0 ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            if (resultSet.getObject("f_num") != null) {
                number = resultSet.getInt("f_num");
            }
        }

        return number;
    }

    @SuppressWarnings("unchecked")
    public static java.lang.String obtainNextItemCode(erp.client.SClientInterface client, int pkIgen, java.lang.String codeBase, java.lang.String code, int max) throws java.lang.Exception {
        int number = 0;
        int len = 0;
        String key = "";
        String zeros = "";
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT MAX(CONVERT(REPLACE(item_key, '" + codeBase + code + "', ''), UNSIGNED INTEGER)) + 1 AS f_num,  " +
                "LENGTH(REPLACE(item_key, '" + codeBase + code + "', '')) AS f_len " +
                "FROM erp.itmu_item " +
                "WHERE fid_igen = " + pkIgen + " AND item_key LIKE '" + codeBase + code + "%' " +
                " AND b_del = 0 " + (max == 0 ? "" : "AND CONVERT(REPLACE(item_key, '" + codeBase + code + "', ''), UNSIGNED INTEGER) < "
                + max + " ");

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            if (resultSet.getObject("f_num") != null) {
                number = resultSet.getInt("f_num");
            }
            if (resultSet.getObject("f_len") != null) {
                len = resultSet.getInt("f_len");
            }
        }

        key = ((Integer) number).toString();

        if (key.length() < len) {
            for (int i = key.length(); i < len; i++) {
                zeros += '0';
            }
        }

        return zeros + key;
    }

    @SuppressWarnings("unchecked")
    public static int[] obtainDpsKey(erp.client.SClientInterface client, java.lang.String series, java.lang.String number, java.lang.Object docClassTypeKey) throws java.lang.Exception {
        return obtainDpsKeyForBizPartner(client, series, number, docClassTypeKey, null);
    }
/**
 * 
 * @param client
 * @param serieIni Initial range value for Serial document (String)
 * @param serieEnd Final range value for serial document (String)
 * @param folioIni Initial 
 * @param folioEnd
 * @param date
 * @param docClassTypeKey
 * @return
 * @throws Exception 
 */
    public static ArrayList<int[]> obtainDpsIds(final SClientInterface client, String serieIni, String serieEnd, String folioIni, String folioEnd, Date date, java.lang.Object docClassTypeKey) throws Exception {
        int[] pk = new int[2];
        String sql = "";
        ResultSet resultSet = null;
        ArrayList<int[]> key = null;
        
        key = new ArrayList<>();
        
        if (!serieIni.isEmpty() && !serieEnd.isEmpty() && !folioIni.isEmpty() && !folioEnd.isEmpty()) {
            sql = "SELECT id_year, id_doc "
                + "FROM trn_dps WHERE NOT b_del "
                + "AND num_ser >= '" + serieIni + "' AND num_ser <= '" + serieEnd + "' " 
                + "AND num >= '" + folioIni + "' "
                + "AND num <= '" + folioEnd + "' AND fid_ct_dps = " + ((int[]) docClassTypeKey)[0] + " AND fid_cl_dps = " + ((int[]) docClassTypeKey)[1] + " AND " +
                (((int[]) docClassTypeKey).length <= 2 ? "" : "fid_tp_dps = " + ((int[]) docClassTypeKey)[2] + " ") 
                + "AND dt = '" + SLibUtils.DbmsDateFormatDate.format(date) + "';";
        }
        else if (serieIni.compareTo("") == 0 && serieEnd.compareTo("") == 0) {
            sql = "SELECT id_year, id_doc "
                + "FROM trn_dps WHERE NOT b_del "
                + "AND num >= '" + folioIni + "' "
                + "AND num <= '" + folioEnd + "' AND fid_ct_dps = " + ((int[]) docClassTypeKey)[0] + " AND fid_cl_dps = " + ((int[]) docClassTypeKey)[1] + " AND " +
                (((int[]) docClassTypeKey).length <= 2 ? "" : "fid_tp_dps = " + ((int[]) docClassTypeKey)[2] + " ") 
                + "AND dt = '" + SLibUtils.DbmsDateFormatDate.format(date) + "';";
        }

        resultSet = client.getSession().getStatement().executeQuery(sql);
        
        while (resultSet.next()) {
            pk = new int[2];
            pk[0] = resultSet.getInt("id_year");
            pk[1] = resultSet.getInt("id_doc");
            key.add(pk);
        }
        return key;
    }
        
    @SuppressWarnings("unchecked")
    public static int[] obtainDpsKeyForBizPartner(erp.client.SClientInterface client, java.lang.String series, java.lang.String number, java.lang.Object docClassTypeKey, java.lang.Object bizPartnerKey_n) throws java.lang.Exception {
        int[] key = null;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_year, id_doc FROM trn_dps " +
                "WHERE fid_ct_dps = " + ((int[]) docClassTypeKey)[0] + " AND fid_cl_dps = " + ((int[]) docClassTypeKey)[1] + " AND " +
                (((int[]) docClassTypeKey).length <= 2 ? "" : "fid_tp_dps = " + ((int[]) docClassTypeKey)[2] + " AND ") +
                "num_ser = '" + series + "' AND num = '" + number + "' AND b_del = 0 " +
                (bizPartnerKey_n == null ? "" : "AND fid_bp_r = " + ((int[]) bizPartnerKey_n)[0] + " ") +
                "ORDER BY id_year, id_doc ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            key = new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_doc") };
        }

        return key;
    }

    /**
     * Obtains item price based in price list by customer or customer type.
     *
     * @param client SClientInterface.
     * @param pnBizPartnerId Biz ID.
     * @param pnBizPartnerBranchId Biz partner branch ID.
     * @param pnBizPartnerCategoryId Biz partener category ID.
     * @param pnBizPartnerTypeId Biz partner type ID.
     * @param pnDpsCategoryId Dps category ID.
     * @param ptDateDoc Document date.
     * @param pnItemId Item ID.
     * @param pnFkCurrencyId Currency id.
* @return double array with item price and discount percentage.
     */
    @SuppressWarnings("unchecked")
    public static SParamsItemPriceList obtainItemPrice(erp.client.SClientInterface client, int pnBizPartnerId, int pnBizPartnerBranchId, int pnBizPartnerCategoryId, int pnBizPartnerTypeId,
        int pnDpsCategoryId, java.util.Date ptDateDoc, int pnItemId, int pnFkCurrencyId) throws java.lang.Exception {

        String sql = "";
        ResultSet resultSet = null;

        SParamsItemPriceList paramsItemPriceList = new SParamsItemPriceList();

        sql = "SELECT " + SModSysConsts.BPSS_LINK_CUS_MKT_TP + " AS f_type, lc.id_dt_start AS f_date, l.id_plist, " +
            "CASE WHEN lc.fid_tp_disc_app = " + SDataConstantsSys.MKTS_TP_DISC_APP_PRICE_U + " THEN " +
            "p.price * (1 - lc.disc_per) ELSE p.price END AS f_price_u, " +
            "CASE WHEN lc.fid_tp_disc_app = " + SDataConstantsSys.MKTS_TP_DISC_APP_DISC_U + " THEN " +
            "p.price * lc.disc_per ELSE 0 END AS f_disc_u, l.fid_cur " +
            "FROM mkt_plist_bp_link AS lc " +
            "INNER JOIN mkt_plist AS l ON " +
            "lc.id_link = " + SModSysConsts.BPSS_LINK_CUS_MKT_TP + " AND lc.fid_plist = l.id_plist AND lc.b_del = 0 AND l.b_del = 0 AND " +
            "lc.id_dt_start <= '" + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(ptDateDoc) + "' " + " AND l.fid_ct_dps = " + pnDpsCategoryId + " " +
            "INNER JOIN mkt_cfg_cus AS cc ON " +
            "lc.id_ref_1 = cc.fid_tp_cus AND cc.b_del = 0 AND cc.id_cus = " + pnBizPartnerId + " " +
            "INNER JOIN mkt_plist_price AS p ON " +
            "l.id_plist = p.id_plist AND p.id_item = " + pnItemId + " " +
            "INNER JOIN erp.cfgu_cur AS c ON " +
            "l.fid_cur = c.id_cur AND l.fid_cur = " + pnFkCurrencyId + " " +
            "UNION " +
            "SELECT " + SModSysConsts.BPSS_LINK_BP_TP + " AS f_type, lc.id_dt_start AS f_date, l.id_plist, " +
            "CASE WHEN lc.fid_tp_disc_app = " + SDataConstantsSys.MKTS_TP_DISC_APP_PRICE_U + " THEN " +
            "p.price * (1 - lc.disc_per) ELSE p.price END AS f_price_u, " +
            "CASE WHEN lc.fid_tp_disc_app = " + SDataConstantsSys.MKTS_TP_DISC_APP_DISC_U + " THEN " +
            "p.price * lc.disc_per ELSE 0 END AS f_disc_u, l.fid_cur " +
            "FROM mkt_plist_bp_link AS lc " +
            "INNER JOIN mkt_plist AS l ON " +
            "lc.id_link = " + SModSysConsts.BPSS_LINK_BP_TP + " AND lc.id_ref_1 = " + pnBizPartnerCategoryId + " AND lc.id_ref_2 = " + pnBizPartnerTypeId + " AND " +
            "lc.fid_plist = l.id_plist AND lc.b_del = 0 AND l.b_del = 0 AND " +
            "lc.id_dt_start <= '" + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(ptDateDoc) + "' " + " AND l.fid_ct_dps = " + pnDpsCategoryId + " " +
            "INNER JOIN erp.bpsu_bp_ct AS cc ON " +
            "cc.b_del = 0 AND cc.id_ct_bp = " + pnBizPartnerCategoryId + " AND " +
            "cc.id_bp = " + pnBizPartnerId + " " +
            "INNER JOIN mkt_plist_price AS p ON " +
            "l.id_plist = p.id_plist AND p.id_item = " + pnItemId + " " +
            "INNER JOIN erp.cfgu_cur AS c ON " +
            "l.fid_cur = c.id_cur AND l.fid_cur = " + pnFkCurrencyId + " " +
            "UNION " +  
            "SELECT " + SModSysConsts.BPSS_LINK_BP + " AS f_type, lc.id_dt_start AS f_date, l.id_plist, " +
            "CASE WHEN lc.fid_tp_disc_app = " + SDataConstantsSys.MKTS_TP_DISC_APP_PRICE_U + " THEN " +
            "p.price * (1 - lc.disc_per) ELSE p.price END AS f_price_u, " +
            "CASE WHEN lc.fid_tp_disc_app = " + SDataConstantsSys.MKTS_TP_DISC_APP_DISC_U + " THEN " +
            "p.price * lc.disc_per ELSE 0 END AS f_disc_u, l.fid_cur " +
            "FROM mkt_plist_bp_link AS lc " +
            "INNER JOIN mkt_plist AS l ON " +
            "lc.id_link = " + SModSysConsts.BPSS_LINK_BP + " AND lc.fid_plist = l.id_plist AND lc.b_del = 0 AND l.b_del = 0 AND " +
            "lc.id_dt_start <= '" + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(ptDateDoc) + "' AND " +
            "lc.id_ref_1 = " + pnBizPartnerId + " AND l.fid_ct_dps = " + pnDpsCategoryId + " " +
            "INNER JOIN mkt_plist_price AS p ON " +
            "l.id_plist = p.id_plist AND p.id_item = " + pnItemId + " " +
            "INNER JOIN erp.cfgu_cur AS c ON " +
            "l.fid_cur = c.id_cur AND l.fid_cur = " + pnFkCurrencyId + " " +
            "UNION " +  
            "SELECT " + SModSysConsts.BPSS_LINK_BPB + " AS f_type, lc.id_dt_start AS f_date, l.id_plist, " +
            "CASE WHEN lc.fid_tp_disc_app = " + SDataConstantsSys.MKTS_TP_DISC_APP_PRICE_U + " THEN " +
            "p.price * (1 - lc.disc_per) ELSE p.price END AS f_price_u, " +
            "CASE WHEN lc.fid_tp_disc_app = " + SDataConstantsSys.MKTS_TP_DISC_APP_DISC_U + " THEN " +
            "p.price * lc.disc_per ELSE 0 END AS f_disc_u, l.fid_cur " +
            "FROM mkt_plist_bp_link AS lc " +
            "INNER JOIN mkt_plist AS l ON " +
            "lc.id_link = " + SModSysConsts.BPSS_LINK_BPB + " AND lc.fid_plist = l.id_plist AND lc.b_del = 0 AND l.b_del = 0 AND " +
            "lc.id_dt_start <= '" + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(ptDateDoc) + "' AND " +
            "lc.id_ref_1 = " + pnBizPartnerId + " AND lc.id_ref_2 = " + pnBizPartnerBranchId + " AND l.fid_ct_dps = " + pnDpsCategoryId + " " +
            "INNER JOIN mkt_plist_price AS p ON " +
            "l.id_plist = p.id_plist AND p.id_item = " + pnItemId + " " +
            "INNER JOIN erp.cfgu_cur AS c ON " +
            "l.fid_cur = c.id_cur AND l.fid_cur = " + pnFkCurrencyId + " " +    
            "ORDER BY f_type DESC, f_date DESC, id_plist; ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            paramsItemPriceList.setItemPriceFound(true);
            paramsItemPriceList.setItemPrice(resultSet.getObject("f_price_u") == null ? 0 : resultSet.getDouble("f_price_u"));
            paramsItemPriceList.setItemDiscount(resultSet.getObject("f_disc_u") == null ? 0 : resultSet.getDouble("f_disc_u"));
            paramsItemPriceList.setCurrencyId(resultSet.getObject("fid_cur") == null ? 0 : resultSet.getInt("fid_cur"));
        }

        return paramsItemPriceList;
    }

    /**
     * Obtains backorder for a item
     *
     * @param client SClientInterface.
     * @param pnCtDpsId Category Dps ID.
     * @param pnCobId Company branch ID.
     * @param pnItemId Item ID.
     * @param ptDateCut Cut date.
     * @return backorder quantity.
     */
    @SuppressWarnings("unchecked")
    public static double obtainBackorderItem(erp.client.SClientInterface client, int pnCtDpsId, int pnCobId, int pnItemId, java.util.Date ptDateCut) throws java.lang.Exception {

        double dBackorder = 0;
        String sql = "";
        ResultSet resultSet = null;
        String sqlCompanyBranch = (pnCobId > 0 ? "AND d.fid_cob = " + pnCobId : "");
        String sqlCompanyBranchSubquery = (pnCobId > 0 ? "AND t.fid_cob = " + pnCobId : "");

        sql = "SELECT SUM(de.qty - " +
                "(SELECT COALESCE(SUM(s.qty), 0) " +
                "FROM trn_dps_dps_supply AS s " +
                "INNER JOIN trn_dps AS t ON s.id_des_year = t.id_year AND s.id_des_doc = t.id_doc " +
                "INNER JOIN trn_dps_ety AS te ON s.id_des_year = te.id_year AND s.id_des_doc = te.id_doc AND s.id_des_ety = te.id_ety " +
                "WHERE s.id_src_year = de.id_year AND s.id_src_doc = de.id_doc AND s.id_src_ety = de.id_ety AND " +
                "t.b_del = FALSE AND te.b_del = FALSE AND d.dt <= '" + ptDateCut + "' " + sqlCompanyBranchSubquery + ")) AS f_qty_pend " +
                "FROM trn_dps AS d " +
                "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND " +
                "d.fid_ct_dps = " + (pnCtDpsId == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[0]) +
                " AND d.fid_cl_dps = " + (pnCtDpsId == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[1]) +
                " AND d.fid_tp_dps = " + (pnCtDpsId == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[2]) + " " +
                "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                "WHERE d.b_del = FALSE AND de.b_del = FALSE AND de.fid_item = " + pnItemId + " AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_st_dps_val = " +
                SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " AND d.b_link = FALSE AND d.dt <= '" + ptDateCut + "' " + sqlCompanyBranch + " " +
                // "AND YEAR(d.dt) = " +  SLibTimeUtilities.digestYear(ptDateCut)[0] +
                "HAVING f_qty_pend > 0 ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            dBackorder = resultSet.getObject("f_qty_pend") == null ? 0d : resultSet.getDouble("f_qty_pend");
        }

        return dBackorder;
    }

    /**
     * Check if explotion materials can be created.
     *
     * @param client SClientInterface.
     * @param pnOrdYearIdStart Start production order year id.
     * @param pnOrdIdStart Start production order id.
     * @param pnOrdYearIdEnd End production order year id.
     * @param pnOrdIdEnd End production order id.
     * @return message with information if explotion of materials can be created, if message is empty explotion of materials can be created.
     */
    @SuppressWarnings("unchecked")
    public static java.lang.Object[] checkExplotionMaterials(erp.client.SClientInterface client, int pnOrdYearIdStart, int pnOrdIdStart, int pnOrdYearIdEnd, int pnOrdIdEnd) throws java.lang.Exception {
        boolean bExpMat = false;
        int nExpYearId = 0;
        int nExpId = 0;
        int nOrdYearIdStart = 0;
        int nOrdIdStart = 0;
        int nOrdYearIdEnd = 0;
        int nOrdIdEnd = 0;
        String sExplotionMaterials = "";
        String sMsg = "";
        String sOrder = "";
        String sRequisition = "";
        String sql = "";
        ResultSet resultSet = null;

        // A. Check if exist the range of production orders in 'mfg_exp_ord' table:

        sql = "SELECT MIN(eo.id_ord_year), MIN(eo.id_ord), MAX(eo.id_ord_year), MAX(eo.id_ord), eo.id_exp_year, eo.id_exp " +
            "FROM mfg_exp_ord AS eo " +
            "INNER JOIN mfg_exp AS e ON eo.id_exp_year = e.id_year AND eo.id_exp = e.id_exp AND e.b_del = 0 " +
            "INNER JOIN mfg_ord AS o ON eo.id_ord_year = o.id_year AND eo.id_ord = o.id_ord AND o.b_del = 0 " +
            "WHERE eo.id_exp_year = (SELECT MAX(id_exp_year) FROM mfg_exp_ord WHERE id_ord_year = " + pnOrdYearIdStart + " AND id_ord = " + pnOrdIdStart + ") AND " +
            "eo.id_exp = (SELECT MAX(id_exp) FROM mfg_exp_ord WHERE id_ord_year = " + pnOrdYearIdStart + " AND id_ord = " + pnOrdIdStart + ")";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            nOrdYearIdStart = resultSet.getObject(1) == null ? 0 : resultSet.getInt(1);
            nOrdIdStart = resultSet.getObject(2) == null ? 0 : resultSet.getInt(2);
            nOrdYearIdEnd = resultSet.getObject(3) == null ? 0 : resultSet.getInt(3);
            nOrdIdEnd = resultSet.getObject(4) == null ? 0 : resultSet.getInt(4);
            nExpYearId = resultSet.getObject(5) == null ? 0 : resultSet.getInt(5);
            nExpId = resultSet.getObject(6) == null ? 0 : resultSet.getInt(6);

            if ((nOrdYearIdStart == pnOrdYearIdStart && nOrdIdStart == pnOrdIdStart && nOrdYearIdEnd == pnOrdYearIdEnd && nOrdIdEnd == pnOrdIdEnd) ||
                    (nOrdYearIdStart == 0 && nOrdIdStart == 0 && nOrdYearIdEnd == 0 && nOrdIdEnd == 0)) {
                bExpMat = true;
            }
        }

        // B. If the range doesn't exist:

        if(!bExpMat) {

            // B.1. Check each production order of the range, if it exist in 'mfg_exp_ord' table:

            sql = "SELECT eo.id_ord_year, eo.id_ord, o.ref, e.ref, erp.lib_fix_int(o.id_ord,6) " +
            "FROM mfg_exp_ord AS eo " +
            "INNER JOIN mfg_exp AS e ON eo.id_exp_year = e.id_year AND eo.id_exp = e.id_exp AND e.b_del = FALSE " +
            "INNER JOIN mfg_ord AS o ON eo.id_ord_year = o.id_year AND eo.id_ord = o.id_ord AND o.b_del = FALSE " +
            "WHERE eo.id_ord_year >= " + pnOrdYearIdStart + " AND eo.id_ord_year <= " + pnOrdYearIdEnd + " AND eo.id_ord >= " + pnOrdIdStart + " AND eo.id_ord <= " + pnOrdIdEnd;

            bExpMat = true;

            resultSet = client.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                nOrdYearIdStart = resultSet.getObject(1) == null ? 0 : resultSet.getInt(1);
                nOrdIdStart = resultSet.getObject(2) == null ? 0 : resultSet.getInt(2);
                sOrder = resultSet.getObject(3) == null ? "" : (nOrdYearIdStart + "-" + resultSet.getString(5) + " " + resultSet.getString(3));
                sExplotionMaterials = resultSet.getObject(4) == null ? "" : resultSet.getString(4);
                bExpMat = true;
            }

            // B.2. If doesn´t exist each production order: (Can create explotion of materials record)

            if (!bExpMat) {
                sMsg = "";
            }
            else {
                // B.3. If exist each production order: (Send message to user informing that the order is used in another explotion)

                sMsg = "La orden de producción '" + sOrder + "' está siendo utilizada en la explosión de materiales '" + sExplotionMaterials + "'.";
            }
        }
        else {
            // C. If the range exist:

            // C.1. Check if explotion of materials record exist in 'mfg_exp_req' table:

            sql = "SELECT r.id_year, r.id_req, e.ref " +
            "FROM mfg_exp_req AS er " +
            "INNER JOIN mfg_exp AS e ON er.id_exp_year = e.id_year AND er.id_exp = e.id_exp AND e.b_del = FALSE AND e.id_year = " + nExpYearId + " AND e.id_exp = " + nExpId + " " +
            "INNER JOIN mfg_req AS r ON er.id_req_year = r.id_year AND er.id_req = r.id_req AND r.b_del = FALSE";

            bExpMat = false;

            resultSet = client.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                sRequisition = resultSet.getObject(1) == null ? "" : (resultSet.getInt(1) + "-" + resultSet.getLong(2));
                sExplotionMaterials = resultSet.getObject(3) == null ? "" : resultSet.getString(3);
                bExpMat = true;
            }

            // C.2. If explotion of materials record exist, then send message to the user: (Message: Explotion of materials has asigned a requisition)

            if (bExpMat) {
                sMsg = "La explosión de materiales '" + sExplotionMaterials + "' ya tiene requisición de materiales '" + sRequisition + "'.";
            }
            else {
                // C.3. If explotion of materials record doesn´t exist, then it can be created:

                sMsg = "";
            }
        }

        return new Object[] { sMsg, nExpYearId, nExpId };
    }

    /**
     * Obtain the last price fo the buy, by supplier and item:
     *
     * @param client SClientInterface.
     * @param pnItemId Item ID.
     * @param psBizPartnersIds Suppliers IDs.
     * @return supplier id, date docto, item total.
     */
    @SuppressWarnings("unchecked")
    public static java.lang.Object[] obtainLastPriceForSupplierItem(erp.client.SClientInterface client, int pnItemId, java.lang.String psBizPartnersIds) throws java.lang.Exception {
        int nBpId = 0;
        java.util.Date tDateDoc = null;
        double dItemPriceUnitaryCur = 0;
        double dItemDiscountUnitaryCur = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT d.fid_bp_r, d.dt_doc, de.price_u_cur, de.disc_u_cur " +
            "FROM trn_dps AS d " +
            "INNER JOIN trn_dps_ety AS de ON d.id_year = " + client.getSessionXXX().getWorkingYear() + " AND d.b_del = FALSE AND de.b_del = FALSE AND d.id_year = de.id_year AND " +
            "d.id_doc = de.id_doc AND d.fid_bp_r IN (" + psBizPartnersIds + ") AND de.fid_item = " + pnItemId + " AND " +
            "d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] + " AND " +
            "d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] + " AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
            "GROUP BY d.id_year, d.id_doc " +
            "ORDER BY de.tot_r ASC, d.dt_doc DESC ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            nBpId = resultSet.getObject(1) == null ? 0 : resultSet.getInt(1);
            tDateDoc = resultSet.getObject(2) == null ? client.getSessionXXX().getSystemDate() : resultSet.getDate(2);
            dItemPriceUnitaryCur = resultSet.getObject(3) == null ? 0d : resultSet.getDouble(3);
            dItemDiscountUnitaryCur = resultSet.getObject(4) == null ? 0d : resultSet.getDouble(4);
        }

        return new Object[] { nBpId, tDateDoc, dItemPriceUnitaryCur, dItemDiscountUnitaryCur };
    }

    /**
     * Ckeck if requisition materials by explotion materials has purchases orders:
     *
     * @param client SClientInterface.
     * @param pnPkYearExpId Explotion materials year ID.
     * @param pnPkExpId Explotion materials ID.
     * @return TRUE if requisition materiales by explotion materials has not purchases orders.
     */
    @SuppressWarnings("unchecked")
    public static boolean checkRequisitionMatExplotionMat(erp.client.SClientInterface client, int pnPkYearExpId, int pnPkExpId, boolean bOnlyExpMatReqExp) throws java.lang.Exception {
        boolean b = true;
        int[] nRequisition = null;
        Vector<int[]> vRequisition = new Vector<int[]>();
        String sql = "";
        ResultSet resultSet = null;

        // Check if explotion of materials has requisition of materials:

        sql = "SELECT id_req_year, id_req FROM mfg_exp_req WHERE id_exp_year = " + pnPkYearExpId + " AND id_exp = " + pnPkExpId + " ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            if ((resultSet.getObject("id_req_year") == null ? 0 : resultSet.getInt("id_req_year")) == 0 && (resultSet.getObject("id_req") == null ? 0 : resultSet.getInt("id_req")) == 0) {
            }
            else {
                vRequisition.add(new int[] { resultSet.getObject("id_req_year") == null ? 0 : resultSet.getInt("id_req_year"), resultSet.getObject("id_req") == null ? 0 : resultSet.getInt("id_req") } );
            }
        }

        // Check if must be check if requisition of materials has purchase order:

        if (!bOnlyExpMatReqExp) {

            // Check if requisition of materials has purchase order:

            for (int i=0; i<vRequisition.size(); i++) {

                nRequisition = vRequisition.get(i);
                if (!checkRequisitionMatPurchaseOrder(client, nRequisition[0], nRequisition[1])) {
                    b = false;
                    break;
                }

            }
        }
        else {

            // Check if vector vRequisition has members:

            if (vRequisition.size() > 0) {
                b = false;
            }
        }

        return b;
    }

    /**
     * Ckeck if requisition materials has purchases orders:
     *
     * @param client SClientInterface.
     * @param pnPkYearReqId Requisition materials year ID.
     * @param pnPkReqId Requisition materials ID.
     * @return TRUE if requisition materiales has not purchases orders.
     */
    @SuppressWarnings("unchecked")
    public static boolean checkRequisitionMatPurchaseOrder(erp.client.SClientInterface client, int pnPkYearReqId, int pnPkReqId) throws java.lang.Exception {
        boolean b = true;
        String sql = "";
        ResultSet resultSet = null;

        // Check if requisition of materials has purchase order:

        sql = "SELECT id_dps_year, id_dps_doc FROM mfg_req_pur WHERE id_req_year = " + pnPkYearReqId + " AND id_req = " + pnPkReqId + " ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            if ((resultSet.getObject("id_dps_year") == null ? 0 : resultSet.getInt("id_dps_year")) > 0 && (resultSet.getObject("id_dps_doc") == null ? 0 : resultSet.getInt("id_dps_doc")) > 0) {
                b = false;
            }
        }

        return b;
    }

    /**
     * Check if item is Bill Of Materials (BOM):
     *
     * @param client SClientInterface.
     * @param pnItemId Item ID.
     * @return bom id, bom ref, root.
     */
    @SuppressWarnings("unchecked")
    public static java.lang.Object[] isBillOfMaterials(erp.client.SClientInterface client, int pnItemId) throws java.lang.Exception {
        int nBomId = 0;
        int nBomRoot = 0;
        String sBomRef = "";
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_bom, bom, root, b_exp, fid_item_n FROM mfg_bom WHERE fid_item = " + pnItemId + " AND fid_item_n IS NULL ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            nBomId = resultSet.getObject("id_bom") == null ? 0 : resultSet.getInt("id_bom");
            sBomRef = resultSet.getObject("bom") == null ? "" : resultSet.getString("bom");
            nBomRoot = resultSet.getObject("root") == null ? 0 : resultSet.getInt("root");
        }

        return new Object[] { nBomId, sBomRef, nBomRoot };
    }

    /**
     * Obtain leadtime of supplier by item:
     *
     * @param client SClientInterface.
     * @param pnReqYearId Requisition year ID.
     * @param pnReqId Requisition ID.
     * @param pnItemId Item ID.
     * @return leadtime of supplier by item.
     */
    @SuppressWarnings("unchecked")
    public static java.util.Date obtainLeadtimeByItem(erp.client.SClientInterface client, int pnReqYearId, int pnReqId, int pnItemId) throws java.lang.Exception {
        Date tDateLeadtime = null;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT e.ts_ltime_n " +
                "FROM mfg_exp_ety AS e " +
                "WHERE e.id_year = (SELECT id_exp_year FROM mfg_exp_req WHERE id_req_year = " + pnReqYearId + " AND id_req = " + pnReqId + ") AND " +
                "e.id_exp = (SELECT id_exp FROM mfg_exp_req WHERE id_req_year = " + pnReqYearId + " AND id_req = " + pnReqId + ") AND " +
                "e.id_item = " + pnItemId + " ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            tDateLeadtime = resultSet.getObject("e.ts_ltime_n") == null ? null : resultSet.getDate("e.ts_ltime_n");
        }

        return tDateLeadtime;
    }

    /**
     * Show DLPS record.
     * @param client
     * @param dps 
     */
    public static void showDpsRecord(erp.client.SClientInterface client, erp.mtrn.data.SDataDps dps) {
        if (dps.getIsRecordAutomatic() && !dps.getAuxIsFormerRecordAutomatic() && dps.getDbmsRecordKey() != null) {
            Object[] key = (Object[]) dps.getDbmsRecordKey();

            client.showMsgBoxInformation("El documento ha sido guardado en la siguiente póliza contable:\n" +
                    "Fecha de la póliza: " + client.getSessionXXX().getFormatters().getDateFormat().format(dps.getDbmsRecordDate()) + "\n" +
                    "Período contable: " + key[0] + "-" + client.getSessionXXX().getFormatters().getMonthFormat().format(key[1]) + "\n" +
                    "Número de póliza: " + key[3] + "-" + key[4]);
        }
    }

    /**
     * Checks if business partner is blocked indirectly.
     * Business partners blocked this way, are blocked on purchases or sales SIIE modules, besides the catalog of business partners itself.
     *
     * @param client ERP Client interface.
     * @param idBizPartner Business partner primary key.
     * @param idBizPartnerCategory Business partner's category primary key.
     * @return <code>true</code> if business partner is blocked.
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    public static boolean obtainIsBizPartnerBlocked(erp.client.SClientInterface client, int idBizPartner, int idBizPartnerCategory) throws java.lang.Exception {
        boolean blocked = false;
        String sql = "SELECT b_block FROM trn_bp_block WHERE id_bp = " + idBizPartner + " AND id_ct_bp = " + idBizPartnerCategory + " AND NOT b_del ";

        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                blocked = resultSet.getBoolean("b_block");
            }
        }

        return blocked;
    }

    /**
     * Obtain if bizPartner is blocked by Dps:
     *
     * @param client SClientInterface.
     * @param dpsKey Key of Dps
     * @return boolean if bizPartner is blocked
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    public static boolean obtainIsBizPartnerBlockedByDps(erp.client.SClientInterface client, int[] dpsKey) throws java.lang.Exception {
        boolean blocked = false;

        String sql = "SELECT d.fid_ct_dps, sup.b_block AS f_block_sup, cus.b_block AS f_block_cus " +
                "FROM trn_dps AS d " +
                "LEFT OUTER JOIN trn_bp_block AS sup ON " +
                "d.fid_bp_r = sup.id_bp AND sup.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_SUP + " AND sup.b_del = 0 " +
                "LEFT OUTER JOIN trn_bp_block AS cus ON " +
                "d.fid_bp_r = cus.id_bp AND cus.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_CUS + " AND cus.b_del = 0 " +
                "WHERE d.id_year = " + dpsKey[0] + " AND d.id_doc = " + dpsKey[1] + " ";

        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                if (resultSet.getInt("d.fid_ct_dps") == SDataConstantsSys.TRNS_CT_DPS_PUR) {
                    if (resultSet.getObject("f_block_sup") != null) {
                        blocked = resultSet.getBoolean("f_block_sup");
                    }
                }
                else {
                    if (resultSet.getObject("f_block_cus") != null) {
                        blocked = resultSet.getBoolean("f_block_cus");
                    }
                }
            }
        }

        return blocked;
    }

    /**
     * Obtain shipment values by entry:
     *
     * @param client SClientInterface.
     * @param dpsEntryKey Key dps entry Id
     * @return values of shipment entry
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    public static double[] obtainShipmentByEntry(erp.client.SClientInterface client, int[] dpsEntryKey) throws java.lang.Exception {
        double[] adShip = null;

        String sql = "SELECT COALESCE(et.qty, 0), COALESCE(et.orig_qty, 0), COALESCE(SUM(se.qty), 0) AS f_ship_qty, COALESCE(SUM(se.orig_qty), 0) AS f_ship_orig_qty, " +
            "COALESCE(et.qty - SUM(se.qty), 0) AS f_pend_ship " +
            "FROM trn_dps AS d " +
            "INNER JOIN trn_dps_ety AS et ON d.id_year = et.id_year AND d.id_doc = et.id_doc AND et.b_del = 0 " +
            "INNER JOIN log_dps_ship_ety AS s ON et.id_year = s.id_dps_year AND et.id_doc = s.id_dps_doc AND et.id_ety = s.id_dps_ety " +
            "INNER JOIN log_ship_ety AS se ON s.id_ship_year = se.id_year AND s.id_ship_doc = se.id_doc AND s.id_ship_ety = se.id_ety " +
            "AND et.fid_item = se.fid_item_r AND et.fid_unit = se.fid_unit AND et.fid_orig_unit = se.fid_orig_unit " +
            "INNER JOIN log_ship AS sh ON se.id_year = sh.id_year AND se.id_doc = sh.id_doc AND sh.b_del = 0 " +
            "WHERE et.id_year = " + dpsEntryKey[0] + " AND et.id_doc = " + dpsEntryKey[1] + " AND et.id_ety = " + dpsEntryKey[2] + " ";

        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                if (resultSet.getObject(1) != null) {
                    adShip = new double[] {
                        resultSet.getObject(1) == null ? new Double(0) : resultSet.getDouble(1),
                        resultSet.getObject(2) == null ? new Double(0) : resultSet.getDouble(2),
                        resultSet.getObject(3) == null ? new Double(0) : resultSet.getDouble(3),
                        resultSet.getObject(4) == null ? new Double(0) : resultSet.getDouble(4),
                        resultSet.getObject(5) == null ? new Double(0) : resultSet.getDouble(5)};
                }
                else {
                    adShip = new double[] { new Double(0), new Double(0), new Double(0), new Double(0), new Double(0) };
                }
            }
        }

        return adShip;
    }

    /**
     * Obtain accounts for cost direct/indirect:
     *
     * @param client SClientInterface.
     * @param pnCompanyBranchId Key company branch entity
     * @return values for accouts cost
     */
    @SuppressWarnings("unchecked")
    public static java.lang.String[] obtainAccountsCosts(erp.client.SClientInterface client, int[] pnCompanyBranchId, boolean pbIsDirectCost) throws java.lang.Exception {
        java.lang.String[] asAccounts = null;

        String sql = "SELECT fid_acc_payroll, " + (pbIsDirectCost ? "fid_acc_expen_mfg" : "fid_acc_wp") + " FROM fin_acc_cob_ent WHERE id_cob = " + pnCompanyBranchId[0] + " AND id_ent = " + pnCompanyBranchId[1] + " AND b_del = 0; ";

        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                if (resultSet.getObject(1) != null) {
                    asAccounts = new java.lang.String[] {
                        resultSet.getObject(1) == null ? "" : resultSet.getString(1),
                        resultSet.getObject(2) == null ? "" : resultSet.getString(2) };
                }
                else {
                    asAccounts = new java.lang.String[] { "", "" };
                }
            }
        }

        return asAccounts;
    }
    
    @SuppressWarnings("unchecked")
    public static ArrayList<SDataAccount> obtainAccountsByType(erp.client.SClientInterface client, int[] pnTypeId) throws java.lang.Exception {
        java.lang.String[] asAccounts = null;
        SDataAccount account = null;
        ArrayList<SDataAccount> dataAccounts = new ArrayList<SDataAccount>();

        String sql = "SELECT am.id_acc "
                + "FROM fin_acc AS a "
                + "INNER JOIN fin_acc AS am ON a.id_acc = CONCAT(LEFT(am.id_acc, 4), '-0000-0000') "
                + "WHERE a.fid_tp_acc_r = " + pnTypeId[0] + " AND a.fid_cl_acc_r = " + pnTypeId[1] + " AND a.fid_cls_acc_r = " + pnTypeId[2] + " AND a.b_del = 0 AND am.b_del = 0; ";
        
        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                account = (SDataAccount) SDataUtilities.readRegistry(client, SDataConstants.FIN_ACC, new Object[] { resultSet.getString("id_acc") }, SLibConstants.EXEC_MODE_SILENT);
                
                dataAccounts.add(account);
            }
        }

        return dataAccounts;
    }

    /**
     * Obtain gic:
     *
     * @param client SClientInterface.
     * @return gic Id
     */
    @SuppressWarnings("unchecked")
    public static int obtainIndirectManufacturingCosts(erp.client.SClientInterface client) throws java.lang.Exception {
        int nPkGicId = 0;

        String sql = "SELECT id_cost_gic FROM erp.finu_cost_gic WHERE id_cost_gic = 1 AND b_del = 0; ";

        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                nPkGicId = resultSet.getObject("id_cost_gic") == null ? 0 : resultSet.getInt("id_cost_gic");
            }
        }

        return nPkGicId;
    }

    /**
     * Obtain agent types:
     *
     * @param client SClientInterface.
     * @return Vector<SDataCommissionsSalesAgent>
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    public static java.util.Vector<erp.mmkt.data.SDataCommissionsSalesAgent> obtainSalesAgent(erp.client.SClientInterface client) throws java.lang.Exception {
        Vector<SDataCommissionsSalesAgent> vCommissionsSalesAgent = new Vector<SDataCommissionsSalesAgent>();
        SDataCommissionsSalesAgent oCommissionsSalesAgent = null;

        String sql = "SELECT DISTINCT id_bp, bp " +
                "FROM erp.bpsu_bp " +
                "WHERE b_att_sal_agt = TRUE " +
                "ORDER BY bp, id_bp; ";

        ResultSet resultSet = client.getSession().getStatement().executeQuery(sql);
        while (resultSet.next()) {
            oCommissionsSalesAgent = new SDataCommissionsSalesAgent();
            oCommissionsSalesAgent.setPkSalesAgentId(resultSet.getInt("id_bp"));
            oCommissionsSalesAgent.setDbmsSalesAgent(resultSet.getString("bp"));

            vCommissionsSalesAgent.add(oCommissionsSalesAgent);
        }
        resultSet.close();

        return vCommissionsSalesAgent;
    }

    /**
     * Obtain sales agent types:
     *
     * @param client SClientInterface.
     * @return Vector<SDataCommissionsSalesAgentType>
     */
    @SuppressWarnings("unchecked")
    public static java.util.Vector<erp.mmkt.data.SDataCommissionsSalesAgentType> obtainSalesAgentType(erp.client.SClientInterface client) throws java.lang.Exception {
        Vector<SDataCommissionsSalesAgentType> vCommissionsSalesAgentType = new Vector<SDataCommissionsSalesAgentType>();
        SDataCommissionsSalesAgentType oCommissionsSalesAgentType = null;

        String sql = "SELECT * FROM mktu_tp_sal_agt WHERE b_del = 0; ";

        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                oCommissionsSalesAgentType = new SDataCommissionsSalesAgentType();
                oCommissionsSalesAgentType.setPkSalesAgentTypeId(resultSet.getInt(1));
                oCommissionsSalesAgentType.setDbmsSalesAgentType(resultSet.getString(2));
                
                vCommissionsSalesAgentType.add(oCommissionsSalesAgentType);
            }
        }

        return vCommissionsSalesAgentType;
    }

    /**
     * Obtain employees from gang:
     *
     * @param client SClientInterface.
     * @param nPkGangId int[]
     * @return Vector<Object>
     */
    @SuppressWarnings("unchecked")
    public static java.util.Vector<java.lang.Object[]> obtainEmployees(erp.client.SClientInterface client, int nPkGangId) throws java.lang.Exception {
        String sql = "";
        SServerRequest request = null;
        SServerResponse response = null;
        Vector<Object[]> oEmployees = new Vector<Object[]>();
        Vector<Vector<java.lang.Object>> rows = null;

        sql = "SELECT ge.fid_bp, b.bp " +
                "FROM mfgu_gang AS g " +
                "INNER JOIN mfgu_gang_ety AS ge ON g.id_gang = ge.id_gang AND g.b_del = 0 AND ge.b_del = 0 AND ge.id_gang = " + nPkGangId + " " +
                "INNER JOIN erp.bpsu_bp AS b ON ge.fid_bp = b.id_bp ";

        request = new SServerRequest(SServerConstants.REQ_DB_QUERY_SIMPLE, sql);
        response = client.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            throw new Exception(response.getMessage());
        }
        else {
            rows = (Vector<Vector<Object>>) response.getPacket();
            for (Vector<Object> row : rows) {
                if (row.get(0) != null) {
                    oEmployees.add(new Object[] { (int)((Long) row.get(0)).longValue(), ((String) row.get(1)).toString() });
                }
            }
        }

        return oEmployees;
    }

    /**
     * Obtain last lot by ítem:
     *
     * @param client SClientInterface.
     * @param nPkItemId int
     * @param nPkUnitId int
     * @return lot
     */
    @SuppressWarnings("unchecked")
    public static int obtainLotByItem(erp.client.SClientInterface client, int nPkItemId, int nPkUnitId) throws java.lang.Exception {
        int lotId = 0;

        String sql = "SELECT COALESCE(MAX(lot), 0) + 1 AS f_lot FROM trn_lot WHERE id_item = " + nPkItemId + " AND id_unit = " + nPkUnitId + " ";

        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                lotId = resultSet.getObject("f_lot") == null ? 0 : resultSet.getInt("f_lot");
            }
        }

        return lotId;
    }

    /**
     * Validate if production order hasn't lots:
     *
     * @param client SClientInterface.
     * @param pnPkIdYear Production Order 'id_year'
     * @param pnPkIdOrd Production Order 'id_ord'
     * @return sMessage
     */
    @SuppressWarnings("unchecked")
    public static java.lang.String validateProductionOrderLots(erp.client.SClientInterface client, int pnPkIdYear, int pnPkIdOrd) throws java.lang.Exception {
        String sMsg = "";

        String sql = "";
        SServerRequest request = null;
        SServerResponse response = null;
        Vector<Vector<Object>> rows = null;

        sql = "SELECT IF(((SELECT si.b_lot FROM mfg_ord AS so " +
            "INNER JOIN mfg_ord_chg AS sc ON so.id_year = sc.id_year AND so.id_ord = sc.id_ord " +
            "INNER JOIN mfg_ord_chg_ety AS sce ON sc.id_year = sce.id_year AND sc.id_ord = sce.id_ord AND sc.id_chg = sce.id_chg " +
            "LEFT OUTER JOIN erp.itmu_item AS si ON sce.fid_item_r = si.id_item AND sce.fid_unit_r = si.fid_unit " +
            "WHERE so.id_year = " + pnPkIdYear + " AND so.id_ord = " + pnPkIdOrd + " AND sce.fid_item_r = ce.fid_item_r AND sce.fid_unit_r = ce.fid_unit_r AND sce.id_chg = ce.id_chg " +
            "GROUP BY sce.fid_item_r, sce.fid_unit_r, sce.id_chg) = 1) &&(COALESCE(( " +
            "SELECT fi.b_lot FROM mfg_ord AS fo " +
            "INNER JOIN mfg_ord_chg AS fc ON fo.id_year = fc.id_year AND fo.id_ord = fc.id_ord " +
            "INNER JOIN mfg_ord_chg_ety AS fce ON fc.id_year = fce.id_year AND fc.id_ord = fce.id_ord AND fc.id_chg = fce.id_chg " +
            "LEFT OUTER JOIN mfg_ord_chg_ety_lot AS fcel ON fce.id_year = fcel.id_year AND fce.id_ord = fcel.id_ord AND fce.id_chg = fcel.id_chg AND fce.id_ety = fcel.id_ety " +
            "LEFT OUTER JOIN erp.itmu_item AS fi ON fcel.id_item = fi.id_item AND fcel.id_unit = fi.fid_unit " +
            "WHERE fo.id_year = " + pnPkIdYear + " AND fo.id_ord = " + pnPkIdOrd + " AND fce.fid_item_r = ce.fid_item_r AND fce.fid_unit_r = ce.fid_unit_r AND fce.id_chg = ce.id_chg " +
            "GROUP BY fce.fid_item_r, fce.fid_unit_r, fce.id_chg),0) = 0), 0, 1) AS f_lot, i.item_key, i.item, c.id_chg " +
            "FROM mfg_ord AS o " +
            "INNER JOIN mfg_ord_chg AS c ON o.id_year = c.id_year AND o.id_ord = c.id_ord " +
            "INNER JOIN mfg_ord_chg_ety AS ce ON c.id_year = ce.id_year AND c.id_ord = ce.id_ord AND c.id_chg = ce.id_chg AND ce.b_req = 1 " +
            "LEFT OUTER JOIN erp.itmu_item AS i ON ce.fid_item_r = i.id_item AND ce.fid_unit_r = i.fid_unit " +
            "WHERE o.id_year = " + pnPkIdYear + " AND o.id_ord = " + pnPkIdOrd + " " +
            "GROUP BY ce.fid_item_r, ce.fid_unit_r, ce.id_chg; ";

        request = new SServerRequest(SServerConstants.REQ_DB_QUERY_SIMPLE, sql);
        response = client.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            throw new Exception(response.getMessage());
        }
        else {
            rows = (Vector<Vector<Object>>) response.getPacket();
            for (Vector<Object> row : rows) {
                if (row.get(0) != null) {
                    if (((Number) row.get(0)).intValue() == 0) {
                        sMsg = "el ítem '" + ((String) row.get(1)).toString() + " - " + ((String) row.get(2)).toString() + "' de la carga no. '" + ((Number) row.get(3)).intValue() + "'";
                        break;
                    }
                }
            }
        }

        return sMsg;
    }

    /**
     * Validate if exist production order principal like ingredient in production order secundary:
     *
     * @param client SClientInterface.
     * @param nFkOrderPrincipalItemId int
     * @param nFkOrderPrincipalUnitId int
     * @param nFkOrderSecondaryItemId int
     * @param nFkOrderSecondaryUnitId int
     * @param sBom String
     * @return nPkItemId
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    public static int validateIngredientInFormula(erp.client.SClientInterface client, int nFkOrderPrincipalItemId, int nFkOrderPrincipalUnitId, int nFkOrderSecondaryItemId, int nFkOrderSecondaryUnitId, java.lang.String sBom) throws java.lang.Exception {
        int nPkItemId = 0;

        String sql = "SELECT MAX(b.fid_item_n) " +
            "FROM mfg_bom AS b " +
            "WHERE b.b_del = 0 AND b.fid_item = " + nFkOrderPrincipalItemId + " AND b.fid_unit = " + nFkOrderPrincipalUnitId + " AND b.bom = '" + sBom + "' " +
            "AND b.fid_item_n = " + nFkOrderSecondaryItemId + " AND b.fid_unit_n = " + nFkOrderSecondaryUnitId + "; ";

        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                nPkItemId = resultSet.getObject(1) == null ? 0 : resultSet.getInt(1);
            }
        }

        return nPkItemId;
    }

    /**
     * Validate if production order has inventory moves:
     *
     * @param client SClientInterface.
     * @param nFkYearId int
     * @param nFkOrderId int
     * @return total inventory moves
     */
    @SuppressWarnings("unchecked")
    public static java.lang.String checkProductionOrderExternalRelations(erp.client.SClientInterface client, int nFkYearId, int nFkOrderId) throws java.lang.Exception {
        int nCount = 0;
        String sMove = "";
        String sql = "";
        ResultSet resultSet = null;

        // Validate if production order has inventory moves(trn_diog):

        sql = "SELECT COUNT(*) " +
            "FROM trn_diog " +
            "WHERE fid_mfg_year_n = " + nFkYearId + " AND fid_mfg_ord_n = " + nFkOrderId + " AND b_del = 0; ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            if (resultSet.getObject(1) != null) {
                nCount = resultSet.getObject(1) == null ? 0 : resultSet.getInt(1);
                sMove = nCount > 0 ? "tiene movimientos de 'inventario'" : "";
            }
        }

        // Validate if production order has inventory moves(trn_diog_ety):

        if (sMove.length() == 0) {

            sql = "SELECT COUNT(*) " +
                "FROM trn_diog AS d " +
                "INNER JOIN trn_diog_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND d.b_del = 0 " +
                "WHERE de.fid_mfg_year_n = " + nFkYearId + " AND de.fid_mfg_ord_n = " + nFkOrderId + " AND de.b_del = 0; ";

            resultSet = client.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                if (resultSet.getObject(1) != null) {
                    nCount = resultSet.getObject(1) == null ? 0 : resultSet.getInt(1);
                    sMove = nCount > 0 ? "tiene movimientos de 'inventario'" : "";
                }
            }

            // Validate if production order has 'trn_dps' relation:

            if (sMove.length() == 0) {

                sql = "SELECT COUNT(*) " +
                    "FROM trn_dps " +
                    "WHERE fid_mfg_year_n = " + nFkYearId + " AND fid_mfg_ord_n = " + nFkOrderId + " AND b_del = 0; ";

                resultSet = client.getSession().getStatement().executeQuery(sql);
                if (resultSet.next()) {
                    if (resultSet.getObject(1) != null) {
                        nCount = resultSet.getObject(1) == null ? 0 : resultSet.getInt(1);
                        sMove = nCount > 0 ? "tiene movimientos de 'documento de ventas'" : "";
                    }
                }

                // Validate if production order has 'fin_rec_ety' relation:

                if (sMove.length() == 0) {

                    sql = "SELECT COUNT(*) " +
                        "FROM fin_rec AS r " +
                        "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num  " +
                        "WHERE re.fid_mfg_year_n = " + nFkYearId + " AND re.fid_mfg_ord_n = " + nFkOrderId + " AND re.b_del = 0; ";

                    resultSet = client.getSession().getStatement().executeQuery(sql);
                    if (resultSet.next()) {
                        if (resultSet.getObject(1) != null) {
                            nCount = resultSet.getObject(1) == null ? 0 : resultSet.getInt(1);
                            sMove = nCount > 0 ? "tiene movimientos de 'póliza contable'" : "";
                        }
                    }
                }
            }
        }

        return sMove;
    }

    /**
     * Validate if raw material of the charge has substitute
     *
     * @param client SClientInterface.
     * @param nPkBomId int
     * @param nPkItemId int
     * @param nPkUnitId int
     * @return total of substitutes
     */
    @SuppressWarnings("unchecked")
    public static int checkRawMaterialSubstitute(erp.client.SClientInterface client, int nPkBomId, int nPkItemId, int nPkUnitId) throws java.lang.Exception {
        int nCount = 0;
        String sql = "";
        SServerRequest request = null;
        SServerResponse response = null;
        Vector<Vector<Object>> rows = null;

        sql = "SELECT COUNT(*) " +
            "FROM mfg_bom_sub " +
            "WHERE id_bom = " + nPkBomId + " AND id_item = " + nPkItemId + " AND id_unit " + nPkUnitId + " AND b_del = 0; ";

        request = new SServerRequest(SServerConstants.REQ_DB_QUERY_SIMPLE, sql);
        response = client.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            throw new Exception(response.getMessage());
        }
        else {
            rows = (Vector<Vector<Object>>) response.getPacket();
            for (Vector<Object> row : rows) {
                if (row.get(0) != null) {
                    nCount = row.get(0) == null ? 0 : ((Number) row.get(0)).intValue();
                }
            }
        }

        return nCount;
    }

    /**
     * Obtain entries for purchase order with information of shipment
     *
     * @param client SClientInterface.
     * @param dPrice double
     * @param dManeuever double
     * @param dStay double
     * @param dOther double
     * @return dps entries ids
     */
    @SuppressWarnings("unchecked")
    public static Vector<java.lang.Object[]> obtainDpsEntriesIdsForShipment(erp.client.SClientInterface client, double dPrice, double dManeuever, double dStay, double dOther) throws java.lang.Exception {
        String sql = "";
        ResultSet resultSet = null;
        Vector<Object[]> vDpsEntries = new Vector<Object[]>();

        sql = "SELECT fid_item_log_delivery_n, fid_item_log_maneuever_n, fid_item_log_stay_n, fid_item_log_other_n " +
            "FROM cfg_param_co";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {

            // Logistic delivery:

            if (resultSet.getObject("fid_item_log_delivery_n") != null) {
                vDpsEntries.add(new Object[] { (resultSet.getObject("fid_item_log_delivery_n") == null ? 0 : resultSet.getInt("fid_item_log_delivery_n")), dPrice});
            }

            // Logistic maneuever:

            if (resultSet.getObject("fid_item_log_maneuever_n") != null) {
                vDpsEntries.add(new Object[] { (resultSet.getObject("fid_item_log_maneuever_n") == null ? 0 : resultSet.getInt("fid_item_log_maneuever_n")), dManeuever});
            }

            // Logistic stay:

            if (resultSet.getObject("fid_item_log_stay_n") != null) {
                vDpsEntries.add(new Object[] { (resultSet.getObject("fid_item_log_stay_n") == null ? 0 : resultSet.getInt("fid_item_log_stay_n")), dStay});
            }

            // Logistic other:

            if (resultSet.getObject("fid_item_log_other_n") != null) {
                vDpsEntries.add(new Object[] { (resultSet.getObject("fid_item_log_other_n") == null ? 0 : resultSet.getInt("fid_item_log_other_n")), dOther});
            }
        }

        return vDpsEntries != null ? vDpsEntries : null;
    }

    /**
     * Validate if order (dps) has links with dps document:
     *
     * @param client SClientInterface.
     * @param nPkYearId int
     * @param nPkDocId int
     * @return total of links
     */
    @SuppressWarnings("unchecked")
    public static int checkDpsOrderLinks(erp.client.SClientInterface client, int nPkYearId, int nPkDocId) throws java.lang.Exception {
        int nCount = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COUNT(*) AS f_count " +
            "FROM trn_dps_dps_supply AS s " +
            "INNER JOIN trn_dps AS d ON s.id_src_year = d.id_year AND s.id_src_doc = d.id_doc AND d.b_del = 0 " +
            "INNER JOIN trn_dps_ety AS e ON d.id_year = e.id_year AND d.id_doc = e.id_doc AND e.b_del = 0 " +
            "WHERE s.id_src_year = " + nPkYearId + " AND s.id_src_doc = " + nPkDocId + " " +
            "GROUP BY s.id_src_year, s.id_src_doc; ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            if (resultSet.getObject("f_count") != null) {
                nCount = resultSet.getObject("f_count") == null ? 0 : resultSet.getInt("f_count");
            }
        }

        return nCount;
    }

    /**
     * Validate if order (dps) has links with dps document:
     *
     * @param client SClientInterface.
     * @param nPkCobId int
     * @param nPkWhId int
     * @param nPkDnsId int
     * @return total of dns
     */
    @SuppressWarnings("unchecked")
    public static int checkStockConfigDns(erp.client.SClientInterface client, int nPkCobId, int nPkWhId, int nPkDnsId) throws java.lang.Exception {
        int nCount = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COUNT(*) AS f_count " +
            "FROM trn_stk_cfg_dns AS s " +
            "WHERE s.id_cob = " + nPkCobId + " AND s.id_wh = " + nPkWhId + " AND s.id_dns = " + nPkDnsId + "; ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            if (resultSet.getObject("f_count") != null) {
                nCount = resultSet.getObject("f_count") == null ? 0 : resultSet.getInt("f_count");
            }
        }

        return nCount;
    }

    /** Gets Jasper Report file name.
     *
     * @param reportType Constant defined in erp.data.SDataConstantsSys.
     */
    public static java.lang.String getReportFileName(int reportType) throws java.lang.Exception {
        String name = "";

        switch (reportType) {
            case SDataConstantsSys.REP_FIN_ACC:
                name = "reps/fin_acc.jasper";
                break;
            case SDataConstantsSys.REP_FIN_AUX_ACC:
                name = "reps/fin_aux_acc.jasper";
                break;
            case SDataConstantsSys.REP_FIN_REC:
                name = "reps/fin_rec.jasper";
                break;
            case SDataConstantsSys.REP_FIN_REC_CY:
                name = "reps/fin_rec_cy.jasper";
                break;
            case SDataConstantsSys.REP_FIN_RECS:
                name = "reps/fin_recs.jasper";
                break;
            case SDataConstantsSys.REP_FIN_RECS_CY:
                name = "reps/fin_recs_cy.jasper";
                break;
            case SDataConstantsSys.REP_FIN_REC_ADV:
                name = "reps/fin_rec_adv.jasper";
                break;
            case SDataConstantsSys.REP_FIN_DPS_LAST_MOV:
                name = "reps/fin_dps_last_mov.jasper";
                break;
            case SDataConstantsSys.REP_FIN_JOURNAL_VOUCHERS:
                name = "reps/fin_journal_vouchers.jasper";
                break;
            case SDataConstantsSys.REP_FIN_JOURNAL_VOUCHERS_CY:
                name = "reps/fin_journal_vouchers_cy.jasper";
                break;
            case SDataConstantsSys.REP_FIN_BPS_BAL:
                name = "reps/fin_bps_bal.jasper";
                break;
            case SDataConstantsSys.REP_FIN_BPS_BAL_PER:
                name = "reps/fin_bps_bal_per.jasper";
                break;
            case SDataConstantsSys.REP_FIN_BPS_BAL_CRED:
                name = "reps/fin_bps_bal_cred.jasper";
                break;
            case SDataConstantsSys.REP_FIN_BPS_BAL_DPS:
                name = "reps/fin_bps_bal_dps.jasper";
                break;
            case SDataConstantsSys.REP_FIN_BPS_BAL_DPS_EXR:
                name = "reps/fin_bps_bal_dps_exr.jasper";
                break;
            case SDataConstantsSys.REP_FIN_BPS_BAL_COLL:
                name = "reps/fin_bps_bal_coll.jasper";
                break;
            case SDataConstantsSys.REP_FIN_BPS_BAL_COLL_DPS:
                name = "reps/fin_bps_bal_coll_dps.jasper";
                break;
            case SDataConstantsSys.REP_FIN_BPS_MOV:
                name = "reps/fin_bps_mov.jasper";
                break;
            case SDataConstantsSys.REP_FIN_ACC_CASH_BAL:
                name = "reps/fin_bal_cob_ent.jasper";
                break;
            case SDataConstantsSys.REP_FIN_BPS_STT:
                name = "reps/fin_bps_stt.jasper";
                break;
            case SDataConstantsSys.REP_FIN_BPS_STT_DPS:
                name = "reps/fin_bps_stt_dps.jasper";
                break;
            case SDataConstantsSys.REP_FIN_BPS_ACC_MOV:
                name = "reps/fin_bps_acc_mov.jasper";
                break;
            case SDataConstantsSys.REP_FIN_BPS_ACC_MOV_DAY:
                name = "reps/fin_bps_acc_mov_day.jasper";
                break;
            case SDataConstantsSys.REP_FIN_BPS_ACC_MOV_ORD:
                name = "reps/fin_bps_acc_mov_ord.jasper";
                break;    
            case SDataConstantsSys.REP_FIN_CASH_BAL:
            case SDataConstantsSys.REP_FIN_BANK_BAL:
                name = "reps/fin_cash_bal.jasper";
                break;
            case SDataConstantsSys.REP_FIN_CASH_MOV:
            case SDataConstantsSys.REP_FIN_BANK_MOV:
            case SDataConstantsSys.REP_FIN_TAX_DBT_MOV:
            case SDataConstantsSys.REP_FIN_TAX_CDT_MOV:
            case SDataConstantsSys.REP_FIN_PROF_LOSS_MOV:
                name = "reps/fin_mov.jasper";
                break;
            case SDataConstantsSys.REP_FIN_CASH_MOV_DET:
            case SDataConstantsSys.REP_FIN_BANK_MOV_DET:
                name = "reps/fin_mov_bank_day.jasper";
                break;
            case SDataConstantsSys.REP_FIN_CASH_MOV_CON:
            case SDataConstantsSys.REP_FIN_BANK_MOV_CON:
                name = "reps/fin_mov_bank_con.jasper";
                break;
            case SDataConstantsSys.REP_FIN_TRIAL_BAL:
                name = "reps/fin_trial_bal.jasper";
                break;
            case SDataConstantsSys.REP_FIN_TRIAL_BAL_DUAL:
                name = "reps/fin_trial_bal_dual.jasper";
                break;
            case SDataConstantsSys.REP_FIN_TRIAL_BAL_MAJOR:
                name = "reps/fin_trial_bal_major.jasper";
                break;
            case SDataConstantsSys.REP_FIN_TRIAL_BAL_CC:
                name = "reps/fin_trial_bal_cc.jasper";
                break;
            case SDataConstantsSys.REP_FIN_TRIAL_BAL_ITEM:
                name = "reps/fin_item_trial_bal.jasper";
                break;
            case SDataConstantsSys.REP_FIN_TRIAL_BAL_CC_ITEM:
                name = "reps/fin_trial_bal_cc_item.jasper";
                break;
            case SDataConstantsSys.REP_FIN_STAT_BAL_SHEET:
                name = "reps/fin_bal_sht.jasper";
                break;
            case SDataConstantsSys.REP_FIN_STAT_PROF_LOSS:
                name = "reps/fin_prof_loss.jasper";
                break;
            case SDataConstantsSys.REP_FIN_ACC_CPT:
                name = "reps/fin_acc_cpt.jasper";
                break;
            case SDataConstantsSys.REP_FIN_CC:
                name = "reps/fin_aux_cc.jasper";
                break;
            case SDataConstantsSys.REP_FIN_TAX_PAY_CPT:
                name = "reps/fin_tax_item.jasper";
                break;
            case SDataConstantsSys.REP_FIN_TAX_MOV:
                name = "reps/fin_tax_mov.jasper";
                break;
            case SDataConstantsSys.REP_FIN_CHECK_REC:
                name = "reps/fin_check_record.jasper";
                break;
            case SDataConstantsSys.REP_FIN_DPS_PAY:
                name = "reps/fin_dps_pay.jasper";
                break;
            case SDataConstantsSys.REP_FIN_AUX_MOV_BPS:
                name = "reps/fin_aux_mov_bps.jasper";
                break;
            case SDataConstantsSys.REP_FIN_STA_BPS: // statement of account of business partner
                name = "reps/fin_sta_bps.jasper";
                break;
            case SDataConstantsSys.REP_FIN_BPS_ACC: // not used yet, for future use
                name = ""; //
                break;
            case SDataConstantsSys.REP_FIN_BPS_ACC_AGI: // business partner accounts aging
                name = "reps/fin_bps_acc_agi.jasper";
                break;
            case SDataConstantsSys.REP_FIN_BPS_ACC_AGI_CRED: // business partner accounts aging with amounts of insurance and guarantee
                name = "reps/fin_bps_acc_agi_cred.jasper";
                break;
            case SDataConstantsSys.REP_FIN_PS_CL_ITEM:
                name = "reps/fin_ps_item.jasper";
                break;
            case SDataConstantsSys.REP_FIN_PS_CL_ITEM_TOT:
                name = "reps/fin_ps_item_tot.jasper";
                break;    
            case SDataConstantsSys.REP_TRN_CTR:
                name = "reps/trn_ctr.jasper";
                break;
            case SDataConstantsSys.REP_TRN_DPS_BPS:
                name = "reps/trn_dps_bps.jasper";
                break;
            case SDataConstantsSys.REP_TRN_DPS_UNP:
                name = "reps/trn_dps_unp.jasper";
                break;
            case SDataConstantsSys.REP_TRN_DPS_UNP_CY:
                name = "reps/trn_dps_unp_cy.jasper";
                break;
            case SDataConstantsSys.REP_TRN_STK:
                name = "reps/trn_stk.jasper";
                break;   
            case SDataConstantsSys.REP_TRN_STK_PERIOD:
                name = "reps/trn_stk_period.jasper";
                break; 
            case SDataConstantsSys.REP_TRN_STK_MOV:
                name = "reps/trn_stk_mov.jasper";
                break;
            case SDataConstantsSys.REP_TRN_STK_MOV_MOV:
                name = "reps/trn_stk_mov_mov.jasper";
                break;
            case SDataConstantsSys.REP_TRN_STK_MOV_SUM:
                name = "reps/trn_stk_mov_sum.jasper";
                break;
            case SDataConstantsSys.REP_TRN_STK_MOV_SUM_SUM:
                name = "reps/trn_stk_mov_sum_sum.jasper";
                break;
            case SDataConstantsSys.REP_TRN_PS:
                name = "reps/trn_ps.jasper";
                break;
            case SDataConstantsSys.REP_TRN_PS_UNIT:
                name = "reps/trn_ps_unit.jasper";
                break;
            case SDataConstantsSys.REP_TRN_PS_CON:
                name = "reps/trn_ps_conc.jasper";
                break;
            case SDataConstantsSys.REP_TRN_PS_LOC:
                name = "reps/trn_ps_loc.jasper";
                break;
            case SDataConstantsSys.REP_TRN_PS_DIARY:
                name = "reps/trn_ps_diary.jasper";
                break;
            case SDataConstantsSys.REP_TRN_PS_CL_ITEM:
                name = "reps/trn_ps_item.jasper";
                break;
            case SDataConstantsSys.REP_TRN_PS_CL_ITEM_TOT:
                name = "reps/trn_ps_item_tot.jasper";
                break;
            case SDataConstantsSys.REP_TRN_PS_CL_ITEM_TOT_COMM:
                name = "reps/trn_ps_item_tot_comm.jasper";
                break;
            case SDataConstantsSys.REP_TRN_PS_COMP_PY:
                name = "reps/trn_ps_comp_py.jasper";
                break;
            case SDataConstantsSys.REP_TRN_PS_COMP_PPY:
                name = "reps/trn_ps_comp_ppy.jasper";
                break;
            case SDataConstantsSys.REP_TRN_PUR_UNIT_COST:
                name = "reps/trn_pur_unit_cost.jasper";
                break;
            case SDataConstantsSys.REP_TRN_ADV:
                name = "reps/trn_adv.jasper";
                break;
            case SDataConstantsSys.REP_TRN_DPS:
                name = "reps/trn_dps.jasper";
                break;
            case SDataConstantsSys.REP_TRN_DPS_US:
                name = "reps/trn_dps_us.jasper";
                break;
            case SDataConstantsSys.REP_TRN_DPS_ADJ:
                name = "reps/trn_dps_adj.jasper";
                break;
            case SDataConstantsSys.REP_TRN_DPS_LIST:
                name = "reps/trn_dps_list.jasper";
                break;
            case SDataConstantsSys.REP_TRN_DPS_ORDER:
                name = "reps/trn_dps_order.jasper";
                break;
            case SDataConstantsSys.REP_TRN_CON:
                name = "reps/trn_con.jasper";
                break;
            case SDataConstantsSys.REP_TRN_CON_MOV:
                name = "reps/trn_con_mov.jasper";
                break;
            case SDataConstantsSys.REP_TRN_BAL_AGI:
                name = "reps/trn_bal_agi.jasper";
                break;
            case SDataConstantsSys.REP_TRN_BPS_MOV_DPS:
                name = "reps/trn_bps_movs_dps.jasper";
                break;
            case SDataConstantsSys.REP_TRN_ORD_GDS:
                name = "reps/trn_dps_order_goods.jasper";
                break;
            case SDataConstantsSys.REP_TRN_EST:
                name = "reps/trn_est.jasper";
                break;
            case SDataConstantsSys.REP_TRN_PS_ITEM_UNIT_PRICE:
                name = "reps/trn_ps_item_price.jasper";
                break;
            case SDataConstantsSys.REP_TRN_DPS_BPS_DETAIL:
                name = "reps/trn_dps_bps_detail.jasper";
                break;
            case SDataConstantsSys.REP_TRN_DPS_MOV:
                name = "reps/trn_dps_rec.jasper";
                break;
            case SDataConstantsSys.REP_TRN_CFD:
                name = "reps/trn_cfd.jasper";
                break;
            case SDataConstantsSys.REP_TRN_CFDI:
                name = "reps/trn_cfdi.jasper";
                break;
            case SDataConstantsSys.REP_TRN_CFDI_33:
                name = "reps/trn_cfdi_33.jasper";
                break;
            case SDataConstantsSys.REP_TRN_CFDI_33_CRP_10:
                name = "reps/trn_cfdi_33_crp_10.jasper";
                break;
            case SDataConstantsSys.REP_TRN_CFDI_PAYROLL:
                name = "reps/trn_cfdi_payroll.jasper";
                break;
            case SDataConstantsSys.REP_TRN_CFDI_ACK_CAN:
                name = "reps/trn_cfdi_ack_can.jasper";
                break;
            case SDataConstantsSys.REP_TRN_SHIP:
                name = "reps/trn_ship.jasper";
                break;
            case SDataConstantsSys.REP_TRN_COMMS_ITEM:
                name = "reps/trn_comms.jasper";
                break;
            case SDataConstantsSys.REP_TRN_COMMS_DPS:
                name = "reps/trn_comms_by_dps.jasper";
                break;
            case SDataConstantsSys.REP_TRN_DIOG:
                name = "reps/trn_diog.jasper";
                break;
            case SDataConstantsSys.REP_TRN_DPS_SHIP_ITEM:
                name = "reps/trn_dps_ship_item.jasper";
                break;
            case SDataConstantsSys.REP_TRN_CON_STK:
                name = "reps/trn_con_stk.jasper";
                break;
            case SDataConstantsSys.REP_MFG_BOM:
                name = "reps/mfg_bom.jasper";
                break;
            case SDataConstantsSys.REP_MFG_ORD:
                name = "reps/mfg_ord.jasper";
                break;
            case SDataConstantsSys.REP_MFG_ORD_PERFORMANCE:
                name = "reps/mfg_ord_performance.jasper";
                break;
            case SDataConstantsSys.REP_MFG_FINISHED_GOODS_EFFICIENCY:
                name = "reps/mfg_finished_goods_efficiency.jasper";
                break;
            case SDataConstantsSys.REP_MFG_RAW_MATERIALS_EFFICIENCY:
                name = "reps/mfg_finished_goods_efficiency.jasper";
                break;
            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        return name;
    }

    /** Fills Jasper Report.
     *
     * @param client ERP Client Interface.
     * @param reportType Constant defined in erp.data.SDataConstantsSys.
     * @param map Report parameters.
     * @return 
     * @throws java.lang.Exception
     */
    public static net.sf.jasperreports.engine.JasperPrint fillReport(erp.client.SClientInterface client, int reportType, java.util.Map<java.lang.String, java.lang.Object> map) throws java.lang.Exception {
        SServerRequest request = null;
        SServerResponse response = null;

        request = new SServerRequest(SServerConstants.REQ_REPS);
        request.setRegistryType(reportType);
        request.setPacket(map);
        
        response = client.getSessionXXX().request(request);
        
        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            throw new Exception(response.getMessage());
        }

        return (net.sf.jasperreports.engine.JasperPrint) response.getPacket();
    }

    public static net.sf.jasperreports.engine.JasperPrint fillCheck(erp.client.SClientInterface client, java.lang.String name, java.util.Map<java.lang.String, java.lang.Object> map) throws java.lang.Exception {
        SServerRequest request = null;
        SServerResponse response = null;

        request = new SServerRequest(SServerConstants.REQ_CHECK);
        request.setPacket(new Object[] { name, map });
        response = client.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            throw new Exception(response.getMessage());
        }

        return (net.sf.jasperreports.engine.JasperPrint) response.getPacket();
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants.
     */
    public static boolean isCatalogueCfg(int catalogue) {
        return catalogue >= SDataConstants.GLOBAL_CAT_CFG && catalogue < SDataConstants.GLOBAL_CAT_USR ||
                catalogue >= SModConsts.CFGS_CT_ENT && catalogue <= SModConsts.CFG_PARAM_CO;
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants.
     */
    public static boolean isCatalogueUsr(int catalogue) {
        return catalogue >= SDataConstants.GLOBAL_CAT_USR && catalogue < SDataConstants.GLOBAL_CAT_LOC;
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants.
     */
    public static boolean isCatalogueLoc(int catalogue) {
        return catalogue >= SDataConstants.GLOBAL_CAT_LOC && catalogue < SDataConstants.GLOBAL_CAT_BPS;
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants.
     */
    public static boolean isCatalogueBps(int catalogue) {
        return catalogue >= SDataConstants.GLOBAL_CAT_BPS && catalogue < SDataConstants.GLOBAL_CAT_ITM;
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants.
     */
    public static boolean isCatalogueItm(int catalogue) {
        return catalogue >= SDataConstants.GLOBAL_CAT_ITM && catalogue < SDataConstants.GLOBAL_CAT_FIN;
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants and erp.gui.SDataConstants.
     */
    public static boolean isCatalogueFin(int catalogue) {
        return catalogue >= SDataConstants.GLOBAL_CAT_FIN && catalogue < SDataConstants.GLOBAL_CAT_TRN ||
                catalogue >= SDataConstants.MOD_FIN && catalogue < SDataConstants.MOD_PUR;
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants and erp.gui.SDataConstants.
     */
    public static boolean isCatalogueTrn(int catalogue) {
        return catalogue >= SDataConstants.GLOBAL_CAT_TRN && catalogue < SDataConstants.GLOBAL_CAT_MKT;
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants and erp.gui.SDataConstants.
     */
    public static boolean isCatalogueTrnPur(int catalogue) {
        return catalogue >= SDataConstants.GLOBAL_CAT_TRN && catalogue < SDataConstants.GLOBAL_CAT_MKT ||
                catalogue >= SDataConstants.MOD_PUR && catalogue < SDataConstants.MOD_SAL;
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants and erp.gui.SDataConstants.
     */
    public static boolean isCatalogueTrnSal(int catalogue) {
        return catalogue >= SDataConstants.GLOBAL_CAT_TRN && catalogue < SDataConstants.GLOBAL_CAT_MKT ||
                catalogue >= SDataConstants.MOD_SAL && catalogue < SDataConstants.MOD_INV;
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants and erp.gui.SDataConstants.
     */
    public static boolean isCatalogueTrnInv(int catalogue) {
        return catalogue >= SDataConstants.GLOBAL_CAT_TRN && catalogue < SDataConstants.GLOBAL_CAT_MKT ||
                catalogue >= SDataConstants.MOD_INV && catalogue < SDataConstants.MOD_MKT;
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants and erp.gui.SDataConstants.
     */
    public static boolean isCatalogueMkt(int catalogue) {
        return catalogue >= SDataConstants.GLOBAL_CAT_MKT && catalogue < SDataConstants.GLOBAL_CAT_LOG ||
                catalogue >= SDataConstants.MOD_MKT && catalogue < SDataConstants.MOD_LOG;
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants and erp.gui.SDataConstants.
     */
    public static boolean isCatalogueLog(int catalogue) {
        return catalogue >= SDataConstants.GLOBAL_CAT_LOG && catalogue < SDataConstants.GLOBAL_CAT_MFG ||
                catalogue >= SDataConstants.MOD_LOG && catalogue < SDataConstants.MOD_MFG ||
                catalogue >= SModConsts.LOGS_TP_SHIP && catalogue <= SModConsts.LOG_BOL_MERCH_QTY;
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants and erp.gui.SDataConstants.
     */
    public static boolean isCatalogueMfg(int catalogue) {
        return catalogue >= SDataConstants.GLOBAL_CAT_MFG && catalogue < SDataConstants.GLOBAL_CAT_HRS ||
                catalogue >= SDataConstants.MOD_MFG && catalogue < SDataConstants.MOD_HRS;
    }

    /**
     * @param catalogue Constants defined in erp.data.SDataConstants and erp.gui.SDataConstants.
     */
    public static boolean isCatalogueHrs(int catalogue) {
        return catalogue >= SDataConstants.GLOBAL_CAT_HRS && catalogue < SDataConstants.GLOBAL_CAT_QLT ||
                catalogue >= SDataConstants.MOD_HRS && catalogue < SDataConstants.MOD_QLT ||
                catalogue >= SModConsts.HRSS_CL_HRS_CAT && catalogue <= SModConsts.HRSX_AUT_DED;
    }
    
    /**
     * @param catalogue Constants defined in erp.data.SDataConstants and erp.gui.SDataConstants.
     */
    public static boolean isCatalogueQlt(int catalogue) {
        return catalogue >= SDataConstants.GLOBAL_CAT_QLT && catalogue < SDataConstants.GLOBAL_CAT_XXX ||
                catalogue >= SDataConstants.MOD_QLT && catalogue < SDataConstants.MOD_XXX ||
                catalogue >= SModConsts.QLT_LOT_APR && catalogue <= SModConsts.QLT_LOT_APR;
    }
    
    /**
     * Validate if account is usable.
     * @param client Client interface.
     * @param account Account to be validated;
     * @param date Date of movement (can be null).
     * @return Validation text.
     */
    public static java.lang.String validateAccount(final erp.client.SClientInterface client, final erp.mfin.data.SDataAccount account, final java.util.Date date) {
        return validateAccount(client, account, date, false);
    }
    
    /**
     * Validate if account is usable.
     * @param client Client interface.
     * @param account Account to be validated;
     * @param date Date of movement (can be null).
     * @param validatingLedger Flag to indicate a ledger account validation.
     * @return Validation text.
     */
    public static java.lang.String validateAccount(final erp.client.SClientInterface client, final erp.mfin.data.SDataAccount account, final java.util.Date date, final boolean validatingLedger) {
        int usedLevels = 0;
        String msg = "";

        if (account == null) {
            msg = "El registro de la cuenta contable no existe.";
        }
        else if (account.getIsDeleted()) {
            msg = "La cuenta contable está eliminada.";
        }
        else if (!account.getIsActive()) {
            msg = "La cuenta contable no está activa.";
        }
        else if (date != null && date.before(account.getDateStart())) {
            msg = "La fecha del registro " +
                    "(" + client.getSessionXXX().getFormatters().getDateFormat().format(date) + ") " +
                    "es anterior a la fecha inicial de vigencia de la cuenta contable " +
                    "(" + client.getSessionXXX().getFormatters().getDateFormat().format(account.getDateStart()) + ").";
        }
        else if (date != null && account.getDateEnd_n() != null && date.after(account.getDateEnd_n())) {
            msg = "La fecha del registro " +
                    "(" + client.getSessionXXX().getFormatters().getDateFormat().format(date) + ") " +
                    "es posterior a la fecha final de vigencia de la cuenta contable " +
                    "(" + client.getSessionXXX().getFormatters().getDateFormat().format(account.getDateEnd_n()) + ").";
        }
        else if (!validatingLedger) {
            usedLevels = getAccountUsedLevelsCount(account.getPkAccountIdXXX(), getAccountLevels(account.getPkAccountIdXXX()));

            if (usedLevels != account.getDbmsMajorDeep()) {
                msg = "La profundidad de captura de la cuenta contable, según su cuenta de mayor, es " + account.getDbmsMajorDeep() + ",\ny el nivel de la cuenta capturada es " + usedLevels + ".";
            }
        }

        return msg;
    }

    /**
     * @param client ERP Client interface.
     * @param costCenter Cost center to be validated;
     * @param date Date of movement (can be null).
     * @return Validation text.
     */
    public static java.lang.String validateCostCenter(erp.client.SClientInterface client, erp.mfin.data.SDataCostCenter costCenter, java.util.Date date) {
        int usedLevels = 0;
        String msg = "";

        if (costCenter == null) {
            msg = "El registro del centro de costo no existe.";
        }
        else if (costCenter.getIsDeleted()) {
            msg = "El centro de costo está eliminado.";
        }
        else if (!costCenter.getIsActive()) {
            msg = "El centro de costo no está activo.";
        }
        else if (date != null && date.before(costCenter.getDateStart())) {
            msg = "La fecha del registro " +
                    "(" + client.getSessionXXX().getFormatters().getDateFormat().format(date) + ") " +
                    "es anterior a la fecha inicial de vigencia del centro de costo " +
                    "(" + client.getSessionXXX().getFormatters().getDateFormat().format(costCenter.getDateStart()) + ").";
        }
        else if (date != null && costCenter.getDateEnd_n() != null && date.after(costCenter.getDateEnd_n())) {
            msg = "La fecha del registro " +
                    "(" + client.getSessionXXX().getFormatters().getDateFormat().format(date) + ") " +
                    "es posterior a la fecha final de vigencia del centro de costo " +
                    "(" + client.getSessionXXX().getFormatters().getDateFormat().format(costCenter.getDateEnd_n()) + ").";
        }
        else {
            usedLevels = getAccountUsedLevelsCount(costCenter.getPkCostCenterIdXXX(), getAccountLevels(costCenter.getPkCostCenterIdXXX()));

            if (usedLevels != costCenter.getDbmsMajorDeep()) {
                msg = "La profundidad de captura del centro de costo, según su centro principal, es " + costCenter.getDbmsMajorDeep() + ",\ny el nivel del centro capturado es " + usedLevels + ".";
            }
        }

        return msg;
    }

    /**
     * Validate exchange rate.
     * @param client
     * @param valCy
     * @param excRate
     * @param val
     * @param field
     * @return 
     */
    public static java.lang.String validateExchangeRate(erp.client.SClientInterface client, double valCy, double excRate, double val, java.lang.String field) {
        int decs = client.getSessionXXX().getParamsErp().getDecimalsValue();
        String msg = "";

        if (SLibUtilities.round(valCy * excRate, client.getSessionXXX().getParamsErp().getDecimalsValue()) != SLibUtilities.round(val, decs)) {
            msg = "El monto original " + client.getSessionXXX().getFormatters().getDecimalsCurrencyFormat().format(valCy) + " por el tipo de cambio del campo '" + field + "' (" + client.getSessionXXX().getFormatters().getDecimalsExchangeRateFormat().format(excRate) + "),\n" +
                    "origina un valor de " +
                    "" + client.getSessionXXX().getFormatters().getDecimalsCurrencyLocalFormat().format(SLibUtilities.round(valCy * excRate, decs)) + ", que difiere en " +
                    "" + client.getSessionXXX().getFormatters().getDecimalsCurrencyLocalFormat().format(Math.abs(SLibUtilities.round(valCy * excRate, client.getSessionXXX().getParamsErp().getDecimalsValue()) - SLibUtilities.round(val, decs))) + "\n" +
                    "respecto del monto en moneda local de " +
                    "" + client.getSessionXXX().getFormatters().getDecimalsCurrencyLocalFormat().format(val) + ".";
        }

        return msg;
    }

    /**
     * Validate DPS entry reference.
     * @param session
     * @param dpsClassKey
     * @param reference
     * @param dpsCurrentKey
     * @throws Exception 
     */
    public static void validateDpsEtyReference(final SGuiSession session, final int[] dpsClassKey, final String reference, final int[] dpsCurrentKey) throws Exception {
        int i = 0;
        String sql = "";
        String txt = "";
        ResultSet resultSet = null;
        
        sql = "SELECT DISTINCT d.num_ser, d.num, d.dt, b.bp "
                + "FROM trn_dps AS d "
                + "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp "
                + "WHERE d.b_del = 0 AND de.b_del = 0 AND d.fid_st_dps <> " + SModSysConsts.TRNS_ST_DPS_ANNULED + " AND "
                + "d.fid_ct_dps = " + dpsClassKey[0] + " AND d.fid_cl_dps = " + dpsClassKey[1] + " AND de.ref = '" + reference + "' "
                + (dpsCurrentKey == null || dpsCurrentKey.length != 2 || dpsCurrentKey[0] == SLibConstants.UNDEFINED || dpsCurrentKey[1] == SLibConstants.UNDEFINED ?
                "" : "AND NOT (d.id_year = " + dpsCurrentKey[0] + " AND d.id_doc = " + dpsCurrentKey[1] + ")")
                + "ORDER BY d.num_ser, d.num, d.dt; ";
        resultSet = session.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            i++;
            txt += (txt.isEmpty() ? "" : ";\n") + "folio: " + (resultSet.getString("d.num_ser").isEmpty() ? "" : resultSet.getString("d.num_ser") + "-") + resultSet.getString("d.num") + ", "
                    + "fecha: " + SLibUtils.DateFormatDate.format(resultSet.getDate("d.dt")) + ", "
                    + SBpsUtils.getBizPartnerCategoryName(STrnUtils.getBizPartnerCategoryId(dpsClassKey[0]), SUtilConsts.NUM_SNG).toLowerCase() + ": " + resultSet.getString("b.bp");
        }
        
        if (!txt.isEmpty()) {
            throw new Exception("La referencia '" + reference + "' ya existe en " + (i == 1 ? "el documento" : "los documentos") + ":\n" + txt + ".");
        }
    }

    @SuppressWarnings("unchecked")
    public static int emitReportCfd(erp.client.SClientInterface client, int pnYear, int pnPeriod, java.io.File poFile) throws java.lang.Exception {
        int docs = 0;
        int[] anDocClass = null;
        boolean bCancel = false;
        String sql = "";
        ResultSet resultSet = null;
        String txt = "";
        String newLine = "";
        String sRfc = "";
        String sSerie = "";
        String sFolio = "";
        String sAprob = "";
        String sFecha = "";
        String sMonto = "";
        String sImpto = "";
        String sDocEdo = "";
        String sDocTip = "";
        String sPedNum = "";
        String sPedFecha = "";
        String sPedAduana = "";
        Node node = null;
        NamedNodeMap namedNodeMap = null;
        Document document = null;
        DocumentBuilder docBuilder = null;
        SimpleDateFormat sdfParser = null;
        SimpleDateFormat sdfFormat = null;
        OutputStreamWriter osw = null;

        docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        sdfParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdfFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        newLine = System.getProperty("line.separator");     // for MS Windows try "\r\n"
        osw = new OutputStreamWriter(new FileOutputStream(poFile), "UTF-8");

        sql = "SELECT d.fid_ct_dps, d.fid_cl_dps, d.fid_tp_dps, d.num_ser, d.num, d.fid_st_dps, xc.doc_xml " +
                "FROM trn_dps AS d INNER JOIN trn_cfd AS c ON " +
                "d.id_year = c.fid_dps_year_n AND d.id_doc = c.fid_dps_doc_n " +
                "LEFT OUTER JOIN " + SClientUtils.getComplementaryDdName(client) + ".trn_cfd AS xc ON c.id_cfd = xc.id_cfd " +
                "WHERE YEAR(d.dt) = " + pnYear + " AND MONTH(d.dt) = " + pnPeriod + " AND d.b_del = 0 AND " +
                "c.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_CFD + " AND " +
                "d.fid_st_dps IN (" + SDataConstantsSys.TRNS_ST_DPS_EMITED + ", " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ") AND " +
                "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CT_DPS_SAL + " " +
                "ORDER BY d.fid_ct_dps, d.fid_cl_dps, d.fid_tp_dps, d.num_ser, d.num; ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        while (resultSet.next()) {
            docs++;
            document = docBuilder.parse(new ByteArrayInputStream(resultSet.getString("xc.doc_xml").trim().getBytes("UTF-8")));

            node = SXmlUtils.extractElements(document, "Comprobante").item(0);
            namedNodeMap = node.getAttributes();

            sSerie = SXmlUtils.extractAttributeValue(namedNodeMap, "serie", false);
            sFolio = SXmlUtils.extractAttributeValue(namedNodeMap, "folio", true);
            sAprob = SXmlUtils.extractAttributeValue(namedNodeMap, "anoAprobacion", true) + SXmlUtils.extractAttributeValue(namedNodeMap, "noAprobacion", true);
            sFecha = sdfFormat.format(sdfParser.parse(SXmlUtils.extractAttributeValue(namedNodeMap, "fecha", true)));
            sMonto = SXmlUtils.extractAttributeValue(namedNodeMap, "total", true);

            node = SXmlUtils.extractElements(document, "Receptor").item(0);
            namedNodeMap = node.getAttributes();

            sRfc = SXmlUtils.extractAttributeValue(namedNodeMap, "rfc", true);

            node = SXmlUtils.extractElements(document, "Impuestos").item(0);
            namedNodeMap = node.getAttributes();

            sImpto = SXmlUtils.extractAttributeValue(namedNodeMap, "totalImpuestosTrasladados", false);
            if (sImpto.length() == 0) {
                sImpto = "0.00";
            }

            anDocClass = new int[] { resultSet.getInt("d.fid_ct_dps"), resultSet.getInt("d.fid_cl_dps") };
            if (SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_DOC, anDocClass)) {
                sDocTip = "I";
            }
            else if (SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ, anDocClass)) {
                sDocTip = "E";
            }
            else {
                throw new Exception("El tipo del documento serie: '" + sSerie + "', no.: " + sFolio + " es desconocido.");
            }

            bCancel = resultSet.getInt("d.fid_st_dps") == SDataConstantsSys.TRNS_ST_DPS_ANNULED;

            sPedNum = "";
            sPedFecha = "";
            sPedAduana = "";
            sDocEdo = "1";

            txt = "|" + sRfc + "|" + sSerie + "|" + sFolio + "|" + sAprob + "|" + sFecha + "|" +
                    sMonto + "|" + sImpto + "|" + sDocEdo + "|" + sDocTip + "|" +
                    sPedNum + "|" + sPedFecha + "|" + sPedAduana + "|";

            osw.write(txt);
            osw.write(newLine);

            if (bCancel) {
                sDocEdo = "0";

                txt = "|" + sRfc + "|" + sSerie + "|" + sFolio + "|" + sAprob + "|" + sFecha + "|" +
                        sMonto + "|" + sImpto + "|" + sDocEdo + "|" + sDocTip + "|" +
                        sPedNum + "|" + sPedFecha + "|" + sPedAduana + "|";

                osw.write(txt);
                osw.write(newLine);
            }
        }

        osw.flush();
        osw.close();

        return docs;
    }

    @SuppressWarnings("unchecked")
    public static int emitReportCf(erp.client.SClientInterface client, int pnYear, int pnPeriod, java.io.File poFile) throws java.lang.Exception {
        int docs = 0;
        int[] anDocClass = null;
        boolean bCancel = false;
        String sql = "";
        ResultSet resultSet = null;
        String txt = "";
        String newLine = "";
        String sRfc = "";
        String sSerie = "";
        String sFolio = "";
        String sAprob = "";
        String sFecha = "";
        String sMonto = "";
        String sImpto = "";
        String sDocEdo = "";
        String sDocTip = "";
        String sPedNum = "";
        String sPedFecha = "";
        String sPedAduana = "";
        DecimalFormat dfFormat = null;
        SimpleDateFormat sdfFormat = null;
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(poFile), "UTF-8");

        dfFormat = new DecimalFormat("#0.00");
        sdfFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        newLine = System.getProperty("line.separator");     // for MS Windows try "\r\n"

        sql = "SELECT d.fid_ct_dps, d.fid_cl_dps, d.fid_tp_dps, d.dt, d.num_ser, d.num, d.fid_st_dps, " +
                "d.tot_r, d.approve_num, b.fiscal_id, " +
                "(SELECT COALESCE(SUM(det.tax), 0) FROM trn_dps_ety AS de, trn_dps_ety_tax AS det " +
                "WHERE de.id_year=d.id_year AND de.id_doc=d.id_doc AND de.b_del=0 AND " +
                "det.id_year = de.id_year AND det.id_doc = de.id_doc AND det.id_ety = de.id_ety AND " +
                "det.id_tax_bas = 1 AND det.fid_tp_tax = 1) AS f_tax " +
                "FROM trn_dps AS d INNER JOIN erp.bpsu_bp AS b ON " +
                "d.fid_bp_r = b.id_bp " +
                "WHERE YEAR(d.dt) = " + pnYear + " AND MONTH(d.dt) = " + pnPeriod + " AND d.b_del = 0 AND " +
                "d.fid_st_dps IN (" + SDataConstantsSys.TRNS_ST_DPS_EMITED + ", " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ") AND ( " +
                "(d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] + ") OR " +
                "(d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1] + ")) AND d.approve_year = 0 " +
                "ORDER BY d.fid_ct_dps, d.fid_cl_dps, d.fid_tp_dps, d.num_ser, d.num; ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        while (resultSet.next()) {
            docs++;
            sRfc = resultSet.getString("b.fiscal_id");
            sSerie = resultSet.getString("d.num_ser");
            sFolio = resultSet.getString("d.num");
            sAprob = resultSet.getString("d.approve_num");
            sFecha = sdfFormat.format(resultSet.getDate("d.dt"));
            sMonto = dfFormat.format(resultSet.getDouble("d.tot_r"));
            sImpto = dfFormat.format(resultSet.getDouble("f_tax"));

            anDocClass = new int[] { resultSet.getInt("d.fid_ct_dps"), resultSet.getInt("d.fid_cl_dps") };

            if (SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_DOC, anDocClass)) {
                sDocTip = "I";
            }
            else if (SLibUtilities.compareKeys(SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ, anDocClass)) {
                sDocTip = "E";
            }
            else {
                throw new Exception("El tipo del documento serie: '" + sSerie + "', no.: " + sFolio + " es desconocido.");
            }

            bCancel = resultSet.getInt("d.fid_st_dps") == SDataConstantsSys.TRNS_ST_DPS_ANNULED;

            sPedNum = "";
            sPedFecha = "";
            sPedAduana = "";
            sDocEdo = "1";

            txt = "|" + sRfc + "|" + sSerie + "|" + sFolio + "|" + sAprob + "|" + sFecha + "|" +
                    sMonto + "|" + sImpto + "|" + sDocEdo + "|" + sDocTip + "|" +
                    sPedNum + "|" + sPedFecha + "|" + sPedAduana + "|";

            osw.write(txt);
            osw.write(newLine);

            if (bCancel) {
                sDocEdo = "0";

                txt = "|" + sRfc + "|" + sSerie + "|" + sFolio + "|" + sAprob + "|" + sFecha + "|" +
                        sMonto + "|" + sImpto + "|" + sDocEdo + "|" + sDocTip + "|" +
                        sPedNum + "|" + sPedFecha + "|" + sPedAduana + "|";

                osw.write(txt);
                osw.write(newLine);
            }
        }

        osw.flush();
        osw.close();

        return docs;
    }

    @SuppressWarnings("unchecked")
    public static void saveRegistry(erp.client.SClientInterface client, SDataRegistry registry) throws java.lang.Exception {
        SServerRequest request = null;
        SServerResponse response = null;

        request = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
        request.setPacket(registry);
        response = client.getSessionXXX().request(request);
        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            throw new Exception(response.getMessage());
        }
    }

    /**
     * Obtains center cost for specified item.
     *
     * @param client SClientInterface.
     * @param nItemId Item ID whose center cost is requiered.
     */
    @SuppressWarnings("unchecked")
    public static java.lang.String obtainCostCenterItem(erp.client.SClientInterface client, int nItemId) throws java.lang.Exception {
        String sql = "";
        ResultSet resultSet = null;
        String costCenter = "";

        sql = "SELECT cci.id_tp_link, cci.fid_cc " +
                "FROM fin_cc_item AS cci WHERE cci.b_del = 0 AND (" +
                "(cci.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_ITEM + " AND cci.id_ref = " + nItemId + ") OR " +
                "(cci.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_MFR + " AND cci.id_ref IN (SELECT i.fid_mfr FROM erp.itmu_item AS i WHERE i.id_item = " + nItemId + ")) OR " +
                "(cci.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_BRD + " AND cci.id_ref IN (SELECT i.fid_brd FROM erp.itmu_item AS i WHERE i.id_item = " + nItemId + ")) OR " +
                "(cci.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_LINE + " AND cci.id_ref IN (SELECT i.fid_line_n FROM erp.itmu_item AS i WHERE i.id_item = " + nItemId + ")) OR " +
                "(cci.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_IGEN + " AND cci.id_ref IN (SELECT i.fid_igen FROM erp.itmu_item AS i WHERE i.id_item = " + nItemId + ")) OR " +
                "(cci.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_IGRP + " AND cci.id_ref IN (SELECT igen.fid_igrp FROM erp.itmu_igen AS igen INNER JOIN erp.itmu_item AS i ON igen.id_igen = i.fid_igen AND i.id_item = " + nItemId + ")) OR " +
                "(cci.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_IFAM + " AND cci.id_ref IN (SELECT igrp.fid_ifam FROM erp.itmu_igrp AS igrp INNER JOIN erp.itmu_igen AS igen ON igrp.id_igrp = igen.fid_igrp INNER JOIN erp.itmu_item AS i ON igen.id_igen = i.fid_igen AND i.id_item = " + nItemId + ")) OR " +
                "(cci.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_TP_ITEM + " AND cci.id_ref IN (SELECT itp.tp_idx FROM erp.itmu_igen AS igen INNER JOIN erp.itmu_item AS i ON igen.id_igen = i.fid_igen AND i.id_item = " + nItemId + " INNER JOIN erp.itms_tp_item AS itp ON igen.fid_ct_item = itp.id_ct_item AND igen.fid_cl_item = itp.id_cl_item AND igen.fid_tp_item = itp.id_tp_item)) OR " +
                "(cci.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_CL_ITEM + " AND cci.id_ref IN (SELECT icl.cl_idx FROM erp.itmu_igen AS igen INNER JOIN erp.itmu_item AS i ON igen.id_igen = i.fid_igen AND i.id_item = " + nItemId + " INNER JOIN erp.itms_cl_item AS icl ON igen.fid_ct_item = icl.id_ct_item AND igen.fid_cl_item = icl.id_cl_item)) OR " +
                "(cci.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_CT_ITEM + " AND cci.id_ref IN (SELECT ict.ct_idx FROM erp.itmu_igen AS igen INNER JOIN erp.itmu_item AS i ON igen.id_igen = i.fid_igen AND i.id_item = " + nItemId + " INNER JOIN erp.itms_ct_item AS ict ON igen.fid_ct_item = ict.id_ct_item)) OR " +
                "(cci.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_ALL + " AND cci.id_ref = " + SLibConstants.UNDEFINED + ")) " +
                "ORDER BY cci.id_tp_link DESC ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            costCenter = resultSet.getString("cci.fid_cc");
        }

        return costCenter;
    }   
}
