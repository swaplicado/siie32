/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.utils;

import erp.data.SDataConstantsSys;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Carmona
 */
public class SMfinUtils {
    
    /**
     * Obtiene el saldo de un documento agrupado por impuesto
     * 
     * @param connection
     * @param idDoc
     * @param idYear
     * @param recYear
     * @param dt
     * @param cat
     * @param tp
     * 
     * @return ArrayList
     */
    public static ArrayList<SBalanceTax> getBalanceByTax(java.sql.Connection connection, int idDoc, int idYear, int recYear, String dt, int cat, int tp) {
        
        /* Query 1. Moves without document: */
        String sql = ""
                + "SELECT " +
                    "    b.id_bp," +
                    "    b.bp," +
                    "    d.id_year," +
                    "    d.id_doc," +
                    "    d.dt," +
                    "    dt.code AS f_doc_code," +
                    "    CONCAT(d.num_ser," +
                    "            IF(LENGTH(d.num_ser) = 0, '', '-')," +
                    "            d.num) AS f_num," +
                    "    d.tot_r," +
                    "    d.exc_rate," +
                    "    d.tot_cur_r," +
                    "    c.id_cur," +
                    "    c.cur_key," +
                    "    cob.code AS f_cob_code," +
                    "    SUM(re.debit - re.credit) AS f_bal," +
                    "    SUM(IF(d.fid_cur IS NULL" +
                    "            OR d.fid_cur <> re.fid_cur," +
                    "        0," +
                    "        re.debit_cur - re.credit_cur)) AS f_bal_cur," +
                    "    btp.id_tp_bp," +
                    "    btp.tp_bp," +
                    "    re.fid_tax_bas_n," +
                    "    re.fid_tax_n " +
                    "FROM " +
                    "    fin_rec AS r" +
                    "        INNER JOIN" +
                    "    fin_rec_ety AS re ON r.id_year = re.id_year" +
                    "        AND r.id_per = re.id_per" +
                    "        AND r.id_bkc = re.id_bkc" +
                    "        AND r.id_tp_rec = re.id_tp_rec" +
                    "        AND r.id_num = re.id_num" +
                    "        AND r.id_year = " + (recYear > 0 ? recYear : "re.id_year") + " " +
                    (dt.isEmpty() ? "" : "AND r.dt <= '" + dt + "' ") +
                    "        AND NOT r.b_del" +
                    "        AND NOT re.b_del" +
                    "        AND re.fid_ct_sys_mov_xxx = " + cat +
                    "        AND re.fid_tp_sys_mov_xxx = " + tp +
                    "        INNER JOIN" +
                    "    erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp" +
                    "        INNER JOIN" +
                    "    erp.bpsu_bp_ct AS bct ON re.fid_bp_nr = bct.id_bp" +
                    "        AND bct.id_ct_bp = re.fid_tp_sys_mov_xxx" +
                    "        INNER JOIN" +
                    "    erp.bpsu_tp_bp AS btp ON bct.fid_tp_bp = btp.id_tp_bp" +
                    "        AND btp.id_ct_bp = re.fid_tp_sys_mov_xxx" +
                    "        INNER JOIN" +
                    "    trn_dps AS d ON re.fid_dps_year_n = d.id_year" +
                    "        AND re.fid_dps_doc_n = d.id_doc" +
                    "        AND d.b_del = 0" +
                    "        AND d.fid_st_dps = 2" +
                    "        AND re.fid_dps_year_n = " + idYear + " " +
                    "        AND re.fid_dps_doc_n = " + idDoc + " " +
                    "        INNER JOIN" +
                    "    erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps" +
                    "        AND d.fid_cl_dps = dt.id_cl_dps" +
                    "        AND d.fid_tp_dps = dt.id_tp_dps" +
                    "        INNER JOIN" +
                    "    erp.cfgu_cur AS c ON d.fid_cur = c.id_cur" +
                    "        INNER JOIN" +
                    "    erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                    "GROUP BY btp.id_tp_bp , b.id_bp , b.bp , d.id_year , d.id_doc , " +
                        "  d.dt , dt.code , d.num_ser , d.num , d.tot_r , d.exc_rate , " +
                        "  d.tot_cur_r , c.id_cur , c.cur_key , cob.code , re.fid_tax_bas_n , re.fid_tax_n " +
                    "HAVING f_bal <> 0 OR f_bal_cur <> 0 " +
                    "ORDER BY tp_bp , id_tp_bp , bp , id_bp , f_num , dt , id_year , id_doc , id_cur;";
        
        ResultSet resultSet;
        
        try {
            resultSet = connection.createStatement().executeQuery(sql);
            ArrayList<SBalanceTax> taxBalances = new ArrayList();
            SBalanceTax tax;
            
            while(resultSet.next()) {
                tax = new SBalanceTax();
                tax.setTaxBasId(resultSet.getInt("fid_tax_bas_n"));
                tax.setTaxId(resultSet.getInt("fid_tax_n"));
                
                if (tp == SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1]) {
                    tax.setBalance(resultSet.getDouble("f_bal"));
                    tax.setBalanceCurrency(resultSet.getDouble("f_bal_cur"));
                }
                else {
                    tax.setBalance(resultSet.getDouble("f_bal") * -1);
                    tax.setBalanceCurrency(resultSet.getDouble("f_bal_cur") * -1);
                }
                
                taxBalances.add(tax);
            }
            
            return taxBalances;
        }
        catch (SQLException ex) {
            Logger.getLogger(SMfinUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList();
    }
}
