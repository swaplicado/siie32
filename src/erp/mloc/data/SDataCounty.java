/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mloc.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Isabel Servín
 */
public class SDataCounty extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected java.lang.String msPkCountyCode;
    protected java.lang.String msPkStateCode;
    protected java.lang.String msDescription;

    public SDataCounty() {
        super(SDataConstants.LOCS_CCP_COUNTY);
        reset();
    }
    
    public void setPkCountyCode(java.lang.String s) { msPkCountyCode = s; }
    public void setPkStateCode(java.lang.String s) { msPkStateCode = s; }
    public void setDescription(java.lang.String s) { msDescription = s; }

    public java.lang.String getPkCountyCode() { return msPkCountyCode; }
    public java.lang.String getPkStateCode() { return msPkStateCode; }
    public java.lang.String getDescription() { return msDescription; }
    
    @Override
    public void setPrimaryKey(Object pk) {
        msPkCountyCode = ((String[]) pk)[0];
        msPkStateCode = ((String[]) pk)[1];
    }

    @Override
    public Object getPrimaryKey() {
        return new String[] { msPkCountyCode, msPkStateCode };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        msPkCountyCode = "";
        msPkStateCode = "";
        msDescription = "";
    }

    @Override
    public int read(Object pk, Statement statement) {
        String[] key = (String[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.locs_ccp_county WHERE id_county_code = '" + key[0] + "' AND id_sta_code = '" + key[1] +"' ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                msPkCountyCode = resultSet.getString("id_county_code");
                msPkStateCode = resultSet.getString("id_sta_code");
                msDescription = resultSet.getString("description");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (Exception e){
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public int save(Connection connection) {
        String sql = "";
        ResultSet resultSet = null;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbIsRegistryNew) {
                sql = "SELECT COUNT(*) FROM erp.locs_ccp_county WHERE id_county_code = " + msPkCountyCode + " AND id_sta_code = " + msPkStateCode;
                resultSet = connection.createStatement().executeQuery(sql);

                if (resultSet.next()) {
                    mbIsRegistryNew = resultSet.getInt(1) == 0;
                }
            }

            if (mbIsRegistryNew) {
                
                sql = "INSERT INTO erp.locs_ccp_county VALUES (" +
                        "'" + msPkCountyCode + "', " +
                        "'" + msPkStateCode + "', " +
                        "'" + msDescription + "', " +
                        ")";
            }
            else {

                sql = "UPDATE erp.locs_ccp_county SET " +
//                        "id_county_code='" + msPkCountyCode + "', " +
//                        "id_sta_code='" + msPkStateCode + "', " +
                        "description='" + msDescription + "', " +
                        "WHERE id_county_code = " + msPkCountyCode + " AND id_sta_code = " + msPkStateCode;
            }

            connection.createStatement().execute(sql);
            
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
