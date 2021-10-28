package erp.redis;

import java.util.Date;

public class SRedisLock {

    private Object moRegistryPk;
    private long mlTimeout;
    protected SLockKey moLockKey;
    private Date mtLockTimestamp;
    
    public SRedisLock(final Object registryPk, final long timeout, final SLockKey lockKey) {
        moRegistryPk = registryPk;
        mlTimeout = timeout;
        moLockKey = lockKey;
        updateLockTimestamp();
    }

    public final void updateLockTimestamp() {
        mtLockTimestamp = new Date();
    }
    
    public Object getRegistryPk() {
        return moRegistryPk;
    }
    
    public long getTimeout() {
        return mlTimeout;
    }
    
    public SLockKey getLockKey() {
        return moLockKey;
    }

    public Date getLockTimestamp() {
        return mtLockTimestamp;
    }
}
