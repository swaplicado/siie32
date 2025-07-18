/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.db;

import java.util.Date;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 * Clase para sincronización de datos con servicios externos. * Esta clase
 * representa una entrada de registro de sincronización, que almacena
 * información sobre cada sincronización realizada, incluyendo el tipo de
 * sincronización, cuerpo de la solicitud, código de respuesta, cuerpo de la
 * respuesta, y marcas de tiempo.
 *
 * @author Edwin Carmona
 */
public class SDbSyncLogEntry extends SDbRegistryUser {

    protected int mnPkSyncLogId;
    protected int mnPkSyncLogEntryId;
    protected String msResponseCode;
    protected String msResponseBody;
    protected String msReferenceId;
    protected Date mtTsSynchronization;
    
    public SDbSyncLogEntry() {
        super(SModConsts.CFG_SYNC_LOG_ETY);
    }

    public void setPkSyncLogId(int n) { this.mnPkSyncLogId = n; }
    public void setPkSyncLogEntryId(int n) { this.mnPkSyncLogEntryId = n; }
    public void setResponseCode(String s) { this.msResponseCode = s; }
    public void setResponseBody(String s) { this.msResponseBody = s; }
    public void setReferenceId(String s) { this.msReferenceId = s; }
    public void setTsSynchronization(Date t) { this.mtTsSynchronization = t; }

    public int getPkSyncLogId() { return this.mnPkSyncLogId; }
    public int getPkSyncLogEntryId() { return this.mnPkSyncLogEntryId; }
    public String getResponseCode() { return this.msResponseCode; }
    public String getResponseBody() { return this.msResponseBody; }
    public String getReferenceId() { return this.msReferenceId; }
    public Date getTsSynchronization() { return this.mtTsSynchronization; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSyncLogId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSyncLogId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkSyncLogId = 0;
        mnPkSyncLogEntryId = 0;
        msResponseCode = "";
        msResponseBody = "";
        msReferenceId = "";
        mtTsSynchronization = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_sync_log = " + mnPkSyncLogId + " AND id_ety = " + mnPkSyncLogEntryId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_sync_log = " + pk[0] + " " +
               "AND id_ety = " + (pk.length > 1 ? pk[1] : 0) + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkSyncLogEntryId = 0;

        msSql = "SELECT COALESCE(MAX(id_ety), 0) + 1 FROM " + getSqlTable() + "  " +
                "WHERE id_sync_log = " + mnPkSyncLogId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkSyncLogEntryId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * FROM " + getSqlTable() + " " + getSqlWhere(pk);
        
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkSyncLogId = resultSet.getInt("id_sync_log");
            mnPkSyncLogEntryId = resultSet.getInt("id_ety");
            msResponseCode = resultSet.getString("response_code");
            msResponseBody = resultSet.getString("response_body");
            msReferenceId = resultSet.getString("reference_id");
            mtTsSynchronization = resultSet.getTimestamp("ts_sync");

            mbRegistryNew = false;
        }
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        // limitar msRequestBody y msResponseBody a 64 KB, para evitar problemas de tamaño en la base de datos:
        if (msResponseBody != null && msResponseBody.length() > 65536) {
            msResponseBody = msResponseBody.substring(0, 65536);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mnFkUserInsertId = session.getUser().getPkUserId();

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkSyncLogId + ", " +
                    mnPkSyncLogEntryId + ", " +
                    "'" + msResponseCode + "', " +
                    "'" + msResponseBody + "', " +
                    "'" + msReferenceId + "', " +
                    "NOW()" + ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    // "id_sync_log = " + mnPkSyncLogId + ", " +
                    // "id_ety = " + mnPkSyncLogEntryId + ", " +
                    "response_code = '" + msResponseCode + "', " +
                    "response_body = '" + msResponseBody + "', " +
                    "reference_id = '" + msReferenceId + "' " +
                    // ts_usr_ins is not updated, as it is the timestamp of the first insert
                    // ts_usr_upd is not updated, as it is the timestamp of the last update
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbSyncLogEntry registry = new SDbSyncLogEntry();
        registry.setPkSyncLogId(mnPkSyncLogId);
        registry.setPkSyncLogEntryId(mnPkSyncLogEntryId);
        registry.setResponseCode(msResponseCode);
        registry.setResponseBody(msResponseBody);
        registry.setReferenceId(msReferenceId);
        registry.setTsSynchronization(mtTsSynchronization);

        return registry;
    }
}
