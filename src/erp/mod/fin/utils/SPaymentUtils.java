package erp.mod.fin.utils;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SDbPayment;
import erp.mod.fin.db.SDbPaymentEntry;
import erp.mod.fin.db.SDbPaymentFile;
import erp.mod.fin.db.SRowPayments;
import erp.mod.fin.form.SDialogPaymentChangeStatus;
import erp.swap.SSwapConsts;
import erp.swap.form.SDocumentUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiConsts;

/**
 * Utilidades para la consulta y mapeo de pagos del módulo financiero.
 *
 * @author Edwin Carmona
 */
public class SPaymentUtils {
    
    public static final String SUGGESTION_SPEED_UP = "\nIMPORTANTE:\nSi urge acelerar la actualización de esta modificación, haga clic en el botón ";

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
        row.setOperationType(resultSet.getInt("p.pay_tp_op"));
        row.setIsDocAdvance(row.getOperationType() == SDataConstantsSys.TRNX_OPS_TYPE_OPS_PREPAY_INVOICED);
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
        double dPaymentsPendCyWithoutCurrent;
        
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

        public double getPaymentsPendCyWithoutCurrent() {
            return dPaymentsPendCyWithoutCurrent;
        }

        public void setPaymentsPendCyWithoutCurrent(double d) {
            this.dPaymentsPendCyWithoutCurrent = d;
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
                                            final int finYear,
                                            final int idPayment,
                                            final int idLayout) throws Exception {
        return getDpsBalance(oStatement, idYear, idDoc, isDocAdvance, 0d, finYear, idPayment, idLayout);
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
                                            final int idPayment,
                                            final int idLayout) throws Exception {
        double paymentCy = calculatePaymentCy(miClient, fkCurrencyId, fkEntryCurrencyId, paymentAmountCy, paymentAmountApplication, date);
        // obtener el año del objeto Date recibido:
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int finYear = cal.get(Calendar.YEAR);
        return getDpsBalance(oStatement, idYear, idDoc, isDocAdvance, paymentCy, finYear, idPayment, idLayout);
    }

    private static PurchaseDpsBalance getDpsBalance(Statement oStatement,
                                            final int idYear,
                                            final int idDoc,
                                            final boolean isDocAdvance,
                                            final double paymentCy,
                                            final int finYear,
                                            final int idPayment,
                                            final int idLayout) throws Exception {
        PurchaseDpsBalance dpsBalance = null;
        String sql = "";
        if (! isDocAdvance) {
            sql += "SELECT " +
                "SUM(re.debit - re.credit) AS f_bal," +
                "SUM(IF(re.fid_cur <> d.fid_cur, 0.0, re.debit_cur - re.credit_cur)) AS f_bal_cur, " +
                "COALESCE(ps.sum_pay_cur, 0.0) - COALESCE(pacc.sum_pay_cur, 0.0) AS f_pay_pend_cur, " + // TODOS los pagos tipo 'R' al documento - (menos) pagos cotabilizados tipo 'P'
                "SUM(re.debit - re.credit) + COALESCE(ps.sum_pay, 0.0) AS f_bal_net, " +
                "SUM(IF(re.fid_cur <> d.fid_cur, 0.0, re.debit_cur - re.credit_cur)) + COALESCE(ps.sum_pay_cur, 0.0) AS f_bal_net_cur " +
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                "r.b_del = 0 AND re.b_del = 0 AND r.id_year = " + finYear + " " +
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
                "    AND p.pay_tp = 'R' ";
            if (idPayment > 0) {
                sql += " AND p.id_pay <> " + idPayment + " ";
            }
            sql += " AND p.fk_st_pay NOT IN ("
                    + "   " + SModSysConsts.FINS_ST_PAY_CANC_P + ", "
                    + "   " + SModSysConsts.FINS_ST_PAY_CANC + ") "
                    + "  GROUP BY pe.fk_doc_year_n, pe.fk_doc_doc_n "
                    + ") AS ps ON ps.id_year = d.id_year AND ps.id_doc = d.id_doc "
                    + "LEFT JOIN "
                    + "    (SELECT  "
                    + "        pe.fk_doc_year_n AS id_year, "
                    + "        pe.fk_doc_doc_n AS id_doc, "
                    + "        SUM(pe.ety_pay) AS sum_pay, "
                    + "        SUM(pe.des_pay_app_ety_cur) AS sum_pay_cur "
                    + "    FROM "
                    + "        fin_pay AS p "
                    + "    INNER JOIN fin_pay_ety AS pe ON p.id_pay = pe.id_pay "
                    + "    INNER JOIN fin_pay_lay_bank AS pl ON p.id_pay = pl.id_pay "
                    + "    INNER JOIN fin_lay_bank AS l ON pl.id_lay_bank = l.id_lay_bank "
                    + "    WHERE "
                    + "        p.b_del = 0 AND p.pay_tp = 'P' "
                    + "            AND p.fk_st_pay NOT IN ("
                    + "   " + SModSysConsts.FINS_ST_PAY_CANC_P + ", "
                    + "   " + SModSysConsts.FINS_ST_PAY_CANC + ") "
                    + "            AND l.b_del = 0 "
                    + "            AND l.tra_pay > 0 "
                    + "    GROUP BY pe.fk_doc_year_n , pe.fk_doc_doc_n) AS pacc ON pacc.id_year = d.id_year "
                    + "        AND pacc.id_doc = d.id_doc ";
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
                    "AND pr.pay_tp_op = " + SDataConstantsSys.TRNX_OPS_TYPE_OPS_PREPAY_INVOICED + " " +
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
                double balanceNet = resultSet.getDouble("f_bal_net");
                double paymentsPendCy;
                double paymentsPendCyWithoutCurrent;
                double balanceNetCy;
                if (idPayment == 0) {
                    paymentsPendCy = SLibUtils.roundAmount(resultSet.getDouble("f_pay_pend_cur") - paymentCy);
                    balanceNetCy = SLibUtils.roundAmount(resultSet.getDouble("f_bal_net_cur") - paymentCy);
                }
                else {
                    paymentsPendCy = SLibUtils.roundAmount(resultSet.getDouble("f_pay_pend_cur"));
                    balanceNetCy = SLibUtils.roundAmount(resultSet.getDouble("f_bal_net_cur"));
                }
                paymentsPendCyWithoutCurrent = SLibUtils.roundAmount(resultSet.getDouble("f_pay_pend_cur"));
                
                dpsBalance = new PurchaseDpsBalance(balance, balanceCy, paymentsPendCy, balanceNet, balanceNetCy);
                dpsBalance.setPaymentsPendCyWithoutCurrent(paymentsPendCyWithoutCurrent);
            }
        }
        
        return dpsBalance;
    }
    

    /**
     * Obtiene los pagos asociados a un documento en layouts distintos al especificado.
     * 
     * <p>Esta función realiza una consulta SQL para recuperar los pagos relacionados con un documento
     * específico, excluyendo aquellos que pertenecen al layout proporcionado. Los resultados se almacenan
     * en un HashMap donde la clave es el ID del pago y el valor es un arreglo de dos elementos:
     * [0] = ety_pay_app
     * [1] = des_pay_app_ety_cur</p>
     * 
     * @param oStatement conexión activa a la base de datos para ejecutar la consulta
     * @param idYear año fiscal del documento
     * @param idDoc ID del documento
     * @param idLayout ID del layout a excluir
     * 
     * @return HashMap con los pagos encontrados, donde la clave es el ID del pago y el valor es un arreglo de dos elementos
     */
    public static HashMap<Integer, double[]> getPaymentsInOtherLayoutsByDoc(Statement oStatement,
                                            final int idYear,
                                            final int idDoc,
                                            final int idLayout) {
        String sql = "select " +
                    "	l.id_lay_bank, " +
                    "	l.dt_lay, " +
                    "	l.dt_due, " +
                    "	l.cpt, " +
                    "	l.con, " +
                    "	l.amt, " +
                    "	l.amt_pay, " +
                    "	(l.amt - l.amt_pay) as f_amt_x_pay, " +
                    "	l.tra, " +
                    "	l.tra_pay, " +
                    "	(l.tra - l.tra_pay) as f_tra_x_pay, " +
                    "	l.b_clo_pay, " +
                    "   fp.id_pay, " +
                    "	fpe.des_pay_app_ety_cur, " +
                    "   fpe.ety_pay_app " +
                    "from " +
                    "	fin_lay_bank as l " +
                    "	inner join fin_pay_lay_bank fplb on l.id_lay_bank = fplb.id_lay_bank  " +
                    "	inner join fin_pay fp on fplb.id_pay = fp.id_pay  " +
                    "	inner join fin_pay_ety fpe on fp.id_pay = fpe.id_pay " +
                    "where " +
                    "	l.trn_tp = 2 " +
                    "	and l.b_del = 0 " +
                    "	and fp.b_del = 0 " +
                    "	and fp.pay_tp = 'R' " +
                    "	and fpe.fk_doc_year_n = " + idYear + " " +
                    "	and fpe.fk_doc_doc_n = " + idDoc + " " +
                    "	and l.id_lay_bank <> " + idLayout + " " +
                    "having " +
                    "	f_tra_x_pay <> 0 " +
                    "	and l.b_clo_pay = 0 " +
                    "order by " +
                    "	l.dt_lay, " +
                    "	l.dt_due, " +
                    "	l.id_lay_bank;";

        HashMap<Integer, double[]> paymentsInOtherLayouts = new HashMap<>();
        try (ResultSet resultSet = oStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                double[] values = new double[2];
                values[0] = resultSet.getDouble("ety_pay_app");
                values[1] = resultSet.getDouble("des_pay_app_ety_cur");
                paymentsInOtherLayouts.put(resultSet.getInt("id_pay"), values);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return paymentsInOtherLayouts;
    }

    /**
     * Obtiene el número de parcialidades de un documento de compra.
     *
     * <p>Este método consulta la base de datos para contar las partidas de recepción
     * relacionadas con un documento específico. Filtra solo los registros no eliminados
     * y que correspondan a movimientos de compra con débito positivo.</p>
     *
     * <p>La consulta realiza JOINs entre:</p>
     * <ul>
     *   <li><strong>fin_rec</strong> (r): Tabla de recepciones de compra</li>
     *   <li><strong>fin_rec_ety</strong> (re): Partidas de recepción</li>
     *   <li><strong>trn_dps</strong> (d): Documentos (órdenes de compra, facturas, etc.)</li>
     * </ul>
     *
     * @param oStatement     conexión activa a la base de datos para ejecutar la consulta
     * @param idYear         año fiscal del documento a consultar
     * @param idDoc          identificador único del documento de compra
     * @param finYear        año fiscal para filtrar las recepciones activas
     * @return número de cuotas/instalaciones encontradas; 0 si no hay registros
     * @throws Exception si ocurre un error al ejecutar la consulta SQL
     * 
     * @see SDataConstantsSys#FINS_CT_SYS_MOV_BPS
     * @see SDataConstantsSys#FINS_TP_SYS_MOV_BPS_SUP
     */
    public static int getDpsNumInstallments(Statement oStatement, 
                                                final int idYear, 
                                                final int idDoc, 
                                                final int finYear) throws Exception {
        int installments = 0;
        
        String sql = "SELECT  "
                + "    COUNT(*) AS installments "
                + "FROM "
                + "    fin_rec AS r "
                + "        INNER JOIN "
                + "    fin_rec_ety AS re ON r.id_year = re.id_year "
                + "        AND r.id_per = re.id_per "
                + "        AND r.id_bkc = re.id_bkc "
                + "        AND r.id_tp_rec = re.id_tp_rec "
                + "        AND r.id_num = re.id_num "
                + "        AND r.b_del = 0 "  // Solo recepciones no eliminadas
                + "        AND re.b_del = 0 "  // Solo partidas no eliminadas
                + "        AND r.id_year = " + finYear + " "  // Filtrar por año fiscal
                + "        INNER JOIN "
                // JOIN con documentos asociados a las partidas de recepción
                + "    trn_dps AS d ON re.fid_dps_year_n = d.id_year "
                + "        AND re.fid_dps_doc_n = d.id_doc "
                + "WHERE "
                // Filtrar por tipo de movimiento: Movimiento de compra (BPS = Buy Purchase Supp)
                + "    re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_CT_SYS_MOV_BPS + " "
                // Filtrar por tipo específico de movimiento de compra
                + "    AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + " "
                + "    AND re.debit > 0 "
                + "    AND d.id_year = " + idYear + ""
                + "    AND d.id_doc = " + idDoc + ";";

        try (ResultSet resultSet = oStatement.executeQuery(sql)) {
            if (resultSet.next()) {
                installments = resultSet.getInt("installments");
            }
        }

        return installments;
    }
    
    /**
     * Mapea un tipo de archivo de pago interno a un tipo de archivo compatible con SWAP Services.
     *
     * <p>Esta función traduce los códigos de tipo de archivo definidos en {@link SDbPaymentFile}
     * a las constantes de tipo de archivo definidas en {@link SSwapConsts} que se utilizan
     * en la integración con SWAP Services.</p>
     *
     * <p>Los mapeos son los siguientes:</p>
     * <ul>
     *   <li><strong>FILE_TP_EF</strong> (Evidencia Final) → {@link SSwapConsts#FILE_TYPE_GRAPHIC_EVIDENCE}</li>
     *   <li><strong>FILE_TP_RA</strong> (Reporte Avance) → {@link SSwapConsts#FILE_TYPE_GRAPHIC_EVIDENCE_PARTIAL}</li>
     *   <li><strong>FILE_TP_PF</strong> (Papel Fiscal) → {@link SSwapConsts#FILE_TYPE_PAY_VOUCHER}</li>
     *   <li><strong>FILE_TP_OS</strong> (Otros Soportes) → {@link SSwapConsts#FILE_TYPE_PAY_SUPP}</li>
     *   <li><strong>Otros</strong> (Defecto) → {@link SSwapConsts#FILE_TYPE_PAY_SUPP}</li>
     * </ul>
     *
     * @param paymentFileType código de tipo de archivo de pago a mapear
     * @return constante entera que representa el tipo de archivo en SWAP Services;
     *         retorna {@link SSwapConsts#FILE_TYPE_PAY_SUPP} como valor por defecto
     *         si el tipo de archivo no coincide con ninguno de los casos definidos
     * 
     * @see SDbPaymentFile
     * @see SSwapConsts
     */
    /**
     * Ejecuta la acción de marcar un pago como operado.
     *
     * @param miClient                   cliente de sesión
     * @param primaryKey
     * @param oDialogPaymentChangeStatus  diálogo para capturar los datos de la operación
     * @param exportButtonTooltip        tooltip del botón de exportar (para el mensaje informativo)
     * @param gridType                   tipo de grid para notificar suscriptores
     * @return {@code true} si el pago fue marcado como operado exitosamente
     * @throws Exception si ocurre un error durante el proceso
     */
    public static boolean markAsPaid(SClientInterface miClient,
                                     int[] primaryKey,
                                     SDialogPaymentChangeStatus oDialogPaymentChangeStatus,
                                     String exportButtonTooltip,
                                     int gridType) throws Exception {
        SDbPayment oPayment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, primaryKey);
        int status = oPayment.getFkStatusPaymentId();

        if (status == SModSysConsts.FINS_ST_PAY_SCHED || status == SModSysConsts.FINS_ST_PAY_EXEC) {
            if (status == SModSysConsts.FINS_ST_PAY_SCHED) {
                oDialogPaymentChangeStatus.setFormCase(SDialogPaymentChangeStatus.CASE_MARK_AS_PAID);
            }
            else {
                oDialogPaymentChangeStatus.setFormCase(SDialogPaymentChangeStatus.CASE_CHANGE_BANK_ACCOUNT);
            }
            
            oDialogPaymentChangeStatus.setRegistry(oPayment);
            oDialogPaymentChangeStatus.setVisible(true);

            if (oDialogPaymentChangeStatus.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                Date date = (Date) oDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_DATE);
                double exchangeRate = SDocumentUtils.getExchangeRate(miClient.getSession(), oPayment.getFkCurrencyId(), date);
                double amount = (double) oDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_PAYMENT);
                int[] paymentBankKey = (int[]) oDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_PAYMENT_BANK);
                int[] benefBankKey = (int[]) oDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_BENEFIT_BANK);
                SDbPaymentEntry oSingleEntry = oPayment.getSingleEntry();
                
                if (oDialogPaymentChangeStatus.getFormCase() == SDialogPaymentChangeStatus.CASE_MARK_AS_PAID) {
                    oPayment.setAuxReloadEntries(false);
                    oPayment.setFkStatusPaymentId(SModSysConsts.FINS_ST_PAY_EXEC_P);
                    oPayment.setDateExecution_n(date);
                    oPayment.setExecutedManually(true);
                    oPayment.setFkUserExecutiondId(miClient.getSession().getUser().getPkUserId());
                }

                if (paymentBankKey != null) {
                    oPayment.setFkPayerCashBizPartnerBranchId_n(paymentBankKey[0]);
                    oPayment.setFkPayerCashAccountingCashId_n(paymentBankKey[1]);
                }
                else {
                    oPayment.setFkPayerCashBizPartnerBranchId_n(0);
                    oPayment.setFkPayerCashAccountingCashId_n(0);
                }

                if (benefBankKey != null) {
                    oPayment.setFkBeneficiaryBankBizParterBranchId_n(benefBankKey[0]);
                    oPayment.setFkBeneficiaryBankAccountCashId_n(benefBankKey[1]);
                }
                else {
                    oPayment.setFkBeneficiaryBankBizParterBranchId_n(0);
                    oPayment.setFkBeneficiaryBankAccountCashId_n(0);
                }
                
                if (oDialogPaymentChangeStatus.getFormCase() == SDialogPaymentChangeStatus.CASE_MARK_AS_PAID) {
                    oPayment.processPaymentAtExecution(miClient.getSession(), amount, exchangeRate, oSingleEntry.getDocInstallment(), oSingleEntry.getDocBalancePreviousCy());
                }

                miClient.showMsgBoxInformation("La solicitud de pago '" + oPayment.getFolio() + "' se actualizará de manera automática en el " + SSwapConsts.PURCHASE_PORTAL + ".\n"
                        + SUGGESTION_SPEED_UP + "'" + exportButtonTooltip + "'.");

                oPayment.save(miClient.getSession());
                miClient.getSession().notifySuscriptors(gridType);
                return true;
            }
        } else {
            switch (status) {
                case SModSysConsts.FINS_ST_PAY_SCHED_P:
                    miClient.showMsgBoxInformation("La solicitud de pago '" + oPayment.getFolio() + "' está en proceso de quedar autorizada.\n"
                            + "Intente más tarde de favor.");
                    break;
                default:
                    throw new UnsupportedOperationException(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        return false;
    }

    public static int mapPaymentFileType(String paymentFileType) {
        switch (paymentFileType) {
            case SDbPaymentFile.FILE_TP_EF:
                return SSwapConsts.FILE_TYPE_GRAPHIC_EVIDENCE;
            case SDbPaymentFile.FILE_TP_RA:
                return SSwapConsts.FILE_TYPE_GRAPHIC_EVIDENCE_PARTIAL;
            case SDbPaymentFile.FILE_TP_PF:
                return SSwapConsts.FILE_TYPE_PAY_VOUCHER;
            case SDbPaymentFile.FILE_TP_OS:
                return SSwapConsts.FILE_TYPE_PAY_SUPP;
            default:
                return SSwapConsts.FILE_TYPE_PAY_SUPP;
        }
    }
    
    
    /**
     * Verifica si el código de referencia del ítem indica un activo fijo.
     * Códigos válidos: ADI, AET, AMC, AME, AMM, AMU
     *
     * @param refItem código de referencia del ítem
     * @return true si el ítem indica activo fijo, false en caso contrario
     */
    private static boolean isFixedAssetItem(String refItem) {
        if (refItem == null || refItem.isEmpty()) {
            return false;
        }
        return refItem.startsWith("ADI") || refItem.startsWith("AET") || 
               refItem.startsWith("AMC") || refItem.startsWith("AME") || 
               refItem.startsWith("AMM") || refItem.startsWith("AMU");
    }
    
    /**
     * Verifica si el código de referencia del ítem indica un gasto.
     * Códigos válidos: GA, GC, GF, GO, GP, GV, HA, OA, OP, OV
     *
     * @param refItem código de referencia del ítem
     * @return true si el ítem indica gasto, false en caso contrario
     */
    private static boolean isExpenseItem(String refItem) {
        if (refItem == null || refItem.isEmpty()) {
            return false;
        }
        return refItem.startsWith("GA") || refItem.startsWith("GC") || 
               refItem.startsWith("GF") || refItem.startsWith("GO") || 
               refItem.startsWith("GP") || refItem.startsWith("GV") || 
               refItem.startsWith("HA") || refItem.startsWith("OA") || 
               refItem.startsWith("OP") || refItem.startsWith("OV");
    }
    
    /**
     * Clasifica los pagos según su tipo basado en el uso fiscal del CFDI y el código de referencia del ítem.
     * 
     * <p>Esta función implementa la lógica de clasificación de pagos en cuatro categorías:</p>
     * 
     * <h3>1. Activo Fijo (paymentClass = 1)</h3>
     * <p>Aplica a proveedores nacionales y extranjeros, cuando:</p>
     * <ul>
     *   <li>El Uso del CFDI de la factura o de la OC relacionada es "I01" - "I08"
     *       <br/><em>Nota: Los extranjeros no emiten CFDI, pero sus facturas tienen OC relacionada.</em></li>
     *   <li>O bien, la clave del ítem del concepto inicia con: ADI, AET, AMC, AME, AMM, AMU</li>
     * </ul>
     * 
     * <h3>2. Compra (paymentClass = 2)</h3>
     * <p>Aplica a proveedores nacionales y extranjeros, cuando:</p>
     * <ul>
     *   <li>El Uso del CFDI de la factura o de la OC relacionada es "G01" - "G03"
     *       <br/><em>Nota: Los extranjeros no emiten CFDI, pero sus facturas tienen OC relacionada.</em></li>
     *   <li>Y la clave del ítem NO inicia con: GA[09], GC[09], GF[09], GO[09], GP[09], GV[09], HA[09], OA[0-9], OP[0-9], OV[0-9]
     *       <br/><em>Nota: Distíngase entre las expresiones regulares [09] (dos caracteres) y [0-9] (un dígito).</em></li>
     * </ul>
     * 
     * <h3>3. Gasto (paymentClass = 3)</h3>
     * <p>Aplica a proveedores nacionales y extranjeros, cuando:</p>
     * <ul>
     *   <li>El Uso del CFDI de la factura o de la OC relacionada es "G01" - "G03"
     *       <br/><em>Nota: Los extranjeros no emiten CFDI, pero sus facturas tienen OC relacionada.</em></li>
     *   <li>Y la clave del ítem INICIA con: GA[09], GC[09], GF[09], GO[09], GP[09], GV[09], HA[09], OA[0-9], OP[0-9], OV[0-9]
     *       <br/><em>Nota: Distíngase entre las expresiones regulares [09] (dos caracteres) y [0-9] (un dígito).</em></li>
     * </ul>
     * 
     * <h3>4. Anticipo (paymentClass = 4)</h3>
     * <p>Aplica a proveedores nacionales y extranjeros, cuando:</p>
     * <ul>
     *   <li>El pago no tiene factura relacionada</li>
     * </ul>
     * 
     * @param documentFiscalUse  Uso del CFDI de la factura (ej: "G01", "I01", etc.), o {@code null}
     * @param refItem            Código de referencia del ítem del concepto, o {@code null}
     * @param hasDocument        Indica si el pago tiene documento relacionado
     * @return código de clasificación: 0 (sin clasificación/anticipo), 1 (activo fijo), 2 (compra), 3 (gasto), 4 (anticipo)
     */
    public static int mapPaymentClass(String documentFiscalUse, String refItem, boolean hasDocument) {
        int paymentClass;
        if (!hasDocument) {
            return SModSysConsts.FINS_CL_PAY_ADVANCE; // anticipo
        }
        
        // Si el uso fiscal no está disponible, verificar solo el refItem
        if (documentFiscalUse == null || documentFiscalUse.isEmpty()) {
            if (isFixedAssetItem(refItem)) {
                paymentClass = SModSysConsts.FINS_CL_PAY_ASSET; // Activo fijo
            }
            else if (isExpenseItem(refItem)) {
                paymentClass = SModSysConsts.FINS_CL_PAY_EXPENSE; // Gasto
            }
            else {
                paymentClass = 0; // Sin clasificación
            }
            return paymentClass;
        }
        
        // Si el uso fiscal está disponible, evaluar según la tabla de códigos
        switch (documentFiscalUse) {
            case "G01":
            case "G02":
            case "G03":
                // Compra o Gasto depende del refItem
                paymentClass = isExpenseItem(refItem) ? SModSysConsts.FINS_CL_PAY_EXPENSE : SModSysConsts.FINS_CL_PAY_PURCHASE;
                break;

            case "I01":
            case "I02":
            case "I03":
            case "I04":
            case "I05":
            case "I06":
            case "I07":
            case "I08":
                paymentClass = SModSysConsts.FINS_CL_PAY_ASSET; // Activo fijo
                break;

            case "D01":
            case "D02":
            case "D03":
            case "D04":
            case "D05":
            case "D06":
            case "D07":
            case "D08":
            case "D09":
            case "D10":
            case "S01":
            case "CP01":
            case "CN01":
            case "P01":
            default:
                // Para otros códigos, verificar el refItem
                paymentClass = isFixedAssetItem(refItem) ? SModSysConsts.FINS_CL_PAY_ASSET : SModSysConsts.FINS_CL_PAY_ND;
                break;
        }
        
        return paymentClass;
    }
}
