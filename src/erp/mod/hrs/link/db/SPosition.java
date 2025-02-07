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
public class SPosition {
    
    int id_position;
    String code;
    String name;
    boolean is_deleted;
    boolean is_system;
    int fk_department;

    public int getId_position() {
        return id_position;
    }

    public void setId_position(int id_position) {
        this.id_position = id_position;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getFk_department() {
        return fk_department;
    }

    public void setFk_department(int fk_department) {
        this.fk_department = fk_department;
    }
    
    
}
