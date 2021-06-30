/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.server;

import erp.data.SDataConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.srv.SSrvConsts;
import sa.lib.srv.SSrvLock;

/**
 *
 * @author Sergio Flores
 */
public class SServiceDataLocks implements Serializable {

    private SServer moServer;
    private long mlCurrentLockId;
    private Vector<SSrvLock> mvLocks;

    private final String msService = "[Locks Srv]: ";

    public SServiceDataLocks(SServer server) {
        moServer = server;
        mlCurrentLockId = 0;
        mvLocks = new Vector<SSrvLock>();
    }

    /*
     * Private methods:
     */

    private boolean isLockAlive(final SSrvLock lock) {
        return moServer.getServiceSessions().isSessionAlive(lock.getSessionId()) &&
                moServer.getSystemDatetime().getTime() - lock.getTimestamp().getTime() < lock.getTimeout();
    }

    private SSrvLock getLock(final long lockId) {
        SSrvLock lockFound = null;

        for (SSrvLock lock : mvLocks) {
            if (lockId == lock.getLockId()) {
                lockFound = lock;
                break;
            }
        }

        return lockFound;
    }


    private SSrvLock getLock(final int companyId, final int registryType, final Object pk) {
        SSrvLock lockFound = null;

        for (SSrvLock lock : mvLocks) {
            if (companyId == lock.getCompanyId() && registryType == lock.getRegistryType() && SLibUtils.compareKeys(pk, lock.getPrimaryKey())) {
                lockFound = lock;
                break;
            }
        }

        return lockFound;
    }

    /** Obtains last update timestamp of provided data registry.
     * 
     * @param sessionId Client user session.
     * @param typeRegistry Constants defined in erp.data.SDataConstants.
     * @param pk Primary key of data registry.
     * @return Last timestamp of data registry when found, otherwise <code>null</code>.
     */
    private Date getLastUpdateTimestamp(final int sessionId, final int registryType, final Object pk) {
        int i = 0;
        Date lastTs = null;
        String fieldNameTs = "";
        String tableName = "";
        String sql = "";
        HashMap<String, String> primaryKeyNames = new HashMap<>();
        ResultSet resultSet = null;
         
        if (registryType <= SDataConstants.MAX_ID) {
            // Former framework table and column names:
            
            tableName = SDataConstants.TablesMap.get(registryType);
            fieldNameTs = "ts_edit";
        }
        else {
            // Current framework table and column names:
            
            tableName = SModConsts.TablesMap.get(registryType);
            fieldNameTs = "ts_usr_upd";
        }
        
        try {
            // Retrieve primary key columns:
            
            sql = "SHOW COLUMNS FROM " + tableName + " WHERE `Key` = 'PRI' ";
            resultSet = moServer.getServiceSessions().getSession(sessionId).getStatement().executeQuery(sql);
                
            while (resultSet.next()) {
                primaryKeyNames.put(resultSet.getString("Field"), resultSet.getString("Type"));
            }
            
            // Validate size of provided key vs. size of retrieved primary key columns:
            
            if (((Object[]) pk).length != primaryKeyNames.size()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + "\n" + SSrvConsts.MSG_ERR_LOCK_ON_VERIFY);
            }
            
            // Get last timestamp of desired registry:
            
            sql = "SELECT " + fieldNameTs + " FROM " + tableName + " WHERE ";

            for (HashMap.Entry<String, String> entry : primaryKeyNames.entrySet()) {
                sql += entry.getKey() + " = ";
                
                if (entry.getValue().contains("char")) {
                    sql += "'" + ((String) ((Object[]) pk)[i++]) + "' ";
                }
                else if (entry.getValue().contains("date")) {
                    sql += "'" + SLibUtils.DbmsDateFormatDate.format((Date) ((Object[]) pk)[i++]) + "' ";
                }
                else {
                    sql += ((Integer) ((Object[]) pk)[i++]) + " ";
                }
            }

            resultSet = moServer.getServiceSessions().getSession(sessionId).getStatement().executeQuery(sql);

            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                lastTs = resultSet.getTimestamp(fieldNameTs);
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.printOutException(this, e);
        } 
        
        return lastTs;
    }
    
    /*
     * Public methods:
     */

    public synchronized void renderLocks() {
        String message = "Active locks: [" + mvLocks.size() + "]";
        for (SSrvLock lock : mvLocks) {
            message += "\n lock user: [" + lock.getLockUser() + "], lock session id: [" + lock.getSessionId() + "]";
        } 
        moServer.renderMessageLn(msService + message);
    }

    public synchronized void evaluateTimeouts() {
        int i = 0;

        moServer.renderMessageLn(msService + "Updating lock timeouts...");

        while (i < mvLocks.size()) {
            if (!isLockAlive(mvLocks.get(i))) {
              mvLocks.remove(i);
            }
            else {
                i++;    // evaluate next lock
            }
        }

        moServer.renderMessageLn(msService + "Lock timeouts updated!");
    }

    public synchronized void terminateLocks(final int sessionId, final boolean showPrompt) {
        int i = 0;
        int count = 0;
        String message = "";

        while (i < mvLocks.size()) {
            if (sessionId == mvLocks.get(i).getSessionId()) {
                mvLocks.remove(i);
                count++;
            }
            else {
                i++;    // check next lock
            }
        }

        if (count == 0) {
            message = "No locks for session [" + sessionId + "] found!";
        }
        else {
            message = "[" + count + "] lock" + (count == 1 ? "" : "s") + " for session [" + sessionId + "] terminated!";
        }

        if (showPrompt) {
            moServer.renderMessageLn(msService + message);
        }
        else {
            moServer.renderMessage(msService + message);
        }
    }

    public synchronized void terminateAllLocks(final boolean showPrompt) {
        int count = mvLocks.size();
        String message = "";

        mvLocks.clear();

        if (count == 0) {
            message = "No locks found!";
        }
        else {
            message = "[" + count + "] lock" + (count == 1 ? "" : "s") + " terminated!";
        }

        if (showPrompt) {
            moServer.renderMessageLn(msService + message);
        }
        else {
            moServer.renderMessage(msService + message);
        }
    }

    public synchronized int getLockStatus(final long lockId) {
        int status = SLibConsts.UNDEFINED;
        SSrvLock lock = getLock(lockId);

        if (lock != null) {
            status = isLockAlive(lock) ? SSrvConsts.LOCK_ST_ALIVE : SSrvConsts.LOCK_ST_EXPIRED;
        }

        return status;
    }

    public synchronized SSrvLock gainLock(final int sessionId, final int companyId, final int registryType, final Object pk, final long timeout) {
        SSrvLock lock = getLock(companyId, registryType, pk);

        moServer.renderMessageLn(msService + "Gaining lock for session [" + sessionId + "] (company=" + companyId + ", type=" + registryType + ")...");

        if (lock != null) {
            lock = lock.clone();
            lock.setLockStatus(SSrvConsts.LOCK_GAIN_DENIED);
        }
        else {
            lock = new SSrvLock(sessionId, companyId, registryType, pk, timeout, moServer.getSystemDatetime());
            lock.setLockId(++mlCurrentLockId);
            lock.setLockStatus(SSrvConsts.LOCK_GAINED);
            lock.setLockUser(moServer.getServiceSessions().getSession(lock.getSessionId()).getUserName());
            mvLocks.add(lock);
        }

        return lock;
    }

    public synchronized SSrvLock recoverLock(final int sessionId, final int companyId, final int registryType, final Object key, final long timeout, final Date timestamp_n) {
        Date lastTs = null;
        SSrvLock lock = new SSrvLock(sessionId, companyId, registryType, key, timeout, moServer.getSystemDatetime());

        moServer.renderMessageLn(msService + "Recovering lock for session [" + sessionId + "] (company=" + companyId + ", type=" + registryType + ")...");

        if (getLock(companyId, registryType, key) != null) {
            // Data registry is already locked:
            
            lock.setLockStatus(SSrvConsts.LOCK_RECOVER_DENIED);
        }
        else {
            // Lock is not longer active, check if it can be recovered:
            
            lastTs = getLastUpdateTimestamp(sessionId, registryType, key);
            
            if (lastTs == null || lastTs.after(timestamp_n)) {
                lock.setLockStatus(SSrvConsts.LOCK_RECOVER_DENIED);
            }
            else {
                lock.setLockId(++mlCurrentLockId);
                lock.setLockStatus(SSrvConsts.LOCK_RECOVERED);
                lock.setLockUser(moServer.getServiceSessions().getSession(lock.getSessionId()).getUserName());
                mvLocks.add(lock);
            }
        }

        return lock;
    }

    public synchronized void releaseLock(final long lockId) {
        moServer.renderMessageLn(msService + "Releasing lock [" + lockId + "] ...");

        for (int i = 0; i < mvLocks.size(); i++) {
            if (lockId == mvLocks.get(i).getLockId()) {
                mvLocks.remove(i);
                break;
            }
        }
    }

    public synchronized void terminateService() {
        moServer.renderMessage(msService + "Terminating service...");

        terminateAllLocks(false);

        moServer.renderMessage(msService + "Service terminated!");
    }
}
