/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbCheckWallet extends SDbRegistryUser {

    protected int mnPkCheckWalletId;
    protected int mnNumberStart;
    protected int mnNumberEnd_n;
    protected boolean mbIsActive;
    protected boolean mbIsDeleted;
    protected int mnFkCompanyBranchId;
    protected int mnFkAccountCashId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected Date mtUserNewTs;
    protected Date mtUserEditTs;
    protected Date mtUserDeleteTs;

    public SDbCheckWallet() {
        super(SModConsts.FIN_CHECK_WAL);
    }

    /*
     * Public methods
     */

    public void setPkCheckWalletId(int n) { mnPkCheckWalletId = n; }
    public void setNumberStart(int n) { mnNumberStart = n; }
    public void setNumberEnd_n(int n) { mnNumberEnd_n = n; }
    public void setIsActive(boolean b) { mbIsActive = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkAccountCashId(int n) { mnFkAccountCashId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkCheckWalletId() { return mnPkCheckWalletId; }
    public int getNumberStart() { return mnNumberStart; }
    public int getNumberEnd_n() { return mnNumberEnd_n; }
    public boolean getIsActive() { return mbIsActive; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkAccountCashId() { return mnFkAccountCashId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCheckWalletId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCheckWalletId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCheckWalletId = 0;
        mnNumberStart = 0;
        mnNumberEnd_n = 0;
        mbIsActive = false;
        mbIsDeleted = false;
        mnFkCompanyBranchId = 0;
        mnFkAccountCashId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(SModConsts.FIN_CHECK_WAL);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_check_wal = " + mnPkCheckWalletId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_check_wal = " + pk[0] + " ";
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
            mnPkCheckWalletId = resultSet.getInt("id_check_wal");
            mnNumberStart = resultSet.getInt("num_start");
            mnNumberEnd_n = resultSet.getInt("num_end_n");
            mbIsActive = resultSet.getBoolean("b_act");
            mbIsDeleted = resultSet.getBoolean("b_del");
            mnFkCompanyBranchId = resultSet.getInt("fid_cob");
            mnFkAccountCashId = resultSet.getInt("fid_acc_cash");
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
    public SDbCheckWallet clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
