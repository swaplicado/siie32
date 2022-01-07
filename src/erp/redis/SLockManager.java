/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.redis;

import erp.client.SClientInterface;
import sa.lib.gui.SGuiClient;
import sa.lib.srv.SLock;
import sa.lib.srv.SLockManagerInterface;

/**
 *
 * @author SW
 */
public class SLockManager implements SLockManagerInterface{
    
    @Override
    public SLock gainLock(SGuiClient client, int registryType, Object registryPk, long timeout) throws Exception{
        SLock slock = null;
        slock = SLockUtils.gainLock((SClientInterface) client, registryType, registryPk, timeout);
        return slock;
    }

    @Override
    public SLock verifyLockStatus(SGuiClient client, SLock slock) throws Exception{
        slock = SLockUtils.verifyLockStatus((SClientInterface) client, slock);
        return slock;
    }

    @Override
    public void releaseLock(SGuiClient client, SLock slock) throws Exception {
        SLockUtils.releaseLock((SClientInterface) client, slock);
    }
    
}
