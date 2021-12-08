/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;

/**
 *
 * @author Isabel Servín
 */
public class SCfdBolUtils {
    public static void sign(final SClientInterface client, final int[] idCfd) throws Exception {
        SDataCfd cfd = SCfdUtils.getCfd(client, SDataConstantsSys.TRNS_TP_CFD_BOL, idCfd); 
        
        if (!cfd.getUuid().isEmpty()) {
            throw new Exception("El CFDI '" + cfd.getCfdNumber() + "' ya está timbrado.");
        }
        else if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
            throw new Exception("El CFDI '" + cfd.getCfdNumber() + "' está anulado.");
        }
        else if (!SDataUtilities.isPeriodOpen(client, cfd.getTimestamp())) {
            throw new Exception(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
        }
        else {
            switch(cfd.getFkXmlTypeId()) {
                case SDataConstantsSys.TRNS_TP_XML_CFD:
                case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                    throw new Exception("El tipo del CFDI '" + cfd.getCfdNumber() + "' es obsoleto.");

                case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                    try {
                        if (((SClientInterface) client).getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticBol()) {
                            if (SCfdUtils.signAndSendCfdi(client, cfd, SLibConstants.UNDEFINED, true, true)) {
                                client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRNX_CFD_PAY_REC);
                            }
                        }
                        else {
                            if (SCfdUtils.signCfdi(client, cfd, SLibConstants.UNDEFINED)) {
                                client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRNX_CFD_PAY_REC);
                            }
                        }
                    }
                    catch (java.lang.Exception e) {
                        throw new Exception("Ha ocurrido una excepción al timbrar o imprimir el comprobante fiscal:\n" + e);
                    }
                    break;

                default:
                    throw new Exception("El tipo del CFDI '" + cfd.getCfdNumber() + "' es desconocido.");
            }
        }
    }
}
