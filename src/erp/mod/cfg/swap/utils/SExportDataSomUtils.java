/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import erp.data.SDataConstantsSys;
import erp.mod.cfg.swap.SSwapConsts;
import erp.server.SSessionServer;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 * Utilidades para la generación y visualización de reportes del módulo SOM
 * (SOM / Báscula).
 *
 * Provee métodos para construir el mapa de parámetros estándar de reportes
 * JasperReports y para generar el PDF del boleto de báscula, ya sea para
 * mostrarlo en pantalla o para exportarlo como archivo temporal.
 *
 * @author Edwin Carmona
 */
public class SExportDataSomUtils {
    
    /**
     * Construye el mapa de parámetros estándar requerido por los reportes
     * JasperReports del módulo SOM.
     *
     * Incluye parámetros obligatorios de identificación (empresa, usuario,
     * aplicación) y parámetros opcionales de formato (fechas, decimales).
     *
     * @param session Sesión activa del usuario.
     * @return Mapa de parámetros listo para pasarse a JasperFillManager.
     */
    public static HashMap<String, Object> createReportParamsMap(final SGuiSession session) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        // Parameters that need to be declared in reports:

        map.put("sCompanyName", session.getConfigCompany().getCompanyId());
        map.put("sUserName", session.getUser().getName());
        map.put("sAppName", "SOM");
        map.put("sAppCopyright", "COPYRIGTH");
        map.put("sAppProvider", "SWAPLICADO");
        map.put("sVendorCopyright", "AETH");
        map.put("sVendorWebsite", "SWAPLICADO.COM");

        // Optional parameters:

        map.put("oFormatDate", SLibUtils.DateFormatDate);
        map.put("oFormatDateShort", SLibUtils.DateFormatDateShort);
        map.put("oFormatDatetime", SLibUtils.DateFormatDatetime);
        map.put("oFormatDatetimeTic", SLibUtils.DateFormatDatetime);
        map.put("oFormatTime", SLibUtils.DateFormatTime);
        map.put("oFormatValue", SLibUtils.DecimalFormatValue0D);
        map.put("oFormatValue0D", SLibUtils.DecimalFormatValue0D);
        map.put("oFormatValue2D", SLibUtils.DecimalFormatValue2D);
        map.put("oFormatValue4D", SLibUtils.DecimalFormatValue4D);
        map.put("oFormatValue8D", SLibUtils.DecimalFormatValue8D);

        return map;
    }
    
    /**
     * Genera el PDF del boleto de báscula a partir de su referencia SWAP.
     *
     * La referencia tiene el formato: {@code CÓDIGO + SEPARATOR + sca_code + '-' + num}
     * (p. ej. {@code "SI/BAS1-1234"}), donde {@code sca_code} es el código de la
     * báscula y {@code num} es el número del boleto. Se usa el último guión como
     * separador para soportar códigos de báscula que contengan guiones.
     *
     * Resuelve el {@code id_tic} consultando {@code s_tic JOIN su_sca} y delega
     * al método {@link #createTicketPdf(SGuiSession, Connection, int, boolean)}.
     *
     * @param session       Sesión activa del usuario.
     * @param somConnection Conexión a la base de datos SOM.
     * @param reference     Referencia SWAP del boleto (p. ej. {@code "SI/BAS1-1234"}).
     * @param show          Si {@code true}, muestra el reporte en el visor JasperViewer
     *                      y retorna {@code null}. Si {@code false}, exporta el reporte
     *                      a un archivo PDF temporal y lo retorna.
     * @return Archivo PDF temporal, o {@code null} si {@code show} es {@code true}
     *         o si ocurre algún error.
     */
    public static File createTicketPdf(final SGuiSession session, Connection somConnection, final String reference, final boolean show) {
        try {
            // Eliminar el prefijo de tipo de referencia (ej. "SI/") para quedarse con "sca_code-num":
            String refValue = reference.substring(reference.indexOf(SSwapConsts.SEPARATOR_REF) + SSwapConsts.SEPARATOR_REF.length());
            
            // Usar el último guión como separador para soportar códigos con guiones (ej. "BAS-1-1234"):
            int lastDash = refValue.lastIndexOf('-');
            String scaCode = refValue.substring(0, lastDash);
            int num = Integer.parseInt(refValue.substring(lastDash + 1));

            // Consultar el id_tic a partir del código de báscula y número de boleto:
            try (Statement st = somConnection.createStatement();
                 ResultSet rs = st.executeQuery(
                     "SELECT t.id_tic FROM s_tic AS t "
                     + "INNER JOIN su_sca AS s ON s.id_sca = t.fk_sca "
                     + "WHERE s.code = '" + scaCode + "' AND t.num = " + num + " LIMIT 1")) {
                if (rs.next()) {
                    return createTicketPdf(session, somConnection, rs.getInt("t.id_tic"), show);
                }
            }
        }
        catch (Exception e) {
            Logger.getLogger(SExportDataUtils.class.getName()).log(Level.SEVERE,
                    "Error resolving ticket ID from reference '" + reference + "'", e);
        }
        
        return null;
    }

    /**
     * Genera el PDF del boleto de báscula a partir de su ID.
     *
     * Dependiendo del parámetro {@code show}:
     * <ul>
     *   <li>{@code true}: muestra el reporte directamente en el visor
     *       JasperViewer (uso interactivo desde la UI) y retorna {@code null}.</li>
     *   <li>{@code false}: exporta el reporte a un archivo PDF temporal en la
     *       carpeta {@code temp/} y lo retorna (uso para subida a GCS u otros
     *       procesos en segundo plano).</li>
     * </ul>
     *
     * @param session       Sesión activa del usuario.
     * @param somConnection Conexión a la base de datos SOM.
     * @param idSomTic      ID del boleto de báscula ({@code s_tic.id_tic}).
     * @param show          Si {@code true}, muestra el visor. Si {@code false},
     *                      exporta a archivo.
     * @return Archivo PDF temporal, o {@code null} si {@code show} es {@code true}
     *         o si ocurre algún error.
     */
    public static File createTicketPdf(final SGuiSession session, Connection somConnection, final int idSomTic, final boolean show) {
        File oPdf = null;
        try {
            HashMap<String, Object> map = SExportDataSomUtils.createReportParamsMap(session);
            map.put("nTicketId", idSomTic);
            map.put("sTable", "s_tic");
            JasperPrint jasperPrint = SSessionServer.createJasperPrint(SDataConstantsSys.REP_TRN_SOM_TICKET, 
                                                map, 
                                                somConnection);
            if (show) {
                // Mostrar el reporte en el visor interactivo:
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Boleto de báscula #" + idSomTic);
                jasperViewer.setVisible(true);
                
                return null;
            }
            
            // Exportar a archivo PDF temporal para procesamiento en segundo plano:
            String fileName = "SOM_TIC_" + idSomTic;
            oPdf = new File("temp", fileName + (new Date()).hashCode() + ".pdf");
            FileOutputStream outputStreamPdf = new FileOutputStream(oPdf);
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStreamPdf);
            outputStreamPdf.close();
            
            return oPdf;
        }
        catch (Exception e) {
            Logger.getLogger(SExportDataUtils.class.getName()).log(Level.SEVERE, 
                    "Error exporting scale ticket report to PDF for ticket ID " + idSomTic, e);
        }
        
        return null;
    }
}
