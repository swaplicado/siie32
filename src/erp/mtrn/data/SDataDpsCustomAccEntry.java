/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.sql.Statement;
import sa.gui.util.SUtilConsts;

/**
 * Esta clase permite la definición de partidas de contabilización personalizadas para DPS.
 * @author Sergio Flores
 */
public class SDataDpsCustomAccEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkAccEntryId;
    protected java.lang.String msConcept;
    protected double mdQuantity;
    protected boolean mbIsSubtotalPctApplying;
    protected double mdSubtotalPct;
    protected double mdSubtotal;
    protected double mdSubtotalCy;
    protected boolean mbIsDeleted;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected java.lang.String msFkCostCenterId_n;
    protected int mnFkItemRefId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;
    
    protected java.lang.String msDbmsItemKey;
    protected java.lang.String msDbmsItem;
    protected java.lang.String msDbmsUnitSymbol;
    protected java.lang.String msDbmsCostCenter_n;
    protected java.lang.String msDbmsItemRefKey_n;
    protected java.lang.String msDbmsItemRef_n;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;

    protected boolean mbOldIsDeleted;

    public SDataDpsCustomAccEntry() {
        super(SDataConstants.TRN_DPS_CUSTOM_ACC_ETY);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkAccEntryId(int n) { mnPkAccEntryId = n; }
    public void setConcept(java.lang.String s) { msConcept = s; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setIsSubtotalPctApplying(boolean b) { mbIsSubtotalPctApplying = b; }
    public void setSubtotalPct(double d) { mdSubtotalPct = d; }
    public void setSubtotal(double d) { mdSubtotal = d; }
    public void setSubtotalCy(double d) { mdSubtotalCy = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkCostCenterId_n(java.lang.String s) { msFkCostCenterId_n = s; }
    public void setFkItemRefId_n(int n) { mnFkItemRefId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkAccEntryId() { return mnPkAccEntryId; }
    public java.lang.String getConcept() { return msConcept; }
    public double getQuantity() { return mdQuantity; }
    public boolean getIsSubtotalPctApplying() { return mbIsSubtotalPctApplying; }
    public double getSubtotalPct() { return mdSubtotalPct; }
    public double getSubtotal() { return mdSubtotal; }
    public double getSubtotalCy() { return mdSubtotalCy; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public java.lang.String getFkCostCenterId_n() { return msFkCostCenterId_n; }
    public int getFkItemRefId_n() { return mnFkItemRefId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    public void setDbmsItemKey(java.lang.String s) { msDbmsItemKey = s; }
    public void setDbmsItem(java.lang.String s) { msDbmsItem = s; }
    public void setDbmsUnitSymbol(java.lang.String s) { msDbmsUnitSymbol = s; }
    public void setDbmsCostCenter_n(java.lang.String s) { msDbmsCostCenter_n = s; }
    public void setDbmsItemRefKey_n(java.lang.String s) { msDbmsItemRefKey_n = s; }
    public void setDbmsItemRef_n(java.lang.String s) { msDbmsItemRef_n = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public java.lang.String getDbmsItemKey() { return msDbmsItemKey; }
    public java.lang.String getDbmsItem() { return msDbmsItem; }
    public java.lang.String getDbmsUnitSymbol() { return msDbmsUnitSymbol; }
    public java.lang.String getDbmsCostCenter_n() { return msDbmsCostCenter_n; }
    public java.lang.String getDbmsItemRefKey_n() { return msDbmsItemRefKey_n; }
    public java.lang.String getDbmsItemRef_n() { return msDbmsItemRef_n; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }

    public void setOldIsDeleted(boolean b) { mbOldIsDeleted = b; }
    
    public boolean getOldIsDeleted() { return mbOldIsDeleted; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkAccEntryId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkAccEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkAccEntryId = 0;
        msConcept = "";
        mdQuantity = 0;
        mbIsSubtotalPctApplying = false;
        mdSubtotalPct = 0;
        mdSubtotal = 0;
        mdSubtotalCy = 0;
        mbIsDeleted = false;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        msFkCostCenterId_n = "";
        mnFkItemRefId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
        
        msDbmsItemKey = "";
        msDbmsItem = "";
        msDbmsUnitSymbol = "";
        msDbmsCostCenter_n = "";
        msDbmsItemRefKey_n = "";
        msDbmsItemRef_n = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";

        mbOldIsDeleted = false;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT e.*, "
                    + "i.item_key, i.item, u.symbol, "
                    + "un.usr, ue.usr, ud.usr, "
                    + "ir.item_key, ir.item, cc.cc "
                    + "FROM trn_dps_custom_acc_ety AS e "
                    + "INNER JOIN erp.itmu_item AS i ON e.fid_item = i.id_item "
                    + "INNER JOIN erp.itmu_unit AS u ON e.fid_unit = u.id_unit "
                    + "INNER JOIN erp.usru_usr AS un ON e.fid_usr_new = un.id_usr "
                    + "INNER JOIN erp.usru_usr AS ue ON e.fid_usr_new = ue.id_usr "
                    + "INNER JOIN erp.usru_usr AS ud ON e.fid_usr_new = ud.id_usr "
                    + "LEFT OUTER JOIN fin_cc AS cc ON e.fid_cc_n = cc.id_cc "
                    + "LEFT OUTER JOIN erp.itmu_item AS ir ON e.fid_item_ref_n = ir.id_item "
                    + "WHERE e.id_year = " + key[0] + " AND e.id_doc = " + key[1] + " AND e.id_acc_ety = " + key[2] +  " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("e.id_year");
                mnPkDocId = resultSet.getInt("e.id_doc");
                mnPkAccEntryId = resultSet.getInt("e.id_acc_ety");
                msConcept = resultSet.getString("e.concept");
                mdQuantity = resultSet.getDouble("e.qty");
                mbIsSubtotalPctApplying = resultSet.getBoolean("e.b_stot_per");
                mdSubtotalPct = resultSet.getDouble("e.stot_per");
                mdSubtotal = resultSet.getDouble("e.stot");
                mdSubtotalCy = resultSet.getDouble("e.stot_cur");
                mbIsDeleted = resultSet.getBoolean("e.b_del");
                mnFkItemId = resultSet.getInt("e.fid_item");
                mnFkUnitId = resultSet.getInt("e.fid_unit");
                msFkCostCenterId_n = resultSet.getString("e.fid_cc_n");
                if (resultSet.wasNull()) {
                    msFkCostCenterId_n = "";
                }
                mnFkItemRefId_n = resultSet.getInt("e.fid_item_ref_n");
                if (resultSet.wasNull()) {
                    mnFkItemRefId_n = 0; // just for consistence, already would be equal to zero
                }
                mnFkUserNewId = resultSet.getInt("e.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("e.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("e.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("e.ts_new");
                mtUserEditTs = resultSet.getTimestamp("e.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("e.ts_del");

                msDbmsItemKey = resultSet.getString("i.item_key");
                msDbmsItem = resultSet.getString("i.item");
                msDbmsUnitSymbol = resultSet.getString("u.symbol");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");
                
                msDbmsCostCenter_n = resultSet.getString("cc.cc");
                if (resultSet.wasNull()) {
                    msDbmsCostCenter_n = "";
                }
                
                msDbmsItemRefKey_n = resultSet.getString("ir.item_key");
                if (resultSet.wasNull()) {
                    msDbmsItemRefKey_n = "";
                }
                
                msDbmsItemRef_n = resultSet.getString("ir.item");
                if (resultSet.wasNull()) {
                    msDbmsItemRef_n = "";
                }
                
                mbOldIsDeleted = mbIsDeleted;

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
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            try (Statement statement = connection.createStatement()) {
                String sql;
                
                if (mnPkAccEntryId == 0) {
                    // get next entry ID:
                    
                    sql = "SELECT COALESCE(MAX(id_acc_ety), 0) + 1 "
                            + "FROM trn_dps_custom_acc_ety "
                            + "WHERE id_year = " + mnPkYearId + " "
                            + "AND id_doc = " + mnPkDocId + ";";
                    ResultSet resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        mnPkAccEntryId = resultSet.getInt(1);
                    }
                    
                    // insert new registry:
                    mnFkUserEditId = SUtilConsts.USR_NA_ID;
                    mnFkUserDeleteId = SUtilConsts.USR_NA_ID;
                    
                    sql = "INSERT INTO trn_dps_custom_acc_ety VALUES ("
                            + mnPkYearId + ", "
                            + mnPkDocId + ", "
                            + mnPkAccEntryId + ", "
                            + "'" + msConcept + "', "
                            + mdQuantity + ", "
                            + mbIsSubtotalPctApplying + ", "
                            + mdSubtotalPct + ", "
                            + mdSubtotal + ", "
                            + mdSubtotalCy + ", "
                            + mbIsDeleted + ", "
                            + mnFkItemId + ", "
                            + mnFkUnitId + ", "
                            + (msFkCostCenterId_n.isEmpty() ? "NULL" : "'" + msFkCostCenterId_n + "'") + ", "
                            + (mnFkItemRefId_n == 0 ? "NULL" : "'" + mnFkItemRefId_n + "'") + ", "
                            + mnFkUserNewId + ", "
                            + mnFkUserEditId + ", "
                            + mnFkUserDeleteId + ", "
                            + "NOW(), "
                            + "NOW(), "
                            + "NOW() "
                            + ");";
                }
                else {
                    sql = "UPDATE trn_dps_custom_acc_ety SET "
                            //+ "id_year = " + mnPkYearId + ", "
                            //+ "id_doc = " + mnPkDocId + ", "
                            //+ "id_acc_ety = " + mnPkAccEntryId + ", "
                            + "concept = " + "'" + msConcept + "', "
                            + "qty = " + mdQuantity + ", "
                            + "b_stot_per = " + mbIsSubtotalPctApplying + ", "
                            + "stot_per = " + mdSubtotalPct + ", "
                            + "stot = " + mdSubtotal + ", "
                            + "stot_cur = " + mdSubtotalCy + ", "
                            + "b_del = " + mbIsDeleted + ", "
                            + "fid_item = " + mnFkItemId + ", "
                            + "fid_unit = " + mnFkUnitId + ", "
                            + "fid_cc_n = " + (msFkCostCenterId_n.isEmpty() ? "NULL" : "'" + msFkCostCenterId_n + "'") + ", "
                            + "fid_item_ref_n = " + (mnFkItemRefId_n == 0 ? "NULL" : "'" + mnFkItemRefId_n + "'") + ", "
                            //+ "fid_usr_new = " + mnFkUserNewId + ", "
                            + "fid_usr_edit = " + mnFkUserEditId + ", "
                            + (mbIsDeleted == mbOldIsDeleted ? "" : "fid_usr_del = " + mnFkUserDeleteId + ", ")
                            //+ "ts_new = " + "NOW(), "
                            + "ts_edit = " + "NOW(), "
                            + (mbIsDeleted == mbOldIsDeleted ? "" : "ts_del = " + "NOW() ")
                            + "WHERE id_year = " + mnPkYearId + " "
                            + "AND id_doc = " + mnPkDocId + " "
                            + "AND id_acc_ety = " + mnPkAccEntryId + ";";
                }
                
                statement.execute(sql);
            }
            
            mnDbmsErrorId = 0;
            msDbmsError = "";

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
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
