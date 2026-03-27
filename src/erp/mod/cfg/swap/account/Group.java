/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.account;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Sergio Flores
 */
public class Group {
    
    public static final String DOC_TYPE_INVOICE = "invoice";
    public static final String DOC_TYPE_BOL = "BOL";

    public static final HashMap<String, String> DocTypes = new HashMap<>();
    public static final HashMap<String, String> DocTypeCodes = new HashMap<>();
    
    static {
        DocTypes.put(DOC_TYPE_INVOICE, "Factura");
        DocTypes.put(DOC_TYPE_BOL, "Carta Porte");
        
        DocTypeCodes.put(DOC_TYPE_INVOICE, "F");
        DocTypeCodes.put(DOC_TYPE_BOL, "CP");
    }
    
    private String groupName;
    private String groupCode;
    private String docType;
    private List<Partner> partners;

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getGroupCode() { return groupCode; }
    public void setGroupCode(String groupCode) { this.groupCode = groupCode; }

    public String getDocType() { return docType; }
    public void setDocType(String docType) { this.docType = docType; }

    public List<Partner> getPartners() { return partners; }
    public void setPartners(List<Partner> partners) { this.partners = partners; }
    
    public Partner getPartner(final boolean isPerson) {
        Partner partner = null;
        
        for (Partner p : partners) {
            if (isPerson && p.getPartnerType().equals(Partner.PARTNER_TYPE_PERSON)) {
                partner = p;
                break;
            }
            else if (!isPerson && p.getPartnerType().equals(Partner.PARTNER_TYPE_ORGANIZATION)) {
                partner = p;
                break;
            }
            else if (p.getPartnerType().equals(Partner.PARTNER_TYPE_ALL)) {
                partner = p;
                break;
            }
        }
        
        return partner;
    }
}
