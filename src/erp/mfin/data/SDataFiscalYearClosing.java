/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following class:
 * - erp.mfin.data.SDataRecordEntry
 * All of them also make raw SQL insertions.
 */

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDataFiscalYearClosing extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkPeriodId;
    protected int mnPkBookkeepingCenterId;
    protected java.lang.String msPkRecordTypeId;
    protected int mnPkNumberId;
    protected int mnFkCompanyBranchId;
    protected int mnFkUserId;
    protected int mnFkAccountSystemTypeId;
    protected int mnFkAccountTypeId_r;
    protected int mnFkAccountId;
    protected java.lang.String msAccountId;
    protected java.lang.String msAccountFormat;
    protected java.text.SimpleDateFormat moAuxSimpleDateFormat;
    protected int mnAuxEntryId;

    public SDataFiscalYearClosing() {
        super(SDataConstants.FIN_YEAR);
        reset();
    }

    private void processFiscalYearClosing(java.sql.Statement st, java.sql.Statement stUpd, java.lang.String sql) throws java.lang.Exception {
        double debit = 0;
        double credit = 0;
        double debitCur = 0;
        double creditCur = 0;
        double excRate = 0;
        int[] sysAccTypeKeyResults = null;
        int[] sysAccTypeKeyProfitLoss = null;
        int[] sysAccMoveTypeKeyProfitLoss = null;
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            if (rs.getDouble("f_bal") >= 0) {
                // loss (credits will be interchanged to debits to register loss):
                
                debit = 0;
                credit = rs.getDouble("f_bal");
                
                debitCur = 0;
                creditCur = rs.getDouble("f_bal_cur");
                
                sysAccTypeKeyResults = SModSysConsts.FINS_TP_SYS_MOV_YO_CDT; // to close results account
                sysAccTypeKeyProfitLoss = SModSysConsts.FINS_TP_SYS_MOV_YO_DBT; // to register profit & loss
                sysAccMoveTypeKeyProfitLoss = SDataConstantsSys.FINS_TP_SYS_MOV_PROF_LOSS;
            }
            else {
                // profit (debits will be interchanged to register profit):
                
                debit = -rs.getDouble("f_bal"); // make it positive
                credit = 0;
                
                debitCur = -rs.getDouble("f_bal_cur"); // make it positive
                creditCur = 0;
                
                sysAccTypeKeyResults = SModSysConsts.FINS_TP_SYS_MOV_YO_DBT; // to close results account
                sysAccTypeKeyProfitLoss = SModSysConsts.FINS_TP_SYS_MOV_YO_CDT; // to register profit & loss
                sysAccMoveTypeKeyProfitLoss = SDataConstantsSys.FINS_TP_SYS_MOV_PROF_PROF;
            }

            if (debit != 0 && debitCur != 0) {
                excRate = debit / debitCur;
            }
            else if (credit != 0 && creditCur != 0) {
                excRate = credit / creditCur;
            }
            else {
                excRate = 0;
            }

            // 1. Journal entry for closing current results account:

            mnAuxEntryId++;
            sql = "INSERT INTO fin_rec_ety VALUES (" +
                    "" + mnPkYearId + ", " + mnPkPeriodId + ", " + mnPkBookkeepingCenterId + ", '" + msPkRecordTypeId + "', " + mnPkNumberId + ", " + mnAuxEntryId + ", " +
                    "'CIERRE EJERCICIO " + mnPkYearId + "', " +
                    "'', 0, " +
                    "" + debit + ", " + credit + ", " + excRate + ", " + excRate + ", " + debitCur + ", " + creditCur + ", 0, 0, " + mnAuxEntryId + ", '', 0, 1, 0, " +
                    "'" + rs.getString("fid_acc") + "', " +
                    "" + rs.getInt("fk_acc") + ", " +
                    "" + (rs.getInt("fk_cc_n") == 0 ? "NULL" : "" + rs.getInt("fk_cc_n")) + ", " +
                    "" + SDataConstantsSys.FINS_CLS_ACC_MOV_FY_CLOSE[0] + ", " +
                    "" + SDataConstantsSys.FINS_CLS_ACC_MOV_FY_CLOSE[1] + ", " +
                    "" + SDataConstantsSys.FINS_CLS_ACC_MOV_FY_CLOSE[2] + ", " +
                    "" + sysAccTypeKeyResults[0] + ", " +
                    "" + sysAccTypeKeyResults[1] + ", " +
                    "" + rs.getInt("fid_cl_sys_acc") + ", " +
                    "" + rs.getInt("fid_tp_sys_acc") + ", " +
                    "" + rs.getInt("fid_ct_sys_mov_xxx") + ", " +
                    "" + rs.getInt("fid_tp_sys_mov_xxx") + ", " +
                    "" + rs.getInt("fid_cur") + ", " +
                    "" + (rs.getString("fid_cc_n") == null ? "NULL" : "'" + rs.getString("fid_cc_n") + "'") + ", " +
                    "NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, " +
                    "NULL, NULL, NULL, NULL, NULL, " +
                    "NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, " +
                    "" + (rs.getInt("fid_item_n") == 0 ? "NULL" : rs.getInt("fid_item_n")) + ", NULL, " +
                    "" + (rs.getInt("fid_unit_n") == 0 ? "NULL" : rs.getInt("fid_unit_n")) + ", " +
                    "NULL, NULL, " +
                    "" + mnFkUserId + ", " + mnFkUserId + ", " + mnFkUserId + ", NOW(), NOW(), NOW()); ";
            stUpd.execute(sql);

            // 1. Journal entry for registering the counterpart balance of current results account into given profit & loss account:

            mnAuxEntryId++;
            sql = "INSERT INTO fin_rec_ety VALUES (" +
                    "" + mnPkYearId + ", " + mnPkPeriodId + ", " + mnPkBookkeepingCenterId + ", '" + msPkRecordTypeId + "', " + mnPkNumberId + ", " + mnAuxEntryId + ", " +
                    "'CIERRE EJERCICIO " + mnPkYearId + "', " +
                    "'', 0, " +
                    "" + credit + ", " + debit + ", " + excRate + ", " + excRate + ", " + creditCur + ", " + debitCur + ", 0, 0, " + mnAuxEntryId + ", '', 0, 1, 0, " +
                    "'" + msAccountId + "', " +
                    "" + mnFkAccountId + ", " +
                    "NULL, " +  // no cost center needed
                    "" + SDataConstantsSys.FINS_CLS_ACC_MOV_FY_CLOSE[0] + ", " +
                    "" + SDataConstantsSys.FINS_CLS_ACC_MOV_FY_CLOSE[1] + ", " +
                    "" + SDataConstantsSys.FINS_CLS_ACC_MOV_FY_CLOSE[2] + ", " +
                    "" + sysAccTypeKeyProfitLoss[0] + ", " +
                    "" + sysAccTypeKeyProfitLoss[1] + ", " +
                    "" + rs.getInt("fid_cl_sys_acc") + ", " +
                    "" + rs.getInt("fid_tp_sys_acc") + ", " +
                    //"" + rs.getInt("fid_ct_sys_mov_xxx") + ", " +
                    //"" + rs.getInt("fid_tp_sys_mov_xxx") + ", " +
                    "" + sysAccMoveTypeKeyProfitLoss[0] + ", " +
                    "" + sysAccMoveTypeKeyProfitLoss[1] + ", " +
                    "" + rs.getInt("fid_cur") + ", " +
                    "NULL, " +  // no cost center needed
                    "NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, " +
                    "NULL, NULL, " + mnPkYearId + ", NULL, NULL, " +
                    "NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, " +
                    "" + (rs.getInt("fid_item_n") == 0 ? "NULL" : rs.getInt("fid_item_n")) + ", NULL, " +
                    "" + (rs.getInt("fid_unit_n") == 0 ? "NULL" : rs.getInt("fid_unit_n")) + ", " +
                    "NULL, NULL, " +
                    "" + mnFkUserId + ", " + mnFkUserId + ", " + mnFkUserId + ", NOW(), NOW(), NOW()); ";
            stUpd.execute(sql);
        }
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkPeriodId(int n) { mnPkPeriodId = n; }
    public void setPkBookkeepingCenterId(int n) { mnPkBookkeepingCenterId = n; }
    public void setPkRecordTypeId(java.lang.String s) { msPkRecordTypeId = s; }
    public void setPkNumberId(int n) { mnPkNumberId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setFkAccountSystemTypeId(int n) { mnFkAccountSystemTypeId = n; }
    public void setFkAccountTypeId_r(int n) { mnFkAccountTypeId_r = n; }
    public void setFkAccountId(int n) { mnFkAccountId = n; }
    public void setAccountId(java.lang.String s) { msAccountId = s; }
    public void setAccountFormat(java.lang.String s) { msAccountFormat = s; }
    public void setAuxSimpleDateFormat(java.text.SimpleDateFormat o) { moAuxSimpleDateFormat = o; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkPeriodId() { return mnPkPeriodId; }
    public int getPkBookkeepingCenterId() { return mnPkBookkeepingCenterId; }
    public java.lang.String getPkRecordTypeId() { return msPkRecordTypeId; }
    public int getPkNumberId() { return mnPkNumberId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkUserId() { return mnFkUserId; }
    public int getFkAccountSystemTypeId() { return mnFkAccountSystemTypeId; }
    public int getFkAccountTypeId_r() { return mnFkAccountTypeId_r; }
    public int getFkAccountId() { return mnFkAccountId; }
    public java.lang.String getAccountId() { return msAccountId; }
    public java.lang.String getAccountFormat() { return msAccountFormat; }
    public java.text.SimpleDateFormat getAuxSimpleDateFormat() { return moAuxSimpleDateFormat; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = (Integer) ((Object[]) pk)[0];
        mnPkPeriodId = (Integer) ((Object[]) pk)[1];
        mnPkBookkeepingCenterId = (Integer) ((Object[]) pk)[2];
        msPkRecordTypeId = (String) ((Object[]) pk)[3];
        mnPkNumberId = (Integer) ((Object[]) pk)[4];

        mnFkCompanyBranchId = 0;
        mnFkUserId = 0;
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new Object[] { mnPkYearId, mnPkPeriodId, mnPkBookkeepingCenterId, msPkRecordTypeId, mnPkNumberId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkPeriodId = 0;
        mnPkBookkeepingCenterId = 0;
        msPkRecordTypeId = "";
        mnPkNumberId = 0;
        mnAuxEntryId = 0;
        mnFkAccountSystemTypeId = 0;
        mnFkAccountTypeId_r = 0;
        mnFkAccountId = 0;
        msAccountId = "";
        msAccountFormat = "";
        moAuxSimpleDateFormat = null;
        mnAuxEntryId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        String sSql = "";
        Vector<Integer> levels = SDataUtilities.getAccountLevels(msAccountFormat);
        Statement stQry = null;
        Statement stUpd = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            stQry = connection.createStatement();
            stUpd = connection.createStatement();

            sSql = "DELETE FROM fin_rec_ety WHERE " +
                    "id_year = " + mnPkYearId + " AND id_per = " + mnPkPeriodId + " AND " +
                    "id_bkc = " + mnPkBookkeepingCenterId + " AND id_tp_rec = '" + msPkRecordTypeId + "' AND " +
                    "id_num = " + mnPkNumberId + " ";
            stUpd.execute(sSql);

            sSql = "DELETE FROM fin_rec WHERE " +
                    "id_year = " + mnPkYearId + " AND id_per = " + mnPkPeriodId + " AND " +
                    "id_bkc = " + mnPkBookkeepingCenterId + " AND id_tp_rec = '" + msPkRecordTypeId + "' AND " +
                    "id_num = " + mnPkNumberId + " ";
            stUpd.execute(sSql);

            sSql = "INSERT INTO fin_rec VALUES (" +
                    "" + mnPkYearId + ", " + mnPkPeriodId + ", " + mnPkBookkeepingCenterId + ", '" + msPkRecordTypeId + "', " + mnPkNumberId + ", " +
                    "'" + moAuxSimpleDateFormat.format(SLibTimeUtilities.createDate(mnPkYearId, 12, 31)) + "', " +
                    "'CIERRE EJERCICIO " + mnPkYearId + "', 1, 0, 0, 0, 1, 0, " + mnFkCompanyBranchId + ", NULL, NULL, 1, 1, " +
                    "" + mnFkUserId + ", " + mnFkUserId + ", " + mnFkUserId + ", NOW(), NOW(), NOW(), NOW(), NOW()); ";
            stUpd.execute(sSql);

            sSql = "SELECT re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                    "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                    "re.fid_item_n, re.fid_unit_n, " +
                    "SUM(re.debit - re.credit) AS f_bal, SUM(re.debit_cur - re.credit_cur) AS f_bal_cur " +
                    "FROM fin_rec AS r " +
                    "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND " +
                    "r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND r.id_year = " + mnPkYearId + " AND r.b_del = 0 AND re.b_del = 0 " +
                    "INNER JOIN fin_acc AS am ON CONCAT(LEFT(re.fid_acc, " + (levels.get(1) - 1) + "), '" + msAccountFormat.substring(levels.get(1) - 1) + "') = am.id_acc AND am.fid_tp_acc_r = " + SDataConstantsSys.FINS_TP_ACC_RES + " " +
                    "GROUP BY re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                    "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, re.fid_item_n, re.fid_unit_n " +
                    "HAVING f_bal <> 0 OR f_bal_cur <> 0 " +
                    "ORDER BY re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                    "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, re.fid_item_n, re.fid_unit_n ";

            processFiscalYearClosing(stQry, stUpd, sSql);

            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
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
        return null;
    }
}
