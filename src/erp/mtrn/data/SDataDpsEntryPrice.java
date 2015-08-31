/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Irving Sánchez
 */
public class SDataDpsEntryPrice  extends erp.lib.data.SDataRegistry implements java.io.Serializable{
    
    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected int mnPkPriceId;
    protected String msReferenceNumber;
    protected double mdOriginalQuantity;
    protected double mdOriginalPriceUnitaryCy;
    protected double mdOriginalPriceUnitaryCySystem;
    protected double mdContractBase;
    protected double mdContractFuture;
    protected double mdContractFactor;
    protected int mnContractPriceYear;
    protected int mnContractPriceMonth;
    protected boolean mbIsPriceVariable;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;
    
    public SDataDpsEntryPrice() {
        super(SDataConstants.TRN_DPS_ETY_PRC);
        
        reset();
    }
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setPkPriceId(int n) { mnPkPriceId = n; }
    public void setReferenceNumber(String s) { msReferenceNumber = s; }
    public void setOriginalQuantity(double d) { mdOriginalQuantity = d; }
    public void setOriginalPriceUnitaryCy(double d) { mdOriginalPriceUnitaryCy = d; }
    public void setOriginalPriceUnitaryCySystem(double d) { mdOriginalPriceUnitaryCySystem = d; }
    public void setContractBase(double d) { mdContractBase = d; }
    public void setContractFuture(double d) { mdContractFuture = d; }
    public void setContractFactor(double d) { mdContractFactor = d; }
    public void setContractPriceYear(int n) { mnContractPriceYear = n; }
    public void setContractPriceMonth(int n) { mnContractPriceMonth = n; }
    public void setIsPriceVariable(boolean b) { mbIsPriceVariable = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }
    
    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getPkPriceId() { return mnPkPriceId; }
    public String getReferenceNumber() { return msReferenceNumber; }
    public double getOriginalQuantity() { return mdOriginalQuantity; }
    public double getOriginalPriceUnitaryCy() { return mdOriginalPriceUnitaryCy; }
    public double getOriginalPriceUnitaryCySystem() { return mdOriginalPriceUnitaryCySystem; }
    public double getContractBase() { return mdContractBase; }
    public double getContractFuture() { return mdContractFuture; }
    public double getContractFactor() { return mdContractFactor; }
    public int getContractPriceYear() { return mnContractPriceYear; }
    public int getContractPriceMonth() { return mnContractPriceMonth; }
    public boolean getIsPriceVariable() { return mbIsPriceVariable; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkEntryId = ((int[]) pk)[2];
        mnPkPriceId = ((int[]) pk)[3];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId, mnPkPriceId};
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        mnPkPriceId = 0;
        msReferenceNumber = "";
        mdOriginalQuantity = 0;
        mdOriginalPriceUnitaryCy = 0;
        mdOriginalPriceUnitaryCySystem = 0;
        mdContractBase = 0;
        mdContractFuture = 0;
        mdContractFactor = 0;
        mnContractPriceYear = 0;
        mnContractPriceMonth = 0;
        mbIsPriceVariable = false;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;        
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT dep.* " +
                    "FROM trn_dps_ety_prc AS dep " +
                    "WHERE dep.id_year = " + key[0] + " AND dep.id_doc = " + key[1] + " AND dep.id_ety = " + key[2] +  " AND dep.id_prc = " + key[3] + " ";

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                mnPkEntryId = resultSet.getInt("id_ety");
                mnPkPriceId = resultSet.getInt("id_prc");
                msReferenceNumber = resultSet.getString("num_ref");
                mdOriginalQuantity = resultSet.getDouble("orig_qty");
                mdOriginalPriceUnitaryCy = resultSet.getDouble("orig_price_u_cur");
                mdOriginalPriceUnitaryCySystem = resultSet.getDouble("orig_price_u_cur_sys");
                mdContractBase = resultSet.getDouble("con_base");
                mdContractFuture = resultSet.getDouble("con_future");
                mdContractFactor = resultSet.getDouble("con_factor");
                mnContractPriceYear = resultSet.getInt("con_prc_year");
                mnContractPriceMonth = resultSet.getInt("con_prc_mon");
                mbIsPriceVariable = resultSet.getBoolean("b_prc_var");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");
                
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
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL trn_dps_ety_prc_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkDocId);
            callableStatement.setInt(nParam++, mnPkEntryId);
            callableStatement.setInt(nParam++, mnPkPriceId);
            callableStatement.setString(nParam++, msReferenceNumber);
            callableStatement.setDouble(nParam++, mdOriginalQuantity);
            callableStatement.setDouble(nParam++, mdOriginalPriceUnitaryCy);
            callableStatement.setDouble(nParam++, mdOriginalPriceUnitaryCySystem);
            callableStatement.setDouble(nParam++, mdContractBase);
            callableStatement.setDouble(nParam++, mdContractFuture);
            callableStatement.setDouble(nParam++, mdContractFactor);
            callableStatement.setInt(nParam++, mnContractPriceYear);
            callableStatement.setInt(nParam++, mnContractPriceMonth);
            callableStatement.setBoolean(nParam++, mbIsPriceVariable);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkEntryId = callableStatement.getInt(nParam - 3);
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
    public Date getLastDbUpdate () {
        return mtUserEditTs;
    }
    
    @Override
     public erp.mtrn.data.SDataDpsEntryPrice clone () {
        SDataDpsEntryPrice clone = new SDataDpsEntryPrice();
        
        clone.setIsRegistryNew(mbIsRegistryNew);
        
        clone.setPkYearId(mnPkYearId);
        clone.setPkDocId(mnPkDocId);
        clone.setPkEntryId(mnPkEntryId);
        clone.setPkPriceId(mnPkPriceId);
        clone.setReferenceNumber(msReferenceNumber);
        clone.setOriginalQuantity(mdOriginalQuantity);
        clone.setOriginalPriceUnitaryCy(mdOriginalPriceUnitaryCy);
        clone.setOriginalPriceUnitaryCySystem(mdOriginalPriceUnitaryCySystem);
        clone.setContractBase(mdContractBase);
        clone.setContractFuture(mdContractFuture);
        clone.setContractFactor(mdContractFactor);
        clone.setContractPriceYear(mnContractPriceYear);
        clone.setContractPriceMonth(mnContractPriceMonth);
        clone.setIsPriceVariable(mbIsPriceVariable);
        clone.setIsDeleted(mbIsDeleted);
        clone.setFkUserNewId(mnFkUserNewId);
        clone.setFkUserEditId(mnFkUserEditId);
        clone.setFkUserDeleteId(mnFkUserDeleteId);
        clone.setUserNewTs(mtUserNewTs);
        clone.setUserEditTs(mtUserEditTs);
        clone.setUserDeleteTs(mtUserDeleteTs);
        
        return clone;        
     }   
}
