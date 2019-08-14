/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

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
 * @author Sergio Flores
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
                                userName, userPswd, cfd.getDocXmlRfcEmi(), cfd.getUuid(), certificate.getExtraFnkPublicKeyBytes_n(), certificate.getExtraFnkPrivateKeyBytes_n());
                        
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
    
    public CfdiAckQuery getCfdiSatStatus(final SDataCfd cfd) throws Exception {
        miClient.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        CfdiAckQuery cfdiAckQuery = null;
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
            
            if (miClient.getSessionXXX().getParamsCompany().getIsCfdiProduction()) {
                // environment for production is set:
                
                switch (pac.getPkPacId()) {
                    case SModSysConsts.TRN_PAC_FCG:
                        break;
                        
                    case SModSysConsts.TRN_PAC_FNK:
                        DecimalFormat totalFormat = new DecimalFormat("#.00");
                        com.finkok.facturacion.cancel.CancelSOAP cancelSOAP = new com.finkok.facturacion.cancel.CancelSOAP();
                        com.finkok.facturacion.cancel.Application application = cancelSOAP.getApplication();
                        
                        views.core.soap.services.apps.AcuseSatEstatus acuseSatEstatus = application.getSatStatus(
                                userName, userPswd, cfd.getDocXmlRfcEmi(), cfd.getDocXmlRfcRec(), cfd.getUuid(), totalFormat.format(cfd.getDocXmlTot()));
                        
                        if (acuseSatEstatus.getError() != null) {
                            throw new Exception(acuseSatEstatus.getError().getValue());
                        }
                        else {
                            String valEsCancelable = "";
                            String valCodigoEstatus = "";
                            String valEstado = "";
                            String valEstatusCancelacion = "";
                            
                            if (acuseSatEstatus.getSat().getValue().getEsCancelable().getValue() != null) {
                                valEsCancelable = acuseSatEstatus.getSat().getValue().getEsCancelable().getValue();
                            }
                            if (acuseSatEstatus.getSat().getValue().getCodigoEstatus().getValue() != null) {
                                valCodigoEstatus = acuseSatEstatus.getSat().getValue().getCodigoEstatus().getValue();
                            }
                            if (acuseSatEstatus.getSat().getValue().getEstado().getValue() != null) {
                                valEstado = acuseSatEstatus.getSat().getValue().getEstado().getValue();
                            }
                            if (acuseSatEstatus.getSat().getValue().getEstatusCancelacion().getValue() != null) {
                                valEstatusCancelacion = acuseSatEstatus.getSat().getValue().getEstatusCancelacion().getValue();
                            }
                            
                            cfdiAckQuery = new CfdiAckQuery(cfd.getUuid(), valEsCancelable, valCodigoEstatus, valEstado, valEstatusCancelacion);
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
                    message += "\n- " + cfdiRelated.composeMessage();
                }
            }
            
            return message;
        }
        */
        
        public String composeMessage() {
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
