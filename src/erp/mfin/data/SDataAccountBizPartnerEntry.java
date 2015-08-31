/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Sergio Flores
 */
public class SDataAccountBizPartnerEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkAccountBizPartnerId;
    protected int mnPkAccountBizPartnerTypeId;
    protected int mnPkEntryId;
    protected double mdPercentage;
    protected java.lang.String msFkAccountId;
    protected java.lang.String msFkCostCenterId_n;
    protected int mnFkBookkeepingRegistryTypeId;

    protected java.lang.String msDbmsAccountBizPartner;
    protected java.lang.String msDbmsAccountBizPartnerType;
    protected java.lang.String msDbmsAccount;
    protected java.lang.String msDbmsCostCenter_n;
    protected java.lang.String msDbmsBookkeepingRegistryType;

    public SDataAccountBizPartnerEntry() {
        super(SDataConstants.FIN_ACC_BP_ETY);
        reset();
    }

    public void setPkAccountBizPartnerId(int n) { mnPkAccountBizPartnerId = n; }
    public void setPkAccountBizPartnerTypeId(int n) { mnPkAccountBizPartnerTypeId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setPercentage(double d) { mdPercentage = d; }
    public void setFkAccountId(java.lang.String s) { msFkAccountId = s; }
    public void setFkCostCenterId_n(java.lang.String s) { msFkCostCenterId_n = s; }
    public void setFkBookkeepingRegistryTypeId(int n) { mnFkBookkeepingRegistryTypeId = n; }

    public int getPkAccountBizPartnerId() { return mnPkAccountBizPartnerId; }
    public int getPkAccountBizPartnerTypeId() { return mnPkAccountBizPartnerTypeId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public double getPercentage() { return mdPercentage; }
    public java.lang.String getFkAccountId() { return msFkAccountId; }
    public java.lang.String getFkCostCenterId_n() { return msFkCostCenterId_n; }
    public int getFkBookkeepingRegistryTypeId() { return mnFkBookkeepingRegistryTypeId; }

    public void setDbmsAccountBizPartner(java.lang.String s) { msDbmsAccountBizPartner = s; }
    public void setDbmsAccountBizPartnerType(java.lang.String s) { msDbmsAccountBizPartnerType = s; }
    public void setDbmsAccount(java.lang.String s) { msDbmsAccount = s; }
    public void setDbmsCostCenter_n(java.lang.String s) { msDbmsCostCenter_n = s; }
    public void setDbmsBookkeepingRegistryType(java.lang.String s) { msDbmsBookkeepingRegistryType = s; }

    public java.lang.String getDbmsAccountBizPartner() { return msDbmsAccountBizPartner; }
    public java.lang.String getDbmsAccountBizPartnerType() { return msDbmsAccountBizPartnerType; }
    public java.lang.String getDbmsAccount() { return msDbmsAccount; }
    public java.lang.String getDbmsCostCenter_n() { return msDbmsCostCenter_n; }
    public java.lang.String getDbmsBookkeepingRegistryType() { return msDbmsBookkeepingRegistryType; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkAccountBizPartnerId = ((int[]) pk)[0];
        mnPkAccountBizPartnerTypeId = ((int[]) pk)[1];
        mnPkEntryId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkAccountBizPartnerId, mnPkAccountBizPartnerTypeId, mnPkEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkAccountBizPartnerId = 0;
        mnPkAccountBizPartnerTypeId = 0;
        mnPkEntryId = 0;
        mdPercentage = 0;
        msFkAccountId = "";
        msFkCostCenterId_n = "";
        mnFkBookkeepingRegistryTypeId = 0;

        msDbmsAccountBizPartner = "";
        msDbmsAccountBizPartnerType = "";
        msDbmsAccount = "";
        msDbmsCostCenter_n = "";
        msDbmsBookkeepingRegistryType = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT e.*, ab.acc_bp, abt.tp_acc_bp, b.tp_bkr, a.acc, c.cc " +
                    "FROM fin_acc_bp_ety AS e " +
                    "INNER JOIN fin_acc_bp AS ab ON e.id_acc_bp = ab.id_acc_bp " +
                    "INNER JOIN erp.fins_tp_acc_bp AS abt ON e.id_tp_acc_bp = abt.id_tp_acc_bp " +
                    "INNER JOIN erp.fins_tp_bkr AS b ON e.fid_tp_bkr = b.id_tp_bkr " +
                    "INNER JOIN fin_acc AS a ON e.fid_acc = a.id_acc " +
                    "LEFT OUTER JOIN fin_cc AS c ON e.fid_cc_n = c.id_cc " +
                    "WHERE e.id_acc_bp = " + key[0] + " AND e.id_tp_acc_bp = " + key[1] + " AND e.id_ety = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkAccountBizPartnerId = resultSet.getInt("e.id_acc_bp");
                mnPkAccountBizPartnerTypeId = resultSet.getInt("e.id_tp_acc_bp");
                mnPkEntryId = resultSet.getInt("e.id_ety");
                mdPercentage = resultSet.getDouble("e.per");
                msFkAccountId = resultSet.getString("e.fid_acc");
                msFkCostCenterId_n = resultSet.getString("e.fid_cc_n");
                if (resultSet.wasNull()) {
                    msFkCostCenterId_n = "";
                }
                mnFkBookkeepingRegistryTypeId = resultSet.getInt("fid_tp_bkr");

                msDbmsAccountBizPartner = resultSet.getString("ab.acc_bp");
                msDbmsAccountBizPartnerType = resultSet.getString("abt.tp_acc_bp");
                msDbmsAccount = resultSet.getString("a.acc");
                msDbmsCostCenter_n = resultSet.getString("c.cc");
                if (resultSet.wasNull()) {
                    msDbmsCostCenter_n = "";
                }
                msDbmsBookkeepingRegistryType = resultSet.getString("b.tp_bkr");

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
            callableStatement = connection.prepareCall(
                    "{ CALL fin_acc_bp_ety_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkAccountBizPartnerId);
            callableStatement.setInt(nParam++, mnPkAccountBizPartnerTypeId);
            callableStatement.setInt(nParam++, mnPkEntryId);
            callableStatement.setDouble(nParam++, mdPercentage);
            callableStatement.setString(nParam++, msFkAccountId);
            if (msFkCostCenterId_n.length() > 0) callableStatement.setString(nParam++, msFkCostCenterId_n); else callableStatement.setNull(nParam++, java.sql.Types.CHAR);
            callableStatement.setInt(nParam++, mnFkBookkeepingRegistryTypeId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnPkEntryId = callableStatement.getInt(nParam - 3);
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
        return null;
    }
}
