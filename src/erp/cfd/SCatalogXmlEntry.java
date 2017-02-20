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
public class SCatalogXmlEntry {
    
    protected String msCode;
    protected String msName;
    protected Date mtDateStart;

    public SCatalogXmlEntry() {
        msCode = "";
        msName = "";
        mtDateStart = null;
    }
    
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDateStart(Date t) { mtDateStart = t; }
    
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public Date getDateStart() { return mtDateStart; }
}
