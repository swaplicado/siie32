/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following classes:
 * - erp.mtrn.data.SDataCfd
 * All of them also make raw SQL insertions.
 */

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDataDpsAddendaEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable  {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected int mnBachocoNumeroPosicion;
    protected java.lang.String msBachocoCentro;
    protected int mnLorealEntryNumber;
    protected java.lang.String msSorianaCodigo;
    protected java.lang.String msElektraOrder;
    protected java.lang.String msElektraBarcode;
    protected int mnElektraCages;
    protected double mdElektraCagePriceUnitary;
    protected int mnElektraParts;
    protected double mdElektraPartPriceUnitary;
    protected java.lang.String msJsonData;

    public SDataDpsAddendaEntry() {
        super(SDataConstants.TRN_DPS_ADD_ETY);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setBachocoNumeroPosicion(int n) { mnBachocoNumeroPosicion = n; }
    public void setBachocoCentro(java.lang.String s) { msBachocoCentro = s; }
    public void setLorealEntryNumber(int n) { mnLorealEntryNumber = n; }
    public void setSorianaCodigo(java.lang.String s) { msSorianaCodigo = s; }
    public void setElektraOrder(java.lang.String s) { msElektraOrder = s; }
    public void setElektraBarcode(java.lang.String s) { msElektraBarcode = s; }
    public void setElektraCages(int n) { mnElektraCages = n; }
    public void setElektraCagePriceUnitary(double d) { mdElektraCagePriceUnitary = d; }
    public void setElektraParts(int n) { mnElektraParts = n; }
    public void setElektraPartPriceUnitary(double d) { mdElektraPartPriceUnitary = d; }
    public void setJsonData(java.lang.String s) { msJsonData = s; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getBachocoNumeroPosicion() { return mnBachocoNumeroPosicion; }
    public java.lang.String getBachocoCentro() { return msBachocoCentro; }
    public int getLorealEntryNumber() { return mnLorealEntryNumber; }
    public java.lang.String getSorianaCodigo() { return msSorianaCodigo; }
    public java.lang.String getElektraOrder() { return msElektraOrder; }
    public java.lang.String getElektraBarcode() { return msElektraBarcode; }
    public int getElektraCages() { return mnElektraCages; }
    public double getElektraCagePriceUnitary() { return mdElektraCagePriceUnitary; }
    public int getElektraParts() { return mnElektraParts; }
    public double getElektraPartPriceUnitary() { return mdElektraPartPriceUnitary; }
    public java.lang.String getJsonData() { return msJsonData; }

    @Override
    public void setPrimaryKey(Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkEntryId = ((int[]) pk)[2];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        mnBachocoNumeroPosicion = 0;
        msBachocoCentro = "";
        mnLorealEntryNumber = 0;
        msSorianaCodigo = "";
        msElektraOrder = "";
        msElektraBarcode = "";
        mnElektraCages = 0;
        mdElektraCagePriceUnitary = 0;
        mnElektraParts = 0;
        mdElektraPartPriceUnitary = 0;
        msJsonData = "";
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_dps_add_ety WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " AND id_ety = " + key[2] +  " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                mnPkEntryId = resultSet.getInt("id_ety");
                mnBachocoNumeroPosicion = resultSet.getInt("bac_num_pos");
                msBachocoCentro = resultSet.getString("bac_cen");
                mnLorealEntryNumber = resultSet.getInt("lor_num_ety");
                msSorianaCodigo = resultSet.getString("sor_cod");
                msElektraOrder = resultSet.getString("ele_ord");
                msElektraBarcode = resultSet.getString("ele_barc");
                mnElektraCages = resultSet.getInt("ele_cag");
                mdElektraCagePriceUnitary = resultSet.getDouble("ele_cag_price_u");
                mnElektraParts = resultSet.getInt("ele_par");
                mdElektraPartPriceUnitary = resultSet.getDouble("ele_par_price_u");
                msJsonData = resultSet.getString("json_data");
            }

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
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
    public int save(Connection connection) {
        String sql = "";
        Statement statement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            statement = connection.createStatement();

            sql = "DELETE FROM trn_dps_add_ety " +
                    "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + ";";
            
            statement.execute(sql);

            sql = "INSERT INTO trn_dps_add_ety (" +
                    "id_year, id_doc, id_ety, " +
                    "bac_num_pos, bac_cen, lor_num_ety, sor_cod, " +
                    "ele_ord, ele_barc, " +
                    "ele_cag, ele_cag_price_u, ele_par, ele_par_price_u, " +
                    "json_data) " +
                    "VALUES (" + 
                    mnPkYearId + ", " + mnPkDocId + ", " + mnPkEntryId + ", " +
                    mnBachocoNumeroPosicion + ", '" + msBachocoCentro + "', " + mnLorealEntryNumber + ", '" + msSorianaCodigo + "', " +
                    "'" + msElektraOrder + "', '" + msElektraBarcode + "', " + 
                    mnElektraCages + ", " + mdElektraCagePriceUnitary + ", " + mnElektraParts + ", " + mdElektraPartPriceUnitary + ", " +
                    "'" + msJsonData + "');";
            
            statement.execute(sql);

            statement.close();

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int delete(Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            String sql = "DELETE FROM trn_dps_add_ety " +
                    "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + ";";
            
            connection.createStatement().execute(sql);

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}
