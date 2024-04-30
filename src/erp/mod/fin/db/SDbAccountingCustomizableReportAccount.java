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
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servin
 */
public class SDbAccountingCustomizableReportAccount extends SDbRegistryUser implements SGridRow, Serializable {
    
    protected int mnPkReportCustomizableAccountId;
    protected int mnPkAccountStartId;
    protected int mnPkAccountEndId;
    
    protected SDbAccount moDbmsAccountStart;
    protected SDbAccount moDbmsAccountEnd;

    public SDbAccountingCustomizableReportAccount() {
        super(SModConsts.FIN_REP_CUS_ACC_ACC);
        initRegistry();
    }
    
    public void setPkReportCustomizableAccountId(int n) { mnPkReportCustomizableAccountId = n; }
    public void setPkAccountStartId(int n) { mnPkAccountStartId = n; }
    public void setPkAccountEndId(int n) { mnPkAccountEndId = n; }

    public int getPkReportCustomizableAccountId() { return mnPkReportCustomizableAccountId; }
    public int getPkAccountStartId() { return mnPkAccountStartId; }
    public int getPkAccountEndId() { return mnPkAccountEndId; }
    
    public void setDbmsAccountStart(SDbAccount o) { moDbmsAccountStart = o; }
    public void setDbmsAccountEnd(SDbAccount o) { moDbmsAccountEnd = o; }
    
    public SDbAccount getDbmsAccountStart() { return moDbmsAccountStart; }
    public SDbAccount getDbmsAccountEnd() { return moDbmsAccountEnd; }
    
    public void readAccountStart(SGuiSession session) throws Exception {
        moDbmsAccountStart = new SDbAccount();
        moDbmsAccountStart.read(session, new int[] { mnPkAccountStartId }) ;
    }
    
    public void readAccountEnd(SGuiSession session) throws Exception {
        moDbmsAccountEnd = new SDbAccount();
        moDbmsAccountEnd.read(session, new int[] { mnPkAccountEndId }) ;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkReportCustomizableAccountId = pk[0];
        mnPkAccountStartId = pk[1];
        mnPkAccountEndId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkReportCustomizableAccountId, mnPkAccountStartId, mnPkAccountEndId }; 
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkReportCustomizableAccountId = 0;
        mnPkAccountStartId = 0;
        mnPkAccountEndId = 0;
        
        moDbmsAccountStart = null;
        moDbmsAccountEnd = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_rep_cus_acc = " + mnPkReportCustomizableAccountId + " "
                + "AND id_acc_start = " + mnPkAccountStartId + " "
                + "AND id_acc_end = " + mnPkAccountEndId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_rep_cus_acc = " + pk[0] + " "
                + "AND id_acc_start = " + pk[1] + " "
                + "AND id_acc_end = " + pk[2] + " ";
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
            mnPkAccountStartId = resultSet.getInt("id_acc_start");
            mnPkAccountEndId = resultSet.getInt("id_acc_end");
            
            mbRegistryNew = false;
        }
        
        readAccountStart(session);
        readAccountEnd(session);
        
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
                    mnPkAccountStartId + ", " + 
                    mnPkAccountEndId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "id_rep_cus_acc = " + mnPkReportCustomizableAccountId + ", " +
                    "id_acc_start = " + mnPkAccountStartId + ", " +
                    "id_acc_end = " + mnPkAccountEndId + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbAccountingCustomizableReportAccount clone() throws CloneNotSupportedException {
        SDbAccountingCustomizableReportAccount registry = new SDbAccountingCustomizableReportAccount();
        
        registry.setPkReportCustomizableAccountId(this.getPkReportCustomizableAccountId());
        registry.setPkAccountStartId(this.getPkAccountStartId());
        registry.setPkAccountEndId(this.getPkAccountEndId());

        registry.setDbmsAccountStart(this.getDbmsAccountStart());
        registry.setDbmsAccountEnd(this.getDbmsAccountEnd());
        
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
            case 0: value = moDbmsAccountStart.getPkAccountIdXXX(); break;
            case 1: value = moDbmsAccountStart.getAccount(); break;
            case 2: value = moDbmsAccountEnd.getPkAccountIdXXX(); break;
            case 3: value = moDbmsAccountEnd.getAccount(); break;
        }
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
