/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import erp.mod.fin.db.SDbCostCenter;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbPackCostCentersCostCenter extends SDbRegistryUser implements SGridRow {

    protected int mnPkPackCostCentersId;
    protected int mnPkCostCenterId;
    protected double mdProrationPercentage;
    
    protected String msDbmsCostCenterCode;
    protected String msDbmsCostCenterName;

    public SDbPackCostCentersCostCenter() {
        super(SModConsts.HRS_PACK_CC_CC);
    }

    public void setPkPackCostCentersId(int n) { mnPkPackCostCentersId = n; }
    public void setPkCostCenterId(int n) { mnPkCostCenterId = n; }
    public void setProrationPercentage(double d) { mdProrationPercentage = d; }

    public int getPkPackCostCentersId() { return mnPkPackCostCentersId; }
    public int getPkCostCenterId() { return mnPkCostCenterId; }
    public double getProrationPercentage() { return mdProrationPercentage; }

    public void setDbmsCostCenterCode(String s) { msDbmsCostCenterCode = s; }
    public void setDbmsCostCenterName(String s) { msDbmsCostCenterName = s; }

    public String getDbmsCostCenterCode() { return msDbmsCostCenterCode; }
    public String getDbmsCostCenterName() { return msDbmsCostCenterName; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPackCostCentersId = pk[0];
        mnPkCostCenterId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPackCostCentersId, mnPkCostCenterId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkPackCostCentersId = 0;
        mnPkCostCenterId = 0;
        mdProrationPercentage = 0;
        
        msDbmsCostCenterCode = "";
        msDbmsCostCenterName = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_pack_cc = " + mnPkPackCostCentersId + " "
                + "AND id_cc = " + mnPkCostCenterId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_pack_cc = " + pk[0] + " "
                + "AND id_cc = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            mnPkPackCostCentersId = resultSet.getInt("id_pack_cc");
            mnPkCostCenterId = resultSet.getInt("id_cc");
            mdProrationPercentage = resultSet.getDouble("prorat_per");

            mbRegistryNew = false;
            
            // read DBMS data:
            
            SDbCostCenter costCenter = new SDbCostCenter();
            
            msDbmsCostCenterCode = (String) costCenter.readField(session.getStatement(), new int[] { mnPkCostCenterId }, SDbCostCenter.FIELD_CODE_CC);
            msDbmsCostCenterName = (String) costCenter.readField(session.getStatement(), new int[] { mnPkCostCenterId }, SDbRegistry.FIELD_NAME);
        }

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
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPackCostCentersId + ", " + 
                    mnPkCostCenterId + ", " + 
                    mdProrationPercentage + " " + 
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_pack_cc = " + mnPkPackCostCentersId + ", " +
                    //"id_cc = " + mnPkCostCenterId + ", " +
                    "prorat_per = " + mdProrationPercentage + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbPackCostCentersCostCenter clone() throws CloneNotSupportedException {
        SDbPackCostCentersCostCenter registry = new SDbPackCostCentersCostCenter();

        registry.setPkPackCostCentersId(this.getPkPackCostCentersId());
        registry.setPkCostCenterId(this.getPkCostCenterId());
        registry.setProrationPercentage(this.getProrationPercentage());

        registry.setDbmsCostCenterCode(this.getDbmsCostCenterCode());
        registry.setDbmsCostCenterName(this.getDbmsCostCenterName());
    
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return getCode();
    }

    @Override
    public String getRowName() {
        return getName();
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
        
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = msDbmsCostCenterCode;
                break;
                
            case 1:
                value = msDbmsCostCenterName;
                break;
                
            case 2:
                value = mdProrationPercentage;
                break;
                
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        
    }
}
