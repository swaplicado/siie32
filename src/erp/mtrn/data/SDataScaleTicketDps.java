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
public class SDataScaleTicketDps extends SDataRegistry {

    protected int mnPkScaleTicketId;
    protected int mnPkDpsYearId;
    protected int mnPkDpsDocId;
    
    public SDataScaleTicketDps() {
        super(SDataConstants.TRN_SCA_TIC_DPS);
        reset();
    }
    
    public void setPkScaleTicketId(int n) { mnPkScaleTicketId = n; }
    public void setPkDpsYearId(int n) { mnPkDpsYearId = n; }
    public void setPkDpsDocId(int n) { mnPkDpsDocId = n; }

    public int getPkScaleTicketId() { return mnPkScaleTicketId; }
    public int getPkDpsYearId() { return mnPkDpsYearId; }
    public int getPkDpsDocId() { return mnPkDpsDocId; }

    @Override
    public void setPrimaryKey(Object pk) {
        mnPkScaleTicketId = ((int[]) pk)[0];
        mnPkDpsYearId = ((int[]) pk)[1];
        mnPkDpsDocId = ((int[]) pk)[2];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkScaleTicketId, mnPkDpsYearId, mnPkDpsDocId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkScaleTicketId = 0;
        mnPkDpsYearId = 0;
        mnPkDpsDocId = 0;
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_sca_tic_dps WHERE id_sca_tic = " + key[0] + " AND id_dps_year = " + key[1] + " AND id_dps_doc = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkScaleTicketId = resultSet.getInt("id_sca_tic");
                mnPkDpsYearId = resultSet.getInt("id_dps_year");
                mnPkDpsDocId = resultSet.getInt("id_dps_doc");
                
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
            String msSql = "INSERT INTO trn_sca_tic_dps VALUES(" +
                    mnPkScaleTicketId + ", " + 
                    mnPkDpsYearId + ", " + 
                    mnPkDpsDocId + " " + 
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
