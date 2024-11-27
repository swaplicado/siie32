/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.cfg.db.SDbDocument;
import erp.mod.hrs.db.SRowPreceptSubsection;
import erp.mod.hrs.form.SDialogDocImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SDocUtils {
    
    public static final String META_LOC = "location";
    public static final String META_TYPE = "type";
    public static final String META_USER = "user";
    
    public static final String BUCKET_DOC_BREACH = "docBreach";
    public static final String BUCKET_DOC_ADM_REC = "docAdminRecord";
    public static final String BUCKET_DOC_DPS_SUPPLIER = "docDpsSupplier";
    
    public static final String FILE_TYPE_IMG = "IMG";
    public static final String FILE_TYPE_PDF = "PDF";
    
    public static final String VAL_DOC_ADM_REC_ORG = "org";
    public static final String VAL_DOC_ADM_REC_PRON = "pronoun";
    public static final String VAL_DOC_ADM_REC_EMP = "employee";
    public static final String VAL_DOC_ADM_REC_BOSS = "boss";
    public static final String VAL_DOC_ADM_REC_LIAB = "liable";
    public static final String VAL_DOC_ADM_REC_REP = "represent";
    public static final String VAL_DOC_ADM_REC_WIT = "witness";
    
    /**
     * Compose descriptions of required precept subsections.
     * @param session GUI session.
     * @param preceptSubsectionKeys Required precept subsections.
     * @param separator Separator.
     * @return Descriptions of required precept subsections.
     */
    public static String composePreceptSubsections(final SGuiSession session, final ArrayList<int[]> preceptSubsectionKeys, String separator) {
        String preceptSubsections = "";
        
        for (int[] key : preceptSubsectionKeys) {
            preceptSubsections += (preceptSubsections.isEmpty() ? "" : separator) + (String) session.readField(SModConsts.HRS_PREC_SUBSEC, key, SDbRegistry.FIELD_NAME);
        }
        
        return preceptSubsections;
    }
    
    /**
     * Get rows for grid of precept subsections.
     * @param session GUI session.
     * @return Rows for grid of precept subsections.
     * @throws Exception 
     */
    public static ArrayList<SRowPreceptSubsection> getPreceptSubsectionRows(final SGuiSession session) throws Exception {
        ArrayList<SRowPreceptSubsection> rows = new ArrayList<>();
        
        String sql = "SELECT pss.id_prec, pss.id_sec, pss.id_subsec, "
                + "p.code, p. name, ps.code, ps.name, ps.sort, pss.code, pss.name, pss.sort "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PREC) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PREC_SEC) + " AS ps ON ps.id_prec = p.id_prec "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PREC_SUBSEC) + " AS pss ON pss.id_prec = ps.id_prec AND pss.id_sec = ps.id_sec "
                + "WHERE NOT p.b_del AND NOT ps.b_del AND NOT pss.b_del "
                + "ORDER BY p.name, ps.sort, pss.sort, pss.id_prec, pss.id_sec, pss.id_subsec;";
        
        int positionGlobal = 0;
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                rows.add(new SRowPreceptSubsection(
                        new int[] { resultSet.getInt("pss.id_prec"), resultSet.getInt("pss.id_sec"), resultSet.getInt("pss.id_subsec") }, 
                        resultSet.getString("p.code"), 
                        resultSet.getString("ps.code"), 
                        resultSet.getString("pss.code"), 
                        resultSet.getString("pss.name"), 
                        ++positionGlobal, 
                        0));
            }
        }
        
        return rows;
    }
    
    /**
     * Get the most recent version of required document type.
     * @param session GUI session.
     * @param docType Document type. Constants defined in SModSysConsts.CFGU_DOC_...
     * @return ID of the most recent version of required document type.
     * @throws Exception 
     */
    public static int getDocCurrentVersionId(final SGuiSession session, final int docType) throws Exception {
        int id = 0;
        
        String sql = "SELECT id_doc "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_DOC) + " "
                + "WHERE fk_tp_doc = " + docType + " AND NOT b_obs AND NOT b_del "
                + "ORDER BY dt_doc DESC "
                + "LIMIT 1;";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        }
        
        return id;
    }
    
    /**
     * Get map of data values of current version of required document type.
     * Format of data values: key1:val1;key2:val2;keyx:valx
     * @param session GUI session.
     * @param docType Document type. Constants defined in SModSysConsts.CFGU_DOC_...
     * @return Map of data values.
     * @throws Exception 
     */
    public static HashMap<String, String> getDocDataValuesMap(final SGuiSession session, final int docType) throws Exception {
        HashMap<String, String> valuesMap = new HashMap<>();
        int docId = getDocCurrentVersionId(session, docType);
        String docData = (String) session.readField(SModConsts.CFGU_DOC, new int[] { docId }, SDbDocument.FIELD_DATA);
        String[] values = SLibUtils.textExplode(docData, ";");
        
        for (String value : values) {
            String[] data = SLibUtils.textExplode(value, ":");
            valuesMap.put(data[0], data[1]);
        }
        
        return valuesMap;
    }
    
    /**
     * Upload file to MongoDB vault.
     * @param session GUI session.
     * @param bucketName Name of Mongo GridFS bucket. Options supported by constants: BUCKET_DOC_...
     * @param fileType File type. Options supported by constants: FILE_TYPE_...
     * @param file File to upload.
     * @return Object ID of file uploaded.
     * @throws IOException, Exception
     */
    public static String uploadFile(final SGuiSession session, final String bucketName, final String fileType, final File file) throws IOException, Exception {
        if (!file.isFile()) {
            throw new Exception("El parámetro File '" + file.getPath() + "' no es un archivo.");
        }
        
        String uri = SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_DOC_MONGO_URI);
        String db = ((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDatabase();
        ObjectId objectId;
        
        try (MongoClient client = MongoClients.create(uri)) {
            MongoDatabase database = client.getDatabase(db);
            GridFSBucket bucket = GridFSBuckets.create(database, bucketName);
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put(META_LOC, file.getParent());
            metadata.put(META_TYPE, fileType);
            metadata.put(META_USER, session.getUser().getName());
            GridFSUploadOptions uploadOptions = new GridFSUploadOptions()
                    .metadata(new Document(metadata));
            
            System.out.println("Cargando archivo '" + file.getPath() + "'...");

            try (FileInputStream inputStream = new FileInputStream(file)) {
                objectId = bucket.uploadFromStream(file.getName(), inputStream, uploadOptions);
                
                System.out.println("El ObjectId del archivo cargado es '" + objectId.toHexString() + "' (" + objectId.toString() + ")!");
            }
        }
        
        return objectId.toHexString();
    }
    
    /**
     * Download file from MongoDB vault.
     * @param session GUI session.
     * @param bucketName Name of Mongo GridFS bucket.
     * @param filevaultId ObjectId of file to download.
     * @param location Location to save the downloaded file.
     * @return Filename of file downloaded, if found.
     * @throws IOException, Exception
     */
    public static String downloadFile(final SGuiSession session, final String bucketName, final String filevaultId, final File location) throws IOException, Exception {
        if (!location.isDirectory()) {
            throw new Exception("El parámetro File '" + location.getPath() + "' no es un directorio.");
        }
        
        String filenameFound = "";
        String uri = SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_DOC_MONGO_URI);
        String db = ((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDatabase();
        
        try (MongoClient client = MongoClients.create(uri)) {
            MongoDatabase database = client.getDatabase(db);
            GridFSBucket bucket = GridFSBuckets.create(database, bucketName);
            
            ObjectId objectId = new ObjectId(filevaultId);
            Bson query = Filters.eq("_id", objectId);
            Bson sort = Sorts.descending("uploadDate");
            
            for (GridFSFile file : bucket.find(query).sort(sort).limit(1)) {
                filenameFound = file.getFilename();
                break;
            }
            
            if (filenameFound.isEmpty()) {
                throw new Exception("No se encontró ningún archivo con el ObjectId '" + filevaultId + "'!");
            }
            else {
                System.out.println("Descargando archivo '" + filenameFound + "', ObjectId '" + filevaultId + "'...");

                try (FileOutputStream fileOutputStream = new FileOutputStream(location.getPath() + File.separator + filenameFound)) {
                    bucket.downloadToStream(objectId, fileOutputStream);
                    fileOutputStream.flush();
                    
                    System.out.println("Archivo '" + filenameFound + "', ObjectId '" + filevaultId + "', descargado!");
                }
            }
        }
        
        return filenameFound;
    }
    
    /**
     * Download file from MongoDB vault.
     * @param session GUI session.
     * @param bucketName Name of Mongo GridFS bucket.
     * @param filevaultId ObjectId of file to download.
     * @param location Location to save the downloaded file.
     * @return File object file downloaded, if was found.
     * @throws IOException, Exception
     */
    public static File downloadAndGetFile(final SGuiSession session, final String bucketName, final String filevaultId, final File location) throws IOException, Exception {
        String filePath = SDocUtils.downloadFile(session, bucketName, filevaultId, location);
        File downloadedFile = new File(filePath);
        return downloadedFile;
    }
    
    /**
     * Download file from MongoDB vault.
     * @param session GUI session.
     * @param bucketName Name of Mongo GridFS bucket.
     * @param filevaultId ObjectId of file to download.
     * @param location Location to save the downloaded file.
     * @return Bytes of file downloaded, if was found.
     * @throws IOException, Exception
     */
    public static byte[] getFileBytes(final SGuiSession session, final String bucketName, final String filevaultId, final File location) throws IOException, Exception {
        String filenameFound = "";
        String uri = SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_DOC_MONGO_URI);
        String db = ((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDatabase();

        byte[] fileData;

        // Conexión a MongoDB y descarga del archivo en memoria
        try (MongoClient client = MongoClients.create(uri)) {
            MongoDatabase database = client.getDatabase(db);
            GridFSBucket bucket = GridFSBuckets.create(database, bucketName);

            ObjectId objectId = new ObjectId(filevaultId);
            Bson query = Filters.eq("_id", objectId);
            Bson sort = Sorts.descending("uploadDate");

            GridFSFile file = bucket.find(query).sort(sort).limit(1).first();
            if (file == null) {
                throw new Exception("No se encontró ningún archivo con el ObjectId '" + filevaultId + "'!");
            }

            filenameFound = file.getFilename();
            System.out.println("Descargando archivo '" + filenameFound + "', ObjectId '" + filevaultId + "'...");

            // Descargar el archivo en un flujo de salida en memoria
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                bucket.downloadToStream(objectId, outputStream);
                fileData = outputStream.toByteArray();
            }
        }

        System.out.println("Archivo descargado en memoria: '" + filenameFound + "'.");
        
        return fileData;
    }
    
    /**
     * Delete file from MongoDB vault.
     * @param session GUI session.
     * @param bucketName Name of Mongo GridFS bucket.
     * @param filevaultId ObjectId of file to delete.
     * @return Filename of file deleted, if found.
     * @throws Exception
     */
    public static String deleteFile(final SGuiSession session, final String bucketName, final String filevaultId) throws Exception {
        String filenameFound = "";
        String uri = SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_DOC_MONGO_URI);
        String db = ((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDatabase();
        
        try (MongoClient client = MongoClients.create(uri)) {
            MongoDatabase database = client.getDatabase(db);
            GridFSBucket bucket = GridFSBuckets.create(database, bucketName);
            
            ObjectId objectId = new ObjectId(filevaultId);
            Bson query = Filters.eq("_id", objectId);
            Bson sort = Sorts.descending("uploadDate");
            
            for (GridFSFile file : bucket.find(query).sort(sort).limit(1)) {
                filenameFound = file.getFilename();
                break;
            }
            
            if (filenameFound.isEmpty()) {
                throw new Exception("No se encontró ningún archivo con el ObjectId '" + filevaultId + "'!");
            }
            else {
                System.out.println("Eliminando archivo '" + filenameFound + "', ObjectId '" + filevaultId + "'...");

                bucket.delete(objectId);
                
                System.out.println("Archivo '" + filenameFound + "', ObjectId '" + filevaultId + "', eliminado!");
            }
        }
        
        return filenameFound;
    }
    
    /**
     * Get GridFSfile from MongoDB vault.
     * @param session GUI session.
     * @param bucketName Name of Mongo GridFS bucket.
     * @param filevaultId ObjectId of file to download.
     * @return GridFSfile, if found.
     * @throws Exception
     */
    public static GridFSFile getGridFSfile(final SGuiSession session, final String bucketName, final String filevaultId) throws Exception {
        GridFSFile gridFSFile = null;
        String uri = SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_DOC_MONGO_URI);
        String db = ((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDatabase();
        
        try (MongoClient client = MongoClients.create(uri)) {
            MongoDatabase database = client.getDatabase(db);
            GridFSBucket bucket = GridFSBuckets.create(database, bucketName);
            
            ObjectId objectId = new ObjectId(filevaultId);
            Bson query = Filters.eq("_id", objectId);
            Bson sort = Sorts.descending("uploadDate");
            
            for (GridFSFile file : bucket.find(query).sort(sort).limit(1)) {
                gridFSFile = file;
                break;
            }
        }
        
        return gridFSFile;
    }
    
    /**
     * Get file bytes from MongoDB vault.
     * @param session GUI session.
     * @param bucketName Name of Mongo GridFS bucket.
     * @param fileType File type. Options supported by constants: FILE_TYPE_...
     * @param filevaultId ObjectId of file to get.
     * @return Array of bytes of file got, if found.
     * @throws Exception
     */
    private static byte[] getFileBytes(final SGuiSession session, final String bucketName, final String fileType, final String filevaultId) throws Exception {
        byte[] bytesWriteTo = null;
        String filenameFound = "";
        String fileTypeFound = "";
        String uri = SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_DOC_MONGO_URI);
        String db = ((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDatabase();
        
        try (MongoClient client = MongoClients.create(uri)) {
            MongoDatabase database = client.getDatabase(db);
            GridFSBucket bucket = GridFSBuckets.create(database, bucketName);
            
            ObjectId objectId = new ObjectId(filevaultId);
            Bson query = Filters.eq("_id", objectId);
            Bson sort = Sorts.descending("uploadDate");
            
            for (GridFSFile file : bucket.find(query).sort(sort).limit(1)) {
                filenameFound = file.getFilename();
                fileTypeFound = file.getMetadata().getString(META_TYPE);
                break;
            }
            
            if (filenameFound.isEmpty()) {
                throw new Exception("No se encontró ningún archivo con el ObjectId '" + filevaultId + "'!");
            }
            else if (!fileTypeFound.equals(fileType)) {
                throw new Exception("El tipo del archivo  '" + filenameFound + "', ObjectId '" + filevaultId + "', no es '" + fileType + "'!");
            }
            else {
                System.out.println("Recuperando archivo '" + filenameFound + "', ObjectId '" + filevaultId + "'...");
                
                try (GridFSDownloadStream downloadStream = bucket.openDownloadStream(objectId)) {
                    int fileLength = (int) downloadStream.getGridFSFile().getLength();
                    bytesWriteTo = new byte[fileLength];
                    downloadStream.read(bytesWriteTo);
                    
                    System.out.println("Archivo '" + filenameFound + "', ObjectId '" + filevaultId + "', recuperado!");
                }
            }
        }
        
        return bytesWriteTo;
    }
    
    /**
     * Get file name from MongoDB vault.
     * @param session GUI session.
     * @param bucketName Name of Mongo GridFS bucket.
     * @param filevaultId ObjectId of file to download.
     * @return GridFSfile, if found.
     * @throws java.io.IOException
     * @throws Exception
     */
    public static String getFileName(final SGuiSession session, final String bucketName, final String filevaultId) throws IOException, Exception {
        String filenameFound = "";
        String uri = SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_DOC_MONGO_URI);
        String db = ((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDatabase();
        
        try (MongoClient client = MongoClients.create(uri)) {
            MongoDatabase database = client.getDatabase(db);
            GridFSBucket bucket = GridFSBuckets.create(database, bucketName);
            
            ObjectId objectId = new ObjectId(filevaultId);
            Bson query = Filters.eq("_id", objectId);
            Bson sort = Sorts.descending("uploadDate");
            
            for (GridFSFile file : bucket.find(query).sort(sort).limit(1)) {
                filenameFound = file.getFilename();
                break;
            }
            
            if (filenameFound.isEmpty()) {
                throw new Exception("No se encontró ningún archivo con el ObjectId '" + filevaultId + "'!");
            }
        }
        
        return filenameFound;
    }
    
    /**
     * Get file image icon from MongoDB vault.
     * @param session GUI session.
     * @param bucketName Name of Mongo GridFS bucket.
     * @param filevaultId ObjectId of file to get.
     * @return Image icon of file got, if found.
     * @throws Exception
     */
    public static ImageIcon getFileImageIcon(final SGuiSession session, final String bucketName, final String filevaultId) throws Exception {
        return new ImageIcon(getFileBytes(session, bucketName, FILE_TYPE_IMG, filevaultId));
    }
    
    /**
     * View file retrieving it from MongoDB vault.
     * @param client GUI client.
     * @param bucketName Name of Mongo GridFS bucket.
     * @param filevaultId ObjectId of file to get.
     * @throws IOException, Exception
     */
    public static void viewFile(final SGuiClient client, final String bucketName, final String filevaultId) throws IOException, Exception {
        if (filevaultId.isEmpty()) {
            client.showMsgBoxWarning("Este documento no tiene archivo.");
        }
        else {
            GridFSFile gridFSFile = SDocUtils.getGridFSfile(client.getSession(), bucketName, filevaultId);

            if (gridFSFile == null) {
                throw new Exception("No se encontró ningún archivo con el ObjectId '" + filevaultId + "'!");
            }
            else {
                String filename = gridFSFile.getFilename();
                String fileType = gridFSFile.getMetadata().getString(META_TYPE);

                if (fileType == null) {
                    throw new Exception("El tipo del archivo  '" + filename + "', ObjectId '" + filevaultId + "', es desconocido!");
                }
                else {
                    switch (fileType) {
                        case FILE_TYPE_IMG:
                            ImageIcon imageIcon = getFileImageIcon(client.getSession(), bucketName, filevaultId);
                            SDialogDocImage dialogDocImage = new SDialogDocImage(client.getFrame());
                            dialogDocImage.setDocImage(imageIcon);
                            dialogDocImage.setVisible(true);
                            break;

                        case FILE_TYPE_PDF:
                            String tempLocation = System.getenv("TEMP");
                            String filePdf = tempLocation + "\\" + filename;
                            File file = new File(filePdf);
                            if (file.exists()) {
                                file.delete();
                            }
                            String fileDownloaded = downloadFile(client.getSession(), bucketName, filevaultId, new File(tempLocation));
                            System.out.println("Abriendo PDF '" + fileDownloaded + "' desde '" + filePdf + "'...");
                            SLibUtils.launchFile(filePdf);
                            break;

                        default:
                    }
                }
            }
        }
    }
    
    /**
     * Check if file is PDF.
     * @param file File to check.
     * @return <code>true</code> if file is PDF, otherwise <code>false</code>.
     */
    public static boolean isPdf(final File file) {
        return file.getName().toLowerCase().endsWith(FILE_TYPE_PDF.toLowerCase());
    }
    
    public static String getExtensionFile (final File file) {
        String name = file.getName();
        int index = name.lastIndexOf(".");
        
        if (index > 0 && index < name.length() - 1) {
            return name.substring(index + 1);
        }
        else {
            return "";
        }
    }
}
