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
public class SDataBolLocality extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected String msPkLocalityCode;
    protected String msPkStateCode;
    protected String msDescription;
    protected boolean mbDeleted;
    protected int mnFkUserId;
    protected Date mtTsUser;

    public SDataBolLocality() {
        super(SDataConstants.LOCS_BOL_LOCALITY);
    }
    
    public void setPkLocalityCode(String s) { msPkLocalityCode = s; }
    public void setPkStateCode(String s) { msPkStateCode = s; }
    public void setDescription(String s) { msDescription = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }

   public String getPkLocalityCode() { return msPkLocalityCode; }
    public String getPkStateCode() { return msPkStateCode; }
    public String getDescription() { return msDescription; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }

    
    @Override
    public void setPrimaryKey(Object pk) {
        msPkLocalityCode = ((String[]) pk)[0];
        msPkStateCode = ((String[]) pk)[1];
    }

    @Override
    public Object getPrimaryKey() {
        return new String[] { msPkLocalityCode, msPkStateCode };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        msPkLocalityCode = "";
        msPkStateCode = "";
        msDescription = "";
        mbDeleted = false;
        mnFkUserId = 0;
        mtTsUser = null;
    }

    @Override
    public int read(Object pk, Statement statement) {
        String[] key = (String[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.locs_bol_locality WHERE id_locality_code = '" + key[0] + "' AND id_sta_code = '" + key[1] + "'";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                msPkLocalityCode = resultSet.getString("id_locality_code");
                msPkStateCode = resultSet.getString("id_sta_code");
                msDescription = resultSet.getString("description");
                mbDeleted = resultSet.getBoolean("b_del");
                mnFkUserId = resultSet.getInt("fid_usr");
                mtTsUser = resultSet.getTimestamp("ts_usr");

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
                sql = "SELECT COUNT(*) FROM erp.locs_bol_locality WHERE id_locality_code = " + msPkLocalityCode + " AND id_sta_code = " + msPkStateCode;
                resultSet = connection.createStatement().executeQuery(sql);

                if (resultSet.next()) {
                    mbIsRegistryNew = resultSet.getInt(1) == 0;
                }
            }

            if (mbIsRegistryNew) {
                
                sql = "INSERT INTO erp.locs_bol_locality VALUES (" +
                        "'" + msPkLocalityCode + "', " + 
                        "'" + msPkStateCode + "', " + 
                        "'" + msDescription + "', " + 
                        (mbDeleted ? 1 : 0) + ", " + 
                        mnFkUserId + ", " + 
                        "NOW()" + " " + 
                        ")";
            }
            else {

                sql = "UPDATE erp.locs_bol_locality SET " +
//                        "id_locality_code='" + msPkLocalityCode + "', " +
//                        "id_sta_code='" + msPkStateCode + "', " +
                        "description = '" + msDescription + "', " +
                        "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                        "fid_usr = " + mnFkUserId + ", " +
//                        "ts_usr = " + "NOW()" + " " +
                        "WHERE id_locality_code = " + msPkLocalityCode + " AND id_sta_code = " + msPkStateCode;
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
