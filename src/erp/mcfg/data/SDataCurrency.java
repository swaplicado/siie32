/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mcfg.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author Alfonso Flores
 */
public class SDataCurrency extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkCurrencyId;
    protected java.lang.String msKey;
    protected java.lang.String msCurrency;
    protected java.lang.String msTextSymbol;
    protected java.lang.String msTextSingular;
    protected java.lang.String msTextPlural;
    protected java.lang.String msTextPrefix;
    protected java.lang.String msTextSuffix;
    protected boolean mbIsDeleted;
    protected int mnFkFiscalCurrencyId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataCurrency() {
        super(SDataConstants.CFGU_CUR);
        reset();
    }

    public void setPkCurrencyId(int n) { mnPkCurrencyId = n; }
    public void setKey(java.lang.String s) { msKey = s; }
    public void setCurrency(java.lang.String s) { msCurrency = s; }
    public void setTextSymbol(java.lang.String s) { msTextSymbol = s; }
    public void setTextSingular(java.lang.String s) { msTextSingular = s; }
    public void setTextPlural(java.lang.String s) { msTextPlural = s; }
    public void setTextPrefix(java.lang.String s) { msTextPrefix = s; }
    public void setTextSuffix(java.lang.String s) { msTextSuffix = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkFiscalCurrencyId(int n) { mnFkFiscalCurrencyId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkCurrencyId() { return mnPkCurrencyId; }
    public java.lang.String getKey() { return msKey; }
    public java.lang.String getCurrency() { return msCurrency; }
    public java.lang.String getTextSymbol() { return msTextSymbol; }
    public java.lang.String getTextSingular() { return msTextSingular; }
    public java.lang.String getTextPlural() { return msTextPlural; }
    public java.lang.String getTextPrefix() { return msTextPrefix; }
    public java.lang.String getTextSuffix() { return msTextSuffix; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkFiscalCurrencyId() { return mnFkFiscalCurrencyId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkCurrencyId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkCurrencyId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkCurrencyId = 0;
        msKey = "";
        msCurrency = "";
        msTextSymbol = "";
        msTextSingular = "";
        msTextPlural = "";
        msTextPrefix = "";
        msTextSuffix = "";
        mbIsDeleted = false;
        mnFkFiscalCurrencyId = 0;
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
            sql = "SELECT * FROM erp.cfgu_cur WHERE id_cur = " + key[0]  + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkCurrencyId = resultSet.getInt("id_cur");
                msKey = resultSet.getString("cur_key");
                msCurrency = resultSet.getString("cur");
                msTextSymbol = resultSet.getString("txt_symbol");
                msTextSingular = resultSet.getString("txt_singular");
                msTextPlural = resultSet.getString("txt_plural");
                msTextPrefix = resultSet.getString("txt_prefix");
                msTextSuffix = resultSet.getString("txt_suffix");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkFiscalCurrencyId = resultSet.getInt("fid_fiscal_cur");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

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
                    "{ CALL erp.cfgu_cur_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkCurrencyId);
            callableStatement.setString(nParam++, msKey);
            callableStatement.setString(nParam++, msCurrency);
            callableStatement.setString(nParam++, msTextSymbol);
            callableStatement.setString(nParam++, msTextSingular);
            callableStatement.setString(nParam++, msTextPlural);
            callableStatement.setString(nParam++, msTextPrefix);
            callableStatement.setString(nParam++, msTextSuffix);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkFiscalCurrencyId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnPkCurrencyId = callableStatement.getInt(nParam - 3);
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
        return mtUserEditTs;
    }
}
