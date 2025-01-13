package erp.mod.trn.api.db;

import com.swaplicado.cloudstoragemanager.CloudStorageManager;
import com.swaplicado.data.StorageManagerException;
import erp.mod.SModConsts;
import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.db.SMySqlClass;
import erp.mod.trn.api.data.SWebMatReqNote;
import erp.mod.trn.api.data.SWebMaterialRequest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para manejar operaciones de base de datos relacionadas con requisiciones de materiales
 * vinculadas a entradas específicas de documentos (DPS).
 */
public class STrnDBMaterialRequest {
    
    SMySqlClass oDbObj;
    String msMainDatabase;
    private final ArrayList<SWebMaterialRequest> lMaterialRequests;

    public STrnDBMaterialRequest(SMySqlClass oDbObj, String mainDatabase) {
        this.oDbObj = oDbObj;
        this.msMainDatabase = mainDatabase;
        this.lMaterialRequests = new ArrayList<>();
    }
    
    public STrnDBMaterialRequest() {
        this.lMaterialRequests = new ArrayList<>();
        try {
            this.oDbObj = new SMySqlClass();
            this.msMainDatabase = this.oDbObj.getMainDatabaseName();
        } catch (SConfigException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Connection getConnection() {
        try {
            return this.oDbObj.connect("", "", this.msMainDatabase, "", "");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private static boolean containsMatReqWithId(ArrayList<SWebMaterialRequest> list, int id) {
        return list.stream().anyMatch(obj -> obj.getIdMaterialRequest() == id);
    }
    
    private static SWebMaterialRequest getMatReqById(ArrayList<SWebMaterialRequest> list, int id) {
        return list.stream()
                   .filter(obj -> obj.getIdMaterialRequest() == id) // Filtra por el id especificado.
                   .findFirst() // Obtiene el primer objeto que cumple la condición.
                   .orElse(null); // Retorna null si no encuentra ningún objeto.
    }

    /**
     * Obtiene la requisición de materiales asociada a una entrada específica de un documento.
     *
     * @param idYear Año del documento.
     * @param idDoc ID del documento.
     * @param idEty ID de la entrada del documento.
     * @return Una instancia de {@link SWebMaterialRequest} que contiene los datos de la requisición de material.
     */
    public SWebMaterialRequest getMaterialRequestOfDpsEty(final int idYear, final int idDoc, final int idEty) {
        try {
            // Conexión a la base de datos principal.
            Connection conn = getConnection();

            if (conn == null) {
                return null;
            }

            // Consulta para obtener la requisición de materiales.
            String query = "SELECT " +
                    "    u.usr AS mr_user, prty.name AS prty_name, mr.* " +
                    "FROM " +
                    "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " AS dpsmr " +
                    "    INNER JOIN " +
                    "    " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS mr ON dpsmr.fid_mat_req = mr.id_mat_req " +
                    "    INNER JOIN " +
                    "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS u ON mr.fk_usr_req = u.id_usr " +
                    "    INNER JOIN " +
                    "    " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS prty ON mr.fk_mat_req_pty = prty.id_mat_req_pty " +
                    "WHERE " +
                    "    dpsmr.fid_dps_year = " + idYear + " " +
                    "    AND dpsmr.fid_dps_doc = " + idDoc + " " +
                    "    AND fid_dps_ety = " + idEty + " " +
                    "GROUP BY mr.id_mat_req " +
                    "ORDER BY id_dps_mat_req DESC " +
                    "LIMIT 1;";

            Statement st = conn.createStatement();
            System.out.println(query);
            ResultSet res = st.executeQuery(query);

            SWebMaterialRequest oMatReq = new SWebMaterialRequest();

            if (res.next()) {
                // Asignar valores a la requisición de materiales.
                oMatReq.setIdMaterialRequest(res.getInt("mr.id_mat_req"));
                if (containsMatReqWithId(lMaterialRequests, oMatReq.getIdMaterialRequest())) {
                    SWebMaterialRequest oMatReqExist = getMatReqById(lMaterialRequests, oMatReq.getIdMaterialRequest());
                    if (oMatReqExist != null) {
                        return oMatReqExist;
                    }
                }
                oMatReq.setMrFolio(res.getString("mr.num"));
                oMatReq.setMrDate(res.getString("mr.dt"));
                oMatReq.setMrRequiredDate(res.getString("mr.dt_req_n"));
                oMatReq.setMrUser(res.getString("mr_user"));
                oMatReq.setMrPriority(res.getString("prty_name"));
                oMatReq.setMrType(res.getString("mr.tp_req"));
                try {
                    String fileName = this.msMainDatabase + "-" + "RM" + "-" + oMatReq.getIdMaterialRequest() + ".pdf";
                    if (CloudStorageManager.storagedFileExists(fileName)) {
                        oMatReq.setMrStorageCloudUrl(CloudStorageManager.generatePresignedUrl(fileName));
                        System.out.println(fileName);
                    }
                }
                catch (StorageManagerException ex) {
                    Logger.getLogger(STrnDBMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
                    oMatReq.setMrStorageCloudUrl("#");
                }
            }

            // Consulta para obtener las notas de la requisición de materiales.
            String matReqNotesQuery = "SELECT " +
                    "    id_nts, nts " +
                    "FROM " +
                    "    " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_NTS) + " " +
                    "WHERE " +
                    "    id_mat_req = " + oMatReq.getIdMaterialRequest() + ";";

            Statement stNts = conn.createStatement();
            ResultSet resNts = stNts.executeQuery(matReqNotesQuery);

            ArrayList<SWebMatReqNote> lNotes = new ArrayList<>();
            while (resNts.next()) {
                SWebMatReqNote oNote = new SWebMatReqNote(
                        oMatReq.getIdMaterialRequest(),
                        resNts.getInt("id_nts"),
                        resNts.getString("nts")
                );
                lNotes.add(oNote);
            }

            // Agregar las notas a la requisición.
            oMatReq.getlNotes().clear();
            oMatReq.getlNotes().addAll(lNotes);
            
            lMaterialRequests.add(oMatReq);

            return oMatReq;
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnDBMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Retornar una instancia vacía si ocurre un error.
        return new SWebMaterialRequest();
    }
}
