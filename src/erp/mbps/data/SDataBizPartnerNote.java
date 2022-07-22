/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataBizPartnerNote extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkBizPartnerId;
    protected int mnPkNotesId;
    protected java.lang.String msNotes;

    public SDataBizPartnerNote() {
        super(SDataConstants.BPSU_BPB_NTS);
        reset();
    }

    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkNotesId(int n) { mnPkNotesId = n; }
    public void setNotes(java.lang.String s) { msNotes = s; }

    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkNotesId() { return mnPkNotesId; }
    public java.lang.String getNotes() { return msNotes; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBizPartnerId = ((int[]) pk)[0];
        mnPkNotesId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkBizPartnerId, mnPkNotesId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkBizPartnerId = 0;
        mnPkNotesId = 0;
        msNotes = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * " +
                    "FROM erp.bpsu_bp_nts " +
                    "WHERE id_bp = " + key[0] + " AND id_nts = " + key[1] + ";";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBizPartnerId = resultSet.getInt("id_bp");
                mnPkNotesId = resultSet.getInt("id_nts");
                msNotes = resultSet.getString("nts");

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

        try {
            callableStatement = connection.prepareCall("{ CALL erp.bpsu_bp_nts_save(?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkBizPartnerId);
            callableStatement.setInt(nParam++, mnPkNotesId);
            callableStatement.setString(nParam++, msNotes);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkNotesId = callableStatement.getInt(nParam - 3);
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
}
