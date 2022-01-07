/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.redis;

import erp.client.SClientInterface;
import sa.lib.srv.SSrvLock;
import sa.lib.srv.SSrvUtils;
import sa.lib.srv.redis.SRedisLock;
import sa.lib.srv.SLock;

/**
 *
 * @author SW
 */
public class SLockUtils {
    public static SLock gainLock(SClientInterface client, int registryType, Object registryPk, long timeout) throws Exception {
        boolean withServer = client.getParamsApp().getWithServer();
        SLock oLock;
        if (withServer) {
            SSrvLock lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), registryType, registryPk, timeout);             
            oLock = new SLock(lock);
        }
        else {
            SRedisLock redisLock = SRedisLockUtils.gainLock(client, registryType, registryPk, timeout/1000);
            oLock = new SLock(redisLock);
        }
        
        return oLock;
    }
    
    public static void releaseLock(SClientInterface client, SLock lock) throws Exception {
        boolean withServer = client.getParamsApp().getWithServer();
        if (withServer) {
            SSrvUtils.releaseLock(client.getSession(), lock.getServerLock());             
        }
        else {
            SRedisLockUtils.releaseLock(client, lock.getRedisLock());
        }
    }
    
    public static SLock verifyLockStatus(SClientInterface client, SLock slock) throws Exception {
        boolean withServer = client.getParamsApp().getWithServer();
        SLock oLock;
        if (withServer) {
            SSrvLock lock = SSrvUtils.verifyLockStatus(client.getSession(), slock.getServerLock());             
            oLock = new SLock(lock);
        }
        else {
            SRedisLock redisLock = SRedisLockUtils.verifyLockStatus(client, slock.getRedisLock());
            oLock = new SLock(redisLock);
        }
        
        return oLock;
    }
}
