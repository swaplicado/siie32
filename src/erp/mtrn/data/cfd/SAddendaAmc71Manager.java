/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data.cfd;

import erp.data.SDataConstantsSys;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Sergio Flores
 */
public class SAddendaAmc71Manager {
    
    private Statement miStatement;
    private int mnCustomerId; // business partner ID
    private ArrayList<SAddendaAmc71Supplier> maSuppliers; // array of most recent addendas
    
    public SAddendaAmc71Manager(final Statement statement, final int customerId) throws Exception {
        miStatement = statement;
        mnCustomerId = customerId;
        maSuppliers = new ArrayList<>();
        
        retriveAddendas();
    }

    private void retriveAddendas() throws Exception {
        maSuppliers.clear();
        
        ArrayList<String> jsonDataStrings = SAddendaUtils.retriveAddendaJsonDataStrings(miStatement, mnCustomerId, SDataConstantsSys.BPSS_TP_CFD_ADD_AMECE71);
        
        for (String json : jsonDataStrings) {
            SAddendaAmc71XmlHeader amc71 = new SAddendaAmc71XmlHeader();
            amc71.decodeJson(json);
            
            SAddendaAmc71Supplier supplier = getSupplierByGln(amc71.Supplier.SupplierGln);
            if (supplier == null) {
                maSuppliers.add(amc71.Supplier);
            }
            else {
                SAddendaAmc71Company company = supplier.getCompanyByGln(amc71.Company.CompanyGln);
                if (company == null) {
                    supplier.Companies.add(amc71.Company);
                }
                else {
                    SAddendaAmc71CompanyBranch companyBranch = company.getCompanyBranchByGln(amc71.CompanyBranch.CompanyBranchGln);
                    if (companyBranch == null) {
                        company.CompanyBranches.add(amc71.CompanyBranch);
                    }
                }
            }
        }
    }
    
    public SAddendaAmc71Supplier getSupplierByGln(final String gln) {
        SAddendaAmc71Supplier supplier = null;
        
        for (SAddendaAmc71Supplier s : maSuppliers) {
            if (s.SupplierGln.equals(gln)) {
                supplier = s;
                break;
            }
        }
        
        return supplier;
    }
    
    public ArrayList<String> getSuppliersGlns() {
        ArrayList<String> glns = new ArrayList<>();
        
        for (SAddendaAmc71Supplier s : maSuppliers) {
            glns.add(s.SupplierGln);
        }
        
        return glns;
    }
}
