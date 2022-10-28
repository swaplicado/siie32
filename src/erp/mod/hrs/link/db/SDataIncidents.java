/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

/**
 *
 * @author swaplicado
 */
public class SDataIncidents {
    
    double id_breakdown = 0;
    double day_consumed = 0;
    int year = 0;
    int anniversary = 0;

    public double getId_breakdown() {
        return id_breakdown;
    }

    public void setId_breakdown(double id_breakdown) {
        this.id_breakdown = id_breakdown;
    }

    public double getDay_consumed() {
        return day_consumed;
    }

    public void setDay_consumed(double day_consumed) {
        this.day_consumed = day_consumed;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAnniversary() {
        return anniversary;
    }

    public void setAnniversary(int anniversary) {
        this.anniversary = anniversary;
    }
    
    
            
    
}
