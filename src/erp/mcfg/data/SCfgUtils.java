package erp.mcfg.data;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Sergio Flores
 */
public abstract class SCfgUtils {
    
    public static String getParamValue(final Statement statement, final String paramKey) throws Exception {
        String paramValue = "";
        String sql = "SELECT param_value FROM cfg_param WHERE param_key = '" + paramKey + "';";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                paramValue = resultSet.getString(1);
            }
        }
        
        return paramValue;
    }
}
