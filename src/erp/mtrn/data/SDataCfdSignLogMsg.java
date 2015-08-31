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
public class SDataCfdSignLogMsg extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    protected int mnPkLogId;
    protected int mnPkMessageId;
    protected java.lang.String msMessage;

    public SDataCfdSignLogMsg() {
        super(SModConsts.TRN_CFD_SIGN_LOG_MSG);
        reset();
    }

    public void setPkLogId(int n) { mnPkLogId = n; }
    public void setPkMessageId(int n) { mnPkMessageId = n; }
    public void setMessage(java.lang.String s) { msMessage = s; }

    public int getPkLogId() { return mnPkLogId; }
    public int getPkMessageId() { return mnPkMessageId; }
    public java.lang.String getMessage() { return msMessage; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkLogId = ((int[]) pk)[0];
        mnPkMessageId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkLogId, mnPkMessageId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkLogId = 0;
        mnPkMessageId = 0;
        msMessage = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_cfd_sign_log_msg WHERE id_log = " + key[0] + " AND id_msg = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkLogId = resultSet.getInt("id_log");
                mnPkMessageId = resultSet.getInt("id_msg");
                msMessage = resultSet.getString("msg");

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
        String sql = "";
        ResultSet resultSet = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            statement = connection.createStatement();
            
            if (mnPkMessageId == SLibConsts.UNDEFINED) {
                sql = "SELECT COALESCE(MAX(id_msg), 0) + 1 AS f_msg_id FROM trn_cfd_sign_log_msg WHERE id_log = " + mnPkLogId + " ";
                resultSet = statement.executeQuery(sql);
                
                if (!resultSet.next()) {
                    throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
                }
                else {
                    mnPkMessageId = resultSet.getInt("f_msg_id");
                }

                sql = "INSERT INTO trn_cfd_sign_log_msg (id_log, id_msg, msg) " +
                        "VALUES (" + mnPkLogId + ", " + mnPkMessageId + ", ?)";
            }
            else {
                sql = "UPDATE trn_cfd_sign_log_msg SET msg = ? " +
                        "WHERE id_log = " + mnPkLogId + " AND id_msg = " + mnPkMessageId + " ";
            }
            
            if (!sql.isEmpty()) {
                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(index++, msMessage);
                preparedStatement.execute();
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
}
