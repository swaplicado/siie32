/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.SFileUtilities;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.cfg.db.SDbComImportLog;
import erp.mod.cfg.db.SDbComImportLogEntry;
import erp.mod.cfg.swap.SHttpConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.form.SImportedDocument;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SThinDps;
import erp.mtrn.data.cfd.SCfdRenderer;
import erp.mtrn.form.SDialogDpsFinder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SImportUtils {
    
    private static final int MODE_DOCS_ALL_FILES_AS_ZIP = 1;
    private static final int MODE_DOC_CFDI_FILES_IN_TEMP_DIR = 2;
    
    private static final String DownloadFilePrefix = "facturas compras "; // keep final blank space!
    
    public static final int FILES_ZIP = 0;
    public static final int CFDI_XML = 0;
    public static final int CFDI_PDF = 1;
    public static final SimpleDateFormat FormatDatetime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    
    /**
     * Get file name without extension.
     * @param fileName File name.
     * @param extension Estension.
     * @return 
     */
    private static String getFileNameWithoutExtension(final String fileName, final String extension) {
        String fileNameWithoutExtension;
        int extensionIndex = fileName.toLowerCase().lastIndexOf(extension.toLowerCase());
        
        if (extensionIndex != -1) {
            fileNameWithoutExtension = fileName.substring(0, extensionIndex);
        }
        else {
            fileNameWithoutExtension = fileName;
        }
        
        return fileNameWithoutExtension;
    }
    
    /**
     * Import and create a new invoice.
     * @param client GUI client.
     * @param isPurchase Is-purchase flag.
     * @param dialogDpsFinder DPS finder dialog.
     * @param cfdiXml XML CFDI file. Can be <code>null</code>. When it is <code>null</code>, then a file is required in an "open" dialog.
     * @param cfdiPdf PDF CFDI file. Can be <code>null</code>.
     * @param linkToOrder Link-to-order flag.
     * @param orderRequired Required order. Can be <code>null</code>. When it is <code>null</code> and an order must to be linked, then an order is required in DPS Finder dialog.
     * @param dueDateRequired Due date required. Can be <code>null</code>.
     * @return DPS key as <code>int[]</code> of new invoice created.
     * @throws java.lang.Exception
     */
    public static int[] importCfdi(final SClientInterface client, final boolean isPurchase, final SDialogDpsFinder dialogDpsFinder, final File cfdiXml, final File cfdiPdf, final boolean linkToOrder, final SDataDps orderRequired, final Date dueDateRequired) throws Exception {
        SDataDps invoice = null;
        SDataDps order = null; 

        if (linkToOrder) {
            if (orderRequired != null) {
                order = orderRequired;
            }
            else {
                int[] orderTypeKey = isPurchase ? SDataConstantsSys.TRNS_CL_DPS_PUR_ORD : SDataConstantsSys.TRNS_CL_DPS_SAL_ORD;
                
                dialogDpsFinder.formReset();
                dialogDpsFinder.setValue(SLibConstants.VALUE_FILTER_KEY, orderTypeKey);
                dialogDpsFinder.setVisible(true);

                if (dialogDpsFinder.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    order = (SDataDps) dialogDpsFinder.getValue(SDataConstants.TRN_DPS);
                }
            }
        }
        
        boolean chooserUsed = false;
        Exception exception = null;

        try {
            if (!linkToOrder || (linkToOrder && order != null)) {
                File chosenFile = cfdiXml;
                
                if (chosenFile == null) {
                    chooserUsed = true;
                    FileFilter filter = SFileUtilities.createFileNameExtensionFilter(SFileUtilities.xml);
                    client.getFileChooser().repaint();
                    client.getFileChooser().setAcceptAllFileFilterUsed(false);
                    client.getFileChooser().setFileFilter(filter);
                    
                    if (client.getFileChooser().showOpenDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
                        chosenFile = client.getFileChooser().getSelectedFile();
                    }
                }

                if (chosenFile.getName().toLowerCase().contains("." + SFileUtilities.xml)) {
                    SCfdRenderer renderer = new SCfdRenderer(client);
                    SDataDps newDps = renderer.renderCfdi(chosenFile, order, isPurchase ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);

                    if (newDps != null) {
                        int module = isPurchase ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;
                        int[] invoiceTypeKey = isPurchase ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV : SDataConstantsSys.TRNU_TP_DPS_SAL_INV;

                        newDps.setAuxFilePdf(cfdiPdf);
                        
                        if (newDps.getFkPaymentTypeId() == SDataConstantsSys.TRNS_TP_PAY_CREDIT && dueDateRequired != null) {
                            newDps.setDaysOfCreditByDueDate(dueDateRequired);
                        }
                        
                        client.getGuiModule(module).setFormComplement(new Object[] { invoiceTypeKey }); // document type key
                        client.getGuiModule(module).setAuxRegistry(newDps);

                        if (client.getGuiModule(module).showForm(SDataConstants.TRN_DPS, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                            client.getGuiModule(module).refreshCatalogues(SDataConstants.TRN_DPS);

                            invoice = (SDataDps) client.getGuiModule(module).getRegistry();
                            SDataUtilities.showDpsRecord(client, invoice);
                        }
                    }
                }
                else {
                    client.showMsgBoxInformation("El archivo proporcionado debe ser XML.\n"
                            + "(Archivo proporcionado: '" + chosenFile.getName() + "')");
                }
            }
        }
        catch (Exception e) {
            exception = e;
        }
        finally {
            if (chooserUsed) {
                client.getFileChooser().resetChoosableFileFilters();
                client.getFileChooser().setAcceptAllFileFilterUsed(true);
            }
        }
        
        if (exception != null) {
            throw exception;
        }
        
        return invoice != null ? (int[]) invoice.getPrimaryKey() : null;
    }
    
    /**
     * Create a new invoice.
     * @param client GUI client.
     * @param isPurchase Is-purchase flag.
     * @param dialogDpsFinder DPS finder dialog.
     * @param dpsXml XML DPS file. Can be <code>null</code>.
     * @param dpsPdf PDF DPS file. Can be <code>null</code>.
     * @param linkToOrder Link-to-order flag.
     * @param orderRequired Required order. Can be <code>null</code>. When it is <code>null</code> and an order must to be linked, then an order is required in DPS Finder dialog.
     * @param importedDocument Documento importado.
     * @return DPS key as <code>int[]</code> of new invoice created.
     * @throws java.lang.Exception
     */
    public static int[] createDps(final SClientInterface client, final boolean isPurchase, final SDialogDpsFinder dialogDpsFinder, final File dpsXml, final File dpsPdf, final boolean linkToOrder, final SDataDps orderRequired, final SImportedDocument importedDocument) throws Exception {
        SDataDps invoice = null;
        SDataDps order = null; 

        if (linkToOrder) {
            if (orderRequired != null) {
                order = orderRequired;
            }
            else {
                int[] orderTypeKey = isPurchase ? SDataConstantsSys.TRNS_CL_DPS_PUR_ORD : SDataConstantsSys.TRNS_CL_DPS_SAL_ORD;
                
                dialogDpsFinder.formReset();
                dialogDpsFinder.setValue(SLibConstants.VALUE_FILTER_KEY, orderTypeKey);
                dialogDpsFinder.setVisible(true);

                if (dialogDpsFinder.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    order = (SDataDps) dialogDpsFinder.getValue(SDataConstants.TRN_DPS);
                }
            }
        }
        
        Exception exception = null;

        try {
            if (!linkToOrder || (linkToOrder && order != null)) {
                int module = isPurchase ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;
                int[] invoiceTypeKey = isPurchase ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV : SDataConstantsSys.TRNU_TP_DPS_SAL_INV;
                
                Object complement;
                
                if (order != null) {
                    complement = new Object[] { invoiceTypeKey, false, order };
                }
                else {
                    complement = new Object[] { invoiceTypeKey };
                }
                
                SDataDps newDps = null;
                
                if (importedDocument != null) {
                    newDps = importedDocument.createDps(client.getSession(), order);
                    newDps.setAuxFileXml(dpsXml);
                    newDps.setAuxFilePdf(dpsPdf);
                }

                client.getGuiModule(module).setFormComplement(complement);
                client.getGuiModule(module).setAuxRegistry(newDps);

                if (client.getGuiModule(module).showForm(SDataConstants.TRN_DPS, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                    client.getGuiModule(module).refreshCatalogues(SDataConstants.TRN_DPS);

                    invoice = (SDataDps) client.getGuiModule(module).getRegistry();
                    SDataUtilities.showDpsRecord(client, invoice);
                }
            }
        }
        catch (Exception e) {
            exception = e;
        }
        
        if (exception != null) {
            throw exception;
        }
        
        return invoice != null ? (int[]) invoice.getPrimaryKey() : null;
    }
    
    /**
     * Download documents files.
     * @param session GUI session.
     * @param serviceUrl Download service URL.
     * @param downloadMode Download mode.
     * @param documents List of external document IDs whose files needs to be downloaded.
     * @return 
     * @throws java.lang.Exception 
     */
    private static File[] downloadDocumentsFiles(final SGuiSession session, final String serviceUrl, final int downloadMode, final ArrayList<Integer> documents) throws Exception {
        File[] files = null;
        File zipFile = null;
        Path tempDir = null;
        Path tempFile = null;
        Exception exception = null;
        HttpURLConnection conn = null;
        String ids = documents.stream().map(String::valueOf).collect(Collectors.joining(", "));
        
        try {
            // open download service connection:
            
            URL url = new URL(serviceUrl);
            String charset = java.nio.charset.StandardCharsets.UTF_8.name();

            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(SSwapConsts.TIME_180_SEC); // timeout para conectar
            conn.setReadTimeout(SSwapConsts.TIME_180_SEC); // timeout para leer la respuesta
            conn.setRequestMethod(SHttpConsts.METHOD_POST);
            conn.setRequestProperty("Accept-Charset", charset);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            //conn.setDoInput(true);

            // send JSON body required by API:
            
            Date requestDatetime = new Date();
            String requestBody = "{\"document_ids\": [" + ids + "]}"; // example payload

            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes("UTF-8"));
            }

            // parse custom header
            
            Date responseDatetime = null;
            String responseBody = "";
            String summaryHeader = conn.getHeaderField("x-download-summary");

            if (summaryHeader != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode summaryJson = mapper.readTree(summaryHeader);

                responseDatetime = new Date();
                responseBody = summaryJson.toPrettyString();

                System.out.println("Download summary from header:");
                System.out.println(responseBody);
            }

            // choose download ZIP file:
            
            String companyCode = SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.CFGU_CO, new int[] { session.getConfigCompany().getCompanyId() }, SLibConstants.DESCRIPTION_CODE);
            
            switch (downloadMode) {
                case MODE_DOCS_ALL_FILES_AS_ZIP:
                    // ask for desired ZIP file:
                    
                    FileFilter filter = SFileUtilities.createFileNameExtensionFilter(SFileUtilities.zip);
                    JFileChooser fileChooser = session.getClient().getFileChooser();
                    fileChooser.repaint();
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    fileChooser.setFileFilter(filter);

                    fileChooser.setSelectedFile(new File(DownloadFilePrefix + companyCode + " " + FormatDatetime.format(new Date()) + "." + SFileUtilities.zip));

                    if (fileChooser.showSaveDialog(session.getClient().getFrame()) == JFileChooser.APPROVE_OPTION) {
                        zipFile = fileChooser.getSelectedFile();

                        // Ensure file ends with ".zip"
                        if (!zipFile.getName().toLowerCase().endsWith("." + SFileUtilities.zip)) {
                            zipFile = new File(zipFile.getAbsolutePath() + "." + SFileUtilities.zip);
                        }
                    }
                    break;
                    
                case MODE_DOC_CFDI_FILES_IN_TEMP_DIR:
                    // set ZIP file in temporal directory:
                    tempDir = Files.createTempDirectory(SSwapConsts.SIIE + "_" + companyCode);
                    System.out.println("Temporary directory created at: " + tempDir);
                    
                    tempFile = Files.createFile(tempDir.resolve(DownloadFilePrefix + FormatDatetime.format(new Date()) + "." + SFileUtilities.zip));
                    zipFile = tempFile.toFile();
                    break;
                    
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }
            
            // download and process chosen file:
            
            if (zipFile != null) {
                // save the ZIP file locally:
                
                String zipPath = zipFile.getAbsolutePath();
                System.out.println("Saving file as: " + zipFile.getAbsolutePath());

                try (InputStream in = conn.getInputStream(); FileOutputStream out = new FileOutputStream(zipPath)) {
                    int bytesRead;
                    byte[] buffer = new byte[8192];

                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
                
                // finish processing:

                switch (downloadMode) {
                    case MODE_DOCS_ALL_FILES_AS_ZIP:
                        // log import downloads:
                        
                        System.out.println("Loging import downloads...");
                        
                        SImportUtils.logImportDownloads(session, requestBody, requestDatetime, SHttpConsts.RSC_SUCC_OK, responseBody, responseDatetime, documents);
                        
                        files = new File[] { zipFile };
                        break;

                    case MODE_DOC_CFDI_FILES_IN_TEMP_DIR:
                        // decompress files in temporal directory:
                        
                        System.out.println("Extracting temporal files...");
                        
                        File xmlFile = null;
                        String xmlFileName = "";
                        File pdfFile = null;
                        HashMap<String, File> pdfFilesMap = new HashMap<>();
                        
                        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(Paths.get(zipPath)))) {
                            ZipEntry entry;

                            while ((entry = zis.getNextEntry()) != null) {
                                System.out.println("Extracting: " + entry.getName());

                                File newFile = new File(tempDir.toString() + "\\output", entry.getName());
                                newFile.getParentFile().mkdirs();

                                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                                    int bytesRead;
                                    byte[] buffer = new byte[8192];

                                    while ((bytesRead = zis.read(buffer)) > 0) {
                                        fos.write(buffer, 0, bytesRead);
                                    }
                                }

                                zis.closeEntry();
                                
                                if (newFile.getName().toLowerCase().endsWith("." + SFileUtilities.xml) && xmlFile == null) {
                                    // choose firt retrieved XML:
                                    xmlFile = newFile;
                                    xmlFileName = getFileNameWithoutExtension(xmlFile.getName(), "." + SFileUtilities.xml);
                                }
                                else if (newFile.getName().toLowerCase().endsWith("." + SFileUtilities.pdf)) {
                                    // reserve all retrieved PDF's:
                                    pdfFilesMap.put(getFileNameWithoutExtension(newFile.getName(), "." + SFileUtilities.pdf), newFile);
                                }
                                
                                if (!xmlFileName.isEmpty() && !pdfFilesMap.isEmpty() && pdfFile == null) {
                                    pdfFile = pdfFilesMap.get(xmlFileName); // choose the right PDF by matching the same name as irs XML counterpart
                                }
                                
                                if (xmlFile != null && pdfFile != null) {
                                    break; // no more files needed!
                                }
                            }
                            
                            if (xmlFile != null && pdfFile == null && !pdfFilesMap.isEmpty()) {
                                // last chance, choose firt retrieved PDF:
                                pdfFile = (File) pdfFilesMap.values().toArray()[0];
                            }
                        }
                        
                        files = new File[] { xmlFile, pdfFile };
                        break;

                    default:
                        // nothing
                }
            }
        }
        catch (Exception e) {
            exception = e;
        }
        finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                }
                catch (Exception e) {
                    exception = e;
                }
            }
            
            if (downloadMode == MODE_DOCS_ALL_FILES_AS_ZIP) {
                JFileChooser fileChooser = session.getClient().getFileChooser();
                fileChooser.resetChoosableFileFilters();
                fileChooser.setAcceptAllFileFilterUsed(true);
            }
        }
        
        if (exception != null) {
            throw exception;
        }
        
        return files;
    }

    /**
     * Download documents all files as ZIP.
     * @param session GUI session.
     * @param serviceUrl Download service URL.
     * @param documents List of external document IDs whose files needs to be downloaded.
     * @return File array of 1 element: the ZIP file.
     * @throws java.lang.Exception 
     */
    public static File[] downloadDocumentsAllFilesAsZip(final SGuiSession session, final String serviceUrl, final ArrayList<Integer> documents) throws Exception {
        return downloadDocumentsFiles(session, serviceUrl, MODE_DOCS_ALL_FILES_AS_ZIP, documents);
    }

    /**
     * Download document XML & PDF files as ZIP.
     * @param session GUI session.
     * @param serviceUrl Download service URL.
     * @param document External document ID whose XML & PDF files needs to be downloaded.
     * @return File array of 2 elements: the XML (at index 0) & PDF (at index 1) files.
     * @throws java.lang.Exception 
     */
    public static File[] downloadDocumentCfdiFilesInTempDir(final SGuiSession session, final String serviceUrl, final int document) throws Exception {
        ArrayList<Integer> documents = new ArrayList<>();
        documents.add(document);
        return downloadDocumentsFiles(session, serviceUrl, MODE_DOC_CFDI_FILES_IN_TEMP_DIR, documents);
    }
    
    /**
     * Create prepared statement to count imports:
     * Index:   Parameter:
     *      1   Sync type.
     *      2   Response code.
     *      3   User ID.
     *      4   Entry response code.
     *      5   Reference ID.
     * @param session GUI session.
     * @return Prepared statement.
     * @throws Exception 
     */
    public static PreparedStatement createPreparedStatementToCountImports(final SGuiSession session) throws Exception {
        String sql = "SELECT COUNT(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFG_COM_IMP_LOG) + " AS il "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFG_COM_IMP_LOG_ETY) + " AS ile ON ile.id_sync_log = il.id_sync_log "
                + "WHERE il.sync_type = ? AND il.response_code = ? AND il.fk_usr = ? AND ile.response_code = ? AND ile.reference_id = ?;";
        
        return session.getStatement().getConnection().prepareStatement(sql);
    }
    
    /**
     * Count imports.
     * @param preparedStatement
     * @param syncType
     * @param responseCode
     * @param userId
     * @param referenceId
     * @throws Exception 
     */
    public static int countImports(final PreparedStatement preparedStatement, final String syncType, final String responseCode, final int userId, final String referenceId) throws Exception {
        int count = 0;
        
        preparedStatement.setString(1, syncType);
        preparedStatement.setString(2, responseCode);
        preparedStatement.setInt(3, userId);
        preparedStatement.setString(4, responseCode);
        preparedStatement.setString(5, referenceId);
        
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        
        return count;
    }
    
    /**
     * Log import downloads.
     * @param session GUI session.
     * @param requestBody Service request body as JSON.
     * @param requestDatetime Service request datetime.
     * @param httpResponseStatusCode HTTP response status code.
     * @param responseBody Service response body as JSON.
     * @param responseDatetime Service response datetime.
     * @param documents List of IDs of downloaded documents.
     * @throws Exception 
     */
    public static void logImportDownloads(final SGuiSession session, final String requestBody, final Date requestDatetime, final int httpResponseStatusCode, final String responseBody, final Date responseDatetime, final ArrayList<Integer> documents) throws Exception {
        SDbComImportLog log = new SDbComImportLog();
        
        //log.setPkSyncLogId(...);
        log.setSyncType(SDbComImportLog.SYNC_TYPE_PUR_INV);
        //log.msRequestBodyFileName...
        log.setRequestTimestamp(requestDatetime);
        log.setResponseCode("" + httpResponseStatusCode);
        //log.msResponseBodyFileName...
        log.setResponseTimestamp(responseDatetime);
        //log.mnFkUserId...
        //log.mtTsUser...
        
        for (Integer document : documents) {
            SDbComImportLogEntry entry = new SDbComImportLogEntry();
            
            //entry.setPkSyncLogId(...);
            //entry.setPkEntryId(...);
            entry.setResponseCode("" + SHttpConsts.RSC_SUCC_OK);
            entry.setResponseBody("");
            entry.setReferenceId("" + document);
            entry.setReferenceUuid("" + document);
            //entry.setFkDpsYearId_n(...);
            //entry.setFkDpsDocumentId_n(...);
            //entry.setTsSync(...);
            
            log.getEntries().add(entry);
        }
        
        log.save(session);
        
        SExportLogsUtils.safeWriteToLogFile(log.getRequestBodyFileName(), requestBody);
        SExportLogsUtils.safeWriteToLogFile(log.getResponseBodyFileName(), responseBody);
    }
    
    /**
     * Update DPS days of credit by required due date.
     * @param session GUI session.
     * @param dpsKey DPS primary key.
     * @param dueDate Required due date.
     * @return 
     */
    public static boolean updateDpsDaysOfCreditByDueDate(final SGuiSession session, final int[] dpsKey, final Date dueDate) throws Exception {
        boolean updated = false;
        
        SThinDps dps = new SThinDps();
        dps.read(dpsKey, session.getStatement());
        
        if (dps.getFkPaymentTypeId() != SDataConstantsSys.TRNS_TP_PAY_CREDIT) {
            throw new Exception("El documento '" + dps.getDpsNumber() + "' debe tener tipo de pago 'crédito'.");
        }
        else if (dueDate.before(dps.getDate())) {
            throw new Exception("La fecha requerida de vencimiento (" + SLibUtils.DateFormatDate.format(dueDate) + ") no puede ser anterior a la fecha del documento '" + dps.getDpsNumber() + "' (" + SLibUtils.DateFormatDate.format(dps.getDate()) + ").");
        }
        else if (dueDate.before(dps.getDateStartOfCredit())) {
            throw new Exception("La fecha requerida de vencimiento (" + SLibUtils.DateFormatDate.format(dueDate) + ") no puede ser anterior a la fecha base de crédito del documento '" + dps.getDpsNumber() + "' (" + SLibUtils.DateFormatDate.format(dps.getDateStartOfCredit()) + ").");
        }
        else {
            long daysOfCredit = SLibTimeUtils.getDaysDiff(dueDate, dps.getDateStartOfCredit());
            
            if (dps.getDaysOfCredit() != (int) daysOfCredit) {
                String sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) +  " SET "
                        + "days_cred = " + (int) daysOfCredit + " "
                        + "WHERE id_year = " + dps.getPkYearId() + " AND id_doc = " + dps.getPkDocId() + ";";
                
                session.getStatement().execute(sql);
                updated = true;
            }
        }
        
        return updated;
    }
    
    /**
     * Create DPS folio from reference folio for given reference prefix.
     * @param refFolio Reference folio, e.g., "OC/A-100".
     * @param refPrefix Reference prefix, e.g., "OC".
     * @return DPS folio.
     */
    public static DpsFolio createDpsFolio(final String refFolio, final String refPrefix) {
        DpsFolio dpsFolio = null;
        
        if (!refFolio.isEmpty() && !refPrefix.isEmpty()) {
            String prefix = refPrefix + SSwapConsts.SEPARATOR_DOC_REF;
            String folio = refFolio.substring(prefix.length());
            String[] folioElements = folio.split("-");
            String series = folioElements.length == 1 ? "" : folioElements[0];
            String number = folioElements.length == 1 ? folioElements[0] : folioElements[1];
            
            dpsFolio = new DpsFolio(series, number);
        }
        
        return dpsFolio;
    }
    
    /**
     * Create DPS key from reference key.
     * @param refKey Reference key, e.g., "2025_1".
     * @return 
     */
    public static DpsKey createDpsKey(final String refKey) {
        DpsKey dpsKey = null;
        
        if (!refKey.isEmpty()) {
            String[] keyElements = refKey.split("_");
            
            if (keyElements.length == 2) {
                int yearId = SLibUtils.parseInt(keyElements[0]);
                int docId = SLibUtils.parseInt(keyElements[1]);
                
                dpsKey = new DpsKey(yearId, docId);
            }
        }
        
        return dpsKey;
    }
    
    /**
     * In memory DPS folio.
     */
    public static class DpsFolio {
        
        public String Series;
        public String Number;
        
        public DpsFolio(final String series, final String number) {
            Series = series;
            Number = number;
        }
        
        public String getFolio() {
            return Series + (Series.isEmpty() ? "" : "-") + Number;
        }
    }
    
    /**
     * In memory DPS key.
     */
    public static class DpsKey {
        
        public int YearId;
        public int DocId;
        
        public DpsKey(final int yearId, final int docId) {
            YearId = yearId;
            DocId = docId;
        }
        
        public int[] asKey() {
            return YearId != 0 && DocId != 0 ? new int[] { YearId, DocId } : null;
        }
    }
}
