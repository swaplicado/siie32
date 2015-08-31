/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataTaxGroup extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkTaxGroupId;
    protected java.lang.String msTaxGroup;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.util.Vector<erp.mfin.data.SDataTaxGroupEntry> mvDbmsTaxGroupEntries;

    public SDataTaxGroup() {
        super(SDataConstants.FIN_TAX_GRP);
        mvDbmsTaxGroupEntries = new Vector<SDataTaxGroupEntry>();
        reset();
    }

    public void setPkTaxGroupId(int n) { mnPkTaxGroupId = n; }
    public void setTaxGroup(java.lang.String s) { msTaxGroup = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkTaxGroupId() { return mnPkTaxGroupId; }
    public java.lang.String getTaxGroup() { return msTaxGroup; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public java.util.Vector<erp.mfin.data.SDataTaxGroupEntry> getDbmsTaxGroupEntries() {return mvDbmsTaxGroupEntries; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkTaxGroupId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkTaxGroupId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkTaxGroupId = 0;
        msTaxGroup = "";
        mbIsDeleted = false;
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
        String sql = "";
        ResultSet resultSet = null;
        Statement statementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM fin_tax_grp WHERE id_tax_grp = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkTaxGroupId = resultSet.getInt("id_tax_grp");
                msTaxGroup = resultSet.getString("tax_grp");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                statementAux = statement.getConnection().createStatement();

                sql = "SELECT id_tax_grp, id_tp_tax_idy_emir, id_tp_tax_idy_recr, id_ety " +
                        "FROM fin_tax_grp_ety " +
                        "WHERE id_tax_grp = " + key[0] + " " +
                        "ORDER BY id_tp_tax_idy_emir, id_tp_tax_idy_recr, dt_start, dt_end_n, app_order, fid_tax_bas, fid_tax ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataTaxGroupEntry entry = new SDataTaxGroupEntry();
                    if (entry.read(new int[] { resultSet.getInt("id_tax_grp"), resultSet.getInt("id_tp_tax_idy_emir"), resultSet.getInt("id_tp_tax_idy_recr"),
                        resultSet.getInt("id_ety") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsTaxGroupEntries.add(entry);
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
        int nParam = 0;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            // Save tax group:

            callableStatement = connection.prepareCall(
                    "{ CALL fin_tax_grp_save(" +
                    "?, ?, ?, ?, ?, ?, ?) }");
            nParam = 1;
            callableStatement.setInt(nParam++, mnPkTaxGroupId);
            callableStatement.setString(nParam++, msTaxGroup);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnPkTaxGroupId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                // Delete previous tax group entries:

                callableStatement = connection.prepareCall(
                        "{ CALL fin_tax_grp_ety_delete(?, ?, ?, ?) }");
                nParam = 1;
                callableStatement.setInt(nParam++, mnPkTaxGroupId);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
                callableStatement.execute();

                mnDbmsErrorId = callableStatement.getInt(nParam - 2);
                msDbmsError = callableStatement.getString(nParam - 1);

                if (mnDbmsErrorId != 0) {
                    throw new Exception(msDbmsError);
                }
                else {
                    // Save current tax group entries:
                    
                    nParam = 1;

                    for (int i = 0; i < mvDbmsTaxGroupEntries.size(); i++) {
                        mvDbmsTaxGroupEntries.get(i).setPkTaxGroupId(mnPkTaxGroupId);
                        mvDbmsTaxGroupEntries.get(i).setPkEntryId(0);   // force save action as if registry were new
                        if (mvDbmsTaxGroupEntries.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }

                    mbIsRegistryNew = false;
                    mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
                }
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
