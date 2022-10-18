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
 * @author Juan Barajas, Sergio Flores, Isabel Servín
 */
public class SXmlDpsCfd extends SXmlDocument {

    public static final String NAME = "Dps";
    
    private boolean mbAvailableCfdiRelacionados;
    private boolean mbAvailableIntCommerce;
    private final ArrayList<DElement> maElements;

    public SXmlDpsCfd() {
        super(NAME);
        mbAvailableCfdiRelacionados = false;
        mbAvailableIntCommerce = false;
        maElements = new ArrayList<>();
    }

    public boolean isAvailableCfdiRelacionados() { return mbAvailableCfdiRelacionados; }
    public boolean isAvailableIntCommerce() { return mbAvailableIntCommerce; }
    public ArrayList<DElement> getElements() { return maElements; }
    
    @Override
    public void processXml(String xml) throws Exception {
        clear();
        mbAvailableCfdiRelacionados = false;
        mbAvailableIntCommerce = false;
        maElements.clear();

        // start parsing document:
        Document document = SXmlUtils.parseDocument(xml);
        NodeList nodeList = SXmlUtils.extractElements(document, SXmlDpsCfd.NAME);
        
        // parse CfdiRelacionados node:
        String cfdiRelacionadosName = new DElementCfdiRelacionados().getName();
        if (SXmlUtils.hasChildElement(nodeList.item(0), cfdiRelacionadosName)) {
            Vector<Node> cfdiRelacionadosNodes = SXmlUtils.extractChildElements(nodeList.item(0), cfdiRelacionadosName);
            for (Node node : cfdiRelacionadosNodes) {
                NamedNodeMap cfdiRelacionadosNodesMap = node.getAttributes();
                DElementCfdiRelacionados cfdiRelacionados = new DElementCfdiRelacionados();
                cfdiRelacionados.getAttTipoRelacion().setString(SXmlUtils.extractAttributeValue(cfdiRelacionadosNodesMap, "TipoRelacion", true));
                
                String cfdiRelacionadoName = new DElementCfdiRelacionado().getName();
                if (SXmlUtils.hasChildElement(node, cfdiRelacionadoName)) {
                    Vector<Node> cfdiRelacionadoNodes = SXmlUtils.extractChildElements(node, cfdiRelacionadoName);

                    for (Node cfdiRelacionadoNode : cfdiRelacionadoNodes) {
                        NamedNodeMap cfdiRelacionadoNodesMap = cfdiRelacionadoNode.getAttributes();

                        DElementCfdiRelacionado cfdiRelacionado = new DElementCfdiRelacionado();
                        cfdiRelacionado.getAttUuid().setString(SXmlUtils.extractAttributeValue(cfdiRelacionadoNodesMap, "UUID", true));

                        cfdiRelacionados.getEltCfdiRelacionados().add(cfdiRelacionado);
                    }
                }

                maElements.add(cfdiRelacionados);
            }
            
            mbAvailableCfdiRelacionados = true;
        }
        
        // parse International Commerce node:
        if (SXmlUtils.hasChildElement(nodeList.item(0), SXmlDpsCfdCce.NAME)) {
            Vector<Node> childNodes = SXmlUtils.extractChildElements(nodeList.item(0), SXmlDpsCfdCce.NAME);
            
            NamedNodeMap namedNodeMap = childNodes.get(0).getAttributes();

            SXmlDpsCfdCce dpsCfdCce = new SXmlDpsCfdCce();
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_MOT_TRAS).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlDpsCfdCce.ATT_MOT_TRAS, true));
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_TP_OPE).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlDpsCfdCce.ATT_TP_OPE, true));
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_CVE_PED).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlDpsCfdCce.ATT_CVE_PED, true));
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_CERT_ORIG).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlDpsCfdCce.ATT_CERT_ORIG, true));
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_NUM_CERT_ORIG).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlDpsCfdCce.ATT_NUM_CERT_ORIG, true));
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_SUB).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlDpsCfdCce.ATT_SUB, true));
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_TP_CAMB).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlDpsCfdCce.ATT_TP_CAMB, true));
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_TOT_USD).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlDpsCfdCce.ATT_TOT_USD, true));
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_NUM_EXP_CONF).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlDpsCfdCce.ATT_NUM_EXP_CONF, true));
            dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_INCOTERM).setValue(SXmlUtils.extractAttributeValue(namedNodeMap, SXmlDpsCfdCce.ATT_INCOTERM, true));

            mvXmlElements.add(dpsCfdCce);
            mbAvailableIntCommerce = true;
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
