/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SFiscalAccounts extends SDbRegistryUser {

    protected ArrayList<SFiscalAccount> maFiscalAccounts;

    public SFiscalAccounts() {
        super(SModConsts.FINS_FISCAL_ACC);
    }

    public ArrayList<SFiscalAccount> getFiscalAccounts() { return maFiscalAccounts; }

    @Override
    public void setPrimaryKey(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initRegistry() {
        maFiscalAccounts = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        Statement statement = null;
        ResultSet resultSet = null;

        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        statement = session.getStatement().getConnection().createStatement();

        for (SFiscalAccount fiscalAccount : maFiscalAccounts) {
            if (fiscalAccount.isRowEdited()) {
                msSql = "SELECT fid_fiscal_acc FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " WHERE pk_acc = " + fiscalAccount.getPkAccountId() + " ";
                resultSet = statement.executeQuery(msSql);
                if (!resultSet.next()) {
                    throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
                }
                else {
                    if (fiscalAccount.getFkFiscalAccountId() != resultSet.getInt(1)) {
                        msSql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " SET "
                                + "fid_fiscal_acc = " + fiscalAccount.getFkFiscalAccountId() + ", "
                                + "fid_usr_edit = " + session.getUser().getPkUserId() + ", "
                                + "ts_edit = NOW() "
                                + "WHERE pk_acc = " + fiscalAccount.getPkAccountId() + " ";
                        session.getStatement().execute(msSql);
                    }
                }
            }
        }

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
