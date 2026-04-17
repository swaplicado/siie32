/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mloc.data;

import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public abstract class SLocUtils {
    
    /**
     * Check if given country has states.
     * @param statement DB statement.
     * @param idCountry Country ID.
     * @return <code>true</code> if country has states, otherwise <code>false</code>.
     * @throws java.sql.SQLException
     */
    public static boolean hasStates(final Statement statement, final int idCountry) throws SQLException {
        boolean hasStates = false;
        
        String sql = "SELECT COUNT(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOCU_STA) + " "
                + "WHERE fid_cty = " + idCountry + " AND NOT b_del;";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                hasStates = true;
            }
        }
        
        return hasStates;
    }
    
    /**
     * Update deprecated state codes with their current version.
     * @param code State code to update.
     * @return 
     */
    public static String updateStateCode(final String code) {
        String updatedCode;
        
        switch (code) {
            case SDataState.CDMX_OBS:
                updatedCode = SDataState.CDMX_NEW;
                break;
            default:
                updatedCode = code;
        }
        
        return updatedCode;
    }
    
    /**
     * Read state by code.
     * @param statement DB statement.
     * @param code Required state code.
     * @return The older found state is returned, otherwise <code>null</code>.
     * @throws java.sql.SQLException
     */
    public static SDataState readStateByCode(final Statement statement, final String code) throws SQLException {
        SDataState state = null;
        String sql = "SELECT id_sta FROM erp.locu_sta WHERE sta_code = '" + updateStateCode(code) + "' ORDER BY id_sta LIMIT 1;";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                state = new SDataState();
                if (state.read(new int[] { resultSet.getInt("id_sta") }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                    state = null;
                }
            }
        }
        
        return state;
    }
    
    /**
     * Read country by code.
     * @param statement DB statement.
     * @param code Required country code.
     * @return The older found country is returned, otherwise <code>null</code>.
     * @throws java.sql.SQLException
     */
    public static SDataCountry readCountryByCode(final Statement statement, final String code) throws SQLException {
        SDataCountry country = null;
        String sql = "SELECT id_cty FROM erp.locu_cty WHERE cty_code = '" + code + "' ORDER BY id_cty LIMIT 1;";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                country = new SDataCountry();
                if (country.read(new int[] { resultSet.getInt("id_cty") }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                    country = null;
                }
            }
        }
        
        return country;
    }
}
