/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import erp.mod.SModConsts;
import erp.mod.cfg.db.SDbComSyncLogEntry;
import erp.mod.cfg.swap.SSyncType;
import java.net.HttpURLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.gui.SGuiSession;

/**
 * Utilidades de la bitácotra de exportación
 *
 * @author Edwin Carmona
 */
public class SExportDpsFileUtils {
    
    /**
     * Obtiene el último registro de sincronización para determinar si ya está sincronizado
     * 
     * @param statement
     * @param syncType SSyncType valor de enumerado
     * @param sReference String de la pk del registro a consultar
     * 
     * @return null si no ha sido sincronizado, objeto Date si ya lo está.
     */
    public static Date isResourceCompanySinchronized(final Statement statement, final SSyncType syncType, final String sReference) {
        try {
            ResultSet resultSet;
            
            String sql = "SELECT  " +
                    "    le.ts_sync " +
                    "FROM " +
                    "    " + SModConsts.TablesMap.get(SModConsts.CFG_COM_SYNC_LOG) + " AS l " +
                    "        INNER JOIN " +
                    "    " + SModConsts.TablesMap.get(SModConsts.CFG_COM_SYNC_LOG_ETY) + " AS le ON l.id_sync_log = le.id_sync_log " +
                    "WHERE " +
                    "    le.response_code IN (" + HttpURLConnection.HTTP_OK + " , " + HttpURLConnection.HTTP_CREATED + ") " +
                    "        AND l.sync_type = '" + syncType + "' " +
                    "        AND le.reference_id = '" + sReference + "' " +
                    "ORDER BY le.ts_sync DESC;";
            
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getTimestamp("le.ts_sync");
            }
            
            return null;
        }
        catch (SQLException ex) {
            Logger.getLogger(SExportDpsFileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Obtiene el último log de sincronización con status 200 o 201 del recurso recibido.
     * 
     * @param session
     * @param syncType SSyncType valor de enumerado
     * @param sReference String de la pk del registro a consultar
     * @param dbName
     * 
     * @return null si no hay coincidencia
     */
    public static SDbComSyncLogEntry getLastSynchronization(final SGuiSession session, final SSyncType syncType, final String sReference, final String dbName) {
        try {
            ResultSet resultSet;
            
            String sql = "SELECT  " +
                    "    le.id_sync_log, le.id_ety " +
                    "FROM " +
                    "    " + (dbName == null || dbName.isEmpty() ? "" : (dbName + ".")) + 
                        SModConsts.TablesMap.get(SModConsts.CFG_COM_SYNC_LOG) + " AS l " +
                    "        INNER JOIN " +
                    "    " + (dbName == null || dbName.isEmpty() ? "" : (dbName + ".")) + 
                        SModConsts.TablesMap.get(SModConsts.CFG_COM_SYNC_LOG_ETY) + " AS le ON l.id_sync_log = le.id_sync_log " +
                    "WHERE " +
                    "    le.response_code IN (" + HttpURLConnection.HTTP_OK + " , " + HttpURLConnection.HTTP_CREATED + ") " +
                    "        AND l.sync_type = '" + syncType + "' " +
                    "        AND le.reference_id = '" + sReference + "' " +
                    "ORDER BY le.ts_sync DESC " +
                    "LIMIT 1;";
            
            resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql);
            if (resultSet.next()) {
                SDbComSyncLogEntry oLogEntry = new SDbComSyncLogEntry();
                oLogEntry.setAuxDatabase(dbName);
                oLogEntry.read(session, new int[] { resultSet.getInt("id_sync_log"), resultSet.getInt("id_ety") });
                return oLogEntry;
            }
            
            return null;
        }
        catch (SQLException ex) {
            Logger.getLogger(SExportDpsFileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SExportDpsFileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
