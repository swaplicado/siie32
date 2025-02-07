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
class SDataCompany {
    
    String nameCompany;
    String rfcCompany;
    String reg_ss;
    
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
    
    public String getReg_ss() {
        return reg_ss;
    }

    public void setReg_ss(String reg_ss) {
        this.reg_ss = reg_ss;
    }
}
