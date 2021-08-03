/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.util;

import erp.client.SClientInterface;
import erp.mbps.data.SDataBizPartner;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbMms;
import erp.mod.fin.db.SDbBankLayout;
import erp.mod.fin.db.SLayoutBankDps;
import erp.mod.fin.db.SLayoutBankPayment;
import erp.mod.fin.db.SLayoutBankPaymentRow;
import erp.mtrn.data.STrnUtilities;
import java.util.ArrayList;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiClient;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public class SBankLayoutCourier extends Thread {
    
    private final SGuiClient miClient;
    private final SDbBankLayout moBankLayout;
    
    public SBankLayoutCourier(SGuiClient client, final SDbBankLayout bankLayout) {
        miClient = client;
        moBankLayout = bankLayout;
        setDaemon(true);
    }
    
    private String composeMailBody(SLayoutBankPaymentRow paymentRow) {
        String mailBody = "<!DOCTYPE html>";
        
        mailBody += "<html lang=\"en\">";
        
        mailBody += "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">"
                + "<title>Notificación SIIE</title>"
                + "<style>"
                + "body { font-family: Verdana, Geneva, Tahoma, sans-serif; }"
                + "#concepts { border-collapse: collapse; }"
                + "#concepts th { background-color: darkgray; }"
                + ".concept { border: 1px solid lightgray; padding: 4px; text-align: left; }"
                + ".amount { border: 1px solid lightgray; padding: 4px; text-align: right; }"
                + ".total { background-color: gainsboro; }"
                + ".warning { font-size: 0.625em; }"
                + "</style>"
                + "</head>";
        
        mailBody += "<body>"
                + "<div>"
                + "<p>"
                + SLibUtils.textToHtml(paymentRow.getBizPartner()) + ":"
                + "</p>"
                + "<p>"
                + SLibUtils.textToHtml("Se programó una transferencia a tu cuenta " + paymentRow.getBeneficiaryAccountBankName() + " con terminación " + paymentRow.getBeneficiaryAccountNumberShort() + ".")
                + "</p>"
                + "<p>"
                + "Importe: $" + SLibUtils.getDecimalFormatAmount().format(paymentRow.getPayment()) + " " + paymentRow.getPayerAccountCurrencyKey() + "."
                + "</p>";
        
        SLayoutBankPayment payment = paymentRow.getLayoutBankPayment();
        
        switch (payment.getTransactionType()) {
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
                // this type of payment contains one or more individual payments:
                
                mailBody += "<table id=\"concepts\">";
                
                mailBody += "<tr>"
                + "<th class=\"concept\">Factura</th>"
                + "<th class=\"amount\">Importe</th>"
                + "</tr>";
                
                for (SLayoutBankDps dps : payment.getLayoutBankDpss()) {
                    mailBody += "<tr>"
                    + "<td class=\"concept\">" + dps.getDps().getDpsNumber() + "</td>"
                    + "<td class=\"amount\">$" + SLibUtils.getDecimalFormatAmount().format(dps.getPayment()) + "</td>"
                    + "</tr>";
                }
                
                if (payment.getLayoutBankDpss().size() > 1) {
                    mailBody += "<tr>"
                            + "<td class=\"concept total\"><strong>Total<strong></td>"
                            + "<td class=\"amount total\"><strong>$" + SLibUtils.getDecimalFormatAmount().format(paymentRow.getPayment()) + "</strong></td>"
                            + "</tr>";
                }
    
                mailBody += "</table>";
                
                break;
                
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                // this type of payment does not contain individual payments:
                
                mailBody += "<p>"
                        + "Motivo: " + payment.getPrepaymentObservations()
                        + "</p>";
                break;
                
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_OWN_TRANSFER:
                // not supported yet!
                break;
            default:
        }
        
        SDataBizPartner company = ((SClientInterface) miClient).getSessionXXX().getCompany().getDbmsDataCompany();
        
        mailBody += "<p>"
                + "Atentamente,<br>"
                + "<strong>" + SLibUtils.textToHtml(company.getBizPartner()) + "</strong>"
                + "</p>"
                + "<p>"
                + "<small>"
                + "P.D.<br>"
                + SLibUtils.textToHtml("Veras reflejado el movimiento en tu cuenta en unos minutos más.") + "<br>"
                + SLibUtils.textToHtml("Para recibir adecuadamente estas notificaciones, agréganos a tu lista de contactos.")
                + "</small>"
                + "</p>";
        
        mailBody += STrnUtilities.composeMailFooter("warning");
        
        mailBody += "</div>"
                + "</body>"
                + "</html>";

        return mailBody;
    }
    
    @Override
    public void run() {
        try {
            SDbMms mms = STrnUtilities.getMms((SClientInterface) miClient, SModSysConsts.CFGS_TP_MMS_FIN_PAY_AUTH_REQ);

            if (mms != null && mms.getQueryResultId() == SDbConsts.READ_OK) {
                int mailsSent = 0;
                SMailSender sender = new SMailSender(mms.getHost(), mms.getPort(), mms.getProtocol(), mms.isStartTls(), mms.isAuth(), mms.getUser(), mms.getUserPassword(), mms.getUser());

                for (SLayoutBankPaymentRow layoutBankPaymentRow : moBankLayout.getAuxLayoutBankPaymentRows()) {
                    ArrayList<String> recipients = layoutBankPaymentRow.getLayoutBankPayment().getEmailRecipients();

                    if (!recipients.isEmpty()) {
                        SDataBizPartner company = ((SClientInterface) miClient).getSessionXXX().getCompany().getDbmsDataCompany();
                        String subject = "Aviso transferencia " + company.getBizPartner();
                        String body = composeMailBody(layoutBankPaymentRow);

                        try {
                            SMail mail = new SMail(sender, subject, body, recipients);
                            mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
                            mail.send();
                            mailsSent++;
                        }
                        catch (Exception e) {
                            SLibUtils.printException(this, e);
                        }
                    }
                }
                
                System.out.println("Layout bancario #" + moBankLayout.getBankLayoutNumber(false) + ": transferencias = " + moBankLayout.getAuxLayoutBankPaymentRows().size() + "; notificaciones mail enviadas = " + mailsSent + ".");
            }
            else {
                throw new Exception("No existe ninguna configuración para envío de solicitudes.");
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
}
