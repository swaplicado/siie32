/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mfin.data.SDataCostCenter;
import erp.mod.SModConsts;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbMaterialRequestCostCenter extends SDbRegistryUser implements SGridRow, Serializable {
    
    protected int mnPkMatRequestId;
    protected int mnPkEntMatConsumptionEntityId;
    protected int mnPkSubentMatConsumptionEntityId;
    protected int mnPkSubentMatConsumptionSubentityId;
    protected int mnPkCostCenterId;
    protected double mdPercentage;
    protected int mnFkBudgetMatConsumptionEntityId;
    protected int mnFkBudgetYearId;
    protected int mnFkBudgetPeriodId;
    
    protected SDbMaterialConsumptionSubentity moSDbConsSubent;
    protected SDataCostCenter moDataCostCenter;
    protected SDbMaterialConsumptionEntityBudget moSDbEntBudget;
    
    public SDbMaterialRequestCostCenter() {
        super(SModConsts.TRN_MAT_REQ_CC);
    }
    
    public void setPkMatRequestId(int n) { mnPkMatRequestId = n; }
    public void setPkEntMatConsumptionEntityId(int n) { mnPkEntMatConsumptionEntityId = n; }
    public void setPkSubentMatConsumptionEntityId(int n) { mnPkSubentMatConsumptionEntityId = n; }
    public void setPkSubentMatConsumptionSubentityId(int n) { mnPkSubentMatConsumptionSubentityId = n; }
    public void setPkCostCenterId(int n) { mnPkCostCenterId = n; }
    public void setPercentage(double d) { mdPercentage = d; }
    public void setFkBudgetMatConsumptionEntityId(int n) { mnFkBudgetMatConsumptionEntityId = n; }
    public void setFkBudgetYearId(int n) { mnFkBudgetYearId = n; }
    public void setFkBudgetPeriodId(int n) { mnFkBudgetPeriodId = n; }

    public int getPkMatRequestId() { return mnPkMatRequestId; }
    public int getPkEntMatConsumptionEntityId() { return mnPkEntMatConsumptionEntityId; }
    public int getPkSubentMatConsumptionEntityId() { return mnPkSubentMatConsumptionEntityId; }
    public int getPkSubentMatConsumptionSubentityId() { return mnPkSubentMatConsumptionSubentityId; }
    public int getPkCostCenterId() { return mnPkCostCenterId; }
    public double getPercentage() { return mdPercentage; }
    public int getFkBudgetMatConsumptionEntityId() { return mnFkBudgetMatConsumptionEntityId; }
    public int getFkBudgetYearId() { return mnFkBudgetYearId; }
    public int getFkBudgetPeriodId() { return mnFkBudgetPeriodId; }
    
    public void readSubentity(SGuiSession session) throws Exception {
        moSDbConsSubent = new SDbMaterialConsumptionSubentity();
        moSDbConsSubent.read(session, new int[] { mnPkSubentMatConsumptionEntityId, mnPkSubentMatConsumptionSubentityId });
    }

    public void readCostCenter(SGuiSession session) throws Exception {
        ResultSet resultSet;
        
        msSql = "SELECT id_cc FROM " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " "
                + "WHERE pk_cc = " + mnPkCostCenterId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            moDataCostCenter = new SDataCostCenter();
            moDataCostCenter.read(new String[] { resultSet.getString(1) }, session.getStatement() );
        }
    }
    
    public void readEntBudget(SGuiSession session) throws Exception {
        moSDbEntBudget = new SDbMaterialConsumptionEntityBudget();
        moSDbEntBudget.read(session, new int[] { mnFkBudgetMatConsumptionEntityId, mnFkBudgetYearId, mnFkBudgetPeriodId });
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatRequestId = pk[0];
        mnPkEntMatConsumptionEntityId = pk[1];
        mnPkSubentMatConsumptionEntityId = pk[2];
        mnPkSubentMatConsumptionSubentityId = pk[3];
        mnPkCostCenterId = pk[4];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatRequestId, mnPkEntMatConsumptionEntityId, mnPkSubentMatConsumptionEntityId, mnPkSubentMatConsumptionSubentityId, mnPkCostCenterId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMatRequestId = 0;
        mnPkEntMatConsumptionEntityId = 0;
        mnPkSubentMatConsumptionEntityId = 0;
        mnPkSubentMatConsumptionSubentityId = 0;
        mnPkCostCenterId = 0;
        mdPercentage = 0;
        mnFkBudgetMatConsumptionEntityId = 0;
        mnFkBudgetYearId = 0;
        mnFkBudgetPeriodId = 0;
        
        moSDbConsSubent = null;
        moDataCostCenter = null;
        moSDbEntBudget = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mat_req = " + mnPkMatRequestId + " " + 
                "AND id_mat_ent_cons_ent = " + mnPkEntMatConsumptionEntityId + " " + 
                "AND id_mat_subent_cons_ent = " + mnPkSubentMatConsumptionEntityId + " " +
                "AND id_mat_subent_cons_subent = " + mnPkSubentMatConsumptionSubentityId + " " + 
                "AND id_cc = " + mnPkCostCenterId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mat_req = " + pk[0] + " " + 
                "AND id_mat_ent_cons_ent = " + pk[1] + " " + 
                "AND id_mat_subent_cons_ent = " + pk[2] + " " +
                "AND id_mat_subent_cons_subent = " + pk[3] + " " + 
                "AND id_cc = " + pk[4] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            mnPkMatRequestId = resultSet.getInt("id_mat_req");
            mnPkEntMatConsumptionEntityId = resultSet.getInt("id_mat_ent_cons_ent");
            mnPkSubentMatConsumptionEntityId = resultSet.getInt("id_mat_subent_cons_ent");
            mnPkSubentMatConsumptionSubentityId = resultSet.getInt("id_mat_subent_cons_subent");
            mnPkCostCenterId = resultSet.getInt("id_cc");
            mdPercentage = resultSet.getDouble("per");
            mnFkBudgetMatConsumptionEntityId = resultSet.getInt("fk_budget_mat_cons_ent");
            mnFkBudgetYearId = resultSet.getInt("fk_budget_year");
            mnFkBudgetPeriodId = resultSet.getInt("fk_budget_period");
            
            readSubentity(session);
            readCostCenter(session);
            readEntBudget(session);
            
            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkMatRequestId + ", " + 
                    mnPkEntMatConsumptionEntityId + ", " + 
                    mnPkSubentMatConsumptionEntityId + ", " + 
                    mnPkSubentMatConsumptionSubentityId + ", " + 
                    mnPkCostCenterId + ", " + 
                    mdPercentage + ", " + 
                    mnFkBudgetMatConsumptionEntityId + ", " + 
                    mnFkBudgetYearId + ", " + 
                    mnFkBudgetPeriodId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_mat_req = " + mnPkMatRequestId + ", " +
                    //"id_mat_ent_cons_ent = " + mnPkEntMatConsumptionEntityId + ", " +
                    //"id_mat_subent_cons_ent = " + mnPkSubentMatConsumptionEntityId + ", " +
                    //"id_mat_subent_cons_subent = " + mnPkSubentMatConsumptionSubentityId + ", " +
                    //"id_cc = " + mnPkCostCenterId + ", " +
                    "per = " + mdPercentage + ", " +
                    "fk_budget_mat_cons_ent = " + mnFkBudgetMatConsumptionEntityId + ", " +
                    "fk_budget_year = " + mnFkBudgetYearId + ", " +
                    "fk_budget_period = " + mnFkBudgetPeriodId + " " +
                    getSqlWhere();
        }
    }

    @Override
    public SDbMaterialRequestCostCenter clone() throws CloneNotSupportedException {
        SDbMaterialRequestCostCenter registry = new SDbMaterialRequestCostCenter();
        
        registry.setPkMatRequestId(this.getPkMatRequestId());
        registry.setPkEntMatConsumptionEntityId(this.getPkEntMatConsumptionEntityId());
        registry.setPkSubentMatConsumptionEntityId(this.getPkSubentMatConsumptionEntityId());
        registry.setPkSubentMatConsumptionSubentityId(this.getPkSubentMatConsumptionSubentityId());
        registry.setPkCostCenterId(this.getPkCostCenterId());
        registry.setPercentage(this.getPercentage());
        registry.setFkBudgetMatConsumptionEntityId(this.getFkBudgetMatConsumptionEntityId());
        registry.setFkBudgetYearId(this.getFkBudgetYearId());
        registry.setFkBudgetPeriodId(this.getFkBudgetPeriodId());
        
        registry.setRegistryNew(this.isRegistryNew());
        
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
            case 0: value = moSDbConsSubent.getXtaConsumptionEntityName(); break;
            case 1: value = moSDbConsSubent.getName(); break;
            case 2: value = moDataCostCenter.getPkCostCenterIdXXX(); break;
            case 3: value = mdPercentage; break;
            case 4: value = mnFkBudgetYearId; break;
            case 5: value = SLibUtils.DateFormatDateShort.format(moSDbEntBudget.getDateStart()); break;
            case 6: value = SLibUtils.DateFormatDateShort.format(moSDbEntBudget.getDateEnd()); break;
            case 7: value = moSDbEntBudget.getBudget(); break;
        }
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
