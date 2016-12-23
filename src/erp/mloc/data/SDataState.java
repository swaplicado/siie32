/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mloc.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import sa.gui.util.SUtilConsts;
/**
 *
 * @author Juan Barajas
 */
public class SDataState extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    protected int mnPkStateId;
    protected java.lang.String msState;
    protected java.lang.String msStateLan;
    protected java.lang.String msStateAbbr;
    protected java.lang.String msStateCode;
    protected boolean mbIsDeleted;
    protected int mnFkCountryId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataState() {
        super(SDataConstants.LOCU_STA);
        reset();
    }

    public void setPkStateId(int n) { mnPkStateId = n; }
    public void setState(java.lang.String s) { msState = s; }
    public void setStateLan(java.lang.String s) { msStateLan = s; }
    public void setStateAbbr(java.lang.String s) { msStateAbbr = s; }
    public void setStateCode(java.lang.String s) { msStateCode = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkCountryId(int n) { mnFkCountryId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkStateId() { return mnPkStateId; }
    public java.lang.String getState() { return msState; }
    public java.lang.String getStateLan() { return msStateLan; }
    public java.lang.String getStateAbbr() { return msStateAbbr; }
    public java.lang.String getStateCode() { return msStateCode; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkCountryId() { return mnFkCountryId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkStateId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkStateId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkStateId = 0;
        msState = "";
        msStateLan = "";
        msStateAbbr = "";
        msStateCode = "";
        mbIsDeleted = false;
        mnFkCountryId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.locu_sta WHERE id_sta = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkStateId = resultSet.getInt("id_sta");
                msState = resultSet.getString("sta");
                msStateLan = resultSet.getString("sta_lan");
                msStateAbbr = resultSet.getString("sta_abbr");
                msStateCode = resultSet.getString("sta_code");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkCountryId = resultSet.getInt("fid_cty");
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
        String sql = "";
        ResultSet resultSet = null;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbIsRegistryNew) {
                sql = "SELECT COUNT(*) FROM erp.locu_sta WHERE id_sta = " + mnPkStateId + " ";
                resultSet = connection.createStatement().executeQuery(sql);

                if (resultSet.next()) {
                    mbIsRegistryNew = resultSet.getInt(1) == 0;
                }
            }

            if (mbIsRegistryNew) {
                mbIsDeleted = false;
                mnFkUserEditId = SUtilConsts.USR_NA_ID;
                
                sql = "INSERT INTO erp.hrsu_emp VALUES (" +
                        mnPkStateId + ", " +
                        "'" + msState + "', " +
                        "'" + msStateLan + "', " +
                        "'" + msStateAbbr + "', " +
                        "'" + msStateCode + "', " +
                        (mbIsDeleted ? 1 : 0) + ", " +
                        mnFkCountryId + ", " + 
                        mnFkUserNewId + ", " +
                        mnFkUserEditId + ", " +
                        mnFkUserDeleteId + ", " +
                        "NOW()" + ", " +
                        "NOW()" + ", " +
                        "NOW()" + " " +
                        ")";
            }
            else {

                sql = "UPDATE erp.hrsu_emp SET " +
                        //"id_sta = " + mnPkStateId + ", " +
                        "sta = '" + msState + "', " +
                        "sta_lan = '" + msStateLan + "', " +
                        "sta_abbr = '" + msStateAbbr + "', " +
                        "sta_code = '" + msStateCode + "', " +
                        "b_del = " + (mbIsDeleted ? 1 : 0) + ", " +
                        "fid_cty = " + mnFkCountryId + ", " +
                        //"fid_usr_new = " + mnFkUserNewId + ", " +
                        "fid_usr_edit = " + mnFkUserEditId + ", " +
                        "fid_usr_del = " + mnFkUserEditId + ", " +
                        //"ts_new = " + "NOW()" + ", " +
                        "ts_edit = " + "NOW()," + " " +
                        "ts_del = " + "NOW()" + " " +
                        "WHERE id_sta = " + mnPkStateId + " ";
            }

            connection.createStatement().execute(sql);
            
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
