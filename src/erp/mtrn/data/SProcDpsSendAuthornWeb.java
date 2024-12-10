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
import erp.mod.hrs.utils.SDocUtils;
import erp.mod.cfg.db.SDbAuthorizationPath;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.trn.db.SDbDps;
import erp.mod.trn.db.SDbSupplierFile;
import erp.mod.trn.db.SDbSupplierFileProcess;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Isabel Servín
 */
public class SProcDpsSendAuthornWeb extends Thread {

    private final SClientInterface miClient;
    private final SDbSupplierFileProcess moSuppFileProc;
    private final ArrayList<SDbAuthorizationPath> maAuthPaths;

    public SProcDpsSendAuthornWeb(SClientInterface client, SDbSupplierFileProcess proc, ArrayList<SDbAuthorizationPath> paths) {
        miClient = client;
        moSuppFileProc = proc;
        maAuthPaths = paths;
        setDaemon(true);
    }

    @Override
    public void run() {
        try {
            Connection connection = miClient.getSession().getDatabase().getConnection();
            SDbDps dps = moSuppFileProc.getDps();
            SDataDpsAuthorn auth = new SDataDpsAuthorn();
            auth.setPrimaryKey(dps.getPrimaryKey());
            auth.setFkAuthorizationStatusId(SDataConstantsSys.CFGS_ST_AUTHORN_SND);
            auth.setFkUserId(miClient.getSession().getUser().getPkUserId());
            auth.save(connection);

            if (sendAuthorn()) {
                auth.setFkAuthorizationStatusId(SDataConstantsSys.CFGS_ST_AUTHORN_PEND);
                auth.save(connection);
                moSuppFileProc.updateDpsStatus(miClient.getSession(), SDataConstantsSys.TRNS_ST_DPS_AUTHORN_PENDING);
            }
            else {
                auth.delete(connection);
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxWarning(e.getMessage());
        }
    }

    private boolean sendAuthorn() throws Exception {
        boolean send = true;
//        Thread.sleep(60000);
        // Envío de archivos
        String sResult = this.sendFilesToCloud();
        if (!sResult.isEmpty()) {
            miClient.showMsgBoxWarning(sResult);
            return false;
        }

        SAuthorizationUtils.processAuthorizationsDps(miClient.getSession(), maAuthPaths,
                SAuthorizationUtils.AUTH_TYPE_DPS, moSuppFileProc.getPrimaryKey(), true);

        System.out.println("Documento enviado con éxito.");
        return send;
    }

    /**
     * Envía los archivos asociados a un proceso a la nube y maneja errores en
     * el proceso de eliminación y carga.
     *
     * @return Un mensaje de error si ocurre un problema, o una cadena vacía si
     * la operación es exitosa.
     */
    private String sendFilesToCloud() {
        // Verifica si hay archivos para procesar
        if (moSuppFileProc != null && moSuppFileProc.getSuppFiles().size() > 0) {
            String sError = ""; // Mensaje de error inicializado vacío

            try {
                // 1. Eliminar archivos existentes en la nube
                ArrayList<SDbSupplierFile> lFilesToDelete = new ArrayList<>();

                // Agregar archivos actuales con nombre de almacenamiento para eliminar
                for (SDbSupplierFile oFile : moSuppFileProc.getSuppFiles()) {
                    if (oFile.getFileStorageName() != null && !oFile.getFileStorageName().isEmpty()) {
                        lFilesToDelete.add(oFile);
                    }
                }

                // Agregar archivos marcados como eliminados
                for (SDbSupplierFile oFile : moSuppFileProc.getSuppFilesDeleted()) {
                    if (oFile.getFileStorageName() != null && !oFile.getFileStorageName().isEmpty()) {
                        lFilesToDelete.add(oFile);
                    }
                }

                // Crear lista de nombres de archivos a eliminar y llamar al gestor de almacenamiento
                ArrayList<String> lNames = lFilesToDelete.stream()
                        .map(SDbSupplierFile::getFileStorageName) // Extraer nombres de archivo
                        .collect(Collectors.toCollection(ArrayList::new));
                String resultDeleted = CloudStorageManager.deleteFiles(lNames);

                // Actualizar los archivos locales eliminados
                for (SDbSupplierFile oFile : lFilesToDelete) {
                    oFile.setFileStorageName("");
                    oFile.save(miClient.getSession());
                }
            } catch (Exception e) {
                e.printStackTrace();
                sError = "Error al eliminar los archivos anteriores, intente de nuevo o contacte a soporte técnico. " + e.getMessage();
            }

            // 2. Subir nuevos archivos
            ArrayList<SDbSupplierFile> lFileNamesToUpload = new ArrayList<>();
            boolean hasError = false;

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
                    sError = "Error al enviar los archivos, intente de nuevo o contacte a soporte técnico. " + e.getMessage();
                    break;
                }
            }

            // 3. Manejo de errores en la subida
            if (hasError) {
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
                    }
                }
                return sError; // Retornar mensaje de error
            }

            return ""; // Operación exitosa
        }

        return "No hay archivos para enviar"; // Mensaje si no hay archivos
    }

}
