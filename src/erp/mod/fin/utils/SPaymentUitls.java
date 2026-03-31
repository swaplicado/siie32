package erp.mod.fin.utils;

import erp.mod.fin.db.SRowPayments;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Utilidades para la consulta y mapeo de pagos del módulo financiero.
 *
 * @author Edwin Carmona
 */
public class SPaymentUitls {

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
}
