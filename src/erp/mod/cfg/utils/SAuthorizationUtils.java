/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.utils;

import erp.mod.SModConsts;
import erp.mod.cfg.db.SDbAuthConfiguration;
import erp.mod.cfg.db.SDbAuthStep;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            "    " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTH_STEP) + " AS cas " +
            "        LEFT JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ua ON cas.fk_usr_auth_n = ua.id_usr " +
            "        LEFT JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON cas.fk_usr_rej_n = ur.id_usr " +
            "        LEFT JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS us ON cas.fk_usr_step = us.id_usr " +
            "WHERE " +
            "    NOT cas.b_del AND cas.b_req ";
    
    /**
     * Constates de estatus de autorización
     */
    public static final int AUTH_STATUS_AUTHORIZED = 1;
    public static final int AUTH_STATUS_REJECTED = 2;
    public static final int AUTH_STATUS_PENDING = 3;
    public static final int AUTH_STATUS_AUTHORIZING = 4;
    
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
        
        String sql = QUERY_AUTHS + " AND cas.fk_auth_type = " + authorizationType + " AND " + condPk;
        try {
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            
            while(res.next()) {
                if (! res.getBoolean("b_auth")) {
                    return false;
                }
            }
            
            return true;
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
     * @return puede ser: AUTH_STATUS_AUTHORIZED, AUTH_STATUS_REJECTED, AUTH_STATUS_PENDING, AUTH_STATUS_AUTHORIZING
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
                        "    " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTH_STEP) + " AS cas " +
                        "        LEFT JOIN " +
                        "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ua ON cas.fk_usr_auth_n = ua.id_usr " +
                        "        LEFT JOIN " +
                        "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON cas.fk_usr_rej_n = ur.id_usr " +
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
                return AUTH_STATUS_AUTHORIZED;
            }
            
            /*
            * Query RECHAZADOS
            **********************************************************************************************************
            */
            String queryRej = query + " AND cas.b_rej";
            
            ResultSet resRej = session.getDatabase().getConnection().createStatement().executeQuery(queryRej);
            
            int rejSteps = 0; 
            if (resRej.next()) {
                rejSteps = res.getInt("n_rows");
            }
            
            if (rejSteps > 0) {
                return AUTH_STATUS_REJECTED;
            }
            
            /*
            * Query AUTORIZADOS
            **********************************************************************************************************
            */
            String queryAuth = query + " AND cas.b_auth";
            
            ResultSet resAuth = session.getDatabase().getConnection().createStatement().executeQuery(queryAuth);
            
            int authSteps = 0; 
            if (resAuth.next()) {
                authSteps = res.getInt("n_rows");
            }
            
            if (authSteps == allSteps) {
                return AUTH_STATUS_AUTHORIZED;
            }
            
            /*
            * Query PENDIENTES
            **********************************************************************************************************
            */
            String queryPending = query + " AND NOT b_auth AND NOT b_rej";
            
            ResultSet resPending = session.getDatabase().getConnection().createStatement().executeQuery(queryPending);
            
            int pendingSteps = 0; 
            if (resPending.next()) {
                pendingSteps = res.getInt("n_rows");
            }
            
            if (pendingSteps == allSteps) {
                return AUTH_STATUS_PENDING;
            }
            
            /*
            * DEFAULT
            **********************************************************************************************************
            */
            return AUTH_STATUS_AUTHORIZING;
        }
        catch (SQLException ex) {
            Logger.getLogger(SAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return AUTH_STATUS_AUTHORIZING;
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
        String sql = QUERY_AUTHS + " AND cas.fk_auth_type = " + authorizationType + " AND " + condPk;
        try {
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            
            while(res.next()) {
                if (res.getBoolean("b_auth")) {
                    lStatus.add(new SAuthStatus(AUTH_STATUS_AUTHORIZED, res.getString("auth_user")));
                }
                else if (res.getBoolean("b_rej")) {
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
     * Petición de autorización o rechazo de recurso.
     * Si el proceso ha sido exitoso devuelve un Strin vacío, si ha ocurrido un error el 
     * String explicará lo que ha sucedido
     * 
     * @param session
     * @param action
     * @param authorizationType
     * @param pk
     * @param userId
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
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTH_STEP) + " "
                + "WHERE NOT b_del AND "
                    + "fk_auth_type = " + authorizationType + " AND "
                    + "fk_usr_step = " + userId + " AND "
                    + condPk + ";";
        
        try {
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            
            if (res.next()) {
                if (action == SAuthorizationUtils.AUTH_ACTION_AUTHORIZE) {
                    return SAuthorizationUtils.authorizeById(session, res.getInt("id_auth_step"));
                }
                else {
                    return SAuthorizationUtils.rejectById(session, res.getInt("id_auth_step"), reasonRej);
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
        SDbAuthStep oStep = new SDbAuthStep();
        try {
            oStep.read(session, new int[] { idAuthStep });
            
            if (oStep.isAuthorized()) {
                return "Este documento ya está autorizado.";
            }
            if (oStep.getFkUserStepId() != session.getUser().getPkUserId()) {
                return "No se puede autorizar este documento, usuario incorrecto.";
            }
            
            String rowsQuery = QUERY_AUTHS + " AND "
                        + "cas.fk_auth_type = " + oStep.getFkAuthTypeId() + " AND "
                        + "NOT cas.b_auth AND "
                        + "cas.lev <= " + oStep.getUserLevel() + " AND "
                        + "cas.res_tab_name_n " + (oStep.getResourceTableName_n() == null ? "IS NULL" : " = '" + oStep.getResourceTableName_n() + "'") + " AND "
                        + "cas.res_pk_n1_n " + (oStep.getResourcePkNum1_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum1_n() + "'") + " AND "
                        + "cas.res_pk_n2_n " + (oStep.getResourcePkNum2_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum2_n() + "'") + " AND "
                        + "cas.res_pk_n3_n " + (oStep.getResourcePkNum3_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum3_n() + "'") + " AND "
                        + "cas.res_pk_n4_n " + (oStep.getResourcePkNum4_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum4_n() + "'") + " AND "
                        + "cas.res_pk_n5_n " + (oStep.getResourcePkNum5_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum5_n() + "'") + " AND "
                        + "cas.res_pk_n6_n " + (oStep.getResourcePkNum6_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum6_n() + "'") + " AND "
                        + "cas.res_pk_n7_n " + (oStep.getResourcePkNum7_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum7_n() + "'") + " AND "
                        + "cas.res_pk_n8_n " + (oStep.getResourcePkNum8_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum8_n() + "'") + " AND "
                        + "cas.res_pk_n9_n " + (oStep.getResourcePkNum9_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum9_n() + "'") + " AND "
                        + "cas.res_pk_n10_n " + (oStep.getResourcePkNum10_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum10_n() + "'") + " AND "
                        + "cas.id_auth_step <> " + oStep.getPkStepId() + " "
                        + ";";
                
                ArrayList<SAuthStatus> lStatus = new ArrayList<>();
                ResultSet resRows = session.getDatabase().getConnection().createStatement().executeQuery(rowsQuery);
                while (resRows.next()) {
                    if (resRows.getBoolean("b_rej")) {
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
                oStep.setFkUserAuthId_n(session.getUser().getPkUserId());
                oStep.setAuthorized(true);
                oStep.setDateTimeRejected_n(null);
                oStep.setFkUserRejId_n(0);
                oStep.setRejected(false);
                oStep.setComments("");
                
                oStep.save(session);
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
     * 
     * @return 
     */
    public static String rejectById(SGuiSession session, final int idAuthStep, String reasonRej) {
        SDbAuthStep oStep = new SDbAuthStep();
        try {
            oStep.read(session, new int[] { idAuthStep });
            
            if (oStep.isRejected()) {
                return "Este documento ya está rechazado.";
            }
            if (oStep.getFkUserStepId() != session.getUser().getPkUserId()) {
                return "No se puede rechazar este documento, usuario incorrecto.";
            }
            
            String rowsQuery = QUERY_AUTHS + " AND "
                        + "cas.fk_auth_type = " + oStep.getFkAuthTypeId() + " AND "
                        + "cas.b_auth AND "
                        + "cas.lev > " + oStep.getUserLevel() + " AND "
                        + "cas.res_tab_name_n " + (oStep.getResourceTableName_n() == null ? "IS NULL" : " = '" + oStep.getResourceTableName_n() + "'") + " AND "
                        + "cas.res_pk_n1_n " + (oStep.getResourcePkNum1_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum1_n() + "'") + " AND "
                        + "cas.res_pk_n2_n " + (oStep.getResourcePkNum2_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum2_n() + "'") + " AND "
                        + "cas.res_pk_n3_n " + (oStep.getResourcePkNum3_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum3_n() + "'") + " AND "
                        + "cas.res_pk_n4_n " + (oStep.getResourcePkNum4_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum4_n() + "'") + " AND "
                        + "cas.res_pk_n5_n " + (oStep.getResourcePkNum5_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum5_n() + "'") + " AND "
                        + "cas.res_pk_n6_n " + (oStep.getResourcePkNum6_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum6_n() + "'") + " AND "
                        + "cas.res_pk_n7_n " + (oStep.getResourcePkNum7_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum7_n() + "'") + " AND "
                        + "cas.res_pk_n8_n " + (oStep.getResourcePkNum8_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum8_n() + "'") + " AND "
                        + "cas.res_pk_n9_n " + (oStep.getResourcePkNum9_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum9_n() + "'") + " AND "
                        + "cas.res_pk_n10_n " + (oStep.getResourcePkNum10_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum10_n() + "'") + " AND "
                        + "cas.id_auth_step <> " + oStep.getPkStepId() + " "
                        + ";";
                
                ArrayList<SAuthStatus> lStatus = new ArrayList<>();
                ResultSet resRows = session.getDatabase().getConnection().createStatement().executeQuery(rowsQuery);
                while (resRows.next()) {
                    if (resRows.getBoolean("b_auth")) {
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
                oStep.setFkUserAuthId_n(0);
                oStep.setAuthorized(false);
                oStep.setDateTimeRejected_n(new Date());
                oStep.setFkUserRejId_n(session.getUser().getPkUserId());
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
        ArrayList<SDbAuthConfiguration> lCfgs = SAuthorizationUtils.getConfigurationsOfType(session, authorizationType);
        String condPk = "";
        ArrayList<SDbAuthStep> lSteps = new ArrayList<>();
        switch(authorizationType) {
            case AUTH_TYPE_MAT_REQUEST:
                condPk = "id_mat_req = " + ((int[]) pk)[0];
                for (SDbAuthConfiguration oCfg : lCfgs) {
                    if (SAuthorizationUtils.applyCfg(session, oCfg, condPk)) {
                        // Crear renglón de autorización
                        SDbAuthStep oStep = SAuthorizationUtils.createStepFromCfg(oCfg, pk);
                        lSteps.add(oStep);
                    }
                }
                break;
            case AUTH_TYPE_DPS:
                condPk = "id_year = " + ((int[]) pk)[0] + " AND id_doc = " + ((int[]) pk)[1] + " ";
                for (SDbAuthConfiguration oCfg : lCfgs) {
                    if (SAuthorizationUtils.applyCfg(session, oCfg, condPk)) {
                        // Crear renglón de autorización
                        SDbAuthStep oStep = SAuthorizationUtils.createStepFromCfg(oCfg, pk);
                        lSteps.add(oStep);
                    }
                }
                break;
        }
        
        for (SDbAuthStep oStep : lSteps) {
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
    private static ArrayList<SDbAuthConfiguration> getConfigurationsOfType(SGuiSession session, final int authorizationType) {
        String sql = "SELECT id_auth "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTH) + " "
                + "WHERE NOT b_del AND fk_auth_type = " + authorizationType + " "
                + "ORDER BY lev ASC;";
        
        try {
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            
            ArrayList<SDbAuthConfiguration> lCfgs = new ArrayList<>();
            SDbAuthConfiguration cfgItem;
            while(res.next()) {
                cfgItem = new SDbAuthConfiguration();
                cfgItem.read(session, new int[]{ res.getInt("id_auth") });
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
    private static boolean applyCfg(SGuiSession session, SDbAuthConfiguration oCfg, final String condPk) {
        if (oCfg.getTableName_n() == null) {
            return true;
        }
        if (oCfg.getConditionField_n() == null) {
            return false;
        }
        if (oCfg.getConditionOperator_n() == null) {
            return false;
        }
        if (oCfg.getConditionValue_n() == null) {
            return false;
        }
        
        String query = "SELECT '1' AS apply_cfg FROM " + oCfg.getTableName_n() + " WHERE " + condPk + " AND ";
        query += oCfg.getConditionField_n();
        
        switch(oCfg.getConditionOperator_n()) {
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
        
        query += oCfg.getConditionValue_n();
        
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
     * Crea un registro SDbAuthStep a partir de SDbAuthConfiguration aplicando la llave primaria.
     * 
     * @param oCfg
     * @param pk
     * 
     * @return 
     */
    private static SDbAuthStep createStepFromCfg(SDbAuthConfiguration oCfg, final Object pk) {
        SDbAuthStep oStep = new SDbAuthStep();
        
        oStep.setResourcePkNum1_n(null);
        oStep.setResourcePkNum2_n(null);
        oStep.setResourcePkNum3_n(null);
        oStep.setResourcePkNum4_n(null);
        oStep.setResourcePkNum5_n(null);
        oStep.setResourcePkNum6_n(null);
        oStep.setResourcePkNum7_n(null);
        oStep.setResourcePkNum8_n(null);
        oStep.setResourcePkNum9_n(null);
        oStep.setResourcePkNum10_n(null);
                
        switch(oCfg.getFkAuthTypeId()) {
            case AUTH_TYPE_MAT_REQUEST:
                oStep.setResourceTableName_n("trn_mat_req");
                oStep.setResourcePkNum1_n("" + ((int[]) pk)[0]);
                oStep.setResourcePkLength(1);
                break;
            case AUTH_TYPE_DPS:
                oStep.setResourceTableName_n("trn_dps");
                oStep.setResourcePkNum1_n("" + ((int[]) pk)[0]);
                oStep.setResourcePkNum2_n("" + ((int[]) pk)[1]);
                oStep.setResourcePkLength(2);
                break;
            default:
                return null;
        }
        
        oStep.setUserLevel(oCfg.getUserLevel());
        oStep.setDateTimeAuthorized_n(null);
        oStep.setDateTimeRejected_n(null);
        oStep.setComments("");
        oStep.setAuthorized(false);
        oStep.setRejected(false);
        oStep.setRequired(oCfg.isRequired());
        oStep.setDeleted(false);
        oStep.setSystem(false);
        oStep.setFkAuthTypeId(oCfg.getFkAuthTypeId());
        oStep.setFkCfgAuthId_n(oCfg.getPkConfigurationId());
        oStep.setFkUserStepId(oCfg.getFkUserAuthId());
        oStep.setFkUserAuthId_n(0);
        oStep.setFkUserRejId_n(0);
                
        return oStep;
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
    private static boolean stepExists(SGuiSession session, SDbAuthStep oStep) {
        String sql = "SELECT '1' AS cfg_exists "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTH_STEP) + " "
                + "WHERE NOT b_del AND "
                    + "fk_auth_type = " + oStep.getFkAuthTypeId() + " AND "
                    + "fk_usr_step = " + oStep.getFkUserStepId() + " AND "
                    + "cas.lev = " + oStep.getUserLevel() + " AND "
                    + "cas.res_tab_name_n " + (oStep.getResourceTableName_n() == null ? "IS NULL" : " = '" + oStep.getResourceTableName_n() + "'") + " AND "
                    + "cas.res_pk_n1_n " + (oStep.getResourcePkNum1_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum1_n() + "'") + " AND "
                    + "cas.res_pk_n2_n " + (oStep.getResourcePkNum2_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum2_n() + "'") + " AND "
                    + "cas.res_pk_n3_n " + (oStep.getResourcePkNum3_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum3_n() + "'") + " AND "
                    + "cas.res_pk_n4_n " + (oStep.getResourcePkNum4_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum4_n() + "'") + " AND "
                    + "cas.res_pk_n5_n " + (oStep.getResourcePkNum5_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum5_n() + "'") + " AND "
                    + "cas.res_pk_n6_n " + (oStep.getResourcePkNum6_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum6_n() + "'") + " AND "
                    + "cas.res_pk_n7_n " + (oStep.getResourcePkNum7_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum7_n() + "'") + " AND "
                    + "cas.res_pk_n8_n " + (oStep.getResourcePkNum8_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum8_n() + "'") + " AND "
                    + "cas.res_pk_n9_n " + (oStep.getResourcePkNum9_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum9_n() + "'") + " AND "
                    + "cas.res_pk_n10_n " + (oStep.getResourcePkNum10_n() == null ? "IS NULL" : " = '" + oStep.getResourcePkNum10_n() + "'") + ";";
        
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
    public static ArrayList<SDbAuthStep> getResourceAuthSteps(SGuiSession session, final int authorizationType, final Object pk) {
        String condPk = "";
        switch(authorizationType) {
            case AUTH_TYPE_MAT_REQUEST:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " ";
                break;
                
            case AUTH_TYPE_DPS:
                condPk = "res_pk_n1_n = " + ((int[]) pk)[0] + " AND res_pk_n2_n = " + ((int[]) pk)[1] + " ";
                break;
        }
        
        
        String sql = "SELECT id_auth_step "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTH_STEP) + " "
                + "WHERE NOT b_del AND "
                    + "fk_auth_type = " + authorizationType + " AND "
                    + condPk + " ORDER BY lev ASC;";
        
        ArrayList<SDbAuthStep> lSteps = new ArrayList<>();
        try {
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(sql);
            SDbAuthStep oStep;
            while (res.next()) {
                oStep = new SDbAuthStep();
                oStep.read(session, new int[] { res.getInt("id_auth_step") });
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
