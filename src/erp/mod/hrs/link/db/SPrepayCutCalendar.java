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
public class SPrepayCutCalendar {
    protected int id_cal;
    protected int year;
    protected int num;
    protected String dt_cut;
    protected boolean is_deleted;
    protected int fk_tp_pay;

    public int getId_cal() {
        return id_cal;
    }

    public void setId_cal(int id_cal) {
        this.id_cal = id_cal;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDt_cut() {
        return dt_cut;
    }

    public void setDt_cut(String dt_cut) {
        this.dt_cut = dt_cut;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public int getFk_tp_pay() {
        return fk_tp_pay;
    }

    public void setFk_tp_pay(int fk_tp_pay) {
        this.fk_tp_pay = fk_tp_pay;
    }
}
