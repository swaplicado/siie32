/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Isabel Servín
 */
public class SDataScaleTicketDpsEntry extends SDataRegistry{

    protected int mnPkScaleTicketId;
    protected int mnPkDpsYearId;
    protected int mnPkDpsDocId;
    protected int mnPkDpsEntryId;
    
    public SDataScaleTicketDpsEntry() {
        super(SDataConstants.TRN_SCA_TIC_DPS_ETY);
        reset();
    }
    
    public void setPkScaleTicketId(int n) { mnPkScaleTicketId = n; }
    public void setPkYearId(int n) { mnPkDpsYearId = n; }
    public void setPkDocId(int n) { mnPkDpsDocId = n; }
    public void setPkEntryId(int n) { mnPkDpsEntryId = n; }

    public int getPkScaleTicketId() { return mnPkScaleTicketId; }
    public int getPkYearId() { return mnPkDpsYearId; }
    public int getPkDocId() { return mnPkDpsDocId; }
    public int getPkEntryId() { return mnPkDpsEntryId; }

    @Override
    public void setPrimaryKey(Object pk) {
        mnPkScaleTicketId = ((int[]) pk)[0];
        mnPkDpsYearId = ((int[]) pk)[1];
        mnPkDpsDocId = ((int[]) pk)[2];
        mnPkDpsEntryId = ((int[]) pk)[3];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkScaleTicketId, mnPkDpsYearId, mnPkDpsDocId, mnPkDpsEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkScaleTicketId = 0;
        mnPkDpsYearId = 0;
        mnPkDpsDocId = 0;
        mnPkDpsEntryId = 0;
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_sca_tic_dps_ety "
                    + "WHERE id_sca_tic = " + key[0] + " AND id_dps_year = " + key[1] + " AND id_dps_doc = " + key[2] + " AND id_dps_ety = " + key[3];
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkScaleTicketId = resultSet.getInt("id_sca_tic");
                mnPkDpsYearId = resultSet.getInt("id_dps_year");
                mnPkDpsDocId = resultSet.getInt("id_dps_doc");
                mnPkDpsEntryId = resultSet.getInt("id_dps_ety");
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public int save(Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            String msSql = "INSERT INTO trn_sca_tic_dps_ety VALUES (" +
                    mnPkScaleTicketId + ", " + 
                    mnPkDpsYearId + ", " + 
                    mnPkDpsDocId + ", " + 
                    mnPkDpsEntryId + " " + 
                    ")";
            connection.createStatement().execute(msSql);
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}
