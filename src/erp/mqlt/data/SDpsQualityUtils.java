/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mqlt.data;

import erp.data.SDataConstants;
import erp.mod.SModConsts;
import erp.mod.qlt.db.SQltUtils;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SDataDpsEntryAnalysis;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 * Esta clase contiene utilidades necesarias para la funcionalidad del apartado
 * de calidad en los Dps.
 * 
 * @author Edwin Carmona
 */
public class SDpsQualityUtils {
    
    /**
     * Public methods:
     */
    
    /**
     * 
     * 
     * @param session
     * @param idItem
     * @param idLogTpDelivery
     * @return 
     */
    public static ArrayList<SDataDpsEntryAnalysis> getAnalysisByItem(final SGuiSession session, final int idItem, final int idLogTpDelivery) {
        String sql = "";
        
        try {
            // Se obtiene el id del template de calidad:
            int idTemplate = SQltUtils.obtainDatasheetTemplateByItemLink(session.getStatement(), idItem, idLogTpDelivery);
            if (idTemplate == 0) {
                return new ArrayList<>();
            }
            sql = "SELECT "
                + " * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.QLT_DATASHEET_TEMPLATE) + " AS qdt "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.QLT_DATASHEET_TEMPLATE_ROW) + " AS qdtr "
                + "ON qdt.id_datasheet_template = qdtr.id_datasheet_template "
                + "WHERE qdt.id_datasheet_template = " + idTemplate + " AND NOT qdtr.b_del "
                + "ORDER BY qdtr.sort_pos ASC, qdtr.ts_usr_ins DESC;";

            ResultSet resultSet = null;
            resultSet = session.getStatement().executeQuery(sql);
            
            SDataDpsEntryAnalysis oEtyAnalysis;
            ArrayList<SDataDpsEntryAnalysis> lAnalysis = new ArrayList<>();
            int sortPosition = 1;
            while (resultSet.next()) {
                oEtyAnalysis = new SDataDpsEntryAnalysis();
                
                oEtyAnalysis.setSortPosition(sortPosition);
                oEtyAnalysis.setOriginalSpecification(resultSet.getString("qdtr.specification"));
                oEtyAnalysis.setSpecification(resultSet.getString("qdtr.specification"));
                oEtyAnalysis.setOriginalMinValue(resultSet.getString("qdtr.min_value"));
                oEtyAnalysis.setOriginalMaxValue(resultSet.getString("qdtr.max_value"));
                oEtyAnalysis.setMinValue(resultSet.getString("qdtr.min_value"));
                oEtyAnalysis.setMaxValue(resultSet.getString("qdtr.max_value"));
                oEtyAnalysis.setIsMin(resultSet.getBoolean("qdtr.b_min"));
                oEtyAnalysis.setIsMax(resultSet.getBoolean("qdtr.b_max"));
                oEtyAnalysis.setIsRequired(resultSet.getBoolean("qdtr.b_dps"));
                oEtyAnalysis.setIsForDps(resultSet.getBoolean("qdtr.b_dps"));
                oEtyAnalysis.setIsForCoA(resultSet.getBoolean("qdtr.b_coa"));
                oEtyAnalysis.setFkAnalysisId(resultSet.getInt("qdtr.id_analysis"));
                oEtyAnalysis.setFkItemId(idItem);
                oEtyAnalysis.setFkDatasheetTemplateId_n(resultSet.getInt(idTemplate));
                
                sql = "SELECT qa.unit_symbol, qa.analysis_name, qtp.name "
                        + "FROM " + SDataConstants.TablesMap.get(SDataConstants.QLT_ANALYSIS) + " AS qa "
                        + "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.QLT_TP_ANALYSIS) + " AS qtp "
                        + "ON qa.fk_tp_analysis_id = qtp.id_tp_analysis "
                        + "WHERE qa.id_analysis = " + oEtyAnalysis.getFkAnalysisId();

                ResultSet resultSetAux = session.getDatabase().getConnection().createStatement().executeQuery(sql);
                if (resultSetAux.next()) {
                    oEtyAnalysis.setAuxAnalysisName(resultSetAux.getString("analysis_name"));
                    oEtyAnalysis.setAuxAnalysisUnit(resultSetAux.getString("unit_symbol"));
                    oEtyAnalysis.setAuxAnalysisType(resultSetAux.getString("name"));
                }
                
                oEtyAnalysis.setFkUserNewId(session.getUser().getPkUserId());
                oEtyAnalysis.setFkUserEditId(SUtilConsts.USR_NA_ID);
                oEtyAnalysis.setFkUserDeleteId(SUtilConsts.USR_NA_ID);
                        
                lAnalysis.add(oEtyAnalysis);
                sortPosition++;
            }
            
            return lAnalysis;
        }
        catch (SQLException ex) {
            Logger.getLogger(SDpsQualityUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * 
     * @param session
     * @param idItem
     * @param entryDocPk
     * @param isPrint
     * @return 
     */
    public static ArrayList<SDataDpsEntryAnalysis> getAnalysisByDocumentEty(final SGuiSession session, final int idItem, final int[] entryDocPk, final boolean isPrint, final int idLogTpDelivery) {
        String sql = "";
        
        sql = "SELECT "
                + " id_ety_analysis "
                + "FROM "
                + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_ETY_ANALYSIS) + " "
                + "WHERE "
                + "fid_dps_year = " + entryDocPk[0] + " AND "
                + "fid_dps_doc = " + entryDocPk[1] + " AND "
                + "fid_dps_ety = " + entryDocPk[2] + " AND "
                + "fid_item = " + idItem + " "
                + "ORDER BY sort_pos ASC, id_ety_analysis ASC;";
        
        try {
            ResultSet resultSet = null;
            resultSet = session.getStatement().executeQuery(sql);
            
            SDataDpsEntryAnalysis oEtyAnalysis;
            ArrayList<SDataDpsEntryAnalysis> lAnalysis = new ArrayList<>();
            while (resultSet.next()) {
                oEtyAnalysis = new SDataDpsEntryAnalysis();
                oEtyAnalysis.read(new int[] { resultSet.getInt("id_ety_analysis") }, session.getDatabase().getConnection().createStatement());
                
                lAnalysis.add(oEtyAnalysis);
            }
            
            if (lAnalysis.isEmpty() && !isPrint) {
                return SDpsQualityUtils.getAnalysisByItem(session, idItem, idLogTpDelivery);
            }
            
            return lAnalysis;
        }
        catch (SQLException ex) {
            Logger.getLogger(SDpsQualityUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SDpsQualityUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ArrayList<>();
    }
    
    public static String createDpsBodyMail(SGuiSession session,
                                       java.util.Vector<SDataDpsEntry> mvDbmsDpsEntries,
                                       final String dtDate,
                                       final String dtDoc,
                                       final String number,
                                       final String numberRef,
                                       final String numberSer,
                                       final String companyName,
                                       final String bpName) {
        boolean hasAnalysis = false;
        String htmlTable = "";

        for (SDataDpsEntry oDpsEntry : mvDbmsDpsEntries) {
            htmlTable += "<div class=\"entry-section\">"
                    + "<p><span class=\"label\">Clave:</span> <span class=\"value\">" + SLibUtils.textToHtml(oDpsEntry.getConceptKey()) + "</span></p>"
                    + "<p><span class=\"label\">Concepto:</span> <span class=\"value\">" + SLibUtils.textToHtml(oDpsEntry.getConcept()) + "</span></p>"
                    + "<p><span class=\"label\">Cantidad:</span> <span class=\"value\">" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue2D.format(oDpsEntry.getQuantity()) + " " + oDpsEntry.getDbmsUnitSymbol()) + "</span></p>";

            if (!oDpsEntry.getDbmsDpsEntryAnalysis().isEmpty()) {
                hasAnalysis = true;
                htmlTable += "<table class=\"analysis-table\">"
                        + "<thead>"
                        + "<tr>"
                        + "<th>Orden</th>"
                        + "<th>Tipo</th>"
                        + "<th>" + SLibUtils.textToHtml("Análisis") + "</th>"
                        + "<th>Unidad</th>"
                        + "<th>" + SLibUtils.textToHtml("Especificación original") + "</th>"
                        + "<th>" + SLibUtils.textToHtml("Especificación actual") + "</th>"
                        + "<th>En contrato</th>"
                        + "<th>En CoA</th>"
                        + "</tr>"
                        + "</thead>"
                        + "<tbody>";

                for (SDataDpsEntryAnalysis oDpsEntryAnalysis : oDpsEntry.getDbmsDpsEntryAnalysis()) {
                    htmlTable += "<tr>"
                            + "<td class=\"center\">" + SLibUtils.textToHtml(String.valueOf(oDpsEntryAnalysis.getSortPosition())) + "</td>"
                            + "<td>" + SLibUtils.textToHtml(oDpsEntryAnalysis.getAuxAnalysisType()) + "</td>"
                            + "<td>" + SLibUtils.textToHtml(oDpsEntryAnalysis.getAuxAnalysisName()) + "</td>"
                            + "<td class=\"center\">" + SLibUtils.textToHtml(oDpsEntryAnalysis.getAuxAnalysisUnit()) + "</td>"
                            + "<td class=\"right\">" + SLibUtils.textToHtml(oDpsEntryAnalysis.getOriginalSpecification()) + "</td>"
                            + "<td class=\"right\">" + SLibUtils.textToHtml(oDpsEntryAnalysis.getSpecification()) + "</td>"
                            + "<td class=\"center\">" + (oDpsEntryAnalysis.isRequired() ? SLibUtils.textToHtml("SÍ") : "NO") + "</td>"
                            + "<td class=\"center\">" + (oDpsEntryAnalysis.isForCoA() ? SLibUtils.textToHtml("SÍ") : "NO") + "</td>"
                            + "</tr>";
                }

                htmlTable += "</tbody></table>";
            }
            else {
                htmlTable += "<p><strong>" + SLibUtils.textToHtml("Sin datos de análisis disponibles.") + "</strong></p>";
            }

            htmlTable += "</div><hr>";
        }

        if (!hasAnalysis) {
            return null;
        }

        String html = "<!DOCTYPE html>"
                + "<html lang=\"es\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<style>"
                + "body { font-family: Arial, sans-serif; font-size: 14px; color: #333; margin: 20px; }"
                + ".label { font-weight: bold; color: #555; }"
                + ".value { color: #000; }"
                + "hr { border: none; height: 1px; background-color: #ddd; margin: 20px 0; }"
                + ".entry-section { margin-bottom: 30px; }"
                + ".analysis-table { width: 100%; border-collapse: collapse; margin-top: 10px; }"
                + ".analysis-table th, .analysis-table td { border: 1px solid #ccc; padding: 8px; }"
                + ".analysis-table th { background-color: #f5f5f5; }"
                + ".center { text-align: center; }"
                + ".right { text-align: right; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<p><strong>" + companyName + "</strong></p>"
                + "<p><span class=\"label\">Cliente:</span> <span class=\"value\">" + SLibUtils.textToHtml(bpName) + "</span></p>"
                + "<p><span class=\"label\">Fecha:</span> <span class=\"value\">" + SLibUtils.textToHtml(dtDate) + "</span></p>"
                + "<p><span class=\"label\">Fecha doc:</span> <span class=\"value\">" + SLibUtils.textToHtml(dtDoc) + "</span></p>"
                + (number.length() > 0 ? "<p><span class=\"label\">" + SLibUtils.textToHtml("Núm") + ":</span> <span class=\"value\">" + SLibUtils.textToHtml(number) + "</span></p>" : "")
                + (numberRef.length() > 0 ? "<p><span class=\"label\">" + SLibUtils.textToHtml("Núm") + " ref:</span> <span class=\"value\">" + SLibUtils.textToHtml(numberRef) + "</span></p>" : "")
                + (numberSer.length() > 0 ? "<p><span class=\"label\">" + SLibUtils.textToHtml("Núm") + " ser:</span> <span class=\"value\">" + SLibUtils.textToHtml(numberSer) + "</span></p>" : "")
                + "<hr>"
                + htmlTable
                + "</body></html>";

        return html;
    }
    
    /**
     * Private methods:
     */
    
}
