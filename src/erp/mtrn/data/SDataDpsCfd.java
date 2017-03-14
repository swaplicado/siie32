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
* Class for handling additional information for the CFDI.
* @author Juan Barajas
 */
public class SDataDpsCfd extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected String msXml;
    
    protected String msCfdConfirmacion;
    protected String msCfdTipoRelacion;
    protected String msCfdUsoCfdi;
    
    protected String msCfdCceMotivoTraslado;
    protected String msCfdCceTipoOperacion;
    protected String msCfdCceClaveDePedimento;
    protected String msCfdCceCertificadoOrigen; 
    protected String msCfdCceNumCertificadoOrigen;
    protected String msCfdCceSubdivision;
    protected String msCfdCceTipoCambioUSD;
    protected String msCfdCceTotalUSD;
    protected String msCfdCceNumExportadorConfiable;
    
    protected boolean mbHasInternationalTradeNode;

    public SDataDpsCfd() {
        super(SDataConstants.TRN_DPS_CFD);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setXml(String s) { msXml = s; }
    
    public void setCfdConfirmacion(String s) { msCfdConfirmacion = s; }
    public void setCfdTipoRelacion(String s) { msCfdTipoRelacion = s; }
    public void setCfdUsoCfdi(String s) { msCfdUsoCfdi = s; }
    
    public void setCfdCceMotivoTraslado(String s) { msCfdCceMotivoTraslado = s; }
    public void setCfdCceTipoOperacion(String s) { msCfdCceTipoOperacion = s; }
    public void setCfdCceClaveDePedimento(String s) { msCfdCceClaveDePedimento = s; }
    public void setCfdCceCertificadoOrigen(String s) { msCfdCceCertificadoOrigen = s; }
    public void setCfdCceNumCertificadoOrigen(String s) { msCfdCceNumCertificadoOrigen = s; }
    public void setCfdCceSubdivision(String s) { msCfdCceSubdivision = s; }
    public void setCfdCceTipoCambioUSD(String s) { msCfdCceTipoCambioUSD = s; }
    public void setCfdCceTotalUSD(String s) { msCfdCceTotalUSD = s; }
    public void setCfdCceNumExportadorConfiable(String s) { msCfdCceNumExportadorConfiable = s; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public String getXml() { return msXml; }
    
    public String getCfdConfirmacion() { return msCfdConfirmacion; }
    public String getCfdTipoRelacion() { return msCfdTipoRelacion; }
    public String getCfdUsoCfdi() { return msCfdUsoCfdi; }
    
    public String getCfdCceMotivoTraslado() { return msCfdCceMotivoTraslado; }
    public String getCfdCceTipoOperacion() { return msCfdCceTipoOperacion; }
    public String getCfdCceClaveDePedimento() { return msCfdCceClaveDePedimento; }
    public String getCfdCceCertificadoOrigen() { return msCfdCceCertificadoOrigen; }
    public String getCfdCceNumCertificadoOrigen() { return msCfdCceNumCertificadoOrigen; }
    public String getCfdCceSubdivision() { return msCfdCceSubdivision; }
    public String getCfdCceTipoCambioUSD() { return msCfdCceTipoCambioUSD; }
    public String getCfdCceTotalUSD() { return msCfdCceTotalUSD; }
    public String getCfdCceNumExportadorConfiable() { return msCfdCceNumExportadorConfiable; }
    
    public boolean hasInternationalTradeNode() { return mbHasInternationalTradeNode; }

    /**
     * Generate the XML with infromation aditional for CFDI.
     * @throws Exception 
     */
    private void computeXml() throws Exception {
        SXmlDpsCfd dpsXml = new SXmlDpsCfd();
        SXmlDpsCfdCce cceXml = new SXmlDpsCfdCce();
        
        dpsXml.getAttribute(SXmlDpsCfd.ATT_CONF).setValue(msCfdConfirmacion);
        dpsXml.getAttribute(SXmlDpsCfd.ATT_TP_REL).setValue(msCfdTipoRelacion);
        dpsXml.getAttribute(SXmlDpsCfd.ATT_USO_CFDI).setValue(msCfdUsoCfdi);
        
        if (!msCfdCceTipoOperacion.isEmpty()) {
            // Attributes for international trade:

            cceXml.getAttribute(SXmlDpsCfdCce.ATT_MOT_TRAS).setValue(msCfdCceMotivoTraslado);
            cceXml.getAttribute(SXmlDpsCfdCce.ATT_TP_OPE).setValue(msCfdCceTipoOperacion);
            cceXml.getAttribute(SXmlDpsCfdCce.ATT_CVE_PED).setValue(msCfdCceClaveDePedimento);
            cceXml.getAttribute(SXmlDpsCfdCce.ATT_CERT_ORIG).setValue(msCfdCceCertificadoOrigen);
            cceXml.getAttribute(SXmlDpsCfdCce.ATT_NUM_CERT_ORIG).setValue(msCfdCceNumCertificadoOrigen);
            cceXml.getAttribute(SXmlDpsCfdCce.ATT_SUB).setValue(msCfdCceSubdivision);
            cceXml.getAttribute(SXmlDpsCfdCce.ATT_TP_CAMB).setValue(msCfdCceTipoCambioUSD);
            cceXml.getAttribute(SXmlDpsCfdCce.ATT_TOT_USD).setValue(msCfdCceTotalUSD);
            cceXml.getAttribute(SXmlDpsCfdCce.ATT_NUM_EXP_CONF).setValue(msCfdCceNumExportadorConfiable);

            dpsXml.getXmlElements().add(cceXml);
        }
        msXml = dpsXml.getXmlString();
    }
    
    /**
     * Load values in fields from XML.
     * @throws Exception 
     */
    private void processXml(final String xml) throws Exception {
        Node node = null;
        NamedNodeMap namedNodeMap = null;
        Node nodeChild = null;
        NamedNodeMap namedNodeMapChild = null;
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        
        node = SXmlUtils.extractElements(doc, SXmlDpsCfd.NAME).item(0);
        namedNodeMap = node.getAttributes();

        msCfdConfirmacion = SXmlUtils.extractAttributeValue(namedNodeMap, SXmlDpsCfd.ATT_CONF, false);
        msCfdTipoRelacion = SXmlUtils.extractAttributeValue(namedNodeMap, SXmlDpsCfd.ATT_TP_REL, false);
        msCfdUsoCfdi = SXmlUtils.extractAttributeValue(namedNodeMap, SXmlDpsCfd.ATT_USO_CFDI, false);
        
        // International Trade:

        if (SXmlUtils.hasChildElement(node, SXmlDpsCfdCce.NAME)) {
            nodeChild = SXmlUtils.extractChildElements(node, SXmlDpsCfdCce.NAME).get(0);
            namedNodeMapChild = nodeChild.getAttributes();
            
            mbHasInternationalTradeNode = true;
            
            msCfdCceMotivoTraslado = SXmlUtils.extractAttributeValue(namedNodeMapChild, SXmlDpsCfdCce.ATT_MOT_TRAS, false);
            msCfdCceTipoOperacion = SXmlUtils.extractAttributeValue(namedNodeMapChild, SXmlDpsCfdCce.ATT_TP_OPE, false);
            msCfdCceClaveDePedimento = SXmlUtils.extractAttributeValue(namedNodeMapChild, SXmlDpsCfdCce.ATT_CVE_PED, false);
            msCfdCceCertificadoOrigen = SXmlUtils.extractAttributeValue(namedNodeMapChild, SXmlDpsCfdCce.ATT_CERT_ORIG, false);
            msCfdCceNumCertificadoOrigen = SXmlUtils.extractAttributeValue(namedNodeMapChild, SXmlDpsCfdCce.ATT_NUM_CERT_ORIG, false);
            msCfdCceSubdivision = SXmlUtils.extractAttributeValue(namedNodeMapChild, SXmlDpsCfdCce.ATT_SUB, false);
            msCfdCceTipoCambioUSD = SXmlUtils.extractAttributeValue(namedNodeMapChild, SXmlDpsCfdCce.ATT_TP_CAMB, false);
            msCfdCceTotalUSD = SXmlUtils.extractAttributeValue(namedNodeMapChild, SXmlDpsCfdCce.ATT_TOT_USD, false);
            msCfdCceNumExportadorConfiable = SXmlUtils.extractAttributeValue(namedNodeMapChild, SXmlDpsCfdCce.ATT_NUM_EXP_CONF, false);
        }
    }
    
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
    }

    @Override
    public Object getPrimaryKey() {
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
        
        msCfdCceMotivoTraslado = "";
        msCfdCceTipoOperacion = "";
        msCfdCceClaveDePedimento = "";
        msCfdCceCertificadoOrigen = "";
        msCfdCceNumCertificadoOrigen = "";
        msCfdCceSubdivision = "";
        msCfdCceTipoCambioUSD = "";
        msCfdCceTotalUSD = "";
        msCfdCceNumExportadorConfiable = "";
        
        mbHasInternationalTradeNode = false;
    }

    @Override
    public int read(Object pk, Statement statement) {
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
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return new Date();
    }
}
