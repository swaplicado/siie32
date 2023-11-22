/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

/**
 *
 * @author Edwin Carmona, Cesar Orozco
 */
public class SAbsence {
    
    protected int id_emp;
    protected int id_abs;
    protected String num;
    protected String dt_date;
    protected String dt_start;
    protected String dt_end;
    protected int eff_days;
    protected int ben_year;
    protected int ben_ann;
    protected String notes;
    protected int fk_class_abs;
    protected int fk_type_abs;
    protected String company;
    protected String json_days;

    public void setCompanie(String company) {
        this.company = company;
    }

    public String getCompanie() {
        return company;
    }
    protected boolean is_closed;
    protected boolean is_deleted;

    public int getId_emp() {
        return id_emp;
    }

    public void setId_emp(int id_emp) {
        this.id_emp = id_emp;
    }

    public int getId_abs() {
        return id_abs;
    }

    public void setId_abs(int id_abs) {
        this.id_abs = id_abs;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDt_date() {
        return dt_date;
    }

    public void setDt_date(String dt_date) {
        this.dt_date = dt_date;
    }

    public String getDt_start() {
        return dt_start;
    }

    public void setDt_start(String dt_start) {
        this.dt_start = dt_start;
    }

    public String getDt_end() {
        return dt_end;
    }

    public void setDt_end(String dt_end) {
        this.dt_end = dt_end;
    }

    public int getEff_days() {
        return eff_days;
    }

    public void setEff_days(int eff_days) {
        this.eff_days = eff_days;
    }

    public int getBen_year() {
        return ben_year;
    }

    public void setBen_year(int ben_year) {
        this.ben_year = ben_year;
    }

    public int getBen_ann() {
        return ben_ann;
    }

    public void setBen_ann(int ben_ann) {
        this.ben_ann = ben_ann;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getFk_class_abs() {
        return fk_class_abs;
    }

    public void setFk_class_abs(int fk_cl_abs) {
        this.fk_class_abs = fk_cl_abs;
    }

    public int getFk_type_abs() {
        return fk_type_abs;
    }

    public void setFk_type_abs(int fk_tp_abs) {
        this.fk_type_abs = fk_tp_abs;
    }

    public boolean isIs_closed() {
        return is_closed;
    }

    public void setIs_closed(boolean is_closed) {
        this.is_closed = is_closed;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getJson_days() {
        return json_days;
    }

    public void setJson_days(String json_days) {
        this.json_days = json_days;
    }
}
