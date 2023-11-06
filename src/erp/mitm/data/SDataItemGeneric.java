/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Alfonso Flores, Juan Barajas, Sergio Flores
 */
public class SDataItemGeneric extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkItemGenericId;
    protected java.lang.String msItemGeneric;
    protected java.lang.String msItemGenericShort;
    protected java.lang.String msCode;
    protected boolean mbIsItemLineApplying;
    protected boolean mbIsItemShortApplying;
    protected boolean mbIsItemNameEditable;
    protected int mnNamingLinePosItemGeneric;
    protected int mnNamingLinePosItemLine;
    protected int mnNamingLinePosBrand;
    protected int mnNamingLinePosManufacturer;
    protected int mnNamingOrdinaryPosItemGeneric;
    protected int mnNamingOrdinaryPosBrand;
    protected int mnNamingOrdinaryPosManufacturer;
    protected int mnNamingOrdinaryPosName;
    protected int mnNamingOrdinaryPosPresentation;
    protected boolean mbIsItemKeyApplying;
    protected boolean mbIsItemKeyAutomatic;
    protected boolean mbIsItemKeyEditable;
    protected int mnKeyLinePosItemGeneric;
    protected int mnKeyLinePosItemLine;
    protected int mnKeyLinePosBrand;
    protected int mnKeyLinePosManufacturer;
    protected int mnKeyOrdinaryPosItemGeneric;
    protected int mnKeyOrdinaryPosBrand;
    protected int mnKeyOrdinaryPosManufacturer;
    protected int mnKeyOrdinaryPosCode;
    protected int mnDaysForExpiration;
    protected java.lang.String msSerialNumber;
    protected java.lang.String msSerialNumberFormat;
    protected double mdSurplusPercentage;
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
    protected boolean mbIsSalesFreightRequired;
    protected boolean mbIsDataShipDomesticReq;
    protected boolean mbIsDataShipInternationalReq;
    protected boolean mbIsDataShipQualityReq;
    protected boolean mbIsItemReferenceRequired;
    protected boolean mbIsMaterial;
    protected boolean mbIsDeleted;
    protected int mnFkItemGroupId;
    protected int mnFkItemCategoryId;
    protected int mnFkItemClassId;
    protected int mnFkItemTypeId;
    protected int mnFkUnitTypeId;
    protected int mnFkUnitUnitsContainedTypeId;
    protected int mnFkUnitUnitsVirtualTypeId;
    protected int mnFkUnitNetContentTypeId;
    protected int mnFkUnitNetContentUnitaryTypeId;
    protected int mnFkSerialNumberTypeId;
    protected int mnFkAdministrativeConceptTypeId;
    protected int mnFkTaxableConceptTypeId;
    protected int mnFkDefaultItemRefId_n;
    protected int mnFkCfdProdServId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected int mnDbmsFkItemFamilyId;
    protected java.util.Vector<erp.mitm.data.SDataItemGenericBizArea> mvDbmsBizAreas;

    public SDataItemGeneric() {
        super(SDataConstants.ITMU_IGEN);
        mvDbmsBizAreas = new Vector<>();
        reset();
    }

    public void setPkItemGenericId(int n) { mnPkItemGenericId = n; }
    public void setItemGeneric(java.lang.String s) { msItemGeneric = s; }
    public void setItemGenericShort(java.lang.String s) { msItemGenericShort = s; }
    public void setCode(java.lang.String s) { msCode = s; }
    public void setIsItemLineApplying(boolean b) { mbIsItemLineApplying = b; }
    public void setIsItemShortApplying(boolean b) { mbIsItemShortApplying = b; }
    public void setIsItemNameEditable(boolean b) { mbIsItemNameEditable = b; }
    public void setNamingLinePosItemGeneric(int n) { mnNamingLinePosItemGeneric = n; }
    public void setNamingLinePosItemLine(int n) { mnNamingLinePosItemLine = n; }
    public void setNamingLinePosBrand(int n) { mnNamingLinePosBrand = n; }
    public void setNamingLinePosManufacturer(int n) { mnNamingLinePosManufacturer = n; }
    public void setNamingOrdinaryPosItemGeneric(int n) { mnNamingOrdinaryPosItemGeneric = n; }
    public void setNamingOrdinaryPosBrand(int n) { mnNamingOrdinaryPosBrand = n; }
    public void setNamingOrdinaryPosManufacturer(int n) { mnNamingOrdinaryPosManufacturer = n; }
    public void setNamingOrdinaryPosName(int n) { mnNamingOrdinaryPosName = n; }
    public void setNamingOrdinaryPosPresentation(int n) { mnNamingOrdinaryPosPresentation = n; }
    public void setIsItemKeyApplying(boolean b) { mbIsItemKeyApplying = b; }
    public void setIsItemKeyAutomatic(boolean b) { mbIsItemKeyAutomatic = b; }
    public void setIsItemKeyEditable(boolean b) { mbIsItemKeyEditable = b; }
    public void setKeyLinePosItemGeneric(int n) { mnKeyLinePosItemGeneric = n; }
    public void setKeyLinePosItemLine(int n) { mnKeyLinePosItemLine = n; }
    public void setKeyLinePosBrand(int n) { mnKeyLinePosBrand = n; }
    public void setKeyLinePosManufacturer(int n) { mnKeyLinePosManufacturer = n; }
    public void setKeyOrdinaryPosItemGeneric(int n) { mnKeyOrdinaryPosItemGeneric = n; }
    public void setKeyOrdinaryPosBrand(int n) { mnKeyOrdinaryPosBrand = n; }
    public void setKeyOrdinaryPosManufacturer(int n) { mnKeyOrdinaryPosManufacturer = n; }
    public void setKeyOrdinaryPosCode(int n) { mnKeyOrdinaryPosCode = n; }
    public void setDaysForExpiration(int n) { mnDaysForExpiration = n; }
    public void setSerialNumber(java.lang.String s) { msSerialNumber = s; }
    public void setSerialNumberFormat(java.lang.String s) { msSerialNumberFormat = s; }
    public void setSurplusPercentage(double d) { mdSurplusPercentage = d; }
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
    public void setIsSalesFreightRequired(boolean b) { mbIsSalesFreightRequired = b; }
    public void setIsDataShipDomesticReq(boolean b) { mbIsDataShipDomesticReq = b; }
    public void setIsDataShipInternationalReq(boolean b) { mbIsDataShipInternationalReq = b; }
    public void setIsDataShipQualityReq(boolean b) { mbIsDataShipQualityReq = b; }
    public void setIsItemReferenceRequired(boolean b) { mbIsItemReferenceRequired = b; }
    public void setIsMaterial(boolean b) { mbIsMaterial = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkItemGroupId(int n) { mnFkItemGroupId = n; }
    public void setFkItemCategoryId(int n) { mnFkItemCategoryId = n; }
    public void setFkItemClassId(int n) { mnFkItemClassId = n; }
    public void setFkItemTypeId(int n) { mnFkItemTypeId = n; }
    public void setFkUnitTypeId(int n) { mnFkUnitTypeId = n; }
    public void setFkUnitUnitsContainedTypeId(int n) { mnFkUnitUnitsContainedTypeId = n; }
    public void setFkUnitUnitsVirtualTypeId(int n) { mnFkUnitUnitsVirtualTypeId = n; }
    public void setFkUnitNetContentTypeId(int n) { mnFkUnitNetContentTypeId = n; }
    public void setFkUnitNetContentUnitaryTypeId(int n) { mnFkUnitNetContentUnitaryTypeId = n; }
    public void setFkSerialNumberTypeId(int n) { mnFkSerialNumberTypeId = n; }
    public void setFkAdministrativeConceptTypeId(int n) { mnFkAdministrativeConceptTypeId = n; }
    public void setFkTaxableConceptTypeId(int n) { mnFkTaxableConceptTypeId = n; }
    public void setFkDefaultItemRefId_n(int n) { mnFkDefaultItemRefId_n = n; }
    public void setFkCfdProdServId(int n) { mnFkCfdProdServId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkItemGenericId() { return mnPkItemGenericId; }
    public java.lang.String getItemGeneric() { return msItemGeneric; }
    public java.lang.String getItemGenericShort() { return msItemGenericShort; }
    public java.lang.String getCode() { return msCode; }
    public boolean getIsItemLineApplying() { return mbIsItemLineApplying; }
    public boolean getIsItemShortApplying() { return mbIsItemShortApplying; }
    public boolean getIsItemNameEditable() { return mbIsItemNameEditable; }
    public int getNamingLinePosItemGeneric() { return mnNamingLinePosItemGeneric; }
    public int getNamingLinePosItemLine() { return mnNamingLinePosItemLine; }
    public int getNamingLinePosBrand() { return mnNamingLinePosBrand; }
    public int getNamingLinePosManufacturer() { return mnNamingLinePosManufacturer; }
    public int getNamingOrdinaryPosItemGeneric() { return mnNamingOrdinaryPosItemGeneric; }
    public int getNamingOrdinaryPosBrand() { return mnNamingOrdinaryPosBrand; }
    public int getNamingOrdinaryPosManufacturer() { return mnNamingOrdinaryPosManufacturer; }
    public int getNamingOrdinaryPosName() { return mnNamingOrdinaryPosName; }
    public int getNamingOrdinaryPosPresentation() { return mnNamingOrdinaryPosPresentation; }
    public boolean getIsItemKeyApplying() { return mbIsItemKeyApplying; }
    public boolean getIsItemKeyAutomatic() { return mbIsItemKeyAutomatic; }
    public boolean getIsItemKeyEditable() { return mbIsItemKeyEditable; }
    public int getKeyLinePosItemGeneric() { return mnKeyLinePosItemGeneric; }
    public int getKeyLinePosItemLine() { return mnKeyLinePosItemLine; }
    public int getKeyLinePosBrand() { return mnKeyLinePosBrand; }
    public int getKeyLinePosManufacturer() { return mnKeyLinePosManufacturer; }
    public int getKeyOrdinaryPosItemGeneric() { return mnKeyOrdinaryPosItemGeneric; }
    public int getKeyOrdinaryPosBrand() { return mnKeyOrdinaryPosBrand; }
    public int getKeyOrdinaryPosManufacturer() { return mnKeyOrdinaryPosManufacturer; }
    public int getKeyOrdinaryPosCode() { return mnKeyOrdinaryPosCode; }
    public int getDaysForExpiration() { return mnDaysForExpiration; }
    public java.lang.String getSerialNumber() { return msSerialNumber; }
    public java.lang.String getSerialNumberFormat() { return msSerialNumberFormat; }
    public double getSurplusPercentage() { return mdSurplusPercentage; }
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
    public boolean getIsSalesFreightRequired() { return mbIsSalesFreightRequired; }
    public boolean getIsDataShipDomesticReq() { return mbIsDataShipDomesticReq; }
    public boolean getIsDataShipInternationalReq() { return mbIsDataShipInternationalReq; }
    public boolean getIsDataQualityReq() { return mbIsDataShipQualityReq; }
    public boolean getIsItemReferenceRequired() { return mbIsItemReferenceRequired; }
    public boolean getIsMaterial() { return mbIsMaterial; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkItemGroupId() { return mnFkItemGroupId; }
    public int getFkItemCategoryId() { return mnFkItemCategoryId; }
    public int getFkItemClassId() { return mnFkItemClassId; }
    public int getFkItemTypeId() { return mnFkItemTypeId; }
    public int getFkUnitTypeId() { return mnFkUnitTypeId; }
    public int getFkUnitUnitsContainedTypeId() { return mnFkUnitUnitsContainedTypeId; }
    public int getFkUnitUnitsVirtualTypeId() { return mnFkUnitUnitsVirtualTypeId; }
    public int getFkUnitNetContentTypeId() { return mnFkUnitNetContentTypeId; }
    public int getFkUnitNetContentUnitaryTypeId() { return mnFkUnitNetContentUnitaryTypeId; }
    public int getFkSerialNumberTypeId() { return mnFkSerialNumberTypeId; }
    public int getFkAdministrativeConceptTypeId() { return mnFkAdministrativeConceptTypeId; }
    public int getFkTaxableConceptTypeId() { return mnFkTaxableConceptTypeId; }
    public int getFkDefaultItemRefId_n() { return mnFkDefaultItemRefId_n; }
    public int getFkCfdProdServId() { return mnFkCfdProdServId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public int getDbmsFkItemFamilyId() { return mnDbmsFkItemFamilyId; }
    public java.util.Vector<SDataItemGenericBizArea> getDbmsBizAreas() { return mvDbmsBizAreas; }
    
    public int[] getItemCategoryKey() { return new int[] { mnFkItemCategoryId }; }
    public int[] getItemClassKey() { return new int[] { mnFkItemCategoryId, mnFkItemClassId }; }
    public int[] getItemTypeKey() { return new int[] { mnFkItemCategoryId, mnFkItemClassId, mnFkItemTypeId }; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemGenericId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkItemGenericId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemGenericId = 0;
        msItemGeneric = "";
        msItemGenericShort = "";
        msCode = "";
        mbIsItemLineApplying = false;
        mbIsItemShortApplying = false;
        mbIsItemNameEditable = false;
        mnNamingLinePosItemGeneric = 0;
        mnNamingLinePosItemLine = 0;
        mnNamingLinePosBrand = 0;
        mnNamingLinePosManufacturer = 0;
        mnNamingOrdinaryPosItemGeneric = 0;
        mnNamingOrdinaryPosBrand = 0;
        mnNamingOrdinaryPosManufacturer = 0;
        mnNamingOrdinaryPosName = 0;
        mnNamingOrdinaryPosPresentation = 0;
        mbIsItemKeyApplying = false;
        mbIsItemKeyAutomatic = false;
        mbIsItemKeyEditable = false;
        mnKeyLinePosItemGeneric = 0;
        mnKeyLinePosItemLine = 0;
        mnKeyLinePosBrand = 0;
        mnKeyLinePosManufacturer = 0;
        mnKeyOrdinaryPosItemGeneric = 0;
        mnKeyOrdinaryPosBrand = 0;
        mnKeyOrdinaryPosManufacturer = 0;
        mnKeyOrdinaryPosCode = 0;
        mnDaysForExpiration = 0;
        msSerialNumber = "";
        msSerialNumberFormat = "";
        mdSurplusPercentage = 0;
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
        mbIsSalesFreightRequired = false;
        mbIsDataShipDomesticReq = false;
        mbIsDataShipInternationalReq = false;
        mbIsDataShipQualityReq = false;
        mbIsItemReferenceRequired = false;
        mbIsMaterial = false;
        mbIsDeleted = false;
        mnFkItemGroupId = 0;
        mnFkItemCategoryId = 0;
        mnFkItemClassId = 0;
        mnFkItemTypeId = 0;
        mnFkUnitTypeId = 0;
        mnFkUnitUnitsContainedTypeId = 0;
        mnFkUnitUnitsVirtualTypeId = 0;
        mnFkUnitNetContentTypeId = 0;
        mnFkUnitNetContentUnitaryTypeId = 0;
        mnFkSerialNumberTypeId = 0;
        mnFkAdministrativeConceptTypeId = 0;
        mnFkTaxableConceptTypeId = 0;
        mnFkDefaultItemRefId_n = 0;
        mnFkCfdProdServId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mnDbmsFkItemFamilyId = 0;
        mvDbmsBizAreas.clear();
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
            sql = "SELECT igen.*, igrp.fid_ifam " +
                    "FROM erp.itmu_igen AS igen " +
                    "INNER JOIN erp.itmu_igrp AS igrp ON " +
                    "igen.fid_igrp = igrp.id_igrp " +
                    "WHERE igen.id_igen = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkItemGenericId = resultSet.getInt("igen.id_igen");
                msItemGeneric = resultSet.getString("igen.igen");
                msItemGenericShort = resultSet.getString("igen.igen_short");
                msCode = resultSet.getString("igen.code");
                mbIsItemLineApplying = resultSet.getBoolean("igen.b_line");
                mbIsItemShortApplying = resultSet.getBoolean("igen.b_item_short");
                mbIsItemNameEditable = resultSet.getBoolean("igen.b_item_name_edit");
                mnNamingLinePosItemGeneric = resultSet.getInt("igen.nam_line_pos_igen");
                mnNamingLinePosItemLine = resultSet.getInt("igen.nam_line_pos_line");
                mnNamingLinePosBrand = resultSet.getInt("igen.nam_line_pos_brd");
                mnNamingLinePosManufacturer = resultSet.getInt("igen.nam_line_pos_mfr");
                mnNamingOrdinaryPosItemGeneric = resultSet.getInt("igen.nam_ord_pos_igen");
                mnNamingOrdinaryPosBrand = resultSet.getInt("igen.nam_ord_pos_brd");
                mnNamingOrdinaryPosManufacturer = resultSet.getInt("igen.nam_ord_pos_mfr");
                mnNamingOrdinaryPosName = resultSet.getInt("igen.nam_ord_pos_name");
                mnNamingOrdinaryPosPresentation = resultSet.getInt("igen.nam_ord_pos_present");
                mbIsItemKeyApplying = resultSet.getBoolean("igen.b_item_key");
                mbIsItemKeyAutomatic = resultSet.getBoolean("igen.b_item_key_aut");
                mbIsItemKeyEditable = resultSet.getBoolean("igen.b_item_key_edit");
                mnKeyLinePosItemGeneric = resultSet.getInt("igen.key_line_pos_igen");
                mnKeyLinePosItemLine = resultSet.getInt("igen.key_line_pos_line");
                mnKeyLinePosBrand = resultSet.getInt("igen.key_line_pos_brd");
                mnKeyLinePosManufacturer = resultSet.getInt("igen.key_line_pos_mfr");
                mnKeyOrdinaryPosItemGeneric = resultSet.getInt("igen.key_ord_pos_igen");
                mnKeyOrdinaryPosBrand = resultSet.getInt("igen.key_ord_pos_brd");
                mnKeyOrdinaryPosManufacturer = resultSet.getInt("igen.key_ord_pos_mfr");
                mnKeyOrdinaryPosCode = resultSet.getInt("igen.key_ord_pos_code");
                mnDaysForExpiration = resultSet.getInt("igen.days_exp");
                msSerialNumber = resultSet.getString("igen.serial_num");
                msSerialNumberFormat = resultSet.getString("igen.serial_num_fmt");
                mdSurplusPercentage = resultSet.getDouble("igen.surplus_per");
                mbIsInventoriable = resultSet.getBoolean("igen.b_inv");
                mbIsLotApplying = resultSet.getBoolean("igen.b_lot");
                mbIsBulk = resultSet.getBoolean("igen.b_bulk");
                mbIsUnitsContainedApplying = resultSet.getBoolean("igen.b_units_cont");
                mbIsUnitsVirtualApplying = resultSet.getBoolean("igen.b_units_virt");
                mbIsUnitsPackageApplying = resultSet.getBoolean("igen.b_units_pack");
                mbIsNetContentApplying = resultSet.getBoolean("igen.b_net_cont");
                mbIsNetContentUnitaryApplying = resultSet.getBoolean("igen.b_net_cont_u");
                mbIsNetContentVariable = resultSet.getBoolean("igen.b_net_cont_variable");
                mbIsLengthApplying = resultSet.getBoolean("igen.b_len");
                mbIsLengthUnitaryApplying = resultSet.getBoolean("igen.b_len_u");
                mbIsLengthVariable = resultSet.getBoolean("igen.b_len_variable");
                mbIsSurfaceApplying = resultSet.getBoolean("igen.b_surf");
                mbIsSurfaceUnitaryApplying = resultSet.getBoolean("igen.b_surf_u");
                mbIsSurfaceVariable = resultSet.getBoolean("igen.b_surf_variable");
                mbIsVolumeApplying = resultSet.getBoolean("igen.b_vol");
                mbIsVolumeUnitaryApplying = resultSet.getBoolean("igen.b_vol_u");
                mbIsVolumeVariable = resultSet.getBoolean("igen.b_vol_variable");
                mbIsMassApplying = resultSet.getBoolean("igen.b_mass");
                mbIsMassUnitaryApplying = resultSet.getBoolean("igen.b_mass_u");
                mbIsMassVariable = resultSet.getBoolean("igen.b_mass_variable");
                mbIsWeightGrossApplying = resultSet.getBoolean("igen.b_weight_gross");
                mbIsWeightDeliveryApplying = resultSet.getBoolean("igen.b_weight_delivery");
                mbIsFreePrice = resultSet.getBoolean("igen.b_free_price");
                mbIsFreeDiscount = resultSet.getBoolean("igen.b_free_disc");
                mbIsFreeDiscountUnitary = resultSet.getBoolean("igen.b_free_disc_u");
                mbIsFreeDiscountEntry = resultSet.getBoolean("igen.b_free_disc_ety");
                mbIsFreeDiscountDoc = resultSet.getBoolean("igen.b_free_disc_doc");
                mbIsFreeCommissions = resultSet.getBoolean("igen.b_free_comms");
                mbIsSalesFreightRequired = resultSet.getBoolean("igen.b_sales_freight_req");
                mbIsDataShipDomesticReq = resultSet.getBoolean("igen.b_ship_dom");
                mbIsDataShipInternationalReq = resultSet.getBoolean("igen.b_ship_int");
                mbIsDataShipQualityReq = resultSet.getBoolean("igen.b_ship_qlt");
                mbIsItemReferenceRequired = resultSet.getBoolean("igen.b_item_ref");
                mbIsMaterial = resultSet.getBoolean("igen.b_mat");
                mbIsDeleted = resultSet.getBoolean("igen.b_del");
                mnFkItemGroupId = resultSet.getInt("igen.fid_igrp");
                mnFkItemCategoryId = resultSet.getInt("igen.fid_ct_item");
                mnFkItemClassId = resultSet.getInt("igen.fid_cl_item");
                mnFkItemTypeId = resultSet.getInt("igen.fid_tp_item");
                mnFkUnitTypeId = resultSet.getInt("igen.fid_tp_unit");
                mnFkUnitUnitsContainedTypeId = resultSet.getInt("igen.fid_tp_unit_units_cont");
                mnFkUnitUnitsVirtualTypeId = resultSet.getInt("igen.fid_tp_unit_units_virt");
                mnFkUnitNetContentTypeId = resultSet.getInt("igen.fid_tp_unit_net_cont");
                mnFkUnitNetContentUnitaryTypeId = resultSet.getInt("igen.fid_tp_unit_net_cont_u");
                mnFkSerialNumberTypeId = resultSet.getInt("igen.fid_tp_snr");
                mnFkAdministrativeConceptTypeId = resultSet.getInt("igen.fid_tp_adm_cpt");
                mnFkTaxableConceptTypeId = resultSet.getInt("igen.fid_tp_tax_cpt");
                mnFkDefaultItemRefId_n = resultSet.getInt("igen.fid_item_ref_def_n");
                mnFkCfdProdServId = resultSet.getInt("igen.fid_cfd_prod_serv");
                mnFkUserNewId = resultSet.getInt("igen.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("igen.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("igen.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("igen.ts_new");
                mtUserEditTs = resultSet.getTimestamp("igen.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("igen.ts_del");
                
                mnDbmsFkItemFamilyId = resultSet.getInt("igrp.fid_ifam");

                // Read aswell item generic's business areas:

                statementAux = statement.getConnection().createStatement();

                sql = "SELECT id_igen, id_ba FROM erp.itmu_igen_ba WHERE id_igen = " + key[0] + " ORDER BY id_ba ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataItemGenericBizArea bizArea = new SDataItemGenericBizArea();
                    if (bizArea.read(new int[] { resultSet.getInt("id_igen"), resultSet.getInt("id_ba") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBizAreas.add(bizArea);
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
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL erp.itmu_igen_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ? ) }");
            callableStatement.setInt(nParam++, mnPkItemGenericId);
            callableStatement.setString(nParam++, msItemGeneric);
            callableStatement.setString(nParam++, msItemGenericShort);
            callableStatement.setString(nParam++, msCode);
            callableStatement.setBoolean(nParam++, mbIsItemLineApplying);
            callableStatement.setBoolean(nParam++, mbIsItemShortApplying);
            callableStatement.setBoolean(nParam++, mbIsItemNameEditable);
            callableStatement.setInt(nParam++, mnNamingLinePosItemGeneric);
            callableStatement.setInt(nParam++, mnNamingLinePosItemLine);
            callableStatement.setInt(nParam++, mnNamingLinePosBrand);
            callableStatement.setInt(nParam++, mnNamingLinePosManufacturer);
            callableStatement.setInt(nParam++, mnNamingOrdinaryPosItemGeneric);
            callableStatement.setInt(nParam++, mnNamingOrdinaryPosBrand);
            callableStatement.setInt(nParam++, mnNamingOrdinaryPosManufacturer);
            callableStatement.setInt(nParam++, mnNamingOrdinaryPosName);
            callableStatement.setInt(nParam++, mnNamingOrdinaryPosPresentation);
            callableStatement.setBoolean(nParam++, mbIsItemKeyApplying);
            callableStatement.setBoolean(nParam++, mbIsItemKeyAutomatic);
            callableStatement.setBoolean(nParam++, mbIsItemKeyEditable);
            callableStatement.setInt(nParam++, mnKeyLinePosItemGeneric);
            callableStatement.setInt(nParam++, mnKeyLinePosItemLine);
            callableStatement.setInt(nParam++, mnKeyLinePosBrand);
            callableStatement.setInt(nParam++, mnKeyLinePosManufacturer);
            callableStatement.setInt(nParam++, mnKeyOrdinaryPosItemGeneric);
            callableStatement.setInt(nParam++, mnKeyOrdinaryPosBrand);
            callableStatement.setInt(nParam++, mnKeyOrdinaryPosManufacturer);
            callableStatement.setInt(nParam++, mnKeyOrdinaryPosCode);
            callableStatement.setInt(nParam++, mnDaysForExpiration);
            callableStatement.setString(nParam++, msSerialNumber);
            callableStatement.setString(nParam++, msSerialNumberFormat);
            callableStatement.setDouble(nParam++, mdSurplusPercentage);
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
            callableStatement.setBoolean(nParam++, mbIsSalesFreightRequired);
            callableStatement.setBoolean(nParam++, mbIsDataShipDomesticReq);
            callableStatement.setBoolean(nParam++, mbIsDataShipInternationalReq);
            callableStatement.setBoolean(nParam++, mbIsDataShipQualityReq);
            callableStatement.setBoolean(nParam++, mbIsItemReferenceRequired);
            callableStatement.setBoolean(nParam++, mbIsMaterial);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkItemGroupId);
            callableStatement.setInt(nParam++, mnFkItemCategoryId);
            callableStatement.setInt(nParam++, mnFkItemClassId);
            callableStatement.setInt(nParam++, mnFkItemTypeId);
            callableStatement.setInt(nParam++, mnFkUnitTypeId);
            callableStatement.setInt(nParam++, mnFkUnitUnitsContainedTypeId);
            callableStatement.setInt(nParam++, mnFkUnitUnitsVirtualTypeId);
            callableStatement.setInt(nParam++, mnFkUnitNetContentTypeId);
            callableStatement.setInt(nParam++, mnFkUnitNetContentUnitaryTypeId);
            callableStatement.setInt(nParam++, mnFkSerialNumberTypeId);
            callableStatement.setInt(nParam++, mnFkAdministrativeConceptTypeId);
            callableStatement.setInt(nParam++, mnFkTaxableConceptTypeId);
            if (mnFkDefaultItemRefId_n > 0) callableStatement.setInt(nParam++, mnFkDefaultItemRefId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mnFkCfdProdServId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkItemGenericId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                nParam = 1;

                callableStatement = connection.prepareCall(
                        "{ CALL erp.itmu_igen_ba_del(" +
                        "?, ?, ?) }");
                callableStatement.setInt(nParam++, mnPkItemGenericId);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                for (int i = 0; i < mvDbmsBizAreas.size(); i++) {
                    mvDbmsBizAreas.get(i).setPkItemGenericId(mnPkItemGenericId);
                    if (mvDbmsBizAreas.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
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
}
