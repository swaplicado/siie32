/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

/**
 *
 * @author Edwin Carmona
 */
public class SFirstDayYear {
    
    protected int year;
    protected String dt_date;
    protected boolean is_deleted;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDt_date() {
        return dt_date;
    }

    public void setDt_date(String dt_date) {
        this.dt_date = dt_date;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }
    
    
}
