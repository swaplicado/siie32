/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

/**
 *
 * @author Claudio Peña
 */
public class SDataEmployee {
    int id_employee;
    String name;
    String rfc;
    String nss;
    String position;
    String benefit_date;
    String salary;
    String nameGh;
    String nameCompany;
    String rfcCompany;
    String rgg_fiscal;
    int company_id;

    

    public int getId_employee() {
        return id_employee;
    }

    public void setId_employee(int id_employee) {
        this.id_employee = id_employee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }
    
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getBenefitData() {
        return position;
    }

    public void setBenefitData(String position) {
        this.position = position;
    }
    
    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
    
    public String getNameGh() {
        return nameGh;
    }

    public void setNameGh(String nameGh) {
        this.nameGh = nameGh;
    }
    
    public String getNameCompany() {
        return nameCompany;
    }

    public void setNameCompany(String nameCompany) {
        this.nameCompany = nameCompany;
    }
    
    
    public String getRfcCompany() {
        return rfcCompany;
    }

    public void setRfcCompany(String rfcCompany) {
        this.rfcCompany = rfcCompany;
    }
    
    public String getRgg_fiscal() {
        return rgg_fiscal;
    }

    public void setRgg_fiscal(String rgg_fiscal) {
        this.rgg_fiscal = rgg_fiscal;
    }
    
    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }
}
