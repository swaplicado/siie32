/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import cfd.DElement;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mcfg.data.SDataCertificate;
import erp.mod.SModSysConsts;
import java.awt.Cursor;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public class SCfdUtilsHandler {
    
    private final SClientInterface miClient;
    
    public SCfdUtilsHandler(SClientInterface client) {
        miClient = client;
    }
    
    public ArrayList<CfdiRelated> getCfdiRelated(final SDataCfd cfd) throws Exception {
        ArrayList<CfdiRelated> cfdiRelatedList = new ArrayList<>();
        SDataCfdPacType cfdPacType = SCfdUtils.getPacConfiguration(miClient, cfd.getFkCfdTypeId());
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
                        
                        views.core.soap.services.apps.RelatedResult relatedResult = application.getRelated(
                                userName, userPswd, cfd.getDocXmlRfcEmi(), cfd.getDocXmlRfcRec(), cfd.getUuid(), certificate.getExtraFnkPublicKeyBytes_n(), certificate.getExtraFnkPrivateKeyBytes_n());
                        
                        if (relatedResult.getError() != null) {
                            throw new Exception(relatedResult.getError().getValue());
                        }
                        else if (relatedResult.getPadres() != null) {
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
                                    valEmisor = padre.getReceptor().getValue();
                                }
                                
                                cfdiRelatedList.add(new CfdiRelated(valUuid, valEmisor, valReceptor));
                            }
                        }
                        break;
                        
                    default:
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
                }
            }
        }
        
        return cfdiRelatedList;
    }
    
    /**
     * Devuelve el estatus del SAT a traves de un SDataCfd
     * @param cfd SDataCfd
     * @return CfdiAckQuery
     * @throws java.lang.Exception 
    */
    public CfdiAckQuery getCfdiSatStatus(final SDataCfd cfd) throws Exception {
        return getCfdiSatStatus(cfd.getFkCfdTypeId(), cfd.getDocXmlRfcEmi(), cfd.getDocXmlRfcRec(), cfd.getUuid(), cfd.getDocXmlTot());
    }
    
    /**
     * Devuelve el estatus del SAT a traves de un DElementComprobante
     * @param cfdiComprobante DElementComprobante
     * @return CfdiAckQuery
     * @throws java.lang.Exception 
    */
    public CfdiAckQuery getCfdiSatStatus(cfd.ver33.DElementComprobante cfdiComprobante) throws Exception {
        cfd.ver33.DElementTimbreFiscalDigital tfd = null;
        if (cfdiComprobante.getEltOpcComplemento() != null) {
            for (DElement element : cfdiComprobante.getEltOpcComplemento().getElements()) {
                if (element.getName().compareTo("tfd:TimbreFiscalDigital") == 0) {
                    tfd = (cfd.ver33.DElementTimbreFiscalDigital) element;
                }
            }
        }
        if (tfd != null) {
            String rfcEmisor = cfdiComprobante.getEltEmisor().getAttRfc().getString();
            String rfcReceptor = cfdiComprobante.getEltReceptor().getAttRfc().getString();
            double total = cfdiComprobante.getAttTotal().getDouble();
            return getCfdiSatStatus(1, rfcEmisor, rfcReceptor, tfd.getAttUUID().getString(), total);
        }
        return null;
    }
    
    /**
     * Devuelve el estatus del SAT a recibiendo los parámetros necesarios para la validación.
     * @param cfdTypeId
     * @param rfcEmisor
     * @param rfcReceptor
     * @param uuid
     * @param total
     * @return CfdiAckQuery
     * @throws java.lang.Exception 
    */
    public CfdiAckQuery getCfdiSatStatus(final int cfdTypeId, final String rfcEmisor, final String rfcReceptor, final String uuid, final double total) throws Exception {
        miClient.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        CfdiAckQuery cfdiAckQuery = null;
        SDataCfdPacType cfdPacType = SCfdUtils.getPacConfiguration(miClient, cfdTypeId);
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
            
            switch (pac.getPkPacId()) {
                case SModSysConsts.TRN_PAC_FCG:
                    break;

                case SModSysConsts.TRN_PAC_FNK:
                    DecimalFormat totalFormat = new DecimalFormat("#.00");
                    com.finkok.facturacion.cancel.CancelSOAP cancelSOAP = new com.finkok.facturacion.cancel.CancelSOAP();
                    com.finkok.facturacion.cancel.Application application = cancelSOAP.getApplication();

                    views.core.soap.services.apps.AcuseSatEstatus acuseSatEstatus = application.getSatStatus(
                            userName, userPswd, rfcEmisor, rfcReceptor, uuid, totalFormat.format(total));

                    if (acuseSatEstatus.getError() != null) {
                        throw new Exception(acuseSatEstatus.getError().getValue());
                    }
                    else {
                        String valEsCancelable = "";
                        String valCodigoEstatus = "";
                        String valEstado = "";
                        String valEstatusCancelacion = "";

                        if (acuseSatEstatus.getSat().getValue().getEsCancelable() != null) {
                            valEsCancelable = acuseSatEstatus.getSat().getValue().getEsCancelable().getValue();
                        }
                        if (acuseSatEstatus.getSat().getValue().getCodigoEstatus() != null) {
                            valCodigoEstatus = acuseSatEstatus.getSat().getValue().getCodigoEstatus().getValue();
                        }
                        if (acuseSatEstatus.getSat().getValue().getEstado() != null) {
                            valEstado = acuseSatEstatus.getSat().getValue().getEstado().getValue();
                        }
                        if (acuseSatEstatus.getSat().getValue().getEstatusCancelacion() != null) {
                            valEstatusCancelacion = acuseSatEstatus.getSat().getValue().getEstatusCancelacion().getValue();
                        }

                        cfdiAckQuery = new CfdiAckQuery(uuid, valEsCancelable, valCodigoEstatus, valEstado, valEstatusCancelacion);
                        /* XXX 2019-08-14, Sergio Flores: Se deshabilita temporalmente la consulta de CFDI relacionados 
                         * debido a cambio inesperado en los parámetros de la solicitud del web service,
                         * ahora se discurrió que deben proporcionarse el RFC y CSD del receptor. ¡Sí, el CSD del receptor! WTF!
                        cfdiAckQuery.CfdiRelatedList.addAll(getCfdiRelated(cfd));
                        */
                    }
                    break;

                default:
            }
        }
        
        miClient.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
        return cfdiAckQuery;
    }
     
    public class CfdiAckQuery {
        public String Uuid;
        public String CancellableInfo;
        public String RetrievalInfo;
        public String CfdiStatus;
        public String CancelStatus;
        public ArrayList<CfdiRelated> CfdiRelatedList;
        
        public CfdiAckQuery(String uuid, String cancellableInfo, String retrievalInfo, String cfdiStatus, String cancelStatus) {
            Uuid = uuid;
            CancellableInfo = cancellableInfo;
            RetrievalInfo = retrievalInfo;
            CfdiStatus = cfdiStatus;
            CancelStatus = cancelStatus;
            CfdiRelatedList = new ArrayList<>();
        }
        
        /* XXX 2019-08-14, Sergio Flores: Se deshabilita temporalmente la consulta de CFDI relacionados 
         * debido a cambio inesperado en los parámetros de la solicitud del web service,
         * ahora se discurrió que deben proporcionarse el RFC y CSD del receptor. ¡Sí, el CSD del receptor! WTF!
        public String composeCfdiRelated() {
            String message;
            
            if (CfdiRelatedList.isEmpty()) {
                message = "Sin CFDI relacionados";
            }
            else {
                message = "Con " + CfdiRelatedList.size() + " CFDI " + (CfdiRelatedList.size() == 1 ? "relacionado" : "relacionados") + ":";
                for (CfdiRelated cfdiRelated : CfdiRelatedList) {
                    message += "\n- " + cfdiRelated.getDetailedStatus();
                }
            }
            
            return message;
        }
        */
        
        public String getDetailedStatus() {
            String message;
            
            message = "ESTATUS SAT DEL CFDI '" + Uuid + "':\n";
            message += "Cancelable: [" + CancellableInfo + "]\n";
            message += "Recuperación: [" + RetrievalInfo + "]\n";
            message += "Estatus CFDI: [" + CfdiStatus + "]\n";
            message += "Estatus cancelación: [" + CancelStatus + "]\n";
            /* XXX 2019-08-14, Sergio Flores: Se deshabilita temporalmente la consulta de CFDI relacionados 
             * debido a cambio inesperado en los parámetros de la solicitud del web service,
             * ahora se discurrió que deben proporcionarse el RFC y CSD del receptor. ¡Sí, el CSD del receptor! WTF!
            message += composeCfdiRelated();
            */
            return message;
        }
        
        /**
         * Obtener estatus del CFDI.
         * @return Estatus del CFDI: "Vigente" en caso de que el CFDI esté vigente; "En proceso de cancelación" si está en proceso de cancelación.
         */
        public String getCfdiStatus() {
            if (CfdiStatus.equals("Vigente") && (CancelStatus.equals("En proceso") || CancelStatus.equals("En Proceso"))) {
                return "En proceso de cancelación";
            }
            return CfdiStatus;
        }
        
        /**
         * Obtener todos los datos que conforman el estatus del SAT de un CFDI.
         * @return Un arreglo de todos los datos que conforman el estatus del SAT de un CFDI.
         * - Estatus
         * - Código de estatus
         * - Información de cancelación y
         * - Estatus de cancelación
         * en ese orden.
         */
        public String[] getArrayStatus() {
            String[] statusArray = { CfdiStatus, RetrievalInfo, CancellableInfo, CancelStatus }; 
            return statusArray;
        }
    }
    
    public class CfdiRelated {
        public String Uuid;
        public String Issuer;
        public String Receiver;
        
        public CfdiRelated(String uuid, String issuer, String receiver) {
            Uuid = uuid;
            Issuer = issuer;
            Receiver = receiver;
        }
        
        public String composeMessage() {
            return "UUID: " + Uuid + "; emisor: " + Issuer + "; receptor: " + Receiver;
        }
    }
}
