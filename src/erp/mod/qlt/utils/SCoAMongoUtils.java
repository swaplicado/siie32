/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.qlt.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SCoAMongoUtils {
    
    /**
     * Obtiene una colección de MongoDB a partir de la sesión y el nombre de la colección.
     * @param session La sesión del cliente.
     * @param collectionName El nombre de la colección a obtener.
     * @return MongoCollection<Document> La colección de MongoDB.
     * @throws Exception
     */
    public static MongoCollection<Document> getCollection(SGuiSession session, String collectionName) throws Exception {
        String uri = SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_DOC_MONGO_URI);
        String dbName = ((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDatabase();
        
        MongoClient client = MongoClients.create(uri);
        MongoDatabase database = client.getDatabase(dbName);
        return database.getCollection(collectionName);
    }
    
    /**
     * Inserta un documento en una colección de MongoDB.
     * 
     * @param session La sesión del cliente.
     * @param collectionName El nombre de la colección donde se insertará el documento.
     * @param document El documento a insertar.
     * @return String El ID del documento insertado.
     * @throws Exception
     */
    public static String insertDocument(SGuiSession session, String collectionName, Document document) throws Exception {
        try (MongoClient client = MongoClients.create(
                SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_DOC_MONGO_URI))) {
            
            MongoDatabase database = client.getDatabase(
                    ((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDatabase());
            
            MongoCollection<Document> collection = database.getCollection(collectionName);
        
            if (document.get("_id") == null) {
                document.remove("_id");
            }

            collection.insertOne(document);
            
            return document.getObjectId("_id").toHexString();
        }
    }

    /**
     * Actualiza un documento en una colección de MongoDB.
     * 
     * @param session La sesión del cliente.
     * @param collectionName El nombre de la colección donde se actualizará el documento.
     * @param id El ID del documento a actualizar.
     * @param updatedDocument El documento con los datos actualizados.
     * @throws Exception
     */
    public static void updateDocument(SGuiSession session, String collectionName, String id, Document updatedDocument) throws Exception {
        try (MongoClient client = MongoClients.create(
                SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_DOC_MONGO_URI))) {
            
            MongoDatabase database = client.getDatabase(
                    ((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDatabase());
            
            MongoCollection<Document> collection = database.getCollection(collectionName);
            Bson filter = Filters.eq("_id", new ObjectId(id));
            
            collection.replaceOne(filter, updatedDocument);
        }
    }
    
    /**
     * Busca un documento por su ID en una colección de MongoDB.
     * 
     * @param session La sesión del cliente.
     * @param collectionName El nombre de la colección donde se buscará el documento.
     * @param id El ID del documento a buscar.
     * @return Document El documento encontrado, o null si no se encuentra.
     * @throws Exception
     */
    public static Document findDocumentById(SGuiSession session, String collectionName, String id) throws Exception {
        try (MongoClient client = MongoClients.create(
                SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_DOC_MONGO_URI))) {
            
            MongoDatabase database = client.getDatabase(
                    ((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDatabase());
            
            MongoCollection<Document> collection = database.getCollection(collectionName);
            Bson filter = Filters.eq("_id", new ObjectId(id));
            
            return collection.find(filter).first();
        }
    }
    
}
