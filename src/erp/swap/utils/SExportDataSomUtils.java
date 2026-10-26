/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.utils;

import erp.SFileUtilities;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SDataParamsCompany;
import erp.swap.SSwapConsts;
import erp.server.SSessionServer;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
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
 * @author Edwin Carmona, Sergio Flores
 */
public abstract class SExportDataSomUtils {
    
    private static final DecimalFormat FormatScaleTicketId = new DecimalFormat(SLibUtils.textRepeat("0", 9)); // 000000000
    
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
    private static HashMap<String, Object> createReportParamsMap(final SGuiSession session) {
        HashMap<String, Object> map = new HashMap<>();

        // Parameters that need to be declared in reports:

        map.put("sCompanyName", session.getConfigCompany().getCompanyId());
        map.put("sImageDir", ((SDataParamsCompany) session.getConfigCompany()).getImagesDirectory());
        
//        map.put("sUserName", session.getUser().getName());
//        map.put("sAppName", "SOM");
//        map.put("sAppCopyright", "COPYRIGTH");
//        map.put("sAppProvider", "SWAPLICADO");
//        map.put("sVendorCopyright", "AETH");
//        map.put("sVendorWebsite", "SWAPLICADO.COM");

        // Optional parameters:

//        map.put("oFormatDate", SLibUtils.DateFormatDate);
//        map.put("oFormatDateShort", SLibUtils.DateFormatDateShort);
//        map.put("oFormatDatetime", SLibUtils.DateFormatDatetime);
//        map.put("oFormatDatetimeTic", SLibUtils.DateFormatDatetime);
//        map.put("oFormatTime", SLibUtils.DateFormatTime);
//        map.put("oFormatValue", SLibUtils.DecimalFormatValue0D);
//        map.put("oFormatValue0D", SLibUtils.DecimalFormatValue0D);
//        map.put("oFormatValue2D", SLibUtils.DecimalFormatValue2D);
//        map.put("oFormatValue4D", SLibUtils.DecimalFormatValue4D);
//        map.put("oFormatValue8D", SLibUtils.DecimalFormatValue8D);

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
            String scaleCode = refValue.substring(0, lastDash);
            int ticketNumber = Integer.parseInt(refValue.substring(lastDash + 1));

            // Consultar el id_tic a partir del código de báscula y número de boleto:
            try (Statement st = somConnection.createStatement();
                 ResultSet rs = st.executeQuery(
                     "SELECT t.id_tic FROM s_tic AS t "
                     + "INNER JOIN su_sca AS s ON s.id_sca = t.fk_sca "
                     + "WHERE s.code = '" + scaleCode + "' AND t.num = '" + ticketNumber + "' LIMIT 1")) {
                if (rs.next()) {
                    return createTicketPdf(session, somConnection, rs.getInt("t.id_tic"), true, show);
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
     * @param session Sesión activa del usuario.
     * @param somConnection Conexión a la base de datos SOM.
     * @param somTicketId ID del boleto de báscula ({@code s_tic.id_tic}).
     * @param forceReissue Forzar reemisión del PDF del boleto.
     * @param showInJasperViewer Si <code>true</code>, muestra la impresión el visor Jasper, si no, exportar a archivo.
     * @return Archivo PDF temporal, o {@code null} si {@code show} es {@code true} o si ocurre algún error.
     */
    public static File createTicketPdf(final SGuiSession session, final Connection somConnection, final int somTicketId, final boolean forceReissue, final boolean showInJasperViewer) {
        File pdf = null;
        
        try {
            HashMap<String, Object> map = SExportDataSomUtils.createReportParamsMap(session);
            map.put("nTicketId", somTicketId);
            map.put("sTable", "s_tic");
            JasperPrint jasperPrint = SSessionServer.createJasperPrint(SDataConstantsSys.REP_TRN_SOM_TICKET, map, somConnection);
            
            if (showInJasperViewer) {
                // Mostrar el reporte en el visor interactivo:
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Boleto de báscula #" + somTicketId);
                jasperViewer.setVisible(true);
                
                return null;
            }
            
            // Exportar a archivo PDF temporal para procesamiento en segundo plano:
            
            String sysTempDir = System.getProperty("java.io.tmpdir");
            File localTempDir = new File(sysTempDir + (sysTempDir.endsWith("\\") ? "" : "\\") + "SOM");

            if (!localTempDir.exists()) {
                boolean ok = localTempDir.mkdirs();
                if (!ok) {
                    throw new RuntimeException("Failed to create directory: " + localTempDir.getAbsolutePath());
                }
            }
            
            String pdfFileName = "SOM_TIC_" + FormatScaleTicketId.format(somTicketId) + (forceReissue ? "_" + (new Date()).hashCode() : "") + "." + SFileUtilities.pdf;
            String pdfAbsolutePath = localTempDir.getAbsolutePath() + "\\" + pdfFileName;
            
            pdf = new File(pdfAbsolutePath);

            if (forceReissue || !pdf.exists()) {
                FileOutputStream outputStreamPdf = new FileOutputStream(pdf);
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStreamPdf);
                outputStreamPdf.close();
            }
        }
        catch (Exception e) {
            pdf = null;
            Logger.getLogger(SExportDataUtils.class.getName()).log(Level.SEVERE,  "Error exporting scale ticket report to PDF for ticket ID " + somTicketId + ".", e);
        }
        
        return pdf;
    }
    /**
     * Recuperar el ID del boleto de báscula SOM a partir de su número de boleto.
     * @param somConnection Conexión a la BD de SOM.
     * @param somTicketNumber Número de boleto de báscula SOM.
     * @return 
     */
    public static int retrieveTicketId(final Connection somConnection, final String somTicketNumber) throws Exception {
        int id = 0;
        String sql = "SELECT id_tic "
                + "FROM s_tic "
                + "WHERE num = '" + somTicketNumber + "' AND NOT b_del "
                + "LIMIT 1;";
        
        try (ResultSet resultSet = somConnection.createStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                id = resultSet.getInt("id_tic");
            }
        }
        
        return id;
    }
}
