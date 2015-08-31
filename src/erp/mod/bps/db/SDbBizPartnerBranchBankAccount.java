/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.bps.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbBizPartnerBranchBankAccount extends SDbRegistryUser {

    protected int mnPkBizPartnerBranchId;
    protected int mnPkBankAccountId;
    protected String msBankAccount;
    protected String msBankAccountBranchNumber;
    protected String msBankAccountNumber;
    protected String msBankAccountNumberStd;
    protected String msAgreement;
    protected String msReference;
    protected String msCode;
    protected String msCodeAba;
    protected String msCodeSwift;
    protected boolean mbIsCardApplying;
    protected boolean mbIsDefault;
    protected boolean mbIsDeleted;
    protected int mnFkBankId;
    protected int mnFkAccountCashCategoryId;
    protected int mnFkAccountCashTypeId;
    protected int mnFkCurrencyId;
    protected int mnFkCardIssuerId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected Date mtUserNewTs;
    protected Date mtUserEditTs;
    protected Date mtUserDeleteTs;

    public SDbBizPartnerBranchBankAccount() {
        super(SModConsts.BPSU_BANK_ACC);
    }

    /*
     * Public methods
     */

    public void setPkBizPartnerBranchId(int n) { mnPkBizPartnerBranchId = n; }
    public void setPkBankAccountId(int n) { mnPkBankAccountId = n; }
    public void setBankAccount(String s) { msBankAccount = s; }
    public void setBankAccountBranchNumber(String s) { msBankAccountBranchNumber = s; }
    public void setBankAccountNumber(String s) { msBankAccountNumber = s; }
    public void setBankAccountNumberStd(String s) { msBankAccountNumberStd = s; }
    public void setAgreement(String s) { msAgreement = s; }
    public void setReference(String s) { msReference = s; }
    public void setCode(String s) { msCode = s; }
    public void setCodeAba(String s) { msCodeAba = s; }
    public void setCodeSwift(String s) { msCodeSwift = s; }
    public void setIsCardApplying(boolean b) { mbIsCardApplying = b; }
    public void setIsDefault(boolean b) { mbIsDefault = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkBankId(int n) { mnFkBankId = n; }
    public void setFkAccountCashCategoryId(int n) { mnFkAccountCashCategoryId = n; }
    public void setFkAccountCashTypeId(int n) { mnFkAccountCashTypeId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkCardIssuerId(int n) { mnFkCardIssuerId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(Date t) { mtUserNewTs = t; }
    public void setUserEditTs(Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(Date t) { mtUserDeleteTs = t; }

    public int getPkBizPartnerBranchId() { return mnPkBizPartnerBranchId; }
    public int getPkBankAccountId() { return mnPkBankAccountId; }
    public String getBankAccount() { return msBankAccount; }
    public String getBankAccountBranchNumber() { return msBankAccountBranchNumber; }
    public String getBankAccountNumber() { return msBankAccountNumber; }
    public String getBankAccountNumberStd() { return msBankAccountNumberStd; }
    public String getAgreement() { return msAgreement; }
    public String getReference() { return msReference; }
    public String getCode() { return msCode; }
    public String getCodeAba() { return msCodeAba; }
    public String getCodeSwift() { return msCodeSwift; }
    public boolean getIsCardApplying() { return mbIsCardApplying; }
    public boolean getIsDefault() { return mbIsDefault; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkBankId() { return mnFkBankId; }
    public int getFkAccountCashCategoryId() { return mnFkAccountCashCategoryId; }
    public int getFkAccountCashTypeId() { return mnFkAccountCashTypeId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkCardIssuerId() { return mnFkCardIssuerId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public Date getUserNewTs() { return mtUserNewTs; }
    public Date getUserEditTs() { return mtUserEditTs; }
    public Date getUserDeleteTs() { return mtUserDeleteTs; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBizPartnerBranchId = pk[0];
        mnPkBankAccountId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[]{mnPkBizPartnerBranchId, mnPkBankAccountId};
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBizPartnerBranchId = 0;
        mnPkBankAccountId = 0;
        msBankAccount = "";
        msBankAccountBranchNumber = "";
        msBankAccountNumber = "";
        msBankAccountNumberStd = "";
        msAgreement = "";
        msReference = "";
        msCode = "";
        msCodeAba = "";
        msCodeSwift = "";
        mbIsCardApplying = false;
        mbIsDefault = false;
        mbIsDeleted = false;
        mnFkBankId = 0;
        mnFkAccountCashCategoryId = 0;
        mnFkAccountCashTypeId = 0;
        mnFkCurrencyId = 0;
        mnFkCardIssuerId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bpb = " + mnPkBizPartnerBranchId + " AND "
                + "id_bank_acc = " + mnPkBankAccountId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bpb = " + pk[0] + " AND "
                + "id_bank_acc = " + pk[1] + " ";
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
        } else {
            mnPkBizPartnerBranchId = resultSet.getInt("id_bpb");
            mnPkBankAccountId = resultSet.getInt("id_bank_acc");
            msBankAccount = resultSet.getString("bank_acc");
            msBankAccountBranchNumber = resultSet.getString("bankb_num");
            msBankAccountNumber = resultSet.getString("acc_num");
            msBankAccountNumberStd = resultSet.getString("acc_num_std");
            msAgreement = resultSet.getString("agree");
            msReference = resultSet.getString("ref");
            msCode = resultSet.getString("code");
            msCodeAba = resultSet.getString("code_aba");
            msCodeSwift = resultSet.getString("code_swift");
            mbIsCardApplying = resultSet.getBoolean("b_card");
            mbIsDefault = resultSet.getBoolean("b_def");
            mbIsDeleted = resultSet.getBoolean("b_del");
            mnFkBankId = resultSet.getInt("fid_bank");
            mnFkAccountCashCategoryId = resultSet.getInt("fid_ct_acc_cash");
            mnFkAccountCashTypeId = resultSet.getInt("fid_tp_acc_cash");
            mnFkCurrencyId = resultSet.getInt("fid_cur");
            mnFkCardIssuerId = resultSet.getInt("fid_card_iss");
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
}
