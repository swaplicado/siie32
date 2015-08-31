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
import erp.mcfg.data.SDataCompanyBranchEntity;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataAccessCompanyBranchEntity extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkUserId;
    protected int mnPkCompanyBranchId;
    protected int mnPkEntityId;
    protected boolean mbIsDefault;

    protected erp.mcfg.data.SDataCompanyBranchEntity moDbmsCompanyBranchEntity;

    public SDataAccessCompanyBranchEntity() {
        super(SDataConstants.USRU_ACCESS_COB_ENT);
        reset();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkCompanyBranchId(int n) { mnPkCompanyBranchId = n; }
    public void setPkEntityId(int n) { mnPkEntityId = n; }
    public void setIsDefault(boolean b) { mbIsDefault = b; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkCompanyBranchId() { return mnPkCompanyBranchId; }
    public int getPkEntityId() { return mnPkEntityId; }
    public boolean getIsDefault() { return mbIsDefault; }

    public void setDbmsCompanyBranchEntity(erp.mcfg.data.SDataCompanyBranchEntity o) { moDbmsCompanyBranchEntity = o; }

    public erp.mcfg.data.SDataCompanyBranchEntity getDbmsCompanyBranchEntity() { return moDbmsCompanyBranchEntity; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkUserId = ((int[]) pk)[0];
        mnPkCompanyBranchId = ((int[]) pk)[1];
        mnPkEntityId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkUserId, mnPkCompanyBranchId, mnPkEntityId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkUserId = 0;
        mnPkCompanyBranchId = 0;
        mnPkEntityId = 0;
        mbIsDefault = false;

        moDbmsCompanyBranchEntity = null;
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
                    "FROM erp.usru_access_cob_ent " +
                    "WHERE id_usr = " + key[0] + " AND id_cob = " + key[1] +  " AND id_ent = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUserId = resultSet.getInt("id_usr");
                mnPkCompanyBranchId = resultSet.getInt("id_cob");
                mnPkEntityId = resultSet.getInt("id_ent");
                mbIsDefault = resultSet.getBoolean("b_def");

                moDbmsCompanyBranchEntity = new SDataCompanyBranchEntity();
                if (moDbmsCompanyBranchEntity.read(new int[] { mnPkCompanyBranchId, mnPkEntityId }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
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
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL erp.usru_access_cob_ent_save(" +
                    "?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkUserId);
            callableStatement.setInt(nParam++, mnPkCompanyBranchId);
            callableStatement.setInt(nParam++, mnPkEntityId);
            callableStatement.setBoolean(nParam++, mbIsDefault);
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
