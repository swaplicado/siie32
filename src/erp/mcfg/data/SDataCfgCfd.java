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
* @author Juan Barajas
 */
public class SDataCfgCfd extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkCfgId;
    protected java.lang.String msXml;
    
    protected java.lang.String msCfdUsoCfdi;
    
    protected ArrayList<SCceEmisorAddressAux> maCceEmisorAddress;

    public SDataCfgCfd() {
        super(SModConsts.CFGU_CO);
        reset();
    }

    public void setXml(java.lang.String s) { msXml = s; }
    
    public void setCfdUsoCfdi(java.lang.String s) { msCfdUsoCfdi = s; }

    public java.lang.String getXml() { return msXml; }
    
    public java.lang.String getCfdUsoCfdi() { return msCfdUsoCfdi; }

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
        
        msCfdUsoCfdi = "";
        
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
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbIsRegistryNew) {
                sql = "SELECT COUNT(*) FROM cfg_cfd WHERE id_year = " + mnPkCfgId + " ";
                resultSet = connection.createStatement().executeQuery(sql);

                if (resultSet.next()) {
                    mbIsRegistryNew = resultSet.getInt(1) == 0;
                }
            }

            if (mbIsRegistryNew) {
                sql = "INSERT INTO cfg_cfd VALUES (" +
                        mnPkCfgId + ", " +
                        "'" + msXml + "' " +
                        ")";
            }
            else {

                sql = "UPDATE trn_dps_cfd SET " +
                        //"id_cfg = " + mnPkCfgId + ", " +
                        "xml = '" + msXml + "' " +
                        "WHERE id_cfg = " + mnPkCfgId + " ";
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
    
    public void processXml(final String xml, final Statement statement) throws Exception {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        Node node = null;
        NamedNodeMap namedNodeMap = null;
        Node nodeChild = null;
        NamedNodeMap namedNodeMapChild = null;
        Vector<Node> nodeChilds = null;
        String cfdCceEmisorCodigoPostal;
        String cfdCceEmisorColonia;
        String cfdCceEmisorLocalidad;
        String cfdCceEmisorMunicipio;
        
        node = SXmlUtils.extractElements(doc, "CfgCfd").item(0);
        namedNodeMap = node.getAttributes();

        msCfdUsoCfdi = SXmlUtils.extractAttributeValue(namedNodeMap, "usoCFDI", false);
        
        // Emisor:

        if (SXmlUtils.hasChildElement(node, "cce11:Emisor")) {
            nodeChild = SXmlUtils.extractChildElements(node, "cce11:Emisor").get(0);
            
            // Address emisor:
            
            if (SXmlUtils.hasChildElement(nodeChild, "cce11:Domicilio")) {
                nodeChilds = SXmlUtils.extractChildElements(nodeChild, "cce11:Domicilio");

                for (int i = 0; i < nodeChilds.size(); i++) {
                    namedNodeMapChild = nodeChilds.get(i).getAttributes();
                    
                    cfdCceEmisorCodigoPostal = SXmlUtils.extractAttributeValue(namedNodeMapChild, "CodigoPostal", false);
                    cfdCceEmisorColonia = SXmlUtils.extractAttributeValue(namedNodeMapChild, "Colonia", false);
                    cfdCceEmisorLocalidad = SXmlUtils.extractAttributeValue(namedNodeMapChild, "Localidad", false);
                    cfdCceEmisorMunicipio = SXmlUtils.extractAttributeValue(namedNodeMapChild, "Municipio", false);
                    
                    maCceEmisorAddress.add(new SCceEmisorAddressAux(cfdCceEmisorCodigoPostal, cfdCceEmisorColonia, cfdCceEmisorLocalidad, cfdCceEmisorMunicipio));
                }
            }
        }
    }
}
