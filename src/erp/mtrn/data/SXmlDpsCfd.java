/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sa.lib.xml.SXmlAttribute;
import sa.lib.xml.SXmlDocument;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author Juan Barajas
 */
public class SXmlDpsCfd extends SXmlDocument {

    public static final String NAME = "Dps";
    public static final String ATT_CONF = "confirmacion";
    public static final String ATT_TP_REL = "tipoRelacion";
    public static final String ATT_USO_CFDI = "usoCFDI";
    
    public static final String ATT_MOT_TRAS = "MotivoTraslado";
    public static final String ATT_TP_OPE = "TipoOperacion";
    public static final String ATT_CVE_PED = "ClaveDePedimento";
    public static final String ATT_CERT_ORIG = "CertificadoOrigen";
    public static final String ATT_NUM_CERT_ORIG = "NumCertificadoOrigen";
    public static final String ATT_SUB = "Subdivision";
    public static final String ATT_TP_CAMB = "TipoCambioUSD";
    public static final String ATT_TOT_USD = "TotalUSD";
    public static final String ATT_NUM_EXP_CONF = "NumExportadorConfiable";

    protected SXmlAttribute moConfirmation;
    protected SXmlAttribute moRelationType;
    protected SXmlAttribute moCfdiUse;
    
    protected SXmlAttribute moCfdCceMotivoTraslado;
    protected SXmlAttribute moCfdCceTipoOperacion;
    protected SXmlAttribute moCfdCceClaveDePedimento;
    protected SXmlAttribute moCfdCceCertificadoOrigen;
    protected SXmlAttribute moCfdCceNumCertificadoOrigen;
    protected SXmlAttribute moCfdCceSubdivision;
    protected SXmlAttribute moCfdCceTipoCambioUSD;
    protected SXmlAttribute moCfdCceTotalUSD;
    protected SXmlAttribute moCfdCceNumExportadorConfiable;
    
    public SXmlDpsCfd() {
        super(NAME);

        moConfirmation = new SXmlAttribute(ATT_CONF);
        moRelationType = new SXmlAttribute(ATT_TP_REL);
        moCfdiUse = new SXmlAttribute(ATT_USO_CFDI);
        
        moCfdCceMotivoTraslado = new SXmlAttribute(ATT_MOT_TRAS);
        moCfdCceTipoOperacion = new SXmlAttribute(ATT_TP_OPE);
        moCfdCceClaveDePedimento = new SXmlAttribute(ATT_CVE_PED);
        moCfdCceCertificadoOrigen = new SXmlAttribute(ATT_CERT_ORIG);
        moCfdCceNumCertificadoOrigen = new SXmlAttribute(ATT_NUM_CERT_ORIG);
        moCfdCceSubdivision = new SXmlAttribute(ATT_SUB);
        moCfdCceTipoCambioUSD = new SXmlAttribute(ATT_TP_CAMB);
        moCfdCceTotalUSD = new SXmlAttribute(ATT_TOT_USD);
        moCfdCceNumExportadorConfiable = new SXmlAttribute(ATT_NUM_EXP_CONF);

        mvXmlAttributes.add(moConfirmation);
        mvXmlAttributes.add(moRelationType);
        mvXmlAttributes.add(moCfdiUse);
        
        mvXmlAttributes.add(moCfdCceMotivoTraslado);
        mvXmlAttributes.add(moCfdCceTipoOperacion);
        mvXmlAttributes.add(moCfdCceClaveDePedimento);
        mvXmlAttributes.add(moCfdCceCertificadoOrigen);
        mvXmlAttributes.add(moCfdCceNumCertificadoOrigen);
        mvXmlAttributes.add(moCfdCceSubdivision);
        mvXmlAttributes.add(moCfdCceTipoCambioUSD);
        mvXmlAttributes.add(moCfdCceTotalUSD);
        mvXmlAttributes.add(moCfdCceNumExportadorConfiable);
    }

    @Override
    public void processXml(String xml) throws Exception {
        Document document = null;
        NodeList nodeList = null;
        Vector<Node> childNodes = null;
        NamedNodeMap namedNodeMap = null;

        clear();

        document = SXmlUtils.parseDocument(xml);
        nodeList = SXmlUtils.extractElements(document, SXmlDpsCfd.NAME);
        
        // Attributes layout
        
        moConfirmation.setValue(nodeList.item(0).getAttributes().getNamedItem(ATT_CONF).getNodeValue());
        moRelationType.setValue(nodeList.item(0).getAttributes().getNamedItem(ATT_TP_REL).getNodeValue());
        moCfdiUse.setValue(nodeList.item(0).getAttributes().getNamedItem(ATT_USO_CFDI).getNodeValue());
        
        mvXmlAttributes.add(moConfirmation);
        mvXmlAttributes.add(moRelationType);
        mvXmlAttributes.add(moCfdiUse);
        
        childNodes = SXmlUtils.extractChildElements(nodeList.item(0), "cce");
        for (Node child : childNodes) {
            namedNodeMap = child.getAttributes();
            
            moCfdCceMotivoTraslado.setValue(namedNodeMap.getNamedItem(SXmlDpsCfd.ATT_MOT_TRAS).getNodeValue());
            moCfdCceTipoOperacion.setValue(namedNodeMap.getNamedItem(SXmlDpsCfd.ATT_TP_OPE).getNodeValue());
            moCfdCceClaveDePedimento.setValue(namedNodeMap.getNamedItem(SXmlDpsCfd.ATT_CVE_PED).getNodeValue());
            moCfdCceCertificadoOrigen.setValue(namedNodeMap.getNamedItem(SXmlDpsCfd.ATT_CERT_ORIG).getNodeValue());
            moCfdCceNumCertificadoOrigen.setValue(namedNodeMap.getNamedItem(SXmlDpsCfd.ATT_NUM_CERT_ORIG).getNodeValue());
            moCfdCceSubdivision.setValue(namedNodeMap.getNamedItem(SXmlDpsCfd.ATT_SUB).getNodeValue());
            moCfdCceTipoCambioUSD.setValue(namedNodeMap.getNamedItem(SXmlDpsCfd.ATT_TP_CAMB).getNodeValue());
            moCfdCceTotalUSD.setValue(namedNodeMap.getNamedItem(SXmlDpsCfd.ATT_TOT_USD).getNodeValue());
            moCfdCceNumExportadorConfiable.setValue(namedNodeMap.getNamedItem(SXmlDpsCfd.ATT_NUM_EXP_CONF).getNodeValue());
        }
        
        mvXmlAttributes.add(moCfdiUse);
    }
}
