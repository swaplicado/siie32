/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public class SDbDpsEntryPrice extends SDbRegistryUser implements SGridRow {

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

    protected double mdOriginalQuantityToProcess;
    protected double mdOriginalQuantityProcessed;

    public SDbDpsEntryPrice() {
        super(SModConsts.TRN_DPS_ETY_PRC);
    }

    /*
     * Public methods
     */

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
    
    public void setOriginalQuantityToProcess(double d) { mdOriginalQuantityToProcess = d; }
    public void setOriginalQuantityProcessed(double d) { mdOriginalQuantityProcessed = d; }
    
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

    public double getOriginalQuantityToProcess() { return mdOriginalQuantityToProcess; }
    public double getOriginalQuantityProcessed() { return mdOriginalQuantityProcessed; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkEntryId = ((int[]) pk)[2];
        mnPkPriceId = ((int[]) pk)[3];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId, mnPkPriceId };
    }

    @Override
    public void initRegistry() {
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
        
        mdOriginalQuantityToProcess = 0;
        mdOriginalQuantityProcessed = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + 
                " AND id_ety = " + mnPkEntryId +  " AND id_prc = " + mnPkPriceId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_year = " + pk[0] + " AND id_doc = " + pk[1] + 
                " AND id_ety = " + pk[2] +  " AND id_prc = " + pk[3] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
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

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch (row) {
            case 0:
                value = msReferenceNumber;
                break;
            case 1:
                value = mnContractPriceYear;
                break;
            case 2:
                value = mnContractPriceMonth;
                break;
            case 3:
                value = mbIsPriceVariable;
                break;
            case 4:
                value = mdOriginalPriceUnitaryCy;
                break;
            case 5:
                value = mdOriginalPriceUnitaryCySystem;
                break;
            case 6:
                value = mdContractBase;
                break;
            case 7:
                value = mdContractFuture;
                break;
            case 8:
                value = mdContractFactor;
                break;
            case 9:
                value = mdOriginalQuantity;
                break;
            case 10:
                value = mdOriginalQuantityProcessed;
                break;
            case 11:
                value = mdOriginalQuantityToProcess;
                break;
            default:
                break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch (row) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                break;
            default:
                break;
        }
    }
}
