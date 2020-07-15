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
public class SDay {
    
    private String dt_date;
    private int holiday_id;
    private int num_absences;
    private boolean is_sunday;
    private int n_days_off;
    private List<SAbsenceKey> events;

    public String getDt_date() {
        return dt_date;
    }

    public void setDt_date(String dt_date) {
        this.dt_date = dt_date;
    }

    public int getHoliday_id() {
        return holiday_id;
    }

    public void setHoliday_id(int holiday_id) {
        this.holiday_id = holiday_id;
    }

    public int getNum_absences() {
        return num_absences;
    }

    public void setNum_absences(int num_absences) {
        this.num_absences = num_absences;
    }

    public boolean isIs_sunday() {
        return is_sunday;
    }

    public void setIs_sunday(boolean is_sunday) {
        this.is_sunday = is_sunday;
    }

    public int getN_days_off() {
        return n_days_off;
    }

    public void setN_days_off(int n_days_off) {
        this.n_days_off = n_days_off;
    }

    public List<SAbsenceKey> getEvents() {
        return events;
    }

    public void setEvents(List<SAbsenceKey> events) {
        this.events = events;
    }
    
}
