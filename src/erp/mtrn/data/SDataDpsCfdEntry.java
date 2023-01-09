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
import java.util.Date;

/**
* Handling additional information for CFDI entry.
* @author Sergio Flores, Isabel Servín
 */
public class SDataDpsCfdEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected java.lang.String msConceptKey;
    protected java.lang.String msConcept;
    protected java.lang.String msCfdProdServ;
    protected java.lang.String msCfdUnit;
    protected java.lang.String msTaxObject;
    protected java.lang.String msPredial;
    protected java.lang.String msXml;
    
    public SDataDpsCfdEntry() {
        super(SDataConstants.TRN_DPS_CFD_ETY);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setConceptKey(java.lang.String s) { msConceptKey = s; }
    public void setConcept(java.lang.String s) { msConcept = s; }
    public void setCfdProdServ(java.lang.String s) { msCfdProdServ = s; }
    public void setCfdUnit(java.lang.String s) { msCfdUnit = s; }
    public void setTaxObject(java.lang.String s) { msTaxObject = s; }
    public void setPredial(java.lang.String s) { msPredial = s; }
    public void setXml(java.lang.String s) { msXml = s; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public java.lang.String getConceptKey() { return msConceptKey; }
    public java.lang.String getConcept() { return msConcept; }
    public java.lang.String getCfdProdServ() { return msCfdProdServ; }
    public java.lang.String getCfdUnit() { return msCfdUnit; }
    public java.lang.String getTaxObject() { return msTaxObject; }
    public java.lang.String getPredial() { return msPredial; }
    public java.lang.String getXml() { return msXml; }

    /**
     * Generate the XML with infromation aditional for CFDI.
     * @throws Exception 
     */
    private void computeXml() throws Exception {
        
    }
    
    /**
     * Load values in fields from XML.
     * @throws Exception 
     */
    private void processXml(final String xml) throws Exception {
        
    }
    
    
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
        msConceptKey = "";
        msConcept = "";
        msCfdProdServ = "";
        msCfdUnit = "";
        msTaxObject = "";
        msPredial = "";
        msXml = "";
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * "
                    + "FROM trn_dps_cfd_ety "
                    + "WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " AND id_ety = " + key[2] + " ";
            
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
                msTaxObject = resultSet.getString("tax_object");
                msPredial = resultSet.getString("predial");
                msXml = resultSet.getString("xml");
                
                processXml(msXml);
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        String sql;
        ResultSet resultSet;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (!mbIsRegistryNew) {
                sql = "SELECT COUNT(*) "
                        + "FROM trn_dps_cfd_ety "
                        + "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId;
                resultSet = connection.createStatement().executeQuery(sql);

                if (resultSet.next()) {
                    mbIsRegistryNew = resultSet.getInt(1) == 0;
                }
            }
            
            computeXml();

            if (mbIsRegistryNew) {
                sql = "INSERT INTO trn_dps_cfd_ety VALUES (" +
                        mnPkYearId + ", " +
                        mnPkDocId + ", " +
                        mnPkEntryId + ", " +
                        "'" + msConceptKey + "', " +
                        "'" + msConcept + "', " +
                        "'" + msCfdProdServ + "', " +
                        "'" + msCfdUnit + "', " +
                        "'" + msTaxObject + "', " +
                        "'" + msPredial + "', " +
                        "'" + msXml + "' " +
                        ")";
            }
            else {
                sql = "UPDATE trn_dps_cfd_ety SET " +
                        //"id_year = " + mnPkYearId + ", " +
                        //"id_doc = " + mnPkDocId + ", " +
                        //"id_ety = " + mnPkEntryId + ", " +
                        "concept_key='" + msConceptKey + "', " +
                        "concept='" + msConcept + "', " +
                        "cfd_prod_serv='" + msCfdProdServ + "', " +
                        "cfd_unit='" + msCfdUnit + "', " +
                        "tax_object='" + msTaxObject + "', " +
                        "predial='" + msPredial + "', " +
                        "xml = '" + msXml + "' " +
                        "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
            }

            connection.createStatement().execute(sql);
            
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return new Date();  // XXX check this! (Sergio Flores, 2017-08-08)
    }
    
    @Override
    public erp.mtrn.data.SDataDpsCfdEntry clone() {
        SDataDpsCfdEntry clone = new SDataDpsCfdEntry();

        clone.setIsRegistryNew(mbIsRegistryNew);

        clone.setPkYearId(mnPkYearId);
        clone.setPkDocId(mnPkDocId);
        clone.setPkEntryId(mnPkEntryId);
        clone.setConceptKey(msConceptKey);
        clone.setConcept(msConcept);
        clone.setCfdProdServ(msCfdProdServ);
        clone.setCfdUnit(msCfdUnit);
        clone.setTaxObject(msTaxObject);
        clone.setPredial(msPredial);
        clone.setXml(msXml);

        return clone;
    }
}
