/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import cfd.DCfdConsts;
import cfd.DCfdUtils;
import cfd.ver3.DCfdVer3Consts;
import cfd.ver33.DCfdi33Consts;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mcfg.data.SDataCertificate;
import erp.mod.SModSysConsts;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.tempuri.ConsultaCFDIService;
import org.tempuri.IConsultaCFDIService;
import sa.lib.SLibUtils;

/**
 * Manejador de consultas de estatus y CFDI relacionados de CFDI.
 * @author Sergio Flores, Isabel Servín, Sergio Flores
 */
public class SCfdUtilsHandler {
    
    private static final String PADRE = "Padre";
    private static final String HIJO = "Hijo";
    
    private final SClientInterface miClient;
    private String msLastCfdiRelatedError;
    
    /**
     * Construye nuevo manejador.
     * @param client Cliente GUI.
     */
    public SCfdUtilsHandler(SClientInterface client) {
        miClient = client;
    }
    
    /**
     * Devuelve los CFDI relacionados del CFDI.
     * NOTA: Al parecer sólo funciona si el emisor del CFDI es la empresa de la sesión de usuario.
     * @param cfd CFDI.
     * @return
     * @throws Exception
     */
    public ArrayList<CfdiRelated> getCfdiRelated(final SDataCfd cfd) throws Exception {
        return getCfdiRelated(cfd.getFkCfdTypeId(), cfd.getDocXmlRfcEmi(), cfd.getUuid());
    }
    
    /**
     * Devuelve los CFDI relacionados del CFDI.
     * NOTA: Al parecer sólo funciona si el emisor del CFDI es la empresa de la sesión de usuario.
     * @param cfdType Tipo de CFDI, constantes definidas en SDataConstantsSys.TRNS_TP_CFD_...
     * @param rfcEmisor RFC del emisor del CFDI.
     * @param uuid UUID del CFDI.
     * @return
     * @throws Exception
     */
    public ArrayList<CfdiRelated> getCfdiRelated(final int cfdType, final String rfcEmisor, final String uuid) throws Exception {
        msLastCfdiRelatedError = "";
        ArrayList<CfdiRelated> cfdiRelated = new ArrayList<>();
        
        if (miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId().equals(rfcEmisor)) { // ¿el emisor del CFDI es la empresa de la sesión?
            SDataCfdPacType cfdPacType = SCfdUtils.getCfdPacType(miClient, cfdType);
            SDataPac pac = null;

            if (cfdPacType != null) {
                pac = (SDataPac) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_PAC, new int[] { cfdPacType.getFkPacId() }, SLibConstants.EXEC_MODE_SILENT);
            }

            if (pac == null) {
                throw new Exception("Error al leer el catálogo de PAC's.\n -No existe ningún PAC registrado para la cancelación de CFDI.");
            }
            else {
                String userName = pac.getUser();
                String userPswd = pac.getUserPassword();
                SDataCertificate certificate = miClient.getSessionXXX().getParamsCompany().getDbmsDataCertificate_n();

                if (miClient.getSessionXXX().getParamsCompany().getIsCfdiProduction()) {
                    // environment for production is set:

                    switch (pac.getPkPacId()) {
                        case SModSysConsts.TRN_PAC_FCG:
                            break;

                        case SModSysConsts.TRN_PAC_FNK:
                            com.finkok.facturacion.cancel.CancelSOAP cancelSOAP = new com.finkok.facturacion.cancel.CancelSOAP();
                            com.finkok.facturacion.cancel.Application application = cancelSOAP.getApplication();

                            /* Después de varios intentos, al parecer esta es la forma de invocar el método que sí funciona.
                             * ¡En 2019 dejó de funcionar porque el SAT solicitó el CSD del receptor!
                             * ¡Sí, el CSD del receptor! ¡¿Qué tiene que hacer el CSD del receptor con el emisor?!
                             */
                            views.core.soap.services.apps.RelatedResult relatedResult = application.getRelated(
                                    userName, userPswd, rfcEmisor, "", uuid, certificate.getExtraFnkPublicKeyBytes_n(), certificate.getExtraFnkPrivateKeyBytes_n());

                            if (relatedResult.getError() != null) {
                                msLastCfdiRelatedError = relatedResult.getError().getValue();
                            }
                            
                            if (relatedResult.getPadres() != null) {
                                views.core.soap.services.apps.PadreArray padreArray = relatedResult.getPadres().getValue();
                                for (views.core.soap.services.apps.Padre padre : padreArray.getPadre()) {
                                    String valUuid = "";
                                    String valEmisor = "";
                                    String valReceptor = "";

                                    if (padre.getUuid().getValue() != null) {
                                        valUuid = padre.getUuid().getValue();
                                    }
                                    if (padre.getEmisor().getValue() != null) {
                                        valEmisor = padre.getEmisor().getValue();
                                    }
                                    if (padre.getReceptor().getValue() != null) {
                                        valReceptor = padre.getReceptor().getValue();
                                    }

                                    cfdiRelated.add(new CfdiRelated(PADRE, valUuid, valEmisor, valReceptor));
                                }
                            }
                            
                            if (relatedResult.getHijos() != null) {
                                views.core.soap.services.apps.HijoArray hijoArray = relatedResult.getHijos().getValue();
                                for (views.core.soap.services.apps.Hijo hijo : hijoArray.getHijo()) {
                                    String valUuid = "";
                                    String valEmisor = "";
                                    String valReceptor = "";

                                    if (hijo.getUuid().getValue() != null) {
                                        valUuid = hijo.getUuid().getValue();
                                    }
                                    if (hijo.getEmisor().getValue() != null) {
                                        valEmisor = hijo.getEmisor().getValue();
                                    }
                                    if (hijo.getReceptor().getValue() != null) {
                                        valReceptor = hijo.getReceptor().getValue();
                                    }

                                    cfdiRelated.add(new CfdiRelated(HIJO, valUuid, valEmisor, valReceptor));
                                }
                            }
                            break;

                        default:
                            // do nothing
                    }
                }
                else {
                    // environment for development is set:

                    switch (pac.getPkPacId()) {
                        case SModSysConsts.TRN_PAC_FCG:
                            break;

                        case SModSysConsts.TRN_PAC_FNK:
                            break;

                        default:
                            // do nothing
                    }
                }
            }
        }
        
        return cfdiRelated;
    }
    
    /**
     * Devuelve el estatus del SAT a traves de un SDataCfd.
     * @param cfd CFDI.
     * @return CfdiAckQuery
     * @throws java.lang.Exception 
    */
    public CfdiAckQuery getCfdiSatStatus(final SDataCfd cfd) throws Exception {
        return getCfdiSatStatus(cfd.getFkCfdTypeId(), DCfdUtils.getCfdi33(cfd.getDocXml()));
    }
    
    /**
     * Devuelve el estatus del SAT a traves de un DElementComprobante.
     * @param cfdType Tipo de CFDI, constantes definidas en SDataConstantsSys.TRNS_TP_CFD_...
     * @param comprobante CFDI.
     * @return CfdiAckQuery
     * @throws java.lang.Exception 
    */
    public CfdiAckQuery getCfdiSatStatus(final int cfdType, final cfd.ver33.DElementComprobante comprobante) throws Exception {
        cfd.ver33.DElementTimbreFiscalDigital tfd = comprobante.getEltOpcComplementoTimbreFiscalDigital();
        
        if (tfd != null) {
            float version = comprobante.getVersion();
            String rfcEmisor = comprobante.getEltEmisor().getAttRfc().getString();
            String rfcReceptor = comprobante.getEltReceptor().getAttRfc().getString();
            double total = comprobante.getAttTotal().getDouble();
            String sello = comprobante.getAttSello().getString();
            
            return getCfdiSatStatus(cfdType, version, rfcEmisor, rfcReceptor, tfd.getAttUUID().getString(), total, sello);
        }
        
        return null;
    }
    
    /**
     * Devuelve el estatus del SAT a recibiendo los parámetros necesarios para la validación.
     * @param cfdType Tipo de CFDI, constantes definidas en SDataConstantsSys.TRNS_TP_CFD_...
     * @param version Versión del CFDI.
     * @param rfcEmisor RFC del emisor del CFDI.
     * @param rfcReceptor RFC del receptor del CFDI.
     * @param uuid UUID del CFDI.
     * @param total Total del CFDI.
     * @param sello Sello (firma electrónica) del CFDI generada con el CSD del emisor.
     * @return CfdiAckQuery
     * @throws java.lang.Exception 
    */
    public CfdiAckQuery getCfdiSatStatus(final int cfdType, final float version, final String rfcEmisor, final String rfcReceptor, final String uuid, final double total, final String sello) throws Exception {
        String data = "";
        
        if (version == DCfdConsts.CFDI_VER_32) {
            DecimalFormat decimalFormat32 = new DecimalFormat("0000000000.000000");
            
            data += "?re=" + rfcEmisor;
            data += "&rr=" + rfcReceptor;
            data += "&tt=" + decimalFormat32.format(total);
            data += "&id=" + uuid;
        }
        else if (version == DCfdConsts.CFDI_VER_33) {
            DecimalFormat decimalFormat33 = new DecimalFormat("#." + SLibUtils.textRepeat("#", 6)); // max decimals for total according to XSD of CFDI 3.3
            
            data += "?id=" + (uuid == null || uuid.isEmpty() ? SLibUtils.textRepeat("0", DCfdVer3Consts.LEN_UUID) : uuid); // UUID length hyphens included
            data += "&re=" + (rfcEmisor == null || rfcEmisor.isEmpty() ? SLibUtils.textRepeat("X", DCfdConsts.LEN_RFC_PER) : rfcEmisor.replaceAll("&", "&amp;")); // some fiscal ID contains character '&', and must be encoded
            data += "&rr=" + (rfcReceptor == null || rfcReceptor.isEmpty() ? SLibUtils.textRepeat("X", DCfdConsts.LEN_RFC_PER) : rfcReceptor.replaceAll("&", "&amp;")); // some fiscal ID contains character '&', and must be encoded
            data += "&tt=" + decimalFormat33.format(SLibUtils.roundAmount(total));
            if (sello != null && !sello.isEmpty()) {
                data += "&fe=" + SLibUtils.textRight(sello, 8); // last 8 characters of electronic signature
            }
        }
        
        ConsultaCFDIService service = new ConsultaCFDIService(); 
        IConsultaCFDIService iService = service.getBasicHttpBindingIConsultaCFDIService();
        org.datacontract.schemas._2004._07.sat_cfdi_negocio_consultacfdi.Acuse acuse = iService.consulta(data);
        
        String valEsCancelable = "";
        String valCodigoEstatus = "";
        String valEstado = "";
        String valEstatusCancelacion = "";
        
        if (acuse != null) {
            if (acuse.getEsCancelable() != null) {
                valEsCancelable = acuse.getEsCancelable().getValue();
            }
            if (acuse.getCodigoEstatus() != null) {
                valCodigoEstatus = acuse.getCodigoEstatus().getValue();
            }
            if (acuse.getEstado() != null) {
                valEstado = acuse.getEstado().getValue();
            }
            if (acuse.getEstatusCancelacion() != null) {
                valEstatusCancelacion = acuse.getEstatusCancelacion().getValue();
            }
        }
        
        boolean areCfdiRelatedApplicable = miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId().equals(rfcEmisor); // ¿el emisor del CFDI es la empresa de la sesión?
        CfdiAckQuery cfdiAckQuery = new CfdiAckQuery(uuid, valEsCancelable, valCodigoEstatus, valEstado, valEstatusCancelacion, areCfdiRelatedApplicable);
        
        if (areCfdiRelatedApplicable) {
            cfdiAckQuery.CfdiRelatedList.addAll(getCfdiRelated(cfdType, rfcEmisor, uuid));
        }
        
        return cfdiAckQuery;
    }
    
    public class CfdiAckQuery {
        public String Uuid;
        public String CancellableInfo;
        public String RetrievalInfo;
        public String CfdiStatus;
        public String CancelStatus;
        public boolean AreCfdiRelatedApplicable;
        public ArrayList<CfdiRelated> CfdiRelatedList;
        
        /**
         * Construye nuevo resultado de la consulta de estatus de CFDI.
         * @param uuid UUID del CFDI.
         * @param cancellableInfo Estatus de cancelabilidad del CFDI.
         * @param retrievalInfo Estatus de obtención del CFDI.
         * @param cfdiStatus Estatus del CFDI.
         * @param cancelStatus Estatus de cancelación del CFDI.
         * @param areCfdiRelatedApplicable Aplicabilidad de obtención de CFDI relacionados del CFDI.
         */
        public CfdiAckQuery(String uuid, String cancellableInfo, String retrievalInfo, String cfdiStatus, String cancelStatus, boolean areCfdiRelatedApplicable) {
            Uuid = uuid;
            CancellableInfo = cancellableInfo;
            RetrievalInfo = retrievalInfo;
            CfdiStatus = cfdiStatus;
            CancelStatus = cancelStatus;
            AreCfdiRelatedApplicable = areCfdiRelatedApplicable;
            CfdiRelatedList = new ArrayList<>();
        }
        
        /**
         * Genera texto con CFDI relacionados del CFDI y mensaje de error asociado, si aplica.
         * @return 
         */
        public String composeCfdiRelated() {
            String message = "";
            
            if (AreCfdiRelatedApplicable) {
                if (CfdiRelatedList.isEmpty()) {
                    message = "Sin CFDI relacionados.";
                }
                else {
                    message = "Con " + CfdiRelatedList.size() + " CFDI " + (CfdiRelatedList.size() == 1 ? "relacionado" : "relacionados") + ":";
                    for (CfdiRelated cfdiRelated : CfdiRelatedList) {
                        message += "\n- " + cfdiRelated.composeMessage();
                    }
                }
                
                if (!msLastCfdiRelatedError.isEmpty()) {
                    message += (!message.isEmpty() ? "\n" : "") + msLastCfdiRelatedError;
                }
            }
            
            return message;
        }
        
        /**
         * Genera texto con la información detallada del estatus del CFDI.
         * @return 
         */
        public String getDetailedStatus() {
            String message;
            
            message = "ESTATUS SAT DEL CFDI '" + Uuid + "':\n";
            message += "Cancelable: [" + CancellableInfo + "]\n";
            message += "Recuperación: [" + RetrievalInfo + "]\n";
            message += "Estatus CFDI: [" + CfdiStatus + "]\n";
            message += "Estatus cancelación: [" + CancelStatus + "]\n";
            message += composeCfdiRelated();
            return message;
        }
        
        /**
         * Obtener estatus del CFDI.
         * @return Estatus del CFDI: "Vigente", "Cancelado", "No Encontrado" o "En proceso de cancelación".
         */
        public String getCfdiStatus() {
            if (CfdiStatus.equals(DCfdi33Consts.CFDI_ESTATUS_VIG) && CancelStatus.toLowerCase().equals(DCfdi33Consts.ESTATUS_CANCEL_PROC.toLowerCase())) {
                return "En proceso de cancelación";
            }
            return CfdiStatus;
        }
    }
    
    public class CfdiRelated {
        public String Tipo;
        public String Uuid;
        public String Issuer;
        public String Receiver;
        
        /**
         * Construye nuevo CFDI relacionado.
         * @param tipo Tipo de CFDI relacionado: PADRE o HIJO.
         * @param uuid UUID del CFDI relacionado.
         * @param issuer Emisor del CFDI relacionado.
         * @param receiver Receptor del CFDI relacionado.
         */
        public CfdiRelated(String tipo, String uuid, String issuer, String receiver) {
            Tipo = tipo;
            Uuid = uuid;
            Issuer = issuer;
            Receiver = receiver;
        }
        
        /**
         * Genera texto con la información del CFDI relacionado.
         * @return 
         */
        public String composeMessage() {
            return "Tipo: " + Tipo + "; UUID: " + Uuid + "; emisor: " + Issuer + "; receptor: " + Receiver + ".";
        }
    }
}
