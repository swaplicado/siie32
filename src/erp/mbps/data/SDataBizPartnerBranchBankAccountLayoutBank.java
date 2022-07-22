/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDataBizPartnerBranchBankAccountLayoutBank extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkBizPartnerBranchId;
    protected int mnPkBankAccountId;
    protected int mnPkBankLayoutTypeId;
    
    public SDataBizPartnerBranchBankAccountLayoutBank() {
        super(SDataConstants.BPSU_BANK_ACC_LAY_BANK);
        reset();
    }

    public void setPkBizPartnerBranchId(int n) { mnPkBizPartnerBranchId = n; }
    public void setPkBankAccountId(int n) { mnPkBankAccountId = n; }
    public void setPkBankLayoutTypeId(int n) { mnPkBankLayoutTypeId = n; }

    public int getPkBizPartnerBranchId() { return mnPkBizPartnerBranchId; }
    public int getPkBankAccountId() { return mnPkBankAccountId; }
    public int getPkBankLayoutTypeId() { return mnPkBankLayoutTypeId; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBizPartnerBranchId = ((int[]) pk)[0];
        mnPkBankAccountId= ((int[]) pk)[1];
        mnPkBankLayoutTypeId= ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkBizPartnerBranchId, mnPkBankAccountId, mnPkBankLayoutTypeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkBizPartnerBranchId = 0;
        mnPkBankAccountId = 0;
        mnPkBankLayoutTypeId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * " +
                    "FROM erp.bpsu_bank_acc_lay_bank " +
                    "WHERE id_bpb = " + key[0] + " AND id_bank_acc = " + key[1] + " AND id_tp_lay_bank = " + key[2] + ";";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBizPartnerBranchId = resultSet.getInt("id_bpb");
                mnPkBankAccountId = resultSet.getInt("id_bank_acc");
                mnPkBankLayoutTypeId = resultSet.getInt("id_tp_lay_bank");
                
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
        String sql = "";
        Statement statement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            statement = connection.createStatement();

            sql = "INSERT INTO erp.bpsu_bank_acc_lay_bank ("
                    + "id_bpb, id_bank_acc, id_tp_lay_bank) " +
                    "VALUES ("
                    + mnPkBizPartnerBranchId + ", " + mnPkBankAccountId + ", " + mnPkBankLayoutTypeId + ")";
            statement.execute(sql);

            mnDbmsErrorId = 0;
            msDbmsError = "";

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }
    
    @Override
    public int delete(Connection connection) {
        String sql = "";
        Statement statement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            statement = connection.createStatement();

            sql = "DELETE FROM erp.bpsu_bank_acc_lay_bank "
                    + "WHERE id_bpb = " + mnPkBizPartnerBranchId + " AND id_bank_acc = " + mnPkBankAccountId + " ";
            statement.execute(sql);

            mnDbmsErrorId = 0;
            msDbmsError = "";

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
