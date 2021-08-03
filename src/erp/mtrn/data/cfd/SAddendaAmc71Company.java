/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data.cfd;

import java.util.ArrayList;

/**
 *
 * @author Sergio Flores
 */
public class SAddendaAmc71Company {
    
    public String CompanyGln;
    public String CompanyContact;
    public ArrayList<SAddendaAmc71CompanyBranch> CompanyBranches;
    
    public SAddendaAmc71Company() {
        reset();
    }
    
    public final void reset() {
        CompanyGln = "";
        CompanyContact = "";
        CompanyBranches = new ArrayList<>();
    }
    
    public SAddendaAmc71CompanyBranch getCompanyBranchByGln(final String gln) {
        SAddendaAmc71CompanyBranch companyBranch = null;
        
        for (SAddendaAmc71CompanyBranch cb : CompanyBranches) {
            if (cb.CompanyBranchGln.equals(gln)) {
                companyBranch = cb;
                break;
            }
        }
        
        return companyBranch;
    }
    
    public ArrayList<String> getCompanyBranchesGlns() {
        ArrayList<String> glns = new ArrayList<>();
        
        for (SAddendaAmc71CompanyBranch cb : CompanyBranches) {
            glns.add(cb.CompanyBranchGln);
        }
        
        return glns;
    }
}
