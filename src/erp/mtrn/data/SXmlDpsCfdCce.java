/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import sa.lib.xml.SXmlAttribute;
import sa.lib.xml.SXmlElement;

/**
 *
 * @author Juan Barajas
 */
public class SXmlDpsCfdCce extends SXmlElement {

    public static final String NAME = "cce11";
    
    public static final String ATT_MOT_TRAS = "MotivoTraslado";
    public static final String ATT_TP_OPE = "TipoOperacion";
    public static final String ATT_CVE_PED = "ClaveDePedimento";
    public static final String ATT_CERT_ORIG = "CertificadoOrigen";
    public static final String ATT_NUM_CERT_ORIG = "NumCertificadoOrigen";
    public static final String ATT_SUB = "Subdivision";
    public static final String ATT_TP_CAMB = "TipoCambioUSD";
    public static final String ATT_TOT_USD = "TotalUSD";
    public static final String ATT_NUM_EXP_CONF = "NumExportadorConfiable";
    
    protected SXmlAttribute moCfdCceMotivoTraslado;
    protected SXmlAttribute moCfdCceTipoOperacion;
    protected SXmlAttribute moCfdCceClaveDePedimento;
    protected SXmlAttribute moCfdCceCertificadoOrigen;
    protected SXmlAttribute moCfdCceNumCertificadoOrigen;
    protected SXmlAttribute moCfdCceSubdivision;
    protected SXmlAttribute moCfdCceTipoCambioUSD;
    protected SXmlAttribute moCfdCceTotalUSD;
    protected SXmlAttribute moCfdCceNumExportadorConfiable;
    
    public SXmlDpsCfdCce() {
        super(NAME);
        
        moCfdCceMotivoTraslado = new SXmlAttribute(ATT_MOT_TRAS);
        moCfdCceTipoOperacion = new SXmlAttribute(ATT_TP_OPE);
        moCfdCceClaveDePedimento = new SXmlAttribute(ATT_CVE_PED);
        moCfdCceCertificadoOrigen = new SXmlAttribute(ATT_CERT_ORIG);
        moCfdCceNumCertificadoOrigen = new SXmlAttribute(ATT_NUM_CERT_ORIG);
        moCfdCceSubdivision = new SXmlAttribute(ATT_SUB);
        moCfdCceTipoCambioUSD = new SXmlAttribute(ATT_TP_CAMB);
        moCfdCceTotalUSD = new SXmlAttribute(ATT_TOT_USD);
        moCfdCceNumExportadorConfiable = new SXmlAttribute(ATT_NUM_EXP_CONF);
        
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
}
