/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.log.db;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerCategory;
import erp.mcfg.data.SDataParamsCompany;
import erp.mcfg.data.SDataParamsErp;
import erp.mitm.data.SDataItem;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.STrnUtils;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SDataUserConfigurationTransaction;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SDbShipment extends SDbRegistryUser {

    public static final int FIELD_BILL_OF_LADING = FIELD_BASE + 1;
    public static final int FIELD_ST_DPS_AUTHORN = FIELD_BASE + 2;

    protected int mnPkShipmentId;
    protected String msNumber;
    protected Date mtDate;
    protected double mdRate;
    protected double mdExportDeclaration;
    protected double mdCarriageExportFreight;
    protected double mdCarriageExportDelay;
    protected double mdCarriageExportOther;
    protected double mdUploadingExport;
    protected double mdLoadingExport;
    protected double mdCarriageImport;
    protected double mdUploadingImport;
    protected double mdInsurance;
    protected double mdLoadingImport;
    protected double mdCarriageImportFreight;
    protected double mdCarriageImportDelay;
    protected double mdCarriageImportOther;
    protected double mdCustomsImport;
    protected double mdTariff;
    protected double mdCost_r;
    protected double mdExchangeRate;
    protected double mdCostCy_r;
    protected double mdCapacityVolume;
    protected double mdCapacityVolumeUsed;
    protected double mdCapacityMass;
    protected double mdCapacityMassUsed;
    protected String msDriver;
    protected String msPlate;
    protected boolean mbConsolidated;
    protected boolean mbBillOfLadingClosed;
    protected boolean mbDeleted;
    protected int mnFkCompanyBranchId;
    protected int mnFkShipmentTypeId;
    protected int mnFkDeliveryTypeId;
    protected int mnFkCurrencyId;
    protected int mnFkIncotermId;
    protected int mnFkSpotSourceId;
    protected int mnFkSpotDestinyId;
    protected int mnFkModeOfTransportationTypeId;
    protected int mnFkCarrierTypeId;
    protected int mnFkCarrierId;
    protected int mnFkVehicleTypeId;
    protected int mnFkVehicleId_n;
    protected int mnFkShipmentAuthorizationStatusId;
    protected int mnFkUnitCapacityVolumeId;
    protected int mnFkUnitCapacityMassId;
    protected int mnFkOrderYearId_n;
    protected int mnFkOrderDocId_n;
    protected int mnFkUserBillOfLadingId;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserBillOfLading;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;

    protected boolean mbLogisticsOrderAutomatic;
    protected String msXtaShipmentNumber;
    protected String msXtaDocNumberSeriesLogistics;
    protected String msXtaStatusDpsAuthorn;
    protected String msXtaOrderNumber;

    protected ArrayList<SDbShipmentDestiny> maShipmentDestinies;
    protected ArrayList<SDbShipmentNotes> maShipmentNotes;

    private SClientInterface miClient_xxx;

    protected SDataItem moItem;
    protected SDataBizPartner moBizPartner;
    protected SDataDps moDpsOrder;
    protected SDataDpsEntry moDpsOrderEntry;

    public SDbShipment() {
        super(SModConsts.LOG_SHIP);
    }

    /*
     * Private methods:
     */

    private ArrayList<java.lang.Object[]> obtainDpsEntriesIdsForShipment(final SGuiSession session) throws java.lang.Exception {
        String sql = "";
        ArrayList<Object[]> vDpsEntries = null;

        ResultSet resulSet = null;
        Statement statement = null;

        sql = "SELECT fid_item_log_delivery_n, fid_item_log_stay_n, fid_item_log_maneuver_n, fid_item_log_insurance_n, " +
            "fid_item_log_customs_exp_n, fid_item_log_customs_imp_n, fid_item_log_tariff_n, fid_item_log_other_n " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.CFG_PARAM_CO) + " ";

        statement = session.getDatabase().getConnection().createStatement();
        resulSet = statement.executeQuery(sql);
        vDpsEntries = new ArrayList<Object[]>();
        if (resulSet.next()) {

            vDpsEntries.add(new Object[] { (resulSet.getObject("fid_item_log_delivery_n") == null ? 0 : resulSet.getInt("fid_item_log_delivery_n")), mdCarriageImportFreight});
            vDpsEntries.add(new Object[] { (resulSet.getObject("fid_item_log_stay_n") == null ? 0 : resulSet.getInt("fid_item_log_stay_n")), mdCarriageImportDelay});
            vDpsEntries.add(new Object[] { (resulSet.getObject("fid_item_log_maneuver_n") == null ? 0 : resulSet.getInt("fid_item_log_maneuver_n")), mdUploadingImport});
            vDpsEntries.add(new Object[] { (resulSet.getObject("fid_item_log_insurance_n") == null ? 0 : resulSet.getInt("fid_item_log_insurance_n")), mdInsurance});
            vDpsEntries.add(new Object[] { (resulSet.getObject("fid_item_log_customs_exp_n") == null ? 0 : resulSet.getInt("fid_item_log_customs_exp_n")), mdExportDeclaration});
            vDpsEntries.add(new Object[] { (resulSet.getObject("fid_item_log_customs_imp_n") == null ? 0 : resulSet.getInt("fid_item_log_customs_imp_n")), mdCustomsImport});
            vDpsEntries.add(new Object[] { (resulSet.getObject("fid_item_log_tariff_n") == null ? 0 : resulSet.getInt("fid_item_log_tariff_n")), mdTariff});
            vDpsEntries.add(new Object[] { (resulSet.getObject("fid_item_log_other_n") == null ? 0 : resulSet.getInt("fid_item_log_other_n")), mdCarriageImportOther});
        }

        return vDpsEntries != null ? vDpsEntries : null;
    }

    private void obtainNextNumber(SGuiSession session) {
        int numNext = 0;

        try {
            numNext = STrnUtils.obtainNextNumberForDps(session, ((SDataParamsCompany) session.getConfigCompany()).getDbmsDocNumberSeriesLogistics(), new int[] { moDpsOrder.getFkDpsCategoryId(), moDpsOrder.getFkDpsClassId(), moDpsOrder.getFkDpsTypeId() });
            moDpsOrder.setNumber("" + numNext);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    private boolean createPurchaseOrder(SGuiSession session) {
        double rate = 0;

        SDataBizPartnerCategory oBizPartnerCategory = moBizPartner.getDbmsCategorySettingsSup();

        moDpsOrder = new SDataDps();
        moDpsOrder.setPkYearId(SLibTimeUtils.digestYear(mtDate)[0]);
        moDpsOrder.setPkDocId(0);
        moDpsOrder.setDate(mtDate);
        moDpsOrder.setDateDoc(mtDate);
        moDpsOrder.setDateStartCredit(mtDate);
        moDpsOrder.setDateDocDelivery_n(null);
        moDpsOrder.setNumberSeries(miClient_xxx.getSessionXXX().getParamsCompany().getDbmsDocNumberSeriesLogistics()); //msXtaDocNumberSeriesLogistics);
        moDpsOrder.setNumber("");
        moDpsOrder.setIsRegistryNew(true);
        moDpsOrder.setIsLinked(false);
        moDpsOrder.setIsClosed(false);
        moDpsOrder.setIsAudited(false);
        moDpsOrder.setIsAuthorized(false);
        moDpsOrder.setIsSystem(true);
        moDpsOrder.setIsDeleted(false);
        moDpsOrder.setFkDpsCategoryId(SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0]);
        moDpsOrder.setFkDpsClassId(SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1]);
        moDpsOrder.setFkDpsTypeId(SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2]);
        moDpsOrder.setFkDpsStatusId(SDataConstantsSys.TRNS_ST_DPS_EMITED);
        moDpsOrder.setFkDpsValidityStatusId(SDataConstantsSys.TRNS_ST_DPS_VAL_EFF);
        moDpsOrder.setFkDpsAuthorizationStatusId(SDataConstantsSys.TRNS_ST_DPS_AUTHORN_PENDING);
        moDpsOrder.setFkDpsAnnulationTypeId(SModSysConsts.TRNU_TP_DPS_ANN_NA);
        moDpsOrder.setFkDpsNatureId(miClient_xxx.getSessionXXX().getParamsCompany().getFkDpsNatureLogisticsId());
        moDpsOrder.setFkUserLinkedId(SDataConstantsSys.USRX_USER_NA);
        moDpsOrder.setFkUserClosedId(SDataConstantsSys.USRX_USER_NA);
        moDpsOrder.setFkUserClosedCommissionsId(SDataConstantsSys.USRX_USER_NA);
        moDpsOrder.setFkUserShippedId(SDataConstantsSys.USRX_USER_NA);
        moDpsOrder.setFkUserDpsDeliveryAckId(SDataConstantsSys.USRX_USER_NA);
        moDpsOrder.setFkUserAuditedId(SDataConstantsSys.USRX_USER_NA);
        moDpsOrder.setFkUserAuthorizedId(SDataConstantsSys.USRX_USER_NA);
        moDpsOrder.setFkUserNewId(session.getUser().getPkUserId());
        moDpsOrder.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
        moDpsOrder.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
        moDpsOrder.setFkCompanyBranchId(mnFkCompanyBranchId);
        moDpsOrder.setFkBizPartnerId_r(moBizPartner.getPkBizPartnerId());
        moDpsOrder.setFkBizPartnerBranchId(moBizPartner.getDbmsBizPartnerBranches().size() > 0 ? moBizPartner.getDbmsBizPartnerBranches().get(0).getPkBizPartnerBranchId() : 0);
        moDpsOrder.setFkBizPartnerBranchAddressId(moBizPartner.getDbmsBizPartnerBranches().size() > 0 ? moBizPartner.getDbmsBizPartnerBranches().get(0).getDbmsBizPartnerBranchAddressOfficial().getPkAddressId() : 0);
        moDpsOrder.setFkBizPartnerAltId_r(moBizPartner.getPkBizPartnerId());
        moDpsOrder.setFkBizPartnerBranchAltId(moBizPartner.getDbmsBizPartnerBranches().size() > 0 ? moBizPartner.getDbmsBizPartnerBranches().get(0).getPkBizPartnerBranchId() : 0);
        moDpsOrder.setFkBizPartnerBranchAddressAltId(moBizPartner.getDbmsBizPartnerBranches().size() > 0 ? moBizPartner.getDbmsBizPartnerBranches().get(0).getDbmsBizPartnerBranchAddressOfficial().getPkAddressId() : 0);
        moDpsOrder.setFkPaymentSystemTypeId(1);
        moDpsOrder.setFkTaxIdentityEmisorTypeId(moBizPartner.getFkTaxIdentityId());
        //moDpsOrder.setFkTaxIdentityReceptorTypeId(miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFkTaxIdentityId());
        //moDpsOrder.setFkTaxIdentityReceptorTypeId(((SDataParamsCompany) miClient_xxx.getSessionXXX().getDbmsDataCompany()).getFkTaxIdentityId()); XXX Review with the Fk from SDataParamsCompany
        moDpsOrder.setFkTaxIdentityReceptorTypeId(miClient_xxx.getSessionXXX().getCompany().getDbmsDataCompany().getFkTaxIdentityId());
        moDpsOrder.setFkIncotermId(mnFkIncotermId);
        moDpsOrder.setFkSpotSourceId_n(mnFkSpotSourceId);
        moDpsOrder.setFkSpotDestinyId_n(mnFkSpotDestinyId);
        moDpsOrder.setFkModeOfTransportationTypeId(mnFkModeOfTransportationTypeId);
        moDpsOrder.setFkCarrierTypeId(mnFkCarrierTypeId);
        moDpsOrder.setFkCarrierId_n(mnFkCarrierId);
        moDpsOrder.resetRecord();

        moDpsOrder.setIsRecordAutomatic(true);

        // Document's series:

        obtainNextNumber(session);

        // Document's payment type:

        if (oBizPartnerCategory.getEffectiveCreditTypeId() == SDataConstantsSys.BPSS_TP_CRED_CRED_NO) {
            moDpsOrder.setFkPaymentTypeId(SDataConstantsSys.TRNS_TP_PAY_CASH);
        }
        else {
            moDpsOrder.setFkPaymentTypeId(SDataConstantsSys.TRNS_TP_PAY_CREDIT);
            moDpsOrder.setDaysOfCredit(oBizPartnerCategory.getEffectiveDaysOfCredit());
        }

        // Document's language:

        if (oBizPartnerCategory.getFkLanguageId_n() == SLibConstants.UNDEFINED) {
            moDpsOrder.setFkLanguajeId(((SDataParamsErp) session.getConfigSystem()).getFkLanguageId());
        }
        else {
            moDpsOrder.setFkLanguajeId(oBizPartnerCategory.getFkLanguageId_n());
        }

        // Document's currency:

        if (oBizPartnerCategory.getFkCurrencyId_n() == SLibConstants.UNDEFINED) {
            moDpsOrder.setFkCurrencyId(((SDataParamsErp) session.getConfigSystem()).getFkCurrencyId());
        }
        else {
            moDpsOrder.setFkCurrencyId(oBizPartnerCategory.getFkCurrencyId_n());

            try {
                rate = STrnUtils.obtainExchangeRate(session, oBizPartnerCategory.getFkCurrencyId_n(), mtDate);
                moDpsOrder.setExchangeRate(rate);
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
                moDpsOrder = null;
            }
        }

        return moDpsOrder != null ? true : false;
    }

    private void calculateTotalDpsEntry(SGuiSession session, double dPrice) {
        double dDiscountUnitaryCy = 0;

        SDataBizPartnerBranch oParamBizPartnerBranch = new SDataBizPartnerBranch();

        //SDataBizPartnerBranch oParamBizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { moDpsOrder.getFkBizPartnerBranchId() }, SLibConstants.EXEC_MODE_VERBOSE);
        oParamBizPartnerBranch.read(new int[] { moDpsOrder.getFkBizPartnerBranchId() }, session.getStatement());
        if (oParamBizPartnerBranch == null) {
            oParamBizPartnerBranch = new SDataBizPartnerBranch();
        }

        // Unitary discount settings:

        moDpsOrderEntry.setPriceUnitary(dPrice);
        moDpsOrderEntry.setPriceUnitaryCy(dPrice);
        moDpsOrderEntry.setPriceUnitaryReal_r(dPrice);
        moDpsOrderEntry.setPriceUnitaryRealCy_r(dPrice);

        if (moItem.getDbmsIsFreeDiscountUnitary()) {
            moDpsOrderEntry.setIsDiscountUnitaryPercentage(false);
            moDpsOrderEntry.setOriginalDiscountUnitaryCy(0d);
        }

        // Entry discount settings:

        if (moItem.getDbmsIsFreeDiscountEntry()) {
            moDpsOrderEntry.setIsDiscountEntryPercentage(false);
            moDpsOrderEntry.setDiscountEntryCy(0d);
        }

        // Document discount settings:

        if (!moDpsOrder.getIsDiscountDocApplying() || moItem.getDbmsIsFreeDiscountDoc()) {
            moDpsOrderEntry.setIsDiscountDocApplying(false);
            moDpsOrderEntry.setDiscountDocCy(0d);
        }

        // Obtain item price with discount included or with discount separate:

        try {
            //dItemPrice = SDataUtilities.obtainItemPrice(null, session.getStatement(), moBizPartner.getPkBizPartnerId(), moItem.getPkItemId(), moDps.getDateDoc());
            dDiscountUnitaryCy = STrnUtils.obtainLastPriceForSupplierItem(session, SModSysConsts.TRNS_CT_DPS_PUR,  moItem.getPkItemId(), "" + moBizPartner.getPkBizPartnerId());

            // Check document currency:

            if (SLibUtilities.compareKeys(new int[] { moDpsOrder.getFkCurrencyId() }, new int[] { ((SDataParamsErp) session.getConfigSystem()).getFkCurrencyId() })) {
                moDpsOrderEntry.setOriginalPriceUnitaryCy(dPrice); //(Double) aux[2]);
                moDpsOrderEntry.setDiscountUnitaryCy(dDiscountUnitaryCy);
            }
            else {
                moDpsOrderEntry.setOriginalPriceUnitaryCy(moDpsOrder.getExchangeRate() != 0 ? dPrice/moDpsOrder.getExchangeRate() : 0);
                moDpsOrderEntry.setOriginalDiscountUnitaryCy(moDpsOrder.getExchangeRate() != 0 ? dPrice/moDpsOrder.getExchangeRate() : 0);
                moDpsOrderEntry.setOriginalPriceUnitarySystemCy(moDpsOrder.getExchangeRate() != 0 ? dPrice/moDpsOrder.getExchangeRate() : 0); // XXX
                /*
                moDpsEntry.setOriginalPriceUnitaryCy(moDps.getExchangeRate() != 0 ? (Double) aux[2]/moDps.getExchangeRate() : 0);
                moDpsEntry.setOriginalDiscountUnitaryCy(moDps.getExchangeRate() != 0 ? (Double) aux[3]/moDps.getExchangeRate() : 0);
                moDpsEntry.setOriginalPriceUnitarySystemCy(moDps.getExchangeRate() != 0 ? (Double) aux[2]/moDps.getExchangeRate() : 0); // XXX
                 */
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
        }

        moDpsOrderEntry.setQuantity(1);
        moDpsOrderEntry.setOriginalQuantity(1);
        moDpsOrderEntry.setIsDiscountUnitaryPercentageSystem(false); // XXX
        moDpsOrderEntry.setDiscountUnitaryPercentage(0);
        moDpsOrderEntry.setDiscountUnitaryPercentageSystem(0); // XXX
        moDpsOrderEntry.setDiscountEntryPercentage(0);
        moDpsOrderEntry.setOriginalDiscountUnitarySystemCy(0); // XXX
        moDpsOrderEntry.setIsTaxesAutomaticApplying(true);

        // Calculate DPS entry's value:

        moDpsOrderEntry.setFkItemId(moItem.getPkItemId());
        moDpsOrderEntry.setFkUnitId(moItem.getFkUnitId());
        moDpsOrderEntry.setFkOriginalUnitId(moItem.getFkUnitId());
        moDpsOrderEntry.setFkTaxRegionId(oParamBizPartnerBranch.getFkTaxRegionId_n() != 0 ? oParamBizPartnerBranch.getFkTaxRegionId_n() : ((SDataParamsCompany) session.getConfigCompany()).getFkDefaultTaxRegionId_n());
        moDpsOrderEntry.setDbmsFkItemGenericId(moItem.getFkItemGenericId());

        moDpsOrderEntry.calculateTotal(miClient_xxx, moDpsOrder.getDate(),
                moDpsOrder.getFkTaxIdentityEmisorTypeId(), moDpsOrder.getFkTaxIdentityReceptorTypeId(),
                moDpsOrder.getIsDiscountDocPercentage(), moDpsOrder.getDiscountDocPercentage(), moDpsOrder.getExchangeRate());

        if (moItem.getDbmsDataItemGeneric() != null) {
            if (moItem.getDbmsDataItemGeneric().getIsLengthApplying() && !moItem.getIsLengthVariable() && !moItem.getDbmsDataItemGeneric().getIsLengthVariable()) {
                moDpsOrderEntry.setLength(moDpsOrderEntry.getQuantity() * moItem.getLength());
            }
            if (moItem.getDbmsDataItemGeneric().getIsMassApplying() && !moItem.getIsMassVariable() && !moItem.getDbmsDataItemGeneric().getIsMassVariable()) {
                moDpsOrderEntry.setMass(moDpsOrderEntry.getQuantity() * moItem.getMass());
            }
            if (moItem.getDbmsDataItemGeneric().getIsWeightGrossApplying()) {
                moDpsOrderEntry.setWeightGross(moDpsOrderEntry.getQuantity() * moItem.getWeightGross());
            }
            if (moItem.getDbmsDataItemGeneric().getIsWeightDeliveryApplying()) {
                moDpsOrderEntry.setWeightDelivery(moDpsOrderEntry.getQuantity() * moItem.getWeightDelivery());
            }
        }
    }

    private void calculateTotalDps(SGuiSession session) throws SQLException, Exception {

        moDpsOrder.setFkLanguajeId(((SDataParamsErp) session.getConfigSystem()).getFkLanguageId());
        moDpsOrder.setFkCurrencyId(((SDataParamsErp) session.getConfigSystem()).getFkCurrencyId());
        moDpsOrder.setExchangeRateSystem(1);
        moDpsOrder.setExchangeRate(1);

        moDpsOrder.setIsDiscountDocApplying(false);
        moDpsOrder.setIsDiscountDocPercentage(false);
        moDpsOrder.setDiscountDocPercentage(0);

        // Calculate and render document's value:

        moDpsOrder.calculateTotal(null);
    }

    private boolean getRegistryPurchaseOrder(SGuiSession session) throws SQLException, Exception {
        boolean b = true;
        String item = "";
        ArrayList<Object[]> vDpsEntries = null;

        SDataDpsEntry oDpsOrderEntry = null;

        try {
            // Obtain dpsEntries with information of cfg_param_erp:

            vDpsEntries = obtainDpsEntriesIdsForShipment(session);
        } catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        //moBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, moDestinationEntry != null ? new int[] { moDestinationEntry.getFkCarrierId() } : 0, SLibConstants.EXEC_MODE_VERBOSE);
        moBizPartner = new SDataBizPartner();
        moBizPartner.read(new int[] { mnFkCarrierId }, session.getStatement());
        if (moBizPartner == null) {
            msQueryResult = "No se encontro el proveedor para la creación de la órden de compra de fletes.";
            b = false;
        }
        else if (vDpsEntries == null) {
            msQueryResult = "No se encontro la configuración de ítems para la orden de compra.";
            b = false;
        }
        else if (moBizPartner.getDbmsCategorySettingsSup() == null) {
            msQueryResult = "El asociado de negocios '" + moBizPartner.getBizPartner() + "' no tiene categoría de proveedor (transportista).";
            b = false;
        }
        else {
            // Create purchase order:

            b = (moDpsOrder != null && moDpsOrder.getPkYearId() > 0 && moDpsOrder.getPkDocId() > 0) ? true : createPurchaseOrder(session);

            if(!b) {
               msQueryResult = "No se pudo crear el documento de la orden de compra.";
            }
            else {
                // Check if document is new:

                if (moDpsOrder.getPkDocId()==0) {
                    moDpsOrder.getDbmsDpsEntries().removeAllElements();
                }
                else {
                    moDpsOrder.setFkUserEditId(session.getUser().getPkUserId());
                }

                for (Object[] oLogEntry : vDpsEntries) {

                    if (oLogEntry == null) {
                        msQueryResult = "No se pudo leer la partida para la orden de compra.";
                        b = false;
                        break;
                    }

                    // Read item:

                    //moItem = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { ((Number) oLogEntry[0]).intValue() }, SLibConstants.EXEC_MODE_VERBOSE);
                    moItem = new SDataItem();
                    moItem.read(new int[] { ((Number) oLogEntry[0]).intValue() }, session.getStatement());
                    if (moItem == null) {
                        msQueryResult = "No se pudo leer el ítem.";
                        b = false;
                        break;
                    }

                    // Assign date delivery document:

                    if (moDpsOrder.getDateDocDelivery_n() == null) {
                        // moDpsOrder.setDateDocDelivery_n(moFieldDateDelivery.getDate()); XXX
                    }

                    // Create dps entry:

                    moDpsOrderEntry = new SDataDpsEntry();
                    if (moDpsOrder.getPkYearId()>0 && moDpsOrder.getPkDocId()>0) {

                        // Look up dps entry:

                        for (int i=0; i<moDpsOrder.getDbmsDpsEntries().size(); i++) {
                            oDpsOrderEntry = moDpsOrder.getDbmsDpsEntries().get(i);
                            if (((Number) oLogEntry[0]).intValue() == oDpsOrderEntry.getFkItemId()) {
                                moDpsOrderEntry = oDpsOrderEntry;

                                // Check if calcuate dps entry o delete if value is zero:

                                if (((Number) oLogEntry[1]).doubleValue()>0) {

                                    if (moDpsOrderEntry.getIsDeleted()) {
                                        moDpsOrderEntry.setIsDeleted(false);
                                    }
                                    calculateTotalDpsEntry(session, ((Number) oLogEntry[1]).doubleValue());
                                }
                                else {
                                    moDpsOrderEntry.setIsDeleted(true);
                                }

                                // Obtain cost center:

                                try {
                                    moDpsOrderEntry.setFkCostCenterId_n(STrnUtils.obtainCostCenterItem(session, moDpsOrderEntry.getFkItemId()));
                                }
                                catch (Exception e) {
                                    SLibUtilities.renderException(this, e);
                                }

                                moDpsOrderEntry.setIsRegistryEdited(true);
                                moDpsOrderEntry.setFkUserEditId(session.getUser().getPkUserId());
                                moDpsOrder.getDbmsDpsEntries().set(i, moDpsOrderEntry);
                                break;
                            }
                        }
                    }

                    // Check if dpsEntry is new:

                    if (moDpsOrderEntry.getPkDocId()==0 && ((Number) oLogEntry[1]).doubleValue()>0) {

                        moDpsOrderEntry.setPkYearId(SLibTimeUtils.digestYear(mtDate)[0]);
                        moDpsOrderEntry.setFkUserNewId(session.getUser().getPkUserId());
                        moDpsOrderEntry.setFkDpsAdjustmentTypeId(SDataConstantsSys.TRNS_TP_DPS_ADJ_NA);
                        moDpsOrderEntry.setFkDpsEntryTypeId(SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY);
                        moDpsOrderEntry.setFkItemId(((Number) oLogEntry[0]).intValue());

                        // Item concept:
                        /*
                         * XXX No required
                        if (moDps.getFkLanguajeId() != ((SDataParamsErp) session.getConfigSystem()).getFkLanguageId()) {
                            for (SDataItemForeignLanguage description : moItem.getDbmsItemForeignLanguageDescriptions()) {
                                if (moDps.getFkLanguajeId() == description.getPkLanguageId()) {
                                    item = description.getItemShort();
                                    break;
                                }
                            }
                        }

                        if (item.length() == 0) {
                            item = moItem.getItemShort();
                        }
                        */

                        moDpsOrderEntry.setConceptKey(moItem.getKey());
                        moDpsOrderEntry.setConcept(moItem.getItem());
                        moDpsOrderEntry.setLength(moItem.getLength());
                        moDpsOrderEntry.setSurface(moItem.getSurface());
                        moDpsOrderEntry.setVolume(moItem.getVolume());
                        moDpsOrderEntry.setMass(moItem.getMass());
                        moDpsOrderEntry.setWeightGross(moItem.getWeightGross());
                        moDpsOrderEntry.setWeightDelivery(moItem.getWeightDelivery());
                        moDpsOrderEntry.setSurplusPercentage(0);
                        moDpsOrderEntry.setOperationsType(SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS);
                        moDpsOrderEntry.setFkItemRefId_n(((Number) oLogEntry[0]).intValue());
                        moDpsOrderEntry.setIsInventoriable(moItem.getIsInventoriable());
                        moDpsOrderEntry.setIsDeleted(false);
                        moDpsOrderEntry.setFkDpsAdjustmentTypeId(SDataConstantsSys.TRNS_TP_DPS_ADJ_NA);
                        moDpsOrderEntry.setFkDpsAdjustmentSubtypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[1]);

                        // Obtain cost center:

                        try {
                            moDpsOrderEntry.setFkCostCenterId_n(STrnUtils.obtainCostCenterItem(session, moItem.getPkItemId()));
                        }
                        catch (Exception e) {
                            SLibUtilities.renderException(this, e);
                        }

                        calculateTotalDpsEntry(session, ((Number) oLogEntry[1]).doubleValue());
                        moDpsOrder.getDbmsDpsEntries().add(moDpsOrderEntry);
                    }
                }

                calculateTotalDps(session);
            }
        }

        return b;
    }

    private void validateCapacityMassUsed(SGuiSession session) {
        SDataUserConfigurationTransaction userConfig = new SDataUserConfigurationTransaction();
        userConfig.read(new int[] { session.getUser().getPkUserId() } , session.getStatement());

        if (mnFkCarrierTypeId == SModSysConsts.LOGS_TP_CAR_CAR &&
                (mdCapacityMassUsed / mdCapacityMass) < userConfig.getCapacityMassMinPercentage()) {
            mnFkShipmentAuthorizationStatusId = SModSysConsts.TRNS_ST_DPS_AUTHORN_PENDING;
        }
        else {
            mnFkShipmentAuthorizationStatusId = SModSysConsts.TRNS_ST_DPS_AUTHORN_AUTHORN;
        }
    }

    private String obtainCodeDeliveryType(SGuiSession session) throws SQLException, Exception {
        String code = "";

        ResultSet resultSet = null;
        Statement statement = null;

        msSql = "SELECT code FROM " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_DLY) + " WHERE id_tp_dly = " + mnFkDeliveryTypeId;

        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);

        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            code = resultSet.getString(1);
        }

        return code;
    }

    private String validateShipmentDestinyEntries(SGuiSession session) {
        ResultSet resultSet = null;
        Statement statement = null;

        for (SDbShipmentDestiny destiny : maShipmentDestinies) {
            for (SDbShipmentDestinyEntry destinyEntry : destiny.getShipmentDestinyEntries()) {
                try {
                    msSql = "SELECT IF((SUM(sde.qty) + " + destinyEntry.getQuantity() + ") > " +
                            (destinyEntry.getXtaDoctoQuantity() - destinyEntry.getXtaDoctoQuantityAdjustment()) + ", 1, 0) AS f_exist " +
                        "FROM " +  SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS s " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " AS sd ON s.id_ship = sd.id_ship " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS sde ON sd.id_ship = sde.id_ship AND sd.id_dest = sde.id_dest " +
                        "WHERE s.b_del = 0 AND " + (destinyEntry.getFkDpsYearId_n() > 0 ?
                        "sde.fk_dps_year_n = " + destinyEntry.getFkDpsYearId_n() + " AND " +
                        "sde.fk_dps_doc_n = " + destinyEntry.getFkDpsDocId_n() + " AND " +
                        "sde.fk_dps_ety_n = " + destinyEntry.getFkDpsEntryId_n() :
                        "sde.fk_diog_year_n = " + destinyEntry.getFkDiogYearId_n() + " AND " +
                        "sde.fk_diog_doc_n = " + destinyEntry.getFkDiogDocId_n() + " AND " +
                        "sde.fk_diog_ety_n = " + destinyEntry.getFkDiogEntryId_n()) + " AND sde.id_ship <> " + mnPkShipmentId;

                    statement = session.getDatabase().getConnection().createStatement();
                    resultSet = statement.executeQuery(msSql);
                    if (resultSet.next()) {
                        if (resultSet.getInt(1) > 0) {

                            msQueryResult = "El embarque contiene destinos con partidas que ya fueron embarcadas en otro embarque.";
                            break;
                        }
                    }
                } catch (SQLException e) {
                    SLibUtils.showException(this, e);
                }
            }
        }

        return msQueryResult.isEmpty() ? "" : msQueryResult;
    }

    /*
     * Public methods:
     */

    public void setPkShipmentId(int n) { mnPkShipmentId = n; }
    public void setNumber(String s) { msNumber = s; }
    public void setDate(Date t) { mtDate = t; }
    public void setRate(double d) { mdRate = d; }
    public void setExportDeclaration(double d) { mdExportDeclaration = d; }
    public void setCarriageExportFreight(double d) { mdCarriageExportFreight = d; }
    public void setCarriageExportDelay(double d) { mdCarriageExportDelay = d; }
    public void setCarriageExportOther(double d) { mdCarriageExportOther = d; }
    public void setUploadingExport(double d) { mdUploadingExport = d; }
    public void setLoadingExport(double d) { mdLoadingExport = d; }
    public void setCarriageImport(double d) { mdCarriageImport = d; }
    public void setUploadingImport(double d) { mdUploadingImport = d; }
    public void setInsurance(double d) { mdInsurance = d; }
    public void setLoadingImport(double d) { mdLoadingImport = d; }
    public void setCarriageImportFreight(double d) { mdCarriageImportFreight = d; }
    public void setCarriageImportDelay(double d) { mdCarriageImportDelay = d; }
    public void setCarriageImportOther(double d) { mdCarriageImportOther = d; }
    public void setCustomsImport(double d) { mdCustomsImport = d; }
    public void setTariff(double d) { mdTariff = d; }
    public void setCost_r(double d) { mdCost_r = d; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setCostCy_r(double d) { mdCostCy_r = d; }
    public void setCapacityVolume(double d) { mdCapacityVolume = d; }
    public void setCapacityVolumeUsed(double d) { mdCapacityVolumeUsed = d; }
    public void setCapacityMass(double d) { mdCapacityMass = d; }
    public void setCapacityMassUsed(double d) { mdCapacityMassUsed = d; }
    public void setDriver(String s) { msDriver = s; }
    public void setPlate(String s) { msPlate = s; }
    public void setConsolidated(boolean b) { mbConsolidated = b; }
    public void setBillOfLadingClosed(boolean b) { mbBillOfLadingClosed = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkShipmentTypeId(int n) { mnFkShipmentTypeId = n; }
    public void setFkDeliveryTypeId(int n) { mnFkDeliveryTypeId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkIncotermId(int n) { mnFkIncotermId = n; }
    public void setFkSpotSourceId(int n) { mnFkSpotSourceId = n; }
    public void setFkSpotDestinyId(int n) { mnFkSpotDestinyId = n; }
    public void setFkModeOfTransportationTypeId(int n) { mnFkModeOfTransportationTypeId = n; }
    public void setFkCarrierTypeId(int n) { mnFkCarrierTypeId = n; }
    public void setFkCarrierId(int n) { mnFkCarrierId = n; }
    public void setFkVehicleTypeId(int n) { mnFkVehicleTypeId = n; }
    public void setFkVehicleId_n(int n) { mnFkVehicleId_n = n; }
    public void setFkShipmentAuthorizationStatusId(int n) { mnFkShipmentAuthorizationStatusId = n; }
    public void setFkUnitCapacityVolumeId(int n) { mnFkUnitCapacityVolumeId = n; }
    public void setFkUnitCapacityMassId(int n) { mnFkUnitCapacityMassId = n; }
    public void setFkOrderYearId_n(int n) { mnFkOrderYearId_n = n; }
    public void setFkOrderDocId_n(int n) { mnFkOrderDocId_n = n; }
    public void setFkUserBillOfLadingId(int n) { mnFkUserBillOfLadingId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserBillOfLading(Date t) { mtTsUserBillOfLading = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkShipmentId() { return mnPkShipmentId; }
    public String getNumber() { return msNumber; }
    public Date getDate() { return mtDate; }
    public double getRate() { return mdRate; }
    public double getExportDeclaration() { return mdExportDeclaration; }
    public double getCarriageExportFreight() { return mdCarriageExportFreight; }
    public double getCarriageExportDelay() { return mdCarriageExportDelay; }
    public double getCarriageExportOther() { return mdCarriageExportOther; }
    public double getUploadingExport() { return mdUploadingExport; }
    public double getLoadingExport() { return mdLoadingExport; }
    public double getCarriageImport() { return mdCarriageImport; }
    public double getUploadingImport() { return mdUploadingImport; }
    public double getInsurance() { return mdInsurance; }
    public double getLoadingImport() { return mdLoadingImport; }
    public double getCarriageImportFreight() { return mdCarriageImportFreight; }
    public double getCarriageImportDelay() { return mdCarriageImportDelay; }
    public double getCarriageImportOther() { return mdCarriageImportOther; }
    public double getCustomsImport() { return mdCustomsImport; }
    public double getTariff() { return mdTariff; }
    public double getCost_r() { return mdCost_r; }
    public double getExchangeRate() { return mdExchangeRate; }
    public double getCostCy_r() { return mdCostCy_r; }
    public double getCapacityVolume() { return mdCapacityVolume; }
    public double getCapacityVolumeUsed() { return mdCapacityVolumeUsed; }
    public double getCapacityMass() { return mdCapacityMass; }
    public double getCapacityMassUsed() { return mdCapacityMassUsed; }
    public String getDriver() { return msDriver; }
    public String getPlate() { return msPlate; }
    public boolean isConsolidated() { return mbConsolidated; }
    public boolean isBillOfLadingClosed() { return mbBillOfLadingClosed; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkShipmentTypeId() { return mnFkShipmentTypeId; }
    public int getFkDeliveryTypeId() { return mnFkDeliveryTypeId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkIncotermId() { return mnFkIncotermId; }
    public int getFkSpotSourceId() { return mnFkSpotSourceId; }
    public int getFkSpotDestinyId() { return mnFkSpotDestinyId; }
    public int getFkModeOfTransportationTypeId() { return mnFkModeOfTransportationTypeId; }
    public int getFkCarrierTypeId() { return mnFkCarrierTypeId; }
    public int getFkCarrierId() { return mnFkCarrierId; }
    public int getFkVehicleTypeId() { return mnFkVehicleTypeId; }
    public int getFkVehicleId_n() { return mnFkVehicleId_n; }
    public int getFkShipmentAuthorizationStatusId() { return mnFkShipmentAuthorizationStatusId; }
    public int getFkUnitCapacityVolumeId() { return mnFkUnitCapacityVolumeId; }
    public int getFkUnitCapacityMassId() { return mnFkUnitCapacityMassId; }
    public int getFkOrderYearId_n() { return mnFkOrderYearId_n; }
    public int getFkOrderDocId_n() { return mnFkOrderDocId_n; }
    public int getFkUserBillOfLadingId() { return mnFkUserBillOfLadingId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserBillOfLading() { return mtTsUserBillOfLading; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setXtaLogisticsOrderAutomatic(boolean b) { mbLogisticsOrderAutomatic = b; }
    public void setXtaShipmentNumber(String s) { msXtaShipmentNumber = s; }
    public void setXtaDocNumberSeriesLogistics(String s) { msXtaDocNumberSeriesLogistics = s; }
    public void setXtaStatusDpsAuthorn(String s) { msXtaStatusDpsAuthorn = s; }
    public void setXtaOrderNumber(String s) { msXtaOrderNumber = s; }
    public void setShipmentDestinies(ArrayList<SDbShipmentDestiny> v) { maShipmentDestinies = v; }
    public void setShipmentNotes(ArrayList<SDbShipmentNotes> v) { maShipmentNotes = v; }
    public void setClient_XXX(SClientInterface o) { miClient_xxx = o; }
    public void setItem(SDataItem o) { moItem = o; }
    public void setBizPartner(SDataBizPartner o) { moBizPartner = o; }
    public void setDpsOrder(SDataDps o) { moDpsOrder = o; }
    public void setDpsOrderEntry(SDataDpsEntry o) { moDpsOrderEntry = o; }

    public boolean getXtaLogisticsOrderAutomatic() { return mbLogisticsOrderAutomatic; }
    public String getXtaShipmentNumber() { return msXtaShipmentNumber; }
    public String getXtaDocNumberSeriesLogistics() { return msXtaDocNumberSeriesLogistics; }
    public String getXtaStatusDpsAuthorn() { return msXtaStatusDpsAuthorn; }
    public String getXtaOrderNumber() { return msXtaOrderNumber; }
    public ArrayList<SDbShipmentDestiny> getShipmentDestinies() { return maShipmentDestinies; }
    public ArrayList<SDbShipmentNotes> getShipmentNotes() { return maShipmentNotes; }
    public SClientInterface getClient_XXX() { return miClient_xxx; }
    public SDataItem getItem() { return moItem; }
    public SDataBizPartner getBizPartner() { return moBizPartner; }
    public SDataDps getDpsOrder() { return moDpsOrder; }
    public SDataDpsEntry getDpsOrderEntry() { return moDpsOrderEntry; }

    public String validateOrderLinks(SGuiSession session) {
        ResultSet resultSet = null;
        Statement statement = null;

        try {
            msSql = "SELECT COUNT(*) AS f_count " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " AS s " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON s.id_src_year = d.id_year AND s.id_src_doc = d.id_doc AND d.b_del = 0 " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS e ON d.id_year = e.id_year AND d.id_doc = e.id_doc AND e.b_del = 0 " +
                "WHERE s.id_src_year = " + mnFkOrderYearId_n + " AND s.id_src_doc = " + mnFkOrderDocId_n + " " +
                "GROUP BY s.id_src_year, s.id_src_doc; ";

            statement = session.getDatabase().getConnection().createStatement();
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                if (resultSet.getInt(1) > 0) {

                    msQueryResult = "La orden de compra se encuentra surtida de forma parcial o completa.";
                }
            }
        } catch (SQLException e) {
            SLibUtils.showException(this, e);
        }

        return msQueryResult.isEmpty() ? "" : msQueryResult;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkShipmentId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkShipmentId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkShipmentId = 0;
        msNumber = "";
        mtDate = null;
        mdRate = 0;
        mdExportDeclaration = 0;
        mdCarriageExportFreight = 0;
        mdCarriageExportDelay = 0;
        mdCarriageExportOther = 0;
        mdUploadingExport = 0;
        mdLoadingExport = 0;
        mdCarriageImport = 0;
        mdUploadingImport = 0;
        mdInsurance = 0;
        mdLoadingImport = 0;
        mdCarriageImportFreight = 0;
        mdCarriageImportDelay = 0;
        mdCarriageImportOther = 0;
        mdCustomsImport = 0;
        mdTariff = 0;
        mdCost_r = 0;
        mdExchangeRate = 0;
        mdCostCy_r = 0;
        mdCapacityVolume = 0;
        mdCapacityVolumeUsed = 0;
        mdCapacityMass = 0;
        mdCapacityMassUsed = 0;
        msDriver = "";
        msPlate = "";
        mbConsolidated = false;
        mbBillOfLadingClosed = false;
        mbDeleted = false;
        mnFkCompanyBranchId = 0;
        mnFkShipmentTypeId = 0;
        mnFkDeliveryTypeId = 0;
        mnFkCurrencyId = 0;
        mnFkIncotermId = 0;
        mnFkSpotSourceId = 0;
        mnFkSpotDestinyId = 0;
        mnFkModeOfTransportationTypeId = 0;
        mnFkCarrierTypeId = 0;
        mnFkCarrierId = 0;
        mnFkVehicleTypeId = 0;
        mnFkVehicleId_n = 0;
        mnFkShipmentAuthorizationStatusId = 0;
        mnFkUnitCapacityVolumeId = 0;
        mnFkUnitCapacityMassId = 0;
        mnFkOrderYearId_n = 0;
        mnFkOrderDocId_n = 0;
        mnFkUserBillOfLadingId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserBillOfLading = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mbLogisticsOrderAutomatic = false;
        msXtaShipmentNumber = "";
        msXtaDocNumberSeriesLogistics = "";
        msXtaStatusDpsAuthorn = "";
        msXtaOrderNumber = "";

        miClient_xxx = null;

        moItem = null;
        moBizPartner = null;
        moDpsOrder = null;
        moDpsOrderEntry = null;

        maShipmentDestinies = new ArrayList<SDbShipmentDestiny>();
        maShipmentNotes = new ArrayList<SDbShipmentNotes>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_ship = " + mnPkShipmentId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ship = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkShipmentId = 0;

        msSql = "SELECT COALESCE(MAX(id_ship), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkShipmentId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        SDbShipmentDestiny destiny = null;
        SDbShipmentNotes notes = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT v.*, aut.st_dps_authorn, IF(LENGTH(d.num_ser) > 0, CONCAT(d.num_ser, '-', d.num), d.num) AS f_order_num " +
            "FROM " + getSqlTable() + " AS v " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_DPS_AUTHORN) + " AS aut ON v.fk_st_ship_authorn = aut.id_st_dps_authorn " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON v.fk_ord_year_n = d.id_year AND v.fk_ord_doc_n = d.id_doc " +
            "WHERE v.id_ship = " + pk[0];
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkShipmentId = resultSet.getInt("v.id_ship");
            msNumber = resultSet.getString("v.num");
            mtDate = resultSet.getDate("v.dt");
            mdRate = resultSet.getDouble("v.rate");
            mdExportDeclaration = resultSet.getDouble("v.cust_exp");
            mdCarriageExportFreight = resultSet.getDouble("v.carr_exp_frei");
            mdCarriageExportDelay = resultSet.getDouble("v.carr_exp_stay");
            mdCarriageExportOther = resultSet.getDouble("v.carr_exp_oth");
            mdUploadingExport = resultSet.getDouble("v.upld_exp");
            mdLoadingExport = resultSet.getDouble("v.load_exp");
            mdCarriageImport = resultSet.getDouble("v.carr");
            mdUploadingImport = resultSet.getDouble("v.upld_imp");
            mdInsurance = resultSet.getDouble("v.insu");
            mdLoadingImport = resultSet.getDouble("v.load_imp");
            mdCarriageImportFreight = resultSet.getDouble("v.carr_imp_frei");
            mdCarriageImportDelay = resultSet.getDouble("v.carr_imp_stay");
            mdCarriageImportOther = resultSet.getDouble("v.carr_imp_oth");
            mdCustomsImport = resultSet.getDouble("v.cust_imp");
            mdTariff = resultSet.getDouble("v.tari");
            mdCost_r = resultSet.getDouble("v.cost_r");
            mdExchangeRate = resultSet.getDouble("v.exc_rate");
            mdCostCy_r = resultSet.getDouble("v.cost_cur_r");
            mdCapacityVolume = resultSet.getDouble("v.cap_vol");
            mdCapacityVolumeUsed = resultSet.getDouble("v.cap_vol_used_r");
            mdCapacityMass = resultSet.getDouble("v.cap_mass");
            mdCapacityMassUsed = resultSet.getDouble("v.cap_mass_used_r");
            msDriver = resultSet.getString("v.driver");
            msPlate = resultSet.getString("v.plate");
            mbConsolidated = resultSet.getBoolean("v.b_con");
            mbBillOfLadingClosed = resultSet.getBoolean("v.b_bol");
            mbDeleted = resultSet.getBoolean("v.b_del");
            mnFkCompanyBranchId = resultSet.getInt("v.fk_cob");
            mnFkShipmentTypeId = resultSet.getInt("v.fk_tp_ship");
            mnFkDeliveryTypeId = resultSet.getInt("v.fk_tp_dly");
            mnFkCurrencyId = resultSet.getInt("v.fk_cur");
            mnFkIncotermId = resultSet.getInt("v.fk_inc");
            mnFkSpotSourceId = resultSet.getInt("v.fk_spot_src");
            mnFkSpotDestinyId = resultSet.getInt("v.fk_spot_des");
            mnFkModeOfTransportationTypeId = resultSet.getInt("v.fk_tp_mot");
            mnFkCarrierTypeId = resultSet.getInt("v.fk_tp_car");
            mnFkCarrierId = resultSet.getInt("v.fk_car");
            mnFkVehicleTypeId = resultSet.getInt("v.fk_tp_veh");
            mnFkVehicleId_n = resultSet.getInt("v.fk_veh_n");
            mnFkShipmentAuthorizationStatusId = resultSet.getInt("v.fk_st_ship_authorn");
            mnFkUnitCapacityVolumeId = resultSet.getInt("v.fk_unit_cap_vol");
            mnFkUnitCapacityMassId = resultSet.getInt("v.fk_unit_cap_mass");
            mnFkOrderYearId_n = resultSet.getInt("v.fk_ord_year_n");
            mnFkOrderDocId_n = resultSet.getInt("v.fk_ord_doc_n");
            mnFkUserBillOfLadingId = resultSet.getInt("v.fk_usr_bol");
            mnFkUserInsertId = resultSet.getInt("v.fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("v.fk_usr_upd");
            mtTsUserBillOfLading = resultSet.getTimestamp("v.ts_usr_bol");
            mtTsUserInsert = resultSet.getTimestamp("v.ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("v.ts_usr_upd");

            msXtaShipmentNumber = obtainCodeDeliveryType(session) + "-" + resultSet.getString("v.num");
            msXtaStatusDpsAuthorn = resultSet.getString("aut.st_dps_authorn");
            msXtaOrderNumber = resultSet.getString("f_order_num");

            mbRegistryNew = false;
        }

        // Read destinies:

        msSql = "SELECT id_ship, id_dest FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " WHERE id_ship = " + mnPkShipmentId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        while (resultSet.next()) {

            destiny = new SDbShipmentDestiny();
            destiny.read(session, new int[] { resultSet.getInt("id_ship"), resultSet.getInt("id_dest") } );
            maShipmentDestinies.add(destiny);
        }

        // Read notes:

        msSql = "SELECT id_ship, id_nts FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_NTS) + " WHERE id_ship = " + mnPkShipmentId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        while (resultSet.next()) {

            notes = new SDbShipmentNotes();
            notes.read(session, new int[] { resultSet.getInt("id_ship"), resultSet.getInt("id_nts") } );
            maShipmentNotes.add(notes);
        }

        // Read purchase order:

        moDpsOrder = new SDataDps();
        if (mnFkOrderYearId_n > 0  && mnFkOrderDocId_n > 0) {
            moDpsOrder.read(new int[] { mnFkOrderYearId_n, mnFkOrderDocId_n } , session.getStatement());
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        boolean saveShipment = true;
        Statement statement = null;

        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        // Save purchase order if apply:

        if (miClient_xxx.getSessionXXX().getParamsCompany().getIsLogisticsOrderAutomatic()) {

            if (moDpsOrder != null) {
                moDpsOrder.setDbmsDataCfd(null);
                moDpsOrder.save(session.getDatabase().getConnection());

                if (moDpsOrder.getLastDbActionResult() != SLibConstants.DB_ACTION_READ_ERROR) {

                    mnFkOrderYearId_n = moDpsOrder.getPkYearId();
                    mnFkOrderDocId_n = moDpsOrder.getPkDocId();

                    if (moDpsOrder.getIsDeleted()) {
                        mnFkOrderYearId_n = 0;
                        mnFkOrderDocId_n = 0;
                    }

                    saveShipment = true;
                }
                else {

                    saveShipment = false;
                }
            }
        }

        if (!saveShipment) {
            mnQueryResultId = SDbConsts.SAVE_ERROR;
        }
        else {
            if (mbRegistryNew) {
                computePrimaryKey(session);
                mbDeleted = false;
                mnFkUserInsertId = session.getUser().getPkUserId();
                mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
                msNumber = "" + mnPkShipmentId;

                msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkShipmentId + ", " +
                    "'" + msNumber + "', " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    mdRate + ", " +
                    mdExportDeclaration + ", " +
                    mdCarriageExportFreight + ", " +
                    mdCarriageExportDelay + ", " +
                    mdCarriageExportOther + ", " +
                    mdUploadingExport + ", " +
                    mdLoadingExport + ", " +
                    mdCarriageImport + ", " +
                    mdUploadingImport + ", " +
                    mdInsurance + ", " +
                    mdLoadingImport + ", " +
                    mdCarriageImportFreight + ", " +
                    mdCarriageImportDelay + ", " +
                    mdCarriageImportOther + ", " +
                    mdCustomsImport + ", " +
                    mdTariff + ", " +
                    mdCost_r + ", " +
                    mdExchangeRate + ", " +
                    mdCostCy_r + ", " +
                    mdCapacityVolume + ", " +
                    mdCapacityVolumeUsed + ", " +
                    mdCapacityMass + ", " +
                    mdCapacityMassUsed + ", " +
                    "'" + msDriver + "', " +
                    "'" + msPlate + "', " +
                    (mbConsolidated ? 1 : 0) + ", " +
                    (mbBillOfLadingClosed ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkCompanyBranchId + ", " +
                    mnFkShipmentTypeId + ", " +
                    mnFkDeliveryTypeId + ", " +
                    mnFkCurrencyId + ", " +
                    mnFkIncotermId + ", " +
                    mnFkSpotSourceId + ", " +
                    mnFkSpotDestinyId + ", " +
                    mnFkModeOfTransportationTypeId + ", " +
                    mnFkCarrierTypeId + ", " +
                    mnFkCarrierId + ", " +
                    mnFkVehicleTypeId + ", " +
                    (mnFkVehicleId_n > 0 ? mnFkVehicleId_n : "NULL") + ", " +
                    mnFkShipmentAuthorizationStatusId + ", " +
                    mnFkUnitCapacityVolumeId + ", " +
                    mnFkUnitCapacityMassId + ", " +
                    (mnFkOrderYearId_n > 0 ? mnFkOrderYearId_n : "NULL") + ", " +
                    (mnFkOrderDocId_n > 0 ? mnFkOrderDocId_n : "NULL") + ", " +
                    mnFkUserBillOfLadingId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
            }
            else {
                mnFkUserUpdateId = session.getUser().getPkUserId();

                msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_ship = " + mnPkShipmentId + ", " +
                    "num = '" + msNumber + "', " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "rate = " + mdRate + ", " +
                    "cust_exp = " + mdExportDeclaration + ", " +
                    "carr_exp_frei = " + mdCarriageExportFreight + ", " +
                    "carr_exp_stay = " + mdCarriageExportDelay + ", " +
                    "carr_exp_oth = " + mdCarriageExportOther + ", " +
                    "upld_exp = " + mdUploadingExport + ", " +
                    "load_exp = " + mdLoadingExport + ", " +
                    "carr = " + mdCarriageImport + ", " +
                    "upld_imp = " + mdUploadingImport + ", " +
                    "insu = " + mdInsurance + ", " +
                    "load_imp = " + mdLoadingImport + ", " +
                    "carr_imp_frei = " + mdCarriageImportFreight + ", " +
                    "carr_imp_stay = " + mdCarriageImportDelay + ", " +
                    "carr_imp_oth = " + mdCarriageImportOther + ", " +
                    "cust_imp = " + mdCustomsImport + ", " +
                    "tari = " + mdTariff + ", " +
                    "cost_r = " + mdCost_r + ", " +
                    "exc_rate = " + mdExchangeRate + ", " +
                    "cost_cur_r = " + mdCostCy_r + ", " +
                    "cap_vol = " + mdCapacityVolume + ", " +
                    "cap_vol_used_r = " + mdCapacityVolumeUsed + ", " +
                    "cap_mass = " + mdCapacityMass + ", " +
                    "cap_mass_used_r = " + mdCapacityMassUsed + ", " +
                    "driver = '" + msDriver + "', " +
                    "plate = '" + msPlate + "', " +
                    "b_con = " + (mbConsolidated ? 1 : 0) + ", " +
                    "b_bol = " + (mbBillOfLadingClosed ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_cob = " + mnFkCompanyBranchId + ", " +
                    "fk_tp_ship = " + mnFkShipmentTypeId + ", " +
                    "fk_tp_dly = " + mnFkDeliveryTypeId + ", " +
                    "fk_cur = " + mnFkCurrencyId + ", " +
                    "fk_inc = " + mnFkIncotermId + ", " +
                    "fk_spot_src = " + mnFkSpotSourceId + ", " +
                    "fk_spot_des = " + mnFkSpotDestinyId + ", " +
                    "fk_tp_mot = " + mnFkModeOfTransportationTypeId + ", " +
                    "fk_tp_car = " + mnFkCarrierTypeId + ", " +
                    "fk_car = " + mnFkCarrierId + ", " +
                    "fk_tp_veh = " + mnFkVehicleTypeId + ", " +
                    "fk_veh_n = " + (mnFkVehicleId_n > 0 ? mnFkVehicleId_n : "NULL") + ", " +
                    "fk_st_ship_authorn = " + mnFkShipmentAuthorizationStatusId + ", " +
                    "fk_unit_cap_vol = " + mnFkUnitCapacityVolumeId + ", " +
                    "fk_unit_cap_mass = " + mnFkUnitCapacityMassId + ", " +
                    "fk_ord_year_n = " + (mnFkOrderYearId_n > 0 ? mnFkOrderYearId_n : "NULL") + ", " +
                    "fk_ord_doc_n = " + (mnFkOrderDocId_n > 0 ? mnFkOrderDocId_n : "NULL") + ", " +
                    //"fk_usr_bol = " + mnFkUserBillOfLadingId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_bol = " + "NOW()" + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
            }

            session.getStatement().execute(msSql);
            statement = session.getDatabase().getConnection().createStatement();

            // Delete destinies entries:

            msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " WHERE id_ship = " + mnPkShipmentId + " ";
            statement.executeUpdate(msSql);

            // Delete destinies:

            msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " WHERE id_ship = " + mnPkShipmentId + " ";
            statement.executeUpdate(msSql);

            // Delete notes:

            msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_NTS) + " WHERE id_ship = " + mnPkShipmentId + " ";
            statement.executeUpdate(msSql);

            // Save destinies:

            for (SDbShipmentDestiny destiny : maShipmentDestinies) {

                destiny.setPkShipmentId(mnPkShipmentId);
                destiny.setPkDestinyId(0);
                destiny.setRegistryNew(true);
                destiny.save(session);
            }

            // Save notes:

            for (SDbShipmentNotes notes : maShipmentNotes) {

                notes.setPkShipmentId(mnPkShipmentId);
                notes.setPkNotesId(0);
                notes.setRegistryNew(true);
                notes.save(session);
            }

            mbRegistryNew = false;
            mnQueryResultId = SDbConsts.SAVE_OK;
        }
    }

    @Override
    public boolean canDelete(SGuiSession session) throws SQLException, Exception {

        validateShipmentDestinyEntries(session);

        if (msQueryResult.isEmpty()) {
            validateOrderLinks(session);
        }

        if (mnFkShipmentAuthorizationStatusId == SModSysConsts.TRNS_ST_DPS_AUTHORN_AUTHORN)  {
            msQueryResult = "El embarque está autorizado, no puede ser eliminado.";
        }

        return msQueryResult.isEmpty() ? true : false;
    }

    @Override
    public boolean canSave(SGuiSession session) throws SQLException, Exception {

        if (miClient_xxx != null && miClient_xxx.getSessionXXX().getParamsCompany().getIsLogisticsOrderAutomatic() &&
                mnFkCarrierTypeId == SModSysConsts.LOGS_TP_CAR_CAR &&
                mnFkIncotermId != SModSysConsts.LOGS_INC_EXW) {

            if (validateOrderLinks(session).isEmpty()) {

                // If order exist delete for create a new order:

                if (moDpsOrder != null) {

                    moDpsOrder.setIsDeleted(true);
                    moDpsOrder.setDbmsDataCfd(null);
                    moDpsOrder.save(session.getDatabase().getConnection());
                    if (moDpsOrder.getLastDbActionResult() != SLibConstants.DB_ACTION_READ_ERROR) {
                        moDpsOrder = null;
                    }
                    else {
                        msQueryResult = "No se pudo generar la orden de compra.";
                    }
                }

                if (msQueryResult.isEmpty() && !getRegistryPurchaseOrder(session)) {

                    msQueryResult = "No se pudo generar la orden de compra.";
                }
            }
        }
        else {
            if (validateOrderLinks(session).isEmpty()) {
                if (moDpsOrder != null) {

                    moDpsOrder.setIsDeleted(true);
                }
            }
        }

        if (msQueryResult.isEmpty()) {
            validateCapacityMassUsed(session);
        }

        if (msQueryResult.isEmpty()) {
            validateShipmentDestinyEntries(session);
        }

        return msQueryResult.isEmpty() ? true : false;
    }

     @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        mbDeleted = !mbDeleted;
        mnFkUserUpdateId = session.getUser().getPkUserId();

        msSql = "UPDATE " + getSqlTable() + " SET " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                "ts_usr_upd = NOW() " +
                getSqlWhere();
        session.getStatement().execute(msSql);

        // Delete purchase order:

        if (mnFkOrderYearId_n > 0 && mnFkOrderDocId_n > 0) {

            msSql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " SET " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fid_usr_del = " + mnFkUserUpdateId + ", " +
                "ts_del = NOW() " +
                "WHERE id_year = " + mnFkOrderYearId_n + " AND id_doc = " + mnFkOrderDocId_n + " ";

            session.getStatement().execute(msSql);
        }

        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbShipment clone() throws CloneNotSupportedException {
        SDbShipment registry = new SDbShipment();

        registry.setPkShipmentId(this.getPkShipmentId());
        registry.setNumber(this.getNumber());
        registry.setDate(this.getDate());
        registry.setRate(this.getRate());
        registry.setExportDeclaration(this.getExportDeclaration());
        registry.setCarriageExportFreight(this.getCarriageExportFreight());
        registry.setCarriageExportDelay(this.getCarriageExportDelay());
        registry.setCarriageExportOther(this.getCarriageExportOther());
        registry.setUploadingExport(this.getUploadingExport());
        registry.setLoadingExport(this.getLoadingExport());
        registry.setCarriageImport(this.getCarriageImport());
        registry.setUploadingImport(this.getUploadingImport());
        registry.setInsurance(this.getInsurance());
        registry.setLoadingImport(this.getLoadingImport());
        registry.setCarriageImportFreight(this.getCarriageImportFreight());
        registry.setCarriageImportDelay(this.getCarriageImportDelay());
        registry.setCarriageImportOther(this.getCarriageImportOther());
        registry.setCustomsImport(this.getCustomsImport());
        registry.setTariff(this.getTariff());
        registry.setCost_r(this.getCost_r());
        registry.setExchangeRate(this.getExchangeRate());
        registry.setCostCy_r(this.getCostCy_r());
        registry.setCapacityVolume(this.getCapacityVolume());
        registry.setCapacityVolumeUsed(this.getCapacityVolumeUsed());
        registry.setCapacityMass(this.getCapacityMass());
        registry.setCapacityMassUsed(this.getCapacityMassUsed());
        registry.setDriver(this.getDriver());
        registry.setPlate(this.getPlate());
        registry.setConsolidated(this.isConsolidated());
        registry.setBillOfLadingClosed(this.isBillOfLadingClosed());
        registry.setDeleted(this.isDeleted());
        registry.setFkCompanyBranchId(this.getFkCompanyBranchId());
        registry.setFkShipmentTypeId(this.getFkShipmentTypeId());
        registry.setFkDeliveryTypeId(this.getFkDeliveryTypeId());
        registry.setFkCurrencyId(this.getFkCurrencyId());
        registry.setFkIncotermId(this.getFkIncotermId());
        registry.setFkSpotSourceId(this.getFkSpotSourceId());
        registry.setFkSpotDestinyId(this.getFkSpotDestinyId());
        registry.setFkModeOfTransportationTypeId(this.getFkModeOfTransportationTypeId());
        registry.setFkCarrierTypeId(this.getFkCarrierTypeId());
        registry.setFkCarrierId(this.getFkCarrierId());
        registry.setFkVehicleTypeId(this.getFkVehicleTypeId());
        registry.setFkVehicleId_n(this.getFkVehicleId_n());
        registry.setFkShipmentAuthorizationStatusId(this.getFkShipmentAuthorizationStatusId());
        registry.setFkUnitCapacityVolumeId(this.getFkUnitCapacityVolumeId());
        registry.setFkUnitCapacityMassId(this.getFkUnitCapacityMassId());
        registry.setFkOrderYearId_n(this.getFkOrderYearId_n());
        registry.setFkOrderDocId_n(this.getFkOrderDocId_n());
        registry.setFkUserBillOfLadingId(this.getFkUserBillOfLadingId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserBillOfLading(this.getTsUserBillOfLading());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setXtaLogisticsOrderAutomatic(this.getXtaLogisticsOrderAutomatic());
        registry.setXtaShipmentNumber(this.getXtaShipmentNumber());
        registry.setXtaDocNumberSeriesLogistics(this.getXtaDocNumberSeriesLogistics());
        registry.setXtaStatusDpsAuthorn(this.getXtaStatusDpsAuthorn());
        registry.setXtaOrderNumber(this.getXtaOrderNumber());
        registry.setShipmentDestinies(this.getShipmentDestinies());
        registry.setShipmentNotes(this.getShipmentNotes());
        registry.setClient_XXX(this.getClient_XXX());
        registry.setItem(this.getItem());
        registry.setBizPartner(this.getBizPartner());
        registry.setDpsOrder(this.getDpsOrder());
        registry.setDpsOrderEntry(this.getDpsOrderEntry());

        return registry;
    }

    @Override
    public Object readField(Statement statement, int[] pk, int field) throws SQLException, Exception {
        Object value = null;
        ResultSet resultSet = null;

        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT ";

        switch (field) {
            case FIELD_ST_DPS_AUTHORN:
                msSql += "fk_st_ship_authorn FROM " + getSqlTable() + " ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        resultSet = statement.executeQuery(msSql);

        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            switch (field) {
                case FIELD_ST_DPS_AUTHORN:
                    value = resultSet.getInt(1);
                    break;
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }

        mnQueryResultId = SDbConsts.READ_OK;

        return value;
    }

    @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        String sqlOrder = "";

        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_BILL_OF_LADING:
                msSql += "b_bol = " + (Boolean) value + " ";

                if (mnFkOrderYearId_n > 0 && mnFkOrderDocId_n > 0) {

                    sqlOrder = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " " +
                        "SET b_link = " + (Boolean) value + ", fid_usr_link = " + mnFkUserUpdateId + ", ts_link = NOW() " +
                        "WHERE id_year = " + mnFkOrderYearId_n + " AND id_doc = " + mnFkOrderDocId_n + ";";
                }
                break;

            case FIELD_ST_DPS_AUTHORN:
                msSql += "fk_st_ship_authorn = " + (Integer) value + " ";
                break;

            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);

        if (!sqlOrder.isEmpty()) {
            statement.execute(sqlOrder);
        }

        mnQueryResultId = SDbConsts.SAVE_OK;
    }
}
