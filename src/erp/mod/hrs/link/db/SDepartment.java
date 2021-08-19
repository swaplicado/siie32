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
public class SDepartment {
    int id_department;
    String dept_code;
    String dept_name;
    boolean is_deleted;
    boolean is_system;
    int head_employee_id;
    int superior_department_id;

    public int getId_department() {
        return id_department;
    }

    public void setId_department(int id_department) {
        this.id_department = id_department;
    }

    public String getDept_code() {
        return dept_code;
    }

    public void setDept_code(String dept_code) {
        this.dept_code = dept_code;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public boolean isIs_system() {
        return is_system;
    }

    public void setIs_system(boolean is_system) {
        this.is_system = is_system;
    }

    public int getHead_employee_id() {
        return head_employee_id;
    }

    public void setHead_employee_id(int head_empployee_id) {
        this.head_employee_id = head_empployee_id;
    }

    public int getSuperior_department_id() {
        return superior_department_id;
    }

    public void setSuperior_department_id(int superior_department_id) {
        this.superior_department_id = superior_department_id;
    }
    
}
