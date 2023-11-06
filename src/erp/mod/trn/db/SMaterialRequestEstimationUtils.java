/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbMms;
import erp.mtrn.data.STrnUtilities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.mail.SMailSender;

/**
 *
 * @author Edwin Carmona
 */
public abstract class SMaterialRequestEstimationUtils {
    
    public static String getSubjectOfEstimate(SGuiClient client) {
        try {
            String subject = SCfgUtils.getParamValue(client.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_TRN_MAT_REQ_EST_SUBJECT);
            
            return subject;
        }
        catch (Exception ex) {
            Logger.getLogger(SMaterialRequestEstimationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
    }
    
    public static String getBodyOfEstimate(SGuiClient client) {
        try {
            String body = SCfgUtils.getParamValue(client.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_TRN_MAT_REQ_EST_BODY);
            
            body = body.replaceAll("__", "\n");
            
            return body;
        }
        catch (Exception ex) {
            Logger.getLogger(SMaterialRequestEstimationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
    }
    
    public static String getBodyEntries(ArrayList<SMaterialRequestEntryRow> estimationRows) {
        String rowsBody = "";
        
        int ety = 1;
        String row = "";
        for (SMaterialRequestEntryRow estimationRow : estimationRows) {
            row = "PARTIDA #" + SMaterialRequestEstimationUtils.padLeft(ety + "", 2, '0') + "\n" + 
                    "CANTIDAD: " + SMaterialRequestEstimationUtils.padLeft(estimationRow.getAuxQuantityToEstimate() + "", 5, ' ')  + " " + estimationRow.getAuxUnitCode() + "     " +
                    "CONCEPTO: " + estimationRow.getAuxItemName() + 
                    ((estimationRow.getAuxPartNumber() != null && !estimationRow.getAuxPartNumber().isEmpty()) ? ("     " + estimationRow.getAuxPartNumber()) : "") + 
                    (estimationRow.getDateRequired() != null ? ("\nFecha requerida entrega: " + SLibUtils.DateFormatDate.format(estimationRow.getDateRequired())) : "") +
                    (estimationRow.getNotes().isEmpty() ? "" : "\nComentarios: " + estimationRow.getNotes());
            
            row += "\n\n";
            
            rowsBody += row;
            ety++;
        }
    
        return rowsBody;
    }
    
    public static String padLeft(String input, int length, char paddingChar) {
        return String.format("%" + length + "s", input).replace(' ', paddingChar);
    }
    
    public static void sendMails(SGuiClient client, ArrayList<SProviderMailRow> lProviderRows) {
        SDbMms mms = STrnUtilities.getMms((SClientInterface) client, SModSysConsts.CFGS_TP_MMS_TRN_EST_REQ);
        SMailSender sender = new SMailSender(mms.getHost(), mms.getPort(), mms.getProtocol(), mms.isStartTls(), mms.isAuth(), mms.getUser(), mms.getUserPassword(), mms.getUser());
        String img = "firmas/" + client.getSession().getUser().getName() + ".jpg";
        String basePath = System.getProperty("user.dir");
        String imageFilePath = basePath + "/" + img;
        Map<String, String> inlineImages = new HashMap<>();
        inlineImages.put("AbcXyz123", imageFilePath);
        
        for (SProviderMailRow oProviderRow : lProviderRows) {
            if (oProviderRow.getTo().isEmpty()) {
                continue;
            }
            ArrayList<String> lTo = new ArrayList<>();
            String[] tos = oProviderRow.getTo().split(";");
            for (String to : tos) {
                lTo.add(to.trim());
            }
            ArrayList<String> lCc = new ArrayList<>();
            String[] ccs = oProviderRow.getCc().split(";");
            for (String cc : ccs) {
                if (cc.isEmpty()) {
                    continue;
                }
                lCc.add(cc.trim());
            }
            ArrayList<String> lCco = new ArrayList<>();
            String[] ccos = oProviderRow.getCco().split(";");
            for (String cco : ccos) {
                if (cco.isEmpty()) {
                    continue;
                }
                lCco.add(cco.trim());
            }
            
            String body = "<!DOCTYPE html>" +
                            "<html lang=\"es\">" +
                            "<body>";
            
            body += "<p>" + SLibUtils.textToHtmlIgnoreWhiteSpace(oProviderRow.getBody()).replaceAll("\n", "<br>") + "</p>";

            // Firma
            body += "<img src=\"cid:AbcXyz123\">";
            body += "</body>" +
                            "</html>";
            
            try {
                STrnUtilities.sendMail((SClientInterface) client, SModSysConsts.CFGS_TP_MMS_TRN_EST_REQ, body, oProviderRow.getSubject(), lTo, lCc, lCco, sender, inlineImages, client.getSession().getCurrentDate());
            }
            catch (Exception ex) {
                Logger.getLogger(SMaterialRequestEstimationUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        client.showMsgBoxInformation("Solicitudes enviadas");
    }
    
    public static String saveEstimationRequest(SGuiClient client, final int matRequestId, ArrayList<SMaterialRequestEntryRow> estimationRows, ArrayList<SProviderMailRow> lProviderRows) {
        String res;
        try {
            SDbEstimationRequest oEstReq = new SDbEstimationRequest();
            oEstReq.setFkMatRequestId_n(matRequestId);
            oEstReq.setFkUserId(client.getSession().getUser().getPkUserId());
            
            SDbEstimationRequestEntry oEty;
            for (SMaterialRequestEntryRow oMatReqEty : estimationRows) {
                oEty = new SDbEstimationRequestEntry();
                oEty.setQuantity(oMatReqEty.getAuxQuantityToEstimate());
                oEty.setDeleted(false);
                oEty.setFkItemId(oMatReqEty.getFkItemId());
                oEty.setFkUnitId(oMatReqEty.getFkUnitId());
                oEty.setFkMatRequestId_n(oMatReqEty.getPkMatRequestId());
                oEty.setFkMatRequestEntryId_n(oMatReqEty.getPkEntryId());
                
                oEstReq.getChildEntries().add(oEty);
            }
            
            SDbEstimationRequestRecipient oRec;
            for (SProviderMailRow oProviderRow : lProviderRows) {
                oRec = new SDbEstimationRequestRecipient();
                oRec.setProviderName(oProviderRow.getProvider());
                oRec.setMailsTo(oProviderRow.getTo());
                oRec.setMailsCc(oProviderRow.getCc());
                oRec.setMailsCco(oProviderRow.getCco());
                oRec.setSubject(oProviderRow.getSubject());
                oRec.setBody(oProviderRow.getBody());
                oRec.setDeleted(false);
                oRec.setFkBusinessPartnerId_n(oProviderRow.getFkProviderId_n());
                
                oEstReq.getChildRecipients().add(oRec);
            }
            
            oEstReq.save(client.getSession());
            
            return "";
        }
        catch (Exception ex) {
            Logger.getLogger(SMaterialRequestEstimationUtils.class.getName()).log(Level.SEVERE, null, ex);
            client.showMsgBoxError(ex.getMessage());
            res = ex.getMessage();
        }
        
        return res;
    }
    
    public static int getNextMailNumberOfMatRequest(Statement statement, final int matRequestId) {
        try {
            String sql = "SELECT " +
                    "COUNT(*) + 1 AS mail_num " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_EST_REQ) + " AS est " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_EST_REQ_REC) + " AS estrec ON est.id_est_req = estrec.id_est_req " +
                    "WHERE est.fk_mat_req_n = " + matRequestId + ";";
            
            ResultSet res = statement.getConnection().createStatement().executeQuery(sql);
            if (res.next()) {
                return res.getInt("mail_num");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestEstimationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 1;
    }
}
