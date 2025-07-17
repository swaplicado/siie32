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
import java.util.ArrayList;
import java.util.List;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbSyncLog extends SDbRegistryUser {

    protected int mnPkSyncLogId;
    protected String msSyncType;
    protected String msRequestBody;
    protected Date msRequestTimestamp;
    protected String msResponseCode;
    protected String msResponseBody;
    protected Date msResponseTimestamp;
    protected int mnFkUserId;
    protected Date mtTsUser;
    
    protected List<SDbSyncLogEntry> mlSyncLogEntries;
    
    public SDbSyncLog() {
        super(SModConsts.CFG_SYNC_LOG);
    }

    public void setIdSyncLog(int n) { this.mnPkSyncLogId = n; }
    public void setSyncType(String s) { this.msSyncType = s; }
    public void setRequestBody(String s) { this.msRequestBody = s; }
    public void setRequestTimestamp(Date t) { this.msRequestTimestamp = t; }
    public void setResponseCode(String s) { this.msResponseCode = s; }
    public void setResponseBody(String s) { this.msResponseBody = s; }
    public void setResponseTimestamp(Date t) { this.msResponseTimestamp = t; }

    public int getIdSyncLog() { return this.mnPkSyncLogId; }
    public String getSyncType() { return this.msSyncType; }
    public String getRequestBody() { return this.msRequestBody; }
    public Date getRequestTimestamp() { return this.msRequestTimestamp; }
    public String getResponseCode() { return this.msResponseCode; }
    public String getResponseBody() { return this.msResponseBody; }
    public Date getResponseTimestamp() { return this.msResponseTimestamp; }

    public List<SDbSyncLogEntry> getSyncLogEntries() { return mlSyncLogEntries; }

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
        msSyncType = "";
        msRequestBody = "";
        msRequestTimestamp = null;
        msResponseCode = "";
        msResponseBody = "";
        msResponseTimestamp = null;
        mnFkUserId = 0;
        mtTsUser = null;

        mlSyncLogEntries = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_sync_log = " + mnPkSyncLogId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_sync_log = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkSyncLogId = 0;

        msSql = "SELECT COALESCE(MAX(id_sync_log), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkSyncLogId = resultSet.getInt(1);
        }
    }
    
    public int getThePk(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkSyncLogId = 0;

        msSql = "SELECT COALESCE(MAX(id_sync_log), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        
        return 0;
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
            msSyncType = resultSet.getString("sync_type");
            msRequestBody = resultSet.getString("request_body");
            msRequestTimestamp = resultSet.getTimestamp("request_timestamp");
            msResponseCode = resultSet.getString("response_code");
            msResponseBody = resultSet.getString("response_body");
            msResponseTimestamp = resultSet.getTimestamp("response_timestamp");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");

            mbRegistryNew = false;

            // Read sync log entries:
            mlSyncLogEntries.clear();
            msSql = "SELECT * FROM " + SModConsts.TablesMap.get(SModConsts.CFG_SYNC_LOG_ETY) + " " +
                    "WHERE id_sync_log = " + mnPkSyncLogId + " ORDER BY id_sync_log_entry ";
            ResultSet resultSetEntries = session.getStatement().executeQuery(msSql);
            while (resultSetEntries.next()) {
                SDbSyncLogEntry oSyncLogEntry = new SDbSyncLogEntry();
                oSyncLogEntry.read(session, new int[] { resultSetEntries.getInt("id_sync_log_entry") });
                mlSyncLogEntries.add(oSyncLogEntry);
            }
        }
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        // limitar msRequestBody y msResponseBody a 64 KB, para evitar problemas de tamaño en la base de datos:
        if (msRequestBody != null && msRequestBody.length() > 65536) {
            msRequestBody = msRequestBody.substring(0, 65536);
        }
        if (msResponseBody != null && msResponseBody.length() > 65536) {
            msResponseBody = msResponseBody.substring(0, 65536);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserId = session.getUser().getPkUserId();

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkSyncLogId + ", " +
                    "'" + msSyncType + "', " +
                    "'" + msRequestBody + "', " +
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(msRequestTimestamp) + "', " +
                    "'" + msResponseCode + "', " +
                    "'" + msResponseBody + "', " +
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(msResponseTimestamp) + "', " +
                    mnFkUserId + ", " +
                    "NOW()" + ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "sync_type = '" + msSyncType + "', " +
                    "request_body = '" + msRequestBody + "', " +
                    "request_timestamp = '" + SLibUtils.DbmsDateFormatDatetime.format(msRequestTimestamp) + "', " +
                    "response_code = '" + msResponseCode + "', " +
                    "response_body = '" + msResponseBody + "', " +
                    "response_timestamp = '" + SLibUtils.DbmsDateFormatDatetime.format(msResponseTimestamp) + "' " +
                    // ts_usr_ins is not updated, as it is the timestamp of the first insert
                    // ts_usr_upd is not updated, as it is the timestamp of the last update
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save sync log entries:
        for (SDbSyncLogEntry oSyncLogEntry : mlSyncLogEntries) {
            oSyncLogEntry.setIdSyncLog(mnPkSyncLogId);
            oSyncLogEntry.save(session);
        }

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
