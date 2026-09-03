/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbStockValuationKardex extends SDbRegistryUser implements Serializable {
    
    protected int mnPkStockValuationKardexId;
    protected Date mtMovDate;
    protected double mdQuantityIn;
    protected double mdQuantityOut;
    protected double mdCostUnit;
    protected double mdCostUnitCurrency;
    protected double mdTotalOut;
    protected double mdTotalOutCurrency;
    protected double mdTotalIn;
    protected double mdTotalInCurrency;
    protected double mdExchangeRate;
    // N=No aplica (Default), C=Consumido, P=Pendiente
//    protected String msAdjustStatus;
//    protected boolean mbSystem;
//    protected boolean mbDeleted;
    protected int mnFkDiogCategoryId;
    protected int mnFkStockValuationKardexTypeId;
    protected int mnFkStockValuationKardexId_n;
    protected int mnFkStockValuationMvtId_n;
    protected int mnFkStockValuationId_n;
    protected int mnFkDiogYearInId_n;
    protected int mnFkDiogDocInId_n;
    protected int mnFkDiogEntryInId_n;
    protected int mnFkDpsYearInMainId_n;
    protected int mnFkDpsDocInMainId_n;
    protected int mnFkDpsEntryInMainId_n;
    protected int mnFkDpsCurrencyInMainId_n;
    protected int mnFkDpsYearInAdjustId_n;
    protected int mnFkDpsDocInAdjustId_n;
    protected int mnFkDpsEntryInAdjustId_n;
    protected int mnFkDpsCurrencyInAdjustId_n;
    protected int mnFkDpsYearInOrdId_n;
    protected int mnFkDpsDocInOrdId_n;
    protected int mnFkDpsEntryInOrdId_n;
    protected int mnFkDpsCurrencyInOrdId_n;
    protected int mnFkDiogYearOutId_n;
    protected int mnFkDiogDocOutId_n;
    protected int mnFkDiogEntryOutId_n;
    protected int mnFkDpsYearOutMainId_n;
    protected int mnFkDpsDocOutMainId_n;
    protected int mnFkDpsEntryOutMainId_n;
    protected int mnFkDpsCurrencyOutMainId_n;
    protected int mnFkDpsYearOutAdjustId_n;
    protected int mnFkDpsDocOutAdjustId_n;
    protected int mnFkDpsEntryOutAdjustId_n;
    protected int mnFkDpsCurrencyOutAdjustId_n;
    protected int mnFkDpsYearOutOrdId_n;
    protected int mnFkDpsDocOutOrdId_n;
    protected int mnFkDpsEntryOutOrdId_n;
    protected int mnFkDpsCurrencyOutOrdId_n;
    protected int mnFkMatRequestId_n;
    protected int mnFkMatRequestEntryId_n;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkLotId;
    protected int mnFkCompanyBranchId;
    protected int mnFkWarehouseId;
//    protected int mnFkUserInsertId;
//    protected int mnFkUserUpdateId;
//    protected Date mtTsUserInsert;
//    protected Date mtTsUserUpdate;

    protected List<SDbStockValuationKardexNote> mlNotes;

    protected boolean mbAuxConsumed;
    protected double mdAuxConsumption;
    protected int[] maAuxWarehousePk;
    protected int[] maAuxMaterialRequestEntryPk;
    protected int mnAuxFkCostCenterId;
    protected String msAuxDpsCostCenterCode;
    protected String msAuxItemDescription;
    protected String msAuxDiogTypeDescription;
    protected String msAuxDiogData;
    protected String msAuxMaterialRequestData;
    protected String sLogMessage;
    protected int[] maAuxTypeDpsIn;
    protected int[] maAuxTypeDpsOut;
    protected boolean mbAuxIsAdjust;
    protected int mnAuxInDpsNature;

    // 11	ENTRADA ALMACÉN
    // 12	RETROCESO CONSUMO
    // 13	AJUSTE ENTRADA A LO CONSUMIDO
    // 14	AJUSTE ENTRADA PRECIO FACTURA-OC
        
    // 21	SALIDA ALMACÉN
    // 22	CONSUMO ALMACÉN
    // 23	AJUSTE SALIDA A LO CONSUMIDO
    // 24	AJUSTE ACTIVO FIJO
    
    public static final int TYPE_VAL_KARDEX_IN = 11;
    public static final int TYPE_VAL_KARDEX_IN_RET_CONSUM = 12;
    public static final int TYPE_VAL_KARDEX_IN_ADJUST_CONSUM = 13;
    public static final int TYPE_VAL_KARDEX_IN_ADJUST_DIFF_COST = 14;

    public static final int TYPE_VAL_KARDEX_OUT = 21;
    public static final int TYPE_VAL_KARDEX_OUT_CONSUM = 22;
    public static final int TYPE_VAL_KARDEX_OUT_ADJUST_CONSUM = 23;
    public static final int TYPE_VAL_KARDEX_OUT_ADJUST_DIFF_COST = 24;
    public static final int TYPE_VAL_KARDEX_OUT_ADJUST_FIX_ASSET = 25;

    public static final String ADJ_STATUS_TYPE_NA = "N";
    public static final String ADJ_STATUS_TYPE_PENDING = "P";
    public static final String ADJ_STATUS_TYPE_CONSUMED = "C";
    

    public SDbStockValuationKardex(int idValuation) {
        super(SModConsts.TRN_STK_VAL_KARDEX);
        mnFkStockValuationId_n = idValuation;
    }

    public SDbStockValuationKardex(int idValuation, int idKardexType) {
        super(SModConsts.TRN_STK_VAL_KARDEX);
        mnFkStockValuationId_n = idValuation;
        mnFkStockValuationKardexTypeId = idKardexType;
        if (idKardexType < 20 ) {
            mnFkDiogCategoryId = 1;
        }
        else {
            mnFkDiogCategoryId = 2;
        }
    }
    
    public void setPkStockValKardexId(int n) { mnPkStockValuationKardexId = n; }
    public void setMovDate(Date t) { mtMovDate = t; }
    public void setQuantityIn(double d) { mdQuantityIn = d; }
    public void setQuantityOut(double d) { mdQuantityOut = d; }
    public void setCostUnit(double d) { mdCostUnit = d; }
    public void setCostUnitCurrency(double d) { mdCostUnitCurrency = d; }
    public void setTotalOut(double d) { mdTotalOut = d; }
    public void setTotalOutCurrency(double d) { mdTotalOutCurrency = d; }
    public void setTotalIn(double d) { mdTotalIn = d; }
    public void setTotalInCurrency(double d) { mdTotalInCurrency = d; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
//    public void setAdjustStatus(String s) { msAdjustStatus = s; }
//    public void setSystem(boolean b) { mbSystem = b; }
//    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkDiogCategoryId(int n) { mnFkDiogCategoryId = n; }
    public void setFkStockValuationKardexTypeId(int n) { mnFkStockValuationKardexTypeId = n; }
    public void setFkStockValuationKardexId_n(int n) { mnFkStockValuationKardexId_n = n; }
    public void setFkStockValuationMovementId_n(int n) { mnFkStockValuationMvtId_n = n; }
    public void setFkStockValuationId_n(int n) { mnFkStockValuationId_n = n; }
    public void setFkDiogYearInId_n(int n) { mnFkDiogYearInId_n = n; }
    public void setFkDiogDocInId_n(int n) { mnFkDiogDocInId_n = n; }
    public void setFkDiogEntryInId_n(int n) { mnFkDiogEntryInId_n = n; }
    public void setFkDpsYearInMainId_n(int n) { mnFkDpsYearInMainId_n = n; }
    public void setFkDpsDocInMainId_n(int n) { mnFkDpsDocInMainId_n = n; }
    public void setFkDpsEntryInMainId_n(int n) { mnFkDpsEntryInMainId_n = n; }
    public void setFkDpsCurrencyInMainId_n(int n) { mnFkDpsCurrencyInMainId_n = n; }
    public void setFkDpsYearInAdjustId_n(int n) { mnFkDpsYearInAdjustId_n = n; }
    public void setFkDpsDocInAdjustId_n(int n) { mnFkDpsDocInAdjustId_n = n; }
    public void setFkDpsEntryInAdjustId_n(int n) { mnFkDpsEntryInAdjustId_n = n; }
    public void setFkDpsCurrencyInAdjustId_n(int n) { mnFkDpsCurrencyInAdjustId_n = n; }
    public void setFkDpsYearInOrdId_n(int n) { mnFkDpsYearInOrdId_n = n; }
    public void setFkDpsDocInOrdId_n(int n) { mnFkDpsDocInOrdId_n = n; }
    public void setFkDpsEntryInOrdId_n(int n) { mnFkDpsEntryInOrdId_n = n; }
    public void setFkDpsCurrencyInOrdId_n(int n) { mnFkDpsCurrencyInOrdId_n = n; }
    public void setFkDiogYearOutId_n(int n) { mnFkDiogYearOutId_n = n; }
    public void setFkDiogDocOutId_n(int n) { mnFkDiogDocOutId_n = n; }
    public void setFkDiogEntryOutId_n(int n) { mnFkDiogEntryOutId_n = n; }
    public void setFkDpsYearOutMainId_n(int n) { mnFkDpsYearOutMainId_n = n; }
    public void setFkDpsDocOutMainId_n(int n) { mnFkDpsDocOutMainId_n = n; }
    public void setFkDpsEntryOutMainId_n(int n) { mnFkDpsEntryOutMainId_n = n; }
    public void setFkDpsCurrencyOutMainId_n(int n) { mnFkDpsCurrencyOutMainId_n = n; }
    public void setFkDpsYearOutAdjustId_n(int n) { mnFkDpsYearOutAdjustId_n = n; }
    public void setFkDpsDocOutAdjustId_n(int n) { mnFkDpsDocOutAdjustId_n = n; }
    public void setFkDpsEntryOutAdjustId_n(int n) { mnFkDpsEntryOutAdjustId_n = n; }
    public void setFkDpsCurrencyOutAdjustId_n(int n) { mnFkDpsCurrencyOutAdjustId_n = n; }
    public void setFkDpsYearOutOrdId_n(int n) { mnFkDpsYearOutOrdId_n = n; }
    public void setFkDpsDocOutOrdId_n(int n) { mnFkDpsDocOutOrdId_n = n; }
    public void setFkDpsEntryOutOrdId_n(int n) { mnFkDpsEntryOutOrdId_n = n; }
    public void setFkDpsCurrencyOutOrdId_n(int n) { mnFkDpsCurrencyOutOrdId_n = n; }
    public void setFkMatRequestId_n(int n) { mnFkMatRequestId_n = n; }
    public void setFkMatRequestEntryId_n(int n) { mnFkMatRequestEntryId_n = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkLotId(int n) { mnFkLotId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkWarehouseId(int n) { mnFkWarehouseId = n; }
//    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
//    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
//    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
//    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public void setNotes(List<SDbStockValuationKardexNote> notes) { this.mlNotes = notes; }

    public void setAuxConsumed(boolean b) { mbAuxConsumed = b; }
    public void setAuxConsumption(double d) { mdAuxConsumption = d; }
    public void setAuxFkCostCenterId(int n) { mnAuxFkCostCenterId = n; }
    public void setAuxDpsCostCenterCode(String s) { msAuxDpsCostCenterCode = s; }
    public void setAuxItemDescription(String s) { msAuxItemDescription = s; }
    public void setAuxDiogTypeDescription(String s) { msAuxDiogTypeDescription = s; }
    public void setAuxDiogData(String s) { msAuxDiogData = s; }
    public void setAuxMaterialRequestData(String s) { msAuxMaterialRequestData = s; }
    public void setLogMessage(String s) { sLogMessage = s; }
    public void setAuxTypeDpsIn(int[] typeDpsIn) { maAuxTypeDpsIn = typeDpsIn; }
    public void setAuxTypeDpsOut(int[] typeDpsOut) { maAuxTypeDpsOut = typeDpsOut; }
    public void setAuxIsAdjust(boolean b) { mbAuxIsAdjust = b; }
    public void setAuxInDpsNature(int n) { mnAuxInDpsNature = n; }
    
    public int getPkStockValKardexId() { return mnPkStockValuationKardexId; }
    public Date getMovDate() { return mtMovDate; }
    public double getQuantityIn() { return mdQuantityIn; }
    public double getQuantityOut() { return mdQuantityOut; }
    public double getCostUnit() { return mdCostUnit; }
    public double getCostUnitCurrency() { return mdCostUnitCurrency; }
    public double getTotalOut() { return mdTotalOut; }
    public double getTotalOutCurrency() { return mdTotalOutCurrency; }
    public double getTotalIn() { return mdTotalIn; }
    public double getTotalInCurrency() { return mdTotalInCurrency; }
    public double getExchangeRate() { return mdExchangeRate; }
//    public String getAdjustStatus() { return msAdjustStatus; }
//    public boolean isSystem() { return mbSystem; }
//    public boolean isDeleted() { return mbDeleted; }
    public int getFkDiogCategoryId() { return mnFkDiogCategoryId; }
    public int getFkStockValuationKardexTypeId() { return mnFkStockValuationKardexTypeId; }
    public int getFkStockValuationKardexId_n() { return mnFkStockValuationKardexId_n; }
    public int getFkStockValuationMovementId_n() { return mnFkStockValuationMvtId_n; }
    public int getFkStockValuationId_n() { return mnFkStockValuationId_n; }
    public int getFkDiogYearInId_n() { return mnFkDiogYearInId_n; }
    public int getFkDiogDocInId_n() { return mnFkDiogDocInId_n; }
    public int getFkDiogEntryInId_n() { return mnFkDiogEntryInId_n; }
    public int getFkDpsYearInMainId_n() { return mnFkDpsYearInMainId_n; }
    public int getFkDpsDocInMainId_n() { return mnFkDpsDocInMainId_n; }
    public int getFkDpsEntryInMainId_n() { return mnFkDpsEntryInMainId_n; }
    public int getFkDpsCurrencyInMainId_n() { return mnFkDpsCurrencyInMainId_n; }
    public int getFkDpsYearInAdjustId_n() { return mnFkDpsYearInAdjustId_n; }
    public int getFkDpsDocInAdjustId_n() { return mnFkDpsDocInAdjustId_n; }
    public int getFkDpsEntryInAdjustId_n() { return mnFkDpsEntryInAdjustId_n; }
    public int getFkDpsCurrencyInAdjustId_n() { return mnFkDpsCurrencyInAdjustId_n; }
    public int getFkDpsYearInOrdId_n() { return mnFkDpsYearInOrdId_n; }
    public int getFkDpsDocInOrdId_n() { return mnFkDpsDocInOrdId_n; }
    public int getFkDpsEntryInOrdId_n() { return mnFkDpsEntryInOrdId_n; }
    public int getFkDpsCurrencyInOrdId_n() { return mnFkDpsCurrencyInOrdId_n; }
    public int getFkDiogYearOutId_n() { return mnFkDiogYearOutId_n; }
    public int getFkDiogDocOutId_n() { return mnFkDiogDocOutId_n; }
    public int getFkDiogEntryOutId_n() { return mnFkDiogEntryOutId_n; }
    public int getFkDpsYearOutMainId_n() { return mnFkDpsYearOutMainId_n; }
    public int getFkDpsDocOutMainId_n() { return mnFkDpsDocOutMainId_n; }
    public int getFkDpsEntryOutMainId_n() { return mnFkDpsEntryOutMainId_n; }
    public int getFkDpsCurrencyOutMainId_n() { return mnFkDpsCurrencyOutMainId_n; }
    public int getFkDpsYearOutAdjustId_n() { return mnFkDpsYearOutAdjustId_n; }
    public int getFkDpsDocOutAdjustId_n() { return mnFkDpsDocOutAdjustId_n; }
    public int getFkDpsEntryOutAdjustId_n() { return mnFkDpsEntryOutAdjustId_n; }
    public int getFkDpsCurrencyOutAdjustId_n() { return mnFkDpsCurrencyOutAdjustId_n; }
    public int getFkDpsYearOutOrdId_n() { return mnFkDpsYearOutOrdId_n; }
    public int getFkDpsDocOutOrdId_n() { return mnFkDpsDocOutOrdId_n; }
    public int getFkDpsEntryOutOrdId_n() { return mnFkDpsEntryOutOrdId_n; }
    public int getFkDpsCurrencyOutOrdId_n() { return mnFkDpsCurrencyOutOrdId_n; }
    public int getFkMatRequestId_n() { return mnFkMatRequestId_n; }
    public int getFkMatRequestEntryId_n() { return mnFkMatRequestEntryId_n; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkLotId() { return mnFkLotId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkWarehouseId() { return mnFkWarehouseId; }
//    public int getFkUserInsertId() { return mnFkUserInsertId; }
//    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
//    public Date getTsUserInsert() { return mtTsUserInsert; }
//    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public List<SDbStockValuationKardexNote> getNotes() { return mlNotes; }

    public boolean isAuxConsumed() { return mbAuxConsumed; }
    public double getAuxConsumption() { return mdAuxConsumption; }
    public int getAuxFkCostCenter() { return mnAuxFkCostCenterId; }
    public String getAuxDpsCostCenterCode() { return msAuxDpsCostCenterCode; }
    public String getAuxItemDescription() { return msAuxItemDescription; }
    public String getAuxDiogTypeDescription() { return msAuxDiogTypeDescription; }
    public String getAuxDiogData() { return msAuxDiogData; }
    public String getAuxMaterialRequestData() { return msAuxMaterialRequestData; }
    public String getLogMessage() { return sLogMessage; }
    public int[] getAuxTypeDpsIn() { return maAuxTypeDpsIn; }
    public int[] getAuxTypeDpsOut() { return maAuxTypeDpsOut; }
    public boolean isAuxAdjust() { return mbAuxIsAdjust; }
    public int getAuxInDpsNature() { return mnAuxInDpsNature; }

    public int[] getAuxWarehousePk() {
        if (mnFkWarehouseId > 0) {
            return new int [] { mnFkCompanyBranchId, mnFkWarehouseId }; 
        }
        else {
            return null;
        }
    }
    
    public int[] getAuxMaterialRequestEntryPk() {
        if (mnFkMatRequestId_n > 0) {
            return new int [] { mnFkMatRequestId_n, mnFkMatRequestEntryId_n }; 
        }
        else {
            return null;
        }
    }
    
    public boolean hasInDps() {
        return mnFkDpsYearInMainId_n > 0 && mnFkDpsDocInMainId_n > 0 && mnFkDpsEntryInMainId_n > 0;
    }

    @Override
    public void setPrimaryKey(int[] key) {
        mnPkStockValuationKardexId = key[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockValuationKardexId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStockValuationKardexId = 0;
        mtMovDate = null;
        mdQuantityIn = 0;
        mdQuantityOut = 0;
        mdCostUnit = 0;
        mdCostUnitCurrency = 0;
        mdTotalOut = 0;
        mdTotalOutCurrency = 0;
        mdTotalIn = 0;
        mdTotalInCurrency = 0;
        mdExchangeRate = 0;
//        msAdjustStatus = "N";
        mbSystem = false;
        mbDeleted = false;
        mnFkDiogCategoryId = 0;
        mnFkStockValuationKardexTypeId = 0;
        mnFkStockValuationKardexId_n = 0;
        mnFkStockValuationMvtId_n = 0;
        mnFkStockValuationId_n = 0;
        mnFkDiogYearInId_n = 0;
        mnFkDiogDocInId_n = 0;
        mnFkDiogEntryInId_n = 0;
        mnFkDpsYearInMainId_n = 0;
        mnFkDpsDocInMainId_n = 0;
        mnFkDpsEntryInMainId_n = 0;
        mnFkDpsCurrencyInMainId_n = 0;
        mnFkDpsYearInAdjustId_n = 0;
        mnFkDpsDocInAdjustId_n = 0;
        mnFkDpsEntryInAdjustId_n = 0;
        mnFkDpsCurrencyInAdjustId_n = 0;
        mnFkDpsYearInOrdId_n = 0;
        mnFkDpsDocInOrdId_n = 0;
        mnFkDpsEntryInOrdId_n = 0;
        mnFkDpsCurrencyInOrdId_n = 0;
        mnFkDiogYearOutId_n = 0;
        mnFkDiogDocOutId_n = 0;
        mnFkDiogEntryOutId_n = 0;
        mnFkDpsYearOutMainId_n = 0;
        mnFkDpsDocOutMainId_n = 0;
        mnFkDpsEntryOutMainId_n = 0;
        mnFkDpsCurrencyOutMainId_n = 0;
        mnFkDpsYearOutAdjustId_n = 0;
        mnFkDpsDocOutAdjustId_n = 0;
        mnFkDpsEntryOutAdjustId_n = 0;
        mnFkDpsCurrencyOutAdjustId_n = 0;
        mnFkDpsYearOutOrdId_n = 0;
        mnFkDpsDocOutOrdId_n = 0;
        mnFkDpsEntryOutOrdId_n = 0;
        mnFkDpsCurrencyOutOrdId_n = 0;
        mnFkMatRequestId_n = 0;
        mnFkMatRequestEntryId_n = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkLotId = 0;
        mnFkCompanyBranchId = 0;
        mnFkWarehouseId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mbAuxConsumed = false;
        mdAuxConsumption = 0d;
        msAuxItemDescription = "";
        msAuxDiogTypeDescription = "";
        msAuxDiogData = "";
        msAuxMaterialRequestData = "";
        maAuxTypeDpsIn = new int[] { 0, 0, 0 };
        maAuxTypeDpsOut = new int[] { 0, 0, 0 };
        mbAuxIsAdjust = false;

        mlNotes = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_stk_val_kardex = " + mnPkStockValuationKardexId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_stk_val_kardex = " + pk[0];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;

        mnPkStockValuationKardexId = 0;

        msSql = "SELECT COALESCE(MAX(id_stk_val_kardex), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkStockValuationKardexId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkStockValuationKardexId = resultSet.getInt("id_stk_val_kardex");
            mtMovDate = resultSet.getDate("dt_mov");
            mdQuantityIn = resultSet.getDouble("qty_mov_in");
            mdQuantityOut = resultSet.getDouble("qty_mov_out");
            mdCostUnit = resultSet.getDouble("cost_u");
            mdCostUnitCurrency = resultSet.getDouble("cost_u_cur");
            mdTotalOut = resultSet.getDouble("total_out");
            mdTotalOutCurrency = resultSet.getDouble("total_out_cur");
            mdTotalIn = resultSet.getDouble("total_in");
            mdTotalInCurrency = resultSet.getDouble("total_in_cur");
            mdExchangeRate = resultSet.getDouble("exc_rate");
//            msAdjustStatus = resultSet.getString("adj_st");
            mbSystem = resultSet.getBoolean("b_sys");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkDiogCategoryId = resultSet.getInt("fk_ct_iog");
            mnFkStockValuationKardexTypeId = resultSet.getInt("fk_tp_stk_val_kardex");
            mnFkStockValuationKardexId_n = resultSet.getInt("fk_stk_val_kardex_n");
            mnFkStockValuationMvtId_n= resultSet.getInt("fk_stk_val_mvt_n");
            mnFkStockValuationId_n = resultSet.getInt("fk_stk_val_n");
            mnFkDiogYearInId_n = resultSet.getInt("fk_diog_year_in_n");
            mnFkDiogDocInId_n = resultSet.getInt("fk_diog_doc_in_n");
            mnFkDiogEntryInId_n = resultSet.getInt("fk_diog_ety_in_n");
            mnFkDpsYearInMainId_n = resultSet.getInt("fk_dps_year_in_main_n");
            mnFkDpsDocInMainId_n = resultSet.getInt("fk_dps_doc_in_main_n");
            mnFkDpsEntryInMainId_n = resultSet.getInt("fk_dps_ety_in_main_n");
            mnFkDpsCurrencyInMainId_n = resultSet.getInt("fk_dps_cur_in_main_n");
            mnFkDpsYearInAdjustId_n = resultSet.getInt("fk_dps_year_in_adj_n");
            mnFkDpsDocInAdjustId_n = resultSet.getInt("fk_dps_doc_in_adj_n");
            mnFkDpsEntryInAdjustId_n = resultSet.getInt("fk_dps_ety_in_adj_n");
            mnFkDpsCurrencyInAdjustId_n = resultSet.getInt("fk_dps_cur_in_adj_n");
            mnFkDpsYearInOrdId_n = resultSet.getInt("fk_dps_year_in_ord_n");
            mnFkDpsDocInOrdId_n = resultSet.getInt("fk_dps_doc_in_ord_n");
            mnFkDpsEntryInOrdId_n = resultSet.getInt("fk_dps_ety_in_ord_n");
            mnFkDpsCurrencyInOrdId_n = resultSet.getInt("fk_dps_cur_in_ord_n");
            mnFkDiogYearOutId_n = resultSet.getInt("fk_diog_year_out_n");
            mnFkDiogDocOutId_n = resultSet.getInt("fk_diog_doc_out_n");
            mnFkDiogEntryOutId_n = resultSet.getInt("fk_diog_ety_out_n");
            mnFkDpsYearOutMainId_n = resultSet.getInt("fk_dps_year_out_main_n");
            mnFkDpsDocOutMainId_n = resultSet.getInt("fk_dps_doc_out_main_n");
            mnFkDpsEntryOutMainId_n = resultSet.getInt("fk_dps_ety_out_main_n");
            mnFkDpsCurrencyOutMainId_n = resultSet.getInt("fk_dps_cur_out_main_n");
            mnFkDpsYearOutAdjustId_n = resultSet.getInt("fk_dps_year_out_adj_n");
            mnFkDpsDocOutAdjustId_n = resultSet.getInt("fk_dps_doc_out_adj_n");
            mnFkDpsEntryOutAdjustId_n = resultSet.getInt("fk_dps_ety_out_adj_n");
            mnFkDpsCurrencyOutAdjustId_n = resultSet.getInt("fk_dps_cur_out_adj_n");
            mnFkDpsYearOutOrdId_n = resultSet.getInt("fk_dps_year_out_ord_n");
            mnFkDpsDocOutOrdId_n = resultSet.getInt("fk_dps_doc_out_ord_n");
            mnFkDpsEntryOutOrdId_n = resultSet.getInt("fk_dps_ety_out_ord_n");
            mnFkDpsCurrencyOutOrdId_n = resultSet.getInt("fk_dps_cur_out_ord_n");
            mnFkMatRequestId_n = resultSet.getInt("fk_mat_req_n");
            mnFkMatRequestEntryId_n = resultSet.getInt("fk_mat_req_ety_n");
            mnFkItemId = resultSet.getInt("fk_item");
            mnFkUnitId = resultSet.getInt("fk_unit");
            mnFkLotId = resultSet.getInt("fk_lot");
            mnFkCompanyBranchId = resultSet.getInt("fk_cob");
            mnFkWarehouseId = resultSet.getInt("fk_wh");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkStockValuationKardexId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtMovDate) + "', " + 
                    mdQuantityIn + ", " + 
                    mdQuantityOut + ", " + 
                    mdCostUnit + ", " + 
                    mdCostUnitCurrency + ", " + 
                    mdTotalOut + ", " + 
                    mdTotalOutCurrency + ", " + 
                    mdTotalIn + ", " + 
                    mdTotalInCurrency + ", " + 
                    mdExchangeRate + ", " + 
//                    ((msAdjustStatus == null || msAdjustStatus.isEmpty()) ? "'" + ADJ_STATUS_TYPE_NA + "'" : ("'" + msAdjustStatus + "'")) + ", " +
                    (mbSystem ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkDiogCategoryId + ", " + 
                    mnFkStockValuationKardexTypeId + ", " + 
                    (mnFkStockValuationKardexId_n == 0 ? "NULL" : mnFkStockValuationKardexId_n) + ", " + 
                    (mnFkStockValuationMvtId_n == 0 ? "NULL" : mnFkStockValuationMvtId_n) + ", " +
                    (mnFkStockValuationId_n == 0 ? "NULL" : mnFkStockValuationId_n) + ", " +
                    (mnFkDiogYearInId_n == 0 ? "NULL" : mnFkDiogYearInId_n) + ", " + 
                    (mnFkDiogDocInId_n == 0 ? "NULL" : mnFkDiogDocInId_n) + ", " + 
                    (mnFkDiogEntryInId_n == 0 ? "NULL" : mnFkDiogEntryInId_n) + ", " + 
                    (mnFkDpsYearInMainId_n == 0 ? "NULL" : mnFkDpsYearInMainId_n) + ", " + 
                    (mnFkDpsDocInMainId_n == 0 ? "NULL" : mnFkDpsDocInMainId_n) + ", " + 
                    (mnFkDpsEntryInMainId_n == 0 ? "NULL" : mnFkDpsEntryInMainId_n) + ", " + 
                    (mnFkDpsCurrencyInMainId_n == 0 ? "NULL" : mnFkDpsCurrencyInMainId_n) + ", " + 
                    (mnFkDpsYearInAdjustId_n == 0 ? "NULL" : mnFkDpsYearInAdjustId_n) + ", " + 
                    (mnFkDpsDocInAdjustId_n == 0 ? "NULL" : mnFkDpsDocInAdjustId_n) + ", " + 
                    (mnFkDpsEntryInAdjustId_n == 0 ? "NULL" : mnFkDpsEntryInAdjustId_n) + ", " + 
                    (mnFkDpsCurrencyInAdjustId_n == 0 ? "NULL" : mnFkDpsCurrencyInAdjustId_n) + ", " + 
                    (mnFkDpsYearInOrdId_n == 0 ? "NULL" : mnFkDpsYearInOrdId_n) + ", " + 
                    (mnFkDpsDocInOrdId_n == 0 ? "NULL" : mnFkDpsDocInOrdId_n) + ", " + 
                    (mnFkDpsEntryInOrdId_n == 0 ? "NULL" : mnFkDpsEntryInOrdId_n) + ", " + 
                    (mnFkDpsCurrencyInOrdId_n == 0 ? "NULL" : mnFkDpsCurrencyInOrdId_n) + ", " + 
                    (mnFkDiogYearOutId_n == 0 ? "NULL" : mnFkDiogYearOutId_n) + ", " + 
                    (mnFkDiogDocOutId_n == 0 ? "NULL" : mnFkDiogDocOutId_n) + ", " + 
                    (mnFkDiogEntryOutId_n == 0 ? "NULL" : mnFkDiogEntryOutId_n) + ", " + 
                    (mnFkDpsYearOutMainId_n == 0 ? "NULL" : mnFkDpsYearOutMainId_n) + ", " + 
                    (mnFkDpsDocOutMainId_n == 0 ? "NULL" : mnFkDpsDocOutMainId_n) + ", " + 
                    (mnFkDpsEntryOutMainId_n == 0 ? "NULL" : mnFkDpsEntryOutMainId_n) + ", " + 
                    (mnFkDpsCurrencyOutMainId_n == 0 ? "NULL" : mnFkDpsCurrencyOutMainId_n) + ", " + 
                    (mnFkDpsYearOutAdjustId_n == 0 ? "NULL" : mnFkDpsYearOutAdjustId_n) + ", " + 
                    (mnFkDpsDocOutAdjustId_n == 0 ? "NULL" : mnFkDpsDocOutAdjustId_n) + ", " + 
                    (mnFkDpsEntryOutAdjustId_n == 0 ? "NULL" : mnFkDpsEntryOutAdjustId_n) + ", " + 
                    (mnFkDpsCurrencyOutAdjustId_n == 0 ? "NULL" : mnFkDpsCurrencyOutAdjustId_n) + ", " + 
                    (mnFkDpsYearOutOrdId_n == 0 ? "NULL" : mnFkDpsYearOutOrdId_n) + ", " + 
                    (mnFkDpsDocOutOrdId_n == 0 ? "NULL" : mnFkDpsDocOutOrdId_n) + ", " + 
                    (mnFkDpsEntryOutOrdId_n == 0 ? "NULL" : mnFkDpsEntryOutOrdId_n) + ", " + 
                    (mnFkDpsCurrencyOutOrdId_n == 0 ? "NULL" : mnFkDpsCurrencyOutOrdId_n) + ", " + 
                    (mnFkMatRequestId_n == 0 ? "NULL" : mnFkMatRequestId_n) + ", " + 
                    (mnFkMatRequestEntryId_n == 0 ? "NULL" : mnFkMatRequestEntryId_n) + ", " + 
                    mnFkItemId + ", " + 
                    mnFkUnitId + ", " + 
                    mnFkLotId + ", " + 
                    mnFkCompanyBranchId + ", " + 
                    mnFkWarehouseId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")" ;
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
//                    "id_stk_val_kardex = " + mnPkStockValuationKardexId + ", " +
                    "dt_mov = '" + SLibUtils.DbmsDateFormatDate.format(mtMovDate) + "', " +
                    "qty_mov_in = " + mdQuantityIn + ", " +
                    "qty_mov_out = " + mdQuantityOut + ", " +
                    "cost_u = " + mdCostUnit + ", " +
                    "cost_u_cur = " + mdCostUnitCurrency + ", " +
                    "total_out = " + mdTotalOut + ", " +
                    "total_out_cur = " + mdTotalOutCurrency + ", " +
                    "total_in = " + mdTotalIn + ", " +
                    "total_in_cur = " + mdTotalInCurrency + ", " +
                    "exc_rate = " + mdExchangeRate + ", " +
//                    "adj_st = " + (msAdjustStatus == null || msAdjustStatus.isEmpty() ? "'" + ADJ_STATUS_TYPE_NA + "'" : ("'" + msAdjustStatus + "'")) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_ct_iog = " + mnFkDiogCategoryId + ", " +
                    "fk_tp_stk_val_kardex = " + mnFkStockValuationKardexTypeId + ", " +
                    "fk_stk_val_kardex_n = " + (mnFkStockValuationKardexId_n == 0 ? "NULL" : mnFkStockValuationKardexId_n) + ", " +
                    "fk_stk_val_mvt_n = " + (mnFkStockValuationMvtId_n == 0 ? "NULL" : mnFkStockValuationMvtId_n) + ", " +
                    "fk_stk_val_n = " + (mnFkStockValuationId_n == 0 ? "NULL" : mnFkStockValuationId_n) + ", " +
                    "fk_diog_year_in_n = " + (mnFkDiogYearInId_n == 0 ? "NULL" : mnFkDiogYearInId_n) + ", " +
                    "fk_diog_doc_in_n = " + (mnFkDiogDocInId_n == 0 ? "NULL" : mnFkDiogDocInId_n) + ", " +
                    "fk_diog_ety_in_n = " + (mnFkDiogEntryInId_n == 0 ? "NULL" : mnFkDiogEntryInId_n) + ", " +
                    "fk_dps_year_in_main_n = " + (mnFkDpsYearInMainId_n == 0 ? "NULL" : mnFkDpsYearInMainId_n) + ", " +
                    "fk_dps_doc_in_main_n = " + (mnFkDpsDocInMainId_n == 0 ? "NULL" : mnFkDpsDocInMainId_n) + ", " +
                    "fk_dps_ety_in_main_n = " + (mnFkDpsEntryInMainId_n == 0 ? "NULL" : mnFkDpsEntryInMainId_n) + ", " +
                    "fk_dps_cur_in_main_n = " + (mnFkDpsCurrencyInMainId_n == 0 ? "NULL" : mnFkDpsCurrencyInMainId_n) + ", " +
                    "fk_dps_year_in_adj_n = " + (mnFkDpsYearInAdjustId_n == 0 ? "NULL" : mnFkDpsYearInAdjustId_n) + ", " +
                    "fk_dps_doc_in_adj_n = " + (mnFkDpsDocInAdjustId_n == 0 ? "NULL" : mnFkDpsDocInAdjustId_n) + ", " +
                    "fk_dps_ety_in_adj_n = " + (mnFkDpsEntryInAdjustId_n == 0 ? "NULL" : mnFkDpsEntryInAdjustId_n) + ", " +
                    "fk_dps_cur_in_adj_n = " + (mnFkDpsCurrencyInAdjustId_n == 0 ? "NULL" : mnFkDpsCurrencyInAdjustId_n) + ", " +
                    "fk_dps_year_in_ord_n = " + (mnFkDpsYearInOrdId_n == 0 ? "NULL" : mnFkDpsYearInOrdId_n) + ", " +
                    "fk_dps_doc_in_ord_n = " + (mnFkDpsDocInOrdId_n == 0 ? "NULL" : mnFkDpsDocInOrdId_n) + ", " +
                    "fk_dps_ety_in_ord_n = " + (mnFkDpsEntryInOrdId_n == 0 ? "NULL" : mnFkDpsEntryInOrdId_n) + ", " +
                    "fk_dps_cur_in_ord_n = " + (mnFkDpsCurrencyInOrdId_n == 0 ? "NULL" : mnFkDpsCurrencyInOrdId_n) + ", " +
                    "fk_diog_year_out_n = " + (mnFkDiogYearOutId_n == 0 ? "NULL" : mnFkDiogYearOutId_n) + ", " +
                    "fk_diog_doc_out_n = " + (mnFkDiogDocOutId_n == 0 ? "NULL" : mnFkDiogDocOutId_n) + ", " +
                    "fk_diog_ety_out_n = " + (mnFkDiogEntryOutId_n == 0 ? "NULL" : mnFkDiogEntryOutId_n) + ", " +
                    "fk_dps_year_out_main_n = " + (mnFkDpsYearOutMainId_n == 0 ? "NULL" : mnFkDpsYearOutMainId_n) + ", " +
                    "fk_dps_doc_out_main_n = " + (mnFkDpsDocOutMainId_n == 0 ? "NULL" : mnFkDpsDocOutMainId_n) + ", " +
                    "fk_dps_ety_out_main_n = " + (mnFkDpsEntryOutMainId_n == 0 ? "NULL" : mnFkDpsEntryOutMainId_n) + ", " +
                    "fk_dps_cur_out_main_n = " + (mnFkDpsCurrencyOutMainId_n == 0 ? "NULL" : mnFkDpsCurrencyOutMainId_n) + ", " +
                    "fk_dps_year_out_adj_n = " + (mnFkDpsYearOutAdjustId_n == 0 ? "NULL" : mnFkDpsYearOutAdjustId_n) + ", " +
                    "fk_dps_doc_out_adj_n = " + (mnFkDpsDocOutAdjustId_n == 0 ? "NULL" : mnFkDpsDocOutAdjustId_n) + ", " +
                    "fk_dps_ety_out_adj_n = " + (mnFkDpsEntryOutAdjustId_n == 0 ? "NULL" : mnFkDpsEntryOutAdjustId_n) + ", " +
                    "fk_dps_cur_out_adj_n = " + (mnFkDpsCurrencyOutAdjustId_n == 0 ? "NULL" : mnFkDpsCurrencyOutAdjustId_n) + ", " +
                    "fk_dps_year_out_ord_n = " + (mnFkDpsYearOutOrdId_n == 0 ? "NULL" : mnFkDpsYearOutOrdId_n) + ", " +
                    "fk_dps_doc_out_ord_n = " + (mnFkDpsDocOutOrdId_n == 0 ? "NULL" : mnFkDpsDocOutOrdId_n) + ", " +
                    "fk_dps_ety_out_ord_n = " + (mnFkDpsEntryOutOrdId_n == 0 ? "NULL" : mnFkDpsEntryOutOrdId_n) + ", " +
                    "fk_dps_cur_out_ord_n = " + (mnFkDpsCurrencyOutOrdId_n == 0 ? "NULL" : mnFkDpsCurrencyOutOrdId_n) + ", " +
                    "fk_mat_req_n = " + (mnFkMatRequestId_n == 0 ? "NULL" : mnFkMatRequestId_n) + ", " +
                    "fk_mat_req_ety_n = " + (mnFkMatRequestEntryId_n == 0 ? "NULL" : mnFkMatRequestEntryId_n) + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_unit = " + mnFkUnitId + ", " +
                    "fk_lot = " + mnFkLotId + ", " +
                    "fk_cob = " + mnFkCompanyBranchId + ", " +
                    "fk_wh = " + mnFkWarehouseId + ", " +
                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().getConnection().createStatement().execute(msSql);

        if (! mbRegistryNew) {
            SDbStockValuationKardexNote.deleteAllNotesFromMvt(session, mnPkStockValuationKardexId);
        }
        
        for (SDbStockValuationKardexNote note : mlNotes) {
            note.setFkStockValKardexId(mnPkStockValuationKardexId);
            note.setFkStockValuationId_n(mnFkStockValuationId_n);
            note.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbStockValuationKardex registry = new SDbStockValuationKardex(this.getFkStockValuationId_n());
        
        registry.setPkStockValKardexId(this.getPkStockValKardexId());
        registry.setMovDate(this.getMovDate());
        registry.setQuantityIn(this.getQuantityIn());
        registry.setQuantityOut(this.getQuantityOut());
        registry.setCostUnit(this.getCostUnit());
        registry.setCostUnitCurrency(this.getCostUnitCurrency());
        registry.setTotalOut(this.getTotalOut());
        registry.setTotalOutCurrency(this.getTotalOutCurrency());
        registry.setTotalIn(this.getTotalIn());
        registry.setTotalInCurrency(this.getTotalInCurrency());
        registry.setExchangeRate(this.getExchangeRate());
//        registry.setAdjustStatus(this.getAdjustStatus());
        registry.setSystem(this.isSystem());
        registry.setDeleted(this.isDeleted());
        registry.setFkDiogCategoryId(this.getFkDiogCategoryId());
        registry.setFkStockValuationKardexTypeId(this.getFkStockValuationKardexTypeId());
        registry.setFkStockValuationKardexId_n(this.getFkStockValuationKardexId_n());
        registry.setFkStockValuationMovementId_n(this.getFkStockValuationMovementId_n());
        registry.setFkStockValuationId_n(this.getFkStockValuationId_n());
        registry.setFkDiogYearInId_n(this.getFkDiogYearInId_n());
        registry.setFkDiogDocInId_n(this.getFkDiogDocInId_n());
        registry.setFkDiogEntryInId_n(this.getFkDiogEntryInId_n());
        registry.setFkDpsYearInMainId_n(this.getFkDpsYearInMainId_n());
        registry.setFkDpsDocInMainId_n(this.getFkDpsDocInMainId_n());
        registry.setFkDpsEntryInMainId_n(this.getFkDpsEntryInMainId_n());
        registry.setFkDpsCurrencyInMainId_n(this.getFkDpsCurrencyInMainId_n());
        registry.setFkDpsYearInAdjustId_n(this.getFkDpsYearInAdjustId_n());
        registry.setFkDpsDocInAdjustId_n(this.getFkDpsDocInAdjustId_n());
        registry.setFkDpsEntryInAdjustId_n(this.getFkDpsEntryInAdjustId_n());
        registry.setFkDpsCurrencyInAdjustId_n(this.getFkDpsCurrencyInAdjustId_n());
        registry.setFkDpsYearInOrdId_n(this.getFkDpsYearInOrdId_n());
        registry.setFkDpsDocInOrdId_n(this.getFkDpsDocInOrdId_n());
        registry.setFkDpsEntryInOrdId_n(this.getFkDpsEntryInOrdId_n());
        registry.setFkDpsCurrencyInOrdId_n(this.getFkDpsCurrencyInOrdId_n());
        registry.setFkDiogYearOutId_n(this.getFkDiogYearOutId_n());
        registry.setFkDiogDocOutId_n(this.getFkDiogDocOutId_n());
        registry.setFkDiogEntryOutId_n(this.getFkDiogEntryOutId_n());
        registry.setFkDpsYearOutMainId_n(this.getFkDpsYearOutMainId_n());
        registry.setFkDpsDocOutMainId_n(this.getFkDpsDocOutMainId_n());
        registry.setFkDpsEntryOutMainId_n(this.getFkDpsEntryOutMainId_n());
        registry.setFkDpsCurrencyOutMainId_n(this.getFkDpsCurrencyOutMainId_n());
        registry.setFkDpsYearOutAdjustId_n(this.getFkDpsYearOutAdjustId_n());
        registry.setFkDpsDocOutAdjustId_n(this.getFkDpsDocOutAdjustId_n());
        registry.setFkDpsEntryOutAdjustId_n(this.getFkDpsEntryOutAdjustId_n());
        registry.setFkDpsCurrencyOutAdjustId_n(this.getFkDpsCurrencyOutAdjustId_n());
        registry.setFkDpsYearOutOrdId_n(this.getFkDpsYearOutOrdId_n());
        registry.setFkDpsDocOutOrdId_n(this.getFkDpsDocOutOrdId_n());
        registry.setFkDpsEntryOutOrdId_n(this.getFkDpsEntryOutOrdId_n());
        registry.setFkDpsCurrencyOutOrdId_n(this.getFkDpsCurrencyOutOrdId_n());
        registry.setFkMatRequestId_n(this.getFkMatRequestId_n());
        registry.setFkMatRequestEntryId_n(this.getFkMatRequestEntryId_n());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkLotId(this.getFkLotId());
        registry.setFkCompanyBranchId(this.getFkCompanyBranchId());
        registry.setFkWarehouseId(this.getFkWarehouseId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}
