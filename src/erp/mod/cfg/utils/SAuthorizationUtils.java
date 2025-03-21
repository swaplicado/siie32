/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.utils;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbAuthorizationPath;
import erp.mod.cfg.db.SDbAuthorizationStep;
import erp.mod.cfg.db.SDbMms;
import erp.mod.trn.db.SDbSupplierFileProcess;
import erp.mod.trn.form.SDialogSelectAuthornPath;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SProcDpsSendAuthornWeb;
import erp.mtrn.data.STrnUtilities;
import erp.siieapp.SUserResource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.json.simple.JSONObject;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiSession;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;

/**
 * Clase de utilería de autorizaciones
 * 
 * @author Edwin Carmona, Isabel Servín
 */
public abstract class SAuthorizationUtils {
    
    /**
     * Query base para la obtención de la ruta de autorización
     */
    private static final String QUERY_AUTHS = "SELECT " +
            "    cas.*, ua.usr AS auth_user, ur.usr AS rej_user, us.usr AS step_user " +
            "FROM " +
            "    " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS cas " +
            "        LEFT JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ua ON cas.fk_usr_authorn_n = ua.id_usr " +
            "        LEFT JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON cas.fk_usr_reject_n = ur.id_usr " +
            "        LEFT JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS us ON cas.fk_usr_step = us.id_usr " +
            "WHERE " +
            "    NOT cas.b_del AND cas.b_req ";
    
    /**
     * Constates de estatus de autorización
     */
    public static final int AUTH_STATUS_NA = 1;
    public static final int AUTH_STATUS_PENDING = 2;
    public static final int AUTH_STATUS_IN_PROCESS = 3;
    public static final int AUTH_STATUS_AUTHORIZED = 4;
    public static final int AUTH_STATUS_REJECTED = 5;
    
    public static final HashMap<Integer, String> AUTH_STATUS_DESC = new HashMap<>();
    
    static {
        AUTH_STATUS_DESC.put(AUTH_STATUS_NA, "No aplica");
        AUTH_STATUS_DESC.put(AUTH_STATUS_PENDING, "Pendiente");
        AUTH_STATUS_DESC.put(AUTH_STATUS_IN_PROCESS, "En proceso");
        AUTH_STATUS_DESC.put(AUTH_STATUS_AUTHORIZED, "Autorizado");
        AUTH_STATUS_DESC.put(AUTH_STATUS_REJECTED, "Rechazado");
    }
    
    /**
     * Constantes de tipo de autorización
     */
    public static final int AUTH_TYPE_MAT_REQUEST = 1;
    public static final int AUTH_TYPE_DPS = 2;
    
    /**
     * Constantes de acción sobre recursos
     */
    public static final int AUTH_ACTION_AUTHORIZE = 1;
    public static final int AUTH_ACTION_REJECT = 2;
    
    /**
     * Constantes para el envío de correos de autorización
     */
    public static final int AUTH_MAIL_AUTH_PEND = 1;
    public static final int AUTH_MAIL_AUTH_DONE = 2;
    public static final int AUTH_MAIL_AUTH_REJ = 3;
    
    /**
     * Determina si un recurso está autorizado o no.
     * Si lo está devuelve true, si devuelve false NO significa que esté rechazado.
     * 
     * @param session
     * @param authorizationType
     * @param pk
     * 
     * @return 
     */
    public static boolean isAuthorized(SGuiSession session, final int authorizationType, final Object pk) {
        String condPk = "";
        switch(authorizationType) {
            case AUTH_TYPE_MAT_REQUEST:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " ";
                break;
                
            case AUTH_TYPE_DPS:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " AND res_pk_n2_n = " + ((int[]) pk)[1] + " ";
                break;
        }
        
        String sql = QUERY_AUTHS + " AND cas.fk_tp_authorn = " + authorizationType + " AND " + condPk;
        try {
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            boolean authBySteps = false;
            while(res.next()) {
                if (! res.getBoolean("b_authorn")) {
                    return false;
                }
                
                authBySteps = true;
            }
            
            return authBySteps;
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * Obtiene los usuarios que están en turno de autorización del recurso.
     * 
     * @param statement
     * @param authorizationType
     * @param pk
     * @param toNotification Este parámetro se envia cuando el método es utilizado para enviar una notificación a los usuarios
     *                          Por ende, filtra solo los usuarios que tengan activada esta bandera en las tablas pivote de nodos.
     * @return 
     */
    public static ArrayList<Integer> getUsersInTurnAuth(java.sql.Statement statement, final int authorizationType, final Object pk, final boolean toNotification) {
        String condPk1 = "";
        String condPk2 = "";
        String condTableName = "";
        switch(authorizationType) {
            case AUTH_TYPE_MAT_REQUEST:
                condPk1 = "step1.res_pk_n1_n = " + ((int[]) pk)[0] + " ";
                condPk2 = "step2.res_pk_n1_n = " + ((int[]) pk)[0] + " ";
                condTableName = "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "'";
                break;
                
            case AUTH_TYPE_DPS:
                condPk1 = "step1.res_pk_n1_n = " + ((int[]) pk)[0] + " AND step1.res_pk_n2_n = " + ((int[]) pk)[1] + " ";
                condPk2 = "step2.res_pk_n1_n = " + ((int[]) pk)[0] + " AND step2.res_pk_n2_n = " + ((int[]) pk)[1] + " ";
                condTableName = "'" + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + "'";
                break;
        }
        
        try {
            String sql;
            ArrayList<Integer> lUsers = new ArrayList<>();
            if (! toNotification) {
                sql = "SELECT  " +
                            "    step1.fk_usr_step " +
                            "FROM " +
                            "    " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS step1 " +
                            "WHERE " +
                            "    NOT step1.b_del " +
                            "        AND step1.res_tab_name_n = " + condTableName + " " +
                            "        AND " + condPk1 +
                            "        AND NOT step1.b_authorn " +
                            "        AND NOT step1.b_reject " +
                            "        AND step1.lev = (SELECT  " +
                            "            step2.lev " +
                            "        FROM " +
                            "            " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS step2 " +
                            "        WHERE " +
                            "            NOT step2.b_del " +
                            "                AND step2.res_tab_name_n = " + condTableName + " " +
                            "                AND " + condPk2 +
                            "                AND NOT step2.b_authorn " +
                            "                AND NOT step2.b_reject " +
                            "        ORDER BY step2.lev ASC " +
                            "        LIMIT 1);";
                
                Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.INFO, sql);
                ResultSet res = statement.executeQuery(sql);
                while(res.next()) {
                    lUsers.add(res.getInt("step1.fk_usr_step"));
                }
            }
            else {
                sql = "SELECT DISTINCT " +
                            "    step1.fk_node_step_n " +
                            "FROM " +
                            "    " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS step1 " +
                            "WHERE " +
                            "    NOT step1.b_del " +
                            "        AND step1.res_tab_name_n = " + condTableName + " " +
                            "        AND " + condPk1 +
                            "        AND NOT step1.b_authorn " +
                            "        AND NOT step1.b_reject " +
                            "        AND step1.lev = (SELECT  " +
                            "            step2.lev " +
                            "        FROM " +
                            "            " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS step2 " +
                            "        WHERE " +
                            "            NOT step2.b_del " +
                            "                AND step2.res_tab_name_n = " + condTableName + " " +
                            "                AND " + condPk2 +
                            "                AND NOT step2.b_authorn " +
                            "                AND NOT step2.b_reject " +
                            "        ORDER BY step2.lev ASC " +
                            "        LIMIT 1);";
               
                Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.INFO, sql);
                ResultSet res = statement.getConnection().createStatement().executeQuery(sql);
                while(res.next()) {
                    lUsers.addAll(SAuthorizationUtils.getUsersOfAutorizationNode(statement.getConnection(), res.getInt("step1.fk_node_step_n"), toNotification));
                }
            }
            
            return lUsers;
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Obtiene el nombre de usuario y correo de los implicados en turno de autorización del recurso.
     * 
     * @param statement
     * @param userIds
     * @return 
     */
    public static HashMap<String, String> getMailsOfUsers(java.sql.Statement statement, ArrayList<Integer> userIds) {
        StringBuilder userIn = new StringBuilder("(");
        for (int i = 0; i < userIds.size(); i++) {
            userIn.append(userIds.get(i));
            if (i < userIds.size() - 1) {
                userIn.append(", ");
            }
        }
        userIn.append(")");
        
        String sql = "SELECT " +
                    "   usr, email " +
                    "FROM " +
                    "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " " +
                    "WHERE " +
                    "    email IS NOT NULL AND LENGTH(email) > 0 " +
                    "    AND id_usr IN " + userIn + ";";
        try {
            ResultSet res = statement.executeQuery(sql);
            HashMap<String, String> lMails = new HashMap<>();
            while(res.next()) {
                lMails.put(res.getString("usr"), res.getString("email"));
            }
            
            return lMails;
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new HashMap<>();
    }
    
    
    /**
     * Obtiene el folio del documento de acuerdo a su llave primaria
     * 
     * @param statement
     * @param pk
     * 
     * @return 
     */
    public static String getDpsFolio(java.sql.Statement statement, int[] pk) {
        String sql = "SELECT CONCAT(td.code, ' ', CONCAT(num_ser, IF(length(num_ser) = 0, '', '-'), num)) AS descrip "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS td ON d.fid_ct_dps = td.id_ct_dps AND d.fid_cl_dps = td.id_cl_dps AND d.fid_tp_dps = td.id_tp_dps "
                + "WHERE id_year = " + ((int[]) pk)[0] +" AND id_doc = " + ((int[]) pk)[1] + " ";
        try {
            ResultSet res = statement.executeQuery(sql);
            if (res.next()) {
                return res.getString("descrip");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
    }

    /**
     * Obtiene el nombre de usuario de acuerdo a su id
     * 
     * @param statement
     * @param userId
     * 
     * @return 
     */
    public static String getUserName(java.sql.Statement statement, int userId) {
        String sql = "SELECT usr "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " "
                + "WHERE id_usr = " + userId + ";";
        try {
            ResultSet res = statement.executeQuery(sql);
            if (res.next()) {
                return res.getString("usr");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }
    
    /**
     * Retorna el estatus del recurso recibido
     * 
     * @param session
     * @param authorizationType
     * @param pk
     * 
     * @return puede ser: AUTH_STATUS_AUTHORIZED, AUTH_STATUS_REJECTED, AUTH_STATUS_PENDING, AUTH_STATUS_IN_PROCESS, AUTH_STATUS_NA
     */
    public static int getAuthStatus(SGuiSession session, final int authorizationType, final Object pk) {
        String condPk = "";
        switch(authorizationType) {
            case AUTH_TYPE_MAT_REQUEST:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " ";
                break;
                
            case AUTH_TYPE_DPS:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " AND res_pk_n2_n = " + ((int[]) pk)[1] + " ";
                break;
        }
        
        String query = "SELECT " +
                        "    COUNT(*) AS n_rows " +
                        "FROM " +
                        "    " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS cas " +
                        "        LEFT JOIN " +
                        "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ua ON cas.fk_usr_authorn_n = ua.id_usr " +
                        "        LEFT JOIN " +
                        "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON cas.fk_usr_reject_n = ur.id_usr " +
                        "        LEFT JOIN " +
                        "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS us ON cas.fk_usr_step = us.id_usr " +
                        "WHERE " +
                        "    NOT cas.b_del AND cas.b_req " + " AND " + condPk;
        
        try {
            /*
            * Query TODOS
            **********************************************************************************************************
            */
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(query);
            
            int allSteps = 0; 
            if (res.next()) {
                allSteps = res.getInt("n_rows");
            }
            
            if (allSteps == 0) {
                return AUTH_STATUS_NA;
            }
            
            /*
            * Query RECHAZADOS
            **********************************************************************************************************
            */
            String queryRej = query + " AND cas.b_reject";
            
            ResultSet resRej = session.getDatabase().getConnection().createStatement().executeQuery(queryRej);
            
            int rejSteps = 0; 
            if (resRej.next()) {
                rejSteps = resRej.getInt("n_rows");
            }
            
            if (rejSteps > 0) {
                return AUTH_STATUS_REJECTED;
            }
            
            /*
            * Query AUTORIZADOS
            **********************************************************************************************************
            */
            String queryAuth = query + " AND cas.b_authorn";
            
            ResultSet resAuth = session.getDatabase().getConnection().createStatement().executeQuery(queryAuth);
            
            int authSteps = 0; 
            if (resAuth.next()) {
                authSteps = resAuth.getInt("n_rows");
            }
            
            if (authSteps == allSteps) {
                return AUTH_STATUS_AUTHORIZED;
            }
            
            /*
            * Query PENDIENTES
            **********************************************************************************************************
            */
            String queryPending = query + " AND NOT b_authorn AND NOT b_reject";
            
            ResultSet resPending = session.getDatabase().getConnection().createStatement().executeQuery(queryPending);
            
            int pendingSteps = 0; 
            if (resPending.next()) {
                pendingSteps = resPending.getInt("n_rows");
            }
            
            if (pendingSteps == allSteps) {
                return AUTH_STATUS_PENDING;
            }
            
            /*
            * DEFAULT
            **********************************************************************************************************
            */
            return AUTH_STATUS_IN_PROCESS;
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return AUTH_STATUS_IN_PROCESS;
    }
    
    /**
     * Devuelve una lista de los estatus de autorización del recurso y tipo de autorización.
     * Estos estatus son solamente de los pasos REQUERIDOS.
     * 
     * @param session
     * @param authorizationType
     * @param pk
     * 
     * @return 
     */
    public static ArrayList<SAuthStatus> getStatusAuthorized(SGuiSession session, final int authorizationType, final Object pk) {
        String condPk = "";
        switch(authorizationType) {
            case AUTH_TYPE_MAT_REQUEST:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " ";
                break;
                
            case AUTH_TYPE_DPS:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " AND res_pk_n2_n = " + ((int[]) pk)[1] + " ";
                break;
        }
        
        ArrayList<SAuthStatus> lStatus = new ArrayList<>();
        String sql = QUERY_AUTHS + " AND cas.fk_tp_authorn = " + authorizationType + " AND " + condPk;
        try {
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            
            while(res.next()) {
                if (res.getBoolean("b_authorn")) {
                    lStatus.add(new SAuthStatus(AUTH_STATUS_AUTHORIZED, res.getString("auth_user")));
                }
                else if (res.getBoolean("b_reject")) {
                    lStatus.add(new SAuthStatus(AUTH_STATUS_REJECTED, res.getString("rej_user")));
                }
                else {
                    lStatus.add(new SAuthStatus(AUTH_STATUS_PENDING, res.getString("step_user")));
                }
            }
            
            if (lStatus.size() > 0) {
                return lStatus;
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Funcionalidad para aprobar de manera 'forzada' un recurso mediante su llave primaria.
     * Esta funcionalidad obtiene todos los pasos de autorización que no estén eliminados,
     * que no estén autorizados, y los marca como autorizados por el usuario que hace la acción, 
     * agregando la leyenda "Autorización forzada".
     * 
     * @param session
     * @param pk
     * @param resourceType
     * @param userId
     * @return 
     */
    public static String hardAuthorize(SGuiSession session, final int[] pk, final int resourceType, final int userId) {
        String condPk = "";
        switch(resourceType) {
            case AUTH_TYPE_MAT_REQUEST:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " ";
                
                break;
            case AUTH_TYPE_DPS:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " AND res_pk_n2_n = " + ((int[]) pk)[1] + " ";
                
                break;
        }
        
        String sql = "SELECT id_authorn_step "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " "
                + "WHERE NOT b_del AND "
                    + "fk_tp_authorn = " + resourceType + " AND "
                    + "b_req AND NOT b_authorn AND "
                    + condPk + ";";
        
        SDbAuthorizationStep oStepAux;
        try {
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            boolean bHasSteps = false;
            while (res.next()) {
                bHasSteps = true;
                oStepAux = new SDbAuthorizationStep();
                oStepAux.read(session, new int[] { res.getInt("id_authorn_step") });
                oStepAux.setDateTimeAuthorized_n(new Date());
                oStepAux.setFkUserAuthorizationId_n(userId);
                oStepAux.setAuthorized(true);
                oStepAux.setDateTimeRejected_n(null);
                oStepAux.setFkUserRejectId_n(0);
                oStepAux.setRejected(false);
                oStepAux.setComments("Autorización forzada.");
                
                oStepAux.save(session);
            }
            
            if (! bHasSteps) {
                return "No hay autorizaciones pendientes para este documento.";
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
            return "Ocurrió un error en la base de datos al realizar el proceso " + ex.getMessage();
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
            return "Ocurrió un error al realizar el proceso. " + ex.getMessage();
        }
        
        return "";
    }
    
    /**
     * Autorizar o rechazar recurso
     * 
     * @param session
     * @param pk
     * @param resourceType
     * @param iAction
     * @return 
     */
    public static String authorizeOrReject(SGuiSession session, final int[] pk, final int resourceType, final int iAction) {
        String reason = "";
        if (iAction == SAuthorizationUtils.AUTH_ACTION_REJECT) {
            JTextArea textArea = new JTextArea(5, 40); // Set the number of rows and columns
            JScrollPane scrollPane = new JScrollPane(textArea);

            int option = JOptionPane.showOptionDialog(null, scrollPane, "Ingrese motivo de rechazo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

            if (option == JOptionPane.OK_OPTION) {
                reason = textArea.getText();
            }
            else {
                return "Acción cancelada";
            }
        }
        
        String response = SAuthorizationUtils.authOrRejResource(session,
                                                                        iAction,
                                                                        resourceType,
                                                                        pk,
                                                                        session.getUser().getPkUserId(),
                                                                        reason);
        
        return response;
    }
    
    /**
     * Petición de autorización o rechazo de recurso.
     * Si el proceso ha sido exitoso devuelve un String vacío, si ha ocurrido un error el 
     * String explicará lo que ha sucedido
     * 
     * @param session
     * @param action
     * @param authorizationType
     * @param pk
     * @param userId
     * @param reasonRej
     * 
     * @return 
     */
    public static String authOrRejResource(SGuiSession session, final int action, final int authorizationType, final Object pk, final int userId, String reasonRej) {
        String condPk = "";
        switch(authorizationType) {
            case AUTH_TYPE_MAT_REQUEST:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " ";
                
                break;
            case AUTH_TYPE_DPS:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " AND res_pk_n2_n = " + ((int[]) pk)[1] + " ";
                
                break;
        }
        
        String sql = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " "
                + "WHERE NOT b_del AND "
                    + "fk_tp_authorn = " + authorizationType + " AND "
                    + "fk_usr_step = " + userId + " AND "
                    + condPk + ";";
        
        try {
            System.out.println("Obtención de Step: " + sql);
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            
            if (res.next()) {
                if (action == SAuthorizationUtils.AUTH_ACTION_AUTHORIZE) {
                    return SAuthorizationUtils.authorizeById(session, res.getInt("id_authorn_step"), reasonRej);
                }
                else {
                    return SAuthorizationUtils.rejectById(session, res.getInt("id_authorn_step"), reasonRej);
                }
            }
            else {
                String userReject = "SELECT usr "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.USRU_USR) 
                    + " WHERE id_usr = " + userId;
        
                String name = "";
                ResultSet resultSetReject = session.getStatement().getConnection().createStatement().executeQuery(userReject);
                if (resultSetReject.next()) {
                    name = resultSetReject.getString("usr");
                    return "El usuario " + name + " no puede autorizar este documento.";
                }
                else {
                    return "El usuario no puede autorizar este documento.";
                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
    }
    
    /**
     * Autorizar un recurso en base al id de la tabla de pasos de autorización.
     * Si el proceso ha sido exitoso devuelve un String vacío, si no devuleve una cadena con el
     * error ocurrido
     * 
     * @param session
     * @param idAuthStep
     * @param comments
     * 
     * @return 
     */
    public static String authorizeById(SGuiSession session, final int idAuthStep, String comments) {
        SDbAuthorizationStep oStep = new SDbAuthorizationStep();
        try {
            oStep.read(session, new int[] { idAuthStep });
            
            if (oStep.isAuthorized()) {
                return "Este documento ya está autorizado.";
            }
            if (oStep.getFkUserStepId() != session.getUser().getPkUserId()) {
                return "No se puede autorizar este documento, usuario incorrecto.";
            }
            
            String rowsQuery = QUERY_AUTHS + " AND "
                        + "cas.fk_tp_authorn = " + oStep.getFkAuthorizationTypeId() + " AND "
                        + "NOT cas.b_authorn AND "
                        + "cas.lev < " + oStep.getUserLevel() + " AND "
                        + "cas.res_tab_name_n " + (oStep.getResourceTableName_n() == null ? "IS NULL" : " = '" + oStep.getResourceTableName_n() + "'") + " AND "
                        + "cas.res_pk_n1_n " + (oStep.getResourcePkNum1_n() == 0 ? "IS NULL" : " = " + oStep.getResourcePkNum1_n() + "") + " AND "
                        + "cas.res_pk_n2_n " + (oStep.getResourcePkNum2_n() == 0 ? "IS NULL" : " = " + oStep.getResourcePkNum2_n() + "") + " AND "
                        + "cas.res_pk_n3_n " + (oStep.getResourcePkNum3_n() == 0 ? "IS NULL" : " = " + oStep.getResourcePkNum3_n() + "") + " AND "
                        + "cas.res_pk_n4_n " + (oStep.getResourcePkNum4_n() == 0 ? "IS NULL" : " = " + oStep.getResourcePkNum4_n() + "") + " AND "
                        + "cas.res_pk_n5_n " + (oStep.getResourcePkNum5_n() == 0 ? "IS NULL" : " = " + oStep.getResourcePkNum5_n() + "") + " AND "
                        + "cas.id_authorn_step <> " + oStep.getPkAuthorizationStepId() + " "
                        + ";";
                
                ArrayList<SAuthStatus> lStatus = new ArrayList<>();
                ResultSet resRows = session.getDatabase().getConnection().createStatement().executeQuery(rowsQuery);
                while (resRows.next()) {
                    if (resRows.getBoolean("b_reject")) {
                        lStatus.add(new SAuthStatus(AUTH_STATUS_REJECTED, resRows.getString("rej_user")));
                    }
                    else {
                        lStatus.add(new SAuthStatus(AUTH_STATUS_PENDING, resRows.getString("step_user")));
                    }
                }
                
                if (lStatus.size() > 0) {
                    String resp = "";
                    for (SAuthStatus oStatus : lStatus) {
                        if (oStatus.getStatus() == AUTH_STATUS_REJECTED) {
                            resp += "El usuario " + oStatus.getUser() + " rechazó este documento.\n";
                        }
                        else {
                            resp += "El usuario " + oStatus.getUser() + " no ha autorizado este documento.\n";
                        }
                    }
                    
                    return resp;
                }
                
                oStep.setDateTimeAuthorized_n(new Date());
                oStep.setFkUserAuthorizationId_n(session.getUser().getPkUserId());
                oStep.setAuthorized(true);
                oStep.setDateTimeRejected_n(null);
                oStep.setFkUserRejectId_n(0);
                oStep.setRejected(false);
                oStep.setComments(comments);
                
                oStep.save(session);
                
                SAuthorizationUtils.doAfterAuthorization(session, oStep);
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
     
        return "";
    }
    
    /**
     * Rechazar un recurso en base al id de la tabla de pasos de autorización.
     * Si el proceso ha sido exitoso devuelve un String vacío, si no devuleve una cadena con el
     * error ocurrido
     * 
     * @param session
     * @param idAuthStep
     * @param reasonRej
     * 
     * @return 
     */
    public static String rejectById(SGuiSession session, final int idAuthStep, String reasonRej) {
        SDbAuthorizationStep oStep = new SDbAuthorizationStep();
        try {
            oStep.read(session, new int[] { idAuthStep });
            
            if (oStep.isRejected()) {
                return "Este documento ya está rechazado.";
            }
            if (oStep.getFkUserStepId() != session.getUser().getPkUserId()) {
                return "No se puede rechazar este documento, usuario incorrecto.";
            }
            
            String rowsQuery = QUERY_AUTHS + " AND "
                        + "cas.fk_tp_authorn = " + oStep.getFkAuthorizationTypeId() + " AND "
                        + "cas.b_authorn AND "
                        + "cas.lev > " + oStep.getUserLevel() + " AND "
                        + "cas.res_tab_name_n " + (oStep.getResourceTableName_n() == null ? "IS NULL" : " = '" + oStep.getResourceTableName_n() + "'") + " AND "
                        + "cas.res_pk_n1_n " + (oStep.getResourcePkNum1_n() == 0 ? "IS NULL" : " = " + oStep.getResourcePkNum1_n() + "") + " AND "
                        + "cas.res_pk_n2_n " + (oStep.getResourcePkNum2_n() == 0 ? "IS NULL" : " = " + oStep.getResourcePkNum2_n() + "") + " AND "
                        + "cas.res_pk_n3_n " + (oStep.getResourcePkNum3_n() == 0 ? "IS NULL" : " = " + oStep.getResourcePkNum3_n() + "") + " AND "
                        + "cas.res_pk_n4_n " + (oStep.getResourcePkNum4_n() == 0 ? "IS NULL" : " = " + oStep.getResourcePkNum4_n() + "") + " AND "
                        + "cas.res_pk_n5_n " + (oStep.getResourcePkNum5_n() == 0 ? "IS NULL" : " = " + oStep.getResourcePkNum5_n() + "") + " AND "
                        + "cas.id_authorn_step <> " + oStep.getPkAuthorizationStepId() + " "
                        + ";";
                
                ArrayList<SAuthStatus> lStatus = new ArrayList<>();
                ResultSet resRows = session.getDatabase().getConnection().createStatement().executeQuery(rowsQuery);
                System.out.println("rechazar por ID (" +  idAuthStep + "): " + rowsQuery);
                while (resRows.next()) {
                    if (resRows.getBoolean("b_authorn")) {
                        lStatus.add(new SAuthStatus(AUTH_STATUS_AUTHORIZED, resRows.getString("auth_user")));
                    }
                }
                
                if (lStatus.size() > 0) {
                    /**
                     * Se comenta la funcionalidad para permitir el rechazo aún cuando alguien ya haya autorizado.
                     * Edwin Carmona. 2024-12-12
                     */
//                    String resp = "";
//                    for (SAuthStatus oStatus : lStatus) {
////                        if (oStatus.getStatus() == AUTH_STATUS_AUTHORIZED) {
////                            resp += "No se puede rechazar, el usuario " + oStatus.getUser() + " ya autorizó este documento.\n";
////                        }
//                    }
//                    
//                    return resp;
                }
                
                oStep.setDateTimeAuthorized_n(null);
                oStep.setFkUserAuthorizationId_n(0);
                oStep.setAuthorized(false);
                oStep.setDateTimeRejected_n(new Date());
                oStep.setFkUserRejectId_n(session.getUser().getPkUserId());
                oStep.setRejected(true);
                oStep.setComments(reasonRej);
                
                oStep.save(session);
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
     
        return "";
    }
    
    /**
     * Procesa la configuración del recurso recibido y guarda en la base de datos la ruta de autorizaciones con los
     * usuarios correspondientes.
     * 
     * @param session
     * @param authorizationType
     * @param pk
     * @param reset determina si los pasos previos de autorización tienen que borrarse
     * 
     * @throws Exception 
     */
    public static void processAuthorizations(SGuiSession session, final int authorizationType, final Object pk, final boolean reset) throws Exception {
        if (reset) {
            SAuthorizationUtils.deleteStepsOfAuthorization(session, authorizationType, pk);
        }
        
        // Si el recurso ya tiene pasos de autorización no se determinan nuevamente
        if (!reset && SAuthorizationUtils.hasStepsOfAuthorization(session, authorizationType, pk)) {
            return;
        }
        
        // Lectura de path de configuración
        ArrayList<SDbAuthorizationPath> lCfgs = SAuthorizationUtils.getConfigurationsOfType(session, authorizationType);
        String condPk = "";
        ArrayList<SDbAuthorizationStep> lSteps = new ArrayList<>();
        switch(authorizationType) {
            case AUTH_TYPE_MAT_REQUEST:
                condPk = "id_mat_req = " + ((int[]) pk)[0];
                for (SDbAuthorizationPath oCfg : lCfgs) {
                    // Si la configuración es basada en el JSON del registro
                    if (oCfg.getConfigurationJson() != null && oCfg.getConfigurationJson().length() > 0) {
                        // Determinar si la configuración aplica para el registro actual
                        /**
                         * {
                         *       "conditions" : [
                         *               {
                         *                       "keyName" : "ConsumeEntity",
                         *                       "operator" : "=",
                         *                       "strValue" : "2"
                         *               },
                         *               {
                         *                       "keyName" : "MatReqUser",
                         *                       "operator" : "=",
                         *                       "strValue" : "6"
                         *               }
                         *       ]
                         *  }
                         * 
                         * Este es un JSON de ejemplo, se pueden agregar n condiciones y cada una deberá cumplirse para que la configuración se considere
                         * como que aplica
                         */
                        String res = SMatRequestAuthorizationUtils.applyCfg(session, ((int[]) pk)[0], oCfg);
                        if (res.isEmpty()) {
                            // Agregar los pasos de autorización generados por la ruta
                            lSteps.addAll(SAuthorizationUtils.createStepFromCfg(session.getDatabase().getConnection(), oCfg, pk, 0));
                        }
                    }
                    // Si está basada en la condición de la tabla
                    else if (SAuthorizationUtils.applyCfg(session, oCfg, condPk)) {
                        // Crear renglón de autorización
                        lSteps.addAll(SAuthorizationUtils.createStepFromCfg(session.getDatabase().getConnection(), oCfg, pk, 0));
                    }
                    
                    for (SDbAuthorizationStep oStep : lSteps) {
                        if (! SAuthorizationUtils.stepExists(session, oStep)) {
                            oStep.save(session);
                        }
                    }
                }
                break;
            case AUTH_TYPE_DPS:
                condPk = "id_year = " + ((int[]) pk)[0] + " AND id_doc = " + ((int[]) pk)[1] + " ";
                for (SDbAuthorizationPath oCfg : lCfgs) {
                    if (SAuthorizationUtils.applyCfg(session, oCfg, condPk)) {
                        // Crear renglones de autorización
                        lSteps.addAll(SAuthorizationUtils.createStepFromCfg(session.getDatabase().getConnection(), oCfg, pk, 0));
                    }
                    
                    for (SDbAuthorizationStep oStep : lSteps) {
                        if (! SAuthorizationUtils.stepExists(session, oStep)) {
                            oStep.save(session);
                        }
                    }
                }
                break;
        }
    }
    
    /**
     * Procesa la configuración especificamente de un DPS y guarda en la base de datos la ruta de autorizaciones con los
     * usuarios correspondientes.
     * 
     * @param session
     * @param paths recibe ya las rutas de autorizacón
     * @param priority
     * @param authorizationType
     * @param pkDps
     * @param reset determina si los pasos previos de autorización tienen que borrarse
     * 
     * @throws Exception 
     */
    public static void processAuthorizationsDps(SGuiSession session, final ArrayList<SDbAuthorizationPath> paths, final int priority, final int authorizationType, final Object pkDps, final boolean reset) throws Exception {
        if (reset) {
            SAuthorizationUtils.deleteStepsOfAuthorization(session, authorizationType, pkDps);
        }
        
        // Si el recurso ya tiene pasos de autorización no se determinan nuevamente
        if (!reset && SAuthorizationUtils.hasStepsOfAuthorization(session, authorizationType, pkDps)) {
            return;
        }
        
        // Lectura de path de configuración
        ArrayList<SDbAuthorizationStep> lSteps = new ArrayList<>();
        for (SDbAuthorizationPath oCfg : paths) {
            lSteps.addAll(SAuthorizationUtils.createStepFromCfg(session.getDatabase().getConnection(), oCfg, pkDps, priority));
            
            for (SDbAuthorizationStep oStep : lSteps) {
                if (! SAuthorizationUtils.stepExists(session, oStep)) {
                    oStep.save(session);
                }
            }
        }
        
        processAuthornMails(session, authorizationType, (int[])pkDps, "");
        // Enviar notificaciones push
        sendNotificationsOnSend(session, authorizationType, (int[])pkDps);
    }
    
    /**
     * Devuelve todas las configuraciones asociadas a un tipo de autorización.
     * 
     * @param session
     * @param authorizationType
     * 
     * @return 
     */
    private static ArrayList<SDbAuthorizationPath> getConfigurationsOfType(SGuiSession session, final int authorizationType) {
        String sql = "SELECT id_authorn_path "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_PATH) + " "
                + "WHERE NOT b_del AND fk_tp_authorn = " + authorizationType + " "
                + "AND (('" + SLibUtils.DbmsDateFormatDate.format(session.getCurrentDate()) + "' BETWEEN dt_sta AND dt_end_n) OR "
                + "(dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(session.getCurrentDate()) + "' AND dt_end_n IS NULL)) "
                + "ORDER BY lev ASC;";
        
        try {
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            
            ArrayList<SDbAuthorizationPath> lCfgs = new ArrayList<>();
            SDbAuthorizationPath cfgItem;
            while(res.next()) {
                cfgItem = new SDbAuthorizationPath();
                cfgItem.read(session, new int[]{ res.getInt("id_authorn_path") });
                lCfgs.add(cfgItem);
            }
            
            return lCfgs;
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Devuelve todas las configuraciones asociadas a un tipo de autorización y DPS.
     * 
     * @param session
     * @param dpsType
     * 
     * @return 
     */
    public static ArrayList<SDbAuthorizationPath> getConfigurationsDps(SGuiSession session, final HashMap<String, Integer> dpsType) {
        String sql = "SELECT id_authorn_path "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_PATH) + " "
                + "WHERE NOT b_del AND fk_tp_authorn = 2 "
                + "AND (('" + SLibUtils.DbmsDateFormatDate.format(session.getCurrentDate()) + "' BETWEEN dt_sta AND dt_end_n) OR "
                + "(dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(session.getCurrentDate()) + "' AND dt_end_n IS NULL)) "
                + "ORDER BY sort, name, lev;";
        
        try {
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            ArrayList<SDbAuthorizationPath> lCfgs = new ArrayList<>();
            SDbAuthorizationPath cfgItem;
            while(res.next()) {
                boolean add = true;
                cfgItem = new SDbAuthorizationPath();
                cfgItem.read(session, new int[]{ res.getInt("id_authorn_path") });
                forConditions:
                for (SCondition cond : cfgItem.getAuthorizationConfigObject().getConditions()) {
                    switch (cond.getOperator()) {
                        case "=" :
                            int dpsTypeCond = dpsType.get(cond.getKeyName()) == null ? 0 : dpsType.get(cond.getKeyName());
                            if (SLibUtils.parseInt(cond.getStrValue()) != dpsTypeCond) {
                                add = false;
                                break forConditions;
                            }
                        break;
                        default: break forConditions;
                    }
                }
                if (add) {
                    lCfgs.add(cfgItem);
                }
            }
            
            return lCfgs;
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Determina si la configuración actual aplica al recurso recibido.
     * 
     * @param session
     * @param oCfg
     * @param condPk
     * 
     * @return 
     */
    private static boolean applyCfg(SGuiSession session, SDbAuthorizationPath oCfg, final String condPk) {
        if (oCfg.getCondictionTableName() == null) {
            return true;
        }
        if (oCfg.getConditionField() == null) {
            return false;
        }
        if (oCfg.getConditionOperator() == null) {
            return false;
        }
        if (oCfg.getConditionValue() == null) {
            return false;
        }
        
        String query = "SELECT '1' AS apply_cfg FROM " + oCfg.getCondictionTableName() + " WHERE " + condPk + " AND ";
        query += oCfg.getConditionField();
        
        switch(oCfg.getConditionOperator()) {
            case "=":
                query += " = ";
                break;
            case "<>":
                query += " <> ";
                break;           
            case ">":
                query += " > ";
                break;
            case ">=":
                query += " >= ";
                break;
            case "<":
                query += " < ";
                break;
            case "<=":
                query += " <= ";
                break;
                
            default:
                return false;
        }
        
        query += "'" + oCfg.getConditionValue() + "'";
        
        try {
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(query);
            
            return res.next();
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * Crea un registro SDbAuthorizationStep a partir de SDbAuthorizationPath aplicando la llave primaria.
     * 
     * @param oCfg
     * @param pk
     * 
     * @return 
     */
    private static ArrayList<SDbAuthorizationStep> createStepFromCfg(Connection connection, SDbAuthorizationPath oCfg, final Object pk, final int priority) {
        ArrayList<SDbAuthorizationStep> lSteps = new ArrayList<>();
        boolean bNotifications = false;
        ArrayList<Integer> lUsers = SAuthorizationUtils.getUsersOfAutorizationNode(connection, oCfg.getFkNodeAuthorizationId(), bNotifications);
        int nGroup = 0;
        if (oCfg.isNodeAll() || lUsers.size() > 1) {
            nGroup = SAuthorizationUtils.getNewAuthorizationGrouper(connection);
        }
        
        for (Integer userId : lUsers) {
            SDbAuthorizationStep oStep = new SDbAuthorizationStep();

            oStep.setResourcePkNum1_n(0);
            oStep.setResourcePkNum2_n(0);
            oStep.setResourcePkNum3_n(0);
            oStep.setResourcePkNum4_n(0);
            oStep.setResourcePkNum5_n(0);

            switch(oCfg.getFkAuthorizationTypeId()) {
                case AUTH_TYPE_MAT_REQUEST:
                    oStep.setResourceTableName_n("trn_mat_req");
                    oStep.setResourcePkNum1_n(((int[]) pk)[0]);
                    oStep.setResourcePkLength(1);
                    break;
                case AUTH_TYPE_DPS:
                    oStep.setResourceTableName_n("trn_dps");
                    oStep.setResourcePkNum1_n(((int[]) pk)[0]);
                    oStep.setResourcePkNum2_n(((int[]) pk)[1]);
                    oStep.setResourcePkLength(2);
                    break;
                default:
                    return null;
            }

            oStep.setUserLevel(oCfg.getUserLevel());
            oStep.setDateTimeAuthorized_n(null);
            oStep.setDateTimeRejected_n(null);
            oStep.setComments("");
            oStep.setAuthorizationGrouper_n(nGroup);
            oStep.setUserAuthorizationsNode_n(oCfg.getNodeAuthorizationUsers());
            oStep.setPriority(priority);
            oStep.setAllUsers(oCfg.isNodeAll());
            oStep.setAuthorized(false);
            oStep.setRejected(false);
            oStep.setRequired(oCfg.isRequired());
            oStep.setDeleted(false);
            oStep.setSystem(false);
            oStep.setFkAuthorizationTypeId(oCfg.getFkAuthorizationTypeId());
            oStep.setFkAuthorizationPathId_n(oCfg.getPkAuthorizationPathId());
            oStep.setFkUserStepId(userId);
            oStep.setFkNodeStepId_n(oCfg.getFkNodeAuthorizationId());
            oStep.setFkUserAuthorizationId_n(0);
            oStep.setFkUserRejectId_n(0);

            lSteps.add(oStep);
        }
        
        return lSteps;
    }
    
    /**
     * Determina si el paso de autorización existe o no.
     * Compara la llave primaria, el tipo de autorización, el usuario, el nivel y la tabla
     * correspondiente.
     * 
     * @param session
     * @param oStep
     * 
     * @return 
     */
    private static boolean stepExists(SGuiSession session, SDbAuthorizationStep oStep) {
        String sql = "SELECT '1' AS cfg_exists "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS cas "
                + "WHERE NOT b_del AND "
                    + "fk_tp_authorn = " + oStep.getFkAuthorizationTypeId() + " AND "
                    + "fk_usr_step = " + oStep.getFkUserStepId() + " AND "
                    + "cas.lev = " + oStep.getUserLevel() + " AND "
                    + "cas.res_tab_name_n " + (oStep.getResourceTableName_n() == null ? "IS NULL" : " = '" + oStep.getResourceTableName_n() + "'") + " AND "
                    + "cas.res_pk_n1_n " + (oStep.getResourcePkNum1_n() == 0 ? "IS NULL" : " = " + oStep.getResourcePkNum1_n() + "") + " AND "
                    + "cas.res_pk_n2_n " + (oStep.getResourcePkNum2_n() == 0 ? "IS NULL" : " = " + oStep.getResourcePkNum2_n() + "") + " AND "
                    + "cas.res_pk_n3_n " + (oStep.getResourcePkNum3_n() == 0 ? "IS NULL" : " = " + oStep.getResourcePkNum3_n() + "") + " AND "
                    + "cas.res_pk_n4_n " + (oStep.getResourcePkNum4_n() == 0 ? "IS NULL" : " = " + oStep.getResourcePkNum4_n() + "") + " AND "
                    + "cas.res_pk_n5_n " + (oStep.getResourcePkNum5_n() == 0 ? "IS NULL" : " = " + oStep.getResourcePkNum5_n() + "") + ";";
        
        try {
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            
            return res.next();
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    /**
     * Obtiene los pasos de autorización correspondientes a un recurso.
     * Este método devuelve todos los pasos de autorización no eliminados, sean requeridos o no.
     * 
     * @param session
     * @param authorizationType
     * @param pk
     * 
     * @return 
     */
    public static ArrayList<SDbAuthorizationStep> getResourceAuthSteps(SGuiSession session, final int authorizationType, final Object pk) {
        String condPk = "";
        switch(authorizationType) {
            case AUTH_TYPE_MAT_REQUEST:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " ";
                break;
                
            case AUTH_TYPE_DPS:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " AND res_pk_n2_n = " + ((int[]) pk)[1] + " ";
                break;
        }
        
        String sql = "SELECT id_authorn_step "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " "
                + "WHERE NOT b_del AND "
                    + "fk_tp_authorn = " + authorizationType + " AND "
                    + condPk + " ORDER BY lev ASC;";
        
        ArrayList<SDbAuthorizationStep> lSteps = new ArrayList<>();
        try {
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            SDbAuthorizationStep oStep;
            while (res.next()) {
                oStep = new SDbAuthorizationStep();
                oStep.read(session, new int[] { res.getInt("id_authorn_step") });
                lSteps.add(oStep);
            }
            
            return lSteps;
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Obtiene los ID de los usuarios asignados al nodo de autorización recibido.
     * Determina usuarios asignados directamente a un nodo o usuarios asignados indirectamente
     * mediante un puesto.
     * 
     * @param connection
     * @param idNode
     * @param bNotification
     * 
     * @return 
     */
    public static ArrayList<Integer> getUsersOfAutorizationNode(Connection connection, final int idNode, boolean bNotification) {
        String sql = "SELECT canu.id_authorn_user AS id_user "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_NODE_USR) + " AS canu "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " uu ON canu.id_authorn_user = uu.id_usr "
                + "WHERE canu.id_authorn_node = " + idNode + " AND NOT canu.b_del AND NOT uu.b_del AND uu.b_act "
                + (bNotification ? "AND canu.b_email_notifs " : "")
                + "UNION "
                + "SELECT  usr.id_usr AS id_user "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_NODE_POS) + " AS apos "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_POS) + " AS pos ON apos.id_authorn_pos = pos.id_pos "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON pos.id_pos = emp.fk_pos "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS usr ON emp.id_emp = usr.fid_bp_n "
                + "WHERE "
                + "NOT apos.b_del AND NOT emp.b_del "
                + (bNotification ? "AND apos.b_email_notifs " : "")
                + "AND NOT usr.b_del "
                + "AND usr.b_act "
                + "AND apos.id_authorn_node = " + idNode + ";";
        
        ArrayList<Integer> lUsers = new ArrayList<>();
        try {
            ResultSet res = connection.createStatement().executeQuery(sql);
            while (res.next()) {
                lUsers.add(res.getInt("id_user"));
            }
            
            return lUsers;
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Obtiene los ID de los puestos asignados al nodo de autorización recibido.
     * 
     * @param connection
     * @param idNode
     * 
     * @return 
     */
    public static ArrayList<Integer> getJobPositionsOfAutorizationNode(Connection connection, final int idNode) {
        String sql = "SELECT canu.id_authorn_pos "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_NODE_POS) + " AS canp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_POS) + " pos ON canp.id_authorn_pos = pos.id_pos "
                + "WHERE canp.id_authorn_node = " + idNode + " AND NOT canp.b_del AND NOT pos.b_del;";
        
        ArrayList<Integer> lPositions = new ArrayList<>();
        try {
            ResultSet res = connection.createStatement().executeQuery(sql);
            while (res.next()) {
                lPositions.add(res.getInt("id_authorn_pos"));
            }
            
            return lPositions;
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Obtiene el ID del puesto correspodiente al usuario recibido.
     * Determina el puesto mediante el fk de asociado de negocio del usuario, en caso de no tenerlo retorna 0.
     * 
     * @param connection
     * @param idUser
     * @return 
     */
    public static int getPositionOfUser(Connection connection, final int idUser) {
        String sql = "SELECT emp.fk_pos " +
                        "FROM " +
                        SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS usr " +
                        "        INNER JOIN " +
                        SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON usr.fid_bp_n = emp.id_emp " +
                        "WHERE " +
                        "usr.id_usr = " + idUser + ";";
        
        int idPosition = 0;
        try {
            ResultSet res = connection.createStatement().executeQuery(sql);
            if (res.next()) {
                idPosition = res.getInt("fk_pos");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return idPosition;
    }
    
    /**
     * Obtiene un nuevo agrupador para los pasos que pertenezcan a un mismo nodo de autorización
     * 
     * @param connection
     * @return 
     */
    public static int getNewAuthorizationGrouper(Connection connection) {
        String sql = "SELECT "
                + "    COALESCE(MAX(authorn_grouper_n), 0) + 1 AS authorn_grouper "
                + "FROM "
                + " " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + ";";
        
        try {
            ResultSet res = connection.createStatement().executeQuery(sql);
            if (res.next()) {
                return res.getInt("authorn_grouper");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }
    
    /**
     * Obtiene los id de los steps correspondientes al grouper recibido
     * 
     * @param connection
     * @param grouper
     * @return 
     */
    private static ArrayList<Integer> getStepsOfAuthorizationGrouper(Connection connection, final int grouper) {
        String sql = "SELECT id_authorn_step "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " "
                + "WHERE authorn_grouper_n = " + grouper + " AND NOT b_del;";
        
        ArrayList<Integer> lStepIds = new ArrayList<>();
        try {
            ResultSet res = connection.createStatement().executeQuery(sql);
            while (res.next()) {
                lStepIds.add(res.getInt("id_authorn_step"));
            }
            
            return lStepIds;
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Obtiene los id de los steps AUTORIZADOS correspondientes al grouper recibido
     * 
     * @param connection
     * @param grouper
     * @return 
     */
    private static ArrayList<Integer> getStepsAuthorizedOfAuthorizationGrouper(Connection connection, final int grouper) {
        String sql = "SELECT id_authorn_step "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " "
                + "WHERE authorn_grouper_n = " + grouper + " AND NOT b_del AND b_authorn;";
        
        ArrayList<Integer> lStepIds = new ArrayList<>();
        try {
            ResultSet res = connection.createStatement().executeQuery(sql);
            while (res.next()) {
                lStepIds.add(res.getInt("id_authorn_step"));
            }
            
            return lStepIds;
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Obtiene los id de los steps PENDIENTES correspondientes al grouper recibido
     * 
     * @param connection
     * @param grouper
     * @return 
     */
    private static ArrayList<Integer> getStepsPendingOfAuthorizationGrouper(Connection connection, final int grouper) {
        String sql = "SELECT id_authorn_step "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " "
                + "WHERE authorn_grouper_n = " + grouper + " AND NOT b_del AND NOT b_authorn AND NOT b_reject;";
        
        ArrayList<Integer> lStepIds = new ArrayList<>();
        try {
            ResultSet res = connection.createStatement().executeQuery(sql);
            while (res.next()) {
                lStepIds.add(res.getInt("id_authorn_step"));
            }
            
            return lStepIds;
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Determina si los steps del grouper están rechazados.
     * Con un solo paso rechazado esta función devuelve true
     * 
     * @param connection
     * @param grouper
     * @return 
     */
    private static boolean isRejectOfAuthorizationGrouper(Connection connection, final int grouper) {
        String sql = "SELECT id_authorn_step "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " "
                + "WHERE authorn_grouper_n = " + grouper + " AND NOT b_del AND b_reject;";
        
        try {
            ResultSet res = connection.createStatement().executeQuery(sql);
            return res.next();
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * Proceso después de la autorización de un paso
     * 
     * @param session
     * @param oStep
     * @throws Exception 
     */
    private static void doAfterAuthorization(SGuiSession session, SDbAuthorizationStep oStep) throws Exception {
        /**
         * Si todos los usuarios deben autorizar, no hace nada
         */
        if (oStep.isAllUsers()) {
            return;
        }
        /**
         * Si el step no pertenece a un grupo, no hace nada
         */
        if (oStep.getAuthorizationGrouper_n() == 0) {
            return;
        }
        /**
         * Si un step del grupo está rechazado, no hace nada
         */
        if (SAuthorizationUtils.isRejectOfAuthorizationGrouper(session.getDatabase().getConnection(), oStep.getAuthorizationGrouper_n())) {
            return;
        }
        ArrayList<Integer> lStepIds = SAuthorizationUtils.getStepsOfAuthorizationGrouper(session.getDatabase().getConnection(), oStep.getAuthorizationGrouper_n());
        int nStepsInGrouper = lStepIds.size();
        /**
         * Si solo hay un step en el grupo, no hace nada
         */
        if (nStepsInGrouper == 1) {
            return;
        }
        /**
         * Si ya no hay ningún step pendiente de autorizar, no hace nada
         */
        ArrayList<Integer> lPendingStepIds = SAuthorizationUtils.getStepsPendingOfAuthorizationGrouper(session.getDatabase().getConnection(), oStep.getAuthorizationGrouper_n());
        if (lPendingStepIds.isEmpty()) {
            return;
        }
        /**
         * Autorizar por default los steps pendientes si la configuración no los requiere todos
         */
        if (oStep.getUserAuthorizationsNode_n() == 1 || ((nStepsInGrouper - lPendingStepIds.size()) == oStep.getUserAuthorizationsNode_n())) {
            SDbAuthorizationStep oStepAux;
            for (Integer nAuthStepId : lPendingStepIds) {
                oStepAux = new SDbAuthorizationStep();
                oStepAux.read(session, new int[] { nAuthStepId });
                oStepAux.setDateTimeAuthorized_n(null);
                oStepAux.setFkUserAuthorizationId_n(oStep.getFkUserAuthorizationId_n());
                oStepAux.setAuthorized(true);
                oStepAux.setDateTimeRejected_n(null);
                oStepAux.setFkUserRejectId_n(0);
                oStepAux.setRejected(false);
                oStepAux.setComments("Autorizado por default");
                
                oStepAux.save(session);
            }
        }
    }
    
    private static boolean hasStepsOfAuthorization(SGuiSession session, final int authorizationType, final Object pk) {
         String condPk = "";
        switch(authorizationType) {
            case AUTH_TYPE_MAT_REQUEST:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " ";
                break;
                
            case AUTH_TYPE_DPS:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " AND res_pk_n2_n = " + ((int[]) pk)[1] + " ";
                break;
        }
        
        String sql = "SELECT id_authorn_step "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " "
                + "WHERE NOT b_del AND "
                    + "fk_tp_authorn = " + authorizationType + " AND "
                    + condPk + ";";
        
        ResultSet res;
        try {
            res = session.getStatement().getConnection().createStatement().executeQuery(sql);
            
            return res.next();
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public static void deleteStepsOfAuthorization(SGuiSession session, final int authorizationType, final Object pk) {
         String condPk = "";
        switch(authorizationType) {
            case AUTH_TYPE_MAT_REQUEST:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " ";
                break;
                
            case AUTH_TYPE_DPS:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " AND res_pk_n2_n = " + ((int[]) pk)[1] + " ";
                break;
        }
        
        String sql = "SELECT id_authorn_step "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " "
                + "WHERE NOT b_del AND "
                    + "fk_tp_authorn = " + authorizationType + " AND "
                    + condPk + ";";
        
        ResultSet res;
        try {
            res = session.getStatement().getConnection().createStatement().executeQuery(sql);
            String ids = "";
            while (res.next()) {
                ids += res.getInt("id_authorn_step") + ",";
            }
            
            if (! ids.isEmpty()) {
                ids = ids.substring(0, ids.length() - 1);
                sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " SET b_del = true "
                        + "WHERE id_authorn_step IN (" + ids + ");";

                session.getStatement().getConnection().createStatement().executeUpdate(sql);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean sendAuthornAppWeb(SClientInterface client, int[] pk) throws Exception {
        SDbSupplierFileProcess fileProcess = new SDbSupplierFileProcess();
        fileProcess.read(client.getSession(), pk);
        if (fileProcess.getDps().getFkDpsAuthorizationStatusId() == SDataConstantsSys.TRNS_ST_DPS_AUTHORN_NA) {
            if (canSendAuthornAppWeb(client, fileProcess)) {
                SDialogSelectAuthornPath dialog = new SDialogSelectAuthornPath((SGuiClient) client);
                dialog.setVisible(true);
                if (dialog.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                    new SProcDpsSendAuthornWeb(client, fileProcess, dialog.getSelectedAuthPaths(), dialog.getSelectedPriority(), dialog.getAuthornNotes()).start();
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            client.showMsgBoxWarning("No se puede enviar el documento a autorizar debido a que su estatus es " + fileProcess.getDpsStatus());
            return false;
        }
        return true;
    }
    
    public static boolean canSendAuthornAppWeb (SClientInterface client, SDbSupplierFileProcess fileProcess) throws Exception {
        boolean send = true;
        fileProcess.readMaterialRequests(client.getSession());
        if (fileProcess.getSuppFiles().isEmpty()) {
            if (client.showMsgBoxConfirm("El documento no tiene archivos de soporte anexados.\n¿Desea enviar a autorización web de todas formas?") != JOptionPane.OK_OPTION) {
                send = false;
            }
        }
        if (send && fileProcess.getMaterialRequests().isEmpty()) {
            if (client.showMsgBoxConfirm("El documento no tiene requisiciones de materiales asociadas.\n¿Desea enviar a autorización web de todas formas?") != JOptionPane.OK_OPTION) {
                send = false;
            }                        
        }
        int entriesWithoutReq = fileProcess.getDpsEntriesWithoutMaterialRequest(client.getSession());
        if (send && !fileProcess.getMaterialRequests().isEmpty() && entriesWithoutReq > 0) {
            if (client.showMsgBoxConfirm("El documento tiene " + entriesWithoutReq + " partida" + (entriesWithoutReq > 1 ? "s" : "") + " que no estan asociadas a una requisicion de materiales.\n¿Desea enviar a autorización web de todas formas?") != JOptionPane.OK_OPTION) {
                send = false;
            }                                                
        }
        
        return send;
    }
    
    /**
    * Envia una notificación mail cuando al recurso aún le faltan pasos por autorizar.
    * 
    * @param session
    * @param authorizationType
    * @param pkDps
     * @param comments
    */
    public static void processAuthornMails(final SGuiSession session, final int authorizationType, int[] pkDps, String comments) {
        ArrayList<Integer> lUsers;
        try {
            boolean toNotification = true;
            lUsers = SAuthorizationUtils.getUsersInTurnAuth(session.getStatement().getConnection().createStatement(), authorizationType, pkDps, toNotification);
            if (! lUsers.isEmpty()) {
                HashMap<String, String> lMails = SAuthorizationUtils.getMailsOfUsers(session.getStatement().getConnection().createStatement(), lUsers);
                if (! lMails.isEmpty()) {
                    for (Map.Entry<String, String> oRow : lMails.entrySet()) {
                        String userName = oRow.getKey();
                        String userMail = oRow.getValue();
                        
                        SAuthorizationUtils.sendAuthornMails(session, AUTH_MAIL_AUTH_PEND, userMail, "", "", pkDps, userName, comments);
                    }
                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void sendAuthornMails(final sa.lib.gui.SGuiSession session, int mailType, String to, String cc,
            String bcc, int[] pkDps, final String actionUserName, final String comment) throws Exception {
        SDataDps dps = new SDataDps();
        dps.read(pkDps, session.getStatement().getConnection().createStatement());
        SDbMms mms = STrnUtilities.getMms(session, SModSysConsts.CFGS_TP_MMS_DPS_ORD_PUR_AUTH_APP);

        ArrayList<String> toRecipients = new ArrayList<>(Arrays.asList(SLibUtils.textExplode(to.toLowerCase(), ";")));
        ArrayList<String> toCcRecipients = cc.isEmpty() ? new ArrayList<>()
                : new ArrayList<>(Arrays.asList(SLibUtils.textExplode(cc.toLowerCase(), ";")));
        ArrayList<String> toBccRecipients = bcc.isEmpty() ? new ArrayList<>()
                : new ArrayList<>(Arrays.asList(SLibUtils.textExplode(bcc.toLowerCase(), ";")));

        String body = "<html>";
        body += "<head>";
        body += "<style>"
                + "body {"
                + " font-size: 100%;"
                + "} "
                + "h1 {"
                + " font-size: 2.00em;"
                + " font-family: sans-serif;"
                + "} "
                + "h2 {"
                + " font-size: 1.75em;"
                + " font-family: sans-serif;"
                + "} "
                + "h3 {"
                + " font-size: 1.50em;"
                + " font-family: sans-serif;"
                + "} "
                + "h4 {"
                + " font-size: 1.25em;"
                + " font-family: sans-serif;"
                + "} "
                + "</style>";
        body += "</head>";
        body += "<body>";
        body += "<h2>" + SLibUtils.textToHtml("¡Hola!") + "</h2>";
        String subject = "";
        switch (mailType) {
            case AUTH_MAIL_AUTH_PEND:
                subject = "[SIIE] Autorizacion pendiente de orden de compra "
                        + dps.getNumber();
                body += "<p>" + SLibUtils.textToHtml("Tienes una orden de compra por autorizar") + "</p>";
                body += "<b>Folio: " + dps.getNumber() + "</b>";
                if (comment != null && !comment.isEmpty()) {
                    body += "<p><b>Comentarios:</b> " + SLibUtils.textToHtml(comment) + "</p>";
                }
                body += "<p>Ingresa " + SLibUtils.textToHtml("aquí")
                        + " al <a href=\"https://aeth.siieapp.com/portal-autorizaciones/public/dps\">Portal de autorizaciones</a> para revisar, y, en su caso, autorizar esta orden.</p>";
                break;
            case AUTH_MAIL_AUTH_DONE:
                subject = "[SIIE] OC " + dps.getNumber() + " autorizada";
                body += "<p>La siguiente orden de compra ha sido <span style='color: green;'>AUTORIZADA</span> por: <b>" + actionUserName + "</b></p>";
                body += "<b>Folio: " + dps.getNumber() + "</b>";
                if (comment != null && !comment.isEmpty()) {
                    body += "<p><b>Comentarios:</b> " + SLibUtils.textToHtml(comment) + "</p>";
                }
                body += "<p>Ingresa " + SLibUtils.textToHtml("aquí")
                        + " al <a href=\"https://aeth.siieapp.com/portal-autorizaciones/public/dps\">Portal de autorizaciones</a></p>";
                if (!mms.getRecipientTo().isEmpty()) {
                    toRecipients
                            .addAll(new ArrayList<>(Arrays.asList(SLibUtils.textExplode(mms.getRecipientTo(), ";"))));
                }
                break;
            case AUTH_MAIL_AUTH_REJ:
                subject = "[SIIE] OC " + dps.getNumber() + " rechazada";
                body += "<p>La siguiente orden de compra ha sido <span style='color: red;'>RECHAZADA</span> por: <b>" + actionUserName + "</b></p>";
                body += "<b>Folio: " + dps.getNumber() + "</b>";
                if (comment != null && !comment.isEmpty()) {
                    body += "<p><b>Comentarios:</b> " + SLibUtils.textToHtml(comment) + "</p>";
                }
                body += "<p>Ingresa " + SLibUtils.textToHtml("aquí")
                        + " al <a href=\"https://aeth.siieapp.com/portal-autorizaciones/public/dps\">Portal de autorizaciones</a></p>";
                if (!mms.getRecipientTo().isEmpty()) {
                    toRecipients
                            .addAll(new ArrayList<>(Arrays.asList(SLibUtils.textExplode(mms.getRecipientTo(), ";"))));
                }
                break;
        }

        body += "<p>" + SLibUtils.textToHtml("También") + " puedes accesar al portal introduciendo esta "
                + SLibUtils.textToHtml("dirección") + " en tu navegador:</p>";
        body += "<a href=\"https://aeth.siieapp.com/portal-autorizaciones/public/dps\">https://aeth.siieapp.com/portal-autorizaciones/public/dps</a>";
        body += "<p>Recuerda: tu usuario y " + SLibUtils.textToHtml("contraseña")
                + " del portal son los mismos que tu cuenta SIIE.</p>";
        body += "<span style='font-size:10px'>" + STrnUtilities.composeMailFooter("") + "</span>";

        body += "</body>";

        body += "</html>";

        SMailSender moMailSender = new SMailSender(mms.getHost(), mms.getPort(), mms.getProtocol(), mms.isStartTls(),
                mms.isAuth(), mms.getUser(), mms.getUserPassword(), mms.getUser());
        moMailSender.setMailReplyTo(mms.getXtaMailReplyTo());

        if (!mms.getRecipientCarbonCopy().isEmpty()) {
            toCcRecipients
                    .addAll(new ArrayList<>(Arrays.asList(SLibUtils.textExplode(mms.getRecipientCarbonCopy(), ";"))));
        }

        if (!mms.getRecipientBlindCarbonCopy().isEmpty()) {
            toBccRecipients.addAll(
                    new ArrayList<>(Arrays.asList(SLibUtils.textExplode(mms.getRecipientBlindCarbonCopy(), ";"))));
        }

        ArrayList<String> toRecipientsCleaned = new ArrayList<>();
        if (toRecipients.isEmpty()) {
            throw new Exception("No hay destinatarios configurados para la notificación de correo");
        }
        else {
            for (String rec : toRecipients) {
                if (rec != null && !rec.isEmpty() && isValidEmailAddress(rec)) {
                    toRecipientsCleaned.add(rec);
                }
            }
        }
        
        Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.INFO, "Enviando mail a: {0}", toRecipientsCleaned.toString());
        SMail mail = new SMail(moMailSender, subject, body, toRecipientsCleaned, toCcRecipients, toBccRecipients);
        mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
        mail.send();
    }
    
    public static void sendNotificationsOnSend(final SGuiSession session, final int authorizationType, int[] pkDps) {
        ArrayList<Integer> lUsers;
        try {
            boolean toNotification = false;
            lUsers = SAuthorizationUtils.getUsersInTurnAuth(session.getStatement().getConnection().createStatement(), authorizationType, pkDps, toNotification);
            if (! lUsers.isEmpty()) {
                SDataDps dps = new SDataDps();
                dps.read(pkDps, session.getStatement().getConnection().createStatement());

                for (Integer idUser : lUsers) {
                    SNotificationsUtils.sendPushNotification(session, idUser, dps.getNumber(), SNotificationsUtils.PUSH_NOTIFICATION_NEW, 0);
                    
                    ArrayList<SUserResource> lResources = SNotificationsUtils.getResourcesPendingCounter(session.getStatement().getConnection().createStatement(), authorizationType, idUser);
                    for (SUserResource oRes : lResources) {
                        if (oRes.getCounter() > 1) {
                            SNotificationsUtils.sendPushNotification(session, idUser, "", SNotificationsUtils.PUSH_NOTIFICATION_BADGE, oRes.getCounter());
                        }
                    }
                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
    
    /**
     * Clase base de estatus de autorización
     */
    public static class SAuthStatus {
        private int status;
        private String user;

        public SAuthStatus() {
            this.status = 0;
            this.user = "";
        }
        
        public SAuthStatus(int status, String user) {
            this.status = status;
            this.user = user;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }
    }
}
