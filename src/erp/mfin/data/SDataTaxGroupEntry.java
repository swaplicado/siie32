/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataTaxGroupEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkTaxGroupId;
    protected int mnPkTaxIdentityEmisorTypeId;
    protected int mnPkTaxIdentityReceptorTypeId;
    protected int mnPkEntryId;
    protected int mnApplicationOrder;
    protected java.util.Date mtDateStart;
    protected java.util.Date mtDateEnd_n;
    protected int mnFkTaxBasicId;
    protected int mnFkTaxId;

    protected java.lang.String msDbmsTaxIdentityEmisorType;
    protected java.lang.String msDbmsTaxIdentityReceptorType;
    protected java.lang.String msDbmsTaxBasic;
    protected java.lang.String msDbmsTax;

    public SDataTaxGroupEntry() {
        super(SDataConstants.FIN_TAX_GRP_ETY);
        reset();
    }

    public void setPkTaxGroupId(int n) { mnPkTaxGroupId = n; }
    public void setPkTaxIdentityEmisorTypeId(int n) { mnPkTaxIdentityEmisorTypeId = n; }
    public void setPkTaxIdentityReceptorTypeId(int n) { mnPkTaxIdentityReceptorTypeId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setApplicationOrder(int n) { mnApplicationOrder = n; }
    public void setDateStart(java.util.Date t) { mtDateStart = t; }
    public void setDateEnd_n(java.util.Date t) { mtDateEnd_n = t; }
    public void setFkTaxBasicId(int n) { mnFkTaxBasicId = n; }
    public void setFkTaxId(int n) { mnFkTaxId = n; }

    public int getPkTaxGroupId() { return mnPkTaxGroupId; }
    public int getPkTaxIdentityEmisorTypeId() { return mnPkTaxIdentityEmisorTypeId; }
    public int getPkTaxIdentityReceptorTypeId() { return mnPkTaxIdentityReceptorTypeId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getApplicationOrder() { return mnApplicationOrder; }
    public java.util.Date getDateStart() { return mtDateStart; }
    public java.util.Date getDateEnd_n() { return mtDateEnd_n; }
    public int getFkTaxBasicId() { return mnFkTaxBasicId; }
    public int getFkTaxId() { return mnFkTaxId; }

    public void setDbmsTaxIdentityEmisorType(java.lang.String s) { msDbmsTaxIdentityEmisorType = s; }
    public void setDbmsTaxIdentityReceptorType(java.lang.String s) { msDbmsTaxIdentityReceptorType = s; }
    public void setDbmsTaxBasic(java.lang.String s) { msDbmsTaxBasic = s; }
    public void setDbmsTax(java.lang.String s) { msDbmsTax = s; }

    public java.lang.String getDbmsTaxIdentityEmisorType() { return msDbmsTaxIdentityEmisorType; }
    public java.lang.String getDbmsTaxIdentityReceptorType() { return msDbmsTaxIdentityReceptorType; }
    public java.lang.String getDbmsTaxBasic() { return msDbmsTaxBasic; }
    public java.lang.String getDbmsTax() { return msDbmsTax; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkTaxGroupId = ((int[]) pk)[0];
        mnPkTaxIdentityEmisorTypeId = ((int[]) pk)[1];
        mnPkTaxIdentityReceptorTypeId = ((int[]) pk)[2];
        mnPkEntryId = ((int[]) pk)[3];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkTaxGroupId, mnPkTaxIdentityEmisorTypeId, mnPkTaxIdentityReceptorTypeId, mnPkEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkTaxGroupId = 0;
        mnPkTaxIdentityEmisorTypeId = 0;
        mnPkTaxIdentityReceptorTypeId = 0;
        mnPkEntryId = 0;
        mnApplicationOrder = 0;
        mtDateStart = null;
        mtDateEnd_n = null;
        mnFkTaxBasicId = 0;
        mnFkTaxId = 0;

        msDbmsTaxIdentityEmisorType = "";
        msDbmsTaxIdentityReceptorType = "";
        msDbmsTaxBasic = "";
        msDbmsTax = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT tge.*, tie.tax_idy, tir.tax_idy, tb.tax_bas, t.tax " +
                    "FROM fin_tax_grp_ety AS tge " +
                    "INNER JOIN erp.finu_tax_idy AS tie ON " +
                    "tge.id_tp_tax_idy_emir = tie.id_tax_idy " +
                    "INNER JOIN erp.finu_tax_idy AS tir ON " +
                    "tge.id_tp_tax_idy_recr = tir.id_tax_idy " +
                    "INNER JOIN erp.finu_tax_bas AS tb ON " +
                    "tge.fid_tax_bas = tb.id_tax_bas " +
                    "INNER JOIN erp.finu_tax AS t ON " +
                    "tge.fid_tax_bas = t.id_tax_bas AND tge.fid_tax = t.id_tax " +
                    "WHERE tge.id_tax_grp = " + key[0] + " AND tge.id_tp_tax_idy_emir = " + key[1] +
                    " AND tge.id_tp_tax_idy_recr = " + key[2] + " AND tge.id_ety = " + key[3] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkTaxGroupId = resultSet.getInt("tge.id_tax_grp");
                mnPkTaxIdentityEmisorTypeId = resultSet.getInt("tge.id_tp_tax_idy_emir");
                mnPkTaxIdentityReceptorTypeId = resultSet.getInt("tge.id_tp_tax_idy_recr");
                mnPkEntryId = resultSet.getInt("tge.id_ety");
                mnApplicationOrder = resultSet.getInt("tge.app_order");
                mtDateStart = resultSet.getDate("tge.dt_start");
                mtDateEnd_n = resultSet.getDate("tge.dt_end_n");
                if (resultSet.wasNull()) mtDateEnd_n = null;
                mnFkTaxBasicId = resultSet.getInt("tge.fid_tax_bas");
                mnFkTaxId = resultSet.getInt("tge.fid_tax");

                msDbmsTaxIdentityEmisorType = resultSet.getString("tie.tax_idy");
                msDbmsTaxIdentityReceptorType = resultSet.getString("tir.tax_idy");
                msDbmsTaxBasic = resultSet.getString("tb.tax_bas");
                msDbmsTax = resultSet.getString("t.tax");

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
                    "{ CALL fin_tax_grp_ety_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?) }");
            callableStatement.setInt(nParam++, mnPkTaxGroupId);
            callableStatement.setInt(nParam++, mnPkTaxIdentityEmisorTypeId);
            callableStatement.setInt(nParam++, mnPkTaxIdentityReceptorTypeId);
            callableStatement.setInt(nParam++, mnPkEntryId);
            callableStatement.setInt(nParam++, mnApplicationOrder);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDateStart.getTime()));
            if (mtDateEnd_n != null) callableStatement.setDate(nParam++, new java.sql.Date(mtDateEnd_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.DATE);
            callableStatement.setInt(nParam++, mnFkTaxBasicId);
            callableStatement.setInt(nParam++, mnFkTaxId);
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
