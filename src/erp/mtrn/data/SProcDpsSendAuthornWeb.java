/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import com.swaplicado.cloudstoragemanager.CloudStorageManager;
import com.swaplicado.data.CloudStorageFile;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.cfg.db.SDbAuthorizationPath;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.hrs.utils.SDocUtils;
import erp.mod.trn.db.SDbDps;
import erp.mod.trn.db.SDbMaterialRequest;
import erp.mod.trn.db.SDbMaterialRequestExternalStorageLog;
import erp.mod.trn.db.SDbSupplierFile;
import erp.mod.trn.db.SDbSupplierFileProcess;
import erp.mod.trn.db.SMaterialRequestUtils;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiReport;

/**
 *
 * @author Isabel Servín, Edwin Carmona
 */
public class SProcDpsSendAuthornWeb extends Thread {

    private final SClientInterface miClient;
    private final SDbSupplierFileProcess moSuppFileProc;
    private final ArrayList<SDbAuthorizationPath> maAuthPaths;
    private final int mnPriority;
    private final String msAuthNotes;
    
    private String msError;

    public SProcDpsSendAuthornWeb(SClientInterface client, SDbSupplierFileProcess proc, ArrayList<SDbAuthorizationPath> paths, int priority, String notes) {
        miClient = client;
        moSuppFileProc = proc;
        maAuthPaths = paths;
        mnPriority = priority;
        msAuthNotes = notes;
        setDaemon(true);
    }

    @Override
    public void run() {
        Connection connection = miClient.getSession().getDatabase().getConnection();
        SDbDps dps = moSuppFileProc.getDps();
        SDataDpsAuthorn auth = new SDataDpsAuthorn();
        try {
            auth.setPkYearId(dps.getPkYearId());
            auth.setPkDocId(dps.getPkDocId());
            auth.setNotes(msAuthNotes);
            auth.setFkAuthorizationStatusId(SDataConstantsSys.CFGS_ST_AUTHORN_SND);
            auth.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
            auth.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
            auth.setFkUserDeleteId(miClient.getSession().getUser().getPkUserId());
            auth.save(connection);

            if (auth.getLastDbActionResult() == SLibConstants.DB_ACTION_SAVE_OK) {
                if (sendAuthorn()) {
                    auth.setFkAuthorizationStatusId(SDataConstantsSys.CFGS_ST_AUTHORN_PEND);
                    auth.save(miClient.getSession().getDatabase().getConnection());
                    moSuppFileProc.updateDpsStatus(miClient.getSession(), SDataConstantsSys.TRNS_ST_DPS_AUTHORN_PENDING);
                }
                else {
                    auth.setFkAuthorizationStatusId(SDataConstantsSys.CFGS_ST_AUTHORN_SNDF); 
                    if (msError.length() > 511) {
                        auth.setException(SLibUtilities.textLeft(msError, 511));
                    }
                    else {
                        auth.setException(msError);
                    }
                    auth.save(connection);
                }
            }
            else {
                miClient.showMsgBoxWarning("Ocurrió un error al cambiar el estatus del documento.\nContactar a soporte.");
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxWarning(e.getMessage());
        }
    }

    private boolean sendAuthorn() throws Exception {
        //Thread.sleep(60000);
        // Envío de archivos
        String sResult = "";
        if (moSuppFileProc != null && moSuppFileProc.getSuppFiles().size() > 0) {
            sResult = this.sendFilesToCloud();
        }
        
        if (!sResult.isEmpty()) {
            miClient.showMsgBoxWarning(sResult);
            return false;
        }

        SAuthorizationUtils.processAuthorizationsDps(miClient.getSession(), maAuthPaths, mnPriority, SAuthorizationUtils.AUTH_TYPE_DPS, moSuppFileProc.getPrimaryKey(), true);

        System.out.println("Documento enviado a autorización con éxito.");
        return true;
    }

    /**
     * Envía los archivos asociados a un proceso a la nube y maneja errores en
     * el proceso de eliminación y carga.
     *
     * @return Un mensaje de error si ocurre un problema, o una cadena vacía si
     * la operación es exitosa.
     */
    private String sendFilesToCloud() {
        boolean hasError = false;
        msError = ""; // Mensaje de error inicializado vacío

        try {
            // Eliminar los archivos existentes en la nube, se para a libreria para hacer uso en diferentes lugares
            SDocUtils.deleteFilesToCloud(miClient.getSession(), moSuppFileProc); 
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            msError += "Error al eliminar los archivos anteriores, intente de nuevo o contacte a soporte técnico. " + e.getMessage() + " ";
        }

        if (!hasError) {
            // 2. Subir nuevos archivos
            ArrayList<SDbSupplierFile> lFileNamesToUpload = new ArrayList<>();

            for (SDbSupplierFile oFile : moSuppFileProc.getSuppFiles()) {
                try {
                    // Obtener bytes del archivo y generar un nombre único para el almacenamiento en la nube
                    byte[] fileBytes = SDocUtils.getFileBytes(miClient.getSession(), SDocUtils.BUCKET_DOC_DPS_SUPPLIER, oFile.getFilevaultId());
                    String name = SDocUtils.generateFileName(miClient.getSession().getDatabase().getDbName(),
                            moSuppFileProc.getPkYearId(),
                            moSuppFileProc.getPkDocId(),
                            oFile.getPkSupplierFileId(),
                            oFile.getFileType());

                    // Subir archivo a la nube
                    CloudStorageFile oGcsFile = CloudStorageManager.uploadFileData(fileBytes, name);
                    if (oGcsFile != null) {
                        oFile.setFileStorageName(oGcsFile.getFileName());
                        oFile.save(miClient.getSession());
                        lFileNamesToUpload.add(oFile);
                    }
                    else {
                        hasError = true; // Marcar error si falla la subida
                        break;
                    }
                }
                catch (Exception e) {
                    hasError = true;
                    e.printStackTrace();
                    msError += "Error al enviar los archivos, intente de nuevo o contacte a soporte técnico. " + e.getMessage() + " ";
                    break;
                }
            }

            // 3. Leer las requisiciones de materiales asociadas al documento
            if (!hasError) {
                try {
                    moSuppFileProc.readMaterialRequests(miClient.getSession());
                    for (SDbMaterialRequest mat : moSuppFileProc.getMaterialRequests()) {
                        // Leer en memoria el archivo de la RM
                        String name = miClient.getSession().getDatabase().getDbName() + "-RM-" + mat.getPkMatRequestId() + ".pdf";

                        if(!CloudStorageManager.storagedFileExists(name)) {
                            // Guardar en el log la RM que se subió
                            HashMap<String, Object> params = SMaterialRequestUtils.createMatReqParamsMapPdf((SGuiClient) miClient, mat.getPkMatRequestId());
                            SGuiReport report = new SGuiReport("reps/trn_mat_req.jasper", "Requisición de materiales");
                            File matReqFile = new File(report.getFileName());
                            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(matReqFile);
                            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, miClient.getSession().getStatement().getConnection());
                            byte[] fileBytes = JasperExportManager.exportReportToPdf(jasperPrint);
                            SDbMaterialRequestExternalStorageLog extStoLog = new SDbMaterialRequestExternalStorageLog();
                            extStoLog.setPkMatRequestId(mat.getPkMatRequestId());
                            extStoLog.save(miClient.getSession());

                            // Subir los formato de impresión de la requisición de materiales a la nube
                            CloudStorageFile oGcsFile = CloudStorageManager.uploadFileData(fileBytes, name);
                            if (oGcsFile == null) {
                                hasError = true; // Marcar error si falla la subida
                                msError += "Error al enviar los archivos de RM, intente de nuevo o contacte a soporte técnico.";
                                break;
                            }
                        }
                    }
                }
                catch (Exception e) {
                    hasError = true;
                    msError += "Error al enviar los archivos de RM, intente de nuevo o contacte a soporte técnico. " + e.getMessage() + " ";
                }
            }

            // 4. Manejo de errores en la subida
            if (hasError) {
                try {
                    // Si ocurrió un error, eliminar los archivos subidos previamente
                    ArrayList<String> lNames = lFileNamesToUpload.stream()
                            .map(SDbSupplierFile::getFileStorageName) // Extraer nombres de archivo
                            .collect(Collectors.toCollection(ArrayList::new));
                    String resultOfUpload = CloudStorageManager.deleteFiles(lNames);

                    for (SDbSupplierFile oFile : lFileNamesToUpload) {
                        oFile.setFileStorageName("");
                        try {
                            oFile.save(miClient.getSession());
                        }
                        catch (Exception ex) {
                            Logger.getLogger(SProcDpsSendAuthornWeb.class.getName()).log(Level.SEVERE, null, ex);
                            msError += ex.getMessage();
                        }
                    }
                }
                catch (Exception e) {
                    msError += e.getMessage();
                }
                return msError; // Retornar mensaje de error
            }
        }

        return msError; // Vacio = operación exitosa
    } 
}
