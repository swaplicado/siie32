/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;

/**
 *
 * @author Juan Barajas
 */
public class SDataCfdSignLog extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    public static final int FIELD_CODE_STP = 1;
    
    protected int mnPkLogId;
    protected java.util.Date mtDate;
    protected int mnCodeAction;
    protected int mnCodeStep;
    protected boolean mbIsSystem;
    protected boolean mbIsDeleted;
    protected int mnFkCfdId;
    protected int mnFkPacId_n;
    protected int mnFkUserId;
    protected java.util.Date mtUserTs;
    
    protected SDataCfdSignLogMsg moDbmsDataCfdSignLogMsg;
    
    public SDataCfdSignLog() {
        super(SModConsts.TRN_CFD_SIGN_LOG);
        reset();
    }

    public void setPkLogId(int n) { mnPkLogId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setCodeAction(int n) { mnCodeAction = n; }
    public void setCodeStep(int n) { mnCodeStep = n; }
    public void setIsSystem(boolean b) { mbIsSystem = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkCfdId(int n) { mnFkCfdId = n; }
    public void setFkPacId_n(int n) { mnFkPacId_n = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setUserTs(java.util.Date t) { mtUserTs = t; }

    public void setDbmsDataCfdSignLogMsg(SDataCfdSignLogMsg o) { moDbmsDataCfdSignLogMsg = o; }

    public int getPkLogId() { return mnPkLogId; }
    public java.util.Date getDate() { return mtDate; }
    public int getCodeAction() { return mnCodeAction; }
    public int getCodeStep() { return mnCodeStep; }
    public boolean getIsSystem() { return mbIsSystem; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkCfdId() { return mnFkCfdId; }
    public int getFkPacId_n() { return mnFkPacId_n; }
    public int getFkUserId() { return mnFkUserId; }
    public java.util.Date getUserTs() { return mtUserTs; }

    public SDataCfdSignLogMsg getDbmsDataCfdSignLogMsg() { return moDbmsDataCfdSignLogMsg; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkLogId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkLogId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkLogId = 0;
        mtDate = null;
        mnCodeAction = 0;
        mnCodeStep = 0;
        mbIsSystem = false;
        mbIsDeleted = false;
        mnFkCfdId = 0;
        mnFkPacId_n = 0;
        mnFkUserId = 0;
        mtUserTs = null;

        moDbmsDataCfdSignLogMsg = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;
        Statement statementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_cfd_sign_log WHERE id_log = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkLogId = resultSet.getInt("id_log");
                mtDate = resultSet.getDate("dt");
                mnCodeAction = resultSet.getInt("code_act");
                mnCodeStep = resultSet.getInt("code_stp");
                mbIsSystem = resultSet.getBoolean("b_sys");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkCfdId = resultSet.getInt("fid_cfd");
                mnFkPacId_n = resultSet.getInt("fid_pac_n");
                mnFkUserId = resultSet.getInt("fid_usr");
                mtUserTs = resultSet.getTimestamp("ts");

                statementAux = statement.getConnection().createStatement();
                
                sql = "SELECT id_msg FROM trn_cfd_sign_log_msg " +
                    "WHERE id_log = " + mnPkLogId + " ";
                resultSet = statement.executeQuery(sql);
                SDataCfdSignLogMsg logMsg = new SDataCfdSignLogMsg();
                while (resultSet.next()) {
                    if (logMsg.read(new int[] { mnPkLogId,  resultSet.getInt("id_msg")}, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        moDbmsDataCfdSignLogMsg = logMsg;
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
        int index = 1;
        boolean isUpd = false;
        String sql = "";
        ResultSet resultSet = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            statement = connection.createStatement();
            
            if (mnPkLogId == SLibConsts.UNDEFINED) {
                sql = "SELECT COALESCE(MAX(id_log), 0) + 1 AS f_log_id FROM trn_cfd_sign_log ";
                resultSet = statement.executeQuery(sql);
                
                if (!resultSet.next()) {
                    throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
                }
                else {
                    mnPkLogId = resultSet.getInt("f_log_id");
                }

                sql = "INSERT INTO trn_cfd_sign_log (id_log, " +
                        "dt, code_act, code_stp, b_sys, b_del, fid_cfd, fid_pac_n, fid_usr, ts) " +
                        "VALUES (" + mnPkLogId + ", " +
                        "?, ?, ?, ?, ?, ?, ?, ?, NOW())";
            }
            else {
                isUpd = true;
                
                sql = "UPDATE trn_cfd_sign_log SET code_act = ?, code_stp = ? " +
                        "WHERE id_log = " + mnPkLogId + " ";
            }
            
            if (!sql.isEmpty()) {
                preparedStatement = connection.prepareStatement(sql);

                if (!isUpd) {
                    preparedStatement.setDate(index++, new java.sql.Date(mtDate.getTime()));
                }
                preparedStatement.setInt(index++, mnCodeAction);
                preparedStatement.setInt(index++, mnCodeStep);
                if (!isUpd) {
                    preparedStatement.setBoolean(index++, mbIsSystem);
                    preparedStatement.setBoolean(index++, mbIsDeleted);
                    preparedStatement.setInt(index++, mnFkCfdId);
                    if (mnFkPacId_n == SLibConsts.UNDEFINED) {
                        preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
                    }
                    else {
                        preparedStatement.setInt(index++, mnFkPacId_n);
                    }
                    preparedStatement.setInt(index++, mnFkUserId);
                }
                preparedStatement.execute();
            }
            
            if (moDbmsDataCfdSignLogMsg != null) {
                moDbmsDataCfdSignLogMsg.setPkLogId(mnPkLogId);

                if (moDbmsDataCfdSignLogMsg.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
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
        return null;
    }
    
    public void saveField(java.sql.Connection connection, final int[] pk, final int field, final Object value) throws Exception {
        String sSql = "";

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        sSql = "UPDATE trn_cfd_sign_log SET ";

        switch (field) {
            case FIELD_CODE_STP:
                sSql += "code_stp = " + value + " ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        sSql += "WHERE id_log = " + pk[0] + " ";
        
        connection.createStatement().execute(sSql);
        
        mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
    }
}
