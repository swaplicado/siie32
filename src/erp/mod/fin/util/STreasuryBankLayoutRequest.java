/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.util;

import erp.client.SClientInterface;
import erp.lib.SLibUtilities;
import erp.mod.cfg.db.SDbMms;
import erp.mod.fin.db.SDbBankLayout;
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
 * @author Edwin Carmona
 */
public class STreasuryBankLayoutRequest {
    
    private SGuiClient miClient;
    
    private final SDbBankLayout moBankLayout;

    public STreasuryBankLayoutRequest(SGuiClient oClient, SDbBankLayout layout) {
        miClient = oClient;
        moBankLayout = layout;
    }
    
    public boolean makeRequestToTreasury() {
        String comment = "";
        boolean done = false;
        SLayoutParameters parameters = null;
        File pdf = null;
        SDialogComments dialogComments = null;
        STreasuryBankLayoutFile creator = null;
        
        dialogComments = new SDialogComments(miClient, "Comentarios");
        dialogComments.setVisible(true);
        
        if (dialogComments.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
            moBankLayout.setAuthorizationRequests(moBankLayout.getAuthorizationRequests() + 1);
            comment = dialogComments.getComment();
            parameters = SFinUtils.getLayoutParameters(miClient, moBankLayout);
            creator = new STreasuryBankLayoutFile(miClient, moBankLayout);
            pdf = creator.createDocument(parameters);
            
            if (pdf != null) {
                done = sendMail(parameters, comment, pdf);
                
                if (done) {
                    try {
                        if (moBankLayout.getLayoutStatus() == SDbBankLayout.LAY_BANK_NEW_ST) {
                            SFinUtils.changeLayoutStatus(miClient, moBankLayout, SDbBankLayout.LAY_BANK_APPROVED_ST);
                        }
                        SFinUtils.increaseLayoutRequest(miClient, moBankLayout);
                    }
                    catch (Exception e) {
                        SLibUtils.printException(this, e);
                    }
                }
            }
        }
        return done;
    }
    
    private boolean sendMail(SLayoutParameters parameters, String comment, File pdf) {
        SDbMms mms = null;
        SMailSender sender = null;
        ArrayList<String> toRecipients = null;
        ArrayList<String> recipientsCc = null;
        SMail mail = null;
        String body = "";
        String subject = "";
        boolean sent = false;
        
        subject = "SOLIC. AUT. " + ((SClientInterface) miClient).getSessionXXX().getCompany().getDbmsDataCompany().getBizPartnerCommercial() + "#" + parameters.getFolio() + "-" + parameters.getAuthRequests();
        
        body = parameters.getCompanyName() + "\n" +
               "Fecha y hora de solicitud: " + SLibUtils.DateFormatDatetime.format(parameters.getDateTimeRequest()) + "\n\n" +
               "FECHA DE APLICACIÓN: " + SLibUtils.DateFormatDate.format(parameters.getApplicationDate()) + "\n\n" +
               "Banco: " + parameters.getBank() + "\n" +
               "Cuenta bancaria: " + parameters.getBankAccount() + "\n" +
               "Tipo de pago: " + parameters.getTypePayment() + "\n" +
               "Total: " + parameters.getTotal() + " " + parameters.getCurrency() + "\n\n\n" +
               comment;
        
        mms = getMms((SClientInterface) miClient, 1);
        
        if (mms.getQueryResultId() == SDbConsts.READ_OK) {
            sender = new SMailSender(mms.getHost(), mms.getPort(), mms.getProtocol(), mms.isStartTls(), mms.isAuth(), mms.getUser(), mms.getUserPassword(), mms.getUser());
            
            if (mms.getRecipientTo() != null && !mms.getRecipientTo().isEmpty()) {
                toRecipients = new ArrayList<String>(Arrays.asList(SLibUtils.textExplode(mms.getRecipientTo(), ";")));
                
                if (!toRecipients.isEmpty()) {
                    if (mms.getRecipientCarbonCopy() != null && !mms.getRecipientCarbonCopy().isEmpty()) {
                        recipientsCc = new ArrayList<String>(Arrays.asList(SLibUtilities.textExplode(mms.getRecipientCarbonCopy(), ";")));
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
                else {
                    miClient.showMsgBoxWarning("No existe ningún correo-e configurado para envío de solicitudes.");
                }
            }
            else {
                miClient.showMsgBoxWarning("No existe ningún correo-e configurado para envío de solicitudes.");
            }    
        }
        
        return sent;
    }
    
}
