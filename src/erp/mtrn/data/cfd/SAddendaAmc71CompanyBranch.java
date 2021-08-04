/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data.cfd;

/**
 *
 * @author Sergio Flores
 */
public class SAddendaAmc71CompanyBranch {

    public String CompanyBranchGln;
    public String ShipToName;
    public String ShipToAddress;
    public String ShipToCity;
    public String ShipToPostalCode;
    
    public SAddendaAmc71CompanyBranch() {
        reset();
    }
    
    public final void reset() {
        CompanyBranchGln = "";
        ShipToName = "";
        ShipToAddress = "";
        ShipToCity = "";
        ShipToPostalCode = "";
    }
}
