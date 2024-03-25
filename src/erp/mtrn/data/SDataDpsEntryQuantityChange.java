/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Isabel Servin
 */
public class SDataDpsEntryQuantityChange extends SDataRegistry {
    
    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected int mnPkChangeId;
    protected java.util.Date mtDate;
    protected double mdQuantityOld;
    protected double mdQuantityNew;
    protected double mdOriginalQuantityOld;
    protected double mdOriginalQuantityNew;
    protected boolean mbIsDeleted;
    protected int mnFkUnitOldId;
    protected int mnFkUnitNewId;
    protected int mnFkOriginalUnitOldId;
    protected int mnFkOriginalUnitNewId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataDpsEntryQuantityChange() {
        super(SDataConstants.TRN_DPS_ETY_QTY_CHG);
        reset();
    }
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setPkChangeId(int n) { mnPkChangeId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setQuantityOld(double d) { mdQuantityOld = d; }
    public void setQuantityNew(double d) { mdQuantityNew = d; }
    public void setOriginalQuantityOld(double d) { mdOriginalQuantityOld = d; }
    public void setOriginalQuantityNew(double d) { mdOriginalQuantityNew = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUnitOldId(int n) { mnFkUnitOldId = n; }
    public void setFkUnitNewId(int n) { mnFkUnitNewId = n; }
    public void setFkOriginalUnitOldId(int n) { mnFkOriginalUnitOldId = n; }
    public void setFkOriginalUnitNewId(int n) { mnFkOriginalUnitNewId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getPkChangeId() { return mnPkChangeId; }
    public java.util.Date getDate() { return mtDate; }
    public double getQuantityOld() { return mdQuantityOld; }
    public double getQuantityNew() { return mdQuantityNew; }
    public double getOriginalQuantityOld() { return mdOriginalQuantityOld; }
    public double getOriginalQuantityNew() { return mdOriginalQuantityNew; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUnitOldId() { return mnFkUnitOldId; }
    public int getFkUnitNewId() { return mnFkUnitNewId; }
    public int getFkOriginalUnitOldId() { return mnFkOriginalUnitOldId; }
    public int getFkOriginalUnitNewId() { return mnFkOriginalUnitNewId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkEntryId = ((int[]) pk)[2];
        mnPkChangeId = ((int[]) pk)[3];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId, mnPkChangeId }; 
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        mnPkChangeId = 0;
        mtDate = null;
        mdQuantityOld = 0;
        mdQuantityNew = 0;
        mdOriginalQuantityOld = 0;
        mdOriginalQuantityNew = 0;
        mbIsDeleted = false;
        mnFkUnitOldId = 0;
        mnFkUnitNewId = 0;
        mnFkOriginalUnitOldId = 0;
        mnFkOriginalUnitNewId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT * FROM trn_dps_ety_qty_chg " +
                    "WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " AND id_ety = " + key[2] + " AND id_chg = " + key[3] + " ";
            
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                mnPkEntryId = resultSet.getInt("id_ety");
                mnPkChangeId = resultSet.getInt("id_chg");
                mtDate = resultSet.getDate("dt");
                mdQuantityOld = resultSet.getDouble("qty_old");
                mdQuantityNew = resultSet.getDouble("qty_new");
                mdOriginalQuantityOld = resultSet.getDouble("orig_qty_old");
                mdOriginalQuantityNew = resultSet.getDouble("orig_qty_new");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUnitOldId = resultSet.getInt("fid_unit_old");
                mnFkUnitNewId = resultSet.getInt("fid_unit_new");
                mnFkOriginalUnitOldId = resultSet.getInt("fid_orig_unit_old");
                mnFkOriginalUnitNewId = resultSet.getInt("fid_orig_unit_new");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        } 
        
        return mnLastDbActionResult;
    }

    @Override
    public int save(Connection connection) {
        String sql;
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            if (mbIsRegistryNew) {
                sql = "SELECT COALESCE(MAX(id_chg) + 1, 1) AS new_id "
                        + "FROM trn_dps_ety_qty_chg "
                        + "WHERE id_year = " + mnPkYearId + " "
                        + "AND id_doc = " + mnPkDocId + " "
                        + "AND id_ety = " + mnPkEntryId;
                ResultSet resultSet = connection.createStatement().executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
                }
                else {
                    mnPkChangeId = resultSet.getInt("new_id");
                    mnFkUserEditId = SUtilConsts.USR_NA_ID;
                    mnFkUserDeleteId = SUtilConsts.USR_NA_ID;
                    sql = "INSERT INTO trn_dps_ety_qty_chg VALUES (" +
                            mnPkYearId + ", " +
                            mnPkDocId + ", " +
                            mnPkEntryId + ", " +
                            mnPkChangeId + ", " +
                            "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                            mdQuantityOld + ", " +
                            mdQuantityNew + ", " +
                            mdOriginalQuantityOld + ", " +
                            mdOriginalQuantityNew + ", " +
                            (mbIsDeleted ? 1 : 0) + ", " + 
                            mnFkUnitOldId + ", " +
                            mnFkUnitNewId + ", " +
                            mnFkOriginalUnitOldId + ", " +
                            mnFkOriginalUnitNewId + ", " +
                            mnFkUserNewId + ", " +
                            mnFkUserEditId + ", " +
                            mnFkUserDeleteId + ", " +
                            "NOW(), " +
                            "NOW(), " +
                            "NOW() " +
                            ");";
                }
            }
            else {
                sql = "UPDATE trn_dps_ety_qty_chg SET " +
                        //"id_year = " + mnPkYearId + ", " +
                        //"id_doc = " + mnPkDocId + ", " +
                        //"id_ety = " + mnPkEntryId + ", " +
                        //"id_chg = " + mnPkChangeId + ", " +
                        "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                        "qty_old = " + mdQuantityOld + ", " +
                        "qty_new = " + mdQuantityNew + ", " +
                        "orig_qty_old = " + mdOriginalQuantityOld + ", " +
                        "orig_qty_new = " + mdOriginalQuantityNew + ", " +
                        "b_del = " + (mbIsDeleted ? 1 : 0) + ", " +
                        "fid_unit_old = " + mnFkUnitOldId + ", " +
                        "fid_unit_new = " + mnFkUnitNewId + ", " +
                        "fid_orig_unit_old = " + mnFkOriginalUnitOldId + ", " +
                        "fid_orig_unit_new = " + mnFkOriginalUnitNewId + ", " +
                        //"fid_usr_new = " + mnFkUserNewId + ", " +
                        "fid_usr_edit = " + mnFkUserEditId + ", " +
                        "fid_usr_del = " + mnFkUserDeleteId + ", " +
                        //"ts_new = '" + SLibUtils.DbmsDateFormatDate.format(mtUserNewTs) + "', " +
                        "ts_edit = '" + SLibUtils.DbmsDateFormatDate.format(mtUserEditTs) + "', " +
                        "ts_del = '" + SLibUtils.DbmsDateFormatDate.format(mtUserDeleteTs) + "' " +
                        "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + " AND id_chg = " + mnPkChangeId;
            }
            
            connection.createStatement().execute(sql);
            
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return new Date();
    }
}
