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
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbStockValuationMvt extends SDbRegistryUser implements SGridRow, Serializable {
    
    protected int mnPkStockValuationMvtId;
    protected Date mtDate;
    protected double mdQuantityMovement;
    protected double mdCostUnitary;
    protected double mdCost_r;
    protected boolean mbTemporalPrice;
    protected boolean mbRevised;
    //protected boolean mbDeleted;
    protected int mnFkStockValuationId;
    protected int mnFkStockValuationMvtId_n;
    protected int mnFkDiogCategoryId;
    protected int mnFkDiogYearInId_n;
    protected int mnFkDiogDocInId_n;
    protected int mnFkDiogEntryInId_n;
    protected int mnFkDpsYearInId_n;
    protected int mnFkDpsDocInId_n;
    protected int mnFkDpsEntryInId_n;
    protected int mnFkDiogYearOutId_n;
    protected int mnFkDiogDocOutId_n;
    protected int mnFkDiogEntryOutId_n;
    protected int mnFkDpsYearOutId_n;
    protected int mnFkDpsDocOutId_n;
    protected int mnFkDpsEntryOutId_n;
    protected int mnFkMaterialRequestId_n;
    protected int mnFkMaterialRequestEntryId_n;
    protected int mnFkStockValuationMvtRevisionId_n;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkLotId;
    protected int mnFkCompanyBranchId;
    protected int mnFkWarehouseId;
    //protected int mnFkUserInsertId;
    //protected int mnFkUserUpdateId;
    //protected Date mtTsUserInsert;
    //protected Date mtTsUserUpdate;

    protected boolean mbAuxConsumed;
    protected double mdAuxConsumption;
    protected int[] maAuxWarehousePk;
    protected int[] maAuxMaterialRequestEntryPk;
    protected int mnAuxFkCostCenterId;
    protected String msAuxItemDescription;
    protected String msAuxDiogTypeDescription;
    protected String msAuxDiogData;
    protected String msAuxMaterialRequestData;
    protected String sLogMessage;
    protected int[] maAuxTypeDpsIn;
    protected int[] maAuxTypeDpsOut;
    protected boolean mbAuxIsAdjust;

    public SDbStockValuationMvt() {
        super(SModConsts.TRN_STK_VAL_MVT);
    }
    
    public void setPkStockValuationMvtId(int n) { mnPkStockValuationMvtId = n; }
    public void setDateMove(Date t) { mtDate = t; }
    public void setQuantityMovement(double d) { mdQuantityMovement = d; }
    public void setCostUnitary(double d) { mdCostUnitary = d; }
    public void setCost_r(double d) { mdCost_r = d; }
    public void setTemporalPrice(boolean b) { mbTemporalPrice = b; }
    public void setRevised(boolean b) { mbRevised = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkStockValuationId(int n) { mnFkStockValuationId = n; }
    public void setFkStockValuationMvtId_n(int n) { mnFkStockValuationMvtId_n = n; }
    public void setFkDiogCategoryId(int n) { mnFkDiogCategoryId = n; }
    public void setFkDiogYearInId_n(int n) { mnFkDiogYearInId_n = n; }
    public void setFkDiogDocInId_n(int n) { mnFkDiogDocInId_n = n; }
    public void setFkDiogEntryInId_n(int n) { mnFkDiogEntryInId_n = n; }
    public void setFkDpsYearInId_n(int n) { mnFkDpsYearInId_n = n; }
    public void setFkDpsDocInId_n(int n) { mnFkDpsDocInId_n = n; }
    public void setFkDpsEntryInId_n(int n) { mnFkDpsEntryInId_n = n; }
    public void setFkDiogYearOutId_n(int n) { mnFkDiogYearOutId_n = n; }
    public void setFkDiogDocOutId_n(int n) { mnFkDiogDocOutId_n = n; }
    public void setFkDiogEntryOutId_n(int n) { mnFkDiogEntryOutId_n = n; }
    public void setFkDpsYearOutId_n(int n) { mnFkDpsYearOutId_n = n; }
    public void setFkDpsDocOutId_n(int n) { mnFkDpsDocOutId_n = n; }
    public void setFkDpsEntryOutId_n(int n) { mnFkDpsEntryOutId_n = n; }
    public void setFkMaterialRequestId_n(int n) { mnFkMaterialRequestId_n = n; }
    public void setFkMaterialRequestEntryId_n(int n) { mnFkMaterialRequestEntryId_n = n; }
    public void setFkStockValuationMvtRevisionId_n(int n) { mnFkStockValuationMvtRevisionId_n = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkLotId(int n) { mnFkLotId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkWarehouseId(int n) { mnFkWarehouseId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public void setAuxConsumed(boolean b) { mbAuxConsumed = b; }
    public void setAuxConsumption(double d) { mdAuxConsumption = d; }
    public void setAuxFkCostCenterId(int n) { mnAuxFkCostCenterId = n; }
    public void setAuxItemDescription(String s) { msAuxItemDescription = s; }
    public void setAuxDiogTypeDescription(String s) { msAuxDiogTypeDescription = s; }
    public void setAuxDiogData(String s) { msAuxDiogData = s; }
    public void setAuxMaterialRequestData(String s) { msAuxMaterialRequestData = s; }
    public void setLogMessage(String s) { sLogMessage = s; }
    public void setAuxTypeDpsIn(int[] typeDpsIn) { maAuxTypeDpsIn = typeDpsIn; }
    public void setAuxTypeDpsOut(int[] typeDpsOut) { maAuxTypeDpsOut = typeDpsOut; }
    public void setAuxIsAdjust(boolean b) { mbAuxIsAdjust = b; }
    
    public int getPkStockValuationMvtId() { return mnPkStockValuationMvtId; }
    public Date getDateMove() { return mtDate; }
    public double getQuantityMovement() { return mdQuantityMovement; }
    public double getCostUnitary() { return mdCostUnitary; }
    public double getCost_r() { return mdCost_r; }
    public boolean isTemporalPrice() { return mbTemporalPrice; }
    public boolean isRevised() { return mbRevised; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkStockValuationId() { return mnFkStockValuationId; }
    public int getFkStockValuationMvtId_n() { return mnFkStockValuationMvtId_n; }
    public int getFkDiogCategoryId() { return mnFkDiogCategoryId; }
    public int getFkDiogYearInId_n() { return mnFkDiogYearInId_n; }
    public int getFkDiogDocInId_n() { return mnFkDiogDocInId_n; }
    public int getFkDiogEntryInId_n() { return mnFkDiogEntryInId_n; }
    public int getFkDpsYearInId_n() { return mnFkDpsYearInId_n; }
    public int getFkDpsDocInId_n() { return mnFkDpsDocInId_n; }
    public int getFkDpsEntryInId_n() { return mnFkDpsEntryInId_n; }
    public int getFkDiogYearOutId_n() { return mnFkDiogYearOutId_n; }
    public int getFkDiogDocOutId_n() { return mnFkDiogDocOutId_n; }
    public int getFkDiogEntryOutId_n() { return mnFkDiogEntryOutId_n; }
    public int getFkDpsYearOutId_n() { return mnFkDpsYearOutId_n; }
    public int getFkDpsDocOutId_n() { return mnFkDpsDocOutId_n; }
    public int getFkDpsEntryOutId_n() { return mnFkDpsEntryOutId_n; }
    public int getFkMaterialRequestId_n() { return mnFkMaterialRequestId_n; }
    public int getFkMaterialRequestEntryId_n() { return mnFkMaterialRequestEntryId_n; }
    public int getFkStockValuationMvtRevisionId_n() { return mnFkStockValuationMvtRevisionId_n; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkLotId() { return mnFkLotId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkWarehouseId() { return mnFkWarehouseId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public boolean isAuxConsumed() { return mbAuxConsumed; }
    public double getAuxConsumption() { return mdAuxConsumption; }
    public int getAuxFkCostCenter() { return mnAuxFkCostCenterId; }
    public String getAuxItemDescription() { return msAuxItemDescription; }
    public String getAuxDiogTypeDescription() { return msAuxDiogTypeDescription; }
    public String getAuxDiogData() { return msAuxDiogData; }
    public String getAuxMaterialRequestData() { return msAuxMaterialRequestData; }
    public String getLogMessage() { return sLogMessage; }
    public int[] getAuxTypeDpsIn() { return maAuxTypeDpsIn; }
    public int[] getAuxTypeDpsOut() { return maAuxTypeDpsOut; }
    public boolean isAuxAdjust() { return mbAuxIsAdjust; }

    public int[] getAuxWarehousePk() {
        if (mnFkWarehouseId > 0) {
            return new int [] { mnFkCompanyBranchId, mnFkWarehouseId }; 
        }
        else {
            return null;
        }
    }
    public int[] getAuxMaterialRequestEntryPk() {
        if (mnFkMaterialRequestId_n > 0) {
            return new int [] { mnFkMaterialRequestId_n, mnFkMaterialRequestEntryId_n }; 
        }
        else {
            return null;
        }
    }

    @Override
    public void setPrimaryKey(int[] key) {
        mnPkStockValuationMvtId = key[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockValuationMvtId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStockValuationMvtId = 0;
        mtDate = null;
        mdQuantityMovement = 0d;
        mdCostUnitary = 0d;
        mdCost_r = 0d;
        mbTemporalPrice = false;
        mbRevised = false;
        mbDeleted = false;
        mnFkStockValuationId = 0;
        mnFkStockValuationMvtId_n = 0;
        mnFkDiogCategoryId = 0;
        mnFkDiogYearInId_n = 0;
        mnFkDiogDocInId_n = 0;
        mnFkDiogEntryInId_n = 0;
        mnFkDpsYearInId_n = 0;
        mnFkDpsDocInId_n = 0;
        mnFkDpsEntryInId_n = 0;
        mnFkDiogYearOutId_n = 0;
        mnFkDiogDocOutId_n = 0;
        mnFkDiogEntryOutId_n = 0;
        mnFkDpsYearOutId_n = 0;
        mnFkDpsDocOutId_n = 0;
        mnFkDpsEntryOutId_n = 0;
        mnFkMaterialRequestId_n = 0;
        mnFkMaterialRequestEntryId_n = 0;
        mnFkStockValuationMvtRevisionId_n = 0;
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
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_stk_val_mvt = " + mnPkStockValuationMvtId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_stk_val_mvt = " + pk[0];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;

        mnPkStockValuationMvtId = 0;

        msSql = "SELECT COALESCE(MAX(id_stk_val_mvt), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkStockValuationMvtId = resultSet.getInt(1);
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
            mnPkStockValuationMvtId = resultSet.getInt("id_stk_val_mvt");
            mtDate = resultSet.getDate("dt_mov");
            mdQuantityMovement = resultSet.getDouble("qty_mov");
            mdCostUnitary = resultSet.getDouble("cost_u");
            mdCost_r = resultSet.getDouble("cost_r");
            mbTemporalPrice = resultSet.getBoolean("b_temp_price");
            mbRevised = resultSet.getBoolean("b_rev");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkStockValuationId = resultSet.getInt("fk_stk_val");
            mnFkStockValuationMvtId_n = resultSet.getInt("fk_stk_val_mvt_n");
            mnFkDiogCategoryId = resultSet.getInt("fk_ct_iog");
            mnFkDiogYearInId_n = resultSet.getInt("fk_diog_year_in_n");
            mnFkDiogDocInId_n = resultSet.getInt("fk_diog_doc_in_n");
            mnFkDiogEntryInId_n = resultSet.getInt("fk_diog_ety_in_n");
            mnFkDpsYearInId_n = resultSet.getInt("fk_dps_year_in_n");
            mnFkDpsDocInId_n = resultSet.getInt("fk_dps_doc_in_n");
            mnFkDpsEntryInId_n = resultSet.getInt("fk_dps_ety_in_n");
            mnFkDiogYearOutId_n = resultSet.getInt("fk_diog_year_out_n");
            mnFkDiogDocOutId_n = resultSet.getInt("fk_diog_doc_out_n");
            mnFkDiogEntryOutId_n = resultSet.getInt("fk_diog_ety_out_n");
            mnFkDpsYearOutId_n = resultSet.getInt("fk_dps_year_out_n");
            mnFkDpsDocOutId_n = resultSet.getInt("fk_dps_doc_out_n");
            mnFkDpsEntryOutId_n = resultSet.getInt("fk_dps_ety_out_n");
            mnFkMaterialRequestId_n = resultSet.getInt("fk_mat_req_n");
            mnFkMaterialRequestEntryId_n = resultSet.getInt("fk_mat_req_ety_n");
            mnFkStockValuationMvtRevisionId_n = resultSet.getInt("fk_stk_val_mvt_rev_n");
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
                    mnPkStockValuationMvtId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    mdQuantityMovement + ", " +
                    mdCostUnitary + ", " +
                    mdCost_r + ", " +
                    (mbTemporalPrice ? 1 : 0) + ", " +
                    (mbRevised ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkStockValuationId + ", " +
                    (mnFkStockValuationMvtId_n > 0 ? mnFkStockValuationMvtId_n : "null") + ", " +
                    mnFkDiogCategoryId + ", " +
                    (mnFkDiogYearInId_n > 0 ? mnFkDiogYearInId_n : "null") + ", " +
                    (mnFkDiogDocInId_n > 0 ? mnFkDiogDocInId_n : "null") + ", " +
                    (mnFkDiogEntryInId_n > 0 ? mnFkDiogEntryInId_n : "null") + ", " +
                    (mnFkDpsYearInId_n > 0 ? mnFkDpsYearInId_n : "null") + ", " +
                    (mnFkDpsDocInId_n > 0 ? mnFkDpsDocInId_n : "null") + ", " +
                    (mnFkDpsEntryInId_n > 0 ? mnFkDpsEntryInId_n : "null") + ", " +
                    (mnFkDiogYearOutId_n > 0 ? mnFkDiogYearOutId_n : "null") + ", " +
                    (mnFkDiogDocOutId_n > 0 ? mnFkDiogDocOutId_n : "null") + ", " +
                    (mnFkDiogEntryOutId_n > 0 ? mnFkDiogEntryOutId_n : "null") + ", " +
                    (mnFkDpsYearOutId_n > 0 ? mnFkDpsYearOutId_n : "null") + ", " +
                    (mnFkDpsDocOutId_n > 0 ? mnFkDpsDocOutId_n : "null") + ", " +
                    (mnFkDpsEntryOutId_n > 0 ? mnFkDpsEntryOutId_n : "null") + ", " +
                    (mnFkMaterialRequestId_n > 0 ? mnFkMaterialRequestId_n : "null") + ", " +
                    (mnFkMaterialRequestEntryId_n > 0 ? mnFkMaterialRequestEntryId_n : "null") + ", " +
                    (mnFkStockValuationMvtRevisionId_n > 0 ? mnFkStockValuationMvtRevisionId_n : "null") + ", " +
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
                    //"id_stk_val_mvt = " + mnPkValuationEntryId + ", " +
                    "dt_mov = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "qty_mov = " + mdQuantityMovement + ", " +
                    "cost_u = " + mdCostUnitary + ", " +
                    "cost_r = " + mdCost_r + ", " +
                    "b_temp_price = " + (mbTemporalPrice ? 1 : 0) + ", " +
                    "b_rev = " + (mbRevised ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_stk_val = " + mnFkStockValuationId + ", " +
                    "fk_stk_val_mvt_n = " + (mnFkStockValuationMvtId_n > 0 ? mnFkStockValuationMvtId_n : "null") + ", " +
                    "fk_ct_iog = " + mnFkDiogCategoryId + ", " +
                    "fk_diog_year_in_n = " + (mnFkDiogYearInId_n > 0 ? mnFkDiogYearInId_n : "null") + ", " +
                    "fk_diog_doc_in_n = " + (mnFkDiogDocInId_n > 0 ? mnFkDiogDocInId_n : "null") + ", " +
                    "fk_diog_ety_in_n = " + (mnFkDiogEntryInId_n > 0 ? mnFkDiogEntryInId_n : "null") + ", " +
                    "fk_dps_year_in_n = " + (mnFkDpsYearInId_n > 0 ? mnFkDpsYearInId_n : "null") + ", " +
                    "fk_dps_doc_in_n = " + (mnFkDpsDocInId_n > 0 ? mnFkDpsDocInId_n : "null") + ", " +
                    "fk_dps_ety_in_n = " + (mnFkDpsEntryInId_n > 0 ? mnFkDpsEntryInId_n : "null") + ", " +
                    "fk_diog_year_out_n = " + (mnFkDiogYearOutId_n > 0 ? mnFkDiogYearOutId_n : "null") + ", " +
                    "fk_diog_doc_out_n = " + (mnFkDiogDocOutId_n > 0 ? mnFkDiogDocOutId_n : "null") + ", " +
                    "fk_diog_ety_out_n = " + (mnFkDiogEntryOutId_n > 0 ? mnFkDiogEntryOutId_n : "null") + ", " +
                    "fk_dps_year_out_n = " + (mnFkDpsYearOutId_n > 0 ? mnFkDpsYearOutId_n : "null") + ", " +
                    "fk_dps_doc_out_n = " + (mnFkDpsDocOutId_n > 0 ? mnFkDpsDocOutId_n : "null") + ", " +
                    "fk_dps_ety_out_n = " + (mnFkDpsEntryOutId_n > 0 ? mnFkDpsEntryOutId_n : "null") + ", " +
                    "fk_mat_req_n = " + (mnFkMaterialRequestId_n > 0 ? mnFkMaterialRequestId_n : "null") + ", " +
                    "fk_mat_req_ety_n = " + (mnFkMaterialRequestEntryId_n > 0 ? mnFkMaterialRequestEntryId_n : "null") + ", " +
                    "fk_stk_val_mvt_rev_n = " + (mnFkStockValuationMvtRevisionId_n > 0 ? mnFkStockValuationMvtRevisionId_n : "null") + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_unit = " + mnFkUnitId + ", " +
                    "fk_lot = " + mnFkLotId + ", " +
                    "fk_cob = " + mnFkCompanyBranchId + ", " +
                    "fk_wh = " + mnFkWarehouseId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " "  + 
                    getSqlWhere();
        }
        
        session.getStatement().getConnection().createStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbStockValuationMvt registry = new SDbStockValuationMvt();
        
        registry.setPkStockValuationMvtId(this.getPkStockValuationMvtId());
        registry.setDateMove(this.getDateMove());
        registry.setQuantityMovement(this.getQuantityMovement());
        registry.setCostUnitary(this.getCostUnitary());
        registry.setCost_r(this.getCost_r());
        registry.setTemporalPrice(this.isTemporalPrice());
        registry.setRevised(this.isRevised());
        registry.setDeleted(this.isDeleted());
        registry.setFkStockValuationId(this.getFkStockValuationId());
        registry.setFkStockValuationMvtId_n(this.getFkStockValuationMvtId_n());
        registry.setFkDiogCategoryId(this.getFkDiogCategoryId());
        registry.setFkDiogYearInId_n(this.getFkDiogYearInId_n());
        registry.setFkDiogDocInId_n(this.getFkDiogDocInId_n());
        registry.setFkDiogEntryInId_n(this.getFkDiogEntryInId_n());
        registry.setFkDpsYearInId_n(this.getFkDpsYearInId_n());
        registry.setFkDpsDocInId_n(this.getFkDpsDocInId_n());
        registry.setFkDpsEntryInId_n(this.getFkDpsEntryInId_n());
        registry.setFkDiogYearOutId_n(this.getFkDiogYearOutId_n());
        registry.setFkDiogDocOutId_n(this.getFkDiogDocOutId_n());
        registry.setFkDiogEntryOutId_n(this.getFkDiogEntryOutId_n());
        registry.setFkDpsYearOutId_n(this.getFkDpsYearOutId_n());
        registry.setFkDpsDocOutId_n(this.getFkDpsDocOutId_n());
        registry.setFkDpsEntryOutId_n(this.getFkDpsEntryOutId_n());
        registry.setFkMaterialRequestId_n(this.getFkMaterialRequestId_n());
        registry.setFkMaterialRequestEntryId_n(this.getFkMaterialRequestEntryId_n());
        registry.setFkStockValuationMvtRevisionId_n(this.getFkStockValuationMvtRevisionId_n());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkLotId(this.getFkLotId());
        registry.setFkCompanyBranchId(this.getFkCompanyBranchId());
        registry.setFkWarehouseId(this.getFkWarehouseId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        registry.setAuxConsumed(this.isAuxConsumed());
        registry.setAuxItemDescription(this.getAuxItemDescription());
        registry.setAuxDiogTypeDescription(this.getAuxDiogTypeDescription());
        registry.setLogMessage(this.getLogMessage());
        registry.setAuxTypeDpsIn(this.getAuxTypeDpsIn());
        registry.setAuxTypeDpsOut(this.getAuxTypeDpsOut());
        registry.setAuxIsAdjust(this.isAuxAdjust());
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkStockValuationMvtId };
    }

    @Override
    public String getRowCode() {
        return String.valueOf(mnPkStockValuationMvtId);
    }

    @Override
    public String getRowName() {
        return SLibUtils.DateFormatDate.format(mtDate) + " - " + msAuxItemDescription + " - " + msAuxDiogTypeDescription;
    }

    @Override
    public boolean isRowSystem() {
        return false; // This registry is not a system one.
    }

    @Override
    public boolean isRowDeletable() {
        return false;
    }

    @Override
    public boolean isRowEdited() {
        return false; // This registry is not editable.
    }

    @Override
    public void setRowEdited(boolean edited) {
        // This registry is not editable, so this method does nothing.
        // If it were editable, it would set the edited flag to true or false.
        // However, this class is not designed to be edited directly in a grid.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = mtDate;
                break;
            case 1:
                value = msAuxItemDescription;
                break;
            case 2:
                value = msAuxDiogTypeDescription;
                break;
            case 3:
                value = msAuxDiogTypeDescription;
                break;
            case 4:
                value = mdQuantityMovement;
                break;
            case 5:
                value = mdCostUnitary;
                break;
            case 6:
                value = mdCost_r;
                break;
            default:
                break;
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        
    }
}
