/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

import erp.mod.hrs.db.SDbAbsence;
import erp.mod.hrs.db.SDbAbsenceConsumption;
import erp.mod.hrs.link.utils.SDay;
import erp.mod.hrs.link.utils.SPrepayrollRow;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SPrepayrollUtils {
    
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
                        " NOT b_del AND (num = " + payrollNumber + " OR num = " + numAux +") " +
                        " AND fk_tp_pay = " + payType +
                        "  AND (year = " + year + " OR year = " + yearAux + "); ";
        
        Date [] dates = new Date[2];
        ResultSet resulReceipts;
        
        try {
            resulReceipts = client.getSession().getStatement().
                    getConnection().createStatement().
                    executeQuery(sql);
            
            int pos = 0;
            while (resulReceipts.next()) {
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
        /**
         * se realiza el recorrido día a día hacia atrás hasta que se 
         * encuentre el día que coincida con el recibido
         */
        while (weekday != day) {
            calDate.add(Calendar.DATE, -1);
            weekday = calDate.get(Calendar.DAY_OF_WEEK);
        }
        
        Date tStart = (Date) calDate.getTime().clone();
        calDate.add(Calendar.DATE, 6); // se multiplican los días por las semanas de retraso
        Date tEnd = calDate.getTime();
        
        return new Date[] {tStart, tEnd};
    }
    
    public static ArrayList<SDbAbsence> getAbsencesFromNoWorkedDays(SGuiClient client, SPrepayrollRow row, Date captureDt, int payrollId) {
        ArrayList<SDbAbsence> absences = new ArrayList();

        for (SDay day : row.getDays()) {
            if (day.isIs_absence()) {
                SDbAbsence abs = SPrepayrollUtils.createAbsence(client.getSession(), row.getEmployee_id(), captureDt, day.getDt_date(), payrollId);
                absences.add(abs);
            }
        }
        
        return absences;
    }
    
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
    
    public static ArrayList<SDbAbsenceConsumption> getConsumptionsFromAbs(ArrayList<SDbAbsence> abss) {
        ArrayList<SDbAbsenceConsumption> consms = new ArrayList();
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
            
            cons.setAuxNumber(abs.getNumber());
            cons.setAuxDateStart(abs.getDateStart());
            cons.setAuxDateEnd(abs.getDateEnd());
            
            consms.add(cons);
        }
        
        return consms;
    }
    
    public static void deleteAbsenceByImportation(SGuiClient client, final int payrollId) {
        try {
            String sql = "DELETE " +
                    "FROM " +
                    "    hrs_abs " +
                    "WHERE " +
                    "    fk_src_pay_n = " + payrollId + " AND b_time_clock = TRUE;";
            
            client.getSession().getStatement().
                    getConnection().createStatement().
                    executeUpdate(sql);

        } catch (SQLException ex) {
            Logger.getLogger(SPrepayrollUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
