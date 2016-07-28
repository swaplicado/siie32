/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mbps.data.SDataBizPartnerBranchBankAccount;
import erp.mcfg.data.SDataCompanyBranchEntity;

/**
 *
 * @author Sergio Flores
 */
public class SDataAccountCash extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkCompanyBranchId;
    protected int mnPkAccountCashId;
    protected boolean mbIsCheckWalletApplying;
    protected boolean mbIsDeleted;
    protected java.lang.String msFkAccountId;
    protected int mnFkAccountCashCategoryId;
    protected int mnFkAccountCashTypeId;
    protected int mnFkBizPartnerBranchId_n;
    protected int mnFkBankAccountId_n;
    protected int mnFkCheckFormatId_n;
    protected int mnFkCheckFormatGpId_n;
    protected int mnFkCurrencyId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected erp.mcfg.data.SDataCompanyBranchEntity moDbmsCompanyBranchEntity;
    protected erp.mbps.data.SDataBizPartnerBranchBankAccount moDbmsBizPartnerBranchBankAccount;

    protected int mnAuxFkCompanyId;
    protected java.lang.String msAuxEntity;
    protected java.lang.String msAuxCode;
    
    protected int mnAuxFkBankId;
    protected java.lang.String msAuxBankAccountNumber;
    
    protected boolean mbAuxIsEntityActive;

    public SDataAccountCash() {
        super(SDataConstants.FIN_ACC_CASH);
        reset();
    }

    public void setPkCompanyBranchId(int n) { mnPkCompanyBranchId = n; }
    public void setPkAccountCashId(int n) { mnPkAccountCashId = n; }
    public void setIsCheckWalletApplying(boolean b) { mbIsCheckWalletApplying = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkAccountId(java.lang.String s) { msFkAccountId = s; }
    public void setFkAccountCashCategoryId(int n) { mnFkAccountCashCategoryId = n; }
    public void setFkAccountCashTypeId(int n) { mnFkAccountCashTypeId = n; }
    public void setFkBizPartnerBranchId_n(int n) { mnFkBizPartnerBranchId_n = n; }
    public void setFkBankAccountId_n(int n) { mnFkBankAccountId_n = n; }
    public void setFkCheckFormatId_n(int n) { mnFkCheckFormatId_n = n; }
    public void setFkCheckFormatGpId_n(int n) { mnFkCheckFormatGpId_n = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkCompanyBranchId() { return mnPkCompanyBranchId; }
    public int getPkAccountCashId() { return mnPkAccountCashId; }
    public boolean getIsCheckWalletApplying() { return mbIsCheckWalletApplying; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public java.lang.String getFkAccountId() { return msFkAccountId; }
    public int getFkAccountCashCategoryId() { return mnFkAccountCashCategoryId; }
    public int getFkAccountCashTypeId() { return mnFkAccountCashTypeId; }
    public int getFkBizPartnerBranchId_n() { return mnFkBizPartnerBranchId_n; }
    public int getFkBankAccountId_n() { return mnFkBankAccountId_n; }
    public int getFkCheckFormatId_n() { return mnFkCheckFormatId_n; }
    public int getFkCheckFormatGpId_n() { return mnFkCheckFormatGpId_n; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    public erp.mcfg.data.SDataCompanyBranchEntity getDbmsCompanyBranchEntity() { return moDbmsCompanyBranchEntity; }
    public erp.mbps.data.SDataBizPartnerBranchBankAccount getDbmsBizPartnerBranchBankAccount() { return moDbmsBizPartnerBranchBankAccount; }

    public void setAuxFkCompanyId(int n) { mnAuxFkCompanyId = n; }
    public void setAuxEntity(java.lang.String s) { msAuxEntity = s; }
    public void setAuxCode(java.lang.String s) { msAuxCode = s; }
    
    public void setAuxFkBankId(int n) { mnAuxFkBankId = n; }
    public void setAuxBankAccountNumber(java.lang.String s) { msAuxBankAccountNumber = s; }
    public void setAuxIsEntityActive(boolean b) { mbAuxIsEntityActive = b; }

    public int getAuxFkCompanyId() { return mnAuxFkCompanyId; }
    public java.lang.String getAuxEntity() { return msAuxEntity; }
    public java.lang.String getAuxCode() { return msAuxCode; }

    public int getAuxFkBankId() { return mnAuxFkBankId; }
    public java.lang.String getAuxBankAccountNumber() { return msAuxBankAccountNumber; }
    public boolean getAuxIsEntityActive() { return mbAuxIsEntityActive; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkCompanyBranchId = ((int[]) pk)[0];
        mnPkAccountCashId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkCompanyBranchId, mnPkAccountCashId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkCompanyBranchId = 0;
        mnPkAccountCashId = 0;
        mbIsCheckWalletApplying = false;
        mbIsDeleted = false;
        msFkAccountId = "";
        mnFkAccountCashCategoryId = 0;
        mnFkAccountCashTypeId = 0;
        mnFkBizPartnerBranchId_n = 0;
        mnFkBankAccountId_n = 0;
        mnFkCheckFormatId_n = 0;
        mnFkCheckFormatGpId_n = 0;
        mnFkCurrencyId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        moDbmsCompanyBranchEntity = null;
        moDbmsBizPartnerBranchBankAccount = null;

        mnAuxFkCompanyId = 0;
        msAuxEntity = "";
        msAuxCode = "";
        
        mnAuxFkBankId = 0;
        msAuxBankAccountNumber = "";
        mbAuxIsEntityActive = false;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM fin_acc_cash WHERE id_cob = " + key[0] + " AND id_acc_cash = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkCompanyBranchId = resultSet.getInt("id_cob");
                mnPkAccountCashId = resultSet.getInt("id_acc_cash");
                mbIsCheckWalletApplying = resultSet.getBoolean("b_check_wal");
                mbIsDeleted = resultSet.getBoolean("b_del");
                msFkAccountId = resultSet.getString("fid_acc");
                mnFkAccountCashCategoryId = resultSet.getInt("fid_ct_acc_cash");
                mnFkAccountCashTypeId = resultSet.getInt("fid_tp_acc_cash");
                mnFkBizPartnerBranchId_n = resultSet.getInt("fid_bpb_n");
                if (resultSet.wasNull()) mnFkBizPartnerBranchId_n = 0;
                mnFkBankAccountId_n = resultSet.getInt("fid_bank_acc_n");
                if (resultSet.wasNull()) mnFkBankAccountId_n = 0;
                mnFkCheckFormatId_n = resultSet.getInt("fid_check_fmt_n");
                if (resultSet.wasNull()) mnFkCheckFormatId_n = 0;
                mnFkCheckFormatGpId_n = resultSet.getInt("fid_check_fmt_gp_n");
                if (resultSet.wasNull()) mnFkCheckFormatGpId_n = 0;
                mnFkCurrencyId = resultSet.getInt("fid_cur");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                moDbmsCompanyBranchEntity = new SDataCompanyBranchEntity();
                if (moDbmsCompanyBranchEntity.read(getPrimaryKey(), statement) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
                }

                msAuxEntity = moDbmsCompanyBranchEntity.getEntity();
                msAuxCode = moDbmsCompanyBranchEntity.getCode();
                mbAuxIsEntityActive = moDbmsCompanyBranchEntity.getIsActive();
                
                if (mnFkBankAccountId_n != 0) {
                    moDbmsBizPartnerBranchBankAccount = new SDataBizPartnerBranchBankAccount();
                    if (moDbmsBizPartnerBranchBankAccount.read(new int[] { mnPkCompanyBranchId, mnFkBankAccountId_n }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
                    }

                    mnAuxFkBankId = moDbmsBizPartnerBranchBankAccount.getFkBankId();
                    msAuxBankAccountNumber = moDbmsBizPartnerBranchBankAccount.getBankAccountNumber();
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
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbIsRegistryNew) {
                moDbmsCompanyBranchEntity = new SDataCompanyBranchEntity();
                moDbmsCompanyBranchEntity.setPkCompanyBranchId(mnPkCompanyBranchId);
                moDbmsCompanyBranchEntity.setPkEntityId(0);
                moDbmsCompanyBranchEntity.setEntity(msAuxEntity);
                moDbmsCompanyBranchEntity.setCode(msAuxCode);
                moDbmsCompanyBranchEntity.setIsActive(mbAuxIsEntityActive);
                //moDbmsCompanyBranchEntity.setIsActive(true);
                moDbmsCompanyBranchEntity.setIsActiveIn(true);
                moDbmsCompanyBranchEntity.setIsActiveOut(true);
                moDbmsCompanyBranchEntity.setIsDeleted(false);
                moDbmsCompanyBranchEntity.setFkCompanyId(mnAuxFkCompanyId);
                moDbmsCompanyBranchEntity.setFkEntityCategoryId(SDataConstantsSys.CFGS_CT_ENT_CASH);
                moDbmsCompanyBranchEntity.setFkEntityTypeId(mnFkAccountCashCategoryId == SDataConstantsSys.FINS_CT_ACC_CASH_CASH ? SDataConstantsSys.CFGS_TP_ENT_CASH_CASH[1] : SDataConstantsSys.CFGS_TP_ENT_CASH_BANK[1]);
                moDbmsCompanyBranchEntity.setFkUserNewId(mnFkUserNewId);
            }
            else {
                moDbmsCompanyBranchEntity.setEntity(msAuxEntity);
                moDbmsCompanyBranchEntity.setCode(msAuxCode);
                moDbmsCompanyBranchEntity.setIsActive(mbAuxIsEntityActive);
                moDbmsCompanyBranchEntity.setIsDeleted(mbIsDeleted);
                moDbmsCompanyBranchEntity.setFkUserEditId(mnFkUserEditId);
            }

            if (moDbmsCompanyBranchEntity.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE);
            }
            else {
                if (mnFkAccountCashCategoryId != SDataConstantsSys.FINS_CT_ACC_CASH_CASH) {
                    if (mbIsRegistryNew) {
                        moDbmsBizPartnerBranchBankAccount = new SDataBizPartnerBranchBankAccount();
                        moDbmsBizPartnerBranchBankAccount.setPkBizPartnerBranchId(mnPkCompanyBranchId);
                        moDbmsBizPartnerBranchBankAccount.setPkBankAccountId(0);
                        moDbmsBizPartnerBranchBankAccount.setFkAccountCashCategoryId(mnFkAccountCashCategoryId);
                        moDbmsBizPartnerBranchBankAccount.setFkAccountCashTypeId(mnFkAccountCashTypeId);
                        moDbmsBizPartnerBranchBankAccount.setFkBankId(mnAuxFkBankId);
                        moDbmsBizPartnerBranchBankAccount.setFkCurrencyId(mnFkCurrencyId);
                        moDbmsBizPartnerBranchBankAccount.setBankAccount(msAuxEntity);
                        moDbmsBizPartnerBranchBankAccount.setBankAccountNumber(msAuxBankAccountNumber);
                        moDbmsBizPartnerBranchBankAccount.setFkCardIssuerId(SDataConstantsSys.NA);
                        moDbmsBizPartnerBranchBankAccount.setIsDeleted(false);
                        moDbmsBizPartnerBranchBankAccount.setFkUserNewId(mnFkUserNewId);
                    }
                    else {
                        moDbmsBizPartnerBranchBankAccount.setFkAccountCashCategoryId(mnFkAccountCashCategoryId);
                        moDbmsBizPartnerBranchBankAccount.setFkAccountCashTypeId(mnFkAccountCashTypeId);
                        moDbmsBizPartnerBranchBankAccount.setFkBankId(mnAuxFkBankId);
                        moDbmsBizPartnerBranchBankAccount.setFkCurrencyId(mnFkCurrencyId);
                        moDbmsBizPartnerBranchBankAccount.setBankAccount(msAuxEntity);
                        moDbmsBizPartnerBranchBankAccount.setBankAccountNumber(msAuxBankAccountNumber);
                        moDbmsBizPartnerBranchBankAccount.setIsDeleted(mbIsDeleted);
                        moDbmsBizPartnerBranchBankAccount.setFkUserEditId(mnFkUserEditId);
                    }

                    if (moDbmsBizPartnerBranchBankAccount.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE);
                    }
                    else {
                        if (mbIsRegistryNew) {
                            mnFkBizPartnerBranchId_n = moDbmsBizPartnerBranchBankAccount.getPkBizPartnerBranchId();
                            mnFkBankAccountId_n = moDbmsBizPartnerBranchBankAccount.getPkBankAccountId();
                        }
                    }
                }
                
                if (mbIsRegistryNew) {
                    mnPkCompanyBranchId = moDbmsCompanyBranchEntity.getPkCompanyBranchId();
                    mnPkAccountCashId = moDbmsCompanyBranchEntity.getPkEntityId();
                }

                callableStatement = connection.prepareCall(
                        "{ CALL fin_acc_cash_save(" +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                        "?, ?, ?, ?, ?) }");
                callableStatement.setInt(nParam++, mnPkCompanyBranchId);
                callableStatement.setInt(nParam++, mnPkAccountCashId);
                callableStatement.setBoolean(nParam++, mbIsCheckWalletApplying);
                callableStatement.setBoolean(nParam++, mbIsDeleted);
                callableStatement.setString(nParam++, msFkAccountId);
                callableStatement.setInt(nParam++, mnFkAccountCashCategoryId);
                callableStatement.setInt(nParam++, mnFkAccountCashTypeId);
                if (mnFkBizPartnerBranchId_n > 0) callableStatement.setInt(nParam++, mnFkBizPartnerBranchId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
                if (mnFkBankAccountId_n > 0) callableStatement.setInt(nParam++, mnFkBankAccountId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
                if (mnFkCheckFormatId_n > 0) callableStatement.setInt(nParam++, mnFkCheckFormatId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
                if (mnFkCheckFormatGpId_n > 0) callableStatement.setInt(nParam++, mnFkCheckFormatGpId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
                callableStatement.setInt(nParam++, mnFkCurrencyId);
                callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
                callableStatement.execute();

                mnDbmsErrorId = callableStatement.getInt(nParam - 2);
                msDbmsError = callableStatement.getString(nParam - 1);

                if (mnDbmsErrorId != 0) {
                    throw new Exception(msDbmsError);
                }
                else {
                    mbIsRegistryNew = false;
                    mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
                }
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
}
