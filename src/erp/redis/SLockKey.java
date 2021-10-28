/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.redis;

import erp.lib.SLibUtilities;

/**
 *
 * @author Adrián Avilés
 */
public class SLockKey {

    public static final String LOCK = "Lock";
    public static final int KEY_LOCK_ID_INDEX = 1;
    public static final int KEY_COMPANY_ID_INDEX = 2;
    public static final int KEY_REGISTRY_TYPE_INDEX = 3;
    public static final int KEY_REGISTRY_PK_INDEX = 4;
    public static final int KEY_SESSION_ID_INDEX = 5;
    public static final int KEY_USER_ID_INDEX = 6;

    protected long mlLockId;
    protected int mnCompanyId;
    protected int mnRegistryType;
    protected String msRegistryPk;
    protected long mlSessionId;
    protected int mnUserId;
    protected String msLockKey;

    public SLockKey(final String lockKey) {
        String[] key = msLockKey.split("\\+");
        setLockKey(
                SLibUtilities.parseInt(key[KEY_LOCK_ID_INDEX]),
                SLibUtilities.parseInt(key[KEY_COMPANY_ID_INDEX]),
                SLibUtilities.parseInt(key[KEY_REGISTRY_TYPE_INDEX]),
                key[KEY_REGISTRY_PK_INDEX],
                SLibUtilities.parseInt(key[KEY_SESSION_ID_INDEX]),
                SLibUtilities.parseInt(key[KEY_USER_ID_INDEX])
        );

    }
    
    public SLockKey(final long lockId, final int companyId, final int registryType, final String registryPk, final long sessionId, final int userId) {
        setLockKey(lockId, companyId, registryType, registryPk, sessionId, userId);
    }
    
    private void setLockKey(final long lockId, final int companyId, final int registryType, final String registryPk, final long sessionId, final int userId) {
        mlLockId = lockId;
        mnCompanyId = companyId;
        mnRegistryType = registryType;
        msRegistryPk = registryPk;
        mlSessionId = sessionId;
        mnUserId = userId;

        msLockKey = composeLockKey("" + mlLockId, "" + mnCompanyId, "" + mnRegistryType, msRegistryPk, "" + mlSessionId, "" + mnUserId);
    }

    public long getLockId() {
        return mlLockId;
    }

    public int getCompanyId() {
        return mnCompanyId;
    }

    public int getRegistryType() {
        return mnRegistryType;
    }

    public String getRegistryPk() {
        return msRegistryPk;
    }

    public long getSessionId() {
        return mlSessionId;
    }

    public int getUserId() {
        return mnUserId;
    }

    public String getLockKey() {
        return msLockKey;
    }
    
    public String getLockKeyForSearch() {
        return composeLockKey("*", "" + mnCompanyId, "" + mnRegistryType, msRegistryPk, "*", "*");
    }
    
    public static String composeLockKey(final String lockId, final String companyId, final String registryType, final String registryPk, final String sessionId, final String userId) {
        return LOCK + "+"
                + lockId + "+"
                + companyId + "+"
                + registryType + "+"
                + registryPk + "+"
                + sessionId + "+"
                + userId;
    }
    
    public static String composeLockKeyForSearch(final int companyId, final int registryType, final String registryPk) {
        return composeLockKey("*", "" + companyId, "" + registryType, registryPk, "*", "*");
    }
}
