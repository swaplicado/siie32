package erp.mod.cfg.swap.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swaplicado.cloudstoragemanager.CloudStorageManager;
import com.swaplicado.data.CloudStorageFile;
import com.swaplicado.data.StorageManagerException;
import erp.mod.cfg.db.SDbComSyncLogEntry;
import erp.mod.cfg.db.SDbSyncLogEntry;
import erp.mod.cfg.swap.SSyncType;
import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.gui.SGuiSession;

/**
 * Utilidad para manejar la sincronización de archivos con Google Cloud Storage.
 * 
 * Esta clase se encarga de:
 * - Subir archivos a Google Cloud Storage.
 * - Validar si un archivo ya existe o si necesita ser resubido (por cambios o falta de sincronización).
 * - Generar y almacenar logs de sincronización en la base de datos.
 * 
 * @author 
 *   Edwin Carmona
 */
public class SDpsGoogleCloudUtils {

    /**
    * Sube un conjunto de archivos a Google Cloud Storage y registra los resultados en logs de sincronización.
    * 
    * @param session Sesión actual de la aplicación (para registrar logs en BD).
    * @param mFiles  Mapa de archivos a subir: llave = {@link SFileData}, valor = archivo físico.
    */
   public static void uploadFiles(final SGuiSession session, HashMap<SFileData, File> mFiles) {
       try {
           ObjectMapper objectMapper = new ObjectMapper(); // Convierte objetos en JSON
           ArrayList<SDbSyncLogEntry> lLogs = new ArrayList<>(); // Lista de logs a guardar
           boolean hasErrors = false; // Bandera para saber si hubo algún error en el lote

           // Recorre todos los archivos que se desean subir
           for (Map.Entry<SFileData, File> entrySet : mFiles.entrySet()) {
               SFileData fileData = entrySet.getKey();   // Metadatos del archivo
               File file = entrySet.getValue();          // Archivo físico en el sistema
               SDbComSyncLogEntry logEntry = new SDbComSyncLogEntry(); // Registro de log por archivo

               // Construcción del identificador de referencia para el log
               logEntry.setReferenceId(fileData.getIdYear() + "_" + fileData.getIdDoc());
               logEntry.setAuxDatabase(fileData.getDbName());

               CloudStorageFile gcsFile = null; 
               boolean shouldLog = false;

               try {
                   // 1. Verificar si el archivo existe en GCS
                   if (!CloudStorageManager.storagedFileExists(fileData.getFileName())) {
                       // Subir si no existe en GCS
                       gcsFile = SDpsGoogleCloudUtils.uploadFile(file.getAbsolutePath(), fileData.getFileName());
                       logEntry.setResponseCode((gcsFile == null ? HttpURLConnection.HTTP_INTERNAL_ERROR : HttpURLConnection.HTTP_OK) + "");
                       shouldLog = true;
                       if (gcsFile == null) {
                           hasErrors = true;
                       }
                   }
                   else {
                       // 2. Si ya existe, verificar sincronización en la BD
                       Date lastSync = SExportDpsFileUtils.isResourceSinchronized(
                               session.getStatement().getConnection().createStatement(), 
                               SSyncType.PUR_ORDER_FILE, 
                               fileData.getIdYear() + "_" + fileData.getIdDoc()
                       );

                       if (lastSync == null || fileData.getLastUpdate().after(lastSync)) {
                           gcsFile = SDpsGoogleCloudUtils.uploadFile(file.getAbsolutePath(), fileData.getFileName());
                           logEntry.setResponseCode((gcsFile == null ? HttpURLConnection.HTTP_INTERNAL_ERROR : HttpURLConnection.HTTP_OK) + "");
                           shouldLog = true;
                           if (gcsFile == null) {
                               hasErrors = true;
                           }
                       }
                   }

                   // Si se subió correctamente, actualizar los metadatos con info de GCS
                   if (gcsFile != null) {
                       fileData.setProjectId(gcsFile.getProjectId());
                       fileData.setBucketName(gcsFile.getBucketName());
                   }
               }
               catch (StorageManagerException ex) {
                   // Error específico en uploadFile → se marca el log con error
                   Logger.getLogger(SDpsGoogleCloudUtils.class.getName()).log(Level.SEVERE, 
                           "Error al subir archivo a GCS: " + fileData.getFileName(), ex);
                   logEntry.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR + "");
                   logEntry.setResponseBody("Error en GCS: " + ex.getMessage());
                   shouldLog = true;
                   hasErrors = true;
               }
               catch (Exception ex) {
                   // Cualquier otro error inesperado en este archivo
                   Logger.getLogger(SDpsGoogleCloudUtils.class.getName()).log(Level.SEVERE, 
                           "Error inesperado al procesar archivo: " + fileData.getFileName(), ex);
                   logEntry.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR + "");
                   logEntry.setResponseBody("Error inesperado: " + ex.getMessage());
                   shouldLog = true;
                   hasErrors = true;
               }

               // Registrar en log si hubo acción de subida o error
               if (shouldLog) {
                   try {
                       if (logEntry.getResponseBody() == null || logEntry.getResponseBody().isEmpty()) {
                           logEntry.setResponseBody(objectMapper.writeValueAsString(fileData));
                       }
                   }
                   catch (Exception ex) {
                       // Evita que un error serializando JSON bloquee el log
                       logEntry.setResponseBody("Error serializando datos del archivo.");
                   }
                   lLogs.add(logEntry);
               }
               
               file.delete();
           }

           // Código de respuesta general: 206 si hubo errores, 200 si todo OK
           int overallStatus = hasErrors ? HttpURLConnection.HTTP_PARTIAL : HttpURLConnection.HTTP_OK;

           // Guardar todos los logs de la operación de sincronización
           SExportUtils.logSync(
                   session, 
                   SSyncType.PUR_ORDER_FILE, 
                   "", 
                   new Date(), 
                   overallStatus, 
                   "", 
                   new Date(), 
                   lLogs
           );
       }
       catch (Exception ex) {
           // Este catch global solo atrapa errores "fuera del ciclo"
           Logger.getLogger(SDpsGoogleCloudUtils.class.getName()).log(Level.SEVERE, "Error inesperado en el proceso de carga", ex);
       }
   }


    /**
     * Sube un único archivo a Google Cloud Storage.
     * 
     * @param absolutePath Ruta absoluta del archivo en el sistema local.
     * @param fileName     Nombre con el que se almacenará en GCS.
     * @return Objeto {@link CloudStorageFile} con la información del archivo en GCS.
     * @throws StorageManagerException si ocurre un error durante la subida.
     */
    private static CloudStorageFile uploadFile(String absolutePath, String fileName) throws StorageManagerException {
        return CloudStorageManager.uploadObject(absolutePath, fileName);
    }
}
