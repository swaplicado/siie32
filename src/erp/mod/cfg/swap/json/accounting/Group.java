/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.json.accounting;

import java.util.List;

/**
 *
 * @author Sergio Flores
 */
public class Group {

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
}
