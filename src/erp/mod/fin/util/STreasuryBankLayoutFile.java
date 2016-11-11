/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.util;

import erp.lib.SLibUtilities;
import erp.mod.fin.db.SDbBankLayout;
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
public class STreasuryBankLayoutFile {
    
    private SDbBankLayout moBankLayout;
    private SGuiClient miClient;
    private ArrayList<SDocumentRequestRow> moLayoutBankPayments;

    public STreasuryBankLayoutFile(SGuiClient client, SDbBankLayout layout) {
        miClient = client;
        moBankLayout = SFinUtils.loadPaymentsXml(miClient, layout);
        if (moBankLayout != null) {
            moLayoutBankPayments = SFinUtils.populateRows(miClient, moBankLayout.getBankPaymentRows(), moBankLayout.getXmlRows());
        }
    }
    
    public File createDocument(SLayoutParameters parameters) {
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        byte[] reportBytes = null;
        File file = null;
        File fileTemporal = null;
        FileOutputStream fos = null;
        JasperReport reporte = null;

        try {
            if (moBankLayout != null) {
                map = miClient.createReportParams();
                map.put("sTitle", parameters.getTitle());
                map.put("sCompany",parameters.getCompanyName());
                map.put("sDateStamp", parameters.getDateTimeRequest());
                map.put("sApplicationDate", parameters.getApplicationDate());
                map.put("sBank", parameters.getBank());
                map.put("sBankAccount", parameters.getBankAccount());
                map.put("sPaymentType", parameters.getTypePayment());
                map.put("sCurrency", parameters.getCurrency());
                map.put("dTotal", parameters.getTotal());
                
                fileTemporal = new File("reps/fin_lay_bank.jasper");
                reporte = (JasperReport) JRLoader.loadObject(fileTemporal);
                jasperPrint = JasperFillManager.fillReport(reporte, map, new JRBeanCollectionDataSource(moLayoutBankPayments));
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
        }
        catch (Exception e) {
            SLibUtils.showException(STreasuryBankLayoutFile.class, e);
        }
        
        return file;   
    }
}
