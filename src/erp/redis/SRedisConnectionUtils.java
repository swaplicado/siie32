/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.redis;

import java.util.ArrayList;
import java.util.HashMap;
import redis.clients.jedis.Jedis;
import sa.lib.srv.SSrvCompany;

/**
 *
 * @author Adrián Avilés
 */
public abstract class SRedisConnectionUtils {

    public static final int REDIS_PORT = 6379; // puerto por defecto de Redis
    public static final String KEY_COMPANIES = "companies"; // key de Redis que contiene un hash de companyId:companyName

    /**
     * Establecer conexión a servidor Redis.
     * @param host Nombre del host de Redis.
     * @return regresa el cliente de Redis <code>Jedis</code>.
     * @throws Exception
     */
    public static Jedis connect(final String host) throws Exception {
        Jedis jedis = null;

        try {
            jedis = new Jedis(host, REDIS_PORT);
            jedis.connect();
        }
        catch (Exception e) {
            throw new Exception(e);
        }
        
        return jedis;
    }

    /**
     * Crear la key de Redis companies, companies contiene un Hash con el id de la compañia como llave y el nombre como valor.
     * @param jedis Cliente Jedis.
     * @param companies Lista de compañías del servidor SIIE.
     */
    public static void setCompanies(final Jedis jedis, final ArrayList<SSrvCompany> companies) {
        HashMap<String, String> companiesMap = new HashMap<>();

        for (SSrvCompany company : companies) {
            companiesMap.put(Integer.toString(company.getCompanyId()), company.getCompany());
        }

        if (!jedis.exists(KEY_COMPANIES)) {
            jedis.hset(KEY_COMPANIES, companiesMap);
        }
    }

    /**
     * Poner nombre a la sesion de Redis, el nombre de la sesion de Redis queda de la forma: redisSessionId+companyId+userId+userName.
     *
     * @param jedis
     * @param companyId
     * @param userId
     * @param userName
     */
    public static void setSessionName(final Jedis jedis, final int companyId, final int userId, final String userName) {
        jedis.clientSetname(jedis.clientId() + "+" + companyId + "+" + userId + "+" + userName);
    }

    public static String getRedisClientList(final Jedis jedis) {
        return jedis.clientList();
    }
}
