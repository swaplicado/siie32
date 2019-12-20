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
public class SAddendaAmc71Supplier {
    
    public String SupplierGln;
    public String SupplierNumber;
    public ArrayList<SAddendaAmc71Company> Companies;
    
    public SAddendaAmc71Supplier() {
        reset();
    }
    
    public final void reset() {
        SupplierGln = "";
        SupplierNumber = "";
        Companies = new ArrayList<>();
    }
    
    public SAddendaAmc71Company getCompanyByGln(final String gln) {
        SAddendaAmc71Company company = null;
        
        for (SAddendaAmc71Company c : Companies) {
            if (c.CompanyGln.equals(gln)) {
                company = c;
                break;
            }
        }
        
        return company;
    }
    
    public ArrayList<String> getCompaniesGlns() {
        ArrayList<String> glns = new ArrayList<>();
        
        for (SAddendaAmc71Company c : Companies) {
            glns.add(c.CompanyGln);
        }
        
        return glns;
    }
}
