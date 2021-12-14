/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.redis;

import erp.client.SClientInterface;
import sa.lib.gui.SGuiClient;
import sa.lib.srv.redis.SRedisLock;
import sa.lib.srv.redis.SRedisLockManagerInterface;

/**
 *
 * @author SW
 */
public class SRedisLockManager implements SRedisLockManagerInterface{

    @Override
    public SRedisLock gainLock(SGuiClient client, int registryType, Object registryPk, long timeout) throws Exception{
        SRedisLock rlock = null;
        rlock = SRedisLockUtils.gainLock((SClientInterface) client, registryType, registryPk, timeout);
        return rlock;
    }

    @Override
    public SRedisLock verifyLockStatus(SGuiClient client, SRedisLock rlock) throws Exception{
        rlock = SRedisLockUtils.verifyLockStatus((SClientInterface) client, rlock);
        return rlock;
    }

    @Override
    public void releaseLock(SGuiClient client, SRedisLock rlock) {
        SRedisLockUtils.releaseLock((SClientInterface) client, rlock);
    }
    
}
