/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.client.SClientInterface;
import java.sql.ResultSet;

/**
 *
 * @author Uriel Castañeda
 */
public abstract class STrnDpsUtilities {
 
    /**
     * Obtein total amount supplied in DIOG's.
     * @param client GUI client.
     * @param entryKey Entry key.
     * @return totalSupplied Total amount supplied in DIOG's.
     * @throws Exception
     */
    public static double obtainEtyTotalSupplied(final SClientInterface client, final int[] entryKey) throws Exception {
        String sql = "";
        ResultSet resulSet = null;
        double totalSupplied = 0;

        sql = "SELECT SUM(orig_qty) as tot_qty_sup FROM trn_diog_ety AS de WHERE fid_dps_year_n = " + entryKey[0] + " AND fid_dps_doc_n = " + entryKey[1] + " AND fid_dps_ety_n = " + entryKey[2] + " ";

        resulSet = client.getSession().getStatement().executeQuery(sql);
        if (resulSet.next()) {
            totalSupplied = resulSet.getDouble("tot_qty_sup");
        }

        return totalSupplied;
    }
}
