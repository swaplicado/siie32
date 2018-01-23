/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Sergio Flores
 */
public class SDataDpsEntryComplement extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected java.lang.String msConceptKey;
    protected java.lang.String msConcept;
    protected java.lang.String msCfdProdServ;
    protected java.lang.String msCfdUnit;
    
    /**
     * Overrides java.lang.Object.clone() function.
     */
    public SDataDpsEntryComplement() {
        super(SDataConstants.TRN_DPS_ETY_COMPL);

        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setConceptKey(java.lang.String s) { msConceptKey = s; }
    public void setConcept(java.lang.String s) { msConcept = s; }
    public void setCfdProdServ(java.lang.String s) { msCfdProdServ = s; }
    public void setCfdUnit(java.lang.String s) { msCfdUnit = s; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public java.lang.String getConceptKey() { return msConceptKey; }
    public java.lang.String getConcept() { return msConcept; }
    public java.lang.String getCfdProdServ() { return msCfdProdServ; }
    public java.lang.String getCfdUnit() { return msCfdUnit; }

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
        msConceptKey = "";
        msConcept = "";
        msCfdProdServ = "";
        msCfdUnit = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * "
                    + "FROM trn_dps_ety_compl "
                    + "WHERE id_year = " + key[0] + " AND "
                    + "id_doc = " + key[1] + " AND "
                    + "id_ety = " + key[2] +  " ";

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                mnPkEntryId = resultSet.getInt("id_ety");
                msConceptKey = resultSet.getString("concept_key");
                msConcept = resultSet.getString("concept");
                msCfdProdServ = resultSet.getString("cfd_prod_serv");
                msCfdUnit = resultSet.getString("cfd_unit");

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
        int count = 0;
        String sql;
        Statement statement;
        ResultSet resultSet;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            sql = "SELECT COUNT(*) "
                    + "FROM trn_dps_ety_compl "
                    + "WHERE id_year = " + mnPkYearId + " AND "
                    + "id_doc = " + mnPkDocId + " AND "
                    + "id_ety = " + mnPkEntryId + ";";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            
            if (count == 0) {
                sql = "INSERT INTO trn_dps_ety_compl VALUES ("
                        + mnPkYearId + ", "
                        + mnPkDocId + ", "
                        + mnPkEntryId + ", "
                        + "'" + msConceptKey + "', "
                        + "'" + msConcept + "', "
                        + "'" + msCfdProdServ + "', "
                        + "'" + msCfdUnit + "');";
            }
            else {
                sql = "UPDATE trn_dps_ety_compl SET "
                        + "concept_key = '" + msConceptKey + "', "
                        + "concept = '" + msConcept + "', "
                        + "cfd_prod_serv = '" + msCfdProdServ + "', "
                        + "cfd_unit = '" + msCfdUnit + "', "
                        + "WHERE "
                        + "id_year = " + mnPkYearId + " AND "
                        + "id_doc = " + mnPkDocId + " AND "
                        + "id_ety = " + mnPkEntryId + ";";
            }
            
            statement.execute(sql);
            
            mnDbmsErrorId = SLibConstants.UNDEFINED;
            msDbmsError = "";

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
    public java.util.Date getLastDbUpdate() {
        return null;
    }

    @Override
    public erp.mtrn.data.SDataDpsEntryComplement clone() {
        SDataDpsEntryComplement clone = new SDataDpsEntryComplement();

        clone.setIsRegistryNew(mbIsRegistryNew);

        clone.setPkYearId(mnPkYearId);
        clone.setPkDocId(mnPkDocId);
        clone.setPkEntryId(mnPkEntryId);
        clone.setConceptKey(msConceptKey);
        clone.setConcept(msConcept);
        clone.setCfdProdServ(msCfdProdServ);
        clone.setCfdUnit(msCfdUnit);

        return clone;
    }
}
 