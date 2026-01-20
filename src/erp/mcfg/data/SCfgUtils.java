package erp.mcfg.data;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Sergio Flores
 */
public abstract class SCfgUtils {
    
    /**
     * Get value of requested configuration parameter.
     * @param statement Database statement.
     * @param paramKey Requested configuration parameter. Constants defined in class erp.data.SDataConstantsSys (SDataConstantsSys.CFG_PARAM_...)
     * @return
     * @throws Exception 
     */
    public static String getParamValue(final Statement statement, final String paramKey) throws Exception {
        return getParamValue(statement, paramKey, "");
    }
    
    /**
     * Get value of requested configuration parameter.
     * @param statement Database statement.
     * @param paramKey Requested configuration parameter. Constants defined in class erp.data.SDataConstantsSys (SDataConstantsSys.CFG_PARAM_...)
     * @param database Database name. Can be <code>null</code> or empty.
     * @return
     * @throws Exception 
     */
    public static String getParamValue(final Statement statement, final String paramKey, final String database) throws Exception {
        String paramValue = "";
        String sql = "SELECT param_value "
                + "FROM " + (database == null || database.isEmpty() ? "" : database + ".") + "cfg_param "
                + "WHERE param_key = '" + paramKey + "';";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                paramValue = resultSet.getString(1);
            }
        }
        
        return paramValue;
    }
}
