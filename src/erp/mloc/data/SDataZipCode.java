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
public class SDataZipCode extends erp.lib.data.SDataRegistry implements java.io.Serializable{

    protected java.lang.String msPkZipCode;
    protected java.lang.String msPkStateCode;
    protected java.lang.String msFkCountyCode;
    protected java.lang.String msFkLocalityCode;
    
    protected SDataCounty moDbmsCounty;
    protected SDataLocality moDbmsLocality;

    public SDataZipCode() {
        super(SDataConstants.LOCS_BOL_ZIP_CODE);
        reset();
    }
    
    public void setPkZipCode(java.lang.String s) { msPkZipCode = s; }
    public void setPkStateCode(java.lang.String s) { msPkStateCode = s; }
    public void setFkCountyCode(java.lang.String s) { msFkCountyCode = s; }
    public void setFkLocalityCode(java.lang.String s) { msFkLocalityCode = s; }

    public java.lang.String getPkZipCode() { return msPkZipCode; }
    public java.lang.String getPkStateCode() { return msPkStateCode; }
    public java.lang.String getFkCountyCode() { return msFkCountyCode; }
    public java.lang.String getFkLocalityCode() { return msFkLocalityCode; }

    public void setDbmsCounty(SDataCounty o) { moDbmsCounty = o; }
    public void setDbmsLocality(SDataLocality o) { moDbmsLocality = o; }
    
    public SDataCounty getDbmsCounty() { return moDbmsCounty; }
    public SDataLocality getDbmsLocality() { return moDbmsLocality; }

    @Override
    public void setPrimaryKey(Object pk) {
        msPkZipCode = ((String[]) pk)[0];
        msPkStateCode = ((String[]) pk)[1];
    }

    @Override
    public Object getPrimaryKey() {
        return new String[] { msPkZipCode, msPkStateCode };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        msPkZipCode = "";
        msPkStateCode = "";
        msFkCountyCode = "";
        msFkLocalityCode = "";
        
        moDbmsCounty = null;
        moDbmsLocality = null;
    }

    @Override
    public int read(Object pk, Statement statement) {
        String[] key = (String[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.locs_bol_zip_code WHERE id_zip_code = '" + key[0] + "' AND id_sta_code = '" + key[1] + "'";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                msPkZipCode = resultSet.getString("id_zip_code");
                msPkStateCode = resultSet.getString("id_sta_code");
                msFkCountyCode = resultSet.getString("county_code");
                msFkLocalityCode = resultSet.getString("locality_code");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
            
            // Read county:
            
            if (!msFkCountyCode.isEmpty()) {
                moDbmsCounty = new SDataCounty();
                moDbmsCounty.read(new String[] { msFkCountyCode, msPkStateCode }, statement);
            }
            
            // Read locality
            
            if (!msFkLocalityCode.isEmpty()) {
                moDbmsLocality = new SDataLocality();
                moDbmsLocality.read(new String[] { msFkLocalityCode, msPkStateCode }, statement);
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
                sql = "SELECT COUNT(*) FROM erp.locs_bol_zip_code WHERE id_zip_code = " + msPkZipCode + " AND id_sta_code = " + msPkStateCode;
                resultSet = connection.createStatement().executeQuery(sql);

                if (resultSet.next()) {
                    mbIsRegistryNew = resultSet.getInt(1) == 0;
                }
            }

            if (mbIsRegistryNew) {
                
                sql = "INSERT INTO erp.locs_bol_zip_code VALUES (" +
                        "'" + msPkZipCode + "', " +
                        "'" + msPkStateCode + "', " +
                        "'" + msFkCountyCode + "', " +
                        "'" + msFkLocalityCode + "' " +
                        ")";
            }
            else {

                sql = "UPDATE erp.locs_bol_zip_code SET " +
//                        "id_zip_code='" + msPkZipCode + "', " +
//                        "id_sta_code='" + msPkStateCode + "', " +
                        "county_code='" + msFkCountyCode + "', " +
                        "locality_code='" + msFkLocalityCode + "' " +
                        "WHERE id_zip_code = " + msPkZipCode + " AND id_sta_code = " + msPkStateCode;
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
