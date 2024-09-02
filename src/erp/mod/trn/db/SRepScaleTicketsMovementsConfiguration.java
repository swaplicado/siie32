/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import java.util.ArrayList;

/**
 *
 * @author Isabel Servín
 */
public class SRepScaleTicketsMovementsConfiguration {
    
    String docNumber;
    String repTitle;
    ArrayList<Integer> repCompanies;
    int repGrouping;
    
    public String getDocNumber() {
        return docNumber;
    }
    
    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }
    
    public String getRepTitle() {
        return repTitle;
    }
    
    public void setRepTitle(String repTitle) {
        this.repTitle = repTitle;
    }
    
    public ArrayList<Integer> getRepCompanies() {
        return repCompanies;
    }
    
    public void setRepCompanies(ArrayList<Integer> repCompanies) {
        this.repCompanies = repCompanies;
    }
    
    public int getRepGrouping() {
        return repGrouping;
    }
    
    public void setRepGrouping(int repGrouping) {
        this.repGrouping = repGrouping;
    }
}
