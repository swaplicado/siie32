/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import cfd.DCfdConsts;
import cfd.DCfdUtils;
import cfd.ver40.DCfdi40Catalogs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swaplicado.cloudstoragemanager.CloudStorageManager;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbComSyncLogEntry;
import erp.mod.cfg.db.SDbFunctionalSubArea;
import erp.mod.cfg.db.SDbSyncLogEntry;
import erp.mod.cfg.swap.SHttpConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.SSwapUtils;
import erp.mod.cfg.swap.SSyncType;
import erp.mod.fin.db.SDbPayment;
import erp.mod.fin.db.SDbPaymentEntry;
import erp.mod.trn.api.data.SWebDpsFile;
import erp.mod.trn.api.db.STrnDBDocuments;
import erp.musr.data.SSyncRoles;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import sa.lib.mail.SMailUtils;

/**
/**
 * Utilidades para obtener datos para exportación a SWAP Services en formato JSON.
 * 
 * Esta clase contiene métodos para consultar la base de datos y generar 
 * estructuras JSON usando Jackson, facilitando la integración y exportación de 
 * información con otros sistemas.
 * 
 * @author Sergio Flores
 */
public abstract class SExportDataUtils {
    
    /**
     * Obtiene el nombre de la tabla de la bitácora de sincronización solicitada.
     * 
     * @param syncType Tipo de sincronización.
     * @param database Nombre de la base de datos de empresa: solamente requerida para SModConsts.CFG_COM_SYNC_LOG, en otro caso se descarta.
     * @return Nombre de la tabla de la bitácora de sincronización solicitada.
     * @throws Exception Si algún parámetro es inválido.
     */
    private static String getSqlTableSyncLog(final SSyncType syncType, final String database) throws Exception {
        String table = "";
        
        switch (syncType) {
            case USER:
            case PARTNER_SUPPLIER:
            case PARTNER_CUSTOMER:
            case AUTH_ACTOR:
            case AUTH_JOB_TITLE:
                table = SModConsts.TablesMap.get(SModConsts.CFG_SYNC_LOG);
                break;
                
            case FUNCTIONAL_AREA:
            case PUR_ORDER:
            case PUR_ORDER_FILE:
            case PUR_REF_ORDER:
            case PUR_PAYMENT:
            case PUR_PAYMENT_UPD:
                table = (database.isEmpty() ? "" : database + ".") + SModConsts.TablesMap.get(SModConsts.CFG_COM_SYNC_LOG);
                break;
                
            default:
                throw new IllegalArgumentException(SExportUtils.ERR_UNSUPPORTED_SYNC_TYPE + "'" + syncType + "'.");
        }
        
        return table;
    }

    /**
     * Obtiene consulta base SQL para socios de negocios proveedores.
     * 
     * @return consulta base SQL.
     */
    private static String getSqlQueryBasePartnerSuppliers() {
        return "SELECT "
                + "b.id_bp, b.bp, b.lastname, b.firstname, b.bp_comm, "
                + "b.fiscal_id, b.fiscal_frg_id, b.fid_tp_bp_idy, b.b_del, "
                + "bc.b_del, bc.tax_regime, bc.days_cred, bc.fid_tp_cred_n, bba.fid_cty_n, cty.cty_code, bbc.email_01 "
                + "FROM "
                + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON bc.id_bp = b.id_bp AND bc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_SUP + " "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bb ON bb.fid_bp = b.id_bp AND bb.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB_CON) + " AS bbc ON bbc.id_bpb = bb.id_bpb AND bbc.id_con = " + SUtilConsts.BRA_CON_ID + " "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB_ADD) + " AS bba ON bba.id_bpb = bb.id_bpb AND bba.id_add = " + SUtilConsts.BRA_ADD_ID + " "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOCU_CTY) + " AS cty ON cty.id_cty = bba.fid_cty_n "
                + "LEFT OUTER JOIN ("
                    + "SELECT bul.id_bp, bul.ts_usr_upd "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_UPD_LOG) + " AS bul "
                    + "INNER JOIN ("
                        + "SELECT bulx.id_bp, MAX(bulx.id_log) AS id_log "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_UPD_LOG) + " AS bulx "
                        + "GROUP BY bulx.id_bp "
                        + "ORDER BY bulx.id_bp"
                    + ") AS t ON t.id_bp = bul.id_bp AND t.id_log = bul.id_log "
                    + "ORDER BY bul.id_bp"
                + ") AS tbul ON tbul.id_bp = b.id_bp ";
    }

    /**
     * Obtiene subconsulta SQL para recuperar registros previamente sincronizados.
     * 
     * @param syncType Tipo de sincronización.
     * @param database Nombre de la base de datos de empresa: solamente requerida para SModConsts.CFG_COM_SYNC_LOG, en otro caso se descarta.
     * @return Subconsulta SQL.
     * @throws Excepton Si algún parámetro es inválido.
     */
    private static String getSqlSubQuerySyncedRegistries(final SSyncType syncType, final String database) throws Exception {
        String table = getSqlTableSyncLog(syncType, database);
        boolean sortAsUnsigned = syncType == SSyncType.USER || 
                syncType == SSyncType.PARTNER_SUPPLIER || 
                syncType == SSyncType.PARTNER_CUSTOMER || 
                syncType == SSyncType.AUTH_JOB_TITLE || 
                syncType == SSyncType.FUNCTIONAL_AREA;
        
        return "SELECT "
                + "DISTINCT sle.reference_id "
                + "FROM "
                + table + " AS sl "
                + "INNER JOIN " + table + "_ety AS sle ON sle.id_sync_log = sl.id_sync_log "
                + "WHERE "
                + "sl.sync_type = '" + syncType + "' "
                + "AND (sle.response_code = '" + SHttpConsts.RSC_SUCC_OK + "' OR sle.response_code = '" + SHttpConsts.RSC_SUCC_CREATED + "') "
                + "ORDER BY "
                + (sortAsUnsigned ? "CONVERT(sle.reference_id, UNSIGNED)" : "sle.reference_id");
    }

    /**
     * Obtiene la fecha de la última sincronización exitosa para el tipo de sincronización indicado.
     * 
     * @param statement Statement para ejecutar la consulta.
     * @param syncType Tipo de sincronización.
     * @param database Nombre de la base de datos de empresa: solamente requerida para SModConsts.CFG_COM_SYNC_LOG, en otro caso se descarta.
     * @return Fecha de la última sincronización exitosa, o null si no existe.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static Date getLastSyncDatetime(final Statement statement, final SSyncType syncType, final String database) throws SQLException, Exception {
        Date datetime = null;
        String table = getSqlTableSyncLog(syncType, database);
        
        String sql = "SELECT "
                + "id_sync_log, TIMESTAMPADD(HOUR, -1, ts_usr) AS _last_sync " // timestamp of server device! (minus one hour)
                + "FROM "
                + table + " "
                + "WHERE "
                + "response_code = '" + SHttpConsts.RSC_SUCC_OK + "' "
                + "AND sync_type = '" + syncType + "' "
                + "ORDER BY "
                + "id_sync_log DESC " // primero el registro más nuevo
                + "LIMIT 1;";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                datetime = resultSet.getTimestamp("_last_sync");
            }
        }
        
        return datetime;
    }

    /**
     * Marcar la última entrada en bitácora de sincronización con estatus "CREADA" como "OK" para indicar la culminación del proceso de sincronización.
     * 
     * @param statement Statement para ejecutar la consulta.
     * @param syncType Tipo de sincronización.
     * @param firstRequestDatetime Fecha-hora de la primer petición en el dispositivo cliente del conjunto actual de iteraciones de sincronización.
     * @param database Nombre de la base de datos de empresa: solamente requerida para SModConsts.CFG_COM_SYNC_LOG, en otro caso se descarta.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public static void markLastSyncCreatedAsOk(final Statement statement, final SSyncType syncType, final Date firstRequestDatetime, final String database) throws SQLException, Exception {
        String table = getSqlTableSyncLog(syncType, database);
        
        String sql = "SELECT "
                + "MAX(id_sync_log) "
                + "FROM "
                + table + " "
                + "WHERE "
                + "response_code = '" + SHttpConsts.RSC_SUCC_CREATED + "' "
                + "AND sync_type = '" + syncType + "' "
                + "AND request_timestamp >= '" + SLibUtils.DbmsDateFormatDatetime.format(firstRequestDatetime) + "';";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                int syncLogId = resultSet.getInt(1);
                if (syncLogId > 0) {
                    sql = "UPDATE " + table + " SET "
                            + "response_code = '" + SHttpConsts.RSC_SUCC_OK + "' "
                            + "WHERE "
                            + "id_sync_log = " + syncLogId + " ;";
                    statement.execute(sql);
                }
            }
        }
    }

    /**
     * Consulta los usuarios, y los prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @return Lista de usuarios exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfUsersToExport(final SGuiSession session) throws SQLException, Exception {
        ArrayList<SExportData> users = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String referenceId = "CONVERT(u.id_usr, CHAR)";
            Date lastSyncDatetime = getLastSyncDatetime(session.getStatement(), SSyncType.USER, "");
            
            String sqlConfig = "";
            if (lastSyncDatetime != null) {
                HashMap<Integer, String> databasesMap = SExportUtils.getSwapCompaniesDatabasesMap(session);
                for (Integer companyId : databasesMap.keySet()) {
                    String database = databasesMap.get(companyId);
                    sqlConfig += (sqlConfig.isEmpty() ? "" : " UNION ")
                            + "SELECT "
                            + "id_usr "
                            + "FROM "
                            + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_USR_CFG) + " "
                            + "WHERE "
                            + "ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "'";
                }
                
                if (!sqlConfig.isEmpty()) {
                    sqlConfig += " ORDER BY id_usr";
                }
            }
            
            String sql = "SELECT "
                    + "u.id_usr, u.usr, u.email, u.b_act, u.b_del, u.fid_bp_n, "
                    + "b.bp, b.lastname, b.firstname "
                    + "FROM "
                    + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS u "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = u.fid_bp_n "
                    + "WHERE ("
                    + "((NOT u.b_del AND (b.b_del IS NULL OR NOT b.b_del)) "
                    + "AND " + referenceId + " NOT IN (" + getSqlSubQuerySyncedRegistries(SSyncType.USER, "") + "))"
                    + (lastSyncDatetime == null ? "" : " OR (u.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' "
                        + "OR (b.ts_edit IS NOT NULL AND b.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "')"
                        + (sqlConfig.isEmpty() ? "" : " OR u.id_usr IN (" + sqlConfig + ")") + ")")
                    + ") "
                    + "AND u.usr <> '' AND u.email <> '' "
                    + "ORDER BY "
                    + "u.id_usr;";
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                int userId = resultSet.getInt("u.id_usr");
                String username = resultSet.getString("u.usr");
                String email = resultSet.getString("u.email");
                
                // validar que el usuario tenga nombre y correo:
                
                if (username.isEmpty() || !SMailUtils.isValidEmail(email)) {
                    Logger.getLogger(SExportUtils.class.getName()).log(Level.INFO,
                            "Usuario omitido (nombre de usuario vacío o correo inválido): ID = {0}; username = {1}; email = {2}.",
                            new Object[] { userId, username, email });
                    continue; // omitir usuario inválido
                }
                
                // validar que el usuario tenga nombre(s) y apellido(s):
                
                String firstName = resultSet.getString("b.firstname");
                if (resultSet.wasNull()) {
                    firstName = "";
                }
                
                String lastName = resultSet.getString("b.lastname");
                if (resultSet.wasNull()) {
                    lastName = "";
                }
                
                if (firstName.isEmpty() || lastName.isEmpty()) {
                    Logger.getLogger(SExportUtils.class.getName()).log(Level.INFO,
                            "Usuario omitido (nombre(s) o apellido(s) vacíos): ID = {0}; username = {1}; last_name = {2}; first_name = {3}.",
                            new Object[] { userId, username, lastName, firstName });
                    continue; // omitir usuario inválido
                }

                // crear e inicializar el objeto para exportación de datos:
                
                SExportDataUser user = new SExportDataUser();
                
                user.username = username;
                user.email = email;
                user.password = SExportUtils.generateSecurePassword();
                user.is_active = resultSet.getBoolean("u.b_act");
                user.first_name = firstName;
                user.last_name = lastName;

                boolean purchaserAgent = false;
                ArrayList<Integer> roles = SUserUtils.getUserRoles(session, userId);
                ArrayList<Integer> companies = SUserUtils.getUserAccesibleCompanies(session, userId);
                
                HashSet<Integer> groups = new HashSet<>();
                
                for (Integer role : roles) {
                    if (role == SSwapConsts.ROL_PURCHASER_AGENT) {
                        purchaserAgent = true;
                        groups.add(SSwapConsts.ROL_PURCHASER);
                    }
                    else {
                        groups.add(role);
                    }
                }
                
                SExportDataUser.Attributes attributes = new SExportDataUser.Attributes();
                attributes.full_name = SJsonUtils.sanitizeJson(firstName + " " + lastName);
                attributes.user_type = SSwapConsts.USER_TYPE_INTERNAL;
                attributes.other_emails = null; // no soportado en usuarios
                attributes.notification_settings = purchaserAgent ? SSwapConsts.PURCHASER_AGENT : "";
                attributes.is_deleted = resultSet.getBoolean("u.b_del");
                attributes.external_id = userId;
                user.attributes = attributes;
                
                user.groups = groups.stream().mapToInt(Integer::intValue).toArray();

                user.companies = companies.stream().mapToInt(Integer::intValue).toArray();

                ArrayList<int[]> functionalSubAreas = SUserUtils.getUserAsignedFunctionalSubAreas(session, userId);
                ArrayList<SExportDataUser.FunctionalArea> functionalAreas = new ArrayList<>();
                for (int[] functionalSubArea : functionalSubAreas) {
                    SExportDataUser.FunctionalArea functionalArea = new SExportDataUser.FunctionalArea();
                    
                    functionalArea.external_company_id = functionalSubArea[0];
                    functionalArea.external_id = functionalSubArea[1];
                    
                    functionalAreas.add(functionalArea);
                }
                user.functional_areas = functionalAreas.toArray(new SExportDataUser.FunctionalArea[0]);

                users.add(user);
            }
        }

        return users;
    }
    
    /**
     * Crea usuario para socio de negocios proveedor a partir de un conjunto de datos previamente abierto.
     * 
     * @param resultSet Conjunto de datos previamente abierto.
     * @param rfcPattern RFC regex pattern.
     * @return Usuario para socio de negocios proveedor.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static SExportDataUser createUserForPartnerSupplier(final ResultSet resultSet, final Pattern rfcPattern) throws SQLException {
        int partnerId = resultSet.getInt("b.id_bp");
        
        String username;
        String fullName = SJsonUtils.sanitizeJson(resultSet.getString("b.bp"));
        String fiscalId = SJsonUtils.sanitizeJson(resultSet.getString("b.fiscal_id"));
        String foreignFiscalId = SJsonUtils.sanitizeJson(resultSet.getString("b.fiscal_frg_id"));
        
        String countryCode = SJsonUtils.sanitizeJson(resultSet.getString("cty.cty_code")); // ¡NO MOVER! ¡Debe ser la sentencia previa al siguiente "if"!

        if (resultSet.wasNull() || countryCode.equals(DCfdi40Catalogs.ClavePaísMex)) { // ¿el código de país es nulo o igual a "MEX"?
            // proveedor nacional:
            
            if (!rfcPattern.matcher(fiscalId).matches()) {
                Logger.getLogger(SExportUtils.class.getName()).log(Level.INFO,
                        "Socio de negocios omitido (RFC inválido): ID = {0}; name = {1}; RFC = {2}.",
                        new Object[] { partnerId, fullName, fiscalId});
                return null; // regresar null para omitir usuario inválido
            }

            countryCode = DCfdi40Catalogs.ClavePaísMex;

            // formato del nombre de usuario: ID fiscal nacional
            username = SSwapUtils.sanitizeUsername(fiscalId, SLibConsts.LAN_ISO639_ES);
        }
        else {
            // proveedor extranjero:

            if (foreignFiscalId.isEmpty()) {
                Logger.getLogger(SExportUtils.class.getName()).log(Level.INFO,
                        "Socio de negocios omitido (ID fiscal extranjero vacío): ID = {0}; name = {1}; fiscal ID = {2}.",
                        new Object[] { partnerId, fullName, "<blank>"});
                return null; // regresar null para omitir usuario inválido
            }

            // formato del nombre de usuario: código de país + '-' + ID fiscal extrangero
            username = SSwapUtils.sanitizeUsername(countryCode + SSwapConsts.SEPARATOR_FRG_FISCAL_ID + foreignFiscalId, SLibConsts.LAN_ISO639_EN);
        }

        if (username.isEmpty() || username.contains(" ")) {
            Logger.getLogger(SExportUtils.class.getName()).log(Level.INFO,
                    "Socio de negocios omitido (nombre de usuario vacío o inválido): ID = {0}; name = {1}; username = {2}.",
                    new Object[] { partnerId, fullName, username.isEmpty() ? "<blank>" : username});
            return null; // regresar null para omitir usuario inválido
        }
        
        String tradeName = SJsonUtils.sanitizeJson(resultSet.getString("b.bp_comm"));
        String lastName = SJsonUtils.sanitizeJson(resultSet.getString("b.lastname"));
        String firstName = SJsonUtils.sanitizeJson(resultSet.getString("b.firstname"));
        String taxRegimeCode = resultSet.getString("bc.tax_regime"); // no requiere sanitización!
        String[] emails = SMailUtils.sanitizeEmails(resultSet.getString("bbc.email_01")).split(";");
        String email = emails.length > 0 ? SLibUtils.textTrim(emails[0]) : "";
        boolean isPerson = resultSet.getInt("b.fid_tp_bp_idy") == SDataConstantsSys.BPSS_TP_BP_IDY_PER;

        if (!email.isEmpty() && !SMailUtils.isValidEmail(email)) {
            Logger.getLogger(SExportUtils.class.getName()).log(Level.INFO,
                    "Socio de negocios omitido (correo inválido): ID = {0}; name = {1}; email = {2}.",
                    new Object[] { partnerId, fullName, email});
            return null; // regresar null para omitir usuario inválido
        }
        
        SExportDataUser user = new SExportDataUser();

        user.username = username;
        user.email = email;
        user.password = SExportUtils.generateSecurePassword();
        user.is_active = true;
        user.first_name = firstName;
        user.last_name = lastName;

        String otherEmails = "";
        if (emails.length > 1) {
            for (int i = 1; i < emails.length; i++) { // empezar desde el segundo correo (i = 1)!
                if (SMailUtils.isValidEmail(emails[i])) {
                    otherEmails += (otherEmails.isEmpty() ? "" : ";") + emails[i];
                }
            }
        }

        SExportDataUser.Attributes attributes = new SExportDataUser.Attributes();
        attributes.full_name = fullName;
        attributes.user_type = SSwapConsts.USER_TYPE_EXTERNAL;
        attributes.other_emails = !otherEmails.isEmpty() ? otherEmails : null;
        attributes.is_deleted = resultSet.getBoolean("b.b_del") || resultSet.getBoolean("bc.b_del");
        attributes.external_id = partnerId;
        user.attributes = attributes;

        SExportDataUser.Partner partner = new SExportDataUser.Partner();
        
        partner.is_vendor = true;
        partner.fiscal_id = fiscalId;
        partner.foreign_fiscal_id = foreignFiscalId;
        partner.full_name = attributes.full_name;
        partner.trade_name = tradeName;
        partner.entity_type = isPerson ? SSwapConsts.PARTNER_ENTITY_TYPE_PER : SSwapConsts.PARTNER_ENTITY_TYPE_ORG;
        partner.country_code = countryCode;
        partner.tax_regime_code = taxRegimeCode;
        partner.credit_days = resultSet.getInt("bc.fid_tp_cred_n") == SDataConstantsSys.BPSS_TP_CRED_CRED_NO ? 0 : resultSet.getInt("bc.days_cred");
        partner.partner_mail = user.email;
        partner.is_deleted = attributes.is_deleted;
        partner.external_id = attributes.external_id;
        user.partner = partner;

        user.groups = new int[] { SSwapConsts.ROL_SUPPLIER };

        return user;
    }

    /**
     * Verifica si el socio de negocios es compañía en SWAP Services.
     * 
     * @param session Sesión de usuario.
     * @param bizPartnerId ID del socio de negocios.
     * @return 
     */
    public static boolean isCompany(final SGuiSession session, final int bizPartnerId) {
        boolean isCompany = false;
        int[] companies = (int[]) ((SClientInterface) session.getClient()).getSwapServicesSetting(SSwapConsts.CFG_NVP_COMPANIES);
        
        for (int id : companies) {
            if (id == bizPartnerId) {
                isCompany = true;
                break;
            }
        }
        
        return isCompany;
    }

    /**
     * Consulta los socios de negocios proveedores, y los prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @return Lista de socios de negocios proveedores exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfPartnerSuppliersToExport(final SGuiSession session) throws SQLException, Exception {
        ArrayList<SExportData> users = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String referenceId = "CONVERT(b.id_bp, CHAR)";
            Date lastSyncDatetime = getLastSyncDatetime(session.getStatement(), SSyncType.PARTNER_SUPPLIER, "");
            Pattern rfcPattern = DCfdUtils.createRfcPattern();
            
            String companiesToExclude = SExportUtils.getSwapCompaniesForSqlQuery(session); // excluir los socios que son las empresas en SWAP Services
            
            String sql = getSqlQueryBasePartnerSuppliers()
                    + "WHERE "
                    + "(b.id_bp NOT IN (" + companiesToExclude + ") AND "
                    + "((NOT b.b_del AND NOT bc.b_del) "
                    + "AND " + referenceId + " NOT IN (" + getSqlSubQuerySyncedRegistries(SSyncType.PARTNER_SUPPLIER, "") + "))"
                    + (lastSyncDatetime == null ? "" : " OR (b.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' "
                        + "OR bc.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' "
                        + "OR (tbul.ts_usr_upd IS NOT NULL AND tbul.ts_usr_upd >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "'))")
                    + ") "
                    + "AND b.b_sup AND NOT (b.fiscal_id = '' OR b.fiscal_id = '" + DCfdConsts.RFC_GEN_NAC + "' OR (b.fiscal_id = '" + DCfdConsts.RFC_GEN_INT + "' AND b.fiscal_frg_id = '')) " // exclude general public and non established partners
                    + "ORDER BY "
                    + "b.id_bp;";
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                SExportDataUser user = createUserForPartnerSupplier(resultSet, rfcPattern);
                
                if (user != null) {
                    // el usuario del socio de negocios proveedor no fue omitido:
                    users.add(user);
                }
            }
        }

        return users;
    }

    /**
     * Consulta los socios de negocios proveedores de pedidos de compras del Portal de Proveedores de AETH, y los prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @return Lista de socios de negocios proveedores exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    @Deprecated
    private static ArrayList<SExportData> getListOfPartnerSuppliersToExportAeth(final SGuiSession session) throws SQLException, Exception {
        ArrayList<SExportData> users = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String database = "erp_aeth";
            String referenceId = "CONVERT(b.id_bp, CHAR) ";
            Date lastSyncDatetime = getLastSyncDatetime(session.getStatement(), SSyncType.PARTNER_SUPPLIER, "");
            Pattern rfcPattern = DCfdUtils.createRfcPattern();
            
            String sql = getSqlQueryBasePartnerSuppliers()
                    + "WHERE "
                    + "b.id_bp IN ("
                    + "SELECT DISTINCT t.fid_bp_r "
                    + "FROM "
                    + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS t "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON c.id_cur = t.fid_cur "
                    //+ "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC_SUB) + " AS fs ON fs.id_func_sub = t.fid_func_sub "
                    + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS fs ON fs.id_func = t.fid_func "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = t.fid_bp_r "
                    + "WHERE ("
                    + "(t.id_year = 2023 AND t.id_doc = 31) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 6928) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 11809) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 12008) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 12011) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 12050) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 12341) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 12345) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 12346) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 12364) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 12369) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 12370) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 12372) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 12384) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 12719) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 12738) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 13457) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 13458) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 13459) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 13501) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 13518) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 13643) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 13791) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 13967) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 13977) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 14113) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 14115) "
                    + "OR (t.id_year = 2023 AND t.id_doc = 14397) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 4007) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 5341) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 5596) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 5926) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 6552) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 6649) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 6905) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 6910) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7027) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7033) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7291) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7367) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7423) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7426) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7624) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7625) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7626) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7627) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7661) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7764) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7785) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7806) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7818) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7911) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 7989) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8052) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8180) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8205) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8369) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8432) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8453) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8473) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8476) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8656) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8658) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8695) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8808) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8834) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8835) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8853) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 8941) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9050) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9126) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9128) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9346) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9351) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9354) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9357) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9358) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9378) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9382) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9383) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9388) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9403) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9404) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9409) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9420) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9442) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9443) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9446) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9447) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9449) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9527) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9557) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9560) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9565) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9578) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9579) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9580) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9759) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9760) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9761) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9762) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9880) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 9960) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10016) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10017) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10018) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10019) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10024) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10175) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10176) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10178) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10188) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10189) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10191) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10267) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10314) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10374) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10378) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10382) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10424) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10451) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10458) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10468) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10472) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10475) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10477) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10480) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10563) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10631) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10681) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10747) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10748) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10762) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10914) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10920) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10922) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10923) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10924) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10925) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10926) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10928) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10940) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10953) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10990) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10991) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 10993) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11082) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11167) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11183) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11454) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11455) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11481) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11497) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11566) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11600) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11718) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11764) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11765) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11766) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11767) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11772) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11776) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11778) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11779) "
                    + "OR (t.id_year = 2025 AND t.id_doc = 11780) "
                    + ") "
                    + "ORDER BY "
                    + "t.fid_bp_r "
                    + ") "
                    //+ "AND b.b_sup AND b.fiscal_id <> '' AND b.fiscal_id <> '" + DCfdConsts.RFC_GEN_NAC + "' "
                    + "ORDER BY "
                    + "b.id_bp;";
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                SExportDataUser user = createUserForPartnerSupplier(resultSet, rfcPattern);
                
                if (user != null) {
                    // el usuario del socio de negocios proveedor no fue omitido:
                    users.add(user);
                }
            }
        }

        return users;
    }

    /**
     * Consulta los usuarios, y los prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @return Lista de usuarios exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfAuthActorsToExport(final SGuiSession session) throws SQLException, Exception {
        ArrayList<SExportData> actors = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String userReferenceId = "CONCAT('" + SExportDataAuthActor.ACTOR_CODE_PREFIX_USER + "-', u.id_usr)";
            String supplierReferenceId = "CONCAT('" + SExportDataAuthActor.ACTOR_CODE_PREFIX_SUPPLIER + "-', b.id_bp)";
            Date lastSyncDatetime = getLastSyncDatetime(session.getStatement(), SSyncType.AUTH_ACTOR, "");
            
            /*
             * Explicación de la consulta unida para obtener los actores para el Sistema de Autorizaciones:
             * 1. Obtener los usuarios con socio de negocios que tengan cualquiera de los roles afines a "Comprador".
             * 2. Obtener los proveedores.
             */
            
            String sql = "SELECT "
                    + "u.id_usr AS _external_id, '" + SExportDataAuthActor.ACTOR_TYPE_USER + "' AS _actor_type_id, 0 AS _is_vendor, 0 AS _is_customer, "
                    + "'" + SExportDataAuthActor.ENTITY_TYPE_PER + "' AS _entity_type, "
                    + userReferenceId + " AS _code, b.firstname AS _first_name, b.lastname AS _last_name, "
                    + "b.bp AS _full_name, u.email AS _email, '' AS _phone, u.b_del OR b.b_del AS _is_deleted, "
                    + "e.fk_pos AS _job_title_id "
                    + "FROM "
                    + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS u "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = u.fid_bp_n "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON e.id_emp = b.id_bp "
                    + "WHERE ("
                    + "((NOT u.b_del AND NOT b.b_del AND (e.b_del IS NULL OR NOT e.b_del)) "
                    + "AND " + userReferenceId + " NOT IN (" + getSqlSubQuerySyncedRegistries(SSyncType.AUTH_ACTOR, "") + "))"
                    + (lastSyncDatetime == null ? "" : " OR (u.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' "
                        + "OR b.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' "
                        + "OR (e.ts_usr_upd IS NOT NULL AND e.ts_usr_upd >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "'))")
                    + ") "
                    + "AND u.sync_settings LIKE '%" + SSyncRoles.COMPRADOR + "%' AND u.usr <> '' AND u.email <> '' "
                    + "UNION "
                    + "SELECT "
                    + "b.id_bp AS _external_id, '" + SExportDataAuthActor.ACTOR_TYPE_THIRD_PARTY + "' AS _actor_type_id, 1 AS _is_vendor, 0 AS _is_customer, "
                    + "IF(b.fid_tp_bp_idy = " + SDataConstantsSys.BPSS_TP_BP_IDY_PER + ", '" + SExportDataAuthActor.ENTITY_TYPE_PER + "', '" + SExportDataAuthActor.ENTITY_TYPE_ORG + "') AS _entity_type, "
                    + supplierReferenceId + " AS _code, b.firstname AS _first_name, b.lastname AS _last_name, "
                    + "b.bp AS _full_name, bbc.email_01 AS _email, CONCAT(tel_area_code_01, tel_num_01) AS _phone, b.b_del OR bc.b_del AS _is_deleted, "
                    + "NULL AS _job_title_id "
                    + "FROM "
                    + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON bc.id_bp = b.id_bp AND bc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_SUP + " "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bb ON bb.fid_bp = b.id_bp AND bb.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB_CON) + " AS bbc ON bbc.id_bpb = bb.id_bpb AND bbc.id_con = " + SUtilConsts.BRA_CON_ID + " "
                    + "WHERE ("
                    + "((NOT b.b_del AND NOT bc.b_del) "
                    + "AND " + supplierReferenceId + " NOT IN (" + getSqlSubQuerySyncedRegistries(SSyncType.AUTH_ACTOR, "") + "))"
                    + (lastSyncDatetime == null ? "" : " OR (b.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' "
                        + "OR (bc.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "'))")
                    + ") "
                    + "AND b.b_sup AND NOT (b.fiscal_id = '' OR b.fiscal_id = '" + DCfdConsts.RFC_GEN_NAC + "' OR (b.fiscal_id = '" + DCfdConsts.RFC_GEN_INT + "' AND b.fiscal_frg_id = '')) AND bbc.email_01 <> '' " // exclude general public and non established partners and partners without email
                    + "ORDER BY "
                    + "_external_id;";

            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                int externalId = resultSet.getInt("_external_id");
                int actorTypeId = resultSet.getInt("_actor_type_id");
                String code = SJsonUtils.sanitizeJson(resultSet.getString("_code"));
                String fullName = SJsonUtils.sanitizeJson(resultSet.getString("_full_name"));
                String[] emails = SMailUtils.sanitizeEmails(resultSet.getString("_email")).split(";");
                String email = emails.length > 0 ? emails[0] : "";
                String phone = resultSet.getString("_phone");
                
                // validar que el usuario tenga nombre y correo:
                
                if (fullName.isEmpty() || !SMailUtils.isValidEmail(email)) {
                    Logger.getLogger(SExportUtils.class.getName()).log(Level.INFO,
                            "Actor omitido (nombre vacío o correo inválido): ID = {0}; tipo = {1}, código = {2}; full name = {3}; email = {4}.",
                            new Object[] { externalId, actorTypeId, code, fullName, email });
                    continue; // omitir usuario inválido
                }
                
                // validar que el usuario tenga nombre(s) y apellido(s):
                
                String firstName = SJsonUtils.sanitizeJson(resultSet.getString("_first_name"));
                String lastName = SJsonUtils.sanitizeJson(resultSet.getString("_last_name"));
                
                // crear e inicializar el objeto para exportación de datos:
                
                SExportDataAuthActor actor = new SExportDataAuthActor();
                
                actor.external_id = externalId;
                actor.actor_type_id = actorTypeId;
                actor.is_vendor = resultSet.getBoolean("_is_vendor");
                actor.is_customer = resultSet.getBoolean("_is_customer");
                actor.entity_type = resultSet.getString("_entity_type");
                actor.code = !code.isEmpty() ? code : "" + externalId;
                actor.first_name = firstName;
                actor.last_name = lastName;
                actor.full_name = fullName;
                actor.email = email;
                actor.phone = phone;
                actor.is_deleted = resultSet.getBoolean("_is_deleted");
                
                actor.companies = null;
                
                int jobTitleId = resultSet.getInt("_job_title_id");
                
                if (!resultSet.wasNull()) {
                    SExportDataAuthActor.OrgElement jobTitle = new SExportDataAuthActor.OrgElement();
                    
                    jobTitle.element_type_id = SExportDataAuthActor.ORG_ELEMENT_TYPE_JOB_TITLE;
                    jobTitle.external_id = jobTitleId;
                    
                    actor.org_elements = new SExportDataAuthActor.OrgElement[] { jobTitle };
                }

                actors.add(actor);
            }
        }

        return actors;
    }

    /**
     * Consulta los puestos laborales, y los prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @return Lista de puesos laborales.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfAuthJobTitlesToExport(final SGuiSession session) throws SQLException, Exception {
        ArrayList<SExportData> functionalAreas = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String referenceId = "CONVERT(p.id_pos, CHAR)";
            Date lastSyncDatetime = getLastSyncDatetime(session.getStatement(), SSyncType.AUTH_JOB_TITLE, "");

            String sql = "SELECT "
                    + "p.id_pos AS external_id, "
                    + "p.code, p.name, p.b_del AS is_deleted "
                    + "FROM "
                    + SModConsts.TablesMap.get(SModConsts.HRSU_POS) + " AS p "
                    + "WHERE ("
                    + "((NOT p.b_del) "
                    + "AND " + referenceId + " NOT IN (" + getSqlSubQuerySyncedRegistries(SSyncType.AUTH_JOB_TITLE, "") + "))"
                    + (lastSyncDatetime == null ? "" : " OR (p.ts_usr_upd >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "')")
                    + ") "
                    + "ORDER BY "
                    + "p.id_pos";

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                // crear e inicializar el objeto para exportación de datos:
                
                int externalId = resultSet.getInt("external_id");
                String code = SJsonUtils.sanitizeJson(resultSet.getString("code"));
                String name = SJsonUtils.sanitizeJson(resultSet.getString("name"));

                SExportDataAuthOrgElement orgElement = new SExportDataAuthOrgElement();

                orgElement.code = !code.isEmpty() ? code : "" + externalId;
                orgElement.name = name;
                orgElement.org_element_type = SExportDataAuthActor.ORG_ELEMENT_TYPE_JOB_TITLE;
                orgElement.is_deleted = resultSet.getBoolean("is_deleted");
                orgElement.external_id = externalId;

                functionalAreas.add(orgElement);
            }
        }
        
        return functionalAreas;
    }

    /**
     * Consulta las áreas funcionales de todas las empresas configuradas para SWAP Services, y los prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @return Lista de usuarios exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfFunctionalAreasToExport(final SGuiSession session) throws SQLException, Exception {
        ArrayList<SExportData> functionalAreas = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            // extraer áreas funcionales de las bases de datos de todas las empresas configuradas para SWAP Services:
            
            HashMap<Integer, String> databasesMap = SExportUtils.getSwapCompaniesDatabasesMap(session);
            
            // iterar sobre las bases de datos de todas las empresas configuradas para SWAP Services:
            
            for (Integer companyId : databasesMap.keySet()) {
                String database = databasesMap.get(companyId);
                String referenceId = "CONVERT(fs.id_func_sub, CHAR)";
                Date lastSyncDatetime = getLastSyncDatetime(session.getStatement(), SSyncType.FUNCTIONAL_AREA, database);
                
                String sql = "SELECT "
                        + "fs.id_func_sub AS external_id, "
                        + "fs.code AS code, CONCAT(f.code, '" + SDbFunctionalSubArea.SEPARATOR + "', fs.name) AS name, fs.b_del OR f.b_del AS is_deleted "
                        + "FROM "
                        + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC_SUB) + " AS fs "
                        + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS f ON f.id_func = fs.fk_func "
                        + "WHERE ("
                        + "((NOT fs.b_del AND NOT f.b_del) "
                        + "AND " + referenceId + " NOT IN (" + getSqlSubQuerySyncedRegistries(SSyncType.FUNCTIONAL_AREA, database) + "))"
                        + (lastSyncDatetime == null ? "" : " OR (fs.ts_usr_upd >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' "
                            + "OR f.ts_usr_upd >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "')")
                        + ") "
                        + "ORDER BY "
                        + "fs.id_func_sub;";

                ResultSet resultSet = statement.executeQuery(sql);
                
                while (resultSet.next()) {
                    // crear e inicializar el objeto para exportación de datos:
                    
                    SExportDataFunctionalArea functionalArea = new SExportDataFunctionalArea();
                    
                    functionalArea.code = SJsonUtils.sanitizeJson(resultSet.getString("code"));
                    functionalArea.name = SJsonUtils.sanitizeJson(resultSet.getString("name"));
                    functionalArea.is_deleted = resultSet.getBoolean("is_deleted");
                    functionalArea.external_company_id = companyId;
                    functionalArea.external_id = resultSet.getInt("external_id");

                    functionalAreas.add(functionalArea);
                }
            }
        }
        
        return functionalAreas;
    }
    
    /**
     * Consulta los pedidos de compras de todas las empresas configuradas para SWAP Services, y los prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @return Lista de pedidos de compras exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfPurchaseOrdersToExport(final SGuiSession session) throws SQLException, Exception {
        ArrayList<SExportData> lDps = new ArrayList<>();
        String sBucketName = CloudStorageManager.getBucketName();
        String sProjectID = CloudStorageManager.getProjectID();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            // extraer referencias de pedidos de compras de las bases de datos de todas las empresas configuradas para SWAP Services:
            
            HashMap<Integer, String> databasesMap = SExportUtils.getSwapCompaniesDatabasesMap(session);
            STrnDBDocuments oDocCore = new STrnDBDocuments();
            
            // iterar sobre las bases de datos de todas las empresas configuradas para SWAP Services:
            
            for (Integer companyId : databasesMap.keySet()) {
                String database = databasesMap.get(companyId);
                String referenceId = "CONCAT(d.id_year, '_', d.id_doc)"; // ID año + '_' + ID documento
                Date lastSyncDatetime = getLastSyncDatetime(session.getStatement(), SSyncType.PUR_ORDER, database);

                String sql = "SELECT "
                        + "d.num_ser, d.num, d.dt, d.id_year, d.id_doc, "
                        + "d.b_authorn, d.b_link, d.b_del, d.fid_st_dps, d.ts_edit, d.ts_authorn, d.ts_link, "
                        + "d.tot_r, d.tot_cur_r, d.exc_rate, d.fid_cur, d.fid_func_sub, d.fid_bp_r, c.cur_key, "
                        + "COALESCE(dcfd.cfd_use, '') AS _cfd_use, d.fid_tp_pay, "
                        + "IF (d.ts_authorn > d.ts_edit, d.ts_authorn, d.ts_edit) as _last_upd, "
                        + "(SELECT GROUP_CONCAT(DISTINCT fid_mat_req) "
                        + "FROM "
                        + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " "
                        + "WHERE "
                        + "fid_dps_year = d.id_year AND fid_dps_doc = d.id_doc) AS _rms, "
                        + "COALESCE((SELECT GROUP_CONCAT(nts, '. ') "
                        + "FROM "
                        + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS_NTS) + " AS dn "
                        + "WHERE "
                        + "dn.id_year = d.id_year AND dn.id_doc = d.id_doc AND NOT dn.b_del), '') AS _notes "
                        + "FROM "
                        + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON c.id_cur = d.fid_cur "
                        + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC_SUB) + " AS fs ON fs.id_func_sub = d.fid_func_sub "
                        + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS f ON f.id_func = fs.fk_func "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = d.fid_bp_r "
                        + "LEFT JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS_CFD) + " AS dcfd ON dcfd.id_year = d.id_year AND dcfd.id_doc = d.id_doc "
                        + "WHERE "
                        + "d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] + " "
                        + "AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] + " "
                        + "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] + " "
                        + "AND d.id_year >= " + session.getSystemYear() + " " // establecer como límite hasta el año actual
                        + "AND ("
//                        + "d.id_doc = 14947 "
                        + "((NOT d.b_del AND d.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " "
                        + "AND d.b_authorn AND d.fid_st_dps_authorn = " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN + ") "
                        + "AND " + referenceId + " NOT IN (" + getSqlSubQuerySyncedRegistries(SSyncType.PUR_ORDER, database) + "))"
                        + (lastSyncDatetime == null ? "" : " OR ("
                        + "(d.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' "
                        + "AND (d.b_del OR d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ")) "
                        + "OR d.ts_authorn >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "')")
                        + ")";
                sql += ";";

                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    SExportDataDpsContainer oContainer = new SExportDataDpsContainer();
                    SExportDataDps oDpsExport = new SExportDataDps();

                    oDpsExport.company = companyId;
                    oDpsExport.id_year = resultSet.getInt("d.id_year");
                    oDpsExport.id_doc = resultSet.getInt("d.id_doc");
                    oDpsExport.transaction_class = SSwapConsts.TXN_CAT_PURCHASE;
                    oDpsExport.partner = resultSet.getInt("d.fid_bp_r");
                    oDpsExport.series = resultSet.getString("d.num_ser");
                    oDpsExport.number = resultSet.getInt("d.num");
                    oDpsExport.date = SLibUtils.DbmsDateFormatDate.format(resultSet.getDate("d.dt")); // yyyy-mm-dd
                    oDpsExport.currency = resultSet.getString("c.cur_key");
                    oDpsExport.amount = resultSet.getDouble("d.tot_cur_r");
                    oDpsExport.exchange_rate = resultSet.getDouble("d.exc_rate");
                    oDpsExport.notes = resultSet.getString("_notes");
                    oDpsExport.functional_area = resultSet.getInt("d.fid_func_sub");
                    oDpsExport.fiscal_use = resultSet.getString("_cfd_use");
                    oDpsExport.payment_method = resultSet.getInt("d.fid_tp_pay") == SDataConstantsSys.TRNS_TP_PAY_CASH ? DCfdi40Catalogs.MDP_PUE : DCfdi40Catalogs.MDP_PPD;
                    oDpsExport.is_deleted = resultSet.getBoolean("d.b_del") || resultSet.getInt("d.fid_st_dps") == SDataConstantsSys.TRNS_ST_DPS_ANNULED || !resultSet.getBoolean("d.b_authorn") || resultSet.getBoolean("d.b_link");
                    
                    // PDF de la OC:
                    SDbComSyncLogEntry oLogEty = SExportDpsFileUtils.getLastSynchronization(
                            session, SSyncType.PUR_ORDER_FILE, oDpsExport.id_year + "_" + oDpsExport.id_doc, database);
                    if (oLogEty != null) {
                        SFileData oFd = new SFileData(oDpsExport.id_year, oDpsExport.id_doc, database, lastSyncDatetime);
                        // comparacion de last update para actualizar el archivo
                        SExportDataDpsFile oFile = new SExportDataDpsFile();
                        oFile.filename_storage = oFd.getFileName();
                        if (oFile.filename_storage.isEmpty()) {
                            Logger.getLogger(SExportUtils.class.getName()).log(Level.WARNING, "filename_storage de OC vacío, se omite. " + oDpsExport.id_year + "_" + oDpsExport.id_doc);
                        }
                        else {
                            oFile.filename_original = oFile.filename_storage;
                            oFile.title = "PDF de la OC";
                            oFile.bucket_name = sBucketName;
                            oFile.project_id = sProjectID;
                            oContainer.file.add(oFile);
                        }
                    }
                    // Si no existe en la bitácora de siie, se sube
                    else {
                        Logger.getLogger(SExportUtils.class.getName()).log(Level.WARNING, "PDF de OC No existe. "
                                + "Sincronizando: " + oDpsExport.id_year + "_" + oDpsExport.id_doc);
                        try {
                            SFileData oFileData = new SFileData(oDpsExport.id_year,
                                    oDpsExport.id_doc,
                                    database,
                                    null);
                            oLogEty = SDpsGoogleCloudUtils.processSingleRecord(session, oFileData, false);
                            if (oLogEty != null) {
                                if (Integer.parseInt(oLogEty.getResponseCode()) == 200
                                        || Integer.parseInt(oLogEty.getResponseCode()) == 201) {
                                    SFileData oFd = new SFileData(oDpsExport.id_year, oDpsExport.id_doc, database, lastSyncDatetime);
                                    // comparacion de last update para actualizar el archivo
                                    SExportDataDpsFile oFile = new SExportDataDpsFile();
                                    oFile.filename_storage = oFd.getFileName();
                                    if (oFile.filename_storage.isEmpty()) {
                                        Logger.getLogger(SExportUtils.class.getName()).log(Level.WARNING, "filename_storage de OC vacío, se omite. "
                                                + "" + oDpsExport.id_year + "_" + oDpsExport.id_doc);
                                    }
                                    else {
                                        oFile.filename_original = oFile.filename_storage;
                                        oFile.title = "PDF de la OC";
                                        oFile.bucket_name = sBucketName;
                                        oFile.project_id = sProjectID;
                                        oContainer.file.add(oFile);
                                    }
                                } else {
                                    Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, "Error al subir el archivo de la OC. "
                                            + oDpsExport.id_year + "_" + oDpsExport.id_doc + ". "
                                            + oLogEty.getResponseBody());
                                }
                                ArrayList<SDbSyncLogEntry> l = new ArrayList<>();
                                l.add(oLogEty);
                                try {
                                    // guardar encabezado de log de archivos:
                                    SDpsGoogleCloudUtils.saveSyncLogs(session, l, false);
                                }
                                catch (Exception e) {
                                    Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, "No se pudieron guardar los logs de sincronización de archivos de OC.", e);
                                }
                            }
                        }
                        catch (Exception e) {
                            Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, "No se pudo generar el PDF de la OC para exportación. "
                                    + "" + oDpsExport.id_year + "_" + oDpsExport.id_doc, e);
                        }
                    }
                    
                    // documentos (archivos de cotizaciones)
                    
                    boolean withUrl = false;
                    ArrayList<SWebDpsFile> lDpsFiles = oDocCore.getDpsFiles(oDpsExport.id_year, oDpsExport.id_doc, withUrl, statement.getConnection().createStatement(), database);
                    for (SWebDpsFile oDpsFile : lDpsFiles) {
                        SExportDataDpsFile oDpsFileExport = new SExportDataDpsFile();
                        oDpsFileExport.filename_storage = oDpsFile.getoWebFile().getCloudStorageName();
                        if (oDpsFileExport.filename_storage.isEmpty()) {
                            Logger.getLogger(SExportUtils.class.getName()).log(Level.WARNING, "filename_storage de COT vacío, se omite. " + oDpsExport.id_year + "_" + oDpsExport.id_doc);
                        }
                        else {
                            oDpsFileExport.title = "Evidencia de cotización";
                            oDpsFileExport.filename_original = oDpsFile.getoWebFile().getUserFileName() != null && !oDpsFile.getoWebFile().getUserFileName().isEmpty()
                                                                ? oDpsFile.getoWebFile().getUserFileName() : oDpsFileExport.filename_storage;
                            oDpsFileExport.bucket_name = sBucketName;
                            oDpsFileExport.project_id = sProjectID;

                            oContainer.file.add(oDpsFileExport);
                        }
                    }
                    
                    // documentos (archivos de requisiciones)
                    String rms = resultSet.getString("_rms");
                    String fileName;
                    String[] idsRms;
                    SExportDataDpsFile oRmFile;
                    if (rms != null && !rms.isEmpty()) {
                        idsRms = rms.split(",");
                        for (String sId : idsRms) {
                            fileName = database + "-" + "REQ" + "-" + sId + ".pdf";
                            if (CloudStorageManager.storagedFileExists(fileName)) {
                                oRmFile = new SExportDataDpsFile();
                                oRmFile.filename_storage = fileName;
                                if (oRmFile.filename_storage.isEmpty()) {
                                    Logger.getLogger(SExportUtils.class.getName()).log(Level.WARNING, "filename_storage de RM vacío, se omite. " + oDpsExport.id_year + "_" + oDpsExport.id_doc);
                                }
                                else {
                                    oRmFile.filename_original = oRmFile.filename_storage;
                                    oRmFile.title = "PDF de la requisición";
                                    oRmFile.bucket_name = sBucketName;
                                    oRmFile.project_id = sProjectID;

                                    oContainer.file.add(oRmFile);
                                }
                            }
                        }
                    }
                    
                    oContainer.document = oDpsExport;
                    
                    lDps.add(oContainer);
                }
            }
        }
        
        return lDps;
    }
    
    private static void exportPurchaseOrdersFiles(final SGuiSession session) throws SQLException, Exception {
        ObjectMapper mapper = new ObjectMapper();

        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            // extraer referencias de pedidos de compras de las bases de datos de todas las empresas configuradas para SWAP Services:

            HashMap<Integer, String> databasesMap = SExportUtils.getSwapCompaniesDatabasesMap(session);

            // iterar sobre las bases de datos de todas las empresas configuradas para SWAP Services:
            for (Integer companyId : databasesMap.keySet()) {
                String database = databasesMap.get(companyId);
                String referenceId = "CONCAT(d.id_year, '_', d.id_doc)"; // ID año + '_' + ID documento
                Date lastSyncDatetime = getLastSyncDatetime(session.getStatement(), SSyncType.PUR_ORDER_FILE, database);
                ArrayList<SDbSyncLogEntry> lLogFiles = new ArrayList<>();

                String sql = "SELECT "
                        + "d.id_year, d.id_doc, "
                        + "IF (d.ts_authorn > d.ts_edit, d.ts_authorn, d.ts_edit) as _last_upd "
                        + "FROM "
                        + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                        + "WHERE "
                        + "d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] + " "
                        + "AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] + " "
                        + "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] + " "
                        + "AND d.id_year >= " + session.getSystemYear() + " " // establecer como límite hasta el año actual
                        + "AND ("
                        + "((NOT d.b_del AND d.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " "
                        + "AND d.b_authorn AND d.fid_st_dps_authorn = " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN + ")"
//                        + ") AND MONTH(d.dt) >= 10 " // Se comenta para forzar sincronización en caso de reporte de archivos faltantes
                        + "AND " + referenceId + " NOT IN (" + getSqlSubQuerySyncedRegistries(SSyncType.PUR_ORDER_FILE, database) + "))"
                        + (lastSyncDatetime == null ? "" : " OR ("
                                + "(d.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' "
                                + "AND (d.b_del OR d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ")) "
                                + "OR d.ts_authorn >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "')")
                        + ")";
                sql += ";";

                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    SExportDataDps oDpsExport = new SExportDataDps();

                    oDpsExport.company = companyId;
                    oDpsExport.id_year = resultSet.getInt("d.id_year");
                    oDpsExport.id_doc = resultSet.getInt("d.id_doc");

                    // PDF de la OC:
                    SDbComSyncLogEntry oLogEty = SExportDpsFileUtils.getLastSynchronization(
                            session, SSyncType.PUR_ORDER_FILE, oDpsExport.id_year + "_" + oDpsExport.id_doc, database);
                    if (oLogEty != null) {
                        SFileData oFd = null;
                        // comparacion de last update para actualizar el archivo
                        if (oLogEty.getTsSync().before(resultSet.getTimestamp("_last_upd"))) {
                            oFd = new SFileData(oDpsExport.id_year, oDpsExport.id_doc, database, resultSet.getTimestamp("_last_upd"));
                            oLogEty = SDpsGoogleCloudUtils.processSingleRecord(session, oFd, true);
                            lLogFiles.add(oLogEty);
                        } else {
                            oFd = mapper.readValue(oLogEty.getResponseBody(), SFileData.class);
                            if (!CloudStorageManager.storagedFileExists(oFd.getFileName())) {
                                oFd = new SFileData(oDpsExport.id_year, oDpsExport.id_doc, database, resultSet.getTimestamp("_last_upd"));
                                oLogEty = SDpsGoogleCloudUtils.processSingleRecord(session, oFd, true);
                                lLogFiles.add(oLogEty);
                            }
                        }
                    } // Si no existe en la bitácora de siie, se sube
                    else {
                        try {
                            SFileData oFileData = new SFileData(oDpsExport.id_year,
                                    oDpsExport.id_doc,
                                    database,
                                    resultSet.getTimestamp("_last_upd"));
                            oLogEty = SDpsGoogleCloudUtils.processSingleRecord(session, oFileData, false);
                            if (oLogEty != null) {
                                if (Integer.parseInt(oLogEty.getResponseCode()) == 200
                                        || Integer.parseInt(oLogEty.getResponseCode()) == 201) {
                                    lLogFiles.add(oLogEty);
                                } else {
                                    Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, "Error al subir el archivo de la OC. "
                                            + oDpsExport.id_year + "_" + oDpsExport.id_doc + ". "
                                            + oLogEty.getResponseBody());
                                }
                            }
                        } catch (Exception e) {
                            Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, "No se pudo generar el PDF de la OC para exportación. "
                                    + "" + oDpsExport.id_year + "_" + oDpsExport.id_doc, e);
                        }
                    }

                }
                try {
                    // guardar encabezado de log de archivos:
                    SDpsGoogleCloudUtils.saveSyncLogs(session, lLogFiles, false);
                }
                catch (Exception e) {
                    Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, "No se pudieron guardar los logs de sincronización de archivos de OC.", e);
                }
            }
        }
    }

    /**
     * Consulta las referencias de pedidos de compras de todas las empresas configuradas para SWAP Services, y las prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @return Lista de órdenes de compras exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfPurchaseOrderRefsToExport(final SGuiSession session) throws SQLException, Exception {
        ArrayList<SExportData> references = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            // extraer referencias de pedidos de compras de las bases de datos de todas las empresas configuradas para SWAP Services:
            
            HashMap<Integer, String> databasesMap = SExportUtils.getSwapCompaniesDatabasesMap(session);
            
            // iterar sobre las bases de datos de todas las empresas configuradas para SWAP Services:
            
            for (Integer companyId : databasesMap.keySet()) {
                /*
                 * Explicación de la consulta para obtener las referencias de transacciones de órdenes de compras:
                 * 1. Generación de tabla derivada ("t") de partidas de pedidos de compras y sus enlaces con facturas:
                 *    - para determinar cuáles partidas de cada pedido de compras están totalmente enlazadas con facturas;
                 *    - agrupando la suma de los enlaces por partida de pedido de compras;
                 *    - excluyendo los enlaces cuyas de partidas o facturas destino estén eliminados o cancelados.
                 * 2. Generación de consulta principal de pedidos de compras:
                 *    - para determinar cuáles están sin enlazar o parcialmente enlacados con facturas;
                 *    - contando el total de sus partidas y las que ya están totalmente enlazadas;
                 *    - recuperando solamente pedidos con partidas pendientes de enlazar.
                 */
                
                String database = databasesMap.get(companyId);
                String referenceId = "CONCAT('" + SSwapConsts.TXN_DOC_REF_TYPE_ORDER_CODE + "', '" + SSwapConsts.SEPARATOR_DOC_REF + "', CONCAT(t.num_ser, IF(t.num_ser = '', '', '-'), t.num))"; // código de tipo de referencia + '/' + referencia
                Date lastSyncDatetime = getLastSyncDatetime(session.getStatement(), SSyncType.PUR_REF_ORDER, database);
                
                String sqlConcepts = "SELECT "
                        + "DISTINCT ir.item_key, ir.item "
                        + "FROM "
                        + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS ir ON ir.id_item = de.fid_item_ref_n "
                        + "WHERE "
                        + "NOT de.b_del AND de.id_year = ? AND de.id_doc = ? "
                        + "ORDER BY "
                        + "ir.item_key, ir.item;";
                
                String sqlCostCenters = "SELECT "
                        + "DISTINCT cc.id_cc, cc.cc "
                        + "FROM "
                        + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de "
                        + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cc ON cc.id_cc = de.fid_cc_n "
                        + "WHERE "
                        + "NOT de.b_del AND de.id_year = ? AND de.id_doc = ? "
                        + "ORDER BY "
                        + "cc.id_cc, cc.cc;";
                
                PreparedStatement prepStatConcepts = session.getStatement().getConnection().prepareStatement(sqlConcepts);
                PreparedStatement prepStatCostProfitCenters = session.getStatement().getConnection().prepareStatement(sqlCostCenters);
                
                String sql = "SELECT "
                        + "t.num_ser, t.num, t.dt, t.id_year, t.id_doc, "
                        + "t.b_authorn, t.b_link, t.b_del, t.fid_st_dps, t.fid_tp_pay, t.ts_edit, t.ts_authorn, t.ts_link, "
                        + "t.tot_r, t.tot_cur_r, t.fid_cur, c.cur_key, t.fid_func_sub, fs.name AS _func_sub, t.fid_bp_r, b.bp, t.fid_usr_new, COALESCE(dcfd.cfd_use, '') AS _cfd_use, "
                        + "COUNT(*) AS _entries, SUM(_is_linked) AS _entries_linked "
                        + "FROM ("
                        + "SELECT "
                        + "d.num_ser, d.num, d.dt, d.id_year, d.id_doc, "
                        + "d.b_authorn, d.b_link, d.b_del, d.fid_st_dps, d.fid_tp_pay, d.ts_edit, d.ts_authorn, d.ts_link, "
                        + "d.tot_r, d.tot_cur_r, d.fid_cur, d.fid_func_sub, d.fid_bp_r, d.fid_usr_new, "
                        + "de.id_ety, de.fid_item, de.fid_unit, de.qty, "
                        + "COALESCE(SUM(IF(xde.b_del OR xd.b_del OR xd.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", 0.0, dds.qty)), 0.0) AS _qty_linked, "
                        + "de.qty <= COALESCE(SUM(IF(xde.b_del OR xd.b_del OR xd.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", 0.0, dds.qty)), 0.0) AS _is_linked "
                        + "FROM "
                        + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                        + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON de.id_year = d.id_year AND de.id_doc = d.id_doc "
                        + "LEFT OUTER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " AS dds ON dds.id_src_year = de.id_year AND dds.id_src_doc = de.id_doc AND dds.id_src_ety = de.id_ety "
                        + "LEFT OUTER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS xde ON xde.id_year = dds.id_des_year AND xde.id_doc = dds.id_des_doc AND xde.id_ety = dds.id_des_ety "
                        + "LEFT OUTER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS xd ON xd.id_year = xde.id_year AND xd.id_doc = xde.id_doc "
                        + "WHERE "
                        + "/*NOT d.b_del AND */NOT de.b_del " // bloque comentado para incluir pedidos eliminados (para "eliminar" referencias en subsecuentes exportaciones)
                        + "/*AND d.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " */ " // bloque comentado para incluir pedidos "anulados" (para "eliminar" referencias en subsecuentes exportaciones)
                        + "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] + " "
                        + "/*AND NOT d.b_link */ " // bloque comentado para incluir pedidos enlazados forzadamente (para "eliminar" referencias en subsecuentes exportaciones)
                        + "AND d.id_year >= " + (session.getSystemYear() - 1) + " " // establecer como límite hasta el año inmediato anterior
                        + "GROUP BY "
                        + "d.num_ser, d.num, d.dt, d.id_year, d.id_doc, "
                        + "d.b_authorn, d.b_link, d.b_del, d.fid_st_dps, d.fid_tp_pay, d.ts_edit, d.ts_authorn, d.ts_link, "
                        + "d.tot_r, d.tot_cur_r, d.fid_cur, d.fid_func_sub, d.fid_bp_r, "
                        + "de.id_ety, de.fid_item, de.fid_unit, de.qty "
                        + "ORDER BY "
                        + "d.num_ser, LPAD(d.num, " + SSwapConsts.LEN_UUID + ", '0'), d.num, d.dt, d.id_year, d.id_doc, "
                        + "d.b_authorn, d.b_link, d.b_del, d.fid_st_dps, d.fid_tp_pay, d.ts_edit, d.ts_authorn, d.ts_link, "
                        + "d.tot_r, d.tot_cur_r, d.fid_cur, d.fid_func_sub, d.fid_bp_r, "
                        + "de.id_ety, de.fid_item, de.fid_unit, de.qty "
                        + ") AS t "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON c.id_cur = t.fid_cur "
                        + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC_SUB) + " AS fs ON fs.id_func_sub = t.fid_func_sub "
                        + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS f ON f.id_func = fs.fk_func "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = t.fid_bp_r "
                        + "LEFT OUTER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS_CFD) + " AS dcfd ON dcfd.id_year = t.id_year AND dcfd.id_doc = t.id_doc "
                        + "WHERE ("
                        + "((NOT t.b_del AND t.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " AND t.b_authorn AND NOT t.b_link) "
                        + "AND " + referenceId + " NOT IN (" + getSqlSubQuerySyncedRegistries(SSyncType.PUR_REF_ORDER, database) + "))"
                        + (lastSyncDatetime == null ? "" : " OR ("
                        + "(t.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' AND (t.b_del OR t.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ")) "
                        + "OR t.ts_authorn >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' "
                        + "OR t.ts_link >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "')")
                        + ") "
                        + "AND NOT (b.fiscal_id = '' OR b.fiscal_id = '" + DCfdConsts.RFC_GEN_NAC + "' OR (b.fiscal_id = '" + DCfdConsts.RFC_GEN_INT + "' AND b.fiscal_frg_id = '')) " // exclude orders from general public and non established partners
                        + "GROUP BY "
                        + "t.num_ser, t.num, t.dt, t.id_year, t.id_doc, t.b_link, t.b_del, t.fid_st_dps, t.fid_tp_pay, t.ts_edit, t.ts_link, "
                        + "t.tot_r, t.tot_cur_r, t.fid_cur, c.cur_key, t.fid_func_sub, fs.name, t.fid_bp_r, b.bp, dcfd.cfd_use "
                        + "HAVING _entries_linked < _entries "
                        + "ORDER BY "
                        + "t.num_ser, LPAD(t.num, " + SSwapConsts.LEN_UUID + ", '0'), t.num, t.dt, t.id_year, t.id_doc, t.b_link, t.b_del, t.fid_st_dps, t.fid_tp_pay, t.ts_edit, t.ts_link, "
                        + "t.tot_r, t.tot_cur_r, t.fid_cur, c.cur_key, t.fid_func_sub, fs.name, t.fid_bp_r, b.bp, dcfd.cfd_use;";

                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    int dpsYear = resultSet.getInt("t.id_year");
                    int dpsDoc = resultSet.getInt("t.id_doc");
                    
                    String txnReference = resultSet.getString("t.num_ser");
                    txnReference += (txnReference.isEmpty() ? "" : "-") + resultSet.getString("t.num");
                    txnReference = SSwapConsts.TXN_DOC_REF_TYPE_ORDER_CODE + SSwapConsts.SEPARATOR_DOC_REF + txnReference; // código de tipo de referencia + '/' + referencia
                    
                    String concepts = "";
                    prepStatConcepts.setInt(1, dpsYear);
                    prepStatConcepts.setInt(2, dpsDoc);
                    
                    try (ResultSet resultSetAux = prepStatConcepts.executeQuery()) {
                        while (resultSetAux.next()) {
                            concepts += (concepts.isEmpty() ? "" : ";") + resultSetAux.getString("ir.item_key") + " - " + resultSetAux.getString("ir.item");
                        }
                    }
                    
                    String costProfitCenters = "";
                    prepStatCostProfitCenters.setInt(1, dpsYear);
                    prepStatCostProfitCenters.setInt(2, dpsDoc);
                    
                    try (ResultSet resultSetAux = prepStatCostProfitCenters.executeQuery()) {
                        while (resultSetAux.next()) {
                            costProfitCenters += (costProfitCenters.isEmpty() ? "" : ";") + resultSetAux.getString("cc.id_cc") + " - " + resultSetAux.getString("cc.cc");
                        }
                    }
                    
                    SExportDataReference reference = new SExportDataReference();

                    reference.external_id = dpsYear + "_" + dpsDoc;
                    reference.external_company_id = companyId;
                    reference.external_functional_area_id = resultSet.getInt("t.fid_func_sub");
                    reference.transaction_class_id = SSwapConsts.TXN_CAT_PURCHASE;
                    reference.document_ref_type_id = SSwapConsts.TXN_DOC_TYPE_ORDER;
                    reference.external_partner_id = resultSet.getInt("t.fid_bp_r");
                    reference.reference = txnReference;
                    reference.date = SLibUtils.DbmsDateFormatDate.format(resultSet.getDate("t.dt")); // yyyy-mm-dd
                    reference.currency_code = resultSet.getString("c.cur_key");
                    reference.amount = resultSet.getDouble("t.tot_cur_r");
                    reference.fiscal_use = resultSet.getString("_cfd_use");
                    reference.payment_method = resultSet.getInt("t.fid_tp_pay") == SDataConstantsSys.TRNS_TP_PAY_CASH ? DCfdi40Catalogs.MDP_PUE : DCfdi40Catalogs.MDP_PPD;
                    reference.concepts = concepts;
                    reference.cost_profit_centers = costProfitCenters;
                    reference.owner_id = resultSet.getInt("t.fid_usr_new");
                    reference.is_deleted = resultSet.getBoolean("t.b_del") || resultSet.getInt("t.fid_st_dps") == SDataConstantsSys.TRNS_ST_DPS_ANNULED || !resultSet.getBoolean("t.b_authorn") || resultSet.getBoolean("t.b_link");

                    references.add(reference);
                }
            }
        }

        return references;
    }
    
    /**
     * Consulta las referencias de pedidos de compras del Portal de Proveedores de AETH, y las prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @return Lista de órdenes de compras exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    @Deprecated
    private static ArrayList<SExportData> getListOfPurchaseOrderRefsToExportAeth(final SGuiSession session) throws SQLException, Exception {
        ArrayList<SExportData> references = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            // extraer referencias de pedidos de compras de las bases de datos de todas las empresas configuradas para SWAP Services:
            
            HashMap<Integer, String> databasesMap = SExportUtils.getSwapCompaniesDatabasesMap(session);
            
            // iterar sobre las bases de datos de todas las empresas configuradas para SWAP Services:
            
            for (Integer companyId : databasesMap.keySet()) {
                if (companyId != 2852) {
                    continue;
                }

                String database = databasesMap.get(companyId);
                String referenceId = "CONCAT('" + SSwapConsts.TXN_DOC_REF_TYPE_ORDER_CODE + "', '" + SSwapConsts.SEPARATOR_DOC_REF + "', CONCAT(t.num_ser, IF(t.num_ser = '', '', '-'), t.num))"; // código de tipo de referencia + '/' + referencia
                Date lastSyncDatetime = getLastSyncDatetime(session.getStatement(), SSyncType.PUR_REF_ORDER, database);
                
                String sql = "SELECT "
                        + "t.num_ser, t.num, t.dt, t.id_year, t.id_doc, t.b_link, t.b_del, t.fid_st_dps, t.ts_edit, "
                        + "t.tot_r, t.tot_cur_r, t.fid_cur, c.cur_key, t.fid_func, fs.name AS _func_sub, t.fid_bp_r, b.bp "
                        + "FROM "
                        + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS t "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON c.id_cur = t.fid_cur "
                        //+ "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC_SUB) + " AS fs ON fs.id_func_sub = t.fid_func_sub "
                        + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS fs ON fs.id_func = t.fid_func "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = t.fid_bp_r "
                        + "WHERE ("
                        + "(t.id_year = 2023 AND t.id_doc = 31) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 6928) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 11809) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 12008) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 12011) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 12050) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 12341) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 12345) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 12346) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 12364) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 12369) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 12370) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 12372) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 12384) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 12719) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 12738) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 13457) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 13458) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 13459) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 13501) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 13518) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 13643) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 13791) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 13967) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 13977) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 14113) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 14115) "
                        + "OR (t.id_year = 2023 AND t.id_doc = 14397) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 4007) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 5341) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 5596) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 5926) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 6552) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 6649) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 6905) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 6910) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7027) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7033) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7291) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7367) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7423) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7426) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7624) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7625) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7626) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7627) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7661) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7764) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7785) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7806) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7818) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7911) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 7989) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8052) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8180) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8205) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8369) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8432) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8453) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8473) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8476) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8656) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8658) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8695) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8808) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8834) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8835) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8853) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 8941) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9050) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9126) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9128) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9346) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9351) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9354) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9357) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9358) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9378) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9382) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9383) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9388) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9403) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9404) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9409) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9420) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9442) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9443) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9446) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9447) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9449) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9527) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9557) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9560) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9565) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9578) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9579) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9580) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9759) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9760) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9761) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9762) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9880) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 9960) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10016) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10017) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10018) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10019) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10024) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10175) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10176) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10178) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10188) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10189) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10191) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10267) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10314) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10374) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10378) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10382) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10424) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10451) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10458) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10468) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10472) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10475) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10477) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10480) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10563) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10631) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10681) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10747) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10748) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10762) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10914) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10920) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10922) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10923) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10924) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10925) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10926) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10928) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10940) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10953) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10990) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10991) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 10993) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11082) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11167) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11183) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11454) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11455) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11481) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11497) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11566) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11600) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11718) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11764) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11765) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11766) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11767) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11772) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11776) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11778) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11779) "
                        + "OR (t.id_year = 2025 AND t.id_doc = 11780) "
                        + ") "
                        + "ORDER BY "
                        + "t.num_ser, LPAD(t.num, " + SSwapConsts.LEN_UUID + ", '0'), t.num, t.dt, t.id_year, t.id_doc, t.b_link, t.b_del, t.fid_st_dps, t.ts_edit, "
                        + "t.tot_r, t.tot_cur_r, t.fid_cur, c.cur_key, t.fid_func, fs.name, t.fid_bp_r, b.bp;";

                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    String txnReference;
                    
                    txnReference = resultSet.getString("t.num_ser");
                    txnReference += (txnReference.isEmpty() ? "" : "-") + resultSet.getString("t.num");
                    txnReference = SSwapConsts.TXN_DOC_REF_TYPE_ORDER_CODE + SSwapConsts.SEPARATOR_DOC_REF + txnReference; // código de tipo de referencia + '/' + referencia
                    
                    SExportDataReference reference = new SExportDataReference();

                    reference.external_company_id = companyId;
                    reference.external_functional_area_id = resultSet.getInt("t.fid_func");
                    reference.transaction_class_id = SSwapConsts.TXN_CAT_PURCHASE;
                    reference.document_ref_type_id = SSwapConsts.TXN_DOC_TYPE_ORDER;
                    reference.external_partner_id = resultSet.getInt("t.fid_bp_r");
                    reference.reference = txnReference;
                    reference.date = SLibUtils.DbmsDateFormatDate.format(resultSet.getDate("t.dt")); // yyyy-mm-dd
                    reference.currency_code = resultSet.getString("c.cur_key");
                    reference.amount = resultSet.getDouble("t.tot_cur_r");
                    reference.fiscal_use = "";
                    reference.payment_method = "";
                    reference.concepts = "";
                    reference.cost_profit_centers = "";
                    reference.owner_id = 0;
                    reference.is_deleted = resultSet.getBoolean("t.b_del") || resultSet.getInt("t.fid_st_dps") == SDataConstantsSys.TRNS_ST_DPS_ANNULED;

                    references.add(reference);
                }
            }
        }

        return references;
    }

    /**
     * Consulta los pagos de compras de todas las empresas configuradas para SWAP Services, y los prepara para la exportación.
     * Exporta pagos en los estatus "nuevo" y "operado (en proceso)":
     * a) los pagos "nuevo" solo tienen una partida a manera de "solicitud de pago": un solo pago a un documento o un solo anticipo;
     * b) los pagos "operado (en proceso)" pueden tener varias partidas: uno o más pagos a documentos o uno o más anticipos.
     *
     * @param session Sesión de usuario.
     * @return Lista de pagos de compras exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfPurchasePaymentsToExport(final SGuiSession session) throws SQLException, Exception {
        ArrayList<SExportData> payments = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            // extraer referencias de pedidos de compras de las bases de datos de todas las empresas configuradas para SWAP Services:
            
            HashMap<Integer, String> databasesMap = SExportUtils.getSwapCompaniesDatabasesMap(session);
            
            // iterar sobre las bases de datos de todas las empresas configuradas para SWAP Services:
            for (Integer companyId : databasesMap.keySet()) {
                String database = databasesMap.get(companyId);
                String referenceId = "CONVERT(p.id_pay, CHAR)";
                Date lastSyncDatetime = getLastSyncDatetime(session.getStatement(), SSyncType.PUR_PAYMENT, database);
                
                String sql = "SELECT DISTINCT " // XXX TO-DO: "DISTINCT" no debería ser necesario, se encontró un registro de procesamiento de datos de SWAP Services para el documento PK 2025-14514!!!
                        // payment:
                        + "p.id_pay, p.ser AS _pay_ser, p.num AS _pay_num, CONCAT(p.ser, IF(p.ser = '', '', '-'), p.num) AS _pay_folio, p.dt_app, p.dt_req, p.dt_sched_n, p.dt_exec_n, "
                        + "p.pay_cur, p.pay_exc_rate_app, p.pay_app, p.pay_way, p.priority, p.nts, p.nts_auth, p.b_rcpt_pay_req, p.b_del, p.b_sys, "
                        + "p.fk_st_pay, p.fk_cur AS _pay_cur_id, cp.cur_key AS _pay_cur_key, p.fk_ben, p.fk_func, p.fk_func_sub, "
                        + "p.fk_usr_ins, p.fk_usr_upd, p.fk_usr_sched, p.fk_usr_exec, p.ts_usr_sched, p.ts_usr_exec, "
                        // payment entry:
                        + "pe.ety_tp, pe.ety_pay_cur, pe.ety_pay_app, pe.conv_rate_app, pe.des_pay_app_ety_cur, "
                        + "pe.install, pe.doc_bal_prev_app_cur, pe.doc_bal_unpd_app_cur_r, pe.fk_ety_cur AS _pay_ety_cur_id, cpe.cur_key AS _pay_ety_cur_key, "
                        // bank accounts:
                        + "ba_ac.acc_num, ba_ac.acc_num_std, ba_ac_b.bp, ba_ac_b.fiscal_id, ba_ac_b.fiscal_frg_id, "
                        + "ba_ben.acc_num, ba_ben.acc_num_std, ba_ben_b.bp, ba_ben_b.fiscal_id, ba_ben_b.fiscal_frg_id, "
                        // SWAP data processing:
                        + "sdp.ext_data_id, sdp.ext_data_uuid, sdp.b_dps_pay_loc, "
                        // DPS:
                        + "d.id_year, d.id_doc, d.num_ser AS _dps_ser, d.num AS _dps_num, CONCAT(d.num_ser, IF(d.num_ser = '', '', '-'), d.num) AS _dps_folio, d.dt AS _dps_dt, "
                        + "d.tot_cur_r, d.fid_cur AS _dps_cur_id, cd.cur_key AS _dps_cur_key, "
                        // CFD:
                        + "cfd.uuid AS _cfd_uuid, cfd.fid_tp_cfd, cfd.fid_tp_xml, cfd.fid_st_xml "
                        + "FROM "
                        + database + "." + SModConsts.TablesMap.get(SModConsts.FIN_PAY) + " AS p "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS cp ON cp.id_cur = p.fk_cur "
                        + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.FIN_PAY_ETY) + " AS pe ON pe.id_pay = p.id_pay "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS cpe ON cpe.id_cur = pe.fk_ety_cur "
                        + "LEFT OUTER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.FIN_ACC_CASH) + " AS ac ON ac.id_cob = p.fk_pay_cash_cob_n AND ac.id_acc_cash = p.fk_pay_cash_acc_cash_n "
                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BANK_ACC) + " AS ba_ac ON ba_ac.id_bpb = ac.fid_bpb_n AND ba_ac.id_bank_acc = ac.fid_bank_acc_n "
                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS ba_ac_b ON ba_ac_b.id_bp = ba_ac.fid_bank "
                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BANK_ACC) + " AS ba_ben ON ba_ben.id_bpb = p.fk_ben_bank_cob_n AND ba_ben.id_bank_acc = p.fk_ben_bank_acc_cash_n "
                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS ba_ben_b ON ba_ben_b.id_bp = ba_ben.fid_bank "
                        + "LEFT OUTER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_SWAP_DATA_PRC) + " AS sdp ON sdp.fk_dps_year_n = pe.fk_doc_year_n AND sdp.fk_dps_doc_n = pe.fk_doc_doc_n AND NOT sdp.b_del "
                        + "LEFT OUTER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON d.id_year = pe.fk_doc_year_n AND d.id_doc = pe.fk_doc_doc_n AND NOT d.b_del "
                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS cd ON cd.id_cur = d.fid_cur "
                        + "LEFT OUTER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS cfd ON cfd.fid_dps_year_n = pe.fk_doc_year_n AND cfd.fid_dps_doc_n = pe.fk_doc_doc_n "
                        + "WHERE ("
                        + "((NOT p.b_del) "
                        + "AND " + referenceId + " NOT IN (" + getSqlSubQuerySyncedRegistries(SSyncType.PUR_PAYMENT, database) + "))"
                        + (lastSyncDatetime == null ? "" : " OR (p.ts_usr_upd >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "')")
                        + ") "
                        + "AND ((p.fk_st_pay = " + SModSysConsts.FINS_ST_PAY_NEW + " AND p.b_sys) OR p.fk_st_pay = " + SModSysConsts.FINS_ST_PAY_EXEC_P + ") "
                        + "ORDER BY "
                        + "p.ser, p.num, p.id_pay;";

                int currentPaymentId = 0;
                SExportDataPayment currentPayment = null;
                HashMap<SExportDataPayment, ArrayList<SExportDataPaymentEntry>> paymentEntriesMap = new HashMap<>();
                
                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    // payment:
                    
                    int paymentId = resultSet.getInt("p.id_pay");
                    
                    if (currentPaymentId != paymentId) {
                        currentPaymentId = paymentId;
                        
                        currentPayment = new SExportDataPayment();

                        currentPayment.company = companyId;
                        currentPayment.payment_id = paymentId;
                        currentPayment.functional_area = resultSet.getInt("p.fk_func_sub");
                        currentPayment.benef = resultSet.getInt("p.fk_ben");
                        currentPayment.series = resultSet.getString("_pay_ser");
                        currentPayment.number = resultSet.getString("_pay_num");
                        currentPayment.app_date = SLibUtils.DbmsDateFormatDate.format(resultSet.getDate("p.dt_app"));
                        currentPayment.req_date = SLibUtils.DbmsDateFormatDate.format(resultSet.getDate("p.dt_req"));
                        Date dateScheduled = resultSet.getDate("p.dt_sched_n");
                        currentPayment.sched_date_n = resultSet.wasNull() ? null : SLibUtils.DbmsDateFormatDate.format(dateScheduled);
                        Date dateExecution = resultSet.getDate("p.dt_exec_n");
                        currentPayment.exec_date_n = resultSet.wasNull() ? null : SLibUtils.DbmsDateFormatDate.format(dateExecution);
                        currentPayment.currency = resultSet.getString("_pay_cur_key");
                        currentPayment.amount = SExportUtils.FormatStdAmount.format(resultSet.getDouble("p.pay_cur"));
                        currentPayment.exchange_rate_app = SExportUtils.FormatStdExchangeRate.format(resultSet.getDouble("p.pay_exc_rate_app"));
                        currentPayment.amount_loc_app = SExportUtils.FormatStdAmount.format(resultSet.getDouble("p.pay_app"));
                        currentPayment.exchange_rate_exec = currentPayment.exchange_rate_app; // same value "at application"!
                        currentPayment.amount_loc_exec = currentPayment.amount_loc_app; // same value "at application"!
                        currentPayment.payment_way = resultSet.getString("p.pay_way");
                        currentPayment.priority = resultSet.getInt("p.priority");
                        currentPayment.notes = resultSet.getString("p.nts");
                        currentPayment.is_receipt_payment_req = resultSet.getBoolean("p.b_rcpt_pay_req") ? 1 : 0;
                        
                        int oldStatusPaymentId = resultSet.getInt("p.fk_st_pay");
                        currentPayment.payment_status = SDbPayment.getSettledStatusPaymentId(oldStatusPaymentId);
                        currentPayment.authz_authorization_id = SSwapConsts.AUTHZ_STATUS_PENDING;

                        // paying account:
                        BankAccount payingBankAccount = new BankAccount(resultSet, "ba_ac_b.bp", "ba_ac_b.fiscal_id", "ba_ac_b.fiscal_frg_id", "ba_ac.acc_num", "ba_ac.acc_num_std");
                        currentPayment.paying_bank = payingBankAccount.BankName;
                        currentPayment.paying_bank_fiscal_id = payingBankAccount.BankFiscalId;
                        currentPayment.paying_account = payingBankAccount.BankAccount;

                        // beneficiary bank account:
                        BankAccount benefBankAccount = new BankAccount(resultSet, "ba_ben_b.bp", "ba_ben_b.fiscal_id", "ba_ben_b.fiscal_frg_id", "ba_ben.acc_num", "ba_ben.acc_num_std");
                        currentPayment.benef_bank = benefBankAccount.BankName;
                        currentPayment.benef_bank_fiscal_id = benefBankAccount.BankFiscalId;
                        currentPayment.benef_account = benefBankAccount.BankAccount;

                        //currentPayment.sched_user = null;
                        //currentPayment.sched_at = null;
                        
                        if (oldStatusPaymentId == SModSysConsts.FINS_ST_PAY_EXEC_P) {
                            currentPayment.exec_user = resultSet.getInt("p.fk_usr_exec");
                            currentPayment.exec_at = SLibUtils.DbmsDateFormatDatetime.format(resultSet.getTimestamp("p.ts_usr_exec"));
                        }
                        
                        currentPayment.is_deleted = resultSet.getBoolean("p.b_del") ? 1 : 0;
                        
                        if (((SClientInterface) session.getClient()).isDev()) {
                            currentPayment.user_id = 60; // slopez
                        }
                        else {
                            currentPayment.user_id = resultSet.getInt("p.fk_usr_ins"); // el creador del pago
                        }
                    }

                    // payment entry:

                    SExportDataPaymentEntry paymentEntry = new SExportDataPaymentEntry();

                    paymentEntry.entry_type = resultSet.getString("pe.ety_tp");
                    paymentEntry.amount = SExportUtils.FormatStdAmount.format(resultSet.getDouble("pe.ety_pay_cur"));
                    paymentEntry.amount_loc_app = SExportUtils.FormatStdAmount.format(resultSet.getDouble("pe.ety_pay_app"));
                    paymentEntry.entry_currency = resultSet.getString("_pay_ety_cur_key");
                    //paymentEntry.conv_rate_app = SExportUtils.FormatPayConversionRate.format(resultSet.getDouble("pe.conv_rate_app"));
                    paymentEntry.conv_rate_app = SExportUtils.FormatStdExchangeRate.format(resultSet.getDouble("pe.conv_rate_app"));
                    paymentEntry.entry_amount_app = SExportUtils.FormatStdAmount.format(resultSet.getDouble("pe.des_pay_app_ety_cur"));
                    paymentEntry.amount_loc_exec = paymentEntry.amount_loc_app; // same value "at application"!
                    paymentEntry.conv_rate_exec = paymentEntry.conv_rate_app; // same value "at application"!
                    paymentEntry.entry_amount_exec = paymentEntry.entry_amount_app; // same value "at application"!
                    paymentEntry.installment = resultSet.getInt("pe.install");
                    paymentEntry.document_bal_prev_app = SExportUtils.FormatStdAmount.format(resultSet.getDouble("pe.doc_bal_prev_app_cur"));
                    paymentEntry.document_bal_unpd_app = SExportUtils.FormatStdAmount.format(resultSet.getDouble("pe.doc_bal_unpd_app_cur_r"));
                    paymentEntry.document_bal_prev_exec = paymentEntry.document_bal_prev_app; // same value "at application"!
                    paymentEntry.document_bal_unpd_exec = paymentEntry.document_bal_unpd_app; // same value "at application"!
                    
                    int externalId = resultSet.getInt("sdp.ext_data_id");
                    
                    if (resultSet.wasNull() || paymentEntry.entry_type.equals(SDbPaymentEntry.ENTRY_TYPE_ADVANCE)) {
                        paymentEntry.document_n_id = null;
                        paymentEntry.document_uuid = null;
                        paymentEntry.document_folio = null;
                        paymentEntry.document_date = null;
                        paymentEntry.document_currency = null;
                        paymentEntry.document_amount = null;
                    }
                    else {
                        String uuid = resultSet.getString("_cfd_uuid");
                        
                        if (resultSet.wasNull() || uuid.isEmpty()) {
                            uuid = resultSet.getString("sdp.ext_data_uuid");
                            
                            if (resultSet.wasNull()) {
                                uuid = "";
                            }
                        }
                        
                        paymentEntry.document_n_id = externalId;
                        paymentEntry.document_uuid = uuid;
                        paymentEntry.document_folio = resultSet.getString("_dps_folio");
                        paymentEntry.document_date = SLibUtils.DbmsDateFormatDate.format(resultSet.getDate("_dps_dt"));
                        paymentEntry.document_currency = resultSet.getString("_dps_cur_key");
                        paymentEntry.document_amount = SExportUtils.FormatStdAmount.format(resultSet.getDouble("d.tot_cur_r"));
                    }
                    
                    ArrayList<SExportDataPaymentEntry> entries = paymentEntriesMap.get(currentPayment);
                    
                    if (entries == null) {
                        entries = new ArrayList<>();
                        paymentEntriesMap.put(currentPayment, entries);
                    }
                    
                    entries.add(paymentEntry);
                }
                
                for (SExportDataPayment payment : paymentEntriesMap.keySet()) {
                    SRequestPaymentsBody.Payment data = new SRequestPaymentsBody.Payment();

                    data.payment = payment;
                    data.entries = (SExportDataPaymentEntry[]) paymentEntriesMap.get(payment).toArray(new SExportDataPaymentEntry[0]);
                    data.files = new SExportDataFile[] { };

                    payments.add(data);
                }
            }
        }

        return payments;
    }

    /**
     * Consulta los pagos de compras por actualizar de todas las empresas configuradas para SWAP Services, y los prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @return Lista de pagos de compras por actualizar exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfPurchasePaymentUpdatesToExport(final SGuiSession session) throws SQLException, Exception {
        ArrayList<SExportData> payments = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            // extraer referencias de pedidos de compras de las bases de datos de todas las empresas configuradas para SWAP Services:
            
            HashMap<Integer, String> databasesMap = SExportUtils.getSwapCompaniesDatabasesMap(session);
            String statesToUpdateAllways =
                    SModSysConsts.FINS_ST_PAY_REJC_P + ", "
                    + SModSysConsts.FINS_ST_PAY_SCHED_P + ", "
                    //+ SModSysConsts.FINS_ST_PAY_OPER_P + ", " // no aplica, se exporta como nuevo pago
                    + SModSysConsts.FINS_ST_PAY_SUBR_P + ", " // exportar como "eliminado", se integra a un nuevo pago
                    + SModSysConsts.FINS_ST_PAY_RCPT_P + ", "
                    //+ SModSysConsts.FINS_ST_PAY_BLOC_P + ", " // no aplica
                    + SModSysConsts.FINS_ST_PAY_CANC_P; // exportar como "eliminado"
            
            // iterar sobre las bases de datos de todas las empresas configuradas para SWAP Services:
            
            for (Integer companyId : databasesMap.keySet()) {
                String database = databasesMap.get(companyId);
                /* ¡conservar este bloque por consistencia con las demás consultas, pero en esta funcionalidad NO se necesita!
                String referenceId = "CONVERT(p.id_pay, CHAR)";
                Date lastSyncDatetime = getLastSyncDatetime(session.getStatement(), SSyncType.PUR_PAYMENT_UPD, database);
                */
                
                String sql = "SELECT "
                        // payment:
                        + "p.id_pay, p.ser AS _pay_ser, p.num AS _pay_num, CONCAT(p.ser, IF(p.ser = '', '', '-'), p.num) AS _pay_folio, p.dt_app, p.dt_req, p.dt_sched_n, p.dt_exec_n, "
                        + "p.pay_cur, p.pay_exc_rate_app, p.pay_app, p.pay_way, p.priority, p.nts, p.nts_auth, p.b_rcpt_pay_req, p.b_del, p.b_sys, "
                        + "p.fk_st_pay, p.fk_cur AS _pay_cur_id, cp.cur_key AS _pay_cur_key, p.fk_ben, p.fk_func, p.fk_func_sub, "
                        + "p.fk_usr_ins, p.fk_usr_upd, p.ts_usr_ins, p.ts_usr_upd, p.fk_usr_sched, p.fk_usr_exec, p.ts_usr_sched, p.ts_usr_exec "
                        + "FROM "
                        + database + "." + SModConsts.TablesMap.get(SModConsts.FIN_PAY) + " AS p "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS cp ON cp.id_cur = p.fk_cur "
                        + "WHERE ("
                        + "(/*NOT p.b_del AND */p.fk_st_pay IN (" + statesToUpdateAllways + ")) " // exportar todos los pagos actualizables, incluso los eliminados
                        /* ¡conservar este bloque por consistencia con las demás consultas, pero en esta funcionalidad NO se necesita!
                        + "AND " + referenceId + " NOT IN (" + getSqlSubQuerySyncedRegistries(SSyncType.PUR_PAYMENT_UPD, database) + "))"
                        + (lastSyncDatetime == null ? "" : " OR (" + referenceId + " IN (" + getSqlSubQuerySyncedRegistries(SSyncType.PUR_PAYMENT_UPD, database) + ") AND "
                        + "(p.ts_usr_upd >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' AND p.b_del))")
                        */
                        + ") "
                        + " "
                        + "ORDER BY "
                        + "p.ser, p.num, p.id_pay;";

                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    // payment:
                    
                    SExportDataPaymentUpdate paymentUpdate = new SExportDataPaymentUpdate();

                    paymentUpdate.company = companyId;
                    
                    int paymentId = resultSet.getInt("p.id_pay");
                    
                    paymentUpdate.payment_id = paymentId;
                    
                    int oldStatusPaymentId = resultSet.getInt("p.fk_st_pay");
                    
                    switch (oldStatusPaymentId) {
                        case SModSysConsts.FINS_ST_PAY_SCHED_P:
                            Date dateScheduled = resultSet.getDate("p.dt_sched_n");
                            if (!resultSet.wasNull()) {
                                boolean isSchedule = resultSet.getTimestamp("p.ts_usr_upd").equals(resultSet.getTimestamp("p.ts_usr_sched")); // both TS of update and schedule are the same when payment was scheduled!
                                
                                paymentUpdate.sched_date_n = SLibUtils.DbmsDateFormatDate.format(dateScheduled);
                                paymentUpdate.sched_user = resultSet.getInt("p.fk_usr_sched");
                                paymentUpdate.sched_at = SLibUtils.DbmsDateFormatDatetime.format(resultSet.getTimestamp("p.ts_usr_sched"));
                                
                                if (isSchedule) {
                                    // only if payment is scheduled, excluding a reschedule, export as well authorization data (that happens to be the same as the corresponding schedule data!):
                                    paymentUpdate.authorized_by = resultSet.getInt("p.fk_usr_upd");
                                    paymentUpdate.authorized_at = SLibUtils.DbmsDateFormatDatetime.format(resultSet.getTimestamp("p.ts_usr_upd"));
                                }
                            }
                            else {
                                Logger.getLogger(SExportUtils.class.getName()).log(Level.INFO,
                                        "Pago omitido (fecha programación nula): ID = {0}.",
                                        new Object[] { paymentId });
                                continue; // omitir pago inconsistente
                            }
                            break;
                            
                        case SModSysConsts.FINS_ST_PAY_REJC_P:
                            // if payment is rejected, export as well authorization data (that happens to be the same as the corresponding schedule data!):
                            paymentUpdate.authorized_by = resultSet.getInt("p.fk_usr_upd");
                            paymentUpdate.authorized_at = SLibUtils.DbmsDateFormatDatetime.format(resultSet.getTimestamp("p.ts_usr_upd"));
                            break;
                            
                        //case SModSysConsts.FINS_ST_PAY_OPER_P:
                        case SModSysConsts.FINS_ST_PAY_SUBR_P:
                        case SModSysConsts.FINS_ST_PAY_RCPT_P:
                        //case SModSysConsts.FINS_ST_PAY_BLOC_P:
                        case SModSysConsts.FINS_ST_PAY_CANC_P:
                            break;
                            
                        default:
                            // nothing
                    }
                    
                    int newStatusPaymentId = SDbPayment.getSettledStatusPaymentId(oldStatusPaymentId);
                    
                    if (newStatusPaymentId != 0) {
                        paymentUpdate.payment_status = newStatusPaymentId;
                    }
                    
                    boolean exportAsDeleted = SDbPayment.exportStatusPaymentAsDeleted(oldStatusPaymentId);
                    
                    paymentUpdate.is_deleted = exportAsDeleted || resultSet.getBoolean("p.b_del") ? 1 : 0;
                    
                    if (paymentUpdate.is_deleted == 1) {
                        paymentUpdate.deleted_by = resultSet.getInt("p.fk_usr_upd");
                    }

                    payments.add(paymentUpdate);
                }
            }
        }

        return payments;
    }

    /**
     * Obtiene la lista de usuarios o proveedores a exportar según el tipo de sincronización.
     *
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @return Lista de datos a exportar.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public static ArrayList<SExportData> getDataToExport(final SGuiSession session, final SSyncType syncType) throws SQLException, Exception {
        ArrayList<SExportData> data = null;
        
        switch (syncType) {
            case USER:
                data = getListOfUsersToExport(session);
                break;
                
            case PARTNER_SUPPLIER:
                if (((SClientInterface) session.getClient()).isDev()) {
                    data = getListOfPartnerSuppliersToExportAeth(session);
                }
                else {
                    data = getListOfPartnerSuppliersToExport(session);
                }
                break;
                
            case PARTNER_CUSTOMER:
                break;
                
            case AUTH_ACTOR:
                data = getListOfAuthActorsToExport(session);
                break;
                
            case AUTH_JOB_TITLE:
                data = getListOfAuthJobTitlesToExport(session);
                break;
                
            case FUNCTIONAL_AREA:
                data = getListOfFunctionalAreasToExport(session);
                break;
                
            case PUR_ORDER:
                exportPurchaseOrdersFiles(session);
                data = getListOfPurchaseOrdersToExport(session);
                break;
                
            case PUR_REF_ORDER:
                if (((SClientInterface) session.getClient()).isDev()) {
                    data = getListOfPurchaseOrderRefsToExportAeth(session);
                }
                else {
                    data = getListOfPurchaseOrderRefsToExport(session);
                }
                break;
                
            case PUR_PAYMENT:
                data = getListOfPurchasePaymentsToExport(session);
                break;
                
            case PUR_PAYMENT_UPD:
                data = getListOfPurchasePaymentUpdatesToExport(session);
                break;
                
            default:
                throw new IllegalArgumentException(SExportUtils.ERR_UNSUPPORTED_SYNC_TYPE + "'" + syncType + "'.");
        }
        
        return data;
    }

    /**
     * Obtiene un proveedor específico por su ID fiscal.
     * 
     * @param statement Objeto Statement para ejecutar la consulta.
     * @param fiscalId ID fiscal del proveedor a buscar.
     * @return Un objeto <code>SExportDataUser</code> con los datos del proveedor, o <code>null</code> si no se encuentra.
     */
    public static SExportDataUser getSupplierByFiscalId(final Statement statement, final String fiscalId) {
        SExportDataUser user = null;
        
        String sql = getSqlQueryBasePartnerSuppliers()
                + "WHERE "
                + "b.fiscal_id = '" + fiscalId + "' "
                + "AND NOT b.b_del AND NOT bc.b_del "
                + "ORDER BY b.id_bp " // primero el registro más antiguo
                + "LIMIT 1;";
        try {
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    Pattern rfcPattern = DCfdUtils.createRfcPattern();
                    user = createUserForPartnerSupplier(resultSet, rfcPattern);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return user;
    }
    
    /**
     * Update status of authorizable resource.
     * @param statement
     * @param companyDbName
     * @param resourceType
     * @param resourceId
     * @param authStatusId
     * @param userId
     * @param notes
     * @param newAmount
     * @param newDate
     * @return 
     */
    public static SResourceStatusResponse updateResourceStatus(final Statement statement, final String companyDbName, final int resourceType, final String resourceId, final int authStatusId, 
            final int userId, final String notes, final double newAmount, final String newDate) {
        SResourceStatusResponse oResponse;
        
        try {
            oResponse = new SResourceStatusResponse();
            String sTable = "";
            String sWhere = "";
            String sUpdate = "";
            String sSecondQuery = "";
            
            switch (resourceType) {
                case SSwapConsts.RESOURCE_TYPE_PUR_PAYMENT:
                    sTable = SModConsts.TablesMap.get(SModConsts.FIN_PAY);
                    
                    switch (authStatusId) {
                        case SSwapConsts.AUTHZ_STATUS_OK:
                            sUpdate = "fk_st_pay = " + SModSysConsts.FINS_ST_PAY_SCHED_P + ", "
                                    + "fk_usr_sched = " + userId + ", "
                                    + "ts_usr_sched = NOW(), ";
                            if (newAmount > 0) {
                                // en partida: des_pay_app_ety_cur
                                sSecondQuery = "UPDATE " + companyDbName + "." + SModConsts.TablesMap.get(SModConsts.FIN_PAY_ETY) + " SET "
                                        + "des_pay_app_ety_cur = " + newAmount + " "
                                        + "WHERE (id_pay = " + resourceId + ") and (id_ety = 1);";
                            }
                            if (newDate != null && !newDate.isEmpty()) {
                                sUpdate += "dt_sched_n = '" + newDate + "', ";
                            }
                            else {
                                sUpdate += "dt_sched_n = dt_req, ";
                            }
                            break;
                            
                        case SSwapConsts.AUTHZ_STATUS_REJECTED:
                            sUpdate = "fk_st_pay = " + SModSysConsts.FINS_ST_PAY_REJC_P  + ", "
                                    + "dt_sched_n = NULL, "
                                    + "fk_usr_sched = " + SUtilConsts.USR_NA_ID + ", "
                                    + "ts_usr_sched = NOW(), "; // XXX se puede actualizar este TS o no en el rechazo, por lo pronto se deja
                            break;
                            
                        default:
                            oResponse.status_code = HttpURLConnection.HTTP_BAD_REQUEST;
                            oResponse.message = "El tipo de estatus de autorización es desconocido (" + authStatusId + ").";
                            oResponse.error = "Tipo de estatus de autorización desconocido.";

                            return oResponse;
                    }
                    
                    sUpdate += "nts_auth_flow = '" + SLibUtils.textToSql(notes) + "', "
                            + "fk_usr_upd = " + userId + ", "
                            + "ts_usr_upd = NOW() ";
                    
                    sWhere = "WHERE id_pay = " + resourceId;
                    break;

                default:
                    oResponse.status_code = HttpURLConnection.HTTP_BAD_REQUEST;
                    oResponse.message = "No se encontró tipo de recurso.";
                    oResponse.error = "No se encontró tipo de recurso.";

                    return oResponse;
            }

            String sql = "UPDATE " + companyDbName + "." + sTable + " SET " + sUpdate + sWhere + ";";
            Logger.getLogger(SExportDataUtils.class.getName()).log(Level.INFO, "ACTUALIZAR PAGO, company: {0}. QUERY: {1} ", new Object[]{companyDbName, sql});

            // Iniciar transacción
            Connection conn = statement.getConnection();
            boolean autoCommit = conn.getAutoCommit();
            try {
                conn.setAutoCommit(false);

                int res = statement.executeUpdate(sql);
                if (sSecondQuery != null && !sSecondQuery.isEmpty()) {
                    Logger.getLogger(SExportDataUtils.class.getName()).log(Level.INFO, "ACTUALIZAR PARTIDA PAGO, company: {0}. QUERY{1}: ", new Object[]{companyDbName, sSecondQuery});
                    res = statement.getConnection().createStatement().executeUpdate(sSecondQuery);
                }
                if (res != 1) {
                    conn.rollback();
                    oResponse.status_code = HttpURLConnection.HTTP_INTERNAL_ERROR;
                    oResponse.message = "No se realizó ninguna actualización.";
                    oResponse.error = "No se realizó ninguna actualización.";
                }
                else {
                    conn.commit();
                    oResponse.status_code = HttpURLConnection.HTTP_OK;
                    oResponse.message = "OK";
                }
            }
            catch (SQLException ex) {
                Logger.getLogger(SExportDataUtils.class.getName()).log(Level.SEVERE, null, ex);
                conn.rollback();
                throw ex;
            }
            finally {
                conn.setAutoCommit(autoCommit);
            }
        }
        catch (SQLException ex) {
            oResponse = new SResourceStatusResponse();
            oResponse.status_code = HttpURLConnection.HTTP_INTERNAL_ERROR;
            oResponse.message = "Error al actualizar el estatus del recurso.";
            oResponse.error = ex.getMessage();
            Logger.getLogger(SExportDataUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return oResponse;
    }
    
    public static class BankAccount {
        
        public String BankName;
        public String BankFiscalId;
        public String BankAccount;
        
        public BankAccount(final ResultSet resultSet, final String colName, final String colFiscalId, final String colFiscalFrgId, final String colAccount, final String colAccountStd) throws SQLException {
            BankName = resultSet.getString(colName);
            BankFiscalId = "";
            BankAccount = "";

            if (resultSet.wasNull()) {
                BankName = "";
            }
            else {
                // note that remaining data cannot be null:
                
                BankFiscalId = resultSet.getString(colFiscalFrgId); // check first foreign fiscal ID
                if (BankFiscalId.isEmpty()) {
                    BankFiscalId = resultSet.getString(colFiscalId);
                }
                
                BankAccount = resultSet.getString(colAccountStd); // check first standard account number (ClaBE)
                if (BankAccount.isEmpty()) {
                    BankAccount = resultSet.getString(colAccount);
                }
            }
        }
    }
}
