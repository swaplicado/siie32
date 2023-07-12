/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.utils;

import erp.mod.SModConsts;
import erp.mod.cfg.db.SDbAuthorizationPath;
import erp.mod.cfg.db.SDbAuthorizationStep;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 * Clase de utilería de autorizaciones
 * 
 * @author Edwin Carmona
 */
public class SAuthorizationUtils {
    
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
                String input = textArea.getText();
                System.out.println("Input: " + input);
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
     * Si el proceso ha sido exitoso devuelve un Strin vacío, si ha ocurrido un error el 
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
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            
            if (res.next()) {
                if (action == SAuthorizationUtils.AUTH_ACTION_AUTHORIZE) {
                    return SAuthorizationUtils.authorizeById(session, res.getInt("id_authorn_step"));
                }
                else {
                    return SAuthorizationUtils.rejectById(session, res.getInt("id_authorn_step"), reasonRej);
                }
            }
            else {
                return "No se encontró la autorización para el usuario recibido.";
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
     * 
     * @return 
     */
    public static String authorizeById(SGuiSession session, final int idAuthStep) {
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
                oStep.setComments("");
                
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
                while (resRows.next()) {
                    if (resRows.getBoolean("b_authorn")) {
                        lStatus.add(new SAuthStatus(AUTH_STATUS_AUTHORIZED, resRows.getString("auth_user")));
                    }
                }
                
                if (lStatus.size() > 0) {
                    String resp = "";
                    for (SAuthStatus oStatus : lStatus) {
                        if (oStatus.getStatus() == AUTH_STATUS_AUTHORIZED) {
                            resp += "No se puede rechazar, el usuario " + oStatus.getUser() + " ya autorizó este documento.\n";
                        }
                    }
                    
                    return resp;
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
     * 
     * @throws Exception 
     */
    public static void processAuthorizations(SGuiSession session, final int authorizationType, final Object pk) throws Exception {
        ArrayList<SDbAuthorizationPath> lCfgs = SAuthorizationUtils.getConfigurationsOfType(session, authorizationType);
        String condPk = "";
        ArrayList<SDbAuthorizationStep> lSteps = new ArrayList<>();
        switch(authorizationType) {
            case AUTH_TYPE_MAT_REQUEST:
                condPk = "id_mat_req = " + ((int[]) pk)[0];
                for (SDbAuthorizationPath oCfg : lCfgs) {
                    if (SAuthorizationUtils.applyCfg(session, oCfg, condPk)) {
                        // Crear renglón de autorización
                        lSteps.addAll(SAuthorizationUtils.createStepFromCfg(session.getDatabase().getConnection(), oCfg, pk));
                    }
                }
                break;
            case AUTH_TYPE_DPS:
                condPk = "id_year = " + ((int[]) pk)[0] + " AND id_doc = " + ((int[]) pk)[1] + " ";
                for (SDbAuthorizationPath oCfg : lCfgs) {
                    if (SAuthorizationUtils.applyCfg(session, oCfg, condPk)) {
                        // Crear renglones de autorización
                        lSteps.addAll(SAuthorizationUtils.createStepFromCfg(session.getDatabase().getConnection(), oCfg, pk));
                    }
                }
                break;
        }
        
        for (SDbAuthorizationStep oStep : lSteps) {
            if (! SAuthorizationUtils.stepExists(session, oStep)) {
                oStep.save(session);
            }
        }
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
        
        query += oCfg.getConditionValue();
        
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
    private static ArrayList<SDbAuthorizationStep> createStepFromCfg(Connection connection, SDbAuthorizationPath oCfg, final Object pk) {
        ArrayList<SDbAuthorizationStep> lSteps = new ArrayList<>();
        ArrayList<Integer> lUsers = SAuthorizationUtils.getUsersOfAutorizationNode(connection, oCfg.getFkNodeAuthorizationId());
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
     * 
     * @param connection
     * @param idNode
     * 
     * @return 
     */
    public static ArrayList<Integer> getUsersOfAutorizationNode(Connection connection, final int idNode) {
        String sql = "SELECT canu.id_authorn_user "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_NODE_USR) + " AS canu "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " uu ON canu.id_authorn_user = uu.id_usr "
                + "WHERE canu.id_authorn_node = " + idNode + " AND NOT canu.b_del AND NOT uu.b_del AND uu.b_act;";
        
        ArrayList<Integer> lUsers = new ArrayList<>();
        try {
            ResultSet res = connection.createStatement().executeQuery(sql);
            while (res.next()) {
                lUsers.add(res.getInt("id_authorn_user"));
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
