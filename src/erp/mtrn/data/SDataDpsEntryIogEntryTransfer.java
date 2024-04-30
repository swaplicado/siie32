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
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Isabel Servin
 */
public class SDataDpsEntryIogEntryTransfer extends SDataRegistry {
    
    protected int mnPkDpsSourceYearId;
    protected int mnPkDpsSourceDocId;
    protected int mnPkDpsSourceEntryId;
    protected int mnPkDpsDestinyYearId;
    protected int mnPkDpsDestinyDocId;
    protected int mnPkDpsDestinyEntryId;
    protected int mnPkDiogYearId;
    protected int mnPkDiogDocId;
    protected int mnPkDiogEntryId;

    public SDataDpsEntryIogEntryTransfer() {
        super(SDataConstants.TRN_DPS_ETY_IOG_ETY_XFR);
        reset();
    }
    
    public void setPkDpsSourceYearId(int n) { mnPkDpsSourceYearId = n; }
    public void setPkDpsSourceDocId(int n) { mnPkDpsSourceDocId = n; }
    public void setPkDpsSourceEntryId(int n) { mnPkDpsSourceEntryId = n; }
    public void setPkDpsDestinyYearId(int n) { mnPkDpsDestinyYearId = n; }
    public void setPkDpsDestinyDocId(int n) { mnPkDpsDestinyDocId = n; }
    public void setPkDpsDestinyEntryId(int n) { mnPkDpsDestinyEntryId = n; }
    public void setPkDiogYearId(int n) { mnPkDiogYearId = n; }
    public void setPkDiogDocId(int n) { mnPkDiogDocId = n; }
    public void setPkDiogEntryId(int n) { mnPkDiogEntryId = n; }


    public int getPkDpsSourceYearId() { return mnPkDpsSourceYearId; }
    public int getPkDpsSourceDocId() { return mnPkDpsSourceDocId; }
    public int getPkDpsSourceEntryId() { return mnPkDpsSourceEntryId; }
    public int getPkDpsDestinyYearId() { return mnPkDpsDestinyYearId; }
    public int getPkDpsDestinyDocId() { return mnPkDpsDestinyDocId; }
    public int getPkDpsDestinyEntryId() { return mnPkDpsDestinyEntryId; }
    public int getPkDiogYearId() { return mnPkDiogYearId; }
    public int getPkDiogDocId() { return mnPkDiogDocId; }
    public int getPkDiogEntryId() { return mnPkDiogEntryId; }
    
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkDpsSourceYearId = ((int[]) pk)[0];
        mnPkDpsSourceDocId = ((int[]) pk)[1];
        mnPkDpsSourceEntryId = ((int[]) pk)[2];
        mnPkDpsDestinyYearId = ((int[]) pk)[3];
        mnPkDpsDestinyDocId = ((int[]) pk)[4];
        mnPkDpsDestinyEntryId = ((int[]) pk)[5];
        mnPkDiogYearId = ((int[]) pk)[6];
        mnPkDiogDocId = ((int[]) pk)[7];
        mnPkDiogEntryId = ((int[]) pk)[8];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkDpsSourceYearId, mnPkDpsSourceDocId, mnPkDpsSourceEntryId, 
            mnPkDpsDestinyYearId, mnPkDpsDestinyDocId, mnPkDpsDestinyEntryId, 
            mnPkDiogYearId, mnPkDiogDocId, mnPkDiogEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkDpsSourceYearId = 0;
        mnPkDpsSourceDocId = 0;
        mnPkDpsSourceEntryId = 0;
        mnPkDpsDestinyYearId = 0;
        mnPkDpsDestinyDocId = 0;
        mnPkDpsDestinyEntryId = 0;
        mnPkDiogYearId = 0;
        mnPkDiogDocId = 0;
        mnPkDiogEntryId = 0;
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT * FROM trn_dps_ety_iog_ety_xfr " + 
                    "WHERE id_src_year = " + mnPkDpsSourceYearId + " AND id_src_doc = " + mnPkDpsSourceDocId + " AND id_src_ety = " + mnPkDpsSourceEntryId + " " + 
                    "AND id_des_year = " + mnPkDpsDestinyYearId + " AND id_des_doc = " + mnPkDpsDestinyDocId + " AND id_des_ety = " + mnPkDpsDestinyEntryId + " " + 
                    "AND id_diog_year = " + mnPkDiogYearId + " AND id_diog_doc = " + mnPkDiogDocId + " AND id_diog_ety = " + mnPkDiogEntryId + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkDpsSourceYearId = resultSet.getInt("id_src_year");
                mnPkDpsSourceDocId = resultSet.getInt("id_src_doc");
                mnPkDpsSourceEntryId = resultSet.getInt("id_src_ety");
                mnPkDpsDestinyYearId = resultSet.getInt("id_des_year");
                mnPkDpsDestinyDocId = resultSet.getInt("id_des_doc");
                mnPkDpsDestinyEntryId = resultSet.getInt("id_des_ety");
                mnPkDiogYearId = resultSet.getInt("id_diog_year");
                mnPkDiogDocId = resultSet.getInt("id_diog_doc");
                mnPkDiogEntryId = resultSet.getInt("id_diog_ety");
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
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
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            sql = "INSERT INTO trn_dps_ety_iog_ety_xfr VALUES (" +
                    mnPkDpsSourceYearId + ", " +
                    mnPkDpsSourceDocId + ", " +
                    mnPkDpsSourceEntryId + ", " +
                    mnPkDpsDestinyYearId + ", " +
                    mnPkDpsDestinyDocId + ", " +
                    mnPkDpsDestinyEntryId + ", " +
                    mnPkDiogYearId + ", " +
                    mnPkDiogDocId + ", " +
                    mnPkDiogEntryId + " " +
                    ");";
            
            connection.createStatement().execute(sql);
            
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return new Date();
    }
}
