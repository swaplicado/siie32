/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import cfd.DCfdConsts;
import cfd.ver40.DCfdi40Catalogs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.cfg.db.SDbComSyncLog;
import erp.mod.cfg.db.SDbComSyncLogEntry;
import erp.mod.cfg.db.SDbFunctionalSubArea;
import erp.mod.cfg.db.SDbSyncLog;
import erp.mod.cfg.db.SDbSyncLogEntry;
import erp.mod.cfg.db.SSyncType;
import erp.mod.cfg.swap.SHttpConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.utils.SAuthJsonUtils;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
    
    public static final DecimalFormat FormatSyncLogId = new DecimalFormat("000000"); // 6 positions
    public static final SimpleDateFormat FormatSyncLogDatetime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    
    private static final int SEC_PSWD_LEN = 10;
    private static final int TIME_60_SEC = 60 * 1000; // 60 segundos en milisegundos
    
    private static final String ERR_UNKNOWN_SYNC_TYPE = "Tipo de sincronización no soportado: ";
    
    /**
     * Genera una contraseña segura de 10 caracteres aleatorios.
     * Incluye letras ASCII mayúsculas, minúsculas, números y carácteres especiales.
     * 
     * @return Contraseña segura.
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
                table = SModConsts.TablesMap.get(SModConsts.CFG_SYNC_LOG);
                break;
                
            case FUNCTIONAL_AREA:
            case PURCHASE_ORDER_REF:
            case SCALE_TICKET_REF:
                table = (database.isEmpty() ? "" : database + ".") + SModConsts.TablesMap.get(SModConsts.CFG_COM_SYNC_LOG);
                break;
                
            default:
                throw new IllegalArgumentException(ERR_UNKNOWN_SYNC_TYPE + "'" + syncType + "'.");
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
            + "bc.b_del, bc.tax_regime, bba.fid_cty_n, cty.cty_code, bbc.email_01 "
            + "FROM "
            + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON bc.id_bp = b.id_bp AND bc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_SUP + " "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bb ON bb.fid_bp = b.id_bp AND bb.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB_CON) + " AS bbc ON bbc.id_bpb = bb.id_bpb AND bbc.id_con = " + SUtilConsts.BRA_CON_ID + " "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB_ADD) + " AS bba ON bba.id_bpb = bb.id_bpb AND bba.id_add = " + SUtilConsts.BRA_ADD_ID + " "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOCU_CTY) + " AS cty ON cty.id_cty = bba.fid_cty_n ";
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
        boolean sortAsUnsigned = syncType == SSyncType.USER || syncType == SSyncType.PARTNER_SUPPLIER || syncType == SSyncType.FUNCTIONAL_AREA;
        
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
     * @return Fecha de la última sincronización exitosa, o null si no existe.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static Date getLastSyncDatetime(final Statement statement, final SSyncType syncType, final String database) throws SQLException, Exception {
        Date datetime = null;
        String table = getSqlTableSyncLog(syncType, database);
        
        String sql = "SELECT id_sync_log, ts_usr " // timestamp of server device!
                + "FROM " + table + " "
                + "WHERE response_code = '" + SHttpConsts.RSC_SUCC_OK + "' "
                + "AND sync_type = '" + syncType + "' "
                + "ORDER BY id_sync_log DESC " // primero el registro más nuevo
                + "LIMIT 1;";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                datetime = resultSet.getTimestamp("ts_usr");
            }
        }
        
        return datetime;
    }

    /**
     * Consulta los usuarios, y los prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @param syncLimit Número máximo de registros a exportar.
     * @return Lista de usuarios exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfUsersToExport(final SGuiSession session, final int syncLimit) throws SQLException, Exception {
        ArrayList<SExportData> users = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String referenceId = "CONVERT(u.id_usr, CHAR)";
            Date lastSyncDatetime = getLastSyncDatetime(session.getStatement(), SSyncType.USER, "");
            
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
                        + "OR (b.ts_edit IS NOT NULL AND b.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "'))")
                    + ") "
                    + "AND u.usr <> '' AND u.email <> '' "
                    + "ORDER BY "
                    + "u.id_usr "
                    + "LIMIT " + syncLimit + ";";
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                int userId = resultSet.getInt("u.id_usr");
                
                // validar que el usuario tenga nombre y correo:
                
                if (resultSet.getString("u.usr").isEmpty() || !resultSet.getString("u.email").contains("@")) {
                    Logger.getLogger(SExportUtils.class.getName()).log(Level.INFO,
                            "Usuario omitido (nombre o correo): ID = {0}; username = {1}; email = {2}.",
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
                            "Usuario omitido (nombre(s) o apellido(s)): ID = {0}; last_name = {1}; first_name = {2}.",
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
                attributes.other_emails = null; // no soportado en usuarios
                attributes.is_deleted = resultSet.getBoolean("u.b_del");
                attributes.external_id = userId;
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
     * Crea usuario para socio de negocios proveedor a partir de un conjunto de datos previamente abierto.
     * 
     * @param resultSet Conjunto de datos previamente abierto.
     * @return Usuario para socio de negocios proveedor.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static SExportDataUser createUserForPartnerSupplier(final ResultSet resultSet) throws SQLException {
        int partnerId = resultSet.getInt("b.id_bp");
        
        String username;
        String fullName = SJsonUtils.sanitizeJson(resultSet.getString("b.bp"));
        String fiscalId = SJsonUtils.sanitizeJson(resultSet.getString("b.fiscal_id"));
        String foreignFiscalId = SJsonUtils.sanitizeJson(resultSet.getString("b.fiscal_frg_id"));
        
        String countryCode = SJsonUtils.sanitizeJson(resultSet.getString("cty.cty_code")); // ¡NO MOVER! ¡Debe ser la sentencia previa al siguiente "if"!

        if (resultSet.wasNull()) { // ¿el código de país fue nulo?
            // proveedor nacional:

            countryCode = DCfdi40Catalogs.ClavePaísMex;

            // formato del nombre de usuario: ID fiscal nacional
            username = fiscalId;
        }
        else {
            // proveedor extranjero:

            if (foreignFiscalId.isEmpty()) {
                Logger.getLogger(SExportUtils.class.getName()).log(Level.INFO,
                        "Usuario omitido (ID fiscal de asociado de negocios del extranjero): ID = {0}; name = {1}; fiscal_id = {2}.",
                        new Object[] { partnerId, fullName, foreignFiscalId});
                return null; // regresar null para omitir usuario inválido
            }

            // formato del nombre de usuario: código de país + '-' + ID fiscal extrangero
            username = countryCode + SSwapConsts.SEPARATOR_FRG_FISCAL_ID + foreignFiscalId;
        }

        String tradeName = SJsonUtils.sanitizeJson(resultSet.getString("b.bp_comm"));
        String lastName = SJsonUtils.sanitizeJson(resultSet.getString("b.lastname"));
        String firstName = SJsonUtils.sanitizeJson(resultSet.getString("b.firstname"));
        String taxRegimeCode = resultSet.getString("bc.tax_regime"); // no requiere sanitización!
        String[] emails = resultSet.getString("bbc.email_01").split(";");
        String email = emails.length > 0 ? SLibUtils.textTrim(emails[0]) : "";
        boolean isPerson = resultSet.getInt("b.fid_tp_bp_idy") == SDataConstantsSys.BPSS_TP_BP_IDY_PER;

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
        partner.partner_mail = user.email;
        partner.is_deleted = attributes.is_deleted;
        partner.external_id = attributes.external_id;
        user.partner = partner;

        user.groups = new int[] { SSwapConsts.ROL_SUPPLIER };

        return user;
    }

    /**
     * Consulta los socios de negocios proveedores, y los prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @param syncLimit Número máximo de registros a exportar.
     * @return Lista de socios de negocios proveedores exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfPartnerSuppliersToExport(final SGuiSession session, final int syncLimit) throws SQLException, Exception {
        ArrayList<SExportData> users = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String referenceId = "CONVERT(b.id_bp, CHAR) ";
            Date lastSyncDatetime = getLastSyncDatetime(session.getStatement(), SSyncType.PARTNER_SUPPLIER, "");
            
            String sql = getSqlQueryBasePartnerSuppliers()
                    + "WHERE ("
                    + "((NOT b.b_del AND NOT bc.b_del) "
                    + "AND " + referenceId + " NOT IN (" + getSqlSubQuerySyncedRegistries(SSyncType.PARTNER_SUPPLIER, "") + "))"
                    + (lastSyncDatetime == null ? "" : " OR (b.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "' "
                        + "OR bc.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "')")
                    + ") "
                    + "AND b.b_sup AND b.fiscal_id <> '' AND b.fiscal_id <> '" + DCfdConsts.RFC_GEN_NAC + "' "
                    + "ORDER BY "
                    + "b.id_bp "
                    + "LIMIT " + syncLimit + ";";
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                SExportDataUser user = createUserForPartnerSupplier(resultSet);
                
                if (user != null) {
                    // el usuario del socio de negocios proveedor no fue omitido:
                    users.add(user);
                }
            }
        }

        return users;
    }

    /**
     * Consulta las áreas funcionales de todas las empresas configuradas para SWAP Services, y los prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @param syncLimit Número máximo de registros a exportar.
     * @return Lista de usuarios exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfFunctionalAreasToExport(final SGuiSession session, final int syncLimit) throws SQLException, Exception {
        ArrayList<SExportData> functionalAreas = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            // extraer áreas funcionales de las bases de datos de todas las empresas configuradas para SWAP Services:
            
            HashMap<Integer, String> databasesMap = getSwapCompaniesDatabasesMap(session);
            
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
                        + "fs.id_func_sub "
                        + "LIMIT " + syncLimit + ";";

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
     * Consulta las referencias de pedidos de compras de todas las empresas configuradas para SWAP Services, y las prepara para la exportación.
     * 
     * @param session Sesión de usuario.
     * @param syncLimit Número máximo de registros a exportar.
     * @return Lista de órdenes de compras exportables.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getListOfPurchaseOrderRefsToExport(final SGuiSession session, final int syncLimit) throws SQLException, Exception {
        ArrayList<SExportData> references = new ArrayList<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            // extraer referencias de pedidos de compras de las bases de datos de todas las empresas configuradas para SWAP Services:
            
            HashMap<Integer, String> databasesMap = getSwapCompaniesDatabasesMap(session);
            
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
                String referenceId = "CONCAT('" + SSwapConsts.TXN_DOC_REF_TYPE_ORDER_CODE + "', '" + SSwapConsts.SEPARATOR_DOC_REF + "', IF(t.num_ser = '', t.num, CONCAT(t.num_ser, '-', t.num)))"; // código de tipo de referencia + '/' + referencia
                Date lastSyncDatetime = getLastSyncDatetime(session.getStatement(), SSyncType.PURCHASE_ORDER_REF, database);

                String sql = "SELECT "
                        + "t.num_ser, t.num, t.dt, t.id_year, t.id_doc, t.b_link, t.b_del, t.fid_st_dps, t.ts_edit, "
                        + "t.tot_r, t.tot_cur_r, t.fid_cur, c.cur_key, t.fid_func_sub, fs.name, t.fid_bp_r, b.bp, "
                        + "COUNT(*) AS _entries, SUM(_is_linked) AS _entries_linked "
                        + "FROM ("
                        + "SELECT "
                        + "d.num_ser, d.num, d.dt, d.id_year, d.id_doc, d.b_link, d.b_del, d.fid_st_dps, d.ts_edit, "
                        + "d.tot_r, d.tot_cur_r, d.fid_cur, d.fid_func_sub, d.fid_bp_r, "
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
                        + "/*AND d.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " */" // bloque comentado para incluir pedidos "anulados" (para "eliminar" referencias en subsecuentes exportaciones)
                        + "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] + " "
                        + "/*AND NOT d.b_link */" // bloque comentado para incluir pedidos enlazados forzadamente (para "eliminar" referencias en subsecuentes exportaciones)
                        + "GROUP BY "
                        + "d.num_ser, d.num, d.dt, d.id_year, d.id_doc, d.b_link, d.b_del, d.fid_st_dps, d.ts_edit, "
                        + "d.tot_r, d.tot_cur_r, d.fid_cur, d.fid_func_sub, d.fid_bp_r, "
                        + "de.id_ety, de.fid_item, de.fid_unit, de.qty "
                        + "ORDER BY "
                        + "d.num_ser, LPAD(d.num, " + SSwapConsts.LEN_UUID + ", '0'), d.num, d.dt, d.id_year, d.id_doc, d.b_link, d.b_del, d.fid_st_dps, d.ts_edit, "
                        + "d.tot_r, d.tot_cur_r, d.fid_cur, d.fid_func_sub, d.fid_bp_r, "
                        + "de.id_ety, de.fid_item, de.fid_unit, de.qty "
                        + ") AS t "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON c.id_cur = t.fid_cur "
                        + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC_SUB) + " AS fs ON fs.id_func_sub = t.fid_func_sub "
                        + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS fs ON fs.id_func = t.fid_func "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = t.fid_bp_r "
                        + "WHERE ("
                        + "((NOT t.b_del AND t.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " AND NOT t.b_link) "
                        + "AND " + referenceId + " NOT IN (" + getSqlSubQuerySyncedRegistries(SSyncType.PURCHASE_ORDER_REF, database) + "))"
                        + (lastSyncDatetime == null ? "" : " OR (t.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "')")
                        + ") "
                        + "GROUP BY "
                        + "t.num_ser, t.num, t.dt, t.id_year, t.id_doc, t.b_link, t.b_del, t.fid_st_dps, t.ts_edit, "
                        + "t.tot_r, t.tot_cur_r, t.fid_cur, c.cur_key, t.fid_func_sub, fs.name, t.fid_bp_r, b.bp "
                        + "HAVING _entries_linked < _entries "
                        + "ORDER BY "
                        + "t.num_ser, LPAD(t.num, " + SSwapConsts.LEN_UUID + ", '0'), t.num, t.dt, t.id_year, t.id_doc, t.b_link, t.b_del, t.fid_st_dps, t.ts_edit, "
                        + "t.tot_r, t.tot_cur_r, t.fid_cur, c.cur_key, t.fid_func_sub, fs.name, t.fid_bp_r, b.bp "
                        + "LIMIT " + syncLimit + ";";
/*
                String sql = "SELECT "
                        + "t.num_ser, t.num, t.dt, t.id_year, t.id_doc, t.b_link, t.b_del, t.fid_st_dps, t.ts_edit, "
                        + "t.tot_r, t.tot_cur_r, t.fid_cur, c.cur_key, t.fid_func, fs.name, t.fid_bp_r, b.bp, "
                        + "COUNT(*) AS _entries, SUM(_is_linked) AS _entries_linked "
                        + "FROM ("
                        + "SELECT "
                        + "d.num_ser, d.num, d.dt, d.id_year, d.id_doc, d.b_link, d.b_del, d.fid_st_dps, d.ts_edit, "
                        + "d.tot_r, d.tot_cur_r, d.fid_cur, d.fid_func, d.fid_bp_r, "
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
                        + "/*NOT d.b_del AND * /NOT de.b_del " // bloque comentado para incluir pedidos eliminados (para "eliminar" referencias en subsecuentes exportaciones)
                        + "/*AND d.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " * /" // bloque comentado para incluir pedidos "anulados" (para "eliminar" referencias en subsecuentes exportaciones)
                        + "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] + " "
                        + "/*AND NOT d.b_link * /" // bloque comentado para incluir pedidos enlazados forzadamente (para "eliminar" referencias en subsecuentes exportaciones)
                        + "GROUP BY "
                        + "d.num_ser, d.num, d.dt, d.id_year, d.id_doc, d.b_link, d.b_del, d.fid_st_dps, d.ts_edit, "
                        + "d.tot_r, d.tot_cur_r, d.fid_cur, d.fid_func, d.fid_bp_r, "
                        + "de.id_ety, de.fid_item, de.fid_unit, de.qty "
                        + "ORDER BY "
                        + "d.num_ser, LPAD(d.num, " + SSwapConsts.LEN_UUID + ", '0'), d.num, d.dt, d.id_year, d.id_doc, d.b_link, d.b_del, d.fid_st_dps, d.ts_edit, "
                        + "d.tot_r, d.tot_cur_r, d.fid_cur, d.fid_func, d.fid_bp_r, "
                        + "de.id_ety, de.fid_item, de.fid_unit, de.qty "
                        + ") AS t "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON c.id_cur = t.fid_cur "
//                        + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC_SUB) + " AS fs ON fs.id_func_sub = t.fid_func_sub "
                        + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS fs ON fs.id_func = t.fid_func "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = t.fid_bp_r "
                        + "WHERE ("
                        + "("
+ "OR (t.id_year = 2023 AND t.id_doc = 31) "
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
+ "OR (t.id_year = 2025 AND t.id_doc = 11454) "
+ "OR (t.id_year = 2025 AND t.id_doc = 11455) "
+ "OR (t.id_year = 2025 AND t.id_doc = 11481) "
+ "OR (t.id_year = 2025 AND t.id_doc = 11497) "
                        + ") OR "
                        + "((NOT t.b_del AND t.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " AND NOT t.b_link) "
                        + "AND " + referenceId + " NOT IN (" + getSqlSubQuerySyncedRegistries(SSyncType.PURCHASE_ORDER_REF, database) + "))"
                        + (lastSyncDatetime == null ? "" : " OR (t.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(lastSyncDatetime) + "')")
                        + ") "
                        + "GROUP BY "
                        + "t.num_ser, t.num, t.dt, t.id_year, t.id_doc, t.b_link, t.b_del, t.fid_st_dps, t.ts_edit, "
                        + "t.tot_r, t.tot_cur_r, t.fid_cur, c.cur_key, t.fid_func, fs.name, t.fid_bp_r, b.bp "
                        + "HAVING _entries_linked < _entries "
                        + "ORDER BY "
                        + "t.num_ser, LPAD(t.num, " + SSwapConsts.LEN_UUID + ", '0'), t.num, t.dt, t.id_year, t.id_doc, t.b_link, t.b_del, t.fid_st_dps, t.ts_edit, "
                        + "t.tot_r, t.tot_cur_r, t.fid_cur, c.cur_key, t.fid_func, fs.name, t.fid_bp_r, b.bp "
                        + "LIMIT " + syncLimit + ";";
*/
                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    String txnReference;
                    
                    txnReference = resultSet.getString("t.num_ser");
                    txnReference += (txnReference.isEmpty() ? "" : "-") + resultSet.getString("t.num");
                    txnReference = SSwapConsts.TXN_DOC_REF_TYPE_ORDER_CODE + SSwapConsts.SEPARATOR_DOC_REF + txnReference; // código de tipo de referencia + '/' + referencia
                    
                    SExportDataReference reference = new SExportDataReference();

                    reference.external_company_id = companyId;
                    reference.external_functional_area_id = resultSet.getInt("t.fid_func_sub");
                    reference.transaction_class_id = SSwapConsts.TXN_CAT_PURCHASE;
                    reference.document_ref_type_id = SSwapConsts.TXN_DOC_REF_TYPE_ORDER;
                    reference.external_partner_id = resultSet.getInt("t.fid_bp_r");
                    reference.reference = txnReference;
                    reference.date = SLibUtils.DbmsDateFormatDate.format(resultSet.getDate("t.dt")); // yyyy-mm-dd
                    reference.currency_code = resultSet.getString("c.cur_key");
                    reference.amount = resultSet.getDouble("t.tot_cur_r");
                    reference.is_deleted = resultSet.getBoolean("t.b_link") || resultSet.getBoolean("t.b_del") || resultSet.getInt("t.fid_st_dps") == SDataConstantsSys.TRNS_ST_DPS_ANNULED;

                    references.add(reference);
                }
            }
        }

        return references;
    }

    /**
     * Obtiene la lista de usuarios o proveedores a exportar según el tipo de sincronización.
     *
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param syncLimit Número máximo de registros a exportar.
     * @return Lista de datos a exportar.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static ArrayList<SExportData> getDataToExport(final SGuiSession session, final SSyncType syncType, final int syncLimit) throws SQLException, Exception {
        ArrayList<SExportData> data = null;
        
        switch (syncType) {
            case USER:
                data = getListOfUsersToExport(session, syncLimit);
                break;
                
            case PARTNER_SUPPLIER:
                data = getListOfPartnerSuppliersToExport(session, syncLimit);
                break;
                
            case FUNCTIONAL_AREA:
                data = getListOfFunctionalAreasToExport(session, syncLimit);
                break;
                
            case PURCHASE_ORDER_REF:
                data = getListOfPurchaseOrderRefsToExport(session, syncLimit);
                break;
                
            default:
                throw new IllegalArgumentException(ERR_UNKNOWN_SYNC_TYPE + "'" + syncType + "'.");
        }
        
        return data;
    }

    /**
     * Realiza una solicitud HTTP a un servicio de intercambio de datos.
     * 
     * @param queryUrl Parámetros de consulta para la URL (opcional).
     * @param serviceUrl URL del servicio al que se realiza la solicitud.
     * @param method Método HTTP a utilizar (GET, POST, PUT, etc.).
     * @param body Cuerpo de la solicitud (para métodos como POST).
     * @param token Token de autorización (opcional).
     * @param apiKey API Key de autorización (opcional).
     * @return Respuesta del servicio en formato JSON.
     * @throws Exception
     */
    private static String requestSwapService(final String queryUrl, final String serviceUrl, final String method, final String body, final String token, final String apiKey) throws Exception {
        String charset = java.nio.charset.StandardCharsets.UTF_8.name();
        HttpURLConnection connection = null;
        String responseBody = "";

        try {
            URL url;
            
            if (SHttpConsts.METHOD_GET.equalsIgnoreCase(method) && queryUrl != null && !queryUrl.isEmpty()) {
                url = new URL(serviceUrl + "?" + queryUrl);
            }
            else {
                url = new URL(serviceUrl);
            }

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIME_60_SEC); // 30 segundos para conectar
            connection.setReadTimeout(TIME_60_SEC);    // 30 segundos para leer respuesta
            connection.setRequestMethod(method.toUpperCase());
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            if (token != null && !token.isEmpty()) {
                connection.setRequestProperty("Authorization", token);
            }
            if (apiKey != null && !apiKey.isEmpty()) {
                connection.setRequestProperty("x-api-key", apiKey);
            }
            connection.setDoInput(true);

            // Para métodos que envían datos (POST, PUT, etc.)
            
            boolean isBodySent = false; // 2025-08-13, Sergio Flores: ¡no es claro el propósito de esta variable, declarada desde la versión inicial de este método!
            
            if (!SHttpConsts.METHOD_GET.equalsIgnoreCase(method)) {
                if (body != null && !body.trim().isEmpty()) {
                    // Validar que el body sea un JSON válido
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.readTree(body); // Lanza excepción si no es JSON válido
                    }
                    catch (JsonProcessingException ex) {
                        throw new IllegalArgumentException("El parámetro 'sBody' no es un JSON válido.", ex);
                    }
                    connection.setDoOutput(true);
                    try (java.io.OutputStream os = connection.getOutputStream()) {
                        byte[] input = body.getBytes(charset);
                        os.write(input, 0, input.length);
                    }
                    isBodySent = true;
                }
                else if (queryUrl != null && !queryUrl.isEmpty()) {
                    connection.setDoOutput(true);
                    try (java.io.OutputStream os = connection.getOutputStream()) {
                        byte[] input = queryUrl.getBytes(charset);
                        os.write(input, 0, input.length);
                    }
                    isBodySent = true;
                }
            }

            int status = connection.getResponseCode();
            InputStream responseStream = (status >= SHttpConsts.RSC_SUCC_OK && status < SHttpConsts.RSC_ERR_BAD_REQUEST) ? connection.getInputStream() : connection.getErrorStream();

            try (Scanner scanner = new Scanner(responseStream, charset)) {
                responseBody = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            }

            System.out.println("Respuesta desde " + url);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, e);
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        
        return responseBody;
    }

    /**
     * Analiza gramaticalmente la respuesta JSON del servicio de sincronización y genera los entradas de la bitácora de sincronización.
     *
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param responseJson Respuesta JSON del servicio.
     * @return Lista de entradas de log para la sincronización.
     * @throws Exception
     */
    private static ArrayList<SDbSyncLogEntry> createSyncLogEntries(final SGuiSession session, final SSyncType syncType, final JsonNode responseJson) throws Exception {
        ArrayList<SDbSyncLogEntry> entries = new ArrayList<>();
        
        if (SAuthJsonUtils.containsElement(responseJson, "", "results")) {
            JsonNode results = responseJson.path("results");
            if (results.isArray()) {
                switch (syncType) {
                    case USER:
                    case PARTNER_SUPPLIER:
                        for (JsonNode result : results) {
                            JsonNode user = result.path("user");
                            
                            if (user.isObject() && user.has("attributes")) {
                                JsonNode attributes = user.path("attributes");
                                if (attributes.isObject()) {
                                    JsonNode externalId = attributes.path("external_id");
                                    
                                    SDbSyncLogEntry entry = new SDbSyncLogEntry();
                                    entry.setResponseCode(result.path("status_code").asText());
                                    entry.setResponseBody(SJsonUtils.sanitizeJson(result.path("message").asText()));
                                    entry.setReferenceId("" + externalId.asInt());
                                    entries.add(entry);
                                }
                            }
                        }
                        break;

                    case FUNCTIONAL_AREA:
                    case PURCHASE_ORDER_REF:
                        HashMap<Integer, String> databasesMap = getSwapCompaniesDatabasesMap(session);
                        
                        for (JsonNode result : results) {
                            JsonNode data = result.path("data");
                            
                            if (data.isObject()) {
                                JsonNode externalId = data.path("external_id");
                                
                                SDbComSyncLogEntry entry = new SDbComSyncLogEntry();
                                entry.setResponseCode(result.path("status_code").asText());
                                entry.setResponseBody(SJsonUtils.sanitizeJson(result.path("message").asText()));
                                entry.setReferenceId(syncType == SSyncType.FUNCTIONAL_AREA ? "" + externalId.asInt() : externalId.asText());
                                entry.setAuxDatabase(databasesMap.get(data.path("company_id").asInt()));
                                entries.add(entry);
                            }
                        }
                        break;

                    default:
                        throw new IllegalArgumentException(ERR_UNKNOWN_SYNC_TYPE + "'" + syncType + "'.");
                }
            }
        }
        
        return entries;
    }
    
    /**
     * Hace las entradas en las bitácoras de sincronización.
     *
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static int logEmptySync(final SGuiSession session, final SSyncType syncType) throws SQLException, Exception {
        return logSync(session, syncType, "", null, SHttpConsts.RSC_SUCC_NO_CONTENT, "No hay nada para exportar.", null, null);
    }
    
    /**
     * Hace las entradas en las bitácoras de sincronización.
     *
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param requestBody Cuerpo de la petición (compactado).
     * @param requestDatetime Fecha-hora de la petición en el dispositivo cliente.
     * @param httpResponseStatusCode Código de estatus de respuesta HTTP.
     * @param responseBody Cuerpo de la respuesta (compactado).
     * @param responseDatetime Fecha-hora de la respuesta en el dispositivo cliente.
     * @param syncLogEntries Lista de entradas de log generadas.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static int logSync(final SGuiSession session, final SSyncType syncType, final String requestBody, final Date requestDatetime, final int httpResponseStatusCode, final String responseBody, final Date responseDatetime, final ArrayList<SDbSyncLogEntry> syncLogEntries) throws SQLException, Exception {
        int entriesLogged = 0;
        String fileNameRequestBody = "";
        String fileNameResponseBody = "";
        HashMap<String, ArrayList<SDbSyncLogEntry>> syncLogEntriesPerDatabaseMap = new HashMap<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            boolean logDone = false;
            
            try {
                statement.execute("START TRANSACTION");
                
                if (syncLogEntries == null || syncLogEntries.isEmpty()) {
                    // empty sync:

                    SDbSyncLog log;

                    switch (syncType) {
                        case USER:
                        case PARTNER_SUPPLIER:
                            log = new SDbSyncLog();
                            break;

                        case FUNCTIONAL_AREA:
                        case PURCHASE_ORDER_REF:
                            log = new SDbComSyncLog();
                            break;

                        default:
                            throw new IllegalArgumentException(ERR_UNKNOWN_SYNC_TYPE + "'" + syncType + "'.");
                    }

                    log.setSyncType(syncType.toString());
                    log.setRequestTimestamp(requestDatetime);
                    log.setResponseCode("" + httpResponseStatusCode);
                    log.setResponseTimestamp(responseDatetime);
                    log.save(session);

                    fileNameRequestBody = log.getRequestBodyFileName();
                    fileNameResponseBody = log.getResponseBodyFileName();
                }
                else {
                    // effective sync:

                    for (SDbSyncLogEntry entry : syncLogEntries) {
                        ArrayList<SDbSyncLogEntry> entries = syncLogEntriesPerDatabaseMap.get(entry.getAuxDatabase());

                        if (entries == null) {
                            entries = new ArrayList<>();
                            syncLogEntriesPerDatabaseMap.put(entry.getAuxDatabase(), entries);
                        }

                        entries.add(entry);
                    }

                    boolean fileNamesAlreadySet = false;
                    for (String database : syncLogEntriesPerDatabaseMap.keySet()) {
                        SDbSyncLog log;

                        switch (syncType) {
                            case USER:
                            case PARTNER_SUPPLIER:
                                log = new SDbSyncLog();
                                break;

                            case FUNCTIONAL_AREA:
                            case PURCHASE_ORDER_REF:
                                log = new SDbComSyncLog();
                                break;

                            default:
                                throw new IllegalArgumentException(ERR_UNKNOWN_SYNC_TYPE + "'" + syncType + "'.");
                        }

                        log.setSyncType(syncType.toString());
                        log.setRequestTimestamp(requestDatetime);
                        log.setResponseCode("" + httpResponseStatusCode);
                        log.setResponseTimestamp(responseDatetime);
                        log.getEntries().addAll(syncLogEntriesPerDatabaseMap.get(database));
                        log.setAuxDatabase(database);
                        log.save(session);
                        
                        entriesLogged += log.getEntries().size();

                        if (!fileNamesAlreadySet) {
                            fileNameRequestBody = log.getRequestBodyFileName();
                            fileNameResponseBody = log.getResponseBodyFileName();
                            fileNamesAlreadySet = true;
                        }
                    }
                }
                
                logDone = true;

                SExportLogUtils.safeWriteToLogFile(fileNameRequestBody, requestBody);
                SExportLogUtils.safeWriteToLogFile(fileNameResponseBody, responseBody);
            }
            catch (Exception eExe) {
                if (!logDone) {
                    try {
                        statement.execute("ROLLBACK");
                    }
                    catch (SQLException eTxn) {
                        Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, eTxn);
                    }
                }
                
                Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, eExe);
                throw eExe; // focus on execution issues!
            }
            finally {
                if (logDone) {
                    try {
                        statement.execute("COMMIT");
                    }
                    catch (SQLException eTxn) {
                        Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, eTxn);
                        throw eTxn; // focus on transaction issues!
                    }
                }
            }
        }
        
        return entriesLogged;
    }
    
    /**
     * Procesa la respuesta de SWAP Services y hace las entradas en las bitácoras de sincronización.
     * 
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param isSyncWithinBounds La sincronización está dentro de los límites configurados.
     * @param requestBody Cuerpo de la petición (compactado).
     * @param requestDatetime Fecha-hora de la petición en el dispositivo cliente.
     * @param responseBody Respuesta de SWAP Services.
     * @param responseDatetime Fecha-hora de la respuesta en el dispositivo cliente.
     * @throws Exception
     */
    private static int computeResponse(final SGuiSession session, final SSyncType syncType, final boolean isSyncWithinBounds, final String requestBody, final Date requestDatetime, final String responseBody, final Date responseDatetime) throws Exception {
        final JsonNode responseJson = new ObjectMapper().readTree(responseBody);
        
        // Procesar la respuesta y generar las entradas de bitácora correspondientes:
        
        int httpResponseStatusCode;
        ArrayList<SDbSyncLogEntry> syncLogEntries = createSyncLogEntries(session, syncType, responseJson);
        
        if (syncLogEntries.isEmpty() && SAuthJsonUtils.containsElement(responseJson, "", "results")) {
            httpResponseStatusCode = SHttpConsts.RSC_SUCC_NO_CONTENT;
        }
        else {
            httpResponseStatusCode = isSyncWithinBounds ? SHttpConsts.RSC_SUCC_OK : SHttpConsts.RSC_SUCC_ACCEPTED;
        }
        
        // Registrar la operación de exportación en las bitácoras de sincronización:
        
        return logSync(session, syncType, requestBody, requestDatetime, httpResponseStatusCode, responseBody, responseDatetime, syncLogEntries);
    }
    
    /**
     * Procesa la exportación de datos a SWAP Services.
     * 
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @param period Periodo de la sincronización (fechas inicial y final.)
     * @return <code>String</code> con el JSON generado o mensaje de error.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private static String computeRequest(final SGuiSession session, final SSyncType syncType) throws SQLException, Exception {
        // Lee parámetros de configuración para la sincronización:

        String jsonParentKey = "";

        switch (syncType) {
            case USER:
            case PARTNER_SUPPLIER:
                jsonParentKey = SSwapConsts.CFG_OBJ_USERS_SRV;
                break;
                
            case FUNCTIONAL_AREA:
                jsonParentKey = SSwapConsts.CFG_OBJ_AREAS_SRV;
                break;
                
            case PURCHASE_ORDER_REF:
                jsonParentKey = SSwapConsts.CFG_OBJ_TXN_REFS_SRV;
                break;
                
            default:
                throw new IllegalArgumentException(ERR_UNKNOWN_SYNC_TYPE + "'" + syncType + "'.");
        }

        // Instanciar mapeador de objetos multipropósito:
        ObjectMapper mapper = new ObjectMapper();

        // Obtener la configuración del servicio de sincronización
        JsonNode config = mapper.readTree(SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG));

        String syncUrl = SAuthJsonUtils.getValueOfElementAsText(config, jsonParentKey, SSwapConsts.CFG_ATT_URL);
        String syncToken = SAuthJsonUtils.getValueOfElementAsText(config, jsonParentKey, SSwapConsts.CFG_ATT_TOKEN);
        String syncApiKey = SAuthJsonUtils.getValueOfElementAsText(config, jsonParentKey, SSwapConsts.CFG_ATT_API_KEY);
        int syncLimit = SLibUtils.parseInt(SAuthJsonUtils.getValueOfElementAsText(config, jsonParentKey, SSwapConsts.CFG_ATT_LIMIT));
        
        // Procesar la exportación de datos:

        // Obtener los datos a exportar según el tipo de sincronización y la fecha de la última sincronización:
        ArrayList<SExportData> allExportDatas = getDataToExport(session, syncType, syncLimit);

        // Si no hay datos para exportar, registra el intento y retorna vacío:
        if (allExportDatas == null || allExportDatas.isEmpty()) {
            logEmptySync(session, syncType);
            return "";
        }

        // Determinar si los datos a sincronizar están dentro del límite permitido:
        boolean isSyncWithinBounds = allExportDatas.size() <= syncLimit;

        // Acotar la cantidad de datos a exportar según el límite configurado:
        ArrayList<SExportData> boundedExportDatas = isSyncWithinBounds ? allExportDatas : new ArrayList<>(allExportDatas.subList(0, syncLimit));

        // Preparar el cuerpo de la petición en formato JSON:

        String requestBody = "";
        String[] instanceArray = new String[] { "" + ((SClientInterface) session.getClient()).getSwapServicesSetting(SSwapConsts.CFG_NVP_INSTANCE) };

        switch (syncType) {
            case USER:
            case PARTNER_SUPPLIER:
                SRequestUsersBody usersBody = new SRequestUsersBody();
                usersBody.work_instance = instanceArray;
                usersBody.users = (SExportDataUser[]) boundedExportDatas.toArray(new SExportDataUser[0]);
                requestBody = mapper.writeValueAsString(usersBody);
                break;
                
            case FUNCTIONAL_AREA:
                SRequestFunctionalAreasBody functionalAreasBody = new SRequestFunctionalAreasBody();
                functionalAreasBody.work_instance = instanceArray;
                functionalAreasBody.functional_areas = (SExportDataFunctionalArea[]) boundedExportDatas.toArray(new SExportDataFunctionalArea[0]);
                requestBody = mapper.writeValueAsString(functionalAreasBody);
                break;

            case PURCHASE_ORDER_REF:
                SRequestReferencesBody referencesBody = new SRequestReferencesBody();
                referencesBody.work_instance = instanceArray;
                referencesBody.references = (SExportDataReference[]) boundedExportDatas.toArray(new SExportDataReference[0]);
                requestBody = mapper.writeValueAsString(referencesBody);
                break;

            default:
                // nada
        }

        // Realizar la petición HTTP a SWAP Services:
        Date requestDatetime = new Date();
        String responseBody = requestSwapService("", syncUrl, SHttpConsts.METHOD_POST, requestBody, syncToken, syncApiKey);
        Date responseDatetime = new Date();
        
        // Procesar la respuesta:
        computeResponse(session, syncType, isSyncWithinBounds, requestBody, requestDatetime, responseBody, responseDatetime);

        return "";
    }
    
    /**
     * Ejecuta una consulta para obtener datos y generar un JSON.
     * Con período para filtrar los datos a exportar.
     * 
     * @param session Sesión de usuario.
     * @param syncType Tipo de sincronización.
     * @return <code>String</code> con el JSON generado o mensaje de error.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public static String exportData(final SGuiSession session, final SSyncType syncType) throws SQLException, Exception {
        String data = "";
        
        try {
            switch (syncType) {
                case USER:
                case PARTNER_SUPPLIER:
                    if (syncType == SSyncType.USER) {
                        // exportar antes áreas funcionales:
                        data = computeRequest(session, SSyncType.FUNCTIONAL_AREA);
                    }
                    
                    if (data.isEmpty()) {
                        // exportar usuarios o proveedores:
                        data = computeRequest(session, syncType);
                    }
                    break;
                    
                case PURCHASE_ORDER_REF:
                    // exportar antes áreas funcionales:
                    data = computeRequest(session, SSyncType.FUNCTIONAL_AREA);
                    
                    if (data.isEmpty()) {
                        // exportar antes proveedores:
                        data = computeRequest(session, SSyncType.PARTNER_SUPPLIER);

                        if (data.isEmpty()) {
                            // exportar referencias de pedidos de compras:
                            data = computeRequest(session, syncType);
                        }
                    }
                    break;
                    
                default:
                    throw new IllegalArgumentException(ERR_UNKNOWN_SYNC_TYPE + "'" + syncType + "'.");
            }
        }
        catch (JsonProcessingException e) {
            Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, e);
            return "{\"error\": \"Error procesando JSON: " + e.getMessage() + "\"}";
        }
        catch (Exception e) {
            Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, e);
            return "{\"error\": \"Error en la exportación: " + e.getMessage() + "\"}";
        }
        
        return data;
    }
    
    /**
     * Obtiene una cadena de texto con los ID de las emresas configuradas para SWAP Services para consultas SQL.
     *
     * @param session Sesión de usuario.
     * @return Cadena de texto con los ID de las emresas.
     */
    public static String getSwapCompaniesForSqlQuery(final SGuiSession session) {
        int[] companies = (int[]) ((SClientInterface) session.getClient()).getSwapServicesSetting(SSwapConsts.CFG_NVP_COMPANIES);
        return Arrays.stream(companies).mapToObj(String::valueOf).collect(Collectors.joining(", "));
    }

    /**
     * Obtiene un mapa de los nombres de las bases de datos de las emresas configuradas para SWAP Services.
     *
     * @param session Sesión de usuario.
     * @return Mapa de los nombres de las bases de datos: key = company ID; value = database name.
     * @throws SQLException Si ocurre un error en la consulta.
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
                databasesMap.put(resultSet.getInt("id_co"), resultSet.getString("bd"));
            }
        }
        
        return databasesMap;
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
                    user = createUserForPartnerSupplier(resultSet);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            Logger.getLogger(SExportUtils.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return user;
    }
}
