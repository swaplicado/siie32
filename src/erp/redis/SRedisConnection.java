package erp.redis;

import erp.lib.SLibUtilities;
import java.util.ArrayList;
import java.util.HashMap;
import redis.clients.jedis.Jedis;
import sa.lib.srv.SSrvCompany;

public class SRedisConnection {
    
    protected Jedis moJedis;
    protected String msHost;
    protected int mnPort;
    protected int mnCompanyId;
    protected int mnUserId;
    protected String msUserName;
    protected long mnSessionId;
    protected String msSessionName;
    
    public SRedisConnection(final Jedis jedis, final String host, final int port, final int companyId,
                           final int userId, final String userName, final long sessionId, final String sessionName) {
        moJedis = jedis;
        msHost = host;
        mnPort = port;
        mnCompanyId = companyId;
        mnUserId = userId;
        msUserName = userName;
        mnSessionId = sessionId;
        msSessionName = sessionName;
    }
    
    public Jedis getJedis() {
        return moJedis;
    }
    
    public String getHost() {
        return msHost;
    }
    
    public int getPort() {
        return mnPort;
    }
    
    public long getSessionId() {
        return mnSessionId;
    }
    
    public String getSessionName() {
        return msSessionName;
    }
    
    /*
    public static final int REDIS_PORT = 6379;
    public static final String KEY_COMPANIES = "companies";

    private Jedis moJedis;

    public Jedis conect(String host) {
        try {
            moJedis = new Jedis(host, REDIS_PORT);
            moJedis.connect();
        } catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        return moJedis;
    }

    public void setCompanies(final ArrayList<SSrvCompany> companies) {
        HashMap<String, String> companiesMap = new HashMap<>();

        for (SSrvCompany c : companies) {
            companiesMap.put(Integer.toString(c.getCompanyId()), c.getCompany());
        }
        
        if (!moJedis.exists(KEY_COMPANIES)) {
            moJedis.hset(KEY_COMPANIES, companiesMap);
        }
    }

    public void setSessionName(final Jedis jedis, final int companyId, final int userId, final String userName) {
        moJedis = jedis;
        moJedis.clientSetname(moJedis.clientId() + "+" + companyId + "+" + userId + "+" + userName);
    }*/
}
