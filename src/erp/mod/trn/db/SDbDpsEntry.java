/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following classes:
 * - erp.mtrn.data.SDataDpsEntry
 * All of them also make raw SQL insertions.
 */

/**
 * WARNING: Every change that affects the structure of this registry must be reflected in SIIE/ETL Avista classes and methods!
 * @author Juan Barajas, Sergio Flores
 */
public class SDbDpsEntry extends SDbRegistryUser {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected String msConceptKey;
    protected String msConcept;
    protected String msReference;
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
    protected String msSealQuality;
    protected String msSealSecurity;
    protected String msDriver;
    protected String msPlate;
    protected String msTicket;
    protected String msContainerTank;
    protected String msVgm;
    protected int mnOperationsType;
    protected int mnUserId;
    protected int mnSortingPosition;
    protected boolean mbIsPrepayment;
    protected boolean mbIsDiscountRetailChain;
    protected boolean mbIsTaxesAutomaticApplying;
    protected boolean mbIsPriceVariable;
    protected boolean mbIsPriceConfirm;
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
    protected String msFkCostCenterId_n;
    protected int mnFkItemRefId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected Date mtUserNewTs;
    protected Date mtUserEditTs;
    protected Date mtUserDeleteTs;
    
    protected ArrayList<SDbDpsEntryPrice> maChildEntryPrices;

    public SDbDpsEntry() {
        super(SModConsts.TRN_DPS_ETY);
    }

    /*
     * Public methods
     */

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setConceptKey(String s) { msConceptKey = s; }
    public void setConcept(String s) { msConcept = s; }
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
    public void setSealQuality(String s) { msSealQuality = s; }
    public void setSecuritySeal(String s) { msSealSecurity = s; }
    public void setDriver(String s) { msDriver = s; }
    public void setPlate(String s) { msPlate = s; }
    public void setTicket(String s) { msTicket = s; }
    public void setContainerTank(String s) { msContainerTank = s; }
    public void setVgm(String s) { msVgm = s; }
    public void setOperationsType(int n) { mnOperationsType = n; }
    public void setUserId(int n) { mnUserId = n; }
    public void setSortingPosition(int n) { mnSortingPosition = n; }
    public void setIsPrepayment(boolean b) { mbIsPrepayment = b; }
    public void setIsDiscountRetailChain(boolean b) { mbIsDiscountRetailChain = b; }
    public void setIsTaxesAutomaticApplying(boolean b) { mbIsTaxesAutomaticApplying = b; }
    public void setIsPriceVariable(boolean b) { mbIsPriceVariable = b; }
    public void setIsPriceConfirm(boolean b) { mbIsPriceConfirm = b; }
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
    public void setFkCostCenterId_n(String s) { msFkCostCenterId_n = s; }
    public void setFkItemRefId_n(int n) { mnFkItemRefId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(Date t) { mtUserNewTs = t; }
    public void setUserEditTs(Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public String getConceptKey() { return msConceptKey; }
    public String getConcept() { return msConcept; }
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
    public String getSealQuality() { return msSealQuality; }    
    public String getSecuritySeal() { return msSealSecurity; }
    public String getDriver() { return msDriver; }
    public String getPlate() { return msPlate; }
    public String getTicket() { return msTicket; }
    public String getContainerTank() { return msContainerTank; }
    public String getVgm() { return msVgm; }
    public int getOperationsType() { return mnOperationsType; }
    public int getUserId() { return mnUserId; }
    public int getSortingPosition() { return mnSortingPosition; }
    public boolean getIsPrepayment() { return mbIsPrepayment; }
    public boolean getIsDiscountRetailChain() { return mbIsDiscountRetailChain; }
    public boolean getIsTaxesAutomaticApplying() { return mbIsTaxesAutomaticApplying; }
    public boolean getIsPriceVariable() { return mbIsPriceVariable; }
    public boolean getIsPriceConfirm() { return mbIsPriceConfirm; }
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
    public String getFkCostCenterId_n() { return msFkCostCenterId_n; }
    public int getFkItemRefId_n() { return mnFkItemRefId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public Date getUserNewTs() { return mtUserNewTs; }
    public Date getUserEditTs() { return mtUserEditTs; }
    public Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    public ArrayList<SDbDpsEntryPrice> getChildEntryPrices() { return maChildEntryPrices; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkDocId = pk[1];
        mnPkEntryId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId };
    }

    @Override
    public void initRegistry() {
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
        mnOperationsType = 0;
        mnUserId = 0;
        mnSortingPosition = 0;
        mbIsPrepayment = false;
        mbIsDiscountRetailChain = false;
        mbIsTaxesAutomaticApplying = false;
        mbIsPriceVariable = false;
        mbIsPriceConfirm = false;
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
        
        maChildEntryPrices = new ArrayList<SDbDpsEntryPrice>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_year = " + mnPkYearId + " AND "
                + "id_doc = " + mnPkDocId + " AND "
                + "id_ety = " + mnPkEntryId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_year = " + pk[0] + " AND "
                + "id_doc = " + pk[1]  + " AND "
                + "id_ety = " + pk[2]  + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
        ResultSet resultSet = null;
        SDbDpsEntryPrice entryPrice = null;

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
            mnPkEntryId = resultSet.getInt("id_ety");
            msConceptKey = resultSet.getString("concept_key");
            msConcept = resultSet.getString("concept");
            msReference = resultSet.getString("ref");
            mdQuantity = resultSet.getDouble("qty");
            mbIsDiscountDocApplying = resultSet.getBoolean("b_disc_doc");
            mbIsDiscountUnitaryPercentage = resultSet.getBoolean("b_disc_u_per");
            mbIsDiscountUnitaryPercentageSystem = resultSet.getBoolean("b_disc_u_per_sys");
            mbIsDiscountEntryPercentage = resultSet.getBoolean("b_disc_ety_per");
            mdDiscountUnitaryPercentage = resultSet.getDouble("disc_u_per");
            mdDiscountUnitaryPercentageSystem = resultSet.getDouble("disc_u_per_sys");
            mdDiscountEntryPercentage = resultSet.getDouble("disc_ety_per");
            mdPriceUnitary = resultSet.getDouble("price_u");
            mdPriceUnitarySystem = resultSet.getDouble("price_u_sys");
            mdDiscountUnitary = resultSet.getDouble("disc_u");
            mdDiscountUnitarySystem = resultSet.getDouble("disc_u_sys");
            mdDiscountEntry = resultSet.getDouble("disc_ety");
            mdSubtotalProvisional_r = resultSet.getDouble("stot_prov_r");
            mdDiscountDoc = resultSet.getDouble("disc_doc");
            mdSubtotal_r = resultSet.getDouble("stot_r");
            mdTaxCharged_r = resultSet.getDouble("tax_charged_r");
            mdTaxRetained_r = resultSet.getDouble("tax_retained_r");
            mdTotal_r = resultSet.getDouble("tot_r");
            mdPriceUnitaryReal_r = resultSet.getDouble("price_u_real_r");
            mdCommissions_r = resultSet.getDouble("comms_r");
            mdPriceUnitaryCy = resultSet.getDouble("price_u_cur");
            mdPriceUnitarySystemCy = resultSet.getDouble("price_u_sys_cur");
            mdDiscountUnitaryCy = resultSet.getDouble("disc_u_cur");
            mdDiscountUnitarySystemCy = resultSet.getDouble("disc_u_sys_cur");
            mdDiscountEntryCy = resultSet.getDouble("disc_ety_cur");
            mdSubtotalProvisionalCy_r = resultSet.getDouble("stot_prov_cur_r");
            mdDiscountDocCy = resultSet.getDouble("disc_doc_cur");
            mdSubtotalCy_r = resultSet.getDouble("stot_cur_r");
            mdTaxChargedCy_r = resultSet.getDouble("tax_charged_cur_r");
            mdTaxRetainedCy_r = resultSet.getDouble("tax_retained_cur_r");
            mdTotalCy_r = resultSet.getDouble("tot_cur_r");
            mdPriceUnitaryRealCy_r = resultSet.getDouble("price_u_real_cur_r");
            mdCommissionsCy_r = resultSet.getDouble("comms_cur_r");
            mdOriginalQuantity = resultSet.getDouble("orig_qty");
            mdOriginalPriceUnitaryCy = resultSet.getDouble("orig_price_u_cur");
            mdOriginalPriceUnitarySystemCy = resultSet.getDouble("orig_price_u_sys_cur");
            mdOriginalDiscountUnitaryCy = resultSet.getDouble("orig_disc_u_cur");
            mdOriginalDiscountUnitarySystemCy = resultSet.getDouble("orig_disc_u_sys_cur");
            mdLength = resultSet.getDouble("len");
            mdSurface = resultSet.getDouble("surf");
            mdVolume = resultSet.getDouble("vol");
            mdMass = resultSet.getDouble("mass");
            mdWeightPackagingExtra = resultSet.getDouble("weight_pack_extra");
            mdWeightGross = resultSet.getDouble("weight_gross");
            mdWeightDelivery = resultSet.getDouble("weight_delivery");
            mdSurplusPercentage = resultSet.getDouble("surplus_per");
            mdContractBase = resultSet.getDouble("con_base");
            mdContractFuture = resultSet.getDouble("con_future");
            mdContractFactor = resultSet.getDouble("con_factor");
            mnContractPriceYear = resultSet.getInt("con_prc_year");
            mnContractPriceMonth = resultSet.getInt("con_prc_mon");
            msSealQuality = resultSet.getString("seal_qlt");
            msSealSecurity = resultSet.getString("seal_sec");
            msDriver = resultSet.getString("driver"); 
            msPlate = resultSet.getString("plate");
            msTicket = resultSet.getString("ticket");
            msContainerTank = resultSet.getString("cont_tank"); 
            msVgm = resultSet.getString("vgm");
            mnOperationsType = resultSet.getInt("ops_type");
            mnUserId = resultSet.getInt("usr_id");
            mnSortingPosition = resultSet.getInt("sort_pos");
            mbIsPrepayment = resultSet.getBoolean("b_pre_pay");
            mbIsDiscountRetailChain = resultSet.getBoolean("b_disc_retail_chain");
            mbIsTaxesAutomaticApplying = resultSet.getBoolean("b_tax_aut");
            mbIsPriceVariable = resultSet.getBoolean("b_prc_var");
            mbIsPriceConfirm = resultSet.getBoolean("b_prc_cnf");
            mbIsInventoriable = resultSet.getBoolean("b_inv");
            mbIsDeleted = resultSet.getBoolean("b_del");
            mnFkItemId = resultSet.getInt("fid_item");
            mnFkUnitId = resultSet.getInt("fid_unit");
            mnFkOriginalUnitId = resultSet.getInt("fid_orig_unit");
            mnFkTaxRegionId = resultSet.getInt("fid_tax_reg");
            mnFkThirdTaxCausingId_n = resultSet.getInt("fid_third_tax_n");
            mnFkDpsAdjustmentTypeId = resultSet.getInt("fid_tp_dps_adj");
            mnFkDpsAdjustmentSubtypeId = resultSet.getInt("fid_stp_dps_adj");
            mnFkDpsEntryTypeId = resultSet.getInt("fid_tp_dps_ety");
            mnFkVehicleTypeId_n = resultSet.getInt("fid_tp_veh_n");
            mnFkCashCompanyBranchId_n = resultSet.getInt("fid_cash_cob_n");
            mnFkCashAccountId_n = resultSet.getInt("fid_cash_acc_n");
            msFkCostCenterId_n = resultSet.getString("fid_cc_n");
            if (resultSet.wasNull()) {
                msFkCostCenterId_n = "";
            }   
            mnFkItemRefId_n = resultSet.getInt("fid_item_ref_n");
            mnFkUserNewId = resultSet.getInt("fid_usr_new");
            mnFkUserEditId = resultSet.getInt("fid_usr_edit");
            mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
            mtUserNewTs = resultSet.getTimestamp("ts_new");
            mtUserEditTs = resultSet.getTimestamp("ts_edit");
            mtUserDeleteTs = resultSet.getTimestamp("ts_del");

            // Read aswell entry prices:

            statement = session.getStatement().getConnection().createStatement();
            
            msSql = "SELECT id_prc FROM trn_dps_ety_prc "
                    + "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + " AND b_del = 0 "
                    + "ORDER BY con_prc_year, con_prc_mon ";
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                entryPrice = new SDbDpsEntryPrice();
                entryPrice.read(session, new int[] { mnPkYearId, mnPkDocId, mnPkEntryId, resultSet.getInt("id_prc") });
                maChildEntryPrices.add(entryPrice);
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
}
