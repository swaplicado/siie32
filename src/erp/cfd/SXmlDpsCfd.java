/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.cfd;

import cfd.DElement;
import cfd.ver33.DElementCfdiRelacionado;
import cfd.ver33.DElementCfdiRelacionados;
import java.util.ArrayList;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sa.lib.SLibUtils;
import sa.lib.xml.SXmlDocument;
import sa.lib.xml.SXmlUtils;

/**
 * Class for handling additional information for the CFDI in XML format.
 * @author Juan Barajas, Sergio Flores
 */
public class SXmlDpsCfd extends SXmlDocument {

    public static final String NAME = "Dps";
    
    private boolean mbAvailableCfdiRelacionados;
    private boolean mbAvailableCce;
    private final ArrayList<DElement> maElements;

    public SXmlDpsCfd() {
        super(NAME);
        mbAvailableCfdiRelacionados = false;
        mbAvailableCce = false;
        maElements = new ArrayList<>();
    }

    public boolean isAvailableCfdiRelacionados() { return mbAvailableCfdiRelacionados; }
    public boolean isAvailableCce() { return mbAvailableCce; }
    public ArrayList<DElement> getElements() { return maElements; }
    
    @Override
    public void processXml(String xml) throws Exception {
        clear();
        mbAvailableCfdiRelacionados = false;
        mbAvailableCce = false;
        maElements.clear();

        // start parsing document:
        Document document = SXmlUtils.parseDocument(xml);
        NodeList nodeList = SXmlUtils.extractElements(document, SXmlDpsCfd.NAME);
        
        // parse CfdiRelacionados node:
        String cfdiRelacionadosName = new DElementCfdiRelacionados().getName();
        if (SXmlUtils.hasChildElement(nodeList.item(0), cfdiRelacionadosName)) {
            Vector<Node> cfdiRelacionadosNodes = SXmlUtils.extractChildElements(nodeList.item(0), cfdiRelacionadosName);
            
            NamedNodeMap cfdiRelacionadosNodesMap = cfdiRelacionadosNodes.get(0).getAttributes();

            DElementCfdiRelacionados cfdiRelacionados = new DElementCfdiRelacionados();
            cfdiRelacionados.getAttTipoRelacion().setString(cfdiRelacionadosNodesMap.getNamedItem("TipoRelacion").getNodeValue());
            
            String cfdiRelacionadoName = new DElementCfdiRelacionado().getName();
            if (SXmlUtils.hasChildElement(cfdiRelacionadosNodes.get(0), cfdiRelacionadoName)) {
                Vector<Node> cfdiRelacionadoNodes = SXmlUtils.extractChildElements(cfdiRelacionadosNodes.get(0), cfdiRelacionadoName);
                
                for (Node cfdiRelacionadoNode : cfdiRelacionadoNodes) {
                    NamedNodeMap cfdiRelacionadoNodesMap = cfdiRelacionadoNode.getAttributes();

                    DElementCfdiRelacionado cfdiRelacionado = new DElementCfdiRelacionado();
                    cfdiRelacionado.getAttUuid().setString(cfdiRelacionadoNodesMap.getNamedItem("UUID").getNodeValue());
                    
                    cfdiRelacionados.getEltCfdiRelacionados().add(cfdiRelacionado);
                }
            }

            maElements.add(cfdiRelacionados);
            mbAvailableCfdiRelacionados = true;
        }
        
        // parse International Commerce node:
        if (SXmlUtils.hasChildElement(nodeList.item(0), SXmlDpsCfdCce.NAME)) {
            Vector<Node> childNodes = SXmlUtils.extractChildElements(nodeList.item(0), SXmlDpsCfdCce.NAME);
            
            NamedNodeMap namedNodeMap = childNodes.get(0).getAttributes();

            SXmlDpsCfdCce dpsCfdCce = new SXmlDpsCfdCce();
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_MOT_TRAS).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_MOT_TRAS).getNodeValue());
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_TP_OPE).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_TP_OPE).getNodeValue());
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_CVE_PED).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_CVE_PED).getNodeValue());
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_CERT_ORIG).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_CERT_ORIG).getNodeValue());
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_NUM_CERT_ORIG).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_NUM_CERT_ORIG).getNodeValue());
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_SUB).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_SUB).getNodeValue());
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_TP_CAMB).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_TP_CAMB).getNodeValue());
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_TOT_USD).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_TOT_USD).getNodeValue());
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_NUM_EXP_CONF).setValue(namedNodeMap.getNamedItem(SXmlDpsCfdCce.ATT_NUM_EXP_CONF).getNodeValue());

            mvXmlElements.add(dpsCfdCce);
            mbAvailableCce = true;
        }
    }
    
    @Override
    public String getXmlString() {
        String aux = "";
        String tag = "\n</" + NAME + ">";
        String xml = super.getXmlString();  // member maElements is not included!
        
        xml = xml.substring(0, xml.indexOf(tag));   // remove ending tag to include member maElements
        
        // include member maElements:
        try {
            for (DElement element : maElements) {
                aux = element.getElementForXml();
                xml += aux.isEmpty() ? "" : "\n" + aux;
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        xml += tag; // add ending tag again
        
        return xml;
    }
}
