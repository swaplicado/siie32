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
public class SEmployee {
    int id_employee;
    int num_employee;
    String lastname1;
    String lastname2;
    String lastname;
    String firstname;
    String admission_date;
    String leave_date;
    String email;
    int company_id;
    int overtime_policy;
    int checker_policy;
    int way_pay;
    int dept_rh_id;
    int siie_job_id;
    boolean is_active;
    boolean is_deleted;

    public int getId_employee() {
        return id_employee;
    }

    public void setId_employee(int id_employee) {
        this.id_employee = id_employee;
    }

    public int getNum_employee() {
        return num_employee;
    }

    public void setNum_employee(int num_employee) {
        this.num_employee = num_employee;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname1() {
        return lastname1;
    }

    public void setLastname1(String lastname1) {
        this.lastname1 = lastname1;
    }

    public String getLastname2() {
        return lastname2;
    }

    public void setLastname2(String lastname2) {
        this.lastname2 = lastname2;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getAdmission_date() {
        return admission_date;
    }

    public void setAdmission_date(String admission_date) {
        this.admission_date = admission_date;
    }

    public String getLeave_date() {
        return leave_date;
    }

    public void setLeave_date(String leave_date) {
        this.leave_date = leave_date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getOvertime_policy() {
        return overtime_policy;
    }

    public void setOvertime_policy(int overtime_policy) {
        this.overtime_policy = overtime_policy;
    }

    public int getChecker_policy() {
        return checker_policy;
    }

    public void setChecker_policy(int checker_policy) {
        this.checker_policy = checker_policy;
    }

    public int getWay_pay() {
        return way_pay;
    }

    public void setWay_pay(int way_pay) {
        this.way_pay = way_pay;
    }

    public int getDept_rh_id() {
        return dept_rh_id;
    }

    public void setDept_rh_id(int dept_rh_id) {
        this.dept_rh_id = dept_rh_id;
    }

    public int getSiie_job_id() {
        return siie_job_id;
    }

    public void setSiie_job_id(int siie_job_id) {
        this.siie_job_id = siie_job_id;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }
    
    
}
