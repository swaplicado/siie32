/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import sa.lib.xml.SXmlUtils;

/**
 *
* @author Juan Barajas
 */
public class SDataDpsCfd extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected java.lang.String msXml;
    
    protected java.lang.String msCfdConfirmacion;
    protected java.lang.String msCfdTipoRelacion;
    protected java.lang.String msCfdUsoCfdi;

    public SDataDpsCfd() {
        super(SDataConstants.TRN_CTR);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setXml(java.lang.String s) { msXml = s; }
    
    public void setCfdConfirmacion(java.lang.String s) { msCfdConfirmacion = s; }
    public void setCfdTipoRelacion(java.lang.String s) { msCfdTipoRelacion = s; }
    public void setCfdUsoCfdi(java.lang.String s) { msCfdUsoCfdi = s; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public java.lang.String getXml() { return msXml; }
    
    public java.lang.String getCfdConfirmacion() { return msCfdConfirmacion; }
    public java.lang.String getCfdTipoRelacion() { return msCfdTipoRelacion; }
    public java.lang.String getCfdUsoCfdi() { return msCfdUsoCfdi; }

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
        msXml = "";
        
        msCfdConfirmacion = "";
        msCfdTipoRelacion = "";
        msCfdUsoCfdi = "";
    }

    @Override
    public int read(java.lang.Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_dps_cfd WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " ";
            
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                msXml = resultSet.getString("xml");
                
                processXml(msXml, statement);
                
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
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbIsRegistryNew) {
                sql = "SELECT COUNT(*) FROM trn_dps_cfd WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
                resultSet = connection.createStatement().executeQuery(sql);

                if (resultSet.next()) {
                    mbIsRegistryNew = resultSet.getInt(1) == 0;
                }
            }
            
            computeXml();

            if (mbIsRegistryNew) {
                sql = "INSERT INTO trn_dps_cfd VALUES (" +
                        mnPkYearId + ", " +
                        mnPkDocId + ", " +
                        "'" + msXml + "' " +
                        ")";
            }
            else {

                sql = "UPDATE trn_dps_cfd SET " +
                        //"id_year = " + mnPkYearId + ", " +
                        //"id_doc = " + mnPkDocId + ", " +
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
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return new Date();
    }
    
    private void computeXml() throws Exception {
        SXmlDpsCfd dpsXml = new SXmlDpsCfd();
        
        dpsXml.getAttribute(SXmlDpsCfd.ATT_CONF).setValue(msCfdConfirmacion);
        dpsXml.getAttribute(SXmlDpsCfd.ATT_TP_REL).setValue(msCfdTipoRelacion);
        dpsXml.getAttribute(SXmlDpsCfd.ATT_USO_CFDI).setValue(msCfdUsoCfdi);
        
        msXml = dpsXml.getXmlString();
    }
    
    public void processXml(final String xml, final Statement statement) throws Exception {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        Node node = null;
        NamedNodeMap namedNodeMap = null;
        
        node = SXmlUtils.extractElements(doc, "Dps").item(0);
        namedNodeMap = node.getAttributes();

        msCfdConfirmacion = SXmlUtils.extractAttributeValue(namedNodeMap, "confirmacion", false);
        msCfdTipoRelacion = SXmlUtils.extractAttributeValue(namedNodeMap, "tipoRelacion", false);
        msCfdUsoCfdi = SXmlUtils.extractAttributeValue(namedNodeMap, "usoCFDI", false);
    }
}
