/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.util.Date;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataBomSubstitute extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    protected int mnPkBomId;
    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkBomSubstituteId;
    protected double mdPercentage;
    protected double mdPercentageMax;
    protected boolean mbIsDeleted;    
    protected int mnFkItemSubstituteId;
    protected int mnFkUnitSubstituteId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsItemSubstitute;
    protected java.lang.String msDbmsItemSubstituteKey;
    protected java.lang.String msDbmsUnitSubstitute;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;

    public SDataBomSubstitute() {
        super(SDataConstants.MFG_BOM_SUB);
        reset();
    }
    
    public void setPkBomId(int n) { mnPkBomId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkBomSubstituteId(int n) { mnPkBomSubstituteId = n; }
    public void setPercentage(double d) { mdPercentage = d; }
    public void setPercentageMax(double d) { mdPercentageMax = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }    
    public void setFkItemSubstituteId(int n) { mnFkItemSubstituteId = n; }
    public void setFkUnitSubstituteId(int n) { mnFkUnitSubstituteId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkBomId() { return mnPkBomId; }
    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkBomSubstituteId() { return mnPkBomSubstituteId; }
    public double getPercentage() { return mdPercentage; }
    public double getPercentageMax() { return mdPercentageMax; }
    public boolean getIsDeleted() { return mbIsDeleted; }    
    public int getFkItemSubstituteId() { return mnFkItemSubstituteId; }
    public int getFkUnitSubstituteId() { return mnFkUnitSubstituteId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsItemSubstitute(java.lang.String s) { msDbmsItemSubstitute = s; }
    public void setDbmsItemSubstituteKey(java.lang.String s) { msDbmsItemSubstituteKey = s; }
    public void setDbmsUnitSubstitute(java.lang.String s) { msDbmsUnitSubstitute = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public java.lang.String getDbmsItemSubstitute() { return msDbmsItemSubstitute; }
    public java.lang.String getDbmsItemSubstituteKey() { return msDbmsItemSubstituteKey; }
    public java.lang.String getDbmsUnitSubstitute() { return msDbmsUnitSubstitute; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkBomId = 0;
        mnPkItemId = 0;
        mnPkUnitId = 0;
        mnPkBomSubstituteId = 0;
        mdPercentage = 0;
        mdPercentageMax = 0;
        mbIsDeleted = false;        
        mnFkItemSubstituteId = 0;
        mnFkUnitSubstituteId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBomId = ((int[]) pk)[0];
        mnPkItemId = ((int[]) pk)[1];
        mnPkUnitId = ((int[]) pk)[2];
        mnPkBomSubstituteId = ((int[]) pk)[3];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBomId, mnPkItemId, mnPkUnitId, mnPkBomSubstituteId };
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT bs.*, i.item, i.item_key, u.symbol, un.usr, ue.usr, ud.usr " +
                "FROM mfg_bom_sub AS bs " +
                "INNER JOIN erp.itmu_item AS i ON bs.fid_item_sub = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON bs.fid_unit_sub = u.id_unit " +
                "INNER JOIN erp.usru_usr AS un ON bs.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON bs.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON bs.fid_usr_del = ud.id_usr " +
                "WHERE bs.id_bom = " + key[0] + " AND bs.id_item = " + key[1] + " AND bs.id_unit = " + key[2] + " AND bs.id_bom_sub = " + key[3] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBomId = resultSet.getInt("bs.id_bom");
                mnPkItemId = resultSet.getInt("bs.id_item");
                mnPkUnitId = resultSet.getInt("bs.id_unit");
                mnPkBomSubstituteId = resultSet.getInt("bs.id_bom_sub");
                mdPercentage = resultSet.getDouble("bs.per");
                mdPercentageMax = resultSet.getDouble("bs.per_max");
                mbIsDeleted = resultSet.getBoolean("bs.b_del");                
                mnFkItemSubstituteId = resultSet.getInt("bs.fid_item_sub");
                mnFkUnitSubstituteId = resultSet.getInt("bs.fid_unit_sub");
                mnFkUserNewId = resultSet.getInt("bs.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("bs.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("bs.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("bs.ts_new");
                mtUserEditTs = resultSet.getTimestamp("bs.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("bs.ts_del");
                
                msDbmsItemSubstitute = resultSet.getString("i.item");
                msDbmsItemSubstituteKey = resultSet.getString("i.item_key");
                msDbmsUnitSubstitute = resultSet.getString("u.symbol");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

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
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL mfg_bom_sub_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?) }");
            callableStatement.setInt(nParam++, mnPkBomId);
            callableStatement.setInt(nParam++, mnPkItemId);
            callableStatement.setInt(nParam++, mnPkUnitId);
            System.out.println("mnPkBomSubstituteId: " + mnPkBomSubstituteId);
            callableStatement.setInt(nParam++, mnPkBomSubstituteId);
            callableStatement.setDouble(nParam++, mdPercentage);
            callableStatement.setDouble(nParam++, mdPercentageMax);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkItemSubstituteId);
            callableStatement.setInt(nParam++, mnFkUnitSubstituteId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

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
    public Date getLastDbUpdate() {
        return null;
    }
}
