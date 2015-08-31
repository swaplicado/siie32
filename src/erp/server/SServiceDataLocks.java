/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.server;

import java.io.Serializable;
import java.util.Vector;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
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


    private SSrvLock getLock(final int companyId, final int registryType, Object pk) {
        SSrvLock lockFound = null;

        for (SSrvLock lock : mvLocks) {
            if (companyId == lock.getCompanyId() && registryType == lock.getRegistryType() && SLibUtils.compareKeys(pk, lock.getPrimaryKey())) {
                lockFound = lock;
                break;
            }
        }

        return lockFound;
    }

    /*
     * Public methods:
     */

    public synchronized void renderLocks() {
        moServer.renderMessageLn(msService + "Active locks: [" + mvLocks.size() + "]");
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

    public synchronized SSrvLock recoverLock(final int sessionId, final int companyId, final int registryType, final Object key, final long timeout) {
        SSrvLock lock = getLock(companyId, registryType, key);

        moServer.renderMessageLn(msService + "Recovering lock for session [" + sessionId + "] (company=" + companyId + ", type=" + registryType + ")...");

        if (lock != null) {
            lock = lock.clone();
            lock.setLockStatus(SSrvConsts.LOCK_RECOVER_DENIED);
        }
        else {
            lock = new SSrvLock(sessionId, companyId, registryType, key, timeout, moServer.getSystemDatetime());
            lock.setLockId(++mlCurrentLockId);
            lock.setLockStatus(SSrvConsts.LOCK_RECOVERED);
            lock.setLockUser(moServer.getServiceSessions().getSession(lock.getSessionId()).getUserName());
            mvLocks.add(lock);
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
