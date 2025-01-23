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
public class SCompany {
    
    protected int id_company;
    protected String company;
    protected String database_nm;

    public int getId_company() {
        return id_company;
    }

    public void setId_company(int id_company) {
        this.id_company = id_company;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDatabase_nm() {
        return database_nm;
    }

    public void setDatabase_nm(String database_nm) {
        this.database_nm = database_nm;
    }
    
    
}
