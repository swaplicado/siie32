/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mcfg.data;

import erp.cfd.SCceEmisorAddressAux;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import sa.lib.xml.SXmlUtils;

/**
 *
* @author Juan Barajas, Sergio Flores
 */
public class SDataCfgCfd extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkCfgId;
    protected java.lang.String msXml;
    
    protected java.lang.String msCfdRegimenFiscal;
    protected java.lang.String msCfdUsoCFDI;
    
    protected ArrayList<SCceEmisorAddressAux> maCceEmisorAddress;

    public SDataCfgCfd() {
        super(SModConsts.CFGU_CO);
        reset();
    }

    public void setXml(java.lang.String s) { msXml = s; }
    
    public java.lang.String getXml() { return msXml; }
    
    /**
     * Gets default value for CFDI node 'Emisor's attribute 'RegimenFiscal'.
     * @return 
     */
    public java.lang.String getCfdRegimenFiscal() { return msCfdRegimenFiscal; }
    
    /**
     * Gets default value for CFDI node 'Receptor's attribute 'UsoCFDI'.
     * @return 
     */
    public java.lang.String getCfdUsoCFDI() { return msCfdUsoCFDI; }

    public ArrayList<SCceEmisorAddressAux> getCceEmisorAddressAux() { return maCceEmisorAddress; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkCfgId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkCfgId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkCfgId = 0;
        msXml = "";
        
        msCfdRegimenFiscal = "";
        msCfdUsoCFDI = "";
        
        maCceEmisorAddress =  new ArrayList<SCceEmisorAddressAux>();
    }

    @Override
    public int read(java.lang.Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM cfg_cfd WHERE id_cfg = " + key[0] + " ";
            
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkCfgId = resultSet.getInt("id_cfg");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return new Date();
    }
    
    public void processXml(final String xml, final Statement statement) throws Exception {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        Node node = SXmlUtils.extractElements(doc, "CfgCfd").item(0);
        NamedNodeMap namedNodeMap = node.getAttributes();

        msCfdRegimenFiscal = SXmlUtils.extractAttributeValue(namedNodeMap, "RegimenFiscal", true);
        msCfdUsoCFDI = SXmlUtils.extractAttributeValue(namedNodeMap, "UsoCFDI", true);
        
        // Emisor:

        if (SXmlUtils.hasChildElement(node, "cce11:Emisor")) {
            Node nodeChild = SXmlUtils.extractChildElements(node, "cce11:Emisor").get(0);
            
            // Address emisor:
            
            if (SXmlUtils.hasChildElement(nodeChild, "cce11:Domicilio")) {
                Vector<Node> elements = SXmlUtils.extractChildElements(nodeChild, "cce11:Domicilio");
                for (Node element : elements) {
                    NamedNodeMap namedNodeMapChild = element.getAttributes();
                    
                    maCceEmisorAddress.add(new SCceEmisorAddressAux(
                            SXmlUtils.extractAttributeValue(namedNodeMapChild, "CodigoPostal", true),
                            SXmlUtils.extractAttributeValue(namedNodeMapChild, "Colonia", true),
                            SXmlUtils.extractAttributeValue(namedNodeMapChild, "Localidad", true),
                            SXmlUtils.extractAttributeValue(namedNodeMapChild, "Municipio", true)));
                }
            }
        }
    }
}
