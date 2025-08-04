/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import cfd.ver40.DCfdi40Catalogs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.SClient;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.cfg.db.SDbFunctionalSubArea;
import erp.mod.cfg.db.SDbSyncLog;
import erp.mod.cfg.db.SDbSyncLogEntry;
import erp.mod.cfg.db.SSyncType;
import erp.mod.cfg.utils.SAuthJsonUtils;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 * Utilidades para exportación de datos de usuarios y proveedores en formato JSON.
 * 
 * Esta clase contiene métodos para consultar la base de datos y generar 
 * estructuras JSON usando Jackson, facilitando la integración y exportación de 
 * información con otros sistemas.
 * 
 * @author Edwin Carmona, Sergio Flores
 */
public abstract class SExportUtils {
    
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final ZoneId MEXICO = java.time.ZoneId.of("America/Mexico_City");
    
    private static final int HTTP_CODE_OK = 200;
    private static final int HTTP_CODE_CREATED = 201;
    private static final int HTTP_CODE_PENDING = 202;
    private static final int HTTP_CODE_NO_CONTENT = 204;
    private static final int HTTP_CODE_INVALID_REQUEST = 400;
    
    private static final int SEC_PSWD_LEN = 10;
    private static final int TIME_60_SEC = 60 * 1000; // 60 segundos en milisegundos
    
    private static final String ERR_UNKNOWN_SYNC_TYPE = "Tipo de sincronización no soportado: ";

    private static final String SqlQueryBasePartnerSuppliers = "SELECT "
            + "b.id_bp, b.bp, b.lastname, b.firstname, b.bp_comm, "
            + "b.fiscal_id, b.fiscal_frg_id, b.fid_tp_bp_idy, b.b_del, "
            + "bc.b_del, bc.tax_regime, bba.fid_cty_n, cty.cty_code, bbc.email_01 "
            + "FROM "
            + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON bc.id_bp = b.id_bp AND bc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_SUP + " "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bb ON bb.fid_bp = b.id_bp = AND bb.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB_CON) + " AS bbc ON bbc.id_bpb = bb.id_bpb AND bbc.id_con = " + SUtilConsts.BRA_CON_ID + " "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB_ADD) + " AS bba ON bba.id_bpb = bb.id_bpb AND bba.id_add = " + SUtilConsts.BRA_ADD_ID + " "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOCU_CTY) + " AS cty ON cty.id_cty = bba.fid_cty_n ";
    
    /**
     * Obtiene una cadena de texto con los ID de las emresas configuradas para SWAP Services para consultas SQL.
     *
     * @param session Sesión de usuario.
     * @return Cadena de texto con los ID de las emresas.
     */
    public static String getSwapCompaniesForSqlQuery(final SGuiSession session) {
        int[] companies = (int[]) ((SClient) session.getClient()).getSwapServicesSetting(SSwapConsts.CFG_NVP_COMPANIES);
        return Arrays.stream(companies).mapToObj(String::valueOf).collect(Collectors.joining(", "));
    }

    /**
     * Obtiene un mapa de los nombres de las bases de datos de las emresas configuradas para SWAP Services.
     *
     * @param session Sesión de usuario.
     * @return Mapa de los nombres de las bases de datos: key = company ID; value = database name.
     */
    public static HashMap<Integer, String> getSwapCompaniesDatabasesMap(final SGuiSession session) throws SQLException {
        HashMap<Integer, String> databasesMap = new HashMap<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String sql = "SELECT id_co, bd "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_CO) + " "
                    + "WHERE id_co IN (" + getSwapCompaniesForSqlQuery(session) + ") "
                    + "ORDER BY id_co;";
            
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                databasesMap.put(resultSet.getInt("id_co"), resultSet.getString("co"));
            }
        }
        
        return databasesMap;
    }
    
    /**
     * Genera una contraseña segura de 10 caracteres aleatorios.
     * Incluye letras ASCII mayúsculas, minúsculas, números y carácteres especiales.
     * 
     * @return Contraseña segura generada.
     */
    private static String generateSecurePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.!?@#$%^&*()-_=+";
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();
        
        for (int i = 0; i < SEC_PSWD_LEN; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }
        
        return password.toString();
    }

    /**
     * Obtiene la fecha de la última sincronización exitosa para el tipo de sincronización indicado.
     * 
     * @param statement Statement para ejecutar la consulta.
     * @param syncType Tipo de sincronización.
     * @return Fecha de la última sincronización exitosa, o null si no existe.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static Date getLastSyncDatetime(final Statement statement, final SSyncType syncType) throws SQLException, Exception {
        int table = 0;
        
        switch (syncType) {
            case FUNCTIONAL_AREA:
            case USER:
            case PARTNER_SUPPLIER:
                table = SModConsts.CFG_SYNC_LOG;
                break;
                
            case PURCHASE_ORDER_REF:
            case SCALE_TICKET_REF:
                table = SModConsts.CFG_COM_SYNC_LOG;
                break;
                
            default:
                throw new IllegalArgumentException(ERR_UNKNOWN_SYNC_TYPE + "'" + syncType + "'.");
        }
        
        Date datetime = null;
        
        String sql = "SELECT request_timestamp "
                + "FROM " + SModConsts.TablesMap.get(table) + " "
                + "WHERE response_code = '" + HTTP_CODE_OK + "' "
                + "AND sync_type = '" + syncType + "';";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                datetime = resultSet.getTimestamp("request_timestamp");
            }
        }
        
        return datetime;
    }

    /**
     * Consulta las áreas funcionales de todas las empresas configuradas para SWAP Services y los prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @param lastSyncDatetime Fecha-hora de última sincronización (puede ser <code>null</code>).
     * @return Lista de usuarios exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfFunctionalAreasToExport(final SGuiSession session, final Date lastSyncDatetime) throws SQLException {
        ArrayList<SExportData> functionalAreas = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            // extraer áreas funcionales de las bases de datos de todas las empresas configuradas para SWAP Services:
            
            HashMap<Integer, String> databasesMap = getSwapCompaniesDatabasesMap(session);
            
            // iterar sobre las bases de datos de todas las empresas configuradas para SWAP Services:
            
            for (Integer companyId : databasesMap.keySet()) {
                String database = databasesMap.get(companyId);
                
                String sql = "SELECT "
                        + "fs.id_func_sub AS external_id, "
                        + "fs.code AS code, CONCAT(f.code, '" + SDbFunctionalSubArea.SEPARATOR + "', fs.name) AS name, fs.b_del OR f.b_del AS is_deleted "
                        + "FROM "
                        + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC_SUB) + " AS fs "
                        + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS f ON f.id_func = fs.fk_func "
                        + "LEFT OUTER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFG_SYNC_LOG_ETY) + " AS sle ON sle.reference_id = fs.id_func_sub "
                        + "AND (sle.response_code = '" + HTTP_CODE_OK + "' OR sloge.response_code = '" + HTTP_CODE_CREATED + "') "
                        + "LEFT OUTER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFG_SYNC_LOG) + " AS sl ON sl.id_sync_log = sle.id_sync_log "
                        + "AND sl.sync_type = '" + SSyncType.FUNCTIONAL_AREA + "' "
                        + "WHERE "
                        + "(sle.ts_sync IS NULL";

                if (lastSyncDatetime != null) {
                    sql += " OR sle.ts_sync >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "'";
                }

                sql += ") ";

                if (lastSyncDatetime == null) {
                    sql += "AND NOT fs.b_del AND NOT f.b_del";
                }
                else {
                    sql += "AND (fs.ts_usr_upd >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' "
                            + "OR f.ts_usr_upd >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "')";
                }
                
                sql += ";";

                ResultSet resultSet = statement.executeQuery(sql);
                
                while (resultSet.next()) {
                    // crear e inicializar el objeto para exportación de datos:
                    
                    SExportDataFunctionalArea functionalArea = new SExportDataFunctionalArea();
                    
                    functionalArea.code = SJsonUtils.sanitizeJson(resultSet.getString("code"));
                    functionalArea.name = SJsonUtils.sanitizeJson(resultSet.getString("name"));
                    functionalArea.external_company_id = companyId;
                    functionalArea.external_id = resultSet.getInt("external_id");
                    functionalArea.is_deleted = resultSet.getBoolean("is_deleted");

                    functionalAreas.add(functionalArea);
                }
            }
        }
        
        return functionalAreas;
    }

    /**
     * Consulta los usuarios y los prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @param lastSyncDatetime Fecha-hora de última sincronización (puede ser <code>null</code>).
     * @return Lista de usuarios exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfUsersToExport(final SGuiSession session, final Date lastSyncDatetime) throws SQLException {
        ArrayList<SExportData> users = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String sql = "SELECT "
                    + "u.id_usr, u.usr, u.email, u.b_act, u.b_del, u.fid_bp_n, "
                    + "b.bp, b.lastname, b.firstname "
                    + "FROM "
                    + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS u "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON b.id_bp = u.fid_bp_n "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFG_SYNC_LOG_ETY) + " AS sle ON sle.reference_id = u.id_usr "
                    + "AND (sle.response_code = '" + HTTP_CODE_OK + "' OR sloge.response_code = '" + HTTP_CODE_CREATED + "') "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFG_SYNC_LOG) + " AS sl ON slog.id_sync_log = sle.id_sync_log "
                    + "AND slog.sync_type = '" + SSyncType.USER + "' "
                    + "WHERE "
                    + "(sle.ts_sync IS NULL";

            if (lastSyncDatetime != null) {
                sql += " OR sle.ts_sync >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "'";
            }

            sql += ") ";

            if (lastSyncDatetime == null) {
                sql += "AND NOT u.b_del AND (b.b_del IS NULL OR NOT b.b_del)";
            }
            else {
                sql += "AND (u.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' "
                        + "OR (b.ts_edit IS NOT NULL AND b.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "'))";
            }
            
            sql += ";";
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                int userId = resultSet.getInt("u.id_usr");
                
                // validar que el usuario tenga nombre y correo:
                
                if (resultSet.getString("u.usr").isEmpty() || !resultSet.getString("u.email").contains("@")) {
                    Logger.getLogger(SExportUtils.class.getName()).log(Level.INFO,
                            "Usuario omitido (nombre o correo): ID = {0}; username = '{1}'; email = '{2}'.",
                            new Object[] { userId, resultSet.getString("u.usr"), resultSet.getString("u.email") });
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
                            "Usuario omitido (nombre(s) o apellido(s)): ID = {0}; last_name = '{1}'; first_name = '{2}'.",
                            new Object[] { userId, lastName, firstName });
                    continue; // omitir usuario inválido
                }

                // crear e inicializar el objeto para exportación de datos:
                
                SExportDataUser user = new SExportDataUser();
                
                user.username = resultSet.getString("u.usr");
                user.email = resultSet.getString("u.email");
                user.password = generateSecurePassword();
                user.is_active = resultSet.getBoolean("u.b_act");
                user.first_name = firstName;
                user.last_name = lastName;

                SExportDataUser.Attributes attributes = new SExportDataUser.Attributes();
                attributes.full_name = SJsonUtils.sanitizeJson(firstName + " " + lastName);
                attributes.user_type = SSwapConsts.USER_TYPE_INTERNAL;
                attributes.is_deleted = resultSet.getBoolean("u.b_del");
                attributes.external_id = userId;
                attributes.other_emails = null; // no soportado en usuarios
                user.attributes = attributes;
                
                ArrayList<Integer> roles = SUserUtils.getUserRoles(session, userId);
                user.groups = roles.stream().mapToInt(Integer::intValue).toArray();

                ArrayList<Integer> companies = SUserUtils.getUserAccesibleCompanies(session, userId);
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
     * Consulta los proveedores y los prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @param lastSyncDatetime Fecha-hora de última sincronización (puede ser <code>null</code>).
     * @return Lista de proveedores exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfPartnerSuppliersToExport(final SGuiSession session, final Date lastSyncDatetime) throws SQLException {
        ArrayList<SExportData> users = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String sql = SqlQueryBasePartnerSuppliers
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFG_SYNC_LOG_ETY) + " AS sle ON sle.reference_id = b.id_bp "
                    + "AND (sloge.response_code = '" + HTTP_CODE_OK + "' OR sloge.response_code = '" + HTTP_CODE_CREATED + "') "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFG_SYNC_LOG) + " AS sl ON sl.id_sync_log = sle.id_sync_log "
                    + "AND sl.sync_type = '" + SSyncType.PARTNER_SUPPLIER + "' "
                    + "WHERE "
                    + "b.b_sup AND b.fiscal_id <> '' "
                    + "AND (sle.ts_sync IS NULL";

            if (lastSyncDatetime != null) {
                sql += "OR sle.ts_sync >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "'";
            }

            sql += ") ";

            if (lastSyncDatetime == null) {
                sql += "AND NOT b.b_del AND NOT bc.b_del;";
            }
            else {
                sql += "AND (b.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' "
                        + "OR bc.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "')";
            }
            
            sql += ";";
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                int partnerId = resultSet.getInt("b.id_bp");
                String partnerName = SJsonUtils.sanitizeJson(resultSet.getString("b.bp"));
                String commercialName = SJsonUtils.sanitizeJson(resultSet.getString("b.bp_comm"));
                String lastName = SJsonUtils.sanitizeJson(resultSet.getString("b.lastname"));
                String firstName = SJsonUtils.sanitizeJson(resultSet.getString("b.firstname"));
                String fiscalId = SJsonUtils.sanitizeJson(resultSet.getString("b.fiscal_id"));
                String foreignFiscalId = SJsonUtils.sanitizeJson(resultSet.getString("b.fiscal_frg_id"));
                String countryCode = SJsonUtils.sanitizeJson(resultSet.getString("cty.cty_code"));
                String taxRegime = resultSet.getString("bc.tax_regime"); // no requiere sanitización!
                String[] emails = resultSet.getString("bbc.email_01").split(";");
                String email = emails.length > 0 ? SLibUtils.textTrim(emails[0]) : "";
                boolean isPerson = resultSet.getInt("b.fid_tp_bp_idy") == SDataConstantsSys.BPSS_TP_BP_IDY_PER;
                
                String username;
                
                if (resultSet.wasNull()) {
                    // nombre de usuario proveedor nacional = ID fiscal nacional:
                    
                    countryCode = DCfdi40Catalogs.ClavePaísMex;
                    
                    username = fiscalId;
                }
                else {
                    // nombre de usuario proveedor extranjero = código de país + '.' + ID fiscal extrangero:
                    
                    if (foreignFiscalId.isEmpty()) {
                        Logger.getLogger(SExportUtils.class.getName()).log(Level.INFO,
                                "Usuario omitido (ID fiscal de asociado de negocios del extranjero): ID = {0}; name = '{1}'; fiscal_id = '{2}'.",
                                new Object[] { partnerId, partnerName, foreignFiscalId});
                        continue; // omitir usuario inválido
                    }
                    
                    username = countryCode + "." + foreignFiscalId;
                }

                SExportDataUser user = new SExportDataUser();
                
                user.username = username;
                user.email = email;
                user.password = generateSecurePassword();
                user.is_active = true;
                user.first_name = firstName;
                user.last_name = lastName;
                
                String otherEmails = "";
                if (emails.length > 1) {
                    for (int i = 1; i < emails.length; i++) {
                        otherEmails += (otherEmails.isEmpty() ? "" : ";") + emails[i];
                    }
                }
                
                SExportDataUser.Attributes attributes = new SExportDataUser.Attributes();
                attributes.full_name = partnerName;
                attributes.user_type = SSwapConsts.USER_TYPE_EXTERNAL;
                attributes.is_deleted = resultSet.getBoolean("b.b_del") || resultSet.getBoolean("bc.b_del");
                attributes.external_id = partnerId;
                attributes.other_emails = !otherEmails.isEmpty() ? otherEmails : null;
                user.attributes = attributes;

                SExportDataUser.Partner partner = new SExportDataUser.Partner();
                partner.is_vendor = true;
                partner.fiscal_id = fiscalId;
                partner.foreign_fiscal_id = foreignFiscalId;
                partner.full_name = partnerName;
                partner.entity_type = isPerson ? SSwapConsts.PARTNER_ENTITY_TYPE_PER : SSwapConsts.PARTNER_ENTITY_TYPE_ORG;
                partner.country_code = countryCode;
                partner.trade_name = commercialName;
                partner.tax_regime_code = taxRegime;
                partner.external_id = partnerId;
                partner.partner_mail = user.email;
                user.partner = partner;

                user.groups = new int[] { SSwapConsts.ROL_SUPPLIER };

                users.add(user);
            }
        }

        return users;
    }

    /**
     * Consulta las referencias de pedidos de compras y las prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @param lastSyncDatetime Fecha-hora de última sincronización (puede ser <code>null</code>).
     * @param period Periodo de la exportación (fechas inicial y final.)
     * @return Lista de órdenes de compras exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfPurchaseOrderRefsToExport(final SGuiSession session, final Date lastSyncDatetime, final Date[] period) throws SQLException {
        String sql = "SELECT d.id_year, d.id_doc, d.b_del, d.fid_st_dps, "
                + "IF(d.num_ser = '', d.num, CONCAT(d.num_ser, '-', d.num)) AS ref, d.dt, d.fid_cur, d.tot_cur_r, d.fid_bp_r "
                + "FROM "
                + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFG_COM_SYNC_LOG_ETY) + " AS sle ON sle.reference_id = CONCAT(d.id_year, '-', d.id_doc) "
                + "AND (sloge.response_code = '" + HTTP_CODE_OK + "' OR sloge.response_code = '" + HTTP_CODE_CREATED + "') "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFG_COM_SYNC_LOG) + " AS sl ON sl.id_sync_log = sle.id_sync_log "
                + "AND sl.sync_type = '" + SSyncType.PURCHASE_ORDER_REF + "' "
                + "WHERE "
                + "d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] + " "
                + "AND d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(period[0]) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(period[1]) + "' "
                + "AND (sle.ts_sync IS NULL";

        if (lastSyncDatetime != null) {
            sql += "OR sle.ts_sync >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "'";
        }
        
        sql += ") ";
        
        if (lastSyncDatetime == null) {
            sql += "AND NOT d.b_del AND d.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + "";
        }
        else {
            sql += "AND d.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "'";
        }

        sql += ";";

        ArrayList<SExportData> references = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                SExportDataReference reference = new SExportDataReference();

//                reference.external_company_id = companyId;
                reference.transaction_class_id = resultSet.getInt("txn_category");
                reference.document_ref_type_id = resultSet.getInt("ref_type");
                reference.external_partner_id = resultSet.getInt("fid_bp_r");
                reference.reference = resultSet.getString("ref");
                reference.date = SLibUtils.DbmsDateFormatDate.format(resultSet.getDate("dt"));
                reference.currency_code = resultSet.getInt("fid_cur");
                reference.amount = resultSet.getDouble("tot_cur_r");

                references.add(reference);
            }
        }

        return references;
    }

    /**
     * Obtiene la lista de usuarios o proveedores a exportar según el tipo de sincronización.
     *
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param lastSyncDate Fecha de última sincronización exitosa (puede ser <code>null</code>).
     * @param period Periodo de la exportación (fechas inicial y final.)
     * @param companyId ID de la empresa de la sesión de usuario.
     * @return Lista de datos a exportar.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getDataToExport(final SGuiSession session, final SSyncType syncType, final Date lastSyncDate, final int companyId, final Date[] period) throws SQLException {
        switch (syncType) {
            case FUNCTIONAL_AREA:
                return getListOfUsersToExport(session, lastSyncDate);
                
            case USER:
                return getListOfUsersToExport(session, lastSyncDate);
                
            case PARTNER_SUPPLIER:
                return getListOfPartnerSuppliersToExport(session, lastSyncDate);
                
            case PURCHASE_ORDER_REF:
                return getListOfPurchaseOrderRefsToExport(session, lastSyncDate, period);
                
            default:
                throw new IllegalArgumentException(ERR_UNKNOWN_SYNC_TYPE + "'" + syncType + "'.");
        }
    }

    /**
     * Realiza una solicitud HTTP a un servicio de intercambio de datos.
     * 
     * @param sQuery Parámetros de consulta para la URL (opcional).
     * @param sURL URL del servicio al que se realiza la solicitud.
     * @param sMethod Método HTTP a utilizar (GET, POST, etc.).
     * @param sBody Cuerpo de la solicitud (para métodos como POST).
     * @param sToken Token de autorización (opcional).
     * @return Respuesta del servicio en formato JSON.
     * @throws Exception Si ocurre un error durante la solicitud.
     */
    private static String requestSwapService(final String sQuery, final String sURL, final String sMethod, final String sBody, final String sToken) throws Exception {
        String charset = java.nio.charset.StandardCharsets.UTF_8.name();
        HttpURLConnection connection = null;
        String responseBody = "";

        try {
            URL url;
            if ("GET".equalsIgnoreCase(sMethod) && sQuery != null && !sQuery.isEmpty()) {
                url = new URL(sURL + "?" + sQuery);
            }
            else {
                url = new URL(sURL);
            }

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIME_60_SEC); // 30 segundos para conectar
            connection.setReadTimeout(TIME_60_SEC);    // 30 segundos para leer respuesta
            connection.setRequestMethod(sMethod.toUpperCase());
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            if (sToken != null && !sToken.isEmpty()) {
                connection.setRequestProperty("Authorization", sToken);
            }
            connection.setDoInput(true);

            // Para métodos que envían datos (POST, PUT, etc.)
            boolean isBodySent = false;
            if (!"GET".equalsIgnoreCase(sMethod)) {
                if (sBody != null && !sBody.trim().isEmpty()) {
                    // Validar que el body sea un JSON válido
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.readTree(sBody); // Lanza excepción si no es JSON válido
                    }
                    catch (JsonProcessingException ex) {
                        throw new IllegalArgumentException("El parámetro 'sBody' no es un JSON válido.", ex);
                    }
                    connection.setDoOutput(true);
                    try (java.io.OutputStream os = connection.getOutputStream()) {
                        byte[] input = sBody.getBytes(charset);
                        os.write(input, 0, input.length);
                    }
                    isBodySent = true;
                }
                else if (sQuery != null && !sQuery.isEmpty()) {
                    connection.setDoOutput(true);
                    try (java.io.OutputStream os = connection.getOutputStream()) {
                        byte[] input = sQuery.getBytes(charset);
                        os.write(input, 0, input.length);
                    }
                    isBodySent = true;
                }
            }

            int status = connection.getResponseCode();
            InputStream responseStream = (status >= HTTP_CODE_OK && status < HTTP_CODE_INVALID_REQUEST) ? connection.getInputStream() : connection.getErrorStream();

            try (Scanner scanner = new Scanner(responseStream, charset)) {
                responseBody = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            }

            System.out.println("Respuesta desde " + sURL);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        
        return responseBody;
    }

    /**
     * Obtiene el nodo 'external_id' de un resultado JSON, buscando en diferentes niveles.
     *
     * @param result Nodo JSON de resultado.
     * @return Nodo JSON con el external_id o null si no existe.
     */
    private static JsonNode getExternalIdNode(final JsonNode result) {
        JsonNode attributes = result.path("attributes");
        if (attributes.isObject() && attributes.has("external_id")) {
            return attributes.path("external_id");
        }
        
        JsonNode input = result.path("input");
        if (input.isObject()) {
            JsonNode attrInput = input.path("attributes");
            if (attrInput.isObject() && attrInput.has("external_id")) {
                return attrInput.path("external_id");
            }
        }
        
        return null;
    }

    /**
     * Parsea la respuesta JSON del servicio de sincronización y genera los registros de log.
     *
     * @param responseJson Respuesta JSON del servicio.
     * @return Lista de entradas de log para la sincronización.
     */
    private static ArrayList<SDbSyncLogEntry> parseSyncLogEntries(final JsonNode responseJson) {
        ArrayList<SDbSyncLogEntry> entries = new ArrayList<>();
        
        if (SAuthJsonUtils.containsElement(responseJson, "", "results")) {
            JsonNode results = responseJson.path("results");
            if (results.isArray()) {
                for (JsonNode result : results) {
                    JsonNode externalIdNode = getExternalIdNode(result);
                    if (externalIdNode != null && externalIdNode.isInt()) {
                        SDbSyncLogEntry entry = new SDbSyncLogEntry();
                        entry.setResponseCode(result.path("status_code").asText());
                        entry.setReferenceId(String.valueOf(externalIdNode.asInt()));
                        entry.setResponseBody(SJsonUtils.sanitizeJson(result.path("message").asText()));
                        entries.add(entry);
                    }
                }
            }
        }
        
        return entries;
    }

    /**
     * Determina el código de respuesta a registrar según el resultado de la updateDateOfLastSync.
     *
     * @param updateLastSync Indica si se debe actualizar la última sincronización.
     * @param logEntries     Lista de entradas de log generadas.
     * @param responseJson   Respuesta JSON del servicio.
     * @return Código de respuesta como String.
     */
    private static String getResponseCode(final boolean updateDateOfLastSync, final ArrayList<SDbSyncLogEntry> logEntries, final JsonNode responseJson) {
        if (logEntries.isEmpty() && SAuthJsonUtils.containsElement(responseJson, "", "results")) {
            return HTTP_CODE_NO_CONTENT + ""; // no content
        }
        
        return updateDateOfLastSync ? HTTP_CODE_OK + "" : HTTP_CODE_PENDING + "";
    }

    /**
     * Registra en la base de datos el log de la operación de sincronización.
     *
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param requestBody Cuerpo de la petición (compactado).
     * @param responseCode Código de respuesta.
     * @param responseBody Cuerpo de la respuesta (compactado).
     * @param logEntries Lista de entradas de log generadas.
     * @throws SQLException Si ocurre un error al guardar el log.
     */
    private static void logSync(final SGuiSession session, final SSyncType syncType, final String requestBody, final String responseCode, final String responseBody, final ArrayList<SDbSyncLogEntry> logEntries) throws SQLException, Exception {
        String sTimeStamp = TIMESTAMP_FORMATTER.format(
                java.time.LocalDateTime.now().atZone(MEXICO)
        );
        
        SDbSyncLog log = new SDbSyncLog();
        
        String logSufix = syncType + "_" + log.getPk(session) + "_" + sTimeStamp;
        
        log.setSyncType("" + syncType);
        log.setRequestBody(logSufix + "_request_body");
        log.setRequestTimestamp(new Date());
        log.setResponseCode(responseCode);
        log.setResponseBody(logSufix + "_response_body");
        log.setResponseTimestamp(new Date());
        log.getEntries().addAll(logEntries);
        log.save(session);
        
        SExportLogUtils.safeWriteToLogFile(logSufix + "_request_body", requestBody);
        SExportLogUtils.safeWriteToLogFile(logSufix + "_response_body", responseBody);
    }
    
    /**
     * Procesa la exportación de datos a SWAP Services.
     * 
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param period Periodo de la sincronización (fechas inicial y final.)
     * @param exportAll Si es <code>true</code>, sincroniza todos los registros; si es <code>false</code>, solo los nuevos o modificados.
     * @return <code>String</code> con el JSON generado o mensaje de error.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static String computeExportData(final SGuiSession session, final SSyncType syncType, final Date[] period, final boolean exportAll) throws SQLException, Exception {
        // Determinar la fecha de la última sincronización exitosa si no es sincronización total:
        Date lastSyncDate = exportAll ? null : getLastSyncDatetime(session.getStatement(), syncType);

        // Obtener los datos a exportar según el tipo de sincronización y la fecha de la última sincronización:
        ArrayList<SExportData> dataToExport = getDataToExport(session, syncType, lastSyncDate, session.getConfigCompany().getCompanyId(), period);

        // Si no hay datos para exportar, registra el intento y retorna vacío:
        if (dataToExport == null || dataToExport.isEmpty()) {
            logSync(session, syncType, "", "" + HTTP_CODE_NO_CONTENT, "No hay nada para exportar.", new ArrayList<>());
            return "";
        }

        // Lee parámetros de configuración para la sincronización:

        String jsonParentKey = "";

        switch (syncType) {
            case USER:
            case PARTNER_SUPPLIER:
                jsonParentKey = SSwapConsts.CFG_OBJ_AREAS_SRV;
                break;
            case PURCHASE_ORDER_REF:
                jsonParentKey = SSwapConsts.CFG_OBJ_TXN_REFS_SRV;
                break;
            default:
                // nada
        }

        // Instanciar mapeador de objetos multipropósito:
        ObjectMapper mapper = new ObjectMapper();

        // Obtener la configuración del servicio de sincronización
        JsonNode config = mapper.readTree(SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG));

        String syncUrl = SAuthJsonUtils.getValueOfElementAsText(config, jsonParentKey, SSwapConsts.CFG_ATT_URL);
        String syncToken = SAuthJsonUtils.getValueOfElementAsText(config, jsonParentKey, SSwapConsts.CFG_ATT_TOKEN);
        String syncApiKey = SAuthJsonUtils.getValueOfElementAsText(config, jsonParentKey, SSwapConsts.CFG_ATT_API_KEY);
        int syncLimit = SLibUtils.parseInt(SAuthJsonUtils.getValueOfElementAsText(config, jsonParentKey, SSwapConsts.CFG_ATT_LIMIT));

        // Determinar si se debe actualizar la fecha de la última sincronización:
        boolean isSyncWithinBounds = dataToExport.size() <= syncLimit;

        // Acotar la cantidad de datos a exportar según el límite configurado:
        ArrayList<SExportData> dataToExportBounded = isSyncWithinBounds ? dataToExport : (ArrayList<SExportData>) dataToExport.subList(0, syncLimit);

        // Preparar el cuerpo de la petición en formato JSON:

        String requestBody = "";

        switch (syncType) {
            case USER:
            case PARTNER_SUPPLIER:
                SRequestUsersBody body = new SRequestUsersBody();
                body.work_instance = new String[] { "" + ((SClient) session.getClient()).getSwapServicesSetting(SSwapConsts.CFG_NVP_INSTANCE) };
                body.users = (SExportDataUser[]) dataToExportBounded.toArray(new SExportDataUser[0]);
                requestBody = mapper.writeValueAsString(body);
                break;

            case PURCHASE_ORDER_REF:

                requestBody = "";
                break;
            default:
                // nada
        }

        // Realizar la petición HTTP a SWAP Services:
        String response = requestSwapService("", syncUrl, "POST", requestBody, syncToken);
        JsonNode responseJson = mapper.readTree(response);

        // Procesar la respuesta y generar las entradas de bitácora correspondientes:
        ArrayList<SDbSyncLogEntry> logEntries = parseSyncLogEntries(responseJson);
        String responseCode = getResponseCode(isSyncWithinBounds, logEntries, responseJson);

        // Registrar la operación de exportación en la base de datos:
        logSync(session, syncType, SJsonUtils.compactJson(requestBody), responseCode, SJsonUtils.compactJson(response), logEntries);

        return "";
    }
    
    /**
     * Ejecuta una consulta para obtener datos y generar un JSON.
     * Sin período para filtrar los datos a exportar.
     *
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param exportAll Si es <code>true</code>, sincroniza todos los registros; si es <code>false</code>, solo los nuevos o modificados.
     * @return <code>String</code> con el JSON generado o mensaje de error.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public static String exportData(final SGuiSession session, final SSyncType syncType, final boolean exportAll) throws SQLException, Exception {
        return exportData(session, syncType, null, exportAll);
    }

    /**
     * Ejecuta una consulta para obtener datos y generar un JSON.
     * Con período para filtrar los datos a exportar.
     * 
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param period Periodo de la sincronización (fechas inicial y final.)
     * @param exportAll Si es <code>true</code>, exporta todos los registros; si es <code>false</code>, solo los nuevos o modificados.
     * @return <code>String</code> con el JSON generado o mensaje de error.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public static String exportData(final SGuiSession session, final SSyncType syncType, final Date[] period, final boolean exportAll) throws SQLException, Exception {
        String data = "";
        try {
            switch (syncType) {
                case USER:
                case PARTNER_SUPPLIER:
                    if (syncType == SSyncType.USER) {
                        // exportar primero áreas funcionales:
                        data = computeExportData(session, SSyncType.FUNCTIONAL_AREA, period, exportAll);
                    }
                    
                    if (data.isEmpty()) {
                        // exportar usuarios o proveedores:
                        data = computeExportData(session, syncType, period, exportAll);
                    }
                    break;
                    
                case PURCHASE_ORDER_REF:
                    // exportar referencias de pedidos de compras:
                    data = computeExportData(session, syncType, period, exportAll);
                    break;
                    
                default:
                    throw new IllegalArgumentException(ERR_UNKNOWN_SYNC_TYPE + "'" + syncType + "'.");
            }
        }
        catch (JsonProcessingException ex) {
            Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, ex);
            return "{\"error\": \"Error procesando JSON: " + ex.getMessage() + "\"}";
        }
        catch (Exception ex) {
            Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, ex);
            return "{\"error\": \"Error en la exportación: " + ex.getMessage() + "\"}";
        }
        
        return data;
    }

    /**
     * Obtiene un proveedor específico por su ID fiscal.
     * 
     * @param statement Objeto Statement para ejecutar la consulta.
     * @param fiscalId ID fiscal del proveedor a buscar.
     * @return Un objeto SUserExport con los datos del proveedor, o null si no se encuentra.
     */
    public static SExportDataUser getSupplierByFiscalId(final Statement statement, final String fiscalId) {
        String sql = SqlQueryBasePartnerSuppliers
                + "WHERE b.fiscal_id = '" + fiscalId + "' "
                + "AND NOT b.b_del AND (b.b_del IS NULL OR NOT b.b_del) "
                + "GROUP BY b.id_bp "
                + "ORDER BY bb.ts_edit DESC;";

        try (ResultSet res = statement.executeQuery(sql)) {
            if (res.next()) {
                SExportDataUser user = new SExportDataUser();
                user.username = res.getString("b.fiscal_id");
                user.email = res.getString("bbc.email_01");
                user.is_active = true;
                user.first_name = res.getString("b.firstname");
                user.last_name = res.getString("b.lastname");

                SExportDataUser.Attributes attr = new SExportDataUser.Attributes();
                attr.full_name = res.getString("b.bp");
                attr.user_type = SSwapConsts.USER_TYPE_EXTERNAL;
                attr.external_id = res.getInt("b.id_bp");
                user.attributes = attr;

                SExportDataUser.Partner partner = new SExportDataUser.Partner();
                partner.fiscal_id = res.getString("b.fiscal_id");
                partner.full_name = res.getString("b.bp");
                partner.entity_type = res.getInt("b.fid_tp_bp_idy") == 2 ? SSwapConsts.PARTNER_ENTITY_TYPE_ORG : SSwapConsts.PARTNER_ENTITY_TYPE_PER;
                partner.country_code = res.getString("cty.cty_code") == null ? DCfdi40Catalogs.ClavePaísMex : res.getString("cty.cty_code");
                partner.external_id = res.getInt("b.id_bp");
                partner.trade_name = res.getString("b.bp_comm");
                partner.tax_regime_code = res.getString("bc.tax_regime");
                partner.is_vendor = true;
                partner.partner_mail = res.getString("bbc.email_01");
                user.partner = partner;

                return user;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
