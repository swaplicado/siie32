/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SValidationUtils {
    
    /**
     * Valida que la combinación de tipo de cuenta, cuenta y contable e impuesto no se repita en las configuraciones
     * 
     * @param session
     * @param pk
     * @param finTpAcc tipo de cuenta contable
     * @param account cuenta contable
     * @param taxPk llave primaria del impuesto
     * 
     * @return true, si la combinación no existe y es válida
     * 
     * @throws SQLException 
     */
    public static boolean validateAccTax(final SGuiSession session, final int[] pk, final int finTpAcc, final String account, final int[] taxPk) throws SQLException {
        String sql = "";
        ResultSet resultSet = null;
        
        sql = "SELECT " +
                "    * " +
                "FROM " +
                "    fin_acc_bp_ety " +
                "WHERE " +
                (pk[1] == 0 ? "" : " id_acc_bp <> " + pk[0] + " AND id_tp_acc_bp <> " + pk[1] + " AND id_ety <> " + pk[2] + " AND ") +
                "fid_acc = '" + account.trim() + "' " +
                "AND id_tp_acc_bp = " + finTpAcc + " " +
                "AND fid_tax_bas_n " + (taxPk[0] == 0 ? "is null" : "= " + taxPk[0]) + " " +
                "AND fid_tax_n " + (taxPk[1] == 0 ? "is null" : "= " + taxPk[1]) + ";";
        
        resultSet = session.getStatement().executeQuery(sql);
        
        return ! resultSet.next();
    }
    
    /**
     * Determina la llave foránea del impuesto asignado a la cuenta contable recibida
     * Si el impuesto es null o cero regresa un null
     * 
     * @param session
     * @param acc cuenta contable en formato 'XXXX-XXXX-XXXX'
     * @return [int] o null si no hay un impuesto asignado
     */
    public static int[] getTaxFkByAcc(final SGuiSession session, final String acc) {
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT " +
                "    fid_tax_bas_n, " +
                "    fid_tax_n " +
                "FROM " +
                "    fin_acc_bp_ety " +
                "WHERE fid_acc = '" + acc + "';";
        try {
            
            resultSet = session.getStatement().executeQuery(sql);
            
            if (resultSet.next()) {
                if (resultSet.getInt("fid_tax_bas_n") == 0) {
                    return null;
                }
                
                return new int[] { resultSet.getInt("fid_tax_bas_n"), resultSet.getInt("fid_tax_n") };
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SValidationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
