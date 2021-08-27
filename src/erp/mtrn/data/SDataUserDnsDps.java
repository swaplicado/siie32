/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import sa.lib.SLibConsts;

/**
 * 
 * @author Isabel Servín
 */
public class SDataUserDnsDps extends erp.lib.data.SDataRegistry {

    protected int mnPkUserId;
    protected int mnPkDocNumberSeriesId;
    
    protected erp.mtrn.data.SDataDpsDocumentNumberSeries moDbmsDocumentNumberSeries;
    
    public SDataUserDnsDps() {
        super(SDataConstants.TRN_USR_DPS_DNS);
    }
    
    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkDocNumberSeriesId(int n) { mnPkDocNumberSeriesId = n; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkDocNumberSeriesId() { return mnPkDocNumberSeriesId; }
    
    public SDataDpsDocumentNumberSeries getDocumentNumberSeries() { return moDbmsDocumentNumberSeries; }

    
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkUserId = ((int[]) pk)[0];
        mnPkDocNumberSeriesId = ((int[]) pk)[1];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkUserId, mnPkDocNumberSeriesId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkUserId = 0;
        mnPkDocNumberSeriesId = 0;
        
        moDbmsDocumentNumberSeries = null;
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] anKey = (int[]) pk;
        String sSql;
        ResultSet resultSet;
        
        mnLastDbActionResult = SLibConsts.UNDEFINED;
        reset();

        try {
            sSql = "SELECT * FROM trn_usr_dns_dps WHERE id_usr = " + anKey[0] + " AND id_dns = " + anKey[1] + ";";
            resultSet = statement.executeQuery(sSql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUserId = resultSet.getInt("id_usr");
                mnPkDocNumberSeriesId = resultSet.getInt("id_dns");
                
                SDataDpsDocumentNumberSeries series = new SDataDpsDocumentNumberSeries();
                if (series.read(new int[] { mnPkDocNumberSeriesId }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    moDbmsDocumentNumberSeries = series;
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (Exception e) {
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
    public int save(Connection connection) {
        String sSql;
        Statement statement;
        
        mnLastDbActionResult = SLibConsts.UNDEFINED;
        
        try {
            statement = connection.createStatement();
            sSql = "INSERT INTO trn_usr_dns_dps (id_usr, id_dns) VALUES (" + mnPkUserId + ", " + mnPkDocNumberSeriesId + ");";
            statement.execute(sSql);
            
            mnDbmsErrorId = 0;
            msDbmsError = "";

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (Exception e) {
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
    public Date getLastDbUpdate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
    
