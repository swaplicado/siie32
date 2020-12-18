/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SValidationUtils {
    
    public static boolean validateAccTax(final SGuiSession session, final int[] pk, final String account, final int[] taxPk) throws SQLException {
        String sql = "";
        ResultSet resultSet = null;
        
        sql = "SELECT " +
                "    * " +
                "FROM " +
                "    fin_acc_bp_ety " +
                "WHERE " +
                (pk[1] == 0 ? "" : " id_acc_bp <> " + pk[0] + " AND id_tp_acc_bp <> " + pk[1] + " AND id_ety <> " + pk[2] + " AND ") +
                " fid_acc = '" + account.trim() + "' " +
                " AND fid_tax_bas_n " + (taxPk[0] == 0 ? "is null" : "= " + taxPk[0]) + " " +
                " AND fid_tax_n " + (taxPk[1] == 0 ? "is null" : "= " + taxPk[1]) + ";";
        
        resultSet = session.getStatement().executeQuery(sql);
        
        return ! resultSet.next();
    }
}
