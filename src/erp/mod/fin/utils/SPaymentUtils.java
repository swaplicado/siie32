package erp.mod.fin.utils;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SDbPayment;
import erp.mod.fin.db.SRowPayments;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import sa.lib.SLibUtils;

/**
 * Utilidades para la consulta y mapeo de pagos del módulo financiero.
 *
 * @author Edwin Carmona
 */
public class SPaymentUtils {

    /**
     * Mapea una fila del {@link ResultSet} a un objeto {@link SRowPayments}.
     *
     * @param resultSet resultado de la consulta SQL posicionado en la fila a mapear
     * @return objeto {@link SRowPayments} con los datos del pago
     * @throws Exception si ocurre un error al leer el {@link ResultSet}
     */
    private static SRowPayments mapRowPayment(ResultSet resultSet) throws Exception {
        SRowPayments row = new SRowPayments();
        row.setIdYear(resultSet.getInt("d.id_year"));
        row.setIdDoc(resultSet.getInt("d.id_doc"));
        row.setIdPayment(resultSet.getInt("p.id_pay"));
        row.setBeneficiary(resultSet.getString("b.bp"));
        row.setPayNum(resultSet.getString("folio_p"));
        row.setDocNum(resultSet.getString("folio_d"));
        row.setAmount(resultSet.getDouble("des_pay_app_ety_cur"));
        row.setCur(resultSet.getString("c.cur_key"));
        row.setExchangeRate(resultSet.getDouble("p.pay_exc_rate_app"));
        row.setAmountCurrencyToPay(resultSet.getDouble("p.pay_app_cur"));
        row.setCurToPay(resultSet.getString("cp.cur_key"));
        row.setReceptionPayReq(resultSet.getBoolean("b_rcpt_pay_req"));
        row.setFuncArea(resultSet.getInt("p.fk_func"));
        row.setFuncSubarea(resultSet.getInt("p.fk_func_sub"));
        row.setIdBeneficiary(resultSet.getInt("p.fk_ben"));
        row.setNotes(resultSet.getString("p.nts"));
        row.setOperationType(resultSet.getString("p.pay_tp_op"));
        row.setIsDocAdvance(row.getOperationType().equals(SDbPayment.OPERATION_TYPE_DOC_ADVANCE));
        row.setDateScheduled(resultSet.getDate("p.dt_sched_n"));
        row.setInstallment(resultSet.getInt("pe.install"));
        row.setDocBalancePrevAppCy(resultSet.getInt("pe.doc_bal_prev_app_cur"));
        row.setDocBalanceUnpayAppCy(resultSet.getInt("pe.doc_bal_unpd_app_cur_r"));
        row.setDocBalancePrevCy(resultSet.getInt("pe.doc_bal_prev_cur"));
        row.setDocBalanceUnpayCy(resultSet.getInt("pe.doc_bal_unpd_cur_r"));
        row.setSelected(false);
        
        return row;
    }

    /**
     * Consulta un pago por su identificador.
     *
     * <p>Realiza un JOIN entre las tablas de pagos, beneficiarios, monedas y
     * documentos para obtener toda la información necesaria del pago.</p>
     *
     * @param connection conexión activa a la base de datos
     * @param idPay      identificador del pago a leer
     * @return objeto {@link SRowPayments} con los datos del pago, o {@code null} si no existe
     * @throws Exception si ocurre un error en la consulta SQL
     */
    public static SRowPayments readPayment(Connection connection, int idPay) throws Exception {
        String sql = "SELECT d.id_year, d.id_doc, p.id_pay, " +
                "b.bp, " +
                "IF(p.ser <> '', CONCAT(p.ser, '-', p.num), p.num) AS folio_p, " +
                "IF(d.num_ser <> '', CONCAT(d.num_ser, '-', d.num), d.num) AS folio_d, " +
                "pe.des_pay_app_ety_cur, " +
                "c.cur_key, " +
                "p.pay_app_cur, " +
                "cp.cur_key, " +
                "p.pay_exc_rate_app, " +
                "b_rcpt_pay_req, " +
                "p.fk_func, p.fk_func_sub, " +
                "p.fk_ben, " +
                "p.nts, " +
                "p.pay_tp_op, " +
                "p.dt_sched_n, " +
                "pe.install, " +
                "pe.doc_bal_prev_app_cur, " +
                "pe.doc_bal_unpd_app_cur_r, " +
                "pe.doc_bal_prev_cur, " +
                "pe.doc_bal_unpd_cur_r " +
                "FROM fin_pay AS p " +
                "INNER JOIN fin_pay_ety AS pe ON p.id_pay = pe.id_pay " +
                "INNER JOIN erp.bpsu_bp AS b ON p.fk_ben = b.id_bp " +
                "INNER JOIN erp.cfgu_cur AS c ON pe.fk_ety_cur = c.id_cur " +
                "INNER JOIN erp.cfgu_cur AS cp ON p.fk_cur = cp.id_cur " +
                "LEFT JOIN trn_dps AS d ON pe.fk_doc_year_n = d.id_year AND pe.fk_doc_doc_n = d.id_doc " +
                "LEFT JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
                "WHERE NOT p.b_del AND p.id_pay = " + idPay;
        Statement statement = connection.createStatement();
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return mapRowPayment(resultSet);
            }
        }
        return null;
    }
    
    public static class PurchaseDpsBalance {
        
        double dBalance;
        double dBalanceCy;
        double dPaymentsPendCy;
        double dBalanceNet;
        double dBalanceNetCy;
        
        public PurchaseDpsBalance(final double balance, 
                                    final double balanceCy, 
                                    final double paymentsPendCy, 
                                    final double balanceNet,
                                    final double balanceNetCy) {
            dBalance = balance;
            dBalanceCy = balanceCy;
            dPaymentsPendCy = paymentsPendCy;
            dBalanceNet = balanceNet;
            dBalanceNetCy = balanceNetCy;
            
            if (dBalance != 0) {
                dBalance *= -1; // purchases have negative balance by defect
            }

            if (dBalanceCy != 0) {
                dBalanceCy *= -1; // purchases have negative balance by defect
            }

            if (dBalanceNet != 0) {
                dBalanceNet *= -1; // purchases have negative balance by defect
            }

            if (dBalanceNetCy != 0) {
                dBalanceNetCy *= -1; // purchases have negative balance by defect
            }
        }

        public double getBalance() {
            return dBalance;
        }

        public void setBalance(double Balance) {
            this.dBalance = Balance;
        }

        public double getBalanceCy() {
            return dBalanceCy;
        }

        public void setBalanceCy(double BalanceCy) {
            this.dBalanceCy = BalanceCy;
        }

        public double getPaymentsPendCy() {
            return dPaymentsPendCy;
        }

        public void setPaymentsPendCy(double PaymentsPendCy) {
            this.dPaymentsPendCy = PaymentsPendCy;
        }

        public double getBalanceNet() {
            return dBalanceNet;
        }

        public void setBalanceNet(double BalanceNet) {
            this.dBalanceNet = BalanceNet;
        }

        public double getBalanceNetCy() {
            return dBalanceNetCy;
        }

        public void setBalanceNetCy(double BalanceNetCy) {
            this.dBalanceNetCy = BalanceNetCy;
        }
    }
    
    
    /**
     * Calcula el monto del pago en la moneda del documento (cy).
     *
     * @param miClient       cliente de sesión
     * @param fkCurrencyId   ID de la moneda del pago
     * @param fkEntryCurrencyId ID de la moneda de la partida
     * @param paymentAmountCy   monto en la moneda de la partida
     * @param paymentAmountApplication monto en moneda local
     * @param date           fecha para obtener tipo de cambio
     * @return monto calculado en la moneda del documento
     * @throws Exception si no hay tipo de cambio disponible
     */
    public static double calculatePaymentCy(SClientInterface miClient,
                                            int fkCurrencyId,
                                            int fkEntryCurrencyId,
                                            double paymentAmountCy,
                                            double paymentAmountApplication,
                                            Date date) throws Exception {
        double paymentCy = 0;

        if (miClient.getSession().getSessionCustom().isLocalCurrency(new int[] { fkCurrencyId })) {
            paymentCy = paymentAmountApplication;
        }
        else {
            if (fkCurrencyId == fkEntryCurrencyId) {
                paymentCy = paymentAmountCy;
            }
            else {
                if (miClient.getSession().getSessionCustom().isLocalCurrency(new int[] { fkEntryCurrencyId })) {
                    double excRate = SDataUtilities.obtainExchangeRate(miClient, fkCurrencyId, date);
                    if (excRate == 0d) {
                        throw new Exception("No hay tipo de cambio disponible para la moneda ID=" + fkCurrencyId + ".");
                    }
                    paymentCy = SLibUtils.roundAmount(paymentAmountCy * SLibUtils.round(1 / excRate, 6));
                }
                else {
                    double excRatePay = SDataUtilities.obtainExchangeRate(miClient, fkCurrencyId, date);
                    double excRateToPay = SDataUtilities.obtainExchangeRate(miClient, fkEntryCurrencyId, date);
                    if (excRatePay == 0d || excRateToPay == 0d) {
                        throw new Exception("No hay tipo de cambio disponible para las monedas ID=" + fkCurrencyId + " o ID=" + fkEntryCurrencyId + ".");
                    }
                    paymentCy = SLibUtils.roundAmount(paymentAmountCy * SLibUtils.round(excRateToPay / excRatePay, 6));
                }
            }
        }

        return paymentCy;
    }

    public static PurchaseDpsBalance getDpsBalance(Statement oStatement,
                                            final int idYear,
                                            final int idDoc,
                                            final boolean isDocAdvance,
                                            final int idLayout) throws Exception {
        return getDpsBalance(oStatement, idYear, idDoc, isDocAdvance, 0d, idLayout);
    }

    public static PurchaseDpsBalance getDpsBalance(Statement oStatement,
                                            final SClientInterface miClient,
                                            final int fkCurrencyId,
                                            final int fkEntryCurrencyId,
                                            final double paymentAmountCy,
                                            final double paymentAmountApplication,
                                            final Date date,
                                            final int idYear,
                                            final int idDoc,
                                            final boolean isDocAdvance,
                                            final int idLayout) throws Exception {
        double paymentCy = calculatePaymentCy(miClient, fkCurrencyId, fkEntryCurrencyId, paymentAmountCy, paymentAmountApplication, date);
        return getDpsBalance(oStatement, idYear, idDoc, isDocAdvance, paymentCy, idLayout);
    }

    private static PurchaseDpsBalance getDpsBalance(Statement oStatement,
                                            final int idYear,
                                            final int idDoc,
                                            final boolean isDocAdvance,
                                            final double paymentCy,
                                            final int idLayout) throws Exception {
        PurchaseDpsBalance dpsBalance = null;
        String sql = "";
        if (! isDocAdvance) {
            sql += "SELECT " +
                "SUM(re.debit - re.credit) AS f_bal," +
                "SUM(IF(re.fid_cur <> d.fid_cur, 0.0, re.debit_cur - re.credit_cur)) AS f_bal_cur, " +
                "COALESCE(ps.sum_pay_cur, 0.0) AS f_pay_pend_cur, " +
                "SUM(re.debit - re.credit) + COALESCE(ps.sum_pay, 0.0) AS f_bal_net, " +
                "SUM(IF(re.fid_cur <> d.fid_cur, 0.0, re.debit_cur - re.credit_cur)) + COALESCE(ps.sum_pay_cur, 0.0) AS f_bal_net_cur " +
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                "r.b_del = 0 AND re.b_del = 0 AND r.id_year = " + idYear + " " +
                "INNER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc " +
                "LEFT JOIN (" +
                "  SELECT " +
                "    pe.fk_doc_year_n AS id_year, " +
                "    pe.fk_doc_doc_n AS id_doc, " +
                "    SUM(pe.ety_pay) AS sum_pay, " +
                "    SUM(pe.des_pay_app_ety_cur) AS sum_pay_cur " +
                "  FROM fin_pay AS p " +
                "  INNER JOIN fin_pay_ety AS pe ON p.id_pay = pe.id_pay " +
                "  WHERE NOT p.b_del " +
                "    AND p.fk_st_pay IN (" +
                "   " + SModSysConsts.FINS_ST_PAY_NEW + ", " +
                "   " + SModSysConsts.FINS_ST_PAY_IN_AUTH + ", " +
                "   " + SModSysConsts.FINS_ST_PAY_SCHED + ", " + 
                "   " + SModSysConsts.FINS_ST_PAY_SCHED_P + ", " +
                /*
                "   " + SModSysConsts.FINS_ST_PAY_SUBR + ", " + 
                "   " + SModSysConsts.FINS_ST_PAY_SUBR_P + ", " +
                */
                "   " + SModSysConsts.FINS_ST_PAY_BLOC + ", " + 
                "   " + SModSysConsts.FINS_ST_PAY_BLOC_P + ") " +
                "  GROUP BY pe.fk_doc_year_n, pe.fk_doc_doc_n " +
                ") AS ps ON ps.id_year = d.id_year AND ps.id_doc = d.id_doc ";
            sql += "WHERE re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_CT_SYS_MOV_BPS + " AND "
                    + "re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + " ";
        }
        else {
            sql += "SELECT " +
                    "d.tot_r, " +
                    "COALESCE(p.payed, 0) * -1 AS _payed, " +
                    "(d.tot_r - COALESCE(p.payed, 0)) * -1 AS f_bal, " +
                    "(d.tot_cur_r - COALESCE(p.payed_cur, 0)) * -1 AS f_bal_cur, " +
                    "COALESCE(p.payed_pend_cur, 0) * -1 AS f_pay_pend_cur, " +
                    "(d.tot_r - COALESCE(p.payed, 0) - COALESCE(p.payed_pend, 0)) * -1 AS f_bal_net, " +
                    "(d.tot_cur_r - COALESCE(p.payed_cur, 0) - COALESCE(p.payed_pend_cur, 0)) * -1 AS f_bal_net_cur " +
                "FROM trn_dps AS d " +
                "LEFT JOIN (" +
                    "SELECT " +
                        "pre.fk_doc_year_n, " +
                        "pre.fk_doc_doc_n, " +
                        "SUM(CASE WHEN pr.fk_st_pay IN (" + 
                            SModSysConsts.FINS_ST_PAY_SUBR + ", " + 
                            SModSysConsts.FINS_ST_PAY_SUBR_P + 
                        ") THEN ety_pay ELSE 0 END) AS payed, " +
                        "SUM(CASE WHEN pr.fk_st_pay IN (" + 
                            SModSysConsts.FINS_ST_PAY_SUBR + ", " + 
                            SModSysConsts.FINS_ST_PAY_SUBR_P + 
                        ") THEN des_pay_app_ety_cur ELSE 0 END) AS payed_cur, " +
                        "SUM(CASE WHEN pr.fk_st_pay IN (" +
                            SModSysConsts.FINS_ST_PAY_NEW + ", " +
                            SModSysConsts.FINS_ST_PAY_IN_AUTH + ", " +
                            SModSysConsts.FINS_ST_PAY_SCHED + ", " +
                            SModSysConsts.FINS_ST_PAY_SCHED_P + ", " +
                            SModSysConsts.FINS_ST_PAY_BLOC + ", " +
                            SModSysConsts.FINS_ST_PAY_BLOC_P +
                        ") THEN des_pay_app_ety_cur ELSE 0 END) AS payed_pend_cur, " +
                        "SUM(CASE WHEN pr.fk_st_pay IN (" +
                            SModSysConsts.FINS_ST_PAY_NEW + ", " +
                            SModSysConsts.FINS_ST_PAY_IN_AUTH + ", " +
                            SModSysConsts.FINS_ST_PAY_SCHED + ", " +
                            SModSysConsts.FINS_ST_PAY_SCHED_P + ", " +
                            SModSysConsts.FINS_ST_PAY_BLOC + ", " +
                            SModSysConsts.FINS_ST_PAY_BLOC_P +
                        ") THEN ety_pay ELSE 0 END) AS payed_pend " +
                    "FROM fin_pay AS pr " +
                    "INNER JOIN fin_pay_ety AS pre ON pr.id_pay = pre.id_pay ";
            if (idLayout > 0) {
                sql += "INNER JOIN fin_pay_lay_bank AS plb ON pr.id_pay = plb.id_pay "
                                                + "AND plb.id_lay_bank <> " + idLayout + " " +
                        "INNER JOIN fin_lay_bank AS l ON l.id_lay_bank = plb.id_lay_bank "
                                                + "AND l.b_del = 0 ";
            }
            sql += "WHERE pr.b_del = 0 " +
                    "AND pr.pay_tp = '" + SDbPayment.TYPE_REQUEST + "' " +
                    "AND pr.pay_tp_op = '" + SDbPayment.OPERATION_TYPE_DOC_ADVANCE + "' " +
                    "GROUP BY pre.fk_doc_year_n, pre.fk_doc_doc_n " +
                ") AS p ON d.id_year = p.fk_doc_year_n AND d.id_doc = p.fk_doc_doc_n ";
            sql += "WHERE EXISTS(SELECT  " +
                "            1 " +
                "        FROM " +
                "            trn_dps_ety AS tde " +
                "        WHERE " +
                "            tde.id_year = d.id_year " +
                "                AND tde.id_doc = d.id_doc " +
                "                AND tde.b_del = 0 " +
                "                AND tde.ops_type = 13 " +
                "        LIMIT 1) ";
        }
        sql += "AND d.id_year = " + idYear + " AND d.id_doc = " + idDoc + ";";
        
        try (ResultSet resultSet = oStatement.executeQuery(sql)) {
            if (resultSet.next()) {
                double balance = resultSet.getDouble("f_bal");
                double balanceCy = resultSet.getDouble("f_bal_cur");
                double paymentsPendCy = SLibUtils.roundAmount(resultSet.getDouble("f_pay_pend_cur") - paymentCy);
                double balanceNet = resultSet.getDouble("f_bal_net");
                double balanceNetCy = SLibUtils.roundAmount(resultSet.getDouble("f_bal_net_cur") - paymentCy);
                
                dpsBalance = new PurchaseDpsBalance(balance, balanceCy, paymentsPendCy, balanceNet, balanceNetCy);
            }
        }
        
        return dpsBalance;
    }
}
