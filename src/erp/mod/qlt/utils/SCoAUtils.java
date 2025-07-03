/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.qlt.utils;

import erp.mod.hrs.utils.SDocUtils;
import erp.mod.qlt.db.SDbCoAResult;
import erp.mod.qlt.db.SDbDatasheetTemplate;
import erp.mod.qlt.db.SMongoDbCoA;

import java.io.File;
import java.util.Map;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.bson.Document;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiReport;

/**
 *
 * @author Edwin Carmona
 */
public abstract class SCoAUtils {
    
    public static String jbPrintCoA(SGuiClient oClient, SDbCoAResult oCoAResult, Map<String, Object> mParameters) throws Exception {
        if (oCoAResult == null) {
            throw new Exception("No se ha definido el resultado del CoA.");
        }
        if (oCoAResult.getExternalFileId() != null && oCoAResult.getExternalFileId().length() > 0) {
            SDocUtils.viewFile(oClient, SDocUtils.BUCKET_DOC_QLT_COA_FILE, oCoAResult.getExternalFileId());
            
//            return true;
        }
        else {
            SGuiReport report = new SGuiReport("reps/qlt_coa.jasper", "Requisición de materiales");
            File reportFile = new File(report.getFileName());
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, mParameters, oClient.getSession().getStatement().getConnection());
            byte[] fileBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            SDbDatasheetTemplate oTemplate = oCoAResult.getAuxDatasheetTemplate();
            String fileName = oCoAResult.getPkCoAResultId() + "_" + SLibUtils.DbmsDateFormatDate.format(oCoAResult.getDate()) + "_" + (oTemplate != null ? oTemplate.getTemplateStandard() : "") + ".pdf";
//            String sMongoFileId = SDocUtils.uploadFileFromBytes(oClient.getSession(), SDocUtils.BUCKET_DOC_QLT_COA_FILE, SDocUtils.FILE_TYPE_PDF, fileName, fileBytes);
//            
//            oCoAResult.setExternalFileId(sMongoFileId);
//            oCoAResult.setAuxFkNewUserCoAGenId(oClient.getSession().getUser().getPkUserId());
//
//            oCoAResult.save(oClient.getSession());
            
//            return oCoAResult.getQueryResultId() == SDbConsts.SAVE_OK;
        }
        
        return "";
    }

    /**
     * Guarda un CoA en la base de datos MongoDB.
     *
     * @param oClient Cliente de la sesión.
     * @param oCoAResult Resultado del CoA.
     * @param oData Datos del CoA a guardar.
     * @throws Exception Si ocurre un error al guardar el CoA.
     */
    public static Object[] saveCoAMongoDb(SGuiClient oClient, SDbCoAResult oCoAResult, SMongoDbCoA oData) throws Exception {
        if (oCoAResult == null) {
            throw new Exception("No se ha definido el resultado del CoA.");
        }
        if (oCoAResult.getExternalDocumentId() != null && oCoAResult.getExternalDocumentId().length() > 0) {
            if (oData.getId() != null && !oData.getId().toString().equals(oCoAResult.getExternalDocumentId())) {
                throw new Exception("El ID del CoA no coincide con el ID del resultado del CoA.");
            }

            SCoAMongoUtils.updateDocument(oClient.getSession(), SDocUtils.BUCKET_DOC_QLT_COA, oCoAResult.getExternalDocumentId(), oData.toDocument());
        }
        else {
            Document oDoc = oData.toDocument();
            String sMongoFileId = SCoAMongoUtils.insertDocument(oClient.getSession(), SDocUtils.BUCKET_DOC_QLT_COA, oDoc);
            
            oCoAResult.setExternalDocumentId(sMongoFileId);
            oCoAResult.setAuxFkNewUserCoAGenId(oClient.getSession().getUser().getPkUserId());

            oCoAResult.save(oClient.getSession());
        }

        return new Object[] { oCoAResult, oData };
    }
}
