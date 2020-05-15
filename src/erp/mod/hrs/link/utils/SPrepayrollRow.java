/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.utils;

import java.util.List;

/**
 *
 * @author Edwin Carmona
 */
public class SPrepayrollRow {
    
    private int employee_id;
    private double double_overtime;
    private double triple_overtime;
    private List<SDay> days;

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public double getDouble_overtime() {
        return double_overtime;
    }

    public void setDouble_overtime(double double_overtime) {
        this.double_overtime = double_overtime;
    }

    public double getTriple_overtime() {
        return triple_overtime;
    }

    public void setTriple_overtime(double triple_overtime) {
        this.triple_overtime = triple_overtime;
    }

    public List<SDay> getDays() {
        return days;
    }

    public void setDays(List<SDay> days) {
        this.days = days;
    }
    
    public int getAbsences() {
        int absences = 0;
        for (SDay day : days) {
            if (day.isIs_absence()) {
                absences++;
            }
        }
        
        return absences;
    }
    
    public int getSundays() {
        int sundays = 0;
        for (SDay day : days) {
            if (day.isIs_sunday()) {
                sundays++;
            }
        }
        
        return sundays;
    }
    
    public int getDaysOff() {
        int daysOff = 0;
        for (SDay day : days) {
            daysOff += day.getN_days_off();
        }
        
        return daysOff;
    }
}
