/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import erp.mod.SModConsts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SImportUtils {
    
    /**
     * Create prepared statement to count imports:
     * Index:   Parameter:
     *      1   Sync type.
     *      2   Response code.
     *      3   User ID.
     *      4   Entry response code.
     *      5   Reference ID.
     * @param session
     * @return Prepared statement.
     * @throws Exception 
     */
    public static PreparedStatement createPreparedStatementToCountImports(final SGuiSession session) throws Exception {
        String sql = "SELECT COUNT(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFG_COM_IMP_LOG) + " AS il "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFG_COM_IMP_LOG_ETY) + " AS ile ON ile.id_sync_log = il.id_sync_log "
                + "WHERE il.sync_type = ? AND il.response_code = ? AND il.fk_usr = ? AND ile.response_code = ? AND ile.reference_id = ?;";
        
        return session.getStatement().getConnection().prepareStatement(sql);
    }
    
    /**
     * Count imports.
     * @param preparedStatement
     * @param syncType
     * @param responseCode
     * @param userId
     * @param referenceId
     * @throws Exception 
     */
    public static int countImports(final PreparedStatement preparedStatement, final String syncType, final String responseCode, final int userId, final String referenceId) throws Exception {
        int count = 0;
        
        preparedStatement.setString(1, syncType);
        preparedStatement.setString(2, responseCode);
        preparedStatement.setInt(3, userId);
        preparedStatement.setString(4, responseCode);
        preparedStatement.setString(5, referenceId);
        
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        
        return count;
    }
}
