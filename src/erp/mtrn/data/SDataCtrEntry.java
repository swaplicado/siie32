/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Alfonso Flores
 */
public class SDataCtrEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected java.util.Date mtDate;
    protected java.lang.String msNumberSeries;
    protected java.lang.String msNumber;
    protected java.lang.String msNumberSeriesDps;
    protected java.lang.String msNumberDps;
    protected double mdTotal_r;
    protected double mdExchangeRate;
    protected double mdTotalCy;
    protected boolean mbIsDeleted;
    protected int mnFkDpsCategoryId;
    protected int mnFkDpsClassId;
    protected int mnFkDpsTypeId;
    protected int mnFkCurrencyId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsDpsType;
    protected java.lang.String msDbmsCurrencyKey;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;

    public SDataCtrEntry() {
        super(SDataConstants.TRN_CTR_ETY);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setNumberSeries(java.lang.String s) { msNumberSeries = s; }
    public void setNumber(java.lang.String s) { msNumber = s; }
    public void setNumberSeriesDps(java.lang.String s) { msNumberSeriesDps = s; }
    public void setNumberDps(java.lang.String s) { msNumberDps = s; }
    public void setTotal_r(double d) { mdTotal_r = d; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setTotalCy(double d) { mdTotalCy = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkDpsCategoryId(int n) { mnFkDpsCategoryId = n; }
    public void setFkDpsClassId(int n) { mnFkDpsClassId = n; }
    public void setFkDpsTypeId(int n) { mnFkDpsTypeId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public java.util.Date getDate() { return mtDate; }
    public java.lang.String getNumberSeries() { return msNumberSeries; }
    public java.lang.String getNumber() { return msNumber; }
    public java.lang.String getNumberSeriesDps() { return msNumberSeriesDps; }
    public java.lang.String getNumberDps() { return msNumberDps; }
    public double getTotal_r() { return mdTotal_r; }
    public double getExchangeRate() { return mdExchangeRate; }
    public double getTotalCy() { return mdTotalCy; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkDpsCategoryId() { return mnFkDpsCategoryId; }
    public int getFkDpsClassId() { return mnFkDpsClassId; }
    public int getFkDpsTypeId() { return mnFkDpsTypeId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsDpsType(java.lang.String s) { msDbmsDpsType = s; }
    public void setDbmsCurrencyKey(java.lang.String s) { msDbmsCurrencyKey = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public java.lang.String getDbmsDpsType() { return msDbmsDpsType; }
    public java.lang.String getDbmsCurrencyKey() { return msDbmsCurrencyKey; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkEntryId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        mtDate = null;
        msNumberSeries = "";
        msNumber = "";
        msNumberSeriesDps = "";
        msNumberDps = "";
        mdTotal_r = 0;
        mdExchangeRate = 0;
        mdTotalCy = 0;
        mbIsDeleted = false;
        mnFkDpsCategoryId = 0;
        mnFkDpsClassId = 0;
        mnFkDpsTypeId = 0;
        mnFkCurrencyId = 0;
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

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT e.*, tp.code, c.cur_key, un.usr, ue.usr, ud.usr " +
                    "FROM trn_ctr_ety AS e " +
                    "INNER JOIN erp.trnu_tp_dps AS tp ON " +
                    "e.fid_ct_dps = tp.id_ct_dps AND e.fid_cl_dps = tp.id_cl_dps AND e.fid_tp_dps = tp.id_tp_dps " +
                    "INNER JOIN erp.cfgu_cur AS c ON " +
                    "e.fid_cur = c.id_cur " +
                    "INNER JOIN erp.usru_usr AS un ON " +
                    "e.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON " +
                    "e.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON " +
                    "e.fid_usr_del = ud.id_usr " +
                    "WHERE e.id_year = " + key[0] + " AND e.id_doc = " + key[1] + " AND e.id_ety = " + key[2] +  " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("e.id_year");
                mnPkDocId = resultSet.getInt("e.id_doc");
                mnPkEntryId = resultSet.getInt("e.id_ety");
                mtDate = resultSet.getDate("e.dt");
                msNumberSeries = resultSet.getString("e.num_ser");
                msNumber = resultSet.getString("e.num");
                msNumberSeriesDps = resultSet.getString("e.num_ser_dps");
                msNumberDps = resultSet.getString("e.num_dps");
                mdTotal_r = resultSet.getDouble("e.tot_r");
                mdExchangeRate = resultSet.getDouble("e.exc_rate");
                mdTotalCy = resultSet.getDouble("e.tot_cur");
                mbIsDeleted = resultSet.getBoolean("e.b_del");
                mnFkDpsCategoryId = resultSet.getInt("e.fid_ct_dps");
                mnFkDpsClassId = resultSet.getInt("e.fid_cl_dps");
                mnFkDpsTypeId = resultSet.getInt("e.fid_tp_dps");
                mnFkCurrencyId = resultSet.getInt("e.fid_cur");
                mnFkUserNewId = resultSet.getInt("e.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("e.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("e.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("e.ts_new");
                mtUserEditTs = resultSet.getTimestamp("e.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("e.ts_del");

                msDbmsDpsType = resultSet.getString("tp.code");
                msDbmsCurrencyKey = resultSet.getString("c.cur_key");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

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
                    "{ CALL trn_ctr_ety_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkDocId);
            callableStatement.setInt(nParam++, mnPkEntryId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDate.getTime()));
            callableStatement.setString(nParam++, msNumberSeries);
            callableStatement.setString(nParam++, msNumber);
            callableStatement.setString(nParam++, msNumberSeriesDps);
            callableStatement.setString(nParam++, msNumberDps);
            callableStatement.setDouble(nParam++, mdTotal_r);
            callableStatement.setDouble(nParam++, mdExchangeRate);
            callableStatement.setDouble(nParam++, mdTotalCy);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkDpsCategoryId);
            callableStatement.setInt(nParam++, mnFkDpsClassId);
            callableStatement.setInt(nParam++, mnFkDpsTypeId);
            callableStatement.setInt(nParam++, mnFkCurrencyId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
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
        return mtUserEditTs;
    }
}
