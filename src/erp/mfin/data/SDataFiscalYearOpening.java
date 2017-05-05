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

/**
 *
 * @author Sergio Flores
 */
public class SDataFiscalYearOpening extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkPeriodId;
    protected int mnPkBookkeepingCenterId;
    protected java.lang.String msPkRecordTypeId;
    protected int mnPkNumberId;
    protected int mnPkEntryId;
    protected int mnFkCompanyBranchId;
    protected int mnFkUserId;

    protected java.text.SimpleDateFormat moAuxSimpleDateFormat;

    public SDataFiscalYearOpening() {
        super(SDataConstants.FIN_YEAR);
        reset();
    }

    private void processFiscalYearClosing(java.sql.Statement st, java.sql.Statement stUpd, java.lang.String sql) throws java.lang.Exception {
        double debit = 0;
        double credit = 0;
        double debitCur = 0;
        double creditCur = 0;
        double excRate = 0;
        int[] sysAccountTypeKey = null;
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            mnPkEntryId++;

            if (rs.getDouble("f_bal") >= 0) {
                debit = rs.getDouble("f_bal");
                credit = 0;
                sysAccountTypeKey = SModSysConsts.FINS_TP_SYS_MOV_YO_DBT;
            }
            else {
                debit = 0;
                credit = - rs.getDouble("f_bal");
                sysAccountTypeKey = SModSysConsts.FINS_TP_SYS_MOV_YO_CDT;
            }

            if (rs.getDouble("f_bal_cur") >= 0) {
                debitCur = rs.getDouble("f_bal_cur");
                creditCur = 0;
            }
            else {
                debitCur = 0;
                creditCur = - rs.getDouble("f_bal_cur");
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

            sql = "INSERT INTO fin_rec_ety VALUES (" +
                    "" + mnPkYearId + ", " + mnPkPeriodId + ", " + mnPkBookkeepingCenterId + ", '" + msPkRecordTypeId + "', " + mnPkNumberId + ", " + mnPkEntryId + ", " +
                    "'SALDOS INICIALES " + mnPkYearId + "', " +
                    "'" + rs.getString("f_ref") + "', " + rs.getBoolean("b_ref_tax") + ", " +
                    "" + debit + ", " + credit + ", " + excRate + ", " + excRate + ", " + debitCur + ", " + creditCur + ", 0, 0, " + mnPkEntryId + ", 0, 1, 0, " +
                    "'" + rs.getString("fid_acc") + "', " +
                    "" + rs.getInt("fk_acc") + ", " +
                    "" + (rs.getInt("fid_tp_acc_r") == SDataConstantsSys.FINS_TP_ACC_BAL || rs.getInt("fk_cc_n") == 0 ? "NULL" : "" + rs.getInt("fk_cc_n")) + ", " +
                    "" + SDataConstantsSys.FINS_CLS_ACC_MOV_FY_OPEN[0] + ", " +
                    "" + SDataConstantsSys.FINS_CLS_ACC_MOV_FY_OPEN[1] + ", " +
                    "" + SDataConstantsSys.FINS_CLS_ACC_MOV_FY_OPEN[2] + ", " +
                    "" + sysAccountTypeKey[0] + ", " +
                    "" + sysAccountTypeKey[1] + ", " +
                    "" + rs.getInt("fid_cl_sys_acc") + ", " +
                    "" + rs.getInt("fid_tp_sys_acc") + ", " +
                    "" + rs.getInt("fid_ct_sys_mov_xxx") + ", " +
                    "" + rs.getInt("fid_tp_sys_mov_xxx") + ", " +
                    "" + rs.getInt("fid_cur") + ", " +
                    "" + (rs.getInt("fid_tp_acc_r") == SDataConstantsSys.FINS_TP_ACC_BAL || rs.getString("fid_cc_n") == null ? "NULL" : "'" + rs.getString("fid_cc_n") + "'") + ", " +
                    "NULL, NULL, " +
                    "" + (rs.getInt("fid_bp_nr") == 0 ? "NULL" : rs.getInt("fid_bp_nr")) + ", " +
                    "" + (rs.getInt("fid_bpb_n") == 0 ? "NULL" : rs.getInt("fid_bpb_n")) + ", " +

                    //"" + (rs.getInt("fid_ct_ref_n") == 0 ? "NULL" : rs.getInt("fid_ct_ref_n")) + ", " +
                    "NULL, " +

                    "" + (rs.getInt("fid_cob_n") == 0 ? "NULL" : rs.getInt("fid_cob_n")) + ", " +
                    "" + (rs.getInt("fid_ent_n") == 0 ? "NULL" : rs.getInt("fid_ent_n")) + ", " +
                    "NULL, NULL, " +    // plant
                    "" + (rs.getInt("fid_tax_bas_n") == 0 ? "NULL" : rs.getInt("fid_tax_bas_n")) + ", " +
                    "" + (rs.getInt("fid_tax_n") == 0 ? "NULL" : rs.getInt("fid_tax_n")) + ", " +
                    "" + (rs.getInt("fid_year_n") == 0 ? "NULL" : rs.getInt("fid_year_n")) + ", " +
                    "" + (rs.getInt("fid_dps_year_n") == 0 ? "NULL" : rs.getInt("fid_dps_year_n")) + ", " +
                    "" + (rs.getInt("fid_dps_doc_n") == 0 ? "NULL" : rs.getInt("fid_dps_doc_n")) + ", " +
                    "NULL, NULL, " +    // DPS adjustment
                    "NULL, NULL, " +    // DIOG
                    "NULL, NULL, " +    // manufacturing order
                    "NULL, " +          // group of indirect costs
                    "NULL, " +          // payroll (former)
                    "NULL, " +          // payroll
                    "" + (rs.getInt("fid_item_n") == 0 ? "NULL" : rs.getInt("fid_item_n")) + ", " +
                    "" + (rs.getInt("fid_item_aux_n") == 0 ? "NULL" : rs.getInt("fid_item_aux_n")) + ", " +
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

    public int getPkYearId() { return mnPkYearId; }
    public int getPkPeriodId() { return mnPkPeriodId; }
    public int getPkBookkeepingCenterId() { return mnPkBookkeepingCenterId; }
    public java.lang.String getPkRecordTypeId() { return msPkRecordTypeId; }
    public int getPkNumberId() { return mnPkNumberId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkUserId() { return mnFkUserId; }

    public void setAuxSimpleDateFormat(java.text.SimpleDateFormat o) { moAuxSimpleDateFormat = o; }

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
        mnPkEntryId = 0;
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
        String sGroup = "";
        ResultSet rs = null;
        Statement stQry = null;
        Statement stUpd = null;
        String account = "";
        Vector<Integer> levels = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            stQry = connection.createStatement();
            stUpd = connection.createStatement();

            sSql = "SELECT fmt_id_acc FROM erp.cfg_param_erp; ";
            rs = stQry.executeQuery(sSql);
            if (rs.next()) {
                account = rs.getString("fmt_id_acc").replaceAll("9", "0");
                levels = SDataUtilities.getAccountLevels(account);
            }

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
                    "'" + moAuxSimpleDateFormat.format(SLibTimeUtilities.createDate(mnPkYearId - 1, 12, 31)) + "', " +
                    "'SALDOS INICIALES " + mnPkYearId + "', 0, 0, 0, 0, 1, 0, " + mnFkCompanyBranchId + ", NULL, NULL, 1, 1, " +
                    "" + mnFkUserId + ", " + mnFkUserId + ", " + mnFkUserId + ", NOW(), NOW(), NOW(), NOW(), NOW()); ";
            stUpd.execute(sSql);

            mnPkEntryId = 0;

            // 1. Exclude DPS:

            for (int cat = SDataConstantsSys.FINS_CT_SYS_MOV_NA; cat <= SDataConstantsSys.FINS_CT_SYS_MOV_PROF; cat++) {
                /*
                fid_ct_sys_mov_xxx=1 and b_del=0; #fid_acc, fk_acc, fid_cc_n, fk_cc_n, fid_cur, fid_item_n, fid_unit_n, fid_item_aux_n, fid_tax_bas_n, fid_tax_n, fid_bp_nr, fid_bpb_n
                fid_ct_sys_mov_xxx=2 and b_del=0; #fid_acc, fk_acc, fid_cc_n, fk_cc_n, fid_cur, fid_item_n, fid_unit_n, fid_item_aux_n, fid_tax_bas_n, fid_tax_n, fid_cob_n, fid_ent_n
                fid_ct_sys_mov_xxx=3 and b_del=0; #fid_acc, fk_acc, fid_cc_n, fk_cc_n, fid_cur, fid_item_n, fid_unit_n, fid_item_aux_n, fid_tax_bas_n, fid_tax_n, ref, b_ref_tax, fid_cob_n, fid_ent_n
                fid_ct_sys_mov_xxx=4 and b_del=0; #fid_acc, fk_acc, fid_cc_n, fk_cc_n, fid_cur, fid_bp_nr, fid_bpb_n, ref, b_ref_tax, fid_dps_year_n, fid_dps_doc_n
                fid_ct_sys_mov_xxx=5 and b_del=0; #fid_acc, fk_acc, fid_cc_n, fk_cc_n, fid_cur, fid_item_n, fid_unit_n, fid_item_aux_n, fid_tax_bas_n, fid_tax_n
                fid_ct_sys_mov_xxx=6 and b_del=0; #fid_acc, fk_acc, fid_cc_n, fk_cc_n, fid_cur, fid_item_n, fid_unit_n, fid_item_aux_n
                fid_ct_sys_mov_xxx=7 and b_del=0; #fid_acc, fk_acc, fid_cc_n, fk_cc_n, fid_cur, fid_item_n, fid_unit_n, fid_item_aux_n
                fid_ct_sys_mov_xxx=8 and b_del=0; #fid_acc, fk_acc, fid_cc_n, fk_cc_n, fid_cur, fid_item_n, fid_unit_n, fid_item_aux_n
                fid_ct_sys_mov_xxx=9 and b_del=0; #fid_acc, fk_acc, fid_cc_n, fk_cc_n, fid_cur, fid_year_n
                */

                switch (cat) {
                    case SDataConstantsSys.FINS_CT_SYS_MOV_NA:
                        sSql = "SELECT re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "re.fid_item_n, re.fid_unit_n, re.fid_item_aux_n, re.fid_tax_bas_n, re.fid_tax_n, '' AS f_ref, 0 AS b_ref_tax, " +
                                "re.fid_bp_nr, re.fid_bpb_n, NULL AS fid_cob_n, NULL AS fid_ent_n, " +
                                "NULL AS fid_dps_year_n, NULL AS fid_dps_doc_n, NULL AS fid_year_n, ";
                        sGroup = "re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "re.fid_item_n, re.fid_unit_n, re.fid_item_aux_n, re.fid_tax_bas_n, re.fid_tax_n ";
                        break;
                    case SDataConstantsSys.FINS_CT_SYS_MOV_ASSET:
                        sSql = "SELECT re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "re.fid_item_n, re.fid_unit_n, re.fid_item_aux_n, re.fid_tax_bas_n, re.fid_tax_n, '' AS f_ref, 0 AS b_ref_tax, " +
                                "NULL AS fid_bp_nr, NULL AS fid_bpb_n, re.fid_cob_n, re.fid_ent_n, " +
                                "NULL AS fid_dps_year_n, NULL AS fid_dps_doc_n, NULL AS fid_year_n, ";
                        sGroup = "re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "re.fid_item_n, re.fid_unit_n, re.fid_item_aux_n, re.fid_tax_bas_n, re.fid_tax_n, " +
                                "re.fid_cob_n, re.fid_ent_n ";
                        break;
                    case SDataConstantsSys.FINS_CT_SYS_MOV_CASH:
                        sSql = "SELECT re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "re.fid_item_n, re.fid_unit_n, re.fid_item_aux_n, re.fid_tax_bas_n, re.fid_tax_n, re.ref AS f_ref, re.b_ref_tax, " +
                                "NULL AS fid_bp_nr, NULL AS fid_bpb_n, re.fid_cob_n, re.fid_ent_n, " +
                                "NULL AS fid_dps_year_n, NULL AS fid_dps_doc_n, NULL AS fid_year_n, ";
                        sGroup = "re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "re.fid_item_n, re.fid_unit_n, re.fid_item_aux_n, re.fid_tax_bas_n, re.fid_tax_n, " +
                                "f_ref, re.b_ref_tax, re.fid_cob_n, re.fid_ent_n ";
                        break;
                    case SDataConstantsSys.FINS_CT_SYS_MOV_BPS:
                        sSql = "SELECT re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "NULL AS fid_item_n, NULL AS fid_unit_n, NULL AS fid_item_aux_n, NULL AS fid_tax_bas_n, NULL AS fid_tax_n, IF(re.fid_dps_year_n IS NOT NULL, '', re.ref) AS f_ref, re.b_ref_tax, " +
                                "re.fid_bp_nr, re.fid_bpb_n, NULL AS fid_cob_n, NULL AS fid_ent_n, " +
                                "re.fid_dps_year_n, re.fid_dps_doc_n, NULL AS fid_year_n, ";
                        sGroup = "re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "f_ref, re.b_ref_tax, re.fid_bp_nr, re.fid_bpb_n, re.fid_dps_year_n, re.fid_dps_doc_n ";
                        break;
                    case SDataConstantsSys.FINS_CT_SYS_MOV_TAX:
                        sSql = "SELECT re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "re.fid_item_n, re.fid_unit_n, re.fid_item_aux_n, re.fid_tax_bas_n, re.fid_tax_n, '' AS f_ref, 0 AS b_ref_tax, " +
                                "fid_bp_nr, fid_bpb_n, NULL AS fid_cob_n, NULL AS fid_ent_n, " +
                                "NULL AS fid_dps_year_n, NULL AS fid_dps_doc_n, NULL AS fid_year_n, ";
                        sGroup = "re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "re.fid_item_n, re.fid_unit_n, re.fid_item_aux_n, re.fid_bp_nr, re.fid_bpb_n, re.fid_tax_bas_n, re.fid_tax_n ";
                        break;
                    case SDataConstantsSys.FINS_CT_SYS_MOV_PUR:
                        sSql = "SELECT re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "re.fid_item_n, re.fid_unit_n, re.fid_item_aux_n, NULL AS fid_tax_bas_n, NULL AS fid_tax_n, '' AS f_ref, 0 AS b_ref_tax, " +
                                "NULL AS fid_bp_nr, NULL AS fid_bpb_n, NULL AS fid_cob_n, NULL AS fid_ent_n, " +
                                "NULL AS fid_dps_year_n, NULL AS fid_dps_doc_n, NULL AS fid_year_n, ";
                        sGroup = "re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "re.fid_item_n, re.fid_unit_n, re.fid_item_aux_n ";
                        break;
                    case SDataConstantsSys.FINS_CT_SYS_MOV_SAL:
                        sSql = "SELECT re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "re.fid_item_n, re.fid_unit_n, re.fid_item_aux_n, NULL AS fid_tax_bas_n, NULL AS fid_tax_n, '' AS f_ref, 0 AS b_ref_tax, " +
                                "NULL AS fid_bp_nr, NULL AS fid_bpb_n, NULL AS fid_cob_n, NULL AS fid_ent_n, " +
                                "NULL AS fid_dps_year_n, NULL AS fid_dps_doc_n, NULL AS fid_year_n, ";
                        sGroup = "re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "re.fid_item_n, re.fid_unit_n, re.fid_item_aux_n ";
                        break;
                    case SDataConstantsSys.FINS_CT_SYS_MOV_COGS:
                        sSql = "SELECT re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "re.fid_item_n, re.fid_unit_n, re.fid_item_aux_n, NULL AS fid_tax_bas_n, NULL AS fid_tax_n, '' AS f_ref, 0 AS b_ref_tax, " +
                                "NULL AS fid_bp_nr, NULL AS fid_bpb_n, NULL AS fid_cob_n, NULL AS fid_ent_n, " +
                                "NULL AS fid_dps_year_n, NULL AS fid_dps_doc_n, NULL AS fid_year_n, ";
                        sGroup = "re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "re.fid_item_n, re.fid_unit_n, re.fid_item_aux_n ";
                        break;
                    case SDataConstantsSys.FINS_CT_SYS_MOV_PROF:
                        sSql = "SELECT re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "NULL AS fid_item_n, NULL AS fid_unit_n, NULL AS fid_item_aux_n, NULL AS fid_tax_bas_n, NULL AS fid_tax_n, '' AS f_ref, 0 AS b_ref_tax, " +
                                "NULL AS fid_bp_nr, NULL AS fid_bpb_n, NULL AS fid_cob_n, NULL AS fid_ent_n, " +
                                "NULL AS fid_dps_year_n, NULL AS fid_dps_doc_n, re.fid_year_n, ";
                        sGroup = "re.fid_acc, re.fk_acc, re.fid_cc_n, re.fk_cc_n, re.fid_cur, " +
                                "re.fid_cl_sys_acc, re.fid_tp_sys_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                                "re.fid_year_n ";
                        break;
                }

                sSql += "SUM(re.debit - re.credit) AS f_bal, SUM(re.debit_cur - re.credit_cur) AS f_bal_cur, am.fid_tp_acc_r " +
                        "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                        "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND " +
                        "r.id_num = re.id_num AND r.id_year = " + (mnPkYearId - 1) + " AND re.fid_ct_sys_mov_xxx = " + cat + " AND r.b_del = 0 AND re.b_del = 0 " +
                        "INNER JOIN fin_acc AS am ON " +
                        "CONCAT(LEFT(re.fid_acc, " + (levels.get(1) - 1) + "), '" + account.substring(levels.get(1) - 1) + "') = am.id_acc ";

                sSql += "GROUP BY " + sGroup + " " +
                        "HAVING f_bal <> 0 OR f_bal_cur <> 0 " +
                        "ORDER BY " + sGroup;
                processFiscalYearClosing(stQry, stUpd, sSql);
            }

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
