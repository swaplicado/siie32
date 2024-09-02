/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mbps.data;

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
 * @author Isabel Servín
 */
public class SDataScaleBizPartnerMap extends SDataRegistry {

    protected int mnPkScaleId;
    protected int mnPkScaleBizPartnerId;
    protected int mnPkBizPartnerId;
    protected int mnTypeMap;
    protected boolean mbIsDefault;
    protected int mnFkUserId;
    protected java.util.Date mtUserTs;
    
    public SDataScaleBizPartnerMap() {
        super(SDataConstants.BPSU_SCA_BP_MAP);
        reset();
    }
    
    public void setPkScaleId(int n) { mnPkScaleId = n; }
    public void setPkScaleBizPartnerId(int n) { mnPkScaleBizPartnerId = n; }
    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setTypeMap(int n) { mnTypeMap = n; }
    public void setIsDefault(boolean b) { mbIsDefault = b; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setUserTs(java.util.Date t) { mtUserTs = t; }
    
    public int getPkScaleId() { return mnPkScaleId; }
    public int getPkScaleBizPartnerId() { return mnPkScaleBizPartnerId; }
    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getTypeMap() { return mnTypeMap; }
    public boolean getIsDefault() { return mbIsDefault; }
    public int getFkUserId() { return mnFkUserId; }
    public java.util.Date getUserTs() { return mtUserTs; }
    
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkScaleId = ((int[]) pk)[0];
        mnPkScaleBizPartnerId = ((int[]) pk)[1];
        mnPkBizPartnerId = ((int[]) pk)[2];
    }

    @Override
    public Object getPrimaryKey() {
        return new Object[] { mnPkScaleId, mnPkScaleBizPartnerId, mnPkBizPartnerId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkScaleId = 0;
        mnPkScaleBizPartnerId = 0;
        mnPkBizPartnerId = 0;
        mnTypeMap = 0;
        mbIsDefault = false;
        mnFkUserId = 0;
        mtUserTs = null;
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.bpsu_sca_bp_map "
                    + "WHERE id_sca = " + key[0] + " "
                    + "AND id_sca_bp = " + key[1] + " "
                    + "AND id_bp = " + key[2];
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkScaleId = resultSet.getInt("id_sca");
                mnPkScaleBizPartnerId = resultSet.getInt("id_sca_bp");
                mnPkBizPartnerId = resultSet.getInt("id_bp");
                mnTypeMap = resultSet.getInt("tp_map");
                mbIsDefault = resultSet.getBoolean("b_def");
                mnFkUserId = resultSet.getInt("fid_usr");
                mtUserTs = resultSet.getTimestamp("ts");

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
        String sql;
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbIsRegistryNew) {
                sql = "INSERT INTO erp.bpsu_sca_bp_map VALUES(" +
                        mnPkScaleId + ", " +
                        mnPkScaleBizPartnerId + ", " +
                        mnPkBizPartnerId + ", " +
                        mnTypeMap + ", " +
                        mbIsDefault + ", " +
                        mnFkUserId + ", " +
                        "NOW() " +
                        ")";
            }
            else {
                sql = "UPDATE erp.bpsu_sca_bp_map SET " +
                        "b_def = " + mbIsDefault + ", " +
                        "fid_usr = " + mnFkUserId + ", " +
                        "ts = NOW() " +
                        "WHERE id_sca = " + mnPkScaleId + " " +
                        "AND id_sca_bp = " + mnPkScaleBizPartnerId + " " +
                        "AND id_bp = " + mnPkBizPartnerId;
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
        return null;
    }
}
