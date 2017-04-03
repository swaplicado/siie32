/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.util;

import erp.client.SClientInterface;
import erp.lib.SLibUtilities;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbMms;
import erp.mod.fin.db.SDbBankLayout;
import erp.mod.fin.db.SFinConsts;
import static erp.mtrn.data.STrnUtilities.getMms;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailSender;

/**
 *
 * @author Edwin Carmona, Alfredo Perez
 */
public class STreasuryBankLayoutRequest {
    public static final int SND_TP_LAY_BANK = 1;
    public static final int SND_TP_PAY_RCP = 2;

    private SGuiClient miClient;
    private final SDbBankLayout moBankLayout;
    
    public STreasuryBankLayoutRequest(SGuiClient oClient,final SDbBankLayout layout) {
        miClient = oClient;
        moBankLayout = layout;
    }
    
    public boolean makeRequestToTreasury() {
        boolean isSent = false;
        SDbBankLayout oBankLayout = null;
        SLayoutParameters parameters = null;
        SDialogComments dialogComments = null;
        File pdf = null;
        
        String comment = "";
        
        dialogComments = new SDialogComments(miClient, "Comentarios");
        dialogComments.setVisible(true);
        
        if (dialogComments.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
            moBankLayout.setAuthorizationRequests(moBankLayout.getAuthorizationRequests() + 1);
            comment = dialogComments.getComment();
            
            oBankLayout = SFinUtils.loadPaymentsXml(miClient, moBankLayout);
            if (moBankLayout != null) {
                parameters = SFinUtils.getLayoutParameters(miClient, oBankLayout);
                pdf = STreasuryBankLayoutFile.createDocument(miClient, parameters ,SFinUtils.populateRows(miClient, oBankLayout.getBankPaymentRows(), oBankLayout.getXmlRows()));

                if (pdf != null) {
                    isSent = sendMail(parameters, comment, pdf, null);

                    if (isSent) {
                        try {
                            if (oBankLayout.getLayoutStatus() == SFinConsts.LAY_BANK_NEW_ST) {
                                SFinUtils.changeLayoutStatus(miClient, oBankLayout, SFinConsts.LAY_BANK_APPROVED_ST);
                            }
                            SFinUtils.increaseLayoutRequest(miClient, oBankLayout);
                        }
                        catch (Exception e) {
                            SLibUtils.printException(this, e);
                        }
                    }
                }
            }
        }
        return isSent;
    }
    
    public boolean sendMail(SLayoutParameters parameters, String comment, File pdf, String email) {
        return sendMail(parameters, comment, pdf, SND_TP_LAY_BANK, email);
    }
    
    /**
     * To send mail a file in format PDF.
     * @param parameters Object SLayoutParameters type or null.
     * @param comment comment to body mail.
     * @param pdf file to send mail.
     * @param sendType send type of file mail STreasuryBankLayoutRequest.SND_TP_....
     * @return 
     */
    public boolean sendMail(SLayoutParameters parameters, String comment, File pdf, int sendType, String email) {
        SDbMms mms = null;
        SMailSender sender = null;
        ArrayList<String> toRecipients = null;
        ArrayList<String> recipientsCc = null;
        SMail mail = null;
        String body = "";
        String subject = "";
        boolean sent = false;
        
        mms = getMms((SClientInterface) miClient, sendType == SND_TP_LAY_BANK ? SModSysConsts.CFGS_TP_MMS_FIN_PAY_AUTH_REQ : SModSysConsts.CFGS_TP_MMS_CFD);
        
        if (mms != null && mms.getQueryResultId() == SDbConsts.READ_OK) {
            if (sendType == SND_TP_PAY_RCP) {
                subject = "Envío de recibo de nómina";

                body = "Se adjunta recibo de nómina en formato PDF.";
                if (email != null && !email.isEmpty()) {
                    toRecipients = new ArrayList<String>(Arrays.asList(SLibUtils.textExplode(email, ";")));
                }
            }
            else if (sendType == SND_TP_LAY_BANK) {
                if (mms.getRecipientTo() != null && !mms.getRecipientTo().isEmpty()) {
                    toRecipients = new ArrayList<String>(Arrays.asList(SLibUtils.textExplode(mms.getRecipientTo(), ";")));
                }
                else {
                    miClient.showMsgBoxWarning("No existe ningún correo-e configurado para envío de solicitudes.");
                }

                if (mms.getRecipientCarbonCopy() != null && !mms.getRecipientCarbonCopy().isEmpty()) {
                    recipientsCc = new ArrayList<String>(Arrays.asList(SLibUtilities.textExplode(mms.getRecipientCarbonCopy(), ";")));
                }
                subject = "SOLIC. AUT. " + ((SClientInterface) miClient).getSessionXXX().getCompany().getDbmsDataCompany().getBizPartnerCommercial() + "#" + parameters.getFolio() + "-" + parameters.getAuthRequests();

                body = parameters.getCompanyName() + "\n" +
                       "Fecha y hora de solicitud: " + SLibUtils.DateFormatDatetime.format(parameters.getDateTimeRequest()) + "\n\n" +
                       "FECHA DE APLICACIÓN: " + SLibUtils.DateFormatDate.format(parameters.getApplicationDate()) + "\n\n" +
                       "Banco: " + parameters.getBank() + "\n" +
                       "Cuenta bancaria: " + parameters.getBankAccount() + "\n" +
                       "Tipo de pago: " + parameters.getTypePayment() + "\n" +
                       "Total: " + SLibUtils.DecimalFormatValue2D.format( parameters.getOriginalTotal()) + " " + parameters.getCurrency() + "\n\n\n" +
                       comment;
            }
            
            sender = new SMailSender(mms.getHost(), mms.getPort(), mms.getProtocol(), mms.isStartTls(), mms.isAuth(), mms.getUser(), mms.getUserPassword(), mms.getUser());

                if (toRecipients != null) {
                    if (recipientsCc != null) {
                        mail = new SMail(sender, subject, body, toRecipients, recipientsCc);
                    }
                    else {
                        mail = new SMail(sender, subject, body, toRecipients);
                    }

                    if (pdf != null) {
                        mail.getAttachments().add(pdf);
                        try {
                            mail.send();
                            sent = true;
                        }
                        catch (Exception e) {
                            SLibUtils.showException(this, e);
                        }
                    }
                    else {
                        miClient.showMsgBoxWarning("No existe PDF para envío.");
                    }
                }
        }
        else {
            miClient.showMsgBoxWarning("No existe ninguna configuración para envío de solicitudes.");
        }
        
        return sent;
    }
    
}
