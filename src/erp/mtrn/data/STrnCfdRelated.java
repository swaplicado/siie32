/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.lib.table.STableRow;
import java.util.ArrayList;

/**
 *
 * @author Isabel Servín
 */
public class STrnCfdRelated implements java.io.Serializable {
    
    private final ArrayList<SRowRelatedDocument> moRelatedDocuments;
    
    public STrnCfdRelated() {
        moRelatedDocuments = new ArrayList<>();
    }
    
    public ArrayList<SRowRelatedDocument> getRelatedDocuments() { return moRelatedDocuments; }
    
    public void addRelatedDocument(String typeRelation, String uuid) {
        boolean found = false;
        for(SRowRelatedDocument rd : moRelatedDocuments) {
            if (rd.getRelationTypeId().equals(typeRelation) && !found) {
                for (String u : rd.getDocUuids().trim().split(",")) {
                    if (u.equals(uuid)) {
                        found = true;
                    }
                }
                if (!found) {
                   rd.setDocUuids(rd.getDocUuids() + "," + uuid);
                   found = true;
                }
            }
        }
        if (!found) {
            moRelatedDocuments.add(new SRowRelatedDocument(typeRelation, uuid));
        }
    }
    
    public String getAsString() {
        String string = "";
        
        for (STableRow row : moRelatedDocuments) {
            string += string.isEmpty() ? "" : "\n";
            string += ((SRowRelatedDocument) row).getAsString();
        }
        
        return string;
    }
}
