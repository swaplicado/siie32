/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

import erp.mod.hrs.link.db.SDataRow;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Edwin Carmona
 */
public class SPayrollBonusUtils {
    
    public static final int BONUS = 101;
    public static final int PANTRY = 102;
    public static final int SUPER_BONUS = 103;
    
    /**
     * Esta función determina si el empleado ganó o no el bono en base a si
     * no superó el tiempo permitido de retardos y no tiene faltas.
     * 
     * @param startDate
     * @param endDate
     * @param oData
     * @return 
     */
    public static boolean hasBonus(Date startDate, Date endDate, SDataRow oData) {
        if (oData.getDelayMins() > 15) {
            return false;
        }
        if (oData.getAbsences() > 0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Determina si el empleado es acreedor a super bono consultando desde una fecha base 
     * dos meses hacia atrás contando los bonos que se haya ganado.
     * 
     * @param client
     * @param idEmployee
     * @param referenceDate
     * @return 
     */
    public static double hasSuperBonus(SGuiClient client, int idEmployee, Date referenceDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(referenceDate);
        calendar.add(Calendar.MONTH, -2);
        
        try {
            String sql = "SELECT  " +
                    "    COUNT(*) AS n_bonus " +
                    "FROM " +
                    "    hrs_pay hp " +
                    "        INNER JOIN " +
                    "    hrs_pay_rcp_ear hpre ON hp.id_pay = hpre.id_pay " +
                    "WHERE " +
                    "    hpre.id_emp = " + idEmployee +
                    "    AND hp.dt_sta BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(calendar.getTime()) + "' " +
                    "    AND '" + SLibUtils.DbmsDateFormatDate.format(referenceDate) + "' " +
                    "    AND hpre.fk_bonus = " + BONUS +
                    "    AND hp.b_clo" +
                    "    AND NOT hp.b_del" +
                    "    AND NOT hpre.b_del;";
            
            ResultSet resultSet = null;
            int nBonus = 0;
            resultSet = client.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                nBonus =  resultSet.getInt("n_bonus");
            }
            
            return nBonus >= 4 ? 1d : 0d;
        }
        catch (SQLException ex) {
            Logger.getLogger(SPayrollBonusUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0d;
    }
    
    /**
     Determina si el empleado es acreedor a despensa consultando desde una fecha base 
     * un mes hacia atrás contando los bonos que se haya ganado.
     * 
     * @param client
     * @param idEmployee
     * @param referenceDate
     * @return 
     */
    public static double hasPantry(SGuiClient client, int idEmployee, Date referenceDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(referenceDate);
        calendar.add(Calendar.MONTH, -1);
        
        try {
            String sql = "SELECT  " +
                    "    COUNT(*) AS n_bonus " +
                    "FROM " +
                    "    hrs_pay hp " +
                    "        INNER JOIN " +
                    "    hrs_pay_rcp_ear hpre ON hp.id_pay = hpre.id_pay " +
                    "WHERE " +
                    "    hpre.id_emp = " + idEmployee +
                    "    AND hp.dt_sta BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(calendar.getTime()) + "' " +
                    "    AND '" + SLibUtils.DbmsDateFormatDate.format(referenceDate) + "' " +
                    "    AND hpre.fk_bonus = " + BONUS + "" +
                    "    AND hp.b_clo" +
                    "    AND NOT hp.b_del" +
                    "    AND NOT hpre.b_del;";
            
            ResultSet resultSet = null;
            int nBonus = 0;
            resultSet = client.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                nBonus =  resultSet.getInt("n_bonus");
            }
            
            if (nBonus >= 2) {
                return 1d;
            }
            else if (nBonus == 1) {
                return 0.5d;
            }
            
            return 0d;
        }
        catch (SQLException ex) {
            Logger.getLogger(SPayrollBonusUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0d;
    }
}
