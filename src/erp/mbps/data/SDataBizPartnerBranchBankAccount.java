/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataBizPartnerBranchBankAccount extends erp.lib.data.SDataRegistry implements java.io.Serializable{
    
    public static final int ACCOUNT_NUMBER_VISIBLE_RIGHT_LEN = 4;

    protected int mnPkBizPartnerBranchId;
    protected int mnPkBankAccountId;
    protected java.lang.String msBankAccount;
    protected java.lang.String msBankAccountBranchNumber;
    protected java.lang.String msBankAccountNumber;
    protected java.lang.String msBankAccountNumberStd;
    protected java.lang.String msAgree;
    protected java.lang.String msReference;
    protected java.lang.String msCode;
    protected java.lang.String msCodeAba;
    protected java.lang.String msCodeSwift;
    protected java.lang.String msAliasBajio;
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
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsAccountCashType;
    protected java.lang.String msDbmsBank;
    protected java.lang.String msDbmsCurrencyKey;
    protected java.lang.String msDbmsCardIssuer;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;
    protected int mnDbmsFkBizPartnerId;
    protected java.util.Vector<erp.mbps.data.SDataBizPartnerBranchBankAccountCard> mvDbmsBankAccountCards;
    protected java.util.Vector<erp.mbps.data.SDataBizPartnerBranchBankAccountLayoutBank> mvDbmsBankAccountLayoutBank;

    public SDataBizPartnerBranchBankAccount() {
        super(SDataConstants.BPSU_BANK_ACC);
        mvDbmsBankAccountCards = new Vector<>();
        mvDbmsBankAccountLayoutBank = new Vector<>();
        reset();
    }

    public void setPkBizPartnerBranchId(int n) { mnPkBizPartnerBranchId = n; }
    public void setPkBankAccountId(int n) { mnPkBankAccountId = n; }
    public void setBankAccount(java.lang.String s) { msBankAccount = s; }
    public void setBankAccountBranchNumber(java.lang.String s) { msBankAccountBranchNumber = s; }
    public void setBankAccountNumber(java.lang.String s) { msBankAccountNumber = s; }
    public void setBankAccountNumberStd(java.lang.String s) { msBankAccountNumberStd = s; }
    public void setAgree(java.lang.String s) { msAgree = s; }
    public void setReference(java.lang.String s) { msReference = s; }
    public void setCode(java.lang.String s) { msCode = s; }
    public void setCodeAba(java.lang.String s) { msCodeAba = s; }
    public void setCodeSwift(java.lang.String s) { msCodeSwift = s; }
    public void setAliasBajio(java.lang.String s) { msAliasBajio = s; }
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
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkBizPartnerBranchId() { return mnPkBizPartnerBranchId; }
    public int getPkBankAccountId() { return mnPkBankAccountId; }
    public java.lang.String getBankAccount() { return msBankAccount; }
    public java.lang.String getBankAccountBranchNumber() { return msBankAccountBranchNumber; }
    public java.lang.String getBankAccountNumber() { return msBankAccountNumber; }
    public java.lang.String getBankAccountNumberStd() { return msBankAccountNumberStd; }
    public java.lang.String getAgree() { return msAgree; }
    public java.lang.String getReference() { return msReference; }
    public java.lang.String getCode() { return msCode; }
    public java.lang.String getCodeAba() { return msCodeAba; }
    public java.lang.String getCodeSwift() { return msCodeSwift; }
    public java.lang.String getAliasBajio() { return msAliasBajio; }
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
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsAccountCashType(java.lang.String s) { msDbmsAccountCashType = s; }
    public void setDbmsBank(java.lang.String s) { msDbmsBank = s; }
    public void setDbmsCurrencyKey(java.lang.String s) { msDbmsCurrencyKey = s; }
    public void setDbmsCardIssuer(java.lang.String s) { msDbmsCardIssuer = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public int getDbmsPkBizPartnerId() { return mnDbmsFkBizPartnerId; }
    public java.lang.String getDbmsAccountCashType() { return msDbmsAccountCashType; }
    public java.lang.String getDbmsBank() { return msDbmsBank; }
    public java.lang.String getDbmsCurrencyKey() { return msDbmsCurrencyKey; }
    public java.lang.String getDbmsCardIssuer() { return msDbmsCardIssuer; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }

    public java.util.Vector <SDataBizPartnerBranchBankAccountCard> getDbmsBankAccountCards() { return mvDbmsBankAccountCards; }
    public java.util.Vector <SDataBizPartnerBranchBankAccountLayoutBank> getDbmsBankAccountLayoutBank() { return mvDbmsBankAccountLayoutBank; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBizPartnerBranchId = ((int[]) pk)[0];
        mnPkBankAccountId= ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkBizPartnerBranchId, mnPkBankAccountId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkBizPartnerBranchId = 0;
        mnPkBankAccountId = 0;
        msBankAccount = "";
        msBankAccountBranchNumber = "";
        msBankAccountNumber = "";
        msBankAccountNumberStd = "";
        msAgree = "";
        msReference = "";
        msCode = "";
        msCodeAba = "";
        msCodeSwift = "";
        msAliasBajio = "";
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

        mnDbmsFkBizPartnerId = 0;
        mvDbmsBankAccountCards.clear();
        mvDbmsBankAccountLayoutBank.clear();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;
        Statement statementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT bank_acc.*, bpb.fid_bp, tp.tp_acc_cash, bp.bp_comm, cur.cur_key, iss.card_iss, un.usr, ue.usr, ud.usr " +
                    "FROM erp.bpsu_bank_acc AS bank_acc " +
                    "INNER JOIN erp.bpsu_bpb AS bpb ON bank_acc.id_bpb = bpb.id_bpb " +
                    "INNER JOIN erp.fins_tp_acc_cash AS tp ON bank_acc.fid_ct_acc_cash = tp.id_ct_acc_cash AND bank_acc.fid_tp_acc_cash = tp.id_tp_acc_cash " +
                    "INNER JOIN erp.bpsu_bp AS bp ON bank_acc.fid_bank = bp.id_bp " +
                    "INNER JOIN erp.cfgu_cur AS cur ON bank_acc.fid_cur = cur.id_cur " +
                    "INNER JOIN erp.finu_card_iss AS iss ON bank_acc.fid_card_iss = iss.id_card_iss " +
                    "INNER JOIN erp.usru_usr AS un ON bank_acc.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON bank_acc.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON bank_acc.fid_usr_del = ud.id_usr " +
                    "WHERE bank_acc.id_bpb = " + key[0] + " AND bank_acc.id_bank_acc = " + key[1] + ";";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBizPartnerBranchId = resultSet.getInt("bank_acc.id_bpb");
                mnPkBankAccountId = resultSet.getInt("bank_acc.id_bank_acc");
                msBankAccount = resultSet.getString("bank_acc.bank_acc");
                msBankAccountBranchNumber = resultSet.getString("bank_acc.bankb_num");
                msBankAccountNumber = resultSet.getString("bank_acc.acc_num");
                msBankAccountNumberStd = resultSet.getString("bank_acc.acc_num_std");
                msAgree = resultSet.getString("bank_acc.agree");
                msReference = resultSet.getString("bank_acc.ref");
                msCode = resultSet.getString("bank_acc.code");
                msCodeAba = resultSet.getString("bank_acc.code_aba");
                msCodeSwift = resultSet.getString("bank_acc.code_swift");
                msAliasBajio = resultSet.getString("bank_acc.alias_baj");
                mbIsCardApplying = resultSet.getBoolean("bank_acc.b_card");
                mbIsDefault = resultSet.getBoolean("bank_acc.b_def");
                mbIsDeleted = resultSet.getBoolean("bank_acc.b_del");
                mnFkBankId = resultSet.getInt("bank_acc.fid_bank");
                mnFkAccountCashCategoryId = resultSet.getInt("bank_acc.fid_ct_acc_cash");
                mnFkAccountCashTypeId = resultSet.getInt("bank_acc.fid_tp_acc_cash");
                mnFkCurrencyId = resultSet.getInt("bank_acc.fid_cur");
                mnFkCardIssuerId = resultSet.getInt("bank_acc.fid_card_iss");
                mnFkUserNewId = resultSet.getInt("bank_acc.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("bank_acc.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("bank_acc.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("bank_acc.ts_new");
                mtUserEditTs = resultSet.getTimestamp("bank_acc.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("bank_acc.ts_del");

                mnDbmsFkBizPartnerId = resultSet.getInt("bpb.fid_bp");
                msDbmsAccountCashType = resultSet.getString("tp.tp_acc_cash");
                msDbmsBank = resultSet.getString("bp.bp_comm");
                msDbmsCurrencyKey = resultSet.getString("cur.cur_key");
                msDbmsCardIssuer = resultSet.getString("iss.card_iss");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

                statementAux = statement.getConnection().createStatement();

                // Read aswell bank account cards:

                sql = "SELECT id_card FROM erp.bpsu_bank_acc_card WHERE id_bpb = " + mnPkBizPartnerBranchId + " AND id_bank_acc = " + mnPkBankAccountId + " ORDER BY id_card;";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    erp.mbps.data.SDataBizPartnerBranchBankAccountCard card = new SDataBizPartnerBranchBankAccountCard();
                    if (card.read(new int[] { mnPkBizPartnerBranchId, mnPkBankAccountId, resultSet.getInt("id_card") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBankAccountCards.add(card);
                    }
                }
                
                // Read aswell bank account layout type:

                sql = "SELECT id_tp_lay_bank FROM erp.bpsu_bank_acc_lay_bank WHERE id_bpb = " + mnPkBizPartnerBranchId + " AND id_bank_acc = " + mnPkBankAccountId + " ORDER BY id_tp_lay_bank;";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    erp.mbps.data.SDataBizPartnerBranchBankAccountLayoutBank layout = new SDataBizPartnerBranchBankAccountLayoutBank();
                    if (layout.read(new int[] { mnPkBizPartnerBranchId, mnPkBankAccountId, resultSet.getInt("id_tp_lay_bank") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBankAccountLayoutBank.add(layout);
                    }
                }

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
    public int save(java.sql.Connection connection) {
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            int userId = mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId;
            
            callableStatement = connection.prepareCall(
                    "{ CALL erp.bpsu_bank_acc_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkBizPartnerBranchId);
            callableStatement.setInt(nParam++, mnPkBankAccountId);
            callableStatement.setString(nParam++, msBankAccount);
            callableStatement.setString(nParam++, msBankAccountBranchNumber);
            callableStatement.setString(nParam++, msBankAccountNumber);
            callableStatement.setString(nParam++, msBankAccountNumberStd);
            callableStatement.setString(nParam++, msAgree);
            callableStatement.setString(nParam++, msReference);
            callableStatement.setString(nParam++, msCode);
            callableStatement.setString(nParam++, msCodeAba);
            callableStatement.setString(nParam++, msCodeSwift);
            callableStatement.setString(nParam++, msAliasBajio);
            callableStatement.setBoolean(nParam++, mbIsCardApplying);
            callableStatement.setBoolean(nParam++, mbIsDefault);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkBankId);
            callableStatement.setInt(nParam++, mnFkAccountCashCategoryId);
            callableStatement.setInt(nParam++, mnFkAccountCashTypeId);
            callableStatement.setInt(nParam++, mnFkCurrencyId);
            callableStatement.setInt(nParam++, mnFkCardIssuerId);
            callableStatement.setInt(nParam++, userId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkBankAccountId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                // Save aswell cards:

                for (SDataBizPartnerBranchBankAccountCard card : mvDbmsBankAccountCards) {
                    // save only new or edited cards:
                    
                    if (card.getIsRegistryNew() || card.getIsRegistryEdited()) {
                        card.setPkBizPartnerBranchId(mnPkBizPartnerBranchId);
                        card.setPkBankAccountId(mnPkBankAccountId);
                        
                        if (card.getIsRegistryNew()) {
                            card.setFkUserNewId(userId);
                        }
                        else {
                            card.setFkUserEditId(userId);
                        }
                        
                        if (card.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }
                
                // Save aswell bank layouts:
                
                String sql = "DELETE FROM erp.bpsu_bank_acc_lay_bank "
                        + "WHERE id_bpb = " + mnPkBizPartnerBranchId + " AND id_bank_acc = " + mnPkBankAccountId + ";"; // first delete all existing bank layouts
                connection.createStatement().execute(sql);
                
                for (SDataBizPartnerBranchBankAccountLayoutBank layoutBank : mvDbmsBankAccountLayoutBank) {
                    // save all bank layout:
                    
                    layoutBank.setPkBizPartnerBranchId(mnPkBizPartnerBranchId);
                    layoutBank.setPkBankAccountId(mnPkBankAccountId);
                    layoutBank.setIsRegistryNew(true);
                    
                    if (layoutBank.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }

    public SDataBizPartnerBranchBankAccountCard getDbmsBranchBankAccountCard(int[] pk) {
        SDataBizPartnerBranchBankAccountCard card = null;

        for (int i = 0; i < mvDbmsBankAccountCards.size(); i++) {
            if (SLibUtilities.compareKeys(pk, mvDbmsBankAccountCards.get(i).getPrimaryKey())) {
                card = mvDbmsBankAccountCards.get(i);
                break;
            }
        }

        return card;
    }
}
