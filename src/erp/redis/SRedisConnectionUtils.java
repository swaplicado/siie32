/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.redis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import redis.clients.jedis.Jedis;
import sa.lib.srv.SSrvCompany;

/**
 *
 * @author Adrián Avilés
 */
public abstract class SRedisConnectionUtils {

    public static final int REDIS_PORT = 6379; // puerto por defecto de Redis
    public static final String KEY_COMPANIES = "companies"; // key de Redis que contiene un hash de companyId:companyName
    public static final String SESSION = "SESSION";

    private static final SimpleDateFormat DateFormatDatetime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /**
     * Establecer conexión a servidor Redis.
     *
     * @param host Nombre del host de Redis.
     * @param psw
     * @return regresa el cliente de Redis <code>Jedis</code>.
     * @throws Exception
     */
    public static Jedis connect(final String host, final String psw) throws Exception {
        Jedis jedis = null;

        try {
            jedis = new Jedis(host, REDIS_PORT);
            jedis.auth(psw);
            jedis.connect();
        }
        catch (Exception e) {
            throw new Exception(e);
        }

        return jedis;
    }

    /**
     * Crear la key de Redis companies, companies contiene un Hash con el id de
     * la compañia como llave y el nombre como valor.
     *
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
     * Metodo para agregar la sesion de usuario como una key en Redis, esta key
     * queda de la forma: SESSION+jedis.clientId()+companyId+userId+userName
     *
     * @param jedis Cliente de Redis
     * @param companyId id de la compañia del usuario
     * @param userId id del usuario
     * @param userName nombre de usuario
     */
    public static void setSessionsUsers(Jedis jedis, int companyId, int userId, String userName) {
        String session = SESSION + "+"
                + jedis.clientId() + "+"
                + companyId + "+"
                + userId + "+"
                + userName;
        jedis.setex(session, 86400, DateFormatDatetime.format(new Date()));
    }

    /**
     * Metodo para obtener un vector con los key de las sesiones en Redis. El
     * vector contendra las keys de la forma:
     * SESSION+jedis.clientId()+companyId+userId+userName
     *
     * @param jedis
     * @return
     */
    public static Vector getSessionsUsers(Jedis jedis) {
        Set<String> keys = jedis.keys(SESSION + "+*");
        Vector vectorKeys = new Vector();
        for (int i = 0; i < keys.size(); i++) {
            vectorKeys.add(keys.toArray()[i].toString());
        }
        return vectorKeys;
    }

    /**
     * Poner nombre a la sesion de Redis, el nombre de la sesion de Redis queda
     * de la forma: redisSessionId+companyId+userId+userName.
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

    /**
     * Metodo que verifica si existe conexión con servidor Redis, Si no, intenta
     * conectar y crea nuevo objeto jedis en client
     *
     * @param client
     * @return regresa true si existe conexión, false si no.
     */
    public static boolean getConnectionStatus(erp.client.SClientInterface client) {
        boolean connection;
        Jedis jedis = client.getJedis();
        try {
            jedis.ping();
            connection = true;
        }
        catch (Exception e) {
            connection = false;
        }

        if (!connection) {
            try {
                jedis = connect(client.getParamsApp().getRedisHost(), client.getParamsApp().getRedisPswd());
                setSessionName(jedis, client.getSessionXXX().getCompany().getPkCompanyId(),
                        client.getSessionXXX().getUser().getPkUserId(), client.getSessionXXX().getUser().getName());
                setSessionsUsers(jedis, client.getSessionXXX().getCompany().getPkCompanyId(),
                        client.getSessionXXX().getUser().getPkUserId(), client.getSessionXXX().getUser().getName());
                client.setJedis(jedis);
                connection = true;
            }
            catch (Exception e) {
                connection = false;
            }
        }

        return connection;
    }
}
