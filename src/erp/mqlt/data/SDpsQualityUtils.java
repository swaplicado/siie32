/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mqlt.data;

import erp.data.SDataConstants;
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
     * @return 
     */
    public static ArrayList<SDataDpsEntryAnalysis> getAnalysisByItem(final SGuiSession session, final int idItem) {
        String sql = "";
        
            sql = "SELECT "
                + " * "
                + "FROM "
                + SDataConstants.TablesMap.get(SDataConstants.QLT_ANALYSIS_ITEM) + " "
                + "WHERE "
                + "id_item = " + idItem + " AND NOT b_del "
                + "ORDER BY sort_pos ASC, ts_new DESC;";
            
        try {
            ResultSet resultSet = null;
            resultSet = session.getStatement().executeQuery(sql);
            
            SDataDpsEntryAnalysis oEtyAnalysis;
            ArrayList<SDataDpsEntryAnalysis> lAnalysis = new ArrayList<>();
            int sortPosition = 1;
            while (resultSet.next()) {
                oEtyAnalysis = new SDataDpsEntryAnalysis();
                
                oEtyAnalysis.setSortPosition(sortPosition);
                oEtyAnalysis.setMinValue(resultSet.getString("min_value"));
                oEtyAnalysis.setMaxValue(resultSet.getString("max_value"));
                oEtyAnalysis.setIsMin(resultSet.getBoolean("b_min"));
                oEtyAnalysis.setIsMax(resultSet.getBoolean("b_max"));
                oEtyAnalysis.setIsRequired(resultSet.getBoolean("b_required"));
                oEtyAnalysis.setFkAnalysisId(resultSet.getInt("id_analysis"));
                oEtyAnalysis.setFkItemId(resultSet.getInt("id_item"));
                
                sql = "SELECT qa.unit_symbol, qa.analysis_name, qtp.type_name "
                        + "FROM " + SDataConstants.TablesMap.get(SDataConstants.QLT_ANALYSIS) + " AS qa "
                        + "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.QLT_TP_ANALYSIS) + " AS qtp "
                        + "ON qa.fk_tp_analysis_id = qtp.id_analysis_type "
                        + "WHERE qa.id_analysis = " + oEtyAnalysis.getFkAnalysisId();

                ResultSet resultSetAux = session.getDatabase().getConnection().createStatement().executeQuery(sql);
                if (resultSetAux.next()) {
                    oEtyAnalysis.setAuxAnalysisName(resultSetAux.getString("analysis_name"));
                    oEtyAnalysis.setAuxAnalysisUnit(resultSetAux.getString("unit_symbol"));
                    oEtyAnalysis.setAuxAnalysisType(resultSetAux.getString("type_name"));
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
            return new ArrayList<>();
        }
    }
    
    /**
     * 
     * @param session
     * @param idItem
     * @param entryDocPk
     * @param isPrint
     * @return 
     */
    public static ArrayList<SDataDpsEntryAnalysis> getAnalysisByDocumentEty(final SGuiSession session, final int idItem, final int[] entryDocPk, final boolean isPrint) {
        String sql = "";
        
        sql = "SELECT "
                + " id_ety_analysis "
                + "FROM "
                + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_ETY_ANALYSIS) + " "
                + "WHERE "
                + "fid_dps_year_n = " + entryDocPk[0] + " AND "
                + "fid_dps_doc_n = " + entryDocPk[1] + " AND "
                + "fid_dps_ety_n = " + entryDocPk[2] + " AND "
                + "fid_item_id = " + idItem + " AND "
                + "b_required = 1 "
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
                return SDpsQualityUtils.getAnalysisByItem(session, idItem);
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
            htmlTable += "<p>"
                        + "<label>Clave: </label>"
                        + "<b>" + SLibUtils.textToHtml(oDpsEntry.getConceptKey()) + "</b>"
                        + "<label> Concepto: </label><b>" + SLibUtils.textToHtml(oDpsEntry.getConcept()) + "</b>"
                    + "</p>";
            
            htmlTable += " <p>"
                        + "    <label>Cantidad:</label>"
                        + "    <b>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue4D.format(oDpsEntry.getQuantity()) + " " + oDpsEntry.getDbmsUnitSymbol()) + "</b>"
                        + "</p>";
            
            htmlTable += " <br>";
            
            if (! oDpsEntry.getDbmsDpsEntryAnalysis().isEmpty()) {
                ArrayList<SDataDpsEntryAnalysis> lAnalysisVsItem = SDpsQualityUtils.getAnalysisByItem(session, oDpsEntry.getFkItemId());
                Map<Integer, SDataDpsEntryAnalysis> mAnaItems = new HashMap<>();
                for (SDataDpsEntryAnalysis oAnaItem : lAnalysisVsItem) {
                    mAnaItems.put(oAnaItem.getFkAnalysisId(), oAnaItem);
                }
                
                hasAnalysis = true;
                htmlTable += "<table border cellpadding=\"10\" cellspacing=\"0\">"
                    + "<thead>"
                    + "    <tr>"
                            + "<th>" + SLibUtils.textToHtml("Orden") + "</th>"
                            + "<th>" + SLibUtils.textToHtml("Tipo") + "</th>"
                            + "<th>" + SLibUtils.textToHtml("Análisis") + "</th>"
                            + "<th>" + SLibUtils.textToHtml("Unidad") + "</th>"
                            + "<th>" + SLibUtils.textToHtml("Mínimo normativo") + "</th>"
                            + "<th>" + SLibUtils.textToHtml("Mínimo") + "</th>"
                            + "<th>" + SLibUtils.textToHtml("Máximo normativo") + "</th>"
                            + "<th>" + SLibUtils.textToHtml("Máximo") + "</th>"
                            + "<th>" + SLibUtils.textToHtml("Requerido") + "</th>"
                            + "<th>" + SLibUtils.textToHtml("Mod. requerido") + "</th>"
                            + "<th>" + SLibUtils.textToHtml("Mod. parámetros") + "</th>"
                    + "    </tr>"
                    + "</thead>"
                    + "<tbody>";
                 
                for (SDataDpsEntryAnalysis oDpsEntryAnalysis : oDpsEntry.getDbmsDpsEntryAnalysis()) {
                    htmlTable += "<tr>";

                    // Orden
                    htmlTable += "<td style=\"text-align: center;\">" + SLibUtils.textToHtml(oDpsEntryAnalysis.getSortPosition() + "") + "</td>";
                    // Tipo
                    htmlTable += "<td>" + SLibUtils.textToHtml(oDpsEntryAnalysis.getAuxAnalysisType()) + "</td>";
                    // Análisis
                    htmlTable += "<td>" + SLibUtils.textToHtml(oDpsEntryAnalysis.getAuxAnalysisName()) + "</td>";
                    // Unidad
                    htmlTable += "<td style=\"text-align: center;\">" + SLibUtils.textToHtml(oDpsEntryAnalysis.getAuxAnalysisUnit()) + "</td>";
                    // Valor mínimo normativo
                    htmlTable += "<td style=\"text-align: right;\">" + SLibUtils.textToHtml(mAnaItems.get(oDpsEntryAnalysis.getFkAnalysisId()).getMinValue()) + "</td>";
                    // Valor mínimo
                    htmlTable += "<td style=\"text-align: right;" + 
                            (! mAnaItems.get(oDpsEntryAnalysis.getFkAnalysisId()).getMinValue().equals(oDpsEntryAnalysis.getMinValue()) ? "color: blue" : "") + "\">" + 
                            SLibUtils.textToHtml(oDpsEntryAnalysis.getMinValue()) + "</td>";
                    // Valor máximo normativo
                    htmlTable += "<td style=\"text-align: right;\">" + SLibUtils.textToHtml(mAnaItems.get(oDpsEntryAnalysis.getFkAnalysisId()).getMaxValue()) + "</td>";
                    // Valor máximo
                    htmlTable += "<td style=\"text-align: right;" + 
                            (! mAnaItems.get(oDpsEntryAnalysis.getFkAnalysisId()).getMaxValue().equals(oDpsEntryAnalysis.getMaxValue()) ? "color: blue" : "") + "\">" + 
                            SLibUtils.textToHtml(oDpsEntryAnalysis.getMaxValue()) + "</td>";
                    // Requerido
                    htmlTable += "<td style=\"text-align: center;\">" + 
                                    SLibUtils.textToHtml(oDpsEntryAnalysis.isRequired() ? "SÍ" : "NO") + 
                                "</td>";
                    // Modificación de requerido
                    htmlTable += "<td style=\"text-align: center;\">" + 
                                    (oDpsEntryAnalysis.isRequiredModified() ? "<b>" : "") + SLibUtils.textToHtml(oDpsEntryAnalysis.isRequiredModified() ? "SÍ" : "NO") + 
                                    (oDpsEntryAnalysis.isRequiredModified() ? "</b>" : "") + 
                                "</td>";
                    // Modificación de parámetros
                    htmlTable += "<td style=\"text-align: center;\">" + 
                                    (oDpsEntryAnalysis.isLimitModified() ? "<b>" : "") + SLibUtils.textToHtml(oDpsEntryAnalysis.isLimitModified() ? "SÍ" : "NO") + 
                                    (oDpsEntryAnalysis.isLimitModified() ? "</b>" : "") + 
                                "</td>";

                    htmlTable += "</tr>";
                }
                
                htmlTable += "</tbody>"
                        + "</table>";
            }
            else {
                htmlTable += "<p>"
                            + "   <label><b>Sin Datos</b></label>"
                            + "</p>";
            }
            
            htmlTable += " <hr>";
        }
        
        if (! hasAnalysis) {
            return null;
        }
        
        htmlTable += "</body>"
                + "</html>";
        
        String htmlHeader = "<!DOCTYPE html>"
                    + "<html lang=\"es\">"
                    + "<body>"
                    + "<p>"
                        + " <b>" + companyName + "</b>"
                    + "</p>";
        htmlHeader += "<p>"
                    + "<label>Cliente: </label><b>" + SLibUtils.textToHtml(bpName) + "</b>";
        htmlHeader += "<br>"
                    + "<label>Fecha: </label><b>" + SLibUtils.textToHtml(dtDate) + "</b>";
        htmlHeader += "<br>"
                    + "<label>Fecha doc: </label><b>" + SLibUtils.textToHtml(dtDoc) + "</b>";
        htmlHeader += "<br>"
                    + (number.length() > 0 ? "<label>" + SLibUtils.textToHtml("Núm:") + " </label><b>" + SLibUtils.textToHtml(number) + "</b>" : "")
                    + (numberRef.length() > 0 ? "<label>" + SLibUtils.textToHtml("Núm ref:") + " </label><b>" + SLibUtils.textToHtml(numberRef) + "</b>" : "")
                    + (numberSer.length() > 0 ? "<label>" + SLibUtils.textToHtml("Núm ser:") + " </label><b>" + SLibUtils.textToHtml(numberSer) + "</b>" : "")
                + "</p>";
        
        String html = htmlHeader + 
                        "<hr>" + 
                        htmlTable;
        
        return html;
    }
    
    /**
     * Private methods:
     */
    
}
