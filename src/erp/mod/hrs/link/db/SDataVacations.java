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
public class SDataVacations {
    
    double vacation_gained = 0;
    double vacation_consumed = 0;
    double vacation_programm = 0;
    int anniversary = 0;
    int year = 0;
    
    public double getVacation_consumed() {
        return vacation_consumed;
    }

    public void setVacation_consumed(double vacation_consumed) {
        this.vacation_consumed = vacation_consumed;
    }
    
    public double getVacation_gained() {
        return vacation_gained;
    }

    public void setVacation_gained(double vacation_gained) {
        this.vacation_gained = vacation_gained;
    }

    public double getVacation_programm() {
        return vacation_programm;
    }

    public void setVacation_programm(double vacation_programm) {
        this.vacation_programm = vacation_programm;
    }

    public int getAnniversary() {
        return anniversary;
    }

    public void setAnniversary(int anniversary) {
        this.anniversary = anniversary;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    
}
