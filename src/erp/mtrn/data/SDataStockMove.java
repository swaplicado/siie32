/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following classes:
 * - erp.mod.trn.db.SDbInventoryValuationXXX
 * - erp.mtrn.data.SDataDiog
 * All of them also make raw SQL insertions.
 */

/**
 *
 * @author Sergio Flores
 */
public class SDataStockMove extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkLotId;
    protected int mnPkCompanyBranchId;
    protected int mnPkWarehouseId;
    protected int mnPkMoveId;
    protected java.util.Date mtDate;
    protected double mdMoveIn;
    protected double mdMoveOut;
    protected double mdUsed;
    protected double mdCostUnitary;
    protected double mdCost;
    protected double mdDebit;
    protected double mdCredit;
    protected boolean mbIsDeleted;
    protected int mnFkDiogCategoryId;
    protected int mnFkDiogClassId;
    protected int mnFkDiogTypeId;
    protected int mnFkDiogAdjustmentTypeId;
    protected int mnFkDiogYearId;
    protected int mnFkDiogDocId;
    protected int mnFkDiogEntryId;
    protected int mnFkDpsYearId_n;
    protected int mnFkDpsDocId_n;
    protected int mnFkDpsEntryId_n;
    protected int mnFkDpsAdjustmentYearId_n;
    protected int mnFkDpsAdjustmentDocId_n;
    protected int mnFkDpsAdjustmentEntryId_n;
    protected int mnFkMfgYearId_n;
    protected int mnFkMfgOrderId_n;
    protected int mnFkMfgChargeId_n;
    protected int mnFkBookkeepingYearId_n;
    protected int mnFkBookkeepingNumberId_n;

    protected double mdAuxValue;

    public SDataStockMove() {
        super(SDataConstants.TRN_STK);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkLotId(int n) { mnPkLotId = n; }
    public void setPkCompanyBranchId(int n) { mnPkCompanyBranchId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setPkMoveId(int n) { mnPkMoveId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setMoveIn(double d) { mdMoveIn = d; }
    public void setMoveOut(double d) { mdMoveOut = d; }
    public void setUsed(double d) { mdUsed = d; }
    public void setCostUnitary(double d) { mdCostUnitary = d; }
    public void setCost(double d) { mdCost = d; }
    public void setDebit(double d) { mdDebit = d; }
    public void setCredit(double d) { mdCredit = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkDiogCategoryId(int n) { mnFkDiogCategoryId = n; }
    public void setFkDiogClassId(int n) { mnFkDiogClassId = n; }
    public void setFkDiogTypeId(int n) { mnFkDiogTypeId = n; }
    public void setFkDiogAdjustmentTypeId(int n) { mnFkDiogAdjustmentTypeId = n; }
    public void setFkDiogYearId(int n) { mnFkDiogYearId = n; }
    public void setFkDiogDocId(int n) { mnFkDiogDocId = n; }
    public void setFkDiogEntryId(int n) { mnFkDiogEntryId = n; }
    public void setFkDpsYearId_n(int n) { mnFkDpsYearId_n = n; }
    public void setFkDpsDocId_n(int n) { mnFkDpsDocId_n = n; }
    public void setFkDpsEntryId_n(int n) { mnFkDpsEntryId_n = n; }
    public void setFkDpsAdjustmentYearId_n(int n) { mnFkDpsAdjustmentYearId_n = n; }
    public void setFkDpsAdjustmentDocId_n(int n) { mnFkDpsAdjustmentDocId_n = n; }
    public void setFkDpsAdjustmentEntryId_n(int n) { mnFkDpsAdjustmentEntryId_n = n; }
    public void setFkMfgYearId_n(int n) { mnFkMfgYearId_n = n; }
    public void setFkMfgOrderId_n(int n) { mnFkMfgOrderId_n = n; }
    public void setFkMfgChargeId_n(int n) { mnFkMfgChargeId_n = n; }
    public void setFkBookkeepingYearId_n(int n) { mnFkBookkeepingYearId_n = n; }
    public void setFkBookkeepingNumberId_n(int n) { mnFkBookkeepingNumberId_n = n; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkLotId() { return mnPkLotId; }
    public int getPkCompanyBranchId() { return mnPkCompanyBranchId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public int getPkMoveId() { return mnPkMoveId; }
    public java.util.Date getDate() { return mtDate; }
    public double getMoveIn() { return mdMoveIn; }
    public double getMoveOut() { return mdMoveOut; }
    public double getUsed() { return mdUsed; }
    public double getCostUnitary() { return mdCostUnitary; }
    public double getCost() { return mdCost; }
    public double getDebit() { return mdDebit; }
    public double getCredit() { return mdCredit; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkDiogCategoryId() { return mnFkDiogCategoryId; }
    public int getFkDiogClassId() { return mnFkDiogClassId; }
    public int getFkDiogTypeId() { return mnFkDiogTypeId; }
    public int getFkDiogAdjustmentTypeId() { return mnFkDiogAdjustmentTypeId; }
    public int getFkDiogYearId() { return mnFkDiogYearId; }
    public int getFkDiogDocId() { return mnFkDiogDocId; }
    public int getFkDiogEntryId() { return mnFkDiogEntryId; }
    public int getFkDpsYearId_n() { return mnFkDpsYearId_n; }
    public int getFkDpsDocId_n() { return mnFkDpsDocId_n; }
    public int getFkDpsEntryId_n() { return mnFkDpsEntryId_n; }
    public int getFkDpsAdjustmentYearId_n() { return mnFkDpsAdjustmentYearId_n; }
    public int getFkDpsAdjustmentDocId_n() { return mnFkDpsAdjustmentDocId_n; }
    public int getFkDpsAdjustmentEntryId_n() { return mnFkDpsAdjustmentEntryId_n; }
    public int getFkMfgYearId_n() { return mnFkMfgYearId_n; }
    public int getFkMfgOrderId_n() { return mnFkMfgOrderId_n; }
    public int getFkMfgChargeId_n() { return mnFkMfgChargeId_n; }
    public int getFkBookkeepingYearId_n() { return mnFkBookkeepingYearId_n; }
    public int getFkBookkeepingNumberId_n() { return mnFkBookkeepingNumberId_n; }

    public void setAuxValue(double d) { mdAuxValue = d; }

    public double getAuxValue() { return mdAuxValue; }

    public int[] getDiogCategoryKey() { return new int[] { mnFkDiogCategoryId }; }
    public int[] getDiogClassKey() { return new int[] { mnFkDiogCategoryId, mnFkDiogClassId }; }
    public int[] getDiogTypeKey() { return new int[] { mnFkDiogCategoryId, mnFkDiogClassId, mnFkDiogTypeId }; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkItemId = ((int[]) pk)[1];
        mnPkUnitId = ((int[]) pk)[2];
        mnPkLotId = ((int[]) pk)[3];
        mnPkCompanyBranchId = ((int[]) pk)[4];
        mnPkWarehouseId = ((int[]) pk)[5];
        mnPkMoveId = ((int[]) pk)[6];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkItemId, mnPkUnitId, mnPkLotId, mnPkCompanyBranchId, mnPkWarehouseId, mnPkMoveId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkItemId = 0;
        mnPkUnitId = 0;
        mnPkLotId = 0;
        mnPkCompanyBranchId = 0;
        mnPkWarehouseId = 0;
        mnPkMoveId = 0;
        mtDate = null;
        mdMoveIn = 0;
        mdMoveOut = 0;
        mdUsed = 0;
        mdCostUnitary = 0;
        mdCost = 0;
        mdDebit = 0;
        mdCredit = 0;
        mbIsDeleted = false;
        mnFkDiogCategoryId = 0;
        mnFkDiogClassId = 0;
        mnFkDiogTypeId = 0;
        mnFkDiogAdjustmentTypeId = 0;
        mnFkDiogYearId = 0;
        mnFkDiogDocId = 0;
        mnFkDiogEntryId = 0;
        mnFkDpsYearId_n = 0;
        mnFkDpsDocId_n = 0;
        mnFkDpsEntryId_n = 0;
        mnFkDpsAdjustmentYearId_n = 0;
        mnFkDpsAdjustmentDocId_n = 0;
        mnFkDpsAdjustmentEntryId_n = 0;
        mnFkMfgYearId_n = 0;
        mnFkMfgOrderId_n = 0;
        mnFkMfgChargeId_n = 0;
        mnFkBookkeepingYearId_n = 0;
        mnFkBookkeepingNumberId_n = 0;

        mdAuxValue = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_stk " +
                    "WHERE id_year = " + key[0] + " AND id_item = " + key[1] + " AND id_unit = " + key[2] + " AND id_lot = " + key[3] + " AND " +
                    "id_cob = " + key[4] +  " AND id_wh = " + key[5] +  " AND id_mov = " + key[6] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkItemId = resultSet.getInt("id_item");
                mnPkUnitId = resultSet.getInt("id_unit");
                mnPkLotId = resultSet.getInt("id_lot");
                mnPkCompanyBranchId = resultSet.getInt("id_cob");
                mnPkWarehouseId = resultSet.getInt("id_wh");
                mnPkMoveId = resultSet.getInt("id_mov");
                mtDate = resultSet.getDate("dt");
                mdMoveIn = resultSet.getDouble("mov_in");
                mdMoveOut = resultSet.getDouble("mov_out");
                mdUsed = resultSet.getDouble("used");
                mdCostUnitary = resultSet.getDouble("cost_u");
                mdCost = resultSet.getDouble("cost");
                mdDebit = resultSet.getDouble("debit");
                mdCredit = resultSet.getDouble("credit");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkDiogCategoryId = resultSet.getInt("fid_ct_iog");
                mnFkDiogClassId = resultSet.getInt("fid_cl_iog");
                mnFkDiogTypeId = resultSet.getInt("fid_tp_iog");
                mnFkDiogAdjustmentTypeId = resultSet.getInt("fid_tp_iog_adj");
                mnFkDiogYearId = resultSet.getInt("fid_diog_year");
                mnFkDiogDocId = resultSet.getInt("fid_diog_doc");
                mnFkDiogEntryId = resultSet.getInt("fid_diog_ety");
                mnFkDpsYearId_n = resultSet.getInt("fid_dps_year_n");
                mnFkDpsDocId_n = resultSet.getInt("fid_dps_doc_n");
                mnFkDpsEntryId_n = resultSet.getInt("fid_dps_ety_n");
                mnFkDpsAdjustmentYearId_n = resultSet.getInt("fid_dps_adj_year_n");
                mnFkDpsAdjustmentDocId_n = resultSet.getInt("fid_dps_adj_doc_n");
                mnFkDpsAdjustmentEntryId_n = resultSet.getInt("fid_dps_adj_ety_n");
                mnFkMfgYearId_n = resultSet.getInt("fid_mfg_year_n");
                mnFkMfgOrderId_n = resultSet.getInt("fid_mfg_ord_n");
                mnFkMfgChargeId_n = resultSet.getInt("fid_mfg_chg_n");
                mnFkBookkeepingYearId_n = resultSet.getInt("fid_bkk_year_n");
                mnFkBookkeepingNumberId_n = resultSet.getInt("fid_bkk_num_n");

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

        /*
         * NOTE ON STORE PROCEDURE 'trn_stk_save':
         * Before every stock registry is saved, there is a verification to check:
         * 1. If default lot needs to be created.
         * 2. If stock configuration needs to be created.
         */

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL trn_stk_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkItemId);
            callableStatement.setInt(nParam++, mnPkUnitId);
            callableStatement.setInt(nParam++, mnPkLotId);
            callableStatement.setInt(nParam++, mnPkCompanyBranchId);
            callableStatement.setInt(nParam++, mnPkWarehouseId);
            callableStatement.setInt(nParam++, mnPkMoveId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDate.getTime()));
            callableStatement.setDouble(nParam++, mdMoveIn);
            callableStatement.setDouble(nParam++, mdMoveOut);
            callableStatement.setDouble(nParam++, mdUsed);
            callableStatement.setDouble(nParam++, mdCostUnitary);
            callableStatement.setDouble(nParam++, mdCost);
            callableStatement.setDouble(nParam++, mdDebit);
            callableStatement.setDouble(nParam++, mdCredit);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkDiogCategoryId);
            callableStatement.setInt(nParam++, mnFkDiogClassId);
            callableStatement.setInt(nParam++, mnFkDiogTypeId);
            callableStatement.setInt(nParam++, mnFkDiogAdjustmentTypeId);
            callableStatement.setInt(nParam++, mnFkDiogYearId);
            callableStatement.setInt(nParam++, mnFkDiogDocId);
            callableStatement.setInt(nParam++, mnFkDiogEntryId);
            if (mnFkDpsYearId_n != SLibConstants.UNDEFINED) {
                callableStatement.setInt(nParam++, mnFkDpsYearId_n);
                callableStatement.setInt(nParam++, mnFkDpsDocId_n);
                callableStatement.setInt(nParam++, mnFkDpsEntryId_n);
            }
            else {
                callableStatement.setNull(nParam++, Types.SMALLINT);
                callableStatement.setNull(nParam++, Types.INTEGER);
                callableStatement.setNull(nParam++, Types.INTEGER);
            }
            if (mnFkDpsAdjustmentYearId_n != SLibConstants.UNDEFINED) {
                callableStatement.setInt(nParam++, mnFkDpsAdjustmentYearId_n);
                callableStatement.setInt(nParam++, mnFkDpsAdjustmentDocId_n);
                callableStatement.setInt(nParam++, mnFkDpsAdjustmentEntryId_n);
            }
            else {
                callableStatement.setNull(nParam++, Types.SMALLINT);
                callableStatement.setNull(nParam++, Types.INTEGER);
                callableStatement.setNull(nParam++, Types.INTEGER);
            }
            if (mnFkMfgYearId_n != SLibConstants.UNDEFINED) {
                callableStatement.setInt(nParam++, mnFkMfgYearId_n);
                callableStatement.setInt(nParam++, mnFkMfgOrderId_n);
                callableStatement.setInt(nParam++, mnFkMfgChargeId_n);
            }
            else {
                callableStatement.setNull(nParam++, Types.SMALLINT);
                callableStatement.setNull(nParam++, Types.INTEGER);
                callableStatement.setNull(nParam++, Types.INTEGER);
            }
            if (mnFkBookkeepingYearId_n != SLibConstants.UNDEFINED) {
                callableStatement.setInt(nParam++, mnFkBookkeepingYearId_n);
                callableStatement.setInt(nParam++, mnFkBookkeepingNumberId_n);
            }
            else {
                callableStatement.setNull(nParam++, Types.SMALLINT);
                callableStatement.setNull(nParam++, Types.INTEGER);
            }
            callableStatement.registerOutParameter(nParam++, Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, Types.VARCHAR);
            callableStatement.execute();

            mnPkMoveId = callableStatement.getInt(nParam - 3);
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
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return null;
    }

    public void computeValue() {
        if (!SLibUtilities.belongsTo(getDiogClassKey(), new int[][] { SDataConstantsSys.TRNS_CL_IOG_IN_EXP, SDataConstantsSys.TRNS_CL_IOG_OUT_EXP })) {
            // Ordinary stock move:

            if (mnFkDiogCategoryId == SDataConstantsSys.TRNS_CT_IOG_IN) {
                if (mdCostUnitary != 0.0 && SLibUtilities.round(mdDebit, 2) != SLibUtilities.round(mdMoveIn * mdCostUnitary, 2)) {
                    mdDebit = SLibUtilities.round(mdMoveIn * mdCostUnitary, 2);
                }
                mdCredit = 0;
            }
            else {
                mdDebit = 0;
                if (mdCostUnitary != 0.0 && SLibUtilities.round(mdCredit, 2) != SLibUtilities.round(mdMoveOut * mdCostUnitary, 2)) {
                    mdCredit = SLibUtilities.round(mdMoveOut * mdCostUnitary, 2);
                }
            }
        }
        else {
            // Special stock move for expenses:

            if (mnFkDiogCategoryId == SDataConstantsSys.TRNS_CT_IOG_IN) {
                mdDebit = mdAuxValue;
                mdCredit = 0;
            }
            else {
                mdDebit = 0;
                mdCredit = mdAuxValue;
            }
        }
    }
}
