/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.util;

import erp.lib.SLibUtilities;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Edwin Carmona
 */
public abstract class STreasuryBankLayoutFile {
    
    public static File createDocument(final SGuiClient client, final SBankLayoutParams params, final ArrayList<SDocumentRequestRow> bankPayments) {
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        byte[] reportBytes = null;
        File file = null;
        File fileTemporal = null;
        FileOutputStream fos = null;
        JasperReport reporte = null;

        try {
            map = client.createReportParams();
            map.put("sTitle", params.getTitle());
            map.put("sCompany",params.getCompanyName());
            map.put("sDateStamp", SLibUtils.DateFormatDatetime.format(params.getDateTimeRequest()));
            map.put("sApplicationDate",  SLibUtils.DateFormatDate.format(params.getApplicationDate()));
            map.put("sLayoutType", params.getLayoutType());
            map.put("nTransfers", bankPayments.size());                
            map.put("sBank", params.getBank());
            map.put("sBankAccount", params.getBankAccount());
            map.put("sPaymentType", params.getTypePayment());
            map.put("sCurrency", params.getCurrency());
            map.put("dCurrencyTotal", params.getTotal());
            map.put("dOriginalTotal", params.getOriginalTotal());
            map.put("bIsDifferentCurrency", params.getIsDifferentCurrency());
            map.put("sCurrencyDps", params.getCurrencyDps());
            
            fileTemporal = new File("reps/fin_lay_bank.jasper");
            reporte = (JasperReport) JRLoader.loadObject(fileTemporal);
            jasperPrint = JasperFillManager.fillReport(reporte, map, new JRBeanCollectionDataSource(bankPayments));
            reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            fileTemporal = File.createTempFile("document", ".pdf");
            fos = new FileOutputStream(fileTemporal);
            fos.write(reportBytes);
            fos.close();

            file = new File(fileTemporal.getParentFile() + "\\" + SLibUtilities.textToAlphanumeric(params.getCompanyName()) + "#" + params.getFolio() + "-" + params.getAuthRequests() + ".pdf");
            fos = new FileOutputStream(file);
            fos.write(reportBytes);
            fos.close();
        }
        catch (Exception e) {
            SLibUtils.showException(STreasuryBankLayoutFile.class, e);
        }
        
        return file;   
    }
}
