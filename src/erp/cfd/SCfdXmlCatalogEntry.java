/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cfd;

import java.util.Date;

/**
 * Structure for the handling of the entries of the SAT catalogs.
 * @author Juan Barajas
 */
public class SCfdXmlCatalogEntry {
    
    protected String msCode;
    protected String msName;
    protected Date mtValidityStart;
    protected Date mtValidityEnd;
    protected boolean mbTaxpayerPer;    // person
    protected boolean mbTaxpayerOrg;    // organization

    public SCfdXmlCatalogEntry(String code, String name, Date validityStart, Date validityEnd) {
        this(code, name, validityStart, validityEnd, false, false);
    }
    
    public SCfdXmlCatalogEntry(String code, String name, Date validityStart, Date validityEnd, boolean taxpayerPer, boolean taxpayerOrg) {
        msCode = code;
        msName = name;
        mtValidityStart = validityStart;
        mtValidityEnd = validityEnd;
        mbTaxpayerPer = taxpayerPer;
        mbTaxpayerOrg = taxpayerOrg;
    }
    
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public Date getValidityStart() { return mtValidityStart; }
    public Date getValidityEnd() { return mtValidityEnd; }
    public boolean isTaxpayerPer() { return mbTaxpayerPer; }
    public boolean isTaxpayerOrg() { return mbTaxpayerOrg; }
}
