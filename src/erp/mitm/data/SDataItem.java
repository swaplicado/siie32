/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModSysConsts;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataItem extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkItemId;
    protected java.lang.String msKey;
    protected java.lang.String msItem;
    protected java.lang.String msItemShort;
    protected java.lang.String msName;
    protected java.lang.String msNameShort;
    protected java.lang.String msPresentation;
    protected java.lang.String msPresentationShort;
    protected java.lang.String msCode;
    protected boolean mbIsInventoriable;
    protected boolean mbIsLotApplying;
    protected boolean mbIsBulk;
    protected double mdUnitsContained;
    protected double mdUnitsVirtual;
    protected double mdUnitsPackage;
    protected double mdNetContent;
    protected double mdNetContentUnitary;
    protected boolean mbIsNetContentVariable;
    protected double mdLength;
    protected double mdLengthUnitary;
    protected boolean mbIsLengthVariable;
    protected double mdSurface;
    protected double mdSurfaceUnitary;
    protected boolean mbIsSurfaceVariable;
    protected double mdVolume;
    protected double mdVolumeUnitary;
    protected boolean mbIsVolumeVariable;
    protected double mdMass;
    protected double mdMassUnitary;
    protected boolean mbIsMassVariable;
    protected double mdProductionTime;
    protected double mdWeightGross;
    protected double mdWeightDelivery;
    protected double mdUnitAlternativeBaseEquivalence;
    protected double mdSurplusPercentage;
    protected boolean mbIsReference;
    protected boolean mbIsPrepayment;
    protected boolean mbIsFreePrice;
    protected boolean mbIsFreeDiscount;
    protected boolean mbIsFreeDiscountUnitary;
    protected boolean mbIsFreeDiscountEntry;
    protected boolean mbIsFreeDiscountDoc;
    protected boolean mbIsFreeCommissions;
    protected boolean mbIsDeleted;
    protected int mnFkItemGenericId;
    protected int mnFkItemLineId_n;
    protected int mnFkItemStatusId;
    protected int mnFkUnitId;
    protected int mnFkUnitUnitsContainedId;
    protected int mnFkUnitUnitsVirtualId;
    protected int mnFkUnitNetContentId;
    protected int mnFkUnitNetContentUnitaryId;
    protected int mnFkUnitAlternativeTypeId;
    protected int mnFkLevelTypeId;
    protected int mnFkBrandId;
    protected int mnFkManufacturerId;
    protected int mnFkElementId;
    protected int mnFkVariety01Id;
    protected int mnFkVariety02Id;
    protected int mnFkVariety03Id;
    protected int mnFkAdministrativeConceptTypeId;
    protected int mnFkTaxableConceptTypeId;
    protected int mnFkAccountEbitdaTypeId;
    protected int mnFkFiscalAccountIncId;
    protected int mnFkFiscalAccountExpId;
    protected int mnFkItemPackageId_n;
    protected int mnFkDefaultItemRefId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected erp.mitm.data.SDataUnit moDbmsDataUnit;
    protected erp.mitm.data.SDataItemGeneric moDbmsDataItemGeneric;
    protected java.util.Vector<erp.mitm.data.SDataItemBarcode> mvDbmsItemBarcodes;
    protected java.util.Vector<erp.mitm.data.SDataItemForeignLanguage> mvDbmsItemForeignLanguageDescriptions;

    public SDataItem() {
        super(SDataConstants.ITMU_ITEM);
        mvDbmsItemBarcodes = new Vector<SDataItemBarcode>();
        mvDbmsItemForeignLanguageDescriptions = new Vector<SDataItemForeignLanguage>();
        reset();
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setKey(java.lang.String s) { msKey = s; }
    public void setItem(java.lang.String s) { msItem = s; }
    public void setItemShort(java.lang.String s) { msItemShort = s; }
    public void setName(java.lang.String s) { msName = s; }
    public void setNameShort(java.lang.String s) { msNameShort = s; }
    public void setPresentation(java.lang.String s) { msPresentation = s; }
    public void setPresentationShort(java.lang.String s) { msPresentationShort = s; }
    public void setCode(java.lang.String s) { msCode = s; }
    public void setIsInventoriable(boolean b) { mbIsInventoriable = b; }
    public void setIsLotApplying(boolean b) { mbIsLotApplying = b; }
    public void setIsBulk(boolean b) { mbIsBulk = b; }
    public void setUnitsContained(double d) { mdUnitsContained = d; }
    public void setUnitsVirtual(double d) { mdUnitsVirtual = d; }
    public void setUnitsPackage(double d) { mdUnitsPackage = d; }
    public void setNetContent(double d) { mdNetContent = d; }
    public void setNetContentUnitary(double d) { mdNetContentUnitary = d; }
    public void setIsNetContentVariable(boolean b) { mbIsNetContentVariable = b; }
    public void setLength(double d) { mdLength = d; }
    public void setLengthUnitary(double d) { mdLengthUnitary = d; }
    public void setIsLengthVariable(boolean b) { mbIsLengthVariable = b; }
    public void setSurface(double d) { mdSurface = d; }
    public void setSurfaceUnitary(double d) { mdSurfaceUnitary = d; }
    public void setIsSurfaceVariable(boolean b) { mbIsSurfaceVariable = b; }
    public void setVolume(double d) { mdVolume = d; }
    public void setVolumeUnitary(double d) { mdVolumeUnitary = d; }
    public void setIsVolumeVariable(boolean b) { mbIsVolumeVariable = b; }
    public void setMass(double d) { mdMass = d; }
    public void setMassUnitary(double d) { mdMassUnitary = d; }
    public void setIsMassVariable(boolean b) { mbIsMassVariable = b; }
    public void setProductionTime(double d) { mdProductionTime = d; }
    public void setWeightGross(double d) { mdWeightGross = d; }
    public void setWeightDelivery(double d) { mdWeightDelivery = d; }
    public void setUnitAlternativeBaseEquivalence(double d) { mdUnitAlternativeBaseEquivalence = d; }
    public void setSurplusPercentage(double d) { mdSurplusPercentage = d; }
    public void setIsReference(boolean b) { mbIsReference = b; }
    public void setIsPrepayment(boolean b) { mbIsPrepayment = b; }
    public void setIsFreePrice(boolean b) { mbIsFreePrice = b; }
    public void setIsFreeDiscount(boolean b) { mbIsFreeDiscount = b; }
    public void setIsFreeDiscountUnitary(boolean b) { mbIsFreeDiscountUnitary = b; }
    public void setIsFreeDiscountEntry(boolean b) { mbIsFreeDiscountEntry = b; }
    public void setIsFreeDiscountDoc(boolean b) { mbIsFreeDiscountDoc = b; }
    public void setIsFreeCommissions(boolean b) { mbIsFreeCommissions = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkItemGenericId(int n) { mnFkItemGenericId = n; }
    public void setFkItemLineId_n(int n) { mnFkItemLineId_n = n; }
    public void setFkItemStatusId(int n) { mnFkItemStatusId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkUnitUnitsContainedId(int n) { mnFkUnitUnitsContainedId = n; }
    public void setFkUnitUnitsVirtualId(int n) { mnFkUnitUnitsVirtualId = n; }
    public void setFkUnitNetContentId(int n) { mnFkUnitNetContentId = n; }
    public void setFkUnitNetContentUnitaryId(int n) { mnFkUnitNetContentUnitaryId = n; }
    public void setFkUnitAlternativeTypeId(int n) { mnFkUnitAlternativeTypeId = n; }
    public void setFkLevelTypeId(int n) { mnFkLevelTypeId = n; }
    public void setFkBrandId(int n) { mnFkBrandId = n; }
    public void setFkManufacturerId(int n) { mnFkManufacturerId = n; }
    public void setFkElementId(int n) { mnFkElementId = n; }
    public void setFkVariety01Id(int n) { mnFkVariety01Id = n; }
    public void setFkVariety02Id(int n) { mnFkVariety02Id = n; }
    public void setFkVariety03Id(int n) { mnFkVariety03Id = n; }
    public void setFkAdministrativeConceptTypeId(int n) { mnFkAdministrativeConceptTypeId = n; }
    public void setFkTaxableConceptTypeId(int n) { mnFkTaxableConceptTypeId = n; }
    public void setFkAccountEbitdaTypeId(int n) { mnFkAccountEbitdaTypeId = n; }
    public void setFkFiscalAccountIncId(int n) { mnFkFiscalAccountIncId = n; }
    public void setFkFiscalAccountExpId(int n) { mnFkFiscalAccountExpId = n; }
    public void setFkItemPackageId_n(int n) { mnFkItemPackageId_n = n; }
    public void setFkDefaultItemRefId_n(int n) { mnFkDefaultItemRefId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkItemId() { return mnPkItemId; }
    public java.lang.String getKey() { return msKey; }
    public java.lang.String getItem() { return msItem; }
    public java.lang.String getItemShort() { return msItemShort; }
    public java.lang.String getName() { return msName; }
    public java.lang.String getNameShort() { return msNameShort; }
    public java.lang.String getPresentation() { return msPresentation; }
    public java.lang.String getPresentationShort() { return msPresentationShort; }
    public java.lang.String getCode() { return msCode; }
    public boolean getIsInventoriable() { return mbIsInventoriable; }
    public boolean getIsLotApplying() { return mbIsLotApplying; }
    public boolean getIsBulk() { return mbIsBulk; }
    public double getUnitsContained() { return mdUnitsContained; }
    public double getUnitsVirtual() { return mdUnitsVirtual; }
    public double getUnitsPackage() { return mdUnitsPackage; }
    public double getNetContent() { return mdNetContent; }
    public double getNetContentUnitary() { return mdNetContentUnitary; }
    public boolean getIsNetContentVariable() { return mbIsNetContentVariable; }
    public double getLength() { return mdLength; }
    public double getLengthUnitary() { return mdLengthUnitary; }
    public boolean getIsLengthVariable() { return mbIsLengthVariable; }
    public double getSurface() { return mdSurface; }
    public double getSurfaceUnitary() { return mdSurfaceUnitary; }
    public boolean getIsSurfaceVariable() { return mbIsSurfaceVariable; }
    public double getVolume() { return mdVolume; }
    public double getVolumeUnitary() { return mdVolumeUnitary; }
    public boolean getIsVolumeVariable() { return mbIsVolumeVariable; }
    public double getMass() { return mdMass; }
    public double getMassUnitary() { return mdMassUnitary; }
    public boolean getIsMassVariable() { return mbIsMassVariable; }
    public double getProductionTime() { return mdProductionTime; }
    public double getWeightGross() { return mdWeightGross; }
    public double getWeightDelivery() { return mdWeightDelivery; }
    public double getUnitAlternativeBaseEquivalence() { return mdUnitAlternativeBaseEquivalence; }
    public double getSurplusPercentage() { return mdSurplusPercentage; }
    public boolean getIsReference() { return mbIsReference; }
    public boolean getIsPrepayment() { return mbIsPrepayment; }
    public boolean getIsFreePrice() { return mbIsFreePrice; }
    public boolean getIsFreeDiscount() { return mbIsFreeDiscount; }
    public boolean getIsFreeDiscountUnitary() { return mbIsFreeDiscountUnitary; }
    public boolean getIsFreeDiscountEntry() { return mbIsFreeDiscountEntry; }
    public boolean getIsFreeDiscountDoc() { return mbIsFreeDiscountDoc; }
    public boolean getIsFreeCommissions() { return mbIsFreeCommissions; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkItemGenericId() { return mnFkItemGenericId; }
    public int getFkItemLineId_n() { return mnFkItemLineId_n; }
    public int getFkItemStatusId() { return mnFkItemStatusId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkUnitUnitsContainedId() { return mnFkUnitUnitsContainedId; }
    public int getFkUnitUnitsVirtualId() { return mnFkUnitUnitsVirtualId; }
    public int getFkUnitNetContentId() { return mnFkUnitNetContentId; }
    public int getFkUnitNetContentUnitaryId() { return mnFkUnitNetContentUnitaryId; }
    public int getFkUnitAlternativeTypeId() { return mnFkUnitAlternativeTypeId; }
    public int getFkLevelTypeId() { return mnFkLevelTypeId; }
    public int getFkBrandId() { return mnFkBrandId; }
    public int getFkManufacturerId() { return mnFkManufacturerId; }
    public int getFkElementId() { return mnFkElementId; }
    public int getFkVariety01Id() { return mnFkVariety01Id; }
    public int getFkVariety02Id() { return mnFkVariety02Id; }
    public int getFkVariety03Id() { return mnFkVariety03Id; }
    public int getFkAdministrativeConceptTypeId() { return mnFkAdministrativeConceptTypeId; }
    public int getFkTaxableConceptTypeId() { return mnFkTaxableConceptTypeId; }
    public int getFkAccountEbitdaTypeId() { return mnFkAccountEbitdaTypeId; }
    public int getFkFiscalAccountIncId() { return mnFkFiscalAccountIncId; }
    public int getFkFiscalAccountExpId() { return mnFkFiscalAccountExpId; }
    public int getFkItemPackageId_n() { return mnFkItemPackageId_n; }
    public int getFkDefaultItemRefId_n() { return mnFkDefaultItemRefId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public erp.mitm.data.SDataUnit getDbmsDataUnit() { return moDbmsDataUnit; }
    public erp.mitm.data.SDataItemGeneric getDbmsDataItemGeneric() { return moDbmsDataItemGeneric; }
    public java.util.Vector<SDataItemBarcode> getDbmsItemBarcodes() { return mvDbmsItemBarcodes; }
    public java.util.Vector<SDataItemForeignLanguage> getDbmsItemForeignLanguageDescriptions() { return mvDbmsItemForeignLanguageDescriptions; }

    public boolean getDbmsIsFreePrice() { return mbIsFreePrice || (moDbmsDataItemGeneric == null ? false : moDbmsDataItemGeneric.getDbmsIsFreePrice()); }
    public boolean getDbmsIsFreeDiscount() { return mbIsFreeDiscount || (moDbmsDataItemGeneric == null ? false : moDbmsDataItemGeneric.getDbmsIsFreeDiscount()); }
    public boolean getDbmsIsFreeDiscountUnitary() { return mbIsFreeDiscountUnitary || (moDbmsDataItemGeneric == null ? false : moDbmsDataItemGeneric.getDbmsIsFreeDiscountUnitary()); }
    public boolean getDbmsIsFreeDiscountEntry() { return mbIsFreeDiscountEntry || (moDbmsDataItemGeneric == null ? false : moDbmsDataItemGeneric.getDbmsIsFreeDiscountEntry()); }
    public boolean getDbmsIsFreeDiscountDoc() { return mbIsFreeDiscountDoc || (moDbmsDataItemGeneric == null ? false : moDbmsDataItemGeneric.getDbmsIsFreeDiscountDoc()); }
    public boolean getDbmsIsFreeCommissions() { return mbIsFreeCommissions || (moDbmsDataItemGeneric == null ? false : moDbmsDataItemGeneric.getDbmsIsFreeCommissions()); }
    public boolean getDbmsIsInventoriable() { return mbIsInventoriable || (moDbmsDataItemGeneric == null ? false : moDbmsDataItemGeneric.getIsInventoriable()); }
    public boolean getDbmsIsLotApplying() { return mbIsLotApplying || (moDbmsDataItemGeneric == null ? false : moDbmsDataItemGeneric.getIsLotApplying()); }
    public boolean getDbmsIsBulk() { return mbIsBulk || (moDbmsDataItemGeneric == null ? false : moDbmsDataItemGeneric.getIsBulk()); }

    public int getDbmsFkDefaultItemRefId_n() {
        int id = mnFkDefaultItemRefId_n;

        if (mnFkDefaultItemRefId_n == SLibConstants.UNDEFINED && moDbmsDataItemGeneric != null) {
            mnFkDefaultItemRefId_n = moDbmsDataItemGeneric.getFkDefaultItemRefId_n();
        }

        return id;
    }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkItemId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemId = 0;
        msKey = "";
        msItem = "";
        msItemShort = "";
        msName = "";
        msNameShort = "";
        msPresentation = "";
        msPresentationShort = "";
        msCode = "";
        mbIsInventoriable = false;
        mbIsLotApplying = false;
        mbIsBulk = false;
        mdUnitsContained = 0;
        mdUnitsVirtual = 0;
        mdUnitsPackage = 0;
        mdNetContent = 0;
        mdNetContentUnitary = 0;
        mbIsNetContentVariable = false;
        mdLength = 0;
        mdLengthUnitary = 0;
        mbIsLengthVariable = false;
        mdSurface = 0;
        mdSurfaceUnitary = 0;
        mbIsSurfaceVariable = false;
        mdVolume = 0;
        mdVolumeUnitary = 0;
        mbIsVolumeVariable = false;
        mdMass = 0;
        mdMassUnitary = 0;
        mbIsMassVariable = false;
        mdProductionTime = 0;
        mdWeightGross = 0;
        mdWeightDelivery = 0;
        mdUnitAlternativeBaseEquivalence = 0;
        mdSurplusPercentage = 0;
        mbIsReference = false;
        mbIsPrepayment = false;
        mbIsFreePrice = false;
        mbIsFreeDiscount = false;
        mbIsFreeDiscountUnitary = false;
        mbIsFreeDiscountEntry = false;
        mbIsFreeDiscountDoc = false;
        mbIsFreeCommissions = false;
        mbIsDeleted = false;
        mnFkItemGenericId = 0;
        mnFkItemLineId_n = 0;
        mnFkItemStatusId = 0;
        mnFkUnitId = 0;
        mnFkUnitUnitsContainedId = 0;
        mnFkUnitUnitsVirtualId = 0;
        mnFkUnitNetContentId = 0;
        mnFkUnitNetContentUnitaryId = 0;
        mnFkUnitAlternativeTypeId = 0;
        mnFkLevelTypeId = 0;
        mnFkBrandId = 0;
        mnFkManufacturerId = 0;
        mnFkElementId = 0;
        mnFkVariety01Id = 0;
        mnFkVariety02Id = 0;
        mnFkVariety03Id = 0;
        mnFkAdministrativeConceptTypeId = 0;
        mnFkTaxableConceptTypeId = 0;
        mnFkAccountEbitdaTypeId = 0;
        mnFkFiscalAccountIncId = 0;
        mnFkFiscalAccountExpId = 0;
        mnFkItemPackageId_n = 0;
        mnFkDefaultItemRefId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        moDbmsDataUnit = null;
        moDbmsDataItemGeneric = null;
        mvDbmsItemBarcodes.clear();
        mvDbmsItemForeignLanguageDescriptions.clear();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;
        Statement statementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.itmu_item WHERE id_item = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkItemId = resultSet.getInt("id_item");
                msKey = resultSet.getString("item_key");
                msItem = resultSet.getString("item");
                msItemShort = resultSet.getString("item_short");
                msName = resultSet.getString("name");
                msNameShort = resultSet.getString("name_short");
                msPresentation = resultSet.getString("present");
                msPresentationShort = resultSet.getString("present_short");
                msCode = resultSet.getString("code");
                mbIsInventoriable = resultSet.getBoolean("b_inv");
                mbIsLotApplying = resultSet.getBoolean("b_lot");
                mbIsBulk = resultSet.getBoolean("b_bulk");
                mdUnitsContained = resultSet.getDouble("units_cont");
                mdUnitsVirtual = resultSet.getDouble("units_virt");
                mdUnitsPackage = resultSet.getDouble("units_pack");
                mdNetContent = resultSet.getDouble("net_cont");
                mdNetContentUnitary = resultSet.getDouble("net_cont_u");
                mbIsNetContentVariable = resultSet.getBoolean("b_net_cont_variable");
                mdLength = resultSet.getDouble("len");
                mdLengthUnitary = resultSet.getDouble("len_cu");
                mbIsLengthVariable = resultSet.getBoolean("b_len_variable");
                mdSurface = resultSet.getDouble("surf");
                mdSurfaceUnitary = resultSet.getDouble("surf_cu");
                mbIsSurfaceVariable = resultSet.getBoolean("b_surf_variable");
                mdVolume = resultSet.getDouble("vol");
                mdVolumeUnitary = resultSet.getDouble("vol_cu");
                mbIsVolumeVariable = resultSet.getBoolean("b_vol_variable");
                mdMass = resultSet.getDouble("mass");
                mdMassUnitary = resultSet.getDouble("mass_cu");
                mbIsMassVariable = resultSet.getBoolean("b_mass_variable");
                mdProductionTime = resultSet.getDouble("prod_time");
                mdWeightGross = resultSet.getDouble("weight_gross");
                mdWeightDelivery = resultSet.getDouble("weight_delivery");
                mdUnitAlternativeBaseEquivalence = resultSet.getDouble("unit_alt_base_equiv");
                mdSurplusPercentage = resultSet.getDouble("surplus_per");
                mbIsReference = resultSet.getBoolean("b_ref");
                mbIsPrepayment = resultSet.getBoolean("b_pre_pay");
                mbIsFreePrice = resultSet.getBoolean("b_free_price");
                mbIsFreeDiscount = resultSet.getBoolean("b_free_disc");
                mbIsFreeDiscountUnitary = resultSet.getBoolean("b_free_disc_u");
                mbIsFreeDiscountEntry = resultSet.getBoolean("b_free_disc_ety");
                mbIsFreeDiscountDoc = resultSet.getBoolean("b_free_disc_doc");
                mbIsFreeCommissions = resultSet.getBoolean("b_free_comms");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkItemGenericId = resultSet.getInt("fid_igen");
                mnFkItemLineId_n = resultSet.getInt("fid_line_n");
                mnFkItemStatusId = resultSet.getInt("fid_st_item");
                mnFkUnitId = resultSet.getInt("fid_unit");
                mnFkUnitUnitsContainedId = resultSet.getInt("fid_unit_units_cont");
                mnFkUnitUnitsVirtualId = resultSet.getInt("fid_unit_units_virt");
                mnFkUnitNetContentId = resultSet.getInt("fid_unit_net_cont");
                mnFkUnitNetContentUnitaryId = resultSet.getInt("fid_unit_net_cont_u");
                mnFkUnitAlternativeTypeId = resultSet.getInt("fid_tp_unit_alt");
                mnFkLevelTypeId = resultSet.getInt("fid_tp_lev");
                mnFkBrandId = resultSet.getInt("fid_brd");
                mnFkManufacturerId = resultSet.getInt("fid_mfr");
                mnFkElementId = resultSet.getInt("fid_emt");
                mnFkVariety01Id = resultSet.getInt("fid_var_01");
                mnFkVariety02Id = resultSet.getInt("fid_var_02");
                mnFkVariety03Id = resultSet.getInt("fid_var_03");
                mnFkAdministrativeConceptTypeId = resultSet.getInt("fid_tp_adm_cpt");
                mnFkTaxableConceptTypeId = resultSet.getInt("fid_tp_tax_cpt");
                mnFkAccountEbitdaTypeId = resultSet.getInt("fid_tp_acc_ebitda");
                mnFkFiscalAccountIncId = resultSet.getInt("fid_fiscal_acc_inc");
                mnFkFiscalAccountExpId = resultSet.getInt("fid_fiscal_acc_exp");
                mnFkItemPackageId_n = resultSet.getInt("fid_item_pack_n");
                mnFkDefaultItemRefId_n = resultSet.getInt("fid_item_ref_def_n");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                // Read aswell unit object:

                moDbmsDataUnit = new SDataUnit();
                if (moDbmsDataUnit.read(new int[] { mnFkUnitId }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }

                // Read aswell item generic object:

                moDbmsDataItemGeneric = new SDataItemGeneric();
                if (moDbmsDataItemGeneric.read(new int[] { mnFkItemGenericId }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }

                statementAux = statement.getConnection().createStatement();

                // Read aswell barcodes:

                sql = "SELECT id_item, id_barc FROM erp.itmu_item_barc WHERE id_item = " + key[0] + " ORDER BY id_item, id_barc ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    erp.mitm.data.SDataItemBarcode barcode = new SDataItemBarcode();
                    if (barcode.read(new int[] { resultSet.getInt("id_item"), resultSet.getInt("id_barc") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsItemBarcodes.add(barcode);
                    }
                }

                // Read aswell foreign language descriptions:

                sql = "SELECT id_item, id_lan FROM erp.itmu_cfg_item_lan WHERE id_item = " + key[0] + " ORDER BY id_lan ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    erp.mitm.data.SDataItemForeignLanguage desc = new SDataItemForeignLanguage();
                    if (desc.read(new int[] { resultSet.getInt("id_item"), resultSet.getInt("id_lan") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsItemForeignLanguageDescriptions.add(desc);
                    }
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
        int i;
        String sql = "";
        Statement statement = null;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL erp.itmu_item_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?) }");
            callableStatement.setInt(nParam++, mnPkItemId);
            callableStatement.setString(nParam++, msKey);
            callableStatement.setString(nParam++, msItem);
            callableStatement.setString(nParam++, msItemShort);
            callableStatement.setString(nParam++, msName);
            callableStatement.setString(nParam++, msNameShort);
            callableStatement.setString(nParam++, msPresentation);
            callableStatement.setString(nParam++, msPresentationShort);
            callableStatement.setString(nParam++, msCode);
            callableStatement.setBoolean(nParam++, mbIsInventoriable);
            callableStatement.setBoolean(nParam++, mbIsLotApplying);
            callableStatement.setBoolean(nParam++, mbIsBulk);
            callableStatement.setDouble(nParam++, mdUnitsContained);
            callableStatement.setDouble(nParam++, mdUnitsVirtual);
            callableStatement.setDouble(nParam++, mdUnitsPackage);
            callableStatement.setDouble(nParam++, mdNetContent);
            callableStatement.setDouble(nParam++, mdNetContentUnitary);
            callableStatement.setBoolean(nParam++, mbIsNetContentVariable);
            callableStatement.setDouble(nParam++, mdLength);
            callableStatement.setDouble(nParam++, mdLengthUnitary);
            callableStatement.setBoolean(nParam++, mbIsNetContentVariable);
            callableStatement.setDouble(nParam++, mdSurface);
            callableStatement.setDouble(nParam++, mdSurfaceUnitary);
            callableStatement.setBoolean(nParam++, mbIsSurfaceVariable);
            callableStatement.setDouble(nParam++, mdVolume);
            callableStatement.setDouble(nParam++, mdVolumeUnitary);
            callableStatement.setBoolean(nParam++, mbIsVolumeVariable);
            callableStatement.setDouble(nParam++, mdMass);
            callableStatement.setDouble(nParam++, mdMassUnitary);
            callableStatement.setBoolean(nParam++, mbIsMassVariable);
            callableStatement.setDouble(nParam++, mdProductionTime);
            callableStatement.setDouble(nParam++, mdWeightGross);
            callableStatement.setDouble(nParam++, mdWeightDelivery);
            callableStatement.setDouble(nParam++, mdUnitAlternativeBaseEquivalence);
            callableStatement.setDouble(nParam++, mdSurplusPercentage);
            callableStatement.setBoolean(nParam++, mbIsReference);
            callableStatement.setBoolean(nParam++, mbIsPrepayment);
            callableStatement.setBoolean(nParam++, mbIsFreePrice);
            callableStatement.setBoolean(nParam++, mbIsFreeDiscount);
            callableStatement.setBoolean(nParam++, mbIsFreeDiscountUnitary);
            callableStatement.setBoolean(nParam++, mbIsFreeDiscountEntry);
            callableStatement.setBoolean(nParam++, mbIsFreeDiscountDoc);
            callableStatement.setBoolean(nParam++, mbIsFreeCommissions);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkItemGenericId);
            if (mnFkItemLineId_n > 0) callableStatement.setInt(nParam++, mnFkItemLineId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            callableStatement.setInt(nParam++, mnFkItemStatusId);
            callableStatement.setInt(nParam++, mnFkUnitId);
            callableStatement.setInt(nParam++, mnFkUnitUnitsContainedId);
            callableStatement.setInt(nParam++, mnFkUnitUnitsVirtualId);
            callableStatement.setInt(nParam++, mnFkUnitNetContentId);
            callableStatement.setInt(nParam++, mnFkUnitNetContentUnitaryId);
            callableStatement.setInt(nParam++, mnFkUnitAlternativeTypeId);
            callableStatement.setInt(nParam++, mnFkLevelTypeId);
            callableStatement.setInt(nParam++, mnFkBrandId);
            callableStatement.setInt(nParam++, mnFkManufacturerId);
            callableStatement.setInt(nParam++, mnFkElementId);
            callableStatement.setInt(nParam++, mnFkVariety01Id);
            callableStatement.setInt(nParam++, mnFkVariety02Id);
            callableStatement.setInt(nParam++, mnFkVariety03Id);
            callableStatement.setInt(nParam++, mnFkAdministrativeConceptTypeId);
            callableStatement.setInt(nParam++, mnFkTaxableConceptTypeId);
            callableStatement.setInt(nParam++, mnFkAccountEbitdaTypeId);
            callableStatement.setInt(nParam++, mnFkFiscalAccountIncId);
            callableStatement.setInt(nParam++, mnFkFiscalAccountExpId);
            if (mnFkItemPackageId_n > 0) callableStatement.setInt(nParam++, mnFkItemPackageId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkDefaultItemRefId_n > 0) callableStatement.setInt(nParam++, mnFkDefaultItemRefId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkItemId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                statement = connection.createStatement();
                // Save aswell the barcodes:

                for (i = 0; i < mvDbmsItemBarcodes.size(); i++) {
                    mvDbmsItemBarcodes.get(i).setPkItemId(mnPkItemId);
                    if (mvDbmsItemBarcodes.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell foreign language descriptions:

                sql = "DELETE FROM erp.itmu_cfg_item_lan WHERE id_item = " + mnPkItemId + " ";
                statement.execute(sql);

                for (i = 0; i < mvDbmsItemForeignLanguageDescriptions.size(); i++) {
                    mvDbmsItemForeignLanguageDescriptions.get(i).setPkItemId(mnPkItemId);
                    if (mvDbmsItemForeignLanguageDescriptions.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

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
    
    public String getXtaItemWidthStatus() {
        String item = getItem();
        
        switch (mnFkItemStatusId) {
            case SModSysConsts.ITMS_ST_ITEM_ACT:
                break;
            case SModSysConsts.ITMS_ST_ITEM_RES:
                item += " (!)";
                break;
            case SModSysConsts.ITMS_ST_ITEM_INA:
                item += " (/)";
                break;
            default:
        }
        
        return item;
    }
}
