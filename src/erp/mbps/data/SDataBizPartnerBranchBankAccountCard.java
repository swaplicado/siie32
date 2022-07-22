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

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataBizPartnerBranchBankAccountCard extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkBizPartnerBranchId;
    protected int mnPkBankAccountId;
    protected int mnPkCardId;
    protected java.lang.String msHolder;
    protected java.lang.String msCardNumber;
    protected java.lang.String msSecurityCode;
    protected java.util.Date mtExpirationDate;
    protected boolean mbIsDeleted;
    protected int mnFkCardTypeId;

    protected java.lang.String msDbmsCardType;
    protected int mnDbmsFkBizPartnerId;

    public SDataBizPartnerBranchBankAccountCard() {
        super(SDataConstants.BPSU_BANK_ACC_CARD);
        reset();
    }

    public void setPkBizPartnerBranchId(int n) { mnPkBizPartnerBranchId = n; }
    public void setPkBankAccountId(int n) { mnPkBankAccountId = n; }
    public void setPkCardId(int n) { mnPkCardId = n; }
    public void setHolder(java.lang.String s) { msHolder = s; }
    public void setCardNumber(java.lang.String s) { msCardNumber = s; }
    public void setSecurityCode(java.lang.String s) { msSecurityCode = s; }
    public void setExpirationDate(java.util.Date t) { mtExpirationDate = t; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkCardTypeId(int n) { mnFkCardTypeId = n; }

    public int getPkBizPartnerBranchId() { return mnPkBizPartnerBranchId; }
    public int getPkBankAccountId() { return mnPkBankAccountId; }
    public int getPkCardId() { return mnPkCardId; }
    public java.lang.String getHolder() { return msHolder; }
    public java.lang.String getCardNumber() { return msCardNumber; }
    public java.lang.String getSecurityCode() { return msSecurityCode; }
    public java.util.Date getExpirationDate() { return mtExpirationDate; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkCardTypeId() { return mnFkCardTypeId; }

    public void setDbmsCardType(java.lang.String s) { msDbmsCardType = s; }

    public java.lang.String getDbmsCardType() { return msDbmsCardType; }
    public int getDbmsFkBizPartnerId() { return mnDbmsFkBizPartnerId; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBizPartnerBranchId = ((int[]) pk)[0];
        mnPkBankAccountId= ((int[]) pk)[1];
        mnPkCardId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkBizPartnerBranchId, mnPkBankAccountId, mnPkCardId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkBizPartnerBranchId = 0;
        mnPkBankAccountId = 0;
        mnPkCardId = 0;
        msHolder = "";
        msCardNumber = "";
        msSecurityCode = "";
        mtExpirationDate = null;
        mbIsDeleted = false;
        mnFkCardTypeId = 0;

        msDbmsCardType = "";
        mnDbmsFkBizPartnerId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT bank_acc_card.*, bpb.fid_bp, tp_card.tp_card " +
                    "FROM erp.bpsu_bank_acc_card AS bank_acc_card " +
                    "INNER JOIN erp.bpsu_bpb AS bpb ON " +
                    "bank_acc_card.id_bpb = bpb.id_bpb " +
                    "INNER JOIN erp.fins_tp_card AS tp_card ON " +
                    "bank_acc_card.fid_tp_card = tp_card.id_tp_card " +
                    "WHERE bank_acc_card.id_bpb = " + key[0] + " AND bank_acc_card.id_bank_acc = " + key[1] + " AND bank_acc_card.id_card = " + key[2] + ";";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBizPartnerBranchId = resultSet.getInt("bank_acc_card.id_bpb");
                mnPkBankAccountId = resultSet.getInt("bank_acc_card.id_bank_acc");
                mnPkCardId = resultSet.getInt("bank_acc_card.id_card");
                msHolder = resultSet.getString("bank_acc_card.holder");
                msCardNumber = resultSet.getString("bank_acc_card.card_num");
                msSecurityCode = resultSet.getString("bank_acc_card.sec_code");
                mtExpirationDate = resultSet.getDate("bank_acc_card.exp_date");
                mbIsDeleted = resultSet.getBoolean("bank_acc_card.b_del");
                mnFkCardTypeId = resultSet.getInt("bank_acc_card.fid_tp_card");

                mnDbmsFkBizPartnerId = resultSet.getInt("bpb.fid_bp");
                msDbmsCardType = resultSet.getString("tp_card.tp_card");

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
            callableStatement = connection.prepareCall(
                    "{ CALL erp.bpsu_bank_acc_card_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?) }");
            callableStatement.setInt(nParam++, mnPkBizPartnerBranchId);
            callableStatement.setInt(nParam++, mnPkBankAccountId);
            callableStatement.setInt(nParam++, mnPkCardId);
            callableStatement.setString(nParam++, msHolder);
            callableStatement.setString(nParam++, msCardNumber);
            callableStatement.setString(nParam++, msSecurityCode);
            callableStatement.setDate(nParam++, new java.sql.Date(mtExpirationDate.getTime()));
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkCardTypeId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkCardId = callableStatement.getInt(nParam - 3);
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
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return null;
    }
}
