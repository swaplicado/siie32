/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Alfonso Flores
 */
public class SDataAccessCompanyBranch extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkUserId;
    protected int mnPkCompanyBranchId;
    protected boolean mbIsDefault;
    protected boolean mbIsUniversal;

    protected int mnDbmsFkCompanyId;
    protected java.lang.String msDbmsCompanyBranch;
    protected java.lang.String msDbmsCompanyBranchCode;

    public SDataAccessCompanyBranch() {
        super(SDataConstants.USRU_ACCESS_COB);
        reset();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkCompanyBranchId(int n) { mnPkCompanyBranchId = n; }
    public void setIsDefault(boolean b) { mbIsDefault = b; }
    public void setIsUniversal(boolean b) { mbIsUniversal = b; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkCompanyBranchId() { return mnPkCompanyBranchId; }
    public boolean getIsDefault() { return mbIsDefault; }
    public boolean getIsUniversal() { return mbIsUniversal; }

    public void setDbmsFkCompanyId(int n) { mnDbmsFkCompanyId = n; }
    public void setDbmsCompanyBranch(java.lang.String s) { msDbmsCompanyBranch = s; }
    public void setDbmsCompanyBranchCode(java.lang.String s) { msDbmsCompanyBranchCode = s; }

    public int getDbmsFkCompanyId() { return mnDbmsFkCompanyId; }
    public java.lang.String getDbmsCompanyBranch() { return msDbmsCompanyBranch; }
    public java.lang.String getDbmsCompanyBranchCode() { return msDbmsCompanyBranchCode; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkUserId = ((int[]) pk)[0];
        mnPkCompanyBranchId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkUserId,  mnPkCompanyBranchId};
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkUserId = 0;
        mnPkCompanyBranchId = 0;
        mbIsDefault = false;
        mbIsUniversal = false;

        mnDbmsFkCompanyId = 0;
        msDbmsCompanyBranch = "";
        msDbmsCompanyBranchCode = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT access_cob.*, bpb.fid_bp, bpb.bpb, bpb.code " +
                    "FROM erp.usru_access_cob AS access_cob " +
                    "INNER JOIN erp.bpsu_bpb AS bpb ON " +
                    "access_cob.id_cob = bpb.id_bpb " +
                    "WHERE access_cob.id_usr = " + key[0] + " AND access_cob.id_cob = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUserId = resultSet.getInt("access_cob.id_usr");
                mnPkCompanyBranchId = resultSet.getInt("access_cob.id_cob");
                mbIsDefault = resultSet.getBoolean("access_cob.b_def");
                mbIsUniversal = resultSet.getBoolean("access_cob.b_univ");

                mnDbmsFkCompanyId = resultSet.getInt("bpb.fid_bp");
                msDbmsCompanyBranch = resultSet.getString("bpb.bpb");
                msDbmsCompanyBranchCode = resultSet.getString("bpb.code");

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
                    "{ CALL erp.usru_access_cob_save(" +
                    "?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkUserId);
            callableStatement.setInt(nParam++, mnPkCompanyBranchId);
            callableStatement.setBoolean(nParam++, mbIsDefault);
            callableStatement.setBoolean(nParam++, mbIsUniversal);
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
