package erp.mod.trn.api.db;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swaplicado.cloudstoragemanager.CloudStorageManager;
import com.swaplicado.data.StorageManagerException;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.cfg.utils.SAuthJsonUtils;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.db.SMySqlClass;
import erp.mod.trn.api.data.SWebCostCenter;
import erp.mod.trn.api.data.SWebMatReqEtyNote;
import erp.mod.trn.api.data.SWebMatReqNote;
import erp.mod.trn.api.data.SWebMaterialRequest;
import erp.mod.trn.api.data.SWebMaterialRequestEty;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para manejar operaciones de base de datos relacionadas con requisiciones de materiales
 * vinculadas a entradas específicas de documentos (DPS).
 */
public class STrnDBMaterialRequest {

    private static final String BASE_QUERY = 
        "SELECT " +
        "    mr.id_mat_req, mr.cl_req AS _mr_class, mr.tp_req AS _mr_type, mr.num AS _mr_folio, " +
        "    mr.dt AS _mr_dt, mr.dt_req_n, mr.tot_r AS _mr_total, pe.name AS _mr_prov_ent, " +
        "    pty.name AS _mr_priority, st.name AS _mr_status, nat.dps_nat AS _mr_nat, " +
        "    usr.usr AS _mr_usr, itm.item AS _mr_item_ref, "
            +"CASE "
            + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
            + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', mr.id_mat_req, "
            + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_AUTHORIZED + " THEN 'AUTORIZADO' "
            + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
            + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', mr.id_mat_req, "
            + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_REJECTED + " THEN 'RECHAZADO' "
            + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
            + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', mr.id_mat_req, "
            + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_PENDING + " THEN 'PENDIENTE' "
            + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
            + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', mr.id_mat_req, "
            + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_IN_PROCESS + " THEN 'EN PROCESO' "
            + "ELSE '(NO APLICA)' "
            + "END AS auth_status, " +
        "   cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", " +
        "   '" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', mr.id_mat_req, " +
        "   NULL, NULL, NULL, NULL) AS auth_status_id, " +
        "    COALESCE(( " +
        "        SELECT GROUP_CONCAT(u.usr SEPARATOR ', ') " +
        "        FROM cfgu_authorn_step AS steps1 " +
        "        INNER JOIN erp.usru_usr AS u ON steps1.fk_usr_step = u.id_usr " +
        "        WHERE NOT steps1.b_del " +
        "            AND steps1.res_tab_name_n = '" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "' " +
        "            AND steps1.res_pk_n1_n = mr.id_mat_req " +
        "            AND NOT steps1.b_authorn " +
        "            AND NOT steps1.b_reject " +
        "            AND steps1.lev = ( " +
        "                SELECT step2.lev " +
        "                FROM cfgu_authorn_step AS step2 " +
        "                WHERE NOT step2.b_del " +
        "                    AND step2.res_tab_name_n = '" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "' " +
        "                    AND step2.res_pk_n1_n = mr.id_mat_req " +
        "                    AND NOT step2.b_authorn " +
        "                    AND NOT step2.b_reject " +
        "                ORDER BY step2.lev ASC " +
        "                LIMIT 1 " +
        "            ) " +
        "    ), 'NA') AS user_in_turn " +
        "FROM " +
        SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS mr " +
        "INNER JOIN trn_mat_prov_ent AS pe ON pe.id_mat_prov_ent = mr.fk_mat_prov_ent " +
        "INNER JOIN erp.trnu_mat_req_pty AS pty ON mr.fk_mat_req_pty = pty.id_mat_req_pty " +
        "INNER JOIN erp.trns_st_mat_req AS st ON st.id_st_mat_req = mr.fk_st_mat_req " +
        "INNER JOIN erp.trnu_dps_nat AS nat ON nat.id_dps_nat = mr.fk_dps_nat " +
        "INNER JOIN erp.usru_usr AS usr ON usr.id_usr = mr.fk_usr_req " +
        "LEFT JOIN erp.itmu_item AS itm ON itm.id_item = mr.fk_item_ref_n ";

    private static final String WHERE_NOT_DELETED = "WHERE mr.b_del = 0 ";

    private static final String ORDER_BY_ID = "ORDER BY mr.id_mat_req DESC ";

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
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(STrnDBMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Connection obtainConection() {
        return this.getConnection();
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

    private ArrayList<SWebMatReqNote> loadMaterialRequestNotes(final int materialRequestId) throws SQLException {
        String notesQuery = "SELECT id_nts, nts FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_NTS) + " "
                            + "WHERE id_mat_req = " + materialRequestId + ";";
    
        try {
            // Conexión a la base de datos principal.
            Connection conn = this.getConnection();

            if (conn == null) {
                return null;
            }

            Statement st = conn.createStatement();
            ArrayList<SWebMatReqNote> notes = new ArrayList<>();
            ResultSet resNts = st.executeQuery(notesQuery);
        
            while (resNts.next()) {
                notes.add(new SWebMatReqNote(
                    materialRequestId,
                    resNts.getInt("id_nts"),
                    resNts.getString("nts")
                ));
            }
            
            return notes;
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnDBMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();
    }

    private ArrayList<SWebMatReqEtyNote> getMaterialRequestEntryNotes(final int materialRequestId, final int idEty) {
        String etyQuery = "SELECT id_mat_req, id_ety, id_nts, notes FROM " + 
            SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY_NTS) + " " +
            "WHERE id_mat_req = " + materialRequestId + " ";

            if (idEty > 0) {
                etyQuery += "AND id_ety = " + idEty + " ";
            }
        
            try {
                // Conexión a la base de datos principal.
                Connection conn = this.getConnection();
    
                if (conn == null) {
                    return null;
                }
    
                Statement st = conn.createStatement();
                ResultSet resEtyNts = st.executeQuery(etyQuery);
                ArrayList<SWebMatReqEtyNote> etyNotes = new ArrayList<>();
                while (resEtyNts.next()) {
                    etyNotes.add(new SWebMatReqEtyNote(
                        resEtyNts.getInt("id_mat_req"),
                        resEtyNts.getInt("id_ety"),
                        resEtyNts.getInt("id_nts"),
                        resEtyNts.getString("notes")
                    ));
                }

                return etyNotes;
            }
            catch (SQLException ex) {
                Logger.getLogger(STrnDBMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
            }

            return new ArrayList<>();
    }

    /**
     * Obtiene la requisición asociada a una entrada específica de un documento.
     *
     * @param idYear Año del documento.
     * @param idDoc ID del documento.
     * @param idEty ID de la entrada del documento.
     * @return Una instancia de {@link SWebMaterialRequest} que contiene los datos de la requisición de material.
     */
    public SWebMaterialRequest getMaterialRequestOfDpsEty(final int idYear, final int idDoc, final int idEty) {
        try {
            // Conexión a la base de datos principal.
            Connection conn = this.getConnection();

            if (conn == null) {
                return null;
            }

            // Consulta para obtener la requisición.
            String query = "SELECT " +
                    "    dpsmr.fid_mat_req " +
                    "FROM " +
                    "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " AS dpsmr " +
                    "WHERE " +
                    "    dpsmr.fid_dps_year = " + idYear + " " +
                    "    AND dpsmr.fid_dps_doc = " + idDoc + " " +
                    "    AND dpsmr.fid_dps_ety = " + idEty + " " +
                    "GROUP BY dpsmr.fid_mat_req " +
                    "ORDER BY dpsmr.id_dps_mat_req DESC " +
                    "LIMIT 1;";

            Statement st = conn.createStatement();
            System.out.println(query);
            ResultSet res = st.executeQuery(query);

            SWebMaterialRequest oMatReq = new SWebMaterialRequest();

            if (res.next()) {
                // Asignar valores a la requisición.
                oMatReq.setIdMaterialRequest(res.getInt("dpsmr.fid_mat_req"));
                if (containsMatReqWithId(lMaterialRequests, oMatReq.getIdMaterialRequest())) {
                    SWebMaterialRequest oMatReqExist = getMatReqById(lMaterialRequests, oMatReq.getIdMaterialRequest());
                    if (oMatReqExist != null) {
                        return oMatReqExist;
                    }
                }
                
                oMatReq = this.getMatReqById(res.getInt("dpsmr.fid_mat_req"));

                try {
                    String fileName = this.msMainDatabase + "-" + "REQ" + "-" + oMatReq.getIdMaterialRequest() + ".pdf";
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
            else {
                return oMatReq;
            }
            
            lMaterialRequests.add(oMatReq);

            return oMatReq;
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnDBMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Retornar una instancia vacía si ocurre un error.
        return new SWebMaterialRequest();
    }

    /**
     * Método para obtener una lista de requisiciones de materiales.
     * 
     * @param startDate
     * @param endDate
     * @param idUser
     * @param idSessionUser
     * @param statusFilter
     * @return 
     */
    public ArrayList<SWebMaterialRequest> getMatReqs(String startDate, String endDate, int idUser, int idSessionUser, int statusFilter) {
        ArrayList<SWebMaterialRequest> materialRequests = new ArrayList<>();
        StringBuilder query = new StringBuilder(BASE_QUERY).append(WHERE_NOT_DELETED);

        try (Connection conn = this.getConnection()) {
            if (conn == null) {
                return materialRequests;
            }

            String whereUsers = getWhereUsers(conn, idSessionUser);

            // Aplica filtros según el estado
            appendStatusFilter(query, statusFilter, startDate, endDate, idUser, whereUsers);

            query.append(ORDER_BY_ID);

            Logger.getLogger(STrnDBMaterialRequest.class.getName()).log(Level.INFO, query.toString());

            try (Statement st = conn.createStatement();
                 ResultSet res = st.executeQuery(query.toString())) {

                while (res.next()) {
                    SWebMaterialRequest oMatReq = buildMaterialRequest(res);
                    oMatReq.getlNotes().addAll(this.loadMaterialRequestNotes(oMatReq.getIdMaterialRequest()));
                    materialRequests.add(oMatReq);
                }
            }
        }
        catch (Exception ex) {
            Logger.getLogger(STrnDBMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
        }

        return materialRequests;
    }

    /**
     * Obtiene el filtro de usuarios para el query según la configuración.
     */
    private String getWhereUsers(Connection conn, int idSessionUser) {
        try {
            String sCfg = SCfgUtils.getParamValue(conn.createStatement(), SDataConstantsSys.CFG_PARAM_TRN_DPS_AUTH_USR_GRP);
            if (!sCfg.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(sCfg);
                List<Integer> toUsers = SAuthJsonUtils.getArrayIfContains(rootNode, "usuariosSuper", "vistaRM", idSessionUser);
                if (!toUsers.isEmpty()) {
                    return "1 = 1 ";
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(STrnDBMaterialRequest.class.getName()).log(Level.WARNING, "Error parsing user config", ex);
        }
        return "";
    }

    /**
     * Añade el filtro de estado al query.
     */
    private void appendStatusFilter(StringBuilder query, int statusFilter, String startDate, String endDate, int idUser, String whereUsers) {
        String dateFilter = "mr.dt BETWEEN '" + startDate + "' AND '" + endDate + "' ";
        String userStepSubquery = idUser + " IN (SELECT steps1.fk_usr_step FROM " +
                SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS steps1 " +
                "WHERE NOT steps1.b_del AND steps1.res_tab_name_n = '" +
                SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "' AND steps1.res_pk_n1_n = mr.id_mat_req) ";

        switch (statusFilter) {
            case -2: // TODAS MIS requisiciones
                if (whereUsers.isEmpty()) {
                    query.append("AND mr.fk_usr_req = ").append(idUser).append(" AND ").append(dateFilter);
                } else {
                    query.append("AND ((").append(whereUsers).append(") OR (mr.fk_usr_req = ").append(idUser).append(")) AND ").append(dateFilter);
                }
                break;
            case -1: // requisiciones PENDIENTES
                query.append("AND cfg_get_st_authorn(")
                     .append(SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST).append(", '")
                     .append(SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ)).append("', mr.id_mat_req, NULL, NULL, NULL, NULL) IN (")
                     .append(SAuthorizationUtils.AUTH_STATUS_PENDING).append(", ")
                     .append(SAuthorizationUtils.AUTH_STATUS_IN_PROCESS).append(") ");
                if (whereUsers.isEmpty()) {
                    query.append("AND ").append(userStepSubquery);
                } else {
                    query.append("AND ((").append(whereUsers).append(") OR (").append(userStepSubquery).append(")) ");
                }
                break;
            case 0: // TODAS EN LAS QUE PARTICIPO
                query.append("AND ").append(dateFilter);
                if (whereUsers.isEmpty()) {
                    query.append("AND ").append(userStepSubquery);
                } else {
                    query.append("AND ((").append(whereUsers).append(") OR (").append(userStepSubquery).append(")) ");
                }
                break;
            default:
                if (statusFilter > 0) {
                    query.append("AND cfg_get_st_authorn(")
                         .append(SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST).append(", '")
                         .append(SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ)).append("', mr.id_mat_req, NULL, NULL, NULL, NULL) = ")
                         .append(statusFilter).append(" AND ").append(dateFilter);
                }
                break;
        }
    }

    /**
     * Obtiene la requisición por ID.
     * 
     * @param idMaterialRequest
     * @return 
     */
    public SWebMaterialRequest getMatReqById(int idMaterialRequest) {
        String query = BASE_QUERY + WHERE_NOT_DELETED + "AND mr.id_mat_req = " + idMaterialRequest + " LIMIT 1;";

        try (Connection conn = this.getConnection();
             Statement st = conn.createStatement();
             ResultSet res = st.executeQuery(query)) {

            if (res.next()) {
                SWebMaterialRequest oMatReq = buildMaterialRequest(res);
                oMatReq.getlNotes().addAll(this.loadMaterialRequestNotes(oMatReq.getIdMaterialRequest()));
                oMatReq.getlEtys().addAll(this.loadMaterialRequestEtys(oMatReq.getIdMaterialRequest()));
                return oMatReq;
            }

        } catch (SQLException ex) {
            Logger.getLogger(STrnDBMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Construye un objeto SWebMaterialRequest a partir de un ResultSet.
     */
    private SWebMaterialRequest buildMaterialRequest(ResultSet res) throws SQLException {
        SWebMaterialRequest oMatReq = new SWebMaterialRequest();

        oMatReq.setIdMaterialRequest(res.getInt("id_mat_req"));
        oMatReq.setMrFolio(res.getString("_mr_folio"));
        oMatReq.setMrDate(res.getString("_mr_dt"));
        String reqDate = res.getString("dt_req_n");
        if (reqDate == null || reqDate.isEmpty()) {
            // sumar 2 meses a la fecha _mr_dt:
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                c.setTime(sdf.parse(res.getString("_mr_dt")));
                c.add(Calendar.MONTH, 2);
                reqDate = sdf.format(c.getTime());
            }
            catch (ParseException e) {
                Logger.getLogger(STrnDBMaterialRequest.class.getName()).log(Level.SEVERE, "Error parsing date", e);
            }
        }
        oMatReq.setMrRequiredDate(reqDate);
        oMatReq.setMrUser(res.getString("_mr_usr"));
        oMatReq.setMrPriority(res.getString("_mr_priority"));
        oMatReq.setMrType(res.getString("_mr_type"));
        oMatReq.setMrTotal(res.getDouble("_mr_total"));
        oMatReq.setMrClass(res.getString("_mr_class"));
        oMatReq.setMrStorageCloudUrl("#");
        oMatReq.setMrProvEntity(res.getString("_mr_prov_ent"));
        oMatReq.setMrStatus(res.getString("_mr_status"));
        oMatReq.setMrNature(res.getString("_mr_nat"));
        oMatReq.setMrItemReference(res.getString("_mr_item_ref"));
        oMatReq.setUserInTurn(res.getString("user_in_turn"));
        oMatReq.setAuthText(res.getString("auth_status"));
        oMatReq.setAuthStatusId(res.getInt("auth_status_id"));

        oMatReq.getlCostCenter().clear();
        oMatReq.getlCostCenter().addAll(this.getCostCenters(oMatReq.getIdMaterialRequest()));

        return oMatReq;
    }

    /**
     * Carga las entradas de requisición asociadas a una requisición específica.
     *
     * @param materialRequestId ID de la requisición.
     * @return Una lista de objetos SWebMaterialRequestEty que representan las entradas de la requisición.
     */
    public ArrayList<SWebMaterialRequestEty> loadMaterialRequestEtys(final int materialRequestId) {
        /**
         * SELECT 
            mre.id_mat_req,
            mre.id_ety,
            mre.qty,
            mre.price_u_sys,
            mre.price_u,
            mre.tot_r,
            i.item_key,
            i.item
        FROM
            erp_aeth.trn_mat_req_ety AS mre
                INNER JOIN
            erp.itmu_item AS i ON mre.fk_item = i.id_item
        WHERE
            NOT mre.b_del AND mre.id_mat_req = 8263;
         */
        String etysQuery = "SELECT " +
                            "    mre.id_mat_req, " +
                            "    mre.id_ety, " +
                            "    mre.qty, " +
                            "    mre.price_u_sys, " +
                            "    mre.price_u, " +
                            "    mre.tot_r, " +
                            "    i.item_key, " +
                            "    i.item AS item_name, " +
                            "    u.symbol AS unit_symbol, " +
                            "    u.unit AS unit_name, " +
                            "    iref.item_key AS item_ref_key, " +
                            "    iref.item AS item_ref_name, " +
                            "    cc.pk_cc, " +
                            "    cc.cc, " +
                            "    cc.id_cc " +
                            "FROM " +
                            SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " AS mre  " +
                            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON mre.fk_item = i.id_item  " +
                            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON mre.fk_unit = u.id_unit  " +
                            "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS iref ON mre.fk_item_ref_n = iref.id_item  " +
                            "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cc ON mre.fk_cc_n = cc.pk_cc  " +
                            // Agregar la tabla de notas de partidas de requisición
                            // INNER JOIN erp.trn_mat_req_ety_nts AS mre_nts ON mre.id_mat_req = mre_nts.fid_mat_req AND mre.id_ety = mre_nts.fid_mat_req_ety  "
                            // Agregar la tabla de notas de requisición
                            // INNER JOIN erp.trn_mat_req_nts AS mr_nts ON mr_nts.fid_mat_req = mre.id_mat_req  "
        "WHERE " + 
                            "    NOT mre.b_del AND mre.id_mat_req = " + materialRequestId;
    
        try {
            // Conexión a la base de datos principal.
            Connection conn = this.getConnection();

            if (conn == null) {
                return null;
            }

            Statement st = conn.createStatement();
            Logger.getLogger(STrnDBMaterialRequest.class.getName()).log(Level.INFO, etysQuery);
            ArrayList<SWebMaterialRequestEty> etys = new ArrayList<>();
            ResultSet resEtys = st.executeQuery(etysQuery);
            SWebMaterialRequestEty oMatReqEty = null;
            while (resEtys.next()) {
                oMatReqEty = new SWebMaterialRequestEty();
                oMatReqEty.setIdMaterialRequest(resEtys.getInt("id_mat_req"));
                oMatReqEty.setIdEty(resEtys.getInt("id_ety"));
                oMatReqEty.setQuantity(resEtys.getDouble("qty"));
                oMatReqEty.setPriceUnitarySystem(resEtys.getDouble("price_u_sys"));
                oMatReqEty.setPriceUnitary(resEtys.getDouble("price_u"));
                oMatReqEty.setTotal(resEtys.getDouble("tot_r"));
                oMatReqEty.setItemKey(resEtys.getString("item_key"));
                oMatReqEty.setItemName(resEtys.getString("item_name"));
                oMatReqEty.setUnitSymbol(resEtys.getString("unit_symbol"));
                oMatReqEty.setUnitName(resEtys.getString("unit_name"));
                oMatReqEty.setItemRefKey(resEtys.getString("item_ref_key"));
                oMatReqEty.setItemRefName(resEtys.getString("item_ref_name"));
                oMatReqEty.setIdCostCenter(resEtys.getInt("cc.pk_cc"));
                oMatReqEty.setCostCenter(resEtys.getString("cc.id_cc") + " - " + resEtys.getString("cc.cc"));

                oMatReqEty.getlEtyNotes().clear();
                oMatReqEty.getlEtyNotes().addAll(this.getMaterialRequestEntryNotes(oMatReqEty.getIdMaterialRequest(), oMatReqEty.getIdEty()));

                etys.add(oMatReqEty);
            }
            
            return etys;
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnDBMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();
    }

    /**
     * Obtiene los centros de costo asociados a una requisición.
     *
     * @param materialRequestId ID de la requisición.
     * @return Una lista de objetos SWebCostCenter que representan los centros de costo asociados.
     */
    private ArrayList<SWebCostCenter> getCostCenters(final int materialRequestId) {
        String costCenterQuery = "SELECT  " +
                "    mrcc.*, cc.pk_cc, mrcc.per, cc.cc, cc.id_cc " +
                "FROM " +
                "    " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_CC) + " AS mrcc " +
                "        INNER JOIN " +
                "    " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cc ON mrcc.id_cc = cc.pk_cc " +
                "    WHERE mrcc.id_mat_req = " + materialRequestId + ";";

        try {
            // Conexión a la base de datos principal.
            Connection conn = this.getConnection();

            if (conn == null) {
                return null;
            }

            Statement st = conn.createStatement();
            ArrayList<SWebCostCenter> costCenters = new ArrayList<>();
            ResultSet resCostCenters = st.executeQuery(costCenterQuery);
            while (resCostCenters.next()) {
                SWebCostCenter oCostCenter = new SWebCostCenter();
                oCostCenter.setIdMatReq(resCostCenters.getInt("mrcc.id_mat_req"));
                oCostCenter.setIdConsumeEntity(resCostCenters.getInt("mrcc.id_mat_ent_cons_ent"));
                oCostCenter.setIdConsumeSubEntity(resCostCenters.getInt("mrcc.id_mat_subent_cons_ent"));
                oCostCenter.setIdConsumeSubSubEntity(resCostCenters.getInt("mrcc.id_mat_subent_cons_subent"));
                oCostCenter.setIdCostCenter(resCostCenters.getInt("mrcc.id_cc"));
                oCostCenter.setPercentage(resCostCenters.getDouble("per"));
                oCostCenter.setCostCenter(resCostCenters.getString("cc.id_cc") + " - " + resCostCenters.getString("cc.cc"));

                costCenters.add(oCostCenter);
            }
            
            return costCenters;
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnDBMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();
    }
}
