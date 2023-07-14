package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;

/**
 *
 * @author Edwin Carmona
 */
public class SDataStockSegregation extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    protected int mnPkStockSegregationId;
    protected Date mtExpirationDate_n;
    protected boolean mbDeleted;
    protected int mnFkStockSegregationTypeId;
    protected int mnFkReference1Id;
    protected int mnFkReference2Id_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected ArrayList<SDataStockSegregationWarehouse> maChildEntries;
    
    public SDataStockSegregation() {
        super(SDataConstants.TRN_STK_SEG);
        maChildEntries = new ArrayList<>();
        reset();
    }

    public void setPkStockSegregationId(int n) { mnPkStockSegregationId = n; }
    public void setExpirationDate_n(Date t) { mtExpirationDate_n = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkStockSegregationTypeId(int n) { mnFkStockSegregationTypeId = n; }
    public void setFkReference1Id(int n) { mnFkReference1Id = n; }
    public void setFkReference2Id_n(int n) { mnFkReference2Id_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkStockSegregationId() { return mnPkStockSegregationId; }
    public Date getExpirationDate_n() { return mtExpirationDate_n; }
    public boolean getDeleted() { return mbDeleted; }
    public int getFkStockSegregationTypeId() { return mnFkStockSegregationTypeId; }
    public int getFkReference1Id() { return mnFkReference1Id; }
    public int getFkReference2Id_n() { return mnFkReference2Id_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public ArrayList<SDataStockSegregationWarehouse> getChildEntries() { return maChildEntries; }
    
    private ArrayList<SDataStockSegregationWarehouse> readEntries(final Statement statement) throws Exception {
        String sql;
        ResultSet resultSet = null;
        ArrayList<SDataStockSegregationWarehouse> entries = new ArrayList<>();

        sql = "SELECT id_cob, id_whs "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_SEG_WHS) + " " 
                + getSqlWhere()
                + "ORDER BY id_cob, id_whs;";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            SDataStockSegregationWarehouse segregationWarehouse = new SDataStockSegregationWarehouse();
            segregationWarehouse.read(new int[] { mnPkStockSegregationId, resultSet.getInt("id_cob"), resultSet.getInt("id_whs") }, statement.getConnection().createStatement());
            entries.add(segregationWarehouse);
        }
        
        return entries;
    }
    
    private void deleteEntries(final Connection connection) throws Exception {
        if (maChildEntries != null && !maChildEntries.isEmpty()) {
            for (SDataStockSegregationWarehouse segregationWahrehouse : maChildEntries) {
                segregationWahrehouse.setPkStockSegregationId(mnPkStockSegregationId);
                segregationWahrehouse.delete(connection);
            }
        }
    }

    public String getSqlWhere() {
        return "WHERE id_stk_seg = " + mnPkStockSegregationId + " ";
    }

    public String getSqlWhere(int[] pk) {
        return "WHERE id_stk_seg = " + pk[0] + " ";
    }

    public void computeNewPrimaryKey(Connection connection) throws SQLException, Exception {
        ResultSet resultSet = null;
        String sql;
        
        mnPkStockSegregationId = 0;

        sql = "SELECT COALESCE(MAX(id_stk_seg), 0) + 1 FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_SEG);
        resultSet = connection.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            mnPkStockSegregationId = resultSet.getInt(1);
        }
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockSegregationId };
    }
    
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkStockSegregationId = ((int []) pk)[0];
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkStockSegregationId = 0;
        mtExpirationDate_n = null;
        mbDeleted = false;
        mnFkStockSegregationTypeId = 0;
        mnFkReference1Id = 0;
        mnFkReference2Id_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        maChildEntries.clear();
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_SEG) + " " + getSqlWhere(key);
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                mnPkStockSegregationId = resultSet.getInt("id_stk_seg");
                mtExpirationDate_n = resultSet.getDate("dt_exp_n");
                mbDeleted = resultSet.getBoolean("b_del");
                mnFkStockSegregationTypeId = resultSet.getInt("fid_tp_stk_seg");
                mnFkReference1Id = resultSet.getInt("fid_ref_1");
                mnFkReference2Id_n = resultSet.getInt("fid_ref_2_n");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");


                // Read aswell child registries:
                maChildEntries.addAll(readEntries(statement.getConnection().createStatement()));
                
                // Finish registry reading:
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
    public int save(Connection connection) {
        String sql;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            if (mnPkStockSegregationId == 0) {
                computeNewPrimaryKey(connection);
                mbDeleted = false;
                mnFkUserEditId = SUtilConsts.USR_NA_ID;

                sql = "INSERT INTO " + SModConsts.TablesMap.get(SModConsts.TRN_STK_SEG) + " VALUES (" +
                        mnPkStockSegregationId + ", " + 
                        (mtExpirationDate_n != null ? ("'" + SLibUtils.DbmsDateFormatDate.format(mtExpirationDate_n) + "'") : ("null")) + ", " + 
                        (mbDeleted ? 1 : 0) + ", " + 
                        mnFkStockSegregationTypeId + ", " + 
                        mnFkReference1Id + ", " + 
                        (mnFkReference2Id_n == 0 ? "null" : mnFkReference2Id_n) + ", " + 
                        (mnFkUserNewId == 0 ? 1 : mnFkUserNewId) + ", " + 
                        (mnFkUserEditId == 0 ? 1 : mnFkUserEditId) + ", " + 
                        (mnFkUserDeleteId == 0 ? 1 : mnFkUserDeleteId) + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + ", " +
                        "NOW()" +
                        ")";
                
            }
            else {
                sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK_SEG) + " SET " +
                        "id_stk_seg = " + mnPkStockSegregationId + ", " +
                        "dt_exp_n = " + (mtExpirationDate_n != null ? ("'" + SLibUtils.DbmsDateFormatDate.format(mtExpirationDate_n) + "'") : ("null")) + ", " + 
                        "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                        "fid_tp_stk_seg = " + mnFkStockSegregationTypeId + ", " +
                        "fid_ref_1 = " + mnFkReference1Id + ", " +
                        "fid_ref_2_n = " + (mnFkReference2Id_n == 0 ? "null" : mnFkReference2Id_n) + ", " +
                        "fid_usr_edit = " + (mnFkUserEditId == 0 ? 1 : mnFkUserEditId) + ", " +
                        "ts_edit = " + "NOW()";
                
                        if (mbDeleted) {
                           sql += ", fid_usr_del = " + (mnFkUserDeleteId == 0 ? 1 : mnFkUserDeleteId) + ", " +
                            "ts_del = " + "NOW() " ;
                        }
                        
                        sql += getSqlWhere();
            }

            connection.createStatement().execute(sql);
            this.deleteEntries(connection);

            // d) save new child registries:
            if (!mbDeleted) {
                for (SDataStockSegregationWarehouse child : maChildEntries) {
                    child.setPkStockSegregationId(mnPkStockSegregationId);
                    child.setIsRegistryNew(true);
                    child.save(connection);
                }
            }

            // Finish registry saving:
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}
