/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author Isabel Servín
 */
public class SDataMatchingItemBizPartnerConcept extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    protected int mnPkMatchId;
    protected java.lang.String msConceptKey;
    protected double mdFactorConversion;
    protected int mnUses;
    
    protected java.util.Date mtUseFirst;
    protected java.util.Date mtUseLast;
    
    protected boolean mbIsDeleted;
    
    protected int mnFkBizPartnerId;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkItemIdRef;
    protected String mnFkCostCenter;
    protected int mnFkTaxRegionId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;
   
    
    public SDataMatchingItemBizPartnerConcept() {
        super(SModConsts.ITMU_MATCH_ITEM_CPT_BP);
        reset();
    }

    public void setPkMatchId(int n) { mnPkMatchId = n; }
    public void setConceptKey(java.lang.String s) { msConceptKey = s; }
    public void setFactorConversion(double d) { mdFactorConversion = d; }
    public void setUses(int n) { mnUses = n; }
    public void setUseFirst(java.util.Date t) { mtUseFirst = t; }
    public void setUseLast(java.util.Date t) { mtUseLast = t; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkItemIdRef(int n) { mnFkItemIdRef = n; }
    public void setFkCostCenter(String s) { mnFkCostCenter = s; }
    public void setFkTaxRegionId(int n) { mnFkTaxRegionId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }
    
    public int getPkMatchId() { return mnPkMatchId; }
    public java.lang.String getConceptKey() { return msConceptKey; }
    public double getFactorConversion() { return mdFactorConversion; }
    public int getUses() { return mnUses; }
    public java.util.Date getUseFirst() { return mtUseFirst; }
    public java.util.Date getUseLast() { return mtUseLast; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkItemIdRef() { return mnFkItemIdRef; }
    public String getFkCostCenter() { return mnFkCostCenter; }
    public int getFkTaxRegionId() { return mnFkTaxRegionId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkMatchId = ((int[]) pk)[0];
        mnFkBizPartnerId = ((int[]) pk)[1];
        mnFkItemId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkMatchId, mnFkBizPartnerId, mnFkItemId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        mnPkMatchId = 0;
        msConceptKey = "";
        mdFactorConversion = 0;
        mnUses = 0;
        mtUseFirst = null;
        mtUseLast = null;
        mbIsDeleted = false;
        mnFkBizPartnerId = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkItemIdRef = 0;
        mnFkCostCenter = "";
        mnFkTaxRegionId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int key = (int) pk;
        String sql;
        ResultSet resultSet;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT mt.* FROM " +
                "erp.itmu_match_item_cpt_bp AS mt " +
                "WHERE mt.id_match = " + key + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkMatchId = resultSet.getInt("id_match");
                msConceptKey = resultSet.getString("cpt_key");
                mdFactorConversion = resultSet.getDouble("fact_conv");
                mnUses = resultSet.getInt("uses");
                mtUseFirst = resultSet.getDate("use_first");
                mtUseLast = resultSet.getDate("use_last");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkBizPartnerId = resultSet.getInt("fid_bp");
                mnFkItemId = resultSet.getInt("fid_item");
                mnFkUnitId = resultSet.getInt("fid_unit");
                mnFkItemIdRef = resultSet.getInt("fid_item_ref_n");
                mnFkCostCenter = resultSet.getString("fid_cc");
                mnFkTaxRegionId = resultSet.getInt("fid_tax_reg");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                // Read aswell unit object:

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
    public int save(java.sql.Connection connection) {
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL erp.itmu_match_item_cpt_bp_save(" +  
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkMatchId);
            callableStatement.setString(nParam++, msConceptKey);
            callableStatement.setDouble(nParam++, mdFactorConversion);
            callableStatement.setInt(nParam++, mnUses);
            callableStatement.setDate(nParam++, new java.sql.Date(mtUseFirst.getTime()));
            callableStatement.setDate(nParam++, new java.sql.Date(mtUseLast.getTime()));
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkBizPartnerId);
            callableStatement.setInt(nParam++, mnFkItemId);
            callableStatement.setInt(nParam++, mnFkUnitId);
            callableStatement.setInt(nParam++, mnFkItemIdRef);
            callableStatement.setString(nParam++, mnFkCostCenter);
            callableStatement.setInt(nParam++, mnFkTaxRegionId);
            callableStatement.setInt(nParam++, mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();
            
            mnPkMatchId = callableStatement.getInt(nParam -3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }
    
    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}
