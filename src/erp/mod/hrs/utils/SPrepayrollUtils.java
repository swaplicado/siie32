/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbAbsence;
import erp.mod.hrs.db.SDbAbsenceConsumption;
import erp.mod.hrs.db.SHrsPayrollUtils;
import erp.mod.hrs.link.utils.SDay;
import erp.mod.hrs.link.utils.SPrepayrollRow;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SPrepayrollUtils {
    
    /**
     * Obtiene las fechas de corte de semana o quincena basado en la tabla de base 
     * de datos.
     * 
     * @param client
     * @param payType
     * @param year
     * @param payrollNumber
     * @return 
     */
    public static Date[] getPrepayrollDateRangeByTable(SGuiClient client, final int payType, final int year, final int payrollNumber) {
        int numAux = 0;
        int yearAux = 0;
        
        if (payrollNumber > 1) {
            numAux = payrollNumber - 1;
            yearAux = year;
        }
        else {
            yearAux = year - 1;
            String ly = "SELECT " +
                        "    MAX(num) AS max_num " +
                        " FROM " +
                        "    hrs_pre_pay_cut_cal " +
                        " WHERE NOT b_del AND " +
                        "    fk_tp_pay = " + payType + " AND year = " + yearAux + ";";
            
            ResultSet resulReceipts;
        
            try {
                resulReceipts = client.getSession().getStatement().
                        getConnection().createStatement().
                        executeQuery(ly);
                
                if (resulReceipts.next()) {
                    numAux = resulReceipts.getInt("max_num");
                }
            }
            catch (SQLException ex) {
                Logger.getLogger(SPrepayrollUtils.class.getName()).log(Level.SEVERE, null, ex);
                
                return null;
            }
        }
        
        String sql = "SELECT " +
                        "    dt_cut " +
                        " FROM " +
                        "    hrs_pre_pay_cut_cal " +
                        " WHERE " +
                        " NOT b_del" +
                        " AND fk_tp_pay = " + payType +
                        " AND ((num = " + payrollNumber + " AND year = " + year + ") " +
                            "OR (num = " + numAux +" AND year = " + yearAux + "))" +
                        " ORDER BY dt_cut ASC";
        
        Date [] dates = new Date[2];
        ResultSet resulReceipts;
        
        try {
            resulReceipts = client.getSession().getStatement().
                    getConnection().createStatement().
                    executeQuery(sql);
            
            int pos = 0;
            while (resulReceipts.next() && pos < dates.length) {
                dates[pos] = resulReceipts.getDate("dt_cut");
                pos++;
            }
            
            if (pos == 0) {
                return null;
            }
            
            Calendar c = Calendar.getInstance();
            c.setTime(dates[0]); // Now use today date.
            c.add(Calendar.DATE, 1); // Adding 1 days
            
            dates[0] = c.getTime();
        }
        catch (SQLException ex) {
            Logger.getLogger(SPrepayrollUtils.class.getName()).log(Level.SEVERE, null, ex);
            
            return null;
        }
        
        return dates;
    }
    
    /**
     * Obtiene el rango de fechas correspondiente dado un día de corte en la semana
     * 
     * 1 : SUNDAY
     * 7 : SATURDAY
     * 
     * @param day 
     * @param sinceDate fecha de la que se parte y se hace el recorrido hacia atrás en el tiempo
     * @param weekLag número de semanas de retraso
     * 
     * @return un arreglo con fecha inicial y fecha final
     */
    public static Date[] getPrepayrollDateRangeByCutDay(int day, Date sinceDate, int weekLag) {
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(sinceDate);
        int weekday = calDate.get(Calendar.DAY_OF_WEEK);
        
        if (weekday == day) {
            calDate.add(Calendar.DATE, -1);
            weekday = calDate.get(Calendar.DAY_OF_WEEK);
        }
        
        /**
         * se realiza el recorrido día a día hacia atrás hasta que se 
         * encuentre el día que coincida con el recibido
         */
        while (weekday != day) {
            calDate.add(Calendar.DATE, -1);
            weekday = calDate.get(Calendar.DAY_OF_WEEK);
        }
        
        Date tEnd = (Date) calDate.getTime().clone();
        calDate.add(Calendar.DATE, (-6 * weekLag));
        Date tStart = calDate.getTime();
        
        return new Date[] {tStart, tEnd};
    }
    
    /**
     * Crea las ausencias por inasistencia sin permiso
     * 
     * @param client
     * @param row
     * @param captureDt
     * @param payrollId
     * @return 
     */
    public static ArrayList<SDbAbsence> getAbsencesFromNoWorkedDays(SGuiClient client, SPrepayrollRow row, Date captureDt, int payrollId) {
        ArrayList<SDbAbsence> absences = new ArrayList<>();

        for (SDay day : row.getDays()) {
            for (int i = 0; i < day.getNum_absences(); i++) {
                SDbAbsence abs = SPrepayrollUtils.createAbsence(client.getSession(), row.getEmployee_id(), captureDt, day.getDt_date(), payrollId);
                absences.add(abs);
            }
        }
        
        return absences;
    }
    
    /**
     * Crea las ausencias correspondientes a las faltas que reportó el checador
     * 
     * @param session
     * @param emp
     * @param captureDate
     * @param date
     * @param payrollId
     * @return 
     */
    private static SDbAbsence createAbsence(SGuiSession session, int emp, Date captureDate, String date, int payrollId) {
        try {
            SDbAbsence abs = new SDbAbsence();
            Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            
            abs.setPkEmployeeId(emp);
            abs.setPkAbsenceId(0);
            abs.setNumber("");
            abs.setDate(captureDate);
            abs.setDateStart(dt);
            abs.setDateEnd(dt);
            abs.setEffectiveDays(1);
            abs.setBenefitsYear(0);
            abs.setBenefitsAnniversary(0);
            abs.setNotes("FALTA REPORTADA POR CHECADOR");
            abs.setTimeClockSourced(true);
            abs.setClosed(false);
            abs.setDeleted(false);
            abs.setFkAbsenceClassId(1);
            abs.setFkAbsenceTypeId(1);
            abs.setFkSourcePayrollId_n(payrollId);
            abs.setFkUserClosedId(1);
            abs.setFkUserInsertId(1);
            abs.setFkUserUpdateId(1);
            
            abs.save(session);
            
            return abs;
        }
        catch (ParseException ex) {
            Logger.getLogger(SPrepayrollUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SPrepayrollUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Crea los consumos que corresponen a las ausencias creadas por las faltas que determinó el checador
     * 
     * @param abss
     * @return 
     */
    public static ArrayList<SDbAbsenceConsumption> getConsumptionsFromAbs(ArrayList<SDbAbsence> abss) {
        ArrayList<SDbAbsenceConsumption> consms = new ArrayList<>();
        for (SDbAbsence abs : abss) {
            SDbAbsenceConsumption cons = new SDbAbsenceConsumption();
            
            cons.setPkEmployeeId(abs.getPkEmployeeId());
            cons.setPkAbsenceId(abs.getPkAbsenceId());
            cons.setPkConsumptionId(0);
            cons.setDateStart(abs.getDateStart());
            cons.setDateEnd(abs.getDateEnd());
            cons.setEffectiveDays(abs.getEffectiveDays());
            cons.setDeleted(false);
            cons.setFkReceiptPayrollId(abs.getFkSourcePayrollId_n());
            cons.setFkReceiptEmployeeId(abs.getPkEmployeeId());
            cons.setFkUserInsertId(1);
            cons.setFkUserUpdateId(1);
            
            cons.setAuxIsClockSourced(true);
            cons.setParentAbsence(abs);
            
            consms.add(cons);
        }
        
        return consms;
    }
    
    /**
     * Elimina los consumos y las ausencias creadas por reporte del reloj checador
     * 
     * @param client
     * @param payrollId 
     */
    public static void deleteAbsencesAndConsumptionsByImportation(SGuiClient client, final int payrollId) {
        try {
            String sqlIds = "SELECT id_abs " +
                    "FROM " +
                    "    hrs_abs " +
                    "WHERE " +
                    "    fk_src_pay_n = " + payrollId + " AND b_time_clock = TRUE;";
            
            ResultSet resultIds = client.getSession().getStatement().
                    getConnection().createStatement().
                    executeQuery(sqlIds);
            
            ArrayList<String> ids = new ArrayList<>();
            while (resultIds.next()) {
                ids.add(resultIds.getString("id_abs"));
            }
            
            if (ids.isEmpty()) {
                return;
            }
            
            StringBuilder stringIds = new StringBuilder("");
        
            //iterate through ArrayList
            for(String id : ids){
                //append ArrayList element followed by comma
                stringIds.append(id).append(",");
            }

            //convert StringBuffer to String
            String strList = stringIds.toString();

            //remove last comma from String if you want
            if( strList.length() > 0 )
                strList = strList.substring(0, strList.length() - 1);
            
            String sql = "DELETE FROM hrs_abs_cns " +
                            "WHERE " +
                            "id_abs IN (" + strList + ");";
            
            client.getSession().getStatement().
                    getConnection().createStatement().
                    executeUpdate(sql);
            
            String sql1 = "DELETE " +
                    "FROM " +
                    "    hrs_abs " +
                    "WHERE " +
                    "id_abs IN (" + strList + ");";
            
            client.getSession().getStatement().
                    getConnection().createStatement().
                    executeUpdate(sql1);

        }
        catch (SQLException ex) {
            Logger.getLogger(SPrepayrollUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Determina si el bono en cuestión requiere pagos previos para otorgarse
     * 
     * @param session
     * @param idBonus
     * 
     * @return 
     */
    public static boolean isWithPreviousPayment(SGuiSession session, final int idBonus) {
        String query = "SELECT b_pre_pay FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_BONUS) + " WHERE id_bonus = " + idBonus + ";";
        
        try {
            ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getBoolean("b_pre_pay");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SHrsPayrollUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
}
