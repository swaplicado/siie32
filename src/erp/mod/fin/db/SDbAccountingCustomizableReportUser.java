/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

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
public class SDbAccountingCustomizableReportUser extends SDbRegistryUser implements SGridRow, Serializable {
    
    protected int mnPkReportCustomizableAccountId;
    protected int mnPkUserId;
    
    protected String msXtaUser;

    public SDbAccountingCustomizableReportUser() {
        super(SModConsts.FIN_REP_CUS_ACC_USR);
        initRegistry();
    }
    
    public void setPkReportCustomizableAccountId(int n) { mnPkReportCustomizableAccountId = n; }
    public void setPkUserId(int n) { mnPkUserId = n; }

    public int getPkReportCustomizableAccountId() { return mnPkReportCustomizableAccountId; }
    public int getPkUserId() { return mnPkUserId; }
    
    public void setXtaUser(String s) { msXtaUser = s; }
    public String getXtaUser() { return msXtaUser; }
    
    public void readUser(SGuiSession session) throws Exception {
        Statement statement = session.getDatabase().getConnection().createStatement();
        msSql = "SELECT usr FROM erp.usru_usr WHERE id_usr = " + mnPkUserId;
        ResultSet resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            msXtaUser = resultSet.getString(1);
        }
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkReportCustomizableAccountId = pk[0];
        mnPkUserId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkReportCustomizableAccountId, mnPkUserId }; 
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkReportCustomizableAccountId = 0;
        mnPkUserId= 0;
        
        msXtaUser = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_rep_cus_acc = " + mnPkReportCustomizableAccountId + " "
                + "AND id_usr = " + mnPkUserId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_rep_cus_acc = " + pk[0] + " "
                + "AND id_usr = " + pk[1] + " ";
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
            mnPkUserId = resultSet.getInt("id_usr");
            
            mbRegistryNew = false;
        }
        
        readUser(session);
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
                    mnPkUserId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "id_rep_cus_acc = " + mnPkReportCustomizableAccountId + ", " +
                    "id_rep_cus_acc = " + mnPkReportCustomizableAccountId + ", " +
                    "id_usr = " + mnPkUserId + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbAccountingCustomizableReportUser clone() throws CloneNotSupportedException {
        SDbAccountingCustomizableReportUser registry = new SDbAccountingCustomizableReportUser();
        
        registry.setPkReportCustomizableAccountId(this.getPkReportCustomizableAccountId());
        registry.setPkUserId(this.getPkUserId());
        
        registry.setXtaUser(this.getXtaUser());

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
            case 0: value = msXtaUser; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
