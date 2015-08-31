/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Alfonso Flores
 */
public class SDataBizPartnerAttribute extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkBizPartnerId;
    protected int mnPkAttributeTypeId;

    public SDataBizPartnerAttribute() {
        super(SDataConstants.BPSU_BP_ATT);
        reset();
    }

    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkAttributeTypeId(int n) { mnPkAttributeTypeId = n; }

    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkAttributeTypeId() { return mnPkAttributeTypeId; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBizPartnerId = ((int[]) pk)[0];
        mnPkAttributeTypeId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkBizPartnerId, mnPkAttributeTypeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkBizPartnerId = 0;
        mnPkAttributeTypeId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.bpsu_bp_att WHERE id_bp = " + key[0] + " AND id_tp_bp_att = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBizPartnerId = resultSet.getInt("id_bp");
                mnPkAttributeTypeId = resultSet.getInt("id_tp_bp_att");

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
            callableStatement = connection.prepareCall(
                    "{ CALL erp.bpsu_bp_att_save(" +
                    "?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkBizPartnerId);
            callableStatement.setInt(nParam++, mnPkAttributeTypeId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
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
