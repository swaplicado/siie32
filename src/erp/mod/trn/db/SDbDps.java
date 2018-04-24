/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.trn.db;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following classes:
 * - erp.mtrn.data.SDataDps
 * All of them also make raw SQL insertions.
 */

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbDps extends SDbRegistryUser {
    
    public static final int FIELD_LINK = SDbRegistry.FIELD_BASE + 1;

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected Date mtDate;
    protected Date mtDateDoc;
    protected Date mtDateStartCredit;
    protected Date mtDateShipment_n;
    protected Date mtDateDelivery_n;
    protected Date mtDateDocLapsing_n;
    protected Date mtDateDocDelivery_n;
    protected String msNumberSeries;
    protected String msNumber;
    protected String msNumberReference;
    protected String msCommissionsReference;
    protected int mnApproveYear;
    protected int mnApproveNumber;
    protected int mnDaysOfCredit;
    protected boolean mbIsDiscountDocApplying;
    protected boolean mbIsDiscountDocPercentage;
    protected double mdDiscountDocPercentage;
    protected double mdSubtotalProvisional_r;
    protected double mdDiscountDoc_r;
    protected double mdSubtotal_r;
    protected double mdTaxCharged_r;
    protected double mdTaxRetained_r;
    protected double mdTotal_r;
    protected double mdCommissions_r;
    protected double mdExchangeRate;
    protected double mdExchangeRateSystem;
    protected double mdSubtotalProvisionalCy_r;
    protected double mdDiscountDocCy_r;
    protected double mdSubtotalCy_r;
    protected double mdTaxChargedCy_r;
    protected double mdTaxRetainedCy_r;
    protected double mdTotalCy_r;
    protected double mdCommissionsCy_r;
    protected String msDriver;
    protected String msPlate;
    protected String msTicket;
    protected int mnShipments;
    protected int mnPayments;
    protected String msPaymentMethod;
    protected String msPaymentAccount;
    protected int mnAutomaticAuthorizationRejection;
    protected boolean mbIsPublic;
    protected boolean mbIsLinked;
    protected boolean mbIsClosed;
    protected boolean mbIsClosedCommissions;
    protected boolean mbIsShipped;
    protected boolean mbIsDpsDeliveryAck;
    protected boolean mbIsRebill;
    protected boolean mbIsAudited;
    protected boolean mbIsAuthorized;
    protected boolean mbIsRecordAutomatic;
    protected boolean mbIsCopy;
    protected boolean mbIsCopied;
    protected boolean mbIsSystem;
    protected boolean mbIsDeleted;
    protected int mnFkDpsCategoryId;
    protected int mnFkDpsClassId;
    protected int mnFkDpsTypeId;
    protected int mnFkPaymentTypeId;
    protected int mnFkPaymentSystemTypeId;
    protected int mnFkDpsStatusId;
    protected int mnFkDpsValidityStatusId;
    protected int mnFkDpsAuthorizationStatusId;
    protected int mnFkDpsAnnulationTypeId;
    protected int mnFkDpsNatureId;
    protected int mnFkCompanyBranchId;
    protected int mnFkFunctionalAreaId;
    protected int mnFkBizPartnerId_r;
    protected int mnFkBizPartnerBranchId;
    protected int mnFkBizPartnerBranchAddressId;
    protected int mnFkBizPartnerAltId_r;
    protected int mnFkBizPartnerBranchAltId;
    protected int mnFkBizPartnerBranchAddressAltId;
    protected int mnFkAddresseeBizPartnerId_nr;
    protected int mnFkAddresseeBizPartnerBranchId_n;
    protected int mnFkAddresseeBizPartnerBranchAddressId_n;
    protected int mnFkContactBizPartnerBranchId_n;
    protected int mnFkContactContactId_n;
    protected int mnFkTaxIdentityEmisorTypeId;
    protected int mnFkTaxIdentityReceptorTypeId;
    protected int mnFkLanguajeId;
    protected int mnFkCurrencyId;
    protected int mnFkSalesAgentId_n;
    protected int mnFkSalesAgentBizPartnerId_n;
    protected int mnFkSalesSupervisorId_n;
    protected int mnFkSalesSupervisorBizPartnerId_n;
    protected int mnFkIncotermId;
    protected int mnFkSpotSourceId_n;
    protected int mnFkSpotDestinyId_n;
    protected int mnFkModeOfTransportationTypeId;
    protected int mnFkCarrierTypeId;
    protected int mnFkCarrierId_n;
    protected int mnFkVehicleTypeId_n;
    protected int mnFkVehicleId_n;
    protected int mnFkSourceYearId_n;
    protected int mnFkSourceDocId_n;
    protected int mnFkMfgYearId_n;
    protected int mnFkMfgOrderId_n;
    protected int mnFkUserLinkedId;
    protected int mnFkUserClosedId;
    protected int mnFkUserClosedCommissionsId;
    protected int mnFkUserShippedId;
    protected int mnFkUserUserDeliveryAckId;
    protected int mnFkUserAuditedId;
    protected int mnFkUserAuthorizedId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected Date mtUserLinkedTs;
    protected Date mtUserClosedTs;
    protected Date mtUserClosedCommissionsTs;
    protected Date mtUserShippedTs;
    protected Date mtUserDpsDeliveryAckTs;
    protected Date mtUserAuditedTs;
    protected Date mtUserAuthorizedTs;
    protected Date mtUserNewTs;
    protected Date mtUserEditTs;
    protected Date mtUserDeleteTs;

    protected ArrayList<SDbDpsEntry> maChildEntries;

    public SDbDps() {
        super(SModConsts.TRN_DPS);
    }

    /*
     * Public methods
     */

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setDateDoc(Date t) { mtDateDoc = t; }
    public void setDateStartCredit(Date t) { mtDateStartCredit = t; }
    public void setDateShipment_n(Date t) { mtDateShipment_n = t; }
    public void setDateDelivery_n(Date t) { mtDateDelivery_n = t; }
    public void setDateDocLapsing_n(Date t) { mtDateDocLapsing_n = t; }
    public void setDateDocDelivery_n(Date t) { mtDateDocDelivery_n = t; }
    public void setNumberSeries(String s) { msNumberSeries = s; }
    public void setNumber(String s) { msNumber = s; }
    public void setNumberReference(String s) { msNumberReference = s; }
    public void setCommissionsReference(String s) { msCommissionsReference = s; }
    public void setApproveYear(int n) { mnApproveYear = n; }
    public void setApproveNumber(int n) { mnApproveNumber = n; }
    public void setDaysOfCredit(int n) { mnDaysOfCredit = n; }
    public void setIsDiscountDocApplying(boolean b) { mbIsDiscountDocApplying = b; }
    public void setIsDiscountDocPercentage(boolean b) { mbIsDiscountDocPercentage = b; }
    public void setDiscountDocPercentage(double d) { mdDiscountDocPercentage = d; }
    public void setSubtotalProvisional_r(double d) { mdSubtotalProvisional_r = d; }
    public void setDiscountDoc_r(double d) { mdDiscountDoc_r = d; }
    public void setSubtotal_r(double d) { mdSubtotal_r = d; }
    public void setTaxCharged_r(double d) { mdTaxCharged_r = d; }
    public void setTaxRetained_r(double d) { mdTaxRetained_r = d; }
    public void setTotal_r(double d) { mdTotal_r = d; }
    public void setCommissions_r(double d) { mdCommissions_r = d; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setExchangeRateSystem(double d) { mdExchangeRateSystem = d; }
    public void setSubtotalProvisionalCy_r(double d) { mdSubtotalProvisionalCy_r = d; }
    public void setDiscountDocCy_r(double d) { mdDiscountDocCy_r = d; }
    public void setSubtotalCy_r(double d) { mdSubtotalCy_r = d; }
    public void setTaxChargedCy_r(double d) { mdTaxChargedCy_r = d; }
    public void setTaxRetainedCy_r(double d) { mdTaxRetainedCy_r = d; }
    public void setTotalCy_r(double d) { mdTotalCy_r = d; }
    public void setCommissionsCy_r(double d) { mdCommissionsCy_r = d; }
    public void setDriver(String s) { msDriver = s; }
    public void setPlate(String s) { msPlate = s; }
    public void setTicket(String s) { msTicket = s; }
    public void setShipments(int n) { mnShipments = n; }
    public void setPayments(int n) { mnPayments = n; }
    public void setPaymentMethod(String s) { msPaymentMethod = s; }
    public void setPaymentAccount(String s) { msPaymentAccount = s; }
    public void setAutomaticAuthorizationRejection(int n) { mnAutomaticAuthorizationRejection = n; }
    public void setIsPublic(boolean b) { mbIsPublic = b; }
    public void setIsLinked(boolean b) { mbIsLinked = b; }
    public void setIsClosed(boolean b) { mbIsClosed = b; }
    public void setIsClosedCommissions(boolean b) { mbIsClosedCommissions = b; }
    public void setIsShipped(boolean b) { mbIsShipped = b; }
    public void setIsDpsDeliveryAck(boolean b) { mbIsDpsDeliveryAck = b; }
    public void setIsRebill(boolean b) { mbIsRebill = b; }
    public void setIsAudited(boolean b) { mbIsAudited = b; }
    public void setIsAuthorized(boolean b) { mbIsAuthorized = b; }
    public void setIsRecordAutomatic(boolean b) { mbIsRecordAutomatic = b; }
    public void setIsCopy(boolean b) { mbIsCopy = b; }
    public void setIsCopied(boolean b) { mbIsCopied = b; }
    public void setIsSystem(boolean b) { mbIsSystem = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkDpsCategoryId(int n) { mnFkDpsCategoryId = n; }
    public void setFkDpsClassId(int n) { mnFkDpsClassId = n; }
    public void setFkDpsTypeId(int n) { mnFkDpsTypeId = n; }
    public void setFkPaymentTypeId(int n) { mnFkPaymentTypeId = n; }
    public void setFkPaymentSystemTypeId(int n) { mnFkPaymentSystemTypeId = n; }
    public void setFkDpsStatusId(int n) { mnFkDpsStatusId = n; }
    public void setFkDpsValidityStatusId(int n) { mnFkDpsValidityStatusId = n; }
    public void setFkDpsAuthorizationStatusId(int n) { mnFkDpsAuthorizationStatusId = n; }
    public void setFkDpsAnnulationTypeId(int n) { mnFkDpsAnnulationTypeId = n; }
    public void setFkDpsNatureId(int n) { mnFkDpsNatureId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkFunctionalAreaId(int n) { mnFkFunctionalAreaId = n; }
    public void setFkBizPartnerId_r(int n) { mnFkBizPartnerId_r = n; }
    public void setFkBizPartnerBranchId(int n) { mnFkBizPartnerBranchId = n; }
    public void setFkBizPartnerBranchAddressId(int n) { mnFkBizPartnerBranchAddressId = n; }
    public void setFkBizPartnerAltId_r(int n) { mnFkBizPartnerAltId_r = n; }
    public void setFkBizPartnerBranchAltId(int n) { mnFkBizPartnerBranchAltId = n; }
    public void setFkBizPartnerBranchAddressAltId(int n) { mnFkBizPartnerBranchAddressAltId = n; }
    public void setFkAddresseeBizPartnerId_nr(int n) { mnFkAddresseeBizPartnerId_nr = n; }
    public void setFkAddresseeBizPartnerBranchId_n(int n) { mnFkAddresseeBizPartnerBranchId_n = n; }
    public void setFkAddresseeBizPartnerBranchAddressId_n(int n) { mnFkAddresseeBizPartnerBranchAddressId_n = n; }
    public void setFkContactBizPartnerBranchId_n(int n) { mnFkContactBizPartnerBranchId_n = n; }
    public void setFkContactContactId_n(int n) { mnFkContactContactId_n = n; }
    public void setFkTaxIdentityEmisorTypeId(int n) { mnFkTaxIdentityEmisorTypeId = n; }
    public void setFkTaxIdentityReceptorTypeId(int n) { mnFkTaxIdentityReceptorTypeId = n; }
    public void setFkLanguajeId(int n) { mnFkLanguajeId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkSalesAgentId_n(int n) { mnFkSalesAgentId_n = n; }
    public void setFkSalesAgentBizPartnerId_n(int n) { mnFkSalesAgentBizPartnerId_n = n; }
    public void setFkSalesSupervisorId_n(int n) { mnFkSalesSupervisorId_n = n; }
    public void setFkSalesSupervisorBizPartnerId_n(int n) { mnFkSalesSupervisorBizPartnerId_n = n; }
    public void setFkIncotermId(int n) { mnFkIncotermId = n; }
    public void setFkSpotSourceId_n(int n) { mnFkSpotSourceId_n = n; }
    public void setFkSpotDestinyId_n(int n) { mnFkSpotDestinyId_n = n; }
    public void setFkModeOfTransportationTypeId(int n) { mnFkModeOfTransportationTypeId = n; }
    public void setFkCarrierTypeId(int n) { mnFkCarrierTypeId = n; }
    public void setFkCarrierId_n(int n) { mnFkCarrierId_n = n; }
    public void setFkVehicleTypeId_n(int n) { mnFkVehicleTypeId_n = n; }
    public void setFkVehicleId_n(int n) { mnFkVehicleId_n = n; }
    public void setFkSourceYearId_n(int n) { mnFkSourceYearId_n = n; }
    public void setFkSourceDocId_n(int n) { mnFkSourceDocId_n = n; }
    public void setFkMfgYearId_n(int n) { mnFkMfgYearId_n = n; }
    public void setFkMfgOrderId_n(int n) { mnFkMfgOrderId_n = n; }
    public void setFkUserLinkedId(int n) { mnFkUserLinkedId = n; }
    public void setFkUserClosedId(int n) { mnFkUserClosedId = n; }
    public void setFkUserClosedCommissionsId(int n) { mnFkUserClosedCommissionsId = n; }
    public void setFkUserShippedId(int n) { mnFkUserShippedId = n; }
    public void setFkUserUserDeliveryAckId(int n) { mnFkUserUserDeliveryAckId = n; }
    public void setFkUserAuditedId(int n) { mnFkUserAuditedId = n; }
    public void setFkUserAuthorizedId(int n) { mnFkUserAuthorizedId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserLinkedTs(Date t) { mtUserLinkedTs = t; }
    public void setUserClosedTs(Date t) { mtUserClosedTs = t; }
    public void setUserClosedCommissionsTs(Date t) { mtUserClosedCommissionsTs = t; }
    public void setUserShippedTs(Date t) { mtUserShippedTs = t; }
    public void setUserDpsDeliveryAckTs(Date t) { mtUserDpsDeliveryAckTs = t; }
    public void setUserAuditedTs(Date t) { mtUserAuditedTs = t; }
    public void setUserAuthorizedTs(Date t) { mtUserAuthorizedTs = t; }
    public void setUserNewTs(Date t) { mtUserNewTs = t; }
    public void setUserEditTs(Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public Date getDate() { return mtDate; }
    public Date getDateDoc() { return mtDateDoc; }
    public Date getDateStartCredit() { return mtDateStartCredit; }
    public Date getDateShipment_n() { return mtDateShipment_n; }
    public Date getDateDelivery_n() { return mtDateDelivery_n; }
    public Date getDateDocLapsing_n() { return mtDateDocLapsing_n; }
    public Date getDateDocDelivery_n() { return mtDateDocDelivery_n; }
    public String getNumberSeries() { return msNumberSeries; }
    public String getNumber() { return msNumber; }
    public String getNumberReference() { return msNumberReference; }
    public String getCommissionsReference() { return msCommissionsReference; }
    public int getApproveYear() { return mnApproveYear; }
    public int getApproveNumber() { return mnApproveNumber; }
    public int getDaysOfCredit() { return mnDaysOfCredit; }
    public boolean getIsDiscountDocApplying() { return mbIsDiscountDocApplying; }
    public boolean getIsDiscountDocPercentage() { return mbIsDiscountDocPercentage; }
    public double getDiscountDocPercentage() { return mdDiscountDocPercentage; }
    public double getSubtotalProvisional_r() { return mdSubtotalProvisional_r; }
    public double getDiscountDoc_r() { return mdDiscountDoc_r; }
    public double getSubtotal_r() { return mdSubtotal_r; }
    public double getTaxCharged_r() { return mdTaxCharged_r; }
    public double getTaxRetained_r() { return mdTaxRetained_r; }
    public double getTotal_r() { return mdTotal_r; }
    public double getCommissions_r() { return mdCommissions_r; }
    public double getExchangeRate() { return mdExchangeRate; }
    public double getExchangeRateSystem() { return mdExchangeRateSystem; }
    public double getSubtotalProvisionalCy_r() { return mdSubtotalProvisionalCy_r; }
    public double getDiscountDocCy_r() { return mdDiscountDocCy_r; }
    public double getSubtotalCy_r() { return mdSubtotalCy_r; }
    public double getTaxChargedCy_r() { return mdTaxChargedCy_r; }
    public double getTaxRetainedCy_r() { return mdTaxRetainedCy_r; }
    public double getTotalCy_r() { return mdTotalCy_r; }
    public double getCommissionsCy_r() { return mdCommissionsCy_r; }
    public String getDriver() { return msDriver; }
    public String getPlate() { return msPlate; }
    public String getTicket() { return msTicket; }
    public int getShipments() { return mnShipments; }
    public int getPayments() { return mnPayments; }
    public String getPaymentMethod() { return msPaymentMethod; }
    public String getPaymentAccount() { return msPaymentAccount; }
    public int getAutomaticAuthorizationRejection() { return mnAutomaticAuthorizationRejection; }
    public boolean getIsPublic() { return mbIsPublic; }
    public boolean getIsLinked() { return mbIsLinked; }
    public boolean getIsClosed() { return mbIsClosed; }
    public boolean getIsClosedCommissions() { return mbIsClosedCommissions; }
    public boolean getIsShipped() { return mbIsShipped; }
    public boolean getIsDpsDeliveryAck() { return mbIsDpsDeliveryAck; }
    public boolean getIsRebill() { return mbIsRebill; }
    public boolean getIsAudited() { return mbIsAudited; }
    public boolean getIsAuthorized() { return mbIsAuthorized; }
    public boolean getIsRecordAutomatic() { return mbIsRecordAutomatic; }
    public boolean getIsCopy() { return mbIsCopy; }
    public boolean getIsCopied() { return mbIsCopied; }
    public boolean getIsSystem() { return mbIsSystem; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkDpsCategoryId() { return mnFkDpsCategoryId; }
    public int getFkDpsClassId() { return mnFkDpsClassId; }
    public int getFkDpsTypeId() { return mnFkDpsTypeId; }
    public int getFkPaymentTypeId() { return mnFkPaymentTypeId; }
    public int getFkPaymentSystemTypeId() { return mnFkPaymentSystemTypeId; }
    public int getFkDpsStatusId() { return mnFkDpsStatusId; }
    public int getFkDpsValidityStatusId() { return mnFkDpsValidityStatusId; }
    public int getFkDpsAuthorizationStatusId() { return mnFkDpsAuthorizationStatusId; }
    public int getFkDpsAnnulationTypeId() { return mnFkDpsAnnulationTypeId; }
    public int getFkDpsNatureId() { return mnFkDpsNatureId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkFunctionalAreaId() { return mnFkFunctionalAreaId; }
    public int getFkBizPartnerId_r() { return mnFkBizPartnerId_r; }
    public int getFkBizPartnerBranchId() { return mnFkBizPartnerBranchId; }
    public int getFkBizPartnerBranchAddressId() { return mnFkBizPartnerBranchAddressId; }
    public int getFkBizPartnerAltId_r() { return mnFkBizPartnerAltId_r; }
    public int getFkBizPartnerBranchAltId() { return mnFkBizPartnerBranchAltId; }
    public int getFkBizPartnerBranchAddressAltId() { return mnFkBizPartnerBranchAddressAltId; }
    public int getFkAddresseeBizPartnerId_nr() { return mnFkAddresseeBizPartnerId_nr; }
    public int getFkAddresseeBizPartnerBranchId_n() { return mnFkAddresseeBizPartnerBranchId_n; }
    public int getFkAddresseeBizPartnerBranchAddressId_n() { return mnFkAddresseeBizPartnerBranchAddressId_n; }
    public int getFkContactBizPartnerBranchId_n() { return mnFkContactBizPartnerBranchId_n; }
    public int getFkContactContactId_n() { return mnFkContactContactId_n; }
    public int getFkTaxIdentityEmisorTypeId() { return mnFkTaxIdentityEmisorTypeId; }
    public int getFkTaxIdentityReceptorTypeId() { return mnFkTaxIdentityReceptorTypeId; }
    public int getFkLanguajeId() { return mnFkLanguajeId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkSalesAgentId_n() { return mnFkSalesAgentId_n; }
    public int getFkSalesAgentBizPartnerId_n() { return mnFkSalesAgentBizPartnerId_n; }
    public int getFkSalesSupervisorId_n() { return mnFkSalesSupervisorId_n; }
    public int getFkSalesSupervisorBizPartnerId_n() { return mnFkSalesSupervisorBizPartnerId_n; }
    public int getFkIncotermId() { return mnFkIncotermId; }
    public int getFkSpotSourceId_n() { return mnFkSpotSourceId_n; }
    public int getFkSpotDestinyId_n() { return mnFkSpotDestinyId_n; }
    public int getFkModeOfTransportationTypeId() { return mnFkModeOfTransportationTypeId; }
    public int getFkCarrierTypeId() { return mnFkCarrierTypeId; }
    public int getFkCarrierId_n() { return mnFkCarrierId_n; }
    public int getFkVehicleTypeId_n() { return mnFkVehicleTypeId_n; }
    public int getFkVehicleId_n() { return mnFkVehicleId_n; }
    public int getFkSourceYearId_n() { return mnFkSourceYearId_n; }
    public int getFkSourceDocId_n() { return mnFkSourceDocId_n; }
    public int getFkMfgYearId_n() { return mnFkMfgYearId_n; }
    public int getFkMfgOrderId_n() { return mnFkMfgOrderId_n; }
    public int getFkUserLinkedId() { return mnFkUserLinkedId; }
    public int getFkUserClosedId() { return mnFkUserClosedId; }
    public int getFkUserClosedCommissionsId() { return mnFkUserClosedCommissionsId; }
    public int getFkUserShippedId() { return mnFkUserShippedId; }
    public int getFkUserUserDeliveryAckId() { return mnFkUserUserDeliveryAckId; }
    public int getFkUserAuditedId() { return mnFkUserAuditedId; }
    public int getFkUserAuthorizedId() { return mnFkUserAuthorizedId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public Date getUserLinkedTs() { return mtUserLinkedTs; }
    public Date getUserClosedTs() { return mtUserClosedTs; }
    public Date getUserClosedCommissionsTs() { return mtUserClosedCommissionsTs; }
    public Date getUserShippedTs() { return mtUserShippedTs; }
    public Date getUserDpsDeliveryAckTs() { return mtUserDpsDeliveryAckTs; }
    public Date getUserAuditedTs() { return mtUserAuditedTs; }
    public Date getUserAuthorizedTs() { return mtUserAuthorizedTs; }
    public Date getUserNewTs() { return mtUserNewTs; }
    public Date getUserEditTs() { return mtUserEditTs; }
    public Date getUserDeleteTs() { return mtUserDeleteTs; }

    public ArrayList<SDbDpsEntry> getChildEntries() { return maChildEntries; }

    public int[] getKeyDpsCategory() { return new int[] { mnFkDpsCategoryId }; }
    public int[] getKeyDpsClass() { return new int[] { mnFkDpsCategoryId, mnFkDpsClassId }; }
    public int[] getKeyDpsType() { return new int[] { mnFkDpsCategoryId, mnFkDpsClassId, mnFkDpsTypeId }; }
    public int[] getKeyBizPartnerBranchAddress() { return new int[] { mnFkBizPartnerBranchId, mnFkBizPartnerBranchAddressId }; }
    public int[] getKeyBizPartnerBranchAddressAlt() { return new int[] { mnFkBizPartnerBranchAltId, mnFkBizPartnerBranchAddressAltId }; }
    public String getDpsNumber() { return STrnUtils.formatDocNumber(msNumberSeries, msNumber); }
    
    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkDocId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }

    @Override
    public void initRegistry() {
        mnPkYearId = 0;
        mnPkDocId = 0;
        mtDate = null;
        mtDateDoc = null;
        mtDateStartCredit = null;
        mtDateShipment_n = null;
        mtDateDelivery_n = null;
        mtDateDocLapsing_n = null;
        mtDateDocDelivery_n = null;
        msNumberSeries = "";
        msNumber = "";
        msNumberReference = "";
        msCommissionsReference = "";
        mnApproveYear = 0;
        mnApproveNumber = 0;
        mnDaysOfCredit = 0;
        mbIsDiscountDocApplying = false;
        mbIsDiscountDocPercentage = false;
        mdDiscountDocPercentage = 0;
        mdSubtotalProvisional_r = 0;
        mdDiscountDoc_r = 0;
        mdSubtotal_r = 0;
        mdTaxCharged_r = 0;
        mdTaxRetained_r = 0;
        mdTotal_r = 0;
        mdCommissions_r = 0;
        mdExchangeRate = 0;
        mdExchangeRateSystem = 0;
        mdSubtotalProvisionalCy_r = 0;
        mdDiscountDocCy_r = 0;
        mdSubtotalCy_r = 0;
        mdTaxChargedCy_r = 0;
        mdTaxRetainedCy_r = 0;
        mdTotalCy_r = 0;
        mdCommissionsCy_r = 0;
        msDriver = "";
        msPlate = "";
        msTicket = "";
        mnShipments = 0;
        mnPayments = 0;
        msPaymentMethod = "";
        msPaymentAccount = "";
        mnAutomaticAuthorizationRejection = 0;
        mbIsPublic = false;
        mbIsLinked = false;
        mbIsClosed = false;
        mbIsClosedCommissions = false;
        mbIsShipped = false;
        mbIsDpsDeliveryAck = false;
        mbIsRebill = false;
        mbIsAudited = false;
        mbIsAuthorized = false;
        mbIsRecordAutomatic = false;
        mbIsCopy = false;
        mbIsCopied = false;
        mbIsSystem = false;
        mbIsDeleted = false;
        mnFkDpsCategoryId = 0;
        mnFkDpsClassId = 0;
        mnFkDpsTypeId = 0;
        mnFkPaymentTypeId = 0;
        mnFkPaymentSystemTypeId = 0;
        mnFkDpsStatusId = 0;
        mnFkDpsValidityStatusId = 0;
        mnFkDpsAuthorizationStatusId = 0;
        mnFkDpsAnnulationTypeId = 0;
        mnFkDpsNatureId = 0;
        mnFkCompanyBranchId = 0;
        mnFkFunctionalAreaId = 0;
        mnFkBizPartnerId_r = 0;
        mnFkBizPartnerBranchId = 0;
        mnFkBizPartnerBranchAddressId = 0;
        mnFkBizPartnerAltId_r = 0;
        mnFkBizPartnerBranchAltId = 0;
        mnFkBizPartnerBranchAddressAltId = 0;
        mnFkAddresseeBizPartnerId_nr = 0;
        mnFkAddresseeBizPartnerBranchId_n = 0;
        mnFkAddresseeBizPartnerBranchAddressId_n = 0;
        mnFkContactBizPartnerBranchId_n = 0;
        mnFkContactContactId_n = 0;
        mnFkTaxIdentityEmisorTypeId = 0;
        mnFkTaxIdentityReceptorTypeId = 0;
        mnFkLanguajeId = 0;
        mnFkCurrencyId = 0;
        mnFkSalesAgentId_n = 0;
        mnFkSalesAgentBizPartnerId_n = 0;
        mnFkSalesSupervisorId_n = 0;
        mnFkSalesSupervisorBizPartnerId_n = 0;
        mnFkIncotermId = 0;
        mnFkSpotSourceId_n = 0;
        mnFkSpotDestinyId_n = 0;
        mnFkModeOfTransportationTypeId = 0;
        mnFkCarrierTypeId = 0;
        mnFkCarrierId_n = 0;
        mnFkVehicleTypeId_n = 0;
        mnFkVehicleId_n = 0;
        mnFkSourceYearId_n = 0;
        mnFkSourceDocId_n = 0;
        mnFkMfgYearId_n = 0;
        mnFkMfgOrderId_n = 0;
        mnFkUserLinkedId = 0;
        mnFkUserClosedId = 0;
        mnFkUserClosedCommissionsId = 0;
        mnFkUserShippedId = 0;
        mnFkUserUserDeliveryAckId = 0;
        mnFkUserAuditedId = 0;
        mnFkUserAuthorizedId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserLinkedTs = null;
        mtUserClosedTs = null;
        mtUserClosedCommissionsTs = null;
        mtUserShippedTs = null;
        mtUserDpsDeliveryAckTs = null;
        mtUserAuditedTs = null;
        mtUserAuthorizedTs = null;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        maChildEntries = new ArrayList<SDbDpsEntry>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_year = " + mnPkYearId + " AND "
                + "id_doc = " + mnPkDocId  + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_year = " + pk[0] + " AND "
                + "id_doc = " + pk[1]  + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
        ResultSet resultSet = null;
        SDbDpsEntry entry = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkYearId = resultSet.getInt("id_year");
            mnPkDocId = resultSet.getInt("id_doc");
            mtDate = resultSet.getDate("dt");
            mtDateDoc = resultSet.getDate("dt_doc");
            mtDateStartCredit = resultSet.getDate("dt_start_cred");
            mtDateShipment_n = resultSet.getDate("dt_shipment_n");
            mtDateDelivery_n = resultSet.getDate("dt_delivery_n");
            mtDateDocLapsing_n = resultSet.getDate("dt_doc_lapsing_n");
            mtDateDocDelivery_n = resultSet.getDate("dt_doc_delivery_n");
            msNumberSeries = resultSet.getString("num_ser");
            msNumber = resultSet.getString("num");
            msNumberReference = resultSet.getString("num_ref");
            msCommissionsReference = resultSet.getString("comms_ref");
            mnApproveYear = resultSet.getInt("approve_year");
            mnApproveNumber = resultSet.getInt("approve_num");
            mnDaysOfCredit = resultSet.getInt("days_cred");
            mbIsDiscountDocApplying = resultSet.getBoolean("b_disc_doc");
            mbIsDiscountDocPercentage = resultSet.getBoolean("b_disc_doc_per");
            mdDiscountDocPercentage = resultSet.getDouble("disc_doc_per");
            mdSubtotalProvisional_r = resultSet.getDouble("stot_prov_r");
            mdDiscountDoc_r = resultSet.getDouble("disc_doc_r");
            mdSubtotal_r = resultSet.getDouble("stot_r");
            mdTaxCharged_r = resultSet.getDouble("tax_charged_r");
            mdTaxRetained_r = resultSet.getDouble("tax_retained_r");
            mdTotal_r = resultSet.getDouble("tot_r");
            mdCommissions_r = resultSet.getDouble("comms_r");
            mdExchangeRate = resultSet.getDouble("exc_rate");
            mdExchangeRateSystem = resultSet.getDouble("exc_rate_sys");
            mdSubtotalProvisionalCy_r = resultSet.getDouble("stot_prov_cur_r");
            mdDiscountDocCy_r = resultSet.getDouble("disc_doc_cur_r");
            mdSubtotalCy_r = resultSet.getDouble("stot_cur_r");
            mdTaxChargedCy_r = resultSet.getDouble("tax_charged_cur_r");
            mdTaxRetainedCy_r = resultSet.getDouble("tax_retained_cur_r");
            mdTotalCy_r = resultSet.getDouble("tot_cur_r");
            mdCommissionsCy_r = resultSet.getDouble("comms_cur_r");
            msDriver = resultSet.getString("driver");
            msPlate = resultSet.getString("plate");
            msTicket = resultSet.getString("ticket");
            mnShipments = resultSet.getInt("shipments");
            mnPayments = resultSet.getInt("payments");
            msPaymentMethod = resultSet.getString("pay_method");
            msPaymentAccount = resultSet.getString("pay_account");
            mnAutomaticAuthorizationRejection = resultSet.getInt("aut_authorn_rej");
            mbIsPublic = resultSet.getBoolean("b_pub");
            mbIsLinked = resultSet.getBoolean("b_link");
            mbIsClosed = resultSet.getBoolean("b_close");
            mbIsClosedCommissions = resultSet.getBoolean("b_close_comms");
            mbIsShipped = resultSet.getBoolean("b_ship");
            mbIsDpsDeliveryAck = resultSet.getBoolean("b_dps_ack");
            mbIsRebill = resultSet.getBoolean("b_rebill");
            mbIsAudited = resultSet.getBoolean("b_audit");
            mbIsAuthorized = resultSet.getBoolean("b_authorn");
            mbIsRecordAutomatic = resultSet.getBoolean("b_rec_aut");
            mbIsCopy = resultSet.getBoolean("b_copy");
            mbIsCopied = resultSet.getBoolean("b_copied");
            mbIsSystem = resultSet.getBoolean("b_sys");
            mbIsDeleted = resultSet.getBoolean("b_del");
            mnFkDpsCategoryId = resultSet.getInt("fid_ct_dps");
            mnFkDpsClassId = resultSet.getInt("fid_cl_dps");
            mnFkDpsTypeId = resultSet.getInt("fid_tp_dps");
            mnFkPaymentTypeId = resultSet.getInt("fid_tp_pay");
            mnFkPaymentSystemTypeId = resultSet.getInt("fid_tp_pay_sys");
            mnFkDpsStatusId = resultSet.getInt("fid_st_dps");
            mnFkDpsValidityStatusId = resultSet.getInt("fid_st_dps_val");
            mnFkDpsAuthorizationStatusId = resultSet.getInt("fid_st_dps_authorn");
            mnFkDpsAnnulationTypeId = resultSet.getInt("fid_tp_dps_ann");
            mnFkDpsNatureId = resultSet.getInt("fid_dps_nat");
            mnFkCompanyBranchId = resultSet.getInt("fid_cob");
            mnFkFunctionalAreaId = resultSet.getInt("fid_func");
            mnFkBizPartnerId_r = resultSet.getInt("fid_bp_r");
            mnFkBizPartnerBranchId = resultSet.getInt("fid_bpb");
            mnFkBizPartnerBranchAddressId = resultSet.getInt("fid_add");
            mnFkBizPartnerAltId_r = resultSet.getInt("fid_bp_alt_r");
            mnFkBizPartnerBranchAltId = resultSet.getInt("fid_bpb_alt");
            mnFkBizPartnerBranchAddressAltId = resultSet.getInt("fid_add_alt");
            mnFkAddresseeBizPartnerId_nr = resultSet.getInt("fid_add_bp_nr");
            mnFkAddresseeBizPartnerBranchId_n = resultSet.getInt("fid_add_bpb_n");
            mnFkAddresseeBizPartnerBranchAddressId_n = resultSet.getInt("fid_add_add_n");
            mnFkContactBizPartnerBranchId_n = resultSet.getInt("fid_con_bpb_n");
            mnFkContactContactId_n = resultSet.getInt("fid_con_con_n");
            mnFkTaxIdentityEmisorTypeId = resultSet.getInt("fid_tp_tax_idy_emir");
            mnFkTaxIdentityReceptorTypeId = resultSet.getInt("fid_tp_tax_idy_recr");
            mnFkLanguajeId = resultSet.getInt("fid_lan");
            mnFkCurrencyId = resultSet.getInt("fid_cur");
            mnFkSalesAgentId_n = resultSet.getInt("fid_sal_agt_n");
            mnFkSalesAgentBizPartnerId_n = resultSet.getInt("fid_sal_agt_bp_n");
            mnFkSalesSupervisorId_n = resultSet.getInt("fid_sal_sup_n");
            mnFkSalesSupervisorBizPartnerId_n = resultSet.getInt("fid_sal_sup_bp_n");
            mnFkIncotermId = resultSet.getInt("fid_inc");
            mnFkSpotSourceId_n = resultSet.getInt("fid_spot_src_n");
            mnFkSpotDestinyId_n = resultSet.getInt("fid_spot_des_n");
            mnFkModeOfTransportationTypeId = resultSet.getInt("fid_tp_mot");
            mnFkCarrierTypeId = resultSet.getInt("fid_tp_car");
            mnFkCarrierId_n = resultSet.getInt("fid_car_n");
            mnFkVehicleTypeId_n = resultSet.getInt("fid_tp_veh_n");
            mnFkVehicleId_n = resultSet.getInt("fid_veh_n");
            mnFkSourceYearId_n = resultSet.getInt("fid_src_year_n");
            mnFkSourceDocId_n = resultSet.getInt("fid_src_doc_n");
            mnFkMfgYearId_n = resultSet.getInt("fid_mfg_year_n");
            mnFkMfgOrderId_n = resultSet.getInt("fid_mfg_ord_n");
            mnFkUserLinkedId = resultSet.getInt("fid_usr_link");
            mnFkUserClosedId = resultSet.getInt("fid_usr_close");
            mnFkUserClosedCommissionsId = resultSet.getInt("fid_usr_close_comms");
            mnFkUserShippedId = resultSet.getInt("fid_usr_ship");
            mnFkUserUserDeliveryAckId = resultSet.getInt("fid_usr_dps_ack");
            mnFkUserAuditedId = resultSet.getInt("fid_usr_audit");
            mnFkUserAuthorizedId = resultSet.getInt("fid_usr_authorn");
            mnFkUserNewId = resultSet.getInt("fid_usr_new");
            mnFkUserEditId = resultSet.getInt("fid_usr_edit");
            mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
            mtUserLinkedTs = resultSet.getTimestamp("ts_link");
            mtUserClosedTs = resultSet.getTimestamp("ts_close");
            mtUserClosedCommissionsTs = resultSet.getTimestamp("ts_close_comms");
            mtUserShippedTs = resultSet.getTimestamp("ts_ship");
            mtUserDpsDeliveryAckTs = resultSet.getTimestamp("ts_dps_ack");
            mtUserAuditedTs = resultSet.getTimestamp("ts_audit");
            mtUserAuthorizedTs = resultSet.getTimestamp("ts_authorn");
            mtUserNewTs = resultSet.getTimestamp("ts_new");
            mtUserEditTs = resultSet.getTimestamp("ts_edit");
            mtUserDeleteTs = resultSet.getTimestamp("ts_del");
                
            // Read aswell document entries:

            statement = session.getStatement().getConnection().createStatement();
            
            msSql = "SELECT id_year, id_doc, id_ety " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " " +
                    "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " " +
                    "ORDER BY fid_tp_dps_ety = " + SDataConstantsSys.TRNS_TP_DPS_ETY_VIRT + ", sort_pos, id_ety ";
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                entry = new SDbDpsEntry();
                entry.read(session, new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_doc"), resultSet.getInt("id_ety") });
                maChildEntries.add(entry);
            }

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object readField(final Statement statement, final int[] pk, final int field) throws SQLException, Exception {
        Object value = null;
        ResultSet resultSet = null;

        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT ";

        switch (field) {
            case FIELD_LINK:
                msSql += "b_link ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlFromWhere(pk);

        resultSet = statement.executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            switch (field) {
            case FIELD_LINK:
                    value = resultSet.getBoolean(1);
                    break;
                default:
            }
        }

        mnQueryResultId = SDbConsts.READ_OK;
        return value;
    }
    
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        boolean linked = (Boolean) readField(statement, pk, FIELD_LINK);
        Link link = (Link) value;
        
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        msSql = "UPDATE " + getSqlTable() + " SET ";
        
        switch (field) {
            case FIELD_LINK:
                if (link.Link && !linked || !link.Link && linked) {
                    msSql += "b_link = " + link.Link + ", fid_usr_link = " + link.UserId + ", ts_link = NOW() ";
                }
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        msSql += getSqlWhere(pk);
        
        statement.execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }
    
    public class Link {
        boolean Link;
        int UserId;
        
        public Link(boolean link, int userId) {
            Link = link;
            UserId = userId;
        }
        
        public Link() {
            
        }
    }
}
