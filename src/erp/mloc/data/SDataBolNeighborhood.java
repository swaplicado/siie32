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
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Isabel Servín
 */
public class SDataBolNeighborhood extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkNeighborhoodZipCode;
    protected String msNeighborhoodCode;
    protected String msZipCode;
    protected String msDescription;
    protected boolean mbDeleted;
    protected int mnFkUserId;
    protected Date mtTsUser;
    
    public SDataBolNeighborhood() {
        super(SDataConstants.LOCS_BOL_NEI_ZIP_CODE);
        reset();
    }
    
    public void setPkNeighborhoodZipCode(int n) { mnPkNeighborhoodZipCode = n; }
    public void setNeighborhoodCode(String s) { msNeighborhoodCode = s; }
    public void setZipCode(String s) { msZipCode = s; }
    public void setDescription(String s) { msDescription = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }

    public int getPkNeighborhoodZipCode() { return mnPkNeighborhoodZipCode; }
    public String getNeighborhoodCode() { return msNeighborhoodCode; }
    public String getZipCode() { return msZipCode; }
    public String getDescription() { return msDescription; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }

    @Override
    public void setPrimaryKey(Object pk) {
        mnPkNeighborhoodZipCode = ((int[]) pk)[0];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkNeighborhoodZipCode };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkNeighborhoodZipCode = 0;
        msNeighborhoodCode = "";
        msZipCode = "";
        msDescription = "";
        mbDeleted = false;
        mnFkUserId = 0;
        mtTsUser = null;
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT * FROM erp.locs_bol_nei_zip_code WHERE id_nei_zip_code = " + key[0];
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkNeighborhoodZipCode = resultSet.getInt("id_nei_zip_code");
                msNeighborhoodCode = resultSet.getString("nei_code");
                msZipCode = resultSet.getString("zip_code");
                msDescription = resultSet.getString("description");
                mbDeleted = resultSet.getBoolean("b_del");
                mnFkUserId = resultSet.getInt("fid_usr");
                mtTsUser = resultSet.getTimestamp("ts_usr");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        return mnLastDbActionResult;
    }

    @Override
    public int save(Connection connection) {
        String sql;
        ResultSet resultSet;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            if(mbIsRegistryNew) {
                sql = "SELECT COUNT(*) from erp.locs_bol_nei_zip_code WHERE id_nei_zip_code = " + mnPkNeighborhoodZipCode;
                resultSet = connection.createStatement().executeQuery(sql);
                
                if (resultSet.next()) {
                    mbIsRegistryNew = resultSet.getInt(1) == 0;
                }
            }
            
            if (mbIsRegistryNew) {
                sql = "INSERT INTO erp.locs_bol_nei_zip_code VALUES(" +
                        mnPkNeighborhoodZipCode + ", " + 
                        "'" + msNeighborhoodCode + "', " + 
                        "'" + msZipCode + "', " + 
                        "'" + msDescription + "', " + 
                        (mbDeleted ? 1 : 0) + ", " + 
                        mnFkUserId + ", " + 
                        "NOW()" + " " + 
                        ")";
            }
            else {
                sql = "UPDATE erp.locs_bol_nei_zip_code SET " +
//                       "id_nei_zip_code = " + mnPkNeighborhoodZipCode + ", " +
                        "nei_code = '" + msNeighborhoodCode + "', " +
                        "zip_code = '" + msZipCode + "', " +
                        "description = '" + msDescription + "', " +
                        "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                        "fid_usr = " + mnFkUserId + ", " +
//                        "ts_usr = " + "NOW()" + " " +
                        "WHERE id_nei_zip_code = " + mnPkNeighborhoodZipCode;
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
