package erp.lib.data;

import java.sql.Statement;

/**
 * Interfaz para la lectura de registros "delgados".
 * @author Sergio Flores
 */
public interface SThinData {
    
    public void reset();
    public void read(Object primaryKey, Statement statement) throws Exception;
    public Object getPrimaryKey();
}
