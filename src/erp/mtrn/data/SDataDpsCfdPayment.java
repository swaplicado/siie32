/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.SClientUtils;
import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import sa.lib.SLibConsts;

/**
 *
 * @author Isabel Servín
 */
public final class SDataDpsCfdPayment extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkCfdPayId;
    protected int mnFkUserId;
    protected java.util.Date mtUserTs;
    
    protected String msAuxCfd;
    protected String msAuxCfdName;
    protected int mnAuxBizPartner;
    protected boolean mbAuxToBeDeleted;

    public SDataDpsCfdPayment() {
        super(SDataConstants.TRN_DPS_CFD_PAY);
        reset();
    }
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkCfdPayId(int n) { mnPkCfdPayId = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setUserTs(java.util.Date t) { mtUserTs = t; }
    
    public void setAuxCfd(String s) { msAuxCfd = s; }
    public void setAuxCfdName(String s) { msAuxCfdName = s; }
    public void setAuxBizPartner(int n) { mnAuxBizPartner = n; }
    public void setAuxToBeDeleted(boolean b) { mbAuxToBeDeleted = b; }
    
    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkCfdPayId() { return mnPkCfdPayId; }
    public int getFkUserId() { return mnFkUserId; }
    public java.util.Date getUserTs() { return mtUserTs; }
    
    public String getAuxCfd() { return msAuxCfd; }
    public String getAuxCfdName() { return msAuxCfdName; }
    public int getAuxBizParten() { return mnAuxBizPartner; }
    public boolean getAuxToBeDeleted() { return mbAuxToBeDeleted; }
    
    /*
     * Implementation of erp.lib.data.SDataRegistry
     */
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkCfdPayId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkCfdPayId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkCfdPayId = 0;
        mnFkUserId = 0;
        mtUserTs = null;
        
        msAuxCfd = "";
        msAuxCfdName = "";
        mnAuxBizPartner = 0;
        mbAuxToBeDeleted = false;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_dps_cfd_pay AS p " +
                    "LEFT JOIN " + SClientUtils.getComplementaryDbName(statement.getConnection()) + ".trn_cfd_pay AS c ON " +
                    "p.id_cfd_pay = c.id_cfd_pay " +
                    "WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " AND p.id_cfd_pay = " + key[2];
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                mnPkCfdPayId = resultSet.getInt("p.id_cfd_pay");
                mnFkUserId = resultSet.getInt("fid_usr");
                mtUserTs = resultSet.getTimestamp("ts_usr");
                
                msAuxCfd = resultSet.getString("doc_xml");
                msAuxCfdName = resultSet.getString("doc_xml_name");
                mnAuxBizPartner = resultSet.getInt("bp");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_READ;
            }
            msDbmsError += "\n" + e.toString();
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        
        String sql;
        try {
        
            mnLastDbActionResult = SLibConstants.UNDEFINED;
        
            if (!mbAuxToBeDeleted) {
                setPk(connection);
                
                sql = "INSERT INTO trn_dps_cfd_pay "
                        + "VALUES (" + mnPkYearId + ", " + mnPkDocId + ", " + mnPkCfdPayId + ", " + mnFkUserId + ", NOW())";                
                connection.createStatement().execute(sql);

                sql = "INSERT INTO " + SClientUtils.getComplementaryDbName(connection) + ".trn_cfd_pay VALUES (" + mnPkCfdPayId + ", '" + msAuxCfd + "', '" + msAuxCfdName +"', " + mnAuxBizPartner + ")";
                connection.createStatement().execute(sql);
            }
            else {
                sql = "DELETE FROM trn_dps_cfd_pay WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_cfd_pay = " + mnPkCfdPayId; 
                connection.createStatement().execute(sql);
                
                sql = "DELETE FROM " + SClientUtils.getComplementaryDbName(connection) + ".trn_cfd_pay "
                        + "WHERE id_cfd_pay = " + mnPkCfdPayId;
                connection.createStatement().execute(sql);
                
            }
            
            mnDbmsErrorId = 0;
            msDbmsError = "";

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_SAVE;
            }
            msDbmsError += "\n" + e.toString();
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }
    
    @Override
    public java.util.Date getLastDbUpdate() {
        return null;
    }
    
    @Override
    public int canSave(java.sql.Connection connection) {
        return mnLastDbActionResult;
    }

    @Override
    public int canAnnul(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConsts.UNDEFINED;
        return mnLastDbActionResult;
    }
    
    @Override
    public int annul(java.sql.Connection connection) {
        return mnLastDbActionResult;
    }
    
    private void setPk(java.sql.Connection connection) throws Exception {
        String sql = "SELECT COALESCE(MAX(id_cfd_pay), 0) + 1 FROM " + SClientUtils.getComplementaryDbName(connection) + ".trn_cfd_pay";
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            mnPkCfdPayId = resultSet.getInt(1);
        }
    }
}
