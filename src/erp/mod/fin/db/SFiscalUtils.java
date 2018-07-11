/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import cfd.DCfdConsts;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SDataParamsCompany;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SDbBizPartner;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiSession;
import sa.lib.xml.SXmlAttribute;
import sa.lib.xml.SXmlDocument;
import sa.lib.xml.SXmlElement;

/**
 *
 * @author Sergio Flores, Claudio Peña
 */
public abstract class SFiscalUtils {

    private static final int DEBIT = 1;
    private static final int CREDIT = 2;
    public static final DecimalFormat DecimalFormatImporte = new DecimalFormat("#0.00");
    public static final DecimalFormat DecimalFormatTipCamb = new DecimalFormat("#0.00000");         // WTF!!!, SAT defined this exchange rate format so!!!
    public static final SimpleDateFormat DateFormatFecha = new SimpleDateFormat("yyyy-MM-dd");
    public static final DecimalFormat DecimalFormatBizPartner = new DecimalFormat("00000");
    public static final DecimalFormat DecimalFormatEntity = new DecimalFormat("000");
    private static double ALLOWED_AMOUNT_DIFF = 0.05;
    
    /**
     * Extracts only decimal characters from text.
     * @return String containing only digits.
     */
    private static String extractDigits(final String text) {
        String digits = "";
        
        for (char c : text.toCharArray()) {
            if (c >= '0' && c <= '9') {
                digits += c;
            }
        }
        
        return digits;
    }

    /**
     * Creates default text for option of "non applicable" fiscal account.
     */
    public static String createFiscalAccountNameNotApplicable() {
        return SModSysConsts.FINS_FISCAL_ACC_NA_CODE + " - " + SModSysConsts.FINS_FISCAL_ACC_NA_NAME;
    }

    public static SDbFiscalAccountLinkDetail createFiscalAccountLinkDetail(final int accountId, final int linkType, final int[] referenceKey, final String fiscalAccountCode, final String accountCode, final String accountCodeParent, final String accountName, final String accountNature, final int level) {
        SDbFiscalAccountLinkDetail fiscalAccountLinkDetail = new SDbFiscalAccountLinkDetail();

        //fiscalAccountLinkDetail.setPkYearId(...);
        //fiscalAccountLinkDetail.setPkPeriodId(...);
        fiscalAccountLinkDetail.setPkAccountId(accountId);
        fiscalAccountLinkDetail.setPkFiscalAccountLinkTypeId(linkType);
        fiscalAccountLinkDetail.setPkReference1Id(referenceKey[0]);
        fiscalAccountLinkDetail.setPkReference2Id(referenceKey.length == 2 ? referenceKey[1] : SLibConsts.UNDEFINED);
        fiscalAccountLinkDetail.setFiscalAccount(fiscalAccountCode);
        fiscalAccountLinkDetail.setAccountCode(accountCode);
        fiscalAccountLinkDetail.setAccountCodeParent(accountCodeParent);
        fiscalAccountLinkDetail.setAccountName(accountName);
        fiscalAccountLinkDetail.setNature(accountNature);
        fiscalAccountLinkDetail.setLevel(level);

        return fiscalAccountLinkDetail;
    }

    /**
     * Reads accounts for grid pane rows <code>SFiscalAccount</code>.
     */
    public static ArrayList<SFiscalAccount> readAccounts(final SGuiSession session) throws Exception {
        String sql = "";
        ResultSet resultSet = null;
        SFiscalAccount account = null;
        ArrayList<SFiscalAccount> accounts = new ArrayList<>();

        sql = "SELECT a.pk_acc, F_ACC_USR(" + ((SDataParamsCompany) session.getConfigCompany()).getMaskAccount() + ", a.code) AS f_code, a.acc AS f_name, a.deep, a.lev, "
                + "fa.id_fiscal_acc, fa.code AS f_fiscal_code, fa.name AS f_fiscal_name "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_ACC) + " AS fa ON a.fid_fiscal_acc = fa.id_fiscal_acc "
                + "WHERE a.b_del = 0 "
                + "ORDER BY a.code; ";

        resultSet = session.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            account = new SFiscalAccount(resultSet.getInt("pk_acc"), resultSet.getString("f_code"), resultSet.getString("f_name"));
            account.setFiscalAccount(resultSet.getInt("id_fiscal_acc"), resultSet.getString("f_fiscal_code") + " - " + resultSet.getString("f_fiscal_name"));
            accounts.add(account);
        }

        return accounts;
    }

    public static String getFiscalAccountNameByCode(final Statement statement, final String code) throws Exception {
        String sql = "";
        String name = "";
        ResultSet resultSet = null;

        sql = "SELECT name FROM " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_ACC) + " WHERE code = '" + code + "' ";

        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            name = resultSet.getString(1);
        }

        return name;
    }

    public static boolean hasAccountingMoves(final Statement statement, final int itemId, final int periodYear, final int periodMonth, final int moveType) throws Exception {
        boolean hasMoves = false;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COUNT(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON "
                + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND "
                + "r.b_del = 0 AND re.b_del = 0 AND r.id_year = " + periodYear + " AND r.id_per <= " + periodMonth + " "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON re.fid_acc = a.id_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND "
                + "al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " "
                + "WHERE re.fid_item_n = " + itemId + " AND " + (moveType == DEBIT ? "(re.debit <> 0.0 OR (re.debit = 0 AND re.credit = 0))" : "(re.credit <> 0.0)") + "; " ;

        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            hasMoves = resultSet.getInt(1) > 0;
        }

        return hasMoves;
    }

    /*
     * XML: Catálogo de cuentas utilizado en el período.
     */

    public static String createQueryCatalogo11(final SGuiSession session, final int periodYear, final int periodMonth) throws Exception {
        String sql = "";

        if (periodYear < SFiscalConsts.YEAR_MIN || periodYear > SLibTimeConsts.YEAR_MAX) {
            throw new Exception("El atributo 'Anio' debe ser mínimo " + SFiscalConsts.YEAR_MIN + " y máximo " + SLibTimeConsts.YEAR_MAX + ".");
        }

        if (periodMonth < SLibTimeConsts.MONTH_MIN || periodMonth > SLibTimeConsts.MONTH_MAX) {
            throw new Exception("El atributo 'Mes' debe ser mínimo " + SLibTimeConsts.MONTH_MIN + " y máximo " + SLibTimeConsts.MONTH_MAX + ".");
        }

        /*
         * Obtain accounts as follows:
         * - Group 1. Input level accounts (all levels).
         * - Group 2. Level 1 accounts (ledger accounts) not included in Group 1.
         * - Group 3. Level 2 subaccounts not included in Group 1.
         */

        sql =
            "SELECT " +
            "DISTINCT re.fid_acc AS f_acc_id, " +
            "a.pk_acc AS f_acc_pk, " +
            "CASE " +
                "WHEN al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " THEN COALESCE(i.item_key, '') " +
                "ELSE F_ACC_USR(" + ((SDataParamsCompany) session.getConfigCompany()).getMaskAccount() + ", a.code) END AS f_acc_code, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe = " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " THEN e.ent " +
                "WHEN al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " THEN b.bp " +
                "WHEN al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " THEN i.item " +
                "ELSE a.acc END AS f_acc_name, " +
            "a.lev + CASE " +
                "WHEN al.fid_tp_acc_spe = " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " OR al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " OR al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " THEN 1 " +
                "ELSE 0 END AS f_lev, " +
            "al.deep AS f_deep, " +
            "fa.code AS f_fis_acc_code, " +
            "al.fid_tp_acc_r AS f_tp_acc, " +
            "al.fid_cl_acc_r AS f_cl_acc, " +
            "al.fid_tp_acc_spe AS f_tp_spe, " +
            "spe.name AS f_tp_spe_name, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe = " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " THEN re.fid_cob_n " +
                "ELSE NULL END AS f_bnk_cob_id, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe = " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " THEN re.fid_ent_n " +
                "ELSE NULL END AS f_bnk_ent_id, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe = " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " THEN bk.fiscal_id " +
                "ELSE NULL END AS f_bnk_fis_id, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " THEN re.fid_bp_nr " +
                "ELSE NULL END AS f_bpr_id, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " THEN b.fiscal_id " +
                "ELSE NULL END AS f_bpr_fis_id, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " THEN b.b_att_emp " +
                "ELSE NULL END AS f_bpr_emp, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " THEN b.b_att_par_shh " +
                "ELSE NULL END AS f_bpr_shh, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " THEN b.b_att_rel_pty " +
                "ELSE NULL END AS f_bpr_rel, " +
            "CASE " +
                "WHEN al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " THEN COALESCE(re.fid_item_n, 0) " +
                "ELSE NULL END AS f_itm_id, " +
            "faii.code AS f_itm_fis_acc_inc, " +
            "faie.code AS f_itm_fis_acc_exp " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num AND r.b_del = 0 AND re.b_del = 0 " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON a.id_acc = re.fid_acc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_ACC) + " AS fa ON fa.id_fiscal_acc = a.fid_fiscal_acc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_TP_ACC_SPE) + " AS spe ON spe.id_tp_acc_spe = al.fid_tp_acc_spe " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS e ON e.id_cob = re.fid_cob_n AND e.id_ent = re.fid_ent_n " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC_CASH) + " AS ac ON ac.id_cob = e.id_cob AND ac.id_acc_cash = e.id_ent " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BANK_ACC) + " AS bka ON bka.id_bpb = ac.fid_bpb_n AND bka.id_bank_acc = ac.fid_bank_acc_n " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bk ON bk.id_bp = bka.fid_bank " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = re.fid_bp_nr " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON i.id_item = re.fid_item_n " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_ACC) + " AS faii ON faii.id_fiscal_acc = i.fid_fiscal_acc_inc " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_ACC) + " AS faie ON faie.id_fiscal_acc = i.fid_fiscal_acc_exp " +
            "WHERE r.id_year = " + periodYear + " AND r.id_per <= " + periodMonth + " " +
            "" +
            "UNION " +
            "" +
            "SELECT " +
            "a.id_acc AS f_acc_id, " +
            "a.pk_acc AS f_acc_pk, " +
            "F_ACC_USR(" + ((SDataParamsCompany) session.getConfigCompany()).getMaskAccount() + ", a.code) AS f_acc_code, " +
            "a.acc AS f_acc_name, " +
            "a.lev AS f_lev, " +
            "a.deep AS f_deep, " +
            "fa.code AS f_fis_acc_code, " +
            "a.fid_tp_acc_r AS f_tp_acc, " +
            "a.fid_cl_acc_r AS f_cl_acc, " +
            "a.fid_tp_acc_spe AS f_tp_spe, " +
            "spe.name AS f_tp_spe_name, " +
            "NULL AS f_bnk_cob_id, " +
            "NULL AS f_bnk_ent_id, " +
            "NULL AS f_bnk_fis_id, " +
            "NULL AS f_bpr_id, " +
            "NULL AS f_bpr_fis_id, " +
            "NULL AS f_bpr_emp, " +
            "NULL AS f_bpr_shh, " +
            "NULL AS f_bpr_rel, " +
            "NULL AS f_itm_id, " +
            "NULL AS f_itm_fis_acc_inc, " +
            "NULL AS f_itm_fis_acc_exp " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_ACC) + " AS fa ON fa.id_fiscal_acc = a.fid_fiscal_acc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_TP_ACC_SPE) + " AS spe ON spe.id_tp_acc_spe = a.fid_tp_acc_spe " +
            "WHERE a.lev = 1 AND a.code IN (" +
            "SELECT DISTINCT CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AS f_acc_ldg_id " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num AND r.b_del = 0 AND re.b_del = 0 " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON a.id_acc = re.fid_acc " +
            "WHERE r.id_year = " + periodYear + " AND r.id_per <= " + periodMonth + " " +
            "ORDER BY f_acc_ldg_id) " +
            "" +
            "UNION " +
            "" +
            "SELECT " +
            "a.id_acc AS f_acc_id, " +
            "a.pk_acc AS f_acc_pk, " +
            "F_ACC_USR(" + ((SDataParamsCompany) session.getConfigCompany()).getMaskAccount() + ", a.code) AS f_acc_code, " +
            "a.acc AS f_acc_name, " +
            "a.lev AS f_lev, " +
            "al.deep AS f_deep, " +
            "fa.code AS f_fis_acc_code, " +
            "al.fid_tp_acc_r AS f_tp_acc, " +
            "al.fid_cl_acc_r AS f_cl_acc, " +
            "al.fid_tp_acc_spe AS f_tp_spe, " +
            "spe.name AS f_tp_spe_name, " +
            "NULL AS f_bnk_cob_id, " +
            "NULL AS f_bnk_ent_id, " +
            "NULL AS f_bnk_fis_id, " +
            "NULL AS f_bpr_id, " +
            "NULL AS f_bpr_fis_id, " +
            "NULL AS f_bpr_emp, " +
            "NULL AS f_bpr_shh, " +
            "NULL AS f_bpr_rel, " +
            "NULL AS f_itm_id, " +
            "NULL AS f_itm_fis_acc_inc, " +
            "NULL AS f_itm_fis_acc_exp " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_ACC) + " AS fa ON fa.id_fiscal_acc = a.fid_fiscal_acc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_TP_ACC_SPE) + " AS spe ON spe.id_tp_acc_spe = al.fid_tp_acc_spe " +
            "WHERE a.lev = 2 AND a.code IN ( " +
            "SELECT DISTINCT CONCAT(LEFT(a.code, 12), REPEAT('0', 36)) AS f_acc_ldg_id " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num AND r.b_del = 0 AND re.b_del = 0 " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON a.id_acc = re.fid_acc " +
            "WHERE r.id_year = " + periodYear + " AND r.id_per <= " + periodMonth + " " +
            "ORDER BY f_acc_ldg_id) " +
            "" +
            "ORDER BY f_acc_id, f_lev, f_acc_code, f_bnk_cob_id, f_bnk_ent_id, f_bpr_id, f_itm_id; ";

        return sql;
    }

    public static String createQueryCatalogo13(final SGuiSession session, final int periodYear, final int periodMonth) throws Exception {
        String sql = "";

        if (periodYear < SFiscalConsts.YEAR_MIN || periodYear > SLibTimeConsts.YEAR_MAX) {
            throw new Exception("El atributo 'Anio' debe ser mínimo " + SFiscalConsts.YEAR_MIN + " y máximo " + SLibTimeConsts.YEAR_MAX + ".");
        }

        if (periodMonth < SLibTimeConsts.MONTH_MIN || periodMonth > SLibTimeConsts.MONTH_MAX) {
            throw new Exception("El atributo 'Mes' debe ser mínimo " + SLibTimeConsts.MONTH_MIN + " y máximo " + SLibTimeConsts.MONTH_MAX + ".");
        }

        /*
         * Obtain accounts as follows:
         * - Group 1. Input level accounts (all levels).
         * - Group 2. Level 1 accounts (ledger accounts) not included in Group 1.
         * - Group 3. Level 2 subaccounts not included in Group 1.
         */

        sql =
            "SELECT " +
            "DISTINCT re.fid_acc AS f_acc_id, " +
            "a.pk_acc AS f_acc_pk, " +
            "COALESCE(CASE " +
                "WHEN al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " AND al.b_req_item THEN COALESCE(i.item_key, '') " +
                "ELSE F_ACC_USR(" + ((SDataParamsCompany) session.getConfigCompany()).getMaskAccount() + ", a.code) END, '') AS f_acc_code, " +
            "COALESCE(CASE " +
                "WHEN al.fid_tp_acc_spe = " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " THEN e.ent " +
                "WHEN al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " THEN b.bp " +
                "WHEN al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " AND al.b_req_item THEN i.item " +
                "ELSE a.acc END, '') AS f_acc_name, " +
            "COALESCE(CASE " +
                "WHEN al.fid_tp_acc_spe = " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " THEN e.ent " +
                "WHEN al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " THEN b.bp " +
                "WHEN al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " AND al.b_req_item THEN CONCAT(i.item_key, '', i.item) " +
                "ELSE a.acc END, '') AS f_acc, " +
            "a.lev + CASE " +
                "WHEN al.fid_tp_acc_spe = " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " OR al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " OR (al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " AND al.b_req_item) THEN 1 " +
                "ELSE 0 END AS f_lev, " +
            "al.deep AS f_deep, " +
            "fa.code AS f_fis_acc_code, " +
            "al.fid_tp_acc_r AS f_tp_acc, " +
            "al.fid_cl_acc_r AS f_cl_acc, " +
            "al.fid_cls_acc_r AS f_cls_acc, " +
            "al.fid_tp_acc_spe AS f_tp_spe, " +
            "al.b_req_item AS f_req_item, " +
            "spe.name AS f_tp_spe_name, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe = " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " THEN re.fid_cob_n " +
                "ELSE NULL END AS f_bnk_cob_id, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe = " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " THEN re.fid_ent_n " +
                "ELSE NULL END AS f_bnk_ent_id, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe = " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " THEN bk.fiscal_id " +
                "ELSE NULL END AS f_bnk_fis_id, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " THEN re.fid_bp_nr " +
                "ELSE NULL END AS f_bpr_id, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " THEN b.fiscal_id " +
                "ELSE NULL END AS f_bpr_fis_id, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " THEN b.b_att_emp " +
                "ELSE NULL END AS f_bpr_emp, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " THEN b.b_att_par_shh " +
                "ELSE NULL END AS f_bpr_shh, " +
            "CASE " +
                "WHEN al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " THEN b.b_att_rel_pty " +
                "ELSE NULL END AS f_bpr_rel, " +
            "CASE " +
                "WHEN al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " AND al.b_req_item THEN COALESCE(re.fid_item_n, 0) " +
                "ELSE NULL END AS f_itm_id, " +
            "faii.code AS f_itm_fis_acc_inc, " +
            "faie.code AS f_itm_fis_acc_exp " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num AND r.b_del = 0 AND re.b_del = 0 " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON a.id_acc = re.fid_acc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_ACC) + " AS fa ON fa.id_fiscal_acc = a.fid_fiscal_acc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_TP_ACC_SPE) + " AS spe ON spe.id_tp_acc_spe = al.fid_tp_acc_spe " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS e ON e.id_cob = re.fid_cob_n AND e.id_ent = re.fid_ent_n " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC_CASH) + " AS ac ON ac.id_cob = e.id_cob AND ac.id_acc_cash = e.id_ent " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BANK_ACC) + " AS bka ON bka.id_bpb = ac.fid_bpb_n AND bka.id_bank_acc = ac.fid_bank_acc_n " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bk ON bk.id_bp = bka.fid_bank " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = re.fid_bp_nr " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON i.id_item = re.fid_item_n AND al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " AND al.b_req_item " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_ACC) + " AS faii ON faii.id_fiscal_acc = i.fid_fiscal_acc_inc AND al.fid_tp_acc_r = " + SModSysConsts.FINS_CL_ACC_RES_CDT[0] + " AND al.fid_cl_acc_r = " + SModSysConsts.FINS_CL_ACC_RES_CDT[1] + " " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_ACC) + " AS faie ON faie.id_fiscal_acc = i.fid_fiscal_acc_exp AND al.fid_tp_acc_r = " + SModSysConsts.FINS_CL_ACC_RES_DBT[0] + " AND al.fid_cl_acc_r = " + SModSysConsts.FINS_CL_ACC_RES_DBT[1] + " " +
            "WHERE r.id_year = " + periodYear + " AND r.id_per <= " + periodMonth + " " +
            "" +
            "UNION " +
            "" +
            "SELECT " +
            "a.id_acc AS f_acc_id, " +
            "a.pk_acc AS f_acc_pk, " +
            "F_ACC_USR(" + ((SDataParamsCompany) session.getConfigCompany()).getMaskAccount() + ", a.code) AS f_acc_code, " +
            "a.acc AS f_acc_name, " +
            "a.acc AS f_acc, " +
            "a.lev AS f_lev, " +
            "a.deep AS f_deep, " +
            "fa.code AS f_fis_acc_code, " +
            "a.fid_tp_acc_r AS f_tp_acc, " +
            "a.fid_cl_acc_r AS f_cl_acc, " +
            "a.fid_cls_acc_r AS f_cls_acc, " +
            "a.fid_tp_acc_spe AS f_tp_spe, " +
            "a.b_req_item AS f_req_item, " +
            "spe.name AS f_tp_spe_name, " +
            "NULL AS f_bnk_cob_id, " +
            "NULL AS f_bnk_ent_id, " +
            "NULL AS f_bnk_fis_id, " +
            "NULL AS f_bpr_id, " +
            "NULL AS f_bpr_fis_id, " +
            "NULL AS f_bpr_emp, " +
            "NULL AS f_bpr_shh, " +
            "NULL AS f_bpr_rel, " +
            "NULL AS f_itm_id, " +
            "NULL AS f_itm_fis_acc_inc, " +
            "NULL AS f_itm_fis_acc_exp " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_ACC) + " AS fa ON fa.id_fiscal_acc = a.fid_fiscal_acc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_TP_ACC_SPE) + " AS spe ON spe.id_tp_acc_spe = a.fid_tp_acc_spe " +
            "WHERE a.lev = 1 AND a.code IN ( " +
            "SELECT DISTINCT CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AS f_acc_ldg_id " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num AND r.b_del = 0 AND re.b_del = 0 " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON a.id_acc = re.fid_acc " +
            "WHERE r.id_year = " + periodYear + " AND r.id_per <= " + periodMonth + " " +
            "ORDER BY f_acc_ldg_id) " +
            "" +
            "UNION " +
            "" +
            "SELECT " +
            "a.id_acc AS f_acc_id, " +
            "a.pk_acc AS f_acc_pk, " +
            "F_ACC_USR(" + ((SDataParamsCompany) session.getConfigCompany()).getMaskAccount() + ", a.code) AS f_acc_code, " +
            "a.acc AS f_acc_name, " +
            "a.acc AS f_acc, " +
            "a.lev AS f_lev, " +
            "al.deep AS f_deep, " +
            "fa.code AS f_fis_acc_code, " +
            "al.fid_tp_acc_r AS f_tp_acc, " +
            "al.fid_cl_acc_r AS f_cl_acc, " +
            "al.fid_cls_acc_r AS f_cls_acc, " +
            "al.fid_tp_acc_spe AS f_tp_spe, " +
            "al.b_req_item AS f_req_item, " +
            "spe.name AS f_tp_spe_name, " +
            "NULL AS f_bnk_cob_id, " +
            "NULL AS f_bnk_ent_id, " +
            "NULL AS f_bnk_fis_id, " +
            "NULL AS f_bpr_id, " +
            "NULL AS f_bpr_fis_id, " +
            "NULL AS f_bpr_emp, " +
            "NULL AS f_bpr_shh, " +
            "NULL AS f_bpr_rel, " +
            "NULL AS f_itm_id, " +
            "NULL AS f_itm_fis_acc_inc, " +
            "NULL AS f_itm_fis_acc_exp " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_ACC) + " AS fa ON fa.id_fiscal_acc = a.fid_fiscal_acc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_TP_ACC_SPE) + " AS spe ON spe.id_tp_acc_spe = al.fid_tp_acc_spe " +
            "WHERE a.lev = 2 AND a.code IN ( " +
            "SELECT DISTINCT CONCAT(LEFT(a.code, 12), REPEAT('0', 36)) AS f_acc_ldg_id " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num AND r.b_del = 0 AND re.b_del = 0 " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON a.id_acc = re.fid_acc " +
            "WHERE r.id_year = " + periodYear + " AND r.id_per <= " + periodMonth + " " +
            "ORDER BY f_acc_ldg_id) " +
            "" +
            "ORDER BY f_acc_id, f_lev, f_acc, f_bnk_cob_id, f_bnk_ent_id, f_bpr_id, f_itm_id; ";

        return sql;
    }

    private static SXmlElement createElementCatalogo11Ctas(final SDbFiscalAccountLinkDetail fiscalAccountLinkDetail) {
        SXmlElement element = new SXmlElement("catalogocuentas:Ctas");

        element.getXmlAttributes().add(new SXmlAttribute("CodAgrup", fiscalAccountLinkDetail.getFiscalAccount()));
        element.getXmlAttributes().add(new SXmlAttribute("NumCta", fiscalAccountLinkDetail.getAccountCode()));
        element.getXmlAttributes().add(new SXmlAttribute("Desc", fiscalAccountLinkDetail.getAccountName()));

        if (!fiscalAccountLinkDetail.getAccountCodeParent().isEmpty()) {
            element.getXmlAttributes().add(new SXmlAttribute("SubCtaDe", fiscalAccountLinkDetail.getAccountCodeParent()));
        }

        element.getXmlAttributes().add(new SXmlAttribute("Nivel", "" + fiscalAccountLinkDetail.getLevel()));
        element.getXmlAttributes().add(new SXmlAttribute("Natur", fiscalAccountLinkDetail.getNature()));

        return element;
    }
    
    private static SXmlElement createElementCatalogo13Ctas(final SDbFiscalAccountLinkDetail fiscalAccountLinkDetail) {
        SXmlElement element = new SXmlElement("catalogocuentas:Ctas");

        element.getXmlAttributes().add(new SXmlAttribute("CodAgrup", fiscalAccountLinkDetail.getFiscalAccount()));
        element.getXmlAttributes().add(new SXmlAttribute("NumCta", fiscalAccountLinkDetail.getAccountCode()));
        element.getXmlAttributes().add(new SXmlAttribute("Desc", fiscalAccountLinkDetail.getAccountName()));

        if (!fiscalAccountLinkDetail.getAccountCodeParent().isEmpty()) {
            element.getXmlAttributes().add(new SXmlAttribute("SubCtaDe", fiscalAccountLinkDetail.getAccountCodeParent()));
        }

        element.getXmlAttributes().add(new SXmlAttribute("Nivel", "" + fiscalAccountLinkDetail.getLevel()));
        element.getXmlAttributes().add(new SXmlAttribute("Natur", fiscalAccountLinkDetail.getNature()));

        return element;
    }

    /**
     * Creates XML "Catálogo de cuentas utilizado en el período" 1.1.
     * @param session GUI user session.
     * @param periodYear Requested period's year.
     * @param periodMonth Requested period's month.
     */
    public static SXmlDocument createDocCatalogo11(final SGuiSession session, final int periodYear, final int periodMonth) throws Exception {
        int i = 0;
        int id = 0;
        int accountPk = 0;
        int deep = 0;
        int level = 0;
        int count = 0;
        int linkType = 0;
        int accountTypeId = 0;
        int accountClassId = 0;
        int[] accountClassKey = null;
        int[] referenceKey = null;
        char[] charArray = null;
        boolean attEmployee = false;
        boolean attShareholder = false;
        boolean attRelatedParty = false;
        boolean useFixedFiscalAccounts = false;     // when 'true', treat system accounts (e.g., cash, banks, business partners, etc.) as simple fixed fiscal accounts, those ones provided by SAT, without any individual system subaccount. Otherwise, when 'false', treat each individual system subaccount as a fiscal subaccount
        boolean reqBizPartner = false;
        String sql = "";
        String accountId = "";
        String accountCode = "";
        String accountName = "";
        String accountNature = "";
        String parentAccountId = "";
        String fiscalIdBizPartner = "";
        String fiscalIdBank = "";
        String fiscalAccountAux = "";
        String fiscalAccountCodeSpecial = "";
        String fiscalAccountCode = "";
        Statement statement = null;
        Statement statementAux = null;
        ResultSet resultSet = null;
        SDbBizPartner company = null;
        SDbFiscalAccountLink fiscalAccountLink = null;
        ArrayList<SDbFiscalAccountLinkDetail> fiscalAccountLinkDetails = new ArrayList<>();
        SXmlDocument xmlDoc = null;
        SXmlElement xmlCtas = null;
        HashSet<String> fixedFiscalAccounts = new HashSet<>();

        company = (SDbBizPartner) session.readRegistry(SModConsts.BPSU_BP, new int[] { session.getConfigCompany().getCompanyId() });

        fiscalAccountLink = new SDbFiscalAccountLink();
        fiscalAccountLink.setPkYearId(periodYear);
        fiscalAccountLink.setPkPeriodId(periodMonth);

        xmlDoc = new SXmlDocument("catalogocuentas:Catalogo", false) {

            @Override
            public void processXml(String xml) throws Exception {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        xmlDoc.setCustomHeader(
                  "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:catalogocuentas=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas\" "
                + "xsi:schemaLocation=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas/CatalogoCuentas_1_1.xsd\" ");

        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Version", "1.1"));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("RFC", company.getFiscalId()));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Mes", SLibUtils.DecimalFormatCalendarMonth.format(periodMonth)));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Anio", SLibUtils.DecimalFormatCalendarYear.format(periodYear)));

        /*
         * Obtain accounts as follows:
         * - Group 1. Input level accounts (all levels).
         * - Group 2. Level 1 accounts (ledger accounts) not included in Group 1.
         * - Group 3. Level 2 subaccounts not included in Group 1.
         */

        sql = createQueryCatalogo11(session, periodYear, periodMonth);

        statement = session.getStatement().getConnection().createStatement();
        statementAux = session.getStatement().getConnection().createStatement();

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            // Check if Fiscal Account provided by SAT was set on current account:

            accountNature = "";
            fiscalAccountCodeSpecial = "";
            fiscalAccountCode = resultSet.getString("f_fis_acc_code");
            accountPk = resultSet.getInt("f_acc_pk");
            accountId = resultSet.getString("f_acc_id");
            accountCode = resultSet.getString("f_acc_code");
            accountName = resultSet.getString("f_acc_name");
            accountTypeId = resultSet.getInt("f_tp_acc");
            accountClassId = resultSet.getInt("f_cl_acc");
            
            if (SLibUtils.parseDouble(fiscalAccountCode) == 0d) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nCódigo agrupador del SAT no asignado a la "
                        + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
            }

            fiscalAccountLinkDetails.clear();
            linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_ACC;   // default link type
            referenceKey = new int[] { accountPk };                 // default reference

            deep = resultSet.getInt("f_deep");
            level = resultSet.getInt("f_lev");

            if (level == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNivel cero en la "
                        + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
            }
            else if (level > 1 && level > deep) {
                attEmployee = resultSet.getBoolean("f_bpr_emp");
                attShareholder = resultSet.getBoolean("f_bpr_shh");
                attRelatedParty = resultSet.getBoolean("f_bpr_rel");

                fiscalIdBizPartner = resultSet.getString("f_bpr_fis_id");
                if (fiscalIdBizPartner == null) {
                    fiscalIdBizPartner = "";
                }

                fiscalIdBank = resultSet.getString("f_bnk_fis_id");
                if (fiscalIdBank == null) {
                    fiscalIdBank = "";
                }

                reqBizPartner = false;
                id = SLibConsts.UNDEFINED;
                fiscalAccountAux = fiscalAccountCode.indexOf('.') == -1 ? fiscalAccountCode : fiscalAccountCode.substring(0, fiscalAccountCode.indexOf('.'));

                switch (fiscalAccountAux) {
                    case SFiscalConsts.ACC_CSH_CSH:
                        accountNature = SFiscalConsts.COA_DBT;
                        fiscalAccountCodeSpecial = SFiscalConsts.ACC_CSH_CSH_CSH;

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_CSH;
                        referenceKey = new int[] { resultSet.getInt("f_bnk_cob_id"), resultSet.getInt("f_bnk_ent_id") };
                        break;

                    case SFiscalConsts.ACC_CSH_BNK:
                        accountNature = SFiscalConsts.COA_DBT;
                        fiscalAccountCodeSpecial = fiscalIdBank.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_CSH_BNK_INT : SFiscalConsts.ACC_CSH_BNK_DOM;

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_BNK;
                        referenceKey = new int[] { resultSet.getInt("f_bnk_cob_id"), resultSet.getInt("f_bnk_ent_id") };
                        break;

                    case SFiscalConsts.ACC_BPR_CUS:
                        accountNature = SFiscalConsts.COA_DBT;
                        if (attRelatedParty) {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CUS_REL_INT : SFiscalConsts.ACC_BPR_CUS_REL_DOM;
                        }
                        else {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CUS_INT : SFiscalConsts.ACC_BPR_CUS_DOM;
                        }

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CUS;
                        reqBizPartner = true;
                        id = resultSet.getInt("f_bpr_id");
                        referenceKey = new int[] { id };
                        break;

                    case SFiscalConsts.ACC_BPR_DBR:
                        accountNature = SFiscalConsts.COA_DBT;
                        if (attEmployee) {
                            fiscalAccountCodeSpecial = SFiscalConsts.ACC_BPR_DBR_EMP;
                        }
                        else if (attShareholder) {
                            fiscalAccountCodeSpecial = SFiscalConsts.ACC_BPR_DBR_SHH;
                        }
                        else if (attRelatedParty) {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_DBR_REL_INT : SFiscalConsts.ACC_BPR_DBR_REL_DOM;
                        }
                        else {
                            fiscalAccountCodeSpecial = SFiscalConsts.ACC_BPR_DBR_OTH;
                        }

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_DBR;
                        reqBizPartner = true;
                        id = resultSet.getInt("f_bpr_id");
                        referenceKey = new int[] { id };
                        break;

                    case SFiscalConsts.ACC_BPR_SUP_ADV:
                        accountNature = SFiscalConsts.COA_DBT;
                        if (attRelatedParty) {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_SUP_ADV_REL_INT : SFiscalConsts.ACC_BPR_SUP_ADV_REL_DOM;
                        }
                        else {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_SUP_ADV_INT : SFiscalConsts.ACC_BPR_SUP_ADV_DOM;
                        }

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_SUP_ADV;
                        reqBizPartner = true;
                        id = resultSet.getInt("f_bpr_id");
                        referenceKey = new int[] { id };
                        break;

                    case SFiscalConsts.ACC_BPR_SUP:
                        accountNature = SFiscalConsts.COA_CDT;
                        if (attRelatedParty) {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_SUP_REL_INT : SFiscalConsts.ACC_BPR_SUP_REL_DOM;
                        }
                        else {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_SUP_INT : SFiscalConsts.ACC_BPR_SUP_DOM;
                        }

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_SUP;
                        reqBizPartner = true;
                        id = resultSet.getInt("f_bpr_id");
                        referenceKey = new int[] { id };
                        break;

                    case SFiscalConsts.ACC_BPR_CDR_ST:
                        accountNature = SFiscalConsts.COA_CDT;
                        if (attShareholder) {
                            fiscalAccountCodeSpecial = SFiscalConsts.ACC_BPR_CDR_ST_SHH;
                        }
                        else if (attRelatedParty) {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CDR_ST_REL_INT : SFiscalConsts.ACC_BPR_CDR_ST_REL_DOM;
                        }
                        else {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CDR_ST_INT : SFiscalConsts.ACC_BPR_CDR_ST_DOM;
                        }

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CDR;
                        reqBizPartner = true;
                        id = resultSet.getInt("f_bpr_id");
                        referenceKey = new int[] { id };
                        break;

                    case SFiscalConsts.ACC_BPR_CUS_ADV:
                        accountNature = SFiscalConsts.COA_CDT;
                        if (attRelatedParty) {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CUS_ADV_REL_INT : SFiscalConsts.ACC_BPR_CUS_ADV_REL_DOM;
                        }
                        else {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CUS_ADV_INT : SFiscalConsts.ACC_BPR_CUS_ADV_DOM;
                        }

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CUS_ADV;
                        reqBizPartner = true;
                        id = resultSet.getInt("f_bpr_id");
                        referenceKey = new int[] { id };
                        break;

                    case SFiscalConsts.ACC_BPR_CDR_LT:
                        accountNature = SFiscalConsts.COA_CDT;
                        if (attShareholder) {
                            fiscalAccountCodeSpecial = SFiscalConsts.ACC_BPR_CDR_LT_SHH;
                        }
                        else if (attRelatedParty) {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CDR_LT_REL_INT : SFiscalConsts.ACC_BPR_CDR_LT_REL_DOM;
                        }
                        else {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CDR_LT_INT : SFiscalConsts.ACC_BPR_CDR_LT_DOM;
                        }

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CDR;
                        reqBizPartner = true;
                        id = resultSet.getInt("f_bpr_id");
                        referenceKey = new int[] { id };
                        break;

                    default:
                        if (accountTypeId == SModSysConsts.FINS_TP_ACC_BAL) {
                            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nCódigo agrupador del SAT incorrecto (" + fiscalAccountCode + ") en la "
                                    + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").\n"
                                    + "Dicho código agrupador del SAT debe provenir de alguno de los siguientes, a 1er. o 2do. nivel: \n"
                                    + SFiscalConsts.ACC_CSH_CSH + ", "
                                    + SFiscalConsts.ACC_CSH_BNK + ", "
                                    + SFiscalConsts.ACC_BPR_CUS + ", "
                                    + SFiscalConsts.ACC_BPR_DBR + ", "
                                    + SFiscalConsts.ACC_BPR_SUP_ADV + ", "
                                    + SFiscalConsts.ACC_BPR_SUP + ", "
                                    + SFiscalConsts.ACC_BPR_CDR_ST + ", "
                                    + SFiscalConsts.ACC_BPR_CUS_ADV + ", "
                                    + SFiscalConsts.ACC_BPR_CDR_LT + ".");
                        }
                }
                
                if (reqBizPartner && id == SLibConsts.UNDEFINED) {
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nAsociado de negocios no definido en "
                            + "cuenta contable " + accountId + ":\n'" + (accountName == null ? "?" : accountName) + "' (" + accountCode + ").");
                }
            }

            // Create fiscal account link details if necessary:

            if (!fiscalAccountCodeSpecial.isEmpty()) {
                if (!useFixedFiscalAccounts || (useFixedFiscalAccounts && !fixedFiscalAccounts.contains(fiscalAccountCodeSpecial))) {
                    // Prepare account:

                    if (useFixedFiscalAccounts)  {
                        fixedFiscalAccounts.add(fiscalAccountCodeSpecial);
                        accountCode = accountId + fiscalAccountCodeSpecial.substring(fiscalAccountCodeSpecial.indexOf('.'));
                        accountName = getFiscalAccountNameByCode(statementAux, fiscalAccountCodeSpecial).toUpperCase();
                    }
                    else {
                        accountCode = accountId + "." + referenceKey[0] + (referenceKey.length == 2 ? referenceKey[1] : "");
                    }

                    fiscalAccountLinkDetails.add(createFiscalAccountLinkDetail(accountPk, linkType, referenceKey, fiscalAccountCodeSpecial, accountCode, accountId, accountName, accountNature, level));
                }
            }
            else {
                // Make up fiscal account's code if necessary:

                // Define account's nature:

                accountClassKey = new int[] { accountTypeId, accountClassId };

                if (SLibUtils.belongsTo(accountClassKey, new int[][] { SModSysConsts.FINS_CL_ACC_ASSET, SModSysConsts.FINS_CL_ACC_ORD_DBT, SModSysConsts.FINS_CL_ACC_RES_DBT })) {
                    accountNature = SFiscalConsts.COA_DBT;
                }
                else if (SLibUtils.belongsTo(accountClassKey, new int[][] { SModSysConsts.FINS_CL_ACC_LIABTY, SModSysConsts.FINS_CL_ACC_EQUITY, SModSysConsts.FINS_CL_ACC_ORD_CDT, SModSysConsts.FINS_CL_ACC_RES_CDT })) {
                    accountNature = SFiscalConsts.COA_CDT;
                }
                else {
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nClase de cuenta desconocido para la "
                            + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
                }

                // Prepare accounts:

                parentAccountId = "";

                if (level == 1) {
                    fiscalAccountLinkDetails.add(createFiscalAccountLinkDetail(accountPk, linkType, referenceKey, fiscalAccountCode, accountId, parentAccountId, accountName, accountNature, level));
                }
                else {
                    switch (accountTypeId) {
                        case SModSysConsts.FINS_TP_ACC_BAL:
                            count = 1;
                            charArray = accountId.toCharArray();
                            for (i = 0; i < charArray.length; i++) {
                                parentAccountId += charArray[i] == '-' ? '-' : (count < level ? charArray[i] : '0');
                                if (charArray[i] == '-') {
                                    count++;
                                }
                            }

                            fiscalAccountLinkDetails.add(createFiscalAccountLinkDetail(accountPk, linkType, referenceKey, fiscalAccountCode, accountId, parentAccountId, accountName, accountNature, level));
                            break;

                        case SModSysConsts.FINS_TP_ACC_RES:
                            if (accountCode.isEmpty()) {
                                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nCódigo de cuenta desconocido para la "
                                        + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
                            }

                            referenceKey = new int[] { resultSet.getInt("f_itm_id") };
                            parentAccountId = accountId;
                            accountId += "." + accountCode;

                            // Compute account as income, if necessary:

                            if (hasAccountingMoves(statementAux, referenceKey[0], periodYear, periodMonth, CREDIT)) {
                                linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_INC;
                                fiscalAccountCode = resultSet.getString("f_itm_fis_acc_inc");

                                if (SLibUtils.parseDouble(fiscalAccountCode) == 0d) {
                                    fiscalAccountCode = resultSet.getString("f_itm_fis_acc_exp");
                                    if (SLibUtils.parseDouble(fiscalAccountCode) == 0d) {
                                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nCódigo agrupador del SAT (ingresos) no asignado a la "
                                                + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
                                    }
                                }

                                fiscalAccountLinkDetails.add(createFiscalAccountLinkDetail(accountPk, linkType, referenceKey, fiscalAccountCode, accountId, parentAccountId, accountName, accountNature, level));
                            }

                            // Compute account as expenses, if necessary:

                            if (hasAccountingMoves(statementAux, referenceKey[0], periodYear, periodMonth, DEBIT)) {
                                linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_EXP;
                                fiscalAccountCode = resultSet.getString("f_itm_fis_acc_exp");

                                if (SLibUtils.parseDouble(fiscalAccountCode) == 0d) {
                                    fiscalAccountCode = resultSet.getString("f_itm_fis_acc_inc");
                                    if (SLibUtils.parseDouble(fiscalAccountCode) == 0d) {
                                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nCódigo agrupador del SAT (egresos) no asignado a la "
                                                + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
                                    }
                                }

                                fiscalAccountLinkDetails.add(createFiscalAccountLinkDetail(accountPk, linkType, referenceKey, fiscalAccountCode, accountId, parentAccountId, accountName, accountNature, level));
                            }
                            break;

                        default:
                            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nTipo de cuenta desconocido para la "
                                    + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
                    }
                }
            }

            for (SDbFiscalAccountLinkDetail detail : fiscalAccountLinkDetails) {
                xmlCtas = createElementCatalogo11Ctas(detail);
                xmlDoc.getXmlElements().add(xmlCtas);
                fiscalAccountLink.getChildDetails().add(detail);
            }
        }

        fiscalAccountLink.save(session);

        return xmlDoc;
    }
    
    /**
     * Creates XML "Catálogo de cuentas utilizado en el período" 1.3.
     * @param session GUI user session.
     * @param periodYear Requested period's year.
     * @param periodMonth Requested period's month.
     */
    public static SXmlDocument createDocCatalogo13(final SGuiSession session, final int periodYear, final int periodMonth) throws Exception {
        boolean useFixedFiscalAccounts = false; // when 'true', treat system accounts (e.g., cash, banks, business partners, etc.) as simple fixed fiscal accounts, those ones provided by SAT, without any individual system subaccount. Otherwise, when 'false', treat each individual system subaccount as a fiscal subaccount
        HashSet<String> fixedFiscalAccounts = new HashSet<>();

        SXmlDocument xmlDoc = new SXmlDocument("catalogocuentas:Catalogo", false) {

            @Override
            public void processXml(String xml) throws Exception {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        xmlDoc.setCustomHeader(
                  "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:catalogocuentas=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_3/CatalogoCuentas\" "
                + "xsi:schemaLocation=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_3/CatalogoCuentas http://www.sat.gob.mx/esquemas/ContabilidadE/1_3/CatalogoCuentas/CatalogoCuentas_1_3.xsd\" ");

        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Version", "1.3"));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("RFC", ((SDbBizPartner) session.readRegistry(SModConsts.BPSU_BP, new int[] { session.getConfigCompany().getCompanyId() })).getFiscalId()));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Mes", SLibUtils.DecimalFormatCalendarMonth.format(periodMonth)));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Anio", SLibUtils.DecimalFormatCalendarYear.format(periodYear)));

        SDbFiscalAccountLink fiscalAccountLink = new SDbFiscalAccountLink();
        fiscalAccountLink.setPkYearId(periodYear);
        fiscalAccountLink.setPkPeriodId(periodMonth);

        /*
         * Obtain accounts as follows:
         * - Group 1. Input level accounts (all levels).
         * - Group 2. Level 1 accounts (ledger accounts) not included in Group 1.
         * - Group 3. Level 2 subaccounts not included in Group 1.
         */

        String sql = createQueryCatalogo13(session, periodYear, periodMonth);
        Statement statement = session.getStatement().getConnection().createStatement();
        Statement statementAux = session.getStatement().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            SDbFiscalAccountLinkDetail fiscalAccountLinkDetail = null;
            
            // Check if Fiscal Account provided by SAT was set on current account:

            String accountNature = "";
            String accountId = resultSet.getString("f_acc_id");
            String accountCode = resultSet.getString("f_acc_code");
            String accountName = resultSet.getString("f_acc_name");
            String fiscalAccountCode = resultSet.getString("f_fis_acc_code");
            int accountPk = resultSet.getInt("f_acc_pk");
            int accountDeep = resultSet.getInt("f_deep");
            int accountLevel = resultSet.getInt("f_lev");
            
            if (accountCode.isEmpty()) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nCódigo de cuenta desconocido para "
                        + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
            }
            if (SLibUtils.parseDouble(fiscalAccountCode) == 0) {
                throw new Exception("Código agrupador del SAT no configurado en "
                        + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
            }
            if (accountLevel == 0) {
                throw new Exception("Nivel de profundidad cero en "
                        + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
            }

            int accountTypeId = resultSet.getInt("f_tp_acc");
            int accountClassId = resultSet.getInt("f_cl_acc");
            int accountSubclassId = resultSet.getInt("f_cls_acc");
            boolean isItemRequired = resultSet.getBoolean("f_req_item");
            int linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_ACC;   // default link type
            int[] referenceKey = new int[] { accountPk };                 // default reference

            // check if current account is special:
            
            String fiscalAccountCodeSpecial = "";
            
            if (accountLevel > 1 && accountLevel > accountDeep && accountTypeId == SModSysConsts.FINS_TP_ACC_BAL) {
                int idBizPartner = SLibConsts.UNDEFINED;
                boolean isBizPartnerReq = false;
                boolean isAttEmployee = resultSet.getBoolean("f_bpr_emp");
                boolean isAttShareholder = resultSet.getBoolean("f_bpr_shh");
                boolean isAttRelatedParty = resultSet.getBoolean("f_bpr_rel");

                String fiscalIdBank = resultSet.getString("f_bnk_fis_id");
                if (fiscalIdBank == null) {
                    fiscalIdBank = "";
                }

                String fiscalIdBizPartner = resultSet.getString("f_bpr_fis_id");
                if (fiscalIdBizPartner == null) {
                    fiscalIdBizPartner = "";
                }
                
                String fiscalAccountAux = fiscalAccountCode.indexOf('.') == -1 ? fiscalAccountCode : fiscalAccountCode.substring(0, fiscalAccountCode.indexOf('.'));

                switch (fiscalAccountAux) {
                    case SFiscalConsts.ACC_CSH_CSH:
                        accountNature = SFiscalConsts.COA_DBT;
                        fiscalAccountCodeSpecial = SFiscalConsts.ACC_CSH_CSH_CSH;

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_CSH;
                        referenceKey = new int[] { resultSet.getInt("f_bnk_cob_id"), resultSet.getInt("f_bnk_ent_id") };
                        break;

                    case SFiscalConsts.ACC_CSH_BNK:
                        accountNature = SFiscalConsts.COA_DBT;
                        fiscalAccountCodeSpecial = fiscalIdBank.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_CSH_BNK_INT : SFiscalConsts.ACC_CSH_BNK_DOM;

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_BNK;
                        referenceKey = new int[] { resultSet.getInt("f_bnk_cob_id"), resultSet.getInt("f_bnk_ent_id") };
                        break;

                    case SFiscalConsts.ACC_BPR_CUS:
                        accountNature = SFiscalConsts.COA_DBT;
                        if (isAttRelatedParty) {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CUS_REL_INT : SFiscalConsts.ACC_BPR_CUS_REL_DOM;
                        }
                        else {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CUS_INT : SFiscalConsts.ACC_BPR_CUS_DOM;
                        }

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CUS;
                        isBizPartnerReq = true;
                        idBizPartner = resultSet.getInt("f_bpr_id");
                        referenceKey = new int[] { idBizPartner };
                        break;

                    case SFiscalConsts.ACC_BPR_DBR:
                        accountNature = SFiscalConsts.COA_DBT;
                        if (isAttEmployee) {
                            fiscalAccountCodeSpecial = SFiscalConsts.ACC_BPR_DBR_EMP;
                        }
                        else if (isAttShareholder) {
                            fiscalAccountCodeSpecial = SFiscalConsts.ACC_BPR_DBR_SHH;
                        }
                        else if (isAttRelatedParty) {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_DBR_REL_INT : SFiscalConsts.ACC_BPR_DBR_REL_DOM;
                        }
                        else {
                            fiscalAccountCodeSpecial = SFiscalConsts.ACC_BPR_DBR_OTH;
                        }

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_DBR;
                        isBizPartnerReq = true;
                        idBizPartner = resultSet.getInt("f_bpr_id");
                        referenceKey = new int[] { idBizPartner };
                        break;

                    case SFiscalConsts.ACC_BPR_SUP_ADV:
                        accountNature = SFiscalConsts.COA_DBT;
                        if (isAttRelatedParty) {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_SUP_ADV_REL_INT : SFiscalConsts.ACC_BPR_SUP_ADV_REL_DOM;
                        }
                        else {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_SUP_ADV_INT : SFiscalConsts.ACC_BPR_SUP_ADV_DOM;
                        }

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_SUP_ADV;
                        isBizPartnerReq = true;
                        idBizPartner = resultSet.getInt("f_bpr_id");
                        referenceKey = new int[] { idBizPartner };
                        break;

                    case SFiscalConsts.ACC_BPR_SUP:
                        accountNature = SFiscalConsts.COA_CDT;
                        if (isAttRelatedParty) {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_SUP_REL_INT : SFiscalConsts.ACC_BPR_SUP_REL_DOM;
                        }
                        else {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_SUP_INT : SFiscalConsts.ACC_BPR_SUP_DOM;
                        }

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_SUP;
                        isBizPartnerReq = true;
                        idBizPartner = resultSet.getInt("f_bpr_id");
                        referenceKey = new int[] { idBizPartner };
                        break;

                    case SFiscalConsts.ACC_BPR_CDR_ST:
                        accountNature = SFiscalConsts.COA_CDT;
                        if (isAttShareholder) {
                            fiscalAccountCodeSpecial = SFiscalConsts.ACC_BPR_CDR_ST_SHH;
                        }
                        else if (isAttRelatedParty) {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CDR_ST_REL_INT : SFiscalConsts.ACC_BPR_CDR_ST_REL_DOM;
                        }
                        else {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CDR_ST_INT : SFiscalConsts.ACC_BPR_CDR_ST_DOM;
                        }

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CDR;
                        isBizPartnerReq = true;
                        idBizPartner = resultSet.getInt("f_bpr_id");
                        referenceKey = new int[] { idBizPartner };
                        break;

                    case SFiscalConsts.ACC_BPR_CUS_ADV:
                        accountNature = SFiscalConsts.COA_CDT;
                        if (isAttRelatedParty) {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CUS_ADV_REL_INT : SFiscalConsts.ACC_BPR_CUS_ADV_REL_DOM;
                        }
                        else {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CUS_ADV_INT : SFiscalConsts.ACC_BPR_CUS_ADV_DOM;
                        }

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CUS_ADV;
                        isBizPartnerReq = true;
                        idBizPartner = resultSet.getInt("f_bpr_id");
                        referenceKey = new int[] { idBizPartner };
                        break;

                    case SFiscalConsts.ACC_BPR_CDR_LT:
                        accountNature = SFiscalConsts.COA_CDT;
                        if (isAttShareholder) {
                            fiscalAccountCodeSpecial = SFiscalConsts.ACC_BPR_CDR_LT_SHH;
                        }
                        else if (isAttRelatedParty) {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CDR_LT_REL_INT : SFiscalConsts.ACC_BPR_CDR_LT_REL_DOM;
                        }
                        else {
                            fiscalAccountCodeSpecial = fiscalIdBizPartner.compareTo(DCfdConsts.RFC_GEN_INT) == 0 ? SFiscalConsts.ACC_BPR_CDR_LT_INT : SFiscalConsts.ACC_BPR_CDR_LT_DOM;
                        }

                        linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CDR;
                        isBizPartnerReq = true;
                        idBizPartner = resultSet.getInt("f_bpr_id");
                        referenceKey = new int[] { idBizPartner };
                        break;

                    default:
                        throw new Exception("Código agrupador del SAT incorrecto (" + fiscalAccountCode + ") en "
                                + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").\n"
                                + "Dicho código agrupador del SAT debe provenir de alguno de los siguientes, a 1er. o 2do. nivel:\n"
                                + SFiscalConsts.ACC_CSH_CSH + ", "
                                + SFiscalConsts.ACC_CSH_BNK + ", "
                                + SFiscalConsts.ACC_BPR_CUS + ", "
                                + SFiscalConsts.ACC_BPR_DBR + ", "
                                + SFiscalConsts.ACC_BPR_SUP_ADV + ", "
                                + SFiscalConsts.ACC_BPR_SUP + ", "
                                + SFiscalConsts.ACC_BPR_CDR_ST + ", "
                                + SFiscalConsts.ACC_BPR_CUS_ADV + ", "
                                + SFiscalConsts.ACC_BPR_CDR_LT + ".");
                }
                
                if (isBizPartnerReq && idBizPartner == SLibConsts.UNDEFINED) {
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nAsociado de negocios no definido en "
                            + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
                }
            }

            // Create fiscal account link details if necessary:

            if (!fiscalAccountCodeSpecial.isEmpty()) {
                if (!useFixedFiscalAccounts || (useFixedFiscalAccounts && !fixedFiscalAccounts.contains(fiscalAccountCodeSpecial))) {
                    // Prepare account:

                    if (useFixedFiscalAccounts)  {
                        fixedFiscalAccounts.add(fiscalAccountCodeSpecial);
                        accountCode = accountId + fiscalAccountCodeSpecial.substring(fiscalAccountCodeSpecial.indexOf('.'));
                        accountName = getFiscalAccountNameByCode(statementAux, fiscalAccountCodeSpecial).toUpperCase();
                    }
                    else {
                        accountCode = accountId + "." + DecimalFormatBizPartner.format(referenceKey[0]) + (referenceKey.length == 2 ? "." + DecimalFormatEntity.format(referenceKey[1]) : "");
                    }

                    fiscalAccountLinkDetail = createFiscalAccountLinkDetail(accountPk, linkType, referenceKey, fiscalAccountCodeSpecial, accountCode, accountId, accountName, accountNature, accountLevel);
                }
            }
            else {
                // Make up fiscal account's code if necessary:

                // Define account's nature:

                int[] accountClassKey = new int[] { accountTypeId, accountClassId };
                int[] accountSubclassKey = new int[] { accountTypeId, accountClassId, accountSubclassId };

                if (SLibUtils.belongsTo(accountClassKey, new int[][] { SModSysConsts.FINS_CL_ACC_ASSET, SModSysConsts.FINS_CL_ACC_ORD_DBT, SModSysConsts.FINS_CL_ACC_RES_DBT })) {
                    accountNature = SFiscalConsts.COA_DBT;
                }
                else if (SLibUtils.belongsTo(accountClassKey, new int[][] { SModSysConsts.FINS_CL_ACC_LIABTY, SModSysConsts.FINS_CL_ACC_EQUITY, SModSysConsts.FINS_CL_ACC_ORD_CDT, SModSysConsts.FINS_CL_ACC_RES_CDT })) {
                    accountNature = SFiscalConsts.COA_CDT;
                }
                else {
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nClase de cuenta desconocida para "
                            + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
                }

                // Prepare accounts:

                String parentAccountId = "";

                if (accountLevel == 1) {
                    fiscalAccountLinkDetail = createFiscalAccountLinkDetail(accountPk, linkType, referenceKey, fiscalAccountCode, accountId, parentAccountId, accountName, accountNature, accountLevel);
                }
                else {
                    boolean isAccSal;
                    boolean isAccSalAdj;
                    boolean isAccPur;
                    boolean isAccPurAdj;
                    boolean isAccCogs;
                    
                    int count = 1;
                    char[] charArray = accountId.toCharArray();
                    for (int i = 0; i < charArray.length; i++) {
                        parentAccountId += charArray[i] == '-' ? '-' : (count < accountLevel ? charArray[i] : '0');
                        if (charArray[i] == '-') {
                            count++;
                        }
                    }
                    
                    if (accountTypeId == SModSysConsts.FINS_TP_ACC_BAL || (accountTypeId == SModSysConsts.FINS_TP_ACC_RES && !isItemRequired)) {
                        // standard balance accounts and results accounts not requiring items:
                        
                        fiscalAccountLinkDetail = createFiscalAccountLinkDetail(accountPk, linkType, referenceKey, fiscalAccountCode, accountId, parentAccountId, accountName, accountNature, accountLevel);
                    }
                    else if (accountTypeId == SModSysConsts.FINS_TP_ACC_RES && isItemRequired) {
                        // results accounts requiring items:
                        
                        isAccSal = SLibUtils.compareKeys(accountSubclassKey, SDataConstantsSys.FINS_CLS_ACC_SAL);
                        isAccSalAdj = SLibUtils.compareKeys(accountSubclassKey, SDataConstantsSys.FINS_CLS_ACC_SAL_ADJ);
                        isAccPur = SLibUtils.compareKeys(accountSubclassKey, SDataConstantsSys.FINS_CLS_ACC_PUR);
                        isAccPurAdj = SLibUtils.compareKeys(accountSubclassKey, SDataConstantsSys.FINS_CLS_ACC_PUR_ADJ);
                        isAccCogs = SLibUtils.compareKeys(accountSubclassKey, SDataConstantsSys.FINS_CLS_ACC_COGS);

                        switch (accountNature) {
                            case SFiscalConsts.COA_CDT:
                                linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_INC;
                                break;

                            case SFiscalConsts.COA_DBT:
                                linkType = SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_EXP;
                                break;

                            default:
                        }

                        if (accountLevel > 1 && accountLevel > accountDeep) {
                            referenceKey = new int[] { resultSet.getInt("f_itm_id") };
                            parentAccountId = accountId;
                            accountId += "." + accountCode;

                            // Compute account as income, if necessary:

                            switch (accountNature) {
                                case SFiscalConsts.COA_CDT:
                                    fiscalAccountCode = resultSet.getString("f_itm_fis_acc_inc") == null ? "" : resultSet.getString("f_itm_fis_acc_inc");

                                    if (SLibUtils.parseDouble(fiscalAccountCode) == 0d) {
                                        fiscalAccountCode = resultSet.getString("f_itm_fis_acc_exp") == null ? "" : resultSet.getString("f_itm_fis_acc_exp");
                                        if (SLibUtils.parseDouble(fiscalAccountCode) == 0d) {
                                            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nCódigo agrupador del SAT (ingresos) no configurado en "
                                                    + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
                                        }
                                    }

                                    // fix fiscal account code if it is not suitable (when sales or purchases adjustments):

                                    if (isAccSal && !SFiscalAccountUtils.isFiscalAccountCodeSal(fiscalAccountCode)) {
                                        fiscalAccountCode = SFiscalAccountUtils.DEF_CODE_SAL;
                                    }
                                    else if (isAccPurAdj && !SFiscalAccountUtils.isFiscalAccountCodePurAdj(fiscalAccountCode)) {
                                        fiscalAccountCode = SFiscalAccountUtils.DEF_CODE_PUR_ADJ;
                                    }
                                    break;

                                case SFiscalConsts.COA_DBT:
                                    fiscalAccountCode = resultSet.getString("f_itm_fis_acc_exp") == null ? "" : resultSet.getString("f_itm_fis_acc_exp");

                                    if (SLibUtils.parseDouble(fiscalAccountCode) == 0d) {
                                        fiscalAccountCode = resultSet.getString("f_itm_fis_acc_inc") == null ? "" : resultSet.getString("f_itm_fis_acc_inc");
                                        if (SLibUtils.parseDouble(fiscalAccountCode) == 0d) {
                                            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nCódigo agrupador del SAT (egresos) no configurado en "
                                                    + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
                                        }
                                    }

                                    // fix fiscal account code if it is not suitable (when purchases, sales adjustments or COGS):

                                    if (isAccPur && !SFiscalAccountUtils.isFiscalAccountCodePur(fiscalAccountCode)) {
                                        fiscalAccountCode = SFiscalAccountUtils.DEF_CODE_PUR;
                                    }
                                    else if (isAccSalAdj && !SFiscalAccountUtils.isFiscalAccountCodeSalAdj(fiscalAccountCode)) {
                                        fiscalAccountCode = SFiscalAccountUtils.DEF_CODE_SAL_ADJ;
                                    }
                                    else if (isAccCogs && !SFiscalAccountUtils.isFiscalAccountCodeCogs(fiscalAccountCode)) {
                                        fiscalAccountCode = SFiscalAccountUtils.DEF_CODE_COGS;
                                    }
                                    break;

                                default:
                            }
                        }

                        fiscalAccountLinkDetail = createFiscalAccountLinkDetail(accountPk, linkType, referenceKey, fiscalAccountCode, accountId, parentAccountId, accountName, accountNature, accountLevel);
                    }
                    else {
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nTipo de cuenta desconocido para la "
                                + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
                    }
                }
            }

            if (fiscalAccountLinkDetail == null) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNo fue posible crear el nodo XML para la "
                        + "cuenta contable " + accountId + ":\n'" + accountName + "' (" + accountCode + ").");
            }
            else {
                fiscalAccountLink.getChildDetails().add(fiscalAccountLinkDetail);
                xmlDoc.getXmlElements().add(createElementCatalogo13Ctas(fiscalAccountLinkDetail));
            }
        }

        fiscalAccountLink.save(session);

        return xmlDoc;
    }
    
    /*
     * XML: Balanza de comprobación.
     */

    /**
     * Gets the more recent available chart of accounts.
     * @param session User GUI session.
     */
    public static int[] getLastChartOfAccounts(final SGuiSession session) throws Exception {
        int[] period = null;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_year AS f_year, MAX(id_per) AS f_month "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK) + " "
                + "WHERE id_year = (SELECT MAX(id_year) FROM " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK) + ") "
                + "GROUP BY f_year ";

        resultSet = session.getStatement().executeQuery(sql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + "\nConfiguración para contabilidad en medios electrónicos.");
        }
        else {
            period = new int[] { resultSet.getInt(1), resultSet.getInt(2) };
        }

        return period;
    }

    /**
     * Gets the suitablest chart of accounts for requested year and month.
     * @param session User GUI session.
     * @param reqYear Requested year.
     * @param reqMonth Requested month.
     */
    public static int[] getSuitableChartOfAccounts(final SGuiSession session, final int reqYear, final int reqMonth) throws Exception {
        int[] period = null;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_year AS f_year, MAX(id_per) AS f_month "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK) + " "
                + "WHERE (id_year = " + reqYear + " AND id_per <= " + reqMonth + ") OR id_year < " + reqYear + " "
                + "GROUP BY id_year "
                + "ORDER BY f_year DESC "
                + "LIMIT 1";

        resultSet = session.getStatement().executeQuery(sql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + "\nConfiguración para contabilidad en medios electrónicos.");
        }
        else {
            period = new int[] { resultSet.getInt(1), resultSet.getInt(2) };
        }

        return period;
    }

    private static SXmlElement createElementBalanza11Ctas(final String numCta, final double saldoIni, final double debe, final double haber, final double saldoFin) {
        SXmlElement element = new SXmlElement("BCE:Ctas");

        element.getXmlAttributes().add(new SXmlAttribute("NumCta", numCta));
        element.getXmlAttributes().add(new SXmlAttribute("SaldoIni", DecimalFormatImporte.format(saldoIni == 0d ? 0d : saldoIni)));
        element.getXmlAttributes().add(new SXmlAttribute("Debe", DecimalFormatImporte.format(debe == 0d ? 0d : debe)));
        element.getXmlAttributes().add(new SXmlAttribute("Haber", DecimalFormatImporte.format(haber == 0d ? 0d : haber)));
        element.getXmlAttributes().add(new SXmlAttribute("SaldoFin", DecimalFormatImporte.format(saldoFin == 0d ? 0d : saldoFin)));

        return element;
    }
    
    private static SXmlElement createElementBalanza13Ctas(final String numCta, final double saldoIni, final double debe, final double haber, final double saldoFin) {
        SXmlElement element = new SXmlElement("BCE:Ctas");

        element.getXmlAttributes().add(new SXmlAttribute("NumCta", numCta));
        element.getXmlAttributes().add(new SXmlAttribute("SaldoIni", DecimalFormatImporte.format(saldoIni == 0d ? 0d : saldoIni)));
        element.getXmlAttributes().add(new SXmlAttribute("Debe", DecimalFormatImporte.format(debe == 0d ? 0d : debe)));
        element.getXmlAttributes().add(new SXmlAttribute("Haber", DecimalFormatImporte.format(haber == 0d ? 0d : haber)));
        element.getXmlAttributes().add(new SXmlAttribute("SaldoFin", DecimalFormatImporte.format(saldoFin == 0d ? 0d : saldoFin)));

        return element;
    }

    public static String createQueryBalanza11(final SGuiSession session, final int periodYear, final int periodMonth, final int coaYear, final int coaMonth) throws Exception {
        String sql = "";
        String balDate = "";

        if (periodYear < SFiscalConsts.YEAR_MIN || periodYear > SLibTimeConsts.YEAR_MAX) {
            throw new Exception("El atributo 'Anio' debe ser mínimo " + SFiscalConsts.YEAR_MIN + " y máximo " + SLibTimeConsts.YEAR_MAX + ".");
        }

        if (periodMonth < SLibTimeConsts.MONTH_MIN || periodMonth > SLibTimeConsts.MONTH_MAX) {
            throw new Exception("El atributo 'Mes' debe ser mínimo " + SLibTimeConsts.MONTH_MIN + " y máximo " + SLibTimeConsts.MONTH_MAX + ".");
        }

        balDate = SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.createDate(periodYear, periodMonth));

        /*
         * Obtain trial balance as follows:
         * - Group 1. Cash accounts.
         * - Group 2. Business partner accounts.
         * - Group 3. Results accounts.
         * - Group 4. Remaining balance accounts.
         */

        sql = "SELECT "
                + "a.id_acc AS f_acc_id, "
                + "al.fid_tp_acc_r AS f_acc_tp_id, "
                + "al.fid_cl_acc_r AS f_acc_cl_id, "
                + "al.fid_tp_acc_spe AS f_acc_spe_id, "
                + "re.fid_cob_n AS f_ref_1, "
                + "re.fid_ent_n AS f_ref_2, "
                + "fad.acc_code AS f_acc_code, "
                + "ce.ent AS f_acc_name, "
                + "fad.nat AS f_acc_nat, "
                + "SUM(IF(r.dt < '" + balDate + "', re.debit - re.credit, 0.0)) AS f_bal_ope, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.debit, 0.0)) AS f_dbt, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.credit, 0.0)) AS f_cdt, "
                + "SUM(re.debit - re.credit) AS f_bal_clo "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON "
                + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON a.id_acc = re.fid_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_BAL + " AND "
                + "al.fid_tp_acc_spe = " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fad ON fad.id_year = " + coaYear + " AND fad.id_per = " + coaMonth + " AND "
                + "fad.id_tp_fiscal_acc_link BETWEEN " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_CSH + " AND " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_BNK + " AND fad.id_ref_1 = re.fid_cob_n AND fad.id_ref_2 = re.fid_ent_n "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS ce ON ce.id_cob = re.fid_cob_n AND ce.id_ent = re.fid_ent_n "
                + "WHERE "
                + "r.b_del = 0 AND re.b_del = 0 AND r.id_year = " + periodYear + " AND r.id_per <= " + periodMonth + " "
                + "GROUP BY f_acc_id, f_acc_tp_id, f_acc_cl_id, f_acc_spe_id, f_ref_1, f_ref_2, f_acc_code, f_acc_name, f_acc_nat "
                + "HAVING f_bal_ope <> 0 OR f_dbt <> 0 OR f_cdt <> 0 OR f_bal_clo <> 0 "
                + ""
                + "UNION "
                + ""
                + "SELECT "
                + "a.id_acc AS f_acc_id, "
                + "al.fid_tp_acc_r AS f_acc_tp_id, "
                + "al.fid_cl_acc_r AS f_acc_cl_id, "
                + "al.fid_tp_acc_spe AS f_acc_spe_id, "
                + "re.fid_bp_nr AS f_ref_1, "
                + "" + SLibConsts.UNDEFINED + " AS f_ref_2, "
                + "fad.acc_code AS f_acc_code, "
                + "b.bp AS f_acc_name, "
                + "fad.nat AS f_acc_nat, "
                + "SUM(IF(r.dt < '" + balDate + "', re.debit - re.credit, 0.0)) AS f_bal_ope, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.debit, 0.0)) AS f_dbt, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.credit, 0.0)) AS f_cdt, "
                + "SUM(re.debit - re.credit) AS f_bal_clo "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON "
                + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON a.id_acc = re.fid_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_BAL + " AND "
                + "al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fad ON fad.id_year = " + coaYear + " AND fad.id_per = " + coaMonth + " AND "
                + "fad.id_tp_fiscal_acc_link BETWEEN " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_SUP + " AND " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CUS_ADV + " AND fad.id_ref_1 = re.fid_bp_nr AND fad.id_ref_2 = " + SLibConsts.UNDEFINED + " AND fad.id_acc = a.pk_acc "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = re.fid_bp_nr "
                + "WHERE "
                + "r.b_del = 0 AND re.b_del = 0 AND r.id_year = " + periodYear + " AND r.id_per <= " + periodMonth + " "
                + "GROUP BY f_acc_id, f_acc_tp_id, f_acc_cl_id, f_acc_spe_id, f_ref_1, f_ref_2, f_acc_code, f_acc_name, f_acc_nat "
                + "HAVING f_bal_ope <> 0 OR f_dbt <> 0 OR f_cdt <> 0 OR f_bal_clo <> 0 "
                + ""
                + "UNION "
                + ""
                + "SELECT "
                + "a.id_acc AS f_acc_id, "
                + "al.fid_tp_acc_r AS f_acc_tp_id, "
                + "al.fid_cl_acc_r AS f_acc_cl_id, "
                + "al.fid_tp_acc_spe AS f_acc_spe_id, "
                + "re.fid_item_n AS f_ref_1, "
                + "" + SLibConsts.UNDEFINED + " AS f_ref_2, "
                + "fad.acc_code AS f_acc_code, "
                + "i.item AS f_acc_name, "
                + "fad.nat AS f_acc_nat, "
                + "SUM(IF(r.dt < '" + balDate + "', re.debit - re.credit, 0.0)) AS f_bal_ope, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.debit, 0.0)) AS f_dbt, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.credit, 0.0)) AS f_cdt, "
                + "SUM(re.debit - re.credit) AS f_bal_clo "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON "
                + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON a.id_acc = re.fid_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fad ON fad.id_year = " + coaYear + " AND fad.id_per = " + coaMonth + " AND "
                + "fad.id_tp_fiscal_acc_link BETWEEN " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_INC + " AND " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_EXP + " AND fad.id_ref_1 = re.fid_item_n AND fad.id_ref_2 = " + SLibConsts.UNDEFINED + " AND fad.id_acc = a.pk_acc "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON i.id_item = re.fid_item_n "
                + "WHERE "
                + "r.b_del = 0 AND re.b_del = 0 AND r.id_year = " + periodYear + " AND r.id_per <= " + periodMonth + " "
                + "GROUP BY f_acc_id, f_acc_tp_id, f_acc_cl_id, f_acc_spe_id, f_ref_1, f_ref_2, f_acc_code, f_acc_name, f_acc_nat "
                + "HAVING f_bal_ope <> 0 OR f_dbt <> 0 OR f_cdt <> 0 OR f_bal_clo <> 0 "
                + ""
                + "UNION "
                + ""
                + "SELECT "
                + "a.id_acc AS f_acc_id, "
                + "al.fid_tp_acc_r AS f_acc_tp_id, "
                + "al.fid_cl_acc_r AS f_acc_cl_id, "
                + "al.fid_tp_acc_spe AS f_acc_spe_id, "
                + "a.pk_acc AS f_ref_1, "
                + "" + SLibConsts.UNDEFINED + " AS f_ref_2, "
                + "fad.acc_code AS f_acc_code, "
                + "a.acc AS f_acc_name, "
                + "fad.nat AS f_acc_nat, "
                + "SUM(IF(r.dt < '" + balDate + "', re.debit - re.credit, 0.0)) AS f_bal_ope, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.debit, 0.0)) AS f_dbt, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.credit, 0.0)) AS f_cdt, "
                + "SUM(re.debit - re.credit) AS f_bal_clo "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON "
                + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON a.id_acc = re.fid_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_BAL + " AND "
                + "al.fid_tp_acc_spe <> " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " AND al.fid_tp_acc_spe NOT BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fad ON fad.id_year = " + coaYear + " AND fad.id_per = " + coaMonth + " AND "
                + "fad.id_tp_fiscal_acc_link = " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_ACC + " AND fad.id_ref_1 = a.pk_acc AND fad.id_ref_2 = " + SLibConsts.UNDEFINED + " "
                + "WHERE "
                + "r.b_del = 0 AND re.b_del = 0 AND r.id_year = " + periodYear + " AND r.id_per <= " + periodMonth + " "
                + "GROUP BY f_acc_id, f_acc_tp_id, f_acc_cl_id, f_acc_spe_id, f_ref_1, f_ref_2, f_acc_code, f_acc_name, f_acc_nat "
                + ""
                + "ORDER BY f_acc_id, f_acc_name, f_acc_code, f_acc_spe_id, f_ref_1, f_ref_2; ";

        return sql;
    }
    
    public static String createQueryBalanza13(final SGuiSession session, final int periodYear, final int periodMonth, final int coaYear, final int coaMonth) throws Exception {
        int month = 0;
        String sql = "";
        String balDate = "";
        String whereDec = "";
        
        // adjust calendar month if necesary:

        if (periodMonth <= SLibTimeConsts.MONTH_MAX) {
            month = periodMonth;
        }
        else if (periodMonth == (SLibTimeConsts.MONTH_MAX + 1)) {
            month = SLibTimeConsts.MONTH_MAX;
        }
        
        // exclude journal vouchers of closing and audit adjustments only in december:
        
        if (periodMonth == SLibTimeConsts.MONTH_MAX) {
            whereDec = "AND NOT b_adj_year AND NOT b_adj_audit ";
        }
        
        // validate year and month:
        
        if (periodYear < SFiscalConsts.YEAR_MIN || periodYear > SLibTimeConsts.YEAR_MAX) {
            throw new Exception("El atributo 'Anio' debe ser mínimo " + SFiscalConsts.YEAR_MIN + " y máximo " + SLibTimeConsts.YEAR_MAX + ".");
        }

        if (month < SLibTimeConsts.MONTH_MIN || month > SLibTimeConsts.MONTH_MAX) {
            throw new Exception("El atributo 'Mes' debe ser mínimo " + SLibTimeConsts.MONTH_MIN + " y máximo " + SLibTimeConsts.MONTH_MAX + ".");
        }

        balDate = SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.createDate(periodYear, month));

        /*
         * Obtain trial balance as follows:
         * - Group 1. Cash accounts.
         * - Group 2. Business partner accounts.
         * - Group 3. Results accounts (income).
         * - Group 4. Results accounts (expenses).
         * - Group 5. Remaining balance accounts.
         */

        sql = "SELECT "
                + "a.id_acc AS f_acc_id, "
                + "al.fid_tp_acc_r AS f_acc_tp_id, "
                + "al.fid_cl_acc_r AS f_acc_cl_id, "
                + "al.fid_tp_acc_spe AS f_acc_spe_id, "
                + "re.fid_cob_n AS f_ref_1, "
                + "re.fid_ent_n AS f_ref_2, "
                + "fad.acc_code AS f_acc_code, "
                + "ce.ent AS f_acc_name, "
                + "fad.nat AS f_acc_nat, "
                + "SUM(IF(r.dt < '" + balDate + "', re.debit - re.credit, 0.0)) AS f_bal_ope, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.debit, 0.0)) AS f_dbt, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.credit, 0.0)) AS f_cdt, "
                + "SUM(re.debit - re.credit) AS f_bal_clo "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON "
                + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON a.id_acc = re.fid_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_BAL + " AND "
                + "al.fid_tp_acc_spe = " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fad ON fad.id_year = " + coaYear + " AND fad.id_per = " + coaMonth + " AND "
                + "fad.id_tp_fiscal_acc_link BETWEEN " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_CSH + " AND " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_BNK + " AND fad.id_ref_1 = re.fid_cob_n AND fad.id_ref_2 = re.fid_ent_n "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS ce ON ce.id_cob = re.fid_cob_n AND ce.id_ent = re.fid_ent_n "
                + "WHERE "
                + "r.b_del = 0 AND re.b_del = 0 AND r.id_year = " + periodYear + " AND r.id_per <= " + month + " " + whereDec
                + "GROUP BY f_acc_id, f_acc_tp_id, f_acc_cl_id, f_acc_spe_id, f_ref_1, f_ref_2, f_acc_code, f_acc_name, f_acc_nat "
                + "HAVING f_bal_ope <> 0 OR f_dbt <> 0 OR f_cdt <> 0 OR f_bal_clo <> 0 "
                + ""
                + "UNION "
                + ""
                + "SELECT "
                + "a.id_acc AS f_acc_id, "
                + "al.fid_tp_acc_r AS f_acc_tp_id, "
                + "al.fid_cl_acc_r AS f_acc_cl_id, "
                + "al.fid_tp_acc_spe AS f_acc_spe_id, "
                + "re.fid_bp_nr AS f_ref_1, "
                + "" + SLibConsts.UNDEFINED + " AS f_ref_2, "
                + "fad.acc_code AS f_acc_code, "
                + "b.bp AS f_acc_name, "
                + "fad.nat AS f_acc_nat, "
                + "SUM(IF(r.dt < '" + balDate + "', re.debit - re.credit, 0.0)) AS f_bal_ope, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.debit, 0.0)) AS f_dbt, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.credit, 0.0)) AS f_cdt, "
                + "SUM(re.debit - re.credit) AS f_bal_clo "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON "
                + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON a.id_acc = re.fid_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_BAL + " AND "
                + "al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fad ON fad.id_year = " + coaYear + " AND fad.id_per = " + coaMonth + " AND "
                + "fad.id_tp_fiscal_acc_link BETWEEN " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_SUP + " AND " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CUS_ADV + " AND fad.id_ref_1 = re.fid_bp_nr AND fad.id_ref_2 = " + SLibConsts.UNDEFINED + " AND fad.id_acc = a.pk_acc "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = re.fid_bp_nr "
                + "WHERE "
                + "r.b_del = 0 AND re.b_del = 0 AND r.id_year = " + periodYear + " AND r.id_per <= " + month + " " + whereDec
                + "GROUP BY f_acc_id, f_acc_tp_id, f_acc_cl_id, f_acc_spe_id, f_ref_1, f_ref_2, f_acc_code, f_acc_name, f_acc_nat "
                + "HAVING f_bal_ope <> 0 OR f_dbt <> 0 OR f_cdt <> 0 OR f_bal_clo <> 0 "
                + ""
                + "UNION "
                + ""
                + "SELECT "
                + "a.id_acc AS f_acc_id, "
                + "al.fid_tp_acc_r AS f_acc_tp_id, "
                + "al.fid_cl_acc_r AS f_acc_cl_id, "
                + "al.fid_tp_acc_spe AS f_acc_spe_id, "
                + "re.fid_item_n AS f_ref_1, "
                + "" + SLibConsts.UNDEFINED + " AS f_ref_2, "
                + "fad.acc_code AS f_acc_code, "
                + "CONCAT(i.item_key, ' ', i.item) AS f_acc_name, "
                + "fad.nat AS f_acc_nat, "
                + "SUM(IF(r.dt < '" + balDate + "', re.debit - re.credit, 0.0)) AS f_bal_ope, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.debit, 0.0)) AS f_dbt, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.credit, 0.0)) AS f_cdt, "
                + "SUM(re.debit - re.credit) AS f_bal_clo "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON "
                + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON a.id_acc = re.fid_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " AND al.b_req_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fad ON fad.id_year = " + coaYear + " AND fad.id_per = " + coaMonth + " AND "
                + "fad.id_tp_fiscal_acc_link = " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_INC + " AND fad.id_ref_1 = re.fid_item_n AND fad.id_ref_2 = " + SLibConsts.UNDEFINED + " AND fad.id_acc = a.pk_acc "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON i.id_item = re.fid_item_n "
                + "WHERE "
                + "r.b_del = 0 AND re.b_del = 0 AND r.id_year = " + periodYear + " AND r.id_per <= " + month + " " + whereDec
                + "GROUP BY f_acc_id, f_acc_tp_id, f_acc_cl_id, f_acc_spe_id, f_ref_1, f_ref_2, f_acc_code, f_acc_name, f_acc_nat "
                + "HAVING f_bal_ope <> 0 OR f_dbt <> 0 OR f_cdt <> 0 OR f_bal_clo <> 0 "
                + ""
                + "UNION "
                + ""
                + "SELECT "
                + "a.id_acc AS f_acc_id, "
                + "al.fid_tp_acc_r AS f_acc_tp_id, "
                + "al.fid_cl_acc_r AS f_acc_cl_id, "
                + "al.fid_tp_acc_spe AS f_acc_spe_id, "
                + "re.fid_item_n AS f_ref_1, "
                + "" + SLibConsts.UNDEFINED + " AS f_ref_2, "
                + "fad.acc_code AS f_acc_code, "
                + "CONCAT(i.item_key, ' ', i.item) AS f_acc_name, "
                + "fad.nat AS f_acc_nat, "
                + "SUM(IF(r.dt < '" + balDate + "', re.debit - re.credit, 0.0)) AS f_bal_ope, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.debit, 0.0)) AS f_dbt, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.credit, 0.0)) AS f_cdt, "
                + "SUM(re.debit - re.credit) AS f_bal_clo "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON "
                + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON a.id_acc = re.fid_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " AND al.b_req_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fad ON fad.id_year = " + coaYear + " AND fad.id_per = " + coaMonth + " AND "
                + "fad.id_tp_fiscal_acc_link = " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_EXP + " AND fad.id_ref_1 = re.fid_item_n AND fad.id_ref_2 = " + SLibConsts.UNDEFINED + " AND fad.id_acc = a.pk_acc "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON i.id_item = re.fid_item_n "
                + "WHERE "
                + "r.b_del = 0 AND re.b_del = 0 AND r.id_year = " + periodYear + " AND r.id_per <= " + month + " " + whereDec
                + "GROUP BY f_acc_id, f_acc_tp_id, f_acc_cl_id, f_acc_spe_id, f_ref_1, f_ref_2, f_acc_code, f_acc_name, f_acc_nat "
                + "HAVING f_bal_ope <> 0 OR f_dbt <> 0 OR f_cdt <> 0 OR f_bal_clo <> 0 "
                + ""
                + "UNION "
                + ""
                + "SELECT "
                + "a.id_acc AS f_acc_id, "
                + "al.fid_tp_acc_r AS f_acc_tp_id, "
                + "al.fid_cl_acc_r AS f_acc_cl_id, "
                + "al.fid_tp_acc_spe AS f_acc_spe_id, "
                + "a.pk_acc AS f_ref_1, "
                + "" + SLibConsts.UNDEFINED + " AS f_ref_2, "
                + "fad.acc_code AS f_acc_code, "
                + "a.acc AS f_acc_name, "
                + "fad.nat AS f_acc_nat, "
                + "SUM(IF(r.dt < '" + balDate + "', re.debit - re.credit, 0.0)) AS f_bal_ope, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.debit, 0.0)) AS f_dbt, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.credit, 0.0)) AS f_cdt, "
                + "SUM(re.debit - re.credit) AS f_bal_clo "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON "
                + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON a.id_acc = re.fid_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND (al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_BAL + " OR (al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " AND NOT al.b_req_item)) AND "
                + "al.fid_tp_acc_spe <> " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " AND al.fid_tp_acc_spe NOT BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fad ON fad.id_year = " + coaYear + " AND fad.id_per = " + coaMonth + " AND "
                + "fad.id_tp_fiscal_acc_link = " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_ACC + " AND fad.id_ref_1 = a.pk_acc AND fad.id_ref_2 = " + SLibConsts.UNDEFINED + " "
                + "WHERE "
                + "r.b_del = 0 AND re.b_del = 0 AND r.id_year = " + periodYear + " AND r.id_per <= " + month + " " + whereDec
                + "GROUP BY f_acc_id, f_acc_tp_id, f_acc_cl_id, f_acc_spe_id, f_ref_1, f_ref_2, f_acc_code, f_acc_name, f_acc_nat "
                + "HAVING f_bal_ope <> 0 OR f_dbt <> 0 OR f_cdt <> 0 OR f_bal_clo <> 0 "
                + ""
                + "ORDER BY f_acc_id, f_acc_name, f_acc_spe_id, f_ref_1, f_ref_2; ";

         return sql;
    }

    /**
     * Creates XML "Balanza de comprobación" 1.1.
     * @param session GUI user session.
     * @param periodYear Requested period's year.
     * @param periodMonth Requested period's month.
     * @param balanceType Trial balance type. Constants defined in <code>SFiscalConsts.BAL_...</code>.
     * @param lastModification Last accounting modification, when trial balance type is "complement", i.e., <code>SFiscalConsts.TRS_CMP</code>). Otherwise <code>null</code> must be provided.
     */
    public static SXmlDocument createDocBalanza11(final SGuiSession session, final int periodYear, final int periodMonth, final String balanceType, final Date lastModification) throws Exception {
        double sign = 0d;
        int[] coaPeriod = null;
        int[] accountClassKey = null;
        String sql = "";
        String accountCode = "";
        String[] months = null;
        Statement statement = null;
        ResultSet resultSet = null;
        SDbBizPartner company = null;
        SXmlDocument xmlDoc = null;
        SXmlElement xmlCtas = null;

        if (periodYear < SFiscalConsts.YEAR_MIN || periodYear > SLibTimeConsts.YEAR_MAX) {
            throw new Exception("El atributo 'Anio' debe ser mínimo " + SFiscalConsts.YEAR_MIN + " y máximo " + SLibTimeConsts.YEAR_MAX + ".");
        }

        if (periodMonth < SLibTimeConsts.MONTH_MIN || periodMonth > SLibTimeConsts.MONTH_MAX) {
            throw new Exception("El atributo 'Mes' debe ser mínimo " + SLibTimeConsts.MONTH_MIN + " y máximo " + SLibTimeConsts.MONTH_MAX + ".");
        }

        company = (SDbBizPartner) session.readRegistry(SModConsts.BPSU_BP, new int[] { session.getConfigCompany().getCompanyId() });

        xmlDoc = new SXmlDocument("BCE:Balanza", false) {

            @Override
            public void processXml(String xml) throws Exception {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        xmlDoc.setCustomHeader(
                  "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:BCE=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/BalanzaComprobacion\" "
                + "xsi:schemaLocation=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/BalanzaComprobacion http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/BalanzaComprobacion/BalanzaComprobacion_1_1.xsd\" ");

        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Version", "1.1"));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("RFC", company.getFiscalId()));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Mes", SLibUtils.DecimalFormatCalendarMonth.format(periodMonth)));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Anio", SLibUtils.DecimalFormatCalendarYear.format(periodYear)));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("TipoEnvio", balanceType));
        if (balanceType.compareTo(SFiscalConsts.TRS_CMP) == 0) {
            xmlDoc.getXmlAttributes().add(new SXmlAttribute("FechaModBal", DateFormatFecha.format(lastModification)));
        }

        coaPeriod = getSuitableChartOfAccounts(session, periodYear, periodMonth);

        sql = createQueryBalanza11(session, periodYear, periodMonth, coaPeriod[0], coaPeriod[1]);

        statement = session.getStatement().getConnection().createStatement();

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            accountCode = resultSet.getString("f_acc_code");
            if (accountCode == null) {
                months = SLibTimeUtils.createMonthsOfYearStd(Calendar.LONG);
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNo se ha configurado el código agrupador del SAT para la "
                        + "cuenta contable " + resultSet.getString("f_acc_id") + ":\n'" + resultSet.getString("f_acc_name") + "'.\n"
                        + "El catálogo de cuentas más reciente es: " + months[coaPeriod[1] - 1] + " " + coaPeriod[0] + ".");
            }

            accountClassKey = new int[] { resultSet.getInt("f_acc_tp_id"), resultSet.getInt("f_acc_cl_id") };

            if (SLibUtils.belongsTo(accountClassKey, new int[][] { SModSysConsts.FINS_CL_ACC_ASSET, SModSysConsts.FINS_CL_ACC_ORD_DBT, SModSysConsts.FINS_CL_ACC_RES_DBT })) {
                if (resultSet.getString("f_acc_nat").compareTo(SFiscalConsts.COA_DBT) != 0) {
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNaturaleza inapropiada ('" + resultSet.getString("f_acc_nat") + "') para la "
                            + "cuenta contable " + resultSet.getString("f_acc_id") + ":\n'" + resultSet.getString("f_acc_name") + "' (" + resultSet.getString("f_acc_code") + ").");
                }

                sign = 1d;
            }
            else if (SLibUtils.belongsTo(accountClassKey, new int[][] { SModSysConsts.FINS_CL_ACC_LIABTY, SModSysConsts.FINS_CL_ACC_EQUITY, SModSysConsts.FINS_CL_ACC_ORD_CDT, SModSysConsts.FINS_CL_ACC_RES_CDT })) {
                if (resultSet.getString("f_acc_nat").compareTo(SFiscalConsts.COA_CDT) != 0) {
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNaturaleza inapropiada ('" + resultSet.getString("f_acc_nat") + "') para la "
                            + "cuenta contable " + resultSet.getString("f_acc_id") + ":\n'" + resultSet.getString("f_acc_name") + "' (" + resultSet.getString("f_acc_code") + ").");
                }

                sign = -1d;
            }
            else {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nClase de cuenta desconocida para la "
                        + "cuenta contable " + resultSet.getString("f_acc_id") + ":\n'" + resultSet.getString("f_acc_name") + "' (" + resultSet.getString("f_acc_code") + ").");
            }

            xmlCtas = createElementBalanza11Ctas(
                    accountCode,
                    resultSet.getDouble("f_bal_ope") * sign,
                    resultSet.getDouble("f_dbt"),
                    resultSet.getDouble("f_cdt"),
                    resultSet.getDouble("f_bal_clo") * sign);

            xmlDoc.getXmlElements().add(xmlCtas);
        }

        return xmlDoc;
    }

    /**
     * Creates XML "Balanza de comprobación" 1.3.
     * @param session GUI user session.
     * @param periodYear Requested period's year.
     * @param periodMonth Requested period's month. Ranging from 1 up to 13.
     * @param balanceType Trial balance type. Constants defined in <code>SFiscalConsts.BAL_...</code>.
     * @param lastModification Last accounting modification, when trial balance type is "complement", i.e., <code>SFiscalConsts.TRS_CMP</code>). Otherwise <code>null</code> must be provided.
     */
    public static SXmlDocument createDocBalanza13(final SGuiSession session, final int periodYear, final int periodMonth, final String balanceType, final Date lastModification) throws Exception {
        int month = 0;
        double sign = 0d;
        int[] coaPeriod = null;
        int[] accountClassKey = null;
        String sql = "";
        String accountCode = "";
        String[] months = null;
        Statement statement = null;
        ResultSet resultSet = null;
        SDbBizPartner company = null;
        SXmlDocument xmlDoc = null;
        SXmlElement xmlCtas = null;
        double sumBalOpe = 0;
        double sumDbt = 0;
        double sumCdt = 0;
        double sumBalClo = 0;
        
        if (periodMonth <= SLibTimeConsts.MONTH_MAX) {
            month = periodMonth;
        }
        else if (periodMonth == (SLibTimeConsts.MONTH_MAX + 1)) {
            month = SLibTimeConsts.MONTH_MAX;
        }

        if (periodYear < SFiscalConsts.YEAR_MIN || periodYear > SLibTimeConsts.YEAR_MAX) {
            throw new Exception("El atributo 'Anio' debe ser mínimo " + SFiscalConsts.YEAR_MIN + " y máximo " + SLibTimeConsts.YEAR_MAX + ".");
        }

        if (month < SLibTimeConsts.MONTH_MIN || month > SLibTimeConsts.MONTH_MAX) {
            throw new Exception("El atributo 'Mes' debe ser mínimo " + SLibTimeConsts.MONTH_MIN + " y máximo " + SLibTimeConsts.MONTH_MAX + ".");
        }

        company = (SDbBizPartner) session.readRegistry(SModConsts.BPSU_BP, new int[] { session.getConfigCompany().getCompanyId() });

        xmlDoc = new SXmlDocument("BCE:Balanza", false) {

            @Override
            public void processXml(String xml) throws Exception {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        xmlDoc.setCustomHeader(
                  "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:BCE=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_3/BalanzaComprobacion\" "
                + "xsi:schemaLocation=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_3/BalanzaComprobacion http://www.sat.gob.mx/esquemas/ContabilidadE/1_3/BalanzaComprobacion/BalanzaComprobacion_1_3.xsd\" ");

        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Version", "1.3"));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("RFC", company.getFiscalId()));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Mes", SLibUtils.DecimalFormatCalendarMonth.format(periodMonth)));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Anio", SLibUtils.DecimalFormatCalendarYear.format(periodYear)));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("TipoEnvio", balanceType));
        if (balanceType.compareTo(SFiscalConsts.TRS_CMP) == 0) {
            xmlDoc.getXmlAttributes().add(new SXmlAttribute("FechaModBal", DateFormatFecha.format(lastModification)));
        }

        coaPeriod = getSuitableChartOfAccounts(session, periodYear, month);

        sql = createQueryBalanza13(session, periodYear, periodMonth, coaPeriod[0], coaPeriod[1]);

        statement = session.getStatement().getConnection().createStatement();

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            accountCode = resultSet.getString("f_acc_code");
            if (accountCode == null) {
                months = SLibTimeUtils.createMonthsOfYearStd(Calendar.LONG);
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNo se ha configurado el código agrupador del SAT para la "
                        + "cuenta contable " + resultSet.getString("f_acc_id") + ":\n'" + resultSet.getString("f_acc_name") + "'.\n"
                        + "El catálogo de cuentas más reciente es: " + months[coaPeriod[1] - 1] + " " + coaPeriod[0] + ".");
            }

            accountClassKey = new int[] { resultSet.getInt("f_acc_tp_id"), resultSet.getInt("f_acc_cl_id") };

            if (SLibUtils.belongsTo(accountClassKey, new int[][] { SModSysConsts.FINS_CL_ACC_ASSET, SModSysConsts.FINS_CL_ACC_ORD_DBT, SModSysConsts.FINS_CL_ACC_RES_DBT })) {
                if (resultSet.getString("f_acc_nat").compareTo(SFiscalConsts.COA_DBT) != 0) {
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNaturaleza inapropiada ('" + resultSet.getString("f_acc_nat") + "') para la "
                            + "cuenta contable " + resultSet.getString("f_acc_id") + ":\n'" + resultSet.getString("f_acc_name") + "' (" + resultSet.getString("f_acc_code") + ").");
                }

                sign = 1d;
            }
            else if (SLibUtils.belongsTo(accountClassKey, new int[][] { SModSysConsts.FINS_CL_ACC_LIABTY, SModSysConsts.FINS_CL_ACC_EQUITY, SModSysConsts.FINS_CL_ACC_ORD_CDT, SModSysConsts.FINS_CL_ACC_RES_CDT })) {
                if (resultSet.getString("f_acc_nat").compareTo(SFiscalConsts.COA_CDT) != 0) {
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNaturaleza inapropiada ('" + resultSet.getString("f_acc_nat") + "') para la "
                            + "cuenta contable " + resultSet.getString("f_acc_id") + ":\n'" + resultSet.getString("f_acc_name") + "' (" + resultSet.getString("f_acc_code") + ").");
                }

                sign = -1d;
            }
            else {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nClase de cuenta desconocida para la "
                        + "cuenta contable " + resultSet.getString("f_acc_id") + ":\n'" + resultSet.getString("f_acc_name") + "' (" + resultSet.getString("f_acc_code") + ").");
            }

            xmlCtas = createElementBalanza13Ctas(
                    accountCode,
                    resultSet.getDouble("f_bal_ope") * sign,
                    resultSet.getDouble("f_dbt"),
                    resultSet.getDouble("f_cdt"),
                    resultSet.getDouble("f_bal_clo") * sign);

            xmlDoc.getXmlElements().add(xmlCtas);
            
            sumBalOpe = SLibUtils.roundAmount(sumBalOpe + resultSet.getDouble("f_bal_ope"));
            sumDbt = SLibUtils.roundAmount(sumDbt + resultSet.getDouble("f_dbt"));
            sumCdt = SLibUtils.roundAmount(sumCdt + resultSet.getDouble("f_cdt"));
            sumBalClo = SLibUtils.roundAmount(sumBalClo + resultSet.getDouble("f_bal_clo"));
        }

        double realBalOpe = 0;
        double realDbt = 0;
        double realCdt = 0;
        double realBalClo = 0;
        String balDate = SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.createDate(periodYear, month));
        
        sql = "SELECT "
                + "SUM(IF(r.dt < '" + balDate + "', re.debit - re.credit, 0.0)) AS _bal_ope, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.debit, 0.0)) AS _dbt, "
                + "SUM(IF(r.dt >= '" + balDate + "', re.credit, 0.0)) AS _cdt, "
                + "SUM(re.debit - re.credit) AS _bal_clo "
                + "FROM fin_rec AS r "
                + "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc "
                + "AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num "
                + "WHERE NOT r.b_del AND NOT re.b_del AND r.id_year = " + periodYear + " AND r.id_per <= " + month
                + (periodMonth == SLibTimeConsts.MONTH_MAX ? " AND NOT r.b_adj_year AND NOT r.b_adj_audit" : "") + ";";

        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            realBalOpe = resultSet.getDouble("_bal_ope");
            realDbt = resultSet.getDouble("_dbt");
            realCdt = resultSet.getDouble("_cdt");
            realBalClo = resultSet.getDouble("_bal_clo");
        }
        
        // check coherence of generated trial balance:
        
        if (Math.abs(sumDbt - sumCdt) > ALLOWED_AMOUNT_DIFF) {
            throw new Exception("Error: ¡El total de cargos de la balanza de comprobación "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(sumDbt) + ") es distinto al total de abonos "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(sumCdt) + ")!");
        }
        if (Math.abs(realDbt - realCdt) > ALLOWED_AMOUNT_DIFF) {
            throw new Exception("Error: ¡El total de cargos real "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(realDbt) + ") es distinto al total de abonos "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(realCdt) + ")!");
        }
        if (Math.abs(sumBalClo - (sumBalOpe + sumDbt - sumCdt)) > ALLOWED_AMOUNT_DIFF) {
            throw new Exception("Error: ¡El saldo final de la balanza de comprobación "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(sumBalClo) + ") es distinto a la suma del saldo inicial "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(sumBalOpe) + ") + cargos (" + SLibUtils.getDecimalFormatAmount().format(sumDbt) + ") - abonos (" + SLibUtils.getDecimalFormatAmount().format(sumCdt) + ")!");
        }
        if (Math.abs(realBalClo - (realBalOpe + realDbt - realCdt)) > ALLOWED_AMOUNT_DIFF) {
            throw new Exception("Error: ¡El saldo final real "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(realBalClo) + ") es distinto a la suma del saldo inicial "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(realBalOpe) + ") + cargos (" + SLibUtils.getDecimalFormatAmount().format(realDbt) + ") - abonos (" + SLibUtils.getDecimalFormatAmount().format(realCdt) + ")!");
        }
        if (Math.abs(sumBalOpe - realBalOpe) > ALLOWED_AMOUNT_DIFF) {
            throw new Exception("Error: ¡El saldo inicial de la balanza de comprobación "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(sumBalOpe) + ") es distinto al saldo inicial real "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(realBalOpe) + ")!");
        }
        if (Math.abs(sumDbt - realDbt) > ALLOWED_AMOUNT_DIFF) {
            throw new Exception("Error: ¡El total de cargos de la balanza de comprobación "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(sumDbt) + ") es distinto al total de cargos real "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(realDbt) + ")!");
        }
        if (Math.abs(sumCdt - realCdt) > ALLOWED_AMOUNT_DIFF) {
            throw new Exception("Error: ¡El total de abonos de la balanza de comprobación "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(sumCdt) + ") es distinto al total de abonos real "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(realCdt) + ")!");
        }
        if (Math.abs(sumBalClo - realBalClo) > ALLOWED_AMOUNT_DIFF) {
            throw new Exception("Error: ¡El saldo final de la balanza de comprobación "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(sumBalClo) + ") es distinto al saldo final real "
                    + "(" + SLibUtils.getDecimalFormatAmount().format(realBalClo) + ")!");
        }
        
        return xmlDoc;
    }

    /*
     * XML: Pólizas del período.
     */
    
    private static SXmlElement createElementPolizas11Poliza(final String numUnIdenPol, final Date fecha, final String concepto) {
        SXmlElement element = new SXmlElement("PLZ:Poliza");

        element.getXmlAttributes().add(new SXmlAttribute("NumUnIdenPol", numUnIdenPol));
        element.getXmlAttributes().add(new SXmlAttribute("Fecha", DateFormatFecha.format(fecha)));
        element.getXmlAttributes().add(new SXmlAttribute("Concepto", SLibUtils.textToXml(concepto)));

        return element;
    }

    private static SXmlElement createElementPolizas13Poliza(final String numUnIdenPol, final Date fecha, final String concepto) {
        SXmlElement element = new SXmlElement("PLZ:Poliza");

        element.getXmlAttributes().add(new SXmlAttribute("NumUnIdenPol", numUnIdenPol));
        element.getXmlAttributes().add(new SXmlAttribute("Fecha", DateFormatFecha.format(fecha)));
        element.getXmlAttributes().add(new SXmlAttribute("Concepto", SLibUtils.textToXml(concepto)));

        return element;
    }
    
    private static SXmlElement createElementPolizas11Transaccion(final String numCta, final String desCta, final String concepto, final double debe, final double haber) {
        SXmlElement element = new SXmlElement("PLZ:Transaccion");

        element.getXmlAttributes().add(new SXmlAttribute("NumCta", numCta));
        element.getXmlAttributes().add(new SXmlAttribute("DesCta", desCta));
        element.getXmlAttributes().add(new SXmlAttribute("Concepto", SLibUtils.textToXml(concepto)));
        element.getXmlAttributes().add(new SXmlAttribute("Debe", DecimalFormatImporte.format(debe)));
        element.getXmlAttributes().add(new SXmlAttribute("Haber", DecimalFormatImporte.format(haber)));

        return element;
    }

   private static SXmlElement createElementPolizas13Transaccion(final String numCta, final String desCta, final String concepto, final double debe, final double haber) {
        SXmlElement element = new SXmlElement("PLZ:Transaccion");

        element.getXmlAttributes().add(new SXmlAttribute("NumCta", numCta));
        element.getXmlAttributes().add(new SXmlAttribute("DesCta", desCta));
        element.getXmlAttributes().add(new SXmlAttribute("Concepto", SLibUtils.textToXml(concepto)));
        element.getXmlAttributes().add(new SXmlAttribute("Debe", DecimalFormatImporte.format(debe)));
        element.getXmlAttributes().add(new SXmlAttribute("Haber", DecimalFormatImporte.format(haber)));

        return element;
    }
    
    private static SXmlElement createElementPolizas11CompNal(final String uuidCfdi, final String rfc, final double montoTotal, final String moneda, final Double tipCamb) {
        SXmlElement element = new SXmlElement("PLZ:CompNal");

        element.getXmlAttributes().add(new SXmlAttribute("UUID_CFDI", uuidCfdi));

        element.getXmlAttributes().add(new SXmlAttribute("RFC", rfc));
        
        element.getXmlAttributes().add(new SXmlAttribute("MontoTotal", DecimalFormatImporte.format(montoTotal)));

        if (!moneda.isEmpty() && moneda.compareTo(SModSysConsts.FINS_FISCAL_CUR_MXN_NAME) != 0) {
            element.getXmlAttributes().add(new SXmlAttribute("Moneda", moneda));
            element.getXmlAttributes().add(new SXmlAttribute("TipCamb", DecimalFormatTipCamb.format(tipCamb)));
        }

        return element;
    }
    
    private static SXmlElement createElementPolizas13CompNal(final String uuidCfdi, final String rfc, final double montoTotal, final String moneda, final Double tipCamb) {
        SXmlElement element = new SXmlElement("PLZ:CompNal");

        element.getXmlAttributes().add(new SXmlAttribute("UUID_CFDI", uuidCfdi));

        element.getXmlAttributes().add(new SXmlAttribute("RFC", rfc));

        element.getXmlAttributes().add(new SXmlAttribute("MontoTotal", DecimalFormatImporte.format(montoTotal)));

        if (!moneda.isEmpty() && moneda.compareTo(SModSysConsts.FINS_FISCAL_CUR_MXN_NAME) != 0) {
            element.getXmlAttributes().add(new SXmlAttribute("Moneda", moneda));
            element.getXmlAttributes().add(new SXmlAttribute("TipCamb", DecimalFormatTipCamb.format(tipCamb)));
        }

        return element;
    }

    private static SXmlElement createElementPolizas11CompNalOtr(final String cfdCbbSerie, final String cfdCbbNumFol, final String rfc, final double montoTotal, final String moneda, final Double tipCamb) {
        SXmlElement element = new SXmlElement("PLZ:CompNalOtr");

        if (!cfdCbbSerie.isEmpty()) {
            element.getXmlAttributes().add(new SXmlAttribute("CFD_CBB_Serie", cfdCbbSerie));
        }
        
        element.getXmlAttributes().add(new SXmlAttribute("CFD_CBB_NumFol", extractDigits(cfdCbbNumFol)));

        element.getXmlAttributes().add(new SXmlAttribute("RFC", rfc));

        element.getXmlAttributes().add(new SXmlAttribute("MontoTotal", DecimalFormatImporte.format(montoTotal)));

        if (!moneda.isEmpty() && moneda.compareTo(SModSysConsts.FINS_FISCAL_CUR_MXN_NAME) != 0) {
            element.getXmlAttributes().add(new SXmlAttribute("Moneda", moneda));
            element.getXmlAttributes().add(new SXmlAttribute("TipCamb", DecimalFormatTipCamb.format(tipCamb)));
        }

        return element;
    }
    
    private static SXmlElement createElementPolizas13CompNalOtr(final String cfdCbbSerie, final String cfdCbbNumFol, final String rfc, final double montoTotal, final String moneda, final Double tipCamb) {
        SXmlElement element = new SXmlElement("PLZ:CompNalOtr");

        if (!cfdCbbSerie.isEmpty()) {
            element.getXmlAttributes().add(new SXmlAttribute("CFD_CBB_Serie", cfdCbbSerie));
        }
        
        element.getXmlAttributes().add(new SXmlAttribute("CFD_CBB_NumFol", extractDigits(cfdCbbNumFol)));

        element.getXmlAttributes().add(new SXmlAttribute("RFC", rfc));

        element.getXmlAttributes().add(new SXmlAttribute("MontoTotal", DecimalFormatImporte.format(montoTotal)));

        if (!moneda.isEmpty() && moneda.compareTo(SModSysConsts.FINS_FISCAL_CUR_MXN_NAME) != 0) {
            element.getXmlAttributes().add(new SXmlAttribute("Moneda", moneda));
            element.getXmlAttributes().add(new SXmlAttribute("TipCamb", DecimalFormatTipCamb.format(tipCamb)));
        }

        return element;
    }

    private static SXmlElement createElementPolizas11CompExt(final String numFactExt, final String taxID, final double montoTotal, final String moneda, final Double tipCamb) {
        SXmlElement element = new SXmlElement("PLZ:CompExt");

        element.getXmlAttributes().add(new SXmlAttribute("NumFactExt", numFactExt));

        if (!taxID.isEmpty()) {
            element.getXmlAttributes().add(new SXmlAttribute("TaxID", taxID));
        }

        element.getXmlAttributes().add(new SXmlAttribute("MontoTotal", DecimalFormatImporte.format(montoTotal)));

        if (!moneda.isEmpty() && moneda.compareTo(SModSysConsts.FINS_FISCAL_CUR_MXN_NAME) != 0) {
            element.getXmlAttributes().add(new SXmlAttribute("Moneda", moneda));
            element.getXmlAttributes().add(new SXmlAttribute("TipCamb", DecimalFormatTipCamb.format(tipCamb)));            
        }

        return element;
    }
    
    private static SXmlElement createElementPolizas13CompExt(final String numFactExt, final String taxID, final double montoTotal, final String moneda, final Double tipCamb) {
        SXmlElement element = new SXmlElement("PLZ:CompExt");

        element.getXmlAttributes().add(new SXmlAttribute("NumFactExt", numFactExt));

        if (!taxID.isEmpty()) {
            element.getXmlAttributes().add(new SXmlAttribute("TaxID", taxID));
        }

        element.getXmlAttributes().add(new SXmlAttribute("MontoTotal", DecimalFormatImporte.format(montoTotal)));

        if (!moneda.isEmpty() && moneda.compareTo(SModSysConsts.FINS_FISCAL_CUR_MXN_NAME) != 0) {
            element.getXmlAttributes().add(new SXmlAttribute("Moneda", moneda));
            element.getXmlAttributes().add(new SXmlAttribute("TipCamb", DecimalFormatTipCamb.format(tipCamb)));
        }

        return element;
    }

    private static SXmlElement createElementPolizas11Cheque(final String num, final String banEmisNal, final String banEmisExt, final String ctaOri, final Date fecha, final String benef, final String rfc, final double monto, final String moneda, final Double tipCamb) {
        SXmlElement element = new SXmlElement("PLZ:Cheque");

        element.getXmlAttributes().add(new SXmlAttribute("Num", num));
        element.getXmlAttributes().add(new SXmlAttribute("BanEmisNal", banEmisNal));
        if (!banEmisExt.isEmpty()) {
            element.getXmlAttributes().add(new SXmlAttribute("BanEmisExt", banEmisExt));
        }
        element.getXmlAttributes().add(new SXmlAttribute("CtaOri", ctaOri));
        element.getXmlAttributes().add(new SXmlAttribute("Fecha", DateFormatFecha.format(fecha)));
        element.getXmlAttributes().add(new SXmlAttribute("Benef", benef));
        element.getXmlAttributes().add(new SXmlAttribute("RFC", rfc));
        element.getXmlAttributes().add(new SXmlAttribute("Monto", DecimalFormatImporte.format(monto)));

        if (!moneda.isEmpty() && moneda.compareTo(SModSysConsts.FINS_FISCAL_CUR_MXN_NAME) != 0) {
            element.getXmlAttributes().add(new SXmlAttribute("Moneda", moneda));
            element.getXmlAttributes().add(new SXmlAttribute("TipCamb", DecimalFormatTipCamb.format(tipCamb)));
        }

        return element;
    }
    
    private static SXmlElement createElementPolizas13Cheque(final String num, final String banEmisNal, final String banEmisExt, final String ctaOri, final Date fecha, final String benef, final String rfc, final double monto, final String moneda, final Double tipCamb) {
        SXmlElement element = new SXmlElement("PLZ:Cheque");

        element.getXmlAttributes().add(new SXmlAttribute("Num", num));
        element.getXmlAttributes().add(new SXmlAttribute("BanEmisNal", banEmisNal));
        if (!banEmisExt.isEmpty()) {
            element.getXmlAttributes().add(new SXmlAttribute("BanEmisExt", banEmisExt));
        }
        element.getXmlAttributes().add(new SXmlAttribute("CtaOri", ctaOri));
        element.getXmlAttributes().add(new SXmlAttribute("Fecha", DateFormatFecha.format(fecha)));
        element.getXmlAttributes().add(new SXmlAttribute("Benef", benef));
        element.getXmlAttributes().add(new SXmlAttribute("RFC", rfc));
        element.getXmlAttributes().add(new SXmlAttribute("Monto", DecimalFormatImporte.format(monto)));

        if (!moneda.isEmpty() && moneda.compareTo(SModSysConsts.FINS_FISCAL_CUR_MXN_NAME) != 0) {
            element.getXmlAttributes().add(new SXmlAttribute("Moneda", moneda));
            element.getXmlAttributes().add(new SXmlAttribute("TipCamb", DecimalFormatTipCamb.format(tipCamb)));
        }

        return element;
    }

    private static SXmlElement createElementPolizas11Transferencia(final String ctaOri, final String bancoOriNal, final String bancoOriExt, final String ctaDest, final String bancoDestNal, final String bancoDestExt, final Date fecha, final String benef, final String rfc, final double monto, final String moneda, final Double tipCamb) {
        SXmlElement element = new SXmlElement("PLZ:Transferencia");

        element.getXmlAttributes().add(new SXmlAttribute("CtaOri", ctaOri));
        element.getXmlAttributes().add(new SXmlAttribute("BancoOriNal", bancoOriNal));
        if (!bancoOriExt.isEmpty()) {
            element.getXmlAttributes().add(new SXmlAttribute("BanEmisExt", bancoOriExt));
        }
        element.getXmlAttributes().add(new SXmlAttribute("CtaDest", ctaDest));
        element.getXmlAttributes().add(new SXmlAttribute("BancoDestNal", bancoDestNal));
        if (!bancoDestExt.isEmpty()) {
            element.getXmlAttributes().add(new SXmlAttribute("BancoDestExt", bancoDestExt));
        }
        element.getXmlAttributes().add(new SXmlAttribute("Fecha", DateFormatFecha.format(fecha)));
        element.getXmlAttributes().add(new SXmlAttribute("Benef", benef));
        element.getXmlAttributes().add(new SXmlAttribute("RFC", rfc));
        element.getXmlAttributes().add(new SXmlAttribute("Monto", DecimalFormatImporte.format(monto)));

        if (!moneda.isEmpty() && moneda.compareTo(SModSysConsts.FINS_FISCAL_CUR_MXN_NAME) != 0) {
            element.getXmlAttributes().add(new SXmlAttribute("Moneda", moneda));
            element.getXmlAttributes().add(new SXmlAttribute("TipCamb", DecimalFormatTipCamb.format(tipCamb)));
        }

        return element;
    }
    
    private static SXmlElement createElementPolizas13Transferencia(final String ctaOri, final String bancoOriNal, final String bancoOriExt, final String ctaDest, final String bancoDestNal, final String bancoDestExt, final Date fecha, final String benef, final String rfc, final double monto, final String moneda, final Double tipCamb) {
        SXmlElement element = new SXmlElement("PLZ:Transferencia");

        element.getXmlAttributes().add(new SXmlAttribute("CtaOri", ctaOri));
        element.getXmlAttributes().add(new SXmlAttribute("BancoOriNal", bancoOriNal));
        if (!bancoOriExt.isEmpty()) {
            element.getXmlAttributes().add(new SXmlAttribute("BanEmisExt", bancoOriExt));
        }
        element.getXmlAttributes().add(new SXmlAttribute("CtaDest", ctaDest));
        element.getXmlAttributes().add(new SXmlAttribute("BancoDestNal", bancoDestNal));
        if (!bancoDestExt.isEmpty()) {
            element.getXmlAttributes().add(new SXmlAttribute("BancoDestExt", bancoDestExt));
        }
        element.getXmlAttributes().add(new SXmlAttribute("Fecha", DateFormatFecha.format(fecha)));
        element.getXmlAttributes().add(new SXmlAttribute("Benef", benef));
        element.getXmlAttributes().add(new SXmlAttribute("RFC", rfc));
        element.getXmlAttributes().add(new SXmlAttribute("Monto", DecimalFormatImporte.format(monto)));

        if (!moneda.isEmpty() && moneda.compareTo(SModSysConsts.FINS_FISCAL_CUR_MXN_NAME) != 0) {
            element.getXmlAttributes().add(new SXmlAttribute("Moneda", moneda));
            element.getXmlAttributes().add(new SXmlAttribute("TipCamb", DecimalFormatTipCamb.format(tipCamb)));
        }

        return element;
    }
    
    private static SXmlElement createElementPolizas11OtrMetodoPago(final String metPagoPol, final Date fecha, final String benef, final String rfc, final double monto, final String moneda, final Double tipCamb) {
        SXmlElement element = new SXmlElement("PLZ:OtrMetodoPago");

        element.getXmlAttributes().add(new SXmlAttribute("MetPagoPol", metPagoPol));
        element.getXmlAttributes().add(new SXmlAttribute("Fecha", DateFormatFecha.format(fecha)));
        element.getXmlAttributes().add(new SXmlAttribute("Benef", benef));
        element.getXmlAttributes().add(new SXmlAttribute("RFC", rfc));
        element.getXmlAttributes().add(new SXmlAttribute("Monto", DecimalFormatImporte.format(monto)));

        if (!moneda.isEmpty() && moneda.compareTo(SModSysConsts.FINS_FISCAL_CUR_MXN_NAME) != 0) {
            element.getXmlAttributes().add(new SXmlAttribute("Moneda", moneda));
            element.getXmlAttributes().add(new SXmlAttribute("TipCamb", DecimalFormatTipCamb.format(tipCamb)));
        }

        return element;
    }
    
    private static SXmlElement createElementPolizas13OtrMetodoPago(final String metPagoPol, final Date fecha, final String benef, final String rfc, final double monto, final String moneda, final Double tipCamb) {
        SXmlElement element = new SXmlElement("PLZ:OtrMetodoPago");

        element.getXmlAttributes().add(new SXmlAttribute("MetPagoPol", metPagoPol));
        element.getXmlAttributes().add(new SXmlAttribute("Fecha", DateFormatFecha.format(fecha)));
        element.getXmlAttributes().add(new SXmlAttribute("Benef", benef));
        element.getXmlAttributes().add(new SXmlAttribute("RFC", rfc));
        element.getXmlAttributes().add(new SXmlAttribute("Monto", DecimalFormatImporte.format(monto)));

        if (!moneda.isEmpty() && moneda.compareTo(SModSysConsts.FINS_FISCAL_CUR_MXN_NAME) != 0) {
            element.getXmlAttributes().add(new SXmlAttribute("Moneda", moneda));
            element.getXmlAttributes().add(new SXmlAttribute("TipCamb", DecimalFormatTipCamb.format(tipCamb)));
        }

        return element;
    }
    
    /**
     * Creates XML "Pólizas del período" 1.1.
     * @param session GUI user session.
     * @param periodYear Requested period's year.
     * @param periodMonth Requested period's month.
     * @param tipoSolicitud Request type.
     * @param numOrden Order number.
     * @param numTramite Processing number.
     */
    public static SXmlDocument createDocPolizas11(final SGuiSession session, final int periodYear, final int periodMonth, final String tipoSolicitud, final String numOrden, final String numTramite) throws Exception {
        int[] coaPeriod = null;
        boolean useFixedFiscalAccounts = false;     // when 'true', treat system accounts (e.g., cash, banks, business partners, etc.) as simple fixed fiscal accounts, those provided by SAT, without any individual system subaccount. Otherwise treat each individual system subaccount as a fiscal subaccount
        String sql = "";
        String moneda = "";
        double tipoCambio = 0;
        String xmlMoneda = "";
        String xmlTipoCambio = "";
        String[] months = null;
        Statement statementRec = null;
        Statement statementRecEty = null;
        Statement statementRecEtyCfd = null;
        ResultSet resultSetRec = null;
        ResultSet resultSetRecEty = null;
        ResultSet resultSetRecEtyCfd = null;
        SDbBizPartner company = null;
        SXmlDocument xmlDoc = null;
        SXmlElement xmlPoliza = null;
        SXmlElement xmlTransaccion = null;
        SXmlElement xmlComp = null;
        SXmlElement xmlPago = null;

        if (periodYear < SFiscalConsts.YEAR_MIN || periodYear > SLibTimeConsts.YEAR_MAX) {
            throw new Exception("El atributo 'Anio' debe ser mínimo " + SFiscalConsts.YEAR_MIN + " y máximo " + SLibTimeConsts.YEAR_MAX + ".");
        }

        if (periodMonth < SLibTimeConsts.MONTH_MIN || periodMonth > SLibTimeConsts.MONTH_MAX) {
            throw new Exception("El atributo 'Mes' debe ser mínimo " + SLibTimeConsts.MONTH_MIN + " y máximo " + SLibTimeConsts.MONTH_MAX + ".");
        }

        company = (SDbBizPartner) session.readRegistry(SModConsts.BPSU_BP, new int[] { session.getConfigCompany().getCompanyId() });

        xmlDoc = new SXmlDocument("PLZ:Polizas", false) {

            @Override
            public void processXml(String xml) throws Exception {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        xmlDoc.setCustomHeader(
                  "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:PLZ=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/PolizasPeriodo\" "
                + "xsi:schemaLocation=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/PolizasPeriodo http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/PolizasPeriodo/PolizasPeriodo_1_1.xsd\" ");

        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Version", "1.1"));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("RFC", company.getFiscalId()));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Mes", SLibUtils.DecimalFormatCalendarMonth.format(periodMonth)));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Anio", SLibUtils.DecimalFormatCalendarYear.format(periodYear)));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("TipoSolicitud", tipoSolicitud));
        if (!numOrden.isEmpty() && (tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_AF) == 0 || tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_FC) == 0)) {
            xmlDoc.getXmlAttributes().add(new SXmlAttribute("NumOrden", numOrden));
        }
        if (!numTramite.isEmpty() && (tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_DE) == 0 || tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_CO) == 0)) {
            xmlDoc.getXmlAttributes().add(new SXmlAttribute("NumTramite", numTramite));
        }

        coaPeriod = getSuitableChartOfAccounts(session, periodYear, periodMonth);

        /*
         * Obtain journal vouchers as follows:
         * - Main query: Journal vouchers of requested period.
         * - Subquery 1: Movements of current journal voucher:
         *      - Fiscal account link 1. Cash accounts.
         *      - Fiscal account link 2. Business partner accounts.
         *      - Fiscal account link 3. Results accounts.
         *      - Fiscal account link 4. Other remaining balance accounts.
         */

        statementRec = session.getStatement().getConnection().createStatement();
        statementRecEty = session.getStatement().getConnection().createStatement();
        statementRecEtyCfd = session.getStatement().getConnection().createStatement();

        sql = "SELECT r.id_bkc, r.id_tp_rec, r.id_num, r.dt, r.concept "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + "WHERE r.b_del = 0 AND r.id_year = " + periodYear + " AND r.id_per = " + periodMonth + " "
                + "ORDER BY r.id_bkc, r.id_tp_rec, r.id_num; ";

        resultSetRec = statementRec.executeQuery(sql);
        while (resultSetRec.next()) {
            xmlPoliza = createElementPolizas11Poliza(resultSetRec.getString("r.id_tp_rec") + "-" + resultSetRec.getInt("r.id_num"), resultSetRec.getDate("r.dt"), resultSetRec.getString("r.concept"));

            sql = "SELECT re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety, re.debit, re.credit, re.concept, re.exc_rate, "
                    + "re.fid_cl_sys_mov, re.fid_tp_sys_mov, re.fid_cl_sys_acc, re.fid_tp_sys_acc, "
                    + "a.id_acc, a.acc, "
                    + "al.fid_tp_acc_spe, al.fid_tp_acc_r, "
                    + "fald1.id_ref_1 AS f_ld1_ref1, fald1.id_ref_2 AS f_ld1_ref2, fald1.acc_code, fald1.acc_name, "
                    + "fald2.id_ref_1 AS f_ld2_ref1, fald2.id_ref_2 AS f_ld2_ref2, fald2.acc_code, fald2.acc_name, "
                    + "fald3.id_ref_1 AS f_ld3_ref1, fald3.id_ref_2 AS f_ld3_ref2, fald3.acc_code, fald3.acc_name, "
                    + "fald4.id_ref_1 AS f_ld4_ref1, fald4.id_ref_2 AS f_ld4_ref2, fald4.acc_code, fald4.acc_name, "
                    + "re.fid_dps_year_n, re.fid_dps_doc_n, doc.num_ser, doc.num, doc.dt, doc.tot_r, doc.exc_rate, doc_cur.id_cur, doc_fcur.id_fiscal_cur, doc_fcur.code, "
                    + "@pos := INSTR(cfd.doc_xml, 'UUID=') AS f_cfd_pos, UPPER(IF(@pos = 0, '', MID(cfd.doc_xml, @pos + 6, 36))) AS f_cfd_uuid, doc_bp.fiscal_id, doc_bp.fiscal_frg_id, "
                    + "re.fid_check_wal_n, re.fid_check_n, acsh_bnk.acc_num, fbnk.id_fiscal_bank, fbnk.code, bnk_fcur.id_fiscal_cur, bnk_fcur.code, chk.num, chk.dt, chk.val, chk.benef, chk_bp.bp, chk_bp.fiscal_id "
                    + ""
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON re.fid_acc = a.id_acc "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fald1 ON "
                    + "fald1.id_year = " + coaPeriod[0] + " AND fald1.id_per = " + coaPeriod[1] + " AND fald1.id_acc = a.pk_acc AND "
                    + "fald1.id_tp_fiscal_acc_link BETWEEN " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_CSH + " AND " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_BNK + " AND "
                    + "fald1.id_ref_1 = re.fid_cob_n AND fald1.id_ref_2 = re.fid_ent_n "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fald2 ON "
                    + "fald2.id_year = " + coaPeriod[0] + " AND fald2.id_per = " + coaPeriod[1] + " AND fald2.id_acc = a.pk_acc AND "
                    + "fald2.id_tp_fiscal_acc_link BETWEEN " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_SUP + " AND " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CUS_ADV + " AND "
                    + "fald2.id_ref_1 = re.fid_bp_nr AND fald2.id_ref_2 = " + SLibConsts.UNDEFINED + " "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fald3 ON "
                    + "fald3.id_year = " + coaPeriod[0] + " AND fald3.id_per = " + coaPeriod[1] + " AND fald3.id_acc = a.pk_acc AND ("
                    + "(fald3.id_tp_fiscal_acc_link = " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_INC + " AND re.debit = 0.0) OR "
                    + "(fald3.id_tp_fiscal_acc_link = " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_EXP + " AND re.credit = 0.0)) AND "
                    + "fald3.id_ref_1 = re.fid_item_n AND fald3.id_ref_2 = " + SLibConsts.UNDEFINED + " "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fald4 ON "
                    + "fald4.id_year = " + coaPeriod[0] + " AND fald4.id_per = " + coaPeriod[1] + " AND fald4.id_acc = a.pk_acc AND "
                    + "fald4.id_tp_fiscal_acc_link = " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_ACC + " AND "
                    + "fald4.id_ref_1 = a.pk_acc AND fald4.id_ref_2 = " + SLibConsts.UNDEFINED + " "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS doc ON doc.id_year = re.fid_dps_year_n AND doc.id_doc = re.fid_dps_doc_n "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS doc_cur ON doc_cur.id_cur = doc.fid_cur "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_CUR) + " AS doc_fcur ON doc_fcur.id_fiscal_cur = doc_cur.fid_fiscal_cur "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS cfd ON cfd.fid_dps_year_n = doc.id_year AND cfd.fid_dps_doc_n = doc.id_doc "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS doc_bp ON doc_bp.id_bp = doc.fid_bp_r "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CHECK_WAL) + " AS chkw ON chkw.id_check_wal = re.fid_check_wal_n "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC_CASH) + " AS acsh ON acsh.id_cob = chkw.fid_cob AND acsh.id_acc_cash = chkw.fid_acc_cash "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BANK_ACC) + " AS acsh_bnk ON acsh_bnk.id_bpb = acsh.fid_bpb_n AND acsh_bnk.id_bank_acc = acsh.fid_bank_acc_n "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bnk ON bnk.id_bp = acsh_bnk.fid_bank "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_BANK) + " AS fbnk ON fbnk.id_fiscal_bank = bnk.fid_fiscal_bank "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS bnk_cur ON bnk_cur.id_cur = acsh.fid_cur "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_CUR) + " AS bnk_fcur ON bnk_fcur.id_fiscal_cur = bnk_cur.fid_fiscal_cur "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CHECK) + " AS chk ON chk.id_check_wal = re.fid_check_wal_n AND chk.id_check = re.fid_check_n "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS chk_bp ON chk_bp.id_bp = chk.fid_bp_nr "
                    + ""
                    + "WHERE re.b_del = 0 AND re.id_year = " + periodYear + " AND re.id_per = " + periodMonth + " AND "
                    + "re.id_bkc = " + resultSetRec.getInt("r.id_bkc") + " AND re.id_tp_rec = '" + resultSetRec.getString("r.id_tp_rec") + "' AND re.id_num = " + resultSetRec.getInt("r.id_num") + " "
                    + "ORDER BY re.id_ety; ";
            resultSetRecEty = statementRecEty.executeQuery(sql);
            while (resultSetRecEty.next()) {
                if (resultSetRecEty.getString("fald1.acc_code") != null) {
                    // Cash accounts:
                    xmlTransaccion = createElementPolizas11Transaccion(resultSetRecEty.getString("fald1.acc_code"), resultSetRecEty.getString("fald1.acc_name"),
                            resultSetRecEty.getString("re.concept"), resultSetRecEty.getDouble("re.debit"), resultSetRecEty.getDouble("re.credit"));
                }
                else if (resultSetRecEty.getString("fald2.acc_code") != null) {
                    // Business partner accounts:
                    xmlTransaccion = createElementPolizas11Transaccion(resultSetRecEty.getString("fald2.acc_code"), resultSetRecEty.getString("fald2.acc_name"),
                            resultSetRecEty.getString("re.concept"), resultSetRecEty.getDouble("re.debit"), resultSetRecEty.getDouble("re.credit"));
                }
                else if (resultSetRecEty.getString("fald3.acc_code") != null) {
                    // Results accounts:
                    xmlTransaccion = createElementPolizas11Transaccion(resultSetRecEty.getString("fald3.acc_code"), resultSetRecEty.getString("fald3.acc_name"),
                            resultSetRecEty.getString("re.concept"), resultSetRecEty.getDouble("re.debit"), resultSetRecEty.getDouble("re.credit"));
                }
                else if (resultSetRecEty.getString("fald4.acc_code") != null) {
                    // Other remaining balance accounts:
                    xmlTransaccion = createElementPolizas11Transaccion(resultSetRecEty.getString("fald4.acc_code"), resultSetRecEty.getString("fald4.acc_name"),
                            resultSetRecEty.getString("re.concept"), resultSetRecEty.getDouble("re.debit"), resultSetRecEty.getDouble("re.credit"));
                }
                else {
                    months = SLibTimeUtils.createMonthsOfYearStd(Calendar.LONG);
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNo se ha configurado el código agrupador del SAT para la "
                            + "cuenta contable " + resultSetRecEty.getString("id_acc") + ":\n'" + resultSetRecEty.getString("acc") + "'.\n"
                            + "El catálogo de cuentas más reciente es: " + months[coaPeriod[1] - 1] + " " + coaPeriod[0] + ".");
                }
                
                // Check for CFDIs from record entry's document:

                if (resultSetRecEty.getInt("re.fid_dps_year_n") != SLibConsts.UNDEFINED) {
                    // Document available:

                    xmlComp = null;

                    if (resultSetRecEty.getString("doc_bp.fiscal_id").compareTo(DCfdConsts.RFC_GEN_INT) == 0) {
                        // International document:
                        xmlComp = createElementPolizas11CompExt(
                                (resultSetRecEty.getString("doc.num_ser").isEmpty() ? "" : resultSetRecEty.getString("doc.num_ser") + "-") + resultSetRecEty.getString("doc.num"),
                                resultSetRecEty.getString("doc_bp.fiscal_frg_id"), 
                                resultSetRecEty.getDouble("doc.tot_r"),
                                resultSetRecEty.getString("doc_fcur.code"),
                                resultSetRecEty.getDouble("doc.exc_rate"));
                    }
                    else {
                        // Domestic document:
                        if (resultSetRecEty.getString("f_cfd_uuid") != null && !resultSetRecEty.getString("f_cfd_uuid").isEmpty()) {
                            // CFDI:
                            xmlComp = createElementPolizas11CompNal(
                                    resultSetRecEty.getString("f_cfd_uuid"),
                                    resultSetRecEty.getString("doc_bp.fiscal_id"), 
                                    resultSetRecEty.getDouble("doc.tot_r"),
                                    resultSetRecEty.getString("doc_fcur.code"),
                                    resultSetRecEty.getDouble("doc.exc_rate"));
                        }
                        else {
                            // Other:
                            xmlComp = createElementPolizas11CompNalOtr(
                                    resultSetRecEty.getString("doc.num_ser"),
                                    resultSetRecEty.getString("doc.num"),
                                    resultSetRecEty.getString("doc_bp.fiscal_id"), 
                                    resultSetRecEty.getDouble("doc.tot_r"),
                                    resultSetRecEty.getString("doc_fcur.code"),
                                    resultSetRecEty.getDouble("doc.exc_rate"));
                        }
                    }

                    if (xmlComp != null) {
                        xmlTransaccion.getXmlElements().add(xmlComp);
                    }
                }

                // Check for CFDIs from record entry:
                
                sql = "SELECT " +
                        "@ui := LOCATE(' UUID=\"', doc_xml), @uf := LOCATE('\"', doc_xml, @ui + 7), " +
                        "IF(@ui = 0, '" + SUtilConsts.NON_APPLYING + "', UPPER(SUBSTRING(doc_xml, @ui + 7, @uf - @ui - 7))) AS f_uuid, " +
                        "@ri := LOCATE(' rfc=\"', doc_xml), @rf := LOCATE('\"', doc_xml, @ri + 6), " +
                        "IF(@ri = 0, '" + SUtilConsts.NON_APPLYING + "', UPPER(SUBSTRING(doc_xml, @ri + 6, @rf - @ri - 6))) AS f_rfc, " +
                        "@ti := LOCATE(' total=\"', doc_xml), @tf := LOCATE('\"', doc_xml, @ti + 8), " +
                        "IF(@ti = 0, '" + SUtilConsts.NON_APPLYING + "', UPPER(SUBSTRING(doc_xml, @ti + 8, @tf - @ti - 8))) AS f_total, " +
                        "@mi := LOCATE(' Moneda=\"', doc_xml), @mf := LOCATE('\"', doc_xml, @mi + 9), " +
                        "IF(@mi = 0, '" + SUtilConsts.NON_APPLYING + "', UPPER(SUBSTRING(doc_xml, @mi + 9, @mf - @mi - 9))) AS f_Moneda, " +
                        "@xi := LOCATE(' TipoCambio=\"', doc_xml), @xf := LOCATE('\"', doc_xml, @xi + 13), " +
                        "IF(@xi = 0, '" + SUtilConsts.NON_APPLYING + "', UPPER(SUBSTRING(doc_xml, @xi + 13, @xf - @xi - 13))) AS f_TipoCambio " +
                        "FROM trn_cfd " +
                        "WHERE " +
                        "fid_rec_year_n = " + resultSetRecEty.getInt("re.id_year") + " AND " +
                        "fid_rec_per_n = " + resultSetRecEty.getInt("re.id_per") + " AND " +
                        "fid_rec_bkc_n = " + resultSetRecEty.getInt("re.id_bkc") + " AND " +
                        "fid_rec_tp_rec_n = '" + resultSetRecEty.getString("re.id_tp_rec") + "' AND " +
                        "fid_rec_num_n = " + resultSetRecEty.getInt("re.id_num") + " AND " +
                        "fid_rec_ety_n = " + resultSetRecEty.getInt("re.id_ety") + " " +
                        "ORDER BY id_cfd ";
                resultSetRecEtyCfd = statementRecEtyCfd.executeQuery(sql);
                while (resultSetRecEtyCfd.next()) {
                    if (resultSetRecEtyCfd.getString("f_uuid").compareTo(SUtilConsts.NON_APPLYING) != 0) {
                        xmlMoneda = resultSetRecEtyCfd.getString("f_Moneda");
                        xmlTipoCambio = resultSetRecEtyCfd.getString("f_TipoCambio");
                        
                        tipoCambio = SLibUtils.parseDouble(xmlTipoCambio);
                        
                        if (xmlMoneda.isEmpty() || xmlMoneda.compareTo(SUtilConsts.NON_APPLYING) == 0) {
                            if (tipoCambio != 0d && tipoCambio != 1d) {
                                // Exchange rate provided but currency is not available, so USD is assumed:
                                moneda = SModSysConsts.FINS_FISCAL_CUR_USD_NAME;
                            }
                            else {
                                // Exchange rate not provided:
                                moneda = "";
                                tipoCambio = 0d;
                            }
                        }
                        else {
                            moneda = "";
                            
                            switch (xmlMoneda) {
                                case SModSysConsts.FINS_FISCAL_CUR_MXN_NAME:    // MXN
                                case SModSysConsts.FINS_FISCAL_CUR_USD_NAME:    // USD
                                case SModSysConsts.FINS_FISCAL_CUR_EUR_NAME:    // EUR
                                case SModSysConsts.FINS_FISCAL_CUR_JPY_NAME:    // JPY
                                    // Currency identified:
                                    moneda = xmlMoneda;
                                    break;
                                default:
                                    // Attempt to identify currency:
                                    switch (SLibUtils.textToAscii(xmlMoneda.substring(0, xmlMoneda.length() >= 3 ? 3 : xmlMoneda.length()).toUpperCase())) {
                                        case "1":
                                        case "MN":
                                        case "M.N":
                                        case "MXP":
                                        case "NAC":
                                        case "PES":
                                        case "PSM":
                                            moneda = SModSysConsts.FINS_FISCAL_CUR_MXN_NAME;
                                            break;
                                        case "2":
                                        case "DLL":
                                        case "DLS":
                                        case "DOL":
                                        case "DÓL":
                                        case "USA":
                                            moneda = SModSysConsts.FINS_FISCAL_CUR_USD_NAME;
                                            break;
                                        case "3":
                                            moneda = SModSysConsts.FINS_FISCAL_CUR_EUR_NAME;
                                            break;
                                        case "JAP":
                                        case "YEN":
                                            moneda = SModSysConsts.FINS_FISCAL_CUR_JPY_NAME;
                                            break;
                                        default:
                                    }
                            }
                            
                            if (moneda.isEmpty()) {
                                if (tipoCambio != 0d && tipoCambio != 1d) {
                                    // Exchange rate provided but currency is not available:
                                    moneda = SModSysConsts.FINS_FISCAL_CUR_USD_NAME;
                                }
                            }
                        }
                        
                        xmlComp = createElementPolizas11CompNal(
                                resultSetRecEtyCfd.getString("f_uuid"),
                                resultSetRecEtyCfd.getString("f_rfc"),
                                resultSetRecEtyCfd.getDouble("f_total"),
                                moneda,
                                tipoCambio);

                        xmlTransaccion.getXmlElements().add(xmlComp);
                    }
                }

                // check for payments:
                
                if (resultSetRecEty.getInt("re.fid_cl_sys_acc") == SModSysConsts.FINS_CL_SYS_ACC_ENT_CSH && resultSetRecEty.getDouble("re.credit") != 0) {
                    // Outgoing money:

                    xmlPago = null;

                    if (resultSetRecEty.getInt("re.fid_check_wal_n") != SLibConsts.UNDEFINED) {
//                        xmlPago = createElementPolizas11Cheque(resultSetRecEty.getString("chk.num"), resultSetRecEty.getString("fbnk.code"), resultSetRecEty.getString("bnk_fcur.code"), resultSetRecEty.getString("acsh_bnk.acc_num"),
                        xmlPago = createElementPolizas11Cheque(resultSetRecEty.getString("chk.num"), resultSetRecEty.getString("fbnk.code"), resultSetRecEty.getString("doc_fcur.code"), resultSetRecEty.getString("acsh_bnk.acc_num"),
                                resultSetRecEty.getDate("chk.dt"), resultSetRecEty.getString("chk.benef"), resultSetRecEty.getString("chk_bp.fiscal_id") == null ? DCfdConsts.RFC_GEN_NAC : resultSetRecEty.getString("chk_bp.fiscal_id"),
                                resultSetRecEty.getDouble("chk.val"), resultSetRecEty.getString("bnk_fcur.code"), resultSetRecEty.getDouble("re.exc_rate"));
                    }

                    if (xmlPago != null) {
                        xmlTransaccion.getXmlElements().add(xmlPago);
                    }
                }

                xmlPoliza.getXmlElements().add(xmlTransaccion);
            }

            xmlDoc.getXmlElements().add(xmlPoliza);
        }

        return xmlDoc;
    }
    
    /**
    * Creates XML "Pólizas del período" 1.3.
    * @param session GUI user session.
    * @param periodYear Requested period's year.
    * @param periodMonth Requested period's month.
    * @param tipoSolicitud Request type.
    * @param numOrden Order number.
    * @param numTramite Processing number.
    */
    public static SXmlDocument createDocPolizas13(final SGuiSession session, final int periodYear, final int periodMonth, final String tipoSolicitud, final String numOrden, final String numTramite) throws Exception {
        int[] coaPeriod = null;
        boolean useFixedFiscalAccounts = false;     // when 'true', treat system accounts (e.g., cash, banks, business partners, etc.) as simple fixed fiscal accounts, those provided by SAT, without any individual system subaccount. Otherwise treat each individual system subaccount as a fiscal subaccount
        String sql = "";
        String moneda = "";
        double tipoCambio = 0;
        String xmlMoneda = "";
        String xmlTipoCambio = "";
        String[] months = null;
        Statement statementRec = null;
        Statement statementRecEty = null;
        Statement statementRecEtyCfd = null;
        ResultSet resultSetRec = null;
        ResultSet resultSetRecEty = null;
        ResultSet resultSetRecEtyCfd = null;
        SDbBizPartner company = null;
        SXmlDocument xmlDoc = null;
        SXmlElement xmlPoliza = null;
        SXmlElement xmlTransaccion = null;
        SXmlElement xmlComp = null;
        SXmlElement xmlPago = null;

        if (periodYear < SFiscalConsts.YEAR_MIN || periodYear > SLibTimeConsts.YEAR_MAX) {
            throw new Exception("El atributo 'Anio' debe ser mínimo " + SFiscalConsts.YEAR_MIN + " y máximo " + SLibTimeConsts.YEAR_MAX + ".");
        }

        if (periodMonth < SLibTimeConsts.MONTH_MIN || periodMonth > SLibTimeConsts.MONTH_MAX) {
            throw new Exception("El atributo 'Mes' debe ser mínimo " + SLibTimeConsts.MONTH_MIN + " y máximo " + SLibTimeConsts.MONTH_MAX + ".");
        }

        company = (SDbBizPartner) session.readRegistry(SModConsts.BPSU_BP, new int[] { session.getConfigCompany().getCompanyId() });

        xmlDoc = new SXmlDocument("PLZ:Polizas", false) {

            @Override
            public void processXml(String xml) throws Exception {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        xmlDoc.setCustomHeader(
                  "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:PLZ=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_3/PolizasPeriodo\" "
                + "xsi:schemaLocation=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_3/PolizasPeriodo http://www.sat.gob.mx/esquemas/ContabilidadE/1_3/PolizasPeriodo/PolizasPeriodo_1_3.xsd\" ");

        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Version", "1.3"));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("RFC", company.getFiscalId()));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Mes", SLibUtils.DecimalFormatCalendarMonth.format(periodMonth)));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Anio", SLibUtils.DecimalFormatCalendarYear.format(periodYear)));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("TipoSolicitud", tipoSolicitud));
        if (!numOrden.isEmpty() && (tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_AF) == 0 || tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_FC) == 0)) {
            xmlDoc.getXmlAttributes().add(new SXmlAttribute("NumOrden", numOrden));
        }
        if (!numTramite.isEmpty() && (tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_DE) == 0 || tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_CO) == 0)) {
            xmlDoc.getXmlAttributes().add(new SXmlAttribute("NumTramite", numTramite));
        }

        coaPeriod = getSuitableChartOfAccounts(session, periodYear, periodMonth);

        /*
         * Obtain journal vouchers as follows:
         * - Main query: Journal vouchers of requested period.
         * - Subquery 1: Movements of current journal voucher:
         *      - Fiscal account link 1. Cash accounts.
         *      - Fiscal account link 2. Business partner accounts.
         *      - Fiscal account link 3. Results accounts.
         *      - Fiscal account link 4. Other remaining balance accounts.
         */

        statementRec = session.getStatement().getConnection().createStatement();
        statementRecEty = session.getStatement().getConnection().createStatement();
        statementRecEtyCfd = session.getStatement().getConnection().createStatement();

        sql = "SELECT r.id_bkc, r.id_tp_rec, r.id_num, r.dt, r.concept "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + "WHERE r.b_del = 0 AND r.id_year = " + periodYear + " AND r.id_per = " + periodMonth + " "
                + "ORDER BY r.id_bkc, r.id_tp_rec, r.id_num; ";

        resultSetRec = statementRec.executeQuery(sql);
        while (resultSetRec.next()) {
            xmlPoliza = createElementPolizas13Poliza(resultSetRec.getString("r.id_tp_rec") + "-" + resultSetRec.getInt("r.id_num"), resultSetRec.getDate("r.dt"), resultSetRec.getString("r.concept"));

            sql = "SELECT re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety, re.debit, re.credit, re.concept, re.exc_rate, "
                    + "re.fid_cl_sys_mov, re.fid_tp_sys_mov, re.fid_cl_sys_acc, re.fid_tp_sys_acc, "
                    + "a.id_acc, a.acc, "
                    + "al.fid_tp_acc_spe, al.fid_tp_acc_r, "
                    + "fald1.id_ref_1 AS f_ld1_ref1, fald1.id_ref_2 AS f_ld1_ref2, fald1.acc_code, fald1.acc_name, "
                    + "fald2.id_ref_1 AS f_ld2_ref1, fald2.id_ref_2 AS f_ld2_ref2, fald2.acc_code, fald2.acc_name, "
                    + "fald3.id_ref_1 AS f_ld3_ref1, fald3.id_ref_2 AS f_ld3_ref2, fald3.acc_code, fald3.acc_name, "
                    + "fald4.id_ref_1 AS f_ld4_ref1, fald4.id_ref_2 AS f_ld4_ref2, fald4.acc_code, fald4.acc_name, "
                    + "re.fid_dps_year_n, re.fid_dps_doc_n, doc.num_ser, doc.num, doc.dt, doc.tot_r, doc.exc_rate, doc_cur.id_cur, doc_fcur.id_fiscal_cur, doc_fcur.code, "
                    + "@pos := INSTR(cfd.doc_xml, 'UUID=') AS f_cfd_pos, UPPER(IF(@pos = 0, '', MID(cfd.doc_xml, @pos + 6, 36))) AS f_cfd_uuid, doc_bp.fiscal_id, doc_bp.fiscal_frg_id, "
                    + "re.fid_check_wal_n, re.fid_check_n, acsh_bnk.acc_num, fbnk.id_fiscal_bank, fbnk.code, bnk_fcur.id_fiscal_cur, bnk_fcur.code, chk.num, chk.dt, chk.val, chk.benef, chk_bp.bp, chk_bp.fiscal_id "
                    + ""
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON re.fid_acc = a.id_acc "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fald1 ON "
                    + "fald1.id_year = " + coaPeriod[0] + " AND fald1.id_per = " + coaPeriod[1] + " AND fald1.id_acc = a.pk_acc AND "
                    + "fald1.id_tp_fiscal_acc_link BETWEEN " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_CSH + " AND " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_BNK + " AND "
                    + "fald1.id_ref_1 = re.fid_cob_n AND fald1.id_ref_2 = re.fid_ent_n "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fald2 ON "
                    + "fald2.id_year = " + coaPeriod[0] + " AND fald2.id_per = " + coaPeriod[1] + " AND fald2.id_acc = a.pk_acc AND "
                    + "fald2.id_tp_fiscal_acc_link BETWEEN " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_SUP + " AND " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CUS_ADV + " AND "
                    + "fald2.id_ref_1 = re.fid_bp_nr AND fald2.id_ref_2 = " + SLibConsts.UNDEFINED + " "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fald3 ON "
                    + "fald3.id_year = " + coaPeriod[0] + " AND fald3.id_per = " + coaPeriod[1] + " AND fald3.id_acc = a.pk_acc AND ("
                    + "(fald3.id_tp_fiscal_acc_link = " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_INC + " AND re.debit = 0.0) OR "
                    + "(fald3.id_tp_fiscal_acc_link = " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_EXP + " AND re.credit = 0.0)) AND "
                    + "fald3.id_ref_1 = re.fid_item_n AND fald3.id_ref_2 = " + SLibConsts.UNDEFINED + " "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fald4 ON "
                    + "fald4.id_year = " + coaPeriod[0] + " AND fald4.id_per = " + coaPeriod[1] + " AND fald4.id_acc = a.pk_acc AND "
                    + "fald4.id_tp_fiscal_acc_link = " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_ACC + " AND "
                    + "fald4.id_ref_1 = a.pk_acc AND fald4.id_ref_2 = " + SLibConsts.UNDEFINED + " "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS doc ON doc.id_year = re.fid_dps_year_n AND doc.id_doc = re.fid_dps_doc_n "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS doc_cur ON doc_cur.id_cur = doc.fid_cur "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_CUR) + " AS doc_fcur ON doc_fcur.id_fiscal_cur = doc_cur.fid_fiscal_cur "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS cfd ON cfd.fid_dps_year_n = doc.id_year AND cfd.fid_dps_doc_n = doc.id_doc "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS doc_bp ON doc_bp.id_bp = doc.fid_bp_r "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CHECK_WAL) + " AS chkw ON chkw.id_check_wal = re.fid_check_wal_n "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC_CASH) + " AS acsh ON acsh.id_cob = chkw.fid_cob AND acsh.id_acc_cash = chkw.fid_acc_cash "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BANK_ACC) + " AS acsh_bnk ON acsh_bnk.id_bpb = acsh.fid_bpb_n AND acsh_bnk.id_bank_acc = acsh.fid_bank_acc_n "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bnk ON bnk.id_bp = acsh_bnk.fid_bank "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_BANK) + " AS fbnk ON fbnk.id_fiscal_bank = bnk.fid_fiscal_bank "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS bnk_cur ON bnk_cur.id_cur = acsh.fid_cur "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_FISCAL_CUR) + " AS bnk_fcur ON bnk_fcur.id_fiscal_cur = bnk_cur.fid_fiscal_cur "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CHECK) + " AS chk ON chk.id_check_wal = re.fid_check_wal_n AND chk.id_check = re.fid_check_n "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS chk_bp ON chk_bp.id_bp = chk.fid_bp_nr "
                    + ""
                    + "WHERE re.b_del = 0 AND re.id_year = " + periodYear + " AND re.id_per = " + periodMonth + " AND "
                    + "re.id_bkc = " + resultSetRec.getInt("r.id_bkc") + " AND re.id_tp_rec = '" + resultSetRec.getString("r.id_tp_rec") + "' AND re.id_num = " + resultSetRec.getInt("r.id_num") + " "
                    + "ORDER BY re.id_ety; ";

            resultSetRecEty = statementRecEty.executeQuery(sql);
             while (resultSetRecEty.next()) {
                if (resultSetRecEty.getString("fald1.acc_code") != null) {
                    // Cash accounts:
                    xmlTransaccion = createElementPolizas13Transaccion(resultSetRecEty.getString("fald1.acc_code"), resultSetRecEty.getString("fald1.acc_name"),
                            resultSetRecEty.getString("re.concept"), resultSetRecEty.getDouble("re.debit"), resultSetRecEty.getDouble("re.credit"));
                }
                else if (resultSetRecEty.getString("fald2.acc_code") != null) {
                    // Business partner accounts:
                    xmlTransaccion = createElementPolizas13Transaccion(resultSetRecEty.getString("fald2.acc_code"), resultSetRecEty.getString("fald2.acc_name"),
                            resultSetRecEty.getString("re.concept"), resultSetRecEty.getDouble("re.debit"), resultSetRecEty.getDouble("re.credit"));
                }
                else if (resultSetRecEty.getString("fald3.acc_code") != null) {
                    // Results accounts:
                    xmlTransaccion = createElementPolizas13Transaccion(resultSetRecEty.getString("fald3.acc_code"), resultSetRecEty.getString("fald3.acc_name"),
                            resultSetRecEty.getString("re.concept"), resultSetRecEty.getDouble("re.debit"), resultSetRecEty.getDouble("re.credit"));
                }
                else if (resultSetRecEty.getString("fald4.acc_code") != null) {
                    // Other remaining balance accounts:
                    xmlTransaccion = createElementPolizas13Transaccion(resultSetRecEty.getString("fald4.acc_code"), resultSetRecEty.getString("fald4.acc_name"),
                            resultSetRecEty.getString("re.concept"), resultSetRecEty.getDouble("re.debit"), resultSetRecEty.getDouble("re.credit"));
                }
                else {
                    months = SLibTimeUtils.createMonthsOfYearStd(Calendar.LONG);
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNo se ha configurado el código agrupador del SAT para la "
                            + "cuenta contable " + resultSetRecEty.getString("id_acc") + ":\n'" + resultSetRecEty.getString("acc") + "'.\n"
                            + "El catálogo de cuentas más reciente es: " + months[coaPeriod[1] - 1] + " " + coaPeriod[0] + ".");
                }
                
                // Check for CFDIs from record entry's document:

                if (resultSetRecEty.getInt("re.fid_dps_year_n") != SLibConsts.UNDEFINED) {
                    // Document available:

                    xmlComp = null;

                    if (resultSetRecEty.getString("doc_bp.fiscal_id").compareTo(DCfdConsts.RFC_GEN_INT) == 0) {
                        // International document:
                        xmlComp = createElementPolizas13CompExt(
                                (resultSetRecEty.getString("doc.num_ser").isEmpty() ? "" : resultSetRecEty.getString("doc.num_ser") + "-") + resultSetRecEty.getString("doc.num"),
                                resultSetRecEty.getString("doc_bp.fiscal_frg_id"), 
                                resultSetRecEty.getDouble("doc.tot_r"),
                                resultSetRecEty.getString("doc_fcur.code"),
                                resultSetRecEty.getDouble("doc.exc_rate"));
                    }
                    else {
                        // Domestic document:
                        if (resultSetRecEty.getString("f_cfd_uuid") != null && !resultSetRecEty.getString("f_cfd_uuid").isEmpty()) {
                            // CFDI:
                            xmlComp = createElementPolizas13CompNal(
                                    resultSetRecEty.getString("f_cfd_uuid"),
                                    resultSetRecEty.getString("doc_bp.fiscal_id"), 
                                    resultSetRecEty.getDouble("doc.tot_r"),
                                    resultSetRecEty.getString("doc_fcur.code"),
                                    resultSetRecEty.getDouble("doc.exc_rate"));
                        }
                        else {
                            // Other:
                            xmlComp = createElementPolizas13CompNalOtr(
                                    resultSetRecEty.getString("doc.num_ser"),
                                    resultSetRecEty.getString("doc.num"),
                                    resultSetRecEty.getString("doc_bp.fiscal_id"), 
                                    resultSetRecEty.getDouble("doc.tot_r"),
                                    resultSetRecEty.getString("doc_fcur.code"),
                                    resultSetRecEty.getDouble("doc.exc_rate"));
                        }
                    }

                    if (xmlComp != null) {
                        xmlTransaccion.getXmlElements().add(xmlComp);
                    }
                }

                // Check for CFDIs from record entry:
                
                sql = "SELECT " +
                        "@ui := LOCATE(' UUID=\"', doc_xml), @uf := LOCATE('\"', doc_xml, @ui + 7), " +
                        "IF(@ui = 0, '" + SUtilConsts.NON_APPLYING + "', UPPER(SUBSTRING(doc_xml, @ui + 7, @uf - @ui - 7))) AS f_uuid, " +
                        "@ri := LOCATE(' rfc=\"', doc_xml), @rf := LOCATE('\"', doc_xml, @ri + 6), " +
                        "IF(@ri = 0, '" + SUtilConsts.NON_APPLYING + "', UPPER(SUBSTRING(doc_xml, @ri + 6, @rf - @ri - 6))) AS f_rfc, " +
                        "@ti := LOCATE(' total=\"', doc_xml), @tf := LOCATE('\"', doc_xml, @ti + 8), " +
                        "IF(@ti = 0, '" + SUtilConsts.NON_APPLYING + "', UPPER(SUBSTRING(doc_xml, @ti + 8, @tf - @ti - 8))) AS f_total, " +
                        "@mi := LOCATE(' Moneda=\"', doc_xml), @mf := LOCATE('\"', doc_xml, @mi + 9), " +
                        "IF(@mi = 0, '" + SUtilConsts.NON_APPLYING + "', UPPER(SUBSTRING(doc_xml, @mi + 9, @mf - @mi - 9))) AS f_Moneda, " +
                        "@xi := LOCATE(' TipoCambio=\"', doc_xml), @xf := LOCATE('\"', doc_xml, @xi + 13), " +
                        "IF(@xi = 0, '" + SUtilConsts.NON_APPLYING + "', UPPER(SUBSTRING(doc_xml, @xi + 13, @xf - @xi - 13))) AS f_TipoCambio " +
                        "FROM trn_cfd " +
                        "WHERE " +
                        "fid_rec_year_n = " + resultSetRecEty.getInt("re.id_year") + " AND " +
                        "fid_rec_per_n = " + resultSetRecEty.getInt("re.id_per") + " AND " +
                        "fid_rec_bkc_n = " + resultSetRecEty.getInt("re.id_bkc") + " AND " +
                        "fid_rec_tp_rec_n = '" + resultSetRecEty.getString("re.id_tp_rec") + "' AND " +
                        "fid_rec_num_n = " + resultSetRecEty.getInt("re.id_num") + " AND " +
                        "fid_rec_ety_n = " + resultSetRecEty.getInt("re.id_ety") + " " +
                        "ORDER BY id_cfd ";
                resultSetRecEtyCfd = statementRecEtyCfd.executeQuery(sql);
                while (resultSetRecEtyCfd.next()) {
                    if (resultSetRecEtyCfd.getString("f_uuid").compareTo(SUtilConsts.NON_APPLYING) != 0) {
                        xmlMoneda = resultSetRecEtyCfd.getString("f_Moneda");
                        xmlTipoCambio = resultSetRecEtyCfd.getString("f_TipoCambio");
                        
                        tipoCambio = SLibUtils.parseDouble(xmlTipoCambio);
                        
                        if (xmlMoneda.isEmpty() || xmlMoneda.compareTo(SUtilConsts.NON_APPLYING) == 0) {
                            if (tipoCambio != 0d && tipoCambio != 1d) {
                                // Exchange rate provided but currency is not available, so USD is assumed:
                                moneda = SModSysConsts.FINS_FISCAL_CUR_USD_NAME;
                            }
                            else {
                                // Exchange rate not provided:
                                moneda = "";
                                tipoCambio = 0d;
                            }
                        }
                        else {
                            moneda = "";
                            
                            switch (xmlMoneda) {
                                case SModSysConsts.FINS_FISCAL_CUR_MXN_NAME:    // MXN
                                case SModSysConsts.FINS_FISCAL_CUR_USD_NAME:    // USD
                                case SModSysConsts.FINS_FISCAL_CUR_EUR_NAME:    // EUR
                                case SModSysConsts.FINS_FISCAL_CUR_JPY_NAME:    // JPY
                                    // Currency identified:
                                    moneda = xmlMoneda;
                                    break;
                                default:
                                    // Attempt to identify currency:
                                    switch (SLibUtils.textToAscii(xmlMoneda.substring(0, xmlMoneda.length() >= 3 ? 3 : xmlMoneda.length()).toUpperCase())) {
                                        case "1":
                                        case "MN":
                                        case "M.N":
                                        case "MXP":
                                        case "NAC":
                                        case "PES":
                                        case "PSM":
                                            moneda = SModSysConsts.FINS_FISCAL_CUR_MXN_NAME;
                                            break;
                                        case "2":
                                        case "DLL":
                                        case "DLS":
                                        case "DOL":
                                        case "DÓL":
                                        case "USA":
                                            moneda = SModSysConsts.FINS_FISCAL_CUR_USD_NAME;
                                            break;
                                        case "3":
                                            moneda = SModSysConsts.FINS_FISCAL_CUR_EUR_NAME;
                                            break;
                                        case "JAP":
                                        case "YEN":
                                            moneda = SModSysConsts.FINS_FISCAL_CUR_JPY_NAME;
                                            break;
                                        default:
                                    }
                            }
                            
                            if (moneda.isEmpty()) {
                                if (tipoCambio != 0d && tipoCambio != 1d) {
                                    // Exchange rate provided but currency is not available:
                                    moneda = SModSysConsts.FINS_FISCAL_CUR_USD_NAME;
                                }
                            }
                        }
                        
                        xmlComp = createElementPolizas13CompNal(
                                resultSetRecEtyCfd.getString("f_uuid"),
                                resultSetRecEtyCfd.getString("f_rfc"),
                                resultSetRecEtyCfd.getDouble("f_total"),
                                moneda,
                                tipoCambio);

                        xmlTransaccion.getXmlElements().add(xmlComp);
                    }
                }

                if (resultSetRecEty.getInt("re.fid_cl_sys_mov") == SModSysConsts.FINS_CL_SYS_MOV_MO) {
                    // Outgoing money:

                    xmlPago = null;

                    if (resultSetRecEty.getInt("re.fid_check_wal_n") != SLibConsts.UNDEFINED) {
//                        xmlPago = createElementPolizas11Cheque(resultSetRecEty.getString("chk.num"), resultSetRecEty.getString("fbnk.code"), resultSetRecEty.getString("bnk_fcur.code"), resultSetRecEty.getString("acsh_bnk.acc_num"),
                        xmlPago = createElementPolizas13Cheque(resultSetRecEty.getString("chk.num"), resultSetRecEty.getString("fbnk.code"),resultSetRecEty.getString("bnk_fcur.code"), resultSetRecEty.getString("acsh_bnk.acc_num"),
                                resultSetRecEty.getDate("chk.dt"), resultSetRecEty.getString("chk.benef"), resultSetRecEty.getString("chk_bp.fiscal_id") == null ? DCfdConsts.RFC_GEN_NAC : resultSetRecEty.getString("chk_bp.fiscal_id"),
                                resultSetRecEty.getDouble("chk.val"), resultSetRecEty.getString("bnk_fcur.code"), resultSetRecEty.getDouble("re.exc_rate"));
                    }

                    if (xmlPago != null) {
                        xmlTransaccion.getXmlElements().add(xmlPago);
                    }
                }

                xmlPoliza.getXmlElements().add(xmlTransaccion);
            }

            xmlDoc.getXmlElements().add(xmlPoliza);
        }

        return xmlDoc;
    }

    /*
     * XML: Auxiliares de folios fiscales asignados a los comprobantes fiscales dentro de las pólizas.
     */

    /*
     * XML: Auxiliares de cuenta de nivel mayor y/o de la subcuenta de primer nivel.
     */

    private static SXmlElement createElementAuxiliarCtas11Cuenta(final String numCta, final String desCta, final double saldoIni, final double saldoFin) {
        SXmlElement element = new SXmlElement("AuxiliarCtas:Cuenta");

        element.getXmlAttributes().add(new SXmlAttribute("NumCta", numCta));
        element.getXmlAttributes().add(new SXmlAttribute("DesCta", desCta));
        element.getXmlAttributes().add(new SXmlAttribute("SaldoIni", DecimalFormatImporte.format(saldoIni)));
        element.getXmlAttributes().add(new SXmlAttribute("SaldoFin", DecimalFormatImporte.format(saldoFin)));

        return element;
    }
    
    private static SXmlElement createElementAuxiliarCtas13Cuenta(final String numCta, final String desCta, final double saldoIni, final double saldoFin) {
        SXmlElement element = new SXmlElement("AuxiliarCtas:Cuenta");

        element.getXmlAttributes().add(new SXmlAttribute("NumCta", numCta));
        element.getXmlAttributes().add(new SXmlAttribute("DesCta", desCta));
        element.getXmlAttributes().add(new SXmlAttribute("SaldoIni", DecimalFormatImporte.format(saldoIni)));
        element.getXmlAttributes().add(new SXmlAttribute("SaldoFin", DecimalFormatImporte.format(saldoFin)));

        return element;
    }

    private static SXmlElement createElementAuxiliarCtas11DetalleAux(final Date fecha, final String numUnIdenPol, final String concepto, final double debe, final double haber) {
        SXmlElement element = new SXmlElement("AuxiliarCtas:DetalleAux");

        element.getXmlAttributes().add(new SXmlAttribute("Fecha", DateFormatFecha.format(fecha)));
        element.getXmlAttributes().add(new SXmlAttribute("NumUnIdenPol", numUnIdenPol));
        element.getXmlAttributes().add(new SXmlAttribute("Concepto", SLibUtils.textToXml(concepto)));
        element.getXmlAttributes().add(new SXmlAttribute("Debe", DecimalFormatImporte.format(debe)));
        element.getXmlAttributes().add(new SXmlAttribute("Haber", DecimalFormatImporte.format(haber)));

        return element;
    }
    
    private static SXmlElement createElementAuxiliarCtas13DetalleAux(final Date fecha, final String numUnIdenPol, final String concepto, final double debe, final double haber) {
        SXmlElement element = new SXmlElement("AuxiliarCtas:DetalleAux");

        element.getXmlAttributes().add(new SXmlAttribute("Fecha", DateFormatFecha.format(fecha)));
        element.getXmlAttributes().add(new SXmlAttribute("NumUnIdenPol", numUnIdenPol));
        element.getXmlAttributes().add(new SXmlAttribute("Concepto", SLibUtils.textToXml(concepto)));
        element.getXmlAttributes().add(new SXmlAttribute("Debe", DecimalFormatImporte.format(debe)));
        element.getXmlAttributes().add(new SXmlAttribute("Haber", DecimalFormatImporte.format(haber)));

        return element;
    }

    /**
     * Creates XML "Auxiliares de cuenta de nivel mayor y/o de la subcuenta de primer nivel" 1.1.
     * @param session GUI user session.
     * @param periodYear Requested period's year.
     * @param periodMonth Requested period's month.
     * @param tipoSolicitud Request type.
     * @param numOrden Order number.
     * @param numTramite Processing number.
     * @param accountStdStart Starting account code on standard-format.
     * @param accountStdEnd Ending account code on standard-format.
     */
    public static SXmlDocument createDocAuxiliarCtas11(final SGuiSession session, final int periodYear, final int periodMonth, final String tipoSolicitud, final String numOrden, final String numTramite, final String accountStdStart, final String accountStdEnd) throws Exception {
        int[] coaPeriod = null;
        int[] accountClassKey = null;
        double sign = 0;
        boolean useFixedFiscalAccounts = false;     // when 'true', treat system accounts (e.g., cash, banks, business partners, etc.) as simple fixed fiscal accounts, those provided by SAT, without any individual system subaccount. Otherwise treat each individual system subaccount as a fiscal subaccount
        String sql = "";
        String sqlFilter = "";
        String dateOpen = "";
        String dateClose = "";
        String[] months = null;
        Statement statementAcc = null;
        Statement statementAccAux = null;
        ResultSet resultSetAcc = null;
        ResultSet resultSetAccAux = null;
        SDbBizPartner company = null;
        SXmlDocument xmlDoc = null;
        SXmlElement xmlCuenta = null;
        SXmlElement xmlDetalleAux = null;

        if (periodYear < SFiscalConsts.YEAR_MIN || periodYear > SLibTimeConsts.YEAR_MAX) {
            throw new Exception("El atributo 'Anio' debe ser mínimo " + SFiscalConsts.YEAR_MIN + " y máximo " + SLibTimeConsts.YEAR_MAX + ".");
        }

        if (periodMonth < SLibTimeConsts.MONTH_MIN || periodMonth > SLibTimeConsts.MONTH_MAX) {
            throw new Exception("El atributo 'Mes' debe ser mínimo " + SLibTimeConsts.MONTH_MIN + " y máximo " + SLibTimeConsts.MONTH_MAX + ".");
        }

        company = (SDbBizPartner) session.readRegistry(SModConsts.BPSU_BP, new int[] { session.getConfigCompany().getCompanyId() });

        xmlDoc = new SXmlDocument("AuxiliarCtas:AuxiliarCtas", false) {

            @Override
            public void processXml(String xml) throws Exception {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        xmlDoc.setCustomHeader(
                  "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:AuxiliarCtas=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/AuxiliarCtas\" "
                + "xsi:schemaLocation=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/AuxiliarCtas http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/AuxiliarCtas/AuxiliarCtas_1_1.xsd\" ");

        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Version", "1.1"));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("RFC", company.getFiscalId()));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Mes", SLibUtils.DecimalFormatCalendarMonth.format(periodMonth)));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Anio", SLibUtils.DecimalFormatCalendarYear.format(periodYear)));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("TipoSolicitud", tipoSolicitud));
        if (!numOrden.isEmpty() && (tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_AF) == 0 || tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_FC) == 0)) {
            xmlDoc.getXmlAttributes().add(new SXmlAttribute("NumOrden", numOrden));
        }
        if (!numTramite.isEmpty() && (tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_DE) == 0 || tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_CO) == 0)) {
            xmlDoc.getXmlAttributes().add(new SXmlAttribute("NumTramite", numTramite));
        }

        coaPeriod = getSuitableChartOfAccounts(session, periodYear, periodMonth);

        dateOpen = SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.createDate(periodYear, periodMonth));
        dateClose = SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getEndOfMonth(SLibTimeUtils.createDate(periodYear, periodMonth)));

        /*
         * Obtain journal vouchers as follows:
         * - Main query: Accounts ledger.
         *      - Accounts of fiscal account link 1. Cash accounts.
         *      - Accounts of fiscal account link 2. Business partner accounts.
         *      - Accounts of fiscal account link 3. Results accounts.
         *      - Accounts of fiscal account link 4. Other remaining balance accounts.
         * - Subquery: Corresponding journal.
         */

        statementAcc = session.getStatement().getConnection().createStatement();
        statementAccAux = session.getStatement().getConnection().createStatement();

        if (accountStdStart.isEmpty() && accountStdEnd.isEmpty()) {
            sqlFilter = "";
        }
        else if (!accountStdStart.isEmpty() && accountStdEnd.isEmpty()) {
            sqlFilter = "AND a.code >= '" + accountStdStart + "' ";
        }
        else if (accountStdStart.isEmpty() && !accountStdEnd.isEmpty()) {
            sqlFilter = "AND a.code <= '" + accountStdStart + "' ";
        }
        else {
            sqlFilter = "AND a.code BETWEEN '" + accountStdStart + "' AND '" + accountStdEnd + "' ";
        }

        sql = "SELECT " +
                "fld.id_acc, " +
                "fld.id_tp_fiscal_acc_link, " +
                "fld.id_ref_1, " +
                "fld.id_ref_2, " +
                "fld.acc_code, " +
                "fld.acc_name, " +
                "fld.nat, " +
                "a.id_acc, " +
                "a.acc, " +
                "al.fid_tp_acc_r, " +
                "al.fid_cl_acc_r, " +
                "SUM(IF(r.dt < '" + dateOpen + "', re.debit - re.credit, 0.0)) AS f_bal_ope, " +
                "SUM(re.debit - re.credit) AS f_bal_end " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND r.b_del = 0 AND re.b_del = 0 " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON re.fid_acc = a.id_acc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND " +
                "al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_BAL + " AND al.fid_tp_acc_spe = " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fld ON fld.id_year = " + coaPeriod[0] + " AND fld.id_per = " + coaPeriod[1] + " AND " +
                "fld.id_acc = a.pk_acc AND fld.id_tp_fiscal_acc_link BETWEEN " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_CSH + " AND " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_BNK + " AND " +
                "fld.id_ref_1 = re.fid_cob_n AND fld.id_ref_2 = re.fid_ent_n " +
                "WHERE r.id_year = " + periodYear + " AND r.dt <= '" + dateClose + "' " + sqlFilter +
                "GROUP BY fld.id_acc, fld.id_tp_fiscal_acc_link, fld.id_ref_1, fld.id_ref_2, fld.acc_code, fld.acc_name, fld.nat, a.id_acc, a.acc, al.fid_tp_acc_r, al.fid_cl_acc_r " +
                "" +
                "UNION " +
                "" +
                "SELECT " +
                "fld.id_acc, " +
                "fld.id_tp_fiscal_acc_link, " +
                "fld.id_ref_1, " +
                "fld.id_ref_2, " +
                "fld.acc_code, " +
                "fld.acc_name, " +
                "fld.nat, " +
                "a.id_acc, " +
                "a.acc, " +
                "al.fid_tp_acc_r, " +
                "al.fid_cl_acc_r, " +
                "SUM(IF(r.dt < '" + dateOpen + "', re.debit - re.credit, 0.0)) AS f_bal_ope, " +
                "SUM(re.debit - re.credit) AS f_bal_end " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND r.b_del = 0 AND re.b_del = 0 " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON re.fid_acc = a.id_acc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND " +
                "al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_BAL + " AND al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fld ON fld.id_year = " + coaPeriod[0] + " AND fld.id_per = " + coaPeriod[1] + " AND " +
                "fld.id_acc = a.pk_acc AND fld.id_tp_fiscal_acc_link BETWEEN " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_SUP + " AND " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CUS_ADV + " AND " +
                "fld.id_ref_1 = re.fid_bp_nr AND fld.id_ref_2 = 0 " +
                "WHERE r.id_year = " + periodYear + " AND r.dt <= '" + dateClose + "' " + sqlFilter +
                "GROUP BY fld.id_acc, fld.id_tp_fiscal_acc_link, fld.id_ref_1, fld.id_ref_2, fld.acc_code, fld.acc_name, fld.nat, a.id_acc, a.acc, al.fid_tp_acc_r, al.fid_cl_acc_r " +
                "" +
                "UNION " +
                "" +
                "SELECT " +
                "fld.id_acc, " +
                "fld.id_tp_fiscal_acc_link, " +
                "fld.id_ref_1, " +
                "fld.id_ref_2, " +
                "fld.acc_code, " +
                "fld.acc_name, " +
                "fld.nat, " +
                "a.id_acc, " +
                "a.acc, " +
                "al.fid_tp_acc_r, " +
                "al.fid_cl_acc_r, " +
                "SUM(IF(r.dt < '" + dateOpen + "', re.debit - re.credit, 0.0)) AS f_bal_ope, " +
                "SUM(re.debit - re.credit) AS f_bal_end " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND r.b_del = 0 AND re.b_del = 0 " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON re.fid_acc = a.id_acc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND " +
                "al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fld ON fld.id_year = " + coaPeriod[0] + " AND fld.id_per = " + coaPeriod[1] + " AND " +
                "fld.id_acc = a.pk_acc AND fld.id_tp_fiscal_acc_link BETWEEN " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_INC + " AND " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_EXP + " AND " +
                "fld.id_ref_1 = re.fid_item_n AND fld.id_ref_2 = 0 " +
                "WHERE r.id_year = " + periodYear + " AND r.dt <= '" + dateClose + "' " + sqlFilter +
                "GROUP BY fld.id_acc, fld.id_tp_fiscal_acc_link, fld.id_ref_1, fld.id_ref_2, fld.acc_code, fld.acc_name, fld.nat, a.id_acc, a.acc, al.fid_tp_acc_r, al.fid_cl_acc_r " +
                "" +
                "UNION " +
                "" +
                "SELECT " +
                "fld.id_acc, " +
                "fld.id_tp_fiscal_acc_link, " +
                "fld.id_ref_1, " +
                "fld.id_ref_2, " +
                "fld.acc_code, " +
                "fld.acc_name, " +
                "fld.nat, " +
                "a.id_acc, " +
                "a.acc, " +
                "al.fid_tp_acc_r, " +
                "al.fid_cl_acc_r, " +
                "SUM(IF(r.dt < '" + dateOpen + "', re.debit - re.credit, 0.0)) AS f_bal_ope, " +
                "SUM(re.debit - re.credit) AS f_bal_end " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND r.b_del = 0 AND re.b_del = 0 " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON re.fid_acc = a.id_acc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND " +
                "al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_BAL + " AND al.fid_tp_acc_spe <> " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " AND al.fid_tp_acc_spe NOT BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fld ON fld.id_year = " + coaPeriod[0] + " AND fld.id_per = " + coaPeriod[1] + " AND " +
                "fld.id_acc = a.pk_acc AND fld.id_tp_fiscal_acc_link = " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_ACC + " AND " +
                "fld.id_ref_1 = a.pk_acc AND fld.id_ref_2 = 0 " +
                "WHERE r.id_year = " + periodYear + " AND r.dt <= '" + dateClose + "' " + sqlFilter +
                "GROUP BY fld.id_acc, fld.id_tp_fiscal_acc_link, fld.id_ref_1, fld.id_ref_2, fld.acc_code, fld.acc_name, fld.nat, a.id_acc, a.acc, al.fid_tp_acc_r, al.fid_cl_acc_r " +
                "" +
                "ORDER BY acc_code, acc_name; ";

        resultSetAcc = statementAcc.executeQuery(sql);
        while (resultSetAcc.next()) {
            if (resultSetAcc.getString("acc_code") == null) {
                months = SLibTimeUtils.createMonthsOfYearStd(Calendar.LONG);
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNo se ha configurado el código agrupador del SAT para la "
                        + "cuenta contable " + resultSetAcc.getString("id_acc") + ":\n'" + resultSetAcc.getString("acc") + "'.\n"
                        + "El catálogo de cuentas más reciente es: " + months[coaPeriod[1] - 1] + " " + coaPeriod[0] + ".");
            }

            accountClassKey = new int[] { resultSetAcc.getInt("fid_tp_acc_r"), resultSetAcc.getInt("fid_cl_acc_r") };

            if (SLibUtils.belongsTo(accountClassKey, new int[][] { SModSysConsts.FINS_CL_ACC_ASSET, SModSysConsts.FINS_CL_ACC_ORD_DBT, SModSysConsts.FINS_CL_ACC_RES_DBT })) {
                if (resultSetAcc.getString("nat").compareTo(SFiscalConsts.COA_DBT) != 0) {
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNaturaleza inapropiada ('" + resultSetAcc.getString("nat") + "') para la "
                            + "cuenta contable " + resultSetAcc.getString("id_acc") + ":\n'" + resultSetAcc.getString("acc") + "' (" + resultSetAcc.getString("acc_code") + ").");
                }

                sign = 1d;
            }
            else if (SLibUtils.belongsTo(accountClassKey, new int[][] { SModSysConsts.FINS_CL_ACC_LIABTY, SModSysConsts.FINS_CL_ACC_EQUITY, SModSysConsts.FINS_CL_ACC_ORD_CDT, SModSysConsts.FINS_CL_ACC_RES_CDT })) {
                if (resultSetAcc.getString("nat").compareTo(SFiscalConsts.COA_CDT) != 0) {
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNaturaleza inapropiada ('" + resultSetAcc.getString("nat") + "') para la "
                            + "cuenta contable " + resultSetAcc.getString("id_acc") + ":\n'" + resultSetAcc.getString("acc") + "' (" + resultSetAcc.getString("acc_code") + ").");
                }

                sign = -1d;
            }
            else {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nClase de cuenta desconocida para la "
                        + "cuenta contable " + resultSetAcc.getString("id_acc") + ":\n'" + resultSetAcc.getString("acc") + "' (" + resultSetAcc.getString("acc_code") + ").");
            }

            xmlCuenta = createElementAuxiliarCtas11Cuenta(resultSetAcc.getString("acc_code"), resultSetAcc.getString("acc_name"),
                    SLibUtils.round(resultSetAcc.getDouble("f_bal_ope") * sign, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits()),
                    SLibUtils.round(resultSetAcc.getDouble("f_bal_end") * sign, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits()));

            sql = "SELECT r.id_bkc, r.id_tp_rec, r.id_num, r.dt, re.id_ety, re.debit, re.credit, re.concept " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
                    "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND r.b_del = 0 AND re.b_del = 0 " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON " +
                    "re.fid_acc = a.id_acc AND a.pk_acc = " + resultSetAcc.getInt("id_acc") + " ";

            switch (resultSetAcc.getInt("id_tp_fiscal_acc_link")) {
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_CSH:
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_BNK:
                    sql += "AND re.fid_cob_n = " + resultSetAcc.getInt("id_ref_1") + " AND re.fid_ent_n = " + resultSetAcc.getInt("id_ref_2") + " ";
                    break;
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_SUP:
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CUS:
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CDR:
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_DBR:
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_SUP_ADV:
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CUS_ADV:
                    sql += "AND re.fid_bp_nr = " + resultSetAcc.getInt("id_ref_1") + " ";
                    break;
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_INC:
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_EXP:
                    sql += "AND re.fid_item_n = " + resultSetAcc.getInt("id_ref_1") + " ";
                    break;
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_ACC:
                    sql += "";
                    break;
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }

            sql += "WHERE r.id_year = " + periodYear + " AND r.id_per = " + periodMonth + " " +
                    "ORDER BY r.id_bkc, r.id_tp_rec, r.id_num, re.id_ety; ";

            resultSetAccAux = statementAccAux.executeQuery(sql);
            while (resultSetAccAux.next()) {
                xmlDetalleAux = createElementAuxiliarCtas11DetalleAux(resultSetAccAux.getDate("r.dt"), resultSetAccAux.getString("r.id_tp_rec") + "-" + resultSetAccAux.getInt("r.id_num"), resultSetAccAux.getString("re.concept"), resultSetAccAux.getDouble("re.debit"), resultSetAccAux.getDouble("re.credit"));
                xmlCuenta.getXmlElements().add(xmlDetalleAux);
            }

            xmlDoc.getXmlElements().add(xmlCuenta);
        }

        return xmlDoc;
    }
    
    /**
     * Creates XML "Auxiliares de cuenta de nivel mayor y/o de la subcuenta de primer nivel" 1.3.
     * @param session GUI user session.
     * @param periodYear Requested period's year.
     * @param periodMonth Requested period's month.
     * @param tipoSolicitud Request type.
     * @param numOrden Order number.
     * @param numTramite Processing number.
     * @param accountStdStart Starting account code on standard-format.
     * @param accountStdEnd Ending account code on standard-format.
     */    
    public static SXmlDocument createDocAuxiliarCtas13(final SGuiSession session, final int periodYear, final int periodMonth, final String tipoSolicitud, final String numOrden, final String numTramite, final String accountStdStart, final String accountStdEnd) throws Exception {
        int[] coaPeriod = null;
        int[] accountClassKey = null;
        double sign = 0;
        boolean useFixedFiscalAccounts = false;     // when 'true', treat system accounts (e.g., cash, banks, business partners, etc.) as simple fixed fiscal accounts, those provided by SAT, without any individual system subaccount. Otherwise treat each individual system subaccount as a fiscal subaccount
        String sql = "";
        String sqlFilter = "";
        String dateOpen = "";
        String dateClose = "";
        String[] months = null;
        Statement statementAcc = null;
        Statement statementAccAux = null;
        ResultSet resultSetAcc = null;
        ResultSet resultSetAccAux = null;
        SDbBizPartner company = null;
        SXmlDocument xmlDoc = null;
        SXmlElement xmlCuenta = null;
        SXmlElement xmlDetalleAux = null;

        if (periodYear < SFiscalConsts.YEAR_MIN || periodYear > SLibTimeConsts.YEAR_MAX) {
            throw new Exception("El atributo 'Anio' debe ser mínimo " + SFiscalConsts.YEAR_MIN + " y máximo " + SLibTimeConsts.YEAR_MAX + ".");
        }

        if (periodMonth < SLibTimeConsts.MONTH_MIN || periodMonth > SLibTimeConsts.MONTH_MAX) {
            throw new Exception("El atributo 'Mes' debe ser mínimo " + SLibTimeConsts.MONTH_MIN + " y máximo " + SLibTimeConsts.MONTH_MAX + ".");
        }

        company = (SDbBizPartner) session.readRegistry(SModConsts.BPSU_BP, new int[] { session.getConfigCompany().getCompanyId() });

        xmlDoc = new SXmlDocument("AuxiliarCtas:AuxiliarCtas", false) {

            @Override
            public void processXml(String xml) throws Exception {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        xmlDoc.setCustomHeader(
                  "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:AuxiliarCtas=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_3/AuxiliarCtas\" "
                + "xsi:schemaLocation=\"http://www.sat.gob.mx/esquemas/ContabilidadE/1_3/AuxiliarCtas http://www.sat.gob.mx/esquemas/ContabilidadE/1_3/AuxiliarCtas/AuxiliarCtas_1_3.xsd\" ");

        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Version", "1.3"));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("RFC", company.getFiscalId()));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Mes", SLibUtils.DecimalFormatCalendarMonth.format(periodMonth)));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("Anio", SLibUtils.DecimalFormatCalendarYear.format(periodYear)));
        xmlDoc.getXmlAttributes().add(new SXmlAttribute("TipoSolicitud", tipoSolicitud));
        if (!numOrden.isEmpty() && (tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_AF) == 0 || tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_FC) == 0)) {
            xmlDoc.getXmlAttributes().add(new SXmlAttribute("NumOrden", numOrden));
        }
        if (!numTramite.isEmpty() && (tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_DE) == 0 || tipoSolicitud.compareTo(SFiscalConsts.JOV_REQ_TP_CO) == 0)) {
            xmlDoc.getXmlAttributes().add(new SXmlAttribute("NumTramite", numTramite));
        }

        coaPeriod = getSuitableChartOfAccounts(session, periodYear, periodMonth);

        dateOpen = SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.createDate(periodYear, periodMonth));
        dateClose = SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getEndOfMonth(SLibTimeUtils.createDate(periodYear, periodMonth)));

        /*
         * Obtain journal vouchers as follows:
         * - Main query: Accounts ledger.
         *      - Accounts of fiscal account link 1. Cash accounts.
         *      - Accounts of fiscal account link 2. Business partner accounts.
         *      - Accounts of fiscal account link 3. Results accounts.
         *      - Accounts of fiscal account link 4. Other remaining balance accounts.
         * - Subquery: Corresponding journal.
         */

        statementAcc = session.getStatement().getConnection().createStatement();
        statementAccAux = session.getStatement().getConnection().createStatement();

        if (accountStdStart.isEmpty() && accountStdEnd.isEmpty()) {
            sqlFilter = "";
        }
        else if (!accountStdStart.isEmpty() && accountStdEnd.isEmpty()) {
            sqlFilter = "AND a.code >= '" + accountStdStart + "' ";
        }
        else if (accountStdStart.isEmpty() && !accountStdEnd.isEmpty()) {
            sqlFilter = "AND a.code <= '" + accountStdStart + "' ";
        }
        else {
            sqlFilter = "AND a.code BETWEEN '" + accountStdStart + "' AND '" + accountStdEnd + "' ";
        }

        sql = "SELECT " +
                "fld.id_acc, " +
                "fld.id_tp_fiscal_acc_link, " +
                "fld.id_ref_1, " +
                "fld.id_ref_2, " +
                "fld.acc_code, " +
                "fld.acc_name, " +
                "fld.nat, " +
                "a.id_acc, " +
                "a.acc, " +
                "al.fid_tp_acc_r, " +
                "al.fid_cl_acc_r, " +
                "SUM(IF(r.dt < '" + dateOpen + "', re.debit - re.credit, 0.0)) AS f_bal_ope, " +
                "SUM(re.debit - re.credit) AS f_bal_end " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND r.b_del = 0 AND re.b_del = 0 " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON re.fid_acc = a.id_acc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND " +
                "al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_BAL + " AND al.fid_tp_acc_spe = " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fld ON fld.id_year = " + coaPeriod[0] + " AND fld.id_per = " + coaPeriod[1] + " AND " +
                "fld.id_acc = a.pk_acc AND fld.id_tp_fiscal_acc_link BETWEEN " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_CSH + " AND " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_BNK + " AND " +
                "fld.id_ref_1 = re.fid_cob_n AND fld.id_ref_2 = re.fid_ent_n " +
                "WHERE r.id_year = " + periodYear + " AND r.dt <= '" + dateClose + "' " + sqlFilter +
                "GROUP BY fld.id_acc, fld.id_tp_fiscal_acc_link, fld.id_ref_1, fld.id_ref_2, fld.acc_code, fld.acc_name, fld.nat, a.id_acc, a.acc, al.fid_tp_acc_r, al.fid_cl_acc_r " +
                "" +
                "UNION " +
                "" +
                "SELECT " +
                "fld.id_acc, " +
                "fld.id_tp_fiscal_acc_link, " +
                "fld.id_ref_1, " +
                "fld.id_ref_2, " +
                "fld.acc_code, " +
                "fld.acc_name, " +
                "fld.nat, " +
                "a.id_acc, " +
                "a.acc, " +
                "al.fid_tp_acc_r, " +
                "al.fid_cl_acc_r, " +
                "SUM(IF(r.dt < '" + dateOpen + "', re.debit - re.credit, 0.0)) AS f_bal_ope, " +
                "SUM(re.debit - re.credit) AS f_bal_end " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND r.b_del = 0 AND re.b_del = 0 " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON re.fid_acc = a.id_acc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND " +
                "al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_BAL + " AND al.fid_tp_acc_spe BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fld ON fld.id_year = " + coaPeriod[0] + " AND fld.id_per = " + coaPeriod[1] + " AND " +
                "fld.id_acc = a.pk_acc AND fld.id_tp_fiscal_acc_link BETWEEN " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_SUP + " AND " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CUS_ADV + " AND " +
                "fld.id_ref_1 = re.fid_bp_nr AND fld.id_ref_2 = 0 " +
                "WHERE r.id_year = " + periodYear + " AND r.dt <= '" + dateClose + "' " + sqlFilter +
                "GROUP BY fld.id_acc, fld.id_tp_fiscal_acc_link, fld.id_ref_1, fld.id_ref_2, fld.acc_code, fld.acc_name, fld.nat, a.id_acc, a.acc, al.fid_tp_acc_r, al.fid_cl_acc_r " +
                "" +
                "UNION " +
                "" +
                "SELECT " +
                "fld.id_acc, " +
                "fld.id_tp_fiscal_acc_link, " +
                "fld.id_ref_1, " +
                "fld.id_ref_2, " +
                "fld.acc_code, " +
                "fld.acc_name, " +
                "fld.nat, " +
                "a.id_acc, " +
                "a.acc, " +
                "al.fid_tp_acc_r, " +
                "al.fid_cl_acc_r, " +
                "SUM(IF(r.dt < '" + dateOpen + "', re.debit - re.credit, 0.0)) AS f_bal_ope, " +
                "SUM(re.debit - re.credit) AS f_bal_end " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND r.b_del = 0 AND re.b_del = 0 " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON re.fid_acc = a.id_acc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND " +
                "al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_RES + " " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fld ON fld.id_year = " + coaPeriod[0] + " AND fld.id_per = " + coaPeriod[1] + " AND " +
                "fld.id_acc = a.pk_acc AND fld.id_tp_fiscal_acc_link BETWEEN " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_INC + " AND " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_EXP + " AND " +
                "fld.id_ref_1 = re.fid_item_n AND fld.id_ref_2 = 0 " +
                "WHERE r.id_year = " + periodYear + " AND r.dt <= '" + dateClose + "' " + sqlFilter +
                "GROUP BY fld.id_acc, fld.id_tp_fiscal_acc_link, fld.id_ref_1, fld.id_ref_2, fld.acc_code, fld.acc_name, fld.nat, a.id_acc, a.acc, al.fid_tp_acc_r, al.fid_cl_acc_r " +
                "" +
                "UNION " +
                "" +
                "SELECT " +
                "fld.id_acc, " +
                "fld.id_tp_fiscal_acc_link, " +
                "fld.id_ref_1, " +
                "fld.id_ref_2, " +
                "fld.acc_code, " +
                "fld.acc_name, " +
                "fld.nat, " +
                "a.id_acc, " +
                "a.acc, " +
                "al.fid_tp_acc_r, " +
                "al.fid_cl_acc_r, " +
                "SUM(IF(r.dt < '" + dateOpen + "', re.debit - re.credit, 0.0)) AS f_bal_ope, " +
                "SUM(re.debit - re.credit) AS f_bal_end " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND r.b_del = 0 AND re.b_del = 0 " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON re.fid_acc = a.id_acc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS al ON al.code = CONCAT(LEFT(a.code, 6), REPEAT('0', 42)) AND " +
                "al.fid_tp_acc_r = " + SModSysConsts.FINS_TP_ACC_BAL + " AND al.fid_tp_acc_spe <> " + SModSysConsts.FINS_TP_ACC_SPE_ENT_CSH + " AND al.fid_tp_acc_spe NOT BETWEEN " + SModSysConsts.FINS_TP_ACC_SPE_BPR_SUP + " AND " + SModSysConsts.FINS_TP_ACC_SPE_BPR_DBR + " " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " AS fld ON fld.id_year = " + coaPeriod[0] + " AND fld.id_per = " + coaPeriod[1] + " AND " +
                "fld.id_acc = a.pk_acc AND fld.id_tp_fiscal_acc_link = " + SModSysConsts.FINS_TP_FISCAL_ACC_LINK_ACC + " AND " +
                "fld.id_ref_1 = a.pk_acc AND fld.id_ref_2 = 0 " +
                "WHERE r.id_year = " + periodYear + " AND r.dt <= '" + dateClose + "' " + sqlFilter +
                "GROUP BY fld.id_acc, fld.id_tp_fiscal_acc_link, fld.id_ref_1, fld.id_ref_2, fld.acc_code, fld.acc_name, fld.nat, a.id_acc, a.acc, al.fid_tp_acc_r, al.fid_cl_acc_r " +
                "" +
                "ORDER BY acc_code, acc_name; ";

        resultSetAcc = statementAcc.executeQuery(sql);
        while (resultSetAcc.next()) {
            if (resultSetAcc.getString("acc_code") == null) {
                months = SLibTimeUtils.createMonthsOfYearStd(Calendar.LONG);
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNo se ha configurado el código agrupador del SAT para la "
                        + "cuenta contable " + resultSetAcc.getString("id_acc") + ":\n'" + resultSetAcc.getString("acc") + "'.\n"
                        + "El catálogo de cuentas más reciente es: " + months[coaPeriod[1] - 1] + " " + coaPeriod[0] + ".");
            }

            accountClassKey = new int[] { resultSetAcc.getInt("fid_tp_acc_r"), resultSetAcc.getInt("fid_cl_acc_r") };

            if (SLibUtils.belongsTo(accountClassKey, new int[][] { SModSysConsts.FINS_CL_ACC_ASSET, SModSysConsts.FINS_CL_ACC_ORD_DBT, SModSysConsts.FINS_CL_ACC_RES_DBT })) {
                if (resultSetAcc.getString("nat").compareTo(SFiscalConsts.COA_DBT) != 0) {
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNaturaleza inapropiada ('" + resultSetAcc.getString("nat") + "') para la "
                            + "cuenta contable " + resultSetAcc.getString("id_acc") + ":\n'" + resultSetAcc.getString("acc") + "' (" + resultSetAcc.getString("acc_code") + ").");
                }

                sign = 1d;
            }
            else if (SLibUtils.belongsTo(accountClassKey, new int[][] { SModSysConsts.FINS_CL_ACC_LIABTY, SModSysConsts.FINS_CL_ACC_EQUITY, SModSysConsts.FINS_CL_ACC_ORD_CDT, SModSysConsts.FINS_CL_ACC_RES_CDT })) {
                if (resultSetAcc.getString("nat").compareTo(SFiscalConsts.COA_CDT) != 0) {
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nNaturaleza inapropiada ('" + resultSetAcc.getString("nat") + "') para la "
                            + "cuenta contable " + resultSetAcc.getString("id_acc") + ":\n'" + resultSetAcc.getString("acc") + "' (" + resultSetAcc.getString("acc_code") + ").");
                }

                sign = -1d;
            }
            else {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nClase de cuenta desconocida para la "
                        + "cuenta contable " + resultSetAcc.getString("id_acc") + ":\n'" + resultSetAcc.getString("acc") + "' (" + resultSetAcc.getString("acc_code") + ").");
            }

            xmlCuenta = createElementAuxiliarCtas13Cuenta(resultSetAcc.getString("acc_code"), resultSetAcc.getString("acc_name"),
                    SLibUtils.round(resultSetAcc.getDouble("f_bal_ope") * sign, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits()),
                    SLibUtils.round(resultSetAcc.getDouble("f_bal_end") * sign, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits()));

            sql = "SELECT r.id_bkc, r.id_tp_rec, r.id_num, r.dt, re.id_ety, re.debit, re.credit, re.concept " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
                    "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND r.b_del = 0 AND re.b_del = 0 " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON " +
                    "re.fid_acc = a.id_acc AND a.pk_acc = " + resultSetAcc.getInt("id_acc") + " ";

            switch (resultSetAcc.getInt("id_tp_fiscal_acc_link")) {
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_CSH:
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_CSH_BNK:
                    sql += "AND re.fid_cob_n = " + resultSetAcc.getInt("id_ref_1") + " AND re.fid_ent_n = " + resultSetAcc.getInt("id_ref_2") + " ";
                    break;
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_SUP:
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CUS:
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CDR:
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_DBR:
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_SUP_ADV:
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_BPS_CUS_ADV:
                    sql += "AND re.fid_bp_nr = " + resultSetAcc.getInt("id_ref_1") + " ";
                    break;
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_INC:
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_RES_EXP:
                    sql += "AND re.fid_item_n = " + resultSetAcc.getInt("id_ref_1") + " ";
                    break;
                case SModSysConsts.FINS_TP_FISCAL_ACC_LINK_ACC:
                    sql += "";
                    break;
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }

            sql += "WHERE r.id_year = " + periodYear + " AND r.id_per = " + periodMonth + " " +
                    "ORDER BY r.id_bkc, r.id_tp_rec, r.id_num, re.id_ety; ";

            resultSetAccAux = statementAccAux.executeQuery(sql);
            while (resultSetAccAux.next()) {
                xmlDetalleAux = createElementAuxiliarCtas13DetalleAux(resultSetAccAux.getDate("r.dt"), resultSetAccAux.getString("r.id_tp_rec") + "-" + resultSetAccAux.getInt("r.id_num"), resultSetAccAux.getString("re.concept"), resultSetAccAux.getDouble("re.debit"), resultSetAccAux.getDouble("re.credit"));
                xmlCuenta.getXmlElements().add(xmlDetalleAux);
            }

            xmlDoc.getXmlElements().add(xmlCuenta);
        }

        return xmlDoc;
    }
}
