/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.redis;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;
import javax.swing.JOptionPane;
import redis.clients.jedis.Jedis;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.srv.redis.SRedisLock;
import sa.lib.srv.redis.SRedisLockKey;

/**
 *
 * @author Adrián Avilés
 */
public abstract class SRedisLockUtils {

    private static final String[] TsEditFieldNames = new String[]{"ts_edit", "ts_upd", "ts_usr_upd"};

    public static final String LOCK_COUNT = "LockIdCount"; // contador de id de los locks

    private static final SimpleDateFormat DateFormatDatetime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    

    /**
     * Método para pasar el objeto PK a un string.
     *
     * @param client Cliente GUI.
     * @param registryType Tipo de registro. Constantes definidas en
     * <code>SDataConstants</code> o <code>SModConsts</code>.
     * @param registryPk Clave primaria del registro.
     * @return Regresa un <code>String</code> con la forma "pk_1/pk_2/.../pk_n/"
     * @throws java.sql.SQLException
     */
    public static String setFlattenPk(final int registryType, final Object registryPk) throws SQLException {
        String flattenPk = "";
        
        if (registryPk instanceof int[]) {
            for (int index = 0; index < ((int[]) registryPk).length; index++) {
                flattenPk += (((int[]) registryPk)[index]) + "/" + "";
            }
        } 
        else if (registryPk instanceof Object[]) {
            for (int index = 0; index < ((Object[]) registryPk).length; index++) {
                if (!((((Object[]) registryPk)[index]) instanceof Date)) {
                    flattenPk += ((Object[]) registryPk)[index] + "/" + "";
                } 
                else if ((((Object[]) registryPk)[index]) instanceof Date) {
                    flattenPk += SLibUtils.DbmsDateFormatDate.format(((Object[]) registryPk)[index]) + "/" + "";
                }
            }
        }

        return flattenPk;
    }

    /**
     * Método para crear un candado en Redis.
     *
     * @return Regresa la llave del candado en un string en la forma:
     * "Lock+lockId+companyId+registryType+stringId+jedisClientId+userId"
     */
    private static SRedisLockKey setLock(final Jedis jedis, final int companyId, final int registryType, final String registryPk, final int userId, final long timeout) {
        long lockId = jedis.incr(LOCK_COUNT); // incrementa conteo de ID de candado en 1
        SRedisLockKey lockKey = new SRedisLockKey(lockId, companyId, registryType, registryPk, jedis.clientId(), userId);

        jedis.setex(lockKey.getLockKey(), timeout, DateFormatDatetime.format(new Date()) + " : " + timeout); // crea clave Redis con tiempo de expiración en segundos a manera de candado

        return lockKey;
    }

    /**
     * Método para eliminar un candado de Redis.
     *
     * @param client
     * @param redisLock
     */
    public static void releaseLock(erp.client.SClientInterface client, SRedisLock redisLock) {
        if (SRedisConnectionUtils.getConnectionStatus(client)) {
/* Bloque de codigo correspondiente a los candados de Redis
            Jedis jedis = client.getJedis();
            jedis.del(redisLock.getLockKey().getLockKey());
*/            
        } else if (!redisLock.getLockKey().isDummy()) {
            client.showMsgBoxWarning("No se eliminó el candado de acceso exclusivo al registro.\n"
                    + "Favor de comunicarlo al administrador del sistema");
        }
    }

    /**
     * Método para obtener el nombre de usuario a partir de un ID de usuario.
     *
     * @param client Cliente GUI.
     * @param userId ID usuario.
     * @return Nombre de usuario.
     * @throws java.sql.SQLException
     */
    public static String getUserName(SClientInterface client, int userId) throws SQLException {
        String userName = "";

        try (Statement statement = client.getSession().getDatabase().getConnection().createStatement()) {
            String sql = "SELECT usr FROM erp.usru_usr WHERE id_usr = " + userId + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                userName = resultSet.getString(1);
            }
        }

        return userName;
    }

    /**
     * Método que busca un candado de acceso exclusivo al registro por ID de
     * empresa, tipo de registro y PK del registro.
     *
     * @param jedis Cliente Jedis.
     * @param companyId ID empresa.
     * @param registryType Tipo de registro.
     * @param registryPk PK del registro.
     * @return Si existe, regresa un objeto <code>SLockKey</code> del candado
     * correspondiente de acceso exclusivo al registro, si no, regresa
     * <code>null</code>.
     * @throws java.lang.Exception
     */
    public static SRedisLockKey getLockKeyFromRedis(final Jedis jedis, final int companyId, final int registryType, final String registryPk) throws Exception {
        String lockKey = "";
        String lockKeyForSearch = SRedisLockKey.composeLockKeyForSearch(companyId, registryType, registryPk);
        Set<String> keys = jedis.keys(lockKeyForSearch);

        if (!keys.isEmpty()) {
            lockKey = keys.toArray()[0].toString(); // si hay coincidencias, siempre será una sola
        }

        return lockKey.isEmpty() ? null : new SRedisLockKey(lockKey);
    }

    /**
     * Metodo para crear un SRedisLock Dummy, este metodo solo se debe usar si
     * el servidor redis no esta en funcionamiento
     *
     * @param client
     * @param registryType
     * @param registryPk
     * @param timeout
     * @return
     * @throws SQLException
     */
    private static SRedisLock gainLockDummy(erp.client.SClientInterface client, int registryType, Object registryPk, long timeout) throws SQLException {
        int companyId = client.getSessionXXX().getCompany().getPkCompanyId();
        int userId = client.getSessionXXX().getUser().getPkUserId();
        String registryPkFlatten = setFlattenPk(registryType, registryPk);
        SRedisLockKey rLockKey = new SRedisLockKey(0, companyId, registryType, registryPkFlatten, 0, userId);
        SRedisLock rLock = new SRedisLock(registryPk, timeout, rLockKey);

        return rLock;
    }

    /**
     * Crear el candado Redis de acceso exclusivo al registro.
     *
     * @param client Cliente GUI.
     * @param registryType Tipo de registro.
     * @param registryPk PK del registro.
     * @param timeout Timeout del candado de acceso exclusivo al registro.
     * @return Candado Redis de acceso exclusivo al registro como un objeto
     * <code>SRedisLock</code>.
     * @throws java.sql.SQLException
     */
    public static SRedisLock gainLock(SClientInterface client, int registryType, Object registryPk, long timeout) throws Exception, SQLException {
        int companyId = client.getSessionXXX().getCompany().getPkCompanyId();
        int userId = client.getSessionXXX().getUser().getPkUserId();
        String registryPkFlatten = setFlattenPk(registryType, registryPk);
        SRedisLock redisLock = null;
/* Bloque de codigo correspondiente a los candados de Redis
        if (SRedisConnectionUtils.getConnectionStatus(client)) {
            Jedis jedis = client.getJedis();
            SRedisLockKey lockKey = getLockKeyFromRedis(jedis, companyId, registryType, registryPkFlatten);
            if (lockKey != null) {
                // el candado de acceso exclusivo ya existe
                if (lockKey.getUserId() != userId || lockKey.getCompanyId() != companyId) {
                    // el candado de acceso exclusivo pertenece a otro usuario y/o empresa
                    throw new Exception("El registro esta siendo utilizado por: " + getUserName(client, lockKey.getUserId()) + ".");
                } 
                else {
                    // el candado de acceso exclusivo pertenece al mismo usuario y empresa
                    if (client.showMsgBoxConfirm("El registro esta siendo utilizado por usted mismo,\n"
                            + "¿Desea obtener nuevamente el acceso exclusivo al mismo?") != JOptionPane.YES_OPTION) {
                        throw new Exception("El registro esta siendo utilizado por usted mismo.");
                    } 
                    else {
                        jedis.del(lockKey.getLockKey());
                    }
                }
            }
            lockKey = setLock(jedis, companyId, registryType, registryPkFlatten, userId, timeout);     //crea nuevo candado
            redisLock = new SRedisLock(registryPk, timeout, lockKey);
        } 
        else {
            redisLock = gainLockDummy(client, registryType, registryPk, timeout);
        }
*/
        return redisLock;
    }

    /**
     * Obtener la última fecha de modificacion de un registro.
     *
     */
    private static Date getLastUpdateTs(SClientInterface client, int registryType, Object registryPk) throws Exception {
        Date lastUpdateTs = null;

        try (Statement statement = client.getSession().getDatabase().getConnection().createStatement()) {
            String tableName = SDataConstants.TablesMap.get(registryType);

            if (tableName == null || tableName.isEmpty()) {
                tableName = SModConsts.TablesMap.get(registryType);
            }

            if (tableName != null) {
                String sql = "SHOW COLUMNS FROM " + tableName + " WHERE `Key` = 'PRI';";     //consulta los nombres de todas las pk del registro
                ResultSet resultSet = statement.executeQuery(sql);
                LinkedHashMap<String, String> primaryKeyNamesMap = new LinkedHashMap<>();

                while (resultSet.next()) {
                    primaryKeyNamesMap.put(resultSet.getString("Field"), resultSet.getString("Type"));   //guarda las pk el hash
                }
                resultSet.close();

                String lastUpdateTsFieldName = "";

                for (String tsEditFieldName : TsEditFieldNames) {
                    sql = "SHOW COLUMNS FROM " + tableName + " WHERE Field = '" + tsEditFieldName + "';";
                    resultSet = statement.executeQuery(sql);

                    if (resultSet.next()) {
                        lastUpdateTsFieldName = resultSet.getString("Field");
                        break;
                    }
                    resultSet.close();
                }

                int index = 0;
                sql = "SELECT " + lastUpdateTsFieldName + " FROM " + tableName + " WHERE ";   //consulta la fecha de modificacion del registro

                for (HashMap.Entry<String, String> entry : primaryKeyNamesMap.entrySet()) {
                    sql += (index == 0 ? "" : "AND ") + entry.getKey() + " = ";

                    if (registryPk instanceof int[]) {
                        sql += ((int[]) registryPk)[index++] + " ";
                    } 
                    else if (registryPk instanceof Object[]) {
                        if (entry.getValue().contains("char")) {
                            sql += "'" + ((Object[]) registryPk)[index++] + "' ";
                        } 
                        else if (entry.getValue().contains("date")) {
                            int i = index++;
                            Object obj = ((Object[]) registryPk)[i];
                            if (obj instanceof String) {
                                sql += "'" + obj.toString() + "' ";
                            }
                            else {
                                sql += "'" +  SLibUtils.DbmsDateFormatDate.format(obj) + "' ";
                            }
                        } 
                        else {
                            sql += (((Object[]) registryPk)[index++]) + " ";
                        }
                    } 
                    else {
                        throw new Exception("El tipo de dato del PK del registro es desconocido: " + registryPk.getClass().getName() + ".");
                    }
                }

                resultSet = statement.executeQuery(sql);

                if (resultSet.next()) {
                    lastUpdateTs = resultSet.getTimestamp(lastUpdateTsFieldName);
                } 
                else {
                    throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
                }
            }
        }

        return lastUpdateTs;
    }

    /**
     * Verificar si el candado de acceso exclusivo aún existe mediante su clave
     * Redis, si no, verifica que no exista un candado para el registro, si non
     * existe un candado, consulta la fecha de modificacion de registro para
     * recuperar el candado
     * @param client Cliente GUI.
     * @return
     * @throws java.lang.Exception
     */
    public static SRedisLock verifyLockStatus(erp.client.SClientInterface client, SRedisLock rlock) throws Exception {
        Date lastTs = getLastUpdateTs(client, rlock.getLockKey().getRegistryType(), rlock.getRegistryPk());
/* Bloque de codigo correspondiente a los candados de Redis
        if (lastTs != null) {
            if (!lastTs.after(rlock.getLockTimestamp())) {
                if (SRedisConnectionUtils.getConnectionStatus(client)) {
                    Jedis jedis = client.getJedis();
                    if (!jedis.exists(rlock.getLockKey().getLockKey())) {
                        Set<String> redisKeysSet = jedis.keys(rlock.getLockKey().getLockKeyForSearch());
                        if (redisKeysSet.isEmpty()) {

                            Object registryPk = rlock.getRegistryPk();
                            long timeout = rlock.getTimeout();
                            SRedisLockKey rLockKey = setLock(jedis, rlock.getLockKey().getCompanyId(), rlock.getLockKey().getRegistryType(),
                                    rlock.getLockKey().getLockKey(), rlock.getLockKey().getUserId(), rlock.getTimeout());
                            rlock = new SRedisLock(registryPk, timeout, rLockKey);

                        } 
                        else {
                            SRedisLockKey lockKey = new SRedisLockKey((String) (redisKeysSet.toArray())[0]);
                            throw new Exception("El registro esta siendo utilizado por: " + getUserName(client, lockKey.getUserId()) + ".");
                        }
                    }
                }
            } 
            else {
                throw new Exception("El registro ha sido modificado");
            }
        }
*/        
        return rlock;
    }

    /**
     * Metodo que regresa un vector de todos los candados en Redis
     *
     * @param jedis
     * @return
     */
    public static Vector<String> getLocksList(Jedis jedis) {
        Vector<String> vectorKeys = new Vector<>();
        Set<String> keys = jedis.keys(SRedisLockKey.LOCK + "+*");
        for (int i = 0; i < keys.size(); i++) {
            vectorKeys.add(keys.toArray()[i].toString());
        }
        return vectorKeys;
    }

    public static Vector<String> getLocksListFromUser(Jedis jedis, int id) {
        Vector<String> vectorKeys = new Vector<>();
        Set<String> keys = jedis.keys(SRedisLockKey.LOCK + "+*+" + id);
        for (int i = 0; i < keys.size(); i++) {
            vectorKeys.add(keys.toArray()[i].toString());
        }
        return vectorKeys;
    }
}
