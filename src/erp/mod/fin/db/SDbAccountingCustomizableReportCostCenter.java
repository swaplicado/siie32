/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import erp.mfin.data.SDataCostCenter;
import erp.mod.SModConsts;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servin
 */
public class SDbAccountingCustomizableReportCostCenter extends SDbRegistryUser implements SGridRow, Serializable {
    
    protected int mnPkReportCustomizableAccountId;
    protected int mnPkCostCenterStartId;
    protected int mnPkCostCenterEndId;
    
    protected SDataCostCenter moDbmsCostCenterStart;
    protected SDataCostCenter moDbmsCostCenterEnd;

    public SDbAccountingCustomizableReportCostCenter() {
        super(SModConsts.FIN_REP_CUS_ACC_CC);
        initRegistry();
    }
    
    public void setPkReportCustomizableAccountId(int n) { mnPkReportCustomizableAccountId = n; }
    public void setPkCostCenterStartId(int n) { mnPkCostCenterStartId = n; }
    public void setPkCostCenterEndId(int n) { mnPkCostCenterEndId = n; }

    public int getPkReportCustomizableAccountId() { return mnPkReportCustomizableAccountId; }
    public int getPkCostCenterStartId() { return mnPkCostCenterStartId; }
    public int getPkCostCenterEndId() { return mnPkCostCenterEndId; }
    
    public void setDbmsCostCenterStart(SDataCostCenter o) { moDbmsCostCenterStart = o; }
    public void setDbmsCostCenterEnd(SDataCostCenter o) { moDbmsCostCenterEnd = o; }
    
    public SDataCostCenter getDbmsCostCenterStart() { return moDbmsCostCenterStart; }
    public SDataCostCenter getDbmsCostCenterEnd() { return moDbmsCostCenterEnd; }
    
    public void readCostCenterStart(Statement statement) throws Exception {
        String pkCc = "000-00-00-000";
        String sql = "SELECT id_cc FROM fin_cc WHERE pk_cc = " + mnPkCostCenterStartId;
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            pkCc = resultSet.getString(1);
        }
        moDbmsCostCenterStart = new SDataCostCenter();
        moDbmsCostCenterStart.read(new Object[] { pkCc }, statement);
    }
    
    public void readCostCenterEnd(Statement statement) throws Exception {
        String pkCc = "000-00-00-000";
        String sql = "SELECT id_cc FROM fin_cc WHERE pk_cc = " + mnPkCostCenterEndId;
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            pkCc = resultSet.getString(1);
        }
        moDbmsCostCenterEnd = new SDataCostCenter();
        moDbmsCostCenterEnd.read(new Object[] { pkCc }, statement);
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkReportCustomizableAccountId = pk[0];
        mnPkCostCenterStartId = pk[1];
        mnPkCostCenterEndId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkReportCustomizableAccountId, mnPkCostCenterStartId, mnPkCostCenterEndId }; 
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkReportCustomizableAccountId = 0;
        mnPkCostCenterStartId = 0;
        mnPkCostCenterEndId = 0;
        
        moDbmsCostCenterStart = null;
        moDbmsCostCenterEnd = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_rep_cus_acc = " + mnPkReportCustomizableAccountId + " "
                + "AND id_cc_start = " + mnPkCostCenterStartId + " "
                + "AND id_cc_end = " + mnPkCostCenterEndId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_rep_cus_acc = " + pk[0] + " "
                + "AND id_cc_start = " + pk[1] + " "
                + "AND id_cc_end = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkReportCustomizableAccountId = resultSet.getInt("id_rep_cus_acc");
            mnPkCostCenterStartId = resultSet.getInt("id_cc_start");
            mnPkCostCenterEndId = resultSet.getInt("id_cc_end");
            
            mbRegistryNew = false;
        }
        
        readCostCenterStart(session.getStatement());
        readCostCenterEnd(session.getStatement());
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkReportCustomizableAccountId + ", " + 
                    mnPkCostCenterStartId + ", " + 
                    mnPkCostCenterEndId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "id_rep_cus_acc = " + mnPkReportCustomizableAccountId + ", " +
                    "id_cc_start = " + mnPkCostCenterStartId + ", " +
                    "id_cc_end = " + mnPkCostCenterEndId + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbAccountingCustomizableReportCostCenter clone() throws CloneNotSupportedException {
        SDbAccountingCustomizableReportCostCenter registry = new SDbAccountingCustomizableReportCostCenter();
        
        registry.setPkReportCustomizableAccountId(this.getPkReportCustomizableAccountId());
        registry.setPkCostCenterStartId(this.getPkCostCenterStartId());
        registry.setPkCostCenterEndId(this.getPkCostCenterEndId());
        
        registry.setDbmsCostCenterStart(this.getDbmsCostCenterStart());
        registry.setDbmsCostCenterEnd(this.getDbmsCostCenterEnd());

        return registry;
    }    

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
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
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        setRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0: value = moDbmsCostCenterStart.getPkCostCenterIdXXX(); break;
            case 1: value = moDbmsCostCenterStart.getCostCenter(); break;
            case 2: value = moDbmsCostCenterEnd.getPkCostCenterIdXXX(); break;
            case 3: value = moDbmsCostCenterEnd.getCostCenter(); break;
        }
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
