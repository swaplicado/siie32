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
    
    public static File createDocument(final SGuiClient client, final SLayoutParameters parameters, final ArrayList<SDocumentRequestRow> bankPayments) {
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        byte[] reportBytes = null;
        File file = null;
        File fileTemporal = null;
        FileOutputStream fos = null;
        JasperReport reporte = null;

        try {
            map = client.createReportParams();
            map.put("sTitle", parameters.getTitle());
            map.put("sCompany",parameters.getCompanyName());
            map.put("sDateStamp", SLibUtils.DateFormatDatetime.format(parameters.getDateTimeRequest()));
            map.put("sApplicationDate",  SLibUtils.DateFormatDate.format(parameters.getApplicationDate()));
            map.put("sLayoutType", parameters.getLayoutType());
            map.put("nTransfers", bankPayments.size());                
            map.put("sBank", parameters.getBank());
            map.put("sBankAccount", parameters.getBankAccount());
            map.put("sPaymentType", parameters.getTypePayment());
            map.put("sCurrency", parameters.getCurrency());
            map.put("dCurrencyTotal", parameters.getTotal());
            map.put("dOriginalTotal", parameters.getOriginalTotal());
            map.put("bIsDifferentCurrency", parameters.getIsDifferentCurrency());
            map.put("sCurrencyDps", parameters.getCurrencyDps());

            fileTemporal = new File("reps/fin_lay_bank.jasper");
            reporte = (JasperReport) JRLoader.loadObject(fileTemporal);
            jasperPrint = JasperFillManager.fillReport(reporte, map, new JRBeanCollectionDataSource(bankPayments));
            reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            fileTemporal = File.createTempFile("document", ".pdf");
            fos = new FileOutputStream(fileTemporal);
            fos.write(reportBytes);
            fos.close();

            file = new File(fileTemporal.getParentFile() + "\\" + SLibUtilities.textToAlphanumeric(parameters.getCompanyName()) + "#" + parameters.getFolio() + "-" + parameters.getAuthRequests() + ".pdf");
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
