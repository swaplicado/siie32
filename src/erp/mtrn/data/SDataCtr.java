/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.util.Vector;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Alfonso Flores
 */
public class SDataCtr extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected java.util.Date mtDate;
    protected java.util.Date mtDatePayment;
    protected java.lang.String msNumber;
    protected double mdTotal_r;
    protected boolean mbIsDeleted;
    protected int mnFkBizPartnerId;
    protected int mnFkDpsStatusId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.util.Vector<erp.mtrn.data.SDataCtrEntry> mvDbmsEntries;

    public SDataCtr() {
        super(SDataConstants.TRN_CTR);
        mvDbmsEntries = new Vector<erp.mtrn.data.SDataCtrEntry>();
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setDatePayment(java.util.Date t) { mtDatePayment = t; }
    public void setNumber(java.lang.String s) { msNumber = s; }
    public void setTotal_r(double d) { mdTotal_r = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    public void setFkDpsStatusId(int n) { mnFkDpsStatusId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public java.util.Date getDate() { return mtDate; }
    public java.util.Date getDatePayment() { return mtDatePayment; }
    public java.lang.String getNumber() { return msNumber; }
    public double getTotal_r() { return mdTotal_r; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    public int getFkDpsStatusId() { return mnFkDpsStatusId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public java.util.Vector<erp.mtrn.data.SDataCtrEntry> getDbmsEntries() {return mvDbmsEntries; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mtDate = null;
        mtDatePayment = null;
        msNumber = "";
        mdTotal_r = 0;
        mbIsDeleted = false;
        mnFkBizPartnerId = 0;
        mnFkDpsStatusId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mvDbmsEntries.clear();
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
            sql = "SELECT * FROM trn_ctr WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                mtDate = resultSet.getDate("dt");
                mtDatePayment = resultSet.getDate("dt_pay");
                msNumber = resultSet.getString("num");
                mdTotal_r = resultSet.getDouble("tot_r");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkBizPartnerId = resultSet.getInt("fid_bp");
                mnFkDpsStatusId = resultSet.getInt("fid_st_dps");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                statementAux = statement.getConnection().createStatement();

                // Read aswell the entries:

                sql = "SELECT id_year, id_doc, id_ety FROM trn_ctr_ety WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " AND b_del = FALSE ";
                resultSet = statement.executeQuery(sql);
                int i = 0;
                while (resultSet.next()) {
                    SDataCtrEntry entry = new SDataCtrEntry();
                    if (entry.read(new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_doc"), resultSet.getInt("id_ety") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsEntries.add(entry);
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
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL trn_ctr_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkDocId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDate.getTime()));
            callableStatement.setDate(nParam++, new java.sql.Date(mtDatePayment.getTime()));
            callableStatement.setString(nParam++, msNumber);
            callableStatement.setDouble(nParam++, mdTotal_r);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkBizPartnerId);
            callableStatement.setInt(nParam++, mnFkDpsStatusId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkDocId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                // Save aswell the entries:

                for (int i = 0; i < mvDbmsEntries.size(); i++) {
                    mvDbmsEntries.get(i).setPkYearId(mnPkYearId);
                    mvDbmsEntries.get(i).setPkDocId(mnPkDocId);

                    if (mvDbmsEntries.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (SQLException e) {
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
