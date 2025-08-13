/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.db;

import erp.mod.SModConsts;
import erp.mod.cfg.swap.utils.SExportUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 * Clase para registrar la sincronización de datos con servicios externos.
 *
 * @author Edwin Carmona, Sergio Flores
 */
public class SDbSyncLog extends SDbRegistryUser {
    
    protected static final String SUFIX_REQUEST_BODY = "_request_body";
    protected static final String SUFIX_RESPONSE_BODY = "_response_body";
    
    protected int mnPkSyncLogId;
    protected String msSyncType;
    protected String msRequestBodyFileName;
    protected Date mtRequestTimestamp;
    protected String msResponseCode;
    protected String msResponseBodyFileName;
    protected Date mtResponseTimestamp;
    protected int mnFkUserId;
    protected Date mtTsUser;
    
    protected ArrayList<SDbSyncLogEntry> moEntries;
    
    protected String msAuxDatabase;
    
    protected Class moClass;
    protected Class moChildClass;
    protected int mnChildType;
    
    public SDbSyncLog() {
        super(SModConsts.CFG_SYNC_LOG);
        
        moClass = getClass();
        moChildClass = SDbSyncLogEntry.class;
        mnChildType = SModConsts.CFG_SYNC_LOG_ETY;
    }

    public void setPkSyncLogId(int n) { mnPkSyncLogId = n; }
    public void setSyncType(String s) { msSyncType = s; }
    //public void setRequestBodyFileName(String s) { msRequestBody = s; } // ¡not required!
    public void setRequestTimestamp(Date t) { mtRequestTimestamp = t; }
    public void setResponseCode(String s) { msResponseCode = s; }
    //public void setResponseBodyFileName(String s) { msResponseBody = s; } // ¡not required!
    public void setResponseTimestamp(Date t) { mtResponseTimestamp = t; }
    //public void setFkUserId(int n) { mnFkUserId = n; } // ¡not required!
    //public void setTsUser(Date t) { mtTsUser = t; } // ¡not required!

    public int getPkSyncLogId() { return mnPkSyncLogId; }
    public String getSyncType() { return msSyncType; }
    public String getRequestBodyFileName() { return msRequestBodyFileName; }
    public Date getRequestTimestamp() { return mtRequestTimestamp; }
    public String getResponseCode() { return msResponseCode; }
    public String getResponseBodyFileName() { return msResponseBodyFileName; }
    public Date getResponseTimestamp() { return mtResponseTimestamp; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }

    public ArrayList<SDbSyncLogEntry> getEntries() { return moEntries; }

    public void setAuxDatabase(String s) { msAuxDatabase = s; }
    
    public String getAuxDatabase() { return msAuxDatabase; }
    
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
        msRequestBodyFileName = "";
        mtRequestTimestamp = null;
        msResponseCode = "";
        msResponseBodyFileName = "";
        mtResponseTimestamp = null;
        mnFkUserId = 0;
        mtTsUser = null;

        moEntries = new ArrayList<>();
        
        msAuxDatabase = "";
    }

    @Override
    public String getSqlTable() {
        return (msAuxDatabase.isEmpty() ? "" : msAuxDatabase + ".") + SModConsts.TablesMap.get(mnRegistryType);
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

        msSql = "SELECT COALESCE(MAX(id_sync_log), 0) + 1 "
                + "FROM " + getSqlTable() + ";";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkSyncLogId = resultSet.getInt(1);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * "
                + "FROM " + getSqlTable() + " " + getSqlWhere(pk);
        
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkSyncLogId = resultSet.getInt("id_sync_log");
            msSyncType = resultSet.getString("sync_type");
            msRequestBodyFileName = resultSet.getString("request_body");
            mtRequestTimestamp = resultSet.getTimestamp("request_timestamp");
            msResponseCode = resultSet.getString("response_code");
            msResponseBodyFileName = resultSet.getString("response_body");
            mtResponseTimestamp = resultSet.getTimestamp("response_timestamp");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");

            moEntries.clear();
            
            msSql = "SELECT id_ety "
                    + "FROM " + SModConsts.TablesMap.get(mnChildType) + " "
                    + "WHERE id_sync_log = " + mnPkSyncLogId + " "
                    + "ORDER BY id_ety;";
            
            try (ResultSet resultSetEntries = session.getStatement().executeQuery(msSql)) {
                Constructor<?> constructor = moChildClass.getConstructor();
                
                while (resultSetEntries.next()) {
                    SDbSyncLogEntry entry = (SDbSyncLogEntry) constructor.newInstance(new Object[] {});
                    entry.setAuxDatabase(msAuxDatabase);
                    entry.read(session, new int[] { mnPkSyncLogId, resultSetEntries.getInt("id_ety") });
                    moEntries.add(entry);
                }
            }

            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew) {
            // grab timestamp from DBMS:
            
            Date now = null;
            
            msSql = "SELECT NOW();";
            try (ResultSet resultSet = session.getStatement().executeQuery(msSql)) {
                if (resultSet.next()) {
                    now = resultSet.getTimestamp(1);
                }
            }
            
            mtTsUser = now;
            
            if (mtRequestTimestamp == null) {
                mtRequestTimestamp = mtTsUser;
            }
            
            if (mtResponseTimestamp == null) {
                mtResponseTimestamp = mtTsUser;
            }

            // save registry:
            
            computePrimaryKey(session);
            mnFkUserId = session.getUser().getPkUserId();
            
            String baseFileName = msSyncType + "_" +
                    SExportUtils.FormatSyncLogId.format(mnPkSyncLogId) + "_" + // PK has been already set!
                    SExportUtils.FormatSyncLogDatetime.format(mtTsUser);
            
            msRequestBodyFileName = baseFileName + SUFIX_REQUEST_BODY;
            msResponseBodyFileName = baseFileName + SUFIX_RESPONSE_BODY;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkSyncLogId + ", " + 
                    "'" + msSyncType + "', " + 
                    "'" + msRequestBodyFileName + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtRequestTimestamp) + "', " +
                    "'" + msResponseCode + "', " + 
                    "'" + msResponseBodyFileName + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtResponseTimestamp) + "', " +
                    mnFkUserId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtTsUser) + "'" + ");";
        }
        else {
            throw new Exception(SDbConsts.ERR_MSG_REG_NON_UPDATABLE);
        }

        session.getStatement().execute(msSql);

        for (SDbSyncLogEntry entry : moEntries) {
            entry.setPkSyncLogId(mnPkSyncLogId);
            entry.setTsSync(mtTsUser); // sync as well the sync entry!
            entry.setAuxDatabase(msAuxDatabase);
            entry.save(session);
        }

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbSyncLog registry = null;
        
        try {
            Constructor<?> constructor = moClass.getConstructor();
            
            registry = (SDbSyncLog) constructor.newInstance(new Object[] {});

            registry.setPkSyncLogId(this.getPkSyncLogId());
            registry.setSyncType(this.getSyncType());
            registry.msRequestBodyFileName = this.getRequestBodyFileName();
            registry.mtRequestTimestamp = this.getRequestTimestamp();
            registry.setResponseCode(this.getResponseCode());
            registry.msResponseBodyFileName = this.getResponseBodyFileName();
            registry.mtResponseTimestamp = this.getResponseTimestamp();
            registry.mnFkUserId = this.getFkUserId();
            registry.mtTsUser = this.getTsUser();

            for (SDbSyncLogEntry entry : this.moEntries) {
                registry.getEntries().add((SDbSyncLogEntry) entry.clone());
            }
            
            registry.setAuxDatabase(this.getAuxDatabase());
            
            registry.moClass = this.moClass;
            registry.moChildClass = this.moChildClass;
            registry.mnChildType = this.mnChildType;
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            SLibUtils.printException(this, e);
        }
        
        return registry;
    }
}
