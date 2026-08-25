/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.utils;

/**
 *
 * @author Edwin Carmona
 */
public class SStockValuationDpsUtils {
    
    public static String getOrderFromInvoice(final int idYear, final int idDoc, final int idEty) {
        String sql = "SELECT " +
                    "	o.*, " +
                    "	oe.* " +
                    "FROM " +
                    "	trn_dps o " +
                    "INNER JOIN trn_dps_ety oe ON " +
                    "	o.id_year = oe.id_year " +
                    "	AND o.id_doc = oe.id_doc " +
                    "INNER JOIN trn_dps_dps_supply li ON " +
                    "	oe.id_year = li.id_src_year " +
                    "	AND oe.id_doc = li.id_src_doc " +
                    "	AND oe.id_ety = li.id_src_ety " +
                    "INNER JOIN trn_dps_ety fe ON " +
                    "	li.id_des_year = fe.id_year " +
                    "	AND li.id_des_doc = fe.id_doc " +
                    "	AND li.id_des_ety = fe.id_ety " +
                    "INNER JOIN trn_dps f ON " +
                    "	fe.id_year = f.id_year " +
                    "	AND fe.id_doc = f.id_doc " +
                    "WHERE " +
                    "	f.fid_ct_dps = 1 " +
                    "	AND f.fid_cl_dps = 3 " +
                    "	AND f.fid_tp_dps = 1 " +
                    "   AND fe.id_year = " + idYear + " " +
                    "   AND fe.id_doc = " + idDoc + " " +
                    "	AND fe.id_ety = " + idEty + " " +
                    "   AND oe.b_del = 0 " +
                    "	AND o.b_del = 0 " +
                    "LIMIT 1;";
        
        return sql;
    }
}
