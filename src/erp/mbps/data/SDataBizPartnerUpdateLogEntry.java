/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;

/**
 * @author Sergio Flores
 */
public class SDataBizPartnerUpdateLogEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    protected int mnPkBizPartnerId;
    protected int mnPkLogId;
    protected String msBizPartnerCommercial;
    protected String msEmail01;
    protected String msTaxRegime;
    protected int mnLeadTime;
    protected int mnFkUserUpdate;
    protected Date mtUserUpdateTs;
    
    protected String msOldBizPartnerCommercial;
    protected String msOldEmail01;
    protected String msOldTaxRegime;
    protected int mnOldLeadTime;
    
    protected int mnAuxBizPartnerCategoryId;

    public SDataBizPartnerUpdateLogEntry() {
        super(SDataConstants.BPSU_BP_UPD_LOG);
        reset();
    }

    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkLogId(int n) { mnPkLogId = n; }
    public void setBizPartnerCommercial(String s) { msBizPartnerCommercial = s; }
    public void setEmail01(String s) { msEmail01 = s; }
    public void setTaxRegime(String s) { msTaxRegime = s; }
    public void setLeadTime(int n) { mnLeadTime = n; }
    public void setFkUserUpdate(int n) { mnFkUserUpdate = n; }
    public void setUserUpdateTs(Date t) { mtUserUpdateTs = t; }

    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkLogId() { return mnPkLogId; }
    public String getBizPartnerCommercial() { return msBizPartnerCommercial; }
    public String getEmail01() { return msEmail01; }
    public String getTaxRegime() { return msTaxRegime; }
    public int getLeadTime() { return mnLeadTime; }
    public int getFkUserUpdate() { return mnFkUserUpdate; }
    public Date getUserUpdateTs() { return mtUserUpdateTs; }
    
    public void setOldBizPartnerCommercial(String s) { msOldBizPartnerCommercial = s; }
    public void setOldEmail01(String s) { msOldEmail01 = s; }
    public void setOldTaxRegime(String s) { msOldTaxRegime = s; }
    public void setOldLeadTime(int n) { mnOldLeadTime = n; }
    
    public String getOldBizPartnerCommercial() { return msOldBizPartnerCommercial; }
    public String getOldEmail01() { return msOldEmail01; }
    public String getOldTaxRegime() { return msOldTaxRegime; }
    public int getOldLeadTime() { return mnOldLeadTime; }
    
    public void setAuxBizPartnerCategoryId(int n) { mnAuxBizPartnerCategoryId = n; }
    
    public int getAuxBizPartnerCategoryId() { return mnAuxBizPartnerCategoryId; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBizPartnerId = ((int[]) pk)[0];
        mnPkLogId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkBizPartnerId, mnPkLogId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkBizPartnerId = 0;
        mnPkLogId = 0;
        msBizPartnerCommercial = "";
        msEmail01 = "";
        msTaxRegime = "";
        mnLeadTime = 0;
        mnFkUserUpdate = 0;
        mtUserUpdateTs = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int save(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConsts.UNDEFINED;

        try {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT COALESCE(MAX(id_log), 0) + 1 AS _new_id " +
                        "FROM erp.bpsu_bp_upd_log " +
                        "WHERE id_bp = " + mnPkBizPartnerId + ";";
                
                ResultSet resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    mnPkLogId = resultSet.getInt(1);
                }
                
                sql = "INSERT INTO erp.bpsu_bp_upd_log VALUES (" +
                        mnPkBizPartnerId + ", " + 
                        mnPkLogId + ", " + 
                        "'" + msBizPartnerCommercial + "', " + 
                        "'" + msEmail01 + "', " + 
                        "'" + msTaxRegime + "', " + 
                        mnLeadTime + ", " + 
                        mnFkUserUpdate + ", " + 
                        "NOW()" + " " + 
                        ");";
                statement.execute(sql);
                
                if (!msBizPartnerCommercial.equals(msOldBizPartnerCommercial)) {
                    sql = "UPDATE erp.bpsu_bp SET bp_comm = '" + msBizPartnerCommercial + "' " +
                            "WHERE id_bp = " + mnPkBizPartnerId + ";";
                    statement.execute(sql); // stealth update, no direct user traceability!
                }
                
                if (!msEmail01.equals(msOldEmail01)) {
                    sql = "UPDATE erp.bpsu_bpb, erp.bpsu_bpb_con SET erp.bpsu_bpb_con.email_01 = '" + msEmail01 + "' " +
                            "WHERE erp.bpsu_bpb.fid_bp = " + mnPkBizPartnerId + " AND erp.bpsu_bpb.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " +
                            "AND erp.bpsu_bpb.id_bpb = erp.bpsu_bpb_con.id_bpb AND erp.bpsu_bpb_con.id_con = " + SUtilConsts.BRA_CON_ID + ";";
                    statement.execute(sql); // stealth update, no direct user traceability!
                }
                
                if (!msTaxRegime.equals(msOldTaxRegime) || mnLeadTime != mnOldLeadTime) {
                    String set = "";
                    
                    if (!msTaxRegime.equals(msOldTaxRegime)) {
                        set = "tax_regime = '" + msTaxRegime + "'";
                    }
                    
                    if (mnLeadTime != mnOldLeadTime) {
                        set += (set.isEmpty() ? "" : ", ") + "lead_time = " + mnLeadTime;
                    }
                    sql = "UPDATE erp.bpsu_bp_ct SET " + set + " " +
                            "WHERE id_bp = " + mnPkBizPartnerId + " AND id_ct_bp = " + mnAuxBizPartnerCategoryId + ";";
                    statement.execute(sql); // stealth update, no direct user traceability!
                }
            }
            
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.sql.SQLException e) {
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
    public java.util.Date getLastDbUpdate() {
        return null;
    }
}
