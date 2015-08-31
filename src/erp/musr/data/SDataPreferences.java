/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.xml.SXmlDocument;

/**
 *
 * @author Juan Barajas
 */
public class SDataPreferences extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkUserId;
    protected int mnPkGuiTypeId;
    protected int mnPkGui1;
    protected int mnPkGui2;
    protected int mnPkGui3;
    protected int mnPkGui4;
    protected java.lang.String msPreferences;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected SXmlDocument moPreferences;
    
    public SDataPreferences() {
        super(SDataConstants.USRU_PREF);
        reset();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkGuiTypeId(int n) { mnPkGuiTypeId = n; }
    public void setPkGui1(int n) { mnPkGui1 = n; }
    public void setPkGui2(int n) { mnPkGui2 = n; }
    public void setPkGui3(int n) { mnPkGui3 = n; }
    public void setPkGui4(int n) { mnPkGui4 = n; }
    public void setPreferences(java.lang.String s) { msPreferences = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }    
    
    public void setDbmsPreferences(erp.lib.xml.SXmlDocument o) { moPreferences = o; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkGuiTypeId() { return mnPkGuiTypeId; }
    public int getPkGui1() { return mnPkGui1; }
    public int getPkGui2() { return mnPkGui2; }
    public int getPkGui3() { return mnPkGui3; }
    public int getPkGui4() { return mnPkGui4; }
    public java.lang.String getPreferences() { return msPreferences; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    public erp.lib.xml.SXmlDocument getDbmsPreferences() { return moPreferences; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkUserId = ((int[]) pk)[0];
        mnPkGuiTypeId = ((int[]) pk)[1];
        mnPkGui1 = ((int[]) pk)[2];
        mnPkGui2 = ((int[]) pk)[3];
        mnPkGui3 = ((int[]) pk)[4];
        mnPkGui4 = ((int[]) pk)[5];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkUserId, mnPkGuiTypeId, mnPkGui1, mnPkGui2, mnPkGui3, mnPkGui4 };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkUserId = 0;
        mnPkGuiTypeId = 0;
        mnPkGui1 = 0;
        mnPkGui2 = 0;
        mnPkGui3 = 0;
        mnPkGui4 = 0;
        msPreferences = "";
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
        
        moPreferences = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.usru_pref WHERE id_usr = " + key[0] + " AND id_gui_tp = " + key[1] + " AND id_gui_1 = " + key[2] + " AND id_gui_2 = " + key[3] + " AND id_gui_3 = " + key[4] + " AND id_gui_4 = " + key[5] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUserId = resultSet.getInt("id_usr");
                mnPkGuiTypeId = resultSet.getInt("id_gui_tp");
                mnPkGui1 = resultSet.getInt("id_gui_1");
                mnPkGui2 = resultSet.getInt("id_gui_2");
                mnPkGui3 = resultSet.getInt("id_gui_3");
                mnPkGui4 = resultSet.getInt("id_gui_4");
                msPreferences = resultSet.getString("pref");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");
                
                switch (mnPkGuiTypeId) {
                    case SDataConstantsSys.USRX_PREF_SESSION:
                        moPreferences = new SPreferencesSession("Session");           
                        break;
                    case SDataConstantsSys.USRX_PREF_VIEW:
                        moPreferences = new SPreferencesView("View");
                        break;
                    case SDataConstantsSys.USRX_PREF_FORM:
                        moPreferences = new SPreferencesForm("Form");
                        break;
                    case SDataConstantsSys.USRX_PREF_REPORT:
                        moPreferences = new SPreferencesReport("Report");
                        break;
                    default:
                        break;
                }
                moPreferences.processXml(msPreferences);
            }
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
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
        
        msPreferences = moPreferences.getXmlString();
        try {
            callableStatement = connection.prepareCall(
                    "{ CALL erp.usru_pref_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + 
                    " ? ) }");
            callableStatement.setInt(nParam++, mnPkUserId);
            callableStatement.setInt(nParam++, mnPkGuiTypeId);
            callableStatement.setInt(nParam++, mnPkGui1);
            callableStatement.setInt(nParam++, mnPkGui2);
            callableStatement.setInt(nParam++, mnPkGui3);
            callableStatement.setInt(nParam++, mnPkGui4);
            callableStatement.setString(nParam++, msPreferences);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

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
